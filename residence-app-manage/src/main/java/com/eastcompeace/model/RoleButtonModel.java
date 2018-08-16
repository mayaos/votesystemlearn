package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

@Alias(value = "roleButton")
public class RoleButtonModel {

	private int id;
	private String roleid;
	private String meunid;
	private String buttonid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getMeunid() {
		return meunid;
	}

	public void setMeunid(String meunid) {
		this.meunid = meunid;
	}

	public String getButtonid() {
		return buttonid;
	}

	public void setButtonid(String buttonid) {
		this.buttonid = buttonid;
	}

}
