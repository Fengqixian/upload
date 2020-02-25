<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/views/common/meta.jsp"%>
<title>Spring MVC REST Service</title>
<link href="/favicon.ico" rel="shortcut icon" type="image/x-icon">
<meta name="viewport" content="width=device-width">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap-theme.min.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap.min.css" />
<link href="${pageContext.request.contextPath}/resources/css/Css/css.css" rel="stylesheet" />
</head>
<body>
<div class="topnav">
       <ul class="topmenu">
           <li><a href="/">首页</a></li>
           <li class=""><a href="#" data-args="SavedDataSetResult">病种库管理</a></li>
           <li class=""><a href="#" data-args="SavedExpression">表达式管理</a></li>
           <li class=""><a href="#" data-args="AdvancedSearch">高级检索</a></li>
           <li class=""><a href="#" data-args="CustomDataSet">集合定义</a></li>
       </ul>
       <div class="setting">
       	<a>设置</a>
       </div>
   </div>
   
   <div id="container">
   	<div class="container">
       	<h1><img src="../Images/logo.png" height="55px">医院临床大数据医学科研搜索引擎</h1>
		<div class="row">
   			<form name="form1" method="post" action="" class="search_form" onkeydown="if(event.keyCode==13)return false;">
       			<span class="kw col-xs-10"><input type="text" name="txtKeyword" id="txtKeyword" value=""></span>
       			<span class="search_btn col-xs-2"><input type="button" name="searchButton" id="searchButton" value="搜 索" onclick="window.open('results.html')"></span>
       			<!-- 输入框获取焦点弹出框 -->
       			<div class="search_list" style="display: none; bottom: 0;" id="searchKeyword">
           			<ul></ul>
      				</div>
   			</form>
		</div>
           <div class="s_jl col-xs-12">
           	<span class="jl_title">历史表达式 <a class="more"></a></span>
           	<div class="row">
               	<ul class="bds" id="keywordHistory">   
               		<li class="col-lg-3 col-sm-6 col-xs-12">
                           <i class="l_close">x</i>
                           <div>的</div>
                       </li>
                       <li class="col-lg-3 col-sm-6 col-xs-12">
                           <i class="l_close">x</i>
                           <div>yy</div>
                       </li>
                   </ul>
           	</div>
           	<span class="jl_title">已保存病种库<a class="more">更多</a></span>
           	<div id="SavedResultDataSetNamePartial" class="">
				<div class="row">
   					<ul class="shuju"></ul>
				</div>
               </div>
       	</div>
   	</div>
</div>
   <div class="footer" style="position: fixed; bottom: 0px;">
        © 2016 clinbrain 上海柯林布瑞信息技术有限公司Solr
    </div>

</body>
	</html>