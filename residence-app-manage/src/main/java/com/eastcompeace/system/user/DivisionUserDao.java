package com.eastcompeace.system.user;

import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.DivisionsUserModel;

/**
 * 用户小区对应关系 dao接口
 * 
 * @author xiangpei
 * 
 */
@Transactional
public interface DivisionUserDao extends BaseDao{
	/**
	 * 添加信息
	 * @param ucm
	 * @return
	 */
	public int add(DivisionsUserModel ucm);

	/**
	 * 更新信息
	 * @param ucm
	 */
	public void update(DivisionsUserModel ucm);
	

}
