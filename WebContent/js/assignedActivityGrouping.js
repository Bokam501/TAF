var activitiesJsonObj='';
var ActiviesViewDetails = function(){  
	var initialise = function(jsonObj){	    
	   listActiviesDetailsComponent(jsonObj);
	};
	return {
		//main function to initiate the module
        init: function(jsonObj) {        	
       	initialise(jsonObj);
        }		
	};	
}();

var testFactId=0;
var activityWorkpackageIdForWF = 0;
var clarificationTypeId = 31;
var changeRequestTypeId = 42;
var activityEntityTypeId = 28;

//var defaultTaskPlannedStartDate = new Date();
//var defaultTaskPlannedEndDate = new Date();
var defaultTaskLifeCycleStage = 0;
var taskProductId = 0;
var actionType=0;
var assignedActivitySelectedTab=0;

function listActiviesDetailsComponent(jsonObj){
	activitiesJsonObj = jsonObj;
	assignedActivitySelectedTab=$("#tablistActivities>li.active").index();
	assignedActivitiesTabSelection(assignedActivitySelectedTab);
}

function createCustomFields(){
	var jsonObj={"Title":"Custom Fields For Activity : ["+activitiesJsonObj.activityId+"] "+activitiesJsonObj.activityName,
			"subTitle": "",
			"url": "data/data.json",
			"columnGrouping":2,
			"containerSize": "large",
			"componentUsageTitle":"customFields",
			"entityId":"28",
			"entityTypeId":activitiesJsonObj.activityTypeId,
			"entityInstanceId":activitiesJsonObj.activityId,
			"parentEntityId":"18",
			"parentEntityInstanceId":activitiesJsonObj.productId,
			"engagementId":activitiesJsonObj.testFactId,
			"productId":activitiesJsonObj.productId,
			"singleFrequency":"customFieldSingleFrequencyContainer",
			"multiFrequency":"customFieldMultiFrequencyContainer",
			
	};
	//CustomFieldGropings.init(jsonObj);
	
	return jsonObj;
}

$(document).on('click', '#tablistActivities>li', function(){
	assignedActivitySelectedTab=$(this).index();	
	assignedActivitiesTabSelection(assignedActivitySelectedTab);
});

