package com.eastcompeace.org.orgmenu;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eastcompeace.model.OrgMenuModel;

@Repository("orgMenuDao")
public interface OrgMenuDao {
	
	/**
	 * 获取轻应用菜单列表
	 * @param menuModel
	 * @return
	 * @throws Exception
	 */
	public List<OrgMenuModel> queryMenuList(OrgMenuModel menuModel) throws Exception;
	
	/**
	 * 增加菜单信息
	 * @param model
	 * @throws Exception
	 */
	public void addMenu(OrgMenuModel model) throws Exception;
	
	/**
	 * 修改菜单信息
	 * @param model
	 * @throws Exception
	 */
	public void updateMenu(OrgMenuModel model) throws Exception;
	
	/**
	 * 删除机构菜单信息
	 * @param list
	 */
	public void delMenu(List<String> list) throws Exception;

	/**
	 * 批量查询机构菜单信息
	 * @param list
	 * @throws Exception
	 */
	public List<OrgMenuModel> queryMenuByList(List<String> list) throws Exception;

	/**
	 * 生成菜单列表用于生成easyui下拉框，
	 * 只需要有menuId，menuName两个字段
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List<OrgMenuModel> queryMenuListForCombobox(OrgMenuModel model) throws Exception;
}
