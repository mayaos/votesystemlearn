<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>商家app</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/ImgViewJs/css/viewer.min.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/modules//common/merApp/styles/merApp.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/validator.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/merApp/js/merApp.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ImgViewJs/js/viewer.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ckeditor/ckeditor.js"></script>


<script type="text/javascript">
    window.onload = function()
    {
       CKEDITOR.replace(
        	'display_content',
        	{
        	 	toolbarCanCollapse:true,
        		toolbarStartupExpanded:false,
		        readOnly:true,
        		height:284 
        	}
        );
       CKEDITOR.replace(
        	'edit_content',
        	{
        		toolbarCanCollapse:true,
        		toolbarStartupExpanded:true,
        		height:284 
        	}
        );
    };
</script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" fit="true">
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="商家app"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="fromType" width="100" align="center" hidden="true">来源</th>
								<th field="merchantName" width="100" align="center">商户名称 </th>	
								<th field="companyName" width="200" align="center">公司名称</th>	
								<th field="companyType" width="50" align="center">公司性质</th>
								<th field="division" width="100" align="center">地区</th>								
								<th field="adress" width="300" align="center">商家地址</th>
								<th field="mainCategory" width="250" align="center">主营类目</th>
								<th field="contacts" width="100" align="center">联系人</th>	
								<th field="position" width="100" align="center">职务 </th>								
								<th field="phone" width="100" align="center">联系人电话 </th>				
								<th field="email" width="100" align="center">邮箱地址</th>													
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改 -->
	<div id="divWindow2" style="padding:10px;">
		<form name="addForm" id="addForm" class="setting-profile-form" method="post" enctype="multipart/form-data">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="menuId2" id="menuId2" />
			</div>		
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">商家名称：</td>
					<td align="left"><div id="merchantName1" name="merchantName"></div></td>
				</tr>	
				<tr>
					<td align="right" width="100">公司名称：</td>
					<td align="left"><div id="companyName1" name="companyName"></div></td>
				</tr>	
				<tr>
					<td align="right" width="100">公司性质：</td>
					<td align="left"><div id="companyType1" name="companyType"></div></td>
				</tr>	
				<tr>
					<td align="right" width="100">地区编码：</td>
					<td align="left"><div id="division1" name="division"></div></td>
				</tr>	
								<tr>
					<td align="right" width="100">主营类目：</td>
					<td align="left"><div id="mainCategory1" name="mainCategory"></div></td>
				</tr>	
				</tr>	
				<tr>
					<td align="right" width="100">联系人：</td>
					<td align="left"><div id="contacts1" name="contacts"></div></td>
				</tr>
				<tr>
					<td align="right" width="100">职务：</td>
					<td align="left"><div id="position1" name="position"></div></td>
				</tr>
				<tr>
					<td align="right" width="100">商家电话：</td>
					<td align="left"><div id="phone1" name="phone"></div></td>
				</tr>
				<tr>
					<td align="right" width="100">邮箱地址：</td>
					<td align="left"><div id="email1" name="email"></div></td>
				</tr>
				<tr>
					<td align="right" width="100">其他说明：</td>
					<td align="left"><div id="others1" name="others"></div></td>
				</tr>
				<tr>
					<td align="right" width="100">营业执照：</td>
					<td align="left">
						<ul id="imgViewer">
								
							<li style="float:left;padding:10px;">
								<img id="licenseImg11" src=""  width="100" height="100" align="middle"/>
							</li>
							<li style="float:left;padding:10px;">
								<img id="licenseImg21" src=""  width="100" height="100" align="middle"/>
							</li>
							<li style="float:left;padding:10px;">
								<img id="licenseImg31" src=""  width="100" height="100" align="middle"/>
							</li>
						</ul>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">店面照片：</td>
					<td align="left">
						<ul id="imgViewer2">
							<li style="float:left;padding:10px;">
								<img id="storefrontPhotos11" src=""  width="100" height="100" align="middle"/>
							</li>
							<li style="float:left;padding:10px;">
								<img id="storefrontPhotos21" src=""  width="100" height="100" align="middle"/>
							</li>
							<li style="float:left;padding:10px;">
								<img id="storefrontPhotos31" src=""  width="100" height="100" align="middle"/>
							</li>
						</ul>
					</td>
				</tr>																																
				<tr>
					<td align="right">是否通过:</td>
					<td align="left">
						<div>
							<input type="radio" id="pass" name="ispass" value="3" checked/>通过
							<input type="radio" id="noPass" name="ispass" value="4"/>不通过					
						</div>
					</td>
				</tr>
				<tr id="content">
					<td align="right">不通过理由：</td>
					<td align="left">
						<div id="edit_content"></div>
					</td>
				</tr>				
				<tr>
					<td align="center" colspan="2">
						<input type="button"name="btnSave2" id="btnSave2" class="button1" value="提交" />
						<input type="button" name="btnCancel2" id="btnCancel2" class="button1"value="取消" />
					</td>
				</tr>
			</table>
		</form> 
	</div>
	<div id="display_content">	
	</div>	
</body>

</html>