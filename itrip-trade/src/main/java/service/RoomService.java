package service;

import java.util.Date;

public interface RoomService {
    //修改当天临时库存
    public int modifyTempStroecount(int hotelId, int roomId, Date start, Date end, String orderNo,boolean bool);
}
