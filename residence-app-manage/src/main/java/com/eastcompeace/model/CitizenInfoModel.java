package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 流口信息实体类
 * @author caobo
 */
@Alias(value = "CitizenInfo")
public class CitizenInfoModel {

	private String id ; //主键Id（批次号+居住证号）
	private String rcCard; //居住证卡号
	private String name; //姓名
	private String idCard; //身份证号
	private String bankCard; //银行卡号
	private String rcCardType; //居住证卡类型
	private String sex; //性别
	private String birthday; //生日
	private String nation; //民族
	private String address; //户籍地址
	private String issueDate; //居住证签发日期
	private String issueOffice; //居住证签发机关
	private String serviceTel; //服务热线
	private String validThru; //居住证有效期
	private String areaId; //区域代码
	private String importDate; //导出日期
	private String batchNo;  //批次号
	private String citizenSerialNo; //市民卡编号
	private String citizenNo ; //市民卡号
	
	//照片数据
	private String photoUrl;
	private String photoBase64;
	
	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getPhotoBase64() {
		return photoBase64;
	}

	public void setPhotoBase64(String photoBase64) {
		this.photoBase64 = photoBase64;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String resCode = "";	// 存储过程错误代码
	private String resMsg = "";		// 存储过程错误描述

	public CitizenInfoModel() {}

	public String getCitizenSerialNo() {
		return citizenSerialNo;
	}

	public void setCitizenSerialNo(String citizenSerialNo) {
		this.citizenSerialNo = citizenSerialNo;
	}

	public String getCitizenNo() {
		return citizenNo;
	}

	public void setCitizenNo(String citizenNo) {
		this.citizenNo = citizenNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServiceTel() {
		return serviceTel;
	}

	public void setServiceTel(String serviceTel) {
		this.serviceTel = serviceTel;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getRcCard() {
		return rcCard;
	}

	public void setRcCard(String rcCard) {
		this.rcCard = rcCard;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getRcCardType() {
		return rcCardType;
	}

	public void setRcCardType(String rcCardType) {
		this.rcCardType = rcCardType;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getIssueOffice() {
		return issueOffice;
	}

	public void setIssueOffice(String issueOffice) {
		this.issueOffice = issueOffice;
	}

	public String getValidThru() {
		return validThru;
	}

	public void setValidThru(String validThru) {
		this.validThru = validThru;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getImportDate() {
		return importDate;
	}

	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	@Override
	public String toString() {
		return "CitizenInfoModel [rcCard=" + rcCard + ", name=" + name
				+ ", idCard=" + idCard + ", bankCard=" + bankCard
				+ ", rcCardType=" + rcCardType + ", sex=" + sex + ", birthday="
				+ birthday + ", nation=" + nation + ", address=" + address
				+ ", issueDate=" + issueDate + ", issueOffice=" + issueOffice
				+ ", serviceTel=" + serviceTel + ", validThru=" + validThru
				+ ", areaId=" + areaId + ", importDate=" + importDate
				+ ", batchNo=" + batchNo + ", resCode=" + resCode + ", resMsg="
				+ resMsg + "]";
	}

	public CitizenInfoModel(String rcCard, String name, String idCard,
			String bankCard, String rcCardType, String sex, String birthday,
			String nation, String address, String issueDate,
			String issueOffice, String serviceTel, String validThru,
			String areaId, String importDate, String batchNo, String resCode,
			String resMsg) {
		super();
		this.rcCard = rcCard;
		this.name = name;
		this.idCard = idCard;
		this.bankCard = bankCard;
		this.rcCardType = rcCardType;
		this.sex = sex;
		this.birthday = birthday;
		this.nation = nation;
		this.address = address;
		this.issueDate = issueDate;
		this.issueOffice = issueOffice;
		this.serviceTel = serviceTel;
		this.validThru = validThru;
		this.areaId = areaId;
		this.importDate = importDate;
		this.batchNo = batchNo;
		this.resCode = resCode;
		this.resMsg = resMsg;
	}
	
	
}