function assignedActivitiesTabSelection(selectedTab){
	console.log("selected "+selectedTab);
	
	if(selectedTab==0){
		// Overview
		showMyActivitySummaryDetails(activitiesJsonObj.activityId);
		
	}else if(selectedTab==1){
		// Activity Task
		//listActivitiesOfSelectedActivityGrouping(activitiesJsonObj.activityId,0,-1,activityAssigneeId,activityReviewerId,activityWorkPackageId,activitiesJsonObj.productId);
		if(activityWPId == 0 || activityWPId == null || activityWPId == undefined)
			activitiesJsonObj['activityWorkPackageId']=0;
		
		var url='activitytask.list.for.review?activityId='+activitiesJsonObj.activityId+'&productId='+activitiesJsonObj.productId+'&jtStartIndex=0&jtPageSize=10000';
		$("#jTableContainerActivityTasks").html('');
		
		$("#activitySummaryLoaderIcon").show();	
		$("#jTableContainerActivityTasks").append('<div id="activityTabContainer_jTableContainerActivityTasks"></div>');				
		assignDataTableValuesInActivityManagementTab(url, "jTableContainerActivityTasks", "childTable3", '', '');
		
	}/*else if(selectedTab==2){
		//Series		
		var container = '<div id="customFieldMultiFrequencyID"></div>';
		$("#activityMultiSeriesContainer").html('');
		$("#activityMultiSeriesContainer").append(container);
		multiFrequenceyContainer='';
		customFieldGroupingsJsonObj = activityCreationJsonObj(0);
		customFieldGroupingsJsonObj['componentUsageTitle']= 'CustomFieldSeries';
		customFieldSeriesFrequancyHandler();
		
	}*/else if(selectedTab==2){
		// Change Request
		document.getElementById("treeHdnCurrentActivityId").value = activitiesJsonObj.activityId;			
		var testFactoryIdUrl = 'testFactoryId.By.productId?productId='+activitiesJsonObj.productId;
		getTestFactoryId(testFactoryIdUrl,activitiesJsonObj.activityId,activityAssigneeId,activityReviewerId,activityWorkPackageId,activitiesJsonObj.productId);
		
	}else if(selectedTab==3){
		// Clarification
		//listClarificationOfSelectedActivity(activitiesJsonObj.activityId,prodId,activityAssigneeId, activitiesJsonObj.testFactId);
		
		var jsonObj={
			"Title": 'ActivityClarification',			          
			"SubTitle": 'ActivityClarification at Activity level',
			"listURL": 'list.clarificationtracker.by.entityTypeAndInstanceIds?entityTypeId='+activityEntityTypeId+'&entityInstanceId='+activityId+'&jtStartIndex=0&jtPageSize=10000',
			"creatURL": 'add.clarificationtracker.by.activity',
			"updateURL": 'update.clarificationtracker.by.activity',		
			"containerID": 'jTableContainerforClarification',
			"productId": productId,
			"componentUsage": "ActivityLevel",	
		};	 
		ActivityClarification.init(jsonObj);
		
	}else if(selectedTab==4){
		//Attachments
		var jsonObj={
				"Title":"Attachments for Activity",			          
				"SubTitle": 'Activity : ['+activitiesJsonObj.activityId+'] '+' Activity',
			//	"listURL": 'attachment.for.entity.or.instance.list?productId='+activitiesJsonObj.productId+'&entityTypeId=28&entityInstanceId='+activitiesJsonObj.activityId,
				"creatURL": 'upload.attachment.for.entity.or.instance?productId='+activitiesJsonObj.productId+'&entityTypeId=28&entityInstanceId='+activitiesJsonObj.activityId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
				"deleteURL": 'delete.attachment.for.entity.or.instance',
				"updateURL": 'update.attachment.for.entity.or.instance',
				"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=28',
				"jtableDivId": 'jTableContainerforACTAttachments',
				"multipleUpload":true,
		};	 
		AttachmentsTab.init(jsonObj);
		
	}else if(selectedTab==5){
		// Workflow
		
		var jsonObj={
				"Title":"Activity WorkFlow",			          
				"SubTitle": 'Activity WorkFlow : ['+activitiesJsonObj.activityTypeId+'] '+'Activity WorkFlow',
				"listURL": 'workflow.status.policy.list?isActive=1&productId='+productId+'&workflowId='+sourceWorkflowIdMapped+'&entityTypeId='+33+'&entityId='+activitiesJsonObj.activityTypeId+'&entityInstanceId='+activitiesJsonObj.activityId+'&scopeType=Instance&jtStartIndex=0&jtPageSize=1000',
				"creatURL": 'workflow.status.policy.add?workflowId=',				
				"updateURL": 'workflow.status.policy.update?workflowId='+sourceWorkflowIdMapped,				
				"containerID": 'jTableContainerforWorkflowPlanActivityOrTask',		
				"entityTypeIdSP":33,
				"entityIdSP":activitiesJsonObj.activityId,
				"entityInstanceIdSP":activitiesJsonObj.activityTypeId,
		};	 
		ActivityWorkflow.init(jsonObj);
		
		
		//listActivityWorkflowPlan(activitiesJsonObj.productId, 33, activitiesJsonObj.activityTypeId, activitiesJsonObj.activityId, "jTableContainerforWorkflowPlanActivityOrTask");
	
	}else if(selectedTab==6){
		//Audit History
		auditHistoryListURL ='administration.event.list?sourceEntityType=Activity&sourceEntityId='+activitiesJsonObj.activityId;
		auditHistoryCompListing(auditHistoryListURL, 'jTableContainerforACTAuditHistory');		
		
	}	
}

// ---- starting -----
	
function addTaskCommentsGrouping(record){	

	var entityInstanceName = record.activityTaskName;	
	var entityInstanceId = record.activityTaskId;
	var modifiedById = record.assigneeId;
	var currentStatusId = record.statusId;
	var currentStatusName = record.statusName;
	var entityTypeId = 30;
	var entityId = record.activityTaskTypeId;
	var actionTypeValue = 0;
	var secondaryStatusId = record.secondaryStatusId;
	var productId=0;
	var activityTypeId = 0;
	
	var visibleEventComment=record.visibleEventComment;
	$("#addCommentsMainDiv").modal();		
	if(!visibleEventComment) {
		$('#addComments').hide();//Display only histroy of task Effort
	}
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
			primaryStatusUrl : 'workflow.status.master.option.list?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&currentStatus='+currentStatusName,
			secondaryStatusUrl : 'workflow.entity.secondary.status.master.option.list?productId='+productId+'&entityTypeId='+entityTypeId+'&statusId='+currentStatusId,
			currentStatusId : currentStatusId,
			currentStatusName : currentStatusName,
			secondaryStatusId : secondaryStatusId,
			effortListUrl : 'workflow.event.tracker.list?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
			actionTypeValue : actionTypeValue,
			commentsName : commentsReviewActivity,
			urlToSave : 'workflow.event.tracker.add?productId='+productId+'&entityId='+entityId+'&entityTypeId='+entityTypeId+'&primaryStatusId=[primaryStatusId]&secondaryStatusId=[secondaryStatusId]&effort=[effort]&comments=[comments]&sourceStatusId='+currentStatusId+'&approveAllEntityInstanceIds=[approveAllEntityIds]&entityInstanceId='+entityInstanceId+'&attachmentIds=[attachmentIds]&actionDate=[actionDate]',
			// commentsStatus: "['started','InProgress','Completed']",		
	};
	AddComments.init(jsonObj);
}

