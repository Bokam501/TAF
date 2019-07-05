
/*function jSingleTableInit(productName,entityType){
	var jsonObj={
			"Title":"Workflow Summary : "+productName+" Product's "+entityType,          
	};
	SingleJtableContainer.init(jsonObj);
	clearSingleJTableDatas();
	
	$("#div_SingleJTableSummary .modal-header .row").css('display','none');
}
*/
function jSingleTableInit(productName,entityType,titlename){
	if(titlename == " " || titlename == undefined){
		var jsonObj={
				"Title":"Workflow Summary : "+productName+" Product's "+entityType,          
		};
	}
	else{
		var jsonObj={
				"Title":"Workflow Summary - "+titlename+ " : "+productName+" Product's "+entityType,          
		};
	}
	
	
	SingleJtableContainer.init(jsonObj);
	clearSingleJTableDatas();
	
	$("#div_SingleJTableSummary .modal-header .row").css('display','none');
}

function listWorkflowSummarySLADetail(engagementId,productId,productName,entityTypeId,indicator,entityId,workflowStatusId,userId,roleId,typeId,titlename,entityParentInstanceId,workflowStatusCategoryId) {
	var entityTypeName = "";
	var tableHeader="";
	var workflowType="Status Workflow";
	var workPackageId=0;
	var lifeCycleStageId=0;
	var showEntity=false;
	if(entityTypeId == 30 ){
		entityTypeName = "Tasks";
		showEntity= true;
		tableHeader='Activity Tasks';
	}else if(entityTypeId == 3){
		entityTypeName = "Testcases";
		tableHeader='Testcases';
	}else if(entityTypeId == 33){
		entityTypeName = "Activities";
		tableHeader='Activities';
	}else if(entityTypeId == 34){
		entityTypeName = "Activity Workpackages";
		tableHeader='Activity Workpackages';
	}	
	jSingleTableInit(productName,entityTypeName);
	jSingleTableInit(productName,entityTypeName,titlename);
	var urlToGetActivitiesOfSpecifiedActivityId ="";
	if(indicator !="") {                         
		urlToGetActivitiesOfSpecifiedActivityId ='workflow.summary.SLA.indicator.detail?engagementId='+engagementId+'&productId='+productId+'&entityTypeId='+entityTypeId+'&lifeCycleEntityId='+workPackageId+'&lifeCycleStageId='+lifeCycleStageId+'&indicator='+indicator+'&workflowType='+workflowType+'&entityParentInstanceId='+entityParentInstanceId;
	}else{
		urlToGetActivitiesOfSpecifiedActivityId ='workflow.status.summary.instance.detail?engagementId='+engagementId+'&productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&workflowStatusId='+workflowStatusId+'&userId='+userId+'&roleId='+roleId+'&typeId='+typeId+'&entityParentInstanceId='+entityParentInstanceId+'&workflowStatusCategoryId='+workflowStatusCategoryId;
	}
			try {
				if ($('#JtableSingleContainer').length > 0) {
					$('#JtableSingleContainer').jtable('destroy');
				}
			} catch (e) {
			}
			$('#JtableSingleContainer')
					.jtable(
							{
								title : tableHeader,
								editinline : {
									enable : false
								},
								selecting : true, //Enable selecting 
								paging : true, //Enable paging
								pageSize : 10,
								
								actions : {
									listAction : urlToGetActivitiesOfSpecifiedActivityId,
								},
								
								fields : {
									
									instanceId : {
										title : 'Id',
										create : false,
										list : true,
										edit : false,
										width : "5%"
									},
									instanceName : {
										title : 'Name',
										create : false,
										list : true,
										edit : false,
										width : "20%"
									},
									productName : {
										title : 'Product',
										create : false,
										list : false,
										edit : false,
										width : "5%"
									},									
									entityTypeName : {
										title : 'Entity Type',
										create : false,
										list : false,
										edit : false,
										width : "5%"
									},
									entityName : {
										title : 'Entity',
										create : false,
										list : showEntity,
										edit : false,
										width : "15%"
									},
								
									workflowStatusName : {
										title : 'Current Status',
										create : false,
										list : true,
										edit : false,
										width : "15%",
										display: function(data){
											return data.record.workflowStatusName+"&nbsp;"+data.record.workflowStatusCategoryName;
										}
									},	
									actors : {
										title : 'Pending With',
										create : false,
										list : true,
										edit : false,
										width : "20%"
									},	
									completedBy : {
										title : 'Complete By',
										create : false,
										list : true,
										edit : false,
										width : "20%"
									},
									remainingHrsMins : {
										title : 'Time Left',
										create : false,
										list : true,
										edit : false,
										width : "5%"
									},
									workflowIndicator : {
										title : '',
										create : false,
										list : false,
										edit : false,
										width : "5%"
									},
									plannedSize : {
										title : 'Planned Size',
										create : false,
										list : true,
										edit : false,
										width : "5%"
									},
									
									actualSize : {
										title : 'Actual Size',
										create : false,
										list : true,
										edit : false,
										width : "5%"
									},
									
									plannedEffort : {
										title : 'Planned Effort',
										create : false,
										list : true,
										edit : false,
										width : "5%"
									},
								
	        				        totalEffort : {
	        				        	title : 'Actual Effort',
	        				        	create : false,
	        				        	edit : false,
	        				        	list : true,
	        				        	width : "10%",
	        				        	
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
		        			           				showEventHistory(data.record);
		        			           		  });
		        			           			return $img;
		        				        	}
		        				     },
	        				       
								}, 
								 
							});
			$('#JtableSingleContainer').jtable('load');
		}


