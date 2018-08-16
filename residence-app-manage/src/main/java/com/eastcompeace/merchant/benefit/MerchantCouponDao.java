package com.eastcompeace.merchant.benefit;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.MerchantCouponModel;
/**
 * 商家优惠券Dao
 * @author Administrator
 *
 */
@Repository
public interface MerchantCouponDao extends BaseDao{

	/**
	 * 查询优惠券信息
	 * @return
	 * @throws Exception
	 */
	public List<MerchantCouponModel> queryCoupon(MerchantCouponModel model)throws Exception;
	/**
	 * 添加优惠券信息
	 * @param model
	 * @throws Exception
	 */
	public void addcoupon(MerchantCouponModel model)throws Exception;
	/**
	 * 根据优惠券ID集删除优惠券信息
	 * @param list
	 * @throws Exception
	 */
	public void delcoupon(List<String> list) throws Exception;
	
	/**
	 * 修改优惠券信息
	 * @param model
	 * @throws Exception
	 */
	public void updateCopon(MerchantCouponModel model)throws Exception;
	
	/**
	 * 根据优惠券ID集合删除居民优惠券关系表里的信息
	 * @param list
	 * @throws Exception
	 */
	public void delCitizenCoupon(List<String> list) throws Exception;
}
