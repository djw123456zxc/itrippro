package config;

/**
 * 请求参数
 *
 *
 */
public class WxRequestParam {
	private String appid = "wx171686d5566aae60";// 公众账号ID

	private String mch_id = "1510700851";// 商户号

	private String nonce_str = String.valueOf(System.currentTimeMillis());// 随机字符串

	private String sign;// 签名

	private String body;// 商品描述

	private String out_trade_no;// 商户订单号

	private String total_fee;// 金额

	private String spbill_create_ip = "127.0.0.1";// 客户端的ip

	private String notify_url = "http://localhost:8080/itriptrade/notice";// 通知路径

	private String trade_type = "NATIVE";// 交易类型

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		// this.total_fee = total_fee;
		// 将元换成分
		int length = total_fee.length();
		int index = total_fee.indexOf(".");
		Long money = null;
		if (index == -1) {// 为整数
			money = Long.valueOf(total_fee + "00");
		} else if (length - (index + 1) >= 2) {// 有两位小数及以上
			money = Long.valueOf(total_fee.substring(0, index + 1 + 2).replace(".", ""));
		} else if (length - (index + 1) == 1) {// 只有一位小数
			money = Long.valueOf(total_fee.substring(0, index + 1 + 1).replace(".", "") + 0);
		} else {// 如果点后面没数字
			money = Long.valueOf(total_fee.substring(0, index + 1).replace(".", "") + 00);
		}
		this.total_fee = money.toString();

	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

}
