<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>轻应用文章详情管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/modules/common/orgarticle/styles/orgarticle.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/orgarticle/js/orgarticle.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ckeditor/ckeditor.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ckeditor/samples/js/sample.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ckeditor/plugin.js"></script>
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
					&nbsp;文章作者：<input name="authorSearch" id="authorSearch" class="easyui-textbox"style="width:100px;height:22px;line-height:22px;"/>
					&nbsp;选择区域：<input name="areaIdSearch" id="areaIdSearch" class="easyui-combotree" style="width:180px;"
						data-options="
						    editable:false,
						    url:Common.getUrl('areactrl/addrTreelist'), 
			   				 method:'post'"			   		
					 />&nbsp;
					菜单名：<select class="easyui-combobox" id="menuIdSearch" name="menuIdSearch" style="width:120px;"
						data-options="editable:false,
            			panelHeight:'200',
                    	valueField:'menuId',
                    	textField:'menuName',
                    	url:Common.getUrl('orgMenuctrl/createCombobox?menuStatus=1')">
					</select>
					&nbsp;是否发布：
					<select class="easyui-combobox" id="issueOrNotSearch" name="issueOrNotSearch" data-options="editable:false,panelHeight:'auto'" style="width:80px;">
						<option value=""></option>
						<option value="1">是</option>
						<option value="0">否</option>
		        	</select>
					&nbsp;发布日期:<input id="issueDateSearch"	name="issueDateSearch" class="easyui-datebox"	data-options="editable:true" style="width:150px">
					 <a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">查询</a>
					  <a id="reset" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">重置</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="轻应用文章信息列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="articleId" width="100" align="center" hidden="true">文章ID</th>
								<th field="orgId" width="50" align="center"  hidden="true">机构ID</th>
								<th field="menuId" width="50" align="center"  hidden="true">菜单ID</th>
								<th field="areaName" width="80" align="center">区域名称</th>
								<th field="menuName" width="80" align="center">菜单名称</th>
								<th field="orgName" width="80" align="center">机构名称</th>
								<th field="articleTitle" width="280" align="center">文章标题</th>
								<th field="articleAuthor" width="80" align="center">文章作者</th>
								<th field="readTimes" width=60 align="center">浏览次数</th>
								<th field="likeTimes" width="60" align="center">点赞次数</th>
								<th field="issueFlag" width="60" align="center">是否发布</th>
								<th field="userCode" width="100" align="center">发布人</th>
								<th field="issueTime" width="150" align="center">发布时间</th>
								<th field="createTime" width="150" align="center">创建时间</th>
								<th field="updateTime" width="150" align="center">修改时间</th>
								<th field="articleContent" width="80" align="center" formatter="functionLink">查看内容</th>
							</tr>
						</thead>
					</table>
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
					<td align="right" width="100">选择区域：</td>
					<td align="left">
						<input name="areaId" id="areaId" class="easyui-combotree" style="width:180px;"
							data-options="
									    editable:false,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				 method:'post'	"			   		
						 />						
					</td>
				</tr>
				<tr>
					<td align="right" width="100">菜单名：</td>
					<td align="left">
					<select class="easyui-combobox" id="menuId" name="menuId" style="width:120px;"
							data-options="editable:false,required:true,
	            			panelHeight:'200',
	                    	valueField:'menuId',
	                    	textField:'menuName',
	                    	url:Common.getUrl('orgMenuctrl/createCombobox?menuStatus=1')">
						</select><font color="red">*</font>
			 		</td>
				</tr>
				<tr>
					<td align="right" width="100">机构名称：</td>
					<td align="left">
					<select class="easyui-combobox" id="orgId" name="orgId" style="width: 180px;"
					data-options="editable:false,
            		panelHeight:'200',
            		required:true,
                    valueField:'orgId',
                    textField:'orgName',
                    url:Common.getUrl('orginfoctrl/selectOrgInfoList') ">
					</select>
			 		&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">文章标题：</td>
					<td align="left"><input name="articleTitle" id="articleTitle"
						style="width:285px" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">文章作者：</td>
					<td align="left"><input name="articleAuthor" id="articleAuthor"
						style="width:285px" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">文章来源：</td>
					<td align="left"><input name="articleFrom" id="articleFrom"
						style="width:285px" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">文章标题图片：</td>
					<td align="left">
						<input id="topicPic" name="topicPic" class="easyui-filebox" style="width: 250px;" data-options="required:true" />&nbsp;<font color="red">*&nbsp;建议上传图片像素为 600*270</font>
					</td>				
				</tr>
				<tr>
					<td align="right">文章描述：</td>
					<td align="left">
