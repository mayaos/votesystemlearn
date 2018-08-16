﻿var divWindow, divWindow2, divWindow3, divWindow4;
var divForm, divForm2, divForm3, divForm4;
var grid;
var toolbarres;
$(function(){
	$.ajax({
		timeout	: 15000,
		async	: true,
		cache	: false,
		type	: 'POST',
		url		: Common.getUrl('rolesctrl/getToolbar'),
		dataType: 'json',
		data	: {
		},
		beforeSend: function() {
		},
		success	: function(data) {
			toolbarres = data.result;
		},
		error: function(errdata) {
			Common.alert('请求服务器失败，操作未能完成！');
		}
	});
	
	var toolbar = [{}];
	// 初始化数据表格，并将对象实例存储到变量
	 grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('rolesctrl/getlist'),
		pagination: true,
		pagePosition: 'bottom',
		pageNumber: 1,
		pageSize: Common.pageSize,
		pageList: Common.pageList,
		toolbar: toolbar ,
		multiSort: false,
		remoteSort: false,
		onLoadSuccess: function(data){
			//清空div
			$('#gridList').datagrid("emptyItem",'');
			//循环添加
			for (var i = 0; i < toolbarres.length; i++) {  
				var text = toolbarres[i].text+"";
				var icon = toolbarres[i].icon+"";
				var handler = toolbarres[i].handler+"";
				$('#gridList').datagrid("addToolbarItem",[{"text" : text,"iconCls" : icon,"handler" : handler},"-"]);
			}
		}
		//sortOrder: 'asc',
		//sortName: 'factoryCode'
	});
	// 设置分页控件显示汉字
	grid.datagrid('getPager').pagination({ 
		beforePageText: Common.beforePageText, 
		afterPageText: Common.afterPageText, 
		displayMsg: Common.displayMsg
	});
	
	//条件查询
	$('#search').click(function() {
		var keyword = $('#rolename').textbox('getText');
		// 使用参数查询
		grid.datagrid('load', {
			'roleName': keyword
		});
	});
	divWindow = $('#divWindow').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:650,
		height:280,
		title: '角色信息编辑'
	});
	divForm = divWindow.find('form');
	
	divWindow2 = $('#divWindow2').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		modal:true,
		closed:true,
		width:650,
		height:350,
		title: '授权用户'
	});
	divForm2 = divWindow2.find('form');
	
	divWindow3 = $('#divWindow3').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:300,
		height:450,
		title: '分配菜单'
	});
	divForm3 = divWindow3.find('form');
	
	divWindow4 = $('#divWindow4').window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		modal:true,
		closed:true,
		width:300,
		height:450,
		title: '分配按钮'
	});
	divForm4 = divWindow4.find('form');
	
	//修改ajax
	$('#btnSave').click(function(){
		var roleName = $('input#role_name');
		var roleDesc = $('#role_desc');
		var roleId = $('#role_id');
		var isdefault = $('input[name=isdefault]:checked').val();
			
		if (roleName.val() == '') {
			roleName.focus();
			Common.alert('角色名称不能为空！');
			return;
		}
		if(roleName.val().length>20) {
			roleName.focus();
			Common.alert('角色名称不能超过20位！');
			return;
		}
		
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: divForm.url,
			dataType: 'json',
			data	: {
				'id': roleId.val(),
				'roleName': roleName.val(),
				'roleDesc': roleDesc.val(),
				'isdefault' :isdefault,
			},
			beforeSend: function() {
				$("#btnSave").removeAttr("disabled");//启用按钮  
			},
			success	: function(data) {
				//$("#btnSave").attr({ "disabled": "disabled" });//禁用按钮  
				if (data.result == 1) {
					$("#btnSave").attr({ "disabled": "disabled" });
					grid.datagrid('load');
					divWindow.window('close');
					grid.datagrid('clearSelections');
					if(divForm.url.indexOf("insert") > 0 ){
					}else {
						Common.alert(data.message);
					}
				} else {
					Common.alert(data.message);
				}
			},
			error: function(errdata) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});
	});
	
	$('#btnCancel').click(function() {
		divWindow.window('close'); 
	});
	// 添加角色信息
	function addinfo() {
		$("#btnSave").removeAttr("disabled");//启用按钮
		divWindow.window('open');
		divForm.form('clear');
		divForm.url = Common.getUrl('rolesctrl/insert');
	}
	// 修改角色信息
	function editInfo() {}
	
	// 删除角色信息
	function deleteinfo() {}
	
	// 授权菜单
	function empower() {}
	
	// 授权按钮
	function embutton() {}
	
});

