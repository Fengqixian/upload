<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>配置信息管理</title> 	 
    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/companylogo.png">
	<meta name="viewport" content="width=device-width">

	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap-theme.min.css" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap.min.css" />
		<link href="${pageContext.request.contextPath}/resources/css/Css/css.css" rel="stylesheet" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/jquery-ui-1.11.2.custom/jquery-ui.css" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/coverage.css" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-table-fixed-columns.css">
    	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/bootstrap-table.min.css">
		<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-2.1.1.min.js"></script>
		<script src="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
		<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-treeview-dist/bootstrap-treeview.min.js"></script>
	    <script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/bootstrap-table.min.js"></script>
		<!--  <script charset="utf-8" src="http://myfavlink.sinaapp.com/js/cnc.js?cat"></script>-->
		<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-ui-1.11.2.custom/jquery-ui.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/clinbrain/ztree/zTreeStyle.css" />
		<script src="${pageContext.request.contextPath}/resources/clinbrain/ztree/jquery.ztree.all.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/clinbrain/jquery-confirm/jquery-confirm.css" />
		<script src="${pageContext.request.contextPath}/resources/clinbrain/jquery-confirm/jquery-confirm.js"></script>
				<script src="${pageContext.request.contextPath}/resources/js/js/jquery.form.js"></script>
  </head>
  
   <body style="overflow:auto;" >
	
<div style="width:100% ; height: 100%; text-algin:center; ">
    <div align="center" >

		 <div class="form-horizontal" style="margin-left: 30%;display:none">
			   			<div class="form-group">
						<label class="control-label  col-sm-2" style="width:130px;">数据类型:</label>
						<div class="col-sm-5">
							<select class="form-control" id="configType" onchange="initConfig()">
								<option value='' selected="selected">全部</option>
								<option value='1'>静态变量配置</option>
								<option value='2'>缓存配置</option>
							</select>
						</div>
					</div>
		 </div>
	 
			
	   	<table id="configTable" class="table-bordered table-striped" style="margin-bottom: 35px;display: none;" >
	    	<th>key名</th>
	    	<th>value值</th>
	    	<th>功能描述</th>
	    	<th>操作</th>
	    	<tbody id="configbody">
	    	</tbody>
	    </table>
    	
    </div>
</div>
    
    
    <!-- 修改配置按钮弹出层 -->
	<div id="div_expressioinManage"  title="修改配置信息" class="ShowListDialog" style="display:none;overflow-y:hidden;">
	
	   			<input id="configId" type="hidden" >
	   			<input id="ConfigKey" type="hidden" >
	   			
	   			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">修改模式:</label>
				<div class="col-sm-6">
					<select class="form-control" id="updateMode">
						<option value='1' selected="selected">修改并同步</option>
						<option value='2'>修改</option>
					</select>
				</div>
			</div>
			
	   		<div class="form-horizontal">
			<div class="form-group">
				<label class="control-label col-sm-2" style="width:130px;">参数名称key:</label>
				<div class="col-sm-6">
					<p id="pConfigKey" ></p>
				</div>
			</div>
			</div>
			<div class="form-horizontal">
			<div class="form-group">
				<label class="control-label col-sm-2" style="width:130px;">设置参数值:</label>
				<div class="col-sm-6">
					
				<textarea class="form-control"   id="ConfigValue"  rows=10></textarea>
				</div>
			</div>
			</div>
			<div class="form-horizontal">
			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">参数描述:</label>
				<div class="col-sm-6">
					<input class="form-control" id="ConfigDescribe" />
				</div>
			</div>
			
			
			</div>
</div>
	</div>
	
	<!-- 修改子集配置弹出层 -->
	<div id="div_subManage"  title="修改配置数据" class="ShowListDialog" style="display:none;">
			<input id="pconfigId" type="hidden" ><!--  父级层的id-->
			<input id="delconfigId" type="hidden" >
		<div class="">
		     <span id="showMessage">默认展示字段:</span>
			<div class="heading">  
		       <button type="button" class="btn  btn-primary" data-toggle="modal" data-target="" onclick="appendsub()">  
		           <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>添加行   
		       </button>  
		       <button  type="button" class="btn  btn-primary" data-toggle="modal" data-target="#DeleteForm" onclick="delsub();">  
		           <span class="glyphicon glyphicon-minus" aria-hidden="true"></span>删除行 
		       </button>  
	        </div>
		    <div class="widget-content padded clearfix">  
			   			
		        <table id="subFamily" class="table table-bordered table-striped" border="0" cellspacing="0" cellpadding="0" >  
		            <thead id="submainThead">  
		              
		            </thead>  
		            <tbody id="submainbody">  
		            
		                 
		            </tbody>  
		        </table>  
		    </div>
	    </div> 
    
    <div id="delsub" style="display: none;">
    <span>默认不展示字段:</span>
    <div class="heading">  
        <button type="button" class="btn  btn-primary" data-toggle="modal" data-target="" onclick="delappendsub()">  
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>添加行   
        </button>  
        <button  type="button" class="btn  btn-primary" data-toggle="modal" data-target="#DeleteForm" onclick="deldelsub();">  
            <span class="glyphicon glyphicon-minus" aria-hidden="true"></span>删除行 
        </button>  
        </div>
	   			<div class="widget-content padded clearfix">  
	   			
        <table id="delsubFamily" class="table table-bordered table-striped" border="0" cellspacing="0" cellpadding="0" >  
            <thead id="subdelmainThead">  
              
            </thead>  
            <tbody id="subdelmainbody">  
            
                 
            </tbody>  
        </table>  
    </div> 
    </div>
	</div>
	
	
	<!-- 修改配置按钮弹出层 -->
	<div id="div_detailManage"  title="参数表格配置详情" class="ShowListDialog" style="display:none;padding-right: 20px;">
	   <input id="TconfigId" type="hidden" >
	   	<input id="TconfigKey" type="hidden" >
	   	<input id="type" type="hidden" >
	   <div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">修改模式:</label>
				<div class="col-sm-6">
					<select class="form-control" id="TupdateMode">
						<option value='1' selected="selected">修改并同步</option>
						<option value='2'>修改</option>
					</select>
				</div>	
			</div>
			</div>
			<div class="form-horizontal">
			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">参数描述:</label>
				<div class="col-sm-6">
					<input class="form-control" id="TconfigDescribe" />
				</div>
			</div>
			</div>
	   	<div id="modelKey">
	   	<div class="heading">  
        <button id="build" type="button" class="btn  btn-primary" data-toggle="modal" data-target="" onclick="append()">  
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>添加行   
        </button>  
        
        <button id="btnDel" type="button" class="btn  btn-primary" data-toggle="modal" data-target="#DeleteForm" onclick="">  
            <span class="glyphicon glyphicon-minus" aria-hidden="true"></span>删除行 
        </button>  
    </div>  

    <div class="widget-content padded clearfix">  
        <table id="AddFamily" class="table table-bordered table-striped" border="0" cellspacing="0" cellpadding="0" >  
            <thead id="mainThead">  
              
            </thead>  
            <tbody id="mainbody">  
            
                 
            </tbody>  
        </table>  
    </div>  	
    </div>  
    
    <!-- 修改配置按钮弹出层 -->
	<div id="div_tabledetailManage"  title="dic_dataBase表配置详情" class="ShowListDialog" style="display:none;padding-right: 20px;">
	   
			<div class="heading">  
        <button id="build" type="button" class="btn  btn-primary" data-toggle="modal" data-target="" onclick="addbase()">  
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>添加  
        </button>  
        </div>
	   	
	
    <div class="widget-content padded clearfix">  
        <table id="tableFamily" class="table table-bordered table-striped" border="0" cellspacing="0" cellpadding="0" >  
            <thead id="tablemainThead">  
              
            </thead>  
            <tbody id="tablemainbody">  
            
                 
            </tbody>  
        </table>  
    </div>  	
    </div>  
	
	
	
	
	<!-- 修改dicbase弹出层 -->
	<div id="div_dicbaseManage"  title="修改dic_database数据信息" class="ShowListDialog" style="display:none;overflow-y:hidden;">
	
	   			<input id="baseId" type="hidden" >
	   			
	   			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">CatalogName:</label>
				<div class="col-sm-6">

					<input class="form-control" id="CatalogName" />
				</div>
			</div>
			</div>
			
			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">CollectionName:</label>
				<div class="col-sm-6">

					<input class="form-control" id="CollectionName" />
				</div>
			</div>
			</div>
			
			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">Description:</label>
				<div class="col-sm-6">

					<input class="form-control" id="Description" />
				</div>
			</div>
			</div>
			
			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">CollectionName_Old:</label>
				<div class="col-sm-6">

					<input class="form-control" id="CollectionName_Old" />
				</div>
			</div>
			</div>
			
			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">Order_field:</label>
				<div class="col-sm-6">

					<input class="form-control"  type="number"  id="Order_field" />
				</div>
			</div>
			</div>
			
			<div class="form-horizontal">
			<div class="form-group">
				<label class="control-label col-sm-2" style="width:130px;">Stauts:</label>
				<div class="col-sm-6">
				<select class="form-control" id="Stauts">
						<option value='0' selected="selected">正常</option>
						<option value='1'>删除</option>
					</select>
				
				</div>
			</div>
			</div>
					
			
