var wfSummaryProductId;
var wfSummaryProductName;
var wfSummaryEntityTypeId;
var wfSummaryEntityTypeName;
var wfSummaryentityId;
var wfSummaryentityInstanceId;
var wfSummaryProductVersionId;
var wfSummaryProductBuildId;
var wfSummaryActivityWorkPackageId;
var wfSummarydashBoardDivId;
var typeFiledDisplay=true;
var entityParentInstanceId;
var engagementId=0; // dashboard grid no need  for temp
function showDashBoardWorkflowEntityInstanceStatusSummary(dashBoardDivId,dashBoardHeadTitle,testFactoryId,productId,productVersionId,productBuildId,activityWorkPackageId, productName, entityTypeId, entityTypeName, entityId, entityInstanceId){
	wfSummaryProductId = productId;
	wfSummaryProductName = productName;
	wfSummaryEntityTypeId = entityTypeId;
	wfSummaryEntityTypeName = entityTypeName;
	wfSummaryEntityId = entityId;
	wfSummaryEntityInstanceId = entityInstanceId;
	wfSummaryProductVersionId=productVersionId;
	wfSummaryProductBuildId=productBuildId;
	wfSummaryActivityWorkPackageId=activityWorkPackageId;
	wfSummarydashBoardDivId=dashBoardDivId;
	engagementId=testFactoryId;
	$("#"+dashBoardDivId).find(".jScrollContainerResponsiveTop").empty();
	if(wfSummaryEntityTypeId == 34) {
		typeFiledDisplay=false;
	}
	var jsonObj={};
	
	if(productId!=0 && activityWorkPackageId ==0) {
		jsonObj={
				"Title":"Workflow Summary: "+productName+" Products",
		};
	} else if(productId != 0 && activityWorkPackageId!=0) {
		jsonObj={
				"Title":"Workflow Summary : "+wfSummaryProductName+" Workpackage "+wfSummaryEntityTypeName,
		};
	}	
	
	listDashBoardWorkflowInidcatorySummary(dashBoardDivId,dashBoardHeadTitle);
	
	listDashBoardWorkflowEntityInstanceTypeSummary(dashBoardDivId,dashBoardDivId+'InstanceTypeSummary');
	listDashBoardWorkflowEntityInstanceStatusSummary(dashBoardDivId,dashBoardDivId+'InstanceStatusSummary');
	listDashBoardWorkflowEntityInstanceStatusUserSummary(dashBoardDivId,dashBoardDivId+'InstanceStatusUserSummary');	
	listDashBoardWorkflowEntityInstanceCategorySummary(dashBoardDivId,dashBoardDivId+'InstanceStatusCategorySummary');	
	
	//$("#"+dashBoardDivId+".modal-header .row").css('display','none');    
}

function listDashBoardWorkflowInidcatorySummary(dashBoardDivId,dashBoardHeadTitle){
	var indicatorSummaryURL = 'workflow.status.indicator.product.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+"&entityParentInstanceId="+wfSummaryEntityInstanceId;
	$.post(indicatorSummaryURL, function(data) {
		if(data.Result == 'OK' || data.Result == 'Ok'){
			var indicatorSummary = '<span style="font-weight:bolder;">SLA </span><span style="padding:10px"><i class="" style="color: blue;" title="Total Workflow Status"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'blue\')">[0]</a></span>:<span style="padding:10px"><i class="fa fa-circle" style="color: red;" title="SLA duration elapsed"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'red\')">0</a></span>|<span style="padding:10px"><i class="fa fa-circle" style="color: orangered;" title="Needs immediate action"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'orangered\')">0</a></span>|<span style="padding:10px"><i class="fa fa-circle" style="color: orange;" title="Needs immediate attention"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'orange\')">0</a></span>|<span style="padding:10px"><i class="fa fa-circle" style="color: green;" title="Availble for action"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'green\')">0</a></span>';
			if(typeof data.Records != 'undefined' && data.Records.length > 0){
				var indicatorDetails = data.Records[0];
				indicatorSummary = '<span style="font-weight:bolder;">SLA </span><span style="padding:10px"><i class="" style="color: blue;" title="Total Workflow Status"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'blue\')">['+indicatorDetails["totalWorkflowStatusCount"]+']</a></span>:<span style="padding:10px"><i class="fa fa-circle" style="color: red;" title="SLA duration elapsed"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'red\')">'+indicatorDetails["red"]+'</a></span>|<span style="padding:10px"><i class="fa fa-circle" style="color: orangered;" title="Needs immediate action"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'orangered\')">'+indicatorDetails["orangered"]+'</a></span>|<span style="padding:10px"><i class="fa fa-circle" style="color: orange;" title="Needs immediate attention"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'orange\')">'+indicatorDetails["orange"]+'</a></span>|<span style="padding:10px"><i class="fa fa-circle" style="color: green;" title="Availble for action"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'green\')">'+indicatorDetails["green"]+'</a></span>';
			}
		$('#'+dashBoardHeadTitle).html(indicatorSummary);	
		
		}else{
			callAlert(data.Message);
		}
	});
}

// ----- In workflow Activities -----

function listDashBoardWorkflowEntityInstanceTypeSummary(dashBoardDivId,containerId){	
	var urlToListWorkflowEntityInstanceTypeSummary = 'workflow.entity.instance.type.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+"&entityParentInstanceId="+wfSummaryEntityInstanceId;	
	listSummaryView(urlToListWorkflowEntityInstanceTypeSummary, "workflowInstanceTypeSummary", dashBoardDivId, containerId)
}

function clearDynamicJTableDatas(dashBoardDivId){
	$('#'+dashBoardDivId).find(".jScrollContainerResponsiveTop").empty();
}

function showDashBoardEntityInstanceRAGSummary(dashBoardDivId,dashBoardHeadTitle,testFactoryId,productId,productVersionId,productBuildId,activityWorkPackageId, productName, entityTypeId, entityTypeName, entityId, entityInstanceId){
	wfSummaryProductId = productId;
	wfSummaryProductName = productName;
	wfSummaryEntityTypeId = entityTypeId;
	wfSummaryEntityTypeName = entityTypeName;
	wfSummaryEntityId = entityId;
	wfSummaryEntityInstanceId = entityInstanceId;
	wfSummaryProductVersionId=productVersionId;
	wfSummaryProductBuildId=productBuildId;
	wfSummaryActivityWorkPackageId=activityWorkPackageId;
	wfSummarydashBoardDivId=dashBoardDivId;
	engagementId=testFactoryId;
	entityParentInstanceId=entityInstanceId;
	clearDynamicJTableDatas(dashBoardDivId);
	
	listWorkflowInidcatorDashboardRAGSummary(dashBoardHeadTitle);		
	lisRAGViewProductSummary(wfSummarydashBoardDivId, dashBoardDivId+'ProductRAGSummary');
	lisDashBoardRAGViewActivityCategorySummary(wfSummarydashBoardDivId,dashBoardDivId+'ActivityRAGSummary');
		
	var jsonObj={};
	if(productId != 0 && activityWorkPackageId==0) {
		jsonObj={
				"Title":"RAG Summary : "+wfSummaryProductName+" Product",
		};		
	} else if(productId != 0 && activityWorkPackageId!=0) {
		jsonObj={
				"Title":"RAG Summary : "+wfSummaryProductName+" Workpackage "+wfSummaryEntityTypeName,
		};		
	} 
	
	if(engagementId != 0 || productId != 0 ) {		
		listRAGViewResourceTemplateSummary(wfSummarydashBoardDivId,dashBoardDivId+'ResourceRAGSummary');
	}	
}


function listWorkflowInidcatorDashboardRAGSummary(dashBoardHeadTitle){
	var entityParentInstanceId=0;
	if (wfSummaryEntityTypeId == 34) {
		entityParentInstanceId=wfSummaryProductBuildId;
	} else if(wfSummaryEntityTypeId == 33){
		entityParentInstanceId=wfSummaryActivityWorkPackageId;
	} 
//	var indicatorSummaryURL = 'workflow.RAG.indicator.product.SLA.summary?productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+entityParentInstanceId;
	var indicatorSummaryURL = 'workflow.RAG.indicator.product.SLA.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+entityParentInstanceId;
	$.post(indicatorSummaryURL, function(data) {
		if(data.Result == 'OK' || data.Result == 'Ok'){
			var indicatorSummary = '<span style="font-weight:bolder;">Summary</span><span style="padding:10px"><i class="" style="color: Blue;" title="Total RAG Summary"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailRAGView(\'blue\')">[0]</a></span>:<span style="padding:10px"><i class="fa fa-square" style="color: red;" title="Delayed"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'red\')">0</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: orangered;" title="Nearing End date"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'orangered\')">0</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: orange;" title="Nearing End date"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'orange\')">0</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: green;" title="In Progress"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'green\')">0</a></span>';
			if(typeof data.Records != 'undefined' && data.Records.length > 0){
				var indicatorDetails = data.Records[0];
				indicatorSummary = '<span style="font-weight:bolder;">Summary</span><span style="padding:10px"><i class="" style="color: Blue;" title="Total RAG Summary"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailRAGView(\'blue\')">['+indicatorDetails["totalRAGCount"]+']</a></span>:<span style="padding:10px"><i class="fa fa-square" style="color: red;" title="Delayed"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailRAGView(\'red\')">'+indicatorDetails["red"]+'</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: orange;" title="Nearing End date"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailRAGView(\'orange\')">'+indicatorDetails["orange"]+'</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: green;" title="In Progress"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailRAGView(\'green\')">'+indicatorDetails["green"]+'</a></span>|<span style="padding:10px"><i class="fa fa-check" style="color: darkgreen;" title="Completed"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailRAGView(\'darkgreen\')">'+indicatorDetails["completedCount"]+'</a></span>|<span style="padding:10px"><i class="fa fa-times" style="color: darkRed;" title="Aborted"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailRAGView(\'darkRed\')">'+indicatorDetails["abortCount"]+'</a></span>';
			}
			$('#'+dashBoardHeadTitle).html(indicatorSummary);			
			
		}else{
			callAlert(data.Message);
		}
	});
}

