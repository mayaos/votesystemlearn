/**
 * area.js
 * 封装了与区域管理有关的的脚本
 */

$(function(){

	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('areactrl/areaList'),
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
		url		: Common.getUrl('areactrl/getToolbar'),
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
	
	// 设置分页控件显示汉字
	grid.datagrid('getPager').pagination({ 
		beforePageText: Common.beforePageText, 
		afterPageText: Common.afterPageText, 
		displayMsg: Common.displayMsg
	});
	
	var divWindow = $('#divWindow').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:500,
		title: '地区信息编辑'
	});
	
	var divForm = divWindow.find('form');
	
	//条件查询
	$('#search').click(function() {
		var areaNameSearch = $('#areaNameSearch').textbox('getText');
		// 使用参数查询
		grid.datagrid('load', {
			'areaNameSearch' : areaNameSearch
		});
	});
	
	//条件查询返回
	$('#return').click(function() {
		$('#areaNameSearch').textbox('setValue','');		
		var areaNameSearch=$('#areaNameSearch');		
		grid.datagrid('load', {
			'areaNameSearch' : areaNameSearch.val(),
		});
	});
	
	// 添加和编辑地区信息
	$('#btnSave').click(function() {
		var areaId = $("#areaId").textbox('getValue');
		var areaName = $("#areaName").textbox('getValue');
		var areaFullName = $("#areaFullName").textbox('getValue');

		if(!$("#addForm").form('validate')){
			return false;
		}

		var re=/[0-9][0-9][0-9][0-9][0-9][0-9]/;
		if (!re.test(areaId)) {
			$("#areaId").focus();
			Common.alert('地区代码输入不合法，必须为数字[0-9]。');
			return false;
		}

		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: divForm.url,
			dataType: 'json',
			data	: {
				'areaId': areaId,
				'areaName':areaName,
				'areaFullName': areaFullName
			},
	
			success	: function(data) {
				if(data.result == 0){
					grid.datagrid('load');
					divWindow.window('close');
					Common.alert(data.message);
				}else {
					Common.alert(data.message);
				}
			},
			error: function() {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});
	});
	
	$('#btnCancel').click(function() {
		divWindow.window('close');
	});
	
	// 添加地区信息
	function addInfo() {
		$('#areaId').textbox('enable',false);
		divWindow.window('open');
		divForm.form('clear');
		divForm.url = Common.getUrl('areactrl/addArea');
	}
	
	// 修改地区信息
	function editInfo() {
		divForm.url =  Common.getUrl('areactrl/updateArea');
		//把选中的那行数据存在row中
		var row = grid.datagrid('getSelected');
		//把数据存入divForm
		if (row){
			divForm.form('load', row);//从页面获取值
			divWindow.window('open');
		} else {
			Common.alert('请先选择要编辑的数据！');
		}
		//给编辑框添加默认值
		$('#areaId').textbox('setValue', row.areaID);
		$('#areaName').textbox('setValue', row.areaName);
		$('#areaFullName').textbox('setValue', row.fullName);
		//设置areaId框不能修改
		$('#areaId').textbox('disable',true);
	}
	
	// 删除地区信息
	function deleteInfo() {
		var row = grid.datagrid('getSelected');
		
		if (row) {
			Common.confirm('确定要删除当前选择的记录吗？', function(r) {
				if (r) {
					grid.datagrid('load');
					grid.datagrid('clearSelections');
					
					$.ajax({
						timeout	: 15000,
						async	: true,
						cache	: false,
						type	: 'POST',
						url		:  Common.getUrl('areactrl/deleteArea'),
						dataType: 'json',
						data	: {
							'areaId': row.areaID
						},
						success	: function(data) {
							if(data.result == 0){
								grid.datagrid('load');
								grid.datagrid('clearSelections');
								Common.alert(data.message);
							}else {
								Common.alert(data.message);
							}
						}
					});
				}
			});
		} else {
			Common.alert('请先选择要删除的数据！');
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