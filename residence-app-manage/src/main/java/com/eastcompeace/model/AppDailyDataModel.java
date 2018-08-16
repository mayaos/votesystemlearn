package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

@Alias("AppDailyData")
public class AppDailyDataModel {
	private String appDownloadCount;//APP当天下载量
	private String activeCount;//当天活跃人数
	private String registeCount;//注册总人数
	private String idAuthCount;//实名认证总人数
	private String rcAuthCount;//居住证认证总人数
	private String interactiveCount;//当日前端与后台交互次数
	private String recordDate;//记录日期
	
	public AppDailyDataModel() {}
	

	public String getInteractiveCount() {
		return interactiveCount;
	}


	public void setInteractiveCount(String interactiveCount) {
		this.interactiveCount = interactiveCount;
	}


	public String getAppDownloadCount() {
		return appDownloadCount;
	}
	public void setAppDownloadCount(String appDownloadCount) {
		this.appDownloadCount = appDownloadCount;
	}
	public String getActiveCount() {
		return activeCount;
	}
	public void setActiveCount(String activeCount) {
		this.activeCount = activeCount;
	}
	public String getRegisteCount() {
		return registeCount;
	}
	public void setRegisteCount(String registeCount) {
		this.registeCount = registeCount;
	}
	public String getIdAuthCount() {
		return idAuthCount;
	}
	public void setIdAuthCount(String idAuthCount) {
		this.idAuthCount = idAuthCount;
	}
	public String getRcAuthCount() {
		return rcAuthCount;
	}
	public void setRcAuthCount(String rcAuthCount) {
		this.rcAuthCount = rcAuthCount;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	
}
