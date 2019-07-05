var updateDiv = '';
var sourceWorkflowIdMapped = 0;

function statusPolicies(productId, workflowId, entityTypeId, entityId, entityInstanceId, entityInstanceName, entityTypeName, statusId){
	if(typeof statusId == 'undefined' || statusId == null || statusId == -1){
		callAlert("Instance is already Auto Approved, cannot configure workflow");
		return;
	}
	getSourceWorkflowMapped(productId, entityTypeId, entityId, entityInstanceId);
	var jsonObj={
			"Title":"Workflow "+entityTypeName+" : ["+entityInstanceId+"] "+entityInstanceName,
			"url": 'workflow.status.policy.list?isActive=1&productId='+productId+'&workflowId='+workflowId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId+'&scopeType=Instance',            
	};
	SingleJtableContainer.init(jsonObj);
	clearSingleJTableDatas();
	
	listStatusAndPolicies(productId, workflowId, entityTypeId, entityId, entityInstanceId);
	
	$("#div_SingleJTableSummary .modal-header .row").css('display','block');       
	if(updateDiv!='')
		updateDiv.empty();

	updateDiv = $("#div_SingleJTableSummary .modal-header .row").find('div').eq(1);
	updateDiv.append('<div id="singleJtableUpdate_dd"><select class="form-control input-medium select2me" id="singleJtableUpdate_ul"></select></div>');   
	addUpdateDropDown(productId, entityTypeId, entityId);      
	updateDiv.append('<button type="button" class="btn green-haze" style="margin-left: 10px;" onclick="singleJtableUpdate('+productId+','+entityTypeId+','+entityId+','+entityInstanceId+');">Update</button>');  

}

function getSourceWorkflowMapped(productId, entityTypeId, entityId, entityInstanceId){
	sourceWorkflowIdMapped = 0;
	var sourceWorkflowMappedUrl = 'workflow.master.mapped.to.entity.instance?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId;
	$.post(sourceWorkflowMappedUrl, function(data) {
		if(data.Result == 'OK' || data.Result == 'Ok'){
			sourceWorkflowIdMapped = data.Record;
		}else{
			callAlert(data.Message);
		}
	});
}

function singleJtableUpdate(productId, entityTypeId, entityId, entityInstanceId, workflowIdValue){
	var workflowId = '';	
	if(workflowIdValue == undefined){	
		workflowId = $("#singleJtableUpdate_ul").find('option:selected').attr('id');
	}else{
		workflowId = workflowIdValue;
	}
	
	if(workflowId == sourceWorkflowIdMapped){
		callAlert("Selected workflow is currently mapped with instance, please select different workflow for update");
		return;
	}
	var workflowUpdateUrl = 'workflow.change.instance.workflow.mapping?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId+'&workflowId='+workflowId;
	
	$.post(workflowUpdateUrl, function(data) {
		callAlert(data.Message);
		if(data.Result == 'OK' || data.Result == 'Ok'){
			sourceWorkflowIdMapped = workflowId;
			listStatusAndPolicies(productId, 0, entityTypeId, entityId, entityInstanceId);
		}
	});
}

