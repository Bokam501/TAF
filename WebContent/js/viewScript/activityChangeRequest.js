var ActivityChangeRequest = function(){	
	var initialise = function(jsonObj){
		initialiseActivityChangeRequestTab (jsonObj);
	};	
		return {
			init : function(jsonObj){
				initialise(jsonObj);
			}
		};
}();
var activityCRTabJsonObj='';
var entityTypeId;
function initialiseActivityChangeRequestTab(jsonObj){
	activityCRTabJsonObj = jsonObj;
	if(activityCRTabJsonObj.componentUsage == "ProductLevel") {
		entityTypeId=18;
	} else if (activityCRTabJsonObj.componentUsage == "WorkpackageLevel") {
		entityTypeId=34;
	}
	listChangeRequestsOfSelProdComponent(activityCRTabJsonObj);
}

var changeRequestTitleDT = "";
var addPoPupChangeRequestNameDT = "";
function crTOUseCaseDT(){
	if(changeRequestToUsecase == "YES"){		
		changeRequestTitleDT = "Add/Edit Use Cases";	
		addPoPupChangeRequestNameDT = 'Use Case Name <font color="#efd125" size="4px">*</font>';
		//$('#prodChangeRequests').text("Use Case");
	}else{
		changeRequestTitleDT = "Add/Edit Change Request";
		addPoPupChangeRequestNameDT = 'Change Request Name <font color="#efd125" size="4px">*</font>';
	}
	return changeRequestTitleDT;
}

var activityChangeRequestTab_oTable='';
var activityChangeRequestTab_editor='';
var clearTimeoutDTActivityChangeRequestTab='';

var activityCROptionsArrTab=[];
var activityCRTabResultArr=[];
var activityCROptionsItemCounter=0;
var optionsType_ActivityChangeRequestTab='ActivityChangeRequestTab';

function listChangeRequestsOfSelProdComponent(jsonObj){	
	openLoaderIcon();
	
	 $.ajax({
		  type: "POST",
		  url: jsonObj.listURL,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			activityChangeRequestTabResults_Container(data, jsonObj);
			//activityChangeRequestTab_Container(data, jsonObj); 
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function activityChangeRequestTabResults_Container(data, jsonObj){
	
	activityCROptionsItemCounter=0;
	activityCRTabResultArr=[];
	activityCROptionsArrTab = [{id:"priorityId", type:optionsType_ActivityChangeRequestTab, url:'administration.executionPriorityList'},
					{id:"statusCategoryId", type:optionsType_ActivityChangeRequestTab, url:'status.category.option.list'},
	               {id:"ownerId", type:optionsType_ActivityChangeRequestTab, url:'common.user.list.by.resourcepool.id.productId?productId='+jsonObj.productId},
	           ];
	returnOptionsItemForActivityManagementTab(activityCROptionsArrTab[0].url, data, jsonObj);
	
}

function returnOptionsItemForActivityManagementTab(url, data, jsonObj){
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
         	activityCRTabResultArr.push(json.Options);         	
         	activityChangeRequestTab_Container(data, row, tr); 
         	
         }else{
        	 if(activityCROptionsArrTab[0].type == optionsType_ActivityChangeRequestTab && activityCROptionsItemCounter == 2){
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
     	   activityCRTabResultArr.push(json.Options);
     	   
     	   if(activityCROptionsItemCounter<activityCROptionsArrTab.length-1){
     		 returnOptionsItemForActivityManagementTab(activityCROptionsArrTab[activityCRTabResultArr.length].url, data, jsonObj);     		  
     	   }else{
     		    activityChangeRequestTab_Container(data, jsonObj);
     	   }
     	   activityCROptionsItemCounter++;     	   
         }
         },
         error: function (data) {
        	 activityCROptionsItemCounter++;
        	 
         },
         complete: function(data){
         	//console.log('Completed');
         	
         },	            
   	});	
}

// ----- Ended -----

