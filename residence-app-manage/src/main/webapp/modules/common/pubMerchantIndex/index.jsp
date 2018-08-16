
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>商家服务首页显示管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/modules/common/pubMerchantIndex/styles/pubMerchantIndex.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/pubMerchantIndex/js/pubMerchantIndex.js"></script>
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
					资讯类型：<select class="easyui-combobox" name="merchantIndexType" id="merchantIndexType1" style="width:120px;“>
							<option selected="selected" disabled="disabled"  style='display: none' value=''></option>
							<option value ="0">功能介绍</option>
							<option value ="1">旅游文章</option>
						</select>
					资讯标题：<input name="merchantIndexTitle" id="merchantIndexTitle1" class="easyui-textbox"style="width:100px;height:22px;line-height:22px;"/>
					 创建时间：<input  name="createTime" id="createTime1"
						style="width:180px" class="easyui-datebox" data-options="editable:false"/> 
					 发表时间：<input  name="issueTime" id="issueTime1"
						style="width:180px" class="easyui-datebox" data-options="editable:false"/> 
					 <a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">查询</a>
					  <a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options=""style="width:60px;height:22px;">返回</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="商家服务首页显示列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="areaIdList" width="1" align="center" hidden="true"></th>
								<th field="merchantIndexTypeValue" width="1" align="center" hidden="true"></th>
								<th field="merchantIndexType" width="100" align="center">所属类型</th>
								<th field="areaNameList" width="100" align="center">区域</th>
								<th field="merchantIndexPic" width="100" align="center" formatter="functionIcon">文章标题图片</th>
								<th field="merchantIndexTitle" width="100" align="center">标题</th>
								<th field="merchantIndexDesc" width="100" align="center">描述</th>
								<th field="userName" width="100" align="center">发布作者</th>
								<th field="issueTime" width="100" align="center">发表时间</th>
								<th field="createTime" width="100" align="center">创建时间</th>
								<th field="updateTime" width="100" align="center">更新时间</th>
								<th field="rank" width="100" align="center">排序</th>		
								<th field="link" width="200" align="center">链接</th>	
								<th field="merchantIndexId" width="100" align="center" hidden="true">ID</th>
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
					<td align="right" width="100">资讯类型：</td>
					<td align="left">
						<div>
							<input type="radio" id="merchantIndexType11" name="merchantIndexType1" value="0" />功能介绍
							<input type="radio" id="merchantIndexType12" name="merchantIndexType1" value="1"/>旅游文章				
						</div>						
					</td>
				</tr>
				<tr>
					<td align="right">所属区域：</td>
					<td align="left">
						<input name="areaId" id="areaId"  class="easyui-combotree" style="width:360;"
							data-options="
									    editable:false,
									    required:true,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				 method:'post'	"
						   	multiple 			   		
						 />	&nbsp;<font color="red">*</font>					
					</td>					
				</tr>
				<tr>
					<td align="right" width="100">标题</td>
					<td align="left"><input type="text" name="merchantIndexTitle" id="merchantIndexTitle"
						size="38" maxlength="10" class="easyui-textbox"
						data-options="required:true,validType:'length[1,50]'" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr id="merchantIndexImgAll">
					<td align="right" width="100">标题图片</td>
					<td align="left"><input type="file" name="merchantIndexImg" id="merchantIndexImg" class="easyui-validatebox" style="width:230px;border:1px" data-options="required:true"></input>&nbsp;<font color="red">普通文章格式：194x140，Banner文章格式：720x300*</font></td>
				</tr>
								
				<tr>
					<td align="right" width="100">发表时间</td>
					<td align="left"><input type="text" name="issueTime" id="issueTime"
						size="38" maxlength="10" class="easyui-datebox"
						data-options="required:true,editable:false" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr id="merchantIndexDescAll">
					<td align="right">描述：</td>
					<td align="left">
						<textarea name="merchantIndexDesc" id="merchantIndexDesc" rows="3" cols="60" maxlength="500" style="white-space:normal;" class="textarea"></textarea>
					</td>
				</tr> 
				<tr>
					<td align="right">排序：</td>
					<td align="left"><input type="text" name="rank" id="rank"
						size="38" maxlength="10" class="easyui-textbox"
						data-options="required:true,validType:'length[1,50]'" />&nbsp;<font color="red">按1,2,3...的顺序进行排序，默认999，可自己修改排序*</font></td>					
				</tr>
				<tr>
					<td align="right">链接：</td>
					<td align="left"><input type="text" name="link" id="link"
						size="38" maxlength="10" class="easyui-textbox"
						data-options="required:true" />&nbsp;<font color="red">*</font></td>					
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
		<form name="addForm2" id="addForm2" method="post" action="" enctype="multipart/form-data">
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">资讯类型：</td>
					<td align="left">
						<div>
							<input type="radio" id="merchantIndexType21" name="merchantIndexType2" value="0" />功能介绍
							<input type="radio" id="merchantIndexType22" name="merchantIndexType2" value="1"/>旅游文章				
						</div>
					</td>
				</tr>
				<tr>
					<td align="right">所属区域：</td>
					<td align="left">
						<input name="areaId2" id="areaId2"  class="easyui-combotree" style="width:360;"
							data-options="
									    editable:false,
									    required:true,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				 method:'post'	"
						   	multiple 			   		
						 />	&nbsp;<font color="red">*</font>					
					</td>					
				</tr>
				<tr>
					<td align="right" width="100">标题</td>
					<td align="left"><input type="text" name="merchantIndexTitle2" id="merchantIndexTitle2"
						size="38" maxlength="10" class="easyui-textbox"
						data-options="required:true,validType:'length[1,50]'" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr id="merchantIndexImgAll2">
					<td align="right" width="100">标题图片</td>
					<td align="left"><input type="file" name="merchantIndexImg2" id="merchantIndexImg2" class="easyui-validatebox" style="width:230px;border:1px"></input><font color="red">普通文章格式：194x140，Banner文章格式：720x300*</font></td>
				</tr>
 				<tr id="merchantIndexDescAll2">
					<td align="right">描述：</td>
					<td align="left">
						<textarea name="merchantIndexDesc2" id="merchantIndexDesc2" rows="3" cols="60" maxlength="500" style="white-space:normal;" class="textarea"></textarea>
					</td>
				</tr> 
				<tr>
					<td align="right">排序：</td>
					<td align="left"><input type="text" name="rank2" id="rank2"
						size="38" maxlength="10" class="easyui-textbox"
						data-options="required:true,validType:'length[1,50]'" />&nbsp;<font color="red">*</font></td>				
				</tr>
				<tr>
					<td align="right">链接：</td>
					<td align="left"><input type="text" name="link2" id="link2"
						size="38" maxlength="10" class="easyui-textbox"
						data-options="required:true" />&nbsp;<font color="red">*</font></td>					
				</tr>									
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