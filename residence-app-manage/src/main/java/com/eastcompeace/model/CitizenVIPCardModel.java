package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 居民信息表
 * @author ecp-zeng
 * @date 2017年1月4日
 */
@Alias(value ="vipCard")
public class CitizenVIPCardModel {

	private String vipcardID;
	private String citizenID;
	private String merchantID;
	private String vipcardCode;
	private String openTime;
	private String endTime;
	
	/** 会员卡商家信息*/
	private String merchantName;
	private String merchantLogo;
	private String vipcardRule;
	private String contacts;
	private String telephone;
	
	/** 会员卡用户信息*/
	private String userName;
	private String citizenName;
	private String idCard;
	
	public String getVipcardID() {
		return vipcardID;
	}
	public void setVipcardID(String vipcardID) {
		this.vipcardID = vipcardID;
	}
	public String getCitizenID() {
		return citizenID;
	}
	public void setCitizenID(String citizenID) {
		this.citizenID = citizenID;
	}
	public String getMerchantID() {
		return merchantID;
	}
	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}
	public String getVipcardCode() {
		return vipcardCode;
	}
	public void setVipcardCode(String vipcardCode) {
		this.vipcardCode = vipcardCode;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMerchantLogo() {
		return merchantLogo;
	}
	public void setMerchantLogo(String merchantLogo) {
		this.merchantLogo = merchantLogo;
	}
	public String getVipcardRule() {
		return vipcardRule;
	}
	public void setVipcardRule(String vipcardRule) {
		this.vipcardRule = vipcardRule;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
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
	
}
