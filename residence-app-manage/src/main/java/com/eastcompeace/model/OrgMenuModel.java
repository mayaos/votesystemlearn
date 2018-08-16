package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 轻应用菜单
 *
 */
@Alias(value="OrgMenu")
public class OrgMenuModel {
	private String menuId;   		//轻应用ID
	private String menuName; 		//轻应用名称
	private String menuLogo; 		//轻应用Logo
	private String backgroundImg;	//轻应用背景图片
	private String menuUrl;  		//轻应用链接
	private String menuType; 		//轻应用类型
	private String menuTypeName;	//轻应用类型显示值
	private String menuOrder; 		//排序
	private String menuStatus; 		//是否有效[1-是/2-否]
	private String isDefault; 		//是否首页默认[0-否/1-是]
	private String menuLogoUrl; 	//菜单LogoUrl
	private String areaId;			//区域ID
	private String areaName;		//区域ID显示值
	private String menuDesc;		//菜单描述
	
	public OrgMenuModel() {}
	
	public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public String getMenuLogoUrl() {
		return menuLogoUrl;
	}

	public void setMenuLogoUrl(String menuLogoUrl) {
		this.menuLogoUrl = menuLogoUrl;
	}

	public String getBackgroundImg() {
		return backgroundImg;
	}

	public void setBackgroundImg(String backgroundImg) {
		this.backgroundImg = backgroundImg;
	}

	public String getMenuTypeName() {
		return menuTypeName;
	}
	
	public void setMenuTypeName(String menuTypeName) {
		this.menuTypeName = menuTypeName;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
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
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public String getMenuOrder() {
		return menuOrder;
	}
	public void setMenuOrder(String menuOrder) {
		this.menuOrder = menuOrder;
	}
	public String getMenuStatus() {
		return menuStatus;
	}
	public void setMenuStatus(String menuStatus) {
		this.menuStatus = menuStatus;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}
