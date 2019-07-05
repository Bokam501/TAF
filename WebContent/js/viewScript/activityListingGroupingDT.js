// ----- converting DT -------

var priorityHeaderTitle='';
var plannedActivitySizeHeaderTitle='';
var actualActivitySizeHeaderTitle='';
var plannedActivitySize=0;
var jTableContainerDataTable='';
var testFactoryId=0;//TODO
var currentProductId=0;//TODO 
var pokeRadioBtnEsc="";
var pokeRadioBtnNrml="";
var pokeSubjectMsg="";
var activityNameForSub="";
var ccMailId="";
var toMailContentNrml="";
var toMailContentEsc="";

//var defaultuserId = '${userId}';

var clearTimeoutPaginationFlagDT='';
var clearTimeoutFlag=false;
function adjustTimeoutFlag(){	
	clearTimeoutPaginationFlagDT =setTimeout(function(){				
		clearTimeoutFlag=false;
	},100);
}

function showActivityAtEngagementLevel(tabSelectedIndex){
	$('#toAnimate .portlet .actions').eq(0).css('display','none');
	adjustTimeoutFlag();
	
	 if(!clearTimeoutFlag){
		clearTimeoutFlag=true;
			
		if(tabSelectedIndex == 0){         // ----- Overview -----
			$('#engagementDashBoard').hide();
			$('#listingDIV').hide();
			$('#listingActivitiesDIV').hide();		
			$('#engagementOverview').show();
			$('#workflow_status_dd').hide();
	
			var jsonObj={
				"Title":"EngageMent Details :- "+ testFactId + "-"+engagementName,
			    "testFactId":testFactId,
				"engagementName":engagementName, 
				"AWEngageMentDetailsTitle":"Enagement WorkPackageDetails",
				"EnageMentActivityWPURL":"engagement.awp.summary?testFactId="+testFactId,
			};
			EngGageMentActivityWorkPackageViewDetails.init(jsonObj);		
			
		}else if(tabSelectedIndex == 1){      // ----- Dashboard -----
			$('#engagementOverview').hide();
			$('#listingDIV').hide();
			$('#listingActivitiesDIV').hide();		
			$('#engagementDashBoard').show();
			$('#workflow_status_dd').hide();
			
			showEngageMentDashBoard();
		
		}else if(tabSelectedIndex == 2){          // ----- Workpackages -----
			$('#engagementOverview').hide();
			$('#engagementDashBoard').hide();
			$('#listingActivitiesDIV').hide();
			$('#workflow_status_dd').hide();
				
			$('#listingDIV').show();
			
			var jsonObj={
				"Title": 'ActivityWorkpackage',			          
				"SubTitle": 'ActivityWorkpackage at Engagement level',
				"listURL": 'activityWorkPackage.testFactory.product.level?testFactoryId='+testFactId+'&isActive=1&productId=-1&jtStartIndex=0&jtPageSize=10000',
				"creatURL": 'process.workrequest.add',
				"updateURL": 'process.workrequest.update',		
				"containerID": 'jTableContainerActivityWP',
				"productId": prodId,
				"componentUsage": "ActivityAtEngagementLevel",	
			};	 
			ActivityWorkpackage.init(jsonObj);
		
		}else if(tabSelectedIndex == 3){          // ----- Activities -----
			$('#engagementOverview').hide();
			$('#engagementDashBoard').hide();
			$('#listingDIV').hide();
			
			$('#listingActivitiesDIV').show();
			$('#workflow_status_dd').show();
			
			var activityWPId=0;
			var enableAddOrNot="yes";	
			listActivitiesOfSelectedAWP_DT(0,0,0,activityWPId,1,enableAddOrNot, "activitiesWP_dataTable");
			$('#toAnimate .portlet .actions').eq(0).css('display','block');
		}
	 }
}

var isActivityGanttChartModified=false;
function activityGanttCloseHandler(){
	// ---- reloading ActivityDataTable -----
	if(isActivityGanttChartModified){
		activityWPTabSelection(2); // 2 = Activity Tab;
		isActivityGanttChartModified=false;
	}
}

var customFieldResultObjCollection=[];
function listActivitiesOfSelectedAWP_DT(productId,productVersionId,productBuildId,activityWorkPackageId,isActive,enableAddOrNot, jTableContainerId) {
	jTableContainerDataTable = jTableContainerId;	
	openLoaderIcon();
	priorityHeaderTitle="Priority";
	plannedActivitySizeHeaderTitle="Planned Activity Size";
	actualActivitySizeHeaderTitle="Actual Activity Size";
	plannedActivitySize=0;
	
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
	
	if(document.getElementById("treeHdnCurrentActivityWorkPackageId").value != "")
		activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
	
	// ----- ended ----
	
	var activityListUrl = "process.activity.engagement.list?engagementId="+engagementId+"&productId";
	if(jTableContainerDataTable == "activitiesWP_dataTable"){
		productId=0;
		productVersionId=0;
		productBuildId=0;
		activityWorkPackageId=0;
		
	}else if(jTableContainerDataTable == "jTableContainerWPActivities_dataTable"){
	
	}else if(jTableContainerDataTable == "productActivities_dataTable"){
		productVersionId=0;
		productBuildId=0;
		activityWorkPackageId=0;
	
	}else if(jTableContainerDataTable == "assigneeActivitiesContainer" || 
			jTableContainerDataTable == "pendingWithActivitiesContainer" ||
			jTableContainerDataTable == "passingThroughContainer"){
		productVersionId=0;
		productBuildId=0;		
	}
	
	if(productId == "") {
		productId=0;
	}

	// ----- custom field activity list -----
	customFieldResultObjCollection=[];
	customFieldActivityCreationFlag=false;

	if(nodeType == "TestFactory".toLowerCase()){
		parentEntityId = 13;
		parentEntityInstanceId = engagementId;					
	}else if(nodeType == "Product".toLowerCase()){
		parentEntityId = 18;
		parentEntityInstanceId = productId;					
	}else{
		parentEntityId = 18;
		parentEntityInstanceId = productId;
	}	
	//customFieldUrl ='custom.field.exist.for.entity?entityId=33&entityTypeId=14&parentEntityId='+parentEntityId+'&parentEntityInstanceId='+parentEntityInstanceId;
	customFieldUrl ='custom.field.exist.for.entity.engagementId.and.productId?entityId=33&parentEntityId='+parentEntityId+'&parentEntityInstanceId='+parentEntityInstanceId;
	activityCustomFieldHandler(productId,productVersionId,productBuildId,activityWorkPackageId,isActive,enableAddOrNot,jTableContainerId,customFieldUrl,"customFieldID",activityListUrl);
}

$(document).on('change','#workflow_status_ul', function() {
	
	productId=document.getElementById("treeHdnCurrentProductId").value;
	if(document.getElementById("treeHdnCurrentProductVersionId").value != "")
		productVersionId = document.getElementById("treeHdnCurrentProductVersionId").value;
	
	if(document.getElementById("treeHdnCurrentProductBuildId").value != "")
		productBuildId = document.getElementById("treeHdnCurrentProductBuildId").value;
	
	if(document.getElementById("treeHdnCurrentActivityWorkPackageId").value != "")
		activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
	
	// ----- At engagement level Activities -----
	var urlToGetActivitiesOfSpecifiedAWPId_URL='';
	var url = "process.activity.engagement.list?engagementId="+engagementId+"&productId";
	if(jTableContainerDataTable == "activitiesWP_dataTable"){
		productId=0;
		productVersionId=0;
		productBuildId=0;
		activityWorkPackageId=0;		
		urlToGetActivitiesOfSpecifiedAWPId_URL = url+'='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&activityWorkPackageId='+activityWorkPackageId+'&isActive=1&statusType='+statusType;
		
	}else if(jTableContainerDataTable == "jTableContainerWPActivities_dataTable"){
		//url = "process.activity.list?productId";
		urlToGetActivitiesOfSpecifiedAWPId_URL = url+'='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&activityWorkPackageId='+activityWorkPackageId+'&isActive=1&statusType='+statusType;
	
	}else if(jTableContainerDataTable == "productActivities_dataTable"){
		productVersionId=0;
		productBuildId=0;
		activityWorkPackageId=0;
		//url = "process.activity.list?productId";		
		urlToGetActivitiesOfSpecifiedAWPId_URL = url+'='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&activityWorkPackageId='+activityWorkPackageId+'&isActive=1&statusType='+statusType;
	
	}else if(jTableContainerDataTable == "assigneeActivitiesContainer" || 
			jTableContainerDataTable == "pendingWithActivitiesContainer" || 
			jTableContainerDataTable == "passingThroughContainer"){
		productVersionId=0;
		productBuildId=0;
		activityWorkPackageId=0;
		urlToGetActivitiesOfSpecifiedAWPId_URL = myActivitiesSelectedTabUrl;
	}
	
	if(productId == "") {
		productId=0;
	}
	
	var statusType = $("#workflow_status_dd").find('option:selected').val();
	//var urlToGetActivitiesOfSpecifiedAWPId_URL = url+'='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&activityWorkPackageId='+activityWorkPackageId+'&isActive=1&statusType='+statusType;	
	var url = urlToGetActivitiesOfSpecifiedAWPId_URL+'&jtStartIndex=0&jtPageSize=10000';
	assignDataTableValuesInActivityManagementTab(url, "160px", "parentTable", activityWorkPackageId, productId);
 });

//----- Making ajax request for the dataTable ----- 
var activityManagementTabDTJsonData='';
var activityManagementOptionsArr=[];
var activityManagementTabResultArr=[];
var activityManagementOptionsItemCounter=0;
var optionsType_ActivityTab="ActivityManagementTab";
var optionsType_ActivityChangeRequest="ActivityChangeRequest";
var optionsType_ActivityTask="ActivityTask";

var activityManagementTab_oTable='';
var activityManagementTab_editor='';

function assignDataTableValuesInActivityManagementTab(url, value, tableValue, row, tr){
	openLoaderIcon();
	
	if(tableValue == "parentTable")
		$("#addCommentsLoaderIcon").show();	
	
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			$("#addCommentsLoaderIcon").hide();
			
			if(data == null || data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			
			if(tableValue == "parentTable"){
				activityManagementTabDTJsonData = data;								
				var customFieldValue=0;
				for(var i=0;i<customFieldResultObjCollection.length;i++){
					getJsonCustomFieldObject(customFieldResultObjCollection[i]['id'], customFieldResultObjCollection[i]['fieldName'], activityManagementTabDTJsonData);					
					//console.log("id : "+customFieldResultObjCollection[i]['id']+' value : '+customFieldValue);						
				}				

				// ----- At engagement level Activities -----
				if(jTableContainerDataTable == "activitiesWP_dataTable"){
					customFieldResultObjCollection=[];
					
				}else if(jTableContainerDataTable == "jTableContainerWPActivities_dataTable" ||
						jTableContainerDataTable == "assigneeActivitiesContainer"){
					// ----- customFieldValue -----					
					if(jTableContainerDataTable == "assigneeActivitiesContainer" && nodeType == "testfactory"){
						customFieldResultObjCollection=[];
					}
					
				}else if(jTableContainerDataTable == "productActivities_dataTable"){
					//customFieldResultObjCollection=[];
					
				}else if(jTableContainerDataTable == "pendingWithActivitiesContainer" || 
						jTableContainerDataTable == "passingThroughContainer"){
					//customFieldResultObjCollection=[];
				}				
				activityManagementResults_Container(data, value, row, tr);
				
			}else if(tableValue == "childTable1"){
				data = convertDTDateFormat(data, ["raisedDate"]);				
				activityChangeRequestResults_Container(data, row, tr);
				
				//fixDataTablePopupHeight('activityChangeRequest_dataTable');
				
			}else if(tableValue == "childTable2"){							
				activityEnvironmentCombination_Container(data, row, tr);				
				
			}else if(tableValue == "childTable3"){				
				activityTaskResults_Container(data, value, row, tr);
				$("#activitySummaryLoaderIcon").hide();	
				
			}else{
				console.log("no child");
			}
			
		  },
		  error : function(data) {
			 closeLoaderIcon();  
			 $("#addCommentsLoaderIcon").hide();
			 $("#activitySummaryLoaderIcon").hide();
		 },
		 complete: function(data){
			closeLoaderIcon();
			$("#addCommentsLoaderIcon").hide();
			$("#activitySummaryLoaderIcon").hide();
		 }
	});	
}
var acitivityChangeRequest_oTable='';
var acitivityChangeRequest_editor='';

function createEditorForactivityChangeRequest(data, row){
	
	acitivityChangeRequest_editor = new $.fn.dataTable.Editor( {
			    "table": "#activityChangeRequest_dataTable",
				ajax: "changerequests.add.by.entityTypeAndInstanceId?entityInstanceId="+row.data().activityId,
				ajaxUrl: "list.change.requests.by.activity.update?activityId="+row.data().activityId,			
				
				"idSrc":  "changeRequestId",
				i18n: {
	    	        create: {
	    	           // title:  'Add/Edit Change Request',
	    	        	title: crTOUseCaseDT(),
	    	            submit: "Create",
	    	        }
	    	    },
				fields: [ {
					//label: 'Change Request Name <font color="#efd125" size="4px">*</font>',
					label: addPoPupChangeRequestNameDT,
		            name: "changeRequestName",		            
		        },{
		            label: "Description",
		            name: "description",					
		        },{
		            label: "Priority",
		            name: "priorityId",
					options: activityManagementTabResultArr[0],
		            "type"  : "select",	
		        },		        
		        {
		            label: "Planned Value",
		            name: "planExpectedValue",
					def: 1,		            
		        },{
		            label: "Owner",
		            name: "ownerId",
					options: activityManagementTabResultArr[1],
		            "type"  : "select",	
		            
		        },{
		            label: "Raised Date:",
		            name: "raisedDate",		            
					type:  'datetime',
					def:    function () { return new Date(); },
					format: 'M/D/YYYY',
		        },{
					label: "entityType1",
					name: "entityType1",
					"type": "hidden",
					def: activityMasterTypeId, 	
				},{
					label: "changeRequestId",
					name: "changeRequestId",
					"type": "hidden",					
				},{
					label: "attachmentCount",
					name: "attachmentCount",
					"type": "hidden",	
					 def: 0,	
				},{
					label: "entityInstance1",
					name: "entityInstance1",
					"type": "hidden",
					def: row.data().activityId, 	
				},		        
		    ]
			});
			
		// ----- focus area -----	
			
		$( 'input', acitivityChangeRequest_editor.node()).on( 'focus', function () {
			this.select();
		});
}