function getTestFactoryId(testFactoryIdUrl,activityId,activityAssigneeId,activityReviewerId,activityWorkPackageId,productId){	
	var jsonObj={
			"Title": 'ActivityChangeRequest',			          
			"SubTitle": 'ActivityChangeRequest at activityIDLevel',
			"listURL": 'list.changerequests.by.entityTypeAndInstanceId?entityType1='+activityEntityTypeId+'&entityInstance1='+activityId+'&jtStartIndex=0&jtPageSize=10000',
			"creatURL": 'changerequests.add.by.entityTypeAndInstanceId?entityInstanceId='+activityId,
			"updateURL": 'list.change.requests.by.activity.update?activityId='+activityId,		
			"containerID": 'jTableContainerforChangeRequests',
			"productId": productId,
			"componentUsage": "ActivityIDLevel",	
		};	 
		ActivityChangeRequest.init(jsonObj);
	
	$.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: testFactoryIdUrl,
        dataType: 'json',
        success: function(data) {   
        	testFactId=data.Records[0].testFactoryId;		        	
			
			var jsonObj={
				"Title": 'ActivityClarification',			          
				"SubTitle": 'ActivityClarification at Activity level',
				"listURL": 'list.clarificationtracker.by.entityTypeAndInstanceIds?entityTypeId='+activityEntityTypeId+'&entityInstanceId='+activityId+'&jtStartIndex=0&jtPageSize=10000',
				"creatURL": 'add.clarificationtracker.by.activity',
				"updateURL": 'update.clarificationtracker.by.activity',		
				"containerID": 'jTableContainerforClarification',
				"productId": productId,
				"componentUsage": "ActivityLevel",	
			};	 
			ActivityClarification.init(jsonObj);
		
			//$("#bulkselectionnew").show();
			$("#reviewFilter").show();
        	
        }
   });
}

