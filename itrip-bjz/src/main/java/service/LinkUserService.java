package service;

import org.apache.ibatis.annotations.Param;
import pojo.ItripOrderLinkUser;
import pojo.LinkUser;

public interface LinkUserService {
    //增加用户相关信息
    public int insertLinkUser(LinkUser linkUser);
    //根据身份证查询用户
    public LinkUser selLinkUserByIdCard(@Param("idCard") String idCard);
    //增加用户订单关系
    public int insertOrderLinkUser(ItripOrderLinkUser itripOrderLinkUser);
}