function createEditorForactivityChangeRequestTab(jsonObj){
	
	activityChangeRequestTab_editor = new $.fn.dataTable.Editor( {
			"table": '#activityCRTab_'+jsonObj.containerID+'_dataTable',
			ajax: jsonObj.creatURL,
			ajaxUrl: jsonObj.updateURL,			
			"idSrc":  "changeRequestId",
			i18n: {
				create: {	    	           
					title: crTOUseCaseDT() +'*',
					submit: "Create",
				}
			},
			fields: [ {
				label: "changeRequestId",
				name: "changeRequestId",
				"type": "hidden",					
			},{
				label: "entityType",
				name: "entityType",
				"type": "hidden",					
			},{					
				label: addPoPupChangeRequestNameDT,
				name: "changeRequestName",		            
			},{
				label: "Source",
				name: "entityInstanceName",			
			},{
				label: "entityType1",
				name: "entityType1",
				"type": "hidden",
				def: entityTypeId, 	
			},{
				label: "entityType2",
				name: "entityType2",
				"type": "hidden",
				def: changeRequestTypeId, 	
			},{
				label: "entityInstance1",
				name: "entityInstance1",
				"type": "hidden",	
				def: jsonObj.productId, 		
			},{
				label: "Description",
				name: "description",					
			},{
				label: "priorityName",
				name: "priorityName",
				"type": "hidden",				
			},{
				label: "Priority",
				name: "priorityId",
				options: activityCRTabResultArr[0],
				"type"  : "select",	
			},{
				label: "Planned Value",
				name: "planExpectedValue",
				defaultValue: 0,	            
			},{
				label: "statusCategoryName",
				name: "statusCategoryName",
				"type": "hidden",				
			},{
				label: "isActive",
				name: "isActive",
				"type": "hidden",
				def: 1,					
			},{
				label: "productId",
				name: "productId",
				"type": "hidden",
				def: jsonObj.productId,					
			},{
				label: "productName",
				name: "productName",
				"type": "hidden",				
			},{
				label: "Status",
				name: "statusCategoryId",
				options: activityCRTabResultArr[1],
				"type"  : "select",					
			},{
				label: "ownerName",
				name: "ownerName",
				"type": "hidden",				
			},{
				label: "Owner",
				name: "ownerId",
				options: activityCRTabResultArr[2],
				"type"  : "select",		
				def: defaultuserId,		
			},{
				label: "Raised Date *",
				name: "raisedDate",		            
				type:  'datetime',
				def:    function () { return new Date(); },
				format: 'M/D/YYYY',
			},{
				label: "attachmentCount",
				name: "attachmentCount",
				"type": "hidden",	
				 def: 0,	
			}]
		});
}

function reInitializeDTActivityChangeRequestTab(){
	clearTimeoutDTActivityChangeRequestTab = setTimeout(function(){				
		activityChangeRequestTab_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTActivityChangeRequestTab);
	},200);
}

function activityCRTabHeader(){
	var childDivString = '<table id="activityCRTab_'+activityCRTabJsonObj.containerID+'_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>Source</th>'+			
			'<th>Source Name</th>'+	
			'<th>Name</th>'+
			'<th>Description</th>'+
			'<th>Priority</th>'+
			'<th>Planned Value</th>'+
			'<th>Status</th>'+
			'<th>Owner</th>'+		
			'<th>Active</th>'+		
			'<th>Raised Date</th>'+
			'<th></th>'+			
		'</tr>'+
	'</thead>'+
	'<tfoot>'+
		'<tr></tr>'+
	'</tfoot>'+
	'</table>';		
	
	return childDivString;
}

