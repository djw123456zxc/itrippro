package service;

import com.alibaba.fastjson.JSON;
import nl.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;
import pojo.User;
import utils.MD5;
import utils.RedisAPI;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service("TokenService")
public class TokenServiceImpl implements TokenService{
    @Resource
    private RedisAPI redisAPI;
    @Override
    public String createToken(String userAgent, User user) throws Exception {
        StringBuilder str=new StringBuilder();
        str.append("token:");
        UserAgent agent=UserAgent.parseUserAgentString(userAgent);
        if(agent.getOperatingSystem().isMobileDevice()){
            str.append("MOBILE-");
        }else{
            str.append("PC-");
        }
        str.append(MD5.getMd5(user.getUserCode(),32)+"-");
        str.append(user.getId()+"-");
        str.append(new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date())+"-");
        str.append(MD5.getMd5(userAgent,6));
        return str.toString();
    }

    @Override
    public void saveToken(String token,User user) throws Exception {
        if(token.startsWith("token:PC-")){
            redisAPI.set(token, JSON.toJSONString(user),2*60*60);
        }else{
            redisAPI.set(token,JSON.toJSONString(user));
        }
    }

    @Override
    public boolean validate(String userAgent, String token) throws Exception {
        if(!redisAPI.exists(token)){
            return false;
        }
        String agentMD5=token.split("-")[4];
        if(!MD5.getMd5(userAgent,6).equals(agentMD5)){
            return false;
        }
        return true;
    }

    @Override
    public boolean delToken(String token) throws Exception {
        long num=redisAPI.del(token);
        if(num>0){
            return true;
        }else {
            return false;
        }
    }

    private long protectedTime=30*60;
    private int delay=2*60;
    @Override
    public String reloadToken(String userAgent, String token) throws Exception {
        //1.验证token是否有效
        if(!redisAPI.exists(token)){
            throw new Exception("token无效");
        }
        //2.能不能够置换
        Date genTime=new SimpleDateFormat("yyyyMMddHHmmsss").parse(token.split("-")[3]);
        long passed= Calendar.getInstance().getTimeInMillis()-genTime.getTime();
        if(passed<protectedTime*1000){
            throw new Exception("token处于置换保护期内,不能置换,剩余"+(protectedTime*1000-passed)/1000);
        }
        //3，进行置换
        String userjson=redisAPI.get(token);
        User user=JSON.parseObject(userjson,User.class);
        String newToken=this.createToken(userAgent, user);
        //4.旧token延时过期
        redisAPI.set(token,userjson,delay);
        //5.新token存入redis
        this.saveToken(newToken,user);
        return newToken;
    }
}
