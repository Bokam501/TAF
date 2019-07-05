var plannedActivitySize=0;
var activitySummaryFlag=false;
function updateActivityDetailsInSummary(event){	   	   
		var modifiedField='';
		var modifiedValue='';
		var modifiedFieldTitle='';		
		var modifiedFieldValue='';		
		var modifiedFieldID='';
		
		var oldFieldValue='';
		
		var resultActivitySingleObj ='';
		var changedFlag=false;
		var elementId = event.target.id;
		console.log("elementId :"+elementId);		
		
		var itemName = jsonActivityUpdateData;
	    resultActivitySingleObj = itemName;
			    				
		if(elementId == 'myActivityName'){
			modifiedField = 'activityName';			
			oldFieldValue = jsonActivityUpdateData[modifiedField];
			
			modifiedValue = $('#myActivityName').text();		
			modifiedFieldTitle = "Activity Name";
			modifiedFieldValue = modifiedValue;				
			resultActivitySingleObj[modifiedField] = modifiedValue;
			
		}else if(elementId == 'spplannedActivitySize'){
			modifiedField = 'plannedActivitySize';
			oldFieldValue = jsonActivityUpdateData[modifiedField];
			
			modifiedValue = $('#spplannedActivitySize').text();
			modifiedFieldTitle = plannedActivitySizeHeaderTitle;
			modifiedFieldValue = modifiedValue;				
			 resultActivitySingleObj[modifiedField] = modifiedValue;
			
		}else if(elementId == 'spactualActivitySize'){
			modifiedField = 'actualActivitySize';
			modifiedValue = $('#spactualActivitySize').text();
			oldFieldValue = jsonActivityUpdateData[modifiedField];
			
			modifiedFieldTitle = actualActivitySizeHeaderTitle;
			modifiedFieldValue = modifiedValue;
			resultActivitySingleObj[modifiedField] = modifiedValue;
			
		}else if(elementId == 'spActualUnit'){
			modifiedField = 'actualEffort';
			modifiedValue = $('#spActualUnit').text();
			oldFieldValue = jsonActivityUpdateData[modifiedField];
			
			modifiedFieldTitle = "Actual Effort";
			modifiedFieldValue = modifiedValue;
			resultActivitySingleObj[modifiedField] = modifiedValue;
			
		}else if(elementId == 'spRemark'){
			modifiedField = 'remark';
			modifiedValue = $('#spRemark').val();
			modifiedFieldTitle = "Remarks";
			modifiedFieldValue = modifiedValue;
		
		}else if(elementId == 'spPercentageCompletion'){
			modifiedField = 'percentageCompletion';
			modifiedValue = $('#spPercentageCompletion').text();			
			oldFieldValue = jsonActivityUpdateData[modifiedField];
			
			modifiedFieldTitle = "% Completion";
			modifiedFieldValue = modifiedValue;	
			resultActivitySingleObj[modifiedField] = modifiedValue;
		
		}else if(elementId == 'spAssignee_options'){ // -- dropdown values
			modifiedField = 'assigneeId';
			oldFieldID = jsonActivityUpdateData[modifiedField];
			oldFieldValue = jsonActivityUpdateData[modifiedField];
			
			modifiedValue = $("#spAssignee_options").find('option:selected').val();
			modifiedFieldID = modifiedValue;
			modifiedFieldValue = modifiedValue;
			jsonActivityUpdateData[modifiedField] = modifiedFieldID; 
			modifiedFieldTitle = "Assignee";
			changedFlag=true;
		
		}else if(elementId == 'spReviewer_options'){
			modifiedField = 'reviewerId';
			oldFieldID = jsonActivityUpdateData[modifiedField];
			oldFieldValue = jsonActivityUpdateData[modifiedField];
			
			modifiedValue = $("#spReviewer_options").find('option:selected').val();
			modifiedFieldID = modifiedValue;
			modifiedFieldValue = modifiedValue;
			jsonActivityUpdateData[modifiedField] = modifiedFieldID; 
			modifiedFieldTitle = "Reviewer";
			changedFlag=true;
		
		}else if(elementId == 'myActivityType_options'){
			modifiedField = 'activityMasterId';
			oldFieldID = jsonActivityUpdateData[modifiedField];
			oldFieldValue = jsonActivityUpdateData[modifiedField];
			
			modifiedValue = $("#myActivityType_options").find('option:selected').val();
			modifiedFieldID = modifiedValue;
			modifiedFieldValue = modifiedValue;
			jsonActivityUpdateData[modifiedField] = modifiedFieldID; 
			modifiedFieldTitle = "Activity Type";
			changedFlag=true;
			
			var resultArr = [jsonActivityUpdateData];
			updateWorkflowTemplateOnActivityTypeChange(jsonActivityUpdateData.productId, modifiedFieldID, resultArr);
			
		}else if(elementId == 'productFeatureId_options'){
			modifiedField = 'productFeatureId';
			oldFieldID = jsonActivityUpdateData[modifiedField];
			oldFieldValue = jsonActivityUpdateData[modifiedField];
			
			modifiedValue = $("#productFeatureId_options").find('option:selected').val();
			modifiedFieldID = modifiedValue;
			modifiedFieldValue = modifiedValue;
			jsonActivityUpdateData[modifiedField] = modifiedFieldID; 
			modifiedFieldTitle = "Product Feature";
			changedFlag=true;
			
		}else if(elementId == 'spPriority_options'){
			modifiedField = 'priorityId';
			oldFieldID = jsonActivityUpdateData[modifiedField];
			oldFieldValue = jsonActivityUpdateData[modifiedField];
			
			modifiedValue = $("#spPriority_options").find('option:selected').val();
			modifiedFieldID = modifiedValue;
			modifiedFieldValue = modifiedValue;
			jsonActivityUpdateData[modifiedField] = modifiedFieldID; 
			modifiedFieldTitle = "Priority";
			changedFlag=true;
		
		}else if(elementId == 'spCategoryName_options'){
			modifiedField = 'categoryId';
			oldFieldID = jsonActivityUpdateData[modifiedField];
			oldFieldValue = jsonActivityUpdateData[modifiedField];
			
			modifiedValue = $("#spCategoryName_options").find('option:selected').val();
			modifiedFieldID = modifiedValue;
			modifiedFieldValue = modifiedValue;
			jsonActivityUpdateData[modifiedField] = modifiedFieldID; 
			modifiedFieldTitle = "Category";
			changedFlag=true;
		
		}else if(elementId == 'spComplexity_options'){
			modifiedField = 'complexity';
			oldFieldID = jsonActivityUpdateData[modifiedField];
			oldFieldValue = jsonActivityUpdateData[modifiedField];
			
			modifiedValue = $("#spComplexity_options").find('option:selected').val();
			modifiedFieldID = modifiedValue;
			modifiedFieldValue = modifiedValue;
			jsonActivityUpdateData[modifiedField] = modifiedFieldID; 
			modifiedFieldTitle = "Complexity";
			changedFlag=true;
		
		}else if(elementId == 'spLifeCycleStatus_options'){
			modifiedField = 'lifeCycleStageId';
			oldFieldID = jsonActivityUpdateData[modifiedField];
			oldFieldValue = jsonActivityUpdateData[modifiedField];
			
			modifiedValue = $("#spLifeCycleStatus_options").find('option:selected').val();
			modifiedFieldID = modifiedValue;
			modifiedFieldValue = modifiedValue;
			jsonActivityUpdateData[modifiedField] = modifiedFieldID; 
			modifiedFieldTitle = "Life Cycle Stage";
			changedFlag=true;
		
		}else if(elementId == 'spworkflowId_options'){
			modifiedField = 'workflowId';
			oldFieldID = jsonActivityUpdateData[modifiedField];
			oldFieldValue = jsonActivityUpdateData[modifiedField];
			
			modifiedValue = $("#spworkflowId_options").find('option:selected').val();
			modifiedFieldID = modifiedValue;
			modifiedFieldValue = modifiedValue;
			jsonActivityUpdateData[modifiedField] = modifiedFieldID; 
			modifiedFieldTitle = "Workflow Template";
			changedFlag=false;		
			
			var resultArr = [jsonActivityUpdateData];
			updateWorkflowTemplateOnActivityTypeChange(jsonActivityUpdateData.productId, modifiedFieldID, resultArr);
			
			//var entityTypeId = 33;			
			//singleJtableUpdate(jsonActivityUpdateData.productId, entityTypeId, jsonActivityUpdateData.activityMasterId, jsonActivityUpdateData.activityId, modifiedValue);
			return true;
		
		}else if(elementId == 'activityPlannedStartDate'){
			oldFieldValue = jsonActivityUpdateData.plannedStartDate;
			modifiedField = 'plannedStartDate';
			modifiedFieldTitle = "Planned Start Date";
			modifiedValue = document.getElementById("activityPlannedStartDate").value;
			modifiedFieldValue = modifiedValue;
			jsonActivityUpdateData[modifiedField] = modifiedValue;
			
			modifiedFieldID = modifiedField;			
			changedFlag=true;
		
		}else if(elementId == 'activityPlannedEndDate'){			
			oldFieldValue = jsonActivityUpdateData.plannedEndDate;			
			modifiedField = 'plannedEndDate';
			modifiedFieldTitle = "Planned End Date";
			modifiedValue = document.getElementById("activityPlannedEndDate").value;
			modifiedFieldValue = modifiedValue;
			jsonActivityUpdateData[modifiedField] = modifiedValue;
			
			modifiedFieldID = modifiedField;			
			changedFlag=true;
		
		}else if(elementId == 'activityActualStartDate'){
			oldFieldValue = jsonActivityUpdateData.actualStartDate;
			modifiedField = 'actualStartDate';
			modifiedFieldTitle = "Actual Start Date";
			modifiedValue = document.getElementById("activityActualStartDate").value;
			modifiedFieldValue = modifiedValue;
			jsonActivityUpdateData[modifiedField] = modifiedValue;
			
			modifiedFieldID = modifiedField;			
			changedFlag=true;
		
		}else if(elementId == 'activityActualEndDate'){
			oldFieldValue = jsonActivityUpdateData.actualEndDate;
			modifiedField = 'actualEndDate';
			modifiedFieldTitle = "Actual End Date";
			modifiedValue = document.getElementById("activityActualEndDate").value;
			modifiedFieldValue = modifiedValue;
			jsonActivityUpdateData[modifiedField] = modifiedValue;
			
			modifiedFieldID = modifiedField;			
			changedFlag=true;
		}
		
		oldFieldID = jsonActivityUpdateData[modifiedField];
		
		if($('#myActivityName').text() != oldFieldValue || 
			$('#spRemark').val()!= oldFieldValue ||
			$('#spplannedActivitySize').text()!= oldFieldValue || 
			$('#spactualActivitySize').text()!= oldFieldValue ||			
			$('#spPercentageCompletion').text()!= oldFieldValue || changedFlag){
		    activitySummaryFlag=true;
			
			resultActivitySingleObj['oldFieldID'] = oldFieldID;
			resultActivitySingleObj['oldFieldValue'] = oldFieldValue;
			resultActivitySingleObj['modifiedFieldID'] = modifiedFieldID;
			resultActivitySingleObj['modifiedField'] = modifiedField;
			resultActivitySingleObj['modifiedFieldValue'] = modifiedFieldValue;
			resultActivitySingleObj['modifiedFieldTitle'] = modifiedFieldTitle;				
						
			updataActivityHandler(resultActivitySingleObj, itemName.activityId);
	  }		
}