function activityChangeRequest_Container(data, row, tr){
	try{
		if ($("#activityTabContainer").children().length>0) {
			$("#activityTabContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = activityTabChild1Table(); 			 
	$("#activityTabContainer").append(childDivString);
		
	// --- editor for the activity Change Request started -----
	createEditorForactivityChangeRequest(data, row);
	var titleCrUc = "";
	
	 if(changeRequestToUsecase == "YES"){
		   titleCrUc = "Activity Use Cases";
		   
	   }else{
		   
		   titleCrUc = "Activity Change Request";
	   }
	 
	acitivityChangeRequest_oTable = $("#activityChangeRequest_dataTable").dataTable( {
		 	dom: "Bfrtilp",
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bSort": false,
	       "bScrollCollapse": true,
	       /*fixedColumns:   {
	           leftColumns: 1,
	       }, */
	       "fnInitComplete": function(data) {
			   reInitializeDTActivityChangeRequest();			   
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: acitivityChangeRequest_editor },	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: titleCrUc,
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: titleCrUc,
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: titleCrUc,
		                    	exportOptions: {
		                            columns: ':visible'
		                        },
		                        orientation: 'landscape',
		                        pageSize: 'LEGAL'
		                    },	                    
		                ],	                
		            },
		            'colvis'
	         ], 	         
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "changeRequestId",className: 'disableEditInline', sWidth: '15%' },		
           { mData: "changeRequestName",className: 'editable', sWidth: '20%' },
           { mData: "description",className: 'editable', sWidth: '20%' },           
			{ mData: "priorityName", className: 'editable', sWidth: '20%', editField: "priorityId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(acitivityChangeRequest_editor, 'priorityId', full.priorityId);
					 }
					 else if(type == "display"){
						data = full.priorityName;
					 }	           	 
					 return data;
				 },
			},	
           { mData: "planExpectedValue",className: 'editable', sWidth: '12%' },		           	
		   { mData: "ownerName", className: 'editable', sWidth: '15%', editField: "ownerId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(acitivityChangeRequest_editor, 'ownerId', full.ownerId);
					 }
					 else if(type == "display"){
						data = full.ownerName;
					 }	           	 
					 return data;
				 },
			},
           { mData: "raisedDate", className: 'disableEditInline', sWidth: '15%',
           		mRender: function (data, type, full) {
		           	 if (full.action == "create"){
		           		data = convertDTDateFormatAdd(data, ["raisedDate"]);		           		
		           	 }else if(type == "display"){
		           		data = full.raisedDate;
		           	 }	           	 
		             return data;
	             }
           },
			{ mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
     	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<img src="css/images/attachment.png" class="activityChangeRequestImg1" title="Attachment" style="width: 18px;height: 18px;">&nbsp;['+data.attachmentCount+']&nbsp;</img></button>'+
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="fa fa-search-plus activityChangeRequestImg2" title="Audit History"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },
       ],       
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		 
		 $("#activityTabLoaderIcon").hide();
		 
		 $("#activityChangeRequest_dataTable_length").css('margin-top','8px');
		 $("#activityChangeRequest_dataTable_length").css('padding-left','35px');
		 
		 disableCreateButton("activityChangeRequest_dataTable");
		 
		 // Activate an inline edit on click of a table cell
        $('#activityChangeRequest_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	acitivityChangeRequest_editor.inline( this, {
        		submitOnBlur: true
			}); 
		});
			
		// ------ Attachments -----
		 
		 $('#activityChangeRequest_dataTable tbody').on('click', 'td button .activityChangeRequestImg1', function () {
				var tr = $(this).closest('tr');
			    var row = acitivityChangeRequest_oTable.DataTable().row(tr);
			    				
				isViewAttachment = false;
				var jsonObj={"Title":"Attachments for ChangeRequest",			          
					"SubTitle": 'ChangeRequest : ['+row.data().changeRequestId+'] '+row.data().changeRequestName,
					"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=42&entityInstanceId='+row.data().changeRequestId,
					"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=42&entityInstanceId='+row.data().changeRequestId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
					"deleteURL": 'delete.attachment.for.entity.or.instance',
					"updateURL": 'update.attachment.for.entity.or.instance',
					"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=42',
					"multipleUpload":true,
					
				};	 
				Attachments.init(jsonObj);
			    
		 });
		 // ----- ended -----
		 
		 // ----- Audit History -----
		 
		 $('#activityChangeRequest_dataTable tbody').on('click', 'td button .activityChangeRequestImg2', function () {
			 
			 var titleCrtoUc = '';
				if(changeRequestToUsecase == "YES"){
					titleCrtoUc = "Use Cases";
				}else{
					titleCrtoUc = "ChangeRequest";
				}
				var tr = $(this).closest('tr');
			    var row = acitivityChangeRequest_oTable.DataTable().row(tr);
				
				listGenericAuditHistory(row.data().changeRequestId,titleCrtoUc,"changeRequestAudit");				
		 });
		 
		 // ----- ended -----	
		
	});
	// ------	
}

// ----- Return options ----

function returnOptionsItemForActivityManagement(url, value, data, row, tr){
	$.ajax( {
 	   "type": "POST",
        "url":  url,
        "dataType": "json",
         success: function (json) {
         if(json == null || json.Result == "ERROR" || json.Result == "Error" || json.Options == null){
         	if(json.Message !=null) {
         		callAlert(json.Message);
         	}
         	json.Options=[];
         	activityManagementTabResultArr.push(json.Options);
         	
         	if(activityManagementOptionsArr[0].type == optionsType_ActivityTab){				  
         		returnOptionsItemForActivityManagement(data, value, data, row, tr);     			   
  		   }else if(activityManagementOptionsArr[0].type == optionsType_ActivityChangeRequest){
  			  activityChangeRequest_Container(data, row, tr);
  		   }else if(activityManagementOptionsArr[0].type == optionsType_ActivityTask){
  			  activityActivityTaskResults_Container(data, value, row, tr);
  		   } 
         	
         }else{
        	 if(activityManagementOptionsArr[0].type == optionsType_ActivityTab && activityManagementOptionsItemCounter == 2){
        		 var autoAllocateUser = {
        				 'label' : 'AutoAllocate',
        				 'DisplayText' : 'AutoAllocate',
        				 'value' : -4,
        				 'Value' : -4
        		 };
        		 var userListArray = [];
        		 if(autoAllocateFromProperty == "Yes") {
        		 	userListArray.push(autoAllocateUser);
        	 	 }
        		 if(json.Options.length>0){     		   
        			 for(var i=0;i<json.Options.length;i++){
        				 userListArray.push(json.Options[i]);
        			 }			   
        		 }
        		 json.Options = userListArray.slice(0);
        	 }
        	 if(json.Options.length>0){     		   
			   for(var i=0;i<json.Options.length;i++){
				   json.Options[i].label=json.Options[i].DisplayText;
				   json.Options[i].value=json.Options[i].Value;
			   }			   
     	   }else{
     		  json.Options=[];
     	   }     	   
     	   activityManagementTabResultArr.push(json.Options);
     	   
     	   if(activityManagementOptionsItemCounter<activityManagementOptionsArr.length-1){
     		 returnOptionsItemForActivityManagement(activityManagementOptionsArr[activityManagementTabResultArr.length].url, value, data, row, tr);     		  
     	   }else{
     		   if(activityManagementOptionsArr[0].type == optionsType_ActivityTab){
				  
				 $("#"+jTableContainerDataTable+" thead").html('');
    			 $("#"+jTableContainerDataTable+" thead").append(activityManagementHeader());
				  
     			  //var totalDefaultActivityColumnCount=32;    			  
     			  //var total = totalDefaultActivityColumnCount + customFieldResultObjCollection.length;     			  
     			  //var emptytr = emptyTableRowAppending(total+2);  // total coulmn count     			 		 
				  
				  var totalDefaultActivityColumnCount=0;    			  
				  var total = $("#"+jTableContainerDataTable+" thead tr th").length;
				  var emptytr = emptyTableRowAppending(total);
				  totalDefaultActivityColumnCount = total - customFieldResultObjCollection.length;     			  
				  
     			  $("#"+jTableContainerDataTable+" tfoot tr").html('');     			  
     			  $("#"+jTableContainerDataTable+" tfoot tr").append(emptytr);
     			 
     			 var cloned_array = [];
     			 cloned_array = [].concat(activityInvisibleColumnValue);
     			 var cloneIndex=0; 
     			  for(var i=0;i<cloned_array.length;i++){
    				cloneIndex = activityInvisibleColumnValue.indexOf(cloned_array[i]);
     				if(cloned_array[i]>=totalDefaultActivityColumnCount && cloneIndex != -1){
     					activityInvisibleColumnValue.splice(cloneIndex, 1);    					
     				}  
     			  }
     			  // -- removing custom fields at the end of dataTable --
     			  var lastElement = $("#"+jTableContainerDataTable+" thead tr th").length;
     			 activityInvisibleColumnValue.push(lastElement-1);
     			  
     			  activityManagementTabResultsEditor(row); 
				  
				  data = convertDTDateFormat(data, ["plannedStartDate"]);
				  data = convertDTDateFormat(data, ["plannedEndDate"]);
				  data = convertDTDateFormat(data, ["actualStartDate"]);
				  data = convertDTDateFormat(data, ["actualEndDate"]);
				  data = convertDTDateFormat(data, ["baselineStartDate"]);
				  data = convertDTDateFormat(data, ["baselineEndDate"]);
				  
     			  activityManagementTabResultsContainer(data, value);  	
     			  
     		   }else if(activityManagementOptionsArr[0].type == optionsType_ActivityChangeRequest){
     			  activityChangeRequest_Container(data, row, tr);
				  
     		   }else if(activityManagementOptionsArr[0].type == optionsType_ActivityTask){
				   
				  data = convertDTDateFormat(data, ["plannedStartDate"]);
				  data = convertDTDateFormat(data, ["plannedEndDate"]);
				  data = convertDTDateFormat(data, ["actualStartDate"]);
				  data = convertDTDateFormat(data, ["actualEndDate"]);
				  data = convertDTDateFormat(data, ["baselineStartDate"]);
				  data = convertDTDateFormat(data, ["baselineEndDate"]);
				  
     			 activityActivityTaskResults_Container(data, value, row, tr);
     		   }  
     	   }
     	   activityManagementOptionsItemCounter++;     	   
         }
         },
         error: function (data) {
        	 activityManagementOptionsItemCounter++;
        	 
         },
         complete: function(data){
         	//console.log('Completed');
         	
         },	            
   	});	
}

// ----- Ended -----

function activityManagementHeader(){
	var customFieldTH='';
	for(var i=0;i<customFieldResultObjCollection.length;i++){
		customFieldTH +='<th>'+customFieldResultObjCollection[i]['fieldName']+'</th>';
	}	
	customFieldTH +='<th></th><th>customFields</th>';
	
		var tr = '<tr>'+			
			'<th></th>'+
			'<th>Activity Name</th>'+
			'<th>Engagement</th>'+
			'<th>Product</th>'+
			'<th>Work Package</th>'+
		    '<th>Activity Type</th>'+
		    '<th>Requirement</th>'+
		    '<th>Life Cycle Stage</th>'+
		    '<th>Planned Start Date</th>'+
		    '<th>Planned End Date</th>'+
		    '<th></th>'+
		    '<th>Actual Start Date</th>'+
		    '<th>Actual End Date</th>'+
		    '<th>isModified</th>'+
		  	'<th>Status</th>'+
		    '<th>Pending With</th>'+
		    '<th>Complete By</th>'+
		    '<th>Time Left</th>'+
		    '<th>Assignee</th>'+		
		  	'<th>Reviewer</th>'+
		  	'<th>Priority</th>'+
		    '<th>Complexity</th>'+
		    '<th>Tracker Number</th>'+		              	
		  	'<th>Category</th>'+
		    '<th>Remarks</th>'+
		    '<th>Planned Activity Size</th>'+
		    '<th>Actual Activity Size</th>'+
		    '<th>% Complition</th>'+
		    '<th>Planned Effort</th>'+
			'<th>Total Effort</th>'+	
		    '<th>Status Type</th>'+
		    '<th>Latest Comment</th>'+
		    '<th>Active</th>'+
		    '<th>Predecessors</th>'+	  	
		  	customFieldTH		              									              	
	  	'</tr>';
		return tr;
}

// ----- Environment combination ChildTable 2 -----

function activityTabChild2Table(){
	var childDivString = '<table id="activityEnvironmentCombination_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+			
			'<th>Environment Combination</th>'+	
		'</tr>'+
	'</thead>'+
	'</table>';		
	
	return childDivString;
}

