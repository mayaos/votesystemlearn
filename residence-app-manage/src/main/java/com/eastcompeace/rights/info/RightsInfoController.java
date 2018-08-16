package com.eastcompeace.rights.info;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.Constant;
import com.eastcompeace.model.ButtonModel;
import com.eastcompeace.model.MenuModel;
import com.eastcompeace.model.PubGuideModel;
import com.eastcompeace.model.RightsInfoModel;
import com.eastcompeace.model.UserModel;
import com.eastcompeace.pubguide.PubGuideDao;
import com.eastcompeace.share.cipher.Base64Cipher;
import com.eastcompeace.system.menu.MenuDao;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/**
 * 权益信息增删改查
 * @author cui
 *
 */
@Controller
@RequestMapping("/rightsInfoctrl")
public class RightsInfoController extends BaseController {
	private Logger logger = Logger.getLogger("RightsInfoController");
	@Resource
	private SqlSession sqlSession;
	@Resource
	private RightsInfoDao rightsInfoDao;
	/**
	 * 指南信息表基础页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView codedictIndex() {
		return super.getModelView("common/rightsinfo/");
	}
	/**
	 * 查询权益信息列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryInfo")
	@ResponseBody
	public JSONObject queryInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		JSONObject jsos = new JSONObject();
		RightsInfoDao rid = sqlSession.getMapper(RightsInfoDao.class);
		//获取参数
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		String titleSearch = request.getParameter("titleSearch");
		String rightsTypeSearch = request.getParameter("rightsTypeSearch");
		String uploadDateStart = request.getParameter("uploadDateStart");
		String uploadDateEnd = request.getParameter("uploadDateEnd");
		
		//实例化实体类
		RightsInfoModel rightsInfo = new RightsInfoModel();
		rightsInfo.setRightsTitle(titleSearch);
		rightsInfo.setRightsTypeName(rightsTypeSearch);
		rightsInfo.setUploadDateStart(uploadDateStart);
		rightsInfo.setUploadDateEnd(uploadDateEnd);
		
		
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
	
		List<RightsInfoModel> infoslist = rid.queryInfo(rightsInfo);
		for(RightsInfoModel r : infoslist) {
			r.setTitleImage(ResourceUtils.getProperty("httpDownloadURL")+r.getTitleImage());
		}
	
		PageInfo<RightsInfoModel> pages = new PageInfo(infoslist);
		
		jsos.put("total", pages.getTotal());
		jsos.put("rows",infoslist);
		return jsos;
	} 
	/**
	 * 添加详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addRightsInfo")
	@ResponseBody
	public JSONObject addRightsInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsos = new JSONObject();
		RightsInfoDao rightsInfoDao = sqlSession.getMapper(RightsInfoDao.class);
		RightsInfoModel model = new RightsInfoModel();
		// 建立多部分请求解析器
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest muRequest = resolver.resolveMultipart(request);
		//获取参数
		String rightsId = muRequest.getParameter("rightsType1");
		String areaId = muRequest.getParameter("areaId1");
		String rightsFrom = muRequest.getParameter("rightsFrom1");
		String rightsTitle = muRequest.getParameter("rightsTitle1");
		String rightsContent = muRequest.getParameter("rightsContent");
		String issueTime = muRequest.getParameter("issueTime1");
		//检查参数是否符合要求
		if(rightsId==null || !rightsId.matches("\\d+")) {
			jsos.put("result","1");
			jsos.put("message","权益类别不符合要求，请重新选择！");
			return jsos;
		}
		if(areaId==null || !areaId.matches("\\d+")) {
			jsos.put("result","1");
			jsos.put("message","区域不符合要求，请重新选择！");
			return jsos;
		}
		if(rightsFrom==null || "".equals(rightsFrom.trim())) {
			jsos.put("result","1");
			jsos.put("message","文章来源不允许为空，请重新输入！");
			return jsos;
		}
		if(rightsTitle==null || "".equals(rightsTitle.trim())) {
			jsos.put("result","1");
			jsos.put("message","文章标题不允许为空，请重新输入！");
			return jsos;
		}
		if(rightsContent==null || "".equals(rightsContent.trim())) {
			jsos.put("result","1");
			jsos.put("message","文章内容不允许为空，请重新输入！");
			return jsos;
		}
		if(issueTime==null || "".equals(issueTime.trim())) {
			jsos.put("result","1");
			jsos.put("message","发表时间不允许为空，请重新选择！");
			return jsos;
		}
		//把参数存入实例model中
		model.setRightsId(rightsId);
		model.setAreaId(areaId);
		model.setRightsFrom(rightsFrom);
		model.setRightsTitle(rightsTitle);
		model.setRightsContent(rightsContent);
		model.setIssueTime(issueTime);
		//获取用户id
		UserModel user = new UserModel();
		HttpSession session = request.getSession();
		user  = (UserModel) session.getAttribute(Constant.SESSION_USER);
		model.setUserId(user.getUserId());
		// 获取上传图片文件
		MultipartFile titleImage = muRequest.getFile("logo1");
		
		//上传文件
		//定义文件Id,通过文件Id可以从服务器获取文件
		String imgId = null;
		try {
			imgId = uploadImage(titleImage,"", "");
		} catch (Exception e) {
			logger.error("文件上传失败", e);
			jsos.put("result", 1);
			jsos.put("message", "文件上传失败");
			return jsos;
		} 
			
		//将标题图片Id存入Model
		model.setTitleImage(imgId);
	
		//连接数据库保存数据				
		try {
			rightsInfoDao.addRightsInfo(model);
		} catch (Exception e) {
			logger.error("保存信息失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","信息添加失败，请稍后再试！");
			return jsos;
		}
		jsos.put("result","0");
		jsos.put("message","添加成功");
		
		return jsos;
	}
	
	/**
	 * 获取权益文章内容
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/querycontent")
	@ResponseBody
	public JSONObject querycontent(HttpServletRequest request) throws Exception{
		JSONObject jsonObj=new JSONObject();
		
		//获取文章Id
		String articleId = request.getParameter("articleId");
		//创建权益信息类
		RightsInfoModel model = new RightsInfoModel();
		
		try{
			model = rightsInfoDao.querycontent(articleId);
		}catch(Exception e){
			logger.error("查询失败:" + e.getMessage());
			jsonObj.put("result","1");
			jsonObj.put("message","查询文章详情失败，请稍后再试！");
			return jsonObj;
		}
		JSONObject jsonObject = JSONObject.fromObject(model);
		jsonObj.put("article",jsonObject);
		jsonObj.put("result","0");
		jsonObj.put("message","查询成功");
		return jsonObj;
	} 
	
	
	
	
	/**
	 * 修改权益详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateRightsInfo")
	@ResponseBody
	public JSONObject updateRightsInfo(HttpServletRequest request,HttpServletResponse response) {
		JSONObject jsos = new JSONObject();
		RightsInfoDao rightsInfoDao = sqlSession.getMapper(RightsInfoDao.class);
		RightsInfoModel model = new RightsInfoModel();
		// 建立多部分请求解析器
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest muRequest = resolver.resolveMultipart(request);
		//获取参数
		String articleId = muRequest.getParameter("articleId2");
		String rightsId = muRequest.getParameter("rightsType2");
		String areaId = muRequest.getParameter("areaId2");
		String rightsFrom = muRequest.getParameter("rightsFrom2");
		String rightsTitle = muRequest.getParameter("rightsTitle2");
		String rightsContent = muRequest.getParameter("rightsContent");
		String issueTime = muRequest.getParameter("issueTime2");
		//检查参数是否有值，若有值则加入model
		if(articleId==null || !articleId.matches("\\d+")) {
			jsos.put("result","1");
			jsos.put("message","无法获取文章Id，请重新选择需要修改的数据");
			return jsos;
		}
		if(rightsId!=null && rightsId.matches("\\d+")) {
			model.setRightsId(rightsId);
		}
		if(areaId!=null && areaId.matches("\\d+")) {
			model.setAreaId(areaId);
		}
		if(rightsFrom==null || "".equals(rightsFrom.trim())) {
			jsos.put("result","1");
			jsos.put("message","文章来源不允许为空，请重新输入！");
			return jsos;
		}
		if(rightsTitle==null || "".equals(rightsTitle.trim())) {
			jsos.put("result","1");
			jsos.put("message","文章标题不允许为空，请重新输入！");
			return jsos;
		}
		if(rightsContent==null || "".equals(rightsContent.trim())) {
			jsos.put("result","1");
			jsos.put("message","文章内容不允许为空，请重新输入！");
			return jsos;
		}
		if(issueTime!=null && !"".equals(issueTime.trim())) {
			model.setIssueTime(issueTime);
		}		
		model.setArticleId(articleId);
		model.setRightsFrom(rightsFrom);
		model.setRightsTitle(rightsTitle);
		model.setRightsContent(rightsContent);
		
		//获取用户id
		UserModel user = new UserModel();
		HttpSession session = request.getSession();
		user  = (UserModel) session.getAttribute(Constant.SESSION_USER);
		model.setUserId(user.getUserId());
		// 获取上传图片文件
		MultipartFile titleImage = muRequest.getFile("logo2");
		//判断图片文件是否存在，若存在则执行图片文件处理内容
		if(titleImage!=null && !titleImage.isEmpty()) {
			try {
				//查询数据库获取旧图片id
				List<RightsInfoModel> list= rightsInfoDao.queryInfo(model);
				//由于这里查询出来的集合只有1个元素，所以直接获取图片ID
				String oldImgId = "";
				for(RightsInfoModel r : list) {
					oldImgId = r.getTitleImage();
				}
				//上传新的图片到服务器，并删除旧的图片
				String imgId = uploadImage(titleImage, oldImgId, "");			
				//将标题图片绝对路径存入Model
				model.setTitleImage(imgId);
			} catch (Exception e) {
				logger.error("更新图片失败:" + e.getMessage());
				jsos.put("result","1");
				jsos.put("message","更新图片信息，请稍后再试！");
				return jsos;
			}
			
		}
		//连接数据库保存数据				
		try {
			rightsInfoDao.updateRightsInfo(model);
		} catch (Exception e) {
			logger.error("保存信息失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","信息添加失败，请稍后再试！");
			return jsos;
		}
		jsos.put("result","0");
		jsos.put("message","添加成功");
		
		return jsos;
	}
	/**
	 * 删除权益详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delRightsInfo")
	@ResponseBody
	public JSONObject delRightsInfo(HttpServletRequest request,HttpServletResponse response) {
	
		JSONObject jsos = new JSONObject();
		RightsInfoDao rightsInfoDao = sqlSession.getMapper(RightsInfoDao.class);
		//获取需要删除的记录articlId
		String articleIds = request.getParameter("articleId");

		//把articlId字符串拆分为数组，数据转换为List
		String[] idArr = articleIds.split(",");
		List list = Arrays.asList(idArr);
		
		//定义权益信息实例
		List<RightsInfoModel> rightsList = new ArrayList<RightsInfoModel>();
			
		try{
			//查询数据库批量获取权益信息
			rightsList = rightsInfoDao.queryInfoByList(list);
			//批量删除数据
			rightsInfoDao.delRightsInfo(list);
		}catch(Exception e){
			logger.error("删除记录失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","删除记录失败，请稍后再试！");
			return jsos;
		}
		
		//获取文章标题图片路径，删除图片,如果保存文件的文件夹为空则删除文件夹
		for(RightsInfoModel r : rightsList) {
			try {
				deleteImg(r.getTitleImage());
			} catch (Exception e) {
				logger.error("删除图片文件失败:" + e.getMessage());
				jsos.put("result","1");
				jsos.put("message","删除图片文件失败！");
			}
		}
		
		jsos.put("result","0");
		jsos.put("message","删除记录成功");
		return jsos;
		
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
	
	public static void main(String[] args) {
		String TitleImage = "rightsImg20170214\\button-bg-6.png";
		System.out.println("\\"+File.separator);
		String folderUrl = ResourceUtils.getProperty("rightsTitileImgUrl") + File.separator + TitleImage.split("\\"+File.separator)[0];
		System.out.println("folderUrl:"+folderUrl);
	}
}