function activityChangeRequestTab_Container(data, jsonObj){
	try{
		if ($("#"+jsonObj.containerID).children().length>0) {
			$("#"+jsonObj.containerID).children().remove();
		}
	} 
	catch(e) {}
	
	var emptytr = emptyTableRowAppending(12);  // total coulmn count		  
	var childDivString = activityCRTabHeader(); 			 
	$("#"+jsonObj.containerID).append(childDivString);
	
	$('#'+jsonObj.containerID+' tfoot tr').html('');     			  
	$('#'+jsonObj.containerID+' tfoot tr').append(emptytr);
	
	var titleCrUc = "";
	
	 if(changeRequestToUsecase == "YES"){
	 	   titleCrUc = "Activity Use Cases";		   
	 }else{		   
		   titleCrUc = "Activity Change Request";
	 }	
	
	// --- editor for the activity Change Request started -----
	createEditorForactivityChangeRequestTab(jsonObj);
	
	if(data.length>0){
		for(var i=0;i<data.length;i++){
			if(data[i]["raisedDate"] == null){				
				data[i]["raisedDate"] = null;				
			}else{
				data[i]["raisedDate"] = convertDTDateFormatAdd(data[i]["raisedDate"]);				
			}
		}
	}
	
	activityChangeRequestTab_oTable = $('#activityCRTab_'+activityCRTabJsonObj.containerID+'_dataTable').dataTable( {
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
			   
			   var searchcolumnVisibleIndex = [11]; // search column TextBox Invisible Column position			   
     		  $('#activityCRTab_'+activityCRTabJsonObj.containerID+'_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			   
			   reInitializeDTActivityChangeRequestTab();			   
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: activityChangeRequestTab_editor },	  
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
		            'colvis',
					{					
					text: '<i class="fa fa-upload showHandCursor" title="Upload Activities ChangeRequest"></i>',
						action: function ( e, dt, node, config ) {					
							triggerActivityCRTabUpload();
						}
					},
					{					
						text: '<i class="fa fa-download showHandCursor" title="Download Template - Change Requests"></i>',
						action: function ( e, dt, node, config ) {					
							downloadTemplateChangeRequest();
						}
					}
	         ], 	         
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "changeRequestId",className: 'disableEditInline', sWidth: '10%' },
			{ mData: "entityType",className: 'disableEditInline', sWidth: '10%' },		   
			{ mData: "entityInstanceName",className: 'disableEditInline', sWidth: '10%' },		   			
           { mData: "changeRequestName",className: 'editable', sWidth: '10%' },
           { mData: "description",className: 'editable', sWidth: '20%' },           
			{ mData: "priorityName", className: 'editable', sWidth: '20%', editField: "priorityId",				
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(activityChangeRequestTab_editor, 'priorityId', full.priorityId);
					 }else if(type == "display"){
						data = full.priorityName;
					 }	           	 
					 return data;
				 },
			},	
           { mData: "planExpectedValue",className: 'editable', sWidth: '5%' },			
			{ mData: "statusCategoryName", className: 'editable', sWidth: '15%',  editField: "statusCategoryId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(activityChangeRequestTab_editor, 'statusCategoryId', full.statusCategoryId);
					 }else if(type == "display"){
						data = full.statusCategoryName;
					 }	           	 
					 return data;
				 },
			},
		   { mData: "ownerName", className: 'editable', sWidth: '15%',  editField: "ownerId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(activityChangeRequestTab_editor, 'ownerId', full.ownerId);
					 }else if(type == "display"){
						data = full.ownerName;
					 }	           	 
					 return data;
				 },
			},
			{ mData: "isActive",
              mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editor-active-activityTabCR">';
                    }
                    return data;
				},
                className: "dt-body-center"
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
           },{ mData: null,				 
            	bSortable: false,
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
     	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<img src="css/images/attachment.png" class="activityChangeRequesTabtImg1" title="Attachment" style="width: 18px;height: 18px;">&nbsp;['+data.attachmentCount+']&nbsp;</img></button>'+
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="fa fa-search-plus activityChangeRequesTabtImg2" title="Audit History"></i></button>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						'<i class="fa fa-comments activityChangeRequesTabtImg3" title="Comments"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },
       ],
		 rowCallback: function ( row, data ) {
            $('input.editor-active-activityTabCR', row).prop( 'checked', data.isActive == 1 );
        },  
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		 
		 $("#activityTabLoaderIcon").hide();
		 
		 $('#activityCRTab_'+activityCRTabJsonObj.containerID+'_dataTable_length').css('margin-top','8px');
		 $('#activityCRTab_'+activityCRTabJsonObj.containerID+'_dataTable_length').css('padding-left','35px');
	 
		 // ----- focus area -----			
		$( 'input', activityChangeRequestTab_editor.node()).on( 'focus', function () {
			this.select();
		});
		 		 
		 activityChangeRequestTab_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );
		 
		 // Activate an inline edit on click of a table cell
        $('#activityCRTab_'+activityCRTabJsonObj.containerID+'_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	activityChangeRequestTab_editor.inline( this, {
        		submitOnBlur: true
			}); 
		});
		
		$('#activityCRTab_'+activityCRTabJsonObj.containerID+'_dataTable').on( 'change', 'input.editor-active-activityTabCR', function () {
			 activityChangeRequestTab_editor
				.edit( $(this).closest('tr'), false )
				.set( 'isActive', $(this).prop( 'checked' ) ? 1 : 0 )
				.submit();			
	    });
			
		// ------ Attachments -----
		 
		 $('#activityCRTab_'+activityCRTabJsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityChangeRequesTabtImg1', function () {
				var tr = $(this).closest('tr');
			    var row = activityChangeRequestTab_oTable.DataTable().row(tr);
			    				
				isViewAttachment = false;
				var jsonObj={"Title":"Attachments for ChangeRequest",			          
					"SubTitle": 'ChangeRequest : ['+row.data().changeRequestId+'] '+row.data().changeRequestName,
					"listURL": 'attachment.for.entity.or.instance.list?productId='+row.data().productId+'&entityTypeId=42&entityInstanceId='+row.data().changeRequestId,
					"creatURL": 'upload.attachment.for.entity.or.instance?productId='+row.data().productId+'&entityTypeId=42&entityInstanceId='+row.data().changeRequestId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
					"deleteURL": 'delete.attachment.for.entity.or.instance',
					 "updateURL": 'update.attachment.for.entity.or.instance',
					"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=9',
					"multipleUpload":true,
				};
				$('#attachmentPopup').css('z-index','10051');
				Attachments.init(jsonObj);
				
			    
		 });
		 // ----- ended -----
		 
		 // ----- Audit History -----
		 
		 $('#activityCRTab_'+activityCRTabJsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityChangeRequesTabtImg2', function () {
			 var tr = $(this).closest('tr');
			  var row = activityChangeRequestTab_oTable.DataTable().row(tr);
			  
			  var titleCrtoUc = '';
				if(changeRequestToUsecase == "YES"){
					titleCrtoUc = "Use Cases";
				}else{
					titleCrtoUc = "ChangeRequest";
				}
				
			listGenericAuditHistory(row.data().changeRequestId,titleCrtoUc,"changeRequestAudit");		 
			   
		 });
		 
		 // ----- ended -----

		// -----Comments -----
		 
		 $('#activityCRTab_'+activityCRTabJsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityChangeRequesTabtImg3', function () {
			 var tr = $(this).closest('tr');
			 var row = activityChangeRequestTab_oTable.DataTable().row(tr);
			 
			var entityTypeIdComments = 42;
			var entityNameComments = "ClarificationTracker";
			listComments(entityTypeIdComments, entityNameComments, row.data().changeRequestId, row.data().changeRequestName, "productLevelCRComments");
						
		 });
		 
		 // ----- ended -----		 
		 		 		
	});
	// ------	
}

