package com.eastcompeace.system.config;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.model.ConfigModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 配置管理类（配置增、删、改、查功能）
 * 
 * @author zhuchaochao
 * 
 */
@Controller
@RequestMapping("/configctrl")
public class ConfigController extends BaseController {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 配置管理基础页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView menuIndex() {
		return super.getModelView("common/config/");
	}

	/**
	 * 查询配置列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/configlist")
	@ResponseBody
	public JSONObject getConfigList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();

		String rows = request.getParameter("rows");
		String page = request.getParameter("page");

		String paramName = request.getParameter("strParamName");
		String paramValue = request.getParameter("strParamValue");

		ConfigModel config = new ConfigModel();
		
		config.setParamName(paramName);
		config.setParamValue(paramValue);

		ConfigDao configDao = sqlSession.getMapper(ConfigDao.class);

		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		List<ConfigModel> configlist = configDao.queryConfigList(config);
		PageInfo<ConfigModel> pageinfo = new PageInfo<ConfigModel>(configlist);

		jsonObj.put("total", pageinfo.getTotal());
		jsonObj.put("rows", pageinfo.getList());

		return jsonObj;
	}

	/**
	 * 保存配置信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addconfiginfo")
	@ResponseBody
	public JSONObject addConfigInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		response.setCharacterEncoding("utf-8");
		
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
		
		String paramName = multipartRequest.getParameter("param_name");
		String paramValue = multipartRequest.getParameter("param_value");
		String paramDesc = multipartRequest.getParameter("param_desc");
		String paramValid = multipartRequest.getParameter("param_valid");
		MultipartFile upImgsrc = multipartRequest.getFile("up_imgsrc");
		
		ConfigModel config = new ConfigModel();
		
		config.setParamName(paramName);
		config.setParamValue(paramValue);
		config.setParamDesc(paramDesc);
		config.setParamValid(paramValid);
		if(!upImgsrc.isEmpty()){
			config.setUpImgsrc(uploadImage(upImgsrc, "config_"));
		}
		
		ConfigDao configDao = sqlSession.getMapper(ConfigDao.class);

		try {
			configDao.insertConfig(config);
			jsonObj.put("result", 0);
			jsonObj.put("message", "配置信息保存成功");
		} catch (Exception ex) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "配置信息保存失败");
			return jsonObj;
		}

		return jsonObj;
	}

	//上传图片
	private String uploadImage(MultipartFile upImgsrc, String head) throws Exception{
		InputStream inStream = ConfigController.class.getClassLoader().getResourceAsStream("configure.properties");
		Properties prop = new Properties();
		prop.load(inStream);
		String saveImgUrl = prop.getProperty("saveImgUrl") + "/config";
		
		String imageFileName = upImgsrc.getOriginalFilename();
		String temp=imageFileName.substring(imageFileName.indexOf("."),imageFileName.length());
		String name = head + UUID.randomUUID().toString()+ temp;
		
		File file=new File(saveImgUrl + "/" + name);
		if(!file.exists()){
			file.mkdirs();
		}
		upImgsrc.transferTo(file);
			
		return name;
	}

	/**
	 * 修改配置信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateconfiginfo")
	@ResponseBody
	public JSONObject updateConfigInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		response.setCharacterEncoding("utf-8");
		
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
		
		String paramId = multipartRequest.getParameter("param_id2");
		String imgSrc = multipartRequest.getParameter("img_src");
		String paramName = multipartRequest.getParameter("param_name2");
		String paramValue = multipartRequest.getParameter("param_value2");
		String paramDesc = multipartRequest.getParameter("param_desc2");
		MultipartFile upImgsrc = multipartRequest.getFile("up_imgsrc2");
		String paramValid = multipartRequest.getParameter("param_valid2");
		
		ConfigModel config = new ConfigModel();
		
		if (StringUtils.isNotBlank(paramId)) {
			config.setParamId(Integer.parseInt(paramId));
		}
		
		config.setParamName(paramName);
		config.setParamValue(paramValue);
		config.setParamDesc(paramDesc);
		config.setParamValid(paramValid);

		if(!upImgsrc.isEmpty()){
			if(!imgSrc.equals("")){
				deleteImage(imgSrc);
			}
			config.setUpImgsrc(uploadImage(upImgsrc, "config_"));
		}
		
		ConfigDao configDao = sqlSession.getMapper(ConfigDao.class);

		try {
			configDao.updateConfig(config);
			jsonObj.put("result", 0);
			jsonObj.put("message", "配置信息修改成功");
		} catch (Exception ex) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "配置信息修改失败");
			return jsonObj;
		}

		return jsonObj;
	}
	
	//删除图片
	public boolean deleteImage(String imgName) throws Exception{
		InputStream inStream = ConfigController.class.getClassLoader().getResourceAsStream("configure.properties");
		Properties prop = new Properties();
		prop.load(inStream);
		String saveImgUrl = prop.getProperty("saveImgUrl") + "/config";
		File file = new File(saveImgUrl + "/" + imgName);
		boolean flag = false;
		
		if(file.isFile() && file.exists()){
			file.delete();
			flag = true;
		}
		
		return flag;
	}
	
	/**
	 * 根据ID删除配置信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteconfig")
	@ResponseBody
	public JSONObject delConfigInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();

		ConfigDao configDao = sqlSession.getMapper(ConfigDao.class);

		String arrParamIds = request.getParameter("arrParamIds");
		String arrUpImgsrcs = request.getParameter("arrUpImgsrcs");
		String[] paramIds = arrParamIds.split(",");
		String[] upImgsrcs = arrUpImgsrcs.split(",");
		int idLen = paramIds.length;
		int upImgsrcLen = upImgsrcs.length;

		try {
			for (int i = 0; i < upImgsrcLen; i++){
				if(!upImgsrcs[i].equals("")){
					deleteImage(upImgsrcs[i]);
				}
			}
			
			for (int i = 0; i < idLen; i++) {
				configDao.deleteConfig(paramIds[i]);
			}
			
			jsonObj.put("result", 0);
			jsonObj.put("message", "配置信息删除成功");
		} catch (Exception ex) {
			jsonObj.put("result", 1);
			jsonObj.put("message", "配置信息删除失败，稍后请重试");
			return jsonObj;
		}

		return jsonObj;
	}
	
	/**
	 * 显示图片
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showimage")
	@ResponseBody
	public JSONObject showImage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		InputStream inStream = ConfigController.class.getClassLoader().getResourceAsStream("configure.properties");
		Properties prop = new Properties();
		prop.load(inStream);
		String getImgUrl = prop.getProperty("getImgUrl") + "config/";
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("webapps", getImgUrl);
		
		return jsonObj;
	}
}