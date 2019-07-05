var isLoad = false;			
var exceptedOutput='';
var listURL = '';
var attachmentTypeURL = '';
var productTypeId = 18;

var productActivityWPJsonObj='';
var ProductActivityWorkPackageViewDetails = function(){  
	var initialise = function(jsonObj){	    
	   listProductAWPDetailsComponent(jsonObj);
	};
	return {
		//main function to initiate the module
        init: function(jsonObj) {        	
       	initialise(jsonObj);
        }		
	};	
	}();

$(document).ready(function(){
	$('#productActivityWPSummaryView').append($('#ProductACTWPSummaryPage'));	
	$('#breadCrumHeader').text('PRODUCT - ACTIVITY WORKPACKAGE');
	if(changeRequestToUsecase == "YES"){
		$('#tablistProductACTWP ul:first li:eq(5) a').text("Use Cases");
		$('#prodChangeRequests #trigUploadChangeRequests').val('Use Cases');
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

$(document).on('click', '#tablistProductACTWP>li', function(){
	productActivityWPselectedTab=$(this).index();	
	productActivityWPTabSelection(productActivityWPselectedTab);
});

function productActivityWPTabSelection(selectedTab){
	console.log("selected "+selectedTab);	
	//ActivityWorkPackage Tab
	
	if(changeRequestToUsecase == "YES"){
		$('#tablistProductACTWP li:eq(5) a').text("Use Cases");
		$('#prodChangeRequests #trigUploadChangeRequests').val('Use Cases');
	}
	
	productId = productActivityWPJsonObj.productId;
	productName = productActivityWPJsonObj.productName;
	productVersionId = 0;
	productBuildId = 0;
	//activityWorkPackageId = 0;
	enableAddOrNot = "yes";
	//var isActive = $("#isActive_ul").find('option:selected').val();	
	
	$('#workflow_status_dd').hide();
	
	$('#toAnimate .portlet .actions').eq(0).css('display','none');	
	if(selectedTab==0){
		// Overview
		showProductAWPSummary(productActivityWPJsonObj);
		
	}else if(selectedTab==1){
		// Dashboard
		showProductDashboard();
		
	}else if(selectedTab==2){
		//AWP Listing
		
		var jsonObj={
			"Title": 'ActivityWorkpackage',			          
			"SubTitle": 'ActivityWorkpackage at Product level',
			"listURL": 'activityWorkPackage.testFactory.product.level?testFactoryId='+testFactId+'&isActive=1&productId='+productId+'&jtStartIndex=0&jtPageSize=10000',
			"creatURL": 'process.workrequest.add',
			"updateURL": 'process.workrequest.update',		
			"containerID": 'jTableContainerProductAWPList',
			"productId": productId,
			"componentUsage": "ActivityAtProductLevel",	
		};	 
		ActivityWorkpackage.init(jsonObj);
		
		//urlToGetWorkRequestsOfSpecifiedProductId = 'activityWorkPackage.testFactory.product.level?testFactoryId=-1&isActive=1&productId='+productId;
		//listActivityWorkPackagesOfSelProduct(urlToGetWorkRequestsOfSpecifiedProductId, '#jTableContainerProductAWPList');		
		
	}else if(selectedTab==3){
		//Product Activities
		
		$('#workflow_status_dd').show();
		var activityWPId=0;
		var enableAddOrNot="yes";
		document.getElementById("treeHdnCurrentProductId").value = productId;
		listActivitiesOfSelectedAWP_DT(0,0,0,activityWPId,1,enableAddOrNot, "productActivities_dataTable");
		$('#toAnimate .portlet .actions').eq(0).css('display','block');
		
	}else if(selectedTab==4){
		//Clarification Listing
		//listClarificationOfSelProdComponent(productId, activityId,activityAssigneeId,testFactId,0, '#jTableContainerProdAWPClarifications');	
		
		var jsonObj={
			"Title": 'ActivityClarification',			          
			"SubTitle": 'ActivityClarification at product level',
			"listURL": 'list.clarificationtracker.by.product?entityTypeId='+productTypeId+'&entityInstanceId='+productId+'&jtStartIndex=0&jtPageSize=10000',
			"creatURL": 'add.clarificationtracker.by.activity',
			"updateURL": 'update.clarificationtracker.by.entityType',		
			"containerID": 'jTableContainerProdAWPClarifications',
			"productId": productId,
			"componentUsage": "ProductLevel",	
		};	 
		ActivityClarification.init(jsonObj);
		
	}else if(selectedTab==5){
		//ChangeRequest Listing	
		//listChangeRequestsOfSelProdComponent(productId, activityId, '#jTableContainerProdAWPChangeRequests');			
			
		var jsonObj={
			"Title": 'ActivityChangeRequest',			          
			"SubTitle": 'ActivityChangeRequest at product level',
			"listURL": 'list.changerequestset.by.product?entityType1='+productTypeId+'&entityInstance1='+productId+'&jtStartIndex=0&jtPageSize=10000',
			"creatURL": 'changerequests.add.by.entityTypeAndInstanceId?entityInstanceId='+productId,
			"updateURL": 'list.change.requests.by.activity.update?activityId='+activityId,		
			"containerID": 'jTableContainerProdAWPChangeRequests',
			"productId": productId,
			"componentUsage": "ProductLevel",	
		};	 
		ActivityChangeRequest.init(jsonObj);
		
	}else if(selectedTab==6){
		//Attachments
		var jsonObj={
			"Title":"Attachments for Product",			          
			"SubTitle": 'Product : ['+productId+'] '+' Product',
			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=18&entityInstanceId='+productId,
			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=18&entityInstanceId='+productId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
			"deleteURL": 'delete.attachment.for.entity.or.instance',
			"updateURL": 'update.attachment.for.entity.or.instance',
			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=18',
			"jtableDivId": 'jTableContainerforProdAWPAttachments',
			"multipleUpload":true,
		};	 
		AttachmentsTab.init(jsonObj);
		
	}else if(selectedTab==7){
		//Audit History
		auditHistoryListURL ='administration.event.list?sourceEntityType=Product&sourceEntityId='+productId;	
		auditHistoryCompListing(auditHistoryListURL, 'jTableContainerforProdAWPAuditHistory');
	}	
}

function listProductAWPDetailsComponent(jsonObj){
	productActivityWPJsonObj=jsonObj;	
	
	productActivityWPselectedTab=$("#tablistProductACTWP>li.active").index();
	productActivityWPTabSelection(productActivityWPselectedTab);
}

function showProductDashboard(){
	//DashBoard
	var productVersionId=0;
	var productBuildId=0;
	var entityTypeId=33;
	var entityTypeName="Activities";
	$('#productDashboardRAGHeaderSubTitle').show();
	$('#productDashboardWorkflowHeaderSubTitle').hide();
	var productRAGDashBoardDiv='productRAGSummary';
	var productRAGSummaryHeaderTitle='productDashboardRAGHeaderSubTitle';
	activityWPId=0;
	showDashBoardEntityInstanceRAGSummary(productRAGDashBoardDiv,productRAGSummaryHeaderTitle,testFactId,prodId,productVersionId,productBuildId,activityWPId,productName, entityTypeId, "", 0, activityWPId);
	
}

function showProductAWPSummary(jsonObj){
	$("#productActWorkPackageDiv #prodName").text('hello');
	$("#productActWorkPackageDiv #productVersionName").text('-hello-');
	$("#productActWorkPackageDiv #prodBuildName").text('-hello-');
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
	//	url : 'workpackage.result.testcase.details?testcaseId='+testcaseId+"&wptcepId="+wptcepId,
		url : jsonObj.productActivityWPURL,
		dataType : 'json',
		success : function(data) {
			var result=data.Records;
			var executionName="";
			var executionTime="";
			var executionStatus="";
			var isapproved = "";
			
			$("#productActWorkPackageDiv #prodIdPS").text('--');
			$("#productActWorkPackageDiv #prodNamePS").text('--');
			$("#productActWorkPackageDiv #testFactIdPS").empty();
			$("#productActWorkPackageDiv #testFactNamePS").empty();
			$("#productActWorkPackageDiv #verIdPS").text('--');
			$("#productActWorkPackageDiv #verNamePS").text('--');
			$("#productActWorkPackageDiv #buildIdPS").text('--');			
			$("#productActWorkPackageDiv #buildNamePS").text('--');
			$("#productActWorkPackageDiv #clarificationsPS").text('--');
			$("#productActWorkPackageDiv #ChangeRequestsPS").text('--');
			$("#productActWorkPackageDiv #attachmentsPS").text('--');
			
			$("#productActWorkPackageDiv #customerPS").text('--');			
			$("#productActWorkPackageDiv #projectCodePS").text('--');			
			$("#productActWorkPackageDiv #projectNamePS").text('--');			
			$("#productActWorkPackageDiv #producttypePS").text('--');			
			$("#productActWorkPackageDiv #productmodePS").text('--');	
			$("#productActWorkPackageDiv #createdOnPS").text('--');			
			$("#productActWorkPackageDiv #createdByPS").text('--');	
			$("#productActWorkPackageDiv #modifiedOnPS").text('--');	
			$("#productActWorkPackageDiv #modifiedByPS").text('--');
			$.each(result, function(i,item){	
				if(item.productId != null){
					$("#productActWorkPackageDiv #prodIdPS").text(item.productId+" / "+item.productName);		
				}
				if(item.description != null){
					$("#productActWorkPackageDiv #descPS").text(item.description);		
				}
				if(item.testFactoryId  != null){
					$("#productActWorkPackageDiv #testFactIdPS").text(item.testFactoryId+" / "+item.testFactoryName);		
				}
				if(item.productVersionListId  != null){
					$("#productActWorkPackageDiv #verIdPS").text(item.productVersionListId+" / "+item.productVersionName);		
				}
				if(item.productBuildId  != null){
					$("#productActWorkPackageDiv #buildIdPS").text(item.productBuildId+" / "+item.buildname);		
				}
				if(item.clarificationCount  != null){
					$("#productActWorkPackageDiv #clarificationsPS").text(item.clarificationCount);		
				}
				if(item.changeRequestCount  != null){
					$("#productActWorkPackageDiv #ChangeRequestsPS").text(item.changeRequestCount);		
				}
				if(item.attachmentsCount  != null){
					$("#productActWorkPackageDiv #attachmentsPS").text(item.attachmentsCount);		
				}			
				
				if(item.customerName  != null){
					$("#productActWorkPackageDiv #customerPS").text(item.customerName);		
				}
				
				if(item.projectCode  != null){
					$("#productActWorkPackageDiv #projectCodePS").text(item.projectCode);		
				}
				
				if(item.projectName  != null){
					$("#productActWorkPackageDiv #projectNamePS").text(item.projectName);		
				}
				
				if(item.typeName  != null){
					$("#productActWorkPackageDiv #producttypePS").text(item.typeName);		
				}
				
				if(item.modeName  != null){
					$("#productActWorkPackageDiv #productmodePS").text(item.modeName);		
				}
				
				if(item.createdDate  != null){
					$("#productActWorkPackageDiv #createdOnPS").text(item.createdDate);		
				}
				
				if(item.createdBy  != null){
					$("#productActWorkPackageDiv #createdByPS").text(item.createdBy);		
				}				
				
				if(item.lastModifiedDate  != null){
					$("#productActWorkPackageDiv #modifiedOnPS").text(item.lastModifiedDate);		
				}
				
				if(item.lastModifiedBy  != null){
					$("#productActWorkPackageDiv #modifiedByPS").text(item.lastModifiedBy);		
				}
				if(item.plannedExecutionDate != null){				
					
					$("#productActWorkPackageDiv #plannedDate").append("<div style='font-size:small;' >"+item.plannedExecutionDate+"</div>");	
				}
				if(item.actualExecutionDate != null){
					$("#productActWorkPackageDiv #actualDate").append("<div  style='font-size:small;' >"+item.actualExecutionDate+"</div>");
				}
							
				if(item.actualExecutionDate != null){
					$("#productActWorkPackageDiv #actualDate").text(item.actualExecutionDate);	
				}
				if(item.environmentCombinationName != null){
					$("#productActWorkPackageDiv #environmentName").text(item.environmentCombinationName);	
				}
			});			
		}
	});	
}

/*
function listActivityWorkPackagesOfSelProduct(urlToGetWorkRequestsOfSpecifiedProductId, jTableContainerId){
	try{
		if ($(jTableContainerId).length>0) {
			 $(jTableContainerId).jtable('destroy'); 
		}
	} catch(e) {}
	$(jTableContainerId).jtable({
        title: 'Activity Work Package List',
        editinline:{enable:true},
        selecting: true,  //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10,
		recordAdded: function (event, data) {
             if(data.serverResponse.Message!='undefined' && data.serverResponse.Message!=""){
					callAlert("Warning: "+data.serverResponse.Message)
			}          				         
		},
          actions: {
        	  listAction:urlToGetWorkRequestsOfSpecifiedProductId,
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
	                 edit : false,
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
					inputTitle : 'Planned Start Date <font color="#efd125" size="4px">*</font>',
					edit : true,
					create : true,
					list : true,
					type : 'date',
					defaultValue : new Date().getMonth()+1+"/"+new Date().getDate()+"/"+new Date().getFullYear(),										
					width : "20%"
				},
				plannedEndDate : {
					title : 'Planned End Date',
					inputTitle : 'Planned End Date <font color="#efd125" size="4px">*</font>',
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
				     	return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
				    }
				},
				productBuildId: {
	                 title: 'Build',
	             	inputTitle: 'Build <font color="#efd125" size="4px">*</font>',
	                 list:true,
		             edit:false,
		             create:true,
		             options: 'process.list.builds.by.product?productId='+productId
	                 
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
	             }, MovingAWP:{
						title : '',
						list : true,
						create : false,
						width: "5%",
						display:function (cloneData) { 
						 var $img = $('<i class="fa fa-scissors" style="width: 24px;height: 24px;" title="Moving Activity Workpackage" data-toggle="modal" />'); 
						$img.click(function () {
							$('#plannedDateDiv').hide();
							openLoaderIcon();
							// -- for Moving workpackage ---
								$.ajax({
									type: "POST",
							        contentType: "application/json; charset=utf-8",
							     //   url: 'administration.product.hierarchy.tree',
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
						if(changeRequestToUsecase == "YES"){
							var $img = $('<img src="css/images/mapping.png" title="Activity Creation from Use Cases" data-toggle="modal" />');
						}else{
							var $img = $('<img src="css/images/mapping.png" title="Activity Creation from ChangeRequest" data-toggle="modal" />');
						}
						 
						//Open child table when user clicks the image 
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
		        	title : 'Workflow Template',
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
				 commentsProductAWP:{
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
							var entityTypeIdComments = 34;
							var entityNameComments = "ActivityWorkpackage";
							listComments(entityTypeIdComments, entityNameComments, data.record.activityWorkPackageId, data.record.activityWorkPackageName, "productLevelAWPComments");
						});
					return $img;
					}
				 },     
        },  // This is for closing $img.click(function (data) { 
        formSubmitting: function (event, data) {
      	  //data.form.find('input[name="activityWorkPackageName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[maxSize[100]]');
      	  	data.form.find('input[name="activityWorkPackageName"]').addClass('class="validate[required,funcCall[validateSpecialCharactersExceptDot]]", custom[maxSize[100]]');
            data.form.find('select[name="ownerId"]').addClass('validate[required]');
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

*/

function updateModifiedValue(type){
	 var modifiedValue;
	 var modifiedField;
	 var activityWorkPackageId= document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
	 var thediv = document.getElementById('reportbox');
	 
	 if(type=='act_wpkg_name'){
		 modifiedValue= $('#productActWorkPackageDiv #act_wpkg_name').val();
	 }
	if(type=='act_wpkg_desc'){
		modifiedValue=$('#productActWorkPackageDiv #act_wpkg_desc').val();
	 }
  	 
	 if(type == 'start' || type == 'end'){
		 modifiedValue = $("#productActWorkPackageDiv#hdnStartDateAWP").val() + "~" + $("#hdnEndDateAWP").val();
	 }
	 if(type == 'plannedStartDate' || type == 'plannedEndDate'){
		 modifiedValue = $("#productActWorkPackageDiv #hdnStartDateAWP").val() + "~" + $("#hdnEndDateAWP").val();
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
function listClarificationOfSelProdComponent(productId, activityId,activityAssigneeId,testFactId, id, jTableContainerId) { 
			var urlToGetClarificationTrackersByProdID = 'list.clarificationtracker.by.product?entityTypeId='+productTypeId+'&entityInstanceId='+productId;
			var plannedActivitySize=0;
			
			try {
				if ($(jTableContainerId).length > 0) {
					$(jTableContainerId).jtable('destroy');
				}
			} catch (e) {
			}
			$(jTableContainerId).jtable( 
       					{ 
       						title: 'Clarification', 
       					 	editinline:{enable:true},
       					 editInlineRowRequestModeDependsOn: true,
       					 paging : true, //Enable paging								
							pageSize : 10,
       					actions: { 
       						listAction: urlToGetClarificationTrackersByProdID,
       						createAction : 'add.clarificationtracker.by.activity',	    									
							editinlineAction : 'update.clarificationtracker.by.entityType',
       						},
      					recordsLoaded: function(event, data) {
      		        		$(".jtable-edit-command-button").prop("disabled", true);      		        		
      		         	},
    		         	recordUpdated:function(event, data){
    		         		$(jTableContainerId).find('.jtable-main-container .reload').trigger('click');
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
								defaultValue: productTypeId
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
       							defaultValue: productId
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
							},
							attachment: { 
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
							listComments(entityTypeIdComments, entityNameComments, data.record.clarificationTrackerId, data.record.clarificationTitle, "productLevelclarificationTrackerComments");
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
			$(jTableContainerId).jtable('load');
}
*/

var changeRequestTitle = "";
var addPoPupChangeRequestName  = "";
function crTOUseCase(){
	if(changeRequestToUsecase == "YES"){		
		changeRequestTitle = "Use Cases";	
		addPoPupChangeRequestName = 'Use Case Name <font color="#efd125" size="4px">*</font>';
		//$('#prodChangeRequests').text("Use Case");
	}else{
		changeRequestTitle = "Change Request";
		addPoPupChangeRequestName = 'Change Request Name <font color="#efd125" size="4px">*</font>';
	}
			return changeRequestTitle;
}

/*
function listChangeRequestsOfSelProdComponent(productId, activityId, jTableContainerId) { 
			var urlToGetChangeRequestsOfSpecifiedProdId = 'list.changerequestset.by.product?entityType1='+productTypeId+'&entityInstance1='+productId;
			var plannedActivitySize=0;
			
			var jtableTitle = crTOUseCase();
			try {
				if ($(jTableContainerId).length > 0) {
					$(jTableContainerId).jtable('destroy');
				}
			} catch (e) {
			}
       				$(jTableContainerId).jtable( 
       					{						                   	      	   
       						//title: 'Change Request',	
       						title : jtableTitle,
       						editinline:{enable:true},       
       						paging : true, //Enable paging								
							pageSize : 10,
       					actions: { 
       						listAction: urlToGetChangeRequestsOfSpecifiedProdId,
       						createAction : 'changerequests.add.by.entityTypeAndInstanceId?entityInstanceId='+productId,
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
								defaultValue: productTypeId
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
								defaultValue: productId,
							},
           					changeRequestName: { 					                   							
       							title : 'Name',
       							//inputTitle : 'Change Request Name <font color="#efd125" size="4px">*</font>',
       							inputTitle :	addPoPupChangeRequestName,
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
											var titleCrtoUc = '';
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
							var entityNameComments = "ClarificationTracker";
							listComments(entityTypeIdComments, entityNameComments, data.record.changeRequestId, data.record.changeRequestName, "productLevelCRComments");
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
       				$(jTableContainerId).jtable('load');
			} */

function showProductGroupDashboardSummaryTableTool(tabVisible){
	if(tabVisible == 1){
		$('#productWorkflowSummary').hide();
		$('#productDashboardWorkflowHeaderSubTitle').hide();
		$('#productDashboardRAGHeaderSubTitle').show();
		$('#productRAGSummary').show();
	} else {
		$('#productRAGSummary').hide();
		$('#productWorkflowSummary').show();
		$('#productDashboardWorkflowHeaderSubTitle').show();
		$('#productDashboardRAGHeaderSubTitle').hide();
		var productWorkflowDashBoardDiv='productWorkflowSummary';
		var productWorkflowSummaryHeaderTitle='productDashboardWorkflowHeaderSubTitle';
		
		//DashBoard
		var productVersionId=0;
		var productBuildId=0;
		var entityTypeId=33;
		var entityTypeName="Activities";
		var WPId=0;
		
		showDashBoardWorkflowEntityInstanceStatusSummary(productWorkflowDashBoardDiv,productWorkflowSummaryHeaderTitle,testFactId,prodId,productVersionId,productBuildId,activityWPId,productName, entityTypeId, entityTypeName, 0,WPId);
	}
}


var jsonUserMappingForWP='';
function workPackageUserMapping($img, proId, entityTypeId, entityInstanceId,entityInstanceName){
	if(proId == null){
		proId = productId;
	}
	var leftUrl="entity.user.group.un.mapped.count?productId="+proId+"&entityTypeId="+entityTypeId+"&entityInstanceId="+entityInstanceId;							
	var rightUrl = "";
	var leftDefaultUrl="entity.user.group.un.mapped.list?productId="+proId+"&entityTypeId="+entityTypeId+"&entityInstanceId="+entityInstanceId;
	var rightDefaultUrl = "entity.user.group.mapped.list?entityTypeId="+entityTypeId+"&entityInstanceId="+entityInstanceId;									
	var leftDragUrl = "entity.user.group.map.or.unmap?productId="+proId+"&entityTypeId="+entityTypeId+"&entityInstanceId="+entityInstanceId;
	var rightDragtUrl = "entity.user.group.map.or.unmap?productId="+proId+"&entityTypeId="+entityTypeId+"&entityInstanceId="+entityInstanceId;
	var leftPaginationUrl = "entity.user.group.un.mapped.list?productId="+proId+"&entityTypeId="+entityTypeId+"&entityInstanceId="+entityInstanceId;
	var rightPaginationUrl="";									
	jsonUserMappingForWP={
		"Title":"User Mapping to WorkPackage - "+entityInstanceName,
		"leftDragItemsHeaderName":"Available User",
		"rightDragItemsHeaderName":"Mapped User",
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
		"noItems":"No User to show",	
		"componentUsageTitle":"userForGroup",											
	};
	
	DragDropListItems.init(jsonUserMappingForWP); 
	$('#dropDownWorkpacakge_dd').hide();
}