/*
var auditHistoryTabDT_oTable='';

//-- Audit History for the Product Management Plan ----
function auditHistoryTabDataTable(containerID){
	var childDivString = '<table id="auditHistoryTab_'+containerID+'dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>Event</th>'+
			'<th>Description</th>'+
			'<th>Remarks</th>'+
			'<th>User</th>'+
			'<th>Time</th>'+
		'</tr>'+
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+
	'</table>';		
	
	return childDivString;	
}

var clearTimeoutTabDataTable;
function reInitializeTabDTAuditHistory(){
	clearTimeoutTabDataTable = setTimeout(function(){				
		auditHistoryTabDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTabDataTable);
	},200);
}

function auditHistoryTabCompListing(containerID, data){
	
	try{
		if ($("#"+containerID).children().length>0) {
			$("#"+containerID).children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = auditHistoryTabDataTable(containerID); 			 
	$("#"+containerID).append(childDivString);
	
	auditHistoryTabDT_oTable = $('#auditHistoryTab_'+containerID+'_dataTable').dataTable( {
		 	"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       "aaSorting": [[5,'desc']],
	       "fnInitComplete": function(data) {
	    	   
	    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     	  var headerItems = $('#auditHistoryTab_'+containerID+'dataTable_wrapper tfoot tr th');
	     	  headerItems.each( function () {   
	   	    	    var i=$(this).index();
	   	    	    var flag=false;
	   	    	    var singleItem = $(headerItems).eq(i).find('div'); 
	   	    	    for(var j=0; j < searchcolumnVisibleIndex.length; j++){
	   	    	    	if(i == searchcolumnVisibleIndex[j]){
	   	    	    		flag=true;
	   	    	    		$(singleItem).css('height','0px');
	    	    	    	$(singleItem).css('color','#4E5C69');
	    	    	    	$(singleItem).css('line-height','12px');
	   	    	    		break;
	   	    	    	}else{
	   	    	    		$(singleItem).css('height','0px');
	    	    	    	$(singleItem).css('color','#4E5C69');
	    	    	    	$(singleItem).css('line-height','12px');
	   	    	    	}
	   	    	    }
	   	    	    
	   	    	    if(searchcolumnVisibleIndex.length==0){
	   	    	    	$(singleItem).css('height','0px');
	     	    		$(singleItem).css('color','#4E5C69');
	     	    		$(singleItem).css('line-height','12px');
	   	    	    }
	   	    	    
	   	    	    if(!flag){
	   	    	    	$(this).prepend( '<input type="text" name="'+data.aoColumns[i].mData+'" value="" style="width:100%" class="search_init" />');
	   	    	    }
	     	   });	 	  
	    	   reInitializeTabDTAuditHistory();
		   },  
		   buttons: [
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Audit History',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Audit History',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Audit History',
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
	       { mData: "eventId", className: 'disableEditInline', sWidth: '5%' },
           { mData: "eventName", className: 'disableEditInline', sWidth: '10%' },		
           { mData: "eventDescription", className: 'disableEditInline', sWidth: '35%' },		
           { mData: "sourceEntityName", className: 'disableEditInline', sWidth: '30%' },		
           { mData: "userName", className: 'disableEditInline', sWidth: '10%' },		
           { mData: "endTime", className: 'disableEditInline', sWidth: '10%' },	
       ],       
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		
		$('#auditHistoryTab_'+containerID+'dataTable_length').css('margin-top','8px');
		$('#auditHistoryTab_'+containerID+'dataTable_length').css('padding-left','35px');
		
		$('#auditHistoryTab_'+containerID+'_dataTable').DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });
	});
}

*/