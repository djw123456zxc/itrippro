package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dto.Dto;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.ItripComment;
import pojo.ItripHotelRoom;
import service.HotelService;
import utils.DtoUtil;
import utils.ErrorCode;
import vo.hotel.HotelVo;
import vo.hotel.SearchHotelVO;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/search")
public class HotelController {
    @Resource
    private HotelService hotelService;
    @RequestMapping(value = "/searchHotel",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "关键字查询酒店",notes = "使用json 格式返回hotel数据集合")
    public Dto searchHotel(SearchHotelVO searchHotelVO){
        List<HotelVo> list=hotelService.searchHotel(searchHotelVO);
        JSONObject object=new JSONObject();
        if(list!=null){
            System.out.println("查询酒店数据："+list.size());
        }
        object.put("data",list);
        return DtoUtil.returnDataSuccess(object);
    }
}
