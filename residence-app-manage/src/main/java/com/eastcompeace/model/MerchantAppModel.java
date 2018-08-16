package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

public class MerchantAppModel {
    /** 商家app ID*/
    private String id;
    /** 来源 1:微信公众号2:支付宝生活号 */
    private String fromType;
    /** 平台Id如果从微信公众号来，则是openId如果是支付宝生活号来，则是userId */
    private String platformId;
    /** 基础资料 0未填写 1已填写 */
    private String baseStatus;
    /** 实名认证状态 0.认证失败 1.已通过 2.审核中 */
    private String authStatus;
    /** 商户资质状态 0未填写 1.已填写 */
    private String qualiStatus;
    /** 姓名 */
    private String userName;
    /** 身份证号 */
    private String idCardNo;
    /** 商户名称 */
    private String merchantName;
    /** 公司名称 */
    private String companyName;
    /** 公司性质01:外企02:中外合资企业03:民营企业04:国企05:其他 */
    private String companyType;
    /** 地区编码 */
    private String division;
    /** 商家地址 */
    private String address;
    /** 主营类目 */
    private String mainCategory;
    /** 联系人 */
    private String contacts;
    /** 职务 */
    private String position;
    /** 联系人电话 */
    private String phone;
    /** 邮箱地址 */
    private String email;
    /** 其他说明 */
    private String others;
    /** 营业执照图片链接1 */
    private String licenseImg1;
    /** 营业执照图片链接2 */
    private String licenseImg2;
    /** 营业执照图片链接3 */
    private String licenseImg3;
    /** 店面照片图片链接1 */
    private String storefrontPhotos1;
    /** 店面照片图片链接2 */
    private String storefrontPhotos2;
    /** 店面照片图片链接3 */
    private String storefrontPhotos3;
    /** 失败错误信息,成功时为空 */
    private String auditFailedMsg;
    /**0 未填写资料 1:未填写完,已填写部分资料 2:已填写资料但未通过审核 3:全部审核通过 4:审核失败*/
    private String step;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFromType() {
        return fromType;
    }
    public void setFromType(String fromType) {
        this.fromType = fromType;
    }
    public String getPlatformId() {
        return platformId;
    }
    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }
    public String getBaseStatus() {
        return baseStatus;
    }
    public void setBaseStatus(String baseStatus) {
        this.baseStatus = baseStatus;
    }
    public String getAuthStatus() {
        return authStatus;
    }
    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }
    public String getQualiStatus() {
        return qualiStatus;
    }
    public void setQualiStatus(String qualiStatus) {
        this.qualiStatus = qualiStatus;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getIdCardNo() {
        return idCardNo;
    }
    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }
    public String getMerchantName() {
        return merchantName;
    }
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getCompanyType() {
        return companyType;
    }
    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }
    public String getDivision() {
        return division;
    }
    public void setDivision(String division) {
        this.division = division;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getMainCategory() {
        return mainCategory;
    }
    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }
    public String getContacts() {
        return contacts;
    }
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getOthers() {
        return others;
    }
    public void setOthers(String others) {
        this.others = others;
    }
    public String getLicenseImg1() {
        return licenseImg1;
    }
    public void setLicenseImg1(String licenseImg1) {
        this.licenseImg1 = licenseImg1;
    }
    public String getLicenseImg2() {
        return licenseImg2;
    }
    public void setLicenseImg2(String licenseImg2) {
        this.licenseImg2 = licenseImg2;
    }
    public String getLicenseImg3() {
        return licenseImg3;
    }
    public void setLicenseImg3(String licenseImg3) {
        this.licenseImg3 = licenseImg3;
    }
    public String getStorefrontPhotos1() {
        return storefrontPhotos1;
    }
    public void setStorefrontPhotos1(String storefrontPhotos1) {
        this.storefrontPhotos1 = storefrontPhotos1;
    }
    public String getStorefrontPhotos2() {
        return storefrontPhotos2;
    }
    public void setStorefrontPhotos2(String storefrontPhotos2) {
        this.storefrontPhotos2 = storefrontPhotos2;
    }
    public String getStorefrontPhotos3() {
        return storefrontPhotos3;
    }
    public void setStorefrontPhotos3(String storefrontPhotos3) {
        this.storefrontPhotos3 = storefrontPhotos3;
    }
    public String getAuditFailedMsg() {
        return auditFailedMsg;
    }
    public void setAuditFailedMsg(String auditFailedMsg) {
        this.auditFailedMsg = auditFailedMsg;
    }
    public String getStep() {
        return step;
    }
    public void setStep(String step) {
        this.step = step;
    }
    @Override
    public String toString() {
        return "MerchantAppModel [id=" + id + ", fromType=" + fromType + ", platformId=" + platformId + ", baseStatus=" + baseStatus + ", authStatus=" + authStatus + ", qualiStatus=" + qualiStatus + ", userName=" + userName + ", idCardNo=" + idCardNo + ", merchantName=" + merchantName + ", companyName=" + companyName + ", companyType=" + companyType + ", division=" + division + ", address=" + address + ", mainCategory=" + mainCategory + ", contacts=" + contacts + ", position=" + position + ", phone=" + phone + ", email=" + email + ", others=" + others + ", licenseImg1=" + licenseImg1 + ", licenseImg2=" + licenseImg2 + ", licenseImg3=" + licenseImg3 + ", storefrontPhotos1=" + storefrontPhotos1 + ", storefrontPhotos2=" + storefrontPhotos2 + ", storefrontPhotos3=" + storefrontPhotos3
                + ", auditFailedMsg=" + auditFailedMsg + ", step=" + step + "]";
    }

    
    
    
    

}
