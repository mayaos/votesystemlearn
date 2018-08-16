package com.eastcompeace.merchant.vip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.merchant.info.MerchantInfoDao;
import com.eastcompeace.model.CitizenVIPCardModel;
import com.eastcompeace.model.MerchantInfoModel;
import com.eastcompeace.model.MerchantVipModel;
import com.eastcompeace.share.cipher.Base64Cipher;
import com.eastcompeace.util.IntegerUtils;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/vipctrl")
public class MerchantVipController extends BaseController{

	private static Log LOGGER = LogFactory.getLog(MerchantVipController.class);
	@Resource
	private SqlSession sqlSession;
	@Resource
	private MerchantVipDao merchantVipDao;

	/**
	 *商家信息表基础页面跳转
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView merchantVipIndex() {
		return super.getModelView("merchant/vip/");
	}
	
	/**
	 * 根据权限生成toolbar
	 * @return
	 */
	@RequestMapping("/getToolbar")
	@ResponseBody
	public JSONObject getToolbar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.getToolbar(request, sqlSession);
	}
	
	/**
	 * 查询商户信息列表
	 * @param request
	 * @param response
	 * @return
	 * @throws exception
	 */
	@RequestMapping("/vipInfoList")
	@ResponseBody
	public JSONObject vipInfoList(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject josj = new JSONObject();
			
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		MerchantVipModel model = new MerchantVipModel();
		model.setMerchantName(request.getParameter("merchantName"));
		model.setAreaName(request.getParameter("areaName"));
		try {
			PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
			List<MerchantVipModel> infolist = merchantVipDao.queryVipInfoList(model);
			PageInfo<MerchantVipModel> pages = new PageInfo<MerchantVipModel>(infolist);
			josj.put("total", pages.getTotal());
			josj.put("rows",infolist);
		} catch (Exception e) {
			LOGGER.error(this, e);
			e.printStackTrace();
			System.out.println("ex");
		}
		
		return josj;
	}
	
	/**
	 * 添加vip信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editVip")
	@ResponseBody
	public JSONObject editMerchant(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsn = new JSONObject();
		
		MultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest multiRequest = multipartResolver.resolveMultipart(request);
		
		
		MerchantVipModel model = new MerchantVipModel();
		model.setVipcardId(multiRequest.getParameter("vipcardId"));
		model.setMerchantId(multiRequest.getParameter("merchantId"));
		model.setVipNoType(multiRequest.getParameter("vipNoType"));
		model.setVipDesc(multiRequest.getParameter("vipDesc"));
		model.setVipRule(multiRequest.getParameter("vipRule"));
		
		try{
			//判断VipcardId是否为空，若不为空则为更新vip信息，为空则为插入新信息
			if(model.getVipcardId() == null || "".equals(model.getVipcardId())){
				//判断该商家是否已有会员信息，若有则不允许添加
				List<MerchantVipModel> list = merchantVipDao.queryVipInfoList(model);
				if(list.size()>0) {
					jsn.put("result","1");
					jsn.put("message","商家已添加会员信息，无法再次添加！");
					return jsn;
				}
				//添加会员信息
				merchantVipDao.insertVipInfo(model);
			} else {
				merchantVipDao.updateVipInfo(model);
			}		
		}catch(Exception e){
			LOGGER.error(this, e);
			jsn.put("result","1");
			jsn.put("message","保存失败，数据保存异常！");
			return jsn;
		}
					
		jsn.put("result","0");
		jsn.put("message","保存成功");
		return jsn;
	}
	/**
	 * 删除商家会员卡信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delinfo")
	@ResponseBody
	public JSONObject delinfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsonObj=new JSONObject();
		
		String log_ids = request.getParameter("vipcardId");
		String[] idarry = log_ids.split(",");
		List<String> list = Arrays.asList(idarry);
		try{
			//删除数据库中的商家会员卡信息
			merchantVipDao.deleteVipInfoList(list);	
			//删除citizen_vipcard中涉及到的数据
			merchantVipDao.deleteVipMappingList(list);
		}catch(Exception e){
			LOGGER.error(this, e);
			jsonObj.put("result","1");
			jsonObj.put("message","删除记录失败");
			return jsonObj;
		}
		
		jsonObj.put("result","0");
		jsonObj.put("message","删除记录成功");
		return jsonObj;
	} 
}
