package com.eastcompeace.advertisement;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.eastcompeace.model.AdvInfoModel;
import com.eastcompeace.model.AdvMappingModel;
import com.eastcompeace.model.AreaModel;
@Repository
public interface AdvertisementDao {
    /**
     * 保存图片信息
     * 
     * @param advInfoModel
     * @throws Exception
     */
    void saveAdvImgInfo(AdvInfoModel advInfoModel) throws Exception;

    /**
     * 获取图片信息
     * 
     * @param advInfoModel
     * @return
     */
    AdvInfoModel getAdvImgInfo(AdvInfoModel advInfoModel) throws Exception;

    /**
     * 保存图片-发布区域关联信息
     * 
     * @param tempList
     * @throws Exception
     */
    void saveAdvMapping(List<AdvMappingModel> tempList) throws Exception;

    /**
     * 根据发布区域查询广告信息
     * 
     * @param areaNameSearch
     * @return
     * @throws Exception
     */
    List<AdvInfoModel> queryAdvInfoList(@Param("areaNameSearch") String areaNameSearch) throws Exception;

    /**
     * 查询广告发布区域
     * 
     * @param imgId
     * @return
     * @throws Exception
     */
    List<AreaModel> listAreaName(@Param("imgId") String imgId) throws Exception;

    /**
     * 更新广告图片信息
     * 
     * @param advInfoModel
     * @throws Exception
     */
    void updateAdvImgInfo(AdvInfoModel advInfoModel) throws Exception;

    /**
     * 删除图片-发布区域关联信息
     * 
     * @param advInfoModel
     */
    void delAdvMapping(AdvInfoModel advInfoModel) throws Exception;

    /**
     * 删除广告信息
     * 
     * @param list
     * @throws Exception
     */
    void delAdvInfo(List<String> list) throws Exception;
}
