package dao.user;

import pojo.User;

import java.util.List;

public interface UserDao {
    public List<User> getUserListByUser(User user); //根据条件获得用户数据集合
    public int insertUser(User user); //插入用户
    public int updateUser(User user); //更新用户
}