function activityEnvironmentCombination_Container(data, row, tr){
	try{
		if ($("#activityTabContainer").children().length>0) {
			$("#activityTabContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = activityTabChild2Table(); 			 
	$("#activityTabContainer").append(childDivString);
	
	$('#activityEnvironmentCombination_dataTable').dataTable( {
		dom: "Bfrtilp",
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"scrollY":"100%",
		"sScrollX": "90%",
        "sScrollXInner": "100%",        
        "bScrollCollapse": true,
        "bSort": false,
        "fnInitComplete": function(data) {     	   
 	   }, 
		buttons: [		         
		         {
		          extend: "collection",	 
		          text: 'Export',
	              buttons: [
		          {
                    	extend: 'excel',
                    	title: 'Environment Combination',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'csv',
                    	title: 'Environment Combination',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'pdf',
                    	title: 'Environment Combination',
                    	exportOptions: {
                            columns: ':visible',	                            
                        },
                        orientation: 'landscape',
                        pageSize: 'LEGAL'
                    
                    },	                    
                   ],
		         },
		         'colvis',
               ],				        
        aaData:data,		    				 
	    aoColumns: [	    			           
            { mData: "environmentCombinationName", className: 'disableEditInline', sWidth: '5%'},		    				                                              				            
        ],       
        "oLanguage": {
        	"sSearch": "",
        	"sSearchPlaceholder": "Search all columns"
        },
	});	 
	
	$(function(){ // this will be called when the DOM is ready 
		 
		 $("#activityEnvironmentCombination_dataTable_length").css('margin-top','8px');
		 $("#activityEnvironmentCombination_dataTable_length").css('padding-left','35px');
	});
}

// ----- Ended ------

// -- Activity Task Child Table 3 -----

function activityTabChild3Table(parent){
	var childDivString = '<table id="activityTask_dataTable_'+parent+'" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th class="dataTableChildHeaderTitleTH">Task Name</th>'+
			'<th class="dataTableChildHeaderTitleTH">Task Type</th>'+			
			'<th class="dataTableChildHeaderTitleTH">Life Cycle Stage</th>'+	
			'<th class="dataTableChildHeaderTitleTH">Planned Start Date</th>'+
			'<th class="dataTableChildHeaderTitleTH">Planned End Date</th>'+
			'<th class="dataTableChildHeaderTitleTH"></th>'+
			'<th class="dataTableChildHeaderTitleTH">Current Status</th>'+
			'<th class="dataTableChildHeaderTitleTH">Status Pending With</th>'+
			'<th class="dataTableChildHeaderTitleTH">Status Complete By</th>'+			
			'<th class="dataTableChildHeaderTitleTH">Status Time Left</th>'+	
			'<th class="dataTableChildHeaderTitleTH">Assignee</th>'+
			'<th class="dataTableChildHeaderTitleTH">Reviewer</th>'+
			'<th class="dataTableChildHeaderTitleTH">Priority</th>'+
			'<th class="dataTableChildHeaderTitleTH">Complexity</th>'+
			'<th class="dataTableChildHeaderTitleTH">Category</th>'+			
			'<th class="dataTableChildHeaderTitleTH">Environment Combination</th>'+	
			'<th class="dataTableChildHeaderTitleTH">Result</th>'+
			'<th class="dataTableChildHeaderTitleTH">Actual Start Date</th>'+
			'<th class="dataTableChildHeaderTitleTH">Actual End Date</th>'+
			'<th class="dataTableChildHeaderTitleTH">Planned Task Size</th>'+			
			'<th class="dataTableChildHeaderTitleTH">Actual Task Size</th>'+	
			'<th class="dataTableChildHeaderTitleTH">% Completion</th>'+
			'<th class="dataTableChildHeaderTitleTH">Planned Effort</th>'+
			'<th class="dataTableChildHeaderTitleTH">Total Effort</th>'+
			'<th class="dataTableChildHeaderTitleTH">Active</th>'+
			'<th class="dataTableChildHeaderTitleTH">Planned Task Size</th>'+			
		'</tr>'+
	'</thead>'+
	'</table>';		
	
	return childDivString;
}

function activityTaskResults_Container(data, value, row, tr){	
	activityManagementOptionsItemCounter=0;
	activityManagementTabResultArr=[];
	
	var workflowEntityId = 0;	
	if(data.length == 0 || typeof workflowEntityId == 'undefined' || workflowEntityId == null){
		workflowEntityId = 0;
	}else{
		workflowEntityId = data[0].activityTaskTypeId;
	}

	var actID = '';
	var actWPID	= '';
	
	if(row == ''){
		actID = activitiesJsonObj.activityId;
		actWPID = activitiesJsonObj.activityWorkPackageId;
		activityManagementTab_productId = activitiesJsonObj.productId;
	}else{
		actID = row.data().activityId;
		actWPID = row.data().activityWorkPackageId;
	}
	
	var entityTypeId = 34;
	var entityId = 0;
	var entityInstanceId = actWPID;
	if(typeof entityInstanceId == 'undefined'){
		entityInstanceId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
	};	
	
	activityManagementOptionsArr = [{id:"activityTaskTypeId", type:optionsType_ActivityTask, url:'common.list.activity.tasktype?productId='+activityManagementTab_productId},
				   {id:"lifeCycleStageId", type:optionsType_ActivityTask, url:'workflow.life.cycle.stages.options?entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId},
	               {id:"assigneeId", type:optionsType_ActivityTask, url:'common.user.list.by.resourcepool.id.productId?productId='+activityManagementTab_productId},
				   {id:"reviewerId", type:optionsType_ActivityTask, url:'common.user.list.by.resourcepool.id.productId?productId='+activityManagementTab_productId},
				   {id:"priorityId", type:optionsType_ActivityTask, url:'administration.executionPriorityList'},
				   {id:"complexity", type:optionsType_ActivityTask, url:'common.list.complexity'},
				   {id:"categoryId", type:optionsType_ActivityTask, url:'common.list.executiontypemaster.byentityid?entitymasterid=1'},
				   {id:"enviromentCombinationId", type:optionsType_ActivityTask, url:'environment.combinations.options.by.activity?activityId='+actID+'&activityWorkPkgId='+actWPID},
				   {id:"workflowId", type:optionsType_ActivityTask, url:'workflow.master.mapped.to.entity.list.options?productId='+activityManagementTab_productId+'&entityTypeId=30&entityId='+workflowEntityId},
				   
	           ];
	returnOptionsItemForActivityManagement(activityManagementOptionsArr[0].url, value, data, row, tr); 	
}

var acitivityTask_oTable='';
var acitivityTask_editor='';

function createEditorForActivityTask(data, parent, row){
	
	var actID='';
	var stDt='';
	var endDT='';
	
	if(row == ''){
		actID = activitiesJsonObj.activityId;
		if(defaultTaskPlannedStartDate != "" || defaultTaskPlannedStartDate != undefined){
			stDt = defaultTaskPlannedStartDate;
			endDT = defaultTaskPlannedEndDate;
		}else{		
			stDt = function () { return new Date(); };
			endDT =  function () { return new Date(); };
		}
	}else{
		actID = row.data().activityId;
		stDt = row.data().plannedStartDate;
		endDT = row.data().plannedEndDate;		
	}
	
	acitivityTask_editor = new $.fn.dataTable.Editor( {
			    "table": "#activityTask_dataTable_"+parent,
				ajax: "process.activitytask.add",
				ajaxUrl: "process.activitytask.update",
				"idSrc":  "activityTaskId",
				i18n: {
	    	        create: {
	    	            title:  'Add/Edit Activity Task',
	    	            submit: "Create",
	    	        }
	    	    },
				fields: [ {
					label: 'activityId',
		            name: "activityId",
					"type":"hidden",
					def: actID,	
		        },{
					label: 'Activity Task <font color="#efd125" size="4px">*</font>',
		            name: "activityTaskName",		            
		        },{
					label: 'Task Type <font color="#efd125" size="4px">*</font>',
		            name: "activityTaskTypeId",
					options: activityManagementTabResultArr[0],
		            "type"  : "select",	
		        },{
		            label: "Life Cycle Stage",
		            name: "lifeCycleStageId",
					options: activityManagementTabResultArr[1],
		            "type"  : "select",	
		        },		        
		        {
		            label: "Planned Value",
		            name: "planExpectedValue",
					def: 1,		            
		        },{
		            label: "Planned Start Date",
		            name: "plannedStartDate",		            
					type:  'datetime',
					def:    stDt,
					format: 'M/D/YYYY',
		        },{
		            label: "Planned End Date",
		            name: "plannedEndDate",		            
					type:  'datetime',
					def:    endDT,
					format: 'M/D/YYYY',
		        },
				{
		            label: "Actual Start Date",
		            name: "actualStartDate",		            					
					type:  'datetime',
					def:    stDt,
					format: 'M/D/YYYY',	
		        },{
		            label: "Actual End Date",
		            name: "actualEndDate",		            					
					type:  'datetime',
					def:    endDT,
					format: 'M/D/YYYY',
		        },
				{
		            label: "Assignee",
		            name: "assigneeId",
					options: activityManagementTabResultArr[2],
		            "type"  : "select",	
		        },{
		            label: "Reviewer",
		            name: "reviewerId",
					options: activityManagementTabResultArr[3],
		            "type"  : "select",	
		        },{
		            label: "Priority",
		            name: "priorityId",
					options: activityManagementTabResultArr[4],
		            "type"  : "select",	
		        },{
		            label: "Complexity",
		            name: "complexity",
					options: activityManagementTabResultArr[5],
		            "type"  : "select",	
		        },{
		            label: "Category",
		            name: "categoryId",
					options: activityManagementTabResultArr[6],
		            "type"  : "select",	
		        },{
		            label: "Environment Combination",
		            name: "enviromentCombinationId",
					options: activityManagementTabResultArr[7],
		            "type"  : "select",	
		        },{
					label: "Remarks",
					name: "remark",					
				},{
					label: "Planned Task Size",
					name: "plannedTaskSize",										
				},{
					label: "Actual Task Size",
					name: "actualTaskSize",					
				},{
					label: "Workflow Template",
					name: "workflowId",					 	
					options: activityManagementTabResultArr[8],
		            "type"  : "select",
				},{
					label: 'resultId',
		            name: "resultId",					
		            "type"  : "hidden",	
		        },{
					label: 'percentageCompletion',
		            name: "percentageCompletion",
		        },{
					label: 'plannedEffort',
		            name: "plannedEffort",
		        },{
					label: 'totalEffort',
		            name: "totalEffort",
					"type":"hidden", 
		        },{
					label: "workflowRAG",
					name: "workflowRAG",
					"type": "hidden",					
				},{
					label: "statusDisplayName",
					name: "statusDisplayName",
					"type": "hidden",					
				},{
					label: "actors",
					name: "actors",
					"type": "hidden",					
				},{
					label: "completedBy",
					name: "completedBy",
					"type": "hidden",					
				},{
					label: "remainingHrsMins",
					name: "remainingHrsMins",
					"type": "hidden",					
				},{
					label: "isActive",
					name: "isActive",
					"type": "hidden",					
				}	        
		    ]
			});
			
		// ----- focus area -----	
			
		$( 'input', acitivityTask_editor.node()).on( 'focus', function () {
			this.select();
		});
}

function activityActivityTaskResults_Container(data, parent, row, tr){
	try{
		if ($("#activityTabContainer_"+parent).children().length>0) {
			$("#activityTabContainer_"+parent).children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = activityTabChild3Table(parent);	
	$("#activityTabContainer_"+parent).append(childDivString);
		
	// --- editor for the activity Change Request started -----
	createEditorForActivityTask(data, parent, row);
		
	acitivityTask_oTable = $("#activityTask_dataTable_"+parent).dataTable( {
		 	dom: "Bfrtilp",
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bSort": false,
	       "bScrollCollapse": true,
	       /*fixedColumns:   {
	           leftColumns: 1,
	       }, */
	       "fnInitComplete": function(data) {	
				reInitializeDTActivityTask();
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: acitivityTask_editor },	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Activity Task',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Activity Task',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Activity Task',
		                    	exportOptions: {
		                            columns: ':visible'
		                        },
		                        orientation: 'landscape',
		                        pageSize: 'LEGAL'
		                    }	                    
		                ],	                
		            },
		            'colvis',
					{					
						text: '<i class="fa fa-upload showHandCursor" title="Upload Activities task"></i>',
						action: function ( e, dt, node, config ) {					
							triggerActivityTaskUpload();
						}
					},
					{					
						text: '<i class="fa fa-download showHandCursor downloadActivityTemplate" title="Download Activity Template"></i>',
						action: function ( e, dt, node, config ) {					
							downloadTemplateActivityTask();
						}
					},
	         ], 	         
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "activityTaskName",className: 'editable', sWidth: '5%' },		           
		   { mData: "activityTaskTypeName", className: 'editable', sWidth: '10%', editField: "activityTaskTypeId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(acitivityTask_editor, 'activityTaskTypeId', full.activityTaskTypeId);
					 }
					 else if(type == "display"){
						data = full.activityTaskTypeName;
					 }	           	 
					 return data;
				 },
			},
           { mData: "lifeCycleStageId",className: 'editable', sWidth: '5%' },		  
		   { mData: "plannedStartDate", className: 'editable', sWidth: '15%',
				mRender: function (data, type, full) {
					 if (full.action == "create"){						
						 data = convertDTDateFormatAdd(data, ["plannedStartDate"]);						
					 }else if(type == "display"){
						data = full.plannedStartDate;
					 }	           	 
					 return data;
				 }
			},           
		   { mData: "plannedEndDate", className: 'editable', sWidth: '15%',
				mRender: function (data, type, full) {
					 if (full.action == "create"){						
						 data = convertDTDateFormatAdd(data, ["plannedEndDate"]);						
					 }else if(type == "display"){
						data = full.plannedEndDate;
					 }	           	 
					 return data;
				 }
			},
		    { mData: "workflowRAG",className: 'editable', sWidth: '5%' },		
           { mData: "statusDisplayName",className: 'disableEditInline', sWidth: '5%' },
           { mData: "actors",className: 'disableEditInline', sWidth: '5%' },
		   { mData: "completedBy",className: 'disableEditInline', sWidth: '5%' },
           { mData: "remainingHrsMins",className: 'disableEditInline', sWidth: '5%' },		    	
			{ mData: "assigneeName", className: 'editable', sWidth: '10%', editField: "assigneeId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(acitivityTask_editor, 'assigneeId', full.assigneeId);
					 }
					 else if(type == "display"){
						data = full.assigneeName;
					 }	           	 
					 return data;
				 },
			},          
		   { mData: "reviewerName", className: 'editable', sWidth: '10%', editField: "reviewerId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(acitivityTask_editor, 'reviewerId', full.reviewerId);
					 }
					 else if(type == "display"){
						data = full.reviewerName;
					 }	           	 
					 return data;
				 },
			},           
		   { mData: "priorityName", className: 'editable', sWidth: '10%', editField: "priorityId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(acitivityTask_editor, 'priorityId', full.priorityId);
					 }
					 else if(type == "display"){
						data = full.priorityName;
					 }	           	 
					 return data;
				 },
			},		   
		   { mData: "complexity", className: 'editable', sWidth: '10%',
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(acitivityTask_editor, 'complexity', data);
					 }
					 else if(type == "display"){
						data = full.complexity;
					 }	           	 
					 return data;
				 },
			},           	
			{ mData: "categoryName", className: 'editable', sWidth: '10%', editField: "categoryId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(acitivityTask_editor, 'categoryId', full.categoryId);
					 }
					 else if(type == "display"){
						data = full.categoryName;
					 }	           	 
					 return data;
				 },
			},		   
		   { mData: "enviromentCombinationId", className: 'editable', sWidth: '10%',
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(acitivityTask_editor, 'enviromentCombinationId', data);
					 }
					 else if(type == "display"){
						data = "No Data";
						//data = full.enviromentCombinationName;
					 }	           	 
					 return data;
				 },
			},			           
		   { mData: "resultName", className: 'editable', sWidth: '10%', editField: "resultId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(acitivityTask_editor, 'resultId', full.resultId);
					 }
					 else if(type == "display"){
						data = full.resultName;
					 }	           	 
					 return data;
				 },
			},           
		   { mData: "actualStartDate", className: 'editable', sWidth: '15%',
				mRender: function (data, type, full) {
					 if (full.action == "create"){						
						 data = convertDTDateFormatAdd(data, ["actualStartDate"]);						
					 }else if(type == "display"){
						data = full.actualStartDate;
					 }	           	 
					 return data;
				 }
			},		   
		   { mData: "actualEndDate", className: 'editable', sWidth: '15%',
				mRender: function (data, type, full) {
					 if (full.action == "create"){						
						 data = convertDTDateFormatAdd(data, ["actualEndDate"]);						
					 }else if(type == "display"){
						data = full.actualEndDate;
					 }	           	 
					 return data;
				 }
			},
           { mData: "plannedTaskSize",className: 'editable', sWidth: '5%' },
		    { mData: "actualTaskSize",className: 'editable', sWidth: '5%' },		
           { mData: "percentageCompletion",className: 'editable', sWidth: '5%' },
           { mData: "plannedEffort",className: 'editable', sWidth: '5%' },
		   { mData: "totalEffort",className: 'disableEditInline', sWidth: '5%' },          
		   { mData: "isActive",
              mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editor-active-activityTask">';
                    }
                    return data;
				},
                className: "dt-body-center"
            },
			{ mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
					'<button style="border: none; background-color: transparent; outline: none;">'+
						/*'<i class="fa fa-copy showHandCursor activityTaskImg1" title="Clone Task"></i></button>'+*/
						'<img src="css/images/cloning.jpg" style="width: 24px;height: 24px;" class="activityTaskImg1" title="Cloning Task" data-toggle="modal" /></button>'+
     	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
						'<i class="fa fa-history showHandCursor activityTaskImg2" title="Event History"></i></button>'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
						'<img src="css/images/workflow.png" class="activityTaskImg3" title="Configure Workflow" /></button>'+
     	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
						'<img src="css/images/attachment.png" class="activityTaskImg4" title="Attachment" style="width: 18px;height: 18px;">&nbsp;['+data.attachmentCount+']&nbsp;</img></button>'+
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="fa fa-search-plus activityTaskImg5" title="Audit History"></i></button>'+
					'<button style="border: none; background-color: transparent; outline: none;">'+
						'<i class="fa fa-trash-o details-control activityTaskImg6" title="Delete Activity Task" onClick="deleteActivityTaskItem('+data.activityTaskId+')" style="margin-left: 5px;"></i></button>'+	
     	       		'</div>');	      		
           		 return img;
            	}
            },
       ],
        rowCallback: function ( row, data ) {
            $('input.editor-active-activityTask', row).prop( 'checked', data.isActive == 1 );
        },       
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
	
		$("#activityTabLoaderIcon").hide();
		 
		 $('#activityTask_dataTable_'+parent+'_length').css('margin-top','8px');
		 $('#activityTask_dataTable_'+parent+'_length').css('padding-left','35px');
		 
		 //disableCreateButton("activityTask_dataTable_"+parent);
		 
		 // Activate an inline edit on click of a table cell
        $('#activityTask_dataTable_'+parent).on( 'click', 'tbody td.editable', function (e) {
        	acitivityTask_editor.inline( this, {
        		submitOnBlur: true
			}); 
		});
		
		$('#activityTask_dataTable_'+parent).on( 'change', 'input.editor-active-activityTask', function () {
			 acitivityTask_editor
		            .edit( $(this).closest('tr'), false )
		            .set( 'isActive', $(this).prop( 'checked' ) ? 1 : 0 )
		            .submit();			
	    });
		
		// ----- dependent Values -----
        
        acitivityTask_editor.dependent('activityTaskTypeId',function ( val, data, callback ) {        	
        	var entityId = 0;					       								
			if(val != undefined){			
				entityId = val;			
				var url = 'workflow.master.mapped.to.entity.list.options?productId='+activityManagementTab_productId+'&entityTypeId=30&entityId='+entityId;
				$.ajax( {
					url: url,
					type: "POST",
					dataType: 'json',
					success: function ( json ) {			    	        	
						
						for(var i=0;i<json.Options.length;i++){
							json.Options[i].label=json.Options[i].DisplayText;
							json.Options[i].value=json.Options[i].Value;
						}
						
						json.url = url;
						acitivityTask_editor.set('workflowId',json.Options);
						acitivityTask_editor.field('workflowId').update(json.Options);
					}
				} );
			}
        });
		
		// ----- Cloning -----
		 
		 $('#activityTask_dataTable_'+parent+' tbody').on('click', 'td button .activityTaskImg1', function () {
			 var tr = $(this).closest('tr');
			 var row = acitivityTask_oTable.DataTable().row(tr);

			var jsonActivityTaskCloningObj = {
				"title": "Cloning Activity Task",
				"packageName":"Activity Task Name",
				"startDate" : dateFormat(row.data().plannedStartDate),
				"endDate" : dateFormat(row.data().plannedEndDate),
				"selectionTerm" : "Select Activity",
				"sourceParentID": row.data().activityId,
				"sourceParentName": row.data().activityName,
				"sourceID": row.data().activityTaskId,
				"sourceName": row.data().activityTaskName,
				"componentUsageTitle":"activityTaskClone",
			};
						
			openLoaderIcon();
			// -- for cloning workpackage ---
			$.ajax({
				type: "POST",
				contentType: "application/json; charset=utf-8",
				url: 'administration.product.activity.tree?actionType=3',
				dataType : 'json',
				complete : function(data){
					closeLoaderIcon();
					},
				success : function(data) {
					treeData = data;			
					Cloning.init(jsonActivityTaskCloningObj);
					},
				error: function (data){
					closeLoaderIcon();
				}
			});
		 });
		 
		 // ----- ended -----
		
		// ----- Event History -----
		 
		 $('#activityTask_dataTable_'+parent+' tbody').on('click', 'td button .activityTaskImg2', function () {
				var tr = $(this).closest('tr');
			    var row = acitivityTask_oTable.DataTable().row(tr);									

				addTaskComments(row.data());	
		 });
		 
		 // ----- ended -----
		
		// ----- workflow started -----
		 
		 $('#activityTask_dataTable_'+parent+' tbody').on('click', 'td button .activityTaskImg3', function () {
				var tr = $(this).closest('tr');
			    var row = acitivityTask_oTable.DataTable().row(tr);									

				workflowId = 0;
				entityTypeId = 30;
				statusPolicies(activityManagementTab_productId, workflowId, entityTypeId, row.data().activityTaskTypeId, row.data().activityTaskId, row.data().activityTaskName, "Task", row.data().statusId);	
		 });
		 
		 // ----- ended -----
			
		// ------ Attachments -----		 
		 $('#activityTask_dataTable_'+parent+' tbody').on('click', 'td button .activityTaskImg4', function () {
				var tr = $(this).closest('tr');
			    var row = acitivityTask_oTable.DataTable().row(tr);
			    	
				isViewAttachment = false;
				var jsonObj={"Title":"Attachments for Task",			          
					"SubTitle": 'Task : ['+row.data().activityTaskId+'] '+row.data().activityTaskName,
					"listURL": 'attachment.for.entity.or.instance.list?productId='+activityManagementTab_productId+'&entityTypeId=29&entityInstanceId='+row.data().activityTaskId,
					"creatURL": 'upload.attachment.for.entity.or.instance?productId='+activityManagementTab_productId+'&entityTypeId=29&entityInstanceId='+row.data().activityTaskId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
					"deleteURL": 'delete.attachment.for.entity.or.instance',
					"updateURL": 'update.attachment.for.entity.or.instance',
					"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=29',
					"multipleUpload":true,
				};	 
				Attachments.init(jsonObj);
			    
		 });
		 // ----- ended -----
		 
		 // ----- Audit History -----
		 
		 $('#activityTask_dataTable_'+parent+' tbody').on('click', 'td button .activityTaskImg5', function () {
				var tr = $(this).closest('tr');
			    var row = acitivityTask_oTable.DataTable().row(tr);				
				listActivityTaskAuditHistory(row.data().activityTaskId);						
		 });
		 
		 // ----- ended -----	
		
	});
	// ------	
}

