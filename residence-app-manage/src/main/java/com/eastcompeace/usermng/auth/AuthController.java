package com.eastcompeace.usermng.auth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.LogType.Logenum;
import com.eastcompeace.model.CitizenAuthModel;
import com.eastcompeace.model.excel.AuthExcelModel;
import com.eastcompeace.model.excel.CitizenAuthExcelModel;
import com.eastcompeace.system.log.LogDao;
import com.eastcompeace.util.BuildModel;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.ExcelUtils;
import com.eastcompeace.util.ReadExcel;
import com.eastcompeace.util.ResourceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 
 * @author ecp-zeng
 * @date 2017年1月9日
 */
@Controller
@RequestMapping("/authctrl")
public class AuthController extends BaseController{

	private static Log LOGGER = LogFactory.getLog(AuthController.class);
	
	@Resource
	private SqlSession sqlSession;
	
	/**
	 * 意见（投诉建议）主页
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView menuIndex() {
		return super.getModelView("usermng/auth");
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
		String userName = request.getParameter("userName");
		String authType = request.getParameter("authType");
		String authStatus = request.getParameter("authStatus");
		String strStartTime = request.getParameter("strStartTime");
		String strEndTime = request.getParameter("strEndTime");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("authType", authType);
		map.put("authStatus", authStatus);
		map.put("startTime", strStartTime);
		map.put("endTime", strEndTime);
		
		/*验证居民用户账号是否存在和合法*/
		AuthDao authDao = sqlSession.getMapper(AuthDao.class);
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<CitizenAuthModel> msgList = authDao.queryListBy(map);
		PageInfo<CitizenAuthModel> pages = new PageInfo<CitizenAuthModel>(msgList);
		
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel("Y", "查看用户认证信息", String.valueOf(Logenum.QUERYLIST), request));
		
		jsonObj.put("total", pages.getTotal());
		jsonObj.put("rows", pages.getList());
		return jsonObj;
	}
	
	/**
	 * 查看认证数据导出
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
//	@RequestMapping("/export")
//	@ResponseBody
//	public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String exportType = request.getParameter("exportType");
//		String userName = request.getParameter("userName");
//		String authType = request.getParameter("authType");
//		String authStatus = request.getParameter("authStatus");
//		String strStartTime = request.getParameter("strStartTime");
//		String strEndTime = request.getParameter("strEndTime");
//		
//		AuthDao authDao = sqlSession.getMapper(AuthDao.class);
//		List<Object> excelList = null;
//		String templatePath = null;
//		String fileName = null;
//		
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("userName", userName);
//		map.put("authType", authType);
//		map.put("authStatus", authStatus);
//		if("1".equals(exportType)){
//			if("".equals(strEndTime)){
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				map.put("startTime", "0000-00-00 00:00:00");
//				map.put("endTime", sdf.format(new Date()));
//			}
//			List<CitizenAuthExcelModel> msgList = authDao.queryAuthExcelListBy(map);
//			excelList = new ArrayList<Object>(msgList);
//			map.put("newStatus", 2);
//			authDao.updateMore(map);
//			templatePath = ResourceUtils.getProperty("AuthUserTemplate");
//			fileName = "身份证认证用户数据-" + excelList.size()+"-" +DateUtils.getNow("yyyyMMddHHmmss")+ ".xls";
//		}else{
//			map.put("startTime", strStartTime);
//			map.put("endTime", strEndTime);
//			List<AuthExcelModel> msgList = authDao.queryExcelListBy(map);
//			excelList = new ArrayList<Object>(msgList);
//			templatePath = ResourceUtils.getProperty("UserAuthTemplate");
//			fileName = "认证用户数据-" +DateUtils.getNow("yyyyMMddHHmmss")+ ".xls";
//		}
//		
//		/** 日志 */
//		LogDao logDao = sqlSession.getMapper(LogDao.class);
//		logDao.add(BuildModel.getModel("Y", "导出认证用户信息", String.valueOf(Logenum.QUERYLIST), request));
//		
//		ExcelUtils.exportToExcel(response, "", excelList, fileName, templatePath);
//	}
//	
	/**
	 * 认证结果导入
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/import")
	@ResponseBody
	public JSONObject importAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		MultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest multiRequest = multipartResolver.resolveMultipart(request);
		MultipartFile file = multiRequest.getFile("authFile");
	
		//取得当前上传文件的文件名称   
		JSONObject jsonObj = new JSONObject();
		if(file == null || "".equals(file)){
			jsonObj.put("result", -1);
			jsonObj.put("message", "上传文件为空！");
			return jsonObj; 
		}
		String fileName = file.getOriginalFilename();
        
		/*解析导入文件*/
        List<Map> list = new ArrayList<Map>();
		try {
			if("xls".equals(ReadExcel.getPostfix(fileName))){
				list = ReadExcel.readXlsStream(file.getInputStream(), 1);
			}else if("xlsx".equals(ReadExcel.getPostfix(fileName))){
				list = ReadExcel.readXlsxStream(file.getInputStream(), 1);
			}
		} catch (Exception ex) {
			LOGGER.error(this, ex);
			jsonObj.put("result", -1);
			jsonObj.put("message", "导入文件解析异常，请核对文件格式。");
			return jsonObj;
		}
		if (list == null) {
			jsonObj.put("result", -1);
			jsonObj.put("message", "导入文件为空！");
			return jsonObj;
		}
		List<AuthExcelModel> authList = new ArrayList<AuthExcelModel>();
		AuthExcelModel authExcel =null;
		for (int i = 0; i < list.size(); i++) {
			authExcel =new AuthExcelModel();
			authExcel.setUserName(list.get(i).get("用户账号")==null? "":list.get(i).get("用户账号").toString());
			authExcel.setCitizenName(list.get(i).get("姓名")==null? "":list.get(i).get("姓名").toString());
			authExcel.setIdCard(list.get(i).get("身份证号")==null? "":list.get(i).get("身份证号").toString());
			authExcel.setAuthTime(list.get(i).get("认证时间")==null? "":list.get(i).get("认证时间").toString());
			authExcel.setAuthResult(list.get(i).get("认证结果(返回)")==null? "":list.get(i).get("认证结果(返回)").toString());
			authExcel.setAuthResultMessage(list.get(i).get("结果备注(返回)")==null? "":list.get(i).get("结果备注(返回)").toString());
			authList.add(authExcel);
		}
		
		/*保存、验证、更新数据*/
		AuthDao authDao = sqlSession.getMapper(AuthDao.class);
		Map<String, Object> mParam = new HashMap<String, Object>();
		mParam.put("rtCode", 0);
		try {//1.清除，2.导入临时，3.验证和更新认证结果
			authDao.clearAuthTemp();
			authDao.importMore(authList);
			authDao.verifyAndUpdateAuth(mParam);
		} catch (Exception ex) {
			LOGGER.error(this, ex);
			jsonObj.put("result", -1);
			jsonObj.put("message", "导入文件解析异常，请核对文件格式。");
			return jsonObj;
		}
		
		if("0".equals(mParam.get("rtCode").toString())){
			jsonObj.put("result", 0);
			jsonObj.put("message", "导入成功！");
		}else if("10".equals(mParam.get("rtCode").toString())){
			List<AuthExcelModel> nullList =authDao.getImportNullList();
			jsonObj.put("result", -1);
			jsonObj.put("message", "导入文件中存在空值的表格！");
			jsonObj.put("errorList", nullList);
		}else if("11".equals(mParam.get("rtCode").toString())){
			List<AuthExcelModel> NonList =authDao.getImportNonList();
			jsonObj.put("result", -1);
			jsonObj.put("message", "导入文件中用户信息在认证表中不存在！");
			jsonObj.put("errorList", NonList);
		}else{
			jsonObj.put("result", -1);
			jsonObj.put("message", "导入文件过程发生数据库异常，请联系技术员处理！");
		}
		return jsonObj; 
	}
	
	@RequestMapping("/rcAuth")
	@ResponseBody
	public JSONObject rcAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String authStartTime = request.getParameter("authStartTime");
		String authEndTime = request.getParameter("authEndTime");

		/*保存、验证、更新数据*/
		AuthDao authDao = sqlSession.getMapper(AuthDao.class);
		Map<String, Object> mParam = new HashMap<String, Object>();
		mParam.put("startTime", authStartTime);
		mParam.put("endTime", authEndTime);
		mParam.put("rtCode", 0);
		mParam.put("rtMessage", "");
		try {
			authDao.RcAuth(mParam);
		} catch (Exception ex) {
			LOGGER.error(this, ex);
			jsonObj.put("result", -1);
			jsonObj.put("message", "居住证认证失败：操作数据库异常！");
			return jsonObj;
		}
		jsonObj.put("result", mParam.get("rtCode").toString());
		jsonObj.put("message", mParam.get("rtMessage").toString());
		
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel("Y", "居住证认证", String.valueOf(Logenum.AUTH), request));
		
		return jsonObj;
	}
	
	/**
	 * 删除消息
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
		AuthDao authDao = sqlSession.getMapper(AuthDao.class);
		try {
			authDao.deleteMoreBy(list);
			msg = "删除成功";
		} catch (Exception e) {
			LOGGER.error(this, e);
			flag = false;
			msg = "删除失败";
		}
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel(flag? "Y":"N", "删除用户认证信息", String.valueOf(Logenum.DELETEINFO), request));
		
		jsonObj.put("result", flag);
		jsonObj.put("message", msg);
		return jsonObj;
		
	}
	
}