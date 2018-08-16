/**
 * software.js
 * 封装了与软件信息有关的的脚本
 */

$(function() {
//-------------初始化表格与查询功能-----------------------------------------------		
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('softwarectrl/softwareList'),
		loadMsg:'数据加载中...',
		pagination: true,
		pagePosition: 'bottom',
		pageNumber: 1,
		toolbar: [{}],
		pageSize: Common.pageSize,
		pageList: Common.pageList,
		multiSort: false,
		remoteSort: false
	});
	
	//动态获取工具菜单
	$.ajax({
		timeout	: 15000,
		async	: true,
		cache	: false,
		type	: 'POST',
		url		: Common.getUrl('softwarectrl/getToolbar'),
		dataType: 'json',
		beforeSend: function() {
		},
		success	: function(data) {
			var jsToolbar = data.result; 
			grid.datagrid("emptyItem",'');
			for (var i=0; i<jsToolbar.length; i++) {
				var text = jsToolbar[i].text+"";
				var icon = jsToolbar[i].icon+"";
				var handler = jsToolbar[i].handler+"";
				grid.datagrid('addToolbarItem', [{"text":text, "iconCls":icon, "handler":handler}, "-"]);
			}
		},
		error: function(errdata) {
			Common.alert('请求加载工具菜单服务器失败，操作未能完成！');
		}
	});
	
	//扩展datagrid对象方法
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
	
	//条件 查询
	$('#search').click(function() {
		var softVersion = $('#softwareVersion').textbox('getText');
		var uploadDateStart = $('#uploadDateStart').datebox('getValue');
		var uploadDateEnd = $('#uploadDateEnd').datebox('getValue');
		// 使用参数查询
		grid.datagrid('load', {
			'softVersion' : softVersion,
			'uploadDateStart' : uploadDateStart,
			'uploadDateEnd' : uploadDateEnd
		});
	});
	
	//条件查询返回
	$('#return').click(function() {
		$('#softwareVersion').textbox('setValue','');
		$('#uploadDateStart').textbox('setValue','');
		$('#uploadDateEnd').textbox('setValue','');
		
		var softwareVersion=$('#softwareVersion');	
		var uploadDateStart=$('#uploadDateStart');
		var uploadDateEnd=$('#uploadDateEnd');
		grid.datagrid('load', {
			'softwareVersion' : softwareVersion.val(),
			'uploadDateStart' : uploadDateStart.val(),
			'uploadDateEnd' : uploadDateEnd.val()
		});
	});
	
	// 设置分页控件显示汉字
	grid.datagrid('getPager').pagination({
		beforePageText : Common.beforePageText,
		afterPageText : Common.afterPageText,
		displayMsg : Common.displayMsg
	});
//-------------初始化表格与查询功能结束-------------------------------------------------------------
	
//-------------新增安装包功能--------------------------------------------------------------------
	
	var divWindow = $('#divWindow').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 450,
		height : 320,
		title : '新增'
	});
	
	var divForm = divWindow.find('form');
	
	// 点击添加按钮
	function addInfo() {
		$("#btnSave").removeAttr("disabled");// 启用按钮
		divWindow.window('open');
		divForm.form('clear');
		//$('select #forceUpgrade option[value=false]').attr('seleted',true);
		$("#softType").combobox({
		    value: 'android',
		})
		$("#forceUpgrade").combobox({
		    value: '否',
		})
		divForm.url = Common.getUrl('softwarectrl/addSoftware');
	}
	
	// 提交软件信息
	$('#btnSave').click(function() {	
		//检查输入参数是否合规
		var softType = $("#softType").textbox('getValue');
		var forceUpgrade = $("#forceUpgrade").textbox('getValue');
		var softVersion = $("#softVersion").textbox('getValue');
		var file1 = $("#file1").textbox('getValue');
		if (!softType) {
			$("#softType").focus();
			Common.alert('软件类型不能为空');
			return false;
		}
		if (!forceUpgrade) {
			$("#forceUpgrade").focus();
			Common.alert('强制升级不能为空');
			return false;
		}

		if (!softVersion) {
			Common.alert('软件版本不能为空');
			$("#softVersion").focus();
			return false;
		}
		var reg = /^v\d{1,2}\.\d{1,2}\.\d{1,2}$/; 
		if(!reg.test(softVersion)) {
			$("#softType").focus();
			Common.alert('软件版本格式不对，请重新输入，格式参考：v1.0.0');
			return false;
		}
		if (!file1) {
			$("#file1").focus();
			Common.alert('上传安装包不能为空');
			return false;
		}
		
		divForm.ajaxSubmit({
			url : divForm.url,
			type: 'post', 
			dataType : "json",
			beforeSend : function() {
				Common.masker.open('文件上传中，请耐心等待...');
			},
			success : function(data) {
				if (data.result == 0) {
					grid.datagrid('load');
					divWindow.window('close');
					grid.datagrid('clearSelections');
					Common.alert(data.message);
				} else {
					Common.alert(data.message);
				}
				Common.masker.close();
			},
			error : function(data) {
				Common.masker.close();
				Common.alert('请求服务器失败，操作未能完成！');			
			}
		});
	});
	
	
	$('#btnCancel').click(function() {
		divWindow.window('close');
	});
	
