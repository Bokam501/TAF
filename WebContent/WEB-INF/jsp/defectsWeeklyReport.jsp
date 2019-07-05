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

<!-- BEGIN GLOBAL MANDATORY STYLES -->

<link type="text/css" href="css/jquery.jscrollpane.css" rel="stylesheet" media="all" />
<!-- <link rel="stylesheet" href="css/style.css" type="text/css" media="all"> -->
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<!-- <link href="css/customStyle.css" rel="stylesheet" type="text/css"> -->

<link href="css/bootstrap-select.min.css" rel="stylesheet" type="text/css"></link>
<link rel="stylesheet" type="text/css" href="css/bootstrap-datetimepicker.min.css"></link>
<!-- END THEME STYLES -->

<!-- BEGIN HORIZONTAL CALENDAR -->
<link rel="stylesheet" type="text/css" media="screen" href="js/calendar/calendarstyle.css" />
<link rel="stylesheet" href="js/Scripts/DateTimePicker/anytime.css" />
<link rel="stylesheet" type="text/css" media="screen" href="js/calendar/includes/shadowbox-3.0.3/shadowbox.css" />
<link rel="stylesheet" type="text/css" media="screen" href="js/calendar/includes/jquery-minicolors/jquery.miniColors.css" />
<!-- END HORIZONTAL CALENDAR -->

<link rel="stylesheet" href="js/datatable/jquery.dataTables.css" type="text/css" media="all">
<link rel="stylesheet" href="js/datatable/dataTables.tableTools.css" type="text/css" media="all">
<link rel="stylesheet" href="css/customizeDataTable.css" type="text/css" media="all">
<link rel="stylesheet" href="https://code.jquery.com/ui/1.11.3/themes/smoothness/jquery-ui.css" type="text/css" media="all"> 
	
<link rel="stylesheet" href="js/Scripts/star-rating/jquery.rating.css" type="text/css">
<link rel="stylesheet" href="css/iosOverlay.css" type="text/css" media="all">
<link href="css/customStyle.css" rel="stylesheet" type="text/css"> 
<link rel="stylesheet" href="js/datatable/jquery-ui.css" type="text/javascript" media="all">

<!-- <link href="files/lib/metronic/theme/assets/admin/layout3/css/layout.css" rel="stylesheet" type="text/css"> -->
<link rel="stylesheet" href="js/datatable/dataTables.jqueryui.min.css" type="text/javascript" media="all">

</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-header-menu-fixed" class to set the mega menu fixed  -->
<!-- DOC: Apply "page-header-top-fixed" class to set the top menu fixed  -->
<body>

<!-- <div id="header"></div> -->
<div id="reportbox" style="display: none;"></div>
<!-- BEGIN HEADER -->
<div class="page-header">
	<!-- BEGIN HEADER TOP -->
	<div><%@include file="headerlayout.jsp" %></div>			
	<!-- END HEADER TOP -->
	<!-- BEGIN HEADER MENU -->
	<div class="page-header-menu">
		<div class="container container-position">			
			<!-- BEGIN MEGA MENU -->
			<!-- DOC: Apply "hor-menu-light" class after the "hor-menu" class below to have a horizontal menu with white background -->
			<!-- DOC: Remove data-hover="dropdown" and data-close-others="true" attributes below to disable the dropdown opening on mouse hover -->
			 <div><%@include file="menu.jsp" %></div>			
 			<!-- END MEGA MENU -->
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->

<!-- BEGIN CONTAINER -->
<div class="page-container">
<%-- <div><%@include file="treeStructureSidebar.jsp"%></div> --%>
<div><%@include file="treeStructureSidebarLazyLoading.jsp" %></div>
<!-- BEGIN PAGE HEAD --> 
<div> <%@include file="postHeader.jsp" %></div>
<!-- END PAGE HEAD -->

