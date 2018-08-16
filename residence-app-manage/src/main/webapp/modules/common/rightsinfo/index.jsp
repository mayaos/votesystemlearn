<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>权益信息详情管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/modules/common/rightsinfo/styles/rightsinfo.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/rightsinfo/js/rightsinfo.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ckeditor/ckeditor.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ckeditor/samples/js/sample.js"></script>
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
        	'add_content',
        	{
        		toolbarCanCollapse:true,
        		toolbarStartupExpanded:true,
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
				<div data-options="region:'north'"style="height:35px;text-align:center;padding-top:5px;">
					文章标题：<input name="titleSearch" id="titleSearch" class="easyui-textbox"style="width:100px;height:22px;line-height:22px;"/>
					&nbsp;权益类型：<input name="rightsTypeSearch" id="rightsTypeSearch" class="easyui-textbox"style="width:100px;height:22px;line-height:22px;"/>
					&nbsp;上传日期：<input id="uploadDateStart" name="uploadDateStart" class="easyui-datebox" data-options="editable:true" style="width:150px"/>
					--&nbsp;<input id="uploadDateEnd"	name="uploadDateEnd" class="easyui-datebox"	data-options="editable:true" style="width:150px">
					 <a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">查询</a>
					  <a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">返回</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="权益详情信息列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="areaId" width="100" align="center" hidden="true">区域ID</th>
								<th field="articleId" width="100" align="center" hidden="true">文章ID</th>
								<th field="rightsTypeName" width="100" align="center">权益类型</th>
								<th field="areaName" width="100" align="center">区域名称</th>
								<th field="rightsFrom" width="150" align="center">文章来源</th>
								<th field="rightsTitle" width="150" align="center">文章标题</th>
								<th field="titleImage" width="100" align="center" formatter="functionLogo">文章标题logo</th>
								<th field="issueTime" width="150" align="center">发表时间</th>
								<th field="createTime" width="150" align="center">创建时间</th>
								<th field="userCode" width="100" align="center">创建人</th>
								<th field="rightsContent" width="80" align="center" formatter="functionLink">查看内容</th>
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
				<input class="easyui-textbox" type="hidden" name="id" id="id" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">权益类别：</td>
					<td align="left">
					<select class="easyui-combobox" id="rightsType1" name="rightsType1" style="width: 180px;"
					data-options="editable:false,
            		panelHeight:'auto',
            		required:true,
                    valueField:'rightsId',
                    textField:'rightsName',
                    url:Common.getUrl('rithtsMenuctrl/addrTreelist') ">
					</select>
			 		&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" width="100">选择区域：</td>
					<td align="left">
						<input name="areaId1" id="areaId1" class="easyui-combotree" style="width:180px;"
							data-options="
									    editable:false,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				 method:'post',
						   				 required:true" 
						 />&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right">文章来源：</td>
					<td align="left"><input name="rightsFrom1" id="rightsFrom1" maxlength="5"
						style="width:285px" class="easyui-textbox" data-options="required:true,validType:'length[1,50]'"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">文章标题：</td> 
					<td align="left"><input name="rightsTitle1" id="rightsTitle1" maxlength="50"
						style="width:285px" class="easyui-textbox" data-options="required:true,validType:'length[1,50]'"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">文章标题图片：</td>
					<td align="left">
						<input id="logo1" name="logo1" class="easyui-filebox" style="width: 250px;" data-options="required:true" />&nbsp;<font color="red">*</font>
					</td>				
				</tr>
				<tr>
					<td align="right">权益文章内容：</td>
					<td align="left">
						<div id="add_content"></div>
					</td>
				</tr>
				<tr>
					<td align="right">文章发表时间：</td>
					<td align="left"><input name="issueTime1" id="issueTime1"
						style="width:150px" class="easyui-datetimebox" data-options="required:true,editable:false"/>&nbsp;<font color="red">*</font></td>
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
	<!-- 修改  -->
	<div id="divWindow2" style="padding:10px;">
		<form name="addForm" id="addForm" method="post" action="" enctype="multipart/form-data">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="articleId2" id="articleId2" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">权益类别：</td>
					<td align="left">
						<select class="easyui-combobox" id="rightsType2" name="rightsType2" style="width: 180px;"
						data-options="editable:false,
	            		panelHeight:'auto',
	            		required:false,
	                    valueField:'rightsId',
	                    textField:'rightsName',
	                    url:Common.getUrl('rithtsMenuctrl/addrTreelist') ">
						</select>
			 		</td>
				</tr>
				<tr>
					<td align="right" width="100">选择区域：</td>
					<td align="left">
						<input name="areaId2" id="areaId2" class="easyui-combotree" style="width:180px;"
							data-options="
									    editable:false,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				 method:'post'" 
						 /><div id="areaIdHidden" style="display:none"></div>
					</td>
				</tr>
				<tr>
					<td align="right">文章来源：</td>
					<td align="left"><input name="rightsFrom2" id="rightsFrom2" maxlength="50"
						style="width:285px" class="easyui-textbox" data-options="required:true,validType:'length[1,50]'"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">文章标题：</td>
					<td align="left"><input name="rightsTitle2" id="rightsTitle2" maxlength="50"
						style="width:285px" class="easyui-textbox" data-options="required:true,validType:'length[1,50]'"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">文章标题图片：</td>
					<td align="left">
						<input id="logo2" name="logo2" class="easyui-filebox" style="width: 250px;"  />
					</td>				
				</tr>
				<tr>
					<td align="right">权益文章内容：</td>
					<td align="left">
						<div id="edit_content"></div>
					</td>
				</tr>
				<tr>
					<td align="right">文章发表时间：</td>
					<td align="left"><input name="issueTime2" id="issueTime2"
						style="width:150px" class="easyui-datetimebox" data-options="editable:false"/></td>
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
	<div id="display_content" ></div>	
	
</body>
</html>