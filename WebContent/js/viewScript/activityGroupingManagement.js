var prodBuildId="";
var actWorkPackageId="";
/*
function listActivityWorkPackagesOfSelectedTestFactory(urlToGetWorkRequestsOfSpecifiedBuildId){
	try{
		if ($('#jTableContainerActivityWP').length>0) {
			 $('#jTableContainerActivityWP').jtable('destroy'); 
		}
	}catch(e){}
	
	$('#jTableContainerActivityWP').jtable({
        title: 'Activity WorkPackages',
        editinline:{enable:true},
        selecting: true,  //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10,
		recordAdded: function (event, data) {
             if(data.serverResponse.Message!='undefined' && data.serverResponse.Message!=""){
					callAlert("Warning: "+data.serverResponse.Message);
					jQuery('#treeContainerDiv').jstree(true).refresh(true);
			}          				         
		},        
         actions: {
        	  listAction:urlToGetWorkRequestsOfSpecifiedBuildId,
       	 	  createAction:'process.workrequest.add',
       	      editinlineAction: 'process.workrequest.update',
       	      deleteAction: 'process.workrequest.delete',
         },  
         fields : {
        	activityWorkPackageId: {
        		title: 'Work Package Id', 
	                 key: true,
	                 list: true,
	                 edit: false,
	                 create: false
	             },				 
				productVesionBuildName: {
					title: 'Product/Version/Build',        	
	                 list: true,
	                 create: false,
	                 edit: false,
	             },	
	             activityWorkPackageName:{
	                 title: 'Work Package Name',
	                 inputTitle: 'Activity Package Name <font color="#efd125" size="4px">*</font>',
	            	 list:true,
	            	 edit:true,
	                 width:"20%"
	             },
				plannedStartDate : {
					title : 'Planned Start Date',
					inputTitle : 'Planned Start Date <font color="#efd125" size="4px"></font>',
					edit : true,
					create : true,
					list : true,
					type : 'date',
					defaultValue : new Date().getMonth()+1+"/"+new Date().getDate()+"/"+new Date().getFullYear(),										
					width : "20%"
				},
				plannedEndDate : {
					title : 'Planned End Date',
					inputTitle : 'Planned End Date <font color="#efd125" size="4px"></font>',
					edit : true,
					list : true,
					create : true,
					type : 'date',
					defaultValue : new Date().getMonth()+1+"/"+new Date().getDate()+"/"+new Date().getFullYear(),
					width : "20%"
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
				actorIds : {
					title : 'Status Pendig with IDs',
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
	             description:{
	                 title: 'Description',
	            	 list:false,
	            	 type: 'textarea',    
	            	 create:true,
	            	 edit:true,
	                 width:"20%"
	             },
	             ownerId : {
					title : 'Owner',
					inputTitle: 'Owner <font color="#efd125" size="4px">*</font>',
					list : true,
					create:true,
					edit:true,
					options:function(data){
						if(productId == 0){
							if(data.record.productId != null){
								productId = data.record.productId;
							}
						}
				     	return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
				    }
				},
				productBuildId: {
	                 title: 'Build',
	             	inputTitle: 'Build <font color="#efd125" size="4px">*</font>',
	                 list:true,
		             edit:false,
		             create:true,
					options:function(data){
						if(productId == 0){
							if(data.record.productId != null){
								productId = data.record.productId;
							}
						}
				     	return 'process.list.builds.by.product?productId='+productId
				    }	                 
	             },
			    priorityId : {
					title : 'Priority',					
					list : true,
					create:true,
					edit:true,
					options: 'administration.executionPriorityList'
				},
				competencyId : {
					title: 'Competency',
		           	options:function(data){
				        return 'dimension.list.options?dimensionTypeId=1';
				    },
				},        
				 remark:{
	            	 title: 'Remarks',
	            	 type: 'textarea',  
	            	 list:true,
	                 width:"10%",
	                 list:false
	             },
	            
				actualStartDate : {
					title : 'Actual Start Date',
					create : false,
					edit : true,
					list : true,
					type : 'date',
					width : "20%"
				},
	             actualEndDate:{
	                 title: 'Actual End Date',
	            	 create:false,
	       			 edit: true, 
					 list: false,
	       			 type: 'date',
	       			 width: "20%"	                 
	             },
			      totalEffort : {
		        	title : 'Total Effort',
		        	create : false,
		        	edit : false,
		        	list : true,
		        	width : "20%",		        	
		         },
				percentageCompletion : {
					title : '% Completion',
					create : false,
					list : true,
					edit : true,
					width : "4%"
				},
				workflowIndicator : {
					title : '',
					create : false,
					list : false,
					edit : false,
					width : "20%"
				},
		        isActive: { 
                 	title: 'Active',
                 	list:true,
     				edit:true,
	  				create:false,
	  				type : 'checkbox',
	  				values: {'0' : 'No','1' : 'Yes'},
	  		    	defaultValue: '1'
		    	}, 
	             CloningAWP:{
						title : '',
						list : true,
						create : false,
						width: "5%",
						display:function (cloneData) { 
						 var $img = $('<img src="css/images/cloning.jpg" style="width: 24px;height: 24px;" title="Cloning Activity Workpackage" data-toggle="modal" />'); 
						$img.click(function () {
							openLoaderIcon();
							// -- for cloning workpackage ---
								$.ajax({
									type: "POST",
							        contentType: "application/json; charset=utf-8",
							        url: 'administration.product.hierarchy.tree',
									dataType : 'json',
									complete : function(data){
										console.log('complete');
										closeLoaderIcon();
									},
									success : function(data) {
										treeData = data;
										$('#plannedDateDiv').show();
										closeLoaderIcon();
										var jsonActivityWorkPackageCloningObj = {
         									"title": "Cloning Activity WorkPackage",
         									"packageName":"Activity WorkPackage Name",
         									"startDate" : dateFormat(cloneData.record.plannedStartDate),
        									"endDate" : dateFormat(cloneData.record.plannedEndDate),
         									"selectionTerm" : "Select Build",
         									"sourceParentID": cloneData.record.productBuildId,
											"sourceParentName": cloneData.record.productBuildName,
         									"sourceID": cloneData.record.activityWorkPackageId,
         									"sourceName": cloneData.record.activityWorkPackageName,
         									"componentUsageTitle":"activityWorkpackageClone",
         							}
         							Cloning.init(jsonActivityWorkPackageCloningObj);
									},
									error: function (data){
										console.log('error');
										closeLoaderIcon();
									}
								});				
						});
						return $img; 
					}
	             },
	             MovingAWP:{
						title : '',
						list : true,
						create : false,
						width: "5%",
						display:function (cloneData) { 
						 var $img = $('<i class="fa fa-scissors" style="width: 24px;height: 24px;" title="Moving Activity Workpackage" data-toggle="modal" />'); 
						$img.click(function () {
							openLoaderIcon();
							$('#plannedDateDiv').hide();
							// -- for cloning workpackage ---
								$.ajax({
									type: "POST",
							        contentType: "application/json; charset=utf-8",
							      //  url: 'administration.product.hierarchy.tree',
							        url: 'administration.product.hierarchy.tree.by.productId?productId='+cloneData.record.productId,
									dataType : 'json',
									complete : function(data){
										console.log('complete');
										closeLoaderIcon();
									},
									success : function(data) {
										treeData = data;
										closeLoaderIcon();
										var jsonActivityWorkPackageCloningObj = {
      									"title": "Moving Activity WorkPackage",
      									"packageName":"Activity WorkPackage Name",
      									"startDate" : dateFormat(cloneData.record.plannedStartDate),
     									"endDate" : dateFormat(cloneData.record.plannedEndDate),
      									"selectionTerm" : "Select Build",
      									"sourceParentID": cloneData.record.productBuildId,
											"sourceParentName": cloneData.record.productBuildName,
      									"sourceID": cloneData.record.activityWorkPackageId,
      									"sourceName": cloneData.record.activityWorkPackageName,
      									"componentUsageTitle":"activityWorkpackageMove",
      							}
      							Cloning.init(jsonActivityWorkPackageCloningObj);
									},
									error: function (data){
										console.log('error');
										closeLoaderIcon();
									}
								});				
						});
						return $img; 
					}
	             },
	             RCN:{
					title : '',
					list : true,
					create : false,
					edit : false,
					width: "10%",
					display:function (data) { 
						//Create an image that will be used to open child table 
						
						if(useCaseAWP == "YES"){
							var $img = $('<img src="css/images/mapping.png" title="Activity Creation from Use Cases" data-toggle="modal" />');
							$('#prodChangeRequestsImg').attr('title', 'Download Template - Use Cases');
						}else{		
							var $img = $('<img src="css/images/mapping.png" title="Activity Creation from ChangeRequest" data-toggle="modal" />');
							$('#prodChangeRequestsImg').attr('title', 'Download Template - Change Requests');
							
						}
						$img.click(function () {
							prodBuildId=data.record.productBuildId;
							actWorkPackageId = data.record.activityWorkPackageId;
							loadChangeRequestsByProductId(prodId,prodBuildId,actWorkPackageId);	           				
					    });
					    return $img;
				    }
			     },		     
			     workPackageMapWithUser:{
						title : '',
						list : true,
						create : false,
						edit : false,
						width: "10%",
						display:function (data) { 
							//Create an image that will be used to open child table 
							var $img = $('<img src="css/images/user.png" title="WorkPackage and User Mapping" data-toggle="modal" />'); 
							//Open child table when user clicks the image 
							$img.click(function () {

								prodBuildId=data.record.productBuildId;
								actWorkPackageId = data.record.activityWorkPackageId;
								awName = data.record.activityWorkPackageName;
								workPackageUserMapping($img, data.record.productId, 34, actWorkPackageId,awName);
								
						    });
						    return $img;
					    }
				     },			     
			     activityEffortTrackerId:{
		        	title : '',
		        	list : true,
		        	create : false,
		        	edit : false,
		        	width: "5%",
		        	display:function (data) { 
		           		//Create an image for test script popup 
		           		var $img = $('<i class="fa fa-history showHandCursor" title="Event History"></i>');  
	           			//Open Testscript popup  
	           			$img.click(function () {
	           				addActivityWorkpackageComments(data.record);
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
		           		//Create an image for test script popup 
		        		var $img = $('<img src="css/images/workflow.png" title="Configure Workflow" />'); 
	           			$img.click(function () {
	           				workflowId = 0;
	           				entityTypeId = 34;
	           				entityId = 0;
	           				statusPolicies(productId, workflowId, entityTypeId, entityId, data.record.activityWorkPackageId, data.record.activityWorkPackageName, "Activity Workpackage", data.record.statusId);
	           		  });
	           			return $img;
		        	}
		        },
		        workflowId:{
		        	title : 'WorkFlow Template',
		        	list:false, 
	     	  		create:true,
	     	  		edit:false,
					options:function(data){							
						var entityId = 0;
						return 'workflow.master.mapped.to.entity.list.options?productId='+productId+'&entityTypeId=34&entityId='+entityId;
		     		}
		        },
		        attachment: { 
					title: '', 
					list: true,
					edit: false,
					create:false,
					width: "25%",
					display:function (data) {	        		
		           		//Create an image that will be used to open child table 
						var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>'); 
		       			$img.click(function () {
		       				isViewAttachment = false;
		       				var jsonObj={"Title":"Attachments for Activity Workpackage",			          
		       					"SubTitle": 'Activity Workpackage : ['+data.record.activityWorkPackageId+'] '+data.record.activityWorkPackageName,
		    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=34&entityInstanceId='+data.record.activityWorkPackageId,
		    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=34&entityInstanceId='+data.record.activityWorkPackageId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
		    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
		    	    			"updateURL": 'update.attachment.for.entity.or.instance',
		    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=34',
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
					width: "5%",
					display:function (data) { 
						//Create an image for test script popup 
						var $img = $('<i class="fa fa-search-plus" title="Audit History"></i>');
						$img.click(function () {						
							listActivityWorkPackageAuditHistory(data.record.activityWorkPackageId);
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
							var entityTypeId = 34;
							var entityName = "ActivityWorkpackage";
						listComments(entityTypeId,entityName,data.record.activityWorkPackageId,data.record.activityWorkPackageName, "testFactoryLevelAWPComments");
						});
					return $img;
					}
				 },   
        },  // This is for closing $img.click(function (data) { 
        formSubmitting: function (event, data) {
      	  	data.form.find('input[name="activityWorkPackageName"]').addClass('class="validate[required,funcCall[validateSpecialCharactersExceptDot]]", custom[maxSize[100]]');
            data.form.find('select[name="ownerId"]').addClass('validate[required]');
           // data.form.find('input[name="plannedStartDate"]').addClass('validate[required]');
           // data.form.find('input[name="plannedEndDate"]').addClass('validate[required]');
            data.form.validationEngine();
           return data.form.validationEngine('validate');
          }, 
		});
	 $('#jTableContainerActivityWP').jtable('load');	
	 
	 $("button[role='button']:contains('Close')").click(function () {
	        $('.ui-dialog').filter(function () {
	            return $(this).css("display") === "block";
	        }).find('#ui-error-dialog').dialog('close');
	    });
}
*/