<!-- BEGIN PAGE CONTENT -->
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
								<span class="caption-subject theme-font bold uppercase">Defects Weekly Report</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
							</div>
						</div>
						
						<div class="portlet-body form"  >
						<div class="row " >
							<div class="col-md-12">
					        <ul class="nav nav-tabs" id="tabslist">
								<!-- <li class="active"><a href="#TimeManagement" data-toggle="tab">My Shift Check-Ins and Time Bookings</a></li> -->
								<li class="active"><a href="#Availability" data-toggle="tab" >Defects</a></li>
							</ul>
							</div>																
						</div>
						<div id="hidden"></div>
						<div class="tab-content">
							<!-- TimeManagement Tab -->
							<%-- <div class="TimeManagement  tab-pane fade active in" id="TimeManagement" style="max-height:73%;overflow:hidden;overflow-y:auto;">
								<div style="margin:10px 0px 0px 0px;" class="col-md-12">
								<form action="#" class="form-horizontal form-row-sepe">
								<input type="hidden" id="hdnCurrentDate" value="">
								<ul style="display: -webkit-inline-box; list-style-type: none;">
									<li style="padding:7px">
									<label class="control-label"> My attendance & time data completeness, daywise over selected month</label>
										<div id="miniMonthCalendarContainer">
											<div id="monthManagement">
												<input type="button" id="prevYear" name="prevYear" value="«" title="Previous year">
												<input type="button" id="prevMonth" name="prevMonth" value="<" title="Previous month">
												<span id="currentMonth"></span>
												<input type="button" id="nextMonth" name="prevMonth" value=">" title="Next month">
												<input type="button" id="nextYear" name="nextYear" value="»" title="Next year">
											</div>
										<table id="miniMonthCalendar" onmouseup="highLightSelectedDate(event);">
											<tbody>
												<tr><td>Fr</td><td>Sa</td><td>Su</td><td>Mo</td><td>Tu</td><td>We</td><td>Th</td><td>Fr</td><td>Sa</td><td>Su</td><td>Mo</td><td>Tu</td><td>We</td><td>Th</td><td>Fr</td><td>Sa</td><td>Su</td><td>Mo</td><td>Tu</td><td>We</td><td>Th</td><td>Fr</td><td>Sa</td><td>Su</td><td>Mo</td><td>Tu</td><td>We</td><td>Th</td><td>Fr</td><td>Sa</td><td>Su</td></tr>
												<tr><td>01</td><td>02</td><td>03</td><td>04</td><td>05</td><td>06</td><td>07</td><td>08</td><td>09</td><td>10</td><td>11</td><td>12</td><td>13</td><td>14</td><td>15</td><td>16</td><td>17</td><td>18</td><td>19</td><td>20</td><td>21</td><td>22</td><td>23</td><td>24</td><td>25</td><td>26</td><td>27</td><td>28</td><td>29</td><td>30</td><td>31</td></tr>
											</tbody>
										</table>
									</div>
								</li>	
							 <li style="padding:0px">
							 	
							 	<div class="row">
							 		
							 		<label class="control-label col-md-6" style="text-align:left"><label style="width:5px;height:0;border:5px solid #4F9DF0;vertical-align:bottom"></label> Current/Selected Date: </label>
							 		<input type="text" class="form-control input-small col-md-5" id="crtSelectedDate" name="crtSelectedDate" value="" readonly />
								</div>
							 
								<div class="legend" style="margin-left:15px; margin-top:30px; padding:10px; border:1px solid lightgrey;">
<!-- 								<div style="position: absolute; width: 54px; height: 85px; top: 5px; right: 5px; opacity: 0.85; background-color: rgb(255, 255, 255);"> </div> -->
									<table>
										<tbody>
											<tr>
												<td class="legendColorBox">
													<div >
														<div style="width:5px;height:0;border:5px solid rgb(9, 113, 9);"></div>
													</div>
												</td>
												<td class="legendLabel"><label>Time data is complete</label></td>
												<td class="legendColorBox">
													<div >
														<div style="width:5px;height:0;border:5px solid rgb(255,64,0);"></div>
													</div>
												</td>
												<td class="legendLabel"><label>Time data is missing</label></td>
										    </tr>
										    <tr>
										    		<td class="legendColorBox">
													<div >
														<div style="width:5px;height:0;border:5px solid #f93;"></div>
													</div>
												</td>
												<td class="legendLabel"><label>Time data needs to be completed</label></td>
												<td class="legendColorBox">
													<div >
														<div style="width:5px;height:0;border:5px solid rgb(230,230,230);"></div>
													</div>
												</td>
												<td class="legendLabel"><label>Not present for day</label></td>
											</tr>
										</tbody>
									</table>

							</div>
							</li>		 	
										</ul>
								</form>
							</div>
								<div class="row list-separated" style="margin-top:0px;padding:10px">	
								<!-- jtable started -->
									        <div id="hidden"></div>
									        <!-- ContainerBox -->
<!-- 									      <div class="jScrollContainer">  -->
											<div class="jScrollContainerResponsiveTop" >
									      		<div id="jTableContainer"></div>
									      	</div>
											<div>
										      	<label >My time data completeness, shift-wise over the selected week</label>
										      	<div class="jScrollContainerResponsiveTop" >
													<div id="jTableContainerTimeSummary" ></div>
												</div>
											</div>
											<div >
												<label >Enter my shift check-in and timesheet entries, for selected day</label>
												<div class="jScrollContainerResponsiveTop" >
													<div id="jTableContainerResourceCheckInTimeBooking"></div>
												</div>
											</div>
											<div class="jScrollContainerResponsiveTop" >
												<div id="jTableTimeSheetEntryContainer"></div>
											</div>
