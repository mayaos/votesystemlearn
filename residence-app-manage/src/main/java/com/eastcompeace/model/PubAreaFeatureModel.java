package com.eastcompeace.model;
import org.apache.ibatis.type.Alias;
/**
 * 公共信息实体类
 * 
 * @author Administrator
 */
@Alias(value = "PubareaFeature")
public class PubAreaFeatureModel {
    private String  areaFeatureTypeValue; // 地方特色类型码值
    private String  areaFeatureType;     // 地方特色类型
    private String  areaNameList;  // 发布区域名集合
    private String  areaIdList;    // 发布区域id集合
    private Integer areaFeatureId;       // 文章ID
    private String  areaFeatureTitle;    // 文章标题
    private String  areaFeatureFrom;     // 文章来源
    private String  issueTime;     // 发表时间
    private Integer readTimes;     // 阅读次数
    private Integer likeTimes;     // 点赞次数
    private String  areaFeaturePic;      // 文章图片
    private Integer userId;        // 管理员Id
    private String  createTime;    // 创建时间
    private String  updateTime;    // 更新时间
    private String  areaFeatureContent;  // 文章内容
    private String  areaFeatureDesc;     // 文章描述
    private String  userName;      // 文章内容
    /**文章是否置顶 0：不置顶 1：置顶**/
    private String areaFeatureBar;
    /** 文章排序 **/
    private String rank;
    
    public PubAreaFeatureModel() {
    }

    public PubAreaFeatureModel(Integer areaFeatureId) {
        super();
        this.areaFeatureId = areaFeatureId;
    }    
    
    public String getAreaFeatureTypeValue() {
        return areaFeatureTypeValue;
    }
    public void setAreaFeatureTypeValue(String areaFeatureTypeValue) {
        this.areaFeatureTypeValue = areaFeatureTypeValue;
    }
    public String getAreaFeatureType() {
        return areaFeatureType;
    }
    public void setAreaFeatureType(String areaFeatureType) {
        this.areaFeatureType = areaFeatureType;
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
    public Integer getAreaFeatureId() {
        return areaFeatureId;
    }
    public void setAreaFeatureId(Integer areaFeatureId) {
        this.areaFeatureId = areaFeatureId;
    }
    public String getAreaFeatureTitle() {
        return areaFeatureTitle;
    }
    public void setAreaFeatureTitle(String areaFeatureTitle) {
        this.areaFeatureTitle = areaFeatureTitle;
    }
    public String getAreaFeatureFrom() {
        return areaFeatureFrom;
    }
    public void setAreaFeatureFrom(String areaFeatureFrom) {
        this.areaFeatureFrom = areaFeatureFrom;
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
    public String getAreaFeaturePic() {
        return areaFeaturePic;
    }
    public void setAreaFeaturePic(String areaFeaturePic) {
        this.areaFeaturePic = areaFeaturePic;
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
    public String getAreaFeatureContent() {
        return areaFeatureContent;
    }
    public void setAreaFeatureContent(String areaFeatureContent) {
        this.areaFeatureContent = areaFeatureContent;
    }
    public String getAreaFeatureDesc() {
        return areaFeatureDesc;
    }
    public void setAreaFeatureDesc(String areaFeatureDesc) {
        this.areaFeatureDesc = areaFeatureDesc;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getAreaFeatureBar() {
        return areaFeatureBar;
    }
    public void setAreaFeatureBar(String areaFeatureBar) {
        this.areaFeatureBar = areaFeatureBar;
    }
    public String getRank() {
        return rank;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    @Override
    public String toString() {
        return "PubAreaFeatureModel [areaFeatureTypeValue=" + areaFeatureTypeValue + ", areaFeatureType=" + areaFeatureType + ", areaNameList=" + areaNameList + ", areaIdList=" + areaIdList + ", areaFeatureId=" + areaFeatureId + ", areaFeatureTitle=" + areaFeatureTitle + ", areaFeatureFrom=" + areaFeatureFrom + ", issueTime=" + issueTime + ", readTimes=" + readTimes + ", likeTimes=" + likeTimes + ", areaFeaturePic=" + areaFeaturePic + ", userId=" + userId + ", createTime=" + createTime + ", updateTime=" + updateTime + ", areaFeatureContent=" + areaFeatureContent + ", areaFeatureDesc=" + areaFeatureDesc + ", userName=" + userName + ", areaFeatureBar=" + areaFeatureBar + ", rank=" + rank + "]";
    }
    





}