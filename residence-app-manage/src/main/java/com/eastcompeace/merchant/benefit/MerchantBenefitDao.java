package com.eastcompeace.merchant.benefit;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.MerchantBenefitModel;

/**
 * @author ecp-zeng
 * @date 2017年2月10日
 */
@Repository
public interface MerchantBenefitDao extends BaseDao{
	/**
	 * 设置推荐商家
	 * @param model
	 * @throws Exception
	 */
	public void setRecommendMerchant(List list)throws Exception;
	
	/**
	 * 取消推荐商家
	 * @param model
	 * @throws Exception
	 */
	public void cancelRecommendMerchant(List list)throws Exception;
	
	/**
	 * 删除优惠信息
	 * @param benefitIdList
	 */
	public void deleteBenefitBy(List<String> benefitIdList)throws Exception;
}
