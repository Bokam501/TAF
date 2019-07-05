<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.hcl.atf.taf.controller.HomePageController"%>
<%@page import="com.hcl.atf.taf.model.UserList"%>
<%@page import="java.util.List"%>
<%@page import="com.hcl.atf.taf.model.TestRunList"%>
<%@ page import = "java.util.ResourceBundle"%>

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

<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link href="js/Scripts/validationEngine/validationEngine.jquery.css" rel="stylesheet" type="text/css" />
<!-- END THEME STYLES -->
<div><jsp:include page="atlasComponent.jsp"></jsp:include></div>

<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>

<link href="css/bootstrap-select.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet" type="text/css"></link>
<link rel="stylesheet" type="text/css" href="css/bootstrap-datetimepicker.min.css"></link>
<link rel="stylesheet" href="css/iosOverlay.css" type="text/css" media="all">
<link rel="stylesheet" href="js/Scripts/star-rating/jquery.rating.css" type="text/css">

<link href="css/customStyle.css" rel="stylesheet" type="text/css"></link>
<spring:eval var="toenableTab" expression="@ilcmProps.getProperty('ENABLING_TAB_PROPERTY')" />
<spring:eval var="enableATSG" expression="@ilcmProps.getProperty('ENABLE_ATSG')" />

<spring:eval var="productMgmGridEngagementHeader" expression="@ilcmProps.getProperty('XEROX_SETUP_PRODUCTMANAGEMENT_GRID_HEADER_ENGAGEMENT')" />
<spring:eval var="productMgmGridProductHeader" expression="@ilcmProps.getProperty('XEROX_SETUP_PRODUCTMANAGEMENT_GRID_HEADER_PRODUCT')" />

<style type="text/css">
.modal-open .select2-drop {
	z-index: 10055 !important;
}
#select2-drop-mask {
	z-index: 10054 !important;
}
.expanded-group{
	background: url("media/images/minus.jpg") no-repeat scroll left center transparent;
	padding-left: 15px !important
}

.collapsed-group{
	background: url("media/images/plus.jpg") no-repeat scroll left center transparent;
	padding-left: 15px !important
}			
			
#downloadScriptType_id > .input-small {
       width: 180px !important;
}

#downloadTestEngineType_id > .input-small {
       width: 180px !important;
}

#downloadTestStepOption_id > .input-small {
       width: 180px !important;
}
#Edit-parentFeatureId,#Edit-parentCategoryId{
		width:400px !important;
}
.row > #filter > .col-md-4 >.select2me, .row > #filterForFeatures> .col-md-4 >.select2me , #filter1 > .col-md-4 >.select2me , #filterForEnvironments > .col-md-4 >.select2me , #filterRisk > .col-md-4 >.select2me ,
#filterRiskSeverity > .col-md-4 >.select2me , #filterRiskLikeHood > .col-md-4 >.select2me , #filterRiskMitigation > .col-md-4 >.select2me , #filterRiskRating > .col-md-4 >.select2me{ 
 	 height: 28px !important; 
     margin-top: 3px; 
     width: 90px !important ;
     font-size: 13px;
 }  
 .row > #filter > .col-md-4 >.select2me > a , .row > #filterForFeatures> .col-md-4 >.select2me > a ,#filter1 > .col-md-4 >.select2me > a , #filterForEnvironments> .col-md-4 >.select2me > a , #filterRisk > .col-md-4 >.select2me > a ,
 #filterRiskSeverity > .col-md-4 >.select2me > a , #filterRiskLikeHood > .col-md-4 >.select2me > a , #filterRiskMitigation > .col-md-4 >.select2me > a, #filterRiskRating > .col-md-4 >.select2me > a { 
 	 height: 28px !important; 
     padding-top: 0px; 
     margin-top: 0px; 
 } 
.row > .col-lg-5 > #filterForChangeRequest > .col-md-4 >.select2me{
	height: 26px !important;
    margin-top: 5px;
    width: 101px !important;
    font-size: 13px;
}

.row > .col-lg-5 > #filterForChangeRequest > .col-md-4 >.select2me > a{
	height: 28px !important; 
     padding-top: 0px; 
     margin-top: 0px; 
}

#tabslist li a {
	padding: 8px 12px !important;
}

#productMgtTestCasePaginationDT .customPagination{
    padding: 2px 5px 2px 5px;
    margin-right: 2px;
    font-weight: bold;
    color: #000;
    background-color: #adadad;
    border: 1px solid #878585;
}

.alignImageCenter{
	 padding: 7px;
    width: 10%;
    text-align: center;
    vertical-align: inherit;
}