function addUpdateDropDown(productId, entityTypeId, entityId){    
	$.post('workflow.master.mapped.to.entity.list.options?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId,function(data) {
		var ary = (data.Options);
		$('#singleJtableUpdate_ul').empty();   
		$.each(ary, function(index, element) {
			var selected = "";
			if(element.Value == sourceWorkflowIdMapped){
				selected = "selected";
			}
			$('#singleJtableUpdate_ul').append('<option id="' + element.Value + '" '+selected+' ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
	});

	updateDiv.append($('#singleJtableUpdate_ul'));
}

function listStatusAndPolicies(productId, workflowId, entityTypeId, entityId, entityInstanceId){
	if(typeof entityId == 'undefined' || entityId == null){
		entityId = 0;
	}
	 var url = 'workflow.status.policy.list?isActive=1&productId='+productId+'&workflowId='+workflowId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId+'&scopeType=Instance';		
	workflowTemplateDataTable(url, "ParentTable", "");

}

var workflowTemplateDT_oTable='';
var editorWorkflowTemplate='';
var activityWorkflowOptionsArr=[];
var activityWorkflowOptionsResultArr=[];
var activityWorkflowOptionsItemCounter=0;
var wrkflwId;
var tableType;

function workflowTemplateDataTable(url, tableType, row){
	activityWorkflowOptionsItemCounter=0;
	activityWorkflowOptionsResultArr=[];
	activityWorkflowOptionsArr=[];
	if(tableType == "ParentTable"){
		activityWorkflowOptionsArr = [{id:"statusPolicyTypeList", url:'workflow.status.policy.SLA.Violation.action.option.list'},		              
		              {id:"workflowStatusType", url:'workflow.status.type.option.list'},
		              {id:"stautsTransitionPolicy", url:'workflow.status.transition.policy.option.list?actionScope=User'},
		              {id:"actionScope", url:'workflow.status.policy.action.mode.option.list'},
		              ];
		workflowTemplateOptions_Container(url, activityWorkflowOptionsArr, tableType, row);
		
	}else if(tableType == "ChildTable1"){	 
		 activityWorkflowOptionsArr = [{id:"statusPolicyUserList", url:'workflow.user.list.by.product.id.options?productId='+productId+'&isAddonUsersAllowed=true'}];
		 workflowTemplateOptions_Container(url, activityWorkflowOptionsArr, tableType, row);	 
		 $("#statusPolicyUserDT_Child_Container").modal();
	
	}else if(tableType == "ChildTable2"){		 
		 activityWorkflowOptionsArr = [{id:"statusPolicyRoleList", url:'administration.user.role.list?typeFilter=1'}];
		 workflowTemplateOptions_Container(url, activityWorkflowOptionsArr, tableType, row);
		 
		 $("#statusPolicyRoleDT_Child_Container").modal();
	}	
}
		
function workflowTemplateOptions_Container(url, urlArr, tableType, row){
	$.ajax( {
 	   "type": "POST",
        "url":  urlArr[activityWorkflowOptionsItemCounter].url,
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
				   activityWorkflowOptionsResultArr.push(json.Options);						 
	         }
	         activityWorkflowOptionsItemCounter++;	
			 if(activityWorkflowOptionsItemCounter<activityWorkflowOptionsArr.length){
				 workflowTemplateOptions_Container(url, activityWorkflowOptionsArr, tableType, row);
			 }else{
				if(tableType == "ParentTable"){	
					assignActivityWorkflowTemplateDT(url, tableType, row);
				
				}else if(tableType == "ChildTable1"){	
					var actionTypeObj = {"Options":[{"value":"Optional","label":"Optional","DisplayText":"Optional"},
							 {"value":"Mandatory","label":"Mandatory","DisplayText":"Mandatory"}
							]		
						};						
					activityWorkflowOptionsResultArr.push(actionTypeObj.Options);
		 
					assignActivityWorkflowTemplateDT(url, tableType, row)
				
				}else if(tableType == "ChildTable2"){		 
					assignActivityWorkflowTemplateDT(url, tableType, row)
				}
	
			 }					 
         },
         error: function (data) {
			activityWorkflowOptionsItemCounter++;
         },
         complete: function(data){
         	//console.log('Completed');
         },	            
   	});
}