/* Pop Up close function */
function popupClose() {
	//document.getElementById("lbl_PopupTitle").innerHTML = "";
	//document.getElementById("lbl_RightTitle").innerHTML = "";
	//$("#tbl_PopupData").empty();
	$("#div_PopupMain").fadeOut("normal");
	$("#div_PopupBackground").fadeOut("normal");
}
function ActivitiesExecutionDetailsFolderLevel(){	
	try{
		urlForListing='process.activity.folder.list';
		
		/* if  toShow=7(autometed) if toShow =8(manual) and obj==1(tree clicked) else(tab clicked)  */
		/*if(obj==1){
			var toShow = $("#giveShowTab").find("label.active").attr("data");
		
			if(toShow==8){				
				urlForListing='workpackage.executiondetails.productorbuildlevel.list?productBuildId='+productBuildId+'&productId='+prodId;
			}else {
				urlForListing='workpackage.executiondetails.productorbuildlevel.list?productBuildId='+productBuildId+'&productId='+prodId;
			}
			
		}else{
			toShow=workPackageType;
			urlForListing='testrunjob.listbyBuildId?productBuildId='+productBuildId+'&workPackageType='+workPackageType;//check here to change
		}*/
		
		if($('#jTableContainerActivityTable').length > 0){
			$('#jTableContainerActivityTable').jtable("destroy");
		}		
	}catch(e){
		
	}
	$('#jTableContainerActivityTable').jtable({
		
			title: 'Activities', 
			editinline:{enable:false},
			paging: true, //Enable paging
		    pageSize: 10,
			actions: { 
				listAction: urlForListing
					},
			recordsLoaded: function(event, data) {
				//$('.portlet > .portlet-title > .tools > .reload').trigger('click');
				
				var flag=false;
	    		function checkForChanges()
	            {
	    			flag=false;
	    			if (!flag && $("#jTableContainerActivityTable .jtable").eq(0).css('width') != $("#jTableContainerActivityTable .jtable-title").eq(0).css("width"))
	                {
	                	$("#jTableContainerActivityTable .jtable-title").eq(0).css("width" , $("#jTableContainerActivityTable .jtable").eq(0).css('width'));
	                    $("#jTableContainerActivityTable .jtable-bottom-panel").eq(0).css("width" , $("#jTableContainerActivityTable .jtable").eq(0).css('width'));                     
	                    flag=true;
	                }
	            }
	        	setInterval(checkForChanges, 100);								
				
			 },
			fields: { 
				activityId : {
					key : true,
					list : false,
					create : false
				},
				activityName:{
	                 title: 'Activity Name',
	                 inputTitle: 'Activity Name <font color="#efd125" size="4px">*</font>',
	            	 list:true,
	            	 edit:false,
	            	 create:false,
	                 width:"5%"
	             },
				activityWorkPackageName : {
					title : 'Work Package',
					list : true,
					 edit:false,
	            	 create:false
				},
				activityTrackerNumber : {
					title : 'Tracker Number',
					list : true,
					edit : true,
					width : "20%"
				},
				 productFeatureName : {
						title : 'Requirement',											
						list : true,
						 edit:false,
		            	 create:false,					    	
		      		}, 
				activityMasterName : {
					title : 'Activity Type',
					list : true,
					 edit:false,
	            	 create:false,
					width : "25%",
					//options:function(data){
					//return 'process.list.activity.type?&activityWorkPackageId='+activityWorkPackageId;
		     		//}
				/* 	options:function () {
                return  activityMasterOptionsArr; //Cache results and return options
				} */
				},
				categoryName : {
					title : 'Category',										
					list : true,
					 edit:false,
	            	 create:false,
				},	
				assigneeName : {
					title : 'Assignee',										
					list : true,
					 edit:false,
	            	 create:false,
	             },
				reviewerName : {
					title : 'Reviewer',
					list : true,
					 edit:false,
	            	 create:false,
					width : "20%",							
					
				},
				priorityName : {
					title : 'Priority',										
					list : true,
					 edit:false,
	            	 create:false,
					width : "20%",
					//options : 'administration.executionPriorityList'
		/* 			options:function () {
return  priorityOptionsArr; //Cache results and return options
}
 */				},
/*  statusName : {
					title : 'Status',
					
					list : true,
					 edit:false,
	            	 create:false,
					width : "20%",
					//options:function(data){
					//return 'activity.primary.status.master.option.list?productId='+productId;
		     		//}
					 options:function () {
return  activityPrimaryOptionsArr; //Cache results and return options
}
				},  */
				statusCategoryId : {
					title : 'Status',
					create:false,
					list : false,
					edit : false,
					defaultValue : 1,
					options:function(data){
					 	return 'status.category.option.list';
		     		}
				},
				remark : {
					title : 'Remarks',
					type: 'textarea',  
					list : true,
					create : true,
					edit : true,
					width : "10%",
					width : "20%",
				},
				plannedStartDate : {
					title : 'Planned Start Date',										
					inputTitle : 'Planned Start Date <font color="#efd125" size="4px">*</font>',
					 edit:false,
	            	 create:false,
					list : true,
					type : 'date',
					width : "20%"
				},
				plannedEndDate : {
					title : 'Planned End Date',										
					inputTitle : 'Planned End Date <font color="#efd125" size="4px">*</font>',
					 edit:false,
	            	 create:false,
					 list : true,
				     type : 'date',
					width : "20%"
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
					 edit:false,
	            	 create:false,
					list : true,
					type : 'date',
					width : "20%"
				},
				plannedActivitySize : {
					title : 'Planned Activity Size',										
					list : true,				    										
					 edit:false,
	            	 create:false,
					width : "20%"
					
				},
				actualActivitySize : {
					title : 'Actual Activity Size',									
					list : true,				    										
					 edit:false,
	            	 create:false,
					width : "20%"
					
				},
	      	        
			}, 				
     });
	 
	 $('#jTableContainerActivityTable').jtable('load');
	 
		
}
/* function reviewAllTask(){			
    activityId = document.getElementById("treeHdnCurrentActivityId").value;			    
	var reviewItemsListsFromUI = [];
	var $selectedFeatureRows = '';			   
	//alert("activityId"+activityId);
	$selectedFeatureRows = $('#jTableContainer').jtable('selectedRows');
		if($selectedFeatureRows.length == 0){
			callAlert("Please select task(s) for bulk review");
		}
		else{

	$selectedFeatureRows.each(function () {
		    var record = $(this).data('record');
		    var taskId = record.activityTaskId;
		   // alert(record.isPeerReviewed);
		    reviewItemsListsFromUI.push(taskId);
		    
		});
				

	if(reviewItemsListsFromUI.length == null || reviewItemsListsFromUI ==''){
			callAlert("Select atleast one Task","ok");
		}				
		else{			
			
		
		$.post('activitytask.selfreviewall.update?reviewItemsListsFromUI='+reviewItemsListsFromUI,function(data){
			if(data.Result=="OK"){
			iosOverlay({
				text: "Selected task(s) are reviewed", // String
				icon: "css/images/check.png", // String (file path)
				spinner: null,
				duration: 500, // in ms
				});
			var reviewfilter = $("#reviewFilter_ul").find('option:selected').val();					
			var actionType=1;
		    listActivitiesOfSelectedActivity(activityId,actionType,reviewfilter);
			}
			else{
				iosOverlay({
					text: "Task(s) Not Reviewed", // String
					icon: "css/images/cross.png", // String (file path)
					spinner: null,
					duration: 500, // in ms
					});
			}
		
			});
		}
	}
} */

