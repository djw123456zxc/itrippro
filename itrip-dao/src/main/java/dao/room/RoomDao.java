package dao.room;

import org.apache.ibatis.annotations.Param;
import pojo.ItripHotelRoom;
import pojo.ItripHotelTempStore;
import pojo.ItripProductStore;

import java.util.Date;
import java.util.List;

public interface RoomDao {
    //根据酒店id获得房间集合
    public List<ItripHotelRoom> getRoomListByHotelId(@Param("hId")int hid,@Param("rId")int rid);
    //根据条件获得临时库存集合
    public ItripHotelTempStore getTempStoreList(@Param("hId")int hotelId, @Param("rId")int roomId,
                                                      @Param("Date")Date date);
    //查询房间原始库存
    public ItripProductStore getProductStoreByid(@Param("rId")int roomId);
    //增加临时库存
    public int insertTempStore(ItripHotelTempStore itripHotelTempStore);
    //根据条件查询房间最小库存
    public int getminTempSotre(@Param("hId")int hotelId, @Param("rId")int roomId,
                               @Param("inDate")Date indate,@Param("outDate")Date outdate);
    //修改当天临时库存
    public int modifyTempStroecount(@Param("hId")int hotelId, @Param("rId")int roomId,
                                                @Param("Date")Date date,@Param("Count")int count);
}
