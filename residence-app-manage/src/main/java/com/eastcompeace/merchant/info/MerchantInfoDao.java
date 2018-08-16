package com.eastcompeace.merchant.info;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.CitizenVIPCardModel;
import com.eastcompeace.model.MerchantBenefitModel;
import com.eastcompeace.model.MerchantCouponModel;
import com.eastcompeace.model.MerchantInfoModel;
import com.eastcompeace.model.OrgMenuModel;
/**
 * 
 * 商家信息Dao
 *
 */
@Repository
public interface MerchantInfoDao extends BaseDao{
	    /**
	     * 添加商家
	     * @param record
	     * @return
	     */
	    public void insertinfo(MerchantInfoModel merchantinfo) throws Exception;
	    /**
	     * 查询商户信息
	     * @param merchantId
	     * @return
	     */
	    public List<MerchantInfoModel> selectinfo(MerchantInfoModel model) throws Exception;
       /**
        * 删除商家信息
        * @param list
        * @throws Exception
        */
	    public void delinfo(List list) throws Exception;
	    
	    /**
	     * 删除merchant_vipcard表信息
	     * @param list
	     * @throws Exception
	     */
	    public void delMerchantVipcardList(List list) throws Exception;
	    
	    /**
	     * 删除citizen_vipcard表信息
	     * @param list
	     * @throws Exception
	     */
	    public void delCitizenVipcardList(List list) throws Exception;
	    /**
	     * 修改商家信息
	     * @param model
	     * @throws Exception
	     */
	    public void updateMerchantInfo(MerchantInfoModel model)throws Exception;
	    
	    
	    
//		public List<MerchantInfoModel> selectMerchantList(@Param("areaID")String areaID);
		public List<MerchantInfoModel> selectMerchantList(MerchantInfoModel model);
		
		/**
		 * 商家会员卡用户
		 * @param model
		 * @return
		 */
		public List<CitizenVIPCardModel> queryVipcardList( CitizenVIPCardModel model);
		
		/**
		 * 根据传入的商家Id List返回商家photo1/2/3信息结合
		 * @param list
		 * @return
		 * @throws Exception
		 */
		public List<MerchantInfoModel> queryMerchantByList(List<String> list) throws Exception;
		
		
//	    /**
//	     * 根据行业类型 查询表中最大的 自营店 商家ID
//	     * SUBSTRING(m.merchant_new_id, 9, 2) = '00'  直营店
//	     * @return
//	     */
//	    public MerchantInfoModel selectMaxMerchantDirectId(@Param("industryType")String industryType) throws Exception;
//	    
//		 /**
//	     * 根据行业类型 查询表中最大的 连锁店 商家ID
//	     * SUBSTRING(merchantId, 7, 2)  行业类型
//	     * MAX(SUBSTRING(merchantId, 1, 10))  当前最大的连锁店ID
//	     * @param industryType
//	     * @return
//	     */
//	    public MerchantInfoModel selectMaxMerchantChainId(@Param("industryType")String industryType) throws Exception;
		
		/**
	     * 根据行业类型 查询表中最大的 自营店 商家ID
	     * @param areaIndustryChain 包含 地区、行业类型、直营店
	     * @return
	     */
	    public MerchantInfoModel selectMaxMerchantDirectId(@Param("areaIndustryDirect")String areaIndustryDirect) throws Exception;
	    
		 /**
	     * 根据行业类型 查询表中最大的 连锁店 商家ID
	     * SUBSTRING(m.merchant_new_id, 1, 8)  地区+行业类型
	     * MAX(SUBSTRING(merchantId, 1, 10))  当前最大的连锁店ID
	     * @param areaIndustry
	     * @return
	     */
	    public MerchantInfoModel selectMaxMerchantChainId(@Param("areaIndustry")String areaIndustry) throws Exception;
	    
	    /**
	     * 根据行业类型 查询表中 指定连锁店 最大分店的商家ID
	     * SUBSTRING(m.merchant_new_id, 1, 10) = '3306040102'
	     * @param areaIndustryChain 包含 地区、行业类型、哪家连锁店
	     * @return
	     */
	    public MerchantInfoModel selectMaxMerchantChainBranchId(@Param("areaIndustryChain")String areaIndustryChain) throws Exception;
	    
	    /**
	     * 根据行业类型 查询表中 指定连锁店 最大分店的商家ID
	     * SUBSTRING(m.merchant_new_id, 1, 8)  地区+行业类型
	     * SUBSTRING(m.merchant_new_id, 9, 2) <> '00' 不是直营店，即连锁店
	     * SUBSTRING(m.merchant_new_id, 11, 4) = '0000'  连锁店总店
	     * @param areaIndustry
	     * @return
	     */
	    public List<MerchantInfoModel> selectMerchantHeadList(@Param("areaIndustry")String areaIndustry) throws Exception;
	    
	    /**
	     * 修改DAS同步商家编码返回 信息
	     * @param model
	     * @throws Exception
	     */
	    public void updateDASReturnInfo(@Param("sendDasFlag")String sendDasFlag, @Param("merchantId")String merchantId)throws Exception;
		
	    /**
		 * 根据商家ID集获取优惠信息列表
		 * @param list
		 * @return
		 */
	    public List<MerchantBenefitModel> queryBenefitByList(List<String> list) throws Exception;
	    
	    /**
	     * 根据优惠信息ID查询优惠券ID信息集合
	     * @param benefitIdList
	     * @return
	     * @throws Exception
	     */
		public List<String> queryCouponIdByList(List<String> benefitIdList) throws Exception;
}
