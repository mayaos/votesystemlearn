<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>配置管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/modules/common/config/styles/config.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/config/js/config.js"></script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" fit="true">
				<div data-options="region:'north'" style="height:35px;text-align:left;padding-top:5px;">
					&nbsp;&nbsp;参数名称：<input name="paramName" id="paramName" class="easyui-textbox"
								  style="width:100px;height:22px;line-height:22px;"> 
					参数值：<input name="paramValue" id="paramValue" class="easyui-textbox"
								  style="width:100px;height:22px;line-height:22px;"> 
					<a id="search" href="javascript:void(0);" class="easyui-linkbutton"
					   data-options="iconCls:'icon-search'" style="width:60px;height:22px;">查询</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="参数列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field=paramName width="100" align="center">参数名称</th>
								<th field="paramValue" width="100" align="center">参数值</th>
								<th field="paramDesc" width="100" align="center">参数描述</th>
								<th field="paramValidVal" width="130" align="center">参数是否有效</th>
								<th field="upImgsrc" width="100" align="center" formatter="functionLink">上传图片</th>
								<th field="modifyDate" width="130" align="center">修改时间</th>							
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
				<input class="easyui-textbox" type="hidden" name="param_id" id="param_id" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">参数名称：</td>
					<td align="left">
						<input type="text" name="param_name" id="param_name"
							   size="38" maxlength="10" class="easyui-textbox"
						       data-options="required:true" />
						&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right">参数值：</td>
					<td align="left">
						<input type="text" name="param_value" id="param_value"
							   size="38" maxlength="30" class="easyui-textbox" />
					</td>
				</tr>
				<tr>
					<td align="right">参数描述：</td>
					<td align="left">
						<input type="text" name="param_desc" id="param_desc"
						       size="38" maxlength="30" class="easyui-textbox" />
					</td>
				</tr>
				<tr>
					<td align="right">上传图片：</td>
					<td align="left">
						<input type="file" name="up_imgsrc" id="up_imgsrc" 
							   class="easyui-validatebox" style="width:200px;border:1px"></input>
					</td>
				</tr>
				<tr>
					<td align="right">参数是否有效：</td>
					<td align="left">
						<select name="param_valid" id="param_valid"
						        class="easyui-combobox" style="width:50px;"
						        data-options="editable:false,panelHeight:'auto'">
							<option value="VALID">有效</option>
							<option value="INVALID">失效</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="2">
						<input type="button" name="btnSave" id="btnSave" class="button1" value="保存" />
						&nbsp; 
						<input type="button" name="btnCancel" id="btnCancel" class="button1" value="取消" />
					</td>
				</tr>
			</table>
		</form>
	</div>

	<!-- 修改  -->
	<div id="divWindow2" style="padding:10px;">
		<form name="addForm2" id="addForm2" method="post" action="" enctype="multipart/form-data">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="param_id2" id="param_id2" />
				<input class="easyui-textbox" type="hidden" name="img_src" id="img_src" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">参数名称：</td>
					<td align="left">
						<input type="text" name="param_name2" id="param_name2"
							   size="38" maxlength="10" class="easyui-textbox"
						       data-options="required:true" />
						&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right">参数值：</td>
					<td align="left">
						<input type="text" name="param_value2" id="param_value2"
							   size="38" maxlength="30" class="easyui-textbox" />
					</td>
				</tr>
				<tr>
					<td align="right">参数描述：</td>
					<td align="left">
						<input type="text" name="param_desc2" id="param_desc2"
						       size="38" maxlength="30" class="easyui-textbox" />
					</td>
				</tr>
				<tr>
					<td align="right">上传图片：</td>
					<td align="left">
						<input type="file" name="up_imgsrc2" id="up_imgsrc2" 
							   class="easyui-validatebox" style="width:200px;border:1px"></input>
					</td>
				</tr>
				<tr>
					<td align="right">参数是否有效：</td>
					<td align="left">
						<select name="param_valid2" id="param_valid2"
						        class="easyui-combobox" style="width:50px;"
						        data-options="editable:false,panelHeight:'auto'">
							<option value="VALID">有效</option>
							<option value="INVALID">失效</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="2">
						<input type="button" name="btnSave2" id="btnSave2" class="button1" value="保存" />
						&nbsp; 
						<input type="button" name="btnCancel2" id="btnCancel2" class="button1" value="取消" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	<!-- 打开图片 -->
	<div id="divWindow3" style="padding:10px;">
		<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
			<tr>
				<td align="right"><img id="imgId" src="" width="500" height="240"/> </td>
			</tr>
		</table>
	</div> 
</body>
</html>