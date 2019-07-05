<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.hcl.atf.taf.controller.HomePageController"%>
<%@page import="com.hcl.atf.taf.model.UserList"%>
<%@page import="java.util.List"%>
<%@ page import = "java.util.ResourceBundle" %>
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

<spring:eval var="activityInvisibleColumnIndex" expression="@ilcmProps.getProperty('ACITIVYLIST_INVISIBLE_COLUMN_INDEX')"/>

<link type="text/css" href="css/jquery.jscrollpane.css" rel="stylesheet" media="all" />
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet"	type="text/css"></link>
<link href="css/customStyle.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="css/iosOverlay.css" type="text/css" media="all">
<link href="js/datatable/Editor-1.5.6/css/dataTables.editor.css" rel="stylesheet" type="text/css"></link>
<link href="js/datatable/Editor-1.5.6/css/editor.dataTables.css" rel="stylesheet" type="text/css"></link>


<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>

<!-- END THEME STYLES -->

<style type="text/css">

  .select2-drop-active {
	z-index:100060 !important;
  }

#filterIsActiveGroup> #status_dd >.select2me{
	height: 26px;
    padding-top: 1px;
    margin-top: 3px;
    width: 100px !important;
    font-size: 13px;
}
#filterIsActiveGroup > #status_dd >.select2me > a{
	height: 30px;
	margin-top: -1px;
	padding-top: 0px;
}
#status_dd > .form-control .select2-choice {
 	height: 28px; 
 }
 
div.DTE_Body div.DTE_Body_Content div.DTE_Field {
    width: 50%;
    padding: 5px 20px;
    box-sizing: border-box;
}
 
div.DTE_Body div.DTE_Form_Content {
    display:flex;
    flex-direction: row;
    flex-wrap: wrap;
}

.customPagination{
    padding: 2px 5px 2px 5px;
    margin-right: 2px;
    font-weight: bold;
    color: #000;
    background-color: #adadad;
    border: 1px solid #878585;
}

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
			<!-- BEGIN MEGA MENU -->			
			 <div><%@include file="menu.jsp" %></div>			
 			<!-- END MEGA MENU -->
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->

<!-- BEGIN CONTAINER -->
<div class="page-container reduceFooter">
<%-- <div><%@include file="treeStructureSidebar.jsp" %></div> --%>

<div><%@include file="treeStructureSidebarLazyLoading.jsp" %></div>
<div><%@include file="postHeader.jsp" %></div>
<div><%@include file="dynamicJtableContainer.jsp"%></div>

