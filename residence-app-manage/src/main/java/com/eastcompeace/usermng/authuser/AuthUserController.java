package com.eastcompeace.usermng.authuser;

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
@RequestMapping("/userAuthctrl")
public class AuthUserController extends BaseController{

	private static Log LOGGER = LogFactory.getLog(AuthUserController.class);
	
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
		return super.getModelView("usermng/authUser");
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
		AuthUserDao authDao = sqlSession.getMapper(AuthUserDao.class);
		PageHelper.startPage(Integer.parseInt(strPage),Integer.parseInt(strRows));
		List<CitizenAuthModel> msgList = authDao.queryListBy(map);
		PageInfo<CitizenAuthModel> pages = new PageInfo<CitizenAuthModel>(msgList);
		
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel("Y", "查看用户身份认证信息", String.valueOf(Logenum.QUERYLIST), request));
		
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
		AuthUserDao authDao = sqlSession.getMapper(AuthUserDao.class);
		String maxImportDate = authDao.selectMaxImportDate();
		jsonObj.put("maxImportDate", maxImportDate);
		return jsonObj;
		
	}
	
	/**
	 * 用户身份批量认证
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/batchIdAuth")
	@ResponseBody
	@Transactional
	public JSONObject batchIdAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String authStartTime = request.getParameter("authStartTime");
		String authEndTime = request.getParameter("authEndTime");

		/*保存、验证、更新数据*/
		AuthUserDao authDao = sqlSession.getMapper(AuthUserDao.class);
		Map<String, Object> mParam = new HashMap<String, Object>();
		mParam.put("startTime", authStartTime);
		mParam.put("endTime", authEndTime);
		
		// 从 citizen_auth citizen_info 表里取出身份证验证用户
		List<CitizenAuthModel> idInfoList = authDao.selectUserInfo(mParam);
		
		if(idInfoList == null || idInfoList.size() == 0) {
			jsonObj.put("message", "没有可以认证的身份证认证业务数据");
			return jsonObj;
		}
		
		jsonObj = idAuth(idInfoList);
		
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel("Y", "居住证认证", String.valueOf(Logenum.AUTH), request));
		
		return jsonObj;
	}
	
	
	/**
	 * 用户身份认证
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/idAuth")
	@ResponseBody
	@Transactional
	public JSONObject userAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String authStartTime = request.getParameter("authStartTime");
		String authEndTime = request.getParameter("authEndTime");

		/*保存、验证、更新数据*/
		AuthUserDao authDao = sqlSession.getMapper(AuthUserDao.class);
		Map<String, Object> mParam = new HashMap<String, Object>();
		mParam.put("startTime", authStartTime);
		mParam.put("endTime", authEndTime);
		
		String authID = request.getParameter("authID");
		String[] iIdarry = authID.split(",");
		List<String> list = Arrays.asList(iIdarry);
		
		List<CitizenAuthModel> idInfoList = authDao.selectForCondition(list);
		if(idInfoList == null || idInfoList.size() == 0) {
			jsonObj.put("message", "没有可以认证的身份证认证业务数据");
			return jsonObj;
		}
		
		jsonObj = idAuth(idInfoList);
		
		/** 日志 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel("Y", "居住证认证", String.valueOf(Logenum.AUTH), request));
		
		return jsonObj;
	}
	
	/** 
	 * 身份认证和身份批量认证公用方法
	 * @param idInfoList
	 * @return
	 */
	private JSONObject idAuth(List<CitizenAuthModel> idInfoList) {
		
		int succeedCount = 0;
		int failedCount = 0;
		int totalCount = idInfoList.size();
		String message = "";
		int result = 0;
		JSONObject jsonObj = new JSONObject();
		
		AuthUserDao authDao = sqlSession.getMapper(AuthUserDao.class);
		// 从 citizen_auth citizen_info 表里取出身份证验证用户
		
		if(idInfoList == null || idInfoList.size() == 0) {
			jsonObj.put("message", "没有可以认证的身份证认证业务数据");
			return jsonObj;
		}
		
		List<String> listIdCardInfo = new ArrayList();
		for(CitizenAuthModel model : idInfoList) {
			listIdCardInfo.add(model.getIdCard());
		}
		
		List<RcCardInfoModel> rcCardInfoList = null;
		try {
			//从rc_card_info表里搜出数据
			rcCardInfoList = authDao.selectRcCardInfoForPerson(listIdCardInfo);
		} catch (Exception e) {
			LOGGER.error(this, e);
			result = -1;
			message = "身份证认证失败：操作数据库异常！";
			jsonObj.put("message", "身份证认证失败：操作数据库异常！");
		}
		
		for(CitizenAuthModel citizen : idInfoList) {
			String citizenName = citizen.getCitizenName(); //用户名
			String idCard = citizen.getIdCard(); // 用户身份证号码
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
				// 从rc_card_info表 里查身份证信息是否存在
				for(RcCardInfoModel rcCardInfo : rcCardInfoList) {
					//如果用户名和身份证号码 在 rc_card_info表存在， 身份证验证通过
					if(idCard.equals(rcCardInfo.getIdCard()) &&
							citizenName.equals(rcCardInfo.getName())) {
				        //获取事务状态对象
				        TransactionStatus transactionStatus = (TransactionStatus) transactionManager.getTransaction(definition);
						
						try {
						
							CitizenAuthModel authmodel = new CitizenAuthModel();
							authmodel.setCitizenID(citizenId);
							authmodel.setAuthStatus(Constant.AUTH_STATUS_FINISHED); // 审核完成
							authmodel.setAuthResult("true"); // 审核成功 
							authmodel.setAuthResultMessage(Constant.MSG_AUTH_ID_SUCCEEDED); // 身份证认证成功
							// Update citizen_auth
							authDao.updateCitizenAuth(authmodel);
							
							// Update citizen_user
							CitizenUserModel usermodel = new CitizenUserModel();
							usermodel.setCitizenId(citizenId);
							usermodel.setUserLevel(Constant.AUTH_LEVEL_ID); //身份证认证
							authDao.updateCitizenUser(usermodel);
			
							//发送消息通知用户
							CitizenMessageModel msgModel = new CitizenMessageModel();
							msgModel.setCitizenID(citizenId);
							msgModel.setMessageTitle(Constant.MSG_AUTH_NOTICE_TITLE); // 实名认证结果通知！
							msgModel.setMessageContent(Constant.MSG_AUTH_ID_NOTICE_SUCCEEDED); // 尊敬的用户，您好！您的身份证APP账号通过居住证认证成功。
							msgModel.setMessageStatus(Constant.MSG_STATUS_UNREAD); // 消息状态[0-未读]
							msgModel.setMessageType(Constant.MSG_TYPE_AUTH); // 消息类型[1-实名认证]
							NewsDao newsDao = sqlSession.getMapper(NewsDao.class);
							newsDao.insert(msgModel);
							
							flag = true; //该用户身份证信息信息存在
							succeedCount++;
//							jsonObj.put("message", "身份证认证成功");
							//提交事务
					        transactionManager.commit(transactionStatus);
						} catch (Exception e) {
							LOGGER.error(this, e);
							result = -1;
							message = "身份证认证失败：操作数据库异常！";
							jsonObj.put("message", "身份证认证失败：操作数据库异常！");
							//事务回滚
							transactionManager.rollback(transactionStatus);
						}
					} 
				} 
				if(flag == false) {
					//获取事务状态对象
			        TransactionStatus transactionStatus = (TransactionStatus) transactionManager.getTransaction(definition);
					try {
						// Update citizen_auth
						if(longTimeWaitingAuth(citizen.getAuthTime())) {
							CitizenAuthModel authmodel = new CitizenAuthModel();
							authmodel.setCitizenID(citizenId);
							//authmodel.setAuthStatus("1"); 
							authmodel.setAuthResult("false"); // 审核失败
							authmodel.setAuthResultMessage(Constant.MSG_AUTH_ID_FAILED); // 身份证认证失败
							authDao.updateCitizenAuth(authmodel);
							
							//发送消息通知用户
							CitizenMessageModel msgModel = new CitizenMessageModel();
							msgModel.setCitizenID(citizenId);
							msgModel.setMessageTitle(Constant.MSG_AUTH_NOTICE_TITLE); // 实名认证结果通知！
							msgModel.setMessageContent(Constant.MSG_AUTH_ID_NOTICE_FAILED); // 尊敬的用户，您好！您的身份证APP账号通过身份证认证失败。。
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
						//jsonObj.put("message", "身份证认证失败");
					} catch (Exception ex) {
						LOGGER.error(this, ex);
						failedCount++;
						result = -1;
						message = "身份证认证失败：操作数据库异常！";
						jsonObj.put("message", "身份证证失败：操作数据库异常！");
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
		message = message + "<br> 认证总数 " + totalCount + " 个，认证成功 " + succeedCount + " 个， 认证失败 " + failedCount + " 个。";
		
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
	
//	public static void main(String[] args) throws Exception {
//		String authDate = "2017-03-01";
//		
//		System.out.println(longTimeWaitingAuth(authDate));
//	}
	
}