function listWorkflowSummarySLARAGDetail(engagementId,productId,productName,entityTypeId,indicator,userId,roleId,entityTypeInstanceId,titlename) {
	var entityTypeName = "";
	var tableHeader="";
	var showEntity=false;
	var entityId=0;
	if(entityTypeId == 30 ){
		entityTypeName = "Tasks";
		showEntity= true;
		tableHeader='Activity Tasks';
	}else if(entityTypeId == 3){
		entityTypeName = "Testcases";
		tableHeader='Testcases';
	}else if(entityTypeId == 33){
		entityTypeName = "Activities";
		tableHeader='Activities';
	}else if(entityTypeId == 34){
		entityTypeName = "Activity Workpackages";
		tableHeader='Activity Workpackages';
	}	
	
	if(engagementId != 0 && productId == 0) {
		entityTypeInstanceId=0;
	}
	if(typeof entityTypeInstanceId == 'undefined'){
		entityTypeInstanceId=0;
	}
	jSingleTableInit(productName,entityTypeName);
	jSingleTableInit(productName,entityTypeName,titlename);
	var urlToGetSLARAGDetailView ="";
	if(userId ==0 && roleId == 0 && entityTypeInstanceId == 0) {		
		urlToGetSLARAGDetailView ='workflow.RAG.summary.SLA.indicator.detail?engagementId='+engagementId+'&productId='+productId+'&entityTypeId='+entityTypeId+'&indicator='+indicator;
		
	} else {
		urlToGetSLARAGDetailView ='workflow.RAG.status.instance.detail?engagementId='+engagementId+'&productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityTypeInstanceId='+entityTypeInstanceId+'&userId='+userId+'&roleId='+roleId+'&indicator='+indicator;
	}
			try {
				if ($('#JtableSingleContainer').length > 0) {
					$('#JtableSingleContainer').jtable('destroy');
				}
			} catch (e) {
			}
			$('#JtableSingleContainer')
					.jtable(
							{
								title : tableHeader,
								editinline : {
									enable : false
								},
								selecting : true, //Enable selecting 
								paging : true, //Enable paging
								pageSize : 10,
								
								actions : {
									listAction : urlToGetSLARAGDetailView,
								},
								
								fields : {
									
									instanceId : {
										title : 'Id',
										create : false,
										list : true,
										edit : false,
										width : "5%"
									},
									instanceName : {
										title : 'Name',
										create : false,
										list : true,
										edit : false,
										width : "20%"
									},
									productName : {
										title : 'Product',
										create : false,
										list : false,
										edit : false,
										width : "5%"
									},									
									entityTypeName : {
										title : 'Entity Type',
										create : false,
										list : false,
										edit : false,
										width : "5%"
									},
									entityName : {
										title : 'Entity',
										create : false,
										list : showEntity,
										edit : false,
										width : "15%"
									},
								
									workflowStatusName : {
										title : 'Current Status',
										create : false,
										list : true,
										edit : false,
										width : "15%",
										display: function(data){
											return data.record.workflowStatusName+"&nbsp;"+data.record.workflowStatusCategoryName;
										}
									},	
									actors : {
										title : 'Pending With',
										create : false,
										list : true,
										edit : false,
										width : "20%"
									},	
									completedBy : {
										title : 'Complete By',
										create : false,
										list : true,
										edit : false,
										width : "20%"
									},
									remainingHrsMins : {
										title : 'Time Left',
										create : false,
										list : true,
										edit : false,
										width : "5%"
									},
									workflowIndicator : {
										title : '',
										create : false,
										list : true,
										edit : false,
										width : "5%"
									},
									
									plannedSize : {
										title : 'Planned Size',
										create : false,
										list : true,
										edit : false,
										width : "5%"
									},
									
									actualSize : {
										title : 'Actual Size',
										create : false,
										list : true,
										edit : false,
										width : "5%"
									},
									
									plannedEffort : {
										title : 'Planned Effort',
										create : false,
										list : true,
										edit : false,
										width : "5%"
									},
									
	        				        totalEffort : {
	        				        	title : 'Actual Effort',
	        				        	create : false,
	        				        	edit : false,
	        				        	list : true,
	        				        	width : "10%",
	        				        	
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
		        			           				showEventHistory(data.record);
		        			           		  });
		        			           			return $img;
		        				        	}
		        				     },
	        				       
								}, 
								 
							});
			$('#JtableSingleContainer').jtable('load');
		}


