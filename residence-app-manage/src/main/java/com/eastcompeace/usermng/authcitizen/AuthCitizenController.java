package com.eastcompeace.usermng.authcitizen;

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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.LogType.Logenum;
import com.eastcompeace.model.CitizenAuthModel;
import com.eastcompeace.model.CitizenMessageModel;
import com.eastcompeace.model.CitizenModel;
import com.eastcompeace.model.CitizenUserModel;
import com.eastcompeace.model.RcCardInfoModel;
import com.eastcompeace.system.log.LogDao;
import com.eastcompeace.usermng.news.NewsDao;
import com.eastcompeace.util.BuildModel;
import com.eastcompeace.util.Constant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 
 * @author xianzehua
 * @date 2017年2月24日
 */
@Controller
@RequestMapping("/citizenAuthctrl")
public class AuthCitizenController extends BaseController{

	private static Log LOGGER = LogFactory.getLog(AuthCitizenController.class);
	
	public static  final String SUCCESS = "1"; //
	
	@Resource
	private SqlSession sqlSession;
	
	/**
	 * 身份认证主页
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView menuIndex() {
		return super.getModelView("usermng/authCitizen");
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
	 * 查看用户身份认证
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
		String authStatus = request.getParameter("authStatus");
		String strStartTime = request.getParameter("strStartTime");
		String strEndTime = request.getParameter("strEndTime");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("authStatus", authStatus);
		map.put("startTime", strStartTime);
		map.put("endTime", strEndTime);
		
		/*验证居民用户账号是否存在和合法*/
		AuthCitizenDao authDao = sqlSession.getMapper(AuthCitizenDao.class);
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<CitizenAuthModel> msgList = authDao.queryListBy(map);
		PageInfo<CitizenAuthModel> pages = new PageInfo<CitizenAuthModel>(msgList);
		
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel("Y", "查看用户居住认证信息", String.valueOf(Logenum.QUERYLIST), request));
		
		jsonObj.put("total", pages.getTotal());
		jsonObj.put("rows", pages.getList());
		return jsonObj;
	}
	
	/**
	 * 查看最新import_date
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/maxImportDate")
	@ResponseBody
	public JSONObject maxImportDate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		AuthCitizenDao authDao = sqlSession.getMapper(AuthCitizenDao.class);
		String maxImportDate = authDao.selectMaxImportDate();
		jsonObj.put("maxImportDate", maxImportDate);
		return jsonObj;
		
	}
	
	/**
	 * 用户居住证认证
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/batchCitizenAuth")
	@ResponseBody
	@Transactional
	public JSONObject batchCitizenAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String authStartTime = request.getParameter("authStartTime");
		String authEndTime = request.getParameter("authEndTime");

		/*保存、验证、更新数据*/
		AuthCitizenDao authDao = sqlSession.getMapper(AuthCitizenDao.class);
		Map<String, Object> mParam = new HashMap<String, Object>();
		mParam.put("startTime", authStartTime);
		mParam.put("endTime", authEndTime);
		
		// 从 citizen_auth citizen_info 表里取出要进行居住证验证用户,并且身份证验证已通过的用户
		List<CitizenAuthModel> citizenInfoList = authDao.selectCitizenInfo(mParam);
		if(citizenInfoList == null || citizenInfoList.size() == 0) {
			jsonObj.put("message", "没有可以认证的居住证认证业务数据");
			return jsonObj;
		}
		
		jsonObj = citizenAuth(citizenInfoList);
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel("Y", "居住证认证", String.valueOf(Logenum.AUTH), request));
		
		return jsonObj;
	}
	
	/**
	 * 用户居住证认证
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/citizenAuth")
	@ResponseBody
	@Transactional
	public JSONObject citizenAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String authStartTime = request.getParameter("authStartTime");
		String authEndTime = request.getParameter("authEndTime");

		/*保存、验证、更新数据*/
		AuthCitizenDao authDao = sqlSession.getMapper(AuthCitizenDao.class);
		Map<String, Object> mParam = new HashMap<String, Object>();
		mParam.put("startTime", authStartTime);
		mParam.put("endTime", authEndTime);
		
		String authID = request.getParameter("authID");
		String[] iIdarry = authID.split(",");
		List<String> list = Arrays.asList(iIdarry);
		List<CitizenAuthModel> citizenInfoList = authDao.selectForCondition(list);
		if(citizenInfoList == null || citizenInfoList.size() == 0) {
			jsonObj.put("message", "没有可以认证的居住证认证业务数据");
			return jsonObj;
		}
		
		jsonObj = citizenAuth(citizenInfoList);
		
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel("Y", "居住证认证", String.valueOf(Logenum.AUTH), request));
		
		return jsonObj;
	}
	
	/**
	 * 居住证认证和居住批量认证公用方法
	 * @param citizenInfoList
	 * @return
	 */
	private JSONObject citizenAuth(List<CitizenAuthModel> citizenInfoList) {
		int succeedCount = 0;
		int failedCount = 0;
		int totalCount = citizenInfoList.size();
		String message = "";
		int result = 0;
		JSONObject jsonObj = new JSONObject();
		
		/*保存、验证、更新数据*/
		AuthCitizenDao authDao = sqlSession.getMapper(AuthCitizenDao.class);
		List<RcCardInfoModel> rcCardInfoList = null;
		
		List<String> listIdCardInfo = new ArrayList();
		for(CitizenAuthModel model : citizenInfoList) {
			listIdCardInfo.add(model.getIdCard());
		}
		
		try {
			//从rc_card_info表里搜出数据
			rcCardInfoList = authDao.selectRcCardInfoForPerson(listIdCardInfo);
		} catch (Exception e) {
			LOGGER.error(this, e);
			result = -1;
			message = "居住证认证失败：操作数据库异常！";
			jsonObj.put("message", "居住证认证失败：操作数据库异常！");
		}
		
		for(CitizenAuthModel citizen : citizenInfoList) {
			String citizenName = citizen.getCitizenName(); //用户名
			String rcCard = citizen.getRcCard(); // 用户居住证号码
			String citizenId = citizen.getCitizenID();
			
			if(citizenName != null && !"".equals(citizenName) &&
					rcCard != null && !"".equals(rcCard) &&
					citizenId != null && !"".equals(citizenId)) {
				
				//进行数据库交互
				//获取Spring容器的对象
		        WebApplicationContext contextLoader = ContextLoader.getCurrentWebApplicationContext();
		        //设置属性的默认属性
		        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		        //设置事务的传播行为，此处是设置为开启一个新事物
		        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		        //设置事务的隔离级别，此处是读已经提交
		        definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		        //从spring容器对象中获取DataSourceTransactionManager，这个根据配置文件中配置的id名（transactionManager）
		        DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) contextLoader.getBean("transactionManager");
				
				boolean flag = false;
				// 从rc_card_info表 里查居住证信息是否存在
				for(RcCardInfoModel rcCardInfo : rcCardInfoList) {
					//如果用户名和居住证号码 在 rc_card_info表存在， 居住证验证通过
					if(rcCard.equals(rcCardInfo.getRcCard()) &&
							citizenName.equals(rcCardInfo.getName())) {
						 //获取事务状态对象
				        TransactionStatus transactionStatus = (TransactionStatus) transactionManager.getTransaction(definition);
				        
				        try {
							CitizenAuthModel authmodel = new CitizenAuthModel();
							authmodel.setCitizenID(citizenId);
							authmodel.setAuthStatus(Constant.AUTH_STATUS_FINISHED); // 审核完成
							authmodel.setAuthResult("true"); // 审核成功 
							authmodel.setAuthResultMessage(Constant.MSG_AUTH_CITIZEN_SUCCEEDED);
							// Update citizen_auth
							authDao.updateCitizenAuth(authmodel);
							
							// Update citizen_user
							CitizenUserModel usermodel = new CitizenUserModel();
							usermodel.setCitizenId(citizenId);
							usermodel.setUserLevel(Constant.AUTH_LEVEL_CITIZEN); //居住证认证
							authDao.updateCitizenUser(usermodel);
							
							// Update citizen_identity
							rcCardInfo.setCitizenId(citizenId);
							authDao.updateCitizenIdentity(rcCardInfo);
							
							//发送消息通知用户
							CitizenMessageModel msgModel = new CitizenMessageModel();
							msgModel.setCitizenID(citizenId);
							msgModel.setMessageTitle(Constant.MSG_AUTH_NOTICE_TITLE); // 实名认证结果通知！
							msgModel.setMessageContent(Constant.MSG_AUTH_CITIZEN_NOTICE_SUCCEEDED); // 尊敬的用户，您好！您的居住证APP账号通过居住证认证成功。
							msgModel.setMessageStatus(Constant.MSG_STATUS_UNREAD); // 消息状态[0-未读]
							msgModel.setMessageType(Constant.MSG_TYPE_AUTH); // 消息类型[1-实名认证]
							NewsDao newsDao = sqlSession.getMapper(NewsDao.class);
							newsDao.insert(msgModel);
							
							flag = true; //该用户居住证信息信息存在
							succeedCount++;
							
							//提交事务
					        transactionManager.commit(transactionStatus);
				        } catch (Exception e) {
							LOGGER.error(this, e);
							result = -1;
							message = "居住证认证失败：操作数据库异常！";
							jsonObj.put("message", "居住证认证失败：操作数据库异常！");
							//事务回滚
							transactionManager.rollback(transactionStatus);
						}
					} 
				} 
				if(flag == false) {
					
					//获取事务状态对象
			        TransactionStatus transactionStatus = (TransactionStatus) transactionManager.getTransaction(definition);
					try {
						if(longTimeWaitingAuth(citizen.getAuthTime())) {
							CitizenAuthModel authmodel = new CitizenAuthModel();
							authmodel.setCitizenID(citizenId);
							authmodel.setAuthResult("false"); // 审核失败
							authmodel.setAuthResultMessage(Constant.MSG_AUTH_CITIZEN_FAILED);
							// Update citizen_auth
							authDao.updateCitizenAuth(authmodel);
							
							//发送消息通知用户
							CitizenMessageModel msgModel = new CitizenMessageModel();
							msgModel.setCitizenID(citizenId);
							msgModel.setMessageTitle(Constant.MSG_AUTH_NOTICE_TITLE); // 实名认证结果通知！
							msgModel.setMessageContent(Constant.MSG_AUTH_CITIZEN_NOTICE_FAILED); // 尊敬的用户，您好！您的居住证APP账号通过居住证认证失败。
							msgModel.setMessageStatus(Constant.MSG_STATUS_UNREAD); // 消息状态[0-未读]
							msgModel.setMessageType(Constant.MSG_TYPE_AUTH); // 消息类型[1-实名认证]
							NewsDao newsDao = sqlSession.getMapper(NewsDao.class);
							newsDao.insert(msgModel);
							
							//提交事务
					        transactionManager.commit(transactionStatus);
						} else {
							CitizenAuthModel authmodel = new CitizenAuthModel();
							authmodel.setCitizenID(citizenId);
							authmodel.setAuthStatus(Constant.AUTH_STATUS_CHECKING); // 用户认证状态[2-审核中]
							authmodel.setAuthResult("false"); // 审核失败
							authDao.updateCitizenAuth(authmodel);
							//提交事务
					        transactionManager.commit(transactionStatus);
						}
						
						failedCount++;
					} catch (Exception ex) {
						LOGGER.error(this, ex);
						failedCount++;
						result = -1;
						message = "居住证认证失败：操作数据库异常！";
						jsonObj.put("message", "居住证认证失败：操作数据库异常！");
						//事务回滚
						transactionManager.rollback(transactionStatus);
					}
				}
			} else {
				failedCount++;
				continue;
			}
		}
		result = 0;
		message =  message + "<br> 认证总数 " + totalCount + " 个，认证成功 " + succeedCount + " 个， 认证失败 " + failedCount + " 个。";
		
