package com.eastcompeace.config.software;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.eastcompeace.base.LogType.Logenum;
import com.eastcompeace.model.ButtonModel;
import com.eastcompeace.model.MenuModel;
import com.eastcompeace.model.SoftwareModel;
import com.eastcompeace.share.cipher.SHACipher;
import com.eastcompeace.share.utils.ByteUtils;
import com.eastcompeace.system.log.LogDao;
import com.eastcompeace.system.menu.MenuDao;
import com.eastcompeace.util.BuildModel;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/softwarectrl")
public class SoftwareController extends BaseController{
	private Logger logger = Logger.getLogger("SoftwareController");
	@Resource
	private SoftwareDao softwareDao;
	@Resource
	private SqlSession sqlSession;
	/**
	 * 转发到software主页面
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView areaIndex() {
		return super.getModelView("config/software/");
	}
	
	/**
	 * 获取软件信息列表，带条件查询功能
	 * @param req
	 * @return
	 */
	@RequestMapping("/softwareList")
	@ResponseBody
	public JSONObject softwareList(HttpServletRequest req) {
		//获取参数
		String rows = req.getParameter("rows");
		String page = req.getParameter("page");
		// 获取条件查询参数	
		String softVersion = req.getParameter("softVersion");
		String uploadDateStart = req.getParameter("uploadDateStart");
		String uploadDateEnd = req.getParameter("uploadDateEnd");		
		
		//定义变量
		List<SoftwareModel> softwareModelList;
		PageInfo<SoftwareModel> pageinfo;
		JSONObject json = new JSONObject();
		//定义查询参数Map
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("softVersion", softVersion);
		paramMap.put("uploadDateStart", uploadDateStart);
		paramMap.put("uploadDateEnd", uploadDateEnd);
		//访问数据库获取区域信息列表
		try {
			PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
			softwareModelList = softwareDao.querySoftwareList(paramMap);
			pageinfo = new PageInfo<SoftwareModel>(softwareModelList);
			json.put("total", pageinfo.getTotal());
			json.put("rows", softwareModelList);
		} catch (Exception e) {
			logger.error("数据库异常，获取软件信息列表信息失败:" + e.getMessage());
			return null;
		}	
		
		return json;
	}
	
