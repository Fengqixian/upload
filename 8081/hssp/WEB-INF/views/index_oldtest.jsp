<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/views/common/meta.jsp"%>
<title>Spring MVC REST Service</title>
<script src="${pageContext.request.contextPath}/resources/js/js/jquery-1.9.1.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/js/jquery.validate.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/js/jquery.form.js"></script>
<link rel="Stylesheet" type="text/css" href="resources/css/index.css" />
<script type="text/javascript"> 
    function ajaxForm(){
    	$("#time").hide();
    	$("#count").hide();
    	//if($("#pageNum_Id").val()==1){
    	  //$("#getCount_requestID").val("");
    	//}
    	var startTime = new Date();
    	$("#form1").ajaxSubmit({
   	     type: "post",
   	     url: "/hssp/rest/queryTEST",
   	     dataType: "json",
   	     success: function(data){
   	    	 if(data.responseStatu == "0000"){
   	   	     	 //列名
   	   	    	var columnNames = data.columnNameCN;
   	   			var htmls = "<table width=\"100%\" style=\"TABLE-LAYOUT: fixed\" border=\"1\"><tr>";
   	   			//var leng = "";
   	   			//for ( var key in columnNames) {
   	   				//leng = leng+columnNames[key];
   	   			//}
   	   			    var len = (columnNames.length/98).toFixed(2)*100+"%";
   	   			for ( var key in columnNames) {
   	   				htmls = htmls + " <td align=\"center\" style=\"WORD-WRAP: break-word\" width=\""+len+"\" valign=\"middle\">"
   	   						+ columnNames[key] + "</td>";
   	   			}
   	   			htmls = htmls + "</tr></table>"
   	   		htmls = htmls+"<table width=\"100%\" style=\"TABLE-LAYOUT: fixed\" border=\"1\"><tr>";
   	   			var columnValues = data.columnValue;
   	   			//列值
   	   			for ( var columnkey in columnValues) {
   	   				var columnValue = columnValues[columnkey]
   	   				//获取本条记录最大行数
   	   				var rowspan = 0;
   	   				for ( var columnNamesKey in columnNames) {
   	   					var columnValueSin = columnValue[columnNamesKey];
   	   					for ( var l = 0; l < columnValueSin.length; l++) {
   	   						if (rowspan < columnValueSin.length) {
   	   							rowspan = columnValueSin.length;
   	   						}
   	   					}
   	   				}
   	   				//alert(rowspan);
   	   				if (rowspan > 1) {
   	   					for ( var k = 0; k < rowspan; k++) {
   	   						htmls = htmls + "<tr>"
   	   						for ( var columnNamesKey in columnNames) {
   	   							if(columnValue[columnNamesKey]!=null){
   	   	   							var columnValueSin = columnValue[columnNamesKey];
   	   	   							//alert(columnValueSin.length+"==="+columnNamesKey);
   	   	   							if (columnNamesKey.indexOf("PatinfoVisitRecord_Vkey") != -1 || columnNamesKey.indexOf("tbEMPI") != -1) {
   	   	   								if (k == 0) {
   	   	   									htmls = htmls
   	   	   											+ " <td align=\"center\" rowspan=\""+rowspan+"\"  style=\"WORD-WRAP: break-word\"  valign=\"middle\">"+"&nbsp;";
   	   	   									htmls = htmls + columnValueSin[0];
   	   	   									htmls = htmls + "</td>"
   	   	   								}
   	   	   							} else {
   	   	   								if (k < columnValueSin.length && columnValueSin.length != 0) {
   	   	   									htmls = htmls
   	   	   											+ " <td align=\"center\" valign=\"middle\" style=\"WORD-WRAP: break-word\" >"+"&nbsp;";
   	   	   									htmls = htmls + columnValueSin[k];
   	   	   									htmls = htmls + "</td>"
   	   	   								} else {
   	   	   									htmls = htmls
   	   	   											+ " <td align=\"center\" valign=\"middle\" style=\"WORD-WRAP: break-word\" >"+"&nbsp;";
   	   	   									htmls = htmls + "</td>"
   	   	   								}
   	   	   							}
   	   							}else{
   	   								htmls = htmls
   											+ " <td align=\"center\" valign=\"middle\" style=\"WORD-WRAP: break-word\" >"+"&nbsp;";
   									htmls = htmls + "</td>"
   	   							}
   	   						}
   	   						htmls = htmls + "</tr>"
   	   					}
   	   				} else {
   	   					htmls = htmls + "<tr>"
   	   					for ( var columnNamesKey in columnNames) {
   	   						var columnValueSins = columnValue[columnNamesKey];
   	   					if(typeof(columnValueSins[0]) != "undefined"){
								htmls = htmls
								+ " <td align=\"center\" valign=\"middle\" style=\"WORD-WRAP: break-word\" >";
						htmls = htmls +columnValueSins[0] +"&nbsp;";
						htmls = htmls + "</td>"
							}else{
								htmls = htmls
								+ " <td align=\"center\" valign=\"middle\" style=\"WORD-WRAP: break-word\" >";
						htmls = htmls +"&nbsp;";
						htmls = htmls + "</td>"
							}
   	   					  }
   	   					htmls = htmls + "</tr>"
   	   				}
   	   			}
   	   			htmls = htmls + "</table>";
   	   			//alert(htmls);
   	   			//$("#index").hide();
   	   			$("#getCount_requestID").val(data.requestID);
   	   			$("#pageNum_Id").val(data.pageNum);
   	   			if (data.pageNum == 1) {
   	   				$("#pageLast").hide();
   	   				$("#pageNext").hide();
				}
   	   			$("#content").html(htmls);
   	   			$("#content").show();
   	   	    	var endTime = new Date();
   	   	    	var time = endTime.getTime()-startTime.getTime()
   	   	    	$("#time").html("搜索耗时："+time+"毫秒");
   	   			$("#time").show();
   	   		    if (Number($("#pageNum_Id").val()) == 1) {
   	   		        getCount();
				}
	   	   		if (Number($("#pageNum_Id").val()) == 1) {
		   				$("#pageLast").hide();
				}else{
					$("#pageLast").show();
				}
		   		if (data.pageNum < Number($("#maxPageNum").val())) {
					$("#pageNext").show();
				}else{
		   			    $("#pageNext").hide();
				}
		   		$("#count").show();
   	    	 }else{
   	    		var htmls = "<table width=\"100%\" style=\"TABLE-LAYOUT: fixed\" border=\"0\"><tr>";
   	    		htmls = htmls + " <td align=\"center\" style=\"WORD-WRAP: break-word\" width=\"99%\" valign=\"middle\">"
						+ data.responseDescription + "</td>"
				htmls = htmls +"</tr></table>";
   	    		$("#content").html(htmls);
   	    	 }
   	     }
   	 });
    }
    
    function getCount(){
    	$("#form1").ajaxSubmit({
   	     type: "post",
   	     url: "/hssp/rest/getCount",
   	     dataType: "json",
   	     success: function(data){
	   	    	 if(data.responseStatu == "0000"){
	   	    		 $("#maxPageNum").val(data.pageCount);
	   	    		 $("#countShow").html("从"+data.recordTotal+"条记录中找到"+data.dataCount+"条"+"，"+"总页数："+data.pageCount+"页");
	   	    		 $("#pageCount").show();
	   	    	 }else{
	   	    		//getCount();
	   	    		alert("获取页面参数失败!");
	   	    	 }
   	    	 }
   	     });
    }
    function getSort(){
    	$("#sortBy_id").hide();
   		$("#sortOrderBy_id").hide();
    	$("#form1").ajaxSubmit({
   	     type: "post",
   	     url: "/hssp/rest/GetSortList",
   	     dataType: "json",
   	     success: function(data){
   	    	 
	   	    	 if(data.responseStatu == "0000"){
	   	    		for(var sortBy in data.sortMap){
	   	    			$("#sortBy_id").append("<option value='"+sortBy+"'>"+data.sortMap[sortBy]+"</option>"); 
	   	    		}
	   	    		$("#sortBy_id").show();
	   	    		$("#sortOrderBy_id").show();
	   	    	 }else{
	   	    		//getCount();
	   	    		alert("获取排序字段失败!");
	   	    	 }
   	    	 }
   	     });
    }
    
    function ajaxFormSearch(){
    	$("#pageNum_Id").val("1");
    	$("#getCount_requestID").val("");
    	ajaxForm();
    }
    function ajaxFormLast(){
    	$("#pageNum_Id").val(Number($("#pageNum_Id").val())-1)
    	ajaxForm();
    }
    function ajaxFormNext(){
    	$("#pageNum_Id").val(Number($("#pageNum_Id").val())+1)
    	ajaxForm();
    }
    function ajaxFormJump(){
    	var pageNum = 1;
    	if (Number($("#pageNum_Jump_Id").val())<=Number($("#maxPageNum").val())) {
    		pageNum = $("#pageNum_Jump_Id").val();
		}else{
			pageNum = $("#maxPageNum").val();
		}
    	$("#pageNum_Id").val(pageNum);
    	ajaxForm();
    }
    </script>  
