<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="com.hcl.atf.taf.controller.HomePageController"%>
<%@page import="com.hcl.atf.taf.model.UserList"%>
<%@page import="java.util.List"%>
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
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css"
	type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link
	href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235"
	rel="stylesheet" type="text/css" />
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet"
	type="text/css"></link>
	
	<link rel="stylesheet" href="css/iosOverlay.css" type="text/css" media="all">
<link href="css/customStyle.css" rel="stylesheet" type="text/css">

<link href="js/datatable/Editor-1.5.6/css/dataTables.editor.css" rel="stylesheet" type="text/css"></link>
<link href="js/datatable/Editor-1.5.6/css/editor.dataTables.css" rel="stylesheet" type="text/css"></link>
<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>

<!-- END THEME STYLES -->

<style type="text/css">
#filterIsActive > #status_dd > .select2me{
	height: 26px;
    padding-top: 1px;
    margin-top: 5px;
    width: 90px !important;
    font-size: 13px;
 }
  #filterIsActive > #status_dd >.select2me > a{
 	height: 28px !important; 
     padding-top: 0px; 
     margin-top: -1px; 
 }
  
#status_dd > .form-control .select2-choice {
 	height: 28px; 
 }
</style>

</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body>
	<!-- BEGIN HEADER -->
	<div class="page-header">
		<!-- BEGIN HEADER TOP -->
		<div><%@include file="headerlayout.jsp"%></div>
		<!-- END HEADER TOP -->
		<!-- BEGIN HEADER MENU -->
		<div class="page-header-menu">
			<div class="container container-position">
				<!-- BEGIN MEGA MENU -->
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
		<div><%@include file="dynamicJtableContainer.jsp"%></div>
		<div><%@include file="singleDataTableContainer.jsp"%></div>
	

		<!-- BEGIN PAGE CONTENT -->
		<div class="page-content">
			<div class="container-fluid">
				<!-- BEGIN PAGE CONTENT INNER -->
				<div id="toAnimate" class="row margin-top-10">
					<div class="col-md-12">
						<!-- BEGIN PORTLET-->
						<div class="portlet light">
							<div class="portlet-title">
								<div class="caption caption-md">
									<i class="icon-bar-chart theme-font hide"></i> <span
										class="caption-subject theme-font bold uppercase">ACTIVITY</span>&nbsp;<span
										id="headerTitle" class="caption-subject theme-font"></span> <span
										class="caption-helper hide">weekly stats...</span>
								</div>
							</div>
							<div class="portlet-body" style="padding-top: 0px;display:none;">
								<div class="row list-separated" style="margin-top: 0px;">							     
								<div id="bulkselection" class="toolbarFullScreen" style="display:none">
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
											<input class="form-control input-small  date-picker" id="datepickerenddate" size="10" type="text" value="" />
										</div>	
										<div class='col-md-2'>       
											<div id="category_dd" class="">												
												<select class="form-control input-small  select2me" id="category_ul">
								        			<option class="categoryList_ids" value="-">-</option> 			   			
												</select>
											</div>
										</div>										
										<div class='col-md-2'>       
											<div id="assignee_dd" class="">												
												<select class="form-control input-small  select2me" id="assignee_ul">
								        			<option class="assigneeList_ids" value="-">-</option> 			   			
												</select>
											</div>
										</div>
											<div class='col-md-2'>       
											<div id="reviewer_dd" class="">												
												<select class="form-control input-small  select2me" id="reviewer_ul">
								        			<option class="reviewerList_ids" value="-">-</option> 			   			
												</select>
											</div>
										</div>
								
										<div class='col-md-2'>     
											<div id="executionPriorityList_dd" class="">
												<select class="form-control input-small  select2me" id="executionPriorityList_ul">
								        			<option class="executionPriorityList_ids" value="-">-</option>  			   			
												</select>
											</div>
										</div>
										<div class="col-md-2">        
									      <button type="button" id="save" class="btn green-haze" onclick="SaveActivityDetail()" style="padding: 6px 7px;font-size: 12px;">Save</button>                                  		     
									     <button type="reset" id="reset" class="btn green-haze" onclick="ResetActivityDetail()" style="padding: 6px 7px;font-size: 12px;">Reset</button> 
										</div>						
									 </div>		
								   </div>
								</div>
								</fieldset>								   
								   	 </div> 
								<div id="activitySearchBox" class="toolbarFullScreen" style="display:none">
								<fieldset class="scheduler-border" style="display:none">
									<legend class="scheduler-border theme-font" style="width: 44px;">Search</legend>
									<div class="control-group">
									
							    <div id="filter" class="fliter-align" >
						  			<!-- Adding filter -->									
									<div class='col-md-12'>		
										<div class='col-md-2'>       											
											<label class="bulkAllocationTitle">Assignee</label>
										</div>
										<div class='col-md-2'>       											
											<label class="bulkAllocationTitle">Status</label>
										</div>											
										<div class='col-md-2'>      											
											<label class="bulkAllocationTitle">Priority</label>										
										</div>									
												
								   </div>

							    <div class='col-md-12'>							  						
									<div class='col-md-2'>       
											<div id="searchAssignee_dd" class="">												
												<select class="form-control input-small  select2me" id="assigneeSearch_ul">
								        			<option class="searchassigneeList_ids" value="-">-</option> 			   			
												</select>
											</div>
										</div>
											<div class='col-md-2'>       
											<div id="searchStatus_dd" class="">												
												<select class="form-control input-small  select2me" id="statusSearch_ul">
								        			<option class="searchstatusList_ids" value="-">-</option> 			   			
												</select>
											</div>
										</div>
								
										<div class='col-md-2'>     
											<div id="searchPriorityList_dd" class="">
												<select class="form-control input-small  select2me" id="prioritySearch_ul">
								        			<option class="searchpriorityList_ids" value="-">-</option>  			   			
												</select>
											</div>
										</div>
										<div class="col-md-2">        
									      <button type="button" id="search" class="btn green-haze" onclick="SearchDetail()" style="padding: 6px 7px;font-size: 12px;">Search</button>                                  		     
									     <button type="reset" id="reset" class="btn green-haze" onclick="ResetSearchDetail()" style="padding: 6px 7px;font-size: 12px;">Reset</button> 
										</div>						
									 </div>		
								   </div>
								</div>
								</fieldset>								   
								   	 </div> 
									<!-- jtable started -->
									<!-- Main -->
									<div id="main"style="float: left; padding-top: 0px; width: 100%;">
										<div id="hdnDiv"></div>
										<div id="hidden"></div>
										<!-- Box -->
										<div class="box">
											<!-- ContainerBox -->
											<div class="cl">&nbsp;</div>
											<div class="jScrollContainerResponsive jScrollContainerFullScreen" style="clear: both; padding-top: 10px;position:relative;max-height: 300px;">
											 	<div class="col-md-8 jScrollTitleContainer" id="divUploadActivities" style="position: absolute; z-index: 10;
											 		margin-left: 180px;padding-right: 0px;">											 												 		
													<div class="col-md-8" id="divUploadActivityContainer1">
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
													 <div class="col-md-2" id="filterIsActive" style="margin-top: 0px;float: right;">
															<div id="status_dd">
															<select class="form-control input-small select2me" id="isActive_ul">
																<option value="2" ><a href="#">ALL</a></option>
																<option value="1" selected><a href="#">Active</a></option>
																<option value="0" ><a href="#">Inactive</a></option>
															</select>
														</div>
				    								</div>    					

												</div>
												<div id="jTableContainer" class ="jTableContainerFullScreen"></div>
											</div>
											<!-- End Container Box -->

											<div class="cl">&nbsp;</div>
										</div>
										<!-- End Box -->
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
		
			<div><%@include file="singleJtableContainer.jsp" %></div> 
	</div>
	<!-- END PAGE-CONTAINER -->

	<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp"%></div>
	<!-- END FOOTER -->
	<!-- Popup -->
	<div id="div_PopupMain" class="modal " tabindex="-1" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						title="Press Esc to close" onclick="popupClose()"
						aria-hidden="true"></button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body">
					<div class="scroller" style="height: 320px" data-always-visible="1"
						data-rail-visible1="1">
						<div class="jScrollContainerResponsive"
							style="clear: both; padding-top: 10px">
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
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script
		src="files/lib/metronic/theme/assets/admin/layout/scripts/quick-sidebar.js"
		type="text/javascript"></script>
	<script
		src="files/lib/metronic/theme/assets/global/plugins/jstree/dist/jstree.min.js"
		type="text/javascript"></script>
	<!-- END PAGE LEVEL SCRIPTS -->

	<!-- the jScrollPane script -->
	<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

	<!-- Include jTable script file. -->
	<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>

	<!-- Validation engine script file and english localization -->
	<script src="js/select2.min.js" type="text/javascript"></script>
	<script src="js/bootstrap-select.min.js" type="text/javascript"></script>	
	<script src="js/bootstrap-datetimepicker.min.js" type="text/javascript" ></script>
	<script src="js/moment.min.js" type="text/javascript"></script>
	<script src="js/daterangepicker.js" type="text/javascript"></script>
	<script src="js/components-pickers.js" type="text/javascript"></script>

	<!-- the jScrollPane script -->
	<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

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
		
		<script type="text/javascript" src="js/draganddrop/Sortable.js"></script>
	<script type="text/javascript" src="js/draganddrop/ng-sortable.js"></script>
	<script src="js/pagination/jquery.paging.min.js" type="text/javascript"></script>
	
	<script src="js/viewScript/instanceWorkflowConfiguration.js" type="text/javascript"></script>
	<script src="js/viewScript/instanceWorkflowSummary.js" type="text/javascript"></script>
	
	<div><%@include file="addComments.jsp"%></div>
	<%-- <div><%@include file="dynamicJtableContainer.jsp"%></div>
	<div><%@include file="singleJtableContainer.jsp"%></div> --%>

	<script type="text/javascript">
		var defaultuserId = '${userId}';
		var isFirstLoad = true;
		var productId;
		var productName;
		var activityTypeId = 28;
		var changeRequestTypeId = 42;
		var defaultActivityPlannedStartDate = new Date();
		var defaultActivityPlannedEndDate = new Date();
		var defaultActivityLifeCycleStage = 0;
		
		
		var allowPlanDateEndDate = true;
		if('${roleName}' == "Tester"){	
		allowPlanDateEndDate = false;		
	}
		
		jQuery(document).ready(function() {
			QuickSidebar.init(); // init quick sidebar
			setBreadCrumb("Activity Management");
			createHiddenFieldsForTree();
			setPageTitle("Products");							
			console.log("Before")
			getTreeData('administration.product.activity.workpackage.tree');
			console.log("After")
			
			var closeChildTableFlag=false;
			
		    var productVersionId;
			var productBuildId;							
			var activityWorkPackageId;
			var nodeType;
			statusOption('common.list.activity.tasktype?productId=',null);
			setDefaultnode();
			
			$("#treeContainerDiv")
				.on("select_node.jstree",
						function(evt, data) {
							if(data.node != undefined){
							var entityIdAndType = data.node.data;
							var arry = entityIdAndType.split("~");
							var key = arry[0];
							var type = arry[1];
							var title = data.node.text;
					        var date = new Date();
							var loMainSelected = data;
					        nodeType = type;
							uiGetParents(loMainSelected);
							var enableAddOrNot = "no";
							
							defaultActivityPlannedStartDate = new Date();
							defaultActivityPlannedEndDate = new Date();
							defaultActivityLifeCycleStage = 0;
							
							if(nodeType == "Product"){
						 		productId=key;
						 		productName = title;
						 		productVersionId = 0;
								productBuildId = 0;
								activityWorkPackageId = 0;
								var isActive = $("#isActive_ul").find('option:selected').val();	
								listActivitiesOfSelectedAWP(productId,productVersionId,productBuildId,activityWorkPackageId,isActive,enableAddOrNot);
								$("#bulkselection").show();
								//$("#filterIsActive").show();
								$("#divUploadActivityContainer").show();
								
						 	}
							if(nodeType == "ProductVersion"){
						 		productId=document.getElementById("treeHdnCurrentProductId").value;
						 		productName = document.getElementById("treeHdnCurrentProductName").value;
						 		productVersionId = key;
								productBuildId = 0;
								activityWorkPackageId = 0;
								var isActive = $("#isActive_ul").find('option:selected').val();	
								//listActivitiesOfSelectedAWP(0,productVersionId,productBuildId,activityWorkPackageId,isActive,enableAddOrNot);
								listActivitiesOfSelectedAWP(productId,productVersionId,productBuildId,activityWorkPackageId,isActive,enableAddOrNot);
								$("#bulkselection").show();
								//$("#filterIsActive").show();
								$("#divUploadActivityContainer").show();
						 	}
							 if(nodeType == "ProductBuild"){
								 	productId=document.getElementById("treeHdnCurrentProductId").value;
								 	productName = document.getElementById("treeHdnCurrentProductName").value;
									productVersionId=document.getElementById("treeHdnCurrentProductVersionId").value;
									productBuildId = key;
									activityWorkPackageId = 0;
									var isActive = $("#isActive_ul").find('option:selected').val();	
									//listActivitiesOfSelectedAWP(0,0,productBuildId,activityWorkPackageId,isActive,enableAddOrNot);
									listActivitiesOfSelectedAWP(productId,productVersionId,productBuildId,activityWorkPackageId,isActive,enableAddOrNot);
									$("#bulkselection").show();
									//$("#filterIsActive").show();
									$("#divUploadActivityContainer").show();
							 }

							if (nodeType == "ActivityWorkPackage") {	
							//	ActivitySearchInitloadList();
								if(typeof data.node.original.plannedStartDate != "undefined" && data.node.original.plannedStartDate != null && data.node.original.plannedStartDate != ""){
									defaultActivityPlannedStartDate = data.node.original.plannedStartDate;
								}
								if(typeof data.node.original.plannedEndDate != "undefined" && data.node.original.plannedEndDate != null && data.node.original.plannedEndDate != ""){
									defaultActivityPlannedEndDate = data.node.original.plannedEndDate;
								}
								if(typeof data.node.original.lifeCycleStage != "undefined" && data.node.original.lifeCycleStage != null && data.node.original.lifeCycleStage != ""){
									defaultActivityLifeCycleStage = data.node.original.lifeCycleStage;
								}
								enableAddOrNot = "yes";
								productId=document.getElementById("treeHdnCurrentProductId").value;
								productName = document.getElementById("treeHdnCurrentProductName").value;
								productVersionId=document.getElementById("treeHdnCurrentProductVersionId").value;
								productBuildId=document.getElementById("treeHdnCurrentProductBuildId").value;
								activityWorkPackageId = key;
								document.getElementById("treeHdnCurrentActivityWorkPackageId").value = activityWorkPackageId;
								var isActive = $("#isActive_ul").find('option:selected').val();														
								//listActivitiesOfSelectedAWP(0,0,0,activityWorkPackageId,isActive,enableAddOrNot);
								listActivitiesOfSelectedAWP(productId,productVersionId,productBuildId,activityWorkPackageId,isActive,enableAddOrNot);
								
								statusOption('common.list.activity.tasktype?productId='+productId,null);
								
								
								$("#bulkselection").show();
								//$("#filterIsActive").show();
								$("#divUploadActivityContainer").show();
								//$("#activitySearchBox").show();
								
							}
						ActivitySearchInitloadList();
						ActivityInitloadList();
						
						//$("#datepickerenddate").datepicker('setDate','today');
						}
							
						$('.portlet-body').show();
					});
						
					$("#datepickerenddate").on("changeDate",function() {
						$(".datepicker").hide();
					});

				$(document).on('change','#isActive_ul', function() {
					 var isActive = $("#isActive_ul").find('option:selected').val();
					 productId=document.getElementById("treeHdnCurrentProductId").value;	
					 productName = document.getElementById("treeHdnCurrentProductName").value;
					 productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value;
					 productBuildId = document.getElementById("treeHdnCurrentProductBuildId").value;
					 activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
					 if(nodeType == "ActivityWorkPackage"){
							enableAddOrNot = "yes";
					 }
					 listActivitiesOfSelectedAWP(0,0,0,activityWorkPackageId,isActive,enableAddOrNot);
			 	});				
				
		});
		
		function setDefaultnode() {			
			var nodeFlag = false;			
			if(isFirstLoad) {
				$("#treeContainerDiv").on("loaded.jstree",function(evt, data) {
					$.each($('#treeContainerDiv li'), function(ind, ele){
						if($.jstree.reference('#treeContainerDiv').is_parent($(ele).attr("id"))){
							defaultNodeId = $(ele).attr("id");							
							isFirstLoad = false;
							return false;
						}
					});	
					setDefaultnode();
				});
			} else {
				
				defaultNodeId = $.jstree.reference('#treeContainerDiv').get_node(defaultNodeId).children[0];
				nodeFlag = validateNodeLength($.jstree.reference('#treeContainerDiv').get_node(defaultNodeId))
				if(nodeFlag){
					setDefaultnode();					
				}else{			
					$.jstree.reference('#treeContainerDiv').deselect_all();
					$.jstree.reference('#treeContainerDiv').close_all();
					$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
					$.jstree.reference('#treeContainerDiv').trigger("select_node");
				}
			}
		}
		
		function validateNodeLength(nodeArray){
			var flag = false;			
			if(nodeArray.children && nodeArray.children.length>0){
				flag=true;
			}
			return flag;
		}

		/* Pop Up close function */
		function popupClose() {
			$("#div_PopupMain").fadeOut("normal");
			$("#div_PopupBackground").fadeOut("normal");
		}
		
		function SaveActivityDetail(){	
			activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;			
			productId=document.getElementById("treeHdnCurrentProductId").value;
			productName = document.getElementById("treeHdnCurrentProductName").value;
		    var activitywpListsFromUI = [];
			var $selectedFeatureRows = '';	
		    $selectedFeatureRows = $('#jTableContainer').jtable('selectedRows');
				if($selectedFeatureRows.length == 0){
					callAlert("Please select activity for bulk allocation");
				}

			$selectedFeatureRows.each(function () {
				    var record = $(this).data('record');
				    var wptcepId = record.activityId;
				    activitywpListsFromUI.push(wptcepId);
				});
				
				 var plannedEndDate=datepickerenddate.value;
				 if(plannedEndDate=='Planned End Date')
					 plannedEndDate='';		 
				 var category=$("#category_dd .select2-chosen").text();
				 var assignee=$("#assignee_dd .select2-chosen").text();
				 var reviewer=$("#reviewer_dd .select2-chosen").text();
				 var priority=  $("#executionPriorityList_dd .select2-chosen").text();		 
				 
				var categoryId ="";
				if((category!="-")&&(category!="")){
					categoryId = $("#category_ul").find('option:selected').attr('id');
				}else{
					categoryId=-1;
				}		
				
				 var assigneeId="";
				 if((assignee!="-")&&(assignee!="")){
					 assigneeId= $("#assignee_ul").find('option:selected').attr('id');
				 }else{
					 assigneeId=-1;
				 }
				
				var reviewerId="";
				if((reviewer!="-")&&(reviewer!="")){
					reviewerId = $("#reviewer_ul").find('option:selected').attr('id');
				}else{
					reviewerId=-1;
				}
				
				var priorityId ="";
				if((priority!="-")&&(priority!="")){
					priorityId = $("#executionPriorityList_ul").find('option:selected').attr('id');
				}else{
					priorityId=-1;
				}	
		       
				if(activityWorkPackageId==null || activityWorkPackageId <=0 || activityWorkPackageId == 'null'){
						callAlert("Please select the activityWorkPackage");
						return false;
					}else
			    if(activitywpListsFromUI.length == null || activitywpListsFromUI==''){
						callAlert("Select atleast one Activity","ok");
					}else if(plannedEndDate=='' && assigneeId==-1 && reviewerId==-1&&	priorityId==-1 &&	categoryId ==-1){
						callAlert("Select atleast one Option","ok");
					}
					
					else{			
					$.post('activitywp.activity.bulk.update?categoryId='+categoryId+'&assigneeId='+assigneeId+'&activitywpListsFromUI='+activitywpListsFromUI+'&plannedEndDate='+plannedEndDate+'&priorityId='+priorityId+'&reviewerId='+reviewerId,function(data){
						if(data.Result=="OK"){
						iosOverlay({
							text: "Saved", // String
							icon: "css/images/check.png", // String (file path)
							spinner: null,
							duration: 1500, // in ms
							});		
						
						productVersionId = 0;
						productBuildId = 0;
						activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
						productId=0;
						var date = new Date();						   
						var isActive = $("#isActive_ul").find('option:selected').val();					
					    listActivitiesOfSelectedAWP(productId,productVersionId,productBuildId,activityWorkPackageId,isActive,"yes");
						}
						else{
							iosOverlay({
								text: "Not Saved", // String
								icon: "css/images/cross.png", // String (file path)
								spinner: null,
								duration: 1500, // in ms
								});
						}
						});
					}
				
		}

		/* Load Poup function */
		function loadPopup(divId) {
			$("#" + divId).fadeIn(0500); // fadein popup div
			$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
			// IE7, IE8
			$("#div_PopupBackground").fadeIn(0001);
		}
		