</style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body>
<!--BEGIN: Alert -->
	<div class="modal fade" id="basicAlert" tabindex="-1" role="basic" aria-hidden="true" style="z-index:100080;">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
					<h4 class="modal-title">Alert</h4>
				</div>
				<div class="modal-body" >
					 <span id="alertModal"></span>
					 <span id="alertModalHelp"></span>
					 <a href="" id="processFlow" style="display:none" target="_blank">process workflow.</a>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn green-haze" data-dismiss="modal">Ok</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!--END: Alert -->
	<!-- BEGIN HEADER -->
	<div class="page-header hidden">
		<!-- BEGIN HEADER TOP -->
		<div><%@include file="headerlayout.jsp"%></div>
		<!-- END HEADER TOP -->
		<!-- BEGIN HEADER MENU -->
		<div class="page-header-menu">
			<div class="container container-position">				
				<div><%@include file="menu.jsp"%></div>				
			</div>
		</div>
		<!-- END HEADER MENU -->
	</div>
	<!-- END HEADER -->

	<!-- BEGIN CONTAINER -->
	<div class="page-container reduceFooter">
		
		<div class="hidden"> <%@include file="treeStructureSidebar.jsp" %></div>
		<div class="hidden"><%@include file="postHeader.jsp"%></div>
		<div id="reportbox" style="display: none;"></div>		
		<div class="hidden"><%@include file="progressSteps.jsp" %></div>
		<!-- BEGIN PAGE CONTENT -->
		<div class="page-content hidden">
			<div class="container-fluid">
				<!-- BEGIN PAGE CONTENT INNER -->
				<div class="row margin-top-10" id="toAnimate" style="margin-left:0px !important;">
					<div class="col-md-12">
						<!-- BEGIN PORTLET-->
						<div class="portlet light "style="min-height: 440px;overflow: auto">
							<div class="portlet-title toolbarFullScreen" style="margin-bottom:0px;">
								<div class="caption caption-md">
									<span class="caption-subject theme-font bold uppercase">Products</span>&nbsp;
									<span id="headerTitle" class="caption-subject theme-font"></span>
									<div id="products_list" class="inlineHelp">
											<i class="fa fa-question-circle inlineHelpItem" title="Help" onclick="editorHTMLHandler(event)"></i>
									 	</div>
								</div>
								<div class="actions">
									<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" 
										onClick="fullScreenHandlerDTProductManagementPlan()" data-original-title="" title="FullScreen"></a>
								</div>
								<div id="filter"  style="float:right;padding-bottom:10px">
									<div id="status_dd" class="col-md-4">
										<select class="form-control input-small select2me" id="status_ul">														
											<option value="2" ><a href="#">ALL</a></option>
											<option value="1" selected><a href="#">Active</a></option>
											<option value="0" ><a href="#">Inactive</a></option>
										</select>
									</div>
   								</div>
							</div>
							<div class="portlet-body">								

									<div id="testFacMode">	
										<!-- <div class="caption caption-md" style="margin:5px 0px -5px 0px;">
											<span class="caption-subject theme-font bold uppercase">Add/Edit Product(s):</span>
										</div>	 -->
										<!-- <button type="button" class="button" id="add_product" style="float: right;">Add product</button> -->		 
										 <table id="products_dataTable"  class="cell-border compact row-border" cellspacing="0" width="100%">
											<thead>
							            		<tr>
							            			<th id="Engagementth" >Engagement</th>
									                <th>Customer</th>
									                <th id="productidlbl">Product Id</th>
									                <th id="product">Product</th>
									                <th>Description</th>
									                <th>Ref Project Code</th>
									                <th>Ref Project</th>
									                <th>Type</th>
									                <th>Mode</th>
									                <th>Status</th>		
									              	<th></th>								              	
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
							            			<th></th>				            										            			            				                	
							            		</tr>
							       			 </tfoot>
										</table>									
									</div> 
																		
								<div id="pdtMode" style="display:none;">
										<ul class="nav nav-tabs toolbarFullScreen" id="tabslist">
											<li class="active"><a href="#Products" data-toggle="tab">Summary</a></li>
											<li><a href="#ToolIntegration" data-toggle="tab">Tools</a></li>
											<li><a href="#Features" data-toggle="tab">Features</a></li>
											<li><a href="#TestTabPage" data-toggle="tab">Test</a></li>
											<li><a href="#Decoupling" data-toggle="tab">Decoupling</a></li>
											<li><a href="#Environment" data-toggle="tab">Environments</a></li>
											<li><a href="#Devices" data-toggle="tab">Devices</a></li>
											<li><a href="#TeamTabPage" data-toggle="tab">Team</a></li>
											<li><a href="#UserGroup" data-toggle="tab">User Group</a></li>
											<li><a href="#ChangeRequests" data-toggle="tab">Change Requests</a></li>
											<li><a href="#Risks" data-toggle="tab">Risks</a></li>
										</ul>
									</div>
								<input type="hidden" id="hdnTrpLength" value=""/>
								<input type="hidden" id="hdnTestRunPlanName" value=""/>
								<input type="hidden" id="hdnTestRunPlanDeviceId" value=""/>
								
								<div id="hidden"></div>
								<div id="selectedUserDetailshidden"></div>
								<div class="tab-content" id="tbCntnt" style="display:none;">
									<!-- Products Tab -->
									<div class="Products  tab-pane fade active in" id="Products" style="overflow: hidden; height: 73%;">
										<%@include file="productSummary.jsp"%>
									</div>
									
									<!-- Tool -->
									<div id="ToolIntegration" class="tab-pane fade">
										<div id="toolId" class="radio-toolbar col-lg-6 toolbarFullScreen">								
											<div class=" btn-group" data-toggle="buttons" style="width:130%" id="allocateRadios">
												<label class="btn darkblue active" data-name="tms" onclick="showCorrespondingTableTool(1);">
												<input type="radio" class="toggle" >Test Management Systems</label>
												<label class="btn darkblue" data-name="dms" onclick="showCorrespondingTableTool(2);">
												<input type="radio" class="toggle" >Defect Management Systems</label>
												<label class="btn darkblue" data-name="scms" onclick="showCorrespondingTableTool(3);">
												<input type="radio" class="toggle" >SCM Systems</label> 																														
											</div>
										</div>	 
										<div class="jtbToolTMS" style="clear:both; padding-top:15px;">
											<div id="dataTableContainerForTMS"></div>
										</div>										
										<div class="jtbToolDMS" style="clear:both;display:none !important; padding-top:15px;">
											<div id="dataTableContainerForDMS"></div>
										</div>										
										<div class="jtbToolSCMS" style="clear:both;display:none !important; padding-top:15px;">
											<div style="position: absolute;z-index: 10;right: 165px;margin-top: 3px;display: block;">
												<div id="status_dd" class="col-md-4">
													<select class="form-control input-small select2me" id="scmSystemStatus_ul">
														<option value="2" selected><a href="#">ALL</a></option>
														<option value="1" ><a href="#">Active</a></option>
														<option value="0"><a href="#">Inactive</a></option>
													</select>
												</div>
		    								</div>
											<div id="dataTableContainerForSCMS"></div>
										</div>									
										<div class="jScrollContainerResponsive jtable-align jtbToolAS" style="clear:both;display:none !important; padding-top:15px;">
											<div id="jTableContainerAS"></div>
										</div>
									</div>
									<!-- Tools Ends -->
									
									<!-- Features -->
									<div id="Features" class="tab-pane fade">
										<div class="row" style="position: absolute;z-index: 10; display:none">
											<input id="uploadFileOfProductFeatures" type="file" name="uploadFileOfProductFeatures" 
														   onclick="{this.value = null;};" onchange="importProductFeatures(event);" />
										</div>
										
										<div id="jTableContainerfeatures"></div>
									</div>
									<!-- Features Ends -->
									<!-- FeatureTestCases -->
									<div id="FeatureTestCases" class="tab-pane fade" style="display:none">
										<!-- ContainerBox -->										
										<div class="row">
											<div class="col-md-12"><i class="fa fa-tree" style="float:right;cursor: pointer;" onclick="showFeatureTestCaseTree(0)"></i></div>
										</div>
										<div id="featureTestCasesContent">			        
											<table id="featureTestCases_dataTable"  class="display" cellspacing="0" width="100%">
											<thead>
							            	<tr>							              				                	
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
							            	</tr>
							                 </tfoot>
											</table>
										</div>
										<!-- End Container Box -->
									</div>
									<!-- FeatureTestCases Ends -->
									<!-- Decoupling -->
									<div id="Decoupling" class="tab-pane fade">										
										<div style="clear:both;padding-top:10px">
											<div id="jTableContainerdecoupling"></div>										
										</div>
									</div>
									<!-- Decoupling Ends -->
									
									<div id="Environment" class="tab-pane fade">
										<div id="envRadioGrp" class="radio-toolbar col-lg-6 toolbarFullScreen">								
											<div class=" btn-group" data-toggle="buttons" style="width:100%">
												<label class="btn darkblue active" data-name="EnvironmentCombinations" onclick="showEnvironmentCombinationTable(1);">
												<input type="radio" class="toggle" >Environment Combinations</label>
												<label class="btn darkblue" data-name="Environments" onclick="showEnvironmentCombinationTable(2);">
												<input type="radio" class="toggle" >Select Environments</label>
											</div>
										</div>	 
										
										<div class="jTbEnv" style="display:none;clear:both;padding-top:10px;position: relative">
											<div id="filterForEnvironments" class="jScrollTitleContainer" style="position: absolute;z-index: 10;right: 165px;margin-top: 3px;display: block;">
													<div id="status_dd" class="col-md-4">
														<select class="form-control input-small select2me" id="selectEnvironmentstatus_ul">
															<option value="2" ><a href="#">ALL</a></option>
															<option value="1" selected><a href="#">Active</a></option>
															<option value="0" ><a href="#">Inactive</a></option>
														</select>
													</div>
		    								</div>
		    			                    <div id="jTableContainerProductEnvironment" class ="jTableContainerFullScreen"></div>
										</div>
										<div class="jTbEnvCmb" style="clear:both;padding-top:10px;position: relative">
											<div id="filter1" class="jScrollTitleContainer" style="position: absolute;z-index: 10;right: 165px;margin-top: 3px;display: block;">
												<div id="status_dd" class="col-md-4">
													<select class="form-control input-small select2me" id="envCombinationstatus_ul">
														<option value="2" selected><a href="#">ALL</a></option>
														<option value="1" ><a href="#">Active</a></option>
														<option value="0"><a href="#">Inactive</a></option>
													</select>
												</div>
		    								</div>		    								
											<div id="jTableContainerenvironmentCombination" class ="jTableContainerFullScreen"></div>
										</div>
									</div>
									<div id="Devices" class="tab-pane fade">
										<!-- ContainerBox -->
										<div class="jScrollContainerResponsive" style="clear:both;padding-top:10px">
											<jsp:include page="deviceDetails.jsp"></jsp:include>
										</div>
										<!-- End Container Box -->
									</div>
									<div id="VersionTestCaseMapping" class="tab-pane fade">
										<!-- ContainerBox -->
										<div class="jScrollContainerResponsive" style="clear:both;padding-top:10px">
											<div id="jTableContainerversiontestcase"></div>
										</div>
										<!-- End Container Box -->
									</div>
									<!-- Product Team Resources -->
									<div id="ProductTeamResources" class="tab-pane fade"></div>
									<!-- User Group -->
									<div id="UserGroup" class="tab-pane fade">
										<!-- ContainerBox -->
										<div id="dataTableContainerForUserGroup"></div>
										<!-- End Container Box -->
									</div>
									<!-- User Group Ends -->
									
									<!-- Core Resources -->
									<div id="CoreResources" class="tab-pane fade">
										<!-- ContainerBox -->
										<div class="jScrollContainerfilter">
											<label class="control-label">The flex testing resources below are exclusively available only for this product's workpackages during the specified durations. They cannot be reserved for other products</label>
											<div id="jTableContainercoreresources"></div>
											<br><br>
											<label class="control-label">The flex testing resources below are exclusively available only for products of the current Test Factory during the specified durations. They cannot be reserved for products of other Test Factories</label>											
											<div id="jTableContainertestfactorycoreresources"></div>
											<br><br>
											<label class="control-label">In addition, flex testing resources from the resource pools listed below can be used for testing workpackages, based on their availability</label>											
											<div id="jTableContainertestfactorycoreresources"></div>
										</div>
										<!-- End Container Box -->
									</div>
									<!-- Core Resources Ends -->
									<!-- Change Requests -->
									<div id="ChangeRequests" class="tab-pane fade">
										<div style="display: none;">
											<input id="uploadFileChangeRequests" type="file" name="uploadFileChangeRequests" onclick="{this.value = null;};" onchange="importChangeRequests();" />
										</div>
										<div id="filterForChangeRequest" style="position: absolute;z-index: 10;right: 160px;margin-top: 3px;display: none;">
											<div id="changeRequeststatus_dd" class="col-md-4">
												<select class="form-control input-small select2me" id="changeRequeststatus_ul">
													<option value="2" ><a href="#">All</a></option>
													<option value="1" selected><a href="#">Active</a></option>
													<option value="0" ><a href="#">Inactive</a></option>
												</select>
											</div>
										</div>
										<div id="jTableContainerChangeRequests"></div>										
									</div>
									<!-- Change Requests -->
									<!-- Risks -->
									<div id="Risks" class="tab-pane fade ">
										<div id="riskRadioGrp" class="radio-toolbar col-md-12 toolbarFullScreen">								
											<div class=" btn-group" data-toggle="buttons" style="width:100%">
												<label class="btn darkblue active" data-name="Risks" onclick="showCorrespondingRisks(1);">
												<input type="radio" class="toggle" >Risks</label>
												<label class="btn darkblue" data-name="RiskMitigation" onclick="showCorrespondingRisks(2);">
												<input type="radio" class="toggle" >Risk Mitigation</label>
												<label class="btn darkblue" data-name="RiskSeverity" onclick="showCorrespondingRisks(3);">
												<input type="radio" class="toggle" >Risk Severity</label>
												<label class="btn darkblue" data-name="RiskLikeHood" onclick="showCorrespondingRisks(4);">
												<input type="radio" class="toggle" >Risk LikeHood</label>
												<label class="btn darkblue" data-name="RiskRating" onclick="showCorrespondingRisks(5);">
												<input type="radio" class="toggle" >Risk Rating</label>
												<!-- <label class="btn darkblue" data-name="RiskMatrix" onclick="showCorrespondingRisks(6);">
												<input type="radio" class="toggle" >Risk Matrix</label> -->
											</div>
										</div>									
										<div class="jtbToolRisk" style="clear:both;padding-top:10px;position: relative;">
											<div id="filterRisk" class ="jScrollTitleContainer" style="float:right;position: absolute;Z-index:10;top: 16px;right: 129px;">
												<div id="riskStatus_dd" class="col-md-4">
													<select class="form-control input-small select2me" id="riskStatus_ul">
														<option value="2" ><a href="#">All</a></option>
														<option value="1" selected><a href="#">Active</a></option>
														<option value="0" ><a href="#">Inactive</a></option>
													</select>
												</div>
		    								</div>
											<div id="dataTableContainerForRisk"></div>
											<!-- End Container Box -->
										</div>
										
										<div class="jtbToolSeverity" style="clear:both;padding-top:10px;position: relative;">
											 <div id="filterRiskSeverity" class ="jScrollTitleContainer" style="float:right;position: absolute;Z-index:10;top: 12px;right: 80px;display:none">
												<div id="riskSeverityStatus_dd" class="col-md-4">
													<select class="form-control input-small select2me" id="riskSeverityStatus_ul">
														<option value="2" ><a href="#">All</a></option>
														<option value="1" selected><a href="#">Active</a></option>
														<option value="0" ><a href="#">Inactive</a></option>
													</select>
												</div>
		    								</div> 
											<div id="dataTableContainerForRiskSeverity"></div>
										</div>
										
										<div class="jtbToolLikeHood" style="clear:both;padding-top:10px;position: relative;">
											<div id="filterRiskLikeHood" class ="jScrollTitleContainer" style="float:right;position: absolute;Z-index:10;top: 12px;right: 80px;display:none">
												<div id="riskLikeHoodStatus_dd" class="col-md-4">
													<select class="form-control input-small select2me" id="riskLikeHoodStatus_ul">
														<option value="2" ><a href="#">All</a></option>
														<option value="1" selected><a href="#">Active</a></option>
														<option value="0" ><a href="#">Inactive</a></option>
													</select>
												</div>
		    							 	</div>
											<div id="dataTableContainerForRiskLikeHood"></div>
										</div>
										
										<div class="jtbToolMitigation" style="clear:both;padding-top:10px;position: relative;">
											<div id="filterRiskMitigation" class ="jScrollTitleContainer"  style="float:right;position: absolute;Z-index:10;top: 12px;right: 80px;display:none">
												<div id="riskMitigationStatus_dd" class="col-md-4">
													<select class="form-control input-small select2me" id="riskMitigationStatus_ul">
														<option value="2" ><a href="#">All</a></option>
														<option value="1" selected><a href="#">Active</a></option>
														<option value="0" ><a href="#">Inactive</a></option>
													</select>
												</div>
		    								</div>
											<div id="dataTableContainerForRiskMitigation"></div>
										</div>
										
										<div class="jtbToolRating" style="clear:both;padding-top:10px;position: relative;">
											<div id="filterRiskRating" class ="jScrollTitleContainer" style="float:right;position: absolute;Z-index:10;top: 12px;right: 80px;display:none">
												<div id="riskRatingStatus_dd" class="col-md-4">
													<select class="form-control input-small select2me" id="riskRatingStatus_ul">
														<option value="2" ><a href="#">All</a></option>
														<option value="1" selected><a href="#">Active</a></option>
														<option value="0" ><a href="#">Inactive</a></option>
													</select>
												</div>
		    							    </div>
											<div id="dataTableContainerForRiskRating"></div>
										</div>
										
										<div class="jScrollContainerResponsive jtbToolMatrix jScrollContainerFullScreen" style="clear:both;padding-top:10px;position: relative;">
											<div id="jTableContainerRiskMatrix" class ="jTableContainerFullScreen"></div>
										</div>										
									</div>
									<!-- Risks -->
									<!-- Traceability Report -->
									<div id="TraceabilityReport" class="tab-pane fade" style="display:none">		
										<div id="traceabilityReportRiskRadioGrp" class="radio-toolbars">								
											<div class=" btn-group toolbarFullScreen" data-toggle="buttons" style="width:100%">
												<label class="btn darkblue active" data-name="TestReport" onclick="showCorrespondingTraceabilityReports(1);">
												<input type="radio" class="toggle" >Feature Traceability</label>
												<label class="btn darkblue " data-name="RiskReport" onclick="showCorrespondingTraceabilityReports(2);">
												<input type="radio" class="toggle" >Risk Traceability</label>
												<label class="btn darkblue" data-name="DefectReport" onclick="showCorrespondingTraceabilityReports(3);">
												<input type="radio" class="toggle" >Defect Traceability</label>
											</div>	
											<div class="jScrollContainerResponsive jtbToolRiskReport" style="clear:both;padding-top:5px;overflow: auto;">
												<div class="jtbRiskTraceability" style="clear:both;">
												<div id="dataTableContainerRiskTraceability">
												<div class="caption caption-md" style="margin:0px">
													<span class="caption-subject theme-font bold uppercase">Risk Hazard Traceability Matrix:</span>
												</div>					
													<table id="filter_dataTable_RiskTraceability" class="display" style="padding-top: 0px;" cellspacing="0" width="100%">									
													<thead>
														<tr>
														<th rowspan="2">Risk-ID</th>
											            <th rowspan="2">Feature ID</th>
											            <th rowspan="2">Hazard</th>		
											            <th colspan="3" style="text-align: center;">Pre Mitigation</th> 
											            <th rowspan="2">Mitigation ID</th>
											            <th colspan="3" style="text-align: center;">Post Mitigation</th>
											            <th rowspan="2">Test Case ID</th>	
											             <th colspan="5" style="text-align: center;">Test Execution Results</th>	           		            
														</tr>					
											             <tr>         						              
														 <th>Severity</th>
											            <th>Likehood</th>
											            <th>Risk Priority</th>
											            <th>Severity</th>
											            <th>Likehood</th>
											            <th>Risk Priority</th>
											            <th>Execution ID</th>	
											            <th>Status</th>
											            <th>Iteration</th>		            
											            <th>Phase</th>
											            <th>Date</th>
											            </tr>
											        </thead>
												</table>												
												</div>
											</div>											
												<!-- End Container Box -->
											</div>
											<div class="jScrollContainerResponsive jtbToolDefectReport jScrollContainerFullScreen" style="clear:both;padding-top:5px;">
												<div id="jTableContainerDefectReport" class ="jTableContainerFullScreen" style="padding-top:5px;"></div>
												<div id="dataTableContainerForDefectTraceability"></div>
											</div>
											
											 <div class="jScrollContainerResponsive jtbToolTestReport" style="clear:both;padding-top:5px;">
												<div class="jtbTestReport">
													<div class="caption caption-md" style="margin:0px;">
														<span class="caption-subject theme-font bold uppercase">Test Report - Fix Fail Details:</span>
													</div>
													<div id="dataTableContainerTestReport">
														<table id="filter_dataTable_TestReport" class="display" style="padding-top:0px;width:100%" cellspacing="0" >									
															<thead>
																<tr>
																<th rowspan="2">Feature ID</th>
													            <th rowspan="2">Test Case ID</th>
													            <th rowspan="2">Execution ID</th>
													            <th colspan="3" style="text-align: center;">Test Script Approved / Released</th> 		           
													            <th colspan="4" style="text-align: center;">Test Execution</th>		        
													            <th colspan="4" style="text-align: center;">Defect</th>	           		            
																</tr>					
													             <tr>         						              
																<th>Test Script ID</th>
													            <th>Status</th>
													            <th>Date</th>
													            <th>Test Execution Phase</th>
													            <th>Iteration</th>
													            <th>Last Execution Date</th>
													            <th>Last Execution Result</th>	
													            <th>Defect Id </th>
													            <th>Defect Status</th>		            
													            <th>Re-Open Date </th>
													            <th>Resolution Adopted</th>
													            </tr>
													        </thead>
														</table>											
													</div>
												 </div>								
											</div>
											<!-- END PORTLET-->			
								
											<div id="noDataTable" style="display:none;text-align:center;"> 
												<label class="control-label" style="font-size:16px;color:grey;opacity:0.7;"></label>
											</div>
										</div>
										<!-- END PAGE CONTENT INNER -->
									</div>
									
									<!-- Automation Scripts - Starts -->
									<div id="AutomationScripts" class="tab-pane fade">
											<div class="row">
												<input type="hidden" id="hdnProductId" name="hdnProductId">
												<input type="hidden" id="hdnProductName" name="hdnProductName">
												<input type="hidden" id="hdnProductVersionId" name="hdnProductVersionId"> 
												<input type="hidden" id="hdnTestRunPlanId" name="hdnTestRunPlanId"> 
												<input type="hidden" id="hdnTestRunPlanGroupId" name="hdnTestRunPlanGroupId">
												<input type="hidden" id="hdntestFactoryId" name="hdntestFactoryId">
												<input type="hidden" id="hdnTestRunPlanType" name="hdnTestRunPlanType">
												<input type="hidden" id="hdnproductType" value="hdnproductType" >
												<input type="hidden" id="tcIdExecutionHistory" value="" >
												<input type="hidden" id="tcNameExecutionHistory" value="" >
												
											</div>
											<!-- ContainerBox -->
											<div class="jScrollContainerResponsive jScrollContainerFullScreen" style="clear:both;padding-top: 5px;position: relative;min-height: 270px;">
												<div class="col-lg-2 jScrollTitleContainer" style="position: absolute;z-index: 10;width: 340px;margin-left: 244px;margin-top: 6px;float:left">
													
															<label style="font-size: 1.1em !important;color: #FFF;font-family: sans-serif;">Upload : </label>
													
															<input style="margin-left: 4px;height: 26px;background-color:  #55616f;font-size: 0.9em;padding-top: 3px;margin-top: 2px;"
																type="button" class="btn blue" id="trigUploadTestCase" value="Test Cases">
							           			  			<span id="fileNameTestCase"></span>
															<input id="uploadFileTestCase" type="file" name="uploadFileTestCase" style="display:none;" 
															onclick="{this.value = null;};" onchange="importTestCase()">
															
															<img src="css/images/dt_download_features.png" title="Download Template - Test Cases" class="icon_imgSize showHandCursor" 
															alt="Download" onclick="downloadTemplateTestCase();">
																																							
							           			  			<input style="margin-left: 4px;height: 26px;background-color:  #55616f; font-size: 0.9em;padding-top: 3px;margin-top: 2px;"
							           			  				type="button" class="btn blue" id="trigUploadTestSteps" value="Test Steps">
							           			  			<span id="fileNameTestSteps"></span>
															<input id="uploadFileTestSteps" type="file" name="uploadFileTest" style="display:none;" 
															onclick="{this.value = null;};" onchange="importTestSteps()">
															
															<img src="css/images/dt_download_features.png" title="Download Template - Test Steps" class="icon_imgSize showHandCursor" 
															alt="Download" onclick="downloadTemplateTestSteps();"> 
													
												</div>
												<div id="jTableContainertest123" class ="jTableContainerFullScreen"></div>
											</div>
											<!-- End Container Box -->
									</div>
									
									<!-- Automation Scripts - Ends -->									
									<div id="TestData" class="tab-pane fade">																			
									</div>
									
									<!-- Team Utilization Tab -->
									<div xclass="TeamUtilization  tab-pane fade" xid="TeamUtilization" style="overflow: hidden;height:73%;">										
									</div>
									
									<!-- Tests  Tab -->
									<div class="TestTabPage  tab-pane fade" id="TestTabPage" style="overflow: hidden;height:73%;">
										<div id="testsRadioGrp" class="radio-toolbar xcol-lg-8 toolbarFullScreen">								
											<div class="btn-group tests_radGrp" data-toggle="buttons" style="width:100%">
												<label class="btn darkblue active" data-name="testcase" onclick="showCorrespondingTestsTable(1);"><input type="radio" class="toggle" >Testcases</label>
												<label class="btn darkblue" data-name="testscript" onclick="showCorrespondingTestsTable(2);"><input type="radio" class="toggle" >Test Scripts</label>
												<label class="btn darkblue" data-name="testsuite" onclick="showCorrespondingTestsTable(3);"><input type="radio" class="toggle" >Test Suites</label>
												<label class="btn darkblue" data-name="testplan" onclick="showCorrespondingTestsTable(4);"><input type="radio" class="toggle" >Test Plans</label>
												<label class="btn darkblue" data-name="testplangroup" onclick="showCorrespondingTestsTable(5);"><input type="radio" class="toggle" >Test Plan Groups</label>
												<label class="btn darkblue" data-name="testdata" onclick="showCorrespondingTestsTable(6);"><input type="radio" class="toggle" >Test Data</label>
												<label class="btn darkblue" data-name="objectrepository" onclick="showCorrespondingTestsTable(7);"><input type="radio" class="toggle" >Object Repository</label>
												<label class="btn darkblue" data-name="testdataAttachment" onclick="showCorrespondingTestsTable(8);"><input type="radio" class="toggle" >Test Data Attachments</label>
												<label class="btn darkblue" data-name="eDATAttachment" onclick="showCorrespondingTestsTable(9);"><input type="radio" class="toggle" >eDAT Attachments</label>
											</div>
										</div>
										<div class="testCaseTab" id="Testcases" xclass="jScrollContainerResponsive jScrollContainerFullScreen" style="margin-right:5px;" >
											<div id="testCaseStatusFilter_dd" style="float:right;margin-top: 7px;display: block;">
												<select class="form-control input-small select2me" id="testCaseStatusFilter_ul">
													<option value="ALL" ><a href="#">ALL</a></option>
													<option value="Approved" selected><a href="#">Approved</a></option>
												</select>
											</div>
											<div class="row">
												<input type="hidden" id="hdnProductId" name="hdnProductId">
												<input type="hidden" id="hdnProductName" name="hdnProductName">
												<input type="hidden" id="hdnProductVersionId" name="hdnProductVersionId"> 
												<input type="hidden" id="hdnTestRunPlanId" name="hdnTestRunPlanId"> 
												<input type="hidden" id="hdnTestRunPlanGroupId" name="hdnTestRunPlanGroupId">
												<input type="hidden" id="hdntestFactoryId" name="hdntestFactoryId">
												<input type="hidden" id="hdnTestRunPlanType" name="hdnTestRunPlanType">
												<input type="hidden" id="hdnproductType" value="hdnproductType" >
												<input type="hidden" id="tcIdExecutionHistory" value="" >
												<input type="hidden" id="tcNameExecutionHistory" value="" >
												
											</div>
											<div>
												<input id="uploadFileTestCase" type="file" name="uploadFileTestCase" style="display:none;" 
													onclick="{this.value = null;};" onchange="importTestCase()">											
												<input id="uploadFileTestSteps" type="file" name="uploadFileTestSteps" style="display:none;" 
													onclick="{this.value = null;};" onchange="importTestSteps()">											 
												<input id="testCaseGroup" style="margin-left: 4px;height: 26px;background-color:  #55616f; font-size: 0.9em;padding-top: 3px;margin-top: 2px;" type="button" class="btn blue" onclick="testCaseGroupHandler(event)" value="Test Scenario">																							
											</div>
											<div id="jTableContainertest"></div>
											
											<div id="productMgtTestCasePaginationContainerDT" class="a">																					
												<div style="display: -webkit-box; margin-top: 10px;">
												    <u style="color: #4db3a4; padding-right: 10px;"><span class="caption-subject theme-font bold uppercase">Server Side Pagination :</span></u>	 
													<div class="dataTables_info" id="productMgtTestCaseBadgeDT" role="status" aria-live="polite">Showing 1 to 1000 of 1000 entries</div>					
													<div style="padding-right:0px; padding-left: 18px;">
														<label>Show 
														<select id="selectProductMgtTestCaseCountDT" onchange="setDropDownProductMgtTestCaseDT(this.value)" style="vertical-align: middle;">
															<option value="1000" selected="selected">1000</option>
															<option value="2500">2500</option>
															<option value="5000">5000</option>
															<option value="10000">10000</option>
														</select>
														 entries</label>
													</div>
													<div style="width: 35%;">
														<div id="productMgtTestCasePaginationDT" class="pagination toppagination" style="text-align: right;width: 100%;margin: 0px;"></div>
													</div>															
												</div>											
											</div>
																						
										</div>
										<div class="testScriptTab" xclass="jScrollContainerResponsive jScrollContainerFullScreen" style="clear:both;padding-top:1px;display:none;margin-right:5px;">
	      									<div class="row">
												<input type="hidden" id="hdnProductId" name="hdnProductId">
												<input type="hidden" id="hdnProductName" name="hdnProductName">
												<input type="hidden" id="hdnProductVersionId" name="hdnProductVersionId"> 
												<input type="hidden" id="hdnTestRunPlanId" name="hdnTestRunPlanId"> 
												<input type="hidden" id="hdnTestRunPlanGroupId" name="hdnTestRunPlanGroupId">
												<input type="hidden" id="hdntestFactoryId" name="hdntestFactoryId">
												<input type="hidden" id="hdnTestRunPlanType" name="hdnTestRunPlanType">
												<input type="hidden" id="hdnproductType" value="hdnproductType" >
												<input type="hidden" id="tcIdExecutionHistory" value="" >
												<input type="hidden" id="tcNameExecutionHistory" value="" >
											</div>
											<div id="jTableContainerTestCaseScript" class ="jTableContainerFullScreen"></div>
	         							</div>	
	         							<div class="testSuiteTab" style="clear:both;padding-top:10px;min-height:270px;margin-right:5px;">
											<div id="jTableContainertestsuite"></div>
											<!-- End Container Box -->
										</div>
										<div class="testPlanTab" style="margin-right:5px;">
											<div><%@include file="testRunPlan.jsp"%></div>
											<div style="clear:both;padding-top:10px;" >
												<div id="jTableContainertestrun"></div>
											</div>
										</div>
										<div class="testPlanGroupTab" style="clear:both;padding-top:10px;margin-right:5px;">
											<div id="dataTableContainerForTestplanGroup"></div>
											<div class="jScrollContainerResponsive jScrollContainerFullScreen" style="clear:both;padding-top:1px;display:none;">
		      									<div id="jTableContainertestrunplangroup" class ="jTableContainerFullScreen"></div>
		         							</div>
										</div>
										<div class="testDataTab" style="clear:both;padding-top:10px;margin-right:5px;">
											<div id="dataTableContainerForTestdata"></div>
										</div>
										<div class="objectrepositoryTab" style="clear:both;padding-top:10px;margin-right:5px;">
											<div id="dataTableContainerForObjectRepository"></div>
										</div>
										<div class="testDataAttachmentTab" style="clear:both;padding-top:10px;margin-right:5px;">
											<div id="dataTableContainerForTDAttachment"></div>
										</div>
										<div class="eDATAttachmentTab" style="clear:both;padding-top:10px;margin-right:5px;">
											<div id="dataTableContainerForEData"></div>
										</div>
									</div>
									<!-- Tests Tab Ends -->
									
									<!-- Team  Tab -->
									<div class="TeamTabPage tab-pane fade" id="TeamTabPage" style="overflow: hidden;height:73%;">
										<div id="teamRadioGrp" class="radio-toolbar col-lg-8 toolbarFullScreen">								
											<div class="btn-group team_radGrp" data-toggle="buttons" style="width:100%">
												<label class="btn darkblue active" data-name="team" onclick="showCorrespondingTeamTable(1);"><input type="radio" class="toggle" >Team</label>
												<label class="btn darkblue" data-name="teamutilization" onclick="showCorrespondingTeamTable(2);"><input type="radio" class="toggle" >Team Utilization</label>
											</div>
										</div>
										<div class="teamTab" style="clear:both;padding-top:10px;margin-right:5px;">
											<div id="jTableContainerProductTeamResources"></div>
										</div>
										<div class="teamUtilizationTab TeamUtilization" id="TeamUtilization" style="clear:both;padding-top:10px;"></div>
									</div>
									<!-- Tests Tab Ends -->
									
							</div>
							<!-- END PAGE CONTENT -->
						</div>
					</div>
					<!-- END PAGE-CONTAINER -->
				</div>
			</div>
			
		</div>
	</div>
