package com.eastcompeace.stat.dailyData;

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
import com.eastcompeace.model.AppDailyDataModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/dailyDataStatisticsctrl")
public class DailyDataStatisticsController extends BaseController{
	private Logger logger = Logger.getLogger("DailyDataStatisticsController");
	@Resource
	private SqlSession sqlSession;
	@Resource
	private DailyDataStatisticsDao dailyDataStatisticsDao;
	/**
	 * 用户访问轨迹跳转主页面
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView orgMenuIndex() {
		return super.getModelView("statistic/appDailyData");
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
		String recordDate = request.getParameter("recordDate");
		AppDailyDataModel addm = new AppDailyDataModel();
		addm.setRecordDate(recordDate);
		
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<AppDailyDataModel> list = dailyDataStatisticsDao.queryForList(addm);
		PageInfo<AccessLogModel> pages = new PageInfo(list);
		jsonObj.put("total", pages.getTotal());
		jsonObj.put("rows", list);
		return jsonObj;
	}
}
