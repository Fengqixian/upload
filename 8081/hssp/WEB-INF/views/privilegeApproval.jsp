<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>临床大数据搜索</title>
<link rel="icon" href="${pageContext.request.contextPath}/resources/images/companylogo.png">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/basictable.css" />
        <link rel="icon" href="${pageContext.request.contextPath}/resources/images/logo ico.png">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap-theme.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap.min.css" />
    
    <!--  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.3.5-dist/css/bootstrap.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.3.5-dist/css/bootstrap-theme.css" />-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/css.css"  />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/Approval_Process.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/jquery-ui-1.11.2.custom/jquery-ui.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/coverage.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-table-fixed-columns.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/bootstrap-table.min.css">
      <script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-2.1.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-treeview-dist/bootstrap-treeview.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/bootstrap-table.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/locale/bootstrap-table-zh-CN.js"></script>
   
	<script src="${pageContext.request.contextPath}/resources/js/js/jquery.validate.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/js/jquery.form.js"></script>
	 <script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-ui-1.11.2.custom/jquery-ui.js"></script>
	 <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/js/jquery.basictable.min.js"></script>
	 <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/clinbrain/ztree/zTreeStyle.css" />
	<script src="${pageContext.request.contextPath}/resources/clinbrain/ztree/jquery.ztree.all.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
				<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-datetimepicker-master/js/locales/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>
	<style type="text/css">
		
		span{
				font-size: 14px;
			}
			/*头部-开始*/
			.header{
				background-color:#0e183f;
				height:50px;
				width: 100%;
				line-height: 50px;
				padding:0 10px;
				overflow: hidden;
			}
			.user{
				float: right; 
				color: #fff;
			}
			.logo{
				color: #fff;
				float:left;
			}
			/*头部-结束*/
			
			/*菜单样式-开始*/
			 ul {
		    	list-style: none;
			    font-size:14px;
			    padding:0;
			    
		    }
		    .menu-left{
			 	height: 100vh;
			    background-color: #fff;
			    width: 14%;
			    float: left;
			    box-shadow: 2px 2px 2px #ccc;
		    }
		    .menu-left > ul {
		    	margin-top: 0;
		    	padding-left: 0;
		    }
		    .menu-left> ul > li {
		        line-height: 50px;
		        width: 100%;
				padding-left: 10px;
		    }
			.menu-left ul li a{
				text-decoration: none;
				color: #666;
			}
			.s-firstDrop>li{
				padding-left: 10px;
			}
		    .iconRotate {
		        transform: rotate(180deg);
		        transition: transform .5s;
		    }   
		    .s-firstDrop,
		    .s-secondDrop {
		        display: none;
		    }          
		    .s-secondDrop {
		        padding-left: 20px;
		    }
			.active{
				background-color: #eee;
			}
			.active .d-firstNav a{
				color: #1890ff
			}
			.s-secondItem a{
				font-size:12px;
				color:#ccc;
				
			}
			/*菜单样式-结束*/
			.container-right{
				padding:10px;
				width: 86%;
				float: right;
				height:100vh;
				position: relative;
			}
			.content-center{
				overflow: hidden;
				
			}
			.footer{
				width:100%;
				height:31px;
				background-color:#262f52;
				color: #fff;
				line-height: 31px;
				text-align: center;
			}
			.table th,.table tr{
				font-size: 12px;
				padding: 0.45rem;
			}
			.table th{
				font-weight: normal;
				color: #333;
			}
			.table td{
				padding: 0.25rem;
				line-height:30px;
			}
			.table-bordered thead{
				background-color: #e5e5e5;
			}
			.input-right{
				float: right;
			}
			.select-left{
				float: left;
			}
			.query{
				padding:  10px 0;
				overflow: hidden;
				margin-top: 20px;
			}
			.btn{
				margin-left: 10px;
				margin-right: 10px;
				padding: 0.45rem 1.2rem;
				font-size:12px;
				border-radius: 1px;
				border: none;
				margin-bottom: 1px;
			}
			.form-control{
				display: inline-block;
				width: auto;
				padding: 0.36rem 0.75rem;
				font-size: 12px;
				background-color: #e5e5e5;
				border-radius: 1px;
				border:1px solid #e5e5e5;
			}
			select.form-control:not([size]):not([multiple]){
				height: auto;
			}
			.pagination{
				width: 100%;
				overflow: hidden;
				display: inherit;
			}
			.pagination_btn {
			float: left;
			margin-left: -10px;
			}
			.pagination_btn li{
				padding: 4px 10px;
				border: 1px solid #eee;
				margin-left: 10px;
				float: left;
			}
			.pagination_btn .active{
				background-color: #007bff;
				
			}
			.pagination_btn .active a{
				color: #fff;
			}
			.select-width{
				width:100%;
			}
		   .Pagesign li{
		   		width: 100px;
		    	height: 30px;
		    	padding:8px;
		   		position: relative;
		    	color: #fff;
				float: left;
			    font-size: 12px;
			    text-align: center;
		    }
			.Pagesign li::before{
				content:"";
				position:absolute;
				top: 0;
				left: 0;
				bottom: 0;
				right: 0;
				background-color:#545a75;
		        transform: perspective(.5em) rotateX(3deg);
		        transform-origin: bottom;
				z-index: -1;
				}
			.Pagesign{
			position:absolute;
		    top: 0;
			left: 0;
			background-color:#293152; 
		    width: 100%;
			z-index: -2;
			}
			.form-control:focus{
				background-color: #e5e5e5;
				border:1px solid #007bff;
				box-shadow:none;
			}
			.img{
				margin-bottom: 6px;
				margin-right: 10px;
			}
			.operation-btn{
				margin-left: 6px;
			}
			.Pagesign_Statistics{
				clear: left; float: right;
			}
			.btn:focus{
				box-shadow: none;
			}
			.btn-success:not(:disabled):not(.disabled):active:focus{
				box-shadow: none;
			}
			.btn-primary:not(:disabled):not(.disabled):active:focus{
				box-shadow: none;
			}		
		
	</style>
