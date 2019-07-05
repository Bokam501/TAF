<script type="text/javascript">
var plannedActivitySize=0;
function showMyActivitySummaryDetails(activityId){
	var urlToGetMyActivitySummaryOfSelectedActivityId = 'my.Activity.status.summary?activityId='+activityId;
	$.ajax({
		type:"POST",
	 	contentType: "application/json; charset=utf-8",
		url : urlToGetMyActivitySummaryOfSelectedActivityId,
		dataType : 'json',
		cache:false,
		recordsLoaded: function(event, data) {
		$('.portlet > .portlet-title > .tools > .reload').trigger('click');
								 },
		success : function(data) {
			var result=data.Records;
			
			var activityName = "";	
			var activityWorkPackageName = "";
			var activityType = "";
			var status = "";
			var productFeatureName="";
			var assigneeName = "";
			var reviewerName="";
			var trackerNumber="";
			var priority="";
			var createdOn = "";
			var remark="";
			var clarificationNumber="";
			var envcombination="";			
			var lastModifiedDate = "";
			var plannedStartDate="";
			var numberOfOpenStatus = "";	
			var numberOfOnHoldStatus = "";	
			var numberOfCompletedStatus = "";
			var totalNumberOfTasks = "";	
			var plannedEndDate="";
			var createdByName="";
			var modifiedByName="";
			var categoryName="";
			var taskInProgressStatusRecords = "";
			var loggedInUserTaskCount ="";
			var actualStartDate="";
			var actualEndDate="";
			var changeRequest="";			
			var actualActivitySize="";
			var productName="";
			var engagementName="";
			
			
			if(data.Records.length === 0) {
				 $("#myActivityName").text("");
			} else {
				$.each(result, function(i,item){ 
					activityName = item.activityName;
					activityWorkPackageName = item.activityWorkPackageName;
					createdOn = item.createdDate;
					activityType=item.activityMasterName;
					productFeatureName=item.productFeatureName;		
					remark=item.remark;
					priority=item.priorityName;
					status=item.statusCategoryName;
					assigneeName=item.assigneeName;
					reviewerName=item.reviewerName;
					drReferenceNumber=item.drReferenceNumber;
					lastModifiedDate = item.modifiedDate;
					numberOfOpenStatus = item.taskOpenStatusValue;	
					numberOfOnHoldStatus = item.taskOnHoldStatusRecords;	
					numberOfCompletedStatus = item.taskCompletedStatusRecords;
					totalNumberOfTasks = item.totalTaskCount;	
					plannedStartDate=item.plannedStartDate;
					plannedEndDate=item.plannedEndDate;
					createdByName=item.createdByName;
					modifiedByName=item.modifiedByName;
					trackerNumber=item.activityTrackerNumber;
					categoryName=item.categoryName;
					loggedInUserTaskCount=item.loggedInUserTaskCount;
					taskInProgressStatusRecords=item.taskInProgressStatusRecords;
					envcombination=item.enviCombination;
					clarificationNumber=item.clarificationNumber;
					actualStartDate=item.actualStartDate;
					actualEndDate=item.actualEndDate;
					changeRequest=item.changeRequest;
					plannedActivitySize=item.plannedActivitySize;
					actualActivitySize=item.actualActivitySize;
					productName=item.productName;
					engagementName=item.engagementName;
					
				});
			} 
			$("#myActivityName").text(activityName);
			$("#myActivityWorkpackage").text(activityWorkPackageName);
		 	$("#myRequirement").text(productFeatureName); 
			$("#spStatus").text(status);
			$("#myActivityType").text(activityType);
			$("#spRemark").text(remark);				
			$("#spPriority").text(priority);
			$("#spAssignee").text(assigneeName);
			$("#spCategoryName").text(categoryName);			
			$("#spReviewer").text(reviewerName);			
			if(changeRequest == 0){
			$("#spChangeRequest").text("-");	
			}else{
				$("#spChangeRequest").text(changeRequest);
			}
			if(envcombination == 0){
			$("#spEnvcombination").text("-");	
			}else{
				$("#spEnvcombination").text(envcombination);
			}
			if(clarificationNumber == 0){
			$("#spDrReferenceNumber").text("-");	
			}else{
				$("#spDrReferenceNumber").text(clarificationNumber);
			}
			if(trackerNumber == 0){				
			$("#spTrackerNumber").text("-");
			}else{
				$("#spTrackerNumber").text(trackerNumber);
			}
			
			$("#spactualActivitySize").text(actualActivitySize);
			$("#spplannedActivitySize").text(plannedActivitySize);
			$("#spCreatedBy").text(createdByName);
			$("#spCreatedOn").text(createdOn);
			$("#spModifiedBy").text(modifiedByName);
			$("#spModifiedOn").text(lastModifiedDate);
			/* $("#spTotalTaskStatus").text(totalNumberOfTasks); */
			$("#spTotalOpenStatus").text(numberOfOpenStatus);
			$("#spTotalOnHoldStatus").text(numberOfOnHoldStatus);
			$("#spTotalCompletedStatus").text(numberOfCompletedStatus);
			$("#spTotalTaskAssigned").text(loggedInUserTaskCount);
			$("#spTotalInprogressStatus").text(taskInProgressStatusRecords);
			$("#spPlannedStartDate").text(plannedStartDate);
			$("#spPlannedEndDate").text(plannedEndDate);
			$("#engagementorproduct").text(engagementName+" / "+productName);
			if(actualStartDate == null){
				$("#spActualStartDate").text("-");	
				}else{
					$("#spActualStartDate").text(actualStartDate);
				}
			if(actualEndDate == null){
				$("#spActualEndDate").text("-");	
				}else{
					$("#spActualEndDate").text(actualEndDate);
				}
		},
		complete:function(data) {
		}
	});
}
</script>