<!-- 										 </div> -->
										<!-- End Container Box -->
										<div class="cl">&nbsp;</div>
									<!-- jtable ended -->											
								</div>
							</div> --%>
							<!-- Attendance Tab Ends -->
							<!-- Availability  -->
							<div id="Availability" class="tab-pane fade active in">
								<div class="cl">&nbsp;</div>
								<!-- ContainerBox -->
								 <input type="hidden" id="ssnHdnSelectedResourceId" value='<%=session.getAttribute("ssnHdnSelectedResourceId")%>' >					
								<input type="hidden" id="ssnHdnResourceAvailabilityWeekNo" value='<%=session.getAttribute("ssnHdnResourceAvailabilityWeekNo")%>' >
								<input type="hidden" id="hdnProductId" name="hdnProductId">
								<input type="hidden" id="hdnProductName" name="hdnProductName">
								<input type="hidden" id="hdnProductVersionId" name="hdnProductVersionId">
								<input type="hidden" id="hdnProductBuildId" name="hdnProductBuildId">  
								<input type="hidden" id="hdnWorkPackageId" name="hdnWorkPackageId">
								<br>
								<!-- <div class="jScrollContainerfilter">
									<div id="jTableContainerDefectsWeeklyView"></div>
									<br>
									<br>
									<div id="jTableContainerBooking"></div>
								</div> -->
								<div class="row" id="prevNextBtn" style="display:none">
									<div class="col-md-12">
										<a onclick="javascript:showDefectsOfPreviousWeek();" style="float:left">&lt&lt Previous Week</a>
										<a onclick="javascript:showDefectsOfNextWeek();" style="float:right">Next Week &gt&gt</a>
									</div>
								</div>
								<table id="defectsReportDataTable" class="display" cellspacing="0" width="100%" style="display:none">
        <thead>
			<tr>
				<th rowspan="2" id="appStatus"></th>
				<th colspan="2" id="totalRepCnt"></th>
				<th colspan="2" id="day1"></th>
				<th colspan="2" id="day2"></th>
				<th colspan="2" id="day3"></th>
				<th colspan="2" id="day4"></th>
				<th colspan="2" id="day5"></th>
				<th colspan="2" id="day6"></th>
				<th colspan="2" id="day7"></th>
				 
			</tr> 
			<tr>
				
				<th >Live</th>
				<th >Web</th>
				<th >Live</th>
				<th >Web</th>
				
				<th >Live</th>
				<th >Web</th>
				<th >Live</th>
				<th >Web</th>
				<th >Live</th>
				<th >Web</th>
				<th >Live</th>
				<th >Web</th>
				<th >Live</th>
				<th >Web</th>
				<th >Live</th>
				<th >Web</th>
			</tr>
		</thead>
 
 
		<tbody>
          
		</tbody>
</table>
								
							</div>
							<!-- Availability End -->
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
<input value="" id="hdnTimepicker_checkInAjax" type="hidden" />
<input value="" id="hdnTimepicker_checkOutAjax" type="hidden" />
<!-- popup -->
<div id="div_PopupMain" class="modal " tabindex="-1" aria-hidden="true" style="z-index:999">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupClose()" aria-hidden="true"></button>
				<h4 class="modal-title">My Shift Check-In</h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:320px" data-always-visible="1" data-rail-visible1="1">
					
				</div>
			</div>

		</div>
	</div>
</div>

<div id="div_PopupExportDefect" class="modal " tabindex="-1" aria-hidden="true" >
	<div class="modal-dialog modal-md"  style="width:35%">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close"  aria-hidden="true"></button>
				<h4 class="modal-title">Export Defect To Google Drive</h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:95px" data-always-visible="1" data-rail-visible1="1">
				    <div class="row">
				    	<div class="col-md-2"><label>URL :</label></div>
				    	<div class="col-md-8"><a href="" target='_blank' id="export_url"></a></div>
				    </div>				
				    <div class="row">
				    	<div class="col-md-2"><label>Key :</label></div>
				    	<div class="col-md-8"><input type="text" class="form-control input-med" id="export_key" /></div>
				    </div>				
				    <div class="row">
				    	<button type="button" class="btn btn-success" onclick="exportOption();" style="float:right">Export</button>
				    </div>				
				</div>
			</div>
		</div>
	</div>
</div>

    <div id="div_PopupBackground"></div>

<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/jquery.vmap.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.sparkline.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="files/lib/metronic/theme/assets/admin/layout/scripts/quick-sidebar.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jstree/dist/jstree.min.js" type="text/javascript"></script>
<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/bootstrap-datetimepicker.min.js"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>

<!-- END PAGE LEVEL SCRIPTS -->

<!-- the jScrollPane script -->
<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

<!-- Include jTable script file. -->
<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>
<!-- Datetimepicker -->
<script type="text/javascript" 	src="js/Scripts/DateTimePicker/anytime.js"></script>
<script type="text/javascript" 	src="js/Scripts/DateTimePicker/jquery.themeswitcher.js"></script>
<!-- End Datetimepicker -->

<!-- Plugs ins for jtable inline editing -->
<script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
<script type="text/javascript" src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>

<!-- Validation engine script file and english localization -->
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>

<!-- SHADOWBOX -->
	<script type='text/javascript' src='js/calendar/includes/shadowbox-3.0.3/shadowbox.js'></script>
	
	<!-- JQUERY - MINICOLORS -->
	<script type='text/javascript' src='js/calendar/includes/jquery-minicolors/jquery.miniColors.min.js'></script>

	<!-- DATES MANAGEMENT -->
