package com.eastcompeace.system.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.UserCommModel;

/**
 * 用户小区对应关系 dao接口
 * 
 * @author xiangpei
 * 
 */
@Transactional
public interface UserCommDao extends BaseDao{
	/**
	 * 添加信息
	 * @param ucm
	 * @return
	 */
	public int add(UserCommModel ucm);

	/**
	 * 更新信息
	 * @param ucm
	 */
	public void update(UserCommModel ucm);
	
	/**
	 * 查询信息
	 * 
	 * @param userid
	 * @return
	 */
	public List<UserCommModel> query(String userid);

	/**
	 * 查询指定信息 Add by zhaoxi.zeng
	 * 
	 * @param userid
	 * @return
	 */
	public UserCommModel queryByUser(String userId);

	/**
	 * 根据用户ID删除用户对应区域信息
	 * 
	 * @param list
	 */
	public void delete(@Param("userId")String userId);

}