function listSummaryView(url, tableValue, dashBoardDivId, containerId){
	openLoaderIcon();
	
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();		
			
			if(data == null || data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			
			if(tableValue == "product"){										
				listRAGViewProductSummaryView(data, dashBoardDivId, containerId);						
				
			}else if(tableValue == "ActivityCategory"){
				listRAGViewActivitySummaryView(data, dashBoardDivId, containerId);			
				
			}else if(tableValue == "ResourceTemplate"){
				listRAGViewResourceTemplateView(data, dashBoardDivId, containerId);
				
			}else if(tableValue == "workflowInstanceTypeSummary"){
				listWorkflowInstanceTypeSummaryView(data, dashBoardDivId, containerId);			
				
			}else if(tableValue == "workflowInstanceStatusSummary"){
				listWorkflowInstanceStatusSummaryView(data, dashBoardDivId, containerId);		
								
			}else if(tableValue == "workflowInstanceUserSummary"){
				listWorkflowInstanceUserSummaryView(data, dashBoardDivId, containerId);
										
			}else if(tableValue == "workflowInstanceCategoryrSummary"){
				listWorkflowInstanceCategorySummaryView(data, dashBoardDivId, containerId);
				
			}			
			else{
				console.log("no child");
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

function lisRAGViewProductSummary(dashBoardDivId,containerId){
	var urlToLisRAGViewProductSummary = 'workflow.RAG.indicator.engagement.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+wfSummaryActivityWorkPackageId;
	
	listSummaryView(urlToLisRAGViewProductSummary, "product", dashBoardDivId, containerId);
}

function lisDashBoardRAGViewActivityCategorySummary(dashBoardDivId,containerId){	
	var entityTypeTitle="Category Summary";
	
	var urlToLisRAGViewActivityCategorySummary = 'workflow.RAG.indicator.activity.category.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+wfSummaryActivityWorkPackageId;	
	listSummaryView(urlToLisRAGViewActivityCategorySummary, "ActivityCategory", dashBoardDivId, containerId);
}

function listRAGViewResourceTemplateSummary(dashBoardDivId,containerId){
	var urlToListWorkflowEntityInstanceResourceTemplateSummary = 'workflow.RAG.indicator.product.resource.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+"&entityParentInstanceId="+wfSummaryActivityWorkPackageId;
	
	listSummaryView(urlToListWorkflowEntityInstanceResourceTemplateSummary, "ResourceTemplate", dashBoardDivId, containerId);
}

function listDashBoardWorkflowEntityInstanceStatusSummary(dashBoardDivId, containerId){	
	var urlToListWorkflowEntityInstanceStatusSummary = 'workflow.entity.instance.status.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityId='+ wfSummaryEntityId+'&entityParentInstanceId='+ wfSummaryEntityInstanceId;	
	listSummaryView(urlToListWorkflowEntityInstanceStatusSummary, "workflowInstanceStatusSummary", dashBoardDivId, containerId);
}

function listDashBoardWorkflowEntityInstanceStatusUserSummary(dashBoardDivId, containerId){
	var urlToListWorkflowEntityInstanceUserSummary = 'workflow.entity.instance.status.actor.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityId='+ wfSummaryEntityId+'&entityInstanceId='+ wfSummaryEntityInstanceId+'&actorType=User';
	
	listSummaryView(urlToListWorkflowEntityInstanceUserSummary, "workflowInstanceUserSummary", dashBoardDivId, containerId);
}

function listDashBoardWorkflowEntityInstanceCategorySummary(dashBoardDivId, containerId){
		var urlToListWorkflowEntityInstanceStatusCategorySummary = 'workflow.entity.instance.status.category.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityId='+ wfSummaryEntityId+'&entityParentInstanceId='+ wfSummaryEntityInstanceId;
		
		listSummaryView(urlToListWorkflowEntityInstanceStatusCategorySummary, "workflowInstanceCategoryrSummary", dashBoardDivId, containerId);
}

// ----- WorkflowInstanceUserSummary -----

var listRAGViewWorkflowInstanceCategorySummary_oTable='';
var clearTimeoutDTRAGViewWorkflowInstanceCategorySummary=''
function reInitializeDTWorkflowInstanceCategorySummary(){
	clearTimeoutDTRAGViewWorkflowInstanceCategorySummary = setTimeout(function(){				
		listRAGViewWorkflowInstanceCategorySummary_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTRAGViewWorkflowInstanceCategorySummary);
	},200);
}

function ragViewWorkflowInstanceCategorySummaryHeader(){		
	var tr = '<tr>'+			
		'<th>Type</th>'+
		'<th>Category</th>'+		
		'<th>Count</th>'+			
	'</tr>';
	return tr;
}

function listWorkflowInstanceCategorySummaryView(data,dashBoardDivId,containerId){
	try{
		if ($('#'+dashBoardDivId+' #InWorkflowInstanceCategorySummary').children().length>0) {
			$('#'+dashBoardDivId+' #InWorkflowInstanceCategorySummary').children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(3);  	  
	  var childDivString = '<table id="'+containerId+'" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $('#'+dashBoardDivId+' #InWorkflowInstanceCategorySummary').append('<h5 class="caption-subject theme-font" style="text-decoration: underline;">Status Category Summary :</h5>');	
	  $('#'+dashBoardDivId+' #InWorkflowInstanceCategorySummary').append(childDivString); 						  
	  
	  $('#'+containerId+' thead').html('');
	  $('#'+containerId+' thead').append(ragViewWorkflowInstanceCategorySummaryHeader());
	  
	  $('#'+containerId+' tfoot tr').html('');     			  
	  $('#'+containerId+' tfoot tr').append(emptytr);
				
	listRAGViewWorkflowInstanceCategorySummary_oTable = $('#'+containerId+'').dataTable( {
		 	dom: "Bfrtilp",
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY": "100%",
	       //"bSort": false,
		    select: false,		   
	       "bScrollCollapse": true,
	       "aaSorting": [[2,'desc']],
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex = [3]; // search column TextBox Invisible Column position
     		  $('#'+containerId+'_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			   reInitializeDTWorkflowInstanceCategorySummary();			   
		   },  
		   select: true,
		   buttons: [	             	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "WorkflowInstanceCategorySummary",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "WorkflowInstanceCategorySummary",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "WorkflowInstanceCategorySummary",
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
           { mData: "typeName",className: 'disableEditInline', sWidth: '3%' },           
		   { mData: "workflowStatusCategoryName",className: 'disableEditInline', sWidth: '3%' },           		         
		   { mData: "workflowStatusCount",				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="WorkflowInstanceCategorySummaryImg1" style="color: blue;" title="Delayed">'+data+'</i></button>'+							
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
		 	 
		 $('#'+containerId+'_length').css('margin-top','8px');
		 $('#'+containerId+'_length').css('padding-left','35px');
		 				
		listRAGViewWorkflowInstanceCategorySummary_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() != this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );
			
		// ----- Delayed -----
		 
		  $('#'+containerId+' tbody').on('click', 'td button .WorkflowInstanceCategorySummaryImg1', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewWorkflowInstanceCategorySummary_oTable.DataTable().row(tr);									

				viewWorkflowDashBoradStatusCategoryBasedDetail(row.data().workflowStatusCategoryId, row.data().entityId);	
		 });
		 		   
	});
}

// ----- WorkflowInstanceUserSummary -----

var listRAGViewWorkflowInstanceUserSummary_oTable='';
var clearTimeoutDTRAGViewWorkflowInstanceUserSummary=''
function reInitializeDTWorkflowInstanceUserSummary(){
	clearTimeoutDTRAGViewWorkflowInstanceUserSummary = setTimeout(function(){				
		listRAGViewWorkflowInstanceUserSummary_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTRAGViewWorkflowInstanceUserSummary);
	},200);
}

function ragViewWorkflowInstanceUserSummaryHeader(){		
	var tr = '<tr>'+			
		'<th>Type</th>'+
		'<th>Status</th>'+
		'<th>User Name</th>'+
		'<th>Count</th>'+			
	'</tr>';
	return tr;
}

function listWorkflowInstanceUserSummaryView(data,dashBoardDivId,containerId){
	try{
		if ($('#'+dashBoardDivId+' #InWorkflowInstanceUserSummary').children().length>0) {
			$('#'+dashBoardDivId+' #InWorkflowInstanceUserSummary').children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(4);  	  
	  var childDivString = '<table id="'+containerId+'" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';			
	  $('#'+dashBoardDivId+' #InWorkflowInstanceUserSummary').append('<h5 class="caption-subject theme-font" style="text-decoration: underline;">User Queue :</h5>');		
	  $('#'+dashBoardDivId+' #InWorkflowInstanceUserSummary').append(childDivString); 						  
	  
	  $('#'+containerId+' thead').html('');
	  $('#'+containerId+' thead').append(ragViewWorkflowInstanceUserSummaryHeader());
	  
	  $('#'+containerId+' tfoot tr').html('');     			  
	  $('#'+containerId+' tfoot tr').append(emptytr);
				
	listRAGViewWorkflowInstanceUserSummary_oTable = $('#'+containerId+'').dataTable( {
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
			  var searchcolumnVisibleIndex = [3]; // search column TextBox Invisible Column position
     		  $('#'+containerId+'_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			   reInitializeDTWorkflowInstanceUserSummary();			   
		   },  
		   select: true,
		   buttons: [	             	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "WorkflowInstanceUserSummary",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "WorkflowInstanceUserSummary",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "WorkflowInstanceUserSummary",
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
           { mData: "typeName",className: 'disableEditInline', sWidth: '3%' },           
		   { mData: "workflowStatus",className: 'disableEditInline', sWidth: '3%' },           
		   { mData: "actorName",className: 'disableEditInline', sWidth: '3%' },           
		   { mData: "instanceCount",				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="WorkflowInstanceUserSummaryImg1" style="color: blue;" title="Delayed">'+data+'</i></button>'+							
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
		 	 
		 $('#'+containerId+'_length').css('margin-top','8px');
		 $('#'+containerId+'_length').css('padding-left','35px');
		 				
		listRAGViewWorkflowInstanceUserSummary_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() != this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );
			
		// ----- Delayed -----
		 
		  $('#'+containerId+' tbody').on('click', 'td button .WorkflowInstanceUserSummaryImg1', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewWorkflowInstanceUserSummary_oTable.DataTable().row(tr);									

				viewWorkflowDashBoradUserBasedDetail(row.data().workflowStatusId, row.data().entityId, row.data().userId);	
		 });
		 		   
	});
}

// ----- WorkflowInstanceStatusSummary -----

var listRAGViewWorkflowInstanceStatusSummary_oTable='';
var clearTimeoutDTRAGViewWorkflowInstanceStatusSummary=''
function reInitializeDTWorkflowInstanceStatusSummary(){
	clearTimeoutDTRAGViewWorkflowInstanceStatusSummary = setTimeout(function(){				
		listRAGViewWorkflowInstanceStatusSummary_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTRAGViewWorkflowInstanceStatusSummary);
	},200);
}

function ragViewWorkflowInstanceStatusSummaryHeader(){		
	var tr = '<tr>'+			
		'<th>Type</th>'+
		'<th>Status</th>'+
		'<th>Count</th>'+			
	'</tr>';
	return tr;
}

function listWorkflowInstanceStatusSummaryView(data,dashBoardDivId,containerId){
	try{
		if ($('#'+dashBoardDivId+' #InWorkflowInstanceStatusSummary').children().length>0) {
			$('#'+dashBoardDivId+' #InWorkflowInstanceStatusSummary').children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(3);  	  
	  var childDivString = '<table id="'+containerId+'" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $('#'+dashBoardDivId+' #InWorkflowInstanceStatusSummary').append('<h5 class="caption-subject theme-font" style="text-decoration: underline;">Status Summary :</h5>');	
	  $('#'+dashBoardDivId+' #InWorkflowInstanceStatusSummary').append(childDivString); 						  
	  
	  $('#'+containerId+' thead').html('');
	  $('#'+containerId+' thead').append(ragViewWorkflowInstanceStatusSummaryHeader());
	  
	  $('#'+containerId+' tfoot tr').html('');     			  
	  $('#'+containerId+' tfoot tr').append(emptytr);
				
	listRAGViewWorkflowInstanceStatusSummary_oTable = $('#'+containerId+'').dataTable( {
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
			  var searchcolumnVisibleIndex = [2]; // search column TextBox Invisible Column position
     		  $('#'+containerId+'_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			   reInitializeDTWorkflowInstanceStatusSummary();			   
		   },  
		   select: true,
		   buttons: [	             	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "WorkflowInstanceStatusSummary",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "WorkflowInstanceStatusSummary",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "WorkflowInstanceStatusSummary",
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
           { mData: "typeName",className: 'disableEditInline', sWidth: '3%' },           
		   { mData: "workflowStatus",className: 'disableEditInline', sWidth: '3%' },           
		   { mData: "workflowStatusCount",				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="WorkflowInstanceStatusSummaryImg1" style="color: blue;" title="Delayed">'+data+'</i></button>'+							
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
		 	 
		 $('#'+containerId+'_length').css('margin-top','8px');
		 $('#'+containerId+'_length').css('padding-left','35px');
		 				
		listRAGViewWorkflowInstanceStatusSummary_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() != this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );
			
		// ----- Delayed -----
		 
		  $('#'+containerId+' tbody').on('click', 'td button .WorkflowInstanceStatusSummaryImg1', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewWorkflowInstanceStatusSummary_oTable.DataTable().row(tr);									

				viewWorkflowDashBoradStatusBasedDetail(row.data().workflowStatusId, row.data().entityId);	
		 });
		 		   
	});
}

// ----- WorkflowInstanceTypeSummar -----

var listRAGViewWorkflowInstanceTypeSummary_oTable='';
var clearTimeoutDTRAGViewWorkflowInstanceTypeSummary=''
function reInitializeDTWorkflowInstanceTypeSummary(){
	clearTimeoutDTRAGViewWorkflowInstanceTypeSummary = setTimeout(function(){				
		listRAGViewWorkflowInstanceTypeSummary_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTRAGViewWorkflowInstanceTypeSummary);
	},200);
}

function ragViewWorkflowInstanceTypeSummaryHeader(){		
	var tr = '<tr>'+			
		'<th>Type</th>'+
		'<th>Count</th>'+			
	'</tr>';
	return tr;
}

var workflowContainerID='';
function listWorkflowInstanceTypeSummaryView(data,dashBoardDivId,containerId){
	workflowContainerID = containerId;
	try{
		if ($('#'+dashBoardDivId+' #InWorkflowInstanceTypeSummary').children().length>0) {
			$('#'+dashBoardDivId+' #InWorkflowInstanceTypeSummary').children().remove();
		}
	} 
	catch(e) {}
	
	var entityTypeTitle="";
	if(wfSummaryEntityTypeId == 30) {
		entityTypeTitle="Task Life Cycle";
		typeName ="Task Type";
	} else if(wfSummaryEntityTypeId == 33) {
		entityTypeTitle="Activities";
		typeName ="Activity Type";
	} else if(wfSummaryEntityTypeId == 34) {
		entityTypeTitle="Workpackage Life Cycle";
		typeName ="Type";
	} else if(wfSummaryEntityTypeId == 3) {
		entityTypeTitle="Testcase Life Cycle";
		typeName ="Type";
	} 
	
	  var emptytr = emptyTableRowAppending(2);  	  
	  var childDivString = '<table id="'+containerId+'" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';
	  $('#'+dashBoardDivId+' #InWorkflowInstanceTypeSummary').append('<h5 class="caption-subject theme-font" style="text-decoration: underline;">'+entityTypeTitle+':</h5>');		
	  $('#'+dashBoardDivId+' #InWorkflowInstanceTypeSummary').append(childDivString); 						  
	  
	  $('#'+containerId+' thead').html('');
	  $('#'+containerId+' thead').append(ragViewWorkflowInstanceTypeSummaryHeader());
	  
	  $('#'+containerId+' tfoot tr').html('');     			  
	  $('#'+containerId+' tfoot tr').append(emptytr);
				
	listRAGViewWorkflowInstanceTypeSummary_oTable = $('#'+containerId+'').dataTable( {
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
			  var searchcolumnVisibleIndex = [1]; // search column TextBox Invisible Column position
     		  $('#'+containerId+'_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			   reInitializeDTWorkflowInstanceTypeSummary();			   
		   },  
		   select: true,
		   buttons: [	             	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "WorkflowInstanceTypeSummary",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "WorkflowInstanceTypeSummary",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "WorkflowInstanceTypeSummary",
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
           { mData: "typeName",className: 'disableEditInline', sWidth: '3%' },           
		   { mData: "typeCount",				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+   						
						'<i class="WorkflowInstanceTypeSummaryImg1" style="color: blue;" title="Delayed">'+data+'</i></button>'+							
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
		 	 
		 $('#'+workflowContainerID+'_length').css('margin-top','8px');
		 $('#'+workflowContainerID+'_length').css('padding-left','35px');
		 				
		listRAGViewWorkflowInstanceTypeSummary_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() != this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );
			
		// ----- Delayed -----
		 
		  $('#'+workflowContainerID+' tbody').on('click', 'td .WorkflowInstanceTypeSummaryImg1', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewWorkflowInstanceTypeSummary_oTable.DataTable().row(tr);									

				viewWorkflowDashBoradTypeBasedDetail(row.data().typeId);	
		 });
		 		   
	});
}

// ----- ResourceTemplate -----

var listRAGViewResourceTemplate_oTable='';
var clearTimeoutDTRAGViewResourceTemplate=''
function reInitializeDTRAGViewResourceTemplate(){
	clearTimeoutDTRAGViewResourceTemplate = setTimeout(function(){				
		listRAGViewResourceTemplate_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTRAGViewResourceTemplate);
	},200);
}

function ragViewResourceTemplateHeader(){		
	var tr = '<tr>'+			
		'<th>Resource</th>'+
		'<th><i class="fa fa-square" style="color: red;" title="Delayed"></i></th>'+
		'<th><i class="fa fa-square" style="color: orange;" title="Nearing End date"></i></th>'+
		'<th><i class="fa fa-square" style="color: green;" title="In Progress"></i></th>'+		
		'<th><i class="fa fa-check" style="color: darkgreen;" title="Completed"></i></th>'+
		'<th><i class="fa fa-times" style="color: darkRed;" title="Aborted"></i></th>'+		
	'</tr>';
	return tr;
}

function listRAGViewResourceTemplateView(data,dashBoardDivId,containerId){
	try{
		if ($('#'+dashBoardDivId+' #InResourceTemplate').children().length>0) {
			$('#'+dashBoardDivId+' #InResourceTemplate').children().remove();
		}
	} 
	catch(e) {}
	
	var entityTypeTitle="";
	if(wfSummaryEntityTypeId == 30) {
		entityTypeTitle="Resource Tasks Queue";
	} else if(wfSummaryEntityTypeId == 33) {
		entityTypeTitle="Resource Activities Queue";
	} else if(wfSummaryEntityTypeId == 34) {
		entityTypeTitle="Resource Workpackage Queue";
	} else if(wfSummaryEntityTypeId == 3) {
		entityTypeTitle="Resource  Testcases Queue";
	}
	
	  var emptytr = emptyTableRowAppending(6);  	  
	  var childDivString = '<table id="'+containerId+'" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';		
	  $('#'+dashBoardDivId+' #InResourceTemplate').append('<h5 class="caption-subject theme-font" style="text-decoration: underline;">'+entityTypeTitle+'</h5>');		
	  $('#'+dashBoardDivId+' #InResourceTemplate').append(childDivString); 						  
	  
	  $('#'+containerId+' thead').html('');
	  $('#'+containerId+' thead').append(ragViewResourceTemplateHeader());
	  
	  $('#'+containerId+' tfoot tr').html('');     			  
	  $('#'+containerId+' tfoot tr').append(emptytr);
				
	listRAGViewResourceTemplate_oTable = $('#'+containerId+'').dataTable( {
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
			  var searchcolumnVisibleIndex = [1,2,3,4,5]; // search column TextBox Invisible Column position
     		  $('#'+containerId+'_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			   reInitializeDTRAGViewResourceTemplate();			   
		   },  
		   select: true,
		   buttons: [	             	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "ResourceTemplate",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "ResourceTemplate",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "ResourceTemplate",
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
           { mData: "userName",className: 'disableEditInline', sWidth: '3%' },           
		   { mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="ResourceTemplateSummaryImg1" style="color: blue;" title="Delayed">'+data.red+'</i></button>'+							
     	       		'</div>');	      		
           		 return img;
            	}
            },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+   						
					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="ResourceTemplateSummaryImg2" style="color: blue;" title="Nearing End date">'+data.orange+'</i></button>'+								
     	       		'</div>');	      		
           		 return img;
            	}
            },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="ResourceTemplateSummaryImg3" style="color: blue;" title="Completed">'+data.green+'</i></button>'+								
     	       		'</div>');	      		
           		 return img;
            	}
            },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="ResourceTemplateSummaryImg4" style="color: blue;" title="Aborted">'+data.completedCount+'</i></button>'+									
     	       		'</div>');	      		
           		 return img;
            	}
            },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="ResourceTemplateSummaryImg5" style="color: blue;" title="Delayed">'+data.abortCount+'</i></button>'+								
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
		 	 
		 $('#'+containerId+'_length').css('margin-top','8px');
		 $('#'+containerId+'_length').css('padding-left','35px');
		 				
		listRAGViewResourceTemplate_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() != this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );
			
		// ----- Delayed -----
		 
		  $('#'+containerId+' tbody').on('click', 'td button .ResourceTemplateSummaryImg1', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewResourceTemplate_oTable.DataTable().row(tr);									

				viewRAGStatusResourceBasedDetail(row.data().userId, row.data().roleId, 'red');	
		 });
		 
		 // ----- Nearing End date -----

		 $('#'+containerId+' tbody').on('click', 'td button .ResourceTemplateSummaryImg2', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewResourceTemplate_oTable.DataTable().row(tr);									

				viewRAGStatusResourceBasedDetail(row.data().userId, row.data().roleId, 'orange');	
		 });
			
		 // ----- In Progress -----

		 $('#'+containerId+' tbody').on('click', 'td button .ResourceTemplateSummaryImg3', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewResourceTemplate_oTable.DataTable().row(tr);									

				viewRAGStatusResourceBasedDetail(row.data().userId, row.data().roleId, 'green');	
		 });

		 // ----- Completed -----

		 $('#'+containerId+' tbody').on('click', 'td button .ResourceTemplateSummaryImg4', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewResourceTemplate_oTable.DataTable().row(tr);									

				viewRAGStatusResourceBasedDetail(row.data().userId, row.data().roleId, 'darkgreen');	
		 });

		 // ----- Aborted -----

		 $('#'+containerId+' tbody').on('click', 'td button .ResourceTemplateSummaryImg5', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewResourceTemplate_oTable.DataTable().row(tr);									

				viewRAGStatusResourceBasedDetail(row.data().userId, row.data().roleId, 'darkRed');	
		 });	
		   
	});
}

