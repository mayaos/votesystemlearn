package com.eastcompeace.config.area;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.eastcompeace.model.AreaModel;

@Repository
public interface AreaDao {
	/**
	 * 获取所有区域信息列表
	 * @param areaNameSearch 条件查询参数，若为空则返回所有信息列表
	 * @return
	 * @throws Exception
	 */
	public List<AreaModel> queryAreaList(@Param("areaNameSearch")String areaNameSearch) throws Exception;
	/**
	 * 插入一条区域信息
	 * @throws Exception
	 */
	public void addAreaInfo(AreaModel area) throws Exception;
	/**
	 * 更新区域信息中的区域名和全名两个字段
	 * @param area
	 * @throws Exception
	 */
	public void updateAreaInfo(AreaModel area) throws Exception;
	/**
	 * 删除区域信息
	 * @param areaId 区域代码
	 * @throws Exception
	 */
	public void delAreaInfo(String areaId) throws Exception;
	/**
	 * 查询区域Id和区域全称列表
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryAreaIdAndFullNameList() throws Exception;
	
	/**
	 * 查询省份列表——下拉选择用
	 * @return
	 * @throws Exception
	 */
	public List<AreaModel> selectProvinceList() throws Exception;
	
	/**
	 * 查询地市列表——下拉选择用
	 * @param province
	 * @return
	 * @throws Exception
	 */
	public List<AreaModel> selectCityList(@Param("province")String province) throws Exception;
	
	/**
	 * 查询县/区列表——下拉选择用
	 * @param province
	 * @param city :  by city
	 * @return
	 * @throws Exception
	 */
	public List<AreaModel> selectTownList(@Param("province")String province, @Param("city")String city) throws Exception;
		
	/**
	 * 查询县/区列表——下拉选择用
	 * @param province
	 * @param city: by area_id
	 * @return
	 */
	public List<AreaModel> selectTownList2(@Param("province")String province, @Param("city")String city);
	
	/**
	 * 查询县/区列表——下拉选择用
	 * @param province
	 * @param city :  by city
	 * @return
	 * @throws Exception
	 */
	public List<AreaModel> selectTownList3(@Param("province")String province, @Param("city")String city) throws Exception;
	/**
	 * 根据区域id查询区域名
	 * @param areaid
	 * @return
	 */
	public String queryAreaName(String areaid) throws Exception;
}
