<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>修改密码</title>
  
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	

  </head>
  
  <body>
    <div id="pwd_dialog">
		<div class="easyui-panel"  style="width:400px;height:230px;padding:5px;overflow: hidden" data-options="iconCls:'icon-edit',collapsible:true" >
			<form id="f1" method="post">
			  <table width="98%"  border="0" cellpadding="0" cellspacing="0" class="ht-table-form">
		         <tr>  
		            <th  style="padding: 8px">原密码：</th>  
		            <td td align="left">
		             <input name="acc_password"  type="password" id="acc_password" maxlength="16" onblur="checkold()"></input>
		           </td>  
		           <td><font color="red">*&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
		        </tr> 
		         <tr >  
		            <td id="p" colspan="3" style="display: none;text-align: center;"></td>
		        </tr>  
		        <tr>  
		            <th  style="padding: 8px">新密码：</th>  
		            <td td align="left">
		             <input name="password"  type="password" id="password" maxlength="16" onblur="checknew()"></input>
		           </td>  
		           <td><font color="red">*&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
		        </tr> 
		        <tr >  
		            <td id="p1" colspan="3" style="display: none;text-align: center;" ></td>
		        </tr> 
		        <tr >  
		            <th  style="padding: 8px">确认新密码：</th>  
		            <td align="left">
		             <input name="password1" type="password"  id="password1" maxlength="16" onblur="checknew()"></input>
		            </td>  
		            <td><font color="red">*&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
		        </tr> 
		        
		        <tr >  
		            <td id="p2" colspan="3" style="display: none;text-align: center;" ></td>
		        </tr> 
			  </table>
			  	<font id="msg_pass" color='red'>&nbsp;系统检测到您的密码过于简单！建议重新设置</font>
			</form>
		</div>
	</div>
  </body>
  
<script type="text/javascript">
function checkold(){
	var oldpwd=$('#acc_password').val();
	 $.ajax({  
          type : "post",  
          url : "login!checkPwd.action",  
          data : 'acPasswd='+oldpwd,
          async : false,  
          dataType: "json",
          success : function(data){  
             if(data){
             	//alert(data.message);
             	$('#p').show();
             	$('#p').html('<font color="red" >'+data.message+'</font>');
             	$('#acc_password').focus();
             	return false;
			 } else{
			 	$('#p').hide();
			 }
          }  
     }); 
}
var usern = /^[a-zA-Z0-9_]{6,16}$/; 
function checknew(){
	var newpwd=$('#password').val();
  	var repwd=$('#password1').val();
	if(null!=newpwd && ""!=newpwd){
		if(Number(newpwd)||newpwd.length<6||newpwd.length>16){
			$('#p2').hide();
			$('#p1').show();
			$('#p1').html('<font color="red" >密码不能为纯数字组成,且长度要在6-16位数之间</font>');
			$('#password').focus();
			return false;
		}else{
			$('#p1').hide();
		}
	}
	if(null!=newpwd && ""!=newpwd && null!=repwd && ""!=repwd){
		if(newpwd!=repwd){
			$('#p2').show();
			$('#p2').html('<font color="red" >两次密码输入不一致</font>');
			return false;
		}else{
			$('#p2').hide();
		}
	}
}
function  savepwd(){
 	var oldpwd=$('#acc_password').val();
 	var newpwd=$('#password').val();
  	var repwd=$('#password1').val();
 	if(null!=oldpwd && ""!=oldpwd && null!=newpwd && ""!=newpwd && null!=repwd && ""!=repwd){
		if(Number(newpwd)){
			$('#p2').hide();
			$('#p1').show();
			$('#p1').html('<font color="red" >密码必须由6-16位字母数字下划线组成</font>');
			$('#password').focus();
			return false;
		}else{
			$('#p1').hide();
		}
		if(newpwd!=repwd){
			$('#p2').show();
			$('#p2').html('<font color="red" >两次密码输入不一致</font>');
			return false;
		}else{
			$('#p2').hide();
		}
  		 $.post('login!upPwd.action',{'oldPasWord':oldpwd,'newPasWord':newpwd},function(msg){
  		 	if(msg){
  		 		msgShow('提示', ''+msg.message+'', 'info');
  		 	}
  		 },'json');
 		return true;
 	}else{
 		msgShow('提示', '请将信息输入完整！', 'info');
 		return false;
 	}
 	
 }

</script>
</html>
