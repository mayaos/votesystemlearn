<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>商家vip信息表管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/ImgViewJs/css/viewer.min.css">
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/modules/merchant/vip/styles/merchantVip.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ImgViewJs/js/viewer.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/validator.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/merchant/vip/js/merchantVip.js"></script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" fit="true">
				<div data-options="region:'north'"style="height:35px;text-align:center;padding-top:5px;">
					商家名称：<input name="merchantName" id="merchantName" class="easyui-textbox"style="width:100px;height:22px;line-height:22px;">
					区域名称：<input name="areaName" id="areaName" class="easyui-textbox"style="width:100px;height:22px;line-height:22px;">
					 <a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">查询</a>
					  <a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">返回</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="商家信息表列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="vipcardId" width="20" align="center" hidden="true">会员卡ID</th>
								<th field="areaId" width="20" align="center"  hidden="true">区域ID</th>
								<th field="areaName" width="100" align="center">区域名</th>	
								<th field="merchantName" width="160" align="center">商家名称</th>				
								<th field="vipNoType" width="100" align="center">会员卡号类型</th>						
								<th field="vipDesc" width="300" align="center">会员卡描述</th>
								<th field="vipRule" width="300" align="center">会员卡使用规则</th>	
								<th field="createTime" width="150" align="center">生成时间</th>
								<th field="changeTime" width="150" align="center">修改时间</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 添加/修改  -->
	<div id="divWindow" style="padding:10px;">
		 <form name="addForm" id="addForm" method="post" enctype="multipart/form-data">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="vipcardId" id="vipcardId" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
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
					</td><div id="areaIdHidden" style="display:none"></div>
				</tr>
				<tr>
					<td align="right" width="100">商家：</td>
					<td align="left">
						 <select class="easyui-combobox" id="merchantId" name="merchantId" style="width:180px;"
							data-options="editable:false,required:true,
	            			panelHeight:'auto',
	                    	valueField:'merchantId',
	                    	textField:'merchantName',
	                    	url:Common.getUrl('merchantInfoctrl/selectMerchantList')">
						</select>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" width="100">会员卡号类型：</td>
					<td align="left">
						<select  name="vipNoType"  id="vipNoType" class="easyui-combobox" style="width:100px;" panelHeight="auto" data-options="required:true, editable:false">   
						    <option value="1">居住证号</option>   
						    <option value="2">身份证号</option>   
						    <option value="3">银行卡号</option>  
						    <option value="4">手机号</option> 
						</select>
						&nbsp;
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">会员卡描述：</td>
					<td align="left">
						<textarea name="vipDesc" id="vipDesc" rows="3" cols="60" maxlength="500" style="white-space:normal;" class="textarea"></textarea>
					</td>
				</tr>
				<tr>
					<td align="right" width="100">会员卡使用规则：</td>
					<td align="left">
						<textarea name="vipRule" id="vipRule" rows="3" cols="60" maxlength="1000" style="white-space:normal;" class="textarea"></textarea>
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
</body>
</html>