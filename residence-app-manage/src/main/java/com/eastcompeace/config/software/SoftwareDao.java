package com.eastcompeace.config.software;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.model.SoftwareModel;

@Transactional
@Repository
public interface SoftwareDao {
	/**
	 * 查询软件信息列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<SoftwareModel> querySoftwareList(Map<String,Object> paramMap) throws Exception;
	/**
	 * 插入软件信息
	 * @param softwareModel
	 * @throws Exception
	 */
	public void addSoftwareInfo(SoftwareModel softwareModel) throws Exception;
	/**
	 * 删除软件信息
	 * @param softId
	 * @throws Exception
	 */
	public void delSoftwareInfo(String softId) throws Exception;
	/**
	 * 根据传入的参数信息查询信息是否存在
	 * @param params
	 * @return 0：信息不存在；
	 * 			其他数字则表示存在
	 * @throws Exception
	 */
	public Integer querySoftwareByParams(Map<String,Object> params) throws Exception;
	/**
	 * 根据软件类型、软件版本查询数据库，返回软件信息类，
	 * 用于软件更新前进行软件信息唯一性判断
	 * @param softType
	 * @param softVersion
	 * @return
	 * @throws Exception
	 */
	public List<SoftwareModel> queryBySoftTypeAndSoftVersion(@Param("softType")String softType, @Param("softVersion")String softVersion) throws Exception;
	/**
	 * 更新软件信息
	 * @param softwareModel
	 * @throws Exception
	 */
	public void updateSoftware(SoftwareModel softwareModel) throws Exception;
}
