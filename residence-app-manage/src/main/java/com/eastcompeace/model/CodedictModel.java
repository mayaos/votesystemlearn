package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 用户实体类
 * 
 * @author suyang
 * 
 */
@Alias(value ="Codedict")
public class CodedictModel {
	// 与数据库表一一映射的实体字段
	private String codeType;
	private String codeTypeName;
	private String codeName;
	private String codeValue;
	private String codeOrder;
	private String codeValid;
	private String codeDate;
	private String code;
	
	private String codeValidVal;
	
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getCodeTypeName() {
		return codeTypeName;
	}
	public void setCodeTypeName(String codeTypeName) {
		this.codeTypeName = codeTypeName;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getCodeValue() {
		return codeValue;
	}
	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getCodeOrder() {
		return codeOrder;
	}
	public void setCodeOrder(String codeOrder) {
		this.codeOrder = codeOrder;
	}
	
	public String getCodeDate() {
		return codeDate;
	}
	public void setCodeDate(String codeDate) {
		this.codeDate = codeDate;
	}
	public String getCodeValid() {
		return codeValid;
	}
	public void setCodeValid(String codeValid) {
		this.codeValid = codeValid;
	}
	public String getCodeValidVal() {
		return codeValidVal;
	}
	public void setCodeValidVal(String codeValidVal) {
		this.codeValidVal = codeValidVal;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "CodedictModel [codeType=" + codeType + ", codeTypeName="
				+ codeTypeName + ", codeName=" + codeName + ", codeValue="
				+ codeValue + ", codeOrder=" + codeOrder + ", codeValid="
				+ codeValid + ", codeDate=" + codeDate + ", code=" + code
				+ ", codeValidVal=" + codeValidVal + "]";
	}
	
	
	
}
