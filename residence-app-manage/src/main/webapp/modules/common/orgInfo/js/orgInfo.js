
$(function() {
	// 初始化logo选择栏属性
	$('#orgLogo').filebox({
		buttonText : '选择logo',
		buttonAlign : 'left'
	});
	$('#orgLogo2').filebox({
		buttonText : '选择logo',
		buttonAlign : 'left'
	});
	
	var folder;
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('orginfoctrl/queryInfoList'),
		loadMsg:'数据加载中...',
		pagination: true,
		pagePosition: 'bottom',
		pageNumber: 1,
		toolbar: [{}],
		pageSize: Common.pageSize,
		pageList: Common.pageList,
		multiSort: false,
		remoteSort: false,
		onLoadSuccess: function(data){
			folder = data.folder;
		}
	});
	
	//动态获取工具菜单
	$.ajax({
		timeout	: 15000,
		async	: true,
		cache	: false,
		type	: 'POST',
		url		: Common.getUrl('orginfoctrl/getToolbar'),
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
	var divWindow = $('#divWindow').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 500,
		title : '信息添加'
	});
	var divForm = divWindow.find('form');

	var divWindow1 = $('#divWindow1').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 500,
		title : '信息详情'
	});
	var divForm1 = divWindow1.find('form');
	
	var divWindow2 = $('#divWindow2').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 500,
		title : '信息修改'
	});
	var divForm2 = divWindow2.find('form');
	
	//添加机构信息开始-------------------------------------------------------
	function addInfo() {
		$("#btnSave").removeAttr("disabled");// 启用按钮
		divWindow.window('open');
		divForm.form('clear');
		divForm.url = Common.getUrl('orginfoctrl/addOrgInfo');
		$('#passFlag').combobox('select', '1');
	}
	
	$('#btnSave').click(function() {
		
		var orgName = $('#orgName').textbox('getValue');		
		var menuID = $('#menuID').combo('getValue');
		var areaId = $('#areaId').combo('getValue');
		var orgPhone = $('#orgPhone').textbox('getValue');
		var passFlag = $('#passFlag').textbox('getValue');

		if(orgName==''){
			Common.alert("机构名称不能为空!");
			return;
		}
		if(areaId==''){
			Common.alert("区域不能为空!");
			return;
		}
		if(menuID==''){
			Common.alert("菜单不能为空!");
			return;
		}       
		if(!checkedLogo($('input#orgLogo'))) {
			return;
		}
		if(passFlag==''){
			Common.alert("审核不能为空，请重新选择!");
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
	//添加结束-------------------------------------------------------
	
	//修改开始-------------------------------------------------------
	function editInfo() {
		divForm2.form('clear');
		divForm2.url = Common.getUrl('orginfoctrl/updateOrgInfo');
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
		$('#orgId2').textbox('setValue', checkedItems.orgId);
		$("#orgName2").textbox('setValue',checkedItems.orgName);		
		$('#menuID2').combobox('select', checkedItems.menuId);		
		$('#orgDesc2').val(checkedItems.orgDesc);
		$("#orgPhone2").textbox('setValue',checkedItems.orgPhone);
		//区域内容回显
		$("#areaId2").textbox('setValue',checkedItems.areaName);
		$("#areaIdHidden").html(checkedItems.areaId);
		if(checkedItems.passFlag=='通过') {
			$('#passFlag2').combobox('select',0);
		}else if(checkedItems.passFlag=='未通过') {
			$('#passFlag2').combobox('select',2);
		}else {
			$('#passFlag2').combobox('select',1);
		}
			
		$('#noPassReason2').val(checkedItems.noPassReason);
	}

	$('#btnSave2').click(function() {
		
		var orgName = $('#orgName2').textbox('getValue');		
		var menuID = $('#menuID2').combo('getValue');
		var orgPhone = $('#orgPhone2').textbox('getValue');
		var passFlag = $('#passFlag2').textbox('getValue');
		var orgLogo = $('#orgLogo2').textbox('getValue');
		
		//区域内容回显
		//areaIdHidden 得到的是areaId， areaId2得到的可能是areaId，也有可能是areaName
		var areaId;
		if(!isNaN($('#areaId2').combo('getValue'))) {
			//是数字
			areaId = $('#areaId2').combo('getValue');
		} else {
			areaId = $('#areaIdHidden').html();
		}
		
		if(orgName==''){
			Common.alert("机构名称不能为空!");
			return;
		}
		if(menuID==''){
			Common.alert("菜单不能为空!");
			return;
		}
		if(orgLogo!='') {
			if(!checkedLogo($('#orgLogo2'))) {
				return;
			}
		}
		if(passFlag==''){
			Common.alert("审核不能为空，请重新选择!");
			return;
		}
		
		divForm2.ajaxSubmit({
			url : divForm2.url,
			type: 'post', 
			dataType : "json",
			data : {
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
			items.push(item.orgId);
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
						url : Common.getUrl('orginfoctrl/delOrgInfo'),
						dataType : 'json',
						data : {
							'orgId' : ids
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
	
	//查看信息详情开始---------------------------------------------------------
	
	function showInfo() {
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems) {
			Common.alert('请先选择要查看的数据！');
			return;
		}
		var rows = grid.datagrid('getSelections');
		if (rows.length > 1) {
			Common.alert('无法同时选择多条信息操作！');
			return;
		}		
		divWindow1.window('open');
		//赋值
		$('#area1').text(checkedItems.areaName);
		$('#menu1').text(checkedItems.menuName);
		$('#menuLogo1').attr('src', 'data:image/png;base64,'+checkedItems.menuLogo); 
		$('#orgName1').text(checkedItems.orgName);
		$('#orgLogo1').attr('src', 'data:image/png;base64,'+checkedItems.orgLogo); 
		$('#orgName1').text(checkedItems.orgName);
		$('#orgDesc1').text(checkedItems.orgDesc);
		$('#orgPhone1').text(checkedItems.orgPhone);
		$('#passFlag1').text(checkedItems.passFlag);
		$('#noPassReason1').text(checkedItems.noPassReason);
		$('#createTime1').text(checkedItems.createTime);
		$('#updateTime1').text(checkedItems.updateTime);
	}
	//查看信息详情结束---------------------------------------------------------
	
	//条件查询开始------------------------------------------------------------
	$('#search').click(function() {
		var menuNameSearch =  $('#menuNameSearch').textbox('getValue'); 
		var orgNameSearch =  $('#orgNameSearch').textbox('getValue'); 
		
		// 使用参数查询
		grid.datagrid('load', {
			'menuNameSearch' : menuNameSearch,
			'orgNameSearch' : orgNameSearch
		});
	});
	
	$('#return').click(function() {
		$('#menuNameSearch').textbox('setValue', ''); 
		$('#orgNameSearch').textbox('setValue', ''); 
		grid.datagrid('load', {
			'menuNameSearch' : '',
			'orgNameSearch' : ''
		});
	});
	//条件查询结束------------------------------------------------------------
	//自定义联系电话格式--------------------------------------------------------
	$.extend($.fn.validatebox.defaults.rules, {
		phoneRex: {
			validator: function(value){
			var rex=/^1[3-8]+\d{9}$/;
			//var rex=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
			//区号：前面一个0，后面跟2-3位数字 ： 0\d{2,3}
			//电话号码：7-8位数字： \d{7,8
			//分机号：一般都是3位数字： \d{3,}
			 //这样连接起来就是验证电话的正则表达式了：/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/		 
			var rex2=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
			if(rex.test(value)||rex2.test(value))
			{
			  // alert('t'+value);
			  return true;
			}else
			{
			 //alert('false '+value);
			   return false;
			}
			  
			},
			message: '请输入正确电话或手机格式'
		}
	});
});


/**
 * 检查图片格式是否为png格式
 */
function checkedLogo(Logo) {
	// logo文件扩展名解析 xxx.png
	var logoFile = Logo.filebox("getValue");
	// 非空校验
	if (logoFile.length == 0) {
		Common.alert('请选择要上传的logo!');
		return false;
	}
	var logoPointIndex = logoFile.lastIndexOf(".");
	var logoExtenName = logoFile.substring(logoPointIndex + 1, logoFile.length);
	// alert(phoextenName);
	// 扩展名校验
	if (logoExtenName.toLowerCase() != "png") {
		Common.alert("文件格式错误，请选择扩展名为.png的图片文件！");
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