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
<link href="js/Scripts/validationEngine/validationEngine.jquery.css" rel="stylesheet" type="text/css" />
<link href="css/customStyle.css" rel="stylesheet" type="text/css">
<!-- END THEME STYLES -->
<style>
.logo{
   margin-left: 45px;
}
div.radio{
	float:left;
}
</style>
</head>
<!-- END HEAD -->
<body>
<!-- BEGIN HEADER -->
<div class="page-header">
	<!-- BEGIN HEADER TOP -->
	<div><%@include file="headerlayout.jsp" %></div>			
	<!-- END HEADER TOP -->
	<!-- BEGIN HEADER MENU -->
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
								<span class="caption-subject theme-font bold uppercase">MANAGE BULK PUSH</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
							</div>
								<button type="button" id="syncButton" class="btn green-haze" style="float: right;">Sync DB</button>
							
						</div>
						<div class="portlet-body">
							<div class="row list-separated">		
								<div class="col-md-3">
									<div><label class="control-label "><input type="checkbox" id="select_all" value="0" onchange="checkAll(this)"/>SELECT ALL</label></div>
									
									<div><label class="control-label "><input type="checkbox" value="1" onchange="checkAll(this)"/>Product</label></div>	
									
									<div><label class="control-label "><input type="checkbox" value="2" onchange="checkAll(this)"/>ProductVersion</label></div>	
									
									<div><label class="control-label "><input type="checkbox" value="3" onchange="checkAll(this)"/>Testcase</label></div>	
									
									<div><label class="control-label "><input type="checkbox" value="4" onchange="checkAll(this)"/>TestExecutionResult</label></div>	
									
									<div><label class="control-label "><input type="checkbox" value="5" onchange="checkAll(this)"/>Workpackage</label></div>
									
									<div><label class="control-label "><input type="checkbox" value="6" onchange="checkAll(this)"/>Defects</label></div>
									
									<div><label class="control-label "><input type="checkbox" value="7" onchange="checkAll(this)"/>ProductUserRole</label></div>	
									
									<div><label class="control-label"><input type="checkbox" value="8" onchange="checkAll(this)"/>TestRunJob</label></div>
									
									<div><label class="control-label"><input type="checkbox"  value="9" onchange="checkAll(this)"/>ProductBuild</label></div>
									<div><label class="control-label"><input type="checkbox"  value="10" onchange="checkAll(this)"/>TestFactory</label></div>	
									
									<div><label class="control-label"><input type="checkbox"  value="11" onchange="checkAll(this)"/>TestSteps</label></div>	
																		
									</div>
									
								<div class="col-md-3">
									
									<div><label class="control-label"><input type="checkbox"  value="12" onchange="checkAll(this)"/>ActivityTask</label></div>		
									
									<div><label class="control-label"><input type="checkbox"  value="13" onchange="checkAll(this)"/>Activity </label></div>	
									
									<div><label class="control-label"><input type="checkbox"  value="14" onchange="checkAll(this)"/>ActivityWorkpackage </label></div>	
									
									<div><label class="control-label"><input type="checkbox"  value="15" onchange="checkAll(this)"/>ProductTeamResource </label></div>	
									
									<div><label class="control-label"><input type="checkbox"  value="16" onchange="checkAll(this)"/>WorkFlow Events</label></div>	
									
									<div><label class="control-label"><input type="checkbox"  value="17" onchange="checkAll(this)"/>User List </label></div>	
									<div><label class="control-label"><input type="checkbox"  value="18" onchange="checkAll(this)"/>Clarification Tracker </label></div>
									<div><label class="control-label"><input type="checkbox"  value="19" onchange="checkAll(this)"/>Product Feature </label></div>
									<div><label class="control-label"><input type="checkbox"  value="20" onchange="checkAll(this)"/>Test Suites </label></div>
									<div><label class="control-label"><input type="checkbox"  value="21" onchange="checkAll(this)"/>Change Request </label></div>
									<div><label class="control-label"><input type="checkbox"  value="22" onchange="checkAll(this)"/>Workpackage Resource Demand </label></div>
									<div><label class="control-label"><input type="checkbox"  value="23" onchange="checkAll(this)"/>Workpackage Resource Reservation </label></div>
									<div><label class="control-label"><input type="checkbox"  value="24" onchange="checkAll(this)"/>Change Request Mapping </label></div>
									<div><label class="control-label"><input type="checkbox"  value="25" onchange="checkAll(this)"/>Defect Collection </label></div>
									<div><label class="control-label"><input type="checkbox"  value="26" onchange="checkAll(this)"/>Feature TestCase Mapping </label></div>
									</div>
								
								<!-- <div class="col-md-1">
								   <div class="form-group">
										<div class="radio-list">
											<label class="radio-inline">
											<input id="new" type="radio" name="selection" value="all" class="" checked"> &nbsp &nbsp All</label></br>
											<label class="radio-inline" style="padding-left:0px;">
											<input id="copy" type="radio" name="selection" value="date" class="" ></label>
										</div>
								  </div>
								</div>
								<div class="col-md-5" style="margin-top: 22px;margin-left: -71px;padding-left:0px;">
									<div class="form-group">
										<div class="col-md-9">
											<div class="input-group" id="planned_defaultrange">								
												<input type="text" class="form-control" id="planned_fromTodatepicker"/>
												<span class="input-group-btn">
												<button class="btn default date-range-toggle" type="button" style="height:34px"><i class="fa fa-calendar"></i></button>
												</span>
											</div>
										</div>	
									</div>
								</div>
								<div class="col-md-5" style="margin-top:45px;margin-left:10px;">
									<button type="button" id="save" class="btn green-haze" onclick="collection()" >Create</button> 
								</div> -->
								<div class="col-md-6">
									<div class="form">
										<div class="form-group">
											<div class="radio-list">
												<label class="radio-inline"><input id="allData" type="radio" name="selection" value="all" class="" checked"> &nbsp &nbsp All</label></br>
											</div>
										</div>
									  <div class="form-group" style="padding-top: 15px;">									
										<div class="radio-list"><label class="radio-inline"><input id="copy" type="radio" name="selection" value="date" class="" ></label>
										</div>
										<div class="input-group" id="planned_defaultrange" style="margin-left: 32px;margin-top: -25px;width:400px">								
										<input type="text" class="form-control" id="planned_fromTodatepicker">
											<span class="input-group-btn">
											<button class="btn default date-range-toggle" type="button" style="height:34px"><i class="fa fa-calendar"></i></button>
											</span>
										</div>									
									</div>			
									<div class="form-group"><button type="button" id="save" class="btn green-haze" onclick="collection()">Create</button>
									</div>
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