<script type='text/javascript' src='js/calendar/includes/date.js'></script>
	
<script type='text/javascript' src='js/calendar/eventouchcalendar.min.js'></script>

 <script src="js/tab/jquery-ui.js"></script> 
 <script type="text/javascript" src="js/Scripts/jquery.popupwindow.js"></script>
<script src="js/datatable/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="js/datatable/dataTables.jqueryui.min.js" type="text/javascript"></script>

<script type="text/javascript" src="js/datatable/jquery.dataTables.columnFilter.js"></script>
<script type="text/javascript" src="js/datatable/dataTables.fixedHeader.js"></script>
<script type="text/javascript" src="js/datatable/dataTables.tableTools.js"></script>

<script type="text/javascript">
var  checkout=new Date();
var usercheckout=new Date();

function showWorkPackageTreeData(){	
	var jsonObj={"Title":"",				
	    	 	"urlToGetTree": 'administration.workpackage.plan.tree?type=2',
	    	 	"urlToGetChildData": 'administration.workpackage.plan.tree?type=2',	    	 	
	 };	 
	 TreeLazyLoading.init(jsonObj);	 
}

jQuery(document).ready(function() {	
  // $("#menuList li:first-child").eq(0).remove();
    QuickSidebar.init(); // init quick sidebar
   ComponentsPickers.init();
   setBreadCrumb("Defects Weekly Report");
   createHiddenFieldsForTree();
   setPageTitle("Products");
  // getTreeData('administration.workpackage.plan.tree?type=2');
    showWorkPackageTreeData();
   var selectedTab;
   resourceId='<%=session.getAttribute("ssnHdnUserId")%>';
   $("#treeContainerDiv").on("select_node.jstree",
		    function(evt, data){
		   		var entityIdAndType =  data.node.data;
				var arry = entityIdAndType.split("~");
				key = arry[0];
				var type = arry[1];
				title = data.node.text;
				currentNodeId = data.node.id;
		    	nodeType = type;
		    	var loMainSelected = data;
	       		uiGetParents(loMainSelected);
	       	//	$("#defectsReportDataTable, #prevNextBtn").hide();	
	       	 if(nodeType == "Product"){
	       		productId = key;
	       		productVersionId = 0;
	       		productBuildId = 0;
	       		workPackageId=0;
	       		setTreeDataInHiddenFieldData(productId, productVersionId, productBuildId,workPackageId);
	    		if(productId==null && productId==undefined){
	    			productId=0;
	    		}
	    		//showDefects();
	       	 }
	     	 
	     	 if(nodeType == "ProductVersion"){
	     			productVersionId = key;
		    		productId = document.getElementById("treeHdnCurrentProductId").value; 
		    		productBuildId = 0;
		       		workPackageId=0;
		       		setTreeDataInHiddenFieldData(productId, productVersionId, productBuildId,workPackageId);
		    		if(productVersionId==null && productVersionId==undefined){
						productVersionId=0;
					}
		    		//showDefects();
		       }
	     	 
	     	if(nodeType == "ProductBuild"){
	     		productBuildId = key;
     			productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value; 
	    		productId = document.getElementById("treeHdnCurrentProductId").value; 
	       		workPackageId=0;
	       		setTreeDataInHiddenFieldData(productId, productVersionId, productBuildId,workPackageId);
	    		if(productVersionId==null && productVersionId==undefined){
					productVersionId=0;
				}
	    		//showDefects();
	       }
       		
	     	
	     	if(nodeType == "WorkPackage"){
	     		workPackageId = key;
	     		productBuildId = document.getElementById("treeHdnCurrentProductBuildId").value;
     			productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value;
	    		productId = document.getElementById("treeHdnCurrentProductId").value;    	
	    		setTreeDataInHiddenFieldData(productId, productVersionId, productBuildId,workPackageId);
	    		if(productVersionId==null && productVersionId==undefined){
					productVersionId=0;
				}
	    		//showDefects();
	       }
	       		
	       	 selectedTab = $("#tabslist>li.active").index();
	         if(selectedTab == 0){
	      		showDefects();
      		}
   		});
  
   
});


$(document).on('click', '#tabslist>li', function(){
	selectedTab=$(this).index();
	if(selectedTab == 0){
		showDefects();
	}
});

var hdnCurrentDateValue;
var resourceId;
var wNo;
var productId;
var productVersionId;
var productBuildId;
var workPackageId;
var urlForReportDefectsWeeklyView;
var requiredDateFormat;
var urlForReservedStatus;
var newGlobal;
// Time Management Scope Starts


function setTreeDataInHiddenFieldData(productId, productVersionId, productBuildId,workPackageId){
	document.getElementById("hdnProductId").value=productId;
	document.getElementById("hdnProductVersionId").value=productVersionId;
	document.getElementById("hdnProductBuildId").value=productBuildId;
	document.getElementById("hdnWorkPackageId").value=workPackageId;
}

		
function getHiddenFieldData(){
	wNo = document.getElementById("ssnHdnResourceAvailabilityWeekNo").value;
	resourceId = document.getElementById("ssnHdnSelectedResourceId").value;
	productId=document.getElementById("hdnProductId").value;
	productVersionId=document.getElementById("hdnProductVersionId").value;
	productBuildId=document.getElementById("hdnProductBuildId").value;
	workPackageId=document.getElementById("hdnWorkPackageId").value;
}


