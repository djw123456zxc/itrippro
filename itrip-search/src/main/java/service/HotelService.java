package service;

import org.apache.ibatis.annotations.Param;
import pojo.ItripComment;
import pojo.ItripHotelRoom;
import vo.comment.ItripSearchCommentVO;
import vo.hotel.HotelVo;
import vo.hotel.SearchHotelVO;
import vo.hotelroom.SearchHotelRoomVO;

import java.util.List;

public interface HotelService{
    //根据酒店条件查询酒店集合
    public List<HotelVo> searchHotel(SearchHotelVO searchHotelVO);
    //根据酒店id获取酒店房间集合
    public List<ItripHotelRoom> selHotelRoomList(SearchHotelRoomVO searchHotelRoomVO);
    //根据酒店id获取酒店评论集合
    public List<ItripComment> selHotelCommentList(ItripSearchCommentVO itripSearchCommentVO);
}
