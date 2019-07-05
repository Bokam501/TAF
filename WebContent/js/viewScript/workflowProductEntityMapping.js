var testFactoryId = 0;
var productId = "";
var isFirstLoad = true;
var isAddonUsersAllowed = false;

jQuery(document).ready(function() {	
	
	QuickSidebar.init(); // init quick sidebar
	setBreadCrumb(userRole);
	createHiddenFieldsForTree();
	setPageTitle("Workflow for Entity");
	$('#treeStructure-portlet-light .portlet-title .tools').css('margin-right','12px');
	getTreeData('workflow.my.actions.product.tree');
	setDefaultnode("j1_1");
		
	$("#treeContainerDiv").on("select_node.jstree",
	     function(evt, data){
			if(data.node != undefined){
	   			var entityIdAndType =  data.node.data;
	   			var arry = entityIdAndType.split("~");
	   			var key = arry[0];
	   			var type = arry[1];
	   			//var name = arry[2];
	   			//var title = data.node.text;
				//var date = new Date();
			    //var timestamp = date.getTime();
			    var nodeType = type;
			    var parent = data.node.original.parent;
			    var loMainSelected = data;
		        uiGetParents(loMainSelected);
		        
		        if(nodeType == 'Product' || nodeType == 'product'){
		        	productId = key;
		        	testFactoryId = data.node.original.parent;
		        }else{
		        	testFactoryId = key;
		        	productId = 0;
		        }
		        //listWorkflowEntityMappings();
		        entityWorkflowMappingDataTable("ParentTable");
				if(parent == "#"){
					setDefaultnode(data.node.id);
				}
			}
		}
	);
	
});

//BEGIN: ConvertDataTable - EntityWorkflowMapping
var entityWorkflowMappingDT_oTable='';
var editorEntityWorkflowMapping='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;
var wrkflwId;
var entityId;
var entityTypeId;
var wrkflwStatusId;
var tableType;
var workflowType = 'Status Workflow';
var actionTypeObj={};
var actionTypeArr=[];

function entityWorkflowMappingDataTable(tableType){
	optionsItemCounter=0;
	optionsResultArr=[];
	if(tableType == "ParentTable"){
		optionsArr = [{id:"entityWorkflowMappingEntityTypeList", url:'worflow.capable.entity.type.list'},
		              {id:"entityWorkflowMappingEntityForTypeList", url:'entity.for.type.list?engagementId='+testFactoryId+'&productId='+productId+'&entityTypeId=33'},
		              {id:"entityWorkflowMasterList", url:'workflow.master.list.options?workflowType='+workflowType}
		              ];
	}else if(tableType == "ChildTable1"){
		optionsArr = [{id:"statusPolicyTypeList", url:'workflow.status.policy.SLA.Violation.action.option.list'},
		              {id:"statusTypeList", url:'workflow.status.type.option.list'},
		              {id:"statusTransition", url:'workflow.status.transition.policy.option.list?actionScope=User'},
		              {id:"statusPolicyMode", url:'workflow.status.policy.action.mode.option.list'},
		              ];
	}else if(tableType == "ChildTable2"){
		optionsArr = [{id:"statusPolicyUserList", url:'workflow.user.list.by.product.id.options?productId='+productId+'&isAddonUsersAllowed=true'}];
	}else if(tableType == "ChildTable3"){
		optionsArr = [{id:"statusPolicyRoleList", url:'administration.user.role.list?typeFilter=1'}];
	}
	entityWorkflowMappingOptions_Container(optionsArr, tableType);
}
		
function entityWorkflowMappingOptions_Container(urlArr, tableType){
	$.ajax( {
 	   "type": "POST",
        "url":  urlArr[optionsItemCounter].url,
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
				   optionsResultArr.push(json.Options);						 
	         }
	         optionsItemCounter++;	
			 if(optionsItemCounter<optionsArr.length){
				 entityWorkflowMappingOptions_Container(optionsArr,tableType);
			 }else{
				entityWorkflowMappingDataTableInit(tableType);	
			 }					 
         },
         error: function (data) {
			optionsItemCounter++;
         },
         complete: function(data){
         	//console.log('Completed');
         },	            
   	});
}

