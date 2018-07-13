package com.eastcompeace.dto;

/**
 * 
 * @FileName SuggestSubmitInfoDto.java
 * @Date   2018年6月13日下午2:12:11
 * @author wzh
 *
 * 意见建议提交信息实体类
 *
 */
public class SuggestSubmitInfoDto {
	private String ctgId;	//意见建议类别(1:意见,2:建议)
	private String email;	//电子邮箱
	private String phone;	//电话
	private String qq;		//QQ
	private String title;	//意见建议标题
	private String content;	//意见建议内容
	private String captcha;	//验证码
	
	@Override
	public String toString() {
		return "SuggestSubmitInfoDto [ctgId=" + ctgId + ", email=" + email
				+ ", phone=" + phone + ", qq=" + qq + ", title=" + title
				+ ", content=" + content + ", captcha=" + captcha + "]";
	}
	
	public String getCtgId() {
		return ctgId;
	}
	public void setCtgId(String ctgId) {
		this.ctgId = ctgId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}
