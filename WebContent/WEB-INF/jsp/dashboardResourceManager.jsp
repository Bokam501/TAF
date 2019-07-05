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

<!-- BEGIN HORIZONTAL CALENDAR -->
<link rel="stylesheet" type="text/css" media="screen" href="js/calendar/calendarstyle.css" />
<link rel="stylesheet" type="text/css" media="screen" href="js/calendar/includes/shadowbox-3.0.3/shadowbox.css" />
<link rel="stylesheet" type="text/css" media="screen" href="js/calendar/includes/jquery-minicolors/jquery.miniColors.css" />
<!-- END HORIZONTAL CALENDAR -->	

<!-- END LIVE TILE -->   
<link href="css/customStyle.css" rel="stylesheet" type="text/css">
<style>
	.modal-backdrop{
		z-index: 10 !important;
	}
</style>

</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-header-menu-fixed" class to set the mega menu fixed  -->
<!-- DOC: Apply "page-header-top-fixed" class to set the top menu fixed  -->
	
<body>
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

<!-- BEGIN PAGE CONTENT -->
<div class="page-container">
<div><%@include file="postHeader.jsp" %></div>
<!-- BEGIN DASHBOARD STATS -->
<div class="page-content">
<div class="container-fluid">
<div class="portlet light" >
		<div class="portlet-body" >
		<div id="div1" >
			<div class="row" >
			<form action="#" class="form-horizontal form-row-sepe">
					<input type="hidden" id="hdnSelectedDate" value="">
					<input type="hidden" id="hdnTestFactoryLabId" value="">
					<input type="hidden" id="hdnResourcePoolId" value="">
					<ul style="display: -webkit-inline-box; list-style-type: none;">
						<li>
							<div id="miniMonthCalendarContainer" style="margin-top:20px">
								<div id="monthManagement">
									<input type="button" id="prevYear" name="prevYear" value="«" title="Previous year">
									<input type="button" id="prevMonth" name="prevMonth" value="<" title="Previous month">
									<span id="currentMonth"></span>
									<input type="button" id="nextMonth" name="prevMonth" value=">" title="Next month">
									<input type="button" id="nextYear" name="nextYear" value="»" title="Next year">
								</div>
							<table id="miniMonthCalendar" >
								<tbody>
									<tr><td>Fr</td><td>Sa</td><td>Su</td><td>Mo</td><td>Tu</td><td>We</td><td>Th</td><td>Fr</td><td>Sa</td><td>Su</td><td>Mo</td><td>Tu</td><td>We</td><td>Th</td><td>Fr</td><td>Sa</td><td>Su</td><td>Mo</td><td>Tu</td><td>We</td><td>Th</td><td>Fr</td><td>Sa</td><td>Su</td><td>Mo</td><td>Tu</td><td>We</td><td>Th</td><td>Fr</td><td>Sa</td><td>Su</td></tr>
									<tr><td>01</td><td>02</td><td>03</td><td>04</td><td>05</td><td>06</td><td>07</td><td>08</td><td>09</td><td>10</td><td>11</td><td>12</td><td>13</td><td>14</td><td>15</td><td>16</td><td>17</td><td>18</td><td>19</td><td>20</td><td>21</td><td>22</td><td>23</td><td>24</td><td>25</td><td>26</td><td>27</td><td>28</td><td>29</td><td>30</td><td>31</td></tr>
								</tbody>
							</table>
						</div>
						</li>

						<li>
							<div class="form-body" style="margin: 10px 30px;padding-top:26px;width: 400px;">
								 
								 <div class="form-group" style="margin-left: 0px;margin-right: 0px;">								 
									 <ul style="list-style-type: none;  display: -webkit-inline-box;">
									 	<li style="padding-top: 5px;">
									 		<label>Test Factory Lab</label>
									 	</li>								 	
									 	<li>
									 	   <div class="col-md-12">
									 		<div class="input-group">
												<span class="input-group-addon">
												<i class="fa fa-institution"></i>
												</span>
												<select class="form-control select2me" data-placeholder="Select..." id="testFactoryLab_dd" >
													<option value="select">Select</option>
												</select>
											</div>
											</div>
									 	</li>
									 </ul>									
								</div>
								
								<div class="form-group" style="margin-left: 0px;margin-right: 0px;">								 
								 <ul style="list-style-type: none;  display: -webkit-inline-box;">
								 	<li style="padding-top: 5px; padding-right: 15px;">
								 		<label>Resource Pool</label>
								 	</li>								 	
								 	<li>
								 	   <div class="col-md-12">
								 		<div class="input-group">
											<span class="input-group-addon">
											<i class="fa fa-institution"></i>
											</span>
											<select class="form-control select2me" data-placeholder="Select..." id="resourcePools_dd">
												<option value="select">Select</option>
											</select>
										</div>
										</div>
								 	</li>
								 </ul>									
							</div>															 
							</div>
					 	</li>					 	
				 </ul>
			</form>
		</div>
		</div>
		<div id="div2_dashboard" data-toggle="modal" >
		<div class="row" >
				<div class="col-md-4 col-md-4 col-sm-6 col-xs-12">
					<div class="dashboard-stat red-intense">
						<div class="visual">
							<i class="fa fa-sun-o"></i>
						</div>
						<div class="details" >
							<div class="number" id="divMorningShiftDemand">
								 
							</div>
							<div class="desc">
								 Demand
							</div>
						</div>
						<a class="more" href="javascript:showDemand(1);">
						Morning <i class="m-icon-swapright m-icon-white" href="javascript:showDemand(1);"></i>
						</a>
					</div>
				</div>
				<div class="col-md-4 col-md-4 col-sm-6 col-xs-12">
					<div class="dashboard-stat green-haze">
						<div class="visual">
							<i class="fa fa-sun-o"></i>
						</div>
						<div class="details">
							<div class="number" id="divMorningShiftBookingAndReservation">
								 
							</div>
							<div class="desc">
								 Booking/Reservation
							</div>
						</div>
						<a class="more" href="javascript:showBookings(1);">
						Morning <i class="m-icon-swapright m-icon-white"></i>
						</a> <div id="divBRMorningDate"></div>
					</div>
				</div>
				<div class="col-md-4 col-md-4 col-sm-6 col-xs-12">
					<div class="dashboard-stat purple-plum">
						<div class="visual">
							<i class="fa fa-sun-o"></i>
						</div>
						<div class="details">
							<div class="number" id="divMorningShiftAttendance">
								
							</div>
							<div class="desc">
								 Attendance
							</div>
						</div>
						<a class="more" href="javascript:showAttendance(1);">
						Morning <i class="m-icon-swapright m-icon-white"></i>
						</a>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-4 col-md-4 col-sm-6 col-xs-12">
					<div class="dashboard-stat red-intense">
						<div class="visual">
							<i class="fa fa-moon-o"></i>
						</div>
						<div class="details">
							<div class="number" id="divNightShiftDemand">
								 
							</div>
							<div class="desc">
								 Demand
							</div>
						</div>
						<a class="more" href="javascript:showDemand(2);">
						Night <i class="m-icon-swapright m-icon-white"></i>
						</a>
					</div>
				</div>
				<div class="col-md-4 col-md-4 col-sm-6 col-xs-12">
					<div class="dashboard-stat green-haze">
						<div class="visual">
							<i class="fa fa-moon-o"></i>
						</div>
						<div class="details">
							<div class="number" id="divNightShiftBookingAndReservation">
								 
							</div>
							<div class="desc">
								 Booking/Reservation
							</div>
						</div>
						<a class="more" href="javascript:showBookings(2);">
						Night <i class="m-icon-swapright m-icon-white"></i>
						</a>
					</div>
				</div>
				<div class="col-md-4 col-md-4 col-sm-6 col-xs-12">
					<div class="dashboard-stat purple-plum">
						<div class="visual">
							<i class="fa fa-moon-o"></i>
						</div>
						<div class="details">
							<div class="number" id="divNightShiftAttendance">
								
							</div>
							<div class="desc">
								 Attendance
							</div>
						</div>
						<a class="more" href="javascript:showAttendance(2);">
						Night <i class="m-icon-swapright m-icon-white"></i>
						</a>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-4 col-md-4 col-sm-6 col-xs-12">
					<div class="dashboard-stat red-intense">
						<div class="visual">
							<i class="fa fa-star-o"></i>
						</div>
						<div class="details">
							<div class="number" id="divGraveyardShiftDemand">
								 
							</div>
							<div class="desc">
								 Demand
							</div>
						</div>
						<a class="more" href="javascript:showDemand(3);">
						Graveyard <i class="m-icon-swapright m-icon-white"></i>
						</a>
					</div>
				</div>
				<div class="col-md-4 col-md-4 col-sm-6 col-xs-12">
					<div class="dashboard-stat green-haze">
						<div class="visual">
							<i class="fa fa-star-o"></i>
						</div>
						<div class="details">
							<div class="number" id="divGraveyardShiftBookingAndReservation">
								 
							</div>
							<div class="desc">
								 Booking/Reservation
							</div>
						</div>
						<a class="more" href="javascript:showBookings(3);">
						Graveyard <i class="m-icon-swapright m-icon-white"></i>
						</a>
					</div>
				</div>
				<div class="col-md-4 col-md-4 col-sm-6 col-xs-12">
					<div class="dashboard-stat purple-plum">
						<div class="visual">
							<i class="fa fa-star-o"></i>
						</div>
						<div class="details">
							<div class="number" id="divGraveyardShiftAttendance">
								
							</div>
							<div class="desc">
								 Attendance
							</div>
						</div>
						<a class="more" href="javascript:showAttendance(3);">
						Graveyard <i class="m-icon-swapright m-icon-white"></i>
						</a>
					</div>
				</div>
			</div>
		</div>
		</div>
			<!-- END DASHBOARD STATS -->
     </div>
    </div>
   </div>