function updateWorkflowTemplateOnActivityTypeChange(productId, entityId, result){
	var url = 'workflow.master.mapped.to.entity.list.options?productId='+productId+'&entityTypeId=33&entityId='+entityId;
	$.ajax( {
		url: url,
		type: "POST",
		dataType: 'json',
		success: function ( json ) {			    	        	
			
			for(var i=0;i<json.Options.length;i++){
				json.Options[i].label=json.Options[i].DisplayText;
				json.Options[i].value=json.Options[i].Value;
			}		
			updateDropDownActivitySummary("spworkflowIdWorkflow", result, json.Options);
		}
	} );
}

function updateDropDownActivitySummary(idValue, data, workflowArr){
	var activitySummaryOption='';
	var arrayOptions=[];
	var value='';
	
	if(data != undefined){
		var result=data[0];
				
		if(idValue == "myActivityType"){
			arrayOptions=activitySummaryTabResultArr[0];
			value = result.activityMasterName;
			
		}else if(idValue == "productFeatureId"){
			arrayOptions=activitySummaryTabResultArr[1];
			value =	optionDisplayTextHandler(activitySummaryTabResultArr[1], result.productFeatureId);
			
		}else if(idValue == "spCategoryName"){
			arrayOptions=activitySummaryTabResultArr[5];
			value = result.categoryName;
			
		}else if(idValue == "spPriority"){
			arrayOptions=activitySummaryTabResultArr[3];
			value = result.priorityName;
			
		}else if(idValue == "spComplexity"){
			arrayOptions=activitySummaryTabResultArr[4];
			value = result.complexity;
			
		}else if(idValue == "spAssignee"){
			arrayOptions=activitySummaryTabResultArr[2];
			value = result.assigneeName;
			
		}else if(idValue == "spReviewer"){
			arrayOptions=activitySummaryTabResultArr[2];
			value = result.reviewerName;
			
		}else if(idValue == "spLifeCycleStatus"){
			arrayOptions=activitySummaryTabResultArr[7];
			value = result.lifeCycleStageName;
			
		}else if(idValue == "spworkflowId"){
			arrayOptions=activitySummaryTabResultArr[6];			
			value =	optionDisplayTextHandler(arrayOptions, result.workflowId);
		
		}else if(idValue == "spworkflowIdWorkflow"){
			idValue = "spworkflowId";
			arrayOptions= workflowArr;			
			value =	optionDisplayTextHandler(arrayOptions, result.workflowId);
			
		}
		
		var resultType='<span><select id="'+idValue+'_options" onchange="updateActivityDetailsInSummary(event)">';
		if(arrayOptions.length>0){
			$.each(arrayOptions, function(index, element) {
				activitySummaryOption="";
			    if(value == element.DisplayText){
					activitySummaryOption = "selected";
				}			
				
				resultType += '<option '+activitySummaryOption+' value="'+element.Value +'">'+ element.DisplayText + '</option>'; 
			});		
		}else{
			resultType += '<option value="--">--</option>';
		}
		resultType += '</select></span>';
		
		$("#"+idValue).html('');
		$("#"+idValue).html(resultType);
	}
}

