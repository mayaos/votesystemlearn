package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;
/**
 * 按钮菜单对应实体类
 * @author xiangpei
 *
 */
@Alias(value = "menuButton")
public class MenuButtonModel {
	private int id;
	private String menuid;
	private String buttonid;

	private String buttontext;

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public String getButtonid() {
		return buttonid;
	}

	public void setButtonid(String buttonid) {
		this.buttonid = buttonid;
	}

	public String getButtontext() {
		return buttontext;
	}

	public void setButtontext(String buttontext) {
		this.buttontext = buttontext;
	}

}
