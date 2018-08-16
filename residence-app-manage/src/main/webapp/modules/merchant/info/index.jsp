<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>商家信息表管理</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/ImgViewJs/css/viewer.min.css">
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/resources/styles/global.css" />
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/modules/merchant/info/styles/merchantInfo.css" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ImgViewJs/js/viewer.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/validator.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/merchant/info/js/merchantInfo.js"></script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" fit="true">
				<div data-options="region:'north'"style="height:35px;text-align:center;padding-top:5px;">
					商家名称：<input name="merchantName" id="merchantName1" class="easyui-textbox"style="width:100px;height:22px;line-height:22px;">
					 联系人：<input name="contacts" id="contacts1" class="easyui-textbox"style="width:100px;height:22px;line-height:22px;">
					 <a id="search" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">查询</a>
					  <a id="return" href="javascript:void(0);"class="easyui-linkbutton" data-options="iconCls:'icon-search'"style="width:60px;height:22px;">返回</a>
				</div>
				<div data-options="region:'center',border:false">
					<table id="gridList" class="easyui-datagrid" title="商家信息表列表"
						data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
						<thead>
							<tr>
								<th field="checked" width="30" data-options="checkbox:true"></th>
								<th field="merchantId" width="120" align="center">商家ID</th>	
								<th field="merchantName" width="160" align="center">商家名称</th>
								<th field="industryNote" width="100" align="center">行业类型</th>								
								<th field="merchantNote" width="100" align="center">商家类型</th>							
								<th field="merchantNature" width="100" align="center">商家性质</th>	
								<th field="merchantChain" width="80" align="center">连锁类型 </th>						
								<th field="areaName" width="130" align="center">区域</th>
								<th field="contacts" width="100" align="center">联系人</th>
								<th field="telephone" width="100" align="center">电话</th>
								<th field="address" width="200" align="center">地址</th>
								<th field="createTime" width="100" align="center">创建时间</th>	
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
				<input class="easyui-textbox" type="hidden" name="merchantId" id="merchantId" />
			</div>
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
			
				<tr>
					<td align="right" width="100">选择区域：</td>
					<td align="left">
						<input name="areaID" id="areaID" class="easyui-combotree" style="width:180px;"
							data-options="
									    editable:false,
									    url:Common.getUrl('areactrl/addrTreelist'), 
						   				 method:'post'	"			   		
						 />	<div id="areaIdHidden" style="display:none"></div>			
					</td>
				</tr>
				<tr>
					<td align="right" width="100">行业类型：</td>
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
				<tr>
					<td align="right" width="100">商家名称：</td>
					<td align="left">
						<input type="text" name="merchantName" id="merchantName" size="40" class="easyui-textbox"
						data-options="required:true,validType:'length[1,50]'" />&nbsp;<font color="red">*</font>
						<input type="hidden" name="dasPlaceId" id="dasPlaceId" size="10" class="easyui-textbox"/>
						<input type="button" class="buttonDAS" id="searchInDASBtn" value="DAS系统查询商家名称"/>
					</td>
				</tr>
				<tr class="trShowOrNot">
					<td align="right" width="100">商家性质：</td>
					<td align="left">
						<input name="merchantNature" type="radio" value="0"/>直营店  
						<span style="margin-left:10px;"></span>
						<input name="merchantNature" type="radio" value="1" />连锁店
						<span style="margin-left:10px;"></span>
						<font color="red">*</font>
					</td>
				</tr>
				<tr id="chainMerchant" style="display:none">
					<td align="right" width="100">连锁类型：</td>
					<td align="left">
						<input name="chainMerchant" type="radio" value="0" />总店 
						<span style="margin-left:18px;"></span>
						<input name="chainMerchant" type="radio" value="1" />分店
						<span style="margin-left:10px;"></span>
						<font color="red">*</font>
					</td>
				</tr>
				<tr id="merchantHeadBelong" style="display:none">
					<td align="right" width="100">隶属那家连锁总店：</td>
					<td align="left">
						<select class="easyui-combobox" id="merchantHeadName" name="merchantHeadName" style="width:120px;"
						data-options="editable:false,required:true,panelHeight:'auto',valueField:'merchantId',textField:'merchantHeadName'"></select>
						&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<tr class="trShowOrNot">
					<td align="right" width="100">商家类型：</td>
					<td align="left">
						<select class="easyui-combobox" id="merchantType" name="merchantType" style="width:120px;"
						data-options="editable:false,required:true,panelHeight:'auto',valueField:'codeValue',textField:'codeName'"></select>
						&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<tr class="trShowOrNot">
					<td align="right" width="100">联系人：</td>
					<td align="left">
						<input type="text" name="contacts" id="contacts" size="28" class="easyui-textbox"
						data-options="required:true,validType:'length[1,20]'"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr class="trShowOrNot">
					<td align="right" width="100">电话：</td>
					<td align="left"><input type="text" name="telephone" id="telephone"
						size="28" maxlength="20" class="easyui-textbox"
						data-options="required:true" validtype="phoneRex"/>&nbsp;<font color="red">*</font></td>
				</tr>
				<tr class="trShowOrNot">
					<td align="right" width="100">商家地址：</td>
					<td align="left">
						<input type="text" name="address" id="address" size="28" class="easyui-textbox" data-options="validType:'length[0,50]'"/>
					</td>
				</tr>
				<tr class="trShowOrNot">
					<td align="right" width="100">商家描述：</td>
					<td align="left">
						<textarea name="description" id="description" rows="3" cols="60" maxlength="500" style="white-space:normal;" class="textarea"></textarea>
					</td>
				</tr>
				<tr class="trShowOrNot">
					<td align="right">商家Logo:</th>
					<td align="left">
						<ul id="logoViewer">
							<li style="float:left"><input class="easyui-filebox" name="merchantLogo2" id="merchantLogo2"data-options="buttonText:'选择文件',buttonAlign:'left'" style="width:280px;" validtype="image"/></li>
							<li style="padding-left:10px;float:left"><img id="merchantLogo" src="" alt="" width="28" height="28" align="middle"/></li>
						</ul>
					</td>
				</tr>
				<tr class="trShowOrNot">
					<td align="right">商家营业执照:</th>
					<td align="left">
						<ul id="lecenseViewer">
							<li style="float:left"><input class="easyui-filebox" name="lecenseImage2" id="lecenseImage2" data-options="buttonText:'选择文件',buttonAlign:'left'" style="width:280px;" validtype="image"/></li>
							<li style="padding-left:10px;float:left"><img id="lecenseImage" src="" alt="" width="28" height="28" align="middle"/></li>
						</ul>
					</td>
				</tr>
				<tr class="trShowOrNot">
					<td align="right">商家照片1:</th>
					<td align="left">
						<ul id="photo1Viewer">
							<li style="float:left"><input class="easyui-filebox" name="photo12" id="photo12" data-options="buttonText:'选择文件',buttonAlign:'left'" style="width:280px;" validtype="image"/></li>
							<li style="padding-left:10px;float:left"><img id="photo1" src="" alt="" width="28" height="28" align="middle"/></li>
						</ul>
					</td>
				</tr>
				 <tr class="trShowOrNot">
					<td align="right">商家照片2:</th>
					<td align="left">
						<ul id="photo2Viewer">
							<li style="float:left"><input class="easyui-filebox" name="photo22" id="photo22" data-options="buttonText:'选择文件',buttonAlign:'left'" style="width:280px;" validtype="image"/></li>
							<li style="padding-left:10px;float:left"><img id="photo2" src="" alt="" width="28" height="28" align="middle"/></li>
						</ul>
					</td>
				</tr>
				 <tr class="trShowOrNot">
					<td align="right">商家照片3:</th>
					<td align="left">
						<ul id="photo3Viewer">
							<li style="float:left"><input class="easyui-filebox" name="photo32" id="photo32" data-options="buttonText:'选择文件',buttonAlign:'left'" style="width:280px;" validtype="image"/></li>
							<li style="padding-left:10px;float:left"><img id="photo3" src="" alt="" width="28" height="28" align="middle"/></li>
						</ul>
					</td>
				</tr>
				<tr class="trShowOrNot">
					<td align="center" colspan="2">
						<input type="button"name="btnSave" id="btnSave" class="button1" value="保存" />
						<input type="button" name="btnCancel" id="btnCancel" class="button1"value="取消" />
					</td>
				</tr>
			</table>
		</form> 
	</div>
	
	<!-- DAS系统商家信息返回  -->
	<div id="divDASWindow" style="padding:10px;">
		<input type="button" name="btnCancel" id="btnSelectM" class="button1"value="选择" />
		<div class="easyui-layout" fit="true">
				<table id="gridDASMerchantList" 
					data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
					<thead>
						<tr>
							<th field="checked" width="30" data-options="checkbox:true"></th>
							<th field="merchantId" width="100" align="center" hidden="true">Place ID</th>	
							<th field="merchantName" width="252" align="center">商家名称</th>
						</tr>
					</thead>
				</table>
		</div>
	</div>
	
	<!--详情显示  -->
	<div style="margin:20px 0 10px 0;"></div>
	<div id="divWindow2" class="easyui-tabs" style="width:638px; height:385px">
		<!-- 基本信息  -->
		<div title="信息详情" style="padding:10px;">
			<table align="center" cellpadding="0" cellspacing="1" width="100%" class="tab">
				<tr>
					<td align="right" width="100">商家名称：</td>
					<td align="left" id="merchantName2"></td>
				</tr>
				<tr>
					<td align="right" width="100">选择地区：</td>
					<td align="left" id="areaName"></td>
				</tr>
				<tr>
					<td align="right" width="100">行业类型：</td>
					<td align="left" id="industryNote"></td>
				</tr>
				<tr>
					<td align="right" width="100">商家类型：</td>
					<td align="left" id="merchantNote" ></td>
				</tr>
				<tr>
					<td align="right" width="100">联系人：</td>
					<td align="left" id="contacts2"></td>
				</tr>
				<tr>
					<td align="right" width="100">电话：</td>
					<td align="left" id="telephone2"></td>
				</tr>
				<tr>
					<td align="right" width="100">商家地址：</td>
					<td align="left" id="address2"></td>
				</tr>
				<tr>
					<td align="right" width="100">商家描述：</td>
					<td align="left" id="description2"></td>
				</tr>
				<tr>
					<td align="right">商家Logo:</th>
					<td align="left">
						<img id="merchantLogo3" src="" alt="" width="100" height="100" align="middle"/>
					</td>
				</tr>
				<tr>
					<td align="right">商家营业执照:</th>
					<td align="left">
						<img id="lecenseImage3" src="" alt="" width="100" height="100" align="middle"/>
					</td>
				</tr>
				 <tr>
					<td align="right">商家照片:</th>
					<td align="left">
						<ul id="imgViewer">
							<li style="float:left;padding:10px;">
								<img id="photo13" src="" alt="点击查看原图" width="100" height="100" align="middle"/>
							</li>&nbsp;
							<li style="float:left;padding:10px;">
								<img id="photo23" src="" alt="点击查看原图" width="100" height="100" align="middle"/>
							</li>&nbsp;
							<li style="float:left;padding:10px;">
								<img id="photo33" src="" alt="点击查看原图" width="100" height="100" align="middle"/>
							</li>
						</ul>
					</td>
				</tr>
			</table>
		</div>
		<!-- 商家优惠信息 -->
		<div title="商家优惠信息" style="padding:10px 10px 20px 10px">
			<div>
				优惠信息: <input type="text" class="easyui-textbox" style="width:160px" id="benefitName" name="benefitName">
				<a id="searchBenefit" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:60px;height:22px;">查询</a>
			</div>
			<table id="benefitList" class="easyui-datagrid"
				data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
				<thead>
					<tr>
						<th field="benefitName" width="160" align="left">优惠信息</th>
						<th field="benefitQuota" width="80" align="center">优惠额度</th>
						<th field="benefitCount" width="80" align="center">优惠数量</th>
						<th field="useExplain" width="100" align="left">使用条件</th>
						<th field="limitedCount" width="100" align="center">限制领取/人</th>
						<th field="createTime" width="125" align="center">创建时间</th>
						<th field="changeTime" width="125" align="center">修改时间</th>
						<th field="validTime" width="125" align="center">有效期</th>
					</tr>
				</thead>
			</table>
		</div>
		
		<!-- 商家会员信息 -->
		<div title="商家会员信息" style="padding:10px 10px 20px 10px">
			<div>
				会员号: <input type="text" class="easyui-textbox" style="width:160px" id="vipcardCode" name="vipcardCode">
				<a id="searchVipcard" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:60px;height:22px;">查询</a>
			</div>
			<table id="vipcardList" class="easyui-datagrid"
				data-options="singleSelect:false,selectOnCheck:true,checkOnSelect:true,fixed:true,fit:true,border:false,nowrap:true,rownumbers:true">
				<thead>
					<tr>
						<th field="vipcardCode" width="100" align="center">会员卡号</th>
						<th field="userName" width="160" align="center">会员账号</th>
						<th field="openTime" width="100" align="center">开卡时间</th>
						<th field="endTime" width="100" align="center">截止时间</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>
</html>