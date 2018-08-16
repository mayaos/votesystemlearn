package com.eastcompeace.merchant.vip;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eastcompeace.model.MerchantVipModel;

@Repository
public interface MerchantVipDao {
	
	/**
	 * 查询会员卡信息列表
	 * @param model
	 * @return
	 * @throws Exception
	 */
	List<MerchantVipModel> queryVipInfoList(MerchantVipModel model) throws Exception;
	/**
	 * 插入新的vip信息
	 * @param model
	 * @throws Exception
	 */
	void insertVipInfo(MerchantVipModel model) throws Exception;
	/**
	 * 更新会员卡信息
	 * @param model
	 * @throws Exception
	 */
	void updateVipInfo(MerchantVipModel model) throws Exception;
	/**
	 * 删除会员卡信息
	 * @param list
	 * @throws Exception
	 */
	void deleteVipInfoList(List<String> list) throws Exception;
	/**
	 * 删除居民会员卡信息
	 * @param list
	 * @throws Exception
	 */
	void deleteVipMappingList(List<String> list)  throws Exception;

}
