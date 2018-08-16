package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 用户轻应用菜单实例类
 * @author ecp-zeng
 * @date 2017年1月9日
 */
@Alias(value = "citizenApp")
public class CitizenAppMenuModel {
	private String citizenID;
	private String menuID;
	private String menuOrder;
	
	/** 轻应用菜单信息*/
	private String menuName;
	private String menuLogo;
	private String menuUrl;
	private String menuType;
	
	public String getCitizenID() {
		return citizenID;
	}
	public void setCitizenID(String citizenID) {
		this.citizenID = citizenID;
	}
	public String getMenuID() {
		return menuID;
	}
	public void setMenuID(String menuID) {
		this.menuID = menuID;
	}
	public String getMenuOrder() {
		return menuOrder;
	}
	public void setMenuOrder(String menuOrder) {
		this.menuOrder = menuOrder;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuLogo() {
		return menuLogo;
	}
	public void setMenuLogo(String menuLogo) {
		this.menuLogo = menuLogo;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	
}