<!-- BEGIN PAGE CONTENT -->
<div class="page-content">
	<div class="container-fluid">			
		<!-- BEGIN PAGE CONTENT INNER -->
		<div class="row margin-top-10" id="toAnimate">
			<div class="col-md-12">
				<!-- BEGIN PORTLET-->
				<div class="portlet light" style="padding: 2px 20px 5px;">
					<div class="portlet-title toolbarFullScreen" style="margin-bottom:0px;">
						<div class="caption caption-md">
							<span id="breadCrumHeader" class="caption-subject theme-font bold uppercase">ACTIVITY WORKPACKAGE</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span> 
							<span class="caption-helper hide">weekly stats...</span>
						</div>
											
						<div class="actions" style="padding-top: 4px;padding-left: 5px; display:none">
							<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" 
								onClick="fullScreenHandlerDTActivities()" data-original-title="" title="FullScreen"></a>
						</div>
						<div id="workflow_status_dd" style="float:right; display: block;">
							<select class="form-control input-small select2me" id="workflow_status_ul" style="float:right;">
								<option value="All" selected><a href="#">ALL</a></option>
								<option value="Begin" ><a href="#">BEGIN</a></option>
								<option value="Intermediate" ><a href="#">INTERMEDIATE</a></option>
								<option value="End" ><a href="#">END</a></option>
								<option value="Abort" ><a href="#">ABORT</a></option>								
							</select>
						</div>				
					</div>				
					
					<!-- Content -->
					<div id="hdnDiv"> </div>
					<div id="hdnDemandDiv"> </div>
					<input type="hidden" id="testFactoryKey" value='null'>
					<input type="hidden" id="productKey" value='null'>
					<input type="hidden" id="activityWorkPackageKey" value='null'>
					<input type="hidden" id="activityTaskId" value='null'>
					<input type="hidden" id="hdnproductId" value="" >
					<div class="portlet-body"  >					
						
						<div id="activityEngagementToggle" class="row prodTabCntnt">
							<div class="col-md-12 toolbarFullScreen">
				   				<ul class="nav nav-tabs" id="tablistEngagementACTWP">
				   					<li class="active"><a href="#engagementOverview" data-toggle="tab" onclick="showActivityAtEngagementLevel(0);">Overview</a></li>						
      								<li><a href="#engagementDashBoard" data-toggle="tab" onclick="showActivityAtEngagementLevel(1);">Dashboard</a></li>
				      				<li><a href="#activityWorkpackageTab" data-toggle="tab" onclick="showActivityAtEngagementLevel(2);">WorkPackages</a></li>						
				      				<li><a href="#activityTab" data-toggle="tab" onclick="showActivityAtEngagementLevel(3);">Activities</a></li>
				       			</ul>
							</div>
						</div>
						
						<div class="tab-content prodCntntDetails" style="padding-left: 0px;padding-right: 15px;" >
							<div id="engagementOverview" class="tab-pane fade active in">
								<div class="panel panel-default" style="margin-left: 15px;">					
									<div class="row"  id="engagementActWorkPackageDiv">
										<div class="col-md-6">
											<div class="table-scrollable">
												<table class="table table-striped table-hover" id="engagementdfnTable">
													<thead>
													<tr height="30">
														<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Overview</th>
														<th>
														</th>
													</tr>
													</thead>
													<tbody>
													
													<tr>
														<td>TestFactory Id :
														</td>
														<td>
														<span id="engagementTestFactoryId"></span>
														</td>
													</tr>
													
													<tr>
														<td>TestFactory Name :
														</td>
														<td><span id="engagementNamePS"></span>
														</td>
													</tr>	
													
													<tr>
														<td>Description :
														</td>
														<td>
														<span id="engagementDescPS"></span>
														</td>
													</tr>
													
													
													
													<tr>
														<td>Country :
														</td>
														<td>
														<span id="engagementCountryPS"></span>
														</td>
													</tr>												
													
													<tr>
														<td>State :
														</td>
														<td>
														<span id="engagementStatePS"></span>
														</td>
													</tr>
													
													<tr>
														<td>City :
														</td>
														<td>
														<span id="engagementCity"></span>
														</td>
													</tr>
													<tr>
														<td>Status :
														</td>
														<td>
														<span id="engagementStatusPS" contenteditable="true"></span>
														</td>
													</tr>													
																	
													</tbody>
												</table>
											</div>
										</div>
										<div class="col-md-6">
											<div class="table-scrollable">
												<table class="table table-striped table-hover" id="engagementAuditTable">
													<thead>
													<tr height="30">
														<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Audit </th>
														<th>
														</th>
													</tr>
													</thead>
													<tbody>					
														<tr>
														<td>Created On :
														</td>
														<td><span id="engagementCreatedOnPS"></span>
														</td>
													</tr>
													<tr>
														<td>Last Modified On : 
														</td>
														<td><span id="engagementModifiedOnPS"></span></td>
													</tr>
													</tbody>
												</table>
											</div>
										</div>									
									</div>
								</div>							
							</div>
							
							<div id="engagementDashBoard" class="tab-pane fade">
								<div class=" btn-group" data-toggle="buttons" style="width:100%">
									<label class="btn darkblue active" data-name="engagementRAGSummary" onclick="showEngamentGroupDashboardSummaryTableTool(1);">
									<input type="radio" class="toggle" >RAG Summary</label>
									<label class="btn darkblue" data-name="engagementWorkflowSummary" onclick="showEngamentGroupDashboardSummaryTableTool(2);">
									<input type="radio" class="toggle" >Workflow Summary</label>
								</div> 
								<h4 class="modal-title" id="engagementDashboardRAGHeaderSubTitle" style="padding-top: 10px;"></h4>
								<h4 class="modal-title" id="engagementDashboardWorkflowHeaderSubTitle" style="padding-top: 5px;"></h4>  
								
								<div id="engagementRAGSummary">				
									<!-- <div class="scroller" style="height: 100%; padding: 0px;" data-always-visible="1" data-rail-visible1="1"> -->						
										<div class="container" style="height: 100%; padding: 0px;">																						
											<div class="row">
												<div class="col-lg-6 col-md-6" id="InProductSummary">
												</div>
												<div class="col-lg-6 col-md-6" id="InResourceTemplate">
												</div>	
											</div>
											
											<div class="dynamic_line"></div>
											
											<div class="row">
												<div class="col-lg-6 col-md-6" id="InActivitySummary">
												</div>
												<div class="col-lg-6 col-md-6">														
												</div>	
											</div>																							
										</div>						
									<!-- </div> -->     					
								</div>	
								<div id="engagementWorkflowSummary" style="display:none">				
									<div class="scroller" style="height: 100%; padding: 0px;" data-always-visible="1" data-rail-visible1="1">						
										<div class="container" style="height: 100%; padding: 0px;">								
											<div class="row">
												<div class="col-lg-6 col-md-6" id="InWorkflowInstanceTypeSummary">
												</div>
												<div class="col-lg-6 col-md-6" id="InWorkflowInstanceStatusSummary">
												</div>	
											</div>
											
											<div class="dynamic_line"></div>
											
											<div class="row">
												<div class="col-lg-6 col-md-6" id="InWorkflowInstanceUserSummary">
												</div>
												<div class="col-lg-6 col-md-6" id="InWorkflowInstanceCategorySummary">
												</div>	
											</div>
										</div>						
									</div>     					
								</div>
							</div>
							
							<div id="activityWorkpackageTab" class="tab-pane fade"></div>
							<div id="activityTab" class="tab-pane fade"></div>
						</div>	
	
						<div id="productActivityWPSummaryView" style="display:none;"></div>						
						<div id="activityWPSummaryView" style="display:none;"></div>	
						<div id="activitySummaryView" style="display:none;"></div>	
						
						<div class="col-md-2" id="filterIsActive" style="margin-top: 0px;display:none;">
							<div id="status_dd">
								<select class="form-control input-small select2me" id="isActive_ul">
									<option value="2" ><a href="#">ALL</a></option>
									<option value="1" selected><a href="#">Active</a></option>
									<option value="0" ><a href="#">Inactive</a></option>
								</select>
							</div>
						</div>
						<div id="listingActivitiesDIV" >
							<table id="activitiesWP_dataTable"  class="cell-border compact row-border" cellspacing="0" width="100%">
								<thead></thead>       
				       			<tfoot><tr></tr></tfoot>				       						
							</table>
							
							<div id="paginationContainerDT" class="a">																					
								<div id="activityPaginationContainerDT" style="display: -webkit-box; margin-top: 10px;">
									<u style="color: #4db3a4; padding-right: 10px;"><span class="caption-subject theme-font bold uppercase">Server Side Pagination :</span></u>	 
									<div class="dataTables_info" id="paginationBadgeDTActivity" role="status" aria-live="polite">Showing 1 to 10 of 10 entries</div>					
									<div style="padding-right:0px; padding-left: 18px;">
										<label>Show 
										<select id="selectActivityCountDT" onchange="setDropDownActivityDT(this.value)" style="vertical-align: middle;">
											<option value="10" selected="selected">10</option>
											<option value="50">50</option>
											<option value="100">100</option>
											<option value="1000">1000</option>
											<option value="5000">5000</option>
										</select>entries</label>
									</div>
									<div style="width: 35%;">
										<div id="paginationDTActivity" class="pagination toppagination" style="text-align: right;width: 100%;margin: 0px;"></div>
									</div>															
								</div>											
							</div>
							
						</div>
						<div id="listingDIV">
						<!-- For Task Starts -->
						<div class="jScrollContainerResponsive jScrollContainerFullScreen" style="clear: both; padding-top: 10px;position:relative;">							
							<div class="col-md-8 jScrollTitleContainer jTableContainerFullScreen" id="divUploadActivitiesTask" style="display:none; position: absolute; z-index: 10;
						 		margin-left: 180px;padding-right: 0px;">
						 	<div class="col-md-8" id="divUploadActivityTaskContainer">
							 	<div class="col-md-2" style="padding-right: 10px;padding-top: 8px;margin-top: 2px;margin-right: 20px;width: 95px;display:none">
									<label style="color:  #FFF;font-family: sans-serif;font-size: 1.1em;">Upload : </label>
								</div>
							 	<div class="col-md-5" style="padding-left: 0px;margin-top: 0px;margin-left: -17px; display:none">													
          			  					<input type="button" class="btn blue" id="trigUploadActivityTask" value="Activity Task" style="background-color: #55616f; font-size: 0.96em;font-family: sans-serif;margin-top: 8px;height: 26px;padding-top: 5px;">
          			  					<span id="fileNameActivityTask"></span>
									<input id="uploadFileActivityTask" type="file" name="uploadFileActivityTask" style="display:none;" 
									onclick="{this.value = null;};" onchange="importActivityTask()">
									
									<img src="css/images/dt_download_features.png" title="Download Template - Activity Task" class="icon_imgSize showHandCursor" 
									alt="Download" onclick="downloadTemplateActivityTask();" style="margin-top: 11px;margin-left: 5px;">
								</div>
							</div>		
								
							<div class="col-md-2" id="filterIsActiveTask" style="margin-top: 0px;float:right;">		
							  <div id="status_dd">
								<select class="form-control input-small select2me" id="isActiveTask_ul">
									<option value="2" selected ><a href="#">ALL</a></option>
									<option value="1" ><a href="#">Active</a></option>
									<option value="0" ><a href="#">Inactive</a></option>
								</select>
					          </div>
					         </div>
					         					       
			            	</div> 
							<div id="jTableContainerActivityTaskWP" class ="jTableContainerFullScreen"></div>
						</div>
						<!-- For Task Starts -->
						
						<div class="row list-separated" style="margin-top: 0px;">
						<div id="bulkselectionActGrouping" class="toolbarFullScreen" style="display:none">
								<fieldset class="scheduler-border">
									<legend class="scheduler-border theme-font">Bulk Allocation</legend>
									<div class="control-group">
									
							    <div id="filter" class="fliter-align" >
						  			<!-- Adding filter -->									
									<div class='col-md-12'>
										<div class='col-md-2'>       										
										  <label class="bulkAllocationTitle">End Date</label>										
										</div>										
										<div class='col-md-2'>       											
											<label class="bulkAllocationTitle">Category</label>											
										</div>
										<div class='col-md-2'>       											
											<label class="bulkAllocationTitle">Assignee</label>											
										</div>
										<div class='col-md-2'>      											
											<label class="bulkAllocationTitle">Reviewer</label>										
										</div>		
										<div class='col-md-2'>      											
											<label class="bulkAllocationTitle">Priority</label>										
										</div>									
												
								   </div>

							    <div class='col-md-12'>								  						
										<div class='col-md-2'> 												
											<input class="form-control input-small  date-picker" id="bulkalloc_datepickerenddate" size="10" type="text" value="" />
										</div>	
										<div class='col-md-2'>       
											<div id="bulkalloc_category_dd" class="">												
												<select class="form-control input-small  select2me" id="bulkalloc_category_ul">
								        			<option class="categoryList_ids" value="-">-</option> 			   			
												</select>
											</div>
										</div>										
										<div class='col-md-2'>       
											<div id="bulkalloc_assignee_dd" class="">												
												<select class="form-control input-small  select2me" id="bulkalloc_assignee_ul">
								        			<option class="assigneeList_ids" value="-">-</option> 			   			
												</select>
											</div>
										</div>
											<div class='col-md-2'>       
											<div id="bulkalloc_reviewer_dd" class="">												
												<select class="form-control input-small  select2me" id="bulkalloc_reviewer_ul">
								        			<option class="reviewerList_ids" value="-">-</option> 			   			
												</select>
											</div>
										</div>
								
										<div class='col-md-2'>     
											<div id="bulkalloc_executionPriorityList_dd" class="">
												<select class="form-control input-small  select2me" id="bulkalloc_executionPriorityList_ul">
								        			<option class="executionPriorityList_ids" value="-">-</option>  			   			
												</select>
											</div>
										</div>
										<div class="col-md-2">        
									      <button type="button" id="save" class="btn green-haze" onclick="SaveActivityDetailsGrouping()" style="padding: 6px 7px;font-size: 12px;">Save</button>                                  		     
									     <button type="reset" id="reset" class="btn green-haze" onclick="ResetActivityDetailsGrouping()" style="padding: 6px 7px;font-size: 12px;">Reset</button> 
										</div>						
									 </div>		
								   </div>
								</div>
								</fieldset>								   
							</div>
						
							<div style="clear: both; padding-top: 10px;position:relative;"> 
								<div class="col-md-8 id="divUploadActivitiesList" style="position: absolute; z-index: 10;
							 		margin-left: 140px;padding-right: 0px;width: 680px;">											 												 		
									<div class="col-md-8" id="divUploadActivityContainerList">
										<div class="col-md-2" style="padding-right: 10px;padding-top: 8px;margin-top: 2px;margin-right: 20px;width: 95px;">
											<label style="color:  #FFF;font-family: sans-serif;font-size: 1.1em;">Upload : </label>
										</div>										
										<div class="col-md-5" style="padding-left: 0px;margin-top: 0px;margin-left: -17px;">													
	           			  					<input type="button" class="btn blue" id="trigUploadActivities" value="Activities" style="background-color: #55616f; 
	           			  							font-size: 0.96em;font-family: sans-serif;margin-top: 8px;height: 26px;padding-top: 5px;">
	           			  					<span id="fileNameActivities"></span>
											<input id="uploadFileActivities" type="file" name="uploadFileActivities" style="display:none;" 
											onclick="{this.value = null;};" onchange="importActivityWorkpackage()">
											<img src="css/images/dt_download_features.png" title="Download Template - Activities" class="icon_imgSize showHandCursor" 
											alt="Download" onclick="downloadTemplateActivity();" style="margin-top: 11px;margin-left: 5px;">
										</div>
																				
									 </div>
									 <div class="col-md-3" id="filterIsActiveGroup" style="margin-top: 0px;float: right;">
											<div id="status_dd">
											<select class="form-control input-small select2me" id="isActiveGroup_ul">
												<option value="2" selected><a href="#">ALL</a></option>
												<option value="1" ><a href="#">Active</a></option>
												<option value="0" ><a href="#">Inactive</a></option>
											</select>
										</div>
    								</div>	
								</div>								
		     				 	<div id="jTableContainerActivityWP" style="padding-left: 15px;"></div>
							</div>
							
						  </div>
						</div>
						<div id="hidden"></div>
					</div>
				</div>
				<!-- END PORTLET-->
			</div>				
		</div>
		<!-- END PAGE CONTENT INNER -->
	</div>
