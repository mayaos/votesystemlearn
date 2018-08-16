<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>日志管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/styles/global.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/modules/common/log/styles/log.css"/>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/log/js/logBase.js"></script>
<script language="javascript" type="text/javascript">
    var root = "<%=request.getContextPath()%>"; 
</script>
</head>
<body>
	<div class="easyui-layout" fit="true">	    
		<div data-options="region:'north'" style="height:35px;text-align:center;padding-top:5px;">
			操作人：
			<input type="text" name="userId" id="userId" class="easyui-textbox" style="width:100px;" />
			是否成功：
			<select name="isSuccess" id="isSuccess" class="easyui-combobox" data-options="editable:false,cache:false,panelHeight:'auto'" style="width:70px;">
			    <option value="">请选择</option>
				<option value="Y">成功</option>
				<option value="N">失败</option>
			</select>
			日志类型：
			<select name="logType" id="logType" class="easyui-combobox" style="width:100px;" 
				data-options="editable:'false',
            	panelHeight:'auto',
            	valueField:'codeValue',
            	textField:'codeName',
            	url:Common.getUrl('codectrl/createComboBox?codeType=logType')" >
            </select>
			起止时间：
			<input name="happentimeStart" id="happentimeStart" class="easyui-datetimebox" data-options="editable:false" style="width:150px;"/> 
			--
			<input name="happentimeEnd" id="happentimeEnd" class="easyui-datetimebox" data-options="editable:false" style="width:150px;"/>
			<a id="search" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:60px;height:22px;">查询</a>
			<a id="reset" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:60px;height:22px;">重置</a>
		</div>
		
		<div data-options="region:'center',border:false">
			<table id="gridList" class="easyui-datagrid" data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true" title="日志数据" >
				<thead>
					<tr>
						<th field="checked" width="30" data-options="checkbox:true"></th>
						<th field="username" width="100" align="center">操作人</th>
						<th field="logcontent" width="150" align="center">日志描述</th>	
						<th field="logtype" width="100" align="center">日志类型</th>						
						<th field="ip" width="100" align="center">IP</th>
						<th field="issuccess" width="100" align="center">是否成功</th>
						<th field="happentime" width="200" align="center">操作时间</th>
						<th field="requesturl" width="350" align="center">操作URL</th>						
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<form name="logForm" id="logForm" method="post" action="">
	     <input class="easyui-textbox" type="hidden" name="log.username" id="usernameUse"/>
	     <input class="easyui-textbox" type="hidden" name="log.log_type" id="logTypeUse"/>
	     <input class="easyui-textbox" type="hidden" name="log.issuccess" id="issuccessUse"/>
	     <input class="easyui-textbox" type="hidden" name="log.happentimeStart" id="happentimeStartUse"/>
	     <input class="easyui-textbox" type="hidden" name="log.happentimeEnd" id="happentimeEndUse"/>
	</form>
	<div id="divWindow" style="padding:10px;">
		<form name="showForm" id="showForm" method="post" action="">
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right">操作人：</td>
					<td align="left"><span id="userid"></span></td>
				</tr>
				<tr>
					<td align="right">操作URL：</td>
					<td align="left"><span id="requesturl"></span></td>
				</tr>
				<tr>
					<td align="right">IP：</td>
					<td align="left"><span id="ip"></span></td>
				</tr>
				<tr>
					<td align="right">日志类型：</td>
					<td align="left"><span id="logtype"></span></td>
				</tr>
				<tr>
					<td align="right">是否成功：</td>
					<td align="left"><span id="issuccess"></span></td>
				</tr>
				<tr>
					<td align="right">操作时间：</td>
					<td align="left"><span id="happentime"></span></td>
				</tr>
				<tr>
					<td align="right">日志描述：</td>
					<td align="left"><span id="logcontent" style="white-space:normal;"></span></td>
				</tr>
				<tr>
					<td colspan="2"><a id="btnClose" href="javascript:void(0);" class="easyui-linkbutton">&nbsp;关闭&nbsp;</a></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>