package config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Map;

/**
 * 微信支付工具类
 *
 */
public class WxPayUtil {
	
	//拼接请求参数
	public static String requestParam(WxRequestParam param) {
		String sign="appid="+param.getAppid()+"&"+
				"body="+param.getBody()+"&"+
				"mch_id="+param.getMch_id()+"&"+
				"nonce_str="+param.getNonce_str()+"&"+
				"notify_url="+param.getNotify_url()+"&"+
				"out_trade_no="+param.getOut_trade_no()+"&"+
				"spbill_create_ip="+param.getSpbill_create_ip()+"&"+
				"total_fee="+param.getTotal_fee()+"&"+
				"trade_type="+param.getTrade_type()+"&"+
				"key=yueyanghainaruanjiankeji86055555";
		param.setSign(MD5(sign).toUpperCase());
		
		String str="<xml>" + 
				"<appid>"+param.getAppid()+"</appid>" + 
				"<body>"+param.getBody()+"</body>" + 
				"<mch_id>"+param.getMch_id()+"</mch_id>" + 
				"<nonce_str>"+param.getNonce_str()+"</nonce_str>"+
				"<notify_url>"+param.getNotify_url()+"</notify_url>" +
				"<out_trade_no>"+param.getOut_trade_no()+"</out_trade_no>" + 
				"<spbill_create_ip>"+param.getSpbill_create_ip()+"</spbill_create_ip>" + 
				"<total_fee>"+param.getTotal_fee()+"</total_fee>" + 
				"<trade_type>"+param.getTrade_type()+"</trade_type>" + 
				"<sign>"+param.getSign()+"</sign>" + 
				"</xml>";
		return str;
	}
	
	//发送请求
	public static String sendRequest(String requestUrl,String param) {
		StringBuffer sb = new StringBuffer();
		try {
			//建立网络连接
			URL url = new URL(requestUrl);
			//打开连接
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();
			//设置连接的参数
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			//提交请求
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write(param);
			out.flush();
			
			//读取返回的结果
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String result=reader.readLine();
			while (result!=null) {
				sb.append(result);
				result=reader.readLine();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	//处理返回的结果
	public static String getCodeUrl(WxRequestParam param) {
		String result = sendRequest("https://api.mch.weixin.qq.com/pay/unifiedorder",requestParam(param));
		String codeUrl="";
		try {
			System.out.println(result);
			Map map = WXPayXmlUtil.xmlToMap(result);
			codeUrl=(String) map.get("code_url");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return codeUrl;
	}

	//查询定单状态
	public static boolean queryOrderState(WxRequestParam param) {
		String str = "appid="+param.getAppid()+"&"+
				"mch_id="+param.getMch_id()+"&"+
				"nonce_str="+param.getNonce_str()+"&"+
				"out_trade_no="+param.getOut_trade_no()+"&"+
				"key=yueyanghainaruanjiankeji86055555";

		param.setSign(MD5(str).toUpperCase());

		String xml = "<xml>"
				+ "<appid>" + param.getAppid() + "</appid>"
				+ "<mch_id>"+ param.getMch_id() + "</mch_id>"
				+ "<nonce_str>" + param.getNonce_str()+ "</nonce_str>"
				+ "<out_trade_no>" + param.getOut_trade_no()+ "</out_trade_no>"
				+ "<sign>"+param.getSign()+"</sign>"
				+ "</xml>";

		String result = sendRequest("https://api.mch.weixin.qq.com/pay/orderquery",xml);
		try {
			Map map =WXPayXmlUtil.xmlToMap(result);
			String trade_state=(String) map.get("trade_state");
			if("SUCCESS".equals(trade_state)) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static void main(String[] args) {
		WxRequestParam param = new WxRequestParam();
		param.setOut_trade_no("123456789");
		System.out.println(queryOrderState(param));
	}
	
	  /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data){
    	StringBuilder sb = new StringBuilder();
    	try {
    		MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(data.getBytes("UTF-8"));
	       
	        for (byte item : array) {
	            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
	        }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return sb.toString().toUpperCase();
    }

}