</div>
<!-- END PAGE CONTENT -->

<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp" %></div>			
<!-- END FOOTER -->
<!-- Start Popup -->
	 <div id="div_PopupMain" class="divPopUpMain" style="display:none;">
	        <div title="Press Esc to close" onclick="popupClose()" class="ImgPopupClose">
	        </div>	        
	</div>
	<div id="div_PopupBackground"></div>
	<!-- End Popup -->

<div id="div_PopupMainrc" class="modal " tabindex="-1" aria-hidden="true" style="z-index:999">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupCloserc()" aria-hidden="true"></button>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:320px" data-always-visible="1" data-rail-visible1="1">
				    <div id="demandconfig">
				    </div>				
				</div>
			</div>
		</div>
	</div>
</div>	

<!-- BEGIN JAVASCRIPTS (Load javascripts at bottom, this will reduce page load time) -->
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.cokie.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->

<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.sparkline.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->

<!-- <script type='text/javascript' src='js/calendar/includes/jquery-1.7.1.min.js'></script> -->
	<script type='text/javascript' src='js/calendar/includes/jquery-ui-1.8.17.custom.js'></script>
	<script type='text/javascript' src='js/calendar/includes/jquery.ui.core.min.js'></script>
	<script type='text/javascript' src='js/calendar/includes/jquery.ui.widget.min.js'></script>
	<script type='text/javascript' src='js/calendar/includes/jquery.ui.mouse.min.js'></script>
	<script type='text/javascript' src='js/calendar/includes/jquery.ui.position.min.js'></script>
	<script type='text/javascript' src='js/calendar/includes/jquery.ui.draggable.min.js'></script>
	<script type='text/javascript' src='js/calendar/includes/jquery.ui.resizable.min.js'></script>
	<script type='text/javascript' src='js/calendar/includes/jquery.ui.selectable.min.js'></script>
	<script type='text/javascript' src='js/calendar/includes/jquery.ui.droppable.min.js'></script>
	<script type='text/javascript' src='js/calendar/includes/jquery.ui.slider.min.js'></script>
	<script type='text/javascript' src='js/calendar/includes/jquery.ui.dialog.min.js'></script>
