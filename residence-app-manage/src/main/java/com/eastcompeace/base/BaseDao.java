package com.eastcompeace.base;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * 公共Dao
 * @author ecp-zhaoxi.zeng
 * @date 2015年11月26日
 */
@Transactional
public interface BaseDao {
	
	/**
	 * 查询
	 * @param Id Integer
	 * @return Object
	 * @throws Exception
	 */
	public Object query(@Param("id") Integer id) throws Exception;
	
	/**
	 * 查询
	 * @param obj Object
	 * @return Object
	 * @throws Exception
	 */
	public Object query(@Param("uuid") String uuid) throws Exception;

	/**
	 * 查询
	 * @param Id Integer
	 * @return Object
	 * @throws Exception
	 */
	public Object queryByBigId(@Param("id") BigInteger id) throws Exception;
	
	/**
	 * 查询——建议使用本函数代替上面的
	 * @param Id Integer
	 * @param uuid String
	 * @return Object
	 * @throws Exception
	 */
	public Object queryIU(@Param("id") Integer id, @Param("uuid") String uuid) throws Exception;
	
	/**
	 * 查询
	 * @param obj Object
	 * @return Object
	 * @throws Exception
	 */
	public Object queryBy(Object obj) throws Exception;
	
	/**
	 * 查询
	 * @param obj Object
	 * @return Object
	 * @throws Exception
	 */
	public Object queryBy(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询所有数量列表
	 * @param Id Integer
	 * @return List
	 * @throws Exception
	 */
	public <T> List<T> queryList() throws Exception;
	
	/**
	 * 按条件查询数量列表
	 * @param <T>
	 * @param obj Object
	 * @return List
	 * @throws Exception
	 */
	public <T> List<T> queryListBy(Object obj) throws Exception;
	
	/**
	 * 按条件查询数量列表
	 * @param <T>
	 * @param obj Object
	 * @return List
	 * @throws Exception
	 */
	public <T> List<T> queryListBy(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询下拉选择列表 <select></select>
	 * @param obj
	 * @return List [id, name]
	 * @throws Exception
	 */
	public <T> List<T> querySelectList(Object obj) throws Exception;
	
	
	/**
	 * 插入
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	 public void insert(Object obj) throws Exception;
	 
	 /**
	  *  批量插入
	  * @param list
	  * @throws Exception
	  */
	 public <T> void insertMore(List<T> list) throws Exception;
	 
	/**
	 * 更新
	 * @param obj
	 * @throws Exception
	 */
	public void update(Object obj) throws Exception;
	
	/**
	 * 更新批量数据
	 * @param obj  [Object]更新内容
	 * @param list [List] 更新条件
	 * @throws Exception
	 */
	 public <T> void updateMore(List<T> list) throws Exception;
	 
	 /**
	  * 更新批量数据
	  * @param map
	  * @throws Exception
	  */
	 public void updateMore(Map<String, Object> map) throws Exception;

	 /**
	 * 更新状态
	 * @param obj
	 * @throws Exception
	 */
	public void updateStatus(@Param("id") Integer id, @Param("uuid")String uuid, @Param("status")int status) throws Exception;
		
	/**
	 * 删除单条
	 * @param obj
	 * @return int
	 * @throws Exception
	 */
	public int deleteByID(@Param("id")String id) throws Exception;
	
	/**
	 * 删除单条
	 * @param obj
	 * @return int
	 * @throws Exception
	 */
	public int deleteOneBy(Object obj) throws Exception;
	
	/**
	 * 删除批量
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int deleteMoreBy(List<?> list) throws Exception;
	
	/**
	 * 删除批量
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int deleteMoreBy(Map<String, Object> map) throws Exception;
	
	/**
	 * 清除所有
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int delete() throws Exception;
	
	/**
	 *  统计数据量
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public int getCount(Object obj) throws Exception;
	

	/**
	 *  统计数据量
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getSum(Object obj) throws Exception;
	
	
}