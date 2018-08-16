function loadNavDiv(thiz){
	$.each($('[name=li_a]'), function(i, value) {
		$(this).css('color','black');
	});
	$(thiz).css('color','blue');
	var navDiv = $('div[id^=nav_]');
	var id = thiz?$(thiz).attr('val'):$($("#getfirst").find("ul li a").first()).attr('val'); 
	//var id = thiz?$(thiz).attr('val'):"0083"; 
	mm=$(thiz).attr('val');
	var nav_div = userAuth[id]['children'].length > 0?'nav_model1':'nav_'+id;
	$('#'+nav_div).show();
	$.each(navDiv, function(i, tr) {
		if ($(this).attr('id') != nav_div) {
			$(this).hide();
		}
	});
	if(userAuth[id]['children'].length > 0){
		$.lhgDiv({
			href : ht.ROOT+'fpps/nav/n_base.html',
			divId : nav_div,
			init : function() {
				
				/*
				if(id == '0082'){
//					tabsCloseAll(1);
//					addTab('发卡', ht.ROOT+userAuth[id]['children'][0].attributes.funUrl, false);
					addTab('发卡', ht.ROOT+userAuth[id]['children'][0].attributes.funUrl);
				}else {
					if (!$('#tabs').tabs('exists', mTitle)){
						$('#tabs').tabs('add',{
							title: '首页',
							content: '<div id="baseMain" style="width:100%;height:100%;" ></div>',
							closable: false
						});
						$('#baseMain').css('background','url("'+ht.ROOT+'public/login/images/welcome.jpg") transparent 10% center no-repeat');
					}
//					tabsCloseAll();
				}
				*/
				var str='<div class="commlist" >'+
							'<h3>通知公告</h3>'+
  							'<p>本月25号系统进行升级，如有不便，请联系系统管理员。</p>'+
						  '<ul>'+
						    '<h3>待办事项</h3>';
				$.ajax({  
				    type: 'post',  
				    url: 'fpps_gd!countGdByType.action',  
				    dataType: 'text', 
				    async: false, 
				    success: function(data){  
			        var jsonObj=eval("("+data+")");  
			        $.each(jsonObj, function (i, item) { 
			        	if(item.gtype=='1'){
				           str+='<li class="n1"><span>巡检工单</span> <a href="#" onclick=addTab("工单跟踪","fpps/gz/gz.jsp","007528") ><strong>'+item.gsum+'</strong> 条</a></li>';
			            }
			            if(item.gtype=='2'){
				           str+='<li class="n1"><span>故障工单</span> <a href="#" onclick=addTab("工单跟踪","fpps/gz/gz.jsp","007528") ><strong>'+item.gsum+'</strong> 条</a></li>';
			            }
			        }); 
				    }
			    });  
				 str+= '</ul>'+
						'</div>'+
						'<div class="entrylist">'+
						  '<ul>'+
						    '<li class="entry01" onclick=addTab("发卡","fpps/work/fk.jsp","008229")>发卡</li>'+
						    '<li class="entry02" onclick=addTab("补卡","fpps/work/html/kb.jsp","008290")>补卡</li>'+
						    '<li class="entry03" onclick=addTab("续卡","fpps/work/html/kx.jsp","008284")>续卡</li>'+
						    '<li class="entry04" onclick=addTab("换租","fpps/work/html/kh.jsp","008253")>换租</li>'+
						    '<li class="entry05" onclick=addTab("退卡","fpps/work/html/kt.jsp","008205")>退卡</li>'+
						    '<li class="entry06" onclick=addTab("门锁权限管理","fpps/deviceqx/deviceqx.jsp","008500")>门锁权限管理</li>'+
						    '<li class="entry07" onclick=addTab("出入卡管理","search/crk/crk.jsp","008541")>出入卡管理</li>'+
						    '<li class="entry08" onclick=addTab("工单跟踪","fpps/gz/gz.jsp","007528")>工单跟踪</li>'+
						  '</ul>'+
						'</div>';
					if (!$('#tabs').tabs('exists', mTitle)){
						
						$('#tabs').tabs('add',{
							title: '首页',
							content: '<div id="baseMain" style="width:100%;height:100%;overflow:hidden;" >'+str+'</div>',
							closable: false
						});
						$('#baseMain').css('background','url("'+ht.ROOT+'public/login/images/bg_dbsx.gif") #eef4ff  no-repeat  right bottom');
						
					}
				loadMenuData('menuTree', id);
				//$('#menuTree').tree('loadData',userAuth[id]['children']);
				$('#_west').panel('setTitle',userAuth[id].text);
			}
		});
	}else{
		$.lhgDiv({
			href : ht.ROOT+userAuth[id].attributes.funUrl,
			divId : nav_div,
			init : function() {
			}
		});
	}
}
function loadMenuData(menuId, navId){
	$('#'+menuId).html('');
	var arrs = [];
	var _children = userAuth[navId]['children'];
	for(var ii=0; ii<_children.length; ii++){
		var node = _children[ii].attributes;
		var _url = ht.ROOT + node.funUrl;
//		arrs.push('<li class="'+node.funCsscode+'" style="cursor:hand;" onclick="openTab(this, \''+menuId+'\', \''+node.funName+'\',\''+_url+'\')">'+node.funName+'</li>');
		arrs.push('<li class="'+node.funCsscode+'" style="cursor:hand;" onclick="openTab(this, \''+node.funCode+'\', \''+menuId+'\', \''+node.funName+'\',\''+_url+'\')">'+node.funName+'</li>');
	}
	$('#'+menuId).html(arrs.join(''));
}

function openTab(thiz, code, menuId, title, url){
	$('li', $('#'+menuId)).each(function(){
		$(this).css('color','black');
	});
	$(thiz).css('color','blue');
	addTab(title, url, code);
}

function  doLogout(){	//注销
  	window.self.location=ht.ROOT+'login!logOut.action';
}
function updPwd(value){
	$.lhgWindow({
		href : 'home/pwd_update.jsp',
		winId : 'pwd_dialog',
		winHeight:'250',
		title : '修改密码',
		button : [{
					name : '保存',
					callback : function() {
						if(savepwd()){
							return true;
						}else{
							return false;
						}
					},
					focus : true
		}],
		init : function() {
			if(value==1){
			$('#msg_pass').show();
			}else{
			$('#msg_pass').hide();
			}
			$('#f1')[0].reset();
			$('#p').hide();
			$('#p1').hide();
			$('#p2').hide();
		}
	});
}
