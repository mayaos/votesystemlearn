<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>商家优惠信息表管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/modules/merchant/benefit/styles/merchantBenefit.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/merchant/benefit/js/merchantBenefit.js"></script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" fit="true">
				<div data-options="region:'north'"style="height:35px;text-align:center;padding-top:5px;">
					优惠信息：<input name="benefitName1" id="benefitName1" class="easyui-textbox" style="width:100px;height:22px;line-height:22px;">
					优惠商家：<input name="merchantName1" id="merchantName1" class="easyui-textbox" style="width:100px;height:22px;line-height:22px;">
					创建起止时间：
					<input name="startTime" id="startTime" class="easyui-datetimebox" data-options="editable:false" style="width:150px;"/> 
					--
					<input name="endTime" id="endTime" class="easyui-datetimebox" data-options="editable:false" style="width:150px;"/>
					<a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">查询</a>
					<a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">返回</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="商家优惠信息表列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="benefitId" width="80" align="center"  hidden="true">优惠信息Id</th>
								<th field="benefitName" width="160" align="left">优惠信息</th>
								<th field="merchantName" width="160" align="left">优惠商家</th>
								<th field="benefitQuota" width="80" align="center">优惠额度</th>
								<th field="benefitCount" width="80" align="center">优惠数量</th>
								<th field="useExplain" width="100" align="left">使用条件</th>
								<th field="limitedCount" width="100" align="center">限制领取/人</th>
								<th field="recommendFlag" width="60" align="center">是否推荐</th>
								<th field="createTime" width="125" align="center">创建时间</th>
								<th field="changeTime" width="125" align="center">修改时间</th>
								<th field="validTime" width="125" align="center">有效期</th>	
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 添加/修改 -->
	<div id="divWindow" style="padding:10px;">
		<form name="addForm" id="addForm" method="post" enctype="multipart/form-data">
			<div style="display: none">
				<input class="easyui-textbox" type="hidden" name="benefitId" id="benefitId"/>
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr id="area">
					<td align="right" width="180">选择地区：</td>
					<td align="left">
						城市 <select class="easyui-combobox" id="cityID" name="cityID" style="width:120px;"
							data-options="editable:false,
	            			panelHeight:'auto',
	                    	valueField:'areaID',
	                    	textField:'areaName',
	                    	url:Common.getUrl('areactrl/selectAreaList?flag=2')">
						</select>&nbsp;
						区县 <select class="easyui-combobox" id="areaID" name="areaID" style="width:120px;"
							data-options="editable:false,panelHeight:'auto',valueField:'areaID',textField:'areaName'"></select>
					</td>
				</tr>
				<tr id="trIndustryType">
					<td align="right" width="180">行业类型：</td>
					<td align="left">
						<select class="easyui-combobox" id="industryType" name="industryType" style="width:120px;"
							data-options="editable:false,required:true,
	            			panelHeight:'auto',
	                    	valueField:'codeValue',
	                    	textField:'codeName',
	                    	url:Common.getUrl('codectrl/createComboBox?codeType=industryType') ">
						</select>&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<tr id="trMerchantNature">
					<td align="right" width="180">选择商家性质：</td>
					<td align="left">
						<select class="easyui-combobox" id="merchantNature" name="merchantNature" style="width:120px;">
							<option value ="0">直营店</option>
							<option value ="1">连锁店总店</option>
							<option value ="2">连锁店分店</option>
						</select>&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td align="right" width="180">商家：</td>
					<td align="left">
						 <select class="easyui-combobox" id="merchantId" name="merchantId" style="width:180px;"
							data-options="editable:false,required:true,
	            			panelHeight:'auto',
	                    	valueField:'merchantId',
	                    	textField:'merchantName',
	                    	url:Common.getUrl('merchantInfoctrl/selectMerchantList')">
						</select>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr id="chainMerchant" style="display:none">
					<td align="right" width="180">选择可以在那些连锁分店使用：</td>
					<td align="left">
						<div id="chainBranch">请选择商家!</div>
					</td>
				</tr>
				<tr>
					<td align="right" width="180">优惠信息名称：</td>
					<td align="left"><input type="text" name="benefitName" id="benefitName"
						size="46" maxlength="50" class="easyui-textbox"
						data-options="required:true,validType:'length[1,50]'" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" width="180">优惠额度：</td>
					<td align="left"><!-- <input type="text" name="benefitQuota" id="benefitQuota"
						size="30" maxlength="10" class="easyui-textbox"
						data-options="required:true"/> -->
						<input i class="input easyui-numberbox" data-options="required:true,validType:'length[1,11]'" min="0.01"  max="99999999.99" precision="2" type="text" name="benefitQuota" id="benefitQuota"/>
						&nbsp;<font color="red">*</font></td>
				</tr>
				<tr id="coupon">
					<td align="right" width="180">
						优惠卷定义：<br/>
					          导入&nbsp;<input type="checkbox" id="importCheck" name="importCheck" value="true">&nbsp;&nbsp;
					</td>
					<td align="left">
						<div id="define">
							<div style="float:left;">
								优惠券码规则：<br />
								<input type="text" name="couponRule" id="couponRule" size="20" maxlength="20" class="easyui-textbox" data-options="required:true"/>
								<font color="red">*</font>
							</div>
							<div style="float:left;padding-left:10px;">
								序列范围：<br />
								<input type="text" name="couponSeqStart" id="couponSeqStart" size="15" maxlength="20" class="easyui-textbox" data-options="required:true"/>
								-
								<input type="text" name="couponSeqEnd" id="couponSeqEnd" size="12" maxlength="10" class="easyui-textbox" data-options="required:true"/>
								<font color="red">*</font>
							</div>
						</div>
						<div id="import">
							<input class="easyui-filebox" name="couponFile" id="couponFile" data-options="required:true,buttonText:'选择文件',buttonAlign:'left'" style="width:280px;" validtype="image"/>&nbsp;&nbsp;<font style="color:red"></font>
						</div>
					</td>
				</tr>
				<tr>
					<td align="right" width="180">使用条件：</td>
					<td align="left"><input type="text" name="useExplain" id="useExplain"
						size="30" maxlength="50" class="easyui-textbox"
						data-options="required:true,validType:'length[1,30]'"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" width="180">限制领取/人：</td>
					<td align="left"><!-- <input type="text" name="limitedCount" id="limitedCount"
						size="30" maxlength="50" class="easyui-textbox" 
						data-options="required:true"/> -->
						<input i class="input easyui-numberbox" data-options="required:true,validType:'length[1,2]'" min="1"  max="99" precision="0" type="text" name="limitedCount" id="limitedCount"/>
						&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right" width="180">优惠描述：</td>
					<td align="left">
						<textarea name="description" id="description" rows="3" cols="60" maxlength="255" style="white-space:normal;" class="textarea"></textarea>
					</td>
				</tr>
				<tr>
					<td align="right">有效时间：</td>
					<td align="left"><input type="text" name="validTime" id="validTime" data-options="required:true"
						size="30" maxlength="20" class="easyui-datebox" />&nbsp;<font color="red">*</font></td>
				</tr>
				<tr>
					<td align="right">优惠照片:</th>
					<td align="left">
						<ul id="benefitViewer">
							<li style="padding-left:10px;float:left"><input class="easyui-filebox" name="benefitImage2" id="benefitImage2" data-options="buttonText:'选择文件',buttonAlign:'left'" style="width:280px;" validtype="image"/></li>
							<img id="benefitImage21" src="" alt="" width="20" height="20" align="middle"/></li>
						</ul>
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
	<div id="couponWindow" style="padding:10px;">
		<table id="couponList" class="easyui-datagrid"
			data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
			<thead>
				<tr>
					<th field="couponId" width="100" align="center" hidden="true">ID</th>
					<th field="couponCode" width="200" align="center">优惠券码</th>								
					<th field="status" width="100" align="center">状态</th>		
				</tr>
			</thead>
		</table>
	</div>
</body>
</html>