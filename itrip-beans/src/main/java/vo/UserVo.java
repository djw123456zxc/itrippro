package vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel(value = "UserVo",description = "用户注册信息")
public class UserVo {
    //private int id;
    @ApiModelProperty("【必填项】注册用户名称")
    private String userCode;
    @ApiModelProperty("【必填项】注册用户密码")
    private String userPassword;
    //private int userType;
    //private int flatID;
    @ApiModelProperty("【非必填项】注册用户昵称")
    private String userName;
    //private String weChat;
    //private String QQ;
    //private String weibo;
    //private String baidu;
    //private Date creationDate;
    //private int createdBy;
    //private Date modifyDate;
    //private int modifiedBy;
    //private int activated;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