function entityWorkflowMappingDataTableInit(tableType){
	var url,jsonObj={};
	if(entityId == null) {
		entityId=0;
	}
	if(tableType=="ParentTable"){
		 url= 'workflow.product.entity.mapping.list?productId='+productId+'&jtStartIndex=0&jtPageSize=10';
		 jsonObj={"Title":" Entity Workflow Mapping",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Entity Workflow Mapping",
		};
	}else if(tableType=="ChildTable1"){
		 url= 'workflow.status.policy.list?isActive=1&productId='+productId+'&workflowId='+wrkflwId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&scopeType=Entity&jtStartIndex=0&jtPageSize=10';	
		 jsonObj={"Title":"Status Policy",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Status Policy",
		};
	}else if(tableType=="ChildTable2"){
		 url= 'workflow.status.policy.actor.list?productId='+productId+'&workflowStatusId='+wrkflwStatusId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&actorType=User&jtStartIndex=0&jtPageSize=10';
		 jsonObj={"Title":"Status Policy User",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Status Policy User",
		 };
		 actionTypeObj = {"Options":[{"value":"Optional","label":"Optional","DisplayText":"Optional"},
			                         {"value":"Mandatory","label":"Mandatory","DisplayText":"Mandatory"}
									]		
						};						
		 actionTypeArr.push(actionTypeObj.Options);
	}else if(tableType=="ChildTable3"){
		 url= 'workflow.status.policy.actor.list?productId='+productId+'&workflowStatusId='+wrkflwStatusId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&actorType=Role&jtStartIndex=0&jtPageSize=10';	
		 jsonObj={"Title":"Status Policy",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Status Policy Role",
		};
	}
	entityWorkflowMappingDataTableContainer.init(jsonObj);
}

var entityWorkflowMappingDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		if(jsonObj.componentUsageTitle == "Entity Workflow Mapping"){
 			assignEntityWorkflowMappingDataTableValues(jsonObj, "ParentTable");
 		}else if(jsonObj.componentUsageTitle == "Status Policy"){
 			assignEntityWorkflowMappingDataTableValues(jsonObj, "ChildTable1");
 		}else if(jsonObj.componentUsageTitle == "Status Policy User"){
 			assignEntityWorkflowMappingDataTableValues(jsonObj, "ChildTable2");
 		}else if(jsonObj.componentUsageTitle == "Status Policy Role"){
 			assignEntityWorkflowMappingDataTableValues(jsonObj, "ChildTable3");
 		}
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignEntityWorkflowMappingDataTableValues(jsonObj, tableType){
	openLoaderIcon();			
	 $.ajax({
		  type: "POST",
		  url: jsonObj.url,
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
			if(tableType == "ParentTable"){
				entityWorkflowMappingDT_Container(jsonObj);
			}else if(tableType == "ChildTable1"){
				statusPolicyDT_Container(jsonObj);
			}else if(tableType == "ChildTable2"){
				statusPolicyUserDT_Container(jsonObj);
			}else if(tableType == "ChildTable3"){
				statusPolicyRoleDT_Container(jsonObj);
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

function entityWorkflowMappingDataTableHeader(){
	var childDivString ='<table id="entityWorkflowMapping_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Id</th>'+
			'<th>Entity Type</th>'+
			'<th>Entity</th>'+
			'<th>Workflow Template</th>'+
			'<th>Is Default</th>'+
			'<th>Status Policy</th>'+
			'<th>Comments</th>'+
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
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function entityWorkflowMappingDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForEntityWorkflowMapping").children().length>0) {
			$("#dataTableContainerForEntityWorkflowMapping").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = entityWorkflowMappingDataTableHeader(); 			 
	$("#dataTableContainerForEntityWorkflowMapping").append(childDivString);
	
	editorEntityWorkflowMapping = new $.fn.dataTable.Editor( {
	    "table": "#entityWorkflowMapping_dataTable",
		ajax: "workflow.product.entity.mapping.add",
		ajaxUrl: "workflow.product.entity.mapping.update",
		idSrc:  "workflowEntityMappingId",
		i18n: {
	        create: {
	            title:  "Create a new Entity Workflow Mapping",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "Id",
            name: "workflowEntityMappingId",
            "type":"hidden"
         },{
             label: "productId",
             name: "productId",
             "type":"hidden",
             "def": productId,
         },{
             label: "Entity Type",
             name: "entityTypeId",
             options: optionsResultArr[0],
             "type":"select"
          },{
            label: "Entity",
            name: "entityId",
            options: optionsResultArr[1],
            "type":"select",
            "def":0,	
        },{
            label: "Workflow Template",
            name: "workflowId",
            options: optionsResultArr[2],
            "type": "select"
        },{
            label: "Is Default",
            name: "isDefault", 
            "type": "hidden",
        }        
    ]
	});
	
	entityWorkflowMappingDT_oTable = $("#entityWorkflowMapping_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "90%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [4,5,6]; // search column TextBox Invisible Column position
	     		  $('#entityWorkflowMapping_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeEntityWorkflowMappingDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorEntityWorkflowMapping },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Entity Workflow Mapping',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Entity Workflow Mapping',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
						{
						    text: 'Status Policy for User and Role',
						    action: function ( e, dt, node, config ) {
						    	$("#trigUploadStatusPolicy").trigger("click");
						    }
						},
						{
						    text: '<i class="fa fa-download" title="Download Template - Workflow Status Policy for User & Role" aria-hidden="true"></i>',
						    action: function ( e, dt, node, config ) {
						    	downloadEntityWorkflowMappingStatusPolicyForUserandRole();
						    }
						},
			    		//    'colvis'
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "workflowEntityMappingId", className: 'disableEditInline', sWidth: '10%' },	
           { mData: "entityTypeId", className: 'editable', sWidth: '15%', editField: "entityTypeId",
             	mRender: function (data, type, full) {
	 				if (full.action == "create" || full.action == "edit"){
	 					data = optionsValueHandler(editorEntityWorkflowMapping, 'entityTypeId', full.entityTypeId);			           		
		           	}
		           	else if(type == "display"){
		           		data = full.entityTypeName;
		           	}	           	 
                   return data;
               },
       		},
            { mData: "entityId", className: 'editable', sWidth: '15%', editField: "entityId",
             	mRender: function (data, type, full) {
	 				data = optionsValueHandler(editorEntityWorkflowMapping, 'entityId', full.entityId);
                   return data;
               },
       		},
       		{ mData: "workflowId", className: 'editable', sWidth: '15%', editField: "workflowId",
             	mRender: function (data, type, full) {
	 				data = optionsValueHandler(editorEntityWorkflowMapping, 'workflowId', full.workflowId);
                   return data;
               },
       		},
           { mData: null,
              mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editorEntityWorkflowMapping-active">';
                    }
                    return data;
                },
                className: "dt-body-center"
            },
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
 	       				'<img src="css/images/list_metro.png" class="statusPolicyImg" title="Status Policy" />'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<i class="fa fa-comments commentsImg" title="Comments"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
       ],       
       rowCallback: function ( row, data ) {
    	   $('input.editorEntityWorkflowMapping-active', row).prop( 'checked', data.isDefault == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	// ------
	 $(function(){ // this will be called when the DOM is ready
	 
	// Activate an inline edit on click of a table cell
	$('#entityWorkflowMapping_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorEntityWorkflowMapping.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#entityWorkflowMapping_dataTable tbody').on('click', 'td .statusPolicyImg', function () {
		var tr = $(this).closest('tr');
    	var row = entityWorkflowMappingDT_oTable.DataTable().row(tr);
		wrkflwId = row.data().workflowId;
		entityId = row.data().entityId;
		entityTypeId = row.data().entityTypeId;
		$('#statusPolicyDT_Child_Container').modal();
		$(document).off('focusin.modal');
    	entityWorkflowMappingDataTable("ChildTable1");
	});
	
	$('#entityWorkflowMapping_dataTable tbody').on('click', 'td .commentsImg', function () {
		var tr = $(this).closest('tr');
    	var row = entityWorkflowMappingDT_oTable.DataTable().row(tr);
		var entityTypeIdComments = 70;
		var entityNameComments = "WorkflowMasterEntityMapping";
		listComments(entityTypeIdComments, entityNameComments, row.data().workflowEntityMappingId, row.data().entityTypeId, "workFlowEntMapComments");

	});

	$('#entityWorkflowMapping_dataTable').on( 'change', 'input.editorEntityWorkflowMapping-active', function () {
		editorEntityWorkflowMapping
            .edit( $(this).closest('tr'), false )
            .set( 'isDefault', $(this).prop( 'checked' ) ? 1 : 0 )
            .submit();
	});
	
	$("#entityWorkflowMapping_dataTable_length").css('margin-top','8px');
	$("#entityWorkflowMapping_dataTable_length").css('padding-left','35px');
	$("#entityWorkflowMapping_dataTable_filter").css('margin-right','5px');
	
	
	entityWorkflowMappingDT_oTable.DataTable().columns().every( function () {
	    var that = this;
	    $('input', this.footer() ).on( 'keyup change', function () {
	        if ( that.search() !== this.value ) {
	            that
	            	.search( this.value, true, false )
	                .draw();
	        }
	    } );
	} );
	
	// ----- dependent Values -----
        
        editorEntityWorkflowMapping.dependent('entityTypeId',function ( val, data, callback ) {						
			var workflowType = "Status Workflow";
			 
			if(val != undefined){				
				var entityTypeId = val;
				
				// -- workflow ----
				if(entityTypeId == 34){
					workflowType = "Life Cycle Workflow";
				}			
										
				var url = 'workflow.master.list.options?workflowType='+workflowType;
				$.ajax( {
					url: url,
					type: "POST",
					dataType: 'json',
					success: function ( json ) {			    	        	
						
						for(var i=0;i<json.Options.length;i++){
							json.Options[i].label=json.Options[i].DisplayText;
							json.Options[i].value=json.Options[i].Value;
						}				
						
						if(editorEntityWorkflowMapping.s.action == "create"){
							editorEntityWorkflowMapping.set('workflowId',json.Options);
							editorEntityWorkflowMapping.field('workflowId').update(json.Options);
						}
					}
				} );
				
				// ----- ended -----
				
				if(entityTypeId == 30 || entityTypeId == 33){			
					
					var url = 'entity.for.type.list?engagementId='+testFactoryId+'&productId='+productId+'&entityTypeId='+entityTypeId;
						$.ajax( {
							url: url,
							type: "POST",
							dataType: 'json',
							success: function ( json ) {			    	        	
								
								for(var i=0;i<json.Options.length;i++){
									json.Options[i].label=json.Options[i].DisplayText;
									json.Options[i].value=json.Options[i].Value;
								}
								
								if(editorEntityWorkflowMapping.s.action == "create"){
									editorEntityWorkflowMapping.set('entityId',json.Options);
									editorEntityWorkflowMapping.field('entityId').update(json.Options);
								}
							}
						} );
				
				 }else{
					 var result = {'0' : '--'};
					 
					 if(editorEntityWorkflowMapping.s.action == "create"){
						editorEntityWorkflowMapping.set('entityId',result);
						editorEntityWorkflowMapping.field('entityId').update(result);
					}
				 }
			}				
        });
	        
	    // ----- ended -----		
	
	});

}

var clearTimeoutEntityWorkflowMappingDT='';
function reInitializeEntityWorkflowMappingDT(){
	clearTimeoutEntityWorkflowMappingDT = setTimeout(function(){				
		entityWorkflowMappingDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutEntityWorkflowMappingDT);
	},200);
}

function fullScreenHandlerDTEntityWorkflowMapping(){
	
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
		$('#testFacMode').css('max-height',displaytestFaceModeResponsive(window.innerWidth));
		
		entityWorkflowMappingDTFullScreenHandler(true);
		reInitializeEntityWorkflowMappingDT();
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
		$('#testFacMode').css('max-height','');
		
		reInitializeEntityWorkflowMappingDT();				
		entityWorkflowMappingDTFullScreenHandler(false);			
	}
}

function entityWorkflowMappingDTFullScreenHandler(flag){
	if(flag){
		reInitializeEntityWorkflowMappingDT();
		$("#entityWorkflowMapping_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
	}else{
		reInitializeEntityWorkflowMappingDT();
		$("#entityWorkflowMapping_dataTable_wrapper .dataTables_scrollBody").css('max-height','450px');
	}
}

function displaytestFaceModeResponsive(widthValue){
	var resultWidth="";
	if(widthValue<768){
		resultWidth = 200;			
	}else if(widthValue<992){
		resultWidth = 300;
	}else if(widthValue<1200){
		resultWidth = 400;
	}else if(widthValue<1500){
		resultWidth = 500;			
	}else if(widthValue<1600){
		resultWidth = 600;
	}else if(widthValue<1800){
		resultWidth = 700;
	}else if(widthValue<2050){
		resultWidth = 750;
	}else if(widthValue<2400){
		resultWidth = 850;
	}else if(widthValue<3000){
		resultWidth = 1100;
	}else if(widthValue<4000){
		resultWidth = 1300;
	}else if(widthValue<5000){
		resultWidth = 1500;
	}
	
	return resultWidth+'px';
}
//END: ConvertDataTable - EntityWorkflowMapping

//BEGIN: ConvertDataTable - StatusPolicy - ChildTable1
var statusPolicyDT_oTable='';
var editorStatusPolicy='';
function statusPolicyDataTableHeader(){
	var childDivString ='<table id="statusPolicy_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
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
			'<th>User</th>'+
			'<th>Role</th>'+
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
function statusPolicyDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForStatusPolicy").children().length>0) {
			$("#dataTableContainerForStatusPolicy").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = statusPolicyDataTableHeader(); 			 
	$("#dataTableContainerForStatusPolicy").append(childDivString);
	
	editorStatusPolicy = new $.fn.dataTable.Editor( {
	    "table": "#statusPolicy_dataTable",
		//ajax: 'workflow.status.policy.add?workflowId='+wrkflwId,
		ajaxUrl: 'workflow.status.policy.update?workflowId='+wrkflwId,
		idSrc:  "workflowStatusId",
		i18n: {
	        create: {
	            title:  "Create a new Workflow Template Status",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "Status Name",
            name: "workflowStatusName",
        },{
            label: "Status Display Name",
            name: "workflowStatusDisplayName",
        },{
            label: "Description",
            name: "description",
        },{
            label: "Weightage",
            name: "weightage",
        },{
            label: "SLA Duration(Hrs)",
            name: "slaDuration",
        },{
            label: "SLA Violation Action",
            name: "slaViolationAction",
            options: optionsResultArr[0],
            "type":"select",
        },{
            label: "Status Order",
            name: "statusOrder",
        },{
            label: "Workflow Status Type",
            name: "workflowStatusType",
            options: optionsResultArr[1],
            "type":"select",
        },{
            label: "Workflow Status Policy",
            name: "stautsTransitionPolicy",
            options: optionsResultArr[2],
            "type":"select",
        },{
            label: "Actor",
            name: "actionScope",
            options: optionsResultArr[3],
            "type":"select",
        }        
    ]
	});
	
	statusPolicyDT_oTable = $("#statusPolicy_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "90%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [7,8,9,10,11]; // search column TextBox Invisible Column position
	     		  $('#statusPolicy_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeStatusPolicyDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Status Policy',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Status Policy',
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
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "workflowStatusName", className: 'disableEditInline', sWidth: '15%' },	
           { mData: "workflowStatusDisplayName", className: 'editable', sWidth: '15%' },	
           { mData: "description", className: 'editable', sWidth: '12%' },	
           { mData: "weightage",  className: 'editable', sWidth: '12%' },
           { mData: "slaDuration",  className: 'editable', sWidth: '12%' },
           { mData: "slaViolationAction",  className: 'editable', sWidth: '12%' },
           { mData: "statusOrder",  className: 'disableEditInline', sWidth: '12%' },
           { mData: "workflowStatusType",  className: 'editable', sWidth: '12%' },
           { mData: "stautsTransitionPolicy",  className: 'editable', sWidth: '12%' },
			{ mData: "actionScope", className: 'editable', sWidth: '10%', editField: "actionScope",
	       		mRender: function (data, type, full) {
	       			 if (full.action == "create" || full.action == "edit"){
	       				data = optionsValueHandler(editorStatusPolicy, 'actionScope', full.actionScope);
		           	 }
		           	 else if(type == "display"){
		           		data = full.actionScope;
		           	 }	
		             return data;
	            },
			},
			{ mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
 	       				'<i class="fa fa-user usersImg" title="Users"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
 	       				'<i class="fa fa-users rolesImg" title="Roles"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorStatusPolicy-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	// Activate an inline edit on click of a table cell
	$('#statusPolicy_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorStatusPolicy.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#statusPolicy_dataTable tbody').on('click', 'td .usersImg', function () {
		var tr = $(this).closest('tr');
    	var row = statusPolicyDT_oTable.DataTable().row(tr);
    	wrkflwStatusId = row.data().workflowStatusId;
    	entityTypeId = row.data().entityTypeId;
    	entityId = row.data().entityId;
    	$('#statusPolicyUserDT_Child_Container').modal();
		$(document).off('focusin.modal');
    	entityWorkflowMappingDataTable("ChildTable2");
	});
	
	$('#statusPolicy_dataTable tbody').on('click', 'td .rolesImg', function () {
		var tr = $(this).closest('tr');
    	var row = statusPolicyDT_oTable.DataTable().row(tr);
    	wrkflwStatusId = row.data().workflowStatusId;
    	entityTypeId = row.data().entityTypeId;
    	entityId = row.data().entityId;
    	$('#statusPolicyRoleDT_Child_Container').modal();
		$(document).off('focusin.modal');
		entityWorkflowMappingDataTable("ChildTable3");
	});
	
	$("#statusPolicy_dataTable_length").css('margin-top','8px');
	$("#statusPolicy_dataTable_length").css('padding-left','35px');		
	
	statusPolicyDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutStatusPolicyDT='';
function reInitializeStatusPolicyDT(){
	clearTimeoutStatusPolicyDT = setTimeout(function(){				
		statusPolicyDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutStatusPolicyDT);
	},200);
}
//END: ConvertDataTable - StatusPolicy - ChildTable1

//BEGIN: ConvertDataTable - StatusPolicyUser - ChildTable2
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
function statusPolicyUserDT_Container(jsonObj){
	
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
		ajax: 'workflow.status.policy.actor.add?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&actorMappingType=User',
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
		    "def": wrkflwStatusId,
		},         
		{
            label: "User",
            name: "userName",            
            "type":"hidden",
        },{
            label: "User",            
			name: "userId",
            options: optionsResultArr[0],
            "type":"select",
        },{
            label: "Action Type",
            name: "actionRequirement",
            options: actionTypeArr[0],
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
	       //"aaSorting": [[4,'desc']],
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
        aaData:jsonObj.data,		    				 
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
       rowCallback: function ( row, data ) {
    	   //$('input.editorStatusPolicyUser-active', row).prop( 'checked', data.status == 1 );
       },
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
//END: ConvertDataTable - StatusPolicyUser - ChildTable2

// ----- Deletion Activity Items Started -----

function deleteWordflowUserEntity(workflowStatusActorId){
	var fd = new FormData();
	fd.append("workflowStatusActorId", workflowStatusActorId);
	
	$("#activityTabLoaderIcon").show();
	$.ajax({
		url : 'workflow.status.policy.actor.delete',
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
			//Reload/Refresh Actors Table after delete.			
			reloadStatusPolicyUserDT();
			$("#activityTabLoaderIcon").hide();
		}
	});
}

function reloadStatusPolicyUserDT(){
	url= 'workflow.status.policy.actor.list?productId='+productId+'&workflowStatusId='+wrkflwStatusId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&actorType=User&jtStartIndex=0&jtPageSize=10';
	jsonObj={"Title":"Status Policy User",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"Status Policy User",
	};
	actionTypeObj = {"Options":[{"value":"Optional","label":"Optional","DisplayText":"Optional"},
	                         {"value":"Mandatory","label":"Mandatory","DisplayText":"Mandatory"}
							]		
					};						
	actionTypeArr.push(actionTypeObj.Options);
	openLoaderIcon();			
	 $.ajax({
		  type: "POST",
		  url: jsonObj.url,
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
			
				statusPolicyUserDT_Container(jsonObj);
			
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});
}
//BEGIN: ConvertDataTable - StatusPolicyRole - ChildTable3
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
function statusPolicyRoleDT_Container(jsonObj){
	
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
		ajax: 'workflow.status.policy.actor.add?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&actorMappingType=User',
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
		    "def": wrkflwStatusId,
		},{
		    label: "workflowStatusActorId",
		    name: "workflowStatusActorId",            
		    "type":"hidden",		    
		},{
            label: "Role",
            name: "roleId",
            options: optionsResultArr[0],
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
	       //"aaSorting": [[4,'desc']],
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
			    		//    'colvis'
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           //{ mData: "roleName", className: 'editable', sWidth: '50%' },	
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
       rowCallback: function ( row, data ) {
    	   //$('input.editorStatusPolicyRole-active', row).prop( 'checked', data.status == 1 );
       },
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
//END: ConvertDataTable - StatusPolicyRole - ChildTable3

/*function listWorkflowEntityMappings(){
	
	try{
		if ($('#jTableContainerWorkflowEntityMapping').length>0) {
			$('#jTableContainerWorkflowEntityMapping').jtable('destroy'); 
		}
	}catch(e){}
	
	 //init jTable
	 $('#jTableContainerWorkflowEntityMapping').jtable({
         title: 'Entity Workflow Mapping',
         selecting: true, //Enable selecting 
         paging: true, //Enable paging
         pageSize: 10, //Set page size (default: 10)
         editinline:{enable:true},	         
         //sorting: true, //Enable sortin
         recordsLoaded: function(event, data) {
        	 $(".jtable-edit-command-button").prop("disabled", true);
         },
         recordUpdated:function(event, data){
 			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
 		 },
 		 recordAdded: function (event, data) {
 			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
 			listWorkflowEntityMappings();
 		 },
 		 recordDeleted: function (event, data) {
 			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
 		 },
         actions: {
             listAction: 'workflow.product.entity.mapping.list?productId='+productId,
             createAction: 'workflow.product.entity.mapping.add',
             editinlineAction: 'workflow.product.entity.mapping.update'
         },
         fields: {
        	 workflowEntityMappingId:{
	             title: 'Id',
	          	 list:true,
	           	 edit:false,
	           	 create:false,
	           	 key: true,
	             width:"10%"
	        },
	        productId: { 
				type: 'hidden',  
				list:false,
				defaultValue: productId
			}, 
	        entityTypeId:{
	        	 title: 'Entity Type',
	          	 list:true,
	           	 edit:false,
	           	 create:true,
	             width:"20%",
	             options : 'worflow.capable.entity.type.list'
	        },
	        entityId:{
	        	 title: 'Entity',
	          	 list:true,
	           	 edit:false,
	           	 create:true,
	             width:"20%",
	             dependsOn: 'entityTypeId',
	             options:function(data){
	            	 if(data.dependedValues.entityTypeId == 30 || data.dependedValues.entityTypeId == 33){
	            		 return 'entity.for.type.list?engagementId='+testFactoryId+'&productId='+productId+'&entityTypeId='+data.dependedValues.entityTypeId;
	            	 }else{
	            		 return {'0' : '--'};
	            	 }
	             }
	        },
	        entityInstanceId:{
	        	list:false,
	        	create:false,
	        	edit:false,
	        	defaultValue: 0
	        },
	        workflowId:{
	             title: 'Workflow Template',
	          	 list:true,
	           	 edit:false,
	           	 create:true,
	             width:"30%",
	             dependsOn:'entityTypeId',
	             options:function(data){
	            	 var workflowType = "Status Workflow";
	            	 if(data.dependedValues.entityTypeId == 34){
	            		 workflowType = "Life Cycle Workflow";
	            	 }
	            	 return 'workflow.master.list.options?workflowType='+workflowType;
	             }
	        },
	        isDefault: {
	        	title: 'Is Default' ,
	        	list:true,
	        	edit:true,
	        	create:false,
	        	type : 'checkbox',
	        	values: {'0' : 'No','1' : 'Yes'},
	        	defaultValue: '0'
	        }, 
			statusAndPolicies:{
	        	title : '',
	        	list : true,
	        	create : false,
	        	edit : false,
	        	width: "10%",
	        	display:function (data) { 
	           		//Create an image for test script popup 
	        		var $img = $('<img src="css/images/list_metro.png" title="Status Policy" />'); 
           			$img.click(function () {
           				$img = listStatusAndPolicies($img, data.record.workflowId, data.record.entityTypeId, data.record.entityId);
           		  });
           			return $img;
	        	}
	        },
			commentsWorkFlowEntMapping:{
				title : '',
				list : true,
				create : false,
				edit : false,
				width: "5%",
				display:function (data) { 
				//Create an image for test script popup 
					var $img = $('<i class="fa fa-comments" title="Comments"></i>');
					$img.click(function () {
						var entityTypeIdComments = 70;
						var entityNameComments = "WorkflowMasterEntityMapping";
						listComments(entityTypeIdComments, entityNameComments, data.record.workflowEntityMappingId, data.record.entityTypeId, "workFlowEntMapComments");
					});
					return $img;
				}		
			},
	        /*usersMapping:{
	        	var $img = $('<img src="css/images/user.png" title="User Permissions" />');
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
     });		 
	 $('#jTableContainerWorkflowEntityMapping').jtable('load');		 
}*/

/*function listStatusAndPolicies($img, workflowId, entityTypeId, entityId){
	if(typeof entityId == 'undefined' || entityId == null){
		entityId = 0;
	}
	$('#jTableContainerWorkflowEntityMapping').jtable('openChildTable', $img.closest('tr'), 
	{
		title: 'Status Policy',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: 'workflow.status.policy.list?isActive=1&productId='+productId+'&workflowId='+workflowId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&scopeType=Entity',
            //createAction: 'workflow.status.policy.add?workflowId='+workflowId,
            editinlineAction: 'workflow.status.policy.update?workflowId='+workflowId,
		}, 
		recordUpdated:function(event, data){
			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		recordAdded: function (event, data) {
			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		recordDeleted: function (event, data) {
			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
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
			/*statusPolicyType: {
				title: 'Status Policy Type',
				inputTitle : 'Status Policy Type',
				width: "15%",
				list: true,
				edit : true,
				create: true,
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
	              edit : true,
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
	        			$imgUser = listUsers($imgUser, data.record.workflowStatusId, entityTypeId, entityId);
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
	        			$imgRole = listRoles($imgRole, data.record.workflowStatusId, entityTypeId, entityId);
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
    	data.childTable.jtable('load'); 
	});

	return $img; 
}

function listUsers($imgUser, workflowStatusId, entityTypeId, entityId){
	
	if(typeof entityId == 'undefined' || entityId == null){
		entityId = 0;
	}
	
	if(entityTypeId == 30 || entityTypeId == 33){
		isAddonUsersAllowed = true;
	}
	
	$('#jTableContainerWorkflowEntityMapping').jtable('openChildTable', $imgUser.closest('tr'), 
	{
		title: 'Status Policy User',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: 'workflow.status.policy.actor.list?productId='+productId+'&workflowStatusId='+workflowStatusId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&actorType=User',
            createAction: 'workflow.status.policy.actor.add?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&actorMappingType=User',
            editinlineAction :'workflow.status.policy.actor.update',
            deleteAction: 'workflow.status.policy.actor.delete'
		}, 
		recordUpdated:function(event, data){
			//$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		recordAdded: function (event, data) {
			//$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		recordDeleted: function (event, data) {
			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
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
					return 'workflow.user.list.by.product.id.options?productId='+productId+'&isAddonUsersAllowed='+isAddonUsersAllowed;
				}
			}, 
			actionRequirement: { 
				title : 'Action Type',
				inputTitle: 'Action Type <font color="#efd125" size="4px"></font>',
				create:true,
				list : true,
				edit : true,
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
		formCreated: function (event, data) {
			data.form.find('select[name="userId"]').addClass('validate[required]');
            data.form.validationEngine();
        },
		formSubmitting: function (event, data) {
			//data.form.validationEngine();
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

function listRoles($imgRole, workflowStatusId, entityTypeId, entityId){
	
	if(typeof entityId == 'undefined' || entityId == null){
		entityId = 0;
	}
	$('#jTableContainerWorkflowEntityMapping').jtable('openChildTable', $imgRole.closest('tr'), 
	{
		title: 'Status Policy Role',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:false},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: 'workflow.status.policy.actor.list?productId='+productId+'&workflowStatusId='+workflowStatusId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&actorType=Role',
            createAction: 'workflow.status.policy.actor.add?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityInstanceId=0&actorMappingType=Role',
            deleteAction: 'workflow.status.policy.actor.delete'
		}, 
		recordUpdated:function(event, data){
			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		recordAdded: function (event, data) {
			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
		},
		recordDeleted: function (event, data) {
			$('#jTableContainerWorkflowEntityMapping').find('.jtable-child-table-container').jtable('reload');
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
			/*actionRequirement: { 
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

	return $imgRole; 
}*/

function setDefaultnode(defaultNodeId) {			
	if(isFirstLoad) {
		$("#treeContainerDiv").on("loaded.jstree",function(evt, data) {
			$.each($('#treeContainerDiv li'), function(ind, ele){
				if($.jstree.reference('#treeContainerDiv').is_parent($(ele).attr("id"))){
					defaultNodeId = $(ele).attr("id");							
					isFirstLoad = false;
					return false;
				}
			});	
			setDefaultnode(defaultNodeId);
		});
	} else {
		defaultNodeId = $.jstree.reference('#treeContainerDiv').get_node(defaultNodeId).children[0];
		$.jstree.reference('#treeContainerDiv').deselect_all();
		$.jstree.reference('#treeContainerDiv').close_all();
		$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
		$.jstree.reference('#treeContainerDiv').trigger("select_node");
	}
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
	};
	CommentsMetronicsUI.init(jsonObj);
}