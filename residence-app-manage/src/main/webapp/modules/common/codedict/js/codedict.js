$(function() {

	/*// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped : true,
		dataType : 'json',
		url : Common.getUrl('codectrl/codelist'),
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
	});*/
	
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('codectrl/codelist'),
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
		url		: Common.getUrl('codectrl/getToolbar'),
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
	
	//扩展datagrid对象方法
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
		title : '业务码表信息添加'
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
		title : '修改业务码表'
	});
	var divForm2 = divWindow2.find('form');

	var divWindow3 = $('#divWindow3').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 600,
		height : 400,
		title : '用户角色信息配置'
	});

	// 添加业务码表信息 start 显示添加用户窗口
	function addInfo() {
		$("#btnSave").removeAttr("disabled");// 启用按钮
		divWindow.window('open');
		divForm.form('clear');
		divForm.url = Common.getUrl('codectrl/addCodedictInfo');
		$('#codeDate').datebox('setValue', formatterDate(new Date()));
	}

	$('#btnSave').click(function() {
		var codeType = $('input#codeType').textbox('getValue');
		var codeTypeName = $('input#codeTypeName').textbox('getValue');
		var codeName = $('input#codeName').textbox('getValue');
		var codeValue = $('input#codeValue').textbox('getValue');
		var codeDate = $('#codeDate').datebox('getValue');
		var codeOrder = $('input#codeOrder').textbox('getValue');
		var codeValid = $('#codeValid').combobox('getValue');
		
		if(codeDate==''){
			Common.alert("修改日期不能为空!");
		}
		if(codeValid==''){
			Common.alert("是否有效标志不能为空!");
		}
		if(codeTypeName==''){
			Common.alert("码值类型名称不能为空!");
		}
		if(codeOrder==''){
			Common.alert("码值排序不能为空!");
		}
		if(codeType==''){
			Common.alert("码值类型不能为空!");
		}
		
		if(codeName==''){
			Common.alert("码值描述不能为空!");
		}
		if(codeValue==''){
			Common.alert("码值不能为空!");
		}
		
		$.ajax({
			timeout : 15000,
			async : true,
			cache : false,
			type : 'POST',
			url : divForm.url,
			dataType : 'json',
			data : {
				'codeType' : codeType,
				'codeTypeName' : codeTypeName,
				'codeName' : codeName,
				'codeValue' : codeValue,
				'codeDate' : codeDate,
				'codeOrder' : codeOrder,
				'codeValid' : codeValid
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
	// 添加业务码表信息 end 保存信息及提交数据库处理

	// 修改业务码表信息
	function editInfo() {
		divForm2.url = Common.getUrl('codectrl/updateCodedictInfo');
		$("#btnSave2").removeAttr("disabled");// 启用按钮
		var checkedItems = grid.datagrid('getChecked');
		if (checkedItems.length < 1) {
			Common.alert('请先选择要编辑的数据！');
			return;
		}
		if (checkedItems.length > 1) {
			Common.alert('只能选择一条数据！');
			return;
		}
		divWindow2.window('open');
		$('#codeType2').textbox('setValue', checkedItems[0].codeType);
		$('#codeTypeName2').textbox('setValue', checkedItems[0].codeTypeName);
		$('#codeName2').textbox('setValue', checkedItems[0].codeName);
		$('#codeValue2').textbox('setValue', checkedItems[0].codeValue); 
		$('#codeDate2').datebox('setValue', formatterDate(new Date()));
		$('#codeOrder2').textbox('setValue', checkedItems[0].codeOrder);
		if(checkedItems[0].codeValid =='VALID') {
			$('#codeValid2 option[value=VALID]').attr('seleted');
		} else {
			$('#codeValid2 option[value=INVALID]').attr('seleted');
		}
		//设置码值类型，码值不允许编辑
		$('#codeType2').textbox('disable',true);
		$('#codeValue2').textbox('disable',true);
		$('#codeDate2').textbox('disable',true);
	}

	$('#btnSave2').click(function() {
		var codeType2 = $('input#codeType2');
		var codeTypeName2 = $('input#codeTypeName2');
		var codeName2 = $('input#codeName2');
		var codeDate2 = $('#codeDate2').datebox('getValue');
		var codeValue2= $('input#codeValue2');
		var codeOrder2 = $('input#codeOrder2');
		var codeValid2 = $('#codeValid2').combobox('getValue');
		
		$.ajax({
			timeout : 15000,
			async : true,
			cache : false,
			type : 'POST',
			url : divForm2.url,
			dataType : 'json',
			data : {
				'codeType' : codeType2.val(),
				'codeTypeName' : codeTypeName2.val(),
				'codeName' : codeName2.val(),
				'codeValue' : codeValue2.val(),
				'codeDate' : codeDate2,
				'codeOrder' : codeOrder2.val(),
				'codeValid' : codeValid2
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

	// 删除业务码表记录
	function deleteInfo() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.codeType);
			items.push(item.codeValue);
			items.push(item.codeName);
		});
//		var codeType = items[0].join(',');
//		var codeValue=items[1].join(',');
//		var codeTypeName=items[2].join(',');

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
						url : Common.getUrl('codectrl/deleteCodedict'),
						dataType : 'json',
						data : {
							'codeType' : items[0],
							'codeValue':items[1],
							'codeName':items[2]
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
		var codeType1 = $('#codeType1').textbox('getText');
		var codeName1 = $('#codeName1').textbox('getText');
		var codeTypeName1=$('#codeTypeName1').textbox('getText');
		
		// 使用参数查询
		grid.datagrid('load', {
			'codeType' : codeType1,
			'codeTypeName' : codeTypeName1,
			'codeName' : codeName1
		});
	});
	
	$('#return').click(function() {
		$('#codeType1').textbox('setValue','');
		$('#codeName1').textbox('setValue','');
		$('#codeTypeName1').textbox('setValue','');
		
		var codeType1=$('#codeType1');
		var codeName1=$('#codeName1');
		var codeTypeName1=$('#codeTypeName1');
		
		grid.datagrid('load', {
			'codeType' : codeType1.val(),
			'codeTypeName' : codeTypeName1.val(),
			'codeName' : codeName1.val()
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

		Common.alertalert(items.join(','));
	});
	
	//得到当前日期
	formatterDate = function(date) {
		var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
		var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
		+ (date.getMonth() + 1);
		return date.getFullYear() + '-' + month + '-' + day;
	};

});