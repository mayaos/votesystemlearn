package com.eastcompeace.system.user;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.Constant;
import com.eastcompeace.base.LogType.Logenum;
import com.eastcompeace.model.AccountSmsCodeModel;
import com.eastcompeace.model.ButtonModel;
import com.eastcompeace.model.DivisionsUserModel;
import com.eastcompeace.model.MenuModel;
import com.eastcompeace.model.UserCommModel;
import com.eastcompeace.model.UserModel;
import com.eastcompeace.model.UserRolesModel;
import com.eastcompeace.share.cipher.MD5Cipher;
import com.eastcompeace.share.utils.ByteUtils;
import com.eastcompeace.share.utils.ToolKitUtils;
import com.eastcompeace.system.log.LogDao;
import com.eastcompeace.system.menu.MenuDao;
import com.eastcompeace.util.BuildModel;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.UuidUtil;
import com.eastcompeace.util.sms.SmsClient;
import com.eastcompeace.util.sms.TplidEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 用户管理类（用户登录、增、删、改、查功能）
 * 
 * @author caobo
 * 
 */
@Controller
@RequestMapping("/userctrl")
public class UserController extends BaseController {

	@Resource
	private SqlSession sqlSession;

	/*
	 * ==========================登录功能
	 * start======================================================
	 */
	/**
	 * 用户登录功能实现
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/login")
	@ResponseBody
	public JSONObject login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String uName = request.getParameter("l_username");
		String uPass = request.getParameter("l_password");
		String uCode = request.getParameter("l_validateCode");
		HttpSession session = request.getSession();
		String sessCheckCode = (String) session.getAttribute(Constant.SESSION_CHECK_CODE);

		JSONObject jsonObj = new JSONObject();
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		
		if (!uCode.equalsIgnoreCase(sessCheckCode)) {
			jsonObj.put("result", -1);
			jsonObj.put("message", "验证码不正确");
			return jsonObj;
		}
		
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		UserModel userModel = userDao.queryUserInfo(uName, uPass);
		if (userModel == null) { // 判断用户名密码是否正确
			jsonObj.put("result", -1);
			jsonObj.put("message", "用户名或密码不正确！");
			return jsonObj;
		}
		
		String pwdvaildydate = userModel.getPwdvaildTime();
		if(null == pwdvaildydate || "".equals(pwdvaildydate)){
			jsonObj.put("result", -1);
			jsonObj.put("message", "密码已过期！");
			return jsonObj;
		}
		
		Date date = DateUtils.getDate(pwdvaildydate);
		boolean b = DateUtils.compare2Day(new Date(), date);
		if(!b){
			jsonObj.put("result", -1);
			jsonObj.put("message", "密码已过期！");
			return jsonObj;
		}
		
		List<UserRolesModel> list = userDao.queryUserroleslistById(userModel.getUserId() + "");
		if (null == list || list.size() < 1) {
			jsonObj.put("result", -1);
			jsonObj.put("message", "无效账号，请联系管理员配置角色权限！");
			return jsonObj;
		}
		if (userModel.getPwdinitial().equals("YES")) { // 判断是否原始密码登录
			jsonObj.put("result", 2);
			jsonObj.put("message", "请修改原始密码后再登陆！");
			return jsonObj;
		}

		/** 把权限放到session */
		session.setAttribute(Constant.SESSION_ROLEID, list.get(0).getRoleId());
		
		//判断用户是否绑定手机号如果未绑定则进入返回响应代码3：绑定手机号
		if (userModel.getUserMobile() == null || "".equals(userModel.getUserMobile().trim())) { 
			List<MenuModel> menuidslist = userDao.queryUserMenuids(userModel.getUserId());
			session.setAttribute(Constant.SESSION_POWER, menuidslist);
			session.setAttribute(Constant.SESSION_USER, userModel);
			
			UserModel upUser = new UserModel();
			upUser.setUserId(userModel.getUserId());
			upUser.setLastloginTime("Yes");
			upUser.setLastloginIp(request.getRemoteAddr());
			userDao.updateUser(upUser);		
			
			jsonObj.put("result", 3);
			jsonObj.put("message", "请绑定手机号！");
			return jsonObj;
		}
		
