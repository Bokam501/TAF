<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

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

<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>

<style type="text/css">

.textControl{
	border: 1px solid #e5e5e5; 
	border-radius: 4px; 
	background-color: #fff; 
	background-image: none; 
	filter: none; 
	height: 34px; 
	padding: 3px 0 0px 12px;
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
								<span class="caption-subject theme-font bold uppercase">Custom Field Configuration</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
							</div>
						</div>
						<div class="portlet-body">
							<div class ="row ">		
								<div class="container1">
									<div class="row" style="border: 1px solid grey;padding: 5px 5px 5px 5px">
										<div class="col-md-3">
											<div class="col-md-5" style="margin-top: 8px; text-align: right;"> Engagement </div>  
						      				<div id="factoryList_dd">	
							      				<select class="form-control input-small select2me" id="engagementList_ul">
												</select>	
											</div>
										</div>
										<div class="col-md-3">
								      		<div class="col-md-5" style="margin-top: 8px; text-align: right;"> Product </div>  
								      		<div id="productList_dd">	
									      		<select class="form-control input-small select2me" id="productList_ul">
												</select>	
											</div>
										</div>
										<div class="col-md-3">
											<div class="col-md-5" style="margin-top: 8px; text-align: right;"> Entity </div>  
						      				<div id="entityList_dd">	
							      				<select class="form-control input-small select2me" id="entityList_ul">
												</select>	
											</div>
										</div>
										<div class="col-md-3">
											<div class="col-md-5" style="margin-top: 8px; text-align: right;"> Entity Type </div>  
						      				<div id="entityTypeList_dd">	
							      				<select class="form-control input-small select2me" id="entityTypeList_ul">
												</select>	
											</div>
										</div>
									</div>
							
									<div class="clearfix"></div>
						     	</div>
						      
						      <!-- Nreco code ends -->
								
							</div>
							<!-- <div class="clearfix"></div>
							<div class="row" style="padding: 5px 5px 5px 5px; margin-top: 15px !important;">
								<div class="col-md-6">
									<div class="form-horizontal">
										<div class="form-group">
					                		<label class="col-sm-4 control-label">Field Name</label>
					                		<div class="cols-sm-8" id="row">
					                			<input type="text" id="fieldName" class = "textControl"/>
					                		</div>
					                	</div>
					                	
					                	<div class="form-group">
					                		<label class="col-sm-4 control-label">Description</label>
					                		<div class="cols-sm-8" id="row">
					                			<textarea cols = "19" rows = "2" id="description" class = "textControl"></textarea>
					                		</div>
					                	</div>
					                	
					                	<div class="form-group">
					                		<label class="col-sm-4 control-label">Options</label>
					                		<div class="cols-sm-8" id="row">
					                			<textarea cols = "19" rows = "2" id="options" class = "textControl"></textarea>
					                		</div>
					                	</div>
					                	
					                	<div class="form-group">
					                		<label class="col-sm-4 control-label">Display Order</label>
					                		<div class="cols-sm-8" id="row">
					                			<input type="text" id="displayOrder" class = "textControl"/>
					                		</div>
					                	</div>
					                	
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-horizontal">
										<div class="form-group">
					                		<label class="col-sm-4 control-label">Data Type</label>
					                		<div class="cols-sm-8" id="row">
					                			<select class="form-control input-small select2me" id="dataType_ul"> </select>
					                		</div>
					                	</div>
					                	
					                	<div class="form-group">
					                		<label class="col-sm-4 control-label">Control Type</label>
					                		<div class="cols-sm-8" id="row">
					                			<select class="form-control input-small select2me" id="controlType_ul"> </select>
					                		</div>
					                	</div>
					                	
					                	<div class="form-group">
					                		<label class="col-sm-4 control-label">Group</label>
					                		<div class="cols-sm-8" id="row">
					                			<select class="form-control input-small select2me" id="group_ul"> </select>
					                		</div>
					                	</div>
					                	
					                	<div class="form-group">
					                		<label class="col-sm-4 control-label">Recurrence Type</label>
					                		<div class="cols-sm-8" id="row">
					                			<select class="form-control input-small select2me" id="recurrenceType_ul"> </select>
					                		</div>
					                	</div>
					                	
									</div>
								</div>
								<div class="pull-right">
									<a href="javascript:;" id="createCustomField" class="btn btn-primary">Create Custom Field</a>
									<a href="javascript:;" id="clearFields" class="btn btn-primary">Clear</a>
						        </div>
							</div> -->
							<div class="clearfix"></div>
							<div class="row" style="padding: 5px 5px 5px 5px; margin-top: 15px !important;">
								<div id="customFieldsListContainer"></div>
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



<!-- Include jTable script file. -->
<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>

<!-- Plugs ins for jtable inline editing -->
<script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
<script type="text/javascript" src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>

<!-- Plugs ins for datatable -->
<script src="js/datatable/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="js/datatable/dataTables.jqueryui.min.js" type="text/javascript"></script>
<script src="js/Scripts/popup/jquery.jtable.toolbarsearch.js" type="text/javascript"></script>

<!-- Validation engine script file and english localization -->
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>

<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet" type="text/css"></link>

<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>

<!-- <script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script> -->
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="js/moment.min.js" type="text/javascript"></script>
<script src="js/daterangepicker.js" type="text/javascript"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>

<script src="js/viewScript/customFieldConfiguration.js" type="text/javascript"></script>						

<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>

<!-- the jScrollPane script -->
<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

<script src="files/lib/metronic/theme/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script> 

<div><%@include file="comments.jsp"%></div>

<script type="text/javascript">
	var isCustomFieldConfiguration = true;
</script>

<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>