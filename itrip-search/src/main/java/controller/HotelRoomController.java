package controller;

import dto.Dto;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.ItripHotelRoom;
import service.HotelService;
import utils.DtoUtil;
import utils.ErrorCode;
import vo.hotelroom.SearchHotelRoomVO;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/search")
public class HotelRoomController {
    @Resource
    private HotelService hotelService;
    @RequestMapping(value = "/searchHotelRoom",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "查询酒店房间的信息",notes = "使用json 格式返回hotel数据集合")
    public Dto searchHotelRoomType(SearchHotelRoomVO searchHotelRoomVO){
        List<ItripHotelRoom> list=hotelService.selHotelRoomList(searchHotelRoomVO);
        if(list.size()>0){
            return DtoUtil.returnDataSuccess(list);
        }else{
            return DtoUtil.returnFail("查询房间信息失败", ErrorCode.AUTH_UNKNOWN);
        }
    }
}
