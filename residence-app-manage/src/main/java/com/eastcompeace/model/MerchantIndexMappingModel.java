package com.eastcompeace.model;
public class MerchantIndexMappingModel {
    private Integer id;
    private Integer merchantIndexId;
    private String  areaId;
    private String  createTime;
    private String  creator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public Integer getMerchantIndexId() {
        return merchantIndexId;
    }

    public void setMerchantIndexId(Integer merchantIndexId) {
        this.merchantIndexId = merchantIndexId;
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
        return "MerchantIndexMappingModel [id=" + id + ", merchantIndexId=" + merchantIndexId + ", areaId=" + areaId + ", createTime=" + createTime + ", creator=" + creator + "]";
    }


}
