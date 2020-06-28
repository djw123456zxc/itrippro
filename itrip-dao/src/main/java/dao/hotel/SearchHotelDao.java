package dao.hotel;

import org.apache.ibatis.annotations.Param;
import pojo.ItripComment;
import pojo.ItripHotelRoom;
import vo.comment.ItripSearchCommentVO;
import vo.hotelroom.SearchHotelRoomVO;

import java.util.List;

public interface SearchHotelDao {
    //根据酒店房间vo获取酒店房间集合
    public List<ItripHotelRoom> selHotelRoomList(SearchHotelRoomVO searchHotelRoomVO);
    //根据酒店vo获取酒店评论集合
    public List<ItripComment> selHotelCommentList(ItripSearchCommentVO itripSearchCommentVO);
}
