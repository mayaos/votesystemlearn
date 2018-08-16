package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 管理员管理区域表
 * 
 * @author caobo
 * 
 */
@Alias(value = "DivisionsUser")
public class DivisionsUserModel {
	
	private int userId;
	
	private String userCode;
	
	private String divisionsId;

	public String getUserCode() {
		return userCode;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getDivisionsId() {
		return divisionsId;
	}

	public void setDivisionsId(String divisionsId) {
		this.divisionsId = divisionsId;
	}

}