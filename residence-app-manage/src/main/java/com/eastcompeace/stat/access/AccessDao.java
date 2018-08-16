package com.eastcompeace.stat.access;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eastcompeace.model.AccessLogModel;
import com.eastcompeace.model.LogModel;

@Repository
public interface AccessDao {
	/**
	 * 获取用户轨迹记录表
	 * @param alm
	 * @return
	 * @throws Exception
	 */
	List<AccessLogModel> queryForList(AccessLogModel alm) throws Exception;

}