// ----- CategoryRAGSummary -----

var listRAGViewCategorySummary_oTable='';
var clearTimeoutDTRAGViewCategorySummary=''
function reInitializeDTRAGViewCategorySummary(){
	clearTimeoutDTRAGViewCategorySummary = setTimeout(function(){				
		listRAGViewCategorySummary_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTRAGViewCategorySummary);
	},200);
}

function ragViewCategorySummaryHeader(){		
	var tr = '<tr>'+			
		'<th>Product</th>'+
		'<th>Workpackage</th>'+
		'<th>Category</th>'+
		'<th><i class="fa fa-square" style="color: red;" title="Delayed"></i></th>'+
		'<th><i class="fa fa-square" style="color: orange;" title="Nearing End date"></i></th>'+
		'<th><i class="fa fa-square" style="color: green;" title="In Progress"></i></th>'+		
		'<th><i class="fa fa-check" style="color: darkgreen;" title="Completed"></i></th>'+
		'<th><i class="fa fa-times" style="color: darkRed;" title="Aborted"></i></th>'+		
	'</tr>';
	return tr;
}

function listRAGViewActivitySummaryView(data,dashBoardDivId,containerId){
	try{
		if ($('#'+dashBoardDivId+' #InActivitySummary').children().length>0) {
			$('#'+dashBoardDivId+' #InActivitySummary').children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(8);  	  
	  var childDivString = '<table id="'+containerId+'" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $('#'+dashBoardDivId+' #InActivitySummary').append('<h5 class="caption-subject theme-font" style="text-decoration: underline;">Category Summary :</h5>');	 
	  $('#'+dashBoardDivId+' #InActivitySummary').append(childDivString); 						  
	  
	  $('#'+containerId+' thead').html('');
	  $('#'+containerId+' thead').append(ragViewCategorySummaryHeader());
	  
	  $('#'+containerId+' tfoot tr').html('');     			  
	  $('#'+containerId+' tfoot tr').append(emptytr);
				
	listRAGViewCategorySummary_oTable = $('#'+containerId+'').dataTable( {
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
			  var searchcolumnVisibleIndex = [3,4,5,6,7]; // search column TextBox Invisible Column position
     		  $('#'+containerId+'_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			   reInitializeDTRAGViewCategorySummary();			   
		   },  
		   select: true,
		   buttons: [	             	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "Category Summary",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "Category Summary",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "Category Summary",
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
           { mData: "productName",className: 'disableEditInline', sWidth: '10%' },
           { mData: "workpackageName",className: 'disableEditInline', sWidth: '10%' },   
			{ mData: "activityCategoryName",className: 'disableEditInline', sWidth: '10%' },
		   { mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+   						
						'<i class="categoryRAGSummaryImg1" style="color: blue;" title="Delayed">'+data.red+'</i></button>'+	
     	       		'</div>');	      		
           		 return img;
            	}
            },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+   						
					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="categoryRAGSummaryImg2" style="color: blue;" title="Nearing End date">'+data.orange+'</i></button>'+	
     	       		'</div>');	      		
           		 return img;
            	}
            },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+   						
						'<i class="categoryRAGSummaryImg3" style="color: blue;" title="Completed">'+data.green+'</i></button>'+	
     	       		'</div>');	      		
           		 return img;
            	}
            },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+					
						'<i class="categoryRAGSummaryImg4" style="color: blue;" title="Aborted">'+data.completedCount+'</i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="categoryRAGSummaryImg5" style="color: blue;" title="Delayed">'+data.abortCount+'</i></button>'+								
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
		 	 
		 $('#'+containerId+'_length').css('margin-top','8px');
		 $('#'+containerId+'_length').css('padding-left','35px');
		 				
		listRAGViewCategorySummary_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() != this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );
			
		// ----- Delayed -----
		 
		  $('#'+containerId+' tbody').on('click', 'td button .categoryRAGSummaryImg1', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewCategorySummary_oTable.DataTable().row(tr);									

				viewWorkflowEntityCategoryBasedDetail(row.data().productId, row.data().workpackageId, row.data().activityCategoryId, 'red');	
		 });
		 
		 // ----- Nearing End date -----

		 $('#'+containerId+' tbody').on('click', 'td button .categoryRAGSummaryImg2', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewCategorySummary_oTable.DataTable().row(tr);									

				viewRAGStatusBasedEntityTypeDetail(row.data().productId, row.data().workpackageId, row.data().activityCategoryId, 'orange');	
		 });
			
		 // ----- In Progress -----

		 $('#'+containerId+' tbody').on('click', 'td button .categoryRAGSummaryImg3', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewCategorySummary_oTable.DataTable().row(tr);									

				viewRAGStatusBasedEntityTypeDetail(row.data().productId, row.data().workpackageId, row.data().activityCategoryId, 'green');	
		 });

		 // ----- Completed -----

		 $('#'+containerId+' tbody').on('click', 'td button .categoryRAGSummaryImg4', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewCategorySummary_oTable.DataTable().row(tr);									

				viewRAGStatusBasedEntityTypeDetail(row.data().productId, row.data().workpackageId, row.data().activityCategoryId, 'darkgreen');	
		 });

		 // ----- Aborted -----

		 $('#'+containerId+' tbody').on('click', 'td button .categoryRAGSummaryImg5', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewCategorySummary_oTable.DataTable().row(tr);									

				viewRAGStatusBasedEntityTypeDetail(row.data().productId, row.data().workpackageId, row.data().activityCategoryId, 'darkRed');	
		 });	
		   
	});
}