		//判断是否需要短信验证登录，如果需要则进入短信验证步骤
		if("YES".equals(userModel.getIsSmsVerify())){
			jsonObj.put("result", 1);
			jsonObj.put("message", "请进行短信验证！");
			return jsonObj;
		}else{
			// 登录成功
			List<MenuModel> menuidslist = userDao.queryUserMenuids(userModel.getUserId());
			session.setAttribute(Constant.SESSION_POWER, menuidslist);
			session.setAttribute(Constant.SESSION_USER, userModel);
			
			UserModel upUser = new UserModel();
			upUser.setUserId(userModel.getUserId());
			upUser.setLastloginTime("Yes");
			upUser.setLastloginIp(request.getRemoteAddr());
			userDao.updateUser(upUser);
			
			jsonObj.put("result", 0);
			jsonObj.put("message", "登录成功");
		}
		logDao.add(BuildModel.getModel( "Y", "登录成功", String.valueOf(Logenum.LOGIN), userModel.getUserId(), request));
		
		return jsonObj;
	}

	/**
	 * 用户登出
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/logout")
	@ResponseBody
	public JSONObject logOut(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();

		HttpSession session = request.getSession();

		if (session == null) {
			jsonObj.put("result", 0);
			jsonObj.put("message", "登出成功");
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "N", "登出", String.valueOf(Logenum.LOGOUT), request));
			/**
			 * 日志end
			 */

		} else {
			jsonObj.put("result", 0);
			jsonObj.put("message", "登出成功");
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "Y", "登出", String.valueOf(Logenum.LOGOUT), request));
			/**
			 * 日志end
			 */
			session.invalidate();
		}

		return jsonObj;
	}

	/**
	 * 用户登录成功后获取用户信息功能
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loginfo")
	@ResponseBody
	public JSONObject getLoginfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();

		HttpSession session = request.getSession();

		if (session == null) {
			jsonObj.put("result", 1);
		} else {
			UserModel userMo = (UserModel) session.getAttribute(Constant.SESSION_USER);
			if (userMo == null) {
				jsonObj.put("result", 1);
				jsonObj.put("message", "session invalid, please login again!");
			} else {
				jsonObj.put("result", 0);
				jsonObj.put("userID", userMo.getUserId());
				jsonObj.put("userCode", userMo.getUserCode());
				jsonObj.put("userName", userMo.getUserName());
				jsonObj.put("messageCount", 0);
			}
		}

		return jsonObj;
	}
	
	/**
	 * 发送短信
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sendSms")
	@ResponseBody
	public JSONObject sendSms(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userMobile = request.getParameter("userMobile");
		String strRand = ToolKitUtils.randomNumber(6);
		JSONObject jsonObj = new JSONObject();
		
 
		//存储手机验证码到数据库
		AccountSmsCodeModel smsCode = new AccountSmsCodeModel();
		smsCode.setSmsCode(strRand);
		smsCode.setUserName(userMobile);
		
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		try {
			//清除过期验证码
			userDao.deleteSmsCode();
			//存储新生成的手机验证码
			userDao.saveSmsCode(smsCode);		
		} catch (Exception e) {
			jsonObj.put("result", -1);
			jsonObj.put("message", "数据库操作失败");
			return jsonObj;
		}
		
		//发密码短信
		SmsClient sms = new SmsClient();
		String smsContent = "您的验证码是：" + strRand + "。30分钟内有效，请不要把验证码泄露给其他人。如非本人操作，可不用理会！";
		int i = sms.sendSms(sms.SMS_TYPE_IDENTIFY, TplidEnum.REGISTER,userMobile, strRand);
		if(i!=0){
			jsonObj.put("result", -1);
			jsonObj.put("message", "短信发送失败，请稍后再试");
			return jsonObj;
		}
			
		jsonObj.put("result", 0);
		jsonObj.put("message", "短信发送成功！");
        return jsonObj;
	}
	
	/**
	 * 验证短信
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/verifySms")
	@ResponseBody
	public JSONObject verifySms(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String smsCode = request.getParameter("smsCode");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		
		HttpSession session = request.getSession();
		JSONObject jsonObj = new JSONObject();
	
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		UserModel userModel = userDao.queryUserInfo(userName, password);
		if(userModel == null){
			jsonObj.put("result", -1);
			jsonObj.put("message", "账号验证失败！");
			return jsonObj;	
		}

		String vCode = userDao.querySmsCode(userModel.getUserMobile());
		if(vCode == null || !smsCode.equals(vCode)){
			jsonObj.put("result", -1);
			jsonObj.put("message", "验证码错误或已过期，请输入正确的验证码！");
			return jsonObj;	
		}
		
		// 登录成功
		List<MenuModel> menuidslist = userDao.queryUserMenuids(userModel.getUserId());
		session.setAttribute(Constant.SESSION_POWER, menuidslist);
		session.setAttribute(Constant.SESSION_USER, userModel);
					
		jsonObj.put("result", 0);
		return jsonObj;
	}
	/*
	 * ==========================登录功能
	 * start====================================================
	 */

	/*
	 * ==========================用户管理功能
	 * start=====================================================
	 */

	/**
	 * 用户管理基础页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/showindex")
	public ModelAndView menuIndex() {
		return super.getModelView("common/user/");
	}


	/**
	 * 查询用户列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/userlist")
	@ResponseBody
	public JSONObject getUserList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();

		String rows = request.getParameter("rows");
		String page = request.getParameter("page");

		String userCode = request.getParameter("s_user_code");
		String userName = request.getParameter("s_user_name");
		String userIsfrozen = request.getParameter("l_user_isfrozen");
		String userTimeStart = request.getParameter("d_createtime_start");
		String userTimeEnd = request.getParameter("d_createtime_end");

		UserModel user = new UserModel();
		user.setUserCode(userCode);
		user.setUserName(userName);
		user.setIsFrozen(userIsfrozen);
		if (StringUtils.isNotBlank(userTimeStart)) {
			user.setCreatetimeStart(userTimeStart.replace("-", ""));
		}
		if (StringUtils.isNotBlank(userTimeEnd)) {
			user.setCreatetimeEnd(userTimeEnd.replace("-", ""));
		}

		UserDao userDao = sqlSession.getMapper(UserDao.class);
		
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		List<UserModel> userlist = userDao.queryUserList2(user);
		PageInfo<UserModel> pageinfo = new PageInfo<UserModel>(userlist);

		jsonObj.put("total", pageinfo.getTotal());
		jsonObj.put("rows", pageinfo.getList());
		/**
		 * 日志start
		 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel( "Y", "查询用户分页", String.valueOf(Logenum.QUERYLIST), request));
		/**
		 * 日志end
		 */
		return jsonObj;
	}
	
	/**
	 * 根据用户Id删除用户信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteuser")
	@ResponseBody
	public JSONObject deleteUserInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();

		UserDao userDao = sqlSession.getMapper(UserDao.class);
		String strIds = request.getParameter("d_userIds");
		String[] userIds = strIds.split(",");
		int idLen = userIds.length;

		try {
			for (int i = 0; i < idLen; i++) {
				
				// 第一步，删除用户和角色映射表中相关数据
				userDao.delUserRolesInfo(userIds[i]);
				// 第三步，删除用户信息
				userDao.deleteUser(userIds[i]);
			}
			jsonObj.put("result", 0);
			jsonObj.put("message", "用户信息删除成功");
			/** 日志 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "Y", "删除用户", String.valueOf(Logenum.DELETEINFO), request));
			
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObj.put("result", 1);
			jsonObj.put("message", "用户信息删除失败，稍后请重试");
			/** 日志 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "N", "删除用户", String.valueOf(Logenum.DELETEINFO), request));
			return jsonObj;
		}
		return jsonObj;
	}

//	/**
//	 * 根据ID注销用户信息
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("/canceluser")
//	@ResponseBody
//	public JSONObject cancelUserInfo(HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		JSONObject jsonObj = new JSONObject();
//
//		UserDao userDao = sqlSession.getMapper(UserDao.class);
//		String strIds = request.getParameter("d_userIds");
//		String[] userIds = strIds.split(",");
//		int idLen = userIds.length;
//
//		try {
//			for (int i = 0; i < idLen; i++) {
//				// 第一步，更新用户表中相关数据
//				userDao.updateUserby(userIds[i]);
//				// 第二步，删除用户和角色映射表中相关数据
//				userDao.delUserRolesInfo(userIds[i]);
//			}
//			jsonObj.put("result", 0);
//			jsonObj.put("message", "用户信息注销成功");
//			/** 日志 */
//			LogDao logDao = sqlSession.getMapper(LogDao.class);
//			logDao.add(BuildModel.getModel( "Y", "注销用户", String.valueOf(Logenum.DELETEINFO), request));
//			
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			jsonObj.put("result", 1);
//			jsonObj.put("message", "用户信息注销失败，稍后请重试");
//			/** 日志 */
//			LogDao logDao = sqlSession.getMapper(LogDao.class);
//			logDao.add(BuildModel.getModel( "N", "注销用户", String.valueOf(Logenum.DELETEINFO), request));
//			return jsonObj;
//		}
//		return jsonObj;
//	}

	/**
	 * 保存用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/adduserinfo")
	@ResponseBody
	public JSONObject addUserInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		DivisionUserDao divisionUserDao = sqlSession.getMapper(DivisionUserDao.class);
		JSONObject jsonObj = new JSONObject();
		String userCode = request.getParameter("s_usercode");
		String userName = request.getParameter("s_username");
		String userPassword = ByteUtils.toHexString(MD5Cipher.encrypt(request.getParameter("s_password").getBytes()));
		String sex = request.getParameter("s_sex");
		String idCard = request.getParameter("s_idcard");
		String sPwdvaildtime = request.getParameter("s_pwdvaildtime");
		String StrEmail = request.getParameter("s_email");
		String roleUuid = request.getParameter("s_roleUuid");
		String isSmsVerify = request.getParameter("s_isSmsVerify");
		String uMobile = request.getParameter("s_mobile");
		String userBirthday = request.getParameter("s_birthdate");
		String areaId = request.getParameter("s_areaId");
		
		//判断是否存在该用户名
		UserModel queryUser = new UserModel();
		queryUser.setUserCode(userCode);
		UserModel  um = userDao.queryUser(queryUser);
		if(null!=um){
			jsonObj.put("result", 1);
			jsonObj.put("message", "已存在该用户名");
			return jsonObj;
		}
		UserModel user = new UserModel();
		user.setUserCode(userCode);
		user.setUserName(userName);
		user.setUserPassword(userPassword);
		user.setUserSex(sex);
		user.setUserIdcard(idCard);
		user.setPwdvaildTime(sPwdvaildtime);
		user.setUserEmail(StrEmail);
		user.setIsSmsVerify(isSmsVerify);
		user.setUserMobile(uMobile);
		user.setUserBirthdate(userBirthday);
		
		try {
			userDao.insertUser(user);
			UserModel umodel = userDao.queryUser(user); //获取user_id
			int userid = umodel.getUserId();
			
			DivisionsUserModel divisionUser = new DivisionsUserModel();
			divisionUser.setUserId(userid);
			divisionUser.setUserCode(userCode);
			divisionUser.setDivisionsId(areaId);
			divisionUserDao.add(divisionUser); //把信息插入 divisions_user_mapping 表
			
			UserCommModel ucm = new UserCommModel();
			ucm.setUuid(UuidUtil.getUuid());
			ucm.setUserid(userid+"");
			
			userDao.delUserRolesInfo(userid+"");
			userDao.addUserRoleInfo(userid+"", roleUuid);
			
			jsonObj.put("result", 0);
			jsonObj.put("message", "用户信息保存成功");
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "Y", "添加用户", String.valueOf(Logenum.ADDINFO), request));
			/**
			 * 日志end
			 */
			return jsonObj;
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObj.put("result", 1);
			jsonObj.put("message", "用户信息保存失败");
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "N", "添加用户", String.valueOf(Logenum.ADDINFO), request));
			/**
			 * 日志end
			 */
			return jsonObj;
		}
	}

	/**
	 * 修改用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updatuserinfo")
	@ResponseBody
	public JSONObject updateUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		// uId为user对象主键字段，js中内部传递，一定不为空
		String uId = request.getParameter("u_id");
		String userCode = request.getParameter("u_usercode");
		String userName = request.getParameter("u_username");
		String roleUuid = request.getParameter("s_roleUuid");
		String isFrozen = request.getParameter("u_isFrozen");
		String sex = request.getParameter("u_sex");
		String idCard = request.getParameter("u_idcard");
		String sPwdvaildtime = request.getParameter("u_pwdvaildtime");
		String userBirthday = request.getParameter("u_birthdate");
		String uMobile = request.getParameter("u_mobile");
		String uEmail = request.getParameter("u_email");
		String isSmsVerify = request.getParameter("u_isSmsVerify");
		String areaId = request.getParameter("s_areaId");

		UserModel user = new UserModel();
		if (StringUtils.isNotBlank(uId)) {
			user.setUserId(Integer.parseInt(uId));
		}
		user.setUserCode(userCode);
		user.setUserName(userName);
		user.setIsFrozen(isFrozen);
		user.setUserSex(sex);
		user.setUserIdcard(idCard);
		user.setPwdvaildTime(sPwdvaildtime);
		user.setUserBirthdate(userBirthday);
		user.setUserMobile(uMobile);
		user.setUserEmail(uEmail);
		user.setIsSmsVerify(isSmsVerify);
		
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		DivisionUserDao divisionUserDao = sqlSession.getMapper(DivisionUserDao.class);
		try {
			userDao.updateUser(user);
			
			UserCommModel ucm = new UserCommModel();
			ucm.setUserid(uId);
			
			userDao.updateRole(uId, roleUuid);
			
			DivisionsUserModel divisionUser = new DivisionsUserModel();
			divisionUser.setUserId(Integer.parseInt(uId));
			divisionUser.setDivisionsId(areaId);
			divisionUserDao.update(divisionUser); //把信息更新到divisions_user_mapping 表
			
			jsonObj.put("result", 0);
			jsonObj.put("message", "用户信息修改成功");
			
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "Y", "修改用户信息", String.valueOf(Logenum.EDITINFO), request));
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObj.put("result", 1);
			jsonObj.put("message", "用户信息修改失败");
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "N", "修改用户信息", String.valueOf(Logenum.EDITINFO), request));
			return jsonObj;
		}
		return jsonObj;
	}
	
	@RequestMapping("/addPhoneNum")
	@ResponseBody
	public JSONObject addPhoneNum(HttpServletRequest request, HttpServletResponse response) {
		//获取参数
		String phoneNum = request.getParameter("phoneNum");
		String phoneCheckCode = request.getParameter("phoneCheckCode");
		
		HttpSession session = request.getSession();
		JSONObject jsonObj = new JSONObject();
		
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		//对比手机验证码
		String vCode = userDao.querySmsCode(phoneNum);
		if(vCode == null || !phoneCheckCode.equals(vCode)){
			jsonObj.put("result", -1);
			jsonObj.put("message", "验证码错误或已过期，请输入正确的验证码！");
			return jsonObj;	
		}
		
		//从session获取用户信息
		UserModel userMo = (UserModel) session.getAttribute(Constant.SESSION_USER);
		
		//保存手机号到数据库
		UserModel user = new UserModel();
		user.setUserMobile(phoneNum);
		user.setUserId(userMo.getUserId());
		try {
			userDao.updateUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObj.put("result", -1);
			jsonObj.put("message", "数据库访问失败，无法保存手机号！");
			return jsonObj;
		}
		
		
		jsonObj.put("result", 0);
		jsonObj.put("message", "手机号绑定成功！");
		return jsonObj;
	}

	/*
	 * ==========================用户管理功能
	 * end=====================================================
	 */

	/*
	 * ==========================用户角色配置
	 * start=====================================================
	 */

	/**
	 * 根据用户ID，获取用户的角色信息列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/selectuserroles")
	@ResponseBody
	public JSONObject selectUserRoles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String userId = request.getParameter("se_user_id");
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		List<UserRolesModel> userRoleslist = userDao.queryUserroleslistById(userId);
		jsonObj.put("result", 0);
		jsonObj.put("datalist", userRoleslist);
		jsonObj.put("message", "用户角色信息查询成功");
		/**
		 * 日志start
		 */
		LogDao logDao = sqlSession.getMapper(LogDao.class);
		logDao.add(BuildModel.getModel( "Y", "用户角色信息查询", String.valueOf(Logenum.QUERYINFO), request));
		/**
		 * 日志end
		 */
		return jsonObj;
	}

	/**
	 * 保存用户的角色 信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveuserroles")
	@ResponseBody
	public JSONObject saveUserRolesInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String userId = request.getParameter("sa_user_id");
		String roles = request.getParameter("sa_role_ids");
		String[] arryRoels = roles.split(",");
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		try {
			userDao.delUserRolesInfo(userId);
			for (int i = 0; i < arryRoels.length; i++) {
				userDao.addUserRoleInfo(userId, arryRoels[i]);
			}
			jsonObj.put("result", 0);
			jsonObj.put("message", "用户角色信息保存成功");
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "Y", "保存用户角色信息", String.valueOf(Logenum.ADDINFO), request));
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObj.put("result", 1);
			jsonObj.put("message", "用户角色信息保存失败");
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "N", "保存用户角色信息", String.valueOf(Logenum.ADDINFO), request));
			/**
			 * 日志end
			 */
		}
		return jsonObj;
	}
	
	/**
	 * 保存用户的角色 信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deluserroles")
	@ResponseBody
	public JSONObject delUserRolesInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String userId = request.getParameter("sa_user_id");
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		try {
			userDao.delUserRolesInfo(userId);
			jsonObj.put("result", 0);
			jsonObj.put("message", "用户角色信息删除成功");
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "Y", "删除用户角色信息", String.valueOf(Logenum.DELETEINFO), request));
			/**
			 * 日志end
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObj.put("result", 1);
			jsonObj.put("message", "用户角色信息删除失败");
			/**
			 * 日志start
			 */
			LogDao logDao = sqlSession.getMapper(LogDao.class);
			logDao.add(BuildModel.getModel( "N", "删除用户角色信息", String.valueOf(Logenum.DELETEINFO), request));
			/**
			 * 日志end
			 */
			return jsonObj;
		}

		return jsonObj;
	}
	
	
	/**
	 * 根据权限生成toolbar
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getToolbar")
	@ResponseBody
	public JSONObject getToolbar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		MenuDao md = sqlSession.getMapper(MenuDao.class);
		MenuModel mm = new MenuModel();
		String uri = request.getRequestURI();
		int index = 0;
		String str = uri;
		index = str.lastIndexOf("/");
		str = str.substring(0, index);
		index = str.lastIndexOf("/");
		str = str.substring(index + 1, str.length());
		String menuUrl = str + "/showindex";
		mm.setMenuUrl(menuUrl);
		String menuid = "";
		List<MenuModel> listmenu = md.queryMenuby(mm);
		if (null != listmenu && listmenu.size() > 0) {
			menuid = listmenu.get(0).getMenuId() + "";
		}
		String roleid = request.getSession().getAttribute(Constant.SESSION_ROLEID).toString();
		List<ButtonModel>  listbutton = md.queryButtonListby(roleid, menuid);
		if(null != listbutton && listbutton.size() > 0){
			jsonObj.put("result", listbutton);
		}
		return jsonObj;
	}
	
}