package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;
/**
 * 轻应用文章表实体
 * @author xianzehua
 *
 */
@Alias(value="ArticleInfo")
public class OrgArticleModel {

	private String articleId; //权益文章ID
	private String menuId; //菜单ID
	private String menuName; //机构名称
	private String areaId; //区域ID
	private String areaName; //区域名称
	private String orgId; //机构ID
	private String orgName; //机构名称
	private String articleTitle; //文章标题
	private String articleAuthor; //文章作者
	private String articleFrom; //文章来源
	private String topicPic; //文章图片链接
	private String articleDesc; //文章简要描述
	private String articleContent; //文章内容
	private String readTimes; //浏览次数
	private String likeTimes; //点赞次数
	private String issueFlag; //是否发布标志
	private Integer userId; //管理员ID
	private String issueTime; //文章发表时间
	private String createTime; //创建时间
	private String updateTime; //更新时间
	private String userCode; //用户账号
	private String articleType;//文章类型
	private String articleLink;//文章链接
	
	
	public String getArticleType() {
        return articleType;
    }
    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }
    public String getArticleLink() {
        return articleLink;
    }
    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }
    public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public String getArticleAuthor() {
		return articleAuthor;
	}
	public void setArticleAuthor(String articleAuthor) {
		this.articleAuthor = articleAuthor;
	}
	public String getTopicPic() {
		return topicPic;
	}
	public void setTopicPic(String topicPic) {
		this.topicPic = topicPic;
	}
	public String getArticleDesc() {
		return articleDesc;
	}
	public void setArticleDesc(String articleDesc) {
		this.articleDesc = articleDesc;
	}

	public String getArticleContent() {
		return articleContent;
	}
	public void setArticleContent(String articleContent) {
		this.articleContent = articleContent;
	}
	public String getReadTimes() {
		return readTimes;
	}
	public void setReadTimes(String readTimes) {
		this.readTimes = readTimes;
	}
	public String getLikeTimes() {
		return likeTimes;
	}
	public void setLikeTimes(String likeTimes) {
		this.likeTimes = likeTimes;
	}
	public String getIssueFlag() {
		return issueFlag;
	}
	public void setIssueFlag(String issueFlag) {
		this.issueFlag = issueFlag;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getIssueTime() {
		return issueTime;
	}
	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
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
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getArticleFrom() {
		return articleFrom;
	}
	public void setArticleFrom(String articleFrom) {
		this.articleFrom = articleFrom;
	}
	
}
