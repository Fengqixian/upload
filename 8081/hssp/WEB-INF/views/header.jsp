<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/views/common/meta.jsp"%>
<title>临床大数据搜索</title>
<link href="/favicon.ico" rel="shortcut icon" type="image/x-icon">
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
		<style type="text/css">
		.checkButton:before {
		    content: "";
		    border-width: 5px;
		    border-color: transparent transparent #ffffff transparent;
		    border-style: solid;
		    position: absolute;
		    bottom: 0;
		    /* margin: auto; */
		    /* width: 10px; */
		    left: 40%;
		    /* color: blue; */}
		    .checkNo{
			    pointer-events: none;
			}
    	</style>
</head>
<body>
	<div class="topnav">
		<ul class="topmenu">
			<li><a tabIndex="1" id="hsspMainPage" href="/hssp" target="_parent">首 页</a></li>
			<li class="" id="showSearch" style="display:none" disabled="disabled"><a tabIndex="2" onclick="window.parent.showSearch()" >数据检索</a></li>
			 <li id="showExport" class="" disabled="disabled"><a tabIndex="2" id="showExport_a" onclick="window.parent.showExport()" >数据导出信息</a></li>
			<!-- <li class="" disabled="disabled"><a tabIndex="2" onclick="getDiseaseDBDetailLocal(1)" >数据导出信息</a></li>
			 <li class="" disabled="disabled"><a tabIndex="2" onclick="window.parent.jumpExport()" >数据导出信息</a></li> 
		 <li class=""><a tabIndex="3" onclick="parent.showDiseaseStatu(1)">科研病种库信息</a></li>	
			 
			 <li class=""><a tabIndex="4" onclick="parent.showStatisticsStatu(1)">科研统计库信息</a></li> -->
			<li class=""><a tabIndex="5"	onclick="expressionManage()">表达式管理</a></li>
			<li class=""><a tabIndex="6" onclick="advancedSearch()">高级检索</a></li>
			<li id="diseaseCountDetail"><a tabIndex="10" onclick="getDiseaseCountDetail()">使用量统计</a></li>
			<li id="wordCloud"><a tabIndex="11" onclick="wordCloud()">热词统计</a></li>
			
			<!--<li class=""><a tabIndex="6" onclick="window.parent.showFancytree()">树</a></li>
			    <li class=""><a tabIndex="4" onclick="uploadShow()">文件上传</a></li>
			-->
			<li class=""><a tabIndex="9" onclick="feedbackManage()">反馈</a></li>
			<li class="" id="showPrivilegeApproval"><a tabIndex="5" onclick="showPrivilegeApproval()">权限信息</a></li>
			<li class=""><a tabIndex="5" href="${pageContext.request.contextPath}/help">帮助</a></li>
			<!-- 
			<li class=""><a onclick="advancedSearch()">集合定义</a></li> -->
		</ul>
		<div class="setting">
			<!-- <a>设置</a> -->
			<!--<a tabIndex="6" style="float:right;" onclick="parent.ssoUserLogout()">退出</a>-->
			<a tabIndex="6"  style="float:right;padding: 0px 0px;" onclick="parent.ssoUserLogout()">
			  <img src="${pageContext.request.contextPath}/resources/images/ic_power_white_48dp.png" style="float:right;height:24px;margin:3px;" >
			</a>
			<span id="ssoUserName" style="float:right;padding: 0px 10px;"></span>
		</div>
	</div>
	<div style="display:none">
		<form id="form_downloadFile" method="post" action="/hssp/body/downloadFile">
			<input type="hidden" id="fileName" name="fileName"/>
			<input type="hidden" id="fileID" name="fileID"/>
			<input type="hidden" id="typeName" name="typeName" value="0"/>
			<input type="submit" id="downloadFile"/>
		</form>
	</div>
</body>
<script>
$(document).ajaxComplete(function (event,xhr,settings){
   if(xhr.getResponseHeader("sessionstatus")=="timeout"){
      window.parent.loginAgain(xhr.getResponseHeader("loginPath"));
   }

});

