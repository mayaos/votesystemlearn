package com.eastcompeace.usermng.info;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.CitizenCouponModel;
import com.eastcompeace.model.CitizenMessageModel;
import com.eastcompeace.model.CitizenModel;
import com.eastcompeace.model.CitizenVIPCardModel;

/**
 * 居民Dao
 * @author ecp-zeng
 * @date 2017年1月4日
 */
@Transactional
public interface CitizenDao extends BaseDao{
	
	CitizenModel queryIdentity(@Param("citizenID")String citizenID);

	List<CitizenVIPCardModel> queryVipCardListBy(@Param("citizenID")String citizenID);

	List<CitizenCouponModel> queryCouponListBy(@Param("citizenID")String citizenID);

	List<CitizenMessageModel> queryMessageListBy(@Param("citizenID")String citizenID);

}