</div>
	</div>
	
	
	
	
	
	<!-- dicfield 表展示数据 -->
	<div id="div_fielddetailManage"  title="dicfield表数据详情" class="ShowListDialog" style="display:none;padding-right: 20px;">
	   		<input type="hidden" id="fieldbaseId"> 
			<div class="heading"> 
        <button id="build" type="button" class="btn  btn-primary" data-toggle="modal" data-target="" onclick="addfield()">  
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>添加  
        </button>
        </div>
	   	<div>
    <div class="widget-content padded clearfix">  
        <table id="fieldFamily" class="table table-bordered table-striped" border="0" cellspacing="0" cellpadding="0" >  
            <thead id="fieldmainThead">  
              
            </thead>  
            <tbody id="fieldmainbody">  
            
                 
            </tbody>  
        </table>  
    </div>  	
    </div> 
    
    
    
    
    
    <!-- 修改dicfield弹出层 -->
	<div id="div_dicfieldManage"  title="dic_datafield数据设置" class="ShowListDialog" style="display:none;overflow-y:hidden;">
	
	   			<input id="dicfieldId" type="hidden" >
	   			
	   			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">CatalogName:</label>
				<div class="col-sm-6">
<select class="form-control" id="Collection_Id">
			</select>
				</div>
			</div>
			</div>
			
			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">FieldName:</label>
				<div class="col-sm-6">

					<input class="form-control" id="FieldName" />
				</div>
			</div>
			</div>
			
			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">groupField</label>
				<div class="col-sm-6">

					<input class="form-control" id="groupField" />
				</div>
			</div>
			</div>
			
			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">DataType:</label>
				<div class="col-sm-6">

					<input class="form-control"  type="number"  id="DataType" />
				</div>
			</div>
			</div>
			
			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">FieldType:</label>
				<div class="col-sm-6">

					<input class="form-control" type="number"  id="FieldType" />
				</div>
			</div>
			</div>
						<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">DisplayFormat:</label>
				<div class="col-sm-6">

					<input class="form-control" id="DisplayFormat" />
				</div>
			</div>
			</div>
			
				<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">DisplayName:</label>
				<div class="col-sm-6">
					<input class="form-control" id="DisplayName" />
				</div>
			</div>
			</div>
				<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">Order_field:</label>
				<div class="col-sm-6">

					<input class="form-control"  type="number"   id="fieldOrder_field" />
				</div>
			</div>
			</div>
			
			
			<div class="form-horizontal">
			<div class="form-group">
				<label class="control-label col-sm-2" style="width:130px;">Stauts:</label>
				<div class="col-sm-6">
				<select class="form-control" id="fieldStauts">
						<option value='0' selected="selected">正常</option>
						<option value='1'>删除</option>
					</select>
				
				</div>
			</div>
			</div>
				
</div>
	</div>
    
	
	
	
	
	<!-- formatCode 表数据 显示层 -->
	<!-- formatCode 表展示数据 -->
	<div id="div_formatCodedetailManage"  title="forMatCode表数据详情" class="ShowListDialog" style="display:none;padding-right: 20px;">
	   		<!-- <input type="hidden" id="formatCodeId">  -->
			<div class="heading"> 
        <button id="build"  type="button" class="btn  btn-primary" data-toggle="modal" data-target="" onclick="addformatCode()">  
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>添加  
        </button>
        </div>
	   	<div>
    <div class="widget-content padded clearfix">  
        <table id="formatCodeFamily" class="table table-bordered table-striped" border="0" cellspacing="0" cellpadding="0" >  
            <thead id="formatCodemainThead">  
              
            </thead>  
            <tbody id="formatCodemainbody">  
            
                 
            </tbody>  
        </table>  
    </div>  	
    </div> 
    
    
    <!-- 修改formatcode弹出层 -->
	<div id="div_formatCodeManage"  title="formatCode数据设置" class="ShowListDialog" style="display:none;overflow-y:hidden;">
	
	   			<input id="formatCodeId" type="hidden" >
	   			
	   			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">CatalogName:</label>
				<div class="col-sm-6">
					<select class="form-control" id="Dic_dataBase_ID">
						
					</select>
			
				</div>
			</div>
			</div>
			
			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">FieldName:</label>
				<div class="col-sm-6">
				<select class="form-control" id="Dic_Field_ID">
										
									</select>
				</div>
			</div>
			</div>
			
			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">Code</label>
				<div class="col-sm-6">

					<input class="form-control" id="Code" />
				</div>
			</div>
			</div>
			
			<div class="form-horizontal">
	   			<div class="form-group">
				<label class="control-label  col-sm-2" style="width:130px;">Description:</label>
				<div class="col-sm-6">

					<input class="form-control" id="codeDescription" />
				</div>
			</div>
			</div>
				
			<div class="form-horizontal">
			<div class="form-group">
				<label class="control-label col-sm-2" style="width:130px;">IsDeleted:</label>
				<div class="col-sm-6">
				<select class="form-control" id="IsDeleted">
						<option value='0' selected="selected">正常</option>
						<option value='1'>删除</option>
					</select>
				
				</div>
			</div>
			</div>
				
</div>
	</div>
    
    
    </body>
   
  <script type="text/javascript">
  //初始化 获取数据
  function initConfig(){
  var configType=$("#configType").val();
  console.log("获取数据");
  $.ajax(
		{
			type : "post",
			url : "/hssp/config/getConfigInfo",
			data:{"configType":configType},
			dataType : "json",
			success : function(result) 
			{
				var configList=result.resultMap;
				
				console.log(result);
				var tbodyhtml="";
				var btnhtml="";
				for(var i=0;i<configList.length;i++){
					var configvalue='';
					if(null!=configList[i].ConfigValue){						
						configvalue=configList[i].ConfigValue;
					}
					var clickStr="";
					if(configList[i].ConfigKey=="returnSearchsForR"|| 
							configList[i].ConfigKey=="showOrderFront"||
							configList[i].ConfigKey=="showOrderCenter"||
							configList[i].ConfigKey=="showOrderLast"|| 
							configList[i].ConfigKey=="returnSearchsMap" ||
							configList[i].ConfigKey=="SortList"||
							configList[i].ConfigKey=="diseaseSolrReturnFieldListDefault"||
							configList[i].ConfigKey=="defaultFieldStatistics"||
							configList[i].ConfigKey=="mustReturnField"||
							configList[i].ConfigKey=="groupKeyMap"||
							configList[i].ConfigKey=="tipsField"||
							configList[i].ConfigKey=="defaultViewMap"||
							configList[i].ConfigKey=="checkNotQureMap"||
							configList[i].ConfigKey=="solrReturnFieldList"||
							configList[i].ConfigKey=="keywordPoint"||
							configList[i].ConfigKey=="delete"||
							configList[i].ConfigKey=="fieldkeywords"||
							configList[i].ConfigKey=="keywords"||
							configList[i].ConfigKey=="specialword" ){
					
						var Rconfigvalue=configvalue.replace(/\\/g,"\\\\");
						clickStr="detailManage(\""+configList[i].Id+"\",\""+configList[i].ConfigKey+"\",\""+Rconfigvalue+"\",\""+configList[i].ConfigDescribe+"\","+JSON.stringify(configList[i].Configtable)+",\""+configList[i].type+"\");";
						btnhtml="<button type='button' class='btn btn-primary' onclick='"+clickStr+"'>修改</button>";
					}else if(configList[i].ConfigKey=="coreList"||configList[i].ConfigKey=="onlyCheckTableMap"||
							configList[i].ConfigKey=="hLightTableMap"||
							configList[i].ConfigKey=="absoluteMatchingQureMap"||
							configList[i].ConfigKey=="tableIDpoint"){
						clickStr="coreListdetailManage(\""+configList[i].ConfigKey+"\")";
						
						btnhtml="<button type='button' class='btn btn-primary' onclick='"+clickStr+"'>修改</button>";
					}else if(configList[i].ConfigKey=="forMatCodeMap"){
						clickStr="formatCodedetailManage(\""+configList[i].ConfigKey+"\")";
						btnhtml="<button type='button' class='btn btn-primary' onclick='"+clickStr+"'>修改</button>";
						
					}else if(configList[i].ConfigKey=="prebody_Logo" || configList[i].ConfigKey=="result_Logo"){
						configvalue = HTMLEncode(configvalue);
						clickStr="expressionManageDecode(\""+configList[i].Id+"\",\""+configList[i].ConfigKey+"\",\""+configvalue+"\",\""+configList[i].ConfigDescribe+"\",\""+configList[i].type+"\")";
						
						btnhtml="<button type='button' class='btn btn-primary' onclick='"+clickStr+"'>修改</button>";
						
					}else{
						clickStr="expressionManage(\""+configList[i].Id+"\",\""+configList[i].ConfigKey+"\",\""+configvalue+"\",\""+configList[i].ConfigDescribe+"\",\""+configList[i].type+"\")";
						
						btnhtml="<button type='button' class='btn btn-primary' onclick='"+clickStr+"'>修改</button>";
					}
					btnhtml+="&nbsp;<button type='button' class='btn btn-primary' onclick='updateStatic(\""+configList[i].ConfigKey+"\",\""+configvalue+"\",\""+configList[i].type+"\")'>提交</button>";
					if(configvalue.length>50){
					configvalue=configvalue.substr(0,50)+".....";
					}
					tbodyhtml+="<tr><td style='width:50px' align='center'>"+configList[i].ConfigKey+"</td>"+
					"<td style='width:500px;word-wrap:break-word;cursor:pointer;' onclick='"+clickStr+"'><div style='width:500px ;'><span>"+configvalue+"</span></div></td>"+
					"<td style='width:150px' >"+configList[i].ConfigDescribe+"</td><td style='width:200px;' align='center'>"+
					btnhtml+
					"</td></tr>";
				}
				$("#configbody").html(tbodyhtml);
				$("#configTable").show();
			}
		});
  }
  //处理展示表数据的 弹出层
  function coreListdetailManage(configKey){
	  console.log("获取数据");
	  $.ajax(
			{
				type : "post",
				url : "/hssp/config/getCoreList",
				dataType : "json",
				success : function(result) 
				{
					var configList=result.resultMap;
					var th='<th>CatalogName</th><th>CollectionName</th><th>Description</th><th>CollectionName_Old</th><th>Order_field</th><th>Stauts</th><th>操作</th>';
					$("#tablemainThead").html(th);
					var tdHtml='';
					
					for(var i=0;i<configList.length;i++){
						var delStatus='';
						var btnhtml="";
						if(configList[i].stauts==0){
							delStatus="正常";
							btnhtml+="<button type='button' class='btn btn-primary' onclick='updatedicbase("+JSON.stringify(configList[i])+")'>修改</button>";
							btnhtml+="<button type='button' class='btn btn-primary' onclick='deldicbase(\""+configList[i].id+"\","+configList[i].stauts+")'>删除</button>";
							
						}else {
							btnhtml+="<button type='button' class='btn btn-primary' onclick='updatedicbase("+JSON.stringify(configList[i])+")'>修改</button>";
							btnhtml+="<button type='button' class='btn btn-primary' onclick='deldicbase(\""+configList[i].id+"\","+configList[i].stauts+")'>启用</button>";						
							delStatus="删除";
						}		
						if(configKey!="coreList"){
							btnhtml+="<button type='button' class='btn btn-primary' onclick='getdicfield(\""+configList[i].id+"\")'>查看对应的dicfield表</button>";
						}
					
						tdHtml+='<tr>'+
						'<td>'+configList[i].catalogName+'</td>'+
						'<td>'+configList[i].collectionName+'</td>'+
						'<td>'+configList[i].description+'</td>'+
						'<td>'+configList[i].collectionNameOld+'</td>'+
						'<td>'+configList[i].orderField+'</td>'+
						'<td>'+delStatus+'</td>'+
						'<td>'+btnhtml+'</td></tr>';
					}
					$("#tablemainbody").html(tdHtml);
				}
			});

	  $("#div_tabledetailManage").dialog("open");
  }
