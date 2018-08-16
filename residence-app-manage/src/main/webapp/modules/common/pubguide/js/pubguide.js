$(function() {
	$.ajax({
		timeout	: 15000,
		async	: true,
		cache	: false,
		type	: 'POST',
		url		: Common.getUrl('guidectrl/getToolbar'),
		dataType: 'json',
		data	: {
		},
		beforeSend: function() {
		},
		success	: function(data) {
			//清空diiv
			$('#gridList').datagrid("emptyItem",'');
			//循环添加
				for (var i = 0; i < data.result.length; i++) {  
					var text = data.result[i].text+"";
					var icon = data.result[i].icon+"";
					var handler = data.result[i].handler+"";
					$('#gridList').datagrid("addToolbarItem",[{"text" : text,"iconCls" : icon,"handler" : handler},"-"]);
				}
			
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
		url : Common.getUrl('guidectrl/guidelist'),
		pagination : true,
		pagePosition : 'bottom',
		pageNumber : 1,
		pageSize : Common.pageSize,
		pageList : Common.pageList,
		toolbar : toolbar,
		multiSort : false,
		remoteSort : false,
		onLoadSuccess : function(data) {}
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
		width : 750,
		height : 400,
		title : '指南表信息添加'
	});
	var divForm = divWindow.find('form');

	var divWindow2 = $('#divWindow2').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 750,
		height : 400,
		title : '修改指南表'
	});
	var divForm2 = divWindow2.find('form');
	
	var divWindow3 = $('#divWindow3').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 600,
		height : 800,
		title : '用户角色信息配置'
	});

	
	displayWindow = $('#display_content').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 750,
		height : 400,
		title : '查看指南内容'
	});


	
	// 添加指南信息表信息 start显示添加用户窗口
	function addInfo() {
		$("#btnSave").removeAttr("disabled");// 启用按钮
		divWindow.window('open');
		divForm.form('clear');
		CKEDITOR.instances['add_content'].setData("");
		divForm.url = Common.getUrl('guidectrl/addguide');
	}

	$('#btnSave').click(function() {
		var guideType =  $('#guideType').combobox('getValue'); 
		var areaId =  $('#areaId').combobox('getValue'); 
		var guideContent = CKEDITOR.instances['add_content'].getData();
		var valid = $('#valid').combo('getValue');
		//var createTime = $('#createTime').datebox('getValue');
		
		if(guideType==''){
			Common.alert("指南类型不能为空!");
			return;
		}
		if(areaId==''){
			Common.alert("区域ID不能为空!");
			return;
		}

		if(valid==''){
			Common.alert("是否有效不能为空!");
			return;
		}
		/*if(createTime==''){
			Common.alert("创建时间不能为空!");
			return;
		}*/
		if(guideContent==''){
			Common.alert("指南内容不能为空!");
			return;
		}
		$.ajax({
			timeout : 15000,
			async : true,
			cache : false,
			type : 'POST',
			url : divForm.url,
			dataType : 'json',
			data : {
				'guideType' : guideType,
				'areaId' : areaId,
				'valid' : valid,
				//'createTime' : createTime,
				'guideContent' : guideContent
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
	// 添加指南信息表信息 end 保存信息及提交数据库处理

	// 修改指南信息表信息
	function editInfo() {
		divForm2.url = Common.getUrl('guidectrl/editguide');
		$("#btnSave2").removeAttr("disabled");// 启用按钮
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems) {
			Common.alert('请先选择要编辑的数据！');
			return;
		}
		divWindow2.window('open');
		$('#guideType2').combobox('select',checkedItems.guideType)
		$('#guideId2').textbox('setValue',checkedItems.guideId);
		$('#guideType').textbox('setValue', checkedItems.guideName);
		//区域内容回显
		$("#areaId1").textbox('setValue',checkedItems.areaName);
		$("#areaIdHidden").html(checkedItems.areaId);
		CKEDITOR.instances['edit_content'].setData(checkedItems.guideContent);
	}

	$('#btnSave2').click(function() {
		var guideId;
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
		 guideId = item.guideId;
		});
		var guideType2 = $('#guideType2').combobox('getValue');
		var guideContent2 = CKEDITOR.instances['edit_content'].getData();
		//var createTime2 = $('#createTime2').datebox('getValue');
		var guideId2 = $('#guideId2').textbox('getValue');
		var valid2 = $('#valid2').combo('getValue');
		//区域内容回显
		//areaIdHidden 得到的是areaId， areaId2得到的可能是areaId，也有可能是areaName
		var areaId;
		if(!isNaN($('#areaId1').combobox('getValue'))) {
			//是数字
			areaId = $('#areaId1').combobox('getValue');
		} else {
			areaId = $('#areaIdHidden').html();
		}
		
		if(guideType2==''){
			Common.alert("请选择指南类别!");
			return;
		}
		if(areaId==''){
			Common.alert("请选择地区!");
			return;
		}
		
		if(guideContent2==''){
			Common.alert("指南内容不能为空!");
			return;
		}
		
		if(valid2==''){
			Common.alert("请选择是否有效");
			return;
		}
		$.ajax({
			timeout : 15000,
			async : true,
			cache : false,
			type : 'POST',
			url : divForm2.url,
			dataType : 'json',
			data : {
				'guideType' : guideType2,
				'areaId' : areaId,
				'guideContent' : guideContent2,
				'valid' : valid2,
				'guideId':guideId2
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
	
	$('#btnSave2').click(function() {
		var guideId;
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
		 guideId = item.guideId;
		});
		var guideType3 = $('input#guideType3');
		
		var areaId3 =$('input#areaId3');
	
		var guideContent3 = $('#guideContent3');
		
		var userId3 = $('input#userId3');
		
		var readTimes3= $('input#readTimes3');
	
		var valid3 = $('#valid3').combobox('getValue');
		
		var createTime3 = $('#createTime3').textbox('getValue');
		var updateTime3 = $('#updateTime3').textbox('getValue');
		
		var guideId3 = $('#guideId3').textbox('getValue');
		
		$.ajax({
			timeout : 15000,
			async : true,
			cache : false,
			type : 'POST',
			url : divForm2.url,
			dataType : 'json',
			data : {
				'guideType' : guideType3.val(),
				'areaId' : areaId3.val(),
				'guideContent' : guideContent3.val(),
				'userId' : userId3.val(),
				'createTime' : createTime3.val(),
				'readTimes' : readTimes3.val(),
				'updateTimes' :updateTimes3.val(),
				'valid' : valid3,
				'guideId':guideId3
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
	$('#btnCancel3').click(function() {
		divWindow4.window('close');
	});
	// 删除指南信息表记录
	function deleteInfo() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.guideId);
		});
		var ids = items.join(',');
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
						url : Common.getUrl('guidectrl/delguide'),
						dataType : 'json',
						data : {
							'guideId' : ids
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
			Common.alert('请先选择要删除的数据！');
		}

	}

	// 查询用户信息
	$('#search').click(function() {
		var areaIdSearch = $('#areaIdSearch').textbox('getValue');
		var guideTypeSearch = $('#guideTypeSearch').textbox('getValue');
		var issueOrNotSearch = $('#issueOrNotSearch').textbox('getValue');
		var createTime1 = $('#createTime1').datebox('getText');
		// 使用参数查询
		grid.datagrid('load', {
			'areaIdSearch' : areaIdSearch,
			'guideTypeSearch' : guideTypeSearch,
			'issueOrNotSearch' : issueOrNotSearch,
			'createTime' : createTime1
		});
	});
	
	$('#return').click(function() {
		$('#areaIdSearch').combotree('clear');
		$('#guideTypeSearch').combobox('clear');
		$('#issueOrNotSearch').combobox('clear');
		$('#createTime1').datebox('clear');
		
	});
	
	$('#chkbox').click(function() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();

		$.each(checkedItems, function(index, item) {
			items.push(item.factoryCode + '=' + item.factoryName);
		});

		Common.alert(items.join(','));
	});

	
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


function functionLink(value,row,index){
	if(value){
		return "<a onclick="+"display('" + row.guideId + "') style='cursor:pointer;color:blue'>" + "查看" + "</a>";
	}
	else{
		return "";
	}
}

function display(pkId){
	$.ajax({
		timeout : 15000,
		async : true,
		cache : false,
		type : 'POST',
		url : Common.getUrl('guidectrl/querycontent'),
		dataType : 'json',
		data : {
			'iId' : pkId
		},
		success : function(data) {
			//CKEDITOR.instances['edit_content'].setData(checkedItems.guideContent);
			CKEDITOR.instances['display_content'].setData(data.guide.guideContent);
			$('#gridList').datagrid('clearSelections');
			displayWindow.window('open');
		},
		error : function(errdata) {
			Common.alert('请求服务器失败，操作未能完成！');
		}
	});
}

