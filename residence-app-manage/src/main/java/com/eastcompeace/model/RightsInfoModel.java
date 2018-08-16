package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;
/**
 * 权益详情表实体
 * @author Administrator
 *
 */
@Alias(value="RightsInfo")
public class RightsInfoModel {

	private String articleId; //权益文章ID
	private String rightsId; //权益ID
	private String areaId; //权益地区ID
	private String rightsFrom; //权益文章来源
	private String rightsTitle; //权益标题
	private String titleImage; //权益标题图片
	private String issueTime; //权益文章发表时间
	private Integer userId; //管理员Id
	private String createTime; //创建时间
	private String rightsContent; //权益文章
	private String rightsTypeName; //权益类型名称
	private String userCode; //用户账号
	private String areaName; //区域名
	
	//条件查询用
	private String uploadDateStart; //起始日期
	private String uploadDateEnd;  //截止日期
	
	public String getUploadDateStart() {
		return uploadDateStart;
	}

	public String getRightsFrom() {
		return rightsFrom;
	}

	public void setRightsFrom(String rightsFrom) {
		this.rightsFrom = rightsFrom;
	}

	public void setUploadDateStart(String uploadDateStart) {
		this.uploadDateStart = uploadDateStart;
	}

	public String getUploadDateEnd() {
		return uploadDateEnd;
	}

	public void setUploadDateEnd(String uploadDateEnd) {
		this.uploadDateEnd = uploadDateEnd;
	}

	public String getRightsTypeName() {
		return rightsTypeName;
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

	public void setRightsTypeName(String rightsTypeName) {
		this.rightsTypeName = rightsTypeName;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getRightsId() {
		return rightsId;
	}

	public void setRightsId(String rightsId) {
		this.rightsId = rightsId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getRightsTitle() {
		return rightsTitle;
	}

	public void setRightsTitle(String rightsTitle) {
		this.rightsTitle = rightsTitle;
	}

	public String getTitleImage() {
		return titleImage;
	}

	public void setTitleImage(String titleImage) {
		this.titleImage = titleImage;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
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

	public String getRightsContent() {
		return rightsContent;
	}

	public void setRightsContent(String rightsContent) {
		this.rightsContent = rightsContent;
	}
	
	
}
