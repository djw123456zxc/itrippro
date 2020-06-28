package controller;

import dto.Dto;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.ItripHotelTempStore;
import pojo.ItripProductStore;
import service.RoomService;
import utils.DtoUtil;
import utils.ErrorCode;
import vo.order.ValidateRoomStoreVO;
import vo.store.StoreVO;

import javax.annotation.Resource;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/room")
public class RoomContoller {
    @Resource
    private RoomService roomService;
    @RequestMapping(value = "/ValidateRoom",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "验证酒店库存是否存足",notes = "使用json 格式返回hotel数据集合")
    public Dto ValidateRoom(ValidateRoomStoreVO validateRoomStoreVO){
        List<StoreVO> list=new ArrayList<StoreVO>();
        int hid=validateRoomStoreVO.getHotelId().intValue();
        int rid=validateRoomStoreVO.getRoomId().intValue();
        System.out.println("入住日期"+validateRoomStoreVO.getCheckInDate());
        System.out.println("退房日期"+validateRoomStoreVO.getCheckOutDate());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date start = sdf.parse(sdf.format(validateRoomStoreVO.getCheckInDate()));
            Date end = sdf.parse(sdf.format(validateRoomStoreVO.getCheckOutDate()));
            Date temp = start;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            while(temp.getTime()<=end.getTime()){
                System.out.println("进入while循坏判断临时库存是否满足");
                temp = calendar.getTime();
                Date daydate=sdf.parse(sdf.format(temp));
                System.out.println("入住日期是"+temp);
                ItripHotelTempStore tempStore=roomService.getTempStoreList(hid,rid,daydate);
                if(tempStore==null){
                    System.out.println("临时库存为空");
                    ItripProductStore productStore=roomService.getProductStoreByid(rid);
                    tempStore=new ItripHotelTempStore();
                    tempStore.setHotelId(hid);
                    tempStore.setRoomId((long)rid);
                    tempStore.setRecordDate(temp);
                    tempStore.setStore(productStore.getStore());
                    tempStore.setCreationDate(new Date());
                    int num=roomService.insertTempStore(tempStore);
                    if(num>0){
                        tempStore=roomService.getTempStoreList(hid,rid,temp);
                    }else{
                        return DtoUtil.returnFail("插入临时库存失败", ErrorCode.AUTH_UNKNOWN);
                    }
                }
                System.out.println("当天库存为"+tempStore.getStore());
                if(tempStore.getStore()>validateRoomStoreVO.getCount()) {
                    StoreVO storeVO=new StoreVO();
                    storeVO.setRoomId((long)rid);
                    storeVO.setStore(tempStore.getStore());
                    storeVO.setDate(daydate);
                    list.add(storeVO);
                }
                calendar.add(Calendar.DAY_OF_MONTH,1);
                if(temp.getTime()==end.getTime()){
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(list.size()>0){
            return  DtoUtil.returnDataSuccess(list);
        }else{
            return  DtoUtil.returnSuccess("入住时间内没有库存房间");
        }
    }
}
