package com.eastcompeace.usermng.authuser;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.CitizenAuthModel;
import com.eastcompeace.model.CitizenUserModel;
import com.eastcompeace.model.RcCardInfoModel;
import com.eastcompeace.model.excel.AuthExcelModel;

/**
 * 身份证认证Dao
 * @author xianzehua
 * @date 2017年2月24日
 */
@Transactional
public interface AuthUserDao extends BaseDao{

	/**
	 * 查询身份证认证信息
	 * @param mParam
	 * @return
	 * @throws Exception
	 */
	public  List<CitizenAuthModel> selectUserInfo(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询rc_card_info表
	 * @param mParam
	 * @return
	 * @throws Exception
	 */
	public  List<RcCardInfoModel> selectRcCardInfoForPerson(List<String> list) throws Exception;
	
	/**
	 * 查询rc_card_info表
	 * @return
	 * @throws Exception
	 */
	public  List<RcCardInfoModel> selectRcCardInfo() throws Exception;
	
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
	 * 查询最大的导入日期
	 * @return
	 * @throws Exception
	 */
	public String selectMaxImportDate() throws Exception;
	
	/**
	 * 根据条件进行查询
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<CitizenAuthModel> selectForCondition(List list) throws Exception;
	
}
