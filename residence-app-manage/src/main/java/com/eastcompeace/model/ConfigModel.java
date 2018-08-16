package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 配置实体类
 * 
 * @author zhuchaochao
 * 
 */
@Alias(value = "Config")
public class ConfigModel {
	// 与数据表一一映射的实体字段
	private int paramId;
	private String paramName;
	private String paramValue;
	private String paramDesc;
	private String paramValid;
	private String modifyDate;
	private String upImgsrc; 

	// 虚拟显示字段
	private String paramValidVal;
	
	// getter and setter

	public int getParamId() {
		return paramId;
	}
	
	public void setParamId(int paramId) {
		this.paramId = paramId;
	}
	
	public String getParamName() {
		return paramName;
	}
	
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	
	public String getParamValue() {
		return paramValue;
	}
	
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
	public String getParamDesc() {
		return paramDesc;
	}
	
	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}
	
	public String getParamValid() {
		return paramValid;
	}
	
	public void setParamValid(String paramValid) {
		this.paramValid = paramValid;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getParamValidVal() {
		return paramValidVal;
	}

	public void setParamValidVal(String paramValidVal) {
		this.paramValidVal = paramValidVal;
	}
	
	public String getUpImgsrc() {
		return upImgsrc;
	}
	
	public void setUpImgsrc(String upImgsrc) {
		this.upImgsrc = upImgsrc;
	}
}
