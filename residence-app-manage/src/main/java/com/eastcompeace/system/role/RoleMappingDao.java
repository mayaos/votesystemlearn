package com.eastcompeace.system.role;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.RoleButtonModel;
import com.eastcompeace.model.RoleMappingModel;
import com.eastcompeace.model.RoleModel;

/**
 * 角色DAO接口
 * 
 * @author xp
 */

@Transactional
public interface RoleMappingDao extends BaseDao{
	/**
	 * 查询分页
	 * @param rolemap
	 * @return
	 * @throws Exception
	 */
	public List<RoleMappingModel> queryForList(RoleMappingModel rolemap) throws Exception;
	
	/**
	 * 根据角色ID和菜单ID查询按钮
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<RoleButtonModel> getButtonList(String roleid ,String menuid) throws Exception;

	/**
	 * 插入
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int insert(List<RoleMappingModel> list) throws Exception;
	
	/**
	 * 插入
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int insertbutton(List<RoleButtonModel> list) throws Exception;

	/**
	 * 删除
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public int delete(int roleId) throws Exception;
	
	/**
	 * 删除
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public int deletebutton(int roleId) throws Exception;

}