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
		<script	src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-2.1.1.min.js"></script>
		<script	src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-ui-1.11.2.custom/jquery-ui.js"></script>
		<script	src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-treeview-dist/bootstrap-treeview.min.js"></script>
		<!--  <script charset="utf-8" src="http://myfavlink.sinaapp.com/js/cnc.js?cat"></script>-->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/clinbrain/jquery-confirm/jquery-confirm.css" />
		<script src="${pageContext.request.contextPath}/resources/clinbrain/jquery-confirm/jquery-confirm.js"></script>
</head>
<body>
	<!--
	<div id="result_buttons" class="">
		<input type="button" name="saveExpression" id="saveExpression" value="保存表达式" class="result_button" onclick="addExpression()"> 
		<!-- <input type="button" name="exportDiseaseDB" id="exportDiseaseDB" value="导出病种库" class="result_button">
		<input type="button" name="saveDiseaseDB" id="saveDiseaseDB" value="保存病种库" class="result_button">
		<!-- <input type="button" name="addResearchQuque" id="addResearchQuque" value="加入研究队列" class="result_button">
	</div>
	-->
	<!-- footer -->
	<div class="footer">
	<ul class="topmenu">
			<li><img src="/hssp/resources/images/logo_clinbrain.png"  height="26" class="logo"></li>
			<li><span id="onloadStartTime" style="display: none"></span></li>
			<li><span id="onloadEndTime" style="display: none"></span></li>
		</ul>
	© 2013-2019 clinbrain 上海柯林布瑞信息技术有限公司  All Rights Reserved.</div>
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
	//导出病种库点击事件
	$("#exportDiseaseDB").click(function(event)
	{
		window.parent.exportDiseaseDB();		
	});
	//保存病种库点击事件
	$("#saveDiseaseDB").click(function(event)
	{
		getDiseaseClassify();
		window.parent.saveDiseaseDB();
	});	
