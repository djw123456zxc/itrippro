package controller;

import dto.Dto;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import service.TokenService;
import utils.DtoUtil;
import utils.ErrorCode;
import vo.ItripTokenVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@Controller
@RequestMapping("/api")
public class TokenController {
    @Resource
    private TokenService tokenService;

    @RequestMapping(value = "/validateToken",method = RequestMethod.GET,produces = "application/json",headers = "token")
    @ResponseBody
    @ApiOperation(value ="验证token是否存在",notes = "使用json 格式返回发送状态")
    public Dto validate(HttpServletRequest request){
        try {
            Boolean result = tokenService.validate(request.getHeader("user-agent"), request.getHeader("token"));
            if(result){
                return DtoUtil.returnSuccess("token有效");
            }else{
                return DtoUtil.returnSuccess("token无效");
            }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(), ErrorCode.AUTH_UNKNOWN);
        }
    }
    @RequestMapping(value = "/reloadToken",method = RequestMethod.GET,produces = "application/json",headers = "token")
    @ResponseBody
    @ApiOperation(value ="重置用户token",notes = "使用json 格式返回发送状态")
    public Dto reloadtoken(HttpServletRequest request){
        String token;
        try {
            token=tokenService.reloadToken(request.getHeader("user-agent"),request.getHeader("token"));
            ItripTokenVo vo=new ItripTokenVo(token, Calendar.getInstance().getTimeInMillis()+2*60*60*1000, Calendar.getInstance().getTimeInMillis());
            return DtoUtil.returnDataSuccess(vo);
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail(e.getLocalizedMessage(),ErrorCode.AUTH_UNKNOWN);
        }
    }
}