function getDateFormat(dt,completeMonthYear){
	  var yrMon = completeMonthYear.split(" ");
	  var mn = yrMon[0];
	  if(mn.length>3){
		  mn = mn.substring(0, 3);
	  }
	  var yr = yrMon[1];
	  var date = dt + "-" + mn + "-" + yr;
	  return date;
}

function getDateInyyyyMMddFormat(date){
		 var dt = new Date(date);
		  var yr = dt.getYear() + 1900;
		  var mn = dt.getMonth() + 1;
		  var dtValue =  dt.getDate();
		  if(mn.toString().length == 1){
			  mn =  "0"+mn;
		  }
		  if(dtValue.toString().length == 1){
			  dtValue =  "0"+dtValue;
		  }
		 date=mn + "/" + dtValue + "/" + yr;
		 return date;
	}

function getWeekDateNames(weekIncrement) {
	 var dateNames = new Array();
	 getHiddenFieldData();
	 
	
	    $.ajax({
	            url : 'common.list.weekDateNames?weekNo='+wNo,
	 			dataType : 'json',
	 		/* 	error: function() {
	 				callAlert("Unable to get the dates for the week");
	 			}, */
	            success : function(jsonData) {
	            	if(jsonData.Result=="ERROR"){
	    				callAlert(jsonData.Message);
	    	    		return false;
	    	    	}else{
		        	   	for (var i = 0; i < jsonData.Records.length; i++) {
			             	var dateName = jsonData.Records[i];
			             	dateNames.push(dateName.option);
		        	   }
		               setWeekDateNames(dateNames);
	    	    	}
	          }
	    });
	   
};

function setWeekDateNames(dateNames){
	weekDateNames = dateNames;
	//alert(weekDateNames.toString());
	//listDefectsOfSelectedEntity();
	listDefectsReportDataTable();
}


function showDefects(){
	$("#defectsReportDataTable, #prevNextBtn").show();
	getHiddenFieldData();
	urlForReportDefectsWeeklyView = 'report.defects.weekly.view?&weekNo='+wNo+'&productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&workPackageId='+workPackageId;
	getWeekDateNames(0);
}

