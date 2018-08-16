<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String path = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户认证管理 </title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/styles/global.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/modules/usermng/auth/styles/base.css"/>
<script language="javascript" type="text/javascript" src="<%=path%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/modules/usermng/auth/js/base.js"></script>
<script language="javascript" type="text/javascript">
    var root = "<%=path%>";
</script>
</head>
<body>
   <div class="easyui-layout" fit="true">	    
		<div data-options="region:'north'" style="height:45px;text-align:center;padding-top:10px;">
			用户账号：
			<input type="text" id="userName" name="userName" class="easyui-textbox"/>
			&nbsp;选择认证类型：
			<select class="easyui-combobox" id="authType" name="authType" data-options="editable:false,panelHeight:'auto'" style="width:80px;">
				<option value=""></option>
				<option value="1">身份证</option>
				<option value="2">居住证</option>
        	</select>
        	&nbsp;选择认证状态：
			<select class="easyui-combobox" id="authStatus" name="authStatus" style="width:80px;"
				data-options="editable:false,
            		panelHeight:'auto',
                    valueField:'codeValue',
                    textField:'codeName',
                    url:Common.getUrl('codectrl/createComboBox?codeType=authStatus') ">
			</select>
			认证起止时间：
			<input name="startTime" id="startTime" class="easyui-datetimebox" data-options="editable:false" style="width:150px;"/> 
			--
			<input name="endTime" id="endTime" class="easyui-datetimebox" data-options="editable:false" style="width:150px;"/>
			<a id="search" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:60px;height:22px;">查询</a>
			<a id="reset" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:60px;height:22px;">重置</a>
		</div>
		
		<div data-options="region:'center',border:false">
			<table id="gridList" class="easyui-datagrid"  title="用户认证信息" 
			data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
				<thead>
					<tr>
						<th field="checked" width="30" data-options="checkbox:true"></th>
						<th field="authID" width="80" align="center" hidden="true">用户id</th>
						<th field="userName" width="100" align="center" >用户账号</th>
						<th field="citizenName" width="100" align="center">姓名</th>
						<th field="idCard" width="160" align="center" >身份证号</th>
						<th field="rcCard" width="160" align="center" >居住证卡号</th>
						<th field="authType" width="100" align="center">认证类型</th>
						<th field="authTime" width="125" align="center">认证时间</th>
						<th field="authStatus" width="60" align="center">认证状态</th>
						<th field="authResult" width="80" align="center">认证结果</th>
						<th field="authResultMessage" width="180" align="center">认证结果备注</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	
	<div id="exportWindow" style="padding:10px;">
		<span align="left" class="tips">“认证导出”：请结合搜索栏的查询条件限制导出</span>
		<form name="exportForm" id="exportForm" method="post" action="">
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="120">导出类别：</td>
					<td align="left">
						<label><input type="radio" name="exportType" value="1" checked/>认证导出</label>&nbsp;&nbsp;
						<label><input type="radio" name="exportType" value="2"/>普通导出</label>
					</td>
				</tr>
				<tr>
					<td align="right">用户账号：</td>
					<td align="left" id="userName2"></td>
				</tr>
				<tr>
					<td align="right" width="50">认证类型：</td>
					<td align="left" id="authType2"></td>
				</tr>
				<tr>
					<td align="right">认证状态：</td>
					<td align="left" id="authStatus2"></td>
				</tr>
				<tr>
					<td align="right">起止时间：</td>
					<td align="left" id="authTime2"></td>
				</tr>
				<tr>
					<td align="right">导出数据数量：</td>
					<td align="left" id="count"></td>
				</tr>
				<tr>
					<td colspan="2" height="40px"><a id="btnExport" href="javascript:void(0);" class="easyui-linkbutton">&nbsp;确认导出&nbsp;</a></td>
				</tr>
			</table>
		</form>
	</div>
	
	<div id="importWindow" style="padding:10px;">
		<form name="importForm" id="importForm" method="post" action="">
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="120">导入认证结果文件：</td>
					<td align="left">
						<input class="easyui-filebox" name="authFile" id="authFile" data-options="buttonText:'选择文件',buttonAlign:'left'" style="width:280px;"/>&nbsp;&nbsp;<font style="color:red"></font>
					</td>
				</tr>
				<tr>
					<td colspan="2" height="80px" align="left">
						身份证认证结果导入说明：<br/>
						1. 导入文件：导出功能中‘认证导出’的文件,如《身份证认证用户数据-2-20170205160150.xls》<br/>
						2. 导入文件中的“认证结果(返回)”、“结果备注(返回)”不为空<br/>
					</td>
				</tr>
				<tr>
					<td colspan="2" height="40px"><a id="btnImport" href="javascript:void(0);" class="easyui-linkbutton">&nbsp;确认导入&nbsp;</a></td>
				</tr>
			</table>
		</form>
		<div align="left" id="tips" class="tips"></div>
		<div id="errorMsg" style="height:100px;">
			<table id="errorList" class="easyui-datagrid"  title="错误用户信息列表(显10条)" 
			data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
				<thead>
					<tr>
						<th field="userName" width="120" align="center" >用户账号</th>
						<th field="citizenName" width="100" align="center">姓名</th>
						<th field="idCard" width="160" align="center" >身份证号</th>
						<th field="authTime" width="140" align="center" >认证时间</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	
	<div id="rcAuthWindow" style="padding:10px;">
		<form name="rcAuthForm" id="rcAuthForm" method="post" action="">
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="120">认证起止时间：</td>
					<td align="left">
						<input name="authStartTime" id="authStartTime" class="easyui-datetimebox" data-options="editable:false" style="width:150px;"/> 
						--
						<input name="authEndTime" id="authEndTime" class="easyui-datetimebox" data-options="editable:false" style="width:150px;"/>
					</td>
				</tr>
				<tr>
					<td colspan="2" height="40px"><a id="btnRcAuth" href="javascript:void(0);" class="easyui-linkbutton">&nbsp;提交认证&nbsp;</a></td>
				</tr>
			</table>
		</form>
	</div>	
</body>
</html>
