package com.eastcompeace.interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
/**
 * 获取请求报文，对请求报文进行解析
 */
@Component
public class RequestInterceptor extends BaseController implements HandlerInterceptor {
	private static Log logger = LogFactory.getLog(RequestInterceptor.class);
    private String 					   requestID;

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
    }

    /**
     * 执行方法前操作
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    	requestID = System.currentTimeMillis() + "";
    	logger.info("================================REQUEST("+requestID+") Start================================");
        response.reset();
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
    	logger.info("================================REQUEST("+requestID+") End================================");
    }
}
