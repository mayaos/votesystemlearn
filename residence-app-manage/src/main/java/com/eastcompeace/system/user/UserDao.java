package com.eastcompeace.system.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.AccountSmsCodeModel;
import com.eastcompeace.model.MenuModel;
import com.eastcompeace.model.UserModel;
import com.eastcompeace.model.UserRolesModel;

/**
 * 用户DAO接口
 * 
 * @author caobo
 */

@Transactional
public interface UserDao extends BaseDao{
	/**
	 * 根据用户名和密码查询用户对象
	 * 
	 * @param userCode
	 * @param passWord
	 * @return
	 * @throws Exception
	 */
	public UserModel queryUserInfo(@Param("uCode") String userCode, @Param("uPwd") String passWord) throws Exception;
	
	/**
	 * 根据用户查询用户对象
	 * 
	 * @param userCode
	 * @param passWord
	 * @return
	 * @throws Exception
	 */
	public UserModel queryUser(UserModel user) throws Exception;

	/**
	 * 更新用户密码
	 * 
	 * @param userId
	 * @param newPwd
	 */
	public void updateUserPassword(int userId, String newPwd ,String pwdvaildTime) throws Exception;
	
	/**
	 * 更新用户密码为初始密码（该函数在找回密码中用到）
	 * 
	 * @param username
	 * @param newPwd
	 * @param pwdinitial
	 */
	public void updateUserpwd(String userName, String newPwd ,String pwdinitial,String modifydate) throws Exception;

	/**
	 * 查询User分页列表
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<UserModel> queryUserList(@Param("user") UserModel user, @Param("list")List list) throws Exception;

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @throws Exception
	 */

	public void insertUser(UserModel user) throws Exception;

	/**
	 * 修改用户
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void updateUser(UserModel user) throws Exception;
	
	/**
	 * 注销用户
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void updateUserby(@Param("userId")String userId) throws Exception;

	/**
	 * 根据用户ID，删除用户
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public void deleteUser(String userId) throws Exception;
	

	/**
	 * 根据用户ID查询用户角色信息
	 * @param userId
	 * @return
	 */
	public List<UserRolesModel> queryUserroleslistById(String userId);
	
	
	/**
	 * 根据用户ID查询用户角色信息
	 * @param roleidList
	 * @return
	 */
	public List<UserRolesModel> queryUserroleslistByroleId(List roleidList);

	/**
	 * 根据用户ID删除用户的角色配置信息，即删除用户和角色的映射数据
	 * @param userId
	 */
	public void delUserRolesInfo(@Param("userId")String userId);

	/**
	 * 添加用户和角色的配置信息
	 * @param userId
	 * @param string
	 */
	public void addUserRoleInfo(String userId, String string);

	/**
	 * 根据userid ,查询用户拥有的菜单权限
	 * @param userId
	 * @return
	 */
	public List<MenuModel> queryUserMenuids(int userId);

	/**
	 * 清除超时短信验证码
	 * @param userMobile
	 */
	public void deleteSmsCode();

	/**
	 * 根据手机号查询验证码
	 * @param userMobile
	 * @return
	 */
	public String querySmsCode(String userMobile);
	
	/**
	 * 保存手机验证码
	 * @param user
	 * @return
	 */
	public void saveSmsCode(AccountSmsCodeModel smsCode) throws Exception;

	public List<UserModel> queryUserList2(@Param("user")UserModel user);

	/**
	 * 更新角色
	 * @param uId
	 * @param roleUuid
	 */
	public void updateRole(String uId, String roleUuid);

	/**
	 * 更新角色为“54|决策权限管理员”的用户状态为：YES|冻结
	 * @param commUuid
	 * @param frozen
	 */
	public void updateUserStatus(@Param("commUuid")String commUuid, @Param("frozen")String frozen);

}