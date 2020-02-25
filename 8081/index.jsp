<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/views/common/meta.jsp"%>
<title>临床大数据搜索</title>
<link rel="icon" href="${pageContext.request.contextPath}/resources/images/companylogo.png">
	<meta name="viewport" content="width=device-width">
		<link href="${pageContext.request.contextPath}/resources/css/Css/css.css" rel="stylesheet" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/jquery-ui-1.11.2.custom/jquery-ui.css" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/coverage.css" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-table-fixed-columns.css">
    	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-table-develop/dist/bootstrap-table.min.css">
    	
    	
    	<script src="${pageContext.request.contextPath}/resources/js/highcharts6/highcharts.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/highcharts6/modules/exporting.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/highcharts6/modules/wordcloud.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/highcharts6/modules/oldie.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/highcharts6/themes/grid-light.js"></script>
    	
    	<script src="${pageContext.request.contextPath}/resources/js/wordcloud2.js"></script>
    
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
		<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-datetimepicker-master/js/locales/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>
				<!-- fancytree 属性插件引用-->
	<script src="${pageContext.request.contextPath}/resources/js/fancytree/jquery.fancytree.ui-deps.js"></script>
	<link href="${pageContext.request.contextPath}/resources/js/fancytree/skin-lion/ui.fancytree.css" rel="stylesheet">
	<script src="${pageContext.request.contextPath}/resources/js/fancytree/jquery.fancytree.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/fancytree/sample.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/fancytree/sample.css"></script>
	
	<script src="${pageContext.request.contextPath}/resources/js/fancytree/jquery.fancytree.dnd5.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/fancytree/jquery.fancytree.filter.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/fancytree/prettify.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/fancytree/prettify.css"></script>
	<%-- 
	<script src="${pageContext.request.contextPath}/resources/js/fancytree/jquery-ui.min.js"></script>
	
	<script src="${pageContext.request.contextPath}/resources/js/fancytree/jquery-3.4.1.min.js"></script> --%>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap-theme.min.css" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/Css/bootstrap-3.2.0/bootstrap.css" />
	
		<style type="text/css">
		/* fancytree 属性插件引用*/
		div#fancytre_id {
			height: 95%;
			width: 95%;
			padding: 5px;
			margin-right: 16px;
		}
		ul.fancytree-container {
			height: 65vh;
			width: 40vh;
			overflow: auto;
			background-color: transparent;
		}
		span.fancytree-node span.fancytree-title {
			/* color: white; */
			text-decoration: none;
		}
		/* span.fancytree-focused span.fancytree-title {
			outline-color: white;
		} */
		span.fancytree-node:hover span.fancytree-title,
		span.fancytree-active span.fancytree-title,
		span.fancytree-active.fancytree-focused span.fancytree-title,
		.fancytree-treefocus span.fancytree-title:hover,
		.fancytree-treefocus span.fancytree-active span.fancytree-title {
			color: #39414A;
		}
		span.external span.fancytree-title:after {
			content: "";
			background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAMAAAC67D+PAAAAFVBMVEVmmcwzmcyZzP8AZswAZv////////9E6giVAAAAB3RSTlP///////8AGksDRgAAADhJREFUGFcly0ESAEAEA0Ei6/9P3sEcVB8kmrwFyni0bOeyyDpy9JTLEaOhQq7Ongf5FeMhHS/4AVnsAZubxDVmAAAAAElFTkSuQmCC") 100% 50% no-repeat;
			padding-right: 13px;
		}
		/* Remove system outline for focused container */
		.ui-fancytree.fancytree-container:focus {
			outline: none;
		}
		.ui-fancytree.fancytree-container {
			border: none;
		}
		
		/* fancytree 属性插件引用*/
		
		
		.ui-dialog .ui-dialog-titlebar-close {display:none}
		#myTab li{
					width:20%;
					float:left;
					height:20px;	
					list-style: none;
					margin: 0;
					padding: 0;
					color: #fff;
				}
				
		#myTab li img{
					float:left;
					height: 20px;	
				}
				
		#myTab li a{
					color:white;
					text-align: center;
					position: relative;
				    display: block;
				    padding: 10px 15px;    
				}
				
		.blue{
					background:#3385FF;
				}
			.gray{
					background: #dfdfdf;
				}
			.centerIframe{
				height: calc(100% - 60px);
			}
			.mark {
			    background-color: yellow;
			    color: black;
			}
			.checkNo{
			    pointer-events: none;
			}
		
		</style>
</head>
<body>
	<iframe id="header" src="header" name="header" frameBorder="0" scrolling="no"
		style="border:0;padding:0;margin:0;width:100%;height:30px; position:relative; z-index:1;"></iframe>
	<iframe id="prebody" src="prebody" name="prebody"  frameBorder="0" scrolling="no"
		class="centerIframe" style="border:0;padding:0;margin:0;width:100%;   position:relative; z-index:1;"></iframe>
	<iframe id="results" src="results" name="results" frameBorder="0" scrolling="no"
		class="centerIframe" style="border:0;padding:0;margin:0;width:100%; position:relative; z-index:1;display:none;"></iframe>
	<iframe id="privilegeApproval" src="privilegeApproval" name="privilegeApproval" frameBorder="0" scrolling="no"
		class="centerIframe" style="border:0;padding:0;margin:0;width:100%; position:relative; z-index:1;display:none;"></iframe>
	  <iframe id="export" src="" name="results" frameBorder="0" scrolling="no"
		class="centerIframe" style="border:0;padding:0;margin:0;width:100%; position:relative; z-index:1;display:none;"></iframe>
	<iframe id="footer" src="footer" name="footer" frameBorder="0" scrolling="no"
		style="border:0;padding:0;margin:0;width:100%;height:30px; position:fixed;bottom:0px; z-index:1;"></iframe>
	<div id="div_diseaseDBSave_local" title="数据导出信息" style="display:none;">
			<div class="questionForm">
				<form class="form-horizontal">
					<div class="form-group">
						<label class="control-label col-sm-2">大类:</label>
						<div class="col-sm-6" id="select_diseaseClassify_div_local">
								
						</div>
						<button id="btn_diseaseNew" type="button" class="btn btn-primary col-sm-2" onclick="$('#div_classifyName').dialog('open')">新增</button>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-2">分类:</label>
						<div class="col-sm-6" id="select_diseaseClassify_Child_div_local">
							
						</div>
						<button id="btn_diseaseNew" type="button" class="btn btn-primary col-sm-2" onclick="$('#div_classifyChildName').dialog('open')">新增</button>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-2">名称:</label>
						<div class="col-sm-6"id="select_diseaseClassify_Child_dieaseName_div">
							<!--  <input class="form-control" id="diseaseDBName" onkeyup="this.value=this.value.replace(/[^\u4e00-\u9fa5\w]/g,'')"/>-->
						</div>
						<button id="btn_diseaseNew" type="button" class="btn btn-primary col-sm-2" onclick="$('#div_dieaseNameList').dialog('open')">新增</button>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-2">权限:</label>
						<div class="col-sm-6" id="select_jurisdiction_div">
							<select id="jurisdiction" class="form-control">
						   
						    </select>	
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-2">备注:</label>
						<div class="col-sm-6">
							<textarea id="description_old" rows="2" class="form-control"></textarea>
						</div>
					</div>
				</form>
			</div>
		</div>
	<!-- 反馈按钮弹出层enctype="multipar/form-data" -->
	<div id="div_feedbackManage" title="意见反馈" class="ShowListDialog" style="display:none;overflow-y:hidden;width:600px;">
	   	<div class="questionForm" style="padding-left:5%;margin-top:30px;margin-bottom:30px;">
			<form id ="feedback" class="form-horizontal"  enctype="multipart/form-data">
				<div class="form-group">
					<label class="control-label col-sm-1" style="width:120px;margin-right:20px;">反馈类型:</label>
					<div class="col-sm-3">
						<select id="types" name="types" class="form-control" style="width:250px;">
							<option value="同义词">同义词</option>
							<option value="搜索数据疑问">搜索数据疑问</option>
							<option value="功能使用疑问">功能使用疑问</option>
							<option value="功能扩展建议">功能扩展建议</option>
							<option value="其他">其他</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2" class="form-control"  style="width:120px;margin-right:20px;"">手机号码:</label>
					<div class="col-sm-6">
						<input id="contactPhone" type="text" name="contactPhone" onkeyup="this.value=this.value.replace(/\D/g,'')"
							onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="11" placeholder="请输入手机号" class="form-control" style="width:250px;"/>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2"  style="width:120px;margin-right:20px;"">附&nbsp&nbsp&nbsp&nbsp&nbsp 件:</label>
					<div class="col-sm-6">
						<input onchange = "SelectFile(this)" class="form-control" style="width:250px;" id="annex" type="file" name="file" accept=".xls,.xlsx,.doc,docx,.txt,.pdf,.jpg,.jpeg,.png," />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2"  style="width:120px;margin-right:20px;""><span style="color:red">*</span>反馈内容</>:</label>
					<div class="col-sm-6">
						<textarea id="feedbackContent" name="feedbackContent" rows="10" class="form-control" style="width:300px;height:120px"></textarea>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- 使用量统计 -->
	<div id="div_diseaseCountDetail" title="使用量统计" style="display:none;overflow-y:hidden;" class="bootstrap-table">
		  <div class="clearfix sortdiv">
		     <div class="form-inline" style="position:absolute;">
			      <div id="div_diseaseCountDetailTotalInfo" class="page_show">
		              <span>总使用数:<b style="color:#3385FF" id="diseaseCountDetailTotalInfo"></b>次 </span>
		              <span>使用科室:<b style="color:#3385FF" id="diseaseCountDetailTotalDepa"></b>个</span>
		              <span style="position:relative">
			            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			              <span id="limit_time_text">时间段统计</span>
			              <span class="caret"></span>
			            </button>
			            <!-- Time screening -->
			              <div class="c-tip-con dropdown-menu" style="position:absolute; right:initial; float:right; top:24px">
			                
			                <div class="c-tip-menu c-tip-timerfilter">
			                      <ul>
			                        <li>
			                            <a onclick="selectChange(0,'时间不限')">时间不限</a>
			                        </li>
			                        <li>
			                            <a onclick="selectChange(7,'一周内使用量')">一周内使用量</a>
			                        </li> 
			                        <li>
			                            <a onclick="selectChange(1,'一个月内使用量')">一个月内使用量</a>
			                        </li>
			                        <li>
			                            <a onclick="selectChange(3,'三个月内使用量')">三个月内使用量</a>
			                        </li> 
			                        <li>
			                            <a onclick="selectChange(6,'半年内使用量')">半年内使用量</a>
			                        </li>
			                        <li>
			                            <a onclick="selectChange(12,'一年内使用量')">一年内使用量</a>
			                        </li>
			                        <li class="c-tip-custom">
			                           <!--   <hr>自定义<p class="c-tip-custom-st">从<input size="16" type="text" id="select_Start" value="" class="c-tip-custom-input c-tip-custom-input-init form_date"></p><p class="c-tip-custom-et">至<input size="16" type="text" id="select_End" value="" class="c-tip-custom-input c-tip-custom-input-init form_date"></p>-->
			                            <hr>自定义<p class="c-tip-custom-st">从<input type="text" id="select_Start" class="c-tip-custom-input" value="yyyy-MM-dd" onblur="if(this.value==''){this.value='yyyy-MM-dd'}" onfocus="if(this.value=='yyyy-MM-dd'){this.value=''}" onchange="IsValidDate('select_Start')" /></p>
			                            <p class="c-tip-custom-et">至<input type="text" id="select_End" class="c-tip-custom-input" value="yyyy-MM-dd" onblur="if(this.value==''){this.value='yyyy-MM-dd'}" onfocus="if(this.value=='yyyy-MM-dd'){this.value=''}" onchange="IsValidDate('select_End')" /></p>
			                            <a class="c-tip-custom-submit" onclick="time_select_2_submit()">确认</a>
			                        </li>
			                        <li>                      	
			                        </li>                     
			                      </ul>
			                    </div>
			              </div>
		              </span>
		           </div>
		    </div>
           </div>
           
		<div class="fixed-table-body">
			<table id="diseaseCountDetail" data-search="true" data-show-columns="false" >
			</table>
		</div>
	</div>
	<!-- 热词词云 -->
	<div id="div_wordCloudDetail" title="热词统计" style="display:none;overflow-y:hidden;" class="bootstrap-table">
		  <div class="clearfix sortdiv"  style="z-index:2;">
		     <div class="form-inline" style="position:absolute;">
			      <div id="div_wordCloudDetailTotalInfo" class="page_show">
		              <span style="position:relative">
		              <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			              <span id="limit_top_wordCloud_text">Top100</span>
			              <span class="caret"></span>
			            </button>
			              <div class="c-tip-con dropdown-menu" style="position:absolute; right:initial; float:right; top:24px">
			                <div class="c-tip-menu c-tip-timerfilter">
			                      <ul>
			                        <li>
			                            <a onclick="selectWordCloudTop(0,'不限')">不限</a>
			                        </li>
			                        <li>
			                            <a onclick="selectWordCloudTop(50,'Top50')">Top50</a>
			                        </li> 
			                        <li>
			                            <a onclick="selectWordCloudTop(100,'Top100')">Top100</a>
			                        </li>
			                        <li>
			                            <a onclick="selectWordCloudTop(300,'Top300')">Top300</a>
			                        </li>
			                        <li>
			                            <a onclick="selectWordCloudTop(500,'Top500')">Top500</a>
			                        </li> 
			                        <li class="c-tip-custom">
			                            <hr>自定义<p class="c-tip-custom-st">Top<input style="margin-left: 0px" type="text" id="select_wordCloud_top" class="c-tip-custom-input" value="数字(0~1000)" onblur="if(this.value==''){this.value='数字(0~1000)'}" onfocus="if(this.value=='数字(0~1000)'){this.value=''}" onchange="IsValidDateNumber('select_wordCloud_top')" /></p>
			                            <a class="c-tip-custom-submit" onclick="top_select_submit()">确认</a>
			                        </li>
			                      </ul>
			                    </div>
			              </div>
			          </span>
		              <span style="position:relative">
		              <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			              <span id="limit_department_wordCloud_text">全院</span>
			              <span class="caret"></span>
			            </button>
			              <div class="c-tip-con dropdown-menu" style="position:absolute; right:initial; float:right; top:24px">
			                <div class="c-tip-menu c-tip-timerfilter" >
			                      <ul id="ul_department_wordCloud" style="overflow-y:scroll; max-height:315px;">
			                        <li>
			                            <a onclick="selectWordCloudDepartment('')">全院</a>
			                        </li>
			                      </ul>
			                  </div>
			              </div>
			          </span>
		              <span style="position:relative">
			            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			              <span id="limit_time_wordCloud_text">时间段统计</span>
			              <span class="caret"></span>
			            </button>
			            <!-- Time screening -->
			              <div class="c-tip-con dropdown-menu" style="position:absolute; right:initial; float:right; top:24px">
			                
			                <div class="c-tip-menu c-tip-timerfilter">
			                      <ul>
			                        <li>
			                            <a onclick="selectWordCloudChange(0,'时间不限')">时间不限</a>
			                        </li>
			                        <li>
			                            <a onclick="selectWordCloudChange(3,'三个月内统计')">三个月内统计</a>
			                        </li> 
			                        <li>
			                            <a onclick="selectWordCloudChange(6,'半年内统计')">半年内统计</a>
			                        </li>
			                        <li>
			                            <a onclick="selectWordCloudChange(12,'一年内统计')">一年内统计</a>
			                        </li>
			                        <li>
			                            <a onclick="selectWordCloudChange(24,'两年内统计')">两年内统计</a>
			                        </li> 
			                        <li>
			                            <a onclick="selectWordCloudChange(36,'三年内统计')">三年内统计</a>
			                        </li>
			                        <li class="c-tip-custom">
			                            <hr>自定义<p class="c-tip-custom-st">从<input type="text" id="select_wordCloud_Start" class="c-tip-custom-input" value="yyyy-MM-dd" onblur="if(this.value==''){this.value='yyyy-MM-dd'}" onfocus="if(this.value=='yyyy-MM-dd'){this.value=''}" onchange="IsValidDate('select_wordCloud_Start')" /></p>
			                            <p class="c-tip-custom-et">至<input type="text" id="select_wordCloud_End" class="c-tip-custom-input" value="yyyy-MM-dd" onblur="if(this.value==''){this.value='yyyy-MM-dd'}" onfocus="if(this.value=='yyyy-MM-dd'){this.value=''}" onchange="IsValidDate('select_wordCloud_End')" /></p>
			                            <a class="c-tip-custom-submit" onclick="time_select_submit()">确认</a>
			                        </li>
			                        <li>                      	
			                        </li>                     
			                      </ul>
			                    </div>
			              </div>
		              </span>
		           </div>
		    </div>
           </div>
		<!-- 词云-调用R语言词云展示--> 
		  <div id="div_wordCloudDetail_show" class="fixed-table-body" style="z-index:1">
			 <div id="container" frameBorder="0" scrolling="no"
			style="border:0;padding:0;margin:0;width:100%;height:100%; position:relative; z-index:1;display:none"> 
		</div>
		<!--<div id="canvas-container" style="float: left;margin-top: -50px;">
			
		</div>--> 
	</div>
	<div id="div_diseaseDBSave_local_div" title="数据导出信息" style="display:none;">
			<div class="questionForm">
				<form class="form-horizontal">
					<div class="form-group">
						<label class="control-label col-sm-2">名称:</label>
						<div class="col-sm-6"id="select_diseaseClassify_Child_dieaseName_Local_div">
							<!--  <input class="form-control" id="diseaseDBName" onkeyup="this.value=this.value.replace(/[^\u4e00-\u9fa5\w]/g,'')"/>-->
						</div>
						<button id="btn_diseaseNew" type="button" class="btn btn-primary col-sm-2" onclick="$('#div_dieaseNameList').dialog('open')">新增</button>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-2">备注:</label>
						<div class="col-sm-6">
							<textarea id="description" rows="2" class="form-control"></textarea>
						</div>
					</div>
				</form>
			</div>
		</div>
	<!-- 病种库保存按钮弹出层 -->
	<div id="div_diseaseDBSave" title="科研病种库保存" style="display:none;">
		<div class="questionForm">
			<form class="form-horizontal">
				<div class="form-group" style="display:none">
					<label class="control-label col-sm-2">科室:</label>
					<div class="col-sm-6" id="select_diseaseClassify_div">
							
					</div>
					<!--<button id="btn_diseaseNew" type="button" class="btn btn-primary col-sm-2" onclick="$('#div_classifyName').dialog('open')">新增</button>-->
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2">病种名称:</label>
					<div class="col-sm-6" id="select_diseaseClassify_Child_div">
						
					</div>
				<!--  	<button id="btn_diseaseNew" type="button" class="btn btn-primary col-sm-2" onclick="$('#div_classifyChildName').dialog('open')">新增</button>-->
				</div>
			</form>
		</div>
	</div>
	<!-- 病种库保存按钮弹出层 -->
	<div id="div_diseaseDBSave_Statistics" title="科研统计数据保存" style="display:none;">
		<div class="questionForm">
			<form class="form-horizontal">
				<div class="form-group" style="display:none">
					<label class="control-label col-sm-2">科室:</label>
					<div class="col-sm-6" id="select_diseaseClassify_Statistics_div">
							
					</div>
					<!--<button id="btn_diseaseNew" type="button" class="btn btn-primary col-sm-2" onclick="$('#div_classifyName').dialog('open')">新增</button>-->
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2">病种名称:</label>
					<div class="col-sm-6" id="select_diseaseClassify_Child_Statistics_div">
						
					</div>
				<!--  	<button id="btn_diseaseNew" type="button" class="btn btn-primary col-sm-2" onclick="$('#div_classifyChildName').dialog('open')">新增</button>-->
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2">数据集名称:</label>
					<div class="col-sm-6" id="select_diseaseClassify_Child_Statistics_div_child">
						<input class="form-control" id="diseaseDBName_Statistics"/>
					</div>
				</div>
			</form>
		</div>
	</div>
	<div id="show_value" title="内容" style="display:none;">
		<div class="questionForm" id="show_value_id" >
		</div>
	</div>
