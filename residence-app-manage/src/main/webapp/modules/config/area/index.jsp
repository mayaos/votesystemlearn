<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>地市列表</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/styles/global.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/modules/config/area//styles/area.css"/>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/config/area/js/area.js"></script>
</head>
<body>
<div class="easyui-layout" fit="true">
	<div data-options="region:'north'"
						style="height:35px;text-align:center;padding-top:5px;">	
		地区名称：<input name="areaNameSearch" id="areaNameSearch" class="easyui-textbox"	style="width:100px;height:22px;line-height:22px;"/>
		&nbsp;<a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"	style="width:60px;height:22px;">查询</a>
		&nbsp;<a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">返回</a>
	</div>
	<div data-options="region:'center',border:false">
		<table id="gridList" class="easyui-datagrid" title="地市列表" data-options="singleSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
			<thead>
				<tr>
					<th field="areaID" width="80" align="center">地区代码</th>
					<th field="areaName" width="100" align="center">地区名称</th>
					<th field="fullName" width="195" align="center">地区全称</th>
					<th field="provinceCode" width="80" align="center">省份代码</th>
					<th field="cityCode" width="100" align="center">地市代码</th>
					<th field="townCode" width="100" align="center">区县代码</th>
				</tr>
			</thead>
		</table>
		<form style="display:none" id="export_form" action="" method="post"></form>
	</div>
</div>

<div id="divWindow" style="padding:10px;">
	<form name="addForm" id="addForm" method="post" action="">
		<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
			<tr>
				<td align="right" width="100">地区代码：</td>
				<td align="left"><input type="text" name="areaId" id="areaId" size="10" class="easyui-textbox" data-options="required:true,validType:'length[6,6]',invalidMessage:'地区代码长度必须是6位。'" />&nbsp;<font color="red">*</font></td>
			</tr>
			<tr>
				<td align="right">地区名称：</td>
				<td align="left"><input type="text" name="areaName" id="areaName" size="30" maxlength="20" class="easyui-textbox" data-options="required:true,invalidMessage:'地区名称不能为空。'"/>&nbsp;<font color="red">*</font></td>
			</tr>
			<tr>
				<td align="right">地区全称：</td>
				<td align="left"><input type="text" name="areaFullName" id="areaFullName" size="30" maxlength="50" class="easyui-textbox" data-options="required:true,invalidMessage:'地区全称不能为空。'"/>&nbsp;<font color="red">*</font></td>
			</tr>
			<tr>
				<td align="center" colspan="2">
					<input type="button" name="btnSave" id="btnSave" class="button1" value="保存" />
					<input type="button" name="btnCancel" id="btnCancel" class="button1" value="取消" />
				</td>
			</tr>
		</table>
	</form>
</div>

</body>
</html>