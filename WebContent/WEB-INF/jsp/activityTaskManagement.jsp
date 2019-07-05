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
<link href="css/customStyle.css" rel="stylesheet" type="text/css">

<link href="js/datatable/Editor-1.5.6/css/dataTables.editor.css" rel="stylesheet" type="text/css"></link>
<link href="js/datatable/Editor-1.5.6/css/editor.dataTables.css" rel="stylesheet" type="text/css"></link>
<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>
<!-- END THEME STYLES -->

<style type="text/css">
#filterIsActiveTask > #status_dd >.select2me{
	height: 26px;
    padding-top: 1px;
    margin-top: 6px;
    width: 90px !important;
    font-size: 13px;
}
#filterIsActiveTask> #status_dd >.select2me > a{
 	height: 28px !important; 
     padding-top: 0px; 
     margin-top: -1px; 
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
				<div class="row margin-top-10" id="toAnimate">
					<div class="col-md-12">
						<!-- BEGIN PORTLET-->
						<div class="portlet light ">
							<div class="portlet-title">
								<div class="caption caption-md">
									<i class="icon-bar-chart theme-font hide"></i> <span
										class="caption-subject theme-font bold uppercase">ACTIVITY TASK</span>&nbsp;<span
										id="headerTitle" class="caption-subject theme-font"></span> <span
										class="caption-helper hide">weekly stats...</span>
								</div>
							</div>
							<div class="portlet-body">
								<div class="row list-separated" style="margin-top: 0px;">

									<!-- jtable started -->
										<!-- Filer -->
										<!-- <div id="filter" style="float:right;display:none">
											<div id="status_dd" class="col-md-4">
											<select class="form-control input-small select2me" id="isActiveTask_ul">
												<option value="2" selected ><a href="#">ALL</a></option>
												<option value="1" ><a href="#">Active</a></option>
												<option value="0" ><a href="#">Inactive</a></option>
											</select>
										</div>
    								</div> -->
								<!-- End Filter -->
									<!-- Main -->
									<div id="main"
										style="float: left; padding-top: 0px; width: 100%;">
										<div id="hdnDiv"></div>

										<!-- Box -->
										<div class="box">
											<div id="hidden"></div>
											<!-- ContainerBox -->
											<div class="cl">&nbsp;</div>
											<div id="taskSearchBox" style="display:none">
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
								        			<option class="assigneeList_ids" value="-">-</option> 			   			
												</select>
											</div>
										</div>
											<div class='col-md-2'>       
											<div id="searchStatus_dd" class="">												
												<select class="form-control input-small  select2me" id="statusSearch_ul">
								        			<option class="statusList_ids" value="-">-</option> 			   			
												</select>
											</div>
										</div>
								
										<div class='col-md-2'>     
											<div id="searchPriorityList_dd" class="">
												<select class="form-control input-small  select2me" id="prioritySearch_ul">
								        			<option class="priorityList_ids" value="-">-</option>  			   			
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
											<div class="jScrollContainerResponsive jScrollContainerFullScreen" style="clear: both; padding-top: 10px;position:relative;">
												<!--  <div id="filter" style="float:right;display:none;position: absolute;Z-index: 10;right: 60px"> -->
												<div class="col-md-8 jScrollTitleContainer" id="divUploadActivitiesTask" style="position: absolute; z-index: 10;
											 		margin-left: 180px;padding-right: 0px;">
											 	<div class="col-md-8" id="divUploadActivityTaskContainer">
												 	<div class="col-md-2" style="padding-right: 10px;padding-top: 8px;margin-top: 2px;margin-right: 20px;width: 95px;">
														<label style="color:  #FFF;font-family: sans-serif;font-size: 1.1em;">Upload : </label>
													</div>
												 	<div class="col-md-5" style="padding-left: 0px;margin-top: 0px;margin-left: -17px;">													
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
		
		<div><%@include file="singleJtableContainer.jsp"%></div>
	</div>
	<!-- END PAGE-CONTAINER -->

	<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp"%></div>
	<!-- END FOOTER -->
	
	<input type="hidden" id="activityAssigneeId"/>
	<input type="hidden" id="activityReviewerId"/>
	
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
	<!--  <div id="div_PopupMain" class="divPopUpMain" style="display:none;"> -->
	<!--         <div title="Press Esc to close" onclick="popupClose()" class="ImgPopupClose"> -->
	<!--         </div> -->
	<!-- 	</div> -->
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
	<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
	<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
	<script src="js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
	<script src="js/importData.js" type="text/javascript"></script>
	<script src="js/pageSpecific/download.js" type="text/javascript"></script>
	
	<script src="js/viewScript/instanceWorkflowConfiguration.js" type="text/javascript"></script>
	<script src="js/viewScript/instanceWorkflowSummary.js" type="text/javascript"></script>
	
	<div><%@include file="addComments.jsp"%></div>
		<%-- <div><%@include file="dynamicJtableContainer.jsp"%></div> --%>

