var ActivityWorkflow = function(){	
	var initialise = function(jsonObj){
		initialiseActivityWorkflowTab (jsonObj);
	};	
		return {
			init : function(jsonObj){
				initialise(jsonObj);
			}
		};
}();
var activityWFTabJsonObj='';
function initialiseActivityWorkflowTab(jsonObj){
	activityWFTabJsonObj = jsonObj;
	listActivityWorkflowOfSelProdComponent(activityWFTabJsonObj);
}

var activityWorkflowTab_oTable='';
var activityWorkflowTab_editor='';
var clearTimeoutDTActivityWorkflowTab='';

var activityWFOptionsArrTab=[];
var activityWFTabResultArr=[];
var activityWFOptionsItemCounter=0;
var optionsType_ActivityWorkflowTab='ActivityWorkflowTab';

function listActivityWorkflowOfSelProdComponent(jsonObj){	
	openLoaderIcon();
	
	if(typeof activityWFTabJsonObj.entityIdSP == 'undefined' || activityWFTabJsonObj.entityIdSP == null){
		activityWFTabJsonObj.entityIdSP = 0;
	}
	getSourceWorkflowMapped(productId, activityWFTabJsonObj.entityTypeIdSP, activityWFTabJsonObj.entityIdSP, activityWFTabJsonObj.entityInstanceIdSP);
	
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
			activityWorkflowTab_Container(data, jsonObj); 
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

function createEditorForactivityWorkflowTab(jsonObj){
	
	activityWorkflowTab_editor = new $.fn.dataTable.Editor( {
			"table": '#activityWFTab_'+jsonObj.containerID+'_dataTable',
			ajax: jsonObj.creatURL,
			ajaxUrl: jsonObj.updateURL,			
			"idSrc":  "workflowStatusPolicyId",
			i18n: {
				create: {	    	           
					title: "Add ActivityWorkflow",
					submit: "Create",
				}
			},
			fields: [ {
				label: "workflowStatusPolicyId",
				name: "workflowStatusPolicyId",
				"type": "hidden",					
			},{
				label: "workflowStatusName",
				name: "workflowStatusName",
				"type": "hidden",					
			},{					
				label: "workflowStatusDisplayName",
				name: "workflowStatusDisplayName",		            
			},{
				label: "statusActors",
				name: "statusActors",			
			},{
				label: "plannedStartDate",
				name: "plannedStartDate",					
			},
			{
				label: "plannedEndDate",
				name: "plannedEndDate",					
			},
			{
				label: "actualStartDate",
				name: "actualStartDate",					
			},{
				label: "actualEndDate",
				name: "actualEndDate",					
			},{
				label: "plannedEffort",
				name: "plannedEffort",					
			},{
				label: "actualEffort",
				name: "actualEffort",					
			},{
				label: "slaDuration",
				name: "slaDuration",
				"type": "hidden",		
			},
			]
		});
}


function reInitializeDTActivityWorkflowTab(){
	clearTimeoutDTActivityWorkflowTab = setTimeout(function(){				
		activityWorkflowTab_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTActivityWorkflowTab);
	},200);
}

function activityWFTabHeader(){
	var childDivString = '<table id="activityWFTab_'+activityWFTabJsonObj.containerID+'_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Status</th>'+
			'<th>Actors</th>'+			
			'<th>Planned Start Date</th>'+	
			'<th>Planned End Date</th>'+
			'<th>Actual Start Date</th>'+
			'<th>Actual End Date</th>'+
			'<th>Planned Effort</th>'+
			'<th>Actual Effort</th>'+
			'<th></th>'+					
		'</tr>'+
	'</thead>'+
	'<tfoot>'+
		'<tr></tr>'+
	'</tfoot>'+
	'</table>';		
	
	return childDivString;
}

