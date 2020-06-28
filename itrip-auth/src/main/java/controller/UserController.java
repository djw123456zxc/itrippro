package controller;

import dto.Dto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pojo.User;
import service.UserService;
import utils.DtoUtil;
import utils.ErrorCode;
import utils.MD5;
import vo.UserVo;

import java.util.regex.Pattern;

@Controller
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    //邮箱验证
    private boolean validEmail(String email){
        String regex="^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        return Pattern.compile(regex).matcher(email).find();
    }
    //手机验证
    private boolean validPhone(String phone) {
        String regex="^1[3578]{1}\\d{9}$";
        return Pattern.compile(regex).matcher(phone).find();
    }
    @RequestMapping(value = "/registerbyemail",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    @ApiOperation(value ="邮箱注册用户",notes = "使用json 格式返回发送状态")
    public Dto doRegister(@RequestBody UserVo userVo){
        //1.邮箱验证
        if(!this.validEmail(userVo.getUserCode())){
           return DtoUtil.returnFail("请使用正确的邮箱地址", ErrorCode.AUTH_ILLEGAL_USERCODE);
        }
        User user=new User();
        user.setUserCode(userVo.getUserCode());
        try{
         if(null!=userService.getUserByUserCode(userVo.getUserCode())){
             return  DtoUtil.returnFail("用户已存在",ErrorCode.AUTH_USER_ALREADY_EXISTS);
         }else{
             user.setUserPassword(MD5.getMd5(userVo.getUserPassword(),32));
             user.setUserName(userVo.getUserName());
             userService.itriptxCreatUser(user);
             return DtoUtil.returnSuccess();
         }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("出现异常",ErrorCode.AUTH_UNKNOWN);
        }
        //return DtoUtil.returnFail("注册失败",ErrorCode.AUTH_UNKNOWN);
    }

    @RequestMapping(value = "/activatebymail",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    @ApiOperation(value ="邮箱激活用户",notes = "使用json 格式返回发送状态")
    public Dto activateUser(@RequestParam String mail,@RequestParam String code){
         try {
           if(userService.activate(mail,code)){
               return DtoUtil.returnSuccess("激活成功！");
           }else{
               return DtoUtil.returnSuccess("激活失败！");
           }
         }catch (Exception e){
            e.printStackTrace();
         }
        return DtoUtil.returnFail("激活失败",ErrorCode.AUTH_UNKNOWN);
    }
    @RequestMapping(value = "/registerbyphone",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    @ApiOperation(value ="手机注册用户",notes = "使用json 格式返回发送状态")
    public Dto RegisterbyPhone(@RequestBody UserVo userVo){
        //1.手机验证
        if(!this.validPhone(userVo.getUserCode())){
            return DtoUtil.returnFail("请使用正确的手机号码", ErrorCode.AUTH_ILLEGAL_USERCODE);
        }
        User user=new User();
        user.setUserCode(userVo.getUserCode());
        try{
            if(null!=userService.getUserByUserCode(userVo.getUserCode())){
                return  DtoUtil.returnFail("用户已存在",ErrorCode.AUTH_USER_ALREADY_EXISTS);
            }else{
                user.setUserPassword(MD5.getMd5(userVo.getUserPassword(),32));
                user.setUserName(userVo.getUserName());
                userService.itriptxCreatUserByPhone(user);
                return DtoUtil.returnSuccess();
            }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("出现异常",ErrorCode.AUTH_UNKNOWN);
        }
        //return DtoUtil.returnFail("注册失败",ErrorCode.AUTH_UNKNOWN);
    }
    @RequestMapping(value = "/activatebyphone",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    @ApiOperation(value ="手机激活用户",notes = "使用json 格式返回发送状态")
    public Dto activateUserByPhone(@RequestParam String phone,@RequestParam String code){
        try {
            if(userService.activateByPhone(phone, code)){
                return DtoUtil.returnSuccess("激活成功！");
            }else{
                return DtoUtil.returnSuccess("激活失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return DtoUtil.returnFail("激活失败",ErrorCode.AUTH_UNKNOWN);
    }
}
