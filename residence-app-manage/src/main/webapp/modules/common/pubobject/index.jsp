<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>反馈信息表管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/ImgViewJs/css/viewer.min.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/modules/common/pubobject/styles/pubobject.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/common/pubobject/js/pubobject.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ImgViewJs/js/viewer.min.js"></script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" fit="true">
				<div data-options="region:'north'"style="height:35px;text-align:center;padding-top:5px;">
					状态： <select class="easyui-combobox" id="isDone" name="isDone" style="width: 109px;"
							data-options="editable:false,
	            			panelHeight:'auto',
	                    	valueField:'codeValue',
	                    	textField:'codeName',
	                    	url:Common.getUrl('codectrl/createComboBox?codeType=isDone')">
						</select>
					 反馈开始时间：<input  name="beginDate" id="beginDate"
						style="width:180px" class="easyui-datebox" data-options="editable:false"/>
				         反馈结束时间：<input  name="endDate" id="endDate"
						style="width:180px" class="easyui-datebox" data-options="editable:false"/> 
					<!-- 回馈时间：<input id="codeName1" name="codeName1"class="easyui-textbox" style="width:100px;height:22px;line-height:22px;"> -->
					 <a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">查询</a>
					 <a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options=""style="width:60px;height:22px;">返回</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="回馈信息表列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="userName" width="100" align="center">反馈用户</th>
								<th field="objectionTime" width="100" align="center">反馈时间</th>
								<th field="objectionContent" width="100" align="center">意见内容</th>								
								<th field="replyContent" width="100" align="center">回复内容</th>
								<th field="replyTime" width="100" align="center">回复时间</th>
								<th field="objectionStatus" width="100" align="center">状态</th>	
								<th field="objectionPic1" width="100" align="center" hidden="true">图片1</th>	
								<th field="objectionPic2" width="100" align="center" hidden="true">图片2</th>	
								<th field="objectionPic3" width="100" align="center" hidden="true">图片3</th>	
								<th field="objectionPic4" width="100" align="center" hidden="true">图片4</th>	
								<th field="objectionId" width="100" align="center" hidden="true">ID</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改  -->
	<div id="divWindow2" style="padding:10px;">
		<form name="addForm2" id="addForm2" method="post" action="">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="objectionId2" id="objectionId2"/>
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">

				<tr>
					<td align="right">意见内容：</td>
					<td align="left"><textarea name="objectionContent2" id="objectionContent2" rows="5" cols="40" disabled="disabled" maxlength="255" style="white-space:normal;" class="textarea">
					</textarea>&nbsp;
					<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right">意见图片：</td>
					 <td align="left">
						<ul id="jsImgView">
						   <!--  <li id="jsImgView1" style="padding-left: 10px">点击图片查看原图</li>
							<li id="jsImgView2" style="float:left; padding-left: 10px"><img id="jsImg1" height="200" width="200" src="http://www.jq22.com/demo/jQueryViewer20160329/img/tibet-1.jpg" ></li>
							<li id="jsImgView3" style="float:left; padding-left: 10px"><img id="jsImg2" height="200" width="200" src= "http://www.jq22.com/demo/jQueryViewer20160329/img/tibet-2.jpg"></li>
							<li id="jsImgView4" style="float:left; padding-left: 10px"><img id="jsImg3" height="200" width="200" src="http://www.jq22.com/demo/jQueryViewer20160329/img/tibet-3.jpg" ></li>
							<li id="jsImgView5" style="float:left; padding-left: 10px"><img id="jsImg4" height="200" width="200" src="http://www.jq22.com/demo/jQueryViewer20160329/img/tibet-4.jpg" ></li>   -->
						</ul>
					</td> 
				</tr>
				<tr>
					<td align="right">回复内容：</td>
					<td align="left"><textarea name="replyContent2" id="replyContent2" disabled="disabled" rows="5" cols="40" maxlength="255" style="white-space:normal;" class="textarea"></textarea>&nbsp;
					<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="2">
					<input type="button" name="btnSave2" id="btnSave2" class="button1" value="保存"/>&nbsp;
					<input type="button" name="btnCancel" id="btnCancel2" class="button1" value="取消" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>