// ----- productRAGSummary -----

var listRAGViewProductSummary_oTable='';
var clearTimeoutDTRAGViewProductSummary=''
function reInitializeDTRAGViewProductSummary(){
	clearTimeoutDTRAGViewProductSummary = setTimeout(function(){				
		listRAGViewProductSummary_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTRAGViewProductSummary);
	},200);
}

function ragViewProductSummaryHeader(){		
	var tr = '<tr>'+			
		'<th>Product</th>'+
		'<th>Workpackage</th>'+
		'<th><i class="fa fa-square" style="color: red;" title="Delayed"></i></th>'+
		'<th><i class="fa fa-square" style="color: orange;" title="Nearing End date"></i></th>'+
		'<th><i class="fa fa-square" style="color: green;" title="In Progress"></i></th>'+		
		'<th><i class="fa fa-check" style="color: darkgreen;" title="Completed"></i></th>'+
		'<th><i class="fa fa-times" style="color: darkRed;" title="Aborted"></i></th>'+
		/*'<th></th>'+*/
	'</tr>';
	return tr;
}

function listRAGViewProductSummaryView(data,dashBoardDivId,containerId){
	try{
		if ($('#'+dashBoardDivId+' #InProductSummary').children().length>0) {
			$('#'+dashBoardDivId+' #InProductSummary').children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(7);  // total coulmn count		  
	  var childDivString = '<table id="'+containerId+'" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';	
	  $('#'+dashBoardDivId+' #InProductSummary').append('<h5 class="caption-subject theme-font" style="text-decoration: underline;">Activities :</h5>');				
	  $('#'+dashBoardDivId+' #InProductSummary').append(childDivString); 						  
	  
	  $('#'+containerId+' thead').html('');
	  $('#'+containerId+' thead').append(ragViewProductSummaryHeader());
	  
	  $('#'+containerId+' tfoot tr').html('');     			  
	  $('#'+containerId+' tfoot tr').append(emptytr);
				
	listRAGViewProductSummary_oTable = $('#'+containerId+'').dataTable( {
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
			  var searchcolumnVisibleIndex = [2,3,4,5,6]; // search column TextBox Invisible Column position
     		  $('#'+containerId+'_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			   reInitializeDTRAGViewProductSummary();			   
		   },  
		   select: true,
		   buttons: [	             	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "Product RAG Summary",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "Product RAG Summary",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "Product RAG Summary",
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
           { mData: "productName",className: 'disableEditInline', sWidth: '3%' },
           { mData: "workpackageName",className: 'disableEditInline', sWidth: '20%' },           
		   { mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       										
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i style="color: blue;" class="productRAGSummaryImg1" title="Total Workflow Status">'+data.red+'</i></button>'+							
     	       		'</div>');	      		
           		 return img;
            	}
            },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+   						
					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="productRAGSummaryImg2" style="color: blue;" title="Nearing End date">'+data.orange+'</i></button>'+													
     	       		'</div>');	      		
           		 return img;
            	}
            },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="productRAGSummaryImg3" style="color: blue;" itle="Completed">'+data.green+'</i></button>'+								
     	       		'</div>');	      		
           		 return img;
            	}
            },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="productRAGSummaryImg4" style="color: blue;" "title="Aborted">'+data.completedCount+'</i></button>'+									
     	       		'</div>');	      		
           		 return img;
            	}
            },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+       				
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="productRAGSummaryImg5" style="color: blue;" title="Delayed">'+data.abortCount+'</i></button>'+								
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
		 	 
		 $('#'+containerId+'_length').css('margin-top','8px');
		 $('#'+containerId+'_length').css('padding-left','35px');
		 				
		listRAGViewProductSummary_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() != this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );
			
		// ----- Delayed -----
		 
		  $('#'+containerId+' tbody').on('click', 'td button .productRAGSummaryImg1', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewProductSummary_oTable.DataTable().row(tr);									

				viewRAGStatusBasedEntityTypeDetail(row.data().productId, row.data().workpackageId, 'red');	
		 });
		 
		 // ----- Nearing End date -----

		 $('#'+containerId+' tbody').on('click', 'td button .productRAGSummaryImg2', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewProductSummary_oTable.DataTable().row(tr);									

				viewRAGStatusBasedEntityTypeDetail(row.data().productId, row.data().workpackageId, 'orange');	
		 });
			
		 // ----- In Progress -----

		 $('#'+containerId+' tbody').on('click', 'td button .productRAGSummaryImg3', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewProductSummary_oTable.DataTable().row(tr);									

				viewRAGStatusBasedEntityTypeDetail(row.data().productId, row.data().workpackageId, 'green');	
		 });

		 // ----- Completed -----

		 $('#'+containerId+' tbody').on('click', 'td button .productRAGSummaryImg4', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewProductSummary_oTable.DataTable().row(tr);									

				viewRAGStatusBasedEntityTypeDetail(row.data().productId, row.data().workpackageId, 'darkgreen');	
		 });

		 // ----- Aborted -----

		 $('#'+containerId+' tbody').on('click', 'td button .productRAGSummaryImg5', function () {
				var tr = $(this).closest('tr');
			    var row = listRAGViewProductSummary_oTable.DataTable().row(tr);									

				viewRAGStatusBasedEntityTypeDetail(row.data().productId, row.data().workpackageId, 'darkRed');	
		 });	
		   
	});
}

