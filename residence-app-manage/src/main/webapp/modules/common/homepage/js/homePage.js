// JavaScript Document
$(function(){
//	-------------此处可根据首页需求进行配置 start-------------------------
	// sys_menu表中 批次审核 菜单的ID 与父ID
	var menuidParam1 = "'44','4'";
	// sys_menu表中 提交申请 菜单的ID 与父ID
	var menuidParam2 = "'43','4'";
	// sys_menu表中 订单合成 菜单的ID 与父ID
	var menuidParam3 = "'46','4'";
//	-------------此处可根据首页需求进行配置  end -------------------------
//	// 查看待办事项的数量
//		$.ajax({
//			timeout	: 15000,
//			async	: true,
//			cache	: false,
//			type	: 'POST',
//			url		: 'untreated-selectUntreatedCount.action',
//			dataType: 'json',
//			success	: function(res) {
//				var html= new Array();	
//				if(res.showType=='1'){
//					html.push('待办事项 >><br/><br/>');
////					省厅级用户权限；
//					html.push('<span ><img src="images/do_matter_image.jpg"/> 目前有 <a href="javascript:void(0);" onclick="linkToDetailPage('+menuidParam1+')" >&nbsp;'+res.untreatedCount+'&nbsp;</a>个批次信息待审核</span>');
//				}else if(res.showType=='2'){
//					html.push('待办事项 >><br/><br/>');
////					区县级用户权限
//					html.push('<span ><img src="images/do_matter_image.jpg"/> 目前有 <a href="javascript:void(0);" onclick="linkToDetailPage('+menuidParam2+')" >&nbsp;'+res.unpassCount+'&nbsp;</a>个审核未通过批次信息</span>');
//					html.push('<br>');
//					html.push('<span ><img src="images/do_matter_image.jpg"/> 目前有 <a href="javascript:void(0);" onclick="linkToDetailPage('+menuidParam3+')" >&nbsp;'+res.passCount+'&nbsp;</a>个审核已通过批次信息</span>');
//				}else if(res.showType=='3'){
////					超级管理员权限;同时有省厅用户权限和区县用户权限
//					html.push('待办事项 >><br/><br/>');
//					html.push('<span ><img src="images/do_matter_image.jpg"/> 目前有 <a href="javascript:void(0);" onclick="linkToDetailPage('+menuidParam1+')" >&nbsp;'+res.untreatedCount+'&nbsp;</a>个批次信息待审核</span>');
//					html.push('<br>');
//					html.push('<span ><img src="images/do_matter_image.jpg"/> 目前有 <a href="javascript:void(0);" onclick="linkToDetailPage('+menuidParam2+')" >&nbsp;'+res.unpassCount+'&nbsp;</a>个审核未通过批次信息</span>');
//					html.push('<br>');
//					html.push('<span ><img src="images/do_matter_image.jpg"/> 目前有 <a href="javascript:void(0);" onclick="linkToDetailPage('+menuidParam3+')" >&nbsp;'+res.passCount+'&nbsp;</a>个审核已通过批次信息</span>');
//				}else {
////					其他权限
//					html.push('尊敬的用户，欢迎登录本系统 。');
//				}
//				$('.schedule_title').html(html.join(""));
//			},
//			error: function(data) {
//				Common.alert('请求服务器失败，操作未能完成！');
//			}
//		});
});

/**
 * 根据传入菜单ID和父ID打开相应页面
 * @param menuId 菜单ID,与sys_menu表中menu_id一致
 * @param mfaId 菜单父ID,与sys_menu表中menu_fatherid一致
 * @return
 */
function linkToDetailPage(menuId,mfaId){
	var $ul = parent.window.$("#"+mfaId).next('ul');
	if(!$ul.is(':visible')) {
		parent.window.$("#"+mfaId).trigger("click");
	}
	parent.window.$("#"+menuId).trigger("click");
	parent.window.$("#"+menuId).find('a').trigger("click");
}