var activityMasterOptionsArr= [];
var categoryOptionsArr= []; 
var resourcePoolOptionsArr= []; 
var priorityOptionsArr= []; 
var activityPrimaryOptionsArr= []; 

		function option(url,fieldName){
			if(fieldName==''){}
			 
            $.ajax({ //Not found in cache, get from server
                url: url,
                type: 'POST',
                dataType: 'json',
                async: false,
                success: function (data) {
                    if (data.Result != 'OK') {
                    //	callAlert(data.Message);
                        return;
                    }
					if(fieldName=='activityMasterUrl'){
					activityMasterOptionsArr = data.Options;
					}
					if(fieldName=='categoryUrl'){
					categoryOptionsArr = data.Options;     }
					if(fieldName=='resourcePoolUrl'){
					resourcePoolOptionsArr = data.Options;       }
					if(fieldName=='priorityUrl'){
					priorityOptionsArr = data.Options;     }
					if(fieldName=='activityPrimaryStatus'){
					activityPrimaryOptionsArr = data.Options;     }
                    
                }
            });
			//return optionsArr;
}


var statusOptionsArr= [];
var activityTaskOptionsArr= [];

function statusOption(url, value){
	 
    $.ajax({ //Not found in cache, get from server
        url: url,
        type: 'POST',
        dataType: 'json',
        async: false,
        success: function (data) {
            if (data.Result != 'OK') {
            	callAlert(data.Message);
                return;
            }
            if(value == "primaryStatus"){
            	statusOptionsArr = data.Options;            	
            }else{            
            	activityTaskOptionsArr = data.Options; 
            }
        },
        error: function (data){
        	console.log("error");
        }
    });
	//return optionsArr;
}


