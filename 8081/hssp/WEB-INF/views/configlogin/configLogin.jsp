<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<!DOCTYPE HTML>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
<title>数据配置管理</title>
    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/companylogo.png">
<link  rel="stylesheet"  href="${pageContext.request.contextPath}/resources/assets/css/style_config.css"  type="text/css">
<script src="${pageContext.request.contextPath}/resources/assets/js/jquery-1.8.3.min.js"></script>
</head>
<body>


	<!-- <video  autoplay muted loop poster="assets/images/fallba1ck.jpg">
		<source src="assets/images/mov.mp4">		
		你的游览器不支持video支持
	</video> -->
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
		    	<div class="header">搜索引擎配置管理</div> 
		        <div style="text-align: center;">
		        	<form action="#"  id="configForm" method="post">
		                <div class="m-2-2">
		                   <input type="text" name="solrAccount" placeholder="请输入账号" />
		                </div>
		                <div class="m-2-2">
		                   <input type="password"  name="solrPassword" placeholder="请输入密码"/>
		                </div>
		                
		                <div class="m-2-2">
		                   <button type="button"  onclick ="configLogin()" value="登录" > 登录
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
function configLogin(){
	 $.ajax(
				{
					type : "post",
					url : "/hssp/config/configLogin",
					data:$("#configForm").serialize(),
					dataType : "text",
					success : function(result) 
					{
						if(result=="ERROR"){
							alert("账户或者密码有误!");
						}else{
								window.location.href="/hssp/configMannage";
						}
					},error:function(e){
						alert(e);
					}
				});
}

$(document).keyup(function(event){
	  if(event.keyCode ==13){
		  configLogin();
	  }
	});
</script>
</body>
</html>