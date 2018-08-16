package com.eastcompeace.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.eastcompeace.base.UserUtil;
import com.eastcompeace.model.LogModel;
import com.eastcompeace.model.UserModel;

public class BuildModel {
	public static LogModel getModel( String Issuccess, String content,String type, HttpServletRequest request) {
		UserModel um = UserUtil.getUser(request);
		return getModel(Issuccess, content, type, um.getUserId(), request);
	}
	
	public static LogModel getModel(String Issuccess, String content, String type, int userId, HttpServletRequest request) {
		LogModel log = new LogModel ();
		log.setHappentime(DateUtils.dateToString(new Date(),DateUtils.FORMAT_yMdHms)); // 日期
		log.setIssuccess(Issuccess); //是否成功
		log.setLogcontent(content); //操作内容
		log.setLogtype(type); 		//日志类型
		
		if(request == null){
			log.setIp("127.0.0.1");
			log.setRequesturl("common");
			log.setUserid(userId + "");
			log.setUsername("非法操作用户");
		}else{
			UserModel um = UserUtil.getUser(request);
			log.setIp(IpUtils.getClientIP(request));	// ip
			log.setRequesturl(request.getRequestURI());
			log.setUserid(um.getUserId() + "");			// userID
			log.setUsername(um.getUserCode());
		}
		return log;
	}
}
