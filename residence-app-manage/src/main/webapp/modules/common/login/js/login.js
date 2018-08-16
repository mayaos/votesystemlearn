/**
 * JavaScript for Redisence Permit system
 * Copyright(c) 2014-2015,  qinyong, eastcompeace
 */
 
$(function(){
	
	// float cloud layer
	(function cloudFloat() {
		var offset1 = 450, offset2 = 0, offsetbg = 0;
		var cloudWidth = $('div#cloud').outerWidth();
		var $cloud1 = $("div#cloud1"), $cloud2 = $("div#cloud2");
		
		setInterval(function flutter() {
			if (offset1 >= cloudWidth) offset1=-580;
			if (offset2 >= cloudWidth) offset2=-580;
			
			offset1 += 1.1;
			offset2 += 1;
			$cloud1.css("background-position", offset1 + "px 120px");
			$cloud2.css("background-position", offset2 + "px 450px");
		}, 70);
		
		setInterval(function bg() {
			if (offsetbg >= cloudWidth) offsetbg=-580;
			
			offsetbg += 0.9;
			$("body").css("background-position", -offsetbg + "px 0px");
		}, 90);
	})();
		
	
	$('input#UserName').focus();
	
	function fresh(){
		var n = Math.round(Math.random() * 1000);
		$('img#Validcode').attr('src', Common.getUrl('checkcode?n=' + n));
	}
	$('img#Validcode').click(function(){
		fresh();
	}).trigger('click');
	
	$('#Login').click(function(){
		var UserName = $('input#UserName');
		var Password = $('input#Password');
		var CheckCode = $('input#CheckCode');
		
		if (UserName.val() == '') {
			$('li#message').hide().html('提示：请输入用户名！').fadeIn();
			UserName.focus();
			return;
		}
		if (Password.val() == '') {
			$('li#message').hide().html('提示：请输入密码！').fadeIn();
			Password.focus();
			return;
		}
		if (CheckCode.val() == '') {
			$('li#message').hide().html('提示：请输入验证码！').fadeIn();
			CheckCode.focus();
			return;
		}
		
		var pass=hex_md5(Password.val());
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: Common.getUrl('userctrl/login'),
			dataType: 'json',
			data	: {
//				'l_username': 'admin',
//				'l_password': '123456'
				'l_username': UserName.val(),
				'l_password': pass,
				'l_validateCode': CheckCode.val()
			},
			beforeSend: function() {
				$('li#message').hide().html('<img src="../../../resources/images/spinner.gif" border="0" align="absmiddle" />&nbsp;请稍候，正在登录...').fadeIn();
				$('input#Login').attr('disabled', true);
			},
			success	: function(data) {
				if (data.result == 0) { //正常登录
					window.location.href = '../portal/portal.html';
				}else if (data.result == 1) { //短信验证登陆
					smsWindow.window('open');
					$('#userMobile').val(data.userMobile); //userMobile还没有传入，需要改
					$('li#smsMsg').hide().html('提示：' + data.message).fadeIn();
				}else if (data.result == 2) { //初始密码登录
					divWindow.window('open');
				}else if (data.result == 3) { //首次登录绑定手机号				
					phoneWindow.window('open');	
				}else { //用户名密码或者验证码错误
					fresh();
					var n = Math.round(Math.random() * 1000);
					$('#Validcode').attr('src', Common.getUrl('checkcode?n=' + n));
					$('li#message').hide().html('提示：' + data.message).fadeIn();
				}
				$('input#Login').attr('disabled', false);
			},
			error: function(errdata) {
				$('div#message').hide().html('提示：登录失败，请求服务器失败！').fadeIn();
				$('input#Login').attr('disabled', false);
			}
		});
	});
	
	var divWindow = $('#divWindow2').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:630,
		height:280,
		title: '重置密码',
		onClose:function(){
			var newPwd = $('input#newPwd');
			var confirmPwd = $('input#confirmPwd');
			newPwd.textbox('clear');
			confirmPwd.textbox('clear');
			$('input#CheckCode3').textbox('clear');
			fresh2();
			$('li#message').hide().html('<img src="../../../resources/images/spinner.gif" border="0" align="absmiddle" />&nbsp;请稍候，正在登录...').fadeOut();
		}
	});
	
	
	function fresh2(){
		var n = Math.round(Math.random() * 1000);
		//$('img#Validcode3').attr('src', Common.getUrl('checkcodeByPram?width=100&height=30&sessionName=updatecheckcode' ));
		$('img#Validcode3').attr('src', Common.getUrl('checkcode3?n=' + n));
	}
	
	$('img#Validcode3').click(function(){
		fresh2();
	}).trigger('click');
	
	
	$('#newPwd').textbox('textbox').keyup(function(){
		var nPwd=$('#newPwd').textbox('textbox').val();
		ps.update(nPwd);
	})
	
	$('#btnSave2').click(function() {
	    var newPwd = $('input#newPwd');
		var confirmPwd = $('input#confirmPwd');
		var CheckCode3 = $('input#CheckCode3');
		var username=$('input#UserName');
		if (newPwd.val().length < 6) {
			$('li#divMessage3').hide().html('新密码长度不能小于6').fadeIn();
			newPwd.focus();
			return;
		}
		if (confirmPwd.val() != newPwd.val()) {
			$('li#divMessage3').hide().html('两次输入密码不一致').fadeIn();
			confirmPwd.focus();
			return;
		}
		if(passwordStrongLevel<2) {
			$('li#divMessage3').hide().html('密码强度不足!').fadeIn();
			return;
		}
		if (CheckCode3.val() == '') {
			$('li#divMessage3').hide().html('提示：请输入验证码!').fadeIn();
			CheckCode3.focus();
			return;
		}
		var pass2=hex_md5(newPwd.val());
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: Common.getUrl('pwdctrl/updatepsw'),
			dataType: 'json',
			data	: {
				'userPwd': pass2,
				'checkcode':CheckCode3.val(),
				"userName":username.val()
			},
			beforeSend: function() {
				$("#btnSave2").removeAttr("disabled");
			},
			success	: function(data) {
				if (data.result == 2) {
					Common.alert(data.message);
					fresh2();
				}else if(data.result==0) {
					Common.confirm("密码修改成功，请重新登录！");
					parent.location.href = 'login.html';					
				}else{
					Common.alert(data.message);
				}
			},
			error: function(errdata) {
				Common.alert('请求服务器失败，操作未能完成！');
				fresh2();
			}
		});
		
	});
	
	$('#btnCancel2').click(function() {
		$('#newPwd').textbox('clear');
		$('#confirmPwd').textbox('clear');
		divWindow.window('close');
	});
	
	/* check client browser */
	(function checkBrowser() {
		var app = navigator.appName;
		var verStr = navigator.appVersion;
		if(app.indexOf('Microsoft') != -1) {
			if (verStr.indexOf('MSIE 3.0') != -1 || verStr.indexOf('MSIE 4.0') != -1 || verStr.indexOf('MSIE 5.0') != -1 || verStr.indexOf('MSIE 5.1') != -1 || verStr.indexOf('MSIE 6.0') != -1) {
				alert('系统提示：\n    您的浏览器版本太低，可能会导致无法使用后台的部分功能。建议您使用 IE7.0 或以上版本。');
				window.close();
			}
		}
	})();
	
	
	var smsWindow = $('#smsWindow').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 400,
		height : 195,
		title : '短信验证确认',
		onClose:function(){
			
		}
	});
	//smsWindow.window('open');
	
	$('#btnSms').click(function() {
		var userMobile = $('#userMobile').val()
		if(userMobile =='' || userMobile==null) {
			$('#smsMsg').hide().html('提示：请输入手机号码！').fadeIn();
			return;
		}

		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: Common.getUrl('userctrl/sendSms'),
			dataType: 'json',
			data	: {
				'userMobile': userMobile
			},
			beforeSend: function() {
				time(document.getElementById("btnSms"));
				$('#smsMsg').html('<img src="../../../resources/images/spinner.gif" border="0" align="absmiddle" />&nbsp;请稍候，正在发送短信...').fadeIn();
				$('#btnSms').attr('disabled', true);
			},
			success	: function(data) {
				time(document.getElementById("btnSms"));
				$('#smsMsg').hide().html('提示：' + data.message).fadeIn();
				$('#btnSms').attr('disabled', false);
			},
			error: function(errdata) {
				$('#smsMsg').hide().html('提示：发送短信失败，请求服务器失败！').fadeIn();
				$('#btnSms').attr('disabled', false);
			}
		});
	});
	
	
	$('#btnOk').click(function() {
		var smsCode = $('#smsCode').val();
		if(smsCode =='' || smsCode==null) {
			$('#smsMsg').hide().html('提示：请输入短信验证码！').fadeIn();
			return;
		}
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: Common.getUrl('userctrl/verifySms'),
			dataType: 'json',
			data	: {
				'smsCode'  : smsCode,
				'userName' : $('#UserName').val(),
				'password' :hex_md5($('#Password').val()) 
			},
			beforeSend: function() {
				$('#smsMsg').html('<img src="../../../resources/images/spinner.gif" border="0" align="absmiddle" />&nbsp;请稍候，验证码校验中...').fadeIn();
				$('#btnOk').attr('disabled', true);
			},
			success	: function(data) {
				if (data.result == 0) { //正常登录
					window.location.href = '../portal/portal.html';
				}
				$('#smsMsg').hide().html('提示：' + data.message).fadeIn();
				$('#btnOk').attr('disabled', false);
			},
			error: function(errdata) {
				$('#smsMsg').hide().html('提示：短信确认失败，请求服务器失败！').fadeIn();
				$('#btnOk').attr('disabled', false);
			}
		});
	});
	
	//----------------------------------------------------------------
	//------------------------绑定手机号----------------------------------
	//----------------------------------------------------------------
	/**
	 * 设置绑定手机号窗口初始属性
	 */
	var phoneWindow = $('#phoneWindow').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:630,
		height:160,
		title: '首次登录绑定手机号',
		onClose:function(){		
			$('input#phoneNum').textbox('clear');
			$('input#phoneCheckCode').textbox('clear');
			logout();
		}
	});
	
	/**
	 * 单击发送短信事件
	 */
	$('#btnSms3').click(function() {
		var userMobile = $('input#phoneNum').val();
		if(userMobile =='' || userMobile==null) {	
			$('li#divMessage4').hide().html('提示：请输入手机号码！').fadeIn();
			return;
		}
	
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: Common.getUrl('userctrl/sendSms'),
			dataType: 'json',
			data	: {
				'userMobile': userMobile
			},
			beforeSend: function() {
				time(document.getElementById("btnSms3"));
				$('#btnSave3').attr('disabled', true);
			},
			success	: function(data) {
				$('li#divMessage4').hide().html('短信发送成功，请注意查收！').fadeIn();
				$('#btnSave3').attr('disabled', false);
			},
			error: function(errdata) {
				$('li#divMessage4').hide().html('发送短信失败，请求服务器失败！').fadeIn();
				$('#btnSave3').attr('disabled', false);
			}
		});
	});
	
	/**
	 * 单击保存按钮事件
	 */
	$('#btnSave3').click(function() {
	    var phoneNum = $('input#phoneNum').val();
		var phoneCheckCode = $('input#phoneCheckCode').val();
		if(phoneNum =='' || phoneNum==null) {	
			$('li#divMessage4').hide().html('提示：请输入手机号码！').fadeIn();
			return;
		}
		if(phoneCheckCode =='' || phoneCheckCode==null) {	
			$('li#divMessage4').hide().html('提示：请输入手机验证码！').fadeIn();
			return;
		}
		
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: Common.getUrl('userctrl/addPhoneNum'),
			dataType: 'json',
			data	: {
				'phoneNum':phoneNum,
				"phoneCheckCode":phoneCheckCode
			},
			beforeSend: function() {
				$("#btnSave3").removeAttr("disabled");
			},
			success	: function(data) {
				if(data.result==0) {
					//用到了easyui确认框语法参考API文档
					Common.confirm("手机号绑定成功，进入主页面！",function(r){
						if(r){
							window.location.href = '../portal/portal.html';	
						} else {
							logout();
						}					
					});					
				}else{
					$('li#divMessage4').hide().html(data.message).fadeIn();
				}
			},
			error: function(errdata) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});
		
	});
	
	/**
	 * 单击取消按钮事件
	 */
	$('#btnCancel3').click(function() {
		$('input#phoneNum').textbox('clear');
		$('input#phoneCheckCode').textbox('clear');
		phoneWindow.window('close');
		logout();
	});
	//----------------------------------------------------------------
});

var wait=90;
function time(o) {
		if (wait == 0) {
			o.removeAttribute("disabled");			
			o.value="获取短信验证码";
			wait = 90;
		} else {
			o.setAttribute("disabled", true);
			o.value="重新发送(" + wait + ")";
			wait--;
			setTimeout(function() {
				time(o)
			},
			1000)
		}
	}

function logout() {
	$.ajax({
		timeout	: 15000,
		async	: true,
		cache	: false,
		type	: 'POST',
		url		: Common.getUrl('userctrl/logout'),
		dataType: 'json',
		success	: function(data) {
			window.location.href = 'login.html';
		}
	});
}