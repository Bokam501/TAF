<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@page import="com.hcl.atf.taf.controller.HomePageController"%>
<%@page import="com.hcl.atf.taf.model.UserList"%>
<%@page import="java.util.List"%>

<!DOCTYPE html>
<!-- 
Template Name: Metronic - Responsive Admin Dashboard Template build with Twitter Bootstrap 3.3.4
Version: 3.9.0
Author: KeenThemesContact: support@keenthemes.com
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
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="files/lib/metronic/theme/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css">
<!-- END GLOBAL MANDATORY STYLES -->
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet" type="text/css"></link>

<!-- BEGIN PAGE STYLES -->
<link href="files/lib/metronic/theme/assets/admin/pages/css/tasks.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">
<!-- END PAGE STYLES -->
<!-- BEGIN THEME STYLES -->
<link href="files/lib/metronic/theme/assets/global/css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/global/css/plugins.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/admin/layout3/css/layout.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/admin/layout3/css/themes/blue-steel.css" rel="stylesheet" type="text/css" id="style_color">
<link href="files/lib/metronic/theme/assets/admin/layout3/css/custom.css" rel="stylesheet" type="text/css">

<link href="css/metrojs.min.css" rel="stylesheet" type="text/css">
<link href="css/customStyle.css" rel="stylesheet" type="text/css">

<!-- END THEME STYLES -->

<spring:eval var="dashboardDateFilterVisible" expression="@ilcmProps.getProperty('DASHBOARD_DATE_FILTER_VISIBLE')" />

<spring:eval var="kibanaReportVisible" expression="@ilcmProps.getProperty('KIBANA_REPORT_VISIBLE')" />

<style>
.progressMetric{
	 margin: 0px; 
	 position: absolute;
	 width: 50px; 
	 display: block; 
	 overflow: hidden;
	 height: 8px;
}

.percentMetric{
  color: #4db3a4;
  font-weight: 600;
  font-size:12px;
}

.percentMetricFont{
 font-size:12px;
}

.navbar-static-top{
	display: none !important;
}

.inline-form{
	display: none !important;
}

.dashboard-container navbar{
	display: none !important;
}

.maximiseScreen{};


</style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
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
	<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>
	<div><%@include file="dataTableHeader.jsp" %></div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->

<div class="dashboardContainerResponsive">
	<div class="form-group">
	    <div id="loadingIconTM" style="display:none;z-index:100001;position:absolute;top:20px;left:50%">			
	    	<img src="css/images/ajax-loader.gif"/>
		</div>		
		<div><div id="buildReport">
			<%@include file="buildInformation.jsp"%></div>
		</div>
		<div id="metricsReport" class="row hidden">								
				<div>						
					<div class="portlet light " style="margin-top: 1%; height:48px;">						
						<div class="col-md-7">
     						<ul class="nav nav-tabs" id="tablistWP">    </ul>							
						</div>						
						<div class="tab-content wrkPckgTabCntnt" id="tablistWPContent" style="padding-left: 15px;padding-right: 15px;">						
					    <!-- <div class="col-md-1" style="margin-left : 53px;">
					      <select class="form-control input-small select2me" id="metricsDataPeriod_ul" style="display:none;">
					        <option value="0">Daily</option>
					        <option value="1">Weekly</option>
					        <option value="2" selected="selected">Monthly</option>
					        <option value="3">Quaterly</option>
					        <option value="4">Yearly</option>
					      </select>
					    </div> --> 
					    <div class="col-md-5" id="planned_defaultrangeDisable" style="margin-left: -32px;">
					    	<div class="input-group col-md-7" id="planned_defaultrange" style="float: left;">								
								<input type="text" class="form-control" id="planned_fromTodatepicker">
								<span class="input-group-btn">
								<button class="btn default date-range-toggle" type="button" style="height:34px"><i class="fa fa-calendar"></i></button>
								</span>
						  </div>
					    
						<div id="productfilterContainer" class="col-md-1" style="margin-left : 0px">
						    <label>Engagement </label>
						    <select class="form-control input-small select2me" id="testFactoryfilter_ul">
						         <option value="0" selected="selected">ALL</option>
						    </select>
						    <label>Product </label>
						    <select class="form-control input-small select2me" id="productfilter_ul">
						         <option value="ALL" selected="selected">ALL</option>
						    </select>
						</div> 
					 </div>
				 </div>
				 
					
				<div class="portlet light ">
					<div class="portlet-title" style="padding-left: 35%;">
						<div class="caption">
							<i class="font-green-sharp"></i>
							<span class="caption-subject font-green-sharp bold uppercase"></span>
						</div>	
					    <div class="actions">
					        <!-- <a class="btn btn-circle btn-icon-only btn-default dropdown-toggle" onclick="hideTrendHandler();" title="Trend"><i class="icon-logout"></i></a> -->
					        <a class="btn btn-circle btn-icon-only btn-default dropdown-toggle" onclick="hideTrendHandler();" title="Trend">
					        <img src="css/images/trendChart.png" class="fontIconImagePosition" alt="NoImage"></img></a>
							<a class="btn btn-circle btn-icon-only btn-default icon-bar-chart" style="margin-right: -32px; margin-top: -115px;" href="javascript:;" onclick="dashboardHandler();" data-original-title="" title="Dashboard"></a>
							<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" onclick="fullScreenIFrameHandler();" data-original-title="" title="FullScreen"></a>
						</div>
					</div>
					<div class="portlet-body" style="padding-left: 10px;display: inline-flex;width: 100%;">
						<div id="driverMetricsTable" class="table-scrollable table-scrollable-borderless"  style="width: 90%;padding-left: 10%"></div>
						<div id="loadingChart" class="table-scrollable table-scrollable-borderless" style="width: 100%; display:none">													
						</div>
						<div id="dashboardTile" class="dashboardContainerResponsive" style="display:none;position: absolute;top: 195px;width: 95%;height: 65%;">
							<div class="form-group col-lg-12">								
								<div id="overallStatusReport" style="display:none; margin:10px; background-color: #eff3f8; border-style: ridge; padding: 20px;"></div>
							</div>    
						</div>											
					</div>					
				</div>
			  </div>														
			</div>				
		</div>		
    </div>    
