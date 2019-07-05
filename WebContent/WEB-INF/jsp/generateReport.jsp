<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@page import="com.hcl.atf.taf.controller.HomePageController"%>
<%@page import="com.hcl.atf.taf.model.UserList"%>
<%@page import="java.util.List"%>
<%@page import="com.hcl.atf.taf.model.TestRunList"%>

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
<link href="css/customStyle.css" rel="stylesheet" type="text/css">

<link rel="stylesheet" href="css/jquery.ui.timepicker.css?v=0.3.2" type="text/css" />
<link rel="stylesheet" href="js/datatable/jquery.dataTables.css" type="text/css" media="all">
<link rel="stylesheet" href="js/datatable/dataTables.tableTools.css" type="text/css" media="all">
<link rel="stylesheet" href="css/customizeDataTable.css" type="text/css" media="all">
<link rel="stylesheet" href="css/jquery.alerts.css" type="text/css" media="all">
<link rel="stylesheet" href="css/iosOverlay.css" type="text/css" media="all">

<!-- END THEME STYLES -->
<link href="css/bootstrap-select.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet" type="text/css"></link>
<link href="css/customStyle.css" rel="stylesheet" type="text/css"></link>
<link rel="stylesheet" href="js/datatable/dataTables.jqueryui.min.css" type="text/javascript" media="all">
<style type="text/css">
th, td {
    white-space: nowrap;
}
.col-md-1,.col-md-4{
	padding-right: 5px !important;
	padding-left: 0px !important;
}
</style>
</head>
<body>
<input type="hidden" id="hdnDateSet" value="no"/>
<!-- BEGIN HEADER -->
<div class="page-header">
	<!-- BEGIN HEADER TOP -->
	<div><%@include file="headerlayout.jsp" %></div>	
	<div class="page-header-menu">
		<div class="container container-position">			
			 <div><%@include file="menu.jsp" %></div> 			
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->

<!-- BEGIN CONTAINER -->
<div class="page-container">
<div><%@include file="postHeader.jsp" %></div>

