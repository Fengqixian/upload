<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/basictable.css" />
        <link rel="icon" href="${pageContext.request.contextPath}/resources/images/logo ico.png">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap-theme.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap.min.css" />
    
    <!--  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.3.5-dist/css/bootstrap.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.3.5-dist/css/bootstrap-theme.css" />-->
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/css_config.css"  />
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
	
</head>
<body>

<div id="dicDb_div"  style=" height:100%; width:100%; overflow:auto; float:left;">
		<div id="toolbarDicDb" class="btn-group">
            <button id="showDicDbHs" type="button" class="btn btn-primary" onclick="showDicDbHs()">
                <span class="glyphicon glyphicon-trash" aria-hidden="true" "></span>回收站
            </button>
            <button id="showDicAddDb" type="button" class="btn btn-primary" onclick="showDicAddDb()">
                <span class="glyphicon glyphicon-plus" aria-hidden="true" "></span>添加表
            </button>
            
            <button id="showDicDbBack" style="display: none;" type="button" class="btn btn-primary" onclick="showDicDb()">
                <span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span>返回
            </button>
            
            
        </div>
			<table  id="cusTable"></table>
	</div>
	
	
<div id="dicField_div"  style=" height:100%; width:100%; overflow:auto; float:left;">
		<div id="toolbarDicField" class="btn-group">
            <button id="showDicFieldHs" type="button" class="btn btn-primary"  onclick="showDicFieldHs()">
                <span class="glyphicon glyphicon-trash" aria-hidden="true" ></span> 回收站
            </button>
            <button id="showDicFieldBack" style="display: none;" type="button" class="btn btn-primary"  onclick="showDicFieldBack()">
                <span class="glyphicon glyphicon-share-alt" aria-hidden="true" ></span> 返回字段列表
            </button>
            <button id="showDicDb" type="button" class="btn btn-primary" onclick="showDicDb()">
                <span class="glyphicon glyphicon-share-alt" aria-hidden="true" ></span> 返回上一级
            </button>
        </div>

			<table id="dicFieldTable"></table>
		</form>
	</div>
	
	
<div id="solrConfig_div"  style=" height:100%; width:100%; overflow:auto; float:left;">
		<div id="toolbarDicAddDb" class="btn-group">
            <button id="addTable" type="button" class="btn btn-primary" onclick="addTable()">
                <span class="glyphicon glyphicon-plus" aria-hidden="true" "></span>添加到MySql
            </button>
            <button id="showDicDbBack" type="button" class="btn btn-primary" onclick="showDicDb()">
                <span class="glyphicon glyphicon-share-alt" aria-hidden="true" ></span>返回表配置
            </button>
            
        </div>
			<table  id="cusAddTable">
			 <thead>
		       <th>选择</th>
		       </thead>
		       <tbody>
		        
		       </tbody>
			</table>
	</div>	
	