function assignActivityWorkflowTemplateDT(url, tableType, row){
	openLoaderIcon();			
	 $.ajax({
		  type: "POST",
		  url: url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}					
			
			if(tableType == "ParentTable"){
				activityWorkflowTemplateStatusDT_Container(data);
			}else if(tableType == "ChildTable1"){
				statusPolicyUserDT_Container(data, row);
			}else if(tableType == "ChildTable2"){
				statusPolicyRoleDT_Container(data, row);
			}
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

// -- Role begin

var statusPolicyRoleDT_oTable='';
var editorStatusPolicyRole='';
function statusPolicyRoleDataTableHeader(){
	var childDivString ='<table id="statusPolicyRole_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Role</th>'+
			'<th></th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function statusPolicyRoleDT_Container(data, row){
	
	try{
		if ($("#dataTableContainerForStatusPolicyRole").children().length>0) {
			$("#dataTableContainerForStatusPolicyRole").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = statusPolicyRoleDataTableHeader(); 			 
	$("#dataTableContainerForStatusPolicyRole").append(childDivString);
	
	editorStatusPolicyRole = new $.fn.dataTable.Editor( {
	    "table": "#statusPolicyRole_dataTable",
		ajax: 'workflow.status.policy.actor.add?productId='+productId+'&entityTypeId='+row.data().entityTypeId+'&entityId='+row.data().entityId+'&entityInstanceId='+row.data().entityInstanceId+'&actorMappingType=User',
		ajaxUrl: 'workflow.status.policy.actor.update',
		idSrc:  "workflowStatusActorId",
		i18n: {
	        create: {
	            title:  "Create a new Status Policy Role",
	            submit: "Create",
	        }
	    },
		fields: [
		{
		    label: "workflowStatusId",
		    name: "workflowStatusId",            
		    "type":"hidden",
		    "def": row.data().workflowStatusId,
		},{
		    label: "workflowStatusActorId",
		    name: "workflowStatusActorId",            
		    "type":"hidden",		    
		},{
            label: "Role",
            name: "roleId",
            options: activityWorkflowOptionsResultArr[0],
            "type":"select",
        },{
            label: "Role",
            name: "roleName",
            "type":"hidden",
        },{
            label: "userActionStatus",
            name: "userActionStatus",            
            "type":"hidden",
            "def": 'Not Complete',
        }         
    ]
	});
	
	statusPolicyRoleDT_oTable = $("#statusPolicyRole_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     		  $('#statusPolicyRole_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeStatusPolicyRoleDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorStatusPolicyRole },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Status Policy Role',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Status Policy Role',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		//'colvis'
	         ], 
	         columnDefs: [
	         ],
        aaData:data,		    				 
	    aoColumns: [	        	                   
		   { mData: "roleId", className: 'disableEditInline', sWidth: '90%', editField: "roleId",
             	mRender: function (data, type, full) {
	 				if (full.action == "create" || full.action == "edit"){
	 					data = optionsValueHandler(editorStatusPolicyRole, 'roleId', full.roleId);			           		
		           	}else if(type == "display"){
		           		data = full.roleName;
		           	}	           	 
                   return data;
               },
       		},
			{ mData: null,				 
            	bSortable: false,
				sWidth: '10%',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
					'<button style="border: none; background-color: transparent; outline: none;">'+
						'<i class="fa fa-trash-o details-control workflowEntityUserDelete" title="Delete User worflow" onClick="deleteWordflowUserEntity('+data.workflowStatusActorId+')" style="margin-left: 5px;"></i></button>'+	
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
	
	// Activate an inline edit on click of a table cell
	$('#statusPolicyRole_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorStatusPolicyRole.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#statusPolicyRole_dataTable_length").css('margin-top','8px');
	$("#statusPolicyRole_dataTable_length").css('padding-left','35px');		
	
	statusPolicyRoleDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutStatusPolicyRoleDT='';
function reInitializeStatusPolicyRoleDT(){
	clearTimeoutStatusPolicyRoleDT = setTimeout(function(){				
		statusPolicyRoleDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutStatusPolicyRoleDT);
	},200);
}

// -- User begin
var statusPolicyUserDT_oTable='';
var editorStatusPolicyUser='';
function statusPolicyUserDataTableHeader(){
	var childDivString ='<table id="statusPolicyUser_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>User</th>'+
			'<th>Action Type</th>'+
			'<th></th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function statusPolicyUserDT_Container(data, row){
	
	try{
		if ($("#dataTableContainerForStatusPolicyUser").children().length>0) {
			$("#dataTableContainerForStatusPolicyUser").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = statusPolicyUserDataTableHeader(); 			 
	$("#dataTableContainerForStatusPolicyUser").append(childDivString);
	
	editorStatusPolicyUser = new $.fn.dataTable.Editor( {
	    "table": "#statusPolicyUser_dataTable",
		ajax: 'workflow.status.policy.actor.add?productId='+productId+'&entityTypeId='+row.data().entityTypeId+'&entityId='+row.data().entityId+'&entityInstanceId='+row.data().entityInstanceId+'&actorMappingType=User',
		ajaxUrl: 'workflow.status.policy.actor.update',
		idSrc:  "workflowStatusActorId",
		i18n: {
	        create: {
	            title:  "Create a new Status Policy User",
	            submit: "Create",
	        }
	    },
		fields: [
		{
		    label: "workflowStatusId",
		    name: "workflowStatusId",            
		    "type":"hidden",
		    "def": row.data().workflowStatusId,
		},         
		{
            label: "User",
            name: "userName",            
            "type":"hidden",
        },{
            label: "User",            
			name: "userId",
            options: activityWorkflowOptionsResultArr[0],
            "type":"select",
        },{
            label: "Action Type",
            name: "actionRequirement",
            options: activityWorkflowOptionsResultArr[1],
            "type":"select",
        },{
            label: "userActionStatus",
            name: "userActionStatus",            
            "type":"hidden",
            "def": 'Not Complete',
        }        
    ]
	});
	
	statusPolicyUserDT_oTable = $("#statusPolicyUser_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     		  $('#statusPolicyUser_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeStatusPolicyUserDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorStatusPolicyUser },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Status Policy User',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Status Policy User',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		//    'colvis'
	         ], 
	         columnDefs: [
	         ],
        aaData:data,		    				 
	    aoColumns: [	        	        	    
		    { mData: "userId", className: 'disableEditInline', sWidth: '50%', editField: "userId",
             	mRender: function (data, type, full) {
	 				if (full.action == "create" || full.action == "edit"){
	 					data = optionsValueHandler(editorStatusPolicyUser, 'userId', full.userId);			           		
		           	}else if(type == "display"){
		           		data = full.userName;
		           	}	           	 
                   return data;
               },
       		},			
           { mData: "actionRequirement", className: 'editable', sWidth: '40%' },
		   { mData: null,				 
            	bSortable: false,
				sWidth: '10%',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
					'<button style="border: none; background-color: transparent; outline: none;">'+
						'<i class="fa fa-trash-o details-control workflowEntityUserDelete" title="Delete User worflow" onClick="deleteWordflowUserEntity('+data.workflowStatusActorId+')" style="margin-left: 5px;"></i></button>'+	
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
	
	
	// Activate an inline edit on click of a table cell
	$('#statusPolicyUser_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorStatusPolicyUser.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#statusPolicyUser_dataTable_length").css('margin-top','8px');
	$("#statusPolicyUser_dataTable_length").css('padding-left','35px');		
	
	statusPolicyUserDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutStatusPolicyUserDT='';
function reInitializeStatusPolicyUserDT(){
	clearTimeoutStatusPolicyUserDT = setTimeout(function(){				
		statusPolicyUserDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutStatusPolicyUserDT);
	},200);
}

var activityWorkflowTemplateStatusDT_oTable='';
var editorActivityStatusWorkflow='';
var activityWorkflowTemplateContainerID='JtableSingleContainer';
var clearTimeoutActivityStatusWorkflowCDT='';
function reInitializeActivityStatusWorkflowDT(){
	clearTimeoutActivityStatusWorkflowCDT = setTimeout(function(){				
		activityWorkflowTemplateStatusDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutActivityStatusWorkflowCDT);
	},200);
}

function activityWorkflowTemplateStatusDTHeader(){
	var childDivString ='<table id="activityWorkflow_'+activityWorkflowTemplateContainerID+'_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Status Name</th>'+
			'<th>Status Display Name</th>'+
			'<th>Description</th>'+			
			'<th>Weightage</th>'+
			'<th>SLA Duration (Hrs)</th>'+
			'<th>SLA Violation Action</th>'+
			'<th>Status Order</th>'+
			'<th>Workflow Status Type</th>'+
			'<th>Status Transition Policy</th>'+
			'<th>Actor</th>'+			
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
function activityWorkflowTemplateStatusDT_Container(data){
	
	try{
		if($("#"+activityWorkflowTemplateContainerID).children().length>0) {
			$("#"+activityWorkflowTemplateContainerID).children().remove();
		}
	} 
	catch(e) {}
	
	var emptytr = emptyTableRowAppending(11);  // total coulmn count	
	var childDivString = activityWorkflowTemplateStatusDTHeader(); 			 
	$("#"+activityWorkflowTemplateContainerID).append(childDivString);
	
	$("#"+activityWorkflowTemplateContainerID+' tfoot tr').html('');     			  
	$("#"+activityWorkflowTemplateContainerID+' tfoot tr').append(emptytr);
		
	editorActivityStatusWorkflow = new $.fn.dataTable.Editor( {
	    "table": '#activityWorkflow_'+activityWorkflowTemplateContainerID+'_dataTable',
		ajax: 'workflow.status.policy.add?workflowId='+workflowId,
		ajaxUrl: 'workflow.status.policy.update?workflowId='+workflowId,
		idSrc:  "workflowStatusPolicyId",
		i18n: {
	        create: {
	            title:  "Create a new Workflow Template Status",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "workflowStatusPolicyId",
            name: "workflowStatusPolicyId",
			"type": "hidden",			
        },{
            label: "workflowStatusId",
            name: "workflowStatusId",
			"type": "hidden",
        },{
            label: "Status",
            name: "workflowStatusName",
        },{
            label: "Status Display",
            name: "workflowStatusDisplayName",
        },{
            label: "Description",
            name: "description",
        },{
            label: "statusCategoryId",
            name: "statusCategoryId",
			"type": "hidden",
			"def":1,
        },{
            label: "Weightage",
            name: "weightage",
        },{
            label: "SLA Duration(Hrs)",
            name: "slaDuration",
            "def": 1,
        },{
            label: "SLA Violation Action",
            name: "slaViolationAction",
            options: activityWorkflowOptionsResultArr[0],
            "type":"select",
        },{
            label: "Status Order",
            name: "statusOrder",
        },{
            label: "Workflow Status Type",
            name: "workflowStatusType",
            options: activityWorkflowOptionsResultArr[1],
            "type":"select",
        },{
            label: "Workflow Status Policy",
            name: "stautsTransitionPolicy",
            options: activityWorkflowOptionsResultArr[2],
            "type":"select",
        },{
            label: "Actor",
            name: "actionScope",
            options: activityWorkflowOptionsResultArr[3],
            "type":"select",
        },{
            label: "isLifeCycleStage",
            name: "isLifeCycleStage",
			"type": "hidden",
			"def":1,
        }       
    ]
	});
	
	activityWorkflowTemplateStatusDT_oTable = $('#activityWorkflow_'+activityWorkflowTemplateContainerID+'_dataTable').dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 	       
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [9,10]; // search column TextBox Invisible Column position
	     		  $('#activityWorkflow_'+activityWorkflowTemplateContainerID+'_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeActivityStatusWorkflowDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorActivityStatusWorkflow },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Workflow Template Status',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Workflow Template Status',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
						'colvis',		
				],	         
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "workflowStatusName", className: 'disableEditInline', sWidth: '10%' },	
           { mData: "workflowStatusDisplayName", className: 'editable', sWidth: '10%' },	
           { mData: "description", className: 'editable', sWidth: '15%' },	          
           { mData: "weightage",  className: 'editable', sWidth: '5%' },
           { mData: "slaDuration",  className: 'editable', sWidth: '5%' },
           { mData: "slaViolationAction",  className: 'editable', sWidth: '5%' },
           { mData: "statusOrder",  className: 'disableEditInline', sWidth: '5%' },           
		   { mData: "workflowStatusType", className: 'disableEditInline', sWidth: '10%',
	       		mRender: function (data, type, full) {
	       			 if (full.action == "create" || full.action == "edit"){
	       				data = optionsValueHandler(editorActivityStatusWorkflow, 'workflowStatusType', full.workflowStatusType);
		           	 }
		           	 else if(type == "display"){
		           		data = full.workflowStatusType;
		           	 }	
		             return data;
	            },
			},
			{ mData: "stautsTransitionPolicy", className: 'editable', sWidth: '10%', editField: "stautsTransitionPolicy",
	       		mRender: function (data, type, full) {
	       			 if (full.action == "create" || full.action == "edit"){
	       				data = optionsValueHandler(editorActivityStatusWorkflow, 'stautsTransitionPolicy', full.stautsTransitionPolicy);
		           	 }
		           	 else if(type == "display"){
		           		data = full.stautsTransitionPolicy;
		           	 }	
		             return data;
	            },
			},
			{ mData: "actionScope", className: 'editable', sWidth: '10%', editField: "actionScope",
	       		mRender: function (data, type, full) {
	       			 if (full.action == "create" || full.action == "edit"){
	       				data = optionsValueHandler(editorActivityStatusWorkflow, 'actionScope', full.actionScope);
		           	 }
		           	 else if(type == "display"){
		           		data = full.actionScope;
		           	 }	
		             return data;
	            },
			},
			{ mData: null,				 
            	bSortable: false,
				sWidth: '10%',
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
 	       				'<i class="fa fa-user activityWorkflowStatusMappingImg1" title="Users"></i></button>'+
					'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
 	       				'<i class="fa fa-users activityWorkflowStatusMappingImg2" title="Roles"></i></button>'+	
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
	
	 $(function(){ 
	 
		$('#activityWorkflow_'+activityWorkflowTemplateContainerID+'_dataTable_wrapper').find(".buttons-create").hide();
	 
		$('#activityWorkflow_'+activityWorkflowTemplateContainerID+'_dataTable_length').css('margin-top','8px');
		$('#activityWorkflow_'+activityWorkflowTemplateContainerID+'_dataTable_length').css('padding-left','35px');		
		
		activityWorkflowTemplateStatusDT_oTable.DataTable().columns().every( function () {
			var that = this;
			$('input', this.footer() ).on( 'keyup change', function () {
				if ( that.search() !== this.value ) {
					that
						.search( this.value, true, false )
						.draw();
				}
			} );
		} );
		
		$( 'input', editorActivityStatusWorkflow.node()).on( 'focus', function () {
			this.select();
		});
	 
			// Activate an inline edit on click of a table cell
			$('#activityWorkflow_'+activityWorkflowTemplateContainerID+'_dataTable').on( 'click', 'tbody td.editable', function (e) {
				editorActivityStatusWorkflow.inline( this, {
					submitOnBlur: true
				} );
			});
			
			editorActivityStatusWorkflow.dependent('actionScope',function ( val, data, callback ) {        	
				var entityId = 0;					       								
				if(val != undefined){			
					entityId = val;
					var url = 'workflow.status.transition.policy.option.list?actionScope='+entityId;
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
							editorActivityStatusWorkflow.set('stautsTransitionPolicy',json.Options);
							editorActivityStatusWorkflow.field('stautsTransitionPolicy').update(json.Options);
						}
					} );
				}
			});
			
			$('#activityWorkflow_'+activityWorkflowTemplateContainerID+'_dataTable tbody').on('click', 'td .activityWorkflowStatusMappingImg1', function () {
				var tr = $(this).closest('tr');
				var row = activityWorkflowTemplateStatusDT_oTable.DataTable().row(tr);
				
				var url= 'workflow.status.policy.actor.list?productId='+productId+'&workflowStatusId='+row.data().workflowStatusId+'&entityTypeId='+row.data().entityTypeId+'&entityId='+row.data().entityId+'&entityInstanceId='+row.data().entityInstanceId+'&actorType=User&jtStartIndex=0&jtPageSize=10000';				 
				 workflowTemplateDataTable(url, "ChildTable1", row);
				
			});
			
			$('#activityWorkflow_'+activityWorkflowTemplateContainerID+'_dataTable tbody').on('click', 'td .activityWorkflowStatusMappingImg2', function () {
				var tr = $(this).closest('tr');
				var row = activityWorkflowTemplateStatusDT_oTable.DataTable().row(tr);
				
				var url='workflow.status.policy.actor.list?productId='+productId+'&workflowStatusId='+row.data().workflowStatusId+'&entityTypeId='+row.data().entityTypeId+'&entityId='+row.data().entityId+'&entityInstanceId='+row.data().entityInstanceId+'&actorType=Role&jtStartIndex=0&jtPageSize=10000';	
				 workflowTemplateDataTable(url, "ChildTable2", row);
			}); 
	
	 });
}

/*
function listStatusAndPolicies(productId, workflowId, entityTypeId, entityId, entityInstanceId){
	if(typeof entityId == 'undefined' || entityId == null){
		entityId = 0;
	}
	
	$('#JtableSingleContainer').jtable({
		title: 'Status Policy',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},        
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: 'workflow.status.policy.list?isActive=1&productId='+productId+'&workflowId='+workflowId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId+'&scopeType=Instance',
            editinlineAction: 'workflow.status.policy.update?workflowId='+workflowId,
		}, 
		recordUpdated:function(event, data){
			$('#JtableSingleContainer').find('.jtable-child-table-container').jtable('reload');
		},
		recordAdded: function (event, data) {
			$('#JtableSingleContainer').find('.jtable-child-table-container').jtable('reload');
		},
		recordDeleted: function (event, data) {
			$('#JtableSingleContainer').find('.jtable-child-table-container').jtable('reload');
		},
		fields: {
			workflowStatusPolicyId: { 
				type: 'hidden', 
				edit: false,
			}, 
			workflowStatusName: { 
				title: 'Status Name',
				inputTitle: 'Status Name <font color="#efd125" size="4px">*</font>',
				width: "15%",
				list: true,
				edit : false,
				create: true,
			},
			workflowStatusDisplayName: { 
            	title: 'Status Display Name',
            	inputTitle: 'Status Name <font color="#efd125" size="4px">*</font>',
                width: "15%",
                list: true,
                edit : true,
                create: true,
   			},
			description: { 
				title: 'Description',
				width: "15%",
				list: true,
				edit : true,
				create: true,
			},
			statusCategoryId: { 
				title : 'Status Category',
				inputTitle: 'Status Category <font color="#efd125" size="4px">*</font>',
				create:false,
				list : false,
				edit : false,
				defaultValue : 1,
				options:function(data){
					return 'status.category.option.list';
				},
			},
			weightage: {
				title: 'Weightage',
				inputTitle:'Weightage',
				width: "10%",
				list: true,
				edit : true,
				create: true,
			},
			slaDuration : {
				title: 'SLA Duration(Hrs)',
				create : true,
				edit : true,
				list: true,
			},
			slaViolationAction : {
				title : 'SLA Violation Action',
				width : "20%",
				create : true,
				edit : true,
				list: true,
			},
			statusOrder: {
   				title: 'Status Order',
   				inputTitle:'Status Order',
                width: "15%",
                list: true,
               edit : false,
               create: true,
  		  	},
	  		workflowStatusType : {
	    		 title : 'Workflow Status Type',
	    		 width : "10%",
	  			 inputTitle: 'Workflow Status Type',
	              create : true,
	              edit : false,
	              list: true,
	              options:function(data){
				 	return 'workflow.status.type.option.list';
	     		},
			}, 
			stautsTransitionPolicy : {
	    		 title : 'Status Transition Policy',
	    		 width : "10%",
	    		 inputTitle: 'Status Transition Policy',
	             create : true,
	             edit : true,
	             list: true,
	             dependsOn:'actionScope',
	             options:function(data){
				 	return 'workflow.status.transition.policy.option.list?actionScope='+data.dependedValues.actionScope;
	     		},
			},
			actionScope: {
				title: 'Actor',
				inputTitle: 'Actor',
				create : true,
				edit : true,
				list: true,
				options:function(data){
					return 'workflow.status.policy.action.mode.option.list';
				},
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
	        			$imgUser = listUsers($imgUser, productId, data.record.workflowStatusId, entityTypeId, entityId, entityInstanceId, '#JtableSingleContainer');
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
	        			$imgRole = listRoles($imgRole, productId, data.record.workflowStatusId, entityTypeId, entityId, entityInstanceId, '#JtableSingleContainer');
           		  });
           			return $imgRole;
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
	$('#JtableSingleContainer').jtable('load'); 
	//return $imgStatusPolicy; 
}

*/

function listUsers($imgUser, productId, workflowStatusId, entityTypeId, entityId, entityInstanceId, containerId){
	
	if(typeof entityId == 'undefined' || entityId == null){
		entityId = 0;
	}
	$(containerId).jtable('openChildTable', $imgUser.closest('tr'), 
	{
		title: 'Status Policy User',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:false},        
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: 'workflow.status.policy.actor.list?productId='+productId+'&workflowStatusId='+workflowStatusId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId+'&actorType=user',
            createAction: 'workflow.status.policy.actor.add?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId+'&actorMappingType=User',
            deleteAction: 'workflow.status.policy.actor.delete'
		}, 
		recordUpdated:function(event, data){
			$(containerId).find('.jtable-child-table-container').jtable('reload');
		},
		recordAdded: function (event, data) {
			$(containerId).find('.jtable-child-table-container').jtable('reload');
		},
		recordDeleted: function (event, data) {
			$(containerId).find('.jtable-child-table-container').jtable('reload');
		},
		fields: {
			workflowStatusId:{
	          	 type : 'hidden',
	           	 defaultValue: workflowStatusId,
	        },
	        workflowStatusActorId:{
	        	list:false,
	           	edit:false,
	           	create:false,
	        	key: true,
	        },
			userId: { 
				title: 'User',
				edit: false,
				create: true,
				list: true,
				options : function(data) {
					return 'workflow.user.list.by.product.id.options?productId='+productId+'&isAddonUsersAllowed=false';
				}
			}, 
			actionRequirement: { 
				title : 'Action Type',
				inputTitle: 'Action Type <font color="#efd125" size="4px"></font>',
				create:true,
				list : true,
				edit : false,
				defaultValue : 'Optional',
				options: {'Optional' : 'Optional', 'Mandatory' : 'Mandatory'}
			},
			userActionStatus: { 
				title : 'Action Status',
				create:false,
				list : false,
				edit : false,
				options: {'Not Complete' : 'Not Complete', 'Completed' : 'Completed'},
				defaultValue : 'Not Complete'
			}
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
    	data.childTable.jtable('load'); 
	});

	return $imgUser; 
}

function listRoles($imgRole, productId, workflowStatusId, entityTypeId, entityId, entityInstanceId, containerId){
	
	if(typeof entityId == 'undefined' || entityId == null){
		entityId = 0;
	}
	$(containerId).jtable('openChildTable', $imgRole.closest('tr'), 
	{
		title: 'Status Policy Role',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:false},        
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: 'workflow.status.policy.actor.list?productId='+productId+'&workflowStatusId='+workflowStatusId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId+'&actorType=role',
            createAction: 'workflow.status.policy.actor.add?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId='+entityInstanceId+'&actorMappingType=Role',
            deleteAction: 'workflow.status.policy.actor.delete'
		}, 
		recordUpdated:function(event, data){
			$(containerId).find('.jtable-child-table-container').jtable('reload');
		},
		recordAdded: function (event, data) {
			$(containerId).find('.jtable-child-table-container').jtable('reload');
		},
		recordDeleted: function (event, data) {
			$(containerId).find('.jtable-child-table-container').jtable('reload');
		},
		fields: {
			workflowStatusId:{
	          	 type : 'hidden',
	           	 defaultValue: workflowStatusId,
	        },
	        workflowStatusActorId:{
	        	list:false,
	           	edit:false,
	           	create:false,
	        	key: true,
	        },
			roleId: { 
				title: 'Role',
				edit: false,
				create: true,
				list: true,
				options : function(data) {
					return 'administration.user.role.list?typeFilter=1';
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
    	data.childTable.jtable('load'); 
	});

	return $imgRole; 
}
/*
function listWorkflowStatusAndPoliciesPlan(productId, entityTypeIdSP, entityIdSP, entityInstanceIdSP, jTableContainerId){
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
						listComments(entityTypeIdComments, entityNameComments, data.record.workflowStatusPolicyId, data.record.workflowStatusName, "wflowComments");
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