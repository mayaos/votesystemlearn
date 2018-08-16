package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

@Alias(value = "button")
public class ButtonModel {
	private String uuid;// 主键ID
	private String text;// 显示名称
	private String icon;// icon图标
	private String handler;// 实现方法
	private String desc;// 排序
	private String status;// 状态:1-启用2-未启用

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