//-------------新增安装包功能结束--------------------------------------------------------------------	

//-------------删除功能开始------------------------------------------------------------------------
	function deleteInfo() {
		var checkedObj = grid.datagrid('getSelected');
		if(!checkedObj){
			Common.alert('请先选择要删除的数据！');
			return;
		}
		var softId = checkedObj.softId;
		var softURL = checkedObj.softURL;
		Common.confirm('确定要删除当前选择的记录吗？', function(r) {
			if (r) {
				grid.datagrid('load');
				grid.datagrid('clearSelections');
				$.ajax({
					timeout : 15000,
					async : true,
					cache : false,
					type : 'POST',
					url : Common.getUrl('softwarectrl/delSoftware'),
					dataType : 'json',
					data : {
						'softId' : softId,
						'softURi' :softURL
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
	}
//-------------删除功能结束------------------------------------------------------------------------
	
//-------------修改功能开始------------------------------------------------------------------------	
	//定义更新窗口属性
	var divWindow1 = $('#divWindow1').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 450,
		height : 320,
		title : '更新'
	});
	
	var divForm1 = divWindow1.find('form');
	// 修改地区信息
	function editInfo() {
		divForm1.url =  Common.getUrl('softwarectrl/updateSoftware');
		//把选中的那行数据存在row中
		var row = grid.datagrid('getSelected');
		//把数据存入divForm1
		if (row){
			divForm1.form('load', row);//从页面获取值
			divWindow1.window('open');
		} else {
			Common.alert('请先选择要修改的数据！');
		}
		
		//------------
		$("#btnSave1").removeAttr("disabled");// 启用按钮
		divForm1.form('clear');
		if(row.softType == 'android') {
			$("#softType1").combobox('select','android');
		} else {
			$("#softType1").combobox('select','ios');
		}
		if(row.forceUpgrade == '是') {
			$("#forceUpgrade1").combobox('select','是');
		} else {
			$("#forceUpgrade1").combobox('select','否');
		}
				
		//给编辑框添加默认值
		$('#softVersion1').textbox('setValue', row.softVersion);
		$('#softNotes1').val( row.softNotes);
		$('#softId1').textbox('setValue', row.softId);
		//软件ID框设置不允许修改
		$('#softId1').textbox('readonly',true);
	}
	
	// 提交软件信息
	$('#btnSave1').click(function() {	
		//检查输入参数是否合规
		var softType = $("#softType1").textbox('getValue');
		var forceUpgrade = $("#forceUpgrade1").textbox('getValue');
		var softVersion = $("#softVersion1").textbox('getValue');
		var softid = $("#softId1").textbox('getValue');
		
		if(!softid) {
			Common.alert('软件ID不能为空');
			return false;
		}
		
		if (!forceUpgrade) {
			$("#forceUpgrade").focus();
			Common.alert('强制升级不能为空');
			return false;
		}

		if (!softVersion) {
			Common.alert('软件版本不能为空');
			$("#softVersion").focus();
			return false;
		}
		var reg = /^v\d{1,2}\.\d{1,2}\.\d{1,2}$/; 
		if(!reg.test(softVersion)) {
			$("#softType").focus();
			Common.alert('软件版本格式不对，请重新输入，格式参考：v1.0.0');
			return false;
		}
		if (!file1) {
			$("#file1").focus();
			Common.alert('上传安装包不能为空');
			return false;
		}
		
		divForm1.ajaxSubmit({
			url : divForm1.url,
			type: 'post', 
			dataType : "json",
			success : function(data) {
				if (data.result == 0) {
					grid.datagrid('load');
					divWindow1.window('close');
					grid.datagrid('clearSelections');
					Common.alert(data.message);
				} else {
					Common.alert(data.message);
				}
			},
			error : function(data) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});
	});
	
	
	$('#btnCancel1').click(function() {
		divWindow1.window('close');
	});
	
//-------------修改功能结束------------------------------------------------------------------------	
});