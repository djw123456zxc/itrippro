package service;

import com.alibaba.fastjson.JSON;
import dao.linkuser.LinkUserDao;
import dao.order.OrderDao;
import nl.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;
import pojo.ItripHotelOrder;
import pojo.ItripOrderLinkUser;
import pojo.LinkUser;
import pojo.User;
import utils.MD5;
import utils.RedisAPI;

import javax.annotation.Resource;
import java.util.Date;

@Service("OrderService")
public class OrderServiceImpl implements OrderService{
    @Resource
    private OrderDao orderDao;
    @Resource
    private RedisAPI redisAPI;
    @Resource
    private LinkUserDao linkUserDao;
    @Override
    public boolean insertHotelOrder(ItripHotelOrder itripHotelOrder, LinkUser linkUser, String userAgent, String token) {
        int num=0;
        try{
            if(!redisAPI.exists(token)){
                return false;
            }
            String agentMD5=token.split("-")[4];
            if(!MD5.getMd5(userAgent,6).equals(agentMD5)){
                return false;
            }
            UserAgent agent=UserAgent.parseUserAgentString(userAgent);
            if(agent.getOperatingSystem().isMobileDevice()){
               itripHotelOrder.setBookType(1);
            }else{
                itripHotelOrder.setBookType(0);
            }
            User user= JSON.parseObject(redisAPI.get(token),User.class);
            itripHotelOrder.setUserId((long)user.getId());
            itripHotelOrder.setCreatedBy((long)user.getId());
            itripHotelOrder.setCreationDate(new Date());
            num=orderDao.insertHotelOrder(itripHotelOrder);
            if(num>0){
                ItripHotelOrder Order=orderDao.selHotelOrder(itripHotelOrder.getOrderNo(),user.getId());
                LinkUser oldlinkUser=linkUserDao.selLinkUserByIdCard(linkUser.getLinkIdCard());
                if(oldlinkUser==null){
                    linkUser.setCreationDate(new Date());
                    linkUser.setCreatedBy((long)user.getId());
                    linkUser.setUserId((long)user.getId());
                    num=linkUserDao.insertLinkUser(linkUser);
                    if(num>0){
                        oldlinkUser=linkUserDao.selLinkUserByIdCard(linkUser.getLinkIdCard());
                    }
                }
                ItripOrderLinkUser itripOrderLinkUser=new ItripOrderLinkUser();
                itripOrderLinkUser.setLinkUserId(oldlinkUser.getId());
                itripOrderLinkUser.setOrderId(Order.getId());
                itripOrderLinkUser.setLinkUserName(oldlinkUser.getLinkUserName());
                itripOrderLinkUser.setCreatedBy((long)user.getId());
                itripOrderLinkUser.setCreationDate(new Date());
                num=linkUserDao.insertOrderLinkUser(itripOrderLinkUser);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(num>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public ItripHotelOrder selHotelOrder(String orderNo, int uid) {
        ItripHotelOrder itripHotelOrder=null;
        try{
            itripHotelOrder=orderDao.selHotelOrder(orderNo,uid);
        }catch (Exception e){
            e.printStackTrace();
        }
        return itripHotelOrder;
    }

    @Override
    public int modifyHotelOrder(ItripHotelOrder itripHotelOrder) {
        int num=0;
        try{
            orderDao.modifyHotelOrder(itripHotelOrder);
        }catch (Exception e){
            e.printStackTrace();
        }
        return num;
    }
}
