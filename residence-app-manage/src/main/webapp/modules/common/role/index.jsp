<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>角色管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />


<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/modules/common/role/styles/role.css" />
<script language="javascript" type="text/javascript"src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript"src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript"src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript"src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript"src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript"src="<%=request.getContextPath()%>/modules/common/role/js/role.js"></script>
</head>
<body>
	<!-- 角色列表 -->
	<div class="easyui-layout" fit="true">
		<div data-options="region:'north'"
			style="height:35px;text-align:center;padding-top:5px;">
			角色名称： 
			<input name="rolename" id="rolename" class="easyui-textbox" style="width:200px;height:22px;line-height:22px;"> 
				<a id="search" href="javascript:void(0);" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" style="width:60px;height:22px;">查询</a>
		</div>
		<div data-options="region:'center',border:false">
			<table id="gridList" class="easyui-datagrid"
				data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true" title="角色明细">
				<thead>
					<tr>
						<th field="checked" width="30" data-options="checkbox:true"></th>
						<th field="role_id" width="80" align="center" hidden="true">角色id</th>
						<th field="role_name" width="120" align="center">角色名称</th>
						<th field="create_date" width="100" align="center">修改时间</th>
						<th field="isdefault" width="60" align="center">是否默认</th>
						<th field="role_desc" width="420" align="left">描述</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>

	<!-- 添加修改 -->
	<div id="divWindow" style="padding:10px;">
		<form name="addForm" id="addForm" method="post" action="">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="role_id" id="role_id" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%"
				class="tab">
				<tr>
					<td align="right" width="100">角色名称：</td>
					<td align="left"><input class="easyui-textbox" type="text"
						name="role_name" id="role_name" data-options="required:true,validType:'length[1,20]'" 
						size="32" maxlength="20" />&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right">是否是默认用户：</td>
					<td align="left">
						<label><input type="radio" name="isdefault" value="YES" checked/> 是</label>&nbsp;&nbsp;
						<label><input type="radio" name="isdefault" value="NO"/> 否 </label>
					</td>
				</tr>
				<tr>
					<td align="right">描述：</td>
					<td align="left">
						<textarea name="role_desc" id="role_desc" rows="5" cols="60" maxlength="255" style="white-space:normal;" class="textarea"></textarea>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="2">
						<input type="button" name="btnSave" id="btnSave" class="button1" value="保存" />&nbsp;
						<input type="button" name="btnCancel" id="btnCancel" class="button1" value="取消" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	<!-- 菜单分配功能 -->
	<div id="divWindow3" class="easyui-layout">
		<div id="divTree" style="height:380px;overflow-y:auto;"></div>
		<div style="height:30px;text-align:center;padding:5px;">
			<a id="saveSettings" href="javascript:void(0);"
				class="easyui-linkbutton" data-options="iconCls:'icon-ok'">保存设置</a>
		</div>
	</div>
	
	<!-- 按钮分配功能 -->
	<div id="divWindow4" class="easyui-layout">
		<div id="divbutton" style="height:380px;overflow-y:auto;"></div>
		<div style="height:30px;text-align:center;padding:5px;">
			<a id="savebtton" href="javascript:void(0);"
				class="easyui-linkbutton" data-options="iconCls:'icon-ok'">保存设置</a>
		</div>
	</div>

</body>
</html>