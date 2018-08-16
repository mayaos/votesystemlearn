package com.eastcompeace.util.sms;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 * 短信请求信息类
 * 
 * @author created by panyanlin
 * @date 2018年4月19日 上午10:20:41
 */
public class SmsDto {
    /** 短信网关为平台分配的id */ 
    private String        uuid;
    /** 签名字段sign的计算：对请求报文（除sign字段）先使用SHA1算法计算HASH，再使用平台私钥对HASH值进行签名 */
    private String        sign;
    /** 时间戳，示例：'2018-04-17 15:49:39' */
    private String        timestamp;
    /** 发送消息集合 */
    private List<Message> message;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<Message> getMessage() {
        return message;
    }

    public void setMessage(List<Message> message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SmsDto [uuid=" + uuid + ", sign=" + sign + ", timestamp=" + timestamp + ", message=" + message + "]";
    }
}
