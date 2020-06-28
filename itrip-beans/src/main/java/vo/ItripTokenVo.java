package vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ItripTokenVo",description = "用户登陆信息")
public class ItripTokenVo {
    @ApiModelProperty("用户认证凭据")
    private String token;
    @ApiModelProperty("过期时间,单位:豪秒")
    private long expTime;
    @ApiModelProperty("生成时间,单位:豪秒")
    private long genTime;

    public ItripTokenVo(String token,long expTime,long genTime){
        this.token=token;
        this.expTime=expTime;
        this.genTime=genTime;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpTime() {
        return expTime;
    }

    public void setExpTime(long expTime) {
        this.expTime = expTime;
    }

    public long getGenTime() {
        return genTime;
    }

    public void setGenTime(long genTime) {
        this.genTime = genTime;
    }
}
