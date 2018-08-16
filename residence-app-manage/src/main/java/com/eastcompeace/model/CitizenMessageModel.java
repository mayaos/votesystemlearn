package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 居民消息表
 * @author ecp-zeng
 * @date 2017年1月4日
 */
@Alias(value ="citizenMsg")
public class CitizenMessageModel {
	private String messageID;
	private String citizenID;
	private String messageTitle;
	private String messageContent;
	private String messageStatus;
	private String createTime;
	private String messageType;
	
	public String getCitizenID() {
		return citizenID;
	}
	public void setCitizenID(String citizenID) {
		this.citizenID = citizenID;
	}
	public String getMessageID() {
		return messageID;
	}
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	public String getMessageTitle() {
		return messageTitle;
	}
	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public String getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
}