<!-- BEGIN PAGE CONTENT -->
<div id="reportbox" style="display: none;"></div>
<div class="page-content">
		<div class="container-fluid">			
			<!-- BEGIN PAGE CONTENT INNER -->
			<div class="row margin-top-10" id="toAnimate">
				<div class="col-md-12">
					<!-- BEGIN PORTLET-->
					<div class="portlet light ">
					    <div class="portlet-title">
							<div class="caption caption-md">
								<i class="icon-bar-chart theme-font hide"></i>
 								<span class="caption-subject theme-font bold uppercase">Task Effort Report</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
								<span class="caption-helper hide">weekly stats...</span>
							</div>
							<img src="css/images/generatereport.png" title="Create New Report" class="icon_imgSize showHandCursor" 
							alt="GenerateReport" onclick="generateNewReport();" style="width: 30px;height: 30px;float:right;margin: 5px 10px 0px 0px;"> 
						</div>
						<div class="portlet-body">
							<div class="row list-separated" style="margin-top:0px;">	
							<!-- jtable started -->
								<!-- Main -->
								    <div id="main" style="float:left; padding-top:0px; width:100%;">
							<div class="col-md-1">Reports :</div>  
							<div id="reportList_dd">									
								<select class="form-control input-medium select2me" id="reportList_ul">
								</select>										
							</div>
							<div id="generateReportFormID" class="row" style="display:none;border:2px solid lightgrey;padding: 10px 0 10px 0;margin-top: 15px">
								<div class="col-md-4" style="padding: 0px;">
									
									<div class="row" style="padding-top: 10px;">
										<div class="col-md-3">Product :</div>  
											<div id="groupReport_dd">									
											  <!-- <select class="form-control input-medium select2me" id="groupByList_ul">
												</select> -->

												<!-- <div id="testRunPlan_list" class="icheck-list">	</div>	 -->
												
											  <div id="groupByList_ul" style="height : 120px;overflow: auto;"></div>
											</div> 
									</div>
									<!-- <div class="row" style="padding-top: 10px;">	
										<div class="col-md-3">Status :</div>  
											<div id="statusReport_dd">									
												<select class="form-control input-medium select2me" id="status_ul">
												</select>										
											</div>
									</div> -->
									<div class="row" style="padding-top: 10px;">	
										<div class="col-md-3">Resource :</div>  
											<div id="resourceReport_dd">									
												<!-- <select class="form-control input-medium select2me" id="resource_ul"></select> -->
												  
												  <div id="resource_ul" style="height : 120px;overflow: auto;"></div>
																						
											</div>
									</div>
									<!-- <div class="row" style="padding-top: 10px;">	
										<button id="fieldMappingID" class="btn blue" onclick="fieldMappingHandler()">Field Mapping</button>
									</div> -->
								</div>
								<div class="col-md-8">
									<div class="row" style="padding-top: 10px;">
										<div class="col-md-2">Name :</div> 
										<div><input style="margin-left: 150px;" id="templateReport_name" class="form-control input-medium" type="text" name="report_name"></div> 
									</div>
									<div class="row" style="padding-top: 10px;">	
									<div class="col-md-2" style="padding-top: 10px;"> Date :</div>  
									<div class="col-md-6">
										<div class="input-group" id="planned_defaultrange" >								
											<input type="text" class="form-control" id="planned_fromTodatepicker" readonly style="background-color:white"/>
											<span class="input-group-btn">
											<button class="btn default date-range-toggle" type="button" style="height:34px"><i class="fa fa-calendar"></i></button>
											</span>
										</div>
									</div>
								</div>
									
									<div class="row" style="padding-top: 10px;">
										<div class="col-md-2" style="padding-top: 10px;">Field Mapping :</div>  		
										<div class="col-md-4" style="height : 120px;overflow: auto;">
											<!--  <div><label class="control-label "><input type="checkbox" name="mapFields" id="select_all" value="0" onchange="checkAll(this)"/>SELECT ALL</label></div>-->
											<div><label class="control-label "><input type="checkbox" name="mapFields" value="Product" />Product</label></div>	
											<div><label class="control-label "><input type="checkbox" name="mapFields" value="Workpackage"/>Workpackage</label></div>	
											<div><label class="control-label "><input type="checkbox" name="mapFields" value="Activity"/>Activity</label></div>	
											<div><label class="control-label "><input type="checkbox" name="mapFields" value="Task"/>Task</label></div>	
											<div><label class="control-label "><input type="checkbox" name="mapFields" value="Resource"/>Resource</label></div>
											<div><label class="control-label "><input type="checkbox" name="mapFields" value="Status"/>Status</label></div>
											<div><label class="control-label "><input type="checkbox" name="mapFields" value="Effort"/>Effort</label></div>	
											<div><label class="control-label"><input type="checkbox"  name="mapFields" value="Actual Start Date"/>Actual Start Date</label></div>
											<div><label class="control-label"><input type="checkbox"  name="mapFields" value="Actual End Date"/>Actual End Date</label></div>									
											<div><label class="control-label"><input type="checkbox"  name="mapFields" value="Planned Start Date"/>Planned Start Date</label></div>
											<div><label class="control-label"><input type="checkbox"  name="mapFields" value="Planned End Date"/>Planned End Date</label></div>
											<div><label class="control-label"><input type="checkbox"  name="mapFields" value="Comments"/>Comments</label></div>
											<!-- <div><label class="control-label"><input type="checkbox"  value="9"/>wpk1cr</label></div>
											<div><label class="control-label"><input type="checkbox"  value="9"/>wpk1cr</label></div> -->
											
											<!-- <div class="form-actions" style="padding-top: 10px;">-->
												<!--<button type="button"  onclick="changeUserProfile();popupCloserolechange();" class="btn green-haze" >Submit</button>-->
												<!-- <button type="button" onclick="submitRadioBtnHandler();" class="btn green-haze" style="padding: 6px 12px;">Submit</button>
												<button type="button" class="btn grey-cascade" onclick="popupCloserolechange()">Cancel</button>-->
											<!--</div> -->
										
										</div>
										
								 	</div> 
								</div>
								<div id="saveReportID" class="row" style ="padding-top: 20px;clear:both">
									<div class="col-md-4"></div>
										<div class="col-md-4">
											<button class="btn green-haze" onclick="saveReport()">Save & Generate</button>
																		
											<button class="btn gray" onclick="cancelReport()">Cancel</button>
										</div>
								</div>
						 	</div>										
							
						
						
					<div id="hidden"></div>

								
				    <!-- End Main -->
				<!-- jtable ended -->											
							</div>							
						</div>
						<div class="row list-separated" style="margin-top:0px;">	
							<!-- jtable started -->
								<!-- Main -->
								    <div id="main" style="float:left; padding-top:0px; width:100%;">
							
					<div id="hidden"></div>

					<!-- ContainerBox -->
				 	<div id="taskEffortReportDiv">
					 	<!-- <div id="jTableContainer"></div> -->  
						<table id="taskEffortReportTable"   class="display" cellspacing="0" width="100%">
							<thead>
							<tr></tr>
							</thead>
						</table>					
						<div class="cl">&nbsp;</div>		
					</div>					
						<!-- End Container Box -->					
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
</div>
<!-- END PAGE CONTENT -->
</div>
<!-- END PAGE-CONTAINER -->

