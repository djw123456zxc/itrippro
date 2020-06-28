package service;

import pojo.User;

import java.util.List;

public interface UserService {
    public List<User> getAllUser(User user); //获得所有用户信息
    public void itriptxCreatUser(User user) throws Exception; //邮箱增加用户
    public boolean activate(String mail,String code)throws Exception; //邮箱激活用户
    public User getUserByUserCode(String userCode)throws Exception;//根据用户注册名查找用户
    public void itriptxCreatUserByPhone(User user) throws Exception; //手机增加用户
    public  boolean activateByPhone(String phone,String code)throws Exception;//手机激活用户
    public User login(String userCode,String userPass)throws  Exception;//用户登陆
}
