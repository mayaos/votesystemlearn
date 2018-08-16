package com.eastcompeace.util;

public class Constant {
	
	// 用户认证状态
	public final static String AUTH_STATUS_WAITING = "1"; // 用户认证状态[1-待审核]
	
	public final static String AUTH_STATUS_CHECKING = "2"; // 用户认证状态[2-审核中]

	public final static String AUTH_STATUS_FINISHED = "3"; //用户认证状态[3-审核完成]
	
    public final static String ID_AUTH = "1"; // 用户身份证认证
 	
 	public final static String RC_AUTH = "2"; // 用户居住证认证
	
	// 用户账号级别[1/2/3-未认证/身份证认证/居住证认证](字典表)
	
	public final static String AUTH_LEVEL_ID = "2"; // 身份证认证
	
	public final static String AUTH_LEVEL_CITIZEN = "3"; // 居住证认证
	
	public final static int AUTH_WAITING_DAYS = 7; // 用户等待认证天数
	
	// 消息状态
	public final static String MSG_STATUS_UNREAD = "0"; //消息状态[0-未读]
	
	public final static String MSG_STATUS_READ = "1"; //消息状态[1-已读]
	
	// 消息类型[1-实名认证/2-优惠券/3-到期通知]
	public final static String MSG_TYPE_AUTH = "1"; //1-实名认证
	
	public final static String MSG_TYPE_COUPON = "2"; //2-优惠券
	
	public final static String MSG_TYPE_OVERDUE = "3"; //3-到期通知
	
	// 商家性质[0: 直营店 1: 连锁店]
	public final static String 	MERCHANT_NATURE_DIRECT = "0";
	
	public final static String 	MERCHANT_NATURE_CHAIN = "1";
	
	// 连锁类型[0: 总店  1: 分店]
	public final static String 	MERCHANT_CHAIN_HEAD = "0";
	
	public final static String 	MERCHANT_CHAIN_BRANCH = "1";
	
	// 消息
	public final static String MSG_AUTH_ID_FAILED = "身份证认证失败";
	
	public final static String MSG_AUTH_ID_SUCCEEDED = "身份证认证成功";
	
	public final static String MSG_AUTH_CITIZEN_FAILED = "居住证认证失败";
	
	public final static String MSG_AUTH_CITIZEN_SUCCEEDED = "居住证认证成功";
	
	public final static String MSG_AUTH_NOTICE_TITLE = "实名认证结果通知！";
	
	public final static String MSG_AUTH_ID_NOTICE_SUCCEEDED = "尊敬的用户，您好！您的居住证APP账号通过身份证认证成功。";
	
	public final static String MSG_AUTH_ID_NOTICE_FAILED = "尊敬的用户，您好！您的居住证APP账号通过身份证认证失败。";
	
	public final static String MSG_AUTH_CITIZEN_NOTICE_SUCCEEDED = "尊敬的用户，您好！您的居住证APP账号通过居住证认证成功。";
	
	public final static String MSG_AUTH_CITIZEN_NOTICE_FAILED = "尊敬的用户，您好！您的居住证APP账号通过居住证认证失败。";
	
}
