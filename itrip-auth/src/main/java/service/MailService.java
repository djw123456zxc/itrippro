package service;

public interface MailService {
    public void sendActivationMail(String mailTo,String activationCode); //发送邮件
}
