package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 用户和角色映射实体类
 * 
 * @author caobo 对应的数据库表名： common_user_mapping
 */
@Alias(value = "UserRoles")
public class UserRolesModel {

	private int id;
	private int userId;
	private int roleId;

	// getter and setter
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
}