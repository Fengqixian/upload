<%@page import="java.util.Map.Entry"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.clinbrain.hssp.ws.dto.UploadMessage" %>
<%@ page import="com.clinbrain.hssp.ws.dto.ExportMessage" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>临床大数据搜索</title>
    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/companylogo.png">
    <link rel="stylesheet" type="text/css" media="print" href="yyy.css">
	<meta name="viewport" content="width=device-width">
	<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-2.1.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/jquery-ui-1.11.2.custom/jquery-ui.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/Scriptsbybs/bootstrap-treeview-dist/bootstrap-treeview.min.js"></script>
	<!--  <script charset="utf-8" src="http://myfavlink.sinaapp.com/js/cnc.js?cat"></script>-->
	<script src="${pageContext.request.contextPath}/resources/clinbrain/jquery-confirm/jquery-confirm.js"></script>
<style type="text/css">
#title {
	text-align: center;
}
.text_class {
	font-size: 20px;
	font-family: inherit;
}
.divcss5-b{ width:1000px; border:1px solid black;margin:auto;overflow-y:scroll; } 
.warn{ width:1000px; border:1px solid black;margin:auto;} 
@media print{
    .p_class{
    	font-weight:bold;
    }
}
</style>
  </head>
<body>

<div class="divcss5-b" id="message_show_div">
<button style="font-size:19px;text-align: left;color: red;"><a onclick="downloadFile()">打印</a></button><br/>
<!--startprint-->
<%
	  UploadMessage uploadMessage = (UploadMessage) request.getAttribute("uploadMessage");
	%>
<div  id="message_print_div">
  <div style="text-align: center; font-size: 36px; font-family: SimHei;"><strong><span id="title">长海医院临床数据导出备案登记表</span></strong></div>
  <br/>
<div>
<input type="hidden" id="timestamp" value=""/>
<input type="hidden" id="userID" value="<%=uploadMessage.getUserID()%>"/>
<input type="hidden" id="uploadMessageID" value="<%=uploadMessage.getId()%>"/>
<input type="hidden" id="exportTable" value="<%=uploadMessage.getExportMessage().getExportMessageMap().size()%>"/>
<input type="hidden" id="exportFieldCount" value="<%=uploadMessage.getExportMessage().getFieldCount()%>"/>
<div style="text-align: right;width: 900px;">
   <span  id="print_time"></span>
