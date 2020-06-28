package controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.User;
import service.UserService;

import java.util.List;

@Controller
@RequestMapping(value = "/user")
public class AuthController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getalluser.html",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value ="显示所有用户信息",notes = "使用json 格式返回用户列表信息")
    public Object getAllUser(){
        User user=new User();
        List<User> list=userService.getAllUser(user);
        return JSONArray.toJSONString(list);
    }
    @RequestMapping(value = "/sendmail.html",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value ="发送用户激活码",notes = "使用json 格式返回发送状态")
    public Object sendmail(User user){
        JSONObject obj=new JSONObject();
        try {
            userService.itriptxCreatUser(user);
        }catch (Exception e){
            e.printStackTrace();
            obj.put("message","后台出错");
            return obj.toString();
        }
        obj.put("message","发送成功");
        return obj.toString();
    }
}
