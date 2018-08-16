<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>机构信息管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/ImgViewJs/css/viewer.min.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/modules/common/orgInfo/styles/orgInfo.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript"src="<%=request.getContextPath()%>/resources/js/validator.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/orgInfo/js/orgInfo.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ImgViewJs/js/viewer.min.js"></script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" fit="true">
				<div data-options="region:'north'"style="height:35px;text-align:center;padding-top:5px;">
					菜单名：<input type="text" name="menuNameSearch" id="menuNameSearch" size="20" class="easyui-textbox"
						data-options="validType:'length[1,10]'" />
					&nbsp;机构名：<input type="text" name="orgNameSearch" id="orgNameSearch" size="20" class="easyui-textbox"
						data-options="validType:'length[1,10]'" />	
					 <a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">查询</a>
					 <a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options=""style="width:60px;height:22px;">返回</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="第三方机构列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="orgId" width="50" align="center"  hidden="true">机构ID</th>
								<th field="menuId" width="50" align="center"  hidden="true">菜单ID</th>
								<th field="areaId" width="50" align="center"  hidden="true">区域ID</th>
								<th field="menuLogo" width="50" align="center"  hidden="true">菜单Logo</th>
								<th field="orgDes" width="100" align="center"  hidden="true">机构描述</th>	
								<th field="noPassReason" width="100" align="center"  hidden="true">审核失败原因</th>									
								<th field="areaName" width="100" align="center">区域名</th>
								<th field="menuName" width="100" align="center">菜单名</th>
								<th field="orgName" width="100" align="center">机构名称</th>
								<th field="orgLogo" width="100" align="center" formatter="formatLogo">机构Logo</th>
								<th field="orgPhone" width="100" align="center">电话号码</th>								
								<th field="passFlag" width="100" align="center">审核状态</th>
								<th field="createTime" width="150" align="center">创建时间</th>
								<th field="updateTime" width="150" align="center">修改时间</th>			
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 添加 -->
	<div id="divWindow" style="padding:10px;">
		<form name="addForm" id="addForm" class="setting-profile-form" method="post" enctype="multipart/form-data">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="orgId" id="orgId" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">机构名称：</td>
					<td align="left"><input type="text" name="orgName" id="orgName"
						size="40" class="easyui-textbox"
						data-options="required:true,validType:'length[1,20]'" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" width="100">选择区域：</td>
					<td align="left">
						<input name="areaId" id="areaId" class="easyui-combotree" style="width:180px;"
							data-options="
									    editable:false,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				 method:'post',
						   				 required:true" 
						   	multiple 
						 />&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">选择菜单：</td>
					<td align="left">
						 <select class="easyui-combobox" id="menuID" name="menuID" style="width:120px;"
							data-options="editable:false,required:true,
	            			panelHeight:'200',
	                    	valueField:'menuId',
	                    	textField:'menuName',
	                    	url:Common.getUrl('orgMenuctrl/createCombobox?menuStatus=1')">
						</select><font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">机构描述：</td>
					<td align="left">
						<textarea name="orgDesc" id="orgDesc" rows="3" cols="60" maxlength="100" style="white-space:normal;" class="textarea"></textarea>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">联系电话：</td>
					<td align="left"><input type="text" name="orgPhone" id="orgPhone"
						size="28" maxlength="20" class="easyui-textbox"
						data-options="required:true" validtype="phoneRex"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">机构Logo:</th>
					<td align="left">
						<input name="orgLogo" id="orgLogo" class="easyui-filebox" style="width: 250px;" data-options="required:true" />&nbsp;<font color="red">*</font>
					</td>
				</tr>				
				<tr>
					<td align="right" width="100">审核：</td>
					<td align="left">
						<select  name="passFlag"  id="passFlag" class="easyui-combobox" style="width:100px;" panelHeight="auto" data-options="required:true, editable:false">   
						    <option value="1">未审核</option>   
						    <option value="0">通过</option>   
						    <option value="2">未通过</option>  
						</select>
						&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">审核未通过原因：</td>
					<td align="left">
						<textarea name="noPassReason" id="noPassReason" rows="3" cols="60" maxlength="50" style="white-space:normal;" class="textarea"></textarea>
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
	
	<!-- 信息详情 -->
	<div title="信息详情" id="divWindow1"  style="width:638px; height:385px">
		<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
			<tr>
				<td align="right" width="100">区域：</td>
				<td align="left" id="area1"></td>
			</tr>
			<tr>
				<td align="right" width="100">菜单：</td>
				<td align="left" id="menu1"></td>
			</tr>
			<tr>
				<td align="right">菜单Logo:</th>
				<td align="left">
					<img id="menuLogo1" src="../../../resources/images/hyaline-Image.png" alt="" width="100" height="100" align="middle"/>
				</td>
			</tr>
			<tr>
				<td align="right" width="100">机构名称：</td>
				<td align="left" id="orgName1"></td>
			</tr>
			<tr>
				<td align="right">机构Logo:</th>
				<td align="left">
					<img id="orgLogo1" src="../../../resources/images/hyaline-Image.png" alt="" width="100" height="100" align="middle"/>
				</td>
			</tr>
			<tr>
				<td align="right" width="100">机构描述：</td>
				<td align="left" id="orgDesc1"></td>
			</tr>
			<tr>
				<td align="right" width="100">联系电话：</td>
				<td align="left" id="orgPhone1"></td>
			</tr>			
			<tr>
				<td align="right" width="100">审核状态：</td>
				<td align="left" id="passFlag1"></td>
			</tr>
			<tr>
				<td align="right" width="100">审核不通过原因：</td>
				<td align="left" id="noPassReason1"></td>
			</tr>
			<tr>
				<td align="right" width="100">创建时间：</td>
				<td align="left" id="createTime1"></td>
			</tr>
			<tr>
				<td align="right" width="100">修改时间：</td>
				<td align="left" id="updateTime1"></td>
			</tr>
		</table>
	</div>	
	
	<!-- 信息修改 -->
	<div id="divWindow2" style="padding:10px;">
		<form name="addForm" id="addForm" class="setting-profile-form" method="post" enctype="multipart/form-data">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="orgId2" id="orgId2" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">机构名称：</td>
					<td align="left"><input type="text" name="orgName2" id="orgName2"
						size="40" class="easyui-textbox"
						data-options="required:true,validType:'length[1,20]'" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" width="100">选择区域：</td>
					<td align="left">
						<input name="areaId2" id="areaId2" class="easyui-combotree" style="width:180px;"
							data-options="
									    editable:false,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				 method:'post'	"	
						   	multiple 		   		
						 /><div id="areaIdHidden" style="display:none"></div>				
					</td>
				</tr>
				<tr>
					<td align="right" width="100">选择菜单：</td>
					<td align="left">
						 <select class="easyui-combobox" id="menuID2" name="menuID2" style="width:120px;"
							data-options="editable:false,required:true,
	            			panelHeight:'200',
	                    	valueField:'menuId',
	                    	textField:'menuName',
	                    	url:Common.getUrl('orgMenuctrl/createCombobox?menuStatus=1')">
						</select><font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">机构描述：</td>
					<td align="left">
						<textarea name="orgDesc2" id="orgDesc2" rows="3" cols="60" maxlength="100" style="white-space:normal;" class="textarea"></textarea>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">联系电话：</td>
					<td align="left"><input type="text" name="orgPhone2" id="orgPhone2"
						size="28" maxlength="20" class="easyui-textbox"
						data-options="required:true" validtype="phoneRex"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">机构Logo:</th>
					<td align="left">
						<input name="orgLogo2" id="orgLogo2" class="easyui-filebox" style="width: 250px;" />
					</td>
				</tr>				
				<tr>
					<td align="right" width="100">审核：</td>
					<td align="left">
						<select  name="passFlag2"  id="passFlag2" class="easyui-combobox" style="width:100px;" panelHeight="auto" data-options="required:true, editable:false">   
						    <option value="1">未审核</option>   
						    <option value="0">通过</option>   
						    <option value="2">未通过</option>  
						</select>
						&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">审核未通过原因：</td>
					<td align="left">
						<textarea name="noPassReason2" id="noPassReason2" rows="3" cols="60" maxlength="50" style="white-space:normal;" class="textarea"></textarea>
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