</div>	
	<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayoutComponent.jsp"%></div>
	<!-- END FOOTER -->

	<!-- Popup -->
	<div id="div_PopupMain" class="divPopUpMain" style="display: none;">
		<div title="Press Esc to close" onclick="popupClose()"
			class="ImgPopupClose"></div>
	</div>
	
	<div id="div_PopupSelectedUserProfile" class="modal " tabindex="-1" >
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupCloseUserProfile()" aria-hidden="true"></button>
					<h4 class="modal-title">Profile</h4>
				</div>
				<div class="modal-body">
					<div class="scroller" style="height:500px" data-always-visible="1" data-rail-visible1="1">
	        			<div id="div_SelectedUserProfile"></div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="div_PopupTestcase" class="modal" tabindex="-1"
		aria-hidden="true" style="z-index:10053;">
		<div class="modal-dialog modal-dialog-dnd">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" onclick="popupTestcaseClose()"></button>
					<h4 id="headertag" class="modal-title">Map Testcases/TestSuites to
						TestSuite</h4>
				</div>
				<div class="modal-body" style="padding: 15px 0px;">
					<div class="scroller" style="height: 100%; padding: 0px;"
						data-always-visible="1" data-rail-visible1="1">
						<div class="col-md-12">
							<div class="container" id="test"
								style="height: 100%; float: left; display: inline-flex; padding: 0px;">
								<div class="portlet box purple-plum"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">										
										<h5>Available Test Cases
											<span class='badge badge-default' id='listCount_allTestcasesTestSuites' style='float:right;background:#a294bb'>0</span>
										</h5> 
										</div>
									
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="searchdev" placeholder="Type to search"></div>
									</div>
									<ul id="alltestcases" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; overflow-y: auto; background-color: white;">
									</ul>
								</div>
									
								</div>
								<div class="portlet box green"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">										
										<h5>Mapped Test Cases
											<span class='badge badge-default' id='listCount_TestcasesTestSuitesmapped' style='float:right;background:#076'>0</span>
										</h5>
									</div>
									
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="searchmapdev" placeholder="Type to search"></div>
									</div>
									<ul id="testcasemapped1" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; overflow-y: auto; background-color: white;">
										<li style="color: black;">No Test cases</li>
									</ul>
								</div>
									
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="div_PopupFeature" class="modal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" onclick="popupFeatureClose()"></button>
					<h4 id="headertag" class="modal-title">Map Features to
						Testcase</h4>
				</div>
				<div class="modal-body" style="padding: 15px 0px;">
					<div class="scroller" style="height: 100%; padding: 0px;"
						data-always-visible="1" data-rail-visible1="1">
						<div class="col-md-12">
							<div class="container" id="test"
								style="height: 100%; float: left; display: inline-flex; padding: 0px;">
								<div class="portlet box purple-plum"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">										
										<h5>Available Features
											<span class='badge badge-default' id='listCount_alltestcasefeatures' style='float:right;background:#a294bb'>0</span>										
										</h5>
									</div>
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="search_availfeatures" placeholder="Type to search"></div>
									</div>
									<ul id="allFeatures" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; overflow-y: scroll; background-color: white;">
										<li style="color: black;">No Features</li>
									</ul>
								</div>
									
								</div>
								<div class="portlet box green"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">										
										<h5>Mapped Features
											<span class='badge badge-default' id='listCount_testcasefeaturesmapped' style='float:right;background:#076'>0</span>											
										</h5>
									</div>
										<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="search_mappedfeatures" placeholder="Type to search"></div>
									</div>
									<ul id="featuresmapped" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; overflow-y: scroll; background-color: white;">
										<li style="color: black;">No Features</li>
									</ul>
								</div>
									
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
<!-- 	sat popup -->
<div id="div_PopupTestRunPlanGroup" class="modal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" onclick="popupTestRunPlanMapClose()"></button>
					<h4 id="headertag" class="modal-title">Map Test Plan Group
				 	</h4>
				</div>
				<div class="modal-body" style="padding: 15px 0px;">
					<div class="scroller" style="height: 100%; padding: 0px;"
						data-always-visible="1" data-rail-visible1="1">
						<div class="col-md-12">
							<div class="container" id="test1"
								style="height: 100%; float: left; display: inline-flex; padding: 0px;">
								<div class="portlet box purple-plum"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">										
									 <h5>Available Test Plan<span class='badge badge-default' id='listCount_avilabletestRunPlan' style='float:right;background:#a294bb'>0</span></h5> 
									</div>
									
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="search_availtestcases1" placeholder="Type to search"></div>
									</div>
									<ul id="allUnmappedTestcases1" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; overflow-y: scroll; background-color: white;">
										<li style="color: black;">No Test Plan</li>
									</ul>
								</div>
							</div>
								<div class="portlet box green"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">										
										<h5>Mapped Test Plan<span class='badge badge-default' id='listCount_mappedtestcases1' style='float:right;background:#076'>0</span></h5>
									</div>
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="search_mapptestcase1" placeholder="Type to search"></div>
									</div>
									<ul id="testcasesmappedtofeature1" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; overflow-y: scroll; background-color: white;">
										<li style="color: black;">No Test Plan</li>
									</ul>
								</div>
							</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div id="div_PopupFeatureTestCaseExecutionResult" class="modal " tabindex="-1"
	 aria-hidden="true">
		<div class="modal-dialog modal-lg" >
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" onclick="popupFeatureTestCaseExecutionResultClose()"></button>
					<h4 id="headertag" class="modal-title">Test Execution Results Statistics</h4>
				</div>
				<div class="modal-body" style="padding: 15px 0px;">
					<div class="scroller" style="height: 100%; padding: 0px;"
						data-always-visible="1" data-rail-visible1="1">
						<div class="col-md-12">
							<div class="container" id="test"
								style="height: 100%; float: left; display: inline-flex; padding: 0px;margin-top: -20px !important;">
								<div id="featureTestCasesExecutionResultContent" style="width:100%">
									<table id="featureTestCasesExecutionResult_dataTable"  class="display" cellspacing="0" width="100%">		
										<thead>
							            	<tr>							              				                	
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
							            	</tr>
							            </tfoot>
									</table>
								</div>								
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="div_PopupFeatureTestCaseExecutionDefects" class="modal " tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" onclick="popupFeatureTestCaseExecutionDefectsClose()"></button>
					<h4 id="headertag" class="modal-title">Test Execution Defect Statistics</h4>
				</div>
				<div class="modal-body" style="padding: 15px 0px;">
					<div class="scroller" style="height: 100%; padding: 0px;"
						data-always-visible="1" data-rail-visible1="1">
						<div class="col-md-12" >
							<div class="container" id="test"
								style="height: 100%; float: left; display: inline-flex; padding: 0px;margin-top: -20px !important;">
								<div id="featureTestCasesExecutionDefectsContent"  style="width:100%">
									<table id="featureTestCasesExecutionDefects_dataTable"  class="display" cellspacing="0" width="100%">		
										<thead>
							            	<tr>							              				                	
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
							            	</tr>
							            </tfoot>
									</table>
								</div>								
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="div_PopupDecouplingToTestCase" class="modal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" onclick="PopupDecouplingToTestCase()"></button>
					<h4 id="headertag" class="modal-title">Map Features to
						Testcase</h4>
				</div>
				<div class="modal-body" style="padding: 15px 0px;">
					<div class="scroller" style="height: 100%; padding: 0px;"
						data-always-visible="1" data-rail-visible1="1">
						<div class="col-md-12">
							<div class="container" id="test"
								style="height: 100%; float: left; display: inline-flex; padding: 0px;">
								<div class="portlet box purple-plum"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">										
										<div class="caption" style="font-size: 14px;line-height: 14px;">Available Decoupling Category
											<span class='badge badge-default' id='listCount_alltestcasedecoupling' style='float:right;background:#a294bb'>0</span> 
											<input type="text" id="searchdev" placeholder="Type to search">
										</div>
									</div>
									<ul id="alldecoupling" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; overflow-y: scroll; background-color: white;">
										<li style="color: black;">No Decoupling Category</li>
									</ul>
								</div>
								<div class="portlet box green"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">										
										<div class="caption" style="font-size: 14px;line-height: 14px;">Mapped Decoupling Category
											<span class='badge badge-default' id='listCount_testcasedecouplingsmapped' style='float:right;background:#076'>0</span>
											<input type="text" id="searchmapdev" placeholder="Type to search">
										</div>
									</div>
									<ul id="decouplingsmapped" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; overflow-y: scroll; background-color: white;">
										<li style="color: black;">No Features</li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="div_PopupTestCaseToDecoupling" class="modal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog modal-full">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" onclick="PopupTestCaseToDecoupling()"></button>
					<h4 id="headertag" class="modal-title">Map Testcase to Features
						</h4>
				</div>
				<div class="modal-body" style="padding: 15px 0px;">
					<div class="scroller" style="height: 100%; padding: 0px;"
						data-always-visible="1" data-rail-visible1="1">
						<div class="col-md-12">
							<div class="container" id="test"
								style="height: 100%; float: left; display: inline-flex; padding: 0px;">
								<div class="portlet box purple-plum"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">										
										<h5>Available Test Cases<span class='badge badge-default' id='listCount_alldecouplingtestcase' style='float:right;background:#a294bb'>0</span> 
										</h5>	
										
									</div>
									
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="search_dctc" placeholder="Type to search"></div>
									</div>
									<ul id="alldctestcases" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; overflow-y: scroll; background-color: white;">
										</ul>
								</div>
									
								</div>
								<div class="portlet box green"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">										
										<h5>Mapped Test Cases
											<span class='badge badge-default' id='listCount_decouplingtestcasesmapped' style='float:right;background:#076'>0</span>
										</h5>
									</div>
									
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="search_mapdctc" placeholder="Type to search"></div>
									</div>
									<ul id="dctestcasesmapped" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; overflow-y: scroll; background-color: white;">
										
									</ul>
								</div>
									
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="div_PopupDevices" class="modal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" onclick="popupDevicesClose()"></button>
					<h4 id="headertag" class="modal-title">Map Target Devices</h4>
				</div>
				<div class="modal-body" style="padding: 15px 0px;">
					<div class="scroller" style="height: 100%; padding: 0px;"
						data-always-visible="1" data-rail-visible1="1">
						<div class="col-md-12">
							<div class="container" id="test"
								style="height: 100%; float: left; display: inline-flex; padding: 0px;">
								<div class="portlet box purple-plum"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">
										<h5>Devices <span class='badge badge-default' id='listCount_alldevices' style='float:right;background:#a294bb'>0</span></h5>
									</div>
									
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="search_device" placeholder="Type to search"></div>
									</div>
									<ul id="alldevices" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; overflow-y: scroll; background-color: white;">
									</ul>
								</div>
									
								</div> 
								<div class="portlet box green"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">
										<h5>Mapped Devices<span class='badge badge-default' id='listCount_devicesmapped' style='float:right;background:#076'>0</span></h5>
									</div>
									
									<div class="portlet-body" >
									<div class="form-group autotext"><div class="input-icon"><i class="icon-magnifier"></i>
										<input type="text" class="form-control" id="search_mapdev" placeholder="Type to search"></div>
									</div>
									<ul id="devicesmapped" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; overflow-y: scroll; background-color: white;">
									</ul>
								</div>									
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>	
	<div id="div_PopupTerminal" class="modal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" onclick="popupTerminalClose()"></button>
					<h4 id="headertag" class="modal-title">Map Target Terminals</h4>
				</div>
				<div class="modal-body" style="padding: 15px 0px;">
					<div class="scroller" style="height: 100%; padding: 0px;"
						data-always-visible="1" data-rail-visible1="1">
						<div class="col-md-12">
							<div class="container" id="test1"
								style="height: 100%; float: left; display: inline-flex; padding: 0px;">
								<div class="portlet box purple-plum"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">
										<h5>Terminal<span class='badge badge-default' id='listCount_allterminals' style='float:right;background:#a294bb'>0</span></h5>
									</div>
									<ul id="allterminals" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; overflow-y: scroll; background-color: white;">
										<li style="color: black;">No Terminal</li>
									</ul>
								</div> 
								<div class="portlet box green"
									style="width: 100%; background: white;" data-force="30">
									<div class="portlet-title" style="min-height: 14px;">
										<h5>Mapped Terminal<span class='badge badge-default' id='listCount_terminalsmapped' style='float:right;background:#076'>0</span></h5>
									</div>
									<ul id="terminalsmapped" class="block__list block__list_tags"
										style="padding-left: 0px; height: 300px; overflow-y: scroll; background-color: white;">
										<li style="color: black;">No Terminal</li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

