package com.eastcompeace.usermng.auth;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.excel.AuthExcelModel;
import com.eastcompeace.model.excel.CitizenAuthExcelModel;

/**
 * 居民Dao
 * @author ecp-zeng
 * @date 2017年1月4日
 */
@Transactional
public interface AuthDao extends BaseDao{

	/**
	 * 查询导出Excel数据列表
	 * @param map
	 * @return
	 */
	public List<AuthExcelModel> queryExcelListBy(Map<String, Object> map) throws Exception;

	/**
	 * 查询导出Excel数据列表
	 * @param map
	 * @return
	 */
	public List<CitizenAuthExcelModel> queryAuthExcelListBy(Map<String, Object> map) throws Exception;

	/**
	 * 导入认证结果文件数据
	 * @param authList
	 */
	public void importMore(List<AuthExcelModel> authList) throws Exception;

	/**
	 * 清空临时导入表数据
	 */
	public void clearAuthTemp() throws Exception;

	/**
	 * 验证导入数据和跟新认证结果
	 * @param mParam
	 */
	public void verifyAndUpdateAuth(Map<String, Object> mParam) throws Exception;

	/**
	 * 获取表格中存在空值的用户信息
	 * @return
	 */
	public List<AuthExcelModel> getImportNullList();

	/**
	 * 获取认证表中找不到的用户信息
	 * @return
	 */
	public List<AuthExcelModel> getImportNonList();
	
	/**
	 * 居住证认证
	 * @param mParam
	 */
	public void RcAuth(Map<String, Object> mParam);
	
}