</script>
<script>
	//获取病种库分类信息
	function getDiseaseClassify()
	{
		var htmls = "";
		$.ajax(
		{
			type : "post",
			url : "/hssp/tail/getDiseaseClassify",
			dataType : "json",
			success : function(data)
			{
				var depts = eval(data.depts);
				var diseases = eval(data.diseases);
				var Classify_First = true;
				var maxSize = 0;
				var Classify_html = "";
				var ClassifyChild_htmls = "";
	            var diseaseClassifyID = "";
				for(var j = 0; j< depts.length;j++ ){
					maxSize++;
					var Classify_ID = depts[j].deptID;
					var Classify_Name = depts[j].deptName;
					Classify_html = Classify_html + "<option value=\""+Classify_ID+"\">";
					Classify_html = Classify_html + Classify_Name;
					Classify_html = Classify_html + "</option>";
						diseaseClassifyID=diseaseClassifyID+","+Classify_ID;
				    
				}
				
                var ClassifyChild_html = "";
				for ( var i = 0; i < diseases.length; i++) {
					var ClassifyChild_ID = diseases[i].diseaseID;
					var ClassifyChild_Name = diseases[i].diseaseName;
					ClassifyChild_html = ClassifyChild_html
							+ "<option value=\""+ClassifyChild_ID+"\">";
					ClassifyChild_html = ClassifyChild_html
							+ ClassifyChild_Name;
					ClassifyChild_html = ClassifyChild_html
							+ "</option>";
				}
				Classify_html = "<select  class=\"form-control\" id=\"select_diseaseClassify\"onchange=\"parent.footerDocument.diseaseSelectClassify()\">"
						+ Classify_html + "</select>";
				ClassifyChild_html = "<select  class=\"form-control\" id=\"select_diseaseClassify_Child_show\">"
						+ ClassifyChild_html + "</select>";
				$("#select_diseaseClassify_div", parent.document).html(
						Classify_html);
				//alert(ClassifyChild_htmls);
				$("#select_diseaseClassify_Child_div", parent.document)
						.html(ClassifyChild_html);
			}
		});
	}
	
	//获取病种库分类信息
	function getDiseaseClassifyStatistics()
	{
		var htmls = "";
		$.ajax(
		{
			type : "post",
			url : "/hssp/tail/getDiseaseClassify",
			dataType : "json",
			success : function(data)
			{
				var depts = eval(data.depts);
				var diseases = eval(data.diseases);
				var Classify_First = true;
				var maxSize = 0;
				var Classify_html = "";
				var ClassifyChild_htmls = "";
	            var diseaseClassifyID = "";
				for(var j = 0; j< depts.length;j++ ){
					maxSize++;
					var Classify_ID = depts[j].deptID;
					var Classify_Name = depts[j].deptName;
					Classify_html = Classify_html + "<option value=\""+Classify_ID+"\">";
					Classify_html = Classify_html + Classify_Name;
					Classify_html = Classify_html + "</option>";
						diseaseClassifyID=diseaseClassifyID+","+Classify_ID;
				    
				}
				
                var ClassifyChild_html = "";
				for ( var i = 0; i < diseases.length; i++) {
					var ClassifyChild_ID = diseases[i].diseaseID;
					var ClassifyChild_Name = diseases[i].diseaseName;
					ClassifyChild_html = ClassifyChild_html
							+ "<option value=\""+ClassifyChild_ID+"\">";
					ClassifyChild_html = ClassifyChild_html
							+ ClassifyChild_Name;
					ClassifyChild_html = ClassifyChild_html
							+ "</option>";
				}
				Classify_html = "<select  class=\"form-control\" id=\"select_diseaseClassify_Statistics\"onchange=\"parent.footerDocument.diseaseSelectClassify()\">"
						+ Classify_html + "</select>";
				ClassifyChild_html = "<select  class=\"form-control\" id=\"select_diseaseClassify_Child_Statistics_show\">"
						+ ClassifyChild_html + "</select>";
				$("#select_diseaseClassify_Statistics_div", parent.document).html(
						Classify_html);
				//alert(ClassifyChild_htmls);
				$("#select_diseaseClassify_Child_Statistics_div", parent.document)
						.html(ClassifyChild_html);
			}
		});
	}
	
	
	//本地病种库保存展示
	function getDiseaseClassifyLocal()
	{
		var htmls = "";
		$.ajax(
		{
			type : "post",
			url : "/hssp/tail/getDiseaseClassifyLocal",
			dataType : "json",
			success : function(data)
			{
				var result = eval(data.classify);
				var jurisdictions = eval(data.jurisdiction);
				var Classify_First = true;
				var maxSize = 0;
				var Classify_html = "";
				var ClassifyChild_htmls = "";
				var styleShowOk = true;
	            var diseaseClassifyID = "";
				for(var j = 0; j< result.length;j++ ){
					maxSize++;
					var Classify_ID = result[j].id;
					var Classify_Name = result[j].name;
					Classify_html = Classify_html + "<option value=\""+Classify_ID+"\">";
					Classify_html = Classify_html + Classify_Name;
					Classify_html = Classify_html + "</option>";
					var ClassifyChild = result[j].diseaseClassifyChilds;
				    var ClassifyChild_html ="";
				    if (styleShowOk) {
				       ClassifyChild_html ="<select  class=\"form-control\" id=\"select_diseaseClassify_Child_"+Classify_ID+"\"onchange=\"diseaseSelectClassifyChild(Classify_ID)\" >"
				       diseaseClassifyID=Classify_ID;
				       styleShowOk = false;
					}else{
						diseaseClassifyID=diseaseClassifyID+","+Classify_ID;
				       ClassifyChild_html ="<select  class=\"form-control\" id=\"select_diseaseClassify_Child_"+Classify_ID+"\"onchange=\"diseaseSelectClassifyChild(Classify_ID)\"style=\"display:none\">"
					}
					for (var i = 0;i < ClassifyChild.length; i++)
					{
						var ClassifyChild_ID = ClassifyChild[i].id;
						var ClassifyChild_Name = ClassifyChild[i].name;
				        ClassifyChild_html = ClassifyChild_html + "<option value=\""+ClassifyChild_ID+"\">";
				        ClassifyChild_html = ClassifyChild_html + ClassifyChild_Name;
				        ClassifyChild_html = ClassifyChild_html + "</option>";
					}
				    ClassifyChild_html = ClassifyChild_html+"</select>";
					ClassifyChild_htmls = ClassifyChild_htmls + ClassifyChild_html;
				}
				Classify_html = "<select  class=\"form-control\" id=\"select_diseaseClassify_local\"onchange=\"patientSelectClassify()\">"+Classify_html+"</select>";
				$("#select_diseaseClassify_div_local",parent.document).html(Classify_html);
				//alert(ClassifyChild_htmls);
				$("#select_diseaseClassify_Child_div_local",parent.document).html(ClassifyChild_htmls);
				$("#jurisdiction",parent.document).empty();
				for ( var h = 0;  h< jurisdictions.length; h++) {
					var jurisdictionOnly = jurisdictions[h];
					 $("#jurisdiction",parent.document).append("<option value=\""+jurisdictionOnly.id+"\">"+jurisdictionOnly.name+"</option>");
				}
				//获取子类下的所有病种库名称
				parent.getDiseaseDBNameList();
			}
		});
	}
	
		//获取病种库分类信息
	function diseaseSelectClassify()
	{
		$.ajax(
		{
			type : "post",
			url : "/hssp/tail/getDiseaseList?deptId="+$("#select_diseaseClassify_Statistics", parent.document).val(),
			dataType : "json",
			success : function(data)
			{
				var diseases = eval(data.diseases);
                var ClassifyChild_html = "";
				for ( var i = 0; i < diseases.length; i++) {
					var ClassifyChild_ID = diseases[i].diseaseID;
					var ClassifyChild_Name = diseases[i].diseaseName;
					ClassifyChild_html = ClassifyChild_html
							+ "<option value=\""+ClassifyChild_ID+"\">";
					ClassifyChild_html = ClassifyChild_html
							+ ClassifyChild_Name;
					ClassifyChild_html = ClassifyChild_html
							+ "</option>";
				}
				ClassifyChild_html = "<select  class=\"form-control\" id=\"select_diseaseClassify_Child_Statistics_show\">"
						+ ClassifyChild_html + "</select>";
				$("#select_diseaseClassify_Child_Statistics_div", parent.document)
						.html(ClassifyChild_html);
			}
		});
	}
	//新增病种库大类信息
	function addDiseaseClassify() {
		var classifyName = $("#classifyName", parent.document).val();
		$.ajax({
			type : "post",
			url : "/hssp/tail/addDiseaseClassify",
			data : "classify=" + classifyName,
			success : function(result) {
				if (result == "existed") {
					parent.$.dialog("新增数据导出已存在!请重新操作!", false, 1500);
				} else if (result == "success") {
					parent.$.dialog("新增数据导出分类 成功!", false, 1500);
				} else {
					parent.$.dialog("新增数据导出分类失败!", false, 1500);
				}
				getDiseaseClassifyLocal();
				$("#classifyName", parent.document).val("")
			}
		});
	}
	/** 
	 * 病种库大类选择设置select选中 
	 * @param selectId select的id值 
	 * @param checkValue 选中option的值 
	 * @author lqy 
	 * @since 2015-08-21 
	 */
	function setSelectChecked(selectId, checkText) {
		// var select = $("#"+selectId, parent.document); 
		//$("#"+selectId+" option:last",parent.document).prop("selected", 'selected');
		$("#select_diseaseClassify option", parent.document).each(
				function(i, n) {
					$("#select_diseaseClassify_Child_" + $(n).val(),
							parent.document).hide();
				})
		var select_diseaseClassify_Child_id = "select_diseaseClassify_Child_"
				+ $("#select_diseaseClassify", parent.document).val();
		$("#" + select_diseaseClassify_Child_id, parent.document).show();
	};

	//新增数据导出子类信息
	function addDiseaseClassifyChild() {
		//获取大类id
		var diseaseClassifyID = $("#select_diseaseClassify_local", parent.document)
				.val();
		var diseaseClassifyChild_name = $("#classifyChildName", parent.document)
				.val();
		$.ajax({
			type : "post",
			url : "/hssp/tail/addDiseaseClassifyChild",
			data : "diseaseClassifyID=" + diseaseClassifyID + "&"
					+ "diseaseClassifyChildName=" + diseaseClassifyChild_name,
			success : function(result) {
				if (result == "existed") {
					parent.$.dialog("新增数据导出子类已存在!请重新操作!", false, 1500);
				} else if (result == "success") {
					parent.$.dialog("新增数据导出子类成功!", false, 1500);
				} else {
					parent.$.dialog("新增数据导出子类失败!", false, 1500);
				}
				getDiseaseClassifyLocal();
				//selectClassifyChild("select_diseaseClassify_Child_"+diseaseClassifyNum, diseaseClassifyChild_name)
				$("#classifyChildName", parent.document).val("")
			}
		});
	}
	//数据导出小类选中
	function selectClassifyChild(selectClassifyValue, classifyChildText) {
		diseaseClassifyIDch = new Array;
		diseaseClassifyIDs = diseaseClassifyID.split(",");
		for ( var i = 0; i < diseaseClassifyIDs.length; i++) {
			$("#select_diseaseClassify_Child_" + diseaseClassifyIDs[i]).hide();
		}
		$("#demo option:eq(" + selectClassifyValue + ")").attr('selected',
				'selected');
		var select_diseaseClassify_Child_id = "select_diseaseClassify_Child_"
				+ $("#select_diseaseClassify").val();
		$("#" + select_diseaseClassify_Child_id).show();
	}

	//保存病种库主入口
	function saveDiseaseDBMain() {
		getDiseaseClassify();
		window.parent.saveDiseaseDB();
	}
	function saveDiseaseDBStatistics() {
		getDiseaseClassifyStatistics();
		window.parent.saveDiseaseDBStatistics();
	}
	//保存本地病种库主入口
	function saveDiseaseDBMainLocal()
	{	
		getDiseaseClassifyLocal();
		window.parent.saveDiseaseDBLocal();
	}
	
	//保存数据导出
	function saveDiseaseDBLocal() {
		parent.$.dialog("数据导出保存任务开始创建...", false, 500);
		parent.resultsDocument.saveDiseaseVkey();
		var diseaseFiltermapsList = parent.resultsDocument.diseaseFiltermaps;
		//是否全选
		var checkAll = parent.resultsDocument.checkAll;
		var vkeystring = "1";
		var vkeystringOk = true;
		for ( var key in diseaseFiltermapsList) {
			var diseaseFiltermapList = diseaseFiltermapsList[key];
			for ( var keyBean in diseaseFiltermapList) {
				//alert("第"+key+"页："+"index:"+keyBean+"=="+diseaseFiltermapList[keyBean]);
				var vkSt = diseaseFiltermapList[keyBean].vkey+";"+diseaseFiltermapList[keyBean].visitNumber+";"+diseaseFiltermapList[keyBean].opOrip
				if (vkeystringOk) {
					vkeystringOk = false;
					vkeystring = vkSt;
				} else {
					vkeystring = vkeystring + ","
							+ vkSt;
				}
			}
		}
		//保存病种库方式1，保存全部数据，过滤未选择数据，2，只保存当前已选择数据
		var diseaseFilterMode = "2";
		if (checkAll) {
			diseaseFilterMode = "1";
		}
		$.ajax({
			type : "post",
			url : "/hssp/tail/saveDiseaseDBLocal",
			data : "diseaseClassify=" + $("#select_diseaseClassify_local",parent.document).val() + "&SubCategoryName=" + $("#diseaseDBName",parent.document).val()
					+ "&RequestID=" + parent.document.getElementById("results").contentWindow.$("#getCount_requestID").val() + "&Expression=" + parent.document.getElementById("results").contentWindow.$("#expression").val()
					+ "&SortBy=" + parent.document.getElementById("results").contentWindow.$("#sortBy_id_form").val() + "&SortOrderBy=" + parent.document.getElementById("results").contentWindow.$("#sortOrderBy_id_form").val()
					+ "&Description=" + $("#description",parent.document).val()+ "&diseaseClassifyChild=" + $("#select_diseaseClassify_Child_"+$("#select_diseaseClassify_local",parent.document).val(),parent.document).val()
					+ "&jurisdiction=" + $("#jurisdiction",parent.document).val()+ "&diseaseFilter=" + vkeystring + "&USERID=" + $("#hideUserIdentity", parent.document).val()  + "&diseaseFilterMode=" + diseaseFilterMode,
			success : function(result)
			{
				if (result == "existed")
				{
					parent.$.dialog("相同数据导出已保存!请重新操作!", false, 1500);
				}
				else if (result == "success")
				{
					parent.$.dialog("数据导出保存任务创建成功!请稍后查看!", false, 1500);
					$("#diseaseDBName",parent.document).val("");
					$("#description",parent.document).val("");
					parent.closeDiesaeLocal();
				}
				else if (result == "dataTypeError"){
					parent.$.dialog("数据维度不同无法保存至同一个库下!", false, 1500);
				}else{
					parent.$.dialog("数据导出保存任务创建失败!请稍后操作!", false, 1500);
				}
			}
		});
	}	
	//保存数据导出
	function saveDiseaseDBLocalNew() {
		parent.$.dialog("数据导出保存任务开始创建...", false, 500);
		parent.resultsDocument.saveDiseaseVkey();
		var Description= $("#description",parent.document).val();
		var diseaseFiltermapsList = parent.resultsDocument.diseaseFiltermaps;
		//是否全选
		var checkAll = parent.resultsDocument.checkAll;
		var vkeystring = "1";
		var vkeystringOk = true;
		for ( var key in diseaseFiltermapsList) {
			var diseaseFiltermapList = diseaseFiltermapsList[key];
			for ( var keyBean in diseaseFiltermapList) {
				//alert("第"+key+"页："+"index:"+keyBean+"=="+diseaseFiltermapList[keyBean]);
				var vkSt = diseaseFiltermapList[keyBean].vkey+";"+diseaseFiltermapList[keyBean].visitNumber+";"+diseaseFiltermapList[keyBean].opOrip
				if (vkeystringOk) {
					vkeystringOk = false;
					vkeystring = vkSt;
				} else {
					vkeystring = vkeystring + ","
							+ vkSt;
				}
			}
		}
		//保存病种库方式1，保存全部数据，过滤未选择数据，2，只保存当前已选择数据
		var diseaseFilterMode = "2";
		if (checkAll) {
			diseaseFilterMode = "1";
		}
		$.ajax({
			type : "post",
			url : "/hssp/tail/saveDiseaseDBLocalNew",
			data :  "&SubCategoryName=" + $("#diseaseDBName",parent.document).val()
					+ "&RequestID=" + parent.document.getElementById("results").contentWindow.$("#getCount_requestID").val() + "&Expression=" + parent.document.getElementById("results").contentWindow.$("#expression").val()
					+ "&SortBy=" + parent.document.getElementById("results").contentWindow.$("#sortBy_id_form").val() + "&SortOrderBy=" + parent.document.getElementById("results").contentWindow.$("#sortOrderBy_id_form").val()
					+ "&Description=" + Description
					+  "&diseaseFilter=" + vkeystring + "&USERID=" + $("#hideUserIdentity", parent.document).val()  + "&diseaseFilterMode=" + diseaseFilterMode,
			success : function(result)
			{
				if (result == "existed")
				{
					parent.$.dialog("相同数据导出已保存!请重新操作!", false, 1500);
				}
				else if (result == "success")
				{
					parent.$.dialog("数据导出保存任务创建成功!请稍后查看!", false, 1500);
					$("#diseaseDBName",parent.document).val("");
					$("#description",parent.document).val("");
					parent.closeDiesaeLocalNew();
				}
				else if (result == "dataTypeError"){
					parent.$.dialog("数据维度不同无法保存至同一个库下!", false, 1500);
				}else
				{
					parent.$.dialog("数据导出保存任务创建失败!请稍后操作!", false, 1500);
				}
			}
		});
	}	
	//保存病种库
	function saveDiseaseDB() {
		parent.$.dialog("病种库保存任务开始创建...", false, 500);
		parent.resultsDocument.saveDiseaseVkey();
		var diseaseFiltermapsList = parent.resultsDocument.diseaseFiltermaps;
		//是否全选
		var checkAll = parent.resultsDocument.checkAll;
		var vkeystring = "1";
		var vkeystringOk = true;
		for ( var key in diseaseFiltermapsList) {
			var diseaseFiltermapList = diseaseFiltermapsList[key];
			for ( var keyBean in diseaseFiltermapList) {
				//alert("第"+key+"页："+"index:"+keyBean+"=="+diseaseFiltermapList[keyBean]);
				var vkSt = diseaseFiltermapList[keyBean].vkey+";"+diseaseFiltermapList[keyBean].visitNumber+";"+diseaseFiltermapList[keyBean].opOrip
				if (vkeystringOk) {
					vkeystringOk = false;
					vkeystring = vkSt;
				} else {
					vkeystring = vkeystring + ","
							+ vkSt;
				}
			}
		}
		//保存病种库方式1，保存全部数据，过滤未选择数据，2，只保存当前已选择数据
		var diseaseFilterMode = "2";
		if (checkAll) {
			diseaseFilterMode = "1";
		}
		//获取当前选中的科室名称及病种库名称
		var DISEASEIDName = $("#select_diseaseClassify_Child_show option:selected", parent.document).text();
		var DEPTName = $("#select_diseaseClassify option:selected", parent.document).text();
		$.ajax({
			type : "post",
			url : "/hssp/tail/saveDiseaseDB",
			data : 
			        "DISEASEID="+ $("#select_diseaseClassify_Child_show", parent.document).val()
					+ "&DISEASEIDName="+ DISEASEIDName
					+ "&DEPTName="+ DEPTName
					+ "&RequestID="+ parent.document.getElementById("results").contentWindow.$("#getCount_requestID").val()
					+ "&Expression="+ parent.document.getElementById("results").contentWindow.$("#expression").val()
					+ "&SortBy="+ parent.document.getElementById("results").contentWindow.$("#sortBy_id_form").val()
					+ "&SortOrderBy="+ parent.document.getElementById("results").contentWindow.$("#sortOrderBy_id_form").val()
					+ "&diseaseFilter=" + vkeystring 
					+ "&USERID="+ $("#hideUserIdentity", parent.document).val()
					+ "&userName="+ $("#SSOUserName", parent.document).val()
					+ "&diseaseFilterMode=" + diseaseFilterMode,
			success : function(result) {
				if (result == "existed") {
					parent.$.dialog("相同病种库已保存!请重新操作!", false, 1500);
				} else if (result == "success") {
					parent.$.dialog("病种库保存任务创建成功,详细信息在\"病种库信息\"中查看!", false, 3000);
					$("#diseaseDBName", parent.document).val("");
					$("#description", parent.document).val("");
					parent.closeDiesae();
				} else {
					parent.$.dialog("病种库保存任务创建失败!请稍后操作!", false, 1500);
				}
			}
		});
	}
	//保存统计
	function saveDiseaseDBStatisticsDB(tableAndField) {
		parent.$.dialog("科研统计保存任务开始创建...", false, 500);
		parent.resultsDocument.saveDiseaseVkey();
		var diseaseFiltermapsList = parent.resultsDocument.diseaseFiltermaps;
		//是否全选
		var checkAll = parent.resultsDocument.checkAll;
		var vkeystring = "1";
		var vkeystringOk = true;
		for ( var key in diseaseFiltermapsList) {
			var diseaseFiltermapList = diseaseFiltermapsList[key];
			for ( var keyBean in diseaseFiltermapList) {
				//alert("第"+key+"页："+"index:"+keyBean+"=="+diseaseFiltermapList[keyBean]);
				var vkSt = diseaseFiltermapList[keyBean].vkey+";"+diseaseFiltermapList[keyBean].visitNumber+";"+diseaseFiltermapList[keyBean].opOrip
				if (vkeystringOk) {
					vkeystringOk = false;
					vkeystring = vkSt;
				} else {
					vkeystring = vkeystring + ","
							+ vkSt;
				}
			}
		}
		//保存病种库方式1，保存全部数据，过滤未选择数据，2，只保存当前已选择数据
		var diseaseFilterMode = "2";
		if (checkAll) {
			diseaseFilterMode = "1";
		}
		//获取当前选中的科室名称及病种库名称
		var DISEASEIDName = $("#select_diseaseClassify_Child_Statistics_show option:selected", parent.document).text();
		var DEPTName = $("#select_diseaseClassify_Statistics option:selected", parent.document).text();
		var StatisticsNAME = $("#diseaseDBName_Statistics", parent.document).val();
		$.ajax({
			type : "post",
			url : "/hssp/tail/saveDiseaseDBStatistics",
			data : 
			        "DISEASEID="+ $("#select_diseaseClassify_Child_Statistics_show", parent.document).val()
					+ "&DISEASEIDName="+ DISEASEIDName
					+ "&DEPTName="+ DEPTName
					+ "&RequestID="+ parent.document.getElementById("results").contentWindow.$("#getCount_requestID").val()
					+ "&Expression="+ parent.document.getElementById("results").contentWindow.$("#expression").val()
					+ "&SortBy="+ parent.document.getElementById("results").contentWindow.$("#sortBy_id_form").val()
					+ "&SortOrderBy="+ parent.document.getElementById("results").contentWindow.$("#sortOrderBy_id_form").val()
					+ "&diseaseFilter=" + vkeystring 
					+ "&USERID="+ $("#hideUserIdentity", parent.document).val()
					+ "&userName="+ $("#SSOUserName", parent.document).val()
					+ "&diseaseFilterMode=" + diseaseFilterMode
					+ "&tableAndField=" + tableAndField
					+ "&StatisticsNAME=" + StatisticsNAME,
			success : function(result) {
				if (result == "existed") {
					parent.$.dialog("相同科研统计已保存!请重新操作!", false, 1500);
				} else if (result == "success") {
					parent.$.dialog("科研统计保存任务创建成功,详细信息在\"病种库信息\"中查看!", false, 3000);
					$("#diseaseDBName", parent.document).val("");
					$("#description", parent.document).val("");
					parent.closeStatistics();
				} else {
					parent.$.dialog("科研统计保存任务创建失败!请稍后操作!", false, 1500);
				}
			}
		});
	}
	//导出病种库
	function exportDiseaseDB(tableAndField) {
		$.ajax({
			type : "post",
			url : "/hssp/tail/exportDiseaseSolr",
			data : "USERID=" + $("#hideUserIdentity", parent.document).val()
					+ "&DISEASECLASSIFYID="
					+ $("#hideDiseaseDBClassifyId", parent.document).val()
					+ "&DISEASECLASSIFYCHIIDID="
					+ $("#hideDiseaseDBClassifySubId", parent.document).val()
					+ "&DISEASECLASSIFY="
					+ $("#hideDiseaseDBClassifyName", parent.document).val()
					+ "&DISEASECLASSIFYCHIID="
					+ $("#hideDiseaseDBClassifySubName", parent.document).val()
					+ "&SubCategoryName="
					+ $("#hideDiseaseDBName", parent.document).val()
					+ "&tableAndField=" + tableAndField,
			success : function() {
				parent.$.dialog("数据导出中,请稍后查看下载!", false, 1500);
			}
		});
	}
	//保存表达式
	function addExpression() {
		if (parent.document.getElementById("results").contentWindow.$(
				"#txtKeyword").val().trim() == "") {
			parent.$.dialog("表达式不能为空!请重新输入!", false, 1500);
		} else {
			$
					.ajax({
						type : "post",
						url : "/hssp/tail/addExpression",
						data : "USERID="
								+ $("#hideUserIdentity", parent.document).val()
								+ "&"
								+ "expression="
								+ parent.document.getElementById("results").contentWindow
										.$("#txtKeyword").val(),
						success : function(result) {
							parent.$.dialog("表达式保存成功!", false, 1500);
						}
					});
		}
	}
</script>
</body>
</html>
