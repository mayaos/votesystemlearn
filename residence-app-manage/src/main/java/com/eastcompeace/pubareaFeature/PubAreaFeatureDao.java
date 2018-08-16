package com.eastcompeace.pubareaFeature;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.eastcompeace.model.AreaModel;
import com.eastcompeace.model.PubAreaFeatureModel;
import com.eastcompeace.model.AreaFeatureMappingModel;
/**
 * 热点地方特色Dao
 * 
 * @author Administrator
 */
@Repository
public interface PubAreaFeatureDao {
    /**
     * 查询地方特色
     * 
     * @return
     * @throws Exception
     */
    public List<PubAreaFeatureModel> selAreaFeature(PubAreaFeatureModel model) throws Exception;

    /**
     * 添加地方特色信息
     * 
     * @param model
     * @throws Exception
     */
    public Integer addAreaFeature(PubAreaFeatureModel model) throws Exception;

    /**
     * 删除指南信息
     * 
     * @param pm
     * @throws Exception
     */
    public void delAreaFeature(List list) throws Exception;

    /**
     * 修改地方特色表信息
     * 
     * @param model
     * @throws Exception
     */
    public void editAreaFeature(PubAreaFeatureModel model) throws Exception;

    /**
     * 根据ID查询内容
     * 
     * @return
     * @throws Exception
     */
    public PubAreaFeatureModel querycontent(String id) throws Exception;

    /**
     * 根据传入的地方特色Id获取对应的地方特色标题图片Id ，用于删除服务器上的图片
     * 
     * @param list
     * @return
     * @throws Exception
     */
    public List<PubAreaFeatureModel> queryAreaFeaturePic(List<String> list) throws Exception;

    /**
     * 保存地方特色区域关联表
     * 
     * @param tempList
     * @throws Exception
     */
    public void saveAreaFeatureMapping(List<AreaFeatureMappingModel> tempList) throws Exception;

    /**
     * 查询区域名称
     * 
     * @param AreaFeatureId
     * @return
     * @throws Exception
     */
    public List<AreaModel> listAreaName(Integer AreaFeatureId) throws Exception;

    /**
     * 删除所属区域
     * 
     * @param parseInt
     * @throws Exception
     */
    public void delAreaFeatureAreaMapping(Integer parseInt) throws Exception;
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
