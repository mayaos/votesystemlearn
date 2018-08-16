package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;
/**
 * 居民用户表 citizen_user
 * @author xianzehua
 *
 */
@Alias(value="citizenUser")
public class CitizenUserModel {

	private String citizenId; //citizen_id
	private String userName; //用户账号(手机号)
	private String userLevel; //用户账号级别[1/2/3-未认证/身份证认证/居住证认证]
	private String userStatus; //用户账号状态[0-正常/1-冻结
	public String getCitizenId() {
		return citizenId;
	}
	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	
}