// ----- Ended -----

// -- Change Request Child Table 1 -----

function activityChangeRequestResults_Container(data, row, tr){
	
	activityManagementOptionsItemCounter=0;
	activityManagementTabResultArr=[];
	activityManagementOptionsArr = [{id:"priorityId", type:optionsType_ActivityChangeRequest, url:'administration.executionPriorityList'},	               
	               {id:"ownerId", type:optionsType_ActivityChangeRequest, url:'common.user.list.by.resourcepool.id.productId?productId='+activityManagementTab_productId},
	           ];
	returnOptionsItemForActivityManagement(activityManagementOptionsArr[0].url, "", data, row, tr);
	
}

function activityTabChild1Table(){
	var childDivString = '<table id="activityChangeRequest_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th class="dataTableChildHeaderTitleTH">ID</th>'+
			'<th class="dataTableChildHeaderTitleTH">Name</th>'+			
			'<th class="dataTableChildHeaderTitleTH">Description</th>'+	
			'<th class="dataTableChildHeaderTitleTH">Priority</th>'+
			'<th class="dataTableChildHeaderTitleTH">Planned Value</th>'+
			'<th class="dataTableChildHeaderTitleTH">Owner</th>'+
			'<th class="dataTableChildHeaderTitleTH">Raised Date</th>'+
			'<th class="dataTableChildHeaderTitleTH"></th>'+	
		'</tr>'+
	'</thead>'+
	'</table>';		
	
	return childDivString;
}

function changeArrayPosition(arr, fromIndex, toIndex) {
    var element = arr[fromIndex];
    arr.splice(fromIndex, 1);
    arr.splice(toIndex, 0, element);
    return arr;
}

var activityManagementTab_productId='';
function activityManagementResults_Container(data, value, activityWorkPackageId, productId){		
	var workflowURL='';
	var lifeCycleStageIdURL='';
	
	// ------	
	var entityTypeId = 34;
	var entityId = 0;
	var entityInstanceId = activityWorkPackageId;
	activityManagementTab_productId = productId;
	if(typeof entityInstanceId == 'undefined'){
		entityInstanceId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
	}
	lifeCycleStageIdURL = 'workflow.life.cycle.stages.options?entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId;
	
	// ------		
	var entityId = data.activityMasterId;
	if(typeof entityId == 'undefined' || entityId == null){
		entityId = 0;
	}
	workflowURL = 'workflow.master.mapped.to.entity.list.options?productId='+productId+'&entityTypeId=33&entityId='+entityId;
	
	activityManagementOptionsItemCounter=0;
	activityManagementTabResultArr=[];
	
	activityManagementOptionsArr = [{id:"activityMasterId", type:optionsType_ActivityTab, url:'process.list.activity.type.engagement?engagementId='+engagementId+'&productId='+productId},	               
                   {id:"productFeatureId", type:optionsType_ActivityTab, url:'list.features.by.enagment?engagementId='+engagementId+'&productId='+productId+'&activityId=-1&activityWorkPackageId='+activityWorkPackageId},	               
                   {id:"assigneeId", type:optionsType_ActivityTab, url:'common.user.list.by.resourcepool.id.productId?&productId='+productId},
				   {id:"priorityId", type:optionsType_ActivityTab, url:'administration.executionPriorityList'},
				   {id:"complexity", type:optionsType_ActivityTab, url:'common.list.complexity'},
				   {id:"categoryId", type:optionsType_ActivityTab, url:'common.list.executiontypemaster.byentityid?entitymasterid=1'},					   	
				   {id:"workflowId", type:optionsType_ActivityTab, url:workflowURL},
				   {id:"lifeCycleStageId", type:optionsType_ActivityTab, url:lifeCycleStageIdURL},
				   /*{id:"autoAllocateReferenceId", type:optionsType_ActivityTab, url:'auto.allocation.bot.for.productId?&productId='+productId+'&botType=Task Allocation Controller'},*/
	             ];
	returnOptionsItemForActivityManagement(activityManagementOptionsArr[0].url, value, data, activityWorkPackageId, productId);
}
function autoAllocateCheck(){	
	var autoAllocateType = "";
	if(autoAllocateFromProperty.toLowerCase() == "yes"){	
		autoAllocateType = "hidden";
	}else{
		autoAllocateType = "checkbox";
	}
	
	return autoAllocateType;
}

function activityManagementTabResultsEditor(activityWorkPackageId){
	var autoAllocateType = autoAllocateCheck();
	var customFieldObj=[] ;
	var customFieldName='';
	var customFieldStr='';
    for(var i=0;i<customFieldResultObjCollection.length;i++){    	
    	customFieldStr = ((customFieldResultObjCollection[i]['fieldName']).replace(" ", ""));
    	customFieldStr  = customFieldStr.replace(/ +/g, "");
    	customFieldName = customFieldResultObjCollection[i]['id']+'-'+customFieldStr;	
    	customFieldObj.push({label: customFieldResultObjCollection[i]['fieldName'], name: customFieldName});    	
    }
    var singleCustomFieldObj = {'customFields' : customFieldResultObjCollection};
    var singleJsonString = JSON.stringify(singleCustomFieldObj.customFields);
    customFieldObj.push({label: 'customFields', name: 'customFields', 'def': singleJsonString, "type": "hidden"});
    //console.log("CustomField in Editor: "+customFieldObj.length);	
    
	activityManagementTab_editor = new $.fn.dataTable.Editor( {
			"table": "#"+jTableContainerDataTable,
    		ajax: "process.activity.add",
    		ajaxUrl: "process.activity.update",
    		idSrc:  "activityId",
    		i18n: {
    	        create: {
    	            title:  "Create a new Activity",
    	            submit: "Create",
    	        }
    	    },
    		fields: [{								
				label:"activityWorkPackageId",
				name:"activityWorkPackageId",					
				type: 'hidden',				
				def: activityWorkPackageId
			},{
                label: "activityId",
                name: "activityId",
                "type": "hidden",
            },{
                label: "Activity Name",
                name: "activityName",                
            },{
                label: "Engagement",
                name: "engagementName",
                "type": "hidden",
            },{
                label: "Product",
                name: "productName",
                "type": "hidden",
            },{
                label: "Workpackage",
                name: "activityWorkPackageName",
                "type": "hidden",
            },{
                label: "Activity Type",
                name: "activityMasterId",
                 options: activityManagementTabResultArr[0],
                "type"  : "select",
            }, {
                label: "Requirement",
                name: "productFeatureId",
                options: activityManagementTabResultArr[1],
                "type"  : "select",
            },{
                label: "Life Cycle Stage",
                name: "lifeCycleStageId",  
				options: activityManagementTabResultArr[7],
                "type"  : "select",				
            },{
                label: "Planned Start Date",
                name: "plannedStartDate",
                type:  'datetime',
				def:    function () { return new Date(); },
				format: 'M/D/YYYY',
				def : defaultActivityWorkpackagePlannedStartDate ,
            },{
                label: "Planned End Date",
                name: "plannedEndDate",
                type:  'datetime',
				def:    function () { return new Date(); },
				format: 'M/D/YYYY',
				def : defaultActivityWorkpackagePlannedEndDate,
            },{
                label: "workflowRAG",
                name: "workflowRAG",
                "type": "hidden",				
            },{
                label: "Actual Start Date",
                name: "actualStartDate",
			    type:  'datetime',
			    "type": "hidden",
				//def:    function () { return new Date(); },
				format: 'M/D/YYYY',
            },{
                label: "Actual End Date",
                name: "actualEndDate",
				type:  'datetime',
				 "type": "hidden",
			//	def:    function () { return new Date(); },
				format: 'M/D/YYYY',
            },{
                label: "isModified",
                name: "isModified",	
				"type": "hidden",
            },{
                label: "Current Status:",
                name: "statusDisplayName",	
				"type": "hidden",
            },{
                label: "Status Pending With",
                name: "actors",
				"type": "hidden",
            },{
                label: "Status Complete By",
                name: "completedBy",
				"type": "hidden",
            },{
                label: "Status Time Left",
                name: "remainingHrsMins",
				"type": "hidden",
            },{
                label: "Assignee",
                name: "assigneeId",
                type : "select",
                options: activityManagementTabResultArr[2],
            },{
                label: "Reviewer",
                name: "reviewerId",
                type : "select",
                options: activityManagementTabResultArr[2],
            },/*{
                label: "BOT",
                name: "autoAllocateReferenceId",
                type : "select",                
                options: activityManagementTabResultArr[8],
            },{
            	
                label: "Auto Allocate Now",    
                name: "isImmidiateAutoAllocation",
                type: autoAllocateType,
                //type: "checkbox",
                separator: "|",
                options:[{ label: '', value: 1 }],
              
            },*/{
                label: priorityHeaderTitle,
                name: "priorityId",
                type : "select",
                options: activityManagementTabResultArr[3],
            },{
                label: "Complexity",
                name: "complexity",
                type : "select",
                options: activityManagementTabResultArr[4],
            },{
                label: "Tracker Number",
                name: "activityTrackerNumber",                		                
            },{
                label: "Clarification",
                name: "drReferenceNumber",
				"type": "hidden",				
            },{
                label: "Category:",
                name: "categoryId",
                type : "select",
                options: activityManagementTabResultArr[5],
            },{
                label: "Remarks",
                name: "remark",
                type:  'textarea',				
            },{
                label: plannedActivitySizeHeaderTitle,
                name: "plannedActivitySize",
				def: 1,                
            },{
                label: actualActivitySizeHeaderTitle,
                name: "actualActivitySize",
				def: 1,									
            },{
                label: "% Completion",
                name: "percentageCompletion",	
            },{
                label: "workflowIndicator",
                name: "workflowIndicator", 
				"type": "hidden",		
            },{
                label: "Planned Effort",
                name: "plannedEffort", 
				def: 1, 	
            },{
                label: "Actual Effort",
                name: "totalEffort", 
				"type": "hidden",	
            },{
                label: "Active",
                name: "isActive",
				"type": "hidden",
				def: 1, 	
            },{
                label: "latestComment",
                name: "latestComment",
				"type": "hidden",
            },
            {
                label: "workflowStatusType",
                name: "workflowStatusType",
            },{
                label: "Workflow Template:",
                name: "workflowId",
                type : "select",
                options: activityManagementTabResultArr[6],
            },
            {
                label: "Predecessors",
                name: "activityPredecessors",
            },            
        ].concat(customFieldObj)
    	});	
}

function disableCreateButton(id){	
	if(jTableContainerDataTable == "assigneeActivitiesContainer" ||
			jTableContainerDataTable == "pendingWithActivitiesContainer" || 
			jTableContainerDataTable == "passingThroughContainer"){
		$("#myActivitiesTab").find(".buttons-create").hide();
	}else{
		$("#"+id+"_wrapper").find(".buttons-create").hide();
	}

	$("#"+id+"_wrapper").find(".autoAllocation").parent().parent().hide();
	$("#"+id+"_wrapper").find(".activityGanttChart").parent().parent().hide();
	$("#filterActivityTab").hide();
	
	if(jTableContainerDataTable == "activitiesWP_dataTable"){
		
		$("#customActivityCreate").parent().parent().hide();		
		$("#"+id+"_wrapper").find(".demandActivity").parent().parent().show();
		$("#"+id+"_wrapper").find(".downloadActivityTemplate").parent().parent().show();	
		$("#"+id+"_wrapper").find(".multiSelectionActivityMove").parent().parent().hide();
		
	}else if(jTableContainerDataTable == "jTableContainerWPActivities_dataTable" ||
			jTableContainerDataTable == "assigneeActivitiesContainer"){
		
		if(jTableContainerDataTable == "assigneeActivitiesContainer"){
			
			if(nodeType == "testfactory" || nodeType == "product" || nodeType == "activityworkpackageicon"){
				$("#"+id+"_wrapper #customActivityCreate").parent().parent().hide();		
				$("#"+id+"_wrapper").find(".demandActivity").parent().parent().hide();
				$("#"+id+"_wrapper").find(".downloadActivityTemplate").parent().parent().hide();
				$("#"+id+"_wrapper").find(".multiSelectionActivityMove").parent().parent().hide();
			}else{
				$("#"+id+"_wrapper #customActivityCreate").parent().parent().show();	
				$("#"+id+"_wrapper").find(".autoAllocation").parent().parent().show();
				$("#"+id+"_wrapper").find(".demandActivity").parent().parent().show();
				$("#"+id+"_wrapper").find(".downloadActivityTemplate").parent().parent().show();
				$("#"+id+"_wrapper").find(".activityGanttChart").parent().parent().show();
				$("#filterActivityTab").show();
			}
			
		}else{		
			$("#customActivityCreate").parent().parent().show();	
			$("#"+id+"_wrapper").find(".autoAllocation").parent().parent().show();
			$("#"+id+"_wrapper").find(".demandActivity").parent().parent().show();
			$("#"+id+"_wrapper").find(".downloadActivityTemplate").parent().parent().show();
			$("#"+id+"_wrapper").find(".activityGanttChart").parent().parent().show();
			$("#filterActivityTab").show();
		}
		
	}else if(jTableContainerDataTable == "productActivities_dataTable"){
		
		$("#customActivityCreate").parent().parent().hide();		
		$("#"+id+"_wrapper").find(".demandActivity").parent().parent().show();
		$("#"+id+"_wrapper").find(".downloadActivityTemplate").parent().parent().show();				
	
	}else if(jTableContainerDataTable == "pendingWithActivitiesContainer" || 
			jTableContainerDataTable == "passingThroughContainer"){		
		
		$("#"+id+"_wrapper #customActivityCreate").parent().parent().hide();		
		$("#"+id+"_wrapper").find(".demandActivity").parent().parent().hide();
		$("#"+id+"_wrapper").find(".downloadActivityTemplate").parent().parent().hide();
		$("#"+id+"_wrapper").find(".multiSelectionActivityMove").parent().parent().hide();		
	}	
}