/* var isFirstLoad=true;
function setDefaultnode() {			
	var nodeFlag = false;			
	if(isFirstLoad) {
		$("#treeContainerDiv").on("loaded.jstree",function(evt, data) {
			$.each($('#treeContainerDiv li'), function(ind, ele){
				if($.jstree.reference('#treeContainerDiv').is_parent($(ele).attr("id"))){
					defaultNodeId = $(ele).attr("id");							
					isFirstLoad = false;
					
					$.jstree.reference('#treeContainerDiv').deselect_all();
					$.jstree.reference('#treeContainerDiv').close_all();
					$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
					$.jstree.reference('#treeContainerDiv').trigger("select_node");							
					//return false;
				}
			});	
			//setDefaultnode();
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
} */

function validateNodeLength(nodeArray){
	var flag = false;			
	if(nodeArray.children && nodeArray.children.length>0){
		flag=true;
	}
	return flag;
}

/* Load Poup function */
function loadPopup(divId) {
	$("#" + divId).fadeIn(0500); // fadein popup div
	$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
	// IE7, IE8
	$("#div_PopupBackground").fadeIn(0001);
}

/*
function listActivitiesOfSelectedActivityGrouping(activityId,actionType,reviewfilter,activityAssigneeId,activityReviewerId,activityWorkPackageId,prodId) {
	var urlToGetActivitiesOfSpecifiedActivityId = 'activitytask.list.for.review?activityId='+activityId+'&productId='+taskProductId;
	var productId= 1;
	var entityLevel="product";
	var entityLevelId=30;
	var entityId=1;
	
	try {
		if ($('#jTableContainerActivityTasks').length > 0) {
			$('#jTableContainerActivityTasks').jtable('destroy');
		}
	} catch (e) {
	}
	$('#jTableContainerActivityTasks')
			.jtable(
					{
						title : 'Activity Task',						
						editinline : {
							enable : true
						},								
						paging : true, //Enable paging								
						pageSize : 10,							  
						actions : {
							listAction : urlToGetActivitiesOfSpecifiedActivityId,
							createAction : 'process.activitytask.add',
							editinlineAction : 'process.activitytask.update',
						},						
						recordAdded: function (event, data) {
							if(data.serverResponse.Message!='undefined' && data.serverResponse.Message!=""){
								callAlert("Warning: "+data.serverResponse.Message)
							}						
						},
						recordsLoaded: function(event, data) {
							$('.portlet > .portlet-title > .tools > .reload').trigger('click');
							
							var flag=false;
				    		function checkForChanges()
				            {
				    			flag=false;
				    			if (!flag && $("#jTableContainerActivityTasks .jtable").eq(0).css('width') != $("#jTableContainerActivityTasks .jtable-title").eq(0).css("width"))
				                {
				                	$("#jTableContainerActivityTasks .jtable-title").eq(0).css("width" , $("#jTableContainerActivityTasks .jtable").eq(0).css('width'));
				                    $("#jTableContainerActivityTasks .jtable-bottom-panel").eq(0).css("width" , $("#jTableContainerActivityTasks .jtable").eq(0).css('width'));                     
				                    flag=true;
				                }
				            }
				        	setInterval(checkForChanges, 100);						
						 },
						fields : {						
							activityId : {
					 				type: 'hidden', 
					 				defaultValue: activityId 
							},
							activityTaskId : {
								key : true,
								list : false,
								create : false
							},
							activityTaskName:{
				                 title: 'Task Name',
				                 inputTitle: 'Activity Task <font color="#efd125" size="4px">*</font>',
				            	 list:true,
				            	 edit:false,
				            	 create:true,
				                 width:"20%"
				             },	
				             activityTaskTypeId : {
								title : 'Task Type',
								inputTitle: 'Task Type <font color="#efd125" size="4px">*</font>',
								create : true,
								list : true,
								edit : false,
								width : "20%",
								options : 'common.list.activity.tasktype?productId='+taskProductId
							},					            
							lifeCycleStageId : {
								title : 'Life Cycle Stage',
								create : true,
								list : true,
								edit : false,
								width : "20%",
								dependsOn : 'activityWorkpackageId',
								defaultValue : defaultTaskLifeCycleStage,
								options:function(data){
									var entityTypeId = 34;
								 	var entityId = 0;
								 	var entityInstanceId = activityWorkpackageIdForWF;
									return 'workflow.life.cycle.stages.options?entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId;
					     		},
							},
							plannedStartDate : {
								title : 'Planned Start Date',
								inputTitle : 'Planned Start Date <font color="#efd125" size="4px">*</font>',
								edit : allowPlanDateEndDate,
								create : allowPlanDateEndDate,
								list : true,
								type : 'date',
								defaultValue : defaultTaskPlannedStartDate,
								width : "20%",
							},
							plannedEndDate : {
								title : 'Planned End Date',
								inputTitle : 'Planned End Date <font color="#efd125" size="4px">*</font>',
								edit : allowPlanDateEndDate,
								list : true,
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
							visibleEventComment : {
								title : 'visibleComment',
								create : false,
								list : false,
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
								edit : false,
								defaultValue:activityAssigneeId,
								options : function(data) {
									return 'common.user.list.by.resourcepool.id.activityId?activityId='+activityId;
								}
							},
							reviewerId : {
								title : 'Reviewer',
								list : true,
								create : true,
								edit : false,
								width : "20%",
								defaultValue : activityReviewerId,
								options : function(data) {
									return 'common.user.list.by.resourcepool.id.activityId?activityId='+activityId;
								} 
							},
							priorityId : {
								title : 'Priority',									
								list : true,
								create : true,
								edit : false,
								width : "5%",
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
							activityMasterName : {
								title : 'Activity Type',
								list : true,
								edit : false,
								create : false
							}, 
							categoryId : {
								title : 'Category',										
								list : true,
								create : true,
								edit : false,
								options : function(data) {
									return 'common.list.executiontypemaster.byentityid?entitymasterid=1';
								}
							},								 
							 enviromentCombinationId:{										
								title : 'Environment Combination',										
								list : true,
								create : true,
								edit : false,
								 options : function(envdata) {
									 if (typeof activityWorkPackageId == "undefined") {
										 activityWorkPackageId=0;
									 }
									return 'environment.combinations.options.by.activity?activityId='+activityId+'&activityWorkPkgId='+activityWorkPackageId;
								} 
						   },						
							resultId : {
								title : 'Result',
								create : false,
								list : true,
								edit : false,
								width : "20%",
								options : 'process.list.activity.result'
							},
							remark : {
								title : 'Remarks',
								type: 'textarea',  
								list : true,
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
								edit : false,
								width : "20%"								
							},
							actualTaskSize : {
								title : 'Actual Task Size',										
								list : true,				    										
								create : false,
								edit : true,
								width : "20%"								
							},
							percentageCompletion : {
								title : '% Completion',
								create : false,
								list : true,
								edit : true,
								width : "4%"
							},
							totalEffort : {
    				        	title : 'Total Efforts',
    				        	create : false,
    				        	edit : false,
    				        	list : true,
    				        	width : "20%",    				        	
    				         },
    				        plannedTaskSize : {
				        		title : 'Planned Task Size',									
				        		list : true,				    										
				        		create : true,
				        		edit : false,
				        		width : "20%"				        		
				        	},
    				        actualTaskSize : {
				        		title : 'Actual Task Size',										
				        		list : true,				    										
				        		create : false,
				        		edit : true,
				        		width : "20%"				        		
				        	},
							workflowIndicator : {
								title : '',
								create : false,
								list : false,
								edit : false,
								width : "20%"
							},
							activityEffortTrackerId:{
    				        	title : '',
    				        	list : false,
    				        	create : false,
    				        	edit : false,
    				        	width: "10%",
    				        	display:function (data) { 
    				           		var $img = $('<i class="fa fa-history showHandCursor" title="Event History"></i>');  
    			           			//Open Testscript popup  
    			           			$img.click(function () {
    			           				addTaskCommentsGrouping(data.record);
    			           		  });
    			           			return $img;
    				        	}
    				        },							
							workflowEntityEffortTrackerId:{
    				        	title : '',
    				        	list : true,
    				        	create : false,
    				        	edit : false,
    				        	width: "10%",
    				        	display:function (data) { 
    				           		//Create an image for test script popup 
    								var $img = $('<i class="fa fa-history showHandCursor" title="Event History"></i>');
    			           			//Open Testscript popup  
    			           			$img.click(function () {
    			           				addTaskCommentsGrouping(data.record);
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
					        		var $img = $('<img src="css/images/workflow.png" title="Configure Workflow" />'); 
				           			$img.click(function () {
				           				workflowId = 0;
				           				entityTypeId = 30;
				           				statusPolicies(taskProductId, workflowId, entityTypeId, data.record.activityTaskTypeId, data.record.activityTaskId, data.record.activityTaskName, "Task", data.record.statusId);
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
										return 'workflow.master.mapped.to.entity.list.options?productId='+taskProductId+'&entityTypeId=30&entityId='+entityId;
						     		}
  					        },
      					      attachment: { 
								title: '', 
								list: true,
								create : false,
								edit:true,
								width: "5%",
								display:function (data) {	        		
					           		//Create an image that will be used to open child table 
									var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>'); 
					       			$img.click(function () {
					       				isViewAttachment = false;
					       				var jsonObj={"Title":"Attachments for Task",			          
					       					"SubTitle": 'Activity : ['+data.record.activityTaskId+'] '+data.record.activityTaskName,
					    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+taskProductId+'&entityTypeId=29&entityInstanceId='+data.record.activityTaskId,
					    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+taskProductId+'&entityTypeId=29&entityInstanceId='+data.record.activityTaskId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
					    	    			"updateURL": 'update.attachment.for.entity.or.instance',		
					    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
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
							commentsAWP:{
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
									var entityTypeIdComments = 35;
									var entityNameComments = "ActivityTask";
									listComments(entityTypeIdComments, entityNameComments, data.record.activityTaskId, data.record.activityTaskName, "activityTaskComments");
									});
								return $img;
							}
							},
						}, // This is for closing $img.click(function (data) { 
						 formSubmitting: function (event, data) {			
							 	data.form.find('input[name="activityTaskName"]').addClass('validate[required],custom[maxSize[500]]');									 
					      	  	//data.form.find('input[name="activityTaskName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[maxSize[500]]');
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
				});
	
		$('#jTableContainerActivityTasks').jtable('load');
}
*/

