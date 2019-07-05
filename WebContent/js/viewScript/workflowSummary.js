function jSingleTableInit(entityType, productName,title){
	var jsonObj={
			"Title":title+" : "+productName+" Product's "+entityType,          
	};
	SingleJtableContainer.init(jsonObj);
	clearSingleJTableDatas();
	
	$("#div_SingleJTableSummary .modal-header .row").css('display','none');
}

function listWorkflowSummarySLADetail(productId,productName,entityTypeId,workPackageId,lifeCycleStageId,workflowType,indicator) {
	var entityTypeName = "";
	var tableHeader="";
	var showEntity=false;
	if(entityTypeId == 30 || entityTypeId == 29){
		entityTypeName = "Tasks";
		showEntity= true;
		tableHeader='Activity Tasks';
	}else if(entityTypeId == 3){
		entityTypeName = "Testcases";
		tableHeader='Testcases';
	} else if(entityTypeId == 33 || entityTypeId == 28){
		entityTypeName = "Activities";
		tableHeader='Activities';
	} else if(entityTypeId == 34){
		entityTypeName = "Activity Workpackages";
		tableHeader='Activity Workpackages';
	}	
	

	if(typeof entityTypeId == 'undefined' || entityTypeId == null){
		entityTypeId = 0;
	}
	var title="";
	if(workflowType =="Status Workflow") {
		title="Workflow Summary";
	} else if(workflowType =="Life Cycle Workflow") {
		title="Workpackage RAG Summary";
	}
	
	jSingleTableInit(entityTypeName, productName,title);
	
	var urlToGetActivitiesOfSpecifiedActivityId ='workflow.summary.SLA.indicator.detail?productId='+productId+'&entityTypeId='+entityTypeId+'&lifeCycleEntityId='+workPackageId+'&lifeCycleStageId='+lifeCycleStageId+'&indicator='+indicator+'&workflowType='+workflowType;
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
										width : "7%"
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
									},									
									entityTypeName : {
										title : 'Entity Type',
										create : false,
										list : false,
										edit : false,
									},
									entityName : {
										title : 'Entity',
										create : false,
										list : showEntity,
										edit : false,
										width : "10%"
									},
								
									workflowStatusName : {
										title : 'Current Status',
										create : false,
										list : true,
										edit : false,
										width : "10%",
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
										width : "10%"
									},
									workflowIndicator : {
										title : '',
										create : false,
										list : true,
										edit : false,
										width : "5%"
									},
									
	        				        totalEffort : {
	        				        	title : 'Total Effort',
	        				        	create : false,
	        				        	edit : false,
	        				        	list : true,
	        				        	width : "15%",
	        				        	
	        				         },
	        				         activityEffortTrackerId:{
		        				        	title : '',
		        				        	list : true,
		        				        	create : false,
		        				        	edit : false,
		        				        	width: "5%",
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

var productId='';
function showEventHistory(record){	

	var entityInstanceName = record.instanceName;	
	var entityInstanceId = record.instanceId;
	var modifiedById = new Date();
	var currentStatusId = record.workflowStatusId;
	var currentStatusName = record.workflowStatusName;
	var entityTypeId = record.entityTypeId;
	var entityTypeName = record.entityTypeName;
	var entityId = record.entityId;
	var actionTypeValue = 0;
	//var secondaryStatusId = record.secondaryStatusId;
	var secondaryStatusId = 0;
	//var productId=document.getElementById("treeHdnCurrentProductId").value;
	if(document.getElementById("treeHdnCurrentProductId") !=null)
		productId= document.getElementById("treeHdnCurrentProductId").value;
	
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