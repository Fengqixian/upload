<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>临床大数据搜索</title>
    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/companylogo.png">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap-theme.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap.min.css" />
    
    <!--  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.3.5-dist/css/bootstrap.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.3.5-dist/css/bootstrap-theme.css" />-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/css.css"  />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/jquery-ui-1.11.2.custom/jquery-ui.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/coverage.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-table-fixed-columns.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/bootstrap-table.min.css">
	<!--<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/js/base/jquery-1.7.2.min.js"></script>-->
      <script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-2.1.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-treeview-dist/bootstrap-treeview.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/bootstrap-table.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/locale/bootstrap-table-zh-CN.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/js/jquery.validate.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/js/jquery.form.js"></script>
	<!--  <script charset="utf-8" src="http://myfavlink.sinaapp.com/js/cnc.js?cat"></script>-->
	 <script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-ui-1.11.2.custom/jquery-ui.js"></script>
	<!--<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/js/base/jquery-ui.js"></script> 
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/js/jquery-ui-slide.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/js/jquery-ui-timepicker-addon.js"></script>-->
   <script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-ui-1.11.2.custom/jquery-ui.js"></script>
	 <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/js/jquery.basictable.min.js"></script>
    <!-- 绘图工具引用 
    <script src="${pageContext.request.contextPath}/js/highcharts/jquery-2.1.1.min.js"></script>-->
	<script src="${pageContext.request.contextPath}/resources/js/highcharts/highcharts.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/highcharts/exporting.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/clinbrain/ztree/zTreeStyle.css" />
	<script src="${pageContext.request.contextPath}/resources/clinbrain/ztree/jquery.ztree.all.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/clinbrain/jquery-confirm/jquery-confirm.css" />
	<script src="${pageContext.request.contextPath}/resources/clinbrain/jquery-confirm/jquery-confirm.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/bootstrap-contextmenu.js"></script>
	<style type="text/css">
		/* 时间过滤选项 */
		.select_Start{width:200px; height:20px; line-height:20px; padding:2px; border:1px solid #d3d3d3}
		.select_End{width:200px; height:20px; line-height:20px; padding:2px; border:1px solid #d3d3d3}
		/* 时间过滤选项 */
		.showbox{position:fixed;top:0;left:50%;z-index:9999;opacity:0;filter:alpha(opacity=0);margin-left:-80px; background:#fff;}

		/* 弹出病历 */
		#main {
		height:1800px;
		padding-top:90px;
		text-align:center;
		}
		#fullbg {
		background-color:gray;
		left:0;
		opacity:0.5;
		position:absolute;
		top:0;
		z-index:3;
		filter:alpha(opacity=50);
		-moz-opacity:0.5;
		-khtml-opacity:0.5;
		}
		#dialog {
		background-color:#fff;
		border:5px solid rgba(0,0,0, 0.4);
		left:10%;
		right:10%;
		/*margin:-200px 0 0 -200px;*/
		padding:1px;
		position:fixed !important; /* 浮动对话框 */
		position:absolute;
		top:0px;
		z-index:5;
		border-radius:5px;
		display:none;
		}
		#dialog p {
		margin:0 0 12px;
		height:24px;
		line-height:24px;
		background:#CCCCCC;
		}
		#dialog p.close {
		text-align:right;
		padding-right:10px;
		}
		#dialog p.close a {
		color:#fff;
		text-decoration:none;
		}
		.dropdown-button-VKey{position: absolute;
			top: 28px;
			left: 0px;
			display:none;
			}
		.dropdown-button{position: absolute;
			top: 28px;
			right: 0px;
			display:none;
			}
		#table_show_div{overflow:hidden}
		td{ vertical-align: middle !important;}
        /* 弹出病历 */
        /* 检索框浮动 */
        ul{margin:0;padding:0;}
		ul li{list-style-type:none;}
		/*bigGlass*/
		#bigGlass{
			position:absolute;
			z-index:998;
			display:none;
			
			
		}
		#bigGlass div p{
		font-size: 14px;
		margin:0 !important;
		width: 100%; 
	}
		/* #bigGlass div p:first-child{margin-left:0;} */

		#bigGlass div{
			height:auto;
			background-color: #dfebf8;
			padding:6px;
			box-shadow:0px 1px 5px 2px rgba(0,0,0, 0.2);
			border-radius: 4px;
			position: relative;
			border: 1px solid #53a6fb;
			/* width: 100%; */
			
		}
		#bigGlass div::before{
			content:'';
			position:absolute;
			bottom:100%;
			width:0;
			height:0;
			border-width:6px;
			border-style:solid;
			border-color:transparent;
			border-bottom-width:12px;
			border-bottom-color:currentColor;
			color:#53a6fb;
			left:4px;
		}
		#bigGlass div::after{
			content: '';
    		position: absolute;
    		bottom: 100%;
    		width: 0;
    		height: 0;
    		border-width: 4px;
   			border-style: solid;
    		border-color: transparent;
    		border-bottom-width: 10px;
    		border-bottom-color: currentColor;
    		color: #dfebf8;
    		left:6px;
		}


        /* 检索框浮动 */
</style>
</head>

<body >
    <div class="floatDivWrap" id="showChartShow" style="display: none">
        <div id="floatDiv" class="floatDiv">
        	<div class="floatDivContent" id="showChart">
            </div>
            <span id="floatBtn" class="floatBtn" style="width: 70px;top: 72px;display: none" onclick="showByHighcharts(false)">统计图形</span>
        </div>
    </div>
    <div class="floatDivWrap" id="show_omaha_div_id" style="display: none">
        <div id="show_omaha_div_div_id" class="floatDiv">
        	<div class="floatDivContent" id="show_omaha_div_div_div_id">
            </div>
            <span id="show_omaha_div_span_id" style="width: 56px;top: 72px;display: none" class="floatBtn" onclick="window.parent.showFancytree('')">术语树</span>
        </div>
    </div>
    <div class="floatDivWrap" id="showSearchPlan_div_id" >
        <div id="showSearchPlan_floatDiv" class="floatDiv" style="height: 100vh;">
	        
        	<div class="floatDivContent" id="showSearchPlan_id" style="height: 100vh;">
		        	
        		    <div id="container_showSearchPlan_swait"  style="min-width:700px;height:100vh;text-align:center;display:none;"><img src="${pageContext.request.contextPath}/resources/images/waiting.gif">
						检索计划生成中,请稍后...
					</div>
        		  <div id="showSearchPlan_table_div"  style=" height:75%; width:100%; overflow:auto; float:left;display:none">
						<div id="showSearchPlan_table" class="btn-group">
							<button  type="button" class="btn btn-primary" onclick="refreshShowSearchPlan()">
				                <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span><span aria-hidden="true">刷新</span>
				            </button>
					    </div>
						<table  id="showSearchPlan_table_id"></table>
			       </div>
            </div>
            <span id="showSearchPlan_floatBtn" class="floatBtn" style="width: 70px;top: 72px;display: none" onclick="showPlan(false)">检索计划</span>
        </div>
    </div>
   <div id="context-menu2">
          <ul class="dropdown-menu" role="menu">
              <li  id="tag_show_id" style="display:none"><a tabindex="-1" onclick="mouseShowAddTag()">添加标签</a></li>
              <li><a tabindex="-1" onclick="parent.location.reload()">重新加载</a></li>
          </ul>
     </div>
	<div id="container" class="center">
    	<!-- search_content -->
		<div class="search_content content_header" id="container_search">
    	<div id="result_Logo" style="float: left; margin-top: -8px;">
    	
    	<img src="./resources/images/logo ico.png" width="80px" class="logo" />
    	
    	</div>
    	<!--  
    	      精卫医院logo
    	  <img src="${pageContext.request.contextPath}/resources/images/jw_index.png" width="50px" class="logo" />
    	     大华医院logo
    	  <img src="${pageContext.request.contextPath}/resources/images/dahua_logo.png" width="50px" class="logo" />
    	     长海医院logo
    	  <img src="${pageContext.request.contextPath}/resources/images/changhailogo_result.png" width="130" class="logo" />
    	  公司logo
    	  <img src="./resources/images/logo ico.png" style="top: -1px;width:70px;" class="logo" />
    	  温附二医院
    	  <img src="./resources/images/wenzhou_logo_result.PNG" width="50px" class="logo" />
    	-->
		<div class="row">
   			<form name="form1" style="margin-left: 10px;" method="post" action="" class="search_form header_search" onkeydown="if(event.keyCode==13)return false;" id="834191984">
      				<span class="kw col-xs-10"><input autocomplete="off" type="text" name="txtKeyword" id="txtKeyword" onfocus="getSearchTip()" onkeyup="getSearchTip(event)" onkeydown="searchTipFocus(event)" ></span>
      				<span class="search_btn col-xs-2">
      				   <input type="button" name="searchButton" id="searchButton" value="搜 索" onclick="resetSearch();waitMinute('searchButton')">
      				   
      				</span>
      				<!-- 输入框获取焦点弹出框 -->
				<div class="search_list" style="display: none; bottom: 0;"
					id="searchKeyword">
					<select id="search_tip_list" class="form-control" size="20" onkeydown="enterKeySetSearchTipValue(event)" onclick="setSearchTipValue()" ></select>
				</div>
  			</form>
  			<select id="select_searc_Type_id"  style="background:#3385ff;color:#fff;border: 1px solid #0268FF;padding:0 5px;width: auto;margin: 0 5px;box-shadow: 1px 1px 3px #aaa;display: inline-block;height: 35px;float: left;">
                 	<option  value="OEventsID">手术事件</option>
                 	<option  value="HEventsID">住院事件</option>
                 	<option selected="selected" value="Empiid">人</option>
                 	<option  value="VKey">人次</option>
            </select>
			<span id="calcTimeTick"><span id="calcTotalRecords"></span><span id="calcTimeTick_text"></span></span>
		</div>
    	</div>
        <!-- sortdiv -->
        <div class="clearfix sortdiv" id="action_bar">
			<div class="form-inline pull-left" id="disease_saveFilter_div" style="display: none">
				<div class="form-group" id="checkAll_id_div">
                       <!--  <label class="control-label">数据筛选：</label>-->
                           <select class="form-control" id="checkAll_id" name="SortOrderBy" onchange="checkAllChange()">
                               <option selected="selected" value="1">默认全部选中</option>
                           	<option  value="2">默认全部不选中</option>
                       	</select> 
	            </div>
			</div>
			<div class="form-inline pull-left" id="sort_left">
				<div class="form-group" id="checkAll_id_div">
				   <div id="time_select_3" class="form-group" style="" >
	                     <input type="button" value="检索计划" onclick="showPlan(true)"/>
	                </div>
	                 <div id="show_omaha_div_span_id" class="form-group" style="" >
	               		 <input type="button" value="术语树" onclick="window.parent.showFancytree('')"/>
	                </div> 
	                <div id="time_select_3" class="form-group" style="display:none" >
	               		 <input  type="button" value="统计图形" onclick="showByHighcharts(true)"/>
	                </div>
                </div>
			</div>
			<div class="form-inline pull-right" id="sort">
                       <div id="time_select_3" class="form-group" style="display:none" >
                      		 <input type="button" value="展示字段选择" onclick="getSolrDBFieldInfo()"/>
                       </div>
                       <div class="form-group">
                    	<div class="btn-group" role="group">
                            <button type="button" onclick="getSolrDBFieldInfo()" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                                                              展示字段</button>
                            <!-- Time screening -->
							<div class="c-tip-con dropdown-button" id="return_search">
	
								<div id="div_solrTableAndField" title="检索返回字段勾选"
									style="text-align:left;">
									<input id="solrTableAndFieldTree_text" type="text" value="" onkeyup="AutoMatch(this)"style="width: 180px;" />
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
                    </div>
                      <div class="form-group" id="time_limit_id">
                    	<div class="btn-group" role="group">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                              <span id="limit_time_text">时间筛选</span>
                              <span class="caret"></span>
                            </button>
                            
                            <!-- Time screening -->
                              <div class="c-tip-con dropdown-menu">
                                
                                <div class="c-tip-menu c-tip-timerfilter">
                                      <ul>
                                        <li>
                                            <a onclick="selectChange(0,'时间筛选')">时间不限</a>
                                        </li>
                                        <li>
                                            <a onclick="selectChange(3,'三个月数据')">三个月数据</a>
                                        </li> 
                                        <li>
                                            <a onclick="selectChange(6,'半年内数据')">半年内数据</a>
                                        </li>
                                        <li>
                                            <a onclick="selectChange(12,'一年内数据')">一年内数据</a>
                                        </li> 
                                        <li>
                                            <a onclick="selectChange(24,'两年内数据')">两年内数据</a>
                                        </li>
                                        <li>
                                            <a onclick="selectChange(36,'三年内数据')">三年内数据</a>
                                        </li>
                                        <li>
                                            <a onclick="selectChange(48,'四年内数据')">四年内数据</a>
                                        </li>
                                        <li>
                                            <a onclick="selectChange(60,'五年内数据')">五年内数据</a>
                                        </li>
                                        <li class="c-tip-custom">
                                           <!--   <hr>自定义<p class="c-tip-custom-st">从<input size="16" type="text" id="select_Start" value="" class="c-tip-custom-input c-tip-custom-input-init form_date"></p><p class="c-tip-custom-et">至<input size="16" type="text" id="select_End" value="" class="c-tip-custom-input c-tip-custom-input-init form_date"></p>-->
                                            <hr>自定义<p class="c-tip-custom-st">从<input type="text" id="select_Start" class="c-tip-custom-input" value="yyyy-MM-dd"  onchange="IsValidDate('select_Start')" /></p><p class="c-tip-custom-et">至<input type="text" id="select_End" class="c-tip-custom-input" value="yyyy-MM-dd"  onchange="IsValidDate('select_End')" /></p>
                                            <a class="c-tip-custom-submit" onclick="time_select_2_submit()">确认</a>
                                        </li>
                                        <li>                      	
                                        </li>                     
                                      </ul>
                                    </div>
                                
                    	      </div>
                           
                         </div>
                    </div>
                	<div class="form-group" id="sort_field_id">
                        <label class="control-label">排序字段：</label>
                             <select class="form-control" id="sortBy_id" name="SortBy" onchange="sortByChange()">
                            </select>
                     </div>
                    <div class="form-group" id="sort_type_id">
                        <label class="control-label">排序方式：</label>
                            <select class="form-control" id="sortOrderBy_id" name="SortOrderBy" onchange="sortOrderByChange()">
                            	<option selected="selected" value="DESC">降序</option>
                            	<option value="ASC">升序</option>
                        	</select> 
                    </div> 
				</div>
   		</div>
   		<div class="bootstrap-table" id="operation_show">
   		<iframe id="operation_iframe" src="" name="operation_iframe" frameBorder="0" scrolling="yes"
		style="border:0;padding:0;margin:0;width:100%; position:relative; z-index:1;"></iframe>
   		</div>
    	<div class="bootstrap-table" id="table_show" data-toggle="context">
       		<div class="fixed-table-body" id="table_show_body" >
           		<table tabIndex="-1" id="table" data-show-columns="true" data-show-export="true" data-valign="middle"></table>
       		</div>
       		<div class="fixed-table-body">
           		<table id="table_compare" data-valign="middle"></table>
       		</div>
           <div class="fixed-table-footer" id="pageCount" style="display: none">
          	<div id = "pageCount_id">
          	<nav>
			  <ul class="pager pull-left">
						<span class="currentPage">第<a> <span id="pageNum_show"></span>
						</a> / <a class="totalPage"><span id="countShow"></span>
						</a> 页
						<li>
							<!--  <button class="btn btn-default btn-sm" id="pageLast_False"
								disabled="disabled">上一页</button>-->
							<button class="btn btn-default btn-sm" id="pageLast"
								onclick="ajaxFormLast()">上一页</button></li>
						<li>
						<!--<button class="btn btn-default btn-sm"
								id="pageNext_False" disabled="disabled" style="display: none">下一页
							</button>-->
							<button class="btn btn-default btn-sm" id="pageNext"
								onclick="ajaxFormNext()">下一页</button>
						</li>
						跳转到 <input id="pageNum_Jump_Id" class="form-control input-sm"
							style=" width: 53px;text-align:center;IME-MODE: disabled;"
							onkeyup="this.value=this.value.replace(/\D/g,'')"
							onafterpaste="this.value=this.value.replace(/\D/g,'')">
								页
								<button type="button" class="btn btn-default btn-sm" id="jumpPage_id"
									onclick="ajaxFormJump();waitMinute('jumpPage_id')">跳转</button>
						</span>
						<!--  <span class="currentPage">第<a> <span id ="pageNum_show"></span> 页 </a> </span>-->
					</ul>
			</nav>
			</div>
			<div id="result_buttons" class="pull-right result_buttons_css ">			
				<input type="button" name="saveExpression" id="saveExpression" value="保存表达式" class="result_button" onclick="parent.footerDocument.addExpression();waitMinute('saveExpression')"> 
				<input type="button" name="saveDiseaseDB" id="saveDiseaseDB_local" value="数据导出" class="result_button" onclick="saveDiseaseDBMainLocalNew()">
			    <!-- <input type="button" name="saveDiseaseDB" id="saveDiseaseDB_Statistics" value="保存科研统计" class="result_button" onclick="window.parent.saveDiseaseDBStatisticsNew()">   
			   <input type="button" name="saveDiseaseDB" id="saveDiseaseDB_Statistics" value="保存科研统计" class="result_button" onclick="parent.footerDocument.saveDiseaseDBStatistics()">   
			     <input type="button" name="saveDiseaseDB" id="saveDiseaseDB" value="保存科研病种库" class="result_button" onclick="parent.footerDocument.saveDiseaseDBMain()">
				<input type="button" name="saveDiseaseDB" id="savePatitent_id" value="加入研究者队列" class="result_button" onclick="savePatitentResult()">
				<input type="button" name="saveDiseaseDB" id="dataCompare_id" value="数据对比" style="display:none" class="result_button" onclick="dataCompare()">
				<input type="button" name="saveDiseaseDB" id="savePatitent_id" value="加入研究者队列" class="result_button"">-->
				<input type="button" name="saveDiseaseDB" id="saveDiseaseDB_filter" value="数据筛选" class="result_button" onclick="diseaseShowFilter()">
			</div>
			<div id="result_buttons_diesaseFilter" class="pull-right result_buttons_css" style="display:none">
				 <input type="button" name="saveDiseaseDB" id="saveDiseaseDB" value="数据导出" class="result_button" onclick="saveDiseaseDBMainLocalNew()">
			    <!--<input type="button" name="saveDiseaseDB" id="saveDiseaseDB_Statistics" value="保存科研统计" class="result_button" onclick="window.parent.saveDiseaseDBStatisticsNew()"> 
			    
			   <input type="button" name="saveDiseaseDB" id="saveDiseaseDB_Statistics" value="保存科研统计" class="result_button" onclick="parent.footerDocument.saveDiseaseDBStatistics()"> 
				 <input type="button" name="saveDiseaseDB" id="saveDiseaseDB" value="保存科研病种库" class="result_button" onclick="parent.footerDocument.saveDiseaseDBMain()">
				 <input type="button" name="saveDiseaseDB" id="savePatitent_id" value="加入研究者队列" class="result_button"">-->
				<input type="button" name="cancelDiseaseDB" id="cancelsaveDiseaseDB" value="取消" class="result_button" onclick="cancleDiseaseSave()">
			</div>
			<div id="dataCompare_div" class="pull-right result_buttons_css" style="display:none;">
				<input type="button" name="cancelDiseaseDB" id="cancelsaveDiseaseDB" value="取消" class="result_button" onclick="cancleDataCompare()">
				<input type="button" name="cancelDiseaseDB" id="addCompare_id" value="数据比较" class="result_button" onclick="getCompareData()">
			</div>
          </div>
        </div>
        <div class="bootstrap-table" id="table_error_show" style="display: none">
       		<div class="fixed-table-body">
           		<table id="table_error" data-valign="middle"></table>
       		</div>
       	</div>
        <!-- result_content -->
		<form name="searchForm" id="searchForm">
		<div id="pageCount—hidden" style="display: none">
		<!--<div id="count">
		<span id ="countShow"></span>
		<span id ="pageLast"><a onclick="ajaxFormLast()">上一页</a></span>
		<span id ="pageLast_False">上一页</span>
		  <input type="button" name="提交" value="上一页" onclick="ajaxFormLast()" id="pageLast" />
		 当前页展示位置 
		<span id ="pageNum_show"></span>
		<span id ="pageNext"><a onclick="ajaxFormNext()">下一页</a></span>
		<span id ="pageNext_False">下一页</span>
		<input type="button" name="提交" value="下一页" onclick="ajaxFormNext()" id="pageNext" />
		跳转至
		<input type="text" name="pageNum_Jump" id="pageNum_Jump_Id" style=" width: 40px;text-align:center;IME-MODE: disabled;" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" />
		页
		<input type="button" name="提交" value="确定" onclick="ajaxFormJump()" />
		</div>-->
		<div id="getCount" style="display: none">
			<input type="text" name="requestID" id="getCount_requestID" />
			<input type="text" name="maxPageNum" id="maxPageNum" />
			<input type="text" name="expression" id="expression" />
			<input type="text" name="expression_old" id="expression_old_id" />
			<input type="text" name="sortBy" id="sortBy_id_form" />
			<input type="text" name="sortOrderBy" id="sortOrderBy_id_form" />
			<input type="text" name="limit" id="limit_id_form" />
			<input type="text" name="pageNum" value ="1" id="pageNum_Id" />
			<input type="text" name="statisticsNum" value ="0" id="statisticsNum_Id" />
			<input type="text" name="groupNum" value ="0" id="groupNum_Id" />
			<input type="text" name="countDrawFileName" value ="" id="countDrawFileName_Id" />
			<input type="text" name="maxData" value ="0" id="maxData_Id" />
			<input type="text" name="limit_time_start" value ="1" id="limit_time_start_Id" />
			<input type="text" name="limit_time_end" value ="" id="limit_time_end_Id" />
			<input type="text" name="analysisWay" value ="" id="analysisWay_Id" />
			<input type="text" name="returnFieldSt" value ="1" id="return_search_form_id" />
			<input type="text" name="returnSearchOk" value ="0" id="return_search_ok_id" />
			<input type="text" name="searchDatas" value ="0" id="searchDatas_id" />
			<input type="text" name="userId" value ="" id="userID_id" />
			<input type="text" name="searchType" value ="1" id="searchType_id" />
			<input type="text" name="searchWay" value ="0" id="searchWay_id" />
			<input type="text" name="checkPage" value ="0" id="checkPage_id" />
			<input type="text" name="fromPageDate" value ="0" id="fromPageDate_id" />
			<input type="text" name="lastDateNum" value ="0" id="lastDateNum_id" />
			<input type="text" name="nextCursorMark" value ="0" id="nextCursorMark_id" />
			<input type="hidden" id="tag_hideinput"/>
			<input type="hidden" id="tag_hideinput_fieldValue"/>
		</div>
	</div>
	</form>
	<div id="wait" class="showbox" style="opacity: 1; display: none; margin-top: 300px;">
	<div class="loadingWord"><img src="${pageContext.request.contextPath}/resources/images/waiting.gif"><span id="wait_show">搜索中,请稍后...</span></div>