var dataSet = {"exportFileName":"","Result":"OK","TotalRecordCount":6,"Message":"","Records":[
{"defectId":0,"defectName":null,"defectsApprovalStatusId":1,"defectsApprovalStatusName":"Open","defectsTypeId":null,"defectsTypeName":null,"weekStartDate":null,"weekNo":44,"day1ValueforToolTip":null,"day2ValueforToolTip":null,"day3ValueforToolTip":null,"day4ValueforToolTip":null,"day5ValueforToolTip":null,"day6ValueforToolTip":null,"day7ValueforToolTip":null,"day1":null,"day2":null,"day3":null,"day4":null,"day5":null,"day6":null,"day7":null,"day1LiveDefectsCount":0,"day2LiveDefectsCount":0,"day3LiveDefectsCount":0,"day4LiveDefectsCount":0,"day5LiveDefectsCount":0,"day6LiveDefectsCount":0,"day7LiveDefectsCount":0,"day8LiveDefectsCount":null,"day1WebDefectsCount":1,"day2WebDefectsCount":5,"day3WebDefectsCount":0,"day4WebDefectsCount":0,"day5WebDefectsCount":null,"day6WebDefectsCount":0,"day7WebDefectsCount":0,"day8WebDefectsCount":null,"totalLiveReportCount":0,"totalWebReportCount":0,"weekDateNumber":null,"weekDateName":null},
{"defectId":0,"defectName":null,"defectsApprovalStatusId":2,"defectsApprovalStatusName":"Approved","defectsTypeId":null,"defectsTypeName":null,"weekStartDate":null,"weekNo":44,"day1ValueforToolTip":null,"day2ValueforToolTip":null,"day3ValueforToolTip":null,"day4ValueforToolTip":null,"day5ValueforToolTip":null,"day6ValueforToolTip":null,"day7ValueforToolTip":null,"day1":null,"day2":null,"day3":null,"day4":null,"day5":null,"day6":null,"day7":null,"day1LiveDefectsCount":0,"day2LiveDefectsCount":0,"day3LiveDefectsCount":0,"day4LiveDefectsCount":0,"day5LiveDefectsCount":0,"day6LiveDefectsCount":0,"day7LiveDefectsCount":0,"day8LiveDefectsCount":null,"day1WebDefectsCount":0,"day2WebDefectsCount":0,"day3WebDefectsCount":0,"day4WebDefectsCount":0,"day5WebDefectsCount":null,"day6WebDefectsCount":0,"day7WebDefectsCount":0,"day8WebDefectsCount":null,"totalLiveReportCount":0,"totalWebReportCount":0,"weekDateNumber":null,"weekDateName":null},{"defectId":0,"defectName":null,"defectsApprovalStatusId":3,"defectsApprovalStatusName":"Corrected Approved","defectsTypeId":null,"defectsTypeName":null,"weekStartDate":null,"weekNo":44,"day1ValueforToolTip":null,"day2ValueforToolTip":null,"day3ValueforToolTip":null,"day4ValueforToolTip":null,"day5ValueforToolTip":null,"day6ValueforToolTip":null,"day7ValueforToolTip":null,"day1":null,"day2":null,"day3":null,"day4":null,"day5":null,"day6":null,"day7":null,"day1LiveDefectsCount":0,"day2LiveDefectsCount":0,"day3LiveDefectsCount":0,"day4LiveDefectsCount":0,"day5LiveDefectsCount":0,"day6LiveDefectsCount":0,"day7LiveDefectsCount":0,"day8LiveDefectsCount":null,"day1WebDefectsCount":0,"day2WebDefectsCount":0,"day3WebDefectsCount":0,"day4WebDefectsCount":0,"day5WebDefectsCount":null,"day6WebDefectsCount":0,"day7WebDefectsCount":0,"day8WebDefectsCount":null,"totalLiveReportCount":0,"totalWebReportCount":0,"weekDateNumber":null,"weekDateName":null},{"defectId":0,"defectName":null,"defectsApprovalStatusId":4,"defectsApprovalStatusName":"Noise","defectsTypeId":null,"defectsTypeName":null,"weekStartDate":null,"weekNo":44,"day1ValueforToolTip":null,"day2ValueforToolTip":null,"day3ValueforToolTip":null,"day4ValueforToolTip":null,"day5ValueforToolTip":null,"day6ValueforToolTip":null,"day7ValueforToolTip":null,"day1":null,"day2":null,"day3":null,"day4":null,"day5":null,"day6":null,"day7":null,"day1LiveDefectsCount":0,"day2LiveDefectsCount":0,"day3LiveDefectsCount":0,"day4LiveDefectsCount":0,"day5LiveDefectsCount":0,"day6LiveDefectsCount":0,"day7LiveDefectsCount":0,"day8LiveDefectsCount":null,"day1WebDefectsCount":0,"day2WebDefectsCount":0,"day3WebDefectsCount":0,"day4WebDefectsCount":0,"day5WebDefectsCount":null,"day6WebDefectsCount":0,"day7WebDefectsCount":0,"day8WebDefectsCount":null,"totalLiveReportCount":0,"totalWebReportCount":0,"weekDateNumber":null,"weekDateName":null},{"defectId":0,"defectName":null,"defectsApprovalStatusId":5,"defectsApprovalStatusName":"Voice","defectsTypeId":null,"defectsTypeName":null,"weekStartDate":null,"weekNo":44,"day1ValueforToolTip":null,"day2ValueforToolTip":null,"day3ValueforToolTip":null,"day4ValueforToolTip":null,"day5ValueforToolTip":null,"day6ValueforToolTip":null,"day7ValueforToolTip":null,"day1":null,"day2":null,"day3":null,"day4":null,"day5":null,"day6":null,"day7":null,"day1LiveDefectsCount":0,"day2LiveDefectsCount":0,"day3LiveDefectsCount":0,"day4LiveDefectsCount":0,"day5LiveDefectsCount":0,"day6LiveDefectsCount":0,"day7LiveDefectsCount":0,"day8LiveDefectsCount":null,"day1WebDefectsCount":0,"day2WebDefectsCount":0,"day3WebDefectsCount":0,"day4WebDefectsCount":0,"day5WebDefectsCount":null,"day6WebDefectsCount":0,"day7WebDefectsCount":0,"day8WebDefectsCount":null,"totalLiveReportCount":0,"totalWebReportCount":0,"weekDateNumber":null,"weekDateName":null},{"defectId":0,"defectName":null,"defectsApprovalStatusId":6,"defectsApprovalStatusName":"Rejected","defectsTypeId":null,"defectsTypeName":null,"weekStartDate":null,"weekNo":44,"day1ValueforToolTip":null,"day2ValueforToolTip":null,"day3ValueforToolTip":null,"day4ValueforToolTip":null,"day5ValueforToolTip":null,"day6ValueforToolTip":null,"day7ValueforToolTip":null,"day1":null,"day2":null,"day3":null,"day4":null,"day5":null,"day6":null,"day7":null,"day1LiveDefectsCount":0,"day2LiveDefectsCount":0,"day3LiveDefectsCount":0,"day4LiveDefectsCount":0,"day5LiveDefectsCount":0,"day6LiveDefectsCount":0,"day7LiveDefectsCount":0,"day8LiveDefectsCount":null,"day1WebDefectsCount":0,"day2WebDefectsCount":0,"day3WebDefectsCount":0,"day4WebDefectsCount":0,"day5WebDefectsCount":null,"day6WebDefectsCount":0,"day7WebDefectsCount":0,"day8WebDefectsCount":null,"totalLiveReportCount":0,"totalWebReportCount":0,"weekDateNumber":null,"weekDateName":null}]}

