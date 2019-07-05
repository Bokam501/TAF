/* ----- Activity Creation View -----*/
//	var defaultuserId = '${userId}';


function createAcitivityView(){
	var activityText = '<div>'+
	 
    '<div class="row" id="myActivityStatusSummaryView"><div class="col-md-6"><div class="table-scrollable"><table class="table table-striped table-hover" id="dfnTable">'+
		'<thead></thead>'+
		'<tbody><tr>'+			
		'<tr><td>Activity Name : </td><td><input id="activityCreationName" type="text" class="form-control" placeholder="Activity name" required></td></tr>'+
		'<tr><td>Requirement :	</td><td id="activityCreationRequirement">'+updateDropDownActivityCreation("activityCreationRequirement")+'</td></tr>'+
		'<tr><td>Planned Start Date :	</td><td><span><input class="form-control input-small date-picker" id="activityCreationPlannedStartDate" size="10" type="text" value="" readonly="readonly" style="cursor: default; background-color: rgb(255, 255, 255);"></span></td></tr>'+
		'<tr><td>Assignee : </td><td id="activityCreationAssignee">'+updateDropDownActivityCreation("activityCreationAssignee")+'</td></tr>'+
		/*'<tr><td>BOT :	</td><td id="activityCreationBOT">'+updateDropDownActivityCreation("activityCreationBOT")+'</td></tr>'+*/
		'<tr><td>Complexity : </td><td id="activityCreationComplexity">'+updateDropDownActivityCreation("activityCreationComplexity")+'</td></tr>'+
		'<tr><td>Category :	</td><td id="activityCreationCategory">'+updateDropDownActivityCreation("activityCreationCategory")+'</td></tr>'+			
		 '<tr><td>Planned Activity Size : </td><td><input id="activityCreationPlannedSize" type="text" class="form-control" placeholder="Planned Activity Size"></td></tr>'+	
		'<tr><td>% Completion : </td><td><input id="activityCreationPercentageCompletion" type="text" class="form-control" placeholder="% Completion"></td></tr>'+			
		'<tr><td>Activity Type :	</td><td id="activityCreationType">'+updateDropDownActivityCreation("activityCreationType")+'</td></tr>'+
		'<tr><td>Workflow Template :	</td><td id="activityCreationWorkflowTemplate">'+updateDropDownActivityCreation("activityCreationWorkflowTemplate")+'</td></tr>'+
		'</tbody></table></div></div>'+	
	
	'<div class="col-md-6"><div class="table-scrollable"><table class="table table-striped table-hover" id="statusTable">'+	
	'<tbody>'+	
	'<tr><td>Life Cycle Stage : </td><td id="activityCreationLifeCycleStage">'+updateDropDownActivityCreation("activityCreationLifeCycleStage")+'</td></tr>'+
	'<tr><td>Planned End Date :	</td><td><span><input class="form-control input-small date-picker" id="activityCreationPlannedEndDate" size="10" type="text" value="" readonly="readonly" style="cursor: default; background-color: rgb(255, 255, 255);"></span></td></tr>'+
	'<tr><td>Reviewer :	</td><td id="activityCreationReviewer">'+updateDropDownActivityCreation("activityCreationReviewer")+'</td></tr>'+
	'<tr><td>Priority :	</td><td id="activityCreationPriority">'+updateDropDownActivityCreation("activityCreationPriority")+'</td></tr>'+
	'<tr><td>Tracker Number : </td><td><input id="activityCreationTrackerNumber" type="text" class="form-control" placeholder="Tracker Number"></td></tr>'+
	'<tr><td>Remarks : </td><td><textArea id="activityCreationRemarks" contenteditable="true" style="margin: 0px; width: 300px; height: 100px;"></textArea></td></tr>'+
	'<tr><td>Actual Activity Size : </td><td><input id="activityCreationActualActivitySize" type="text" class="form-control" placeholder="Actual Activity Size"></td></tr>'+
	'<tr><td>Planned Effort : </td><td><input id="activityCreationPlannedEffort" type="text" class="form-control" placeholder="Planned Effort"></td></tr>'+
	'</tbody></table></div></div></div>'+	

	'<div class="row">'+
	'<div class="table-scrollable"><table class="table table-striped table-hover" id="auditTable">'+
	'<thead><tr height="30"><th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Additional Fields </th><th></th></tr></thead>'+
	'</table></div><div id="activityCustomFieldsContainer"></div></div>'+
	
	'<div class="row"><div id="savebutton"><div class="col-md-12"><div class="form-group">'+
	'<div class="col-md-10"></div><div class="col-md-2" style=""><button type="button" id="cancel" class="btn gray" onclick="activiyCreationCancelHandler()">Cancel</button>'+
	'<button type="button" id="save" class="btn green-haze" onclick="activiyCreationSubmitHandler()">Submit</button></div></div></div></div></div></div>';
		
	return activityText;
}