</head>
<body>
 <div class="content-center">
      <!-- 左侧菜单栏 -->
	  <div class="menu-left">
	    <ul>
			<li><a href="javascript:void(0);">全部菜单 <span style="float: right;margin-right:10px;"><img src="${pageContext.request.contextPath}/resources/images/Approval/menu.png"></span></a> </li>
			<li ><img  class="img" src="${pageContext.request.contextPath}/resources/images/Approval/home_bule.png" width="20"><a href="javascript:void(0);" onclick ="mainPage()">首页</a></li>
			 <li class="active">
                <div class="d-firstNav" id="search_Privilege_ALL">
                  <img  class="img" src="${pageContext.request.contextPath}/resources/images/Approval/PrivilegeSearch_bule.png" width="20">  <a  href="javascript:void(0);">检索权限 <span style="float: right; margin-right: 10px;"><img src="${pageContext.request.contextPath}/resources/images/Approval/zd.png"></span></a>
                </div>
                <ul class="d-firstDrop s-firstDrop">
                    <li class="s-secondItem">
                        <a id="search_Privilege_Self" href="javascript:void(0);" onclick="showPrivilegeSelf('0,1')">--  当前权限查看</a>
                    </li>
                    <li class="s-secondItem">
                        <a href="javascript:void(0);" onclick="showPrivilegeSelf()" >-- 权限记录</a>
                    </li>
                </ul>
            </li>
            <li ><img  class="img" src="${pageContext.request.contextPath}/resources/images/Approval/PrivilegeApply_blue.png" width="20"><a href="javascript:void(0);" onclick="showPrivilegeByApproval()">权限申请</a></li>
            <li class="active">
                <div class="d-firstNav">
                  <img  class="img" src="${pageContext.request.contextPath}/resources/images/Approval/PrivilegeApproval_blue.png" width="20">  <a href="javascript:void(0);">权限审批 <span style="float: right; margin-right: 10px;"><img src="${pageContext.request.contextPath}/resources/images/Approval/zd.png"></span></a>
                </div>
                <ul class="d-firstDrop s-firstDrop">
                    <li class="s-secondItem">
                        <a href="javascript:void(0);" onclick="showPrivilegeSelf('1,2,3,4')">-- 由我发起的</a>
                    </li>
                    <li class="s-secondItem">
                        <a href="javascript:void(0);" onclick="applyForPrivilege()">-- 待我审批的</a>
                    </li>
                    <li class="s-secondItem">
                        <a href="javascript:void(0);" onclick="applyForPrivilegeRecords()">-- 我审批过的</a>
                    </li>
                </ul>
            </li>
	    </ul>
 	  </div>
 	  <!-- 右侧展示 -->
 	  <div class="container-right" id="div_right_id">
 	     <!--  <ul class="Pagesign">
		    <li class="s-secondItem"><a>首页</a></li>
		    <li class="s-secondItem"><a>审批查看</a></li>
		    <li class="s-secondItem"><a>权限</a></li>
		  </ul> -->
		   <!-- <div class="query">
				<div class="select-left">
			      
			   </div>
			   <div class="input-right">
			   	
			   </div> 
		   </div> -->
		   <div class="table-responsive">
		  		<!-- 角色展示 -->
				<div id="priRole_div"  style="height:100%; width:99%;  margin-left: 0.5%; float:left;display:none;">
					<table id="priRoleTable"></table>
				</div>
				<div id="Approval_Process_Show"  style=" width:100%; overflow:auto; float:left;display:none;" >
					<ul class="list" id="Approval_Process_ul_id">
					</ul>
				</div>
				<!-- 查看申请详细信息 -->
		    <div class="modal fade bs-example-modal-lg" id="Approval_User_Role_Detail_Div_id" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		        <div class="modal-dialog modal-lg" role="document">
		            <div class="modal-content" style="height:80vh; width:900px;">
		                <div class="modal-header" style="text-align: center;">
		                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		                    <h4 class="modal-title" id="insertRole_Label">权限申请明细</h4>
		                </div>
		                <div class="modal-body">
							  <div class="form-group col-sm-12">
						         <label class="control-label col-sm-4">申请人账号：</label><div class="col-sm-8"><input class="form-control" type="text" name="Approval_User_UserID" id="Approval_User_UserID" disabled="disabled"/></div>
							     <label class="control-label col-sm-4">申请人名称：</label><div class="col-sm-8"><input class="form-control" type="text" name="Approval_User_UserName" id="Approval_User_UserName" disabled="disabled"/></div>
							     <label class="control-label col-sm-4">申请人科室：</label><div class="col-sm-8"><input class="form-control" type="text" name="Approval_User_deptName" id="Approval_User_deptName" disabled="disabled"/></div>
							  </div>
							  <div class="form-group col-sm-12">
								  <label class="control-label col-sm-4">角色名称：</label><div class="col-sm-8"><input class="form-control" type="text" name="Approval_Role_Detail_RoleName" id="Approval_Role_Detail_RoleName" disabled="disabled"/></div>
								  <label class="control-label col-sm-4">角色描述：</label><div class="col-sm-8"><input class="form-control" type="text" name="Approval_Role_Detail_Description" id="Approval_Role_Detail_Description" disabled="disabled"/></div>
								  <label class="control-label col-sm-4">权限类型：</label><div class="col-sm-8"><input class="form-control" type="text" name="Approval_User_Role_Privilege_Type" id="Approval_User_Role_Privilege_Type" disabled="disabled" /></div>
								  <label class="control-label col-sm-4">权限开始时间：</label><div class="col-sm-8"><input class="form-control" type="text" name="Approval_User_Role_valid_startTime" id="Approval_User_Role_valid_startTime" disabled="disabled"/></div>
								  <label class="control-label col-sm-4">权限结束时间：</label><div class="col-sm-8"><input class="form-control" type="text" name="Approval_User_Role_valid_EndTime" id="Approval_User_Role_valid_EndTime" disabled="disabled" /></div>
							   	  <label class="control-label col-sm-4">申请理由：</label>
							  	  <div class="col-sm-8">
							  	  		<textarea id="Approval_User_Role_Detail_Description" class="form-control" maxlength="450"  rows='6' cols='30' class="form-control"   style="resize:none;float: left;width: 100%;" disabled="disabled">
										</textarea>
								  </div>
							  </div>
			            </div>
			            <!-- <div class="modal-footer" style="margin-top: 100px;">
			                    <button type="button" class="btn btn-primary" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
			                    <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="saveRole()"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
			            </div> -->
			        </div>
			    </div>
		    </div>
		   </div>
		       
		   <div id="Approval_Process_Right" class="right" style="top: 30px;display: none;width: 100vh;">
			   <div class="right-list">
				   <div class="titel" id = "Approval_Process_Right_DIV_Title" style="font-size: 20px;height: 3%">当前审批状态</div>
			       <div class="modal-body" style="height: 10%">
			         <div class="form-group col-sm-12">
			            <label class="control-label col-sm-3">流程名称：</label>
			         	<div class="col-sm-8">
			           <input id="Approval_Process_Right_Title"   class="form-control" style="float: left;width: 100%;text-align: center;" disabled="disabled"/>
			           </div> 
			         </div>
			         <div class="form-group col-sm-12">
			            <label class="control-label col-sm-3">流程描述：</label>
			            <div class="col-sm-8"><textarea id="Approval_Process_Right_Description" class="form-control" disabled="disabled"  style="resize:none;text-align: center;float: left;width: 100%;"></textarea>
			            </div> 
			         </div>
				 	 <input id="Approval_Process_Right_hidden_Role_id" style="display:none"/>
			       	 <input id="Approval_Process_Right_hidden_Role_name" style="display:none"/>
			       	 <input id="Approval_Process_Right_hidden_ApprovalProcessGroupID" style="display:none"/>
			         <input id="Approval_Process_Right_hidden_id" style="display:none"/>
			       	 <input id="Approval_Process_Right_hidden_className" style="display:none"/>
			       	 <input id="Approval_Process_Right_hidden_user_role_id" style="display:none"/>
			       </div>
			       <div class="modal-footer" id="Approval_Process_Right_Show" style="margin-top: 100px;height: 0% ;display:none">
			               
			       </div>
			       <div id="Approval_Process_div_Add"  style=" height:75%; width:100%; overflow:auto; float:left;display:none;">
						<div id="Approval_Process_toolbar_add" class="btn-group">
							<button  type="button" class="btn btn-primary" onclick="refreshApprovalProcessUser()">
				                <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span><span aria-hidden="true">刷新</span>
				            </button>
				            <button type="button" class="btn btn-primary" data-dismiss="modal" onClick="processClose('hidden'),setTimeout('reduceWidth(50,2)', 10)"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
					    </div>
						<table  id="Approval_Process_Table_Add"></table>
			       </div>
				</div>
			</div>
 	  </div>
 </div>
 <div id="hiddenInfo" style="display: none">
	<form id="getForm">
		<input type="text" name="hid_priUserId" id="hid_priUserId" />
		<input type="text" name="hid_fieldId" id="hid_fieldId" />
		<input type="text" name="hid_valueId" id="hid_valueId" />
		<input type="text" name="hid_dicDataBaseId" id="hid_dicDataBaseId" />
		<input type="text" name="hid_role_id" id="role_id" />
		<input type="text" name="hid_roleName" id="hid_roleName" />
		<input type="text" name="hid_isDeleted" id="hid_isDeleted" />
	</form>
</div>
 <div class="footer" style="display:none">
	 <span>上海柯林布瑞信息技术有限公司</span>
