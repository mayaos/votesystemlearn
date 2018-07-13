package com.eastcompeace.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @FileName SuggestTypeDto.java
 * @Date   2018年6月12日上午9:35:41
 * @author wzh
 *
 * 意见建议类别信息实体类
 *
 */
public class SuggestTypeDto {
	private String totalCount;	//记录总数
	private String currentPage;	//当前页
	private String totalPage;	//总页数
	private List<SuggestInfoDto> dataList = new ArrayList<SuggestInfoDto>();	//意见建议信息实体类
	
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
	public List<SuggestInfoDto> getDataList() {
		return dataList;
	}
	public void setDataList(List<SuggestInfoDto> dataList) {
		this.dataList = dataList;
	}
}