</div>
<!-- END PAGE CONTENT -->

<div><%@include file="singleJtableContainer.jsp" %></div> 
<div><%@include file="workflowSummarySingleDT.jsp" %></div>
</div>
<!-- END PAGE-CONTAINER -->

<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp" %></div>			
<!-- END FOOTER -->
<!-- Popup -->
<div id="div_PopupMain" class="modal " tabindex="-1" aria-hidden="true" >
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupClose()" aria-hidden="true"></button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:320px" data-always-visible="1" data-rail-visible1="1">
					<div class="jScrollContainerResponsive" style="clear:both;padding-top:10px">
						<div id="jtableSelectEnv"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>	

<div id="div_PopupBackground"></div>
<!-- Popup Ends -->	

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
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datetimepicker.min.js" type="text/javascript" ></script>
<script src="js/moment.min.js" type="text/javascript"></script>
<script src="js/daterangepicker.js" type="text/javascript"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>
<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/viewScript/instanceWorkflowConfiguration.js" type="text/javascript"></script>
<script src="js/viewScript/instanceWorkflowSummary.js" type="text/javascript"></script>

<script src="js/datatable/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="js/datatable/dataTables.jqueryui.min.js" type="text/javascript"></script>
<script src="js/pagination/jquery.paging.min.js" type="text/javascript"></script>

