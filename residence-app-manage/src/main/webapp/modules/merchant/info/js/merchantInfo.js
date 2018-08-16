$(function() {
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('merchantInfoctrl/merchantInfo'),
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
		url		: Common.getUrl('merchantInfoctrl/getToolbar'),
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
	

	var divWindow2 = $('#divWindow2').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 420,
		title : '商家信息详情'
	});
	
	// 商家详情信息
	function showInfo(){
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
		
		divWindow2.window('open');
		detail();
		benefit();
		vipcardUser();
	}
	
	//编辑商家信息（添加和修改）
	var divWindow = $('#divWindow').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 420,
		title : '商家表信息添加'
	});
	var divForm = divWindow.find('form');
	divForm.url = Common.getUrl('merchantInfoctrl/editMerchant');

	// 添加商家信息 start 显示添加用户窗口
	function addInfo() {
		$("#btnSave").removeAttr("disabled");// 启用按钮
		divWindow.window('open');
		divForm.form('clear');
		$('#lecenseImage').attr('src','');
		$('#merchantLogo').attr('src', '');
		$('#photo1').attr('src', '');
		$('#photo2').attr('src', '');
		$('#photo3').attr('src', '');
		$(".trShowOrNot").hide();
		$("#chainMerchant").hide();
		$("#merchantHeadBelong").hide();
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
		$(".trShowOrNot").show();
		$('#chainMerchant').show();
		$('#merchantHeadBelong').show();
		var index = 0;
		if(checkedItems.merchantNature == "直营店") {
			index = 0;
		} else {
			index = 1;
		}
		$(":radio[name='merchantNature']").eq(index).attr('checked','true');
		
		var indexChain = 0;
		if(checkedItems.merchantChain == "总店") {
			indexChain = 0;
		} else {
			indexChain = 1;
		}
		$(":radio[name='chainMerchant']").eq(indexChain).attr('checked','true');
		
		$('#lecenseImage').attr('src', 'data:image/png;base64,'+checkedItems.lecenseImage); 
		$('#merchantLogo').attr('src', 'data:image/png;base64,'+checkedItems.merchantLogo);
		$('#photo1').attr('src', checkedItems.photo1);
		$('#photo2').attr('src', checkedItems.photo2);
		$('#photo3').attr('src', checkedItems.photo3);
		new Viewer(document.getElementById('lecenseViewer'));
		new Viewer(document.getElementById('logoViewer'));
		new Viewer(document.getElementById('photo1Viewer'));
		new Viewer(document.getElementById('photo2Viewer'));
		new Viewer(document.getElementById('photo3Viewer'));
		
	}
	
	$('#industryType').combobox({
		 onChange:function(){
			 var industryType = $(this).combobox('getValue');
			 $('#merchantType').combobox({
            	 url:Common.getUrl('codectrl/relationSelect'),
            	 queryParams:{
             		'codeType' : 'merchantType',
            		'relationType' : industryType
            	 }
			 });
		 }
	}); 
	
	$('#searchInDASBtn').click(function() {
		var merchantName = $('#merchantName').textbox('getValue');
		var areaID = $('#areaID').combobox('getValue');
		var industryType = $('#industryType').textbox('getValue');
		var grid2 = $('#gridDASMerchantList').datagrid({
			striped: true,
			dataType: 'json',
			url: Common.getUrl('merchantInfoctrl/searchMerchantNameInDAS'),
			pagination: true,
			pagePosition: 'bottom',
			pageNumber: 1,
			toolbar: [{}],
			pageSize: 10,
			pageList: Common.pageList,
			multiSort: false,
			remoteSort: false,
			onLoadSuccess: function(data){
				
			}
		});
		
		grid2.datagrid('load', {
			'areaID' : areaID,
			'industryType' : industryType,
			'merchantName' : merchantName
		});
		
		// 设置分页控件显示汉字
		grid2.datagrid('getPager').pagination({ 
			beforePageText: Common.beforePageText, 
			afterPageText: Common.afterPageText, 
			displayMsg: Common.displayMsg
		});
		
		//编辑商家信息（添加和修改）
		var divDASWindow = $('#divDASWindow').window({
			collapsible : false,
			minimizable : false,
			maximizable : false,
			modal : true,
			closed : true,
			width : 350,
			height : 400,
			title : 'DAS系统返回商家信息表列表'
		});
		divDASWindow.window('open');
		$('#divDASWindow').find('.datagrid-toolbar').hide();
		
	});
	
	// 添加商家表信息 end 保存信息及提交数据库处理
	$('#btnSelectM').click(function() {
		var row = $('#gridDASMerchantList').datagrid('getSelected');
		if (row.length > 1) {
			Common.alert('无法选择多条数据的信息！');
			return;
		}
        if (row){
            //$.messager.alert('Info', row.merchantName);
        	$('#merchantName').textbox('setValue', row.merchantName);
        	$('#dasPlaceId').textbox('setValue', row.merchantId);
        }
        $('#divDASWindow').window('close');
        
        $('.trShowOrNot').show();
        //$('#trChainOrNot').css("display","block");
	});
	
	$(":radio[name='merchantNature']").change(function() {
		var val = $(":radio[name='merchantNature']:checked").val();
		
		if (val == 1) { // 连锁店
			$('#chainMerchant').show();
			setChainMerchant();
		} else { // 直营店
			$('#chainMerchant').hide();
			$('#merchantHeadBelong').hide();
		}
		
	});
	
	$(":radio[name='chainMerchant']").change(function() {
		setChainMerchant();
		
	});
	
	function setChainMerchant() {
	var val = $(":radio[name='chainMerchant']:checked").val();
		
		if (val == 1) { // 分店
			$('#merchantHeadBelong').show();
			
			 var areaID = $('#areaID').combobox('getValue');
			 var industryType = $('#industryType').combobox('getValue');
			 $('#merchantHeadName').combobox({
          	 url:Common.getUrl('merchantInfoctrl/merchantHeadList'),
          	 queryParams:{
          		   'areaID' : areaID,
          		   'industryType' : industryType
          	 	}
			 });
			
		} else { // 总店 
			$('#merchantHeadBelong').hide();
		}
	}


	$('#btnSave').click(function() {
		var merchantName = $('#merchantName').textbox('getValue');
		var areaID = $('#areaID').combobox('getValue');
		var industryType = $('#industryType').combobox('getValue');
		var merchantType = $('#merchantType').combobox('getValue');
		var contacts = $('#contacts').textbox('getValue');		
		var telephone = $('#telephone').textbox('getValue');
		var merchantNatureVal = $(":radio[name='merchantNature']:checked").val();
		var chainMerchantVal = $(":radio[name='chainMerchant']:checked").val();
		
		
		if(merchantName==''){
			Common.alert("商家名称不能为空!");
			return;
		}
		if(areaID==''){
			Common.alert("地区不能为空!");
			return;
		}
		if(industryType==''){
			Common.alert("行业类型不能为空!");
			return;
		}
		if(typeof(merchantNatureVal)=="undefined"){ 
			Common.alert("商家性质不能为空!");
			return;
		} else {
			if(merchantNatureVal == 1) {
				if(typeof(chainMerchantVal)=="undefined"){ 
					Common.alert("连锁类型不能为空!");
					return;
				}
			}
		}
		if(merchantType==''){
			Common.alert("商家类型不能为空!");
			return;
		}
		if(contacts==''){
			Common.alert("联系人不能为空!");
			return;
		}
		if(telephone==''){
			Common.alert("电话号码不能为空!");
			return;
		}

		var merchantLogo = $('#merchantLogo2').filebox('getValue');
		var lecenseImage = $('#lecenseImage2').filebox('getValue');
		var photo1 = $('#photo12').filebox('getValue');
		var photo2 = $('#photo22').filebox('getValue');
		var photo3 = $('#photo32').filebox('getValue');
		
		var reg = /\.(jpe?g|gif|png)$/i;
		if(merchantLogo !='' && !reg.test(merchantLogo.toLowerCase())) {
			Common.alert('Logo文件类型只能为jpg、png、gif、jpeg！');
			return;
		}
		if(lecenseImage !='' && !reg.test(lecenseImage.toLowerCase())) {
			Common.alert('营业执照文件类型只能为jpg、png、gif、jpeg！');
			return;
		}
		if(photo1 !='' && !reg.test(photo1.toLowerCase())) {
			Common.alert('照片1文件类型只能为jpg、png、gif、jpeg！');
			return;
		}
		if(photo2 !='' && !reg.test(photo2.toLowerCase())) {
			Common.alert('照片2文件类型只能为jpg、png、gif、jpeg！');
			return;
		}
		if(photo3 !='' && !reg.test(photo3.toLowerCase())) {
			Common.alert('照片3文件类型只能为jpg、png、gif、jpeg！');
			return;
		}
		
		divForm.ajaxSubmit({
			url : divForm.url,
			dataType : "json",
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

	// 删除商家表记录
	function deleteInfo() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.merchantId);
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
						url : Common.getUrl('merchantInfoctrl/delinfo'),
						dataType : 'json',
						data : {
							'merchantId' : ids
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

	//商家详情
	function detail(){
		var checkedItems = grid.datagrid('getSelected');
		$('#merchantName2').text(checkedItems.merchantName);
		$('#areaName').text(checkedItems.areaName);
		$('#industryNote').text(checkedItems.industryNote);
		$('#merchantNote').text(checkedItems.merchantNote);
		$('#vipNoTypeName').text(checkedItems.vipNoType);
		$('#contacts2').text(checkedItems.contacts);
		$('#telephone2').text(checkedItems.telephone);
		$('#address2').text(checkedItems.address);
		$('#description2').text(checkedItems.description);
		$('#lecenseImage3').attr('src', 'data:image/png;base64,'+checkedItems.lecenseImage); 
		$('#merchantLogo3').attr('src', 'data:image/png;base64,'+checkedItems.merchantLogo);
		$('#photo13').attr('src', checkedItems.photo1);
		$('#photo23').attr('src', checkedItems.photo2);
		$('#photo33').attr('src', checkedItems.photo3);
		//$('#imgViewer').viewer();
		new Viewer(document.getElementById('imgViewer'));
	}
	
	//商家优惠信息
	function benefit(){
		var checkedItems = grid.datagrid('getSelected');
		
		var benefitList = $('#benefitList').datagrid({
			striped : true,
			dataType : 'json',
			url : Common.getUrl('benefitctrl/list'),
			method: 'POST',
			queryParams: {
				merchantID : checkedItems.merchantId
			},
			pagination : true,
			pagePosition : 'bottom',
			pageNumber : 1,
			pageSize : Common.pageMiniSize,
			pageList : Common.pageList,
			multiSort : false,
			remoteSort : false,
			onLoadSuccess: function(data){
				//grid.datagrid('clearSelections');
			}
		});
		benefitList.datagrid('getPager').pagination({
			beforePageText : Common.beforePageText,
			afterPageText : Common.afterPageText,
			displayMsg : Common.displayMsg
		});
	}
		
	//商家会员信息
	function vipcardUser(){
		var checkedItems = grid.datagrid('getSelected');
		
		var vipcardList = $('#vipcardList').datagrid({
			striped : true,
			dataType : 'json',
			url : Common.getUrl('merchantInfoctrl/vipcardList'),
			method: 'POST',
			queryParams: {
				merchantID : checkedItems.merchantId
			},
			pagination : true,
			pagePosition : 'bottom',
			pageNumber : 1,
			pageSize : Common.pageMiniSize,
			pageList : Common.pageList,
			multiSort : false,
			remoteSort : false,
			onLoadSuccess: function(data){
				//grid.datagrid('clearSelections');
			}
		});
			vipcardList.datagrid('getPager').pagination({
			beforePageText : Common.beforePageText,
			afterPageText : Common.afterPageText,
			displayMsg : Common.displayMsg
		});
		
		// 查询用户信息
		$('#searchVipcard').click(function() {
			var vipcardCode = $('#vipcardCode').textbox('getValue');
			// 使用参数查询
			vipcardList.datagrid('load', {
				'merchantID' : checkedItems.merchantId,
				'vipcardCode' : vipcardCode
			});
		});
	}
	
	// 查询用户信息
	$('#search').click(function() {
		var merchantName1 = $('#merchantName1').textbox('getText');
		var contacts1 = $('#contacts1').textbox('getText')
		
		// 使用参数查询
		grid.datagrid('load', {
			'merchantName' : merchantName1,
			'contacts' : contacts1
		});
	});
	
	$('#return').click(function() {
		$('#merchantName1').textbox('setValue','');
		$('#contacts1').textbox('setValue','');
		
		
		var merchantName1=$('#merchantName1');
		var contacts1=$('#contacts1');
		
		
		grid.datagrid('load', {
			'merchantName' : merchantName1.val(),
			'contacts' : contacts1.val()
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
	          toolbar = null;
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