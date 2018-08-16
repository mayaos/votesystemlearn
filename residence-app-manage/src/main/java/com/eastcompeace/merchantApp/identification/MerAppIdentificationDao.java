package com.eastcompeace.merchantApp.identification;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.eastcompeace.model.MerchantAppModel;
import com.eastcompeace.model.OrgMenuModel;

@Repository("merAppIdentificationDao")
public interface MerAppIdentificationDao {
	
	/**
	 * 获取商家app列表
	 * @return
	 * @throws Exception
	 */
	public List<MerchantAppModel> listMerchatApp() throws Exception;
	
    /**
     * 获取商家认证
     * @return
     * @throws Exception
     */
    public void updateMerApp(MerchantAppModel model) throws Exception;
}