<!-- 					<input name="articleDesc" id="articleDesc" -->
<!-- 						style="width:285px" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font> -->
						<textarea name="articleDesc" id="articleDesc" rows="3" cols="60" maxlength="500" style="white-space:normal;" class="textarea">>&nbsp;<font color="red">*</font></textarea>
					</td>
				</tr>				
				<tr>
					<td align="right">选择文章形式:</td>
					<td align="left">
						<div>
							<input type="radio" id="articleType1" name="articleType" value="0" />内容
							<input type="radio" id="articleType2" name="articleType" value="1"/>链接					
						</div>
					</td>
				</tr>
				<tr id="content">
					<td align="right">文章内容：</td>
					<td align="left">
						<div id="add_content"></div>
					</td>
				</tr>
				<tr id="link">
					<td align="right">文章链接：</td>
					<td align="left">
						<input name="articleLink" id="articleLink"
						style="width:285px" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font>
					</td>
				</tr>				
				<tr>
					<td align="right">文章发表时间：</td>
					<td align="left"><input name="issueTime1" id="issueTime1"
						style="width:150px" class="easyui-datebox" data-options="required:true,editable:false"/>&nbsp;<font color="red">*</font></td>
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
		<form name="editForm" id="editForm" method="post" action="" enctype="multipart/form-data">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="id" id="id" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">选择区域：</td>
					<td align="left">
						<input name="areaId2" id="areaId2" class="easyui-combotree" style="width:180px;"
							data-options="
									    editable:false,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				 method:'post'	"			   		
						 />	<div id="areaIdHidden" style="display:none"></div>			
					</td>
				</tr>
				<tr>
					<td align="right" width="100">菜单名：</td>
					<td align="left">
			 		<select class="easyui-combobox" id="menuId2" name="menuId2" style="width:120px;"
							data-options="editable:false,required:true,
	            			panelHeight:'200',
	                    	valueField:'menuId',
	                    	textField:'menuName',
	                    	url:Common.getUrl('orgMenuctrl/createCombobox?menuStatus=1')">
						</select><font color="red">*</font></td>
				</tr>
				<tr>
					<td style="display:none"><input name="articleId2" id="articleId2" style="width:25px" class="easyui-textbox" /></td>
					<td align="right" width="100">机构名称：</td>
					<td align="left">
					<select class="easyui-combobox" id="orgId2" name="orgId2" style="width: 180px;"
					data-options="editable:false,
            		panelHeight:'200',
            		required:true,
                    valueField:'orgId',
                    textField:'orgName',
                    url:Common.getUrl('orginfoctrl/selectOrgInfoList') ">
					</select>
			 		&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">文章标题：</td>
					<td align="left"><input name="articleTitle2" id="articleTitle2"
						style="width:285px" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">文章作者：</td>
					<td align="left"><input name="articleAuthor2" id="articleAuthor2"
						style="width:285px" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">文章来源：</td>
					<td align="left"><input name="articleFrom2" id="articleFrom2"
						style="width:285px" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">文章标题图片：</td>
					<td align="left">
						<input id="topicPic2" name="topicPic2" class="easyui-filebox" style="width: 250px;" /> &nbsp;<font color="red">&nbsp;建议上传图片像素为 600*270</font>
					</td>				
				</tr>
				<tr>
					<td align="right">文章描述：</td>
					<td align="left">
<!-- 					<input name="articleDesc2" id="articleDesc2" -->
<!-- 						style="width:285px" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font> -->
						<textarea name="articleDesc2" id="articleDesc2" rows="3" cols="60" maxlength="500" style="white-space:normal;" class="textarea">>&nbsp;<font color="red">*</font></textarea>
					</td>
				</tr>
				<tr>
					<td align="right">选择文章形式:</td>
					<td align="left">
						<div>
							<input type="radio" id="article2Type1" name="articleType2" value="0" />内容
							<input type="radio" id="article2Type2" name="articleType2" value="1"/>链接					
						</div>
					</td>
				</tr>				
				<tr id="content2">
					<td align="right">文章内容：</td>
					<td align="left">
						<div id="edit_content"></div>
					</td>
				</tr>
				<tr id="link2">
					<td align="right">文章链接：</td>
					<td align="left">
						<input name="articleLink2" id="articleLink2"
						style="width:285px" class="easyui-textbox" data-options="required:true"/>&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right">文章发表时间：</td>
					<td align="left"><input name="issueTime2" id="issueTime2"
						style="width:150px" class="easyui-datebox" data-options="required:true,editable:false"/>&nbsp;<font color="red">*</font></td>
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
	<div id="display_content">
		
	</div>	
	
</body>
</html>