<!-- Include jTable script file. -->
<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>
<script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
	<!-- SHADOWBOX -->
	<script type='text/javascript' src='js/calendar/includes/shadowbox-3.0.3/shadowbox.js'></script>
	
	<!-- JQUERY - MINICOLORS -->
	<script type='text/javascript' src='js/calendar/includes/jquery-minicolors/jquery.miniColors.min.js'></script>
	
	<!-- DATES MANAGEMENT -->
	<script type='text/javascript' src='js/calendar/includes/date.js'></script>
	
	<script src="js/tab/jquery-ui.js"></script>
	
	<script type='text/javascript' src='js/calendar/eventouchcalendar.min.js'></script>
	
	<!-- BEGIN LIVE TILE  -->
	<!-- <script type='text/javascript' src='js/metrojs/modernizr-2.5.3.js'></script> -->
	<!-- END LIVE TILE   -->
<script>
jQuery(document).ready(function() {    
   $("#menuList li:first-child").eq(0).remove();
   setBreadCrumb("My Dashboard");
   var CELL_WIDTH_LITTLE = 100, CELL_WIDTH_MEDIUM = 150, CELL_WIDTH_LARGE = 200;
   var CELL_HEIGHT_LITTLE = 7, CELL_HEIGHT_MEDIUM = 9, CELL_HEIGHT_LARGE = 15;	
   var selectedTestFactoryLab;
   
   loadTestFactoryLabs();
      
	$("#testFactoryLab_dd").change(function() {
	 	selectedTestFactoryLab = $('#testFactoryLab_dd option:selected').val();
	 	document.getElementById("hdnTestFactoryLabId").value = selectedTestFactoryLab;	 	
	 	hdnselectedDateValue = document.getElementById("hdnSelectedDate").value;
	 	loadResourcePools(selectedTestFactoryLab);
	 	
// 	 	selectedResourcePoolId = $('select#resourcePools_dd').find('option:nth-child(0)').val();console.log(selectedResourcePoolId);//.val();
// 	 	loadDashBoard(hdnselectedDateValue,selectedTestFactoryLab,selectedResourcePoolId);
	});
	
	$("#resourcePools_dd").change(function() {	 	 
	 	 hdnselectedDateValue = document.getElementById("hdnSelectedDate").value;
	 	 hdnTestFactoryLabId = document.getElementById("hdnTestFactoryLabId").value;
	 	 selectedResourcePoolId = $('#resourcePools_dd option:selected').val();
	 	 
	 	 document.getElementById("hdnResourcePoolId").value = selectedResourcePoolId;
	 	 loadDashBoard(hdnselectedDateValue,hdnTestFactoryLabId,selectedResourcePoolId);	 	 
	});
	
	/* var auto_refresh = setInterval(function(){
		getHiddenFieldData();
		loadDashBoard(hdnselectedDateValue,hdnTestFactoryLabId,hdnResourcePoolId);
	},20000);   */
	
 	auto_refresh = setInterval(function(){
 		setCurrentDate();
		//getHiddenFieldData();
		//loadDashBoard(hdnselectedDateValue,hdnTestFactoryLabId,hdnResourcePoolId);
	},100);		
	
	$agenda = $(document).eventouchcalendar({	
	});
	
});
$(document).on("click","#miniMonthCalendar>tbody>tr:last-child", function(event){
	highLightSelectedDate(event);
});
var $td;
var moringShiftType = 1;
var nightShiftType = 2;
var graveyardShiftType = 3;
var hdnselectedDateValue;
var hdnTestFactoryLabId;
var hdnResourcePoolId;
var auto_refresh;
//var initialTestFactoryLab_Value;
var urlToGetDetails;
var dataTypeFilter;
var requiredDateFormat;

