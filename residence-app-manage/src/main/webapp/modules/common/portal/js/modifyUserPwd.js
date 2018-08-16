$(function(){
	// 修改密码
	var flag= false;
	$('a#passwordModify').click(function() {
		divWindow.window('open');
		flag= false;
	});
	var divWindow = $('#divWindow').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		draggable:false,
		resizable:false,
		closed:true,
		width:550,
		height:240,
		title: '修改密码',
		onClose:function(){
			if(flag){
				$.ajax({
					timeout	: 15000,
					async	: true,
					cache	: false,
					type	: 'POST',
					url		: Common.getUrl('userctrl/logout'),
					dataType: 'json',
					success	: function(data) {
						parent.location.href = 'login.html';
					}
				});
			}
		}
	});
	
	var divForm = divWindow.find('form');
	$('#btnSave').click(function() {
		var initialPwd = $('input#initialPwd');
	    var newPwd = $('input#newPwd');
		var confirmPwd = $('input#confirmPwd');
		
		if (initialPwd.val() == '') {
			initialPwd.focus();
			$('#messageTips').hide().html('提示：原始密码不能为空！').fadeIn();
			return;
		}
		if (newPwd.val().length < 6) {
			newPwd.focus();
			$('#messageTips').hide().html('提示：新密码长度不能小于6！').fadeIn();
			return;
		}
		if (confirmPwd.val() != newPwd.val()) {
			confirmPwd.focus();
			$('#messageTips').hide().html('提示：两次输入密码不一致！').fadeIn();
			return;
		}
		if(passwordStrongLevel<2) {
			$('#messageTips').hide().html('提示：密码强度不足！').fadeIn();
			return;
		}
		
		var pass=hex_md5(initialPwd.val());
		var pass2=hex_md5(newPwd.val());
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: Common.getUrl('pwdctrl/updatepassword'),
			dataType: 'json',
			data	: {
			    'user_password':pass,
				'user_newPwd': pass2
			},
			beforeSend: function() {
				$("#btnSave").removeAttr("disabled");
			},
			success	: function(data) {
				if (data.result == 0) {
					flag = true;
					$('#initialPwd').textbox('clear');
					$('#newPwd').textbox('clear');
					$('#confirmPwd').textbox('clear');
					$("#btnSave").hide();
					$("#btnCancel").hide();
					$('#messageTips').hide().html('提示：密码修改成功，为了系统安全，将重新登录！').fadeIn();
				} else {
					$('#messageTips').hide().html('提示：'+data.message).fadeIn();
				}
			},
			error: function(errdata) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});
		
	});
	
	$('#btnCancel').click(function() {
		$('#initialPwd').textbox('clear');
		$('#newPwd').textbox('clear');
		$('#confirmPwd').textbox('clear');
		divWindow.window('close');
		if(flag){
			$.ajax({
				timeout	: 15000,
				async	: true,
				cache	: false,
				type	: 'POST',
				url		: Common.getUrl('userctrl/logout'),
				dataType: 'json',
				success	: function(data) {
					parent.location.href = 'login.html';
				}
			});
		};
		
	});
	
});