<script src="js/activitySearch.js" type="text/javascript"></script>
<script src="js/viewScript/activityListingGroupingDT.js" type="text/javascript"></script>
<script src="js/viewScript/engageMentLevelActivityGrouping.js" type="text/javascript"></script>


<% ResourceBundle rb = ResourceBundle.getBundle("TAFServer");
    String autoAllocate=rb.getString("ACTIVITY_AUTOALLOCATE");  	
	String changeReqeustTitleAWP = rb.getString("CHANGEREQUEST_TABLE_TITLE");	
%>

<script type="text/javascript">
var autoAllocateFromProperty = "<%=autoAllocate%>";
var useCaseAWP = "<%=changeReqeustTitleAWP%>";
var defaultuserId = '${userId}';
var activityInvisibleColumnValue= ${activityInvisibleColumnIndex};

var isFirstLoad = true;
var testFactId=0;
var engagementId=0;
var engagementName="";
var productId=0;
var prodId = 0;
var productVersionId=0;
var productBuildId=0;	
var activityWPId = 0;
var activityWPName = "";
var productName="";
var defaultActivityWorkpackagePlannedStartDate = new Date();
var defaultActivityWorkpackagePlannedEndDate = new Date();
/* Task code Starts*/
var activityAssigneeId = '';
var activityReviewerId = '';
var allowPlanDateEndDate = true;
var defaultTaskPlannedStartDate = new Date();
var defaultTaskPlannedEndDate = new Date();
var defaultTaskLifeCycleStage = 0;
var workPackageId = 0;
var loginUserId;



	
if('${roleName}' == "Tester"){	
	allowPlanDateEndDate = false;		
}
		
/* Task code Ends */
function showWorkPackageTreeData(){	
	var jsonObj={"Title":"",				
	    	 	"urlToGetTree": 'administration.activity.workpackage.grouping.tree?type=1',
	    	 	"urlToGetChildData": 'administration.activity.workpackage.grouping.tree?type=1',	    	 	
	 };	 
	 TreeLazyLoading.init(jsonObj);	 
}


