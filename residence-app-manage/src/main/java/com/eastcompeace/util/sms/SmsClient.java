package com.eastcompeace.util.sms;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import com.alibaba.fastjson.JSONObject;
import com.eastcompeace.share.cipher.RSACipher;
import com.eastcompeace.share.utils.ByteUtils;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.ResourceUtils;
/**
 * 短信发送客户端
 * 
 * @author created by panyanlin
 * @date 2018年4月19日 上午9:54:39
 */
public class SmsClient {
    /** 短信网关为平台分配的id */ 
    private static String       smsUuid            = ResourceUtils.getProperty("SMS_UUID");
    /** 短信网关地址 */
    private static String       smsUrl             = ResourceUtils.getProperty("SMS_URL");
    /** RSA公钥 */
    private static final String strPublicKey       = "da5829fae66a0b9f194ff3f4ff54485c527f8f4ec6c0db0e0edbc621803c62e971f5de27a3a9b611fbf91a4d26403d8bdea07e1424d0136481e5041e7170c7f200b112bfd61550e83d5a6a1f3bc4d190f5e6375f3913eec758875c478c9dc06a6cbb985ddad89eb5d53c5942e8c82d66a405609e9f0fc7d2f9124747eb6decf1";
    /** 短信类型-验证码短信 */
    public final static String  SMS_TYPE_IDENTIFY  = "identify";
    /** 短信类型-通知短信 */
    public final static String  SMS_TYPE_NOTIFY    = "notify";
    /** 短信类型-推广短信 */
    public final static String  SMS_TYPE_PROMOTION = "promotion";
    /** 短信地址 */
    public final static String  URL = "http://47.98.50.42:8080/shortmessage/ctrls/message/send";

    /**
     * RSA公钥加密数据
     * 
     * @param plaintext
     * @return
     */
    private static String getRSACiphertext(String plaintext) {
        BigInteger modulus = new BigInteger(strPublicKey, 16);
        BigInteger publicExponent = new BigInteger("010001", 16);
        RSAPublicKey publicKey = RSACipher.getRSAPublicKey(modulus, publicExponent);
        byte[] cipherBytes = RSACipher.encrypt(ByteUtils.toBytes(plaintext), publicKey);
        String ciphertext = ByteUtils.toHexString(cipherBytes); // 经过平台公钥加密后的密文数据
        return ciphertext;
    }

    /**
     * 发送短信
     * 
     * @param smsType
     *            短信类型，在本类属性里选择：SMS_TYPE_*
     * @param tplid
     *            短信消息模本，在TplidEnum枚举类中选择
     * @param phoneNum
     *            电话号码
     * @param content
     *            消息发送内容，根据所选的模板类型来组拼字符串
     * @return retCode
     *             0 成功，其他数字失败
     */
    public static int sendSms(String smsType, TplidEnum tplid, String phoneNum, String content) throws Exception {
        int retCode = -1;
        // 创建消息集合
        Message msg = new Message();
        msg.setContent(content);
        msg.setPhoneNumber(phoneNum);
        msg.setTplid(tplid.getValue());
        msg.setType(smsType);
        List<Message> msglist = new ArrayList<>();
        msglist.add(msg);
        // 创建短信信息传输对象
        SmsDto smsDto = new SmsDto();
        smsDto.setMessage(msglist);
        smsDto.setTimestamp(DateUtils.getNow());
        smsDto.setUuid(smsUuid);
        // 签名数据
        JSONObject json = (JSONObject) JSONObject.toJSON(smsDto);
        String sign = getRSACiphertext(json.toJSONString());
        smsDto.setSign(sign);
        JSONObject json1=httpConn(URL,(JSONObject) JSONObject.toJSON(smsDto));
        retCode=json1.getInteger("result");
        return retCode;
    }
    
    
    
    
    
    /**
     * 连接居住证APP接口端
     * 
     * @param strURL
     * @param inObj
     * @return
     * @throws HttpConnException
     * @throws Exception
     */
    private static JSONObject httpConn(String strURL, JSONObject inObj) throws Exception {
        // 创建URL
        URL url = new URL(strURL);
        // 创建http连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置访问请求方式为post
        conn.setRequestMethod("POST");
        // 设置是否向请求输入，输出参数，默认false，post方式必须设置的项
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 设置请求为长连接
        conn.setRequestProperty("Connection", "Keep-Alive");
        // 设置请求报文编码为utf-8
        conn.setRequestProperty("Charset", "UTF-8");
        // 连接对方前置服务
        conn.connect();
        // 定义输出流对象
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        // 传输数据
        if (inObj != null) {
            dos.writeBytes(inObj.toString());
            dos.flush();
        }
        // 获取输入流
        InputStream is = conn.getInputStream();
        byte[] buffer = new byte[1024];
        byte[] data = new byte[0];
        int length2 = 0;
        // 获取返回结果
        while ((length2 = is.read(buffer)) != -1) {
            byte[] arrBytes = new byte[data.length + length2];
            System.arraycopy(data, 0, arrBytes, 0, data.length);
            System.arraycopy(buffer, 0, arrBytes, data.length, length2);
            data = arrBytes;
        }
        // 关闭流
        is.close();
        // 将byte[] data转为字符串
        String jstr = new String(data, "UTF-8");
        if (jstr == null || "".equals(jstr)) {
            throw new Exception("返回值为空，连接异常！");
        }
        System.out.println(JSONObject.parseObject(jstr));
        return  JSONObject.parseObject(jstr);
    }
    
    
    
    
    
}
