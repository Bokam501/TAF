<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import = "java.util.ResourceBundle" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>

<style>
	#filterForChangeRequest > #changeRequeststatus_dd > .select2me > a{
		height: height: 30px !important;
	}
	#filterForChangeRequest > #changeRequeststatus_dd > .select2me{
		width: 110px !important;
	    height: 30px !important;
	}
	
</style>

<% ResourceBundle resourceChange = ResourceBundle.getBundle("TAFServer");
    String changeReqeustTitle = resourceChange.getString("CHANGEREQUEST_TABLE_TITLE");    
%>

<body>
	<div id="ProductACTWPSummaryPage">
		<div class="row prodTabCntnt">
			<div class="col-md-12 toolbarFullScreen">
   				<ul class="nav nav-tabs" id="tablistProductACTWP">
      				<li class="active"><a href="#accordionWD" data-toggle="tab">Overview</a></li>						
      				<li><a href="#prodDashBoard" data-toggle="tab">Dashboard</a></li>
	            	<li><a href="#prodAWP" data-toggle="tab">Activity WorkPackage</a></li>
	            	<li><a href="#prodActivities" data-toggle="tab">Activities</a></li>
	            	<li><a href="#prodClarifications" data-toggle="tab">Clarifications</a></li>
	            	<li><a href="#prodChangeRequests" data-toggle="tab">Change Requests</a></li>
	            	<li><a href="#prodAttachments" data-toggle="tab">Attachments</a></li>	            	
	            	<li><a href="#prodAuditHistory" data-toggle="tab">Audit History</a></li>
	            							            			 
       			</ul>
			</div>
		</div>
		<div class="tab-content prodCntntDetails" style="padding-left: 0px;padding-right: 15px;" >
	    	<div id="accordionWD" class="tab-pane fade active in panel-group accordion"  >
				<div class="panel panel-default">					
					<div class="row"  id="productActWorkPackageDiv">
						<div class="col-md-6">
							<div class="table-scrollable">
								<table class="table table-striped table-hover" id="dfnTable">
									<thead>
									<tr height="30">
										<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Overview</th>
										<th>
										</th>
									</tr>
									</thead>
									<tbody>
									
									<tr>
										<td>Product Id / Name :
										</td>
										<td>
										<!-- <span id="prodIdPS" contenteditable="true" onblur="updateProductSummary('prodIdPS');"></span> -->
										<span id="prodIdPS" contenteditable="true"></span>
										</td>
									</tr>
									<!-- <tr>
										<td>Product :
										</td>
										<td>
										<span id="prodNamePS" contenteditable="true"></span>
										</td>
									</tr> -->			
									<tr>
										<td>Description :
										</td>
										<td>
										<span id="descPS"></span>
										</td>
									</tr>				
									<tr>
										<td>TestFactory Id / Name :
										</td>
										<td>
										<span id="testFactIdPS"></span>
										</td>
									</tr>
									<!-- <tr>
										<td>TestFactory :
										</td>
										<td><span id="testFactNamePS"></span>
										</td>
									</tr> -->
									<tr>
										<td>Version  Id / Name : 
										</td>
										<td><span id="verIdPS"></span></td>
									</tr>				
									<!-- <tr>
										<td>Version  : 
										</td>
										<td><span id="verNamePS"></span></td>
									</tr> -->
									<tr>
										<td>Build Id / Name : 
										</td>
										<td><span id="buildIdPS"></span></td>
									</tr>
										</tr>
										<!-- <tr>
										<td>Build Name  : 
										</td>
										<td><span id="buildNamePS"></span></td>
									</tr> -->													
									<tr>
										<td>Customer :
										</td>
										<td><span id="customerPS"></span>
										</td>
									</tr>													
									<tr>
										<td>ProjectCode :
										</td>
										<td><span id="projectCodePS"></span>
										</td>
									</tr>													
									<tr>
										<td>Project :
										</td>
										<td><span id="projectNamePS"></span>
										</td>
									</tr>													
									<tr>
										<td>ProductType :
										</td>
										<td><span id="producttypePS"></span>
										</td>
									</tr>													
									<tr>
										<td>ProductMode :
										</td>
										<td><span id="productmodePS"></span>
										</td>
									</tr>											
									
									</tbody>
								</table>
							</div>
							
							<div class="table-scrollable">
								<table class="table table-striped table-hover" id="relatedEntityTable">
									<thead>
									<tr height="30">
										<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Related </th>
										<th>
										</th>
									</tr>
									</thead>
									<tbody>					
										<tr>
											<td>Clarification:
											</td>
											<td><span id="clarificationsPS"></span>
											</td>
										</tr>	
										<tr>
											<td>ChangeRequest:
											</td>
											<td><span id="ChangeRequestsPS"></span>
											</td>
										</tr>	
										<tr>
											<td>Attachment:
											</td>
											<td><span id="attachmentsPS"></span>
											</td>
										</tr>
										
									</tbody>
								</table>
							</div>
						</div>
						<div class="col-md-6">
							
							<!-- <div class="table-scrollable">
								<table class="table table-striped table-hover" id="statusTable">
									<thead>
									<tr height="30">
										<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Status </th>
										<th>
										</th>
									</tr>
									</thead>
									<tbody>					
										<tr>
											<td>Current Status :
											</td>
											<td><span id="spCurrentStatus"></span></td>
										</tr>
										<tr>
											<td>Assignee :
											</td>
											<td><span id="spAssignee"></span></td>
										</tr>
										<tr>
											<td>Reviewer :
											</td>
											<td><span id="spReviewer"></span></td>
										</tr>
										
										<tr>
											<td>Pending With : 
											</td>
											<td><span id="spPendingWith"></span></td>
										</tr>					
									
										<tr>
											<td>Life Cycle Stage : 
											</td>
											<td><span id="spLifeCycleStatus"></span></td>
										</tr>
									
										<tr>
											<td>% Completion : 
											</td>
											<td><span id="spPercentageCompletion"></span></td>
										</tr>
									</tbody>
								</table>
							</div> -->
						
							<!-- <div class="table-scrollable">
								<table class="table table-striped table-hover" id="datesTable">
									<thead>
									<tr height="30">
										<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Track </th>
										<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Baseline </th>
										<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Planned </th>
										<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Actual </th>					
										<th>
										</th>
									</tr>
									</thead>
									<tbody>
										<tr>
											<td>Start Date :
											</td>
											<td><span id="spBaselineStartDate"></span></td>
											<td><span id="spPlannedStartDate"></span></td>
											<td><span id="spActualStartDate"></span></td>																		
											<td></td>
										</tr>
										<tr>
											<td>End Date :
											</td>
											<td><span id="spBaselineEndDate"></span></td>
											<td><span id="spPlannedEndDate"></span></td>
											<td><span id="spActualEndDate"></span></td>						
											<td></td>
										</tr>					
										<tr>
											<td>Activity Size :
											</td>
											<td><span id="spbaselineActivitySize" contenteditable="true" onblur="updateProductSummary('baselineActivitySize');"></span></td>
											<td><span id="spplannedActivitySize" contenteditable="true" onblur="updateProductSummary('plannedActivitySize');"></span></td>
											<td><span id="spactualActivitySize" contenteditable="true" onblur="updateProductSummary('actualActivitySize');"></span></td>
											<td></td>
										</tr>					
										<tr>
											<td>Effort :
											</td>
											<td><span id="spBaselineEffort" contenteditable="true" onblur="updateProductSummary('baselineEffort');"></span></td>						
											<td><span id="spPlannedEffort" contenteditable="true" onblur="updateProductSummary('plannedEffort');"></span></td>
											<td><span id="spTotalEffort" contenteditable="true" onblur="updateProductSummary('actualEffort');"></span></td>
											<td></td>
										</tr>					
									</tbody>
								</table>
							</div> -->
							
							<div class="table-scrollable">
								<table class="table table-striped table-hover" id="auditTable">
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
										<td><span id="createdOnPS"></span>
										</td>
									</tr>
									<tr>
										<td>Created By :
										</td>
										<td><span id="createdByPS"></span>
										</td>
									</tr>				
									<tr>
										<td>Last Modified On : 
										</td>
										<td><span id="modifiedOnPS"></span></td>
									</tr>
									<tr>
										<td>Modified By : 
										</td>
										<td><span id="modifiedByPS"></span></td>
									</tr>
										
									</tbody>
								</table>
							</div>
						</div>
	
						<!-- <div class="col-md-6">
							<div class="table-scrollable">
								<table class="table table-striped table-hover" id="dfnTable">
									<tbody>
									<tr>
										<td>Product/Version/Build :
										</td>
										<td><span id="prodVerBuildName" ></span>
										</td>
									</tr>
									<tr>
										<td>Activity WorkPackage Name : 
										</td>
										<td><span id="act_wpkg_name"  contentEditable='true' onChange="updateModifiedValue('act_wpkg_name');" ></span></td>
									</tr>
									<tr>
										<td>Description :
										</td>
										<td><span id="act_wpkg_desc" contentEditable='true' onChange="updateModifiedValue('act_wpkg_desc');"></span>
										</td>
									</tr>
									<tr>
										<td>Planned Date :
										</td>
										<td>
											<div class="input-group" id="plan_defaultrange">								
											<input type="text" class="form-control" id="planned_fromTodatepicker"/>
												<span class="input-group-btn">
													<button class="btn default date-range-toggle" type="button"  style="height:34px"><i class="fa fa-calendar"></i></button>
												</span>
											</div>
										</td>
									</tr>
									<tr>
										<td>Actual Date :
										</td>
										<td>
										<div class="input-group" id="actual_defaultrange">								
											<input type="text" class="form-control" id="actual_fromTodatepicker"/>
												<span class="input-group-btn">
													<button class="btn default date-range-toggle" type="button" style="height:34px"><i class="fa fa-calendar"></i></button>
												</span>
										</div>
										</td>
									</tr>
									<tr>
										<td>Status :
										</td>
										<td><span id="status_txt"></span>
										</td>
									</tr>
									<tr>
										<td>Owner :
										</td>
										<td><span id="act_wpkg_owner"></span>
										</td>
									</tr>
									</tbody>
								</table>
							</div>
						</div> -->
						<!--<div class="col-md-6">
							<div class="table-scrollable">
								<table class="table table-striped table-hover" id="opnTable">
									
									<tbody>
									
									     <tr>
											<td>Activities(s) :
											</td>
											<td><span class="label label-sm label-proper" id="sel_Activitycount"></span></td>
										</tr>
										<tr>
											<td>Clarifications(s) :
											</td>
											<td><span class="label label-sm label-proper" id="sel_CLARTcount"></span></td>
										</tr>
										<tr>
											<td>ChangeRequest(s) :
											</td>
											<td><span class="label label-sm label-proper" id="sel_CRcount"></span>
											</td>
										</tr>
										<tr>
											<td>Attachment(s) :
											</td>
											<td><span class="label label-sm label-proper" id="sel_ATTcount"></span>
											</td>
										</tr>
									</tbody>
								</table>
							</div>		
						</div> -->
					</div>
				</div>				
			</div>
			<div id="prodDashBoard" class="tab-pane fade">
				<div class=" btn-group" data-toggle="buttons" style="width:100%">
					<label class="btn darkblue active" data-name="productRAGSummary" onclick="showProductGroupDashboardSummaryTableTool(1);">
					<input type="radio" class="toggle" >RAG Summary</label>
					<label class="btn darkblue" data-name="productWorkflowSummary" onclick="showProductGroupDashboardSummaryTableTool(2);">
					<input type="radio" class="toggle" >Workflow Summary</label>
				</div> 
				<h4 class="modal-title" id="productDashboardRAGHeaderSubTitle" style="padding-top: 10px;"></h4>
				<h4 class="modal-title" id="productDashboardWorkflowHeaderSubTitle" style="padding-top: 5px;"></h4>  
				
				<div id="productRAGSummary">				
					<!-- <div class="scroller" style="height: 100%; padding: 0px;" data-always-visible="1" data-rail-visible1="1"> -->						
						<div class="container" id="test" style="height: 100%; padding: 0px;">																						
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
				<div id="productWorkflowSummary" style="display:none">				
					<!-- <div class="scroller" style="height: 100%; padding: 0px;" data-always-visible="1" data-rail-visible1="1"> -->						
						<div class="container" id="test" style="height: 100%; padding: 0px;">								
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
					<!-- </div> -->     					
				</div>		
			</div>
			<div id="prodAWP" class="tab-pane fade">
			     <div id="jTableContainerProductAWPList"></div>
			</div>
			<div id="prodActivities" class="tab-pane fade">
				<table id="productActivities_dataTable"  class="cell-border compact row-border" cellspacing="0" width="100%">
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
			<div id="prodClarifications" class="tab-pane fade">
				<div style="clear: both; padding-top: 10px;position: relative;">
			       <div id="jTableContainerProdAWPClarifications"></div>
				</div>
			</div>							
			<div id="prodChangeRequests" class="tab-pane fade">
				<div class="row" style="position: absolute;z-index: 10; display:none">
					<input id="uploadFileChangeRequests" type="file" name="uploadFileChangeRequests" 
						   onclick="{this.value = null;};" onchange="importChangeRequests();" />
				</div>
				<div id="filterForChangeRequest" style="position: absolute;z-index: 10;right: 160px;margin-top: 3px;display: none;">
					<div id="changeRequestTabstatus_dd" class="col-md-4">
						<select class="form-control input-small select2me" id="changeRequestTabstatus_ul">
							<option value="2" ><a href="#">All</a></option>
							<option value="1" selected><a href="#">Active</a></option>
							<option value="0" ><a href="#">Inactive</a></option>
						</select>
					</div>
				</div>
				<div id="jTableContainerProdAWPChangeRequests"></div>				
	       	</div>
	     	<div id="prodAttachments" class="tab-pane fade">
	     		<div style="clear: both; padding-top: 10px;position: relative;">
					<div id="jTableContainerforProdAWPAttachments"></div>
				</div>
	       	</div>
	     	<div id="prodAuditHistory" class="tab-pane fade">
	     		<div style="clear: both; padding-top: 10px;position: relative;">
					<div id="jTableContainerforProdAWPAuditHistory"></div>
				</div>
	       	</div>			
		</div>						
	</div>

<input type="hidden" id="hdnStartDateAWP" value=""/>
<input type="hidden" id="hdnEndDateAWP" value=""/>
<input type="hidden" id="hdnFieldTypeAWP" value=""/>
	
<!-- <script src="js/viewScript/activityListingGrouping.js" type="text/javascript"></script> -->
<script src="js/productActivityWorkPackageViewComponent.js" type="text/javascript"></script>
<script src="js/viewScript/instanceGroupingDashBoardSummary.js" type="text/javascript"></script>
<!-- Included attachmentmeth in parent AWPGrouping-->
<!-- <script src="js/viewScript/attachmentsMethAWPACT.js" type="text/javascript"></script> -->

<script type="text/javascript">
var changeRequestToUsecase = "<%=changeReqeustTitle%>";
</script>
</body>

</html>