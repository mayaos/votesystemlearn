package com.eastcompeace.util.sms;
import java.io.Serializable;
/**
 * 短信消息类
 * 
 * @author created by panyanlin
 * @date 2018年4月19日 上午10:22:37
 */
public class Message implements Serializable {
    /** 短信类型 */
    private String type; 
    /** 短信模板ID */
    private String tplid;
    private String phoneNumber;
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTplid() {
        return tplid;
    }

    public void setTplid(String tplid) {
        this.tplid = tplid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "message [type=" + type + ", tplid=" + tplid + ", phoneNumber=" + phoneNumber + ", content=" + content + "]";
    }
}