function addInfo(){
	$("#btnSave").removeAttr("disabled");//启用按钮
	divWindow.window('open');
	divForm.form('clear');
	divForm.url = Common.getUrl('rolesctrl/insert');
	$('input[name=isdefault]').get(0).checked= true;
}

function editInfo(){
	$("#btnSave").removeAttr("disabled");//启用按钮  
	divForm.url = Common.getUrl('rolesctrl/update');
	var checkedItems = grid.datagrid('getSelected');
	if (checkedItems){
		var check = grid.datagrid('getChecked');
		if(check.length > 1){
			Common.alert('请选择一条数据！');
			return;
		}
		if (check[0].role_name == '超级管理员') {
			Common.alert('超级管理员角色不允许修改！');
			return;
		}
		divWindow.window('open');
		divForm.form('load', checkedItems);//从页面获取值
		$("#id").textbox('setValue',checkedItems.role_id);
		var isdefault = checkedItems.isdefault=="否"? "NO":"YES";
		$('input[name=isdefault][value='+isdefault+']').prop('checked', true);
	} else {
		Common.alert('请先选择要编辑的数据！');
	}
}

function deleteInfo(){
	var checkedItems = grid.datagrid('getChecked');
	var items = new Array();
	var delFlag = 1;
	$.each(checkedItems, function(index, item) {
		items.push(item.role_id);
		if(item.role_name =='超级管理员') {
			Common.alert('超级管理员不允许删除');
			delFlag = 0;
			return;
		}
	});
	
	if(delFlag == 0) {
		return;
	}
	
	var ids = items.join(',');
	if (checkedItems.length > 0) {
		Common.confirm('确定要删除当前选择的记录吗？', function(r) {
			if (r) {
				grid.datagrid('load');
				grid.datagrid('clearSelections');
				$.ajax({
					timeout	: 15000,
					async	: true,
					cache	: false,
					type	: 'POST',
					url		: Common.getUrl('rolesctrl/delete'),
					dataType: 'json',
					data	: {
						'strids': ids
					},
					success	: function(data) {
						if (data.result == 1) {
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
		Common.alert('请先选择要删除的数据！');
	}

}

function embutton(){
	var checkedItems = grid.datagrid('getChecked');
	if (checkedItems.length < 1) {
		Common.alert('请先选择要分配按钮的角色！');
		return;
	} else if (checkedItems.length > 1) {
		Common.alert('只能选择一个角色！');
		return;
	}
	divWindow4.window('open');
	var t = Common.tree('divbutton', {
		url : Common.getUrl('menuctrl/buttontree?strRoleId='+checkedItems[0].role_id),
		checkbox : true,
		contextMenuAction : true,
		onLoadSuccess: function() {
				// 动态加载角色对应菜单列表，并选中checkbox
				$.ajax({
					timeout	: 15000,
					async	: true,
					cache	: false,
					type	: 'POST',
					url		: Common.getUrl('rolesctrl/rolebutton'),
					dataType: 'json',
					data	: {
						'strRoleId': checkedItems[0].role_id
					},
					beforeSend: function() {
						$('#saveSettings').attr('disabled', true);
					},
					success	: function(data) {
						if (data.result == 0) {
							for (var key in data.map) {
								var arr = [];
								arr = data.map[key];
								var  node= t.tree('find', key); 
								if (node) {  
				                    var children = t.tree('getChildren', node.target);  
				                }  
								if(children.length>0){
				                for (var i = 0; i < children.length; i++) {  
				                	//解决ie8不支持indexOf
				                    if (!Array.prototype.indexOf){  
				                        Array.prototype.indexOf = function(elt /*, from*/){  
				                        var len = this.length >>> 0;  
				                        var from = Number(arguments[1]) || 0;  
				                        from = (from < 0)  
				                             ? Math.ceil(from)  
				                             : Math.floor(from);  
				                        if (from < 0)  
				                          from += len;  
				                        for (; from < len; from++) {  
				                          if (from in this &&  
				                              this[from] === elt)  
				                            return from;  
				                        }  
				                        return -1;  
				                      };  
				                    }  
				                  //解决ie8不支持indexOf
				                  if(arr.indexOf(children[i].id)>=0){
				                	  var  nodes= t.tree('find', children[i].id); 
				                	  if(null!=nodes){
											t.tree('check', nodes.target);
										}
				                  }
				                }  
								}
						    }
						}
						// 绑定一次性事件
						$('#savebtton').attr('disabled', false);
						$('#savebtton').one('click', function() { savebutton(t) });
					}
				});
			}
	});
	// 设置对应角色的菜单列表，获取选中的checkbox，并使用ajax提交
	// 此设置不能在tree的onCheck方法中实现，在回显的时候会触发tree的check事件，会不停的check
	function savebutton(tree) {
		var arrMenuID = new Array();
		var nodes = tree.tree('getChecked');
		$.each(nodes, function(i, n) {
			var b = $('#divTree').tree('isLeaf', n.target);  //判断是否是叶子节点
			if(b){
				arrMenuID.push(n.id);
			}
		});
		var menuIds = arrMenuID.join(',');
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: Common.getUrl('rolesctrl/rolebuttoninsert'),
			dataType: 'json',
			data	: {
				'strRoleId': checkedItems[0].role_id,
				'strmenuIds': menuIds
			},
			success	: function(data) {
				if (data.result == 0) {
					Common.alert(data.message);
					divWindow4.window('close'); //关闭窗口
					grid.datagrid('load'); //数据重新加载
				} else {
					Common.alert(data.message);
				}			
			}
		});
	}
}

function empower(){
	var checkedItems = grid.datagrid('getChecked');
	if (checkedItems.length < 1) {
		Common.alert('请先选择要授权的角色！');
		return;
	} else if (checkedItems.length > 1) {
		Common.alert('只能选择一个角色！');
		return;
	}
	divWindow3.window('open');
	var t = Common.tree('divTree', {
		url : Common.getUrl('menuctrl/menustree'),
		checkbox : true,
		contextMenuAction : true,
		onLoadSuccess: function() {
				// 动态加载角色对应菜单列表，并选中checkbox
				$.ajax({
					timeout	: 15000,
					async	: true,
					cache	: false,
					type	: 'POST',
					url		: Common.getUrl('rolesctrl/rolemenu'),
					dataType: 'json',
					data	: {
						'strRoleId': checkedItems[0].role_id
					},
					beforeSend: function() {
						$('#saveSettings').attr('disabled', true);
					},
					success	: function(data) {
						if (data.result == 0) {
							var arrList = data.rList;
							for (var i=0; i<arrList.length; i++) {
								var m = arrList[i];
								var  node= t.tree('find', m.menu_id);  
								if(null!=node){
									t.tree('check', node.target);
								}
							}
						}
						// 绑定一次性事件
						$('#saveSettings').attr('disabled', false);
						$('#saveSettings').one('click', function() { saveSettings(t) });
					}
				});
			}
	});

	
	// 设置对应角色的菜单列表，获取选中的checkbox，并使用ajax提交
	// 此设置不能在tree的onCheck方法中实现，在回显的时候会触发tree的check事件，会不停的check
	function saveSettings(tree) {
		var arrMenuID = new Array();
		var nodes = tree.tree('getChecked');
		$.each(nodes, function(i, n) {
			var b = $('#divTree').tree('isLeaf', n.target);  //判断是否是叶子节点
			if(b){
				arrMenuID.push(n.id);
			}
		});
		var menuIds = arrMenuID.join(',');
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: Common.getUrl('rolesctrl/roleinsert'),
			dataType: 'json',
			data	: {
				'strRoleId': checkedItems[0].role_id,
				'strmenuIds': menuIds
			},
			success	: function(data) {
				if (data.result == 0) {
					Common.alert(data.message);
					divWindow3.window('close'); //关闭窗口
					grid.datagrid('load'); //数据重新加载
				} else {
					Common.alert(data.message);
				}			
			}
		});
	}

}

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

//for ie can't support indexof
if(!Array.indexOf){
            Array.prototype.indexOf = function(obj){
                for(var i=0; i<this.length; i++){
                    if(this[i]==obj){
                        return i;
                    }
                }
                return -1;
            }
        }