</div>

<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp" %></div>			
<!-- END FOOTER -->

<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="js/moment.min.js" type="text/javascript"></script>
<script src="js/daterangepicker.js" type="text/javascript"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.bootstrap.wizard.min.js"></script>

<script>

var kibanaReportVisible ='<c:out value="${kibanaReportVisible}"/>';
var pageType="HOMEPAGE";
var recentBuildFlag;
var testsuitesTabFlag=false;
var homeMetrics= "homeMetrics";
var slaMetrics = "slaMetrics";
var dashboardLayout = "dashboard";
var dashboardTileView = "dashboardTileView";
var workflowSummaryView ="workflowSummaryView";
var workPackageRAGView="workPackageRAGView";

var isFirstTime = true;
var currentTabId = 0;
var currentTabHref = "";
var urlSummary="";
var summaryPeriodValue="";
var metricsDataPeriodValue="";
var producMetricsDataValue="";

var summaryURLs = ["dashboard.metrics.summary.calculation",
                   "sla.dashboard.metrics.summary.calculation",
                   "standard.dashboard.summary.calculation",
                   "workpackage.for.testmanager",
                   "productList.based.user.loged.in",
				   "workflow.status.indicator.product.summary","workpackageRAG.View.summary"];
var slaURLs = [];

var slaRecords = {};

var loadWorkPackage = false;
var loadProduct = false;
var DaterangePicker='';

var plannedStartDate="";		
var plannedStartDateFormat="";
var plannedEndDate="";		
var plannedEndDateFormat="";

var selectedTestFactoryId = 0;
var selectedTestFactoryName = "ALL";

var dashBoardDateFilter="${dashboardDateFilterVisible}";

