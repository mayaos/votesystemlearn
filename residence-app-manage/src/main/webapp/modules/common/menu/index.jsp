<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>菜单管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/styles/global.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/modules/common/menu/styles/menu.css"/>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/menu/js/menu.js"></script>
</head>
<body>
<div class="easyui-layout" fit="true">
	<div data-options="region:'west',split:false,collapsible:true,iconCls:'icon-reload'" title="菜单信息" style="width:200px;">
		<div id="divTree"></div>
	</div>
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" fit="true">
			<div data-options="region:'center',border:false">
				<form name="addForm" id="addForm" method="post" action="">
					<div style="display: none">
	       				<input class="easyui-textbox" type="hidden" name="menu_id" id="menu_id" />
	    			</div>
					<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
					    <tr>
							<td align="left" width="100" colspan="2"><font size="50">编辑菜单：</font></td>
						</tr>
			            <tr>
							<td align="right" width="100">菜单名称：</td>
							<td align="left"><input class="easyui-textbox" type="text" name="menu_name" id="menu_name" data-options="required:true,validType:'length[1,100]'" size="30" maxlength="10" />&nbsp;<font color="red">*</font></td>
						</tr>
						<tr>
							<td align="right" width="100">菜单样式：</td>
							<td align="left"><input type="text" name="menu_icon" id="menu_icon" size="30" maxlength="30" class="easyui-textbox" /></td>
						</tr>
						<tr>
							<td align="right" width="100">菜单URL：</td>
							<td align="left"><input type="text" name="menu_url" id="menu_url" size="30" maxlength="30" class="easyui-textbox" /></td>
						</tr>
							<tr>
								<td align="right" width="100">菜单按钮（多选）：</td>
								<td align="left"><input class="easyui-combobox" id="cc" style="width:200px;"/></td>
							</tr>
							<tr>
							<td align="right" width="100">排序：</td>
							<td align="left"><input type="text" name="menu_sort" id="menu_sort" size="30" maxlength="30" class="easyui-textbox" /></td>
						</tr>
						<tr>
							<td align="right">描述：</td>
							<td align="left"><textarea name="menu_desc" id="menu_desc" rows="5" cols="60" class="textarea"></textarea></td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<input type="button" name="btnSave" id="btnSave" class="button1" disabled="disabled" value="确定" />&nbsp;
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	</div>
</body>
</html>