function getHiddenFieldData(){
	 hdnTestFactoryLabId = document.getElementById("hdnTestFactoryLabId").value;
	 hdnResourcePoolId = document.getElementById("hdnResourcePoolId").value;
	 hdnselectedDateValue = document.getElementById("hdnSelectedDate").value;
}

function highLightSelectedDate(event){
	if($td){
		$td.removeClass("isEvent");
	}
	$td = $(event.target);
	$td.addClass("isEvent");
	var dt = event.target.innerText;
	var yrMon = $('#monthManagement').find('span').text();
	var selectedDate = getDateFormat(dt,yrMon);
	document.getElementById("hdnSelectedDate").value = selectedDate;
	loadTestFactoryLabsAndResourcePools(selectedDate);
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

function loadTestFactoryLabs(){
	$('#testFactoryLab_dd').empty();
		$.post('common.list.testfactorylab.list',function(data) {	
        var ary = (data.Options);
        //initialTestFactoryLab_Value = ary[0].DisplayText;
        
        $.each(ary,function(i,data){
        	var div_data="<option value="+data.Value+">"+data.DisplayText+"</option>";
        	$(div_data).appendTo('#testFactoryLab_dd'); 
        	
       });
 	});
} 

function loadResourcePools(selectedTestFactoryLab){
	$('#resourcePools_dd').empty();
	var temp =$('#hdnResourcePoolId').val();
		$.post('common.resourcePool.list.by.testFactoryLabId?testFactoryLabId='+selectedTestFactoryLab,function(data) {	
        var ary = (data.Options);
        $.each(ary,function(i,data){
        	var div_data="";
        	if(temp == data.Value){
        		div_data = "<option selected value="+data.Value+">"+data.DisplayText+"</option>";
        	}
        	else{
        		div_data = "<option value="+data.Value+">"+data.DisplayText+"</option>";
        	}
        	$(div_data).appendTo('#resourcePools_dd'); 
       });
        selectedTestFactoryLab = $('#testFactoryLab_dd option:selected').val();
	 	document.getElementById("hdnTestFactoryLabId").value = selectedTestFactoryLab;
	 		 		 	
	 	selectedResourcePoolId = $('#resourcePools_dd option:selected').val();
		document.getElementById("hdnResourcePoolId").value = selectedResourcePoolId;		
	 	
	 	loadDashBoard(hdnselectedDateValue,selectedTestFactoryLab,selectedResourcePoolId);
 	});
}

function loadTestFactoryLabsAndResourcePools(currentDate){	
	requiredDateFormat = getDateInyyyyMMddFormat(currentDate);
	var dt = new Date(currentDate);
	wNo = dt.getWeekOfYear()+1;
	document.getElementById("hdnTestFactoryLabId").value = wNo;
	getHiddenFieldData();
	
	var selectedDate=new Date(requiredDateFormat);
	selectd=new Date(selectedDate.getFullYear(),selectedDate.getMonth(),selectedDate.getDate());
	var toSend = $('#testFactoryLab_dd option:selected').val();
	if (typeof toSend == "undefined"){
		toSend = 1 ;
	}
	loadResourcePools(toSend);
	//loadDashBoard(currentDate,'1','1');	
}

function loadDashBoard(hdnselectedDateValue,testFactoryLabId,selectedResourcePoolId){
	loadDemand(hdnselectedDateValue,testFactoryLabId,selectedResourcePoolId);
	loadBookingsAndReservations(hdnselectedDateValue,testFactoryLabId,selectedResourcePoolId);
	loadAttendance(hdnselectedDateValue,testFactoryLabId,selectedResourcePoolId);
}

function loadDemand(selectedDate,testFactoryLabId,selectedResourcePoolId){
	// Type 0 - Demand
	var demandUrl = 'dashboard.resource.demand?type=0&strSelectedDate='+selectedDate+'&testFactoryLabId='+testFactoryLabId+'&resourcePoolId='+selectedResourcePoolId;
	var divMorningShiftDemand = "#divMorningShiftDemand";
	var divNightShiftDemand = "#divNightShiftDemand";
	var divGraveyardShiftDemand = "#divGraveyardShiftDemand";
	loadDemandByShift(demandUrl,moringShiftType,divMorningShiftDemand);
	loadDemandByShift(demandUrl,nightShiftType,divNightShiftDemand);
	loadDemandByShift(demandUrl,graveyardShiftType,divGraveyardShiftDemand);
}

function loadDemandByShift(demandUrl,shiftTypeId,divId){
	var completeDemandUrl = demandUrl+'&shiftTypeId='+shiftTypeId;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : completeDemandUrl,
		dataType : 'json',
		success : function(data) {
			if($(divId).length>0){
				$(divId).empty();
				$(divId).append(data.Message);
			}
		}
	});
}

