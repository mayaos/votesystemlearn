package com.eastcompeace.pubguide;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eastcompeace.model.PubGuideModel;

/**
 * 指南信息Dao接口
 * @author Administrator
 *
 */
@Repository
public interface PubGuideDao {
 
	/**
	 * 查询指南信息
	 * @return
	 * @throws Exception
	 */
	public List<PubGuideModel> selectguide(PubGuideModel model) throws Exception;
	/**
	 * 添加指南信息
	 * @param pm
	 * @throws Exception
	 */
	public void addguide(PubGuideModel pm) throws Exception;
	/**
	 * 删除指南信息
	 * @param pm
	 * @throws Exception
	 */
	public void delguide(List list) throws Exception;
	
	/**
	 * 修改指南信息
	 * @param pm
	 * @throws Exception
	 */
	public void updateguide(PubGuideModel pm) throws Exception;
	
	/**
	 * 根据ID查询内容
	 * @return
	 * @throws Exception
	 */
	public PubGuideModel querycontent(String  id) throws Exception;
}
