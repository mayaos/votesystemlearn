package com.eastcompeace.pubFAQ;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.eastcompeace.model.FAQModel;

/**
 * 常见问题信息Dao接口
 * @author Administrator
 *
 */
@Repository
public interface PubFAQDao {
 
	/**
	 * 查询常见问题信息
	 * @return
	 * @throws Exception
	 */
	public List<FAQModel> selectFAQ(FAQModel model) throws Exception;
	/**
	 * 添加常见问题信息
	 * @param pm
	 * @throws Exception
	 */
	public void addFAQ(FAQModel pm) throws Exception;
	/**
	 * 删除常见问题信息
	 * @param pm
	 * @throws Exception
	 */
	public void delFAQ(List list) throws Exception;
	
	/**
	 * 修改常见问题信息
	 * @param pm
	 * @throws Exception
	 */
	public void updateFAQ(FAQModel pm) throws Exception;
	   /**
     * 发布问题
     * @param list
     * @throws Exception
     */
    public void releaseFAQ(List list) throws Exception;
    /**
     * 取消发布问题
     * @param list
     * @throws Exception
     */
    public void delreleaseFAQ(List list) throws Exception;  
}
