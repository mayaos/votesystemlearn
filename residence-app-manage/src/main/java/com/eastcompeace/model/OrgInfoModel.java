package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

@Alias(value = "OrgInfo")
public class OrgInfoModel {
	private String orgId        ; //机构ID
	private String menuId          ; //菜单ID
	private String areaId          ;//区域ID
	private String areaName        ;//区域名称
	private String menuLogo       ; //菜单Logo
	private String menuName     ; //菜单名
	private String orgName         ; //机构名称
	private String orgDesc         ; //机构描述
	private String orgLogo         ; //机构LOGO
	private String orgPhone     ; //电话号码
	private String passFlag        ; //审核状态 1：未审核 0：审核通过 2：审核未通过
	private String noPassReason   ; //审核未通过原因
	private String createTime     ; //创建时间
	private String updateTime     ; //修改时间
	
	public OrgInfoModel() {}

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


	public String getMenuName() {
		return menuName;
	}

	public String getMenuLogo() {
		return menuLogo;
	}


	public void setMenuLogo(String menuLogo) {
		this.menuLogo = menuLogo;
	}


	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}


	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}


	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgDesc() {
		return orgDesc;
	}

	public void setOrgDesc(String orgDesc) {
		this.orgDesc = orgDesc;
	}

	public String getOrgLogo() {
		return orgLogo;
	}

	public void setOrgLogo(String orgLogo) {
		this.orgLogo = orgLogo;
	}

	

	public String getOrgPhone() {
		return orgPhone;
	}

	public void setOrgPhone(String orgPhone) {
		this.orgPhone = orgPhone;
	}

	public String getPassFlag() {
		return passFlag;
	}

	public void setPassFlag(String passFlag) {
		this.passFlag = passFlag;
	}

	public String getNoPassReason() {
		return noPassReason;
	}

	public void setNoPassReason(String noPassReason) {
		this.noPassReason = noPassReason;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