// ----- ended -----

/*
function listRAGviewWorkpackageSummary(dashBoardDivId,containerId){
	var entityTypeTitle="Workpackage Activities";
	
	var urlToListRAGviewWorkpackageSummary = 'workflow.RAG.indicator.product.workpackage.summary?productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+wfSummaryActivityWorkPackageId;
	$('#'+dashBoardDivId).find(".jScrollContainerResponsiveTop").eq(0).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: entityTypeTitle,
        selecting: true, //Enable selecting 
        //paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListRAGviewWorkpackageSummary,
		}, 
		fields: {
			workpackageId:{
				list: false,
				edit : false,
				create: false,
			},
			workpackageName:{
				title : 'WorkPackage',
				list: true,
				edit : false,
				create: false,
			},
			entityTypeId:{
				list: false,
				edit : false,
				create: false,
			},
			red: { 
				title : '<i class="fa fa-square" style="color: red;" title="Delayed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+wfSummaryProductId+','+data.record.workpackageId+', \'red\')">'+data.record.red+'</a>';
		           	return link;
		        }
			},
			orange: { 
				title : '<i class="fa fa-square" style="color: orange;" title="Nearing End date"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+wfSummaryProductId+','+data.record.workpackageId+', \'orange\')">'+data.record.orange+'</a>';
		           	return link;
		        }
			},green: { 
				title : '<i class="fa fa-square" style="color: green;" title="In Progress"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+wfSummaryProductId+','+data.record.workpackageId+', \'green\')">'+data.record.green+'</a>';
		           	return link;
		        }
			},
			completedCount: { 
				title : '<i class="fa fa-check" style="color: darkgreen;" title="Completed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+wfSummaryProductId+','+data.record.workpackageId+', \'darkgreen\')">'+data.record.completedCount+'</a>';
		           	return link;
		        }
			},abortCount: { 
				title : '<i class="fa fa-times" style="color: darkRed;" title="Aborted"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+wfSummaryProductId+','+data.record.workpackageId+', \'darkRed\')">'+data.record.abortCount+'</a>';
		           	return link;
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
	$('#'+containerId).jtable('load'); 
}
*/

