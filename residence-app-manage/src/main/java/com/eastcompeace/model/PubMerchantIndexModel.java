package com.eastcompeace.model;
import org.apache.ibatis.type.Alias;
/**
 * 商家前端页面显示实体类
 * 
 * @author Administrator
 */
@Alias(value = "PubMerchantIndex")
public class PubMerchantIndexModel {
    private String  merchantIndexType;     // 类型
    private String  areaNameList;  // 发布区域名集合
    private String  areaIdList;    // 发布区域id集合
    private Integer merchantIndexId;       // ID
    private String  merchantIndexTitle;    // 标题
    private String  issueTime;     // 发表时间
    private String  merchantIndexPic;      // 图片
    private Integer userId;        // 管理员Id
    private String  userName;      // 管理员姓名
    private String  createTime;    // 创建时间
    private String  updateTime;    // 更新时间
    private String  merchantIndexDesc;     // 描述
    /**是否置顶 0：不置顶 1：置顶**/
    private String merchantIndexBar;
    /** 排序 **/
    private String rank;
    /** 链接 **/
    private String link;
    public PubMerchantIndexModel() {
    }

    public PubMerchantIndexModel(Integer merchantIndexId) {
        super();
        this.merchantIndexId = merchantIndexId;
    } 
        
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMerchantIndexType() {
        return merchantIndexType;
    }
    public void setMerchantIndexType(String merchantIndexType) {
        this.merchantIndexType = merchantIndexType;
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
    public Integer getMerchantIndexId() {
        return merchantIndexId;
    }
    public void setMerchantIndexId(Integer merchantIndexId) {
        this.merchantIndexId = merchantIndexId;
    }
    public String getMerchantIndexTitle() {
        return merchantIndexTitle;
    }
    public void setMerchantIndexTitle(String merchantIndexTitle) {
        this.merchantIndexTitle = merchantIndexTitle;
    }
    public String getIssueTime() {
        return issueTime;
    }
    public void setIssueTime(String issueTime) {
        this.issueTime = issueTime;
    }
    public String getMerchantIndexPic() {
        return merchantIndexPic;
    }
    public void setMerchantIndexPic(String merchantIndexPic) {
        this.merchantIndexPic = merchantIndexPic;
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
    public String getMerchantIndexDesc() {
        return merchantIndexDesc;
    }
    public void setMerchantIndexDesc(String merchantIndexDesc) {
        this.merchantIndexDesc = merchantIndexDesc;
    }
    public String getMerchantIndexBar() {
        return merchantIndexBar;
    }
    public void setMerchantIndexBar(String merchantIndexBar) {
        this.merchantIndexBar = merchantIndexBar;
    }
    public String getRank() {
        return rank;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    @Override
    public String toString() {
        return "PubMerchantIndexModel [merchantIndexType=" + merchantIndexType + ", areaNameList=" + areaNameList + ", areaIdList=" + areaIdList + ", merchantIndexId=" + merchantIndexId + ", merchantIndexTitle=" + merchantIndexTitle + ", issueTime=" + issueTime + ", merchantIndexPic=" + merchantIndexPic + ", userId=" + userId + ", createTime=" + createTime + ", updateTime=" + updateTime + ", merchantIndexDesc=" + merchantIndexDesc + ", merchantIndexBar=" + merchantIndexBar + ", rank=" + rank + ", link=" + link + "]";
    }
    




}