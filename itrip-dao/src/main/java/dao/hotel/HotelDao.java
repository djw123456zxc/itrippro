package dao.hotel;

import org.apache.ibatis.annotations.Param;
import pojo.ItripHotelRoom;
import vo.hotel.HotelVo;
import vo.hotel.SearchHotelVO;


import java.util.List;

public interface HotelDao {
    //根据酒店条件查询酒店集合
    public List<HotelVo> queryHotelList(SearchHotelVO searchHotelVO);
}
