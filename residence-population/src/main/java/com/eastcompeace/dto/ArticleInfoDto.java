package com.eastcompeace.dto;

/**
 * 
 * @FileName ArticleInfoDto.java
 * @Date   2018年5月30日下午4:35:20
 * @author wzh
 *
 * 文章信息实体类
 *
 */
public class ArticleInfoDto {
	private String articleID;			//文章ID
	private String articleTitle;		//文章标题
	private String articlePicURL;		//文章图片链接URL
	private String articleDate;			//文章发布日期
	private String articleAuthor;		//文章作者
	private String articleFrom;			//文章来源
	private String articleContent;		//文章内容
	private String beforeArticleTitle;	//上一个文章标题
	private String beforeArticleID;		//上一个文章ID
	private String afterArticleTitle;	//下一个文章标题
	private String afterArticleID;		//下一个文章ID
	public String getArticleID() {
		return articleID;
	}
	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}
	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public String getArticlePicURL() {
		return articlePicURL;
	}
	public void setArticlePicURL(String articlePicURL) {
		this.articlePicURL = articlePicURL;
	}
	public String getArticleDate() {
		return articleDate;
	}
	public void setArticleDate(String articleDate) {
		this.articleDate = articleDate;
	}
	public String getArticleAuthor() {
		return articleAuthor;
	}
	public void setArticleAuthor(String articleAuthor) {
		this.articleAuthor = articleAuthor;
	}
	public String getArticleFrom() {
		return articleFrom;
	}
	public void setArticleFrom(String articleFrom) {
		this.articleFrom = articleFrom;
	}
	public String getArticleContent() {
		return articleContent;
	}
	public void setArticleContent(String articleContent) {
		this.articleContent = articleContent;
	}
	public String getBeforeArticleTitle() {
		return beforeArticleTitle;
	}
	public void setBeforeArticleTitle(String beforeArticleTitle) {
		this.beforeArticleTitle = beforeArticleTitle;
	}
	public String getBeforeArticleID() {
		return beforeArticleID;
	}
	public void setBeforeArticleID(String beforeArticleID) {
		this.beforeArticleID = beforeArticleID;
	}
	public String getAfterArticleTitle() {
		return afterArticleTitle;
	}
	public void setAfterArticleTitle(String afterArticleTitle) {
		this.afterArticleTitle = afterArticleTitle;
	}
	public String getAfterArticleID() {
		return afterArticleID;
	}
	public void setAfterArticleID(String afterArticleID) {
		this.afterArticleID = afterArticleID;
	}
}
