package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

@Alias("AccessLog")
public class AccessLogModel {
	private String citizenId;
	private String userName;
	private String accessFrom;
	private String requestUrl;
	private String requestTime;
	
	//条件查询
	private String startTime;
	private String endTime;
	
	public AccessLogModel() {}

	public String getCitizenId() {
		return citizenId;
	}

	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccessFrom() {
		return accessFrom;
	}

	public void setAccessFrom(String accessFrom) {
		this.accessFrom = accessFrom;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
