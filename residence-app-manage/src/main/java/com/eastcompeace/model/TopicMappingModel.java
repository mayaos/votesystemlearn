package com.eastcompeace.model;
public class TopicMappingModel {
    private Integer id;
    private Integer topicId;
    private String  areaId;
    private String  createTime;
    private String  creator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
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
        return "TopicMappingModel [id=" + id + ", topicId=" + topicId + ", areaId=" + areaId + ", createTime=" + createTime + ", creator=" + creator + "]";
    }
}
