package service;

import com.alibaba.fastjson.JSON;
import dao.order.OrderDao;
import org.springframework.stereotype.Service;
import pojo.ItripHotelOrder;
import pojo.User;
import utils.MD5;
import utils.RedisAPI;

import javax.annotation.Resource;

@Service("OrderService")
public class OrderServiceImpl implements OrderService{
    @Resource
    private OrderDao orderDao;
    @Resource
    private RedisAPI redisAPI;
    @Override
    public ItripHotelOrder getHotelOrderByOrderNo(String OrderNo, String token,String userAgent) {
        ItripHotelOrder itripHotelOrder=null;
        try{
            if(!redisAPI.exists(token)){
                return null;
            }
            String agentMD5=token.split("-")[4];
            if(!MD5.getMd5(userAgent,6).equals(agentMD5)){
                return null;
            }
            User user= JSON.parseObject(redisAPI.get(token),User.class);
            System.out.println(OrderNo+user.getId());
            itripHotelOrder=orderDao.selHotelOrder(OrderNo,user.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        return itripHotelOrder;
    }

    @Override
    public int modifyHotelOrder(ItripHotelOrder itripHotelOrder) {
        int num=0;
        try{
            num=orderDao.modifyHotelOrder(itripHotelOrder);
        }catch (Exception e){
            e.printStackTrace();
        }
        return num;
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
}