function updateActivityDTOnClose(){
	customFieldActivityCreationFlag=true;
	if(activitySummaryFlag){
		activitySummaryFlag=false;
		var enableAddOrNot="yes";	
		
		if(jTableContainerDataTable == "activitiesWP_dataTable"){
			listActivitiesOfSelectedAWP_DT(0,0,0,0,1,enableAddOrNot, jTableContainerDataTable);
			
		}else if(jTableContainerDataTable == "jTableContainerWPActivities_dataTable"){
			listActivitiesOfSelectedAWP_DT(0,0,0,activityWPId,1,enableAddOrNot, jTableContainerDataTable);
			
		}else if(jTableContainerDataTable == "productActivities_dataTable"){
			document.getElementById("treeHdnCurrentProductId").value = productId;
			listActivitiesOfSelectedAWP_DT(0,0,0,0,1,enableAddOrNot, jTableContainerDataTable);
		}		
	}
}

var jsonActivityUpdateData={};
function updateOnActivityList(activityId){
	var getActivityLength = 0;	
	if(activityManagementTab_oTable != ''){
		getActivityLength = activityManagementTab_oTable.fnGetData().length;
		
		for(var i=0;i<getActivityLength;i++){
			if(activityId == activityManagementTab_oTable.fnGetData(i).activityId){
				jsonActivityUpdateData = activityManagementTab_oTable.fnGetData(i);						
				break;
			}
		}
	}
	
	return jsonActivityUpdateData;
}

