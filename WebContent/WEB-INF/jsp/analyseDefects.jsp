<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@page import="com.hcl.atf.taf.controller.HomePageController"%>
<%@page import="com.hcl.atf.taf.model.UserList"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<!-- 
Template Name: Metronic - Responsive Admin Dashboard Template build with Twitter Bootstrap 3.3.4
Version: 3.9.0
Author: KeenThemes
Website: http://www.keenthemes.com/
Contact: support@keenthemes.com
Follow: www.twitter.com/keenthemes
Like: www.facebook.com/keenthemes
Purchase: http://themeforest.net/item/metronic-responsive-admin-dashboard-template/4021469?ref=keenthemes
License: You must have a valid license purchased only from themeforest(the above link) in order to legally use the theme for your project.
-->
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8">
<link rel="shortcut icon" href="css/images/logo_new.png">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<link type="text/css" href="css/jquery.jscrollpane.css" rel="stylesheet" media="all" />
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet" type="text/css"></link>
<link href="css/customStyle.css" rel="stylesheet" type="text/css">
<!-- END THEME STYLES -->
<style>
/* .select2-drop-mask{
	z-index:15000 !important;
}
.select2-drop{
	z-index:12000 !important;
}
.daterangepicker {
	z-index:12000 !important;
} */
</style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body>
<!-- BEGIN HEADER -->
<div id="reportbox" style="display: none;"></div>
<div class="page-header">
	<!-- BEGIN HEADER TOP -->
	<div><%@include file="headerlayout.jsp" %></div>			
	<!-- END HEADER TOP -->
	<!-- BEGIN HEADER MENU -->
	<div class="page-header-menu">
		<div class="container container-position">			
			<!-- BEGIN MEGA MENU -->			
			 <div><%@include file="menu.jsp" %></div>			
 			<!-- END MEGA MENU -->
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->

<!-- BEGIN CONTAINER -->
<div class="page-container">
<div><%@include file="treeStructureSidebar.jsp" %></div>
<div><%@include file="postHeader.jsp" %></div>

