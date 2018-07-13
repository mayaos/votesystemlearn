package com.eastcompeace.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.ServiceException;
import com.eastcompeace.dto.ArticleAllTypeDto;
import com.eastcompeace.dto.ArticleInfoDto;
import com.eastcompeace.dto.ArticleTypeDto;
import com.eastcompeace.dto.ReturnDto;
import com.eastcompeace.service.ArticleService;
import com.eastcompeace.share.utils.StringUtils;
import com.eastcompeace.util.ProjectPathUtils;

@Controller
@RequestMapping("/articleCtrl")
public class ArticleController extends BaseController{
	private static Log logger = LogFactory.getLog(ArticleController.class);
	
	@Autowired
	ArticleService articleService;
	
	/**
	 * 
	 * @MethodName getTargetArticleContent
	 * @Date   2018年5月30日下午4:28:03
	 * @author wzh
	 *
	 * 获取指定文章信息接口
	 * @param request
	 * @param response
	 * @return ReturnDto<Object>
	 * 方法操作步骤简述
	 * 1.获取参数,并进行非空校验
	 * 2.获取指定文章信息
	 */
	@RequestMapping("/getTargetArticleContent")
	@ResponseBody
	public ReturnDto<Object> getTargetArticleContent(HttpServletRequest request,HttpServletResponse response){
		logger.info(">>>-- 获取指定文章信息接口 开始 --<<<");
		ArticleInfoDto articleInfoDto = null;
		try {
			//1.获取参数,并进行校验
			String strText = getText(request);
			logger.info(">>>传入参数为："+strText);
			if( StringUtils.isEmpty(strText)) {
				throw new ServiceException("参数为空!");
			}
			JSONObject jso = JSONObject.fromObject(strText);
			if(!jso.containsKey("articleType")||!jso.containsKey("articleID")){
				throw new ServiceException("必填参数为空!");
			}
			String articleType = jso.getString("articleType");
			String articleID = jso.getString("articleID");
			//2.获取指定文章信息
			articleInfoDto = articleService.getTargetArticleContent(articleType,articleID);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(">>>异常信息:"+e.getLocalizedMessage());
			logger.info(">>>-- 获取指定文章信息接口 结束 --<<<");
			return returnFailJson(e.getMessage());
		}
		logger.info(">>>-- 获取指定文章信息接口 结束 --<<<");
		return returnSuccessJson(articleInfoDto);
	}
	
	/**
	 * 
	 * @MethodName getTargetTypeArticleList
	 * @Date   2018年5月30日下午5:47:59
	 * @author wzh
	 *
	 * 获取指定类别文章信息集合接口
	 * @param request
	 * @param response
	 * @return ReturnDto<Object>
	 * 方法操作步骤简述
	 * 1.获取参数,并进行校验
	 * 2.获取指定类别文章信息集合
	 */
	@RequestMapping("/getTargetTypeArticleList")
	@ResponseBody
	public ReturnDto<Object> getTargetTypeArticleList(HttpServletRequest request,HttpServletResponse response){
		logger.info(">>>-- 获取指定类别文章信息集合接口 开始 --<<<");
		ArticleTypeDto articleTypeDto = null;
		try {
			//1.获取参数,并进行校验
			String strText = getText(request);
			logger.info(">>>传入参数为："+strText);
			if( StringUtils.isEmpty(strText)) {
				throw new ServiceException("参数为空!");
			}
			JSONObject jso = JSONObject.fromObject(strText);
			if(!jso.containsKey("articleType")||!jso.containsKey("targetPage")){
				throw new ServiceException("必填参数为空!");
			}
			String articleType = jso.getString("articleType");
			String targetPage = jso.getString("targetPage");
			boolean checkResult = targetPage.matches("^[0-9]{1,5}$");
			if(!checkResult){
				throw new ServiceException("目标页数不是数字文本!");
			}else if(0==Integer.valueOf(targetPage)){
				throw new ServiceException("目标页数不能为0!");
			}
			//2.获取指定类别文章信息集合
			articleTypeDto = articleService.getTargetTypeArticleList(articleType,targetPage);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(">>>异常信息:"+e.getMessage());
			logger.info(">>>-- 获取指定类别文章信息接口 结束 --<<<");
			return returnFailJson(e.getMessage());
		}
		logger.info(">>>-- 获取指定类别文章信息集合接口 结束 --<<<");
		return returnSuccessJson(articleTypeDto);
	}
	
	/**
	 * 
	 * @MethodName getAllTypeArticleList
	 * @Date   2018年5月31日上午9:20:49
	 * @author wzh
	 *
	 * 获取所有类别文章信息集合接口
	 * @param request
	 * @param response
	 * @return ReturnDto<Object>
	 * 方法操作步骤简述
	 * 1.获取所有类别文章信息集合
	 */
	@RequestMapping("/getAllTypeArticleList")
	@ResponseBody
	public ReturnDto<Object> getAllTypeArticleList(HttpServletRequest request,HttpServletResponse response){
		logger.info(">>>-- 获取所有类别文章信息集合接口 开始 --<<<");
		ArticleAllTypeDto articleAllTypeDto = null;
		try {
			//1.获取所有类别文章信息集合
			articleAllTypeDto = articleService.getAllTypeArticleList();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(">>>异常信息:"+e.getLocalizedMessage());
			logger.info(">>>-- 获取所有类别文章信息集合接口 结束 --<<<");
			return returnFailJson(e.getMessage());
		}
		logger.info(">>>-- 获取所有类别文章信息集合接口 结束 --<<<");
		return returnSuccessJson(articleAllTypeDto);
	}
	
	
	@RequestMapping("/downloadFile")
	public void downloadFile(HttpServletRequest request,HttpServletResponse response) throws Exception {
		logger.info(">>>-- 下载文件 开始 --<<<");
		
		//1.获取参数,并进行校验
		String fileName = request.getParameter("filePath");
		if(StringUtils.isEmpty(fileName)){
			fileName =(String)request.getAttribute("filePath");
		}
		logger.info(">>>下载文件名:"+fileName);
		String fileFullPath = ProjectPathUtils.getProjectPath()+ "/static/files/" +fileName;
		logger.info(">>>下载文件全路径:"+fileFullPath);
		
		response.reset();
		//设置文件ContentType类型，会自动判断下载文件类型   
		response.setContentType("multipart/form-data");   
		response.setHeader("Content-Disposition", "attachment; filename="+ fileName);
		response.setContentType("charset=UTF-8");
		
		OutputStream out = response.getOutputStream();
		// 读取文件
		InputStream in = new FileInputStream(fileFullPath);
		// 写文件
		int b;
		while ((b = in.read()) != -1) {
			out.write(b);
		}
		in.close();
		logger.info(">>>下载文件完成!");
		out.flush();
		out.close();
		logger.info(">>>-- 下载文件 结束 --<<<");
	}
}
