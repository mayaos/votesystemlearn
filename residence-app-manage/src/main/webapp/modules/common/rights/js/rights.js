$(function() {
	// 初始化logo选择栏属性
	$('#rightsLogo').filebox({
		buttonText : '选择logo',
		buttonAlign : 'left'
	});
	$('#rightsLogo2').filebox({
		buttonText : '选择logo',
		buttonAlign : 'left'
	});
	
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('rithtsMenuctrl/querylist'),
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
		url		: Common.getUrl('rithtsMenuctrl/getToolbar'),
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
		height : 200,
		title : '权益表信息添加'
	});
	var divForm = divWindow.find('form');

	var divWindow2 = $('#divWindow2').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 450,
		height : 200,
		title : '修改权益表'
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

	// 添加权益信息表信息 start显示添加用户窗口
	function addInfo() {
		$("#btnSave").removeAttr("disabled");// 启用按钮
		divWindow.window('open');
		divForm.form('clear');
		divForm.url = Common.getUrl('rithtsMenuctrl/addMenu');
		$('#rightsStatus').combobox('select', '1');
	}

	$('#btnSave').click(function() {
		var rightsName = $('input#rightsName').textbox('getValue');
		
		var rightsOrder = $('input#rightsOrder').textbox('getValue');
	
		var rightsStatus = $('select#rightsStatus').combo('getValue');
		var rightsLogo = $('input#rightsLogo').textbox('getValue');

		if(rightsName==''){
			Common.alert("权益名称不能为空!");
			return;
		}
		var reg = /^[0-1]{1}$/;
		if(!reg.test(rightsStatus)){
			Common.alert("请选择权益状态!");
			return;
		}
		reg = /^\d+$/;
		if(!reg.test(rightsOrder)) {
			Common.alert("请正确输入权益排序!");
			return;
		}

		if(!checkedLogo($('input#rightsLogo'))) {
			return;
		}
		
		divForm.ajaxSubmit({
			url : divForm.url,
			type: 'post', 
			dataType : "json",
			success : function(data) {
				if (data.result == 0) {
					grid.datagrid('load');
					divWindow.window('close');
					grid.datagrid('clearSelections');
					Common.alert(data.message);
				} else {
					Common.alert(data.message);
				}
			},
			error : function(data) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});

	});
	$('#btnCancel').click(function() {
		divWindow.window('close');
	});
	// 添加权益信息表信息 end 保存信息及提交数据库处理

	// 修改权益信息表信息
	function editInfo() {
		divForm2.form('clear');
		divForm2.url = Common.getUrl('rithtsMenuctrl/updateMenu');
		$("#btnSave2").removeAttr("disabled");// 启用按钮
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems) {
			Common.alert('请先选择要编辑的数据！');
			return;
		}
		//判断是否选取多条数据
		var rows = grid.datagrid('getSelections');
		if (rows.length > 1) {
			Common.alert('无法编辑多条数据的信息！');
			return;
		}
		divWindow2.window('open');
		$('#rightsName2').textbox('setValue', checkedItems.rightsName);
		$("#rightsOrder2").textbox('setValue',checkedItems.rightsOrder);
		if(checkedItems.rightsStatus == '启用') {
			$('#rightsStatus2').combobox('select','1');
		} else {
			$('#rightsStatus2').combobox('select','0');
		}			
		$('#rightsId2').textbox('setValue',checkedItems.rightsId);
		
	}

	$('#btnSave2').click(function() {
		var rightsId;
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			rightsId = item.rightsId;
		});
		
		var rightsName2 = $('input#rightsName2').textbox('getValue');
		var rightsOrder2 =$('input#rightsOrder2').textbox('getValue');
		var rightsStatus2 = $('#rightsStatus2').combobox('getValue');
		var rightsId2 = $('#rightsId2').textbox('getValue');
		var rightsLogo2 = $('input#rightsLogo2').textbox('getValue');
		
		if(rightsId2==''){
			Common.alert("权益Id不能为空,请重新选择数据");
			return;
		}
		if(rightsName2==''){
			Common.alert("权益名称不能为空!");
			return;
		}
		var reg = /^[0-1]{1}$/;
		if(!reg.test(rightsStatus2)){
			Common.alert("请选择权益状态!");
			return;
		}
		reg = /^\d+$/;
		if(!reg.test(rightsOrder2)) {
			Common.alert("请正确输入权益排序!");
			return;
		}		
		if(rightsLogo2 != '') {
			if(!checkedLogo($('#rightsLogo2'))) {
				return;
			}
		}
		
		divForm2.ajaxSubmit({
			url : divForm2.url,
			type: 'post', 
			dataType : "json",
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
			error : function(data) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});

	});
	$('#btnCancel2').click(function() {
		divWindow2.window('close');
	});

	// 删除权益表记录
	function deleteInfo() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.rightsId);
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
						url : Common.getUrl('rithtsMenuctrl/delMenu'),
						dataType : 'json',
						data : {
							'rightsId' : ids
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
							Common.alert(errdata.message);
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
		var rightsName1 = $('#rightsName1').textbox('getText');
		
		// 使用参数查询
		grid.datagrid('load', {
			'rightsName' : rightsName1
		});
	});
	
	$('#return').click(function() {
		$('#rightsName1').textbox('setValue','');
		
		
		var rightsName1=$('#rightsName1');
		
		
		grid.datagrid('load', {
			'rightsName' : rightsName1.val()
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
	
});

/**
 * 检查图片格式
 */
function checkedLogo(rightsLogo) {
	// logo文件扩展名解析 xxx.png
	var logoFile = rightsLogo.filebox("getValue");
	// 非空校验
	if (logoFile.length == 0) {
		Common.alert('请选择要上传的logo!');
		return false;
	}
	var logoPointIndex = logoFile.lastIndexOf(".");
	var logoExtenName = logoFile.substring(logoPointIndex + 1, logoFile.length);
	// alert(phoextenName);
	// 扩展名校验
	if (logoExtenName.toLowerCase() != "jpg" && logoExtenName.toLowerCase() != "png" && 
			logoExtenName.toLowerCase() != "bmp" && logoExtenName.toLowerCase() != "gif") {
		Common.alert("文件格式错误，请选择图片格式文件！");
		return false;
	}
	return true;
}

/**
 * 将base64图片数据，转为图片显示
 * @param val
 * @param row
 * @returns
 */
function formatLogo(val,row) {
    if(val) {
    	return   '<img id="rightsLogo" src="data:image/png;base64, '  +  val  + ' " alt="" width="30" height="30" align="middle"/>';
	} 
}