// -- For DataTable Pagination -----

var totalActivityList=300;
var Prev = {start: 0,stop: 0}, 
TabPage, Paging = [], CONT;
var pageCountSelectedActivityDT=9;
var currentPageCountSelectedActivity=10;

function getParentIDPagination(){
	var parentName='';
	var value = jTableContainerDataTable; 
	switch(value){
		case 'activitiesWP_dataTable':
			parentName='listingActivitiesDIV';
		break;
		
		case 'productActivities_dataTable':
			parentName='prodActivities';
		break;
		
		case 'jTableContainerWPActivities_dataTable':
			parentName='wpActivities';
		break;
		
		case 'assigneeActivitiesContainer':
			parentName='assigneeActivities';
		break;
		
		case 'pendingWithActivitiesContainer':
			parentName='PendingWithActivities';
		break;
		
		case 'passingThroughContainer':
			parentName='PassingThrough';
		break;
		
	default:
		parentName='listingActivitiesDIV';
		break;		
	}
	
	return parentName;
}

function allActivityFunDT(index, pageCntSelected){
	var rowCount = pageCntSelected;			
	var totalCurrentCount=0;
	var currentPageNo=0;
	var parentID=getParentIDPagination();
	var page = Number($('#'+parentID+' #paginationContainerDT').find('span.current').text());			
	currentPageNo=page;
		
	if(page==0){
		currentPageNo=1;
	}
	var tempUrl = urlActivityDTPagination.replace("process.activity.engagement.list","process.activity.list.count");
	var url = tempUrl+"&jtStartIndex=0&jtPageSize="+currentPageCountSelectedActivity;	
	$.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {
			  totalActivityList = data.TotalRecordCount;

			  $('#'+parentID+' #paginationContainerDT').show();
			  if(totalActivityList<pageCountSelectedActivityDT){
				  $('#'+parentID+' #paginationContainerDT').hide();
			  }
			  
			  Paging[0] = $('#'+parentID+' #paginationContainerDT .toppagination').paging(totalActivityList, {								                    
				  onSelect: function(page) {					
					  currentPageNo=page;
					  if(totalActivityList < (rowCount*currentPageNo)){
						  totalCurrentCount = totalActivityList;
					  }else{
						  totalCurrentCount = (rowCount*page);
					  }
					  $('#'+parentID+' #paginationBadgeDTActivity').text('Showing '+((rowCount*page)-rowCount+1)+' to '+totalCurrentCount+' of '+totalActivityList+' entries');		
					  openLoaderIcon();
					  
					  var url = urlActivityDTPagination+"&jtStartIndex="+((rowCount*page)-rowCount)+"&jtPageSize="+rowCount;		
					  activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
					  assignDataTableValuesInActivityManagementTab(url, "100%", "parentTable", activityWorkPackageId, productId);		
					  index++;
				  },			
				  onFormat: function (type) {		
					  switch (type) {			
					  case 'block':				
						  if (!this.active)
							  return '<span class="fg-button ui-button ui-state-default ui-state-disabled disabled">' + this.value + '<\/span>';
						  else if (this.value != this.page)
							  return '<em><a class="fg-button ui-button ui-state-default customPagination" href="#' + this.value + '">' + this.value + '<\/a><\/em>';
						  return '<span class="current fg-button ui-button ui-state-default customPagination">' + this.value + '<\/span>';
						  
					  case 'right':
					  case 'left':				
						  if (!this.active) {
							  return '';
						  }
						  return '<a class="fg-button ui-button ui-state-default" href="#' + this.value + '">' + this.value + '<\/a>';
						  
					  case 'next':				
						  if (this.active) {
							  return '<a href="#' + this.value + '" class="next fg-button ui-button ui-state-default customPagination">&raquo;<\/a>';
						  }
						  return '<span class="fg-button ui-button ui-state-default ui-state-disabled disabled">&raquo;<\/span>';
						  
					  case 'prev':					
						  if (this.active) {
							  return '<a href="#' + this.value + '" class="prev fg-button ui-button ui-state-default customPagination">&laquo;<\/a>';
						  }
						  return '<span class="fg-button ui-button ui-state-default ui-state-disabled disabled">&laquo;<\/span>';
						  
					  case 'first':				
						  if (this.active) {
							  return '<a href="#' + this.value + '" class="first fg-button ui-button ui-state-default customPagination">|&lt;<\/a>';
						  }
						  return '<span class="fg-button ui-button ui-state-default ui-state-disabled disabled">|&lt;<\/span>';
						  
					  case 'last':				
						  if (this.active) {
							  return '<a href="#' + this.value + '" class="prev fg-button ui-button ui-state-default customPagination">&gt;|<\/a>';
						  }
						  return '<span class="fg-button ui-button ui-state-default ui-state-disabled disabled">&gt;|<\/span>';
						  
					  case 'fill':
						  if (this.active) {
							  return "...";
						  }
					  }
					  return ""; // return nothing for missing branches
				  },
				  format: '[< ncnnn! >]',
				  perpage: rowCount,
				  lapping: 0,
				  page: null // we await hashchange() event
			  });
			  
			  Paging[0].setPage(1); // we dropped "page" support and need to run it by hand
			  
			  adjustSideBarHeight();
		
		  },error: function(data){
			  console.log("error");
		  },complete: function(data){			  
		  }
	});
	
};

$("#"+jTableContainerDataTable).on( 'length.dt', function ( e, settings, len ) {
    adjustSideBarHeight();		
} );

$("#"+jTableContainerDataTable).on( 'page.dt', function () {
	 adjustSideBarHeight();		
} );

var clearTimeoutPaginationWKDT='';
function adjustSideBarHeight(){	
	clearTimeoutPaginationWKDT =setTimeout(function(){				
		adjustSideBarHeightDT();
	},1000);
}
	
function adjustSideBarHeightDT(){
	var pageHeight = $('.page-container').css('height');
	$('.page-quick-sidebar-wrapper').css('height', pageHeight);
}

function setDropDownActivityDT(value){
	var pageSize=0;
	var starting = 0;
	var ending = 0;
	var currentPageNo=0;
	var parentID=getParentIDPagination();
	var pageNo = Number($('#'+parentID+' #activitypaginationContainerDT').find('span.current').text());
	
	if(pageNo==0)
		pageNo=1;
	
	if ((value*pageNo) > totalActivityList){
		currentPageNo = 0;
		pageSize = (value*pageNo);
		$('#'+parentID+' #selectActivityCountDT').find('option[value="'+pageSize+'"]').attr("selected", true);		
	}else{
		currentPageNo = ((value*pageNo)-value);		
		pageSize = (value*pageNo);
	}
	var startValue=1;
	if(totalActivityList==0)
		startValue=0;
	
	$('#'+parentID+' #paginationBadgeDTActivity').text('Showing '+startValue+' to '+pageSize+' of '+totalActivityList+' entries');
	currentPageCountSelectedActivity = pageSize;
	
	var enableAddOrNot="yes";	
	if(jTableContainerDataTable == "activitiesWP_dataTable"){
		productId=0;
		productVersionId=0;
		productBuildId=0;
		activityWorkPackageId=0;
		
	}else if(jTableContainerDataTable == "jTableContainerWPActivities_dataTable"){

	}else if(jTableContainerDataTable == "productActivities_dataTable"){
		productVersionId=0;
		productBuildId=0;
		activityWorkPackageId=0;

	}else if(jTableContainerDataTable == "assigneeActivitiesContainer" || 
			jTableContainerDataTable == "pendingWithActivitiesContainer" ||
			jTableContainerDataTable == "passingThroughContainer"){
		productVersionId=0;
		productBuildId=0;		
	}
	listActivitiesOfSelectedAWP_DT(0,0,0,activityWorkPackageId,1,enableAddOrNot, jTableContainerDataTable);
}
// ----- ended -----

var editorInStancenceForActivityCell='';
//var activityStateOnLoadFlag=false;

