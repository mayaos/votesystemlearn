package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 商家优惠券表对应实体
 * 
 * @author cuichenyao
 *
 */
@Alias(value="MerchantCoupon")
public class MerchantCouponModel {

	private String couponId;  //优惠券id

    private String benefitId;  //优惠信息id

    private String couponCode;  //优惠券码

    private String status;      //优惠券状态

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    
}
