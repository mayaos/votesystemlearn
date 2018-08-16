package com.eastcompeace.model;
import org.apache.ibatis.type.Alias;
/**
 * 公共信息实体类
 * 
 * @author Administrator
 */
@Alias(value = "PubTopic")
public class PubTopicModel {
    private String  topicTypeValue; // 资讯类型码值
    private String  topicType;     // 资讯类型
    private String  areaNameList;  // 发布区域名集合
    private String  areaIdList;    // 发布区域id集合
    private Integer topicId;       // 文章ID
    private String  topicTitle;    // 文章标题
    private String  topicFrom;     // 文章来源
    private String  issueTime;     // 发表时间
    private Integer readTimes;     // 阅读次数
    private Integer likeTimes;     // 点赞次数
    private String  topicPic;      // 文章图片
    private Integer userId;        // 管理员Id
    private String  createTime;    // 创建时间
    private String  updateTime;    // 更新时间
    private String  topicContent;  // 文章内容
    private String  topicDesc;     // 文章描述
    private String  userName;      // 文章内容
    /**文章是否置顶 0：不置顶 1：置顶**/
    private String topicBar;
    /** 文章排序 **/
    private String rank;
    
    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTopicBar() {
        return topicBar;
    }

    public void setTopicBar(String topicBar) {
        this.topicBar = topicBar;
    }

    public PubTopicModel() {
    }

    public PubTopicModel(Integer topicId) {
        super();
        this.topicId = topicId;
    }

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
    }

    public String getTopicDesc() {
        return topicDesc;
    }

    public void setTopicDesc(String topicDesc) {
        this.topicDesc = topicDesc;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public String getTopicFrom() {
        return topicFrom;
    }

    public void setTopicFrom(String topicFrom) {
        this.topicFrom = topicFrom;
    }

    public String getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(String issueTime) {
        this.issueTime = issueTime;
    }

    public Integer getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(Integer readTimes) {
        this.readTimes = readTimes;
    }

    public Integer getLikeTimes() {
        return likeTimes;
    }

    public void setLikeTimes(Integer likeTimes) {
        this.likeTimes = likeTimes;
    }

    public String getTopicPic() {
        return topicPic;
    }

    public void setTopicPic(String topicPic) {
        this.topicPic = topicPic;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTopicTypeValue() {
        return topicTypeValue;
    }

    public void setTopicTypeValue(String topicTypeValue) {
        this.topicTypeValue = topicTypeValue;
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

    @Override
    public String toString() {
        return "PubTopicModel [topicTypeValue=" + topicTypeValue + ", topicType=" + topicType + ", areaNameList=" + areaNameList + ", areaIdList=" + areaIdList + ", topicId=" + topicId + ", topicTitle=" + topicTitle + ", topicFrom=" + topicFrom + ", issueTime=" + issueTime + ", readTimes=" + readTimes + ", likeTimes=" + likeTimes + ", topicPic=" + topicPic + ", userId=" + userId + ", createTime=" + createTime + ", updateTime=" + updateTime + ", topicContent=" + topicContent + ", topicDesc=" + topicDesc + ", userName=" + userName + ", topicBar=" + topicBar + ", rank=" + rank + "]";
    }




}