<div class="modal fade" id="myModal_dicDb" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel_dicDb">修改</h4>
                </div>
                <div class="modal-body">

                    <div class="form-group">
                        <label for="txt_departmentname">Id</label>
                        <input type="text" name="txt_departmentname"  class="form-control" id="Id" readonly= "true">
                    </div>
                    <div class="form-group">
                        <label for="txt_parentdepartment">库名</label>
                        <input type="text" name="CatalogName" class="form-control" id="CatalogName" placeholder="库名">
                    </div>
                    <div class="form-group">
                        <label for="txt_departmentlevel">表名</label>
                        <input type="text" name="CollectionName" class="form-control" id="CollectionName" placeholder="表名">
                    </div>
                    <div class="form-group">
                        <label for="txt_statu">描述</label>
                        <input type="text" name="Description" class="form-control" id="Description" placeholder="描述">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
                    <button type="button" id="btn_submit" class="btn btn-primary" data-dismiss="modal" onclick="saveEdit()"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
                </div>
            </div>
        </div>
    </div>
    
    
    
    <div class="modal fade" id="myModal_Dic_Field_Edit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModal_Dic_Field_Edit_title">属性值修改</h4>
                </div>
                <div class="modal-body" >
                    <div class="form-group" style="display:none">
                        <label for="txt_departmentname">编码</label>
                        <input type="text" name="dicFieldId"  class="form-control" id="Dic_Field_Edit_dicFieldId" readonly= "true">
                    </div>
                    <div class="form-group">
                        <label for="txt_parentdepartment">字段名称</label>
                        <input type="text" name="FieldName" class="form-control" id="Dic_Field_Edit_FieldName" placeholder="字段名称" readonly= "true">
                    </div>
                    <div class="form-group">
                        <label for="txt_departmentlevel">字段说明</label>
                        <input type="text" name="DisplayName" class="form-control" id="Dic_Field_Edit_DisplayName" placeholder="字段说明" readonly= "true">
                    </div>
                     <div class="form-group">
                        <label for="txt_departmentlevel">修改属性名称:</label>
                        <input type="text" name="edit_field_Name" class="form-control" id="edit_field_Name_id" placeholder="属性值名称" readonly= "true">
                        <input type="text" name="edit_field_Name" class="form-control" id="edit_field_Name_submit_id" placeholder="字段说明" readonly= "true" >
                        <label for="txt_departmentlevel">修改属性值:</label>
                        <input type="text" name="edit_field_Value" class="form-control" id="edit_field_Value_id" placeholder="属性值修改" >
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
                    <button type="button" id="btn_submit" class="btn btn-primary" data-dismiss="modal" onclick="saveEditDicFieldByProperty()"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="myModal_dicField" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel_dicField">修改</h4>
                </div>
                <div class="modal-body">

                    <div class="form-group">
                        <label for="txt_departmentname">编码</label>
                        <input type="text" name="dicFieldId"  class="form-control" id="dicFieldId" readonly= "true">
                    </div>
                    <div class="form-group">
                        <label for="txt_parentdepartment">字段名称</label>
                        <input type="text" name="FieldName" class="form-control" id="FieldName" placeholder="库名">
                    </div>
                    <div class="form-group">
                        <label for="txt_departmentlevel">字段说明</label>
                        <input type="text" name="DisplayName" class="form-control" id="DisplayName" placeholder="表名">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
                    <button type="button" id="btn_submit" class="btn btn-primary" data-dismiss="modal" onclick="saveEditDicField()"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
                </div>
            </div>
        </div>
    </div>
    
<div id="hiddenInfo" style="display: none">
	<input type="text" name="hid_dicDbId" id="hid_dicDbId" />
	<input type="text" name="hid_fieldvalueId" id="hid_fieldvalueId" />
	<input type="text" name="hid_valueId" id="hid_valueId" />
	<input type="text" name="hid_dicDataBaseId" id="hid_dicDataBaseId" />
