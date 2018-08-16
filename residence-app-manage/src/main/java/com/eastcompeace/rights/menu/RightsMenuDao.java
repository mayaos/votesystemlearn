package com.eastcompeace.rights.menu;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eastcompeace.model.RightsMenuModel;

/**
 * 权益菜单Dao
 * @author cui
 *
 */
@Repository
public interface RightsMenuDao {

	/**
	 * 查询权益菜单列表
	 * @return
	 * @throws Exception
	 */
	public List<RightsMenuModel> queryMenu(RightsMenuModel model)throws Exception;
	
	/**
	 * 添加权益信息
	 * @param model
	 * @throws Exception
	 */
	public void addMenu(RightsMenuModel model) throws Exception;
	
	/**
	 * 修改权益信息
	 * @param model
	 * @throws Exception
	 */
	public void updateMenu(RightsMenuModel model) throws Exception;
	/**
	 * 删除权益信息菜单
	 * @param list
	 * @throws Exception
	 */
	public void delMenu(List list) throws Exception;

}
