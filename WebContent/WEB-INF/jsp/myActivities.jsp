<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

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
<!-- <link rel="stylesheet" href="css/style.css" type="text/css" media="all"> -->
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link href="files/lib/metronic/theme/assets/global/plugins/icheck/skins/all.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet"	type="text/css"></link>
<link href="css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"></link>

<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>

<link href="css/customStyle.css" rel="stylesheet" type="text/css">

<!-- END THEME STYLES -->
<style>
.ui-dialog .ui-widget-content, .ui-dialog .ui-dialog-content{
	max-height:500px !important;
	overflow-x:hidden !important;
}
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

	.select2-drop-active {
		z-index:100060 !important;
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
<spring:eval var="customActivityListHeaderFieldsEnable" expression="@ilcmProps.getProperty('CUSTOM_HEADER_FIELDS_ENABLE')" />

<body>
<form action="" method="post" class="button" name="importForm" id="importForm" enctype="multipart/form-data">
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
<div><%@include file="treeStructureSidebarLazyLoading.jsp" %></div>

<div><%@include file="postHeader.jsp" %></div>
<!-- BEGIN PAGE CONTENT -->
<div class="page-content">
		<div class="container-fluid">			
			<!-- BEGIN PAGE CONTENT INNER -->
			<div class="row margin-top-10" id="toAnimate">
				<div class="col-md-12">
					<!-- BEGIN PORTLET-->
					<div class="portlet light ">
						<div class="portlet-title" style="margin-bottom: 0px;">
							<div class="caption caption-md">
								<i class="icon-bar-chart theme-font hide"></i>
								<span class="caption-subject theme-font bold uppercase">My Activities</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font">: My Activities</span>
								<span class="caption-helper hide"></span>
							</div>						
							<div class="actions" style="padding-top: 4px;padding-left: 5px;">
								<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" 
									onClick="fullScreenHandlerDTMyActivities()" data-original-title="" title="FullScreen"></a>
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
						<div class="portlet-body">
							<div class="row list-separated" style="margin-top:0px;">	
								<!-- Tabs -->
								<div class="col-md-12"  id="myActivitiesTab" style="padding-left: 0px; display:none;">
									<div class = "toolbarFullScreen">
									  <ul class="nav nav-tabs" id="tabslistmyActivities">
										<!-- Commented code: getting activity count url is taking more time than need to be fixed once done this code will  be reverted back -->
										
										 <li class="active"><a href="#assigneeActivities" data-toggle="tab" onclick="showMyActivities(0);">My Activities</a></li> 
										 <li><a href="#PendingWithActivities" data-toggle="tab" onclick="showMyActivities(1);">Pending With Me</a></li>
										 <li><a href="#PassingThrough" data-toggle="tab" onclick="showMyActivities(2);">Passing Through Me</a></li>
									  </ul>
									</div>
									<div class="tab-content" id="tbCntnt" style="display:none;">
										
										<!-- Activity -->
										<div id="assigneeActivities" class="tab-pane fade active in">	
											<div id="assigneeActivitiesContainerParent">
											</div>
										
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
										<!-- Activity Ends -->
										
										<!-- Activity -->
										<div id="PendingWithActivities" class="tab-pane fade">	
											<div id="pendingWithActivitiesContainerParent">
											</div>
											
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
										<!-- Activity Ends -->
										
										<!-- Activity -->
										<div id="PassingThrough" class="tab-pane fade">
											<div id="passingThroughContainerParent">
											</div>
											
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
										<!-- Activity Ends -->
										
										<div id="hidden"></div>
										
								<!-- 	</div> -->
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


	<!-- Popup -->
<div id="activityTabSummaryContainer" class="modal " tabindex="-1" aria-hidden="true" style="width: 95%; top: 5%; left: 3%;">
	<div class="modal-full">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title"></h4>
				<div id="activityAddLoaderIcon" style="display:none;z-index:100001;position:absolute;top: 3%;left: 48%;">
					<img src="css/images/ajax-loader.gif">
				</div>
			</div>
			<div class="modal-body">
			    <div id="activityTabSummaryLoaderIcon" style="display:none; z-index:100001; position:absolute; top:2%; left:50%">
					<img src="css/images/ajax-loader.gif"/>
				</div>	
				<div class="scroller" style="height:450px" data-always-visible="1" data-rail-visible1="1">
					<div id="ActivitySummary">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


<div id="div_PopupPokeNotifications" class="modal " tabindex="-1" aria-hidden="true">
	<div class="modal-dialog" style="width: 400px;">
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
												<textarea id="pokeInput" style="width: 300px;height:100px;"></textarea>	 
												</div>
											</div>
										</div>
										<div class="form-actions" style="padding-top: 5px;padding-bottom: 5px;">
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

<script>
var activityInvisibleColumnValue= ${activityInvisibleColumnIndex};

</script>

<% ResourceBundle resBun = ResourceBundle.getBundle("TAFServer");
    String autoAllocate=resBun.getString("ACTIVITY_AUTOALLOCATE");  	
	String changeReqeustTitleAWP = resBun.getString("CHANGEREQUEST_TABLE_TITLE");	
%>

<script type="text/javascript">
var autoAllocateFromProperty = "<%=autoAllocate%>";
var changeRequestToUsecase = "<%=changeReqeustTitleAWP%>";
</script>

<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp" %></div>			
<!-- END FOOTER -->

<div> <%@include file ="myActivitySummaryGrouping.jsp" %></div>
 
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/jquery.vmap.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.sparkline.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="files/lib/metronic/theme/assets/admin/layout/scripts/quick-sidebar.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jstree/dist/jstree.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>

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
<!-- <script src="js/select2.min.js" type="text/javascript"></script> -->

<script src="js/viewScript/activityCreation.js" type="text/javascript"></script>

<div><%@include file="addComments.jsp"%></div>
<div><%@include file="cloning.jsp"%></div>
<div><%@include file="singleJtableContainer.jsp" %></div>
<div><%@include file="singleDataTableContainer.jsp" %></div>

<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
<div><%@include file="dragDropListItems.jsp"%></div> 
<div><%@include file="comments.jsp"%></div>
<script src="js/pageSpecific/download.js" type="text/javascript"></script>

<!-- testing started-->
<div><jsp:include page="customFields.jsp"></jsp:include></div>
<!-- testing ended -->	

<%-- <div><%@include file="assignedActivityGrouping.jsp"%></div> --%>
<!-- For Activities Ends -->

<div> <%@include file ="activityChart.jsp" %></div>
<script src="js/importData.js" type="text/javascript"></script>
<script type="text/javascript" src="js/datatable/Editor-1.5.6/js/dataTables.editor.js"></script>
<script src="js/pagination/jquery.paging.min.js" type="text/javascript"></script>
<script src="js/activitySearch.js" type="text/javascript"></script>
<script src="js/viewScript/myActivities.js" type="text/javascript"></script>
<script src="js/viewScript/activityListingGroupingDT.js" type="text/javascript"></script>
<script src="js/viewScript/instanceWorkflowConfiguration.js" type="text/javascript"></script>
<script src="js/activitymanagement.js" type="text/javascript"></script>
<script src="js/viewScript/activityGroupingManagement.js" type="text/javascript"></script>
<script src="js/viewScript/attachmentsMethAWPACT.js" type="text/javascript"></script>
<script src="js/viewScript/auditHistoryListingContainer.js" type="text/javascript"></script>


<script type="text/javascript">
var userRole = '${roleName}';
var customActivityListHeaderFieldsEnable="${customActivityListHeaderFieldsEnable}";

function setLoginUserId(){
	 loginUserId = '<%=session.getAttribute("ssnHdnUserId")%>';
}

</script>
</form>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>