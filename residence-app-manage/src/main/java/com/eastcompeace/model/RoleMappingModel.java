package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 角色菜单映射
 * 
 * @author xp
 * 
 */
@Alias(value = "RoleMaping")
public class RoleMappingModel {
	// 与数据表一一映射的实体字段
	private int id; // 主键id,自增长
	private String role_id; // 对应角色ID
	private String menu_id; // 对应按钮ID

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}

}