//		message = "居住证认证失败";
//		message = "认证总数 " + totalCount + " 个，认证成功 " + succeedCount + " 个， 认证失败 " + failedCount + " 个。";
		jsonObj.put("result", result);
		jsonObj.put("message", message);
		return jsonObj;
	}
	
	/**
	 * 用户等待认证的天数
	 * @param authDate
	 * @return
	 * @throws Exception
	 */
	private boolean longTimeWaitingAuth(String authDate) throws Exception{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date currentTime = new Date(System.currentTimeMillis());
		String currentDate = formatter.format(currentTime);
		
		long ti = formatter.parse(currentDate).getTime() - formatter.parse(authDate).getTime();
		long day = ti / (1000 * 60 * 60 * 24);
		
		if(day >= Constant.AUTH_WAITING_DAYS) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 居住证认证临时处理程序
	 * APPv1.0.0实名认证用户需要分别进行身份证认证和居住证认证，
	 * APPv1.0.1用户只需要进行身份证认证，后台自动匹配其居住证信息，
	 * 该程序对v1.0.0版本只申请了身份证认证但未申请居住证认证的用户进行手动居住证认证。
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/citizenAuthProvisional")
	@ResponseBody
	@Transactional
	public JSONObject citizenAuthProvisional(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		AuthCitizenDao authCitizenDao = sqlSession.getMapper(AuthCitizenDao.class);
		List<CitizenAuthModel> CitizenList = null;
		
		try {
			//查询实名等级为‘身份证认证’的用户信息
			CitizenList = authCitizenDao.selectForIdAuthLevalCitizenList();
			if(CitizenList.size()==0) {
				jsonObj.put("result", 0);
				jsonObj.put("message", "本次操作0条记录操作成功！");
				return jsonObj;
			}			
			//判断citizen_auth是否有用户的居住证认证业务信息，若没有则插入业务信息
			for(CitizenAuthModel cm : CitizenList) {
				List<CitizenAuthModel> authModelList = authCitizenDao.queryAuthInfo(cm.getCitizenID(), 2); 
				if(authModelList.size() == 0) {
					CitizenAuthModel authModel = new CitizenAuthModel();
					authModel.setCitizenID(cm.getCitizenID());
					authModel.setAuthType(Constant.RC_AUTH); //2-居住证认证
					authModel.setAuthStatus(Constant.AUTH_STATUS_WAITING);
					authCitizenDao.insertAuth(authModel);
				}
			}
			//调用居住证认证函数进行批量居住证认证
			jsonObj = citizenAuthProvisional(CitizenList);		
		} catch (Exception e) {
			LOGGER.error(this, e);
			jsonObj.put("result", -1);
			jsonObj.put("message", "居住证认证失败：操作数据库异常！");
			return jsonObj;
		}
		
		return jsonObj;
	}
	
	/**
	 * 居住证认证临时处理程序
	 * @param citizenInfoList
	 * @return
	 */
	private JSONObject citizenAuthProvisional(List<CitizenAuthModel> citizenInfoList) {
		int succeedCount = 0;
		int failedCount = 0;
		int totalCount = citizenInfoList.size();
		String message = "";
		int result = 0;
		JSONObject jsonObj = new JSONObject();
		
		/*保存、验证、更新数据*/
		AuthCitizenDao authDao = sqlSession.getMapper(AuthCitizenDao.class);
		List<RcCardInfoModel> rcCardInfoList = null;
		
		List<String> listIdCardInfo = new ArrayList();
		for(CitizenAuthModel model : citizenInfoList) {
			listIdCardInfo.add(model.getIdCard());
		}
		
		try {
			//从rc_card_info表里搜出数据
			rcCardInfoList = authDao.selectRcCardInfoForPerson(listIdCardInfo);
		} catch (Exception e) {
			LOGGER.error(this, e);
			result = -1;
			message = "居住证认证失败：操作数据库异常！";
			jsonObj.put("message", "居住证认证失败：操作数据库异常！");
		}
		
		for(CitizenAuthModel citizen : citizenInfoList) {
			String citizenName = citizen.getCitizenName(); //用户名
			String idCard = citizen.getIdCard(); // 用户居住证号码
			String citizenId = citizen.getCitizenID();
			
			if(citizenName != null && !"".equals(citizenName) &&
					idCard != null && !"".equals(idCard) &&
					citizenId != null && !"".equals(citizenId)) {
				
				//进行数据库交互
				//获取Spring容器的对象
		        WebApplicationContext contextLoader = ContextLoader.getCurrentWebApplicationContext();
		        //设置属性的默认属性
		        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		        //设置事务的传播行为，此处是设置为开启一个新事物
		        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		        //设置事务的隔离级别，此处是读已经提交
		        definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		        //从spring容器对象中获取DataSourceTransactionManager，这个根据配置文件中配置的id名（transactionManager）
		        DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) contextLoader.getBean("transactionManager");
				
				boolean flag = false;
				// 从rc_card_info表 里查居住证信息是否存在
				for(RcCardInfoModel rcCardInfo : rcCardInfoList) {
					//如果用户名和居住证号码 在 rc_card_info表存在， 居住证验证通过
					if(idCard.equals(rcCardInfo.getIdCard()) &&
							citizenName.equals(rcCardInfo.getName())) {
						 //获取事务状态对象
				        TransactionStatus transactionStatus = (TransactionStatus) transactionManager.getTransaction(definition);
				        
				        try {
							CitizenAuthModel authmodel = new CitizenAuthModel();
							authmodel.setCitizenID(citizenId);
							authmodel.setAuthStatus(Constant.AUTH_STATUS_FINISHED); // 审核完成
							authmodel.setAuthResult("true"); // 审核成功 
							authmodel.setAuthResultMessage(Constant.MSG_AUTH_CITIZEN_SUCCEEDED);
							// Update citizen_auth
							authDao.updateCitizenAuth(authmodel);
							
							// Update citizen_user
							CitizenUserModel usermodel = new CitizenUserModel();
							usermodel.setCitizenId(citizenId);
							usermodel.setUserLevel(Constant.AUTH_LEVEL_CITIZEN); //居住证认证
							authDao.updateCitizenUser(usermodel);
							
							// Update citizen_identity
							rcCardInfo.setCitizenId(citizenId);
							authDao.updateCitizenIdentity(rcCardInfo);
							
							//发送消息通知用户
							CitizenMessageModel msgModel = new CitizenMessageModel();
							msgModel.setCitizenID(citizenId);
							msgModel.setMessageTitle(Constant.MSG_AUTH_NOTICE_TITLE); // 实名认证结果通知！
							msgModel.setMessageContent(Constant.MSG_AUTH_CITIZEN_NOTICE_SUCCEEDED); // 尊敬的用户，您好！您的居住证APP账号通过居住证认证成功。
							msgModel.setMessageStatus(Constant.MSG_STATUS_UNREAD); // 消息状态[0-未读]
							msgModel.setMessageType(Constant.MSG_TYPE_AUTH); // 消息类型[1-实名认证]
							NewsDao newsDao = sqlSession.getMapper(NewsDao.class);
							newsDao.insert(msgModel);
							
							flag = true; //该用户居住证信息信息存在
							succeedCount++;
							
							//提交事务
					        transactionManager.commit(transactionStatus);
				        } catch (Exception e) {
							LOGGER.error(this, e);
							result = -1;
							message = "居住证认证失败：操作数据库异常！";
							jsonObj.put("message", "居住证认证失败：操作数据库异常！");
							//事务回滚
							transactionManager.rollback(transactionStatus);
						}
					} 
				} 
				if(flag == false) {
					
					//获取事务状态对象
			        TransactionStatus transactionStatus = (TransactionStatus) transactionManager.getTransaction(definition);
					try {
						CitizenAuthModel authmodel = new CitizenAuthModel();
						authmodel.setCitizenID(citizenId);
						authmodel.setAuthResult("false"); // 审核失败
						authmodel.setAuthResultMessage(Constant.MSG_AUTH_CITIZEN_FAILED);
						// Update citizen_auth
						authDao.updateCitizenAuth(authmodel);
						
						//发送消息通知用户
						CitizenMessageModel msgModel = new CitizenMessageModel();
						msgModel.setCitizenID(citizenId);
						msgModel.setMessageTitle(Constant.MSG_AUTH_NOTICE_TITLE); // 实名认证结果通知！
						msgModel.setMessageContent(Constant.MSG_AUTH_CITIZEN_NOTICE_FAILED); // 尊敬的用户，您好！您的居住证APP账号通过居住证认证失败。
						msgModel.setMessageStatus(Constant.MSG_STATUS_UNREAD); // 消息状态[0-未读]
						msgModel.setMessageType(Constant.MSG_TYPE_AUTH); // 消息类型[1-实名认证]
						NewsDao newsDao = sqlSession.getMapper(NewsDao.class);
						newsDao.insert(msgModel);
						
						//提交事务
				        transactionManager.commit(transactionStatus);
								
						failedCount++;
					} catch (Exception ex) {
						LOGGER.error(this, ex);
						failedCount++;
						result = -1;
						message = "居住证认证失败：操作数据库异常！";
						jsonObj.put("message", "居住证认证失败：操作数据库异常！");
						//事务回滚
						transactionManager.rollback(transactionStatus);
					}
				}
			} else {
				failedCount++;
				continue;
			}
		}
		result = 0;
		message =  message + "<br> 认证总数 " + totalCount + " 个，认证成功 " + succeedCount + " 个， 认证失败 " + failedCount + " 个。";
		
		jsonObj.put("result", result);
		jsonObj.put("message", message);
		return jsonObj;
	}
}