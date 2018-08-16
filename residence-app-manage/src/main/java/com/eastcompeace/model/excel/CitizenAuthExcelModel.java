package com.eastcompeace.model.excel;

import org.apache.ibatis.type.Alias;

/**
 * 居民信息表
 * @author ecp-zeng
 * @date 2017年1月4日
 */
@Alias(value ="citizenAuthExcel")
public class CitizenAuthExcelModel {

	/*认证信息*/
	private String userName;
	private String citizenName;
	private String idCard;
	private String rcCard;
	private String authType;
	private String authTime;
	private String authStatus;
	private String authResult;
	private String authResultMessage;
	
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
	public String getAuthTime() {
		return authTime;
	}
	public void setAuthTime(String authTime) {
		this.authTime = authTime;
	}
	public String getRcCard() {
		return rcCard;
	}
	public void setRcCard(String rcCard) {
		this.rcCard = rcCard;
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
	
}