<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet" type="text/css"></link>

<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="js/moment.min.js" type="text/javascript"></script>
<script src="js/daterangepicker.js" type="text/javascript"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>


<script type="text/javascript">
var checkedValues=[];
	function checkAll(ele) {
		checkedValues = [];
		
	     //var checkboxes = document.getElementsByTagName('input');
	     var checkboxes = $(".control-label input");
	     var spanCheckboxes = $(".control-label span"); 
	     
	     if (ele.checked && ele.value ==0) {
	        checkedValues = checkboxSelectionHandler(checkboxes,spanCheckboxes, ele.checked);
			
	     }else if (!ele.checked && ele.value ==0) {
			checkedValues = checkboxSelectionHandler(checkboxes,spanCheckboxes, ele.checked);	         		  
			
		 }else {
			for (var i = 0; i < checkboxes.length; i++) {
	             if (checkboxes[i].type == 'checkbox') {					 					 
	                 if(spanCheckboxes.eq(i).hasClass('checked')){							
							checkedValues.push(checkboxes[i].value);
					 }
				}
			}		
	     }
		 console.log(checkedValues)
	 }
	 
	 function checkboxSelectionHandler(checkboxes, spanCheckboxes, value){
		  for (var i = 0; i < checkboxes.length; i++) {
	             if (checkboxes[i].type == 'checkbox') {					 					 				 
					if(value){
						 if(!spanCheckboxes.eq(i).hasClass('checked')){
							 spanCheckboxes.eq(i).addClass('checked');
						 }
						 checkedValues.push(checkboxes[i].value);
					}else{
						if(spanCheckboxes.eq(i).hasClass('checked')){
							spanCheckboxes.eq(i).removeClass('checked');
						}
						checkedValues = [];
					}
	             }				 
	         }
			 return checkedValues;
	 }


	jQuery(document).ready(function() {	
		   
		   ComponentsPickers.init();		   
		   $("#uniform-allData span").addClass('checked')
		 		   
		   $("#planned_fromTodatepicker").daterangepicker();
			
			$(function() {
				$( "#datepickerFrom" ).datepicker();
				$( "#datepickerTo" ).datepicker();
				
				$( "#datepickerFrom" ).change(function() {
				});
				
				
				$( "#datepickerTo" ).change(function() {
				});
				
			});
		   
	});
	
	$(document).on("click",".daterangepicker .applyBtn", function(e) {
		$("#hdnDateSet").val("yes");
	});
	
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
	
	function collection(){
		
		if(checkedValues.length==0){
			callAlert("Please select minimum one collection for process");
			return false;
		}
				
		var dateRange='';
		var startRange="";
		var endRange="";
		if($("#uniform-copy span").hasClass('checked')){
			if($("#planned_fromTodatepicker").val() != ''){
				dateRange = '&startDate='+DaterangePicker.fromDate()+'&endDate='+DaterangePicker.toDate();
			}else{
				callAlert("Please select data range");
				return false;
			}
				
		}else{
			dateRange = '&startDate='+startRange+'&endDate='+endRange;
		}
		
		
		openLoaderIcon();
		 $.ajax({
	         type: "POST",
	   		contentType: "application/json; charset=utf-8",
	         url :'administration.bulk.push.from.sqlTo.MongoDB?collectionType='+checkedValues+''+dateRange,
	         dataType : 'json',
	         success : function(data) {
	                if(data.Result=="ERROR"){
	                	closeLoaderIcon();
	                      callAlert(data.Message);
	                return false;
			         }else{
			        	 closeLoaderIcon();
			        	 if(data.Result=="OK"){
			        		 callAlert("Successfully collection  created");
			        	 }
		         	}
	               
	         }
	  });
	}
	
	$('#syncButton').click(function () {	
		
   		 openLoaderIcon();
   		var urltoSave="sync.db.backup.restore";
   		$.ajax(
		{
		    type: "POST",
		    url : urltoSave,
		    cache:false,
		    success: function(data) {
		    	closeLoaderIcon();
		    	callAlert("DB Synchronized Successfully...");  
		    	//location.reload();
			},
	 		error : function(data){					
	 				closeLoaderIcon();
	 				callAlert("Error...");
	 				//location.reload();
	 			}
			}
		);  
		
		console.log("*******ends*********");
  	});
</script>						
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>