var nodeType=0;		
jQuery(document).ready(function() {	
	$('#divUploadActivityContainerList').hide();
	$('#filterIsActiveGroup').hide();	
	$('#jTableContainerActivityTaskWP').hide();
	
   QuickSidebar.init(); // init quick sidebar 
   setBreadCrumb("Activity Management");
   ComponentsPickers.init();
   createHiddenFieldsForTree();
	setPageTitle("Engagement");
	//getTreeData('administration.product.hierarchy.tree');
	showWorkPackageTreeData();	 
	$('#activitySummaryView').hide();
	setLoginUserId();
	
	
var productActivityWPselectedTab=0;

$("#treeContainerDiv").on("select_node.jstree",
function(evt, data){
	if(data.node != undefined){
		if(data.node.icon=='fa fa-close' || data.node.icon=='fa fa-lock'){
			editableFlag=false;
		}else{
			editableFlag=true;
		}
		var entityIdAndType =  data.node.data;			 		
		var arry = entityIdAndType.split("~");
		key = arry[0];
		var type = arry[1];
				 		
		title = data.node.text;
		var loMainSelected = data;
		uiGetParents(loMainSelected);
		var date = new Date();
		var timestamp = date.getTime();
		nodeType = type;
		
		var activityId;
		var activityWorkPackageId=0;
		var activityAssigneeId = defaultuserId;
		
		defaultTaskPlannedStartDate = new Date();
		defaultTaskPlannedEndDate = new Date();
		defaultTaskLifeCycleStage = 0;
		
		var taskId = 0;
		var taskTypeId = 0;
		
		if(data.node.original.activityAssigneeId!="undefined" && data.node.original.activityAssigneeId!=null && data.node.original.activityAssigneeId!= ""){
			activityAssigneeId = data.node.original.activityAssigneeId;
		}
		var activityReviewerId = defaultuserId;
		if(data.node.original.activityReviewerId!="undefined" && data.node.original.activityReviewerId!=null && data.node.original.activityReviewerId!= ""){
			activityReviewerId = data.node.original.activityReviewerId;
		}
		//var productId = defaultuserId;
		if(data.node.original.productId!="undefined" && data.node.original.productId!=null && data.node.original.productId!= ""){
			productId = data.node.original.productId;
			taskProductId = data.node.original.productId;
		}
		if(data.node.original.activityWorkpackageId!="undefined" && data.node.original.activityWorkpackageId!=null && data.node.original.activityWorkpackageId!= ""){
			activityWorkpackageIdForWF = data.node.original.activityWorkpackageId;
			activityWorkPackageId = data.node.original.activityWorkpackageId;
		}
		if(data.node.original.activityTypeId != "undefined" && data.node.original.activityTypeId != null && data.node.original.activityTypeId != ""){
			activityTypeId = data.node.original.activityTypeId;
		}else{
			activityTypeId = 0;
		}
		if(data.node.original.taskId != "undefined" && data.node.original.taskId != null && data.node.original.taskId != ""){
			taskId = data.node.original.taskId;
		}else{
			taskId = 0;
		}
		
		if(data.node.original.taskTypeId != "undefined" && data.node.original.taskTypeId != null && data.node.original.taskTypeId != ""){
			taskTypeId = data.node.original.taskTypeId;
		}else{
			taskTypeId = 0;
		}
		
		activityAssigneeId = defaultuserId;
		if(data.node.original.activityAssigneeId!="undefined" && data.node.original.activityAssigneeId!=null 
				&& data.node.original.activityAssigneeId!= "")
		{
			activityAssigneeId = data.node.original.activityAssigneeId;
		}	


		activityReviewerId = defaultuserId;
		if(data.node.original.activityReviewerId!="undefined" && data.node.original.activityReviewerId!=null && data.node.original.activityReviewerId!= "")
		{
			activityReviewerId = data.node.original.activityReviewerId;
		}		
		$('#activityWPSummaryView').hide();
		$('#activitySummaryView').hide();
		$('#listingDIV').hide();
		
		$('#engagementOverview').hide();
		$('#engagementDashBoard').hide();
		
		nodeType = nodeType.toLowerCase();
		
		$("#activityEngagementToggle").hide();		
		$("#listingActivitiesDIV").hide();
			
		if(nodeType == "TestFactory".toLowerCase())//Listing TestFactory Level WP
		{			
			testFactId = key;
			engagementId=key;
			engagementName=title;
			
			$("#activityEngagementToggle").show();			
			var indexValue = $("#tablistEngagementACTWP>li.active").index();
			showActivityAtEngagementLevel(indexValue);
			
			$('#productActivityWPSummaryView').hide();			
			$('#activityWPSummaryView').hide();
			$('#activitySummaryView').hide();
			$('#jTableContainerActivityTaskWP').hide();
			$('#bulkselectionActGrouping').hide();	
			
			//urlToGetWorkRequestsOfSpecifiedBuildId = 'activityWorkPackage.testFactory.product.level?testFactoryId='+testFactId+'&isActive=1&productId=-1';
			//listActivityWorkPackagesOfSelectedTestFactory(urlToGetWorkRequestsOfSpecifiedBuildId);
			return false;					    
		}
		else if(nodeType == "Product".toLowerCase())//Each Product AWP Summary View
		{			
			$('#activityWPSummaryView').hide();
			$('#activitySummaryView').hide();			
			$('#listingDIV').hide();		
			$('#bulkselectionActGrouping').hide();
			
			$('#breadCrumHeader').text('PRODUCT');
			$('#productActivityWPSummaryView').show();			
			$("#ProductACTWPSummaryPage ul li a").css('paddingLeft','10px');
			
			prodId = key;
			productId=key;
			document.getElementById("hdnproductId").value=key;
			productName = title;
			
			//ProdutAWP Component
			productActivityWPVeiwComponent(productId, productName);			
			return false;
		}
		else if(nodeType == "ActivityWorkPackageIcon".toLowerCase())//Listing Product Level WP
		{	
			$('#breadCrumHeader').text('PRODUCT LEVEL WORKPACKAGES');
			$('#productActivityWPSummaryView').hide();
			$('#activityWPSummaryView').hide();
			$('#activitySummaryView').hide();
			$('#divUploadActivityContainerList').hide();
			$('#filterIsActiveGroup').hide();
			$('#bulkselectionActGrouping').hide();
			
			$('#listingDIV').show();
			$('#jTableContainerActivityWP').show();
			$('#jTableContainerActivityTaskWP').hide();
			
			prodId = key;
			productId=prodId;
			document.getElementById("hdnproductId").value=key;
			
			var jsonObj={
				"Title": 'ActivityWorkpackage',			          
				"SubTitle": 'ActivityWorkpackage at Workpackage level',
				"listURL": 'activityWorkPackage.testFactory.product.level?testFactoryId=-1&isActive=1&productId='+prodId+'&jtStartIndex=0&jtPageSize=10000',
				"creatURL": 'process.workrequest.add',
				"updateURL": 'process.workrequest.update',		
				"containerID": 'jTableContainerActivityWP',
				"productId": prodId,
				"componentUsage": "ActivityWorkpackageLevel",	
			};	 
			ActivityWorkpackage.init(jsonObj);			
			
			//urlToGetWorkRequestsOfSpecifiedBuildId = 'activityWorkPackage.testFactory.product.level?testFactoryId=-1&isActive=1&productId='+prodId;
			//listActivityWorkPackagesOfSelectedTestFactory(urlToGetWorkRequestsOfSpecifiedBuildId);					    	
			return false;
		}
		else if(nodeType == "ActivityWorkPackage".toLowerCase())//Each WP Summary View
		{
			$('#breadCrumHeader').text('ACTIVITY WORKPACKAGE');
			$('#productActivityWPSummaryView').hide();			
			$('#activitySummaryView').hide();
			$('#listingDIV').hide();
			$('#bulkselectionActGrouping').hide();
			
			$('#activityWPSummaryView').show();
			var productArry =data.node.parent.split('~');
			productId=productArry[1];
			prodId=productArry[1];
			activityWPId = key;
			document.getElementById("treeHdnCurrentActivityWorkPackageId").value = activityWPId;
			activityWPName = title;
			workPackageId = activityWPId;	
			engagementId = 	data.node.original.testFactId;
			if(typeof data.node.original.plannedStartDate != "undefined" && data.node.original.plannedStartDate != null && data.node.original.plannedStartDate != ""){
				defaultActivityWorkpackagePlannedStartDate = data.node.original.plannedStartDate;
			}
			if(typeof data.node.original.plannedEndDate != "undefined" && data.node.original.plannedEndDate != null && data.node.original.plannedEndDate != ""){
				defaultActivityWorkpackagePlannedEndDate = data.node.original.plannedEndDate;
			}
			if(typeof data.node.original.testFactId != "undefined" && data.node.original.testFactId != null && data.node.original.testFactId != ""){
				testFactId = data.node.original.testFactId;
			}	
			//AWP Component
			activityWPVeiwComponent(activityWPId, activityWPName);
			//listActivityWorkflowPlan(productId, 34, 0, activityWPId, "jTableContainerforWorkflowPlanWorkpackage");
		}
		else if(nodeType == "ActivityIcon".toLowerCase())//Listing WP Level Activities
		{		
			$('#breadCrumHeader').text('WORKPACKAGE LEVEL ACTIVITIES');
			$('#productActivityWPSummaryView').hide();
			$('#activityWPSummaryView').hide();
			$('#activitySummaryView').hide();
			
			$('#bulkselectionActGrouping').show();			
			
			$('#listingDIV').show();
			$('#filterIsActiveGroup').show();			
			$('#divUploadActivityContainerList').show();
			$('#jTableContainerActivityWP').show();
			$('#jTableContainerActivityTaskWP').hide();
						
			activityWPId = key;							
			productName = title;
			productVersionId = 0;
			productBuildId = 0;
			activityWorkPackageId = 0;
			enableAddOrNot = "yes";
			var isActive = $("#isActiveGroup_ul").find('option:selected').val();
			if(typeof data.node.original.plannedStartDate != "undefined" && data.node.original.plannedStartDate != null && data.node.original.plannedStartDate != ""){
				defaultActivityWorkpackagePlannedStartDate = setDateInFormat(data.node.original.plannedStartDate);
			}
			if(typeof data.node.original.plannedEndDate != "undefined" && data.node.original.plannedEndDate != null && data.node.original.plannedEndDate != ""){
				defaultActivityWorkpackagePlannedEndDate = setDateInFormat(data.node.original.plannedEndDate);
			}
			//urlToGetActivitiesOfSpecifiedActivityWPId = 'process.list.activity.type?activityWorkPackageId='+activityWPId;
			//mamtha			
			if(typeof data.node.original.testFactId != "undefined" && data.node.original.testFactId != null && data.node.original.testFactId != ""){
			testFactId = data.node.original.testFactId;
			}			
			ActivitySearchInitloadList();
			ActivityInitloadListGrouping();
			listActivitiesOfSelectedAWP(0,0,0,activityWPId,isActive,enableAddOrNot, '#jTableContainerActivityWP');
			$("#bulkalloc_datepickerenddate").on("changeDate",function() {
				$(".datepicker").hide();
			});						    	
			return false;	
		}
		else if(nodeType == "Activity".toLowerCase())//Each Activity's Summary View
		{
			$('#breadCrumHeader').text('ACTIVITIES');
			$('#productActivityWPSummaryView').hide();
			$('#activityWPSummaryView').hide();						
			$('#listingDIV').hide();
			$('#bulkselectionActGrouping').hide();
			
			$('#activitySummaryView').show();
			$("#ActivitySummary").html(activityTabSummaryHandler());
			
			//$("#bulkselectionnew").show();
			$("#reviewFilter").show();												
			activityId = key;
			
			if(prodId == 0)
			{			
				fetchProductIdOfActivity(activityId);
				prodId = document.getElementById("hdnproductId").value;
			}
			if(typeof data.node.original.plannedStartDate != "undefined" && data.node.original.plannedStartDate != null && data.node.original.plannedStartDate != ""){
					defaultTaskPlannedStartDate = data.node.original.plannedStartDate;
			}
			if(typeof data.node.original.plannedEndDate != "undefined" && data.node.original.plannedEndDate != null && data.node.original.plannedEndDate != ""){
					defaultTaskPlannedEndDate = data.node.original.plannedEndDate;
			}
			if(typeof data.node.original.lifeCycleStage != "undefined" && data.node.original.lifeCycleStage != null && data.node.original.lifeCycleStage != ""){
					defaultTaskLifeCycleStage = data.node.original.lifeCycleStage;
			}
			if(typeof data.node.original.testFactId != "undefined" && data.node.original.testFactId != null && data.node.original.testFactId != ""){
				testFactId = data.node.original.testFactId;
			}			
			
			activitiesVeiwComponent(activityId, activityTypeId, prodId, productId, testFactId, title);	
        	return false;	
        	
		}
		else if(nodeType == "ActivityTaskIcon".toLowerCase())//Listing Activity Level Tasks
		{				
			$('#breadCrumHeader').text('ACTIVITY LEVEL TASKS');
			$('#productActivityWPSummaryView').hide();
			$('#activityWPSummaryView').hide();
			$('#activitySummaryView').hide();
			$('#bulkselectionActGrouping').hide();
			$('#filterIsActiveGroup').hide();
			$('#listingDIV').show();
			
			ActivitySearchInitloadList();
			enableAddOrNot = "yes";
				if(document.getElementById("treeHdnCurrentProductId").value != "")
					productId=document.getElementById("treeHdnCurrentProductId").value;
				if(document.getElementById("treeHdnCurrentProductName").value != "")
					productName = document.getElementById("treeHdnCurrentProductName").value;
				if(document.getElementById("treeHdnCurrentProductVersionId").value != "")
					productVersionId=document.getElementById("treeHdnCurrentProductVersionId").value;
				if(document.getElementById("treeHdnCurrentProductBuildId").value != ""){
					productBuildId=document.getElementById("treeHdnCurrentProductBuildId").value;
				}
				else{
					productBuildId=0;
				}
			//activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
			// activityWorkPackageId =	activityWPId;
			
			activityId = key;
			if(typeof data.node.original.plannedStartDate != "undefined" && data.node.original.plannedStartDate != null && data.node.original.plannedStartDate != ""){
				defaultTaskPlannedStartDate = setDateInFormat(data.node.original.plannedStartDate);
			}
			if(typeof data.node.original.plannedEndDate != "undefined" && data.node.original.plannedEndDate != null && data.node.original.plannedEndDate != ""){
				defaultTaskPlannedEndDate = setDateInFormat(data.node.original.plannedEndDate);
			}
			if(typeof data.node.original.lifeCycleStage != "undefined" && data.node.original.lifeCycleStage != null && data.node.original.lifeCycleStage != ""){
				defaultTaskLifeCycleStage = data.node.original.lifeCycleStage;
			}
			document.getElementById("treeHdnCurrentActivityId").value=activityId;
			var isActive = $("#isActiveTask_ul").find('option:selected').val();		
			listActivitiesOfSelectedActivity(productId,productVersionId,productBuildId,activityWorkPackageId,activityId,isActive,enableAddOrNot,activityAssigneeId,activityReviewerId);
			statusOption('common.list.activity.tasktype?productId='+productId,null);
			
			$("#filterIsActiveTask").show();
			$('#divUploadActivitiesTask').show();
			$('#jTableContainerActivityWP').hide();
			$("#jTableContainerActivityTaskWP").show();
			
		}else if(nodeType == "ActivityTask".toLowerCase())//Each Task's Summary View
		{
			$('#breadCrumHeader').text('TASKS');					
			$('#productActivityWPSummaryView').hide();
			$('#activityWPSummaryView').hide();
			$('#activitySummaryView').hide();			
			$('#listingDIV').hide();			
			//$("#tablistActivities").hide();
			$('#bulkselectionActGrouping').hide();
		//listActivityWorkflowPlan(productId, 30, taskTypeId, taskId, "jTableContainerforWorkflowPlanActivityOrTask");
						
		}else
		{
			  	prodId = document.getElementById("treeHdnCurrentProductId").value;
			   	productBuildId = document.getElementById("treeHdnCurrentProductBuildId").value;
		}
	};	
});
	 $(document).on('change','#isActive_ul', function() {
		 var isActive = $("#isActive_ul").find('option:selected').val();	
		 productId=document.getElementById("treeHdnCurrentProductId").value;
		 productName=document.getElementById("treeHdnCurrentProductName").value;
		 productVersionId=document.getElementById("treeHdnCurrentProductVersionId").value;
		 productBuildId=document.getElementById("treeHdnCurrentProductBuildId").value;		
		 urlToGetWorkRequestsOfSpecifiedBuildId = 'process.workRequest.list?productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&isActive='+isActive;
		 nodeType = nodeType.toLowerCase();
		 if(nodeType == "ProductBuild".toLowerCase()){
				enableAddOrNot = "yes";
			}
		 listWorkRequestsOfSelectedProductBuild(enableAddOrNot);
		 }); 
	 
	 	var elementDivId = "#treeContainerDiv";
		 $(elementDivId).on("loaded.jstree", function(evt, data){
			 setDefaultnode();		
	     });
	 
	  $(document).on('change','#isActiveGroup_ul', function() {
		  var isActive = $("#isActiveGroup_ul").find('option:selected').val();
		  listActivitiesOfSelectedAWP(0,0,0,activityWPId,isActive,enableAddOrNot, '#jTableContainerActivityWP');  
	  });
	 
	  $(document).on('click', "#menuList .dropdown-quick-sidebar-toggler .dropdown-toggle" ,function(){
			resizeDataTablesofProducts();	
		});
	  
	  function resizeDataTablesofProducts(){			
			setTimeout(function(){							
				if(activityManagementTab_oTable != ''){
					reInitializeDTActivityTab();
				}
			},600);
		}
	  
	 
});

