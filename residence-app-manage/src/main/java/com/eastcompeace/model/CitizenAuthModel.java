package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 居民信息表
 * @author ecp-zeng
 * @date 2017年1月4日
 */
@Alias(value ="citizenAuth")
public class CitizenAuthModel {

	private String authID;
	
	private String citizenID;
	
	/**认证类型：1-身份证/2-居住证*/
	private String authType;
	
	/**认证状态：1-待审核/2-审核中/3-审核完成*/
	private String authStatus;
	
	/**认证结果：TRUE-成功/FALSE-失败*/
	private String authResult;
	
	private String authResultMessage;
	
	private String authTime;
	
	/*用户信息*/
	private String userName;
	private String citizenName;
	private String idCard;
	private String rcCard;
	
	
	public String getAuthID() {
		return authID;
	}
	public void setAuthID(String authID) {
		this.authID = authID;
	}
	public String getCitizenID() {
		return citizenID;
	}
	public void setCitizenID(String citizenID) {
		this.citizenID = citizenID;
	}
	public String getAuthType() {
		return authType;
	}
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	public String getAuthStatus() {
		return authStatus;
	}
	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}
	public String getAuthResult() {
		return authResult;
	}
	public void setAuthResult(String authResult) {
		this.authResult = authResult;
	}
	public String getAuthResultMessage() {
		return authResultMessage;
	}
	public void setAuthResultMessage(String authResultMessage) {
		this.authResultMessage = authResultMessage;
	}
	public String getAuthTime() {
		return authTime;
	}
	public void setAuthTime(String authTime) {
		this.authTime = authTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCitizenName() {
		return citizenName;
	}
	public void setCitizenName(String citizenName) {
		this.citizenName = citizenName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getRcCard() {
		return rcCard;
	}
	public void setRcCard(String rcCard) {
		this.rcCard = rcCard;
	}

}
