package com.eastcompeace.model;
import java.io.Serializable;
public class AdvMappingModel implements Serializable {
    private String id;
    private String imgId;
    private String areaId;
    private String createTime;
    private String creator;

    public AdvMappingModel() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "AdvMappingModel [id=" + id + ", imgId=" + imgId + ", areaId=" + areaId + ", createTime=" + createTime + ", creator=" + creator + "]";
    }
}
