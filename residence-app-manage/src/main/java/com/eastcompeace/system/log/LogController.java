package com.eastcompeace.system.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.Constant;
import com.eastcompeace.model.ButtonModel;
import com.eastcompeace.model.LogModel;
import com.eastcompeace.model.MenuModel;
import com.eastcompeace.model.excel.LogExceModel;
import com.eastcompeace.system.menu.MenuDao;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.ExcelUtils;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 日志管理类
 * 
 * @author xiangpei
 * 
 */
@Controller
@RequestMapping("/logctrl")
public class LogController extends BaseController {
	@Resource
	private SqlSession sqlSession;

	/**
	 * 日志首页
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView menuIndex() {
		return super.getModelView("common/log/");
	}

	/**
	 * 查询日志，包含分页功能
	 * 
	 * @param request
	 * @param response
	 * @return JSONObject
	 * @throws Exception
	 */
	@RequestMapping("/getlist")
	@ResponseBody
	public JSONObject getList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		JSONObject jsonObj = new JSONObject();
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		String strUsername = request.getParameter("strUsername");
		String strIssuccess = request.getParameter("strIssuccess");
		String strLogtype = request.getParameter("strLogtype");
		String strHappentimeStart = request.getParameter("strHappentimeStart");
		String strHappentimeEnd = request.getParameter("strHappentimeEnd");
		LogModel log = new LogModel();
		log.setUsername(strUsername);
		log.setIssuccess(strIssuccess);
		log.setLogtype(strLogtype);
		log.setHappentimeStart(strHappentimeStart);
		log.setHappentimeEnd(strHappentimeEnd);
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<LogModel> list = logDao.queryForList(log);
		PageInfo<LogModel> pages = new PageInfo(list);
		jsonObj.put("total", pages.getTotal());
		jsonObj.put("rows", list);
		return jsonObj;
	}

	/**
	 * 
	 * 删除传入ID的日志
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/del")
	@ResponseBody
	public JSONObject del(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		boolean flag = true;
		String msg = null;
		JSONObject jsonObj = new JSONObject();
		String log_ids = request.getParameter("log_ids");
		String[] idarry = log_ids.split(",");
		List list = Arrays.asList(idarry);
		try {
			int i = logDao.del(list);
			msg = "删除成功";
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			msg = "删除失败";
		}
		jsonObj.put("result", flag);
		jsonObj.put("message", msg);
		return jsonObj;
	}

	/**
	 * 删除所有日志
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delall")
	@ResponseBody
	public JSONObject delall(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		boolean flag = true;
		String msg = null;
		JSONObject jsonObj = new JSONObject();
		try {
			int i = logDao.delall();
			msg = "删除成功";
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			msg = "删除失败";
		}
		jsonObj.put("result", flag);
		jsonObj.put("message", msg);
		return jsonObj;
	}

	/**
	 * 根据传入条件导出excle
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/download")
	@ResponseBody
	public void download(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		String strUsername = request.getParameter("struserId");
		String strIssuccess = request.getParameter("strIssuccess");
		String strLogType = request.getParameter("strLogType");
		String strHappentimeStart = request.getParameter("strHappentimeStart");
		String strHappentimeEnd = request.getParameter("strHappentimeEnd");
		LogModel log = new LogModel();
		log.setUsername(strUsername);
		log.setIssuccess(strIssuccess);
		log.setLogtype(strLogType);
		log.setHappentimeStart(strHappentimeStart);
		log.setHappentimeEnd(strHappentimeEnd);
		List<LogExceModel> list = logDao.queryForDownload(log);
		String fileName = "logInfo_" + DateUtils.getNow("yyyyMMddHHmmss")+ ".xls";
		String tempPath = ResourceUtils.getProperty("LogExcelTempPath");
		List<Object> ObjList = new ArrayList<Object>(list);
		ExcelUtils.exportToExcel(response, null, ObjList, fileName,tempPath);
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
	public JSONObject getToolbar(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		MenuDao md = sqlSession.getMapper(MenuDao.class);
		MenuModel mm = new MenuModel();
		String uri = request.getRequestURI();
		int index = 0;
		String str = uri;
		index = str.lastIndexOf("/");
		str = str.substring(0, index);
		index = str.lastIndexOf("/");
		str = str.substring(index + 1, str.length());
		String menuUrl = str + "/showindex";
		mm.setMenuUrl(menuUrl);
		String menuid = "";
		List<MenuModel> listmenu = md.queryMenuby(mm);
		if (null != listmenu && listmenu.size() > 0) {
			menuid = listmenu.get(0).getMenuId() + "";
		}
		String roleid = request.getSession().getAttribute(Constant.SESSION_ROLEID).toString();
		List<ButtonModel>  listbutton = md.queryButtonListby(roleid, menuid);
		if(null != listbutton && listbutton.size() > 0){
			jsonObj.put("result", listbutton);
		}
		return jsonObj;
	}
}