function crTOUseCaseDTImgTitle(){
	if(changeRequestToUsecase == "YES"){		
			$(".activityListingImg5").attr('title',"use cases");
	}else{
			$(".activityListingImg5").attr('title',"Change Request");	
	}		
}
function activityManagementTabResultsContainer(data, scrollYValue){	
	   //activityStateOnLoadFlag=false;
		var tit = "";
		if(changeRequestToUsecase == "YES"){		
			tit = $(".activityListingImg5").attr('title',"use cases");
		}else{
			tit = $(".activityListingImg5").attr('title',"Change Request");	
		}
			
		if(activityInvisibleColumnValue == ''){
			activityInvisibleColumnValue=[];
		}
		
		// ----- customField ----- 
		var customFieldObj=[];
		var iconFieldObj=[];
		var customFieldName='';
		var customFieldStr='';
		
		if(data.length == 0){			
			for(var j=0;j<customFieldResultObjCollection.length;j++){
				customFieldStr = ((customFieldResultObjCollection[j]['fieldName']).replace(" ", ""));
				customFieldStr = customFieldStr.replace(/ +/g, "");
				customFieldName = customFieldResultObjCollection[j]['id']+'-'+customFieldStr;				
				customFieldObj.push({ mData: customFieldName , className: 'disableEditInline', sWidth: '5'});
			}			
		}
		
		for(var i=0;i<data.length;i++){
			
			for(var j=0;j<customFieldResultObjCollection.length;j++){
				customFieldStr = ((customFieldResultObjCollection[j]['fieldName']).replace(" ", ""));
				customFieldStr = customFieldStr.replace(/ +/g, "");
				customFieldName = customFieldResultObjCollection[j]['id']+'-'+customFieldStr;
				//console.log(data[i]['activityId']," -- ",customFieldName," - ",data[i][customFieldName]);
				
				customFieldStr = data[i][customFieldName];
				if(customFieldStr == undefined){
					customFieldStr='';
				}
				
				data[i][customFieldName] = customFieldStr;
				if(i==0){
					customFieldObj.push({ mData: customFieldName , className: 'disableEditInline', sWidth: '5'});
				}
				
			}		
			data[i]['customFields'] = "customFields";		
		}
		var activityIconObj = { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
     	       		'<button style="border: none; background-color: transparent; outline: none;">'+     	       				
     	       				'<img src="css/images/cloning.jpg" style="width: 16px;height: 16px;" class="activityListingImg1" title="Cloning Activity" data-toggle="modal" /></button>'+
     	       			'<button style="border: none; background-color: transparent; outline: none;">'+     	       				
     	       				'<i class="fa fa-scissors activityListingImg15" aria-hidden="true" title="Moving Activity" data-toggle="modal" /></i></button>'+	
 	       			'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
 	       					'<img src="css/images/workflow.png" class="activityListingImg3" title="Configure Workflow" /></button>'+
 	       			'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						//'<i class="fa fa-list-alt activityListingImg4" title="Specify ROI"></i></button>'+
 	       				  '<img src="css/images/roi.png" class="activityListingImg4" title="Specify ROI" /></button>'+
 	       			'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
 	       					'<img src="css/images/list_metro.png" class="activityListingImg5" title= changeToUseCase /></button>'+
 	       			'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
							'<img src="css/images/mapping.png" class="activityListingImg6" title= useCaseMaping data-toggle="modal" /></button>'+					
					/*'<button style="border: none; background-color: transparent; outline: none;">'+
     	       				'<img src="css/images/list_metro.png" class="activityListingImg7" title="Environment Combinations" /></button>'+*/
     	       		'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
     	       				'<img src="css/images/list_metro.png" class="activityListingImg8" title="Activity Tasks" /></button>'+
 	       			'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
 	       					'<img src="css/images/mapping.png" class="activityListingImg9" title="Environment Mapping" data-toggle="modal" /></button>'+
 	       			'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
 	       					'<img src="css/images/attachment.png" class="activityListingImg10" title="Attachment" style="width: 18px;height: 18px;">&nbsp;['+data.attachmentCount+']&nbsp;</img></button>'+
   					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						'<i class="fa fa-search-plus activityListingImg11" title="Audit History"></i></button>'+
   					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						'<i class="fa fa-comments activityListingImg12" title="Comments"></i></button>'+
   					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						'<i class="fa fa-hand-o-right activityListingImg16" title="Poke"></i></button>'+
   					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+   						
   						'<img src="css/images/allocation-bot.png" style="width:15px; height:15px; cursor:pointer;" class="activityListingImg13" title="Auto Allocate" /></button>'+
   					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
	       					'<i class="fa fa-link activityListingImg17" title="Activity Relation">&nbsp;['+data.attachmentCount+']&nbsp;</i></button>'+	
   					'<button style="border: none; background-color: transparent; outline: none;">'+
       							'<i class="fa fa-trash-o details-control activityListingImg14" onClick="deleteActivityListingItem('+data.activityId+')" title="Delete Activity" style="padding-left: 0px;"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            };	
			iconFieldObj.push(activityIconObj);
			iconFieldObj.push({ mData: 'customFields', className: 'disableEditInline', sWidth: '5'});
		
		//console.log("customField mData: "+customFieldObj.length);	
	   activityManagementTab_oTable = $("#"+jTableContainerDataTable).dataTable( {	 
		dom: "Bfrtilp",
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "90%",
        "sScrollXInner": "100%",
        "scrollY":"100%",
        "bSort": true,
        "bScrollCollapse": true,
        "fnInitComplete": function(data) { 
        	
        	if(changeRequestToUsecase == "YES"){		
        		$(".activityListingImg5").attr('title',"Use Cases");
        		$(".activityListingImg6").attr('title',"Use Cases Mapping");
        		
        	}else{
        		$(".activityListingImg5").attr('title',"Change Request");	
        		$(".activityListingImg6").attr('title',"Change Requesting Mapping");
        	}
        	 var searchcolumnVisibleIndex = [5,11,12]; // search column TextBox Invisible Column position
     		  $("#"+jTableContainerDataTable+"_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
    	    	    var i=$(this).index();
    	    	    
    	    	    var flag=false;
    	    	    for(var j=0;j<searchcolumnVisibleIndex.length;j++){
    	    	    	if(i == searchcolumnVisibleIndex[j]){
    	    	    		flag=true;
    	    	    		break;
    	    	    	}
    	    	    }
    	    	    
    	    	    if(!flag){
    	    	    	$(this).html('');
    	    	    	$(this).append( '<input type="text" name="'+data.aoColumns[i].mData+'" value="" style="width:100%" class="search_init" />');
    	    	    }
	       	   });       	  
			 reInitializeDTActivityTab();
			 //activityStateOnLoadFlag=true;			 
 	   }, 
		buttons: [
		         { extend: "create", editor: activityManagementTab_editor },
		         {					
					text: '<span id="customActivityCreate" title="Create Activity">New</span>',
					action: function ( e, dt, node, config ) {							
						createActivityHandlder(activityManagementTabDTJsonData, activityManagementTabResultArr);
					}
				 },
		         {
		          extend: "collection",	 
		          text: 'Export',
	              buttons: [
		          {
                    	extend: 'excel',
                    	title: 'Activities',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'csv',
                    	title: 'Activities',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'pdf',
                    	title: 'Activities',
                    	exportOptions: {
                            columns: ':visible',	                            
                        },
                        orientation: 'landscape',
                        pageSize: 'LEGAL'
                    
                    }	                    
                   ],
		         },
		         'colvis',
		         {					
					text: '<i class="fa fa-list-alt" title="Custom Fields"></i>',
					action: function ( e, dt, node, config ) {					
						activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
						getAllInstanceCustomFieldsOfEntity(28, 0, activityWorkPackageId, engagementId, activityManagementTab_productId, 'Activities');				
					}
				}, {					
					text: '<i class="fa fa-comments" title="Comments"></i>',
					action: function ( e, dt, node, config ) {					
						activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
						var prId=document.getElementById("treeHdnCurrentProductId").value;
						getCommentsOfEntity(engagementId,prId,activityWorkPackageId,28);				
					}
				},/*{					
					text: '<img src="css/images/allocation-bot.png" class="autoAllocation" style="width:15px; height:15px; cursor:pointer;" title="Auto Allocate" />',
					action: function ( e, dt, node, config ) {					
						activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
						//getActivityInstanceAndAutoAllocationOfResource(28, 0, 34, activityWorkPackageId);
					}
				},*/
				{					
					text: '<i class="fa fa-upload showHandCursor demandActivity" title="Upload Activities"></i>',
					action: function ( e, dt, node, config ) {					
						triggerActivityUpload();
					}
				},
				{					
					text: '<i class="fa fa-download showHandCursor downloadActivityTemplate" title="Download Activity Template"></i>',
					action: function ( e, dt, node, config ) {					
						downloadTemplateActivity();
					}
				},				
				{					
					text: '<i class="fa fa-exchange showHandCursor multiSelectionActivityMove" title="Migrate Activities"></i>',
					action: function ( e, dt, node, config ) {					
						getMultiSelectActivityMove();
					}
				},				
				{					
					text: '<i class="fa fa-line-chart showHandCursor activityGanttChart" title="Gantt Chart"></i>',
					action: function ( e, dt, node, config ) {					
						activityGanttChartHanlder();
					}
				},
				/*{					
					text: '<img src="files/lib/chart/images/chart_gantt.png" class="activityGanttChart" style="width:15px; height:15px; cursor:pointer;" title="Workpackage Gantt View" />',
					action: function ( e, dt, node, config ) {					
						activityGanttChartHanlder();
					}
				},*/
               ],               
               // ----- modified -----
		       	columnDefs: [ {targets: activityInvisibleColumnValue ,visible: false}],
		       // ----- ended -----
        aaData:data,		    				 
	    aoColumns: [	        
            { mData: "activityId", sWidth: '5%', "render": function (tcData,type,full) {	        
			 		var entityInstanceId = full.activityId;					
			 		return ('<a onclick="displayTabActivitySummaryHandler('+entityInstanceId+')">'+entityInstanceId+'</a>');		        
		    	},
		    },            
            { mData: "activityName", className: 'editable', sWidth: '20'},
            { mData: "engagementName", className: 'disableEditInline', sWidth: '5%'},
            { mData: "productName", className: 'disableEditInline', sWidth: '5%'},
            { mData: "activityWorkPackageName", className: 'disableEditInline', sWidth: '5%'},
			{ mData: "activityMasterName", className: 'editable', sWidth: '10%', editField: "activityMasterId",
				mRender: function (data, type, full) {					
					 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(activityManagementTab_editor, 'activityMasterId', full.activityMasterId);
		           	 }else if(type == "display"){
						data = full.activityMasterName;
					 }	           	 
					 return data;
				 },
			},			
			{ mData: "productFeatureName", className: 'editable', sWidth: '10%', editField: "productFeatureId",
				mRender: function (data, type, full) {
					if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(activityManagementTab_editor, 'productFeatureId', full.productFeatureId);
		           	 }else if(type == "display"){
						data = full.productFeatureName;
					 }	           	 
					 return data;
				 },
			},	
			{ mData: "lifeCycleStageId", className: 'editable', sWidth: '10%',
				mRender: function (data, type, full) {
					if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(activityManagementTab_editor, 'lifeCycleStageId', data);
		           	}else if(type == "display"){
						if(full.lifeCycleStageName == null)
							full.lifeCycleStageName='--';
						
						data = full.lifeCycleStageName;
					 }	           	 
					 return data;
				 },
			},           
			{ mData: "plannedStartDate", className: 'editable', sWidth: '10%',
				mRender: function (data, type, full) {
					 if (full.action == "create"){						
						 data = convertDTDateFormatAdd(data, ["plannedStartDate"]);						
					 }else if(type == "display"){
						data = full.plannedStartDate;
					 }	           	 
					 return data;
				 }
			},	           
			{ mData: "plannedEndDate", className: 'editable', sWidth: '10%',
				mRender: function (data, type, full) {
					 if (full.action == "create"){
						 data = convertDTDateFormatAdd(full.plannedEndDate);
					 }else if(type == "display"){
						data = full.plannedEndDate;						
					 }	           	 
					 return data;
				 }
			},			
            { mData: "workflowRAG", className: 'disableEditInline', sWidth: '5%'},			            	
			{ mData: "actualStartDate", className: 'disableEditInline', sWidth: '15%',
				mRender: function (data, type, full) {
					 if (full.action == "create"){						
						data = convertDTDateFormatAdd(full.actualStartDate);
					 }else if(type == "display"){
						data = full.actualStartDate;						
					 }	           	 
					 return data;
				 }
			},           
			{ mData: "actualEndDate", className: 'disableEditInline', sWidth: '15%',
				mRender: function (data, type, full) {
					/* if (full.action == "create"){						
						 data = convertDTDateFormatAdd(full.actualEndDate);	
					 }*/ if(type == "display"){
						data = full.actualEndDate;						
					 }	           	 
					 return data;
				 }
			},
			{ mData: "isModified", className: 'disableEditInline', sWidth: '5%'},
			 { mData: "statusDisplayName", sWidth: '7%', "render": function (tcData,type,full) {	        
				 	var entityInstanceId = full.activityId;
					var entityInstanceName = full.activityName;	
					var modifiedById = full.assigneeId;
					var currentStatusId = full.statusId;
					var currentStatusDisplayName = full.statusDisplayName;
					var entityId = full.activityMasterId;	
					var secondaryStatusId = full.secondaryStatusId;
					var visibleEventComment=full.visibleEventComment;
					var prodcutId=full.productId;					
					return ('<a onclick="addActivityComments('+prodcutId+','+entityInstanceId+',\''+entityInstanceName+'\','+modifiedById+','+currentStatusId+',\''+currentStatusDisplayName+'\','+entityId+','+secondaryStatusId+','+visibleEventComment+')">'+currentStatusDisplayName+'</a>');		        
			      },
            },
            { mData: "actors", className: 'disableEditInline', sWidth: '5%'},
            { mData: "completedBy", className: 'disableEditInline', sWidth: '5%'},			
            { mData: "remainingHrsMins", className: 'disableEditInline', sWidth: '5%'},            
			{ mData: "assigneeName", className: 'editable', sWidth: '10%', editField: "assigneeId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(activityManagementTab_editor, 'assigneeId', full.assigneeId);
		           	}else if(type == "display"){
						data = full.assigneeName;
					 }	           	 
					 return data;
				 },
			},	            
			{ mData: "reviewerName", className: 'editable', sWidth: '10%', editField: "reviewerId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(activityManagementTab_editor, 'reviewerId', full.reviewerId);
		           	 }
					 else if(type == "display"){
						data = full.reviewerName;
					 }	           	 
					 return data;
				 },
			},	            			
			{ mData: "priorityName", className: 'editable', sWidth: '10%', editField: "priorityId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(activityManagementTab_editor, 'priorityId', full.priorityId);
		           	 }
					 else if(type == "display"){
						data = full.priorityName;
					 }	           	 
					 return data;
				 },
			},            
			{ mData: "complexity", className: 'editable', sWidth: '10%',
				mRender: function (data, type, full) {
					if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(activityManagementTab_editor, 'complexity', data);
		           	 }
					 else if(type == "display"){
						data = full.complexity;
					 }	           	 
					 return data;
				 },
			},            
            { mData: "activityTrackerNumber", className: 'editable', sWidth: '5%'},			    				                       			            
			{ mData: "categoryName", className: 'editable', sWidth: '10%', editField: "categoryId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(activityManagementTab_editor, 'categoryId', full.categoryId);
		           	 }
					 else if(type == "display"){
						data = full.categoryName;
					 }	           	 
					 return data;
				 },
			},	
            { mData: "remark", className: 'editable', sWidth: '5%'},			
            { mData: "plannedActivitySize", className: 'editable', sWidth: '5%'},
            { mData: "actualActivitySize", className: 'editable', sWidth: '5%'},			    				            
            { mData: "percentageCompletion", className: 'editable', sWidth: '5%'},
            { mData: "plannedEffort",className: 'editable', sWidth: '5%' },
 		   { mData: "totalEffort",className: 'disableEditInline', sWidth: '5%' },
            { mData: "workflowStatusType", className: 'disableEditInline', sWidth: '5%'},
            { mData: "latestComment", className: 'disableEditInline', sWidth: '15%'},
            { mData: "isActive",
              mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editor-active-activity">';
                    }
                    return data;
                },
                className: "dt-body-center"
            },
            { mData: "activityPredecessors",className: 'editable', sWidth: '15%' },
                        				            
        ].concat(customFieldObj).concat(iconFieldObj),
        rowCallback: function ( row, data ) {
            $('input.editor-active-activity', row).prop( 'checked', data.isActive == 1 );
	      	
	      	if(data['userTagActivity']<0) {
	      		$(row).find('td:eq(0)').css('backgroundColor', 'orange');
	      	} 
        },
        "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },
	});	    		
	
	// ------
	 $(function(){ // this will be called when the DOM is ready
		 activityManagementTab_editor.field('workflowStatusType').hide();
		 activityManagementTab_editor.field('customFields').hide();
		  		 
		 $("#"+jTableContainerDataTable+"_length").css('margin-top','8px');
		 $("#"+jTableContainerDataTable+"_length").css('padding-left','35px');
			 
		 new $.fn.dataTable.FixedColumns( activityManagementTab_oTable, {
		    leftColumns: 2,
			//rightColumns: 1,
		});
		 	 
		 activityManagementTab_editor.on( 'preSubmit', function ( e, data, action ) {
			  var getData='';
		      if(action === "create"){		    	  
		    	  var temp = JSON.parse(activityManagementTab_editor.field('customFields').val());
		    	  for(var i=0;i<temp.length;i++){
		    		  for(var keyValue in data){
		    			  getData = keyValue.split('-'); 
			    		  if(getData.length>1 && temp[i]['customFieldId'] == getData[0]){
			    			  temp[i]['fieldValue'] = activityManagementTab_editor.field(keyValue).val();		   
			    			  break;
			    		  } 
		    		  }
		    	  }
		    	  var singleJsonResult = JSON.stringify(temp);
		    	  data['customFields'] = singleJsonResult;
		      }
		    } );
		 
		 activityManagementTab_editor.on( 'submit', function ( e, data, action ) {
			 reInitializeDTActivityTab();
		 });
		 
		/* $("#"+jTableContainerDataTable+" tbody").on( 'click', 'td', function (e) {
			 editorInStancenceForActivityCell = activityManagementTab_oTable.DataTable().cell(this);
	     } );*/
						 
		 var importActivityUpload = '<span id="fileNameActivities"></span><input id="uploadFileActivities" type="file" name="uploadFileActivities" style="display:none;" onclick="{this.value = null;};" onchange="importActivityWorkpackage()">';
		 $('#'+jTableContainerDataTable+'_filter').append(importActivityUpload);
		 
		 activityManagementTab_oTable.DataTable().columns().every( function () {
		        var that = this;
		        $('input', this.footer() ).on( 'keyup change', function () {
		            if ( that.search() !== this.value ) {
		                that
		                    .search( this.value, true, false )
		                    .draw();
		            }
		        });
		    });
		 
		 /* Use the elements to store their own index */
		   $('#'+jTableContainerDataTable+'_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input').each( function (i) {
				this.visibleIndex = i;
			} );
			
		   $('#'+jTableContainerDataTable+'_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input').keyup( function () {
				/* If there is no visible index then we are in the cloned node */
				var visIndex = typeof this.visibleIndex == 'undefined' ? 0 : this.visibleIndex;
				
				/* Filter on the column (the index) of this element */
		   		activityManagementTab_oTable.fnFilter( this.value, visIndex );
			});
		 
		 disableCreateButton(jTableContainerDataTable);	 
		 	 	
		 // Activate an inline edit on click of a table cell
		  $("#"+jTableContainerDataTable).on( 'click', 'tbody td.editable', function (e) {
        	activityManagementTab_editor.inline( this, {
                submitOnBlur: true
            } );
        } );
		  
		 adjustSideBarHeight();
	  
        // ----- dependent Values -----
        
        activityManagementTab_editor.dependent('activityMasterId',function ( val, data, callback ) {
        	var entityTypeId = 33;
			if(val != undefined){
        	var entityId = val;
				if(typeof entityId == 'undefined'){
					entityId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
				}							
				var url = 'workflow.master.mapped.to.entity.list.options?productId='+activityManagementTab_productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId;
				$.ajax( {
					url: url,
					type: "POST",
					dataType: 'json',
					success: function ( json ) {			    	        	
						
						for(var i=0;i<json.Options.length;i++){
							json.Options[i].label=json.Options[i].DisplayText;
							json.Options[i].value=json.Options[i].Value;
						}
						
						json.url = url;
						
						if(activityManagementTab_editor.s.action == "create"){
							activityManagementTab_editor.set('workflowId',json.Options);
							activityManagementTab_editor.field('workflowId').update(json.Options);
						}
					}
				} );
			}
			var customFieldName='';				
			var customFieldStr='';
			for(var i=0;i<customFieldResultObjCollection.length;i++){
				customFieldStr = ((customFieldResultObjCollection[i]['fieldName']).replace(" ", ""));
				customFieldStr = customFieldStr.replace(/ +/g, "");
				customFieldName = customFieldResultObjCollection[i]['id']+'-'+customFieldStr;
				
				if(customFieldResultObjCollection[i]['activityMasterId'] == entityId){					
					activityManagementTab_editor.field(customFieldName).show();
				}else{
					activityManagementTab_editor.field(customFieldName).hide();
				}
			}		
        });
	        
	        // ----- ended -----
		 
		 // ----- activityListingImg1 -----
		 $("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg1', function () {
	    	var tr = $(this).closest('tr');
	    	var row = activityManagementTab_oTable.DataTable().row(tr);
	    	
	    	openLoaderIcon();
			// -- for cloning workpackage ---
			$.ajax({
				type: "POST",
				contentType: "application/json; charset=utf-8",
				//url: 'administration.product.activity.workpackage.tree',
				url: 'administration.product.activity.workpackage.tree.by.productId?productId='+row.data().productId,
				dataType : 'json',
				complete : function(data){
					closeLoaderIcon();
					},
				success : function(data) {
					closeLoaderIcon();
					
					treeData = data;
					$('#plannedDateDiv').show();
					var jsonActivityCloningObj = {
						"title": "Cloning Activity",
						"packageName":"Activity Name",
						"startDate" : dateFormat(row.data().plannedStartDate),
						"endDate" : dateFormat(row.data().plannedEndDate),
						"selectionTerm" : "Select Activity Workpackage",
						"sourceParentID": row.data().activityWorkPackageId,
						"sourceParentName": row.data().activityWorkPackageName,
						"sourceID": row.data().activityId,
						"sourceName": row.data().activityName,
						"componentUsageTitle":"activityClone",
					};
					Cloning.init(jsonActivityCloningObj);
	
				},
				error: function (data){
					closeLoaderIcon();
				}
			});	
	    	
		 });
		 
		 
		 $("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg15', function () {
		    	var tr = $(this).closest('tr');
		    	var row = activityManagementTab_oTable.DataTable().row(tr);
		    	
		    	openLoaderIcon();
				// -- for Moving workpackage ---
				$.ajax({
					type: "POST",
					contentType: "application/json; charset=utf-8",
					url: 'administration.product.activity.workpackage.tree.by.productId?productId='+row.data().productId,
					dataType : 'json',
					complete : function(data){
						closeLoaderIcon();
						},
					success : function(data) {
						closeLoaderIcon();
						
						treeData = data;
						var jsonActivityCloningObj = {
							"title": "Moving Activity",
							"packageName":"Activity Name",
							"startDate" : dateFormat(row.data().plannedStartDate),
							"endDate" : dateFormat(row.data().plannedEndDate),
							"selectionTerm" : "Select Activity Workpackage",
							"sourceParentID": row.data().activityWorkPackageId,
							"sourceParentName": row.data().activityWorkPackageName,
							"sourceID": row.data().activityId,
							"sourceName": row.data().activityName,
							"componentUsageTitle":"activityMove",
						};
						$('#plannedDateDiv').hide();
						Cloning.init(jsonActivityCloningObj);
		
					},
					error: function (data){
						closeLoaderIcon();
					}
				});	
		    	
			 });	
		 
		 // ----- ended -----
		 
		 // ----- WorkFlow -----
		 
		 $("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg3', function () {
	    	var tr = $(this).closest('tr');
	    	var row = activityManagementTab_oTable.DataTable().row(tr);
	    	
	    	workflowId = 0;
			entityTypeId = 33;
			if(row.data().productId != null){
				productId=row.data().productId;
			} 
			statusPolicies(productId, workflowId, entityTypeId, row.data().activityMasterId, row.data().activityId, row.data().activityName, "Activity", row.data().statusId);	    	
		 });	
		 // ----- ended -----
		 
		// ----- Custom Field -----
		 
		  $("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg4', function () {
	    	var tr = $(this).closest('tr');
	    	var row = activityManagementTab_oTable.DataTable().row(tr);			
			
	    	var jsonObj={"Title":"Custom Fields For Activity : ["+row.data().activityId+"] "+row.data().activityName,
					"subTitle": "",
					"url": "data/data.json",
					"columnGrouping":2,
					"containerSize": "large",
					"componentUsageTitle":"customFields",
					"entityId":"28",
					"entityTypeId":row.data().activityMasterId,
					"entityInstanceId":row.data().activityId,
					"parentEntityId":"18",
					"parentEntityInstanceId":row.data().productId,
					"engagementId":row.data().engagementId,
					"productId":row.data().productId,
					"singleFrequency":"customFieldSingleFrequencyContainer",
					"multiFrequency":"customFieldMultiFrequencyContainer",
			};
			CustomFieldGropings.init(jsonObj);
			$("#activityTabLoaderIcon").show();				
		 });
		 
		 // ----- ended -----
		 
		 // ----- change request -----
		 
		  $("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg5', function () {
	    	var tr = $(this).closest('tr');
	    	var row = activityManagementTab_oTable.DataTable().row(tr);			
			
	    	var url = 'list.CR.by.entityTypeIds.and.instanceId?entityType1=28&entityType2=42&entityInstance1='+row.data().activityId+'&jtStartIndex=0&jtPageSize=10000';	
			assignDataTableValuesInActivityManagementTab(url, "", "childTable1", row, tr); 
			
			if(changeRequestToUsecase == "YES"){
				$("#activityChildContainer .modal-header .modal-title").text("Use Cases");
			}else{
				$("#activityChildContainer .modal-header .modal-title").text("Change Request");
			}
			$("#activityChildContainer").modal();
			
			$("#activityTabLoaderIcon").show();				
		 });
		 
		 // ----- ended -----
		 
		 // ----- changeRequestMapping -----
		 
		 $("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg6', function () {
	    	var tr = $(this).closest('tr');
	    	var row = activityManagementTab_oTable.DataTable().row(tr);
	    	
	    	var activityId = row.data().activityId;
	    	var wkPkgId = row.data().activityWorkPackageId;
	    	var pdId = row.data().productId;
	    	
			var leftUrl="unmapped.changeRequest.count.by.wpId?activityId="+activityId+"&activityWPId="+wkPkgId;							
			var rightUrl = "";							
			var leftDefaultUrl="unmapped.changerequest.list.by.wpId?productId="+pdId+"&activityWPId="+wkPkgId+"&activityId="+activityId+"&jtStartIndex=0&jtPageSize=10";							
			var rightDefaultUrl="changerequest.mapped.to.activity.list?activityId="+activityId;
			var leftDragUrl = "activity.changerequest.mapping?activityId="+activityId;
		    var rightDragtUrl = "activity.changerequest.mapping?activityId="+activityId;					
			var leftPaginationUrl = "unmapped.changerequest.list.by.wpId?productId="+pdId+"&activityWPId="+wkPkgId+"&activityId="+activityId;
			var rightPaginationUrl="";
			
			var availableChangeRequest='';
			var mapedChangeRequest = '';
			var changeRequestTitle = '';
			var changeReqMapInfo = '';
			if(changeRequestToUsecase == "YES"){
				availableChangeRequest = "Available Use Cases Mapping";
				mapedChangeRequest  = "Mapped Use Cases";
				changeRequestTitle = "Map Use Cases to Activity";
				changeReqMapInfo = "No Use Case Mapped";				
        	}else{
        		availableChangeRequest = "Available Change Request";  
        		mapedChangeRequest = "Mapped Change Request";
        		changeRequestTitle = "Map Change Request to Activity";
        		changeReqMapInfo = "No Change Request Mapped";
        	}
			
			jsonActivityChangeRequestObj={"Title":changeRequestTitle,
					"leftDragItemsHeaderName":availableChangeRequest,
					"rightDragItemsHeaderName":mapedChangeRequest,
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
					"noItems":changeReqMapInfo,
					"componentUsageTitle":"Activity-RcnToActivity"
					};
			
			DragDropListItems.init(jsonActivityChangeRequestObj);
			loadDragDropSelectOption(jsonActivityChangeRequestObj);
		 });		 
		 
		 // ----- ended -----
		 
		 // ----- Environment Combination -----
		 
		 /*$("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg7', function () {
				var tr = $(this).closest('tr');
			    var row = activityManagementTab_oTable.DataTable().row(tr);
			    
			    var url = 'list.environment.combinations.by.activity?activityId='+row.data().activityId+'&jtStartIndex=0&jtPageSize=10000';	
				assignDataTableValuesInActivityManagementTab(url, "", "childTable2", row, tr); 		
				
				$("#activityChildContainer .modal-header .modal-title").text("Environment Combination");
				$("#activityChildContainer").modal();				
		 });*/
		 
		 // ----- ended -----
		 
		 // ----- Activity Task -----
		 
		  $("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg8', function () {
				var tr = $(this).closest('tr');
				var row = activityManagementTab_oTable.DataTable().row(tr);		
				
				if(jTableContainerDataTable == "activitiesWP_dataTable"){
					productBuildId=0;
				}
				productVersionId=0;
				
				var url = 'process.activitytask.list?productId='+activityManagementTab_productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&activityWorkPackageId='+row.data().activityWorkPackageId+'&activityId='+row.data().activityId+'&isActive=1'
				+'&jtStartIndex=0&jtPageSize=10000';
				
				$("#activitySummaryLoaderIcon").show();
				$("#activityTabLoaderIcon").show();
				
				$("#activityTabContainer").html('');
				$("#activityTabContainer").append('<div id="activityTabContainer_activityChildContainer"></div>');				
				assignDataTableValuesInActivityManagementTab(url, "activityChildContainer", "childTable3", row, tr); 
								
				$("#activityChildContainer .modal-header .modal-title").text("Activity Task");
				$("#activityChildContainer").modal();			
				
		 });
		 
		 // ----- ended -----
		 
		 // ----- Environment Mapping -----
		 
		 $("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg9', function () {
			var tr = $(this).closest('tr');
		    var row = activityManagementTab_oTable.DataTable().row(tr);
			 
			var activityId = row.data().activityId;
			// ----- DragDrop started----		
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
			loadDragDropSelectOption(jsonActivityObj);
			// DragDrop ended----	
			 
		 });		 
		 
		 // ----- ended -----
		 
		 // ------ Attachments -----
		 
		 $("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg10', function () {
				var tr = $(this).closest('tr');
			    var row = activityManagementTab_oTable.DataTable().row(tr);
			    
			    isViewAttachment = false;
   				var jsonObj={"Title":"Attachments for Activity",			          
	    			"SubTitle": 'Activity : ['+row.data().activityId+'] '+row.data().activityName,
	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=28&entityInstanceId='+row.data().activityId,
	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=28&entityInstanceId='+row.data().activityId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
	    			"updateURL": 'update.attachment.for.entity.or.instance',
	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=28',
	    			"multipleUpload":true,
	    		};	 
        		Attachments.init(jsonObj);
			    
		 });
		 // ----- ended -----
		 
		 // ----- Audit History -----
		 
		 $("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg11', function () {
				var tr = $(this).closest('tr');
			    var row = activityManagementTab_oTable.DataTable().row(tr);
			    
			    listActivityAuditHistory(row.data().activityId);			    
		 });
		 
		 // ----- ended -----
		 
		 // ----- Comments  -----
		 
		 $("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg12', function () {
				var tr = $(this).closest('tr');
			    var row = activityManagementTab_oTable.DataTable().row(tr);
			    
			    var entityTypeIdComments = 28;
				var entityNameComments = "Activity";
				listComments(entityTypeIdComments, entityNameComments, row.data().activityId, row.data().activityName, "activityComments");			    
		 });
		 
		 // ----- ended -----
		 
		 // ----- Comments  -----
		 
		 $("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg16', function () {
				var tr = $(this).closest('tr');
			    var row = activityManagementTab_oTable.DataTable().row(tr);
			    getWorkpackageOwnerMailByWPId(row.data().activityId,row.data().activityName,row.data().activityWorkPackageId);
		 });
		 
		 
		 $("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg13', function () {
				var tr = $(this).closest('tr');
			    var row = activityManagementTab_oTable.DataTable().row(tr);
			    if(row.data().assigneeId == -4 || row.data().reviewerId == -4){
			    	getActivityInstanceAndAutoAllocationOfResource(28, row.data().activityId, 0, 0);
			    }else{
			    	callAlert('Assginee and Reviewer are already allocated, Auto allocation is not possible.');
			    }
		 });
		 
		 // ----- ended -----
		 
		 $("#"+jTableContainerDataTable).on( 'change', 'input.editor-active-activity', function () {
			 activityManagementTab_editor
		            .edit( $(this).closest('tr'), false )
		            .set( 'isActive', $(this).prop( 'checked' ) ? 1 : 0 )
		            .submit();			
	    });   
		 
		 
		 $("#"+jTableContainerDataTable+" tbody").on('click', 'td button .activityListingImg17', function () {
				var tr = $(this).closest('tr');
			    var row = activityManagementTab_oTable.DataTable().row(tr);
				displayActivityRelationShip(row.data().activityId,row.data().activityName);			    
		 });
		 
 });
}