<div id="div_PopupExportResTestcase" class="modal " tabindex="-1" aria-hidden="true" >
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title">Import Test Cases</h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:320px" data-always-visible="1" data-rail-visible1="1">
					<div class="form-group">
						<div class="input-group">
							<div id="expRes_tc_types" class="icheck-list">	
							
							</div>	 
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<div class="col-md-6">
								   <button type="button" id="expSubmit" class="btn green-haze" onclick="importTestcasesFromTMS_Bulk(1);">Submit</button>
								</div>
								<div class="col-md-6">
								   <button type="button" id="expCancel" class="btn grey-cascade" onclick="importTestcasesFromTMS_Bulk(0);">Cancel</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


	<div id="div_PopupRunConfigurationList" class="modal fade" id="full" tabindex="-1" role="dialog" aria-hidden="true">
     <div class="modal-dialog modal-full">
         <div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title">Configure Test Plan</h4>
			</div>			
			<div class="modal-body">
					<div class="row">
					<div class="col-md-12 col-lg-12">
						<div class="col-md-12 col-lg-12">
							<div class="container" id="test" style="height: 100%; padding: 0px;margin-top: -20px !important;">								
								<div class="col-lg-12 col-md-12" id="treeJTableDivId"> 								
										<div id="recommendedHeader">
											<div style="padding:5px 10px 5px 0px;"><h4> Recommended Testcases</h4></div>
											<div id="executionTitle" style="padding:0px 10px 10px 0px; font-size:16px;display:inline-block;width:80%;"></div>	
											<span style="display:inline;">
												<div style="float: right;">
												   <button type="button" id="expSubmit" class="btn green-haze" onclick="runConfigurationSubmit(1);runConfigurationSubmit(0);">Execute</button>						
												   <button type="button" id="expCancel" class="btn grey-cascade" onclick="runConfigurationSubmit(0);">Cancel</button>
												</div>					
											</span>
										</div>						
										<div id="ConfigureTestBeds"></div>
								</div>													
							</div>					
						</div>
					</div>														
				</div>
			<!-- </div> -->
			<div class="row">
				<div style="padding-right: 45px;padding-top: 10px;float: right;">
				   <button type="button" id="expSubmit" class="btn green-haze" onclick="runConfigurationSubmit(1);runConfigurationSubmit(0);">Execute</button>						
				   <button type="button" id="expCancel" class="btn grey-cascade" onclick="runConfigurationSubmit(0);">Cancel</button>
				</div>				
			</div>			
		</div>
		</div>			
	</div>
