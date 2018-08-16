package com.eastcompeace.rights.menu;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
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
import com.eastcompeace.model.RightsMenuModel;
import com.eastcompeace.share.cipher.Base64Cipher;
import com.eastcompeace.system.menu.MenuDao;
import com.eastcompeace.util.ImageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/**
 * 权益菜单表（增删改查操作）
 * @author cui
 *
 */
@Controller
@RequestMapping("/rithtsMenuctrl")
public class RightsMenuController extends BaseController {
	private Logger logger = Logger.getLogger("RightsMenuController");
	@Resource
	private SqlSession sqlSession;
	/**
	 * 权益菜单表基础页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView codedictIndex() {
		return super.getModelView("common/rights/");
	}
	/**
	 * 查询权益菜单列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/querylist")
	@ResponseBody
	public JSONObject queryMenu(HttpServletRequest request,HttpServletResponse response) {
		JSONObject jsoj = new JSONObject();
		RightsMenuDao rmd = sqlSession.getMapper(RightsMenuDao.class);
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		
		RightsMenuModel model = new RightsMenuModel();
		model.setRightsName(request.getParameter("rightsName"));
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<RightsMenuModel> menulist;
		try {
			menulist = rmd.queryMenu(model);
			PageInfo<RightsMenuModel> pages = new PageInfo<RightsMenuModel>(menulist);
			jsoj.put("total", pages.getTotal());
			jsoj.put("rows",menulist);
		} catch (Exception e) {
			logger.error("数据库异常，获取权益菜单列表信息失败:" + e.getMessage());
			return null;
		}
		
		
		
		return jsoj;
	}
	/**
	 * 添加权益信息菜单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addMenu")
	@ResponseBody
	public JSONObject addMenu(HttpServletRequest request,HttpServletResponse response) {
		JSONObject jsos = new JSONObject();
		RightsMenuDao rmd = sqlSession.getMapper(RightsMenuDao.class);
		RightsMenuModel model = new RightsMenuModel();
		// 建立多部分请求解析器
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest muRequest = resolver.resolveMultipart(request);
		//获取参数
		model.setRightsName(muRequest.getParameter("rightsName"));
		model.setRightsOrder(Integer.parseInt(muRequest.getParameter("rightsOrder")));
		model.setRightsStatus(muRequest.getParameter("rightsStatus"));
			
		// 获取上传文件
		MultipartFile logoFile = muRequest.getFile("rightsLogo");
		//获取上传文件字节数组，转为base64然后存入model实例中
		byte[] bytes;
		try {
			bytes = logoFile.getBytes();
			model.setRightsLogo(new String(Base64Cipher.encode(bytes)));
		} catch (IOException e1) {
			logger.error("图片文件转换成base64失败:" + e1.getMessage());
			jsos.put("result","1");
			jsos.put("message","图片文件上传失败，请上传正确图片文件！");
			return jsos;
		}
		
		try {
			rmd.addMenu(model);
		} catch (Exception e) {
			logger.error("数据库连接失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","添加失败"+e.getMessage());
			return jsos;
		}
		jsos.put("result","0");
		jsos.put("message","添加成功");
		
		return jsos;
	}
	/**
	 * 修改权益信息菜单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateMenu")
	@ResponseBody
	public JSONObject updateMenu(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		JSONObject jsos = new JSONObject();
		
		RightsMenuDao rmd = sqlSession.getMapper(RightsMenuDao.class);
		RightsMenuModel model = new RightsMenuModel();
		
		// 建立多部分请求解析器
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest muRequest = resolver.resolveMultipart(request);
		//获取参数
		model.setRightsName(muRequest.getParameter("rightsName2"));
		model.setRightsOrder(Integer.parseInt(muRequest.getParameter("rightsOrder2")));
		model.setRightsStatus(muRequest.getParameter("rightsStatus2"));
		model.setRightsId(Integer.parseInt(muRequest.getParameter("rightsId2")));
		
		// 获取上传文件
		MultipartFile logoFile = muRequest.getFile("rightsLogo2");
		//判断上传文件是否为空，如果不为空则转换成base64存入model
		if(logoFile!=null && !logoFile.isEmpty()) {
			byte[] bytes;
			try {
				bytes = logoFile.getBytes();
				model.setRightsLogo(new String(Base64Cipher.encode(bytes)));
			} catch (IOException e1) {
				logger.error("图片文件转换成base64失败:" + e1.getMessage());
				jsos.put("result","1");
				jsos.put("message","图片文件上传失败，请上传正确图片文件！");
				return jsos;
			}
		}
		
		try {
			rmd.updateMenu(model);
		} catch (Exception e) {
			logger.error("数据库连接失败:" + e.getMessage());
			jsos.put("result", "1");
			jsos.put("message","数据库连接失败，请稍后再试");
		}
		jsos.put("result","0");
		jsos.put("message","修改成功");
		return jsos;
	}
	/**
	 * 删除权益信息菜单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delMenu")
	@ResponseBody
	public JSONObject delMenu(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsos = new JSONObject();
		RightsMenuDao rmd = sqlSession.getMapper(RightsMenuDao.class);
		
		String log_ids = request.getParameter("rightsId");
		String[] idarry = log_ids.split(",");
		List<String> list = Arrays.asList(idarry);
		try {
			rmd.delMenu(list);
		} catch (Exception e) {
			jsos.put("result","1");
			jsos.put("message","删除记录失败"+e.getMessage());
			return jsos;
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
	
	/**
	 * 地区树
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/addrTreelist")
	@ResponseBody
	public JSONArray getMenuTreeList(HttpServletRequest request, HttpServletResponse response) {
		JSONArray jsArr = null;
		RightsMenuDao rmd = sqlSession.getMapper(RightsMenuDao.class);
			
		RightsMenuModel model = new RightsMenuModel();

		List<RightsMenuModel> menulist;
		try {
			menulist = rmd.queryMenu(model);
			jsArr = JSONArray.fromObject(menulist);
		} catch (Exception e) {
			logger.error("数据库异常，获取权益菜单列表信息失败:" + e.getMessage());
			return null;
		}
		
		return jsArr;
	}

}