<!-- BEGIN PAGE CONTENT -->
<div class="page-content">
		<div class="container-fluid">			
			<!-- BEGIN PAGE CONTENT INNER -->
			<div class="row margin-top-10" id="toAnimate">
				<div class="col-md-12">
					<!-- BEGIN PORTLET-->
					<div class="portlet light ">
						<div class="portlet-title toolbarFullScreen">
							<div class="caption caption-md">
								<i class="icon-bar-chart theme-font hide"></i>
 								<span class="caption-subject theme-font bold uppercase">Analyze Defects</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span> 
								<span class="caption-helper hide">weekly stats...</span>
							</div>
							<!-- <div class="actions">
									<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" onClick="fullScreenHanlder('fullscreen')" data-original-title="" title="FullScreen"></a>
							</div>  -->
						</div>
						<div class="portlet-body">
							<div class="row list-separated" style="margin-top:0px;">	
							
							<!-- jtable started -->
								<!-- Main -->
								    <div id="main" style="float:left; padding-top:0px; width:100%;">
								    <div id="hdnDiv"> </div>
								    			<!-- Filer -->
							    <div id="filter" class="col-md-12 toolbarFullScreen">
							    	<div class="row">							    	
							    		<div class="col-md-4"></div>
							    		<div class="col-md-2"><label style="padding-right: 10px;padding-top: 8px;">Issue ID Exist:</label></div>
							    		<div class="col-md-2"><label style="padding-right: 10px;padding-top: 8px;">Analyzed :</label></div>
							    		<div class="col-md-2"><label style="padding-right: 10px;padding-top: 8px;">Status :</label></div>
							    		
							    	</div>
							    	<div class="row">
							    		<div class="col-md-4">
							    			<div class="input-group" id="defaultrange" style="padding-right: 10px;">								
												<input type="text" class="form-control" id="fromTodatepicker" readonly="readonly" style="background-color: rgb(255, 255, 255);">
												<span class="input-group-btn">
												<button class="btn default date-range-toggle" type="button" style="height:34px"><i class="fa fa-calendar"></i></button>
												</span>
  											</div>
  										</div>
  										<div id="issue_dd" class="col-md-2">										
											<select class="form-control input-small select2me" id="issue_ul">
												<option value="2" selected><a href="#">All</a></option>
												<option value="1" ><a href="#">Yes</a></option>
												<option value="0" ><a href="#">No</a></option>
											</select>
										</div>
									<div id="analyse_dd" class="col-md-2">									
										<select class="form-control input-small select2me" id="analyse_ul">
											<option value="2" selected><a href="#">All</a></option>
											<option value="1" ><a href="#">Yes</a></option>
											<option value="0" ><a href="#">No</a></option>
										</select>
									</div>
									<div id="status_dd" class="col-md-2">									
										<select class="form-control input-small select2me" id="status_ul">
										</select>										
									</div>
									<div class="col-md-2">																																
										
									</div>
								</div>					    	
								</div>	
									    <!-- End Filter -->
								      <!-- Box -->
								      <div class="box">								        
								         <div id="hidden"></div>
								       	<!-- ContainerBox -->
								       	<div class="cl">&nbsp;</div>
								      	<div class="jScrollContainerResponsive jScrollContainerFullScreen" style="clear:both;padding-top:10px;position: relative"> 
								       		<div class="col-md-2 jScrollTitleContainer" style="position: absolute;Z-index: 10;margin-left: 155px">
								       			<label style="padding-right: 10px;padding-top: 8px;margin-left: 24px;font-size: 1.1em;font-family: sans-serif;color: #FFF;;">Upload :</label>
								       			<input style="margin-top: -46px; margin-left: 95px;height: 24px;padding-top: 3px;background-color: #55616f; font-size: 0.96em;" type="button" class="btn blue" id="trigUploadAnalyseDefects" value="Import Defects">
												<span id="fileNameAnalyseDefects"></span>
												<input id="uploadFileOfAnalyseDefects" type="file" name="uploadFileOfAnalyseDefects" style="display:none;" 
												onclick="{this.value = null;};" onchange="importAnalyseDefects()">
								       		</div> 
								      		<div id="jTableContainerDefects" class ="jTableContainerFullScreen"></div>
									   	</div>
										<!-- End Container Box -->
								 
										<div class="cl">&nbsp;</div>
								      </div>
								      <!-- End Box -->     
								    </div>
								    <!-- End Main -->
							<!-- jtable ended -->											
							</div>							
						</div>
					</div>
					<!-- END PORTLET-->
				</div>				
			</div>			
			
			<!-- END PAGE CONTENT INNER -->
		</div>
	</div>
<!-- END PAGE CONTENT -->
</div>
<!-- END PAGE-CONTAINER -->

<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp" %></div>			
<!-- END FOOTER -->
<!-- Popup -->
<div id="div_PopupAnalyseDefect" class="modal " tabindex="-1" aria-hidden="true">
	<div class="modal-dialog" style="width: 80%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupCloseAnalyseDefect()" aria-hidden="true"></button>
				<h4 class="modal-title">Analyse Defect</h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="width:80%;height:320px;" data-always-visible="1" data-rail-visible1="1">
				    <div class="col-md-12">
						<div class="portlet light">								
							<div class="portlet-body form">							
								<!-- ContainerBox -->
								<div class="jScrollContainerResponsive" style="clear:both;padding-top:10px">
									<div id="jTableContainerDefectsspe"></div>
									<div id="jTableAnalyseDefects"></div>
									
									
								</div>
								<!-- End Container Box -->							
							    <div class="form-actions" style="float:right;">						    
									<button type="button" id="saveButton" data-dismiss="modal" onclick="saveAnalyseDefect(0);popupCloseAnalyseDefect();" class="btn green-haze" >Save</button>
									<button type="button" data-dismiss="modal" class="btn grey-cascade" value="Cancel" onclick="popupCloseAnalyseDefect()">Cancel</button>
								</div>
							</div>
						</div>
						</div>
				    </div>				
				</div>
		</div>
	</div>
</div>
<div id="div_PopupMain" class="modal " tabindex="-1" aria-hidden="true" >
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupClose()" aria-hidden="true"></button>
				<h4 class="modal-title">Bugnizer Defects</h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:320px" data-always-visible="1" data-rail-visible1="1">
					<div class="jScrollContainerResponsive" style="clear:both;padding-top:10px">
						<div id="jTableDefectsforBugnizer"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>	

<div id="div_PopupBackground"></div>
<!-- Popup Ends -->	

