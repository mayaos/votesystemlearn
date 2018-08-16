package com.eastcompeace.util.sms;
/**
 * 短信消息模板
 * 
 * @author created by panyanlin
 * @date 2018年4月19日  上午11:03:35
 */
public enum TplidEnum {
    REGISTER("d8845f09-973c-43ef-9ebd-9d43d32db85a", "用户注册验证码短信模板"), 
    UPDATE_PWD("f5a7cfd5-874e-4930-b2d2-1fee92af09c9", "用户修改密码验证码短信模板"),
    UPDATE_INFO("b9683367-7b99-469a-83af-16afa2b8dda1", "用户修改信息验证码短信模板"),
    RC_AURH_LISTENER("e46bc0c8-d0bd-47c0-b65b-d51c850e6e28", "第三方认证平台业务监控短信模板");
    private String value;
    private String note;

    private TplidEnum(String value, String note) {
        this.value = value;
        this.note = note;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
