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
		<script	src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-ui-1.11.2.custom/jquery-ui.js"></script>
		<script	src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-treeview-dist/bootstrap-treeview.min.js"></script>
		<!--  <script charset="utf-8" src="http://myfavlink.sinaapp.com/js/cnc.js?cat"></script>-->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/clinbrain/jquery-confirm/jquery-confirm.css" />
		<script src="${pageContext.request.contextPath}/resources/clinbrain/jquery-confirm/jquery-confirm.js"></script>
</head>
<body>
     <div class="floatDivWrap" id="show_omaha_div_id" style="">
        <div id="show_omaha_div_div_id" class="floatDiv">
        	<div class="floatDivContent" id="show_omaha_div_div_div_id">
            </div>
            <span id="show_omaha_div_span_id" class="floatBtn" onclick="window.parent.showFancytree('prebody')">术语树</span>
        </div>
    </div>
    
	<div id="container">
		<div class="container">
			<div id="prebody_Logo">
			  <h1><img src="./resources/images/logo_clinbrain.png" style="height: 60px;"><p>临床大数据搜索引擎</p></h1>
			</div>
			<!-- 
			精卫医院logo
			<h1><img src="${pageContext.request.contextPath}/resources/images/jw_result.png"   style="height:60px;"><p>临床大数据搜索引擎</p></h1> 
			大华医院logo
			<h1><p><img src="${pageContext.request.contextPath}/resources/images/dahua_logo.png"   style="height:100px;">  临床大数据搜索引擎</p></h1>
			长海医院logo
			<h1><img src="${pageContext.request.contextPath}/resources/images/changhailogo_result.png" height="100px"><p>临床大数据搜索引擎</p></h1>
			公司logo
			<h1><img src="./resources/images/logo_clinbrain.png" style="height: 60px;"><p>临床大数据搜索引擎</p></h1>
			温附二医院
			<h1><img src="./resources/images/wenzhou_logo_index.png" style="height: 70px;"><p>临床大数据搜索引擎</p></h1>
			-->
			<div class="row">
				<form name="form1"  method="post" action="" class="search_form" onkeydown="if(event.keyCode==13)return false;" id="txtKeyword_form">
					<span class="kw col-xs-10">
						<input autocomplete="off" tabIndex="1" type="text" name="txtKeyword" id="txtKeyword" value="" onfocus="getSearchTip()" onkeyup="getSearchTip(event)" onkeydown="searchTipFocus(event)">
					</span>
					<span class="search_btn col-xs-2">
						<input tabIndex="2" type="button" name="searchButton" id="searchButton" value="搜 索" onclick="gotoResult()">
					</span>
					<!-- 输入框获取焦点弹出框 -->
					<div class="search_list" style="display: none; bottom: 0;" id="searchKeyword">
						<select id="search_tip_list" class="form-control" size="20" onkeydown="enterKeySetSearchTipValue(event)" onclick="setSearchTipValue()"></select>
					</div>
				</form>
			</div>
			<div class="s_jl col-xs-12">
				<span class="jl_title">历史表达式 <a class="more"></a></span>
				<div class="row" style="margin:0 20px;">
					<ul class="bds" id="keywordHistory">
					</ul>
				</div>
			</div>
		</div>
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
	//window.onload = getHistoryExpression();
	document.onclick = function(){
		//关闭搜索提示框,当聚焦的元素不为搜索提示框时，隐藏搜索提示框
		if (document.activeElement.id != "search_tip_list" && document.activeElement.id != "txtKeyword")
		{
			document.getElementById("searchKeyword").style.display = "none";
		}
	};
	
	//键盘按键触发获取搜索提示词
	function getSearchTip(e)
	{
		var e = e||window.event;
		if (e.keyCode)
		{
			if (e.keyCode != 13 && e.keyCode != 108)
			{
				getSearchTip();	
			}
		}
	}
	//获取搜索提示词
	function getSearchTip()
	{
		//焦点所在位置
		var start = document.getElementById("txtKeyword").selectionStart;
		var inputStr = $("#txtKeyword").val().substring(0,start);
		var lastInputStr = $("#txtKeyword").val().substring(start);
		var div_searchTip = document.getElementById("searchKeyword");
		div_searchTip.style.display = "none";
		$("#search_tip_list").html("");
		if(inputStr != ""){
			var expression;
			var field = "";
			var brackets = "";
			var type = 0;
			if (inputStr.substring(inputStr.length - 1) == "[")
			{
				expression = inputStr;
			}
			else
			{
				var leftNum = inputStr.split("[").length-1	;
				var rightNum = inputStr.split("]").length-1;	
				if(leftNum == rightNum ){
					var strArr = inputStr.split(new RegExp("([a|A][n|N][d|D]|[o|O][r|R]|[n|N][o|O][t|T]|>|<|=)",'gm'));
					expression = strArr[strArr.length - 1].trim();
				}else{
					expression =inputStr;
					field = inputStr.substring(inputStr.lastIndexOf("[")+1,inputStr.length);
					type = 1;
				}
			}
			//参数首位为小括号
			if(expression.lastIndexOf("(")==0){
				expression=expression.substring(1);
				brackets="(";
			}
			//提示词检索方式0.均提示，1，只提示医疗，2，只提示运营
			//注意同时修改results.jsp中的当前参数值和隐藏域中的值
			var searchType = 0;
			$.ajax({
				type : "post",
				url : "/hssp/body/getSearchTip",
				data : "expression=" + expression+"&type="+type+"&field="+field+"&searchType="+searchType+"&Identity="+$("#hideUserIdentity",parent.document).val(),
				dataType : "json",
				success : function(result)
				{
					var searchTipList = eval(result.searchTipList);
					if (searchTipList.length == 0)
					{
						div_searchTip.style.display = "none";
					}
					else
					{
						var htmls = "";
						if(searchTipList.length != 0 ){
							var expressionALL = $("#txtKeyword").val();
							var showTipsOk = false;
							var startNum = expressionALL.indexOf(field);
							var fieldSt = "";
							if(expressionALL.length >= startNum+field.length+1 ){
								fieldSt = expressionALL.substring(startNum-1,startNum+field.length+1);
							}else{
								fieldSt = expressionALL.substring(startNum-1,startNum+1)+field;
							}
							for (var i = 0,size = searchTipList.length; i < size; i++)
							{
								if(type == 1){
									if(searchTipList[i] != fieldSt){
										htmls = htmls + "<option value=\""  + searchTipList[i] + lastInputStr + "\">";
										htmls = htmls + searchTipList[i];
										htmls = htmls + "</option>";
										showTipsOk = true;
									}
								}else{
									if(searchTipList[i] != expression){
										htmls = htmls + "<option value=\"" +  searchTipList[i] + lastInputStr + "\">";
										htmls = htmls + searchTipList[i];
										htmls = htmls + "</option>";
										showTipsOk = true;
									}
								}
							}
							if(showTipsOk){
								$("#search_tip_list").html(htmls);
								div_searchTip.style.width = document.getElementById("txtKeyword_form").scrollWidth + 'px';
								div_searchTip.style.top = document.getElementById("txtKeyword").scrollHeight + document.getElementById("txtKeyword").offsetTop + 'px';
								div_searchTip.style.display = "block";
							}else{
								return;
							}
						}else{
							return;
						}
					}
				}
			});
		}else{
			div_searchTip.style.display = "none";
		}	
	}
	//当输入框聚焦情况下按向下方向键时，把焦点转移到下拉提示框
	function searchTipFocus(e)
	{
		var e = e||window.event;
		if (e.keyCode)
		{
			if (e.keyCode == 40)
			{
				document.getElementById("search_tip_list").options[0].selected = true;
				$("#search_tip_list").focus();
				document.getElementById("search_tip_list").options[0].selected = true;
			}
			if (e.keyCode == 13)
			{
				gotoResult()
			}
		}
	}
	//当焦点在下拉提示框时，按下Enter键把用户选择的值输入到输入框中
	function enterKeySetSearchTipValue(e)
	{
		var e = e||window.event;
		if (e.keyCode)
		{
			if (e.keyCode == 13 || e.keyCode == 108)
			{
					setSearchTipValue();
			}
		}
	}
	
	function formatValue(search_tip_list_val)
	{
		var startStnum = 0;
		var checkSt = search_tip_list_val;
		var checkSt_change="";
	    while(true){
	    	var start_brackets_num = checkSt.indexOf("[",startStnum);
	    	var end_brackets_num = checkSt.indexOf("]",startStnum);
	        if(start_brackets_num >= 0 && end_brackets_num>=0){
       			if( start_brackets_num < end_brackets_num ){
	        		startStnum = end_brackets_num+1;
	        	}else{
        			checkSt = checkSt.substring(0,startStnum)+checkSt.substring(end_brackets_num+1,checkSt.length);
	        	}
	        }else if(start_brackets_num < 0 && end_brackets_num >=0 ){
	        	checkSt = checkSt.substring(0,startStnum)+checkSt.substring(end_brackets_num+1,checkSt.length);
	        }else if(start_brackets_num < 0 && end_brackets_num < 0 ){
	        	break;
	        }else if(start_brackets_num >= 0 && end_brackets_num < 0 ){
	        	break;
	        }
	    }
		if(search_tip_list_val.indexOf("]]") > 0){
			var startNum = search_tip_list_val.indexOf("]]");
			var endNum = 0;
			var addNum =1;
			while(true){
				var judgeSt = search_tip_list_val.substring(startNum+addNum-1,startNum+addNum);
				if(judgeSt != "]"){
					endNum = startNum+addNum-1;
					break;
				}
				if(search_tip_list_val.length >= startNum+addNum+1){
					addNum++;
				}else{
					endNum = startNum+addNum-1;
					break;
				}
			}
			search_tip_list_val = search_tip_list_val.substring(0,startNum)+search_tip_list_val.substring(endNum,search_tip_list_val.length);
			
		}
		
		if(search_tip_list_val.indexOf("]]") > 0){
			var endNum = search_tip_list_val.indexOf("]]");
			var startNum = 0;
			var addNum =-1;
			while(true){
				var judgeSt = search_tip_list_val.substring(endNum+addNum,endNum+1);
				if(judgeSt != "]"){
					startNum = endNum+addNum+1;
					break;
				}
				if( startNum+addNum-1>=0){
					addNum--;
				}else{
					startNum = endNum+addNum+1;
					break;
				}
			}
			search_tip_list_val = search_tip_list_val.substring(0,startNum)+search_tip_list_val.substring(endNum,search_tip_list_val.length);
			
		}
		return search_tip_list_val;
	}
	
	
	//把用户选择的值赋给输入框中
	//判断输入“[”时是否有值
    var selectOk = true;
	function setSearchTipValue()
	{
		if(selectOk){
			//焦点所在位置
			var start = document.getElementById("txtKeyword").selectionStart;
			var inputStr = $("#txtKeyword").val().substring(0,start);
			var lastKHZ =inputStr.lastIndexOf("[");
			var lastKHY =inputStr.lastIndexOf("]");
			var expression = "";
			if (inputStr.substring(inputStr.length - 1) == "]" || inputStr.substring(inputStr.length - 1) == "[" || inputStr.substring(inputStr.length - 2) == "[*")
			{
				var search_tip_list_val=$("#search_tip_list").val();
				search_tip_list_val = formatValue(search_tip_list_val);
				if(search_tip_list_val.lastIndexOf("(")==0){
					search_tip_list_val=search_tip_list_val.substring(1);
				}
				//expression = expression + inputStr.substring(0, inputStr.lastIndexOf("[")) + $("#search_tip_list").val();
				expression =expression + inputStr.substring(0, inputStr.lastIndexOf("[")) + search_tip_list_val;
			}
			else if(inputStr.substring(inputStr.length - 1) == "." || lastKHZ > lastKHY)
			{
				var search_tip_list_val=$("#search_tip_list").val();
				search_tip_list_val = formatValue(search_tip_list_val);
				//expression = expression + inputStr.substring(0, inputStr.lastIndexOf("[")) + $("#search_tip_list").val();
				expression =expression + inputStr.substring(0, inputStr.lastIndexOf("[")) + search_tip_list_val;
			}
			else
			{
				var strArr = inputStr.split(new RegExp("([a|A][n|N][d|D]|[o|O][r|R]|[n|N][o|O][t|T]|>|<|=)",'gm'));
				if(strArr.length > 1){
					for (var i = 0,size = strArr.length; i < size - 1; i++)
					{
						expression = expression + strArr[i];
					}
					if(strArr[0].indexOf("(")>=0){
						expression = "("+expression;
					}
				}else{
					if(inputStr.indexOf("(")>=0){
						expression = "(";
					}
				}
				var tip =  $("#search_tip_list").val();
				tip = tip.replace(/\(运营.*?\)/,"");
				if(tip.indexOf("]]") > 0){
					var startNum = tip.indexOf("]]");
					var endNum = 0;
					var addNum =1;
					while(true){
						var judgeSt = tip.substring(startNum+addNum-1,startNum+addNum);
						if(judgeSt != "]"){
							endNum = startNum+addNum-1;
							break;
						}
						if(tip.length >= startNum+addNum+1){
							addNum++;
						}else{
							endNum = startNum+addNum-1;
							break;
						}
					}
					tip = tip.substring(0,startNum)+tip.substring(endNum,tip.length);
					
				}
				expression = expression + tip;
			}
			$("#txtKeyword").val(expression);
			document.getElementById("searchKeyword").style.display = "none";
			$("#txtKeyword").focus();
		}
	}
	//按Enter键相当于点击搜索按钮
	function enterKeyGotoResult(e)
	{
		var e = e||window.event;
		if (e.keyCode)
		{
			if (e.keyCode == 13)
			{
				gotoResult();
			}
		}
	}
	//跳转到搜索结果展示详情页面
	function gotoResult()
	{
		if ($("#txtKeyword").val().trim() == "")
		{
			parent.$.dialog("搜索条件不能为空!请重新输入!", false, 1500);
		}
		else
		{
			window.parent.gotoResult();
		}
	}
	//添加历史表达式
	function addHistoryExpression(expression)
	{
		if (expression.trim() != "")
		{
			$.ajax(
				{
					type : "post",
					url : "/hssp/body/addHistoryExpression",
					data : "expression=" + expression
				});
		}
	}
	//获取历史表达式
	function getHistoryExpression()
	
	{
		$("#prebody_Logo").html("");
		
		$.ajax(
		{
			type : "post",
			url : "/hssp/body/getHistoryExpression"+"?Identity="+$("#hideUserIdentity",parent.document).val(),
			dataType : "json",
			success : function(result) 
			{
				var prebody_Logo =result.prebody_Logo;
				$("#prebody_Logo").html(prebody_Logo);
				//$("h1",resultsDocument).html(containers);
				var expressions = eval(result.expression);
				var htmls = "";
				for (var i = 0,size = expressions.length; i < size; i++)
				{
					
					//htmls = htmls + "<li style=\"height:80px;\" class=\"col-lg-3 col-sm-6 col-xs-12\"><i class=\"l_close\">x</i><div style=\"overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + expressions[i] + "\">";
					var title_text = expressions[i].replace(/\"/g,"&quot");
					title_text = deleteTag(expressions[i]);
					htmls = htmls + "<li style=\"height:80px;\" class=\"col-lg-3 col-sm-6 col-xs-12\"><div style=\"border-radius: 10px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + title_text + "\">";
					htmls = htmls +"<span>"+ title_text;
					htmls = htmls + "</span></div></li>";
				}
				$("#keywordHistory").html(htmls);
				//bind_remove_historyExpression();
				bind_click_historyExpression();
			}
		});
	}
	function deleteTag(str)
	{
		return str ? str.replace(/[<">']/g, (a) => {
			             return {
			                 '<': '&lt;',
			                 '"': '&quot;',
			                '>': '&gt;',
			                "'": '&#39;'
			            }[a]
			        }) : '';
	}
	//绑定历史表达式删除事件
	function bind_remove_historyExpression()
	{
		var iList = document.getElementById("keywordHistory").getElementsByTagName("i");
		var divList = document.getElementById("keywordHistory").getElementsByTagName("div");	
		for (var i = 0,size = iList.length; i < size; i++)
		{
			iList[i].onclick = function(num)
			{
				return function()
				{
					$.ajax(
					{
						type : "post",
						url : "/hssp/body/removeHistoryExpression",
						data : "expression=" + divList[num].innerText,
						success : function()
						{
							getHistoryExpression();
						}
					});
				}
			}(i);
		}
	}
	//绑定历史表达式点击事件
	function bind_click_historyExpression()
	{
		var divList = document.getElementById("keywordHistory").getElementsByTagName("div");	
		for (var i = 0,size = divList.length; i < size; i++)
		{
			divList[i].onclick = function(num)
			{
				return function()
				{
					$("#txtKeyword").val(divList[num].innerText);
					gotoResult();
				}
			}(i);
		}
	}
</script>
</html>