/*
function listWorkflowEntityInstanceTemplateRAGActivitiesSummary(dashBoardDivId,containerId){
	var entityTypeTitle="Activities";	
	var urlToListWorkflowEntityInstanceActivityTemplateSummary = 'workflow.RAG.indicator.product.activity.summary?productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId;
	$('#'+dashBoardDivId).find(".jScrollContainerResponsiveTop").eq(0).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: entityTypeTitle,
        selecting: true, //Enable selecting 
        //paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceActivityTemplateSummary,
		}, 
		fields: {
			activityId:{
				list: false,
				edit : false,
				create: false,
			},
			activityName:{
				title : 'Activity',
				list: true,
				edit : false,
				create: false,
			},
			entityTypeId:{
				list: false,
				edit : false,
				create: false,
			},
			red: { 
				title : '<i class="fa fa-square" style="color: red;" title="Delayed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+wfSummaryProductId+','+data.record.activityId+', \'red\')">'+data.record.red+'</a>';
		           	return link;
		        }
			},
			orange: { 
				title : '<i class="fa fa-square" style="color: orange;" title="Nearing End date"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+wfSummaryProductId+','+data.record.activityId+', \'orange\')">'+data.record.orange+'</a>';
		           	return link;
		        }
			},green: { 
				title : '<i class="fa fa-square" style="color: green;" title="In Progress"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+wfSummaryProductId+','+data.record.activityId+', \'green\')">'+data.record.green+'</a>';
		           	return link;
		        }
			},
			completedCount: { 
				title : '<i class="fa fa-check" style="color: darkgreen;" title="Completed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+wfSummaryProductId+','+data.record.activityId+', \'darkgreen\')">'+data.record.completedCount+'</a>';
		           	return link;
		        }
			},abortCount: { 
				title : '<i class="fa fa-times" style="color: darkRed;" title="Aborted"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+wfSummaryProductId+','+data.record.activityId+', \'darkRed\')">'+data.record.abortCount+'</a>';
		           	return link;
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
	$('#'+containerId).jtable('load'); 
}
*/

function viewWorkflowDashBoradIndicatorDetailView(indicatiorValue) {
	var userId = 0;
	var roleId = 0;
	var workflowStatusId = 0;
	var typeId=0;
	var workflowStatusCategoryId=0;
	listWorkflowSummarySLADetails(engagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicatiorValue,  wfSummaryEntityId, workflowStatusId, userId, roleId,typeId,"",wfSummaryEntityInstanceId,workflowStatusCategoryId);
}

function viewWorkflowDashBoradStatusBasedDetail(workflowStatusId, entityId){
	var userId = 0;
	var roleId = 0;
	var indicator="";
	var typeId=0;
	var workflowStatusCategoryId=0;
	listWorkflowSummarySLADetails(engagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,  wfSummaryEntityId, workflowStatusId, userId, roleId,typeId,"",wfSummaryEntityInstanceId,workflowStatusCategoryId);
} 

function viewWorkflowDashBoradUserBasedDetail(workflowStatusId, entityId, userId){
	var roleId = 0;
	var indicator = "";
	var typeId=0;
	var workflowStatusCategoryId=0;
	listWorkflowSummarySLADetails(engagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,  wfSummaryEntityId, workflowStatusId, userId, roleId,typeId,"",wfSummaryEntityInstanceId,workflowStatusCategoryId);
}
function viewWorkflowDashBoradRoleBasedDetail(workflowStatusId, entityId, roleId){
	var userId = 0;
	var indicator = "";
	var typeId=0;
	var workflowStatusCategoryId=0;
	listWorkflowSummarySLADetails(engagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,  wfSummaryEntityId, workflowStatusId, userId, roleId,typeId,"",wfSummaryEntityInstanceId,workflowStatusCategoryId);
}

function viewWorkflowDashBoradTypeBasedDetail(typeId){
	var userId = 0;
	var roleId = 0;
	var indicator="";
	var workflowStatusId=0;
	var workflowStatusCategoryId=0;
	listWorkflowSummarySLADetails(engagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,  wfSummaryEntityId, workflowStatusId, userId, roleId,typeId,"",wfSummaryEntityInstanceId,workflowStatusCategoryId);
}

function viewWorkflowDashBoradStatusCategoryBasedDetail(workflowStatusCategoryId, entityId){
	var userId = 0;
	var roleId = 0;
	var indicator="";
	var typeId=0;
	var workflowStatusId=0;
	listWorkflowSummarySLADetails(engagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,  wfSummaryEntityId, workflowStatusId, userId, roleId,typeId,"",wfSummaryEntityInstanceId,workflowStatusCategoryId);
}


function viewWorkflowDashBoradIndicatorDetailRAGView(indicatiorValue) {
	var userId = 0;
	var roleId = 0;
	var entityTypeInstanceId = 0;
	if(wfSummaryActivityWorkPackageId != 0){
		entityTypeInstanceId=wfSummaryActivityWorkPackageId;
	}
	listWorkflowSummarySLARAGDetails(engagementId, wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicatiorValue,userId, roleId,entityTypeInstanceId);
}

function viewRAGStatusResourceBasedDetail(userId, roleId,indicator){
	var entityTypeInstanceId=0;
	if(roleId == null) {
		roleId=0;
	}
	if(userId == null) {
		userId=0;
	}
	if(wfSummaryProductId == 0 && wfSummaryEntityTypeId == 34) {
		wfSummaryProductId=0;
	}
	if(wfSummaryEntityTypeId != null && wfSummaryEntityTypeId == 34) {
		entityTypeInstanceId=0;
	}
	if(wfSummaryActivityWorkPackageId != 0){
		entityTypeInstanceId=wfSummaryActivityWorkPackageId;
	}
	listWorkflowSummarySLARAGDetails(engagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,userId, roleId,entityTypeInstanceId);
} 

function viewRAGStatusBasedEntityTypeDetail(productId,entityTypeInstanceId, indicator){
	var roleId = 0;
	var userId = 0;
	if(wfSummaryProductId == 0 && wfSummaryEntityTypeId == 34) {
		wfSummaryProductId=entityTypeInstanceId;
	}
	if(wfSummaryEntityTypeId != null && wfSummaryEntityTypeId == 34) {
		entityTypeInstanceId=0;
	}
	if(wfSummaryProductId ==0) {
		wfSummaryProductId=productId;
	}
	listWorkflowSummarySLARAGDetails(engagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,userId, roleId,entityTypeInstanceId);
}

function viewWorkflowEntityCategoryBasedDetail(productId,entityTypeInstanceId,categoryId,indicator){
	var roleId = 0;
	var userId = 0;
	if(wfSummaryProductId == 0 && wfSummaryEntityTypeId == 34) {
		wfSummaryProductId=entityTypeInstanceId;
	}
	if(wfSummaryEntityTypeId != null && wfSummaryEntityTypeId == 34) {
		entityTypeInstanceId=0;
	}
	if(wfSummaryProductId ==0) {
		wfSummaryProductId=productId;
	}
	listWorkflowSummarySLARAGDetails(engagementId,wfSummaryProductId,  wfSummaryProductName,  wfSummaryEntityTypeId, indicator,userId, roleId,entityTypeInstanceId,categoryId);
}

/*
// ----- Product -----

function lisRAGViewProductSummary(dashBoardDivId,containerId){
	var entityTypeTitle="Activities";
	
	//var urlToLisRAGViewProductSummary = 'workflow.RAG.indicator.product.grouping.summary?productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+wfSummaryProductBuildId;
	var urlToLisRAGViewProductSummary = 'workflow.RAG.indicator.engagement.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+wfSummaryActivityWorkPackageId;
	$('#'+dashBoardDivId).find(".jScrollContainerResponsiveTop").eq(0).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: entityTypeTitle,
        selecting: true, //Enable selecting 
        //paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
       // saveUserPreferences: false, 
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToLisRAGViewProductSummary,
		}, 
		fields: {
			productId:{
				list: false,
				edit : false,
				create: false,
			},
			productName:{
				title : 'Product',
				list: true,
				edit : false,
				create: false,
			},
			workpackageId:{
				list: false,
				edit : false,
				create: false,
			},
			workpackageName:{
				title : 'Workpackage',
				list: true,
				edit : false,
				create: false,
			},
			entityTypeId:{
				list: false,
				edit : false,
				create: false,
			},
			red: { 
				title : '<i class="fa fa-square" style="color: red;" title="Delayed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+data.record.productId+','+data.record.workpackageId+', \'red\')">'+data.record.red+'</a>';
		           	return link;
		        }
			},
			orange: { 
				title : '<i class="fa fa-square" style="color: orange;" title="Nearing End date"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+data.record.productId+','+data.record.workpackageId+', \'orange\')">'+data.record.orange+'</a>';
		           	return link;
		        }
			},green: { 
				title : '<i class="fa fa-square" style="color: green;" title="In Progress"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+data.record.productId+','+data.record.workpackageId+', \'green\')">'+data.record.green+'</a>';
		           	return link;
		        }
			},
			completedCount: { 
				title : '<i class="fa fa-check" style="color: darkgreen;" title="Completed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+data.record.productId+','+data.record.workpackageId+', \'darkgreen\')">'+data.record.completedCount+'</a>';
		           	return link;
		        }
			},abortCount: { 
				title : '<i class="fa fa-times" style="color: darkRed;" title="Aborted"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+data.record.productId+','+data.record.workpackageId+', \'darkRed\')">'+data.record.abortCount+'</a>';
		           	return link;
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
	$('#'+containerId).jtable('load'); 
}
*/