<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/jquery.vmap.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.sparkline.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="files/lib/metronic/theme/assets/admin/layout/scripts/quick-sidebar.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jstree/dist/jstree.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->

<!-- the jScrollPane script -->
<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

<!-- Include jTable script file. -->
<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>

<!-- Plugs ins for jtable inline editing -->
<script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
<script type="text/javascript" src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>

<!-- Validation engine script file and english localization -->
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>

<script src="js/moment.min.js" type="text/javascript"></script>
<script src="js/daterangepicker.js" type="text/javascript"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>
<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/importData.js" type="text/javascript"></script>

<script type="text/javascript">
var urlToGetAnalyseDefects;
var productId;
var productVersionId;
var productBuildId;
var workpackageId;
bugIdVal=0;
jQuery(document).ready(function() {	
QuickSidebar.init(); // init quick sidebar
setBreadCrumb("Monitoring");
createHiddenFieldsForTree();
ComponentsPickers.init();
setPageTitle("Products");
getTreeData('administration.workpackage.testcase.review.tree');
	$('#filter').hide();
	$("#treeContainerDiv").on("select_node.jstree",
		     function(evt, data){
	   			var entityIdAndType =  data.node.data;
	   			var arry = entityIdAndType.split("~");
	   			 key = arry[0];
	   			var type = arry[1];
	   			var title = data.node.text;
				var date = new Date();
			    var timestamp = date.getTime();
			    nodeType = type;
			    var loMainSelected = data;
		        uiGetParents(loMainSelected);
		        $('#filter').show();
		        var workdateFrom = DaterangePicker.fromDate();
				var workdateTo = DaterangePicker.toDate();
				var issue = $("#issue_ul").find('option:selected').val();
				var analyse = $("#analyse_ul").find('option:selected').val();
				var id = $("#status_ul").find('option:selected').attr('id');
				id = (typeof id == 'undefined') ? -1 : id;
			    if(nodeType == "Product"){
			    	  productId = key;
			    	  urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+0+"&productBuildId="+0+"&workPackageId="+0+"&approveStatus="+id+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
			    	  listAnalyseDefects();
			    }
			    if(nodeType == "ProductVersion"){
			    	  productVersionId = key;
			    	  productId = document.getElementById("treeHdnCurrentProductId").value;
			    	  urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+0+"&workPackageId="+0+"&approveStatus="+id+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
			    	  listAnalyseDefects();
			    }
			    if(nodeType == "ProductBuild"){
					  productBuildId = key;
			    	  productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value;;
			    	  productId = document.getElementById("treeHdnCurrentProductId").value;
			    	  urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+productBuildId+"&workPackageId="+0+"&approveStatus="+id+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
			    	  listAnalyseDefects();
			    }
			    if(nodeType == "WorkPackage"){
			    	  workPackageId = key;
			    	  productBuildId = document.getElementById("treeHdnCurrentProductBuildId").value;;
			    	  productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value;;
			    	  productId = document.getElementById("treeHdnCurrentProductId").value;
			    	  urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+productBuildId+"&workPackageId="+workPackageId+"&approveStatus="+id+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
			    	  listAnalyseDefects();
			    }
			   	
			    loadFillingStatus();
		     }
		);
	
  	$(document).on("click",".daterangepicker .applyBtn, .daterangepicker .cancelBtn", function(e) {
    	var workdateFrom = DaterangePicker.fromDate();
		var workdateTo = DaterangePicker.toDate();
		var approveStatus = $("#status_ul").find('option:selected').attr('id');
		approveStatus = (typeof approveStatus == 'undefined') ? -1 : approveStatus;
		var issue = $("#issue_ul").find('option:selected').val();
		var analyse = $("#analyse_ul").find('option:selected').val();
		var date = new Date();
	    var timestamp = date.getTime();
		if(nodeType == "Product"){
		  	productId = key;
		  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+0+"&productBuildId="+0+"&workPackageId="+0+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
		  	listAnalyseDefects();
		  }
		  if(nodeType == "ProductVersion"){
		  	productVersionId = key;
		  	productId = document.getElementById("treeHdnCurrentProductId").value;
		  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+0+"&workPackageId="+0+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
		  	listAnalyseDefects();
		  }
		  if(nodeType == "ProductBuild"){
		  	productBuildId = key;
		  	productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value;
		  	productId = document.getElementById("treeHdnCurrentProductId").value;
		  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+productBuildId+"&workPackageId="+0+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
		  	listAnalyseDefects();
		  }
		  if(nodeType == "WorkPackage"){
		  	workPackageId = key;
		  	productBuildId = document.getElementById("treeHdnCurrentProductBuildId").value;
		  	productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value;
		  	productId = document.getElementById("treeHdnCurrentProductId").value;
		  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+productBuildId+"&workPackageId="+workPackageId+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
		  	listAnalyseDefects();
		  }
  	});
});

	var DaterangePicker = {
			fromDate : function(){
				$("#fromTodatepicker").daterangepicker();
				var datepickerStartDate = $(".daterangepicker_start_input>input").val();
				datepickerStartDate = ((typeof datepickerStartDate == "undefined") || (datepickerStartDate == ''))? new Date() : new Date(datepickerStartDate);
				datepickerStartDate = (datepickerStartDate.getMonth() + 1) + "/" + datepickerStartDate.getDate() + "/" + datepickerStartDate.getFullYear();
				return datepickerStartDate;
			},
			toDate : function(){
				$("#fromTodatepicker").daterangepicker();
				var datepickerEndDate = $(".daterangepicker_end_input>input").val();
				datepickerEndDate = ((typeof datepickerEndDate == "undefined") || (datepickerEndDate == '')) ? new Date() : new Date(datepickerEndDate);
				datepickerEndDate = (datepickerEndDate.getMonth() + 1) + "/" + datepickerEndDate.getDate() + "/" + datepickerEndDate.getFullYear();		
				return datepickerEndDate;
			}
	};
	


	$(document).on('change','#issue_ul', function() {
		var approveStatus = $("#status_ul").find('option:selected').attr('id');
		approveStatus = (typeof approveStatus == 'undefined') ? -1 : approveStatus;
		var issue = $("#issue_ul").find('option:selected').val();
		var analyse = $("#analyse_ul").find('option:selected').val();
		var date = new Date();
	    var timestamp = date.getTime();
	    selectedTab = $("#tabslist>li.active").index();
	    var workdateFrom = DaterangePicker.fromDate();
		var workdateTo = DaterangePicker.toDate();
		
	  if(nodeType == "Product"){
	  	productId = key;
	  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+0+"&productBuildId="+0+"&workPackageId="+0+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
	  	listAnalyseDefects();
	  }
	  if(nodeType == "ProductVersion"){
	  	productVersionId = key;
	  	productId = document.getElementById("treeHdnCurrentProductId").value;
	  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+0+"&workPackageId="+0+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
	  	listAnalyseDefects();
	  }
	  if(nodeType == "ProductBuild"){
	  	productBuildId = key;
	  	productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value;;
	  	productId = document.getElementById("treeHdnCurrentProductId").value;
	  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+productBuildId+"&workPackageId="+0+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
	  	listAnalyseDefects();
	  }
	  if(nodeType == "WorkPackage"){
	  	workPackageId = key;
	  	productBuildId = document.getElementById("treeHdnCurrentProductBuildId").value;;
	  	productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value;;
	  	productId = document.getElementById("treeHdnCurrentProductId").value;
	  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+productBuildId+"&workPackageId="+workPackageId+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
	  	listAnalyseDefects();
	  }
	    
	 });
	 
	 $(document).on('change','#analyse_ul', function() {
		var approveStatus = $("#status_ul").find('option:selected').attr('id');
		approveStatus = (typeof approveStatus == 'undefined') ? -1 : approveStatus;
		var issue = $("#issue_ul").find('option:selected').val();
		var analyse = $("#analyse_ul").find('option:selected').val();
		var date = new Date();
	    var timestamp = date.getTime();
	    selectedTab = $("#tabslist>li.active").index();
	    var workdateFrom = DaterangePicker.fromDate();
		var workdateTo = DaterangePicker.toDate();
		
	  if(nodeType == "Product"){
	  	productId = key;
	  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+0+"&productBuildId="+0+"&workPackageId="+0+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
	  	listAnalyseDefects();
	  }
	  if(nodeType == "ProductVersion"){
	  	productVersionId = key;
	  	productId = document.getElementById("treeHdnCurrentProductId").value;
	  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+0+"&workPackageId="+0+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
	  	listAnalyseDefects();
	  }
	  if(nodeType == "ProductBuild"){
	  	productBuildId = key;
	  	productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value;;
	  	productId = document.getElementById("treeHdnCurrentProductId").value;
	  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+productBuildId+"&workPackageId="+0+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
	  	listAnalyseDefects();
	  }
	  if(nodeType == "WorkPackage"){
	  	workPackageId = key;
	  	productBuildId = document.getElementById("treeHdnCurrentProductBuildId").value;;
	  	productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value;;
	  	productId = document.getElementById("treeHdnCurrentProductId").value;
	  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+productBuildId+"&workPackageId="+workPackageId+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
	  	listAnalyseDefects();
	  }
	    
	 });
	
	 
	 $(document).on('change','#status_ul', function() {
			var approveStatus = $("#status_ul").find('option:selected').attr('id');
			approveStatus = (typeof approveStatus == 'undefined') ? -1 : approveStatus;
			var issue = $("#issue_ul").find('option:selected').val();
			var analyse = $("#analyse_ul").find('option:selected').val();
			var date = new Date();
		    var timestamp = date.getTime();
		    selectedTab = $("#tabslist>li.active").index();
		    var workdateFrom = DaterangePicker.fromDate();
			var workdateTo = DaterangePicker.toDate();
			
		  if(nodeType == "Product"){
		  	productId = key;
		  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+0+"&productBuildId="+0+"&workPackageId="+0+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
		  	listAnalyseDefects();
		  }
		  if(nodeType == "ProductVersion"){
		  	productVersionId = key;
		  	productId = document.getElementById("treeHdnCurrentProductId").value;
		  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+0+"&workPackageId="+0+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
		  	listAnalyseDefects();
		  }
		  if(nodeType == "ProductBuild"){
		  	productBuildId = key;
		  	productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value;;
		  	productId = document.getElementById("treeHdnCurrentProductId").value;
		  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+productBuildId+"&workPackageId="+0+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
		  	listAnalyseDefects();
		  }
		  if(nodeType == "WorkPackage"){
		  	workPackageId = key;
		  	productBuildId = document.getElementById("treeHdnCurrentProductBuildId").value;;
		  	productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value;;
		  	productId = document.getElementById("treeHdnCurrentProductId").value;
		  	urlToGetAnalyseDefects = 'list.defects.for.analyse?productId='+productId+"&productVersionId="+productVersionId+"&productBuildId="+productBuildId+"&workPackageId="+workPackageId+"&approveStatus="+approveStatus+"&timeStamp="+timestamp+'&startDate='+workdateFrom+'&endDate='+workdateTo+'&issueStatus='+issue+'&analyseStatus='+analyse;
		  	listAnalyseDefects();
		  }
		    
		 });


