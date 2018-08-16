
$(function() {
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('objectctrl/objectlist'),
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
		url		: Common.getUrl('objectctrl/getToolbar'),
		dataType: 'json',
		beforeSend: function() {
		},
		success	: function(data) {
			var jsToolbar = data.result; 
			grid.datagrid("emptyItem",'');
			if(jsToolbar == null){
				return;
			}
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
		beforePageText : Common.beforePageText,
		afterPageText : Common.afterPageText,
		displayMsg : Common.displayMsg
	});

	var divWindow2 = $('#divWindow2').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 350,
		title : '修改回馈表'
	});
	var divForm2 = divWindow2.find('form');
 
	// 修改回馈信息表信息
	function reply() {
		divForm2.url = Common.getUrl('objectctrl/editobject');
		$("#btnSave2").removeAttr("disabled");// 启用按钮
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems) {
			Common.alert('请先选择要编辑的数据！');
			return;
		}
		divWindow2.window('open');
		
		//$('#objectionTime2').datebox('setValue', checkedItems.objectionTime);
		$("#objectionContent2").val(checkedItems.objectionContent);
		$("#replyContent2").val(checkedItems.replyContent);
		
		//已经处理过的不允许编辑
		if(checkedItems.objectionStatus=='未处理'){
			$('#replyContent2').prop('disabled', false);
		}else {
			$('#btnSave2').prop('disabled', true);
		}
		
		
		var html  ="";
		if(checkedItems.objectionPic1.length<0){
			$("#jsImgView").html('<li id="jsImgView1" style="padding-left: 10px">用户没有上传图片</li>');
		}else {
			html+='<li id="" style="padding-left: 10px">点击图片查看原图</li>';
			html+='<li  style="float:left; padding-left: 10px"><img  height="200" width="200" src="'+checkedItems.objectionPic1+'" ></li>';
		   if (checkedItems.objectionPic2.length>0) {
			   html+='<li  style="float:left; padding-left: 10px"><img  height="200" width="200" src="'+checkedItems.objectionPic2+'" ></li>';
		     }
		   if (checkedItems.objectionPic3.length>0) {
			   html+='<li  style="float:left; padding-left: 10px"><img  height="200" width="200" src="'+checkedItems.objectionPic3+'" ></li>';
		     }
		   if (checkedItems.objectionPic4.length>0) {
			   html+='<li  style="float:left; padding-left: 10px"><img  height="200" width="200" src="'+checkedItems.objectionPic4+'" ></li>';
		     }
		   $("#jsImgView").html(html);
		   var viewer = new Viewer(document.getElementById('jsImgView')); //初始化图片查看插件
		}
		
	}

	$('#btnSave2').click(function() {
		var objectionId;
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			objectionId = item.objectionId;
		});
		
		var objectionContent2 =$('#objectionContent2');
		var replyContent2 = $('#replyContent2');
		
		$.ajax({
			timeout : 15000,
			async : true,
			cache : false,
			type : 'POST',
			url : divForm2.url,
			dataType : 'json',
			data : {
				
				'objectionContent' : objectionContent2.val(),
				'replyContent' : replyContent2.val(),
				'objectionId':objectionId
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

	//添加信息
	function addInfo(){
		
	}
	// 删除回馈表记录
	function deleteInfo() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.objectionId);
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
						url : Common.getUrl('objectctrl/delobject'),
						dataType : 'json',
						data : {
							'objectionId' : ids
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
		var isDone =  $('#isDone').combobox('getValue'); 
		var beginDate=$('#beginDate').datebox('getText');
		var endDate=$('#endDate').datebox('getText');
		// 验证日期
		if ((beginDate == "")||(endDate == "")) {
			Common.alert('开始日期或结束日期不能为空！');
			return false;
		}
		// 验证日期
		if (beginDate > endDate) {
			Common.alert('开始日期不能大于结束日期！');
			return false;
		}
		
		// 使用参数查询
		grid.datagrid('load', {
			'isDone' : isDone,
			'beginDate' : beginDate,
			'endDate' : endDate
		});
	});
	
	$('#return').click(function() {
		$('#isDone').combobox('setValue', '');
		$('#beginDate').datebox('setValue','');
		$('#endDate').datebox('setValue','');
		grid.datagrid('load', {
			'isDone' : '',
			'beginDate' : '',
			'endDate' : ''
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