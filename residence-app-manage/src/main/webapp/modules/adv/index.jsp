<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>广告管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/ImgViewJs/css/viewer.min.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/modules/adv/styles/adv.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/adv/js/adv.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ImgViewJs/js/viewer.min.js"></script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" fit="true">
				<div data-options="region:'north'" style="height:35px;text-align:center;padding-top:5px;">
                   	广告发布区域：<input id="areaNameSearch" class="easyui-textbox" style="width:100px;height:22px;" type="text" />
					&nbsp;&nbsp;
					<a id="search" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:60px;height:22px;">查询</a>
					&nbsp;&nbsp;
					<a id="reset" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:60px;height:22px;">重置</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="广告列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="id" width="100" align="center" hidden="true"></th>
								<th field="areaIdList" width="100" align="center" hidden="true"></th>
								<th field="advImgUrl" width="100" align="center" formatter="formatImg">广告图片</th>
								<th field="advDesc" width="200" align="center" hidden="true">广告描述</th>
								<th field="redirectUrl" width="250" align="center">图片跳转链接</th>
								<th field="areaNameList" width="200" align="center" hidden="true">广告发布区域</th>
								<th field="valid" width="100" align="center">是否发布</th>
								<th field="creator" width="100" align="center">创建者</th>
								<th field="createTime" width="150" align="center">创建时间</th>								
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 添加广告  -->
	<div id="divWindow" style="padding:10px;">
		<form name="addForm" id="addForm" class="setting-profile-form" method="post" enctype="multipart/form-data">
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right">选择广告图片投放区域：</td>
					<td align="left">
						<input name="areaId" id="areaId"  class="easyui-combotree" style="width:360;"
							data-options="
									    editable:false,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				 method:'post'	"
						   	multiple 			   		
						 />						
					</td>					
				</tr>
				<tr>
					<td align="right">广告图片：</td>
					<td align="left">
						<input class="easyui-filebox" name="advImg" id="advImg" size="48" width="360" maxlength="10" data-options="required:true,prompt:'请选择一个图片文件...'" buttonText="浏览" />
					<font color="red">*</font>
					</td>				
				</tr>
				<tr>
					<td align="right">图片描述（字数限制200位）：</td>
					<td align="left"><textarea name="advDesc" id="advDesc" width="100%" maxlength="200"
							style="border:0px" rows="5" cols="40" class="textarea"></textarea>
					</td>
				</tr>
				<tr>
					<td align="right">图片跳转链接：</td>
					<td align="left">
						<input name="redirectUrl" id="redirectUrl"
							style="width:285px" class="easyui-textbox"/>
					</td>
				</tr>
				<tr>
					<td align="right">是否发布：</td>
					<td align="left">
						<select  name="advValid"  id="advValid" class="easyui-combobox" style="width:100px;" panelHeight="auto"  data-options="required:true,editable:false">   
						    <option value="1">是</option>
						    <option value="0">否</option>   						       
						</select>
						&nbsp;
						<font color="red">*</font>
					</td>	
				</tr>			
				<tr>
					<td align="center" colspan="2">
						<input type="button" name="btnSave" id="btnSave" class="button1" value="上传" />&nbsp; 
						<input type="button" name="btnCancel" id="btnCancel" class="button1" value="取消" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	
	<!-- 修改广告信息  -->
	<div id="divWindow1" style="padding:10px;">
		<form name="addForm1" id="addForm1" class="setting-profile-form" method="post" enctype="multipart/form-data">
			<div style="display: none">
				<tr>
					<td align="left"><input type="text" name="advId1" id="advId1"
					class="easyui-textbox" data-options="required:true"/>
				</tr>
			</div>
			
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">								
				<tr>
					<td align="right">选择广告图片投放区域：</td>
					<td align="left">
						<input name="areaId1" id="areaId1"  class="easyui-combotree" style="width:360;"
							data-options="
									    editable:false,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				 method:'post'	"
						   	multiple 			   		
						 />						
					</td>					
				</tr>
				<tr>
					<td align="right">广告图片:</th>
					<td align="left">
						<img id="advImg1" src="" alt="" width="100" height="100" align="middle"/>
					</td>
				</tr>
				<tr>
					<td align="right">图片描述：</td>
					<td align="left"><textarea name="advDesc1" id="advDesc1" width="100%" style="border:0px"
							maxlength="200" rows="5" cols="60" class="textarea"></textarea>
					</td>
				</tr>
				<tr>
					<td align="right">图片跳转链接：</td>
					<td align="left">
						<input name="redirectUrl1" id="redirectUrl1"
							style="width:285px" class="easyui-textbox"/>
					</td>
				</tr>
				<tr>
					<td align="right">是否发布：</td>
					<td align="left">
						<select  name="advValid1"  id="advValid1" class="easyui-combobox" style="width:100px;" panelHeight="auto"  data-options="required:true,editable:false">   
						    <option value="1">是</option>
						    <option value="0">否</option>   						       
						</select>
						&nbsp;
						<font color="red">*</font>
					</td>	
				</tr>			
				<tr>
					<td align="center" colspan="2">
						<input type="button" name="btnSave1" id="btnSave1" class="button1" value="上传" />&nbsp; 
						<input type="button" name="btnCancel1" id="btnCancel1" class="button1" value="取消" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	
	<!--详情显示  -->
	<div id="divWindow2" style="padding:10px;">
		<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab" >
			<tr>
				<td align="right" width="100">广告图片投放区域：</td>
				<td align="left">
					<textarea  id="areaId2" width="100%" style="border:0px"
						rows="5" cols="60" class="textarea"></textarea>
				</td>
			</tr>
			<tr>
				<td align="right" width="100">广告图片：</td>
				<td align="left">
					<img id="advImg2" src="" alt="" width="100" height="100" align="middle"/>
				</td>
			</tr>
			<tr>
				<td align="right" width="100">图片描述：</td>
				<td align="left">
					<textarea  id="advDesc2" width="100%" style="border:0px"
						rows="5" cols="60" class="textarea"></textarea>
				</td>
			</tr>
			<tr>
				<td align="right" width="100">图片跳转链接：</td>
				<td align="left" id="redirectUrl2" ></td>
			</tr>
			<tr>
				<td align="right" width="100">是否发布：</td>
				<td align="left" id="advValid2" ></td>
			</tr>
			<tr>
				<td align="right" width="100">创建者：</td>
				<td align="left" id="creator2"></td>
			</tr>
			<tr>
				<td align="right" width="100">创建时间：</td>
				<td align="left" id="createTime2"></td>
			</tr>			
		</table>
	</div>
</body>
</html>