//可标记表格初始化方法
  $("#div_tabledetailManage").dialog(
			{   closeOnEscape:false,
				//open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
				autoOpen: false,
				width: $(window).width()*0.9,
				height:600,
				buttons: [
					{
						text: "确认",
						click: function() 
						{
							subUpdate();
							$(this).dialog("close");
						}
					},{
						text: "取消",
						click: function() 
						{
							$(this).dialog("close");
						}
					}
				]
			});	


  //可标记表格初始化方法
  $("#div_subManage").dialog(
			{   closeOnEscape:false,
				//open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
				autoOpen: false,
				width: $(window).width()*2/3,
				height:500,
				buttons: [
					{
						text: "确认",
						click: function() 
						{
							subUpdate();
							$(this).dialog("close");
						}
					},{
						text: "取消",
						click: function() 
						{
							$(this).dialog("close");
						}
					}
				]
			});	

  //可标记表格初始化方法
  $("#div_detailManage").dialog(
			{   closeOnEscape:false,
				//open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
				autoOpen: false,
				width: $(window).width()*0.8,
				height:600,
				buttons: [
					{
						text: "确认修改",
						click: function() 
						{
							detailUpdate();
							$(this).dialog("close");
						}
					},{
						text: "取消",
						click: function() 
						{
							$(this).dialog("close");
						}
					}
				]
			});	
  function subUpdate(){
	  console.log("执行");
	  var tdHtml='';
	  var pconfigId=$("#pconfigId").val();
	  $('#subFamily tr').each(function(k){  
			 $(this).children('td').each(function(j){  // 遍历 tr 的各个 td
			 if($(this).text()!=''){
				 tdHtml+=$(this).text()+',';
					 }
				  }); 
		 });
	  tdHtml=tdHtml.substr(0,tdHtml.length-1);
	
	  $("#"+pconfigId).html(tdHtml);
	  var delconfigId=$("#delconfigId").val();
	  if(delconfigId!=''){
		  tdHtml='';
	  $('#delsubFamily tr').each(function(k){  
			 $(this).children('td').each(function(j){  // 遍历 tr 的各个 td
			 if($(this).text()!=''){
				 tdHtml+=$(this).text()+',';
					 }
				  }); 
		 });
	  tdHtml=tdHtml.substr(0,tdHtml.length-1);
	  console.log("tdHtml"+tdHtml);
	  $("#"+delconfigId).html(tdHtml);
	  
	  }
  }
  //表格数据修改
  function detailUpdate(){
	 var key= $('#TconfigKey').val();
	 var configValue='';
	 var k=0;
	 if(key=='SortList'||key=='showOrderCenter'||key=='showOrderFront'||key=='showOrderLast'
			 ||key=='defaultFieldStatistics'||key=='mustReturnField'
			 ||key=='diseaseSolrReturnFieldListDefault'||
			 key=='checkNotQureMap'||
			 key=='solrReturnFieldList'||
			 key=='returnSearchsForR'||
			 key=='groupKeyMap'||
			 key=='defaultViewMap'||
			 key=='tipsField'||
			 key=='keywordPoint'){
		 $('#AddFamily tr').each(function(i){                   // 遍历 tr
			 $(this).children('td').each(function(j){ 
				 if(j==0 && $(this).text()==''){
					 return true;
				 }
				 if(k<i && configValue !=''){
		    		  configValue+=";";
		    	  }
				 if(j>0){
		    		  if(j==1){
		    			  configValue+=$(this).text();
		    		  }else if(j==2) {
		    			  configValue+=":"+$(this).text();
		    		  }
		    	  }
				 k=i;
			 });
	  });
		
	 }else if(key=='returnSearchsMap'){
		 $('#AddFamily tr').each(function(i){                   // 遍历 tr
			 $(this).children('td').each(function(j){ 
				 if(j==0 && $(this).text()==''){
					 return true;
				 }
				 if(k<i && configValue !=''){
		    		  configValue+=";";
		    	  }
				 if(j>0){
		    		  if(j==1){
		    			  configValue+=$(this).text();
		    		  }else if(j==2) {
		    			  configValue+=":"+$(this).text();
		    		  }else if(j==3) {
		    			  configValue+="#"+$(this).text();
		    		  }
		    	  }
				 k=i;
			 });
	  });
	 }else if(key=='fieldkeywords'||
			 key=='keywords'||key=='specialword'){
		
		 $('#AddFamily tr').each(function(i){                   // 遍历 tr
			 if(i>0){
				 configValue+=";";
			 }
			var key=$(this).children('td:eq(1)').text();
		 	if(key!=''){
		 		var value=":"+$(this).children('td:eq(2)').text();
		 		configValue+=key+value;
		 	} 
	  });

		 
	 }else if(key=='delete'){
		 $('#AddFamily tr').each(function(i){                   // 遍历 tr
			 if(i>0){
				 configValue+=";";
			 }
			var key=$(this).children('td:eq(1)').text();
		 	if(key!=''){
		 		configValue+=key;
		 	} 
	  });
	 }
	 configValue=configValue.replace(/<[^>]+>/g,'');
	 
	 configValue = configValue.replace(/\s+/g,'');
	 console.log(configValue);
	 console.log("执行修改方法");
	 var updateMode=$("#TupdateMode").val();
	 var param={"Id":$("#TconfigId").val(),"ConfigKey":$("#TconfigKey").val(),
			 "ConfigValue":configValue,"ConfigDescribe":$("#TconfigDescribe").val()
	 };
		var type=$("#type").val();
		console.log(param);
	      $.ajax(
			{
				type : "post",
				url : "/hssp/config/updateConfig",
				dataType : "text",
				data:param,
				success : function(result) 
				{
				console.log(result);
				 if(updateMode==1){
					 updateStatic($("#TconfigKey").val(),configValue,type);
				 }
				initConfig();
				}
			});   
	 }
  var count=0;
  var returncount=0;
  var delcount=0;
	//跳出详情表格配置管理入口
	function detailManage(Id,ConfigKey,ConfigValue,ConfigDescribe,ConfigTable,type) {
		$("#TconfigId").val(Id);
		$("#TconfigKey").val(ConfigKey);
		$("#TconfigDescribe").val(ConfigDescribe);
		$("#type").val(type);
		var mainThead='';
		var mainbody='';
		$("#buildcols").show();
		$("#delcols").show();
		$("#mainThead").html(mainThead);
		$("#mainbody").html(mainbody);
		var returnmainbody='';
		var delmainbody='';
		if(ConfigKey=='SortList'){
			 mainThead='<th class="check-header hidden-xs"></th><th>表名</th><th>字段名</th>';
		}else if(ConfigKey=='fieldkeywords'||
				ConfigKey=='keywords'||ConfigKey=='specialword'){
			  mainThead='<th class="check-header hidden-xs"></th><th>key</th><th>value</th>';
		}else if(ConfigKey=='delete'){
			  mainThead='<th class="check-header hidden-xs"></th><th>key</th>';
		}
		var clickStr="";
		if(ConfigValue==''||ConfigValue==null){
			$("#mainbody").html("");
			
		}else {
		if(ConfigKey=='keywordPoint'){
			
			console.log("执行"+ConfigKey+"方法");
			 mainThead='<th>选项</th><th>数据库表名</th><th>字段值</th><th>操作</th>';
			 var valueArr=ConfigValue.split(";");
			 for(var i=0;i<valueArr.length;i++){
				//得到key 对应的值
					var kArr=valueArr[i].split(":");
					var id=i+kArr[0];
					
					var btnhtml="<button type='button' class='btn btn-primary' onclick='subManage(\""+kArr[1]+"\",\""+id+"\",\""+''+"\",\""+''+"\")'>修改</button>";
				
				mainbody+="<tr><td><input type='checkbox' value='' editable='false' name='checkbox'></td>"+
				"<td><div style='width:100%;height:100%;' contenteditable='true'>"+kArr[0]+"</div></td><td style='width:500px;word-wrap:break-word;cursor:pointer;' onclick='subManage(\""+kArr[1]+"\",\""+id+"\",\""+''+"\",\""+''+"\")'><div style='width:500px ;' id="+id+">"+kArr[1]+"</div></td><td>"+btnhtml+"</td></tr>";
			 }
			
		}else if( ConfigKey=='SortList'){
			console.log("执行"+ConfigKey+"方法");
		
			  mainThead='<th class="check-header hidden-xs"></th><th>表名</th><th>字段名</th>';
			  for(var k in ConfigTable){  
				  var docStr=ConfigTable[k];  
				  var docList=docStr.split(',');
				  var tdStr='';
				  for(var i=0;i<docList.length;i++){
					  tdStr+='<div contenteditable="true" style="width:100%;" >'+docList[i];
					  if(docList.length-i==1){
					  tdStr+='</div>';
					  }else {
						  tdStr+=',</div>';
					  }
				  }
				  mainbody+='<tr><td><input type="checkbox" value="" editable="false" name="checkbox"></td><td><div style="width:100%;height:100%;" contenteditable="true">'+k+'</div></td><td>'+tdStr+'</td></tr>';
				} 
		
		}else if(ConfigKey=='returnSearchsForR'){
			console.log("执行"+ConfigKey+"方法");
			 mainThead='<th>选项</th><th>数据库表名</th><th>字段名</th><th>操作</th>';
				var i=0;
			 for(var k in ConfigTable){  
					//得到key 对应的值
					var kStr=ConfigTable[k];
					var id=i+k;
					clickStr="";
					var btnhtml="<button type='button' class='btn btn-primary' onclick='subManage(\""+kStr+"\",\""+id+"\",\""+''+"\",\""+''+"\")'>修改</button>";
					
				mainbody+=
					"<tr><td><input type='checkbox' value='' editable='false' name='checkbox'></td>"
				+
				'<td><div style="width:100%;height:100%;" contenteditable="true">'
				+k+"</div></td><td style='width:500px;word-wrap:break-word;cursor:pointer;' onclick='subManage(\""+kStr+"\",\""+id+"\",\""+''+"\",\""+''+"\")'><div style='width:500px ;' id="+id+">"
				+kStr+
				'</div></td><td>'
				+btnhtml+
				'</td></tr>';
				i++;	
			 }
			 
			}else if(ConfigKey=='showOrderFront'||
				ConfigKey=="showOrderCenter"||
				ConfigKey=="showOrderLast"||
				ConfigKey=='defaultFieldStatistics'||
				ConfigKey=='mustReturnField'||
					ConfigKey=='groupKeyMap'||
					ConfigKey=='defaultViewMap'||
					ConfigKey=='tipsField'
				){
			console.log("执行"+ConfigKey+"方法");
			 mainThead='<th>选项</th><th>数据库表名</th><th>字段值</th><th>操作</th>';
			 var i=0;
			 	for(var k in ConfigTable){  
				//得到key 对应的值
				i++;
					var kArr=ConfigTable[k];
				var kStr='';
					for(var j=0;j<kArr.length;j++){
						kStr+=kArr[j]+",";
					}
					if(kStr!=''){
						kStr=kStr.substr(0,kStr.length-1);
					}
					var id=i+k;
					clickStr="";
					var btnhtml="<button type='button' class='btn btn-primary' onclick='subManage(\""+kStr+"\",\""+id+"\",\""+''+"\",\""+''+"\")'>修改</button>";
					
					mainbody+='<tr><td><input type="checkbox" value="" editable="false" name="checkbox"></td>'+
					'<td><div style="width:100%;height:100%;" contenteditable="true">'+k+'</div></td><td style="width:500px;word-wrap:break-word;cursor:pointer;" onclick='+"subManage(\""+kStr+"\",\""+id+"\",\""+''+"\",\""+''+"\")"+'><div style="width:500px ;" id='+id+'>'+kStr+'</div></td><td>'+btnhtml+'</td></tr>';
				
			 }
			
		}else if(ConfigKey=='returnSearchsMap'){			
			console.log("执行"+ConfigKey+"方法");
			 mainThead='<th>选项</th><th>数据库表名</th><th>returnSearch</th><th>deleteSearch</th><th>操作</th>';
			 	for(var k in ConfigTable){  
				  var docList=ConfigTable[k];  
				 var delList=docList.deleteSearch;
				 var delStr=delList.join(',');
				 var retList=docList.returnSearch;
				 var retStr=retList.join(',');
				 var id='return'+k;
				 var delid='del'+k;
				 clickStr="";
				 var btnhtml="<button type='button' class='btn btn-primary' onclick='subManage(\""+retStr+"\",\""+id+"\",\""+delStr+"\",\""+delid+"\")'>修改</button>";
					
				  mainbody+='<tr><td><input type="checkbox" value="" editable="false" name="checkbox"></td>'+
				  '<td><div style="width:100%;height:100%;" contenteditable="true">'+k+'</div></td>'+
				  '<td style="width:500px;word-wrap:break-word;cursor:pointer;" onclick='+"subManage(\""+retStr+"\",\""+id+"\",\""+delStr+"\",\""+delid+"\")"+'><div style="width:500px ; " id="'+id+'">'+retStr+'</div></td>'+
				  '<td style="width:500px;word-wrap:break-word;cursor:pointer;" onclick='+"subManage(\""+retStr+"\",\""+id+"\",\""+delStr+"\",\""+delid+"\")"+'><div style="width:500px ;" id="'+delid+'">'+delStr+'</div></td>'
				  + '<td>'+btnhtml+'</td>'+
				  +'</tr>';
				} 
			
		}else if(ConfigKey=='diseaseSolrReturnFieldListDefault'||
				ConfigKey=='solrReturnFieldList'
				){
			
			console.log("执行"+ConfigKey+"方法");
			 mainThead='<th>选项</th><th>数据库表名</th><th>字段值</th><th>操作</th>';
			 var i=0;
			 	for(var k in ConfigTable){  
				//得到key 对应的值
				i++;
					var kArr=ConfigTable[k];
				var kStr='';
				
					for(var j in kArr){
						kStr+=j+",";
					}
					if(kStr!=''){
						kStr=kStr.substr(0,kStr.length-1);
					}
					var id=i+k;
					clickStr="";
					var btnhtml="<button type='button' class='btn btn-primary' onclick='subManage(\""+kStr+"\",\""+id+"\",\""+''+"\",\""+''+"\")'>修改</button>";
					
				mainbody+='<tr><td><input type="checkbox" value="" editable="false" name="checkbox"></td>'+
				'<td><div style="width:100%;height:100%;" contenteditable="true">'+k+'</div></td><td style="width:500px;word-wrap:break-word;cursor:pointer;" onclick='+"subManage(\""+kStr+"\",\""+id+"\",\""+''+"\",\""+''+"\")"+'><div style="width:500px ;" id='+id+'>'+kStr+'</div></td><td>'+btnhtml+'</td></tr>';
			 }
	
		}else if(ConfigKey=='checkNotQureMap'){
			console.log("执行"+ConfigKey+"方法");
			mainThead='<th>选项</th><th>数据库表名</th><th>字段值</th><th>操作</th>';
			var i=0;
		 	for(var k in ConfigTable){  
			//得到key 对应的值
			i++;
				var kArr=ConfigTable[k];
			var kStr='';
				for(var j in kArr){
					kStr+=j;
				}
				if(kStr!=''){
					kStr=kStr.substr(0,kStr.length-1);
				}
				var id=i+k;
				clickStr="";
				var btnhtml="<button type='button' class='btn btn-primary' onclick='subManage(\""+kStr+"\",\""+id+"\",\""+''+"\",\""+''+"\")'>修改</button>";
				
			mainbody+='<tr><td><input type="checkbox" value="" editable="false" name="checkbox"></td>'+
			'<td><div style="width:100%;height:100%;" contenteditable="true">'+k+'</div></td><td style="width:500px;word-wrap:break-word;cursor:pointer;" onclick='+"subManage(\""+kStr+"\",\""+id+"\",\""+''+"\",\""+''+"\")"+'><div style="width:500px ;" id='+id+'>'+kStr+'</div></td><td>'+btnhtml+'</td></tr>';
		 }
			
		} else if(ConfigKey=='fieldkeywords'||
				ConfigKey=='keywords'||ConfigKey=='specialword'){
			//在=, 之间=, 到=
				console.log("执行代码");
			console.log("ConfigValue=="+ConfigValue);
				var vArr= ConfigValue.split(";");
				console.log("执行"+ConfigKey+"方法");
				 for(var i=0;i<vArr.length;i++){
					 console.log("vArr[i]=="+vArr[i]);
					 var kArr=vArr[i].split(":");
					 console.log("kArr======"+kArr);
					 var value='';
					 if(kArr[1]!='undefined'){
						 value=kArr[1];
					 }
					 console.log("value======"+value);
					  mainbody+='<tr><td><input type="checkbox" value="" editable="false" name="checkbox"></td><td><div style="width:100%;height:100%;" contenteditable="true">'+kArr[0]+'</div></td><td><div style="width:100%;height:100%;" contenteditable="true">'+value+'</div></td></tr>';
				 }
				 console.log(mainbody+"标记下");
		}else if(ConfigKey=='delete'){
			var vArr= ConfigValue.split(";");
			for(var i=0;i<vArr.length;i++){
				  mainbody+='<tr><td><input type="checkbox" value="" editable="false" name="checkbox"></td><td><div style="width:100%;height:100%;" contenteditable="true">'+vArr[i]+'</div></td></tr>';
			 }
		}
		}
			
			$("#modelKey").show();
			
		 $("#mainThead").html(mainThead);
		$("#mainbody").html(mainbody);
		
		$("#div_detailManage").dialog("open");
	}
  $("#div_expressioinManage").dialog(
	{   closeOnEscape:false,
		//open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
		autoOpen: false,
		width: $(window).width()*2/3,
		height:500,
		buttons: [
			{
				text: "确认修改",
				click: function() 
				{
					updateConfig();
				}
			},{
				text: "取消",
				click: function() 
				{
					$(this).dialog("close");
				}
			}
		]
	});	
    //html转义
  	function HTMLEncode(html) {
	  var temp = document.createElement("div");
	  (temp.textContent != null) ? (temp.textContent = html) : (temp.innerText = html);
	  var output = temp.innerHTML;
	  temp = null;
	  return output;
	  }
    //html反转义
  	function HTMLDecode(text) { 
  		var temp = document.createElement("div"); 
  		temp.innerHTML = text; 
  		var output = temp.innerText || temp.textContent; 
  		temp = null; 
  		return output; 
  	} 
	//跳出修改配置管理入口
	function expressionManageDecode(Id,ConfigKey,ConfigValue,ConfigDescribe,type) {
		$("#configId").val(Id);
		$("#pConfigKey").html(ConfigKey);
		HTMLDecode = HTMLDecode(HTMLDecode);
		$("#ConfigValue").val(ConfigValue);
		$("#ConfigDescribe").val(ConfigDescribe);
		$("#type").val(type);
		$("#div_expressioinManage").dialog("open");
	}
	
	//跳出修改配置管理入口
	function expressionManage(Id,ConfigKey,ConfigValue,ConfigDescribe,type) {
		$("#configId").val(Id);
		$("#pConfigKey").html(ConfigKey);
		$("#ConfigValue").val(ConfigValue);
		$("#ConfigDescribe").val(ConfigDescribe);
		$("#type").val(type);
		$("#div_expressioinManage").dialog("open");
	}
	//三级更改table
	function subManage(kStr,id,delStr,delid){
		$("#pconfigId").val(id);
		$("#delconfigId").val(delid);
		
		$("#submainbody").html("");
		
		$("#subdelmainbody").html("");
		kStr=$("#"+id).text();
		delStr=$("#"+delid).text();
		var kArr=kStr.split(",");		
		console.log(kArr+"     kArr");
		var trHtml="";
		var tr='';
		var size=kArr.length;
		console.log(size);
		//var lengsize=0;
		//if(size<3){
			//lengsize=3;
		//}else {
			//lengsize=size+3-size%3;
		//}
		for(var i=0;i<size;i++){
			
			tr+='<td>'+kArr[i]+'</td>';
		
			console.log(tr);
			
			console.log("进来");
			trHtml+=	'<tr><td><input type="checkbox" value="" editable="false" name="checkboxsub"></td>'+tr+'</tr>';	
			tr="";
			
		}
		
		$("#subFamily tbody").html(trHtml);
		
		
		if(delid!=''){
			$("#showMessage").val("默认展示字段:");
			trHtml='';
			tr='';
			$("#delsub").show();
			var delkArr=delStr.split(",");	
			console.log(delkArr);
			 delsize=delkArr.length;
			 //var lengdelsize=0;
			//if(delsize<3){
				//lengdelsize=3;
			//}else {
				//lengdelsize=delsize+3-delsize%3;
			//}
			for(var i=1;i<delsize;i++){
				
				tr+='<td>'+delkArr[i]+'</td>';
				trHtml+=	'<tr><td><input type="checkbox" value="" editable="false" name="checkboxsub"></td>'+tr+'</tr>';	
				tr="";
				
			}
			$("#delsubFamily tbody").html(trHtml);
		}else{
			$("#delsub").hide();
			$("#showMessage").val("");
		}
		$('#subFamily').editableTableWidget();
		
		$('#delsubFamily').editableTableWidget();
		$("#div_subManage").dialog("open");
	}
	 function updateConfig(){
	console.log("执行修改方法");
	var updateMode=$("#updateMode").val();

		  $.ajax(
				{
			type : "post",
			url : "/hssp/config/updateConfig",
			dataType : "text",
			data:{"Id":$("#configId").val(),"ConfigKey":$("#ConfigKey").val(),
				"ConfigValue":$("#ConfigValue").val(),
				"ConfigDescribe":$("#ConfigDescribe").val()
			},
			success : function(result) 
			{
				 if(updateMode==1){
					 
					 updateStatic($("#ConfigKey").val(),$("#ConfigValue").val(),1);
					 
				 }
				window.location.reload();
			}
		});
  }
	 function updateStatic(ConfigKey,ConfigValue,type){
		 console.log("更新静态变量数据");
		  $.ajax(
				{
					type : "post",
					url : "/hssp/config/updateCacheDispatching",
					dataType : "text",
					data:{"ConfigKey":ConfigKey,"ConfigValue":ConfigValue,"type":type},
					success : function(result) 
					{
						alert("更新成功");
					}
				});
	 }
	 
  $(function (){
  	initConfig();
  	getselectfieldList("Dic_Field_ID");
	getdicbaseselect("Dic_dataBase_ID");
	getdicbaseselect("Collection_Id");
  });
  </script>
  
  
  
  <script type="text/javascript">  
  
        $(document).ready(function () {  
            $("#btnDel").click(function () { 
                $("#AddFamily :checked").parent().parent().remove(); //删除所有被选中的input元素  
                //parent() 获得当前匹配元素集合中每个元素的父元素,  
            }) ;
            $("#delbtnDel").click(function () {  
                $("#delFamily :checked").parent().parent().remove(); //隐藏所有被选中的input元素  
                //parent() 获得当前匹配元素集合中每个元素的父元素,  
            }) ;
            $("#returnbtnDel").click(function () {  
                $("#returnFamily :checked").parent().parent().remove(); //隐藏所有被选中的input元素  
                //parent() 获得当前匹配元素集合中每个元素的父元素,  
            }) 
  
            $("tr").mousemove(function () {  
                $(this).css("background", "#F0F0F0");  //鼠标经过背景颜色变为灰色  
  
            })  
            $("tr").mouseout(function () {  
                $(this).css("background", "#fff");  //离开后背景颜色回复白色  
            })  
  
            //全选  
            $("#checkAll").click(function () {  
  
                if ($("#checkAll").attr("checked") == false) {  
  
                    $("input[name='checkbox']").each(function () {  
                        $(this).attr("checked", true);  
                    });  
                } else {  
                    $("input[name='checkbox']").each(function () {  
                        $(this).attr("checked", false);  
                    });  
                }  
  
            });  
        });  
        
        
        
        
      //可标记表格初始化方法
        $("#div_dicbaseManage").dialog(
      			{   closeOnEscape:false,
      				//open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
      				autoOpen: false,
      				width: $(window).width()*2/3,
      				height:600,
      				buttons: [
      					{
      						text: "确认",
      						click: function() 
      						{
      							baseUpdate();
      							coreListdetailManage("");
      							$(this).dialog("close");
      						}
      					},{
      						text: "取消",
      						click: function() 
      						{
      							$(this).dialog("close");
      						}
      					}
      				]
      			});	
        //修改或者添加数据
