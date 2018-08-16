<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>App软件管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/modules/config/software/styles/software.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/config/software/js/software.js"></script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" fit="true">
				<div data-options="region:'north'"
					style="height:35px;text-align:center;padding-top:5px;">
					软件版本：<input name="softwareVersion" id="softwareVersion" class="easyui-textbox"	style="width:100px;height:22px;line-height:22px;"/>
					&nbsp;上传日期：<input id="uploadDateStart" name="uploadDateStart" class="easyui-datebox" data-options="editable:false" style="width:150px"/>
					--&nbsp;<input id="uploadDateEnd"	name="uploadDateEnd" class="easyui-datebox"	data-options="editable:false" style="width:150px">
					&nbsp;<a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"	style="width:60px;height:22px;">查询</a>
					&nbsp;<a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">返回</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="App更新列表"
						data-options="singleSelect:true,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="softId" width="100" align="center" hidden="true">软件ID</th>
								<th field="softName" width="150" align="center">软件名称</th>
								<th field="softVersion" width="100" align="center">软件版本</th>							
								<th field="softType" width="100" align="center">软件类型</th>
								<th field="softSize" width="100" align="center">软件大小</th>
								<th field="softURL" width="200" align="center" hidden="true">软件URL</th>
								<th field="forceUpgrade" width="100" align="center">强制升级</th>
								<th field="createTime" width="150" align="center">上传日期</th>								
								<th field="softNotes" width="300" align="center">更新描述</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 添加  -->
	<div id="divWindow" style="padding:10px;">
		<form name="addForm" id="addForm" method="post" action="" enctype="multipart/form-data">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="id" id="id" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%"
				class="tab">
				<tr>
					<td align="right" width="100">软件类型：</td>
					<td align="left">
						<select  name="softType"  id="softType" class="easyui-combobox" style="width:100px;" panelHeight="auto" data-options="required:true,invalidMessage:'软件类型不能为空。',editable:false">   
						    <option value="android">android</option>   
						    <option value="ios">ios</option>   
						</select>
						&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">强制升级：</td>
					<td align="left">
						<select  name="forceUpgrade"  id="forceUpgrade" class="easyui-combobox" style="width:100px;" panelHeight="auto"  data-options="required:true,invalidMessage:'强制升级不能为空。',editable:false">   
						    <option value="否">否</option>   
						    <option value="是">是</option>   
						</select>
						&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">软件版本：</td>
					<td align="left"><input type="text" name="softVersion" id="softVersion"	size="38" maxlength="30" 
					class="easyui-textbox" data-options="required:true,invalidMessage:'软件版本不能为空。'"/>
					&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">上传安装包：</td>
					<td align="left">
						<input class="easyui-filebox" name="file1" id="file1" size="38" maxlength="10" data-options="prompt:'请选择一个apk文件...',required:true,invalidMessage:'上传安装包不能为空。'" buttonText="浏览" />
						&nbsp;<font color="red">*</font></td>
					</td>
				</tr>
				<tr>
					<td align="right">更新描述：</td>
					<td align="left"><textarea name="softNotes" id="softNotes"
							rows="5" cols="32" class="textarea"></textarea></td>
				</tr>
				<tr>
					<td align="center" colspan="2"><input type="button"	name="btnSave" id="btnSave" class="button1" value="保存" />&nbsp; 
						<input type="button" name="btnCancel" id="btnCancel" class="button1" value="取消" /></td>
				</tr>
			</table>
		</form>
	</div>

	<!-- 修改 -->
	<div id="divWindow1" style="padding:10px;">
		<form name="addForm" id="addForm" method="post" action="">
			<div style="display: none">
				<tr>
					<td align="left"><input type="text" name="softId1" id="softId1"	size="38" maxlength="30" 
					class="easyui-textbox" data-options="required:true"/>
					&nbsp;<font color="red">*</font></td>
				</tr>
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%"
				class="tab">
				<tr>
					<td align="right" width="100">软件类型：</td>
					<td align="left">
						<select  name="softType1"  id="softType1" class="easyui-combobox"  style="width:100px;" panelHeight="auto"  data-options="required:true,invalidMessage:'软件类型不能为空。',editable:false">   
						    <option value="android">android</option>   
						    <option value="ios">ios</option>   
						</select>
						&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">强制升级：</td>
					<td align="left">
						<select  name="forceUpgrade1"  id="forceUpgrade1" class="easyui-combobox" style="width:100px;" panelHeight="auto"  data-options="required:true,invalidMessage:'强制升级不能为空。',editable:false">   
						    <option value="否">否</option>   
						    <option value="是">是</option>   
						</select>
						&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">软件版本：</td>
					<td align="left"><input type="text" name="softVersion1" id="softVersion1"	size="38" maxlength="30" 
					class="easyui-textbox" data-options="required:true,invalidMessage:'软件版本不能为空。'"/>
					&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">更新描述：</td>
					<td align="left"><textarea name="softNotes1" id="softNotes1"
							rows="5" cols="32" class="textarea"></textarea></td>
				</tr>
				<tr>
					<td align="center" colspan="2"><input type="button"	name="btnSave1" id="btnSave1" class="button1" value="保存" />&nbsp; 
						<input type="button" name="btnCancel1" id="btnCancel1" class="button1" value="取消" /></td>
				</tr>
			</table>
		</form>
	</div>	
</body>
</html>