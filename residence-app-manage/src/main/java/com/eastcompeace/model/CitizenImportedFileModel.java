package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 流口数据导入文件实体类
 * @author caobo,xiaoyiyi
 */
@Alias(value ="CitizenImportedFile")
public class CitizenImportedFileModel {

	private String serializeNo;
	private String dataFileName;
	private String photoFileName;
	private String zipPhotoFileName;
	private String importDate;
	public String getSerializeNo() {
		return this.serializeNo;
	}
	public void setSerializeNo(String serializeNo) {
		this.serializeNo = serializeNo;
	}
	public String getDataFileName() {
		return this.dataFileName;
	}
	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}
	public String getPhotoFileName() {
		return this.photoFileName;
	}
	public void setPhotoFileName(String photoFileName) {
		this.photoFileName = photoFileName;
	}
	public String getImportDate() {
		return this.importDate;
	}
	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}
	public String getZipPhotoFileName() {
		return zipPhotoFileName;
	}
	public void setZipPhotoFileName(String zipPhotoFileName) {
		this.zipPhotoFileName = zipPhotoFileName;
	}
}
