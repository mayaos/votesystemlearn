package com.eastcompeace.base;

import javax.servlet.http.HttpServletRequest;

import com.eastcompeace.model.UserModel;

public class UserUtil {
	public static UserModel getUser(HttpServletRequest request) {
		UserModel userMo = (UserModel) request.getSession().getAttribute(
				Constant.SESSION_USER);
		return userMo;
	}
public static void main(String[] args) {}
}
