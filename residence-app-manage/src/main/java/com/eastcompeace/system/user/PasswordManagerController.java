package com.eastcompeace.system.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.eastcompeace.base.BaseController;
import com.eastcompeace.base.Constant;
import com.eastcompeace.model.UserModel;
import com.eastcompeace.share.cipher.MD5Cipher;
import com.eastcompeace.share.utils.ByteUtils;
import com.eastcompeace.share.utils.ToolKitUtils;
import com.eastcompeace.util.DateUtils;
import com.eastcompeace.util.GetPwdUtils;
import com.eastcompeace.util.IntegerUtils;
import com.eastcompeace.util.Mail;
import com.eastcompeace.util.sms.SmsClient;
import com.eastcompeace.util.sms.TplidEnum;

/**
 * 密码管理类
 * 
 * @author caobo
 * 
 */
@Controller
@RequestMapping("/pwdctrl")
public class PasswordManagerController extends BaseController {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 修改用户密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updatepassword")
	@ResponseBody
	public JSONObject updateUserPassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String oldPwd =request.getParameter("user_password");
		String newPwd = request.getParameter("user_newPwd");
		HttpSession session = request.getSession();
		//计算密码有效期
		int month = Constant.PWD_MODIFY_DATE; //密码默认有效期为3个月
		String date = "";
		date = DateUtils.getAfterMonth(month);
		if (session != null) {
			//从session里获取用户信息，如果信息为null则登录状态失效，需要重新登录
			UserModel currUser = (UserModel) session.getAttribute(Constant.SESSION_USER);
			if (currUser == null) {
				jsonObj.put("result", 1);
				jsonObj.put("message", "session 已失效，请重新登录");
			} else {
				String userPassword = currUser.getUserPassword();
				//对比原始密码是否正确，如果正确才允许修改密码
				if (userPassword.equalsIgnoreCase(oldPwd)) {
					int userId = currUser.getUserId();
					UserDao userDao = sqlSession.getMapper(UserDao.class);
					//链接数据库更新用户密码
					userDao.updateUserPassword(userId, newPwd, date); 
					jsonObj.put("result", 0);
					jsonObj.put("message", "用户密码修改成功，请重新登录");
				} else {
					jsonObj.put("result", 1);
					jsonObj.put("message", "原始用户密码错误");
				}
			}
		} else {
			jsonObj.put("result", 1);
			jsonObj.put("message", "session 已失效，请重新登录");
		}
		return jsonObj;
	}

	/**
	 * 找回密码 ：　1、通过手机号找回
	 * 		    2、通过邮箱找回
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/stepOne")
	@ResponseBody
	public JSONObject stepOne(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//获取用户账号
		String strUserName = request.getParameter("strUsername");
		//获取用户联系方式
		String link = request.getParameter("strLink");
		link = link.trim();
		//获取验证码
		String strCheckCode = request.getParameter("strCheckCode");
		//获取查找密码方式 1：手机号查询，2：邮箱查询
		int findType = IntegerUtils.integer(request.getParameter("iFindType"));
		
		JSONObject jsonObj = new JSONObject();
		//生成初始密码
		String pwd =GetPwdUtils.leagalPwd();
		//初始密码加密
		String MD5pwd = ByteUtils.toHexString(MD5Cipher.encrypt(pwd.getBytes()));

		//连接数据库查询用户账号是否存在
		UserModel usermodel = new UserModel();
		usermodel.setUserCode(strUserName);
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		UserModel um = userDao.queryUser(usermodel);
		if (null == um) {
			jsonObj.put("result", -1);
			jsonObj.put("message", "用户账号不存在！");
			return jsonObj;
		}
		//如果密码找回方式为1-通过手机号找回，则先连接数据库查询该用户是否存在手机号
		if(findType == 1){
			String userMobile = um.getUserMobile();
			if(userMobile == null ||"".equals(userMobile)){
				jsonObj.put("result", -1);
				//jsonObj.put("message", "用户账号没有登记手机号码！");
				jsonObj.put("message", "用户没有在系统登记的手机号码，请用试着用邮箱方式找回，如不行，请联系高级管理员！");
				return jsonObj;
			}
			if(!link.equals(userMobile)){
				jsonObj.put("result", -1);
				//jsonObj.put("message", "用户账号没有登记手机号码！");
				jsonObj.put("message", "该手机号码和用户在系统登记的手机号码不一致！");
				return jsonObj;
			}
			//数据库获取手机验证码
			String vCode = userDao.querySmsCode(link);
			if(vCode == null || !strCheckCode.equals(vCode)){
				jsonObj.put("result", -1);
				jsonObj.put("message", "验证码错误或已过期，请输入正确的验证码！");
				return jsonObj;	
			}
			    
			//发密码短信
			int i = SmsClient.sendSms(SmsClient.SMS_TYPE_IDENTIFY, TplidEnum.UPDATE_PWD, link, vCode);
			 
			if(i!=0){
				jsonObj.put("result", -1);
				jsonObj.put("message", "短信发送失败，请稍后再试");
				return jsonObj;
			}
			jsonObj.put("message", "密码已经通过短信发送出去，请及时查看。");
		} else {
			String userEmail = um.getUserEmail();
			if(userEmail == null || "".equals(userEmail)){
				jsonObj.put("result", -1);
				//jsonObj.put("message", "用户账号没有登记手机号码！");
				jsonObj.put("message", "用户没有在系统登记的邮箱，请用试着用手机号码方式找回，如不行，请联系高级管理员！");
				return jsonObj;
			}
			if(!link.equals(userEmail)) {
				jsonObj.put("result", -1);
				//jsonObj.put("message", "用户账号没有登记邮箱！");
				jsonObj.put("message", "该邮箱号码和用户在系统登记邮箱号码不一致！");
				return jsonObj;
			}
			HttpSession session = request.getSession();
			String sessCheckCode = (String) session.getAttribute(Constant.SESSION_CHECK_CODE2);
			if (!strCheckCode.equalsIgnoreCase(sessCheckCode)) {
				jsonObj.put("result", -1);
				jsonObj.put("message", "验证码错误！");
				return jsonObj;
			}
			Mail mail = new Mail();
			mail.setHost("smtp.126.com"); // 设置邮件服务器
			mail.setSender("eastcompeace_kmc@126.com");
			mail.setReceiver(link);// 接收人
			mail.setUsername("eastcompeace_kmc@126.com"); // 登录账号,一般都是和邮箱名一样吧
			mail.setPassword("eastcompeaceKmc"); // 发件人邮箱的登录密码
			mail.setSubject("密码找回");
			mail.setMessage("尊敬的用户您好,您正在使用密码找回功能，您的账号登录密码是："+pwd);
//			if (!EmailUntils.send(mail)) {
//				jsonObj.put("result", -1);
//				jsonObj.put("message", "发送邮件失败，无法通过邮件找回密码！");
//				return jsonObj;
//			}

			jsonObj.put("message", "密码已经通过邮件发送出去，请及时查看");
		}
		
		//获取密码有效期限
		int month = Constant.PWD_MODIFY_DATE; //密码默认有效期为3个月
		String date = "";

		//计算密码有效期
		date = DateUtils.getAfterMonth(month);
		//链接数据库更新用户密码信息
		userDao.updateUserpwd(strUserName, MD5pwd, "YES", date);

		jsonObj.put("result", 0);
		return jsonObj;
	}

	/**
	 * 修改用户密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updatepsw")
	@ResponseBody
	public JSONObject updatepsw(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String strNewPwd = request.getParameter("userPwd");
		String strcheckCode =request.getParameter("checkcode");
		String strUserName =request.getParameter("userName");
		HttpSession session = request.getSession();
		if (session != null) {
			String sessCheckCode = (String) session.getAttribute("updatecheckcode"); //session验证码
			if(strcheckCode.equalsIgnoreCase(sessCheckCode)){
				UserDao userDao = sqlSession.getMapper(UserDao.class);
				int month = Constant.PWD_MODIFY_DATE; //密码默认有效期为3个月
				String date = "";
				date = DateUtils.getAfterMonth(month);
				userDao.updateUserpwd(strUserName, strNewPwd,"NO",date);
						jsonObj.put("result", 0);
						jsonObj.put("message", "用户密码修改成功，请重新登录");
					}else {
				jsonObj.put("result", 2);
				jsonObj.put("message", "验证码错误");
			}
		}else{
			jsonObj.put("result", 3);
			jsonObj.put("message", "session错误");
		}
		return jsonObj;
	}
	
	public static void main(String[] args) throws Exception {
		//发密码短信
		SmsClient sms = new SmsClient();
		String strRand = ToolKitUtils.randomNumber(6);
		String pwd = "803576JFch$%";
		String smsContent = "尊敬的用户您好!您的账号新密码为：" + pwd + "，密码区分大小写，请妥善保管，如非本人操作，可不用理会！";
		int i = sms.sendSms(sms.SMS_TYPE_IDENTIFY, TplidEnum.REGISTER, "13143114212", strRand);
		//int i = sms.sendSms0("15015967583", smsContent);
		
		System.out.println(i);
	}
}