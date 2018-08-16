package com.eastcompeace.usermng.info;

import java.util.Arrays;
import java.util.HashMap;
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
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.LogType.Logenum;
import com.eastcompeace.model.CitizenCouponModel;
import com.eastcompeace.model.CitizenMessageModel;
import com.eastcompeace.model.CitizenModel;
import com.eastcompeace.model.CitizenVIPCardModel;
import com.eastcompeace.system.log.LogDao;
import com.eastcompeace.util.BuildModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 
 * @author ecp-zeng
 * @date 2017年1月9日
 */
@Controller
@RequestMapping("/citizenctrl")
public class CitizenController extends BaseController{

	private static Log LOGGER = LogFactory.getLog(CitizenController.class);
	
	@Resource
	private SqlSession sqlSession;
	
	/**
	 * 主页
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView menuIndex() {
		return super.getModelView("usermng/info");
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
	 * 查看用户消息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/list")
	@ResponseBody
	public JSONObject list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		String userName = request.getParameter("userName");
		String userLevel = request.getParameter("userLevel");
		String strStartTime = request.getParameter("strStartTime");
		String strEndTime = request.getParameter("strEndTime");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("userLevel", userLevel);
		map.put("startTime", strStartTime);
		map.put("endTime", strEndTime);
		
		/*验证居民用户账号是否存在和合法*/
		CitizenDao cizitenDao = sqlSession.getMapper(CitizenDao.class);
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<CitizenModel> cizitenList = cizitenDao.queryListBy(map);
		PageInfo<CitizenModel> pages = new PageInfo<CitizenModel>(cizitenList);
		
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel("Y", "查看用户认证信息", String.valueOf(Logenum.QUERYLIST), request));
		
		jsonObj.put("total", pages.getTotal());
		jsonObj.put("rows", pages.getList());
		return jsonObj;
	}
	
	/**
	 * 查看用户身份信息详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/identity")
	@ResponseBody
	public JSONObject identity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String citizenID = request.getParameter("citizenID");
		
		CitizenDao cizitenDao = sqlSession.getMapper(CitizenDao.class);
		CitizenModel identityList = (CitizenModel) cizitenDao.queryIdentity(citizenID);
		
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel("Y", "查看用户身份信息详情", String.valueOf(Logenum.QUERYLIST), request));
		
		jsonObj.put("row", identityList);
		return jsonObj;
	}
	
	/**
	 * 查看用户会员卡
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/vipCard")
	@ResponseBody
	public JSONObject vipCard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		String citizenID = request.getParameter("citizenID");
		
		CitizenDao cizitenDao = sqlSession.getMapper(CitizenDao.class);
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<CitizenVIPCardModel> vipCardList = cizitenDao.queryVipCardListBy(citizenID);
		PageInfo<CitizenVIPCardModel> pages = new PageInfo<CitizenVIPCardModel>(vipCardList);
		
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel("Y", "查看用户会员卡信息", String.valueOf(Logenum.QUERYLIST), request));
		
		jsonObj.put("total", pages.getTotal());
		jsonObj.put("rows", pages.getList());
		return jsonObj;
	}
	
	/**
	 * 查看用户优惠券
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/coupon")
	@ResponseBody
	public JSONObject coupon(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		String citizenID = request.getParameter("citizenID");
		
		CitizenDao cizitenDao = sqlSession.getMapper(CitizenDao.class);
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<CitizenCouponModel> couponList = cizitenDao.queryCouponListBy(citizenID);
		PageInfo<CitizenCouponModel> pages = new PageInfo<CitizenCouponModel>(couponList);
		
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel("Y", "查看用户优惠券信息", String.valueOf(Logenum.QUERYLIST), request));
		
		jsonObj.put("total", pages.getTotal());
		jsonObj.put("rows", pages.getList());
		return jsonObj;
	}
	
	/**
	 * 查看用户消息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/news")
	@ResponseBody
	public JSONObject news(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		String citizenID = request.getParameter("citizenID");
		
		CitizenDao cizitenDao = sqlSession.getMapper(CitizenDao.class);
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<CitizenMessageModel> messageList = cizitenDao.queryMessageListBy(citizenID);
		PageInfo<CitizenMessageModel> pages = new PageInfo<CitizenMessageModel>(messageList);
		
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel("Y", "查看用户消息", String.valueOf(Logenum.QUERYLIST), request));
		
		jsonObj.put("total", pages.getTotal());
		jsonObj.put("rows", pages.getList());
		return jsonObj;
	}
	
	/**
	 * 删除用户消息
	 * 
	 * @param decision
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public JSONObject delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		boolean flag = true;
		String msg = null;
		
		String iIds = request.getParameter("iIds");
		
		String[] iIdarry = iIds.split(",");
		List<String> list = Arrays.asList(iIdarry);
		CitizenDao cizitenDao = sqlSession.getMapper(CitizenDao.class);
		try {
			cizitenDao.deleteMoreBy(list);
			msg = "删除成功";
		} catch (Exception e) {
			LOGGER.error(this, e);
			flag = false;
			msg = "删除失败";
		}
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel(flag? "Y":"N", "删除用户认证信息", String.valueOf(Logenum.DELETEINFO), request));
		
		jsonObj.put("result", flag);
		jsonObj.put("message", msg);
		return jsonObj;
		
	}
	
}