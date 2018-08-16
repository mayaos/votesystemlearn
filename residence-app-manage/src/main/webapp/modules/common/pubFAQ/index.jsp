<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>常见问题</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/modules/common/pubguide/styles/pubguide.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/pubFAQ/js/pubFAQ.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ckeditor/ckeditor.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ckeditor/samples/js/sample.js"></script>
<script type="text/javascript">
    window.onload = function()
    {
        CKEDITOR.replace(
        	'add_content',
        	{
        		resize_enabled:false
        	}
        );
        
        CKEDITOR.replace(
        	'edit_content',
        	{
        		resize_enabled:false
        	}
        );
        
        CKEDITOR.replace(
        	'display_content',
        	{
        	 	toolbarCanCollapse:true,
        		toolbarStartupExpanded:false,
		        readOnly:true,
        		height:284 
        		//resize_enabled:false
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
					 创建时间：<input  name="createTime1" id="createTime1"
						style="width:180px" class="easyui-datebox" data-options="editable:false"/> 
					 <a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">查询</a>
					  <a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options=""style="width:60px;height:22px;">返回</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="常见问题信息表列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="id" width="50" align="center">id</th>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="questions" width="400" align="center">问题</th> 
								<th field="answers" width="400" align="center">回答</th>
								<th field="issueFlag" width="50" align="center">是否发布</th>
								<th field="orderNo" width="50" align="center">问题排序</th>
								<th field="createTime" width="150" align="center">创建时间</th>
								<th field="updateTime" width="150" align="center">修改时间</th>
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
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">问题：</td>
					<td align="left"><input name="questions1" id="questions1"
						style="width:500px" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>						
					</td>
				</tr>
				<tr>
					<td align="right">常见问题回答：</td>
					<td align="left">
						<div id="add_content"></div>
					</td>
				</tr>
		
				<tr>
					<td align="right">排序：</td>
					<td align="left"><input name="orderNo1" id="orderNo1"
						style="width:100px" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>						
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

	<!-- 修改  -->
	<div id="divWindow2" style="padding:10px;">
		<form name="addForm2" id="addForm2" method="post" action="">
			<input class="easyui-textbox" type="hidden" name="id2" id="id2"/>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">问题：</td>
					<td align="left"><input name="questions2" id="questions2"
						style="width:500px" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>						
					</td>
				</tr>
				<tr>
					<td align="right">常见问题回答：</td>
					<td align="left">
						<div id="edit_content"></div>
					</td>
				</tr>
		
				<tr>
					<td align="right">排序：</td>
					<td align="left"><input name="orderNo2" id="orderNo2"
						style="width:100px" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>						
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
	<div id="display_content" ></div>
</body>
</html>