</div>
  <table width="900" border="1" cellpadding="0" cellspacing="0" bordercolor="#000000" align="center">
    <tr>
      <td width="150" height="60" align="center" valign="middle" class="text_class"><span style="color:red">*</span>申请科室</td>
      <td width="300"  height="30" align="center" valign="middle" id="departMentName_td">
      <input class="text_class" maxlength="20" style="display:none; width: 100%; height: inherit; border: 0px; text-align: center;" type="text"  name="departMentName" id="departMentName_id"/>
      <input class="text_class" style="width: 100%; height: inherit; border: 0px;text-align: center;" type="text" name="departMentName" disabled="disabled" id="departMentName_default" value="<%=uploadMessage.getDepartMentName()%>"/></td>
      <td width="150" height="60" align="center" valign="middle" class="text_class">搜索内容<br />是否本科室</td>
      <td  height="30" align="center" valign="middle" class="text_class">
        <input type="radio" name="check_1" id="check_1_1" value="是" checked="checked" onclick="selectDept()"/>
      是 &nbsp;&nbsp;&nbsp;<input type="radio" name="check_1" id="check_1_2" value="否" onclick="selectDept()"/>否      
      </td>
    </tr>
    <tr>
      <td width="150" height="60" align="center" valign="middle" class="text_class"><span style="color:red">*</span>申 请 人</td>
      <td  height="60" align="center" valign="middle" id="person_td">
      <input class="text_class" maxlength="20" style=" width: 100%; height: inherit; border: 0px; text-align: center;" type="text"  name="person" id="person_id"/></td>
      <td width="150"  height="60" align="center" valign="middle" class="text_class"><span style="color:red">*</span>联系电话</td>
      <td  height="60" align="center" valign="middle" id="phoneNumber_td">
      <input class="text_class" maxlength="20" media="print" style="width: 100%; height: inherit; border: 0px; text-align: center;" type="text"  name="phoneNumber" id="phoneNumber_id"/></td>
    </tr>
    <tr>
      <td width="150" height="100" align="center" valign="middle" class="text_class"><span style="color:red">*</span>研究目的</td>
      <td  height="100" align="left" valign="middle" colspan="3" id="description_td">
       	<textarea class="text_class" cols="5" maxlength="200" style="width: 100%; height: inherit; border: 0px; resize:none;" name="description" id="description_id"></textarea>
      </td>
    </tr>
    <tr>
      <td width="150" height="75" align="center" valign="middle" class="text_class"><span style="color:red">*</span>研究类型</td>
      <td align="left" valign="middle" colspan="3" id="studyType_td">
          <input type="checkbox" name="studyType" value="队列研究" title="选择/不选择">队列研究</input>
          <input type="checkbox" name="studyType" value="病例对照" title="选择/不选择">病例对照</input>
          <input type="checkbox" name="studyType" value="非随机对照研究" title="选择/不选择">非随机对照研究</input>
          <input type="checkbox" name="studyType" value="病例报告研究" title="选择/不选择">病例报告研究</input>
          <input type="checkbox" name="studyType" value="病例系列研究" title="选择/不选择">病例系列研究</input>
          <input type="checkbox" name="studyType" value="横断面研究" title="选择/不选择">横断面研究</input><br/>
          <input type="checkbox" name="studyType" value="其他" title="选择/不选择">其他</input>
          <font face="Courier"><input id="studyTypeOther" style="border-left:medium none;border-right:medium none;border-top:medium none;border-bottom:1px solid rgb(192,192,192)"/></font>
      </td>
    </tr>
    <tr>
      <td width="150" rowspan="4" height="150" align="center" valign="middle" class="text_class"><span style="color:red">*</span>预期研究<br />成    果</td>
      <td align="left" valign="middle" colspan="3" id="paper_td">
                           发表论文：
          <input type="checkbox" name="paper" value="SCI" title="选择/不选择">SCI &nbsp;</input>
          <input type="checkbox" name="paper" value="北大中文核心" title="选择/不选择">北大中文核心</input>
          <input type="checkbox" name="paper" value="统计源" title="选择/不选择">统计源</input>
          <input type="checkbox" name="paper" value="一般" title="选择/不选择">一般</input>
      </td>
    </tr>
    <tr>
    	<td align="left" valign="middle" colspan="3" id="question_td">
                           课题申请：
          <input type="checkbox" name="question" value="国家级" title="选择/不选择">国家级</input>
          <input type="checkbox" name="question" value="省部级" title="选择/不选择">省部级</input>
          <input type="checkbox" name="question" value="校级" title="选择/不选择">校级</input>
          <input type="checkbox" name="question" value="其他" title="选择/不选择">其他</input>
          <font face="Courier"><input id="questionOther" style="border-left:medium none;border-right:medium none;border-top:medium none;border-bottom:1px solid rgb(192,192,192)"/></font>
      </td>
    </tr>
    <tr>
      <td align="left" valign="middle" colspan="3" id="award_td">
                          报&nbsp;&nbsp;奖：
          <input type="checkbox" name="award" value="国家级" title="选择/不选择">国家级</input>
          <input type="checkbox" name="award" value="省部级" title="选择/不选择">省部级</input>
          <input type="checkbox" name="award" value="校级" title="选择/不选择">校级</input>
          <input type="checkbox" name="award" value="其他" title="选择/不选择">其他</input>
          <font face="Courier"><input id="awardOther" style="border-left:medium none;border-right:medium none;border-top:medium none;border-bottom:1px solid rgb(192,192,192)"/></font>
      </td>
    </tr>
    <tr>
      <td align="left" valign="middle" colspan="3" id="other_td">
                           其&nbsp;&nbsp;他：
          <font face="Courier"><input id="other" style="border-left:medium none;border-right:medium none;border-top:medium none;border-bottom:1px solid rgb(192,192,192)"/></font>
      </td>
    </tr>
    <tr>
      <td width="150" height="70" align="center" valign="middle" class="text_class">重要说明</td>
      <td align="left" valign="middle" colspan="3" >
      	  <p class="p_class" style="font-weight:bold;color:#000000;font-size:18px">①研究成果进展情况请于当年12月1日前反馈至信息科数据中心（电话62419）；<br />
      	  	 ②为保障信息系统安全，防止病毒感染，拷贝数据请自行提供未启封U盘，请理解配合。
      	  </p>
      </td>
    </tr>
    <tr>
      <td height="250" align="center" valign="middle" class="text_class">数据内容</td>
      <td width="600" align="center" valign="middle" colspan="3">
      <table width="100%" border="1" cellpadding="0" cellspacing="0" bordercolor="#C0C0C0">
        <tr>
          <td width="150" height="40" align="right" valign="middle">检索账户：</td>
          <td width="150" height="40" align="center" valign="middle"><%=uploadMessage.getUserName()%></td>
          <td width="150" height="40" align="right" valign="middle">上传时间：</td>
          <td height="30" align="center" valign="middle"><%=uploadMessage.getUploadDate()%></td>
        </tr>
        <tr>
          <td width="150" height="40" align="right" valign="middle">上传文件名称：</td>
          <td height="20" colspan="3" align="center" valign="middle"><%=uploadMessage.getFileName()%>(<%=uploadMessage.getFileSize()%>)</td>
          </tr>
        <tr>
          <td width="150" height="40" align="right" valign="middle">导出数据涉及：</td>
          <td height="20" colspan="3" align="center" valign="middle">共:<%=uploadMessage.getExportMessage().getExportCount()%>条就诊记录,<%=uploadMessage.getExportMessage().getExportMessageMap().size()%>张表,<%=uploadMessage.getExportMessage().getFieldCount()%>个字段</td>
        </tr>
        <tr>
	      <td width="150" height="40" align="right" valign="middle"><span style="color:red">*</span>数据时间跨度：</td>
	      <td  height="30" align="left" valign="middle" colspan="3" id="dataTime_td">
	       	<input maxlength="20" class="text_class" style="width: 100%; height: inherit; border: 0px; text-align: center;" type="text"  name="dataTime" id="dataTime_id"/></td>
	      </td>
	    </tr>  
        <tr>
	      <td width="150" height="90" align="right" valign="middle"><span style="color:red">*</span>导出内容描述：</td>
	      <td  height="90" align="left" valign="middle" colspan="3" id="uploadDescription_td">
	       	<textarea class="text_class" cols="5" maxlength="200" style="width: 100%; height: inherit; border: 0px; resize:none;" name="uploadDescription" id="uploadDescription_id"></textarea>
	      </td>
	    </tr>  
      </table>
      </td>
    </tr>
    <tr class="text_class">
      <td height="80" colspan="4">申请人声明：<br />
       <p>&nbsp;&nbsp;本单位和本人已知悉泄露数据是违法行为，在此保证全部获取的数据仅用于上述指定用途，必将<br/>
       妥善保管，未经批准不得向第三方提供，并愿承担就此产生的一切法律责任。</p>   
