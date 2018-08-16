$(function() {

	$('#cc').combobox({
		url : Common.getUrl('menuctrl/getbutton'),
		valueField : 'id',
		textField : 'text',
		multiple : true,
		editable: false, 
		panelHeight : '220',
		onLoadSuccess : function() {		
		}

	});  

	// 初始化tree，需要放到datagrid之后进行初始化
	var t = Common.tree('divTree', {
		url : Common.getUrl('menuctrl/menustree'),
		checkbox : false,
		contextMenuAction : true,
		onClick : function(node) {
			
			//加载 表单
			var nodeId = node.id;
			if(nodeId == 0){
				return;
			}
			$.ajax({
				timeout : 15000,
				async : true,
				cache : false,
				type : 'POST',
				url : Common.getUrl('menuctrl/selectmenuinfo'),
				dataType : 'json',
				data : {
					'se_menu_id' : node.id
				},
				success : function(data) {
					if (data.result == 0) {
						var menu = data.menuObj;
						$('#menu_id').textbox('setValue', menu.menuId);
						$('#menu_name').textbox('setValue', menu.menuName);
						$('#menu_icon').textbox('setValue', menu.menuIcon);
						$('#menu_sort').textbox('setValue', menu.menuOrder);
						$('#menu_url').textbox('setValue', menu.menuUrl);
						$("#menu_desc").val(menu.menuDesc);
						$("#btnSave").removeAttr("disabled");// 启用按钮
					} else {
						Common.alert(data.message);
					}
				},
				error : function(errdata) {
					Common.alert('请求服务器失败，操作未能完成！');
				}
			});
			//加载下拉列表框
			
			$('#cc').combobox({
				url : Common.getUrl('menuctrl/getbutton'),
				valueField : 'id',
				textField : 'text',
				multiple : true,
				editable: false, 
				panelHeight : '220',
				onLoadSuccess : function() {
					var datanode = $('#cc').combobox('getData');
		            if (datanode.length > 0) {
		            	var arrayObj = new Array();
		            	for (var j = 0; j < datanode.length; j++) {
		            		arrayObj[j] = datanode[j].id;
						}
		            }
					$.ajax({
						timeout	: 15000,
						async	: true,
						cache	: false,
						type	: 'POST',
						url		: Common.getUrl('menuctrl/getbuttonby'),
						dataType: 'json',
						data	: {
							'menuid' : node.id
						},
						beforeSend: function() {
						},
						success	: function(data) {
							
							
							 for (var i = 0; i < data.str.length; i++){
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
							            for (; from < len; from++)  
							            {  
							              if (from in this &&  
							                  this[from] === elt)  
							                return from;  
							            }  
							            return -1;  
							          };  
							        }  
								  //解决ie8不支持indexOf end
								if( arrayObj.indexOf(data.str[i])>=0){ 
									$("#cc").combobox('select', arrayObj[arrayObj.indexOf(data.str[i])])
								}
							 }
						}
					});
				}
			});  
			
			
		},
		onAdd : function() {
			var node = t.tree('getSelected');
			var m_name = 'newNode';
			Common.confirm("确定为  《" + node.text + "》 添加子节点？", function() {
				$.ajax({
					timeout : 15000,
					async : true,
					cache : false,
					type : 'POST',
					url : Common.getUrl('menuctrl/addmenuinfo'),
					dataType : 'json',
					data : {
						'ad_menu_name' : m_name,
						'ad_menu_fatherid' : node.id
					},
					success : function(data) {
						if (data.result == 0) {
							t.tree('reload');
						} else {
							Common.alert(data.message);
						}
					},
					error : function(errdata) {
						Common.alert('请求服务器失败，操作未能完成！');
					}
				});
			});
		},
		onEdit : function() {
			var node = t.tree('getSelected');
			$.ajax({
				timeout : 15000,
				async : true,
				cache : false,
				type : 'POST',
				url : Common.getUrl('menuctrl/selectmenuinfo'),
				dataType : 'json',
				data : {
					'se_menu_id' : node.id
				},
				success : function(data) {
					if (data.result == 0) {
						var menu = data.menuObj;
						$('#menu_id').textbox('setValue', menu.menuId);
						$('#menu_name').textbox('setValue', menu.menuName);
						$('#menu_icon').textbox('setValue', menu.menuIcon);
						$('#menu_sort').textbox('setValue', menu.menuOrder);
						$('#menu_url').textbox('setValue', menu.menuUrl);
						$("#menu_desc").val(menu.menuDesc);
						$("#btnSave").removeAttr("disabled");// 启用按钮
					} else {
						Common.alert(data.message);
					}
				},
				error : function(errdata) {
					Common.alert('请求服务器失败，操作未能完成！');
				}
			});
		},
		onRemove : function() {
			var node = t.tree('getSelected');
			Common.confirm("确定删除  《" + node.text + "》 节点？", function() {
				$.ajax({
					timeout : 15000,
					async : true,
					cache : false,
					type : 'POST',
					url : Common.getUrl('menuctrl/delmenuinfo'),
					dataType : 'json',
					data : {
						'del_menu_id' : node.id
					},
					success : function(data) {
						if (data.result == 0) {
							t.tree('reload');
						} else {
							Common.alert(data.message);
						}
					},
					error : function(errdata) {
						Common.alert('请求服务器失败，操作未能完成！');
					}
				});
			});
		},
		onDblClick : function() {
			//双击折叠&展开节点
			var node = t.tree('getSelected');
			 $(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);  
			    node.state = node.state === 'closed' ? 'open' : 'closed';    
		}
	});

	$('#btnSave').click(function() {
		var buttonId = new Array();
		var buttonId = $("#cc").combobox("getValues");
		buttonId = buttonId+"";
		var menu_id = $('input#menu_id');
		var menu_name = $('input#menu_name');
		var menu_icon = $('input#menu_icon');
		var menu_sort = $('input#menu_sort');
		var menu_url = $('input#menu_url');
		var menu_desc = $('#menu_desc');
		if (menu_id.val() == '') {
			return;
		}
		if (menu_name.val() == '') {
			menu_name.focus();
			Common.alert('菜单名不能为空');
			return;
		}
		$.ajax({
			timeout : 15000,
			async : true,
			cache : false,
			type : 'POST',
			url : Common.getUrl('menuctrl/updatemenuinfo'),
			dataType : 'json',
			data : {
				'buttonid' : buttonId,
				'up_menu_id' : menu_id.val(),
				'up_menu_name' : menu_name.val(),
				'up_menu_icon' : menu_icon.val(),
				'up_menu_order' : menu_sort.val(),
				'up_menu_url' : menu_url.val(),
				'up_menu_desc' : menu_desc.val()
			},
			beforeSend : function() {
				$("#btnSave").removeAttr("disabled");// 启用按钮
			},
			success : function(data) {
				if (data.result == 0) {
					Common.alert(data.message);
					t.tree('reload');
				} else {
					Common.alert(data.message);
				}
			},
			error : function(errdata) {
				Common.alert('请求服务器失败，操作未能完成！');
			}
		});

	});

});