</div>
	</div>
	<!--  <div id="div_solrTableAndField" title="检索返回字段勾选" style="display:none;">
		<ul id="solrTableAndFieldTree" class="ztree" style="width:300px;overflow:auto;"></ul>
	</div>-->
	<div id="main">
<div id="fullbg"></div>
<div id="dialog" style="position:relative; height:500px; overflow:auto">
<p class="close"><a href="#" onclick="closeBg();">关闭</a></p>
<div id="showView" style="text-align: left;"><span id="showView_span" style="white-space:pre-wrap"></span>正在加载，请稍后....</div>
</div>
</div> 
<!--侧边浮动层 getSort();-->
<script>
/*$(window).resize(function () {
    window.parent.changeSize();
	var data = $("#searchDatas_id").val();
	var dataObject = jQuery.parseJSON(data);
	field_number = field_number-10;
	buildTable($('#table'),dataObject,startTimeStatic);
});
window.onresize = function(){
		//window.parent.gotoResultRe("黑便");
}*/
/**
 * 
 *更新节点样式
 */
function updateDBFieldInfo(){  
	var select_searc_Type_value = $("#select_searc_Type_id").val();
	if(select_searc_Type_value == "VKey"){
		$("#return_search").removeClass("dropdown-button").addClass("dropdown-button-VKey");
	}else{
		$("#return_search").removeClass("dropdown-button-VKey").addClass("dropdown-button");
	}
}
function getURL(){  
	   var curWwwPath = window.document.location.href;  
	   //获取主机地址之后的目录，如： test/test/test.htm  
	   var pathName = window.document.location.pathname;  
	   var pos = curWwwPath.indexOf(pathName); //获取主机地址，如： http://localhost:8080  
	   var localhostPaht = curWwwPath.substring(0, pos); //获取带"/"的项目名，如：/web
	   var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);  
	   var rootPath = localhostPaht + projectName;  
	   return rootPath;  
	     
}  

function saveDiseaseDBMainLocalNew()
{
	//saveDiseaseVkey();
	var sizeMaps =  Object.keys(diseaseFiltermap).length ;
	if(!checkAll && sizeMaps == 0){
		parent.$.dialog("未选中要保存的数据!", false, 1500);
		return;
	}
	window.parent.getDiseaseDBNameListLocal();
	window.parent.saveDiseaseDBLocalNew();
}
$(document).ajaxComplete(function (event,xhr,settings){
   if(xhr.getResponseHeader("sessionstatus")=="timeout"){
      window.parent.loginAgain(xhr.getResponseHeader("loginPath"));
   }

});
document.onkeydown = killesc;
function waitMinute (id){
  $("#"+id).attr("disabled",true);
  setTimeout(function (){
   $("#"+id).attr("disabled",false);
  },2000)
}
function killesc (){
   if(window.event.keyCode == 27){
      window.event.keyCode =0;
      window.event.returnValue = false;
   }
}
window.onload = function(){
	getSort();
	updateDBFieldInfo();
	/* var oDiv = document.getElementById('floatDiv');
	var oBtn = document.getElementById('floatBtn');
	oBtn.onclick = function(){
		if( oDiv.offsetLeft == -1200){
			startMove(0,"floatDiv");
			getDrawDataStart();
			$("#showChartShow").attr("z-index","99999");
			}
		else{
			$("#floatBtn").hide();
			startMove(-1200,"floatDiv");
			$("#showChartShow").attr("z-index","1");
			}
		}
	var oDiv = document.getElementById('showSearchPlan_floatDiv');
	var oBtn = document.getElementById('showSearchPlan_floatBtn');
	oBtn.onclick = function(){
		if( oDiv.offsetLeft == -1200){
			startMove(0,"showSearchPlan_floatDiv");
			showCheckSearchPlan();
			$("#showSearchPlan_div_id").attr("z-index","99999");
			}
		else{
			$("#showSearchPlan_floatBtn").hide();
			startMove(-1200,"showSearchPlan_floatDiv");
			$("#showSearchPlan_div_id").attr("z-index","0");
			}
		} */
	}
function showPlan(showOrhide){
	if( showOrhide){
		$("#showSearchPlan_floatBtn").show();
		startMove(0,"showSearchPlan_floatDiv");
		showCheckSearchPlan();
		$("#showSearchPlan_div_id").attr("z-index","99999");
	}
	else{
		$("#showSearchPlan_floatBtn").hide();
		startMove(-1200,"showSearchPlan_floatDiv");
		$("#showSearchPlan_div_id").attr("z-index","0");
	}
}
function showByHighcharts(showOrhide){
	if( showOrhide){
		$("#floatBtn").show();
		startMove(0,"floatDiv");
		getDrawDataStart();
		$("#showChartShow").attr("z-index","99999");
		}
	else{
		$("#floatBtn").hide();
		startMove(-1200,"floatDiv");
		$("#showChartShow").attr("z-index","1");
	}
}
document.onkeydown = function(event){
 var e = event || window.event || arguments.callee.caller.arguments[0];
 if(e && e.keyCode == 27){
  closeBg();
 }
};
var timer = null;
function startMove(iTarget,id){
	clearInterval(timer);
	var oDiv = document.getElementById(id);
	timer = setInterval( function(){
		var speed = (iTarget-oDiv.offsetLeft)/20;
		speed = speed > 0 ? Math.ceil(speed): Math.floor(speed);
		if(oDiv.offsetLeft == iTarget){
			clearInterval(timer);
			}
		else{
			oDiv.style.left = oDiv.offsetLeft+speed+'px';
		}
		},30)
	}
//数据查看
//保存病种库
function showValue(value) 
{
	$("#show_value_id",parent.document).text("");
	$("#show_value_id",parent.document).text(value);
	window.parent.showVauleDiv();
}	
//病历弹出层
function showBg() {
	var bw = $("body").width();
	var bh = $("body").height();
	var windows_height = $(window).height();
	
	$("#dialog").css({
		height:windows_height
		});
	$("#fullbg").css({
	height:bh,
	width:bw,
	display:"block"
	});
	$("#dialog").show();
	}
function openBlack() {
	var bw = $("body").width();
	var bh = $("body").height();
	var windows_height = $(window).height();
	$("#fullbg").css({
	height:bh,
	width:bw,
	display:"block"
	});
}
	//关闭灰色 jQuery 遮罩
function closeBg() {
	$("#fullbg,#dialog").hide();
} 
function limitChange(){
	if($("#time_select_3_function").val()!="检索指定"){
	$("#limit_id_form").val($("#time_select_3_function").val());
	search();
	}
} 
			
function getExpressionFromIndex()
{
	$("#txtKeyword").val($("#hideinput",parent.document).val());
  	resetSearch();
}
//展示加入研究者队列界面
function savePatitentResult(){
	//全选默认关闭
	checkAll=false;
	$("#checkAll_id option:last").prop("selected", 'selected');
	diseaseFilterOk = true;
	diseaseShowFilter();
	$("#savePatitent_id_filter").show();
	$("#checkAll_id_div").show();
	//parent.savePatitent();
}
var compareOk = false;
//展示数据比较界面
function gotoPage(){
	$("#results",parent.document.body).attr("src","http://219.233.194.20:6002/FormManager/Display?id=104")
	$("#results",parent.document.body).attr("scrolling","yes");
	$("#footer",resultsDocument.document).hide();
	
}
function dataCompare(){
	//全选默认关闭
	checkAll=false;
	compareOk = true;
	$("#checkAll_id option:last").prop("selected", 'selected');
	diseaseShowFilter();
	$("#result_buttons_diesaseFilter").hide();
	$("#dataCompare_div").show();
	$("#checkAll_id_div").hide();
	showCompare();
}
//关闭数据比较界面
function cancleDataCompare(){
	$("#container_search").show();
	$("#result_buttons").show();
	var data = $("#searchDatas_id").val();
	var dataObject = jQuery.parseJSON(data);
	checkAll = true;
	compareOk = false;
	$("#checkAll_id option:first").prop("selected", 'selected');
	field_number = field_number-dataObject.columnValueList.length;
	buildTable($('#table'),dataObject,startTimeStatic);
	diseaseFiltermaps={};
	$("#action_bar").show();
	$("#dataCompare_div").hide();
	$("#result_buttons_diesaseFilter").hide();
	$("#pageCount_id").show();
	$("#addCompare_id").show();
	$('#table_compare').bootstrapTable('destroy');
	parent.$("#div_compare").dialog("close");
}
var compareLists = {};
function addCompare(vkey){
	var num = 1;
	for(var k in compareLists){
		num++;
	}
	if(num >5){
	   parent.$.dialog("数据比较最多只能比较五条!", false, 1000);
	   return;
	}
	compareLists[vkey] = vkey;
	showCompare();
}
function showCompare() {
	var htmls = "";
	for(var k in compareLists){
		htmls = htmls + "<li class=\"col-lg-3 col-sm-6 col-xs-12\" style=\"width: 100%;\"><i class=\"l_close\">X</i><div title=\"" + compareLists[k] + "\">";
		htmls = htmls + compareLists[k];
		htmls = htmls + "</div><span style=\"display: none\">"+compareLists[k]+"<span></li>";
	}
	$("#compare_Vkey",parent.document).html(htmls);
	parent.bind_remove_compare();
	parent.$("#div_compare").dialog("open");
}

function getCompareData() {
	var vkeyList = "";
	var vkeyListOk = true;
	for(var k in compareLists){
		if(vkeyListOk){
			vkeyList = k;
			vkeyListOk = false;
		}else{
			vkeyList = vkeyList+","+k
		}
	}
	
	$.ajax(
			{
				type : "post",
				url : "/hssp/rest/compareDate?vkeyLists="+vkeyList+"&tableAndField=1",
				dataType : "json",
				success : function(data)
				{
					$('#table').bootstrapTable('destroy');
					buildTableForCompare($('#table_compare'),data);
					parent.$("#div_compare").dialog("close");
					$("#action_bar").hide();
					$("#pageCount_id").hide();
					$("#addCompare_id").hide();
				},error:function(e){
	   	    		parent.location.reload();    	    
				}
			}
		);
}

function buildTableForCompare($el,datas) {
	 var columnNames = eval(datas.columnNameCN);
     //记录Vkey
    // alert( datas.vKeyList.length);
     /*columns.push({
         title: "序号",
         formatter:"runningFormatter",
         width: '10px',
         valign:'middle',
         align:'center'
     });*/
    /* columns.push({
         title: "序号",
         field: 'field' + "_number",
         valign:'middle',
         sortable:"true",
         width: '10px',
         align:'center'
     });*/
     var docid = "";
    for ( var key in columnNames) {
    	var columnName = columnNames[key];
    	for(var keyBean in columnName){
	        if(docid.indexOf(keyBean)<0){
	        columns.push({
	                    title: columnName[keyBean],
	                    field: 'field' + keyBean,
	                    valign:'middle',
	                    align:'center'
	                });
	        }
    	}
    }
    //列值
    var columnValues = eval(datas.columnValue);
    for ( var columnkey in columnValues) {
    	var columnValue = columnValues[columnkey]
        row = {};
    	for ( var key in columnNames) {
        	var columnName = columnNames[key];
    	for ( var columnNamesKey in columnName) {
    		var t = docid.indexOf(columnNamesKey);
			if(t<0){
				var columnValueSin = columnValue[columnNamesKey];
				var value = "";
				var title_text = "";
				
				if(columnValueSin.length > 1){
					for ( var l = 0; l < columnValueSin.length; l++) {
						value = value+"<div>"+columnValueSin[l]+"</div>";
					}
				}else{
						value = columnValueSin[0];
				}
	                row['field' + columnNamesKey] =value;
				}else{
				//value = "测试词";
				value = "&nbsp;&nbsp;&nbsp;&nbsp;";
			  }
			}
     }
        data.push(row);
    }
    var windows_height = $(window).height()-48;
    $el.bootstrapTable('destroy').bootstrapTable({
        columns: columns,
        data: data,
        height:windows_height
        //search:"true"
    });
    $('a[rel*=external]').attr('target', '_blank');
    
}
    </script>


<script type="text/javascript">
var field_number = 1;
var diseaseFilterOk = false;
//病种库保存数据筛选
var diseaseFiltermap = {};
var diseaseFiltermaps = {};
var vKeyList;
//默认是否全选
var checkAll = true;
var returnRownum =0;
function runningFormatter(value, row, index) {
	return (Number($("#pageNum_Id").val()) - 1) * returnRownum + index + 1;
}
var field_Empiid = "0";
var field_Empiid_index=1;
function runningFormatterForEmpiid(value, row, index) {
	if(field_Empiid == "0"){
		field_Empiid = row.field_Empiid;
	}else{
		if(field_Empiid != row.field_Empiid){
			field_Empiid_index = 1;
			field_Empiid = row.field_Empiid;
		}else{
			field_Empiid_index++;
		}
	}
	return  field_Empiid_index ;
}
function runningFormatterForOEventsID(value, row, index) {
	if(field_Empiid == "0"){
		field_Empiid = row.field_OEventsID;
	}else{
		if(field_Empiid != row.field_OEventsID){
			field_Empiid_index = 1;
			field_Empiid = row.field_OEventsID;
		}else{
			field_Empiid_index++;
		}
	}
	return  field_Empiid_index ;
}
function runningFormatterForHEventsID(value, row, index) {
	if(field_Empiid == "0"){
		field_Empiid = row.field_HEventsID;
	}else{
		if(field_Empiid != row.field_HEventsID){
			field_Empiid_index = 1;
			field_Empiid = row.field_HEventsID;
		}else{
			field_Empiid_index++;
		}
	}
	return  field_Empiid_index ;
}
function stateFormatter(value, row, index) {
	var pageNum = $("#pageNum_show").text();
	if(pageNum in diseaseFiltermaps){
		var diseaseFiltermapBean = diseaseFiltermaps[pageNum];
		if(index in diseaseFiltermapBean){
			if(checkAll){
		        return {
		           checked: false
		        }
			}else{
				return {
			       checked: true
			    }
			}
		}else{
			if(checkAll){
		    	return {
				   checked: true
			    }
			}else{
				return {
			           checked: false
			    }
			}
		 }
	}else{
		if(checkAll){
			return {
			       checked: true
			}
		}else{
			return {
			       checked: false
			}
		}
	}
    return value;
}

//数据筛选下拉框选择
function checkAllChange(){
	var value = $("#checkAll_id").val();
	diseaseFiltermap = {};
	diseaseFiltermaps = {};
	if(value == "1"){
		checkAll = true;
		$("#savePatitent_id_filter").hide();
	}else{
		checkAll = false;
		$("#savePatitent_id_filter").show();
	}
	diseaseShowFilter();
}

function diseaseSaveFilter(key,value){
	//var map = {}; // Map map = new HashMap();
	//map[key] = value; // map.put(key, value);
	//var value = map[key]; // Object value = map.get(key);
	//var has = key in map; // boolean has = map.containsKey(key);
	//delete map[key]; // map.remove(key); 
	diseaseFiltermap[key] = value;
}
$(function () {
    $('#table').on('check.bs.table', function (e, row, $el) {
    	//alert('check index: ' + $el.closest('tr').data('index'));
    	var indexNum = $el.closest('tr').data('index');
    	if(checkAll){
    	    delete diseaseFiltermap[indexNum];
    	}else{
        	diseaseFiltermap[indexNum] = vKeyList[indexNum];
    	}
    	
    });
    $('#table').on('uncheck.bs.table', function (e, row, $el) {
    	//alert('uncheck index: ' + $el.closest('tr').data('index'));
    	var indexNum = $el.closest('tr').data('index');
    	if(checkAll){
        	diseaseFiltermap[indexNum] = vKeyList[indexNum];
    	}else{
    	    delete diseaseFiltermap[indexNum];
    	}
    });
});
function diseaseShowFilter(){
	var data = $("#searchDatas_id").val();
	var dataObject = jQuery.parseJSON(data);
	diseaseFilterOk = true;
	field_number = field_number-10;
	buildTable($('#table'),dataObject,startTimeStatic);
	$("#showChartShow").hide();
}
function cancleDiseaseSave(){
	$("#container_search").show();
	$("#result_buttons").show();
	$("#result_buttons_diesaseFilter").hide();
	$("#disease_saveFilter_div").hide();
	var data = $("#searchDatas_id").val();
	var dataObject = jQuery.parseJSON(data);
	diseaseFilterOk = false;
	checkAll = true;
	$("#checkAll_id option:first").prop("selected", 'selected');
	field_number = field_number-dataObject.columnValueList.length;
	buildTable($('#table'),dataObject,startTimeStatic);
	
	//vkey记录;
	//var vKeyList = dataObject.vKeyList;
	diseaseFiltermaps={};
}
function saveDiseaseVkey(){
	var pageNum = $("#pageNum_show").text();
	var diseaseFiltermapBean = {};
	for(var key in diseaseFiltermap){
		diseaseFiltermapBean[key] = diseaseFiltermap[key];
		//delete diseaseFiltermap[key];
	}
	diseaseFiltermaps[pageNum]=diseaseFiltermapBean;
}