function listAnalyseDefects(){
	try{
		if ($('#jTableContainerDefects').length>0) {
			 $('#jTableContainerDefects').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainerDefects').jtable({
	   title: 'Defect Details',
	    editinline:{enable:true},
	    selecting: true,  //Enable selecting 
	    paging: true, //Enable paging
	    pageSize: 10, 
	    
	    toolbar : {
	  		items : [
	    		  {
	    				text : 'Bugnizer Data',
	    				click : function() {
	    					showBugnizerData();
	    				}
	    			}
	  			 ]    			
	  		},
	    
	     actions: {
	  	 	listAction: urlToGetAnalyseDefects,
	  	 	editinlineAction: 'defects.for.analyse.update'
	   	},  
	    fields : {
	    	testExecutionResultBugId: { 
	    		title: 'Bug ID',  
	    		key: true,
	            list: false,
	            create: false
	    	},
	    	bugManagementSystemBugId: { 
	    		title: 'Issue ID',  
	    		width: "7%",                          
	            create: true,
	            list:true,
	            edit:true
	    	},
	    	defectTypeName: { 
	    		title: 'Type',  
	    		width: "7%",                          
	            create: true,
	            list:true,
	            edit:false 
	    	},
    		bugCreationTime: { 
	    		title: 'Creation Time',
	    		width: "7%",         
	    		create: false,
	            list:true,
	            edit:false							                
	        },
	        reportedBy: { 
	    		title: 'Reported By',  
	    		width: "7%",                          
	            create: true,
	            list:true,
	            edit:false 
	    	},
	    	bugTitle: { 
	    		title: 'Bug Title',  
	    		width: "7%",                          
	            create: true,
	            list:true,
	            edit:false 
	    	},
	    	analysedFlag: { 
	    		title: 'Analyzed',  
	            create: false,
	            list:true,
	            edit:false,
	            type : 'checkbox',
				 values: {'0' : 'No','1' : 'Yes'},
	    	},	    	
	        analyseDefect:{
				title : 'Analyze',
				list : true,
				edit : false,
				width: "10%",
				display:function (data) { 
					var $img = $('<i class="fa fa-copy" title="Analyse"></i>');
					//Open Build Clone pop up
					$img.click(function () {
						analyseDefectsData(data.record.testExecutionResultBugId); 			           				
				  });
				  return $img;
				}
			},
	    },  // This is for closing $img.click(function (data) {  
	      
		});
	 $('#jTableContainerDefects').jtable('load');
		
	}
	
