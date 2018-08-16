package com.eastcompeace.citizeninfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.model.CitizenImportedFileModel;
import com.eastcompeace.model.CitizenInfoModel;
import com.eastcompeace.model.CitizenPhotoInfoModel;


@Transactional
// 注解配置事务管理
@Repository("citizenInfoDao")
public interface CitizenInfoDao {

	/** 将流口数据集插入正式表 */
	public void importCitizenData(List<CitizenInfoModel> citizenInfoList) throws Exception;
	
	/**
	 * 删除流口数据
	 * @param serialNo  20160127
	 */
	public void deleteCitizenData(String serialNo) throws Exception;
	
	/**
	 * 查询居住证信息数据表中身份证号重复的数据的身份证号
	 * @return
	 * @throws Exception
	 */
	public List<CitizenInfoModel> queryDuplicateData() throws Exception;
	
	/**
	 * 根据提供的居住证号集合更新rc_card_info表中的rc_status字段状态
	 * @param list
	 * @throws Exception
	 */
	public void updateRcStatus(List<String> list) throws Exception;
	
	/**
	 * 将注销状态的数据复制到备份表中
	 * @throws Exception
	 */
	public void insertDataToBakTab() throws Exception;
	
	/**
	 * 删除重复的并且rc_status为01的数据
	 * @throws Exception
	 */
	public void delDuplicateData() throws Exception;
}