function activiyCreationCancelHandler(){
	$("#activityCreationContainer").modal('hide');
	customFieldActivityCreationFlag=false;
}

function activiyCreationSubmitHandler(){
	/*console.log("activityWorkPackageId :"+"TBD");
	console.log("activityId :"+"TBD");
	console.log("engagementName :"+"TBD");
	console.log("productName :"+"TBD");
	console.log("activityWorkPackageName :"+"TBD");
	console.log("productFeatureId :"+"TBD");*/	
	
	var activityName = $("#activityCreationName").val();
	//console.log("activityName :"+activityName);
	
	var requirementValue = $("#activityCreationRequirement_options").find('option:selected').val();
	//console.log("productFeatureId : "+requirementValue);	
	
	var plannedStartDate = $("#activityCreationPlannedStartDate").val();
	//console.log("plannedStartDate : "+plannedStartDate);
	
	var plannedEndDate = $("#activityCreationPlannedEndDate").val();
	//console.log("plannedEndDate : "+plannedEndDate);
	
	var assigneeValue = $("#activityCreationAssignee_options").find('option:selected').val();
	//console.log("assigneeId : "+assigneeValue);
	
	//var botValue = $("#activityCreationBOT_options").find('option:selected').val();
	//console.log("autoAllocateReferenceId : "+botValue);
	
	var complexityValue = $("#activityCreationComplexity_options").find('option:selected').val();
	//console.log("complexity : "+complexityValue);	
	
	var categoryValue = $("#activityCreationCategory_options").find('option:selected').val();
	//console.log("categoryId : "+categoryValue);
		
	var perCentageCompletion = $("#activityCreationPercentageCompletion").val();
	//console.log("percentageCompletion :"+$("#activityCreationPercentageCompletion").val());
		
	var activityType = $("#activityCreationType_options").find('option:selected').val();
	//console.log("activityMasterId : "+activityType);
	
	var workflowTemplate = $("#activityCreationWorkflowTemplate_options").find('option:selected').val();
	//console.log("workflowId : "+workflowTemplate);
	
	var lifeCycleStage = $("#activityCreationLifeCycleStage_options").find('option:selected').val();
	//console.log("lifeCycleStageId : "+lifeCycleStage);
	
	var reviewerValue = $("#activityCreationReviewer_options").find('option:selected').val();
	//console.log("reviewerId : "+reviewerValue);
	
	var priorityValue = $("#activityCreationPriority_options").find('option:selected').val();
	//console.log("priorityId : "+priorityValue);
		
	var trackerNumber = $("#activityCreationTrackerNumber").val();
	//console.log("activityTrackerNumber :"+$("#activityCreationTrackerNumber").val());
		
	var remarks = $("#activityCreationRemarks").val();
	//console.log("remark :"+$("#activityCreationRemarks").val());
		
	var plannedSize = $("#activityCreationPlannedSize").val();
	//console.log("plannedActivitySize :"+$("#activityCreationPlannedSize").val());
	
	var actualSize = $("#activityCreationActualActivitySize").val();
	//console.log("actualActivitySize :"+$("#activityCreationActualActivitySize").val());
		
	var plannedEffort = $("#activityCreationPlannedEffort").val();
	//console.log("plannedEffort :"+$("#activityCreationPlannedEffort").val());
	
	 var value='';
	 var formdata = new FormData();
	 formdata.append("activityWorkPackageId", document.getElementById("treeHdnCurrentActivityWorkPackageId").value);
	 formdata.append("activityId", value);
	 formdata.append("activityName", activityName);
	 formdata.append("engagementName", value);
	 formdata.append("productName", value);
	 formdata.append("activityWorkPackageName", value);
	 formdata.append("productFeatureId", requirementValue); // requirement
	 formdata.append("activityMasterId", activityType);     // activity Type
	 formdata.append("lifeCycleStageId", lifeCycleStage);
	 formdata.append("plannedStartDate", plannedStartDate);
	 formdata.append("plannedEndDate", plannedEndDate);
	 formdata.append("assigneeId", assigneeValue);
	 formdata.append("reviewerId", reviewerValue);
	 formdata.append("priorityId", priorityValue);
	 formdata.append("complexity", complexityValue); 
	 formdata.append("activityTrackerNumber", trackerNumber);
	 formdata.append("categoryId", categoryValue);
	 formdata.append("remark", remarks);
	 formdata.append("plannedActivitySize", plannedSize);
	 formdata.append("actualActivitySize", actualSize);
	 formdata.append("percentageCompletion", perCentageCompletion);
	 formdata.append("plannedEffort", plannedEffort);
	 formdata.append("workflowId", workflowTemplate);
	 //formdata.append("autoAllocateReferenceId", botValue);
	 formdata.append("isActive", 1);	 
	 formdata.append("workflowRAG", value);
	 formdata.append("actualStartDate", value);
	 formdata.append("actualEndDate", value);
	 formdata.append("isModified", value);
	 formdata.append("statusDisplayName", value);
	 formdata.append("actors", value);
	 formdata.append("completedBy", value);
	 formdata.append("remainingHrsMins", value);
	 formdata.append("isImmidiateAutoAllocation", value);
	 formdata.append("drReferenceNumber", value);
	 formdata.append("workflowIndicator", value);
	 formdata.append("totalEffort", value);
	 formdata.append("latestComment", value);
	 formdata.append("workflowStatusType", value);
	 
	 // ---
	 
	 $("#customFieldSingleFrequencyContainer").html('');
	 var customFieldName='';
	 var customFieldStr='';
     var fieldValue='';
	 for(var i=0;i<customFieldResultObjCollection.length;i++){    	
    	customFieldStr = ((customFieldResultObjCollection[i]['fieldName']).replace(" ", ""));
    	customFieldName = customFieldResultObjCollection[i]['id']+'-'+customFieldStr.replace(/ +/g, "");
    	customFieldResultObjCollection[i]['activityMasterId'] = customFieldResultObjCollection[i]['entityTypeId'];
    	customFieldResultObjCollection[i]['customFieldId'] = customFieldResultObjCollection[i]['id'];
    	//console.log("id : "+customFieldName);
    	// ---- for various control type -----
    	if(document.getElementById(customFieldName) != null){
	    	if(customFieldResultObjCollection[i]['controlType'] == "Text Box"){
	    		fieldValue =  document.getElementById(customFieldName).value;    		
	    	
	    	}else if(customFieldResultObjCollection[i]['controlType'] == "Drop Down"){
	    		fieldValue =  document.getElementById(customFieldName).find('option:selected').val();
	    		
	    	}else if(customFieldResultObjCollection[i]['controlType'] == "Radio Button"){
	    		var inputV =  document.getElementById(customFieldName);
	    		fieldValue = $(inputV).find("input[type=radio]:checked").val();
	    		
	    	}else {
	    		fieldValue='';
	    	}	    	
	    	customFieldResultObjCollection[i]['fieldValue'] = fieldValue;
    	}
    	
    	//console.log("CustomField in Editor: "+customFieldName+" value : "+fieldValue);
    	formdata.append(customFieldName, fieldValue);    	    	
    }
     var singleCustomFieldObj = {'customFields' : customFieldResultObjCollection};
     var singleJsonString = JSON.stringify(singleCustomFieldObj.customFields);
     formdata.append("customFields", singleJsonString); 
	 //console.log("formdata : "+singleJsonString);
     $("#activityCreationLoaderIcon").show();
     
     var url='process.activity.add'
		 return $.Deferred(function($dfd){
	     $.ajax({
			    url:url,
			    method: 'POST',
			    contentType: false,
			    data: formdata,
			    dataType:'json',
			    processData: false,
			    success: function (data) {		    
			    	if(data.Result == "INFORMATION" || data.Result == "ERROR"){
			    		callAlert(data.Message);
			    		return false;
			    	}else if(data.Result == "OK"){
			    	 	//callAlert(data.Message);
			    	 	
			    	 	// -- performace enhancement TBD
			    	 	var activityWPId=0;
			    		var enableAddOrNot="yes";			    		
			    		var dataTableToLoad='jTableContainerWPActivities_dataTable';
			    		if(jTableContainerDataTable == "assigneeActivitiesContainer"){
			    			dataTableToLoad = jTableContainerDataTable;
			    		}			    					    		
			    		listActivitiesOfSelectedAWP_DT(0,0,0,activityWPId,1,enableAddOrNot, dataTableToLoad);
			    	}
			    	activiyCreationCancelHandler();		
			    	 $("#activityCreationLoaderIcon").hide();
				},
                error: function () {
                    $dfd.reject();
                    activiyCreationCancelHandler();
                    $("#activityCreationLoaderIcon").hide();
                }
		});
	 });
}