//加入研究者队列
function addPatitent(){
	parent.$.dialog("创建加入研究者队列任务正在执行中，请稍等...", false, 2000);
	saveDiseaseVkey();
	var diseaseFiltermapsList = diseaseFiltermaps;
	//是否全选
	var checkAll = checkAll;
	var vkeyLists = new Array();
	for(var key in diseaseFiltermapsList){
		var diseaseFiltermapList = diseaseFiltermapsList[key];
		for(var keyBean in diseaseFiltermapList){
			vkeyLists.push(diseaseFiltermapList[keyBean]);
		}
	}
	var projectId = $("#select_Patient",parent.document).val();
	var centerInfoId = $("#select_Patient_Child_"+projectId,parent.document).val();
	if(vkeyLists.length <= 0)
	{
		parent.closePatitent();
		parent.$.dialog("未选择要加入的数据!", false, 1500);
		return;
	}
	$.ajax({
		type : "post",
		url : "/hssp/tail/savePatient",
		data :"vkeyLists=" +JSON.stringify(vkeyLists)+"&projectId="+projectId+"&centerInfoId="+centerInfoId,
		success : function(result)
		{
			 if (result == "success")
			{
				parent.$.dialog("加入研究者队列任务执行成功!请稍后查看!", false, 1500);
				parent.closePatitent();
			}
			 else if(result == "defeated")
			{
				parent.$.dialog("加入研究者队列任务创建失败!", false, 1500);
			}else
			{
				parent.$.dialog("系统出错，请联系系统管理员!", false, 1500);
			}
		}
	});
}
/**
 *转义处理 
 *
 */
function htmlEscape(text){ 
	  return text.replace(/[<>"&]/g, function(match, pos, originalText){
	    switch(match){
	    case "<": return "&lt;"; 
	    case ">":return "&gt;";
	    case "&":return "&amp;"; 
	    case "\"":return "&quot;"; 
	    case "\'":return "&apos;";
	  } 
	  }); 
	}


