package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 流口照片实体类
 * 
 * @author caobo 20151214
 */
@Alias(value = "CitizenPhotoInfo")
public class CitizenPhotoInfoModel {
	private String id; //主键Id（batchNo+idcardNo）
	private String batchNo;
	private String idcardNo;
	private String photoUrl;
	private String photoBase64;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getIdcardNo() {
		return this.idcardNo;
	}

	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}

	public String getPhotoUrl() {
		return this.photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getPhotoBase64() {
		return this.photoBase64;
	}

	public void setPhotoBase64(String photoBase64) {
		this.photoBase64 = photoBase64;
	}

}
