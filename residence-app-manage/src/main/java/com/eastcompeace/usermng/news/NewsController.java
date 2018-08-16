package com.eastcompeace.usermng.news;

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
import com.eastcompeace.model.CitizenMessageModel;
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
@RequestMapping("/newsctrl")
public class NewsController extends BaseController{

	private static Log LOGGER = LogFactory.getLog(NewsController.class);
	
	@Resource
	private SqlSession sqlSession;
	
	/**
	 * 意见（投诉建议）主页
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView menuIndex() {
		return super.getModelView("usermng/news");
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
		String status = request.getParameter("msgStatus");
		String userName = request.getParameter("userName");
		String strStartTime = request.getParameter("strStartTime");
		String strEndTime = request.getParameter("strEndTime");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("messageStatus", status);
		map.put("userName", userName);
		map.put("startTime", strStartTime);
		map.put("endTime", strEndTime);
		
		/*验证居民用户账号是否存在和合法*/
		NewsDao newsDao = sqlSession.getMapper(NewsDao.class);
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<CitizenMessageModel>  msgList = newsDao.queryListBy(map);
		PageInfo<CitizenMessageModel> pages = new PageInfo<CitizenMessageModel>(msgList);
		
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel("Y", "查看用户消息", String.valueOf(Logenum.QUERYLIST), request));
		
		jsonObj.put("total", pages.getTotal());
		jsonObj.put("rows", pages.getList());
		return jsonObj;
	}
	
	/**
	 * 编辑用户消息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public JSONObject edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		boolean flag = true;
		String msg = null;
		
		String messageID = request.getParameter("messageID");
		String citizenID = request.getParameter("citizenID");
		String messageTitle = request.getParameter("messageTitle");
		String messageContent = request.getParameter("messageContent");
		String messageStatus = request.getParameter("messageStatus");
		
		CitizenMessageModel msgModel = new CitizenMessageModel();
		msgModel.setMessageID(messageID);
		msgModel.setCitizenID(citizenID);
		msgModel.setMessageTitle(messageTitle);
		msgModel.setMessageContent(messageContent);
		msgModel.setMessageStatus(messageStatus);
		
		NewsDao newsDao = sqlSession.getMapper(NewsDao.class);
		try {
			if(messageID ==null || "".equals(messageID)){
				newsDao.insert(msgModel);
			}else{
				newsDao.update(msgModel);
			}
			msg = "保存成功";
		} catch (Exception e) {
			LOGGER.error(this, e);
			flag = false;
			msg = "保存失败";
		}
		
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel(flag? "Y":"N", "编辑用户消息", String.valueOf(Logenum.VOTED), request));
		
		jsonObj.put("result", flag);
		jsonObj.put("message", msg);
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
		NewsDao newsDao = sqlSession.getMapper(NewsDao.class);
		try {
			newsDao.deleteMoreBy(list);
			msg = "删除成功";
		} catch (Exception e) {
			LOGGER.error(this, e);
			flag = false;
			msg = "删除失败";
		}
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel(flag? "Y":"N", "删除用户消息", String.valueOf(Logenum.DELETEINFO), request));
		
		jsonObj.put("result", flag);
		jsonObj.put("message", msg);
		return jsonObj;
		
	}
	
}