$(function() {
	
	var folder;
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('benefitctrl/list'),
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
		url		: Common.getUrl('benefitctrl/getToolbar'),
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
	
	// 查询用户信息
	$('#search').click(function(){
		search();
	});	
	$('#return').click(function() {
		$('#benefitName1').textbox('setValue','');
		$('#merchantName1').textbox('setValue','');
		$('#startTime').textbox('setValue','');
		$('#endTime').textbox('setValue','');
		search();
	});
	function search() {
		var benefitName1 = $('#benefitName1').textbox('getValue');
		var merchantName1 = $('#merchantName1').textbox('getValue');
		var startTime  = $('input#startTime').textbox('getValue');
		var endTime    = $('input#endTime').textbox('getValue');
		
		var startTime2 = new Date(startTime.replace(/-/gm,"/"));
		var endTime2 = new Date(endTime.replace(/-/gm,"/"));
		if (Number(endTime2 - startTime2) < 0) {
			Common.alert('结束时间必须大于开始时间！');
			return;
		}
		// 使用参数查询
		grid.datagrid('load', {
			'benefitName' : benefitName1,
			'merchantName' : merchantName1,
			'startTime' : startTime,
			'endTime' : endTime
		});
	}
	
	var divWindow = $('#divWindow').window({
		collapsible : false,
		minimizable : false,
		maximizable : false,
		modal : true,
		closed : true,
		width : 650,
		height : 420,
		title : '商家优惠表信息添加'
	});
	var divForm = divWindow.find('form');
	divForm.url = Common.getUrl('benefitctrl/editBenefit');
	
	$('#cityID').combobox({
		 onChange:function(){
			 var cityID = $(this).combobox('getValue'); 
			 checkBranchState();
			 
			 $('#areaID').combobox({
            	 url:Common.getUrl('areactrl/selectAreaList'),
            	 queryParams:{
            		'city' : cityID,
            		'flag' : 3
            	 }
			 });
//			 $('#merchantId').combobox({
//	           	 url:Common.getUrl('merchantInfoctrl/selectMerchantList'),
//	           	 queryParams:{
//	           		'areaID' : cityID
//	           	 }
//			 });
		 }
	}); 
	
	$('#merchantNature').combobox({
		 onChange:function(){
			 updateMerchant();
		 }
	}); 
	
	$('#industryType').combobox({
		 onChange:function(){
			 updateMerchant();
		 }
	}); 
	
	function updateMerchant() {
		var cityID = $("#areaID").combobox('getValue'); 
		 var industryType = $('#industryType').combobox('getValue');
		 var merchantNature = $('#merchantNature').combobox('getValue');
		 checkBranchState();
		 
		 $('#merchantId').combobox({
          	 url:Common.getUrl('merchantInfoctrl/selectMerchantList'),
          	 queryParams:{
          		'areaID' : cityID,
          		'industryType' : industryType,
          		'merchantNature' : merchantNature
          	 }
		 });
	}
	
	$('#merchantId').combobox({
		 onChange:function(){
			 var cityID = $("#areaID").combobox('getValue'); 
			 var industryType = $('#industryType').combobox('getValue');
			 var merchantNature = $('#merchantNature').combobox('getValue');
			 var merchantId = $('#merchantId').combobox('getValue');
			 checkBranchState();
			 
			 if(merchantNature == 1) { // 连锁店总店 的情况下
				 $("#chainMerchant").show();
				 $.ajax({
						timeout : 15000,
						async : true,
						cache : false,
						type : 'POST',
						url : Common.getUrl('merchantInfoctrl/selectMerchantList'),
						dataType : 'json',
						data : {
							'cityID' : cityID,
							'industryType' : industryType,
							'merchantNature' : merchantNature,
							'merchantId' : merchantId
						},
						success : function(data) {
							
							var htmlItem = "";
							if(data.length > 0) {
								for (var i = 0; i < data.length; i++) {
									var merchantName = data[i].merchantName;
									var merchantId = data[i].merchantId;
									htmlItem += "<input name='merchatBranch' type='checkbox' style='margin-left:18px' value='" + merchantId + "'/>" + merchantName;
								}
							} else {
								htmlItem = "还没有该商家的分店数据，请先录入！";
							}
							$("#chainBranch").html(htmlItem);
							
							
						},
						error : function(errdata) {
							Common.alert('请求服务器失败，操作未能完成！');
						}
					});
			 }
			
		 }
	}); 
	
	$('#areaID').combobox({
		 onChange:function(){
			 var areaID = $(this).combobox('getValue'); 
			 $('#merchantId').combobox({
	          	 url:Common.getUrl('merchantInfoctrl/selectMerchantList'),
	          	 queryParams:{
	          		'areaID' : areaID
	          	 }
			 });
		 }
	}); 
	
	$('#importCheck').change(function() { 
		if($(this).is(':checked')){
			$('#import').show();
			$('#define').hide();
		}else{
			$('#define').show();
			$('#import').hide();
		}
			
	}); 
	
	function checkBranchState() {
		 var merchantNature = $('#merchantNature').combobox('getValue');
		 if(merchantNature == 1) { //连锁店总店
			 $("#chainMerchant").show();
			 $('#chainBranch').html("请选择商家!");
		 } else {
			 $("#chainMerchant").hide();
		 }
	}
	
	// 添加商家优惠信息 start 显示添加用户窗口
	function addInfo() {
		$("#btnSave").removeAttr("disabled");// 启用按钮
		divWindow.window('open');
		divForm.form('clear');
		$('#area').show();
		$('#coupon').show();
		$('#limitedCount').textbox('setValue', 1);
		$('#merchantId').combobox('enable'); 
		$('#benefitCount').combobox('enable');
		$('#define').show();
		$('#import').hide();
		$('#chainMerchant').hide();
		$('#trIndustryType').show();
		$('#trMerchantNature').show();
	}
	
	// 修改商家优惠信息
	function editInfo() {
		$("#btnSave").removeAttr("disabled");// 启用按钮
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems) {
			alert('请先选择要编辑的数据！');
			return;
		}
		
		var rows = grid.datagrid('getSelections');
		if (rows.length > 1) {
			Common.alert('无法同时选择多条信息操作！');
			return;
		}
		
		divWindow.window('open');
		divForm.form('clear');
		$('#area').hide();
		$('#coupon').hide();
		$('#trIndustryType').hide();
		$('#trMerchantNature').hide();
		divForm.form('load', checkedItems);  //从页面获取值
		$('#benefitImage2').attr('src', checkedItems.benefitImage); 
		$('#merchantId').combobox('disable'); 
	}

	$('#btnSave').click(function() {
		var benefitId = $('#benefitId').textbox('getValue');
		var merchantId = $('#merchantId').textbox('getValue');
		var benefitName = $('#benefitName').textbox('getValue');
		var benefitQuota = $('#benefitQuota').textbox('getValue');
		var useExplain = $('#useExplain').textbox('getValue');		
		var validTime = $('input#validTime').datebox('getValue');
		if(merchantId==''){
			Common.alert("商家不能为空!");
			return;
		}
		if(benefitName==''){
			Common.alert("优惠信息名称不能为空!");
			return;
		}
		if(benefitQuota==''){
			Common.alert("优惠额度不能为空!");
			return;
		}
		
		var display =$('#chainMerchant').css('display');
		var merchantIdMuti = ""; // 连锁分店列表值
		if(display != 'none'){
		   
			var merchantIdMuti = "";
			 $("[name='merchatBranch']:checked").each(function(index, element) {
				 merchantIdMuti += $(this).val() + ",";
			 });
			 if(merchantIdMuti.length > 0) {//如果获取到
		         merchantIdMuti = merchantIdMuti.substring(0, merchantIdMuti.length - 1); //把最后一个逗号去掉
			 } else {
				 Common.alert("请选择要使用的分店名称!");
			 }
		}
		
		if(benefitId =='' || benefitId == null){
			if($('#importCheck').is(':checked')){
				var couponFile = $('#couponFile').filebox('getValue');
				if(couponFile==''){
					Common.alert("优惠券导入文件不能为空!");
					return;
				}else{
					var reg =/([^\s]+(?=\.(xls|xlsx))\.\2)/gi;
					if(couponFile !='' && !reg.test(couponFile)) {
						Common.alert('优惠券导入文件类型只能为xls、xlsx！');
						return;
					}
				}
			}else{
				var couponRule = $('#couponRule').textbox('getValue');
				var couponSeqStart = $('#couponSeqStart').textbox('getValue');
				var couponSeqEnd = $('#couponSeqEnd').textbox('getValue');
				if(couponRule==''){
					Common.alert("优惠券规则不能为空!");
					return;
				}
				if(couponSeqStart=='' || couponSeqEnd == ''){
					Common.alert("优惠券序列范围不能为空!");
					return;
				}
				var count = Number(couponSeqEnd)- Number(couponSeqStart);
				if(Number(count)<1){
					Common.alert("优惠券序列范围错误，生成的优惠券数量为："+ count);
					return;
				}
			}
		}
		
		if(useExplain==''){
			Common.alert("使用条件不能为空!");
		}
		
		if(validTime==''){
			Common.alert("有效时间不能为空!");
			return;
		}
		
		var benefitImage = $('#benefitImage2').filebox('getValue');
		var reg =/([^\s]+(?=\.(jpg|png|gif|jpeg))\.\2)/gi; 
		if(benefitImage !='' && !reg.test(benefitImage)) {
			Common.alert('优惠照片文件类型只能为jpg、png、gif、jpeg！');
			return;
		}
		
		divForm.ajaxSubmit({
			url : divForm.url,
			dataType : "json",
			data : {
				'merchantIdMuti' : merchantIdMuti
			},
			success : function(data) {
				Common.alert(data.message);
				if (data.result == 0) {
					//$("#btnSave").attr({"disabled" : "disabled"});
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
			items.push(item.benefitId);
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
						url : Common.getUrl('benefitctrl/delbenefit'),
						dataType : 'json',
						data : {
							'benefitId' : ids
						},
						success : function(data) {
							Common.alert(data.message);
							if (data.result == 0) {
								grid.datagrid('reload');
								grid.datagrid('clearSelections');
							}

						},
						error : function(errdata) {
							Common.alert(data.message);
						}
					});
				}
			});
		} else {
			alert('请先选择要删除的数据！');
		}
	}
	
	//优惠券码
	//初始化弹出框	
	var couponWindow = $('#couponWindow').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:650,
		height:400,
		title: '优惠券码列表'
	});
	var couponForm = couponWindow.find('form');
	couponForm.url = Common.getUrl('/benefitctrl/couponList');
	
	function coupon(){
		var checkedItems = grid.datagrid('getSelected');
		if (!checkedItems) {
			Common.alert('请先选择一条信息操作！');
			return;
		}
		var rows = grid.datagrid('getSelections');
		if (rows.length > 1) {
			Common.alert('无法同时选择多条信息操作！');
			return;
		}
		couponWindow.window('open');
		//$('#imgViewer').viewer();
		var couponList = $('#couponList').datagrid({
			striped : true,
			dataType : 'json',
			url : couponForm.url,
			method: 'POST',
			queryParams: {
				benefitID : checkedItems.benefitId
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
		couponList.datagrid('getPager').pagination({
			beforePageText : Common.beforePageText,
			afterPageText : Common.afterPageText,
			displayMsg : Common.displayMsg
		});
	}
	
	//推荐
	function setRecommend() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.benefitId);
		});
		var benefitId = items.join(',');
        
		if (checkedItems.length > 0) {
			Common.confirm('确定要对设置当前的记录为推荐数据吗？', function(r) {
				if (r) {
					grid.datagrid('load');
					grid.datagrid('clearSelections');
					
					$.ajax({
						timeout	: 15000,
						async	: true,
						cache	: false,
						type	: 'POST',
						url		: Common.getUrl('/benefitctrl/setRecommend'),
						dataType: 'json',
						data	: {
							'benefitId': benefitId
						},
						success	: function(data) {
							if (data.result == 0) {
								Common.alert(data.message);
								grid.datagrid('reload');
								grid.datagrid('clearSelections');
							} else {
								Common.alert(data.message);
							}
						},
						error: function(errdata) {
							Common.alert(data.message);
						}
					});
				}
			});
		} else {
			Common.alert('请先选择要推荐的数据！');
		}		
	}
	
	//取消推荐
	function cancelRecommend() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.benefitId);
		});
		var benefitId = items.join(',');
        
		if (checkedItems.length > 0) {
			Common.confirm('确定要对取消当前的推荐数据吗？', function(r) {
				if (r) {
					grid.datagrid('load');
					grid.datagrid('clearSelections');
					
					$.ajax({
						timeout	: 15000,
						async	: true,
						cache	: false,
						type	: 'POST',
						url		: Common.getUrl('/benefitctrl/cancelRecommend'),
						dataType: 'json',
						data	: {
							'benefitId': benefitId
						},
						success	: function(data) {
							if (data.result == 0) {
								Common.alert(data.message);
								grid.datagrid('reload');
								grid.datagrid('clearSelections');
							} else {
								Common.alert(data.message);
							}
						},
						error: function(errdata) {
							Common.alert(data.message);
						}
					});
				}
			});
		} else {
			Common.alert('请先选择要取消推荐的数据！');
		}		
	}

	
	// checkbox 取值
	// 注意：如果需要使用checkbox进行多选，须在jsp页面中对datagrid加入:
	// singleSelect:false,selectOnCheck:true,checkOnSelect:true
	$('#chkbox').click(function() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();

		$.each(checkedItems, function(index, item) {
			items.push(item.factoryCode + '=' + item.factoryName);
		});

		alert(items.join(','));
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
});