/*

// ----- Category Summary -----

function lisDashBoardRAGViewActivityCategorySummary(dashBoardDivId,containerId){
	var entityTypeTitle="Category Summary";
	
	var urlToLisRAGViewActivityCategorySummary = 'workflow.RAG.indicator.activity.category.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+wfSummaryActivityWorkPackageId;
	$('#'+dashBoardDivId).find(".jScrollContainerResponsiveTop").eq(2).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: entityTypeTitle,
        selecting: true, //Enable selecting 
        //paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin        
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToLisRAGViewActivityCategorySummary,
		}, 
		fields: {
			engagementId:{
				list: false,
				edit : false,
				create: false,
			},
			productId:{
				list: false,
				edit : false,
				create: false,
			},
			productName:{
				title : 'Product',
				list: true,
				edit : false,
				create: false,
			},
			workpackageId:{
				list: false,
				edit : false,
				create: false,
			},
			workpackageName:{
				title : 'Workpackage',
				list: true,
				edit : false,
				create: false,
			},
			activityCategoryId:{
				list: false,
				edit : false,
				create: false,
			},
			activityCategoryName:{
				title : 'Category',
				list: true,
				edit : false,
				create: false,
			},
			entityTypeId:{
				list: false,
				edit : false,
				create: false,
			},
			red: { 
				title : '<i class="fa fa-square" style="color: red;" title="Delayed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityCategoryBasedDetail('+data.record.productId+','+data.record.workpackageId+','+data.record.activityCategoryId+',\'red\')">'+data.record.red+'</a>';
		           	return link;
		        }
			},
			orange: { 
				title : '<i class="fa fa-square" style="color: orange;" title="Nearing End date"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityCategoryBasedDetail('+data.record.productId+','+data.record.workpackageId+','+data.record.activityCategoryId+', \'orange\')">'+data.record.orange+'</a>';
		           	return link;
		        }
			},green: { 
				title : '<i class="fa fa-square" style="color: green;" title="In Progress"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityCategoryBasedDetail('+data.record.productId+','+data.record.workpackageId+','+data.record.activityCategoryId+', \'green\')">'+data.record.green+'</a>';
		           	return link;
		        }
			},
			completedCount: { 
				title : '<i class="fa fa-check" style="color: darkgreen;" title="Completed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityCategoryBasedDetail('+data.record.productId+','+data.record.workpackageId+','+data.record.activityCategoryId+', \'darkgreen\')">'+data.record.completedCount+'</a>';
		           	return link;
		        }
			},abortCount: { 
				title : '<i class="fa fa-times" style="color: darkRed;" title="Aborted"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowEntityCategoryBasedDetail('+data.record.productId+','+data.record.workpackageId+','+data.record.activityCategoryId+', \'darkRed\')">'+data.record.abortCount+'</a>';
		           	return link;
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
	$('#'+containerId).jtable('load'); 
}
*/
/*
function listRAGViewResourceTemplateSummary(dashBoardDivId,containerId){
	var entityTypeTitle="";
	if(wfSummaryEntityTypeId == 30) {
		entityTypeTitle="Resource Tasks Queue";
	} else if(wfSummaryEntityTypeId == 33) {
		entityTypeTitle="Resource Activities Queue";
	} else if(wfSummaryEntityTypeId == 34) {
		entityTypeTitle="Resource Workpackage Queue";
	} else if(wfSummaryEntityTypeId == 3) {
		entityTypeTitle="Resource  Testcases Queue";
	} 
	
	var urlToListWorkflowEntityInstanceResourceTemplateSummary = 'workflow.RAG.indicator.product.resource.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+"&entityParentInstanceId="+wfSummaryActivityWorkPackageId;
	$('#'+dashBoardDivId).find(".jScrollContainerResponsiveTop").eq(1).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: entityTypeTitle,
        selecting: true, //Enable selecting 
        //paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin        
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceResourceTemplateSummary,
		}, 
		fields: {
			userId:{
				list: false,
				edit : false,
				create: false,
			},
			roleId:{
				list: false,
				edit : false,
				create: false,
			},
			userName:{
				title : 'Resource',
				list: true,
				edit : false,
				create: false,
			},
			entityTypeId:{
				list: false,
				edit : false,
				create: false,
			},
			red: { 
				title : '<i class="fa fa-square" style="color: red;" title="Delayed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusResourceBasedDetail('+data.record.userId+','+data.record.roleId+', \'red\')">'+data.record.red+'</a>';
		           	return link;
		        }
			},
			orange: { 
				title : '<i class="fa fa-square" style="color: orange;" title="Nearing End date"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusResourceBasedDetail('+data.record.userId+','+data.record.roleId+', \'orange\')">'+data.record.orange+'</a>';
		           	return link;
		        }
			},green: { 
				title : '<i class="fa fa-square" style="color: green;" title="In Progress"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusResourceBasedDetail('+data.record.userId+','+data.record.roleId+', \'green\')">'+data.record.green+'</a>';
		           	return link;
		        }
			},
			completedCount: { 
				title : '<i class="fa fa-check" style="color: darkgreen;" title="Completed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
					var link = '<a href="javascript:void(0)" onclick="viewRAGStatusResourceBasedDetail('+data.record.userId+','+data.record.roleId+', \'darkgreen\')">'+data.record.completedCount+'</a>';
		           	return link;
		        }
			},abortCount: { 
				title : '<i class="fa fa-times" style="color: darkRed;" title="Aborted"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
					var link = '<a href="javascript:void(0)" onclick="viewRAGStatusResourceBasedDetail('+data.record.userId+','+data.record.roleId+', \'darkRed\')">'+data.record.abortCount+'</a>';
		           	return link;
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
	$('#'+containerId).jtable('load'); 
}
*/

/*function listWorkflowInidcatoryRAGSummary(){
	var indicatorSummaryURL = 'workflow.RAG.indicator.product.SLA.summary?productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId;
	$.post(indicatorSummaryURL, function(data) {
		if(data.Result == 'OK' || data.Result == 'Ok'){
			var indicatorSummary = '<span style="font-weight:bolder;">SLA :</span><span style="padding:10px"><i class="fa fa-square" style="color: red;" title="SLA duration elapsed"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'red\')">0</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: orangered;" title="Needs immediate action"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'orangered\')">0</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: orange;" title="Needs immediate attention"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'orange\')">0</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: green;" title="Available for action"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailView(\'green\')">0</a></span>';
			if(typeof data.Records != 'undefined' && data.Records.length > 0){
				var indicatorDetails = data.Records[0];
				indicatorSummary = '<span style="font-weight:bolder;">SLA :</span><span style="padding:10px"><i class="fa fa-square" style="color: red;" title="SLA duration elapsed"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailRAGView(\'red\')">'+indicatorDetails["red"]+'</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: orange;" title="Needs immediate attention"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailRAGView(\'orange\')">'+indicatorDetails["orange"]+'</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: green;" title="Available for action"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailRAGView(\'green\')">'+indicatorDetails["green"]+'</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: darkgray;" title="SLA duration End"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailRAGView(\'darkgray\')">'+indicatorDetails["completedCount"]+'</a></span>|<span style="padding:10px"><i class="fa fa-square" style="color: gray;" title="SLA duration aborted"></i> <a href="javascript:void(0)" onclick="viewWorkflowDashBoradIndicatorDetailRAGView(\'gray\')">'+indicatorDetails["abortCount"]+'</a></span>';
			}
			$('#headerSubTitle').html(indicatorSummary);
		}else{
			callAlert(data.Message);
		}
	});
}*/


