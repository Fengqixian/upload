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

<div id="solrConfig_div"  style=" height:100%; width:100%; overflow:auto; float:left;">
		<div id="toolbarDicDb" class="btn-group">
            <button id="showDicDbHs" type="button" class="btn btn-primary" onclick="addTable()">
                <span class="glyphicon glyphicon-plus" aria-hidden="true" "></span>添加表
            </button>
            
        </div>
			<table  id="cusTable">
			 <thead>
		       <th>选择</th>
		       </thead>
		       <tbody>
		        
		       </tbody>
			</table>
	</div>
    
</body>
<script type="text/javascript">  
      function initTable(flagDicDb) {
        //先销毁表格
        $('#cusTable').bootstrapTable('destroy');
        //初始化表格,动态从服务器加载数据
        $("#cusTable").bootstrapTable({
            url: "/hssp/configTableController/getAllSolrConfig", //获取数据的Servlet地址
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
            search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
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
          initTable();
          //当点击查询按钮的时候执行
          $("#search").bind("click", initTable);
      });
      
   
      function addTable(){
    	  var a= $("#cusTable").bootstrapTable('getSelections');  
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