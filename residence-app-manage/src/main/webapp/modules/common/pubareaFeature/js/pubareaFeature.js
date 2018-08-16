$(function() {

	var toolbarres;
	$.ajax({
		timeout	: 15000,
		async	: true,
		cache	: false,
		type	: 'POST',
		url		: Common.getUrl('pubareaFeaturectrl/getToolbar'),
		dataType: 'json',
		data	: {
		},
		beforeSend: function() {
		},
		success	: function(data) {
			//toolbarres = data.result;
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
		url : Common.getUrl('pubareaFeaturectrl/areaFeaturelist'),
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
		title : '地方特色信息添加'
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
		title : '修改地方特色信息'
	});
	var divForm2 = divWindow2.find('form');

	displayWindow = $('#display_content').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 750,
		height : 400,
		title : '查看地方特色内容'
	});
	// 添加地方特色信息表信息 start显示添加用户窗口
	function addInfo() {
		$("#btnSave").removeAttr("disabled");// 启用按钮
		divWindow.window('open');
		divForm.form('clear');
		divForm.url = Common.getUrl('pubareaFeaturectrl/addareaFeature');
		$('#rank').textbox('setValue',"999");
		CKEDITOR.instances['add_content'].setData("");	
	}
	
	$('#btnSave').click(function() {
		// 验证表单信息是否满足规则
		if (!divForm.form('validate')) {
			Common.alert("请根据提示完善表单信息！");
			return false;
		}	
		var areaFeatureContent = CKEDITOR.instances['add_content'].getData();
		if(areaFeatureContent==''){
			Common.alert("文章内容不能为空!");
			return;
		}
		var filename = $('#areaFeatureImg').val();
		if (filename != '') {
			var filetype = filename.substring(filename.indexOf("."), filename.length).toLowerCase();
			if (".jpg" != filetype && ".png" != filetype && ".bmp" != filetype && ".gif" != filetype) {
				Common.alert('超出上传范围的图片格式');
				return;
			}
		}else{
			Common.alert("请上传图片！");
			return;
		}
			
		divForm.ajaxSubmit({
			url : divForm.url,
			dataType : "json",
			data : {
				'areaFeatureContent' : areaFeatureContent
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
	// 添加地方特色信息表信息 end 保存信息及提交数据库处理

	// 修改地方特色信息表信息
	function editInfo() {
		divForm2.url = Common.getUrl('pubareaFeaturectrl/editareaFeature');
		$("#btnSave2").removeAttr("disabled");// 启用按钮
		var checkedItems = grid.datagrid('getChecked');
		if (checkedItems.length < 1) {
			Common.alert('请先选择要编辑的数据！');
			return;
		}
		if (checkedItems.length > 1) {
			Common.alert('无法同时修改多条数据！');
			return;
		}
		divWindow2.window('open');
		$('#areaFeatureType2').combobox('setValue',checkedItems[0].areaFeatureTypeValue);
		$('#areaId2').combotree('setValues', checkedItems[0].areaIdList.split(','));
		$('#areaFeatureTitle2').textbox('setValue', checkedItems[0].areaFeatureTitle);
		CKEDITOR.instances['edit_content'].setData(checkedItems[0].areaFeatureContent);
		$('#areaFeatureFrom2').textbox('setValue', checkedItems[0].areaFeatureFrom);
		//$('#areaFeatureDesc2').val(checkedItems[0].areaFeatureDesc);
		$('#rank2').textbox('setValue',checkedItems[0].rank);
	}

	$('#btnSave2').click(function() {
		var areaFeatureId;
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			areaFeatureId = item.areaFeatureId;
		});
		var areaFeatureTitle = $('input#areaFeatureTitle2').textbox('getValue');
		var areaFeatureFrom = $('input#areaFeatureFrom2').textbox('getValue');
		var areaFeatureContent = CKEDITOR.instances['edit_content'].getData();
		var filename = $('#areaFeatureImg2').val();
		var areaList = $('#areaId2').combotree('getText');
		var rank = $('#rank2').textbox('getValue');
		if(areaList==''){
			Common.alert("所属区域不能为空!");
			return;
		}	
		if(areaFeatureTitle==''){
			Common.alert("文章标题不能为空!");
			return;
		}
		if(areaFeatureContent==''){
			Common.alert("文章内容不能为空!");
			return;
		}

		if(areaFeatureFrom==''){
			Common.alert("文章来源不能为空!");
			return;
		}
		if(rank==''){
			Common.alert("文章排序不能为空!");
			return;
		}		
		if (filename != '') {
			var filetype = filename.substring(filename.indexOf("."), filename.length).toLowerCase();
			if (".jpg" != filetype && ".png" != filetype && ".bmp" != filetype && ".gif" != filetype) {
				Common.alert('超出上传范围的图片格式');
				return;
			}
		}
			
		divForm2.ajaxSubmit({
			url : divForm2.url,
			dataType : "json",
			data : {
				'areaFeatureContent' : areaFeatureContent,
				'areaFeatureId' : areaFeatureId,
				'rank' : rank
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

	// 删除地方特色表记录
	function deleteInfo() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.areaFeatureId);
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
						url : Common.getUrl('pubareaFeaturectrl/delareaFeature'),
						dataType : 'json',
						data : {
							'areaFeatureId' : ids
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
	// 设置置顶
	function addBar() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.areaFeatureId);
		});
		var ids = items.join(',');

		if (checkedItems.length > 0) {
			Common.confirm('确定要设置置顶当前选择的记录吗？', function(r) {
				if (r) {
					grid.datagrid('load');
					grid.datagrid('clearSelections');

					$.ajax({
						timeout : 15000,
						async : true,
						cache : false,
						type : 'POST',
						url : Common.getUrl('pubareaFeaturectrl/setBar'),
						dataType : 'json',
						data : {
							'areaFeatureId' : ids
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
			Common.alert('请先选择要设置置顶的数据！');
		}

	}
	
	// 取消置顶
	function removeBar() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.areaFeatureId);
		});
		var ids = items.join(',');

		if (checkedItems.length > 0) {
			Common.confirm('确定要取消置顶当前选择的记录吗？', function(r) {
				if (r) {
					grid.datagrid('load');
					grid.datagrid('clearSelections');

					$.ajax({
						timeout : 15000,
						async : true,
						cache : false,
						type : 'POST',
						url : Common.getUrl('pubareaFeaturectrl/removeBar'),
						dataType : 'json',
						data : {
							'areaFeatureId' : ids
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
			Common.alert('请先选择要取消置顶的数据！');
		}

	}	
	//显示详细信息
	function showInfo(){
		
	} 
	// 查询用户信息
	$('#search').click(function() {
		var areaFeatureTitle1 = $('#areaFeatureTitle1').textbox('getText');
		var createTime1 = $('#createTime1').datebox('getText');
		var issueTime1 = $('#issueTime1').datebox('getText');
		var areaFeatureType1 = $('#areaFeatureType1').combobox('getValue');
		var areaFeatureBar1 = $('#areaFeatureBar1').combobox('getValue');
		// 使用参数查询
		grid.datagrid('load', {
			'areaFeatureTitle' : areaFeatureTitle1,
			'createTime' : createTime1,
			'issueTime' : issueTime1,
			'areaFeatureType' : areaFeatureType1,
			'areaFeatureBar' : areaFeatureBar1
		});
	});
	
	$('#return').click(function() {
		$('#areaFeatureTitle1').textbox('setValue','');
		$('#createTime1').datebox('setValue','');
		$('#issueTime1').datebox('setValue','');
		//var areaFeatureTitle1=$('#areaFeatureTitle1');
		//var createTime1=$('#createTime1');
		//var issueTime1=$('#issueTime1');
		grid.datagrid('load', {
			'areaFeatureTitle' : '',
			'createTime' : '',
			'issueTime' : ''
		});
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


function functionIcon(val,row){
	    if (val) {
		return ' <img id="merchantLogo2" src=' + val+ ' alt="" width="35" height="35" align="middle"/>';
	  } else {
		//return ' <img id="merchantLogo2" src=../../resources/images/default_image.png alt="" width="100" height="100" align="middle"/>';
	         }
}

function functionLink(value,row,index){
	if(value){
		return "<a onclick="+"display('" + row.areaFeatureId + "') style='cursor:pointer;color:blue'>" + "查看" + "</a>";
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
		url : Common.getUrl('pubareaFeaturectrl/querycontent'),
		dataType : 'json',
		data : {
			'iId' : pkId
		},
		success : function(data) {
			CKEDITOR.instances['display_content'].setData(data.areaFeature.areaFeatureContent);
			$('#gridList').datagrid('clearSelections');
			displayWindow.window('open');
		},
		error : function(errdata) {
			Common.alert('请求服务器失败，操作未能完成！');
		}
	});
}