function listActivityTaskAuditHistory(activityTaskId){
clearSingleJTableDatas();
var url='administration.event.list?sourceEntityType=ActivityTask&sourceEntityId='+activityTaskId;
var jsonObj={"Title":"ActivityTask Audit History:",
	"url": url,	
	"jtStartIndex":0,
	"jtPageSize":10,	    
	"componentUsageTitle":"activityTaskAudit",
};
SingleJtableContainer.init(jsonObj);
}

/*
function listChangeRequestsOfSelectedActivity(activityId,productId) { 
	var urlToGetChangeRequestsOfSpecifiedActivityId = 'list.changerequests.by.entityTypeAndInstanceId?entityType1='+activityEntityTypeId+'&entityInstance1='+activityId;
	try {
		if ($('#jTableContainerforChangeRequests').length > 0) {
			$('#jTableContainerforChangeRequests').jtable('destroy');
		}
	} catch (e) {
	}
				$('#jTableContainerforChangeRequests').jtable( 
					{						                   	      	   
						title: 'Add/Edit Change Request',							                   	      
						editinline:{enable:true},       
						paging : true, //Enable paging								
					pageSize : 10,
					actions: { 
						listAction: urlToGetChangeRequestsOfSpecifiedActivityId,
						createAction : 'changerequests.add.by.entityTypeAndInstanceId?entityInstanceId='+activityId,
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
   					entityType1:{
						type: 'hidden',
						create: true, 
		                edit: true, 
   						list: true,
						defaultValue: activityEntityTypeId
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
						defaultValue: activityId,
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
									listGenericAuditHistory(data.record.changeRequestId,"ChangeRequest","changeRequestAudit");
								});
								return $img;
						}
					},
							commentsAWP:{
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
									var entityTypeIdComments = 42;
									var entityNameComments = "ChangeRequest";
									listComments(entityTypeIdComments, entityNameComments, data.record.changeRequestId, data.record.changeRequestName, "activityLevelCRComments");
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
				$('#jTableContainerforChangeRequests').jtable('load');
	}
*/

