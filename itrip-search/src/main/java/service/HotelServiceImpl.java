package service;

import dao.hotel.HotelDao;
import dao.hotel.HotelDaoImpl;
import dao.hotel.SearchHotelDao;
import org.springframework.stereotype.Service;
import pojo.ItripComment;
import pojo.ItripHotelRoom;
import vo.comment.ItripSearchCommentVO;
import vo.hotel.HotelVo;
import vo.hotel.SearchHotelVO;
import vo.hotelroom.SearchHotelRoomVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("HotelService")
public class HotelServiceImpl implements HotelService{
    @Resource
    private SearchHotelDao searchHotelDao;

    private HotelDao hotelDao=new HotelDaoImpl();
    @Override
    public List<HotelVo> searchHotel(SearchHotelVO searchHotelVO) {
        return hotelDao.queryHotelList(searchHotelVO);
    }

    @Override
    public List<ItripHotelRoom> selHotelRoomList(SearchHotelRoomVO searchHotelRoomVO) {
        List<ItripHotelRoom> list=new ArrayList<ItripHotelRoom>();
        try{
            list=searchHotelDao.selHotelRoomList(searchHotelRoomVO);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ItripComment> selHotelCommentList(ItripSearchCommentVO itripSearchCommentVO) {
        List<ItripComment> list=new ArrayList<ItripComment>();
        try{
            list=searchHotelDao.selHotelCommentList(itripSearchCommentVO);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