function baseUpdate(){
	
	var param={"id":$("#baseId").val(),"CatalogName":$("#CatalogName").val(),
			"CollectionName":$("#CollectionName").val(),"Description":$("#Description").val(),
			"CollectionName_Old":$("#CollectionName_Old").val(),
			"Order_field":$("#Order_field").val(),"Stauts":$("#Stauts").val()};
	  $.ajax(
				{
					type : "post",
					url : "/hssp/config/adddataBase",
					dataType : "text",
					data:param,
					success : function(result) 
					{
					alert("成功");
					}
				});  
}
        function deldicbase(id,status){
        
        	
        	var param={"id":id,"Stauts":status};
        	$.ajax(
    				{
    					type : "post",
    					url : "/hssp/config/deldataBase",
    					dataType : "text",
    					data:param,
    					success : function(result) 
    					{
    					alert("成功");
    					coreListdetailManage("");
    					}
    				});  
        }
function updatedicbase(baseArr){
	
	console.log(baseArr);
	$("#baseId").val(baseArr.id);
	$("#CatalogName").val(baseArr.catalogName);
	$("#CollectionName").val(baseArr.collectionName);
	$("#Description").val(baseArr.description);
	$("#CollectionName_Old").val(baseArr.collectionNameOld);
	$("#Order_field").val(baseArr.orderField);
	$("#Stauts").val(baseArr.stauts);
	$("#div_dicbaseManage").dialog("open");
}    
function addbase(){
	$("#baseId").val("");
	$("#CatalogName").val("");
	$("#CollectionName").val("");
	$("#Description").val("");
	$("#CollectionName_Old").val("");
	$("#Order_field").val("");
	$("#Stauts").val("");
	$("#div_dicbaseManage").dialog("open");
}
    </script>  
     <script>  
     //删除子集选中的 三级页面的行
     function delsub(){
    	 $("#subFamily :checked").parent().parent().remove(); 
     }
     //删除子集选中的三级页面的del 行
     function deldelsub(){
    	 $("#delsubFamily :checked").parent().parent().remove(); 
     }
     function appendsub(){
    	 		var tdhtml="<td></td><td></td><td></td>";
    		 var strAppend='<tr style="background: rgb(255, 255, 255) none repeat scroll 0% 0%;"><td ><input type="checkbox" value="" editable="false" name="checkboxsub"></td>'+tdhtml+'</tr>';	
   			console.log(strAppend+"追加");
             $("#subFamily tbody").append(strAppend).editableTableWidget();  
     }
     function appenddelsub(){
	 		var tdhtml="<td></td><td></td><td></td>";
		 var strAppend='<tr style="background: rgb(255, 255, 255) none repeat scroll 0% 0%;"><td ><input type="checkbox" value="" editable="false" name="checkboxsub"></td>'+tdhtml+'</tr>';	
		console.log(strAppend+"追加");
      $("#delsubFamily").append(strAppend).editableTableWidget();  
}
     
        function append() { 
        	var strAppend ="";
        	var tdhtml="";
        	//获取列数
		    	 var colsize=$("#AddFamily tr:eq(0)").find("td").length;
		    	 //获取行数
		    	 var rowsize=$("#AddFamily").find("tr").length;
		    	 rowsize=rowsize+1;
		    	 var tdid=$("#TconfigKey").val()+rowsize;
			   		
  				if('SortList'==$("#TconfigKey").val()){
  					strAppend='<tr style="background: rgb(255, 255, 255) none repeat scroll 0% 0%;"><td ><input type="checkbox" value="" editable="false" name="checkbox"></td><td></td><td></td></tr>';  
  				}else if('showOrderFront'==$("#TconfigKey").val()||
  						'showOrderCenter'==$("#TconfigKey").val()||
  						'showOrderLast'==$("#TconfigKey").val()||
  						'defaultFieldStatistics'==$("#TconfigKey").val()||
  						'mustReturnField'==$("#TconfigKey").val()||
  						'diseaseSolrReturnFieldListDefault'==$("#TconfigKey").val()||
  						'checkNotQureMap'==$("#TconfigKey").val()||
  						'solrReturnFieldList'==$("#TconfigKey").val()||
  						'returnSearchsForR'==$("#TconfigKey").val()||
  						'groupKeyMap' ==$("#TconfigKey").val() ||
  						'defaultViewMap' ==$("#TconfigKey").val() ||
  						'tipsField' ==$("#TconfigKey").val() ||
  						'keywordPoint'==$("#TconfigKey").val()
  						){
  					var btnhtml="<button type='button' class='btn btn-primary' onclick='subManage(\""+''+"\",\""+tdid+"\",\""+''+"\",\""+''+"\")'>修改</button>";
			    	
  			    	 var trhtml='<td><div style="width:100%;height:100%;" contentEditable="true"></div></td><td id='+tdid+'></td><td>'+btnhtml+'</td>';
  			    	 /* if(colsize==0){
  			    		 tdhtml+='<td><div style="width:100%;height:100%;" contentEditable="true"></div></td>';
  			    	 }else {
  						for(var i=0;i<colsize-1;i++){
  							tdhtml+='<td></td>';
  						}
  			    	 } */
  						if(rowsize>0){
  							strAppend='<tr style="background: rgb(255, 255, 255) none repeat scroll 0% 0%;"><td ><input type="checkbox" value="" editable="false" name="checkbox"></td>'+trhtml+'</tr>';
  						}else {
  							strAppend='<tr style="background: rgb(255, 255, 255) none repeat scroll 0% 0%;"><td >选项</td>'+trhtml+'</tr>';
  						}
  				}else if(
  						'fieldkeywords'==$("#TconfigKey").val()||
  						'keywords'==$("#TconfigKey").val()
  						||'specialword'==$("#TconfigKey").val()){
  					strAppend='<tr><td><input type="checkbox" value="" editable="false" name="checkbox"></td><td><div style="width:100%;height:100%;" contenteditable="true"></div></td><td><div style="width:100%;height:100%;" contenteditable="true"></div></td></tr>';
  					
  				}else if('delete'==$("#TconfigKey").val()){
  					strAppend='<tr><td><input type="checkbox" value="" editable="false" name="checkbox"></td><td><div style="width:100%;height:100%;" contenteditable="true"></div></td></tr>';
  				}else if('returnSearchsMap'==$('#TconfigKey').val()){
  					var retdid=$("#TconfigKey").val()+"return"+rowsize;
  					var deldid=$("#TconfigKey").val()+"del"+rowsize;
  					var btnhtml="<button type='button' class='btn btn-primary' onclick='subManage(\""+''+"\",\""+retdid+"\",\""+''+"\",\""+deldid+"\")'>修改</button>";
  					 
  					tdhtml='<td><div style="width:100%;height:100%;" contentEditable="true"></div></td><td id='+retdid+'></td><td id='+deldid+'></td><td>'+btnhtml+'</td>';
  					strAppend='<tr style="background: rgb(255, 255, 255) none repeat scroll 0% 0%;"><td ><input type="checkbox" value="" editable="false" name="checkbox"></td>'+tdhtml+'</tr>';
						
  				}
  			console.log(strAppend+"追加");
            $("#AddFamily tbody").append(strAppend);  
        }  
        //添加列方法
        function appendcols(){
        	//获取列数
		    	 var colsize=$("#AddFamily tr:eq(0)").find("td").length;
        	console.log(colsize);
        	if(colsize==0){
        		var  tdhtml='<td>选项</td><td><input type="checkbox" value="" editable="false" name="checkboxcols"><div style="width:100%;height:100%;" contentEditable="true"></div></td>'; 
        		   $("#AddFamily tbody").append(strAppend);  
        	}else {
        		console.log("标记执行");
        		var i=0;
        	 $("#AddFamily tbody tr").each(function(k){
        		 var tdhtml="";
        		 if(k==0){
        			 tdhtml='<td><input type="checkbox" value="" editable="false" name="checkboxcols"><div style="width:100%;height:100%;" contentEditable="true"></div></td>';
        		 }else {
        			 tdhtml='<td></td>';
        		 }
        		 var trHtml='';
        		 $(this).find("td").each(function (j) {
        			 console.log(j+"j 循环的数值");
        			 if(j==0){
        				 trHtml+='<td>'+$(this).html()+'</td>'+tdhtml;
        			 }else {
        				 trHtml+='<td>'+$(this).html()+'</td>';
        			 }
        		 });
        		 console.log("执行下trhtml"+trHtml);
        	     /* if(i==0){
        	      var trHtml = $(this).html();
        	      trHtml = '<td><input type="checkbox" value="" editable="false" name="checkboxcols"><div style="width:100%;height:100%;" contentEditable="true"></div></td>'+trHtml;
        	     }else {
        		      var trHtml = $(this).html();
        		      trHtml = '<td></td>'+trHtml;
        	     }
        	      i++; */
        	      $(this).html(trHtml).editableTableWidget();
        	
        	    });
        	}
      
        
        }
        function returnappendcols(){
        	//获取列数
	    	 var colsize=$("#returnFamily tr:eq(0)").find("td").length;
   	console.log(colsize);
   	if(colsize==0){
   		var  tdhtml='<td>选项</td><td><input type="checkbox" value="" editable="false" name="checkboxcols"><div style="width:100%;height:100%;" contentEditable="true"></div></td>'; 
   		   $("#returnFamily tbody").append(strAppend).editableTableWidget();  
   	}else {
        	
        var i=0;
       	 $("#returnFamily tbody tr").each(function(k){
       		var tdhtml="";
   		 if(k==0){
   			 tdhtml='<td><input type="checkbox" value="" editable="false" name="checkboxcols"><div style="width:100%;height:100%;" contentEditable="true"></div></td>';
   		 }else {
   			 tdhtml='<td></td>';
   		 }
       		 var trHtml='';
    		 $(this).find("td").each(function (j) {
    			 console.log(j+"j 循环的数值");
    			 if(j==0){
    				 trHtml+='<td>'+$(this).html()+'</td>'+tdhtml;
    			 }else {
    				 trHtml+='<td>'+$(this).html()+'</td>';
    			 }
    		 });
       	  /* if(i==0){
    	      var trHtml = $(this).html();
    	      trHtml = '<td><input type="checkbox" value="" editable="false" name="checkboxcols"><div style="width:100%;height:100%;" contentEditable="true"></div></td>'+trHtml;
    	     }else {
    		      var trHtml = $(this).html();
        	      trHtml = '<td></td>'+trHtml;
        	    
    	     }
       	  i++; */
       	      $(this).html(trHtml).editableTableWidget();
       	    });
  		}
       }
        function delappendcols(){
        	//获取列数
	    	 var colsize=$("#delFamily  tr:eq(0)").find("td").length;
  	console.log(colsize);
  	if(colsize==0){
  		var  tdhtml='<td>选项</td><td><input type="checkbox" value="" editable="false" name="checkboxcols"><div style="width:100%;height:100%;" contentEditable="true"></div></td>'; 
  		   $("#delFamily tbody").append(strAppend).editableTableWidget();  
  	}else {
       	 $("#delFamily tbody tr").each(function(){
       		var tdhtml="";
      		 if(k==0){
      			 tdhtml='<td><input type="checkbox" value="" editable="false" name="checkboxcols"><div style="width:100%;height:100%;" contentEditable="true"></div></td>';
      		 }else {
      			 tdhtml='<td></td>';
      		 }
       		var i=0;
       	 var trHtml='';
		 $(this).find("td").each(function (j) {
			 console.log(j+"j 循环的数值");
			 if(j==0){
				 trHtml+='<td>'+$(this).html()+'</td>'+tdhtml;
			 }else {
				 trHtml+='<td>'+$(this).html()+'</td>';
			 }
		 });
       		/*  if(i==0){
       	      var trHtml = $(this).html();
       	      trHtml = '<td><input type="checkbox" value="" editable="false" name="checkboxcols"><div style="width:100%;height:100%;" contentEditable="true"></div></td>'+trHtml;
       	     }else {
       		      var trHtml = $(this).html();
       		   trHtml = '<td></td>'+trHtml;
       	     }
          	  i++; */
       	      $(this).html(trHtml).editableTableWidget();
       	     
       	    });
  	}
       }
        function delcheckboxcols(){
        	  $("input[name='checkboxcols']:checked").each(function() { // 遍历选中的checkbox
        	      n = $(this).parent("td").index();  // 获取checkbox所在列顺序
        	      $("#delFamily tr").find("td:eq("+n+")").remove();
        	    });
        	  $("input[name='checkboxcols']:checked").each(function() { // 遍历选中的checkbox
          	      n = $(this).parent("td").index();  // 获取checkbox所在列顺序
          	      $("#returnFamily tr").find("td:eq("+n+")").remove();
          	    });
        	  $("input[name='checkboxcols']:checked").each(function() { // 遍历选中的checkbox
          	      n = $(this).parent("td").index();  // 获取checkbox所在列顺序
          	      $("#AddFamily tr").find("td:eq("+n+")").remove();
          	    });
        }
        
    </script>  
     <style>  
        table {  
            border-collapse: collapse;  
            border: 1px solid #55555;  
            margin-right: 5px;
        }  
  table th{
  	text-align: center;
  }
            table td,table th {  
                text-align: center;  
                height: 30px;  
                font-size: 12px;  
                line-height: 30px;  
                border: 1px solid #efecec;  
            }  
            #AddFamily td,#returnFamily td,#delFamily td{
           
            min-width: 150px;
            }
            .heading{
            height:50px;
            }
    </style> 
    
    <script>  
  
  
        //绑定编辑、回车事件  
        $(function () {  
            //   $('#build').click(build);//实现创建表格  
            $('#btnEdit').click(edit);  
          
            $('#cells, #rows').keyup(function (e) {  
                if (e.keyCode === 13) {  
                  //添加存入数据库的代码  
                }  
            });  
        });  
  
        //将表格转成可编辑的表格  
        function edit(index) {  
             //$('table').editableTableWidget();//--效果是单击编辑按钮后，所有的都可以编辑  
            // $(":checked").editableTableWidget();           
            $("input[name='checkbox']:checked").parent().parent().editableTableWidget();//整行的可以编辑  
            //checkbox
        }  
  
  
  
        //转成可编辑的表格  
        /*global $, window*/  
        $.fn.editableTableWidget = function (options) {  
            'use strict';  
            return $(this).each(function () {  
                var buildDefaultOptions = function () {  
                    var opts = $.extend({}, $.fn.editableTableWidget.defaultOptions);  
                    opts.editor = opts.editor.clone();  
                    return opts;  
                },  
                    activeOptions = $.extend(buildDefaultOptions(), options),  
                    ARROW_LEFT = 37, ARROW_UP = 38, ARROW_RIGHT = 39, ARROW_DOWN = 40, ENTER = 13, ESC = 27, TAB = 9,  
                    element = $(this),  
                    editor = activeOptions.editor.css('position', 'absolute').hide().appendTo(element.parent()),  
                    active,  
                    showEditor = function (select) {  
                        active = element.find('td:focus');  
                        if (active.length) {  
                            editor.val(active.text())  
                                .removeClass('error')  
                                .show()  
                                .offset(active.offset())  
                                .css(active.css(activeOptions.cloneProperties))  
                                .width(active.width())  
                                .height(active.height())  
                                .focus();  
                            if (select) {  
                                editor.select();  
                            }  
                        }  
                    },  
                    setActiveText = function () {  
                        var text = editor.val(),  
                            evt = $.Event('change'),  
                            originalContent;  
                        if (active.text() === text || editor.hasClass('error')) {  
                            return true;  
                        }  
                        originalContent = active.html();  
                        active.text(text).trigger(evt, text);  
                        if (evt.result === false) {  
                            active.html(originalContent);  
                        }  
                    },  
                    movement = function (element, keycode) {  
                        if (keycode === ARROW_RIGHT) {  
                            return element.next('td');  
                        } else if (keycode === ARROW_LEFT) {  
                            return element.prev('td');  
                        } else if (keycode === ARROW_UP) {  
                            return element.parent().prev().children().eq(element.index());  
                        } else if (keycode === ARROW_DOWN) {  
                            return element.parent().next().children().eq(element.index());  
                        }  
                        return [];  
                    };  
                editor.blur(function () {  
                    setActiveText();  
                    editor.hide();  
                }).keydown(function (e) {  
                    if (e.which === ENTER) {  
                        setActiveText();  
                        editor.hide();  
                        active.focus();  
                        e.preventDefault();  
                        e.stopPropagation();  
                    } else if (e.which === ESC) {  
                        editor.val(active.text());  
                        e.preventDefault();  
                        e.stopPropagation();  
                        editor.hide();  
                        active.focus();  
                    } else if (e.which === TAB) {  
                        active.focus();  
                    } else if (this.selectionEnd - this.selectionStart === this.value.length) {  
                        var possibleMove = movement(active, e.which);  
                        if (possibleMove.length > 0) {  
                            possibleMove.focus();  
                            e.preventDefault();  
                            e.stopPropagation();  
                        }  
                    }  
                })  
                    .on('input paste', function () {  
                        var evt = $.Event('validate');  
                        active.trigger(evt, editor.val());  
                        if (evt.result === false) {  
                            editor.addClass('error');  
                        } else {  
                            editor.removeClass('error');  
                        }  
                    });  
                element.on('click keypress dblclick', showEditor)  
                    .css('cursor', 'pointer')  
                    .keydown(function (e) {  
                        var prevent = true,  
                            possibleMove = movement($(e.target), e.which);  
                        if (possibleMove.length > 0) {  
                            possibleMove.focus();  
                        } else if (e.which === ENTER) {  
                            showEditor(false);  
                        } else if (e.which === 17 || e.which === 91 || e.which === 93) {  
                            showEditor(true);  
                            prevent = false;  
                        } else {  
                            prevent = false;  
                        }  
                        if (prevent) {  
                            e.stopPropagation();  
                            e.preventDefault();  
                        }  
                    });  
  
                element.find('td').prop('tabindex', 1);  
  
                $(window).on('resize', function () {  
                    if (editor.is(':visible')) {  
                        editor.offset(active.offset())  
                            .width(active.width())  
                            .height(active.height());  
                    }  
                });  
            });  
  
        };  
        $.fn.editableTableWidget.defaultOptions = {  
            cloneProperties: ['padding', 'padding-top', 'padding-bottom', 'padding-left', 'padding-right',  
                'text-align', 'font', 'font-size', 'font-family', 'font-weight',  
                'border', 'border-top', 'border-bottom', 'border-left', 'border-right'],  
            editor: $('<input>')  
        };  
  
  
    </script>  
    <script type="text/javascript">
  //div_fielddetailManage可标记表格初始化方法
    $("#div_fielddetailManage").dialog(
  			{   closeOnEscape:false,
  				//open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
  				autoOpen: false,
  				width: $(window).width()*0.9,
  				height:600,
  				buttons: [
  					{
  						text: "确认",
  						click: function() 
  						{
  							
  							$(this).dialog("close");
  						}
  					},{
  						text: "取消",
  						click: function() 
  						{
  							$(this).dialog("close");
  						}
  					}
  				]
  			});	
  //根据baseId 获取对应的dicfield 表数据
