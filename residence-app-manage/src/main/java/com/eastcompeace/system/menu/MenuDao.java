package com.eastcompeace.system.menu;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.ButtonModel;
import com.eastcompeace.model.MenuButtonModel;
import com.eastcompeace.model.MenuModel;

/**
 * 菜单DAO接口
 * 
 * @author caobo
 */

@Transactional
public interface MenuDao extends BaseDao{
	
	/**
	 * 查询按钮列表
	 * @return
	 * @throws Exception
	 */
	public List<ButtonModel> queryButtonList() throws Exception;
	
	/**
	 * 根据条件查询按钮
	 * @return
	 * @throws Exception
	 */
	public List<ButtonModel> queryButtonListby(String roleid , String meunid) throws Exception;
	
	/**
	 * 查询菜单列表
	 * @return
	 * @throws Exception
	 */
	public List<MenuModel> queryMenuInfoList() throws Exception;
	
	/**
	 * 根据条件查询菜单列表
	 * @return
	 * @throws Exception
	 */
	public List<MenuModel> queryMenuby(MenuModel mm) throws Exception;

	/**
	 * 根据菜单的父ID,查询菜单列表
	 * @param menuFatherId
	 * @return
	 */
	public List<MenuModel> queryMenuListByFid(int menuFatherId) throws Exception;
	
	/**
	 * 根据菜单的父ID,查询菜单列表
	 * @param menuFatherId
	 * @return
	 */
	public List<MenuModel> queryMenuListByFidroleid(int menuFatherId,String roleid) throws Exception;
	
	
	/**
	 * 根据菜单子ID 查询对应的按钮
	 * @param menuFatherId
	 * @return
	 */
	public List<MenuButtonModel> queryButtonListByFid(int menuId) throws Exception;

	/**
	 * 根据菜单ID查询菜单对象信息
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	public MenuModel findMenuById(String menuId) throws Exception;

	/**
	 * 根据菜单ID删除菜单
	 * @param menuId
	 * @throws Exception
	 */
	public void delMenuById(String menuId) throws Exception;

	/**
	 * 更新menu信息
	 * @param menu
	 * @throws Exception
	 */
	public void updateMenuInfo(MenuModel menu) throws Exception;

	/**
	 * 向menu表中插入数据
	 * @param meName
	 * @param meFatherId
	 * @throws Exception
	 */
	public void addMenuInfo(String meName, String meFatherId)  throws Exception;

	/**
	 * 根据menuids 获取用户menulist
	 * @param menuidlist
	 * @return
	 */
	public List<MenuModel> queryMenuList(List<MenuModel> menuidlist);

	/**
	 * 根据菜单ID，删除角色菜单映射表中相关数据
	 * @param menuId
	 */
	public void delRoleMappingByMid(@Param("menuId")String menuId);
	
	
	/**
	 * 根据menuid删除菜单按钮对应关系
	 * @param menuidlist
	 * @return
	 */
	public int deleteMenuButton(Integer menuid);
	
	/**
	 * 根据菜单Id删除角色-按钮关系表中的数据
	 * @param menueid
	 */
	public void delRoleButtonMapping(Integer menueId) throws Exception;
	
	/**
	 * 添加按钮菜单对应关系
	 * @param menuidlist
	 * @return
	 */
	public int addMenuButton(MenuButtonModel mbm);

}