/*
function listClarificationOfSelectedActivity(activityId,productId,activityAssigneeId,testFactId) { 
	var urlToGetClarificationsOfSpecifiedActivityId = 'list.clarificationtracker.by.entityTypeAndInstanceIds?entityTypeId='+activityEntityTypeId+'&entityInstanceId='+activityId;
	
	
	try {
		if ($('#jTableContainerforClarification').length > 0) {
			$('#jTableContainerforClarification').jtable('destroy');
		}
	} catch (e) {
	}
	$('#jTableContainerforClarification').jtable( 
					{ 
						title: 'Add/Edit Clarification', 
					 	editinline:{enable:true},
					 editInlineRowRequestModeDependsOn: true,
					 paging : true, //Enable paging								
					pageSize : 10,
					actions: { 
						listAction: urlToGetClarificationsOfSpecifiedActivityId,
						createAction : 'add.clarificationtracker.by.activity',	    									
					editinlineAction : 'update.clarificationtracker.by.activity',
					deleteAction : 'delete.clarificationtracker',
						},
					recordsLoaded: function(event, data) {
		        		$(".jtable-edit-command-button").prop("disabled", true);      		        		
		         	},
	         	recordUpdated:function(event, data){
	         		$('#jTableContainerforClarification').find('.jtable-main-container .reload').trigger('click');
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
						defaultValue: activityEntityTypeId
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
							defaultValue: activityId
					}, 
						clarificationTrackerId: { 
   						key: true, 
   						title : 'ID',
   						create: false, 
		                edit: false, 
   						list: true
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
								return 'clarification.resolution.option.list?parentStatusId='+data.dependedValues.workflowStatusId;		
													
			     		}	 				
					
					}, 
					entityType : {
						title : 'Entity Type',
						list : false,				    										
						create : false,
						edit : false,
						width : "20%"
						
					}, entityInstanceName : {
						title : 'Entity Instance Name',
						list : false,				    										
						create : false,
						edit : false,
						width : "20%"
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
					commentsAWP:{
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
									listComments(entityTypeIdComments, entityNameComments, data.record.clarificationTrackerId, data.record.clarificationTitle, "activityLevelCTComments");
									});
								return $img;
							}
							},
					
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
                }, 
			 	}); 
	$('#jTableContainerforClarification').jtable('load');
}
*/

