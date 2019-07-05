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
<!-- <link rel="stylesheet" href="css/style.css" type="text/css" media="all"> -->
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link href="files/lib/metronic/theme/assets/global/plugins/icheck/skins/all.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet"	type="text/css"></link>
<link href="css/customStyle.css" rel="stylesheet" type="text/css">

<!-- END THEME STYLES -->
<style>
.ui-dialog .ui-widget-content, .ui-dialog .ui-dialog-content{
	max-height:500px !important;
	overflow-x:hidden !important;
}
/*
@media screen and (min-width:1600px){
.jScrollContainer{

	max-height:500px !important;
	overflow:auto !important;
}
}*/
#filterCompetencies > .col-md-4 >.select2me{
	height: 26px;
    padding-top: 2px;
    margin-top: 7px;
    width: 101px !important;
    font-size: 13px;
}
#filterProducts > .col-md-4 >.select2me{
	height: 26px;
    padding-top: 2px;
    margin-top: 7px;
    width: 101px !important;
    font-size: 13px;
}
#filterTeam > .col-md-4 >.select2me{
	height: 26px;
    padding-top: 2px;
    margin-top: 7px;
    width: 101px !important;
    font-size: 13px;
}
</style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-header-menu-fixed" class to set the mega menu fixed  -->
<!-- DOC: Apply "page-header-top-fixed" class to set the top menu fixed  -->
<body>

<!-- <div id="header"></div> -->
<form action="" method="post" class="button" name="importForm" id="importForm" enctype="multipart/form-data">
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
								<span class="caption-subject theme-font bold uppercase">Competency</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font">: Competencies</span>
								<span class="caption-helper hide"></span>
							</div>
						</div>
						<div class="portlet-body">
							<div class="row list-separated" style="margin-top:0px;">	
								<div class="col-md-12" id="competenciesList">	    
									<!-- jtable started -->
									<!-- Main -->
								    <div id="main" style="float:left; padding-top:0px; width:100%;">
								    	<div id="hdnDiv"> </div>
								    	<div id="hidden"></div>
								    
								        <!-- ContainerBox -->
								       	<div class="jScrollContainerResponsive" >
								       		<!-- Filer -->
										    <div id="filterCompetencies" style="float:right;position: absolute;Z-index: 10;right: 75px;">
										    	<div id="status_dd_competencies" class="col-md-4">
													<select class="form-control input-small select2me" id="status_ul_competencies">
														<option value="2" selected><a href="#">ALL</a></option>
														<option value="1" ><a href="#">Active</a></option>
														<option value="0" ><a href="#">Inactive</a></option>
													</select>
												</div>
			  								</div>
										    <!-- End Filter -->
								      		<div id="jTableContainerCompetencies"></div>
										 </div>
										<!-- End Container Box -->
										<div class="cl">&nbsp;</div>
								    </div>
								    <!-- End Main -->
									<!-- jtable ended -->	
								</div>	
								
								<!-- Tabs -->
								<div id="competencyTabs" style="display:none;">
									<div class ="toolbarFullScreen">
										<ul class="nav nav-tabs " id="tabslistCompetencies">
											<li class="active"><a href="#Summary" data-toggle="tab">Competency Summary</a></li>
											<li><a href="#Products" data-toggle="tab">Products</a></li>
											<li><a href="#Team" data-toggle="tab">Team</a></li>
										</ul>
									</div>
									
									<div class="tab-content" id="tbCntnt" style="display:none;">
										<!-- Summary Tab -->
										<div class="Summary tab-pane fade active in" id="Summary" style="overflow: hidden; height: 73%;">
											<%@include file="competencySummary.jsp"%>
										</div>
										<!-- Summary Tab Ends -->
										
										<!-- Products -->
										<div id="Products" class="tab-pane fade">										
											<!-- jtable started -->
											<!-- Main -->
										    <div id="main" style="float:left; padding-top:0px; width:100%;">
										    	<div id="hdnDiv"> </div>
										    	<div id="hidden"></div>
										    
										        <!-- ContainerBox -->
										       	<div class="jScrollContainerResponsive jScrollContainerFullScreen" style="padding-top: 10px";> 
										       		<!-- Filer -->
												    <!-- <div id="filterProducts" style="float:right;position: absolute;Z-index: 10;right: 75px;">
												    	<div id="status_dd_products" class="col-md-4">
															<select class="form-control input-small select2me" id="status_ul_products">
																<option value="2" selected><a href="#">ALL</a></option>
																<option value="1" ><a href="#">Active</a></option>
																<option value="0" ><a href="#">Inactive</a></option>
															</select>
														</div>
					  								</div> -->
												    <!-- End Filter -->
										      		<div id="jTableContainerProducts" class ="jTableContainerFullScreen"></div>
												 </div>
												<!-- End Container Box -->
												<div class="cl">&nbsp;</div>
										    </div>
										    <!-- End Main -->
											<!-- jtable ended -->	
										</div>
										<!-- Products Ends -->
										
										<!-- Team -->
										<div id="Team" class="tab-pane fade">										
											<!-- jtable started -->
											<!-- Main -->
										    <div id="main" style="float:left; padding-top:0px; width:100%;">
										    	<div id="hdnDiv"> </div>
										    	<div id="hidden"></div>
										    
										        <!-- ContainerBox -->
										       	<div class="jScrollContainerResponsive jScrollContainerFullScreen" style="position: relative;padding-top: 10px;"> 
										       		<!-- Filer -->
												    <div id="filterTeam" class="jScrollTitleContainer" style="float:right;position: absolute;Z-index: 10;right: 100px;">
												    	<div id="status_dd_team" class="col-md-4">
															<select class="form-control input-small select2me" id="status_ul_team">
																<option value="2" selected><a href="#">ALL</a></option>
																<option value="1" ><a href="#">Active</a></option>
																<option value="0" ><a href="#">Inactive</a></option>
															</select>
														</div>
					  								</div>
												    <!-- End Filter -->
										      		<div id="jTableContainerTeam" class ="jTableContainerFullScreen"></div>
												 </div>
												<!-- End Container Box -->
												<div class="cl">&nbsp;</div>
										    </div>
										    <!-- End Main -->
											<!-- jtable ended -->
										</div>
										<!-- Team Ends -->
									</div>
								</div>	
								<!-- Tabs ended -->								
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
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>

<!-- END PAGE LEVEL SCRIPTS -->

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
<!-- <script src="js/select2.min.js" type="text/javascript"></script> -->
<script src="js/viewScript/competencyManagePlan.js" type="text/javascript"></script>
<script type="text/javascript">
var userRole = '${roleName}';
</script>
<div><%@include file="comments.jsp"%></div>
</form>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>