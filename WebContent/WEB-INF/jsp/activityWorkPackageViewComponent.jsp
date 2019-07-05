<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<title></title>
</head>
<spring:eval var="customActivityListHeaderFieldsEnable" expression="@ilcmProps.getProperty('CUSTOM_HEADER_FIELDS_ENABLE')" />
<body>
	
	<div id="ACTWPSummaryPage">
		<div class="row wrkPckgTabCntnt">
			<div class="col-md-12 toolbarFullScreen">
				<ul class="nav nav-tabs" id="tablistACTWP">
					<li class="active"><a href="#wpAccordionWD" data-toggle="tab">Overview</a></li>							
					<li><a href="#wpDashBoard" data-toggle="tab">Dashboard</a></li>
	            	<li><a href="#wpActivities" data-toggle="tab">Activities</a></li>
	            	<li><a href="#wpClarifications" data-toggle="tab">Clarifications</a></li>
	            	<li><a href="#wpChangeRequests" data-toggle="tab">Change Requests</a></li>
	            	<li><a href="#wpAttachments" data-toggle="tab">Attachments</a></li>
	            	<li><a href="#wpWorkFlow" data-toggle="tab">Workflow</a></li>
	            	<li><a href="#wpAuditHistory" data-toggle="tab">Audit History</a></li>	            							            			 
       			</ul>
			</div>
		</div>
		
	<div class="tab-content actwrkPckgTabCntntDetails" style="padding-left: 0px;padding-right: 15px;" >
    	<div id="wpAccordionWD" class="tab-pane fade active in panel-group accordion "  >
				<div class="panel panel-default">					
					
					<div class="row"  id="actworkPackageDiv">
						<div class="col-md-6">
							<div class="table-scrollable">
								<table class="table table-striped table-hover" id="dfnTable">
								<!-- 	<thead>
									<tr height="30">
										<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Definition </th>
										<th>
										</th>
									</tr>
									</thead> -->
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
										<td><!-- <span id="spCustomer"></span> -->
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
										<td><!-- <span id="spCustomer"></span> -->
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
						</div>
						<div class="col-md-6">
							<div class="table-scrollable">
								<table class="table table-striped table-hover" id="opnTable">									
									<tbody>				
									     <tr>
											<td>Number of Activities(s) :
											</td>
											<td><span class="label label-sm label-proper" id="sel_Activitycount"></span></td>
										</tr>				
										<tr>
											<td>Number of Clarifications(s) :
											</td>
											<td><span class="label label-sm label-proper" id="sel_CLARTcount"></span></td>
										</tr>
										<tr>
											<td>Number of ChangeRequest(s) :
											</td>
											<td><span class="label label-sm label-proper" id="sel_CRcount"></span>
											</td>
										</tr>
										<tr>
											<td>Number of Attachment(s) :
											</td>
											<td><span class="label label-sm label-proper" id="sel_ATTcount"></span>
											</td>
										</tr>
									</tbody>
								</table>
							</div>							
						</div>
					</div>
				</div>				
			</div>
		<div id="wpDashBoard" class="tab-pane fade">
			<div class=" btn-group" data-toggle="buttons" style="width:100%">
				<label class="btn darkblue active" data-name="ragSummary" onclick="showGroupDashboardSummaryTableTool(1);">
				<input type="radio" class="toggle" >RAG Summary</label>
				<label class="btn darkblue" data-name="workflowSummary" onclick="showGroupDashboardSummaryTableTool(2);">
				<input type="radio" class="toggle" >Workflow Summary</label>
		   </div> 
			<h4 class="modal-title" id="dashboardRAGHeaderSubTitle" style="padding-top: 5px;"></h4>
			<h4 class="modal-title" id="dashboardWorkflowHeaderSubTitle" style="padding-top: 5px;"></h4>  
		
			<div id="RAGSummary">				
				<div class="scroller" style="height: 100%; padding: 0px;" data-always-visible="1" data-rail-visible1="1">						
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
						<!-- </div> -->													
					</div>						
				</div>     					
			</div>			
			<div id="workflowSummary" style="display:none">				
				<div class="scroller" style="height: 100%; padding: 0px;" data-always-visible="1" data-rail-visible1="1">						
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
						<!-- </div> -->													
					</div>						
				</div>     					
			</div>			
		</div>			
		<div id="wpActivities" class="tab-pane fade">			
			<table id="jTableContainerWPActivities_dataTable"  class="cell-border compact row-border" cellspacing="0" width="100%">
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
		<div id="wpClarifications" class="tab-pane fade">
			<div style="clear: both; padding-top: 10px;position: relative;">
		       <div id="jTableContainerWPClarifications"></div>
			</div>
		</div>							
		<div id="wpChangeRequests" class="tab-pane fade">
			<div class="row" style="position: absolute;z-index: 10; display:none">
				<input id="uploadFileChangeRequests" type="file" name="uploadFileChangeRequests" 
					   onclick="{this.value = null;};" onchange="importChangeRequests();" />
			</div>
			<div id="filterForWChangeRequest" style="position: absolute;z-index: 10;right: 160px;margin-top: 3px;display: none;">
				<div id="changeRequestTabWstatus_dd" class="col-md-4">
					<select class="form-control input-small select2me" id="changeRequestTabWstatus_ul">
						<option value="2" ><a href="#">All</a></option>
						<option value="1" selected><a href="#">Active</a></option>
						<option value="0" ><a href="#">Inactive</a></option>
					</select>
				</div>
			</div>
			<div id="jTableContainerWPChangeRequest"></div>		
      </div>
     	<div id="wpAttachments" class="tab-pane fade">
     		<div style="clear: both; padding-top: 10px;position: relative;">
				<div id="jTableContainerforWPAttachments"></div>
			</div>
       	</div>
     	<div id="wpWorkFlow" class="tab-pane fade">
     		<div style="clear: both; padding-top: 10px;position: relative;">
				<div id="jTableContainerforWorkflowPlanWorkpackage"></div>
			</div>
       	</div>
     	<div id="wpAuditHistory" class="tab-pane fade">
     		<div style="clear: both; padding-top: 10px;position: relative;">
				<div id="jTableContainerforAuditHistoryWorkpackage"></div>
			</div>
       	</div>			
       				
		</div>						
	</div>
	
<input type="hidden" id="hdnStartDateAWP" value=""/>
<input type="hidden" id="hdnEndDateAWP" value=""/>
<input type="hidden" id="hdnFieldTypeAWP" value=""/>

<script type="text/javascript">	
	var customActivityListHeaderFieldsEnable="${customActivityListHeaderFieldsEnable}";	
</script>
<script src="js/viewScript/customFieldsForAllInstanceOfEntity.js" type="text/javascript"></script>
<script src="js/viewScript/comments.js" type="text/javascript"></script>
<script src="js/viewScript/instanceWorkflowConfiguration.js" type="text/javascript"></script>	
<script src="js/viewScript/activityListingGrouping.js" type="text/javascript"></script>
<script src="js/viewScript/activityListingGroupingDT.js" type="text/javascript"></script>
<script src="js/activityWorkPackageViewComponent.js" type="text/javascript"></script>
<script src="js/viewScript/instanceGroupingDashBoardSummary.js" type="text/javascript"></script>
<!-- Included attachmentmeth in parent AWPGrouping-->
<!-- <script src="js/viewScript/attachmentsMethAWPACT.js" type="text/javascript"></script> -->
</body>

</html>