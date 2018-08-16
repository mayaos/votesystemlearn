
$(function() {
/*	// 初始化图片选择栏属性
	$('#menuLogo1').filebox({
		buttonText : '选择logo',
		buttonAlign : 'left'
	});
	$('#menuLogo2').filebox({
		buttonText : '选择logo',
		buttonAlign : 'left'
	});
	$('#backgroundImg1').filebox({
		buttonText : '选择背景图片',
		buttonAlign : 'left'
	});
	$('#backgroundImg2').filebox({
		buttonText : '选择背景图片',
		buttonAlign : 'left'
	});*/
	
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('merAppctrl/selectMerAppList'),
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
		}
	});
	
	//动态获取工具菜单
	$.ajax({
		timeout	: 15000,
		async	: true,
		cache	: false,
		type	: 'POST',
		url		: Common.getUrl('merAppctrl/getToolbar'),
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
	var divWindow1 = $('#divWindow1').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 300,
		title : '信息添加'
	});
	var divForm1 = divWindow1.find('form');
	
	var divWindow2 = $('#divWindow2').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 300,
		title : '信息修改'
	});
	var divForm2 = divWindow2.find('form');
	
	//商家信息认证开始-------------------------------------------------------
	//是否通过按钮选择事件---------------
	$("#noPass").click(function(){
		$("#content").show()	   
	});
	$("#pass").click(function(){
		$("#content").hide()		   
	});
	//-----------------------------
	function merAppIdentifiction() {
		$("input[name='ispass'][value='1']").prop('checked', true);
		$("#content").hide()	
		divForm2.form('clear');
		divForm2.url = Common.getUrl('merAppctrl/updateMerApp');
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
		$('#merchantName1').html(checkedItems.merchantName);
		$('#companyName1').html(checkedItems.companyName);
		$('#companyType1').html(checkedItems.companyType);
		$('#division1').html(checkedItems.division);
		$('#mainCategory1').html(checkedItems.mainCategory);
		$('#contacts1').html(checkedItems.contacts);
		$('#position1').html(checkedItems.position);
		$('#phone1').html(checkedItems.phone);
		$('#email1').html(checkedItems.email);
		$('#others1').html(checkedItems.others); 
		$("#licenseImg11").attr('src',checkedItems.licenseImg1); 
		$("#licenseImg21").attr('src',checkedItems.licenseImg2);
		$("#licenseImg31").attr('src',checkedItems.licenseImg3);
		$("#storefrontPhotos11").attr('src',checkedItems.storefrontPhotos1); 
		$("#storefrontPhotos21").attr('src',checkedItems.storefrontPhotos2);
		$("#storefrontPhotos31").attr('src',checkedItems.storefrontPhotos3);
		new Viewer(document.getElementById('imgViewer'));
		new Viewer(document.getElementById('imgViewer2'));
	}

	$('#btnSave2').click(function() {
		var checkedItems = grid.datagrid('getSelected');
		var id = checkedItems.id;
		var step = 	$("input[name='ispass']:checked").val();
		var auditFailedMsg = CKEDITOR.instances['edit_content'].getData();
		var val=$('input:radio[name="ispass"]:checked').val();
		if(val==null){
			Common.alert("请判断是否可以通过审核，如暂时无法判断请点击取消!");
			return;
		} 
		divForm2.ajaxSubmit({
			url : divForm2.url,
			type: 'post', 
			dataType : "json",
			data : {
				'id' : id,
				'step' : step,
				'auditFailedMsg' : auditFailedMsg
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
	//商家信息认证结束-------------------------------------------------------
	
});


