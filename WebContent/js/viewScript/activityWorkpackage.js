var activityWorkpackageTabJsonObj='';
var ActivityWorkpackage = function() {
 	var initialise = function(jsonObj){
 		assignActivityWorkpackageDT(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};		
}();

function assignActivityWorkpackageDT(jsonObj){
	activityWorkpackageTabJsonObj = jsonObj;
	activityWorkpackageListDataTable(jsonObj);
}

var activityWorkpackageOptionsArr=[];
var activityWorkpackageResultArr=[];
var activityWorkpackageOptionsItemCounter=0;

var optionsType_activityWorkpackage="activityWorkpackage";
var activityWorkpackage_editor='';

function activityWorkpackageListDataTable(jsonObj){
	openLoaderIcon();	
	 $.ajax({
		  type: "POST",
		  url:jsonObj.listURL,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
						
			if(data == null || data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			jsonObj.data = data;
			if(jsonObj.data.length != 0){
				jsonObj.productId = jsonObj.data[0].productId;
				productId = jsonObj.data[0].productId;
			}
			
			activityWorkpackageResults_Container(jsonObj);						
		  },
		  error : function(data) {
			 closeLoaderIcon();  			
		 },
		 complete: function(data){
			closeLoaderIcon();			
		 }
	});	
}

function activityWorkpackageResults_Container(jsonObj){		
	var entityId = 0;
	activityWorkpackageOptionsItemCounter=0;
	activityWorkpackageResultArr=[];			
	activityWorkpackageOptionsArr = [{id:"ownerId", type:optionsType_activityWorkpackage, url:'common.user.list.by.resourcepool.id.productId?productId='+jsonObj.productId},
	{id:"productBuildId", type:optionsType_activityWorkpackage, url:'process.list.builds.by.product?productId='+jsonObj.productId},
	{id:"priorityId", type:optionsType_activityWorkpackage, url:'administration.executionPriorityList'},
	{id:"competencyId", type:optionsType_activityWorkpackage, url:'dimension.list.options?dimensionTypeId=1'},
	{id:"workflowId", type:optionsType_activityWorkpackage, url:'workflow.master.mapped.to.entity.list.options?productId='+jsonObj.productId+'&entityTypeId=34&entityId='+entityId},	
	];
	
	returnOptionsItemForActivityWorkpackage(activityWorkpackageOptionsArr[0].url, jsonObj);
}
// ----- Return options ----

function returnOptionsItemForActivityWorkpackage(url, jsonObj){
	openLoaderIcon();
	
	$.ajax( {
 	   "type": "POST",
        "url":  url,
        "dataType": "json",
         success: function (json) {
         if(json == null || json.Result == "ERROR" || json.Result == "Error"){
         	if(json.Message !=null) {
         		callAlert(json.Message);
         	}
         	json.Options=[];
         	activityWorkpackageResultArr.push(json.Options);         	
         	returnOptionsItemForActivityWorkpackage(url, jsonObj); 
         	
         }else{        	        	     	        	
			for(var i=0;i<json.Options.length;i++){
				if(json.Options[i].DisplayText == null)
					json.Options[i].DisplayText = "--";	
				
				json.Options[i].label=json.Options[i].DisplayText;
				json.Options[i].value=json.Options[i].Value;
			}
     	   activityWorkpackageResultArr.push(json.Options);
		   
     	   if(activityWorkpackageOptionsItemCounter<activityWorkpackageOptionsArr.length-1){			   
			   returnOptionsItemForActivityWorkpackage(activityWorkpackageOptionsArr[activityWorkpackageResultArr.length].url, jsonObj);     		  
     	   }else{   			  	      			       			   				  				  
     		  activityWorkpackage_Container(jsonObj.data, jsonObj); 
		   }
     	   activityWorkpackageOptionsItemCounter++;     	   
         }
		 
			closeLoaderIcon();
         },
         error: function (data) {
        	 activityWorkpackageOptionsItemCounter++;
        	 closeLoaderIcon(); 
         },
         complete: function(data){
         	//console.log('Completed');
         	closeLoaderIcon(); 
         },	            
   	});	
}

function activityWorkpackageResultsEditor(jsonObj){	
    
	activityWorkpackage_editor = new $.fn.dataTable.Editor( {
			"table": '#activityWkPkg_'+jsonObj.containerID+'_dataTable',
    		ajax: "process.workrequest.add",
    		ajaxUrl: "process.workrequest.update",
    		idSrc:  "activityWorkPackageId",
    		i18n: {
    	        create: {
    	            title:  "Create a new workPackage",
    	            submit: "Create",
    	        }
    	    },
    		fields: [{								
				label:"activityWorkPackageId",
				name:"activityWorkPackageId",					
				type: 'hidden',	                
			},{								
				label:"productVesionBuildName",
				name:"productVesionBuildName",					
				type: 'hidden',	
						
			},{
                label: "Work Package Name *",
                name: "activityWorkPackageName",                
            },{
                label: "Planned Start Date *",
                name: "plannedStartDate",  
				type:  'datetime',
				def:    function () { return new Date(); },
				format: 'M/D/YYYY',	
            },{
                label: "Planned End Date *",
                name: "plannedEndDate",     
				type:  'datetime',
				def:    function () { return new Date(); },
				format: 'M/D/YYYY',	
            },{								
				label:"workflowRAG",
				name:"workflowRAG",	
				type: 'hidden',	  				
			},{
                label: "statusDisplayName",
                name: "statusDisplayName",
               type: 'hidden',	 
            },{
                label: "actors",
                name: "actors", 
				type: 'hidden',	 	
            },{
                label: "actorIds",
                name: "actorIds",                	
				type: 'hidden',	 
            },{
                label: "completedBy",
                name: "completedBy",
                 type: 'hidden',	 
            },{
                label: "remainingHrsMins",
                name: "remainingHrsMins",
                type: 'hidden',	 
            },{
                label: "Owner *",
                name: "ownerId",
                 options: activityWorkpackageResultArr[0],
                "type"  : "select",
            },{
                label: "Build *",
                name: "productBuildId",
                 options: activityWorkpackageResultArr[1],
                "type"  : "select",
            },{
                label: "Priority",
                name: "priorityId",
                 options: activityWorkpackageResultArr[2],
                "type"  : "select",
            },{								
				label:"Competency",
				name:"competencyId",					
				options: activityWorkpackageResultArr[3],
                "type"  : "select",				
			},{								
				label:"Description",
				name:"description",
                type: 'hidden',				
			},{								
				label:"Remarks",
				name:"remark",
                type: 'hidden',				
			},{								
				label:"WorkFlow Template",
				name:"workflowId",					
				options: activityWorkpackageResultArr[4],
                "type"  : "select",				
			},{								
				label:"actualStartDate",
				name:"actualStartDate",					
				type: 'hidden',					
			},{								
				label:"actualEndDate",
				name:"actualEndDate",					
				type: 'hidden',					
			},{								
				label:"totalEffort",
				name:"totalEffort",					
				type: 'hidden',					
			},{
				label:"percentageCompletion",
				name:"percentageCompletion",
				type: 'hidden',	
			},{								
				label:"workflowIndicator",
				name:"workflowIndicator",					
				type: 'hidden',					
			},{								
				label:"Active",
				name:"isActive",					
				type: 'hidden',					
			},{								
				label:"productBuildName",
				name:"productBuildName",					
				type: 'hidden',					
			},
        ]
    	});	
}

function activityWorkpackageHeader(){
	var childDivString ='<table id="activityWkPkg_'+activityWorkpackageTabJsonObj.containerID+'_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>Product/Version/Build</th>'+
			'<th>Name</th>'+
			'<th>Planned Start Date</th>'+			
			'<th>Planned End Date</th>'+
			'<th></th>'+
			'<th>Current Status</th>'+
			'<th>Status Pending With</th>'+
			'<th>Status Complete By</th>'+
			'<th>Status Time Left</th>'+
			'<th>Owner</th>'+ 
			'<th>Build</th>'+
			'<th>Priority</th>'+
			'<th>Competency</th>'+			
			'<th>Actual Start Date</th>'+ 
			'<th>Total Effort</th>'+
			'<th>% Completion</th>'+
			'<th>Active</th>'+					
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

var clearTimeoutDTactivityWorkpackages='';
function reInitializeDTactivityWorkpackages(){
	clearTimeoutDTactivityWorkpackages = setTimeout(function(){				
		activityWorkpackage_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTactivityWorkpackages);
	},200);
}

function activityWorkpackage_Container(data, jsonObj){
	try{
		if ($("#"+jsonObj.containerID).children().length>0) {
			$("#"+jsonObj.containerID).children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(19);  // total coulmn count		  
	  var childDivString = activityWorkpackageHeader(); 						  
	  $("#"+jsonObj.containerID).append(childDivString);
	  	  
	  $('#'+jsonObj.containerID+' tfoot tr').html('');     			  
	  $('#'+jsonObj.containerID+' tfoot tr').append(emptytr);
	
	// --- editor for the activity Change Request started -----
	activityWorkpackageResultsEditor(jsonObj);
	
	if(data.length>0){
		for(var i=0;i<data.length;i++){			
			if(data[i]["actualStartDate"] == null){				
				data[i]["actualStartDate"] = null;				
			}else{
				data[i]["actualStartDate"] = convertDTDateFormatAdd(data[i]["actualStartDate"]);				
			}
			
			if(data[i]["actualEndDate"] == null){				
				data[i]["actualEndDate"] = null;				
			}else{
				data[i]["actualEndDate"] = convertDTDateFormatAdd(data[i]["actualEndDate"]);				
			}
			
			if(data[i]["baselineStartDate"] == null){				
				data[i]["baselineStartDate"] = null;				
			}else{
				data[i]["baselineStartDate"] = convertDTDateFormatAdd(data[i]["baselineStartDate"]);				
			}
			
			if(data[i]["baselineEndDate"] == null){				
				data[i]["baselineEndDate"] = null;				
			}else{
				data[i]["baselineEndDate"] = convertDTDateFormatAdd(data[i]["baselineEndDate"]);				
			}
			
			if(data[i]["plannedStartDate"] == null){				
				data[i]["plannedStartDate"] = null;				
			}else{
				data[i]["plannedStartDate"] = convertDTDateFormatAdd(data[i]["plannedStartDate"]);				
			}
			
			if(data[i]["plannedEndDate"] == null){				
				data[i]["plannedEndDate"] = null;				
			}else{
				data[i]["plannedEndDate"] = convertDTDateFormatAdd(data[i]["plannedEndDate"]);				
			}
			if(data[i]["modifiedDate"] == null){				
				data[i]["modifiedDate"] = null;				
			}else{
				data[i]["modifiedDate"] = convertDTDateFormatAdd(data[i]["modifiedDate"]);				
			}
			
		}
	}
	
	var useCaseTitle='';	
	if(useCaseAWP == "YES"){
		useCaseTitle= 'Activity Creation from Use Cases';
		$('#prodChangeRequestsImg').attr('title', 'Download Template - Use Cases');
	}else{		
		useCaseTitle= 'Activity Creation from ChangeRequest';	
		$('#prodChangeRequestsImg').attr('title', 'Download Template - Change Requests');
		
	}
			
	activityWorkpackage_oTable = $('#activityWkPkg_'+jsonObj.containerID+'_dataTable').dataTable( {
		 	dom: "Bfrtilp",
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY": "100%",
	       "bSort": false,
		    select: false,		   
	       "bScrollCollapse": true,	       
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex = [5,17,18]; // search column TextBox Invisible Column position
     		  $('#activityWkPkg_'+jsonObj.containerID+'_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			   reInitializeDTactivityWorkpackages();			   
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: activityWorkpackage_editor },	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "ActivityWorkpackages",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "ActivityWorkpackages",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "ActivityWorkpackages",
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
		   { mData: "activityWorkPackageId",className: 'disableEditInline', sWidth: '3%' },	
           { mData: "productVesionBuildName",className: 'disableEditInline', sWidth: '3%' },
           { mData: "activityWorkPackageName",className: 'editable', sWidth: '20%' },           
		   { mData: "plannedStartDate",className: 'editable', sWidth: '25%' },           
		   { mData: "plannedEndDate",className: 'editable', sWidth: '3%' },		   		   
		  	{ mData: "workflowRAG",className: 'editable', sWidth: '3%' },	           
		    { mData: "statusDisplayName", className: 'disableEditInline', sWidth: '10%',
				mRender: function (data, type, full) {					
					return full.statusDisplayName+"&nbsp;"+full.workflowStatusCategoryName;
				 },
			},	
           { mData: "actors",className: 'disableEditInline', sWidth: '20%' },           
		   { mData: "completedBy",className: 'disableEditInline', sWidth: '25%' },           
		   { mData: "remainingHrsMins",className: 'disableEditInline', sWidth: '3%' },         
		   { mData: "ownerName", className: 'editable', sWidth: '10%', editField: "ownerId",
				mRender: function (data, type, full) {					
					 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(activityWorkpackage_editor, 'ownerId', full.ownerId);
		           	 }else if(type == "display"){
						data = full.ownerName;
					 }	           	 
					 return data;
				 },
			},			           
		   { mData: "productBuildName", className: 'editable', sWidth: '10%',
				mRender: function (data, type, full) {					
					 data = full.productBuildName;           	 
					 return data;
				 },
			},		          
		   { mData: "priorityName", className: 'editable', sWidth: '10%', editField: "priorityId",
				mRender: function (data, type, full) {					
					 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(activityWorkpackage_editor, 'priorityId', full.priorityId);
		           	 }else if(type == "display"){
						data = full.priorityName;
					 }	           	 
					 return data;
				 },
			},		   
		   { mData: "competencyName", className: 'editable', sWidth: '10%', editField: "competencyId",
				mRender: function (data, type, full) {					
					 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(activityWorkpackage_editor, 'competencyId', full.competencyId);
		           	 }else if(type == "display"){
						data = full.competencyName;
					 }	           	 
					 return data;
				 },
			},
		  { mData: "actualStartDate",className: 'editable', sWidth: '3%' },	
           { mData: "totalEffort",className: 'disableEditInline', sWidth: '3%' },
		   { mData: "percentageCompletion",className: 'editable', sWidth: '3%' },
           { mData: "isActive",className: 'editable', sWidth: '20%',           		  
			  mRender: function (data, type, full) {
				  if ( type === 'display' ) {
						return '<input type="checkbox" class="activityWorkpackageEditor-active">';
				  }
					return data;
				},
				className: "dt-body-center"	            
           },           
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
       				'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;  margin-left: 5px;">'+
						'<img src="css/images/cloning.jpg" class="activityWorkpackageImg1" style="width: 24px;height: 24px;" title="Cloning Activity Workpackage" data-toggle="modal"/></button>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;  margin-left: 5px;">'+
						'<i class="fa fa-scissors details-control activityWorkpackageImg2" title="Moving Activity Workpackage" style="padding-left: 0px;"></i></button>'+  	
					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
							'<img src="css/images/mapping.png" class="details-control activityWorkpackageImg3" title="'+useCaseTitle+'"></button>'+
					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
							'<img src="css/images/user.png" class="details-control activityWorkpackageImg4" title="WorkPackage and User Mapping"></button>'+										
					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
						'<i class="fa fa-history showHandCursor activityWorkpackageImg5" title="Event History"></i></button>'+						
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;  margin-left: 5px;">'+
 	       					'<img src="css/images/workflow.png" class="activityWorkpackageImg6" title="Configure Workflow" /></button>'+	 
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px; margin-left: 5px;">'+
 	       					'<div><img src="css/images/attachment.png" title="Attachment" class="activityWorkpackageImg7" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+full.attachmentCount+']</span></div></button>'+
     	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="fa fa-search-plus activityWorkpackageImg8" title="Audit History"></i></button>'+
   					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						'<i class="fa fa-comments activityWorkpackageImg9" title="Comments"></i></button>'+
					'<button style="border: none; background-color: transparent; outline: none;">'+
						'<i class="fa fa-trash-o details-control" title="Delete Activity Task" onClick="deleteActivityWorkpackage('+data.activityWorkPackageId+')" style="margin-left: 5px;"></i></button>'+		
     	       		'</div>');	      		
           		 return img;
            	}
            },
       ], 
		rowCallback: function ( row, data ) {
	           $('input.activityWorkpackageEditor-active', row).prop( 'checked', data.isActive == 1 );
	    },	   
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		 	 		 
		 $('#activityWkPkg_'+jsonObj.containerID+'_dataTable_length').css('margin-top','8px');
		 $('#activityWkPkg_'+jsonObj.containerID+'_dataTable_length').css('padding-left','35px');
		 
		 $('#activityWkPkg_'+jsonObj.containerID+'_dataTable').on( 'change', 'input.activityWorkpackageEditor-active', function () {
			 activityWorkpackage_editor
					.edit( $(this).closest('tr'), false )
					.set( 'isActive', $(this).prop( 'checked' ) ? 1 : 0 )
					.submit();
			});
		 	 
		 // Activate an inline edit on click of a table cell
        $('#activityWkPkg_'+jsonObj.containerID+'_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	activityWorkpackage_editor.inline( this, {
        		submitOnBlur: true
			}); 
		});
		
		activityWorkpackage_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
					if ( that.search() !== this.value ) {
						that
							.search( this.value, true, false )
							.draw();
					}
				} );
			} );
			
		// -----		
		 
		 // ----- Execute -----		 
		 $('#activityWkPkg_'+jsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityWorkpackageImg1', function () {			 
			 var tr = $(this).closest('tr');
			 var row = activityWorkpackage_oTable.DataTable().row(tr);
			
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
					success : function(datas) {
						treeData = datas;
						$('#plannedDateDiv').show();
						closeLoaderIcon();
						var jsonActivityWorkPackageCloningObj = {
							"title": "Cloning Activity WorkPackage",
							"packageName":"Activity WorkPackage Name",
							"startDate" : dateFormat(row.data().plannedStartDate),
							"endDate" : dateFormat(row.data().plannedEndDate),
							"selectionTerm" : "Select Build",
							"sourceParentID": row.data().productBuildId,
							"sourceParentName": row.data().productBuildName,
							"sourceID": row.data().activityWorkPackageId,
							"sourceName": row.data().activityWorkPackageName,
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
		 // ----- ended -----
		 
		 $('#activityWkPkg_'+jsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityWorkpackageImg2', function () {			 
			 var tr = $(this).closest('tr');
			 var row = activityWorkpackage_oTable.DataTable().row(tr);
			
			openLoaderIcon();
		$('#plannedDateDiv').hide();
		// -- for cloning workpackage ---
			$.ajax({
				type: "POST",
				contentType: "application/json; charset=utf-8",			  
				url: 'administration.product.hierarchy.tree.by.productId?productId='+row.data().productId,
				dataType : 'json',
				complete : function(data){
					console.log('complete');
					closeLoaderIcon();
				},
				success : function(datas) {
					treeData = datas;
					closeLoaderIcon();
					var jsonActivityWorkPackageCloningObj = {
					"title": "Moving Activity WorkPackage",
					"packageName":"Activity WorkPackage Name",
					"startDate" : dateFormat(row.data().plannedStartDate),
					"endDate" : dateFormat(row.data().plannedEndDate),
					"selectionTerm" : "Select Build",
					"sourceParentID": row.data().productBuildId,
						"sourceParentName": row.data().productBuildName,
					"sourceID": row.data().activityWorkPackageId,
					"sourceName": row.data().activityWorkPackageName,
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
		// ----- Feature Mapping -----		 
		 $('#activityWkPkg_'+jsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityWorkpackageImg3', function () {			 
			 var tr = $(this).closest('tr');
			 var row = activityWorkpackage_oTable.DataTable().row(tr);	

			prodBuildId=row.data().productBuildId;
			actWorkPackageId = row.data().activityWorkPackageId;
			loadChangeRequestsByProductId(row.data().productId,prodBuildId,actWorkPackageId);								
			// DragDrop Testing ended----	
						 
		 });		 
		 // ----- ended -----	

		// ----- TestScripts -----		 
		 $('#activityWkPkg_'+jsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityWorkpackageImg4', function () {			 
			 var tr = $(this).closest('tr');
			 var row = activityWorkpackage_oTable.DataTable().row(tr);	

			prodBuildId=row.data().productBuildId;
			actWorkPackageId = row.data().activityWorkPackageId;
			awName = row.data().activityWorkPackageName;
			workPackageUserMapping('img', row.data().productId, 34, actWorkPackageId,awName);
						 
		 });		 
		 // ----- ended -----
		
		// ----- TestScripts -----		 
		 $('#activityWkPkg_'+jsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityWorkpackageImg5', function () {			 
			var tr = $(this).closest('tr');
			var row = activityWorkpackage_oTable.DataTable().row(tr);		 
		 
			addActivityWorkpackageComments(row.data());
		});
		
		// ----- Execution Summary -----		 
		 $('#activityWkPkg_'+jsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityWorkpackageImg6', function () {			 
			var tr = $(this).closest('tr');
			var row = activityWorkpackage_oTable.DataTable().row(tr);		 		 
		
			workflowId = 0;
			entityTypeId = 34;
			entityId = 0;
			statusPolicies(row.data().productId, workflowId, entityTypeId, entityId, row.data().activityWorkPackageId, row.data().activityWorkPackageName, "Activity Workpackage", row.data().statusId);
		});
		
		// ----- Configure Workflow -----		 
		 $('#activityWkPkg_'+jsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityWorkpackageImg7', function () {			 
			var tr = $(this).closest('tr');
			var row = activityWorkpackage_oTable.DataTable().row(tr);		 
		 
			isViewAttachment = false;
			var jsonObj={"Title":"Attachments for Activity Workpackage",			          
				"SubTitle": 'Activity Workpackage : ['+row.data().activityWorkPackageId+'] '+row.data().activityWorkPackageName,
				"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=34&entityInstanceId='+row.data().activityWorkPackageId,
				"creatURL": 'upload.attachment.for.entity.or.instance?productId='+row.data().productId+'&entityTypeId=34&entityInstanceId='+row.data().activityWorkPackageId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
				"deleteURL": 'delete.attachment.for.entity.or.instance',
				"updateURL": 'update.attachment.for.entity.or.instance',
				"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=34',
				"multipleUpload":true,
			};	 
			Attachments.init(jsonObj);
		});
		
		// ----- Audit History -----		 
		 $('#activityWkPkg_'+jsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityWorkpackageImg8', function () {			 
			var tr = $(this).closest('tr');
			var row = activityWorkpackage_oTable.DataTable().row(tr);		 
		 
			//listActivityWorkPackageAuditHistory(row.data().activityWorkPackageId);			
			listGenericAuditHistory(row.data().activityWorkPackageId, "ActivityWorkpackage","activityWorkPackageAudit");
		});
		
		// ----- Comments -----		 
		 $('#activityWkPkg_'+jsonObj.containerID+'_dataTable tbody').on('click', 'td button .activityWorkpackageImg9', function () {			 
			var tr = $(this).closest('tr');
			var row = activityWorkpackage_oTable.DataTable().row(tr);		 
		 
			var entityTypeId = 34;
			var entityName = "ActivityWorkpackage";
			listComments(entityTypeId,entityName,row.data().activityWorkPackageId,row.data().activityWorkPackageName, "testFactoryLevelAWPComments");
		}); 
	});
	// ------	
}

// ----- Deletion Activity Items Started -----

function deleteActivityWorkpackage(activityWorkPackageId){
	var fd = new FormData();
	fd.append("activityWorkPackageId", activityWorkPackageId);
	
	openLoaderIcon();
	$.ajax({
		url : 'process.workrequest.delete',
		data : fd,
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
				callAlert(data.Message);
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
