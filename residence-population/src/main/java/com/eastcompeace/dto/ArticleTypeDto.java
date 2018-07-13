package com.eastcompeace.dto;

import java.util.ArrayList;
import java.util.List;

public class ArticleTypeDto {
	private String articleType;		//文章类别
	private String totalCount;		//文章总数
	private String currentPage;		//当前页
	private String totalPage;		//总页数
	private List<ArticleInfoDto> dataList = new ArrayList<>();	//文章信息实体类集合
	public String getArticleType() {
		return articleType;
	}
	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public String getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}
	public List<ArticleInfoDto> getDataList() {
		return dataList;
	}
	public void setDataList(List<ArticleInfoDto> dataList) {
		this.dataList = dataList;
	}
}
