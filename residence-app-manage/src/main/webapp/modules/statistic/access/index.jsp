<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户访问轨迹日志</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/modules/statistic/access/styles/access.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/statistic/access/js/access.js"></script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" fit="true">
				<div data-options="region:'north'"style="height:35px;text-align:left;padding-top:5px;">
					&nbsp;&nbsp;用户ID：<input id="citizenId" class="easyui-textbox" name="citizenId" style="width:100px;height:22px;line-height:22px;" />   
					&nbsp;&nbsp;用户账号名：<input id="userName" class="easyui-textbox" name="userName" style="width:100px;height:22px;line-height:22px;" />   
					&nbsp;&nbsp;访问来源：<select id="accessFrom" class="easyui-combobox" name="accessFrom" style="width:100px;" data-options="value:'', panelHeight:'auto',editable:false">   
										    <option value="wechat">wechat</option>   
										    <option value="app">app</option>     									 
									   </select> 				
					&nbsp;&nbsp;请求资源地址：<input name="requestUrl" id="requestUrl" class="easyui-textbox"style="width:200px;height:22px;line-height:22px;"/>
					&nbsp;&nbsp;访问时间段：
					<input name="startTime" id="startTime" class="easyui-datetimebox" data-options="editable:false" style="width:150px;"/> 
					--
					<input name="endTime" id="endTime" class="easyui-datetimebox" data-options="editable:false" style="width:150px;"/>
					&nbsp;&nbsp;<a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">查询</a>
					<a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">返回</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="用户访问轨迹列表"
						data-options="singleSelect:true,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="citizenId" width="100" align="left">用户ID</th>
								<th field="userName" width="150" align="left"">用户账号名</th>
								<th field="accessFrom" width="100" align="left">访问来源</th>
								<th field="requestUrl" width="300" align="left">请求资源地址</th>
								<th field="requestTime" width="150" align="center">访问时间</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>