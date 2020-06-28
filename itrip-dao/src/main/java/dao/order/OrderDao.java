package dao.order;

import org.apache.ibatis.annotations.Param;
import pojo.ItripHotelOrder;

public interface OrderDao {
    //新增订单
    public int insertHotelOrder(ItripHotelOrder itripHotelOrder);
    //根据订单编码获取用户订单信息
    public ItripHotelOrder selHotelOrder(@Param("orderNo")String orderNo,@Param("uid")int uid);
    //更新订单
    public int modifyHotelOrder(ItripHotelOrder itripHotelOrder);
}
