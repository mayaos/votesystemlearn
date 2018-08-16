<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/modules/common/user/styles/user.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript"src="<%=request.getContextPath()%>/resources/js/validator.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/user/js/user.js"></script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" fit="true">
				<div data-options="region:'north'" style="height:35px;text-align:center;padding-top:5px;">
					用户名：<input name="userCode" id="userCode" class="easyui-textbox" style="width:100px;height:22px;line-height:22px;"> 
					别名：<input name="userName" id="userName" class="easyui-textbox" style="width:100px;height:22px;line-height:22px;"> 
					创建日期：<input id="createDateStart" name="createDateStart" class="easyui-datebox" data-options="editable:false" style="width:150px"> 
					-- <input id="createDateEnd" name="createDateEnd" class="easyui-datebox" data-options="editable:false" style="width:150px"> 
					注销：<select name="isFrozen" id="isFrozen" class="easyui-combobox" data-options="editable:false,cache:false,panelHeight:'auto'" style="width:80px;">
						<option value="">--请选择--</option>
						<option value="NO">否</option>
						<option value="YES">是</option>
					</select> 
					<a id="search" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:60px;height:22px;">查询</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="用户列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="userCode" width="100" align="center">用户名</th>
								<th field="userName" width="150" align="center">别名</th>
								<th field="roleId" width="100" align="center" hidden="true">角色权限ID</th>
								<th field="roleName" width="150" align="center">角色权限</th>
								<th field="userSexVal" width="50" align="center">性别</th>
								<th field="pwdvaildTime" width="100" align="center">密码有效期</th>
								<th field="isFrozenVal" width="50" align="center">注销</th>
								<th field="isSmsVerify" width="60" align="center">短信验证</th>
								<th field="userMobile" width="100" align="center">联系电话</th>
								<th field="userBirthdate" width="100" align="center">出生日期</th>
								<th field="userEmail" width="180" align="center">邮箱</th>
								<th field="createTime" width="130" align="center">创建时间</th>
								<th field="lastloginTime" width="130" align="center">最后登录时间</th>
								<th field="lastloginIp" width="130" align="center">最后登录IP</th>
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
			<table align="center" cellpadding="0" cellspacing="1" width="100%"
				class="tab">
				<tr>
					<td align="right" width="100">用户名：</td>
					<td align="left"><input type="text" name="ucode" id="ucode" size="32" maxlength="10" class="easyui-textbox"
						data-options="required:true,validType:'length[4,20]',invalidMessage:'输入内容长度必须4-20位'"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">别名：</td>
					<td align="left">
						<input type="text" name="uname" id="uname" size="32" class="easyui-textbox" data-options="validType:'length[0,30]'" />
					</td>
				</tr>
				<tr>
					<td align="right" width="100">选择管理员管理区域：</td>
					<td align="left">
						<input name="areaId" id="areaId" class="easyui-combotree" style="width:180px;"
							data-options="
									    editable:false,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				 method:'post'	"			   		
						 />&nbsp;<font color="red">*</font>			
					</td>
				</tr>
				<tr>
					<td align="right">密码：</td>
					<td align="left"><input type="text" name="upwd" id="upwd" size="32" maxlength="30" class="easyui-textbox"
						data-options="required:true,validType:'length[8,50]',invalidMessage:'输入内容长度必须8-50位'" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">密码有效期：</td>
					<td align="left"><input id="upwdvdate" name="upwdvdate" class="easyui-datebox" data-options="editable:false ,required:true"
						style="width:180px">&nbsp;<font color="red">*</font></td>
				</tr>
				<!-- 
				<tr>
					<td align="right" width="100">选择用户所属区域：</td>
					<td align="left">
						<input name="distcode" id="distcode" class="easyui-combotree" style="width:180px;" 
							data-options="required:true,
							url:'../districtctrl/addrTreelist',
							method:'post',
							queryParams:{'iSelf':'1'}" 
						/>&nbsp;<font color="red">*</font>
					</td>
				</tr>
				 -->
				<tr>
					<td align="right" width="100">选择用户角色：</td>
					<td align="left"><select name="roleUuid" id="roleUuid" class="easyui-combobox" style="width:180px;" 
						data-options="required:true,editable:false"></select>&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<tr hidden="true">
					<td align="right" height="30">短信验证登陆：</td>
					<td align="left" >
						<label><input type="radio" name="isSmsVerify" value="NO" checked/> 否 </label>&nbsp;&nbsp;
						<label><input type="radio" name="isSmsVerify" value="YES"/> 是 (决策账号建议选择短信验证监管)</label>
					</td>
				</tr>
				<tr>
					<td align="right">联系电话：</td>
					<td align="left"><input type="text" name="umobile"
						id="umobile" size="32" maxlength="30" validtype="phone" class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td align="right">性别：</td>
					<td align="left"><select name="usex" id="usex" class="easyui-combobox" style="width:50px;"
						data-options="editable:false,panelHeight:'auto'">
							<option value="MALE">男</option>
							<option value="FEMALE">女</option>
							<option value="UNKNOWN">未知</option>
					</select></td>
				</tr>
				<tr>
					<td align="right">证件号码：</td>
					<td align="left">
						<input type="text" name="uidcard" id="uidcard" size="25" class="easyui-textbox" data-options="validType:'length[1,18]'"/>
					</td>
				</tr>
				<tr>
					<td align="right">出生日期：</td>
					<td align="left">
						<input id="ubdate" name="ubdate" class="easyui-datebox" data-options="editable:false" style="width:180px">
					</td>
				</tr>
				<tr>
					<td align="right">邮箱：</td>
					<td align="left"><input type="text" name="uemail" id="uemail"
						size="38" maxlength="30" class="easyui-textbox" data-options="validType:['email','length[0,30]']" />
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

	<!-- 修改  -->
	<div id="divWindow2" style="padding:10px;">
		<form name="addForm2" id="addForm2" method="post" action="">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="id2" id="id2" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%"
				class="tab">
				<tr>
					<td align="right" width="100">用户名：</td>
					<td align="left"><input type="text" name="ucode2" id="ucode2" size="32" class="easyui-textbox"  disabled="disabled" readonly="readonly"/>
						&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">密码有效期：</td>
					<td align="left"><input id="upwdvdate2" name="upwdvdate2"
						class="easyui-datebox" style="width:180px">&nbsp;
						<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" width="100">选择管理员管理区域：</td>
					<td align="left">
						<input name="areaId2" id="areaId2" class="easyui-combotree" style="width:180px;"
							data-options="
									    editable:false,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				 method:'post'	"			   		
						 />&nbsp;<font color="red">*</font>			
					</td>
				</tr>
				<tr>
					<td align="right" width="100">选择用户角色：</td>
					<td align="left"><select name="roleUuid2" id="roleUuid2" class="easyui-combobox" style="width:180px;" 
						data-options="required:true,editable:false"></select>&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right">注销：</td>
					<td align="left"><select name="isFrozen2" id="isFrozen2"
						class="easyui-combobox" style="width:50px;" data-options="editable:false">
							<option value="YES">是</option>
							<option value="NO">否</option>
					</select></td>
				</tr>
				<tr hidden="true">
					<td align="right" height="30">短信验证登陆：</td>
					<td align="left" >
						<label><input type="radio" name="isSmsVerify2" value="NO"/> 否 </label>&nbsp;&nbsp;
						<label><input type="radio" name="isSmsVerify2" value="YES"/> 是 (决策账号建议选择短信验证监管)</label>
					</td>
				</tr>
				<tr>
					<td align="right">联系电话：</td>
					<td align="left"><input type="text" name="umobile2"
						id="umobile2" size="32" maxlength="30" class="easyui-textbox" validtype="phone" /></td>
				</tr>
				<tr>
					<td align="right">别名：</td>
					<td align="left">
						<input type="text" name="uname2" id="uname2" size="32" class="easyui-textbox" data-options="validType:'length[0,30]'" />
					</td>
				</tr>
				<tr>
					<td align="right">性别：</td>
					<td align="left">
						<select name="usex2" id="usex2" class="easyui-combobox" style="width:50px;" data-options="editable:false">
							<option value="MALE">男</option>
							<option value="FEMALE">女</option>
					</select></td>
				</tr>
				<tr>
					<td align="right">证件号码：</td>
					<td align="left"><input type="text" name="uidcard2"
						id="uidcard2" size="25" maxlength="30" class="easyui-textbox" data-options="validType:'length[1,18]',invalidMessage:'输入内容长度必须1-18位'"/></td>
				</tr>
				<tr>
					<td align="right">出生日期：</td>
					<td align="left"><input id="ubdate2" name="ubdate2"
						class="easyui-datebox" style="width:180px"></td>
				</tr>
				<tr>
					<td align="right">电子邮箱：</td>
					<td align="left"><input type="text" name="uemail2"
						id="uemail2" size="38" maxlength="30" class="easyui-textbox" data-options="validType:['email','length[0,30]']" /></td>
				</tr>
				<tr>
					<td align="center" colspan="2"><input type="button"
						name="btnSave" id="btnSave2" class="button1" value="保存" />&nbsp;
						<input type="button" name="btnCancel" id="btnCancel2"
						class="button1" value="取消" /></td>
				</tr>
			</table>
		</form>
	</div> 
	<!-- 角色配置 -->
	<div id="divWindow3" class="easyui-layout">
		<div data-options="region:'center',border:false,fit:true">
			<table id="roleSettingList" class="easyui-datagrid"
				data-options="singleSelect:true,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
				 <thead>
					<tr>
						<th field="checked" width="30" data-options="checkbox:true"></th>
						<th field="role_id" width="60" align="center">角色id</th>
						<th field="role_name" width="100" align="center">角色名称</th>
						<th field="create_date" width="90" align="center">修改时间</th>
						<th field="idefault" width="50" align="center">是否默认</th>
						<th field="role_desc" width="240" align="center">描述</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>
</html>