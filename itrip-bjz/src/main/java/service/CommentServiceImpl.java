package service;

import com.alibaba.fastjson.JSON;
import dao.comment.CommentDao;
import org.springframework.stereotype.Service;
import pojo.ItripComment;
import pojo.User;
import utils.MD5;
import utils.RedisAPI;

import javax.annotation.Resource;
import java.util.Date;

@Service("CommentService")
public class CommentServiceImpl implements  CommentService{
    @Resource
    private RedisAPI redisAPI;
    @Resource
    private CommentDao commentDao;
    @Override
    public boolean addComment(ItripComment itripComment,String userAgent, String token) {
        int num=0;
        try{
            if(!redisAPI.exists(token)){
                return false;
            }
            String agentMD5=token.split("-")[4];
            if(!MD5.getMd5(userAgent,6).equals(agentMD5)){
                return false;
            }
            User user= JSON.parseObject(redisAPI.get(token),User.class);
            itripComment.setUserId((long)user.getId());
            itripComment.setCreatedBy((long)user.getId());
            itripComment.setCreationDate(new Date());
            num=commentDao.addComment(itripComment);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(num>0){
            return true;
        }else{
            return false;
        }
    }
}
