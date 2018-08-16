package com.eastcompeace.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登录 拦截器
 * 
 * @author caobo 20151022
 */
public class SessionInterceptor implements HandlerInterceptor {

	/**
	 * preHandle方法是进行处理器拦截用的，顾名思义，该方法将在Controller处理之前进行调用，
	 * SpringMVC中的Interceptor拦截器是链式的，可以同时存在
	 * 多个Interceptor，然后SpringMVC会根据声明的前后顺序一个接一个的执行
	 * ，而且所有的Interceptor中的preHandle方法都会在 Controller方法调
	 * 用之前调用。SpringMVC的这种Interceptor链式结构也是可以进行中断的，这种中断方式是令preHandle的返
	 * 回值为false，当preHandle的返回值为 false的时候整个请求就结束了。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		// // 获取session中 user对象信息
		// UserModel user = (UserModel) request.getSession().getAttribute(
		// Constant.SESSION_USER);
		//
		// System.out.println("class:sessionInterceptor  method:preHandle  execute.......");
		// System.out.println("request url :  "+request.getRequestURL().toString());
		//
		// session 判断，session超时跳转至登录页面
		// if (user == null) {
		// String url = request.getContextPath() + "/login.html";
		// response.sendRedirect(url);
		// return false;
		// } else {
		// return true;
		// }
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler, ModelAndView ma)
			throws Exception {
		// System.out.println("请求之后，生成视图之前执行！！");
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// System.out.println("最后执行，一般用于释放资源 ！！");
	}

}
