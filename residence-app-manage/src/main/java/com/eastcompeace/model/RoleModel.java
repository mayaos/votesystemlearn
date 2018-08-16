package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 角色实体类
 * 
 * @author caobo
 * 
 */
@Alias(value = "Role")
public class RoleModel {

	private int role_id; // 角色ID
	private String role_name; // 角色名
	private String role_desc; // 角色描述
	private String create_date;// 创建时间
	private String isdefault;// 是否默认
	private int disLevel;// 区域级别[0-超级/1-省/2-市/3-区/4-镇街/5-居委/6小区]
	private String disLevelVal;

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getRole_desc() {
		return role_desc;
	}

	public void setRole_desc(String role_desc) {
		this.role_desc = role_desc;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}
	public int getDisLevel() {
		return disLevel;
	}

	public void setDisLevel(int disLevel) {
		this.disLevel = disLevel;
	}

	public String getDisLevelVal() {
		return disLevelVal;
	}

	public void setDisLevelVal(String disLevelVal) {
		this.disLevelVal = disLevelVal;
	}
	
}