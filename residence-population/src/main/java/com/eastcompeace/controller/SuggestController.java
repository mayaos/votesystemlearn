package com.eastcompeace.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.Constant;
import com.eastcompeace.base.ServiceException;
import com.eastcompeace.dto.ReturnDto;
import com.eastcompeace.dto.SuggestTypeDto;
import com.eastcompeace.service.SuggestService;

@Controller
@RequestMapping("/suggestCtrl")
public class SuggestController extends BaseController{
	private static Log logger = LogFactory.getLog(SuggestController.class);
	
	@Autowired
	SuggestService suggestService;
	
	/**
	 * 
	 * @MethodName getSuggestList
	 * @Date   2018年6月12日上午9:49:54
	 * @author wzh
	 *
	 * 意见建议信息集合接口
	 * @param request
	 * @param response
	 * @return ReturnDto<Object>
	 * 方法操作步骤简述
	 */
	@RequestMapping("getSuggestList")
	@ResponseBody
	public ReturnDto<Object> getSuggestList(HttpServletRequest request,HttpServletResponse response){
		logger.info(">>>-- 意见建议信息集合接口 开始 --<<<");
		SuggestTypeDto suggestTypeDto = null;
		try {
			//1.获取参数,并进行校验
			String strText = getText(request);
			logger.info(">>>传入参数:"+strText);
			if(StringUtils.isEmpty(strText)){
				throw new ServiceException("参数为空!");
			}
			JSONObject jso = JSONObject.fromObject(strText);
			if(!jso.containsKey("targetPage")){
				throw new ServiceException("必填参数为空!");
			}
			String targetPage = jso.getString("targetPage");
			boolean checkResult = targetPage.matches("^[0-9]{1,5}$");
			if(!checkResult){
				throw new ServiceException("目标页数不是数字文本!");
			}else if(0 == Integer.valueOf(targetPage)){
				throw new ServiceException("目标页数不能为0!");
			}
			//2.获取意见建议信息集合
			suggestTypeDto = suggestService.getSuggestList(targetPage);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(">>>异常信息:"+e.getMessage());
			logger.info(">>>-- 意见建议信息集合接口 结束 --<<<");
			return returnFailJson(e.getMessage());
		}
		logger.info(">>>-- 意见建议信息集合接口 结束 --<<<");
		return returnSuccessJson(suggestTypeDto);
	}
	
	/**
	 * 
	 * @MethodName submitSuggest
	 * @Date   2018年6月13日下午2:21:10
	 * @author wzh
	 *
	 * 提交意见建议信息接口
	 * @param request
	 * @param response
	 * @return ReturnDto<Object>
	 * 方法操作步骤简述
	 */
	@RequestMapping("submitSuggest")
	@ResponseBody
	public ReturnDto<Object> submitSuggest(HttpServletRequest request,HttpServletResponse response){
		logger.info(">>>-- 提交意见建议信息接口 开始 --<<<");
		JSONObject returnJSON = null;
		try{
			//1.获取参数,并进行校验
			String strText = getText(request);
			logger.info(">>>传入参数:"+strText);
			if(StringUtils.isEmpty(strText)){
				throw new ServiceException("参数为空!");
			}
			JSONObject requestjso = JSONObject.fromObject(strText);
			if(!requestjso.containsKey("ctgId")||!requestjso.containsKey("email")||!requestjso.containsKey("phone")
					||!requestjso.containsKey("qq")||!requestjso.containsKey("title")||!requestjso.containsKey("content")
					||!requestjso.containsKey("captcha")){
				throw new ServiceException("必填参数为空!");
			}
			//校验验证码
			String captcha = requestjso.getString("captcha");
			HttpSession session = request.getSession();
			String captchaInSession = (String)session.getAttribute(Constant.SESSION_CHECK_CODE);
			if(!captcha.equals(captchaInSession)){
				logger.info(">>>验证码错误!");
				returnJSON = new JSONObject();
				returnJSON.put("status",1);
				return returnSuccessJson(returnJSON);
			}
			
			//2.提交意见建议信息
			returnJSON = suggestService.submitSuggest(requestjso);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(">>>异常信息:"+e.getMessage());
			logger.info(">>>-- 意见建议信息集合接口 结束 --<<<");
			return returnFailJson(e.getMessage());
		}
		logger.info(">>>-- 提交意见建议信息接口 结束 --<<<");
		return returnSuccessJson(returnJSON);
	}
}