document.onkeydown = killesc;
	function killesc (){
	   if(window.event.keyCode == 27){
	      window.event.keyCode =0;
	      window.event.returnValue = false;
	   }
	}
	function showPrivilegeApproval(){
		window.parent.showPrivilegeApproval();
	}
	//************************************************************病种库管理************************************************************
	//病种库管理主入口
	function uploadShow()
	{
		window.parent.uploadShowDiv();
	}
	//病种库管理主入口
	function diseaseDBManage()
	{
		window.parent.getDiseaseClassifyLocal();
	}
	//病种库搜索管理主入口
	function diseaseDBManageLocal()
	{
		window.parent.getDiseaseClassifyLocal();
	}
	//获取某分类上一页的病种库
	function getPreviousPageDiseaseDBDetail()
	{
		if (($("#diseaseDBCurrentPage_local",parent.document).text() - 1) < 1)
		{
			parent.$.dialog("已经是第一页", false, 1000);
		}
		else
		{
			getDiseaseDBDetailLocal($("#diseaseDBCurrentPage_local",parent.document).text() - 1);
		}
	}
	//获取某分类下一页的病种库
	function getNextPageDiseaseDBDetail()
	{
		if (($("#diseaseDBCurrentPage_local",parent.document).text() - 0 + 1) > $("#diseaseDBTotalPage_local",parent.document).text())
		{
			parent.$.dialog("已经是最后一页", false, 1000);
		}
		else
		{
			getDiseaseDBDetailLocal($("#diseaseDBCurrentPage_local",parent.document).text() - 0 + 1);
		}
	}
	//跳转至某分类指定页数的病种库
	function jumpToDiseaseDBDetail()
	{
		var forPage = $("#diseaseDB_pageNum_local",parent.document).val();
		if (forPage != 0 && forPage != "" && Number(forPage) <= Number($("#diseaseDBTotalPage_local",parent.document).text()))
		{
			$("#diseaseDB_pageNum_local",parent.document).val("");
			getDiseaseDBDetailLocal(forPage);
		}
		else
		{
			parent.$.dialog("跳转页数输入有误!请重新输入!", false, 1000);
			$("#diseaseDB_pageNum_local",parent.document).val("");
		}
	}
	//获取某分类上一页的病种库
	function getPreviousPageuploadDetail()
	{
		if (($("#diseaseDBCurrentPage_uploadDetail",parent.document).text() - 1) < 1)
		{
			parent.$.dialog("已经是第一页", false, 1000);
		}
		else
		{
			getUploadDetail($("#diseaseDBCurrentPage_uploadDetail",parent.document).text() - 1);
		}
	}
	//获取某分类下一页的病种库
	function getNextPageuploadDetail()
	{
		if (($("#diseaseDBCurrentPage_uploadDetail",parent.document).text() - 0 + 1) > $("#diseaseDBTotalPage_uploadDetail",parent.document).text())
		{
			parent.$.dialog("已经是最后一页", false, 1000);
		}
		else
		{
			getUploadDetail($("#diseaseDBCurrentPage_uploadDetail",parent.document).text() - 0 + 1);
		}
	}
	//跳转至某分类指定页数的病种库
	function jumpTouploadDetail()
	{
		var forPage = $("#diseaseDB_pageNum_uploadDetail",parent.document).val();
		if (forPage != 0 && forPage != "" && Number(forPage) <= Number($("#diseaseDBTotalPage_uploadDetail",parent.document).text()))
		{
			$("#diseaseDB_pageNum_uploadDetail",parent.document).val("");
			getUploadDetail(forPage);
		}
		else
		{
			parent.$.dialog("跳转页数输入有误!请重新输入!", false, 1000);
			$("#diseaseDB_pageNum_uploadDetail",parent.document).val("");
		}
	}
	//获取某分类指定页数的病种库
	function getDiseaseDBDetail(forPage)
	{
		$.ajax(
		{
			type : "post",
			url : "/hssp/head/getDiseaseDB",
			data : "pageCount=15&pageNum=" + forPage + "&USERID=" + $("#hideUserIdentity", parent.document).val() + "&DISEASECLASSIFYID=" + $("#hideDiseaseDBClassifyId",parent.document).val() + "&DISEASECLASSIFYCHIIDID=" + $("#hideDiseaseDBClassifySubId",parent.document).val() + "&department" + $("#hideUserDepartment",parent.document).val(),
			dataType : "json",
			success : function(result) 
			{
				parent.buildDiseaseDBDetailTable(forPage,result);
			}
		});
	}
	//获取某分类指定页数的病种库
	function getDiseaseDBDetailLocal(forPage)
	{
		$.ajax(
		{
			type : "post",
			url : "/hssp/head/getDiseaseDBLocal",
			data : "pageCount=15&pageNum=" + forPage + "&USERID=" + $("#hideUserIdentity", parent.document).val(),
			dataType : "json",
			success : function(result) 
			{
			    parent.resetRefreshExportMessageOk(true);
				parent.buildDiseaseDBDetailTable(forPage,result);
			}
		});
	}
	//获取某分类指定页数的病种库
	function getUploadDetail(forPage)
	{
		$.ajax(
		{
			type : "post",
			url : "/hssp/head/getUploadMessage",
			data : "pageCount=15&pageNum=" + forPage + "&USERID=" + $("#hideUserIdentity", parent.document).val(),
			dataType : "json",
			success : function(result) 
			{
				parent.buildUploadDetail(forPage,result);
			}
		});
	}
	//*******************************************************************表达式管理*******************************************************************
	//表达式管理主入口
	function expressionManage()
	{
		window.parent.expressionManage();
	}
	//获取上一页的表达式
	function getPreviousPageExpression()
	{
		if (($("#expressionCurrentPage",parent.document).text() - 1) < 1)
		{
			parent.$.dialog("已经是第一页", false, 1000);
		}
		else
		{
			getExpression($("#expressionCurrentPage",parent.document).text() - 1);
		}
	}
	//获取下一页的表达式
	function getNextPageExpression()
	{
		if (($("#expressionCurrentPage",parent.document).text() - 0 + 1) > $("#expressionTotalPage",parent.document).text())
		{
			parent.$.dialog("已经是最后一页", false, 1000);
		}
		else
		{
			getExpression($("#expressionCurrentPage",parent.document).text() - 0 + 1);
		}
	}
	//跳转至指定页数的表达式
	function jumpToExpression()
	{
		var forPage = $("#expression_pageNum",parent.document).val();
		if (forPage != "" && Number(forPage)!=0 && Number(forPage) <= Number($("#expressionTotalPage",parent.document).text()))
		{
			$("#expression_pageNum",parent.document).val("");
			getExpression(Number(forPage));
		}
		else
		{
			parent.$.dialog("跳转页数输入有误!请重新输入!", false, 1000);
			$("#expression_pageNum",parent.document).val("");
		}
	}
	//获取指定页数的表达式
	function getExpression(forPage)
	{
		$.ajax(
		{
			type : "post",
			url : "/hssp/head/getExpression",
			data : "&pageCount=15&pageNum=" + forPage,
			dataType : "json",
			success : function(result) 
			{
				var expressions = eval(result.expression);
				var htmls = "";
				for (var i = 0,size = expressions.length; i < size; i++)
				{
					var title_St = expressions[i].expression.replace(/\"/g,"&quot");
					if(expressions[i].deleteOk == "0"){
						//htmls = htmls + "<li class=\"col-lg-3 col-sm-6 col-xs-12\"><i class=\"l_close\">X</i><span>"+"创建者："+expressions[i].userid+"<span><div title=\"" + expressions[i].expression + "\">";
						htmls = htmls + "<li class=\"col-lg-3 col-sm-6 col-xs-12\"><i class=\"l_close\">X</i><div title=\"" + title_St + "\">";
					}else{
						htmls = htmls + "<li class=\"col-lg-3 col-sm-6 col-xs-12\"><i class=\"l_close\" style=\"display: none\">X</i><span>"+expressions[i].userid+"创建用户："+"<span><div title=\"" + expressions[i].expression + "\">";
					}
					htmls = htmls + expressions[i].expression;
					htmls = htmls + "</div><span style=\"display: none\">"+expressions[i].id+"<span></li>";
				}
				var bodyHeight = parent.document.getElementById("prebody").scrollHeight || parent.document.getElementById("results").scrollHeight;
				$("#div_expressionTotalInfo",parent.document).prev().height(bodyHeight - 130);
				$("#expressioinManage",parent.document).html(htmls);
				$("#expressionTotalInfo",parent.document).html(result.totalCount);
				$("#expressionTotalPage",parent.document).html(Math.ceil(result.totalCount/15));
				$("#expressionCurrentPage",parent.document).html(forPage);
				window.parent.bind_remove_expression();
				window.parent.bind_click_expression();
			}
		});
	}
	//****************************************************************高级检索********************************************************************
	//高级检索主入口
	function advancedSearch()
	{
		//parent.resultsDocument.openBlack();
		getSearchSet();
	}
	//获取高级检索界面的集合
	function getSearchSet()
	{
		$.ajax({
			type : "post",
			url : "/hssp/head/getSearchSet",
			dataType : "json",
			success : function(result)
			{
				var searchSetList = eval(result.searchSet);
				var htmls = "";
				for (var i = 0,size = searchSetList.length; i < size; i++)
				{
					if (i == 0)
						htmls = htmls + "<option selected=\"selected\">";
					else
						htmls = htmls + "<option>";
					htmls = htmls + searchSetList[i];
					htmls = htmls + "</option>";					
				}
				$("#select_searchSet", parent.document).html(htmls);
				getSearchField();
			}			
		});	
	}
	var filterKey="";
	//获取高级检索界面的集合对应字段
	function getSearchField(filterKeyValue)
	
	{
		if(!(typeof(filterKeyValue) =="undefined")){
			filterKey = filterKeyValue;
		}
		$.ajax({
			type : "post",
			url : "/hssp/head/getSearchField",
			data : "searchSet=" + $("#select_searchSet", parent.document).val(),
			dataType : "json",
			success : function(result)
			{
				var searchFieldList = eval(result.searchField);
				var htmls = "";
				for (var i = 0,size = searchFieldList.length; i < size; i++)
				{
					if(filterKey.length <= 0){
						if (i == 0)
							htmls = htmls + "<option selected=\"selected\">";
						else
							htmls = htmls + "<option>";
							
						htmls = htmls + searchFieldList[i];
						htmls = htmls + "</option>";
					}else{
						if(searchFieldList[i].split(filterKey).length > 1){
							if (i == 0)
								htmls = htmls + "<option selected=\"selected\">";
							else
								htmls = htmls + "<option>";
								
							htmls = htmls + searchFieldList[i];
							htmls = htmls + "</option>";	
						}
					}					
				}
				$("#select_searchField", parent.document).html(htmls);
				getSearchOperator();
			}			
		});			
	}
	
	
	
	//获取高级检索界面的运算符
	function getSearchOperator()
	{
		$.ajax({
			type : "post",
			url : "/hssp/head/getSearchOperator",
			data : "searchSet=" + $("#select_searchSet", parent.document).val() + "&searchField=" + $("#select_searchField", parent.document).val(),
			dataType : "json",
			success : function(result)
			{
				var searchOperatorList = eval(result.searchOperator);
				var htmls = "";
				for (var i = 0,size = searchOperatorList.length; i < size; i++)
				{
					if (searchOperatorList[i] == 0 || searchOperatorList[i] == 1 || searchOperatorList[i] == 3)
					{
						htmls = htmls + "<option selected=\"selected\">=</option>";
						htmls = htmls + "<option>大于</option>";
						htmls = htmls + "<option>小于</option>";
						htmls = htmls + "<option>大于或等于</option>";
						htmls = htmls + "<option>小于或等于</option>";
					}
					else if (searchOperatorList[i] == 2 || searchOperatorList[i] == 9)
					{
						htmls = htmls + "<option selected=\"selected\">同义包含</option>";
						htmls = htmls + "<option selected=\"selected\">绝对包含</option>";
						htmls = htmls + "<option>等于</option>";
						htmls = htmls + "<option>以XXX开头</option>";
						htmls = htmls + "<option>以XXX结尾</option>";
						$("#table_field", parent.document).val($("#select_searchSet", parent.document).val()+"."+$("#select_searchField", parent.document).val());
					}
					else if (searchOperatorList[i] ==4)
					{
						htmls = htmls + "<option selected=\"selected\">绝对包含</option>";
						htmls = htmls + "<option>等于</option>";
						htmls = htmls + "<option>以XXX开头</option>";
						htmls = htmls + "<option>以XXX结尾</option>";
						$("#table_field", parent.document).val($("#select_searchSet", parent.document).val()+"."+$("#select_searchField", parent.document).val());
					}
					
				}
				$("#searchValue",parent.document).val("");
				$("#select_searchOperator",parent.document).html(htmls);
				window.parent.advancedSearch();
			}			
		});			
	}
	//下载病种库导出文件
	function downloadFile(fileName,fileID)
	{
		$("#fileName").val(fileName);
		$("#fileID").val(fileID);
		$("#form_downloadFile").submit();
	}
	
	//**********************************************反馈提交*************************************************************
	//反馈提交主入口
	function feedbackManage()
	{
		window.parent.feedbackManage();
	}
	//**********************************************使用量统计*************************************************************
	function getDiseaseCountDetail()
	{
		window.parent.diseaseCountSearch();
	}
	//**********************************************热词统计*************************************************************
	function wordCloud()
	{
		window.parent.wordCloudSearch();
	}
</script>
</html>