function listDefectsReportDataTable() {
	try{
		if ($('#defectsReportDataTable').length>0) {			 
			$('#defectsReportDataTable').dataTable().clear();
		}
	} catch(e) {}
	
	openLoaderIcon();
	$.ajax({
	    type: "POST",
	    url : urlForReportDefectsWeeklyView,
	    success: function(data) {
	    	closeLoaderIcon();
	    	
	    	if(data.Result=="ERROR"){
				 return false;
			 }else{
	 	    	if(data.Result=='OK'){
		    		if(data.Records.length>0){
		    			  $('#defectsReportDataTable').DataTable( {
		    			        data: data.Records,
		    					paging: false,					    				
		    					destroy: true,
		    					searching: false,
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
		    			        aoColumns: [
		    						{ mData: "defectsApprovalStatusName" },
		    						
		    						{ mData: "totalLiveReportCount" },
		    						{ mData: "totalWebReportCount" },
		    						
		    						{ mData: "day1LiveDefectsCount" },
		    						{ mData: "day1WebDefectsCount" },
		    						
		    						
		    						{ mData: "day2LiveDefectsCount" },
		    						{ mData: "day2WebDefectsCount" },
		    						
		    						{ mData: "day3LiveDefectsCount" },
		    						{ mData: "day3WebDefectsCount" },

		    						{ mData: "day4LiveDefectsCount" },
		    						{ mData: "day4WebDefectsCount" },
		    						
		    						
		    						{ mData: "day5LiveDefectsCount" },
		    						{ mData: "day5WebDefectsCount" },
		    						
		    						{ mData: "day6LiveDefectsCount" },
		    						{ mData: "day6WebDefectsCount" },
		    						
		    						{ mData: "day7LiveDefectsCount" },
		    						{ mData: "day7WebDefectsCount" }
		    								    				         
		    					]
		    			    } );
		    			  $("#appStatus").text(weekDateNames[0].substring(0,6) + " ~ " + weekDateNames[weekDateNames.length-1].substring(0,6));
		    			  $("#totalRepCnt").text("Total for " + $("#appStatus").text());
		    			  for(var i=1;i<=weekDateNames.length;i++) {
		    				  $("#day" + i).text(weekDateNames[i-1]);
		    			  }
		    			 // $("#defectsReportDataTable, #prevNextBtn").show();
		    		}
	 	    	}
			 }
	    },
	    error : function(data){					
			closeLoaderIcon();
		},
		complete : function(data){				
			closeLoaderIcon();
		}
	});
	

}
function listDefectsOfSelectedEntity(){
	try{
		if ($('#jTableContainerDefectsWeeklyView').length>0) {
			 $('#jTableContainerDefectsWeeklyView').jtable('destroy'); 
		}
	} catch(e) {}
	//init jTable
	$('#jTableContainerDefectsWeeklyView').jtable({

		title: 'Defect Weekly Report',
		selecting : true, //Enable selecting 
		paging : false, //Enable paging
		//pageSize : 10,
		editinline : {enable : true},
		//selecting : true, //Enable selecting
		//multiselect : true, //Allow multiple selecting
		//selectingCheckboxes : true, //Show checkboxes on first column
		//Set page size (default: 10)
		//sorting: true, //Enable sortin
		/*  saveUserPreferences: false, */
		toolbar : {
			items : [
				{
					text : 'Export to Google Drive',
					click : function() {
						exportToGD();
					}
				},         
			{
				text : '   <<   ',
				click : function() {
					showDefectsOfPreviousWeek();
				}
			},
			{	
				text : '   >>   ',
				click : function() {
					showDefectsOfNextWeek();
				}
			}
			]
		},
		
		actions : {
			listAction : urlForReportDefectsWeeklyView,
		},
		
		fields : {
			defectId: { 
      		 title: 'Defect ID',
      		 edit: false,
      		 list:false,
      		 key: true
      	},
      	weekNo: {
           title: 'WeekNo',
           width: "12%",
           edit: true,
           list:false,
           type:'hidden',
	   	},
	   	defectsApprovalStatusId: {
               title: 'Status  Id',
               edit: false,
               type:'hidden',
               list:false,
                display: function (data) { 
  				 return data.record.defectsApprovalStatusId;
               },
      	},
      	defectsApprovalStatusName: {
               title: 'Approval Status Name',
               width: "12%",
               edit: false,
                display: function (data) { 
  				 return data.record.defectsApprovalStatusName;
               },
      	},
      	totalLiveReportCount: {
            title: 'Total Live',
            width: "7%",
            list:true
   		},
   		totalWebReportCount: {
            title: 'Total Web',
            width: "7%",
            list:true
   		},
      	day1WebDefectsCount: {
            title: weekDateNames[0]+' Web',
            width: "7%",
            list:true
   		},
   		day2LiveDefectsCount: {
            title: weekDateNames[1]+' Live',
            width: "7%",
            list:true
	   	},
	   	day2WebDefectsCount: {
         	title: weekDateNames[1]+' Web',
         	width: "7%",
         	list:true
		},
	 	day3LiveDefectsCount: {
            title: weekDateNames[2]+' Live',
            width: "7%",
            list:true
	   	},
	   	day3WebDefectsCount: {
         	title: weekDateNames[2]+' Web',
         	width: "7%",
         	list:true
		},
		day4LiveDefectsCount: {
        	 title: weekDateNames[3]+' Live',
         	width: "7%",
         	list:true
	   	},
	   	day4WebDefectsCount: {
      		title: weekDateNames[3]+' Web',
      		width: "7%",
      		list:true
		},day5LiveDefectsCount: {
            title: weekDateNames[4]+' Live',
            width: "7%",
            list:true
	   	},
	   	day5WebDefectsCount: {
         	title: weekDateNames[4]+' Web',
         	width: "7%",
         	list:true
		},
	 	day6LiveDefectsCount: {
            title: weekDateNames[5]+' Live',
            width: "7%",
            list:true
	   	},
	   	day6WebDefectsCount: {
         	title: weekDateNames[5]+' Web',
         	width: "7%",
         	list:true
		},
		day7LiveDefectsCount: {
        	 title: weekDateNames[6]+' Live',
         	width: "7%",
         	list:true
	   	},
	   	day7WebDefectsCount: {
      		title: weekDateNames[6]+' Web',
      		width: "7%",
      		list:true
		},
	},
	});
	$('#jTableContainerDefectsWeeklyView').jtable(
			'load',
			{
				workPackageName : $('#workPackageName_dd')
						.children('div').attr('id')

			});

	var jscrolheight = $("#jTableContainerDefectsWeeklyView").height();
	var jscrolwidth = $("#jTableContainerDefectsWeeklyView").width();

	$(".jScrollContainer").on(
			"scroll",
			function() {
				var lastScrollLeft = 0;

				var documentScrollLeft = $(".jScrollContainer")
						.scrollLeft();

				if (lastScrollLeft < documentScrollLeft) {
					$("#jTableContainerDefectsWeeklyView").width(
							$(".jtable").width()).height(
							$(".jtable").height());
					lastScrollLeft = documentScrollLeft;
				} else if (lastScrollLeft >= documentScrollLeft) {
					$("#jTableContainerDefectsWeeklyView").width(jscrolwidth)
							.height(jscrolheight);
				}
			});
}

