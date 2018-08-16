/**
 * Smart City jquery version 1.0
 * By xianzehua
 * For opinion's index.jsp
 */

var grid;
$(function(){
	
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('userAuthctrl/list'),
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
		url		: Common.getUrl('userAuthctrl/getToolbar'),
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
	
	//获取rc_card_info表里最新的import date
	$.ajax({
		timeout	: 15000,
		async	: true,
		cache	: false,
		type	: 'POST',
		url		: Common.getUrl('userAuthctrl/maxImportDate'),
		dataType: 'json',
		beforeSend: function() {
		},
		success	: function(data) {
			$("[name='maxImportDate']").html(data.maxImportDate);
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
			'authStatus' : authStatus,
			'strStartTime': startTime,
			'strEndTime'  : endTime 
 		});
	});
	
	//重置查询条件
	$('#reset').click(function() {
		$('input#userName').textbox('setValue', '');
		$('select#authStatus').textbox('clear');
		$('input#startTime').datebox('setValue', '');
		$('input#endTime').datebox('setValue', '');
	});
	
	/*居住证认证*/
	var rcAuthWindow = $('#userAuthWindow').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:650,
		height:210,
		title: '身份证认证'
	});
	var rcAuthForm = rcAuthWindow.find('form');
	rcAuthForm.url = Common.getUrl('/userAuthctrl/batchIdAuth');
	
	function bathIdAuth() {
		rcAuthWindow.window('open');
	}
	
	$(":radio[name='authWay']").change(function(){
		var authWayVal = $(this).val();
		if(authWayVal === "2") {
			$("#authStartTime").datebox('enable');
			$("#authEndTime").datebox('enable');
		} else {
			$("#authStartTime").datebox('disable');
			$("#authEndTime").datebox('disable');
			$("#authStartTime").textbox('setValue','');
			$("#authEndTime").textbox('setValue','');
		}
	});
	
	$('#authEndTime').datetimebox({
	    onSelect: function(date){
	    	var y = date.getFullYear();
	    	var m = date.getMonth()+1;
	    	var d = date.getDate();
	    	var endDate =  y +'-'+ m + '-'+ d;
			var importDate = $("[name='maxImportDate']:first").text();
			
			endDate = new Date(endDate.replace(/\-/gm,"/")).valueOf();
			var importDateN = new Date(importDate.replace(/\-/gm,"/")).valueOf();
			
			if (Number(endDate - importDateN) > 0) {
				var msg = "结束时间不能大于公安局导过来的最新日期 <span style='color:red'>" + importDate + "</span>";
				Common.alert(msg);
				$(".messager-window").css("z-index","9999999");
				importDate = importDate + " 23:59:00"
				$("#authEndTime").textbox('setValue', importDate);
			}
	    }
	});
	
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
			url		: Common.getUrl('/userAuthctrl/batchIdAuth'),
			dataType: 'json',
			data	: {
				'authStartTime': authStartTime,
				'authEndTime': authEndTime
			},
			success	: function(data) {
				Common.alert(data.message);
				grid.datagrid('clearSelections');
				grid.datagrid('reload');
				if(data.result == 0){
					rcAuthWindow.window('close');
				}
			},
			error: function(errdata) {
				Common.alert("请求服务失败！");
			}
		});
	});
	
	//身份证认证
	function idAuth() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.authID);
		});
		var authID = items.join(',');
        
		if (checkedItems.length > 0) {
			Common.confirm('确定要对当前选择的记录做身份证认证吗？', function(r) {
				if (r) {
					grid.datagrid('load');
					grid.datagrid('clearSelections');
					
					$.ajax({
						timeout	: 15000,
						async	: true,
						cache	: false,
						type	: 'POST',
						url		: Common.getUrl('/userAuthctrl/idAuth'),
						dataType: 'json',
						data	: {
							'authID': authID
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
			Common.alert('请先选择要进行身份证认证的数据！');
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


