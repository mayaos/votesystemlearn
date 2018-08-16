$(function() {
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('vipctrl/vipInfoList'),
		pagination: true,
		pagePosition: 'bottom',
		pageNumber: 1,
		toolbar: [{}],
		pageSize: Common.pageSize,
		pageList: Common.pageList,
		multiSort: false,
		remoteSort: false,
		onLoadSuccess: function(data){
		}
	});
	
	//动态获取工具菜单
	$.ajax({
		timeout	: 15000,
		async	: true,
		cache	: false,
		type	: 'POST',
		url		: Common.getUrl('vipctrl/getToolbar'),
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
	

	
	//编辑商家信息（添加和修改）
	var divWindow = $('#divWindow').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 420,
		title : '会员卡信息添加'
	});
	var divForm = divWindow.find('form');
	divForm.url = Common.getUrl('vipctrl/editVip');

	// 添加商家信息 start 显示添加用户窗口
	function addInfo() {
		$("#btnSave").removeAttr("disabled");// 启用按钮
		divWindow.window('open');
		divForm.form('clear');
		$('#vipNoType').combobox('setValue', '1');
	}
	
	// 修改商家信息
	function editInfo() {
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems) {
			Common.alert('请先选择要编辑的数据！');
			return;
		}
		var rows = grid.datagrid('getSelections');
		if (rows.length > 1) {
			Common.alert('无法同时选择多条信息操作！');
			return;
		}
		divWindow.window('open');
		divForm.form('clear');
		divForm.form('load', checkedItems);  //从页面获取值
		//为各个输入框赋值
		$('#vipcardId').textbox('setValue', checkedItems.vipcardId);
		$('#merchantName').textbox('setValue', checkedItems.merchantName);
		if(checkedItems.vipNoType == '居住证号'){
			$('#vipNoType').combobox('select','1')
		} 
		if(checkedItems.vipNoType == '身份证号'){
			$('#vipNoType').combobox('select','2')
		} 
		if(checkedItems.vipNoType == '银行卡号'){
			$('#vipNoType').combobox('select','3')
		} 
		if(checkedItems.vipNoType == '手机号'){
			$('#vipNoType').combobox('select','4')
		} 
		//区域内容回显
		$("#areaId").textbox('setValue',checkedItems.areaName);
		$("#areaIdHidden").html(checkedItems.areaId);
		$("#vipDesc").html(checkedItems.vipDesc);
		$("#vipRule").html(checkedItems.vipRule);
	}
		
	$('#btnSave').click(function() {
		var areaId = $('#areaId').combotree('getValue');
		var merchantName = $('#merchantId').combobox('getValue');
		var vipNoType = $('#vipNoType').combobox('getValue');
		var areaIdHidden = $("#areaIdHidden").html();

		if(areaId==''){
			Common.alert("地区不能为空!");
			return;
		}
		if(merchantName==''){
			Common.alert("商家不能为空!");
			return;
		}
		
		if(vipNoType==''){
			Common.alert("会员卡号类型不能为空!");
			return;
		}
		
		divForm.ajaxSubmit({
			url : divForm.url,
			dataType : "json",
			data : {
				'areaIdHidden' : areaIdHidden
			},
			success : function(data) {
				Common.alert(data.message);
				if (data.result == 0) {
					grid.datagrid('load');
					grid.datagrid('clearSelections');
					divWindow.window('close');
				}
			},
			error : function(data) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		})
		
	});
	
	$('#btnCancel').click(function() {
		divWindow.window('close');
	});
	// 添加商家表信息 end 保存信息及提交数据库处理

	// 删除商家表记录
	function deleteInfo() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.vipcardId);
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
						url : Common.getUrl('vipctrl/delinfo'),
						dataType : 'json',
						data : {
							'vipcardId' : ids
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
	
	//条件查询
	// 查询用户信息
	$('#search').click(function() {
		var merchantName = $('#merchantName').textbox('getText');
		var areaName = $('#areaName').textbox('getText')
		
		// 使用参数查询
		grid.datagrid('load', {
			'merchantName' : merchantName,
			'areaName' : areaName
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
	
	//区域树的当字段值改变的时候触发事件
	$('#areaId').combotree({
		onChange:function(){			 
			var areaID = $(this).combotree("getValue"); 
			 $('#merchantId').combobox({
	          	 url:Common.getUrl('merchantInfoctrl/selectMerchantList'),
	          	 queryParams:{
	          		'areaID' : areaID
	          	 }
			 });
		 }
	});
});

 