function showMyActivitySummaryDetails(activityId){
	var urlToGetMyActivitySummaryOfSelectedActivityId = 'my.Activity.status.summary?activityId='+activityId;
	openLoaderIcon();
	$("#activitySummaryLoaderIcon").show();
	
	$.ajax({
		type:"POST",
	 	contentType: "application/json; charset=utf-8",
		url : urlToGetMyActivitySummaryOfSelectedActivityId,
		dataType : 'json',
		cache:false,	
		success : function(data) {
			$("#activitySummaryLoaderIcon").hide();
			closeLoaderIcon();			
			var result=data.Records;
			result[0].baselineStartDate = convertDTDateFormatAdd(result[0].baselineStartDate, ["baselineStartDate"]);
			result[0].baselineEndDate = convertDTDateFormatAdd(result[0].baselineEndDate, ["baselineEndDate"]);
			result[0].plannedStartDate = convertDTDateFormatAdd(result[0].plannedEndDate, ["plannedStartDate"]);
			result[0].plannedEndDate = convertDTDateFormatAdd(result[0].plannedEndDate, ["plannedEndDate"]);
			result[0].actualStartDate = convertDTDateFormatAdd(result[0].actualStartDate, ["actualStartDate"]);
			result[0].actualEndDate = convertDTDateFormatAdd(result[0].actualEndDate, ["actualEndDate"]);
								
			activityManagementSummary_Container(result);
		},
		complete:function(data) {
			closeLoaderIcon();
			$("#activitySummaryLoaderIcon").hide();
		},
		error: function(data){
			closeLoaderIcon();
			$("#activitySummaryLoaderIcon").hide();
		}
	});
}