function showBugnizerData() {
	
	//Destroy the current jtable so that it is forced to reload with the new workpackage id
	try{
		if ($('#jTableDefectsforBugnizer').length>0) {
			 $('#jTableDefectsforBugnizer').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableDefectsforBugnizer').jtable({
	   title: 'Bugnizer Data',
	    editinline:{enable:true},
	    selecting: true,  //Enable selecting 
	    paging: true, //Enable paging
	    pageSize: 10,
	     actions: {
	  	 	listAction: "defects.analyse.from.data.list?&executionResultBugId=-1&action=0",		  	 	
	   	},  
	    fields : {
	    	testExecutionResultBugId: { 
	    		title: 'Bug ID',  
	    		key: true,
	            list: false,
	            create: false
	    	},
	    	bugManagementSystemBugId: { 
	    		title: 'Issue ID',  
	    		width: "7%",                          
	            create: true,
	            list:true,
	            edit:false
	    	},
	    	defectTypeName: { 
	    		title: 'Type',  
	    		width: "7%",                          
	            create: true,
	            list:true,
	            edit:false 
	    	},
    		bugCreationTime: { 
	    		title: 'Creation Time',
	    		width: "7%",         
	    		create: false,
	            list:true,
	            edit:false							                
	        },
	        reportedBy: { 
	    		title: 'Reported By',  
	    		width: "7%",                          
	            create: true,
	            list:true,
	            edit:false 
	    	},
	    	bugTitle: { 
	    		title: 'Bug Title',  
	            create: true,
	            list:true,
	            edit:false 
	    	},
	        
	    },  
		
		
		// This is for closing $img.click(function (data) {  
	      
		});
	 $('#jTableDefectsforBugnizer').jtable('load');
 
 $("#div_PopupMain").modal();
	
}
	
	
function listAnalyseDefectsSpecific(bugid){
try{
	if ($('#jTableContainerDefectsspe').length>0) {
		 $('#jTableContainerDefectsspe').jtable('destroy'); 
	}
} catch(e) {}

urlToGetAnalyseDefectsSpecific = 'list.defects.for.analyse.using.testCaseExecutionResultId?bugId='+bugid;
$('#jTableContainerDefectsspe').jtable({
   title: 'Defect Detail',
    editinline:{enable:true},
    selecting: true,  //Enable selecting 
    paging: false, //Enable paging
    pageSize: 10, 
     actions: {
  	 	listAction: urlToGetAnalyseDefectsSpecific,
   	},  
    fields : {
    	testExecutionResultBugId: { 
    		title: 'Bug Id',  
    		key: true,
            list: false,
            create: false
    	},
    	defectTypeName: { 
    		title: 'Type',  
    		width: "7%",                          
            create: true,
            list:true,
            edit:false 
    	},
		bugCreationTime: { 
    		title: 'Creation Time',
    		width: "7%",         
    		create: false,
            list:true,
            edit:false							                
        },
        reportedBy: { 
    		title: 'Reported By',  
    		width: "7%",                          
            create: true,
            list:true,
            edit:false 
    	},
    	bugTitle: { 
    		title: 'Bug Title',  
    		width: "7%",                          
            create: true,
            list:true,
            edit:false 
    	},
    	analysedFlag: { 
    		title: 'Analyzed',  
            create: false,
            list:true,
            edit:false,
            type : 'checkbox',
			 values: {'0' : 'No','1' : 'Yes'},
		   },	    	
    },  // This is for closing $img.click(function (data) {  
      
	});
 $('#jTableContainerDefectsspe').jtable('load');
 $("#div_PopupAnalyseDefect").modal();
 
}

	
function analyseDefectsData(bugid){
	bugIdVal=bugid;
	//Destroy the current jtable so that it is forced to reload with the new workpackage id
		try{
			if ($('#jTableAnalyseDefects').length>0) {
				 $('#jTableAnalyseDefects').jtable('destroy'); 
			}
		} catch(e) {}
		listAnalyseDefectsSpecific(bugid);
		$('#jTableAnalyseDefects').jtable({
		   title: 'Analyze Defects',
		    editinline:{enable:true},
		    selecting: true,  //Enable selecting 
		    paging: true, //Enable paging
		    selectingCheckboxes: true, //Show checkboxes on first column
		    multiselect: false,
		    pageSize: 10,

			
		     actions: {
		  	 	listAction: "defects.analyse.from.data.list?&executionResultBugId="+bugid+"&action=1",		  	 	
		   	},  
		    fields : {
		    	testExecutionResultBugId: { 
		    		title: 'Bug ID',  
		    		key: true,
		            list: false,
		            create: false
		    	},
		    	bugManagementSystemBugId: { 
		    		title: 'Issue ID',  
		    		width: "7%",                          
		            create: true,
		            list:true,
		            edit:false
		    	},
		    	defectTypeName: { 
		    		title: 'Type',  
		    		width: "7%",                          
		            create: true,
		            list:true,
		            edit:false 
		    	},
	    		bugCreationTime: { 
		    		title: 'Creation Time',
		    		width: "7%",         
		    		create: false,
		            list:true,
		            edit:false							                
		        },
		        reportedBy: { 
		    		title: 'Reported By',  
		    		width: "7%",                          
		            create: true,
		            list:true,
		            edit:false 
		    	},
		    	bugTitle: { 
		    		title: 'Bug Title',  
		            create: true,
		            list:true,
		            edit:false 
		    	},
		        
		    },  
			
			selectionChanged:function(event,data){
				 var $selectedRows = $('#jTableAnalyseDefects').jtable('selectedRows');
				 if($selectedRows.length>0){
					 $("#saveButton").attr("onclick","saveAnalyseDefect(1);");
				 }else{
					 $("#saveButton").attr("onclick","saveAnalyseDefect(0);");
				 }
				
			},
			recordsLoaded:function(event,data){
				$("#saveButton").attr("onclick","saveAnalyseDefect(0);");
			}
			
			// This is for closing $img.click(function (data) {  
		      
			});
		 $('#jTableAnalyseDefects').jtable('load');

	 
	 $("#div_PopupAnalyseDefect").modal();
}

