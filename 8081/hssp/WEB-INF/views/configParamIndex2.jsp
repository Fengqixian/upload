<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>临床大数据搜索</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/basictable.css" />
<link rel="icon"
	href="${pageContext.request.contextPath}/resources/images/logo ico.png">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap-theme.min.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap.min.css" />
<link  rel="stylesheet"  href="${pageContext.request.contextPath}/resources/assets/css/style_config.css"  type="text/css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/Css/css_config.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/Css/jquery-ui-1.11.2.custom/jquery-ui.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/Css/coverage.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-table-fixed-columns.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/bootstrap-table.min.css">
<script
	src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-2.1.1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-treeview-dist/bootstrap-treeview.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/bootstrap-table.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/bootstrap-table.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/locale/bootstrap-table-zh-CN.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/js/js/jquery.validate.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/js/jquery.form.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-ui-1.11.2.custom/jquery-ui.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/js/jquery.basictable.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/clinbrain/ztree/zTreeStyle.css" />
<script
	src="${pageContext.request.contextPath}/resources/clinbrain/ztree/jquery.ztree.all.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/src/extensions/editable/bootstrap-table-editable.js"></script>
</head>
<body>

	<style type="text/css">
#main-nav {
	margin-left: 1px;
}

#main-nav.nav-tabs.nav-stacked>li>a {
	padding: 10px 8px;
	font-size: 12px;
	font-weight: 600;
	color: #4A515B;
	background: #E9E9E9;
	background: -moz-linear-gradient(top, #FAFAFA 0%, #E9E9E9 100%);
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #FAFAFA),
		color-stop(100%, #E9E9E9));
	background: -webkit-linear-gradient(top, #FAFAFA 0%, #E9E9E9 100%);
	background: -o-linear-gradient(top, #FAFAFA 0%, #E9E9E9 100%);
	background: -ms-linear-gradient(top, #FAFAFA 0%, #E9E9E9 100%);
	background: linear-gradient(top, #FAFAFA 0%, #E9E9E9 100%);
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#FAFAFA',
		endColorstr='#E9E9E9');
	-ms-filter:
		"progid:DXImageTransform.Microsoft.gradient(startColorstr='#FAFAFA', endColorstr='#E9E9E9')";
	border: 1px solid #D5D5D5;
	border-radius: 4px;
}

#main-nav.nav-tabs.nav-stacked>li>a>span {
	color: #4A515B;
}

#main-nav.nav-tabs.nav-stacked>li.active>a,#main-nav.nav-tabs.nav-stacked>li>a:hover
	{
	color: #FFF;
	background: #3C4049;
	background: -moz-linear-gradient(top, #4A515B 0%, #3C4049 100%);
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #4A515B),
		color-stop(100%, #3C4049));
	background: -webkit-linear-gradient(top, #4A515B 0%, #3C4049 100%);
	background: -o-linear-gradient(top, #4A515B 0%, #3C4049 100%);
	background: -ms-linear-gradient(top, #4A515B 0%, #3C4049 100%);
	background: linear-gradient(top, #4A515B 0%, #3C4049 100%);
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#4A515B',
		endColorstr='#3C4049');
	-ms-filter:
		"progid:DXImageTransform.Microsoft.gradient(startColorstr='#4A515B', endColorstr='#3C4049')";
	border-color: #2B2E33;
}

#main-nav.nav-tabs.nav-stacked>li.active>a,#main-nav.nav-tabs.nav-stacked>li>a:hover>span
	{
	color: #FFF;
}

#main-nav.nav-tabs.nav-stacked>li {
	margin-bottom: 4px;
}

/*定义二级菜单样式*/
.secondmenu a {
	font-size: 10px;
	color: #4A515B;
	text-align: center;
}

.navbar-static-top {
	background-color: #212121;
	margin-bottom: 5px;
}

.navbar-brand {
	background: url('') no-repeat 10px 8px;
	display: inline-block;
	vertical-align: middle;
	padding-left: 50px;
	color: #fff;
}
</style>
	<div class="login-container">
		<div class="header">
			<img src="${pageContext.request.contextPath}/resources/assets/images/login-bg-header-logo.png">
			<span>后台配置管理</span>
			<div class="clear">
			</div>
				<a onclick="loginout()"><img src="/hssp/resources/images/ic_power_white_48dp.png" style="float:right;height:40px;margin:10px;"></a>
			</div>
		</div>
		<div class="login-content">
			<div class="row">
				<div class="col-md-2" style="padding-left: 0px;padding-right: 0px;">
					<ul id="main-nav" class="nav nav-tabs nav-stacked" style="">
						<!--  <li class="active"><a href="#"> <i
								class="glyphicon glyphicon-th-large"></i> 首页
						</a></li>-->
						<li><a id="system_id"; href="#systemSetting" class="nav-header collapsed"
							data-toggle="collapse"> <i class="glyphicon glyphicon-cog"></i>
								系统配置管理 <span class="pull-right glyphicon glyphicon-chevron-down"></span>
						</a>
							<ul id="systemSetting" class="nav nav-list collapse secondmenu"
								style="height: 0px;">
								<li><a 
									href="${pageContext.request.contextPath}/getConfigTable"
									target="right"><i class="glyphicon glyphicon-asterisk"></i><span id="system_table">系统表配置</span></a></li>
								<li><a href="${pageContext.request.contextPath}/configInfo"
									target="right"><i class="glyphicon glyphicon-th-list"></i>启动常量配置</a></li>
							</ul></li>
						<li><a id="Privilege_id" href="#systemSettings" class="nav-header collapsed"
							data-toggle="collapse"> <i
								class="glyphicon glyphicon-credit-card"></i> 用户权限管理 <span
								class="pull-right glyphicon glyphicon-chevron-down"></span>
						</a>
							<ul id="systemSettings" class="nav nav-list collapse secondmenu"
								style="height: 0px;">
								<li><a  href="${pageContext.request.contextPath}/userConfig"
									target="right"><i class="glyphicon glyphicon-user"></i>用户设置</a></li>
								<li><a href="${pageContext.request.contextPath}/roleConfig"
									target="right"><i class="glyphicon glyphicon-user"></i><span id="user_show_id">权限角色设置</span></a></li>
								<li><a href="${pageContext.request.contextPath}/privilegeRowConfig"
									target="right"><i class="glyphicon glyphicon-user"></i>行级权限</a></li>
								<li><a href="${pageContext.request.contextPath}/tableFieldDescriptionConfig"
									target="right"><i class="glyphicon glyphicon-user"></i>表列头管理</a></li>
							</ul></li>

					</ul>
				</div>
				<div id="iframe_div_id" class="col-md-10" style="padding-left: 0px;padding-right: 0px;">

					<iframe id="iframe_id" scrolling="auto" frameborder="0" name="right" width="100%"></iframe>
				</div>
			</div>
		</div>
		<div class="login-footer">   
		<img src="${pageContext.request.contextPath}/resources/assets/images/login-bg-footer-logo.png">
		<div class="right">上海柯林布瑞信息技术有限公司Copyright@2018-2020</div>
	</div>
	</div>

</body>
<script>
	$(document).ready(function () {        
	    //当点击查询按钮的时候执行
	    $("#systemSetting").click();
	    //$("#Privilege_id").click();
	    var windows_height = $(window).height()-96;
	    $("#main-nav").height(windows_height);
	    $("#iframe_div_id").height(windows_height);
	    $("#iframe_id").height(windows_height);
	    $("#system_table").trigger("click");
	});
	function loginout(){
		$.ajax(
				{
					type : "post",
					url : "/hssp/config/loginOut",
					dataType : "json",
					success : function(result) 
					{
						window.location.reload();
					},error:function(e){
						window.location.reload();
					}
				});
	}
</script>
</html>