var activitySummaryOptionsArr=[];
var activitySummaryTabResultArr=[];
var activitySummaryOptionsItemCounter=0;

function activityManagementSummary_Container(dataValues){
	var data = dataValues[0];
	var productIdValue = data.productId;
	var engagementIdValue = data.engagementId;
	
	var entityTypeId = 34;
	var entityId = 0;
	var entityInstanceId = data.activityWorkPackageId;
	if(typeof entityInstanceId == 'undefined'){
		entityInstanceId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
	}
	
	var lifeCycleStageIdURL = 'workflow.life.cycle.stages.options?entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId;
	
	// ------		
	var entityId = data.activityMasterId;
	if(typeof entityId == 'undefined' || entityId == null){
		entityId = 0;
	}
	var workflowURL = 'workflow.master.mapped.to.entity.list.options?productId='+productIdValue+'&entityTypeId=33&entityId='+entityId;
	
	activitySummaryOptionsItemCounter=0;
	activitySummaryTabResultArr=[];
	
	activitySummaryOptionsArr = [{id:"activityMasterId", url:'process.list.activity.type.engagement?engagementId='+engagementIdValue+'&productId='+productIdValue},	               
	       {id:"productFeatureId", url:'list.features.by.enagment?engagementId='+engagementIdValue+'&productId='+productIdValue+'&activityId=-1&activityWorkPackageId='+entityInstanceId},	               
	       {id:"assigneeId", url:'common.user.list.by.resourcepool.id.productId?&productId='+productIdValue},
		   {id:"priorityId", url:'administration.executionPriorityList'},
		   {id:"complexity", url:'common.list.complexity'},
		   {id:"categoryId", url:'common.list.executiontypemaster.byentityid?entitymasterid=1'},					   	
		   {id:"workflowId", url:workflowURL},
		   {id:"lifeCycleStageId", url:lifeCycleStageIdURL},
		  /* {id:"autoAllocateReferenceId", url:'auto.allocation.bot.for.productId?&productId='+productIdValue+'&botType=Task Allocation Controller'},*/
		];
	returnOptionsItemForActivitySummary(activitySummaryOptionsArr[0].url, dataValues);
}

//----- Return options ----
function returnOptionsItemForActivitySummary(url, data){
	$.ajax( {
 	   "type": "POST",
        "url":  url,
        "dataType": "json",
         success: function (json){
	         if(json.Result == "ERROR" || json.Result == "Error" || json.Options == null){
	         	callAlert(json.Message);
	         	json.Options=[];
	         	activitySummaryTabResultArr.push(json.Options);	         	
	         	
	         	if(activitySummaryOptionsItemCounter<activitySummaryOptionsArr.length-1)
		      		 returnOptionsItemForActivitySummary(activitySummaryOptionsArr[activitySummaryTabResultArr.length].url, data);		      		 
	         		 activitySummaryOptionsItemCounter++;
	         	
	         }else{        	 
	         	if(json.Options.length>0){     		   
				   for(var i=0;i<json.Options.length;i++){
					   json.Options[i].label=json.Options[i].DisplayText;
					   json.Options[i].value=json.Options[i].Value;
				   }			   
	     	   	}else{
	     			  json.Options=[];
	     	   	}     	   
	     	   activitySummaryTabResultArr.push(json.Options);     	   
	     	  
	     	   if(activitySummaryOptionsItemCounter<activitySummaryOptionsArr.length-1){
	      		 returnOptionsItemForActivitySummary(activitySummaryOptionsArr[activitySummaryTabResultArr.length].url, data);     		  
	      	   }else{	      		 
	      		   activitiesJsonObj = {"Title":"Activities Details :- "+ data[0].activityId + "- ",
	      				   "activityId": data[0].activityId,
	      				   "activityWorkPackageId": data[0].activityWorkPackageId,
	      				   "activityTypeId": data[0].activityMasterId,
	      				   "productId": data[0].productId,
	      				   "testFactId":data[0].engagementId ,				 
	      				   "ActiviesDetailsTitle":"Activity Details",
	      				   "activityName" : data[0].activityName,	      			
	      		   };
         		 $("#ActivitySummary").html(activityTabSummaryHandler());      		 			
	      		 ComponentsPickers.init();	      		
	 			 updateActivitySummaryElements(data);
	 			
	 			// ----- initiliase values -----
	 			$("#activityCreationPlannedSize").val(1);
	 			$("#activityCreationActualActivitySize").val(1);
	 			$("#activityCreationPlannedEffort").val(1);
	 			
	 			//customFieldGroupingsJsonObj = activityCreationJsonObj(0);	
	 			customFieldGroupingsJsonObj = createCustomFields();
	 			updateActivityCreationElements(activityManagementTabResultArr);
	 			
	 			var selectedActivityTypeID = $("#myActivityType_options").find('option:selected').val();
	 			if(selectedActivityTypeID != undefined && selectedActivityTypeID != '' && activityManagementTabResultArr.length>0){		
	 				updateActivityWorkfowSumary(selectedActivityTypeID);
	 			}
	 			 // ----- end ----- 
	      	   }
	     	   activitySummaryOptionsItemCounter++;
	         }
         },
         error: function (data) {
        	 activitySummaryOptionsItemCounter++;
        	 
         },
         complete: function(data){
         	console.log('Completed');
         	
         },	            
   	});	
}