/*
function listActivityWorkflowPlan(productId, entityTypeIdSP, entityIdSP, entityInstanceIdSP, jTableContainerId){
	if(typeof entityIdSP == 'undefined' || entityIdSP == null){
		entityIdSP = 0;
	}
	getSourceWorkflowMapped(productId, entityTypeIdSP, entityIdSP, entityInstanceIdSP);
	jTableContainerId = '#'+jTableContainerId;
	try {
		if ($(jTableContainerId).length > 0) {
			$(jTableContainerId).jtable('destroy');
		}
	} catch (e) {
	}
	
	$(jTableContainerId).jtable({
		title: 'Workflow',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	                 
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: 'workflow.status.policy.list?isActive=1&productId='+productId+'&workflowId='+sourceWorkflowIdMapped+'&entityTypeId='+entityTypeIdSP+'&entityId='+entityIdSP+'&entityInstanceId='+entityInstanceIdSP+'&scopeType=Instance',
            //createAction: 'workflow.status.policy.add?workflowId='+workflowId,
            editinlineAction: 'workflow.status.policy.update?workflowId='+sourceWorkflowIdMapped,
		}, 
		recordUpdated:function(event, data){
			$(jTableContainerId).find('.jtable-child-table-container').jtable('reload');
		},
		recordAdded: function (event, data) {
			$(jTableContainerId).find('.jtable-child-table-container').jtable('reload');
		},
		recordDeleted: function (event, data) {
			$(jTableContainerId).find('.jtable-child-table-container').jtable('reload');
		},
		fields: {
			workflowStatusPolicyId: { 
				type: 'hidden', 
				edit: false,
			}, 
			workflowStatusName: { 
				title: 'Status Name',
				inputTitle: 'Status Name <font color="#efd125" size="4px">*</font>',
				list: false,
				edit : false,
				create: true,
			},
			workflowStatusDisplayName: { 
            	title: 'Status',
                list: true,
                edit : false,
                create: true,
   			},
   			statusActors:{
   				title: 'Actors',
                list: true,
                edit : false,
                create: true,
   			},
			plannedStartDate: { 
				title: 'Planned Start Date',
				list: true,
				edit : true,
				create: true,
			},
			plannedEndDate: { 
				title : 'Planned End Date',
				list: true,
				edit : true,
				create: true,
			},
			actualStartDate: { 
				title: 'Actual Start Date',
				list: true,
				edit : true,
				create: true,
			},
			actualEndDate: { 
				title : 'Actual End Date',
				list: true,
				edit : true,
				create: true,
			},
			plannedEffort : {
				title: 'Planned Effort',
				width: "10%",
				create : true,
				edit : true,
				list: true,
			},
			actualEffort : {
				title: 'Actual Effort',
				width: "10%",
				create : true,
				edit : true,
				list: true,
			},
			slaDuration: {
   				title: 'SLA Duration',
                list: false,
                edit : false,
                create: true,
   			},
   			usersMapping:{
	        	title : '',
	        	list : true,
	        	create : false,
	        	edit : true,
	        	width: "5%",
	        	display:function (data) { 
	           		//Create an image for test script popup 
	        		var $imgUser = $('<i class="fa fa-user" title="Users"></i>');
	        		$imgUser.click(function () {
	        			$imgUser = listUsers($imgUser, productId, data.record.workflowStatusId, entityTypeIdSP, entityIdSP, entityInstanceIdSP, jTableContainerId);
           		  });
           			return $imgUser;
	        	}
	        }, 
	        rolesMapping:{
	        	title : '',
	        	list : true,
	        	create : false,
	        	edit : true,
	        	width: "5%",
	        	display:function (data) { 
	           		//Create an image for test script popup 
	        		var $imgRole = $('<i class="fa fa-users" title="Roles"></i>');
	        		$imgRole.click(function () {
	        			$imgRole = listRoles($imgRole, productId, data.record.workflowStatusId, entityTypeIdSP, entityIdSP, entityInstanceIdSP, jTableContainerId);
           		  });
           			return $imgRole;
	        	}
	        },
					commentsAWP:{
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
									var entityTypeIdComments = 48;
									var entityNameComments = "WorkFlowStatusPolicy";
									listComments(entityTypeIdComments, entityNameComments, data.record.workflowStatusPolicyId, data.record.workflowStatusName, "activityLevelWFStatusComments");
									});
								return $img;
							}
							},
		}, 
		
		formSubmitting: function (event, data) {
			data.form.validationEngine();
			return data.form.validationEngine('validate');
		},  
		//Dispose validation logic when form is closed
		formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }
	}, function (data) { //opened handler 
		
	});
	$(jTableContainerId).jtable('load'); 
	//return $imgStatusPolicy; 
}
*/