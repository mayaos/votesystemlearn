package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

import com.eastcompeace.util.ResourceUtils;
/**
 * 商家信息对于实体
 * 
 * @author cuichenyao
 *
 */
@Alias(value="MerchantInfo")
public class MerchantInfoModel {

	
	private String merchantId;         //商家id
	
    private String areaID;             //区域id

    private String merchantName;         //商家名称
    
    private String merchantHeadName;         //商家连锁总店名称

    private String industryType;        //行业类型

    private String merchantType;         //商家类型
    
    private String merchantNature;          // 商家性质    0: 直营店    1: 连锁店
    
    private String merchantChain;       // 连锁类型    0: 总店        1: 分店
 
    private String contacts;              //联系人

    private String telephone;              //联系电话

    private String address;                //地址

    private String description;            //商家描述

    private String createTime;                //创建时间

    private String changeTime;                 //修改时间

    private String photo1;                   //照片1(磁盘存储路径)

    private String photo2;                    //照片2(磁盘存储路径)

    private String photo3;                     //照片3(磁盘存储路径)
    
    private String lecenseImage;               //营业执照照片
    
    private String merchantLogo;                 //商家logo
    

    private String areaName;             //区域名称
    private String industryNote;        //行业类型
    private String merchantNote;         //商家类型

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getAreaID() {
		return areaID;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public void setAreaID(String areaID) {
		this.areaID = areaID;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantHeadName() {
		return merchantHeadName;
	}

	public void setMerchantHeadName(String merchantHeadName) {
		this.merchantHeadName = merchantHeadName;
	}

	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}

	public String getPhoto1() {
		return photo1;
	}

	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}

	public String getPhoto2() {
		return photo2;
	}

	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}

	public String getPhoto3() {
		return photo3;
	}

	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}

	public String getLecenseImage() {
		return lecenseImage;
	}

	public void setLecenseImage(String lecenseImage) {
		this.lecenseImage = lecenseImage;
	}

	public String getMerchantLogo() {
		return merchantLogo;
	}

	public void setMerchantLogo(String merchantLogo) {
		this.merchantLogo = merchantLogo;
	}

	public String getIndustryNote() {
		return industryNote;
	}

	public void setIndustryNote(String industryNote) {
		this.industryNote = industryNote;
	}

	public String getMerchantNote() {
		return merchantNote;
	}

	public void setMerchantNote(String merchantNote) {
		this.merchantNote = merchantNote;
	}

	public String getMerchantNature() {
		return merchantNature;
	}

	public void setMerchantNature(String merchantNature) {
		this.merchantNature = merchantNature;
	}

	public String getMerchantChain() {
		return merchantChain;
	}

	public void setMerchantChain(String merchantChain) {
		this.merchantChain = merchantChain;
	}

}
