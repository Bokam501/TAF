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
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<link rel="stylesheet" href="dashboard/styles/main.css?_b=5930">
<link type="text/css" href="css/jquery.jscrollpane.css" rel="stylesheet" media="all" />
<link rel="stylesheet" href="css/style.css" type="text/css" media="all">
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all"> 
<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link href="css/bootstrap-select.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet" type="text/css"></link>
<link href="css/customStyle.css" rel="stylesheet" type="text/css"></link>
<!-- END THEME STYLES -->
<style>
.container-fluid{
  display: none;
}
</style>

</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-header-menu-fixed" class to set the mega menu fixed  -->
<!-- DOC: Apply "page-header-top-fixed" class to set the top menu fixed  -->
<body >

<!-- <div id="header"></div> -->

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
	<div><%@include file="postHeader.jsp" %></div>
<!-- BEGIN PAGE CONTENT -->
<div class="page-content">
<div>
<spring:eval var="kibana" expression="@ilcmProps.getProperty('KIBANA_HOST')" />
<script>
var kibana='<c:out value="${kibana}"/>';
	var currRole = '${roleName}';
	var iFrm='';
	if(currRole == 'TestManager') {
		 var contextpath = (window.location.pathname).split("/", 2);
		var root = window.location.protocol	+ "//"+ window.location.host+ "/"+ contextpath[1];			
		iFrm = '<iframe style="width: 100%;" id="kibana" src='+root+'/dashboard/index.html#/dashboard/Admin?embed&_a=(filters:!(),panels:!((col:7,id:defect,row:3,size_x:3,size_y:2,type:visualization),(col:4,id:result,row:3,size_x:3,size_y:2,type:visualization),(col:1,id:productversion,row:1,size_x:3,size_y:2,type:visualization),(col:4,id:feature,row:1,size_x:3,size_y:2,type:visualization),(col:7,id:testcase,row:1,size_x:3,size_y:2,type:visualization)),query:(query_string:(analyze_wildcard:!t,query:\'*\')),title:Admin)&_g=(time:(from:now-7d,mode:quick,to:now)) height="600" width="800"></iframe>';		
	}else{		
		iFrm = '<iframe frameborder="0"  id="kibana" width="100%" scrolling="auto" height="500px" src="http://'+kibana+'" style="position:absolute; width:100%;"></iframe>';
	}
		document.write(iFrm);
</script>
</div>
</div>
<!-- END PAGE CONTENT -->
</div>
<!-- END PAGE-CONTAINER -->

</body>
<!-- END BODY -->
<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp" %></div>			
<!-- END FOOTER -->
</html>