var activityselectedTabAtEngLevel='0';

$(document).on('click', '#tablistEngagementACTWP>li', function(){
	activityselectedTabAtEngLevel=$(this).index();	
	showActivityAtEngagementLevel(activityselectedTabAtEngLevel);
});

function loadChangeRequestsByProductId(productId){	
	loadPopup("div_PopupMainrolechange");
	
	if(useCaseAWP == "YES"){
		$("#div_PopupMainrolechange .modal-title").text("Create Activity from Use Cases");		
	}else{		
		$("#div_PopupMainrolechange .modal-title").text("Create Activity from CR");
	}	
	
	$("#div_PopupMainrolechange .green-haze").text("Create Activity");
	$('#role_types1').empty();
		$.post('unmapped.changerequest.list.by.productId?productId='+productId+'&jtStartIndex=0&jtPageSize=10',function(data) {	
			var ary = (data.Records);
			if(ary.length <= 0) {
				callAlert("No Change Request are exists");
			}
	        $.each(ary, function(index, element) {
	        	$('#role_types1').append('<label><input type="checkbox" name="radio1" class="icheck" id="' + element.itemId + '" data-radio="iradio_flat-grey">'+element.itemName+'</label>');
			});   
	});
}

function submitRadioBtnHandler(){
	var changeRequestIdFromUI = []; 
   $("input:checkbox[name=radio1]:checked").each(function(){
	   changeRequestIdFromUI.push($(this).attr('id'));
	});
    if(confirm("Are you sure to create activity based on changeRequest")){
	    $.ajax({
		    type: "POST",
		    data: {changeRequestIdFromUI:changeRequestIdFromUI},
		    url: "add.activity.by.changerequest?changeRequestIdFromUI="+changeRequestIdFromUI+"&prodBuildId="+prodBuildId+"&actWorkPackageId="+actWorkPackageId+"&productId="+productId,
		    success: function(data) {
		    	$.unblockUI();
		    	if(data.Result=="ERROR"){
 		    		callAlert("Activity Names: "+data.Message+" are already exists");
 		    	}else{
 		    		callAlert("Activities saved successfully");			    	
 		    	}
		    },
		    error: function(data){
		    	callAlert(data.Message);
		    },
		    complete:function(data){
		    }
		});
	  }
    }

