var activityClarificationTabJsonObj='';
var ActivityClarification = function() {
 	var initialise = function(jsonObj){
 		assignActivityClarificationDT(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};		
}();

var entityTypeId;
var entityInstanceId;
function assignActivityClarificationDT(jsonObj){
	activityClarificationTabJsonObj = jsonObj;
	if(activityClarificationTabJsonObj.componentUsage == "ProductLevel") {
		entityTypeId=18;
		entityInstanceId=productId;
	} else if (activityClarificationTabJsonObj.componentUsage == "WorkpackageLevel") {
		entityTypeId=34;
		entityInstanceId=workPackageId;
	}
	clarificationListDataTable();
}

var activityClarificationListDT_oTable='';
var editorActivityClarificationList='';
var optionsClarificationClarificationArr=[];
var optionsClarificationResultArr=[];
var optionsClarificationItemCounter=0;
var tableType;
var clarfnTrackerId;

function clarificationListDataTable(){
	optionsClarificationItemCounter=0;
	optionsClarificationResultArr=[];	
	optionsClarificationArr = [{id:"clarificationTypeList", url:'list.clarification.type.option'},
		  {id:"executionPriorityList", url:'administration.executionPriorityList'},
		  {id:"statusOptionList", url:'clarification.status.option.list?entityTypeId=31'},
		  {id:"parentStatusList", url:'clarification.resolution.option.list?parentStatusId=15'},
		  {id:"allUsersList", url:'common.allusers.list.by.resourcepool.id'},
		  {id:"clarificationScopeId", url:'list.clarification.scope.option'},
	 ];

	activityClarificationListOptions_Container(optionsClarificationArr);
}

function activityClarificationListOptions_Container(urlArr){
	$.ajax( {
 	   "type": "POST",
        "url":  urlArr[optionsClarificationItemCounter].url,
        "dataType": "json",
         success: function (json) {		        	 
	         if(json.Result == "OK"){						 
				 if(json.Options.length>0){     		   
					   for(var i=0;i<json.Options.length;i++){
						   json.Options[i].label=json.Options[i].DisplayText;
						   json.Options[i].value=json.Options[i].Value;
					   }			   
				   }else{
					  json.Options=[];
				   }     	   
				   optionsClarificationResultArr.push(json.Options);						 
	         }
	         optionsClarificationItemCounter++;	
			 if(optionsClarificationItemCounter<optionsClarificationArr.length){
				 activityClarificationListOptions_Container(optionsClarificationArr);
			 }else{
				assignActivityClarificationDTValues(activityClarificationTabJsonObj);	
			 }					 
         },
         error: function (data) {
			optionsClarificationItemCounter++;
         },
         complete: function(data){
         	//console.log('Completed');
         },	            
   	});
}

function assignActivityClarificationDTValues(jsonObj){
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
			jsonObj.data = data;
			activityClarificationListDT_Container(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function activityClarificationHeader(){
	var childDivString ='<table id="clarification_'+activityClarificationTabJsonObj.containerID+'_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>Source</th>'+
			'<th>Source Name</th>'+
			'<th>Title</th>'+			
			'<th>Description</th>'+
			'<th>Priority</th>'+
			'<th>Type</th>'+
			'<th>Scope</th>'+
			'<th>Planned Value</th>'+
			'<th>Status</th>'+
			'<th>Resolution</th>'+ 
			'<th>Owner</th>'+
			'<th>Raised By</th>'+
			'<th>Raised Date</th>'+
			'<th>Expected End Date</th>'+						
			'<th></th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+			
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}

function createEditorForActivityClarificationListTab(jsonObj){
	editorActivityClarificationList = new $.fn.dataTable.Editor( {
	    "table": '#clarification_'+jsonObj.containerID+'_dataTable',
		ajax: "add.clarificationtracker.by.activity",
		ajaxUrl: "update.clarificationtracker.by.activity",
		idSrc:  "clarificationTrackerId",
		i18n: {
	        create: {
	            title:  "Create a new clarification",
	            submit: "Create",
	        }
	    },
		fields: [
		 {
             label: "activityId",
             name: "activityId",
             "type": "hidden",
			 def : activityId
          },{
             label: "productId",
             name: "productId",
             "type": "hidden",
			 def: productId
          },{
             label: "testFactoryId",
             name: "testFactoryId",
             "type": "hidden",
			 def: testFactId
          },{
             label: "entityTypeId",
             name: "entityTypeId",
             "type": "hidden",
			 def: entityTypeId
          },{
             label: "entityTypeId2",
             name: "entityTypeId2",
             "type": "hidden",
			 def: clarificationTypeId
          },{
             label: "entityInstanceId",
             name: "entityInstanceId",
             "type": "hidden",
			 def: entityInstanceId
          },{
            label: "ID",
            name: "clarificationTrackerId",
            "type": "hidden",
         },{
             label: "Entity Type",
             name: "entityType",
             "type": "hidden",
          },{
              label: "Entity Instance Name",
              name: "entityInstanceName",
              "type": "hidden",
           },{
             label: "Clarification Title *",
             name: "clarificationTitle",
         },{
             label: "priority",
             name: "priorityId",
             options: optionsClarificationResultArr[1],
             "type":"select"
          },{
            label: "Description",
            name: "clarificationDescription",
        },{
             label: "Clarification Type *",
             name: "clarificationTypeId",
             options: optionsClarificationResultArr[0],
             "type":"select"
         },{
            label: "clarification Scope *",
            name: "clarificationScopeId",
            options: optionsClarificationResultArr[5],
            "type":"select"
        },{
            label: "Planned Value",
            name: "planExpectedValue",
			def:0
        },{
            label: "Status",
            name: "workflowStatusId",
            options: optionsClarificationResultArr[2],
            "type":"select"
        },{
            label: "Resolution",
            name: "resolution",
            options: optionsClarificationResultArr[3],
            "type":"select"
        },{
              label: "ownerName",
              name: "ownerName",
              "type": "hidden",
	   },{
            label: "Owner",
            name: "ownerId",
            options: optionsClarificationResultArr[4],
            "type":"select"
        },{
            label: "Raised By *",
            name: "raisedById",
            options: optionsClarificationResultArr[4],
            "type":"select"
        },{
            label: "Raised Date *",
            name: "raisedDate", 
			type:  'datetime',
			def:    function () { return new Date(); },
			format: 'M/D/YYYY',
        },{
            label: "Expected End Date",
            name: "plannedEndDate", 
			type:  'datetime',
			def:    function () { return new Date(); },
			format: 'M/D/YYYY',
        }        
    ]
	});
}
function activityClarificationListDT_Container(jsonObj){
	
	try{
		if ($("#"+jsonObj.containerID).children().length>0) {
			$("#"+jsonObj.containerID).children().remove();
		}
	} 
	catch(e) {}
	
	var emptytr = emptyTableRowAppending(16);  
	var childDivString = activityClarificationHeader(); 			 
	$("#"+jsonObj.containerID).append(childDivString);
		
	$('#'+jsonObj.containerID+' tfoot tr').html('');     			  
	$('#'+jsonObj.containerID+' tfoot tr').append(emptytr);
	
	createEditorForActivityClarificationListTab(jsonObj);
	
	if(jsonObj.data.length>0){
		for(var i=0;i<jsonObj.data.length;i++){
			if(jsonObj.data[i]["raisedDate"] == null){				
				jsonObj.data[i]["raisedDate"] = null;				
			}else{
				jsonObj.data[i]["raisedDate"] = convertDTDateFormatAdd(jsonObj.data[i]["raisedDate"]);				
			}
			
			if(jsonObj.data[i]["actualStartDate"] == null){				
				jsonObj.data[i]["actualStartDate"] = null;				
			}else{
				jsonObj.data[i]["actualStartDate"] = convertDTDateFormatAdd(jsonObj.data[i]["actualStartDate"]);				
			}
			
			if(jsonObj.data[i]["actualEndDate"] == null){				
				jsonObj.data[i]["actualEndDate"] = null;				
			}else{
				jsonObj.data[i]["actualEndDate"] = convertDTDateFormatAdd(jsonObj.data[i]["actualEndDate"]);				
			}
			
			if(jsonObj.data[i]["plannedStartDate"] == null){				
				jsonObj.data[i]["plannedStartDate"] = null;				
			}else{
				jsonObj.data[i]["plannedStartDate"] = convertDTDateFormatAdd(jsonObj.data[i]["plannedStartDate"]);				
			}
			
			if(jsonObj.data[i]["plannedEndDate"] == null){				
				jsonObj.data[i]["plannedEndDate"] = null;				
			}else{
				jsonObj.data[i]["plannedEndDate"] = convertDTDateFormatAdd(jsonObj.data[i]["plannedEndDate"]);				
			}
		}
	}
		
	activityClarificationListDT_oTable = $('#clarification_'+activityClarificationTabJsonObj.containerID+'_dataTable').dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "90%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 	      
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [15]; // search column TextBox Invisible Column position
	     		  $('#clarification_'+activityClarificationTabJsonObj.containerID+'_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeActivityClarificationListDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorActivityClarificationList },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Clarification',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Clarification',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		'colvis',
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "clarificationTrackerId", className: 'disableEditInline', sWidth: '15%' },	
           { mData: "entityType", className: 'disableEditInline', sWidth: '15%' },	
           { mData: "entityInstanceName", className: 'disableEditInline', sWidth: '12%' },
           { mData: "clarificationTitle", className: 'editable', sWidth: '12%' },	            
           { mData: "clarificationDescription", className: 'editable', sWidth: '12%' },		
           { mData: "priorityName", className: 'editable', sWidth: '10%', editField: "priorityId",
	       		mRender: function (data, type, full) {
		           	 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(editorActivityClarificationList, 'priorityId', full.priorityId);
					 }else if(type == "display"){
						data = full.priorityName;
					 }
					return data;
	            },
			},
			{ mData: "clarificationTypeId", className: 'editable', sWidth: '10%',
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorActivityClarificationList, 'clarificationTypeId', full.clarificationTypeId); 		           	 
		             return data;
	            },
			},
			{ mData: "clarificationScopeId", className: 'editable', sWidth: '10%',
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorActivityClarificationList, 'clarificationScopeId', full.clarificationScopeId); 		           	 
		             return data;
	            },
			},
			{ mData: "planExpectedValue", className: 'editable', sWidth: '12%' },
			{ mData: "workflowStatusId", className: 'editable', sWidth: '10%', 
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorActivityClarificationList, 'workflowStatusId', full.workflowStatusId); 		           	 
		             return data;
	            },
			},
			{ mData: "resolution", className: 'editable', sWidth: '10%', editField: "resolution",
	       		mRender: function (data, type, full) {
					if(full.resolution == null){
						data = "--";
					}else{
						data = optionsValueHandler(editorActivityClarificationList, 'resolution', full.resolution); 		           	 
					}
		             return data;
	            },
			},
			{ mData: "ownerName", className: 'editable', sWidth: '10%', editField: "ownerId",
	       		mRender: function (data, type, full) {
		           	if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(editorActivityClarificationList, 'ownerId', full.ownerId);
					 }else if(type == "display"){
						data = full.ownerName;
					 }
					 return data;
	            },
			},
			{ mData: "raisedByName", className: 'editable', sWidth: '10%', editField: "raisedById",
	       		mRender: function (data, type, full) {
		           	 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(editorActivityClarificationList, 'raisedById', full.raisedById);
					 }else if(type == "display"){
						data = full.raisedByName;
					 }
					return data;
	            },
			},
			//{ mData: "raisedDate", className: 'editable', sWidth: '12%' },
			//{ mData: "plannedEndDate", className: 'editable', sWidth: '12%' },
			{ mData: "raisedDate", className: 'editable', sWidth: '15%',
           		mRender: function (data, type, full) {
		           	 /*if (full.action == "create"){
		           		data = convertDTDateFormatAdd(data, ["raisedDate"]);		           		
		           	 }else if(type == "display"){
		           		data = full.raisedDate;
		           	 }*/
					 data = full.raisedDate;
		             return data;
	             }
           },
		   { mData: "plannedEndDate", className: 'editable', sWidth: '15%',
           		mRender: function (data, type, full) {
		           	 /*if (full.action == "create"){
		           		data = convertDTDateFormatAdd(data, ["plannedEndDate"]);		           		
		           	 }else if(type == "display"){
		           		data = full.plannedEndDate;
		           	 }	 */
					data = full.plannedEndDate;					 
		             return data;
	             }
           },
            { mData: null,				 
            	bSortable: false,
				className: 'disableEditInline',
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       			'<div><img src="css/images/attachment.png" class="activityClarificationTabtImg1" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+full.attachmentCount+']</span>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						'<i class="fa fa-comments activityClarificationTabtImg2" title="Comments"></i></button>'+
					'</div>'+
           		'</div>');	
           		 return img;
            	}
            },	
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorActivityClarificationList-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#clarification_'+activityClarificationTabJsonObj.containerID+'_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorActivityClarificationList.inline( this, {
            submitOnBlur: true
        } );
	});
	
	 // ----- focus area -----			
		$( 'input', editorActivityClarificationList.node()).on( 'focus', function () {
			this.select();
		});
		
	$('#clarification_'+activityClarificationTabJsonObj.containerID+'_dataTable tbody').on('click', 'td .activityClarificationTabtImg1', function () {
		var tr = $(this).closest('tr');
    	var row = activityClarificationListDT_oTable.DataTable().row(tr);
		if(activityClarificationTabJsonObj.productId == 0){
			callAlert("Please select a product");
			return;
		}
		isViewAttachment = false;
		var jsonObj={"Title":"Attachments for Clarification",			          
			"SubTitle": 'Clarification : ['+row.data().clarificationTrackerId+'] '+row.data().clarificationTitle,
			"listURL": 'attachment.for.entity.or.instance.list?productId='+activityClarificationTabJsonObj.productId+'&entityTypeId=31&entityInstanceId='+row.data().clarificationTrackerId,
			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+activityClarificationTabJsonObj.productId+'&entityTypeId=31&entityInstanceId='+row.data().clarificationTrackerId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
			"updateURL": 'update.attachment.for.entity.or.instance',
			"deleteURL": 'delete.attachment.for.entity.or.instance',
			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=31',
			"multipleUpload":true,
		};	 
		Attachments.init(jsonObj);
	});
	
	$('#clarification_'+activityClarificationTabJsonObj.containerID+'_dataTable tbody').on('click', 'td .activityClarificationTabtImg2', function () {
		var tr = $(this).closest('tr');
    	var row = activityClarificationListDT_oTable.DataTable().row(tr);
		
    	var entityTypeIdComments = 52;
		var entityNameComments = "ClarificationTracker";
		listComments(entityTypeIdComments, entityNameComments, row.data().clarificationTrackerId, row.data().clarificationTitle, "productLevelclarificationTrackerComments");
	});
	
	$('#clarification_'+activityClarificationTabJsonObj.containerID+'_dataTable').on( 'change', 'input.editorActivityClarificationList-active', function () {
		editorActivityClarificationList
            .edit( $(this).closest('tr'), false )
            .set( 'readyState', $(this).prop( 'checked' ) ? 1 : 0 )
            .submit();
	});
	
	 editorActivityClarificationList.on( 'submit', function ( e, data, action ) {
			 reInitializeActivityClarificationListDT();
		 });
	
	$('#clarification_'+activityClarificationTabJsonObj.containerID+'_dataTable_length').css('margin-top','8px');
	$('#clarification_'+activityClarificationTabJsonObj.containerID+'_dataTable_length').css('padding-left','35px');		
	
	activityClarificationListDT_oTable.DataTable().columns().every( function () {
	    var that = this;
	    $('input', this.footer() ).on( 'keyup change', function () {
	        if ( that.search() !== this.value ) {
	            that
	            	.search( this.value, true, false )
	                .draw();
	        }
	    } );
	} );

}

var clearTimeoutActivityClarificationListDT='';
function reInitializeActivityClarificationListDT(){
	clearTimeoutActivityClarificationListDT = setTimeout(function(){				
		activityClarificationListDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutActivityClarificationListDT);
	},200);
}