<!-- 病种库保存按钮弹出层 -->
	<div id="div_Patitent" title="加入研究者队列" style="display:none;">
		<div class="questionForm">
			<form class="form-horizontal">
				<div class="form-group">
					<label class="control-label col-sm-2">项目中心：</label>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2">临床中心:</label>
					<div class="col-sm-6" id="select_Patitent_Child_div">
						
					</div>
				</div>
			</form>
		</div>
	</div>
	<div name="div_compare_name" id="div_compare" title="数据对比" style="display:none;">
	    <div  id="compare_ul">
	      <ul class="shuju" id="compare_Vkey">
	      </ul>
        </div>
	</div>
	<!-- 新增病种库分类按钮弹出层 -->
	<div id="div_classifyName" title="新增数据导出大类" style="display:none;">
		<div class="questionForm">
			<div class="form-group">
				<label class="control-label col-sm-2" style="width:130px;">新增分类名称:</label>
				<div class="col-sm-6">
					<input class="form-control" id="classifyName" onkeyup="this.value=this.value.replace(/[^\u4e00-\u9fa5\w]/g,'')"/>
				</div>
			</div>
		</div>
	</div>
	<!-- 新增病种库分类按钮弹出层 -->
	<div id="div_classifyChildName" title="新增数据导出小类" style="display:none;">
		<div class="questionForm">
			<div class="form-group">
				<label class="control-label col-sm-2" style="width:130px;">新增子类名称:</label>
				<div class="col-sm-6">
					<input class="form-control" id="classifyChildName" onkeyup="this.value=this.value.replace(/[^\u4e00-\u9fa5\w]/g,'')"/>
				</div>
			</div>
		</div>
	</div>
	<div id="div_uploadFile" title="上传文件" style="display:none;">
		<div class="questionForm">
			<div class="form-group">
				<label class="control-label col-sm-2" style="width:130px;">选择文件:</label>
				<div class="col-sm-6">
					<form id="form_uploadFile" method="post">
						<input class="form-control" id="div_uploadFile_id" name="div_uploadFile_Name" type="file"/>
					</form>
				</div>
			</div>
		</div>
	</div>
	<div id="div_dieaseNameList" title="新增数据导出名称" style="display:none;">
		<div class="questionForm">
			<div class="form-group">
				<label class="control-label col-sm-2" style="width:130px;">导出名称:</label>
				<div class="col-sm-6">
					<input class="form-control" id="dieaseNameList_input"/><span>禁止含特殊符号(/ \ : * " < > | ？-)</span>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 病种库管理点击弹出层大类 -->
	<div id="div_diseaseDBClassify" title="数据导出大分类" class="ShowListDialog" style="display:none;overflow-y:hidden;">
	   			<div style="overflow-y:scroll;height:500px;">
		    		<ul class="shuju" id="diseaseDBClassify">
		   			</ul>
				</div>
	</div>
	<!-- 病种库管理点击弹出层小类 -->
	<div id="div_diseaseDBClassifySub" title="数据导出小分类" class="ShowListDialog" style="display:none;overflow-y:hidden;">
	   			<div style="overflow-y:scroll;height:500px;">
		    		<ul class="shuju" id="diseaseDBClassifySub">
		   			</ul>
				</div>
	</div>
	
	<!-- 分类下病种库点击弹出层 -->
	<div id="div_diseaseDBView" title="数据导出详情展示" style="display:none;overflow-y:hidden;" class="bootstrap-table">
			<div class="clearfix sortdiv">
				<div class="form-inline" style="position:absolute;">
					<div id="div_diseaseDBRecordTotalInfo" class="page_show">
			            <span>总记录数:<b style="color:#3385FF" id="diseaseDBRecordTotalInfo"></b>/每页条数:10</span>
			            <span>总共<b style="color:#3385FF" id="diseaseDBRecordTotalPage"></b>页</span>
			            <span>当前第<b style="color:#3385FF" id="diseaseDBRecordCurrentPage"></b>页</span>
			            <a class="PageButton" onclick="viewDiseaseDBPreviousPage()"><b style="color:#3385FF">上一页</b></a>
			            <a class="PageButton" onclick="viewDiseaseDBNextPage()"><b style="color:blue">下一页</b></a>
		                <span class="currentPage">跳转到 <input id="diseaseDBRecord_pageNum" class="form-control input-sm" style=" width: 53px;text-align:center;IME-MODE: disabled;" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" > 页 <button type="button" class="btn btn-default btn-sm" onclick="jumpToViewDiseaseDB()" >跳转</button></span>
			        </div>
		        </div>
	        </div>
			<div class="fixed-table-body">
				<table id="table" data-search="true" data-show-columns="true" data-height="280" >
				</table>
			</div>
	</div>
	
	<!-- 病种库分类点击弹出层 -->
	  <div id="div_diseaseDBDetail_local" title="数据导出" style="display:none;overflow-y:hidden;" class="bootstrap-table">
		  <div class="clearfix sortdiv">
		     <div class="form-inline" style="position:absolute;">
			      <div id="div_diseaseDBDetailTotalInfo_local" class="page_show">
		              <span>总记录数:<b style="color:#3385FF" id="diseaseDBDetailTotalInfo_local"></b>/每页条数:15</span>
		              <span>总共<b style="color:#3385FF" id="diseaseDBTotalPage_local"></b>页</span>
		              <span>当前第<b style="color:#3385FF" id="diseaseDBCurrentPage_local"></b>页</span>
		              <a class="PageButton" onclick="headerDocument.getPreviousPageDiseaseDBDetail()"><b style="color:#3385FF">上一页</b></a>
		              <a class="PageButton" onclick="headerDocument.getNextPageDiseaseDBDetail()"><b style="color:blue">下一页</b></a>
		              <span class="currentPage">跳转到 <input id="diseaseDB_pageNum_local" class="form-control input-sm" style=" width: 53px;text-align:center;IME-MODE: disabled;" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" > 页 <button type="button" class="btn btn-default btn-sm" onclick="headerDocument.jumpToDiseaseDBDetail()" >跳转</button></span>
		          </div>
		      </div>
           </div>
		<div class="fixed-table-body">
			<table id="diseaseDBDetail_local" data-search="true" data-show-columns="true" style="max-height: 400px" >
			</table>
		</div>
	</div>
	<!-- 病种库分类点击弹出层-调用export项目进行数据导出 -->
	  <div id="div_diseaseDBDetail_local_Export" title="数据导出" style="display:none;overflow-y:hidden;" class="bootstrap-table">
		
	</div>
	<!-- 病种库分类点击弹出层-调用export项目进行数据导出 -->
	  <div id="div_Tag_show" title="添加标签" style="display:none;overflow-y:hidden;" class="bootstrap-table">
	  
	</div>
	   
	<!-- 数据导出上传信息展示 -->
	  <div id="div_uploadDetail" title="审批信息" style="display:none;overflow-y:hidden;" class="bootstrap-table">
		  <div class="clearfix sortdiv">
		     <div class="form-inline" style="position:absolute;">
			      <div id="div_uploadDetail_Message" class="page_show">
		              <span>总记录数:<b style="color:#3385FF" id="diseaseDBDetailTotalInfo_uploadDetail"></b>/每页条数:15</span>
		              <span>总共<b style="color:#3385FF" id="diseaseDBTotalPage_uploadDetail"></b>页</span>
		              <span>当前第<b style="color:#3385FF" id="diseaseDBCurrentPage_uploadDetail"></b>页</span>
		              <a class="PageButton" onclick="headerDocument.getPreviousPageuploadDetail()"><b style="color:#3385FF">上一页</b></a>
		              <a class="PageButton" onclick="headerDocument.getNextPageuploadDetail()"><b style="color:blue">下一页</b></a>
		              <span class="currentPage">跳转到 <input id="diseaseDB_pageNum_uploadDetail" class="form-control input-sm" style=" width: 53px;text-align:center;IME-MODE: disabled;" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" > 页 <button type="button" class="btn btn-default btn-sm" onclick="headerDocument.jumpTouploadDetail()" >跳转</button></span>
		          </div>
		      </div>
           </div>
		<div class="fixed-table-body">
			<table id="table_uploadDetail" data-search="true" data-show-columns="true" >
			</table>
		</div>
	</div>
	<div id="div_diseaseDBDetail" title="科研病种库任务展示" class="ShowListDialog" style="display:none;overflow-y:hidden;">
				<div class="fixed-table-body">
					<table id="disease_show" data-valign="middle">
					</table>
				</div>
	            <div id="div_diseaseDBDetailTotalInfo" class="page">
	                <span>总记录数:<b style="color:#3385FF" id="diseaseDBDetailTotalInfo"></b>/每页条数:<bstyle="color:#3385FF" id="disease_pageCount"></b></span>
	                <span>总共<b style="color:#3385FF" id="diseaseDBTotalPage"></b>页</span>
	                <span>当前第<b style="color:#3385FF" id="diseaseDBCurrentPage"></b>页</span>
	                <a class="PageButton" onclick="getShowDiseaseStatu()"><b style="color:#3385FF">上一页</b></a>
	                <a class="PageButton" onclick="getNextShowDiseaseStatu()"><b style="color:#3385FF">下一页</b></a>
	                <span class="currentPage">跳转到 <input id="diseaseDB_pageNum" class="form-control input-sm" style=" width: 53px;text-align:center;IME-MODE: disabled;" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" > 页 <button type="button" class="btn btn-default btn-sm" style="height:28px" onclick="jumpToShowDiseaseStatu()" >跳转</button></span>
	            </div>
	</div>
	<div id="div_StatisticsDBDetail" title="科研统计库任务展示" class="ShowListDialog" style="display:none;overflow-y:hidden;">
				<div class="fixed-table-body">
					<table id="Statistics_show" data-valign="middle">
					</table>
				</div>
	            <div id="div_StatisticsDBDetailTotalInfo" class="page">
	                <span>总记录数:<b style="color:#3385FF" id="StatisticsDBDetailTotalInfo"></b>/每页条数:<bstyle="color:#3385FF" id="Statistics_pageCount"></b></span>
	                <span>总共<b style="color:#3385FF" id="StatisticsDBTotalPage"></b>页</span>
	                <span>当前第<b style="color:#3385FF" id="StatisticsDBCurrentPage"></b>页</span>
	                <a class="PageButton" onclick="getShowStatisticsStatu()"><b style="color:#3385FF">上一页</b></a>
	                <a class="PageButton" onclick="getNextShowStatisticsStatu()"><b style="color:#3385FF">下一页</b></a>
	                <span class="currentPage">跳转到 <input id="StatisticsDB_pageNum" class="form-control input-sm" style=" width: 53px;text-align:center;IME-MODE: disabled;" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" > 页 <button type="button" class="btn btn-default btn-sm" style="height:28px" onclick="jumpToShowStatisticsStatu()" >跳转</button></span>
	            </div>
	</div>
	<div id="div_diseaseDBDetail_progres" title="任务执行进度" class="ShowListDialog" style="display:none;overflow-y:hidden;">
		<div class="questionForm">
			<form class="form-horizontal">
				<div class="form-group">
					<label class="control-label col-sm-2">所处状态:</label>
					<div class="col-sm-6" style="width:81% ;padding-top: 6px;" id="div_diseaseDBDetail_progres_message_id_1">
							
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2">完成度:</label>
					<div class="col-sm-6" style="width:81% ;padding-top: 6px;" id="select_Patitent_Child_div">
						<div class="progress progress-striped active">
						   <div id="div_diseaseDBDetail_progres_id"  class="progress-bar progress-bar-success" role="progressbar" 
						        aria-valuenow="60" aria-valuemin="0" aria-valuemax="120" style="width:1%">
						        <span class="sr-noly" id="div_diseaseDBDetail_progres_message_id_2"></span>
						   </div>
						</div>	
					</div>
				</div>
			</form>
		</div>
	</div>
	<div id="div_diseaseDBDetail_inner" title="任务执行进度" class="" style="display:none;overflow-y:hidden;padding: 1.5em 1em;min-height: 56px;">
		<!--  <div class="questionForm">
			<form class="form-horizontal">
				<div class="form-group">
					<label class="control-label col-sm-2">所处状态:</label>
					<div class="col-sm-6" style="width:81% ;padding-top: 6px;" id="div_diseaseDBDetail_inner_message_id_1">
							
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2">完成状况:</label>
					<div class="col-sm-6" style="width:81% ;padding-top: 6px;" id="div_diseaseDBDetail_inner_message_id_div">
						
				</div>
			</form>
		</div>-->
		<div id="page-inner">
						  <div class="row" >
						      <ul id="myTab" role="tablist">
						        <li id="step1Li" class="gray">
 						           <img id="step1Img" src="${pageContext.request.contextPath}/resources/images/blue_gray.png"/>
 						                                任务创建成功
 						        </li>
						        <li id="step3Li" class="gray">
 						           <img id="step3Img" src="${pageContext.request.contextPath}/resources/images/gray_gray.png"/>
 						                               数据导入中
 						        </li>
 						        <li id="step5Li" class="gray">
 						           <img id="step5Img" src="${pageContext.request.contextPath}/resources/images/gray_gray.png"/>
 						                              数据导入成功
 						        </li>
 						        <li id="step6Li" class="gray">
 						           <img id="step6Img" src="${pageContext.request.contextPath}/resources/images/gray_gray.png"/>
 						                             数据抽取中
 						        </li>
 						        <li id="step7Li" class="gray">
 						           <img id="step7Img" src="${pageContext.request.contextPath}/resources/images/gray_gray.png"/>
 						                           数据抽取成功               
 						        </li>
						      </ul>
						  </div>
					</div>
	</div>
	
	<!-- 表达式管理按钮弹出层 -->
	<div id="div_expressioinManage" title="表达式管理" class="ShowListDialog" style="display:none;overflow-y:hidden;">
	   			<div style="overflow-y:scroll;">
		    		<ul class="shuju" id="expressioinManage">
		   			</ul>
				</div>
	            <div id="div_expressionTotalInfo" class="paginationNoScroll page">
	                <span>总记录数:<b style="color:#3385FF" id="expressionTotalInfo"></b>/每页条数:15</span>
	                <span>总共<b style="color:#3385FF" id="expressionTotalPage"></b>页</span>
	                <span>当前第<b style="color:#3385FF" id="expressionCurrentPage"></b>页</span>
	                <a class="PageButton" onclick="headerDocument.getPreviousPageExpression()"><b style="color:#3385FF">上一页</b></a>
	                <a class="PageButton" onclick="headerDocument.getNextPageExpression()"><b style="color:#3385FF">下一页</b></a>
	                <span class="currentPage">跳转到 <input id="expression_pageNum" class="form-control input-sm" style=" width: 53px;text-align:center;IME-MODE: disabled;" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" > 页 <button type="button" class="btn btn-default btn-sm" style="height:28px;margin-top: -5px;" onclick="headerDocument.jumpToExpression()" >跳转</button></span>
	            </div>
	</div>
	<!-- fancytree 树形结构显示 -->
	<div id="show_fancytree_div_id"  title="Omaha术语树" style="display:none; ">
		<div style="font-size: 1px;display:none">
			<button class="control-button" id="show_fancytree_option">设置</button>
			<button class="control-button" id="btnResetSearch">&times;</button>
			<input style="width:15vh" name="search"  placeholder="Filter..." autocomplete="off"/>
			<span id="matches"></span>
			 <span id="echoSelection1"></span>
		</div>
		<div id="show_fancytree_search_div_id" style="font-size: 1px;display:none">
			<input style="width:30vh" name="searchByData" id="show_fancytree_search_id"/>
			<input style="display:none" value="1" name="searchByDataDataStar" id="show_fancytree_search_dataStart_id"/>
			<input style="display:none" value="0" name="searchByDataCount" id="show_fancytree_search_Count_id"/>
			<button class="control-button" id="show_fancytree_search_submit_id" onclick="searchFancytree(1)">搜索</button>
			<div id="container_fancytree_search_swait"  style="min-width:100px;height:100px;text-align:center;display:none;">
				<img src="${pageContext.request.contextPath}/resources/images/waiting.gif">
						检索中,请稍后...
			</div>
			<div class="search_list" style="display: none; bottom: 0;left: 13px;min-width: 200px;" id="show_fancytree_search_list_div_id">
				<select id="show_fancytree_search_list_select_id" class="form-control" size="20"></select>
				<button class="control-button" id="show_fancytree_search_submit_Last_id" onclick="searchFancyLast()">上一页</button>
				<span id="show_fancytree_search_pageNum"></span>
				<button class="control-button" id="show_fancytree_search_submit_Next_id" onclick="searchFancyNext()">下一页</button>
			</div>
		</div>
		<div id="show_fancytree_option_div" style="font-size:10px;display:none">
			<fieldset>
				<label for="regex">
					<input type="checkbox" id="regex">
					Regular expression
				</label>
				<br>
				<label for="hideMode">
					<input type="checkbox" id="hideMode" checked="checked">
					Hide unmatched nodes
				</label>
				<label for="autoExpand">
					<input type="checkbox" id="autoExpand" checked="checked">
					Auto expand
				</label>
				<label for="branchMode">
					<input type="checkbox" id="branchMode">
					Match whole branch
				</label>
				<label for="leavesOnly">
					<input type="checkbox" id="leavesOnly">
					Match end nodes only
				</label>
				<br>
				<label for="fuzzy">
					<input type="checkbox" id="fuzzy">
					Fuzzy
				</label>
				<label for="hideExpanders">
					<input type="checkbox" id="hideExpanders">
					hideExpanders
				</label>
				<label for="highlight">
					<input type="checkbox" id="highlight" checked="checked">
					Highlight
				</label>
				<label for="nodata">
					<input type="checkbox" id="nodata" checked="checked">
					nodata
				</label>
				<br>
				<label for="counter">
					<input type="checkbox" id="counter" checked="checked">
					Add counter badges
				</label>
				<label for="hideExpandedCounter">
					<input type="checkbox" id="hideExpandedCounter" checked="checked">
					hideExpandedCounter
				</label>
			</fieldset>
		</div>	
	
		<div id="container_fancytree_swait"  style="min-width:100px;height:100px;text-align:center;display:none;">
				<img src="${pageContext.request.contextPath}/resources/images/waiting.gif">
						术语库生成中,请稍后...
		</div>
		<div id="fancytree_div_id" >
		   <p id="sampleButtons"></p>
			<div id="fancytree_id">
	        </div>
		</div>
	</div>
	<!-- 高级检索按钮弹出层 -->
	<div id="AdvancedSearchDialog" title="高级检索" style="display:none;">
		<div class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-1 control-label" style="width:100px;">集合选择:</label>
				<div class="col-sm-3">
					<select tabIndex="1" class="form-control" id="select_searchSet" onchange="headerDocument.getSearchField()"></select>
				</div>
			</div>
		</div>
		<div class="row filterConfigArea">
			<div class=" form-group filterConfigFields col-lg-4">
				<form style="white-space:nowrap;">
					<label class="control-label">字段:</label>
					<input id="select_searchField_Search" type="text" class="form-control" onkeyup="searchFieldMatch(this)"style="width: 100%;" /> 
				</form>
				<select tabIndex="2" class="form-control columns" size="10" id="select_searchField" onchange="headerDocument.getSearchOperator()"></select>
			</div>
			<div class="filterConfigOperate col-lg-4">
				<div class="form-group">
					<label class="control-label">运算符:</label> 
					<select tabIndex="3" class="form-control" data-role="FilterArg.Operators" id="select_searchOperator"></select>
				</div>
					<form name="form1"  onkeydown="returnResat(event)" id="834191984">
					<div class="form-group">
						<label class="control-label">检索值:</label>
		     				<input tabIndex="4" type="text" class="form-control" data-role="FilterArg.FilterValue" id="searchValue" onfocus="getSearchTip()" onkeyup="getSearchTip(event)" onkeydown="searchTipFocus(event)" >
		     				<!-- 输入框获取焦点弹出框 -->
							<div class="search_list" style="display: none; bottom: 0;left:15px"	id="searchKeyword">
								<select id="search_tip_list" class="form-control" size="18" onkeydown="enterKeySetSearchTipValue(event)" onclick="setSearchTipValue()" ></select>
							</div>
							  <input  type="text" id="table_field" hidden="hidden"/>
					</div>
	 			    </form>
				<div class="form-group">
					<label class="control-label">与前一检索条件关系:</label>
					<select tabIndex="5" class="form-control" data-role="FilterArg.Logic" id="select_logicRelation">
						<option value="AND" selected="selected">AND</option>
						<option value="OR">OR</option>
						<option value="NOT">NOT</option>
					</select>
				</div>
				<div class="clearfix filterConfigButton">
					<a tabIndex="6" class="btn btn-primary AddFilterButton" onclick="addSearchValue()">新增条件</a>
					<a tabIndex="6" class="btn btn-primary AddFilterButton" onclick="addSearchSign('(','前括号')">追加"("</a>
					<a tabIndex="6" class="btn btn-primary AddFilterButton" onclick="addSearchSign(')','后括号')">追加")"</a>
				</div>
			</div>
			<div class="form-group filterConfigInfo col-lg-4">
				<label class="control-label">已选检索条件:</label>
				<select tabIndex="7" id="select_advancedSearchExpression" class="form-control columns" size="12" data-role="FilterArg.SelectedFilter"></select>
			</div>
		</div>
		<div class="row filterConfigArea">
		    <label class="control-label" style="margin: 5px;">预览检索条件:</label>
		    <textarea rows="4" cols="30" id="searchShow" style="margin-top: 5px;margin-left:15px; width: 97%;resize:none;"></textarea>
		</div>
	</div>
	<!-- ******************************************************权限申请 **********************************************************-->
	<div id="div_privilege_Approval" title="权限申请" style="display:none;overflow-y:hidden;" class="bootstrap-table">
		<div class="questionForm">
			<div class="form-group col-sm-12">
				<label class="control-label col-sm-4" >权限类型:</label>
				<div class="col-sm-8">
					<select class="form-control" id="select_privilege_Approval" name="SortOrderBy" onchange="selectApproval()">
                            <option selected="selected" value="1">临时权限</option>
                           	<option  value="0">永久权限</option>
                    </select> 
				</div>
			</div>
			<div class="form-group col-sm-12" id="valid_startTime_div" >
				<label class="control-label col-sm-4">权限生效开始时间:</label>
				<div class="col-sm-8" onclick="showTime('valid_startTime_id')">
					<input class="form-control" id="valid_startTime_id"  disabled="disabled"/>
				</div>
			</div>
			<div class="form-group col-sm-12" id="valid_EndTime_div">
			  <label class="control-label col-sm-4">权限生效结束时间:</label>
				<div class="col-sm-8" onclick="showTime('valid_EndTime_id')">
					<input class="form-control" id="valid_EndTime_id" disabled="disabled"/>
				</div>
			</div>
			<div class="form-group col-sm-12" id="privilege_Approval_Description_div">
			  <label class="control-label col-sm-4">理由:</label>
				<div class="col-sm-8">
					<textarea id="privilege_Approval_Description_id" maxlength="450"  rows='6' cols='30' class="form-control"   style="resize:none;float: left;width: 100%;">
					</textarea>
				</div>
			</div>
			<div style="display:none"><form id='form_role_id_privilege_Approval' method='post' action=""> <input id="input_role_id_privilege_Approval"/></form></div>
		</div>
	</div>
	<div id="div_privilege_Approval_Field" title="权限申请" style="display:none;overflow-y:hidden;" class="bootstrap-table">
		<div class="questionForm">
			<div class="form-group col-sm-12">
				<label class="control-label col-sm-6" style="width:130px;">可检索字段:</label>
				<div class="col-sm-6">
					<div id="div_solrTableAndField" title="检索返回字段勾选"
									style="text-align:left;">
									<ul id="solrTableAndFieldTree" class="ztree" style="overflow:auto;"></ul>
					</div>
				</div>
			</div>
		</div>
	</div>
 <div class="modal fade bs-example-modal-lg" id="showPrivilegeColumnItem" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content" style="height:500px; width:350px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="showPrivilegeColumnItem_Label">权限分配</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group" >
	                 	<div class="btn-group" role="group">
	                         <!-- <button type="button" onclick="getSolrDBFieldInfo()" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	                                                                                           展示字段1</button> -->
	                         <!-- Time screening -->
							<div class="c-tip-con dropdown-button" id="return_search" >
			
								
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
								<!-- <div class="modal-footer">
				                    <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
				                    <button type="button" id="btn_submit" class="btn btn-primary" data-dismiss="modal" onclick="saveAddPriFieldValue()"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
				                </div> -->
							</div>
						</div>
                 </div>
            </div>
        </div>
    </div>
    </div>
    <!-- ******************************************************权限申请 **********************************************************-->
	<div id="div_diseaseTableAndField" title="数据导出字段勾选" style="display:none;">
		<ul id="diseaseTableAndFieldTree" class="ztree" style="overflow:auto;"></ul>
	</div>
	<div id="div_StatisticsTableAndField" title="科研统计字段选择" style="display:none;">
		<ul id="StatisticsTableAndFieldTree" class="ztree" style="overflow:auto;"></ul>
	</div>
	<div id="div_diseaseShow" title="病种库展示字段选择" style="display:none;">
		<ul id="div_diseaseShow_Tree" class="ztree" style="overflow:auto;"></ul>
	</div>
	
	<div id="AjaxLoading" class="showbox" style="opacity: 1; display: none; margin-top: 300px;">
		<div class="loadingWord" style="width:200px;"><img src="${pageContext.request.contextPath}/resources/images/waiting.gif"/>获取病种库详情中...</div>
	</div>
	
	<div id="div_bingli" title="病历内容详情" style="display:none;">
		<div id="showView" style="text-align: left;">
			<span id="showView_span" style="white-space:pre-wrap"></span>正在加载,请稍后...
		</div>
	</div>
	<div id="wait" class="showbox" style="opacity: 1; display: none; margin-top: 300px;">
		 <div class="loadingWord"><img src="${pageContext.request.contextPath}/resources/images/waiting.gif"><span id="wait_show">统计中,请稍后...</span></div>
	</div>
	
	<form name="SSO-login" method="post" action="http://10.40.22.97:7011/sso/" target="_self">            
       <input type="hidden" name="SSOAction" value="ClientAuthenticate"/>
       <input type="hidden" id="loginClientUrl" name="ClientUrl"/>  
       <input type='hidden' id='AppName_id' name="AppName" value="临床大数据检索" />   
    </form>

	<form id="logout_id" name='SSO-logout' method='post' target="_self">  
        <input type='hidden' id='logoutClientUrl' name='ClientUrl' />                            
        <input type='hidden' id='AppName_logout_id' name="AppName" value="临床大数据检索" />
        <input type="hidden" id="token_id" name="token"/>                            
    </form>
    <!-- 
          退出登录地址
          精卫医院
    <form id="logout_id" name='SSO-logout' method='post' action="http://10.40.22.97:7011/sso/home/logout?appname=临床大数据检索" target="_self">  
        <input type='hidden' id='logoutClientUrl' name='ClientUrl' />                            
        <input type='hidden' id='AppName_logout_id' name="AppName" value="临床大数据检索" />                            
    </form>
           大华医院
    <form id="logout_id" name='SSO-logout' method='post' action="http://202.202.202.182:6880/sso/home/logout?appname=临床大数据检索" target="_self">  
        <input type='hidden' id='logoutClientUrl' name='ClientUrl' />                            
        <input type='hidden' id='AppName_logout_id' name="AppName" value="临床大数据检索" />                            
    </form>
           长海医院
    <form id="logout_id" name='SSO-logout' method='post' action="http://172.16.32.111:6880/sso/home/logout?appname=临床大数据检索" target="_self">  
        <input type='hidden' id='logoutClientUrl' name='ClientUrl' />                            
        <input type='hidden' id='AppName_logout_id' name="AppName" value="临床大数据检索" />                            
    </form>
     -->
    <div style="display:none">
	<form id='SmartFile' method='post' action="/hssp/body/SmartFile">  
        <input type='hidden' id='userID_id' name="userID" />                            
        <input type='hidden' id='uploadMessageID_id' name="uploadMessageID" />  
    </form>
	<input type="hidden" id="hideinput"/>
	<input type="hidden" id="hideDiseaseDBClassifyId"/>
	<input type="hidden" id="hideDiseaseDBClassifyIdList"/>
	<input type="hidden" id="hideDiseaseDBClassifySubId"/>
	<input type="hidden" id="hideDiseaseDBClassifySubIdList"/>
	<input type="hidden" id="hideDiseaseDBClassifyName"/>
	<input type="hidden" id="hideDiseaseDBClassifySubName"/>
	<input type="hidden" id="hideDiseaseDBName"/>
	<input type="hidden" id="hideUserIdentity"/>
	<input type="hidden" id="SSOUserName"/>
	 <input type="hidden" id="token"/>  
	<input type="hidden" id="hideUserDepartment"/>
	<input type="hidden" id="tableAndField" value="1"/>
	<input type="text" name="limit_time_start" value ="1" id="limit_time_start_Id" />
	<input type="text" name="limit_time_end" value ="" id="limit_time_end_Id" />
	<input type="text" name="select_wordCloud_top" value ="" id="select_wordCloud_top_id" />
	<input type="text" name="select_wordCloud_department" value ="" id="select_wordCloud_department_Id" />
    </div>
	<!-- 以下均为隐藏属性的文本框,用于存放临时数据 -->
	
<script>
document.onclick = function(){
	//关闭搜索提示框,当聚焦的元素不为搜索提示框时，隐藏搜索提示框
	if (document.activeElement.id != "search_tip_list" && document.activeElement.id != "searchValue")
	{
		document.getElementById("searchKeyword").style.display = "none";
	}
};
$(document).ajaxComplete(function (event,xhr,settings){
   if(xhr.getResponseHeader("sessionstatus")=="timeout"){
      loginAgain(xhr.getResponseHeader("loginPath"));
   }

});
//权限时间选择显示或隐藏
function selectApproval(){
	if($("#select_privilege_Approval").val() == 0){
		$("#valid_startTime_div").hide();
		$("#valid_EndTime_div").hide();
	}else{
		$("#valid_startTime_div").show();
		$("#valid_EndTime_div").show();
	}
}
//权限申请展示
function applyforShow(role_id){
	$("#div_privilege_Approval").dialog("open");
	$("#input_role_id_privilege_Approval").val(role_id);
}
$("#div_privilege_Approval").dialog(
		{
			autoOpen: false,
			width: $(window).width()/2,
			buttons: [
				{
					text: "确 认",
					click: function() 
					{
						submitApplyfor();
					}
				},
				{
					text: "关 闭",
					click: function() 
					{
						$(this).dialog("close");
					}
				}
			]
		});
//提交申请
 function submitApplyfor(){
	 //privilegeApprovalDocument
	 $.ajax({
			type : "post",
			url : "/hssp/rest/AddRoleByuserForApproval",
			data : "role_id=" +$("#input_role_id_privilege_Approval").val() +
			       "&valid_EndTime=" + $("#valid_EndTime_id").val() +
			       "&valid_startTime=" + $("#valid_startTime_id").val() +
			       "&privilege_Type=" + $("#select_privilege_Approval").val() +
			       "&Approval_Description=" + $("#privilege_Approval_Description_id").val() ,
			dataType: "json",
   	     	success: function(data){
   	    	 if(data.result == "succeed"){
   	    		parent.$.dialog("添加申请成功！", false, 1000);
   	    		$("#div_privilege_Approval").dialog("close");
   	    		$("#valid_startTime_id").val("");
   	    		$("#valid_EndTime_id").val("");
   	    		$("#privilege_Approval_Description_id").text("");
   	    		privilegeApprovalDocument.showPrivilegeByApproval();
   	    	 }else{
   	    		parent.$.dialog("添加申请失败！", false, 1000);
   	    	 }
   	     }
	 });
}
//跳转到结果展示页面
function gotoResult() 
{
	var expression = $("#txtKeyword", prebodyDocument.document).val();
	document.getElementById("hideinput").value = expression;
	 $("#prebody").hide(1000);
	resetResultHeight();
	resultsDocument.getExpressionFromIndex();
	resultPage = true;
	$("#results").show(1000);
}
function resetResultHeight(){
	var windows_height = $(window).height();
	document.getElementById("results").style.height = windows_height-60;
	document.getElementById("results").style.display = "block";
}
function resetCenterHeight(id){
	var windows_height = $(window).height();
	document.getElementById(id).style.height = windows_height-60;
}
var resultPage = false;
//跳转到结果展示页面
function gotoResultRe(expression) 
{
	document.getElementById("hideinput").value = expression;
	document.getElementById("prebody").style.display = "none";
	var windows_height = $(window).height();
	document.getElementById("results").style.height = windows_height;
	document.getElementById("results").style.display = "block";
	resultsDocument.getExpressionFromIndex();
}

function showPrivilegeApproval (){
	/* var htmlst=" <iframe id=\"export_show\"  name=\"export_show_name\" frameBorder=\"0\" scrolling=\"no\"style=\"border:0;padding:0;margin:0;width:100%;height:100%; position:relative; z-index:1\"></iframe> ";
	$("#div_diseaseDBDetail_local_Export").html(htmlst); */
	hideAll();
	//resetCenterHeight("privilegeApproval");
	//document.getElementById("privilegeApproval").style.display = "block";
	 $("#privilegeApproval").show(1000);
	//privilegeApprovalDocument.showPrivilegeSelf('0,1');
	 $("#search_Privilege_ALL",privilegeApprovalDocument.document).click();
	 $("#search_Privilege_Self",privilegeApprovalDocument.document).click();
	 $("#showSearch",headerDocument.document).show();
	 checkButton("showPrivilegeApproval");
}

function removeCheckButton(){
	$("#showPrivilegeApproval",headerDocument.document).removeClass("checkButton");
	 $("#showPrivilegeApproval",headerDocument.document).removeAttr("style","");
	 
	 $("#showExport",headerDocument.document).removeClass("checkButton");
	 $("#showExport",headerDocument.document).removeAttr("style","");
}
function checkButton(id){
	 $("#"+id,headerDocument.document).addClass("checkButton");
	 $("#"+id,headerDocument.document).css("position","relative");
}

function showSearch(){
	hideAll();
	if(resultPage){
		$("#results").show(1000);
		resetCenterHeight("results");
	}else{
		$("#prebody").show(1000);
		resetCenterHeight("prebody");
	}
	 $("#showSearch",headerDocument.document).hide();
}
function hideAll(){
	 $("#prebody").hide(1000);
	 $("#results").hide(1000);
	 $("#privilegeApproval").hide(1000);
	 $("#export").hide(1000);
	 $("#showExport_a",headerDocument.document).removeClass("checkNo");
	 removeCheckButton();
}
function getURL(){  
	   var curWwwPath = window.document.location.href;  
	   //获取主机地址之后的目录，如： test/test/test.htm  
	   var pathName = window.document.location.pathname;  
	   var pos = curWwwPath.indexOf(pathName); //获取主机地址，如： http://localhost:8080  
	   var localhostPaht = curWwwPath.substring(0, pos); //获取带"/"的项目名，如：/web
	   var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);  
	   var rootPath = localhostPaht + projectName;  
	   return localhostPaht.replace("8081","8086");  
	     
}  