<script type="text/javascript">
var defaultuserId = '${userId}';
/* function addComments(record){	
	var activityTaskUI = [];
	var taskName= record.activityTaskName;
	//alert(record.activityTaskName);
	var modifiedByName=record.reviewerName;
	var modifiedById=record.assigneeId;
	var primaryStatusId=record.statusId;
	var sourceStatus=record.statusName;
	var taskId=record.activityTaskId;
	var effortList='';
	var productId=document.getElementById("treeHdnCurrentProductId").value;
	var secondaryStatusId=record.secondaryStatusId;
	//alert("ModofiedBy"+modifiedById);
	//alert("ModofiedByName"+modifiedByName);
 $("#addCommentsMainDiv").modal();			

 var jsonObj = {
		Title : "Effort Details",	
		entityType: 'process.list.activity.entity.master',		
		entityTypeDefault: '2',
		entityNameDefault:taskName,
	    modifiedBy: 'common.user.list.by.resourcepool.id',		
	    modifiedByDefault: modifiedById,
	    raisedDate: '2016-03-11',
	    comments:"",	   
		activityStatus:'activity.primary.status.master.option.list?productId='+productId,
		activityStatusDefault:primaryStatusId,
		secondaryStatus:'activity.secondary.status.master.option.list?statusId='+primaryStatusId,
		taskId:taskId,
		effortList:'activitytask.effort.tracker.list?taskId='+taskId,
		sourceStatus:sourceStatus,
		commentsName:"Effort",
		secondaryStatusId:secondaryStatusId
	 
	};
	AddComments.init(jsonObj);
	} */
	
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

