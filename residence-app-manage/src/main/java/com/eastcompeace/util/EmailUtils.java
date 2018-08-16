package com.eastcompeace.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtils {
	// 设置服务器
	private static String KEY_SMTP = "mail.smtp.host";
	private static String VALUE_SMTP = "smtp.126.com";
	// 服务器验证
	private static String KEY_PROPS = "mail.smtp.auth";
	private static String VALUE_PROPS = "true";
	// 发件人用户名、密码
	private String SEND_USER = "eastcompeace_kmc@126.com";
	private String SEND_UNAME = "eastcompeace_kmc@126.com";
	private String SEND_PWD = "eastcompeaceKmc"; 
	// 建立会话
	private MimeMessage message;
	private Session session;
	
	/*
	 * 初始化方法
	 */
	public EmailUtils() {
		Properties props = System.getProperties();
		props.setProperty(KEY_SMTP, VALUE_SMTP);
		props.put(KEY_PROPS, VALUE_PROPS);
		session = Session.getDefaultInstance(props, new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SEND_UNAME, SEND_PWD);
			}
		});
		session.setDebug(true);
		message = new MimeMessage(session);
	}
	
	/**
	 * 发送邮件
	 * 
	 * @param headName
	 *            邮件头文件名
	 * @param sendHtml
	 *            邮件内容
	 * @param receiveUser
	 *            收件人地址
	 */
	public void doSendEmail(String headName, String sendHtml, String receiveUser) {
		try {
			// 发件人
			InternetAddress from = new InternetAddress(SEND_USER);
			message.setFrom(from);
			// 收件人
			InternetAddress to = new InternetAddress(receiveUser);
			message.setRecipient(Message.RecipientType.TO, to);
			// 邮件标题
			message.setSubject(headName);
			String content = sendHtml.toString();
			// 邮件内容,也可以使纯文本"text/plain"
			message.setContent(content, "text/html;charset=GBK");
			message.saveChanges();
			Transport transport = session.getTransport("smtp");
			// smtp验证，用来发邮件的邮箱用户名密码
			transport.connect(VALUE_SMTP, SEND_UNAME, SEND_PWD);
			// 发送
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		EmailUtils se = new EmailUtils();
		se.doSendEmail("香洲物业", "邮件内容", "panyanlin@eastcompeace.com");
		System.out.println("send success!");
	}
	
	// 常见邮件服务器（接收服务器和发送邮件服务器）地址
		// 腾讯QQ邮箱
		// 接收服务器：pop.qq.com
		// 发送服务器：smtp.qq.com
		// 网易126邮箱
		// 接收服务器：pop3.126.com
		// 发送服务器：smtp.126.com
		// 网易163免费邮
		// 接收服务器：pop.163.com
		// 发送服务器：smtp.163.com
		// 网易163VIP邮箱
		// 接收服务器：pop.vip.163.com
		// 发送服务器：smtp.vip.163.com
		// 网易188财富邮
		// 接收服务器：pop.188.com
		// 发送服务器：smtp.188.com
		// 网易yeah.net邮箱
		// 接收服务器：pop.yeah.net
		// 发送服务器：smtp.yeah.net
		// 网易netease.com邮箱
		// 接收服务器：pop.netease.com
		// 发送服务器：smtp.netease.com
		// 新浪收费邮箱
		// 接收服务器：pop3.vip.sina.com
		// 发送服务器：smtp.vip.sina.com
		// 新浪免费邮箱
		// 接收服务器：pop3.sina.com.cn
		// 发送服务器：smtp.sina.com.cn
		// 搜狐邮箱
		// 接收服务器：pop3.sohu.com
		// 发送服务器：smtp.sohu.com
		// 21cn快感邮
		// 接收服务器：vip.21cn.com
		// 发送服务器：vip.21cn.com
		// 21cn经济邮
		// 接收服务器：pop.163.com
		// 发送服务器：smtp.163.com
		// tom邮箱
		// 接收服务器：pop.tom.com
		// 发送服务器：smtp.tom.com
		// 263邮箱
		// 接收服务器：263.net
		// 发送服务器：smtp.263.net
		// 网易163.com邮箱
		// 接收服务器：rwypop.china.com
		// 发送服务器：rwypop.china.com
		// 雅虎邮箱
		// 接收服务器：pop.mail.yahoo.com
		// 发送服务器：smtp.mail.yahoo.com
		// Gmail邮箱
		// 接收服务器：pop.gmail.com
		// 发送服务器：smtp.gmail.com
		
}