function activityWPVeiwComponent(activityWorkPackageId, activityWPName){
	var jsonObj=
	{
		"Title":"ActivitWorkPackage Details :- "+ activityWorkPackageId + "- "+activityWPName,
	    "activityWorkPackageId":activityWorkPackageId,
		"activityWPName":activityWPName ,				 
		"AWPDetailsTitle":"Activity WorkPackage Details",
		"activityWPURL":"activity.workpackage.summary.list?activityWorkPackageId="+activityWorkPackageId,
	};
	ActivityWorkPackageViewDetails.init(jsonObj);	
}	

function productActivityWPVeiwComponent(productId, productName){
	var jsonObj={
		"Title":"Product Details :- "+ productId + "- "+productName,
	    "productId":productId,
		"productName":productName ,				 
		"AWPDetailsTitle":"Product Activity WorkPackage Details",
		"productActivityWPURL":"product.awp.summary?productId="+productId,
	};
	ProductActivityWorkPackageViewDetails.init(jsonObj);	
}

function activitiesVeiwComponent(activityId, activityTypeId, prodId, productId, testFactId, activityName){
	if(activityWPId == null || activityWPId == undefined || activityWPId == '' ){
		activityWPId=0;
	}
	
	var jsonObj=
	{
		"Title":"Activities Details :- "+ activityId + "- ",
	    "activityId":activityId,
	    "activityTypeId":activityTypeId,
	    "productId":productId,
		"testFactId":testFactId ,				 
		"ActiviesDetailsTitle":"Activity Details",
		"activityName" : activityName,
		"activityWorkPackageId" : activityWPId, 
		//"activityWPURL":"activity.workpackage.summary.list?activityWorkPackageId="+activityWorkPackageId,
	};
	ActiviesViewDetails.init(jsonObj);	
}

