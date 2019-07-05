
var activityMasterOptionsArr= [];
var categoryOptionsArr= []; 
var resourcePoolOptionsArr= []; 
var priorityOptionsArr= []; 
var activityPrimaryOptionsArr= []; 

function option(url,fieldName){
	if(fieldName==''){}
	 
    $.ajax({ //Not found in cache, get from server
        url: url,
        type: 'POST',
        dataType: 'json',
        async: false,
        success: function (data) {
            if (data.Result != 'OK') {
            //	callAlert(data.Message);
                return;
            }
			if(fieldName=='activityMasterUrl'){
			activityMasterOptionsArr = data.Options;
			}
			if(fieldName=='categoryUrl'){
			categoryOptionsArr = data.Options;     }
			if(fieldName=='resourcePoolUrl'){
			resourcePoolOptionsArr = data.Options;       }
			if(fieldName=='priorityUrl'){
			priorityOptionsArr = data.Options;     }
			if(fieldName=='activityPrimaryStatus'){
			activityPrimaryOptionsArr = data.Options;     }
            
        }
    });
	//return optionsArr;
}
var defaultuserId = '1';
		var isFirstLoad = true;
		var productId;
		var productName;		
		var defaultActivityPlannedStartDate = new Date();
		var defaultActivityPlannedEndDate = new Date();
		var defaultActivityLifeCycleStage = 0;
		var activityMasterTypeId = 28;
		
