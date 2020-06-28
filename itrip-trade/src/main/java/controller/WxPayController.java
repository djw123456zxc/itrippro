package controller;


import config.WxPayUtil;
import config.WxRequestParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import pojo.ItripHotelOrder;
import service.OrderService;
import service.RoomService;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Date;


@Controller
@RequestMapping("/api")
public class WxPayController {

    @Resource
    private OrderService orderService;
    @Resource
    private RoomService roomService;

    @RequestMapping(value = "/wxpay/{orderNo}", method = RequestMethod.GET)
    public String wxPay(@PathVariable String orderNo, ModelMap model) {
        try {
            //根据订单号查询订单信息
            ItripHotelOrder order = orderService.selHotelOrder(orderNo,0);
            if(order.getOrderStatus()!=0){
                return "notfound";
            }
            if (order!=null) {
                model.addAttribute("orderNo", order.getOrderNo());
                model.addAttribute("body", order.getHotelName());
                model.addAttribute("money", order.getPayAmount());
                return "index";
            }else {
                return "notfound";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error";
        }

    }
    /**
     * 从页面获取参数
     *  @param orderNo 订单号
     * @param body 描述
     * @param money 金额
     * @param session
     * @return
     */
    @RequestMapping(value = "/pay")
    @ApiOperation(value = "微信支付请求")
    @ResponseBody
    public String pay(String orderNo, String body, String money, HttpSession session) {
        WxRequestParam param = new WxRequestParam();
        try {
            body=new String(body.getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ItripHotelOrder itripHotelOrder=orderService.selHotelOrder(orderNo,0);
        if(itripHotelOrder.getOrderStatus()==1){
            return "订单已取消,无法支付";
        }
        param.setBody(body);
        param.setTotal_fee(money);
        param.setOut_trade_no(String.valueOf(System.currentTimeMillis()));
        session.setAttribute("tradeNo", param.getOut_trade_no());   //保存交易号
        session.setAttribute("orderNo",orderNo);                    //保存订单号
        String codeUrl= WxPayUtil.getCodeUrl(param);
        return codeUrl;
    }

    private int time=0;
    /**
     * 验证支付方法
     * @param session
     * @return
     */
    @RequestMapping(value = "/query")
    @ApiOperation(value = "验证支付请求")
    @ResponseBody
    public Object queryOrderState(HttpSession session) {
        time+=5;
        System.out.println("时间"+time);
        String orderId=(String) session.getAttribute("tradeNo");
        WxRequestParam param = new WxRequestParam();
        param.setOut_trade_no(orderId);
        boolean result=WxPayUtil.queryOrderState(param);    //支付成功返回true
        if(result){
            time=0;
        }
        if(time==60){
            time=0;
           return "timeout";
        }
        return result;
    }


    /**
     * 支付成功调用
     * @return
     */
    @RequestMapping("success")
    @ApiOperation(value = "支付成功")
    public String success(HttpSession session,ModelMap model) throws Exception {
        String orderNo=(String) session.getAttribute("orderNo");
        String tradeNo=(String)session.getAttribute("tradeNo");
        ItripHotelOrder itripHotelOrder=orderService.selHotelOrder(orderNo,0);
        itripHotelOrder.setTradeNo(tradeNo);
        itripHotelOrder.setOrderStatus(2);
        itripHotelOrder.setPayType(2);
        itripHotelOrder.setModifyDate(new Date());
        orderService.modifyHotelOrder(itripHotelOrder);
        model.addAttribute("orderNo",orderNo);
        return "success";
    }

    /**
     *
     * @return
     */
    @RequestMapping("notice")
    @ApiOperation(value = "通知")
    public String notice(HttpSession session) {
        String orderNo=(String) session.getAttribute("orderNo");
        ItripHotelOrder itripHotelOrder=orderService.selHotelOrder(orderNo,0);
        roomService.modifyTempStroecount(itripHotelOrder.getHotelId().intValue(),itripHotelOrder.getRoomId().intValue(),itripHotelOrder.getCheckInDate(),itripHotelOrder.getCheckOutDate(),orderNo,false);
        return "notice";
    }
}
