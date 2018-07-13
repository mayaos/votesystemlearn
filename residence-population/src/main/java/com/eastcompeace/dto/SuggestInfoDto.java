package com.eastcompeace.dto;

/**
 * 
 * @FileName SuggestInfoDto.java
 * @Date   2018年6月12日上午9:35:26
 * @author wzh
 *
 * 意见建议信息实体类
 *
 */
public class SuggestInfoDto {
	private String sugType;	//发布类型(意见,建议)
	private String sugUser;	//发布者姓名
	private String sugTime;	//发布时间
	private String sugCont;	//意见建议内容
	private String sugRepl;	//官方答复
	
	public String getSugType() {
		return sugType;
	}
	public void setSugType(String sugType) {
		this.sugType = sugType;
	}
	public String getSugUser() {
		return sugUser;
	}
	public void setSugUser(String sugUser) {
		this.sugUser = sugUser;
	}
	public String getSugTime() {
		return sugTime;
	}
	public void setSugTime(String sugTime) {
		this.sugTime = sugTime;
	}
	public String getSugCont() {
		return sugCont;
	}
	public void setSugCont(String sugCont) {
		this.sugCont = sugCont;
	}
	public String getSugRepl() {
		return sugRepl;
	}
	public void setSugRepl(String sugRepl) {
		this.sugRepl = sugRepl;
	}
}
