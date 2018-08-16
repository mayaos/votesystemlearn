package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;
/**
 * 
 * 商家优惠信息对于实体
 * 
 * @author cuichenyao
 *
 */
@Alias(value = "MerchantBenefit")
public class MerchantBenefitModel {

	private String benefitId;        //优惠信息Id
	
    private String benefitName;      //优惠信息名称

    private String merchantId;       //商家Id

    private String merchantName;      //商家名称
    
    private String benefitQuota;  //优惠额度

    private String benefitCount;     //优惠总数量

    private String useExplain;        //使用条件

    private String description;       //描述

    private String limitedCount;     //限制每人领取数量

    private String createTime;          //创建时间

    private String changeTime;          //修改时间

    private String validTime;           //有效期

    private String benefitImage;     //优惠信息图片（base64）
    
    private String recommendFlag;  // 是否推荐标志[1-推荐/0-不推荐]

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getBenefitQuota() {
		return benefitQuota;
	}

	public void setBenefitQuota(String benefitQuota) {
		this.benefitQuota = benefitQuota;
	}

	public String getBenefitCount() {
		return benefitCount;
	}

	public void setBenefitCount(String benefitCount) {
		this.benefitCount = benefitCount;
	}

	public String getUseExplain() {
		return useExplain;
	}

	public void setUseExplain(String useExplain) {
		this.useExplain = useExplain;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLimitedCount() {
		return limitedCount;
	}

	public void setLimitedCount(String limitedCount) {
		this.limitedCount = limitedCount;
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

	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	public String getBenefitImage() {
		return benefitImage;
	}

	public void setBenefitImage(String benefitImage) {
		this.benefitImage = benefitImage;
	}

	public String getRecommendFlag() {
		return recommendFlag;
	}

	public void setRecommendFlag(String recommendFlag) {
		this.recommendFlag = recommendFlag;
	}
    
}
