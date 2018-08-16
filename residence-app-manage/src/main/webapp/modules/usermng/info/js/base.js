/**
 * jquery version 1.0
 * By ecp
 * For user.info index.jsp
 */

$(function(){
	
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('citizenctrl/list'),
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
		url		: Common.getUrl('citizenctrl/getToolbar'),
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
		var userLevel  = $('select#userLevel').textbox('getValue');
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
			'userLevel' : userLevel,
			'strStartTime': startTime,
			'strEndTime'  : endTime 
 		});
	});
	
	//重置查询条件
	$('#reset').click(function() {
		$('input#userName').textbox('setValue', '');
		$('select#userLevel').textbox('clear');
		$('input#startTime').datebox('setValue', '');
		$('input#endTime').datebox('setValue', '');
		
	});
	
	/*$('#userId').textbox({
		 onChange:function(){ 
			 alert(0);
		 }   
	 }); */
	
	//初始化弹出框	
	var showWindow = $('#showWindow').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:650,
		height:400,
		title: '查看用户身份信息详情'
	});
	var showForm = showWindow.find('form');
	showForm.url = Common.getUrl('/citizenctrl/identity');
	
	function showInfo() {
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems) {
			Common.alert('请先选择一条信息操作！');
			return;
		}
		var check = grid.datagrid('getChecked');
		if(check.length > 1){
			Common.alert('请选择一条数据！');
			return;
		}
		
		showWindow.window('open');
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: showForm.url,
			dataType: 'json',
			data	: {
				'citizenID': checkedItems.citizenID
			},
			success	: function(data) {
				var identity = data.row;
				if(identity != null){
					$('#citizenName').text(identity.citizenName);
					$('#idCard').text(identity.idCard);
					$('#rcCard').text(identity.rcCard);
					$('#bankCard').text(identity.bankCard);
					$('#rcCardType').text(identity.rcCardType);
					$('#sex').text(identity.sex);
					$('#birthday').text(identity.birthday);
					$('#nation').text(identity.nation);
					$('#address').text(identity.address);
					$('#issueDate').text(identity.issueDate);
					$('#issueOffice').text(identity.issueOffice);
					$('#validThru').text(identity.validThru);
					$('#rcHead').attr('src', 'data:image/png;base64,'+identity.rcHead);
				}else {
					Common.alert("没有查询的用户身份信息详情！");
				}
			},
			error: function(errdata) {
				Common.alert(data.message);
			}
		});
	}
	
	var vipWindow = $('#vipWindow').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:650,
		height:400,
		title: '会员卡信息列表'
	});
	vipForm = vipWindow.find('form');
	vipForm.url = Common.getUrl("/citizenctrl/vipCard");
	function vipCard() {
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems) {
			Common.alert('请先选择一条信息操作！');
			return;
		}
		var check = grid.datagrid('getChecked');
		if(check.length > 1){
			Common.alert('请选择一条数据！');
			return;
		}
		
		vipWindow.window('open');
		var vipList = $('#vipCardList').datagrid({
			striped : true,
			dataType : 'json',
			url : vipForm.url,
			method: 'POST',
			queryParams: {
				citizenID : checkedItems.citizenID
			},
			pagination : true,
			pagePosition : 'bottom',
			pageNumber : 1,
			pageSize : Common.pageMiniSize,
			pageList : Common.pageList,
			multiSort : false,
			remoteSort : false,
			onLoadSuccess: function(data){
				//grid.datagrid('clearSelections');
			}
		});
		vipList.datagrid('getPager').pagination({
			beforePageText : Common.beforePageText,
			afterPageText : Common.afterPageText,
			displayMsg : Common.displayMsg
		});
	}
	 
	var couponWindow = $('#couponWindow').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:650,
		height:400,
		title: '优惠券信息列表'
	});
	couponForm = couponWindow.find('form');
	couponForm.url = Common.getUrl("/citizenctrl/coupon");
	function coupon() {
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems) {
			Common.alert('请先选择一条信息操作！');
			return;
		}
		var check = grid.datagrid('getChecked');
		if(check.length > 1){
			Common.alert('请选择一条数据！');
			return;
		}
		
		couponWindow.window('open');
		var couponList = $('#couponList').datagrid({
			striped : true,
			dataType : 'json',
			url : couponForm.url,
			method: 'POST',
			queryParams: {
				citizenID : checkedItems.citizenID
			},
			pagination : true,
			pagePosition : 'bottom',
			pageNumber : 1,
			pageSize : Common.pageMiniSize,
			pageList : Common.pageList,
			multiSort : false,
			remoteSort : false,
			onLoadSuccess: function(data){
				//grid.datagrid('clearSelections');
			}
		});
		couponList.datagrid('getPager').pagination({
			beforePageText : Common.beforePageText,
			afterPageText : Common.afterPageText,
			displayMsg : Common.displayMsg
		});
	}
	 
	var newsWindow = $('#newsWindow').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:650,
		height:400,
		title: '用户消息列表'
	});
	newsForm = newsWindow.find('form');
	newsForm.url = Common.getUrl("/citizenctrl/news");
	function news() {
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems) {
			Common.alert('请先选择一条信息操作！');
			return;
		}
		var check = grid.datagrid('getChecked');
		if(check.length > 1){
			Common.alert('请选择一条数据！');
			return;
		}
		
		newsWindow.window('open');
		var newsList = $('#newsList').datagrid({
			striped : true,
			dataType : 'json',
			url : newsForm.url,
			method: 'POST',
			queryParams: {
				citizenID : checkedItems.citizenID
			},
			pagination : true,
			pagePosition : 'bottom',
			pageNumber : 1,
			pageSize : Common.pageMiniSize,
			pageList : Common.pageList,
			multiSort : false,
			remoteSort : false,
			onLoadSuccess: function(data){
				//grid.datagrid('clearSelections');
			}
		});
		newsList.datagrid('getPager').pagination({
			beforePageText : Common.beforePageText,
			afterPageText : Common.afterPageText,
			displayMsg : Common.displayMsg
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