function activityWorkflowTab_Container(data, jsonObj){
	try{
		if ($("#"+jsonObj.containerID).children().length>0) {
			$("#"+jsonObj.containerID).children().remove();
		}
	} 
	catch(e) {}
	
	var emptytr = emptyTableRowAppending(9);  // total coulmn count		  
	var childDivString = activityWFTabHeader(); 			 
	$("#"+jsonObj.containerID).append(childDivString);
	
	$('#'+jsonObj.containerID+' tfoot tr').html('');     			  
	$('#'+jsonObj.containerID+' tfoot tr').append(emptytr);
	
	// --- editor for the activity Change Request started -----
	createEditorForactivityWorkflowTab(jsonObj);
	
	activityWorkflowTab_oTable = $('#activityWFTab_'+activityWFTabJsonObj.containerID+'_dataTable').dataTable( {
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
			   
			   var searchcolumnVisibleIndex = [8]; // search column TextBox Invisible Column position			   
     		  $('#activityWFTab_'+activityWFTabJsonObj.containerID+'_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			   
			   reInitializeDTActivityWorkflowTab();			   
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: activityWorkflowTab_editor },	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "Workflow",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "Workflow",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "Workflow",
		                    	exportOptions: {
		                            columns: ':visible'
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
           { mData: "workflowStatusDisplayName",className: 'disableEditInline', sWidth: '5%' },
			{ mData: "statusActors",className: 'disableEditInline', sWidth: '10%' },		   
			{ mData: "plannedStartDate",className: 'editable', sWidth: '15%' },		   			
           { mData: "plannedEndDate",className: 'editable', sWidth: '15%' },
           { mData: "actualStartDate",className: 'editable', sWidth: '15%' },           
			{ mData: "actualEndDate",className: 'editable', sWidth: '15%' },
			{ mData: "plannedEffort",className: 'editable', sWidth: '5%' },           
			{ mData: "actualEffort",className: 'editable', sWidth: '5%' },	
           /*{ mData: "raisedDate", className: 'disableEditInline', sWidth: '15%',
           		mRender: function (data, type, full) {
		           	 if (full.action == "create"){
		           		data = convertDTDateFormatAdd(data, ["raisedDate"]);		           		
		           	 }else if(type == "display"){
		           		data = full.raisedDate;
		           	 }	           	 
		             return data;
	             }
           },)*/{ mData: null,				 
            	bSortable: false,
				className: 'disableEditInline',
				sWidth: '5%',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
     	       		'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
 	       				'<i class="fa fa-user activityWorkflowabtImg1" title="Users"></i></button>'+
					'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
 	       				'<i class="fa fa-users activityWorkflowabtImg2" title="Roles"></i></button>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						'<i class="fa fa-comments activityWorkflowabtImg3" title="Comments"></i></button>'+
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
		 
		 $('#activityWFTab_'+activityWFTabJsonObj.containerID+'_dataTable_wrapper').find(".buttons-create").hide();
		 
		 $('#activityWFTab_'+activityWFTabJsonObj.containerID+'_dataTable_length').css('margin-top','8px');
		 $('#activityWFTab_'+activityWFTabJsonObj.containerID+'_dataTable_length').css('padding-left','35px');
	 
		 // ----- focus area -----			
		$( 'input', activityWorkflowTab_editor.node()).on( 'focus', function () {
			this.select();
		});
		 		 
		 activityWorkflowTab_oTable.DataTable().columns().every( function () {
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
        $('#activityWFTab_'+activityWFTabJsonObj.containerID+'_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	activityWorkflowTab_editor.inline( this, {
        		submitOnBlur: true
			}); 
		});
					
		// ------ Attachments -----
		 
		 $('#activityWFTab_'+activityWFTabJsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityWorkflowabtImg1', function () {
				var tr = $(this).closest('tr');
			    var row = activityWorkflowTab_oTable.DataTable().row(tr);
								
				var url= 'workflow.status.policy.actor.list?productId='+productId+'&workflowStatusId='+row.data().workflowStatusId+'&entityTypeId='+row.data().entityTypeId+'&entityId='+row.data().entityId+'&entityInstanceId=0&actorType=User&jtStartIndex=0&jtPageSize=10000';				 
				 workflowTemplateDataTable(url, "ChildTable1", row);
		 });
		 // ----- ended -----
		 
		 // ----- Audit History -----
		 
		 $('#activityWFTab_'+activityWFTabJsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityWorkflowabtImg2', function () {
			 var tr = $(this).closest('tr');
			  var row = activityWorkflowTab_oTable.DataTable().row(tr);	 		 				
			
			var url='workflow.status.policy.actor.list?productId='+productId+'&workflowStatusId='+row.data().workflowStatusId+'&entityTypeId='+row.data().entityTypeId+'&entityId='+row.data().entityId+'&entityInstanceId=0&actorType=Role&jtStartIndex=0&jtPageSize=10000';	
		   workflowTemplateDataTable(url, "ChildTable2", row);
			   
		 });
		 
		 // ----- ended -----

		// -----Comments -----
		 
		 $('#activityWFTab_'+activityWFTabJsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityWorkflowabtImg3', function () {
			 var tr = $(this).closest('tr');
			 var row = activityWorkflowTab_oTable.DataTable().row(tr);
			 
			var entityTypeIdComments = 48;
			var entityNameComments = "WorkFlowStatusPolicy";
			listComments(entityTypeIdComments, entityNameComments, row.data().workflowStatusPolicyId, row.data().workflowStatusName, "activityLevelWFStatusComments");
						
		 });
		 
		 // ----- ended -----		 
		 	 		
	});
	// ------	
}