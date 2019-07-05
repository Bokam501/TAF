var SingleDataTableContainer = function() {
  
   var initialise = function(jsonObj){
	   assignSingleDataTableValues(jsonObj);
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();
var editorComments;
var commentsSelectedTr='';
var commentsSelectedRow='';
var botId=0;
var botName='';

//var singleDataTableJsonObject='';
function assignSingleDataTableValues(jsonObj){
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:jsonObj.url,
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
			singleDataTableContainerSummary(jsonObj);
			botId=jsonObj.botId;
			botName = jsonObj.botName;
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

var clearTimeoutSingleDataTable;
function reInitializeDTAuditHistory(){
	clearTimeoutSingleDataTable = setTimeout(function(){				
		auditHistorySingleDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutSingleDataTable);
	},200);
}

function reInitializeDTAuditFile(){
	clearTimeoutSingleDataTable = setTimeout(function(){				
		auditFileSingleDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutSingleDataTable);
	},200);
}


function reInitializeDTBotAuditFile(){
	clearTimeoutSingleDataTable = setTimeout(function(){				
		botauditFileSingleDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutSingleDataTable);
	},200);
}

function singleDataTableContainerSummary(jsonObj){	
	$("#div_SingleDataTableSummary").find("h4").text(jsonObj.Title);
	
	if(jsonObj.componentUsageTitle == "DefectSummary"){			
		listDefectSummaryTable(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "BDDkeywords"){		  
		bddKeywordDT_Container(jsonObj);		
	}
	else if(jsonObj.componentUsageTitle == "ObjectRepository"){		
		objectRepositoryFileContent(jsonObj);		
	}
	else if(jsonObj.componentUsageTitle == "TestData"){				
		testDataFileContent(jsonObj);		
	}
	else if(jsonObj.componentUsageTitle == "productAudit"){		
		auditHistoryDT_Container(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "productComments"){		
		commentsListDT_Container(jsonObj);
		
	}
	else if(jsonObj.componentUsageTitle == "productVersionAudit"){		
		auditHistoryDT_Container(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "productBuildAudit"){	
		auditHistoryDT_Container(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "productFeatureAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "environmentAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "testRunPlanAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "testRunPlanGroupAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "attachmentAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "workPackageAudit"){		
		auditHistoryDT_Container(jsonObj);
	    //workPackageAuditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "activityWorkPackageAudit"){		
		auditHistoryDT_Container(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "activityAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "activityTaskAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "changeRequestAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "clarificationTrackerAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "testCaseAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "testCaseStepsAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "testSuiteAudit"){			
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "regularUserAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "customerUserAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "flexUserAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "testFactoryAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "testFactoryLabAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "vendorAudit"){		   
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "customerAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "environmentGroupAudit"){	
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "environmentCategoryAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "riskAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "riskMitigationAudit"){	
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "riskSeverityAudit"){	
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "riskLikehoodAudit"){			
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "riskRatingAudit"){			
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "workFlowAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "workFlowStatusAudit"){	
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "workFlowEventsAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "visualizationURLAudit"){	
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "dashboardTabURLAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "dashboardTabRoleBasedAudit"){	
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "sourceEntityAudit"){		
		sourceEntityConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "testManagementSystemAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "defectManagementSystemAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "scmSystemAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "deviceAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "serverAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "storageAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "resourcePoolAudit"){		
		auditFileContent(jsonObj);
	}else if(jsonObj.componentUsageTitle == "userSkillsAudit"){		
		auditFileContent(jsonObj);
	}else if(jsonObj.componentUsageTitle == "resourceDemandAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "workShiftAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "productTeamResourcesAudit"){		
		auditFileContent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "botExecutionAudit"){		
		botAuditFileContent(jsonObj);
	}
	else{
		console.log("add custom Jtable");
	}
				
	$("#div_SingleDataTableSummary").modal();	
}

// --- BDDKeyword Phrase -----

var bddKeywordSingleDT_oTable='';
var clearTimeoutKeywordPhraseDataTable;
function reInitializeDTkeywordPhrase(){
	clearTimeoutKeywordPhraseDataTable = setTimeout(function(){				
		bddKeywordSingleDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutKeywordPhraseDataTable);
	},200);
}
function bddKeywordDT_Container(jsonObj){
	
	try{
		if ($("#dataTableSingleContainer").children().length>0) {
			$("#dataTableSingleContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = bddKeywordPhraseDataTable(); 			 
	$("#dataTableSingleContainer").append(childDivString);
	
	bddKeywordSingleDT_oTable = $("#bddKeyword_dataTable").dataTable( {
		 	"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       "fnInitComplete": function(data) {
	    	   
	    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
    		   $('#bddKeyword_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
	    	   reInitializeDTkeywordPhrase();
		   },  
		   buttons: [
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'BDDKeyword Phrase',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'BDDKeyword Phrase',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'BDDKeyword Phrase',
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
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "keywordPhrase", className: 'disableEditInline', sWidth: '35%' },		
           { mData: "description", className: 'disableEditInline', sWidth: '35%' },		
           { mData: "tags", className: 'disableEditInline', sWidth: '10%' },		
           { mData: "isSeleniumScripGeneration", className: 'disableEditInline', sWidth: '10%' },		
           { mData: "isAppiumScripGeneration", className: 'disableEditInline', sWidth: '10%' },	
       ],       
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		
		$("#bddKeyword_dataTable_length").css('margin-top','8px');
		$("#bddKeyword_dataTable_length").css('padding-left','35px');
		
		$('#bddKeyword_dataTable').DataTable().columns().every( function () {
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

function bddKeywordPhraseDataTable(){
	var childDivString = '<table id="bddKeyword_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Keyword Phrase</th>'+
			'<th>Description</th>'+
			'<th>Tags</th>'+
			'<th>Selenium(Web)</th>'+
			'<th>Appium(Mobile)</th>'+			
		'</tr>'+
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
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
// ----- ended -----

// -- Audit History for the Product Management Plan ----
function auditHistoryDataTable(){
	var childDivString = '<table id="auditHistory_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Event</th>'+
			'<th>Description</th>'+
			'<th>Remarks</th>'+
			'<th>User</th>'+
			'<th>Date & Time</th>'+
		'</tr>'+
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
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

var auditHistorySingleDT_oTable='';
function auditHistoryDT_Container(jsonObj){
	
	try{
		if ($("#dataTableSingleContainer").children().length>0) {
			$("#dataTableSingleContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = auditHistoryDataTable(); 			 
	$("#dataTableSingleContainer").append(childDivString);
	
	auditHistorySingleDT_oTable = $("#auditHistory_dataTable").dataTable( {
		 	"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       "aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
	    	   
	    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     	  var headerItems = $('#auditHistory_dataTable_wrapper tfoot tr th');
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
	    	   reInitializeDTAuditHistory();
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
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "eventName", className: 'disableEditInline', sWidth: '10%' },		
           { mData: "eventDescription", className: 'disableEditInline', sWidth: '35%' },		
           { mData: "sourceEntityName", className: 'disableEditInline', sWidth: '35%' },		
           { mData: "userName", className: 'disableEditInline', sWidth: '10%' },		
           { mData: "endTime", className: 'disableEditInline', sWidth: '10%' },	
       ],       
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		
		$("#auditHistory_dataTable_length").css('margin-top','8px');
		$("#auditHistory_dataTable_length").css('padding-left','35px');
		
		$('#auditHistory_dataTable').DataTable().columns().every( function () {
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

// ----- ended -----

function auditFileDataTable(){
	var childDivString = '<table id="auditFile_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Event Name</th>'+
			'<th>Event Description</th>'+
			'<th>EntityType</th>'+
			'<th>Remarks</th>'+
			'<th>User</th>'+
			'<th>Date & Time</th>'+
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


var auditFileSingleDT_oTable='';

function auditFileContent(jsonObj){
	try{
		if ($("#dataTableSingleContainer").children().length>0) {
			$("#dataTableSingleContainer").children().remove();
		}
	} 
	catch(e) {}
	
	
	var childDivString = auditFileDataTable(); 			 
	$("#dataTableSingleContainer").append(childDivString);
	
	auditFileSingleDT_oTable = $("#auditFile_dataTable").dataTable( {
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
	     	  var headerItems = $('#auditFile_dataTable_wrapper tfoot tr th');
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
	     	  
    	   reInitializeDTAuditFile();
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
	                        }
	                    },	                    
	                ],	                
	            },
	            'colvis'
	           ], 
          aaData:jsonObj.data,		    				 
          aoColumns: [	        	        
		       { mData: "eventName", className: 'disableEditInline', sWidth: '10%' },		
		       { mData: "eventDescription", className: 'disableEditInline', sWidth: '30%' },		
		       { mData: "sourceEntityType", className: 'disableEditInline', sWidth: '30%' },		
		       { mData: "sourceEntityName", className: 'disableEditInline', sWidth: '10%' },	
		       { mData: "userName", className: 'disableEditInline', sWidth: '10%' },	
		       { mData: "endTime", className: 'disableEditInline', sWidth: '10%' },	
		   ],
		   rowCallback: function ( row, data ) {
			  if(data['isModified']) {
				  $(row).find('td:eq(1)').css('backgroundColor', 'orange');
		      }
		   },
		   "oLanguage": {
			   "sSearch": "",
				"sSearchPlaceholder": "Search all columns"
		   },     
	});
	
	$(function(){ // this will be called when the DOM is ready 
		
		$("#auditFile_dataTable_length").css('margin-top','8px');
		$("#auditFile_dataTable_length").css('padding-left','35px');
	});
	
	$('#auditFile_dataTable').DataTable().columns().every( function () {
        var that = this;
        $('input', this.footer() ).on( 'keyup change', function () {
            if ( that.search() !== this.value ) {
                that
                	.search( this.value, true, false )
                    .draw();
            }
        });
    });
}

function listGenericAuditHistory(entityTypeIdAudit, entityNameAudit, componentUsageTitle){
	var url='administration.event.list?sourceEntityType='+entityNameAudit+'&sourceEntityId='+entityTypeIdAudit+'&jtStartIndex=0&jtPageSize=1000';
	if(entityNameAudit == "TestRunPlanGroup"){
		entityNameAudit = "Test Plan Group";
	}else if(entityNameAudit=="TestRunPlan"){
		entityNameAudit = "Test Plan";
	}
	var jsonObj={"Title":entityNameAudit+" Audit History",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":1000,
			"componentUsageTitle":componentUsageTitle,
	};
	SingleDataTableContainer.init(jsonObj);
}

//Bot Event History
function botAuditFileDataTable(){
	var botChildDivString = '<table id="botAuditFile_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Event Name</th>'+
			'<th>Event Description</th>'+
			'<th>EntityType</th>'+
			'<th>Execution Result</th>'+
			'<th>Remarks</th>'+
			'<th>User</th>'+
			'<th>Date & Time</th>'+
			'<th>Result</th>'+
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
		'</tr>'+
	'</tfoot>'+
	'</table>';		
	
	return botChildDivString;	
}


var botauditFileSingleDT_oTable='';

function botAuditFileContent(jsonObj){
	try{
		if ($("#dataTableSingleContainer").children().length>0) {
			$("#dataTableSingleContainer").children().remove();
		}
	} 
	catch(e) {}
	
	
	var childDivString = botAuditFileDataTable(); 			 
	$("#dataTableSingleContainer").append(childDivString);
	
	botauditFileSingleDT_oTable = $("#botAuditFile_dataTable").dataTable( {
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
    	   
    	   var searchcolumnVisibleIndex = [6]; // search column TextBox Invisible Column position
	     	  var headerItems = $('#botAuditFile_dataTable_wrapper tfoot tr th');
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
	     	  
	     	 reInitializeDTBotAuditFile();
	   },  
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'Bot History',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Bot History',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Bot History',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },	                    
	                ],	                
	            },
	            'colvis'
	           ], 
          aaData:jsonObj.data,		    				 
          aoColumns: [
		       { mData: "eventName", className: 'disableEditInline', sWidth: '10%' },		
		       { mData: "eventDescription", className: 'disableEditInline', sWidth: '30%' },		
		       { mData: "sourceEntityType", className: 'disableEditInline', sWidth: '15%' },		
		       { mData: "fieldValue", className: 'disableEditInline', sWidth: '10%' },
		       { mData: "sourceEntityName", className: 'disableEditInline', sWidth: '20%' },
		       { mData: "userName", className: 'disableEditInline', sWidth: '10%' },	
		       { mData: "endTime", className: 'disableEditInline', sWidth: '10%' },
	            { mData: null,sWidth: '5%',				 
	            	bSortable: false,
	            	mRender: function(data, type, full) {
	            		var img ="";
	           		 	      		
	           				 if(full.eventName == "Bot Execution") {
	           					img = ('<div style="display: flex;">'+
		       	           				 '<button><span><i class="fa fa-search botEventImg" title="Bot Event Output" onclick="displayBotEventResult(\''+full.eventId+'\',\''+ jsonObj.botId+'\',\''+0+'\')"></i></span></button>'+
	           					 '<button style="border: none; background-color: transparent; outline: none;">'+
	           					 '<img src="css/images/list_metro.png" class="details-control botEventImg2" title="Bot Command Event Result" style="margin-left: 5px;"></button>'+
	           					'</div>');
	           				 } else {
	           					img = ('<div style="display: flex;">'+
	       	           				 '<button><span><i class="fa fa-search botEventImg" title="Bot Command Event Output" onclick="displayBotEventResult(\''+full.eventId+'\',\''+ jsonObj.botId+'\',\''+0+'\')"></i></span></button>'+
	       	           		 		'</div>');
	           				 }
	           		 return img;
	            	}
	            },	
		   ],
		   rowCallback: function ( row, data ) {
			  if(data['isModified']) {
				  $(row).find('td:eq(1)').css('backgroundColor', 'orange');
		      }
		   },
		   "oLanguage": {
			   "sSearch": "",
			   "sSearchPlaceholder": "Search all columns"
		   },     
	});
	
	$(function(){ // this will be called when the DOM is ready 
		
		$("#botAuditFile_dataTable_length").css('margin-top','8px');
		$("#botAuditFile_dataTable_length").css('padding-left','35px');
	});
	
	$('#botAuditFile_dataTable').DataTable().columns().every( function () {
        var that = this;
        $('input', this.footer() ).on( 'keyup change', function () {
            if ( that.search() !== this.value ) {
                that
                	.search( this.value, true, false )
                    .draw();
            }
        });
    });
	
	// ----- productVersion child table -----
    $('#botAuditFile_dataTable tbody').on('click', 'td button .botEventImg2', function () {
    	var tr = $(this).closest('tr');
    	var row = botauditFileSingleDT_oTable.DataTable().row(tr);
    	
    	//productSelectedTr = tr;
    	//productSelectedRow = row;
    	
    	botCommandResultHandler(row.data().eventId, row, tr);
    	//$("#div_SingleDataTableBotCommand").modal();
	});
}

//----- product Version child table started -----
function botCommandResultHandler(eventId, row, tr){
	//var botURL='get.botCommands.by.BotId?botId='+botId;
	//assignDataTableValuesForBot(botURL, "childTable1", row, tr);
	botCommandListAuditHistory(eventId,botId);
}

function displayBotEventResult(eventId,botId,botCommandId) {
	var url="load.botEvent.history.by.bot?botId="+botId+"&botCommandId="+botCommandId+"&eventId="+eventId;
	var response='';	
	$("#botEventID").val(response.toString());
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			response = data.Message;
			$("#div_SingleDataTableBot").modal();
			$("#botEventID").jJsonViewer(response.toString());
			$("#botEventID ul").css('overflow-y',"hidden");
		},
		error : function(data){
			closeLoaderIcon();
		},
		complete : function(data){
			closeLoaderIcon();
		}
	});
	
}

function botCommandListAuditHistory(eventId,botId){
	var url='bot.command.event.list?botEventId='+eventId+'&jtStartIndex=0&jtPageSize=10000';
	var jsonObj={"Title":"Bot Command Execution History : [ "+botId+" ] "+botName,
			"botId": botId,
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,
			"componentUsageTitle":"botExecutionAudit",
	};
	SingleDataTableContainer.init(jsonObj);
}