function listActivitiesOfSelectedAWP(productId,productVersionId,productBuildId,activityWorkPackageId,isActive,enableAddOrNot) {
	openLoaderIcon();					 
	var urlToGetActivitiesOfSpecifiedAWPId = 'process.activity.list?productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&activityWorkPackageId='+activityWorkPackageId+'&isActive='+isActive;		
	    		
	var activityMasterUrl='process.list.activity.type?activityWorkPackageId='+activityWorkPackageId; 
    var categoryUrl='common.list.executiontypemaster.byentityid?entitymasterid=1';
	var resourcePoolUrl='common.user.list.by.resourcepool.id.productId?productId='+productId;
	var priorityUrl='administration.executionPriorityList';
	var activityPrimaryStatus='activity.primary.status.master.option.list?productId='+productId;
	
	option(activityMasterUrl,'activityMasterUrl');
	option(categoryUrl,'categoryUrl');
	option(resourcePoolUrl,'resourcePoolUrl');
	option(priorityUrl,'priorityUrl');
	option(activityPrimaryStatus,'activityPrimaryStatus');
			
			try {
				if ($('#jTableContainer').length > 0) {
					$('#jTableContainer').jtable('destroy');
				}
			} catch (e) {
			}
			
			$('#jTableContainer')
				.jtable(
							{
								title: 'Activity List',
								paging : true, //Enable paging
								pageSize : 10,
								"scrollY":"50px",
								editinline : {
									enable : true
								},
								toolbarsearch:false,
								selecting : true, //Enable selecting
								multiselect : true, //Allow multiple selecting
								selectingCheckboxes : true, //Show checkboxes on first column
								recordUpdated:function(event, data){
									if(data.serverResponse.Message!='undefined' && data.serverResponse.Message!=""){
										callAlert("Warning: "+data.serverResponse.Message)
									}
								},
								recordAdded: function (event, data) {
									if(data.serverResponse.Message!='undefined' && data.serverResponse.Message!=""){
										callAlert("Warning: "+data.serverResponse.Message)
									}
								},
								recordsLoaded: function(event, data) {								
									closeLoaderIcon();
									$('.portlet > .portlet-title > .tools > .reload').trigger('click');							
		   
									if(enableAddOrNot == "no"){
										$('#jTableContainer .jtable-toolbar-item-add-record').hide();
										$('#divUploadActivityContainer').hide();
									 }			        	
								},
								/* toolbar : {
	                                items : [
	                                    {
										icon : '',
										tooltip: 'Workflow Summary',
										cssClass: 'fa fa-th showHandCursor',
										click : function() {
											showWorkflowEntityInstanceStatusSummary(productId, productName, 33, "Activities", 0, 0);
										}
									},{
										icon : '',
										tooltip: 'RAG Summary',
										cssClass: 'fa fa-th showHandCursor',
										click : function() {
											var engagementId=0;
											showWorkflowEntityInstanceRAGSummary(engagementId,productId,productVersionId,productBuildId,activityWorkPackageId,productName, 33, "Activities", 0, 0);
										}
									}]
								}, */
						        actions : {
									listAction : urlToGetActivitiesOfSpecifiedAWPId,
									createAction : 'process.activity.add',
									editinlineAction : 'process.activity.update',
									deleteAction : 'process.activity.delete',
								},								 
								fields : {								
									activityWorkPackageId : {
							 				type: 'hidden', 
							 				defaultValue: activityWorkPackageId 
									},
								
									activityId : {
										key : true,
										list : false,
										create : false
									},
									activityName:{
						                 title: 'Activity Name',
						                 inputTitle: 'Activity Name <font color="#efd125" size="4px">*</font>',
						            	 list:true,
						            	 edit:true,
						            	 create:true,
						                 width:"20%"
						             },
									activityMasterId : {
										title : 'Activity Type',
										inputTitle: 'Activity Type <font color="#efd125" size="4px">*</font>',
										list : true,
										edit : true,
										create : true,
										width : "25%",
										//options:function(data){
										//return 'process.list.activity.type?&activityWorkPackageId='+activityWorkPackageId;
							     		//}
										options:function () {
				                    		return  activityMasterOptionsArr; //Cache results and return options
										}
									},
									productFeatureId : {
										title : 'Requirement',											
										list : true,
										create : true,
										edit : true,
								    	options:function(data){
								    		if(data.source =='list'){	      				
								    			return 'list.features.by.activity?activityId='+data.record.activityId+'&activityWorkPackageId='+activityWorkPackageId;
						      				}else if(data.source == 'create'){	      				
						      					return 'list.features.by.activity?activityId=-1&activityWorkPackageId='+activityWorkPackageId;
						      				}
						      			},
									},
									lifeCycleStageId : {
										title : 'Life Cycle Status',
										create : true,
										list : true,
										edit : true,
										width : "20%",
										dependsOn : 'activityWorkPackageId',
										defaultValue : defaultActivityLifeCycleStage,
										options:function(data){
										 	var entityTypeId = 34;
										 	var entityId = 0;
										 	var entityInstanceId = data.dependedValues.activityWorkPackageId;
										 	if(typeof entityInstanceId == 'undefined'){
										 		entityInstanceId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
										 	}
											return 'workflow.life.cycle.stages.options?entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId;
							     		},
									},
									plannedStartDate : {
										title : 'Planned Start Date',										
										inputTitle : 'Planned Start Date <font color="#efd125" size="4px">*</font>',
										edit : true,
										create : true,
										list : true,
										type : 'date',
										defaultValue : defaultActivityPlannedStartDate,
										width : "20%"
									},
									plannedEndDate : {
										title : 'Planned End Date',										
										inputTitle : 'Planned End Date <font color="#efd125" size="4px">*</font>',
										edit : true,
										list : true,
										create : true,
										type : 'date',
										defaultValue : defaultActivityPlannedEndDate,
										width : "20%"
									},
									workflowRAG : {
										create : false,
										edit : true,
										list : true,
									},
									actualStartDate : {
										title : 'Actual Start Date',
										create : false,
										edit : true,
										list : true,
										type : 'date',
										width : "20%"
									},
									actualEndDate : {
										title : 'Actual End Date',
										create : false,
										edit : true,
										list : true,
										type : 'date',
										width : "20%"
									},
									description:{
						                 title: 'Description',
						            	 list:false,
						            	 type: 'textarea',    
						            	 create:true,
						            	 edit:true,
						                 width:"20%"
						            },
						            statusDisplayName : {
										title : 'Current Status',
										create : false,
										list : true,
										edit : false,
										width : "20%",
										display: function(data){
											return data.record.statusDisplayName+"&nbsp;"+data.record.workflowStatusCategoryName;
										}
									},
									actors : {
										title : 'Status Pending With',
										create : false,
										list : true,
										edit : false,
										width : "20%"
									},	
									completedBy : {
										title : 'Status Complete By',
										create : false,
										list : true,
										edit : false,
										width : "20%"
									},
									remainingHrsMins : {
										title : 'Status Time Left',
										create : false,
										list : true,
										edit : false,
										width : "20%"
									},
									assigneeId : {
										title : 'Assignee',										
										list : true,
										create : true,
										edit : true,
										defaultValue : defaultuserId,
										//options : function(data) {
										//	return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
										//}
										options:function () {
                    						return  resourcePoolOptionsArr; //Cache results and return options
										}										
									},
									reviewerId : {
										title : 'Reviewer',
										list : true,
										create : true,
										edit : true,
										width : "20%",
										//options : function(data) {
											//return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
										//}
										options:function () {
                   							 return  resourcePoolOptionsArr; //Cache results and return options
										}
									},
									priorityId : {
										title : 'Priority',										
										list : true,
										create : true,
										edit : true,
										width : "20%",
										//options : 'administration.executionPriorityList'
										options:function () {
                    						return  priorityOptionsArr; //Cache results and return options
										}
									},
									complexity : {
										title : 'Complexity',									
										list : true,
										create : true,
										edit : true,
										width : "20%",
										options:function(){
										 	return 'common.list.complexity';
							     		},
									},
									activityWorkPackageName : {
										title : 'Work Package',
										list : true,
										edit : false,
										create : false
									},
									activityTrackerNumber : {
										title : 'Reference Code',
										list : true,
										edit : true,
										width : "20%"
									},
							    	drReferenceNumber : {
										title : 'Clarification',
										list : false,
										create : false,
										edit : false,
										width : "20%"
									}, 							
									categoryId : {
										title : 'Category',										
										list : true,
										create : true,
										edit : true,

										//options : function(data) {
											//return 'common.list.executiontypemaster.byentityid?entitymasterid=1';
										//}
										options:function () {
                    						return  categoryOptionsArr; //Cache results and return options
										}
									},
									/* statusId : {
										title : 'Status',
										create : false,
										list : false,
										edit : false,
										width : "20%",
										//options:function(data){
										//return 'activity.primary.status.master.option.list?productId='+productId;
							     		//}
										options:function () {
                    						return  activityPrimaryOptionsArr; //Cache results and return options
										}
									}, */
									/*
									// ----- modified -- not needed ---
									statusCategoryId : {
										title : 'Status',
										create:false,
										list : true,
										edit : true,
										defaultValue : 1,
										options:function(data){
										 	return 'status.category.option.list';
							     		}
									}, */
									remark : {
										title : 'Remarks',
										type: 'textarea',  
										list : true,
										width : "10%",
										width : "20%",
										list : false
									},
									plannedActivitySize : {
										title : 'Planned Size',										
										list : true,				    										
										create : true,
										edit : true,
										width : "20%"
										
									},
									actualActivitySize : {
										title : 'Actual Size',									
										list : true,				    										
										create : true,
										edit : true,
										width : "20%"
										
									},
									percentageCompletion : {
										title : '%Completion',
										create : false,
										list : true,
										edit : true,
										width : "4%"
									},
									workflowIndicator : {
										title : '',
										create : false,
										list : false,
										edit : false,
										width : "20%"
									},
									plannedEffort : {
	        				        	title : 'Planned Effort',
	        				        	create : true,
	        				        	edit : false,
	        				        	list : true,
	        				        	width : "20%",		        				        	
	        				        },
									totalEffort : {
	        				        	title : 'Actual Effort',
	        				        	create : false,
	        				        	edit : false,
	        				        	list : true,
	        				        	width : "20%",
	        				        	
	        				         },
	        				         isActive: { 
	                                   	title: 'Active',list:true,
	                       				edit:true,
	                    				create:false,
	                    				type : 'checkbox',
	                    				values: {'0' : 'No','1' : 'Yes'},
	                    		    	defaultValue: '1'
                    		    	},
	        				         Cloning:{
		             						title : '',
		             						list : true,
		             						create : false,
		             						width: "5%",
		             						display:function (data) { 
		             						 var $img = $('<img src="css/images/cloning.jpg" style="width: 24px;height: 24px;" title="Cloning Activity" data-toggle="modal" />'); 
		             						$img.click(function () {
		             							var jsonActivityCloningObj = {
		             									"title": "Cloning Activity",
		             									"packageName":"Activity Name",
		             									"startDate" : dateFormat(data.record.plannedStartDate),
		            									"endDate" : dateFormat(data.record.plannedEndDate),
		             									"selectionTerm" : "Select Activity Workpackage",
		             									"sourceParentID": data.record.activityWorkPackageId,
		             									"sourceParentName": data.record.activityWorkPackageName,
		             									"sourceID": data.record.activityId,
		             									"sourceName": data.record.activityName,
		             									"componentUsageTitle":"activityClone",
		             							}
		             							Cloning.init(jsonActivityCloningObj);							
		             						});
		             						return $img; 
		             					}
		             	             }, Moving:{
		             						title : '',
		             						list : true,
		             						create : false,
		             						width: "5%",
		             						display:function (data) { 
		             						 var $img = $('<img src="css/images/cloning.jpg" style="width: 24px;height: 24px;" title="Moving Activity" data-toggle="modal" />'); 
		             						$img.click(function () {
		             							var jsonActivityCloningObj = {
		             									"title": "Moving Activity",
		             									"packageName":"Activity Name",
		             									"startDate" : dateFormat(data.record.plannedStartDate),
		            									"endDate" : dateFormat(data.record.plannedEndDate),
		             									"selectionTerm" : "Select Activity Workpackage",
		             									"sourceParentID": data.record.activityWorkPackageId,
		             									"sourceParentName": data.record.activityWorkPackageName,
		             									"sourceID": data.record.activityId,
		             									"sourceName": data.record.activityName,
		             									"componentUsageTitle":"activityMove",
		             							}
		             							Cloning.init(jsonActivityCloningObj);							
		             						});
		             						return $img; 
		             					}
		             	             },
	        				         activityEffortTrackerId:{
		        				        	title : '',
		        				        	list : true,
		        				        	create : false,
		        				        	edit : false,
		        				        	width: "10%",
		        				        	display:function (data) { 
		        				           		//Create an image for test script popup 
		        							//	var $img = $('<i class="fa fa-clock-o" title="Edit status and Enter Effort spent for task"></i>');
		        				           		var $img = $('<i class="fa fa-history showHandCursor" title="Event History"></i>');  
		        			           			//Open Testscript popup  
		        			           			$img.click(function () {
		        			           				addActivityComments(data.record);
		        			           		  });
		        			           			return $img;
		        				        	}
		        				        },
	        				         statusAndPolicies:{
        					        	title : '',
        					        	list : true,
        					        	create : false,
        					        	edit : false,
        					        	width: "5%",
        					        	display:function (data) { 
        					           		//Create an image for test script popup 
        					        		var $img = $('<img src="css/images/workflow.png" title="Configure Workflow" />'); 
        				           			$img.click(function () {
        				           				workflowId = 0;
        				           				entityTypeId = 33;
        				           				statusPolicies(productId, workflowId, entityTypeId, data.record.activityMasterId, data.record.activityId, data.record.activityName, "Activity", data.record.statusId);
        				           		  });
        				           			return $img;
        					        	}
        					        },
        					        workflowId:{
        					        	title : 'WorkFlow Template',
        					        	list:false, 
						     	  		create:true,
						     	  		edit:false,
										dependsOn:'activityMasterId',
										options:function(data){							
											var entityId = data.dependedValues.activityMasterId;
											if(typeof entityId == 'undefined' || entityId == null){
												entityId = 0;
											}
											return 'workflow.master.mapped.to.entity.list.options?productId='+productId+'&entityTypeId=33&entityId='+entityId;
							     		}
        					        },
									changeRequest:{
					                	title: '',
					                	width: "5%",
					                	edit: true,
					                	create: false,
					                	display: function (activityData) { 
					                   		//Create an image that will be used to open child table 
					                   			var $img = $('<img src="css/images/list_metro.png" title="Change Request" />'); 
					                   			//Open child table when user clicks the image 
					                   			$img.click(function () {
					                   			
					                   				// ----- Closing child table on the same icon click -----
					                   			    closeChildTableFlag = closeJtableTableChildContainer($(this), $('#jTableContainer'));
					                   				if(closeChildTableFlag){
					                   					return;
					                   				} 		                   						
					                   				
					                   				$('#jTableContainer').jtable('openChildTable', 
					                   				$img.closest('tr'), 
					                   					{						                   	      	   
					                   						title: 'Add/Edit Change Request',							                   	      
					                   						editinline:{enable:true},
					                   						recordsLoaded: function(event, data) {
					                   		        		$(".jtable-edit-command-button").prop("disabled", true);
					                   		         	},
					                   					actions: { 
					                   						listAction: 'list.changerequests.by.entityTypeAndInstanceId?entityType1='+activityTypeId+'&entityInstance1='+activityData.record.activityId,
					                   						createAction : 'changerequests.add.by.entityTypeAndInstanceId?entityInstanceId='+activityData.record.activityId,
					    									/* createAction : 'process.activity.add?activityWorkPackageId='+activityWorkPackageId, */
					    									editinlineAction : 'list.change.requests.by.activity.update?activityId='+activityId,					    								
					                   						}, 
					                   						recordsLoaded: function(event, data) {
					                   							$('.jtable-toolbar-item-add-record').click(function(){
						                   							$("#Edit-raisedDate").datepicker('setDate','today');
			                   									});				                   												                   							
					                   						},
					                   					fields: { 
					                   						/*  activityId: { 
					                   							type: 'hidden', 
					                   							defaultValue: activityData.record.activityId 
					                   						},  */
					                   						changeRequestId: { 					                   							
					                   							title : 'ID',
					                   							key: true, 
					                       						create: false, 
					        					                edit: true, 
					                       						list: true
					                       					},
					                       					entityType1:{
																type: 'hidden',
																create: true, 
					        					                edit: true, 
					                       						list: true,
																defaultValue: activityTypeId,
															},
															entityType2:{
																type: 'hidden',
																create: true, 
					        					                edit: false, 
					                       						list: false,
																defaultValue: changeRequestTypeId,
															},
															entityInstance1:{
																type: 'hidden',
																create: true, 
					        					                edit: false, 
					                       						list: false,
																defaultValue: activityData.record.activityId,
															},
					                       					changeRequestName: { 					                   							
					                   							title : 'Name',
					                   							inputTitle : 'Change Request Name <font color="#efd125" size="4px">*</font>',
					                   							key: true, 
					                       						create: true, 
					        					                edit: true, 
					                       						list: true
					                       					},
					                       					description: {
						                       					 title: 'Description',
					    							             list:false,
					    							             type: 'textarea',    
					    							             create:true,
					    							             edit:true,
					    							             width:"20%"
					    							             },
					                       					priorityId : {
					    										title : 'Priority',					    										
					    										list : true,
					    										create : true,
					    										edit : true,
					    										width : "20%",
					    										//options : 'administration.executionPriorityList'
																options:function () {
												                    return  priorityOptionsArr; //Cache results and return options
																}
					    									},
															planExpectedValue: {
															title : 'Planned Value',										
															inputTitle : 'Planned Value <font color="#efd125" size="4px"></font>',
															edit : true,
															list : true,
															create : true,    							
															width : "20%",
															defaultValue:activityData.record.plannedActivitySize,
															},
					    									/* statusId : {
					    										title : 'Status',
					    										create : true,
					    										list : true,
					    										edit : false,
					    										width : "20%",
					    										//options:function(data){
					    										//	return 'activity.primary.status.master.option.list?productId='+productId;
					    								     	//	}
																options:function () {
												                    return  activityPrimaryOptionsArr; //Cache results and return options
																}
					    									}, */
					    									statusCategoryId : {
					    										title : 'Status',
					    										create:false,
					    										list : false,
					    										edit :false,
					    										defaultValue : 1,
					    										options:function(data){
					    										 	return 'status.category.option.list';
					    							     		}
					    									},
					    									ownerId : {
					    										title : 'Owner',					    										
					    										list : true,
					    										create : true,
					    										edit : true,
					    									//	options : function(data) {
					    									//		return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
					    									//	}
															options:function () {
											                    	return  resourcePoolOptionsArr; //Cache results and return options
																}
					    									},
					    									raisedDate : {
					    										title : 'Raised Date',
					    										inputTitle : 'Raised Date <font color="#efd125" size="4px">*</font>',
					    										edit : false,
					    										list : true,
					    										create : true,
					    										type : 'date',
					    										width : "20%",
					    										defaultValue : (new Date().getMonth()+1)+"/"+new Date().getDate()+"/"+new Date().getFullYear(),
					    									},attachment: { 
					    										title: '', 
					    										list: true,
					    										edit: false,
					    										create: false,
					    										width: "10%",
					    										display:function (data) {	        		
					    							           		//Create an image that will be used to open child table 
					    											var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>'); 
					    							       			$img.click(function () {          
					    							       				isViewAttachment = false;
					    							       				var jsonObj={"Title":"Attachments for ChangeRequest",			          
					    							       					"SubTitle": 'ChangeRequest : ['+data.record.changeRequestId+'] '+data.record.changeRequestName,
					    							    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=42&entityInstanceId='+data.record.changeRequestId,
					    							    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=42&entityInstanceId='+data.record.changeRequestId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
					    							    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
					    							    	    			"updateURL": 'update.attachment.for.entity.or.instance',
					    							    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=42',
					    							    	    			"multipleUpload":true,
					    							    	    			
					    							    	    		};	 
					    								        		Attachments.init(jsonObj);
					    							       		  });
					    							       			return $img;
					    							        	}				
					    									},					    									
					    									
															auditionHistory:{
																title : '',
																list : true,
																create : false,
																edit : false,
																width: "10%",
																display:function (data) { 
																	//Create an image for test script popup 
																	var $img = $('<i class="fa fa-search-plus" title="Audit History"></i>');
																	//Open Testscript popup  
																	$img.click(function () {
																		listGenericAuditHistory(data.record.changeRequestId,"ChangeRequest","changeRequestAudit");
																	});
																	return $img;
																}
															},				    									
					                   					},					                   	
					                   					formSubmitting: function (event, data) {
					  							      	    data.form.find('input[name="changeRequestName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[maxSize[100]');
					  							      	    data.form.find('input[name="raisedDate"]').addClass('validate[required]');
					  							            data.form.validationEngine();
					  							            return data.form.validationEngine('validate');
					  							          },   
								                         //Dispose validation logic when form is closed
								                         formClosed: function (event, data) {
								                            data.form.validationEngine('hide');
								                            data.form.validationEngine('detach');
								                        }
					                   				}, function (data) { //opened handler 
					                   						data.childTable.jtable('load'); 
					                   				}); 
					                 	  	}); 
					                   	//Return image 
					                   	return $img; 				                   	
					                	}
					                }, 
									RCN:{
							        	title : '',
							        	list : true,
							        	create : false,
							        	edit : false,
							        	width: "10%",
							        	display:function (data) { 
							        		//Create an image that will be used to open child table 
						           			var $img = $('<img src="css/images/mapping.png" title="Change Request Mapping" data-toggle="modal" />'); 
						           			//Open child table when user clicks the image 
						           			$img.click(function () {
												var activityId = data.record.activityId;
												var leftUrl="unmapped.changeRequest.count?activityId="+activityId;							
												var rightUrl = "";							
												var leftDefaultUrl="unmapped.changerequest.list?productId="+productId+"&activityId="+activityId+"&jtStartIndex=0&jtPageSize=10";							
												var rightDefaultUrl="changerequest.mapped.to.activity.list?activityId="+activityId;
												var leftDragUrl = "activity.changerequest.mapping?activityId="+activityId;
											    var rightDragtUrl = "activity.changerequest.mapping?activityId="+activityId;					
												var leftPaginationUrl = "unmapped.changerequest.list?productId="+productId+"&activityId="+activityId;
												var rightPaginationUrl="";						
												
												jsonActivityChangeRequestObj={"Title":"Map Change Request to Activity",
														"leftDragItemsHeaderName":"Available Change Request",
														"rightDragItemsHeaderName":"Mapped Change Request",
														"leftDragItemsTotalUrl":leftUrl,
														"rightDragItemsTotalUrl":rightUrl,
														"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
														"rightDragItemsDefaultLoadingUrl":rightDefaultUrl,
														"leftDragItemUrl":leftDragUrl,
														"rightDragItemUrl":rightDragtUrl,									
														"leftItemPaginationUrl":leftPaginationUrl,
														"rightItemPaginationUrl":rightPaginationUrl,									
														"leftDragItemsPageSize":"50",
														"rightDragItemsPageSize":"50",
														"noItems":"No Change Request Mapped",
														"componentUsageTitle":"Activity-RcnToActivity"
														};
												
												DragDropListItems.init(jsonActivityChangeRequestObj);							
												// DragDrop Testing ended----	           				
						           		  });
						           			return $img;
							        	}
							        }, 
									DR:{
					                	title: '',
				                	    width: "5%",
				                	    edit: true,
				                	    create: false,
				                	    list:false,
				                          	display: function (activityDrData) { 
				                   		//Create an image that will be used to open child table 
				                   			var $img = $('<img src="css/images/list_metro.png" title="Clarifications" />'); 
				                   			//Open child table when user clicks the image 
				                   			$img.click(function () {
				                   				
				                   				// ----- Closing child table on the same icon click -----
				                   			    closeChildTableFlag = closeJtableTableChildContainer($(this), $('#jTableContainer'));
				                   				if(closeChildTableFlag){
				                   					return;
				                   				} 
				                   			
				                   				$('#jTableContainer').jtable('openChildTable', 
				                   				$img.closest('tr'), 
				                   					{ 
				                   						title: 'Add/Edit Clarification', 
				                   					 	editinline:{enable:true},
				                   						recordsLoaded: function(event, data) {
				                   		        	 	$(".jtable-edit-command-button").prop("disabled", true);
				                   		         	},
				                   					actions: { 
				                   						listAction: 'list.clarificationtracker.by.entityTypeAndInstanceIds?entityTypeId=28&entityInstanceId='+activityDrData.record.activityId,
				                   						createAction : 'add.clarificationtracker.by.activity',		    									
				    									editinlineAction : 'update.clarificationtracker.by.activity',	
				                   						}, 
				                   					fields: { 
				                   						activityId: { 
				                   							type: 'hidden', 
				                   							defaultValue: activityDrData.record.activityId 
				                   						},  
				                   						clarificationTrackerId: { 
				                       						key: true, 
				                       						create: false, 
				        					                edit: false, 
				                       						list: false
				                       					},
				                       					clarificationTitle : {
				    										title : 'Title',
				    										inputTitle : 'Title <font color="#efd125" size="4px">*</font>',
				    										list : true,				    										
				    										create : true,
				    										edit : true,
				    										width : "10%"
				    										
				    									},
				    									clarificationDescription:{
				    							             title: 'Description',
				    							             list:false,
				    							             type: 'textarea',    
				    							             create:true,
				    							             edit:true,
				    							             width:"10%"
				    							             },
				    										
				                       					priorityId : {
				    										title : 'Priority',				    										
				    										list : true,
				    										create : true,
				    										edit : true,
				    										width : "10%",
				    										//options : 'administration.executionPriorityList'
															options:function () {
                    											return  priorityOptionsArr; //Cache results and return options
															}
				    									},
				    									/* statusId : {
				    										title : 'Status',
				    										create : true,
				    										list : true,
				    										edit : true,
				    										width : "10%",
				    										//options:function(data){
				    										//	return 'activity.primary.status.master.option.list?productId='+productId;
				    								     	//	}
															options:function () {
                    											return  activityPrimaryOptionsArr; //Cache results and return options
															}
				    									}, */
				    									statusCategoryId : {
				    										title : 'Status',
				    										create:true,
				    										list : true,
				    										edit : true,
				    										defaultValue : 1,
				    										options:function(data){
				    										 	return 'status.category.option.list';
				    							     		}
				    									},
				    									ownerId : {
				    										title : 'Owner',				    									
				    										list : true,
				    										width : "10%",
				    										create : true,
				    										edit : true,
				    										//options : function(data) {
				    										//	return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
				    										//}
															options:function () {
                    											return  resourcePoolOptionsArr; //Cache results and return options
															}
				    									},
				    									raisedDate : {
				    										title : 'Raised Date',
				    										inputTitle : 'Raised Date <font color="#efd125" size="4px">*</font>',
				    										edit : true,
				    										list : true,
				    										create : true,
				    										type : 'date',
				    										width : "10%"
				    									},   
				    									raisedById : {
				    										title : 'Raised By',
				    										inputTitle : 'Raised By <font color="#efd125" size="4px">*</font>',
				    										list : true,
				    										create : true,
				    										width : "10%",
				    										edit : true,
				    										defaultValue : defaultuserId,
				    										//options : function(data) {
				    										//	return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
				    										//}
															options:function () {
                    											return  resourcePoolOptionsArr; //Cache results and return options
															}
				    									}
				    								},
				                   	
				    								formSubmitting: function (event, data) {
					  							      	    data.form.find('input[name="clarificationTitle"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[maxSize[50]]');
					  							            data.form.find('input[name="raisedDate"]').addClass('validate[required]');					  							           
					  							            data.form.validationEngine();
					  							            return data.form.validationEngine('validate');
					  							          },   
							                         //Dispose validation logic when form is closed
							                         formClosed: function (event, data) {
							                            data.form.validationEngine('hide');
							                            data.form.validationEngine('detach');
							                        }
				                   				}, function (data) { //opened handler 
				                  					 	data.childTable.jtable('load'); 
				                  			 	}); 
				                 	  	}); 
				                   	//Return image 
				                   	return $img; 
				                   	} 
				                },
								environments:{
				                	title: '',
			                	    width: "5%",
			                	    edit: true,
			                	    create: false,
			                          	display: function (activityEnvData) { 
			                   		//Create an image that will be used to open child table 
			                   			var $img = $('<img src="css/images/list_metro.png" title="Environment Combinations" />'); 
			                   			//Open child table when user clicks the image 
			                   			$img.click(function () { 
			                   				
			                   				// ----- Closing child table on the same icon click -----
			                   			    closeChildTableFlag = closeJtableTableChildContainer($(this), $('#jTableContainer'));
			                   				if(closeChildTableFlag){
			                   					return;
			                   				} 
			                   				
			                   				$('#jTableContainer').jtable('openChildTable', 
			                   				$img.closest('tr'), 
			                   					{ 
			                   					title: 'Environment Combination', 
			                   					 editinline:{enable:true},
			                   					recordsLoaded: function(event, data) {
			                   		        	 $(".jtable-edit-command-button").prop("disabled", true);
			                   		         },
			                   					actions: { 
			                   						listAction: 'list.environment.combinations.by.activity?activityId='+activityEnvData.record.activityId,
			                   						//createAction : 'add.clarificationtracker.by.activity',		    									
			    									//editinlineAction : 'update.clarificationtracker.by.activity',	
			                   						}, 
			                   					fields: { 
			                   						  activityId: { 
			                   							type: 'hidden', 
			                   							defaultValue: activityEnvData.record.activityId 
			                   						},  
			                   						envionmentCombinationId: { 
			                       						key: true, 
			                       						create: false, 
			        					                edit: false, 
			                       						list: false
			                       					},
			                       					environmentCombinationName : {
			    										title : '',
			    										inputTitle : 'Environment Combination <font color="#efd125" size="4px">*</font>',
			    										list : true,				    										
			    										create : true,
			    										edit : true,
			    										width : "20%"
			    									},
			    								},
			                   	
			                         formSubmitting: function (event, data) {
			                   
			                        }, 
			                         //Dispose validation logic when form is closed
			                         formClosed: function (event, data) {
			                            data.form.validationEngine('hide');
			                            data.form.validationEngine('detach');
			                        }
			                   	}, function (data) { //opened handler 
			                   			data.childTable.jtable('load'); 
			                   		}); 
			                   	}); 
			                   	//Return image 
			                   	return $img; 
			                   	} 
			                },
							
							activityTask:{
				                	title: '',
			                	    width: "5%",
			                	    edit: true,
			                	    create: false,
			                          	display: function (activityData) { 
			                   		//Create an image that will be used to open child table 
			                   			var $img = $('<img src="css/images/list_metro.png" title="Activity Tasks" />'); 
			                   			//Open child table when user clicks the image 
			                   			$img.click(function () { 
			                   				
			                   				// ----- Closing child table on the same icon click -----
			                   			    closeChildTableFlag = closeJtableTableChildContainer($(this), $('#jTableContainer'));
			                   				if(closeChildTableFlag){
			                   					return;
			                   				} 
			                   				
			                   				$('#jTableContainer').jtable('openChildTable', 
			                   				$img.closest('tr'), 
			                   					{ 
			                   					title: 'Activity Tasks', 
			                   					 editinline:{enable:true},
			                   					recordsLoaded: function(event, data) {
			                   		        	 $(".jtable-edit-command-button").prop("disabled", true);
			                   		         },
			                   					actions: { 
			                   		listAction:'process.activitytask.list?productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&activityWorkPackageId='+activityWorkPackageId+'&activityId='+activityData.record.activityId+'&isActive=1'+'&jtStartIndex=0&jtPageSize=10',
											
									createAction : 'process.activitytask.add',
									editinlineAction : 'process.activitytask.update',
									deleteAction : 'process.activitytask.delete',
			                   						}, 
													
									recordAdded: function (event, data) {
									if(data.serverResponse.Message!='undefined' && data.serverResponse.Message!=""){
										callAlert("Warning: "+data.serverResponse.Message)
									}
								
								},
													
									recordsLoaded:function(){    	
									 $('.portlet > .portlet-title > .tools > .reload').trigger('click'); 
										//console.log("enableAddOrNot"+enableAddOrNot);
										if(enableAddOrNot == "no"){
											$('#jTableContainer .jtable-toolbar-item-add-record').hide();
											//$('#divUploadActivitiesTask').hide();
										 }
								        // var lastWidth = $(".jtable").css('width');
								        //var myVar = setInterval(checkForChanges, 100);
								        
								        /* var flag=false;
							    		function checkForChanges()
							            {
							    			flag=false;
							    			if (!flag && $("#jTableContainer .jtable").eq(0).css('width') != $("#jTableContainer .jtable-title").eq(0).css("width"))
							                {
							                	$("#jTableContainer .jtable-title").eq(0).css("width" , $("#jTableContainer .jtable").eq(0).css('width'));
							                    $("#jTableContainer .jtable-bottom-panel").eq(0).css("width" , $("#jTableContainer .jtable").eq(0).css('width'));                     
							                    flag=true;
							                }
							            }
							        	setInterval(checkForChanges, 100); */								         
								     },
									 
													
			                   fields: { 
			                   						  
									activityId : {
							 				type: 'hidden', 
							 				defaultValue: activityData.record.activityId
									},
								
									activityTaskId : {
										key : true,
										list : false,
										create : false,
									},
									activityTaskName:{
						                 title: 'Task Name',
						                 inputTitle: 'Activity Task <font color="#efd125" size="4px">*</font>',
						            	 list:true,
						            	 edit:true,
						            	 create:true,
						                 width:"20%"
						             },
						             /* activityMasterName : {
											title : 'Activity Master',
											list : true,
											edit : false,
											create : false,
										}, */

								      activityTaskTypeId : {
										title : 'Task Type',
										inputTitle: 'Task Type <font color="#efd125" size="4px">*</font>',
										create : true,
										list : true,
										edit : true,
										width : "20%",
										//options : 'common.list.activity.tasktype'
										options:function () {
                    						return  activityTaskOptionsArr; //Cache results and return options
										}
											
									},
									lifeCycleStageId : {
										title : 'Life Cycle Status',
										create : true,
										list : true,
										edit : true,
										width : "20%",
										dependsOn : 'activityWorkPackageId',
										defaultValue : activityData.record.lifeCycleStageId,
										options:function(data){
										 	var entityTypeId = 34;
										 	var entityId = 0;
										 	var entityInstanceId = data.dependedValues.activityWorkPackageId;
										 	if(typeof entityInstanceId == 'undefined'){
										 		entityInstanceId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
										 	}
											return 'workflow.life.cycle.stages.options?entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId;
							     		},
									},
									plannedStartDate : {
										title : 'Planned Start Date',
										inputTitle : 'Planned Start Date <font color="#efd125" size="4px">*</font>',
										edit : allowPlanDateEndDate,
										create : allowPlanDateEndDate,
										type : 'date',
										defaultValue :setJtableFormattedDate(activityData.record.plannedStartDate),
										width : "20%",
									},
									plannedEndDate : {
										title : 'Planned End Date',
										inputTitle : 'Planned End Date <font color="#efd125" size="4px">*</font>',
										edit : allowPlanDateEndDate,										
										create : allowPlanDateEndDate,
										type : 'date',
										defaultValue : setJtableFormattedDate(activityData.record.plannedEndDate),
										width : "20%",
									},
									workflowRAG : {
										create : false,
										edit : true,
										list : true,
									}, 
									statusDisplayName : {
										title : 'Current Status',
										create : false,
										list : true,
										edit : false,
										width : "20%",
										display: function(data){
											return data.record.statusDisplayName+"&nbsp;"+data.record.workflowStatusCategoryName;
										}
									},
									actors : {
										title : 'Status Pending With',
										create : false,
										list : true,
										edit : false,
										width : "20%"
									},	
									completedBy : {
										title : 'Status Complete By',
										create : false,
										list : true,
										edit : false,
										width : "20%"
									},
									remainingHrsMins : {
										title : 'Status Time Left',
										create : false,
										list : true,
										edit : false,
										width : "20%"
									},
									assigneeId : {
										title : 'Assignee',										
										list :true ,
										create : true,
										edit :true,
										defaultValue : activityData.record.assigneeId,
										options : function(data) {
											return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
										}
									},
									reviewerId : {
										title : 'Reviewer',
										list : true,
										create : true,
										edit : true,
										width : "20%",
										defaultValue : activityData.record.reviewerId,
										options : function(data) {
											return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
										}
									},
									priorityId : {
										title : 'Priority',									
										list : true,
										create : true,
										edit : true,
										width : "20%",
										options : 'administration.executionPriorityList'
									},
									categoryId : {
										title : 'Category',									
										list : true,
										create : true,
										edit : true,

										options : function(data) {
											return 'common.list.executiontypemaster.byentityid?entitymasterid=1';
										}
									},								 
									 enviromentCombinationId:{									
										title : 'Environment Combination',									
										list : true,
										create : true,
										edit : true,
										options : function(envdata) {
											return 'environment.combinations.options.by.activity?activityId='+activityId+'&activityWorkPkgId='+activityWorkPackageId;
										}
								   }, 
							 		/* statusId : {
										title : 'Primary Status',
										create : true,
										list : true,
										edit : false,
										width : "20%",										
										options:function () {		
											
											if(activityTaskOptionsArr[0] !=null){
												statusOption('workflow.getAllStatus.master.option.list?productId='+productId+'&entityTypeId=30&entityId='+activityTaskOptionsArr[0].Value, "primaryStatus");
												console.log("statusOptionsArr :"+statusOptionsArr);
											}	
											return  statusOptionsArr; //Cache results and return options
										}
									
										 options:function(data){
											var entityId = 1;
											return 'workflow.getAllStatus.master.option.list?productId='+currentProductId+'&entityTypeId=30&entityId='+activityTaskTypeId;
						     			} 
									},									
									secondaryStatusId:{
							  			title: 'Secondary Status',						     	  		
						     	  		width: "50%",
						     	  		list:true, 
						     	  		create:true,
						     	  		edit:false,
										dependsOn:'statusId',
										options:function(data){							
										if(data.dependedValues.statusId == null){
												return 'activity.secondary.status.master.option.list?statusId=1';
											}
											else{
												return 'activity.secondary.status.master.option.list?statusId='+data.dependedValues.statusId;
											}
							     		}
							  		},  */
							  										
									/* secondaryStatusName:{
							  			title: 'Secondary Status',						     	  		
						     	  		width: "50%",
						     	  		list:false, 
						     	  		create:false,
						     	  		edit:false,
							  		},  */  
						  		  
									resultId : {
										title : 'Result',
										create : false,
										list : true,
										edit : true,
										width : "20%",
										options : 'process.list.activity.result'
									},
									remark : {
										title : 'Remarks',
										type: 'textarea',  
										width : "10%",
										width : "20%",
										list : false,
									},
									actualStartDate : {
										title : 'Actual Start Date',
										create : false,
										edit : true,
										list : true,
										type : 'date',
										width : "20%",
									},
									actualEndDate : {
										title : 'Actual End Date',
										create : false,
										edit : true,
										list : true,
										type : 'date',
										width : "20%",
									},
									plannedTaskSize : {
										title : 'Planned Task Size',									
										list : true,				    										
										create : true,
										edit : true,
										width : "20%"
										
									},
									actualTaskSize : {
										title : 'Actual Task Size',										
										list : true,				    										
										create : false,
										edit : true,
										width : "20%"
										
									},
									plannedEffort : {
										title : 'Planned Effort',									
										list : true,
										create : true,
										edit : true,
										width : "20%"
									},
									totalEffort : {
	        				        	title : 'Total Effort',
	        				        	create : false,
	        				        	edit : false,
	        				        	list : true,
	        				        	width : "20%",
	        				        	
	        				         },
									percentageCompletion : {
										title : '%Completion',
										create : false,
										list : true,
										edit : true,
										width : "4%"
									},
									workflowIndicator : {
										title : '',
										create : false,
										list : false,
										edit : false,
										width : "20%"
									},
	        				         isActive: { 
	                                   	title: 'Active',
	                                   	list:true,
	                       				edit:true,
	                    				create:false,
	                    				type : 'checkbox',
	                    				values: {'0' : 'No','1' : 'Yes'},
	                    		    	defaultValue: '1'
                    		    	},
	                   	             Cloning:{
		             						title : '',
		             						list : true,
		             						create : false,
		             						width: "5%",
		             						display:function (data) { 
		             						 var $img = $('<img src="css/images/cloning.jpg" style="width: 24px;height: 24px;" title="Cloning Activity" data-toggle="modal" />'); 
		             						$img.click(function () {
		             							var jsonActivityTaskCloningObj = {
		             									"title": "Cloning Activity Task",
		             									"packageName":"Activity Task Name",
		             									"startDate" : dateFormat(data.record.plannedStartDate),
		            									"endDate" : dateFormat(data.record.plannedEndDate),
		             									"selectionTerm" : "Select Activity",
		             									"sourceParentID": data.record.activityId,
		             									"sourceParentName": data.record.activityName,
		             									"sourceID": data.record.activityTaskId,
		             									"sourceName": data.record.activityTaskName,
		             									"componentUsageTitle":"activityTaskClone",
		             							}
		             							Cloning.init(jsonActivityTaskCloningObj);							
		             						});
		             						return $img; 
		             					}
		             	             },
		             	            activityEffortTrackerId:{
	        				        	title : '',
	        				        	list : true,
	        				        	create : false,
	        				        	edit : false,
	        				        	width: "10%",
	        				        	display:function (data) { 
	        				           		//Create an image for test script popup 
	        							//	var $img = $('<i class="fa fa-clock-o" title="Edit status and Enter Effort spent for task"></i>');
	        				           		var $img = $('<i class="fa fa-history showHandCursor" title="Event History"></i>');  
	        			           			//Open Testscript popup  
	        			           			$img.click(function () {
	        			           				addTaskComments(data.record);
	        			           		  });
	        			           			return $img;
	        				        	}
	        				        },
	        				         statusAndPolicies:{
        					        	title : '',
        					        	list : true,
        					        	create : false,
        					        	edit : false,
        					        	width: "5%",
        					        	display:function (data) { 
        					           		//Create an image for test script popup 
        					        		var $img = $('<img src="css/images/workflow.png" title="Configure Workflow" />'); 
        				           			$img.click(function () {
        				           				workflowId = 0;
        				           				entityTypeId = 30;
        				           				statusPolicies(productId, workflowId, entityTypeId, data.record.activityTaskTypeId, data.record.activityTaskId, data.record.activityTaskName, "Task", data.record.statusId);
        				           		  });
        				           			return $img;
        					        	}
        					        },
        					        workflowId:{
        					        	title : 'WorkFlow Template',
        					        	list:false, 
						     	  		create:true,
						     	  		edit:false,
										dependsOn:'activityTaskTypeId',
										options:function(data){							
											var entityId = data.dependedValues.activityTaskTypeId;
											if(typeof entityId == 'undefined' || entityId == null){
												entityId = 0;
											}
											return 'workflow.master.mapped.to.entity.list.options?productId='+productId+'&entityTypeId=30&entityId='+entityId;
							     		}
        					        },
        					        attachment: { 
										title: '', 
										list: true,
										create:false,
										edit:false,
										width: "10%",
										display:function (data) {	        		
							           		//Create an image that will be used to open child table 
											var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>'); 
							       			$img.click(function () {
							       				isViewAttachment = false;
							       				var jsonObj={"Title":"Attachments for Task",			          
							       					"SubTitle": 'Task : ['+data.record.activityTaskId+'] '+data.record.activityTaskName,
							    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=29&entityInstanceId='+data.record.activityTaskId,
							    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=29&entityInstanceId='+data.record.activityTaskId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
							    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
							    	    			"updateURL": 'update.attachment.for.entity.or.instance',
							    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=29',
							    	    			"multipleUpload":true,
							    	    		};	 
								        		Attachments.init(jsonObj);
							       		  });
							       			return $img;
							        	}				
									},
									auditionHistory:{
										title : '',
										list : true,
										create : false,
										edit : false,
										width: "10%",
										display:function (data) { 
										//Create an image for test script popup 
										var $img = $('<i class="fa fa-search-plus" title="Audit History"></i>');
										$img.click(function () {						
											listActivityTaskAuditHistory(data.record.activityTaskId);
										});
										return $img;
										}
									},
								}, 
								
			                   	
			                        formSubmitting: function (event, data) {							   
									// data.form.find('input[name="activityTaskName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[maxSize[500]]');
									 	data.form.find('input[name="activityTaskName"]').addClass('class="validate[required,funcCall[validateSpecialCharactersExceptDot]]", custom[maxSize[500]]');
							      	    data.form.find('input[name="plannedStartDate"]').addClass('validate[required]');
							            data.form.find('input[name="plannedEndDate"]').addClass('validate[required]');
							            data.form.validationEngine();
							           return data.form.validationEngine('validate');
							          }, 
			                         //Dispose validation logic when form is closed
			                        formClosed: function (event, data) {
									data.form.validationEngine('hide');
									data.form.validationEngine('detach');
									}
			                   	}, function (data) { //opened handler 
			                   			data.childTable.jtable('load'); 
			                   		}); 
			                   	}); 
			                   	//Return image 
			                   	return $img; 
			                   	} 
			                },							
			                EnvironmentMapping:{
					        	title : '',
					        	list : true,
					        	create : false,
					        	edit : false,
					        	width: "10%",
					        	display:function (data) { 
					        		//Create an image that will be used to open child table 
				           			var $img = $('<img src="css/images/mapping.png" title="Environment Mapping" data-toggle="modal" />'); 
				           			//Open child table when user clicks the image 
				           			$img.click(function () {
										var activityId = data.record.activityId;
										//var productId =	data.record.productId;					
										// ----- DragDrop Testing started----		dragListItemsContainer
										//var productId = document.getElementById("hdnProductId").value;												
										var leftUrl="envicombi.unmappedto.activity.count?productId="+productId+"&activityId="+activityId;							
										var rightUrl = "";							
										var leftDefaultUrl="activity.unmappedenvironmentcombi.byProduct.list?productId="+productId+"&activityId="+activityId+"&jtStartIndex=0&jtPageSize=10";							
										var rightDefaultUrl="activity.Environmentcombination.list?activityId="+activityId+"&productId="+productId;
										var leftDragUrl = "activity.envcombination.mapping?activityId="+activityId;
									    var rightDragtUrl = "activity.envcombination.mapping?activityId="+activityId;					
										var leftPaginationUrl = "activity.unmappedenvironmentcombi.byProduct.list?productId="+productId+"&activityId="+activityId;
										var rightPaginationUrl="";						
										
										jsonActivityObj={"Title":"Map Environment Combination to Activity",
												"leftDragItemsHeaderName":"Available Environment Combinations",
												"rightDragItemsHeaderName":"Mapped Environment Combinations ",
												"leftDragItemsTotalUrl":leftUrl,
												"rightDragItemsTotalUrl":rightUrl,
												"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
												"rightDragItemsDefaultLoadingUrl":rightDefaultUrl,
												"leftDragItemUrl":leftDragUrl,
												"rightDragItemUrl":rightDragtUrl,									
												"leftItemPaginationUrl":leftPaginationUrl,
												"rightItemPaginationUrl":rightPaginationUrl,									
												"leftDragItemsPageSize":"50",
												"rightDragItemsPageSize":"50",
												"noItems":"No Environment Combinations",
												"componentUsageTitle":"Activity-ECToActivity"
												};
										
										DragDropListItems.init(jsonActivityObj);							
										// DragDrop Testing ended----	           				
				           		  });
				           			return $img;
					        	}
					        },
					        attachment: { 
								title: '', 
								list: true,
								create:false,
								edit:false,									
								width: "5%",
								display:function (data) {	        		
					           		//Create an image that will be used to open child table 
									var $img = $('<img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;">&nbsp;['+data.record.attachmentCount+']&nbsp;</img>'); 
					       			$img.click(function () {
					       				isViewAttachment = false;
					       				var jsonObj={"Title":"Attachments for Activity",			          
					    	    			"SubTitle": 'Activity : ['+data.record.activityId+'] '+data.record.activityName,
					    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=28&entityInstanceId='+data.record.activityId,
					    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=28&entityInstanceId='+data.record.activityId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
					    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
					    	    			"updateURL": 'update.attachment.for.entity.or.instance',
					    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=28',
					    	    			"multipleUpload":true,
					    	    		};	 
						        		Attachments.init(jsonObj);
					       		  });
					       			return $img;
					        	}				
							},
							auditionHistory:{
								title : '',
								list : true,
								create : false,
								edit : false,
								width: "10%",
							display:function (data) { 
								//Create an image for test script popup 
								var $img = $('<i class="fa fa-search-plus" title="Audit History"></i>');
								$img.click(function () {						
									listActivityAuditHistory(data.record.activityId);
								});
								return $img;
							}
							},   
								},
								 formCreated: function (event, data) 
					                {
					                    var $input_start_time = data.form.find ('input[name="testTimePicker"]');
					                    $input_start_time.datetimepicker();

					                },
								// This is for closing $img.click(function (data) { 
								 formSubmitting: function (event, data) {							 
									 plannedStartDate=$("input[name=plannedStartDate]").val();
									 plannedEndDate=$("input[name=plannedEndDate]").val();
										
							      	    //data.form.find('input[name="activityName"]').addClass('validate[required],custom[maxSize[500]]');
							      	  	data.form.find('input[name="activityName"]').addClass('class="validate[required,funcCall[validateSpecialCharactersExceptDot]]", custom[maxSize[500]]');
							            data.form.find('input[name="assigneeId"]').addClass('validate[required]');		
							            
							            if(new Date(plannedStartDate)>new Date(plannedEndDate))
										{
											$("#basicAlert").css("z-index", "100001");
											callAlert("Warning: From date should be lessthan or equal to Todate");
											return false;
										}							            
							            data.form.find('input[name="plannedStartDate"]').addClass('validate[required]');
							            data.form.find('input[name="plannedEndDate"]').addClass('validate[required]');
							       		data.form.validationEngine();
							           return data.form.validationEngine('validate');
							          }, 
							});
			$('#jTableContainer').jtable('load');
			 
			$("button[role='button']:contains('Close')").click(function () {
		        $('.ui-dialog').filter(function () {
		            return $(this).css("display") === "block";
		        }).find('#ui-error-dialog').dialog('close');
		    });
			
		}
		
			
		document.onkeydown = function(evt) {
			evt = evt || window.event;
			if (evt.keyCode == 27) {
				if (document.getElementById("div_PopupMain").style.display == 'block') {
					popupClose();
				}
			}
		};	



