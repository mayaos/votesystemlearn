<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String path = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户信息管理 </title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/styles/global.css"/>
<link rel="stylesheet" type="text/css" href="<%=path%>/modules/usermng/info/styles/base.css"/>
<script language="javascript" type="text/javascript" src="<%=path%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=path%>/modules/usermng/info/js/base.js"></script>
<script language="javascript" type="text/javascript">
    var root = "<%=path%>";
</script>
</head>
<body>
   <div class="easyui-layout" fit="true">	    
		<div data-options="region:'north'" style="height:45px;text-align:center;padding-top:10px;">
			用户账号：
			<input type="text" id="userName" name="userName" class="easyui-textbox"/>
			选择级别：
			<select class="easyui-combobox" id="userLevel" name="userLevel" style="width:80px;"
				data-options="editable:false,
            		panelHeight:'auto',
                    valueField:'codeValue',
                    textField:'codeName',
                    url:Common.getUrl('codectrl/createComboBox?codeType=userLevel') ">
			</select>
			注册起止时间：
			<input name="startTime" id="startTime" class="easyui-datetimebox" data-options="editable:false" style="width:150px;"/> 
			--
			<input name="endTime" id="endTime" class="easyui-datetimebox" data-options="editable:false" style="width:150px;"/>
			<a id="search" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:60px;height:22px;">查询</a>
			<a id="reset" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:60px;height:22px;">重置</a>
		</div>
		
		<div data-options="region:'center',border:false">
			<table id="gridList" class="easyui-datagrid"  title="用户账号信息" 
			data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
				<thead>
					<tr>
						<th field="checked" width="30" data-options="checkbox:true"></th>
						<th field="citizenID" width="80" align="center" hidden="true">用户id</th>
						<th field="userName" width="100" align="center" >用户账号</th>
						<th field="userNickname" width="100" align="center">用户昵称</th>
						<th field="userLevel" width="80" align="center">账号级别</th>
						<th field="userStatus" width="60" align="center">账号状态</th>
						<th field="userRegTime" width="125" align="center">注册时间</th>
						<th field="userLastLoginTime" width="125" align="center">最后登录时间</th>
						<th field="email" width="125" align="center">电子邮件</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	
	<div id="showWindow" style="padding:10px;">
		<form name="showForm" id="showForm" method="post" action="">
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="50">姓名：</td>
					<td align="left" id="citizenName"></td>
				</tr>
				<tr>
					<td align="right">身份证号：</td>
					<td align="left" id="idCard"></td>
				</tr>
				<tr>
					<td align="right">居住证号：</td>
					<td align="left" id="rcCard"></td>
				</tr>
				<tr>
					<td align="right">银行卡号：</td>
					<td align="left" id="bankCard"></td>
				</tr>
				<tr>
					<td align="right">居住证卡类型：</td>
					<td align="left" id="rcCardType"></td>
				</tr>
				<tr>
					<td align="right">性别：</td>
					<td align="left" id="sex"></td>
				</tr>
				<tr>
					<td align="right">生日：</td>
					<td align="left" id="birthday"></td>
				</tr>
				<tr>
					<td align="right">民族：</td>
					<td align="left" id="nation" ></td>
				</tr>
				<tr>
					<td align="right">户籍地址：</td>
					<td align="left" id="address" style="white-space:normal;"></td>
				</tr>
				<tr>
					<td align="right">居住证签发日期：</td>
					<td align="left" id="issueDate"></td>
				</tr>
				<tr>
					<td align="right">居住证签发机关：</td>
					<td align="left" id="issueOffice" style="white-space:normal;"></td>
				</tr>
				<tr>
					<td align="right">居住证卡有效期：</td>
					<td align="left" id="validThru"></td>
				</tr>
				<tr>
					<td align="right">居住证头像：</td>
					<td align="left">
						<img id="rcHead" src="" alt=""  width="100" height="100" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	
	<div id="vipWindow" style="padding:10px;">
		<table id="vipCardList" class="easyui-datagrid"
			data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
			<thead>
				<tr>
					<th field="vipcardCode" width="100" align="center">会员卡号</th>
					<th field="merchantName" width="160" align="center">卡商</th>
					<th field="openTime" width="100" align="center">开卡时间</th>
					<th field="endTime" width="100" align="center">截止时间</th>
					<th field="vipcardRule" width="230" align="left">使用规则</th>
				</tr>
			</thead>
		</table>
	</div>
	
	<div id="couponWindow" style="padding:10px;">
		<table id="couponList" class="easyui-datagrid"
			data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
			<thead>
				<tr>
					<th field="couponCode" width="100" align="center">优惠券码</th>
					<th field="benefitName" width="160" align="center">优惠信息</th>
					<th field="merchant" width="100" align="center">商家名称</th>
					<th field="quota" width="60" align="center">优惠额度</th>
					<th field="useExplain" width="100" align="left">使用条件</th>
					<th field="status" width="80" align="center">状态</th>
					<th field="createTime" width="125" align="center">创建时间</th>
					<th field="validTime" width="125" align="center">有效期</th>
				</tr>
			</thead>
		</table>
	</div>
	
	<div id="newsWindow" style="padding:10px;">
		<table id="newsList" class="easyui-datagrid"
			data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
			<thead>
				<tr>
					<th field="messageTitle" width="100" align="left">消息标题</th>
					<th field="messageContent" width="360" align="left">消息内容</th>
					<th field="messageStatus" width="80" align="center">消息状态</th>
					<th field="createTime" width="125" align="center">消息创建时间</th>
				</tr>
			</thead>
		</table>
	</div>
	
</body>
</html>
