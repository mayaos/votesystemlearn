package com.eastcompeace.system.codedict;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.eastcompeace.base.BaseDao;
import com.eastcompeace.model.CodedictModel;

/**
 * 用户DAO接口
 * 
 * @author suyang
 */

@Transactional
public interface CodedictDao extends BaseDao{
	
	/**
	 * 查询code_dict分页列表
	 * @param CodedictModel
	 * @return List<CodedictModel>
	 * @throws Exception
	 */
	public List<CodedictModel> queryCodeList(CodedictModel cm) throws Exception;
	
	
	/**
	 * 添加code_dict记录
	 * @param CodedictModel
	 * @return
	 * @throws Exception
	 */
	public void insertCodeInfo(CodedictModel cm)throws Exception;
	
	/**
	 * 修改code_dict记录
	 * @param CodedictModel
	 * @return
	 * @throws Exception
	 */
	public void updateCodeInfo(CodedictModel cm)throws Exception;
	
	/**
	 * 删除code_dict记录
	 * @param String
	 * @return
	 * @throws Exception
	 */
	public void deleteCodedictInfo(String cType,String cTypeName,String cValue)throws Exception;
	
	/**
	 * 根据字典类型查找字典名称和字典值
	 * 
	 * @param codeType
	 * @return
	 * @throws Exception
	 */
	public List<CodedictModel> selectCodelist(String codeType) throws Exception;
	/**
	 * 根据码值类型，码值查询数据是否存在
	 * @param codedictModel 
	 * @return 0：数据不存在，其他数值：数据存在
	 * @throws Exception
	 */
	public Integer queryCodedict(CodedictModel codedictModel) throws Exception;

	/**
	 * 关联关系码字查询
	 * @param codeType
	 * @param relationType
	 * @return
	 */
	public List<CodedictModel> relationSelectList(@Param("codeType")String codeType, @Param("relationType")String relationType);
}
