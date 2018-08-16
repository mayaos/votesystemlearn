package com.eastcompeace.org.orginfo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eastcompeace.model.OrgInfoModel;

@Repository
public interface OrgInfoDao {
	/**
	 * 添加机构信息
	 * @param model
	 * @throws Exception
	 */
	public void addOrgInfo(OrgInfoModel model) throws Exception;

	/**
	 * 查询机构信息列表
	 * @param orgInfoModel
	 * @return
	 * @throws Exception
	 */
	public List<OrgInfoModel> queryOrgInfoList(OrgInfoModel orgInfoModel) throws Exception;
	
	/**
	 * 更新机构信息
	 * @param model
	 * @throws Exception
	 */
	public void updateOrgInfo(OrgInfoModel model) throws Exception;
	
	/**
	 * 删除机构信息
	 * @param list
	 * @throws Exception
	 */
	public void delOrgInfo(List<String> list) throws Exception;
	
}
