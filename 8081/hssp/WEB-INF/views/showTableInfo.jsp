<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>临床大数据搜索</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/zzsc-demo.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/basictable.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/themes/demo.css">

        <link rel="icon" href="${pageContext.request.contextPath}/resources/images/logo ico.png">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap-theme.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap.min.css" />
    
    <!--  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.3.5-dist/css/bootstrap.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.3.5-dist/css/bootstrap-theme.css" />-->
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/css.css"  />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/jquery-ui-1.11.2.custom/jquery-ui.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/coverage.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-table-fixed-columns.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/bootstrap-table.min.css">
      <script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-2.1.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-treeview-dist/bootstrap-treeview.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/bootstrap-table.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/js/jquery.validate.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/js/jquery.form.js"></script>
	 <script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-ui-1.11.2.custom/jquery-ui.js"></script>
	 <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/js/jquery.basictable.min.js"></script>
	 <script src="${pageContext.request.contextPath}/resources/js/js/navfix.js"></script>
	 <script src="${pageContext.request.contextPath}/resources/js/themes/jquery.easyui.min.js"></script>
	 


</head>

<body style= "overflow:auto;  overflow-x:auto">
			<div class="questionForm">
						<div class="c-tip-con dropdown-button" style="margin-left: 181px; margin-top: 33px;"     
						 id="return_search">
	
								<div id="div_solrTableAndField" title="检索返回字段勾选"
									style="text-align:left;">
									<ul id="solrTableAndFieldTree" class="ztree" style="overflow:auto;"></ul>
								</div>
								<div class="c-tip-menu c-tip-timerfilter">
									<ul style="line-height:30px">
										<li class=".c-tip-custom"  style="padding: 0 0px 0px 9px;position: relative;zoom: 1;">
										<a class="c-tip-custom-submit"
											onclick="return_search_submit()">确认</a> <a
											class="c-tip-custom-submit" onclick="return_search_cancel()">取消</a>
										</li>
										<li></li>
									</ul>
								</div>
	
					</div>
			</div>
		
		
	
       
  <script type="text/javascript">     
       
//添加
    $(document).ready(function () {        
          //调用函数，初始化表格
    	autoOpen: false,
			width: $(window).width()/1.3,
			height : "500"
      });
   	
   	
   	

	
   	
   	var solrDBFieldInfoOk =true;
	function getSolrDBFieldInfo()
	{
		var userid = document.getElementById("userid").value;
			$.ajax({
				type : "post",
				url : "/hssp/tail/getMenu"+"?Identity="+userid,
				dataType : "json",
				success : function(result)
				{
					
					var windows_height = $(window).height()*0.5;
					
					$("#solrTableAndFieldTree").css({
						height:windows_height,
						});
					treeNodes_solr = result.getSolrReturn;
					solrTableAndFieldDisplay();
					solrDBFieldInfoOk = false;
					$("#return_search").show();
				}
			});
	}
	//返回字段树结构
	var setting_solr = {
			data : {
				simpleData : {
					enable : true,
					idKey : "id",
					pIdKey : "pId",
					rootPId : 0
				},
				key : {
					name : 'name',
				}
			},
			check : {
				enable : true,
				chkStyle : "checkbox",
				chkboxType : {
					"Y" : "ps",
					"N" : "ps"
				}
			}
		};	
	var treeNodes_solr;
	
	function return_search_cancel(){
		  $("#return_search").hide();
		  $("#return_search_form_id").val("1");
	    }
	
	function solrTableAndFieldDisplay() {  
		zTree_solr = $.fn.zTree.init($("#solrTableAndFieldTree"),setting_solr, treeNodes_solr);  
	    $("#return_search").show();
	 }
	
	
	function return_search_submit(){
	    var tableAndField = "";
		var nodeList = zTree_solr.getNodes();
		var nodeArr = zTree_solr.transformToArray(nodeList);
	    var numCheck =0 ;
	    var tableName = "";
	    var filedName="";
	    var checkTableMap = {};
		for (var i = 0,size = nodeArr.length; i < size; i++)
		{
			if(numCheck==0){
				if (nodeArr[i].pId != 0 && nodeArr[i].checked){
					tableAndField =nodeArr[i].filedId;
					filedName = nodeArr[i].name;
					numCheck++;
				}
			}else{
				if (nodeArr[i].pId != 0 && nodeArr[i].checked){
					tableAndField = tableAndField + "," + nodeArr[i].filedId;
					filedName = filedName + "," + nodeArr[i].name;
					numCheck++;
				}
			}
			
			
		}
		//numCheck = numCheck -13;
		if(numCheck == 0 ){
			return_search_cancel();
			parent.$.dialog("未选择 展示字段!", false, 1500);
		}if(numCheck > 1 ){
			alert("选择最大为1个,请确认返回字段数目！");
		}
		
		var uid= document.getElementById("userid").value;
		var datas = {
				
				uid:uid,
				tableAndField:tableAndField,
				filedName:filedName
    	};
		
		
		
		
		 $.ajax({
 	        type:"POST",
 	        url:"/hssp/configTableController/addField",
 	        dataType: "json",
 	        data:datas,
 	        statusCode : {
				404 : function() {
					alert('page not found');
				}
			},
 	        success:function (msg) {
 	        	alert(msg);
 	        }

 	    });
		var hid_userId = $("#hid_userId").val();
		showPrivilegeFieldTable(hid_userId);
		$("#div_diseaseDBSave_local_div").dialog("close");

		
		
    }
	
	
	
	function deleteDicDb(id){
		 var datas = {
   				field:id
   		};
   		  $.ajax({
   		        type:"POST",
   		     	url:"/hssp/configTableController/deleteDicDb",
   		        contentType: "application/x-www-form-urlencoded; charset=utf-8", 
   		        dataType:"json",
   		        data:datas,
   		        success:function (msg) {
   		        	alert(msg);
   		        	
   		        }
   		    });
		
	}
	
	
       function updateDicDb() {
    	   
    	   
		
	}
       
</script>
	


</body>

</html>