function createActivityHandlder(data, dropdownResult){
	$("#activityContainer").html('');
	$("#activityContainer").html(createAcitivityView());
	
	// ----- initiliase values -----
	$("#activityCreationPlannedSize").val(1);
	$("#activityCreationActualActivitySize").val(1);
	$("#activityCreationPlannedEffort").val(1);
	
	ComponentsPickers.init();
	
	customFieldGroupingsJsonObj = activityCreationJsonObj(0);	
	updateActivityCreationElements(dropdownResult);
	
	var selectedActivityTypeID = $("#activityCreationType_options").find('option:selected').val();
	if(selectedActivityTypeID != undefined && selectedActivityTypeID != '' && dropdownResult.length>0){		
		updateActivityWorkfow(selectedActivityTypeID);
	}
	customFieldActivityCreationFlag=true;
	$("#activityCreationContainer").modal();
}

function updateActivityCreationElements(dropdownResult){

	$("#activityCreationName").text('');
	$("#activityCreationPlannedSize").text('');
	$("#activityCreationCompletion").text('');
	$("#activityCreationRemarks").text('');
	$("#activityCreationActualActivitySize").text('');
	$("#activityCreationPlannedEffort").text('');
		
	updateDropDownActivityCreation("activityCreationRequirement", dropdownResult[1]);
	updateDropDownActivityCreation("activityCreationAssignee", dropdownResult[2]);
	//updateDropDownActivityCreation("activityCreationBOT", dropdownResult);
	updateDropDownActivityCreation("activityCreationComplexity", dropdownResult[4]);
	updateDropDownActivityCreation("activityCreationCategory", dropdownResult[5]);
	updateDropDownActivityCreation("activityCreationType", dropdownResult[0]);
	updateDropDownActivityCreation("activityCreationReviewer", dropdownResult[2]);
	updateDropDownActivityCreation("activityCreationPriority", dropdownResult[3]);
	updateDropDownActivityCreation("activityCreationWorkflowTemplate", dropdownResult[6]);
	updateDropDownActivityCreation("activityCreationLifeCycleStage", dropdownResult[7]);
	   
	// ----- Date picker -----
	$("#activityCreationPlannedStartDate").datepicker('setDate', 'today');
	$("#activityCreationPlannedEndDate").datepicker('setDate', 'today');	
}

