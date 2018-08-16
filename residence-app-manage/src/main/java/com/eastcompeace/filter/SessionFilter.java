package com.eastcompeace.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastcompeace.base.Configuration;
import com.eastcompeace.base.Constant;
import com.eastcompeace.model.UserModel;

/**
 * 此类主要对session进行过滤 1 session失效时，跳转登录页面 2 session有效时，根据具体请求跳转页面
 * 
 * @author Administrator
 * 
 */
public class SessionFilter implements Filter {

	public static List list = new ArrayList();
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		new Configuration();
		Configuration config = Configuration.instance();
		list =	config.getNodes();
	}

	@Override
	public void destroy() {

	}

	/**
	 * 具体过滤规则
	 */
	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		if (!(servletRequest instanceof HttpServletRequest)
				|| !(servletResponse instanceof HttpServletResponse)) {
			throw new ServletException(
					"SessionFilter just supports HTTP requests");
		}
		// 类型强转
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		new Configuration();
		Configuration config = Configuration.instance();
		// 获取session中 user对象信息
		UserModel user = (UserModel) request.getSession().getAttribute(Constant.SESSION_USER);
		// 项目路径获取
		String contextPath = request.getContextPath() + "/";
		// 请求地址获取
		String requestUrl = request.getRequestURI();
		// 用户登录系统时网页地址
		String loginUrl = contextPath + Constant.LOGIN;
		// portal
		String porUrl = contextPath + Constant.PORTAL;
		// 用户已经登录，则可以继续访问
		if (user != null) {
			if (requestUrl.equals(contextPath)) {
				response.sendRedirect(porUrl);
				return;
			}
			if (requestUrl.equals(loginUrl)) {
				response.sendRedirect(porUrl);
				return;
			}
			chain.doFilter(servletRequest, servletResponse);
			return;
		}
		if (requestUrl.equals(contextPath)) {
			response.sendRedirect(loginUrl);
			return;
		}
	
		boolean b = false;
		for (int i = 0; i < list.size(); i++) {
			String allowPath = contextPath+list.get(i);
			b = requestUrl.startsWith(allowPath, 0);
			while (b) {
				chain.doFilter(servletRequest, servletResponse);
				return;
			}
		}
		response.sendRedirect(loginUrl);
		return;
		
	}
}