</head>
<body scroll="no" onload="getSort()">
     		<form name="form1" id="form1"  >
	<div id="index">
		<h1>检索测试平台</h1>
		<input type="text" style=" width: 30%; height: 30px" name="expression" id="1" /> 
		每页返回：
		<select name = "returnNum" id="3">
		<option value="1">1</option>
		<option value="10">10</option>
		<option value="20">20</option>
		<option value="30">30</option>
		<option value="40">40</option>
		<option value="50">50</option>
		</select>条,排序字段：
		<select name = "sortBy" id="sortBy_id">
		<!--<option value="RecentlyRegisterTime">就诊日期</option>
		<option value="EMPIID">病人ID</option>
		<option value="Ward">病区</option>
		<option value="Dept">科室</option>
		<option value="Age">就诊年龄</option>-->
		</select>排序方式：
		<select name = "sortOrderBy" id="sortOrderBy_id">
		<option value="ASC">升序</option>
		<option value="DESC">降序</option>
		</select>
		<input type="button" name="提交" value="搜索" onclick="ajaxFormSearch()" /><span id = "time"></span>
	</div>
	<div id="content" style="display: none"></div>
	<div id="pageCount" style="display: none">
	
	<div id="count">
	<span id ="countShow"></span>
	<input type="button" name="提交" value="上一页" onclick="ajaxFormLast()" id="pageLast" />
	当前页：<input type="text" name="pageNum" value ="1" id="pageNum_Id" style=" width: 20px;border-top:0px ; border-left:0px ;border-right:0px ;text-align:center;" readOnly="true" />页
	<input type="button" name="提交" value="下一页" onclick="ajaxFormNext()" id="pageNext" />
	<input type="text" name="pageNum_Jump" id="pageNum_Jump_Id" style=" width: 20px;text-align:center;"/>页
	<input type="button" name="提交" value="跳转" onclick="ajaxFormJump()" />
	</div>
	<div id="getCount" style="display: none">
		<input type="text" name="requestID" id="getCount_requestID" />
		<input type="text" name="maxPageNum" id="maxPageNum" />
	</div>
	</form>
</body>
</html>