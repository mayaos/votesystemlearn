package com.eastcompeace.stat.access;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.model.AccessLogModel;
import com.eastcompeace.model.LogModel;
import com.eastcompeace.system.log.LogDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/accessctrl")
public class AccessController extends BaseController{
	private Logger logger = Logger.getLogger("BaseController");
	@Resource
	private SqlSession sqlSession;
	@Resource
	private AccessDao accessDao;
	/**
	 * 用户访问轨迹跳转主页面
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView orgMenuIndex() {
		return super.getModelView("statistic/access");
	}
	
	/**
	 * 根据权限生成toolbar
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getToolbar")
	@ResponseBody
	public JSONObject getToolbar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.getToolbar(request, sqlSession);
	}
	
	/**
	 * 查询日志，包含分页功能
	 * 
	 * @param request
	 * @param response
	 * @return JSONObject
	 * @throws Exception
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public JSONObject getList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		String requestUrl = request.getParameter("requestUrl");
		String accessFrom = request.getParameter("accessFrom");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String citizenId = request.getParameter("citizenId");
		String userName = request.getParameter("userName");
		
		AccessLogModel alm = new AccessLogModel();
		alm.setRequestUrl(requestUrl);
		alm.setAccessFrom(accessFrom);
		alm.setStartTime(startTime);
		alm.setEndTime(endTime);
		alm.setCitizenId(citizenId);
		alm.setUserName(userName);
		
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<AccessLogModel> list = accessDao.queryForList(alm);
		PageInfo<AccessLogModel> pages = new PageInfo(list);
		jsonObj.put("total", pages.getTotal());
		jsonObj.put("rows", list);
		return jsonObj;
	}

}