function showExport(){
	hideAll();
	//document.getElementById("privilegeApproval").style.display = "none";
	var windows_height = $(window).height()-60;
	resetCenterHeight("export");
	document.getElementById("export").src=getURL()+"/export/prebody?u="+$("#hideUserIdentity").val()+"&h="+windows_height;
	$("#showSearch",headerDocument.document).show();
	$("#export").show();
	$("#showExport_a",headerDocument.document).addClass("checkNo");
	checkButton("showExport");
}

function SelectFile(){
	var fileAccept = $("#annex").val().split(".")[1];//获取上传文件的后缀
	var fileFilter = ".xls,.xlsx,.doc,docx,.txt,.pdf,.jpg,.jpeg,.png,";
	if(typeof(fileAccept) == "undefined" ){
		return;
	}
	if( fileFilter.indexOf("."+fileAccept) < 0 ){
		$("#annex").val("");
		//parent.$.dialog("只可以上传后缀名为.xls,.xlsx,.doc,docx,.txt,.pdf,.jpg,.jpeg,.png类型的文件", false, 1500);
		//alert("只可以上传后缀名为.xls,.xlsx,.doc,docx,.txt,.pdf,.jpg,.jpeg,.png类型的文件");
		parent.$.dialog("只可以上传后缀名为.xls,.xlsx,.doc,docx,.txt,.pdf,.jpg,.jpeg,.png类型的文件!", false, 1500);
	}
    if(!limitFileSize($("#annex")[0],'50MB')){
    	parent.$.dialog("文件大小不可以超过50兆!", false, 1500);
    }
}
function limitFileSize (file, limitSize) {
    var arr = ["KB", "MB", "GB"]
    var limit = limitSize.toUpperCase();
    var limitNum = 0;
    for (var i = 0; i < arr.length; i++) {
        var leval = limit.indexOf(arr[i]);
        if (leval > -1) {
            limitNum = parseInt(limit.substr(0, leval)) * Math.pow(1024, (i + 1))
            break
        }
    }
    if (file.size > limitNum) {
        return false
    }
    return true
}
function showExport1(){
	jumpExport();
	return;
	var htmlst=" <iframe id=\"export_show\"  name=\"export_show_name\" frameBorder=\"0\" scrolling=\"no\"style=\"border:0;padding:0;margin:0;width:100%;height:100%; position:relative; z-index:1\"></iframe> ";
	$("#div_diseaseDBDetail_local_Export").html(htmlst);
	//$("#export_show").attr("src","http://192.168.0.4:8086/export?token="+$("#token").val());
	$("#export_show").attr("src","http://172.22.128.129:8080/export?u="+$("#hideUserIdentity").val());
	$("#div_diseaseDBDetail_local_Export").dialog("open");
}
var toBase64 = function (val) {
    var b = new Base64();
    return b.encode(val);
 }
