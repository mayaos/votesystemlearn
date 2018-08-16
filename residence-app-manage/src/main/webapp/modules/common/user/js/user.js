$(function() {
	var toolbarres;
	$.ajax({
		timeout	: 15000,
		async	: true,
		cache	: false,
		type	: 'POST',
		url		: Common.getUrl('userctrl/getToolbar'),
		dataType: 'json',
		data	: {
		},
		beforeSend: function() {
		},
		success	: function(data) {
			toolbarres = data.result;
		},
		error: function(errdata) {
			Common.alert('请求服务器失败，操作未能完成！');
		}
	});
	var toolbar = [{}];
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped : true,
		dataType : 'json',
		url : Common.getUrl('userctrl/userlist'),
		pagination : true,
		pagePosition : 'bottom',
		pageNumber : 1,
		pageSize : Common.pageSize,
		pageList : Common.pageList,
		toolbar : toolbar,
		multiSort : false,
		remoteSort : false,
		onLoadSuccess : function(data) {
		//清空diiv
		$('#gridList').datagrid("emptyItem",'');
		//循环添加
			for (var i = 0; i < toolbarres.length; i++) {  
				var text = toolbarres[i].text+"";
				var icon = toolbarres[i].icon+"";
				var handler = toolbarres[i].handler+"";
				$('#gridList').datagrid("addToolbarItem",[{"text" : text,"iconCls" : icon,"handler" : handler},"-"]);
			}
		}
	});

	// 设置分页控件显示汉字
	grid.datagrid('getPager').pagination({
		beforePageText : Common.beforePageText,
		afterPageText : Common.afterPageText,
		displayMsg : Common.displayMsg
	});

	var divWindow = $('#divWindow').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 400,
		title : '用户信息编辑'
	});
	var divForm = divWindow.find('form');

	var divWindow2 = $('#divWindow2').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 400,
		title : '用户信息编辑'
	});
	var divForm2 = divWindow2.find('form');

	var divWindow3 = $('#divWindow3').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 420,
		title : '用户角色信息配置'
	});

	// 添加用户信息 start 显示添加用户窗口
	function addInfo() {
		$('#btnSave').removeAttr('disabled');// 启用按钮
		divWindow.window('open');
		divForm.form('clear');
		divForm.url = Common.getUrl('userctrl/adduserinfo');
		$('input[name=isSmsVerify]').get(0).checked= true; 
		$('#roleUuid').combobox('clear');
	}
	
	// 删除用户消息
	function deleteInfo() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		var delFlag = 1;
		var isFrozenFlag = 1;
		$.each(checkedItems, function(index, item) {
			if(item.roleName!='超级管理员' && item.isFrozen != 'YES') {			
				isFrozenFlag = 0;
			}
			if(item.roleName=='超级管理员') {
				Common.alert('超级管理员不允许删除');
				delFlag = 0;
				return;
			}
			items.push(item.userId);
		});
		
		if(isFrozenFlag == 0) {
			Common.alert('选择的信息中存在未注销用户，请先注销再删除！');
			return;
		}
		
		if(delFlag == 1) {
			var ids = items.join(',');
			if (checkedItems.length > 0) {
				Common.confirm('确定要删除当前选择的用户吗？', function(r) {
					if (r) {
						grid.datagrid('load');
						grid.datagrid('clearSelections');
	
						$.ajax({
							timeout : 15000,
							async : true,
							cache : false,
							type : 'POST',
							url : Common.getUrl('userctrl/deleteuser'),
							dataType : 'json',
							data : {
								'd_userIds' : ids
							},
							success : function(data) {
								if (data.result == 0) {
									Common.alert(data.message);
									grid.datagrid('reload');
									grid.datagrid('clearSelections');
								} else {
									Common.alert(data.message);
								}
	
							},
							error : function(errdata) {
								Common.alert(data.message);
							}
						});
					}
				});
			} else {
				Common.alert('请先选择要删除的用户！');
			}
		}
	}

	$('#btnSave').click(function() {
		var ucode = $('input#ucode');
		var upwd = $('input#upwd');
		var uname = $('input#uname');
		var sex = $('#usex').combobox('getValue');
		var uidcard = $('input#uidcard');
		var upwdvdate = $('#upwdvdate').datebox('getValue');
		var ubdate = $('#ubdate').datebox('getValue');
		var umobile = $('input#umobile');
		var uemail = $('input#uemail');
		var roleUuid = $('#roleUuid').combobox('getValue');
		var userId = $('#id');
		var isSmsVerify = $('input[name=isSmsVerify]:checked').val();
		var areaId = $('#areaId').combo('getValue');	
		
		if (ucode.val() == '') {
			ucode.focus();
			Common.alert('用户名不能为空');
			return;
		}
		if(/.*[\u4e00-\u9fa5]+.*$/.test(ucode.val())){
			Common.alert("用户名不能含有汉字！");
			return false;
		} 
		if(areaId==''){
			Common.alert("管理员管理区域名称不能为空!");
			return;
		}
		if (upwd.val() == '') {
			Common.alert('密码不能为空');
			return;
		}
		if (roleUuid == '') {
			Common.alert('用户角色不能为空');
			return;
		}
		var re = /[a-zA-Z0-9]/;
		if (!upwd.val().match(re)) {
			Common.alert('密码不合法，必须为数字或者英文');
			return false;
		}
		if (upwdvdate == '') {
			Common.alert('密码有效期不能为空');
			return;
		}
		
		if(isSmsVerify =='YES'){
			if(umobile.val() == '' || umobile.val() == null){
				Common.alert('请输入联系电话（手机号码）用于短信验证');
				return;
			}
		}

		var re = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
		if (uemail.val() != '') {
			if (!uemail.val().match(re)) {
				Common.alert('邮箱不合法');
				return;
			}
		}

		if (!$("#addForm").form('validate')) {
			Common.alert("请根据验证提示完善表单信息！");
			return false;
		}
		
		$.ajax({
			timeout : 15000,
			async : true,
			cache : false,
			type : 'POST',
			url : divForm.url,
			dataType : 'json',
			data : {
				's_usercode' : ucode.val(),
				's_username' : uname.val(),
				's_password' : upwd.val(),
				's_roleUuid': roleUuid,
				's_sex' : sex,
				's_idcard' : uidcard.val(),
				's_pwdvaildtime' : upwdvdate,
				's_birthdate' : ubdate,
				's_mobile' : umobile.val(),
				's_email' : uemail.val(),
				's_id' : userId.val(),
				's_isSmsVerify' :isSmsVerify,
				's_areaId' :areaId,
			},
			beforeSend : function() {
				$("#btnSave").removeAttr("disabled");// 启用按钮
			},
			success : function(data) {
				if (data.result == 0) {
					$("#btnSave").attr({
						"disabled" : "disabled"
					});
					grid.datagrid('load');
					divWindow.window('close');
					grid.datagrid('clearSelections');
					Common.alert(data.message);
				} else {
					Common.alert(data.message);
				}
			},
			error : function(errdata) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});

	});
	$('#btnCancel').click(function() {
		divWindow.window('close');
	});
	
	//获取角色列表内容		
	$('#roleUuid').combobox({
		dataType : 'json',
		url : Common.getUrl('/rolesctrl/querySelectList'),
		method: 'POST',
		valueField : 'role_id',
		textField : 'role_name'
	}); 
  
	
	
	// 添加用户信息 end 保存信息及提交数据库处理

	// 修改用户信息
	function editInfo() {
		divForm2.form('clear');
		divForm2.url = Common.getUrl('userctrl/updatuserinfo');
		$("#btnSave2").removeAttr("disabled");// 启用按钮
		var checkedItems = grid.datagrid('getSelected');
		if (checkedItems) {
			var check = grid.datagrid('getChecked');
			if (check.length > 1) {
				Common.alert('请选择一条数据！');
				return;
			}
		}
		if (!checkedItems) {
			Common.alert('请先选择要编辑的数据！');
			return;
		}
		
		if(checkedItems.roleName=='超级管理员') {
			Common.alert('超级管理员不允许修改');
			return;
		}
		
		divWindow2.window('open');
		$('#ucode2').textbox('setValue', checkedItems.userCode);
		$('#uname2').textbox('setValue', checkedItems.userName);
		$('#isFrozen2').combobox('setValue', '');
		$('#usex2').combobox('setValue', checkedItems.userSex);
		$('#uidcard2').textbox('setValue', checkedItems.userIdcard);
		$('#upwdvdate2').datebox('setValue', checkedItems.pwdvaildTime);
		$('#ubdate2').datebox('setValue', checkedItems.userBirthdate);
		$('#roleUuid2').combobox('select', checkedItems.roleId);		
		$('#umobile2').textbox('setValue', checkedItems.userMobile);
		$('#uemail2').textbox('setValue', checkedItems.userEmail);
		$("#id2").textbox('setValue', checkedItems.userId);
		$("#areaId2").textbox('setValue', checkedItems.userDivisionsId);	
		var isSmsVerify = checkedItems.isSmsVerify=="否"? "NO":"YES";
		$('input[name=isSmsVerify2][value='+isSmsVerify+']').prop('checked', true);
		roleId = checkedItems.roleId;
	}
	
	//获取角色列表内容--修改	
	$('#roleUuid2').combobox({
		dataType : 'json',
		url : Common.getUrl('/rolesctrl/querySelectList'),
		method: 'POST',
		valueField : 'role_id',
		textField : 'role_name'
	}); 
  

	$('#btnSave2').click(function() {
		if (!$("#addForm2").form('validate')) {
			Common.alert("请根据提示完善表单信息！");
			return false;
		}
		var ucode = $('input#ucode2');
		var uname = $('input#uname2');
//		var roleUuid = 	$("#roleUuid2").combotree("getValue");
		var roleUuid = 	$('#roleUuid2').combobox('getValue');
		var isFrozen = $('#isFrozen2').combobox('getValue');
		var sex = $('#usex2').combobox('getValue');
		var uidcard = $('input#uidcard2');
		var upwdvdate = $('#upwdvdate2').datebox('getValue');
		var ubdate = $('#ubdate2').datebox('getValue');
		var umobile = $('input#umobile2');
		var uemail = $('input#uemail2');
		var userId = $('#id2');
		var isSmsVerify = $('input[name=isSmsVerify2]:checked').val();
		var areaId = $('#areaId2').combo('getValue');	
	
		if (ucode.val() == '') {
			ucode.focus();
			Common.alert('用户名不能为空');
			return;
		}
		
		if (roleUuid == '') {
			Common.alert('用户角色不能为空');
			return;
		}
		if(areaId==''){
			Common.alert("管理员管理区域名称不能为空!");
			return;
		}
		if(isSmsVerify =='YES'){
			if(umobile.val() == '' || umobile.val() == null){
				Common.alert('请输入联系电话（手机号码）用于短信验证');
				return;
			}
		}		

		$.ajax({
			timeout : 15000,
			async : true,
			cache : false,
			type : 'POST',
			url : divForm2.url,
			dataType : 'json',
			data : {
				'u_usercode' : ucode.val(),
				'u_username' : uname.val(),
				's_roleUuid': roleUuid,
				'u_isFrozen' :isFrozen,
				'u_sex' : sex,
				'u_idcard' : uidcard.val(),
				'u_pwdvaildtime' : upwdvdate,
				'u_birthdate' : ubdate,
				'u_mobile' : umobile.val(),
				'u_email' : uemail.val(),
				'u_id' : userId.val(),
				'u_isSmsVerify': isSmsVerify,
				's_areaId' :areaId
			},
			beforeSend : function() {
				$("#btnSave2").removeAttr("disabled");// 启用按钮
			},
			success : function(data) {
				if (data.result == 0) {
					grid.datagrid('load');
					divWindow2.window('close');
					grid.datagrid('clearSelections');
					Common.alert(data.message);
				} else {
					Common.alert(data.message);
				}
			},
			error : function(errdata) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});

	});
	
	$('#btnCancel2').click(function() {
		divWindow2.window('close');
	});

	// 注销用户信息	
	function cancel(){
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.userId);
		});
		var ids = items.join(',');
		if (checkedItems.length > 0) {
			Common.confirm('确定要注销当前选择的记录吗？', function(r) {
				if (r) {
					grid.datagrid('load');
					grid.datagrid('clearSelections');

					$.ajax({
						timeout : 15000,
						async : true,
						cache : false,
						type : 'POST',
						url : Common.getUrl('userctrl/canceluser'),
						dataType : 'json',
						data : {
							'd_userIds' : ids
						},
						success : function(data) {
							if (data.result == 0) {
								Common.alert(data.message);
								grid.datagrid('reload');
								grid.datagrid('clearSelections');
							} else {
								Common.alert(data.message);
							}

						},
						error : function(errdata) {
							Common.alert(data.message);
						}
					});
				}
			});
		} else {
			Common.alert('请先选择要注销的数据！');
		}

	}

	// 查询用户信息
	$('#search').click(function() {
		var userCode = $('#userCode').textbox('getText');
		var userName = $('#userName').textbox('getText');
		var isFrozen = $('#isFrozen').combobox('getValue');
		var createDateStart = $('#createDateStart').datebox('getValue');
		var createDateEnd = $('#createDateEnd').datebox('getValue');
		// 使用参数查询
		grid.datagrid('load', {
			's_user_code' : userCode,
			's_user_name' : userName,
			'l_user_isfrozen' : isFrozen,
			'd_createtime_start' : createDateStart,
			'd_createtime_end' : createDateEnd
		});
	});

	// checkbox 取值
	// 注意：如果需要使用checkbox进行多选，须在jsp页面中对datagrid加入:
	// singleSelect:false,selectOnCheck:true,checkOnSelect:true
	$('#chkbox').click(function() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();

		$.each(checkedItems, function(index, item) {
			items.push(item.factoryCode + '=' + item.factoryName);
		});

		//alert(items.join(','));
	});

	// =======================================================================================
	var selectUserId;
	// 分配角色信息
	function grantUser() {
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems) {
			Common.alert("请选择进行角色配置的用户");
			return;
		}
		selectUserId = checkedItems.userId;
		grid2 = $('#roleSettingList').datagrid({
			idField : 'role_id',
			striped : true,
			dataType : 'json',
			url : Common.getUrl('rolesctrl/getlist'),
			pagination : true,
			pagePosition : 'bottom',
			pageNumber : 1,
			pageSize : Common.pageMiniSize,
			pageList : Common.pageList,
			toolbar : [ {
				text : '提交',
				iconCls : 'icon-add',
				handler : addToUser
			},'-', {
				text : '清空角色',
				iconCls : 'icon-remove',
				handler : delUserRole
			}],
			multiSort : false,
			remoteSort : false,
			// 加载成功后，反显用户已有角色
			onLoadSuccess : function(gridObj) {
				$.ajax({
					timeout : 15000,
					async : true,
					cache : false,
					type : 'POST',
					url : Common.getUrl('userctrl/selectuserroles'),
					dataType : 'json',
					data : {
						'se_user_id' : checkedItems.userId
					},
					success : function(data) {
						if (data.result == 0) {
							grid2.datagrid('clearSelections');
							// 用户角色信息回显
							$.each(gridObj.rows, function(index, item) {
								var arrlist = data.datalist;
								for (var i = 0; i < arrlist.length; i++) {
									var roleId = arrlist[i].roleId;
									if (item.role_id == roleId) {
										grid2.datagrid('checkRow', index);
									}
								}
							});
						} else {
							Common.alert(data.message);
						}

					},
					error : function(errdata) {
						Common.alert(data.message);
					}
				});
			}
		});

		divWindow3.window('open');
		// 设置分页控件显示汉字

		grid2.datagrid('getPager').pagination({
			beforePageText : Common.beforePageText,
			afterPageText : Common.afterPageText,
			displayMsg : Common.displayMsg
		});

	}

	// 为用户添加角色信息
	function addToUser() {
		var checkedItems = grid2.datagrid('getSelections');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.role_id);
		});
		var roleids = items.join(',');

		if (checkedItems.length > 0) {
			$.ajax({
				timeout : 15000,
				async : true,
				cache : false,
				type : 'POST',
				url : Common.getUrl('userctrl/saveuserroles'),
				dataType : 'json',
				data : {
					'sa_user_id' : selectUserId,
					'sa_role_ids' : roleids
				},
				success : function(data) {
					if (data.result == 0) {
						grid.datagrid('clearSelections');
						Common.alert(data.message);
					} else {
						Common.alert(data.message);
					}
					divWindow3.window('close');

				},
				error : function(errdata) {
					Common.alert(data.message);
					divWindow3.window('close');
				}
			});
		} else {
			Common.alert('请先选择要设置的数据！');
		}
	}
	
	// 清空用户所有角色
	function delUserRole() {
		var checkedItems = grid2.datagrid('getSelections');
		if (checkedItems.length > 0) {
			$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
			    if (r){    
			    	$.ajax({
						timeout : 15000,
						async : true,
						cache : false,
						type : 'POST',
						url : Common.getUrl('userctrl/deluserroles'),
						dataType : 'json',
						data : {
							'sa_user_id' : selectUserId,
						},
						success : function(data) {
							if (data.result == 0) {
								grid.datagrid('clearSelections');
								Common.alert(data.message);
							} else {
								Common.alert(data.message);
							}
							divWindow3.window('close');

						},
						error : function(errdata) {
							Common.alert(data.message);
							divWindow3.window('close');
						}
					});   
			    }    
			});  
			
		} else {
			Common.alert('请先选择要设置的数据！');
		}
	}

	
	$.extend($.fn.datagrid.methods, {  
		emptyItem: function(jq, items){  
	          return jq.each(function(){  
	             var toolbar =$(this).parent().prev("div.datagrid-toolbar");//toolbar; 
	             toolbar.empty();
	             toolbar.css('background','#F4F4F4').css('width','100%').css('overflow','hidden');
	          });  
	     },
	      addToolbarItem: function(jq, items){  
	    	  var toolbar;
	    	  return jq.each(function(){  
	              toolbar =$(this).parent().prev("div.datagrid-toolbar");//toolbar; 
	              for(var i = 0;i<items.length;i++){
	                  var item = items[i];
	                 if(item === "-"){
	                      toolbar.append('<div class="datagrid-btn-separator"></div>');
	                  }else{
	                     var btn=$("<a href=\"javascript:void(0)\"></a>");
	                     btn[0].onclick=eval(item.handler||function(){});
	                     btn.css("float","left").css('background','#F4F4F4').appendTo(toolbar).linkbutton($.extend({},item,{plain:true}));
	                 }
	             }
	              toolbar = null;
	         });  
           
	     }            
	 });
});