function buildTableByVkey(datas){
	 var  row;
     columns = [];
     data = [];
 	 var docid = "";
    //var docid = "CDA_Main__CDA_Main_DoctypeName,CDA_Main_CDA_Main_DocID,CDA_Main_CDA_Main_PatientID,CDA_Main_CDA_Main_VisitNumber,CDA_Main_CDA_Main_HospitalNo,PatinfoVisitRecord_PatinfoVisitRecord_VisitNumber,cda_segment_filter_cda_segment_filter_visitnumber,cda_segment_cda_segment_hive_visitnumber";
    var showValue ="";
    var docid_DocID = "CDA_Main_CDA_Main_DocID";
    var docid_VisitNumber = "CDA_Main_CDA_Main_VisitNumber";
    var segment_docid = "cda_segment_cda_segment_hive_segtext";
    var segment_Filter_docid = "cda_segment_filter_cda_segment_filter_yuejing_youwu,cda_segment_filter_cda_segment_filter_liuchanshu,cda_segment_filter_cda_segment_filter_paogongchanshu,cda_segment_filter_cda_segment_filter_shunchanshu,cda_segment_filter_cda_segment_filter_chuchaonianling,cda_segment_filter_cda_segment_filter_zhouqi_min,cda_segment_filter_cda_segment_filter_zhouqi_max,cda_segment_filter_cda_segment_filter_jingqi_min,cda_segment_filter_cda_segment_filter_jingqi_max";
    var segment_VisitNumber = datas.pat_Vk_Name+"_"+datas.pat_Vk_Name+"_VisitNumber";
    var segment_Filter_VisitNumber = datas.pat_Vk_Name+"_"+datas.pat_Vk_Name+"_VisitNumber";
    var docid_PatientID = "CDA_Main_CDA_Main_PatientID";
    var docid_DoctypeName = "CDA_Main_CDA_Main_DoctypeName";
    var docid_HospitalNo = "CDA_Main_CDA_Main_HospitalNo";
    var docText = "CDA_Main_CDA_Main_DocText";
    var pa_VisitNumber = datas.pat_Vk_Name+"_"+datas.pat_Vk_Name+"_VisitNumber";
    var pa_OPorIP = datas.pat_Vk_Name+"_"+datas.pat_Vk_Name+"_OPOrIP";
    var formatDate = datas.forMatCodeMap;
    var pa_EMPIID = datas.pat_Vk_Name+"_"+datas.pat_Vk_Name+"_PatientID";
    var pa_Age = datas.pat_Vk_Name+"_"+datas.pat_Vk_Name+"_CurrentAge";
    var Patho_id="vTumourStatusCharacter_vTumourStatusCharacter_Id,vTumourCharacter_vTumourCharacter_Id,vLymphadenCharacter_vLymphadenCharacter_Id,vImmunityResutCharacter_vImmunityResutCharacter_Id,vEdgingCharacter_vEdgingCharacter_Id";
    var columnNames = datas.columnNameCN;
    //记录Vkey
   // alert( datas.vKeyList.length);
    vKeyList =  datas.vKeyList;
    $("#getCount_requestID").val(datas.requestID);
    $("#pageNum_Id").val(datas.pageNum);
    $("#checkPage_id").val(datas.checkPage);
    $("#pageNum_show").text(datas.pageNum);
    var pageNum = $("#pageNum_show").text();
    diseaseFiltermap = {};
    if(pageNum in diseaseFiltermaps){
       diseaseFiltermap = diseaseFiltermaps[pageNum];
    }
    //病历请求地址
    var showDocRecordAddress = datas.showDocRecordAddress;
    //病历请求方式
    var analysisWay = datas.analysisWay;
    $("#analysisWay_Id").val(analysisWay);
    //复选框
    if(diseaseFilterOk){
		$("#container_search").hide();
		$("#result_buttons").hide();
		$("#result_buttons_diesaseFilter").show();
     columns.push({
    	 title: "勾选",
         field:"state",
         valign:'middle',
            align:'center',
         formatter:"stateFormatter",
    	 checkbox: "true"
     })
    };
    if(compareOk){
   	 $("#container_search").hide();
		$("#result_buttons").hide();
		$("#result_buttons_diesaseFilter").show();
     columns.push({
         title: "加入比较",
         field: 'field_compare',
         valign:'middle',
         width: '60px',
         align:'center'
     });
    }
    columns.push({
        title: "序号",
        formatter:"runningFormatter",
        width: '10px',
        valign:'middle',
        align:'center'
    });
    columns.push({
        title: "一次就诊号",
        field: "field_VKey",
        sortable:sortTable,
        valign:'middle',
        width: '60px',
        align:'center'
    });
    /* columns.push({
        title: "标签",
        field: "field_Tag",
        valign:'middle',
        width: '60px',
        align:'center'
    }); */
   /* columns.push({
        title: "序号",
        field: 'field' + "_number",
        valign:'middle',
        sortable:"true",
        width: '10px',
        align:'center'
    });*/
   for ( var key in columnNames) {
       if(docid.indexOf(key)<0){
       	var sortTable = "false";
       	var strC =key.split("_")[1];
       	if(key.split("_").length>2){
       		strC =key.split("_")[0]+key.split("_")[2];
       	}
       	//var str="fact_pa_patinfoVisitRecord_CurrentAge,PatinfoVisitRecord_SexName,PatinfoVisitRecord_PatientID,PatinfoVisitRecord_DeptName,PatinfoVisitRecord_RegDeptName,PatinfoVisitRecord_AttendingDoctorName,PatinfoVisitRecord_DirectorDoctorName";
       	var str="";
       	//if(1>=0){
      		var length = "100px";
      		if(columnNames[key].displayFormat.length<0){
      			
      		}
      		var title_st = "<div title=\""+columnNames[key].searchTableCN+"."+columnNames[key].displayName+"\">"+"<a onclick = \"showValue('["+columnNames[key].searchTableCN+"."+columnNames[key].displayName+"]')\" rel=\"external\">"+columnNames[key].displayName+"</a></div>"
      		if(strC=="CDA_Main_DocText"){
      			sortTable = "false";        	
       		columns.push({
                   title: title_st,
                   field: 'field' + key,
                   sortable:sortTable,
                   valign:'middle',
                   width: '200px',
                   align:'center'
               });
       		continue;
      		}
      		if(strC=="fact_pa_patinfoVisitRecord_CurrentAge"){
      			sortTable = "false";        	
       		columns.push({
                   title: title_st,
                   field: 'field' + key,
                   sortable:sortTable,
                   valign:'middle',
                   width: '60px',
                   align:'center'
               });
       		continue;
      		}
      		if(strC=="PatinfoVisitRecord_SexName"){
      			sortTable = "false";        	
       		columns.push({
                   title: title_st,
                   field: 'field' + key,
                   sortable:sortTable,
                   valign:'middle',
                   width: '40px',
                   align:'center'
               });
       		continue
      		}
       	if(str.indexOf(strC)>=0){
       		sortTable = "true";        	
       		columns.push({
                   title: title_st,
                   field: 'field' + key,
                   sortable:sortTable,
                   valign:'middle',
                   width: length,
                   align:'center'
               });
       	}else{
       		columns.push({
                   title: title_st,
                   field: 'field' + key,
                   valign:'middle',
                   width: length,
                   align:'center'
               });
       	}
       	
       }
   }
   //列值
   length = "90px";
   var columnValues = datas.columnValueList;
   var vKeyListData = datas.vKeyList;
   //记录当期数据记录数
   $("#lastDateNum_id").val(datas.rowNum);
   for ( var columnkey in columnValues) {
   	var columnValue = columnValues[columnkey]
   	if(columnValue == null ){
   		continue;
   	}
       row = {};
   	if(compareOk){
   		row['field_compare'] ="<a onclick = \"addCompare('"+vKeyListData[columnkey]["vkey"]+"')\">添加("+vKeyListData[columnkey]["vkey"]+")<a>";
   	}
   	row['field' + "_number"] =field_number;
   	row["field_VKey"] ="<div id=\"table_show_div_一次就诊号"+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+vKeyListData[columnkey]["vkey"]+"\" >"+vKeyListData[columnkey]["vkey"]+"</div>";
   //	row["field_Tag"] ="<a onclick = \"showAddTag('VKey','"+vKeyListData[columnkey]["vkey"]+"','"+vKeyListData[columnkey]["vkey"]+"')\">添加标签<a>";
   	field_number ++;
   	//是否已经生成html请求路径
   	var t_Patho_ok=true;
   	for ( var columnNamesKey in columnNames) {
   		var t = docid.indexOf(columnNamesKey);
	    var value = "";
   		//if(typeof(columnValue[columnNamesKey])=="undefined"){
			//   continue;
			//}
		if(t<0 && !(typeof(columnValue[columnNamesKey])=="undefined")){
			var columnValueSin = columnValue[columnNamesKey];
			var t1 = docText.indexOf(columnNamesKey);
			var t_segment_docid = segment_docid.indexOf(columnNamesKey);
			var t_segment_Filter_docid = segment_Filter_docid.indexOf(columnNamesKey);
			var t2 = columnNamesKey.indexOf(pa_EMPIID);
			var t3 = columnNamesKey.indexOf(formatDate);
			var t4 = columnNamesKey.indexOf(pa_Age);
			var t_CheckSample = columnNamesKey.indexOf("CheckSample_CheckSample_Field");
			var t_Patho_id=Patho_id.indexOf(columnNamesKey);
			var title_text = "";
			if(t_Patho_id >= 0 && t_Patho_ok){
				if(columnValueSin.length == 1){
				    if(columnValueSin[0].length > 0){
					    title_text = columnValueSin[0].replace(/<font style=\"color:red;\">/g,"");
						title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
						title_text = title_text.replace(/s+/g,"");
						title_text = parseInt(title_text);
						//长海医院浦三科病理展示
				        value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.0.3:6199/home/detail?id="+title_text+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+"查看病理详情"+"</p></a></div>";
					}
				}else if(columnValueSin.length > 1){
				  for ( var l = 0; l < columnValueSin.length; l++) {
				    title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
					title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
					title_text = title_text.replace(/s+/g,"");
					title_text = parseInt(title_text);
					//长海医院浦三科病理展示
					value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.0.3:6199/home/detail?id="+title_text+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+"查看病理详情"+"</p></a></div>";
				   }
				}else{
				    value = "&nbsp;&nbsp;&nbsp;&nbsp;";
				}
				t_Patho_ok = false;
			}else if(t_CheckSample >= 0){
			    title_text = columnValueSin[0];
			    if(title_text == "有"){
    				   var url = columnValue["CheckSample_CheckSample_Field_url"][0];
				   value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://"+url+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+title_text+"</p></a></div>";
			    }else{
			       value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+title_text+"</p></a></div>";
			    }
			}else if(t_segment_docid >= 0){
			
			    if(analysisWay == 7){
				    if(columnValueSin.length == 1){
					    if(columnValueSin[0].length > 0){
						    title_text = columnValueSin[0].replace(/<font style=\"color:red;\">/g,"");
							title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
							title_text = title_text.replace(/s+/g,"");
								//华西医院病例展示
							if(!(typeof(columnValue[segment_VisitNumber])=="undefined")){
					            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.0.42/trakcarelive/trak/web/csp/epr.newfw.OtherUsemain.csp?EpisodeID="+""+columnValue[segment_VisitNumber][0]+"&USERNAME=00357&PASSWORD=dhcc43\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
							}else{
								value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
							}
						}
					}else if(columnValueSin.length > 1){
					  for ( var l = 0; l < columnValueSin.length; l++) {
					   // title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"").replace(/<\/font>/g,"");
					    title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
						title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
						//华西医院病例展示
							if((!(typeof(columnValue[segment_VisitNumber])=="undefined")) && !(typeof(columnValueSin[0])=="undefined")){
					            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.0.42/trakcarelive/trak/web/csp/epr.newfw.OtherUsemain.csp?EpisodeID="+""+columnValue[segment_VisitNumber][l]+"&USERNAME=00357&PASSWORD=dhcc43\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
							}else{
								value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
							}
					   }
					}else{
					    value = "&nbsp;&nbsp;&nbsp;&nbsp;";
					}
				}else{
				    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
				}
			}else if(t_segment_Filter_docid>0){
				if(analysisWay == 7){
						//华西医院病例展示
					if((!(typeof(columnValue[segment_Filter_VisitNumber])=="undefined")) &&  !(typeof(columnValueSin[0])=="undefined")){
			            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.0.42/trakcarelive/trak/web/csp/epr.newfw.OtherUsemain.csp?EpisodeID="+""+columnValue[segment_Filter_VisitNumber][0]+"&USERNAME=00357&PASSWORD=dhcc43\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
					}else{
					    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
					}
				}else{
				    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
				}
			}else if(t1>=0){
			   //病历展示接口
				if(columnValueSin.length == 1){						 
					if(columnValueSin[0].length > 0){
						title_text = columnValueSin[0].replace(/<font style=\"color:red;\">/g,"");
						title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
						title_text = title_text.replace(/s+/g,"");
						if(analysisWay == 3 && (!(typeof(columnValue[docid_DocID])=="undefined") && !(typeof(columnValue[docid_VisitNumber]) =="undefined"))){
							value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/medicalrecord/index?docid="+columnValue[docid_DocID][0]+"&hospitalNo="+"0001"+"&VisitNumber="+columnValue[docid_VisitNumber][0]+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
						}else if(analysisWay == 4){
						//长海医院病历展示接口
				        	if(columnValue[docid_VisitNumber][0].split("_").length == 2){
				        		var nVisitID =columnValue[docid_VisitNumber][0].split("_")[1];
							    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/CommonService/MedicalRecord/index?strPatientId="+columnValue[docid_PatientID][0]+"&strFileName="+columnValue[docid_DoctypeName][0]+"&nVisitID="+nVisitID+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
				        	}else{
							     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></div>";
							}
						}else if(analysisWay == 5){
						//大华医院病历展示
							if((!(typeof(columnValue[docid_VisitNumber])=="undefined"))){
					            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/medicalrecord/index?docid="+columnValue[docid_DocID][0]+"&hospitalNo="+columnValue[docid_HospitalNo][0]+"&VisitNumber="+columnValue[docid_VisitNumber][0]+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
							}else{
							     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></div>";
							}
						}else if(analysisWay == 6){
						//精卫医院病历展示接口
							if((!(typeof(columnValue[docid_DocID])=="undefined"))){
					            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/CommonService/medicalrecord/index?docid="+columnValue[docid_DocID][0]+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
							}else{
							     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></div>";
							}
					     }else if(analysisWay == 9){
								//中西医展示
							if((!(typeof(columnValue[docid_DocID])=="undefined"))){
					            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/CommonService/medicalrecord/index?docid="+columnValue[docid_DocID][0]+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
							}else{
							     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></div>";
							}
					     }else {
						    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
						}
					}else{
						value = "&nbsp;&nbsp;&nbsp;&nbsp;";
					}
				}else if(columnValueSin.length > 1){
					for ( var l = 0; l < columnValueSin.length; l++) {
					   // title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"").replace(/<\/font>/g,"");
					    title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
						title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
					    if(analysisWay == 3 && (!(typeof(columnValue[docid_DocID])=="undefined") && !(typeof(columnValue[docid_VisitNumber]) =="undefined"))){
							value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/medicalrecord/index?docid="+columnValue[docid_DocID][l]+"&hospitalNo="+"0001"+"&VisitNumber="+columnValue[docid_VisitNumber][l]+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
						}else if(analysisWay == 4){
						//长海医院病历展示接口
							if((!(typeof(columnValue[docid_VisitNumber])=="undefined")) && columnValue[docid_VisitNumber][l].split("_").length == 2){
					            var nVisitID =columnValue[docid_VisitNumber][l].split("_")[1];
								 value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/CommonService/MedicalRecord/index?strPatientId="+columnValue[docid_PatientID][l]+"&strFileName="+columnValue[docid_DoctypeName][l]+"&nVisitID="+nVisitID+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
							}else{
							     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></div>";
							}
						}else if(analysisWay == 5){
						//大华医院病历展示
							if((!(typeof(columnValue[docid_VisitNumber])=="undefined"))){
					            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/medicalrecord/index?docid="+columnValue[docid_DocID][l]+"&hospitalNo="+columnValue[docid_HospitalNo][l]+"&VisitNumber="+columnValue[docid_VisitNumber][l]+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
							}else{
							     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></div>";
							}
						}else  if(analysisWay == 6){
						//精卫医院病历展示接口
							if((!(typeof(columnValue[docid_DocID])=="undefined"))){
					            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/CommonService/medicalrecord/index?docid="+columnValue[docid_DocID][l]+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
							}else{
							     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></div>";
							}
					     }else if(analysisWay == 7){
						//华西医院病例展示
							if((!(typeof(columnValue[docid_VisitNumber])=="undefined"))){
					            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.0.42/trakcarelive/trak/web/csp/epr.newfw.OtherUsemain.csp?EpisodeID="+""+columnValue[docid_VisitNumber][l]+"&USERNAME=00357&PASSWORD=dhcc43\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
							}else{
							     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></div>";
							}
						}else{
						    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
						}
					}
				}else{
					//value = "测试词";
					value = "&nbsp;&nbsp;&nbsp;&nbsp;";
				}
			}else if(t2>=0){
			    //患者360视图
			    if(columnValueSin.length == 1){		
					if( columnValueSin[0].length > 0){
					    var vn_value = "";
			            if(typeof(columnValue[pa_VisitNumber])!="undefined" && typeof(columnValue[pa_VisitNumber][0])!="undefined"){
			        	  vn_value = columnValue[pa_VisitNumber][0];
			        	  vn_value = vn_value.replace(/<font style=\"color:red;\">/g,"");;
			            	vn_value = vn_value.replace(/<\/font>/g,"");
			            }
			            var SYSTEM = "";
			            if(typeof(columnValue[pa_OPorIP])!="undefined" && typeof(columnValue[pa_OPorIP][0])!="undefined"){
			            	SYSTEM = columnValue[pa_OPorIP][0];
			            	SYSTEM = SYSTEM.replace(/<font style=\"color:red;\">/g,"");;
			            	SYSTEM = SYSTEM.replace(/<\/font>/g,"");
			            	if(SYSTEM == "门诊"){
			            	   SYSTEM = "OP";
			            	}else{
			            	   SYSTEM = "IP";
			            	}
			            }
						title_text = columnValueSin[0].replace(/<font style=\"color:red;\">/g,"");
						title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
					    if(analysisWay == 4){
					        // 长海医院
							vn_value=toBase64(vn_value);
							vn_value=encodeURI(vn_value);			 
					        value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://172.16.32.111:6008/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
					    }else if(analysisWay == 5){
					   		//大华医院
					   	 	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://202.202.202.182:6008/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
					    }else if(analysisWay == 6){
					    	//精卫医院
					    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://10.40.22.97:6004/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
					    }else if(analysisWay == 8){
					    	//广西医院
					    	vn_value=toBase64(vn_value);
							vn_value=encodeURI(vn_value);	
					    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.100.153:6601/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
					    }else if(analysisWay == 9){
					    	//中西医
					    	vn_value=toBase64(vn_value);
							//vn_value=encodeURI(vn_value);	
					    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.10.6.56:6008/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
					    }else{
					    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></div>";
					    }
					}else{
						value = "&nbsp;&nbsp;&nbsp;&nbsp;";
					}
				}else if(columnValueSin.length > 1){
					for ( var l = 0; l < columnValueSin.length; l++) {
						var vn_value = "";
						if(typeof(columnValue[pa_VisitNumber])!="undefined" && typeof(columnValue[pa_VisitNumber][l])!="undefined"){
			        	  vn_value = columnValue[pa_VisitNumber][l];
			        	  vn_value = vn_value.replace(/<font style=\"color:red;\">/g,"");;
			            	vn_value = vn_value.replace(/<\/font>/g,"");
			            }
			            var SYSTEM = "";
			            if(typeof(columnValue[pa_OPorIP])!="undefined" && typeof(columnValue[pa_OPorIP][l])!="undefined"){
			            	SYSTEM = columnValue[pa_OPorIP][l];
			            	SYSTEM = SYSTEM.replace(/<font style=\"color:red;\">/g,"");;
			            	SYSTEM = SYSTEM.replace(/<\/font>/g,"");
			            	if(SYSTEM == "门诊"){
			            	   SYSTEM = "OP";
			            	}else{
			            	   SYSTEM = "IP";
			            	}
			            }
					    title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
						title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
						if(analysisWay == 4){
							 // 长海医院
							vn_value=toBase64(vn_value);
							vn_value=encodeURI(vn_value);			 
					        value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://172.16.32.111:6008/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";		 
					    
					    }else if(analysisWay == 5){
					   		//大华医院
					   	 	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://202.202.202.182:6008/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
					    }else if(analysisWay == 6){
					    	//精卫医院
					    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://10.40.22.97:6004/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
					    }else if(analysisWay == 8){
					    	//广西医院
					    	vn_value=toBase64(vn_value);
							vn_value=encodeURI(vn_value);	
					    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.100.153:6601/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
					    }else if(analysisWay == 9){
					    	//中西医
					    	vn_value=toBase64(vn_value);
							//vn_value=encodeURI(vn_value);	
					    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.10.6.56:6008/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
					    }else{
					    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></div>";
					    }
					}
				}else{
					//value = "测试词";
					value = "&nbsp;&nbsp;&nbsp;&nbsp;";
				}
			}else  
			  if(t4>=0){
				if(columnValueSin.length == 1){						 
						if(columnValueSin[0].length > 0){
							//var age = Number(columnValueSin[0]);
							var age = columnValueSin[0];
						    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+age+"\" style=\"margin: auto;width:"+"60px"+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+age+"</p></div>";
						}else{
							value = "&nbsp;&nbsp;&nbsp;&nbsp;";
						}
					}else if(columnValueSin.length > 1){
						for ( var l = 0; l < columnValueSin.length; l++) {
					    //var age = Number(columnValueSin[l]);
					    var age = columnValueSin[l];
						   value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+age+"\" style=\"margin: auto;width:"+"60px"+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+age+"</p></div>";
						}
					}else{
						//value = "测试词";
						value = "&nbsp;&nbsp;&nbsp;&nbsp;";
					}
			}
			else{
				if(columnValueSin.length == 1){
					if(columnValueSin[0].length > 0){
						title_text = columnValueSin[0].replace(/<font style=\"color:red;\">/g,"");
						title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
						
						title_text=title_text.replace("&#x0D;", "");
						title_text=title_text.replace(/<\/?.+?>/g, "");
						if(columnNamesKey.indexOf("fact_pa_patinfoVisitRecord_CurrentAge")>=0){
							value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+"60px"+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+columnValueSin[0]+"</p></div>";
						}else if(columnNamesKey.indexOf("PatinfoVisitRecord_SexName")>=0){
						    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+"40px"+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+columnValueSin[0]+"</p></div>";
						}else if(showValue.indexOf(columnNamesKey)>=0){
						    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><u><p>"+columnValueSin[0]+"</p></u></a></div>";
						}else{
							if(title_text.length > 25){
						      value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><u><p>"+columnValueSin[0]+"</p></u></a></div>";
							}else{
							  value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+columnValueSin[0]+"</p></div>";
							}
						}
					}else{
						value = "&nbsp;&nbsp;&nbsp;&nbsp;";
					}
				}else if(columnValueSin.length > 1){
					if(columnNamesKey.indexOf("fact_pa_patinfoVisitRecord_CurrentAge")>=0){
						for ( var l = 0; l < columnValueSin.length; l++) {
							title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
							title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
							title_text=title_text.replace(/<\/?.+?>/g, "");
							value = value+"<div class=\"th-inner\" id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+"60px"+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+columnValueSin[l]+"</p></div>";
						}
					}else if(columnNamesKey.indexOf("PatinfoVisitRecord_SexName")>=0){
						for ( var l = 0; l < columnValueSin.length; l++) {
							title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
							title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
							title_text=title_text.replace(/<\/?.+?>/g, "");
							value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+"40px"+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+columnValueSin[l]+"</p></div>";
						}
					}else if(showValue.indexOf(columnNamesKey)>=0){
						  for ( var l = 0; l < columnValueSin.length; l++) {
							title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
							title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
							
						    title_text=title_text.replace("&#x0D;", "");
						    title_text=title_text.replace(/<\/?.+?>/g, "");
							value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><u><p>"+columnValueSin[l]+"</p></u></a></div>";
						}
					}else{
						for ( var l = 0; l < columnValueSin.length; l++) {
							title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
							title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
							
						    title_text=title_text.replace("&#x0D;", "");
						    title_text=title_text.replace(/<\/?.+?>/g, "");
							if(title_text.length > 25){
							  value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><u><p>"+columnValueSin[l]+"</p></u></a></div>";
							}else{
							  value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_VKey_"+vKeyListData[columnkey]["vkey"]+"_"+vKeyListData[columnkey]["vkey"]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+columnValueSin[l]+"</p></div>";
							}
						}
					}
				}else{
					//value = "测试词";
					value = "&nbsp;&nbsp;&nbsp;&nbsp;";
				}
			}
                row['field' + columnNamesKey] =value;
		}
   	}
       data.push(row);
   }
}
var columns = [],
	data = [];
var input_length= 0;
function buildTable($el,datas,startTime) {
	//当页应该返回条数
	var  row;
	returnRownum = datas.pageRowNum;
    //一次就诊数据展示
	openBlack()
     //列名
     if(datas.responseStatu == "0000"){
    	if(datas.searchByType == "VKey"){
    		buildTableByVkey(datas);
    	}else{
    		buildTableByEmpiid(datas,datas.searchByType);
    	}
    	input_length = document.getElementById("txtKeyword").value.length;
	    var windows_height = $(window).height();
	    if (diseaseFilterOk || compareOk) {
	    	//windows_height = windows_height-$("#action_bar").height()-$("#pageCount").height();
	    	windows_height = windows_height-$("#action_bar").height();
		}else{
			//windows_height = windows_height-70-$("#action_bar").height()-$("#pageCount").height();
			windows_height = windows_height-70-$("#action_bar").height();
		}
	    $el.bootstrapTable('destroy').bootstrapTable({
	        columns: columns,
	        data: data,
	        height:windows_height,
	        striped:true
	        //search:"true"
	    });
    	$el.bootstrapTable("hideColumn","field_VKey");
    	$('a[rel*=external]').attr('target', '_blank');
    	$("i[class='glyphicon glyphicon-th icon-th']").html("隐藏");
		$("i[class='glyphicon glyphicon-th icon-th']").removeClass("glyphicon-th");
	    if(diseaseFilterOk){
	      $("input[name='btSelectAll']").hide();
	    }
	    if (diseaseFilterOk || compareOk) {
	    	
	    	$("#disease_saveFilter_div").show();
	    	$("div.fixed-table-toolbar .columns").css({
	    		top:-5,
	    		});
	    	//$("input[name='btSelectAll']").click();
	    	//$("input[name='btSelectItem']").attr("checked", 'checked');
	    }
		//$("#content").show();
   	    var endTime = new Date();
   	    var time = endTime.getTime()-startTime.getTime()
   	    $("#calcTimeTick_text").html("搜索耗时："+time+"毫秒");
		$("#calcTimeTick").show();
		$("#calcTimeTick_text").show();
		$("#calcTotalRecords").show();
		  if(datas.pageNum == 1){
			 
			     $("#pageLast").attr("disabled",true);
		  }else{
		    setTimeout(function (){
		     $("#pageLast").attr("disabled",false);
		       },1000);
		  }
		  if(datas.pageNum >= datas.totalPageNum){
		     $("#pageNext").attr("disabled",true);
		  }else{
		  setTimeout(function (){
		     $("#pageNext").attr("disabled",false);
		      },1000);
		  }
		 var calcTotalRecords = "从"+datas.recordTotal+"条记录中找到"+datas.totalRecordShow+"条,"
   		 $("#maxData_Id").val(datas.totalRecord);
   		 $("#nextCursorMark_id").val(datas.nextCursorMark);
   		 $("#calcTotalRecords").html(calcTotalRecords);
   		 $("#calcTotalRecords").show();
   		 $("#countShow").html(datas.totalPageNum);
   		 $("#maxPageNum").val(datas.totalPageNum);
   		 $("#pageCount").show();
	     $("#pageCount_id").show();
		 judgeShowChart(datas);
		 if(datas.searchByType == "VKey"){
			$("#time_limit_id").show();
			$("#sort_field_id").show();
			$("#sort_type_id").show();
	 	}else{
	 		var index = 0;
			 var rowspanNum= 1;
			 var columnValueListMap = datas.columnValueListMap;
		     for(var empiid in columnValueListMap){
		        var columnValues = columnValueListMap[empiid];
				rowspanNum =columnValues.length;
			    $('#table').bootstrapTable("mergeCells",{index:index, field:"field_"+datas.searchByType, colspan: 1, rowspan: rowspanNum});
			    if(diseaseFilterOk){
			   		 $('#table').bootstrapTable("mergeCells",{index:index, field:"state", colspan: 1, rowspan: rowspanNum});
			    }
			    index =index+columnValues.length;
			}
			$("#time_limit_id").hide();
			$("#sort_field_id").hide();
			$("#sort_type_id").hide();
			$("#table").attr("data-show-columns","false");	
	 	}
		 $("#showSearchPlan_div_id").show(500);
     }else{
    	 columns.push({
             field: 'field' + "errorMessage",
         });
    	 columns = [];
    		data = [];
    	 row = {};
    	 if(datas.responseStatu == "9999"){
    		 row['field' + "errorMessage"] = datas.responseDescription;
    	 }else{
    		 row['field' + "errorMessage"] = "检索失败,请检查您的检索条件或者联系系统管理员！";
    	 }
    	 data.push(row);
    	 $el.bootstrapTable('destroy').bootstrapTable({
    	        columns: columns,
    	        data: data
    	    });
     }
	 if(datas.searchByType == "VKey"){
			$("#time_limit_id").show();
			$("#sort_field_id").show();
			$("#sort_type_id").show();
	 }else{
			$("#time_limit_id").hide();
			$("#sort_field_id").hide();
			$("#sort_type_id").hide();
	 }
     closeBg();
}
function buildTableByEmpiid(datas,serchType) {
	field_Empiid_index = 1;
	field_Empiid = "0";
    //以人维度数据展示
    var  row;
        columns = [];
        data = [];
     //列名
     var docid = "";
     //var docid = "CDA_Main__CDA_Main_DoctypeName,CDA_Main_CDA_Main_DocID,CDA_Main_CDA_Main_PatientID,CDA_Main_CDA_Main_VisitNumber,CDA_Main_CDA_Main_HospitalNo,PatinfoVisitRecord_PatinfoVisitRecord_VisitNumber,cda_segment_filter_cda_segment_filter_visitnumber,cda_segment_cda_segment_hive_visitnumber";
     var showValue ="";
     var docid_DocID = "CDA_Main_CDA_Main_DocID";
     var docid_VisitNumber = "CDA_Main_CDA_Main_VisitNumber";
     var segment_docid = "cda_segment_cda_segment_hive_segtext";
     var segment_Filter_docid = "cda_segment_filter_cda_segment_filter_yuejing_youwu,cda_segment_filter_cda_segment_filter_liuchanshu,cda_segment_filter_cda_segment_filter_paogongchanshu,cda_segment_filter_cda_segment_filter_shunchanshu,cda_segment_filter_cda_segment_filter_chuchaonianling,cda_segment_filter_cda_segment_filter_zhouqi_min,cda_segment_filter_cda_segment_filter_zhouqi_max,cda_segment_filter_cda_segment_filter_jingqi_min,cda_segment_filter_cda_segment_filter_jingqi_max";
     var segment_VisitNumber = datas.pat_Vk_Name+"_"+datas.pat_Vk_Name+"_VisitNumber";
     var segment_Filter_VisitNumber = datas.pat_Vk_Name+"_"+datas.pat_Vk_Name+"_VisitNumber";
     var docid_PatientID = "CDA_Main_CDA_Main_PatientID";
     var docid_DoctypeName = "CDA_Main_CDA_Main_DoctypeName";
     var docid_HospitalNo = "CDA_Main_CDA_Main_HospitalNo";
     var docText = "CDA_Main_CDA_Main_DocText";
     var pa_VisitNumber = datas.pat_Vk_Name+"_"+datas.pat_Vk_Name+"_VisitNumber";
     var pa_OPorIP = datas.pat_Vk_Name+"_"+datas.pat_Vk_Name+"_OPOrIP";
     var formatDate = datas.forMatCodeMap;
     var pa_EMPIID = datas.pat_Vk_Name+"_"+datas.pat_Vk_Name+"_PatientID";
     var pa_Age = datas.pat_Vk_Name+"_"+datas.pat_Vk_Name+"_CurrentAge";
     var Patho_id="vTumourStatusCharacter_vTumourStatusCharacter_Id,vTumourCharacter_vTumourCharacter_Id,vLymphadenCharacter_vLymphadenCharacter_Id,vImmunityResutCharacter_vImmunityResutCharacter_Id,vEdgingCharacter_vEdgingCharacter_Id";
     var columnNames = datas.columnNameCN;
     //记录Vkey
    // alert( datas.vKeyList.length);
     vKeyList =  datas.empidList;
     $("#getCount_requestID").val(datas.requestID);
	$("#pageNum_Id").val(datas.pageNum);
	$("#checkPage_id").val(datas.checkPage);
	$("#pageNum_show").text(datas.pageNum);
     var pageNum = $("#pageNum_show").text();
     diseaseFiltermap = {};
     if(pageNum in diseaseFiltermaps){
        diseaseFiltermap = diseaseFiltermaps[pageNum];
     }
     //病历请求地址
     var showDocRecordAddress = datas.showDocRecordAddress;
     //病历请求方式
     var analysisWay = datas.analysisWay;
     $("#analysisWay_Id").val(analysisWay);
     //复选框
     if(diseaseFilterOk){
			$("#container_search").hide();
			$("#result_buttons").hide();
			$("#result_buttons_diesaseFilter").show();
	     columns.push({
	    	 checkbox: "true",
	         field:"state",
	         formatter:"stateFormatter",
        	 valign:'middle',
             align:'center'
	     })
     };
    if(serchType == "Empiid"){
    	 columns.push({
             title: "人员编号",
             field: "field_"+serchType,
             sortable:sortTable,
             valign:'middle',
             width: '60px',
             align:'center'
         });
    }else if(serchType == "OEventsID"){
    	 columns.push({
             title: "手术事件编号",
             field: "field_"+serchType,
             sortable:sortTable,
             valign:'middle',
             width: '60px',
             align:'center'
         });
    }else if(serchType == "HEventsID"){
   	 columns.push({
         title: "住院事件编号",
         field: "field_"+serchType,
         sortable:sortTable,
         valign:'middle',
         width: '60px',
         align:'center'
     });
	}
    /*  columns.push({
         title: "序号",
         formatter:"runningFormatterFor"+serchType,
         width: '10px',
         valign:'middle',
         align:'center'
     }); */
     columns.push({
         title: "一次就诊号",
         field: "field_VKey",
         sortable:sortTable,
         valign:'middle',
         width: '60px',
         align:'center'
     });
    /*  columns.push({
         title: "标签",
         field: "field_Tag",
         valign:'middle',
         width: '60px',
         align:'center'
     }); */
    columns.push({
         title: "序号",
         field: 'field' + "_number",
         valign:'middle',
         sortable:"true",
         width: '10px',
         align:'center'
     });
    for ( var key in columnNames) {
        if(docid.indexOf(key)<0){
        	var sortTable = "false";
        	var strC =key.split("_")[1];
        	if(key.split("_").length>2){
        		strC =key.split("_")[0]+key.split("_")[2];
        	}
        	//var str="fact_pa_patinfoVisitRecord_CurrentAge,PatinfoVisitRecord_SexName,PatinfoVisitRecord_PatientID,PatinfoVisitRecord_DeptName,PatinfoVisitRecord_RegDeptName,PatinfoVisitRecord_AttendingDoctorName,PatinfoVisitRecord_DirectorDoctorName";
        	var str="";
        	//if(1>=0){
       		var length = "100px";
       		if(columnNames[key].displayFormat.length<0){
       			
       		}
       		var title_st = "<div title=\""+columnNames[key].searchTableCN+"."+columnNames[key].displayName+"\">"+"<a onclick = \"showValue('["+columnNames[key].searchTableCN+"."+columnNames[key].displayName+"]')\" rel=\"external\">"+columnNames[key].displayName+"</a></div>"
       		if(strC=="CDA_Main_DocText"){
       			sortTable = "false";        	
        		columns.push({
                    title: title_st,
                    field: 'field' + key,
                    sortable:sortTable,
                    valign:'middle',
                    width: '200px',
                    align:'center'
                });
        		continue;
       		}
       		if(strC=="fact_pa_patinfoVisitRecord_CurrentAge"){
       			sortTable = "false";        	
        		columns.push({
                    title: title_st,
                    field: 'field' + key,
                    sortable:sortTable,
                    valign:'middle',
                    width: '60px',
                    align:'center'
                });
        		continue;
       		}
       		if(strC=="PatinfoVisitRecord_SexName"){
       			sortTable = "false";        	
        		columns.push({
                    title: title_st,
                    field: 'field' + key,
                    sortable:sortTable,
                    valign:'middle',
                    width: '40px',
                    align:'center'
                });
        		continue
       		}
        	if(str.indexOf(strC)>=0){
        		sortTable = "true";        	
        		columns.push({
                    title: title_st,
                    field: 'field' + key,
                    sortable:sortTable,
                    valign:'middle',
                    width: length,
                    align:'center'
                });
        	}else{
        		columns.push({
                    title: title_st,
                    field: 'field' + key,
                    valign:'middle',
                    width: length,
                    align:'center'
                });
        	}
        	
        }
    }
    //列值
    length = "90px";
    var vKeyListData = datas.vKeyList;
    //记录当期数据记录数
    $("#lastDateNum_id").val(datas.rowNum);
    var columnValueListMap = datas.columnValueListMap;
    var num = 0;
    for(var empiid in columnValueListMap){
        var columnValues = columnValueListMap[empiid];
       // var empiid = vKeyListData[num]["vkey"];
        num++;
        field_number =1;
    	for ( var columnkey in columnValues) {
            row = {};
        	var columnValue = columnValues[columnkey];
        	var serchTypeDes = "";
        	 if(serchType == "Empiid"){
        		serchTypeDes= "人员编号";
            }else if(serchType == "OEventsID"){
            	serchTypeDes= "手术事件编号";
            }else if(serchType == "HEventsID"){
            	serchTypeDes= "住院事件编号";
        	}
            row["field_"+serchType] ="<div id=\"table_show_div_"+serchTypeDes+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+empiid+"\" >"+empiid+"</div>";;
        	row['field' + "_number"] =field_number;
        	row["field_VKey"] ="<div id=\"table_show_div_一次就诊号"+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+columnValue["VKey"][0]+"\" >"+columnValue["VKey"][0]+"</div>";
        	//row["field_Tag"] ="<a onclick = \"showAddTag('"+serchType+"','"+empiid+"','"+columnValue["VKey"][0]+"')\">添加标签<a>";
        	field_number ++;
        	var t_Patho_ok = true;
        	for ( var columnNamesKey in columnNames) {
        		var t = docid.indexOf(columnNamesKey);
    		    var value = "";
        		//if(typeof(columnValue[columnNamesKey])=="undefined"){
    				//   continue;
    				//}
    			if(t<0 && !(typeof(columnValue[columnNamesKey])=="undefined")){
    				var columnValueSin = columnValue[columnNamesKey];
    				var t1 = docText.indexOf(columnNamesKey);
    				var t_segment_docid = segment_docid.indexOf(columnNamesKey);
    				var t_segment_Filter_docid = segment_Filter_docid.indexOf(columnNamesKey);
    				var t2 = columnNamesKey.indexOf(pa_EMPIID);
    				var t3 = columnNamesKey.indexOf(formatDate);
    				var t4 = columnNamesKey.indexOf(pa_Age);
    				var t_CheckSample = columnNamesKey.indexOf("CheckSample_CheckSample_Field");
    				var t_Patho_id=Patho_id.indexOf(columnNamesKey);
    				var title_text = "";
    				if(t_Patho_id >= 0 && t_Patho_ok){
    					if(columnValueSin.length == 1){
    					    if(columnValueSin[0].length > 0){
    						    title_text = columnValueSin[0].replace(/<font style=\"color:red;\">/g,"");
    							title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
    							title_text = title_text.replace(/s+/g,"");
    							title_text = parseInt(title_text);
    							//长海医院浦三科病理展示
    					        value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.0.3:6199/home/detail?id="+title_text+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+"查看病理详情"+"</p></a></div>";
    						}
    					}else if(columnValueSin.length > 1){
    					  for ( var l = 0; l < columnValueSin.length; l++) {
    					    title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
    						title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
    						title_text = title_text.replace(/s+/g,"");
    						title_text = parseInt(title_text);
    						//长海医院浦三科病理展示
    						value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.0.3:6199/home/detail?id="+title_text+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+"查看病理详情"+"</p></a></div>";
    					   }
    					}else{
    					    value = "&nbsp;&nbsp;&nbsp;&nbsp;";
    					}
    					t_Patho_ok = false;
    				}else if(t_CheckSample >= 0){
    				    title_text = columnValueSin[0];
    				    if(title_text == "有"){
         				   var url = columnValue["CheckSample_CheckSample_Field_url"][0];
    					   value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://"+url+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+title_text+"</p></a></div>";
    				    }else{
    				       value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+title_text+"</p></a></div>";
    				    }
    				}else
    				if(t_segment_docid >= 0){
    				
    				    if(analysisWay == 7){
    					    if(columnValueSin.length == 1){
    						    if(columnValueSin[0].length > 0){
    							    title_text = columnValueSin[0].replace(/<font style=\"color:red;\">/g,"");
    								title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
    								title_text = title_text.replace(/s+/g,"");
    									//华西医院病例展示
    								if(!(typeof(columnValue[segment_VisitNumber])=="undefined")){
    						            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.0.42/trakcarelive/trak/web/csp/epr.newfw.OtherUsemain.csp?EpisodeID="+""+columnValue[segment_VisitNumber][0]+"&USERNAME=00357&PASSWORD=dhcc43\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    								}else{
    									value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    								}
    							}
    						}else if(columnValueSin.length > 1){
    						  for ( var l = 0; l < columnValueSin.length; l++) {
    						   // title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"").replace(/<\/font>/g,"");
    						    title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
    							title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
    							//华西医院病例展示
    								if((!(typeof(columnValue[segment_VisitNumber])=="undefined")) && !(typeof(columnValueSin[0])=="undefined")){
    						            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.0.42/trakcarelive/trak/web/csp/epr.newfw.OtherUsemain.csp?EpisodeID="+""+columnValue[segment_VisitNumber][l]+"&USERNAME=00357&PASSWORD=dhcc43\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
    								}else{
    									value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
    								}
    						   }
    						}else{
    						    value = "&nbsp;&nbsp;&nbsp;&nbsp;";
    						}
    					}else{
    					    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    					}
    				}else if(t_segment_Filter_docid>0){
    					if(analysisWay == 7){
    							//华西医院病例展示
    						if((!(typeof(columnValue[segment_Filter_VisitNumber])=="undefined")) &&  !(typeof(columnValueSin[0])=="undefined")){
    				            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.0.42/trakcarelive/trak/web/csp/epr.newfw.OtherUsemain.csp?EpisodeID="+""+columnValue[segment_Filter_VisitNumber][0]+"&USERNAME=00357&PASSWORD=dhcc43\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    						}else{
    						    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    						}
    					}else{
    					    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    					}
    				}else if(t1>=0){
    				   //病历展示接口
    					if(columnValueSin.length == 1){						 
    						if(columnValueSin[0].length > 0){
    							title_text = columnValueSin[0].replace(/<font style=\"color:red;\">/g,"");
    							title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
    							title_text = title_text.replace(/s+/g,"");
    							if(analysisWay == 3 && (!(typeof(columnValue[docid_DocID])=="undefined") && !(typeof(columnValue[docid_VisitNumber]) =="undefined"))){
    								value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/medicalrecord/index?docid="+columnValue[docid_DocID][0]+"&hospitalNo="+"0001"+"&VisitNumber="+columnValue[docid_VisitNumber][0]+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    							}else if(analysisWay == 4){
    							//长海医院病历展示接口
    					        	if(columnValue[docid_VisitNumber][0].split("_").length == 2){
    					        		var nVisitID =columnValue[docid_VisitNumber][0].split("_")[1];
    								    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/CommonService/MedicalRecord/index?strPatientId="+columnValue[docid_PatientID][0]+"&strFileName="+columnValue[docid_DoctypeName][0]+"&nVisitID="+nVisitID+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    					        	}else{
    								     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></div>";
    								}
    							}else if(analysisWay == 5){
    							//大华医院病历展示
    								if((!(typeof(columnValue[docid_VisitNumber])=="undefined"))){
    						            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/medicalrecord/index?docid="+columnValue[docid_DocID][0]+"&hospitalNo="+columnValue[docid_HospitalNo][0]+"&VisitNumber="+columnValue[docid_VisitNumber][0]+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    								}else{
    								     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></div>";
    								}
    							}else if(analysisWay == 6){
    							//精卫医院病历展示接口
    								if((!(typeof(columnValue[docid_DocID])=="undefined"))){
    						            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/CommonService/medicalrecord/index?docid="+columnValue[docid_DocID][0]+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    								}else{
    								     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></div>";
    								}
    						     }else {
    							    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    							}
    						}else{
    							value = "&nbsp;&nbsp;&nbsp;&nbsp;";
    						}
    					}else if(columnValueSin.length > 1){
    						for ( var l = 0; l < columnValueSin.length; l++) {
    						   // title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"").replace(/<\/font>/g,"");
    						    title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
    							title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
    						    if(analysisWay == 3 && (!(typeof(columnValue[docid_DocID])=="undefined") && !(typeof(columnValue[docid_VisitNumber]) =="undefined"))){
    								value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/medicalrecord/index?docid="+columnValue[docid_DocID][l]+"&hospitalNo="+"0001"+"&VisitNumber="+columnValue[docid_VisitNumber][l]+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
    							}else if(analysisWay == 4){
    							//长海医院病历展示接口
    								if((!(typeof(columnValue[docid_VisitNumber])=="undefined")) && columnValue[docid_VisitNumber][l].split("_").length == 2){
    						            var nVisitID =columnValue[docid_VisitNumber][l].split("_")[1];
    									 value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/CommonService/MedicalRecord/index?strPatientId="+columnValue[docid_PatientID][l]+"&strFileName="+columnValue[docid_DoctypeName][l]+"&nVisitID="+nVisitID+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
    								}else{
    								     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></div>";
    								}
    							}else if(analysisWay == 5){
    							//大华医院病历展示
    								if((!(typeof(columnValue[docid_VisitNumber])=="undefined"))){
    						            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/medicalrecord/index?docid="+columnValue[docid_DocID][l]+"&hospitalNo="+columnValue[docid_HospitalNo][l]+"&VisitNumber="+columnValue[docid_VisitNumber][l]+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
    								}else{
    								     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></div>";
    								}
    							}else  if(analysisWay == 6){
    							//精卫医院病历展示接口
    								if((!(typeof(columnValue[docid_DocID])=="undefined"))){
    						            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\""+showDocRecordAddress+"/CommonService/medicalrecord/index?docid="+columnValue[docid_DocID][l]+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
    								}else{
    								     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></div>";
    								}
    						     }else if(analysisWay == 7){
    							//华西医院病例展示
    								if((!(typeof(columnValue[docid_VisitNumber])=="undefined"))){
    						            value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.0.42/trakcarelive/trak/web/csp/epr.newfw.OtherUsemain.csp?EpisodeID="+""+columnValue[docid_VisitNumber][l]+"&USERNAME=00357&PASSWORD=dhcc43\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
    								}else{
    								     value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></div>";
    								}
    							}else{
    							    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
    							}
    						}
    					}else{
    						//value = "测试词";
    						value = "&nbsp;&nbsp;&nbsp;&nbsp;";
    					}
    				}else if(t2>=0){
    				    //患者360视图
    				    if(columnValueSin.length == 1){		
    						if( columnValueSin[0].length > 0){
    						    var vn_value = "";
    				            if(typeof(columnValue[pa_VisitNumber])!="undefined"){
    				        	  vn_value = columnValue[pa_VisitNumber][0];
    				        	  vn_value = vn_value.replace(/<font style=\"color:red;\">/g,"");;
    				            	vn_value = vn_value.replace(/<\/font>/g,"");
    				            }
    				            var SYSTEM = "";
    				            if(typeof(columnValue[pa_OPorIP])!="undefined"){
    				            	SYSTEM = columnValue[pa_OPorIP][0];
    				            	SYSTEM = SYSTEM.replace(/<font style=\"color:red;\">/g,"");;
    				            	SYSTEM = SYSTEM.replace(/<\/font>/g,"");
    				            	if(SYSTEM == "门诊"){
    				            	   SYSTEM = "OP";
    				            	}else{
    				            	   SYSTEM = "IP";
    				            	}
    				            }
    							title_text = columnValueSin[0].replace(/<font style=\"color:red;\">/g,"");
    							title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
    						    if(analysisWay == 4){
    						        // 长海医院
    								vn_value=toBase64(vn_value);
    								vn_value=encodeURI(vn_value);			 
    						        value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://172.16.32.111:6008/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    						    }else if(analysisWay == 5){
    						   		//大华医院
    						   	 	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://202.202.202.182:6008/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    						    }else if(analysisWay == 6){
    						    	//精卫医院
    						    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://10.40.22.97:6004/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    						    }else if(analysisWay == 8){
    						    	//广西医院
    						    	vn_value=toBase64(vn_value);
    								vn_value=encodeURI(vn_value);	
    						    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.100.153:6601/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    						    }else if(analysisWay == 9){
    						    	//中西医
    						    	vn_value=toBase64(vn_value);
    								//vn_value=encodeURI(vn_value);	
    						    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.10.6.56:6008/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></a></div>";
    						    }else{
    						    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[0]+"</p></div>";
    						    }
    						}else{
    							value = "&nbsp;&nbsp;&nbsp;&nbsp;";
    						}
    					}else if(columnValueSin.length > 1){
    						for ( var l = 0; l < columnValueSin.length; l++) {
    							var vn_value = "";
    				            if(typeof(columnValue[pa_VisitNumber])!="undefined"){
    				        	  vn_value = columnValue[pa_VisitNumber][0];
    				        	  vn_value = vn_value.replace(/<font style=\"color:red;\">/g,"");;
    				            	vn_value = vn_value.replace(/<\/font>/g,"");
    				            }
    				            var SYSTEM = "";
    				            if(typeof(columnValue[pa_OPorIP])!="undefined"){
    				            	SYSTEM = columnValue[pa_OPorIP][0];
    				            	SYSTEM = SYSTEM.replace(/<font style=\"color:red;\">/g,"");;
    				            	SYSTEM = SYSTEM.replace(/<\/font>/g,"");
    				            	if(SYSTEM == "门诊"){
    				            	   SYSTEM = "OP";
    				            	}else{
    				            	   SYSTEM = "IP";
    				            	}
    				            }
    						    title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
    							title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
    							if(analysisWay == 4){
    								 // 长海医院
    								vn_value=toBase64(vn_value);
    								vn_value=encodeURI(vn_value);			 
    						        value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://172.16.32.111:6008/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";		 
    						    
    						    }else if(analysisWay == 5){
    						   		//大华医院
    						   	 	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://202.202.202.182:6008/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
    						    }else if(analysisWay == 6){
    						    	//精卫医院
    						    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://10.40.22.97:6004/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
    						    }else if(analysisWay == 8){
    						    	//广西医院
    						    	vn_value=toBase64(vn_value);
    								vn_value=encodeURI(vn_value);	
    						    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.168.100.153:6601/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
    						    }else if(analysisWay == 9){
    						    	//中西医
    						    	vn_value=toBase64(vn_value);
    								//vn_value=encodeURI(vn_value);	
    						    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a href=\"http://192.10.6.56:6008/FormManager/Display?id=16&VN="+vn_value+"&SYSTEM="+SYSTEM+"\" rel=\"external\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></a></div>";
    						    }else{
    						    	value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p style=\"text-decoration: underline;color: #3385ff;\">"+columnValueSin[l]+"</p></div>";
    						    }
    						}
    					}else{
    						//value = "测试词";
    						value = "&nbsp;&nbsp;&nbsp;&nbsp;";
    					}
    				}else  
    				  if(t4>=0){
    					if(columnValueSin.length == 1){						 
    							if(columnValueSin[0].length > 0){
    								//var age = Number(columnValueSin[0]);
    								var age =columnValueSin[0];
    							    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+age+"\" style=\"margin: auto;width:"+"60px"+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+age+"</p></div>";
    							}else{
    								value = "&nbsp;&nbsp;&nbsp;&nbsp;";
    							}
    						}else if(columnValueSin.length > 1){
    							for ( var l = 0; l < columnValueSin.length; l++) {
    						    //var age = Number(columnValueSin[l]);
    						    var age =columnValueSin[0];
    							   value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+age+"\" style=\"margin: auto;width:"+"60px"+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+age+"</p></div>";
    							}
    						}else{
    							//value = "测试词";
    							value = "&nbsp;&nbsp;&nbsp;&nbsp;";
    						}
    				}
    				else{
    					if(columnValueSin.length == 1){
    						if(columnValueSin[0].length > 0){
    							title_text = columnValueSin[0].replace(/<font style=\"color:red;\">/g,"");
    							title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
    							
    							title_text=title_text.replace("&#x0D;", "");
    							title_text=title_text.replace(/<\/?.+?>/g, "");
    							if(columnNamesKey.indexOf("fact_pa_patinfoVisitRecord_CurrentAge")>=0){
    								value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+"60px"+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+columnValueSin[0]+"</p></div>";
    							}else if(columnNamesKey.indexOf("PatinfoVisitRecord_SexName")>=0){
    							    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+"40px"+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+columnValueSin[0]+"</p></div>";
    							}else if(showValue.indexOf(columnNamesKey)>=0){
    							    value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><u><p>"+columnValueSin[0]+"</p></u></a></div>";
    							}else{
    								if(title_text.length > 25){
    							      value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><u><p>"+columnValueSin[0]+"</p></u></a></div>";
    								}else{
    								  value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+columnValueSin[0]+"</p></div>";
    								}
    							}
    						}else{
    							value = "&nbsp;&nbsp;&nbsp;&nbsp;";
    						}
    					}else if(columnValueSin.length > 1){
    						if(columnNamesKey.indexOf("fact_pa_patinfoVisitRecord_CurrentAge")>=0){
    							for ( var l = 0; l < columnValueSin.length; l++) {
    								title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
    								title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
    								title_text=title_text.replace(/<\/?.+?>/g, "");
    								value = value+"<div class=\"th-inner\" id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+"60px"+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+columnValueSin[l]+"</p></div>";
    							}
    						}else if(columnNamesKey.indexOf("PatinfoVisitRecord_SexName")>=0){
    							for ( var l = 0; l < columnValueSin.length; l++) {
    								title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
    								title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
    								title_text=title_text.replace(/<\/?.+?>/g, "");
    								value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+"40px"+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+columnValueSin[l]+"</p></div>";
    							}
    						}else if(showValue.indexOf(columnNamesKey)>=0){
    							  for ( var l = 0; l < columnValueSin.length; l++) {
    								title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
    								title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
    								
    							    title_text=title_text.replace("&#x0D;", "");
    							    title_text=title_text.replace(/<\/?.+?>/g, "");
    								value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><u><p>"+columnValueSin[l]+"</p></u></a></div>";
    							}
    						}else{
    							for ( var l = 0; l < columnValueSin.length; l++) {
    								title_text = columnValueSin[l].replace(/<font style=\"color:red;\">/g,"");
    								title_text = title_text.replace(/<\/font>/g,"");
						title_text = htmlEscape(title_text);
    								
    							    title_text=title_text.replace("&#x0D;", "");
    							    title_text=title_text.replace(/<\/?.+?>/g, "");
    								if(title_text.length > 25){
    								  value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><a onclick = \"showValue('"+title_text+"')\" rel=\"external\"><u><p>"+columnValueSin[l]+"</p></u></a></div>";
    								}else{
    								  value = value+"<div id=\"table_show_div_"+columnNames[columnNamesKey].searchTableCN+"."+columnNames[columnNamesKey].displayName+"_"+serchType+"_"+empiid+"_"+columnValue["VKey"][0]+"\" title=\""+title_text+"\" style=\"margin: auto;width:"+length+";height:30px;text-align:center;line-height:30px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;\"><p>"+columnValueSin[l]+"</p></div>";
    								}
    							}
    						}
    					}else{
    						//value = "测试词";
    						value = "&nbsp;&nbsp;&nbsp;&nbsp;";
    					}
    				}
    	                row['field' + columnNamesKey] =value;
    			}
        	}
            data.push(row);
        }
    }
}

function getCount(){
	$("#calcTotalRecords").hide();
	$("#searchForm").ajaxSubmit({
	     type: "post",
	     url: "/hssp/rest/getCount",
	     dataType: "json",
	     success: function(data){
   	    	 if(data.responseStatu == "0000"){
   	    		 $("#maxPageNum").val(data.pageCount);
   	    		 
   	    		var calcTotalRecords = "从"+data.recordTotal+"条记录中找到"+data.dataCount+"条,"
   	    		 $("#maxData_Id").val(data.dataCount);
   	    		 $("#calcTotalRecords").html(calcTotalRecords);
   	    		 $("#calcTotalRecords").show();
   	    		 $("#countShow").html(data.pageCount);
   	    		 $("#pageCount").show();
   	    		/*if (Number($("#pageNum_Id").val()) == 1) {
					$("#pageLast").hide();
					$("#pageLast__False").show();
		   		}else{
		   			$("#pageLast").show();
		   			$("#pageLast_False").hide();
		   		}
		   		if (Number($("#pageNum_Id").val()) < Number(data.pageCount)) {
		   			$("#pageNext").show();
		   			$("#pageNext_False").hide();
		   		}else{
		   		    $("#pageNext").hide();
		   		    $("#pageNext_False").show();
		   		}*/
		   		$("#pageCount").show();
			    $("#pageCount_id").show();
   	    	 }else{
   	    		//getCount();
   	    		alert("获取页面参数失败!");
   	    	 }
	    	 },error:function(e){
	   	    		parent.location.reload();    	    
				}
	     });
}
function getSort(){
	getURL();
	$("#sort").hide();
	$("#result_Logo").html("");
	$("#searchForm").ajaxSubmit({
	     type: "post",
	     url: "/hssp/rest/GetSortList",
	     dataType: "json",
	     success: function(datas){
	    	 $("#result_Logo").html(datas.result_Logo);
   	    	 if(datas.responseStatu == "0000"){
   	    		for(var sortBy in datas.sortMap){
   	    			$("#sortBy_id").append("<option value='"+sortBy+"'>"+datas.sortMap[sortBy]+"</option>"); 
   	    		}
   	    		$("#sort").show();
   	    	 }else{
   	    		//getCount();
   	    		alert("获取排序字段失败!");
   	    	 }
	    	 },error:function(e){
	   	    		parent.location.reload();    	    
				}
	     });
}

function ajaxFormSearch(){
	$("#wait_show").html("");
	ajaxForm(false);
}
//上一页
function ajaxFormLast(){
	if (diseaseFilterOk) {
		saveDiseaseVkey();
	}
	$("#pageNum_Id").val(Number($("#pageNum_Id").val())-1)
	$("#searchWay_id").val(1)
	field_number = field_number - 20;
	ajaxFormSearch();
}
//下一页
function ajaxFormNext(){
	if (diseaseFilterOk) {
		saveDiseaseVkey();
	}
	$("#searchWay_id").val(2)
	$("#pageNum_Id").val(Number($("#pageNum_Id").val())+1)
	ajaxFormSearch();
}

//跳页
function ajaxFormJump(){
	if (diseaseFilterOk) {
		saveDiseaseVkey();
	}
	var pageNum = 1;
	if(Number($("#pageNum_Jump_Id").val()) <= 0){
	    parent.$.dialog("请输入正确的页数!", false, 1000);
	    return ;
	}else if(Number($("#pageNum_Jump_Id").val()) >　Number($("#maxPageNum").val())　){
	    parent.$.dialog("请输入正确的页数!", false, 1000);
	    return;
	}else{
		if ($("#pageNum_Jump_Id").val()!="" && Number($("#pageNum_Jump_Id").val())<=Number($("#maxPageNum").val())) {
			pageNum = $("#pageNum_Jump_Id").val();
			field_number = (pageNum-1) * 10+1;
			$("#pageNum_Id").val(pageNum);
			ajaxFormSearch();
		}
	}
	
}
//下拉框选择
function sortOrderByChange(){
	$("#sortBy_id_form").val($("#sortBy_id").val());
	judgeReturnOk("1");
	//清空勾选信息
	diseaseFiltermaps = {};
	search();
}
function sortByChange(){
	$("#sortOrderBy_id_form").val($("#sortOrderBy_id").val());
	judgeReturnOk("1");
	//清空勾选信息
	diseaseFiltermaps = {};
	search();
}
//function onlySearch(){
	//$("#searchType_id").val("2");
	//resetSearch();
//}
//重置页面搜索
function resetSearch(){
	if ($("#txtKeyword").val().trim() == "")
	{
		parent.$.dialog("搜索条件不能为空!请重新输入!", false, 1500);
	}
	else
	{
		//时间筛选重置
		$("#limit_time_text").html("时间筛选");
    	$("#limit_time_start_Id").val("1");
    	$("#limit_time_end_Id").val("");
		//检索指定重置
		//$("#time_select_3_function option:first").prop("selected", 'selected'); 
		//$("#limit").val("");
		//检索返回字段重置
		$("#return_search_form_id").val("1");
		//排序字段重置
		$("#sortBy_id option:first").prop("selected", 'selected');
		$("#sortOrderBy_id option:first").prop("selected", 'selected');
		$("#expression").val($("#txtKeyword").val());
		$("#expression_old_id").val($("#txtKeyword").val());
		$("#return_search_ok_id").val("0");
		//返回字段列别是否重置
		solrDBFieldInfoOk = true;
		//保存病种库数据过滤
		diseaseFilterOk = false;
		//数据对比
		compareOk = false;
		//统计图重置
		groupNumstatic =-1;
		CheckSearchPlanOK = false;
		$('#showSearchPlan_table_id').bootstrapTable('destroy');
		search();
		updateDBFieldInfo();
	}
}
//点击搜索事件
function search(){
	//给后台参数传值
	$("#txtKeyword").val($("#expression_old_id").val());
	var searchText = $("#txtKeyword").val();
	field_number= 1;
	if(searchText.length > 0){
		$("#expression").val(searchText);
		$("#sortBy_id_form").val($("#sortBy_id").val());
		$("#sortOrderBy_id_form").val($("#sortOrderBy_id").val());
		$("#getCount_requestID").val("");
		$("#pageNum_Jump_Id").val("");
		$("#maxPageNum").val("");
		$("#pageNum_Id").val("1");
		$("#pageCount").hide();
		$("#calcTotalRecords").hide();
		$("#calcTimeTick_text").hide();
		$("#wait_show").html("搜索中,请稍后...");
		ajaxForm(true);
	}
	parent.prebodyDocument.addHistoryExpression(searchText);
}
var groupNum = 0;
//当次搜索用户行为记录是否关闭，0开启，1关闭
function judgeReturnOk(value){
	var returnOk = $("#return_search_ok_id").val();
	if(returnOk == "0"){
		$("#return_search_ok_id").val(value);
	}
}
var startTimeStatic = new Date();
function getFormatTime(){
  var dateTime = new Date();
  var month = dateTime.getMonth()+1;
  var dateSt = dateTime.getDate();
  if(month >=1 &&　month<= 9　){
 	 month	= "0"+month;
  }
  if(dateSt >=1 &&　dateSt<= 9　){
 	 dateSt	= "0"+dateSt;
  }
  
  var hourSt = dateTime.getHours();
  if(hourSt >=1 &&　hourSt<= 9　){
 	 hourSt	= "0"+hourSt;
  }
  
  var minutesSt = dateTime.getMinutes();
  if(minutesSt >=1 &&　minutesSt<= 9　){
 	 minutesSt	= "0"+minutesSt;
  }
  
  var secondsSt = dateTime.getSeconds();
  if(secondsSt >=1 &&　secondsSt<= 9　){
 	 secondsSt	= "0"+secondsSt;
  }
  var milliseconds = dateTime.getMilliseconds();
  if(milliseconds >=1 &&　milliseconds<= 9　){
 	 milliseconds	= "00"+milliseconds;
  }else if(milliseconds >=10 &&　milliseconds<= 99){
     milliseconds	= "0"+milliseconds;
  }
  var dateSt = dateTime.getFullYear()+"-"+month+"-"+dateSt+" "+
			   hourSt+":"+minutesSt+":"+
			   secondsSt+" "+milliseconds;
  $("#fromPageDate_id").val(dateSt);
}
var select_searc_Type_value = "VKey";
function ajaxForm(firstPage){
	//window.parent.resetResultHeight();
	//$("#time").hide();
	//$("#count").hide();
	//$("#pageCount").hide();
	//$("#calcTotalRecords").hide();
	//$("#calcTimeTick_text").hide();
	//if($("#pageNum_Id").val()==1){
	  //$("#getCount_requestID").val("");
	//}
	//$("#pageCount_id").hide();
	//搜索词准备
	if ($("#txtKeyword").val().trim() == "")
	{
		parent.$.dialog("搜索条件不能为空!请重新输入!", false, 1500);
		return;
	}
	//$("#expression").val($("#txtKeyword").val());
	
	var startTime_limit = $("#limit_time_start_Id").val();
	var startTime_limit_ok=0;
	var timespace_Start = "";
	var timespace_End = "";
	if(startTime_limit != "1"){
		var endTime_limit = $("#limit_time_end_Id").val();
		var oldText = $("#expression_old_id").val();
		timespace_Start = startTime_limit;
		timespace_End = endTime_limit;
		startTime_limit_ok = 1;
		var timespace = "[患者基本信息.就诊日期]>"+startTime_limit+"and"+"[患者基本信息.就诊日期]<"+endTime_limit;
		//oldText = oldText +"and"+timespace;
		$("#expression").val(oldText);
		judgeReturnOk("1");
	}else{
		judgeReturnOk("0");
		var oldText = $("#expression_old_id").val();
		while(true){
			if(oldText.indexOf("]]") > 0){
				var startNum = oldText.indexOf("]]");
				var endNum = 0;
				var addNum =1;
				while(true){
					var judgeSt = oldText.substring(startNum+addNum-1,startNum+addNum);
					if(judgeSt != "]"){
						endNum = startNum+addNum-1;
						break;
					}
					if(oldText.length >= startNum+addNum+1){
						addNum++;
					}else{
						endNum = startNum+addNum-1;
						break;
					}
				}
				oldText = oldText.substring(0,startNum)+oldText.substring(endNum,oldText.length);
			}else{
				break;
			}
		}
		$("#expression").val(oldText);
		$("#expression_old_id").val(oldText);
		$("#txtKeyword").val(oldText);
	}
	var return_search_Field = $("#return_search_form_id").val();
	if(return_search_Field != "1"){
		//var oldText = $("#expression_old_id").val();
		//if(startTime_limit != "1"){
		  //  oldText = $("#expression").val();
		//}
		//oldText = oldText +"and"+return_search_Field;
		//$("#expression").val(oldText);
		judgeReturnOk("1");
	}else{
		judgeReturnOk("0");
	}
	//搜素等待图片展示
	$("#wait").show();
	startTimeStatic = new Date();
	//$("#fromPageDate_id").val(startTimeStatic);
	getFormatTime();
	$("#userID_id").val($("#hideUserIdentity", parent.document).val());
	$("#pageLast").attr("disabled",true);
    $("#pageNext").attr("disabled",true);
    select_searc_Type_value = $("#select_searc_Type_id").val();
    $("#showSearchPlan_div_id").hide();
	if(firstPage){
		$("#operation_show").hide();
		$("#table_show").hide();
		$("#operation_iframe").attr("src","")
		$("#action_bar").hide();
		$("#table_error_show").hide();
		$("#pageCount").hide();
		$("#pageCount_id").hide();
		$("#searchForm").ajaxSubmit({
		     type: "post",
		     url: "/hssp/rest/solrQure"+"?Identity="+$("#hideUserIdentity",parent.document).val()+"&searchByType="+select_searc_Type_value+"&limitTimeOK="+startTime_limit_ok
		    		 +"&limitTimeStart="+startTime_limit+"&limitTimeEnd="+endTime_limit,
		     dataType: "json",
		     success: function(data){
		    	 if(data.responseStatu == "5000"){
			    		//$("#operation_show").load("http://219.233.194.20:6002/FormManager/Display?id=104");
			    		$("#operation_iframe").attr("src",data.operationParam)
			    		var windows_height = $(window).height();
						$("#operation_iframe").attr("height",windows_height);
			    		$("#operation_show").show();
			    		//$("#searchButton").attr("onclick","onlySearch();");
			    		//$("#searchButton").val("搜 医 疗");
			    		//$("#searchType_id").val("2");
			    		$("#wait").hide();
			    		$("#showChartShow").hide();
			    	}else if(data.responseStatu == "0000"){
			    		$("#action_bar").show();
			    		$("#table_show").show();
			    		 //保存当次请求数据
				    	 $("#searchDatas_id").val(JSON.stringify(data));
			    		 buildTable($('#table'),data,startTimeStatic);
			    		 // getCount();
			    		 //$("i[class='glyphicon glyphicon-th icon-th']").html("隐藏");
			    		 //$("i[class='glyphicon glyphicon-th icon-th']").removeClass("glyphicon-th");
			    		 //统计图准备
			    		 judgeShowChart(data);
			    		 $("#wait").hide();
			    		 $("#showSearchPlan_div_id").show(1000);
			    	}else{
			    		if(data.responseStatu == "9999"){
			    		  $("#getCount_requestID").val(data.requestID);
			       		  $("#showSearchPlan_div_id").show(500);
				       	}
			    		$("#action_bar").show();
		    		    showErrorChart(data); 
				        $("#wait").hide();
			       }
		     },error:function(e){
	   	    		parent.location.reload();    	    
				}
		 });
	}else{
		$("#searchForm").ajaxSubmit({
		     type: "post",
		     url: "/hssp/rest/jumpPages"+"?Identity="+$("#hideUserIdentity",parent.document).val()+"&searchByType="+select_searc_Type_value,
		     dataType: "json",
		     success: function(data){
		    	 $("#wait").hide();
		    	 //保存当次请求数据
		    	 $("#searchDatas_id").val(JSON.stringify(data));
	    		 buildTable($('#table'),data,startTimeStatic);
	    		 $("#action_bar").show();
	    		 $("#table_show").show();
	    		 $("#pageCount").show();
	    		 $("#pageCount_id").show();
	    		//统计图准备
	    		 judgeShowChart(data);
		     },error:function(e){
	   	    		parent.location.reload();    	    
				}
		 });
	}
	
}

function showErrorChart(datas){
	columns = [];
	data = [];
	 columns.push({
	         field: 'field' + "errorMessage",
	      });
	 var row = {};
	 if(datas.responseStatu == "9999"){
		 row['field' + "errorMessage"] = datas.responseDescription;
	 }else{
		 row['field' + "errorMessage"] = "检索失败,请检查您的检索条件或者联系系统管理员！";
	 }
   	 data.push(row);
   	$('#table_error').bootstrapTable('destroy').bootstrapTable({
   	        columns: columns,
   	        data: data
   	    });
	$("#table_error_show").show();
	
}
	
function judgeShowChart(data){
	if(data.responseStatu == "0000"){
		 if((data.statisticsNum > 0 && data.chartShow ) 
		 && (data.pageNum == 1 || groupNumstatic != data.groupNum) && !diseaseFilterOk){
			showChart_container_num = 0;
           var i=0;
           var max=data.statisticsNum;
		    var html_text=""; 
			 for(i=0;i<max;i++){
				 html_text = html_text + "<div id=\"container_"+i+"_swait\"  style=\"min-width:700px;height:100px;text-align:center;\"><img src=\"${pageContext.request.contextPath}/resources/images/waiting.gif\">统计图生成中,请稍后...</div>";
				 html_text = html_text + "<div id=\"container_"+i+"\" style=\"min-width:700px;height:400px;display:\"none\"\"></div>";
			 }
			 $("#showChart").html(html_text);
			 var windows_height = $(window).height();
			$("#showChart").css({
				height:windows_height,
			 });
			$("#floatDiv").css({
				height:windows_height,
			 });
			 $("#showChartShow").show();
			 $("#statisticsNum_Id").val(data.statisticsNum);
			 $("#groupNum_Id").val(data.groupNum);
		   $("#countDrawFileName_Id").val(""); 
		   groupNum = data.groupNum;
		  // showChart();
		 }else{
			 $("#showChartShow").hide();
		 }
	 }else{
		 if(data.chartShow && !diseaseFilterOk){
			 $("#showChartShow").show();
		 }else{
			 $("#showChartShow").hide();
		 }
	 }
}

//时间过滤操作事件
function selectChange(value,limit_time_text){
	var myDate = new Date();
	var endYeartext = myDate.getFullYear(); 
	var  startYeartext = myDate.getFullYear(); 
	var endmonthtext = myDate.getMonth()+1;
	var startmonthtext = myDate.getMonth()+1;
	var enddaytext = myDate.getDate();
	var startdaytext = myDate.getDate();
	
	var startTime = "";
	var endTime = "";
	
    if(value == 0){
        $("#limit_time_text").html(limit_time_text);
    	$("#limit_time_start_Id").val("1");
    	$("#limit_time_end_Id").val("");
    	search();
    	return;
    }else {
	    $("#limit_time_text").html(limit_time_text);
	    
    	var year_subtraction = 0;
    	var month_subtraction = 0 ;
    	
        year_subtraction = Math.floor(value/12);
    	startYeartext = startYeartext - year_subtraction;
        month_subtraction = value%12;
    	startmonthtext = startmonthtext - month_subtraction;
    }
    if(startmonthtext<0){
    	startYeartext = startYeartext -1;
    	startmonthtext = startmonthtext + 12
    }
    if(startmonthtext<10){
    	startmonthtext = "0"+""+startmonthtext
	}
    if(endmonthtext<10){
    	endmonthtext = "0"+""+endmonthtext
	}
	if(startdaytext<10){
		startdaytext = "0"+""+startdaytext
	}
	if(enddaytext<10){
		enddaytext = "0"+""+enddaytext
	}
	startTime = startYeartext+""+startmonthtext+""+startdaytext;
	endTime = endYeartext+""+endmonthtext+""+enddaytext;
	$("#limit_time_start_Id").val(startTime);
	$("#limit_time_end_Id").val(endTime);
	//changeSearchText(startTime, endTime);
	//情况记录的勾选信息
	diseaseFiltermaps = {};
	search();
}
//加入时间限制条件
function changeSearchText(startTime,endTime) {
	var oldText = $("#txtKeyword").val();
	var timespace = "[患者基本信息.就诊日期]>"+startTime+"and"+"[患者基本信息.就诊日期]<"+endTime;
	oldText = oldText +"and"+timespace;
	if(oldText.replace(" ","").length > 0){
		//$("#txtKeyword").val(oldText);
	    $("#expression").val(oldText);
		$("#sortBy_id_form").val($("#sortBy_id").val());
		$("#sortOrderBy_id_form").val($("#sortOrderBy_id").val());
		$("#getCount_requestID").val("");
		$("#pageNum_Jump_Id").val("");
		$("#maxPageNum").val("");
		$("#limit").val("");
		$("#pageNum_Id").val("1");
		$("#wait").show();
		ajaxForm(true);
	}
	
}


function time_select_2_submit(){
	var startTime = $("#select_Start").val();
	var endTime = $("#select_End").val();
	var startTimeOk = getdate("select_Start");
	var endTimeOk = getdate("select_End");
	if(startTimeOk){
		if(endTimeOk){
	        if(compareDate(startTime, endTime)){
	        	return;
	        }
			$("#limit_time_text").html(startTime+"～"+endTime);
			$("#limit_time_start_Id").val(startTime);
	        $("#limit_time_end_Id").val(endTime);
	        diseaseFiltermaps = {};
			search();
			//changeSearchText(startTime, endTime);
	    }else{
	    	$("#select_End").val("yyyy-MM-dd");
	    }
	}else{
		$("#select_Start").val("yyyy-MM-dd");
	}
}

function compareDate(checkStartDate, checkEndDate) {      
     var arys1= new Array();      
     var arys2= new Array();      
	 if(checkStartDate != null && checkEndDate != null) {      
	     arys1=checkStartDate.split('-');      
	     var sdate=new Date(arys1[0],parseInt(arys1[1]-1),arys1[2]);      
	     arys2=checkEndDate.split('-');      
	     var edate=new Date(arys2[0],parseInt(arys2[1]-1),arys2[2]);      
		 if(sdate > edate) {      
		    alert("日期开始时间大于结束时间");         
		    return true;         
		 }  else {   
		    return false;      
		 }   
	  }else{
		  alert("时间不可以为空！");		  
		  return true;        
	  }
 } 


function time_select_2_return(){
	$('#time_select_2').hide();
	$('#time_select_1').show();
}
/*$(function(){
	$('#select_Start').datepicker({
		dateFormat:'yymmdd'
		});
	$('#select_End').datepicker({
		dateFormat:'yymmdd'
		});
	
});*/
//情况文本框内容
var text ;
$("#select_Start").focus(function() { // 获得焦点事件
    text= $(this).val();
    $(this).val("");
});
$("#select_End").focus(function() { // 获得焦点事件
    text= $(this).val();
    $(this).val("");
});
$("#select_Start").blur(function() {  // 失去焦点事件
    $(this).val()!="" || $(this).val(text);   // 如果文本框为空，那就设置之前定义的text变量值
});
$("#select_End").blur(function() {  // 失去焦点事件
    $(this).val()!="" || $(this).val(text);   // 如果文本框为空，那就设置之前定义的text变量值
});
function IsValidDate(id){
	var isok = getdate(id);
	if(!isok){
		$('#'+id).val("yyyy-MM-dd");
	}
}
function getdate(id){ 
	var strInputDate = $('#'+id).val();
	// 定义一个月份天数常量数组
	var DA = [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
	
	// 统一日期格式
	strDate = strInputDate.replace(/-/g, "/");
	
	//判断日期是否是预期的格式
	if (strDate.indexOf("/") == -1) {
	  return false;
	}
	
	// 分解出年月日
	arrD = strDate.split("/");
	if (arrD.length != 3) return false;
	y = parseInt(arrD[0], 10);
	m = parseInt(arrD[1], 10);
	d = parseInt(arrD[2], 10);
	
	//判断年月日是否是数字
	if (isNaN(y) || isNaN(m) || isNaN(d)) return false;
	
	// 判断月份是否在1-12之间
	if (m > 12 || m < 1) return false;
	//判断是否是闰年
	if (isLoopYear(y)) DA[2] = 29;
	
	//判断输入的日是否超过了当月月份的总天数。
	if (d > DA[m]) return false;
	
	//各种条件都验证了，则应该是一个合法的日期了。
	// 如果要对日期进行一次格式化，则可以在这里进行处理了，下面格式化成数据库识别的日期格式 yyyy-MM-dd
	// str = y + "-" + (m<10?"0":"") + m + "-" + (d<10?"0":"") + d;
	str = y + "-" + (m < 10 ? "0" : "") + m + "-" + (d < 10 ? "0" : "") + d;
	$('#'+id).val(str);
	return true;
}
function isLoopYear(theYear) {
	  return (new Date(theYear, 1, 29).getDate() == 29);
}
function getShowView(DocId){
	$.ajax(
			{
				type : "post",
				url : "/hssp/body/showView",
				data : "DocId=" + DocId,
				dataType : "json",
				success : function(json)
				{
					var responseShowView = eval(json.responseShowView);
					if(responseShowView.responseStatu == "0000"){
						var analysisWay = $("#analysisWay_Id").val();
						if(analysisWay == 1){
						   document.getElementById("showView").innerHTML =responseShowView.html;
						}else if(analysisWay == 2){
							$("#showView_span").text(responseShowView.html);
						}else{
							return;
						}
						var windows_height = $(window).height();
						$("#showView").css({
							height:windows_height,
						 });
						
						showBg();
					}else{
						document.getElementById("showView").innerHTML =responseShowView.responseDescription;
						showBg();
					}
				},error:function(e){
	   	    		parent.location.reload();    	    
				}
			});
}
//统计图  饼-8 柱-4  横-17
var showChart_container_num = 0;

var groupNumstatic =-1;
function getDrawDataStart(){
	var num = Number($("#groupNum_Id").val());
    if(groupNumstatic != num){
	$("#searchForm").ajaxSubmit({
	     type: "post",
	     url: "/hssp/rest/getDrawDataStart",
	     dataType: "json",
	     success: function(data){   
	    	 showChart(); 
	    	 groupNumstatic = num;
	     },error:function(e){
	    		parent.location.reload();    	    
			}
	 });
    }
}
function showChart(){
	
	//show_chart.abort();
	//第一步请求后台数据
    $("#searchForm").ajaxSubmit({
	     type: "post",
	     url: "/hssp/rest/getDrawData",
	     dataType: "json",
	     success: function(data){
	    	 
	    	 
	     
	    	 if(data.responseStatu == "0000"){
	    		//更新数据
	    		$("#countDrawFileName_Id").val(data.countDrawFileName);
	    		//生成统计图
	    		var csvBeans = data.csvBeans;
	    		for ( var i = 0; i < csvBeans.length; i++) {
	    			var csvBean = csvBeans[i];
	    			var fileName = csvBean.fileName;
	    			var headers = csvBean.headers;
	    			var listDatas = csvBean.listData;
					//拿到表类型
					var type = fileName.replace(".csv","").split("-")[1];
					//if(fileName.replace(".csv","").split("-")[0] == "年龄"){
					  //type = "8";
					//}
					if(type == "8"){
						//饼状图
						var title_text = headers[0];
						//数据准备 格式[{name:'0',y:0.1},{name:'5',y:0.1}]
						var listData_name = listDatas[0];
						var listData_y = listDatas[1];
						var	series="";
						for ( var j = 0; j < listData_name.length; j++) {
							if(j==0){
								series = "{name:\'"+listData_name[j].replace(/'/g,"").replace("\\","")+"\',y:"+listData_y[j]+"}";
							}else{
								series = series+",{name:\'"+listData_name[j].replace(/'/g,"")+"\',y:"+listData_y[j]+"}";
							}
						}
						series = "["+series+"]";
						var container = "container_"+showChart_container_num;
						$("#"+container+"_swait").hide();
						$("#"+container).show();
						showPie(title_text,series,container);
						showChart_container_num++;
					}else if(type == "4"){
						//柱状图
						var title_text=headers[0];
						var xAxis_title_text=headers[0];
						var yAxis_title_text=headers[1];
						var xAxis_categories_data="";
						var series_data="";
						var series_data_data="";
						var listData_name = listDatas[0];
					    var listData_y = listDatas[1];
					    var maxCount = Number($("#maxData_Id").val());
					    //var maxCount = 100;
						for ( var j = 0; j < listData_name.length; j++) {
								if(j==0){
									xAxis_categories_data = "'"+listData_name[j]+"～"+listData_name[j+1]+"'";
									//series_data_data = (Math.round(listData_y[j]*maxCount*100/100));
									series_data_data = (listData_y[j]*maxCount).toFixed(0);
								}else if(j==(listData_name.length-1)){
									xAxis_categories_data = xAxis_categories_data+",'"+listData_name[j]+"～*"+"'";
									//series_data_data = series_data_data+","+(Math.round(listData_y[j]*maxCount*100/100));
									series_data_data = series_data_data+","+(listData_y[j]*maxCount).toFixed(0);
								}else{
									xAxis_categories_data = xAxis_categories_data+",'"+listData_name[j]+"～"+listData_name[j+1]+"'";
									//series_data_data = series_data_data+","+(Math.round(listData_y[j]*maxCount*100/100));
									series_data_data = series_data_data+","+(listData_y[j]*maxCount).toFixed(0);
								}
						}
						xAxis_categories_data = "["+xAxis_categories_data+"]";
						series_data_data = "["+series_data_data+"]";
						series_data = "[{name:'"+yAxis_title_text+"',data:"+series_data_data+"}]";
						var container = "container_"+showChart_container_num;
						$("#"+container+"_swait").hide();
						$("#"+container).show();
						showColumn(container,title_text, xAxis_title_text, yAxis_title_text, xAxis_categories_data, series_data);
						showChart_container_num++;
					}else if(type == "17"){
						//横向柱状图
						var title_text=headers[0];
						var xAxis_title_text=headers[0];
						var yAxis_title_text=headers[1];
						var xAxis_categories_data="";
						var series_data="";
						var series_data_data="";
						var listData_name = listDatas[0];
					    var listData_y = listDatas[1];
					    var maxCount = Number($("#maxData_Id").val());
						for ( var j = 0; j < listData_name.length; j++) {
								if(j==0){
									xAxis_categories_data = "'"+listData_name[j]+"～"+listData_name[j+1]+"'";
									series_data_data = (Math.round(listData_y[j]*maxCount));
								}else if(j==(listData_name.length-1)){
									xAxis_categories_data = xAxis_categories_data+",'"+listData_name[j]+"～*"+"'";
									series_data_data = series_data_data+","+(Math.round(listData_y[j]*maxCount));
								}else{
									xAxis_categories_data = xAxis_categories_data+",'"+listData_name[j]+"～"+listData_name[j+1]+"'";
									series_data_data = series_data_data+","+(Math.round(listData_y[j]*maxCount));
								}
						}
						xAxis_categories_data = "["+xAxis_categories_data+"]";
						series_data_data = "["+series_data_data+"]";
						series_data = "[{name:'"+yAxis_title_text+"',data:"+series_data_data+"}]";
						var container = "container_"+showChart_container_num;
						$("#"+container+"_swait").hide();
						$("#"+container).show();
						showBar(container,title_text, xAxis_title_text, yAxis_title_text, xAxis_categories_data, series_data);
						showChart_container_num++;
					}else{
						//报错数据
						var container = "container_"+showChart_container_num;
						$("#"+container+"_swait").hide();
						$("#"+container).hide();
						showChart_container_num++;
					}
				}
	    		setTimeout("showChart();", 5000);
	    	 }else if(data.responseStatu == "9990"){
	    		 return;
	    	 }else{
	    		 setTimeout("showChart();", 5000);
	    		 //showChart();
	    	 }
	     },error:function(e){
	    		parent.location.reload();    	    
			}
	 });
}
//条形图
function showBar(container,title_text,xAxis_title_text,yAxis_title_text,xAxis_categories_data,series_data) {  
	//图表类型
	var chart_type="bar";
	//图表名称
	//var title_text="年龄分布图";
	title_text = title_text+"分布图";
	//图表副标题  
	var subtitle_text="";
	//x轴
	//范围值
	var xAxis_categories = eval(xAxis_categories_data);
	//var xAxis_categories = ['0','5','10','15','20','25','30','35','40','45','50','55'];
	//标题
	//var xAxis_title_text = "年龄";
	//y轴
	//最小值开始位置
	var yAxis_min = 0;
	//名称
	//var yAxis_title_text = "频数";
	//柱状图参数
	var series = eval(series_data);
	/*var series = [{
				   name: '频数1',
				   data: [0.0002*3341759,0.0005*3341759,0.0015*3341759,0.0065*3341759,0.0124*3341759,0.0301*3341759,0.0464*3341759,0.0639*3341759,0.0962*3341759,0.1188*3341759,0.1292*3341759,0.1571*3341759]
				 },{
					   name: '频数2',
					   data: [0.0002*3341759,0.0005*3341759,0.0015*3341759,0.0065*3341759,0.0124*3341759,0.0301*3341759,0.0464*3341759,0.0639*3341759,0.0962*3341759,0.1188*3341759,0.1292*3341759,0.1571*3341759]
					 }];*/
	
    $('#'+container).highcharts({                                         
        chart: {                                                           
            type: chart_type                                                    
        },                                                                 
        title: {                                                           
            text: title_text                    
        },                                                                 
        subtitle: {                                                        
            text: subtitle_text                                  
        },                                                                 
        xAxis: {                                                           
            categories:xAxis_categories,
            title: {                                                       
                text: xAxis_title_text                                                 
            }                                                              
        },                                                                 
        yAxis: {                                                           
            min: yAxis_min,                                                        
            title: {                                                       
                text: yAxis_title_text,                             
                align: 'high'                                              
            },                                                             
            /*labels: {                                                      
                overflow: 'justify'                                        
            } */                                                             
        },                                                                 
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:f} </b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },                                                                 
        plotOptions: {                                                     
            bar: {                                                         
                dataLabels: {                                              
                    enabled: true                                          
                }                                                          
            }                                                              
        },                                                                 
        legend: {                                                          
            layout: 'vertical',                                            
            align: 'right',                                                
            verticalAlign: 'center',                                          
            x: -40,                                                        
            y: 100,                                                        
            floating: true,                                                
            borderWidth: 1,                                                
            backgroundColor: '#FFFFFF',                                    
            shadow: true                                                   
        },                                                                 
        credits: {                                                         
            enabled: false                                                 
        },                                                                 
        series: series                                                           
    });                                                                    
}
//柱状图
function showColumn (container,title_text,xAxis_title_text,yAxis_title_text,xAxis_categories_data,series_data) {
	//图表类型
	var chart_type="column";
	//图表名称
	//var title_text="年龄分布图";
	title_text = title_text+"分布图";
	//图表副标题  
	var subtitle_text="";
	//x轴
	//范围值
	var xAxis_categories = eval(xAxis_categories_data);
	//var xAxis_categories = ['0','5','10','15','20','25','30','35','40','45','50','55'];
	//标题
	//var xAxis_title_text = "年龄";
	//y轴
	//最小值开始位置
	var yAxis_min = 0;
	//名称
	//var yAxis_title_text = "频数";
	//柱状图参数
	var series = eval(series_data);
	/*var series = [{
				   name: '频数1',
				   data: [0.0002*3341759,0.0005*3341759,0.0015*3341759,0.0065*3341759,0.0124*3341759,0.0301*3341759,0.0464*3341759,0.0639*3341759,0.0962*3341759,0.1188*3341759,0.1292*3341759,0.1571*3341759]
				 },{
					   name: '频数2',
					   data: [0.0002*3341759,0.0005*3341759,0.0015*3341759,0.0065*3341759,0.0124*3341759,0.0301*3341759,0.0464*3341759,0.0639*3341759,0.0962*3341759,0.1188*3341759,0.1292*3341759,0.1571*3341759]
					 }];*/
	
    $('#'+container).highcharts({
    	//图表类型
        chart: {
            type: chart_type
        },
      //图表名称
        title: {
            text: title_text
        },
      //图表副标题        
        subtitle: {
            text: subtitle_text
        },
        //x轴区间范围
        xAxis: {
            categories:xAxis_categories,
            title: {
                    text: xAxis_title_text
                }
        },
        //y轴区间范围
        yAxis: {
            min:yAxis_min,
            title: {
                text: yAxis_title_text
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:f}</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.1,
                borderWidth: 0
            }
        },
        series:series 
    });
};	
//饼状图
function showPie(title_text,series,container) {
	
	series_data = eval(series);
	title_text = title_text+"分布图";
	//图表类型
	var chart_type="bar";
	//图表名称
	//var title_text="年龄分布图";
	//图表副标题  
	var subtitle_text="";
	//数据
	var series_name = "占整体的";
	/*var series = [{
				   name: '0',
				   y: 0.0002
				 },{
					   name: '5',
					   y: 0.0005
					 },{
						   name: '10',
						   y: 0.0015
						 },{
							   name: '15',
							   y: 0.0065
							 }];*/
    $('#'+container).highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: title_text
        },
        subtitle: {                                                        
            text: subtitle_text                                  
        },
        tooltip: {
    	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    color: '#000000',
                    connectorColor: '#000000',
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                }
            }
        },
        series: [{
            type: 'pie',
            name: series_name,
            data: series_data
            
        }]
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
			
		},
		callback: {
			onCheck: zTreeOnCheck,
		}
	};	
//var treeNodes = [{"id":1,"pId":0,"name":"诊断"},{"id":11,"pId":1,"name":"诊断类型"},{"id":12,"pId":1,"name":"icd-10"},{"id":13,"pId":1,"name":"病人唯一标识"},{"id":14,"pId":1,"name":"院内诊断"},{"id":2,"pId":0,"name":"病历"},{"id":21,"pId":2,"name":"病历"},{"id":22,"pId":2,"name":"病区"},{"id":23,"pId":2,"name":"床位"},{"id":24,"pId":2,"name":"科室"},{"id":25,"pId":2,"name":"病人唯一标识"},{"id":3,"pId":0,"name":"基本信息"},{"id":31,"pId":3,"name":"病人唯一标识"},{"id":32,"pId":3,"name":"病区"},{"id":33,"pId":3,"name":"科室"},{"id":34,"pId":3,"name":"性别"},{"id":35,"pId":3,"name":"年龄"},{"id":36,"pId":3,"name":"病人唯一标识"},{"id":4,"pId":0,"name":"药品医嘱"},{"id":41,"pId":4,"name":"病人唯一标识"},{"id":42,"pId":4,"name":"病区"},{"id":43,"pId":4,"name":"科室"},{"id":44,"pId":4,"name":"药品"},{"id":45,"pId":4,"name":"长期医嘱"},{"id":46,"pId":4,"name":"给出频次"},{"id":47,"pId":4,"name":"医嘱类别"},{"id":48,"pId":4,"name":"医嘱状态"},{"id":5,"pId":0,"name":"门诊药品处方明细"},{"id":51,"pId":5,"name":"病人唯一标识"},{"id":52,"pId":5,"name":"药品"},{"id":53,"pId":5,"name":"给出频次"},{"id":6,"pId":0,"name":"手术诊断"},{"id":61,"pId":6,"name":"病人唯一标识"},{"id":62,"pId":6,"name":"诊断类型"},{"id":63,"pId":6,"name":"诊断"},{"id":7,"pId":0,"name":"手术明细"},{"id":71,"pId":7,"name":"病人唯一标识"},{"id":72,"pId":7,"name":"切口类型"},{"id":73,"pId":7,"name":"手术"},{"id":74,"pId":7,"name":"手术部位"},{"id":75,"pId":7,"name":"手术规模"},{"id":76,"pId":7,"name":"愈合情况"},{"id":8,"pId":0,"name":"检验明细"},{"id":81,"pId":8,"name":"病人唯一标识"},{"id":82,"pId":8,"name":"检验结果"},{"id":83,"pId":8,"name":"检验明细"},{"id":84,"pId":8,"name":"检验明细"}];
	var zTree_solr;
	var treeNodes_solr;
	function solrTableAndFieldDisplay() {  
		zTree_solr = $.fn.zTree.init($("#solrTableAndFieldTree"),setting_solr, treeNodes_solr);  
	    $("#return_search").show();
	 }
	
	var checkMap = {};
	//字段展示选中取消事件
	function zTreeOnCheck(event, treeId, treeNodeChange){
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		var changedNodes = zTree.getChangeCheckedNodes();
		var tableName = "";
		if(changedNodes.length>0){
		    //大于零表示选中
			for ( var i=0 ; i < changedNodes.length ; i++ ){
				var treeNode = changedNodes[i];
				
				if(changedNodes.length == 1){
					//tableName  = $.fn.zTree.init($("#solrTableAndFieldTree"),setting_solr, treeNodes_solr).getNodeByParam("id",changedNodes.pId ).name;
					tableName = treeNode.name.substring(treeNode.name.indexOf("(")+1 ,treeNode.name.length -1);
				}else{
					if(treeNode.pId == 0){
						tableName = treeNode.name;
						continue;
					}
				}
					var fieldName ={};
				if(treeNode.checked){
					if(checkMap.hasOwnProperty(tableName)){
						 fieldName =checkMap[tableName];
						 fieldName[treeNode.id] = treeNode.name;
					}else{
						 fieldName[treeNode.id] = treeNode.name;
					}
					checkMap[tableName] = fieldName;
				}else{
					if(checkMap.hasOwnProperty(tableName)){
						 fieldName =checkMap[tableName];
					     delete fieldName[treeNode.id];
					}
					checkMap[tableName] = fieldName;
				}
			}
		}else{
			//表示取消
			if(treeNodeChange.pId == 0){
				tableName = treeNodeChange.name;
				 delete checkMap[tableName];
			}else{
				//第一步获取他的父节点，就是表名
				 tableName = zTree.getNodeByParam("id",treeNodeChange.pId ).name;
				 if(checkMap.hasOwnProperty(tableName)){
					 fieldName =checkMap[tableName];
				     delete fieldName[treeNodeChange.id];
				}
			}
		}
	}
	function AutoMatch(txtObj) {
	    if (txtObj.value.length > 0) {
	    	var zTree_solr_bean =  $.fn.zTree.init($("#solrTableAndFieldTree"), setting_solr, treeNodes_solr);
	        var nodeList = zTree_solr_bean.getNodesByParamFuzzy("name", txtObj.value);
	        var nodeList_id = {};
	        for (var i = 0; i < nodeList.length; i++) {
	        	if(nodeList[i].pId > 0){
	        		var node_id=zTree_solr_bean.getNodeByParam("id",nodeList[i].pId);
		        	nodeList[i].name=nodeList[i].name+"("+node_id.name+")";
		        	if(checkMap.hasOwnProperty(node_id.name) && checkMap[node_id.name].hasOwnProperty(nodeList[i].id)){
		        		nodeList[i].checked = true;
		        	}
	        	}
			}
	        //将找到的nodelist节点更新至Ztree内
	        var zTree_solr_mapping = $.fn.zTree.init($("#solrTableAndFieldTree"), setting_solr, nodeList);
	        //$("#return_search").show();
	    }else{
	    	checkZtree();
	    }
	}
	
	//遍历选中的字段及表
	function checkZtree() {
		zTree_solr = $.fn.zTree.init($("#solrTableAndFieldTree"),setting_solr, treeNodes_solr);
    	//获取所有节点
    	for ( var tableKey in checkMap) {
    		var tableNodeId = "";
    		for ( var fieldKey in checkMap[tableKey]) {
    			var node = zTree_solr.getNodeByParam("id",fieldKey );
    			//node.checked = true;
    			 zTree_solr.checkNode(node, true, true);
    			tableNodeId = node.pId;
    		}
    		var tableNode = zTree_solr.getNodeByParam("id",tableNodeId );
    		if(tableNode != null){
    			//tableNode.checked = true;
    			zTree_solr.checkNode(node, true, true);
    		}
    	}
	}
	/** 
     * 刷新当前节点 
     */  
    function refreshNode(zTree) {  
        /*根据 treeId 获取 zTree 对象*/  
        type = "refresh",  
        silent = false,  
        /*获取 zTree 当前被选中的节点数据集合*/  
        nodes = zTree.getSelectedNodes();  
        /*强行异步加载父节点的子节点。[setting.async.enable = true 时有效]*/  
        zTree.reAsyncChildNodes(nodes[0], type, silent);  
    }  
  
    /** 
     * 刷新当前选择节点的父节点 
     */  
    function refreshParentNode(zTree) {  
        type = "refresh",  
        silent = false,  
        nodes = zTree.getSelectedNodes();  
        /*根据 zTree 的唯一标识 tId 快速获取节点 JSON 数据对象*/  
        var parentNode = zTree.getNodeByTId(nodes[0].parentTId);  
        /*选中指定节点*/  
        zTree.selectNode(parentNode);  
        zTree.reAsyncChildNodes(parentNode, type, silent);  
    }  
	
	function return_search_cancel(){
	  $("#return_search").hide();
	  $("#return_search_form_id").val("1");
    }
   
	function return_search_submit(){
		checkZtree();
	    var tableAndField = "";
		var nodeList = zTree_solr.getNodes();
		var nodeArr = zTree_solr.transformToArray(nodeList);
	    var numCheck =0 ;
	    var tableName = "";
	    var checkTableMap = {};
		for (var i = 0,size = nodeArr.length; i < size; i++)
		{
			if (nodeArr[i].pId == 0 && nodeArr[i].checked){
			  checkTableMap[nodeArr[i].id] = nodeArr[i].checked
			}
			if (nodeArr[i].pId != 0 && typeof(checkTableMap[nodeArr[i].pId]) != "undefined" && nodeArr[i].checked)
			{
				    numCheck++;
					if (tableAndField == "")
						//tableAndField = "*[" +zTree_solr.getNodeByParam("id",nodeArr[i].pId,null).name + "." + nodeArr[i].name+"]";							
						tableAndField = "*[" +tableName + "." + nodeArr[i].name+"]";							
					else							
						//tableAndField = tableAndField + "AND"+"*[" +  zTree_solr.getNodeByParam("id",nodeArr[i].pId,null).name + "." + nodeArr[i].name+"]";
						tableAndField = tableAndField + "AND"+"*[" +  tableName + "." + nodeArr[i].name+"]";
			}else{
				
				if(nodeArr[i].pId == 0){
					tableName = nodeArr[i].name;
				}
				
			}
		}
		//numCheck = numCheck -13;
		if(numCheck == 0 || numCheck < 0){
			return_search_cancel();
			$("#searchButton").click();
			parent.$.dialog("未选择 展示字段!", false, 1500);
		}else if(numCheck>0 && numCheck <= 50){
			//alert(tableAndField);
			if(tableAndField.replace(" ", "").length > 0){
				$("#return_search").hide();
				$("#return_search_form_id").val(tableAndField);
				groupNumstatic =-1;
				search();
			}
		}else{
			alert("选择最大为50个,请确认返回字段数目！");
		}
    }
	
	
	
	
	var solrDBFieldInfoOk =true;
	function getSolrDBFieldInfo()
	{
		if(solrDBFieldInfoOk){
			$.ajax({
				type : "post",
				url : "/hssp/tail/getSolrReturn"+"?Identity="+$("#hideUserIdentity",parent.document).val(),
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
				},error:function(e){
	   	    		parent.location.reload();    	    
				}
			});
		}else{
			$("#return_search").show();
		}
	}
</script>
<script type="text/javascript">
var endWords = false;
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
		//if($("#searchType_id").val() == "2"){
			//$("#searchButton").attr("onclick","resetSearch();");
		//	$("#searchType_id").val("0");
		//	$("#searchButton").val("搜 索");
		//}
		//获取搜索提示词
		//var inputStr = $("#txtKeyword").val();
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
			var searchType = 1;
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
										htmls = htmls + "<option value=\"" +  searchTipList[i] + lastInputStr + "\">";
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
								div_searchTip.style.width = document.getElementById("834191984").scrollWidth + 'px';
								div_searchTip.style.top = document.getElementById("txtKeyword").scrollHeight + document.getElementById("txtKeyword").offsetTop + 'px';
								div_searchTip.style.display = "block";
							}else{
								return;
							}
						}else{
							return;
						}
					}
				},error:function(e){
	   	    		parent.location.reload();    	    
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
				resetSearch()
			}
		}
	}
	/*当输入框聚焦情况下按向下方向键时，把焦点转移到下拉提示框
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
		}
	}*/
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
	    search_tip_list_val = checkSt;
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
			//var inputStr = $("#txtKeyword").val();
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
			if (e.keyCode == 13 || e.keyCode == 108)
			{
				gotoResult();
			}
		}
	}
	var encodeURI = function (val) {
     return encodeURIComponent(val);
  }
    
	var toBase64 = function (val) {
     var b = new Base64();
     return b.encode(val);
  }
    
	function Base64() {

        // private property
        _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

        // public method for encoding
        this.encode = function (input) {
            var output = "";
            var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
            var i = 0;
            input = _utf8_encode(input);
            while (i < input.length) {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);
                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;
                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }
                output = output +
                _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
                _keyStr.charAt(enc3) + _keyStr.charAt(enc4);
            }
            return output;
        }

        // public method for decoding
        this.decode = function (input) {
            var output = "";
            var chr1, chr2, chr3;
            var enc1, enc2, enc3, enc4;
            var i = 0;
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
            while (i < input.length) {
                enc1 = _keyStr.indexOf(input.charAt(i++));
                enc2 = _keyStr.indexOf(input.charAt(i++));
                enc3 = _keyStr.indexOf(input.charAt(i++));
                enc4 = _keyStr.indexOf(input.charAt(i++));
                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;
                output = output + String.fromCharCode(chr1);
                if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
                }
            }
            output = _utf8_decode(output);
            return output;
        }

        // private method for UTF-8 encoding
        _utf8_encode = function (string) {
            string = string.replace(/\r\n/g, "\n");
            var utftext = "";
            for (var n = 0; n < string.length; n++) {
                var c = string.charCodeAt(n);
                if (c < 128) {
                    utftext += String.fromCharCode(c);
                } else if ((c > 127) && (c < 2048)) {
                    utftext += String.fromCharCode((c >> 6) | 192);
                    utftext += String.fromCharCode((c & 63) | 128);
                } else {
                    utftext += String.fromCharCode((c >> 12) | 224);
                    utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                    utftext += String.fromCharCode((c & 63) | 128);
                }

            }
            return utftext;
        }

        // private method for UTF-8 decoding
        _utf8_decode = function (utftext) {
            var string = "";
            var i = 0;
            var c = c1 = c2 = 0;
            while (i < utftext.length) {
                c = utftext.charCodeAt(i);
                if (c < 128) {
                    string += String.fromCharCode(c);
                    i++;
                } else if ((c > 191) && (c < 224)) {
                    c2 = utftext.charCodeAt(i + 1);
                    string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                    i += 2;
                } else {
                    c2 = utftext.charCodeAt(i + 1);
                    c3 = utftext.charCodeAt(i + 2);
                    string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                    i += 3;
                }
            }
            return string;
        }
  }
	var getOK = false;
	$.fn.bigGlass = function(type){
		var gId = $(this).attr("id");
		var glassStr = '<div id="bigGlass"><div><p></p><p></p><p></p><p></p></div></div>';
		$(this).after($(glassStr));
		$(this).keyup(function(){
			showBigGlass();
		})
		//生成放大镜
		function showBigGlass(){
			var glassT = 50, glassL = 0;//定义预展示输入框的坐标
			var glass_width = $("#txtKeyword").width();
			var inputVal = $("#"+gId).val(), l = inputVal.length;
			$("#bigGlass").css({"top":(glassT)+"px","left":glassL+"px"});
			 //style="top:'+(glassT-50)+'px;left:'+glassL+'px;  ,,"width":glass_width+"px","word-wrap":"break-word""
			 var input_word_length = $("#txtKeyword").val().length;
			/*  if(getOK){
				 input_length=document.getElementById("txtKeyword").value.length;
			 }else{
				 getOK = true;
			 } */
			if(!inputVal){
				$("#bigGlass").hide();
				return false;
			}else if(input_word_length <= input_length){
				/* $("#bigGlass").hide();
				return false; */
			}
			 var heightPx = 40;
			 var realLength = 0, len = inputVal.length, charCode = -1;
			//身份证号码与电话号码展示逻辑不同，做区分
			for (var i = 0; i < len; i++) {
	            charCode = inputVal.charCodeAt(i);
	            if (charCode >= 0 && charCode <= 128) realLength += 1;
	            else realLength += 2;
	        }
			if(realLength < 50 ){
				/* var inputValShow = "<p>";	
				var st = inputVal;
				var st_num = 30;
				while(true){
					if(st.length > st_num ){
						inputValShow = inputValShow+st.substring(0,st_num)+"</p><p>";
						st = st.substring(st_num,st.length);
						heightPx = heightPx+heightPx;
					}else{
						inputValShow = inputValShow+st+"</p></div>";
						break;
					}
				}
				$("#bigGlass").html(inputValShow); */
				$("#bigGlass").hide();
			}else{
				$("#bigGlass").html("<div><p>"+inputVal+"</p></div>");
				$("#bigGlass").show();
			}
			//$("#bigGlass").css({"top":(glassT)+"px","left":glassL+"px","height":(heightPx)+"px"});
			//$("#bigGlass").find("span").eq(0).text(inputVal);
			
		}
		//控制数字放大镜的显示与销毁
		$(document).click(function(event){
			var obj = event.srcElement || event.target;
			if($(obj).attr("id") != gId){
				$("#bigGlass").html("").hide();
			}else{
				showBigGlass();
			}
		});
	}
	$(function(){
		$("#txtKeyword").bigGlass(1);
	})
/*******************************************************************************/

function showFanctreeByKeywords(keywords){
	/* window.parent.showFancytree("");
	setTimeout("", 5000);
	window.parent.showFancytreeSearch();
	return;
	$("#show_fancytree_search_id",parent.document).val(keywords);
	$("#show_fancytree_search_submit_id",parent.document).click(); */
	if(keywords.indexOf("[") > 0 ){
		keywords = keywords.substring(0,keywords.indexOf("["));
	}
	window.parent.searchFancytreeForSearch(keywords);
}

 /*
        执行计划查看
 */
 var CheckSearchPlanOK =false;
 function showCheckSearchPlan(){
		if(CheckSearchPlanOK){
			return;	 
		}
		$("#container_showSearchPlan_swait").show(1000);
		$("#showSearchPlan_table_div").hide();	
		$("#searchForm").ajaxSubmit({
		     type: "post",
		     url: "/hssp/rest/checkSearchPlan"+"?Identity="+$("#hideUserIdentity",parent.document).val(),
		     dataType: "json",
		     success: function(datas){
		    	 if(datas.result == "0000"){
		    		 if(datas.status == "2"){
		    			 refreshShowSearchPlan();
		    			 $("#container_showSearchPlan_swait").hide(1000);
		        		 $("#showSearchPlan_table_div").show(1000);
		        		 CheckSearchPlanOK = true;
		        	  }else{
		    		   	setTimeout("showCheckSearchPlan()", 2000);
		        	  }
		    	 }
		     },
		     error :function(e){
   	   	     // parent.location.reload();    	
   	   	     alert("请求报错！");
          	}
		});
		  
	}
	function refreshShowSearchPlan(){
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
    	         title: "检索词",
    	         field: "keywords",
    	         valign:'middle',
    	         align:'center',
    	         formatter:function(value,row,index){
      	        	 var e = "";
      	        	  if(row.ShowRelation == 1){
      	        		 e=row.keywords;
      	        	 }else if(row.ShowRelation == 0){
      	        		 e = "<a href=\"javascript:void(0);\" onclick=\"showFanctreeByKeywords('"+row.keywords+"')\">";
      	        		 e= e+row.keywords;
      	        		 e =e+"</a>";
      	        	}
      	        	  return e;
    	         }
    	     },
    	     {
  	         title: "命中结果",
  	         field: "totalRecord",
  	         valign:'middle',
  	         align:'center'
  	     	}
			];
		 $('#showSearchPlan_table_id').bootstrapTable('destroy');
		 var datas = "";
      	 //初始化表格,动态从服务器加载数据
         $("#showSearchPlan_table_id").bootstrapTable({
             url: "/hssp/rest/getcheckSearchPlan", //获取数据的Servlet地址
             //method: "POST",  //使用get请求到服务器获取数据
             //contentType: "application/x-www-form-urlencoded",//post请求的话就加上这个句话
             toolbar: "#showSearchPlan_table",
             striped: true,                      //是否显示行间隔色
             cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
             pagination: true,                   //是否显示分页（*）
             sortable: false,                     //是否启用排序
             sortOrder: "asc",                   //排序方式
             sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
             pageNumber:1,                       //初始化加载第一页，默认第一页
             pageSize: 10,                       //每页的记录行数（*）
             pageList: [10],        			//可供选择的每页的行数（*）
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
               	requestID:$("#getCount_requestID").val(),
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
            	 datas = data;	
            	 return data.rows;
             },
        		onLoadError: function(e){
   	   	      //parent.location.reload();
   	   	      alert("请求失败！");
            	}
           });
         if(datas.result == "0000"){
       		if(datas.status == "2"){
       			$("#container_showSearchPlan_swait").hide();
       			$("#showSearchPlan_table_div").show(1000);
       		}else{
       			$("#container_showSearchPlan_swait").show(1000);
       			$("#showSearchPlan_table_div").hide(1000);
       		}
       	  }else{
       		
       	  }
      	 
	}
	function showVenn(container,title_text,series_data) {  
		$('#'+container).highcharts({                                         
	        chart: {                                                           
	            type: "Venn"                                                    
	        },                                                                 
	        title: {                                                           
	            text: title_text                    
	        },                                                                 
	        tooltip: {
	            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	                '<td style="padding:0"><b>{point.y:f} </b></td></tr>',
	            footerFormat: '</table>',
	            shared: true,
	            useHTML: true
	        },                                                                 
	        plotOptions: {                                                     
	            bar: {                                                         
	                dataLabels: {                                              
	                    enabled: true                                          
	                }                                                          
	            }                                                              
	        },                                                                 
	        legend: {                                                          
	            layout: 'vertical',                                            
	            align: 'right',                                                
	            verticalAlign: 'center',                                          
	            x: -40,                                                        
	            y: 100,                                                        
	            floating: true,                                                
	            borderWidth: 1,                                                
	            backgroundColor: '#FFFFFF',                                    
	            shadow: true                                                   
	        },                                                                 
	        credits: {                                                         
	            enabled: false                                                 
	        },                                                                 
	        series: series                                                           
	    });        
	}
