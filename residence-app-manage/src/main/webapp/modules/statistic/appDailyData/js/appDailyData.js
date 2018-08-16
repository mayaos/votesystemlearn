$(function() {
	// 初始化数据表格，并将对象实例存储到变量
	var grid = $('#gridList').datagrid({
		striped: true,
		dataType: 'json',
		url: Common.getUrl('dailyDataStatisticsctrl/getList'),
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
		var recordDate = $('#recordDate').textbox('getText');
		// 使用参数查询
		grid.datagrid('load', {
			'recordDate' : recordDate		
		});
	});
	
	$('#return').click(function() {	
		
		$('#recordDate').textbox('setValue','');
		
		grid.datagrid('load', {
			'recordDate' : ''
		});
	});	
});

