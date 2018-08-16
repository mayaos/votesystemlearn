/*
 * JavaScript for Redisence Permit system
 * Copyright(c) 2015-2016,  qinyong, eastcompeace
 */
//javascript:self.resizeTo(1024,768);
$(function(){
	$('#help').click(function(){
		//top.location.href = '../help/help.html';
		//window.open('../help/help.html'); 
	});
	
	// 退出登录
	$('a#logout').click(function() {
		//用到了easyui确认框语法参考API文档
		Common.confirm("确定要退出系统吗？",function(r){
			if(r){
				$.ajax({
					timeout	: 15000,
					async	: true,
					cache	: false,
					type	: 'POST',
					url		: Common.getUrl('userctrl/logout'),
					dataType: 'json',
					success	: function(data) {
						window.location.href = 'login.html';
					}
				});
			}				
		});		
	});
	
	$('#newPwd').textbox('textbox').keyup(function(){
		var nPwd=$('#newPwd').textbox('textbox').val();
		ps.update(nPwd);
	})
	
	// 页面右上角显示当前登录用户和消息条数
	$('#date').html(new Date().format('yyyy年MM月dd日'));
	loadUserInfo.call();
	function loadUserInfo() {
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: Common.getUrl('userctrl/loginfo'),
			dataType: 'json',
			data	: null,
			success	: function(data) {
				if (data.result == 0) {
					$('#userName').html(data.userName);
					if(data.userName == null || data.userName =='')
						$('#userName').html(data.userCode);
				} else {
					Common.alert('登录超时，请返回登录页面重新登录！', function() {
						top.location.href = 'login.html';
					});
				}
			}
		});
	}
	
	
	
	// 初始化
	bindTabEvent();
	bindTabCloseEvent();
	
	// 给tab绑定双击事件和右键菜单
	function bindTabEvent() {
		// 双击关闭tab选项卡
		$('.tabs-inner').dblclick(function(){
			var t = $(this).children("span").text();
			
			if ($(this).next().is('.tabs-close')) {
				$('div#tabs').tabs('close', t);
			}
		});
		
		// 为选项卡绑定右键
		$('.tabs-inner').bind('contextmenu', function(e){
			var t = $(this).children(".tabs-title").text();
			// 首页不弹出菜单
			if (t != '首页') {
				$('div#mm').menu('show', {left: e.pageX, top: e.pageY});
				$('div#mm').data("currtab", t);
				$('div#tabs').tabs('select', t);
			}
			return false;
		});
	};
	
	// 绑定右键菜单事件
	function bindTabCloseEvent() {
		// 刷新
		$('div#mm-tabupdate').click(function(){
			var currTab = $('div#tabs').tabs('getSelected');
			var url = $(currTab.panel('options').content).attr('src');
			
			currTab.find('iframe').get(0).contentWindow.location.href = url;
		});
		
		// 关闭当前
		$('div#mm-tabclose').click(function(){
			var currTabTitle = $('div#mm').data("currtab");
			$('div#tabs').tabs('close', currTabTitle);
		});
		
		// 全部关闭
		$('div#mm-tabcloseall').click(function(){
			$('.tabs-inner span').each(function(i,n){
				var t = $(n).text();
				if (t != '首页') $('div#tabs').tabs('close',t);
			});
		});
		
		// 关闭除当前之外的tab
		$('div#mm-tabcloseother').click(function(){
			var prevall = $('.tabs-selected').prevAll();
			var nextall = $('.tabs-selected').nextAll();
			
			if (prevall.length>0) {
				prevall.each(function(i,n){
					var t = $('a:eq(0) span',$(n)).text();
					if(t != '首页') $('div#tabs').tabs('close',t);
				});
			}
			
			if(nextall.length>0) {
				nextall.each(function(i,n){
					var t = $('a:eq(0) span',$(n)).text();
					if(t != '首页') $('div#tabs').tabs('close',t);
				});
			}
			return false;
		});
		
		// 关闭当前右侧的tab
		$('div#mm-tabcloseright').click(function(){
			var nextall = $('.tabs-selected').nextAll();
			if(nextall.length==0) return false;
			
			nextall.each(function(i,n){
				var t=$('a:eq(0) span',$(n)).text();
				$('div#tabs').tabs('close',t);
			});
			return false;
		});
		
		// 关闭当前左侧的tab
		$('div#mm-tabcloseleft').click(function(){
			var prevall = $('.tabs-selected').prevAll();
			if(prevall.length==0) return false;
			
			prevall.each(function(i,n){
				var t=$('a:eq(0) span',$(n)).text();
				$('div#tabs').tabs('close',t);
			});
			return false;
		});
	}
	
	
	// 动态加载菜单并绑定事件
	bindMenuEvent.call();
	loadMenu.call();
	function loadMenu() {
		$.ajax({
			timeout	: 15000,
			async	: true,
			cache	: false,
			type	: 'POST',
			url		: Common.getUrl('menuctrl/menulist'),
			dataType: 'json',
			data	: null,
			beforeSend: function() {
				$('dl#menu').html('<dd>&nbsp;<img src="images/spinner.gif" border="0" align="absmiddle" />&nbsp;菜单加载中...</dd>');
			},
			success	: function(data) {
				if (data.result == 0) {
					var menuList = data.menuList;
					var strHTML = '';
					
					for (var i in menuList) {
						var menu = menuList[i];
						strHTML += '<dd>';
						strHTML += '	<div class="title" id="'+menu.id+'"><em class="' + menu.iconCls + '"></em>' + menu.text + '</div>';
						if (menu.children && menu.children.length > 0) {
							strHTML += '		<ul class="menuson">';
							for (j in menu.children) {
								var child = menu.children[j];
//								var chidUrl = Common.getUrl(child.url);
								strHTML += '			<li id="'+child.id+'"><cite></cite><a url="' + Common.getUrl(child.url) + '">' + child.text + '</a><i></i></li>';
							}
							strHTML += '		</ul>';
						}
						strHTML += '</dd>';
					}
					
					$('dl#menu').html(strHTML);
					bindMenuEvent();
				} else {
					$('dl#menu').html('<dd>&nbsp;' + data.message + '</dd>');
				}
			},
			error: function(errdata) {
				if(errdata.message.indexOf("登录超时") >= 0 )  {
					window.location.href = Common.getUrl('resources/timeout.html');  
				} else {
					$('dl#menu').html('<dd>&nbsp;菜单加载失败...</dd>');
				}
				
			}
		});
	}
	
	// 左侧菜单导航切换
	function bindMenuEvent() {
		$(".menuson li").click(function() {
			$(".menuson li.active").removeClass("active");
			$(this).addClass("active");
		});
		$('.title').click(function(){
			var $ul = $(this).next('ul');
			$('dd').find('ul').slideUp();
			
			if($ul.is(':visible')) {
				$(this).next('ul').slideUp();
			} else {
				$(this).next('ul').slideDown();
			}
		});
		// 左侧菜单点击事件
		$('.menu').find('a').click(function(){
			var url = $(this).attr('url');
			var value = $(this).text();
			
			if ($('div#tabs').tabs('exists', value)) {    
				$('div#tabs').tabs('select', value);
			} else {
				if($('div#tabs').find('.tabs li').length > 10){
					Common.warning('根据系统优化建议，当前最多只能打开10个窗口！');
					return;
				} else {
					var content = '<iframe src="' + url + '" width="100%" marginwidth="0" height="100%" marginheight="0" align="middle" scrolling="yes" frameborder="0"></iframe>';
					
					$('div#tabs').tabs('add', {title: value, content:content, closable:true});
					bindTabEvent();
				}
			}
		});
	}
	
});