/******************************************************************Omaha树获取展现结束**********************************************************************************/
/******************************************************************boostrap table 鼠标右键事件开始**********************************************************************************/
	//展示标签页面
	function mouseShowAddTag(){
		var tagDataParam = $("#tag_hideinput").val();
		//VKey_32360853_32360853
		tagDataParam = tagDataParam.split("_");
		if(tagDataParam.length == 4){
			window.parent.showAddTag(tagDataParam[1],tagDataParam[2],tagDataParam[3],tagDataParam[0],$("#tag_hideinput_fieldValue").val());
	        $("#tag_hideinput").val("");
	        $("#tag_hideinput_fieldValue").val("");
		}else{
			//alert("标签开启失败，失败值："+tagDataParam);
		}
	}
	function showAddTag(dataType,dataValue,visitnumberid){
		window.parent.showAddTag(dataType,dataValue,visitnumberid);
	}
	
	$('#table').contextmenu({
        target: '#context-menu2',
        before: function (e) {
          // This function is optional.
          // Here we use it to stop the event if the user clicks a span
          e.preventDefault();
          //alert(e.target.tagName);
          //alert(e.originalEvent.target.parentNode.tagName);
          if(e.target.tagName != "TD"){
        	  var tagDataParam = "";
        	  var fieldValue = "";
        	  var tagDataParamStatus = -1;
        	  var node = e.originalEvent.target;
	          while(true){
	        	  if(node.tagName == "DIV"){
	        		  tagDataParam = node.id;
	        		  fieldValue = node.innerText;
	        		  if(fieldValue.length > 5){
	        			  fieldValue = fieldValue.substring(0,5)+"...";
	        		  }
	        		  tagDataParamStatus = 1;
	        		  break;
	        	  }else if(node.tagName == "TD"){
	        		  break;
	        	  }else{
	        		  node = node.parentNode;
	        	  }
	          }
	          if(tagDataParamStatus == 1){
	        	  tagDataParam = tagDataParam.replace("table_show_div_","")
	  	  		if(tagDataParam.split("_").length == 4){
	  	  	        $("#tag_show_id").show();
	  	  	        $("#tag_hideinput").val(tagDataParam);
	  	  	        $("#tag_hideinput_fieldValue").val(fieldValue);
	  	  		}else{
	  	  			$("#tag_show_id").hide();
	  	  			//alert("标签开启失败，失败值："+tagDataParam);
	  	  		} 
	          }
          }else{
        	  $("#tag_show_id").hide();
        	  //parent.$.dialog("右键事件加载失败，请联系系统管理员！", false, 1000);
          }
          if (e.target.tagName == 'SPAN') {
            e.preventDefault();
            this.closemenu();
            return false;
          }
          return true;
        }
      });
 
/******************************************************************boostrap table 鼠标右键事件结束**********************************************************************************/
//dropdown-menu
</script>
</body>

</html>
