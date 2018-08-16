$(function() {
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('accessctrl/getList'),
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
	
	// 设置分页控件显示汉字
	grid.datagrid('getPager').pagination({
		beforePageText : Common.beforePageText,
		afterPageText : Common.afterPageText,
		displayMsg : Common.displayMsg
	});


	// 查询用户信息
	$('#search').click(function() {
		var citizenId = $('#citizenId').textbox('getText');
		var userName = $('#userName').textbox('getText');
		var requestUrl = $('#requestUrl').textbox('getText');
		var accessFrom = $('#accessFrom').combobox('getValue');
		var startTime = $('#startTime').textbox('getText');
		var endTime = $('#endTime').textbox('getText');
		
		if(citizenId.length>12) {
			Common.alert('用户ID过长，请输入长度为12以内字符！');
			return;
		}
		
		if(userName.length>32) {
			Common.alert('用户账号名过长，请输入长度为32以内字符！');
			return;
		}
		
		if(requestUrl.length>100) {
			Common.alert('请求资源地址过长，请输入长度为100以内字符！');
			return;
		}
		
		if(startTime!='' && endTime=='') {
			Common.alert('请选择截止时间！');
			return;
		}
		if(endTime!='' && startTime=='') {
			Common.alert('请选择开始时间！');
			return;
		}
		var startTime2 = new Date(startTime.replace(/-/gm,"/"));
		var endTime2 = new Date(endTime.replace(/-/gm,"/"));
		if (Number(endTime2 - startTime2) < 0) {
			Common.alert('截止时间必须大于开始时间！');
			return;
		}
		
		// 使用参数查询
		grid.datagrid('load', {
			'citizenId' : citizenId,
			'userName' : userName,
			'requestUrl' : requestUrl,
			'accessFrom' : accessFrom,
			'startTime' : startTime,
			'endTime' : endTime
		});
	});
	
	$('#return').click(function() {	
		 $('#citizenId').textbox('setValue','');
		 $('#userName').textbox('setValue','');
		 $('#requestUrl').textbox('setValue','');
		 $('#accessFrom').combobox('setValue','');
		 $('#startTime').textbox('setValue','');
		 $('#endTime').textbox('setValue','');
		
		grid.datagrid('load', {
			'citizenId' : '',
			'userName' : '',
			'requestUrl' : '',
			'accessFrom' : '',
			'startTime' : '',
			'endTime' : ''
		});
	});	
});