function loadBookingsAndReservations(selectedDate,testFactoryLabId,selectedResourcePoolId){
	// Type 1 - Booking/Reservation
	var bookingAndResUrl = 'dashboard.resource.demand?type=1&strSelectedDate='+selectedDate+'&testFactoryLabId='+testFactoryLabId+'&resourcePoolId='+selectedResourcePoolId;
	var divMorningShiftDemand = "#divMorningShiftBookingAndReservation";
	var divNightShiftDemand = "#divNightShiftBookingAndReservation";
	var divGraveyardShiftDemand = "#divGraveyardShiftBookingAndReservation";
	loadBookingsAndReservationsByShift(bookingAndResUrl,moringShiftType,divMorningShiftDemand);
	loadBookingsAndReservationsByShift(bookingAndResUrl,nightShiftType,divNightShiftDemand);
	loadBookingsAndReservationsByShift(bookingAndResUrl,graveyardShiftType,divGraveyardShiftDemand);
}

function loadBookingsAndReservationsByShift(bookingAndResUrl,shiftTypeId,divId){
	//$("#divBRMorningDate").append(shiftTypeId);
	var completeBookingAndResUrl = bookingAndResUrl+'&shiftTypeId='+shiftTypeId;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : completeBookingAndResUrl,
		dataType : 'json',
		success : function(data) {
			if($(divId).length>0){
				$(divId).empty();
				$(divId).append(data.Message);
			}
		}
	});
}

