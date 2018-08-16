package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

@Alias("MerchantVip")
public class MerchantVipModel {
	private String vipcardId;  //会员卡ID
	private String merchantId; //商家Id
	private String vipNoType;  //会员卡号类型
	private String vipDesc;    //会员卡描述
	private String vipRule;    //会员卡规则
	private String merchantName; //商家名称
	private String createTime;   //信息生成时间
	private String changeTime;   //信息修改时间
	private String areaName;     //区域名称
	private String areaId;       //区域ID
	
	
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
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
	public String getVipcardId() {
		return vipcardId;
	}
	public void setVipcardId(String vipcardId) {
		this.vipcardId = vipcardId;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getVipNoType() {
		return vipNoType;
	}
	public void setVipNoType(String vipNoType) {
		this.vipNoType = vipNoType;
	}
	public String getVipDesc() {
		return vipDesc;
	}
	public void setVipDesc(String vipDesc) {
		this.vipDesc = vipDesc;
	}
	public String getVipRule() {
		return vipRule;
	}
	public void setVipRule(String vipRule) {
		this.vipRule = vipRule;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	
}
