package service;

import org.apache.ibatis.annotations.Param;
import pojo.ItripHotelOrder;

public interface OrderService {
    //获得订单对象
    public ItripHotelOrder getHotelOrderByOrderNo(String OrderNo,String token,String userAgent);
    //修改订单对象
    public int modifyHotelOrder(ItripHotelOrder itripHotelOrder);
    //根据订单编码获取用户订单信息
    public ItripHotelOrder selHotelOrder(@Param("orderNo")String orderNo, @Param("uid")int uid);
}