// ---- Ended -----

function activitiesDTFullScreenHandler(flag){
	if(flag){
		reInitializeDTActivityTab();
		$("#"+jTableContainerDataTable+"_wrapper .dataTables_scrollBody").css('max-height','150px');
	}else{
		reInitializeDTActivityTab();
		$("#"+jTableContainerDataTable+"_wrapper .dataTables_scrollBody").css('max-height','385px');
	}
}

function displayPokeNotification(activityId,activityName){
	$("#div_PopupPokeNotifications").modal();
	
	var activityNameTitle="";
	if(activityName.length > 25){         		
		activityNameTitle = (activityName).toString().substring(0,20)+'...';         		
 	} else {
 		activityNameTitle =activityName;
 	}
	
	$("#div_PopupPokeNotifications .modal-header h4").text("["+activityId+"] "+activityNameTitle);
	$("#div_PopupPokeNotifications .modal-header h4").attr('title',activityName);
	$("#div_PopupPokeNotifications .green-haze").text("Poke");
	
	$("#notifyPoke").empty();
	$("#pokeInput").val('');
	$("#pokeActivityID").text(activityId);
	
	toMailContentNrml="@Assignee;@Reviewer";
	toMailContentEsc="@Assignee;@Reviewer";
	$("#pokeToList").val(toMailContentNrml);
	pokeRadioBtnEsc = "@WorkpackageOwner";
	$("#pokeCCList").val(pokeRadioBtnEsc);
	activityNameForSub = activityName;
	pokeSubjectMsg = "Reminder ["+activityNameForSub+"]";
	$("#pokeSubjId").val(pokeSubjectMsg);
	$('#notifyPoke').append('<label><input type="radio" name="radio1" class="icheck" id="alert" data-radio="iradio_flat-grey" onclick="pokeRadioHandler();">Normal'+
			'<input type="radio" name="radio1" style="margin-left: 20px;" class="icheck" id="warning" data-radio="iradio_flat-grey" onclick="pokeRadioHandler();" style="margin-left: 15px;">Escalation</label>');
	$("#alert").attr("checked",true);
	$('#notifyPoke').show();
}

function submitRadioPokeNotificationHandler(){
	
	var pokeMessageFromUI = []; 
	   $("input:radio[name=radio1]:checked").each(function(){
		   pokeMessageFromUI.push($(this).attr('id'));
		}); 
	   
	   var ccMailIds=$("#pokeCCList").val();
	   var pokeCCFlag = ccMailIds.startsWith("@WorkpackageOwner");
		if (pokeCCFlag===true){
			ccMailIds = ccMailIds.split(";")[1];
			if(ccMailIds==""||ccMailIds==undefined){
				ccMailIds = ccMailId;
			}else{
				ccMailIds = ccMailIds.concat(";"+ccMailId);
			}
			
		}
	   
	var toMailIds = $("#pokeToList").val();
		if(toMailIds==undefined){
			toMailIds="";
		}		
	
	var message=$("#pokeInput").val();
	if(message==undefined){
		message="";
	}
	var messageType=pokeMessageFromUI[0];
	$.post('process.activity.poke.notification?activityId='+$("#pokeActivityID").text()+'&message='+message+'&messageType='+messageType+'&ccMailIds='+ccMailIds+'&toMailIds='+toMailIds ,function(data) {
		if(data.Result=="SUCCESS"){
			callAlert(data.Message);
		}
		else if(data.Result=="ERROR"){
			callAlert(data.Message);
		}
	});
	
	$("#div_PopupPokeNotifications").modal('hide');
	
}

function popupClosePokeNotificationHandler(){
	$("#div_PopupPokeNotifications").modal('hide');
}


function displayActivityRelationShip(activityId,activityName) {
	
	
$("#div_PopupActivityRelationShip").modal();
	
	var activityNameTitle="";
	if(activityName.length > 25){         		
		activityNameTitle = (activityName).toString().substring(0,20)+'...';         		
 	} else {
 		activityNameTitle =activityName;
 	}
	
	$("#div_PopupActivityRelationShip .modal-header h4").text("["+activityId+"] "+activityNameTitle);
	$("#div_PopupActivityRelationShip .modal-header h4").attr('title',activityName);
	$("#div_PopupActivityRelationShip .green-haze").text("Activity Relation");
	
	$.post('activity.mapping.by.entity.relation?activityId='+parseInt(activityId),function(data){
		//console.log("success "+data);
		if(data.Result =="OK" && data.TotalRecordCount >0) {
			$("#entityInstanceId1ActivityMapping a").text(data.Records[0].entityInstanceId1);
			$("#entityInstanceId2ActivityMapping a").text(data.Records[0].entityInstanceId2);
		} else {
			callAlert("No Data Exists for this acitivty Relation");
		}
		
	});
}


