package service;

public interface SmsService {
    public void SendTo(String to,String templateId,String[] datas) throws Exception; //发送短信
}
