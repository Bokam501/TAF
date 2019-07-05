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

</head>
<body>
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.bootstrap.wizard.min.js"></script>
<script src="js/moment.min.js" type="text/javascript"></script>
<script src="js/daterangepicker.js" type="text/javascript"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>
<div><%@include file="testPlanGridView.jsp"%></div>

<script>
$(document).ready(function(){
	$(document).on('change','#recentBuilds', function() {
		initRecentBuilds();
	});
	setTimeout(function(){initRecentBuilds();workpackageDetails();},2000);

	function workpackageDetails(){
		recentBuildFlag="Workpackage";
		var leftUrl="get.completed.and.aborted.workpackage.details";							
		var leftDefaultUrl="get.completed.and.aborted.workpackage.details";
		var leftPaginationUrl = "get.completed.and.aborted.workpackage.details";
		jsonTestCaseTabObj={"Title":"Map Feature to Test Cases :- ",
				"leftDragItemsHeaderName":"Recent Workpackages",
				"leftDragItemsTotalUrl":leftUrl,
				"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
				"leftItemPaginationUrl":leftPaginationUrl,
				"leftDragItemsPageSize":"50",
				"noItems":"No Data Available",	
				"componentUsageTitle":"TestcaseTab-FeaturetoTC",											
			};
		
		listItems.init(jsonTestCaseTabObj); 
	}

	function initRecentBuilds(){
		recentBuildFlag="Build";
		var selecteBuild = $("#recentBuilds_ul").find('option:selected').text();
		var tcId = '1';//row.data().testCaseId;
		var productId =	'1';//row.data().productId;					
		// ----- DragDrop Testing started----		
		var leftUrl="feature.unmappedto.testcase.count?productId="+productId+"&testCaseId="+tcId;							
		var leftDefaultUrl="get.latest.product.build.details.by.day.or.week?filter="+selecteBuild+"&jtStartIndex=0&jtPageSize=50";
		var leftPaginationUrl = "get.latest.product.build.details.by.day.or.week?filter="+selecteBuild;
		jsonTestCaseTabObj={"Title":"Map Feature to Test Cases :- ",
			"leftDragItemsHeaderName":"Recent Builds",
			"leftDragItemsTotalUrl":leftUrl,
			"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
			"leftItemPaginationUrl":leftPaginationUrl,
			"leftDragItemsPageSize":"50",
			"noItems":"No Data Available",	
			"componentUsageTitle":"TestcaseTab-FeaturetoTC",											
			};
	
		listItems.init(jsonTestCaseTabObj); 
	}
	$(document).on('change','#envCombinationstatus_ul', function() {
		envCombination();
	});

	$(document).on('change','#selectEnvironmentstatus_ul', function() {
		var envstatus = $("#selectEnvironmentstatus_ul").find('option:selected').val();				
		 urlToGetEnvironmentOfSpecifiedProductId = 'administration.product.environment.list?productMasterId='+productId+"&envstatus="+envstatus+"&timeStamp="+timestamp+"&jtStartIndex=0&jtPageSize=10000";
		 listSelectedEnvCombinationByProduct(urlToGetEnvironmentOfSpecifiedProductId, "parentTable");
	});

});
</script>

</body>