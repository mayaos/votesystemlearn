$(function() {
	
	// 初始化logo选择栏属性
	$('#topicPic').filebox({
		buttonText : '选择标题图片',
		buttonAlign : 'left'
	});
	$('#topicPic2').filebox({
		buttonText : '选择标题图片',
		buttonAlign : 'left'
	});
	
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('orgArticlectrl/queryInfo'),
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
		url		: Common.getUrl('orgArticlectrl/getToolbar'),
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
		title : '文章详情信息添加'
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
		title : '文章详情信息编辑'
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
		width : 800,
		height : 500,
		title : '查看文章内容'
	});

	//根据区域id获取机构菜单
	$('#areaId').combotree({
		 onChange:function(node) {
			var areaId=$('#areaId').combo('getValue');
			 $('#orgId').combobox({
            	 url:Common.getUrl('orginfoctrl/queryorginfo'),
            	 queryParams:{
            		'areaId' : areaId
            	 }
			 });
		  }
		});

	//-------------------添加文章信息详情------------------------------------------------------
	//-------------------按钮选择事件------------------------------------------------------	
	$("#articleType1").click(function(){
			$("#link").hide()
			$("#content").show()	   
		});
	$("#articleType2").click(function(){
			$("#link").show()
			$("#content").hide()		   
		});
	//初始化选择类型显示
	function addInfo() {
		$("#btnSave1").removeAttr("disabled");// 启用按钮
		divWindow.window('open');
		divForm.form('clear');
		CKEDITOR.instances['add_content'].setData("");
		$("#link").hide()
		$("#content").hide()
		articleTypeShow();
		divForm.url = Common.getUrl('orgArticlectrl/addArticleInfo');
	}

	$('#btnSave1').click(function() {
		//获取参数
//		var menuId = $('#menuId').textbox('getValue');	
		var articleTitle = $('#articleTitle').textbox('getValue');
		var articleAuthor = $('#articleAuthor').textbox('getValue');
		var menuId = $('#menuId').combo('getValue');
		var orgId = $('#orgId').combo('getValue');	
		var areaId = $('#areaId').combo('getValue');	
//		var articleDesc = $('#articleDesc').textbox('getValue');
		var topicPic = $('#topicPic').textbox('getValue');
		var articleFrom = $('#articleFrom').textbox('getValue');
		var articleContent = CKEDITOR.instances['add_content'].getData();
		var issueTime = $('#issueTime1').textbox('getValue');
		var articleType = 	$("input[name='articleType']:checked").val();
		var articleLink = $('#articleLink').textbox('getValue');
		divForm.url = Common.getUrl('orgArticlectrl/addArticleInfo');
		console.log("divForm.url= " + divForm.url);
		//判断参数是否符合要求
		if(menuId==''){
			Common.alert("菜单名称不能为空!");
			return;
		}
		if(areaId==''){
			Common.alert("区域名称不能为空!");
			return;
		}
		if(orgId==''){
			Common.alert("机构名称不能为空!");
			return;
		}
		if(articleTitle==''){
			Common.alert("文章标题不能为空!");
			return;
		}
		if(articleAuthor==''){
			Common.alert("文章作者不能为空!");
			return;
		}
		if(articleFrom==''){
			Common.alert("文章来源不能为空!");
			return;
		}
		if(articleTitle.length>50){
			Common.alert("文章标题不能超过50个字符!");
			return;
		}
		if(articleAuthor.length>20){
			Common.alert("文章作者不能超过20个字符!");
			return;
		}
		if(articleFrom.length>50){
			Common.alert("文章来源不能超过50个字符!");
			return;
		}
		if(!checkedLogo($('#topicPic'))) {
			return;
		}
		if(articleType==null){
			Common.alert("文章类型必须选择!");
			return;			
		}
		else if(articleType=='0'){
			if(articleContent=='') {
				Common.alert("文章内容不能为空!");
				return;
			}			
		}
		else if(articleType=='1'){
			if(articleLink=='') {
				Common.alert("文章链接不能为空!");
				return;
			}
			var fdStart = articleLink.indexOf("http://");
			if(fdStart<0){
				Common.alert("文章链接必须以http://");
				return;
			}
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
				'articleContent' : articleContent
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
	
	//----------------------修改文章信息---------------------------------------------
	//-------------------按钮选择事件------------------------------------------------------	
	$("#article2Type1").click(function(){
			$("#link2").hide()
			$("#content2").show()	   
		});
	$("#article2Type2").click(function(){
			$("#link2").show()
			$("#content2").hide()		   
		});
	// 修改文章信息详情表信息
	function editInfo() {
		divForm2.url = Common.getUrl('orgArticlectrl/updateArticleInfo');
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
		
		$.ajax({
			timeout : 15000,
			async : true,
			cache : false,
			type : 'POST',
			url : Common.getUrl('orgArticlectrl/queryArticleDetailInfo'),
			dataType : 'json',
			data : {
				'articleId' : checkedItems.articleId
			},
			success : function(data) {
				console.log(data.article.issueTime);
				$('#articleId2').textbox('setValue', data.article.articleId);
				$('#menuId2').combobox('select', data.article.menuId);
				$('#orgId2').combobox('select', data.article.orgId);
				$('#areaId2').textbox('setValue', data.article.areaName);
				$('#areaIdHidden').html(data.article.areaId);
				$('#articleTitle2').textbox('setValue', data.article.articleTitle);
				$('#articleAuthor2').textbox('setValue', data.article.articleAuthor);
				$('#articleFrom2').textbox('setValue', data.article.articleFrom);
				$('#articleDesc2').val(data.article.articleDesc);
				CKEDITOR.instances['edit_content'].setData(data.article.articleContent);
				$('#issueTime2').datebox('setValue', data.article.issueTime);
				$('#gridList').datagrid('clearSelections');
				$("#articleLink2").textbox('setValue', data.article.articleLink);
				if(data.article.articleType=="0"){
					$("#article2Type1").checked="true";
					$("#link2").hide()
					$("#content2").show()	
				}
				else if(data.article.articleType=="1"){
					$("#article2Type2").checked="true";
					$("#link2").show()
					$("#content2").hide()	
				}
				divWindow2.window('open');
			},
			error : function(errdata) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});
		
	}
	
	$('#btnSave2').click(function() {
		//获取参数
		var articleAuthor = $('#articleAuthor2').textbox('getValue');
		var menuId = $('#menuId2').combo('getValue');
		var orgId = $('#orgId2').combo('getValue');	
		
		//区域内容回显
		//areaIdHidden 得到的是areaId， areaId2得到的可能是areaId，也有可能是areaName
		var areaId;
		if(!isNaN($('#areaId2').combo('getValue'))) {
			//是数字
			areaId = $('#areaId2').combo('getValue');
		} else {
			areaId = $('#areaIdHidden').html();
		}
		
		var topicPic = $('#topicPic2').textbox('getValue');
		var articleFrom = $('#articleFrom2').textbox('getValue');
		var articleContent = CKEDITOR.instances['edit_content'].getData();
		var issueTime = $('#issueTime2').textbox('getValue');
		var articleType = 	$("input[name='articleType2']:checked").val();
		var articleLink = $('#articleLink2').textbox('getValue');
		console.log("divForm.url= " + divForm.url);
		//判断参数是否符合要求
		if(menuId==''){
			Common.alert("机构名称不能为空!");
			return;
		}
		if(orgId==''){
			Common.alert("机构名称不能为空!");
			return;
		}
		if(articleTitle==''){
			Common.alert("文章标题不能为空!");
			return;
		}
		if(articleFrom==''){
			Common.alert("文章来源不能为空!");
			return;
		}
		if(articleAuthor==''){
			Common.alert("文章作者不能为空!");
			return;
		}
		if(articleTitle.length>50){
			Common.alert("文章标题不能超过50个字符!");
			return;
		}
		if(articleAuthor.length>20){
			Common.alert("文章作者不能超过20个字符!");
			return;
		}
		if(articleFrom.length>50){
			Common.alert("文章来源不能超过50个字符!");
			return;
		}
//		if(!checkedLogo($('#topicPic2'))) {
//			return;
//		}
		if(articleType==null){
			Common.alert("文章类型必须选择!");
			return;			
		}
		else if(articleType=='0'){
			if(articleContent=='') {
				Common.alert("文章内容不能为空!");
				return;
			}			
		}
		else if(articleType=='1'){
			if(articleLink=='') {
				Common.alert("文章链接不能为空!");
				return;
			}
			var fdStart = articleLink.indexOf("http://");
			if(fdStart<0){
				Common.alert("文章链接必须以http://");
				return;
			}
		}
		if(issueTime=='') {
			Common.alert("文章发表时间不能为空!");
			return;
		}
		
		divForm2.ajaxSubmit({
			type : 'post',
			url : divForm2.url,
			dataType : 'json',
			beforeSend : function() {
				$("#btnSave2").removeAttr("disabled");// 启用按钮
			},
			data : {
				'articleContent' : articleContent,
				'areaIdHidden' : areaId
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
	

	//---- 删除文章详情记录---------------------------------------------------
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
						url : Common.getUrl('orgArticlectrl/delArticleInfo'),
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
	
	//---- 发布文章详情记录---------------------------------------------------
	function release() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.articleId);
		});
		var ids = items.join(',');
		if (checkedItems.length > 0) {
			Common.confirm('确定要发布当前选择的记录吗？', function(r) {
				if (r) {
					grid.datagrid('load');
					grid.datagrid('clearSelections');

					$.ajax({
						timeout : 15000,
						async : true,
						cache : false,
						type : 'POST',
						url : Common.getUrl('orgArticlectrl/releaseArticleInfo'),
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
			Common.alert('请先选择要发布的数据！');
		}
	}
	//--------------------------------------------------------------
	//---- 取消发布文章详情记录---------------------------------------------------
	function delrelease() {
		var checkedItems = grid.datagrid('getChecked');
		var items = new Array();
		$.each(checkedItems, function(index, item) {
			items.push(item.articleId);
		});
		var ids = items.join(',');
		if (checkedItems.length > 0) {
			Common.confirm('确定要取消发布当前选择的记录吗？', function(r) {
				if (r) {
					grid.datagrid('load');
					grid.datagrid('clearSelections');

					$.ajax({
						timeout : 15000,
						async : true,
						cache : false,
						type : 'POST',
						url : Common.getUrl('orgArticlectrl/delreleaseArticleInfo'),
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
			Common.alert('请先选择要取消发布的数据！');
		}
	}
	//-------条件查询--------------------------------------------------
	$('#search').click(function() {
		
		var titleSearch = $('#titleSearch').textbox('getText');
		var authorSearch = $('#authorSearch').textbox('getText');
		var issueOrNotSearch = $('#issueOrNotSearch').textbox('getValue');
		var issueDateSearch = $('#issueDateSearch').datebox('getValue');
		var areaIdSearch = $('#areaIdSearch').textbox('getValue');
		var menuIdSearch = $('#menuIdSearch').textbox('getValue');
		
		// 使用参数查询
		grid.datagrid('load', {
			'titleSearch' : titleSearch,
			'authorSearch' : authorSearch,
			'areaIdSearch' : areaIdSearch,
			'menuIdSearch' : menuIdSearch,
			'issueOrNotSearch' : issueOrNotSearch,
			'issueDateSearch' : issueDateSearch
		});
	});
	
	$('#reset').click(function() {
		$('#titleSearch').textbox('setValue','');
		$('#rightsTypeSearch').textbox('setValue','');
		$('#issueOrNotSearch').textbox('clear');
		$('#issueDateSearch').textbox('setValue','');
		$('#authorSearch').textbox('setValue','');
		$('#areaIdSearch').textbox('setValue','');
		$('#menuIdSearch').textbox('setValue','');
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
		url : Common.getUrl('orgArticlectrl/querycontent'),
		dataType : 'json',
		data : {
			'articleId' : pkId
		},
		success : function(data) {
			CKEDITOR.instances['display_content'].setData(data.article.articleContent);
			// 设置样式
			CKEDITOR.config.contentsCss = '/residence/app/manage/modules/common/orgarticle/template_edu.css';
			// ckeditor源码编辑模式，添加style、javascript内容丢失的解决 
			CKEDITOR.replace( 'textarea_id', { allowedContent: true});
			$('#gridList').datagrid('clearSelections');
			displayWindow.window('open');
		},
		error : function(errdata) {
			Common.alert('请求服务器失败，操作未能完成！');
		}
	});
}

/**
 * 检查图片格式是否为png格式
 */
function checkedLogo(topicPic) {
	// logo文件扩展名解析 xxx.png
	var logoFile = topicPic.filebox("getValue");
	// 非空校验
	if (logoFile.length == 0) {
		Common.alert('请选择要上传的图片!');
		return false;
	}
	var logoPointIndex = logoFile.lastIndexOf(".");
	var logoExtenName = logoFile.substring(logoPointIndex + 1, logoFile.length);
	// alert(phoextenName);
	// 扩展名校验
	if (logoExtenName.toLowerCase() != "jpg" && logoExtenName.toLowerCase() != "png" && 
			logoExtenName.toLowerCase() != "bmp" && logoExtenName.toLowerCase() != "gif")  {
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