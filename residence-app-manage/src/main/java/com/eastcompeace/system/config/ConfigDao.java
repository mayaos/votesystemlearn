package com.eastcompeace.system.config;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.ConfigModel;

/**
 * 配置DAO接口
 * 
 * @author zhuchaochao
 */

@Transactional
public interface ConfigDao extends BaseDao{
	/**
	 * 查询Config分页列表
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public List<ConfigModel> queryConfigList(ConfigModel config) throws Exception;
	
	
	/**
	 * 查询Config
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public ConfigModel queryConfig(ConfigModel config) throws Exception;

	/**
	 * 新增配置
	 * 
	 * @param config
	 * @throws Exception
	 */
	public void insertConfig(ConfigModel config) throws Exception;

	/**
	 * 修改配置
	 * 
	 * @param config
	 * @throws Exception
	 */
	public void updateConfig(ConfigModel config) throws Exception;

	/**
	 * 根据配置ID，删除配置
	 * 
	 * @param paramId
	 * @throws Exception
	 */
	public void deleteConfig(String paramId) throws Exception;
}