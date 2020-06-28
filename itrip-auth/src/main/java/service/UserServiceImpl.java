package service;

import dao.user.UserDao;
import org.springframework.stereotype.Service;
import pojo.User;
import utils.MD5;
import utils.RedisAPI;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("UserService")
public class UserServiceImpl implements UserService{
    @Resource
    private UserDao userDao;
    @Resource
    private MailService mailService;
    @Resource
    private RedisAPI redisAPI;
    @Resource
    private SmsService smsService;
    @Override
    public List<User> getAllUser(User user) {
        List<User> list=new ArrayList<User>();
        try{
            list=userDao.getUserListByUser(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void itriptxCreatUser(User user) throws Exception{
        //1.添加用户信息
        userDao.insertUser(user);
        //2.生成激活码
        String activationCode= MD5.getMd5(new Date().toLocaleString(),32);
        //3.发送邮件
        mailService.sendActivationMail(user.getUserCode(),activationCode);
        //4.激活码存入Redis
        redisAPI.set("activation:"+user.getUserCode(),activationCode,30*60);
    }

    @Override
    public boolean activate(String mail, String code) throws Exception {
        //1.验证激活码
        String value=redisAPI.get("activation:"+mail);
        if(value.equals(code)){
            User user=new User();
            user.setUserCode(mail);
            List<User> list=userDao.getUserListByUser(user);
            if(list.size()>0){
                //2.更新用户
                user=list.get(0);
                user.setActivated(1);
                user.setUserType(0);
                user.setFlatID(user.getId());
                userDao.updateUser(user);
                return  true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    @Override
    public User getUserByUserCode(String userCode) throws Exception {
        User user=new User();
        user.setUserCode(userCode);
        try{
            List<User> list=userDao.getUserListByUser(user);
            if(list.size()>0){
                return list.get(0);
            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void itriptxCreatUserByPhone(User user) throws Exception {
        //1.创建用户
        userDao.insertUser(user);
        //2.生成验证码(1111-9999)
        int code=MD5.getRandomCode();
        //3.发送验证码
        smsService.SendTo(user.getUserCode(),"1",new String[]{String.valueOf(code),"1"});
        //4.缓存验证码到redis
        redisAPI.set("activation:"+user.getUserCode(),String.valueOf(code),60);
    }

    @Override
    public boolean activateByPhone(String phone, String code) throws Exception {
        //1.验证激活码
        String value=redisAPI.get("activation:"+phone);
        if(null!=value&&value.equals(code)){
            User user=new User();
            user.setUserCode(phone);
            List<User> list=userDao.getUserListByUser(user);
            if(list.size()>0){
                //2.更新用户
                user=list.get(0);
                user.setActivated(1);
                user.setUserType(0);
                user.setFlatID(user.getId());
                userDao.updateUser(user);
                return  true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    @Override
    public User login(String userCode, String userPass) throws Exception {
        User user=new User();
        user.setUserCode(userCode);
        user.setUserPassword(userPass);
        List<User> list=userDao.getUserListByUser(user);
        if(list.size()>0){
            user=list.get(0);
            if(user.getActivated()!=1){
                  throw new Exception("用户未激活");
            }else{
                return user;
            }
        }else{
            return null;
        }
    }
}