<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp" %></div>			
<!-- END FOOTER -->
<input type="hidden" id="hdnDateSet" value="no"/>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/jquery.vmap.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.sparkline.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="files/lib/metronic/theme/assets/admin/layout/scripts/quick-sidebar.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jstree/dist/jstree.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->

<script src="js/moment.min.js" type="text/javascript"></script>
<script src="js/daterangepicker.js" type="text/javascript"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>

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

<!-- Calender -->
<script type="text/javascript" src="js/jquery.ui.timepicker.js?v=0.3.2"></script>

<!-- datatable -->
<!-- <script type="text/javascript" src="js/datatable/jquery-1.11.1.min.js"></script> -->
<script type="text/javascript" src="js/datatable/jquery.dataTables.min.js"></script>
<script src="js/datatable/dataTables.jqueryui.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/datatable/jquery.dataTables.columnFilter.js"></script>
<script type="text/javascript" src="js/datatable/dataTables.fixedHeader.js"></script>
<script type="text/javascript" src="js/datatable/dataTables.tableTools.js"></script>

<script type="text/javascript">
 var templateId;
 var templateSelectedId;
jQuery(document).ready(function() {	
  
   ComponentsPickers.init();
   setBreadCrumb("Reports");
   loadReportTemplates();
	
	$('#reportList_ul').change(function(){
		generateTaskEffortReport();
	}); 
  
});

var SOURCE;
var treeData;
var workPackageId = "";
var shiftId = "";
var urlToGetTaskTemplateReport = "";
var DaterangePicker = {
		fromDate : function(){
			//$("#fromTodatepicker").daterangepicker();
			var datepickerStartDate = $(".daterangepicker_start_input>input").val();
			datepickerStartDate = ((typeof datepickerStartDate == "undefined") || (datepickerStartDate == ''))? new Date() : new Date(datepickerStartDate);
			datepickerStartDate = (datepickerStartDate.getMonth() + 1) + "/" + datepickerStartDate.getDate() + "/" + datepickerStartDate.getFullYear();
			return datepickerStartDate;
		},
		toDate : function(){
			//$("#fromTodatepicker").daterangepicker();
			var datepickerEndDate = $(".daterangepicker_end_input>input").val();
			datepickerEndDate = ((typeof datepickerEndDate == "undefined") || (datepickerEndDate == '')) ? new Date() : new Date(datepickerEndDate);
			datepickerEndDate = (datepickerEndDate.getMonth() + 1) + "/" + datepickerEndDate.getDate() + "/" + datepickerEndDate.getFullYear();		
			return datepickerEndDate;
		}
	} 
	
$("#taskEffortReportTable tfoot input" ).on( 'keyup change', function () {
    that
    	.search( this.value, true, false )
        .draw();
} );

