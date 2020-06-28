package controller;

import dto.Dto;
import io.swagger.annotations.ApiOperation;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.ItripHotelOrder;
import pojo.ItripHotelRoom;
import pojo.LinkUser;
import service.OrderService;
import service.RoomService;
import utils.*;
import vo.order.ItripAddHotelOrderVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Resource
    private RoomService roomService;
    @Resource
    private OrderService orderService;
    @RequestMapping(value = "/addhotelorder", method = RequestMethod.POST, produces = "application/json",headers = "token")
    @ResponseBody
    @ApiOperation(value ="添加订单",notes = "使用json 格式返回发送状态")
    public Dto addHotelOrder(ItripAddHotelOrderVO itripAddHotelOrderVO, HttpServletRequest request) {
        String useragent=request.getHeader("user-agent");
        String token=request.getHeader("token");
        int hid=itripAddHotelOrderVO.getHotelId().intValue();
        int rid=itripAddHotelOrderVO.getRoomId().intValue();
        int days=0;
        try {
            System.out.println("入住日期" + itripAddHotelOrderVO.getCheckInDate());
            System.out.println("退房日期" + itripAddHotelOrderVO.getCheckOutDate());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(sdf.format(itripAddHotelOrderVO.getCheckInDate()));
            Date end = sdf.parse(sdf.format(itripAddHotelOrderVO.getCheckOutDate()));
            if(end.getTime()>start.getTime()) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(start);
                long time1 = cal.getTimeInMillis();
                cal.setTime(end);
                long time2 = cal.getTimeInMillis();
                long between_days = (time2 - time1) / (1000 * 3600 * 24);
                days = Integer.parseInt(String.valueOf(between_days));
            }else{
                return DtoUtil.returnFail("入住退房时间有误,请确保用户入住时间大于1天",ErrorCode.AUTH_UNKNOWN);
            }
            System.out.println("入住时间为"+days);
            int min=roomService.getminTempSotre(hid,rid,start,end);
            System.out.println("剩余最小库存为"+min);
            if(min>0 && min>=itripAddHotelOrderVO.getCount()){
                // 获取订单联系人信息
                LinkUser linkUser = itripAddHotelOrderVO.getLinkUser();
                linkUser.setLinkUserName(new String(linkUser.getLinkUserName().getBytes("ISO-8859-1"),"UTF-8"));
                // 创建订单对象，用于添加订单信息到表中
                ItripHotelOrder itripHotelOrder = new ItripHotelOrder();
                itripHotelOrder.setOrderType(itripAddHotelOrderVO.getOrderType());
                itripHotelOrder.setHotelId(itripAddHotelOrderVO.getHotelId());
                itripHotelOrder.setRoomId(itripAddHotelOrderVO.getRoomId());
                itripHotelOrder.setCount(itripAddHotelOrderVO.getCount());
                itripHotelOrder.setHotelName(new String(itripAddHotelOrderVO.getHotelName().getBytes("ISO-8859-1"),"UTF-8"));
                itripHotelOrder.setLinkUserName(linkUser.getLinkUserName());
                itripHotelOrder.setBookingDays(days);
                itripHotelOrder.setCheckInDate(start);
                itripHotelOrder.setCheckOutDate(end);
                itripHotelOrder.setOrderType(1);
                //支付之前生成的订单的初始状态为未支付
                itripHotelOrder.setOrderStatus(0);
                //生成订单号：机器码 +日期+（MD5）（商品IDs+毫秒数+1000000的随机数）
                StringBuilder md5String = new StringBuilder();
                md5String.append(itripHotelOrder.getHotelId());// 获取酒店ID
                md5String.append(itripHotelOrder.getRoomId()); // 获取房间ID
                md5String.append(System.currentTimeMillis());
                md5String.append(Math.random() * 1000000);
                String md5 = MD5.getMd5(md5String.toString(), 6);
                System.out.println("订单码为"+md5);
                //生成订单编号
                SystemConfig systemConfig=new SystemConfig();
                StringBuilder orderNo = new StringBuilder();
                orderNo.append(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
                orderNo.append(md5);
                itripHotelOrder.setOrderNo(orderNo.toString());
                System.out.println("订单编号为"+orderNo.toString());
                //计算订单的总金额
                List<ItripHotelRoom> list=roomService.getRoomList(0,rid);
                ItripHotelRoom itripHotelRoom=list.get(0);
                int count=itripAddHotelOrderVO.getCount();
                double price=itripHotelRoom.getRoomPrice().doubleValue();
                System.out.println("房间单价为"+itripHotelRoom.getRoomPrice());
                itripHotelOrder.setPayAmount(BigDecimal.valueOf(days*count*price));
                if(orderService.insertHotelOrder(itripHotelOrder,linkUser,useragent,token)){
                    Map<String, String> map=new HashMap<String, String>();
                    map.put("orderNo",orderNo.toString());
                    roomService.modifyTempStroecount(hid,rid,start,end,orderNo.toString(),true);
                    orderlist.add(orderNo.toString());
                    ordernum.add(0);
                    return DtoUtil.returnSuccess("生成订单成功",map);
                }
            }else{
                return DtoUtil.returnFail("房间库存不足", ErrorCode.AUTH_UNKNOWN);
            }
        }catch (Exception e){
            e.printStackTrace();
            return  DtoUtil.returnFail("接口出现异常",ErrorCode.AUTH_UNKNOWN);
        }
        return  null;
    }
    //订单号循坏次数
    private  int num=0;
    //订单号次数
    private  List<Integer> ordernum=new ArrayList<Integer>();
    //订单号
    private List<String> orderlist=new ArrayList<String>();
    @Scheduled(cron = " 0 0/1 * * * ?")
    public void flushCancelOrderStatus() {
       try{
           for(int i=0;i<orderlist.size();i++){
               ordernum.set(i,ordernum.get(i)+1);
               if(ordernum.get(i)==3){
                   ItripHotelOrder itripHotelOrder=orderService.selHotelOrder(orderlist.get(i),0);
                   if(itripHotelOrder.getOrderStatus()==0){
                       itripHotelOrder.setModifyDate(new Date());
                       itripHotelOrder.setOrderStatus(1);
                       orderService.modifyHotelOrder(itripHotelOrder);
                       roomService.modifyTempStroecount(itripHotelOrder.getHotelId().intValue(),itripHotelOrder.getRoomId().intValue(),itripHotelOrder.getCheckInDate(),itripHotelOrder.getCheckOutDate(),itripHotelOrder.getOrderNo(),false);
                   }
                   orderlist.remove(i);
                   ordernum.remove(i);
                   i=i-1;
               }
           }
       }catch (Exception e){
        e.printStackTrace();
       }
    }

}
