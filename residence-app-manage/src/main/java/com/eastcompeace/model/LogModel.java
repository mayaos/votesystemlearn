package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;
/**
 * 日志实体类
 * @author xiangpei
 *
 */
@Alias(value = "Log")
public class LogModel  {

	private String logids;
	private Long id;
	private String userid;
	private String username;
	private String requesturl;
	private String ip;
	private String issuccess;
	private String happentime;
	private String happentimeStart;
	private String happentimeEnd;
	private String logcontent;
	
	/** 日志类型: 
	 *  1：登录日志 2：异常日志 3:查询操作 4：插入操作 5：修改操作 6：删除操作  7:审核操作
	 */
	private String logtype;

	public String getIssuccessStr() {
		if (this.getIssuccess() == null) {
			return "执行失败";
		}
		if (this.getIssuccess().equals("Y")) {
			return "执行成功";
		}
		return "执行失败";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIssuccess() {
		return issuccess;
	}

	public void setIssuccess(String issuccess) {
		this.issuccess = issuccess;
	}

	public String getHappentime() {
		return happentime;
	}

	public String getHappentimeStart() {
		return happentimeStart;
	}

	public void setHappentime(String happentime) {
		this.happentime = happentime;
	}

	public void setHappentimeStart(String happentimeStart) {
		this.happentimeStart = happentimeStart;
	}

	public String getHappentimeEnd() {
		return happentimeEnd;
	}

	public void setHappentimeEnd(String happentimeEnd) {
		this.happentimeEnd = happentimeEnd;
	}


	public String getLogcontent() {
		return logcontent;
	}

	public void setLogcontent(String logcontent) {
		this.logcontent = logcontent;
	}


	public String getLogtype() {
		return logtype;
	}

	/** 日志类型: 
	 *  1：登录日志 2：异常日志 3:查询操作 4：插入操作 5：修改操作 6：删除操作  7:审核操作
	 */
	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLogids() {
		return logids;
	}

	public void setLogids(String logids) {
		this.logids = logids;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRequesturl() {
		return requesturl;
	}

	public void setRequesturl(String requesturl) {
		this.requesturl = requesturl;
	}
	
	

}