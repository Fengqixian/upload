<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
<!DOCTYPE HTML>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
<title>临床大数据搜索引擎</title>
    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/companylogo.png">
<link  rel="stylesheet"  href="${pageContext.request.contextPath}/resources/assets/css/style_config.css"  type="text/css">
<script src="${pageContext.request.contextPath}/resources/assets/js/jquery-1.8.3.min.js"></script>
</head>
<body>
<div class="login-container">
	<div class="header">
		<img src="${pageContext.request.contextPath}/resources/assets/images/login-bg-header-logo.png">
		<span>上海市柯林布瑞信息技术有限公司</span>
		<div class="clear"></div>
	</div>
	<div class="login-content">
		<div class="container">
			<img src="${pageContext.request.contextPath}/resources/assets/images/login-bg.png">
		    <div class="login">
		    	<div class="header">临床大数据搜索引擎</div> 
		        <div style="text-align: center;">
		        	<form action="#"  id="configForm" method="post">
		                <div class="m-2-2">
		                   <input type="text" name="UserAccount" id="UserAccount" placeholder="请输入账号" />
		                </div>
		                <div class="m-2-2">
		                   <input type="password"  name="UserPassword" id="UserPassword" placeholder="请输入密码"/>
		                </div>
		                
		                <div class="m-2-2">
		                   <button type="button"  onclick ="login()" value="登录" > 登录
		 				</button>
		                </div>
		                    
		        	</form>
		        </div>
		    </div>
	    </div>
	</div>
	<div class="login-footer">   
		<img src="${pageContext.request.contextPath}/resources/assets/images/login-bg-footer-logo.png">
		<div class="right">上海柯林布瑞信息技术有限公司Copyright@2018-2020</div>
	</div>
</div>
<script type="text/javascript">
function login(){
	 $.ajax(
				{
					type : "post",
					url : "/hssp/rest/LoginOK",
					data:"UserAccount="+$("#UserAccount").val()+"&UserPassword="+$("#UserPassword").val(),
					dataType : "json",
					success : function(result) 
					{
						if(result.status=="ERROR"){
							alert("账户或者密码有误!");
						}else if(result.status=="SUCCESS"){
							window.location.href="/hssp";
						}else{
							alert("系统出错，请联系系统管理员或信息科！");
						}
					},error:function(e){
						alert("系统出错，请联系系统管理员或信息科！");
					}
				});
}

$(document).keyup(function(event){
	  if(event.keyCode ==13){
		  login();
	  }
	});
</script>
</body>
</html>