</div>
<script type="text/javascript">
	$(function() {
	    $('.d-firstNav').click(function() {
	        dropSwift($(this), '.d-firstDrop');
	    });
	    $('.d-secondNav').click(function() {
	        dropSwift($(this), '.d-secondDrop');
	    });
	   
	    function dropSwift(dom, drop) {
	        dom.next().slideToggle();
	        dom.parent().siblings().find('.icon-chevron-up').removeClass('iconRotate');
	        dom.parent().siblings().find(drop).slideUp();
	        var iconChevron = dom.find('.icon-chevron-up');
	        if (iconChevron.hasClass('iconRotate')) {
	            iconChevron.removeClass('iconRotate');
	        } else {
	            iconChevron.addClass('iconRotate');
	        }
	    }
	})
	/*------------------------------------------- 审批流程结果展示 -------------------------------------------------------------------------------------------*/
	/**
	 * 展示审批流程
	 */
	function  editApproval_Process(role_id,method,role_name,user_role_id) {
		$("#Approval_Process_Show").css("height", "90vh");
		$("#Approval_Process_ul_id").html("");
		 $("#getForm").ajaxSubmit({
	   	     type: "post",
	   	     url: "/hssp/rest/showApprovalProcessGroup?role_id="+role_id+"&method="+method+"&user_role_id="+user_role_id,
	   	     dataType: "json",
	   	     success: function(data){
	   	    	 if(data.result == "succeed"){
	   	    		 var list = eval(data.list);
	   	    		 var htmStart = ""
	   	    		var htmlAll = "";
	   	    		 var Last_Approval_Status = -10;
	   	    		 var showNextOk = true;
	   	    		for(var k in list){
	   	    			var id = list[k]["Id"];
	   	   	    		var Process_Title = list[k]["Process_Title"];
	   	   	    		var Process_Description = list[k]["Process_Description"];
	   	   	    		var Approval_Order = list[k]["Approval_Order"];
	   	   	    		var Approval_Status = list[k]["Approval_Status"];
	   	    			var html = "";
	   	   	    		if(Last_Approval_Status == -10 || Last_Approval_Status == 0 ){
		   	   	    		if(Approval_Status == 0){
			   	   	    		html = html +"\">"+Process_Title+"(通过审批)"+"</div> <p id=\"Approval_Process_Add_Description_"+Approval_Order+"\">"+Process_Description+"</p>";
		   	   	    	    	Last_Approval_Status = 0;
		   	   	    		}else if(Approval_Status == 1){
			   	   	    		html = html +"\">"+Process_Title+"(已拒绝)"+"</div> <p id=\"Approval_Process_Add_Description_"+Approval_Order+"\">"+Process_Description+"</p>";
		   	   	    	    	Last_Approval_Status = -2;
		   	   	    		}else {
		   	   	    	    	Last_Approval_Status = -2;
			   	   	    		html = html +"\">"+Process_Title+"(等待审批)"+"</div> <p id=\"Approval_Process_Add_Description_"+Approval_Order+"\">"+Process_Description+"</p>";
		   	   	    		}
	   	   	    		}else{
	   	   	    			if(Last_Approval_Status == -1){
				   	   	       html = html +"\">"+Process_Title+"(等待审批)"+"</div> <p id=\"Approval_Process_Add_Description_"+Approval_Order+"\">"+Process_Description+"</p>";
				   	   	       Last_Approval_Status = Last_Approval_Status-1;
	   	   	    			}else{
				   	   	       html = html +"\">"+Process_Title+"</div> <p id=\"Approval_Process_Add_Description_"+Approval_Order+"\">"+Process_Description+"</p>";
				   	   	 	   Approval_Status = -2;
	   	   	    			}
	   	   	    		}
	   	    			var htmlFirst = "<li> <div class=\"list1\" id=\"Approval_Process_div_Add_"+Approval_Order+"\" onclick=\"showApprovalProcessUser("+Approval_Status+",'Approval_Process_div_Add_"+Approval_Order+"',"+id+",'"+Process_Title+"','"+Process_Description+"')\"> <div class=\"titel\" id=\"Approval_Process_div_Add_Title_"+Approval_Order+"";
	   	    			html = htmlFirst+html;
	   	   	    		html = html +"</div> <div class=\"line\">";
	   	   	    		html = html +"</div> </li>"
	   	   	    	    htmlAll = htmlAll + html;
	   	    		 }
	   	    		$("#Approval_Process_ul_id").html(htmStart+htmlAll);
	   	    		$("#Approval_Process_Right_hidden_user_role_id").val(user_role_id);
	   	    	 }else{
	   	    		alert("流程查询失败！");
	   	    	 }
	   	     }
		 });
		 $("#priRole_div").hide();
		 $("#Approval_Process_Show").show(500);
	}
	/**
	 * 刷新
	 */
	function refreshApprovalProcessUser(){
		$("#Approval_Process_Table_Add").hide();
		$("#Approval_Process_Table_Add").bootstrapTable('refresh');
		$("#Approval_Process_Table_Add").show(1000);
	}
	 var showApprovalProcessUserOK = true;
	 function  showApprovalProcessUser(Approval_Status,id,approvalProcessGroupID,title,description) {
	 	 //alert("需要展示id:"+id+" >> title:"+title+">> description:"+description);
	 	 processShow(id,"listChange",2);
	 	 $("#Approval_Process_Right_hidden_ApprovalProcessGroupID").val(approvalProcessGroupID);
	 	 $("#Approval_Process_Right_Title").val(title);
	 	 if(Approval_Status == 0){
	 		$("#Approval_Process_Right_DIV_Title").html("当前审批状态"+"(通过审批)");
	 	 }else if(Approval_Status == 1){
	 		$("#Approval_Process_Right_DIV_Title").html("当前审批状态"+"(已拒绝)");
	 	 }else if(Approval_Status == -1){
	 		$("#Approval_Process_Right_DIV_Title").html("当前审批状态"+"(等待审批)"); 
	 	 }else{
	 		$("#Approval_Process_Right_DIV_Title").html("当前审批状态"); 
	 	 }
	 	 $("#Approval_Process_Right_Description").text(description);
	 	 $("#priRole_div").hide();
	 	 $("#priUser_div_Adduser").hide();
	 	 $("#Approval_Process_Show").show(1000);
	 	 //$("#Approval_Process_Show").css("height", $(window).height());
	 	 initTable_ProcessUser($("#Approval_Process_Right_hidden_ApprovalProcessGroupID").val(), 3, "Approval_Process_Table_Add", "", "", "Approval_Process_toolbar_add");
	 	 $("#Approval_Process_div_Add").show(1000);
	 }
	 /**
	  *审批流程详细展示 
	  *
	  */
	 function processShow(id,className,type)
	     {
	 	 processClose('hidden',"");
	 	 if(showApprovalProcessUserOK){
	 		 setTimeout("reduceWidth(100,1)", 10);
	 	 }
	 	//判断是否有
	 	 if(nextAdd){
	 		 nextAdd = false;
	 		 setTimeout("",1000);
	 	 }
	 	 if(type == 1){
	 		 //显示追加流程的按钮
	 		 $("#Approval_Process_Right_DIV_Title").html("追加审批流程");
	 		// $("#Approval_Process_Right_Show").hide();
	 		 $("#Approval_Process_div_Add").hide();
	 		 $("#Approval_Process_Right_Add").show(500);
	 	 }else if(type == 2){
	 		//显示查看流程的按钮
	 		 $("#Approval_Process_Right_Add").hide();
	 		 $("#Approval_Process_div_Add").show(500);
	 		// $("#Approval_Process_Right_Show").show(500);
	 	 }
	 	 $("#Approval_Process_Right").show(1000);
	 	 nextAdd = true;
	 	 addClassCheck(id,className);
	 	 $("#Approval_Process_Show").animate({ scrollTop: $("#Approval_Process_Show").scrollTop() + $('#'+id).offset().top - $("#Approval_Process_Show").offset().top }, 1000);
	 	 $("#Approval_Process_Right_hidden_id").val(id);
	 	 $("#Approval_Process_Right_hidden_className").val(className);
	   }
	 function processClose(id,className){
			if(id == "hidden"){
			 id =  $("#Approval_Process_Right_hidden_id").val();
			 className =  $("#Approval_Process_Right_hidden_className").val();
			}
			$("#Approval_Process_div_Add").hide(1000);
			$("#Approval_Process_Right").hide(100);
			//$("#Approval_Process_Show").css("width","100%");
			//setTimeout("reduceWidth(50,2)", 10);
			$("#Approval_Process_Right_Description").text("");
			$("#Approval_Process_Right_Title").val("");
			 nextAdd = false;
			 removeClassCheck(id,className);
		}
	 function reduceWidth(widthValue,type){
			if(type == 1){
				widthValue = widthValue - 1;
				$("#Approval_Process_Show").css("width",widthValue+"%");
				if(widthValue > 50){
					setTimeout("reduceWidth("+widthValue+",1)", 10);
				}
				 showApprovalProcessUserOK = false;
			}else{
				widthValue = widthValue + 1;
				$("#Approval_Process_Show").css("width",widthValue+"%");
				if(widthValue < 100){
					setTimeout("reduceWidth("+widthValue+",2)", 10);
				}
				 showApprovalProcessUserOK = true;
			}
		}
	 function removeClassCheck(id,className){
		  $("#"+id).removeClass(className);
	  }
	 //定时任务
	  function removeOrAddClassCheck(id,className,ok){
		   if(nextAdd){
			   if(ok){
				     ok = false;
					 $("#"+id).removeClass(className);
					 //setTimeout("removeOrAddClassCheck('"+id+"','"+className+"',"+ok+")", 500);
				 }else{
					 ok = true;
					 var className_old = $("#"+id).attr("class");
					  $("#"+id).removeClass().addClass(className);
					  $("#"+id).addClass(className_old);
					 //setTimeout("removeOrAddClassCheck('"+id+"','"+className+"',"+ok+")", 500);
				 }
		   }else{
			   $("#"+id).removeClass(className);
			   return;
		   }
		   
	  }
	  var nextAdd = false;
	  function addClassCheck(id,className){
		  var className_old = $("#"+id).attr("class");
		  $("#"+id).removeClass().addClass(className);
		  $("#"+id).addClass(className_old);
	      removeOrAddClassCheck(id,className,false);
	  }
	//用户信息准备页面
	  function initTable_ProcessUser(processGroupId,method,tableId,userid,username,toolbarid) {
	          //先销毁表格
	          var columnNames = [];
	          $("#"+tableId).bootstrapTable('destroy');
	     			  columnNames.push({
	          	         title: "序号",
	          	         field: "rowNum",
	          	         valign:'middle',
	          	         align:'center',
	          	         formatter:function(value,row,index){ 
	    	 	              return index+1;  
	    	 	             } 
	          	     });
	     			  /*columnNames.push({
	          	         title: "编码",
	          	         field: "Id",
	          	         valign:'middle',
	          	         align:'center'
	          	     });*/
	     		  columnNames.push({
	            	         title: "用户ID",
	            	         field: "USERID",
	            	         valign:'middle',
	            	         align:'center'
	            	     });
	     		  columnNames.push({
	            	         title: "用户名",
	            	         field: "USERNAME",
	            	         valign:'middle',
	            	         align:'center'
	            	     });
	          	   var firstOK = true;
	          	 columnNames.push({
	 				 title: '审批结果',
	 	            field: 'Approval_Status',
	 	            align: 'center',
	 	            formatter:function(value,row,index){ 
	 	            	var e = "未操作";
	      	        	  if(row.Approval_Status == 0){
	      	        		e = "同意";
	      	        		//$("#Approval_Process_Right_DIV_Title").html($("#Approval_Process_Right_DIV_Title").html()+"(通过审批)");
	      	        	  }else if(row.Approval_Status == 1){
	      	        		e = "拒绝"; 
	      	        		//$("#Approval_Process_Right_DIV_Title").html($("#Approval_Process_Right_DIV_Title").html()+"(已拒绝)");
	      	        	  }
	 	              return e;  
	 	          } 
	 	 	     });
	          	var user_role_id = $("#Approval_Process_Right_hidden_user_role_id").val();
	         	 //初始化表格,动态从服务器加载数据
	            $("#"+tableId).bootstrapTable({
	                url: "/hssp/rest/showApprovalProcessUser", //获取数据的Servlet地址
	             // method: "POST",  //使用get请求到服务器获取数据
	             // contentType: "application/x-www-form-urlencoded",//post请求的话就加上这个句话
	                toolbar: "#"+toolbarid,
	                striped: true,                      //是否显示行间隔色
	                cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	                pagination: true,                   //是否显示分页（*）
	                sortable: false,                     //是否启用排序
	                sortOrder: "asc",                   //排序方式
	                sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
	                pageNumber:1,                       //初始化加载第一页，默认第一页
	                pageSize: 5,                       //每页的记录行数（*）
	                pageList: [5],        			//可供选择的每页的行数（*）
	                search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
	                strictSearch: false,
	                showColumns: false,                  //是否显示所有的列
	                showRefresh: false,                  //是否显示刷新按钮
	                minimumCountColumns: 2,             //最少允许的列数
	                clickToSelect: true,                //是否启用点击选中行
	                height: "",                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
	                uniqueId: "id",                     //每一行的唯一标识，一般为主键列
	                showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
	                cardView: false,                    //是否显示详细视图
	                detailView: false,                   //是否显示父子表
	                queryParams: function queryParams(params) {   //设置查询参数
	                  var param = {  
	                      pageNumber: params.offset,  
	                      pageSize: params.limit,
	                      method : method,
	                      flagDicDb:0,
	                      processGroupId:processGroupId,
	                      userId:userid,
	                      userName:username,
	                      user_role_id:user_role_id
	                  };  
	                  return param;                 
	                },
	               
	                formatLoadingMessage: function () {
	                    return "请稍等，正在加载中...";
	                },
	                formatNoMatches: function () {  //没有匹配的结果
	                    return '无符合条件的记录';
	                }, 
	                columns : columnNames,
	             	  responseHandler: function(data){
	             		//alert(JSON.stringify(data));
	             	 	return data;
	             	 },
		             	onLoadError: function(e){
			    	   	      parent.location.reload();    	    
			             	}
	              });
	         	 
	        }
	/*------------------------------------------- 审批流程结果展示 -------------------------------------------------------------------------------------------*/
	
	function mainPage(){
		$("#search_Privilege_ALL").click();
		 $("#search_Privilege_Self").click();
	}
	function applyfor(role_id){
		window.parent.applyforShow(role_id);
	}
    //隐藏所有显示
	function hideAll(){
		$("#priRole_div").hide();
		$("#show_privilege_Div").hide();
		$("#privilegeApproval_div").hide();
		//$("#Approval_User_Role_Detail_Div_id").hide();
		processClose('hidden');
		setTimeout('reduceWidth(50,2)', 10);
		$("#Approval_Process_Show").hide();
		/* $("#showPrivilegeByApproval").hide();
		$("#showPrivilegeSelf").hide(); */
	}
    //查看当前自己权限（角色展示）
	function showPrivilegeSelf(privilege_Status){
		hideAll();
		$("#priRole_div").show(1000);
		$("#Approval_Role_Add").show(1000);
		initTable_Role(0, "","","",2,privilege_Status);
	}
	//申请权限（角色展示）
	function showPrivilegeByApproval(){
		hideAll();
		$("#priRole_div").show(1000);
		initTable_Role(0, "","","",3);
	}
	//审批权限（角色展示）
	function applyForPrivilege(){
		hideAll();
		window.parent.resetCountForApproval();
		$("#priRole_div").show(1000);
		initTable_Role(0, "","","",4,"1,2,3,4");
	}
	//审批权限（角色展示）
	function applyForPrivilegeRecords(){
		hideAll();
		$("#priRole_div").show(1000);
		initTable_Role(0, "","","",5,"1,2,3,4");
	}
	 //角色信息维护页面     
	function initTable_Role(isdeleted,Id,roleName,roleDescription,method,privilege_Status) {
		    var privilegeSelf ={};
		    //表格分页方式，客户端默认
		   var sidePagination= "client";
			if(isdeleted==0){
	       		$("#addRole").show();
	       		$("#hsz").show();
	       		$("#yxlb").hide();
	       	}else{
	       		$("#yxlb").show();
	       		$("#addRole").hide();
	       		$("#hsz").hide();
	       	}
	        //先销毁表格
	        var columnNames = [{
    	         title: "序号",
    	         field: "rowNum",
    	         valign:'middle',
    	         align:'center',
    	         formatter:function(value,row,index){ 
	 	              return index+1;  
	 	         } 
	    	  },
		 		 {
         	         title: "角色名称",
         	         field: "RoleName",
         	         valign:'middle',
         	         align:'center'
         	     },
         	     {
       	         title: "角色描述",
       	         field: "Description",
       	         valign:'middle',
       	         align:'center'
       	     }
  			];
	        if( method ==  2){
	        	columnNames.push({
	      	         title: "权限状态",
	      	         field: "Privilege_Status",
	      	         valign:'middle',
	      	         align:'center',
	      	         formatter:function(value,row,index){
	      	        	 var e = "";
	      	        	  if(row.Privilege_Status == 0){
	      	        		 e="系统创建";
	      	        	 }else if(row.Privilege_Status == 1){
	      	        		 e = "<a href=\"javascript:void(0);\" onclick=\"editApproval_Process("+row.Id+",2,'"+row.RoleName+"',"+row.Privilege_User_Role_id+")\">";
	      	        		 e= e+"通过审批";
	      	        		 e =e+"</a>";
	      	        	 }else if(row.Privilege_Status == 2){
	      	        		 e = "<a href=\"javascript:void(0);\" onclick=\"editApproval_Process("+row.Id+",2,'"+row.RoleName+"',"+row.Privilege_User_Role_id+")\">";
	      	        		 e= e+"等待审批";
	      	        		 e =e+"</a>";
	      	        	 } else if(row.Privilege_Status == 3){
	      	        		 e = "<a href=\"javascript:void(0);\" onclick=\"editApproval_Process("+row.Id+",2,'"+row.RoleName+"',"+row.Privilege_User_Role_id+")\">";
	      	        		 e= e+"已撤回";
	      	        		 e =e+"</a>";
	      	        	 } else if(row.Privilege_Status == 4){
	      	        		 e = "<a href=\"javascript:void(0);\" onclick=\"editApproval_Process("+row.Id+",2,'"+row.RoleName+"',"+row.Privilege_User_Role_id+")\">";
	      	        		 e =e+"已拒绝";
	      	        		 e =e+"</a>";
	      	        	 } else if(row.Privilege_Status == 5){
	      	        		 e = "<a href=\"javascript:void(0);\" onclick=\"editApproval_Process("+row.Id+",2,'"+row.RoleName+"',"+row.Privilege_User_Role_id+")\">";
	      	        		 e =e+"已失效";
	      	        		 e =e+"</a>";
	      	        	 }
		              return e;  
		          } 
	      	     });
	        	/* columnNames.push({
	       	         title: "最后操作人 ",
	       	         field: "ApprovalUSERID,",
	       	         valign:'middle',
	       	         align:'center'
	       	     }); */
	        	/* columnNames.push({
	      	         title: "可否审批",
	      	         field: "Approval_OK",
	      	         valign:'middle',
	      	         align:'center',
	      	         formatter:function(value,row,index){
	      	        	/  if(row.Approval_OK == 0){
	      	        		 var e = "<a href=\"#\" mce_href=\"#\" onclick=\"update_approval_user('"+ row.Id+"')\"  >是</a> ";
	      	        	 }else if(row.Approval_OK == 1){
	      	        		 var e = "<a href=\"#\" mce_href=\"#\" onclick=\"update_approval_user('"+ row.Id+"')\"  >否</a> ";
	      	        	 } /
	      	        	 var e = "";
	      	        	  if(row.Approval_OK == 0){
	      	        		 e="否";
	      	        	 }else if(row.Approval_OK == 1){
	      	        		 e="是";
	      	        	 } 
	 	              return e;  
	 	          } 
	      	     }); */
	      	     columnNames.push({
	      	         title: "权限类型",
	      	         field: "Privilege_Type",
	      	         valign:'middle',
	      	         align:'center',
	      	         formatter:function(value,row,index){
	      	        	 var e = "";
	      	        	  if(row.Privilege_Type == 0){
	      	        		 e = e +"永久权限";
	      	        	 }else if(row.Privilege_Type == 1){
	      	        	 	 e = e +"临时权限";
	      	        	 } 
	 	              return e;  
	 	          } 
	      	     });
	      	   columnNames.push({
	       	         title: "权限开始时间",
	       	         field: "valid_startTime",
	       	         valign:'middle',
	       	         align:'center',
	       	         formatter:function(value,row,index){
	       	        	 var e = "<span>";
	       	        	  if(row.Privilege_Type == 0){
	       	        	  	//e= e +row.UpdateTime;
	       	        	  	e = e+"无限制";
	       	        	 }else if(row.Privilege_Type == 1){
	       	        	 	 e = e +row.valid_startTime; 
	       	        	 } 
	       	        	 e = e + "</span>";
	 	              return e;  
	 	          } 
	       	     });
	  columnNames.push({
	       	         title: "权限结束时间",
	       	         field: "valid_EndTime",
	       	         valign:'middle',
	       	         align:'center',
	       	         formatter:function(value,row,index){
	       	        	var e = "<span>";
	       	        	  if(row.Privilege_Type == 0){
      	        	  		//e = e +row.UpdateTime; ;
	       	        		e = e+"无限制";
	       	        	 }else if(row.Privilege_Type == 1){
	       	        	 	 e = e +row.valid_EndTime; ;
	       	        	 } 
	       	        	  e = e + "</span>";
	 	              return e;  
	 	          } 
	       	     });
					  columnNames.push({
			       	         title: "操作",
			       	         field: "function",
			       	         valign:'middle',
			       	         align:'center',
			       	         formatter:function(value,row,index){
			       	        	var e = "";
			      	        		var e = "<button id=\"button_Privilege_Status_"+row.Privilege_User_Role_id+"\" type=\"button\" class=\"\" onclick=\"updatePrivilege_Status(";
			      	        	  if(row.Privilege_Status == 2){
			      	        		 e=e+"1,'撤回',3,"+row.Privilege_User_Role_id+",'','','','')\">撤回";
			      	        		e=e+"</button>";
			      	        	 } else if (row.Privilege_Status == 3){
			      	        		 e=e+"1,'提交',2,"+row.Privilege_User_Role_id+",'','','','')\">再提交 ";
			      	        		e=e+"</button>";
			      	        	 }else if(row.Privilege_Status == 1){
			      	        		e = "";
			      	        	 }  
		      	        	     if(row.Privilege_Status == 0){
		      	        	    	e="不可 操作";
		      	        	     }else{
		      	        	    	e=e+"<button  type=\"button\" class=\"\" onclick=\"deleteRole_user("+row.Privilege_User_Role_id+")\">删除 ";
		      	        	    	e=e+"</button>";
		      	        	     }
		 	 	              return e;  
		 	 	          } 
			       	     });
	        } else if(method ==  3){
	        	 columnNames.push({
	       	         title: "操作",
	       	         field: "function",
	       	         valign:'middle',
	       	         align:'center',
 	       	         formatter:function(value,row,index){
 	       	        	var a = "<a  onclick=\"applyfor("+ row.Id +")\">申请</a> ";
  	 	              return a;  
  	 	          } 
	       	     });
	        }else if(method ==  4){
			       
			       	 columnNames.push({
	         	         title: "用户ID",
	         	         field: "USERID",
	         	         valign:'middle',
	         	         align:'center'
	         	     });
		 			  columnNames.push({
		       	         title: "用户名称",
		       	         field: "USERNAME",
		       	         valign:'middle',
		       	         align:'center'
		       	     });
	        	 columnNames.push({
	      	         title: "权限类型",
	      	         field: "Privilege_Type",
	      	         valign:'middle',
	      	         align:'center',
	      	         formatter:function(value,row,index){
	      	        	var e  ="";
	      	        	if(row.Privilege_Status == 1 ||  row.Privilege_Status == 2){
		      	        	 e= "<select id=\"select_Privilege_Type_id_"+row.Privilege_User_Role_id+"\" onchange=\"updatePrivilege_Type("+row.Privilege_User_Role_id+")\">";
		       	        	  if(row.Privilege_Type == 0){
		       	        		  e = e +"<option value=\"0\">永久权限";
		       	        	 	 e = e +"</option>";
		       	        	 	 e = e +"<option value=\"1\">临时权限";
		       	        	 	 e = e +"</option>"; 
		       	        	 }else if(row.Privilege_Type == 1){
		       	        	 	 e = e +"<option value=\"1\">临时权限";
		       	        	 	 e = e +"</option>"; 
		       	        		   e = e +"<option value=\"0\">永久权限";
		       	        	 	 e = e +"</option>";
		       	        	 } 
		       	        	 	 e = e +row.Approval_OK+"</select>"; 
	      	        	}else{
	      	        		 if(row.Privilege_Type == 0){
		      	        		 e = e +"永久权限";
		      	        	 }else if(row.Privilege_Type == 1){
		      	        	 	 e = e +"临时权限";
		      	        	 } 
	      	        	}
	 	              return e;  
	 	          } 
	      	     });
	        	 columnNames.push({
	       	         title: "权限开始时间",
	       	         field: "valid_startTime",
	       	         valign:'middle',
	       	         align:'center',
	       	    	 width: "10%",
 	       	         formatter:function(value,row,index){
	 	       	        	 var e = "";
	 	       	         if(row.Privilege_Type == 0){
	 	       	        	e = "";
	 	       	         }else{
	 	       	        	if(row.Privilege_Status == 1 ||  row.Privilege_Status == 2){
	 	       	        		e="<div onclick=\"showTime('valid_startTime_id_"+row.Privilege_User_Role_id+"')\"><input class=\"form-control\" disabled=\"disabled\"  value=\""+row.valid_startTime+"\" style=\" text-align: center;\" id=\"valid_startTime_id_"+row.Privilege_User_Role_id+"\" onchange=\"updatevalid_startTime("+row.Privilege_User_Role_id+",'valid_startTime_id_"+row.Privilege_User_Role_id+"')\"/></div>";
	 	       	        	}else{
	 	       	        		e=row.valid_startTime;
	 	       	        	}
 	       	         	 }
  	 	              return e;  
  	 	          } 
	       	     });
				  columnNames.push({
				       	         title: "权限结束时间",
				       	         field: "valid_EndTime",
				       	         valign:'middle',
				       	         align:'center',
				       	      	 width: "10%",
			 	       	         formatter:function(value,row,index){
			 	       	        	var e = "";
				 	       	         if(row.Privilege_Type == 0){
				 	       	        	e = "";
				 	       	         }else{
				 	       	         	if(row.Privilege_Status == 1 ||  row.Privilege_Status == 2){
				 	       	        		e="<div onclick=\"showTime('valid_EndTime_id_"+row.Privilege_User_Role_id+"')\"><input class=\"form-control\" disabled=\"disabled\"  value=\""+row.valid_EndTime+"\" style=\"text-align: center;\" id=\"valid_EndTime_id_"+row.Privilege_User_Role_id+"\"  onchange=\"updatevalid_EndTime("+row.Privilege_User_Role_id+",'valid_EndTime_id_"+row.Privilege_User_Role_id+"')\"/></div>";
				 	       	         	}else{
				 	       	         		e=row.valid_EndTime;
				 	       	         	}
			 	       	         	 }
			  	 	              return e;  
			  	 	          } 
				       	     });
				  columnNames.push({
		      	         title: "权限状态",
		      	         field: "Privilege_Status",
		      	         valign:'middle',
		      	         align:'center',
		      	         formatter:function(value,row,index){
		      	        	 var e = "";
		      	        	if(row.Privilege_Status == 0){
		      	        		 e="系统创建";
		      	        	 }else if(row.Privilege_Status == 1){
		      	        		 e="通过审批";
		      	        	 }else if(row.Privilege_Status == 2){
		      	        		 e="等待审批";
		      	        	 } else if(row.Privilege_Status == 3){
		      	        		 e="已撤回";
		      	        	 } else if(row.Privilege_Status == 4){
		      	        		 e="已拒绝";
		      	        	 } else if(row.Privilege_Status == 5){
		      	        		 e="已失效 ";
		      	        	 }
			              return e;  
			          } 
		      	     });
	        	 columnNames.push({
	       	         title: "操作",
	       	         field: "function",
	       	         valign:'middle',
	       	         align:'center',
 	       	         formatter:function(value,row,index){
 	       	        	var e = "";
	      	        	  if(row.Privilege_Status == 2){
	      	        		e = "<button id=\"button_Privilege_Status_"+row.Privilege_User_Role_id+"\" type=\"button\" class=\"\" onclick=\"getUserRoleDetail(";
	      	        		e=e+row.Privilege_User_Role_id+")\">明细";
	      	        		e=e+"</button>";
	      	        		e = e+"<button id=\"button_Privilege_Status_"+row.Privilege_User_Role_id+"\" type=\"button\" class=\"\" onclick=\"editApproval_Process(";
	      	        		e=e+row.Id+",2,'"+row.RoleName+"',"+row.Privilege_User_Role_id+")\">审批情况";
	      	        		e=e+"</button>";
	      	        		e = e+"<button id=\"button_Privilege_Status_"+row.Privilege_User_Role_id+"\" type=\"button\" class=\"\" onclick=\"updatePrivilege_Status(";
	      	        		e=e+"2,'同意',1,"+row.Privilege_User_Role_id+",'"+row.Process_Group_ID+"','"+row.Process_Group_Approval_Order+"','"+row.Process_Group_Process_Title+"','"+row.Process_Group_Process_Description+"')\">通过";
	      	        		e=e+"</button>";
	      	        		e =e+ "<button id=\"button_Privilege_Status_"+row.Privilege_User_Role_id+"\" type=\"button\" class=\"\" onclick=\"updatePrivilege_Status(";
	      	        		e=e+"2,'拒绝',4,"+row.Privilege_User_Role_id+",'"+row.Process_Group_ID+"','"+row.Process_Group_Approval_Order+"','"+row.Process_Group_Process_Title+"','"+row.Process_Group_Process_Description+"')\">拒绝";
	      	        		e=e+"</button>";
	      	        	 } else {
	      	        		 e = "不可操作！";
	      	        	 } 
  	 	              return e;  
  	 	          } 
	       	     });
	        }else if(method ==  5){
	        	sidePagination = "server";
	        	columnNames.push({
        	         title: "用户ID",
        	         field: "USERID",
        	         valign:'middle',
        	         align:'center'
        	     });
	 			  columnNames.push({
	       	         title: "用户名称",
	       	         field: "USERNAME",
	       	         valign:'middle',
	       	         align:'center'
	       	     });
       	 columnNames.push({
     	         title: "权限类型",
     	         field: "Privilege_Type",
     	         valign:'middle',
     	         align:'center',
     	         formatter:function(value,row,index){
     	        	var e  ="";
     	        /* 	if(row.Privilege_Status == 1 ||  row.Privilege_Status == 2){
	      	        	 e= "<select id=\"select_Privilege_Type_id_"+row.Privilege_User_Role_id+"\" onchange=\"updatePrivilege_Type("+row.Privilege_User_Role_id+")\">";
	       	        	  if(row.Privilege_Type == 0){
	       	        		  e = e +"<option value=\"0\">永久权限";
	       	        	 	 e = e +"</option>";
	       	        	 	 e = e +"<option value=\"1\">临时权限";
	       	        	 	 e = e +"</option>"; 
	       	        	 }else if(row.Privilege_Type == 1){
	       	        	 	 e = e +"<option value=\"1\">临时权限";
	       	        	 	 e = e +"</option>"; 
	       	        		   e = e +"<option value=\"0\">永久权限";
	       	        	 	 e = e +"</option>";
	       	        	 } 
	       	        	 	 e = e +row.Approval_OK+"</select>"; 
     	        	}else{ }*/
     	        		 if(row.Privilege_Type == 0){
	      	        		 e = e +"永久权限";
	      	        	 }else if(row.Privilege_Type == 1){
	      	        	 	 e = e +"临时权限";
	      	        	 } 
	              return e;  
	          } 
     	     });
	       	 columnNames.push({
	      	         title: "权限开始时间",
	      	         field: "valid_startTime",
	      	         valign:'middle',
	      	         align:'center',
	      	    	 width: "10%",
	       	         formatter:function(value,row,index){
		       	        	 var e = "";
		       	         if(row.Privilege_Type == 0){
		       	        	e = "";
		       	         }else{
		       	        	//if(row.Privilege_Status == 1 ||  row.Privilege_Status == 2){
		       	        		//e="<input class=\"form-control\"  value=\""+row.valid_startTime+"\" style=\" text-align: center;\" id=\"valid_startTime_id_"+row.Privilege_User_Role_id+"\" onclick=\"showTime('valid_startTime_id_"+row.Privilege_User_Role_id+"')\"onchange=\"updatevalid_startTime("+row.Privilege_User_Role_id+",'valid_startTime_id_"+row.Privilege_User_Role_id+"')\"/>";
		       	        	//}else{
		       	        		e=row.valid_startTime;
		       	        	//}
	       	         	 }
		 	              return e;  
		 	          } 
	      	     });
			  columnNames.push({
			       	         title: "权限结束时间",
			       	         field: "valid_EndTime",
			       	         valign:'middle',
			       	         align:'center',
			       	      	 width: "10%",
		 	       	         formatter:function(value,row,index){
		 	       	        	var e = "";
			 	       	         if(row.Privilege_Type == 0){
			 	       	        	e = "";
			 	       	         }else{
			 	       	         	//if(row.Privilege_Status == 1 ||  row.Privilege_Status == 2){
			 	       	        		//e="<input class=\"form-control\"  value=\""+row.valid_EndTime+"\" style=\"text-align: center;\" id=\"valid_EndTime_id_"+row.Privilege_User_Role_id+"\" onclick=\"showTime('valid_EndTime_id_"+row.Privilege_User_Role_id+"') \"onchange=\"updatevalid_EndTime("+row.Privilege_User_Role_id+",'valid_startTime_id_"+row.Privilege_User_Role_id+"')\"/>";
			 	       	         	//}else{
			 	       	         		e=row.valid_EndTime;
			 	       	         	//}
		 	       	         	 }
		  	 	              return e;  
		  	 	          } 
			       	     });
				  columnNames.push({
		      	         title: "权限状态",
		      	         field: "Privilege_Status",
		      	         valign:'middle',
		      	         align:'center',
		      	         formatter:function(value,row,index){
		      	        	 var e = "";
		      	        	if(row.Privilege_Status == 0){
		      	        		 e="系统创建";
		      	        	 }else if(row.Privilege_Status == 1){
		      	        		 e="通过审批";
		      	        	 }else if(row.Privilege_Status == 2){
		      	        		 e="等待审批";
		      	        	 } else if(row.Privilege_Status == 3){
		      	        		 e="已撤回";
		      	        	 } else if(row.Privilege_Status == 4){
		      	        		 e="已拒绝";
		      	        	 } else if(row.Privilege_Status == 5){
		      	        		 e="已失效 ";
		      	        	 }
			              return e;  
			          } 
		      	     });
		       	 columnNames.push({
		      	         title: "操作",
		      	         field: "function",
		      	         valign:'middle',
		      	         align:'center',
		       	         formatter:function(value,row,index){
		       	        	var e = "";
		     	        		e = "<button id=\"button_Privilege_Status_"+row.Privilege_User_Role_id+"\" type=\"button\" class=\"\" onclick=\"getUserRoleDetail(";
		     	        		e=e+row.ID+")\">明细";
		     	        		e=e+"</button>";
		     	        		e = e+"<button id=\"button_Privilege_Status_"+row.Privilege_User_Role_id+"\" type=\"button\" class=\"\" onclick=\"editApproval_Process(";
		     	        		e=e+row.Privilege_Role_ID+",2,'"+row.RoleName+"',"+row.ID+")\">审批情况";
		     	        		e=e+"</button>";
/* 		     	        	  if(row.Privilege_Status == 2 || row.Privilege_Status == 2){
		     	        	 } else {
		     	        		 e = "不可操作！";
		     	        	 } 
 */			 	              
                             return e;  
			 	          } 
		      	     });
	        }
	        $('#priRoleTable').bootstrapTable('destroy');
	       	 //初始化表格,动态从服务器加载数据
	          $("#priRoleTable").bootstrapTable({
	              url: "/hssp/rest/getPrivilege_Role", //获取数据的Servlet地址
	           // method: "POST",  //使用get请求到服务器获取数据
	           // contentType: "application/x-www-form-urlencoded",//post请求的话就加上这个句话
	              toolbar: "#toolbarPriField",
	              striped: true,                      //是否显示行间隔色
	              cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	              pagination: true,                   //是否显示分页（*）
	              sortable: false,                     //是否启用排序
	              sortOrder: "asc",                   //排序方式
	              sidePagination: sidePagination,           //分页方式：client客户端分页，server服务端分页（*）
	              pageNumber:1,                       //初始化加载第一页，默认第一页
	              pageSize: 10,                       //每页的记录行数（*）
	              pageList: [5, 10],        			//可供选择的每页的行数（*）
	              search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
	              strictSearch: true,
	              showColumns: false,                  //是否显示所有的列
	              showRefresh: false,                  //是否显示刷新按钮
	              minimumCountColumns: 2,             //最少允许的列数
	              clickToSelect: true,                //是否启用点击选中行
	              height: "",                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
	              uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
	              showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
	              cardView: false,                    //是否显示详细视图
	              detailView: false,                   //是否显示父子表
	              queryParams: function queryParams(params) {   //设置查询参数
	                var param = {  
	                    pageNumber: params.offset,  
	                    pageSize: params.limit,
	                    isDeleted:isdeleted,
	                    ID:Id,
	                    roleName:roleName,
	                    roleDescription:roleDescription,
	                    method:method,
	                    privilege_Status:privilege_Status
	                };  
	                return param;                 
	              },
	             
	              formatLoadingMessage: function () {
	                  return "请稍等，正在加载中...";
	              },
	              formatNoMatches: function (data) {  //没有匹配的结果
	                  return '无符合条件的记录';
	              }, 
	              columns : columnNames,
	             	responseHandler: function(data){
	             		if(sidePagination == "server"){
	             			return data;
	             		}else{
	             			return data.rows;
	             		}
	             	},
	             	onLoadError: function(e){
	    	   	      parent.location.reload();    	    
	             	}
	            });
	      }
	 
	
	 
	/**
	 * 角色下用户查看，删除当前用户
	 *
	 */
	function  deleteRole_user(role_user_id) {
		$.ajax({
	   	     type: "post",
	   	     url: "/hssp/rest/deleteRole_user?Role_user_id="+role_user_id,
	   	     dataType: "json",
	   	     success: function(data){
	   	    	if(data.result == "succeed"){
	   	    		//parent.$.dialog("删除成功！", false, 1000);
	   	    		alert("删除成功！");
	   	    		showPrivilegeSelf();
	   	    	 }else{
	   	    		alert("删除失败！");
	   	    		//parent.$.dialog("删除失败！", false, 1000);  
	   	    	 }
	   	     },error:function(e){
	   	    		parent.location.reload();    	    
				}
		 });
	}
	 //更新 某一行数据
	 function  updateTableRows(tableID,checkIndex,rowData){
		 $('#'+tableID).bootstrapTable('updateRow', {index: checkIndex, row: rowData});
	 }
	//更新用户是否可以审批      
	function updatevalid_startTime(Privilege_User_Role_id,input_time_id) {
		$.ajax({
	   	     type: "post",
	   	     url: "/hssp/rest/updatevalid_startTime?Privilege_User_Role_id="+Privilege_User_Role_id+"&valid_startTime="+$("#"+input_time_id).val(),
	   	     dataType: "json",
	   	     success: function(data){
	   	    	if(data.result == "succeed"){
	   	    		//parent.$.dialog("删除成功！", false, 1000);
	   	    		alert("权限修改成功！");
	   	    	 }else{
	   	    		alert("权限修改失败！");
	   	    		//parent.$.dialog("删除失败！", false, 1000);  
	   	    	 }
	   	     },error:function(e){
	   	    		parent.location.reload();    	    
				}
		 });
	}
	//更新用户是否可以审批      
	function updatevalid_EndTime(Privilege_User_Role_id,input_time_id) {
		$.ajax({
	   	     type: "post",
	   	     url: "/hssp/rest/updatevalid_EndTime?Privilege_User_Role_id="+Privilege_User_Role_id+"&valid_EndTime="+$("#"+input_time_id).val(),
	   	     dataType: "json",
	   	     success: function(data){
	   	    	if(data.result == "succeed"){
	   	    		//parent.$.dialog("删除成功！", false, 1000);
	   	    		alert("权限修改成功！");
	   	    	 }else{
	   	    		alert("权限修改失败！");
	   	    		//parent.$.dialog("删除失败！", false, 1000);  
	   	    	 }
	   	     },error:function(e){
	   	    		parent.location.reload();    	    
				}
		 });
	}
	//更新用户是否可以审批      
	function updateApproval(Privilege_User_Role_id) {
		$.ajax({
	   	     type: "post",
	   	     url: "/hssp/rest/updateApproval?Privilege_User_Role_id="+Privilege_User_Role_id+"&Approval_OK="+$("#select_Approval_id_"+Privilege_User_Role_id).val(),
	   	     dataType: "json",
	   	     success: function(data){
	   	    	if(data.result == "succeed"){
	   	    		//parent.$.dialog("删除成功！", false, 1000);
	   	    		alert("权限修改成功！");
	   	    	 }else{
	   	    		alert("权限修改失败！");
	   	    		//parent.$.dialog("删除失败！", false, 1000);  
	   	    	 }
	   	     },error:function(e){
	   	    		parent.location.reload();    	    
				}
		 });
	}
	//更新用户权限类型 
	function updatePrivilege_Type(Privilege_User_Role_id) {
	    $.ajax({
	   	     type: "post",
	   	     url: "/hssp/rest/updatePrivilege_Type?Privilege_User_Role_id="+Privilege_User_Role_id+"&Privilege_Type="+$("#select_Privilege_Type_id_"+Privilege_User_Role_id).val(),
	   	     dataType: "json",
	   	     success: function(data){
	   	    	if(data.result == "succeed"){
	   	    		//parent.$.dialog("删除成功！", false, 1000);
	   	    		alert("权限类型修改成功！");
	   	    		applyForPrivilege();
	   	    	 }else{
	   	    		alert("权限类型修改失败！");
	   	    		//parent.$.dialog("删除失败！", false, 1000);  
	   	    	 }
	   	     },
	   	     error:function(e){
	   	    		parent.location.reload();    	    
				}
		 });
	}
	function Post(URL, PARAMTERS) {
		//创建form表单
		var temp_form = document.createElement("form");
		temp_form.action = URL;
		//如需打开新窗口，form的target属性要设置为'_blank'
		temp_form.target = "_blank";
		temp_form.method = "post";
		temp_form.style.display = "none";
		//添加参数
		for ( var item in PARAMTERS) {
			var opt = document.createElement("input");
			opt.name = PARAMTERS[item].name;
			opt.value = PARAMTERS[item].value;
			temp_form.appendChild(opt);
		}
		document.body.appendChild(temp_form);
		//提交数据
		temp_form.submit();
	}
	function getUserRoleDetail(Privilege_User_Role_id) {
		//hideAll();
		//$("#Approval_User_Role_Detail_Div_id").modal(1000);
		 $.ajax({
	   	     type: "post",
	   	     url: "/hssp/rest/getPrivilege_Role?Privilege_User_Role_id="+Privilege_User_Role_id+"&method=6",
	   	     dataType: "json",
	   	     success: function(data){
	   	    	if(data.result == "succeed"){
	   	    		  if(data.rows.length == 1){
	   	    			var valueMaps = data.rows[0]
	   	    			$("#Approval_Role_Detail_RoleName").val(valueMaps.RoleName);
	   	    			$("#Approval_Role_Detail_Description").val(valueMaps.Description);
	   	    			$("#Approval_User_UserID").val(valueMaps.USERID);
	   	    			$("#Approval_User_UserName").val(valueMaps.USERNAME);
	   	    			$("#Approval_User_deptName").val(valueMaps.Value_DeptName);
	   	    			if(valueMaps.Privilege_Type == 0){
	   	    				$("#Approval_User_Role_Privilege_Type").val("永久权限");
	   	    				$("#Approval_User_Role_valid_startTime").val("");
		   	    			$("#Approval_User_Role_valid_EndTime").val("");
	   	    			}else if(valueMaps.Privilege_Type == 1){
	   	    				$("#Approval_User_Role_Privilege_Type").val("临时权限");
	   	    				$("#Approval_User_Role_valid_startTime").val(valueMaps.valid_startTime);
		   	    			$("#Approval_User_Role_valid_EndTime").val(valueMaps.valid_EndTime);
	   	    			}
	   	    			$("#Approval_User_Role_Detail_Description").text(valueMaps.Privilege_User_Role_Description);
						$("#Approval_User_Role_Detail_Div_id").modal();
	   	    		  }
	   	    	 }else{
	   	    		alert(updateDes+"失败！");
	   	    		//parent.$.dialog("删除失败！", false, 1000);  
	   	    	 }
	   	     },error:function(e){
	   	    		parent.location.reload();    	    
				}
		 });  
	}
	//更新审批结果  
	function updatePrivilege_Status(refreshShowType,updateDes,Privilege_Status,Privilege_User_Role_id,Approval_Process_Group_Id,Approval_Order,Process_Title,Process_Description) {
	    $.ajax({
	   	     type: "post",
	   	     url: "/hssp/rest/updatePrivilege_Status?Privilege_User_Role_id="+Privilege_User_Role_id+
	   	    		 "&Privilege_Status="+Privilege_Status+"&Approval_Process_Group_Id="+Approval_Process_Group_Id+"&Approval_Order="
	   	    		 +Approval_Order+"&Process_Title="+Process_Title+"&Process_Description="+Process_Description,
	   	     dataType: "json",
	   	     success: function(data){
	   	    	if(data.result == "succeed"){
	   	    		//parent.$.dialog("删除成功！", false, 1000);
	   	    		alert(updateDes+"成功！");
	   	    		if(refreshShowType ==  1){
	   	    			showPrivilegeSelf();
	   	    		}else  if(refreshShowType ==  2){
	   	    			applyForPrivilege();
	   	    		}
	   	    	 }else{
	   	    		alert(updateDes+"失败！");
	   	    		//parent.$.dialog("删除失败！", false, 1000);  
	   	    	 }
	   	     },error:function(e){
	   	    		parent.location.reload();    	    
				}
		 });
	}
	//查看当前自己权限（角色下用户展示）
	function getUser_role(role_id,method,roleName){
		hideAll();
		$("#role_name").text(roleName);
		$("#show_privilege_Div").show();
		/* $("#showPrivilegeSelf").show(); */
		initTable_user(role_id, method, "show_privilege_Table", "", "")
	}
	
	function initTable_user(role_id,method,tableId,userid,username) {
        //先销毁表格
        var columnNames = [];
        $("#"+tableId).bootstrapTable('destroy');
   			  columnNames.push({
        	         title: "序号",
        	         field: "rowNum",
        	         valign:'middle',
        	         align:'center',
        	         formatter:function(value,row,index){ 
  	 	              return index+1;  
  	 	             } 
        	     });
   			  /*columnNames.push({
        	         title: "编码",
        	         field: "Id",
        	         valign:'middle',
        	         align:'center'
        	     });*/
 			if(method == 6){
 				columnNames.push({
	       	         title: "权限状态",
	       	         field: "Privilege_Status",
	       	         valign:'middle',
	       	         align:'center',
	       	         formatter:function(value,row,index){
	       	        	 var e = "";
	       	        	  if(row.Privilege_Status == 0){
	       	        		 e="系统创建";
	       	        	 }else if(row.Privilege_Status == 1){
	       	        		 e="通过审批";
	       	        	 }else if(row.Privilege_Status == 2){
	       	        		 e="等待审批";
	       	        	 } else if(row.Privilege_Status == 2){
	       	        		 e="已撤回";
	       	        	 } 
	 	              return e;  
	 	          } 
	       	     });
 				/* columnNames.push({
	       	         title: "可否审批",
	       	         field: "Approval_OK",
	       	         valign:'middle',
	       	         align:'center',
	       	         formatter:function(value,row,index){
	       	        	/  if(row.Approval_OK == 0){
	       	        		 var e = "<a href=\"#\" mce_href=\"#\" onclick=\"update_approval_user('"+ row.Id+"')\"  >是</a> ";
	       	        	 }else if(row.Approval_OK == 1){
	       	        		 var e = "<a href=\"#\" mce_href=\"#\" onclick=\"update_approval_user('"+ row.Id+"')\"  >否</a> ";
	       	        	 } /
	       	        	 var e = "";
	       	        	  if(row.Approval_OK == 0){
	       	        		 e="否";
	       	        	 }else if(row.Approval_OK == 1){
	       	        		 e="是";
	       	        	 } 
 	 	              return e;  
 	 	          } 
	       	     }); */
	       	     columnNames.push({
	       	         title: "权限类型",
	       	         field: "Privilege_Type",
	       	         valign:'middle',
	       	         align:'center',
	       	         formatter:function(value,row,index){
	       	        	 var e = "";
	       	        	  if(row.Privilege_Type == 0){
	       	        		 e = e +"永久权限";
	       	        	 }else if(row.Privilege_Type == 1){
	       	        	 	 e = e +"临时权限";
	       	        	 } 
 	 	              return e;  
 	 	          } 
	       	     });
	 			  columnNames.push({
				       	         title: "权限开始时间",
				       	         field: "valid_startTime",
				       	         valign:'middle',
				       	         align:'center',
			 	       	         formatter:function(value,row,index){
			 	       	        	 var e = "<span>";
			 	       	        	  if(row.Privilege_Type == 0){
			 	       	        	  	e = e +row.UpdateTime; 
			 	       	        	 }else if(row.Privilege_Type == 1){
			 	       	        	 	 e = e +row.valid_startTime; 
			 	       	        	 } 
			 	       	        	 e = e + "</span>";
			  	 	              return e;  
			  	 	          } 
				       	     });
	 			  columnNames.push({
				       	         title: "权限结束时间",
				       	         field: "valid_EndTime",
				       	         valign:'middle',
				       	         align:'center',
			 	       	         formatter:function(value,row,index){
			 	       	        	var e = "<span>";
			 	       	        	  if(row.Privilege_Type == 0){
		 	       	        	  		e = e +row.UpdateTime; ;
			 	       	        	 }else if(row.Privilege_Type == 1){
			 	       	        	 	 e = e +row.valid_EndTime; ;
			 	       	        	 } 
			 	       	        	  e = e + "</span>";
			  	 	              return e;  
			  	 	          } 
				       	     });
 			}else if(method == 3){
 				 columnNames.push({
          	         title: "用户ID",
          	         field: "USERID",
          	         valign:'middle',
          	         align:'center'
          	     });
 			  columnNames.push({
       	         title: "用户名称",
       	         field: "USERNAME",
       	         valign:'middle',
       	         align:'center'
       	     });
 				 /* columnNames.push({
 	       	         title: "可否审批",
 	       	         field: "Approval_OK",
 	       	         valign:'middle',
 	       	         align:'center',
 	       	         formatter:function(value,row,index){
 	       	        	/  if(row.Approval_OK == 0){
 	       	        		 var e = "<a href=\"#\" mce_href=\"#\" onclick=\"update_approval_user('"+ row.Id+"')\"  >是</a> ";
 	       	        	 }else if(row.Approval_OK == 1){
 	       	        		 var e = "<a href=\"#\" mce_href=\"#\" onclick=\"update_approval_user('"+ row.Id+"')\"  >否</a> ";
 	       	        	 } /
 	       	        	 var e = "<select id=\"select_Approval_id_"+row.Id+"\" onchange=\"updateApproval("+row.Id+")\">";
 	       	        	  if(row.Approval_OK == 0){
 	       	        		  e = e +"<option value=\"0\">否";
 	       	        	 	 e = e +"</option>";
 	       	        	 	 e = e +"<option value=\"1\">是";
 	       	        	 	 e = e +"</option>"; 
 	       	        	 }else if(row.Approval_OK == 1){
 	       	        		  e = e +"<option value=\"0\">否";
 	       	        	 	 e = e +"</option>";
 	       	        	 	 e = e +"<option value=\"1\">是";
 	       	        	 	 e = e +"</option>"; 
 	       	        	 } 
 	       	        	 	 e = e +row.Approval_OK+"</select>";
  	 	              return e;  
  	 	          } 
 	       	     }); */
 	       	     columnNames.push({
 	       	         title: "权限类型",
 	       	         field: "Privilege_Type",
 	       	         valign:'middle',
 	       	         align:'center',
 	       	         formatter:function(value,row,index){
 	       	        	 var e = "<select id=\"select_Privilege_Type_id_"+row.Id+"\" onchange=\"updatePrivilege_Type("+row.Id+")\">";
 	       	        	  if(row.Privilege_Type == 0){
 	       	        		 e = e +"<option value=\"0\">永久权限";
 	       	        	 	 e = e +"</option>";
 	       	        	 	 e = e +"<option value=\"1\">临时权限";
 	       	        	 	 e = e +"</option>"; 
 	       	        	 }else if(row.Privilege_Type == 1){
 	       	        	 	 e = e +"<option value=\"1\">临时权限";
 	       	        	 	 e = e +"</option>"; 
 	       	        		   e = e +"<option value=\"0\">永久权限";
 	       	        	 	 e = e +"</option>";
 	       	        	 } 
 	       	        	 	 e = e +row.Approval_OK+"</select>";
  	 	              return e;  
  	 	          } 
 	       	     });
	 			  columnNames.push({
				       	         title: "权限开始时间",
				       	         field: "valid_startTime",
				       	         valign:'middle',
				       	         align:'center',
			 	       	         formatter:function(value,row,index){
			 	       	        	 var e = "<span>";
			 	       	        	  if(row.Privilege_Type == 0){
			 	       	        	  	e = e +row.UpdateTime; 
			 	       	        	 }else if(row.Privilege_Type == 1){
			 	       	        	 	 e = e +row.valid_startTime; 
			 	       	        	 } 
			 	       	        	 e = e + "</span>";
			  	 	              return e;  
			  	 	          } 
				       	     });
	 			  columnNames.push({
				       	         title: "权限结束时间",
				       	         field: "valid_EndTime",
				       	         valign:'middle',
				       	         align:'center',
			 	       	         formatter:function(value,row,index){
			 	       	        	var e = "<span>";
			 	       	        	  if(row.Privilege_Type == 0){
		 	       	        	  		e = e +row.UpdateTime; ;
			 	       	        	 }else if(row.Privilege_Type == 1){
			 	       	        	 	 e = e +row.valid_EndTime; ;
			 	       	        	 } 
			 	       	        	  e = e + "</span>";
			  	 	              return e;  
			  	 	          } 
				       	     });
 				 columnNames.push({
 	 				 title: '操作',
 	 	            field: 'Id',
 	 	            align: 'center',
 	 	            formatter:function(value,row,index){ 
 	 	            	//var a = "<a href=\"#\" mce_href=\"#\" onclick=\"getPriField('"+ row.Id +"')\">查看详情</a> ";
 	 	            	var e = "<a href=\"#\" mce_href=\"#\" onclick=\"deleteRole_user('"+ row.Id+"')\"  >删除</a> ";
 	 	              return e;  
 	 	          } 
 	 	 	     });
 			}else if(method == 4 || method == 5){
 				columnNames.push({
	 				 title: '操作',
	 	            field: 'Id',
	 	            align: 'center',
	 	            formatter:function(value,row,index){ 
	 	            	//var a = "<a href=\"#\" mce_href=\"#\" onclick=\"getPriField('"+ row.Id +"')\">查看详情</a> ";
	 	            	var e = "<a href=\"#\" mce_href=\"#\" onclick=\"addRole_user('"+ row.Id+"')\"  >添加</a> ";
	 	              return e;  
	 	          } 
	 	 	     });
 			}
       	 //初始化表格,动态从服务器加载数据
          $("#"+tableId).bootstrapTable({
              url: "/hssp/rest/getConfigInfo", //获取数据的Servlet地址
           // method: "POST",  //使用get请求到服务器获取数据
           // contentType: "application/x-www-form-urlencoded",//post请求的话就加上这个句话
           
              striped: true,                      //是否显示行间隔色
              cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
              pagination: true,                   //是否显示分页（*）
              sortable: false,                     //是否启用排序
              sortOrder: "asc",                   //排序方式
              sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
              pageNumber:1,                       //初始化加载第一页，默认第一页
              pageSize: 10,                       //每页的记录行数（*）
              pageList: [5, 10],        			//可供选择的每页的行数（*）
              search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
              strictSearch: true,
              showColumns: false,                  //是否显示所有的列
              showRefresh: false,                  //是否显示刷新按钮
              minimumCountColumns: 2,             //最少允许的列数
              clickToSelect: true,                //是否启用点击选中行
              height: "",                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
              uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
              showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
              cardView: false,                    //是否显示详细视图
              detailView: false,                   //是否显示父子表
              queryParams: function queryParams(params) {   //设置查询参数
                var param = {  
                    pageNumber: params.pageNumber,  
                    pageSize: params.pageSize,
                    grade : method,
                    flagDicDb:0,
                    RoleId:role_id,
                    userId:userid,
                    userName:username
                };  
                return param;                 
              },
             
              formatLoadingMessage: function () {
                  return "请稍等，正在加载中...";
              },
              formatNoMatches: function () {  //没有匹配的结果
                  return '无符合条件的记录';
              }, 
              columns : columnNames,
             	responseHandler: function(data){
             		return data.rows;
             	},
             	onLoadError: function(e){
	    	   	      parent.location.reload();    	    
	             	}
            });
      }
	$.fn.datetimepicker.dates['zh'] = {
	        days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
	        daysShort: ["日", "一", "二", "三", "四", "五", "六", "日"],
	        daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
	        months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
	        monthsShort: ["01月", "02月", "03月", "04月", "05月", "06月", "07月", "08月", "09月", "10月", "11月", "12月"],
	        meridiem: ["上午", "下午"],
	        suffix:      ["st", "nd", "rd", "th"],
	        today: "今天",
	        clear: "Clear"
	    };
	    function showTime(id){
	    	
	    	$("#"+id).datetimepicker({
	            language: 'zh',  //用自己设置的时间文字
	            weekStart: 1,  //一周从那天开始，默认为0，从周日开始，可以设为1从周一开始
	             startDate:new Date(), //开始时间，可以写字符串，也可以直接写日期格式new Date(),在这之前的日期不能选择
	            //endDate:"2018-6-20",
	            //daysOfWeekDisabled: [0,4,6],  //一周的周几不能选
	            todayBtn: 1,  //是否显示今天按钮，0为不显示
	            autoclose: 1, //选完时间后是否自动关闭
	            todayHighlight: 1,  //高亮显示当天日期
	            startView: 4, //0从小时视图开始，选分;1	从天视图开始，选小时;2从月视图开始，选天;3从年视图开始，选月;4从十年视图开始，选年
	            minView: 2,//最精确时间，默认0；0从小时视图开始，选分；1从天视图开始，选小时；2从月视图开始，选天；3从年视图开始，选月；4从十年视图开始，选年
	            //maxView:4,  //默认值：4, ‘decade’
	            keyboardNavigation:true,  //是否可以用键盘方向键选日期，默认true
	            forceParse: 0, //强制解析,你输入的可能不正规，但是它胡强制尽量解析成你规定的格式（format）
	            format: 'yyyy-mm-dd',// 格式,注意ii才是分，mm或MM都是月
	            minuteStep:5, //选择分钟时的跨度，默认为5分钟
	            //pickerPosition:"top-right",  // ‘bottom-left’，’top-right’，’top-left’’bottom-right’
	            showMeridian:0, //在日期和小时选择界面，出现上下午的选项,默认false
	            showSecond: false,
	            showMillisec: false,
	            //timeFormat: 'hh:mm:ss:l',
	            //bootcssVer: 3,
	        });
	    	$("#"+id).datetimepicker('show');
	    }
	</script>
</body>
</html>