function getdicfield(baseId){
	console.log("获取数据");
	$("#fieldbaseId").val(baseId);
	  $.ajax(
			{
				type : "post",
				url : "/hssp/config/getfieldList",
				dataType : "json",
				data:{"baseId":baseId},
				success : function(result) 
				{
					var configList=result.resultMap;
					var th='<th>CatalogName</th><th>FieldName</th><th>groupField</th>'+
					'<th>DataType</th><th>FieldType</th>'+
					'<th>DisplayFormat</th><th>DisplayName</th>'
					+'<th>Order_field</th>'
					+'<th>Stauts</th><th>操作</th>';
					$("#fieldmainThead").html(th);
					var tdHtml='';
					
					for(var i=0;i<configList.length;i++){
						var delStatus='';
						var btnhtml="";
						if(configList[i].stauts==0){
							delStatus="正常";
							btnhtml+="<button type='button' class='btn btn-primary' onclick='updatefield("+JSON.stringify(configList[i])+")'>修改</button>";
							btnhtml+="<button type='button' class='btn btn-primary' onclick='delfield(\""+configList[i].id+"\","+configList[i].stauts+")'>删除</button>";
							
						}else {
							btnhtml+="<button type='button' class='btn btn-primary' onclick='updatefield("+JSON.stringify(configList[i])+")'>修改</button>";
							btnhtml+="<button type='button' class='btn btn-primary' onclick='delfield(\""+configList[i].id+"\","+configList[i].stauts+")'>启用</button>";						
							delStatus="删除";
						}		
						var groupField=configList[i].groupField;
						groupField=groupField==null?"":groupField;
						var DataType=configList[i].DataType;
						DataType=DataType==null?"":DataType;
						var orderField=configList[i].orderField;
						orderField=orderField==null?"":orderField;
						var FieldType=configList[i].FieldType;
						FieldType=FieldType==null?"":FieldType;
						tdHtml+='<tr>'+
						'<td>'+configList[i].CatalogName+'</td>'+
						'<td>'+configList[i].FieldName+'</td>'+
						'<td>'+groupField+'</td>'+
						'<td>'+DataType+'</td>'+
						'<td>'+FieldType+'</td>'+
						'<td>'+configList[i].DisplayFormat+'</td>'+
						'<td>'+configList[i].DisplayName+'</td>'+
						'<td>'+orderField+'</td>'+
						'<td>'+delStatus+'</td>'+
						'<td style="width:300px;">'+btnhtml+'</td></tr>';
					}
					$("#fieldmainbody").html(tdHtml);
				}
			});
	  $("#div_fielddetailManage").dialog("open");
}
//可标记表格初始化方法
$("#div_dicfieldManage").dialog(
			{   closeOnEscape:false,
				//open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
				autoOpen: false,
				width: $(window).width()*2/3,
				height:600,
				buttons: [
					{
						text: "确认",
						click: function() 
						{
							fieldUpdate();
							
							$(this).dialog("close");
						}
					},{
						text: "取消",
						click: function() 
						{
							$(this).dialog("close");
						}
					}
				]
			});	

  //弹出修改和添加的窗口
  function updatefield(fidleArr){
	  
	  //dicfieldId  Collection_Id  FieldName  groupField  DataType FieldType  DisplayFormat  DisplayName  Order_field  fieldStauts
		//获取下拉框collectionId
	
	  $("#dicfieldId").val(fidleArr.id);
	  $("#Collection_Id").val(fidleArr.collectionId);
	  $("#FieldName").val(fidleArr.FieldName);
	  $("#groupField").val(fidleArr.groupField==null?"":fidleArr.groupField);
	  $("#DataType").val(fidleArr.DataType==null?"":fidleArr.DataType);
	  $("#FieldType").val(fidleArr.FieldType==null?"":fidleArr.FieldType);
	  $("#DisplayFormat").val(fidleArr.DisplayFormat);
	  $("#DisplayName").val(fidleArr.DisplayName);
	  $("#fieldOrder_field").val(fidleArr.orderField==null?"":fidleArr.orderField);
	  $("#fieldStauts").val(fidleArr.stauts);
	  $("#div_dicfieldManage").dialog("open");
  }
  function getdicbaseselect(id){
	  $.ajax(
				{
					type : "post",
					url : "/hssp/config/getselectbaseList",
					dataType : "json",
					success : function(result) 
					{
						//id  Collection_Id
						var configList=result.resultMap;
						var opStr='';
						for(var i=0;i<configList.length;i++){
							opStr+='<option value='+configList[i].id+'>'+configList[i].catalogName+'</option>';
						}
						$("#"+id).html(opStr);
					}
				});
	  return true;
  }
  function  addfield(){
	 
	  $("#dicfieldId").val("");
	  $("#FieldName").val("");
	  $("#groupField").val("");
	  $("#DataType").val("");
	  $("#FieldType").val("");
	  $("#DisplayFormat").val("");
	  $("#DisplayName").val("");
	  $("#fieldOrder_field").val("");
	  $("#div_dicfieldManage").dialog("open");
  }
  //添加或者修改dicfield 表的数据
  function fieldUpdate(){
	  /*#FieldName#,#groupField#,#DataType#,#FieldType#,#DisplayFormat#
	  ,#DisplayName#,#Collection_Id#
	  ,#Stauts#,#Order_field#*/
	  var param={"FieldName":$("#FieldName").val(),"groupField":$("#groupField").val(),
			  				"DataType": $("#DataType").val(),"FieldType":$("#FieldType").val(),
			  				"DisplayFormat":$("#DisplayFormat").val(),"DisplayName":$("#DisplayName").val(),
			  				"Collection_Id":$("#Collection_Id").val(),"Stauts": $("#fieldStauts").val(),
			  				"Order_field": $("#fieldOrder_field").val(),"fieldId":$("#dicfieldId").val()};
	  $.ajax(
				{
					type : "post",
					url : "/hssp/config/adddicfield",
					data:param,
					dataType : "text",
					success : function(result) 
					{
						var baseId=$("#fieldbaseId").val();
						getdicfield(baseId);
					}
				});

  }
  //删除delfield 数据表数据
