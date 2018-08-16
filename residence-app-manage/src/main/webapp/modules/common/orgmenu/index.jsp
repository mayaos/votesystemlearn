<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>轻应用菜单管理</title>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/ImgViewJs/css/viewer.min.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/styles/global.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/modules//common/orgmenu/styles/orgMenu.css" />
	<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
	<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
	<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
	<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
	<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
	<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
	<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/validator.js"></script>
	<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/orgmenu/js/orgMenu.js"></script>
	<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ImgViewJs/js/viewer.min.js"></script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" fit="true">
				<div data-options="region:'north'"style="height:35px;text-align:center;padding-top:5px;">
					菜单类型：
					<input type="text" name="menuTypeNameSearch" id="menuTypeNameSearch" size="20" class="easyui-textbox" data-options="validType:'length[1,20]'" />
					&nbsp;
					菜单名称：
					<input type="text" name="menuNameSearch" id="menuNameSearch" size="20" class="easyui-textbox" data-options="validType:'length[1,20]'" />
					 &nbsp;<a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">查询</a>
					 &nbsp;<a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options=""style="width:60px;height:22px;">返回</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="轻应用菜单列表" data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="menuId" width="50" align="center"  hidden="true">菜单ID</th>
								<th field="menuType" width="100" align="center" hidden="true">菜单类型值</th>
								<th field="menuTypeName" width="100" align="center">菜单类型</th>
								<th field="areaName" width="80" align="center">区域名称</th>
								<th field="menuName" width="100" align="center">菜单名称</th>								
								<th field="menuLogo" width="100" align="center" formatter="formatLogo">菜单LOGO</th>	
								<th field="backgroundImg" width="100" align="center" formatter="formatImg">背景图片</th>				
								<th field="menuUrl" width="250" align="center">菜单链接</th>	
								<th field="menuOrder" width="60" align="center">菜单排序</th>
								<th field="menuDesc" width="100" align="center">菜单描述</th>	
								<th field="menuStatus" width="50" align="center">有效</th>	
								<th field="isDefault" width="60" align="center">首页默认</th>													
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 添加 -->
	<div id="divWindow1" style="padding:10px;">
		<form name="addForm" id="addForm" class="setting-profile-form" method="post" enctype="multipart/form-data">
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">区域选择：</td>
					<td align="left">
						<input name="areaId" id="areaId" class="easyui-combotree" style="width:251px;"
							data-options="
									    editable:false,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				method:'post'	"
						   	multiple 			   		
						 />&nbsp;<font color="red">*</font>					
					</td>
				</tr>			
				<tr>
					<td align="right" width="100">菜单名称：</td>
					<td align="left">
						<input type="text" name="menuName1" id="menuName1" maxlength="30" size="40" class="easyui-textbox"
						data-options="required:true,validType:'length[1,30]'" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" width="100">菜单类型：</td>
					<td align="left">
						 <select class="easyui-combobox" id="menuType1" name="menuType1" style="width:251px;"
							data-options="editable:false,required:true,
	            			panelHeight:'auto',
	                    	valueField:'codeValue',
	                    	textField:'codeName',
	                    	url:Common.getUrl('codectrl/createComboBox?codeType=appMenuType')">
						</select>&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right">菜单LOGO：</td>
					<td align="left">
						<input name="menuLogo1" id="menuLogo1" class="easyui-filebox" style="width: 250px;" data-options="required:true,editable:false" />&nbsp;<font color="red">*&nbsp;建议上传图片像素为 74*74</font>
					</td>
				</tr>
				<tr>
					<td align="right">背景图片：</td>
					<td align="left">
						<input name="backgroundImg1" id="backgroundImg1" class="easyui-filebox" style="width: 250px;" data-options="required:true,editable:false" />&nbsp;<font color="red">*&nbsp;建议上传图片像素为 600*240</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">菜单链接：</td>
					<td align="left"><input type="text" name="menuUrl1" maxlength="1024" id="menuUrl1"
						size="40" class="easyui-textbox"
						data-options="required:true,validType:'length[1,1024]'" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" width="100">菜单排序：</td>
					<td align="left"><input type="text" name="menuOrder1" id="menuOrder1" maxlength="2"
						size="40" class="easyui-textbox"
						data-options="required:true,validType:'length[1,2]'" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">菜单描述：</td>
					<td align="left">
						<textarea name="menuDesc" id="menuDesc" rows="3" cols="60" maxlength="200" style="white-space:normal;" class="textarea">>&nbsp;<font color="red">*</font></textarea>
					</td>
				</tr>				
				<tr>
					<td align="right" width="100">有效：</td>
					<td align="left">
						<select  name="menuStatus1"  id="menuStatus1" class="easyui-combobox" style="width:100px;" panelHeight="auto"  data-options="required:true,editable:false">   
						    <option value="2">否</option>   
						    <option value="1">是</option>   
						</select>
						&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">首页默认：</td>
					<td align="left">
						<select  name="isDefault1"  id="isDefault1" class="easyui-combobox" style="width:100px;" panelHeight="auto"  data-options="required:true,editable:false">   
						    <option value="0">否</option>   
						    <option value="1">是</option>   
						</select>
						&nbsp;
						<font color="red">*</font>
					</td>
				</tr>			
				<tr>
					<td align="center" colspan="2">
						<input type="button"name="btnSave1" id="btnSave1" class="button1" value="保存" />
						<input type="button" name="btnCancel1" id="btnCancel1" class="button1"value="取消" />
					</td>
				</tr>
			</table>
		</form> 
	</div>
	
	<!-- 修改 -->
	<div id="divWindow2" style="padding:10px;">
		<form name="addForm" id="addForm" class="setting-profile-form" method="post" enctype="multipart/form-data">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="menuId2" id="menuId2" />
			</div>		
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">区域选择：</td>
					<td align="left">
						<input name="areaId2" id="areaId2" class="easyui-combotree" style="width:251px;"
							data-options="
									    editable:false,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				 method:'post'	"		
						   	multiple 	   		
						 />&nbsp;<font color="red">*</font>
						 <div id="areaIdHidden" style="display:none"></div>	
					</td>
				</tr>			
				<tr>
					<td align="right" width="100">菜单名称：</td>
					<td align="left"><input type="text" name="menuName2" id="menuName2" maxlength="1024"
						size="40" class="easyui-textbox"
						data-options="required:true,validType:'length[1,1024]'" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" width="100">菜单类型：</td>
					<td align="left">
						 <select class="easyui-combobox" id="menuType2" name="menuType2" style="width:251px;"
							data-options="editable:false,required:true,
	            			panelHeight:'auto',
	                    	valueField:'codeValue',
	                    	textField:'codeName',
	                    	url:Common.getUrl('codectrl/createComboBox?codeType=appMenuType')">
						</select>&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right">菜单LOGO：</td>
					<td align="left">
						<input name="menuLogo2" id="menuLogo2" class="easyui-filebox" style="width: 250px;" data-options="editable:false" />&nbsp;<font color="red">&nbsp;<font color="red">*</font>&nbsp;建议上传图片像素为 74*74</font>
					</td>
				</tr>
				<tr>
					<td align="right">背景图片：</td>
					<td align="left">
						<input name="backgroundImg2" id="backgroundImg2" class="easyui-filebox" style="width: 250px;" data-options="editable:false" />&nbsp;<font color="red">&nbsp;<font color="red">*</font>&nbsp;建议上传图片像素为 600*240</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">菜单链接：</td>
					<td align="left"><input type="text" name="menuUrl2" maxlength="1024" id="menuUrl2"
						size="40" class="easyui-textbox"
						data-options="required:true,validType:'length[1,1024]'" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" width="100">菜单排序：</td>
					<td align="left"><input type="text" name="menuOrder2" id="menuOrder2" maxlength="2"
						size="40" class="easyui-textbox"
						data-options="required:true,validType:'length[1,2]'" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">菜单描述：</td>
					<td align="left">
						<textarea name="menuDesc2" id="menuDesc2" rows="3" cols="60" maxlength="200" style="white-space:normal;" class="textarea">>&nbsp;<font color="red">*</font></textarea>
					</td>
				</tr>					
				<tr>
					<td align="right" width="100">有效：</td>
					<td align="left">
						<select  name="menuStatus2"  id="menuStatus2" class="easyui-combobox" style="width:100px;" panelHeight="auto"  data-options="required:true,invalidMessage:'强制升级不能为空。',editable:false">   
						    <option value="2">否</option>   
						    <option value="1">是</option>   
						</select>
						&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">首页默认：</td>
					<td align="left">
						<select  name="isDefault2"  id="isDefault2" class="easyui-combobox" style="width:100px;" panelHeight="auto"  data-options="required:true,invalidMessage:'强制升级不能为空。',editable:false">   
						    <option value="0">否</option>   
						    <option value="1">是</option>   
						</select>
						&nbsp;
						<font color="red">*</font>
					</td>
				</tr>			
				<tr>
					<td align="center" colspan="2">
						<input type="button"name="btnSave2" id="btnSave2" class="button1" value="保存" />
						<input type="button" name="btnCancel2" id="btnCancel2" class="button1"value="取消" />
					</td>
				</tr>
			</table>
		</form> 
	</div>
</body>
</html>