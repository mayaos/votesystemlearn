/**
 * App jquery version 1.0
 * By ecp
 * For new's index.jsp
 */

$(function(){
	
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('newsctrl/list'),
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
		url		: Common.getUrl('newsctrl/getToolbar'),
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
	
	// 条件查询
	$('#search').click(function() {
		var userName   = $('input#userName').textbox('getValue');
		var msgStatus  = $('select#msgStatus').textbox('getValue');
		var startTime  = $('input#startTime').textbox('getValue');
		var endTime    = $('input#endTime').textbox('getValue');
		
		var startTime2 = new Date(startTime.replace(/-/gm,"/"));
		var endTime2 = new Date(endTime.replace(/-/gm,"/"));
		if (Number(endTime2 - startTime2) < 0) {
			Common.alert('结束时间必须大于开始时间！');
			return;
		}
		
		// 使用参数查询
		grid.datagrid('load', {
			'userName' : userName,
			'msgStatus' : msgStatus,
			'strStartTime': startTime,
			'strEndTime'  : endTime 
 		});
	});
	
	//重置查询条件
	$('#reset').click(function() {
		$('input#userName').textbox('setValue', '');
		$('select#msgStatus').textbox('clear');
		$('input#startTime').datebox('setValue', '');
		$('input#endTime').datebox('setValue', '');
		
	});
	
	/*$('#userId').textbox({
		 onChange:function(){ 
			 alert(0);
		 }   
	 }); */
	
	//初始化弹出框	
	var editWindow = $('#divWindow-edit').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:750,
		height:450,
		title: '编辑消息'
	});
	var editForm = editWindow.find('form');
	editForm.url = Common.getUrl('/newsctrl/edit');
	$('#btnClose-edit').click(function() {
		editWindow.window('close');
	});
	
	//添加
	function addInfo() {
		$('#btnOk-edit').linkbutton('enable'); //启用按钮 
		editWindow.window('open');
		editForm.form('clear');
	}
	
	// 修改决策信息
	function editInfo() {
		$('#btnOk-edit').linkbutton('enable'); //启用按钮  
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems){
			Common.alert('请先选择要编辑的数据！');
			return;
		}
		
		var check = grid.datagrid('getChecked');
		if(check.length > 1){
			Common.alert('请选择一条数据！');
			return;
		}
		editWindow.window('open');
		editForm.form('load', checkedItems);  //从页面获取值
	}
	
	$('#btnOk-edit').click(function() {
		var messageID = $('input#messageID').val();
		var messageTitle = $('input#messageTitle');
		var messageContent = $('textarea#messageContent');
		
		if (messageTitle.val() == '') {
			messageTitle.focus();
			Common.alert('请输入消息主题！');
			return;
		}
		
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: editForm.url,
			dataType: 'json',
			data	: {
				'messageID'		: messageID,
				'messageTitle'  : messageTitle.val(),
				'messageContent': messageContent.val()
			},
			beforeSend: function() {
				$("#btnOk-edit").linkbutton("disable"); //禁用按钮  
			},
			success	: function(data) {
				Common.alert(data.message);
				if (data.result) {
					grid.datagrid('load');
					editWindow.window('close');
					grid.datagrid('clearSelections');
				} 
				$("#btnOk-edit").linkbutton("enable");
			},
			error: function(errdata) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});
	});
	 
	//删除
	function deleteInfo() {
		var checkedItems = grid.datagrid('getChecked');
		if (!checkedItems)  {
			Common.alert('请先选择要删除的数据！');
		}
		
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.messageID);
		});
		var ids = items.join(',');
		Common.confirm('确定要删除当前选择的记录吗？', function(r) {
			if (r) {
				grid.datagrid('load');
				grid.datagrid('clearSelections');
				
				$.ajax({
					timeout	: 15000,
					async	: true,
					cache	: false,
					type	: 'POST',
					url		: Common.getUrl('/newsctrl/delete'),
					dataType: 'json',
					data	: {
						'iIds': ids
					},
					success	: function(data) {
						Common.alert(data.message);
						if (data.result) {
							grid.datagrid('reload');
							grid.datagrid('clearSelections');
						} 
					},
					error: function(errdata) {
						Common.alert(data.message);
					}
				});
			}
		});
	}
	
	$.extend($.fn.datagrid.methods, {  
		emptyItem: function(jq, items){  
	          return jq.each(function(){  
	             var toolbar =$(this).parent().prev("div.datagrid-toolbar");//toolbar; 
	             toolbar.empty();
	             toolbar.css('background','#F4F4F4').css('width','100%').css('overflow','hidden');
	          });  
	          toolbar = null;
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


