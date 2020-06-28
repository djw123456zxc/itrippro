package service;

import pojo.User;

public interface TokenService {
    public String createToken(String userAgent, User user) throws Exception; //创建token
    public void  saveToken(String token,User user) throws Exception; //保存token进redis
    public boolean validate(String userAgent,String token)throws Exception;
    public boolean delToken(String token)throws Exception;//删除token
    public String reloadToken(String userAgent,String token)throws Exception; //重置token
}
