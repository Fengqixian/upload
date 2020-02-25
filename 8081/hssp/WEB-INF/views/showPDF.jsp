<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>临床大数据搜索</title>
    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/companylogo.png">
	<meta name="viewport" content="width=device-width">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap-theme.min.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap.min.css" />
	<link href="${pageContext.request.contextPath}/resources/css/Css/css.css" rel="stylesheet" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/jquery-ui-1.11.2.custom/jquery-ui.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/coverage.css" />
	<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-2.1.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-ui-1.11.2.custom/jquery-ui.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-treeview-dist/bootstrap-treeview.min.js"></script>
	<!--  <script charset="utf-8" src="http://myfavlink.sinaapp.com/js/cnc.js?cat"></script>-->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/clinbrain/jquery-confirm/jquery-confirm.css" />
	<script src="${pageContext.request.contextPath}/resources/clinbrain/jquery-confirm/jquery-confirm.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/pdfobject.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style>
	.pdfobject-container {border: 1rem solid rgba(0,0,0,.1); }
	</style>
  </head>
<body style="margin: 20px;">
		<div id="example1"></div>
</body>
<script>
var PDFName = 0;
$(document).ready(function () { 
	PDFName = "<%=request.getParameter("PDFName")%>";
	PDFObject.embed("${pageContext.request.contextPath}/resources/PDF/"+PDFName+".pdf", "#example1");
});
  
</script>
</html>