/*- ----will be replaced in future ---------  */

function listActivityWorkPackageAuditHistory(activityWorkPackageId){
	clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=ActivityWorkpackage&sourceEntityId='+activityWorkPackageId;
	var jsonObj={"Title":"ActivityWorkPackage Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10,	    
			"componentUsageTitle":"activityWorkPackageAudit",
	};
	SingleJtableContainer.init(jsonObj);
}

function listComments(entityTypeId, entityName, instanceId, instanceName, componentUsageTitle){

	var url='comments.for.entity.or.instance.list?productId=0&entityTypeId='+entityTypeId+'&entityInstanceId='+instanceId+'&jtStartIndex=0&jtPageSize=10000';
	//var instanceId = row.data().productId;
	var jsonObj={"Title":"Comments on "+entityName+ ": " +instanceName,
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,
			"componentUsageTitle":componentUsageTitle,			
			"entityTypeId":entityTypeId,
			"entityInstanceId":instanceId,
			"productId":document.getElementById("treeHdnCurrentProductId").value,
	};
	CommentsMetronicsUI.init(jsonObj);
}
function addActivityWorkpackageComments(record){	

	var entityInstanceName = record.activityWorkPackageName;	
	var entityInstanceId = record.activityWorkPackageId;
	var modifiedById = record.ownerId;
	var currentStatusId = record.statusId;
	var currentStatusName = record.statusName;
	var entityTypeId = 34;//Activity type
	var entityId = 0;
	var actionTypeValue = 0;
	var secondaryStatusId = record.secondaryStatusId;
	var visibleEventComment=record.visibleEventComment;
	
	$("#addCommentsMainDiv").modal();		
	if(!visibleEventComment) {
		$('#addComments').hide();//Display only histroy of task Effort
	}
	
	var jsonObj = {
			Title : "Activity Workflow History: ["+entityInstanceId+"] "+entityInstanceName,	
			entityTypeName : 'Activity',		
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
			// commentsStatus: "['started','InProgress','Completed']",		
	};
	AddComments.init(jsonObj);
}