</div>

<div id="div_AssessmentHistory" class="modal " tabindex="-1" aria-hidden="true" style="z-index:10052;width:96%;left:2%;top:2%;" >
	<div class="modal-full">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupClose()" aria-hidden="true"></button>
				<h4 class="modal-title">Risk Assessment History</h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:500px" data-always-visible="1" data-rail-visible1="1">
					<div id="dataTableContainerForAssessmentHistory"></div>
				</div>
			</div>
		</div>
	</div>
</div>

	<!-- productManagementPlan Product Version Details child Popup start-->
	<div id="productVersionPdMgmContainer" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Add/Edit Product Version</h4>
					</div>
					<div class="modal-body">					
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id=productVersionPdMgmDTContainer></div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
	
	<div id="productVersionPdSummaryContainer_div_modal" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px; display:none">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Product Versions</h4>
					</div>
					<div class="modal-body">					
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id=productVersionPdSummaryContainer></div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
	
	<!-- productManagementPlan Product Version Build Details child Popup start-->
	<div id="productVersionBuildPdMgmContainer" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Add/Edit Product Builds</h4>
					</div>
					<div class="modal-body">					
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id=productVersionBuildPdMgmDTContainer></div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
	
	
	<div id="productBuildPdSummaryContainer_div_modal" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px; display:none">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Product Builds</h4>
					</div>
					<div class="modal-body">					
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id=productBuildPdSummaryContainer></div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
	
	
	<!-- Intelligent TestPlan -->	
	<div id="recommendedTestPlanPdMgmContainer" class="modal fade" id="full" tabindex="-1" role="dialog" aria-hidden="true">
     	<div class="modal-dialog modal-full">
        	 <div class="modal-content" style="min-height:500px">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Intelligent TestPlan</h4>
						<div class="row">
							<div class="col-md-9" style="padding-left: 5px;">
								<h5 class="modal-title theme-font" style="padding-top: 5px;float: left;"></h5>
							</div>
						</div>
					</div>
					<div class="modal-body">	
						<div id="recommendedLoaderIcon"  style="display:none; vertical-align: middle;z-index:100001;position:absolute;top:5px;right:50%">	
							<img src="css/images/ajax-loader.gif"/>
						</div>
						<div id="recommendedTestcaseSummary"  style="margin: -12px 0px;"></div>					 
			 			<div id=recommendedTestPlanDTContainer></div>			 								 
					</div>
				</div>
		</div>
	</div>
	
	<!-- productManagementPlan User Permission Details child Popup start-->
	<div id="productUserPermissionPdMgmContainer" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Add/Edit User Permission for Product</h4>
					</div>
					<div class="modal-body">					
					 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
						  <div class="portlet-body" >	
							<div id="userPermissionListItemLoaderIcon"  style="display:none; vertical-align: middle;z-index:100001;position:absolute;top:5px;right:50%">	
								<img src="css/images/ajax-loader.gif"/>
							</div>	
			 				<div id=productUserPermissionPdMgmDTContainer></div>
						</div> 							 
			 		</div>
			 								 
					</div>
				</div>
		</div>
	</div>
		<div id="div_productCloning" class="modal " tabindex="-1" aria-hidden="true">
	<div class="modal-dialog" style="width: 400px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true" onclick="closeCloningContainer()"></button>
				<h4 class="modal-title caption-subject theme-font">Clone Product</h4>
			</div>
			<div class="modal-body" style="padding: 0px;">
								<div class="portlet light">								
							<div class="portlet-body form">
							  <div class="skin skin-flat">
									<form role="form">
										<div class="form-body" style="padding-top: 0px;padding-bottom: 0px;">									
											<div class="form-group">													
												<div class="input-group">
												 	<label>New Product Name</label>
												 </div>
												<div class="input-group">
												 
													 <input id="cloningProductName" class="form-control input-medium" type="text" name="cloning_new_productname">
												</div>
												<div class="row" id="plannedDateDiv">												
													<div class="input-group">
													 <label  >Planned Start Date:</label>
													 </div>
													 <div class="input-group">
														 <div class="">
														  <input id="startDate" NAME="cloningStartDate" class="form-control input-small  date-picker"  />
														</div>
													</div>
													<div class="input-group">
														  <label  >Planned End Date:</label>
													</div>
													<div class="input-group">	  
														<div class="">
														  <input id="endDate" NAME="cloningEndDate" class="form-control input-small  date-picker"  />
														</div>
													</div>		
													
													<div class="input-group">
														<div id="clonecheckboxes">
														<div> <label align ='center'>Clone For Below Actions</label></br></div>
															<div> 
																<label>TEAM <input id="team_clone" type="checkbox" value="team" checked="checked"></label>	
																<label>USER PERMISSION <input id="userPermission_clone" type="checkbox" value="userPermission" checked="checked"></label>
															</div>
															<div>
		   														<label>WORKFLOW <input id="workFlow_clone" type="checkbox" value="workFlow" checked="checked"></label>
		   														<label>WORKPACKAGE <input id="workpackage_clone" type="checkbox" value="workPackage" checked="checked"></label>
	   														</div>
														</div>
													</div>																					 													
												
												</div>
											</div>
										</div>
										<div class="form-actions" style="padding-top: 5px;padding-bottom: 5px;">
										<button type="button"  id = "cloneProductbt" onclick="submitProductCloneHandler();" class="btn green-haze" style="padding: 6px 6px;" >Submit</button>
											<button type="button" class="btn grey-cascade" onclick="popupCloseProductCloneHandler()">Cancel</button>
											
											
										</div>
									</form>
								</div>  
							</div>
						</div>
						<!-- </div> -->
				  <!--   </div> -->				
				</div>
		</div>
	</div>
