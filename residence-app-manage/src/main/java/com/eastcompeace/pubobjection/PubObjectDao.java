package com.eastcompeace.pubobjection;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eastcompeace.model.PubObjectionModel;

/**
 * 回馈信息Dao
 * @author Administrator
 *
 */
@Repository
public interface PubObjectDao {

	/**
	 * 查询回馈信息列表
	 * @return
	 */
	public List<PubObjectionModel> selobject(PubObjectionModel model) throws Exception;
	/**
	 * 添加回馈信息
	 * @param model
	 * @throws Exception
	 */
	public void addobject(PubObjectionModel model) throws Exception;
	/**
	 * 删除回馈信息
	 * @param list
	 * @throws Exception
	 */
	public void delobject(List list) throws Exception;
	
	/**
	 * 修改回馈信息
	 * @param model
	 * @throws Exception
	 */
	public void updateobject(PubObjectionModel model) throws Exception;
	/**
	 * 通过反馈意见Id集合获取意见信息集合
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<PubObjectionModel> queryPubObjByList(List list) throws Exception;
}
