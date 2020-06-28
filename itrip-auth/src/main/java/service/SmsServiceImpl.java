package service;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("SmsService")
public class SmsServiceImpl implements SmsService{
    @Override
    public void SendTo(String to, String templateId, String[] datas) throws Exception {
        CCPRestSmsSDK sdk=new CCPRestSmsSDK();
        sdk.init("app.cloopen.com", "8883");
        sdk.setAccount("8a216da8701eb7c101703cf21acf0b82", "a1517e8a0225445a84d6d2bdc880d90d");
        sdk.setAppId("8a216da8701eb7c101703cf21b3f0b89");
        HashMap result = sdk.sendTemplateSMS(to, templateId, datas);
        if ("000000".equals(result.get("statusCode"))) {
            System.out.println("短信发送成功！");
        }else{
            throw new Exception(result.get("statusCode").toString()+":"+result.get("statusMsg").toString());
        }
    }

}
