package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 用户小区对应关系 实体类
 * 
 * @author xiangpei
 * 
 */
@Alias(value = "userComm")
public class UserCommModel {
	private String uuid;
	private String userid;
	private String type;
	private String disuuid;
	private String commuuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getDisuuid() {
		return disuuid;
	}

	public void setDisuuid(String disuuid) {
		this.disuuid = disuuid;
	}

	public String getCommuuid() {
		return commuuid;
	}

	public void setCommuuid(String commuuid) {
		this.commuuid = commuuid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