function updateDropDownActivityCreation(idValue, dropdownResult){
	var activityCreationOption='';
	var arrayOptions=[];
	var value='';
		
		var initialValue='';
		if(dropdownResult != undefined && dropdownResult.length>0){
			initialValue = dropdownResult[0]['DisplayText'];
		}else{
			dropdownResult=[];
		}
				
		if(idValue == "activityCreationRequirement"){
			arrayOptions=dropdownResult;
			value = initialValue;
			
		}else if(idValue == "activityCreationType"){
			arrayOptions=dropdownResult;
			value =	initialValue;						
			
		}else if(idValue == "activityCreationCategory"){
			arrayOptions=dropdownResult;
			value = initialValue;
			
		}else if(idValue == "activityCreationLifeCycleStage"){
			arrayOptions=dropdownResult;
			value = initialValue;
			
		}else if(idValue == "activityCreationComplexity"){
			arrayOptions=dropdownResult;
			value = initialValue;
			
		}else if(idValue == "activityCreationAssignee"){
			arrayOptions=dropdownResult;
			
			var indexStr='';
			var indexStrFlag=false;
			for(var k=0;k<dropdownResult.length;k++){
				indexStr = dropdownResult[k]['Value'];				
				if(indexStr == loginUserId){
					value = dropdownResult[k]['DisplayText'];
					indexStrFlag=true;
					break;
				}
			}			
			if(!indexStrFlag)
				value = initialValue;
			
		}else if(idValue == "activityCreationReviewer"){
			arrayOptions=dropdownResult;
			var indexStr='';
			var indexStrFlag=false;
			for(var k=0;k<dropdownResult.length;k++){
				indexStr = dropdownResult[k]['Value'];				
				if(indexStr == loginUserId){
					value = dropdownResult[k]['DisplayText'];
					indexStrFlag=true;
					break;
				}
			}			
			if(!indexStrFlag)
				value = initialValue;
			
		}else if(idValue == "activityCreationPriority"){
			arrayOptions=dropdownResult;
			value = initialValue;
			
		}else if(idValue == "activityCreationWorkflowTemplate"){
			arrayOptions=dropdownResult;			
			value =	initialValue;	
		}
		
		var resultType='<span><select id="'+idValue+'_options" onchange="updateActivityDetailsInCreation(event)">';
		if(arrayOptions.length>0){
			$.each(arrayOptions, function(index, element) {
				activityCreationOption="";
			    if(value == element.DisplayText){
					activityCreationOption = "selected";
				}			
				
				resultType += '<option '+activityCreationOption+' value="'+element.Value +'">'+ element.DisplayText + '</option>'; 
			});		
		}else{
			resultType += '<option value="--">--</option>';
		}
		resultType += '</select></span>';
		
		$("#"+idValue).html('');
		$("#"+idValue).html(resultType);	
}

