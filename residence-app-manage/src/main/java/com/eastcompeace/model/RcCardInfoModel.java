package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;
/**
 * 居住证信息表 rc_card_info
 * @author xianzehua
 *
 */
@Alias(value="rcCardInfo")
public class RcCardInfoModel {

	private String citizenId; //Citizen Id
	private String name; //用户名称
	private String idCard; //身份证号码
	private String rcCard; //居住证号
	private String bankCard; //银行卡号
	private String importDate; //导入日期
	private String  rcCardType; //居住证卡类型(字典表)
	private String  sex; //性别(字典表)
	private String  birthday;//生日（YYYY-MM-DD）
	private String  nation; //民族(字典表)
	private String  address; //户籍地址
	private String  issueDate; //居住证签发日期
	private String  issueOffice; //居住证签发机关
	private String  validThru; //居住证有效期
	private String citizenSerialNo; // 市民卡编号
	private String citizenNo; // 市民卡号
	private String rcHead; // 居住证头像
	
	public String getCitizenId() {
		return citizenId;
	}
	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
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
	public String getImportDate() {
		return importDate;
	}
	public void setImportDate(String importDate) {
		this.importDate = importDate;
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
	public String getRcHead() {
		return rcHead;
	}
	public void setRcHead(String rcHead) {
		this.rcHead = rcHead;
	}
}
