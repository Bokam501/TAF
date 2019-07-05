var isLoad = false;			
var exceptedOutput='';
var listURL = '';
var attachmentTypeURL = '';
var workpackageTypeId = 34;
var activityWPselectedTab=0;
var activityWPJsonObj='';

var ActivityWorkPackageViewDetails = function() {
  
   var initialise = function(jsonObj){
	   listAWPDetailsComponent(jsonObj);
   };
   return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

$(document).ready(function(){
	$('#activityWPSummaryView').append($('#ACTWPSummaryPage'));	
	if(changeRequestToUsecase == "YES"){
		$('#ACTWPSummaryPage ul:first li:eq(4) a').text("UseCases");
		$('#trigUploadChangeRequestsActivity').val('Use Cases');
		$('#wpChangeRequestsImg').attr('title', 'Download Template - Use Cases');
	}
	
});

$(document).on("click",".daterangepicker .applyBtn", function(e) {
    var workdateFrom = $(this).siblings(".daterangepicker_start_input").find("input").val();
	var workdateTo = $(this).siblings(".daterangepicker_end_input").find("input").val();
	$("#hdnStartDateAWP").val(workdateFrom);
	$("#hdnEndDateAWP").val(workdateTo);
	updateModifiedValue($("#hdnFieldTypeAWP").val());
});

$(document).on("click",".date-range-toggle", function(e) {
	if($(this).closest("div").attr("id").indexOf("plan") > -1)
		$("#hdnFieldTypeAWP").val("plannedStartDate");
	else
		$("#hdnFieldTypeAWP").val("start");
});

$(document).on('click', '#tablistACTWP>li', function(){
	activityWPselectedTab=$(this).index();	
	activityWPTabSelection(activityWPselectedTab);
});

function activityWPTabSelection(selectedTab){
	console.log("selected "+selectedTab);
	activityWPId = activityWPJsonObj.activityWorkPackageId;	
	activityWPName = activityWPJsonObj.activityWPName;
	
	if(changeRequestToUsecase == "YES"){
		$('#tablistACTWP li:eq(4) a').text("Use Cases");
		$('#wpChangeRequests #trigUploadChangeRequests').val('Use Cases');
		// $('#wpChangeRequests').attr('title', "Download Template - Use Cases");
	}
	
	productName = title;
	productVersionId = 0;
	productBuildId = 0;
	activityWorkPackageId = 0;
	enableAddOrNot = "yes";
	$('#workflow_status_dd').hide();
	//var isActive = $("#isActive_ul").find('option:selected').val();	
	
	$('#toAnimate .portlet .actions').eq(0).css('display','none');
	if(selectedTab==0){
		// Overview
		showAWPSummary(activityWPJsonObj);
		
	}else if(selectedTab==1){
		// Dashboard
		showWorkpackageLevelDashboard();
		
	}else if(selectedTab==2){
		//Activities Tab
		$('#workflow_status_dd').show();
		listActivitiesOfSelectedAWP_DT(0,0,0,activityWPId,1,enableAddOrNot, "jTableContainerWPActivities_dataTable");
		$('#toAnimate .portlet .actions').eq(0).css('display','block');
		
	}else if(selectedTab==3){
		//Clarification Listing
		//listClarificationOfSelAWPComponent(activityWPId, activityId,productId,activityAssigneeId,0);
		
		var jsonObj={
			"Title": 'ActivityClarification',			          
			"SubTitle": 'ActivityClarification at Workpackage level',
			"listURL": 'list.clarificationtracker.by.activityworkpackage?entityTypeId='+workpackageTypeId+'&entityInstanceId='+activityWPId+'&jtStartIndex=0&jtPageSize=10000',
			"creatURL": 'add.clarificationtracker.by.activity',
			"updateURL": 'update.clarificationtracker.by.entityType',		
			"containerID": 'jTableContainerWPClarifications',
			"productId": productId,
			"componentUsage": "WorkpackageLevel",	
		};	 
		ActivityClarification.init(jsonObj);
		
	}else if(selectedTab==4){
		//ChangeRequest Listing
		//listChangeRequestsOfSelAWPComponent(activityWPId, activityId, productId);
		
		var jsonObj={
				"Title": 'ActivityChangeRequest',			          
				"SubTitle": 'ActivityChangeRequest at workpackage level',
				"listURL": 'list.changerequestset.by.activityWP?entityType1='+workpackageTypeId+'&entityInstance1='+activityWPId+'&jtStartIndex=0&jtPageSize=10000',
				"creatURL": 'changerequests.add.by.entityTypeAndInstanceId?entityInstanceId='+activityWPId,
				"updateURL": 'list.change.requests.by.activity.update?activityId='+activityId,		
				"containerID": 'jTableContainerWPChangeRequest',
				"productId": productId,
				"componentUsage": "WorkpackageLevel",	
			};	 
			ActivityChangeRequest.init(jsonObj);
		
	}else if(selectedTab==5){
		//Attachments
		var jsonObj={
				"Title":"Attachments for Activity WorkPackage",			          
				"SubTitle": 'Activity WorkPackage : ['+activityWPId+'] '+'Activity WorkPackage',
				"listURL": 'attachment.for.entity.or.instance.list?productId='+document.getElementById("treeHdnCurrentProductId").value+'&entityTypeId=34&entityInstanceId='+activityWPId,
				"creatURL": 'upload.attachment.for.entity.or.instance?productId='+document.getElementById("treeHdnCurrentProductId").value+'&entityTypeId=34&entityInstanceId='+activityWPId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
				"deleteURL": 'delete.attachment.for.entity.or.instance',
				"updateURL": 'update.attachment.for.entity.or.instance',
				"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=34',
				"jtableDivId": 'jTableContainerforWPAttachments',
				"multipleUpload":true,
		};	 
		AttachmentsTab.init(jsonObj);
		
	}else if(selectedTab==6){
		//Workflow
		
		var jsonObj={
				"Title":"Activity WorkFlow",			          
				"SubTitle": 'Activity WorkFlow : ['+activityWPId+'] '+'Activity WorkFlow',
				"listURL": 'workflow.status.policy.list?isActive=1&productId='+productId+'&workflowId='+sourceWorkflowIdMapped+'&entityTypeId='+34+'&entityId='+activityWPId+'&entityInstanceId='+0+'&scopeType=Instance&jtStartIndex=0&jtPageSize=1000',
				"creatURL": 'workflow.status.policy.add?workflowId=',				
				"updateURL": 'workflow.status.policy.update?workflowId='+sourceWorkflowIdMapped,				
				"containerID": 'jTableContainerforWorkflowPlanWorkpackage',		
				"entityTypeIdSP":34,
				"entityIdSP":0,
				"entityInstanceIdSP":activityWPId,
		};	 
		ActivityWorkflow.init(jsonObj); 
		//listWorkflowStatusAndPoliciesPlan(prodId, 34, 0, activityWPId, "jTableContainerforWorkflowPlanWorkpackage");
		
	}else if(selectedTab==7){
		//Audit History
		auditHistoryListURL ='administration.event.list?sourceEntityType=ActivityWorkpackage&sourceEntityId='+activityWPId;	
		auditHistoryCompListing(auditHistoryListURL, 'jTableContainerforAuditHistoryWorkpackage');		
	}
}

function listAWPDetailsComponent(jsonObj){
	activityWPJsonObj=jsonObj;	
	activityWPselectedTab=$("#tablistACTWP>li.active").index();
	activityWPTabSelection(activityWPselectedTab);	
}
var productVersionId=0;
var productBuildId=0;
var entityTypeId=33;
var entityTypeName="Activities";

function showWorkpackageLevelDashboard(){
	//DashBoard
	$('#dashboardRAGHeaderSubTitle').show();
	$('#dashboardWorkflowHeaderSubTitle').hide();
	showDashBoardEntityInstanceRAGSummary('RAGSummary','dashboardRAGHeaderSubTitle',testFactId,prodId,productVersionId,productBuildId,activityWPId,productName, entityTypeId, "", 0, activityWPId);
	
}
function showAWPSummary(jsonObj){
	$("#actworkPackageDiv #prodName").text('hello');
	$("#actworkPackageDiv #productVersionName").text('-hello-');
	$("#actworkPackageDiv #prodBuildName").text('-hello-');
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
	//	url : 'workpackage.result.testcase.details?testcaseId='+testcaseId+"&wptcepId="+wptcepId,
		url : jsonObj.activityWPURL,
		dataType : 'json',
		success : function(data) {
			var result=data.Records;
			var executionName="";
			var executionTime="";
			var executionStatus="";
			var isapproved = "";
			
			$("#actworkPackageDiv #act_wpkg_name").text('--');
			$("#actworkPackageDiv #act_wpkg_desc").text('--');
			$("#actworkPackageDiv #plannedDate").empty();
			$("#actworkPackageDiv #actualDate").empty();
			$("#actworkPackageDiv #plannedDate").text('--');
			$("#actworkPackageDiv #actualDate").text('--');
			$("#actworkPackageDiv #prodName").text('--');			
			$("#actworkPackageDiv #productVersionName").text('--');
			$("#actworkPackageDiv #prodBuildName").text('--');
			$("#actworkPackageDiv #act_wpkg_owner").val('--');
			$("#actworkPackageDiv #wpkg_status_options").empty();
			
			$("#actworkPackageDiv #sel_Activitycount").empty();
			
			$("#actworkPackageDiv #sel_CLARTcount").empty();
			$("#actworkPackageDiv #sel_CRcount").empty();
			$("#actworkPackageDiv #sel_ATTcount").empty();
			
			//$("#actworkPackageDiv #currCaseName, #currCaseId").text("");
			$.each(result, function(i,item){	
				//if(item.activityWorkPackageId != null){
					//$("#actworkPackageDiv #act_wpkg_name").text(item.activityWorkPackageId);		
				//}
				if(item.activityWorkPackageName != null){
					$("#actworkPackageDiv #act_wpkg_name").text(item.activityWorkPackageName);		
				}
				if(item.description != null){
					$("#actworkPackageDiv #act_wpkg_desc").text(item.description);		
				}
				if(item.plannedStartDate!= null && item.plannedEndDate != null){
					plannedEndDate=item.plannedEndDate;
					plannedStartDate=item.plannedStartDate;
					setDateOnDateRangepicker("#plan_defaultrange", setFormattedDate(item.plannedStartDate), setFormattedDate(item.plannedEndDate));
				}	
						
				if(item.actualStartDate!= null && item.actualEndDate != null)
					setDateOnDateRangepicker("#actual_defaultrange", setFormattedDate(item.actualStartDate), setFormattedDate(item.actualEndDate));
				var prodVerBuildName_val = '';		
				if(item.productName != null){
					$("#actworkPackageDiv #prodName").text(item.productName);
					prodVerBuildName_val = item.productName+'/';
				}	
				if(item.productVersionName != null){
					$("#actworkPackageDiv #productVersionName").text(item.productVersionName);
					prodVerBuildName_val = prodVerBuildName_val + item.productVersionName+'/';
				}					
				if(item.productBuildName != null){
					$("#actworkPackageDiv #prodBuildName").text(item.productBuildName);
					prodVerBuildName_val = prodVerBuildName_val + item.productBuildName;
				}		
				$("#actworkPackageDiv #prodVerBuildName").text(prodVerBuildName_val);
				
				if(item.ownerName != null){
					$("#actworkPackageDiv #act_wpkg_owner").text(item.ownerName);
				}
				
				if(item.activityCount!=null){
					$("#actworkPackageDiv #sel_Activitycount").text(item.activityCount);
				}
				
				if(item.clarificationCount!=null)
					//$("#actworkPackageDiv #sel_CLARTcount").append("<div style='font-size:small;' >"+item.clarificationCount+"</div>");
					$("#actworkPackageDiv #sel_CLARTcount").text(item.clarificationCount);

				if(item.changeRequestCount!=null)
					//$("#actworkPackageDiv #sel_CRcount").append("<div style='font-size:small;' >"+item.changeRequestCount+"</div>");
					$("#actworkPackageDiv #sel_CRcount").text(item.changeRequestCount);

				if(item.attachmentCount!=null)
				//	$("#actworkPackageDiv #sel_ATTcount").append("<div style='font-size:small;' >"+item.attachmentCount+"</div>");
					$("#actworkPackageDiv #sel_ATTcount").text(item.attachmentCount);
				/*if(item.actBeginCount!=null)
					$("#actworkPackageDiv #sel_clarificationscount").append("<div style='font-size:small;' >"+item.actBeginCount+"</div>");

				 */				
				if(item.plannedExecutionDate != null){				
					
					$("#actworkPackageDiv #plannedDate").append("<div style='font-size:small;' >"+item.plannedExecutionDate+"</div>");	
				}
				//$("#actworkPackageDiv #plannedShift").append("<div style='font-size:small;' >"+item.plannedShiftName+"</div>");
				if(item.actualExecutionDate != null){
					$("#actworkPackageDiv #actualDate").append("<div  style='font-size:small;' >"+item.actualExecutionDate+"</div>");
				}
				////////////////////////// - modified started -
				
				if(item.testcaseId != null){
					$("#actworkPackageDiv #testcaseId").text(item.testcaseId);
				}
				var tcCode = "";
				if(item.testcaseCode != null){
					tcCode = item.testcaseCode;
				}				
				$("#actworkPackageDiv #testcaseCode").text(tcCode);
				
				if(item.testcaseName != null){
					$("#actworkPackageDiv #testcaseName").text(item.testcaseName);
				}
				if(item.testcaseType != null){
					$("#actworkPackageDiv #testcasetype").text(item.testcaseType);
				}
				if(item.testcasePriority != null){
					$("#actworkPackageDiv #testcasePriority").text(item.testcasePriority);
				}				
				if(item.workPackageName != null){
					$("#actworkPackageDiv #act_wpkg_name").text(item.workPackageName);
				}
				if(item.testRunJobId != null){
					$("#actworkPackageDiv #wpJob").text(item.testRunJobId);	
				}
				if(item.runConfigurationName != null){
					$("#actworkPackageDiv #environment").text(item.runConfigurationName);	
				}
				//$("#actworkPackageDiv #plannedShift").text(item.plannedShiftName);
				if(item.actualExecutionDate != null){
					$("#actworkPackageDiv #actualDate").text(item.actualExecutionDate);	
				}
				if(item.environmentCombinationName != null){
					$("#actworkPackageDiv #environmentName").text(item.environmentCombinationName);	
				}
				if(item.deviceName != null){
					$("#actworkPackageDiv #deviceId").text(item.deviceName);	
				}
				if(item.devplatformTypeName != null){
					$("#actworkPackageDiv #devPlatform").text(item.devplatformTypeName);	
				}
				if(item.hostName != null){
					$("#actworkPackageDiv #hostName").text(item.hostName);	
				}
				if(item.hostIPAddress != null){
					$("#actworkPackageDiv #hostIp").text(item.hostIPAddress);	
				}
				
				////////////////////////  - modified ended -
				if(jsonObj.executionId != null){
					$("#actworkPackageDiv #exeId").text(jsonObj.executionId);	
				}
				if(item.result != null){					
					$("#actworkPackageDiv #exeResult").text(item.result);	
				}
				if(item.expectedOutput != null){
					$("#actworkPackageDiv #expOutput").text(item.expectedOutput);	
				}
				if(item.observedOutput != null){
					$("#actworkPackageDiv #obsOutput").text(item.observedOutput);		
				}
				if(item.startTime != null){
					$("#actworkPackageDiv #exeStartTime").text(item.startTime);				
				}
				if(item.endTime != null){
					$("#actworkPackageDiv #exeEndTime").text(item.endTime);					
				}
				var evidenceHTML = '';
				if(item.evidenceLabel != null){
				var temp=	item.evidenceLabel.split(",");
				for(var i=0; i < temp.length ;i++){
					var evidence = temp[i];
				var evedenceArr = evidence.split("@");
				var url = evedenceArr[0].split('\\').join('\\\\');
				evidenceHTML = evidenceHTML +"<a href='#' onclick='downloadEvidenceinTestCaseDetails(\""+url+"\")'>"+evedenceArr[1]+"</a><br></br>";
				}
					
					$("#actworkPackageDiv #evidence").append(evidenceHTML);					
				}
				if(item.jobFailureMessage != null){
					$("#actworkPackageDiv #failRemarks").text(item.jobFailureMessage);		
				}
				
				if(item.executionStatus==3)
					executionName="NotStarted";
				else if(item.executionStatus==1)
					executionName="Assigned";
				else if(item.executionStatus==2)
					executionName="Completed";
				
				//$("#actworkPackageDiv #executionStatus").append("<div  style='font-size:small;' >"+executionName+"</div>");
				$("#actworkPackageDiv #executionStatus").text(executionName);				
				//$("#executionPriority").append("<div  style='font-size:small;' title='"+item.executionName +"' >"+item.executionPriorityDisplayName+"</div>");
				
				var inputId = item.id+item.testcaseId;
				var dataid = item.executionPriorityId;
				//console.log(item);
				if(dataid == 5){
					$("#actworkPackageDiv #executionPriority").append('<input name="star'+inputId+'" disabled type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" checked="checked"/> <input name="star'+inputId+'" type="radio" class="hover-star"   value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'"   type="radio" class="hover-star"  value="5~'+inputId+'" title="P0"/> ');
				}else if(dataid == 4){
					$("#actworkPackageDiv #executionPriority").append('<input name="star'+inputId+'"  disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" /> <input name="star'+inputId+'" type="radio" class="hover-star"   value="2~'+inputId+'" title="P3" checked="checked"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="5~'+inputId+'" title="P0"/>  ');
				}else if(dataid == 3){
					$("#actworkPackageDiv #executionPriority").append('<input name="star'+inputId+'"  disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" /> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2" checked="checked"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="5~'+inputId+'" title="P0"/>  ');
				}else if(dataid == 2){
					$("#actworkPackageDiv #executionPriority").append('<input name="star'+inputId+'" disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" /> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1" checked="checked"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="5~'+inputId+'" title="P0"/> ');
				}else if(dataid == 1){
					$("#actworkPackageDiv #executionPriority").append('<input name="star'+inputId+'" disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" /> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'" type="radio"  class="hover-star"  value="5~'+inputId+'" title="P0" checked="checked"/> ');
				}else {
					$("#actworkPackageDiv #executionPriority").append('<input name="star'+inputId+'" disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4"/> <input name="star'+inputId+'" type="radio" class="hover-star"   value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'" type="radio"  class="hover-star"  value="5~'+inputId+'" title="P0"/>  ');
				}			
				
				if(document.getElementById('tcerIdhidden')!=null)
					document.getElementById('tcerIdhidden').value=item.testCaseExecutionResultId;				

				$('#jqmeter-container').find('.percent').text(item.actBeginCount);
				 $('#jqmeter-container-defects').find('.percent').text(item.actIntermediateCount);
				 $('#jqmeter-container-jobs').find('.percent').text(item.actAbortCount);
				 $('#jqmeter-container-time').find('.percent').text(item.actEndCount);
				 
				 $('#actworkPackageDiv #status_txt').text(item.workflowStatusDisplayName);
			});			
		}
	});	
}

function updateModifiedValue(type){
	 var modifiedValue;
	 var modifiedField;
	 var activityWorkPackageId= document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
	 var thediv = document.getElementById('reportbox');
	 
	 if(type=='act_wpkg_name'){
		 modifiedValue= $('#actworkPackageDiv #act_wpkg_name').val();
	 }
	if(type=='act_wpkg_desc'){
		modifiedValue=$('#actworkPackageDiv #act_wpkg_desc').val();
	 }
  	 
	 if(type == 'start' || type == 'end'){
		 modifiedValue = $("#actworkPackageDiv#hdnStartDateAWP").val() + "~" + $("#hdnEndDateAWP").val();
	 }
	 if(type == 'plannedStartDate' || type == 'plannedEndDate'){
		 modifiedValue = $("#actworkPackageDiv #hdnStartDateAWP").val() + "~" + $("#hdnEndDateAWP").val();
	 }
	 url='process.workrequest.update.modifiedfield?activityWorkPackageId='+activityWorkPackageId+"&modifiedfField="+type+"&modifiedValue="+modifiedValue;
	   
	if (thediv.style.display == "none") {
	$.blockUI({
	 	theme : true,
	 	title : 'Please Wait',
	 	message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
	 });
	 $.post(url,function(data) {
		 if(data.Result=="ERROR"){
			 $.unblockUI();
			 callAlert(data.Message);
			 return false;
		 }
		 $.unblockUI();
		});
	} else {
		thediv.style.display = "none";
		thediv.innerHTML = '';
	}
}

function setDateOnDateRangepicker(pkr_id, stDate, edDate){
	$(pkr_id).data("daterangepicker").setStartDate(stDate);
	$(pkr_id).data("daterangepicker").setEndDate(edDate);
	var strtDate = moment();
	strtDate._d = new Date(stDate);
	var endDate = moment();
	endDate._d = new Date(edDate);
	$(pkr_id).data("daterangepicker").cb(strtDate, endDate)
}

/*
function listClarificationOfSelAWPComponent(activityWPId, activityId,productId,activityAssigneeId,testFactId) { 
			var urlToGetClarificationTrackersByAWPID = 'list.clarificationtracker.by.activityworkpackage?entityTypeId='+workpackageTypeId+'&entityInstanceId='+activityWPId;
			var plannedActivitySize=0;
			
			try {
				if ($('#jTableContainerWPClarifications').length > 0) {
					$('#jTableContainerWPClarifications').jtable('destroy');
				}
			} catch (e) {
			}
			$('#jTableContainerWPClarifications').jtable( 
       					{ 
       						title: 'Clarification', 
       					 	editinline:{enable:true},
       					 editInlineRowRequestModeDependsOn: true,
       					 paging : true, //Enable paging								
							pageSize : 10,
       					actions: { 
       						listAction: urlToGetClarificationTrackersByAWPID,
       						createAction : 'add.clarificationtracker.by.activity',	    									
							editinlineAction : 'update.clarificationtracker.by.entityType',
       						},
      					recordsLoaded: function(event, data) {
      		        		$(".jtable-edit-command-button").prop("disabled", true);      		        		
      		         	},
    		         	recordUpdated:function(event, data){
    		         		$('#jTableContainerWPClarifications').find('.jtable-main-container .reload').trigger('click');
      		  			},
       					fields: { 
       						   activityId: { 
       							type: 'hidden', 
       							defaultValue: activityId 
       						},   
       					 productId: { 
    							type: 'hidden', 
    							create: true, 
    							edit:true,
    							defaultValue: productId
    						}, 
    					 testFactoryId: {
    						 type: 'hidden', 
 							create: true, 
 							edit:true,
 							defaultValue: testFactId
    					 },
    					 entityTypeId:{
								type: 'hidden',
								create: true, 
				                edit: true, 
        						list: true,
								defaultValue: workpackageTypeId
							},
							entityTypeId2:{
								type: 'hidden',
								create: true, 
				                edit: false, 
        						list: false,
								defaultValue: clarificationTypeId
							},							
    						 entityInstanceId: {
    							type: 'hidden',
    							create: true, 
				                edit: false, 
        						list: false,
       							defaultValue: activityWPId
    						}, 
       						clarificationTrackerId: { 
           						key: true, 
           						title : 'ID',
           						create: false, 
				                edit: false, 
           						list: true
           					},
							entityType : {
								title : 'Source',
								list : true,				    										
								create : false,
								edit : false,
								width : "5%"
								
							},
							entityInstanceName : {
								title : 'Source Name',
								list : true,				    										
								create : false,
								edit : false,
								width : "10%"
							},
           					clarificationTitle : {
								title : 'Title',
								inputTitle : 'Clarification Title <font color="#efd125" size="4px">*</font>',
								list : true,				    										
								create : true,
								edit : true,
								width : "20%"
								
							},
							clarificationDescription:{
					             title: 'Description',
					             list:true,
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
								options : 'administration.executionPriorityList'
							},					
							clarificationTypeId : {								
								title : 'Type',	
								inputTitle : 'Clarification Type <font color="#efd125" size="4px">*</font>',
								list : true,
								create : true,
								edit : true,
								width : "20%",
								options : 'list.clarification.type.option'															
							},
							clarificationScopeId : {
								title : 'Scope',
								inputTitle : 'Clarification Scope <font color="#efd125" size="4px">*</font>',
								list : true,				    										
								create : true,
								edit : true,
								width : "20%",
								options : 'list.clarification.scope.option'
								
							},
							planExpectedValue: {
								title : 'Planned Value',										
								inputTitle : 'Planned Value <font color="#efd125" size="4px"></font>',
								edit : true,
								list : true,
								create : true,								
								width : "20%",
								defaultValue:plannedActivitySize,
							},
							workflowStatusId : {
								title : 'Status',
								create : false,
								list : true,
								edit : true,
								width : "20%",
								options: function(data){
									return 'clarification.status.option.list?entityTypeId=31';
								}
							}, resolution : {
								title : 'Resolution',
								create : false,
								list : true,
								edit : true,
								width : "20%",
								dependsOn:'workflowStatusId',									
									options:function(data){
										return 'clarification.resolution.option.list?parentStatusId='+data.dependedValues.workflowStatusId;						     		}
							},     
							ownerId : {
								title : 'Owner',				    									
								list : true,
								width : "20%",
								create : true,
								edit : true,
								defaultValue:defaultuserId,
								options : 'common.allusers.list.by.resourcepool.id'
							},  							   
							raisedById : {
								title : 'Raised By',
								inputTitle : 'Raised By <font color="#efd125" size="4px">*</font>',
								list : true,
								create : true,
								width : "20%",
								edit : true,
								defaultValue:defaultuserId,
								options : 'common.allusers.list.by.resourcepool.id'								
							},
							raisedDate : {
								title : 'Raised Date',
								inputTitle : 'Raised Date <font color="#efd125" size="4px">*</font>',
								edit : true,
								list : true,
								create : true,
								type : 'date',
								width : "20%",
								defaultValue : (new Date().getMonth()+1)+"/"+new Date().getDate()+"/"+new Date().getFullYear(),
							},
							plannedEndDate : {
								title : 'Expected End Date',										
								inputTitle : 'Expected End Date <font color="#efd125" size="4px"></font>',
								edit : true,
								list : true,
								create : true,
								type : 'date',
								width : "20%",
								defaultValue : (new Date().getMonth()+1)+"/"+new Date().getDate()+"/"+new Date().getFullYear(),
							},
							plannedStartDate : {
								title : 'Planned Start Date',										
								inputTitle : 'Planned Start Date <font color="#efd125" size="4px"></font>',
								edit : false,
								create : false,
								list : false,
								type : 'date',
								width : "20%",
								defaultValue : (new Date().getMonth()+1)+"/"+new Date().getDate()+"/"+new Date().getFullYear(),
							},actualStartDate : {
								title : 'Actual Start Date',
								create : false,
								edit : false,
								list : false,
								type : 'date',
								width : "20%"
							},
							actualEndDate : {
								title : 'Actual End Date',
								create : false,
								edit : false,
								list : false,
								type : 'date',
								width : "20%"
							}, attachment: { 
								title: '', 
								list: true,
								create :false,
								width: "5%",
								display:function (data) {	        		
					           		//Create an image that will be used to open child table 
									var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>');
					       			$img.click(function () {
					       				isViewAttachment = false;
					       				var jsonObj={"Title":"Attachments for Clarification",			          
					       					"SubTitle": 'Clarification : ['+data.record.clarificationTrackerId+'] '+data.record.clarificationTitle,
					    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+data.record.productId+'&entityTypeId=31&entityInstanceId='+data.record.clarificationTrackerId,
					    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+data.record.productId+'&entityTypeId=31&entityInstanceId='+data.record.clarificationTrackerId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
					    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
					    	    			"updateURL": 'update.attachment.for.entity.or.instance',
					    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=31',
					    	    			"multipleUpload":true,
					    	    		};	 
						        		Attachments.init(jsonObj);
					       		  });
					       			return $img;
					        	}				
							}, 
				 commentsAWPCTracker:{
					title : '',
					list : true,
					create : false,
					edit : false,
					width: "5%",
					display:function (data) { 
						//Create an image for test script popup 
						var $img = $('<i class="fa fa-comments" title="Comments"></i>');
						$img.click(function () {						
							//listActivityWorkPackageAuditHistory(data.record.activityWorkPackageId);
							var entityTypeIdComments = 52;
							var entityNameComments = "ClarificationTracker";
							listComments(productId,entityTypeIdComments, entityNameComments, data.record.clarificationTrackerId, data.record.clarificationTitle, "awpLevelclarificationTrackerComments");
							
						});
					return $img;
					}
				 },
							
						},
       	
						formSubmitting: function (event, data) {
						      	    data.form.find('input[name="clarificationTitle"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[maxSize[250]]');
						            data.form.find('input[name="raisedDate"]').addClass('validate[required]');					  							           
						            data.form.validationEngine();
						            return data.form.validationEngine('validate');
						          },   
                         //Dispose validation logic when form is closed
                         formClosed: function (event, data) {
                            data.form.validationEngine('hide');
                            data.form.validationEngine('detach');
                        }, 
      			 	}); 
			$('#jTableContainerWPClarifications').jtable('load');
}
*/

var changeRequestTitleAWP = "";
var addPoPupChangeRequestNameAWP  = "";
function crTOUseCaseAWP(){
	if(useCaseAWP == "YES"){		
		changeRequestTitleAWP = "Use Cases";	
		addPoPupChangeRequestNameAWP = 'Use Case Name <font color="#efd125" size="4px">*</font>';
		
	}else{
		changeRequestTitleAWP = "Change Request";
		addPoPupChangeRequestNameAWP = 'Change Request Name <font color="#efd125" size="4px">*</font>';
	}
			return changeRequestTitleAWP;
}

/*
function listChangeRequestsOfSelAWPComponent(activityWPId, activityId,productId) { 
			var urlToGetChangeRequestsOfSpecifiedActWPId = 'list.changerequestset.by.activityWP?entityType1='+workpackageTypeId+'&entityInstance1='+activityWPId;
			var plannedActivitySize=0;
			try {
				if ($('#jTableContainerWPChangeRequest').length > 0) {
					$('#jTableContainerWPChangeRequest').jtable('destroy');
				}
			} catch (e) {
			}
       				$('#jTableContainerWPChangeRequest').jtable( 
       					{						                   	      	   
       						//title: 'Change Request',	
       						title : crTOUseCaseAWP(),
       						editinline:{enable:true},       
       						paging : true, //Enable paging								
							pageSize : 10,
       					actions: { 
       						listAction: urlToGetChangeRequestsOfSpecifiedActWPId,
       						createAction : 'changerequests.add.by.entityTypeAndInstanceId?entityInstanceId='+activityWPId,
       						editinlineAction : 'list.change.requests.by.activity.update?activityId='+activityId,
       					},
    					recordsLoaded: function(event, data) {
    		        		$(".jtable-edit-command-button").prop("disabled", true);
    		         	},	
       					fields: { 
       						productId: { 
    							type: 'hidden', 
    							create: true, 
    							edit:true 
    						},       						
       						changeRequestId: { 					                   							
       							title : 'ID',
       							key: true, 
           						create: false, 
				                edit: false, 
           						list: true
           					},
							entityType : {
								title : 'Source',
								list : true,				    										
								create : false,
								edit : false,
								width : "5%"
								
							}, entityInstanceName : {
								title : 'Source Name',
								list : true,				    										
								create : false,
								edit : false,
								width : "10%"
							},
           					entityType1:{
								type: 'hidden',
								create: true, 
				                edit: true, 
           						list: true,
								defaultValue: workpackageTypeId
							},
							entityType2:{
								type: 'hidden',
								create: true, 
				                edit: false, 
           						list: false,
								defaultValue: changeRequestTypeId
							},
							entityInstance1:{
								type: 'hidden',
								create: true, 
				                edit: false, 
           						list: false,
								defaultValue: activityWPId,
							},
           					changeRequestName: { 					                   							
       							title : 'Name',
       							inputTitle : addPoPupChangeRequestNameAWP,
       							key: true, 
           						create: true, 
				                edit: true, 
           						list: true
           					},
           					description: {
               					 title: 'Description',
					             list:true,
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
								options : 'administration.executionPriorityList'
							},
							planExpectedValue: {
								title : 'Planned Value',										
								inputTitle : 'Planned Value <font color="#efd125" size="4px"></font>',
								edit : true,
								list : true,
								create : true,								
								width : "20%",
								defaultValue:plannedActivitySize,
							},
							statusCategoryId : {
								title : 'Status',
								create : true,
								list : true,
								edit : true,
								width : "20%",
								options: 'status.category.option.list'								
							}, 
								ownerId : {
								title : 'Owner',					    										
								list : true,
								create : true,
								edit : true,
								defaultValue:defaultuserId,
								options : function(data) {
									if(productId != null){
										return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
									}
								}
							},
							raisedDate : {
								title : 'Raised Date',
								inputTitle : 'Raised Date <font color="#efd125" size="4px">*</font>',
								edit : true,
								list : true,
								create : true,
								type : 'date',
								width : "20%",
								defaultValue : (new Date().getMonth()+1)+"/"+new Date().getDate()+"/"+new Date().getFullYear(),
							}, attachment: { 
								title: '', 
								list: true,
								create: false,
								width: "5%",
								display:function (data) {	        		
					           		//Create an image that will be used to open child table 
									var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>'); 
					       			$img.click(function () {
					       				isViewAttachment = false;
					       				var jsonObj={"Title":"Attachments for ChangeRequest",			          
					       					"SubTitle": 'ChangeRequest : ['+data.record.changeRequestId+'] '+data.record.changeRequestName,
					    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+data.record.productId+'&entityTypeId=42&entityInstanceId='+data.record.changeRequestId,
					    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+data.record.productId+'&entityTypeId=42&entityInstanceId='+data.record.changeRequestId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
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
								title : 'Audit History',
								list : true,
								create : false,
								edit : false,
								width: "10%",
								display:function (data) { 
									//Create an image for test script popup 
									var $img = $('<i class="fa fa-search-plus" title="Audit History"></i>');
									//Open Testscript popup  
										$img.click(function () {
											titleCrtoUc = '';
											if(changeRequestToUsecase == "YES"){
												titleCrtoUc = "Use Cases";
											}else{
												titleCrtoUc = "ChangeRequest";
											}
											listGenericAuditHistory(data.record.changeRequestId,titleCrtoUc,"changeRequestAudit");
										});
										return $img;
								}
							}, 
							commentsAWPCTracker:{
								title : '',
								list : true,
								create : false,
								edit : false,
								width: "5%",
								display:function (data) { 
								//Create an image for test script popup 
								var $img = $('<i class="fa fa-comments" title="Comments"></i>');
								$img.click(function () {
									var entityTypeIdComments = 42;
									var entityNameComments = "ChangeRequest";
									listComments(entityTypeIdComments, entityNameComments, data.record.changeRequestId, data.record.changeRequestName, "awpLevelCRComments");
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
       				}); 
       				$('#jTableContainerWPChangeRequest').jtable('load');
			}
*/

function showGroupDashboardSummaryTableTool(tabVisible){
	if(tabVisible == 1){
		$('#workflowSummary').hide();
		$('#dashboardWorkflowHeaderSubTitle').hide();
		$('#dashboardRAGHeaderSubTitle').show();
		$('#RAGSummary').show();
	} else {
		$('#RAGSummary').hide();
		$('#workflowSummary').show();
		$('#dashboardWorkflowHeaderSubTitle').show();
		$('#dashboardRAGHeaderSubTitle').hide();
		showDashBoardWorkflowEntityInstanceStatusSummary('workflowSummary','dashboardWorkflowHeaderSubTitle',testFactId,prodId,productVersionId,productBuildId,activityWPId,productName, entityTypeId, entityTypeName, 0,activityWPId);
	}
}