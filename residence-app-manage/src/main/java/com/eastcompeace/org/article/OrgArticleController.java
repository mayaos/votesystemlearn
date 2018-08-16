package com.eastcompeace.org.article;

import java.io.File;
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
import com.eastcompeace.model.OrgArticleModel;
import com.eastcompeace.model.RightsInfoModel;
import com.eastcompeace.model.UserModel;
import com.eastcompeace.system.menu.MenuDao;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/**
 * 轻应用文章信息增删改查
 * @author xianzehua
 *
 */
@Controller
@RequestMapping("/orgArticlectrl")
public class OrgArticleController extends BaseController {
	private Logger logger = Logger.getLogger("OrgArticleController");
	@Resource
	private SqlSession sqlSession;
	@Resource
	private OrgArticleDao orgArticleDao;
	/**
	 * 指南信息表基础页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView codedictIndex() {
		return super.getModelView("common/orgarticle/");
	}
	/**
	 * 查询文章信息列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryInfo")
	@ResponseBody
	public JSONObject queryInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		JSONObject jsos = new JSONObject();
		OrgArticleDao rid = sqlSession.getMapper(OrgArticleDao.class);
		//获取参数
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		String titleSearch = request.getParameter("titleSearch");
		String authorSearch = request.getParameter("authorSearch");
		String issueOrNotSearch = request.getParameter("issueOrNotSearch");
		String issueDateSearch = request.getParameter("issueDateSearch");
		String areaIdSearch = request.getParameter("areaIdSearch");
		String menuIdSearch = request.getParameter("menuIdSearch");
		
		//实例化实体类
		OrgArticleModel model = new OrgArticleModel();
		model.setArticleTitle(titleSearch);
		model.setArticleAuthor(authorSearch);
		model.setIssueFlag(issueOrNotSearch);
		model.setIssueTime(issueDateSearch);
		model.setAreaId(areaIdSearch);
		model.setMenuId(menuIdSearch);
		
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
	
		List<OrgArticleModel> infoslist = rid.queryArticleInfo(model);
		for(OrgArticleModel r : infoslist) {
			r.setTopicPic(ResourceUtils.getProperty("httpDownloadURL")+r.getTopicPic());
		}
	
		PageInfo<OrgArticleModel> pages = new PageInfo(infoslist);
		
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
	@RequestMapping("/addArticleInfo")
	@ResponseBody
	public JSONObject addArticleInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsos = new JSONObject();
		OrgArticleDao orgArticleDao = sqlSession.getMapper(OrgArticleDao.class);
		OrgArticleModel model = new OrgArticleModel();
		// 建立多部分请求解析器
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest muRequest = resolver.resolveMultipart(request);
		//获取参数
		String articleId = muRequest.getParameter("articleId");
		String menuId = muRequest.getParameter("menuId");
		String orgId = muRequest.getParameter("orgId");
		String areaId = muRequest.getParameter("areaId");
		String articleTitle = muRequest.getParameter("articleTitle");
		String articleAuthor = muRequest.getParameter("articleAuthor");
		String articleFrom = muRequest.getParameter("articleFrom");
		String topicPic = muRequest.getParameter("topicPic");
		String articleDesc = muRequest.getParameter("articleDesc");
		String articleContent = muRequest.getParameter("articleContent");
		String issueFlag = muRequest.getParameter("issueFlag");
		String issueTime = muRequest.getParameter("issueTime1");
		//文章类型0为内容，1为链接
		String articleType = muRequest.getParameter("articleType");
		String articleLink = muRequest.getParameter("articleLink");
		//检查参数是否符合要求
//		if(articleId==null || !articleId.matches("\\d+")) {
//			jsos.put("result","1");
//			jsos.put("message","文章类别不符合要求，请重新选择！");
//			return jsos;
//		}
		if(menuId==null || !menuId.matches("\\d+")) {
			jsos.put("result","1");
			jsos.put("message","菜单名称不符合要求，请重新选择！");
			return jsos;
		}
		if(orgId==null || !orgId.matches("\\d+")) {
			jsos.put("result","1");
			jsos.put("message","机构名称不符合要求，请重新选择！");
			return jsos;
		}
		if(areaId==null || !areaId.matches("\\d+")) {
			jsos.put("result","1");
			jsos.put("message","区域名称不符合要求，请重新选择！");
			return jsos;
		}
		if(articleTitle==null || "".equals(articleTitle.trim())) {
			jsos.put("result","1");
			jsos.put("message","文章标题不允许为空，请重新输入！");
			return jsos;
		}
		if(articleAuthor==null || "".equals(articleAuthor.trim())) {
			jsos.put("result","1");
			jsos.put("message","文章作者不允许为空，请重新输入！");
			return jsos;
		}
		if(articleFrom==null || "".equals(articleFrom.trim())) {
			jsos.put("result","1");
			jsos.put("message","文章来源不允许为空，请重新输入！");
			return jsos;
		}
        if(articleType==null || "".equals(articleType.trim())) {
            jsos.put("result","1");
            jsos.put("message","文章选择不允许为空，请重新输入！");
            return jsos;
        }
        else if(articleType=="0" || "".equals(articleType.trim())) {
            if(articleContent==null || "".equals(articleContent.trim())) {
                jsos.put("result","1");
                jsos.put("message","文章内容不允许为空，请重新输入！");
                return jsos;
            }
        }
        else if(articleType=="1" || "".equals(articleType.trim())) {
            if(articleLink==null || "".equals(articleLink.trim())) {
                jsos.put("result","1");
                jsos.put("message","文章链接不允许为空，请重新输入！");
                return jsos;
            }
        }

		if(issueTime==null || "".equals(issueTime.trim())) {
			jsos.put("result","1");
			jsos.put("message","发表时间不允许为空，请重新选择！");
			return jsos;
		}
		model.setArticleId(articleId);
		model.setMenuId(menuId);
		model.setAreaId(areaId);
		model.setOrgId(orgId);
		model.setArticleTitle(articleTitle);
		model.setArticleAuthor(articleAuthor);
		model.setArticleFrom(articleFrom);
		model.setTopicPic(topicPic);
		model.setArticleDesc(articleDesc);
		model.setArticleType(articleType);
		model.setArticleLink(articleLink);
		model.setArticleContent(articleContent);
		model.setIssueFlag(issueFlag);
		model.setIssueTime(issueTime);
		//获取用户id
		UserModel user = new UserModel();
		HttpSession session = request.getSession();
		user  = (UserModel) session.getAttribute(Constant.SESSION_USER);
		model.setUserId(user.getUserId());
		// 获取上传图片文件
		MultipartFile titleImage = muRequest.getFile("topicPic");
		String imgId = null; //图片Id
		try {
			imgId = uploadImage(titleImage ,"", "") ;
		} catch (Exception e) {
			logger.error("图片上传失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","图片上传失败，请稍后再试！");
			return jsos;
		}
		//将标题图片绝对路径存入Model
		model.setTopicPic(imgId);
		//连接数据库保存数据				
		try {
			orgArticleDao.addArticleInfo(model);
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
	 * 获取文章文章内容
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
		//创建文章信息类
		OrgArticleModel model = new OrgArticleModel();
		
		try{
			model = orgArticleDao.queryArticleContent(articleId);
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
	 * 获取详细文章内容
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryArticleDetailInfo")
	@ResponseBody
	public JSONObject queryArticleDetailInfo(HttpServletRequest request) throws Exception{
		JSONObject jsonObj=new JSONObject();
		
		//获取文章Id
		String articleId = request.getParameter("articleId");
		//创建文章信息类
		OrgArticleModel model = new OrgArticleModel();
		
		try{
			model = orgArticleDao.queryArticleDetailInfo(articleId);
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
	 * 修改文章详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateArticleInfo")
	@ResponseBody
	public JSONObject updateArticleInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsos = new JSONObject();
		OrgArticleDao orgArticleDao = sqlSession.getMapper(OrgArticleDao.class);
		OrgArticleModel model = new OrgArticleModel();
		// 建立多部分请求解析器
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest muRequest = resolver.resolveMultipart(request);
		//获取参数
		String articleId = muRequest.getParameter("articleId2");
		String menuId = muRequest.getParameter("menuId2");
		String orgId = muRequest.getParameter("orgId2");
		String areaId = muRequest.getParameter("areaIdHidden");
		String articleTitle = muRequest.getParameter("articleTitle2");
		String articleAuthor = muRequest.getParameter("articleAuthor2");
		String articleFrom = muRequest.getParameter("articleFrom2");
		String topicPic = muRequest.getParameter("topicPic2");
		String articleDesc = muRequest.getParameter("articleDesc2");
		String articleContent = muRequest.getParameter("articleContent");
		String issueFlag = muRequest.getParameter("issueFlag2");
		String issueTime = muRequest.getParameter("issueTime2");
        //文章类型0为内容，1为链接
        String articleType = muRequest.getParameter("articleType2");
        String articleLink = muRequest.getParameter("articleLink2");		
		//检查参数是否有值，若有值则加入model
		if(articleId==null || !articleId.matches("\\d+")) {
			jsos.put("result","1");
			jsos.put("message","无法获取文章Id，请重新选择需要修改的数据");
			return jsos;
		}
		if(articleId!=null && articleId.matches("\\d+")) {
			model.setArticleId(articleId);
		}
		if(menuId!=null && menuId.matches("\\d+")) {
			model.setMenuId(menuId);
		}
		
		if(menuId==null || !menuId.matches("\\d+")) {
			jsos.put("result","1");
			jsos.put("message","机构名称不符合要求，请重新选择！");
			return jsos;
		}
		if(orgId==null || !orgId.matches("\\d+")) {
			jsos.put("result","1");
			jsos.put("message","机构名称不符合要求，请重新选择！");
			return jsos;
		}
//		if(areaId==null || !areaId.matches("\\d+")) {
//			jsos.put("result","1");
//			jsos.put("message","区域名称不符合要求，请重新选择！");
//			return jsos;
//		}
		if(articleTitle==null || "".equals(articleTitle.trim())) {
			jsos.put("result","1");
			jsos.put("message","文章标题不允许为空，请重新输入！");
			return jsos;
		}
		if(articleAuthor==null || "".equals(articleAuthor.trim())) {
			jsos.put("result","1");
			jsos.put("message","文章作者不允许为空，请重新输入！");
			return jsos;
		}
		if(articleFrom==null || "".equals(articleFrom.trim())) {
			jsos.put("result","1");
			jsos.put("message","文章来源不允许为空，请重新输入！");
			return jsos;
		}
        if(articleType==null || "".equals(articleType.trim())) {
            jsos.put("result","1");
            jsos.put("message","文章选择不允许为空，请重新输入！");
            return jsos;
        }
        else if(articleType=="0" || "".equals(articleType.trim())) {
            if(articleContent==null || "".equals(articleContent.trim())) {
                jsos.put("result","1");
                jsos.put("message","文章内容不允许为空，请重新输入！");
                return jsos;
            }
        }
        else if(articleType=="1" || "".equals(articleType.trim())) {
            if(articleLink==null || "".equals(articleLink.trim())) {
                jsos.put("result","1");
                jsos.put("message","文章链接不允许为空，请重新输入！");
                return jsos;
            }
        }
		if(issueTime==null || "".equals(issueTime.trim())) {
			jsos.put("result","1");
			jsos.put("message","发表时间不允许为空，请重新选择！");
			return jsos;
		}
		
		
		if(articleAuthor!=null && !"".equals(articleAuthor.trim())) {
			model.setArticleAuthor(articleAuthor);
		}		
		model.setArticleId(articleId);
		model.setMenuId(menuId);
		model.setAreaId(areaId);
		model.setOrgId(orgId);
		model.setArticleTitle(articleTitle);
		model.setArticleAuthor(articleAuthor);
		model.setArticleFrom(articleFrom);
		model.setTopicPic(topicPic);
		model.setArticleDesc(articleDesc);
		model.setArticleContent(articleContent);
		model.setIssueFlag(issueFlag);
		model.setIssueTime(issueTime);
        model.setArticleLink(articleLink);
        model.setArticleType(articleType);		
		//获取用户id
		UserModel user = new UserModel();
		HttpSession session = request.getSession();
		user  = (UserModel) session.getAttribute(Constant.SESSION_USER);
		model.setUserId(user.getUserId());
		// 获取上传图片文件
		MultipartFile titleImage = muRequest.getFile("topicPic2");
		//判断图片文件是否存在，若存在则执行图片文件处理内容
		if(titleImage!=null && !titleImage.isEmpty()) {
			String imgId = "";
			try {
				//链接数据库获取旧图片Id
				List<OrgArticleModel> list = orgArticleDao.queryArticleInfo(model);
				//由于这里list只有一个元素所以直接获取图片Id
				String oldImgId = "";
				for(OrgArticleModel o : list) {
					oldImgId = o.getTopicPic();
				}
				//上传图片到服务器，并删除旧图片
				imgId = uploadImage(titleImage,oldImgId, "");
			} catch (Exception e) {
				logger.error("更新图片失败:" + e.getMessage());
				jsos.put("result","1");
				jsos.put("message","更新图片失败，请稍后再试！");
				return jsos;
			}
			
			model.setTopicPic(imgId);
		}
		//连接数据库保存数据				
		try {
			orgArticleDao.updateArticleInfo(model);
		} catch (Exception e) {
			logger.error("保存信息失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","信息修改失败，请稍后再试！");
			return jsos;
		}
		jsos.put("result","0");
		jsos.put("message","修改成功");
		
		return jsos;
	}
	/**
	 * 删除文章详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delArticleInfo")
	@ResponseBody
	public JSONObject delArticleInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
	
		JSONObject jsos = new JSONObject();
		OrgArticleDao orgArticleDao = sqlSession.getMapper(OrgArticleDao.class);
		//获取需要删除的记录articlId
		String articleIds = request.getParameter("articleId");

		//把articlId字符串拆分为数组，数据转换为List
		String[] idArr = articleIds.split(",");
		List list = Arrays.asList(idArr);
		
		//定义文章信息实例
		List<OrgArticleModel> rightsList = new ArrayList<OrgArticleModel>();
			
		try{
//			//查询数据库批量获取文章信息
			rightsList = orgArticleDao.queryInfoByList(list);
			//批量删除数据
			orgArticleDao.delArticleInfo(list);
		}catch(Exception e){
			logger.error("删除记录失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","删除记录失败，请稍后再试！");
			return jsos;
		}
		
		//获取文章标题图片Id，删除图片
		for(OrgArticleModel r : rightsList) {
			try {
				deleteImg(r.getTopicPic());
			} catch (Exception e) {
				logger.error("删除图片失败:" + e.getMessage());
				jsos.put("result","1");
				jsos.put("message","删除图片失败！");
				return jsos;
			}
		}
		
		jsos.put("result","0");
		jsos.put("message","删除记录成功");
		return jsos;
		
	}
	
	/**
	 * 发布文章详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/releaseArticleInfo")
	@ResponseBody
	public JSONObject releaseArticleInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
	
		JSONObject jsos = new JSONObject();
		OrgArticleDao orgArticleDao = sqlSession.getMapper(OrgArticleDao.class);
		//获取需要发布的记录articlId
		String articleIds = request.getParameter("articleId");

		//把articlId字符串拆分为数组，数据转换为List
		String[] idArr = articleIds.split(",");
		List list = Arrays.asList(idArr);
		
		//定义文章信息实例
		List<OrgArticleModel> rightsList = new ArrayList<OrgArticleModel>();
			
		try{
			//批量发布数据
			orgArticleDao.releaseArticleInfo(list);
		}catch(Exception e){
			logger.error("发布文章失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","发布文章失败，请稍后再试！");
			return jsos;
		}
		
		jsos.put("result","0");
		jsos.put("message","发布文章成功");
		return jsos;
		
	}
	/**
	 * 取消发布文章
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delreleaseArticleInfo")
	@ResponseBody
	public JSONObject delreleaseArticleInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
	
		JSONObject jsos = new JSONObject();
		OrgArticleDao orgArticleDao = sqlSession.getMapper(OrgArticleDao.class);
		//获取需要发布的记录articlId
		String articleIds = request.getParameter("articleId");

		//把articlId字符串拆分为数组，数据转换为List
		String[] idArr = articleIds.split(",");
		List list = Arrays.asList(idArr);
		
		//定义文章信息实例
		List<OrgArticleModel> rightsList = new ArrayList<OrgArticleModel>();
			
		try{
			//批量发布数据
			orgArticleDao.delreleaseArticleInfo(list);
		}catch(Exception e){
			logger.error("取消发布文章失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","取消发布文章失败，请稍后再试！");
			return jsos;
		}
		
		jsos.put("result","0");
		jsos.put("message","取消发布文章成功");
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
	public JSONObject getToolbar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.getToolbar(request, sqlSession);
	}
	
	public static void main(String[] args) {
		String TitleImage = "rightsImg20170214\\button-bg-6.png";
		System.out.println("\\"+File.separator);
		String folderUrl = ResourceUtils.getProperty("rightsTitileImgUrl") + File.separator + TitleImage.split("\\"+File.separator)[0];
		System.out.println("folderUrl:"+folderUrl);
	}
}