</div>
		
	<div id="featureToBuildMappingContainer" class="modal fade" id="full" tabindex="-1" role="dialog" aria-hidden="true">
     	<div class="modal-dialog modal-full">
        	 <div class="modal-content" style="min-height:500px">
         
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
					<h4 class="modal-title theme-font">Mapping Feature To Build</h4>
				</div>
				<div class="modal-body">
					<div id="featureToBuildLoaderIcon"  style="display:none; vertical-align: middle;z-index:100001;position:absolute;top:5px;right:50%">	
						<img src="css/images/ajax-loader.gif"/>
					</div>					 
		 			<div id="featureToBuildMappingContent"></div>	 								 
				</div>
			</div>
		</div>
	</div>
	
	<div id="versionToTestcaseMappingContainer" class="modal fade" id="full" tabindex="-1" role="dialog" aria-hidden="true">
     	<div class="modal-dialog modal-full">
        	 <div class="modal-content" style="min-height:500px">
         
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
					<h4 class="modal-title theme-font">Mapping Version To Testcase</h4>
				</div>
				<div class="modal-body">
					<div id="versionTestcaseMappingLoaderIcon"  style="display:none; vertical-align: middle;z-index:100001;position:absolute;top:5px;right:50%">	
						<img src="css/images/ajax-loader.gif"/>
					</div>					 
		 			<div id="versionTestcaseMappingDT"></div>	 								 
				</div>
			</div>
		</div>
	</div>
		
<div id="div_testCaseGroupContainer" class="modal" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
	<div class="modal-full">
		<div class="modal-content">
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title theme-font">Test Scenarios</h4>
			</div>
			<div class="modal-body">					
				 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
	 				<div id="div_dataTableTestCaseGroup"></div>
	 			</div>					 
			</div>
		</div>
	</div>
</div>

<!-- Product feature child table-->
	
	<div id="productFeatureChild1_Container" class="modal" tabindex="-1"  aria-hidden="true" style="width: 95%; left: 2%; top: 2%; padding-left: 17px;">
		<div class="modal-full">
			<div class="modal-content" style="min-height:500px">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
					<h4 class="modal-title theme-font">Mapped Testcases</h4>
				</div>
				<div class="modal-body">					
					 <div class="scroller" style="height:100%" data-always-visible="1" data-rail-visible1="1">
		 				<div id="productFeaturePopupChild1"></div>
		 			</div>					 
				</div>
			</div>
		</div>
	</div>

	<!-- Product feature child1 table-->
	
	<div id="productTestCaseChild1_Container" class="modal" tabindex="-1" aria-hidden="true" style="width: 95vw; height: 95vh; left: 2%; top: 2%; padding-left: 17px;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
					<h4 class="modal-title theme-font">Test Step List</h4>
				</div>
				<div class="modal-body">	
					<div id="testStepStatusFilter_dd" style="position: absolute;z-index: 10;right: 165px;margin-top: 3px;display: block;">
						<select class="form-control input-small select2me" id="testStepStatusFilter_ul">
							<option value="2" ><a href="#">ALL</a></option>
							<option value="1" selected><a href="#">Active</a></option>
							<option value="0"><a href="#">InActive</a></option>
						</select>
					</div>				
					 <div class="scroller" style="height:100%" data-always-visible="1" data-rail-visible1="1">
		 				<div id="productTestCaseChild1"></div>
		 			</div>					 
				</div>
			</div>
		</div>
	</div>	
	
	<!-- Product feature child2 table-->
	
	<div id="productTestCaseChild2_Container" class="modal" aria-hidden="true" style="width: 95%; left: 2%; top: 2%; padding-left: 17px;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
					<h4 class="modal-title theme-font">Mapped Product Features</h4>
				</div>
				<div class="modal-body">					
					 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
		 				<div id="productTestCaseChild2"></div>
		 			</div>					 
				</div>
			</div>
		</div>
	</div>
		
<!-- BEGIN: Convert DataTable ChildTablePopup-->
	<div id="workpackageDT_Child_Container" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Workpackage</h4>
					</div>
					<div class="modal-body">					
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id="dataTableContainerForWorkpackage"></div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
	<div id="testPlanConfigureDT_Child_Container" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;z-index:10052;">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Properties</h4>
					</div>
					<div class="modal-body">					
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id=dataTableContainerForTPConfigurationProperties></div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
	<div id="runConfigurationDT_Child_Container" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Test Configuration</h4>
					</div>
					<div class="modal-body">					
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id=jTableContainerForRunConfiguration></div>
			 				<div id=dataTableContainerForTestConfiguration></div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
	<div id="testSuiteDT_Child_Container" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;z-index:10052;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
					<h4 class="modal-title theme-font">Test Suite</h4>
				</div>
				<div class="modal-body">					
					 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
		 				<div id="jTableContainertestsuite"></div>
		 			</div>					 
				</div>
			</div>
		</div>
	</div>
	<div id="testSuiteMapDT_Child_Container" class="modal fade" id="full" tabindex="-1" role="dialog" aria-hidden="true" style="z-index:10053;width:99%;left:0%;top:-2%;">
	   	<div class="modal-dialog modal-full">
	       	 <div class="modal-content" style="min-height:600px">
	        
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
					<h4 class="modal-title theme-font">View Testcases</h4>
				</div>
				<div class="modal-body">
					<div id="testSuiteTestCaseListChildLoaderIcon"  style="display:none; vertical-align: middle;z-index:100001;position:absolute;top:5px;right:50%">	
						<img src="css/images/ajax-loader.gif"/>
					</div>					 
		 			<div id="dataTableContainerForViewTestCases"></div>	 								 
				</div>
			</div>
		</div>
	</div>	
	<div id="tmsDT_Child_Container" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Add/Remove Test Management System</h4>
					</div>
					<div class="modal-body">					
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id="dataTableContainerForTMSMapping"></div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
	<div id="dmsDT_Child_Container" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Add/Remove Defect Management System</h4>
					</div>
					<div class="modal-body">					
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id="dataTableContainerForDMSMapping"></div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
	<div id="riskAssessmentDT_Child_Container" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Risk Assessment</h4>
					</div>
					<div class="modal-body">					
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id="dataTableContainerForRiskAssessment"></div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
<!-- END: Convert DataTable ChildTablePopup-->

<div id="testSuiteTestCaseListChild_Container" class="modal fade" id="full" tabindex="-1" role="dialog" aria-hidden="true" style="z-index:10052;">
   	<div class="modal-dialog modal-full">
       	 <div class="modal-content" style="min-height:500px">
        
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title theme-font">Select Testcases for execution</h4>
				<h5 class="modal-title theme-font">The Testcases selected will be executed. Selecting here will not affect the actual mapping of the Testcase to the Testsuite.</h5>
			</div>
			<div class="modal-body">
				<div id="testSuiteTestCaseListChildLoaderIcon"  style="display:none; vertical-align: middle;z-index:100001;position:absolute;top:5px;right:50%">	
					<img src="css/images/ajax-loader.gif"/>
				</div>					 
	 			<div id="testSuiteTestCaseListChild_Content"></div>	 								 
			</div>
		</div>
	</div>
</div>

