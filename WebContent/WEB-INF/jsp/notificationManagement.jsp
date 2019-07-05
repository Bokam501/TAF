<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<link type="text/css" href="css/jquery.jscrollpane.css" rel="stylesheet"
	media="all" />
<link rel="stylesheet" href="css/style.css" type="text/css" media="all">
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css"
	type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link
	href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235"
	rel="stylesheet" type="text/css" />
<link href="css/customStyle.css" rel="stylesheet" type="text/css">
<link href="js/Scripts/validationEngine/validationEngine.jquery.css"
	rel="stylesheet" type="text/css" />
<!-- END THEME STYLES -->
<link href="css/bootstrap-select.min.css" rel="stylesheet"
	type="text/css"></link>
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet"
	type="text/css"></link>
<link href="css/customStyle.css" rel="stylesheet" type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet"
	type="text/css"></link>
<link rel="stylesheet" type="text/css"
	href="css/bootstrap-datetimepicker.min.css"></link>
<link rel="stylesheet" href="css/iosOverlay.css" type="text/css" media="all">
<link rel="shortcut icon" href="favicon.ico">

<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>

<input type="hidden" value="" id='treeHdnCurrentSelectedNodeType' />
<input type="hidden" value="" id='treeHdnCurrentTestFactoryId' />
<input type="hidden" value="" id='treeHdnCurrentTestFactoryName' />

<style>
.ui-ios-overlay {
	 top: 35%;
  left: 90%;
  width: 100px;
  height: 100px;
}
.ui-ios-overlay img {
	 height:50px;
	width:50px;
}

.ui-ios-overlay .title {
	font-size: 15px;
	bottom:10px;
}
</style>

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
		<div><%@include file="headerlayout.jsp"%></div>
		<!-- END HEADER TOP -->
		<!-- BEGIN HEADER MENU -->
		<div class="page-header-menu">
			<div class="container container-position">
				<!-- BEGIN MEGA MENU -->
				<!-- DOC: Apply "hor-menu-light" class after the "hor-menu" class below to have a horizontal menu with white background -->
				<!-- DOC: Remove data-hover="dropdown" and data-close-others="true" attributes below to disable the dropdown opening on mouse hover -->
				<div><%@include file="menu.jsp"%></div>
				<!-- END MEGA MENU -->
			</div>
		</div>
		<!-- END HEADER MENU -->
	</div>
	<!-- END HEADER -->

	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<div><%@include file="treeStructureSidebar.jsp"%></div>
		<div><%@include file="postHeader.jsp"%></div>
		<div><%@include file="singleJtableContainer.jsp" %></div>
				
		<div id="reportbox" style="display: none;"></div>
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
									<span class="caption-subject theme-font bold uppercase">Notification Management Policy
									</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
								</div>
							</div>
							<div class="portlet-body form">
								<div class="row">
									<div id="notificationListContainer">
										<table id="notifications_dataTable"  class="cell-border compact row-border" cellspacing="0" width="100%">
											<thead>
							            		<tr>
							            			<th>Product</th>
							            			<th>Entity</th>
									                <th>Notification</th>
									                <th>Primary Recipients</th>
									                <th>Secondary Recipients</th>		
									                <th>Email</th>
									                <th>Sms</th>
									                <th>WhatsApp</th>
									                <th>Twitter</th>  
									                <th>Facebook</th>    								              	
							            		</tr>
							       			 </thead>						       			
							       			 <tfoot>
							            		<tr>
									                <th></th>
									                <th></th>
									                <th></th>
									                <th></th>
									                <th></th>
									                <th></th>
									                <th></th>
									                <th></th>
							            			<th></th>	
							            			<th></th>					            										            			            				                	
							            		</tr>
							       			 </tfoot>
										</table>			
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
	</div>
	<!-- END PAGE-CONTAINER -->

	<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp"%></div>
	<!-- END FOOTER -->

	
	
	<script type="text/javascript" src="js/viewScript/notificationManagement.js"></script>
			
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script
		src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/jquery.vmap.js"
		type="text/javascript"></script>
	<script
		src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js"
		type="text/javascript"></script>	
	<script
		src="files/lib/metronic/theme/assets/global/plugins/jquery.sparkline.min.js"
		type="text/javascript"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<script type="text/javascript" src="js/jquery.bootstrap.wizard.min.js"></script>

	<!-- BEGIN PAGE LEVEL SCRIPTS -->	
	<script
		src="files/lib/metronic/theme/assets/admin/layout/scripts/quick-sidebar.js"
		type="text/javascript"></script>
	<script
		src="files/lib/metronic/theme/assets/global/plugins/jstree/dist/jstree.min.js"
		type="text/javascript"></script>
	<!-- END PAGE LEVEL SCRIPTS -->

	<script src="js/select2.min.js" type="text/javascript"></script>
	<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
	<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/bootstrap-datetimepicker.min.js"></script>
	<script src="js/moment.min.js" type="text/javascript"></script>
	<script src="js/daterangepicker.js" type="text/javascript"></script>
	<script src="js/components-pickers.js" type="text/javascript"></script>

	<!-- Plugs ins for datatable -->
	<script src="js/datatable/jquery.dataTables.min.js" type="text/javascript"></script>
	<script src="js/datatable/dataTables.jqueryui.min.js" type="text/javascript"></script>
	<!-- <script src="js/Scripts/popup/jquery.jtable.toolbarsearch.js" type="text/javascript"></script> -->
	
	<!-- the jScrollPane script -->
	<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

	<!-- Include jTable script file. -->
	<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>

	<!-- Plugs ins for jtable inline editing -->
	<script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
	<!-- <script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.modifedColumn.js"></script> -->
	<script type="text/javascript" src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>	

	<script type="text/javascript" src="js/Scripts/popup/jquery.jtable.js"></script> 

	<!-- Validation engine script file and english localization -->
	<script type="text/javascript"
		src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
	<script type="text/javascript"
		src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>

	<script type="text/javascript" src="js/draganddrop/Sortable.js"></script>
	<script type="text/javascript" src="js/draganddrop/ng-sortable.js"></script>
	<script type="text/javascript" src="js/Scripts/popup/iosOverlay.js"></script>
	
	<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
<%-- <div><%@include file="attachments.jsp" %></div> --%>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>