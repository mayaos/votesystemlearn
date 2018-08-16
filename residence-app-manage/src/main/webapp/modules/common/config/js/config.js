$(function() {

	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped : true,
		dataType : 'json',
		url : Common.getUrl('configctrl/configlist'),
		pagination : true,
		pagePosition : 'bottom',
		pageNumber : 1,
		pageSize : Common.pageSize,
		pageList : Common.pageList,
		toolbar : [ {
			text : '添加',
			iconCls : 'icon-add',
			handler : addInfo
		}, '-', {
			text : '修改',
			iconCls : 'icon-edit',
			handler : editInfo
		}, '-', {
			text : '删除',
			iconCls : 'icon-remove',
			handler : delInfo
		}
		],
		multiSort : false,
		remoteSort : false
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
		width : 450,
		height : 370,
		title : '添加参数信息编辑'
	});
	var divForm = divWindow.find('form');

	var divWindow2 = $('#divWindow2').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 450,
		height : 350,
		title : '修改参数信息编辑'
	});
	var divForm2 = divWindow2.find('form');

	// 添加配置信息 start 显示添加用户窗口
	function addInfo() {
		$("#btnSave").removeAttr("disabled");// 启用按钮
		divWindow.window('open');
		divForm.form('clear');
		divForm.url = Common.getUrl('configctrl/addconfiginfo');
	}

	$('#btnSave').click(function() {
		var paramName = $('input#param_name');
		
		if (paramName.val() == '') {
			paramName.focus();
			Common.alert('参数名称不能为空');
			return;
		}

		var filename=$('#up_imgsrc').val();
		if (filename != '') {
			var filetype = filename.substring(filename.indexOf("."),filename.length).toLowerCase();
			if(".jpg" != filetype && ".png" != filetype && ".bmp" != filetype && ".gif" != filetype) {
				Common.alert('超出上传范围的图片格式');
				return;
			}
		}
		
		divForm.ajaxSubmit({
			url : divForm.url,
			dataType: "json",
			success : function(data) {
				if(data.result == 0){
                	grid.datagrid('load');
  					divWindow.window('close');
  					grid.datagrid('clearSelections');
  					Common.alert(data.message);
                }else {
                	Common.alert(data.message);
                }
			},
			error:function(data){ 
				Common.alert('请求服务器失败，操作未能完成！');
            } 		
		});
	});
	
	$('#btnCancel').click(function() {
		divWindow.window('close');
	});
	// 添加配置信息 end 保存信息及提交数据库处理

	// 修改配置信息
	function editInfo() {
		divForm2.url = Common.getUrl('configctrl/updateconfiginfo');
		$("#btnSave2").removeAttr("disabled");// 启用按钮
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems) {
			Common.alert('请先选择要编辑的数据！');
			return;
		}
		divWindow2.window('open');
		$('#param_id2').textbox('setValue', checkedItems.paramId);
		$('#img_src').textbox('setValue', checkedItems.upImgsrc);
		$('#param_name2').textbox('setValue', checkedItems.paramName);
		$('#param_value2').textbox('setValue', checkedItems.paramValue);
		$('#param_desc2').textbox('setValue', checkedItems.paramDesc);
		$('#param_valid2').combobox('setValue', checkedItems.paramValid);
	}

	$('#btnSave2').click(function() {
		var paramName = $('input#param_name2');

		if (paramName.val() == '') {
			paramName.focus();
			Common.alert('参数名称不能为空');
			return;
		}

		var filename=$('#up_imgsrc2').val();
		if (filename != '') {
			var filetype = filename.substring(filename.indexOf("."),filename.length).toLowerCase();
			if(".jpg" != filetype && ".png" != filetype && ".bmp" != filetype && ".gif" != filetype) {
				Common.alert('超出上传范围的图片格式');
				return;
			}
		}
		
		divForm2.ajaxSubmit({
			url : divForm2.url,
			dataType: "json",
			success : function(data) {
				if(data.result == 0){
                	grid.datagrid('load');
  					divWindow2.window('close');
  					grid.datagrid('clearSelections');
  					Common.alert(data.message);
                }else {
                	Common.alert(data.message);
                }
			},
			error:function(data){ 
				Common.alert('请求服务器失败，操作未能完成！');
            } 		
		});
	});
	$('#btnCancel2').click(function() {
		divWindow2.window('close');
	});

	// 删除配置信息
	function delInfo() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		var items2 = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.paramId);
			items2.push(item.upImgsrc);	
		});
		var paramIds = items.join(',');
		var upImgsrcs = items2.join(',');

		if (checkedItems.length > 0) {
			Common.confirm('确定要删除当前选择的记录吗？', function(r) {
				if (r) {
					grid.datagrid('load');
					grid.datagrid('clearSelections');

					$.ajax({
						timeout : 15000,
						async : true,
						cache : false,
						type : 'POST',
						url : Common.getUrl('configctrl/deleteconfig'),
						dataType : 'json',
						data : {
							'arrParamIds' : paramIds,
							'arrUpImgsrcs' : upImgsrcs
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
			alert('请先选择要删除的数据！');
		}
	}

	// 查询配置信息
	$('#search').click(function() {
		var paramName = $('#paramName').textbox('getText');
		var paramValue = $('#paramValue').textbox('getText');
		// 使用参数查询
		grid.datagrid('load', {
			'strParamName' : paramName,
			'strParamValue' : paramValue
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

		alert(items.join(','));
	});
	
	divWindow3 = $('#divWindow3').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:600,
		height:300,
		title: '图片预览'
	});	
});

function functionLink(upImgsrc,row,index){
	if (upImgsrc == undefined) {
		return;
	} else {
		var filetype = upImgsrc.substring(upImgsrc.indexOf("."), upImgsrc.length).toLowerCase();
		if (".jpg" != filetype && ".png" != filetype && ".bmp" != filetype && ".gif" != filetype) {
			return upImgsrc;
		} else { 	
			var s = "<a href='#' onclick=\"showFile('"+upImgsrc+"');\">查看图片</a>";
			return s;
		}
	}
}

function showFile(upImgsrc){
	$.ajax({
		timeout : 15000,
		async : true,
		cache : false,
		type : 'POST',
		url : Common.getUrl('configctrl/showimage'),
		dataType : 'json',
		success : function(data) {
			$("#imgId").attr("src",data.webapps + upImgsrc);
			divWindow3.window('open');
		}
	});	
}