	/**
	 * 上传软件包并把该软件包对应的信息存入数据库
	 * @param req
	 * @return
	 */
	@RequestMapping("/addSoftware")
	@ResponseBody
	public JSONObject addSoftware(HttpServletRequest req) {
		//实例化返回结果类
		JSONObject jsonObj = new JSONObject();
		//查询访问数据库传入参数Map
		Map<String,Object> params = new HashMap<String,Object>();
		//条件查询数据库返回值，Integer类型，0：无数据，其他数字表示有数据
		Integer flag = 0;
		//定义文件上传表单的视图解析器
		MultipartResolver resolver = new CommonsMultipartResolver(req.getSession().getServletContext());
		//req转型为MultipartHttpRequest  
		MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(req);
		//参数获取
		MultipartFile upSoftwareSrc = multipartRequest.getFile("file1");
		String softVersion = multipartRequest.getParameter("softVersion");
		String softType = multipartRequest.getParameter("softType");
		String forceUpgrade = multipartRequest.getParameter("forceUpgrade");
		String softNotes = multipartRequest.getParameter("softNotes");
		String softFullName = upSoftwareSrc.getOriginalFilename();
		
		//检查不能为必填项的参数是否有值
		if(softType==null || "".equals(softType)) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "软件类型选项不能为空");
			return jsonObj;
		}
		if(!"android".equals(softType) && !"ios".equals(softType)) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "软件类型值不正确");
			return jsonObj;
		}
		if(forceUpgrade==null || "".equals(forceUpgrade)) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "强制升级选项不能为空");
			return jsonObj;
		}
		if(softVersion==null || "".equals(softVersion)) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "软件版本栏不能为空");
			return jsonObj;
		}	
		if(upSoftwareSrc.isEmpty()) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "请上传安装包");
			return jsonObj;
		}
		//forceUpgrade传过来的值为‘是’‘否’，需要转为true,false
		if("是".equals(forceUpgrade)) {
			forceUpgrade = "true";
		} else if("否".equals(forceUpgrade)) {
			forceUpgrade = "false";
		} else {
			jsonObj.put("result", 1);
			jsonObj.put("message", "强制升级参数值不正确");
			return jsonObj;
		}
			
		//拆分软件全名
		String softName = softFullName.substring(0, softFullName.lastIndexOf(".")); //文件名
		String suffix = softFullName.substring(softFullName.lastIndexOf(".")+1);   //文件后缀
		//检测后缀名是否为"apk"，若不是则返回错误信息
		if(!"apk".equalsIgnoreCase(suffix)) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "上传文件类型不对，请上传apk类型文件");
			return jsonObj;
		}
		
		//计算上传apk文件哈希值
		byte[] bytes = null;
		try {
			bytes = SHACipher.encrypt(upSoftwareSrc.getInputStream());
		} catch (Exception e) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "无法计算哈希值，文件上传失败");
			return jsonObj;
		}
		String softHash = ByteUtils.toHexString(bytes);
		//访问数据库查看是否存在哈希值一样的数据，若存在则返回文件上传失败信息
		flag = 0;
		params.clear();
		try {
			params.put("softHash", softHash);
			flag = softwareDao.querySoftwareByParams(params);
		} catch (Exception e) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "无法访问数据库，文件上传失败");
			return jsonObj;
		}
		if(flag != 0) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "该安装包已存在，无法再上传");
			return jsonObj;
		}
		
		//检查上传的安装包名是否和已上传的安装包有冲突
		flag = 0;
		params.clear();
		try {		
			params.put("softName", softName);
			flag = softwareDao.querySoftwareByParams(params);
		} catch (Exception e) {
			logger.error("访问数据库失败", e);
			jsonObj.put("result", 1);
			jsonObj.put("message", "访问数据库失败");
			return jsonObj;
		}
		if(flag!=0) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "软件包名已存在，请重新命名");
			return jsonObj;
		}
		
		//检查软件版本号是否已存在
		flag = 0;
		params.clear();
		try {
			params.put("softVersion", softVersion);
			params.put("softType", softType);
			flag = softwareDao.querySoftwareByParams(params);
		} catch (Exception e) {
			logger.error("访问数据库失败", e);
			jsonObj.put("result", 1);
			jsonObj.put("message", "访问数据库失败");
			return jsonObj;
		}
		if(flag!=0) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "该类型软件版本号已存在，请重新定义版本号");
			return jsonObj;
		}
			
		//上传文件到服务器
		String softUri = null;
		try {
			softUri = uploadImage(upSoftwareSrc,"", ".apk");
		} catch (Exception e) {
			logger.error("上传文件失败", e);
			jsonObj.put("result", 1);
			jsonObj.put("message", "软件上传失败！");
			return jsonObj;
		}
		
		//实例化软件信息类
		Integer softId = null;
		Double softSizeLong = (double)upSoftwareSrc.getSize()/1024/1024;
		BigDecimal   b   =   new   BigDecimal(softSizeLong);  
		double   f1   =   b.setScale(2,   RoundingMode.HALF_UP).doubleValue();  
		String softSize = Double.toString(f1)+"M";
		String createTime = DateUtils.getNow();
		SoftwareModel softwareModel = new SoftwareModel(softId, softVersion, softName, softType, 
				softSize, softUri, forceUpgrade, createTime, softNotes, softHash);
		
		//访问数据库保存软件信息
		try {
			softwareDao.addSoftwareInfo(softwareModel);
			jsonObj.put("result", 0);
			jsonObj.put("message", "软件上传成功");
		} catch (Exception e) {
			logger.error("访问数据库失败", e);
			jsonObj.put("result", 1);
			jsonObj.put("message", "软件上传失败");
			return jsonObj;
		}
			
		return jsonObj;
	}
	
	/**
	 * 删除上传的软件安装包和数据库的软件信息
	 * @param req
	 * @return 
	 */
	@RequestMapping("/delSoftware")
	@ResponseBody
	public JSONObject delSoftware(HttpServletRequest req) {
		//实例化返回结果类
		JSONObject jsonObj = new JSONObject();
		//获取传入的参数
		String softId = req.getParameter("softId");
		String softURi = req.getParameter("softURi");
		//检查参数是否为空
		if(softId==null || "".equals(softId)) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "传入的softId不能为空");
			return jsonObj;
		}
		if(softURi==null || "".equals(softURi)) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "传入的softURi不能为空");
			return jsonObj;
		}	
		
		try {
			//访问数据库删除软件信息
			softwareDao.delSoftwareInfo(softId);
			//删除文件服务器上的文件
			deleteImg(softURi);		
		} catch (Exception e) {
			logger.error("删除软件信息失败", e);
			jsonObj.put("result", 1);
			jsonObj.put("message", "删除软件信息失败");
			return jsonObj;
		}	
		
		jsonObj.put("result", 0);
		jsonObj.put("message", "软件信息删除成功");
		return jsonObj;
	}
	
	/**
	 * 更新软件信息
	 * @param req
	 * @return
	 */
	@RequestMapping("updateSoftware")
	@ResponseBody
	public JSONObject updateSoftware(HttpServletRequest req) {
		//实例化返回结果类
		JSONObject jsonObj = new JSONObject();
		//获取传入的参数
		String softId = req.getParameter("softId1");
		String softType = req.getParameter("softType1");
		String forceUpgrade = req.getParameter("forceUpgrade1");
		String softVersion = req.getParameter("softVersion1");
		String softNotes = req.getParameter("softNotes1");
		
		//判断参数是否符合要求
		if(softId==null || "".equals(softId) || !softId.matches("\\d+")) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "软件ID值不正确！");
			return jsonObj;
		}
		if(softType==null || "".equals(softType)) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "软件类型不能为空");
			return jsonObj;
		}
		if(!"android".equals(softType) && !"ios".equals(softType)) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "软件类型值不正确");
			return jsonObj;
		}
		if(softVersion==null || "".equals(softVersion) || !softVersion.matches("v\\d{1,2}\\.\\d{1,2}\\.\\d{1,2}")) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "软件版本不符合要求，请重新输入！");
			return jsonObj;
		}
		//forceUpgrade传过来的值为‘是’‘否’，需要转为true,false
		if("是".equals(forceUpgrade)) {
			forceUpgrade = "true";
		} else if("否".equals(forceUpgrade)) {
			forceUpgrade = "false";
		} else {
			jsonObj.put("result", 1);
			jsonObj.put("message", "强制升级参数值不正确");
			return jsonObj;
		}
		
		//实例化软件信息类
		SoftwareModel software = new SoftwareModel();
		software.setSoftId(Integer.parseInt(softId));
		software.setSoftType(softType);
		software.setSoftVersion(softVersion);
		software.setSoftNotes(softNotes);
		software.setForceUpgrade(forceUpgrade);
		
		//软件类型+软件版本组合需保持唯一，查询数据库如果该组合存在且软件ID与自身ID不一致则返回错误提示
		List<SoftwareModel> list = null;
		try {
			list = softwareDao.queryBySoftTypeAndSoftVersion(softType, softVersion);
		} catch (Exception e) {
			logger.error("数据库异常，获取软件信息列表信息失败:" + e.getMessage());
			jsonObj.put("result", 1);
			jsonObj.put("message", "访问数据库失败，请稍后再试！");
			return jsonObj;
		}
		//如果list里包含的元素多于1则报错（一般不可能出现）
		if(list.size()>1) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "该软件类型的软件版本已存在，请重新输入！");
			return jsonObj;
		}
		//如果list存在一个元素，则对比软件ID是否一致，不一致返回错误信息
		if(list.size()==1) {
			if(list.get(0).getSoftId() != Integer.parseInt(softId)) {
				jsonObj.put("result", 1);
				jsonObj.put("message", "该软件类型的软件版本已存在，请重新输入！");
				return jsonObj;
			}
		}
		
		//连接数据库更新软件信息
		try {
			softwareDao.updateSoftware(software);
		} catch (Exception e) {
			logger.error("数据库异常，更新软件信息列表信息失败:" + e.getMessage());
			jsonObj.put("result", 1);
			jsonObj.put("message", "访问数据库失败，请稍后再试！");
			return jsonObj;
		}
		
		jsonObj.put("result", 0);
		jsonObj.put("message", "更新数据成功！");
		return jsonObj;
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
		String softId = "333";
		System.out.println(softId.matches("\\d+"));
	}
}