function showDefectsOfPreviousWeek() {
	//$("#defectsReportDataTable, #prevNextBtn").hide();
	getHiddenFieldData();
 	var previousWeekNo = wNo-1;
	document.getElementById("ssnHdnResourceAvailabilityWeekNo").value=previousWeekNo;
	urlForReportDefectsWeeklyView = 'report.defects.weekly.view?&weekNo='+previousWeekNo+'&productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&workPackageId='+workPackageId;
	getWeekDateNames(1);
	return false;
}
	
function showDefectsOfNextWeek() {
	//$("#defectsReportDataTable, #prevNextBtn").hide();
	getHiddenFieldData();
 	var nextWeekNo = wNo-(-1);
	document.getElementById("ssnHdnResourceAvailabilityWeekNo").value=nextWeekNo;
	urlForReportDefectsWeeklyView = 'report.defects.weekly.view?&weekNo='+nextWeekNo+'&productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&workPackageId='+workPackageId;
	getWeekDateNames(-1);
	return false;
}

function exportToGD() {
	
	var urlMapping = 'export.to.generate.auth.key';
	 $.ajax({
         type: "POST",
   		contentType: "application/json; charset=utf-8",
         url : urlMapping,
         dataType : 'json',
         success : function(data) {
              if(data.Result=="OK"){
                //callAlert(data.Message);
                $("#export_url").text("Click here for Authentication Key");
                $("#export_url").attr("href", data.Message);
                $("#export_key").val("");
                $("#div_PopupExportDefect").modal();
             }
         }
  }); 
	
}

function exportOption(){
	var keys= $("#export_key").val();
	if($.trim($("#export_key").val()) == "") {
		callAlert("Please Enter Authentication Key");
		return false;
	}
	var url = 'export.to.google.drive.reports?&weekNo='+wNo+'&productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&workPackageId='+workPackageId+'&code='+keys;
	var thediv = document.getElementById('reportbox');
	if (thediv.style.display == "none") {
			$("#loadingIcon").show();
			$("body").addClass("loaderIcon");
		$ 
			.post(url,
				function(data) {
					if(data.Result=='OK'){
						$("#div_PopupExportDefect").modal("hide");
						callAlert(data.Message);
					}else{
						callAlert(data.Message);
						$("#loadingIcon").hide();
						$("body").removeClass("loaderIcon");
						return false;
					}
					thediv.style.display = "";
					$("#loadingIcon").hide();
					$("body").removeClass("loaderIcon");
				});
	} else {
		thediv.style.display = "none";
		thediv.innerHTML = '';
	}
	return false;
}

// Availability Scope End
	
</script>						
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>