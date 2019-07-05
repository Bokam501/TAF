

function jSingleTableInit(productName,entityType,titlename){
	if(titlename == " " || titlename == undefined){
		var jsonObj={
				"Title":"Workflow Summary : "+productName+" Product's "+entityType,          
		};
	}
	else{
		var jsonObj={
				"Title":"Workflow Summary - "+titlename+ " : "+productName+" Product's "+entityType,          
		};
	}
	
	
	SingleJtableContainer.init(jsonObj);
	clearSingleJTableDatas();
	
	$("#div_SingleJTableSummary .modal-header .row").css('display','none');
}

function listWorkflowSummarySLADetails(engagementId,productId,productName,entityTypeId,indicator,entityId,workflowStatusId,userId,roleId,typeId,titlename,entityParentInstanceId,workflowStatusCategoryId) {
	var entityTypeName = "";
	var tableHeader="";
	var workflowType="Status Workflow";
	var workPackageId=0;
	var lifeCycleStageId=0;
	var showEntity=false;
	if(entityTypeId == 30 ){
		entityTypeName = "Tasks";
		showEntity= true;
		tableHeader='Activity Tasks';
	}else if(entityTypeId == 3){
		entityTypeName = "Testcases";
		tableHeader='Testcases';
	}else if(entityTypeId == 33){
		entityTypeName = "Activities";
		tableHeader='Activities';
	}else if(entityTypeId == 34){
		entityTypeName = "Activity Workpackages";
		tableHeader='Activity Workpackages';
	}	
	/*jSingleTableInit(productName,entityTypeName);
	jSingleTableInit(productName,entityTypeName,titlename);*/
	var urlToGetActivitiesOfSpecifiedActivityId ="";
	if(indicator !="") {
		urlToGetActivitiesOfSpecifiedActivityId ='workflow.summary.SLA.indicator.detail?engagementId='+engagementId+'&productId='+productId+'&entityTypeId='+entityTypeId+'&lifeCycleEntityId='+workPackageId+'&lifeCycleStageId='+lifeCycleStageId+'&indicator='+indicator+'&workflowType='+workflowType+'&entityParentInstanceId='+entityParentInstanceId+'&jtStartIndex=0&jtPageSize=10000';
		assignDataTableValuesWorkflowSummary(urlToGetActivitiesOfSpecifiedActivityId);
	}else{
		urlToGetActivitiesOfSpecifiedActivityId ='workflow.status.summary.instance.detail?engagementId='+engagementId+'&productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&workflowStatusId='+workflowStatusId+'&userId='+userId+'&roleId='+roleId+'&typeId='+typeId+'&entityParentInstanceId='+entityParentInstanceId+'&workflowStatusCategoryId='+workflowStatusCategoryId+'&jtStartIndex=0&jtPageSize=10000';
		assignDataTableValuesWorkflowSummary(urlToGetActivitiesOfSpecifiedActivityId);
	}
			
	workflowSummaryJsonData = '';
	function assignDataTableValuesWorkflowSummary(url){
		openLoaderIcon();
		 $.ajax({
			  type: "POST",
			  url:url,
			  contentType: "application/json; charset=utf-8",
			  dataType : 'json',
			  success : function(data) {		
				closeLoaderIcon();
				
				if(data.Result=="ERROR"){
	      		    data = [];						
				}else{
					data = data.Records;
				}
				workflowSummaryJsonData = data;
				$("#div_SingleDataTableWorkflowSummary").modal();
				$("#div_SingleDataTableWorkflowSummary h4").empty();
				$("#div_SingleDataTableWorkflowSummary h4").text("Workflow Summary : "+productName+" Product's "+entityTypeName);
				singleDataTableWorkflowSummary_Container(data);
				
			  },
			  error : function(data) {
				 closeLoaderIcon();  
			 },
			 complete: function(data){
				closeLoaderIcon();
			 }
		});	
	}
	
	
	function workflowSummaryDataTable(){
		var plannedActivitySizeHeaderTitle="Planned Activity Size";
		var actualActivitySizeHeaderTitle="Actual Activity Size";
		if(customActivityListHeaderFieldsEnable == "YES") {
			plannedActivitySizeHeaderTitle="Estimated Savings";
			actualActivitySizeHeaderTitle="Realized Savings";
		}
		var childDivString = '<table id="workflowSummary_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead>'+          
			'<tr>'+
				'<th>Id</th>'+
				'<th>Name</th>'+
				'<th>Product</th>'+
				'<th>Entity Type</th>'+
				'<th>Entity</th>'+
				'<th>Current Status</th>'+
				'<th>Pending With</th>'+
				'<th>Complete By</th>'+
				'<th>Time Left</th>'+
				'<th>'+plannedActivitySizeHeaderTitle+'</th>'+
				'<th>'+actualActivitySizeHeaderTitle+'</th>'+
				'<th>Planned Effort</th>'+
				'<th>Actual Effort</th>'+
				'<th></th>'+
			'</tr>'+
		'</thead>'+
		'</table>';		
		
		return childDivString;	
	}
	
	
	var workflowSummaryDT_oTable='';
	
	function singleDataTableWorkflowSummary_Container(data){
		try{
			if ($('#dataTableSingleWorkflowSummaryContainer').length>0) {
				$('#dataTableSingleWorkflowSummaryContainer').empty();
			}
		} 
		catch(e) {}
		
		var childDivString = workflowSummaryDataTable(); 			 
		$('#dataTableSingleWorkflowSummaryContainer').append(childDivString);
		
		workflowSummaryDT_oTable = $("#workflowSummary_dataTable").dataTable( {
			 	"dom": '<"top"Bf<"clear">>rt<"bottom"ip<"clear">>',
				paging: true,	    			      				
				destroy: true,
				searching: true,
				bJQueryUI: false,
			    "sScrollX": "100%",
		       "sScrollXInner": "100%",
		       "scrollY":"100%",
		       "bScrollCollapse": true,	       
		       "fnInitComplete": function(data) {
		    	  // reInitializeDTAuditHistory();
			   },  
			   buttons: [
				         {
			                extend: 'collection',
			                text: 'Export',
			                buttons: [
			                    {
			                    	extend: 'excel',
			                    	title: 'Workflow Summary',
			                    	exportOptions: {
			                            columns: ':visible'
			                        }
			                    },
			                    {
			                    	extend: 'csv',
			                    	title: 'Workflow Summary',
			                    	exportOptions: {
			                            columns: ':visible'
			                        }
			                    },
			                    {
			                    	extend: 'pdf',
			                    	title: 'Workflow Summary',
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
		         columnDefs: [
		              //{ targets: [0,1,2,3,4,5,6,7,8,9,10,11,12], visible: true},
		              //{ targets: '_all', visible: false }
		          ],
	        aaData:data,		    				 
		    aoColumns: [	                      	        
	          // { mData: "instanceId"},
	            { mData: "instanceId", sWidth: '5%', "render": function (tcData,type,full) {	        
			 		var entityInstanceId = full.instanceId;					
			 		return ('<a onclick="displayTabActivitySummaryHandler('+entityInstanceId+')">'+full.instanceId+'</a>');		        
		    	},
	            },
	           { mData: "instanceName"},		
	           { mData: "productName"},		
	           { mData: "entityTypeName"},		
	           { mData: "entityName"},	
	           { mData: "workflowStatusName"},	
	           { mData: "actors"},	
	           { mData: "completedBy"},	
	           { mData: "remainingHrsMins"},
	           { mData: "plannedSize"},
	           { mData: "actualSize"},
	           { mData: "plannedEffort"},
	           { mData: "totalEffort"},	
	           { 
		    	   mData: '',				 
		    	   sWidth: '2%',
				   "orderable":      false,
				   "data":           data,
	           	   mRender: function(data, type, full) {				            	
		       		var img = ('<div style="display: flex;">'+
		       		'<button style="border: none; background-color: transparent; outline: none;">'+
		       				'<i class="fa fa-history showHandCursor wp-details-control" title="Event History"></i></button>'+
		       		/*'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		       				'<img src="css/images/list_metro.png" class="wp-details-control2" title="Test Job(s)"></button>'+*/
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
			 $('#workflowSummary_dataTable tbody').on('click', 'td button .wp-details-control', function () {
			    	var tr = $(this).closest('tr');
			    	var row = workflowSummaryDT_oTable.DataTable().row(tr);
			    	showEventHistory(row);
		   		});
		});
	
	
	
	}
}






function listWorkflowSummarySLARAGDetails(engagementId,productId,productName,entityTypeId,indicator,userId,roleId,entityTypeInstanceId,activityCategoryId,titlename) {
	var entityTypeName = "";
	var tableHeader="";
	var showEntity=false;
	var entityId=0;
	var categoryId=activityCategoryId;
	if(entityTypeId == 30 ){
		entityTypeName = "Tasks";
		showEntity= true;
		tableHeader='Activity Tasks';
	}else if(entityTypeId == 3){
		entityTypeName = "Testcases";
		tableHeader='Testcases';
	}else if(entityTypeId == 33){
		entityTypeName = "Activities";
		tableHeader='Activities';
	}else if(entityTypeId == 34){
		entityTypeName = "Activity Workpackages";
		tableHeader='Activity Workpackages';
	}	
	
	if(engagementId != 0 && productId == 0) {
		entityTypeInstanceId=0;
	}
	
	if(typeof entityTypeInstanceId == 'undefined'){
		entityTypeInstanceId=0;
	}
	
	if(typeof categoryId == 'undefined'){
		categoryId=0;
	}
	
	/*jSingleTableInit(productName,entityTypeName);
	jSingleTableInit(productName,entityTypeName,titlename);*/
	var urlToGetSLARAGDetailView ="";
	if(userId ==0 && roleId == 0 && entityTypeInstanceId == 0) {		
		urlToGetSLARAGDetailView ='workflow.RAG.summary.SLA.indicator.detail?engagementId='+engagementId+'&productId='+productId+'&entityTypeId='+entityTypeId+'&indicator='+indicator+'&categoryId='+categoryId+'&jtStartIndex=0&jtPageSize=10000';
		assignDataTableValuesWorkflowRAG(urlToGetSLARAGDetailView);
	} else {
		urlToGetSLARAGDetailView ='workflow.RAG.status.instance.detail?engagementId='+engagementId+'&productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&entityTypeInstanceId='+entityTypeInstanceId+'&userId='+userId+'&roleId='+roleId+'&indicator='+indicator+'&categoryId='+categoryId+'&jtStartIndex=0&jtPageSize=10000';
		assignDataTableValuesWorkflowRAG(urlToGetSLARAGDetailView);
	}
			
	workflowSummaryRAGJsonData = '';
	
	function assignDataTableValuesWorkflowRAG(url){
		openLoaderIcon();
		 $.ajax({
			  type: "POST",
			  url:url,
			  contentType: "application/json; charset=utf-8",
			  dataType : 'json',
			  success : function(data) {		
				closeLoaderIcon();
				
				if(data.Result=="ERROR"){
	      		    data = [];						
				}else{
					data = data.Records;
				}
				workflowSummaryRAGJsonData = data;
				$("#div_SingleDataTableWorkflowSummary").modal();
				$("#div_SingleDataTableWorkflowSummary h4").empty();
				$("#div_SingleDataTableWorkflowSummary h4").text("Workflow Summary : "+productName+" Product's "+entityTypeName);
				 workflowSummaryRAGDT_Container(data);
			  },
			  error : function(data) {
				 closeLoaderIcon();  
			 },
			 complete: function(data){
				closeLoaderIcon();
			 }
		});	
	}
	
	
	function workflowSummaryRAGDataTable(){
		var plannedActivitySizeHeaderTitle="Planned Activity Size";
		var actualActivitySizeHeaderTitle="Actual Activity Size";
		if(customActivityListHeaderFieldsEnable == "YES") {
			plannedActivitySizeHeaderTitle="Estimated Savings";
			actualActivitySizeHeaderTitle="Realized Savings";
		}
		var childDivString = '<table id="workflowSummaryRAG_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead>'+          
			'<tr>'+
				'<th>Id</th>'+
				'<th>Name</th>'+
				'<th>Product</th>'+
				'<th>Entity Type</th>'+
				'<th>Entity</th>'+
				'<th>Current Status</th>'+
				'<th>Pending With</th>'+
				'<th>Complete By</th>'+
				'<th>Time Left</th>'+
				'<th>'+plannedActivitySizeHeaderTitle+'</th>'+
				'<th>'+actualActivitySizeHeaderTitle+'</th>'+
				'<th>Planned Effort</th>'+
				'<th>Actual Effort</th>'+
				'<th></th>'+
			'</tr>'+
		'</thead>'+
		'</table>';		
		
		return childDivString;	
	}

	var workflowSummaryRAGDT_oTable='';
	
	
	function workflowSummaryRAGDT_Container(data){
		
		try{
			if ($('#dataTableSingleWorkflowSummaryContainer').length>0) {
				$('#dataTableSingleWorkflowSummaryContainer').empty();
			}
		} 
		catch(e) {}
		
		var childDivString = workflowSummaryRAGDataTable(); 			 
		$("#dataTableSingleWorkflowSummaryContainer").append(childDivString);
		
		workflowSummaryRAGDT_oTable = $("#workflowSummaryRAG_dataTable").dataTable({
			 	"dom": '<"top"Bf<"clear">>rt<"bottom"ip<"clear">>',
				paging: true,	    			      				
				destroy: true,
				searching: true,
				bJQueryUI: false,
			    "sScrollX": "100%",
		       "sScrollXInner": "100%",
		       "scrollY":"100%",
		       "bScrollCollapse": true,	       
		       "fnInitComplete": function(data) {
		    	  // reInitializeDTAuditHistory();
			   },  
			   buttons: [
				         {
			                extend: 'collection',
			                text: 'Export',
			                buttons: [
			                    {
			                    	extend: 'excel',
			                    	title: 'RAG Summary',
			                    	exportOptions: {
			                            columns: ':visible'
			                        }
			                    },
			                    {
			                    	extend: 'csv',
			                    	title: 'RAG Summary',
			                    	exportOptions: {
			                            columns: ':visible'
			                        }
			                    },
			                    {
			                    	extend: 'pdf',
			                    	title: 'RAG Summary',
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
		         columnDefs: [
		              //{ targets: [0,1,2,3,4,5,6,7,8,9,10,11,12], visible: true},
		              //{ targets: '_all', visible: false }
		          ],
	        aaData:data,		    				 
		    aoColumns: [	                      	        
		 	        //   { mData: "instanceId"},
			 	        { mData: "instanceId", sWidth: '5%', "render": function (tcData,type,full) {	        
			 	        		var entityInstanceId = full.instanceId;					
			 	        		return ('<a onclick="displayTabActivitySummaryHandler('+entityInstanceId+')">'+full.instanceId+'</a>');		        
			 	        	},
			 	        },
			           { mData: "instanceName"},		
			           { mData: "productName"},		
			           { mData: "entityTypeName"},		
			           { mData: "entityName"},	
			           { mData: "workflowStatusName"},	
			           { mData: "actors"},	
			           { mData: "completedBy"},	
			           { mData: "remainingHrsMins"},
			           { mData: "plannedSize"},
			           { mData: "actualSize"},
			           { mData: "plannedEffort"},
			           { mData: "totalEffort"},	
			           { 
				    	   mData: '',				 
				    	   sWidth: '2%',
						   "orderable":      false,
						   "data":           data,
			           	   mRender: function(data, type, full) {				            	
				       		var img = ('<div style="display: flex;">'+
				       		'<button style="border: none; background-color: transparent; outline: none;">'+
				       				'<i class="fa fa-history showHandCursor wp-details-control" title="Event History"></i></button>'+
				       		/*'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
				       				'<img src="css/images/list_metro.png" class="wp-details-control2" title="Test Job(s)"></button>'+*/
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
			 $('#workflowSummaryRAG_dataTable tbody').on('click', 'td button .wp-details-control', function () {
			    	var tr = $(this).closest('tr');
			    	var row = workflowSummaryRAGDT_oTable.DataTable().row(tr);
			    	showEventHistory(row);
		   		});
		});
	
	}

	
	
	
			
}

