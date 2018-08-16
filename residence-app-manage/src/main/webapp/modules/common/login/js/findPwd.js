

$(function() {
	var hUserName="";
	
	var divWindow = $('#divWindow').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 475,
		height : 230,
		title : '找回密码',
		onClose:function(){
			$('input#fUserName').textbox('clear');
			$('input#fLink').textbox('clear');
			$('input#fCode').textbox('clear');
			refreshValidCode();
		}
	});

	$.extend($.fn.validatebox.defaults.rules, {
		mail:{
			validator: function(value){
				return  /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(value);
			},
			message:"邮箱合法!"
		}
	});  

	$('#findPwd').click(function() {
		divWindow.window('open');
		$('input[name=findType]').get(0).checked= true; 
		$('#fUserEmail').hide();
		$('#Validcode2').hide();
		$('#btnClose').hide();
		$('#btnNext').show();
		$('li#divMessage').html('');
	});
	
	$('input[name=findType]').click(function(){
		if($('input[name=findType]').get(0).checked){
			$('#btnSms2').show();
			$('#Validcode2').hide();
			$('#fLink').textbox({ 
			    required: true,
			    validType: 'phone'
			}); 
		} else {
			refreshValidCode();
			$('#Validcode2').show();
			$('#btnSms2').hide();
			$('#fLink').textbox({
			    required: true,
			    validType: 'email'
			});
		}
	 });
	
	function refreshValidCode(){
		var n = Math.round(Math.random() * 1000);
		$('img#Validcode2').attr('src', Common.getUrl('checkcode2?n=' + n));
	} 
	$('img#Validcode2').click(function(){
		refreshValidCode();
	}).trigger('click');
	
	
	$('#btnNext').click(function() {
		var userName = $('input#fUserName');
		var checkCode = $('input#fCode');
		var link = $('#fLink');
		var findType = $('input[name=findType]:checked').val();
		
		if (userName.val() == '') {
			$('li#divMessage').hide().html('提示：请输入用户名！').fadeIn();
			userName.focus();
			return;
		}
		
		if (link.val() == '') {
			$('li#divMessage').hide().html('提示：请输入获取密码的联系方式！').fadeIn();
			link.focus();
			return;
		}
		
		if (checkCode.val() == '') {
			$('li#divMessage').hide().html('提示：请输入验证码！').fadeIn();
			checkCode.focus();
			return;
		}
		$.ajax({
			timeout : 15000,
			async : true,
			cache : false,
			type : 'POST',
			url : Common.getUrl('pwdctrl/stepOne'),
			dataType : 'json',
			data : {
				'strUsername' : userName.val(),
				'strLink' :link.val(), 
				'strCheckCode' : checkCode.val(),
				'iFindType': findType
			},
			beforeSend : function() {
			},
			success : function(data) {
				$('li#divMessage').hide().html(data.message).fadeIn();
				if (data.result == 0) {
					refreshValidCode();
					$('#btnClose').show();
					$('#btnNext').hide();
				}
			},
			error : function(errdata) {
				$('li#divMessage').hide().html('提示：请求服务器失败！').fadeIn();
				divWindow.window('close');
			}
		});
	});
	
	$('#btnClose').click(function() {
		divWindow.window('close');
	});

	$('#btnSms2').click(function() {
		var userMobile = $('#fLink').val();
		if(userMobile =='' || userMobile==null) {
			$('li#divMessage').hide().html('提示：请输入手机号码！').fadeIn();
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
				time(document.getElementById("btnSms2"));
				$('li#divMessage').html('<img src="../../../resources/images/spinner.gif" border="0" align="absmiddle" />&nbsp;请稍候，正在发送短信...').fadeIn();
				$('#btnSms').attr('disabled', true);
			},
			success	: function(data) {
				//time(document.getElementById("btnSms2"));
				$('li#divMessage').hide().html('提示：' + data.message).fadeIn();
				$('#btnSms').attr('disabled', false);
			},
			error: function(errdata) {
				$('li#divMessage').hide().html('提示：发送短信失败，请求服务器失败！').fadeIn();
				$('#btnSms').attr('disabled', false);
			}
		});
	});
});