package com.eastcompeace.pubmerchantIndex;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.eastcompeace.model.AreaModel;
import com.eastcompeace.model.PubGuideModel;
import com.eastcompeace.model.PubMerchantIndexModel;
import com.eastcompeace.model.MerchantIndexMappingModel;
/**
 * 热点资讯Dao
 * 
 * @author Administrator
 */
@Repository
public interface PubMerchantIndexDao {
    /**
     * 查询热点资讯
     * 
     * @return
     * @throws Exception
     */
    public List<PubMerchantIndexModel> selmerchantIndex(PubMerchantIndexModel model) throws Exception;

    /**
     * 添加资讯信息
     * 
     * @param model
     * @throws Exception
     */
    public Integer addmerchantIndex(PubMerchantIndexModel model) throws Exception;

    /**
     * 删除指南信息
     * 
     * @param pm
     * @throws Exception
     */
    public void delmerchantIndex(List list) throws Exception;

    /**
     * 修改资讯表信息
     * 
     * @param model
     * @throws Exception
     */
    public void editmerchantIndex(PubMerchantIndexModel model) throws Exception;

    /**
     * 根据ID查询内容
     * 
     * @return
     * @throws Exception
     */
    public PubMerchantIndexModel querycontent(String id) throws Exception;

    /**
     * 根据传入的资讯Id获取对应的资讯标题图片Id ，用于删除服务器上的图片
     * 
     * @param list
     * @return
     * @throws Exception
     */
    public List<PubMerchantIndexModel> queryMerchantIndexPic(List<String> list) throws Exception;

    /**
     * 保存资讯区域关联表
     * 
     * @param tempList
     * @throws Exception
     */
    public void saveMerchantIndexMapping(List<MerchantIndexMappingModel> tempList) throws Exception;

    /**
     * 查询区域名称
     * 
     * @param merchantIndexId
     * @return
     * @throws Exception
     */
    public List<AreaModel> listAreaName(Integer merchantIndexId) throws Exception;

    /**
     * 删除所属区域
     * 
     * @param parseInt
     * @throws Exception
     */
    public void delMerchantIndexAreaMapping(Integer parseInt) throws Exception;
     
    
}
