package com.eastcompeace.usermng.authcitizen;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.CitizenAuthModel;
import com.eastcompeace.model.CitizenModel;
import com.eastcompeace.model.CitizenUserModel;
import com.eastcompeace.model.RcCardInfoModel;
import com.eastcompeace.model.excel.AuthExcelModel;

/**
 * 居住证认证Dao
 * @author xianzehua
 * @date 2017年2月24日
 */
@Transactional
@Repository("authCitizenDao")
public interface AuthCitizenDao extends BaseDao{

	/**
	 * 居住证认证
	 * @param mParam
	 * @throws Exception
	 */
	public void RcAuth(Map<String, Object> mParam) throws Exception;
	
	/**
	 * 查询居住证认证信息
	 * @param mParam
	 * @return
	 * @throws Exception
	 */
	public  List<CitizenAuthModel> selectCitizenInfo(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询居住证认证信息
	 * @return
	 * @throws Exception
	 */
	public  List<RcCardInfoModel> selectRcCardInfo() throws Exception;
	
	/**
	 * 查询rc_card_info表
	 * @param mParam
	 * @return
	 * @throws Exception
	 */
	public  List<RcCardInfoModel> selectRcCardInfoForPerson(List<String> list) throws Exception;
	
	/**
	 * 更新citizen_auth表
	 * @param model
	 * @throws Exception
	 */
	public void updateCitizenAuth(CitizenAuthModel citizenAuthModel) throws Exception;
	
	/**
	 * 更新citizen_user表
	 * @param model
	 * @throws Exception
	 */
	public void updateCitizenUser(CitizenUserModel citizenUserModel) throws Exception;
	
	/**
	 * 更新citizen_identity表
	 * @param model
	 * @throws Exception
	 */
	public void updateCitizenIdentity(RcCardInfoModel rcCardInfoModel) throws Exception;
	
	/**
	 * 查询最大的导入日期
	 * @return
	 * @throws Exception
	 */
	public String selectMaxImportDate() throws Exception;
	
	/**
	 * 根据条件进行查询
	 * @return
	 * @throws Exception
	 */
	public List<CitizenAuthModel> selectForCondition(List list) throws Exception;
	
	/**
	 * 查询实名等级为身份证认证用户的用户信息集合
	 * @return
	 * @throws Exception
	 */
	public List<CitizenAuthModel> selectForIdAuthLevalCitizenList() throws Exception;
	
	/**
	 * 获取身份认证
	 * @param citizenID
	 * @param authType : 1-身份证/2-居住证
	 * @return
	 */
	public List<CitizenAuthModel> queryAuthInfo(@Param("citizenID")String citizenID, @Param("authType")int authType);

	/**
	 * 保存认证业务信息
	 * @param authModel
	 */
	void insertAuth(CitizenAuthModel authModel) throws Exception;
}
