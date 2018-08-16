package com.eastcompeace.system.role;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.RoleModel;

/**
 * 角色DAO接口
 * 
 * @author caobo
 */

@Transactional
public interface RoleDao extends BaseDao{
	/**
	 * 查询分页
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public List<RoleModel> queryForList(RoleModel role) throws Exception;
	
	
	/**
	 * 根据条件查询
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public List<RoleModel> query(RoleModel role) throws Exception;
	
	
	/**
	 * 根据id
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public List<RoleModel> queryById(List listRoleId) throws Exception;

	/**
	 * 根据条件更新
	 * 
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public int update(RoleModel role) throws Exception;
	
	/**
	 * 插入
	 * 
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public int insert(RoleModel role) throws Exception;
	
	/**
	 * 删除角色
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int delete(List list) throws Exception;
	
	/**
	 * 删除角色菜单绑定关系
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int deleteRoleMapping(List list) throws Exception;

	/**
	 * 删除角色按钮绑定关
	 * @param listid
	 */
	public void deleteRoleButton(List<String> listid);
	
	/**
	 * 删除用户角色绑定关系
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int deleteUserMapping(List list) throws Exception;

	/**
	 * 下拉列表查询
	 * @param roleModel
	 * @return
	 */
	public List<RoleModel> querySelectList();

}