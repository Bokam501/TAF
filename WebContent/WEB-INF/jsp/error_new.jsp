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
<link href="js/Scripts/validationEngine/validationEngine.jquery.css" rel="stylesheet" type="text/css" />
<!-- END THEME STYLES -->
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-header-menu-fixed" class to set the mega menu fixed  -->
<!-- DOC: Apply "page-header-top-fixed" class to set the top menu fixed  -->
<body>

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
 			<!-- END MEGA MENU -->
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->

<!-- BEGIN CONTAINER -->
<div class="page-container">

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
								<span class="caption-subject theme-font bold uppercase">ERROR</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
								<span class="caption-helper"><h4>${message}</h4></span>
							</div>
						</div>
						<div class="portlet-body">
							<div class="row list-separated" style="margin-top:0px;">	
							<!-- jtable started -->
								<!-- Main -->
								    <div id="main">
								         <div id="hidden"></div>
								    <input type="button" class="btn green-haze" onclick="javascript:{window.history.back();}" value="Back"/>     
									<div class="cl">&nbsp;</div>
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
<!-- BEGIN JAVASCRIPTS (Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script src="files/lib/metronic/theme/assets/global/plugins/respond.min.js"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/excanvas.min.js"></script> 
<![endif]-->
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="files/lib/metronic/theme/assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.cokie.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/jquery.vmap.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js" type="text/javascript"></script>
<!-- IMPORTANT! fullcalendar depends on jquery-ui.min.js for drag & drop support -->
<!-- <script src="files/lib/metronic/theme/assets/global/plugins/morris/morris.min.js" type="text/javascript"></script> -->
<!-- <script src="files/lib/metronic/theme/assets/global/plugins/morris/raphael-min.js" type="text/javascript"></script> -->
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.sparkline.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="files/lib/metronic/theme/assets/global/scripts/metronic.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/admin/layout3/scripts/layout.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/admin/layout3/scripts/demo.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/admin/layout/scripts/quick-sidebar.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jstree/dist/jstree.min.js" type="text/javascript"></script>
<!-- <script src="files/lib/metronic/theme/assets/admin/pages/scripts/index3.js" type="text/javascript"></script> -->
<script src="files/lib/metronic/theme/assets/admin/pages/scripts/tasks.js" type="text/javascript"></script>

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

<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>