function updateActivityWorkfowSumary(value){	
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
			
			var container = '<div id="'+customFieldGroupingsJsonObj.singleFrequency+'"></div>';
			$("#customFieldsInActivitySummary").html('');
			$("#customFieldsInActivitySummary").append(container);	
			getSingleFrequencyCustomFields(customFieldGroupingsJsonObj.entityId, customFieldGroupingsJsonObj.entityTypeId, customFieldGroupingsJsonObj.entityInstanceId, customFieldGroupingsJsonObj.engagementId, customFieldGroupingsJsonObj.productId);
		}
	});
	
	// ----- update custom fields -----	
	//customFieldGroupingsJsonObj = createCustomFields();	
	

}

function updateActivitySummaryElements(result){
	
	var activityId = "";
	var activityName = "";	
	var activityWorkPackageName = "";
	var status = "";
	var trackerNumber="";
	var createdOn = "";
	var remark="";
	var envcombination="";			
	var lastModifiedDate = "";
	var plannedStartDate="";
	var totalNumberOfTasks = "";	
	var plannedEndDate="";
	var createdByName="";
	var modifiedByName="";
	var taskInProgressStatusRecords = "";
	var loggedInUserTaskCount ="";
	var actualStartDate="";
	var actualEndDate="";
	var plannedActivitySize="";
	var actualActivitySize="";
	var productName="";
	var engagementName="";
	var baselineStartDate="";
	var baselineEndDate ="";
	var percentageCompletion ="";
	var totalEffort="";
	var actors="";
	var currentStatus="";
	var plannedEffort="";			
	var baselineEffort="";
	var baselineActivitySize="";
	var weightage="";
	var baselineUnit="";
	var plannedUnit="";
	var actualUnit="";
	var changeRequestCount='';
	if(result.length === 0) {
		 $("#myActivityName").text("");
	} else {
		$.each(result, function(i,item){ 
			activityId = item.activityId;
			activityName = item.activityName;
			activityWorkPackageName = item.activityWorkPackageName;
			createdOn = item.createdDate;
			remark=item.remark;
			status=item.statusCategoryName;
			drReferenceNumber=item.drReferenceNumber;
			lastModifiedDate = item.modifiedDate;
			totalNumberOfTasks = item.totalTaskCount;	
			plannedStartDate=item.plannedStartDate;
			plannedEndDate=item.plannedEndDate;
			createdByName=item.createdByName;
			modifiedByName=item.modifiedByName;
			trackerNumber=item.activityTrackerNumber;
			loggedInUserTaskCount=item.loggedInUserTaskCount;
			taskInProgressStatusRecords=item.taskInProgressStatusRecords;
			envcombination=item.enviCombination;
			clarificationCount=item.clarificationCount;
			actualStartDate=item.actualStartDate;
			actualEndDate=item.actualEndDate;
			plannedActivitySize=item.plannedActivitySize;
			actualActivitySize=item.actualActivitySize;
			productName=item.productName;
			engagementName=item.engagementName;
			baselineStartDate=item.baselineStartDate;
			baselineEndDate=item.baselineEndDate;
			percentageCompletion=item.percentageCompletion;
			totalEffort=item.totalEffort;
			actors=item.actors;
			currentStatus=item.statusName;	
			plannedEffort=item.plannedEffort;					
			baselineEffort=item.baselineEffort;
			baselineActivitySize=item.baselineActivitySize;
			weightage=item.weightage;
			baselineUnit=item.baselineUnit;
			plannedUnit=item.plannedUnit;
			actualUnit=item.actualUnit;
			changeRequestCount=item.changeRequestCount;
			
		});
	} 
	jsonActivityUpdateData = updateOnActivityList(activityId);
	
	$("#myActivityId").text(activityId);
	$("#myActivityName").text(activityName);
	$("#myActivityWorkpackage").text(activityWorkPackageName);
	$("#spStatus").text(status);
	
	updateDropDownActivitySummary("myActivityType", result);
	updateDropDownActivitySummary("productFeatureId", result);
	updateDropDownActivitySummary("spCategoryName", result);
	updateDropDownActivitySummary("spPriority", result);
	updateDropDownActivitySummary("spComplexity", result);
	updateDropDownActivitySummary("spAssignee", result);
	updateDropDownActivitySummary("spReviewer", result);
	updateDropDownActivitySummary("spLifeCycleStatus", result);
	updateDropDownActivitySummary("spworkflowId", result);
	
	$("#weightage").text(weightage);
	$("#spRemark").val(remark);				
	if(changeRequestCount == 0){
	$("#spChangeRequest").text("-");	
	}else{
		$("#spChangeRequest").text(changeRequestCount);
	}			
	if(totalNumberOfTasks == 0){
	$("#spTaskCount").text("-");	
	}else{
		$("#spTaskCount").text(totalNumberOfTasks);
	}
	
	if(envcombination != null && envcombination !='[]'){
		$("#spEnvcombination").text(envcombination);					
	}else{
		$("#spEnvcombination").text("-");
	}
	if(clarificationCount == 0){
	$("#spDrReferenceNumber").text("-");	
	}else{
		$("#spDrReferenceNumber").text(clarificationCount);
	}
	if(trackerNumber == 0){				
	$("#spTrackerNumber").text("-");
	}else{
		$("#spTrackerNumber").text(trackerNumber);
	}
	
	$("#spactualActivitySize").text(actualActivitySize);
	$("#spplannedActivitySize").text(plannedActivitySize);
	$("#spbaselineActivitySize").text(baselineActivitySize);
	$("#spCreatedBy").text(createdByName);
	$("#spCreatedOn").text(createdOn);
	$("#spModifiedBy").text(modifiedByName);
	$("#spModifiedOn").text(lastModifiedDate);			
	
	$("#spTotalTaskAssigned").text(loggedInUserTaskCount);
	$("#spTotalInprogressStatus").text(taskInProgressStatusRecords);
    
	// ----- Date picker -----
	
	var startDateArr=[];
	
	$("#activityPlannedStartDate").value = plannedStartDate;		
	startDateArr = plannedStartDate.split('/');
	plannedStartDate = new Date(startDateArr[2], startDateArr[0]-1, startDateArr[1]);
	$('#activityPlannedStartDate').datepicker({ dateFormat: 'yy-mm-dd' }); // format to show		
	$("#activityPlannedStartDate").datepicker('setDate', plannedStartDate);
	
	$("#activityPlannedEndDate").value = plannedEndDate;		
	startDateArr = plannedEndDate.split('/');
	plannedEndDate = new Date(startDateArr[2], startDateArr[0]-1, startDateArr[1]);
	$('#activityPlannedEndDate').datepicker({ dateFormat: 'yy-mm-dd' }); // format to show		
	$("#activityPlannedEndDate").datepicker('setDate', plannedEndDate);
	
	$("#activityActualStartDate").value = actualStartDate;		
	startDateArr = actualStartDate.split('/');
	actualStartDate = new Date(startDateArr[2], startDateArr[0]-1, startDateArr[1]);
	$('#activityActualStartDate').datepicker({ dateFormat: 'yy-mm-dd' }); // format to show		
	$("#activityActualStartDate").datepicker('setDate', actualStartDate);
	
	$("#activityActualEndDate").value = actualEndDate;		
	startDateArr = actualEndDate.split('/');
	actualEndDate = new Date(startDateArr[2], startDateArr[0]-1, startDateArr[1]);
	$('#activityActualEndDate').datepicker({ dateFormat: 'yy-mm-dd' }); // format to show
	$("#activityActualEndDate").datepicker('setDate', actualEndDate);
	
	
	$("#activityPlannedStartDate").on("changeDate",function(event) {
		activityDateHandler(event);		
	});
	
	$("#activityPlannedEndDate").on("changeDate",function(event) {
		activityDateHandler(event);		
	});	
	
	$("#activityActualStartDate").on("changeDate",function(event) {
		activityDateHandler(event);		
	});	
	
	$("#activityActualEndDate").on("changeDate",function(event) {
		activityDateHandler(event);		
	});	
	// ----- ended -----
				
	$("#engagementorproduct").text(engagementName+" / "+productName);
	$("#spBaselineStartDate").text(baselineStartDate);	
	$("#spBaselineEndDate").text(baselineEndDate);
	$("#spPendingWith").text(actors);
	$("#spCurrentStatus").text(currentStatus);			
	
	if(baselineUnit == null){
		$("#spBaselineUnit").text("-");	
	}else{
		$("#spBaselineUnit").text(baselineUnit);
	}
	
	if(plannedUnit == null){
		$("#spPlannedUnit").text("-");			
	}else{
		$("#spPlannedUnit").text(plannedUnit);
	}
	
	if(actualUnit == null){
		$("#spActualUnit").text("-");
	}else{			
		$("#spActualUnit").text(actualUnit);
	}			
	
	if(totalEffort == null){
		$("#spTotalEffort").text("-");
	}else{			
		$("#spTotalEffort").text(totalEffort);
	}
	
	if(plannedEffort == null){
		$("#spPlannedEffort").text("-");
	}else{			
		$("#spPlannedEffort").text(plannedEffort);
	}
	
	if(baselineEffort == null){
		$("#spBaselineEffort").text("-");
	}else{			
		$("#spBaselineEffort").text(baselineEffort);
	}
	
	if(percentageCompletion == null){
		$("#spPercentageCompletion").text("-");
	}else{				
		$("#spPercentageCompletion").text(percentageCompletion);
	}	
}

