package service;

import dao.order.OrderDao;
import dao.room.RoomDao;
import org.springframework.stereotype.Service;
import pojo.ItripHotelOrder;
import pojo.ItripHotelTempStore;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service("RoomSerivce")
public class RoomServiceImpl implements RoomService{
    @Resource
    private RoomDao roomDao;
    @Resource
    private OrderDao orderDao;
    @Override
    public int modifyTempStroecount(int hotelId, int roomId, Date start, Date end,String orderNo, boolean bool) {
        int num=0;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date temp = start;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            while(temp.getTime()<=end.getTime()){
                System.out.println("进入while循坏锁定库存");
                temp = calendar.getTime();
                Date daydate=sdf.parse(sdf.format(temp));
                System.out.println("入住日期是"+temp);
                ItripHotelTempStore tempStore=roomDao.getTempStoreList(hotelId,roomId,daydate);
                ItripHotelOrder itripHotelOrder=orderDao.selHotelOrder(orderNo,0);
                if(bool) {
                    num = roomDao.modifyTempStroecount(hotelId, roomId, daydate, tempStore.getStore() - itripHotelOrder.getCount());
                }else{
                    num = roomDao.modifyTempStroecount(hotelId, roomId, daydate, tempStore.getStore() + itripHotelOrder.getCount());
                }
                calendar.add(Calendar.DAY_OF_MONTH,1);
                if(temp.getTime()==end.getTime()){
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return num;
    }
}
