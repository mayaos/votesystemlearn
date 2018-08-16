$(function() {
	
	// 初始化logo选择栏属性
	$('#logo1').filebox({
		buttonText : '选择标题图片',
		buttonAlign : 'left'
	});
	$('#logo2').filebox({
		buttonText : '选择标题图片',
		buttonAlign : 'left'
	});
	
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('rightsInfoctrl/queryInfo'),
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
		url		: Common.getUrl('rightsInfoctrl/getToolbar'),
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
	
	//初始化添加数据窗口
	var divWindow = $('#divWindow').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		draggable : false,
		width : 750,
		height : 400,
		title : '权益详情信息添加'
	});
	var divForm = divWindow.find('form');
	
	//初始化修改数据窗口
	var divWindow2 = $('#divWindow2').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		draggable : false,
		width : 750,
		height : 400,
		title : '权益详情信息编辑'
	});
	var divForm2 = divWindow2.find('form');	

	//初始化显示文章内容窗口
	displayWindow = $('#display_content').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		draggable : false,
		width : 750,
		height : 400,
		title : '查看权益内容'
	});

	//-------------------添加权益信息详情------------------------------------------------------
	function addInfo() {
		$("#btnSave1").removeAttr("disabled");// 启用按钮
		divWindow.window('open');
		divForm.form('clear');
		CKEDITOR.instances['add_content'].setData("");
		divForm.url = Common.getUrl('rightsInfoctrl/addRightsInfo');
	}

	$('#btnSave1').click(function() {
		//获取参数
		var rightsType = $('#rightsType1').textbox('getValue');	
		var areaId = $('#areaId1').textbox('getValue');
		var rightsFrom = $('#rightsFrom1').textbox('getValue');
		var rightsTitle = $('#rightsTitle1').textbox('getValue');
		var logo = $('#logo1').textbox('getValue');
		var rightsContent = CKEDITOR.instances['add_content'].getData();
		var issueTime = $('#issueTime1').textbox('getValue');
		
		//判断参数是否符合要求
		if(rightsType==''){
			Common.alert("权益类别不能为空!");
			return;
		}
		if(areaId==''){
			Common.alert("区域不能为空!");
			return;
		}
		if(rightsFrom==''){
			Common.alert("文章来源不能为空!");
			return;
		}
		if(rightsFrom.length>50){
			Common.alert("文章来源不能超过50个字符!");
			return;
		}
		if(rightsTitle==''){
			Common.alert("文章标题不能为空!");
			return;
		}
		if(rightsTitle.length>50){
			Common.alert("文章标题不能超过50个字符!");
			return;
		}
		if(!checkedLogo($('#logo1'))) {
			return;
		}
		if(rightsContent=='') {
			Common.alert("权益文章内容不能为空!");
			return;
		}
		if(issueTime=='') {
			Common.alert("文章发表时间不能为空!");
			return;
		}
		
		divForm.ajaxSubmit({
			type : 'post',
			url : divForm.url,
			dataType : 'json',
			beforeSend : function() {
				$("#btnSave1").removeAttr("disabled");// 启用按钮
			},
			data : {
				'rightsContent' : rightsContent
			},
			success : function(data) {
				if (data.result == 0) {
					$("#btnSave1").attr({
						"disabled" : true
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
	$('#btnCancel1').click(function() {
		divWindow.window('close');
	});
	// --------------------------------------------------------------------------
	
	//----------------------修改权益信息---------------------------------------------
	// 修改权益信息详情表信息
	function editInfo() {
		divForm2.url = Common.getUrl('rightsInfoctrl/updateRightsInfo');
		$("#btnSave2").removeAttr("disabled");// 启用按钮
		divForm2.form('clear');
		var checkedItems = grid.datagrid('getSelected');
		//判断是否选中数据
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
		
		$('#rightsType2').combobox('select',checkedItems.rightsId);
		$('#issueTime2').datetimebox('setValue',checkedItems.issueTime);
		$('#rightsFrom2').textbox('setValue', checkedItems.rightsFrom);
		$('#rightsTitle2').textbox('setValue', checkedItems.rightsTitle);
		$('#articleId2').textbox('setValue', checkedItems.articleId);
		//区域内容回显
		$("#areaId2").textbox('setValue',checkedItems.areaName);
		$("#areaIdHidden").html(checkedItems.areaId);
		//获取文章内容
		$.ajax({
			timeout : 15000,
			async : true,
			cache : false,
			type : 'POST',
			url : Common.getUrl('rightsInfoctrl/querycontent'),
			dataType : 'json',
			data : {
				'articleId' : checkedItems.articleId
			},
			success : function(data) {
				CKEDITOR.instances['edit_content'].setData(data.article.rightsContent);
			},
			error : function(errdata) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});
	}
	
	$('#btnSave2').click(function() {
		//获取参数
		var rightsFrom = $('#rightsFrom2').textbox('getValue');
		var rightsTitle = $('#rightsTitle2').textbox('getValue');
		var rightsContent = CKEDITOR.instances['edit_content'].getData();
		var logo = $('#logo2').textbox('getValue');
		
		//区域内容回显
		//areaIdHidden 得到的是areaId， areaId2得到的可能是areaId，也有可能是areaName
		var areaId;
		if(!isNaN($('#areaId2').combo('getValue'))) {
			//是数字
			areaId = $('#areaId2').combo('getValue');
		} else {
			areaId = $('#areaIdHidden').html();
		}
		
		//判断参数是否符合要求
		if(rightsFrom==''){
			Common.alert("文章来源不能为空!");
			return;
		}
		if(rightsFrom.length>50){
			Common.alert("文章来源不能超过50个字符!");
			return;
		}
		if(rightsTitle==''){
			Common.alert("文章标题不能为空!");
			return;
		}
		if(rightsTitle.length>50){
			Common.alert("文章标题不能超过50个字符!");
			return;
		}
		if(rightsContent=='') {
			Common.alert("权益文章内容不能为空!");
			return;
		}
		if(logo!='') {
			if(!checkedLogo($('#logo2'))) {
				return;
			}
		}
		
		divForm2.ajaxSubmit({
			type : 'post',
			url : divForm2.url,
			dataType : 'json',
			beforeSend : function() {
				$("#btnSave2").removeAttr("disabled");// 启用按钮
			},
			data : {
				'rightsContent' : rightsContent
			},
			success : function(data) {
				if (data.result == 0) {
					$("#btnSave2").attr({
						"disabled" : true
					});
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
	//------------------------------------------------------------------
	

	//---- 删除权益详情记录---------------------------------------------------
	function deleteInfo() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.articleId);
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
						url : Common.getUrl('rightsInfoctrl/delRightsInfo'),
						dataType : 'json',
						data : {
							'articleId' : ids
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
	//--------------------------------------------------------------
	

	//-------条件查询--------------------------------------------------
	$('#search').click(function() {
		
		var titleSearch = $('#titleSearch').textbox('getText');
		var rightsTypeSearch = $('#rightsTypeSearch').textbox('getText');
		var uploadDateStart = $('#uploadDateStart').datebox('getValue');
		var uploadDateEnd = $('#uploadDateEnd').datebox('getValue');
		
		// 使用参数查询
		grid.datagrid('load', {
			'titleSearch' : titleSearch,
			'rightsTypeSearch' : rightsTypeSearch,
			'uploadDateStart' : uploadDateStart,
			'uploadDateEnd' : uploadDateEnd
		});
	});
	
	$('#return').click(function() {
		$('#titleSearch').textbox('setValue','');
		$('#rightsTypeSearch').textbox('setValue','');
		$('#uploadDateStart').textbox('setValue','');
		$('#uploadDateEnd').textbox('setValue','');
		
		var titleSearch = $('#titleSearch').textbox('getText');
		var rightsTypeSearch = $('#rightsTypeSearch').textbox('getText');
		var uploadDateStart = $('#uploadDateStart').datebox('getValue');
		var uploadDateEnd = $('#uploadDateEnd').datebox('getValue');
		
		// 使用参数查询
		grid.datagrid('load', {
			'titleSearch' : titleSearch,
			'rightsTypeSearch' : rightsTypeSearch,
			'uploadDateStart' : uploadDateStart,
			'uploadDateEnd' : uploadDateEnd
		});
	});
	

});

/**
 * 查看详情函数
 * @param value
 * @param row 选中的行
 * @param index
 * @returns {String}
 */
function functionLink(value,row,index){
//	if(value){
		return "<a onclick="+"display('" + row.articleId + "') style='cursor:pointer;color:blue'>" + "查看" + "</a>";
//	}
//	else{
//		return "";
//	}
}
/**
 * 获取文章内容，该函数被functionLink(value,row,index)调用
 */
function display(pkId){
	$.ajax({
		timeout : 15000,
		async : true,
		cache : false,
		type : 'POST',
		url : Common.getUrl('rightsInfoctrl/querycontent'),
		dataType : 'json',
		data : {
			'articleId' : pkId
		},
		success : function(data) {
			CKEDITOR.instances['display_content'].setData(data.article.rightsContent);
			$('#gridList').datagrid('clearSelections');
			displayWindow.window('open');
		},
		error : function(errdata) {
			Common.alert('请求服务器失败，操作未能完成！');
		}
	});
}

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
 * 显示标题图片
 * @param val 需要为http路径，本地盘无法读取
 * @param row
 * @returns {String}
 */
function functionLogo(val,row){
	if (val) {
		return ' <img id="rightsLogo" src=' + val+ ' alt="" width="30" height="30" align="middle"/>';
	} 
}