function showEventHistory(row){	

	var entityInstanceName = row.data().instanceName;	
	var entityInstanceId = row.data().instanceId;
	var modifiedById = new Date();
	var currentStatusId = row.data().workflowStatusId;
	var currentStatusName = row.data().workflowStatusName;
	var entityTypeId = row.data().entityTypeId;
	var entityTypeName = row.data().entityTypeName;
	var entityId = row.data().entityId;
	var actionTypeValue = 0;
	//var secondaryStatusId = record.secondaryStatusId;
	var secondaryStatusId = 0;
	var productId=document.getElementById("treeHdnCurrentProductId").value;
	$("#addCommentsMainDiv").modal();			
	$('#addComments').hide();//Display only histroy of Effort history
	var jsonObj = {
			Title : "Workflow History: ["+entityInstanceId+"] "+entityInstanceName,	
			entityTypeName : entityTypeName,		
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
			currentStatusName : currentStatusName,
			secondaryStatusId : secondaryStatusId,
			effortListUrl : 'workflow.event.tracker.list?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
			actionTypeValue : actionTypeValue,
			commentsName : commentsReviewActivity,
			urlToSave : 'workflow.event.tracker.add?productId='+productId+'&entityId='+entityId+'&entityTypeId='+entityTypeId+'&primaryStatusId=[primaryStatusId]&secondaryStatusId=[secondaryStatusId]&effort=[effort]&comments=[comments]&sourceStatusId='+currentStatusId+'&approveAllEntityInstanceIds=[approveAllEntityIds]&entityInstanceId='+entityInstanceId+'&attachmentIds=[attachmentIds]&actionDate=[actionDate]',
	};
	AddComments.init(jsonObj);
}
