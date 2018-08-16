package com.eastcompeace.rights.info;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eastcompeace.model.RightsInfoModel;
/**
 * 权益信息Dao
 * @author cui
 *
 */
@Repository
public interface RightsInfoDao {

	/**
	 * 查询权益信息
	 * @return
	 * @throws Exception
	 */
	public List<RightsInfoModel> queryInfo(RightsInfoModel model) throws Exception;
	
	/**
	 * 添加权益详情
	 * @param model
	 * @throws Exception
	 */
	public void addRightsInfo(RightsInfoModel model)throws Exception;
	
	/**
	 * 修改权益详情
	 * @param model
	 * @throws Exception
	 */
	public void updateRightsInfo(RightsInfoModel model)throws Exception;
	/**
	 * 删除权益详情
	 * @param list
	 * @throws Exception
	 */
	public void delRightsInfo(List list) throws Exception;
	/**
	 * 根据权益文章ID获取文章内容
	 * @param articleId
	 * @return
	 */
	public RightsInfoModel querycontent(String articleId);
	/**
	 * 根据传入的权益文章Id列表批量获取权益信息
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<RightsInfoModel> queryInfoByList(List articleIdList) throws Exception;
}
