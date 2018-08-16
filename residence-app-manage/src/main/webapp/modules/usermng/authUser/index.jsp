<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String path = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户身份证认证管理 </title>
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
<script language="javascript" type="text/javascript" src="<%=path%>/modules/usermng/authUser/js/base.js"></script>
<script language="javascript" type="text/javascript">
    var root = "<%=path%>";
</script>
</head>
<body>
   <div class="easyui-layout" fit="true">	    
		<div data-options="region:'north'" style="height:45px;text-align:center;padding-top:10px;">
			用户账号：
			<input type="text" id="userName" name="userName" class="easyui-textbox"/>
<!-- 			&nbsp;选择认证类型： -->
<!-- 			<select class="easyui-combobox" id="authType" name="authType" data-options="editable:false,panelHeight:'auto'" style="width:80px;"> -->
<!-- 				<option value=""></option> -->
<!-- 				<option value="1">身份证</option> -->
<!-- 				<option value="2">居住证</option> -->
<!--         	</select> -->
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
			<table id="gridList" class="easyui-datagrid"  title="用户身份证认证信息" 
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
	
	<div id="userAuthWindow" style="padding:10px;">
		<div>
			注意：从公安局导过来的最新数据是<span name="maxImportDate" style="color:red"></span> 
			，所以身份认证能够认证到最后的日期为<span name="maxImportDate" style="color:red"></span>
		</div></br>
		<form name="rcAuthForm" id="rcAuthForm" method="post" action="">
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
			    <tr>
			    	<td align="right" width="100">选择认证方式：</td>
					<td align="left">
						<input type="radio" id="authWay" name="authWay" value="1" checked/> 全部认证
						<input type="radio" id="authWay" name="authWay" value="2" /> 自定义
					</td>
			    </tr>
				<tr>
					<td align="right" width="120">认证起止时间：</td>
					<td align="left">
						<input name="authStartTime" id="authStartTime" class="easyui-datetimebox" disabled="disabled"  style="width:150px;"/> 
						--
						<input name="authEndTime" id="authEndTime" class="easyui-datetimebox" disabled="disabled"  style="width:150px;"/>
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
