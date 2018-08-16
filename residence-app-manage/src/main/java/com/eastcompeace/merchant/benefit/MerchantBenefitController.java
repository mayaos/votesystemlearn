package com.eastcompeace.merchant.benefit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.eastcompeace.model.MerchantBenefitModel;
import com.eastcompeace.model.MerchantCouponModel;
import com.eastcompeace.model.OrgArticleModel;
import com.eastcompeace.org.article.OrgArticleDao;
import com.eastcompeace.share.utils.DirectoryUtils;
import com.eastcompeace.share.utils.FileUtils;
import com.eastcompeace.share.utils.StringUtils;
import com.eastcompeace.util.IntegerUtils;
import com.eastcompeace.util.ReadExcel;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/benefitctrl")
public class MerchantBenefitController extends BaseController {

	private static Log LOGGER = LogFactory.getLog(MerchantBenefitController.class);

	@Resource
	private SqlSession sqlSession;

	/**
	 * 商家优惠信息基础页面跳转
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView merchantInfoIndex() {
		return super.getModelView("merchant/benefit/");
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
	 * 查询商家优惠信息列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@ResponseBody
	public JSONObject benefitList(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject josj = new JSONObject();
			
		MerchantBenefitDao mbf = sqlSession.getMapper(MerchantBenefitDao.class);
		
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		String merchantID = request.getParameter("merchantID");
		String benefitName = request.getParameter("benefitName");
		String merchantName = request.getParameter("merchantName");
		String strStartTime = request.getParameter("startTime");
		String strEndTime = request.getParameter("endTime");
		
		MerchantBenefitModel benefit = new MerchantBenefitModel();
		benefit.setMerchantId(merchantID);
		benefit.setBenefitName(benefitName);
		benefit.setMerchantName(merchantName);
		benefit.setCreateTime(strStartTime);
		benefit.setValidTime(strEndTime);
	try {		
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<MerchantBenefitModel> benefitlist = mbf.queryListBy(benefit);
		PageInfo<MerchantBenefitModel> pages = new PageInfo(benefitlist);
		
		josj.put("total", pages.getTotal());
		josj.put("rows",benefitlist);
	} catch (Exception e) {
		LOGGER.error(this, e);
	}
	
	return josj;
	}
	
	/**
	 * 添加商家优惠信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editBenefit")
	@ResponseBody
	public JSONObject editBenefit(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsn = new JSONObject();
		
		MultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest multiRequest = multipartResolver.resolveMultipart(request);
		String merchantIdMuti = multiRequest.getParameter("merchantIdMuti");
		
		MultipartFile couponFile = multiRequest.getFile("couponFile");
		MultipartFile benefitImage = multiRequest.getFile("benefitImage2");
		
		MerchantBenefitModel mbm = new MerchantBenefitModel();
		mbm.setBenefitId(multiRequest.getParameter("benefitId"));
//		mbm.setMerchantId(multiRequest.getParameter("merchantId"));
		mbm.setBenefitName(multiRequest.getParameter("benefitName"));;
		mbm.setBenefitQuota(multiRequest.getParameter("benefitQuota"));
		mbm.setUseExplain(multiRequest.getParameter("useExplain"));
		mbm.setLimitedCount(multiRequest.getParameter("limitedCount"));
		mbm.setDescription(multiRequest.getParameter("description"));
		mbm.setValidTime(multiRequest.getParameter("validTime"));
		
		if(merchantIdMuti != null  && !"".equals(merchantIdMuti)) {
			mbm.setMerchantId(merchantIdMuti);
		} else {
			//mbm.setBenefitId(multiRequest.getParameter("benefitId"));
			mbm.setMerchantId(multiRequest.getParameter("merchantId"));
		}
		
		MerchantBenefitDao benefitDao = sqlSession.getMapper(MerchantBenefitDao.class);
				
		//获取被修改的商家优惠信息
		MerchantBenefitModel info = null;
		//判断若BenefitId为空则为新增优惠信息，否则为修改优惠信息
		if(!"".equals(mbm.getBenefitId()) && mbm.getBenefitId() != null){
			//更新优惠信息
			try{
				info = (MerchantBenefitModel) benefitDao.query(IntegerUtils.integerMinus(mbm.getBenefitId()));
				//更新优惠信息图片
				if(benefitImage != null){
					mbm.setBenefitImage(uploadImage(benefitImage,info.getBenefitImage(), ""));
				}
				benefitDao.update(mbm);
			}catch(Exception e){
				LOGGER.error(this, e);
				jsn.put("result","1");
				jsn.put("message","保存失败");
				return jsn;
			}
			jsn.put("result","0");
			jsn.put("message","保存成功");
		}else{
			//添加优惠信息
			List<Map> list = null;
			if("true".equals(multiRequest.getParameter("importCheck"))){
				/*解析导入文件*/
				String fileName = couponFile.getOriginalFilename();
				try {
					if("xls".equals(ReadExcel.getPostfix(fileName.toString()))){
						list = ReadExcel.readXlsStream(couponFile.getInputStream(), 1);
					}else if("xlsx".equals(ReadExcel.getPostfix(fileName.toString()))){
						list = ReadExcel.readXlsxStream(couponFile.getInputStream(), 1);
					}else{
						jsn.put("result", -1);
						jsn.put("message", "导入文件格式错误，请上传'.xls或者.xlsx'的Excel文件。");
						return jsn;
					}
				} catch (Exception ex) {
					LOGGER.error(this, ex);
					jsn.put("result", -1);
					jsn.put("message", "导入文件解析异常，请核对文件格式。");
					return jsn;
				}
				if(list== null || list.size()<1){
					jsn.put("result", -1);
					jsn.put("message", "导入文件没有数据。");
					return jsn;
				}
				mbm.setBenefitCount(""+list.size());
			}else{
				//String couponRule = multiRequest.getParameter("couponRule");
				String couponSeqStart = multiRequest.getParameter("couponSeqStart");
				String couponSeqEnd = multiRequest.getParameter("couponSeqEnd");
				int count = 1+ IntegerUtils.integer(couponSeqEnd) - IntegerUtils.integer(couponSeqStart);
				if(count<1){
					jsn.put("result", -1);
					jsn.put("message", "优惠券码数量为：" + count);
					return jsn;
				}
				mbm.setBenefitCount(""+count);
			}
			//图片上传文件服务器
			if(benefitImage != null){
				mbm.setBenefitImage(uploadImage(benefitImage,"", ""));
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			mbm.setCreateTime(sdf.format(new Date()));
			try {
				benefitDao.insert(mbm);
				mbm = (MerchantBenefitModel) benefitDao.queryBy(mbm);
				//保存优惠券码
				jsn = saveCoupon(mbm.getBenefitId(), list, multiRequest);
				if(!"0".equals(jsn.get("result"))){
					//保存券码失败，则删除优惠信息和图片
					benefitDao.deleteByID(mbm.getBenefitId());
					return jsn;
				}
			} catch (Exception ex) {
				LOGGER.error(this, ex);
				jsn.put("result", -1);
				jsn.put("message", "导入文件解析异常，请核对文件格式。");
				return jsn;
			}
			
		}
		
		return jsn;
	}
	
	
	private JSONObject saveCoupon(String benefitId, List<Map> list, MultipartHttpServletRequest multiRequest) {
		JSONObject json = new JSONObject();
		MerchantCouponModel coupon = null;
		List<MerchantCouponModel> couponList = new ArrayList<MerchantCouponModel>();;
		MerchantCouponDao couponDao = sqlSession.getMapper(MerchantCouponDao.class);
		if("true".equals(multiRequest.getParameter("importCheck"))){
			for (int i = 0; i < list.size(); i++) {
				coupon = new MerchantCouponModel();
				coupon.setBenefitId(benefitId);
				coupon.setStatus("1");
				coupon.setCouponCode(list.get(i).get("优惠券码")==null? "":list.get(i).get("优惠券码").toString());
				couponList.add(coupon);
				if(couponList.size() == 2000){
					try {
						couponDao.insertMore(couponList);
					} catch (Exception e) {
						LOGGER.error(this, e);
						json.put("result","-1");
						json.put("message","保存券码失败");
						return json;
					}
					couponList = null;
					couponList = new ArrayList<MerchantCouponModel>();
				}
			}
			if(couponList.size()>0){
				try {
					couponDao.insertMore(couponList);
				} catch (Exception e) {
					LOGGER.error(this, e);
					json.put("result","-1");
					json.put("message","保存券码失败");
					return json;
				}
				couponList = null;
			}
		}else{
			String couponRule = multiRequest.getParameter("couponRule");
			String couponSeqStart = multiRequest.getParameter("couponSeqStart");
			String couponSeqEnd = multiRequest.getParameter("couponSeqEnd");
			int seqStart = IntegerUtils.integer(couponSeqStart);
			int seqEnd = IntegerUtils.integer(couponSeqEnd);
			int seqLength = couponSeqEnd.length();
			while(seqStart != (seqEnd+1)){
				coupon = new MerchantCouponModel();
				coupon.setBenefitId(benefitId);
				coupon.setStatus("1");
				coupon.setCouponCode(couponRule + StringUtils.padding(""+seqStart++, seqLength, '0', StringUtils.PAD_LEFT));
				couponList.add(coupon);
				if(couponList.size() == 2000){
					try {
						couponDao.insertMore(couponList);
					} catch (Exception e) {
						LOGGER.error(this, e);
						json.put("result","-1");
						json.put("message","保存券码失败");
						return json;
					}
					couponList = null;
					couponList = new ArrayList<MerchantCouponModel>();
				}
			}
			if(couponList.size()>0){
				try {
					couponDao.insertMore(couponList);
				} catch (Exception e) {
					LOGGER.error(this, e);
					json.put("result","-1");
					json.put("message","保存券码失败");
					return json;
				}
				couponList = null;
			}
		}
		
		json.put("result","0");
		json.put("message","保存成功");
		return json;
	}

	/**
	 * 删除商家优惠信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delbenefit")
	@ResponseBody
	public JSONObject delbenefit(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsonObj=new JSONObject();
		String log_ids = request.getParameter("benefitId");

		MerchantBenefitDao mbf = sqlSession.getMapper(MerchantBenefitDao.class);
		String[] idarry = log_ids.split(",");
		List list = Arrays.asList(idarry);
		try{
			mbf.deleteMoreBy(list);
		}catch(Exception e){
			jsonObj.put("result","1");
			jsonObj.put("message","删除数据失败");
			return jsonObj;
		}
		jsonObj.put("result","0");
		jsonObj.put("message","删除数据成功");
		return jsonObj;
	} 

	/**
	 * 查询优惠券码信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/couponList")
	@ResponseBody
	public JSONObject couponList(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject josj = new JSONObject();
		MerchantCouponDao mcd = sqlSession.getMapper(MerchantCouponDao.class);
		
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		String benefitID = request.getParameter("benefitID");
		
		MerchantCouponModel coupon = new MerchantCouponModel();
		coupon.setBenefitId(benefitID);
		try {
			PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
			List<MerchantCouponModel> couponlist = mcd.queryCoupon(coupon);
			PageInfo<MerchantCouponModel> pages = new PageInfo(couponlist);
			josj.put("total", pages.getTotal());
			josj.put("rows",couponlist);
		} catch (Exception e) {
			LOGGER.error(this, e);
		}
		
		return josj;
	}
	
	/**
	 * 把商家信息设置为推荐
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/setRecommend")
	@ResponseBody
	public JSONObject releaseArticleInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
	
		JSONObject jsos = new JSONObject();
		MerchantBenefitDao mbd = sqlSession.getMapper(MerchantBenefitDao.class);
		//获取需要记录benefitId
		String benefitIds = request.getParameter("benefitId");

		//把benefitIds字符串拆分为数组，数据转换为List
		String[] idArr = benefitIds.split(",");
		List list = Arrays.asList(idArr);
		
		//定义商家信息实例
		try{
			//批量推荐
			mbd.setRecommendMerchant(list);
		}catch(Exception e){
			LOGGER.error("商家推荐失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","商家推荐失败，请稍后再试！");
			return jsos;
		}
		
		jsos.put("result","0");
		jsos.put("message","推荐成功");
		return jsos;
		
	}
	
	/**
	 * 取消推荐
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cancelRecommend")
	@ResponseBody
	public JSONObject cancelRecommendMerchant(HttpServletRequest request,HttpServletResponse response) throws Exception{
	
		JSONObject jsos = new JSONObject();
		MerchantBenefitDao mbd = sqlSession.getMapper(MerchantBenefitDao.class);
		//获取需要记录benefitId
		String benefitIds = request.getParameter("benefitId");

		//把benefitIds字符串拆分为数组，数据转换为List
		String[] idArr = benefitIds.split(",");
		List list = Arrays.asList(idArr);
		
		try{
			//批量取消推荐
			mbd.cancelRecommendMerchant(list);
		}catch(Exception e){
			LOGGER.error("取消推荐失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","取消推荐失败，请稍后再试！");
			return jsos;
		}
		
		jsos.put("result","0");
		jsos.put("message","取消推荐成功");
		return jsos;
		
	}
}