//function listActivitiesOfSelectedAWP(productId,productVersionId,productBuildId,activityWorkPackageId,isActive,enableAddOrNot) {
function listActivitiesOfSelectedAWP(productId,productVersionId,productBuildId,activityWorkPackageId,isActive,enableAddOrNot, jTableContainerId) {
	openLoaderIcon();
	var priorityHeaderTitle="Priority";
	var plannedActivitySizeHeaderTitle="Planned Activity Size";
	var actualActivitySizeHeaderTitle="Actual Activity Size";
	var plannedActivitySize=0;
	
	if(customActivityListHeaderFieldsEnable == "YES") {
		priorityHeaderTitle="Impact";
		plannedActivitySizeHeaderTitle="Estimated Savings";
		actualActivitySizeHeaderTitle="Realized Savings";
	} 
	//$("#bulkselectionnew").show();
	productId=document.getElementById("treeHdnCurrentProductId").value;
	if(document.getElementById("treeHdnCurrentProductVersionId").value != "")
		productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value;
	
	if(document.getElementById("treeHdnCurrentProductBuildId").value != "")
		productBuildId = document.getElementById("treeHdnCurrentProductBuildId").value;
	
	/*if(document.getElementById("treeHdnCurrentActivityWorkPackageId").value != "")
		activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;*/
	
	if(activityWorkPackageId == null || activityWorkPackageId == "" || activityWorkPackageId == 0){
		if(document.getElementById("treeHdnCurrentActivityWorkPackageId").value != "")
			activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
	
	}
					 
	var urlToGetActivitiesOfSpecifiedAWPId = 'process.activity.list?productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&activityWorkPackageId='+activityWorkPackageId+'&isActive='+isActive;		
	    		
	var activityMasterUrl='process.list.activity.type?activityWorkPackageId='+activityWorkPackageId; 
    var categoryUrl='common.list.executiontypemaster.byentityid?entitymasterid=1';
	var resourcePoolUrl='common.user.list.by.resourcepool.id.productId?productId='+productId;
	var priorityUrl='administration.executionPriorityList';
	var activityPrimaryStatus='activity.primary.status.master.option.list?productId='+productId;
	
	option(activityMasterUrl,'activityMasterUrl');
	option(categoryUrl,'categoryUrl');
	option(resourcePoolUrl,'resourcePoolUrl');
	option(priorityUrl,'priorityUrl');
	option(activityPrimaryStatus,'activityPrimaryStatus');
			
			try {
				if ($(jTableContainerId).length > 0) {
					$(jTableContainerId).jtable('destroy');
				}
			} catch (e) {
			}
			
			$(jTableContainerId)
				.jtable(
							{
								title: 'Activities',
								paging : true, //Enable paging
								pageSize : 10,
								"scrollY":"50px",
								editinline : {
									enable : true
								},
								toolbarsearch:false,
								selecting : true, //Enable selecting
								multiselect : true, //Allow multiple selecting
								selectingCheckboxes : true, //Show checkboxes on first column
								recordUpdated:function(event, data){
									if(data.serverResponse.Message!='undefined' && data.serverResponse.Message!=""){
										callAlert("Warning: "+data.serverResponse.Message)
									}
								},								
								recordsLoaded: function(event, data) {								
									closeLoaderIcon();
									$('.portlet > .portlet-title > .tools > .reload').trigger('click');							
		   
									if(enableAddOrNot == "no"){
										$('jTableContainerId .jtable-toolbar-item-add-record').hide();
									 }					        	
								},
								toolbar : {
	                                items : [
                                         {
										icon : '',
										tooltip: 'Custom Fields',
										cssClass: 'fa fa-list-alt showHandCursor',
										click : function() {
											getAllInstanceCustomFieldsOfEntity(28, 0, activityWorkPackageId, engagementId, productId, 'Activities');
										}
									}]
								},
						        actions : {
									listAction : urlToGetActivitiesOfSpecifiedAWPId,
									createAction : 'process.activity.add',
									editinlineAction : 'process.activity.update',
									deleteAction : 'process.activity.delete',
								},								 
								fields : {								
									activityWorkPackageId : {
							 				type: 'hidden', 
							 				defaultValue: activityWorkPackageId 
									},
								
									activityId : {
										key : true,
										list : true,
										create : false
									},
									activityName:{
						                 title: 'Activity Name',
						                 inputTitle: 'Activity Name <font color="#efd125" size="4px">*</font>',
						            	 list:true,
						            	 edit:true,
						            	 create:true,
						                 width:"20%"
						             },
									activityMasterId : {
										title : 'Activity Type',
										inputTitle: 'Activity Type <font color="#efd125" size="4px">*</font>',
										list : true,
										edit : true,
										create : true,
										width : "25%",
										options:function () {
				                    		return  activityMasterOptionsArr; //Cache results and return options
										}
									},
									productFeatureId : {
										title : 'Requirement',											
										list : true,
										create : true,
										edit : true,
								    	options:function(data){
								    		if(data.source =='list'){	      				
								    			return 'list.features.by.activity?activityId='+data.record.activityId+'&activityWorkPackageId='+activityWorkPackageId;
						      				}else if(data.source == 'create'){	      				
						      					return 'list.features.by.activity?activityId=-1&activityWorkPackageId='+activityWorkPackageId;
						      				}
						      			},
									},
									lifeCycleStageId : {
										title : 'Life Cycle Stage',
										create : true,
										list : true,
										edit : true,
										width : "20%",
										dependsOn : 'activityWorkPackageId',
										defaultValue : defaultActivityLifeCycleStage,
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
										edit : true,
										create : true,
										list : true,
										type : 'date',										
									defaultValue :	defaultActivityWorkpackagePlannedStartDate,
									width : "20%"
									},
									plannedEndDate : {
										title : 'Planned End Date',										
										inputTitle : 'Planned End Date <font color="#efd125" size="4px">*</font>',
										edit : true,
										list : true,
										create : true,
										type : 'date',
										defaultValue : defaultActivityWorkpackagePlannedEndDate,
										width : "20%"
									},
									workflowRAG : {
										create : false,
										edit : true,
										list : true,
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
										create : false,
										edit : true,
										list : true,
										type : 'date',
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
										edit : true,
										defaultValue : defaultuserId,
										options:function () {
                    						return  resourcePoolOptionsArr; //Cache results and return options
										}										
									},
									reviewerId : {
										title : 'Reviewer',
										list : true,
										create : true,
										edit : true,
										width : "20%",
										options:function () {
                   							 return  resourcePoolOptionsArr; //Cache results and return options
										}
									},
									priorityId : {
										title : priorityHeaderTitle,										
										list : true,
										create : true,
										edit : true,
										width : "20%",
										options:function () {
                    						return  priorityOptionsArr; //Cache results and return options
										}
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
									activityWorkPackageName : {
										title : 'Work Package',
										list : true,
										edit : false,
										create : false
									},
									activityTrackerNumber : {
										title : 'Tracker Number',
										list : true,
										edit : true,
										width : "20%"
									},
							    	drReferenceNumber : {
										title : 'Clarification',
										list : false,
										create : false,
										edit : false,
										width : "20%"
									}, 							
									categoryId : {
										title : 'Category',										
										list : true,
										create : true,
										edit : true,
										options:function () {
                    						return  categoryOptionsArr; //Cache results and return options
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
									plannedActivitySize : {
										//title : 'Planned Activity Size',
										title : plannedActivitySizeHeaderTitle,
										list : true,				    										
										create : true,
										edit : true,
										width : "20%",
										defaultValue : 1,										
									},
									actualActivitySize : {
										//title : 'Actual Activity Size',
										title : actualActivitySizeHeaderTitle,		
										list : true,				    										
										create : false,
										edit : true,
										width : "20%",
										defaultValue :plannedActivitySize,
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
									plannedEffort : {
	        				        	title : 'Planned Effort',
	        				        	create : true,
	        				        	edit : false,
	        				        	list : false,
	        				        	width : "20%",
	        				        	defaultValue : 1,
	        				        },									
									totalEffort : {
	        				        	title : 'Actual Effort',
	        				        	create : false,
	        				        	edit : false,
	        				        	list : true,
	        				        	width : "20%",
	        				        	
	        				         },
	        				         isActive: { 
	                                   	title: 'Active',list:true,
	                       				edit:true,
	                    				create:false,
	                    				type : 'checkbox',
	                    				values: {'0' : 'No','1' : 'Yes'},
	                    		    	defaultValue: '1'
                    		    	},
								CloningActivity:{
		             						title : '',
		             						list : true,
		             						create : false,
		             						width: "5%",
		             						display:function (cloneData) { 
		             						 var $img = $('<img src="css/images/cloning.jpg" style="width: 24px;height: 24px;" title="Cloning Activity" data-toggle="modal" />'); 
		             						$img.click(function () {
		             							
		             						// -- for Moving Activity ---
		        								$.ajax({
		        									type: "POST",
		        							        contentType: "application/json; charset=utf-8",
		        							    //    url: 'administration.product.activity.workpackage.tree',		        							        
		        							        url: 'administration.product.activity.workpackage.tree.by.productId?productId='+productId,    
		        							     
		        									dataType : 'json',
		        									complete : function(data){
		        										console.log('complete');
		        									},
		        									success : function(data) {
		        										treeData = data;
		        										
		        										var jsonActivityCloningObj = {
		                 									"title": "Cloning Activity",
		                 									"packageName":"Activity Name",
		                 									"startDate" : dateFormat(cloneData.record.plannedStartDate),
		                									"endDate" : dateFormat(cloneData.record.plannedEndDate),
		                 									"selectionTerm" : "Select Activity Workpackage",
		                 									"sourceParentID": cloneData.record.activityWorkPackageId,
															"sourceParentName": cloneData.record.activityWorkPackageName,
		                 									"sourceID": cloneData.record.activityId,
		                 									"sourceName": cloneData.record.activityName,
		                 									"componentUsageTitle":"activityClone",
		                 							}
		                 							Cloning.init(jsonActivityCloningObj);
		        										
		        									},
		        									error: function (data){
		        										console.log('error');
		        									}
		        								});
		             														
		             						});
		             						return $img; 
		             					}
		             	             },MovingActivity:{
		             						title : '',
		             						list : true,
		             						create : false,
		             						width: "5%",
		             						display:function (cloneData) { 
		             						 var $img = $('<img src="css/images/cloning.jpg" style="width: 24px;height: 24px;" title="Moving Activity" data-toggle="modal" />'); 
		             						$img.click(function () {
		             							
		             						// -- for Moving Activity ---
		        								$.ajax({
		        									type: "POST",
		        							        contentType: "application/json; charset=utf-8",
		        							        url: 'administration.product.activity.workpackage.tree',
		        									dataType : 'json',
		        									complete : function(data){
		        										console.log('complete');
		        									},
		        									success : function(data) {
		        										treeData = data;
		        										
		        										var jsonActivityCloningObj = {
		                 									"title": "Move Activity",
		                 									"packageName":"Activity Name",
		                 									"startDate" : dateFormat(cloneData.record.plannedStartDate),
		                									"endDate" : dateFormat(cloneData.record.plannedEndDate),
		                 									"selectionTerm" : "Select Activity Workpackage",
		                 									"sourceParentID": cloneData.record.activityWorkPackageId,
															"sourceParentName": cloneData.record.activityWorkPackageName,
		                 									"sourceID": cloneData.record.activityId,
		                 									"sourceName": cloneData.record.activityName,
		                 									"componentUsageTitle":"activityMove",
		                 							}
		                 							Cloning.init(jsonActivityCloningObj);
		        										
		        									},
		        									error: function (data){
		        										console.log('error');
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
		        				           		var $img = $('<i class="fa fa-history showHandCursor" title="Event History"></i>');		        			           			  
		        			           			$img.click(function () {
		        			           				addActivityCommentsJtable(data.record);
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
        				           				entityTypeId = 33;
        				           				statusPolicies(productId, workflowId, entityTypeId, data.record.activityMasterId, data.record.activityId, data.record.activityName, "Activity", data.record.statusId);
        				           		  });
        				           			return $img;
        					        	}
        					        },
        					        workflowId:{
        					        	title : 'Workflow Template',
        					        	list:false, 
						     	  		create:true,
						     	  		edit:false,
										dependsOn:'activityMasterId',
										options:function(data){							
											var entityId = data.dependedValues.activityMasterId;
											if(typeof entityId == 'undefined' || entityId == null){
												entityId = 0;
											}
											return 'workflow.master.mapped.to.entity.list.options?productId='+productId+'&entityTypeId=33&entityId='+entityId;
							     		}
        					        },
									changeRequest:{
					                	title: '',
					                	width: "5%",
					                	edit: true,
					                	create: false,
					                	display: function (activityData) { 
					                   		//Create an image that will be used to open child table 
					                   			var $img = $('<img src="css/images/list_metro.png" title="Change Request" />'); 
					                   			//Open child table when user clicks the image 
					                   			$img.click(function () {
					                   			
					                   				// ----- Closing child table on the same icon click -----
					                   			    closeChildTableFlag = closeJtableTableChildContainer($(this), $(jTableContainerId));
					                   				if(closeChildTableFlag){
					                   					return;
					                   				} 		                   						
					                   				
					                   				$(jTableContainerId).jtable('openChildTable', 
					                   				$img.closest('tr'), 
					                   					{						                   	      	   
					                   						title: 'Add/Edit Change Request',							                   	      
					                   						editinline:{enable:true},
					                   						recordsLoaded: function(event, data) {
					                   		        		$(".jtable-edit-command-button").prop("disabled", true);
					                   		         	},
					                   					actions: { 
					                   						listAction: 'list.changerequests.by.entityTypeAndInstanceId?entityType1=28'+'&entityInstance1='+activityData.record.activityId,
					                   						createAction : 'changerequests.add.by.entityTypeAndInstanceId?entityInstanceId='+activityData.record.activityId,
					    									/* createAction : 'process.activity.add?activityWorkPackageId='+activityWorkPackageId, */
					    								//	editinlineAction : 'list.change.requests.by.activity.update',	
					    									editinlineAction : 'list.change.requests.by.activity.update?activityId='+activityId,
					                   						}, 
					                   						recordsLoaded: function(event, data) {
					                   							$('.jtable-toolbar-item-add-record').click(function(){
						                   							$("#Edit-raisedDate").datepicker('setDate','today');
			                   									});				                   												                   							
					                   						},
					                   					fields: { 
					                   						changeRequestId: { 					                   							
					                   							title : 'ID',
					                   							key: true, 
					                       						create: false, 
					        					                edit: true, 
					                       						list: true
					                       					},
					                       					entityType1:{
					            								type: 'hidden',
					            								create: true, 
					            				                edit: true, 
					                       						list: true,
					            								defaultValue: activityMasterTypeId
					            							},
															entityInstance1:{
																type: 'hidden',
																create: true, 
					        					                edit: false, 
					                       						list: false,
																defaultValue: activityData.record.activityId,
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
					    							             list:false,
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
																options:function () {
												                    return  priorityOptionsArr; //Cache results and return options
																}
					    									},
															planExpectedValue: {
															title : 'Planned Value',										
															inputTitle : 'Planned Value <font color="#efd125" size="4px"></font>',
															edit : true,
															list : true,
															create : true,    							
															width : "20%",
															defaultValue:activityData.record.plannedActivitySize,
															},
					    									statusCategoryId : {
					    										title : 'Status',
					    										create:false,
					    										list : false,
					    										edit :false,
					    										defaultValue : 1,
					    										options:function(data){
					    										 	return 'status.category.option.list';
					    							     		}
					    									},
					    									ownerId : {
					    										title : 'Owner',					    										
					    										list : true,
					    										create : true,
					    										edit : true,
															options:function () {
											                    	return  resourcePoolOptionsArr; //Cache results and return options
																}
					    									},
					    									raisedDate : {
					    										title : 'Raised Date',
					    										inputTitle : 'Raised Date <font color="#efd125" size="4px">*</font>',
					    										edit : false,
					    										list : true,
					    										create : true,
					    										type : 'date',
					    										width : "20%",
					    										defaultValue : (new Date().getMonth()+1)+"/"+new Date().getDate()+"/"+new Date().getFullYear(),
					    									},attachment: { 
					    										title: '', 
					    										list: true,
					    										edit: false,
					    										create: false,
					    										width: "10%",
					    										display:function (data) {	        		
					    							           		//Create an image that will be used to open child table 
					    											var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>'); 
					    							       			$img.click(function () {          
					    							       				isViewAttachment = false;
					    							       				var jsonObj={"Title":"Attachments for ChangeRequest",			          
					    							       					"SubTitle": 'ChangeRequest : ['+data.record.changeRequestId+'] '+data.record.changeRequestName,
					    							    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=42&entityInstanceId='+data.record.changeRequestId,
					    							    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=42&entityInstanceId='+data.record.changeRequestId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
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
																title : '',
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
					                   				}, function (data) { //opened handler 
					                   						data.childTable.jtable('load'); 
					                   				}); 
					                 	  	}); 
					                   	//Return image 
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
						           			var $img = $('<img src="css/images/mapping.png" title="Change Request Mapping" data-toggle="modal" />'); 
						           			//Open child table when user clicks the image 
						           			$img.click(function () {
												var activityId = data.record.activityId;
												var leftUrl="unmapped.changeRequest.count?activityId="+activityId;							
												var rightUrl = "";							
												var leftDefaultUrl="unmapped.changerequest.list?productId="+productId+"&activityId="+activityId+"&jtStartIndex=0&jtPageSize=10";							
												var rightDefaultUrl="changerequest.mapped.to.activity.list?activityId="+activityId;
												var leftDragUrl = "activity.changerequest.mapping?activityId="+activityId;
											    var rightDragtUrl = "activity.changerequest.mapping?activityId="+activityId;					
												var leftPaginationUrl = "unmapped.changerequest.list?productId="+productId+"&activityId="+activityId;
												var rightPaginationUrl="";						
												
												jsonActivityChangeRequestObj={"Title":"Map Change Request to Activity",
														"leftDragItemsHeaderName":"Available Change Request",
														"rightDragItemsHeaderName":"Mapped Change Request",
														"leftDragItemsTotalUrl":leftUrl,
														"rightDragItemsTotalUrl":rightUrl,
														"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
														"rightDragItemsDefaultLoadingUrl":rightDefaultUrl,
														"leftDragItemUrl":leftDragUrl,
														"rightDragItemUrl":rightDragtUrl,									
														"leftItemPaginationUrl":leftPaginationUrl,
														"rightItemPaginationUrl":rightPaginationUrl,									
														"leftDragItemsPageSize":"50",
														"rightDragItemsPageSize":"50",
														"noItems":"No Change Request Mapped",
														"componentUsageTitle":"Activity-RcnToActivity"
														};
												
												DragDropListItems.init(jsonActivityChangeRequestObj);							
												// DragDrop Testing ended----	           				
						           		  });
						           			return $img;
							        	}
							        }, 
									DR:{
					                	title: '',
				                	    width: "5%",
				                	    edit: true,
				                	    create: false,
				                	    list:false,
				                          	display: function (activityDrData) { 
				                   		//Create an image that will be used to open child table 
				                   			var $img = $('<img src="css/images/list_metro.png" title="Clarifications" />'); 
				                   			//Open child table when user clicks the image 
				                   			$img.click(function () {
				                   				
				                   				// ----- Closing child table on the same icon click -----
				                   			    closeChildTableFlag = closeJtableTableChildContainer($(this), $(jTableContainerId));
				                   				if(closeChildTableFlag){
				                   					return;
				                   				} 
				                   			
				                   				$(jTableContainerId).jtable('openChildTable', 
				                   				$img.closest('tr'), 
				                   					{ 
				                   						title: 'Add/Edit Clarification', 
				                   					 	editinline:{enable:true},
				                   						recordsLoaded: function(event, data) {
				                   		        	 	$(".jtable-edit-command-button").prop("disabled", true);
				                   		         	},
				                   					actions: { 
				                   						listAction: 'list.clarificationtracker.by.entityTypeAndInstanceIds?entityTypeId=28&entityInstanceId='+activityDrData.record.activityId,
				                   						createAction : 'add.clarificationtracker.by.activity',		    									
				    									editinlineAction : 'update.clarificationtracker.by.activity',	
				                   						}, 
				                   					fields: { 
				                   						  activityId: { 
				                   							type: 'hidden', 
				                   							defaultValue: activityDrData.record.activityId 
				                   						},  
				                   						clarificationTrackerId: { 
				                       						key: true, 
				                       						create: false, 
				        					                edit: false, 
				                       						list: false
				                       					},
				                       					clarificationTitle : {
				    										title : 'Title',
				    										inputTitle : 'Title <font color="#efd125" size="4px">*</font>',
				    										list : true,				    										
				    										create : true,
				    										edit : true,
				    										width : "10%"
				    										
				    									},
				    									clarificationDescription:{
				    							             title: 'Description',
				    							             list:false,
				    							             type: 'textarea',    
				    							             create:true,
				    							             edit:true,
				    							             width:"10%"
				    							             },
				    										
				                       					priorityId : {
				    										title : 'Priority',				    										
				    										list : true,
				    										create : true,
				    										edit : true,
				    										width : "10%",
															options:function () {
                    											return  priorityOptionsArr; //Cache results and return options
															}
				    									},
				    									statusCategoryId : {
				    										title : 'Status',
				    										create:true,
				    										list : true,
				    										edit : true,
				    										defaultValue : 1,
				    										options:function(data){
				    										 	return 'status.category.option.list';
				    							     		}
				    									},
				    									ownerId : {
				    										title : 'Owner',				    									
				    										list : true,
				    										width : "10%",
				    										create : true,
				    										edit : true,
															options:function () {
                    											return  resourcePoolOptionsArr; //Cache results and return options
															}
				    									},
				    									raisedDate : {
				    										title : 'Raised Date',
				    										inputTitle : 'Raised Date <font color="#efd125" size="4px">*</font>',
				    										edit : true,
				    										list : true,
				    										create : true,
				    										type : 'date',
				    										width : "10%"
				    									},   
				    									raisedById : {
				    										title : 'Raised By',
				    										inputTitle : 'Raised By <font color="#efd125" size="4px">*</font>',
				    										list : true,
				    										create : true,
				    										width : "10%",
				    										edit : true,
				    										defaultValue : defaultuserId,
															options:function () {
                    											return  resourcePoolOptionsArr; //Cache results and return options
															}
				    									}
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
							                        }
				                   				}, function (data) { //opened handler 
				                  					 	data.childTable.jtable('load'); 
				                  			 	}); 
				                 	  	}); 
				                   	//Return image 
				                   	return $img; 
				                   	} 
				                },
								environments:{
				                	title: '',
			                	    width: "5%",
			                	    edit: true,
			                	    create: false,
			                          	display: function (activityEnvData) { 
			                   		//Create an image that will be used to open child table 
			                   			var $img = $('<img src="css/images/list_metro.png" title="Environment Combinations" />'); 
			                   			//Open child table when user clicks the image 
			                   			$img.click(function () { 
			                   				
			                   				// ----- Closing child table on the same icon click -----
			                   			    closeChildTableFlag = closeJtableTableChildContainer($(this), $(jTableContainerId));
			                   				if(closeChildTableFlag){
			                   					return;
			                   				} 
			                   				
			                   				$(jTableContainerId).jtable('openChildTable', 
			                   				$img.closest('tr'), 
			                   					{ 
			                   					title: 'Environment Combination', 
			                   					 editinline:{enable:true},
			                   					recordsLoaded: function(event, data) {
			                   		        	 $(".jtable-edit-command-button").prop("disabled", true);
			                   		         },
			                   					actions: { 
			                   						listAction: 'list.environment.combinations.by.activity?activityId='+activityEnvData.record.activityId,
			                   						//createAction : 'add.clarificationtracker.by.activity',		    									
			    									//editinlineAction : 'update.clarificationtracker.by.activity',	
			                   						}, 
			                   					fields: { 
			                   						  activityId: { 
			                   							type: 'hidden', 
			                   							defaultValue: activityEnvData.record.activityId 
			                   						},  
			                   						envionmentCombinationId: { 
			                       						key: true, 
			                       						create: false, 
			        					                edit: false, 
			                       						list: false
			                       					},
			                       					environmentCombinationName : {
			    										title : '',
			    										inputTitle : 'Environment Combination <font color="#efd125" size="4px">*</font>',
			    										list : true,				    										
			    										create : true,
			    										edit : true,
			    										width : "20%"
			    									},
			    								},
			                   	
			                         formSubmitting: function (event, data) {
			                   
			                        }, 
			                         //Dispose validation logic when form is closed
			                         formClosed: function (event, data) {
			                            data.form.validationEngine('hide');
			                            data.form.validationEngine('detach');
			                        }
			                   	}, function (data) { //opened handler 
			                   			data.childTable.jtable('load'); 
			                   		}); 
			                   	}); 
			                   	//Return image 
			                   	return $img; 
			                   	}
			                },							
							activityTask:{
				                	title: 'Activity Task',
			                	    width: "5%",
			                	    edit: true,
			                	    create: false,
			                          	display: function (activityData) { 
			                   		//Create an image that will be used to open child table 
			                   			var $img = $('<img src="css/images/list_metro.png" title="Activity Tasks" />'); 
			                   			//Open child table when user clicks the image 
			                   			$img.click(function () { 
			                   				
			                   				// ----- Closing child table on the same icon click -----
			                   			    closeChildTableFlag = closeJtableTableChildContainer($(this), $(jTableContainerId));
			                   				if(closeChildTableFlag){
			                   					return;
			                   				} 
			                   				
			                   				$(jTableContainerId).jtable('openChildTable', 
			                   				$img.closest('tr'), 
			                   					{ 
			                   					title: 'Activity Tasks', 
			                   					 editinline:{enable:true},
			                   					recordsLoaded: function(event, data) {
			                   		        	 $(".jtable-edit-command-button").prop("disabled", true);
			                   		         },
			                   					actions: { 
			                   		listAction:'process.activitytask.list?productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&activityWorkPackageId='+activityWorkPackageId+'&activityId='+activityData.record.activityId+'&isActive=1'+'&jtStartIndex=0&jtPageSize=10',
											
									createAction : 'process.activitytask.add',
									editinlineAction : 'process.activitytask.update',
									deleteAction : 'process.activitytask.delete',
			                   						}, 
													
									recordAdded: function (event, data) {
									if(data.serverResponse.Message!='undefined' && data.serverResponse.Message!=""){
										callAlert("Warning: "+data.serverResponse.Message)
									}								
								},													
									recordsLoaded:function(){    	
									 $('.portlet > .portlet-title > .tools > .reload').trigger('click'); 
										//console.log("enableAddOrNot"+enableAddOrNot);
										if(enableAddOrNot == "no"){
											$('jTableContainerId .jtable-toolbar-item-add-record').hide();
											$('#divUploadActivitiesTask').hide();
										 }
								     },
			                   fields: { 
									activityId : {
						 				type: 'hidden', 
						 				defaultValue: activityData.record.activityId
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
										options : 'common.list.activity.tasktype?productId='+productId											
									},
									lifeCycleStageId : {
										title : 'Life Cycle Stage',
										create : true,
										list : true,
										edit : true,
										width : "20%",
										dependsOn : 'activityWorkPackageId',
										defaultValue : activityData.record.lifeCycleStageId,
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
										defaultValue :setJtableFormattedDate(activityData.record.plannedStartDate),
										width : "20%",
									},
									plannedEndDate : {
										title : 'Planned End Date',
										inputTitle : 'Planned End Date <font color="#efd125" size="4px">*</font>',
										edit : allowPlanDateEndDate,										
										create : allowPlanDateEndDate,
										list : true,
										type : 'date',
										defaultValue : setJtableFormattedDate(activityData.record.plannedEndDate),
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
										defaultValue : activityData.record.assigneeId,
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
										defaultValue : activityData.record.reviewerId,
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
									plannedEffort : {
										title : 'Planned Effort',									
										list : true,
										create : true,
										edit : true,
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
        					        	title : 'Workflow Template',
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
							    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=29&entityInstanceId='+data.record.activityTaskId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
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
			                   	}, function (data) { //opened handler 
			                   			data.childTable.jtable('load'); 
			                   		}); 
			                   	}); 
			                   	//Return image 
			                   	return $img; 
			                   	} 
			                },	
			                EnvironmentMapping:{
					        	title : '',
					        	list : true,
					        	create : false,
					        	edit : false,
					        	width: "10%",
					        	display:function (data) { 
					        		//Create an image that will be used to open child table 
				           			var $img = $('<img src="css/images/mapping.png" title="Environment Mapping" data-toggle="modal" />'); 
				           			//Open child table when user clicks the image 
				           			$img.click(function () {
										var activityId = data.record.activityId;
										//var productId =	data.record.productId;					
										// ----- DragDrop Testing started----		dragListItemsContainer
										//var productId = document.getElementById("hdnProductId").value;												
										var leftUrl="envicombi.unmappedto.activity.count?productId="+productId+"&activityId="+activityId;							
										var rightUrl = "";							
										var leftDefaultUrl="activity.unmappedenvironmentcombi.byProduct.list?productId="+productId+"&activityId="+activityId+"&jtStartIndex=0&jtPageSize=10";							
										var rightDefaultUrl="activity.Environmentcombination.list?activityId="+activityId+"&productId="+productId;
										var leftDragUrl = "activity.envcombination.mapping?activityId="+activityId;
									    var rightDragtUrl = "activity.envcombination.mapping?activityId="+activityId;					
										var leftPaginationUrl = "activity.unmappedenvironmentcombi.byProduct.list?productId="+productId+"&activityId="+activityId;
										var rightPaginationUrl="";						
										
										jsonActivityObj={"Title":"Map Environment Combination to Activity",
												"leftDragItemsHeaderName":"Available Environment Combinations",
												"rightDragItemsHeaderName":"Mapped Environment Combinations ",
												"leftDragItemsTotalUrl":leftUrl,
												"rightDragItemsTotalUrl":rightUrl,
												"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
												"rightDragItemsDefaultLoadingUrl":rightDefaultUrl,
												"leftDragItemUrl":leftDragUrl,
												"rightDragItemUrl":rightDragtUrl,									
												"leftItemPaginationUrl":leftPaginationUrl,
												"rightItemPaginationUrl":rightPaginationUrl,									
												"leftDragItemsPageSize":"50",
												"rightDragItemsPageSize":"50",
												"noItems":"No Environment Combinations",
												"componentUsageTitle":"Activity-ECToActivity"
												};
										
										DragDropListItems.init(jsonActivityObj);							
										// DragDrop Testing ended----	           				
				           		  });
				           			return $img;
					        	}
					        },
					        customField:{
					        	title : '',
					        	list: true,
								create:false,
								edit:false,									
								width: "5%",
								display : function(data){
									var $img = $('<i class="fa fa-list-alt" title="Custom Fields"></i>'); 
									$img.click(function () {
					       				var jsonObj={"Title":"Custom Fields For Activity : ["+data.record.activityId+"] "+data.record.activityName,
												"subTitle": "",
												"url": "data/data.json",
												"columnGrouping":2,
												"containerSize": "large",
												"componentUsageTitle":"customFields",
												"entityId":"28",
												"entityTypeId":data.record.activityMasterId,
												"entityInstanceId":data.record.activityId,
												"parentEntityId":"18",
												"parentEntityInstanceId":productId,
												"engagementId":engagementId,
												"productId":productId,
												"singleFrequency":"customFieldSingleFrequencyContainer",
												"multiFrequency":"customFieldMultiFrequencyContainer",
										};
										CustomFieldGropings.init(jsonObj);
					       			});
					       			return $img;
								}
					        },
					        attachment: { 
								title: '', 
								list: true,
								create:false,
								edit:false,									
								width: "5%",
								display:function (data) {	        		
					           		//Create an image that will be used to open child table 
									var $img = $('<img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;">&nbsp;['+data.record.attachmentCount+']&nbsp;</img>'); 
					       			$img.click(function () {
					       				isViewAttachment = false;
					       				var jsonObj={"Title":"Attachments for Activity",			          
					    	    			"SubTitle": 'Activity : ['+data.record.activityId+'] '+data.record.activityName,
					    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=28&entityInstanceId='+data.record.activityId,
					    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=28&entityInstanceId='+data.record.activityId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
					    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
					    	    			"updateURL": 'update.attachment.for.entity.or.instance',
					    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=28',
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
									listActivityAuditHistory(data.record.activityId);
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
							var entityTypeIdComments = 28;
							var entityNameComments = "Activity";
							listComments(entityTypeIdComments, entityNameComments, data.record.activityId, data.record.activityName, "activityComments");
						});
					return $img;
					}
				 },	 
							}, 							
								 formCreated: function (event, data){
					                    var $input_start_time = data.form.find ('input[name="testTimePicker"]');
					                    $input_start_time.datetimepicker();

					                },
								// This is for closing $img.click(function (data) { 
								 formSubmitting: function (event, data) {							 
									 plannedStartDate=$("input[name=plannedStartDate]").val();
									 plannedEndDate=$("input[name=plannedEndDate]").val();
										
							      	    //data.form.find('input[name="activityName"]').addClass('validate[required],custom[maxSize[500]]');
							      	  	data.form.find('input[name="activityName"]').addClass('class="validate[required,funcCall[validateSpecialCharactersExceptDot]]", custom[maxSize[500]]');
							            data.form.find('input[name="assigneeId"]').addClass('validate[required]');		
							            
							            if(new Date(plannedStartDate)>new Date(plannedEndDate))
										{
											$("#basicAlert").css("z-index", "100001");
											callAlert("Warning: From date should be lessthan or equal to Todate");
											return false;
										}							            
							            data.form.find('input[name="plannedStartDate"]').addClass('validate[required]');
							            data.form.find('input[name="plannedEndDate"]').addClass('validate[required]');
							       		data.form.validationEngine();
							           return data.form.validationEngine('validate');
							          }, 
							});
			$(jTableContainerId).jtable('load');
			 
			$("button[role='button']:contains('Close')").click(function () {
		        $('.ui-dialog').filter(function () {
		            return $(this).css("display") === "block";
		        }).find('#ui-error-dialog').dialog('close');
		    });
			
		}

function addActivityCommentsJtable(record){	

	var entityInstanceName = record.activityName;	
	var entityInstanceId = record.activityId;
	var modifiedById = record.assigneeId;
	var currentStatusId = record.statusId;
	var currentStatusName = record.statusName;
	var currentStatusDisplayName = record.statusDisplayName;
	var entityTypeId = 33;//Activity type
	var entityId = record.activityMasterId;
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
