function listTCExecutionResults(workPackageId, nodeType){
	if(nodeType !='WorkPackage'){
		setWorkPackageNode();
	}
	$('#TestCasesResults').children().show();	
	document.getElementById("ResultsGridView").style.display = "none";
	
	var url ='testcasesexecution.of.wptcexplan.Id?workPackageId='+workPackageId+'&jtStartIndex=0&jtPageSize=10000';
	assignDataTableValuesTCDT(url, "parentTable", "", "");
}

var testCaseJsonData='';
function assignDataTableValuesTCDT(url,tableValue, row, tr){
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
			
			if(tableValue == "parentTable"){
				testCaseJsonData = data;				
				if(!workpackageTestCaseDTFlag)
					tcResults_Container(data, "240px");
				else				
					reloadDataTableHandler(data, tcResults_oTable);				
				
			}else if(tableValue == "childTable1"){
				tsResults_Container(data, row, tr);
				
				fixDataTablePopupHeight('tsResults_dataTable');
	    	   	
			}else if(tableValue == "childTable2"){
				tcDefectsResults_Container(data, row, tr);
			}else{
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

var workpackageTestCaseDTFlag=false;
function testCaseDTFullScreenHandler(flag){
	reInitializeDataTableWKTC();
	
	if(flag){
		$("#tcResults_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
	}else{
		$("#tcResults_dataTable_wrapper .dataTables_scrollBody").css('max-height','400px');
	}	
}

var tcResults_oTable='';
var tsResults_oTable='';
var tcDefecResults_oTable='';

function tcResults_Container(data, scrollYValue){
	try{
		if ($('#tcResults_dataTable').length>0) {
			$('#tcResults_dataTable').dataTable().fnDestroy(); 
		}
	} catch(e) {}
	
	 tcResults_oTable = $('#tcResults_dataTable').dataTable( {
		 "dom": '<"top"Bf<"clear">>rt<"bottom"ip<"clear">>',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		//"bJQueryUI": true,
		//"sPaginationType": "full_numbers",
		"bScrollCollapse": true,
		autoWidth: false,
		bAutoWidth:false,
		"sScrollX": "100%",
       "sScrollXInner": "100%",
       "scrollY":"100%",
       fixedColumns:   {
           leftColumns: 2,
           rightColumns: 1
       }, 
       "fnInitComplete": function(data) {
    	   var searchcolumnVisibleIndex = [12]; // search column TextBox Invisible Column position
    	   if(!workpackageTestCaseDTFlag){
    		   $('#tcResults_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
    	   }
    	   workpackageTestCaseDTFlag=true;
		   reInitializeDataTableWKTC();
	   },  
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'Test Case Result',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Test Case Result',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Test Case Result',
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
              { targets: [0,1,2,3,4,5,6,7,8,9,10,11,12], visible: true},
              { targets: '_all', visible: false }
          ],
                  
         aaData:data,                 
	    aoColumns: [	        
	       { mData: "testCaseExecutionResultId", sWidth: '7%', "render": function (tcData,type,full) {	        
		        var exeId = full.testCaseExecutionResultId;
				var tcId = full.testcaseId;
				var tcName = full.testcaseName;
				var result = exeId+'~@'+tcId+'~@'+tcName;		
	    		return ('<a style="color: #0000FF;" id="'+result+'" onclick="testCaseDetailsForResult(event)">'+exeId+'</a>');		        
		        },
	        },	        
	        { mData: "testcaseId", sWidth: '7%', "render": function (tcData,type,full) {
	        	var dataLevel = "workPackageLevel";
				var tcId = full.testcaseId;
				var tcName = full.testcaseName;						
				var result = tcId+'~@'+dataLevel+'~@'+tcName;						
				document.getElementById("treeHdnCurrentWorkPackageId").value = workPackageId;
				return ('<a style="color: #0000FF;" id="'+result+'" onclick="listTCExecutionSummaryHistory(event)">'+tcId+'</a>');
	        	},
	        },	        
           { mData: "testRunJobId", className: 'disableEditInline', sWidth: '7%'},		
           { mData: "testcaseName", className: 'disableEditInline', sWidth: '10%'},		    				            
           { mData: "testcaseCode", className: 'disableEditInline', sWidth: '7%'},		
           { mData: "startTime", className: 'disableEditInline', sWidth: '7%'},		
           { mData: "endTime", className: 'disableEditInline', sWidth: '7%'},	
           { mData: "testerName", className: 'disableEditInline', sWidth: '10%'},		
           { mData: "result", className: 'disableEditInline', sWidth: '10%'},	
           { mData: "runConfigurationName", className: 'disableEditInline', sWidth: '10%'},
           { mData: "teststepcount", className: 'disableEditInline', sWidth: '5%'},
           { mData: "defectsCount", className: 'disableEditInline', sWidth: '6%'},          
           { 
	    	   mData: '',				 
	    	   sWidth: '2%',
			   "orderable":      false,
			   "data":           data,
           	   mRender: function(data, type, full) {				            	
	       		var img = ('<div style="display: flex;">'+
	       		'<button style="border: none; background-color: transparent; outline: none;">'+
	       				'<i class="fa fa-history executionDetalsTC" title="Execution Summary and History"></i></button>'+
	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
	       				'<img src="css/images/list_metro.png" title="Test Step" onclick="testCaseDetailsTSResult(event)" style="margin-left: 5px;"></button>'+
	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
	       				'<img src="css/images/list_metro.png" title="Export System Details" onclick="testCaseDetailsExportResult(event)"></button>'+
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
	 
	//-----------------
	 $(function(){ // this will be called when the DOM is ready 	    
	    tcResults_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer()).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
	    } ); 
	    
	    /* Use the elements to store their own index */
	   $("#tcResults_dataTable_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input").each( function (i) {
			this.visibleIndex = i;
		} );
		
	 	$("#tcResults_dataTable_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input").keyup( function () {
			/* If there is no visible index then we are in the cloned node */
			var visIndex = typeof this.visibleIndex == 'undefined' ? 0 : this.visibleIndex;
			
			/* Filter on the column (the index) of this element */
			tcResults_oTable.fnFilter( this.value, visIndex );
		} );
		
		// ----- product Workflow status child table -----
	    $('#tcResults_dataTable tbody').on('click', 'td button .executionDetalsTC', function (event) {
	    	var tr = $(this).closest('tr');
	    	var row = tcResults_oTable.DataTable().row(tr);			
			var dataLevel = "productLevel";
			listTCExecutionSummaryHistoryView(row.data().testcaseId, row.data().testcaseName, dataLevel);
			
   		}); 	    
	} ); 
}

//TCExecution Report starts
function listTCExecutionSummaryHistoryView(testCaseId, testCaseName, dataLevel){	
	prodId = document.getElementById("treeHdnCurrentProductId").value;
	var jsonObj={"Title":"TestCase Execution ["+ testCaseId + "] "+testCaseName,
			     "testCaseID":testCaseId,
				 "testCaseName":testCaseName,
				 "testCaseExectionSummayTitle":"Execution Summary",
				 "testCaseExectionSummayUrl": 'result.testcase.execution.summary?tcId='+testCaseId+'&workPackageId=-1&productId='+prodId+'&dataLevel='+dataLevel,
				 "testCaseExectionHistoryTitle":"Execution History",
				 "testCaseExectionHistoryUrl":"testcase.execution.history?testCaseId="+testCaseId+'&workPackageId=-1&dataLevel='+dataLevel,
				 "componentUsageTitle":dataLevel,
	};
	TestCaseExecutionDetails.init(jsonObj);	
}

function testCaseDetailsTSResult(event){
	var tr = event.target.closest('tr');
	var row = tcResults_oTable.DataTable().row(tr);
	tcResultHandler(row, tr);
}

function testCaseDetailsExportResult(event){
	var tr = event.target.closest('tr');
	var row = tcResults_oTable.DataTable().row(tr);
	tcDefectsResultHandler(row, tr);
}

var clearTimeoutDTWkTC='';
function reInitializeDataTableWKTC(){
	clearTimeoutDTWkTC = setTimeout(function(){				
		tcResults_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTWkTC);
	},200);
}

// ----- Test step child table started -----
function tcResultHandler(row, tr){
	var url ='teststepsexecution.of.testcaseexecutionId?wptcepId='+row.data().testCaseExecutionResultId+'&jtStartIndex=0&jtPageSize=1000';
	assignDataTableValuesTCDT(url, "childTable1", row, tr);	
	$("#testCaseTestStepsContainer").modal();
}

function tsChild1Table(){
		var childDivString = '<table id="tsResults_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead>'+
			'<tr>'+
				'<th>Test Step Id</th>'+
				'<th>TestStep Execution Id</th>'+
				'<th>Test Step Name</th>'+
				'<th>Test Step Input</th>'+
				'<th>TestCase Code</th>'+
				'<th>Description</th>'+
				'<th>Expected Output</th>'+
				'<th>Observed Output</th>'+
				'<th>Status</th>'+
				'<th>Start Time</th>'+		
				'<th>End Time</th>'+
				'<th>Remarks</th>'+	      	
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

function tsResults_Container(data, row, tr){	
	try{
		if ($("#testCaseTestStepsDTContainer").children().length>0) {
			$("#testCaseTestStepsDTContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = tsChild1Table(); 			 
	$("#testCaseTestStepsDTContainer").append(childDivString);
	
	 tsResults_oTable = $("#tsResults_dataTable").dataTable( {  		   
		 	"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
			autoWidth: false,
			bAutoWidth:false,
			"sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       /*fixedColumns:   {
	           leftColumns: 3,
	       }, */
	       "fnInitComplete": function(data) {   
	    	   var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	    	   $('#tsResults_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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

		   },  
		   buttons: [
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Test Steps',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Test Steps',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Test Steps',
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
           { mData: "testStepId" ,className: 'disableEditInline'},		
           { mData: "teststepexecutionresultId" ,className: 'disableEditInline'},		
           { mData: "testStepName" ,className: 'disableEditInline'},
           { mData: "testStepInput" ,className: 'disableEditInline'},
           { mData: "testStepCode" ,className: 'disableEditInline'},		
           { mData: "testStepDescription" ,className: 'disableEditInline'},	
           { mData: "testStepExpectedOutput" ,className: 'disableEditInline'},
           { mData: "testStepObservedOutput" ,className: 'disableEditInline'},
           { mData: "testResultStatus" ,className: 'disableEditInline'},
           { mData: "startTime" ,className: 'disableEditInline'},
           { mData: "endTime" ,className: 'disableEditInline'},
           { mData: "executionRemarks" ,className: 'disableEditInline'},
       ],
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	 
	 $(function(){ // this will be called when the DOM is ready
			
		$("#tsResults_dataTable_length").css('margin-top','8px');
		$("#tsResults_dataTable_length").css('padding-left','35px');
		
		$('#tsResults_dataTable').DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });
		
		$('#tsResults_dataTable_wrapper .DTFC_LeftFootWrapper').addClass('hidden');
	});
}

function tcDefectsResultHandler(row, tr){
	var url ='export.name.code.list?testCaseExecutionResultId='+row.data().testCaseExecutionResultId+'&jtStartIndex=0&jtPageSize=50';
	assignDataTableValuesTCDT(url, "childTable2", row, tr);	
	$("#testCaseExportDetailsContainer").modal();
}

function tsChild2Table(){
		var childDivString = '<table id="tcDefectsResults_dataTable"  class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead>'+
			'<tr>'+
				'<th>Test Management System</th>'+
				'<th>Export System Result Id</th>'+
				'<th>Exported Date</th>'+
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

function tcDefectsResults_Container(data, row, tr){
	try{
		if ($("#testCaseExportDetailsDTContainer").children().length>0) {
			$("#testCaseExportDetailsDTContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = tsChild2Table(); 			 
	$("#testCaseExportDetailsDTContainer").append(childDivString);
	
	tcDefecResults_oTable = $("#tcDefectsResults_dataTable").dataTable( {
			"dom": '<"top"Bf<"clear">>rt<"bottom"ip<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
			autoWidth: false,
			bAutoWidth:false,
			"sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "fnInitComplete": function(data) {
	    	   var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	    	   $('#tcDefectsResults_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
	    	   reInitializeTCDefecResultsDT();
		   },  
		   buttons: [
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Export System Details',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Export System Details',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Export System Details',
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
	          ],      			        
        aaData:data,		    				 
	    aoColumns: [	        	        
            { mData: "testSystemName" ,className: 'disableEditInline'},		
            { mData: "resultCode" ,className: 'disableEditInline'},		
            { mData: "exportedDate" ,className: 'disableEditInline'},
       ],
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },      
	});

	tcDefecResults_oTable.DataTable().columns().every( function () {
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
var clearTimeoutTCDefecResultsDT='';
function reInitializeTCDefecResultsDT(){
	clearTimeoutTCDefecResultsDT = setTimeout(function(){				
		tcDefecResults_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTCDefecResultsDT);
	},200);
}

