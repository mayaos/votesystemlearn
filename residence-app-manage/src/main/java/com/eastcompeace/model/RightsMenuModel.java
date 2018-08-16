package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 权益信息对应实体
 * @author cui
 *
 */
@Alias(value="RightsMenu")
public class RightsMenuModel {

	private Integer rightsId; 

	private String rightsName;   //权益名称

	private Integer rightsOrder;  //权益排序

	private String rightsStatus;  //权益状态（可用/不可用）

	private String rightsLogo;     //权益logo

	public Integer getRightsId() {
		return rightsId;
	}

	public void setRightsId(Integer rightsId) {
		this.rightsId = rightsId;
	}

	public String getRightsName() {
		return rightsName;
	}

	public void setRightsName(String rightsName) {
		this.rightsName = rightsName;
	}

	public Integer getRightsOrder() {
		return rightsOrder;
	}

	public void setRightsOrder(Integer rightsOrder) {
		this.rightsOrder = rightsOrder;
	}

	public String getRightsStatus() {
		return rightsStatus;
	}

	public void setRightsStatus(String rightsStatus) {
		this.rightsStatus = rightsStatus;
	}

	public String getRightsLogo() {
		return rightsLogo;
	}

	public void setRightsLogo(String rightsLogo) {
		this.rightsLogo = rightsLogo;
	}
	
	
}
