$(function() {
	// 初始化图片选择栏属性
	$('#menuLogo1').filebox({
		buttonText : '点击选择',
		buttonAlign : 'right'
	});
	$('#menuLogo2').filebox({
		buttonText : '点击选择',
		buttonAlign : 'right'
	});
	$('#backgroundImg1').filebox({
		buttonText : '点击选择',
		buttonAlign : 'right'
	});
	$('#backgroundImg2').filebox({
		buttonText : '点击选择',
		buttonAlign : 'right'
	});
	
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('orgMenuctrl/selectMenuList'),
		loadMsg:'数据加载中...',
		pagination: true,
		pagePosition: 'bottom',
		pageNumber: 1,
		toolbar: [{}],
		pageSize: Common.pageSize,
		pageList: Common.pageList,
		multiSort: false,
		remoteSort: false,
		onLoadSuccess: function(data){}
	});
	
	//动态获取工具菜单
	$.ajax({
		timeout	: 15000,
		async	: true,
		cache	: false,
		type	: 'POST',
		url		: Common.getUrl('orgMenuctrl/getToolbar'),
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

	
	//初始化各窗口
	//添加窗口
	var divWindow1 = $('#divWindow1').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 410,
		title : '信息添加'
	});
	var divForm1 = divWindow1.find('form');
	
	//修改窗口
	var divWindow2 = $('#divWindow2').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 410,
		title : '信息修改'
	});
	var divForm2 = divWindow2.find('form');
	
	//添加机构信息开始-------------------------------------------------------
	function addInfo() {
		$("#btnSave1").removeAttr("disabled");// 启用按钮
		divWindow1.window('open');
		divForm1.form('clear');
		divForm1.url = Common.getUrl('orgMenuctrl/addMenu');
		$('#menuStatus1').combobox('select', '1');
		$('#isDefault1').combobox('select', '0');
	}
	
	$('#btnSave1').click(function() {
		var menuName = $('#menuName1').textbox('getValue');		
		var menuType = $('#menuType1').combo('getValue');
		var areaId = $('#areaId').combotree('getValues');
		var menuUrl = $('#menuUrl1').textbox('getValue');
		var menuOrder = $('#menuOrder1').textbox('getValue');
		var menuStatus = $('#menuStatus1').combo('getValue');
		var isDefault = $('#isDefault1').combo('getValue');
		
		if(menuName==''){
			Common.alert("菜单名称不能为空!");
			return;
		}
		if(menuType==''){
			Common.alert("菜单类型不能为空!");
			return;
		}
		if(areaId==''){
			Common.alert("区域名称不能为空!");
			return;
		}		
		if(!checkedImgFormat($('input#menuLogo1'),'logo')) {
			return;
		}
		if(!checkedImgFormat($('input#backgroundImg1'),'backImg')) {
			return;
		}
		if(menuUrl==''){
			Common.alert("链接不能为空！");
			return;
		}
		if(menuOrder==''){
			Common.alert("菜单排序不能为空！");
			return;
		}
		if(menuStatus==''){
			Common.alert("请选择该菜单是否有效！");
			return;
		}
		if(isDefault==''){
			Common.alert("请选择该菜单是否为默认首页菜单！");
			return;
		}
		
		divForm1.ajaxSubmit({
			url : divForm1.url,
			type: 'post', 
			dataType : "json",
			success : function(data) {
				if (data.result == 0) {
					grid.datagrid('load');
					divWindow1.window('close');
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
	$('#btnCancel1').click(function() {
		divWindow1.window('close');
	});
	//添加结束-------------------------------------------------------
	
	//修改开始-------------------------------------------------------
	function editInfo() {
		divForm2.form('clear');
		divForm2.url = Common.getUrl('orgMenuctrl/updateMenu');
		$("#btnSave2").removeAttr("disabled");// 启用按钮
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems) {
			Common.alert('请先选择要编辑的记录！');
			return;
		}
		//判断是否选取多条数据
		var rows = grid.datagrid('getSelections');
		if (rows.length > 1) {
			Common.alert('无法编辑多条记录！');
			return;
		}
		divWindow2.window('open');
		$('#menuId2').textbox('setValue', checkedItems.menuId);	
		$('#menuName2').textbox('setValue', checkedItems.menuName);
		$('#areaId2').textbox('setValue', checkedItems.areaName);
		$('#areaIdHidden').html(checkedItems.areaId);
		$('#menuType2').combobox('select', checkedItems.menuType);	
		$('#menuUrl2').textbox('setValue', checkedItems.menuUrl);	
		$('#menuOrder2').textbox('setValue', checkedItems.menuOrder);	
		if(checkedItems.menuStatus == '否') {
			$('#menuStatus2').combobox('select', 2);	
		} else if(checkedItems.menuStatus == '是') {
			$('#menuStatus2').combobox('select', 1);	
		}
		if(checkedItems.isDefault == '否') {
			$('#isDefault2').combobox('select', 0);	
		} else if(checkedItems.isDefault == '是') {
			$('#isDefault2').combobox('select', 1);	
		}
	}

	$('#btnSave2').click(function() {
		var menuName = $('#menuName2').textbox('getValue');		
		var menuType = $('#menuType2').combo('getValue');
		var menuLogo = $('input#menuLogo2').textbox('getValue');
		
		//areaIdHidden 得到的是areaId， areaId2得到的可能是areaId，也有可能是areaName
		var areaIds;
		
		if(!isNaN($('#areaId2').combo('getValue'))) {
			//是数字
			areaIds = $('#areaId2').combotree('getValues');
//			areaId = $('#areaId2').combo('getValue');
//			alert( $('#areaId2').combotree('getValues'))
		} else {
			areaIds = $('#areaIdHidden').html();
		}	
		var backgroundImg = $('input#backgroundImg2').textbox('getValue');
		var menuUrl = $('#menuUrl2').textbox('getValue');
		var menuOrder = $('#menuOrder2').textbox('getValue');
		var menuStatus = $('#menuStatus2').combo('getValue');
		var isDefault = $('#isDefault2').combo('getValue');
			
		if(menuName==''){
			Common.alert("菜单名称不能为空!");
			return;
		}
		if(menuType==''){
			Common.alert("菜单类型不能为空!");
			return;
		}	
		if(menuLogo!='') {
			if(!checkedImgFormat($('input#menuLogo2'),'png')) {
				return;
			}	
		}	
		if(backgroundImg!='') {
			if(!checkedImgFormat($('input#backgroundImg2'),'jpg')) {
				return;
			}
		}
		if(menuUrl==''){
			Common.alert("链接不能为空！");
			return;
		}
		if(menuOrder==''){
			Common.alert("菜单排序不能为空！");
			return;
		}
		if(menuStatus==''){
			Common.alert("请选择该菜单是否有效！");
			return;
		}
		if(isDefault==''){
			Common.alert("请选择该菜单是否为默认首页菜单！");
			return;
		}
		if(areaIds[0].length>=6){
			var areaId=areaIds.join(",");			
		}

		divForm2.ajaxSubmit({
			url : divForm2.url,
			type: 'post', 
			dataType : "json",
			data : {
				'areaIdHidden' : areaId
			},			
			queryParams : {
				'areaIdHidden' : areaId
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
			error : function(data) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});

	});
	$('#btnCancel2').click(function() {
		divWindow2.window('close');
	});
	//修改结束-------------------------------------------------------
	
	//删除信息开始----------------------------------------------
	function deleteInfo() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.menuId);
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
						url : Common.getUrl('orgMenuctrl/delmenu'),
						dataType : 'json',
						data : {
							'menuId' : ids
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
	//删除信息结束------------------------------------------------------------
	
	//条件查询开始------------------------------------------------------------
	$('#search').click(function() {
		var menuTypeNameSearch =  $('#menuTypeNameSearch').textbox('getValue'); 
		var menuNameSearch =  $('#menuNameSearch').textbox('getValue'); 
		
		// 使用参数查询
		grid.datagrid('load', {
			'menuTypeNameSearch' : menuTypeNameSearch,
			'menuNameSearch' : menuNameSearch
		});
	});
	
	$('#return').click(function() {
		$('#menuTypeNameSearch').textbox('setValue', ''); 
		$('#menuNameSearch').textbox('setValue', ''); 
		grid.datagrid('load', {
			'menuTypeNameSearch' : '',
			'menuNameSearch' : ''
		});
	});
	//条件查询结束------------------------------------------------------------
});


/**
 * 检查图片格式
 */
function checkedImgFormat(Logo,type) {
	// logo文件扩展名解析 xxx.png
	var logoFile = Logo.filebox("getValue");
	// 非空校验
	if (logoFile.length == 0) {
		if(type=='logo') {
			Common.alert('请选择要上传的LOGO!');
		} else{
			Common.alert('请选择要上传的背景图片!');
		}	
		return false;
	}
	var logoPointIndex = logoFile.lastIndexOf(".");
	var logoExtenName = logoFile.substring(logoPointIndex + 1, logoFile.length);
	// alert(phoextenName);
	// 扩展名校验
	if (logoExtenName.toLowerCase() != "jpg" && logoExtenName.toLowerCase() != "png" && 
			logoExtenName.toLowerCase() != "bmp" && logoExtenName.toLowerCase() != "gif") {
		if(type=='logo') {
			Common.alert("LOGO文件格式错误，请选择图片类型文件上传！");
		} else {
			Common.alert("背景图片文件格式错误，请选择图片类型文件上传！");
		}
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
    	return   '<img id="orgLogo" src="data:image/png;base64, '  +  val  + ' " alt="" width="30" height="30" align="middle"/>';
	} 
}
/**
 * 将图片链接显示为图片
 * @param val
 * @param row
 * @returns {String}
 */
function formatImg(val,row) {
    if(val) {
    	return ' <img id="backgroundImg" src=' + val+ ' alt="" width="80" height="30" align="middle"/>';
	} 
}