package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 菜单实体类
 * 
 * @author caobo
 * 
 */
@Alias(value = "Menu")
public class MenuModel {

	private int menuId;
	private int menuFatherid;
	private String menuName;
	private String menuDesc;
	private int menuOrder;
	private String menuUrl;
	private String menuIcon;
	private String menuIsleaf;
	private int menuCreateuser;
	private String menuCreateip;
	private String menuCreatedate;

	// getter and setter
	public int getMenuId() {
		return this.menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getMenuFatherid() {
		return this.menuFatherid;
	}

	public void setMenuFatherid(int menuFatherid) {
		this.menuFatherid = menuFatherid;
	}

	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuDesc() {
		return this.menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}

	public int getMenuOrder() {
		return this.menuOrder;
	}

	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}

	public String getMenuUrl() {
		return this.menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMenuIcon() {
		return this.menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public String getMenuIsleaf() {
		return this.menuIsleaf;
	}

	public void setMenuIsleaf(String menuIsleaf) {
		this.menuIsleaf = menuIsleaf;
	}

	public int getMenuCreateuser() {
		return this.menuCreateuser;
	}

	public void setMenuCreateuser(int menuCreateuser) {
		this.menuCreateuser = menuCreateuser;
	}

	public String getMenuCreateip() {
		return this.menuCreateip;
	}

	public void setMenuCreateip(String menuCreateip) {
		this.menuCreateip = menuCreateip;
	}

	public String getMenuCreatedate() {
		return this.menuCreatedate;
	}

	public void setMenuCreatedate(String menuCreatedate) {
		this.menuCreatedate = menuCreatedate;
	}
}