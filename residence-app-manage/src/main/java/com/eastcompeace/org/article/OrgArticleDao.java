package com.eastcompeace.org.article;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eastcompeace.model.OrgArticleModel;
/**
 * 轻应用文章信息Dao
 * @author xianzehua
 *
 */
@Repository
public interface OrgArticleDao {

	/**
	 * 查询文章信息
	 * @return
	 * @throws Exception
	 */
	public List<OrgArticleModel> queryArticleInfo(OrgArticleModel model) throws Exception;
	
	/**
	 * 添加文章详情
	 * @param model
	 * @throws Exception
	 */
	public void addArticleInfo(OrgArticleModel model)throws Exception;
	
	/**
	 * 修改文章详情
	 * @param model
	 * @throws Exception
	 */
	public void updateArticleInfo(OrgArticleModel model)throws Exception;
	/**
	 * 删除文章详情
	 * @param list
	 * @throws Exception
	 */
	public void delArticleInfo(List list) throws Exception;
	/**
	 * 根据文章ID获取文章内容
	 * @param articleId
	 * @return
	 */
	public OrgArticleModel queryArticleContent(String articleId);
	/**
	 * 根据文章ID获取文章详细内容
	 * @param articleId
	 * @return
	 */
	public OrgArticleModel queryArticleDetailInfo(String articleId);
	/**
	 * 根据传入的文章文章Id列表批量获取文章信息
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<OrgArticleModel> queryInfoByList(List articleIdList) throws Exception;
	/**
	 * 发布文章
	 * @param list
	 * @throws Exception
	 */
	public void releaseArticleInfo(List list) throws Exception;
	/**
	 * 取消发布文章
	 * @param list
	 * @throws Exception
	 */
	public void delreleaseArticleInfo(List list) throws Exception;	
}