function loginAgain (url){
  window.location.replace(url);
}
document.onkeydown = killesc;
function killesc (){
   if(window.event.keyCode == 27){	
      window.event.keyCode =0;
      window.event.returnValue = false;
   }
}
	//用户退出登录操作
	function ssoUserLogout()
	{
	    <%String logout_localUrl = request.getRequestURL().toString();%>;
		$("#logoutClientUrl").val('<%=logout_localUrl%>');
		DelCookie('SSOToken');
		DelCookie('SSOUserIdentity');
		loginout();
	}
	//用户退出登录操作
	function ssoUserLogout_new()
	{
		DelCookie('SSOToken');
		DelCookie('SSOUserIdentity');
		loginout();
	}
	function DelCookie(name)
	{
		var exp = new Date();
		exp.setTime (exp.getTime() - 2);
		document.cookie = name + "=" + "" + "; expires="+ exp.toGMTString();
	}
	function loginout()
	{
		$("#token_id").val($("#token").val())
		$.ajax(
		{
			type : "post",
			url : "/hssp/head/loginOut",
			data : "Identity=" + $("#hideUserIdentity").val(),
			dataType : "json",
			success : function(result) 
			{
			   if(result.login == 3){
				   window.location.reload();
			   }else if(result.login == 1){
				   $("#logout_id").attr('action',result.SSOURL+"/home/logout?appname=临床大数据检索");    
	               $("#logout_id").submit(); 
			   }
			}
		});
	}
	function setName(){
		  $("#ssoUserName",headerDocument.document).html("欢迎您," + $("#SSOUserName").val());
		  if($("#hideUserIdentity").val().toLowerCase() == "sys" ||$("#hideUserIdentity").val().toLowerCase() == "kpi" || $("#hideUserIdentity").val().toLowerCase() == "ss"){
		     $("#diseaseCountDetail",headerDocument.document).show();
		     $("#wordCloud",headerDocument.document).show();
		  }else{
		     $("#diseaseCountDetail",headerDocument.document).hide();
		  }
	}

	//病种库选择
	function patientSelectClassify (){
		$("#select_diseaseClassify_div_local option").each(function(i,n){
			$("#select_diseaseClassify_Child_"+$(n).val()).hide();
        })
		var select_diseaseClassify_Child_id = "select_diseaseClassify_Child_"+$("#select_diseaseClassify_local").val();
		$("#"+select_diseaseClassify_Child_id).show();
		getDiseaseDBNameList();
	}
	var dieaseNames_html_static="";
	//获取某分类指定所有的指定列名称
	function getDiseaseDBNameList()
	{
	    var select_diseaseClassify_Child_id = "select_diseaseClassify_Child_"+$("#select_diseaseClassify_local").val();
	   // alert(select_diseaseClassify_Child_id+">>>>select_diseaseClassify_Child_14");
	    //alert($("#select_diseaseClassify_Child_14",parent.document).val());
	    //alert($("#"+select_diseaseClassify_Child_id,parent.document).val());
	    var DISEASECLASSIFYID = $("#select_diseaseClassify_local",parent.document).val();
	    var DISEASECLASSIFYCHIIDID = $("#"+select_diseaseClassify_Child_id,parent.document).val();
	    //alert("DISEASECLASSIFYCHIIDID:"+DISEASECLASSIFYCHIIDID);
	    //alert("&USERID=" + $("#hideUserIdentity", parent.document).val() + "&DISEASECLASSIFYID=" + DISEASECLASSIFYID + "&DISEASECLASSIFYCHIIDID=" + DISEASECLASSIFYCHIIDID + "&department=" + $("#hideUserDepartment").val());
		$.ajax(
		{
			type : "post",
			url : "/hssp/tail/getDiseaseDBNameList",
			data :  "&USERID=" + $("#hideUserIdentity").val() + "&DISEASECLASSIFYID=" + DISEASECLASSIFYID + "&DISEASECLASSIFYCHIIDID=" + DISEASECLASSIFYCHIIDID + "&department" + $("#hideUserDepartment").val(),
			dataType : "json",
			success : function(result) 
			{
				var diseaseDBNameList = eval(result.diseaseDBName);
				var diseaseIDList = eval(result.diseaseIDList);
				var jurisdictionIDList = eval(result.jurisdictionIDList);
				//select_diseaseClassify_Child_dieaseName_div
				var dieaseNames_html = "";
				var jurisdictionIDList_Show = "";
				for ( var i = 0; i < diseaseDBNameList.length; i++) {
					var dieaseNames_ID = diseaseIDList[i];
					var dieaseNames_Name = diseaseDBNameList[i];
					dieaseNames_html = dieaseNames_html
							+ "<option value=\""+dieaseNames_ID+"\">";
					dieaseNames_html = dieaseNames_html
							+ dieaseNames_Name;
					dieaseNames_html = dieaseNames_html
							+ "</option>";
				}
				dieaseNames_html_static = dieaseNames_html;
				if(jurisdictionIDList.length > 0){
				  jurisdictionIDList_Show = jurisdictionIDList[0];
				  //选中权限默认权限 jurisdiction
				 // $("#jurisdiction option:[value='"+jurisdictionIDList_Show+"']").prop("selected", 'selected');
				}
				//dieaseNames_html = dieaseNames_html+"<option value=\"-1\">"+"<input class=\"form-control\" id=\"diseaseDBName\" onkeyup=\"this.value=this.value.replace(/[^\u4e00-\u9fa5\w]/g,'')\"/>"+ "</option>";
				dieaseNames_html = "<select  class=\"form-control\" id=\"diseaseDBName\">"
						+ dieaseNames_html + "</select>";
				$("#select_diseaseClassify_Child_dieaseName_div").html(dieaseNames_html);
			}
		});
	}
	function getDiseaseDBNameListLocal()
	{
		$.ajax(
		{
			type : "post",
			url : "/hssp/tail/getDiseaseDBNameListLocal",
			data :  "&USERID=" + $("#hideUserIdentity").val(),
			dataType : "json",
			success : function(result) 
			{
				var diseaseDBNameList = eval(result.diseaseDBName);
				var diseaseIDList = eval(result.diseaseIDList);
				//select_diseaseClassify_Child_dieaseName_div
				var dieaseNames_html = "";
				var jurisdictionIDList_Show = "";
				for ( var i = 0; i < diseaseDBNameList.length; i++) {
					var dieaseNames_ID = diseaseIDList[i];
					var dieaseNames_Name = diseaseDBNameList[i];
					dieaseNames_html = dieaseNames_html
							+ "<option value=\""+dieaseNames_ID+"\">";
					dieaseNames_html = dieaseNames_html
							+ dieaseNames_Name;
					dieaseNames_html = dieaseNames_html
							+ "</option>";
				}
				dieaseNames_html_static = dieaseNames_html;
				dieaseNames_html = "<select  class=\"form-control\" id=\"diseaseDBName\">"
						+ dieaseNames_html + "</select>";
				$("#select_diseaseClassify_Child_dieaseName_Local_div").html(dieaseNames_html);
			}
		});
	}
	
	//病种库选择
	function diseaseSelectClassifyChild (){
		$("#select_diseaseClassify_div_local option").each(function(i,n){
			$("#select_diseaseClassify_Child_"+$(n).val()).hide();
        })
		var select_diseaseClassify_Child_id = "select_diseaseClassify_Child_"+$("#select_diseaseClassify_local").val();
		$("#"+select_diseaseClassify_Child_id).show();
	}
	var headerDocument;
	var prebodyDocument;
	var footerDocument;
	var resultsDocument;
	var privilegeApprovalDocument;
	
	if (navigator.userAgent.toLowerCase().indexOf("msie") > 0)
	{
		headerDocument = document.frames["header"];
		prebodyDocument = document.frames["prebody"];
		footerDocument = document.frames["footer"];
		resultsDocument = document.frames["results"];
		privilegeApprovalDocument = document.frames["privilegeApproval"];
	}
	else
	{
		headerDocument = document.getElementById("header").contentWindow;
		prebodyDocument = document.getElementById("prebody").contentWindow;
		footerDocument = document.getElementById("footer").contentWindow;
		resultsDocument = document.getElementById("results").contentWindow;
		privilegeApprovalDocument = document.getElementById("privilegeApproval").contentWindow;
	}
	var setting = {
			data : {
				simpleData : {
					enable : true,
					idKey : "id",
					pIdKey : "pId",
					rootPId : 0
				},
				key : {
					name : 'name'
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
	//var treeNodes = [{"id":1,"pId":0,"name":"诊断"},{"id":11,"pId":1,"name":"诊断类型"},{"id":12,"pId":1,"name":"icd-10"},{"id":13,"pId":1,"name":"病人唯一标识"},{"id":14,"pId":1,"name":"院内诊断"},{"id":2,"pId":0,"name":"病历"},{"id":21,"pId":2,"name":"病历"},{"id":22,"pId":2,"name":"病区"},{"id":23,"pId":2,"name":"床位"},{"id":24,"pId":2,"name":"科室"},{"id":25,"pId":2,"name":"病人唯一标识"},{"id":3,"pId":0,"name":"基本信息"},{"id":31,"pId":3,"name":"病人唯一标识"},{"id":32,"pId":3,"name":"病区"},{"id":33,"pId":3,"name":"科室"},{"id":34,"pId":3,"name":"性别"},{"id":35,"pId":3,"name":"年龄"},{"id":36,"pId":3,"name":"病人唯一标识"},{"id":4,"pId":0,"name":"药品医嘱"},{"id":41,"pId":4,"name":"病人唯一标识"},{"id":42,"pId":4,"name":"病区"},{"id":43,"pId":4,"name":"科室"},{"id":44,"pId":4,"name":"药品"},{"id":45,"pId":4,"name":"长期医嘱"},{"id":46,"pId":4,"name":"给出频次"},{"id":47,"pId":4,"name":"医嘱类别"},{"id":48,"pId":4,"name":"医嘱状态"},{"id":5,"pId":0,"name":"门诊药品处方明细"},{"id":51,"pId":5,"name":"病人唯一标识"},{"id":52,"pId":5,"name":"药品"},{"id":53,"pId":5,"name":"给出频次"},{"id":6,"pId":0,"name":"手术诊断"},{"id":61,"pId":6,"name":"病人唯一标识"},{"id":62,"pId":6,"name":"诊断类型"},{"id":63,"pId":6,"name":"诊断"},{"id":7,"pId":0,"name":"手术明细"},{"id":71,"pId":7,"name":"病人唯一标识"},{"id":72,"pId":7,"name":"切口类型"},{"id":73,"pId":7,"name":"手术"},{"id":74,"pId":7,"name":"手术部位"},{"id":75,"pId":7,"name":"手术规模"},{"id":76,"pId":7,"name":"愈合情况"},{"id":8,"pId":0,"name":"检验明细"},{"id":81,"pId":8,"name":"病人唯一标识"},{"id":82,"pId":8,"name":"检验结果"},{"id":83,"pId":8,"name":"检验明细"},{"id":84,"pId":8,"name":"检验明细"}];
	var zTree;
	var treeNodes;
	function diseaseTableAndFieldDisplay(id) {  
	    zTree = $.fn.zTree.init($("#"+id),setting, treeNodes);  
	 }
	$("#div_diseaseTableAndField").dialog(
	{
		autoOpen: false,
		width: $(window).width()/2,
		
		buttons: [
			{
				text: "确 认",
				click: function() 
				{
					var tableAndField = "";
					var nodeList = zTree.getNodes();
					var nodeArr = zTree.transformToArray(nodeList);
					var numCheck =0 ;
					var tableName = "";
					for (var i = 0,size = nodeArr.length; i < size; i++)
					{
						if (nodeArr[i].pId != 0 && nodeArr[i].checked)
						{
							numCheck++;
							if (nodeArr[i].name == "病历内容")
							{
								if (confirm("病历内容数据较大,确认是否勾选?") == true)
								{
									if (tableAndField == "")
										tableAndField = tableAndField + tableName + ":" + nodeArr[i].name;							
									else							
										tableAndField = tableAndField + "," + tableName + ":" + nodeArr[i].name;
								}else{
									return;
								}
							}
							else
							{
								if (tableAndField == "")
									tableAndField = tableAndField + tableName + ":" + nodeArr[i].name;							
								else							
									tableAndField = tableAndField + "," + tableName + ":" + nodeArr[i].name;
							}
						}else{
							
							if(nodeArr[i].pId == 0){
								tableName = nodeArr[i].name;
							}
							
						}
					}
					$(this).dialog("close");
					if(numCheck == 0){
						parent.$.dialog("未选择导出字段!", false, 1500);
						resultsDocument.closeBg();
					}else if(numCheck <= 500){
						if(tableAndField.replace(" ", "").length > 0){
						    parent.$.dialog("创建导出成功，请稍后查看!", false, 1500);
							footerDocument.exportDiseaseDB(tableAndField);
						}
					}else{
						alert("选择最大为500个,请确认导出字段数目！");
					}
					
				}
			},
			{
				text: "关 闭",
				click: function() 
				{
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});
	$("#div_StatisticsTableAndField").dialog(
	{
		autoOpen: false,
		width: $(window).width()/2,
		
		buttons: [
			{
				text: "确 认",
				click: function() 
				{
					var tableAndField = "";
					var nodeList = zTree.getNodes();
					var nodeArr = zTree.transformToArray(nodeList);
					var numCheck =0 ;
					var tableName = "";
					for (var i = 0,size = nodeArr.length; i < size; i++)
					{
						if (nodeArr[i].pId != 0 && nodeArr[i].checked)
						{
							numCheck++;
							if (nodeArr[i].name == "病历内容")
							{
								if (confirm("病历内容数据较大,确认是否勾选?") == true)
								{
									if (tableAndField == "")
										tableAndField = tableAndField + tableName + ":" + nodeArr[i].name;							
									else							
										tableAndField = tableAndField + "," + tableName + ":" + nodeArr[i].name;
								}else{
									return;
								}
							}
							else
							{
								if (tableAndField == "")
									tableAndField = tableAndField + tableName + ":" + nodeArr[i].name;							
								else							
									tableAndField = tableAndField + "," + tableName + ":" + nodeArr[i].name;
							}
						}else{
							
							if(nodeArr[i].pId == 0){
								tableName = nodeArr[i].name;
							}
							
						}
					}
					$(this).dialog("close");
					if(numCheck == 0){
						parent.$.dialog("未选择导出字段!", false, 1500);
						resultsDocument.closeBg();
					}else if(numCheck <= 500){
						if(tableAndField.replace(" ", "").length > 0){
						    parent.$.dialog("创建导出成功，请稍后查看!", false, 1500);
							footerDocument.saveDiseaseDBStatisticsDB(tableAndField);
						}
					}else{
						alert("选择最大为500个,请确认导出字段数目！");
					}
					
				}
			},
			{
				text: "关 闭",
				click: function() 
				{
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});
	$("#div_diseaseShow").dialog(
	{
		autoOpen: false,
		width: $(window).width()/2,
		
		buttons: [
			{
				text: "确 认",
				click: function() 
				{
					var tableAndField = "";
					var nodeList = zTree.getNodes();
					var nodeArr = zTree.transformToArray(nodeList);
					var numCheck =0 ;
					var tableName = "";
					for (var i = 0,size = nodeArr.length; i < size; i++)
					{
						if (nodeArr[i].pId != 0 && nodeArr[i].checked)
						{
							numCheck ++;
							if (nodeArr[i].name == "病历内容")
							{
								if (confirm("病历内容数据较大,确认是否勾选?") == true)
								{
									if (tableAndField == "")
										tableAndField = tableAndField + tableName + ":" + nodeArr[i].name;							
									else							
										tableAndField = tableAndField + "," + tableName + ":" + nodeArr[i].name;
								}else{
									return;
								}
							}
							else
							{
								if (tableAndField == "")
									tableAndField = tableAndField + tableName + ":" + nodeArr[i].name;							
								else							
									tableAndField = tableAndField + "," + tableName + ":" + nodeArr[i].name;
							}
						}else{
							
							if(nodeArr[i].pId == 0){
								tableName = nodeArr[i].name;
							}
							
						}
					}
					$(this).dialog("close");
					//footerDocument.exportDiseaseDB(tableAndField);
					if(numCheck == 0){
						$("#tableAndField").val("1");
						parent.$.dialog("未选择 展示字段!", false, 1500);
						resultsDocument.closeBg();
					}else if(numCheck <= 30){
						//alert(tableAndField);
						if(tableAndField.replace(" ", "").length > 0){
							$("#tableAndField").val(tableAndField);
							nextOK = false;
							viewDiseaseDB(1);
						}
					}else{
						alert("选择最大为20个,请确认返回字段数目！");
						$("#tableAndField").val("1");
					}
				}
			},
			{
				text: "关 闭",
				click: function() 
				{
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});
	$("#div_diseaseDBView").dialog(
	{
		autoOpen: false,
		width: $(window).width()-100,
		
		buttons: [
			{
				text: "展示字段选择",
				click: function() 
				{
					getDiseaseDBSelectFieldInfo();
				}
			}
			,
			{
				text: "导出",
				click: function() 
				{
					getDiseaseDBFieldInfo();
				}
			},
			{
				text: "返回",
				click: function() 
				{
					nextOK = true;
					$("#tableAndField").val("1");
					$(this).dialog("close");
					$("#div_diseaseDBDetail_local").dialog("open");
				}
			},
			{
				text: "关 闭",
				click: function() 
				{
					$("#tableAndField").val("1");
					nextOK = true;
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});	
	$("#div_diseaseDBClassify").dialog(
	{
		autoOpen: false,
		width: $(window).width()*2/3,
		
		buttons: [
			{
				text: "关 闭",
				click: function() 
				{
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});	
	$("#div_diseaseDBClassifySub").dialog(
	{
		autoOpen: false,
		width: $(window).width()*2/3,
		
		buttons: [
			{
				text: "返回",
				click: function() 
				{
					$(this).dialog("close");
					$("#div_diseaseDBClassify").dialog("open");
				}
			},
			{
				text: "关 闭",
				click: function() 
				{
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});	
//	$("#div_diseaseDBDetail.ui-dialog-titlebar-close").show();
	
	$("#div_diseaseDBDetail").dialog(
	{   closeOnEscape:false,
	    //open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
		autoOpen: false,
		width: $(window).width()-100,
		
		/*
		{
				text: "返回",
				click: function() 
				{
					$(this).dialog("close");
					$("#div_diseaseDBClassifySub").dialog("open");
				}
			},
		*/
		buttons: [
			{
				text: "刷 新",
				click: function() 
				{
					showDiseaseStatu($("#diseaseDBCurrentPage").text());
				}
			},
			
			{
				text: "关 闭",
				click: function() 
				{
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});	
	$("#div_StatisticsDBDetail").dialog(
	{   draggable : false,
		resizable : false,
		modal : true,
	    closeOnEscape:true,
		open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
		autoOpen : false,
		width : $(window).width()*3/4,
		height : $(window).height()-60,
		width: $(window).width()-100,
		
		/*
		{
				text: "返回",
				click: function() 
				{
					$(this).dialog("close");
					$("#div_diseaseDBClassifySub").dialog("open");
				}
			},
		*/
		buttons: [
			{
				text: "刷 新",
				click: function() 
				{
					showStatisticsStatu($("#StatisticsDBCurrentPage").text());
				}
			},
			
			{
				text: "关 闭",
				click: function() 
				{
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});	
	    //open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
		 /*   {
				text: "返回",
				click: function() 
				{
					$(this).dialog("close");
					$("#div_diseaseDBClassifySub").dialog("open");
				}
			},*/
	$("#div_diseaseDBDetail_local").dialog(
	{   closeOnEscape:false,
		autoOpen: false,
		width: $(window).width()-100,
		
		buttons: [
			{
				text: "刷 新",
				click: function() 
				{ 
					headerDocument.getDiseaseDBDetailLocal($("#diseaseDBCurrentPage_local").text());
				}
			},
			
			{
				text: "关 闭",
				click: function() 
				{
			    	resetRefreshExportMessageOk(false);
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});	
	$("#div_diseaseDBDetail_local_Export").dialog(
	{   closeOnEscape:false,
		autoOpen: false,
		width: $(window).width()-100,
		height: $(window).height()-80,
		buttons: [
			{
				text: "关 闭",
				click: function() 
				{
			    	resetRefreshExportMessageOk(false);
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});	
	//标签保存显示
	function showAddTag(dataType,value,VisitID,fieldName,fieldValue){
		/* $("#div_Tag_dataType").val(dataType);
		$("#div_Tag_value").val(value);
		$("#div_Tag_VisitID").val(VisitID); */
		var htmlAddress = getURL().replace("8086","80");
		var htmlst=" <iframe id=\"tag_show\"  name=\"tag_show_name\" frameBorder=\"0\" scrolling=\"no\"style=\"border:0;padding:0;margin:0;width:100%;height:100%; position:relative; z-index:1\"></iframe> ";
		$("#div_Tag_show").html(htmlst);
		$("#tag_show").attr("src","http://192.168.0.30/form?dataType="+dataType+"&value="+value+"&VisitID="+VisitID+"&dataId="+""+"&u="+$("#hideUserIdentity").val());
		$("#div_Tag_show").dialog("open");
		var titleSt ="添加标签 - "+fieldValue+"["+fieldName+"]" ;
		if(dataType == "VKey"){
			titleSt = titleSt+" - 人次【维度】"
		}else if(dataType == "Empiid"){
			titleSt = titleSt+" - 人【维度】"
		}else if(dataType == "OEventsID"){
			titleSt = titleSt+" - 手术【维度】"
		}else if(dataType == "HEventsID"){
			titleSt = titleSt+" - 住院【维度】"
		}
		$("#div_Tag_show").dialog({title:titleSt});
	}
	$("#div_Tag_show").dialog(
	{  	draggable : false,
		resizable : false,
		modal : true,
	    closeOnEscape:true,
		//open:function(event,ui){$(".ui-dialog-titlebar-close").hide();},
		autoOpen : false,
		width: $(window).width()*0.7,
		height: $(window).height()-60,
		buttons: [
			{
				text: "关 闭",
				click: function() 
				{
					$(this).dialog("close");
					
				}
			}
		]
	});	
	$("#div_uploadDetail").dialog(
	{   closeOnEscape:false,
		autoOpen: false,
		width: $(window).width()-100,
		
		buttons: [
			{
				text: "刷 新",
				click: function() 
				{
					headerDocument.getUploadDetail($("#diseaseDBCurrentPage_uploadDetail").text());
				}
			},
			
			{
				text: "关 闭",
				click: function() 
				{
			    	resetRefreshExportMessageOk(false);
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});	
	$("#div_diseaseDBDetail_progres").dialog(
	{   draggable : false,
		resizable : false,
		modal : true,
	    closeOnEscape:false,
		open:function(event,ui){$(".ui-dialog-titlebar-close").hide();},
		autoOpen : false,
		width: $(window).width()/2
	});	
	$("#div_diseaseDBDetail_inner").dialog(
	{   closeOnEscape:false,
	    //open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
		autoOpen: false,
		width: $(window).width()/2
	});	
	/*{
				text: "确 认",
				click: function() 
				{
					$(this).dialog("close");
				}
			},*/
	$("#div_expressioinManage").dialog(
	{   draggable : false,
		resizable : false,
		modal : true,
	    closeOnEscape:true,
		open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
		autoOpen: false,
		width: $(window).width()*2/3,
		height: $(window).height()-60,
		buttons: [
			{
				text: "关 闭",
				click: function() 
				{
					$(this).dialog("close");
				}
			}
		]
	});	
	$("#div_diseaseDBSave").dialog({
	    closeOnEscape:false,
		autoOpen : false,
		width : $(window).width()/2,
		buttons : [
		{
			id : "nextStep",
			text : "确 认",
			click : function() 
			{
				if ($("#select_diseaseClassify_Child_show").val()==null || $("#select_diseaseClassify_Child_show").val().trim() == "")
				{
					parent.$.dialog("病种库不能为空!请重新输入!", false, 1500);
				}
				else
				{    
					footerDocument.saveDiseaseDB();
				}
			}
		}, 
		{
			text : "取 消",
			click : function() 
			{
				$(this).dialog("close");
				resultsDocument.closeBg();
			}
		}]
	});
	
	
	//获取病种库分类信息
	function getDiseaseListAll()
	{
		$.ajax(
		{
			type : "post",
			url : "/hssp/tail/getDiseaseListAll",
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
				$("#select_diseaseClassify_Child_Statistics_div")
						.html(ClassifyChild_html);
			}
		});
	}
	
	
	$("#div_diseaseDBSave_Statistics").dialog({
		draggable : false,
		resizable : false,
		modal : true,
	    closeOnEscape:true,
		open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
		autoOpen: false,
		width : $(window).width()/2,
		buttons : [
		{
			id : "nextStep",
			text : "确 认",
			click : function() 
			{
				if ($("#select_diseaseClassify_Child_Statistics_show").val()==null || $("#select_diseaseClassify_Child_Statistics_show").val().trim() == "")
				{
					parent.$.dialog("病种库不能为空!请重新输入!", false, 1500);
					return;
				}
				 if ($("#diseaseDBName_Statistics").val()==null || $("#diseaseDBName_Statistics").val().trim() == "")
				{
					parent.$.dialog("数据集名称不能为空!请重新输入!", false, 1500);
					return;
				}   
					//footerDocument.saveDiseaseDBStatisticsDB();
					//展示选择字段页面
					getStatisticsDBFieldInfo()
			}
		}, 
		{
			text : "取 消",
			click : function() 
			{
				$(this).dialog("close");
				resultsDocument.closeBg();
			}
		}]
	});
	//数据导出打开按钮
	$("#div_diseaseDBSave_local").dialog({
		draggable : false,
		resizable : false,
		modal : true,
	    closeOnEscape:true,
		open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
		autoOpen: false,
		width : $(window).width()/2,
		buttons : [
		{
			id : "nextStep",
			text : "确 认",
			click : function() 
			{
				if ( $("#diseaseDBName").val() == null || $("#diseaseDBName").val().trim() == "")
				{
					parent.$.dialog("病种库不能为空!请重新输入!", false, 1500);
				}
				else
				{    
					footerDocument.saveDiseaseDBLocal();
				}
			}
		}, 
		{
			text : "取 消",
			click : function() 
			{
				$(this).dialog("close");
				resultsDocument.closeBg();
			}
		}]
	});
	//数据导出打开按钮
	$("#div_diseaseDBSave_local_div").dialog({
		draggable : false,
		resizable : false,
		modal : true,
	    closeOnEscape:true,
		open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
		autoOpen: false,
		width : $(window).width()/2,
		buttons : [
		{
			id : "nextStep",
			text : "确 认",
			click : function() 
			{
				if ($("#diseaseDBName").val() == null || $("#diseaseDBName").val().trim() == "")
				{
					parent.$.dialog("病种库不能为空!请重新输入!", false, 1500);
				}
				else
				{    
					footerDocument.saveDiseaseDBLocalNew();
				}
			}
		}, 
		{
			text : "取 消",
			click : function() 
			{
				$(this).dialog("close");
				resultsDocument.closeBg();
			}
		}]
	});
	$("#show_value").dialog({
		autoOpen : false,
		draggable : false,
		resizable : false,
		modal : true,
	    closeOnEscape:true,
		open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
		width : $(window).width()/2,
		height : $(window).height()/3
	});
	//加入研究者队列下拉框展示
	$("#div_Patitent").dialog({
		autoOpen : false,
		width : $(window).width()/2,
		buttons : [
		{
			id : "nextStep",
			text : "确 认",
			click : function() 
			{
				//alert("ok");
				resultsDocument.addPatitent();
			}
		}, 
		{
			text : "取 消",
			click : function() 
			{
				//$(this).dialog("close");
				//resultsDocument.closeBg();
				closePatitent();
			}
		}]
	});
	//数据对比
	$("#div_compare").dialog({
		autoOpen : false,
		width : $(window).width()/8,
		height : $(window).height()/4*3
	});
	//绑定数据对比删除事件
	function bind_remove_compare()
	{
		var iList = document.getElementById("compare_Vkey").getElementsByTagName("i");
		var divList = document.getElementById("compare_Vkey").getElementsByTagName("div");	
		for (var i = 0,size = iList.length; i < size; i++)
		{
			iList[i].onclick = function(num)
			{
				return function()
				{
					//alert(divList[num].innerText);
					delete  resultsDocument.compareLists[divList[num].innerText];
					resultsDocument.showCompare();
				}
			}(i);
		}
	}
	
	
	function closeDiesae(){
		$("#div_diseaseDBSave").dialog("close");
		resultsDocument.closeBg();
		resultsDocument.cancleDiseaseSave();
	}
	function closeStatistics(){
		$("#div_diseaseDBSave_Statistics").dialog("close");
		resultsDocument.closeBg();
		resultsDocument.cancleDiseaseSave();
	}
	function closeDiesaeLocal(){
		$("#div_diseaseDBSave_local").dialog("close");
		resultsDocument.closeBg();
		resultsDocument.cancleDiseaseSave();
	}
	function closeDiesaeLocalNew(){
		$("#div_diseaseDBSave_local_div").dialog("close");
		resultsDocument.closeBg();
		resultsDocument.cancleDiseaseSave();
	}
	function closePatitent(){
		//resultsDocument.cancleDiseaseSave();
		$("#div_Patitent").dialog("close");
		resultsDocument.closeBg();
	}
	$("#AdvancedSearchDialog").dialog({
		draggable : false,
		resizable : false,
		modal : true,
	    closeOnEscape:true,
		open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
		autoOpen : false,
		width : $(window).width()*3/4,
		height : $(window).height()-60,
		buttons : [
		{
			text : "关 闭",
			click : function() 
			{
				$(this).dialog("close");
			}
		}, 
		{
			text : "删除条件",
			click : function() 
			{
				removeSearchValue();
				setSearchValue(1);	
			}
		}, 
		{
			text : "删除所有条件",
			click : function() 
			{
				removeAllSearchValue();
				setSearchValue(1);	
			}
		} , 
		{
			text : "确定并检索",
			click : function() 
			{
				setSearchValue(2);
			}
		}]
	});	
	$("#show_fancytree_div_id").dialog({
		draggable : true,
		resizable : false,
		modal : false,
	    closeOnEscape : false,
		open:function(event,ui){$(".ui-dialog-titlebar-close").hide();},
		autoOpen : false,
		//width : $(window).width()*3/4,
		height : $(window).height()-60,
		buttons : [
		           {
		        	    text : "分类",
			   			click : function() 
			   			{
			   				showFancytree();
			   			}
		           },{
		        	    text : "查询",
			   			click : function() 
			   			{
			   				showFancytreeSearch();
			   			}
		           },{
		        	    text : "重载",
			   			click : function() 
			   			{
			   				reloadOK = true;
			   				$("#fancytree_id").fancytree("destroy");
			   				$("#show_fancytree_search_list_select_id").html("");
			   				showFancytree();
			   			}
		           },
					{
						
						text : "关 闭",
						click : function() 
						{
							$(this).dialog("close");
							$("#show_omaha_div_id",prebodyDocument.document).show(1000);
							$("#show_omaha_div_id",resultsDocument.document).show(1000);
						}
					}]
	});	
	$("#div_bingli").dialog({
		autoOpen : false,
		width : $(window).width()*3/4,
		height : $(window).height()-60
	});
	$("#div_classifyName").dialog(
	{
		autoOpen: false,
		width: $(window).width()/2,
		buttons: [
			{
				text: "确 认",
				click: function() 
				{
					if ($("#classifyName").val().trim() == "")
					{
						parent.$.dialog("病种库新增分类不能为空!请重新输入!", false, 1500);
					}
					else
					{
						$(this).dialog("close");
						addDiseaseClassifyMain();
					}
				}
			},
			{
				text: "关 闭",
				click: function() 
				{
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});
	$("#div_classifyChildName").dialog(
	{
		autoOpen: false,
		width: $(window).width()/2,
		buttons: [
			{
				text: "确 认",
				click: function() 
				{
					if ($("#classifyChildName").val().replace(/(^\s*)|(\s*$)/g,"") == "")
					{
						parent.$.dialog("病种库新增分类不能为空!请重新输入!", false, 1500);
					}
					else
					{
						$(this).dialog("close");
						addDiseaseClassifyChildMain();
					}
				}
			},
			{
				text: "关 闭",
				click: function() 
				{
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});
	$("#div_uploadFile").dialog(
	{
		autoOpen: false,
		width: $(window).width()/2,
		buttons: [
			{
				text: "确 认",
				click: function() 
				{
					upLoadFileAll();
				}
			},
			{
				text: "关 闭",
				click: function() 
				{
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});
	$("#div_dieaseNameList").dialog(
	{
		autoOpen: false,
		width: $(window).width()/2,
		buttons: [
			{
				text: "确 认",
				click: function() 
				{
					if ($("#dieaseNameList_input").val().replace(/(^\s*)|(\s*$)/g,"") == "")
					{
						parent.$.dialog("病种库新增名称不能为空!请重新输入!", false, 1500);
					}
					else
					{
					    var fileName=$("#dieaseNameList_input").val();
					    var newFileName = fileName.replace(/[^\u4e00-\u9fa5\w]/g,'');
						if(fileName.length != newFileName.length){
						   $("#dieaseNameList_input").css({ color: "red" });
						   return;
						}
						$(this).dialog("close");
						addDiseaseName();
					}
				}
			},
			{
				text: "关 闭",
				click: function() 
				{
					$(this).dialog("close");
					resultsDocument.closeBg();
				}
			}
		]
	});
</script>
<script>
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
  return dateTime.getFullYear()+"-"+month+"-"+dateSt+" "+
			   hourSt+":"+minutesSt+":"+
			   secondsSt+" "+milliseconds;
}
	//进入主页前验证用户身份,如果未登录,则跳转至单点登录界面
	window.onload =function()
	{
	    var onloadStartTime = getFormatTime();
	    $("#onloadStartTime",footerDocument.document).html(onloadStartTime);
		var cookieList = document.cookie.split(";");
		var tokenValue,departmentValue;
		for (var i = 0,size = cookieList.length; i < size; i++)
		{
			if(cookieList[i].split("=").length>1){
			    var cookieName = cookieList[i].split("=")[0].trim();
				var cookieValue = cookieList[i].split("=")[1].trim();
				if (cookieName == "SSOToken")
				{
					if(cookieValue.replace(" ","").length > 0){
						tokenValue = cookieValue;
						$("#token").val(tokenValue);
					//	tokenFindFlag = true;
					}
				}
				else if (cookieName == "SSOUserDepartment")
				{
					departmentValue = cookieValue;
					$("#hideUserDepartment").val(cookieValue);
				}
				else if (cookieName == "SSOUserIdentity")
				{
					$("#hideUserIdentity").val(cookieValue);
					prebodyDocument.getHistoryExpression();
					if(cookieValue.toLowerCase() == "sys"  || cookieValue.toLowerCase() == "kpi" || cookieValue.toLowerCase() == "ss"){
					     $("#diseaseCountDetail",headerDocument.document).show();
					     $("#wordCloud",headerDocument.document).show();
				    }else{
				     $("#diseaseCountDetail",headerDocument.document).hide();
				    }
				}
				else if (cookieName == "SSOUserName")
				{   var ssouserName = decodeURI(cookieValue);
				    $("#ssoUserName",headerDocument.document).html("欢迎您," +ssouserName );
					$("#SSOUserName").val(ssouserName);
					getCountForApproval();
				}
			}
		}
		//setName();
		var onloadStartTime = getFormatTime();
		$("#onloadEndTime",footerDocument.document).html(onloadStartTime);
		document.getElementById("results").style.display = "none";
		document.getElementById("footer").style.display = "block";
		var windows_height = $(window).height();
		$("#results").css({
			height:windows_height-60
			});
		$("#prebody").css({
			height:windows_height-60
			});
	}
	/**
	 *
	 * 查询当前用户要审批数
	 *
	 */
	function  getCountForApproval() {
		$.ajax({
	   	     type: "post",
	   	     url: "/hssp/rest/getCountForApproval",
	   	     dataType: "json",
	   	     success: function(data){
	   	    	 var htmlSt = "<a tabIndex=\"5\" onclick=\"showPrivilegeApproval()\">权限信息</a>";
	   	    	if(data.total != 0){
	   	    		htmlSt = "<a tabIndex=\"5\" onclick=\"showPrivilegeApproval()\">权限信息<span style = \"color: red;\">("+data.total+")</span></a>";
	   	    		$("#showPrivilegeApproval",headerDocument.document).html(htmlSt);
	   	    	 }else{
	   	    		$("#showPrivilegeApproval",headerDocument.document).html(htmlSt);
	   	    	 }
	   	    	setTimeout("getCountForApproval()", 1000*60*60);
	   	     },error:function(e){
	   	    		parent.location.reload();    	    
				}
		 });
	}
	/**
	 *
	 * 重置当前用户要审批数
	 *
	 */
	function  resetCountForApproval() {
		$("#showPrivilegeApproval",headerDocument.document).html("<a tabIndex=\"5\" onclick=\"showPrivilegeApproval()\">权限信息</a>");
	}
	//文件上传div展示
	function uploadShowDiv()
	{
		$("#div_uploadFile").dialog("open");
	}
	//保存病种库
	function saveDiseaseDB() 
	{
		$("#div_diseaseDBSave").dialog("open");
	}
	//保存病种库
	function saveDiseaseDBStatistics() 
	{
		$("#div_diseaseDBSave_Statistics").dialog("open");
	}
	function saveDiseaseDBStatisticsNew() {
		//saveDiseaseVkey();
		var sizeMaps =  Object.keys(resultsDocument.diseaseFiltermap).length ;
		if(!resultsDocument.checkAll && sizeMaps == 0){
			parent.$.dialog("未选中要保存的数据!", false, 1500);
			return;
		}
		getDiseaseListAll();
		saveDiseaseDBStatistics();
	}
	//保存数据导出
	function saveDiseaseDBLocal() 
	{
		$("#div_diseaseDBSave_local").dialog("open");
	}
	//保存数据导出
	function saveDiseaseDBLocalNew() 
	{
		$("#div_diseaseDBSave_local_div").dialog("open");
	}
	//保存病种库
	function showVauleDiv() 
	{
		$("#show_value").dialog("open");
	}
	//保存病种库
	function savePatitent() 
	{
		getProjects();
		$("#div_Patitent").dialog("open");
	}
	
	function getProjects()
	{
		var htmls = "";
		$.ajax(
		{
			type : "post",
			url : "/hssp/tail/getProjects",
			dataType : "json",
			success : function(data)
			{
				var result = eval(data.projects);
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
					var ClassifyChild = result[j].centerInfos;
				    var ClassifyChild_html ="";
				    if (styleShowOk) {
				       ClassifyChild_html ="<select  class=\"form-control\" id=\"select_Patient_Child_"+Classify_ID+"\">"
				       diseaseClassifyID=Classify_ID;
				       styleShowOk = false;
					}else{
						diseaseClassifyID=diseaseClassifyID+","+Classify_ID;
				       ClassifyChild_html ="<select  class=\"form-control\" id=\"select_Patient_Child_"+Classify_ID+"\"style=\"display:none\">"
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
				Classify_html = "<select  class=\"form-control\" id=\"select_Patient\"onchange=\"patientSelectClassify()\">"+Classify_html+"</select>";
				$("#select_Patitent_div",parent.document).html(Classify_html);
				//alert(ClassifyChild_htmls);
				$("#select_Patitent_Child_div",parent.document).html(ClassifyChild_htmls);
			}
		});
	}
	
	/*
	function changeSize() {
		var windows_height = $(window).height();
		document.getElementById("results").style.height = windows_height;
	}*/
	function addDiseaseClassifyChildMain() 
	{		
		footerDocument.addDiseaseClassifyChild();
	}
	function addDiseaseClassifyMain() 
	{		
		footerDocument.addDiseaseClassify();
	}
	//新增病种库名称
	function addDiseaseName() 
	{	
		//dieaseNames_html_static  $("#dieaseNameList_input").val();
		//获取大类id
		var Description= $("#description",parent.document).val();
		var USERID= $("#hideUserIdentity").val();
		var SubCategoryName = $("#dieaseNameList_input").val();
		parent.$.dialog("正在新建导出数据名称......", false, 1500);
		$.ajax({
			type : "post",
			url : "/hssp/tail/saveDiseaseNameLocal",
			data : "jurisdiction=" + 0 +
			       "&USERID=" + USERID +
			       "&DISEASECLASSIFYID=" + 0 +
			       "&DISEASECLASSIFYCHIIDID=" + 0 +
			       "&SubCategoryName=" + SubCategoryName +
			       "&Description=" + Description,
			success : function(result) {
				if (result == "existed") {
					parent.$.dialog("新建导出数据名称已存在!请重新操作!", false, 1500);
				} else if (result == "success") {
					parent.$.dialog("新建导出数据名称成功!", false, 1500);
					getDiseaseDBNameListLocal();
				} else {
					parent.$.dialog("新建导出数据名称失败!", false, 1500);
				}
				//selectClassifyChild("select_diseaseClassify_Child_"+diseaseClassifyNum, diseaseClassifyChild_name)
				$("#dieaseNameList_input").val("");
				$("#dieaseNameList_input").css({ color: "black" });
			}
		});
	}
	function addDiseaseName_old() 
	{	
		//dieaseNames_html_static  $("#dieaseNameList_input").val();
		//获取大类id
		var diseaseClassifyID = $("#select_diseaseClassify_local", parent.document)
				.val();
		var diseaseClassifyChild= $("#select_diseaseClassify_Child_"+diseaseClassifyID,parent.document).val();
		var jurisdiction= $("#jurisdiction",parent.document).val();
		var Description= $("#description",parent.document).val();
		var USERID= $("#hideUserIdentity").val();
		var SubCategoryName = $("#dieaseNameList_input").val();
		parent.$.dialog("正在新增病种库名称......", false, 1500);
		$.ajax({
			type : "post",
			url : "/hssp/tail/saveDiseaseNameLocal",
			data : "jurisdiction=" + jurisdiction +
			       "&USERID=" + USERID +
			       "&DISEASECLASSIFYID=" + diseaseClassifyID +
			       "&DISEASECLASSIFYCHIIDID=" + diseaseClassifyChild +
			       "&SubCategoryName=" + SubCategoryName +
			       "&Description=" + Description,
			success : function(result) {
				if (result == "existed") {
					parent.$.dialog("新增病种库子类已存在!请重新操作!", false, 1500);
				} else if (result == "success") {
					parent.$.dialog("新增病种库子类成功!", false, 1500);
					getDiseaseDBNameList();
				} else {
					parent.$.dialog("新增病种库子类失败!", false, 1500);
				}
				//selectClassifyChild("select_diseaseClassify_Child_"+diseaseClassifyNum, diseaseClassifyChild_name)
				$("#dieaseNameList_input").val("");
			}
		});
	}
	//病种库导出勾选字段
	function getDiseaseDBFieldInfo()
	{
	
	   var USERID= $("#hideUserIdentity").val();
		$.ajax({
			type : "post",
			url : "/hssp/tail/getDiseaseDBFieldInfoSolr",
			data :"USERID=" + USERID ,
			dataType : "json",
			success : function(result)
			{
				treeNodes = result.diseaseDBFieldInfo;
				diseaseTableAndFieldDisplay("diseaseTableAndFieldTree");
				var windows_height = $(window).height()*0.8;
				$("#diseaseTableAndFieldTree").css({
					height:windows_height,
					});
				setTimeout("$(\"#div_diseaseTableAndField\").dialog(\"open\")",100);
			}
		});
	}
		//病种库导出勾选字段
	function getDiseaseDBFieldInfoLocal(diseaseId)
	{
	
	   var USERID= $("#hideUserIdentity").val();
	   $("#hideDiseaseDBName").val(diseaseId);
		$.ajax({
			type : "post",
			url : "/hssp/tail/getDiseaseDBFieldInfoSolr",
			data :"USERID=" + USERID ,
			dataType : "json",
			success : function(result)
			{
				treeNodes = result.diseaseDBFieldInfo;
				diseaseTableAndFieldDisplay("diseaseTableAndFieldTree");
				var windows_height = $(window).height()*0.8;
				$("#diseaseTableAndFieldTree").css({
					height:windows_height,
					});
				setTimeout("$(\"#div_diseaseTableAndField\").dialog(\"open\")",100);
			}
		});
	}
	function getStatisticsDBFieldInfo()
	{
	   var USERID= $("#hideUserIdentity").val();
		$.ajax({
			type : "post",
			url : "/hssp/tail/getDiseaseDBFieldInfoStatistics",
			data :"USERID=" + USERID ,
			dataType : "json",
			success : function(result)
			{
				treeNodes = result.diseaseDBFieldInfo;
				diseaseTableAndFieldDisplay("StatisticsTableAndFieldTree");
				var windows_height = $(window).height()*0.8;
				$("#StatisticsTableAndFieldTree").css({
					height:windows_height,
					});
				setTimeout("$(\"#div_StatisticsTableAndField\").dialog(\"open\")",100);
			}
		});
	}
	function getDiseaseDBSelectFieldInfo()
	{
		var USERID= $("#hideUserIdentity").val();
		$.ajax({
			type : "post",
			url : "/hssp/tail/getDiseaseDBFieldInfoSolr",
			data :"USERID=" + USERID ,
			dataType : "json",
			success : function(result)
			{
				treeNodes = result.diseaseDBFieldInfo;
				diseaseTableAndFieldDisplay("div_diseaseShow_Tree");
				var windows_height = $(window).height()*0.8;
				$("#div_diseaseShow_Tree").css({
					height:windows_height,
					});
				setTimeout("$(\"#div_diseaseShow\").dialog(\"open\")",100);
			}
		});
	}
	//数据导出大分类信息展示
	function diseaseDBClassify() 
	{		
		var htmls = "";
		var idList = "";
		$.ajax(
		{
			type : "post",
			url : "/hssp/tail/getDiseaseClassify",
			dataType : "json",
			success : function(data)
			{
				var result = eval(data.classify);
				for (var i = 0,size = result.length; i < size; i++)
				{
					htmls = htmls + "<li class=\"col-lg-3 col-sm-6 col-xs-12\"><div title=\"" + result[i].name + "\">";
					htmls = htmls + result[i].name;
					htmls = htmls + "</div></li>";
					
					if (idList == "")
					{
						idList = idList + result[i].id;
					}
					else
					{
						idList = idList + ";" + result[i].id;
					}
				}
				$("#diseaseDBClassify").html(htmls);
				document.getElementById("hideDiseaseDBClassifyIdList").value = idList;
				bind_click_diseaseDBClassify();
				$("#div_diseaseDBClassify").dialog("open");
			}
		});
	}
	
	//搜索数据导出大分类信息展示
	function getDiseaseClassifyLocal() 
	{		
		var htmls = "";
		var idList = "";
		$.ajax(
		{
			type : "post",
			url : "/hssp/tail/getDiseaseClassifyLocal",
			dataType : "json",
			success : function(data)
			{
				var result = eval(data.classify);
				for (var i = 0,size = result.length; i < size; i++)
				{
					htmls = htmls + "<li class=\"col-lg-3 col-sm-6 col-xs-12\"><div title=\"" + result[i].name + "\">";
					htmls = htmls + result[i].name;
					htmls = htmls + "</div></li>";
					
					if (idList == "")
					{
						idList = idList + result[i].id;
					}
					else
					{
						idList = idList + ";" + result[i].id;
					}
				}
				$("#diseaseDBClassify").html(htmls);
				document.getElementById("hideDiseaseDBClassifyIdList").value = idList;
				bind_click_diseaseDBClassify();
				$("#div_diseaseDBClassify").dialog("open");
			}
		});
	}
	//数据导出小分类信息展示
	function diseaseDBClassifySub() 
	{		
		var htmls = "";
		var idList = "";
		$.ajax(
		{
			type : "post",
			url : "/hssp/tail/getDiseaseClassifyLocal",
			dataType : "json",
			success : function(data)
			{
				var result = eval(data.classify);
				for (var i = 0,size = result.length; i < size; i++)
				{
					if (document.getElementById("hideDiseaseDBClassifyId").value == result[i].id)
					{
						var classifyChildList = result[i].diseaseClassifyChilds;
						if (Number(classifyChildList.length) == 0)
						{
							parent.$.dialog("该大分类下不存在小分类!", false , 1500);
						}
						else
						{
								for (var j = 0,size_child = classifyChildList.length; j < size_child; j++)
								{
									htmls = htmls + "<li class=\"col-lg-3 col-sm-6 col-xs-12\"><div title=\"" + classifyChildList[j].name + "\">";
									htmls = htmls + classifyChildList[j].name;
									htmls = htmls + "</div></li>";
									
									if (idList == "")
									{
										idList = idList + classifyChildList[j].id;
									}
									else
									{
										idList = idList + ";" + classifyChildList[j].id;
									}
								}
								$("#diseaseDBClassifySub").html(htmls);
								document.getElementById("hideDiseaseDBClassifySubIdList").value = idList;
								bind_click_diseaseDBClassifySub();
								$("#div_diseaseDBClassify").dialog("close");
								$("#div_diseaseDBClassifySub").dialog("open");
						}
						break;
					}
				}
			}
		});
	}
	//绑定数据导出大分类信息点击事件
	function bind_click_diseaseDBClassify()
	{
		var divList = document.getElementById("diseaseDBClassify").getElementsByTagName("div");	
		for (var i = 0,size = divList.length; i < size; i++)
		{
			divList[i].onclick = function(num)
			{
				return function()
				{
					document.getElementById("hideDiseaseDBClassifyId").value = $("#hideDiseaseDBClassifyIdList").val().split(";")[num];
					document.getElementById("hideDiseaseDBClassifyName").value = divList[num].innerText;
					diseaseDBClassifySub();
				}
			}(i);
		}
	}
	//绑定数据导出小分类信息点击事件
	function bind_click_diseaseDBClassifySub()
	{
		var divList = document.getElementById("diseaseDBClassifySub").getElementsByTagName("div");	
		for (var i = 0,size = divList.length; i < size; i++)
		{
			divList[i].onclick = function(num)
			{
				return function()
				{
					document.getElementById("hideDiseaseDBClassifySubId").value = $("#hideDiseaseDBClassifySubIdList").val().split(";")[num];
					document.getElementById("hideDiseaseDBClassifySubName").value = divList[num].innerText;
					headerDocument.getDiseaseDBDetail(1);
				}
			}(i);
		}
	}
	//具体某分类下的病种库展示
	function buildDiseaseDBDetailTable(forPage,result)
	{
		refreshExportMessageNext = false;
		if(!refreshExportMessageOk){
		  return;
		}
		if (Number(result.totalCount) == 0)
		{
		    resultsDocument.closeBg();
			parent.$.dialog("无导出信息!", false, 1500);
		}
		else
		{
				var diseaseDBNameList = eval(result.diseaseDBName);
				var dieaseIDList = eval(result.dieaseIDList);
				var statusList = eval(result.status);
				var fileStatusList = eval(result.fileStatus);
				var expressionList = eval(result.expression);
				var descriptionList = eval(result.description);
				var createTimeList = eval(result.createTime);
				var recordTotalCountList = eval(result.recordTotalCount);
				var maleCountList = eval(result.maleCount);
				var femaleCountList = eval(result.femaleCount);
				var bodyHeight = document.getElementById("prebody").scrollHeight || document.getElementById("results").scrollHeight;
				$("#div_diseaseDBDetailTotalInfo_local").prev().height(bodyHeight - 130);
				$("#diseaseDBDetailTotalInfo_local").html(result.totalCount);
				$("#diseaseDBTotalPage_local").html(Math.ceil(result.totalCount/15));
				$("#diseaseDBCurrentPage_local").html(forPage);
				var columns = [];
				var data = [];
				columns.push({
			         title: "序号",
			          field: 'showNum',
			         formatter:"runningFormatter_diseaseDBLocal",
			         width: '20px',
			         valign:'middle',
			         align:'center'
			     });
			    //var titleList = new Array("病种库名称","创建时间","保存状态","导出状态","病例总数","男女比例","检索式","备注","详情","下载");
			    var titleList = new Array("数据导出名称","总记录数","保存状态","导出状态","详情","导出数据","本地下载","审批拷贝","查看审批信息","创建时间","检索式","备注","导出ID");
			    for (var i = 0,size = titleList.length; i < size; i++)
			    {
					if(i==0){
						columns.push({
					        title: titleList[i],
			                field: i,
			                width: '100px',
			                valign:'middle',
			                align:'center'
					     });
					}else if(i==1){
					    columns.push({
					        title: titleList[i],
			                field: i,
			                width: '100px',
			                valign:'middle',
			                align:'center'
					     });
					}else if(i==2){
					    columns.push({
					        title: titleList[i],
			                field: i,
			                width: '100px',
			                valign:'middle',
			                align:'center'
					     });
					}else if(i==3){
					    columns.push({
					        title: titleList[i],
			                field: i,
			                width: '100px',
			                valign:'middle',
			                align:'center'
					     });
					}else if(i==4){
					    columns.push({
					        title: titleList[i],
			                field: i,
			                width: '100px',
			                valign:'middle',
			                align:'center'
					     });
					}else if(i==5){
					    columns.push({
					        title: titleList[i],
			                field: i,
			                width: '100px',
			                valign:'middle',
			                align:'center'
					     });
					}else if(i==6){
					    columns.push({
					        title: titleList[i],
			                field: i,
			                width: '100px',
			                valign:'middle',
			                align:'center'
					     });
					}else if(i==7){
					    columns.push({
					        title: titleList[i],
			                field: i,
			                width: '100px',
			                valign:'middle',
			                align:'center'
					     });
					}else if(i==8){
					    columns.push({
					        title: titleList[i],
			                field: i,
			                width: '200px',
			                valign:'middle',
			                align:'center'
					     });
					}else if(i==9){
					    columns.push({
					        title: titleList[i],
			                field: i,
			                width: '200px',
			                valign:'middle',
			                align:'center'
					     });
					}else if(i==10){
					    columns.push({
					        title: titleList[i],
			                field: i,
			                width: '200px',
			                valign:'middle',
			                align:'center'
					     });
					}else{
						columns.push({
						        title: titleList[i],
				                field: i,
				                width: '100px',
				                valign:'middle',
				                align:'center'
						     });
					}
			    }
				for (var i = 0,size = diseaseDBNameList.length; i < size; i++)
			    {
			    	row = {};
			    	/*row[0] = "<div name=\"div_diseaseDBName\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + diseaseDBNameList[i] + "\">" + diseaseDBNameList[i] + "</div>";//病种库名称
			    	row[1] = "<div name=\"div_createTime\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + createTimeList[i] + "\">" + createTimeList[i] + "</div>";//创建时间
			    	row[2] = "<div name=\"div_saveStatus\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + getChineseStatus(statusList[i]) + "\">" + getChineseStatus(statusList[i]) + "</div>";//保存状态
			    	row[3] = "<div name=\"div_exportStatus\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + getChineseStatus(fileStatusList[i]) + "\">" + getChineseStatus(fileStatusList[i]) + "</div>";//导出状态
			    	row[4] = "<div name=\"div_totalCount\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + recordTotalCountList[i] + "\">" + recordTotalCountList[i] + "</div>";//病例总数
			    	row[5] = "<div name=\"div_sexScale\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + maleCountList[i] + ":" + femaleCountList [i] + "\">" + maleCountList[i] + ":" + femaleCountList [i] + "</div>";//男女比例
			    	row[6] = "<div name=\"div_expression\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + expressionList[i] + "\">" + expressionList[i] + "</div>";//检索式
			    	row[7] = "<div name=\"div_decription\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + descriptionList[i] + "\">" + descriptionList[i] + "</div>";//备注
			    	row[8] = "<div style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\"><a name=\"a_viewDiseaseDB\" style=\"color:blue\">查看病种库</a></div>";//详细信息
			    	row[9] = "<div style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\"><a name=\"a_download\" style=\"color:blue\">下载至搜索</a></div>";//下载
			    	*/
			    	row[0] = "<div name=\"div_diseaseDBName\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + diseaseDBNameList[i] + "\">" + diseaseDBNameList[i] + "</div>";//病种库名称
			    	row[1] = "<div name=\"div_totalCount\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + recordTotalCountList[i] + "\">" + recordTotalCountList[i] + "</div>";//病例总数
			    	row[2] = "<div name=\"div_saveStatus\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + getChineseStatus(statusList[i]) + "\">" + getChineseStatus(statusList[i]) + "</div>";//保存状态
			    	if(fileStatusList[i]== 1 || fileStatusList[i] == 2){
			    	row[3] = "<div name=\"div_exportStatus\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + getChineseStatus(fileStatusList[i]) + "\"><img src=\"${pageContext.request.contextPath}/resources/images/waiting.gif\"></div>";//导出状态
			    	  refreshExportMessageNext = true;
			    	}else{
			    	row[3] = "<div name=\"div_exportStatus\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + getChineseStatus(fileStatusList[i]) + "\">" + getChineseStatus(fileStatusList[i]) + "</div>";//导出状态
			    	}
			    	//row[4] = "<div style=\"width:80px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\"><a name=\"a_viewDiseaseDB\" style=\"color:blue\">查看</a></div>";//详细信息
			    	row[4] = "<div style=\"width:80px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\"><a onclick=\"viewDiseaseDBLocal('"+dieaseIDList[i]+"')\" style=\"color:blue\">查看</a></div>";//详细信息
			    	row[5] = "<div style=\"width:80px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\"><a onclick=\"getDiseaseDBFieldInfoLocal('"+dieaseIDList[i]+"')\" style=\"color:blue\">导出数据</a></div>";//详细信息
			    	row[6] = "<div style=\"width:80px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\"><a onclick=\"getdownLoad('"+fileStatusList[i]+"','"+diseaseDBNameList[i]+"','"+dieaseIDList[i]+"')\" style=\"color:blue\">下载文件</a></div>";//详细信息
			    	row[7] = "<div style=\"width:80px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\"><a onclick=\"upLoadFile('"+fileStatusList[i]+"','"+diseaseDBNameList[i]+"','"+dieaseIDList[i]+"')\" style=\"color:blue\">上传文件</a></div>";//详细信息
			    	row[8] = "<div style=\"width:80px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\"><a onclick=\"headerDocument.getUploadDetail('1')\" style=\"color:blue\">获取审批文件</a></div>";//详细信息
			    	//row[6] = "<div style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\"><a name=\"a_download\" style=\"color:blue\">下载至本地</a></div>";//下载
			    	row[9] = "<div name=\"div_createTime\" style=\"width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + createTimeList[i] + "\">" + createTimeList[i] + "</div>";//创建时间
			    	//row[4] = "<div name=\"div_sexScale\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + maleCountList[i] + ":" + femaleCountList [i] + "\">" + maleCountList[i] + ":" + femaleCountList [i] + "</div>";//男女比例
			    	row[10] = "<div name=\"div_expression\" style=\"width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + expressionList[i] + "\">" + expressionList[i] + "</div>";//检索式
			    	row[11] = "<div name=\"div_decription\" style=\"width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + descriptionList[i] + "\">" + descriptionList[i] + "</div>";//备注
			    	row[12] = "<div name=\"div_diseaseID\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + dieaseIDList[i] + "\">" + dieaseIDList[i] + "</div>";//病种库名称
			    	data.push(row);
			    }
			    //var windows_height = $(window).height()/5*4;
			    var windows_height = document.documentElement.clientHeight/3*2;
				$("#diseaseDBDetail_local").bootstrapTable('destroy').bootstrapTable({
					columns: columns,//table列头
		        	data: data,//table数据
		        	height:windows_height
		    	});
		    	$("i[class='glyphicon glyphicon-th icon-th']").html("<strong><font size=\"1\">隐藏当前展示字段</font></strong>");
		    	//$("#div_diseaseDBClassifySub").dialog("close");
		    	$("#div_diseaseDBDetail_local").dialog("open");
		    	//$("#diseaseDBDetail_local").bootstrapTable("hideColumn",11);
		    	$("#diseaseDBDetail_local").bootstrapTable("hideColumn",7);
		    	$("#diseaseDBDetail_local").bootstrapTable("hideColumn",8);
		    	//refreshDiseaseDBDetailLocal(forPage);
		    	//setViewDiseaseDB();
				//bind_click_downloadFile();
		}
	}
	//具体某分类下的病种库展示
	function buildUploadDetail(forPage,result)
	{
		if (Number(result.totalCount) == 0)
		{
			parent.$.dialog("无导出信息!", false, 1500);
		}
		else
		{
				var uploadFileNames = eval(result.uploadFileNames);
				var recordTotalCountList = eval(result.recordTotalCount);
				var fileStatusList = eval(result.fileStatusList);
				var createTimeList = eval(result.createTimeList);
				var uploadFileIDList = eval(result.uploadFileIDList);
				var bodyHeight = document.getElementById("prebody").scrollHeight || document.getElementById("results").scrollHeight;
				$("#div_uploadDetail_Message").prev().height(bodyHeight - 130);
				$("#diseaseDBDetailTotalInfo_uploadDetail").html(result.totalCount);
				$("#diseaseDBTotalPage_uploadDetail").html(Math.ceil(result.totalCount/15));
				$("#diseaseDBCurrentPage_uploadDetail").html(forPage);
				var columns = [];
				var data = [];
				columns.push({
			         title: "序号",
			          field: 'showNum',
			         formatter:"runningFormatter_diseaseDBLocal",
			         width: '20px',
			         valign:'middle',
			         align:'center'
			     });
			    //var titleList = new Array("病种库名称","创建时间","保存状态","导出状态","病例总数","男女比例","检索式","备注","详情","下载");
			    var titleList = new Array("上传文件名","总记录数","上传状态","上传日期","下载审批文件","上传文件ID");
			    for (var i = 0,size = titleList.length; i < size; i++)
			    {
						columns.push({
						        title: titleList[i],
				                field: i,
				                valign:'middle',
				                align:'center'
						     });
			    }
				for (var i = 0,size = uploadFileNames.length; i < size; i++)
			    {
			    	row = {};
			    	row[0] = "<div name=\"div_uploadFileName\" style=\"width:500px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + uploadFileNames[i] + "\">" + uploadFileNames[i] + "</div>";//文件上传名称
			    	row[1] = "<div name=\"div_totalCount\"  title=\"" + recordTotalCountList[i] + "\">" + recordTotalCountList[i] + "</div>";//上传文件总记录数
			    	row[2] = "<div name=\"div_fileStatusList\" title=\"" + getChineseStatus(fileStatusList[i]) + "\">" + getChineseStatus(fileStatusList[i]) + "</div>";//文件上传状态
			    	row[3] = "<div name=\"div_totalCount\"  title=\"" + createTimeList[i] + "\">" + createTimeList[i] + "</div>";//上传文件时间
			    	row[4] = "<div><a onclick=\"downLoadPDf('"+uploadFileIDList[i]+"','"+fileStatusList[i]+"')\" style=\"color:blue\">下载审批文件同意书</a></div>";//详细信息
			    	row[5] = "<div name=\"div_uploadFileIDList\"  title=\"" + uploadFileIDList[i] + "\">" + uploadFileIDList[i] + "</div>";//上传文件总记录数
			    	data.push(row);
			    }
			    var windows_height = $(window).height();
				$("#table_uploadDetail").bootstrapTable('destroy').bootstrapTable({
					columns: columns,//table列头
		        	data: data,//table数据
		        	height:bodyHeight/5*4
		    	});
		    	$("i[class='glyphicon glyphicon-th icon-th']").html("<strong><font size=\"1\">隐藏当前展示字段</font></strong>");
		    	//$("#div_diseaseDBClassifySub").dialog("close");
		    	$("#div_uploadDetail").dialog("open");
		    	$("#table_uploadDetail").bootstrapTable("hideColumn",5);
		}
	}
	
	//刷新某分类指定页数的病种库
	function downLoadPDf(id,status)
	{
	    if(status == 5){
	        $("#uploadMessageID_id").val(id);
		    var useid = $("#hideUserIdentity").val();
			$("#userID_id").val(useid);
			$("#SmartFile").submit();
	    }else{
	       parent.$.dialog("请等待文件上传完成后下载审批文件!", false, 1000);
	    }
	}
	//刷新某分类指定页数的病种库
	function refreshDiseaseDBDetail(forPage)
	{
		$.ajax(
		{
			type : "post",
			url : "/hssp/head/getDiseaseDBLocal",
			data : "pageCount=15&pageNum=" + forPage + "&USERID=" + $("#hideUserIdentity", parent.document).val(),
			dataType : "json",
			success : function(result) 
			{
				buildDiseaseDBDetailTable(forPage,result);
			}
		});
	}
	
	
	var refreshExportMessageOk = false;
	var refreshExportMessageNext = false;
	function resetRefreshExportMessageOk(ok){
	   refreshExportMessageOk = ok;
	}
	function refreshDiseaseDBDetailLocal(forPage){
	  if(refreshExportMessageOk && refreshExportMessageNext){
	    //headerDocument.getDiseaseDBDetailLocal(forPage);
	    setTimeout("refreshDiseaseDBDetail("+forPage+");", 5000);
	  }else{
	     return;
	  }
	}
	function showInner(num,message){
	    //$("#div_diseaseDBDetail_inner_message_id_1").html(message);
	    //num:1,2 表示数据导入中
	    if(num == 2){
	       num = 1;
	    }
	  	if(num < 8 ){
		  	$("#div_diseaseDBDetail_inner").dialog("open");
	  	    num=num+2;
            for(var i=1; i <= num;i++){
	            if(i==2 || i== 4){
	              continue;
	            }
              $("#step"+i+"Li").addClass("blue").removeClass("gray");
              $("#step"+i+"Img").attr("src","${pageContext.request.contextPath}/resources/images/blue_blue.png");
            }
            num++;
            for(var i=num; i <= 7;i++){
	            if(i==2 || i== 4){
	              continue;
	            }
              $("#step"+i+"Li").addClass("gray").removeClass("blue");
              $("#step"+i+"Img").attr("src","${pageContext.request.contextPath}/resources/images/gray_gray.png");
            }
            if(num==2 || num== 4){
             num++;
	        }
             $("#step"+(num)+"Img").attr("src","${pageContext.request.contextPath}/resources/images/blue_gray.png");
	  	}
	}
	function closeInner(){  
	  	$("#div_diseaseDBDetail_inner").dialog("close");
	}
	function showProgress(num,message){  
	  	if(num < 6 ){
		  	$("#div_diseaseDBDetail_progres").dialog("open");
	  	    num++;
		  	var progressNum = parseInt(100/6*num);
		  	var progressText = num+"/6";
		  	$("#div_diseaseDBDetail_progres_id").css("width",progressNum+"%");
		  	//$("#div_diseaseDBDetail_progres").attr("title","任务执行进度:  "+message);
		  	$("#div_diseaseDBDetail_progres_message_id_1").html(message);
		  	$("#div_diseaseDBDetail_progres_message_id_2").html("已完成"+progressText+"(当前/总共)");
	  	}
	}
	function closeProgress(){  
	  	$("#div_diseaseDBDetail_progres").dialog("close");
	}
	var pageCountNum = 0;
	//病种库状态信息展示
	function showDiseaseStatu(forPage)
	{
		$.ajax(
			{
				type : "post",
				url : "/hssp/head/showDiseaseStatu",
				data : "pageNum=" + forPage +"&Identity="+$("#hideUserIdentity").val(),
				dataType : "json",
				success : function(result) 
				{
		    pageCountNum = Number(eval(result.pageCountNum));
			var idList = eval(result.idList);
			if(idList.length > 0){
				var statuList = eval(result.statuList);
				var statuIdList = eval(result.statuIdList);
				var expressionList = eval(result.expression);
				var diseaseIDList = eval(result.diseaseIDList);
				var diseaseNameList = eval(result.diseaseNameList);
				var deptNameList = eval(result.deptNameList);
				var createTimeList = eval(result.createTime);
				var bodyHeight = document.getElementById("prebody").scrollHeight || document.getElementById("results").scrollHeight;
				$("#div_diseaseDBDetailTotalInfo").prev().height(bodyHeight - 130);
				$("#diseaseDBDetailTotalInfo").html(result.totalCount);
				$("#disease_pageCount").html(pageCountNum);
				$("#diseaseDBTotalPage").html(Math.ceil(result.totalCount/pageCountNum));
				$("#diseaseDBCurrentPage").html(forPage);
				var columns = [];
				var data = [];
				columns.push({
			         title: "序号",
			         formatter:"runningFormatter_diseaseDB",
			         width: '50px',
			         valign:'middle',
			         align:'center'
			     });
			     columns.push({
				        title: "任务ID",
		                field: 0,
		                width: '50px',
		                valign:'middle',
		                align:'center'
				     });
			     columns.push({
			        title: "病种库名称",
	                field: 1,
	                width: '150px',
	                valign:'middle',
	                align:'center'
			     });
			     columns.push({
			        title: "科室名称",
	                field: 2,
	                width: '100px',
	                valign:'middle',
	                align:'center'
			     });
			     columns.push({
			        title: "检索表达式",
	                field: 3,
	                width: '150px',
	                valign:'middle',
	                align:'center'
			     });
			     columns.push({
			        title: "创建时间",
	                field: 4,
	                width: '200px',
	                valign:'middle',
	                align:'center'
			     });
			     columns.push({
			        title: "状态",
	                field: 5,
	                width: '150px',
	                valign:'middle',
	                align:'center'
			     });
			     columns.push({
			        title: "导出数据",
	                field: 6,
	                width: '150px',
	                valign:'middle',
	                align:'center'
			     });
			   /* var titleList = new Array("任务ID","病种库名称","科室名称","检索表达式","创建时间","状态");
			    for (var i = 0,size = titleList.length; i < size; i++)
			    {
					columns.push({
				        title: titleList[i],
		                field: i,
		                width: '100px',
		                valign:'middle',
		                align:'center'
				     });
			    }*/
			    
				for (var i = 0,size = idList.length; i < size; i++)
			    {
			    	row = {};
			    	row[0] = "<div name=\"div_diseaseDBID\" style=\"width:50px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	+ idList[i] + "\">" + idList[i] + "</div>";//任务ID
			    	row[1] = "<div name=\"div_diseaseDBNameCN\" style=\"width:150px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	+ diseaseNameList[i] + "\">" + diseaseNameList[i] + "</div>";//病种库名称
			    	row[2] = "<div name=\"div_diseaseDBDeptName\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	+ deptNameList[i] + "\">" + deptNameList[i] + "</div>";//科室名称
			    	row[3] = "<div name=\"div_diseaseDBExpression\" style=\"width:150px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	+ expressionList[i] + "\">" + expressionList[i] + "</div>";//检索表达式
			    	row[4] = "<div name=\"div_diseaseDBCreateTime\" style=\"width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	+ createTimeList[i] + "\">" + createTimeList[i] + "</div>";//任务创建时间
			    	row[5] = "<div name=\"div_diseaseDBStatus\" style=\"width:150px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	//+ statuList[i] + "\" onmouseover=\"showProgress("+statuIdList[i]+",'"+statuList[i]+"')\" onmouseout=\"closeProgress()\">" + statuList[i] + "</div>";//任务状态
			    	//+ statuList[i] + "\" onclick=\"showInner("+statuIdList[i]+",'"+statuList[i]+"')\">" + statuList[i] + "</div>";//任务状态
			    	+ statuList[i] + "\" onmouseover=\"showInner("+statuIdList[i]+",'"+statuList[i]+"')\" onmouseout=\"closeInner()\">" + statuList[i] + "</div>";//任务状态
			    	//+ statuList[i] + "\" onclick=\"showInner("+statuIdList[i]+",'"+statuList[i]+"')\">" + statuList[i] + "</div>";//任务状态
			    	//+ statuList[i] + "\" >" + statuList[i] + "</div>";//任务状态
			    	//row[5] = "<div name=\"div_diseaseDBName\" style=\"width:150px;height:30px;text-align:center;line-height:30px;\"" + " onmouseover=\"showProgress(1)\">" + statuList[i] + "</div>";//任务状态
			    	if(statuIdList[i] == 5){
			    	    row[6] = "<div disabled = \"disabled\" name=\"div_diseaseDBExport\" style=\"height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	    + diseaseNameList[i] + "\">" + "<a href=\"http://192.168.0.169:6007/home/index?DiseaseID="+diseaseIDList[i]+"\">"+"<button class=\"btn_export\" style=\"height: 30px;\">数据导出</button></a></div>";//导出数据
/* 			    	    row[6] = "<div disabled = \"disabled\" name=\"div_diseaseDBExport\" style=\"height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	    + diseaseNameList[i] + "\">" + "<a href=\"http://192.168.6.26:6007/FormManager/Display?id=509&DiseaseID="+diseaseIDList[i]+"\">"+"<button class=\"btn_export\" style=\"height: 30px;\">数据导出</button></a></div>";//导出数据 */
			    	 }else{
			    	    row[6] = "<div name=\"div_diseaseDBExport\" style=\"height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	    + diseaseNameList[i] + "\">" + "数据导出</div>";//导出数据
			    	}
			    	data.push(row);
			    }
			    var windows_height = $(window).height()/3*2;
			   
				$("#disease_show").bootstrapTable('destroy').bootstrapTable({
					columns: columns,//table列头
		        	data: data,//table数据
		        	height:windows_height
		    	});
		    	// var windows_height = $("#div_diseaseDBDetail").height();
		    	$("#div_diseaseDBDetail").dialog("open");
				$(".fixed-table-container").css("height",(16*47+50)+"px");
				$("#disease_show").bootstrapTable("hideColumn",2);
			}else{
			   resultsDocument.closeBg();
			   parent.$.dialog("当前无病种库任务!", false, 1000);
			}
			resultsDocument.closeBg();
		}
		});
			
		
	}
	var pageStatisticsCountNum = 0;
	//病种库状态信息展示
	function showStatisticsStatu(forPage)
	{
		$.ajax(
			{
				type : "post",
				url : "/hssp/head/showStatisticsStatu",
				data : "pageNum=" + forPage +"&Identity="+$("#hideUserIdentity").val(),
				dataType : "json",
				success : function(result) 
				{
		    pageStatisticsCountNum = Number(eval(result.pageCountNum));
			var idList = eval(result.idList);
			if(idList.length > 0){
				var statuList = eval(result.statuList);
				var statuIdList = eval(result.statuIdList);
				var expressionList = eval(result.expression);
				var diseaseIDList = eval(result.diseaseIDList);
				var diseaseNameList = eval(result.diseaseNameList);
				var statisticsNameList = eval(result.statisticsNameList);
				var deptNameList = eval(result.deptNameList);
				var dataTypeList = eval(result.dataTypeList);
				var createTimeList = eval(result.createTime);
				var bodyHeight = document.getElementById("prebody").scrollHeight || document.getElementById("results").scrollHeight;
				$("#div_StatisticsDBDetailTotalInfo").prev().height(bodyHeight - 130);
				$("#StatisticsDBDetailTotalInfo").html(result.totalCount);
				$("#Statistics_pageCount").html(pageStatisticsCountNum);
				$("#StatisticsDBTotalPage").html(Math.ceil(result.totalCount/pageStatisticsCountNum));
				$("#StatisticsDBCurrentPage").html(forPage);
				var columns = [];
				var data = [];
				columns.push({
			         title: "序号",
			         formatter:"runningFormatter_StatisticsDB",
			         width: '50px',
			         valign:'middle',
			         align:'center'
			     });
			     columns.push({
				        title: "任务ID",
		                field: 0,
		                width: '50px',
		                valign:'middle',
		                align:'center'
				     });
			     columns.push({
			        title: "数据维度",
	                field: 1,
	                width: '100px',
	                valign:'middle',
	                align:'center'
			     });
			     columns.push({
			        title: "病种库名称",
	                field: 2,
	                width: '150px',
	                valign:'middle',
	                align:'center'
			     });
			      columns.push({
			        title: "数据集名称",
	                field: 3,
	                width: '150px',
	                valign:'middle',
	                align:'center'
			     });
			     columns.push({
			        title: "检索表达式",
	                field: 4,
	                width: '150px',
	                valign:'middle',
	                align:'center'
			     });
			     columns.push({
			        title: "创建时间",
	                field: 5,
	                width: '200px',
	                valign:'middle',
	                align:'center'
			     });
			     columns.push({
			        title: "状态",
	                field: 6,
	                width: '150px',
	                valign:'middle',
	                align:'center'
			     });
			     
			     /*columns.push({
			        title: "导出数据",
	                field: 6,
	                width: '150px',
	                valign:'middle',
	                align:'center'
			     });
			    var titleList = new Array("任务ID","病种库名称","科室名称","检索表达式","创建时间","状态");
			    for (var i = 0,size = titleList.length; i < size; i++)
			    {
					columns.push({
				        title: titleList[i],
		                field: i,
		                width: '100px',
		                valign:'middle',
		                align:'center'
				     });
			    }*/
			    columns.push({
			        title: "统计",
	                field: 7,
	                width: '150px',
	                valign:'middle',
	                align:'center'
			     });
				for (var i = 0,size = idList.length; i < size; i++)
			    {
			    	row = {};
			    	row[0] = "<div name=\"div_StatisticsDBID\" style=\"width:50px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	+ idList[i] + "\">" + idList[i] + "</div>";//任务ID
			    	/* row[1] = "<div name=\"div_StatisticsDBDeptName\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	+ deptNameList[i] + "\">" + deptNameList[i] + "</div>";//科室名称 */
			    	 row[1] = "<div name=\"div_dataTypeListName\" style=\"width:100px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	+ dataTypeList[i] + "\">" + dataTypeList[i] + "</div>";//数据维度 
			    	row[2] = "<div name=\"div_StatisticsDBNameCN\" style=\"width:150px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	+ diseaseNameList[i] + "\">" + diseaseNameList[i] + "</div>";//病种库名称
			    	row[3] = "<div name=\"div_StatisticsDBNameCN\" style=\"width:150px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	+ statisticsNameList[i] + "\">" + statisticsNameList[i] + "</div>";//数据集名称
			    	row[4] = "<div name=\"div_StatisticsDBExpression\" style=\"width:150px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	+ expressionList[i] + "\">" + expressionList[i] + "</div>";//检索表达式
			    	row[5] = "<div name=\"div_StatisticsDBCreateTime\" style=\"width:200px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	+ createTimeList[i] + "\">" + createTimeList[i] + "</div>";//任务创建时间
			    	row[6] = "<div name=\"div_StatisticsDBStatus\" style=\"width:150px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	//+ statuList[i] + "\" onmouseover=\"showProgress("+statuIdList[i]+",'"+statuList[i]+"')\" onmouseout=\"closeProgress()\">" + statuList[i] + "</div>";//任务状态
			    	//+ statuList[i] + "\" onclick=\"showInner("+statuIdList[i]+",'"+statuList[i]+"')\">" + statuList[i] + "</div>";//任务状态
			    	+ statuList[i] + "\" onmouseover=\"showInner("+statuIdList[i]+",'"+statuList[i]+"')\" onmouseout=\"closeInner()\">" + statuList[i] + "</div>";//任务状态
			    	//+ statuList[i] + "\" onclick=\"showInner("+statuIdList[i]+",'"+statuList[i]+"')\">" + statuList[i] + "</div>";//任务状态
			    	//+ statuList[i] + "\" >" + statuList[i] + "</div>";//任务状态
			    	//row[5] = "<div name=\"div_diseaseDBName\" style=\"width:150px;height:30px;text-align:center;line-height:30px;\"" + " onmouseover=\"showProgress(1)\">" + statuList[i] + "</div>";//任务状态
			    	if(statuIdList[i] == 5){
			    	    row[7] = "<div disabled = \"disabled\" name=\"div_diseaseDBExport\" style=\"height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	    + diseaseNameList[i] + "\">" + "<a href=\"http://192.168.0.169:6011?DiseaseID="+diseaseIDList[i]+"\">"+"<button class=\"btn_export\" style=\"height: 30px;\">查看统计</button></a></div>";//导出数据
/* 			    	    row[6] = "<div disabled = \"disabled\" name=\"div_diseaseDBExport\" style=\"height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	    + diseaseNameList[i] + "\">" + "<a href=\"http://192.168.6.26:6007/FormManager/Display?id=509&DiseaseID="+diseaseIDList[i]+"\">"+"<button class=\"btn_export\" style=\"height: 30px;\">数据导出</button></a></div>";//导出数据 */
			    	 }else{
			    	    row[7] = "<div name=\"div_diseaseDBExport\" style=\"height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" 
			    	    + diseaseNameList[i] + "\">" + "查看统计</div>";//导出数据
			    	}
			    	data.push(row);
			    }
			   
		    	$("#div_StatisticsDBDetail").dialog("open");
		    	var div_height = $("#div_StatisticsDBDetail").height()-32;
				$("#Statistics_show").bootstrapTable('destroy').bootstrapTable({
					columns: columns,//table列头
		        	data: data,//table数据
		        	height:div_height
		    	});
		    	// var windows_height = $("#div_diseaseDBDetail").height();
				//$(".fixed-table-container").css("height",(16*47+50)+"px");
				//$("#Statistics_show").bootstrapTable("hideColumn",1);
			}else{
			   resultsDocument.closeBg();
			   parent.$.dialog("当前无统计库任务!", false, 1000);
			}
			resultsDocument.closeBg();
		},error:function(e){
	    		parent.location.reload();    	    
		}
		});
			
		
	}
	
	function getShowDiseaseStatu()
	{
		if (($("#diseaseDBCurrentPage").text() - 1) < 1)
		{
			parent.$.dialog("已经是第一页", false, 1000);
		}
		else
		{
			showDiseaseStatu($("#diseaseDBCurrentPage").text() - 1);
		}
	}
	//获取某分类下一页的病种库
	function getNextShowDiseaseStatu()
	{
		if (($("#diseaseDBCurrentPage").text() - 0 + 1) > $("#diseaseDBTotalPage",parent.document).text())
		{
			parent.$.dialog("已经是最后一页", false, 1000);
		}
		else
		{
			showDiseaseStatu($("#diseaseDBCurrentPage").text() - 0 + 1);
		}
	}
	//跳转至某分类指定页数的病种库
	function jumpToShowDiseaseStatu()
	{
		var forPage = $("#diseaseDB_pageNum").val();
		if (forPage != "" && Number(forPage)!=0 && Number(forPage) <= Number($("#diseaseDBTotalPage",parent.document).text()))
		{
			$("#diseaseDB_pageNum",parent.document).val("");
			showDiseaseStatu(Number(forPage));
		}
		else
		{
			parent.$.dialog("跳转页数输入有误!请重新输入!", false, 1000);
			$("#diseaseDB_pageNum").val("");
		}
	}
	function getShowStatisticsStatu()
	{
		if (($("#StatisticsDBCurrentPage").text() - 1) < 1)
		{
			parent.$.dialog("已经是第一页", false, 1000);
		}
		else
		{
			showStatisticsStatu($("#StatisticsDBCurrentPage").text() - 1);
		}
	}
	//获取某分类下一页的病种库
	function getNextShowStatisticsStatu()
	{
		if (($("#StatisticsDBCurrentPage").text() - 0 + 1) > $("#StatisticsDBTotalPage",parent.document).text())
		{
			parent.$.dialog("已经是最后一页", false, 1000);
		}
		else
		{
			showStatisticsStatu($("#StatisticsDBCurrentPage").text() - 0 + 1);
		}
	}
	//跳转至某分类指定页数的病种库
	function jumpToShowStatisticsStatu()
	{
		var forPage = $("#StatisticsDB_pageNum").val();
		if (forPage != "" && Number(forPage)!=0 && Number(forPage) <= Number($("#StatisticsDBTotalPage",parent.document).text()))
		{
			$("#StatisticsDB_pageNum",parent.document).val("");
			showStatisticsStatu(Number(forPage));
		}
		else
		{
			parent.$.dialog("跳转页数输入有误!请重新输入!", false, 1000);
			$("#StatisticsDB_pageNum").val("");
		}
	}
	
	
	
	//获取某分类上一页的病种库详情
	function viewDiseaseDBPreviousPage()
	{
		if (($("#diseaseDBRecordCurrentPage").text() - 1) < 1)
		{
			parent.$.dialog("已经是第一页", false, 1000);
		}
		else
		{
			viewDiseaseDB($("#diseaseDBRecordCurrentPage").text() - 1);
		}
	}
	//获取某分类下一页的病种库详情
	var nextOK = true;
	function viewDiseaseDBNextPage()
	{
		 nextOK = false;
		if (($("#diseaseDBRecordCurrentPage").text() - 0 + 1) > $("#diseaseDBRecordTotalPage").text())
		{
			parent.$.dialog("已经是最后一页", false, 1000);
		}
		else
		{
			viewDiseaseDB($("#diseaseDBRecordCurrentPage").text() - 0 + 1);
		}
	}
	//跳转至指定页数的病种库详情
	function jumpToViewDiseaseDB()
	{   
		var forPage = $("#diseaseDBRecord_pageNum").val();
		if (forPage != "" && Number(forPage) <= Number($("#diseaseDBRecordTotalPage").text()))
		{
			nextOK = false;
			$("#diseaseDBRecord_pageNum").val("");
			viewDiseaseDB(forPage);
		}
		else
		{
			parent.$.dialog("跳转页数输入有误!请重新输入!", false, 1000);
			$("#diseaseDBRecord_pageNum").val("");
		}
	}
	//数据导出详情展示
	var windows_height = 0;
	var windows_heightOK = true;
	//查看导出数据
	function viewDiseaseDBLocal(diseasId)
	{
	  $("#hideDiseaseDBName").val("");
	  $("#hideDiseaseDBName").val(diseasId);
	  viewDiseaseDB(1);
	}
	function viewDiseaseDB(forPage)
	{
		document.getElementById("AjaxLoading").style.display="block";
		$.ajax({
			type : "post",
			url : "/hssp/tail/viewDiseaseSelectAllSolr",
			data : "&USERID=" + $("#hideUserIdentity").val() + "&DISEASECLASSIFYID=" + $("#hideDiseaseDBClassifyId").val() + "&DISEASECLASSIFYCHIIDID=" + $("#hideDiseaseDBClassifySubId").val() + "&SubCategoryName=" + $("#hideDiseaseDBName").val() + "&pageNum=" + forPage + "&pageCount=10"+ "&department="+$("#hideUserDepartment").val()+"&tableAndField="+$("#tableAndField").val(),
			dataType : "json",
			success : function(result)
			{
				if (Number(result.totalCount) == 0)
				{
					parent.$.dialog("当前导出内容无数据!", false, 1500);
				}
				else
				{
				    	$("#diseaseDBRecordTotalInfo").html(result.totalCount);
						$("#diseaseDBRecordTotalPage").html(Math.ceil(result.totalCount/10));
						$("#diseaseDBRecordCurrentPage").html(forPage);
						var fieldNameList = eval(result.fieldName);
						var fieldTitleList = eval(result.fieldTitle);
						var columns = [];
						columns.push({
					         title: "序号",
					         formatter:"runningFormatter_diseaseDBRecord",
					         width: '20px',
					         valign:'middle',
					         align:'center'
					     });
						for (var i = 0,size = fieldNameList.length; i < size; i++)
					    {
					    	//if (("," + result.fieldDefault + ",").indexOf("," + fieldTitleList[i] + ",") > -1)
					    	//{
								columns.push({
							        title: fieldTitleList[i],
				                    field: fieldNameList[i],
				                    width: '75px',
				                    valign:'middle',
				                    align:'center'
							     });
					    	//}
					    	//else
					    	//{
								//columns.push({
							      //  title: fieldTitleList[i],
				                    //field: fieldNameList[i],
				                   // width: '75px',
				                  //  valign:'middle',
				                  //  align:'center',
				                  //  visible: false
							     //});
					    	//}
					    }
						if(forPage == 1 &&　nextOK){
						  windows_height = $(window).height()-250;
						  windows_heightOK = true;
						}else{
							if(windows_heightOK){
							  windows_height = windows_height+70
							  windows_heightOK =false;
						    }
						}
						//alert(windows_height);
						$("#table").bootstrapTable('destroy').bootstrapTable({
							columns: columns,//table列头
				        	data: result.fieldValue,//table数据
				        	height:windows_height
				    	});
				    	$("i[class='glyphicon glyphicon-th icon-th']").html("<strong><font size=\"1\">隐藏当前展示字段</font></strong>");
				    	$("#div_diseaseDBDetail").dialog("close");
				    	$("#div_diseaseDBView").dialog("open");				
				}
			}
		});
				    	document.getElementById("AjaxLoading").style.display="none";
	}
	//绑定病种库点击事件
	function setViewDiseaseDB()
	{
		var diseaseDBNameList = $("div[name='div_diseaseID']");
		var saveStatusList = $("div[name='div_saveStatus']");
		var viewList = $("a[name='a_viewDiseaseDB']");
		for (var i = 0,size = viewList.length; i < size; i++)
		{
			viewList[i].onclick = function(num)
			{
				return function()
				{
					if (saveStatusList[num].innerText == "已完成")
					{
						document.getElementById("AjaxLoading").style.display="block";
						document.getElementById("hideDiseaseDBName").value = diseaseDBNameList[num].innerText;
						viewDiseaseDB(1);
					}
					else
					{
						parent.$.dialog("病种库还未保存完成,请稍后再试!", false, 1500);
					}
				}
			}(i);
		}
	}
	//绑定病种库点击下载事件
	function bind_click_downloadFile()
	{
		var diseaseDBNameList = $("div[name='div_diseaseDBName']");
		var saveStatusList = $("div[name='div_saveStatus']");
		var exportStatusList = $("div[name='div_exportStatus']");
		var downloadList = $("a[name='a_download']");
		for (var i = 0,size = downloadList.length; i < size; i++)
		{
			downloadList[i].onclick = function(num)
			{
				return function()
				{					
					if (saveStatusList[num].innerText != "已完成")
					{
						parent.$.dialog("病种库还未保存完成,请稍后再试!", false, 1500);
					}
					else if (exportStatusList[num].innerText != "已完成")
					{
						parent.$.dialog("病种库还未导出完成,请稍后再试!", false, 1500);
					}
					else
					{
						var fileName = "" + $("#hideDiseaseDBClassifyName").val() + "_" + $("#hideDiseaseDBClassifySubName").val() + "_" + diseaseDBNameList[num].innerText + ".xls";
						headerDocument.downloadFile(fileName);
					}
				}
			}(i);
		}
	}
	//文件下载
	function getdownLoad(fileStatu,diseaseName,diseaseID){
	      if(fileStatu == 5){
	      var fileName = "" +diseaseName+"_"+diseaseID + ".xls";
		headerDocument.downloadFile(fileName,diseaseID);
	      }else if(fileStatu == 0){
	        parent.$.dialog("请先导出数据到文件后下载!", false, 1500);
	      }else{
	        parent.$.dialog("文件还未生成完成,请稍后再试!", false, 1500);
	      }
	}
	//文件下载
	function upLoadFile(fileStatu,diseaseName,diseaseID){
	      if(fileStatu == 5){
	          parent.$.dialog("文件上传开始!", false, 1500);
		      var fileName = "" +diseaseName+"_"+diseaseID + ".xls";
			   $.ajax({
					type : "post",
					url : "/hssp/body/uploadFileTOFftp",
					data : "fileName=" +fileName+"&diseaseID="+diseaseID+"&USERID=" + $("#hideUserIdentity").val(),
					dataType : "json",
					success : function(json)
					{
					var result = json;
					  if (result == "succeed"){
						  parent.$.dialog("文件上传成功!", false, 1500);
					  }else{
						  parent.$.dialog("文件上传失败!", false, 1500);
					  }
					}
				});
				parent.$.dialog("文件上传任务成功,可去文件上传信息中查看上传情况!", false, 1500);
				var forPage = $("#diseaseDBCurrentPage_local").text();
				headerDocument.getDiseaseDBDetailLocal(forPage);
	      }else if(fileStatu == 0){
	        parent.$.dialog("请先导出数据到文件后上传!", false, 1500);
	      }else{
	        parent.$.dialog("文件还未生成完成,请稍后再试!", false, 1500);
	      }
	}
	//文件上传
	function upLoadFileAll(){
		      var fileName = $("#div_uploadFile_id").val();
	      if(fileName != ""){
		      $("#form_uploadFile").ajaxSubmit({
			     type: "post",
			     url: "/hssp/body/uploadFile"+"?USERID="+$("#hideUserIdentity").val(),
			     dataType: "json",
			     success: function(data){
					  if (result == "succeed"){
						  parent.$.dialog("文件上传成功!", false, 1500);
					  }else{
						  parent.$.dialog("文件上传失败!", false, 1500);
					  }
				 }
			   });
	      }else{
	        parent.$.dialog("请选择要上传的文件!", false, 1500);
	      }
	}
	//病历内容详情展示
	function getShowView(DocId,analysisWay)
	{
		$.ajax({
				type : "post",
				url : "/hssp/body/showView",
				data : "DocId=" + DocId,
				dataType : "json",
				success : function(json)
				{
					var responseShowView = eval(json.responseShowView);
					if(responseShowView.responseStatu == "0000"){
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
					}else{
						document.getElementById("showView").innerHTML =responseShowView.responseDescription;
					}
					$("#div_bingli").dialog("open");
				}
			});
	}
	//******************************************************************表达式管理********************************************************************
	//表达式管理主入口
	function expressionManage() 
	{
		headerDocument.getExpression(1);
		$("#div_expressioinManage").dialog("open");
	}
	//绑定表达式删除事件
	function bind_remove_expression()
	{
		var iList = document.getElementById("expressioinManage").getElementsByTagName("i");
		var divList = document.getElementById("expressioinManage").getElementsByTagName("div");	
		var spanList = document.getElementById("expressioinManage").getElementsByTagName("span");	
		for (var i = 0,size = iList.length; i < size; i++)
		{
			iList[i].onclick = function(num)
			{
				return function()
				{
					$.ajax(
					{
						type : "post",
						url : "/hssp/head/removeExpression",
						data : "USERID=" + $("#hideUserIdentity",parent.document).val()+"&id=" + spanList[num*2].innerText+"&expression=" + divList[num].innerText,
						success : function()
						{
							headerDocument.getExpression($("#expressionCurrentPage").text());
						}
					});
				}
			}(i);
		}
	}
	//绑定表达式点击事件
	function bind_click_expression()
	{
		var divList = document.getElementById("expressioinManage").getElementsByTagName("div");	
		for (var i = 0,size = divList.length; i < size; i++)
		{
			divList[i].onclick = function(num)
			{
				return function()
				{
					$("#div_expressioinManage").dialog("close");
					document.getElementById("hideinput").value = divList[num].innerText;
					document.getElementById("prebody").style.display = "none";
					document.getElementById("results").style.display = "block";
					resultsDocument.getExpressionFromIndex();
					resultsDocument.closeBg();
				}
			}(i);
		}
	}
	//****************************************************************高级检索********************************************************************
	//高级检索主入口
	function searchFieldMatch(txtObj) 
	{
		if (txtObj.value.length > 0) {
			headerDocument.getSearchField(txtObj.value);
		}else{
			headerDocument.getSearchField("");
		}
	}
	//高级检索主入口
	function advancedSearch() 
	{
		$("#AdvancedSearchDialog").dialog("open");
	}
	//高级检索界面检索值判断
	function addSearchSign(value,valueDes){
		var htmls = $("#select_advancedSearchExpression").html();
		htmls = htmls + "<optgroup label=\"" +valueDes+ "\">"+"<option value=\"\">"+value+"</option>";
		htmls = htmls + "</optgroup>";
		$("#select_advancedSearchExpression").html(htmls);
		$("#searchValue").val("");
		setSearchValue(1);
	}
	//高级检索界面检索值判断
	function addSearchValue()
	{
		if ($("#searchValue").val().trim() == "")
		{
			parent.$.dialog("检索值不能为空!请重新输入!", false, 1000);
		}
		else
		{
			//select_advancedSearchExpression
			//var advacedSearchExpression = document.getElementById("select_advancedSearchExpression");
			var htmls = $("#select_advancedSearchExpression").html();
			var searchValueSt = $("#searchValue").val();
			searchValueSt = resultsDocument.htmlEscape(searchValueSt); 
			if (htmls.indexOf($("#select_logicRelation").val()) > 1000)
			{
				var regex = "<optgroup label=\"" + $("#select_logicRelation").val() + "\">.*</optgroup>";
				var pattern = new RegExp(regex);
				var oldHtml = pattern.exec(htmls);
				var newHtml = oldHtml.toString().replace(new RegExp("</optgroup>",'gm'), "<option value=\"" + $("#select_searchSet").val() + "." + $("#select_searchField").val() + "&&" +$("#select_searchOperator").val() + "&&" + searchValueSt + "\">"
						+ $("#select_searchSet").val() + "." + $("#select_searchField").val() + "&&" +$("#select_searchOperator").val() + "&&" + searchValueSt + "</option></optgroup>");
				htmls = htmls.replace(new RegExp(oldHtml.toString(),'gm'), newHtml);
			}
			else
			{
				htmls = htmls + "<optgroup label=\"" + $("#select_logicRelation").val() + "\">";
				htmls = htmls + "<option value=\"" + $("#select_searchSet").val() + "." + $("#select_searchField").val() + "&&" +$("#select_searchOperator").val() + "&&" + searchValueSt + "\">"
						+ $("#select_searchSet").val() + "." + $("#select_searchField").val() + "&&" +$("#select_searchOperator").val() + "&&" + searchValueSt;
				htmls = htmls + "</option></optgroup>";
			}
			$("#select_advancedSearchExpression").html(htmls);
			$("#searchValue").val("");
			setSearchValue(1);			
		}
	}
	//高级检索:删除条件
	function removeSearchValue()
	{
		if ($("#select_advancedSearchExpression").val() == null)
		{
			parent.$.dialog("您还没选中待删除条件!请选中后再操作！", false, 1000);
		}
		else
		{
		   var advacedSearchExpression = document.getElementById("select_advancedSearchExpression");
		   var oprgroupValue = advacedSearchExpression.options[advacedSearchExpression.selectedIndex].parentNode.remove();
		   //advacedSearchExpression.options[advacedSearchExpression.selectedIndex].remove();
		}
	}
	function removeSearchValue1()
	{
		if ($("#select_advancedSearchExpression").val() == null)
		{
			parent.$.dialog("您还没选中待删除条件!请选中后再操作！", false, 1000);
		}
		else
		{
			var advacedSearchExpression = document.getElementById("select_advancedSearchExpression");
			var htmls = $("#select_advancedSearchExpression").html();
			var optionValue = advacedSearchExpression.options[advacedSearchExpression.selectedIndex].text.replace(/&&/g,".*");
			var oprgroupValue = advacedSearchExpression.options[advacedSearchExpression.selectedIndex].parentNode.label;
			var replaceStr = "<optgroup label=\"" + oprgroupValue + "\"><option value=\"" + optionValue + "\">"	+ optionValue + "</option></optgroup>";
			htmls = htmls.replace(new RegExp(replaceStr.toString(),'gm'),"");
			replaceStr = "<option value=\"" + optionValue + "\">" + optionValue + "</option>";
			htmls = htmls.replace(new RegExp(replaceStr.toString(),'gm'),"");
			$("#select_advancedSearchExpression").html(htmls);
		}
	}
	//高级检索:删除所有条件
	function removeAllSearchValue()
	{
		$("#select_advancedSearchExpression").html("");
	}
	//高级检索:确定并检索
	function setSearchValue(ok)
	{
		
			var searchValueList="";
			var advacedSearchExpression = document.getElementById("select_advancedSearchExpression");
			
			if(ok == 1){
				$("#searchShow").text("");
				if (advacedSearchExpression.options.length == 0)
				{
					parent.$.dialog("您还没有新增检索条件!请新增检索条件后再操作!", false, 1000);
				}
				else
				{
					for (var i = 0,size = advacedSearchExpression.options.length; i < size; i++)
					{
						var oprgroupValue = advacedSearchExpression.options[i].parentNode.label;
						if (oprgroupValue == "AND")
						{
							oprgroupValue = " and ";
						}
						else if (oprgroupValue == "OR")
						{
							oprgroupValue = " or ";
						}else if (oprgroupValue == "NOT")
						{
							oprgroupValue = " not ";
						}
						var optionValue = advacedSearchExpression.options[i].text;
						var searchValue;
						if(oprgroupValue == "前括号" || oprgroupValue == "后括号"){
							searchValue = optionValue;
						}else{
							var strArr = optionValue.split("&&");
							if (strArr[1] == "大于或等于")
							{
								searchValue = "[" + strArr[0] + "] >= "+ strArr[2];
							}
							else if (strArr[1] == "小于或等于")
							{
								searchValue = "[" + strArr[0] + "] <= "+ strArr[2];
							}
							else if (strArr[1] == "大于")
							{
								searchValue = "[" + strArr[0] + "] > "+ strArr[2];
							}
							else if (strArr[1] == "小于")
							{
								searchValue = "[" + strArr[0] + "] < "+ strArr[2];
							}
							else if (strArr[1] == "等于")
							{
								searchValue ="\""+strArr[2]+"\""+"[" + strArr[0] + "] " ;
							}
							else if (strArr[1] == "绝对包含")
							{
								//searchValue = strArr[2] + "[" + strArr[0] + "]";
								searchValue = "*" +strArr[2] + "*" + "[" + strArr[0] + "]";
							}else if (strArr[1] == "同义包含")
							{
								//searchValue = strArr[2] + "[" + strArr[0] + "]";
								searchValue = strArr[2]  + "[" + strArr[0] + "]";
							}else if (strArr[1] == "=")
							{
								//searchValue = strArr[2] + "[" + strArr[0] + "]";
								searchValue = strArr[2]  + "[" + strArr[0] + "]";
							}
							/*else if (strArr[1] == "不包含")
							{
								searchValue = "not " + strArr[2] + "[" + strArr[0] + "]";					
							}*/
							else if (strArr[1] == "以XXX开头")
							{
								searchValue = strArr[2] + "*" + "[" + strArr[0] + "]";
							}
							else if (strArr[1] == "以XXX结尾")
							{
								searchValue = "*" + strArr[2] + "[" + strArr[0] + "]";					
							}
							else
							{
								parent.$.dialog("高级检索条件解析失败!", false, 1000);
							}
						}
						
						if (oprgroupValue == "前括号"){
								searchValueList = searchValueList+searchValue;
						}else if(oprgroupValue == "后括号"){
							searchValueList = searchValueList  + searchValue;
						}else{
							if(i-1<0){
								searchValueList = searchValue;
							}else{
								if(advacedSearchExpression.options[i-1].parentNode.label == "前括号"){
									if(i-1 == 0){
										searchValueList = searchValueList + searchValue;
									}else{
										searchValueList = searchValueList.substring(0,searchValueList.lastIndexOf("("));
										searchValueList = searchValueList+oprgroupValue+"("+searchValue;
									}
								}else{
									searchValueList = searchValueList + oprgroupValue + searchValue;
								}
							}
						}
					}
				$("#searchShow").text(searchValueList);
			   }
			}else{
				if (advacedSearchExpression.options.length == 0 || $("#searchShow").text().length == 0)
				{
					parent.$.dialog("您还没有新增检索条件!请新增检索条件后再操作!", false, 1000);
					return;
				}
				$("#AdvancedSearchDialog").dialog("close");
				document.getElementById("hideinput").value = $("#searchShow").text();
				document.getElementById("prebody").style.display = "none";
				document.getElementById("results").style.display = "block";
				$("#select_advancedSearchExpression").html("");
				$("#searchShow").text("");
				resultsDocument.getExpressionFromIndex();
		}
	}
	//bootstrap table控件之病种库详情序号回调函数
	function runningFormatter_diseaseDBRecord(value, row, index)
	{
	    return (Number($("#diseaseDBRecordCurrentPage").html()) - 1) * 10 + index + 1;
	}
	//bootstrap table控件之分类下病种库序号回调函数
	function runningFormatter_diseaseDB(value, row, index)
	{
	    //return (Number($("#diseaseDBCurrentPage",parent.document).html()) - 1) * 10 + index + 1;
	    var diseaseDB_index = (Number($("#diseaseDBCurrentPage",parent.document).html()) - 1) * pageCountNum + index + 1;
	    return "<div style=\"width:50px;height:30px;text-align:center;line-height:30px;\" title=\"" + diseaseDB_index + "\">" + diseaseDB_index + "</div>"
//	    return "<div style=\"width:50px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + diseaseDB_index + "\">" + diseaseDB_index + "</div>"
	}
	//bootstrap table控件之分类下病种库序号回调函数
	function runningFormatter_StatisticsDB(value, row, index)
	{
	    //return (Number($("#diseaseDBCurrentPage",parent.document).html()) - 1) * 10 + index + 1;
	    var StatisticsDB_index = (Number($("#StatisticsDBCurrentPage",parent.document).html()) - 1) * pageStatisticsCountNum + index + 1;
	    return "<div style=\"width:50px;height:30px;text-align:center;line-height:30px;\" title=\"" + StatisticsDB_index + "\">" + StatisticsDB_index + "</div>"
//	    return "<div style=\"width:50px;height:30px;text-align:center;line-height:30px;overflow:hidden;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + diseaseDB_index + "\">" + diseaseDB_index + "</div>"
	}
	function runningFormatter_diseaseDBLocal(value, row, index)
	{
	    //return (Number($("#diseaseDBCurrentPage",parent.document).html()) - 1) * 10 + index + 1;
	    var diseaseDB_index = (Number($("#diseaseDBCurrentPage_local",parent.document).html()) - 1) * 15 + index + 1;
	    return "<div  title=\"" + diseaseDB_index + "\">" + diseaseDB_index + "</div>"
	}
	//返回状态码对应的中文含义
	function getChineseStatus(statusCode)
	{
		if (statusCode == 0)
		{
			return "未开始";
		}
		else if (statusCode == 1)
		{
			return "进行中";
		}
		else if (statusCode == 2)
		{
			return "进行中";
		}
		else if (statusCode == 3)
		{
			//return "异常";
			return "进行中";
		}
		else if (statusCode == 5)
		{
			return "已完成";
		}
		else
		{
			//return "异常";
			return "进行中";
		}
	}
	//*******************************************************反馈*********************************************************
	//反馈管理主入口
	function feedbackManage() 
	{
		$("#div_feedbackManage").dialog("open");
	}
	//反馈功能打开按钮
	$("#div_feedbackManage").dialog({
		draggable : false,
		resizable : false,
		modal : true,
	    closeOnEscape:true,
		open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
		autoOpen : false,
		width : $(window).width()/2.3,
		buttons : [
		{
			id : "nextStep",
			text : "提 交",
			click : function() 
			{
				if ($("#feedbackContent").val().trim() == "")
				{
					parent.$.dialog("反馈内容不能为空!请重新输入!", false, 1500);
					return;
				}
				/* if ( $("#types").val().trim() == "已选择")
				{
					parent.$.dialog("反馈内容不能为空!请重新输入!", false, 1500);
					return;
				} */
				addFeedback();
			}
		}, 
		{
			text : "取 消",
			click : function() 
			{
				$(this).dialog("close");
			}
		}]
	});
	//反馈保存
	function addFeedback()
	  {  
		var requestUrl = "addFeedbackNoFile";
		var fileName = $("#annex").val();
		if(fileName != ""){
			requestUrl = "addFeedback";
		}
	    var types = $("#types").val();
	    var feedbackContent = $("#feedbackContent").val();
	    var contactPhone = $("#contactPhone").val();
	    parent.$.dialog("反馈提交中......", false, 1500);
	    $("#feedback").ajaxSubmit({
		     type: "post",
		     url: "/hssp/head/"+requestUrl+"?"+"types=" + types +
             "&feedbackContent=" + feedbackContent+
             "&contactPhone=" +contactPhone ,
		     dataType: "json",
		     success: function(data){
	   	    	 if(data.status == "succeed"){
		            $("#div_feedbackManage").dialog("close");
	   	    		$("#contactPhone").val("");
		            $("#feedbackContent").val("");
	   	    		parent.$.dialog("您的意见已成功反馈!", false, 1500);
	   	    	 }else{
	   	    		parent.$.dialog("反馈失败，请重新提交或联系系统管理员!", false, 1500);
	   	    	 }
		    	 },error:function(e){
		   	    		parent.location.reload();    	    
					}
		     });
	  }
	
	
	//*******************************************提示词*************************************
	//键盘按键触发获取搜索提示词
	function returnResat(e)
	{
		if(event.keyCode==13)return false;
	}
	function getSearchTip(e)
	{
		var e = e||window.event;
		if (e.keyCode)
		{
			if (e.keyCode != 13 && e.keyCode != 108)
			{
				getSearchTip();	
			}else{
				var div_searchTip = document.getElementById("searchKeyword");
				div_searchTip.style.display = "none";
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
		var start = document.getElementById("searchValue").selectionStart;
		var inputStr = $("#searchValue").val().substring(0,start);
		var lastInputStr = $("#searchValue").val().substring(start);
		var div_searchTip = document.getElementById("searchKeyword");
		div_searchTip.style.display = "none";
		$("#search_tip_list").html("");
		if(inputStr == ""){
			inputStr="*";
		}
		if(inputStr != ""){
			var expression;
			var field = "";
			var brackets = "";
			//提示词类型，0，只提示字段，1，只提示字段
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
					/*expression =inputStr;
					field = inputStr.substring(inputStr.lastIndexOf("[")+1,inputStr.length);
					type = 1;*/
					expression=expression.replaceAll("[","");
					expression=expression.replaceAll("]","");
				}
			}
			//参数首位为小括号
			if(expression.lastIndexOf("(")==0){
				expression=expression.substring(1);
				brackets="(";
			}
			//提示词检索方式0.均提示，1，只提示医疗(检索词加字段)，2，只提示运营,3只做检索词提示
			var searchType = 4;
			var tableField="";
			var limitQure="";
			if(searchType == 3){
				tableField=$("#table_field").val();
				if(tableField == ""){
					return;
				}
			}else if(searchType == 4){
				tableField=$("#table_field").val();
				if(tableField == ""){
					return;
				}else{
					var searchValueList="";
					var advacedSearchExpression = document.getElementById("select_advancedSearchExpression");
					//判断当前字段之前有记录
					if (advacedSearchExpression.options.length > 0)
					{
							var oprgroupValue = advacedSearchExpression.options[advacedSearchExpression.options.length-1].parentNode.label;
							if (oprgroupValue == "AND")
							{
								oprgroupValue = " and ";
							}
							else if (oprgroupValue == "OR")
							{
								oprgroupValue = " or ";
							}else if (oprgroupValue == "NOT")
							{
								oprgroupValue = " not ";
							}
							var optionValue = advacedSearchExpression.options[advacedSearchExpression.options.length-1].text;
							var searchValue="";
							var searchFieldValue="";
							if(oprgroupValue == "前括号" || oprgroupValue == "后括号"){
								
							}else{
								var strArr = optionValue.split("&&");
								if (strArr[1] == "绝对包含")
								{
									searchValue= "*" +strArr[2] + "*";
									searchFieldValue = "[" + strArr[0] + "]";
								}else if (strArr[1] == "同义包含")
								{
									searchValue=  strArr[2] ;
									searchFieldValue = "[" + strArr[0] + "]";
								}
								/*else if (strArr[1] == "不包含")
								{
									searchValue = "not " + strArr[2] + "[" + strArr[0] + "]";					
								}*/
								else if (strArr[1] == "以XXX开头")
								{
									searchValue= strArr[2] + "*";
									searchFieldValue = "[" + strArr[0] + "]";
								}
								else if (strArr[1] == "以XXX结尾")
								{
									searchValue= "*" + strArr[2];
									searchFieldValue = "[" + strArr[0] + "]";
								}
								
							}
							if(searchValue != "" && searchFieldValue != ""){
								limitQure=searchFieldValue+":"+searchValue;
							}
							
					}
					
				}
			}
			$.ajax({
				type : "post",
				url : "/hssp/body/getSearchTip",
				data : "expression=" + expression+"&type="+type+"&field="+field+"&searchType="+searchType+"&Identity="+$("#hideUserIdentity",parent.document).val()+"&tableField="+tableField+"&limitQure="+limitQure,
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
							for (var i = 0,size = searchTipList.length; i < size; i++)
							{
								htmls = htmls + "<option value=\"" + brackets + searchTipList[i] + lastInputStr + "\">";
								htmls = htmls + searchTipList[i];
								htmls = htmls + "</option>";
							}
							$("#search_tip_list").html(htmls);
							div_searchTip.style.width = document.getElementById("searchValue").scrollWidth + 'px';
							div_searchTip.style.top = document.getElementById("searchValue").scrollHeight + document.getElementById("searchValue").offsetTop + 'px';
							div_searchTip.style.display = "block";
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
				//resetSearch()
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
	//把用户选择的值赋给输入框中
	//判断输入“[”时是否有值
    var selectOk = true;
	function setSearchTipValue()
	{
		if(selectOk){
			//var inputStr = $("#searchValue").val();
			//焦点所在位置
			var start = document.getElementById("searchValue").selectionStart;
			var inputStr = $("#searchValue").val().substring(0,start);
			var lastKHZ =inputStr.lastIndexOf("[");
			var lastKHY =inputStr.lastIndexOf("]");
			var expression = "";
			if (inputStr.substring(inputStr.length - 1) == "[" || inputStr.substring(inputStr.length - 2) == "[*")
			{
				var search_tip_list_val=$("#search_tip_list").val();
				if(search_tip_list_val.lastIndexOf("(")==0){
					search_tip_list_val=search_tip_list_val.substring(1);
				}
				//expression = expression + inputStr.substring(0, inputStr.lastIndexOf("[")) + $("#search_tip_list").val();
				expression =expression + inputStr.substring(0, inputStr.lastIndexOf("[")) + search_tip_list_val;
			}
			else if(inputStr.substring(inputStr.length - 1) == "." || lastKHZ > lastKHY)
			{
				var search_tip_list_val=$("#search_tip_list").val();
				if(search_tip_list_val.substring(search_tip_list_val.length - 2)=="]]"){
					search_tip_list_val=search_tip_list_val.substring(0,search_tip_list_val.length - 1);
				}
				//expression = expression + inputStr.substring(0, inputStr.lastIndexOf("[")) + $("#search_tip_list").val();
				expression =expression + inputStr.substring(0, inputStr.lastIndexOf("[")) + search_tip_list_val;
			}
			else
			{
				var strArr = inputStr.split(new RegExp("([a|A][n|N][d|D]|[o|O][r|R]|[n|N][o|O][t|T]|>|<|=)",'gm'));
				for (var i = 0,size = strArr.length; i < size - 1; i++)
				{
					expression = expression + strArr[i];
				}
				var tip =  $("#search_tip_list").val();
				tip = tip.replace(/\(运营.*?\)/,"");
				expression = expression + tip;
			}
			$("#searchValue").val(expression);
			document.getElementById("searchKeyword").style.display = "none";
			$("#searchValue").focus();
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
  
  //*******************************************************使用量统计*********************************************************
	//使用量统计
	$("#div_diseaseCountDetail").dialog({
		draggable : false,
		resizable : false,
		modal : true,
	    closeOnEscape:true,
		open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
		autoOpen : false,
		width: $(window).width()/2,
		height: $(window).height()-60,
		buttons : [
		{
			text : "关闭",
			click : function() 
			{
				$(this).dialog("close");
				resultsDocument.closeBg();
				resetSearch()
			}
		}]
	});
	
	function runningFormatter_diseaseCount(value, row, index)
	{
	    var diseaseCount_index = index + 1;
	    return "<div  title=\"" + diseaseCount_index + "\">" + diseaseCount_index + "</div>"
	}
	//重置页面搜索
	function resetSearch(){
		//时间筛选重置
		$("#limit_time_text").html("时间不限");
    	$("#limit_time_start_Id").val("");
    	$("#limit_time_end_Id").val("");
	}
	//点击搜索事件
	function diseaseCountSearch(){
    	var createTime = $("#limit_time_start_Id").val();
    	var lastTime = $("#limit_time_end_Id").val();
		$.ajax(
		{
			type : "post",
			url : "/hssp/head/getDiseaseCountDetailMessage",
			data : "createTime="+createTime+"&lastTime=" + lastTime,
			dataType : "json",
			success : function(result) 
			{
				parent.buildDiseaseCountDetailTable(result);
			}
		});
	}
	//时间过滤操作事件
	function selectChange(value,limit_time_text){
		var myDate = new Date();
		var startYeartext = myDate.getFullYear(); 
		var startmonthtext = myDate.getMonth()+1;
		var startdaytext = myDate.getDate();
		var startTime = "";
		// 定义一个月份天数常量数组
		var DA = [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
		
	    if(value == 0){
	        $("#limit_time_text").html(limit_time_text);
	    	$("#limit_time_start_Id").val("1");
	    	$("#limit_time_end_Id").val("");
	    	diseaseCountSearch();
	    	return;
	    }else if(value == 7){
	        $("#limit_time_text").html(limit_time_text);
	        var date1= new Date();
	        var date2= new Date(date1);
	        date2.setDate(date1.getDate()-7);
	        var times = date2.getFullYear()+"-"+(date2.getMonth()+1)+"-"+date2.getDate();
	    	$("#limit_time_start_Id").val(times);
	    	$("#limit_time_end_Id").val("");
	    	diseaseCountSearch();
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
	    if(startmonthtext<1){
	    	startYeartext = startYeartext -1;
	    	startmonthtext = startmonthtext + 12
	    }
	    //判断是否是闰年
		if (isLoopYear(startYeartext)) DA[2] = 29;
	    var maxDay = DA[startmonthtext];
	    if(startdaytext>maxDay){
	    	startdaytext=maxDay
	    }
	    if(startmonthtext<10){
	    	startmonthtext = "0"+""+startmonthtext
		}
		if(startdaytext<10){
			startdaytext = "0"+""+startdaytext
		}
		startTime = startYeartext+"-"+startmonthtext+"-"+startdaytext;
		$("#limit_time_start_Id").val(startTime);
		$("#limit_time_end_Id").val("");
		diseaseCountSearch();
	}
	function time_select_2_submit(){
		var startTime = $("#select_Start").val();
		var endTime = $("#select_End").val();
		var startTimeOk = getdate("select_Start");
		var endTimeOk = getdate("select_End");
		if(startTimeOk){
			if(endTimeOk){
				if(resultsDocument.compareDate(startTime, endTime)){
		        	return;
		        }
				$("#limit_time_text").html(startTime+"～"+endTime);
				$("#limit_time_start_Id").val(startTime);
		        $("#limit_time_end_Id").val(endTime);
				diseaseCountSearch();
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
	//使用量统计展示
	function buildDiseaseCountDetailTable(result)
	{   
		refreshExportMessageNext = false;
		$("#div_diseaseCountDetail").dialog("open");
		var departmentList = eval(result.departmentList);
		var countsList = eval(result.countsList);
		var totalCount = eval(result.totalCount);
		var bodyHeight = document.getElementById("prebody").scrollHeight || document.getElementById("results").scrollHeight;
		$("#div_diseaseCountDetailTotalInfo").prev().height(bodyHeight - 130);
		$("#diseaseCountDetailTotalInfo").html(result.totalCount);
		$("#diseaseCountDetailTotalDepa").html(departmentList.length);
		var columns = [];
		var data = [];
		columns.push({
	         title: "序号",
	          field: 'showNum',
	         formatter:"runningFormatter_diseaseCount",
	         width: '20px',
	         valign:'middle',
	         align:'center'
	     });
	    var titleList = new Array("科室","总记录数");
	    for (var i = 0,size = titleList.length; i < size; i++)
	    {
			if(i==0){
				columns.push({
			        title: titleList[i],
	                field: i,
	                valign:'middle',
	                align:'center'
			     });
			}else if(i==1){
			    columns.push({
			        title: titleList[i],
	                field: i,
	                valign:'middle',
	                align:'center'
			     });
			}else{
				columns.push({
				        title: titleList[i],
		                field: i,
		                valign:'middle',
		                align:'center'
				     });
			}
	    }
		//for(var j = 0,size_j = 10; j < size_j; j++){
			for (var i = 0,size = departmentList.length; i < size; i++)
		    {
		    	row = {};
		    	row[0] = "<div name=\"div_diseaseCountName\"  title=\"" + departmentList[i] + "\">" + departmentList[i] + "</div>";//科室
		    	row[1] = "<div name=\"div_totalCount\" title=\"" + countsList[i] + "\">" + countsList[i] + "</div>";//查询总数
		    	data.push(row);
		    }
		//}
	    var windows_height = $(window).height()/5*3.5;
	    //var windows_height = document.documentElement.clientHeight/3*2;
		$("#diseaseCountDetail").bootstrapTable('destroy').bootstrapTable({
			columns: columns,//table列头
        	data: data,//table数据
        	height:windows_height
    	});
		
        if(departmentList.length==0){
        	$("#diseaseCountDetail tbody").html("<tr class='no-records-found'><td colspan='3'>无使用记录!</td></tr>");
        }
    	$("i[class='glyphicon glyphicon-th icon-th']").html("<strong><font size=\"1\">隐藏当前展示字段</font></strong>");
    	
    	//$("#diseaseCountDetail").bootstrapTable("hideColumn",11);
	}
	
	//*******************************************************热词统计*********************************************************
	//热词统计
	$("#div_wordCloudDetail").dialog({
		draggable : false,
		resizable : false,
		modal : true,
	    closeOnEscape:true,
		open:function(event,ui){$(".ui-dialog-titlebar-close").show();},
		autoOpen : false,
		width: $(window).width()-40,
		height: $(window).height()-60,
		buttons : [
		{
			text : "关闭",
			click : function() 
			{
				$(this).dialog("close");
				resultsDocument.closeBg();
				resetWordCloudSearch()
			}
		}]
	});
	
	//时间过滤操作事件
	function selectWordCloudChange(value,limit_time_text){
		var myDate = new Date();
		var startYeartext = myDate.getFullYear(); 
		var startmonthtext = myDate.getMonth()+1;
		var startdaytext = myDate.getDate();
		var startTime = "";
		// 定义一个月份天数常量数组
		var DA = [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
		
	    if(value == 0){
	        $("#limit_time_wordCloud_text").html(limit_time_text);
	    	$("#limit_time_start_Id").val("");
	    	$("#limit_time_end_Id").val("");
	    	wordCloudSearch();
	    	return;
	    }else if(value == 7){
	        $("#limit_time_wordCloud_text").html(limit_time_text);
	        var date1= new Date();
	        var date2= new Date(date1);
	        date2.setDate(date1.getDate()-7);
	        var times = date2.getFullYear()+"-"+(date2.getMonth()+1)+"-"+date2.getDate();
	    	$("#limit_time_start_Id").val(times);
	    	$("#limit_time_end_Id").val("");
	    	wordCloudSearch();
	    	return;
	    }else {
		    $("#limit_time_wordCloud_text").html(limit_time_text);
		    
	    	var year_subtraction = 0;
	    	var month_subtraction = 0 ;
	    	
	        year_subtraction = Math.floor(value/12);
	    	startYeartext = startYeartext - year_subtraction;
	        month_subtraction = value%12;
	    	startmonthtext = startmonthtext - month_subtraction;
	    }
	    if(startmonthtext<1){
	    	startYeartext = startYeartext -1;
	    	startmonthtext = startmonthtext + 12
	    }
	    //判断是否是闰年
		if (isLoopYear(startYeartext)) DA[2] = 29;
	    var maxDay = DA[startmonthtext];
	    if(startdaytext>maxDay){
	    	startdaytext=maxDay
	    }
	    if(startmonthtext<10){
	    	startmonthtext = "0"+""+startmonthtext
		}
		if(startdaytext<10){
			startdaytext = "0"+""+startdaytext
		}
		startTime = startYeartext+"-"+startmonthtext+"-"+startdaytext;
		$("#limit_time_start_Id").val(startTime);
		$("#limit_time_end_Id").val("");
		wordCloudSearch();
	}
	
	function top_select_submit(){
		var topOk=IsValidDateNumber('select_wordCloud_top');
		if(topOk){
			var tops = parseInt($("#select_wordCloud_top").val());
			if(tops == 0 || tops > 1000){
				$('#select_wordCloud_top').val("数字(0~1000)");
				 parent.$.dialog("请输入正确的数字(大于0小于1000)!", false, 1500);
				 return;
			}
			$("#limit_top_wordCloud_text").html("Top"+tops);
			$("#select_wordCloud_top_id").val(tops);
			wordCloudSearch();
		}else{
			$('#select_wordCloud_top').val("数字(0~1000)");
			 parent.$.dialog("请输入正确的数字(大于0小于1000)!", false, 1500);
		}
	}
	
	function time_select_submit(){
		var startTime = $("#select_wordCloud_Start").val();
		var endTime = $("#select_wordCloud_End").val();
		var startTimeOk = getdate("select_wordCloud_Start");
		var endTimeOk = getdate("select_wordCloud_End");
		if(startTimeOk){
			if(endTimeOk){
				$("#limit_time_wordCloud_text").html(startTime+"～"+endTime);
				$("#limit_time_start_Id").val(startTime);
		        $("#limit_time_end_Id").val(endTime);
		        wordCloudSearch();
		    }else{
		    	$("#select_wordCloud_End").val("yyyy-MM-dd");
		    }
		}else{
			$("#select_wordCloud_Start").val("yyyy-MM-dd");
		}
	}
	//是否数字
	function IsValidDateNumber(id){
		var id_val = $('#'+id).val();
	  var isok = /^[0-9]*$/;
		if(!isok.test(id_val)){
			$('#'+id).val("请输入数字");
			return false;
		}
		return true;
	}
	//科室
	function selectWordCloudDepartment(value,limit_department_text){
		$("#limit_department_wordCloud_text").html(limit_department_text);
		$("#select_wordCloud_department_Id").val(value);
		wordCloudSearch();
	}
	//TOP 值
	function selectWordCloudTop(value,limit_top_text){
		$("#limit_top_wordCloud_text").html(limit_top_text);
		$("#select_wordCloud_top_id").val(value);
		wordCloudSearch();
	}
	//重置页面搜索
	function resetWordCloudSearch(){
		$("#limit_top_wordCloud_text").html("Top100");
    	$("#select_wordCloud_top_id").val("");
    	$("#limit_department_wordCloud_text").html("全院");
    	$("#select_wordCloud_department_Id").val("");
		//时间筛选重置
		$("#limit_time_wordCloud_text").html("时间不限");
    	$("#limit_time_start_Id").val("");
    	$("#limit_time_end_Id").val("");
	}
	//词云统计
	function wordCloudSearch(){
	    var tops =$("#select_wordCloud_top_id").val();
	    var department=$("#select_wordCloud_department_Id").val();
	    //department="";
    	var createTime = $("#limit_time_start_Id").val();
    	var lastTime = $("#limit_time_end_Id").val();
		$.ajax(
		{
			type : "post",
			url : "/hssp/head/getWordCloudDetailMessage",
			data : "tops="+tops+"&department="+department+"&createTime="+createTime+"&lastTime=" + lastTime,
			dataType : "json",
			success : function(result) 
			{
				if(result.status == "succeed"){
					parent.buildwordCloudDetail(result,true);
				}else if(result.status == "succeedNull"){
					parent.buildwordCloudDetail(result,false);
				}else{
					
				}
			}
		});
		document.getElementById("wait").style.display="block";
	}
	//词云展示
	var countAdd = 0;
	var wordsCount = 0;
	var countMax = 0;
	function buildwordCloudDetailForBYWordsCloud_JS(result,ok){
	//function buildwordCloudDetail(result,ok){
		$("#canvas-container").html("");
		$("#canvas-container").html("<canvas id=\"container\" width=\"1300px\" height=\"600px\"></canvas>");
		
		//带key值的json对象转化为二维数组
		var arrData = result.wordCloudListJson;
		var wordFreqData= [];
		var countAddOK = false;
		var countSinle = 0;
		var wordscountSinle = 0;
		var countSinleMax = 0;
		if(countAdd == 0){
			countAddOK = true;
			for (var i in arrData) {
				countAdd= countAdd+arrData[i]["weight"];
				wordsCount ++;
				if(arrData[i]["weight"]>countMax){
					countMax =arrData[i]["weight"];
				}
			}
			countSinle=countAdd;
			wordscountSinle=wordsCount;
		}else{
			/* for (var i in arrData) {
				countSinle= countSinle+arrData[i]["weight"];
				if(arrData[i]["weight"]>countSinleMax){
					countSinleMax =arrData[i]["weight"];
				}
			} */
			for (var i in arrData) {
				countSinle= countSinle+arrData[i]["weight"];
				wordscountSinle ++;
				if(arrData[i]["weight"]>countSinleMax){
					countSinleMax =arrData[i]["weight"];
				}
			}
		}
		//lert(countAdd+"~~"+wordsCount+"~~"+countAdd/wordsCount+"===="+countSinle+"~~"+wordscountSinle+"~~"+countSinle/wordscountSinle);
		for (var i in arrData) {
			var item = [];
			if(countAddOK){
				item.push(arrData[i]["name"]);
				item.push(arrData[i]["weight"]);
				//countAdd= countAdd+arrData[i]["weight"];
				if(arrData[i]["weight"]>countMax){
					countMax =arrData[i]["weight"];
				}
			}else{
				if(countSinle <= 100){
					item.push(arrData[i]["name"]);
					if((countSinleMax*4+(countAdd-countSinle*2)/wordscountSinle)>countMax){
						item.push((arrData[i]["weight"]*2+(countAdd-countSinle*2)/wordscountSinle)-(countMax-(countSinleMax*2+(countAdd-countSinle*2)/wordscountSinle)));
					}else{
						item.push(arrData[i]["weight"]*2+(countAdd-countSinle*2)/wordscountSinle);
					}
				}else if(countSinle <= 200){
					item.push(arrData[i]["name"]);
					item.push(arrData[i]["weight"]*3+(countAdd-countSinle*3)/wordscountSinle);
				}else if(countSinle <= 300){
					item.push(arrData[i]["name"]);
					item.push(arrData[i]["weight"]*2+(countAdd-countSinle*2)/wordscountSinle);
				}else if(countSinle <= 400){
					item.push(arrData[i]["name"]);
					item.push(arrData[i]["weight"]*2+(countAdd-countSinle*2)/wordscountSinle);
				}else{
					item.push(arrData[i]["name"]);
					item.push(arrData[i]["weight"]+(countAdd-countSinle)/wordscountSinle);
				}
			}
			wordFreqData.push(item)
		}
		var canvas = document.getElementById('container');
		//alert(countAdd*0.4/countSinle+"---"+countMax+"~~"+countSinleMax+">>>"+countMax*(countAdd*0.4/countSinle));
	    var options = eval({
		    	tooltip: {
		                 show: true,
		                 formatter: function(item) {
		                     return item[0] + ': 词频：' + item[1] + '<br>' + '词云图'
		                 }
		            },
	            "list": wordFreqData,//或者[['各位观众',45],['词云', 21],['来啦!!!',13]],只要格式满足这样都可以
	            "gridSize": 1, // 密集程度 数字越小越密集
	            "weightFactor": 0.4, // 字体大小=原始大小*weightFactor
	            "maxFontSize": 30, //最大字号
	            "minFontSize": 12, //最小字号
	            "fontWeight": 'normal', //字体粗细
	            "fontFamily": 'Times, serif', // 字体
	            "color": 'random-light', // 字体颜色 'random-dark' 或者 'random-light'
	            "backgroundColor": '#FFFFFF', // 背景颜色
	            "rotateRatio": 0, // 字体倾斜(旋转)概率，1代表总是倾斜(旋转)
	            "shape":'circle'
	        });
	    //生成
	    WordCloud(canvas, options);
	
		document.getElementById("wait").style.display="none";
		var departmentList = eval(result.departmentList);
		var datas = "<li><a onclick=\"selectWordCloudDepartment('','全院')\">全院</a></li>";
		for (var i = 0,size = departmentList.length; i < size; i++)
	    {
	    	//科室
	    	datas = datas+"<li><a onclick=\"selectWordCloudDepartment('"+ departmentList[i] + "','"+ departmentList[i] + "')\">"+departmentList[i]+"</a></li>";
	    } 
	    $("#ul_department_wordCloud").html(datas);
	    if(ok){
			
			refreshExportMessageNext = false;
			var bodyHeight = document.getElementById("prebody").scrollHeight || document.getElementById("results").scrollHeight;
			$("#div_wordCloudDetailTotalInfo").prev().height(bodyHeight - 130);
			document.getElementById("container").style.display = "block";
	    	$("#div_wordCloudDetail").dialog("open");
			//$("#div_wordCloudDetail_show").dialog("open");
	    }else{
	    	document.getElementById("container").style.display = "none";
	    	parent.$.dialog("当次查询无数据!", false, 1500);
	       return;
	    } 
	}
	
	//function buildwordCloudDetail_for_Highcharts(result,ok){
	function buildwordCloudDetail(result,ok){
		
		var bodyHeight = document.getElementById("prebody").scrollHeight || document.getElementById("results").scrollHeight;
		$("#div_wordCloudDetailTotalInfo").prev().height(bodyHeight - 130);
		document.getElementById("container").style.display = "block";
    	$("#div_wordCloudDetail").dialog("open");
	
		var tempdatas= result.wordCloudListJson;
	    var datas = [];
	    for (var i in tempdatas) {
	      var item = {"weight":tempdatas[i]["weight"],"name":tempdatas[i]["name"]};
	      datas.push(item);
	    }
	    var datesa=eval(datas);
		Highcharts.chart('container', {
			series: [{
				type: 'wordcloud',
				name:'计数',
				rotateRatio:0,
				data: datesa
			}],
			exporting:{enabled:false},
			title: {
				text: '热词统计图'
			}
		});
	
		document.getElementById("wait").style.display="none";
		var departmentList = eval(result.departmentList);
		var datas = "<li><a onclick=\"selectWordCloudDepartment('','全院')\">全院</a></li>";
		for (var i = 0,size = departmentList.length; i < size; i++)
	    {
	    	//科室
	    	datas = datas+"<li><a onclick=\"selectWordCloudDepartment('"+ departmentList[i] + "','"+ departmentList[i] + "')\">"+departmentList[i]+"</a></li>";
	    } 
	    $("#ul_department_wordCloud").html(datas);
	    if(ok){
			
			refreshExportMessageNext = false;
			$("#div_wordCloudDetail_show").dialog("open");	
	    }else{
	    	document.getElementById("container").style.display = "none";
	    	parent.$.dialog("当次查询无数据!", false, 1500);
	       return;
	    }
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
 /*********************************fancytree树形插件引用*****************************************/
 function searchFancyLast(){
	 var dataStart = parseInt(parseInt($("#show_fancytree_search_dataStart_id").val())-1);
	 searchFancytree(dataStart);
 }
 function searchFancyNext(){
	 var dataStart = parseInt(parseInt($("#show_fancytree_search_dataStart_id").val())+1);
	 searchFancytree(dataStart);
 }
 //术语库的检索
 function searchFancytreeForSearch(keywords){
	 $("#show_fancytree_div_id").dialog("open");
	 showFancytreeSearch();
	 $("#show_fancytree_search_id").val(keywords);
	 //$("#show_fancytree_search_submit_id").click();
	 searchFancytreeForKw(1, keywords);
 }
 function searchFancytree(dataStart){
	var keywordForTree = "*"+$("#show_fancytree_search_id").val()+"*";
	searchFancytreeForKw(dataStart, keywordForTree);
 }
 function searchFancytreeForKw(dataStart,keywords){
	$("#show_fancytree_search_dataStart_id").val(dataStart);
	var pageSize = 40;	
	 $.ajax(
			   {
					type : "post",
					url : "/hssp/rest/searchFancytree?keywords="+keywords+"&pageSize="+pageSize+"&dataStart="+dataStart,
					dataType : "json",
					beforeSend:function(){
						$("#container_fancytree_search_swait").show(1000);
						$("#show_fancytree_search_list_div_id").hide();	 
					},
					success : function(data)
					{
						
						var htmls = "";
						 if(data.status == "succeed"){
							 /* htmls = htmls + "<li style=\"height:80px;\" class=\"col-lg-3 col-sm-6 col-xs-12\"><div style=\"border-radius: 10px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;\" title=\"" + title_text + "\">";
								htmls = htmls + expressions[i];
								htmls = htmls + "</div></li>"; */
							 if(data.searchList.length > 0){
								 for (var i = 0; i < data.searchList.length; i++) {
										/* htmls = htmls + "<option title=\""+data.searchList[i]["term"]+"\" value=\""  + data.searchList[i]["conceptId"]+ "\">";
										if(data.searchList[i]["term"].length < 15){
											htmls = htmls +  data.searchList[i]["term"];
										}else{
											htmls = htmls +  data.searchList[i]["term"].substring(0,15)+"...";
										} */
										 htmls = htmls + "<option title=\""+data.searchList[i].term+"\" value=\""  + data.searchList[i].conceptId+ "\">";
										if(data.searchList[i].term.length < 15){
											htmls = htmls +  data.searchList[i].term;
										}else{
											htmls = htmls +  data.searchList[i].term.substring(0,15)+"...";
										} 
										htmls = htmls + "</option>";
									}
								 //判断是否显示下一页
								 var pageCount = 0;
								 if(dataStart == 1){
									 $("#show_fancytree_search_Count_id").val(data.total);
									 pageCount = data.total;
								 }else{
									 pageCount =  parseInt($("#show_fancytree_search_Count_id").val());
								 }
								 var pageSizeTotal = Math.ceil(pageCount/pageSize);
								 if( pageSizeTotal > 1 && pageSizeTotal > dataStart){
									 $("#show_fancytree_search_submit_Next_id").show(1000);
								 }else{
									 $("#show_fancytree_search_submit_Next_id").hide();
								 }
								 if(pageSizeTotal > 1 && dataStart > 0){
									 $("#show_fancytree_search_submit_Last_id").show(1000);
								 }else{
									 $("#show_fancytree_search_submit_Last_id").hide();
								 }
								 $("#show_fancytree_search_pageNum").text("第"+(dataStart)+"/"+pageSizeTotal+"页");
								 $("#show_fancytree_search_pageNum").show();
							 }else{
								 htmls = htmls + "<option>";
								 htmls = htmls +  "未找到匹配值";
								 htmls = htmls + "</option>";
								 $("#show_fancytree_search_pageNum").hide();
								 $("#show_fancytree_search_submit_Last_id").hide();
								 $("#show_fancytree_search_submit_Next_id").hide();
							 }
						 }else{
							 htmls = htmls + "<option>";
							 htmls = htmls +  "检索失败,请刷新页面后再试或联系后台管理员！";
							 htmls = htmls + "</option>";
							 $("#show_fancytree_search_pageNum").hide();
							 $("#show_fancytree_search_submit_Last_id").hide();
							 $("#show_fancytree_search_submit_Next_id").hide();
							 parent.$.dialog("检索术语库获取失败,请刷新页面后再试或联系后台管理员！", false, 2000);
						 }
							 $("#show_fancytree_search_list_select_id").html(htmls);
							 $("#container_fancytree_search_swait").hide();
							 $("#show_fancytree_search_list_div_id").show(1000);
					},error:function(e){
		   	    		parent.location.reload();    	    
					}
				}
			); 
 }
 $("#show_fancytree_search_list_select_id").on("change",function(){
     var conceptId = $("option:selected",this).val();//需求主键
      //展示父子级关系
	 $("#show_fancytree_search_div_id").hide();
     //获取父子树
     $.ajax(
			   {
					type : "post",
					url : "/hssp/rest/getOmahaByconceptIdUp?conceptId="+conceptId,
					dataType : "json",
					beforeSend:function(){
						$("#container_fancytree_swait").show(100);
						$("#fancytree_div_id").hide();	 
					},
					success : function(data)
					{
						 if(data.status == "succeed"){
						     var source = data.OmahaList;
						     //source = "["+source+"]";
						     source = eval(source);
						     //source = [{"id":"23865250_11679331_11679591","title":"临床所见","key":"1","folder":true,"lazy":false,"children":[{"id":"23741521_11632349_11679331","title":"心电图上发现的症状   ","key":"1_1","folder":true,"lazy":true,"children":null}]}]
							 var tree = $("#fancytree_id").fancytree();
						     $("#fancytree_id").fancytree("destroy");
						     fancytree(source);
							 $("#container_fancytree_swait").hide();
							 $("#fancytree_div_id").show(1000);
							 $("#container_fancytree_search_swait").hide();
							 $("#show_fancytree_search_list_div_id").hide();
							 //$("#show_fancytree_search_id").val("");
							 reloadOK = false;
						 }else{
							 $("#container_fancytree_swait").hide();
							 parent.$.dialog("术语库获取失败,请刷新页面后再试或联系后台管理员！", false, 2000);
						 }
					},error:function(e){
		   	    		parent.location.reload();    	    
					}
				}
			); 
  }); 
 
 //展示Omaha的检索框
 function showFancytreeSearch(){
	 $("#fancytree_div_id").hide();
	 $("#show_fancytree_search_div_id").show(500);
	 //$("#show_fancytree_search_id").focus();
	 if($("#show_fancytree_search_list_select_id").html() != ""){
		 $("#show_fancytree_search_list_div_id").show(1000);
	 }
	
 }
 var pageNameStatic = "prebody";
 var reloadOK = true;
 function showFancytree(pageName){
	
	 if(pageName != null && pageName != ""){
		 pageNameStatic = 	pageName; 
	 }
	 $("#show_omaha_div_id",prebodyDocument.document).hide(1000);
	 $("#show_omaha_div_id",resultsDocument.document).hide(1000);
	 /* var source = [{ "title": "Node 1", "key": "1","folder": true,"id":"id_1" ,"lazy": true},{ "title": "Node 1", "key": "1","folder": true,"id":"id_2" }];
	  var source ="[{\"id\":\"11679591\",\"title\":\"临床所见\",\"key\":\"11679331\",\"folder\":true,\"children\":[{\"id\":\"11679591\",\"title\":\"物理能量\",\"key\":\"1822237\",\"folder\":false,\"children\":[]}]},{\"id\":\"11679331\",\"title\":\"心电图发现\",\"key\":\"11632349\",\"folder\":true,\"children\":[{\"id\":\"11679331\",\"title\":\"病变\",\"key\":\"1006993\",\"folder\":false,\"children\":[]}]},{\"id\":\"11447593\",\"title\":\"中草药剂\",\"key\":\"11677334\",\"folder\":true,\"children\":[{\"id\":\"11447593\",\"title\":\"中毒性皮病\",\"key\":\"11219692\",\"folder\":false,\"children\":[]}]},{\"id\":\"11740332\",\"title\":\"硒结合\",\"key\":\"11719139\",\"folder\":true,\"children\":[{\"id\":\"11740332\",\"title\":\"RPL36A\",\"key\":\"12898141\",\"folder\":false,\"children\":[]}]},{\"id\":\"11740321\",\"title\":\"视黄醇转运\",\"key\":\"11693022\",\"folder\":true,\"children\":[{\"id\":\"11740321\",\"title\":\"RPL36A\",\"key\":\"12898141\",\"folder\":false,\"children\":[]}]},{\"id\":\"14224015\",\"title\":\"单核苷酸变异\",\"key\":\"12971875\",\"folder\":true,\"children\":[{\"id\":\"14224015\",\"title\":\"复杂突变\",\"key\":\"12972007\",\"folder\":false,\"children\":[]}]},{\"id\":\"12571007\",\"title\":\"AAVS1\",\"key\":\"12591731\",\"folder\":true,\"children\":[{\"id\":\"12571007\",\"title\":\"假基因\",\"key\":\"12571030\",\"folder\":false,\"children\":[]}]},{\"id\":\"11663487\",\"title\":\"动物群\",\"key\":\"1305775\",\"folder\":true,\"children\":[{\"id\":\"11663487\",\"title\":\"鳃霉\",\"key\":\"11509240\",\"folder\":false,\"children\":[]}]},{\"id\":\"1327421\",\"title\":\"装置\",\"key\":\"1826550\",\"folder\":true,\"children\":[{\"id\":\"1327421\",\"title\":\"锐器穿透伤\",\"key\":\"11364283\",\"folder\":false,\"children\":[]}]},{\"id\":\"1497848\",\"title\":\"暴露于水污染,具有潜在健康问题\",\"key\":\"1232408\",\"folder\":true,\"children\":[{\"id\":\"1497848\",\"title\":\"运动物体撞击静止物体引起的损伤\",\"key\":\"1531367\",\"folder\":false,\"children\":[]}]},{\"id\":\"11679396\",\"title\":\"诱发电位\",\"key\":\"1383272\",\"folder\":true,\"children\":[{\"id\":\"11679396\",\"title\":\"牙移动性\",\"key\":\"1295477\",\"folder\":false,\"children\":[]}]},{\"id\":\"1822237\",\"title\":\"辐射\",\"key\":\"1234864\",\"folder\":true,\"children\":[{\"id\":\"1822237\",\"title\":\"热和/或光的影响\",\"key\":\"1474473\",\"folder\":false,\"children\":[]}]},{\"id\":\"1728309\",\"title\":\"月经\",\"key\":\"1671636\",\"folder\":true,\"children\":[{\"id\":\"1728309\",\"title\":\"基因位点\",\"key\":\"18168345\",\"folder\":false,\"children\":[]}]},{\"id\":\"12136876\",\"title\":\"肿瘤分期的解剖学标志\",\"key\":\"11414803\",\"folder\":true,\"children\":[{\"id\":\"12136876\",\"title\":\"形态学改变\",\"key\":\"11111046\",\"folder\":false,\"children\":[]}]}]";
	 source = eval(source);*/ 
	 /* fancytree(source);
	 $("#container_fancytree_swait").hide(1000);
	 $("#fancytree_div_id").show(1000);
	  return; 
	  */
	  $("#show_fancytree_div_id").dialog("open");
	  $("#fancytree_div_id").hide(); 
	  $("#show_fancytree_search_div_id").hide();
	  $("#container_fancytree_swait").show(1000);
	 if(reloadOK){
		 $.ajax(
				   {
						type : "post",
						url : "/hssp/rest/getOmaha",
						dataType : "json",
						beforeSend:function(){
							/* $("#container_fancytree_swait").show(1000);
							$("#fancytree_div_id").hide();	 */
						},
						success : function(data)
						{
							 if(data.status == "succeed"){
							     var source = data.OmahaList;
							     //source = "["+source+"]";
							     source = eval(source);
							     //source = [{"id":"23865250_11679331_11679591","title":"临床所见","key":"1","folder":true,"lazy":false,"children":[{"id":"23741521_11632349_11679331","title":"心电图上发现的症状   ","key":"1_1","folder":true,"lazy":true,"children":null}]}]
								 fancytree(source);
								 $("#container_fancytree_swait").hide();
								 $("#fancytree_div_id").show(1000);
								 $("#container_fancytree_search_swait").hide();
								 $("#show_fancytree_search_list_div_id").hide();
								 $("#show_fancytree_search_id").val("");
								 reloadOK = false;
							 }else{
								 parent.$.dialog("术语库获取失败,请刷新页面后再试或联系后台管理员！", false, 2000);
							 }
						},error:function(e){
			   	    		parent.location.reload();    	    
						}
					}
				); 
	 }else{
		 $("#container_fancytree_swait").hide();
		 $("#fancytree_div_id").show(1000);
	 }
	 
 }

 function fancytree(source){
 	// --- Initialize sample trees
 	$("#fancytree_id").fancytree({
 		extensions: ["filter","dnd5"],
		quicksearch: true,
		filter: {
			autoApply: true,   // Re-apply last filter if lazy data is loaded
			autoExpand: false, // Expand all branches that contain matches while filtered
			counter: true,     // Show a badge with number of matching child nodes near parent icons
			fuzzy: false,      // Match single characters in order, e.g. 'fb' will match 'FooBar'
			hideExpandedCounter: true,  // Hide counter badge if parent is expanded
			hideExpanders: false,       // Hide expanders if all child nodes are hidden by filter
			highlight: true,   // Highlight matches by wrapping inside <mark> tags
			leavesOnly: false, // Match end nodes only
			nodata: true,      // Display a 'no data' status node if result is empty
			mode: "dimm"       // Grayout unmatched nodes (pass "hide" to remove unmatched node instead)
		},
		dnd5: {
			dragStart: function(node, data) { return true; },
			dragEnter: function(node, data) { return true; },
			dragDrop: function(node, data) { 
				 return false; 
			}
		},
		activeVisible : true,
		debugLevel:0, //默认为2。其中0代表纯净版，1为正常，2为debug模式，开发版。推荐release时，设置为0。
 		aria: true,
 		autoActivate: true,
		checkbox:false,
		icon:true,
 		source: source,
 		autoActivate: false, // we use scheduleAction()
 		autoCollapse: true,
		autoFocus: true,
 		autoScroll: true,
 		focusOnSelect: true,
 		escapeTitles: true,
 		generateIds: true,
 		keyboard: true,
 		keyPathSeparator: "/",
 		clickFolderMode: 3, // expand with single click
 		minExpandLevel: 1,
 		selectMode: 1,
 		titlesTabbable: true,
 		tooltip: true ,
 		tabindex: "-1", // we don't want the focus frame
 		// toggleEffect: { effect: "blind", options: {direction: "vertical", scale: "box"}, duration: 2000 },
 		// scrollParent: null, // use $container
 		init: function(event, data) {
				// Set key from first part of title (just for this demo output)
				/* data.tree.visit(function(n) {
					n.key = n.title.split(" ")[0];
				}); */
		},
 		tooltip: function(event, data) {
 			return data.node.title;
 		},
 		focus: function(event, data) {
 			var node = data.node;
 			// Auto-activate focused node after 1 second
 			if(node.data.href){
 				node.scheduleAction("activate", 0);
 			}
 		},
 		blur: function(event, data) {
 			data.node.scheduleAction("cancel");
 		},
 		beforeActivate: function(event, data){
 			var node = data.node;

 			if( node.data.href && node.data.target === "_blank") {
 				window.open(node.data.href, "_blank");
 				return false; // don't activate
 			}
 		},
 		activate: function(event, data){
 			/* var node = data.node,
 				orgEvent = data.originalEvent || {};

 			// Open href (force new window if Ctrl is pressed)
 			if(node.data.href){
 				window.open(node.data.href, (orgEvent.ctrlKey || orgEvent.metaKey) ? "_blank" : node.data.target);
 			}
 			// When an external link was clicked, we don't want the node to become
 			// active. Also the URL fragment should not be changed
 			if( node.data.target === "_blank") {
 				return false;
 			}
 			// Append #HREF to URL without actually loading content
 			// (We check for this value on page load re-activate the node.)
 			if( window.parent &&  parent.history && parent.history.pushState ) {
 				parent.history.pushState({title: node.title}, "", "#" + (node.data.href || ""));
 			} */
 			/* var node = data.node;
			$("#echoSelection1").text(node.title + ", key=" + node.key); */
 		},
 		click: function(event, data){
 			/* // We implement this in the `click` event, because `activate` is not
 			// triggered if the node already was active.
 			// We want to allow re-loads by clicking again.
 			var node = data.node,
 				orgEvent = data.originalEvent;

 			// Open href (force new window if Ctrl is pressed)
 			if(node.isActive() && node.data.href){
 				window.open(node.data.href, (orgEvent.ctrlKey || orgEvent.metaKey) ? "_blank" : node.data.target);
 			} */
 			 var node = data.node;
			//$("#echoSelection1").text(node.title + ", id=" + node.tree.focusNode.data.id); 
			node.setFocus();
 		},
 		select: function(event, data) {
			// Display list of selected nodes
			var s = data.tree.getSelectedNodes().join(", ");
			//$("#echoSelection1").text(s);
		},
 		lazyLoad: function(event, data) {
 			var node = data.node;
 			var id = node.tree.focusNode.data.id;
			data.result =$.ajax({
				type : "post",
				url: "/hssp/rest/getOmahaByconceptId?conceptId="+id+"&key="+node.key,
				dataType: "json",
				success : function(data)
				{
					if(data == null){
						 node.folder = false;
					}
				},
				error:function(e){
	   	    		parent.location.reload();    	    
				}
			});
		}
 	});
 	// On page load, activate node if node.data.href matches the url#href
 	var tree = $("#fancytree_id").fancytree("getTree"),
 		frameHash = window.parent && window.parent.location.hash;
 	$("input[name=search]").on("keyup", function(e){
		var n,
			tree = $.ui.fancytree.getTree(),
			args = "autoApply autoExpand fuzzy hideExpanders highlight leavesOnly nodata".split(" "),
			opts = {},
			filterFunc = $("#branchMode").is(":checked") ? tree.filterBranches : tree.filterNodes,
			match = $(this).val();

		$.each(args, function(i, o) {
			opts[o] = $("#" + o).is(":checked");
		});
		opts.mode = $("#hideMode").is(":checked") ? "hide" : "dimm";

		if(e && e.which === $.ui.keyCode.ESCAPE || $.trim(match) === ""){
			$("button#btnResetSearch").click();
			return;
		}
		if($("#regex").is(":checked")) {
			// Pass function to perform match
			n = filterFunc.call(tree, function(node) {
				return new RegExp(match, "i").test(node.title);
			}, opts);
		} else {
			// Pass a string to perform case insensitive matching
			n = filterFunc.call(tree, match, opts);
		}
		$("button#btnResetSearch").attr("disabled", false);
		$("span#matches").text("(" + n + "match)");
	}).focus();

	$("button#btnResetSearch").click(function(e){
		$("input[name=search]").val("");
		$("span#matches").text("");
		tree.clearFilter();
	}).attr("disabled", true);
	$("fieldset input:checkbox").change(function(e){
		var id = $(this).attr("id"),
			flag = $(this).is(":checked");

		// Some options can only be set with general filter options (not method args):
		switch( id ){
		case "counter":
		case "hideExpandedCounter":
			tree.options.filter[id] = flag;
			break;
		}
		tree.clearFilter();
		$("input[name=search]").keyup();
	});
 	if( frameHash ) {
 		frameHash = frameHash.replace("#", "");
 		tree.visit(function(n) {
 			if( n.data.href && n.data.href === frameHash ) {
 				n.setActive();
 				return false; // done: break traversal
 			}
 		});
 	}
 };
 $("#show_fancytree_option").click(function(){
	  $("#show_fancytree_option_div").toggle(1000);
	});
</script>
</body>
</html>