package service;

import dao.linkuser.LinkUserDao;
import pojo.ItripOrderLinkUser;
import pojo.LinkUser;

import javax.annotation.Resource;

public class LinkUserServiceImpl implements LinkUserService{
    @Resource
    private LinkUserDao linkUserDao;
    @Override
    public int insertLinkUser(LinkUser linkUser) {
        int num=0;
        try{
            num=linkUserDao.insertLinkUser(linkUser);
        }catch (Exception e){
            e.printStackTrace();
        }
        return num;
    }

    @Override
    public LinkUser selLinkUserByIdCard(String idCard) {
        LinkUser linkUser=null;
        try {
            linkUser=linkUserDao.selLinkUserByIdCard(idCard);
        }catch (Exception e){
            e.printStackTrace();
        }
        return linkUser;
    }

    @Override
    public int insertOrderLinkUser(ItripOrderLinkUser itripOrderLinkUser) {
        int num=0;
        try{
            num=linkUserDao.insertOrderLinkUser(itripOrderLinkUser);
        }catch (Exception e){
            e.printStackTrace();
        }
        return num;
    }
}
