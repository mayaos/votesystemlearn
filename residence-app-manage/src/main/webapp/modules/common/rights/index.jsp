<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>权益菜单信息管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/modules/common/rights/styles/rights.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/rights/js/rights.js"></script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" fit="true">
				<div data-options="region:'north'"style="height:35px;text-align:center;padding-top:5px;">
					权益名称：<input name="rightsName" id="rightsName1" class="easyui-textbox"style="width:100px;height:22px;line-height:22px;"/>
					
					 <a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">查询</a>
					  <a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">返回</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="权益信息表列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="rightsName" width="100" align="center">权益名称</th>
								<th field="rightsLogo" width="100" align="center" formatter="formatLogo">权益LOGO</th>
								<th field="rightsOrder" width="100" align="center">排序</th>
								<th field="rightsStatus" width="100" align="center">状态</th>
								<th field="rightsId" width="150" align="center" hidden="true">ID</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 添加  -->
	<div id="divWindow" style="padding:10px;">
		<form name="addForm" id="addForm" class="setting-profile-form" method="post" enctype="multipart/form-data">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="id" id="id" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">权益名称：</td>
					<td align="left"><input type="text" name="rightsName" id="rightsName"
						size="20" maxlength="10" class="easyui-textbox"
						data-options="required:true,validType:'length[1,30]'" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" width="100">权益排序：</td>
					<td align="left"><!-- <input type="text" name="rightsOrder" id="rightsOrder"
						size="10" maxlength="10" class="easyui-textbox"
						data-options="required:true" /> -->
						<input i class="input easyui-numberbox"  type="text" name="rightsOrder" id="rightsOrder" data-options="required:true,validType:'length[1,2]'" />
						&nbsp;<font color="red">*</font></td>
				</tr>
				
				<tr>
					<td align="right">状态：</td>
					<td align="left">
						<select name="rightsStatus" id="rightsStatus" class="easyui-combobox" style="width:100px;"
						data-options="editable:false,panelHeight:'auto',required:true" >
							<option value="1">启用</option>
							<option value="0">不启用</option>
					    </select>&nbsp;<font color="red">*</font>
					 </td>
				</tr>
		
				<tr>
					<td align="right">权益LOGO：</td>
					<td align="left">
						<input id="rightsLogo" name="rightsLogo"class="easyui-filebox" style="width: 250px;" data-options="required:true" />&nbsp;<font color="red">*</font>
					</td>				
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
		<form name="addForm2" id="addForm2" class="setting-profile-form" method="post" enctype="multipart/form-data">	
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="rightsId2" id="rightsId2" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">权益名称：</td>
					<td align="left"><input type="text" name="rightsName2" id="rightsName2"
						size="38" maxlength="10" class="easyui-textbox"
						data-options="required:true,validType:'length[1,30]'" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" width="100">权益排序：</td>
					<td align="left"><!-- <input type="text" name="rightsOrder2" id="rightsOrder2"
						size="38" maxlength="10" class="easyui-textbox"
						data-options="required:true" /> -->
						<input i class="input easyui-numberbox"  type="text" name="rightsOrder2" id="rightsOrder2" data-options="required:true,validType:'length[1,2]'" />
						&nbsp;<font color="red">*</font></td>
				</tr>
				
				<tr>
					<td align="right">状态：</td>
					<td align="left">
						<select name="rightsStatus2" id="rightsStatus2" class="easyui-combobox" style="width:100px;"
						data-options="editable:false,panelHeight:'auto',required:true" >
							<option value="1">启用</option>
							<option value="0">不启用</option>
					    </select>&nbsp;<font color="red">*</font>
					 </td>
				</tr>
		
				<tr>
					<td align="right">权益LOGO：</td>
					<td align="left">
						<input id="rightsLogo2" name="rightsLogo2"class="easyui-filebox" style="width: 250px;"  />
					</td>
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