function addTaskComments(record){	

	var entityInstanceName = record.activityTaskName;	
	var entityInstanceId = record.activityTaskId;
	var modifiedById = record.assigneeId;
	var currentStatusId = record.statusId;
	var currentStatusName = record.statusName;
	var currentStatusDisplayName = record.statusDisplayName;
	var entityTypeId = 30;//Activity task type
	var entityId = record.activityTaskTypeId;
	var actionTypeValue = 0;
	var secondaryStatusId = record.secondaryStatusId;
	var productId=document.getElementById("treeHdnCurrentProductId").value;
	$("#addCommentsMainDiv").modal();			
	$('#addComments').hide();//Display only histroy of task Effort
	var jsonObj = {
			Title : "Task Workflow History: ["+entityInstanceId+"] "+entityInstanceName,	
			entityTypeName : 'Task',		
			entityTypeId : entityTypeId,
			entityInstanceName : entityInstanceName,
			entityInstanceId : entityInstanceId,
			modifiedByUrl : 'common.user.list.by.resourcepool.id',		
			modifiedById : modifiedById,
			raisedDate : new Date(),
			comments : "",
			productId : productId,
			primaryStatusUrl : 'workflow.status.master.option.list?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&currentStatusId='+currentStatusId,
			secondaryStatusUrl : 'workflow.entity.secondary.status.master.option.list?productId='+productId+'&entityTypeId='+entityTypeId+'&statusId='+currentStatusId,
			currentStatusId : currentStatusId,
			currentStatusName : currentStatusDisplayName,
			secondaryStatusId : secondaryStatusId,
			effortListUrl : 'workflow.event.tracker.list?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
			actionTypeValue : actionTypeValue,
			commentsName : commentsReviewActivity,
			urlToSave : 'workflow.event.tracker.add?productId='+productId+'&entityId='+entityId+'&entityTypeId='+entityTypeId+'&primaryStatusId=[primaryStatusId]&secondaryStatusId=[secondaryStatusId]&effort=[effort]&comments=[comments]&sourceStatusId='+currentStatusId+'&approveAllEntityInstanceIds=[approveAllEntityIds]&entityInstanceId='+entityInstanceId+'&attachmentIds=[attachmentIds]&actionDate=[actionDate]',
			// commentsStatus: "['started','InProgress','Completed']",		
	};
	AddComments.init(jsonObj);
}

		
/* 		
function listChangeRequestAuditHistory(changeRequestId){
	clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=ChangeRequest&sourceEntityId='+changeRequestId;
	var jsonObj={"Title":"Change Request Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10,
			"componentUsageTitle":"changeRequestAudit",
	};
	SingleJtableContainer.init(jsonObj);
} */
		function addActivityComments(record){	

			var entityInstanceName = record.activityName;	
			var entityInstanceId = record.activityId;
			var modifiedById = record.assigneeId;
			var currentStatusId = record.statusId;
			var currentStatusName = record.statusName;
			var currentStatusDisplayName = record.statusDisplayName;
			var entityTypeId = 33;//Activity type
			var entityId = record.activityMasterId;
			var actionTypeValue = 0;
			var secondaryStatusId = record.secondaryStatusId;
			//var productId=document.getElementById("treeHdnCurrentProductId").value;
			
			$("#addCommentsMainDiv").modal();			
			$('#addComments').hide();//Display only histroy of task Effort
			var jsonObj = {
					Title : "Activity Workflow History: ["+entityInstanceId+"] "+entityInstanceName,	
					entityTypeName : 'Activity',		
					entityTypeId : entityTypeId,
					entityInstanceName : entityInstanceName,
					entityInstanceId : entityInstanceId,
					modifiedByUrl : 'common.user.list.by.resourcepool.id',		
					modifiedById : modifiedById,
					raisedDate : new Date(),
					comments : "",
					productId : productId,
					primaryStatusUrl : 'workflow.status.master.option.list?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&currentStatusId='+currentStatusId,
					secondaryStatusUrl : 'workflow.entity.secondary.status.master.option.list?productId='+productId+'&entityTypeId='+entityTypeId+'&statusId='+currentStatusId,
					currentStatusId : currentStatusId,
					currentStatusName : currentStatusDisplayName,
					secondaryStatusId : secondaryStatusId,
					effortListUrl : 'workflow.event.tracker.list?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
					actionTypeValue : actionTypeValue,
					commentsName : commentsReviewActivity,
					urlToSave : 'workflow.event.tracker.add?productId='+productId+'&entityId='+entityId+'&entityTypeId='+entityTypeId+'&primaryStatusId=[primaryStatusId]&secondaryStatusId=[secondaryStatusId]&effort=[effort]&comments=[comments]&sourceStatusId='+currentStatusId+'&approveAllEntityInstanceIds=[approveAllEntityIds]&entityInstanceId='+entityInstanceId+'&attachmentIds=[attachmentIds]&actionDate=[actionDate]',
					// commentsStatus: "['started','InProgress','Completed']",		
			};
			AddComments.init(jsonObj);
		}

	</script>	
</div>
<div id="popUpAvailableDr" class="modal" tabindex="-1" aria-hidden="true" >
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
<div><%@include file="cloning.jsp"%></div>
<div><%@include file="dragDropListItems.jsp"%></div>
<script src="js/activitydrscript.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="js/activitymanagement.js" type="text/javascript"></script>
<script type="text/javascript" src="js/Scripts/popup/iosOverlay.js"></script>
<script src="js/importData.js" type="text/javascript"></script>
<script src="js/activitySearch.js" type="text/javascript"></script>
<script src="js/pageSpecific/download.js" type="text/javascript"></script>
<script src="js/viewScript/instanceWorkflowSummary.js" type="text/javascript"></script>
<script src="js/viewScript/WorkflowIndicatorDetailView.js" type="text/javascript"></script>
	<!-- END JAVASCRIPTS -->
	<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
</body>
<!-- END BODY -->
</html>