function loadAttendance(selectedDate,testFactoryLabId,selectedResourcePoolId){
	// Type 2 - Attendance
	var attendanceUrl = 'dashboard.resource.demand?type=2&strSelectedDate='+selectedDate+'&testFactoryLabId='+testFactoryLabId+'&resourcePoolId='+selectedResourcePoolId;
	var divMorningShiftAttendance = "#divMorningShiftAttendance";
	var divNightShiftAttendance = "#divNightShiftAttendance";
	var divGraveyardShiftAttendance = "#divGraveyardShiftAttendance";
	loadAttendanceByShift(attendanceUrl,moringShiftType,divMorningShiftAttendance);
	loadAttendanceByShift(attendanceUrl,nightShiftType,divNightShiftAttendance);
	loadAttendanceByShift(attendanceUrl,graveyardShiftType,divGraveyardShiftAttendance);
}

function loadAttendanceByShift(attendanceUrl,shiftTypeId,divId){
	var completeAttendanceUrl = attendanceUrl+'&shiftTypeId='+shiftTypeId;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : completeAttendanceUrl,
		dataType : 'json',
		success : function(data) {
			if($(divId).length>0){
				$(divId).empty();
				$(divId).append(data.Message);
			}
		}
	});
}

function setCurrentDate(){
		clearInterval(auto_refresh);
	 	var d = new Date();
		var day = d.getDate();		
 		 		
		$("#miniMonthCalendar td").filter(function() {			 
			var tdValFromUI = $.text([this]);		
			if(tdValFromUI == day)
			{	 			
				$('#currentMonth').value = d.getMonthName()+" "+d.getDayOfYear();
				var yrMon = $('#monthManagement').find('span').text();			
				
				currentDate = getDateFormat(day,yrMon);				
				document.getElementById("hdnSelectedDate").value = currentDate;
				//document.getElementById("crtSelectedDate").value = currentDate;
				var tempDay=day-1;			
				$td = $("#miniMonthCalendar tr:eq(1)").find('td:eq('+tempDay+')');			
				$td.addClass('isCurrentDate');
				loadTestFactoryLabsAndResourcePools(currentDate);
			}
		});
}