function saveAnalyseDefect(id){
	var urlMapping = "defects.analyse.from.data.update?action="+id+"&testBugId="+bugIdVal;
	 $.ajax({
         type: "POST",
   		contentType: "application/json; charset=utf-8",
         url : urlMapping,
         dataType : 'json',
         success : function(data) {
        	 	callAlert("Defect has been Analysed");
         }
  });
}

/* Pop Up close function */
function popupClose() {
	$("#div_PopupMain").fadeOut("normal");
	$("#div_PopupBackground").fadeOut("normal");
}

function popupCloseAnalyseDefect() {	
	$("#div_PopupAnalyseDefect").fadeOut("normal");
	listAnalyseDefects();
}

/* Load Poup function */
function loadPopup(divId) {
	$("#" + divId).fadeIn(0500); // fadein popup div
	$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
														// IE7, IE8
	$("#div_PopupBackground").fadeIn(0001);
}

document.onkeydown = function(evt) {
	evt = evt || window.event;
	if (evt.keyCode == 27) {
		if (document.getElementById("div_PopupMain").style.display == 'block') {
			popupClose();
		}
	}
};


function loadFillingStatus(){		
	$('#status_ul').empty();				
	$.post('administration.workFlow.list?entityType=1',function(data) {	
        var ary = (data.Options);
     	$('#status_ul').append('<option id="-1"><a href="#">All</a></option>');
        $.each(ary, function(index, element) {
    	$('#status_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
   });
   $('#status_ul').select2();	        
	});
}



</script>

<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>