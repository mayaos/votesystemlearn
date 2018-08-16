package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 我的优惠券
 * @author ecp-zeng
 * @date 2017年1月10日
 */
@Alias(value = "citizenCoupon")
public class CitizenCouponModel{
	private String citizenID; //用户ID
	private String couponID; //优惠券ID

	/*优惠活动信息和商家信息*/
	private String areaID; //区域ID
	private String merchantID; //商家名称ID
	private String benefitID; //优惠信息ID
	private String benefitName; //优惠信息名称
	private String merchant; //商家名称
	private String couponCode; //券码（‘我的优惠券’功能中存在）
	private String quota;   //额度
	private String status;	//优惠券状态
	private String useExplain; //使用条件(范围)
	private String description; //描述
	private String imgUrl; //优惠券图片链接http:
	
	public String getCitizenID() {
		return citizenID;
	}
	public void setCitizenID(String citizenID) {
		this.citizenID = citizenID;
	}
	public String getCouponID() {
		return couponID;
	}
	public void setCouponID(String couponID) {
		this.couponID = couponID;
	}
	public String getAreaID() {
		return areaID;
	}
	public void setAreaID(String areaID) {
		this.areaID = areaID;
	}
	public String getMerchantID() {
		return merchantID;
	}
	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}
	public String getBenefitID() {
		return benefitID;
	}
	public void setBenefitID(String benefitID) {
		this.benefitID = benefitID;
	}
	public String getBenefitName() {
		return benefitName;
	}
	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}
	public String getMerchant() {
		return merchant;
	}
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getQuota() {
		return quota;
	}
	public void setQuota(String quota) {
		this.quota = quota;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUseExplain() {
		return useExplain;
	}
	public void setUseExplain(String useExplain) {
		this.useExplain = useExplain;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	
}
