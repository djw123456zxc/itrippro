package service;

import org.apache.ibatis.annotations.Param;
import pojo.ItripHotelOrder;
import pojo.LinkUser;

public interface OrderService {
    public boolean insertHotelOrder(ItripHotelOrder itripHotelOrder, LinkUser linkUser, String userAgent, String token);
    //根据订单编码获取用户订单信息
    public ItripHotelOrder selHotelOrder(@Param("orderNo")String orderNo, @Param("uid")int uid);
    //更新订单
    public int modifyHotelOrder(ItripHotelOrder itripHotelOrder);
}