//Task Methods Starts ---- Mamtha

function listActivitiesOfSelectedActivity(productId,productVersionId,productBuildId,activityWorkPackageId,activityId,isActive,enableAddOrNot,activityAssigneeId,activityReviewerId) {
	activityTaskRecordArr=[];
	
	var urlToGetActivitiesOfSpecifiedActivityId = 'process.activitytask.list?productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&activityWorkPackageId='+activityWorkPackageId+'&activityId='+activityId+'&isActive='+isActive;

			try {
				if ($('#jTableContainerActivityTaskWP').length > 0) {
					$('#jTableContainerActivityTaskWP').jtable('destroy');
				}
			} catch (e) {
			}
			$('#jTableContainerActivityTaskWP')
					.jtable(
							{
								title : 'Tasks',
								editinline : {
									enable : true
								},
								selecting : true, //Enable selecting 
								paging : true, //Enable paging
								pageSize : 10,								
								actions : {
									listAction : urlToGetActivitiesOfSpecifiedActivityId,
									createAction : 'process.activitytask.add',
									editinlineAction : 'process.activitytask.update',
									deleteAction : 'process.activitytask.delete',
								},
								
								recordAdded: function (event, data) {
									if(data.serverResponse.Message!='undefined' && data.serverResponse.Message!=""){
										callAlert("Warning: "+data.serverResponse.Message);
									}
								},
								 recordsLoaded:function(){    	
									 $('.portlet > .portlet-title > .tools > .reload').trigger('click'); 
										//console.log("enableAddOrNot"+enableAddOrNot);
										if(enableAddOrNot == "no"){
											$('#jTableContainerActivityTaskWP .jtable-toolbar-item-add-record').hide();
											$('#divUploadActivitiesTask').hide();
										 }
								     },
								fields : {
									activityId : {
							 				type: 'hidden', 
							 				defaultValue: activityId 
									},
									activityTaskId : {
										key : true,
										list : false,
										create : false,
									},
									activityTaskName:{
						                 title: 'Task Name',
						                 inputTitle: 'Activity Task <font color="#efd125" size="4px">*</font>',
						            	 list:true,
						            	 edit:true,
						            	 create:true,
						                 width:"20%"
						             },
								      activityTaskTypeId : {
										title : 'Task Type',
										inputTitle: 'Task Type <font color="#efd125" size="4px">*</font>',
										create : true,
										list : true,
										edit : true,
										width : "20%",
										//options : 'common.list.activity.tasktype'
										options:function () {
                    						return  activityTaskOptionsArr; //Cache results and return options
										}
									},
									lifeCycleStageId : {
										title : 'Life Cycle Stage',
										create : true,
										list : true,
										edit : true,
										width : "20%",
										dependsOn : 'activityWorkPackageId',
										defaultValue : defaultTaskLifeCycleStage,
										options:function(data){
										 	var entityTypeId = 34;
										 	var entityId = 0;
										 	var entityInstanceId = data.dependedValues.activityWorkPackageId;
										 	if(typeof entityInstanceId == 'undefined'){
										 		entityInstanceId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
										 	}
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
										create : allowPlanDateEndDate,
										list : true,
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
										list :true ,
										create : true,
										edit :true,
										defaultValue : activityAssigneeId,
										options : function(data) {
											return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
										}
									},
									reviewerId : {
										title : 'Reviewer',
										list : true,
										create : true,
										edit : true,
										width : "20%",
										defaultValue : activityReviewerId,
										options : function(data) {
											return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
										}
									},
									priorityId : {
										title : 'Priority',									
										list : true,
										create : true,
										edit : true,
										width : "20%",
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
									categoryId : {
										title : 'Category',									
										list : true,
										create : true,
										edit : true,

										options : function(data) {
											return 'common.list.executiontypemaster.byentityid?entitymasterid=1';
										}
									},								 
									 enviromentCombinationId:{									
										title : 'Environment Combination',									
										list : true,
										create : true,
										edit : true,
										options : function(envdata) {
											return 'environment.combinations.options.by.activity?activityId='+activityId+'&activityWorkPkgId='+activityWorkPackageId;
										}
								   }, 
									resultId : {
										title : 'Result',
										create : false,
										list : true,
										edit : true,
										width : "20%",
										options : 'process.list.activity.result'
									},
									remark : {
										title : 'Remarks',
										type: 'textarea',  
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
										edit : true,
										width : "20%"
										
									},
									actualTaskSize : {
										title : 'Actual Task Size',										
										list : true,				    										
										create : false,
										edit : true,
										width : "20%"
										
									},
									plannedEffort : {
										title : 'Planned Effort',									
										list : true,
										create : true,
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
									workflowIndicator : {
										title : '',
										create : false,
										list : false,
										edit : false,
										width : "20%"
									},
	        				        totalEffort : {
	        				        	title : 'Total Effort',
	        				        	create : false,
	        				        	edit : false,
	        				        	list : true,
	        				        	width : "20%",
	        				        	
	        				         },
	        				         isActive: { 
	                                   	title: 'Active',
	                                   	list:true,
	                       				edit:true,
	                    				create:false,
	                    				type : 'checkbox',
	                    				values: {'0' : 'No','1' : 'Yes'},
	                    		    	defaultValue: '1'
                    		    	},
	                   	             CloningTask:{
		             						title : '',
		             						list : true,
		             						create : false,
		             						width: "5%",
		             						display:function (cloneData) { 
		             						 var $img = $('<img src="css/images/cloning.jpg" style="width: 24px;height: 24px;" title="Cloning Activity Task" data-toggle="modal" />'); 
		             						$img.click(function () {
							openLoaderIcon();
							// -- for cloning workpackage ---
								$.ajax({
									type: "POST",
							        contentType: "application/json; charset=utf-8",
							        url: 'administration.product.activity.tree?actionType=3',
									dataType : 'json',
									complete : function(data){
										console.log('complete');
										closeLoaderIcon();
									},
									success : function(data) {
										treeData = data;
										closeLoaderIcon();var jsonActivityTaskCloningObj = {
             									"title": "Cloning Activity Task",
             									"packageName":"Activity Task Name",
             									"startDate" : dateFormat(cloneData.record.plannedStartDate),
            									"endDate" : dateFormat(cloneData.record.plannedEndDate),
             									"selectionTerm" : "Select Activity",
             									"sourceParentID": cloneData.record.activityId,
             									"sourceParentName": cloneData.record.activityName,
             									"sourceID": cloneData.record.activityTaskId,
             									"sourceName": cloneData.record.activityTaskName,
             									"componentUsageTitle":"activityTaskClone",
             							}
         							Cloning.init(jsonActivityTaskCloningObj);
										
									},
									error: function (data){
										console.log('error');
										closeLoaderIcon();
									}
								});
     						});
		             						return $img; 
		             					}
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
	        			           				addTaskComments(data.record);
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
        					           		//Create an image for test script popup 
        					        		var $img = $('<img src="css/images/workflow.png" title="Configure Workflow" />'); 
        				           			$img.click(function () {
        				           				workflowId = 0;
        				           				entityTypeId = 30;
        				           				statusPolicies(productId, workflowId, entityTypeId, data.record.activityTaskTypeId, data.record.activityTaskId, data.record.activityTaskName, "Task", data.record.statusId);
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
											return 'workflow.master.mapped.to.entity.list.options?productId='+productId+'&entityTypeId=30&entityId='+entityId;
							     		}
        					        },
        					        attachment: { 
										title: '', 
										list: true,
										create:false,
										edit:false,
										width: "10%",
										display:function (data) {	        		
							           		//Create an image that will be used to open child table 
											var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>'); 
							       			$img.click(function () {
							       				isViewAttachment = false;
							       				var jsonObj={"Title":"Attachments for Task",			          
							       					"SubTitle": 'Task : ['+data.record.activityTaskId+'] '+data.record.activityTaskName,
							    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=29&entityInstanceId='+data.record.activityTaskId,
							    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=29&entityInstanceId='+data.record.activityTaskId+'&description=[description]&attachmentType=[attachmentType]',
							    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
							    	    			"updateURL": 'update.attachment.for.entity.or.instance',
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
									},//tell
									commentsTask:{
										title : '',
										list : true,
										create : false,
										edit : false,
										width: "5%",
									  display:function (data) { 
									//Create an image for test script popup 
										var $img = $('<i class="fa fa-comments" title="Comments"></i>');
										$img.click(function () {
										var entityTypeIdComments = 35;
										var entityNameComments = "ActivityTask";
										listComments(entityTypeIdComments, entityNameComments, data.record.activityTaskId, data.record.activityTaskName, "actLevelTaskComments");
										});
										return $img;
									  }
									},  
								}, 
								 formSubmitting: function (event, data) {							   
									// data.form.find('input[name="activityTaskName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[maxSize[500]]');
									 	data.form.find('input[name="activityTaskName"]').addClass('class="validate[required,funcCall[validateSpecialCharactersExceptDot]]", custom[maxSize[500]]');
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
								// This is for closing $img.click(function (data) {  
							});
			$('#jTableContainerActivityTaskWP').jtable('load');
			
			$("button[role='button']:contains('Close')").click(function () {
		        $('.ui-dialog').filter(function () {
		            return $(this).css("display") === "block";
		        }).find('#ui-error-dialog').dialog('close');
		    });
			
		}


var statusOptionsArr= [];
var activityTaskOptionsArr= [];

function statusOption(url, value){
	 
    $.ajax({ //Not found in cache, get from server
        url: url,
        type: 'POST',
        dataType: 'json',
        async: false,
        success: function (data) {
            if (data.Result != 'OK') {
            	callAlert(data.Message);
                return;
            }
            if(value == "primaryStatus"){
            	statusOptionsArr = data.Options;            	
            }else{            
            	activityTaskOptionsArr = data.Options; 
            }
        },
        error: function (data){
        	console.log("error");
        }
    });
	//return optionsArr;
}		

function addTaskComments(record){
	var entityInstanceName = record.activityTaskName;	
	var entityInstanceId = record.activityTaskId;
	var modifiedById = record.assigneeId;
	var currentStatusId = record.statusId;
	var currentStatusName = record.statusName;
	var currentStatusDisplayName = record.statusDisplayName;
	var entityTypeId = 30;//Activity task type
	var entityId = record.activityTaskTypeId;
	var actionTypeValue = 0;
	var secondaryStatusId = record.secondaryStatusId;
	var productId=document.getElementById("treeHdnCurrentProductId").value;
	$("#addCommentsMainDiv").modal();			
	$('#addComments').hide();//Display only histroy of task Effort
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
			primaryStatusUrl : 'workflow.status.master.option.list?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&currentStatusId='+currentStatusId,
			secondaryStatusUrl : 'workflow.entity.secondary.status.master.option.list?productId='+productId+'&entityTypeId='+entityTypeId+'&statusId='+currentStatusId,
			currentStatusId : currentStatusId,
			currentStatusName : currentStatusDisplayName,
			secondaryStatusId : secondaryStatusId,
			effortListUrl : 'workflow.event.tracker.list?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
			actionTypeValue : actionTypeValue,
			commentsName : commentsReviewActivity,
			urlToSave : 'workflow.event.tracker.add?productId='+productId+'&entityId='+entityId+'&entityTypeId='+entityTypeId+'&primaryStatusId=[primaryStatusId]&secondaryStatusId=[secondaryStatusId]&effort=[effort]&comments=[comments]&sourceStatusId='+currentStatusId+'&approveAllEntityInstanceIds=[approveAllEntityIds]&entityInstanceId='+entityInstanceId+'&attachmentIds=[attachmentIds]&actionDate=[actionDate]',
			// commentsStatus: "['started','InProgress','Completed']",		
	};
	AddComments.init(jsonObj);
}		
		
//Common 

/* Pop Up close function */
function popupClose() {
	$("#div_PopupMain").fadeOut("normal");
	$("#div_PopupBackground").fadeOut("normal");
}

/* Load Poup function */
function loadPopup(divId) {
	$("#" + divId).fadeIn(0500); // fadein popup div
	$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
														// IE7, IE8
	$("#div_PopupBackground").fadeIn(0001);
}	

function SaveActivityDetailsGrouping(){	
	activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;			
	productId=document.getElementById("treeHdnCurrentProductId").value;
	productName = document.getElementById("treeHdnCurrentProductName").value;
    var activitywpListsFromUI = [];
	var $selectedFeatureRows = '';	
    $selectedFeatureRows = $('#jTableContainerActivityWP').jtable('selectedRows');
		if($selectedFeatureRows.length == 0){
			callAlert("Please select activity for bulk allocation");
		}

		$selectedFeatureRows.each(function () {
		    var record = $(this).data('record');
		    var wptcepId = record.activityId;
		    activitywpListsFromUI.push(wptcepId);
		});
		
		 var plannedEndDate=bulkalloc_datepickerenddate.value;
		 if(plannedEndDate=='Planned End Date')
			 plannedEndDate='';		 
		 var category=$("#bulkalloc_category_dd .select2-chosen").text();
		 var assignee=$("#bulkalloc_assignee_dd .select2-chosen").text();
		 var reviewer=$("#bulkalloc_reviewer_dd .select2-chosen").text();
		 var priority=  $("#bulkalloc_executionPriorityList_dd .select2-chosen").text();		 
		 var count = 0;
		var categoryId ="";
		if((category!="-")&&(category!="")){
			categoryId = $("#bulkalloc_category_ul").find('option:selected').attr('id');
			 count =  count +1;
		}else{
			categoryId=-1;
		}		
		
		 var assigneeId="";
		 if( (assignee!="-") &&(assignee!="")){
			 assigneeId= $("#bulkalloc_assignee_ul").find('option:selected').attr('id');
			 count =  count +1;
		 }else{
			 assigneeId=-1;
		 }
		
		var reviewerId="";
		if((reviewer!="-")&&(reviewer!="")){
			reviewerId = $("#bulkalloc_reviewer_ul").find('option:selected').attr('id');
			count =  count +1;
		}else{
			reviewerId=-1;
		}
		
		var priorityId ="";
		if((priority!="-")&&(priority!="")){
			priorityId = $("#bulkalloc_executionPriorityList_ul").find('option:selected').attr('id');
			count =  count +1;
		}else{
			priorityId=-1;
		}	
       
		if(activityWorkPackageId==null || activityWorkPackageId <=0 || activityWorkPackageId == 'null'){
				callAlert("Please select the activityWorkPackage");
				return false;
		}else if(activitywpListsFromUI.length == null || activitywpListsFromUI==''){
				callAlert("Select atleast one Activity","ok");
		}else if(plannedEndDate=='' && assigneeId==-1 && reviewerId==-1&&	priorityId==-1 &&	categoryId ==-1){
				callAlert("Select atleast one Option","ok");
		}else{			
			$.post('activitywp.activity.bulk.update?categoryId='+categoryId+'&assigneeId='+assigneeId+'&activitywpListsFromUI='+activitywpListsFromUI+'&plannedEndDate='+plannedEndDate+'&priorityId='+priorityId+'&reviewerId='+reviewerId,function(data){
				if(data.Result=="OK"){
				iosOverlay({
					text: "Saved", // String
					icon: "css/images/check.png", // String (file path)
					spinner: null,
					duration: 1500, // in ms
					});		
				
				productVersionId = 0;
				productBuildId = 0;
				activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
				productId=0;
				var isActive = $("#isActive_ul").find('option:selected').val();					
			    listActivitiesOfSelectedAWP(productId,productVersionId,productBuildId,activityWorkPackageId,isActive,"yes",  '#jTableContainerActivityWP');
				}
				else{
					iosOverlay({
						text: "Not Saved", // String
						icon: "css/images/cross.png", // String (file path)
						spinner: null,
						duration: 1500, // in ms
						});
				}
				});
			}
		
}

function ResetActivityDetailsGrouping(){	
	bulkalloc_datepickerenddate.value="";	
	$('#bulkalloc_reviewer_ul').select2('data', null);
	$('#bulkalloc_category_ul').select2('data', null);
	$('#bulkalloc_assignee_ul').select2('data', null);
	$('#bulkalloc_executionPriorityList_ul').select2('data', null);	
    return false;
	}

function ActivityInitloadListGrouping(){
    $("#bulkalloc_datepickerenddate").datepicker('setDate','');
	loadReviewerListGrouping();
	loadAssigneeListGrouping();								
	loadCategoryListGrouping();							
	loadExecutionPriorityListGrouping();
}

function loadReviewerListGrouping(){	
$('#bulkalloc_reviewer_ul').empty();	
$.post('common.user.list.by.resourcepool.id.productId?productId='+document.getElementById("treeHdnCurrentProductId").value,function(data) {		
    var ary = (data.Options);
    $.each(ary, function(index, element) {
        $('#bulkalloc_reviewer_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
		});
		});
}


function loadAssigneeListGrouping(){
$('#bulkalloc_assignee_ul').empty();	
$.post('common.user.list.by.resourcepool.id.productId?productId='+document.getElementById("treeHdnCurrentProductId").value,function(data) {	
    var ary = (data.Options);
    $.each(ary, function(index, element) {
        $('#bulkalloc_assignee_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
		});
		});
} 


function loadCategoryListGrouping(){	
$('#bulkalloc_category_ul').empty();	
$.post('common.list.executiontypemaster.byentityid?entitymasterid=1',function(data) {		
    var ary = (data.Options);
    $.each(ary, function(index, element) {
        $('#category_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
		});
		});
}


function loadExecutionPriorityListGrouping(){
$('#bulkalloc_executionPriorityList_ul').empty();	
$.post('administration.executionPriorityList',function(data) {		
    var ary = (data.Options);
    $.each(ary, function(index, element) {
        $('#bulkalloc_executionPriorityList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
		});
		});
}

// ------ Drag and drop functionality -----

/* DragDropListItem Plugin started */ 		 
var jsonActivityMoveObj='';
var jsonActivityChangeRequestObj='';
var jsonActivityObj='';


function loadDragDropSelectOption(json){
	var temp='';

	if($("#dropDownWorkpacakge_dd") != null || $("#dropDownWorkpacakge_dd") != undefined)
		$("#dropDownWorkpacakge_dd").html('');
	
	$("#dropDownWorkpacakge_dd").css('display','none');
	
	if(json.componentUsageTitle == "ActivityMultiselctMove"){
		$("#dropDownWorkpacakge_dd").css('display','inline-flex');

		temp = ('<label style="padding-left: 17px;padding-top: 5px;">Source Workpackage : </label>'+
				'<select class="form-control input-medium select2me" id="sourceDropDownWorkpacakge_ul" onchange="sourceWorkpackageDropDownInActivities(event)">'+
				'</select></div>'+
				'<label style="padding-left: 160px;padding-top: 5px;">Target Workpackage : </label>'+
				'<select class="form-control input-medium select2me" id="dropDownWorkpacakge_ul" onchange="workpackageDropDownInActivities(event)">'+
				'</select></div>');	
	}
	
	$("#dropDownWorkpacakge_dd").append(temp);	
	var flag=false;
	var url = 'get.activityworkpackage.by.engagement.product.list?testFactoryId='+engagementId+'&productId='+productId;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',		
		success : function(data) {
			if(data != undefined){			
				var len = data.Records.length;
				for(var i=0;i<len;i++){					
					if((document.getElementById("treeHdnCurrentActivityWorkPackageId").value == 0 || document.getElementById("treeHdnCurrentActivityWorkPackageId").value == '') && !flag){
						$("#dropDownWorkpacakge_ul").append('<option selected id="'+data.Records[i].itemId+'">'+data.Records[i].itemName+'</option>');
						$("#sourceDropDownWorkpacakge_ul").append('<option selected id="'+data.Records[i].itemId+'">'+data.Records[i].itemName+'</option>');
						if(json.componentUsageTitle == "ActivityMultiselctMove"){
							workpackageDropDownInActivities(null, data.Records[i].itemId);
							sourceWorkpackageDropDownInActivities(null, data.Records[i].itemId);
						}
						flag=true;
					
					}else if(document.getElementById("treeHdnCurrentActivityWorkPackageId").value == data.Records[i].itemId){
						$("#dropDownWorkpacakge_ul").append('<option selected id="'+data.Records[i].itemId+'">'+data.Records[i].itemName+'</option>');
						$("#sourceDropDownWorkpacakge_ul").append('<option selected id="'+data.Records[i].itemId+'">'+data.Records[i].itemName+'</option>');
						if(json.componentUsageTitle == "ActivityMultiselctMove"){
							workpackageDropDownInActivities(null, data.Records[i].itemId);
							sourceWorkpackageDropDownInActivities(null, data.Records[i].itemId);
						}
					}
					else{
						$("#dropDownWorkpacakge_ul").append('<option id="'+data.Records[i].itemId+'">'+data.Records[i].itemName+'</option>');
						$("#sourceDropDownWorkpacakge_ul").append('<option id="'+data.Records[i].itemId+'">'+data.Records[i].itemName+'</option>');
					}				
				}
				
				if(document.getElementById("treeHdnCurrentActivityWorkPackageId").value == 0 || document.getElementById("treeHdnCurrentActivityWorkPackageId").value == ''){
					 $("#rightDragItemsContainer").empty();	
					 $("#rightDragItemsTotalCount").text(0);
				}
			}
		},
		error: function (data){
			console.log("error in ajax");
		}
	});
	
	// ----- 
	
}

var id = 0;
function workpackageDropDownInActivities(event, value){
	if(event == null){
		id = value;
	}else{
		id=$("#"+event.target.id).find('option:selected').attr('id');
	}
	var rightDefaultUrl="map.activities.by.activityWorkPackageId?activityWorkPackageId="+id;
	 $("#rightDragItemsContainer").empty();		 
	$.ajax({
		type: "POST",
       contentType: "application/json; charset=utf-8",
		url : rightDefaultUrl,
		dataType : 'json',
		success : function(data) {						
			var listOfData = data.Records;
			$("#rightDragItemsContainer").empty();
			if(listOfData.length==0){			
			 $("#rightDragItemsContainer").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>"+jsonActivityMoveObj.noItems+"</b></span>");
				$("#rightDragItemsTotalCount").text(listOfData.length);					
				
			}else{
				$.each(listOfData, function(i,item){
					resultValue = rightItemDislayListItem(item, jsonActivityMoveObj);
					$("#rightDragItemsContainer").append(resultValue);										 
			 	});
				$("#rightDragItemsTotalCount").text(listOfData.length);
			}
		}
	});
}


var soruceId = 0;
function sourceWorkpackageDropDownInActivities(event, value){
	if(event == null){
		soruceId = value;
	}else{
		soruceId=$("#"+event.target.id).find('option:selected').attr('id');
	}
	
	//var rightDefaultUrl="map.activities.by.activityWorkPackageId?activityWorkPackageId="+soruceId;
	var leftUrl="unmap.activities.by.productIdandworkpackageId?engagementId="+engagementId+"&productId="+productId+"&activityWorkPackageId="+soruceId;
	 $("#leftDragItemsContainer").empty();		 
	$.ajax({
		type: "POST",
       contentType: "application/json; charset=utf-8",
		url : leftUrl,
		dataType : 'json',
		success : function(data) {						
			var listOfData = data.Records;
			$("#leftDragItemsContainer").empty();
			if(listOfData.length==0){			
			 $("#leftDragItemsContainer").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>"+jsonActivityMoveObj.noItems+"</b></span>");
				$("#leftDragItemsTotalCount").text(listOfData.length);					
				
			}else{
				$.each(listOfData, function(i,item){
					resultValue = rightItemDislayListItem(item, jsonActivityMoveObj);
					$("#leftDragItemsContainer").append(resultValue);										 
			 	});
				$("#leftDragItemsTotalCount").text(listOfData.length);
			}
		}
	});
}

function leftDraggedItemURLChanges(value,type){
	if(type==jsonActivityObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
		    result ='&enviCombiId='+itemid+''+leftDragUrlConcat;
	}
	else if(type==jsonActivityChangeRequestObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
	    x =  value.split("("),
	    itemid = x[0],	
	    result ='&changeRequestId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonActivityMoveObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
			//result ='&dimensionId='+itemid+''+leftDragUrlConcat;
		    result='activityId='+itemid+'&destActWorkpackageId='+soruceId;
	
	}else if(type==jsonUserMappingForWP.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
	    x =  value.split("("),
	    itemid = x[0],	
	    result ='&userId='+itemid+''+leftDragUrlConcat;
	}
	
	return result;
}

function rightDraggedItemURLChanges(value,type){
	result='';
	if(type==jsonActivityObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",
		    x =  value.split("("),
		    itemid = x[0],		
		    result ='&enviCombiId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type==jsonActivityChangeRequestObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",
	    x =  value.split("("),
	    itemid = x[0],		
	    result ='&changeRequestId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonActivityMoveObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
		    result='activityId='+itemid+'&destActWorkpackageId='+id;
	
	}else if(type==jsonUserMappingForWP.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",
	    x =  value.split("("),
	    itemid = x[0],		
	    result ='&userId='+itemid+''+rightDragtUrlConcat;
	}
	return result;
}

function leftItemDislayListItem(item, jsonObj){
	var resultList="";
	var entity_id = item.itemId;
	var entity_name = item.itemName;	
	var entity_dispname = '';
		
	if(jsonObj.componentUsageTitle==jsonActivityObj.componentUsageTitle){
		//var entity_code = item.itemCode;
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";	
	}
	else if(jsonObj.componentUsageTitle == jsonActivityChangeRequestObj.componentUsageTitle){	
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonActivityMoveObj.componentUsageTitle){	
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	
	}else if(jsonObj.componentUsageTitle == jsonUserMappingForWP.componentUsageTitle){	
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}	
	return resultList;	
}

function rightItemDislayListItem(item, jsonObj){
	var resultList="";
	var entity_id = item.itemId;	
	var entity_name = item.itemName;
	var entity_dispname = '';
		
	if(jsonObj.componentUsageTitle==jsonActivityObj.componentUsageTitle){
		 //var entity_code = item.itemCode;
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonActivityChangeRequestObj.componentUsageTitle){	
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}	
	else if(jsonObj.componentUsageTitle == jsonActivityMoveObj.componentUsageTitle){	
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);	
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonUserMappingForWP.componentUsageTitle){	
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
		 $('#dropDownWorkpacakge_dd').hide();
	}
	
	return resultList;
}

function trim(str) {
    return str.replace(/^\s+|\s+$/g,"");
}

/* DragDropListItem Plugin ended */
