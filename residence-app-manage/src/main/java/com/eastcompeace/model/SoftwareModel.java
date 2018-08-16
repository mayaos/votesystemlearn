package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;
/**
 * 软件信息类
 * @author panyanlin
 *
 */
@Alias("Software")
public class SoftwareModel {
	private Integer softId; //软件ID
	private String softVersion; //软件版本
	private String softName; //软件名称
	private String softType; //软件类型
	private String softSize; //>软件大小
	private String softURL; //软件URL
	private String forceUpgrade; //强制升级
	private String createTime; //上传日期
	private String softNotes; //更新描述
	private String softHash; //哈希值
	
	
	public String getSoftHash() {
		return softHash;
	}

	public void setSoftHash(String softHash) {
		this.softHash = softHash;
	}


	public SoftwareModel() {}


	public SoftwareModel(Integer softId, String softVersion, String softName,
			String softType, String softSize, String softURL,
			String forceUpgrade, String createTime, String softNotes,
			String softHash) {
		super();
		this.softId = softId;
		this.softVersion = softVersion;
		this.softName = softName;
		this.softType = softType;
		this.softSize = softSize;
		this.softURL = softURL;
		this.forceUpgrade = forceUpgrade;
		this.createTime = createTime;
		this.softNotes = softNotes;
		this.softHash = softHash;
	}

	public Integer getSoftId() {
		return softId;
	}

	public void setSoftId(Integer softId) {
		this.softId = softId;
	}

	public String getSoftVersion() {
		return softVersion;
	}

	public void setSoftVersion(String softVersion) {
		this.softVersion = softVersion;
	}

	public String getSoftName() {
		return softName;
	}

	public void setSoftName(String softName) {
		this.softName = softName;
	}

	public String getSoftType() {
		return softType;
	}

	public void setSoftType(String softType) {
		this.softType = softType;
	}

	public String getSoftSize() {
		return softSize;
	}

	public void setSoftSize(String softSize) {
		this.softSize = softSize;
	}

	public String getSoftURL() {
		return softURL;
	}

	public void setSoftURL(String softURL) {
		this.softURL = softURL;
	}

	public String getForceUpgrade() {
		return forceUpgrade;
	}

	public void setForceUpgrade(String forceUpgrade) {
		this.forceUpgrade = forceUpgrade;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getSoftNotes() {
		return softNotes;
	}

	public void setSoftNotes(String softNotes) {
		this.softNotes = softNotes;
	}

}
