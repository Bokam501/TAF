<style>
	.activity-pencil-edit{
		padding-right: 5px;
	}	
</style>

<script>

function activityTabSummaryHandler(){	
	     var activityTabSummaryText = '<div id="myActivityStatusSummaryView" style="width: 96%; top: 2%; left: 2%;padding-top: 0px; padding-bottom: 0px;"><div class="row">'+
	     /* '<a class="btn btn-circle btn-icon-only btn-default fa fa-list-alt" href="javascript:;" onclick="createCustomFields();"'+ 
	     'data-original-title="" title="Custom Fields" style="vertical-align: middle;float: right;margin-right: 25px;cursor: pointer;padding-top: 10px;"></a>'+ */
		 
	     '<div class="col-md-6"><div class="table-scrollable"><table class="table table-striped table-hover" id="dfnTable">'+
			'<thead><tr height="30"><th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Overview</th>'+
					'<th></th></tr></thead>'+
			'<tbody><tr><td>Id : </td><td><span id="myActivityId" ></span></td></tr>'+			
			'<tr><td>Activity Name : </td><td><i class="fa fa-pencil activity-pencil-edit"></i><span id="myActivityName" contenteditable="true" onblur="updateActivityDetailsInSummary(event);"></span></td></tr>'+
			'<tr><td>Engagement / Product:	</td><td><span id="engagementorproduct"></span></td></tr>'+
			'<tr><td>Activity Workpackage :	</td><td><span id="myActivityWorkpackage"></span></td></tr>'+
			'<tr><td>Activity Type  : </td><td id="myActivityType">'+updateDropDownActivitySummary("myActivityType")+'</td></tr>'+
			'<tr><td>Feature  :	</td><td id="productFeatureId">'+updateDropDownActivitySummary("productFeatureId")+'</td></tr>'+
			'<tr><td>Weightage  : </td><td><span id="weightage"></span></td></tr>'+
			'<tr><td>Tracker Number  :	</td><td><span id="spTrackerNumber"></span></td></tr>'+			
			 '<tr><td>Category  : </td><td id="spCategoryName">'+updateDropDownActivitySummary("spCategoryName")+'</td></tr>'+	
			'<tr><td>Remark : </td><td><textArea id="spRemark" contenteditable="true" onchange="updateActivityDetailsInSummary(event);" style="margin: 0px; width: 300px; height: 100px;"></textArea></td></tr>'+			
			'<tr><td>Priority :	</td><td id="spPriority">'+updateDropDownActivitySummary("spPriority")+'</td></tr>'+			
			'<tr><td>Complexity : </td><td id="spComplexity">'+updateDropDownActivitySummary("spComplexity")+'</td></tr>'+
			'</tbody></table></div>'+	
	
		'<div class="table-scrollable"><table class="table table-striped table-hover" id="relatedEntityTable">'+
		'<thead><tr height="30"><th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Related </th><th></th></tr></thead>'+
		'<tbody><tr><td>Task(s): </td><td><span id="spTaskCount"></span></td></tr>'+
		'<tr><td>Clarification(s): </td><td><span id="spDrReferenceNumber"></span></td></tr>'+
		'<tr><td>ChangeRequest(s): </td><td><span id="spChangeRequest"></span></td></tr>'+
		'<tr><td>Environment Combination : </td><td><span id="spEnvcombination"></span></td></tr></tbody></table></div></div>'+
		
		'<div class="col-md-6"><div class="table-scrollable"><table class="table table-striped table-hover" id="statusTable">'+
		'<thead><tr height="30"><th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Status </th><th></th></tr></thead>'+
		'<tbody><tr><td>Current Status : </td><td><span id="spCurrentStatus"></span></td></tr>'+
		'<tr><td>Assignee : </td><td id="spAssignee">'+updateDropDownActivitySummary("spAssignee")+'</td></tr>'+
		'<tr><td>Reviewer : </td><td id="spReviewer">'+updateDropDownActivitySummary("spReviewer")+'</td></tr>'+
		'<tr><td>Pending With : </td><td><span id="spPendingWith"></span></td></tr>'+
		'<tr><td>Life Cycle Stage : </td><td id="spLifeCycleStatus">'+updateDropDownActivitySummary("spLifeCycleStatus")+'</td></tr>'+
		'<tr><td>Workflow Template : </td><td id="spworkflowId">'+updateDropDownActivitySummary("spworkflowId")+'</td></tr>'+
		'<tr><td>% Completion :	</td><td><i class="fa fa-pencil activity-pencil-edit"></i><span id="spPercentageCompletion" contenteditable="true" onblur="updateActivityDetailsInSummary(event);"></span></td></tr></tbody></table></div>'+

		'<div class="table-scrollable"><table class="table table-striped table-hover" id="datesTable">'+
		'<thead><tr height="30"><th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Track </th>'+
				'<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Baseline </th>'+
				'<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Planned </th>'+
				'<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Actual </th><th></th></tr></thead>'+
			'<tbody><tr><td>Start Date : </td><td><span id="spBaselineStartDate"></span></td>'+
			/* '<td><span id="spPlannedStartDate"></span></td>'+ */			
			/* '<td><span id="spActualStartDate"></span></td><td></td></tr>'+ */
			'<td><span><input class="form-control input-small date-picker" id="activityPlannedStartDate" size="10" type="text" value="" readonly="readonly" style="cursor: default; background-color: rgb(255, 255, 255);"></span></td>'+
			'<td><span><input class="form-control input-small date-picker" id="activityActualStartDate" size="10" type="text" value="" readonly="readonly" style="cursor: default; background-color: rgb(255, 255, 255);"></span></td></tr>'+
				'<tr><td>End Date : </td><td><span id="spBaselineEndDate"></span></td>'+
				/* '<td><span id="spPlannedEndDate"></span></td>'+
				'<td><span id="spActualEndDate"></span></td><td></td></tr>'+ */
				'<td><span><input class="form-control input-small date-picker" id="activityPlannedEndDate" size="10" type="text" value="" readonly="readonly" style="cursor: default; background-color: rgb(255, 255, 255);"></span></td>'+
				'<td><span><input class="form-control input-small date-picker" id="activityActualEndDate" size="10" type="text" value="" readonly="readonly" style="cursor: default; background-color: rgb(255, 255, 255);"></span></td></tr>'+
				
				'<tr><td>Activity Size : </td><td><span id="spbaselineActivitySize" onblur="updateActivityDetailsInSummary(event);"></span></td>'+						
					'<td><i class="fa fa-pencil activity-pencil-edit"></i><span id="spplannedActivitySize" contenteditable="true" onblur="updateActivityDetailsInSummary(event);"></span></td>'+
					'<td><i class="fa fa-pencil activity-pencil-edit"></i><span id="spactualActivitySize" contenteditable="true" onblur="updateActivityDetailsInSummary(event);"></span></td></tr>'+
				'<tr><td>Effort : </td><td><span id="spBaselineEffort"></span></td>'+						
					'<td><span id="spPlannedEffort"></span></td>'+
					'<td><span id="spTotalEffort"></span></td></tr>'+
				'<tr><td>Unit : </td><td><span id="spBaselineUnit" ></span></td><td><span id="spPlannedUnit"></span></td><td><span id="spActualUnit"></span></td><td></td></tr></tbody></table></div>'+
	
		'<div class="table-scrollable"><table class="table table-striped table-hover" id="auditTable">'+
		'<thead><tr height="30"><th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Audit </th><th></th></tr></thead>'+
			'<tbody><tr><td>Created On : </td><td><span id="spCreatedOn"></span></td></tr>'+
			'<tr><td>Created By : </td><td><span id="spCreatedBy"></span></td></tr>'+		
			'<tr><td>Last Modified On : </td><td><span id="spModifiedOn"></span></td></tr>'+
			'<tr><td>Modified By : </td><td><span id="spModifiedBy"></span></td></tr></tbody></table></div></div></div><div class="row" id="customFieldsInActivitySummary"></div></div>';
			
			return activityTabSummaryText;	
}
</script> 

<style>

	.modal-open .datepicker {
	    z-index: 100061 !important;
	}

	#dfnTable td, #membersTable td, #productsTable td {
		border-top:0px;
	} 
	#dfnTable th, #membersTable th, #productsTable th {
		border-bottom:1px solid #ddd;
	} 
	#dfnTable tbody tr td:nth-child(1) {
		width:200px !important;
	}
	#membersTable tbody tr td:nth-child(1), #produtcsTable tbody tr td:nth-child(1) {
		width:300px !important;
	}
	
	/* Medium devices (desktops, 992px and up) */
	@media (min-width: 992px) {
	 #myActivityStatusSummaryView {overflow:auto;}
	}
	/* Large devices (large desktops, 1200px and up) */
	@media (min-width: 1200px) { 
	 #myActivityStatusSummaryView {overflow:auto;}
	}
	/* Xtra Large devices (xtra large desktops, 1600px and up) */
	@media (min-width: 1500px) { 
	 #myActivityStatusSummaryView {overflow:auto;}
	}
	@media (min-width: 1600px) { 
	 #myActivityStatusSummaryView {overflow:auto;}
	}
	@media (min-width: 1800px) { 
	 #myActivityStatusSummaryView {overflow:auto;}
	}
</style>