/*function lisRAGViewEngagmentSummary(dashBoardDivId,containerId){
	var entityTypeTitle="Activities";
	
	//var urlToLisRAGViewProductSummary = 'workflow.RAG.indicator.product.grouping.summary?productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+wfSummaryProductBuildId;
	var urlToLisRAGViewProductSummary = 'workflow.RAG.indicator.engagement.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityParentInstanceId='+wfSummaryActivityWorkPackageId;
	$('#'+dashBoardDivId).find(".jScrollContainerResponsiveTop").eq(0).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: entityTypeTitle,
        selecting: true, //Enable selecting 
        //paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
         saveUserPreferences: false, 
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToLisRAGViewProductSummary,
		}, 
		fields: {
			enagementId:{
				list: false,
				edit : false,
				create: false,
			},
			productId:{
				list: false,
				edit : false,
				create: false,
			},
			engamentName:{
				title : 'engamentName',
				list: true,
				edit : false,
				create: false,
			},
			productName:{
				title : 'Product',
				list: true,
				edit : false,
				create: false,
			},
			workpackageId:{
				list: false,
				edit : false,
				create: false,
			},
			workpackageName:{
				title : 'Workpackage',
				list: true,
				edit : false,
				create: false,
			},
			entityTypeId:{
				list: false,
				edit : false,
				create: false,
			},
			red: { 
				title : '<i class="fa fa-square" style="color: red;" title="SLA duration elapsed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+data.record.productId+','+data.record.workpackageId+', \'red\')">'+data.record.red+'</a>';
		           	return link;
		        }
			},
			orangered: { 
				title : '<i class="fa fa-square" style="color: orangered;" title="Nearing End date"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+data.record.buildId+')">'+data.record.orangered+'</a>';
		           	return link;
		        }
			},orange: { 
				title : '<i class="fa fa-square" style="color: orange;" title="Nearing End date"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+data.record.productId+','+data.record.workpackageId+', \'orange\')">'+data.record.orange+'</a>';
		           	return link;
		        }
			},green: { 
				title : '<i class="fa fa-square" style="color: green;" title="In Progress"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+data.record.productId+','+data.record.workpackageId+', \'green\')">'+data.record.green+'</a>';
		           	return link;
		        }
			},
			completedCount: { 
				title : '<i class="fa fa-check" style="color: darkgreen;" title="Completed"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+data.record.productId+','+data.record.workpackageId+', \'darkgreen\')">'+data.record.completedCount+'</a>';
		           	return link;
		        }
			},abortCount: { 
				title : '<i class="fa fa-times" style="color: darkRed;" title="Aborted"></i>',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewRAGStatusBasedEntityTypeDetail('+data.record.productId+','+data.record.workpackageId+', \'darkRed\')">'+data.record.abortCount+'</a>';
		           	return link;
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
	$('#'+containerId).jtable('load'); 
}*/


/*
function listDashBoardWorkflowEntityInstanceTypeSummary(dashBoardDivId,containerId){
	
	var entityTypeTitle="";
	if(wfSummaryEntityTypeId == 30) {
		entityTypeTitle="Task Life Cycle";
		typeName ="Task Type";
	} else if(wfSummaryEntityTypeId == 33) {
		entityTypeTitle="Activities";
		typeName ="Activity Type";
	} else if(wfSummaryEntityTypeId == 34) {
		entityTypeTitle="Workpackage Life Cycle";
		typeName ="Type";
	} else if(wfSummaryEntityTypeId == 3) {
		entityTypeTitle="Testcase Life Cycle";
		typeName ="Type";
	} 
	var urlToListWorkflowEntityInstanceTypeSummary = 'workflow.entity.instance.type.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+"&entityParentInstanceId="+wfSummaryEntityInstanceId;
	$("#"+dashBoardDivId).find(".jScrollContainerResponsiveTop").eq(0).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: entityTypeTitle,
        selecting: true, //Enable selecting 
      //  paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin        
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceTypeSummary,
		}, 
		fields: {
			workflowId:{
				list: false,
				edit : false,
				create: false,
			},			
			typeName:{
				title : 'Type',
				list: typeFiledDisplay,
				edit : false,
				create: false,
			},
			typeId:{
				title : 'typeId',
				list: false,
				edit : false,
				create: false,
			},
			entityTypeId:{
				list: false,
				edit : false,
				create: false,
			},
			typeCount: { 
				title : 'Count',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowDashBoradTypeBasedDetail('+data.record.typeId+')">'+data.record.typeCount+'</a>';
		           	return link;
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
	$('#'+containerId).jtable('load'); 
}*/

/*
function listDashBoardWorkflowEntityInstanceStatusSummary(dashBoardDivId,containerId){	
		
	var urlToListWorkflowEntityInstanceStatusSummary = 'workflow.entity.instance.status.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityId='+ wfSummaryEntityId+'&entityParentInstanceId='+ wfSummaryEntityInstanceId;
	$("#"+dashBoardDivId).find(".jScrollContainerResponsiveTop").eq(1).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: 'Status Summary',
        selecting: true, //Enable selecting 
        //paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sorting        
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceStatusSummary,
		}, 
		fields: {
			workflowStatusId:{
				list: false,
				edit : false,
				create: false,
			},
			entityId:{
				list: false,
				edit : false,
				create: false,
			},			
			typeName:{
				title : 'Type',
				list: typeFiledDisplay,
				edit : false,
				create: false,
			},
			workflowStatus: { 
				title: 'Status',
				list: true,
				edit : false,
				create: false,
				size:'7%',
			},
			workflowStatusCount: { 
				title : 'Count',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowDashBoradStatusBasedDetail('+data.record.workflowStatusId+','+data.record.entityId+')">'+data.record.workflowStatusCount+'</a>';
		           	return link;
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
	$('#'+containerId).jtable('load'); 
}
*/

/*
function listDashBoardWorkflowEntityInstanceStatusUserSummary(dashBoardDivId,containerId){
	
	var urlToListWorkflowEntityInstanceStatusSummary = 'workflow.entity.instance.status.actor.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityId='+ wfSummaryEntityId+'&entityInstanceId='+ wfSummaryEntityInstanceId+'&actorType=User';
	$("#"+dashBoardDivId).find(".jScrollContainerResponsiveTop").eq(2).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: 'User Queue',
        selecting: true, //Enable selecting 
        //paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sorting        
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceStatusSummary,
		}, 
		fields: {
			workflowStatusId:{
				list: false,
				edit : false,
				create: false,
			},
			entityId:{
				list: false,
				edit : false,
				create: false,
			},
			userId:{
				list: false,
				edit : false,
				create: false,
			},			
			typeName: { 
			title: 'Type',
			list: true,
			edit : false,
			create: false,
		},
			workflowStatus: { 
				title: 'Status',
				list: true,
				edit : false,
				create: false,
			},
			actorName: { 
				title : 'User Name',
				create:false,
				list : true,
				edit : false,
			},
			instanceCount: { 
				title : 'Count',
				create:false,
				list : true,
				edit : false,
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowDashBoradUserBasedDetail('+data.record.workflowStatusId+','+data.record.entityId+','+data.record.userId+')">'+data.record.instanceCount+'</a>';
		           	return link;
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
	$('#'+containerId).jtable('load'); 
}
*/

/*
function listDashBoardWorkflowEntityInstanceCategorySummary(dashBoardDivId,containerId){
	
	var urlToListWorkflowEntityInstanceStatusCategorySummary = 'workflow.entity.instance.status.category.summary?engagementId='+engagementId+'&productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityId='+ wfSummaryEntityId+'&entityParentInstanceId='+ wfSummaryEntityInstanceId;
	$("#"+dashBoardDivId).find(".jScrollContainerResponsiveTop").eq(3).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: 'Status Category Summary',
        selecting: true, //Enable selecting 
        //paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sorting        
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceStatusCategorySummary,
		}, 
		fields: {
			workflowStatusCategoryId:{
				list: false,
				edit : false,
				create: false,
			},
			entityId:{
				list: false,
				edit : false,
				create: false,
			},			
			typeName: { 
				title: 'Type',
				list: true,
				edit : false,
				create: false,
			},
			workflowStatusCategoryName: { 
				title: 'Category',
				list: true,
				edit : false,
				create: false,
				size:'7%',
			},
			workflowStatusCount: { 
				title : 'Count',
				create:false,
				list : true,
				edit : false,
				size:'3%',
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowDashBoradStatusCategoryBasedDetail('+data.record.workflowStatusCategoryId+','+data.record.entityId+')">'+data.record.workflowStatusCount+'</a>';
		           	return link;
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
	$('#'+containerId).jtable('load'); 
}
*/

/*

function listDashBoardWorkflowEntityInstanceStatusRoleSummary(dashBoardDivId,containerId){
	
	var urlToListWorkflowEntityInstanceStatusSummary = 'workflow.entity.instance.status.actor.summary?productId='+ wfSummaryProductId+'&entityTypeId='+ wfSummaryEntityTypeId+'&entityId='+ wfSummaryEntityId+'&entityInstanceId='+ wfSummaryEntityInstanceId+'&actorType=Role';
	$("#"+dashBoardDivId).find(".jScrollContainerResponsiveTop").eq(3).append("<br><div id='"+containerId+"' class='dynamicJTable'></div>");
	
	$('#'+containerId).jtable({
		title: 'Role Queue',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
         saveUserPreferences: false, 
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
		actions: { 
			listAction: urlToListWorkflowEntityInstanceStatusSummary,
		}, 
		fields: {
			
			workflowStatusId:{
				list: false,
				edit : false,
				create: false,
			},
			entityId:{
				list: false,
				edit : false,
				create: false,
			},
			roleId:{
				list: false,
				edit : false,
				create: false,
			},
			workflowName: { 
				title: 'Workflow Template',
				list: true,
				edit : false,
				create: false,
			},
			workflowStatus: { 
				title: 'Status',
				list: true,
				edit : false,
				create: false,
			},
			actorName: { 
				title : 'Role Name',
				create:false,
				list : true,
				edit : false,
			},
			instanceCount: { 
				title : 'Count',
				create:false,
				list : true,
				edit : false,
				display:function (data) { 
 	            	var link = '<a href="javascript:void(0)" onclick="viewWorkflowDashBoradRoleBasedDetail('+data.record.workflowStatusId+','+data.record.entityId+','+data.record.roleId+')">'+data.record.instanceCount+'</a>';
		           	return link;
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
	$('#'+containerId).jtable('load'); 
}
*/