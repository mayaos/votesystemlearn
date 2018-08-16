<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>业务码表管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/modules/common/codedict/styles/codedict.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/codedict/js/codedict.js"></script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" fit="true">
				<div data-options="region:'north'"style="height:35px;text-align:left;padding-top:5px;">
					&nbsp;&nbsp;码值类型：<input name="codeType" id="codeType1" class="easyui-textbox"style="width:100px;height:22px;line-height:22px;">
					 码值类型名称：<input name="codeTypeName" id="codeTypeName1" class="easyui-textbox"style="width:100px;height:22px;line-height:22px;"> 
					 码值描述：<input id="codeName1" name="codeName1"class="easyui-textbox" style="width:100px;height:22px;line-height:22px;">
					 <a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">查询</a>
					  <a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">返回</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="业务码表列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="codeTypeName" width="100" align="center">码值类型名称</th>
								<th field="codeName" width="100" align="center">码值描述</th>
								<th field="codeValue" width="50" align="center">码值</th>
								<th field="codeType" width="100" align="center">码值类型</th>
								<th field="codeOrder" width="100" align="center">码值排序字段</th>
								<th field="codeValidVal" width="150" align="center">是否有效标志</th>
								<th field="codeDate" width="130" align="center">修改时间</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 添加  -->
	<div id="divWindow" style="padding:10px;">
		<form name="addForm" id="addForm" method="post" action="">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="id" id="id" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">码值类型：</td>
					<td align="left"><input type="text" name="codeType" id="codeType"
						size="38" maxlength="10" class="easyui-textbox"
						data-options="required:true" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">码值类型名称：</td>
					<td align="left"><input type="text" name="codeTypeName" id="codeTypeName"
						size="38" maxlength="30" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">码值描述：</td>
					<td align="left"><input type="text" name="codeName" id="codeName"data-options="required:true"
						size="38" maxlength="30" class="easyui-textbox" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">码值：</td>
					<td align="left"><input type="text"name="codeValue" id="codeValue"
						class="easyui-textbox" size="20" maxlength="30" class="easyui-textbox"
						data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">码值排序字段：</td>
					<td align="left"><input id="codeOrder" name="codeOrder" 
						class="easyui-textbox" type="text" size="20" maxlength="30" data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">是否有效标志：</td>
					<td align="left">
						<select name="codeValid" id="codeValid" class="easyui-combobox" style="width:100px;"
						data-options="editable:false,panelHeight:'auto',required:true" >
							<option value="VALID">有效</option>
							<option value="INVALID">无效</option>
					    </select>&nbsp;<font color="red">*</font>
					 </td>
				</tr>
				<tr>
					<td align="right">修改时间：</td>
					<td align="left"><input name="codeDate" id="codeDate"
						style="width:100px" class="easyui-datebox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="center" colspan="2">
						<input type="button"name="btnSave" id="btnSave" class="button1" value="保存" />
						<input type="button" name="btnCancel" id="btnCancel" class="button1"value="取消" />
					</td>
				</tr>
			</table>
		</form>
	</div>

	<!-- 修改  -->
	<div id="divWindow2" style="padding:10px;">
		<form name="addForm2" id="addForm2" method="post" action="">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="id2" id="id2" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">码值类型：</td>
					<td align="left"><input type="text" name="codeType" id="codeType2"
						size="38" maxlength="10" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">码值类型名称：</td>
					<td align="left"><input type="text" name="codeTypeName" id="codeTypeName2"
						size="38" maxlength="30" class="easyui-textbox"data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">码值描述：</td>
					<td align="left"><input type="text" name="codeName" id="codeName2"
						size="38" maxlength="30" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">码值：</td>
					<td align="left"><input  type="text"name="codeValue" id="codeValue2"
						class="easyui-textbox" size="38" maxlength="30" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">码值排序字段：</td>
					<td align="left"><input id="codeOrder2" name="codeOrder"
						class="easyui-textbox" type="text" size="25" maxlength="30" data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">是否有效标志：</td>
					<td align="left">
						<select name="codeValid" id="codeValid2" class="easyui-combobox" style="width:100px;"
						data-options="editable:false,panelHeight:'auto',required:true" >
							<option value="VALID">有效</option>
							<option value="INVALID">无效</option>
					    </select>&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right">修改时间：</td>
					<td align="left"><input  name="codeDate" id="codeDate2"
						style="width:180px" class="easyui-datebox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="center" colspan="2">
					<input type="button" name="btnSave" id="btnSave2" class="button1" value="保存"/>&nbsp;
					<input type="button" name="btnCancel" id="btnCancel2" class="button1" value="取消" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>