function showDemand(shiftTypeId){
	getHiddenFieldData();
	dataTypeFilter = "Demand";
	urlToGetDetails = 'demand.booking.attendance.details.listing?dataTypeFilter='+dataTypeFilter+'&testFactoryLabId='+hdnTestFactoryLabId+'&resourcePoolId='+hdnResourcePoolId+'&shiftTypeId='+shiftTypeId+'&workDate='+hdnselectedDateValue;
	//showResult(urlToGetDetails);

	//$('#div_PopupMainrc .scroller').empty();
	$(' #demandconfig').empty();
	 try{
		 if ($(' #demandconfig').length>0) {
			 $(' #demandconfig').jtable('destroy'); 
		 }		
		} catch(e) {}
	    $(' #demandconfig').jtable({
			title: "Demand Details",
	        selecting: true,  //Enable selecting 
	        editinline : {enable : true},
	        actions: {
	       	 	listAction: urlToGetDetails,
	        },  
	        fields : {
	        	wpDemandProjectionId: {
	                 key: true,
	                 list: false
	             },
	           workPackageName: {
	                 title: 'Work Package',
	                 edit:false,
	                 list:true,
	                 width: "10%"
	 			},
	 			shiftName: {
	                title: 'Shift Name',
	                edit:false,
	                list:true,
	                width: "10%"
				},
				productName: {
	                title: 'Product',
	                edit:false,
	                list:true,
	                width: "10%"
				},
				workDate: {
	                title: 'Date',
	                edit:false,
	                list:true,
	                width: "10%"
				},
				skillName: {
	                title: 'Skill',
	                edit:false,
	                list:true,
	                width: "10%"
				},
				userRoleName: {
	                title: 'Role',
	                edit:false,
	                list:true,
	                width: "10%"
				},
				demandRaisedUser: {
	                title: 'Demand Raised By',
	                edit:false,
	                list:true,
	                width: "10%"
				},
				resourceCount: {
	                title: 'Resource Count',
	                edit:false,
	                list:true,
	                width: "10%"
				},
	        	}
			});
		$(' #demandconfig').jtable('load');
		
		var jscrolheight = $(" #demandconfig").height();
		var jscrolwidth = $(" #demandconfig").width();
		  
		$(".jScrollContainer").on("scroll", function() {
			 var lastScrollLeft=0;	
			 var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
			 if (lastScrollLeft < documentScrollLeft) {
			    	$(" #demandconfig").width($(".jtable").width()).height($(".jtable").height());			
			        lastScrollLeft = documentScrollLeft;
			  }else if(lastScrollLeft >= documentScrollLeft){
			    	$(" #demandconfig").width(jscrolwidth).height(jscrolheight);
			  }
		});
		loadPopup("div_PopupMainrc");
}

function showBookings(shiftTypeId){
	dataTypeFilter = "Reservation";
	getHiddenFieldData();
	urlToGetDetails = 'demand.booking.attendance.details.listing?dataTypeFilter='+dataTypeFilter+'&testFactoryLabId='+hdnTestFactoryLabId+'&resourcePoolId='+hdnResourcePoolId+'&shiftTypeId='+shiftTypeId+'&workDate='+hdnselectedDateValue;
	//callAlert("urlToGetDetails: "+urlToGetDetails);
	//showResult(urlToGetDetails);
	//$('#div_PopupMainrc .scroller').empty();
	$(' #demandconfig').empty();
	 try{
		 if ($(' #demandconfig').length>0) {
			 $(' #demandconfig').jtable('destroy'); 
		 }		
		} catch(e) {}
	    $(' #demandconfig').jtable({
			title: "Reservations",
	        selecting: true,  //Enable selecting 
	        editinline : {enable : true},
	        actions: {
	       	 	listAction: urlToGetDetails,
	        },  
	        fields : {
	        	testFactoryResourceReservationId: {
	                 key: true,
	                 list: false
	             },
	            productName: {
		                title: 'Product',
		                edit:false,
		                list:true,
		                width: "10%"
				},
	            workPackageName: {
	                 title: 'Work Package',
	                 edit:false,
	                 list:true,
	                 width: "10%"
	 			},
	 			shiftName: {
	                title: 'Shift Name',
	                edit:false,
	                list:true,
	                width: "10%"
				},
				blockedUserName: {
	                title: 'User',
	                edit:false,
	                list:true,
	                width: "10%"
				},
				reservationDate: {
	                title: 'Date',
	                edit:false,
	                list:true,
	                width: "10%"
				},
				reservationActionUserName: {
	                title: 'Reserved By',
	                edit:false,
	                list:true,
	                width: "10%"
				},
	        	}
			});
		$(' #demandconfig').jtable('load');
		
		var jscrolheight = $(" #demandconfig").height();
		var jscrolwidth = $(" #demandconfig").width();
		  
		$(".jScrollContainer").on("scroll", function() {
			 var lastScrollLeft=0;	
			 var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
			 if (lastScrollLeft < documentScrollLeft) {
			    	$(" #demandconfig").width($(".jtable").width()).height($(".jtable").height());			
			        lastScrollLeft = documentScrollLeft;
			  }else if(lastScrollLeft >= documentScrollLeft){
			    	$(" #demandconfig").width(jscrolwidth).height(jscrolheight);
			  }
		});
		loadPopup("div_PopupMainrc");
}

