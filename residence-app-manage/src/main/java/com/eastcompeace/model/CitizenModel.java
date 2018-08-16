package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 居民信息表
 * @author ecp-zeng
 * @date 2017年1月4日
 */
@Alias(value ="citizen")
public class CitizenModel {
	
	private String citizenID;
	private String userName;
	private String userPassword;
	private String userNickname;
	private String userHead;
	private String userLevel;
	private String userStatus;
	private String userRegTime;
	private String userLastLoginTime;
	private String userLoginTimes;
	private String email;
	private String postcode;
	
	private String citizenName;
	private String idCard;
	private String rcCard;
	private String bankCard;
	private String rcCardType;
	private String sex;
	private String birthday;
	private String nation;
	private String address;
	private String issueDate;
	private String issueOffice;
	private String validThru;
	private String rcHead;
	
	
	public String getRcHead() {
		return rcHead;
	}
	public void setRcHead(String rcHead) {
		this.rcHead = rcHead;
	}
	public String getCitizenID() {
		return citizenID;
	}
	public void setCitizenID(String citizenID) {
		this.citizenID = citizenID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserNickname() {
		return userNickname;
	}
	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}
	public String getUserHead() {
		return userHead;
	}
	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getUserRegTime() {
		return userRegTime;
	}
	public void setUserRegTime(String userRegTime) {
		this.userRegTime = userRegTime;
	}
	public String getUserLastLoginTime() {
		return userLastLoginTime;
	}
	public void setUserLastLoginTime(String userLastLoginTime) {
		this.userLastLoginTime = userLastLoginTime;
	}
	public String getUserLoginTimes() {
		return userLoginTimes;
	}
	public void setUserLoginTimes(String userLoginTimes) {
		this.userLoginTimes = userLoginTimes;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
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
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	
	public String getRcCardType() {
		return rcCardType;
	}
	public void setRcCardType(String rcCardType) {
		this.rcCardType = rcCardType;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getIssueOffice() {
		return issueOffice;
	}
	public void setIssueOffice(String issueOffice) {
		this.issueOffice = issueOffice;
	}
	public String getValidThru() {
		return validThru;
	}
	public void setValidThru(String validThru) {
		this.validThru = validThru;
	}

}