function cb(start, end) {
	$('#planned_fromTodatepicker').val(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
} 

jQuery(document).ready(function() {
	if(kibanaReportVisible=="YES"){
		$('#buildReport').addClass('hidden');
		$('#metricsReport').removeClass('hidden');
	}else{
		$('#metricsReport').addClass('hidden');
		$('#buildReport').removeClass('hidden');
	}
   $("#menuList li:first-child").eq(0).remove();   // to remove fancytree icon near home tab
    ComponentsPickers.init();
   
    DaterangePicker = {
		fromDate : function(){
			var datepickerStartDate = $(".daterangepicker_start_input>input").val();
			datepickerStartDate = ((typeof datepickerStartDate == "undefined") || (datepickerStartDate == ''))? new Date() : new Date(datepickerStartDate);
			datepickerStartDate = (datepickerStartDate.getMonth() + 1) + "/" + datepickerStartDate.getDate() + "/" + datepickerStartDate.getFullYear();
			return datepickerStartDate;
		},
		toDate : function(){
			var datepickerEndDate = $(".daterangepicker_end_input>input").val();
			datepickerEndDate = ((typeof datepickerEndDate == "undefined") || (datepickerEndDate == '')) ? new Date() : new Date(datepickerEndDate);
			datepickerEndDate = (datepickerEndDate.getMonth() + 1) + "/" + datepickerEndDate.getDate() + "/" + datepickerEndDate.getFullYear();		
			return datepickerEndDate;
		}
    } 
    
    plannedStartDate = DaterangePicker.fromDate();
    plannedEndDate = DaterangePicker.toDate();
    
    cb(moment().subtract('days', 30), moment()); 
        
    // ----- For Reference use -----
	//getMetricsData("sla.dashboard.metrics.summary.calculation", homeMetrics);
	//getMetricsData("dashboard.metrics.summary.calculation", homeMetrics);   
	//getMetricsData("dashboard.metrics.sla.calculation", slaMetrics);

	getMetricsData("administration.dashboardTabRoleBased.list?tabId=-1&jtStartIndex=0&jtPageSize=10", dashboardLayout);
		
	/* $(document).on('change','#metricsDataPeriod_ul', function() {
		metricsDataPeriodValue = $("#metricsDataPeriod_ul").find('option:selected').val();
		producMetricsDataValue = $("#productfilter_ul").find('option:selected').val();
	    metricsDataPeriod(producMetricsDataValue,metricsDataPeriodValue);
	}); */
		
	$(document).on("click",".daterangepicker .applyBtn", function(e) {
		 
	 	 plannedStartDate=DaterangePicker.fromDate();		//document.getElementById("plannedStartDate").value;
		 plannedStartDateFormat=new Date(plannedStartDate);
		 plannedEndDate=DaterangePicker.toDate();		//document.getElementById("plannedEndDate").value;
		 plannedEndDateFormat=new Date(plannedEndDate);
		producMetricsDataValue = $("#productfilter_ul").find('option:selected').val();
		metricsDataPeriod(plannedStartDate,plannedEndDate,producMetricsDataValue, selectedTestFactoryName);
		
		console.log("plannedStartDateFormat :"+plannedStartDateFormat);
		console.log("plannedEndDateFormat :"+plannedEndDateFormat);
});
	
	$(document).on('change','#productfilter_ul', function() {
		producMetricsDataValue = $("#productfilter_ul").find('option:selected').val();
		metricsDataPeriod(plannedStartDate,plannedEndDate,producMetricsDataValue, selectedTestFactoryName);
	});   
	
	$(document).on('change','#testFactoryfilter_ul', function() {
		selectedTestFactoryId = $("#testFactoryfilter_ul").find('option:selected').val();
		selectedTestFactoryName = $("#testFactoryfilter_ul").find('option:selected').text();
		if(selectedTestFactoryId == 0){
			productDetails = [];
			getProductDropDown();
		}else{
			getProductsOfTestfactory(selectedTestFactoryId);
		}
		producMetricsDataValue = $("#productfilter_ul").find('option:selected').val();
		metricsDataPeriod(plannedStartDate,plannedEndDate,producMetricsDataValue, selectedTestFactoryName);
	}); 
	
	if(dashBoardDateFilter == 'yes') {   	
    	$('#planned_defaultrangeDisable').show();
    } else {   	
    	$('#planned_defaultrangeDisable').hide();
    }
	  	  
});

var graphArray=[];
var urlData="";
function displayDefaultGraph(recordsArray){	
	var tabList='';
	var tabContent='';	
	graphArray = recordsArray;
	
	for(var i=0; i<recordsArray.length; i++){
		if(i == 0){
			tabList +='<li class="active"><a id="'+(i)+'" href="#'+recordsArray[i].dashBoardTabsName+'" onclick="displayGraph(event)" data-toggle="tab">'+recordsArray[i].dashBoardTabsName+'</a></li>';		
			tabContent += '<div id="'+recordsArray[i].dashBoardTabsName+'" class="tab-pane fade active in"></div>';
		}else{
			tabList +='<li class=""><a id="'+(i)+'" href="#'+recordsArray[i].dashBoardTabsName+'" onclick="displayGraph(event)" data-toggle="tab">'+recordsArray[i].dashBoardTabsName+'</a></li>';		
			tabContent += '<div id="'+recordsArray[i].dashBoardTabsName+'" class="tab-pane fade"></div>';
		}
	}
	$("#metricsReport #tablistWP").html(tabList);
	$("#tablistWPContent").html(tabContent);
	$('#0').trigger('click');	
}

function displayGraph(event){
	
	loadWorkPackage = false;
	loadProduct = false;

	var value = parseInt(event.target.id);
	var hrefName = event.target.href;
	currentTabId = value;
	currentTabHref = hrefName;
	
	$("#driverMetricsTable").show();
	$(".portlet-title span").show();
	$("#loadingChart").hide();
	//if(hrefName.toLowerCase().indexOf('#summary') != -1){		
	if($.inArray(graphArray[value].url, summaryURLs) != -1){		
			 	urlData=graphArray[value].url;	
	 		if("workpackage.for.testmanager" == graphArray[value].url || "productList.based.user.loged.in" == graphArray[value].url || 
	 				"workflow.status.indicator.product.summary" == graphArray[value].url || "workpackageRAG.View.summary" == graphArray[value].url){
				//$("#metricsDataPeriod_ul").hide();
	 			$("#productfilterContainer").hide();
				$("#planned_defaultrangeDisable").hide();
				
	 			$(".portlet-title span").html("PROJECT STATUS DASHBOARD");				
	 			$("#driverMetricsTable").hide();
				$('#overallStatusReport').empty();
				//$('#tileBrick1').empty();	 			
	 			$('#dashboardTile').show();
	 			
				if("workpackage.for.testmanager" == graphArray[value].url){
					loadWorkPackage = true;
					getTotalWPCount(graphArray[value].url);					
					
				}else if("productList.based.user.loged.in" == graphArray[value].url) {
					loadProduct = true;
					getProductIds(graphArray[value].url,dashboardTileView);
					
				}else if("workflow.status.indicator.product.summary" == graphArray[value].url){					
					var productIdurl="productList.based.user.loged.in";
					loadProduct = true;
					$(".portlet-title span").hide();
					getProductIds(productIdurl,workflowSummaryView);					
				}else if("workpackageRAG.View.summary" == graphArray[value].url){					
					var productIdurl="productList.based.user.loged.in";
					loadProduct = true;
					$(".portlet-title span").hide();
					getProductIds(productIdurl,workPackageRAGView);					
				}

	 		}else{	
				$('#dashboardTile').hide();
				$("#loadingIconTM").hide();
				if($.inArray(graphArray[value].url, slaURLs) == -1){ 		
					$("#productfilterContainer").show();
					$("#planned_defaultrangeDisable").show();
					$(".portlet-title span").html("SLA DRIVEN OPERATIONAL METRIC BOARD FOR PRODUCTS");
					//summaryPeriodValue=$("#metricsDataPeriod_ul").find('option:selected').val();
					urlSummary=graphArray[value].url;
					metricsDataPeriodValue= $("#metricsDataPeriod_ul").find('option:selected').val();
					producMetricsDataValue = $("#productfilter_ul").find('option:selected').val();
					slaURLs.push(graphArray[value].url);
					getMetricsData(urlSummary+"?startDate="+DaterangePicker.fromDate()+"&endDate="+DaterangePicker.toDate()+"&productName="+producMetricsDataValue, homeMetrics);
					$("#loadingIconTM").hide();
					
				}else{
					$("#productfilterContainer").show();
					//$("#metricsDataPeriod_ul").show();
					$("#planned_defaultrangeDisable").show();
					urlSummary=graphArray[value].url.replace(/\./g,"");
					populateTilesView(slaRecords[urlSummary]);
				}				
	 		}	 				
	}else{
	
		urlData=graphArray[value].url;
		$('#dashboardTile').hide();
		$("#loadingIconTM").hide();
		$("#driverMetricsTable").hide();
		(kibanaReportVisible=="YES") ? $(".portlet-title span").hide() : $(".portlet-title span").show(); 

		$("#loadingChart").show();
		//$("#metricsDataPeriod_ul").show();
		$("#productfilterContainer").hide();
		if((value) < graphArray.length){
			//loadCharts(graphArray[value].url);
			//metricsDataPeriodValue = $("#metricsDataPeriod_ul").find('option:selected').val();
		    metricsDataPeriod(DaterangePicker.fromDate(),DaterangePicker.toDate(),null, null);
		}
	}
	
	if(dashBoardDateFilter == 'yes') {
    	$('#planned_defaultrangeDisable').show();    	
    } else {
    	$('#planned_defaultrangeDisable').hide();    	
    }
	
}

var totalItem = '';
var itemValue = '';
var idValue = '';
var idClassName = '';
var itemValueArr = [];
 function loadCharts(url2){
		var contextpath = (window.location.pathname).split("/", 2);
		var root = window.location.protocol	+ "//"+ window.location.host+ "/"+ contextpath[1];
		// ----- if dashboard is in another server -----
	 	root = '';
		
		var iFrm = '<iframe id="dashBoardMetricsFrameID" src="'+root+''+url2+'"&output=embed frameborder="0" allowfullscreen style="position:absolute; top:155px; width:95%; height:65%;"></iframe>'
		$('#loadingChart').html(iFrm);
		
		document.getElementById("dashBoardMetricsFrameID").onload = function() {					
			setTimeout( function(){				
				$("#dashBoardMetricsFrameID").contents().find('.application .app-container navbar').eq(1).addClass("ng-hide");				
				
				var maximize = '<a ng-show="!appEmbedded" class="ng-show" style="cursor: pointer;"><i class="fa fa-expand maximiseScreen"></i></a>';				
				var anch = $("#dashBoardMetricsFrameID").contents().find('.panel-heading .btn-group');
				anch.html(maximize);
				
				var arr = [];
				arr = document.getElementById("dashBoardMetricsFrameID").contentWindow.document.getElementsByClassName("maximiseScreen");
				
				for(var i=0; i<arr.length; i++){
					arr[i].id = i;
					arr[i].addEventListener("click", functionToRun);
				}				
				totalItem = $("#dashBoardMetricsFrameID").contents().find('.gridster > li');
				console.log('myframe is loaded');
			},5000);
		};		
 }
 
var totalURL="";
 function metricsDataPeriod(plannedStartDateFormat,plannedEndDateFormat,producMetricsDataValue, testFactoryMetricsDataValue){
	 //if(currentTabHref.toLowerCase().indexOf('#summary') == -1){
		 
	   if($.inArray(urlData, summaryURLs) == -1){
		   if(dashBoardDateFilter == 'yes') {   	
		    	$('#planned_defaultrangeDisable').show();
		    } else {   	
		    	$('#planned_defaultrangeDisable').hide();
		    }
		var originalURL = graphArray[currentTabId].url;
		var urlFrontSplit = originalURL.split("time:(")[0]+"time:(";
		var urlBehindSplit = originalURL.split("time:(")[1].substring(originalURL.split("time:(")[1].indexOf(")"));
		
	/*	var urlTimePortion = "from:now%2Fd,mode:quick,to:now%2Fd";
		if(metricsDataPeriodValue == 1){
			urlTimePortion = "from:now%2Fw,mode:quick,to:now%2Fw";
		}else if(metricsDataPeriodValue == 2){
			urlTimePortion = "from:now%2FM,mode:quick,to:now%2FM";
		}else if(metricsDataPeriodValue == 3){
			urlTimePortion = "from:now-90d,mode:quick,to:now";
		}else if(metricsDataPeriodValue == 4){
			urlTimePortion = "from:now%2Fy,mode:quick,to:now%2Fy";
		} */
			
		plannedStartDateFormat = getKibanaDateFormat(plannedStartDateFormat); 
		plannedEndDateFormat = getKibanaDateFormat(plannedEndDateFormat);

// 		from:'2011-01-01T14:30:37.020Z',mode:absolute,to:'2016-06-30T14:30:37.020Z' 		
		var urlTimePortion = "from:'"+plannedStartDateFormat+"',mode:absolute,to:'"+plannedEndDateFormat+"'";
		totalURL=urlFrontSplit+urlTimePortion+urlBehindSplit;
		loadCharts(totalURL);		
	}
	 else{
		 getMetricsData(urlData+"?startDate="+plannedStartDateFormat+"&endDate="+plannedEndDateFormat+"&testFactoryName="+testFactoryMetricsDataValue+"&productName="+producMetricsDataValue, homeMetrics);
	 }
 }
 
 function getKibanaDateFormat(sourceDateFormat){
	var currentTime = new Date();
		
	var sourceDate = new Date(sourceDateFormat);
	sourceDate.setHours(currentTime.getHours(), currentTime.getMinutes(), currentTime.getSeconds(), currentTime.getMilliseconds());
		
	var kibanaDateFormat = setLeadingZeros(4, sourceDate.getUTCFullYear())
	+"-"+setLeadingZeros(2, (sourceDate.getUTCMonth()+1))
	+"-"+setLeadingZeros(2, sourceDate.getUTCDate())
	+"T"+setLeadingZeros(2, sourceDate.getUTCHours())
	+":"+setLeadingZeros(2, sourceDate.getUTCMinutes())
	+":"+setLeadingZeros(2, sourceDate.getUTCSeconds())
	+"."+setLeadingZeros(3, sourceDate.getUTCMilliseconds())+"Z";
	
	return kibanaDateFormat;
 }
 
 function setLeadingZeros(maxSize, value){
	if(value.toString().length < maxSize){
		for(var i = value.toString().length; i < maxSize; i++){
			value = "0"+value;
		}
	}
	return value;
 }
 
 function functionToRun(){
	 console.log("chartID :", this.id," - ",this.className);	
	
	if(this.className == 'fa fa-expand maximiseScreen'){
		this.className = 'fa fa-compress';  
		idValue = parseInt(this.id);
		idClassName = this.className;
		itemValue = $("#dashBoardMetricsFrameID").contents().find('.gridster > li').eq(idValue);	
		$("#dashBoardMetricsFrameID").contents().find('.gridster').html(itemValue);
		
		itemValueArr[0] = $("#dashBoardMetricsFrameID").contents().find('.gridster li').eq(0).attr('data-row');
		itemValueArr[1] = $("#dashBoardMetricsFrameID").contents().find('.gridster li').eq(0).attr('data-col');
		itemValueArr[2] = $("#dashBoardMetricsFrameID").contents().find('.gridster li').eq(0).attr('data-sizex');
		itemValueArr[3] = $("#dashBoardMetricsFrameID").contents().find('.gridster li').eq(0).attr('data-sizey');
		
		var defaultValue=0;		
		$("#dashBoardMetricsFrameID").contents().find('.gridster li').eq(defaultValue).attr('data-row','1');
		$("#dashBoardMetricsFrameID").contents().find('.gridster li').eq(defaultValue).attr('data-col','1');
		$("#dashBoardMetricsFrameID").contents().find('.gridster li').eq(defaultValue).attr('data-sizex','12');
		$("#dashBoardMetricsFrameID").contents().find('.gridster li').eq(defaultValue).attr('data-sizey','12'); 
	}else{
		//this.className = 'fa fa-expand';
		
		$("#dashBoardMetricsFrameID").contents().find('.gridster').html(totalItem);
		
		if(this.className != undefined){
			this.className = 'fa fa-expand maximiseScreen';
		}else{
			var temp = $("#dashBoardMetricsFrameID").contents().find('.gridster > li').eq(idValue);
			if(temp.find('.panel .panel-heading .btn-group a i').hasClass('fa-compress')){
				temp.find('.panel .panel-heading .btn-group a i').removeClass('fa-compress');
				temp.find('.panel .panel-heading .btn-group a i').addClass('fa-expand');
			}
		}	
		
		$("#dashBoardMetricsFrameID").contents().find('.gridster li').eq(idValue).attr('data-row', itemValueArr[0]);
		$("#dashBoardMetricsFrameID").contents().find('.gridster li').eq(idValue).attr('data-col', itemValueArr[1]);
		$("#dashBoardMetricsFrameID").contents().find('.gridster li').eq(idValue).attr('data-sizex', itemValueArr[2]);
		$("#dashBoardMetricsFrameID").contents().find('.gridster li').eq(idValue).attr('data-sizey', itemValueArr[3]);
		
	}	
 }
 
  
 function fullScreenIFrameHandler()
 {
	 var frame = document.getElementById('dashBoardMetricsFrameID');
	 //$("#dashBoardMetricsFrameID").contents().find('.visualize-chart .chart').css('pointer-events','none');
	 
	 if($("#metricsReport .portlet").hasClass('portlet-fullscreen')){
		 $("#dashboardTile").css('top','195px');
		 $("#dashboardTile").css('height','65%');
		 
		 frame.style.top = '155px';
		 frame.style.height = '65%';	 		 
		 
	 }else{
		 $("#dashboardTile").css('top','50px');
		 $("#dashboardTile").css('height','100%');
		 
		 frame.style.top = '0px';
		 frame.style.height = '100%';		 
	 }
	 if($("#dashBoardMetricsFrameID").contents().find('.gridster > li').length == 1){
		 functionToRun();
	 }		 
 } 
   var temp = new Array();
   var productName=new Array();
   var productDetails = [];
   var productDropDown="";
   var testFactoryDetails = [];
   var testFactoryDropDown = "";
   function getMetricsData(fileLocation, urlFunction) {
	   openLoaderIcon();
	   $.ajax({
	        type: "POST",
	        contentType: "application/json; charset=utf-8",
	        url: fileLocation,
	        dataType: 'json',
	        success: function(data) {
	        	closeLoaderIcon();	    		
	        },
	        error : function(data){
	    	   closeLoaderIcon();   
	        },
	       complete: function(data) {
	    	   closeLoaderIcon();
	    	   
	    	   if(data.responseJSON.Result=="ERROR"){
		    		callAlert(data.responseJSON.Message);
	 		    	return false;
		    	}else{

		       var records = data.responseJSON.Records;
	    	   if(urlFunction == homeMetrics){
				slaRecords[urlSummary.replace(/\./g,"")] = records;
	    	   	populateTilesView(data.responseJSON.Records);
    		   // peityGraph();
				
	    	   }
			   else if(urlFunction == slaMetrics){
				   displaySlaHandler(data.responseJSON.Record);
				   
			   }else if(urlFunction == dashboardLayout){				   
				   displayDefaultGraph(records);
				   var result=records;
				   var productDetail = [];
				   if(records != 'undefined' && records.length > 0){
				   		productDetails = records[0].productDetails.split(",");
				   		testFactoryDetails = records[0].testFactoryDetails.split(",");
				   }
				   getProductDropDown();
				   getTestFactoryDropDown();
				   //console.log("productDetails", productDetails)
					
				   }else if (urlFunction == workflowSummaryView) {
			   
				   listWorkflowInidcatorySummary();
			     }else if (urlFunction == workPackageRAGView) {
			    	
			    	 listWorkflowInidcatorySummary();
			     }
		       } 
	       }
	   });	   
   }
   
   function getProductsOfTestfactory(selectedTestFactoryId){
	   openLoaderIcon();
	   $.ajax({
	        type: "POST",
	        contentType: "application/json; charset=utf-8",
	        url: 'administration.products.by.test.factory.id.and.user?testFactoryId='+selectedTestFactoryId,
	        dataType: 'json',
	        success: function(data) {
	        	closeLoaderIcon();	    		
	        },
	        error : function(data){
	    	   closeLoaderIcon();   
	        },
	       complete: function(data) {
	    	   closeLoaderIcon();
	    	   if(data.responseJSON.Result=="ERROR"){
		    		callAlert(data.responseJSON.Message);
	 		    	return false;
		    	}else{
			       var records = data.responseJSON.Records;
			       if(records != 'undefined' && records.length > 0){
				   		productDetails = records[0].productDetails.split(",");
				   }
				   getProductDropDown();
		       } 
	       }
	   });
   }
  
   function getProductDropDown(){
	   $("#productfilter_ul").empty();
	   productDropDown = "<option value='ALL'>ALL</option>";
	   $.each(productDetails, function(i, productDetail){
			if(productDetail != 'undefined' && productDetail != null && productDetail != ''){
				productDropDown += "<option value='"+productDetail.split("~~~")[1]+"'>"+productDetail.split("~~~")[1]+"</option>";
			}
		});	
		$("#productfilter_ul").append(productDropDown);
		$("#s2id_productfilter_ul #select2-chosen-2").text("ALL");
		$("#productfilter_ul").change();
   }
   
   function getTestFactoryDropDown(){
	   testFactoryDropDown = "";
	   $.each(testFactoryDetails, function(i, testFactoryDetail){
			if(testFactoryDetail != 'undefined' && testFactoryDetail != null && testFactoryDetail != ''){
				testFactoryDropDown += "<option value='"+testFactoryDetail.split("~~~")[0]+"'>"+testFactoryDetail.split("~~~")[1]+"</option>";
			}
		});	
		$("#testFactoryfilter_ul").append(testFactoryDropDown);
   }
   
   function displaySlaHandler(data){	   
	   displayslaCirlce('#svID', data.scheduleVariance, data.scheduleVarianceTarget, "lesser");
	   displayslaCirlce('#qiID', data.productQualityIndex, data.productQualityIndexTarget, "greater");
	   displayslaCirlce('#riID', data.riskIndex, data.riskIndexTarget, "lesser");
	   displayslaCirlce('#uiID', data.utlizationIndex, data.utlizationIndexTarget, "greater");
	   displayslaCirlce('#healthIndexID', data.healthIndex, data.healthIndexTarget, "greater");
	   
   }
   
   function displayslaCirlce(id, actual, target, value){	   
	   if($(id).find('i').hasClass('dashboardMetricsBtnGreen')){
		   $(id).find('i').removeClass('dashboardMetricsBtnGreen');
	   }
	   if($(id).find('i').hasClass('dashboardMetricsBtnRed')){
		   $(id).find('i').removeClass('dashboardMetricsBtnRed');
	   }
	   if($(id).find('i').hasClass('dashboardMetricsBtnYellow')){
		   $(id).find('i').removeClass('dashboardMetricsBtnYellow');
	   }	   
	   
	   $(id).attr("title","Actual : "+actual+" / Target :"+target);
	   
	   if(value == "greater"){
		   if(actual < target){
			   $(id).find('i').addClass('dashboardMetricsBtnGreen');
		   }else{
			   $(id).find('i').addClass('dashboardMetricsBtnRed');
		   }		   
	   }else{
		   if(actual > target){
			   $(id).find('i').addClass('dashboardMetricsBtnRed');
		   }else{
			   $(id).find('i').addClass('dashboardMetricsBtnGreen');
		   }
	   }
	   	   
	   var colorValue = $(id).find('i').css('background-color');
	   $(id).find('span').css('color',colorValue);	    
	   $(id).find('span').text("["+actual+"]");
	   
	   //$(id).find('i').addClass('dashboardMetricsBtnYellow');
   }
   
   function dashboardHandler(){
	   if($("#metricsReport #tablistWP > li").length>1){
	   		$("#metricsReport #tablistWP > li").eq(1).find('a').trigger('click')
	   }
   }
         
   function hideTrendHandler(){
	   var rowHideValueHeader = $("#driverMetricsTable table thead tr th");
	   var rowHideValue = $("#driverMetricsTable table tbody tr")
	   
	   var flag=true;	   
	   if($("#driverMetricsTable table thead tr th").eq(3).css('display') == "none")
		   flag = false;
	   
	   for(var i=3;i<rowHideValueHeader.length;i++){
			if(flag)   
		   		$("#driverMetricsTable table thead tr th").eq(i).hide();
			else
				$("#driverMetricsTable table thead tr th").eq(i).show();
	   }
   
	   for(var i=0;i<rowHideValue.length;i++){
		   var columnHideValue = $("#driverMetricsTable table tbody tr").eq(i);   
		   var columnHideLengthValue = $(columnHideValue).find('td').length;
		   
		   for(var j=0;j<columnHideLengthValue;j++){
			   if(columnHideLengthValue>2 && j>2){		   
				   if(flag) 
				   		$(columnHideValue).find('td').eq(j).hide();
				   else
					   $(columnHideValue).find('td').eq(j).show();
			   }
		   }
	   }
   }
   
   function flagDisplayColor(actualVal){
	   var flagColor='';
	   if(actualVal == "Red"){
			flagColor = "badge-danger";
		}else if(actualVal == "Yellow"){
			flagColor = "badge-warning";
		}else if(actualVal == 'Green'){
			flagColor = "badge-success";
		}	
	   return flagColor;
   }
   function populateTilesView(data){	   
		if(data.length>0){
		$("#driverMetricsTable").empty();
		
		var groupValuesAtFirstIndex = data[0].GroupValues;
		
		if(urlData=="sla.dashboard.metrics.summary.calculation"){
			var table = '<table class="table table-hover table-light">'
					+'<thead><tr class="uppercase">'+
					'<th style="width:35%">CORE METRICS</th>'+
					'<th style="width:35%">TARGET</th>'+
					'<th style="width:30%">ACTUAL</th>'+
					'<th style="width:30%">'+groupValuesAtFirstIndex[0].trendMetrics[0].month+'</th>'+
					'<th style="width:30%">'+groupValuesAtFirstIndex[0].trendMetrics[1].month+'</th>'+
					'<th style="width:30%">'+groupValuesAtFirstIndex[0].trendMetrics[2].month+'</th>'+
					'</thead>'
					+'<tbody>';
			
			
		}else{
					var table = '<table class="table table-hover table-light">'
					+'<thead><tr class="uppercase">'+
					'<th style="width:35%">CORE METRICS</th>'+
					'<th style="width:35%">TARGET</th>'+
					'<th style="width:30%">ACTUAL</th>'+
					'</thead>'
					+'<tbody>';
		}
		
					
		var peityArrMain=[];
		for(var i=0;i<data.length;i++){			
			
			var groupValues = data[i].GroupValues;
			var headerActualValue = data[i].ActualValue;
			if(typeof headerActualValue == 'undefined' || headerActualValue == 'null' || headerActualValue == null){
				headerActualValue = "";
				data[i].Indicator = "";
			}
			table +='<tr><td style="color:#4DB3A7; font-weight:600; font-size: 16px;">'+getHeaderProgressBarTarget(headerActualValue, flagDisplayColor(data[i].Indicator), data[i].GroupName)+'</a></td><td></td><td></td></tr>';
			
			var actualVal = '';
			var flagColor="";
			var targetRange='';
			for(var j=0;j<groupValues.length;j++){			
				var valueLeft,valueMiddle,valueRight="";
				actualVal = groupValues[j].indicator;
				flagColor="";
				targetRange= groupValues[j].targetRange.split('~');
							
				valueLeft = targetRange[0];
				valueMiddle = targetRange[1];
				valueRight = targetRange[2];	
					
				flagColor = flagDisplayColor(actualVal);					
				table +='<tr><td style="color: #4DB3A2; font-weight: 600; padding-left:25px;">'+groupValues[j].metricsName+'</a></td>'+
				'<td>'+getMetricsTargetBar(valueLeft,valueMiddle,valueRight,groupValues[j].isTargetAvailable)+'</td>'+
				'<td>'+getProgressBarTarget(groupValues[j].actualValue, flagColor)+'</td>';
				
				 var trendValue='';
				 var pietyArr=[];
				 var colorChange='';
				 for(var k=0;k<groupValues[0].trendMetrics.length;k++){
					 if(groupValues[j].trendMetrics.length <= k){
						 trendValue += '<td></td>';
					 }else{
					 	trendValue += '<td>'+getProgressBarTarget(groupValues[j].trendMetrics[k].actualValue, flagDisplayColor(groupValues[j].trendMetrics[k].indicator))+'</td>';
					 	colorChange = (groupValues[j].trendMetrics[k].indicator).toLowerCase();
					 	
						if((groupValues[j].trendMetrics[k].indicator).toLowerCase() == "yellow")
					 		colorChange = 'orange';
						
					 	pietyArr.push(colorChange);
					 }
				}
				peityArrMain.push(pietyArr);
				table += trendValue;
				table += '<td></td>';
				/* table += '<td></td>'; */
				table += '<td>'+getVariantsCharts(groupValues[j].trendMetrics)+'</td></tr>';				
			}
		  }
		}
		table+= '</tbody></table>';					
		$("#driverMetricsTable").append(table);
		 
		for(var i=0;i<peityArrMain.length;i++){
			peityGraph(peityArrMain[i], i);
		}
  }
   
   function peityGraph(fillArr, value){
	   //$(".line").peity("line",{ width: 64, height: 32});
	   
	   console.log("fillArr "+fillArr, value);	   
	   $(".line").eq(value).peity("bar", {
		   fill: fillArr,
		   height:16,
		   width:24,
		 });   
	}
   
   function getVariantsCharts(trendArr){
	 var progress='';
	 var valueArr=[];
	 for(var i=0;i<trendArr.length;i++){
		 valueArr.push(trendArr[i].actualValue);
	 }
	 var resultValue= valueArr.toString();
   	  progress = '<div><p><span class="line">'+resultValue+'</span></p></div>';
   	  
     return progress;
   }
   
   function getHeaderProgressBarTarget(current, color, groupName){
	   var progress = '';
	   if(color != ''){
		   progress = '<div style="position: relative;clear: both;color: #333333;"><span style="padding-left: 5px;">'+groupName+'<span style="padding-left: 10px;"><i class="dashboardMetricsBtnHeader '+color+'"></i><span style="padding-left: 25px;color: #000;">'+current+'</span></span></span></div>';   
	   }else{
		   progress = '<div style="position: relative;clear: both;color: #333333;"><span style="padding-left: 5px;">'+groupName+'</span></div>';
	   }
	   		 			
	return progress;	
  }
         
   function getProgressBarTarget(current, color){
	   var progress = '';
	   if(color != ''){
		   progress = '<div style="position: relative;clear: both;"><i class="dashboardMetricsBtn '+color+'"></i><span style="padding-left: 25px;color: #000;">'+current+'</span></div>';   
	   }
	   		 			
	return progress;	
  }
   
  
  function getMetricsTargetBar(valueLeft,valueMiddle,valueRight,isTargetAvailable){
		var progress = '';
		
		if(isTargetAvailable){
			progress = '<div><div><ul style="list-style: none; position: relative; margin: 0;padding: 0;width: 100%; height: 20px;">'
				+'<li class="progressMetric" style="left: 10px;height: 20px;"><span class="percentMetric" style="color: #F3565D;">'+valueLeft+'</span></li>'
				+'<li class="progressMetric" style="left: 55px;height: 20px; width: 52px;"><span class="percentMetric" style="color: #dfba49;">'+valueMiddle+'</span></li>'
				+'<li class="progressMetric" style="left: 110px;height: 20px;"><span class="percentMetric">'+valueRight+'</span></li>'
				+'</ul></div>'
				
				+'<div><ul style="list-style: none; position: relative; margin: 0;padding: 0;width: 100%; height: 15px;">'
				+'<li class="progressMetric" style="left: 0px; background-color: #F3565D;border-top-left-radius: 4px;border-bottom-left-radius: 4px;"></li>'
				+'<li class="progressMetric" style="left: 50px; background-color: #dfba49;"></li>'
				+'<li class="progressMetric" style="left: 100px; background-color: rgba(0, 128, 0, 0.78);border-top-right-radius: 4px; border-bottom-right-radius: 4px;"></li>'
				+'</ul></div></div>';			
		}
		
		return progress;	
 }
  </script>
  
<!-- END JAVASCRIPTS -->
<div><%@include file="singleJtableContainer.jsp"%></div>
<div><%@include file="addComments.jsp"%></div>
<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
<div><%@include file="progressSteps.jsp" %></div>

<div><%@include file="schedule.jsp"%></div>

<!-- the jScrollPane script -->
<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

<!-- Include jTable script file. -->
<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>

<!-- Plugs ins for jtable inline editing -->
<script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
<!-- <script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.modifedColumn.js"></script> -->
<script type="text/javascript" src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>

<!-- Validation engine script file and english localization -->
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>

<script src="js/viewScript/workflowSummary.js"></script>
<script src="js/viewScript/dashboardStatus.js" type="text/javascript"></script>
<script src="files/lib/peity/jquery.peity.js"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jstree/dist/jstree.min.js" type="text/javascript"></script>

</body>
<!-- END BODY -->
</html>