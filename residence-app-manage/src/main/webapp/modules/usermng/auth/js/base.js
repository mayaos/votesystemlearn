/**
 * Smart City jquery version 1.0
 * By ecp
 * For opinion's index.jsp
 */

var grid;
$(function(){
	
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('authctrl/list'),
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
		url		: Common.getUrl('authctrl/getToolbar'),
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
		var authType  = $('select#authType').textbox('getValue');
		var authStatus  = $('select#authStatus').textbox('getValue');
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
			'authType' : authType,
			'authStatus' : authStatus,
			'strStartTime': startTime,
			'strEndTime'  : endTime 
 		});
	});
	
	//重置查询条件
	$('#reset').click(function() {
		$('input#userName').textbox('setValue', '');
		$('select#authType').textbox('clear');
		$('select#authStatus').textbox('clear');
		$('input#startTime').datebox('setValue', '');
		$('input#endTime').datebox('setValue', '');
	});
	
	/*$('#userId').textbox({
		 onChange:function(){ 
			 alert(0);
		 }   
	 }); */
	
	//初始化弹出框	
	var importWindow = $('#importWindow').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:650,
		height:250,
		title: '身份证认证结果导入'
	});
	var importForm = importWindow.find('form');
	importForm.url = Common.getUrl('/authctrl/import');
	$('#btnClose').click(function() {
		importWindow.window('close');
	});
	
	function importExcle(){
		importWindow.window('open');
		$('#errorMsg').hide();
	}
	
	$('#btnImport').click(function() {
		var authFile = $('#authFile').filebox('getValue');
		if(authFile == null || authFile.length<1){
			$('#tips').text('请选择导入文件！');
			return;
		}
		var reg =/([^\s]+(?=\.(xls|xlsx))\.\2)/gi;
		if(authFile !='' && !reg.test(authFile)) {
			$('#tips').text('请选择格式为xls、xlsx的文件上传。');
			return;
		}
		
		
		importForm.ajaxSubmit({
			url : importForm.url,
			dataType : "json",
			success : function(data) {
				$('#tips').text(data.message);
				if(data.result == "-1"){
					var errorList = data.errorList;
					if(errorList != null){
						importWindow.window({height:250});
						importWindow.window('open'); 
						$('#errorMsg').show();
						$('#errorList').datagrid('loadData', errorList);
					}
				}
			},
			error : function(data) {
				$('#tips').text('请求服务器失败，操作未能完成！');
			}
		})
	});	
	
	//初始化弹出框	
	var exportWindow = $('#exportWindow').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:650,
		height:300,
		title: '数据导出条件'
	});
	var exportForm = exportWindow.find('form');
	exportForm.url = Common.getUrl('/authctrl/export');
	$('#btnClose').click(function() {
		exportWindow.window('close');
	});
	
	function exportExcle(){
		var userName = $('input#userName').textbox('getValue');
		var authType = $('select#authType').textbox('getText');
		var authStatus = $('select#authStatus').textbox('getText');
		var startTime = $('input#startTime').textbox('getValue');
		var endTime = $('input#endTime').textbox('getValue');
		var options = grid.datagrid('getPager').data("pagination").options;
		$('#userName2').text(userName);
		$('#authType2').text(authType);
		$('#authStatus2').text(authStatus);
		$('#authTime2').text(startTime + ' - ' + endTime);
		$('#count').text(options.total +'条');
		exportWindow.window('open');
	}
	 
	$('#btnExport').click(function() {
		var userName = $('input#userName').textbox('getValue');
		var authType = $('select#authType').textbox('getValue');
		var authStatus = $('select#authStatus').textbox('getValue');
		var startTime = $('input#startTime').textbox('getValue');
		var endTime = $('input#endTime').textbox('getValue');
		var exportType = $('input[name=exportType]:checked').val();
		if(exportType ==1){
			if(authStatus !='1'){
				Common.alert('请在搜索栏中选择"认证状态"为‘未审核’后点击查询！');
				return;
			}
		}
		var parmas = 'exportType='+ exportType;
		parmas = parmas + '&userName='+ userName;
		parmas = parmas + '&authType='+authType;
		parmas = parmas + '&authStatus='+authStatus;
		parmas = parmas + '&startTime='+startTime;
		parmas = parmas + '&endTime='+endTime;
		$('#exportForm').attr('action', exportForm.url + '?' + parmas);
		$('#exportForm').submit();
	});
	
	/*居住证认证*/
	var rcAuthWindow = $('#rcAuthWindow').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:650,
		height:150,
		title: '居住证认证'
	});
	var rcAuthForm = rcAuthWindow.find('form');
	rcAuthForm.url = Common.getUrl('/authctrl/rcAuth');
	
	function rcAuth() {
		rcAuthWindow.window('open');
	}
	
	$('#btnRcAuth').click(function() {
		var authStartTime = $('input#authStartTime').textbox('getValue');
		var authEndTime = $('input#authEndTime').textbox('getValue');
		
		var startTime2 = new Date(authStartTime.replace(/-/gm,"/"));
		var endTime2 = new Date(authEndTime.replace(/-/gm,"/"));
		if (Number(endTime2 - startTime2) < 0) {
			Common.alert('结束时间必须大于开始时间！');
			return;
		}
		
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: Common.getUrl('/authctrl/rcAuth'),
			dataType: 'json',
			data	: {
				'authStartTime': authStartTime,
				'authEndTime': authEndTime
			},
			success	: function(data) {
				Common.alert(data.message);
				grid.datagrid('reload');
				grid.datagrid('clearSelections');
				if(data.result == 0){
					rcAuthWindow.window('close');
				}
			},
			error: function(errdata) {
				Common.alert("请求服务失败！");
			}
		});
	});
	
	//删除
	function deleteInfo() {
		var checkedItems = grid.datagrid('getSelected');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.id);
		});
		var ids = items.join(',');
        
		if (checkedItems) {
			Common.confirm('确定要删除当前选择的记录吗？', function(r) {
				if (r) {
					grid.datagrid('load');
					grid.datagrid('clearSelections');
					
					$.ajax({
						timeout	: 15000,
						async	: true,
						cache	: false,
						type	: 'POST',
						url		: Common.getUrl('/authctrl/delete'),
						dataType: 'json',
						data	: {
							'iIds': ids
						},
						success	: function(data) {
							if (data.result == 0) {
								Common.alert(data.message);
								grid.datagrid('reload');
								grid.datagrid('clearSelections');
							} else {
								Common.alert(data.message);
							}
						},
						error: function(errdata) {
							Common.alert(data.message);
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


