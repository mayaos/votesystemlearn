package com.eastcompeace.pubobjection;

import java.util.Arrays;
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
import com.eastcompeace.base.LogType.Logenum;
import com.eastcompeace.model.PubObjectionModel;
import com.eastcompeace.system.log.LogDao;
import com.eastcompeace.util.BuildModel;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/objectctrl")
public class PubObjectController extends BaseController {
	private Logger logger = Logger.getLogger("PubObjectController");
	@Resource
	private SqlSession sqlSession;
	/**
	 * 资讯信息表基础页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView codedictIndex() {
		return super.getModelView("common/pubobject/");
	}
	/**
	 * 查询回馈信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/objectlist")
	@ResponseBody
	public JSONObject queryGuide(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsn = new JSONObject();
		PubObjectDao pgd = sqlSession.getMapper(PubObjectDao.class);
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		PubObjectionModel pgm = new PubObjectionModel();
		pgm.setObjectionStatus(request.getParameter("isDone"));
		pgm.setBeginDate(request.getParameter("beginDate"));
		pgm.setEndDate(request.getParameter("endDate"));
		try {
			PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
			List<PubObjectionModel> objectlist = pgd.selobject(pgm);
			for(PubObjectionModel p : objectlist) {
				if(p.getObjectionPic1()!=null && !"".equals(p.getObjectionPic1())) {
					p.setObjectionPic1(ResourceUtils.getProperty("httpDownloadURL")+p.getObjectionPic1());
				}
				if(p.getObjectionPic2()!=null && !"".equals(p.getObjectionPic2())) {
					p.setObjectionPic2(ResourceUtils.getProperty("httpDownloadURL")+p.getObjectionPic2());
				}
				if(p.getObjectionPic3()!=null && !"".equals(p.getObjectionPic3())) {
					p.setObjectionPic3(ResourceUtils.getProperty("httpDownloadURL")+p.getObjectionPic3());
				}
				if(p.getObjectionPic4()!=null && !"".equals(p.getObjectionPic4())) {
					p.setObjectionPic4(ResourceUtils.getProperty("httpDownloadURL")+p.getObjectionPic4());
				}
			}
			PageInfo<PubObjectionModel> pages = new PageInfo(objectlist);
			jsn.put("total", pages.getTotal());
			jsn.put("rows",objectlist);
			logDao.add(BuildModel.getModel( "Y", "查询信息回馈分页", String.valueOf(Logenum.QUERYLIST), request));
		} catch (Exception e) {
			logDao.add(BuildModel.getModel( "N", "查询信息回馈分页", String.valueOf(Logenum.QUERYLIST), request));
			logger.info("查询信息回馈分页:"+e);
		}
		return jsn;
				
	}
	
	
	/**
	 * 删除回馈信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delobject")
	@ResponseBody
	public JSONObject delguide(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsonObj=new JSONObject();
		PubObjectDao pgd = sqlSession.getMapper(PubObjectDao.class);
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		String log_ids = request.getParameter("objectionId");
		String[] idarry = log_ids.split(",");
		List list = Arrays.asList(idarry);
		try{
			//获取需要删除的意见信息集合，用户获取意见照片Id
			List<PubObjectionModel> pubObjList = pgd.queryPubObjByList(list);
			//循环删除意见照片
			for(PubObjectionModel p : pubObjList) {
				if(p.getObjectionPic1()!=null && !"".equals(p.getObjectionPic1())) {
					deleteImg(p.getObjectionPic1());
				}
				if(p.getObjectionPic2()!=null && !"".equals(p.getObjectionPic2())) {
					deleteImg(p.getObjectionPic2());
				}
				if(p.getObjectionPic3()!=null && !"".equals(p.getObjectionPic3())) {
					deleteImg(p.getObjectionPic3());
				}
				if(p.getObjectionPic4()!=null && !"".equals(p.getObjectionPic4())) {
					deleteImg(p.getObjectionPic4());
				}
			}
			//批量删除意见信息
			pgd.delobject(list);
			jsonObj.put("result","0");
			jsonObj.put("message","删除记录成功");
			logDao.add(BuildModel.getModel( "Y", "删除信息回馈", String.valueOf(Logenum.DELETEINFO), request));
			return jsonObj;
		}catch(Exception e){
			jsonObj.put("result","1");
			jsonObj.put("message","删除记录失败");
			logDao.add(BuildModel.getModel( "N", "删除信息回馈", String.valueOf(Logenum.DELETEINFO), request));
			logger.error("删除信息回馈:"+e);
			return jsonObj;
		}
		
	} 
	/**
	 * 修改回馈信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editobject")
	@ResponseBody
	public JSONObject editobject(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsn=new JSONObject();
		PubObjectDao pgd = sqlSession.getMapper(PubObjectDao.class);
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		PubObjectionModel pjm = new PubObjectionModel();
		pjm.setObjectionId(Integer.parseInt(request.getParameter("objectionId")));
		pjm.setReplyContent(request.getParameter("replyContent"));
		pjm.setReplyTime(DateUtils.getNow());
		pjm.setObjectionStatus("2");
		try{
			pgd.updateobject(pjm);
			jsn.put("result","0");
			jsn.put("message","回馈成功");
			logDao.add(BuildModel.getModel( "Y", "回馈信息", String.valueOf(Logenum.REPLY), request));
			return jsn;
		}catch(Exception e){
			jsn.put("result","1");
			jsn.put("message","回馈失败");
			logDao.add(BuildModel.getModel( "N", "回馈信息", String.valueOf(Logenum.REPLY), request));
			logger.info("回馈信息:"+e);
			return jsn;
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
