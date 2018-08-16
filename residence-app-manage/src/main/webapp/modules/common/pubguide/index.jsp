<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>指南信息表管理</title>
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
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/pubguide/js/pubguide.js"></script>
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
					区域：<input id="areaIdSearch" name="areaIdSearch"  class="easyui-combotree" style="width:180px;"
						data-options="
						    editable:false,
						    url:Common.getUrl('areactrl/addrTreelist'), 
			   				 method:'post'"			   		
					 />&nbsp;
					 指南类别：<select id="guideTypeSearch" name="guideTypeSearch" class="easyui-combobox" style="width:120px;"
							data-options="editable:false,
	            			panelHeight:'auto',
	                    	valueField:'codeValue',
	                    	textField:'codeName',
	                    	url:Common.getUrl('codectrl/createComboBox?codeType=guideType') ">
						</select>
					是否有效：
					<select id="issueOrNotSearch" name="issueOrNotSearch" class="easyui-combobox" data-options="editable:false,panelHeight:'auto'" style="width:80px;">
						<option value=""></option>
						<option value="YES">是</option>
						<option value="NO">否</option>
		        	</select>&nbsp;
					 创建时间：
					 <input  name="createTime1" id="createTime1" style="width:180px" class="easyui-datebox" data-options="editable:false"/> 
					<a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">查询</a>
					<a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options=""style="width:60px;height:22px;">清空</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="指南信息表列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="areaName" width="150" align="center">区域</th> 
								<th field="guideName" width="100" align="center">指南类别</th>
								<th field="userName" width="100" align="center">添加作者</th>
								<th field="valid" width="100" align="center">是否有效</th>
							 	<th field="readTimes" width="100" align="center">阅读次数</th> 
								<th field="createTime" width="150" align="center">创建时间</th>
								<th field="updateTime" width="150" align="center">修改时间</th>
								<th field="guideContent" width="80" align="center" formatter="functionLink">查看内容</th>
								<th field="guideId" width="150" align="center" hidden="true">ID</th>
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
					<td align="right" width="100">指南类别：</td>
					<td align="left">
					<select class="easyui-combobox" id="guideType" name="guideType" style="width: 285px;"
					data-options="editable:false,
            		panelHeight:'auto',
            		required:true,
                    valueField:'codeValue',
                    textField:'codeName',
                    url:Common.getUrl('codectrl/createComboBox?codeType=guideType') ">
					</select>
			 		&nbsp;<font color="red">*</font></td>
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
						 />&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right">指南内容：</td>
					<td align="left">
						<div id="add_content"></div>
					</td>
				</tr>
		
				<tr>
					<td align="right">是否有效：</td>
					<td align="left">
						<select name="valid" id="valid" class="easyui-combobox" style="width:285px;"
						data-options="editable:false,panelHeight:'auto',required:true" >
							<option value="YES">有效</option>
							<option value="NO">无效</option>
					    </select>&nbsp;<font color="red">*</font>
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
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="guideId2" id="guideId2"/>
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">指南类别：</td>
					<td align="left">
					<select class="easyui-combobox" id="guideType2" name="guideType2" style="width:285px;"
					data-options="editable:false,
            		panelHeight:'auto',
            		required:true,
                    valueField:'codeValue',
                    textField:'codeName',
                    url:Common.getUrl('codectrl/createComboBox?codeType=guideType') ">
					</select>&nbsp;<font color="red">*</font>
			        </td>
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
						 /><div id="areaIdHidden" style="display:none"></div>&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right">指南内容：</td>
					<td align="left">
						<div id="edit_content"></div>
					</td>
				</tr>
		
				<tr>
					<td align="right">是否有效：</td>
					<td align="left">
						<select name="valid2" id="valid2" class="easyui-combobox" style="width:285px;"
						data-options="editable:false,panelHeight:'auto',required:true" >
							<option value="YES">有效</option>
							<option value="NO">无效</option>
					    </select>&nbsp;<font color="red">*</font>
					</td>
				</tr>
			<!-- 	<tr>
					<td align="right">创建时间：</td>
					<td align="left"><input  name="createTime2" id="createTime2"
						style="width:285px" class="easyui-datebox" data-options="required:true,editable:false"/>&nbsp;<font color="red">*</font></td>
				</tr> -->
				<tr>
					<td align="center" colspan="2">
					<input type="button" name="btnSave" id="btnSave2" class="button1" value="保存"/>&nbsp;
					<input type="button" name="btnCancel" id="btnCancel2" class="button1" value="取消" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="display_content" ></div>
</body>
</html>