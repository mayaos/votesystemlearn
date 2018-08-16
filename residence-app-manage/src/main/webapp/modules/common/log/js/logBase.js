 var toolbarres =[];
$(function(){
	
	$.ajax({
		timeout	: 15000,
		async	: true,
		cache	: false,
		type	: 'POST',
		url		: Common.getUrl('logctrl/getToolbar'),
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
		striped: true,
		dataType: 'json',
		url: Common.getUrl('logctrl/getlist'),
		pagination: true,
		pagePosition: 'bottom',
		pageNumber: 1,
		pageSize: Common.pageSize,
		pageList: Common.pageList,
		toolbar:toolbar,
		multiSort: false,
		remoteSort: false,
		onLoadSuccess: function(data){
			//清空div
			$('#gridList').datagrid("emptyItem",'');
			//循环添加
			for (var i = 0; i < toolbarres.length; i++) {  
				var text = toolbarres[i].text+"";
				var icon = toolbarres[i].icon+"";
				var handler = toolbarres[i].handler+"";
				$('#gridList').datagrid("addToolbarItem",[{"text" : text,"iconCls" : icon,"handler" : handler},"-"]);
			}
		}
		//sortOrder: 'asc',
		//sortName: 'factoryCode'
	});
	
	// 设置分页控件显示汉字
	grid.datagrid('getPager').pagination({ 
		beforePageText: Common.beforePageText, 
		afterPageText: Common.afterPageText, 
		displayMsg: Common.displayMsg
	});
		
	//条件查询
	$('#search').click(function() {
		var userId          = $('#userId').val();
		var isSuccess       = $('#isSuccess').textbox('getValue');
		var logType         = $('#logType').textbox('getValue');
		var happentimeStart = $('#happentimeStart').textbox('getValue');
		var happentimeEnd   = $('#happentimeEnd').textbox('getValue');
		
		var startTime = new Date(happentimeStart.replace("-","/").replace("-","/"));
		var endTime = new Date(happentimeEnd.replace("-","/").replace("-","/"));
		var timeCalc = endTime - startTime;
		
		if (Number(timeCalc) < 0) {
			Common.alert('结束时间必须大于开始时间！');
			return;
		}
		
		// 使用参数查询
		grid.datagrid('load', {
			'strUsername' : userId,
			'strIssuccess' : isSuccess,
			'strLogtype' :logType,
			'strHappentimeStart' : happentimeStart,
			'strHappentimeEnd' : happentimeEnd 
 		});
	});
	
	//重置查询条件
	$('#reset').click(function() {
		$('#userId').textbox('clear');
		$('#logType').textbox('setValue', '');
		$('#isSuccess').textbox('setValue', '');
		$('#happentimeStart').datebox('setValue', '');
		$('#happentimeEnd').datebox('setValue', '');
	});
		
	//删除
	function deleteInfo() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.id);
		});
		var ids = items.join(',');
        
		if (checkedItems.length > 0) {
			Common.confirm('确定要删除当前选择的记录吗？', function(r) {
				if (r) {
					grid.datagrid('load');
					grid.datagrid('clearSelections');
					
					$.ajax({
						timeout	: 15000,
						async	: true,
						cache	: false,
						type	: 'POST',
						url		: Common.getUrl('logctrl/del'),
						dataType: 'json',
						data	: {
							'log_ids': ids
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
	
	//清空日志
	function clearInfo() {
		Common.confirm('确定要清空所有的日志文件吗？', function(r) {
			if (r) {
				$.ajax({
					timeout	: 15000,
					async	: true,
					cache	: false,
					type	: 'POST',
					url		: Common.getUrl('logctrl/delall'),
					dataType: 'json',
					success : function(data) {
						Common.alert(data.message);
						grid.datagrid('reload');
						grid.datagrid('clearSelections');
					},
					error   : function(errdata) {
						Common.alert(data.message);
					}
				});				
			}
		});
	}
	
	function exportExcle() {
		var userId          = $('#userId').val();
		var isSuccess       = $('#isSuccess').textbox('getValue');
		var logType         = $('#logType').textbox('getValue');
		var happentimeStart = $('#happentimeStart').textbox('getValue');
		var happentimeEnd   = $('#happentimeEnd').textbox('getValue');
		
		//默认筛选7天内的数据
		if ((happentimeStart == '') && (happentimeEnd == '')) {
			happentimeStart = getNowFormatDate(-7);
			happentimeEnd = getNowFormatDate(0);
			$('#happentimeStart').textbox('setValue', happentimeStart);
			$('#happentimeEnd').textbox('setValue',happentimeEnd);
			Common.confirm('默认筛选7天内的数据！', function(r) {
				if (r) {
					var param = 'struserId='+userId +'&strIssuccess='+isSuccess + '&strLogType='+logType + '&strHappentimeStart='+happentimeStart + '&strHappentimeEnd='+happentimeEnd
					location.href = Common.getUrl('logctrl/download?'+ param);
				}	
			});
		}else{
			var startTime = new Date(happentimeStart.replace("-","/").replace("-","/"));
			var endTime = new Date(happentimeEnd.replace("-","/").replace("-","/"));
			var timeCalc = endTime - startTime;
			if (Number(timeCalc) < 0) {
				Common.alert('结束时间必须大于开始时间！');
				return;
			}
			if (Number(timeCalc) > 2592001000) {
				Common.alert('起止时间范围必须不能超过30天！');
				return;
			}
			var param = 'struserId='+userId +'&strIssuccess='+isSuccess + '&strLogType='+logType + '&strHappentimeStart='+happentimeStart + '&strHappentimeEnd='+happentimeEnd
			location.href = Common.getUrl('logctrl/download?'+ param);
		}
	}
	
	//初始化弹出框	
	var divWindow = $('#divWindow').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:550,
		height:350,
		title: '查看日志详情'
	});
	var divForm = divWindow.find('form');
	
	$('#btnClose').click(function() {
		divWindow.window('close');
	});
	
	//查询明细
	function showInfo() {		
		divForm.url = 'addHouse.action';
		$("#btnSave").removeAttr("disabled");	//启用按钮
		
		//判断是否选取多条数据
		var rows = grid.datagrid('getSelections');
		if (rows.length > 1) {
			Common.alert('无法查看多条数据的明细信息！');
			return;
		}
		
		var checkedItems = grid.datagrid('getSelected');
		if (checkedItems){
			divWindow.window('open');
			$('#id').html(checkedItems.id);			
			$('#userid').html(checkedItems.username);
			$('#requesturl').html(checkedItems.requesturl);
			$('#ip').html(checkedItems.ip);
			$('#issuccess').html(checkedItems.issuccess);
			$('#happentime').html(checkedItems.happentime);
			$('#exception').html(checkedItems.exception);
			$('#logtype').html(checkedItems.logtype);
			$('#sqlstr').html(checkedItems.sqlstr);	
			$('#logcontent').html(checkedItems.logcontent);
		} else {
			Common.alert('请先选择要查看的数据！');
		}
	}		
					
	function expXls() {
	}
	
	//生成当前时间
	function getNowFormatDate(n) {
		var myDate = new Date();
		myDate.setDate(myDate.getDate() + n);
		var formatDate = "";
		
		formatDate = myDate.getFullYear();
		if (myDate.getMonth() > 9) {
			formatDate = formatDate + '-' + (myDate.getMonth() + 1);
		} else {
			formatDate = formatDate + '-0' + (myDate.getMonth() + 1);	
		}
		
		if (myDate.getDate() > 9) {
			formatDate = formatDate + '-' + myDate.getDate();
		} else {
			formatDate = formatDate + '-0' + myDate.getDate();
		}
		
		if (myDate.getHours() > 9) {
			formatDate = formatDate + ' ' + myDate.getHours();
		} else {
			formatDate = formatDate + ' 0' + myDate.getHours();
		}
		
		if (myDate.getMinutes() > 9) {
			formatDate = formatDate + ':' + myDate.getMinutes();
		} else {
			formatDate = formatDate + ':0' + myDate.getMinutes();
		}
		
		if (myDate.getSeconds() > 9) {
			formatDate = formatDate + ':' + myDate.getSeconds();
		} else {
			formatDate = formatDate + ':0' + myDate.getSeconds();
		}
		
		return formatDate;
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
