package com.eastcompeace.pubguide;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.Constant;
import com.eastcompeace.base.LogType.Logenum;
import com.eastcompeace.model.PubGuideModel;
import com.eastcompeace.model.UserModel;
import com.eastcompeace.system.log.LogDao;
import com.eastcompeace.util.BuildModel;
import com.eastcompeace.util.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * 指南信息表（增、删、改、查）
 * @author cuichenyao
 *
 */
@Controller
@RequestMapping("/guidectrl")
public class PubGuideController extends BaseController{
	private Logger logger = Logger.getLogger("PubGuideController");
	@Resource
	private SqlSession sqlSession;
	/**
	 * 指南信息表基础页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView codedictIndex() {
		return super.getModelView("common/pubguide/");
	}
	
	/**
	 * 查询指南表列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/guidelist")
	@ResponseBody
	public JSONObject queryGuide(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsn = new JSONObject();
		PubGuideDao pgd = sqlSession.getMapper(PubGuideDao.class);
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		PubGuideModel pgm = new PubGuideModel();
		pgm.setAreaId(request.getParameter("areaIdSearch"));
		pgm.setValid(request.getParameter("issueOrNotSearch"));
		pgm.setCreateTime(request.getParameter("createTime"));
		String guideType = request.getParameter("guideTypeSearch");
		if(StringUtils.isNotBlank(guideType)){
			pgm.setGuideType(Integer.valueOf(guideType));
		}
		try {
			PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
			List<PubGuideModel> guidelist = pgd.selectguide(pgm);
			PageInfo<PubGuideModel> pages = new PageInfo(guidelist);
			jsn.put("total", pages.getTotal());
			jsn.put("rows",guidelist);
			logDao.add(BuildModel.getModel( "Y", "查询指南分页", String.valueOf(Logenum.QUERYLIST), request));
		} catch (Exception e) {
			logDao.add(BuildModel.getModel( "N", "查询指南分页", String.valueOf(Logenum.QUERYLIST), request));
			logger.info("查询指南分页:"+e);
		}
		return jsn;
	}
	/**
	 * 添加指南信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addguide")
	@ResponseBody
	public JSONObject addguide(HttpServletRequest request,HttpServletResponse response) throws Exception{
		UserModel user = new UserModel();
		HttpSession session = request.getSession();
		user  = (UserModel) session.getAttribute(Constant.SESSION_USER);
		JSONObject jsn = new JSONObject();
		PubGuideDao pgd = sqlSession.getMapper(PubGuideDao.class);
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		PubGuideModel pgm = new PubGuideModel();
		pgm.setCreateTime(DateUtils.getNow());
		pgm.setGuideContent(request.getParameter("guideContent"));
		pgm.setValid(request.getParameter("valid"));
		pgm.setUserId(user.getUserId());
		pgm.setGuideType(Integer.parseInt(request.getParameter("guideType")));
		pgm.setAreaId(request.getParameter("areaId"));
		try{
			//增加信息之前先判断信息是否存在重复，指南信息要求每个区域同一类型的信息只能存在一条记录
			PubGuideModel tempModel = new PubGuideModel();
			tempModel.setAreaId(request.getParameter("areaId"));
			tempModel.setGuideType(Integer.parseInt(request.getParameter("guideType")));
			List<PubGuideModel> list = pgd.selectguide(tempModel);
			if(list.size()!=0) {
				jsn.put("result","1");
				jsn.put("message","该地区已存在该类型指南，无法添加！");
				logDao.add(BuildModel.getModel( "N", "添加指南", String.valueOf(Logenum.ADDINFO), request));
				return jsn;
			}
			pgd.addguide(pgm);
			logDao.add(BuildModel.getModel( "Y", "添加指南", String.valueOf(Logenum.ADDINFO), request));
			jsn.put("result","0");
			jsn.put("message","添加成功");
			return jsn;
		}catch(Exception e){
			jsn.put("result","1");
			jsn.put("message","添加失败");
			logDao.add(BuildModel.getModel( "N", "添加指南", String.valueOf(Logenum.ADDINFO), request));
			logger.info("添加指南:"+e);
			return jsn;
		}
		
	}
	/**
	 * 修改指南信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editguide")
	@ResponseBody
	public JSONObject editguide(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession();
		UserModel user = new UserModel();
		user  = (UserModel) session.getAttribute(Constant.SESSION_USER);
		JSONObject jsn=new JSONObject();
		PubGuideDao pgd = sqlSession.getMapper(PubGuideDao.class);
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		PubGuideModel pgm = new PubGuideModel();
		pgm.setGuideId(Integer.parseInt(request.getParameter("guideId")));
		pgm.setGuideType(Integer.parseInt(request.getParameter("guideType")));
		pgm.setAreaId(request.getParameter("areaId"));
		pgm.setGuideContent(request.getParameter("guideContent"));
		pgm.setValid(request.getParameter("valid"));
		pgm.setUserId(user.getUserId());
		pgm.setUpdateTime(DateUtils.getNow());
		try{
			//修改息之前先判断信息是否存在多条重复，指南信息要求每个区域同一类型的信息只能存在一条记录
			List<PubGuideModel> list = pgd.selectguide(pgm);
			if(list.size()>1) {
				jsn.put("result","1");
				jsn.put("message","指南类型重复无法修改！");
				logDao.add(BuildModel.getModel( "N", "修改指南", String.valueOf(Logenum.ADDINFO), request));
				return jsn;
			}
			if(list.size()==1 && list.get(0).getGuideId().intValue()!=pgm.getGuideId().intValue()) {
				jsn.put("result","1");
				jsn.put("message","指南类型重复无法修改！");
				logDao.add(BuildModel.getModel( "N", "修改指南", String.valueOf(Logenum.ADDINFO), request));
				return jsn;
			}
			pgd.updateguide(pgm);
			jsn.put("result","0");
			jsn.put("message","修改成功");
			logDao.add(BuildModel.getModel( "Y", "修改指南", String.valueOf(Logenum.EDITINFO), request));
			return jsn;
		}catch(Exception e){
			jsn.put("result","1");
			jsn.put("message","修改失败");
			logDao.add(BuildModel.getModel( "N", "修改指南", String.valueOf(Logenum.EDITINFO), request));
			logger.info("修改指南:"+e);
			return jsn;
		}
	
	}
	/**
	 * 删除指南信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delguide")
	@ResponseBody
	public JSONObject delguide(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsonObj=new JSONObject();
		PubGuideDao pgd = sqlSession.getMapper(PubGuideDao.class);
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		String log_ids = request.getParameter("guideId");
		String[] idarry = log_ids.split(",");
		List list = Arrays.asList(idarry);
		try{
			pgd.delguide(list);
			jsonObj.put("result","0");
			jsonObj.put("message","删除记录成功");
			logDao.add(BuildModel.getModel( "Y", "删除指南", String.valueOf(Logenum.DELETEINFO), request));
			return jsonObj;
		}catch(Exception e){
			jsonObj.put("result","1");
			jsonObj.put("message","删除记录失败");
			logDao.add(BuildModel.getModel( "N", "删除指南", String.valueOf(Logenum.DELETEINFO), request));
			logger.info("删除指南:"+e);
			return jsonObj;
		}
		
	} 
	
	
	/**
	 * 查询指南详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/querycontent")
	@ResponseBody
	public JSONObject querycontent(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsonObj=new JSONObject();
		PubGuideDao pgd = sqlSession.getMapper(PubGuideDao.class);
		String id = request.getParameter("iId");
		PubGuideModel pm = new PubGuideModel();
		try{
			pm = pgd.querycontent(id);
			JSONObject jsonObject = JSONObject.fromObject(pm);
			jsonObj.put("guide",jsonObject);
			jsonObj.put("result","0");
			jsonObj.put("message","查询成功");
			return jsonObj;
		}catch(Exception e){
			jsonObj.put("result","1");
			jsonObj.put("message","查询失败"+e.getMessage());
			return jsonObj;
		}
		
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
}
	


