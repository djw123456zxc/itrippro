package service;

import dao.order.OrderDao;
import dao.room.RoomDao;
import org.springframework.stereotype.Service;
import pojo.ItripHotelOrder;
import pojo.ItripHotelRoom;
import pojo.ItripHotelTempStore;
import pojo.ItripProductStore;
import utils.DtoUtil;
import utils.ErrorCode;
import vo.store.StoreVO;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("RoomService")
public class RoomServiceImpl implements RoomService{
    @Resource
    private RoomDao roomDao;
    @Resource
    private OrderDao orderDao;
    @Override
    public List<ItripHotelRoom> getRoomList(int hid,int rid) {
        List<ItripHotelRoom> list=new ArrayList<ItripHotelRoom>();
        try{
            list=roomDao.getRoomListByHotelId(hid,rid);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ItripHotelTempStore getTempStoreList(int hotelId, int roomId, Date date) {
        ItripHotelTempStore tempStore=null;
        try{
            tempStore=roomDao.getTempStoreList(hotelId,roomId,date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  tempStore;
    }

    @Override
    public ItripProductStore getProductStoreByid(int roomId) {
        ItripProductStore productStore=null;
        try{
            productStore=roomDao.getProductStoreByid(roomId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return productStore;
    }

    @Override
    public int insertTempStore(ItripHotelTempStore itripHotelTempStore) {
        int num=0;
        try{
            num=roomDao.insertTempStore(itripHotelTempStore);
        }catch (Exception e){
            e.printStackTrace();
        }
        return num;
    }

    @Override
    public int getminTempSotre(int hotelId, int roomId, Date start, Date end) {
        int min=0;
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date temp = start;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start);
                while(temp.getTime()<=end.getTime()){
                    System.out.println("进入while循坏判断临时库存是否满足");
                    temp = calendar.getTime();
                    Date daydate=sdf.parse(sdf.format(temp));
                    System.out.println("入住日期是"+temp);
                    ItripHotelTempStore tempStore=roomDao.getTempStoreList(hotelId,roomId,daydate);
                    if(tempStore==null){
                        System.out.println("临时库存为空,插入一条入住当天的临时库存");
                        ItripProductStore productStore=roomDao.getProductStoreByid(roomId);
                        tempStore=new ItripHotelTempStore();
                        tempStore.setHotelId(hotelId);
                        tempStore.setRoomId((long)roomId);
                        tempStore.setRecordDate(temp);
                        tempStore.setStore(productStore.getStore());
                        tempStore.setCreationDate(new Date());
                        int num=roomDao.insertTempStore(tempStore);
                        if(num>0){
                            tempStore=roomDao.getTempStoreList(hotelId,roomId,temp);
                        }else{
                            throw new Exception("插入临时库存失败");
                        }
                    }
                    calendar.add(Calendar.DAY_OF_MONTH,1);
                    if(temp.getTime()==end.getTime()){
                        break;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            min=roomDao.getminTempSotre(hotelId,roomId,start,end);
        return min;
    }

    @Override
    public int modifyTempStroecount(int hotelId, int roomId, Date start, Date end,String orderNo,boolean bool) {
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