</div>	
</body>
<script type="text/javascript">  
      function initTable(flagDicDb) {
        //先销毁表格
        $('#cusTable').bootstrapTable('destroy');
        //初始化表格,动态从服务器加载数据
        $("#cusTable").bootstrapTable({
            url: "/hssp/configTableController/getConfigInfo", //获取数据的Servlet地址
         // method: "POST",  //使用get请求到服务器获取数据
         // contentType: "application/x-www-form-urlencoded",//post请求的话就加上这个句话
         
            toolbar: '#toolbarDicDb',                //工具按钮用哪个容器
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
                  grade : 1,
                  flagDicDb:flagDicDb
              };  
              return param;                 
            },
           
            formatLoadingMessage: function () {
                return "请稍等，正在加载中...";
            },
            formatNoMatches: function () {  //没有匹配的结果
                return '无符合条件的记录';
            },            
                  columns: [
                          {
                            title: '编码',
                              field: 'Id',
                              align: 'center',
                              valign: 'middle'
                          }, 
                          {
                              title: '库名',
                              field: 'CatalogName',
                              align: 'center',
                              valign: 'middle',
                          }, 
                          {
                              title: '表名',
                              field: 'CollectionName',
                              align: 'center'
                          },
                          {
                              title: '描述',
                              field: 'Description',
                              align: 'center'
                          },
                          {
                              title: '排序',
                              field: 'Order_field',
                              align: 'center'
                          },
                          {
                              title: '操作',
                              field: 'Id',
                              
                              align: 'center',
                              formatter:function(value,row,index){  
                          
                           if(row.IsDeleted=="0"){
                        	   var a = '<a href="#" mce_href="#" onclick="getDicField(\''+ row.Id +'\')">查看详情</a> ';   	  
                               var e = '<a href="#" mce_href="#" onclick="edit('+ row.Id + ',\'' +  row.CatalogName + '\',\'' +  row.CollectionName +'\',\'' +  row.Description +'\')"  >修改</a> ';  
                        	   var d = '<a href="#" mce_href="#" onclick="delDicDb(\''+ row.Id +'\')">删除</a> '; 
                        	   return a+e+d; 
                           }else{
                        	   var d = '<a href="#" mce_href="#" onclick="reBackDicDb(\''+ row.Id +'\')">恢复</a> '; 
                        	   return d; 
                           }
                            
                                
                            } 
                          }
                      ],
                      responseHandler: function(data){
                          return data.rows;
                  }
             
          });
      }
      
      
      
      
      function initDicFieldTable(id,flagDicfield) {
          //先销毁表格
          $('#dicFieldTable').bootstrapTable('destroy');
          //初始化表格,动态从服务器加载数据
          $("#dicFieldTable").bootstrapTable({
              url: "/hssp/configTableController/getDicFileds", //获取数据的Servlet地址
              //method: "POST",  //使用post请求到服务器获取数据
              //contentType: "application/x-www-form-urlencoded",//post请求的话就加上这个句话
             toolbar: '#toolbarDicField',                //工具按钮用哪个容器
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
                    Id : id,
                    flagDicfield:flagDicfield
                };  
                return param;                 
              },
             
              formatLoadingMessage: function () {
                  return "请稍等，正在加载中...";
              },
              formatNoMatches: function () {  //没有匹配的结果
                  return '无符合条件的记录';
              },            
                    columns: [
                            {
                              title: '编码',
                                field: 'Id',
                                align: 'center',
                                valign: 'middle'
                            }, 
                            {
                                title: '字段名称',
                                field: 'FieldName',
                                align: 'center',
                                valign: 'middle',
                                formatter:function(value,row,index){  
                                	if(row.FieldName == null){
                                		row.FieldName = "-";
                                	}
                                	if(row.IsDeleted==0){
                                        var e = "<a href=\"#\" mce_href=\"#\" onclick=\"editDicFieldByProperty("+ row.Id + ",'" +  row.FieldName + "','" +  row.DisplayName+ "','字段名称','FieldName','" +  row.FieldName +"\')\"  >"+row.FieldName+"</a> ";  
                                 	    return e; 
                                    }else{
	                             	   	return row.FieldName; 
                                    }
                                }
                            }, 
                            {
                                title: '字段说明',
                                field: 'DisplayName',
                                align: 'center',
                                formatter:function(value,row,index){  
                                	if(row.DisplayName == null){
                                		row.DisplayName ="-";
                                	}
                                	if(row.IsDeleted==0){
                                        var e = "<a href=\"#\" mce_href=\"#\" onclick=\"editDicFieldByProperty("+ row.Id + ",'" +  row.FieldName + "','" +  row.DisplayName+ "','字段说明','DisplayName','" +  row.DisplayName +"\')\"  >"+row.DisplayName+"</a> ";  
                                 	    return e; 
                                    }else{
	                                	return row.DisplayName; 
                                    }
                                }
                            },
                            {
                                title: '排序',
                                field: 'Order_field',
                                align: 'center',
                                formatter:function(value,row,index){ 
                                	if(row.Order_field == null){
                                		row.Order_field = "-";
                                	}
                                	if(row.IsDeleted==0){
                                        var e = "<a href=\"#\" mce_href=\"#\" onclick=\"editDicFieldByProperty("+ row.Id + ",'" +  row.FieldName + "','" +  row.DisplayName+ "','排序','Order_field','" +  row.Order_field +"\')\"  >"+row.Order_field+"</a> ";  
                                 	    return e; 
                                    }else{
	                                	return row.Order_field; 
                                    }
                                }
                            },
                            {
                                title: '脱敏开始',
                                field: 'SensitiveFilterStart',
                                align: 'center',
                                formatter:function(value,row,index){  
                                	if(row.IsDeleted==0){
                                        var e = "<a href=\"#\" mce_href=\"#\" onclick=\"editDicFieldByProperty("+ row.Id + ",'" +  row.FieldName + "','" +  row.DisplayName+ "','脱敏开始','SensitiveFilterStart','" +  row.SensitiveFilterStart +"\')\"  >"+row.SensitiveFilterStart+"</a> ";  
                                 	    return e; 
                                    }else{
                             	   		return row.SensitiveFilterStart; 
                                    }
                                }
                            },{
                                title: '脱敏开始',
                                field: 'SensitiveFilterEnd',
                                align: 'center',
                                formatter:function(value,row,index){  
                                	if(row.IsDeleted==0){
                                        var e = "<a href=\"#\" mce_href=\"#\" onclick=\"editDicFieldByProperty("+ row.Id + ",'" +  row.FieldName + "','" +  row.DisplayName+ "','脱敏开始','SensitiveFilterEnd','" +  row.SensitiveFilterEnd +"\')\"  >"+row.SensitiveFilterEnd+"</a> ";  
                                 	    return e; 
                                    }else{
                             	   		return row.SensitiveFilterEnd; 
                                    }
                                }
                            },
                            {
                                title: '操作',
                                field: 'Id',
                                align: 'center',
                                formatter:function(value,row,index){  
                           	      if(row.IsDeleted==0){
                                   // var e = '<a href="#" mce_href="#" onclick="editDicField('+ row.Id + ',\'' +  row.FieldName + '\',\'' +  row.DisplayName +'\')"  >修改</a> ';  
                               	    var d = '<a href="#" mce_href="#" onclick="delDicField(\''+ row.Id +'\')">删除</a> '; 
                               	    return d; 
                                 }else{
                              	   	var d = '<a href="#" mce_href="#" onclick="reBackDicField(\''+ row.Id +'\')">恢复</a> '; 
                           	   		return d; 
                                 }
                              } 
                            }
                        ],
                        responseHandler: function(data){
                            return data.rows;
                    }
               
            });
        }
      
      
      
      
      function initAddTable(flagDicDb) {
          //先销毁表格
          $('#cusAddTable').bootstrapTable('destroy');
          //初始化表格,动态从服务器加载数据
          $("#cusAddTable").bootstrapTable({
              url: "/hssp/configTableController/getAllSolrConfig", //获取数据的Servlet地址
           // method: "POST",  //使用get请求到服务器获取数据
           // contentType: "application/x-www-form-urlencoded",//post请求的话就加上这个句话
           
              toolbar: '#toolbarDicAddDb',                //工具按钮用哪个容器
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
                    grade : 1,
                    flagDicDb:flagDicDb
                };  
                return param;                 
              },
             
              formatLoadingMessage: function () {
                  return "请稍等，正在加载中...";
              },
              formatNoMatches: function () {  //没有匹配的结果
                  return '无符合条件的记录';
              },            
                    columns: [
                  	    {field:"选择",checkbox: true,title:"选择",name:"mybox",value:'Id',align:"center",valign:"middle"},
                            {
                              title: '编码',
                                field: 'Id',
                                align: 'center',
                                valign: 'middle'
                            }, 
                            {
                                title: '数据库表名称',
                                field: 'tableName',
                                align: 'center',
                                valign: 'middle',
                            }, 
                            {
                                title: '数据库名称',
                                field: 'dataBaseName',
                                align: 'center'
                            },
                            {
                                title: 'solr的数据文件名字',
                                field: 'coreName',
                                align: 'center'
                            }
                            
                        ],
                        responseHandler: function(data){
                            return data.rows;
                    }
               
            });
        }
        
      
      
      
      
      
 
      $(document).ready(function () {        
          //调用函数，初始化表格
          var flagDicDb = 0
          
          initTable(flagDicDb);
    	  document.getElementById('solrConfig_div').style.display='none';
          document.getElementById('dicField_div').style.display='none';
          //当点击查询按钮的时候执行
          $("#search").bind("click", initTable);
      });
      
      
      function edit(id,CatalogName,CollectionName,Description){
    	  document.getElementById("Id").value=id;  
    	  document.getElementById("CatalogName").value=CatalogName;  
    	  document.getElementById("CollectionName").value=CollectionName;  
    	  document.getElementById("Description").value=Description;  
  	     $("#myModalLabel_dicDb").text("修改");
  	     $('#myModal_dicDb').modal();
      }
      
      function saveEdit(){
    	  var id = document.getElementById("Id").value;  
    	  var CatalogName =document.getElementById("CatalogName").value;  
    	  var CollectionName= document.getElementById("CollectionName").value;  
    	  var Description=  document.getElementById("Description").value;  
    	  
    	  var datas = {
    			    id:id,
    			    CatalogName:CatalogName,
    			    CollectionName:CollectionName,
    			    Description:Description
    		};
    	  var url = "/hssp/configTableController/updateConfigInfo";
    	    $.ajax({
    	        type: "post",
    	        url:url,
    	        data:datas,
    	        cache: false,
    	        async : false,
    	        dataType: "text",
    	        success: function (data)
    	        {
    	            if("succ"==data){
    	             //  alert("修改成功！");
    	                return true;
    	            }else{
    	             //   alert("修改失败");
    	                return false;
    	            }
    	        }
    	     });
    	    
    	    $("#cusTable").bootstrapTable('refresh',{url: '/hssp/configTableController/getConfigInfo?grade=1'});
      }
      
      function  getDicField(id) {
    	  $("#hid_dicDbId").val(id);
    	  
    	  document.getElementById('showDicFieldHs').style.display='block';
    	  //document.getElementById("hid_userId").value=id;
    	  var  flagDicfield =0;
    	  initDicFieldTable(id,flagDicfield);
    	  document.getElementById('showDicAddDb').style.display='none';
    	  document.getElementById('solrConfig_div').style.display='none';
    	  document.getElementById('dicDb_div').style.display='none';
    	  document.getElementById('dicField_div').style.display='block';
    	  document.getElementById('showDicFieldBack').style.display='none';
	  	 
	}
      
      function delDicDb(id){
    	  var datas = {
    			  dicfield:id,
    			  isReBack:1
     		};
     		  $.ajax({
     		        type:"post",
     		     	url:"/hssp/configTableController/deleteDicDb",
     		        dataType:"text",
     		        data:datas,
     		        cache: false,
       	            async : false,
     		        success:function (msg) {
     		        	//alert(msg);
     		        	
     		        }
     		    });
     		 $("#cusTable").bootstrapTable('refresh',{url: '/hssp/configTableController/getConfigInfo?grade=1'});
      }
      
      function showDicDbHs(){
    	  
    	  document.getElementById('showDicAddDb').style.display='none';
    	  document.getElementById('solrConfig_div').style.display='none';
    	  document.getElementById('showDicDbHs').style.display='none';
    	  document.getElementById('showDicDbBack').style.display='block';
    	  var flagDicDb=1;
    	  initTable(flagDicDb);
      }
      function showDicFieldHs(){
    	  
    	  document.getElementById('showDicDb').style.display='none';
    	  document.getElementById('showDicAddDb').style.display='none';
    	  document.getElementById('solrConfig_div').style.display='none';
    	  document.getElementById('showDicFieldHs').style.display='none';
    	  document.getElementById('showDicFieldBack').style.display='block';
    	  var dicDbId =  $("#hid_dicDbId").val();
    	  var flagDicfield=1;
    	  initDicFieldTable(dicDbId,flagDicfield);
      }
      function reBackDicDb(id){
    	  var datas = {
    			  dicfield:id,
    			  isReBack:0
     		};
     		  $.ajax({
     		        type:"post",
     		     	url:"/hssp/configTableController/deleteDicDb",
     		        dataType:"text",
     		        data:datas,
     		        cache: false,
       	            async : false,
     		        success:function (msg) {
     		        	//alert(msg);
     		        	
     		        }
     		    });
     		 $("#cusTable").bootstrapTable('refresh',{url: '/hssp/configTableController/getConfigInfo?grade=1'});
      }
      
      function showDicDb(){
    	  
    	  document.getElementById('showDicAddDb').style.display='block';
    	  document.getElementById('showDicDbHs').style.display='block';
    	  document.getElementById('solrConfig_div').style.display='none';
    	  document.getElementById('showDicDbBack').style.display='none';
    	  var flagDicDb = 0
          initTable(flagDicDb);
    	  document.getElementById('dicField_div').style.display='none';
    	  document.getElementById('dicDb_div').style.display='block';
      }
      
      
      function editDicField(id,FieldName,DisplayName){
    	  $("#dicFieldId").val(id);  
    	  $("#FieldName").val(FieldName);  
    	  $("#DisplayName").val(DisplayName);  
  	      $("#myModalLabel_dicField").text("修改");
  	      $('#myModal_dicField').modal();
      }
      /*
                             属性值修改
      */
      function editDicFieldByProperty(id,FieldName,DisplayName,field_Name_des,field_Name,field_value){
    	  $("#Dic_Field_Edit_dicFieldId").val(id);  
    	  $("#Dic_Field_Edit_FieldName").val(FieldName);  
    	  $("#Dic_Field_Edit_DisplayName").val(DisplayName); 
    	  $("#edit_field_Name_id").val(field_Name_des);  
    	  $("#edit_field_Name_submit_id").val(field_Name);  
    	  if(field_value != "-"){
    		  $("#edit_field_Value_id").val(field_value); 
    	  }else{
    		  $("#edit_field_Value_id").val(""); 
    	  } 
    	  $("#myModal_Dic_Field_Edit_title").text("属性值修改");
  	      $('#myModal_Dic_Field_Edit').modal();
      }
      
      function  saveEditDicFieldByProperty(){
    	  var dbId = $("#hid_dicDbId").val();
    	  var dicFieldId =document.getElementById("Dic_Field_Edit_dicFieldId").value;  
    	  var field_Name =document.getElementById("edit_field_Name_submit_id").value;  
    	  var field_value= document.getElementById("edit_field_Value_id").value;  
    	  
    	  var datas = {
    			    id:dicFieldId,
    			    fieldName:field_Name,
    			    fieldValue:field_value
    		};
    	  var url = "/hssp/configTableController/updateDicFiledByProperty";
    	    $.ajax({
    	        type: "post",
    	        url:url,
    	        data:datas,
    	        cache: false,
    	        async : false,
    	        dataType: "text",
    	        success: function (data)
    	        {
    	            if("succ"==data){
    	             //  alert("修改成功！");
    	                return true;
    	            }else{
    	             //   alert("修改失败");
    	                return false;
    	            }
    	        }
    	     });
    	    
    	    $("#dicFieldTable").bootstrapTable('refresh',{url: '/hssp/configTableController/getDicFileds?Id='+dbId});
      }
      
      function  saveEditDicField(id){
    	  var dbId = $("#hid_dicDbId").val();
    	  var dicFieldId =document.getElementById("dicFieldId").value;  
    	  var FieldName =document.getElementById("FieldName").value;  
    	  var DisplayName= document.getElementById("DisplayName").value;  
    	  
    	  var datas = {
    			    id:dicFieldId,
    			    fieldName:FieldName,
    			    displayName:DisplayName
    		};
    	  var url = "/hssp/configTableController/updateDicFiled";
    	    $.ajax({
    	        type: "post",
    	        url:url,
    	        data:datas,
    	        cache: false,
    	        async : false,
    	        dataType: "text",
    	        success: function (data)
    	        {
    	            if("succ"==data){
    	             //  alert("修改成功！");
    	                return true;
    	            }else{
    	             //   alert("修改失败");
    	                return false;
    	            }
    	        }
    	     });
    	    
    	    $("#dicFieldTable").bootstrapTable('refresh',{url: '/hssp/configTableController/getDicFileds?Id='+dbId});
      }
      
      
      function delDicField(id){
    	  var dbId = $("#hid_dicDbId").val();
    	  
    	  var datas = {
    			  dicfield:id,
    			  isReBack:0
    		};
    	  var url = "/hssp/configTableController/deleteDicField";
    	    $.ajax({
    	        type: "post",
    	        url:url,
    	        data:datas,
    	        cache: false,
    	        async : false,
    	        dataType: "text",
    	        success: function (data)
    	        {
    	            if("succ"==data){
    	             //  alert("修改成功！");
    	                return true;
    	            }else{
    	             //   alert("修改失败");
    	                return false;
    	            }
    	        }
    	     });
    	    
    	    $("#dicFieldTable").bootstrapTable('refresh',{url: '/hssp/configTableController/getDicFileds?Id='+dbId});
      }
      
      function reBackDicField(id){
    	  var dicDbId =  $("#hid_dicDbId").val();
    	  var flagDicfield=1;
    	  var datas = {
    			  dicfield:id,
    			  isReBack:1
     		};
     		  $.ajax({
     		        type:"post",
     		     	url:"/hssp/configTableController/deleteDicField",
     		        dataType:"text",
     		        data:datas,
     		        cache: false,
       	            async : false,
     		        success:function (msg) {
     		        	//alert(msg);
     		        	
     		        }
     		    });
     		 $("#dicFieldTable").bootstrapTable('refresh',{url: '/hssp/configTableController/getDicFileds?Id='+dicDbId+"&flagDicfield="+flagDicfield});

      }
      
      function showDicFieldBack(){
         var id=$("#hid_dicDbId").val();
         
         document.getElementById('showDicDb').style.display='block';
         document.getElementById('showDicFieldHs').style.display='block';
         document.getElementById('showDicFieldBack').style.display='none';
    	  //document.getElementById("hid_userId").value=id;
    	  var  flagDicfield =0;
    	  initDicFieldTable(id,flagDicfield);
    	  document.getElementById('showDicAddDb').style.display='none';
    	  document.getElementById('solrConfig_div').style.display='none';
    	  document.getElementById('dicDb_div').style.display='none';
    	  document.getElementById('dicField_div').style.display='block';
    	  
    	  
      }
      
      
      function showDicAddDb(){
    	  document.getElementById('solrConfig_div').style.display='block';
    	  document.getElementById('showDicDbHs').style.display='none';
    	  document.getElementById('dicDb_div').style.display='none';
    	  var flagDicDb=0;
    	  initAddTable(flagDicDb);
      }
      
      function addTable(){
    	  var a= $("#cusAddTable").bootstrapTable('getSelections');  
          if(a.length<=0){  
              alert("请选中一行");
              return;
          } 
          var idItem="";
          for(var i= 0;i<a.length;i++){
        	  idItem+=a[i].Id+",";
          }
          idItem= idItem.substr(0,idItem.length-1);
        	  $.ajax({
        			type : "POST",
        			url : "/hssp/configTableController/addTable",
        			dataType: "text",  
        			data: {"idItem":idItem},  
        			statusCode : {
        				404 : function() {
        					alert('page not found');
        				}
        			},
        			success : function(msg) {
        				alert(msg);
        			}
        		});
          }       
      
</script>

</html>