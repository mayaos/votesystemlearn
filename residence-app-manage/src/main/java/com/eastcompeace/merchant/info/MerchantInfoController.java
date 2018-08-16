package com.eastcompeace.merchant.info;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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
import com.eastcompeace.merchant.benefit.MerchantBenefitDao;
import com.eastcompeace.merchant.benefit.MerchantCouponDao;
import com.eastcompeace.model.CitizenVIPCardModel;
import com.eastcompeace.model.MerchantBenefitModel;
import com.eastcompeace.model.MerchantInfoModel;
import com.eastcompeace.share.cipher.Base64Cipher;
import com.eastcompeace.util.Constant;
import com.eastcompeace.util.IntegerUtils;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/merchantInfoctrl")
public class MerchantInfoController extends BaseController{

	private static Log LOGGER = LogFactory.getLog(MerchantInfoController.class);
	@Resource
	private SqlSession sqlSession;

	/**
	 *商家信息表基础页面跳转
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView merchantInfoIndex() {
		return super.getModelView("merchant/info/");
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
	@RequestMapping("/merchantInfo")
	@ResponseBody
	public JSONObject showMerchantInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject josj = new JSONObject();
		MerchantInfoDao pt = sqlSession.getMapper(MerchantInfoDao.class);
		String folder = ResourceUtils.getProperty("httpDownloadURL");
		
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		MerchantInfoModel model = new MerchantInfoModel();
		model.setMerchantName(request.getParameter("merchantName"));
		model.setContacts(request.getParameter("contacts"));
		try {
			PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
			List<MerchantInfoModel> infolist = pt.selectinfo(model);
			//组拼照片1,2,3完整路径，URL=固定路径+图片Id
			for(MerchantInfoModel m : infolist) {
				if(m.getPhoto1()!=null && !"".equals(m.getPhoto1())) {
					m.setPhoto1(folder+m.getPhoto1());
				}
				if(m.getPhoto2()!=null && !"".equals(m.getPhoto2())) {
					m.setPhoto2(folder+m.getPhoto2());
				}
				if(m.getPhoto3()!=null && !"".equals(m.getPhoto3())) {
					m.setPhoto3(folder+m.getPhoto3());
				}
			}
			PageInfo<MerchantInfoModel> pages = new PageInfo<MerchantInfoModel>(infolist);
			josj.put("total", pages.getTotal());
			josj.put("rows",infolist);
		} catch (Exception e) {
			LOGGER.error(this, e);
			e.printStackTrace();
			System.out.println("ex");
		}
		
		return josj;
	}
	
	
	@RequestMapping("/vipcardList")
	@ResponseBody
	public JSONObject vipcardList(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject josj = new JSONObject();
		MerchantInfoDao pt = sqlSession.getMapper(MerchantInfoDao.class);
		
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		
		CitizenVIPCardModel model = new CitizenVIPCardModel();
		model.setMerchantID(request.getParameter("merchantID"));
		model.setVipcardCode(request.getParameter("vipcardCode"));
		try {
			PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
			List<CitizenVIPCardModel> infolist = pt.queryVipcardList(model);
			PageInfo<MerchantInfoModel> pages = new PageInfo(infolist);
			josj.put("total", pages.getTotal());
			josj.put("rows",infolist);
		} catch (Exception e) {
			LOGGER.error(this, e);
		}
		return josj;
	}
	
	/**
	 * 添加商家信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editMerchant")
	@ResponseBody
	public JSONObject editMerchant(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsn = new JSONObject();
		
		MultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest multiRequest = multipartResolver.resolveMultipart(request);
		MultipartFile lecenseImage = multiRequest.getFile("lecenseImage2");
		MultipartFile merchantLogo = multiRequest.getFile("merchantLogo2");
		MultipartFile photo1 = multiRequest.getFile("photo12");
		MultipartFile photo2 = multiRequest.getFile("photo22");
		MultipartFile photo3 = multiRequest.getFile("photo32");
		
		String fileUrl = ResourceUtils.getProperty("getFileUrl") + ResourceUtils.getProperty("merchantFiles");
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSS");
		
		// DAS系统传过来的 placeId
		String placeId = multiRequest.getParameter("dasPlaceId");
		String merchantNature = multiRequest.getParameter("merchantNature");
		String chainMerchant = null;
		//连锁店  如果是连锁店才 去取 是“总店”或“分店”
		if(Constant.MERCHANT_NATURE_CHAIN.equals(merchantNature)){ 
			chainMerchant = multiRequest.getParameter("chainMerchant");
		} 
		
		MerchantInfoDao infoDao = sqlSession.getMapper(MerchantInfoDao.class);
		MerchantInfoModel mif = new MerchantInfoModel();
		mif.setMerchantId(multiRequest.getParameter("merchantId"));
		mif.setMerchantName(multiRequest.getParameter("merchantName"));
		
		mif.setMerchantHeadName(multiRequest.getParameter("merchantHeadName"));
		mif.setMerchantNature(multiRequest.getParameter("merchantNature"));
		mif.setMerchantChain(chainMerchant);
		
		mif.setAreaID(multiRequest.getParameter("areaID"));
		mif.setIndustryType(multiRequest.getParameter("industryType"));
		mif.setMerchantType(multiRequest.getParameter("merchantType"));
		mif.setContacts(multiRequest.getParameter("contacts"));
		mif.setTelephone(multiRequest.getParameter("telephone"));
		mif.setAddress(multiRequest.getParameter("address"));
		mif.setDescription(multiRequest.getParameter("description"));
		
		//获取修改的商家信息
		MerchantInfoModel info = null;
		if(!"".equals(mif.getMerchantId()) && mif.getMerchantId() != null){
			info = (MerchantInfoModel) infoDao.query(IntegerUtils.integerMinus(mif.getMerchantId()));
		}
		
		//上传图片文件到服务器并把返回的图片Id保存到mif里，如果为更新图片信息则删除旧图片
		try {
			if(photo1 != null && !"".equals(photo1)){
				String oldPhoto1 = null;
				if(info!=null) {
					oldPhoto1 = info.getPhoto1();
				}
				mif.setPhoto1(uploadImage(photo1 ,oldPhoto1, ""));
			}
			if(photo2 != null && !"".equals(photo2)){
				String oldPhoto2 = null;
				if(info!=null) {
					oldPhoto2 = info.getPhoto2();
				}
				mif.setPhoto2(uploadImage(photo2 ,oldPhoto2, ""));
			}
			if(photo3 != null && !"".equals(photo3)){
				String oldPhoto3 = null;
				if(info!=null) {
					oldPhoto3 = info.getPhoto3();
				}
				mif.setPhoto3(uploadImage(photo3 ,oldPhoto3, ""));
			}			
		} catch (Exception e) {
			LOGGER.error(this, e);
			jsn.put("result","1");
			jsn.put("message","图片上传失败！");
			return jsn;
		}

	
		
		//InputStream is = null;
		try{
			if(lecenseImage != null && !"".equals(lecenseImage)){
				/*is = lecenseImage.getInputStream();
				byte[] data = new byte[is.available()];
				is.read(data);
				is.close();
				is = null;*/
				mif.setLecenseImage(new String(Base64Cipher.encode(lecenseImage.getBytes())));
			}
			if(merchantLogo != null && !"".equals(merchantLogo)){
				mif.setMerchantLogo(new String(Base64Cipher.encode(merchantLogo.getBytes())));
			}
			
			if(!"".equals(mif.getMerchantId()) && mif.getMerchantId() != null){
				infoDao.update(mif);
			}else{
				// 获取商家ID
				String merchantId = genMerchantId(infoDao, mif);
				mif.setMerchantId(merchantId);
				infoDao.insertinfo(mif);
				
				// DAS商家编号同步接口
				String url = ResourceUtils.getProperty("dasSetPlaceCodeURL");
	            String json = "{'auth': {'keyIndex':'0','randomNum':'2f8d3cb321d046ac','randomNumEnc':'2E1E933814508B38'},'data': {placeId:'" + placeId + "',placeCode:'" +  merchantId + "',}}";
	            JSONObject jsonObject = callService(url, json);
	            
	            System.out.println(jsonObject.getString("result"));
	            //修改DAS同步商家编码返回 信息
	            infoDao.updateDASReturnInfo(jsonObject.getString("result"), merchantId);
	            
			}
		}catch(Exception e){
			LOGGER.error(this, e);
			jsn.put("result","1");
			jsn.put("message","保存失败，数据保存异常！");
			return jsn;
		}/*finally{
			if(is != null) is.close();
		}*/
					
		jsn.put("result","0");
		jsn.put("message","保存成功");
		return jsn;
	}
	
	/**
	 * 生成商家表（merchant_info）的商家ID
	 * @param infoDao
	 * @param info
	 * @return
	 * @throws Exception
	 */
	private String genMerchantId(MerchantInfoDao infoDao, MerchantInfoModel info) throws Exception {
		String merchantId = "";
		// 包含 地区、行业类型 把行业类型从“1”变“01”
		String areaIndustry = info.getAreaID() + info.getIndustryType();
		String merchantNature = info.getMerchantNature();
		//商家性质   0: 直营店
		if(Constant.MERCHANT_NATURE_DIRECT.equals(merchantNature)){ 
			
			// 00 代表直营店
			String areaIndustryDirect = areaIndustry + "00";
			MerchantInfoModel merchantM = infoDao.selectMaxMerchantDirectId(areaIndustryDirect);
			
			if(merchantM != null){
				merchantId = merchantM.getMerchantId();
				long merchantDirectId = Long.valueOf(merchantId) + 1;
				merchantId = String.valueOf(merchantDirectId);
			} else { //数据库还没有这个地区 行业的数据
				merchantId = areaIndustryDirect + "0001";
			}
			
			
		} else { //商家性质   1: 连锁店
			String merchantChain = info.getMerchantChain();
			
			//连锁类型  0: 总店 
			if(Constant.MERCHANT_CHAIN_HEAD.equals(merchantChain)){ 
				
				MerchantInfoModel merchantM = infoDao.selectMaxMerchantChainId(areaIndustry);
				
				if(merchantM != null){
					merchantId = merchantM.getMerchantId();
					long merchantChainId = Long.valueOf(merchantId) + 1;
					merchantId = String.valueOf(merchantChainId) + "0000";
				} else { //数据库还没有这个地区 行业的数据
					merchantId = areaIndustry + "010000";
				}
				
				
			} else { //连锁类型   1: 分店
				merchantId = info.getMerchantHeadName();
				// merchantId.substring(0, 10) 包含 地区、行业类型
				MerchantInfoModel merchantM = infoDao.selectMaxMerchantChainBranchId(merchantId.substring(0, 10));
				
				String merchantChainId = merchantM.getMerchantId();
				long merchantChainId2 = Long.valueOf(merchantChainId) + 1;
				merchantId = String.valueOf(merchantChainId2);
				
			}
			
		}
		return merchantId;
		
	}
	
	/**
	 * 删除商家信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delinfo")
	@ResponseBody
	public JSONObject delinfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsonObj=new JSONObject();
		MerchantInfoDao mid = sqlSession.getMapper(MerchantInfoDao.class);
		
		String log_ids = request.getParameter("merchantId");
		String[] idarry = log_ids.split(",");
		List<String> list = Arrays.asList(idarry);
		List<MerchantInfoModel> merchantList = null;
		List<MerchantBenefitModel> benefitIdList = null;
		List<String> couponIdList = null;
		try{
			//查询需要删除的商家信息，得到存储到文件服务器的图片Id，用于删除服务器上的图片
			merchantList = mid.queryMerchantByList(list);
			//查询商家优惠信息ID，用于删除与需要删除的商家关联的（1、优惠信息 ）
			benefitIdList = mid.queryBenefitByList(list);
			//查询优惠券ID，由于删除（1、优惠券信息 2、与用户关联的优惠券信息）
			List<String> arrList = new ArrayList<>();
			if(benefitIdList.size()>0) {
				for(MerchantBenefitModel b:benefitIdList) {
					arrList.add(b.getBenefitId());
				}
			}
			couponIdList = mid.queryCouponIdByList(arrList);
				
			//删除数据库中相关居民会员卡关系表信息
			mid.delCitizenVipcardList(list);
			//删除数据库中相关的会员信息
			mid.delMerchantVipcardList(list);
			//删除与用户关联的优惠券信息
			MerchantCouponDao mcd = sqlSession.getMapper(MerchantCouponDao.class);
			mcd.delCitizenCoupon(couponIdList);
			//删除优惠券信息		
			mcd.delcoupon(couponIdList);
			//删除优惠信息
			MerchantBenefitDao mbf = sqlSession.getMapper(MerchantBenefitDao.class);
			mbf.deleteBenefitBy(arrList);		
			//删除数据库中的商家信息
			mid.delinfo(list);
			
		}catch(Exception e){
			LOGGER.error(this, e);
			jsonObj.put("result","1");
			jsonObj.put("message","删除记录失败");
			return jsonObj;
		}
		
		//删除文件服务器上的图片
		try {
			if(merchantList.size()>0) {
				for(MerchantInfoModel m : merchantList) {
					if(m.getPhoto1()!=null && !"".equals(m.getPhoto1())) {
						deleteImg(m.getPhoto1());
					}
					if(m.getPhoto2()!=null && !"".equals(m.getPhoto2())) {
						deleteImg(m.getPhoto2());
					}
					if(m.getPhoto3()!=null && !"".equals(m.getPhoto3())) {
						deleteImg(m.getPhoto3());
					}
				}
			}	
			
			if(benefitIdList.size()>0) {
				for(MerchantBenefitModel b : benefitIdList) {
					if(b.getBenefitImage()!=null && !"".equals(b.getBenefitImage())) {
						deleteImg(b.getBenefitImage());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(this, e);
			jsonObj.put("result","1");
			jsonObj.put("message","删除图片文件失败");
			return jsonObj;
		}
		
		
		jsonObj.put("result","0");
		jsonObj.put("message","删除记录成功");
		return jsonObj;
	} 
	
	/**
	 * 下拉框选择商家列表
	 * @param req
	 * @return
	 */
	@RequestMapping("/selectMerchantList")
	@ResponseBody
	public JSONArray selectMerchantList(HttpServletRequest req) {
		//获取参数
		String areaID = req.getParameter("areaID");
		String industryType = req.getParameter("industryType");
		String merchantNature = req.getParameter("merchantNature");
		String merchantId = req.getParameter("merchantId");
		
		MerchantInfoModel infoModel = new MerchantInfoModel();
		infoModel.setAreaID(areaID);
		infoModel.setIndustryType(industryType);
		if(merchantNature != null) { 
			if(Constant.MERCHANT_NATURE_DIRECT.equals(merchantNature)) { // 直营店
				infoModel.setMerchantNature(Constant.MERCHANT_NATURE_DIRECT);
				
			} else if(Constant.MERCHANT_NATURE_CHAIN.equals(merchantNature)) { // 连锁店总店
				
				infoModel.setMerchantNature(Constant.MERCHANT_NATURE_CHAIN); // 连锁店
				infoModel.setMerchantChain(Constant.MERCHANT_CHAIN_HEAD); //总店
			} else {// 连锁店分店
				infoModel.setMerchantNature(Constant.MERCHANT_NATURE_CHAIN); // 连锁店
				infoModel.setMerchantChain(Constant.MERCHANT_CHAIN_BRANCH); // 分店
			}
		}
		if(merchantId != null) {
			infoModel.setMerchantId(merchantId.substring(0, 10));
			infoModel.setMerchantChain(null);
		}
		
		MerchantInfoDao infoDao = sqlSession.getMapper(MerchantInfoDao.class);
		//定义变量
		List<MerchantInfoModel> infoList = new ArrayList<MerchantInfoModel>();
		//访问数据库获取区域信息列表
		try {
			infoList = infoDao.selectMerchantList(infoModel);
		} catch (Exception e) {
			LOGGER.error("数据库异常，获取区域列表信息失败:" + e.getMessage());
			return null;
		}	
		
		JSONArray jsonArray = JSONArray.fromObject(infoList);
		return jsonArray;
	}
	
	/**
	 * 下拉框选择商家列表
	 * @param req
	 * @return
	 */
	@RequestMapping("/searchMerchantNameInDAS")
	@ResponseBody
	public JSONObject searchMerchantNameInDAS(HttpServletRequest request,HttpServletResponse response) {
		//获取参数
		String merchantName = request.getParameter("merchantName");
		String industryType = request.getParameter("industryType");
		String areaID = request.getParameter("areaID");
		System.out.println("merchantName= " + merchantName);
		
		
		JSONObject josj = new JSONObject();
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
//		MerchantInfoModel model = new MerchantInfoModel();
//		model.setMerchantName(request.getParameter("merchantName"));
//		model.setContacts(request.getParameter("contacts"));
//		model.setAreaID("330102");
		try {
			PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
//			List<MerchantInfoModel> infolist = pt.selectMerchantList(model);
			
//            String url = ResourceUtils.getProperty("dasPlaceListForRCAppURL");
//            String json = "{'auth': {'keyIndex':'1','randomNum':'2f8d3cb321d046ac','randomNumEnc':'0eda4431129a548b'},'data': {'strPlaceType':'05','strPlaceName': '','strDivisionsId':''}}";
            
            String url = ResourceUtils.getProperty("dasPlaceListForRCAppURL");
//			String url = "http://localhost:8082/residence-das-manage/rcAppCtrl/placeListForRCApp";

//            industryType = "0" + industryType;
            String json = "{'auth': {'keyIndex':'0','randomNum':'2f8d3cb321d046ac','randomNumEnc':'2E1E933814508B38'}," + 
            		"'data': {'strPlaceType':'" + industryType + "','strPlaceName': '" + merchantName + "','strDivisionsId':'" + areaID+ "'}}";
            JSONObject jsonObject = callService(url, json);
            if(!"0".equals(jsonObject.getString("result"))) {
            	LOGGER.error(jsonObject.toString());
            	return josj;
            }
            JSONArray childs= jsonObject.getJSONArray("placeList");  
            List<MerchantInfoModel> infolist = new ArrayList<MerchantInfoModel>();
            int length = childs.size();
            for (int i = 0; i < length; i++) {  
                jsonObject = childs.getJSONObject(i);  
                String placeId = jsonObject.getString("placeId");  
                String childName = jsonObject.getString("placeName"); 
                MerchantInfoModel modelC = new MerchantInfoModel();
                modelC.setMerchantId(placeId);
                modelC.setMerchantName(childName);
                
                infolist.add(modelC);
                System.out.println(placeId);
                System.out.println(childName);
            }  
			
			PageInfo<MerchantInfoModel> pages = new PageInfo<MerchantInfoModel>(infolist);
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
	 * 下拉框选择连锁商家总店列表
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/merchantHeadList")
	@ResponseBody
	public JSONArray merchantHeadList(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//获取参数
		String areaID = request.getParameter("areaID");
		String industryType = request.getParameter("industryType");
		// 包含 地区、行业类型 把行业类型从“1”变“01”
		String areaIndustry = areaID + "0" + industryType;
		
		MerchantInfoDao merchantInfoDao = sqlSession.getMapper(MerchantInfoDao.class);
		List<MerchantInfoModel> merchantHeadList = merchantInfoDao.selectMerchantHeadList(areaIndustry);
		JSONArray jsonArray = JSONArray.fromObject(merchantHeadList);
		
		return jsonArray;
		
	}
	
	private JSONObject callService(String urlStr, String jsonStr) {
		JSONObject jsonObject = null;
		try {
			URL url = new URL(urlStr);
	        //Insert your JSON query request
	        String query = jsonStr;
	        //It change the apostrophe char to double colon char, to form a correct JSON string
	        query=query.replace("'", "\"");
	
	        //make connection
	        HttpURLConnection connection = (HttpURLConnection) url 
                    .openConnection(); 
            connection.setDoOutput(true); 
            connection.setDoInput(true); 
            connection.setRequestMethod("POST"); //设置请求方法
            connection.setRequestProperty("Charsert", "UTF-8"); //设置请求编码
            connection.setUseCaches(false); 
            connection.setInstanceFollowRedirects(true); 
            connection.setRequestProperty("Content-Type", 
                    "application/json"); 

            connection.connect(); 

            //POST请求 
            PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(),"utf-8"));  
            out.println(query);
            out.flush(); 
            out.close(); 
	
	
	
	        //get result
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
	        String lines;
			StringBuffer sb = new StringBuffer("");
	        while ((lines=br.readLine())!=null) {
//	           lines = new String(lines.getBytes(), "UTF-8");
			    sb.append(lines);
	        }
	        System.out.println(sb.toString());
	        br.close();
	        
	        jsonObject = JSONObject.fromObject(sb.toString());
		} catch(Exception e) {
			e.printStackTrace();
		}
        
        return jsonObject;
	}
	
	
}
