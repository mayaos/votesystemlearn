package com.eastcompeace.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.eastcompeace.share.cipher.MD5Cipher;
import com.eastcompeace.share.cipher.TripleDESCipher;
import com.eastcompeace.share.utils.ByteUtils;
import com.eastcompeace.share.utils.ToolKitUtils;
import com.eastcompeace.util.ResourceUtils;
import com.eastcompeace.util.ToolUtils;

public class SmsClient {
	private static Log logger_ = LogFactory.getLog(SmsClient.class);
	
	// 发送手机短信相关配置
	public static String URL = "http://112.91.149.179/sms/sendctrl/sendSms";
	public final static String DES3_KEY = "DFE27C5D95D218445E19A34507AB76AB";
	public final static String SIGN_KEY = "efef283723237747382910c1234d8989";
	public final static String BUS_CODE = "000003";
	public final static String VECTOR = "00000000";
	static{
		String smsURL = ResourceUtils.getProperty("SMS_URL");
		if(null != smsURL && !"".equals(smsURL)){
			URL = smsURL;
		}
	}
	/**
	 * 发送手机短信  ——默认内容
	 * @param phoneNum  手机号码
	 * @return 0-成功，其他-失败
	 */
	public int sendSms0(String phoneNum) {
		// 取随机产生的认证码  拼装 短信内容
		String strRand = ToolKitUtils.randomNumber(6);
		String smsContent = "您的验证码是：" + strRand + "。请不要把验证码泄露给其他人。如非本人操作，可不用理会！";
		return sendSms0(phoneNum, smsContent);
	}
	 
	/**
	 * 发送手机短信
	 * @param phoneNum  手机号码
	 * @param smsContent 短信内容
	 * @return 0-成功，其他-失败
	 */
	public int sendSms0(String phoneNum, String smsContent) {
		return sendSms0(phoneNum, smsContent, "");
	}

	/**
	 * 注册时发送手机验证码
	 * 
	 * @param phoneNum 手机号
	 * @param deviceCode 设备编号
	 * @return 0-成功，其他-失败
	 */
	public int sendSms0(String phoneNum, String smsContent, String deviceCode) {
		
		// 生成时间戳
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String temptime = sdf.format(date);
		
		// 流水号
		String seqNO = "W" + ToolKitUtils.randomHexString(9);
		
		// 签名, 流水号+设备编号+8个0+密码+时间截”+Body所有属性顺序拼值
		String sign = new StringBuilder().append(seqNO).append(deviceCode)
				.append(VECTOR).append(SIGN_KEY).append(temptime)
				.append(phoneNum).append(smsContent).toString();
		logger_.debug("sign:" + sign);

		DefaultHttpClient httpclient = new DefaultHttpClient();
		String mdSign = null;
		int retCode = -1;
		try {
			mdSign = ByteUtils.toHexString(MD5Cipher.encrypt(sign.getBytes("GBK"))).toUpperCase();
			
			JSONObject jHeasder = new JSONObject();
			jHeasder.put("SeqNO", seqNO);
			jHeasder.put("BusCode", BUS_CODE);
			jHeasder.put("DevCode", deviceCode);
			jHeasder.put("Temptime", temptime);
			jHeasder.put("Sign", mdSign);

			JSONObject jBody = new JSONObject();
			jBody.put("Mobile", phoneNum);
			jBody.put("Sms", smsContent);
			logger_.debug("body内容:" + jBody);
			String ujBody = ToolUtils.encodUnicode(jBody.toString());
			byte[] strBytes = null;
			strBytes = ujBody.toString().getBytes();
			String strMessage = ByteUtils.toHexString(strBytes);
			strMessage = ToolUtils.padding(strMessage, strMessage.length() + 16
					- strMessage.length() % 16, "20", "R");
			logger_.debug("加密前body:" + strMessage);
			String encData = TripleDESCipher.encrypt(strMessage, DES3_KEY);

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("Header", jHeasder);
			jsonObject.put("Body", encData);
			logger_.debug("request:" + URL + jsonObject.toString());

			// 发送HTTP请求
			HttpResponse response = null;
			HttpPost post = new HttpPost(URL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("jsmsg", jsonObject.toString()));
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000); 
			response = httpclient.execute(post);
			 
			// 解析返回值
			HttpEntity entity = response.getEntity();
			entity = response.getEntity();
			String xmlContent = EntityUtils.toString(entity);
			logger_.debug("响应内容：" + xmlContent);
			logger_.debug("2." + response.getHeaders("Content-Type")[0].getValue().toString());

			JSONObject obj = JSONObject.fromObject(xmlContent);
			String header = obj.getString("Header");
			JSONObject obj2 = JSONObject.fromObject(header);
			retCode = Integer.parseInt(obj2.getString("RetCode"));
			if (retCode != 0) {
				logger_.error("[" + phoneNum + "]短信发送失败："	+ obj2.getString("RetMsg"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (ArithmeticException e) {
			e.printStackTrace();
		} finally {
			// 关闭请求
			httpclient.getConnectionManager().shutdown();
		}
		return retCode;
	}
	
	public static void main(String[] args) throws ParseException {
		SmsClient sms = new SmsClient();
		int result = sms.sendSms0("186756210430", "测试验密码01234【验证码】");
		logger_.info(result);

	}

}
