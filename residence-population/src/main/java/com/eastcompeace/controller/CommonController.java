package com.eastcompeace.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.Constant;
import com.eastcompeace.util.VerifyCodeUtils;

@Controller
@RequestMapping("/commonCtrl")
public class CommonController extends BaseController{
	private static Log logger = LogFactory.getLog(CommonController.class);

	@RequestMapping("/checkcode")
	public void getCheckCodeInfo(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("image/jpeg");
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-control", "no-cache");
		response.setDateHeader("expires", 0);

		int width = 100, height = 40;
		
		//生成随机字符串  
        String verifyCode = VerifyCodeUtils.generateVerifyCode(5);
		HttpSession session = request.getSession();
		session.removeAttribute(Constant.SESSION_CHECK_CODE);
		session.setAttribute(Constant.SESSION_CHECK_CODE, verifyCode);
		
		try {
			VerifyCodeUtils.outputImage(width,height,response.getOutputStream(),verifyCode);
		}catch(IOException e) {
			e.printStackTrace();
			logger.info(">>>异常信息:"+e.getMessage());
		}
	}
}
