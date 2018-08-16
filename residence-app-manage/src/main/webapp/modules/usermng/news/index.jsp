<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String path = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>个人消息管理 </title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/styles/global.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/modules/usermng/news/styles/base.css"/>
<script language="javascript" type="text/javascript" src="<%=path%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/resources/js/validator.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/modules/usermng/news/js/base.js"></script>
<script language="javascript" type="text/javascript">
    var root = "<%=path%>";
</script>
</head>
<body>
   <div class="easyui-layout" fit="true">	    
		<div data-options="region:'north'" style="height:45px;text-align:center;padding-top:10px;">
			用户账号：
			<input type="text" id="userName" name="userName" class="easyui-textbox"/>
			&nbsp;选择状态：
			<select class="easyui-combobox" id="msgStatus" name="msgStatus" style="width:80px;">
				<option value="0">未读</option>
				<option value="1">已读</option>
        	</select>
			创建起止时间：
			<input name="startTime" id="startTime" class="easyui-datetimebox" data-options="editable:false" style="width:150px;"/> 
			--
			<input name="endTime" id="endTime" class="easyui-datetimebox" data-options="editable:false" style="width:150px;"/>
			<a id="search" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:60px;height:22px;">查询</a>
			<a id="reset" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:60px;height:22px;">重置</a>
		</div>
       
		<div data-options="region:'center',border:false">
			<table id="gridList" class="easyui-datagrid"  title="用户消息" 
			data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
				<thead>
					<tr>
						<th field="checked" width="30" data-options="checkbox:true"></th>
						<th field="citizenID" width="100" align="center" >用户账号</th>
						<th field="messageTitle" width="180" align="center" >消息标题</th>
						<th field="messageContent" width="480" align="left">消息内容</th>
						<th field="messageStatus" width="60" align="center">消息状态</th>
						<th field="createTime" width="125" align="center">消息创建时间</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	
	<div id="divWindow-edit" style="padding:10px;">
		<form name="editForm" id="editForm" method="post" action="">
		<div style="display: none">
			<input type="hidden" id="messageID" name="messageID" class="easyui-textbox"/>
		</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" height="30">消息标题：</td>
					<td align="left"><input type="text" name="messageTitle" id="messageTitle" class="easyui-textbox" data-options="required:true,validType:'length[1,50]'" style="width:360px;"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" height="30">消息内容：</td>
					<td align="left">
						<textarea name="messageContent" id="messageContent" rows="6" cols="60"  maxlength="300" class="textarea" style="white-space:normal;"></textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2" height="30">
						<a id="btnOk-edit" href="javascript:void(0);" icon="icon-ok" class="easyui-linkbutton">&nbsp;提交&nbsp;</a>&nbsp;
						<a id="btnClose-edit" href="javascript:void(0);" icon="icon-cancel" class="easyui-linkbutton">&nbsp;关闭&nbsp;</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	
</body>
</html>
