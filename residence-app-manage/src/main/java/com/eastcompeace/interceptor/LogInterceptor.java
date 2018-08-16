package com.eastcompeace.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 日志拦截器
 * @author caobo
 * 20151022
 */
public class LogInterceptor implements HandlerInterceptor{

//	protected static final org.apache.commons.logging.Log log = LogFactory.getLog(LogInterceptor.class);
//	@Override
//	public String intercept(ActionInvocation actionInvocation) throws Exception {
//		
//		long beginTime=System.currentTimeMillis();
//		
//		String result="";// action执行结果
//		
//		
//		ActionContext ctx = actionInvocation.getInvocationContext();
//		String namespace=actionInvocation.getProxy().getNamespace();
//		namespace="/".equals(namespace)?"":namespace;
//		String actionName =ctx.getName();		
//		HttpServletRequest request= (HttpServletRequest) ctx.get(StrutsStatics.HTTP_REQUEST);    
//		String ip = request.getRemoteAddr();
//		User user =(User)ctx.getSession().get(Constants.USER_INFO);
//		
//		
//		String sql_sb=null;//执行的sql语句
//		SystemContext.setSql(new StringBuffer(128));
//		try {		
//			result = actionInvocation.invoke();	
//			//action 执行完了产生的sql语句
//			if(StringUtils.isNotBlank(SystemContext.getSql().toString())){
//				sql_sb=SystemContext.getSql().toString();
//			}
//			log.debug("sql=="+sql_sb);
//			
//			//如果是登录或注册action,记录登录日志
//			if(actionName.contains("userLogin")){
//				user= (User)ctx.getSession().get(Constants.USER_INFO);
//				if(user!=null){//登录成功,记录日志
//					Log log=new Log();
//					log.setUser_id(user.getId());
//					log.setRequest_url(namespace + "/" + actionName);	
//					log.setIp(ip);
//					log.setIssuccess("Y");
//					log.setSqlstr(sql_sb);
//					log.setLog_type(1L);
//					logDao.insertLog(log);//插入日志
//				}
//			}else if(actionName.contains("select")){//如果sql语句不为空，则插入日志	
//				Log log=new Log();		
//				log.setUser_id(user.getId());
//				log.setRequest_url(namespace + "/" + actionName);					
//				log.setIp(ip);
//				log.setLog_type(3L);
//				log.setIssuccess("Y");
//				log.setSqlstr(sql_sb);
//				logDao.insertLog(log);				
//			}else if(actionName.contains("insert")){//如果sql语句不为空，则插入日志	
//				Log log=new Log();		
//				log.setUser_id(user.getId());
//				log.setRequest_url(namespace + "/" + actionName);			
//				log.setIp(ip);
//				log.setLog_type(4L);
//				log.setIssuccess("Y");
//				log.setSqlstr(sql_sb);
//				logDao.insertLog(log);				
//			}else if(actionName.contains("update")){//如果sql语句不为空，则插入日志	
//				Log log=new Log();		
//				log.setUser_id(user.getId());
//				log.setRequest_url(namespace + "/" + actionName);					
//				log.setIp(ip);
//				log.setLog_type(5L);
//				log.setIssuccess("Y");
//				log.setSqlstr(sql_sb);
//				logDao.insertLog(log);				
//			}else if(actionName.contains("delete")){//如果sql语句不为空，则插入日志	
//				Log log=new Log();		
//				log.setUser_id(user.getId());
//				log.setRequest_url(namespace + "/" + actionName);				
//				log.setIp(ip);
//				log.setLog_type(6L);
//				log.setIssuccess("Y");
//				log.setSqlstr(sql_sb);
//				logDao.insertLog(log);				
//			}
//		}catch (BusinessException ex) {			
//			throw ex;//业务异常不做处理
//		}catch (Exception ex) {
//			ex.printStackTrace();
//			sql_sb=SystemContext.getSql().toString();
//			Log log=new Log();					
//			log.setException(extractExceptionInfo(ex));
//			log.setUser_id(user.getId());
//			log.setRequest_url(namespace + "/" + actionName);	
//			log.setIp(ip);
//			log.setSqlstr(sql_sb);
//			log.setLog_type(2L);
//			log.setIssuccess("N");
//			try {
//				logDao.insertLog(log);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}		
//			throw ex;// 继续抛异常
//		}finally{
//			SystemContext.remove();//移除ThreadLocal
//		}
//		// 执行结束
//		
//		long endTime=System.currentTimeMillis();		
////		System.out.println("--------actionName:"+actionName+"-------");
//		log.info("--------actionName:"+actionName+"-------");
////		System.out.println("--------action执行结果:"+result+"-------");
//		log.info("--------action执行结果:"+result+"-------");
//		if(sql_sb.length()!=0)
////		System.out.println("--------执行的sql语句"+sql_sb.length()+":"+sql_sb+"-------");
//			log.info("--------执行的sql语句"+sql_sb.length()+":"+sql_sb+"-------");
////		System.out.println("--------action执行时间:"+(endTime-beginTime)+"ms--------");
//		log.info("--------action执行时间:"+(endTime-beginTime)+"ms--------");
//		
//		return result;
//		
//	}
//	
//	/**
//	 * 取异常信息字符串
//	 */
//	public String extractExceptionInfo(Exception ex){
//		if(ex == null){
//			return "";
//		}else{
//			StringBuffer excption=new StringBuffer();
//			excption.append(ex.getMessage()+"\n");			
//			// 取堆栈信息			
//			for (StackTraceElement element : ex.getStackTrace()) {							
//				excption .append(element+"\n");
//			}			
//			return excption.toString().replaceAll("\\$", "##");
//		}
//	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception ex)
			throws Exception {
		System.out.println("最后执行，一般用于释放资源 ！！");
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj, ModelAndView ma) throws Exception {
		System.out.println("请求之后，生成视图之前执行！！");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj) throws Exception {
		System.out.println("请求之前执行！！！");
		return true;
	}

}
