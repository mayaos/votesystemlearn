package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;
/**
 * 区域类
 * @author panyanlin
 *
 */
@Alias("Area")
public class AreaModel {
	private String areaID; //区域ID
	private String areaName; //区域名称
	private String fullName; //区域全称
	private String provinceCode; //省份代码
	private String cityCode; //城市代码
	private String townCode; //区县代码
	
	public AreaModel() {}

	public AreaModel(String areaID, String areaName, String fullName,
			String provinceCode, String cityCode, String townCode) {
		super();
		this.areaID = areaID;
		this.areaName = areaName;
		this.fullName = fullName;
		this.provinceCode = provinceCode;
		this.cityCode = cityCode;
		this.townCode = townCode;
	}

	public String getAreaID() {
		return areaID;
	}

	public void setAreaID(String areaID) {
		this.areaID = areaID;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getTownCode() {
		return townCode;
	}

	public void setTownCode(String townCode) {
		this.townCode = townCode;
	}
	
	
}