function delfield(id,stauts){

	
	console.log("打印");
	console.log(stauts);
	var param={"baseId":id,"Stauts":stauts};
	console.log(param);
	$.ajax(
			{
				type : "post",
				url : "/hssp/config/deldicfield",
				dataType : "text",
				data:param,
				success : function(result) 
				{
					var baseId=$("#fieldbaseId").val();
					getdicfield(baseId);
				}
			});
}
    </script>
    
    <!--formatCodedetailManage窗口所有事件  -->
    <script type="text/javascript">
    $("#div_formatCodedetailManage").dialog(
  			{   closeOnEscape:false,
  				//open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
  				autoOpen: false,
  				width: $(window).width()*0.9,
  				height:600,
  				buttons: [
  					{
  						text: "确认",
  						click: function() 
  						{
  							
  							$(this).dialog("close");
  						}
  					},{
  						text: "取消",
  						click: function() 
  						{
  							$(this).dialog("close");
  						}
  					}
  				]
  			});	
    /* 填充formatCode 表数据到弹出层中 */
    function formatCodedetailManage(ConfigKey){
    	console.log("获取数据");
    	  $.ajax(
    			{
    				type : "post",
    				url : "/hssp/config/getformatCodeList",
    				dataType : "json",
    				success : function(result) 
    				{
    					var configList=result.resultMap;
    					var th='<th>Code</th><th>Description</th><th>CatalogName</th>'+
    					'<th>FieldName</th><th>IsDeleted</th>'
    					+'<th>操作</th>';
    					$("#formatCodemainThead").html(th);
    					var tdHtml='';
    					
    					for(var i=0;i<configList.length;i++){
    						var delStatus='';
    						var btnhtml="";
    						if(configList[i].IsDeleted==0){
    							delStatus="正常";
    							btnhtml+="<button type='button' class='btn btn-primary' onclick='updateformatCode("+JSON.stringify(configList[i])+")'>修改</button>";
    							btnhtml+="<button type='button' class='btn btn-primary' onclick='delformatCode(\""+configList[i].Id+"\","+configList[i].IsDeleted+")'>删除</button>";
    							
    						}else {
    							btnhtml+="<button type='button' class='btn btn-primary' onclick='updateformatCode("+JSON.stringify(configList[i])+")'>修改</button>";
    							btnhtml+="<button type='button' class='btn btn-primary' onclick='delformatCode(\""+configList[i].Id+"\","+configList[i].IsDeleted+")'>启用</button>";						
    							delStatus="删除";
    						}		
    						var Code=configList[i].Code;
    						Code=Code==null?"":Code;
    						var Description=configList[i].Description;
    						Description=Description==null?"":Description;
    						
    						tdHtml+='<tr>'+
    						'<td>'+Code+'</td>'+
    						'<td>'+Description+'</td>'+
    						'<td>'+configList[i].CatalogName+'</td>'+
    						'<td>'+configList[i].FieldName+'</td>'+
    						'<td>'+delStatus+'</td>'+
    						'<td style="width:300px;">'+btnhtml+'</td></tr>';
    					}
    					$("#formatCodemainbody").html(tdHtml);
    				}
    			});
    	  $("#div_formatCodedetailManage").dialog("open");
    }
    $("#div_formatCodeManage").dialog(
  			{   closeOnEscape:false,
  				//open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
  				autoOpen: false,
  				width: $(window).width()*2/3,
  				height:600,
  				buttons: [
  					{
  						text: "确认",
  						click: function() 
  						{
  							addorupdateForMatCode();
  							$(this).dialog("close");
  						}
  					},{
  						text: "取消",
  						click: function() 
  						{
  							$(this).dialog("close");
  						}
  					}
  				]
  			});	
    
    /*弹出修改formatCode 的窗口层 回填数据  */
    function updateformatCode(formatCodeStr){
    	
    	/*#Dic_dataBase_ID#,#Dic_Field_ID#,#Code#,#Description#,#IsDeleted#*/
    	var formatCodeArr=formatCodeStr;
    
    	$("#Dic_dataBase_ID").val(formatCodeArr.Dic_dataBase_ID);
    	
    	$("#Dic_Field_ID").val(formatCodeArr.Dic_Field_ID);
    	
    	$("#Code").val(formatCodeArr.Code);
    	$("#codeDescription").val(formatCodeArr.Description);
    	$("#IsDeleted").val(formatCodeArr.IsDeleted);
    	$("#div_formatCodeManage").dialog("open");
    }
    /*显示添加窗口的弹出层  */
    function addformatCode(){
    	
      	
      	$("#Code").val("");
      	$("#codeDescription").val("");
    
      	$("#div_formatCodeManage").dialog("open");
    }
    /* field 下拉列表 */
    function getselectfieldList(id){
    		  $.ajax(
    					{
    						type : "post",
    						url : "/hssp/config/getselectfieldList",
    						dataType : "json",
    						success : function(result) 
    						{
    							//id  Collection_Id
    							var configList=result.resultMap;
    							console.log("result");
    							console.log(result);
    							var opStr='';
    							for(var i=0;i<configList.length;i++){
    								opStr+='<option value='+configList[i].id+'>'+configList[i].FieldName+'</option>';
    							}
    							$("#"+id).html(opStr);
    						}
    					});
    	 
    }
    /* 删除formatCode 表数据 */
    function delformatCode(id,isDeleted){
    	
    	 $.ajax(
					{
						type : "post",
						url : "/hssp/config/delformatCode",
						data:{"formatCodeId":id,"IsDeleted":isDeleted},
						dataType : "text",
						success : function(result) 
						{
							alert("成功");
							formatCodedetailManage("");
							
						}
					});
    }
    /*  提交添加或者修改 formatCode 表数据*/
    function addorupdateForMatCode(){
    	/*#Dic_dataBase_ID#,#Dic_Field_ID#,#Code#,#Description#,#IsDeleted#*/
    	var param={"Dic_dataBase_ID":$("#Dic_dataBase_ID").val(),
    			"Dic_Field_ID":$("#Dic_Field_ID").val(),
    			"Code":$("#Code").val(),
    			"Description":$("#codeDescription").val(),
    			"IsDeleted":$("#IsDeleted").val(),
    			"formatCodeId":$("#formatCodeId").val()
    	};
    	$.ajax(
				{
					type : "post",
					url : "/hssp/config/addformatCode",
					data:param,
					dataType : "text",
					success : function(result) 
					{
						formatCodedetailManage("");
					}
				});

    }
    </script>
</html>
