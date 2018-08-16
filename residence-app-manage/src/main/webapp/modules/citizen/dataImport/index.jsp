<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流口数据导入</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/themes/icon.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/styles/global.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/modules/citizen/dataImport/styles/dataImport.css"/>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/jquery-1.11.1.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/plugin/jquery-form.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/library/jquery/easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/config.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/modules/citizen/dataImport/js/dataImport.js"></script>
</head>
<body>
<div id="pa_div" class="easyui-layout" fit="true">
	<div data-options="region:'center',border:true">
		<div class="easyui-layout">
			<div style="padding:60px 0 0 300px;width:500px;">
				<form id="citizen_dataimport_form" name="citizen_dataimport_form" action="" enctype="multipart/form-data" method="post">
					<input id="zipImportFile" name="zipImportFile" class="easyui-filebox" style="width:360px;" data-options="required:true" />&nbsp;<font color="red">*</font>
					<!--<input id="xlsImportFile" name="xlsImportFile" class="easyui-filebox" style="width:360px;" data-options="required:true" />&nbsp;<font color="red">*</font>
					<br/>
					<br />
					<input id="photoImportFile" name="photoImportFile"class="easyui-filebox" style="width: 360px;" data-options="required:true" />&nbsp;<font color="red">*</font>
					<br />
					<br />
					<input id="zipPhotoImportFile" name="zipPhotoImportFile"class="easyui-filebox" style="width: 360px;" />&nbsp;&nbsp;
					<br />
					<br />
				 	<select id="areasListCitizen" name ="areasListCitizen" class="easyui-combogrid" style="width:282px;"></select>&nbsp;&nbsp;&nbsp;-->
					<a href="javascript:void(0)" class="easyui-linkbutton" size="45" onclick="populationDateimport()" id="excel_imp_link">数据导入</a>&nbsp;&nbsp;
					<br />
					<br /> 
				</form>
			</div>
			<div style="padding:10px 0 0 300px;border:1px;color:black;line-height: 22px;">
				<p>导入数据说明</p>
				<p>一、数据文件与照片文件放到同一个压缩文件中</p>
				<p>压缩文件命名规则：resAppData-区域代码(时间段)-数量.zip</p>
				<p>示例：resAppData-330100(20170301-20170303)-18.zip</p>
				<p>二、流口数据文件名格式要求</p>
				<p>1、文件名编码规则：resAppData-区域代码(时间段)-数量-data.xls</p>
				<p>2、示例：resAppData-330100(20170301-20170303)-18-data.xls</p>
				<p>三、照片文件格式说明</p>
				<p>1、文件名编码规则：resAppData-区域代码(时间段)-数量-pictures.zip</p>
				<p>2、示例：resAppData-330100(20170301-20170303)-18-pictures.zip</p>
				<p>3、压缩包中的照片全部以数据中的身份证号命名，照片为jpg格式</p>
			</div>
			<div id="error_info_div" style="display:none;margin:20px 10px 0 10px;border-top:1px solid darkgray;color: red;line-height: 22px;fit:true;">
				<div id="fistError" style="padding:10px 0 0 290px;"></div>
			</div>
		</div>
	</div>
</div>
</body>
</html>