package com.eastcompeace.org.orgmenu;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.Constant;
import com.eastcompeace.config.area.AreaDao;
import com.eastcompeace.model.ButtonModel;
import com.eastcompeace.model.MenuModel;
import com.eastcompeace.model.OrgMenuModel;
import com.eastcompeace.share.cipher.Base64Cipher;
import com.eastcompeace.system.menu.MenuDao;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 
 * @FileName OrgMenuController.java
 * @Date   2018年6月15日上午11:26:13
 * @author wzh
 *
 * 轻应用菜单管理控制器
 *
 */
@Controller
@RequestMapping("/orgMenuctrl")
public class OrgMenuController extends BaseController{
	private Logger logger = Logger.getLogger("OrgInfoController");
	@Resource
	private SqlSession sqlSession;
	@Resource
	private OrgMenuDao orgMenuDao;
	@Resource
	private AreaDao areaDao;
	
	/**
	 * 主页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView orgMenuIndex() {
		return super.getModelView("common/orgmenu/");
	}
	
	/**
	 * 根据权限生成toolbar工具菜单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getToolbar")
	@ResponseBody
	public JSONObject getToolbar(HttpServletRequest request,HttpServletResponse response) throws Exception {
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
	 * 获取菜单列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/selectMenuList")
	@ResponseBody
	@Transactional
	public JSONObject selectMenuList(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//实例化返回对象
		JSONObject josj = new JSONObject();
		//实例化列表信息
		List<OrgMenuModel> menuList = null;
		//获取参数
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		String menuStatus = request.getParameter("menuStatus");
		String menuTypeNameSearch = request.getParameter("menuTypeNameSearch");
		String menuNameSearch = request.getParameter("menuNameSearch");
		//获取配置文件中的图片下载路径
		String folder = ResourceUtils.getProperty("httpDownloadURL");	
			
		//实例化轻应用菜单实例，并赋值
		OrgMenuModel model = new OrgMenuModel();
		model.setMenuStatus(menuStatus);
		model.setMenuTypeName(menuTypeNameSearch);
		model.setMenuName(menuNameSearch);
		
		//查询数据库获取轻应用菜单列表
		try {
			PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
			menuList = orgMenuDao.queryMenuList(model);
			for(OrgMenuModel o : menuList) {
			    //菜单描述不用显示全，多余的字用...代替
			    if(o.getMenuDesc()!=null){
	                if(o.getMenuDesc().length()>8){
	                    o.setMenuDesc(o.getMenuDesc().substring(0,7)+"...");
	                }			        
			    }
				//根据区域代码获取区域名，针对区域代码大于两个的情况
				if(o.getAreaId().length()!=6){
					String[] areaIds= o.getAreaId().split(",");
					String areaNames="";
					
					for(String areaId : areaIds ){
						areaNames=areaNames+","+areaDao.queryAreaName(areaId);
					}
					o.setAreaName(areaNames.substring(1));
				}
				o.setBackgroundImg(folder+o.getBackgroundImg());
			}
			PageInfo<OrgMenuModel> pages = new PageInfo<OrgMenuModel>(menuList);
			josj.put("total", pages.getTotal());
			josj.put("rows",menuList);
		} catch (Exception e) {
			logger.error("查询失败:" + e.getMessage());
			return null;
		}	
//		System.out.println(josj);
		return josj;
	}
	
	/**
	 * 添加机构菜单信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addMenu")
	@ResponseBody
	@Transactional
	public JSONObject addMenu(HttpServletRequest request,HttpServletResponse response) {
		//实例化返回结果对象
		JSONObject jsos = new JSONObject();
		//实例化传入数据库的机构信息类
		OrgMenuModel model = new OrgMenuModel();
		// 建立多部分请求解析器
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest muRequest = resolver.resolveMultipart(request);
		//获取字符串参数
		String menuName = muRequest.getParameter("menuName1");
		String menuType = muRequest.getParameter("menuType1");
		String areaId = muRequest.getParameter("areaId");
		String menuUrl = muRequest.getParameter("menuUrl1");
		String menuOrder = muRequest.getParameter("menuOrder1");
		String menuStatus = muRequest.getParameter("menuStatus1");
		String isDefault = muRequest.getParameter("isDefault1");
		String menuDesc =  muRequest.getParameter("menuDesc");
		// 获取上传图片文件
		MultipartFile orgLogo = muRequest.getFile("menuLogo1");
		MultipartFile backgroundImg = muRequest.getFile("backgroundImg1");
		
		//检查字符串参数
		if(menuName==null || "".equals(menuName) || menuName.length()>1024) {
			jsos.put("result","1");
			jsos.put("message","菜单名称不正确，请重新输入！！");
			return jsos;
		}
		if(menuType==null || "".equals(menuType)) {
			jsos.put("result","1");
			jsos.put("message","请选择菜单类型！");
			return jsos;
		}
		if(areaId==null || !areaId.matches("\\d+")) {
			jsos.put("result","1");
			jsos.put("message","区域名称不符合要求，请重新选择！");
			return jsos;
		}		
		if(menuUrl == null || menuUrl.length()>1024) {
			jsos.put("result","1");
			jsos.put("message","菜单链接不正确，请重新输入！");
			return jsos;
		}
		if(menuOrder ==null || !menuOrder.matches("\\d{1,2}")) {
			jsos.put("result","1");
			jsos.put("message","菜单排序不正确，请重新输入！");
			return jsos;
		}
		if(menuStatus==null || "".equals(menuStatus)) {
			jsos.put("result","1");
			jsos.put("message","请选择状态！");
			return jsos;
		}
		if(isDefault==null || "".equals(isDefault)) {
			jsos.put("result","1");
			jsos.put("message","请选择是否默认为首页菜单！");
			return jsos;
		}
		
		//给model成员变量赋值
		model.setIsDefault(isDefault);
		model.setMenuName(menuName);
		model.setMenuOrder(menuOrder);
		model.setAreaId(areaId);
		model.setMenuStatus(menuStatus);
		model.setMenuType(menuType);
		model.setMenuUrl(menuUrl);
		model.setMenuDesc(menuDesc);
		//获取上传orgLogo文件字节数组，转为base64然后存入model实例中
		if(orgLogo==null || "".equals(orgLogo)) {
			jsos.put("result","1");
			jsos.put("message","请上传菜单LOGO！");
			return jsos;
		}
		byte[] bytes;
		try {
			bytes = orgLogo.getBytes();
			model.setMenuLogo(new String(Base64Cipher.encode(bytes)));
		} catch (IOException e1) {
			logger.error("菜单LOGO图片文件转换成base64失败:" + e1.getMessage());
			jsos.put("result","1");
			jsos.put("message","菜单LOGO图片文件上传失败，请上传正确图片文件！");
			return jsos;
		}
		
		//将backgroundImg,orgLogo图片文件上传图片服务器然后把uri保存到model实例中	
		try {
			model.setBackgroundImg(uploadImage(backgroundImg,"",""));
			model.setMenuLogoUrl(uploadImage(orgLogo,"", ""));
		} catch (Exception e) {
			logger.error("图片上传失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","图片上传失败");
			return jsos;
		}
		
		//连接数据库添加机构菜单信息
		try {
			orgMenuDao.addMenu(model);
		} catch (Exception e) {
			logger.error("数据库访问失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","数据库异常，信息添加失败");
			return jsos;
		}
		
		jsos.put("result","0");
		jsos.put("message","添加成功");
		
		return jsos;
	}
	/**
	 * 修改菜单信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateMenu")
	@ResponseBody
	@Transactional
	public JSONObject updateMenu(HttpServletRequest request,HttpServletResponse response) {
		//实例化返回结果对象
		JSONObject jsos = new JSONObject();
		//实例化传入数据库的机构信息类
		OrgMenuModel model = new OrgMenuModel();
		// 建立多部分请求解析器
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest muRequest = resolver.resolveMultipart(request);
		//获取字符串参数
		String menuId = muRequest.getParameter("menuId2");
		String menuName = muRequest.getParameter("menuName2");
		String areaId = muRequest.getParameter("areaIdHidden");
		String menuType = muRequest.getParameter("menuType2");
		String menuUrl = muRequest.getParameter("menuUrl2");
		String menuOrder = muRequest.getParameter("menuOrder2");
		String menuStatus = muRequest.getParameter("menuStatus2");
		String isDefault = muRequest.getParameter("isDefault2");
		String menuDesc =  muRequest.getParameter("menuDesc2");
		// 获取上传图片文件
		MultipartFile orgLogo = muRequest.getFile("menuLogo2");
		MultipartFile backgroundImg = muRequest.getFile("backgroundImg2");
		//检查字符串参数
		if(menuId==null || "".equals(menuId)) {
			jsos.put("result","1");
			jsos.put("message","菜单Id不正确，请重新选择数据修改！");
			return jsos;
		}
		if(menuName==null || "".equals(menuName) || menuName.length()>1024) {
			jsos.put("result","1");
			jsos.put("message","菜单名称不正确，请重新输入！！");
			return jsos;
		}
		if(menuType==null || "".equals(menuType)) {
			jsos.put("result","1");
			jsos.put("message","请选择菜单类型！");
			return jsos;
		}
		if(menuUrl == null || menuUrl.length()>1024) {
			jsos.put("result","1");
			jsos.put("message","菜单链接不正确，请重新输入！");
			return jsos;
		}
		if(menuOrder ==null || !menuOrder.matches("\\d{1,2}")) {
			jsos.put("result","1");
			jsos.put("message","菜单排序不正确，请重新输入！");
			return jsos;
		}
		if(menuStatus==null || "".equals(menuStatus)) {
			jsos.put("result","1");
			jsos.put("message","请选择有效性！");
			return jsos;
		}
		if(isDefault==null || "".equals(isDefault)) {
			jsos.put("result","1");
			jsos.put("message","请选择是否默认为首页菜单！");
			return jsos;
		}
		
		//给model成员变量赋值
		model.setMenuId(menuId);
		model.setIsDefault(isDefault);
		model.setAreaId(areaId);
		model.setMenuName(menuName);
		model.setMenuOrder(menuOrder);
		model.setMenuStatus(menuStatus);
		model.setMenuType(menuType);
		model.setMenuUrl(menuUrl);
		model.setMenuDesc(menuDesc);	
		//获取上传orgLogo文件字节数组，转为base64然后存入model实例中
		if(orgLogo!=null && !"".equals(orgLogo)) {
			byte[] bytes;
			try {
				bytes = orgLogo.getBytes();
				model.setMenuLogo(new String(Base64Cipher.encode(bytes)));
			} catch (IOException e1) {
				logger.error("菜单LOGO图片文件转换成base64失败:" + e1.getMessage());
				jsos.put("result","1");
				jsos.put("message","菜单LOGO图片文件上传失败，请上传正确图片文件！");
				return jsos;
			}									
		}
		
		//查询数据库获取背景图片保存路径
		List<OrgMenuModel> list = null;
		try {
			list = orgMenuDao.queryMenuList(model);
		} catch (Exception e) {
			logger.error("查询菜单信息:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","查询菜单信息失败");
			return jsos;
		}
		//定义原始背景图片路径
		String imgOldId = "";
		String logoOldId = "";
		//因为查出来的数据只会有一条所以直接获取第一天数据取值
		for(OrgMenuModel o : list) {
			imgOldId = o.getBackgroundImg();
			logoOldId = o.getMenuLogoUrl();
		}
		//将image图片文件上传图片服务器然后把uri保存到model实例中，并删除原始图片	
		try {
			if(orgLogo != null && !"".equals(orgLogo)) {
				model.setMenuLogoUrl(uploadImage(orgLogo,logoOldId, ""));
			}
			if(backgroundImg != null && !"".equals(backgroundImg)) {
				model.setBackgroundImg(uploadImage(backgroundImg,imgOldId,""));
			}			
		} catch (Exception e) {
			logger.error("图片上传失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","图片更新失败，"+e.getMessage());
			return jsos;
		}
		
		//链接数据库添加机构菜单信息
		try {
			orgMenuDao.updateMenu(model);
		} catch (Exception e) {
			logger.error("数据库访问失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","数据库异常，信息添加失败");
			return jsos;
		}
		
		jsos.put("result","0");
		jsos.put("message","信息更新成功");		
		
		return jsos;
	}
	
	/**
	 * 删除机构菜单信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delmenu")
	@ResponseBody
	public JSONObject delmenu(HttpServletRequest request,HttpServletResponse response) {
		JSONObject jsos = new JSONObject();	
		String log_ids = request.getParameter("menuId");
		String[] idarry = log_ids.split(",");
		List<String> list = Arrays.asList(idarry);	
		
		//定义机构信息实例
		List<OrgMenuModel> orgMenuList = new ArrayList<OrgMenuModel>();	

		//链接数据库查询背景地址，删除信息
		try {
			//查询机构菜单信息获取背景图片保存地址
			orgMenuList = orgMenuDao.queryMenuByList(list);
			//删除数据库机构信息
			orgMenuDao.delMenu(list);
		} catch (Exception e) {
			logger.error("删除记录失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","删除记录失败"+e.getMessage());
			return jsos;
		}
		
		//获取机构菜单背景图片路径，删除图片,如果保存文件的文件夹为空则删除文件夹
		for(OrgMenuModel model : orgMenuList) {		
			try {
				deleteImg(model.getBackgroundImg());
				deleteImg(model.getMenuLogoUrl());
			} catch (Exception e) {
				logger.error("删除图片失败:" + e.getMessage());
				jsos.put("result","1");
				jsos.put("message","删除图片失败");
				return jsos;
			}		
		}
		
		jsos.put("result","0");
		jsos.put("message","删除记录成功");
		return jsos;
	}
	
	/**
	 * 获取菜单列表，不分页，用于生成菜单选择框
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/createCombobox")
	@ResponseBody
	@Transactional
	public JSONArray createCombobox(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//实例化返回对象
		JSONArray josj = new JSONArray();
		//实例化列表信息
		List<OrgMenuModel> menuList = null;
		//获取参数
		String menuStatus = request.getParameter("menuStatus");
		
		//实例化机构菜单实例，并赋值
		OrgMenuModel model = new OrgMenuModel();
		model.setMenuStatus(menuStatus);
		
		//查询数据库获取菜单列表
		try {
			menuList = orgMenuDao.queryMenuListForCombobox(model);
			josj = JSONArray.fromObject(menuList);
		} catch (Exception e) {
			logger.error("查询失败:" + e.getMessage());
			return null;
		}	
		return josj;
	}	
}