var productIdhidden = 0;	
function fetchProductIdOfActivity(actId){
	var url = 'get.productId.of.activity?activityId='+actId;			
	$.ajax({
		type: "POST",
	    dataType : 'json',
		url : url,
		success: function(data) {
			var prodIdData=eval(data);
			productIdhidden = prodIdData[0].productId;
			if(productIdhidden!=null){
				document.getElementById("hdnproductId").value  = productIdhidden;
			}						  	
		}
		});
}

function setDefaultnode() {		
	
	defaultNodeId = $('#treeContainerDiv li').eq(0);
	$.jstree.reference('#treeContainerDiv').deselect_all();
	$.jstree.reference('#treeContainerDiv').close_all();
	$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
	$.jstree.reference('#treeContainerDiv').trigger("select_node");
	
}

function validateNodeLength(nodeArray){
	var flag = false;			
	if(nodeArray.children && nodeArray.children.length>0){
		flag=true;
	}
	return flag;
}

function setDateInFormat(date){	
	if(date == "" || date == null || typeof date == "undefined") return "";
	
	var dateArr = date.split('/');
	var dateNew = "";
	if(dateArr.length > 0)
		 dateNew = dateArr[0] + "/" + dateArr[1] + "/" + dateArr[2];	
	return dateNew;
}
	
</script>

<!-- Div For AWP Starts -->
<div id="popUpAvailableDr" class="modal " tabindex="-1" aria-hidden="true" >
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:320px" data-always-visible="1" data-rail-visible1="1">
					<div id="listAllDr"></div>					
					<div class="row" style="margin-top:5px;">
						<div class="radio-toolbar col-lg-6">								
						</div>
					</div> 					
				</div>
			</div>
		</div>
	</div>
</div>
<!-- Div For AWP Ends -->

<!-- Activity Child Popups Details child Popup start-->
	<div id="activityChildContainer" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Change Request</h4>
					</div>
					<div class="modal-body">
						<div id="activityTabLoaderIcon" style="display:none;z-index:100001;position:absolute;top:0%;left:44%">
							<img src="css/images/ajax-loader.gif"/>
						</div>					
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id=activityTabContainer></div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
	
	<!-- Popup -->
<div id="activityTabSummaryContainer" class="modal " tabindex="-1" aria-hidden="true" style="width: 96%; top: 2%; left: 2%;padding-top: 0px; padding-bottom: 0px; z-index: 100051;">
	<div class="modal-full">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true" onclick="updateActivityDTOnClose();"></button>
				<h4 class="modal-title"></h4>
				<div id="activitySummaryLoaderIcon" style="display:none; z-index:100001; position:absolute; top:2%; left:50%">
					<img src="css/images/ajax-loader.gif"/>
				</div>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:490px" data-always-visible="1" data-rail-visible1="1">
					<div id="activitySummaryContainer" style="display:none">
						<div><jsp:include page="assignedActivityGrouping.jsp"></jsp:include></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- activity Creation popup -->
<div id="activityCreationContainer" class="modal " tabindex="-1" aria-hidden="true" style="width: 96%; top: 2%; left: 2%;padding-top: 0px; padding-bottom: 0px;">
	<div class="modal-full">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title">Create a new Activity</h4>
			    <div id="activityCreationLoaderIcon" style="display:none; z-index:100001; position:absolute; top:2%; left:50%">
					<img src="css/images/ajax-loader.gif"/>
				</div>	
			</div>
			<div class="modal-body">
			    <div id="activityCreationLoaderIcon" style="display:none; z-index:100001; position:absolute; top:2%; left:50%">
					<img src="css/images/ajax-loader.gif"/>
				</div>	
				
				<!-- <div class="scroller" style="height:490px" data-always-visible="1" data-rail-visible1="1"> -->
				<div id="activityContainer"></div>					
				<!-- </div> -->
			</div>
		</div>
	</div>