function updateActivityDetailsInCreation(event){
	var elementId = event.target.id;
	
	var selectedActivityTypeID = $("#activityCreationType_options").find('option:selected').val();
	if(selectedActivityTypeID != undefined && elementId == 'activityCreationType_options'){
		updateActivityWorkfow(selectedActivityTypeID);
	}		
}

function updateActivityWorkfow(value){	
	var url = 'workflow.master.mapped.to.entity.list.options?productId='+activityManagementTab_productId+'&entityTypeId='+entityTypeId+'&entityId='+value;
	$.ajax( {
		url: url,
		type: "POST",
		dataType: 'json',
		success: function ( json ) {		    	        	
			
			for(var i=0;i<json.Options.length;i++){
				json.Options[i].label=json.Options[i].DisplayText;
				json.Options[i].value=json.Options[i].Value;
			}						
			updateDropDownActivityCreation("activityCreationWorkflowTemplate", json.Options);
		}
	});
	
	// ----- update custom fields -----	
	customFieldGroupingsJsonObj = activityCreationJsonObj(value);	
	
	var container = '<div id="'+customFieldGroupingsJsonObj.singleFrequency+'"></div>';
	$("#activityCustomFieldsContainer").html('');
	$("#activityCustomFieldsContainer").append(container);	
	getSingleFrequencyCustomFields(customFieldGroupingsJsonObj.entityId, customFieldGroupingsJsonObj.entityTypeId, customFieldGroupingsJsonObj.entityInstanceId, customFieldGroupingsJsonObj.engagementId, customFieldGroupingsJsonObj.productId);

}

function activityCreationJsonObj(value){
	var jsonObj={"Title":"",
			"subTitle": "",
			"url": "data/data.json",
			"columnGrouping":2,
			"containerSize": "large",
			"componentUsageTitle":"customFields",
			"entityId": 28, // Activity entity id
			"entityTypeId":value,
			"entityInstanceId":0,
			"parentEntityId": 18,   // Product entity id
			"parentEntityInstanceId": activityManagementTab_productId,
			"engagementId":engagementId,
			"productId":activityManagementTab_productId,
			"singleFrequency":"customFieldActivitySingleFrequencyContainer",
			"multiFrequency":"customFieldActivityMultiFrequencyContainer",
	};
	
	return jsonObj;
}

