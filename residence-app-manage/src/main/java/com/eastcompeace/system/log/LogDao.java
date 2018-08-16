package com.eastcompeace.system.log;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.LogModel;
import com.eastcompeace.model.excel.LogExceModel;

/**
 * 日志DAO接口
 * 
 * @author xp
 */

@Transactional
@Repository
public interface LogDao extends BaseDao{
	/**
	 * 查询分页
	 * 
	 * @param log
	 * @return Log
	 * @throws Exception
	 */
	public List<LogModel> queryForList(LogModel log) throws Exception;
	
	/**
	 * 添加日志
	 * 
	 * @param log
	 * @return Log
	 * @throws Exception
	 */
	public int add(LogModel log) throws Exception;

	/**
	 * 根据条件删除日志
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int del(List list) throws Exception;

	/**
	 * 删除所有日志
	 * 
	 * @return
	 * @throws Exception
	 */
	public int delall() throws Exception;

	/**
	 * 导出excle
	 * 
	 * @param log
	 * @return
	 * @throws Exception
	 */
	public List<LogExceModel> queryForDownload(LogModel log) throws Exception;

}