<div id="enviornmentChild_Container" class="modal fade" id="full" tabindex="-1" role="dialog" aria-hidden="true">
   	<div class="modal-dialog modal-full">
       	 <div class="modal-content" style="min-height:500px">
        
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title theme-font">Environment List</h4>
			</div>
			<div class="modal-body">
				<div id="enviornmentChildLoaderIcon"  style="display:none; vertical-align: middle;z-index:100001;position:absolute;top:5px;right:50%">	
					<img src="css/images/ajax-loader.gif"/>
				</div>					 
	 			<div id="enviornmentChild_Content"></div>	 								 
			</div>
		</div>
	</div>
</div>

<div id="tpgDT_Child_Container" class="modal fade" id="full" tabindex="-1" role="dialog" aria-hidden="true" style="width:96%;top:2%;left:2%;">
   	<div class="modal-full">
       	 <div class="modal-content" style="min-height:500px">
        
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title theme-font">Mapped Test Plan Group</h4>
			</div>
			<div class="modal-body">
				<div id="enviornmentChildLoaderIcon"  style="display:none; vertical-align: middle;z-index:100001;position:absolute;top:5px;right:50%">	
					<img src="css/images/ajax-loader.gif"/>
				</div>					 
	 			<div id="dataTableContainerForTestPlanGroupMappedTCList"></div>	 								 
			</div>
		</div>
	</div>
</div>

<div id="tpgTestCycleDT_Child_Container" class="modal fade" id="full" tabindex="-1" role="dialog" aria-hidden="true" style="width:96%;top:2%;left:2%;">
   	<div class="modal-full">
       	 <div class="modal-content" style="min-height:500px">
        
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title theme-font">Test Cycle</h4>
			</div>
			<div class="modal-body">
				<div id="enviornmentChildLoaderIcon"  style="display:none; vertical-align: middle;z-index:100001;position:absolute;top:5px;right:50%">	
					<img src="css/images/ajax-loader.gif"/>
				</div>					 
	 			<div id="dataTableContainerForTPGTestCycle"></div>	 								 
			</div>
		</div>
	</div>
</div>

<div id="div_PopupTestcaseAnalytics" class="modal " tabindex="-1" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title caption-subject theme-font">Testcase Analytics</h4>
			</div>
			<div class="modal-body">
			
				<div id="testcaseMetics" style="overflow: hidden; height: 100%;">
				
					<div class="table-scrollable">
						<table class="table table-striped table-hover" id="resTable">
							
							<tbody>
								<tr>
									<td>Success Rate :
									</td>
									<td><span id="testcaseSuccessRate"></span></td>
								</tr>
								
								<tr>
									<td>Feature Coverage :
									</td>
									<td><span id="featureCoverage"></span></td>
								</tr>
								
								<tr>
									<td>Build Coverage :
									</td>
									<td><span id="buildCoverage"></span></td>
								</tr>
								
								<tr>
									<td>Average Execution Time :
									</td>
									<td><span id="testCaseAvgExecutionTime"></span></td>
								</tr>
								
								<tr>
									<td>Age of Testcase :
									</td>
									<td><span id="testCaseAge"></span></td>
								</tr>
								
								<tr>
									<td>Testcase Quality Index :
									</td>
									<td><span id="testcaseQualityIndex"></span></td>
								</tr>
								
								<tr>
									<td>%ile of Testcase :
									</td>
									<td><span id="testCasePercentage"></span></td>
								</tr>
																														
							</tbody>
						</table>
					</div>
										
										
				</div>
				
						
			</div>
		</div>
	</div>
</div>
<div id="listTestdataDT_Child_Container" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font"></h4>
					</div>
					<div class="modal-body">					
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id=dataTableContainerForTestdataChildTable></div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
	
	<div id="div_projectTemplates" class="modal " tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog" style="width: 300px;">
			<div class="modal-content" style="height: 100%;">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						title="Press Esc to close" onclick="closeGenerateScriptHandler()"
						aria-hidden="true"></button>
					<h4 class="modal-title">Title</h4>
				</div>
				<div class="modal-body">
					<div class="portlet light">
						<div class="portlet-body">
							<!--  <div id="generateScriptLoaderIcon" style="display:none;z-index:100001;position:absolute;top:33%;left:45%"> 
								<img src="css/images/ajax-loader.gif"/>
							</div> -->

							<div class="skin skin-flat">
								<form role="form">
									<div class="form-body">

										<div class="form-group">
											<div class="input-group">
												<label>Test Tools : </label>
												<div id="downloadProjectRadio" class="icheck-list">
													<!-- <label><input type="radio" name="radio1" checked class="icheck" id="TAF-MODE" data-radio="iradio_flat-grey">TAF</label> -->
													<label><input type="radio" name="radio1" checked class="icheck" id="SELENIUM" data-radio="iradio_flat-grey">SELENIUM</label>
													<label><input type="radio" name="radio1" class="icheck" id="APPIUM" data-radio="iradio_flat-grey">APPIUM</label>
 													<label><input type="radio" name="radio1"  class="icheck" id="PROTRACTOR" data-radio="iradio_flat-grey">PROTRACTOR</label>
 													<label><input type="radio" name="radio1"  class="icheck" id="SEETEST" data-radio="iradio_flat-grey">SEETEST</label>
 													<label><input type="radio" name="radio1"  class="icheck" id="CODEDUI" data-radio="iradio_flat-grey">CODEDUI</label>
 													<label><input type="radio" name="radio1"  class="icheck" id="TESTCOMPLETE" data-radio="iradio_flat-grey">TESTCOMPLETE</label>
												</div>
											</div>
										</div>

										<div class="form-group" style="padding-left: 55px;">
											<button type="button" class="btn grey-cascade"
												onclick="closeProjectTemplate()">Cancel</button>
											<button type="button" onclick="downloadProjectTemplate();"
												class="btn green-haze">Submit</button>

										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	

<div><%@include file="atsg.jsp"%></div>
<%-- <div><%@include file="scriptPromtWindow.jsp"%></div> --%>
<div><%@include file="treePopupLayout.jsp"%></div>
<div><%@include file="scheduleTestRun.jsp"%></div>
<div><%@include file="atsghelp.jsp"%></div>
<div><%@include file="dragDropListItems.jsp"%></div>

<div><%@include file="codeEditor.jsp"%></div>

<%-- <div><%@include file="codeEditorForBDDScript.jsp"%></div> --%>
<div><%@include file="scriptPromtWindow.jsp"%></div>
<div><%@include file="testCaseScriptlessExecution.jsp"%></div>
<div><%@include file="generateScriptSelection.jsp"%></div>


<div><%@include file="cloneBuild.jsp"%></div>
<div><%@include file="testData.jsp" %></div>
<div><%@include file="addComments.jsp"%></div>
<%-- <div><%@include file="attachments.jsp"%></div> --%>

<div><%@include file="codeEditorForBDDScript.jsp"%></div>
<div><%@include file="codeEditorForBDDScriptEmbedded.jsp"%></div>
<div><%@include file="singleDataTableContainer.jsp"%></div>

