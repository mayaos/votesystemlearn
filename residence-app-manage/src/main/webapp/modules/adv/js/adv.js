$(function() {

	var divForm = $('#addForm');
	
	var divWindow = $('#divWindow').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 550,
		height : 320,
		title : '广告信息添加'
	});
	
	var divForm1 = $('#addForm1');
	
	var divWindow1 = $('#divWindow1').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 500,
		height : 410,
		title : '广告信息修改'
	});
	
	var divWindow2 = $('#divWindow2').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 680,
		height : 510,
		title : '广告信息详情'
	});
	
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('advctrl/querylist'),
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
		url		: Common.getUrl('advctrl/getToolbar'),
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

	// 查询场所信息
	$('#search').click(function() {
		// 获取搜索条件
		var areaNameSearch = $('#areaNameSearch').textbox('getValue');		
		// 使用参数查询
		grid.datagrid('load', {
			'areaNameSearch' : areaNameSearch
		});
	});
	
	// 重置查询条件
	$('#reset').click(function() {
		$('#areaNameSearch').textbox('clear');		
		grid.datagrid('load', {
			'areaNameSearch' : "",
		});
	});
	
	// 添加广告信息 start 显示添加广告窗口
	function addInfo() {
		$("#btnSave").removeAttr("disabled");	// 启用按钮
		$('#divWindow').window('open');
		divForm.form('clear');
		divForm.url = Common.getUrl('advctrl/addadvinfo');
		$('#advValid').combobox('select', '1');
	}

	// 新建广告提交 
	$('#btnSave').click(function() {
		// 验证表单信息是否满足规则
		if (!divForm.form('validate')) {
			Common.alert("请根据提示完善表单信息！");
			return false;
		}
		
		divForm.ajaxSubmit({
			url      : divForm.url,
			dataType : 'json',	
			success  : function(data) {
				if (data.result == 0) {
					$("#btnSave").attr({"disabled":"disabled"});
					grid.datagrid('load');
					divWindow.window('close');
					grid.datagrid('clearSelections');
					Common.alert(data.message);
				}else {
					Common.alert(data.message);
				}},
			error : function(data) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});

	});
	
	$('#btnCancel').click(function() {
		divWindow.window('close');
	});
	
	// 添加广告信息 end 保存信息及提交数据库处理
	
	//修改开始-------------------------------------------------------
	function editInfo() {
		var checkedItems = grid.datagrid('getChecked');
		if (!checkedItems) {
			Common.alert('请先选择要编辑的数据！');
			return;
		}
		if (checkedItems.length > 1) {
			Common.alert('无法同时修改多条数据！');
			return;
		}
		$('#advId1').textbox('setValue', checkedItems[0].id);
		$('#areaId1').combotree('setValues', checkedItems[0].areaIdList.split(','));
		$("#btnSave1").removeAttr("disabled");	// 启用按钮
		$('#advDesc1').text(checkedItems[0].advDesc); 
		$('#redirectUrl1').textbox('setValue', checkedItems[0].redirectUrl);
		if(checkedItems[0].valid == '是') {
			$('#advValid1').combobox('select', '1');
		} else {
			$('#advValid1').combobox('select', '0');
		}
		$('#advImg1').attr('src', checkedItems[0].advImgUrl); 		
		divForm1.url = Common.getUrl('advctrl/editadvinfo');
		$('#divWindow1').window('open');		
	}
	
	// 修改广告提交 
	$('#btnSave1').click(function() {
		// 验证表单信息是否满足规则
		if (!divForm1.form('validate')) {
			Common.alert("请根据提示完善表单信息！");
			return false;
		}
		
		divForm1.ajaxSubmit({
			url      : divForm1.url,
			dataType : 'json',	
			success  : function(data) {
				if (data.result == 0) {
					$("#btnSave").attr({"disabled":"disabled"});
					grid.datagrid('load');
					divWindow1.window('close');
					grid.datagrid('clearSelections');
					Common.alert(data.message);
				}else {
					Common.alert(data.message);
				}},
			error : function(data) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});

	});
	
	$('#btnCancel1').click(function() {
		divWindow1.window('close');
	});
	
	
	//删除-----------------------------------------------------------
	function deleteInfo() {
		var checkedItems = grid.datagrid('getChecked');
		var idItems = new Array();
		var urlItems = new Array();
		$.each(checkedItems, function(index, item) {
			idItems.push(item.id);
			urlItems.push(item.advImgUrl);
		});
		var ids = idItems.join(',');
		var urls = urlItems.join(',');
		if (checkedItems.length > 0) {
			Common.confirm('确定要删除当前选择的数据吗？', function(r) {
				if (r) {
					grid.datagrid('load');
					grid.datagrid('clearSelections');

					$.ajax({
						timeout : 15000,
						async : true,
						cache : false,
						type : 'POST',
						url : Common.getUrl('advctrl/deladvinfo'),
						dataType : 'json',
						data : {
							'advImgIds' : ids,
							'urls' : urls
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
	
	//广告信息详情
	function showInfo(){
		var checkedItems = grid.datagrid('getChecked');
		if(checkedItems.length == 0 || checkedItems.length > 1) {
			Common.alert("请选择一条记录查看详情");
			return;
		}
		$('#areaId2').text(checkedItems[0].areaNameList);
		$('#advImg2').attr('src', checkedItems[0].advImgUrl); 		
		$('#advDesc2').text(checkedItems[0].advDesc);
		$('#redirectUrl2').text(checkedItems[0].redirectUrl);
		$('#advValid2').text(checkedItems[0].valid);
		$('#creator2').text(checkedItems[0].creator);
		$('#createTime2').text(checkedItems[0].createTime);
		$('#advDesc2').attr('disabled', true);
		$('#areaId2').attr('disabled', true);
		$('#divWindow2').window('open');
	}
});

/**
 * 将图片链接显示为图片
 * @param val
 * @param row
 * @returns {String}
 */
function formatImg(val,row) {
    if(val) {
    	return ' <img id="backgroundImg" src=' + val+ ' alt="" width="30" height="30" align="middle"/>';
	} 
}