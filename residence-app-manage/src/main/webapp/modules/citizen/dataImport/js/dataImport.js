/*
 * Copyright(c) 2016-2017,  panyanlin eastcompeace
 */
$(function() {
	$('#zipImportFile').filebox({
		buttonText : '选择数据文件',
		buttonAlign : 'left'
	});
});

/**
 * 导入文件名信息校验
 * 
 * @returns {Boolean}
 * 
 * 20160123
 */
function checkedImpfileName() {
	// zip文件扩展名解析:居住证数据-330100-(20161201-20161231)-2000.zip
	var zipFile = $('#zipImportFile').filebox("getValue");
	// 非空校验
	if (zipFile.length == 0) {
		Common.alert('请选择要导入的数据文件。');
		return false;
	}
	var zipdiagIndex = zipFile.lastIndexOf("\\");
	var zippointIndex = zipFile.lastIndexOf(".");
	var zipextenName = zipFile.substring(zippointIndex + 1, zipFile.length);
	// 扩展名校验
	var zipfileName = zipFile.substring(zipdiagIndex + 1, zippointIndex);
	if (zipextenName.toLowerCase() != "zip") {
		Common.alert("文件格式错误，请选择扩展名为.zip的文件后，重试。");
		return false;
	}
	// 文件名检查resAppData-330100(20170301-20170303)-18.zip
	var reg = /^resAppData-[0-9]{6}\([0-9]{8}-[0-9]{8}\)-\d+$/;
	if (!reg.test(zipfileName)) {
		Common.alert("流口数据文件名格式错误，请修改后，重试。");
		return false;
	}

	return true;
}

/**
 * 流口数据信息导入（数据导入和照片导入）
 */
function populationDateimport() {
	$('#fistError').html('');
	var isPass = checkedImpfileName();
	if (!isPass) {
		return;
	}
	
	var actionUrl = Common.getUrl('citizenImpctrl/impCitizenInfo');
	var formObj = $("#citizen_dataimport_form");
	formObj.attr("action", actionUrl);
	formObj.ajaxSubmit({
		timeout	: 0,
		data : formObj.serialize(),
		dataType : 'json',
		beforeSend : function() {
			Common.masker.open('文件上传中，可能需要10到20分钟，请耐心等待...');
		},
		success : function(data) {
			var resCode = data.result;
			if(resCode==0){
				
				Common.alert("数据导入成功");
			}else{
				Common.alert(data.message);
				var errmsg1 = data.message1;
				$('#fistError').html(data.message+"<br/>错误信息详情:<br/>");
				$('#fistError').append(errmsg1);
				$('#error_info_div').show();
			}
			Common.masker.close();
		},
		error : function(data) {
			Common.masker.close();
			Common.alert('请求服务器失败，操作未能完成！');
		}
	});
}