<div style="text-align:right"><span>申请人签名：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;月&nbsp;&nbsp;日 </span></div></td>
    </tr>
    <tr>
      <td width="150" height="60" align="center" valign="middle" class="text_class">本科室<br />
      意见</td>
      <td  height="30" align="center" valign="middle" colspan="3">&nbsp;</td>
    </tr>
    <tr>
      <td width="150" height="60" align="center" valign="middle" class="text_class">跨科室<br />
      科室意见</td>
      <td  height="30" align="center" valign="middle" colspan="3">&nbsp;</td>
    </tr>
    <tr>
      <td width="150" height="60" align="center" valign="middle" class="text_class">信息科<br />
      意见</td>
      <td  height="30" align="center" valign="middle" colspan="3">
        是否敏感数据   
      <input type="radio" name="check_2" id="check_2_1" value="是" />是 &nbsp;&nbsp;&nbsp;&nbsp;
      <input type="radio" name="check_2" id="check_2_2" value="否" />否      
      </form></td>
    </tr>
    <tr>
      <td width="150" height="60" align="center" valign="middle" class="text_class">医务处<br />
      意见</td>
      <td  height="30" align="center" valign="middle" colspan="3">&nbsp;</td>
    </tr>
    <tr>
      <td width="150" height="60" align="center" valign="middle" class="text_class">分管院领导<br />
      意见</td>
      <td  height="30" align="center" valign="middle" colspan="3">&nbsp;</td>
    </tr>
  </table>
  <div style="text-align: right;width: 900px;">
	   <span id="smartFileId"></span>
   </div>
</div>
</div>

<!--endprint-->
</div>
<div class="warn">
填表说明：<br/>
1、	涉及跨科室数据需报相应科室主任审批。<br/>
2、	涉及敏感数据的搜索、所有数据的导出，需报医务处领导审批。<br/>
3、	涉及敏感数据的导出，需报分管院领导审批。<br/>
4、	敏感数据包括：病人隐私信息（联系电话、证件号、通讯地址等）、统方信息（医生开具的药品、材料信息等）。<br/>
5、	其他各项为必填项。<br/>
</div>