function displayTabActivityHandlerEntityInstanceId1(){
	if($("#entityInstanceId1ActivityMapping a").text() != null){
		getValue($("#entityInstanceId1ActivityMapping a").text());
	}else{
		alert("No Data Exists for this acitivty ");
	}
	
}


var getValue = function (activityId) {
	var url = "get.activity.by.activityid?activityId="+activityId;
	$.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
						
			if(data.Result=="ERROR"){
    		    data = [];	
    		    callAlert("No Data Exists for this acitivty ");
			}else{
				if(data.Result =="OK" && data.TotalRecordCount >0) {
					data = data.Records;
					displayTabActivitySummaryHandler(activityId);
				} else {
					callAlert("No Data Exists for this acitivty ");
				}
			}
		  },
		  error : function(data) {
			  callAlert("No Data Exists for this acitivty ");
		 },
		
	});	
	
}

var clearTimeoutDTActivitytab='';
var clearTimeoutDTActivityTask='';
var clearTimeoutDTActivityChangeRequest='';
function reInitializeDTActivityTab(){
	clearTimeoutDTActivitytab = setTimeout(function(){
		if(activityManagementTab_oTable != undefined && activityManagementTab_oTable != ""){
			activityManagementTab_oTable.DataTable().columns.adjust().draw();
			clearTimeout(clearTimeoutDTActivitytab);
		}
	},200);
}

function reInitializeDTActivityTask(){
	clearTimeoutDTActivityTask = setTimeout(function(){				
		acitivityTask_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTActivityTask);
	},200);
}

function reInitializeDTActivityChangeRequest(){
	clearTimeoutDTActivityChangeRequest = setTimeout(function(){				
		acitivityChangeRequest_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTActivityChangeRequest);
	},200);
}


var defaultuserId = '1';
var isFirstLoad = true;
var productId;
var productName;		
var defaultActivityPlannedStartDate = new Date();
var defaultActivityPlannedEndDate = new Date();
var defaultActivityLifeCycleStage = 0;
var activityMasterTypeId = 28;

function addActivityComments(prodId,entityInstanceId,entityInstanceName,modifiedById,currentStatusId,currentStatusDisplayName,entityId,secondaryStatusId,visibleEventComment){	
	
	var entityTypeId = 33;//Activity type
	var actionTypeValue = 0;
	$("#addCommentsMainDiv").modal();
	$("#addCommentsDateTimePickerBox").show();
	isSave= true;
	if(!visibleEventComment) {
		$('#addComments').hide();//Display only histroy of task Effort
		$("#viewComments .slimScrollDiv").css("height", "450px");
		$("#viewComments .scroller").css("height", "450px");
	}else {
		$('#addComments').show();
		$("#viewComments .slimScrollDiv").css("height", "200px");
		$("#viewComments .scroller").css("height", "200px");
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
			productId : prodId,
			primaryStatusUrl : 'workflow.status.master.option.list?productId='+prodId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&currentStatusId='+currentStatusId,
			secondaryStatusUrl : 'workflow.entity.secondary.status.master.option.list?productId='+prodId+'&entityTypeId='+entityTypeId+'&statusId='+currentStatusId,
			currentStatusId : currentStatusId,
			currentStatusName : currentStatusDisplayName,
			secondaryStatusId : secondaryStatusId,
			effortListUrl : 'workflow.event.tracker.list?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
			actionTypeValue : actionTypeValue,
			commentsName : commentsReviewActivity,
			"componentUsageTitle":"activity",
			urlToSave : 'workflow.event.tracker.add?productId='+prodId+'&entityId='+entityId+'&entityTypeId='+entityTypeId+'&primaryStatusId=[primaryStatusId]&secondaryStatusId=[secondaryStatusId]&effort=[effort]&comments=[comments]&sourceStatusId='+currentStatusId+'&approveAllEntityInstanceIds=[approveAllEntityIds]&entityInstanceId='+entityInstanceId+'&attachmentIds=[attachmentIds]&actionDate=[actionDate]&actualSize=[actualSize]',				
	};
	AddComments.init(jsonObj);
}

// ----- Deletion Activity Items Started -----

function deleteActivityListingItem(activityId){
	var fd = new FormData();
	fd.append("activityId", activityId);
	
	openLoaderIcon();
	$.ajax({
		url : 'process.activity.delete',
		data : fd,
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			//if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
			if(data.Result != 'undefined' && data.Result == "OK"){
				callAlert("Activity deleted successfully!");
			}
			closeLoaderIcon();
		},
		error : function(data) {
			closeLoaderIcon();  
		},
		complete: function(data){
			closeLoaderIcon();
		}
	});
}

// ----- Ended -----

// ----- Deletion Activity Items Started -----

function deleteActivityTaskItem(activityTaskId){
	var fd = new FormData();
	fd.append("activityTaskId", activityTaskId);
	
	$("#activityTabLoaderIcon").show();
	$.ajax({
		url : 'process.activitytask.delete',
		data : fd,
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
				callAlert(data.Message);
			}
			$("#activityTabLoaderIcon").hide();
		},
		error : function(data) {
			$("#activityTabLoaderIcon").hide();  
		},
		complete: function(data){
			$("#activityTabLoaderIcon").hide();
		}
	});
}

function getActivityTagIndicator(userActivityTagDiffrence){
	if(userActivityTagDiffrence > 1 || userActivityTagDiffrence == 0){
		return 'red';
	}else{
		return 'gray';
	}
}

function getActivityInstanceAndAutoAllocationOfResource(entityId, entityInstanceId, parentEntityId, parentEntityInstanceId){
	$("#activityTabLoaderIcon").show();
	$.ajax({
		type: "POST",
		url : 'common.auto.allocate.resources.for.instance?entityId='+entityId+'&entityInstanceId='+entityInstanceId+'&parentEntityId='+parentEntityId+'&parentEntityInstanceId='+parentEntityInstanceId,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
				callAlert(data.Message);
			}
			$("#activityTabLoaderIcon").hide();
		},
		error : function(data) {
			$("#activityTabLoaderIcon").hide();  
		},
		complete: function(data){
			$("#activityTabLoaderIcon").hide();
		}
	});
}

function initializeActivityTab(){
	
	$("#tablistActivities li").removeClass('active');
	$("#tablistActivities li").eq(0).addClass('active');
	
	$("#tbCntnt #ActivitySummary").addClass('active in');	
	$("#tbCntnt #ActivityTasks").removeClass('active in');
	$("#tbCntnt #ChangeRequests").removeClass('active in');
	$("#tbCntnt #Clarifications").removeClass('active in');
	$("#tbCntnt #attachmentsOfAct").removeClass('active in');
	$("#tbCntnt #StatusPlan").removeClass('active in');
	$("#tbCntnt #auditHistoryOfAct").removeClass('active in');	
	
	$("#customFieldActivitySingleFrequencyContainer").html('');
}

function displayTabActivitySummaryHandler(activityId){
	document.getElementById("treeHdnCurrentActivityId").value = activityId; 
	$("#activityTabSummaryContainer .modal-header h4").text("Activity");
	$("#activityTabSummaryContainer").modal();	
	$("#activitySummaryContainer").append($("#activitesSummaryPage"));
	
	customFieldActivityCreationFlag=false;
	initializeActivityTab();
	showMyActivitySummaryDetails(activityId);	
	$("#activitySummaryContainer").show();
	$("#myActivityStatusSummaryView").css("max-height", "450px");
}

function activityGanttChartHanlder() {
	activityChartView();	
}

function getMultiSelectActivityMove() {
	var workpackageId=0;
	if(document.getElementById("treeHdnCurrentProductId").value != "")
		productId=parseInt(document.getElementById("treeHdnCurrentProductId").value);
	
	if(document.getElementById("treeHdnCurrentActivityWorkPackageId").value != "")
		workpackageId = parseInt(document.getElementById("treeHdnCurrentActivityWorkPackageId").value);
	
	// ----- At engagement level Activities -----
	if(jTableContainerDataTable == "activitiesWP_dataTable"){
		productId=0;
		workpackageId=0;
		activityDragDropHandler(engagementId, productId, workpackageId);
		
	}else if(jTableContainerDataTable == "jTableContainerWPActivities_dataTable" ||
			jTableContainerDataTable == "assigneeActivitiesContainer"){			
		activityDragDropHandler(engagementId, productId, workpackageId);		
				
	}else if(jTableContainerDataTable == "productActivities_dataTable"){
		if(productId == "") {
			productId=0;
		}
		
		var url = 'get.activityworkpackage.by.engagement.product.list?testFactoryId='+engagementId+'&productId='+productId;
		$.ajax({
			type: "POST",
	        contentType: "application/json; charset=utf-8",
			url : url,
			dataType : 'json',		
			success : function(data) {				
				var tempworkpackageId=0;
				if(data != undefined){
					if(data.Records.length==0){
						tempworkpackageId=0;
					}else{					
						document.getElementById("treeHdnCurrentActivityWorkPackageId").value=0;
						tempworkpackageId=data.Records[0].itemId;
					}
					activityDragDropHandler(engagementId, productId, tempworkpackageId);					
				}
			},error : function(data){
				//console.log("error");
			}
		});	
	}	
}

function activityDragDropHandler(engagementId, productId, workpackageId){
	// ----- DragDrop started----		
	var leftUrl="unmap.activities.by.productIdandworkpackageId.count?engagementId="+engagementId+"&productId="+productId+"&activityWorkPackageId="+workpackageId;							
	var rightUrl = "";	
	var leftDefaultUrl="unmap.activities.by.productIdandworkpackageId?engagementId="+engagementId+"&productId="+productId+"&activityWorkPackageId="+workpackageId+"&startIndex=0&pageSize=50";							
	var rightDefaultUrl="map.activities.by.activityWorkPackageId?activityWorkPackageId="+workpackageId;
	var leftDragUrl = "process.multiselection.activity.move?";
    var rightDragtUrl = "process.multiselection.activity.move?";				
	var leftPaginationUrl = "unmap.activities.by.productIdandworkpackageId?engagementId="+engagementId+"&productId="+productId+"&activityWorkPackageId="+workpackageId;
	var rightPaginationUrl="";						
	
	jsonActivityMoveObj={"Title":"Migrate Activities",
			"leftDragItemsHeaderName":"Available Activities",
			"rightDragItemsHeaderName":"Moved Activities ",
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
			"noItems":"No Activities Available",
			"componentUsageTitle":"ActivityMultiselctMove"
			};
	
	DragDropListItems.init(jsonActivityMoveObj);							
	// DragDrop ended----	
	loadDragDropSelectOption(jsonActivityMoveObj);
}


function listActivityAuditHistory(activityId){
	clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=Activity&sourceEntityId='+activityId+'&jtStartIndex=0&jtPageSize=1000';
	//var url='administration.event.list?sourceEntityType=Activity&sourceEntityId='+activityId;
	var jsonObj={"Title":"Activity Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":1000,	    
			"componentUsageTitle":"activityAudit",
	};
	SingleDataTableContainer.init(jsonObj);
	//SingleJtableContainer.init(jsonObj);
}

// ----- custom field activity list -----
var urlActivityDTPagination='';
function activityCustomFieldHandler(productId,productVersionId,productBuildId,activityWorkPackageId,isActive,enableAddOrNot,jTableContainerId,customFieldUrl,type,listUrl){
		$.ajax({
			type: "POST",
	        contentType: "application/json; charset=utf-8",
			url : customFieldUrl,
			dataType : 'json',		
			success : function(data) {				
				var resultRecords=[];				
				if(data.Records.length==0){
					resultRecords=[];
				}else{											
					resultRecords=data.Records;
				}				
				
				if(type == "customFieldID"){					
					customFieldResultObjCollection=[];
					customFieldResultObjCollection=resultRecords;		
					
					customFieldUrl = 'get.custom.field.exist.for.all.instance?entityId=28&entityParentInstanceId='+activityWorkPackageId+'&engagementId='+engagementId+'&productId='+productId;
					activityCustomFieldHandler(productId,productVersionId,productBuildId,activityWorkPackageId,isActive,enableAddOrNot,jTableContainerId,customFieldUrl,"customFieldValue",listUrl);
					
				}else if(type == "customFieldValue"){
					singleFrequencyJsonResponseValue = [];
					singleFrequencyJsonResponseValue = resultRecords;
															
					var statusType = $("#workflow_status_dd").find('option:selected').val();
					var urlToGetActivitiesOfSpecifiedAWPId_URL = listUrl+'='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&activityWorkPackageId='+activityWorkPackageId+'&isActive='+isActive+'&statusType='+statusType;	
					//var url = urlToGetActivitiesOfSpecifiedAWPId_URL;
					
					var parentID=getParentIDPagination();
					var currentPageCountSelectedActivity = $('#'+parentID+ ' #selectActivityCountDT').find('option:selected').val();					
					//var url = urlToGetActivitiesOfSpecifiedAWPId_URL+'&jtStartIndex=0&jtPageSize='+currentPageCountSelectedActivity;					
					urlActivityDTPagination= urlToGetActivitiesOfSpecifiedAWPId_URL;
						
					if(jTableContainerId == "assigneeActivitiesContainer" || 
							jTableContainerId == "pendingWithActivitiesContainer" || 
							jTableContainerId == "passingThroughContainer"){
						urlActivityDTPagination = myActivitiesSelectedTabUrl;
					}			
					//assignDataTableValuesInActivityManagementTab(url, "150px", "parentTable", activityWorkPackageId, productId);					
					var page = Number($('#'+parentID+ ' #paginationContainerDT').find('span.current').text());
					allActivityFunDT(page, currentPageCountSelectedActivity);
				}
							
			},error : function(data){
				console.log("error");				
			}
		});	
}

function getWorkpackageOwnerMailByWPId(actId,actName,activityWpId){
	var url = 'get.wpOwner.mailId.by.actWPId?activityWPId='+activityWpId;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',		
		success : function(data) {				
			ccMailId = data.Records[0].emailId;
			displayPokeNotification(actId,actName);
			$("#pokeCCList").val('');
		},error : function(data){
		}
	});
	
}

function pokeRadioHandler(){
	if (document.getElementById('warning').checked) {
		toMailContentNrml = $("#pokeToList").val();
		if(toMailContentEsc==""){
			toMailContentEsc="@Assignee;@Reviewer";
			$("#pokeToList").val(toMailContentEsc);
		}else{
			$("#pokeToList").val(toMailContentEsc);
		}
		pokeRadioBtnNrml = $("#pokeCCList").val();
		pokeSubjectMsg = "Escalation ["+activityNameForSub+"]";			
		$("#pokeSubjId").val(pokeSubjectMsg);
			if(pokeRadioBtnEsc==""){
				pokeRadioBtnEsc = "@WorkpackageOwner";
				$("#pokeCCList").val(pokeRadioBtnEsc);
			}else{
				$("#pokeCCList").val(pokeRadioBtnEsc);
			}
		}else{
			toMailContentEsc = $("#pokeToList").val();
			if(toMailContentNrml==""){
				toMailContentNrml="@Assignee;@Reviewer";
				$("#pokeToList").val(toMailContentNrml);
			}else{
				$("#pokeToList").val(toMailContentNrml);
			}
			pokeRadioBtnEsc = $("#pokeCCList").val();
			pokeSubjectMsg = "Reminder ["+activityNameForSub+"]";
			$("#pokeSubjId").val(pokeSubjectMsg);
			if(pokeRadioBtnNrml==""){
				pokeRadioBtnNrml = "";			
				$("#pokeCCList").val(pokeRadioBtnNrml);
			}else{
				$("#pokeCCList").val(pokeRadioBtnNrml);
			}			
		}
}
