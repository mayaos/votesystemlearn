package com.eastcompeace.model;
import java.io.Serializable;
public class AdvInfoModel implements Serializable {
    private String id;
    private String uri;
    private String advDesc;
    private String redirectUrl;
    private String valid;
    private String creator;
    private String createTime;
    private String updateTime;
    // 发布区域
    private String areaNameList;
    private String advImgUrl;
    private String areaIdList;

    public AdvInfoModel() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getAdvDesc() {
        return advDesc;
    }

    public void setAdvDesc(String advDesc) {
        this.advDesc = advDesc;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getAreaNameList() {
        return areaNameList;
    }

    public void setAreaNameList(String areaNameList) {
        this.areaNameList = areaNameList;
    }

    public String getAreaIdList() {
        return areaIdList;
    }

    public void setAreaIdList(String areaIdList) {
        this.areaIdList = areaIdList;
    }

    public String getAdvImgUrl() {
        return advImgUrl;
    }

    public void setAdvImgUrl(String advImgUrl) {
        this.advImgUrl = advImgUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public String toString() {
        return "AdvInfoModel [id=" + id + ", uri=" + uri + ", advDesc=" + advDesc + ", redirectUrl=" + redirectUrl + ", valid=" + valid + ", creator=" + creator + ", createTime=" + createTime + ", updateTime=" + updateTime + ", areaNameList=" + areaNameList + ", advImgUrl=" + advImgUrl + ", areaIdList=" + areaIdList + "]";
    }
}
