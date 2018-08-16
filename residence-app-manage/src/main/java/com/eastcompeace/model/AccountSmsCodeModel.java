package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 
 * @author ecp-zeng
 * @date 2016年5月13日
 */
@Alias(value = "AccountSmsCode")
public class AccountSmsCodeModel {
	
	private String smsID;
	private String userName;
	private String smsCode;
	private String createTime;
	private String expiringTime;
	
	public String getSmsID() {
		return smsID;
	}
	public void setSmsID(String smsID) {
		this.smsID = smsID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSmsCode() {
		return smsCode;
	}
	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getExpiringTime() {
		return expiringTime;
	}
	public void setExpiringTime(String expiringTime) {
		this.expiringTime = expiringTime;
	}
	
	
}