</body>
<script>
$(document).ready(function()
	{
		var windows_height = $(window).height();
		$("#message_show_div").css({
			height:windows_height/3*1.5
			});
			
		if(window.matchMedia){
			var mediaQueryList= window.matchMedia('print');
			mediaQueryList.addListener(function(mql){
				if(mql.matches){
				}else{
					downloadFileAdd();
				}
			})
		}
	})
	function selectDept()
	{
	     var dept = $("input[name='check_1']:checked").val();
	     //var htmlText = "<input class=\"text_class\" maxlength=\"20\" style=\"display:none; width: 100%; height: inherit; border: 0px; text-align: center;\" type=\"text\"  name=\"departMentName\" id=\"departMentName_id\"/>";
		 if(dept == "是"){
		    $("#departMentName_default").show();
		    $("#departMentName_id").hide();
		    $("#check_1_1").prop("checked",true);
		 }else{
		    $("#departMentName_id").show();
		    $("#departMentName_default").hide();
		    $("#check_1_2").prop("checked",true);
		 }
	}
	function downloadFile(){
	   //多选框
	   var studyType = $("input[name='studyType']:checked");
	   var studyTypeValue="";
	   studyType.each(function(){
	   	   studyTypeValue +=$(this).val()+",";
	   })
	   studyTypeValue =studyTypeValue.substring(0,studyTypeValue.length-1);
	   var paper = $("input[name='paper']:checked");
	   var paperValue="";
	   paper.each(function(){
	   	   paperValue +=$(this).val()+",";
	   })
	   paperValue =paperValue.substring(0,paperValue.length-1);
	   var question = $("input[name='question']:checked");
	   var questionValue="";
	   question.each(function(){
	   	   questionValue +=$(this).val()+",";
	   })
	   questionValue =questionValue.substring(0,questionValue.length-1);
	   var award = $("input[name='award']:checked");
	   var awardValue="";
	   award.each(function(){
	   	   awardValue +=$(this).val()+",";
	   })
	   awardValue =awardValue.substring(0,awardValue.length-1);
	   var studyTypeOther=$("#studyTypeOther").val();
	   var questionOther=$("#questionOther").val();
	   var awardOther=$("#awardOther").val();
	   var other=$("#other").val();
	   var dept = $("input[name='check_1']:checked").val();
	   var selectCheck = $("input[name='check_2']:checked").val();
	   var personName=$("#person_id").val();
	   var personName_td=$("#person_td").html();
	   var departMentName=$("#departMentName_id").val();
	   var departMentName_td=$("#departMentName_td").html();
	   var phoneNumber=$("#phoneNumber_id").val();
	   var phoneNumber_td=$("#phoneNumber_td").html();
	   var description=$("#description_id").val();
	   var description_td=$("#description_td").html();
	   var dataTime=$("#dataTime_id").val();
	   var dataTime_td=$("#dataTime_td").html();
	   var uploadDescription=$("#uploadDescription_id").val();
	   var uploadDescription_td=$("#uploadDescription_td").html();
	   if(departMentName=="" && dept=="否"){
	   		alert("申请科室,必须填写");
	   		return;
	   }
	   if(personName==""){
	   		alert("申 请 人,必须填写");
	   		return;
	   }
	   if(phoneNumber==""){
	   		alert("联系电话,必须填写");
	   		return;
	   }
	   if(description==""){
	   		alert("研究目的,必须填写");
	   		return;
	   }
	   if(studyTypeValue.length==0){
	   		alert("研究类型,最少需要一个选项");
	   		return;
	   }
	   if(dataTime==""){
	   		alert("数据时间跨度,必须填写");
	   		return;
	   }
	   if(uploadDescription.length==0){
	   		alert("导出内容描述,必须填写");
	   		return;
	   }
	   if(paperValue.length>0 || questionValue.length>0 || awardValue.length>0 || other!=""){
	   }else{
	   		alert("预期研究成果,最少需要一个选项或填写'其他'中的内容");
	   		return;
	   }
	   if(studyTypeValue.indexOf("其他") !=-1 && studyTypeOther==""){
			alert("因勾选了'研究类型'中的'其他',必须填写具体内容");
	   		return;
		}
		if(questionValue.indexOf("其他") !=-1 && questionOther==""){
			alert("因勾选了'课题申请'中的'其他',必须填写具体内容");
	   		return;
		}
		if(awardValue.indexOf("其他") !=-1 && awardOther==""){
			alert("因勾选了'报 奖'中的'其他',必须填写具体内容");
	   		return;
		}
		
	   //var personName=$("#print_time").val();
	   //var personName=$("#print_time").val();
		bdhtml=window.document.body.innerHTML;
		sprnstr="<!--startprint-->";
		eprnstr="<!--endprint-->";
		prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+17);
		prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr));
		window.document.body.innerHTML=prnhtml;
	    $("#person_td").html(personName_td);
		$("#person_id").val(personName);
	    $("#phoneNumber_td").html(phoneNumber_td);
		$("#phoneNumber_id").val(phoneNumber);
	    $("#description_td").html(description_td);
		$("#description_id").val(description);
	    $("#dataTime_td").html(dataTime_td);
		$("#dataTime_id").val(dataTime);
	    $("#uploadDescription_td").html(uploadDescription_td);
		$("#uploadDescription_id").val(uploadDescription);
		if(studyTypeValue.indexOf("其他") !=-1){
			$("#studyTypeOther").val(studyTypeOther);
		}
		if(questionValue.indexOf("其他") !=-1){
			$("#questionOther").val(questionOther);
		}
		if(awardValue.indexOf("其他") !=-1){
			$("#awardOther").val(awardOther);
		}
		$("#other").val(other);
	   if(dept == "否"){
	    $("#departMentName_td").html(departMentName_td);
		$("#departMentName_id").val(departMentName);
		$("#check_1_2").prop("checked",true);
	   }else if(dept == "是"){
	    $("#check_1_1").prop("checked",true);
	   }else{
	   }
	   if(selectCheck == "否"){
	    $("#check_2_2").prop("checked",true);
	   }else if(selectCheck == "是"){
	     $("#check_2_1").prop("checked",true);
	   }else{
	   }
	   var studyTypeArray=studyTypeValue.split(",");
	   for(var i=0;i<studyTypeArray.length;i++){
  		$("input[name='studyType']").each(function(){
			if($(this).val()==studyTypeArray[i]){
			   $(this).attr("checked","checked");
			}
  		})
	   }
	   var paperArray=paperValue.split(",");
	   for(var i=0;i<paperArray.length;i++){
  		$("input[name='paper']").each(function(){
			if($(this).val()==paperArray[i]){
			   $(this).attr("checked","checked");
			}
  		})
	   }
	   var questionArray=questionValue.split(",");
	   for(var i=0;i<questionArray.length;i++){
  		$("input[name='question']").each(function(){
			if($(this).val()==questionArray[i]){
			   $(this).attr("checked","checked");
			}
  		})
	   }
	   var awardArray=awardValue.split(",");
	   for(var i=0;i<awardArray.length;i++){
  		$("input[name='award']").each(function(){
			if($(this).val()==awardArray[i]){
			   $(this).attr("checked","checked");
			}
  		})
	   }
		var dateTime = new Date();
		var timestamp=dateTime.getTime();
		var smartFileId = $("#uploadMessageID").val();
	    $("#print_time").text(dateTime.toLocaleDateString());
	    $("#smartFileId").text("编号: "+timestamp);
		window.print(); 
		window.document.body.innerHTML=bdhtml;
		//timestamp保存后存放数据库
		$("#timestamp").val(timestamp);
		$("#person_td").html(personName_td);
		$("#person_id").val(personName);
	    $("#phoneNumber_td").html(phoneNumber_td);
		$("#phoneNumber_id").val(phoneNumber);
	    $("#description_td").html(description_td);
		$("#description_id").val(description);
		$("#studyTypeOther").val(studyTypeOther);
		$("#questionOther").val(questionOther);
		$("#awardOther").val(awardOther);
		$("#other").val(other);
		$("#dataTime_td").html(dataTime_td);
		$("#dataTime_id").val(dataTime);
	    $("#uploadDescription_td").html(uploadDescription_td);
		$("#uploadDescription_id").val(uploadDescription);
	   if(dept == "否"){
	    $("#departMentName_td").html(departMentName_td);
		$("#departMentName_id").val(departMentName);
		$("#check_1_2").prop("checked",true);
	   }else if(dept == "是"){
	    $("#check_1_1").prop("checked",true);
	   }else{
	   }
	   if(selectCheck == "否"){
	    $("#check_2_2").prop("checked",true);
	   }else if(selectCheck == "是"){
	     $("#check_2_1").prop("checked",true);
	   }else{
	   }
	   var studyTypeArray=studyTypeValue.split(",");
	   for(var i=0;i<studyTypeArray.length;i++){
  		$("input[name='studyType']").each(function(){
			if($(this).val()==studyTypeArray[i]){
			   $(this).attr("checked","checked");
			}
  		})
	   }
	   var paperArray=paperValue.split(",");
	   for(var i=0;i<paperArray.length;i++){
  		$("input[name='paper']").each(function(){
			if($(this).val()==paperArray[i]){
			   $(this).attr("checked","checked");
			}
  		})
	   }
	   var questionArray=questionValue.split(",");
	   for(var i=0;i<questionArray.length;i++){
  		$("input[name='question']").each(function(){
			if($(this).val()==questionArray[i]){
			   $(this).attr("checked","checked");
			}
  		})
	   }
	   var awardArray=awardValue.split(",");
	   for(var i=0;i<awardArray.length;i++){
  		$("input[name='award']").each(function(){
			if($(this).val()==awardArray[i]){
			   $(this).attr("checked","checked");
			}
  		})
	   }
	}
	
	function downloadFileAdd(){
		//多选框
	   var studyType = $("input[name='studyType']:checked");
	   var studyTypeValue="";
	   studyType.each(function(){
	   	   studyTypeValue +=$(this).val()+",";
	   })
	   studyTypeValue =studyTypeValue.substring(0,studyTypeValue.length-1);
	   var paper = $("input[name='paper']:checked");
	   var paperValue="";
	   paper.each(function(){
	   	   paperValue +=$(this).val()+",";
	   })
	   paperValue =paperValue.substring(0,paperValue.length-1);
	   var question = $("input[name='question']:checked");
	   var questionValue="";
	   question.each(function(){
	   	   questionValue +=$(this).val()+",";
	   })
	   questionValue =questionValue.substring(0,questionValue.length-1);
	   var award = $("input[name='award']:checked");
	   var awardValue="";
	   award.each(function(){
	   	   awardValue +=$(this).val()+",";
	   })
	   awardValue =awardValue.substring(0,awardValue.length-1);
	   var studyTypeOther=$("#studyTypeOther").val();
	   var questionOther=$("#questionOther").val();
	   var awardOther=$("#awardOther").val();
	   var other=$("#other").val();
	   var dept = $("input[name='check_1']:checked").val();
	   var departMentName="";
	   if(dept == "是"){
	   		departMentName=$("#departMentName_default").val();
	   }else{
	   		departMentName=$("#departMentName_id").val();
	   }
	 //  var selectCheck = $("input[name='check_2']:checked").val();
	   var personName=$("#person_id").val();
	   var phoneNumber=$("#phoneNumber_id").val();
	   var description=$("#description_id").val();
	   
	   var timestamp = $("#timestamp").val();
	   var userID = $("#userID").val();
	   var uploadMessageID = $("#uploadMessageID").val();
	   var exportTable = $("#exportTable").val();
	   var exportFieldCount = $("#exportFieldCount").val();
	   var dataTime=$("#dataTime_id").val();
	   var uploadDescription=$("#uploadDescription_id").val();
	  /*  var fileName = $("#fileName").val();
	   var fileSize = $("#fileSize").val();
	   var exportCount = $("#exportCount").val();
	    */
	   $.ajax({
			type : "post",
			url : "/hssp/body/addSmartFile",
			data :{dataTime:dataTime,uploadDescription:uploadDescription,timestamp:timestamp,studyTypeValue:studyTypeValue,paperValue:paperValue,questionValue:questionValue,awardValue:awardValue,studyTypeOther:studyTypeOther,questionOther:questionOther,awardOther:awardOther,other:other,dept:dept,departMentName:departMentName,personName:personName,phoneNumber:phoneNumber,description:description,userID:userID,uploadMessageID:uploadMessageID,exportTable:exportTable,exportFieldCount:exportFieldCount},
			dataType : "json",
			success : function(result)
			{
				/* treeNodes = result.diseaseDBFieldInfo;
				diseaseTableAndFieldDisplay("diseaseTableAndFieldTree");
				setTimeout("$(\"#div_diseaseTableAndField\").dialog(\"open\")",100); */
			}
		});
	}
	
</script>
</html>