</script>


	<script type="text/javascript">
	var activityAssigneeId = '';
	var activityReviewerId = '';
	var allowPlanDateEndDate = true;
	var defaultTaskPlannedStartDate = new Date();
	var defaultTaskPlannedEndDate = new Date();
	var defaultTaskLifeCycleStage = 0;
	
	if('${roleName}' == "Tester"){	
		allowPlanDateEndDate = false;		
	}
		
	var isFirstLoad = true;
	var productId;
	var productName;
	jQuery(document).ready(
						function() {
							QuickSidebar.init(); // init quick sidebar
							setBreadCrumb("Activity Task Management");
							createHiddenFieldsForTree();
							setPageTitle("Products");							
							//getTreeData('administration.product.activity.tree');
							var actionType=3;//All action
							//getTreeData('process.review.activity.tree?type='+actionType);
							getTreeData('administration.product.activity.tree?actionType='+actionType);
							/* var treeProductId=document.getElementById("treeHdnCurrentProductId").value;
							if(treeProductId != null && treeProductId !="") {
								statusOption('common.list.activity.tasktype?productId='+treeProductId,null);
							} */
							statusOption('common.list.activity.tasktype?productId=',null);
							setDefaultnode();
							
							var nodeType;
							$("#treeContainerDiv").on("select_node.jstree",
											function(evt, data) {
												if(data.node != undefined){
												var entityIdAndType = data.node.data;
												var arry = entityIdAndType
														.split("~");
												var key = arry[0];
												var type = arry[1];
												var title = data.node.text;
												var date = new Date();
												var timestamp = date.getTime();
												nodeType = type;
												var loMainSelected = data;
												var productVersionId;
												var productBuildId;
												var activityWorkPackageId;
												var activityId;
												var enableAddOrNot = "no";
												
												defaultActivityPlannedStartDate = new Date();
												defaultActivityPlannedEndDate = new Date();
												defaultActivityLifeCycleStage = 0;
												
												//var activityAssigneeId = defaultuserId;
												activityAssigneeId = defaultuserId;
												if(data.node.original.activityAssigneeId!="undefined" && data.node.original.activityAssigneeId!=null && data.node.original.activityAssigneeId!= ""){
													activityAssigneeId = data.node.original.activityAssigneeId;
													
												}
												//var activityReviewerId = defaultuserId;
												activityReviewerId = defaultuserId;
												if(data.node.original.activityReviewerId!="undefined" && data.node.original.activityReviewerId!=null && data.node.original.activityReviewerId!= ""){
													activityReviewerId = data.node.original.activityReviewerId;
												}
												uiGetParents(loMainSelected);
												if(nodeType == "Product"){
											 		productId=key;
											 		productName = title;
											 		productVersionId = 0;
													productBuildId = 0;
													activityWorkPackageId = 0;
													activityId = 0;
													enableAddOrNot = "no";
													var isActive = $("#isActiveTask_ul").find('option:selected').val();	
													listActivitiesOfSelectedActivity(productId,productVersionId,productBuildId,activityWorkPackageId,activityId,isActive,enableAddOrNot,activityAssigneeId,activityReviewerId);
													//$("#filterIsActiveTask").show();
											 	}
												if(nodeType == "ProductVersion"){
											 		productId=document.getElementById("treeHdnCurrentProductId").value;
											 		productName = document.getElementById("treeHdnCurrentProductName").value;
											 		productVersionId = key;
													productBuildId = 0;
													activityWorkPackageId = 0;
													activityId = 0;
													enableAddOrNot = "no";
													var isActive = $("#isActiveTask_ul").find('option:selected').val();	
													listActivitiesOfSelectedActivity(productId,productVersionId,productBuildId,activityWorkPackageId,activityId,isActive,enableAddOrNot,activityAssigneeId,activityReviewerId);
													//$("#filterIsActiveTask").show();
											 	}
												 if(nodeType == "ProductBuild"){
													 	productId=document.getElementById("treeHdnCurrentProductId").value;
													 	productName = document.getElementById("treeHdnCurrentProductName").value;
														productVersionId=document.getElementById("treeHdnCurrentProductVersionId").value;
														productBuildId = key;
														activityWorkPackageId = 0;
														activityId = 0;
														enableAddOrNot = "no";
														var isActive = $("#isActiveTask_ul").find('option:selected').val();	
														listActivitiesOfSelectedActivity(productId,productVersionId,productBuildId,activityWorkPackageId,activityId,isActive,enableAddOrNot,activityAssigneeId,activityReviewerId);
														//$("#filterIsActiveTask").show();
												 }

												if (nodeType == "ActivityWorkPackage") {
													productId=document.getElementById("treeHdnCurrentProductId").value;
													productName = document.getElementById("treeHdnCurrentProductName").value;
													productVersionId=document.getElementById("treeHdnCurrentProductVersionId").value;
													productBuildId=document.getElementById("treeHdnCurrentProductBuildId").value;
													activityWorkPackageId = key;
													activityId = 0;
													enableAddOrNot = "no";
													document.getElementById("treeHdnCurrentActivityWorkPackageId").value = activityWorkPackageId;
													var isActive = $("#isActiveTask_ul").find('option:selected').val();	
													listActivitiesOfSelectedActivity(productId,productVersionId,productBuildId,activityWorkPackageId,activityId,isActive,enableAddOrNot,activityAssigneeId,activityReviewerId);
													//$("#filterIsActiveTask").show();
													
												}
												if (nodeType == "Activity") {	
													 ActivitySearchInitloadList();
                                                    enableAddOrNot = "yes";
													productId=document.getElementById("treeHdnCurrentProductId").value;
													productName = document.getElementById("treeHdnCurrentProductName").value;
													productVersionId=document.getElementById("treeHdnCurrentProductVersionId").value;
													productBuildId=document.getElementById("treeHdnCurrentProductBuildId").value;
													activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
													activityId = key;
													if(typeof data.node.original.plannedStartDate != "undefined" && data.node.original.plannedStartDate != null && data.node.original.plannedStartDate != ""){
														defaultTaskPlannedStartDate = data.node.original.plannedStartDate;
													}
													if(typeof data.node.original.plannedEndDate != "undefined" && data.node.original.plannedEndDate != null && data.node.original.plannedEndDate != ""){
														defaultTaskPlannedEndDate = data.node.original.plannedEndDate;
													}
													if(typeof data.node.original.lifeCycleStage != "undefined" && data.node.original.lifeCycleStage != null && data.node.original.lifeCycleStage != ""){
														defaultTaskLifeCycleStage = data.node.original.lifeCycleStage;
													}
													document.getElementById("treeHdnCurrentActivityId").value=activityId;
													var isActive = $("#isActiveTask_ul").find('option:selected').val();		
													listActivitiesOfSelectedActivity(productId,productVersionId,productBuildId,activityWorkPackageId,activityId,isActive,enableAddOrNot,activityAssigneeId,activityReviewerId);
													statusOption('common.list.activity.tasktype?productId='+productId,null);
													//$("#filterIsActiveTask").show();
													$('#divUploadActivityTaskContainer').show();
													//$("#taskSearchBox").show();
												}
											}

											});
							
							 $(document).on('change','#isActiveTask_ul', function() {
								 var isActive = $("#isActiveTask_ul").find('option:selected').val();	
								productId=document.getElementById("treeHdnCurrentProductId").value;
								productName = document.getElementById("treeHdnCurrentProductName").value;
								productVersionId=document.getElementById("treeHdnCurrentProductVersionId").value;
								productBuildId=document.getElementById("treeHdnCurrentProductBuildId").value;
								activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
								activityId	= document.getElementById("treeHdnCurrentActivityId").value;	
								if(nodeType == "Activity"){
									enableAddOrNot = "yes";
									$('#divUploadActivityTaskContainer').show();
								}
								 listActivitiesOfSelectedActivity(productId,productVersionId,productBuildId,activityWorkPackageId,activityId,isActive,enableAddOrNot,activityAssigneeId,activityReviewerId);
								// $("#filterIsActiveTask").show();
								 //$("#taskSearchBox").show();
								 
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
					console.log(defaultNodeId)
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
			//document.getElementById("lbl_PopupTitle").innerHTML = "";
			//document.getElementById("lbl_RightTitle").innerHTML = "";
			//$("#tbl_PopupData").empty();
			$("#div_PopupMain").fadeOut("normal");
			$("#div_PopupBackground").fadeOut("normal");
		}

		/* Load Poup function */
		function loadPopup(divId) {
			$("#" + divId).fadeIn(0500); // fadein popup div
			$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
			// IE7, IE8
			$("#div_PopupBackground").fadeIn(0001);
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

function listActivitiesOfSelectedActivity(productId,productVersionId,productBuildId,activityWorkPackageId,activityId,isActive,enableAddOrNot,activityAssigneeId,activityReviewerId) {
	activityTaskRecordArr=[];
	
	var urlToGetActivitiesOfSpecifiedActivityId = 'process.activitytask.list?productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&activityWorkPackageId='+activityWorkPackageId+'&activityId='+activityId+'&isActive='+isActive;

			try {
				if ($('#jTableContainer').length > 0) {
					$('#jTableContainer').jtable('destroy');
				}
			} catch (e) {
			}
			$('#jTableContainer')
					.jtable(
							{
								title : 'Activity Task',
								editinline : {
									enable : true
								},
								selecting : true, //Enable selecting 
								paging : true, //Enable paging
								pageSize : 10,								
								actions : {
									listAction : urlToGetActivitiesOfSpecifiedActivityId,
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
											$('#divUploadActivityTaskContainer').hide();
											
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
								fields : {
									
									activityId : {
							 				type: 'hidden', 
							 				defaultValue: activityId 
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
										defaultValue : defaultTaskLifeCycleStage,
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
										defaultValue : defaultTaskPlannedStartDate,
										width : "20%",
									},
									plannedEndDate : {
										title : 'Planned End Date',
										inputTitle : 'Planned End Date <font color="#efd125" size="4px">*</font>',
										edit : allowPlanDateEndDate,										
										create : allowPlanDateEndDate,
										type : 'date',
										defaultValue : defaultTaskPlannedEndDate,
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
										defaultValue : activityAssigneeId,
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
										defaultValue : activityReviewerId,
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
								// This is for closing $img.click(function (data) {  
							});
			$('#jTableContainer').jtable('load');
			
			$("button[role='button']:contains('Close')").click(function () {
		        $('.ui-dialog').filter(function () {
		            return $(this).css("display") === "block";
		        }).find('#ui-error-dialog').dialog('close');
		    });
			
		}

function listActivityTaskAuditHistory(activityTaskId){
	clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=ActivityTask&sourceEntityId='+activityTaskId+'&jtStartIndex=0&jtPageSize=1000';
	//var url='administration.event.list?sourceEntityType=ActivityTask&sourceEntityId='+activityTaskId;
	var jsonObj={"Title":"ActivityTask Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":1000,	    
			"componentUsageTitle":"activityTaskAudit",
	};
	SingleDataTableContainer.init(jsonObj);
	//SingleJtableContainer.init(jsonObj);
}

		document.onkeydown = function(evt) {
			evt = evt || window.event;
			if (evt.keyCode == 27) {
				if (document.getElementById("div_PopupMain").style.display == 'block') {
					popupClose();
				}
			}
		};
		
	</script>
<div><%@include file="cloning.jsp"%></div>
<script src="js/activityTaskSearch.js" type="text/javascript"></script>
<script src="js/viewScript/WorkflowIndicatorDetailView.js" type="text/javascript"></script>
	<!-- END JAVASCRIPTS -->
	<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
</body>
<!-- END BODY -->
</html>