</div>	

<!---- poke notifications ----->

<div id="div_PopupPokeNotifications" class="modal " tabindex="-1" aria-hidden="true">
	<div class="modal-dialog" style="width: 500px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title caption-subject theme-font">Notification</h4>
			</div>
			<div class="modal-body">
				<!-- <div class="scroller" style="height:275px" data-always-visible="1" data-rail-visible1="1"> -->
					<!-- <h5 class="modal-title" style="color: #44b6ae;">Notification Type</h5> -->
				    <!-- <div class="col-md-12"> -->
						<div class="portlet light">								
							<div class="portlet-body form">
							  <div class="skin skin-flat">
									<form role="form">
										<div class="form-body" style="padding-top: 0px;padding-bottom: 0px;">									
											<div class="form-group">													
												<div class="input-group">
												<div id="notifyPoke" class="icheck-list">														
												  </div>	 
												</div>
											</div>
											<div class="form-group">													
												<div class="input-group">
												<input id="pokeActivityID" type="hidden"></input>
												To: <input type="text" name="ToList" id="pokeToList" value="@Assignee;@Reviewer" style="margin-bottom:10px;margin-left: 41px;width: 350px;"></input><br>
												Cc: <input type="text" name="CcList" id="pokeCCList" style="margin-bottom:10px;margin-left: 42px;width:350px;"><br>
												Subject: <input type="text" name="pokeSubject" id="pokeSubjId" style="margin-bottom:5px;margin-left: 8px;width: 350px;"><br>												
												<div style="display: -webkit-box;"><label for="title" style="position: absolute;bottom: 80px;">Message:</label>
												<textarea id="pokeInput" style="width: 349px;height:100px;margin-left: 66px;"></textarea></div>	 
												</div>
											</div>
										</div>
										<div class="form-actions" style="padding-top: 5px;padding-bottom: 5px;float: right;margin-top: -12px;">
										<button type="button" onclick="submitRadioPokeNotificationHandler();" class="btn green-haze" style="padding: 6px 6px;" >Submit</button>
											<button type="button" class="btn grey-cascade" onclick="popupClosePokeNotificationHandler()">Cancel</button>
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
 
 
 
 <div id="div_PopupActivityRelationShip" class="modal " tabindex="-1" aria-hidden="true">
	<div class="modal-dialog" style="width: 400px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title caption-subject theme-font">Activity RelationShip</h4>
			</div>
			<div class="modal-body">
						<div class="portlet light">								
							<div class="portlet-body form">
							  <div class="skin skin-flat">
									<form role="form">
										<div style="padding: 5px 25px;">
											<table width="200" border="1" class="table">   				 	
							   				 	<thead>
								   				 	<tr>
														 <th>OLD ACTIVITIES</th>
														 <th>NEW ACTIVITIES</th>
								 					</tr>
							 					</thead>   				 	
							   				 	<tbody>
								   				 	<tr>
												 	 	<td id = "entityInstanceId1ActivityMapping"><a href="#" onclick = "displayTabActivityHandlerEntityInstanceId1()"></a></td>
								        				<td id = "entityInstanceId2ActivityMapping"><a href="#" onclick = "displayTabActivityHandlerEntityInstanceId2()"></a></td>       						   
								    				</tr>
							    				</tbody>
							    			</table>		
										</div>
									</form>
								</div>  
							</div>
						</div>
				</div>
		</div>
	</div>
</div>
 
 

<script type="text/javascript" src="js/Scripts/jqmeter/jqmeter.min.js"></script>
<script type="text/javascript" src="js/Scripts/jqmeter/jquery_simple_progressbar.js"></script>

<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
<div><%@include file="dragDropListItems.jsp"%></div> 

<script src="js/viewScript/WorkflowIndicatorDetailView.js" type="text/javascript"></script>
<script src="js/viewScript/activityCreation.js" type="text/javascript"></script>

<div> <%@include file="productActivityWorkPackageViewComponent.jsp"%> </div>
<div> <%@include file ="activityWorkPackageViewComponent.jsp" %></div>

<div><%@include file="addComments.jsp"%></div>
<div><%@include file="comments.jsp"%></div>
<div><%@include file="cloning.jsp"%></div>
<div><%@include file="singleDataTableContainer.jsp"%></div>

<script src="js/viewScript/activityGroupingManagement.js" type="text/javascript"></script>
<script src="js/viewScript/attachmentsMethAWPACT.js" type="text/javascript"></script>
<script src="js/viewScript/auditHistoryListingContainer.js" type="text/javascript"></script>
<script src="js/viewScript/activityChangeRequest.js" type="text/javascript"></script>
<script src="js/viewScript/activityClarification.js" type="text/javascript"></script>
<script src="js/viewScript/activityWorkpackage.js" type="text/javascript"></script>
<script src="js/viewScript/activityWorkflow.js" type="text/javascript"></script>

<!-- For Activities Starts -->
<script src="js/activitydrscript.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/Scripts/popup/iosOverlay.js"></script>
<script src="js/pageSpecific/download.js" type="text/javascript"></script>

<div><jsp:include page="customFields.jsp"></jsp:include></div>
<div> <%@include file ="myActivitySummaryGrouping.jsp" %></div>
<div> <%@include file ="activityChart.jsp" %></div>

<!-- For Activities Ends -->
<script src="js/importData.js" type="text/javascript"></script>

<script>

//--- for the dataTable fullscreen ----

function fullScreenHandlerDTActivities(){
	
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
		
		$('#activityEngagementToggle .toolbarFullScreen').css('display','block');		
		$('#ProductACTWPSummaryPage .toolbarFullScreen').css('display','block');
		$('#ACTWPSummaryPage .toolbarFullScreen').css('display','block');
		
		activitiesDTFullScreenHandler(true);
		
	}else{		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
		
		$('#activityEngagementToggle .toolbarFullScreen').css('display','none');		
		$('#ProductACTWPSummaryPage .toolbarFullScreen').css('display','none');
		$('#ACTWPSummaryPage .toolbarFullScreen').css('display','none');
		
		activitiesDTFullScreenHandler(false);								
	}
}

function setLoginUserId(){
	 loginUserId = '<%=session.getAttribute("ssnHdnUserId")%>';
}

//----

</script>

<!-- END JAVASCRIPTS -->
<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
</body>
<!-- END BODY -->
</html>