function showAttendance(shiftTypeId){
	dataTypeFilter = "Attendance";
	getHiddenFieldData();
	urlToGetDetails = 'demand.booking.attendance.details.listing?dataTypeFilter='+dataTypeFilter+'&testFactoryLabId='+hdnTestFactoryLabId+'&resourcePoolId='+hdnResourcePoolId+'&shiftTypeId='+shiftTypeId+'&workDate='+hdnselectedDateValue;
	//callAlert("urlToGetDetails: "+urlToGetDetails);
	//showResult(urlToGetDetails);
	$(' #demandconfig').empty();
	 try{
		 if ($(' #demandconfig').length>0) {
			 $(' #demandconfig').jtable('destroy'); 
		 }		
		} catch(e) {}
	    $(' #demandconfig').jtable({
			title: "Resource Attendance",
	        selecting: true,  //Enable selecting 
	        editinline : {enable : true},
	        actions: {
	       	 	listAction: urlToGetDetails,
	        },  
	        fields : {
	        	resourceAvailabilityDetailsId: {
	                 key: true,
	                 list: false
	             },
	             resourcePoolName: {
		                title: 'Resource Pool',
		                edit:false,
		                list:true,
		                width: "10%"
				},
				resourceName: {
	                 title: 'Resource Name',
	                 edit:false,
	                 list:true,
	                 width: "10%"
	 			},
	 			shiftName: {
	                title: 'Shift Name',
	                edit:false,
	                list:true,
	                width: "10%"
				},
				availabilityForDate: {
	                title: 'Date',
	                edit:false,
	                list:true,
	                width: "10%"
				},
	        	}
			});
		$(' #demandconfig').jtable('load');
		
		var jscrolheight = $(" #demandconfig").height();
		var jscrolwidth = $(" #demandconfig").width();
		  
		$(".jScrollContainer").on("scroll", function() {
			 var lastScrollLeft=0;	
			 var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
			 if (lastScrollLeft < documentScrollLeft) {
			    	$(" #demandconfig").width($(".jtable").width()).height($(".jtable").height());			
			        lastScrollLeft = documentScrollLeft;
			  }else if(lastScrollLeft >= documentScrollLeft){
			    	$(" #demandconfig").width(jscrolwidth).height(jscrolheight);
			  }
		});
		loadPopup("div_PopupMainrc");
}

function showResult(urlToGetDetails){
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlToGetDetails,
		dataType : 'json',
		success : function(data) {
			callAlert(data);
		}
	}); 
}


/* Pop Up function begins */
function popupClose() {
	$("#div_PopupMain").fadeOut("normal");
	$("#div_PopupBackground").fadeOut("normal");
	var loaddiv = ' <div id="div_PopupMain" class="divPopUpMain" style="display:none;"> <div id="popclose" title="Press Esc to close" onclick="popupClose()" class="ImgPopupClose"> </div>  </div>';
	$("#div_PopupMain").replaceWith(loaddiv);
}

function popupCloserc() {	
	$("#div_PopupMainrc").fadeOut("normal");
	$("#div_PopupBackground").fadeOut("normal");
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
	if (evt.keyCode == 27) { 					//----- keyCode 27 - escape key.
		if (document.getElementById("div_PopupMain").style.display == 'block') {
			popupClose();
		}
	}
};

/** Pop up Function Ends**/
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>