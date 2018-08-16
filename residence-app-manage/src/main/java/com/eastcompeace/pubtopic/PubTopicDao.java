package com.eastcompeace.pubtopic;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.eastcompeace.model.AreaModel;
import com.eastcompeace.model.PubGuideModel;
import com.eastcompeace.model.PubTopicModel;
import com.eastcompeace.model.TopicMappingModel;
/**
 * 热点资讯Dao
 * 
 * @author Administrator
 */
@Repository
public interface PubTopicDao {
    /**
     * 查询热点资讯
     * 
     * @return
     * @throws Exception
     */
    public List<PubTopicModel> seltopic(PubTopicModel model) throws Exception;

    /**
     * 添加资讯信息
     * 
     * @param model
     * @throws Exception
     */
    public Integer addtopic(PubTopicModel model) throws Exception;

    /**
     * 删除指南信息
     * 
     * @param pm
     * @throws Exception
     */
    public void deltopic(List list) throws Exception;

    /**
     * 修改资讯表信息
     * 
     * @param model
     * @throws Exception
     */
    public void edittopic(PubTopicModel model) throws Exception;

    /**
     * 根据ID查询内容
     * 
     * @return
     * @throws Exception
     */
    public PubTopicModel querycontent(String id) throws Exception;

    /**
     * 根据传入的资讯Id获取对应的资讯标题图片Id ，用于删除服务器上的图片
     * 
     * @param list
     * @return
     * @throws Exception
     */
    public List<PubTopicModel> queryTopicPic(List<String> list) throws Exception;

    /**
     * 保存资讯区域关联表
     * 
     * @param tempList
     * @throws Exception
     */
    public void saveTopicMapping(List<TopicMappingModel> tempList) throws Exception;

    /**
     * 查询区域名称
     * 
     * @param topicId
     * @return
     * @throws Exception
     */
    public List<AreaModel> listAreaName(Integer topicId) throws Exception;

    /**
     * 删除所属区域
     * 
     * @param parseInt
     * @throws Exception
     */
    public void delTopicAreaMapping(Integer parseInt) throws Exception;
    /**
     * 修改置顶
     * 
     * @param list
     * @throws Exception
     */
    public void updateBar(List list) throws Exception;  
    /**
     * 取消置顶
     * 
     * @param list
     * @throws Exception
     */
    public void removeBar(List list) throws Exception;       
    
}
