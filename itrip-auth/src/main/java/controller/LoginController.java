package controller;

import dto.Dto;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.User;
import service.TokenService;
import service.UserService;
import utils.DtoUtil;
import utils.EmptyUtils;
import utils.ErrorCode;
import utils.MD5;
import vo.ItripTokenVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@Controller
@RequestMapping("/api")
public class LoginController {
    @Resource
    private TokenService tokenService;
    @Resource
    private UserService userService;
    @RequestMapping(value = "/doLogin",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    @ApiOperation(value ="用户登陆",notes = "使用json 格式返回发送状态")
    public Dto login(@RequestParam String userCode, @RequestParam String UserPass, HttpServletRequest request){
        try {
            User user = userService.login(userCode, MD5.getMd5(UserPass, 32));
            if(EmptyUtils.isNotEmpty(user)){
                String userAgent=request.getHeader("user-agent");
                String token=tokenService.createToken(userAgent,user);
                tokenService.saveToken(token,user);
                ItripTokenVo vo=new ItripTokenVo(token,Calendar.getInstance().getTimeInMillis()+2*60*60*1000, Calendar.getInstance().getTimeInMillis());
                return DtoUtil.returnDataSuccess(vo);
            }else{
                return DtoUtil.returnFail("用户密码错误", ErrorCode.AUTH_AUTHENTICATION_FAILED);
            }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(), ErrorCode.AUTH_AUTHENTICATION_FAILED);
        }
    }
    @RequestMapping(value = "/loginOut",method = RequestMethod.GET,produces = "application/json",headers = "token")
    @ResponseBody
    @ApiOperation(value ="用户退出",notes = "使用json 格式返回发送状态")
    public Dto loginout(HttpServletRequest request){
        String token=request.getHeader("token");
        try {
            if (tokenService.validate(request.getHeader("user-agent"),token)) {
                tokenService.delToken(token);
                return DtoUtil.returnSuccess();
            }else{
                return DtoUtil.returnFail("token无效",ErrorCode.AUTH_TOKEN_INVALID);
            }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("退出失败",ErrorCode.AUTH_TOKEN_INVALID);
        }
    }
}