<!-- Product Bots Child Popup end-->

	<div id="div_SingleDataTableBotCommand" class="modal" tabindex="-1" aria-hidden="true" style="z-index: 10052; width: 96%;left: 2%;top: 2%; padding: 0px;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
					<h4 class="modal-title theme-font">Bot Commands</h4>
				</div>
				<div class="modal-body">					
					 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
		 				<div id="dataTableSingleContainer_botCommand"></div>
		 			</div>					 
				</div>
			</div>
		</div>
	</div>
	
	<div id="div_PopupBackground"></div>
	<!-- Popup Ends -->
			
	<script type="text/javascript" src="js/Scripts/star-rating/jquery.rating.js"></script>		
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
	<!-- <script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script> -->
	<script src="js/bootstrap-datetimepicker.min.js" type="text/javascript" ></script>
	<script src="js/moment.min.js" type="text/javascript"></script>
	<script src="js/daterangepicker.js" type="text/javascript"></script>
	<script src="js/components-pickers.js" type="text/javascript"></script>
	
	<link rel="stylesheet" href="js/Scripts/star-rating/jquery.rating.css" type="text/css">
	
	<!-- the jScrollPane script -->
	<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

	<!-- Include jTable script file. -->
	<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>

	<!-- Plugs ins for jtable inline editing -->
	<script type="text/javascript"
		src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
	<script type="text/javascript"
		src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>

	<!-- Validation engine script file and english localization -->
	<script type="text/javascript"
		src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
	<script type="text/javascript"
		src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>
		
	<script type='text/javascript' src="files/lib/chart/script/ResizeSensor.js"></script>
			
	<div><%@include file="schedule.jsp"%></div>
 	<div><%@include file="testCaseExecutionDetails.jsp" %></div>
	<div><%@include file="testCaseDetails.jsp" %></div>
	<div><%@include file="dynamicJtableContainer.jsp"%></div>	
	<div><%@include file="singleJtableContainer.jsp"%></div> 	
 	<div><%@include file="testCaseOverAllView.jsp" %></div>
 	<div><%@include file="featureOverAllView.jsp" %></div>
		
	<input type="hidden" id="toenableTabs" value="<c:out value="${toenableTab}"/>">
	
	<% ResourceBundle rb = ResourceBundle.getBundle("TAFServer");
		String autoAllocate=rb.getString("ACTIVITY_AUTOALLOCATE");  	
		String changeReqeustTitleAWP = rb.getString("CHANGEREQUEST_TABLE_TITLE");	
	%>
	
	<script type="text/javascript">	
	var autoAllocateFromProperty = "<%=autoAllocate%>";
	var useCaseAWP = "<%=changeReqeustTitleAWP%>";
	var defaultuserId = '${userId}';

	  jQuery(document).ready(function() {
			 // ----- For the transition - opening and closing side bar given max time is 0.5 sec. -----
			$(document).on('click', "#menuList .dropdown-quick-sidebar-toggler .dropdown-toggle" ,function(){
				resizeDataTablesofProducts();	
			});			
			
			function resizeDataTablesofProducts(){
				var selectedTab= $("#tabslist>li.active").index();			
				
				setTimeout(function(){			
					if(nodeType == "TestFactory"){
						productManageMentDTFullScreenHandler(true);
						
					}else if(selectedTab == 5){ 
						// Traceability view for 5--- 
						$('#featureTestCases_dataTable_wrapper').resize();								
					}
				},600);
			}
		});
	</script>
	
	<script type="text/javascript">	
		var pageType="PRODUCTMANAGEMENTPLANVIEW";
		var testsuitesTabFlag=false;
		var productType;
		var enableATSGFlag="${enableATSG}";
		var userRoleId='-1';
		var userId='-1';				
		var engageMentPlanmgmDTLable = "${productMgmGridEngagementHeader}";
		var prodcuctplanmgmtLabel = "${productMgmGridProductHeader}"; 
		var treeArr=[];
		var atlasIframeFlag=false;
		
		jQuery(document).ready(function() {
			userRoleId='<%=session.getAttribute("roleId")%>';
			userId='<%=session.getAttribute("ssnHdnUserId")%>';
						
			var prodcuctGridHeaderEngagementLabel = "${productMgmGridEngagementHeader}";
			var prodcuctGridHeaderProductLabel = "${productMgmGridProductHeader}"; 
			
			labelChanges(prodcuctGridHeaderEngagementLabel, prodcuctGridHeaderProductLabel);
			window.addEventListener('message', function(e) {
				console.log("In inline screen...");
				atlasIframeFlag=true;
				if(typeof(e.data.clonedProductBuildId) == 'undefined'){
					featureOverAllView(e.data);		
					$('.page-content').css('display','none');	
					$('#featureDetailedViewsModal').css({'width':'100%','left':'0','top':'0', 'bottom':'0'});
					$('#featureDetailedViewsModal button').addClass('hidden');
					var containerID = "productFeatureChild1";
					var urlToGetFeaturesOfSpecifiedProductId = 'product.feature.testcase.mappedlist?productFeatureId='+e.data.productFeatureId+'&jtStartIndex=0&jtPageSize=10000';		
					listFeaturesOfSelectedProduct(urlToGetFeaturesOfSpecifiedProductId, "240px", "childTable1", containerID, e.data.productId);
				}else{
					openLoaderIcon();
					setTimeout(function(){callSelectedProduct(e.data);},5000);
				}
			});
			$("#treeContainerDiv").bind('ready.jstree', function(event, data) {
				var $tree = $(this);
				$($tree.jstree().get_json($tree, {
					flat: true
				}))
				.each(function(index, value) {
					if(typeof(value.data) != 'undefined')
					treeArr.push({"pId": (value.data).split('~')[0],"pName": value.text,"id":value.id});
				});
				
			});
			
		});	
		
		function callSelectedProduct(data){			
			var productIdFlag=false;
			for(var i=0;i<treeArr.length;i++){
				if((treeArr[i].pId==data.productId) && (treeArr[i].pName==data.productName)){
					$.jstree.reference('#treeContainerDiv').select_node(treeArr[i].id);
					productIdFlag=true;
					break;
				}
			}
			console.log("productIdFlag="+productIdFlag);
			closeLoaderIcon();
			if(!productIdFlag){
				$('.page-content #toAnimate').addClass('hidden');
				//callAlert("User permission does not exist for the user");
				bootbox.alert("User permission does not exist for the product", function(){
					var messageData = {};
					messageData["action-request"] = "close-model";
					messageData["element-id-for-action"] = "createTAFViewUI";
					parent.postMessage(messageData, '*');
				});
			}else{
				$('.page-content #toAnimate').removeClass('hidden').css('margin-left','0px');
			}
			$('.page-content').removeClass('hidden');
			isEngagementLevelFlag = false;
			productId = data.productId;
			productVersionId=-1;
			buildId=0;
			productNameForWorkflowSummary = data.productName;
			document.getElementById("hdnProductName").value = data.productName;
			testFactoryId = document.getElementById("treeHdnCurrentTestFactoryId").value;
			document.getElementById("hdnProductId").value=productId;
			document.getElementById("hdntestFactoryId").value = testFactoryId;
			fetchProductType(productVersionId, productId);
			if(productId==null && productId==undefined){
				productId=0;
			}						
			showCoreResourcesTab(testFactoryId,productId,0);
			document.getElementById("toenableTabs").value="NO";
			showRiskTab();	
		}
		
		var resize="resize";
		var fullScreenProductManagement="fullscreen";
		function onResizeDocument() {
			//console.log("resized");			
			fullScreenHanlder(resize);
		}
									
		function fullScreenHanlder(value){			
			var flag=false;
			
			switch(value){
				case resize:
					flag=true;
				break;
				
				case fullScreenProductManagement:
					flag=false;
				break;
				
				default:
					flag=false;
				break;
			}
			
			if(!$('#toAnimate .portlet-title .fullscreen').hasClass('on') && flag==false){
				$('#testFacMode .jScrollContainerResponsive').css('max-height',displaytestFaceModeResponsive(window.innerWidth));				
			}
			else if($('#toAnimate .portlet-title .fullscreen').hasClass('on') && flag==true){
				
				var height = Metronic.getViewPort().height -
                $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
                parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
                parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
				
				$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
				$('#testFacMode .jScrollContainerResponsive').css('max-height',displaytestFaceModeResponsive(window.innerWidth));
				
			}
			else{
				$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
				$('#testFacMode .jScrollContainerResponsive').css('max-height','');
			}
		}
		
		function displaytestFaceModeResponsive(widthValue){
			var resultWidth="";
			if(widthValue<768){
				resultWidth = 200;			
			}else if(widthValue<992){
				resultWidth = 300;
			}else if(widthValue<1200){
				resultWidth = 400;
			}else if(widthValue<1500){
				resultWidth = 500;			
			}else if(widthValue<1600){
				resultWidth = 600;
			}else if(widthValue<1800){
				resultWidth = 700;
			}else if(widthValue<2050){
				resultWidth = 750;
			}else if(widthValue<2400){
				resultWidth = 850;
			}else if(widthValue<3000){
				resultWidth = 1100;
			}else if(widthValue<4000){
				resultWidth = 1300;
			}else if(widthValue<5000){
				resultWidth = 1500;
			}
			
			return resultWidth+'px';
		}
		
		function testCaseDetailsForResult(event){
				 var rawdata = event.target.id;
					var splitdata = rawdata.split(/[~@]+/);
					var tcexeId = splitdata[0];
					var testCaseId = splitdata[1];
					var testCaseName = splitdata[2];
			var jsonObj={"Title":"Testcase Details :- "+ testCaseId + "- "+testCaseName,
					     "testCaseID":testCaseId,
						 "testCaseName":testCaseName ,				 
						 "testCaseDetailsTitle":"Details",
						 "executionId":tcexeId,
						 "testCaseDetailsUrl":"workpackage.result.testcase.details?testCaseExecutionResultId="+tcexeId+"&testcaseId="+testCaseId,
			};
			TestCaseDetails.init(jsonObj);	
		}		
		
		function testCaseOverAllView(event){
				 var rawdata = event.target.id;
					var splitdata = rawdata.split(/[~@]+/);
					var testCaseId = splitdata[0];
					var testCaseName = splitdata[1];
			var jsonObj={"Title":"Testcase Details :- "+ testCaseId + "- "+testCaseName,
					     "testCaseID":testCaseId,
						 "testCaseName":testCaseName ,				 
						 "testCaseDetailsTitle":"Details",
						 //"executionId":tcexeId,
						 "testCaseOverAllDetailsUrl":"product.testcase.overall.details?testCaseId="+testCaseId,
			};
			TestCaseOverallViewDetails.init(jsonObj);	
		}
		
		function featureOverAllView(data){
			var featureId = data.productFeatureId;
			var featureName = data.productFeatureName;
			var jsonObj={"Title":"Feature Details :- "+ featureId + "- "+featureName,
				 "featureId":featureId,
				 "featureName":featureName,
				 "featureDetailsTitle":"Feature Details",
				 "containerId":"relatedTestcaseDetails",
				 "featureOverAllDetailsUrl":"product.feature.overall.details?featureId="+featureId,
			};
			FeatureOverallViewDetails.init(jsonObj);	
		}		
		
		// --- for the dataTable fullscreen ----

		function fullScreenHandlerDTProductManagementPlan(){
			
			if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
				
				var height = Metronic.getViewPort().height -
                $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
                parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
                parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
				
				$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
				$('#testFacMode').css('max-height',displaytestFaceModeResponsive(window.innerWidth));
				
				if(nodeType == "TestFactory"){	
					productManageMentDTFullScreenHandler(true);
					reInitializeDTProductManagementPlan();
				}else{
					$("#pdtMode").show();
					
					selectedTab=$("#tabslist>li.active").index();
					if (selectedTab==2){ // Feature
						reInitializeDTProductFeature();		
						
					}else if (selectedTab==3){ // testcase
						reInitializeDTProductTestCases();
					}
				}				
			}
			else{
				$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
				$('#testFacMode').css('max-height','');
				
				if(nodeType == "TestFactory"){	
					reInitializeDTProductManagementPlan();				
					productManageMentDTFullScreenHandler(false);			
				}else{
					$("#pdtMode").hide();
					
					selectedTab=$("#tabslist>li.active").index();
					if (selectedTab==2){ // Feature
						reInitializeDTProductFeature();		
						
					}else if (selectedTab==3){ // testcase
						reInitializeDTProductTestCases();
					}
				}			
			}
		}
		
		function labelChanges(headerLabel, productLabel){
			var title ="Manage "+productLabel;
				$('#products_dataTable  #Engagementth').text(headerLabel);
				$('#products_dataTable  #productidlbl').text(productLabel+"ID");
				$('#products_dataTable  #product').text(productLabel);
				//$('#page-header #currPageTitle').text("Manage SubCompetency");
				$('#page-header #currPageTitle').text("Manage"+productLabel);
				$("#page-header #currSelSubMenu").text(productLabel+"Management");
				setBreadCrumb(title);				
				$('.page-content .toolbarFullScreen .caption-subject').eq(0).text(productLabel);
				$('#productVersionPdMgmContainer .modal-title').text("Add/Edit "+productLabel +" Version");			
		}
		
	</script>	 

 <!-- <link href="css/scott.css" rel="stylesheet" type="text/css"></link> -->
 
<div><%@include file="comments.jsp"%></div>

<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
<div><jsp:include page="workpackagesComponent.jsp"></jsp:include></div>
<script type="text/javascript" src="js/datatable/Editor-1.5.6/js/dataTables.editor.js"></script>

<script src="js/importData.js" type="text/javascript"></script>
<script src="js/productActivityWorkPackageViewComponent.js" type="text/javascript"></script>
<script src="js/pageSpecific/download.js" type="text/javascript"></script> 

 <script src="js/viewScript/testCaseGroup.js" type="text/javascript"></script>
 <script src="js/viewScript/recommentedTestPlan.js" type="text/javascript"></script>
 <script src="js/viewScript/productManagePlanDT.js" type="text/javascript"></script>
 <script src="js/viewScript/testRunPlanConfiguration.js" type="text/javascript"></script>
 <script src="js/viewScript/productManagementFeature.js" type="text/javascript"></script>
 <script src="js/viewScript/productManagementTestCase.js" type="text/javascript"></script>
 <script src="js/viewScript/mappingVersionToTestCase.js" type="text/javascript"></script>
 <script src="js/viewScript/productManagementTestScript.js" type="text/javascript"></script>
 <script src="js/viewScript/productManagementTestSuite.js" type="text/javascript"></script>
 <script src="js/viewScript/decoupling.js" type="text/javascript"></script>
 <script src="js/viewScript/environment.js" type="text/javascript"></script>
 <script src="js/viewScript/productTeam.js" type="text/javascript"></script>
 
<script src="js/viewScript/teamUtilization.js" type="text/javascript"></script>
<script src="js/viewScript/productManagePlan.js" type="text/javascript"></script>
<script src="js/viewScript/testRunPlanDT.js" type="text/javascript"></script>

<script src="js/viewScript/mappingFeatureToBuild.js" type="text/javascript"></script>
<script src="js/viewScript/instanceWorkflowConfiguration.js" type="text/javascript"></script>
 <script src="js/viewScript/instanceWorkflowSummary.js" type="text/javascript"></script>
<script src="js/viewScript/activityChangeRequest.js" type="text/javascript"></script>	
 <script src="js/viewScript/WorkflowIndicatorDetailView.js" type="text/javascript"></script>  
 <script src="js/viewScript/atsgEditorCommon.js" type="text/javascript"></script>  
  
 <script src="files/lib/metronic/theme/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<div id="div_PopupBackground"></div> 
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>