function generateTaskEffortReport(){
	try{
		if ($('#taskEffortReportTable').length>0) {
			$("#taskEffortReportDiv").children(":last").remove();
			$('#taskEffortReportTable').remove();
			var htmlTable = '<table id="taskEffortReportTable"><thead><tr></tr></thead></table>';
			$("#taskEffortReportDiv").append(htmlTable);
		}
	} catch(e) {}
	
 	templateId = $("#reportList_ul").find('option:selected').attr('id');
	templateId = (typeof templateId == 'undefined') ? -1 : templateId;
	templateSelectedId=templateId;
	urlToGetTaskTemplateReport = 'report.activity.task.effort.filters?templateId='+templateSelectedId;
	 
	 var scrollValue = "";
	 if(window.innerWidth < 1600){
		 scrollValue = "250px";
	 }
	 if(window.innerWidth > 1600){
		 scrollValue = "500px";
	 }
		openLoaderIcon(); 
		$.ajax(
		{
		    type: "POST",
		    url : urlToGetTaskTemplateReport,
		    cache:false,
		    success: function(data) {
		    	closeLoaderIcon();
		    	
				$("#taskEffortReportDiv").show();			
				var data1=eval(data);
		  		var cols = data1[0].COLUMNS;
		  		var columnData=data1[0].DATA;
		  		if(columnData == undefined){
		  			columnData = [];
		  		}
		  			  	
			   	 $('#taskEffortReportTable').dataTable({			   		
			   		paging: true,			
					destroy: true,
					bJQueryUI: false,
					"scrollX":true,
					"scrollY":"100%",
					dom: 'T<"clear">lfrtip',
					 tableTools: {
				            //"sSwfPath": "/tfwp/js/swf/copy_csv_xls_pdf.swf"
				            "sSwfPath": "//cdn.datatables.net/tabletools/2.2.2/swf/copy_csv_xls_pdf.swf"
				        },
					 oTableTools: {
				            "aButtons": [
				                "copy",
				                "print",
				                {
				                    "sExtends":    "collection",
				                    "sButtonText": "Save",
				                    "aButtons":    [ "csv", "xls", "pdf" ]
				                }
				            ]
				        },
			   		        "bDeferRender": true,
			   		        "bInfo" : false,
			   		        "bSort" : false,
			   		        "bDestroy" : true,
			   		        "bFilter" : false,
			                 /* "fnRowCallback": function( nRow, aData, iDisplayIndex) {
			                	$.each(aData, function(i,item){
			                		
			                	});	                
			                }, */ 
			   		        "bPagination" : false,
			   		        "aaData": columnData,
			   		        "aoColumns": cols
			   		    });	   			
		  						
				 	},
		 		    error : function(data){					
		 				closeLoaderIcon();
		 			},
		 			complete : function(data){				
		 				closeLoaderIcon();
		 			}
				}
			);
			
}

function stopPropagation(evt) {
	if (evt.stopPropagation !== undefined) {
		evt.stopPropagation();
	} else {
		evt.cancelBubble = true;
	}
}

function removeElement() {
	$.each($('#taskEffortReportTable > tfoot>tr>th'), function(index, item){
		if($(item).attr("colspan")>1) $(this).children().remove();
	});
}


function loadReportTemplates(){		
	$('#reportList_ul').empty();				
	$.post('task.effort.templates.list',function(data) {	
        var ary = (data.Options);
        $.each(ary, function(index, element) {
    		$('#reportList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
  		 });
   		$('#reportList_ul').select2();	
   		
   		templateId = $("#reportList_ul").select2().find('option:selected').attr('id');
   		generateTaskEffortReport();
   		templateSelectedId= parseInt(templateId);
   		console.log( templateSelectedId);
   		
   		urlToGetTaskTemplateReport = 'report.activity.task.effort.filters?templateId='+templateSelectedId;
   		console.log(urlToGetTaskTemplateReport);
   				
   		//    	$('#reportList_ul').select2().val();	       
	});
}


</script>						
<!-- END JAVASCRIPTS -->
<script src="js/viewScript/generateReport.js" type="text/javascript"></script>
<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
</body>
<!-- END BODY -->
</html>