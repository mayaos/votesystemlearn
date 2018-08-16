package com.eastcompeace.org.orginfo;

import java.io.File;
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
import com.eastcompeace.model.MerchantBenefitModel;
import com.eastcompeace.model.OrgInfoModel;
import com.eastcompeace.model.RightsInfoModel;
import com.eastcompeace.rights.menu.RightsMenuDao;
import com.eastcompeace.share.cipher.Base64Cipher;
import com.eastcompeace.system.menu.MenuDao;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/orginfoctrl")
public class OrgInfoController extends BaseController{
	private Logger logger = Logger.getLogger("OrgInfoController");
	@Resource
	private SqlSession sqlSession;
	@Resource
	private OrgInfoDao orgInfoDao;
	/**
	 * 第三方机构跳转主页面
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView orgInfoIndex() {
		return super.getModelView("common/orgInfo/");
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
	 * 查询机构信息列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryInfoList")
	@ResponseBody
	public JSONObject queryInfoList(HttpServletRequest request,HttpServletResponse response) {
		//实例化返回对象
		JSONObject josj = new JSONObject();
		//实例化列表信息
		List<OrgInfoModel> orgList = null;
		//获取参数
		String strRows = request.getParameter("rows");
		String strPage = request.getParameter("page");
		String menuNameSearch = request.getParameter("menuNameSearch");
		String orgNameSearch = request.getParameter("orgNameSearch");
		//实例化参数对象
		OrgInfoModel model = new OrgInfoModel();
		model.setMenuName(menuNameSearch);
		model.setOrgName(orgNameSearch);
		//查询数据库获取菜单列表
		try {
			PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
			orgList = orgInfoDao.queryOrgInfoList(model);
			PageInfo<OrgInfoModel> pages = new PageInfo<OrgInfoModel>(orgList);
			josj.put("total", pages.getTotal());
			josj.put("rows",orgList);
		} catch (Exception e) {
			logger.error("查询失败:" + e.getMessage());
			return null;
		}		
		return josj;
	}
	
	/**
	 * 添加机构信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addOrgInfo")
	@ResponseBody
	public JSONObject addOrgInfo(HttpServletRequest request,HttpServletResponse response) {
		//实例化返回结果对象
		JSONObject jsos = new JSONObject();
		//实例化传入数据库的机构信息类
		OrgInfoModel model = new OrgInfoModel();
		// 建立多部分请求解析器
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest muRequest = resolver.resolveMultipart(request);
		//获取字符串参数
		String orgName = muRequest.getParameter("orgName");
		String areaId = muRequest.getParameter("areaId");
		String menuID = muRequest.getParameter("menuID");
		String orgDesc = muRequest.getParameter("orgDesc");
		String orgPhone = muRequest.getParameter("orgPhone");
		String passFlag = muRequest.getParameter("passFlag");
		String noPassReason = muRequest.getParameter("noPassReason");
		// 获取上传图片文件
		MultipartFile orgLogo = muRequest.getFile("orgLogo");
		
		//检查字符串参数
		if(orgName==null || "".equals(orgName) || orgName.length()>20) {
			jsos.put("result","1");
			jsos.put("message","机构名称不正确，请重新输入！！");
			return jsos;
		}
		if(areaId==null || "".equals(areaId)) {
			jsos.put("result","1");
			jsos.put("message","请选择区域！");
			return jsos;
		}
		if(menuID==null || "".equals(menuID)) {
			jsos.put("result","1");
			jsos.put("message","请选择菜单！");
			return jsos;
		}
		if(orgDesc!=null && orgDesc.length()>100) {
			jsos.put("result","1");
			jsos.put("message","机构描述超长，请输入不超过100位的信息！");
			return jsos;
		}
		if(orgPhone!=null && !orgPhone.matches("1[3-8]\\d{9}|0\\d{2,3}-\\d{7,8}|0\\d{2,3}-\\d{7,8}-\\d{3,}")) {
			jsos.put("result","1");
			jsos.put("message","联系电话格式不对，请重新输入！");
			return jsos;
		}
		if(passFlag==null || "".equals(passFlag)) {
			jsos.put("result","1");
			jsos.put("message","请选择审核！");
			return jsos;
		}
		if(noPassReason!=null && noPassReason.length()>50) {
			jsos.put("result","1");
			jsos.put("message","审核未通过原因超长，请输入不超过50位的信息！");
			return jsos;
		}
		
		//给model成员变量赋值
		model.setOrgName(orgName);
		model.setAreaId(areaId);
		model.setOrgDesc(orgDesc);
		model.setMenuId(menuID);
		model.setNoPassReason(noPassReason);
		model.setPassFlag(passFlag);
		model.setOrgPhone(orgPhone);
			
		//获取上传orgLogo文件字节数组，转为base64然后存入model实例中
		byte[] bytes;
		try {
			bytes = orgLogo.getBytes();
			model.setOrgLogo(new String(Base64Cipher.encode(bytes)));
		} catch (IOException e1) {
			logger.error("机构Logo图片文件转换成base64失败:" + e1.getMessage());
			jsos.put("result","1");
			jsos.put("message","机构Logo图片文件上传失败，请上传正确图片文件！");
			return jsos;
		}
				
		//链接数据库保存机构信息
		try {
			orgInfoDao.addOrgInfo(model);
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
	 * 更新机构信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateOrgInfo")
	@ResponseBody
	public JSONObject updateOrgInfo(HttpServletRequest request,HttpServletResponse response) {
		//实例化返回结果对象
		JSONObject jsos = new JSONObject();
		//实例化传入数据库的机构信息类
		OrgInfoModel model = new OrgInfoModel();
		// 建立多部分请求解析器
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest muRequest = resolver.resolveMultipart(request);
		//获取字符串参数
		String orgId = muRequest.getParameter("orgId2");
		String orgName = muRequest.getParameter("orgName2");
		String areaId = muRequest.getParameter("areaIdHidden");
		String menuID = muRequest.getParameter("menuID2");
		String orgDesc = muRequest.getParameter("orgDesc2");
		String orgPhone = muRequest.getParameter("orgPhone2");
		String passFlag = muRequest.getParameter("passFlag2");
		String noPassReason = muRequest.getParameter("noPassReason2");
		// 获取上传图片文件
		MultipartFile orgLogo = muRequest.getFile("orgLogo2");
		
		//检查字符串参数
		if(orgId==null || "".equals(orgId)) {
			jsos.put("result","1");
			jsos.put("message","机构Id不能为空，请重新选择数据！");
			return jsos;
		}
		if(orgName==null || "".equals(orgName) || orgName.length()>20) {
			jsos.put("result","1");
			jsos.put("message","机构名称不正确，请重新输入！");
			return jsos;
		}
		if(menuID==null || "".equals(menuID)) {
			jsos.put("result","1");
			jsos.put("message","请选择菜单！");
			return jsos;
		}
		if(orgDesc!=null && orgDesc.length()>100) {
			jsos.put("result","1");
			jsos.put("message","机构描述超长，请输入不超过100位的信息！");
			return jsos;
		}
		if(orgPhone!=null && !orgPhone.matches("1[3-8]\\d{9}|0\\d{2,3}-\\d{7,8}|0\\d{2,3}-\\d{7,8}-\\d{3,}")) {
			jsos.put("result","1");
			jsos.put("message","联系电话格式不对，请重新输入！");
			return jsos;
		}
		if(passFlag==null || "".equals(passFlag)) {
			jsos.put("result","1");
			jsos.put("message","请选择审核！");
			return jsos;
		}
		if(noPassReason!=null && noPassReason.length()>50) {
			jsos.put("result","1");
			jsos.put("message","审核未通过原因超长，请输入不超过50位的信息！");
			return jsos;
		}
		
		//给model成员变量赋值
		model.setOrgId(orgId);
		model.setAreaId(areaId);
		model.setOrgName(orgName);
		model.setOrgDesc(orgDesc);
		model.setMenuId(menuID);
		model.setNoPassReason(noPassReason);
		model.setPassFlag(passFlag);
		model.setOrgPhone(orgPhone);
			
		//获取上传orgLogo文件字节数组，转为base64然后存入model实例中
		if(orgLogo!=null) {
			byte[] bytes;
			try {
				bytes = orgLogo.getBytes();
				model.setOrgLogo(new String(Base64Cipher.encode(bytes)));
			} catch (IOException e1) {
				logger.error("机构Logo图片文件转换成base64失败:" + e1.getMessage());
				jsos.put("result","1");
				jsos.put("message","机构Logo图片文件上传失败，请上传正确图片文件！");
				return jsos;
			}
		}
		
		//链接数据库保存机构信息
		try {
			orgInfoDao.updateOrgInfo(model);
		} catch (Exception e) {
			logger.error("数据库访问失败:" + e.getMessage());
			jsos.put("result","1");
			jsos.put("message","数据库异常，信息添加失败");
			return jsos;
		}
				
		jsos.put("result","0");
		jsos.put("message","修改成功");
		
		return jsos;
	}
	
	/**
	 * 删除机构信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delOrgInfo")
	@ResponseBody
	public JSONObject delOrgInfo(HttpServletRequest request,HttpServletResponse response) {
		
		JSONObject jsos = new JSONObject();	
		String log_ids = request.getParameter("orgId");
		String[] idarry = log_ids.split(",");
		List<String> list = Arrays.asList(idarry);	
		
		//链接数据库查询背景地址，删除信息
		try {
			//删除数据库机构信息
			orgInfoDao.delOrgInfo(list);
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
	 * 获取菜单列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/selectOrgInfoList")
	@ResponseBody
	public JSONArray selectMenuList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//实例化返回对象
		JSONArray jsonArr = null;	
		//实例化列表信息
		List<OrgInfoModel> menuList = null;
		String menuNameSearch = request.getParameter("menuNameSearch");
		String orgNameSearch = request.getParameter("orgNameSearch");
		
		//实例化参数对象
		OrgInfoModel model = new OrgInfoModel();
		model.setMenuName(menuNameSearch);
		model.setOrgName(orgNameSearch);
		
		//查询数据库获取菜单列表
		try {
			menuList = orgInfoDao.queryOrgInfoList(model);
		} catch (Exception e) {
			logger.error("查询失败:" + e.getMessage());
			return null;
		}
		jsonArr = JSONArray.fromObject(menuList);
		
		return jsonArr;
	}
	
}