function activityDateHandler(event){	
	updateActivityDetailsInSummary(event);	
}

function ResetSearchDetail(){		
	$('#statusSearch_ul').select2('data', null);	
	$('#assigneeSearch_ul').select2('data', null);
	$('#prioritySearch_ul').select2('data', null);	
	var awpId =  document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
	var isActive = 1;			
	listActivitiesOfSelectedAWP(0,0,0,awpId,isActive,"yes",'#jTableContainerWPActivities');  
    return false;
}

function ActivitySearchInitloadList(){  	
	loadSearchStatusList();
	loadSearchAssigneeList();				
	loadSearchExecutionPriorityList();
}

function loadSearchStatusList(){
	$('#statusSearch_ul').empty();	
	$.post('status.category.option.list',function(data) {		
	    var ary = (data.Options);
	    $.each(ary, function(index, element) {
	        $('#statusSearch_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
		});
	});
}


function loadSearchAssigneeList(){
$('#assigneeSearch_ul').empty();	
$.post('common.user.list.by.resourcepool.id.productId?productId='+document.getElementById("treeHdnCurrentProductId").value,function(data) {	
    var ary = (data.Options);
    $.each(ary, function(index, element) {
        $('#assigneeSearch_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
		});
		});
} 

function loadSearchExecutionPriorityList(){
$('#prioritySearch_ul').empty();	
$.post('administration.executionPriorityList',function(data) {		
    var ary = (data.Options);
    $.each(ary, function(index, element) {
        $('#prioritySearch_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
		});
		});
} 

function SearchDetail(){	
	
	 var status=$("#searchStatus_dd .select2-chosen").text();
	 var assignee=$("#searchAssignee_dd .select2-chosen").text();		
	 var priority=  $("#searchPriorityList_dd .select2-chosen").text();		 
	var reviewfilter =-1;
	var statusId ="";
	var searchFlag =1;
	if((status!="-")&&(status!="")){
		statusId = $("#statusSearch_ul").find('option:selected').attr('id');
	}else{
		statusId=-1;
	}		
	
	 var assigneeId="";
	 if((assignee!="-")&&(assignee!="")){
		 assigneeId= $("#assigneeSearch_ul").find('option:selected').attr('id');
	 }else{
		 assigneeId=-1;
	 }
			
	var priorityId ="";
	if((priority!="-")&&(priority!="")){
		priorityId = $("#prioritySearch_ul").find('option:selected').attr('id');
	}else{
		priorityId=-1;
	}	
  
	 if(assigneeId==-1 && statusId==-1&& priorityId==-1){
			callAlert("Select atleast one Option","ok");
		}
		
		else{			
			$('#jTableContainer').jtable('load',
				{				
				searchassigneeId: assigneeId,
				searchstatusId: statusId,
				searchpriority:priorityId,
				searchFlag:searchFlag
			});		
		
		}	
}