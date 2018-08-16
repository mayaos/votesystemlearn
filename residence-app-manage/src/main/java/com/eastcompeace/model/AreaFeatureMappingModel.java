package com.eastcompeace.model;
public class AreaFeatureMappingModel {
    private Integer id;
    private Integer areaFeatureId;
    private String  areaId;
    private String  createTime;
    private String  creator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public Integer getAreaFeatureId() {
        return areaFeatureId;
    }

    public void setAreaFeatureId(Integer areaFeatureId) {
        this.areaFeatureId = areaFeatureId;
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
        return "AreaFeatureMappingModel [id=" + id + ", areaFeatureId=" + areaFeatureId + ", areaId=" + areaId + ", createTime=" + createTime + ", creator=" + creator + "]";
    }


}
