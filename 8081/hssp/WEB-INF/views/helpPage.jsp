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
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
<body style="margin: 20px;">
	<div>
		<img src="${pageContext.request.contextPath}/resources/images/help.png"
			style="width: 100%;margin: 0 auto;" />
			<div style="float: right"><strong><span style="font-size:15px;text-align: left;color: red;"><a href="${pageContext.request.contextPath}/showPDF?PDFName=1">[帮助文档查看]</a></span>
			<span style="font-size:15px;text-align: left;color: red;"><a href="${pageContext.request.contextPath}/showPDF?PDFName=2">[常见问题解答文档]</a></span>
			<span style="font-size:15px;text-align: left;color: red;"><a href="${pageContext.request.contextPath}/showPDF?PDFName=3">[培训PPT]</a></span>
			</strong></div>
			
			<div style="display:none">
		<form id="form_downloadFile" method="post" action="/hssp/body/downloadFile">
			<input type="hidden" id="fileName" name="fileName"/>
			<input type="hidden" id="downloadfileName" name="downloadfileName"/>
			<input type="hidden" id="fileID" name="fileID"/>
			<input type="hidden" id="typeName" name="typeName" value="1"/>
			<input type="submit" id="downloadFile"/>
		</form>
	</div>
	</div>
</body>
<script>
//下载病种库导出文件
	function downloadFile(fileName,downloadfileName,fileID)
	{
	    //<a onclick="downloadFile('用户指南柯林布瑞临床大数据搜索（CSO）软件.pdf','1.pdf','')">下载</a>
	    //<a onclick="downloadFile('常见问题解答.doc','2.doc','')">[常见问题解答文档查看 下载]</a>
	    //<a onclick="downloadFile('搜索引擎_培训.ppt','3.ppt','')">[培训PPT查看下载]</a>
		$("#fileName").val(fileName);
		$("#downloadfileName").val(downloadfileName);
		$("#fileID").val(fileID);
		$("#form_downloadFile").submit();
	}
</script>
</html>
