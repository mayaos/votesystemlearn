var mTitle = '首页';
var min_c=0;
$(function(){
	tabCloseEven();
    $menuTree=$('#menuTree').tree({
		url:'jfp_function!loginTree.action',
		onClick:function(node){
			if(node.state=='closed'){
				$(this).tree('expand',node.target);
			}else{
				$(this).tree('collapse',node.target);
				if(node.attributes.funUrl!=''){
                    code_all=node.id
					addTab(node.text,_base + node.attributes.funUrl);
				}
			}
		},
//		onBeforeExpand:function(node){
//				$(this).tree('update',{
//					target: node.target,
//					iconCls:'icon-reload'
//				});
//		},
//		onBeforeCollapse:function(node){
//			$(this).tree('update',{
//				target: node.target,
//				iconCls:'icon-a'
//			});
//		},
//		onExpand:function(node){
//			$(this).tree('update',{
//				target: node.target,
//				 iconCls:'icon-a'
//			});
//		},
		onLoadSuccess:function(){
			// var id=window.parent.document.getElementById('flagfortz').value;
			// expandTo(id);
		}
	});
});
function expandTo(treeId, nodeId){
	var _id = nodeId?nodeId:null;
	var node = $('#'+treeId).tree('find',_id);
	if(node){
		$('#'+treeId).tree('expandTo', node.target).tree('select', node.target);
		if(node.attributes.funUrl!=''){
			addTab(node.text,_base+node.attributes.funUrl);
		}
	}
}
function refreshTab(cfg){  
    var refresh_tab = cfg.tabTitle?$('#tabs').tabs('getTab',cfg.tabTitle):$('#tabs').tabs('getSelected');  
    if(refresh_tab && refresh_tab.find('iframe').length > 0){  
	    var _refresh_ifram = refresh_tab.find('iframe')[0];  
	    var refresh_url = cfg.url?cfg.url:_refresh_ifram.src;  
	    _refresh_ifram.contentWindow.location.href=refresh_url;  
    }  
} 
function addTab(title, url){
	if($('#tabs').find('.tabs li').length >= 8){
		warning('最多只能打开8个窗口');
		return;
	}
	if ($('#tabs').tabs('exists', title)){
		$('#tabs').tabs('select', title);// 选中并刷新
		var currTab = $('#tabs').tabs('getSelected');
	} else {
		var content = createFrame(url);
		$('#tabs').tabs('add',{
			title:title,
			content:content,
			closable:true
		});		
	}
	tabClose();
}
function createFrame(url) {
	var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
	return s;
}
		
function tabClose() {
	/*双击关闭TAB选项卡*/
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close',subtitle);
	})
	/*为选项卡绑定右键*/
	$(".tabs-inner").bind('contextmenu',function(e){
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});

		var subtitle =$(this).children(".tabs-closable").text();

		$('#mm').data("currtab",subtitle);
		$('#tabs').tabs('select',subtitle);
		return false;
	});
}		
//绑定右键菜单事件
function tabCloseEven() {
	//刷新
	$('#mm-tabupdate').click(function(){
		var currTab = $('#tabs').tabs('getSelected');
		var url = $(currTab.panel('options').content).attr('src');
		if(url != undefined && currTab.panel('options').title != mTitle) {
			$('#tabs').tabs('update',{
				tab:currTab,
				options:{
					content:createFrame(url),
					closable:true
				}
			})
		}
	})
	//关闭当前
	$('#mm-tabclose').click(function(){
		var currtab_title = $('#mm').data("currtab");
		$('#tabs').tabs('close',currtab_title);
	})
	//全部关闭
	$('#mm-tabcloseall').click(function(){
		$('.tabs-inner span').each(function(i,n){
			var t = $(n).text();
			if(t != mTitle) {
				$('#tabs').tabs('close',t);
			}
		});
	});
	//关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function(){
		var prevall = $('.tabs-selected').prevAll();
		var nextall = $('.tabs-selected').nextAll();		
		if(prevall.length>0){
			prevall.each(function(i,n){
				var t=$('a:eq(0) span',$(n)).text();
				if(t != mTitle) {
					$('#tabs').tabs('close',t);
				}
			});
		}
		if(nextall.length>0) {
			nextall.each(function(i,n){
				var t=$('a:eq(0) span',$(n)).text();
				if(t != mTitle) {
					$('#tabs').tabs('close',t);
				}
			});
		}
		return false;
	});
	//关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function(){
		var nextall = $('.tabs-selected').nextAll();
		if(nextall.length==0){
			//msgShow('系统提示','后边没有啦~~','error');
			//alert('后边没有啦~~');
			return false;
		}
		nextall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});
	//关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function(){
		var prevall = $('.tabs-selected').prevAll();
		if(prevall.length==0){
			//alert('到头了，前边没有啦~~');
			return false;
		}
		prevall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});

	//退出
	$("#mm-exit").click(function(){
		$('#mm').menu('hide');
	})
}