<div class="row"  id="statusSummaryView">
	<div class="col-md-6">
		<div class="table-scrollable">
			<table class="table table-striped table-hover" id="dfnTable">
				<thead>
				<tr height="30">
					<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Activity Details</th>
					<th>
					</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td>ActivityName :
					</td>
					<td><span id="myActivityName"></span>
					</td>
				</tr>
							
				<tr>
					<td>EngageMent / Product:
					</td>
					<td><span id="engagementorproduct"></span>
					</td>
				</tr>
				<tr>
					<td>Activity Workpackage :
					</td>
					<td><span id="myActivityWorkpackage"></span>
					</td>
				</tr>
				<tr>
					<td>Requirement  : 
					</td>
					<td><span id="myRequirement"></span></td>
				</tr>
				<tr>
					<td>ActivityType  : 
					</td>
					<td><span id="myActivityType"></span></td>
				</tr>
				<tr>
					<td>Tracker Number  : 
					</td>
					<td><span id="spTrackerNumber"></span></td>
				</tr>
					</tr>
					<tr>
					<td>Category  : 
					</td>
					<td><span id="spCategoryName"></span></td>
				</tr>
				
				<tr>
					<td>Status :
					</td>
					<td><span id="spStatus"></span>
					</td>
				</tr>	
				<tr>
					<td>Assignee :
					</td>
					<td><span id="spAssignee"></span>
					</td>
				</tr>	
				<tr>
					<td>Reviewer :
					</td>
					<td><span id="spReviewer"></span>
					</td>
				</tr>					
				<tr>
					<td>Remark :
					</td>
					<td><span id="spRemark"></span>
					</td>
				</tr>
				<tr>
					<td>Priority :
					</td>
					<td><span id="spPriority"></span>
					</td>
				</tr>	
				<tr>
					<td>Clarification :
					</td>
					<td><span id="spDrReferenceNumber"></span>
					</td>
				</tr>	
					<tr>
					<td>ChangeRequest :
					</td>
					<td><span id="spChangeRequest"></span>
					</td>
				</tr>	
				
					<tr>
					<td>Environment Combination :
					</td>
					<td><span id="spEnvcombination"></span>
					</td>
				</tr>
				<tr>
					<td>Planned Start Date :
					</td>
					<td><span id="spPlannedStartDate"></span>
					</td>
				</tr>	
					<tr>
					<td>Planned End Date :
					</td>
					<td><span id="spPlannedEndDate"></span>
					</td>
				</tr>
					<tr>
					<td>Actual Start Date :
					</td>
					<td><span id="spActualStartDate"></span>
					</td>
				</tr>	
					<tr>
					<td>Actual End Date :
					</td>
					<td><span id="spActualEndDate"></span>
					</td>
				</tr>		
				<tr>
					<td>Planned Activity Size :
					</td>
					<td><span id="spplannedActivitySize"></span>
					</td>
				</tr>	
					<tr>
					<td>Actual Activity Size :
					</td>
					<td><span id="spactualActivitySize"></span>
					</td>
				</tr>		
				<tr>
					<td>Created On :
					</td>
					<td><span id="spCreatedOn"></span>
					</td>
				</tr>
				<tr>
					<td>Created By :
					</td>
					<td><span id="spCreatedBy"></span>
					</td>
				</tr>
				
				
				<tr>
					<td>Last Modified On : 
					</td>
					<td><span id="spModifiedOn"></span></td>
				</tr>
				<tr>
					<td>Modified By : 
					</td>
					<td><span id="spModifiedBy"></span></td>
				</tr>
				
				</tbody>
			</table>
		</div>
	</div>
	<div class="col-md-6">
		<div class="table-scrollable">
			<table class="table table-striped table-hover" id="membersTable">
				<thead>
				<tr height="30">
					<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Activity task Details </th>
					<th>
					</th>
				</tr>
				</thead>
				<tbody>					
					<tr>
						<td>Total task available in selected activity :
						</td>
						<td><span class="label label-sm label-proper" id="spTotalTaskStatus"></span></td>
					</tr>
					
				</tbody>
			</table>
		</div>
		<div class="table-scrollable">
			<table class="table table-striped table-hover" id="productsTable">
				<thead>
				<tr height="30">
					<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Assigned task details</th>
					<th>
					</th>
				</tr>
				</thead>
				<tbody>
					<tr>
						<td>Number of task assigned to you :
						</td>
						<td><span class="label label-sm label-proper" id="spTotalTaskAssigned"></span></td>
					</tr>
					<tr>
						<td>OPEN status :
						</td>
						<td><span class="label label-sm label-proper" id="spTotalOpenStatus"></span></td>
					</tr>
					<tr>
						<td>INPROGRESS/REWORK status :
						</td>
						<td><span class="label label-sm label-proper" id="spTotalInprogressStatus"></span></td>
					</tr>
					<tr>
						<td>ONHOLD status :
						</td>
						<td><span class="label label-sm label-proper" id="spTotalOnHoldStatus"></span></td>
					</tr>
					<tr>
						<td>COMPLETED status :
						</td>
						<td><span class="label label-sm label-proper" id="spTotalCompletedStatus"></span></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<style>
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
 #statusSummaryView {max-height:285px;overflow:auto;}
}
/* Large devices (large desktops, 1200px and up) */
@media (min-width: 1200px) { 
 #statusSummaryView {max-height:285px;overflow:auto;}
}
/* Xtra Large devices (xtra large desktops, 1600px and up) */
@media (min-width: 1500px) { 
 #statusSummaryView {max-height:380px;overflow:auto;}
}
@media (min-width: 1600px) { 
 #statusSummaryView {max-height:500px;overflow:auto;}
}
@media (min-width: 1800px) { 
 #statusSummaryView {max-height:800px;overflow:auto;}
}
</style>