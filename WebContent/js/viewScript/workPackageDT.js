var test = '';
var wpkageIdArr = [];
var workPackageChecked = false;
var testJobId = "";
var lastLine = 1;
var jobLogContent = "";
var automationMode = "";
var useIntelligentTestPlan = "";

function workPackageExecutionDetailsBuildLevel(prodbuildtype){	
		var url="";
		var testFactoryId=-1;
		if(prodbuildtype == "build"){
			prodId = -1;
		}else if(prodbuildtype == "prod"){
			productBuildId = -1;
		}else if(prodbuildtype == "testFactory"){
			testFactoryId = document.getElementById("treeHdnCurrentTestFactoryId").value;
		}
		
		$('#selectTestRunPlanRowCount').find('option[value="'+pageCountSelected+'"]').attr("selected", true);
		initWorkpackagePagination(0, pageCountSelected);
}

var workPackageJsonData='';
var testRunJobJsonData='';
function assignWPDataTableValues(url,tableValue, row, tr){
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
				testRunJobJsonData = data;
				data = data.Records;
			}
			
			if(tableValue == "parentTable"){
				workpackage_Headerchange();
				workPackageJsonData = data;
				
				if(!workPackageDTFlag){
					workPackage_Container(data, "240px");
				}
				else{				
					reloadDataTableHandler(data, workPackage_oTable);
				}
				adjustSideBarHeight();
				
			}else if(tableValue == "childTable1"){
				wpAuditHistory_Container(data, row, tr);
				
			}else if(tableValue == "childTable2"){
				var gridViewFlag = $($('#workPacakgeRadioGroup label')[0]).hasClass('active');
				if(gridViewFlag){
					wpTestRunJobs_Container(data, row, tr);
				}else{
					testRunJobsTileView(testRunJobJsonData);
				}
				
			}else if(tableValue == "childTable3"){
				wpTestRunCases_Container(data, row, tr);
				
			}else if(tableValue == "childTable4"){
				wpAuditHistory_Container(data, row, tr);
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

var workPackageDTFlag=false;
var workPackage_oTable='';
var wpTestRunJob_oTable='';
var wpTestRuncase_oTable='';
var wkpResults_oTable='';

function workPackage_Container(data, scrollYValue){
	try{
		if ($('#workPackage_dataTable').length>0) {
			$('#workPackage_dataTable').dataTable().fnDestroy(); 
		}
	} catch(e) {}
	
	workPackage_oTable = $('#workPackage_dataTable').dataTable( {
		"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"bScrollCollapse": true,
		autoWidth: false,
		bAutoWidth:false,
		"sScrollX": "100%",
       "sScrollXInner": "100%",
       "scrollY":"100%",
       /*fixedColumns:   {
           leftColumns: 3
           //rightColumns: 0
       },*/ 
       "fnInitComplete": function(data) {
    	   var searchcolumnVisibleIndex = [0,21]; // search column TextBox Invisible Column position
    	   if(!workPackageDTFlag){
    		   $('#workPackage_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		   reInitializeDataTableWKP();
    	   workPackageDTFlag=true;
	   },  
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'WorkPackages',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'WorkPackages',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'WorkPackages',
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
				    text: '<i class="fa fa-desktop" title="View Consolidated Report" aria-hidden="true"></i>',
				    action: function ( e, dt, node, config ) {
				    	if(workPackageChecked){
					    	$('#workpackgeMultiselectDD').modal();
					    	$('#workpackgeMultiselectDDContainer').text("Generating Consolidated report for the following Workpackage IDs : " +wpkageIdArr);
					    	$('.multiWorkPackageCheckbox input').prop('checked',false);
					    	workPackageChecked = false;
				    	}else{
				    		callAlert('Please select Work Package ID for consolidated Report');
				    	}
				    }
				 },
	            {
				    text: '<i class="fa fa-download" title="Download Consolidated Report" aria-hidden="true"></i>',
				    action: function ( e, dt, node, config ) {
				    	if(workPackageChecked){
				    		workPackageChecked = false;
					    	$('.multiWorkPackageCheckbox input').prop('checked',false);
				    		workpackgeMultiselectDDSubmitHandler('downloadReport');
				    	}else{
				    		callAlert('Please select Work Package ID for consolidated Report');
				    	}
				    }
				 }
				 
         ], 
         columnDefs: [
              { targets: [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21], visible: true},
              { targets: '_all', visible: false },
              {
		         'targets': 0,
		         'searchable': false,
		         'orderable': false,
		         'className': 'dt-body-center',
		         'render': function (data, type, full, meta){
		             return '<input type="checkbox" name="id[]" value="' + $('<div/>').text(data).html() + '">';
		         }
		     }
          ],
                  
         aaData:data,                 
	    aoColumns: [
	       { mData: null, className: 'multiWorkPackageCheckbox', sWidth: '1%' },	
	       { mData: "workPackageId", className: 'disableEditInline' , sWidth: '5%'},	        
	       { mData: "workPackageName", className: 'disableEditInline' , sWidth: '15%',
	    	   mRender: function (data, type, full) {
					var wpkId = full.workPackageId;
					var exeType = full.exeType;
					var tpId = full.testPlanId;
					var pdtId = full.productId;
					var result = wpkId+'~'+exeType+'~'+tpId+'~'+pdtId;	
					data = ('<a style="color: #0000FF;" id='+result+' onclick="showWorkpackageSummaryFromTable(event)">'+data+'</a>');
		            return data;
	    	   }
	       },	        
           { mData: "productName", className: 'disableEditInline' , sWidth: '10%'},		    				            
           { mData: "exeType", className: 'disableEditInline' , sWidth: '5%'},	
           { mData: "executionMode", className: 'disableEditInline' , sWidth: '5%'},
           { mData: "wpStatus", className: 'disableEditInline' , sWidth: '5%'},		
           { mData: "result", className: 'disableEditInline' , sWidth: '5%'},	
           { mData: "firstActualExecutionDate", className: 'disableEditInline' , sWidth: '5%'},		
           { mData: "lastActualExecutionDate", className: 'disableEditInline' , sWidth: '5%'},	
           { mData: "jobsCount", className: 'disableEditInline' , sWidth: '4%'},
           { mData: "p2totalPass", className: 'disableEditInline' , sWidth: '4%'},
           { 
	    	   mData: '',				 
	    	   sWidth: '4%',
	    	   className: 'disableEditInline',
           	   mRender: function(data, type, full) {
           		   var totVal = full.p2totalBlock + full.p2totalFail + full.p2totalNoRun + full.p2totalPass + full.notExecuted;
           		   var percentVal = Math.round((full.p2totalPass / totVal) * 100);
           		   (isNaN(percentVal)) ? percentVal=0 : percentVal=percentVal;           		   
           		   return percentVal + " %";
           	   }
           	},
           { mData: "p2totalFail", className: 'disableEditInline' , sWidth: '4%'},
           { 
	    	   mData: '',				 
	    	   sWidth: '4%',
	    	   className: 'disableEditInline',
           	   mRender: function(data, type, full) {
           		   var totVal = full.p2totalBlock + full.p2totalFail + full.p2totalNoRun + full.p2totalPass + full.notExecuted;
           		   var percentVal = Math.round((full.p2totalFail / totVal) * 100);
           		   (isNaN(percentVal)) ? percentVal=0 : percentVal=percentVal;           		   
           		   return percentVal + " %";
           	   }
           	},
           { mData: "p2totalBlock", className: 'disableEditInline' , sWidth: '4%'},   
          // { mData: "p2totalNoRun", className: 'disableEditInline' , sWidth: '4%'},
           { mData: "totalWPTestCase", className: 'disableEditInline' , sWidth: '5%'},
           { mData: "totalExecutedTesCases", className: 'disableEditInline' , sWidth: '5%'},
          // { mData: "notExecuted", className: 'disableEditInline' , sWidth: '4%'},
           { 
	    	   mData: '',				 
	    	   sWidth: '4%',
	    	   className: 'disableEditInline',
           	   mRender: function(data, type, full) {
           		   var notExecuted = full.totalWPTestCase - (full.p2totalPass +  full.p2totalFail);           		   
           		   return notExecuted;
           	   }
           	},
           { mData: "teststepcount", className: 'disableEditInline' , sWidth: '5%'},
           { mData: "defectsCount", className: 'disableEditInline' , sWidth: '5%'},
           { 
	    	   mData: '',				 
	    	   sWidth: '15%',
			   "orderable":      false,
			   "data":           data,
           	   mRender: function(data, type, full) {				            	
	       		var img;
				img	= '<div style="display: flex;">'+
	       		'<button style="border: none; background-color: transparent; outline: none;">'+
	       				'<i class="fa fa-search-plus wp-details-control" title="Audit History"></i></button>'+
	       		'<button style="border: none; background-color: transparent; outline: none;">'+
	       				'<i class="fa fa-comments wp-details-control2" title="Comments"></i></button>'+
	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
	       				'<img src="css/images/list_metro.png" class="wp-details-control3" title="Test Jobs"></button>'+
				'<button style="border: none; background-color: transparent; outline: none;" onclick="reRunTestPlan('+full.testPlanId+',\''+full.testPlanExecutionMode+'\',\''+full.useIntelligentTestPlan+'\')">'+
	       				'<i class="fa fa-repeat" title="Repeat Execution"></i></button>';			
				if(full.wpStatus=="---"){
					img += 	'<button style="border: none; background-color: transparent; outline: none;" onclick="abortWorkPackage('+full.workPackageId+')">'+
	       				'<i class="fa fa-stop" title="Abort WorkPackage"></i></button>';
				}
				img += '</div>';
		
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
		 
		 adjustSideBarHeight();
	    
		 workPackage_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
	    } );
	   
		/* Use the elements to store their own index */
	   $("#workPackage_dataTable_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input").each( function (i) {
			this.visibleIndex = i;
		} );
		
	 	$("#workPackage_dataTable_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input").keyup( function () {
			/* If there is no visible index then we are in the cloned node */
			var visIndex = typeof this.visibleIndex == 'undefined' ? 0 : this.visibleIndex;
			
			/* Filter on the column (the index) of this element */
			//workPackage_oTable.fnFilter( this.value, visIndex );
		} );
	    
	    // ----- test step child table -----
	    $('#workPackage_dataTable tbody').on('click', 'td button .wp-details-control', function () {
	    	var tr = $(this).closest('tr');
	    	var row = workPackage_oTable.DataTable().row(tr);
	    	wpAuditHistoryHandler(row, tr);
   		});	    
		
	    // ----- comments child table -----
	    $('#workPackage_dataTable tbody').on('click', 'td button .wp-details-control2', function () {
	    	var tr = $(this).closest('tr');
	    	var row = workPackage_oTable.DataTable().row(tr);
	    	//wpAuditHistoryHandler(row, tr);
				var entityTypeIdComments = 2;
				var entityNameComments = "Workpackage";
				listComments(entityTypeIdComments, entityNameComments, row.data().workPackageId, row.data().workPackageName, "workPackageComments");
			
   		});
		
	    // ----- export child table -----
	    $('#workPackage_dataTable tbody').on('click', 'td button .wp-details-control3', function () {
	    	var tr = $(this).closest('tr');
	    	var row = workPackage_oTable.DataTable().row(tr);
	    	wpTestRunJobHandler(row, tr);    		
	    	
   		});	  
	    
	    $('#workPackage_dataTable').on( 'click', 'tbody td.multiWorkPackageCheckbox input:checkbox', function (e) {
		    var tr = $(this).closest('tr');
	    	var row = workPackage_oTable.DataTable().row(tr);
	    	if (this.checked) {
	    		workPackageChecked = true;
	    		wpkageIdArr.push(row.data().workPackageId);
	        } else {
	        	workPackageChecked = false;
	        	wpkageIdArr.splice($.inArray(row.data().workPackageId, workPackageId),1);
	        }
	    });
	    
	   // $("#workPackage_dataTable_wrapper .DTFC_LeftFootWrapper").addClass('hidden');
	    
	    $("#workPackage_dataTable_length").css('margin-top','8px');
		$("#workPackage_dataTable_length").css('padding-left','35px');
	} ); 
}

var clearTimeoutDTWk='';
function reInitializeDataTableWKP(){	
	clearTimeoutDTWk =setTimeout(function(){				
		workPackage_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTWk);
	},2000);
}

function workPackageDTFullScreenHandler(flag){
	if(flag){
		$("#workPackage_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
	}else{
		$("#workPackage_dataTable_wrapper .dataTables_scrollBody").css('max-height','400px');
	}	
}

function wpAuditHistoryHandler(row, tr){
	var url='administration.event.list?sourceEntityType=WorkPackage&sourceEntityId='+row.data().workPackageId+'&jtStartIndex=0&jtPageSize=1000';
	var jsonObj={"Title":"WorkPackage Audit History:",
			"url": url,	 
			"jtStartIndex":0,
			"jtPageSize":1000,
			"componentUsageTitle":"workPackageAudit",
	};
	SingleDataTableContainer.init(jsonObj);
}

function workPackageTestRunPlanFromGrid(workpackId){
	workpackageId = workpackId;
	var url = 'workpackage.testcase.execution.summary.build.list?workPackageId='+workpackId+'&productBuildId='+productBuildId+'&jtStartIndex=0&jtPageSize=1000';
	assignWPDataTableValues(url, "childTable2", "", "");
	$('#wpTestRunJobDTContainer').hide();
	$('#wpTestRunJobDTContainerTileView').show();
	$("#wpTestRunJobContainer").modal();
	$('#wpTestRunJobContainer h4').text(workpackageId+' - Test Jobs');
}

function workPackageCommentsFromGrid(workpackId, workpackageName){
	var entityTypeIdComments = 2;
	var entityNameComments = "Workpackage";
	listComments(entityTypeIdComments, entityNameComments, workpackId, workpackageName, "workPackageComments");
	
	//$("#div_CommentsUI .green-haze").show();
}

function workPackageAuditHistoryFromGrid(workpackId){
	var url='administration.event.list?sourceEntityType=WorkPackage&sourceEntityId='+workpackId+'&jtStartIndex=0&jtPageSize=1000';
	var jsonObj={"Title":"WorkPackage Audit History:",
			"url": url,	 
			"jtStartIndex":0,
			"jtPageSize":1000,
			"componentUsageTitle":"workPackageAudit",
	};
	SingleDataTableContainer.init(jsonObj);
}

function workPackageProgressComponent(){
	var jsonObj={"Title":"Progress Steps Component",
		"url": 'serverURL',	 
		"jtStartIndex":0,
		"jtPageSize":1000,
		"componentUsageTitle":"Progress Steps Component",
	};
	ProgressSteps.init(jsonObj);
}

function wpTestRunJobHandler(row, tr){
	workpackageId = row.data().workPackageId;
	var url = 'workpackage.testcase.execution.summary.build.list?workPackageId='+row.data().workPackageId+'&productBuildId='+productBuildId+'&jtStartIndex=0&jtPageSize=1000';
	assignWPDataTableValues(url, "childTable2", row, tr);
	$('#wpTestRunJobDTContainer').show();
	$('#wpTestRunJobDTContainerTileView').hide();
	$('#wpTestRunJobContainer h4').text(workpackageId+' - Test Jobs');
	$("#wpTestRunJobContainer").modal();
}

function wpChild2Table(){
	var childDivString = '<table id="wpTestRunJobs_dataTable"  class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Id</th>'+
			'<th>Environment</th>'+
			'<th>Status</th>'+
			'<th>Result</th>'+
			'<th>Comments</th>'+
			'<th>Evidence</th>'+
			'<th>Passed</th>'+
			'<th>Failed</th>'+
			'<th>Blocked</th>'+
			//'<th>Not Run</th>'+
			'<th>Total TC</th>'+
			'<th>Total TC Executed</th>'+
			'<th>Not executed</th>'+
			'<th>Test Steps</th>'+
			'<th>Defects</th>'+
			'<th>Testcases</th>'+
			
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
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			//'<th></th>'+
		'</tr>'+
	'</tfoot>'+	

	'</table>';		
	
	return childDivString;	
}

function wpTestRunJobs_Container(data, row, tr){
try{
	if ($("#wpTestRunJobDTContainer").children().length>0) {
		$("#wpTestRunJobDTContainer").children().remove();
	}
} 
catch(e) {}

var childDivString = wpChild2Table(); 			 
$("#wpTestRunJobDTContainer").append(childDivString);

wpTestRunJob_oTable = $("#wpTestRunJobs_dataTable").dataTable( {
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
       fixedColumns:   {
           leftColumns: 2,
       },
       "fnInitComplete": function(data) {
    	   var searchcolumnVisibleIndex = [14]; // search column TextBox Invisible Column position
    	   $('#wpTestRunJobs_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
	                    	title: 'Test Jobs',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Test Jobs',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Test Jobs',
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
        { mData: "testRunJobId", className: 'disableEditInline' , sWidth: '7%' },
        { mData: "environmentCombination", className: 'disableEditInline' , sWidth: '7%' },
        { mData: "testRunStatusName", className: 'disableEditInline' , sWidth: '7%' },		
        { mData: "result", className: 'disableEditInline' , sWidth: '7%' },
        { mData: "testRunFailureMessage", className: 'disableEditInline' , sWidth: '7%' },
        { mData: "testRunEvidenceMessage", className: 'disableEditInline' , sWidth: '7%' },
        { mData: "passedCount" , className: 'disableEditInline' , sWidth: '5%'},
        { mData: "failedCount", className: 'disableEditInline' , sWidth: '5%'},
        { mData: "blockedCount", className: 'disableEditInline', sWidth: '5%' },
        { mData: "totalTestCaseCount", className: 'disableEditInline', sWidth: '5%' },
        { mData: "totalTestCaseForExecutionCount", className: 'disableEditInline', sWidth: '5%' },
        { mData: "notexecutedCount", className: 'disableEditInline', sWidth: '5%' },
        { mData: "teststepcount", className: 'disableEditInline', sWidth: '5%' },
        { mData: "defectsCount", className: 'disableEditInline', sWidth: '5%' },
        { 
	    	mData: '',				 
	    	sWidth: '6%',
			"orderable":      false,
			"data":           data,
        	mRender: function(data, type, full) {	
				var passPercentage = 0;			
				if(full.totalTestCaseCount == 0){
					passPercentage=0;
				}else{
					passPercentage = (full.passedCount * 100)/(full.totalTestCaseCount);
					passPercentage = passPercentage.toFixed(0);
				}
				
				var failedPercentage = 0;			
				if(full.totalTestCaseCount == 0){
					failedPercentage=0;
				}else{
					failedPercentage = (full.failedCount * 100)/(full.totalTestCaseCount);
					failedPercentage = failedPercentage.toFixed(0);
				}
			
	       		var img;
				img = '<div style="display: flex;">'+
	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
	       				'<img src="css/images/list_metro.png" class="wp-details-control3" title="Test Run cases"></button>'+
	       		'<button style="border: none; background-color: transparent; outline: none;">'+
	       				'<i class="fa fa-comments wp-details-control4" title="Comments"></i></button>'+
				'<button style="border: none; background-color: transparent; outline: none;" onclick="liveLog('+full.testRunJobId+','+lastLine+')">'+
	       				'<i class="fa fa-file-text" title="Live Log"></i></button>';

				if((passPercentage != 'NaN' && passPercentage != 0 )|| (failedPercentage != 'NaN' && failedPercentage != 0)){
					img += '<button style="border: none; background-color: transparent; outline: none;" onclick="exportWPResultshtmlEvidenceTestRunJob('+full.testRunJobId+');">'+
	       				'<i class="fa fa-desktop" title="Job Evidence HTML Report"></i></button>';
				}		
				if(full.testRunStatus != 5 && full.testRunStatus != 6 && full.testRunStatus != 7 ){
					img += '<button style="border: none; background-color: transparent; outline: none;" onclick="abortjob('+full.testRunJobId+')">'+
	       				'<i class="fa fa-stop" title="Abort Job"></i></button>';
				}
				
				img += '</div>';		
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

	//----- testcases  child table -----
	$('#wpTestRunJobs_dataTable tbody').on('click', 'td button .wp-details-control3', function () {
		var tr = $(this).closest('tr');
		var row = wpTestRunJob_oTable.DataTable().row(tr);
		wpTestRunCaseHandler(row, tr);
		});
		
	$('#wpTestRunJobs_dataTable').DataTable().columns().every( function () {
        var that = this;
        $('input', this.footer() ).on( 'keyup change', function () {
            if ( that.search() !== this.value ) {
                that
                	.search( this.value, true, false )
                    .draw();
            }
        });
    });
	
	$('#wpTestRunJobs_dataTable_wrapper .DTFC_LeftFootWrapper').addClass('hidden');
	
	   $('#wpTestRunJobs_dataTable tbody').on('click', 'td button .wp-details-control4', function () {
	   	var tr = $(this).closest('tr');
	   	var row = wpTestRunJob_oTable.DataTable().row(tr);
	   	//wpAuditHistoryHandler(row, tr);
			var entityTypeIdComments = 10;
			var entityNameComments = "TestRunJob";
			listComments(entityTypeIdComments, entityNameComments, row.data().testRunJobId, row.data().testRunStatusName, "testRunJobComments");
			
   	});
  });
}



var optionsTestCaseExecutionResultArr=[];
var optionsTestCaseExecutionResult=[];
var optionsItemTestCaseExecutionResult=0;
var optionsType_TestCaseExecutionResult="Testcase Execution Result";

function testCaseExecutionResultContainer(data, scrollYValue){
	var entityId=0;
	//var workflowURL = 'workflow.master.mapped.to.entity.list.options?productId='+prodId+'&entityTypeId=79&entityId='+entityId;
	var workflowStatusURL='workflow.getAllStatus.master.option.list?productId='+prodId+'&entityTypeId=79&entityId='+entityId;
	optionsItemTestCaseExecutionResult=0;
	optionsTestCaseExecutionResult=[];
	optionsTestCaseExecutionResultArr = [{id:"assigneeId", type:optionsType_TestCaseExecutionResult, url:'common.user.list.by.resourcepool.id.productId?productId='+prodId},
	                  				   {id:"reviewerId", type:optionsType_TestCaseExecutionResult, url:'common.user.list.by.resourcepool.id.productId?productId='+prodId},
	                  				 {id:"workflowId", type:optionsType_TestCaseExecutionResult, url:workflowStatusURL},
	                  				{id:"analysisOutCome", type:optionsType_TestCaseExecutionResult, url:'testcase.execution.result.analysis.outcome.option.list'},
	              ];
	returnTestcaseExecutionResult(optionsTestCaseExecutionResultArr[0].url, scrollYValue, data, "");
}


function returnTestcaseExecutionResult(url, scrollYValue, data, tr){
	$.ajax( {
 	   "type": "POST",
        "url":  url,
        "dataType": "json",
         success: function (json) {
         if(json.Result == "Error" || json.Options == null){
         	callAlert(json.Message);
         	json.Options=[];
         	optionsTestCaseExecutionResult.push(json.Options);
         	
         	if(optionsTestCaseExecutionResultArr[0].type == optionsType_TestCaseExecutionResult){
         		wpkResults_Container(data, scrollYValue);     			   
  		   }	
         }else{
     	   if(json.Options.length>0){     		   
			   for(var i=0;i<json.Options.length;i++){
				   json.Options[i].label=json.Options[i].DisplayText;
				   json.Options[i].value=json.Options[i].Value;
			   }			   
     	   }else{
     		  json.Options=[];
     	   }     	   
     	  optionsTestCaseExecutionResult.push(json.Options);
     	   
     	   if(optionsItemTestCaseExecutionResult<optionsTestCaseExecutionResultArr.length-1){
     			 //optionsTestCaseExecutionResultArr[optionsTestCaseExecutionResultArr.length].url = optionsTestCaseExecutionResultArr[optionsTestCaseExecutionResultArr.length].url;		
     		     returnTestcaseExecutionResult(optionsTestCaseExecutionResultArr[optionsTestCaseExecutionResult.length].url, scrollYValue, data, tr);     		  
     	   }else{
     		   if(optionsTestCaseExecutionResultArr[0].type == optionsType_TestCaseExecutionResult){
     			  wpkResults_Container(data, scrollYValue);     			   
     		   }
     		}
     	  optionsItemTestCaseExecutionResult++;     	   
         }
         },
         error: function (data) {
        	 optionsItemTestCaseExecutionResult++;
        	 
         },
         complete: function(data){
         	console.log('Completed');
         	
         },	            
   	});	
	
}




function wpTestRunCaseHandler(row, tr){
	var url = 'testcasesexecution.of.testrunjob.Id?testRunJobId='+row.data().testRunJobId+'&jtStartIndex=0&jtPageSize=1000';
	assignWPDataTableValues(url, "childTable3", row, tr);
	$("#wpTestRunCaseContainer").modal();
	
}

function wpTestRunCases_Container(data, row, tr){
	testCaseExecutionResultContainer(data, "350px");
}

function createWorkPackageHeader(){
	var childDivString = '<table id="workPackageTestRun_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th class="dataTableChildHeaderTitleTH">Testcase ID</th>'+
			'<th class="dataTableChildHeaderTitleTH">Testcase Execution Id</th>'+
			'<th class="dataTableChildHeaderTitleTH">Testcase Name</th>'+
			'<th class="dataTableChildHeaderTitleTH">Testcase Code</th>'+
			'<th class="dataTableChildHeaderTitleTH">Start Time</th>'+
			'<th class="dataTableChildHeaderTitleTH">End Time</th>'+
			'<th class="dataTableChildHeaderTitleTH">Executed By</th>'+
			'<th class="dataTableChildHeaderTitleTH">Result</th>'+
			/*'<th class="dataTableChildHeaderTitleTH">Comments</th>'+*/		
			'<th class="dataTableChildHeaderTitleTH">Environment</th>'+
			'<th class="dataTableChildHeaderTitleTH">TestSteps</th>'+
			'<th class="dataTableChildHeaderTitleTH">Defects</th>'+							              			
			'<th class="dataTableChildHeaderTitleTH"></th>'+					
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
			/*'<th></th>'+*/
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;
}

// -----
var testCaseExecutionResult_editor='';

function wpkResults_Container(data, scrollYValue){
	try{
		/*if ($('#workPackageTestRun_dataTable').length>0) {
			$('#workPackageTestRun_dataTable').dataTable().fnDestroy(); 
		}*/
		if ($("#wpTestRunCaseDTContainer").children().length>0) {
			$("#wpTestRunCaseDTContainer").children().remove();
		}
	} catch(e) {}
	
	var childDivString = createWorkPackageHeader(); 			 
	$("#wpTestRunCaseDTContainer").append(childDivString);
	
	testCaseExecutionResult_editor = new $.fn.dataTable.Editor( {
		"table": "#workPackageTestRun_dataTable",
		ajaxUrl: "process.tesecase.executionResult.update",
		idSrc:  "testCaseExecutionResultId",
		i18n: {
	       /* create: {
	            title:  "Create a new Activity",
	            submit: "Create",
	        }*/
	    },
		fields: [{								
			label:"testcaseId",
			name:"testcaseId",					
			type: 'hidden',				
		},{
            label: "testCaseExecutionResultId",
            name: "testCaseExecutionResultId",
            "type": "hidden",
        },{
            label: "Testcase Name",
            name: "testcaseName",                
        },{
            label: "Testcase Code",
            name: "testcaseCode",
            "type": "hidden",
        },{
            label: "Start Time",
            name: "startTime",
            "type": "hidden",
        },{
            label: "End Time",
            name: "endTime",
            "type": "hidden",
        },{
            label: "Tester Name",
            name: "testerName",
            "type"  : "hidden",
        }, {
            label: "Result",
            name: "result",
            "type"  : "hidden",
        },/*{
            label: "Comments",
            name: "comments",  
            "type"  : "hidden",				
        },*/{
            label: "Teststep count",
            name: "teststepcount",
        },
        {
            label: "Defects Count",
            name: "defectsCount",
        },/*{
            label: "Status",
            name: "workflowStatusId",
            type : "select",
            options: optionsTestCaseExecutionResult[2],
        },{
            label: "Assignee",
            name: "assigneeId",
            type : "select",
            options: optionsTestCaseExecutionResult[0],
        },{
            label: "Reviewer",
            name: "reviewerId",
            type : "select",
            options: optionsTestCaseExecutionResult[1],
        },{
            label: "Analysis OutCome",
            name: "analysisOutCome",
            type : "select",
            options: optionsTestCaseExecutionResult[3],
        }, 
        {
            label: "Analysis Remarks",
            name: "analysisRemarks",
        },  */     
        
    ]
	});	
	
	 wkpResults_oTable = $('#workPackageTestRun_dataTable').dataTable( {
		dom: "Bfrtilp",
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"bScrollCollapse": true,
		autoWidth: false,
		bAutoWidth:false,
		"sScrollX": "100%",
       "sScrollXInner": "100%",
       "scrollY":"100%",
       /*fixedColumns:   {
           leftColumns: 2,
           rightColumns: 1
       },*/ 
       "fnInitComplete": function(data) {
    	   var searchcolumnVisibleIndex = [11]; // search column TextBox Invisible Column position
    	   $('#workPackageTestRun_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
    	   reInitializeWPKDataTableTC();
	   },  
	   buttons: [
	             { extend: "create", editor: testCaseExecutionResult_editor },
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'Testcase Result',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Testcase Result',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Testcase Result',
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
         /*columnDefs: [
              { targets: [0,1,2,3,4,5,6,7,8,9,10], visible: true},
              { targets: '_all', visible: false }
          ],*/
                  
         aaData:data,                 
	    aoColumns: [	        
	        { mData: "testcaseId", sWidth: '7%', "render": function (tcData,type,full) {
	        	var dataLevel = "workPackageLevel";
				var tcId = full.testcaseId;
				var tcName = full.testcaseName;						
				var result = tcId+'~@'+dataLevel+'~@'+tcName;						
				document.getElementById("treeHdnCurrentWorkPackageId").value = workpackageId;
				return ('<a style="color: #0000FF;" id="'+result+'" onclick="listTCExecutionSummaryHistory(event)">'+tcId+'</a>');
	        	},
	        },	        
	       { mData: "testCaseExecutionResultId", sWidth: '7%', "render": function (tcData,type,full) {	        
		        var exeId = full.testCaseExecutionResultId;
				var tcId = full.testcaseId;
				var tcName = full.testcaseName;
				var result = exeId+'~@'+tcId+'~@'+tcName;		
	    		return ('<a style="color: #0000FF;" id="'+result+'" onclick="testCaseDetailsForResult(event)">'+exeId+'</a>');		        
		        },
	        },	        
           { mData: "testcaseName", className: 'disableEditInline' , sWidth: '10%'},		    				            
           { mData: "testcaseCode", className: 'disableEditInline' , sWidth: '7%'},		
           { mData: "startTime", className: 'disableEditInline' , sWidth: '7%'},		
           { mData: "endTime", className: 'disableEditInline' , sWidth: '7%'},	
           { mData: "testerName", className: 'disableEditInline' , sWidth: '10%'},		
           { mData: "result", className: 'disableEditInline' , sWidth: '10%'},	
           /*{ mData: "comments", className: 'disableEditInline' , sWidth: '10%'},*/
           { mData: "runConfigurationName", className: 'disableEditInline' , sWidth: '10%'},
           { mData: "teststepcount", className: 'disableEditInline' , sWidth: '5%'},
           { mData: "defectsCount", className: 'disableEditInline' , sWidth: '6%'},         
           /*{ mData: "workflowStatusName", sWidth: '7%', "render": function (tcData,type,full) {	        
			 	var entityInstanceId = full.testCaseExecutionResultId;
				var entityInstanceName = full.result;	
				var modifiedById = full.assigneeId;
				var currentStatusId = full.workflowStatusId;
				var currentStatusDisplayName = full.workflowStatusName;
				var entityId =0;	
				var secondaryStatusId = full.secondaryStatusId;
				var visibleEventComment=full.visibleEventComment;
				var prodcutId=prodId					
				return ('<a onclick="addTestCaseExecutionResultComments('+prodcutId+','+entityInstanceId+',\''+entityInstanceName+'\','+modifiedById+','+currentStatusId+',\''+currentStatusDisplayName+'\','+entityId+','+secondaryStatusId+','+visibleEventComment+')">'+currentStatusDisplayName+'</a>');		        
		      },
       },
           { mData: "assigneeName", className: 'editable', sWidth: '10%', editField: "assigneeId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(testCaseExecutionResult_editor, 'assigneeId', full.assigneeId);
		           	}else if(type == "display"){
						data = full.assigneeName;
					 }	           	 
					 return data;
				 },
			},	            
			{ mData: "reviewerName", className: 'editable', sWidth: '10%', editField: "reviewerId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(testCaseExecutionResult_editor, 'reviewerId', full.reviewerId);
		           	 }
					 else if(type == "display"){
						data = full.reviewerName;
					 }	           	 
					 return data;
				 },
			},
           { mData: "analysisOutCome", className: 'editable', sWidth: '10%', editField: "analysisOutCome",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(testCaseExecutionResult_editor, 'analysisOutCome', full.analysisOutCome);
		           	 }
					 else if(type == "display"){
						data = full.analysisOutCome;
					 }	           	 
					 return data;
				 },
			},
			 { mData: "analysisRemarks", className: 'editable' , sWidth: '6%'},    */     
           
           { 
	    	   mData: '',				 
	    	   sWidth: '2%',
			   "orderable":      false,
			   "data":           data,
           	   mRender: function(data, type, full) {				            	
	       		var img = ('<div style="display: flex;">'+
	       				/*'<button style="border: none; background-color: transparent; outline: none;">'+
 	       				'<img src="css/images/attachment.png" class="testCaseExecutionResultImg1" title="Attachment" style="width: 18px;height: 18px;">&nbsp;['+0+']&nbsp;</img></button>'+*/
 	       			'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
 	       					'<i class="fa fa-comments testCaseExecutionResultImg2" title="Comments"></i></button>'+
	       		'<button style="border: none; background-color: transparent; outline: none;">'+
	       				'<img src="css/images/list_metro.png" title="Test Step" onclick="wpkTestCaseDetailsTSResult(event)" style="margin-left: 5px;"></button>'+
	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
	       				'<img src="css/images/list_metro.png" title="Export System Details" onclick="wpkTestCaseDetailsExportResult(event)"></button>'+
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
	    
	    /*wkpResults_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                    .search( this.value, true, false )
	                    .draw();
	            }
	        } );
	    } ); */ 
		 
		 $("#workPackageTestRun_dataTable_wrapper").find(".buttons-create").hide();
		 
		 $("#workPackageTestRun_dataTable_length").css('margin-top','8px');
		 $("#workPackageTestRun_dataTable_length").css('padding-left','35px');
		 
		 new $.fn.dataTable.FixedColumns( wkpResults_oTable, {
			    leftColumns: 2,
				rightColumns: 1,
			});
		 
		 $('#workPackageTestRun_dataTable').on( 'click', 'tbody td.editable', function (e) {
			 testCaseExecutionResult_editor.inline( this, {
	        		submitOnBlur: true
				}); 
			});
		 
		 $("#workPackageTestRun_dataTable tbody").on('click', 'td button .testCaseExecutionResultImg1', function () {
				var tr = $(this).closest('tr');
			    var row = wkpResults_oTable.DataTable().row(tr);
			    isViewAttachment = false;
				var jsonObj={"Title":"Attachments for Testcase Execution Result",			          
	    			"SubTitle": 'Testcase Execution Result : ['+row.data().testCaseExecutionResultId+'] '+row.data().testcaseName,
	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+prodId+'&entityTypeId=79&entityInstanceId='+row.data().testCaseExecutionResultId,
	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+prodId+'&entityTypeId=79&entityInstanceId='+row.data().testCaseExecutionResultId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
	    			"updateURL": 'update.attachment.for.entity.or.instance',
	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=79',
	    			"multipleUpload":true,
	    		};	 
				mode='edit';
     		Attachments.init(jsonObj);
		 });
		 			// ----- Comments  -----
		 
		 $("#workPackageTestRun_dataTable tbody").on('click', 'td button .testCaseExecutionResultImg2', function () {
				var tr = $(this).closest('tr');
			    var row = wkpResults_oTable.DataTable().row(tr);
			    var entityTypeIdComments = 79;
				var entityNameComments = "TestCaseExecutionResultComments";
				listComments(entityTypeIdComments, entityNameComments, row.data().testCaseExecutionResultId, row.data().testcaseName, "testCaseExecutionResultComments");			    
		 });
		 
		$('#workPackageTestRun_dataTable_wrapper .DTFC_RightWrapper, #workPackageTestRun_dataTable_wrapper .DTFC_LeftFootWrapper').addClass('hidden');
			
		 wkpResults_oTable.DataTable().columns().every( function () {
		        var that = this;
		        $('input', this.footer() ).on( 'keyup change', function () {
		            if ( that.search() !== this.value ) {
		                that
		                	.search( this.value, true, false )
		                    .draw();
		            }
		        } );
				} );
		 
	    
	} ); 
}


function addTestCaseExecutionResultComments(prodId,entityInstanceId,entityInstanceName,modifiedById,currentStatusId,currentStatusDisplayName,entityId,secondaryStatusId,visibleEventComment){	
	
	var entityTypeId = 79;//Activity type
	var actionTypeValue = 0;
	$("#addCommentsMainDiv").modal();
	$("#addCommentsDateTimePickerBox").show();
	isSave= true;
	if(!visibleEventComment) {
		$('#addComments').hide();//Display only histroy of task Effort
		$("#viewComments .slimScrollDiv").css("height", "450px");
		$("#viewComments .scroller").css("height", "450px");
	}else {
		$('#addComments').show();
		$("#viewComments .slimScrollDiv").css("height", "200px");
		$("#viewComments .scroller").css("height", "200px");
	}
	var jsonObj = {
			Title : "TesctCase Execution Result Workflow History: ["+entityInstanceId+"] "+entityInstanceName,	
			entityTypeName : 'TestcaseExecutionResult',		
			entityTypeId : entityTypeId,
			entityInstanceName : entityInstanceName,
			entityInstanceId : entityInstanceId,
			modifiedByUrl : 'common.user.list.by.resourcepool.id',		
			modifiedById : modifiedById,
			raisedDate : new Date(),
			comments : "",
			productId : prodId,
			primaryStatusUrl : 'workflow.status.master.option.list?productId='+prodId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&currentStatusId='+currentStatusId,
			secondaryStatusUrl : 'workflow.entity.secondary.status.master.option.list?productId='+prodId+'&entityTypeId='+entityTypeId+'&statusId='+currentStatusId,
			currentStatusId : currentStatusId,
			currentStatusName : currentStatusDisplayName,
			secondaryStatusId : secondaryStatusId,
			effortListUrl : 'workflow.event.tracker.list?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
			actionTypeValue : actionTypeValue,
			commentsName : "TestCaseExecutionResultComments",
			"componentUsageTitle":"activity",
			urlToSave : 'workflow.event.tracker.add?productId='+prodId+'&entityId='+entityId+'&entityTypeId='+entityTypeId+'&primaryStatusId=[primaryStatusId]&secondaryStatusId=[secondaryStatusId]&effort=[effort]&comments=[comments]&sourceStatusId='+currentStatusId+'&approveAllEntityInstanceIds=[approveAllEntityIds]&entityInstanceId='+entityInstanceId+'&attachmentIds=[attachmentIds]&actionDate=[actionDate]&actualSize=[actualSize]',				
	};
	AddComments.init(jsonObj);
}



function listComments(entityTypeId, entityName, instanceId, instanceName, componentUsageTitle){

	var url='comments.for.entity.or.instance.list?productId=0&entityTypeId='+entityTypeId+'&entityInstanceId='+instanceId+'&jtStartIndex=0&jtPageSize=10000';
	//var instanceId = row.data().productId;
	var jsonObj={"Title":"Response on "+entityName+ ": " +instanceName,
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,
			"componentUsageTitle":componentUsageTitle,			
			"entityTypeId":entityTypeId,
			"entityInstanceId":instanceId,
	};
	CommentsMetronicsUI.init(jsonObj);
}


function wpkTestCaseDetailsTSResult(event){
	var tr = event.target.closest('tr');
	var row = wkpResults_oTable.DataTable().row(tr);
	tcResultHandler(row, tr);
}

function wpkTestCaseDetailsExportResult(event){
	var tr = event.target.closest('tr');
	var row = wkpResults_oTable.DataTable().row(tr);
	tcDefectsResultHandler(row, tr);
}


var clearTimeoutDTWorkPackage='';
function reInitializeWPKDataTableTC(){
	
	/*setTimeout(function(){				
		wkpResults_oTable.DataTable().columns.adjust().draw();		
	},200);*/
	
	clearTimeoutDTWorkPackage = setTimeout(function(){				
		wkpResults_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTWorkPackage);
	},200);
}


function workpackage_Headerchange(){	
	$("#wpproduct").text(wpHeaderProductLable);
}

// ----- started -----

var totalTestPlanList=100;
var Prev = {start: 0,stop: 0}, 
TabPage, Paging = [], CONT;
var prevUnMappedRequest='';
var pageCountSelected=50;
var pageCountSelectedDT=1000;
var workpackageData;
function initWorkpackagePagination(currentPageNo, pageCountSelected){	
	var startIndex=0;
	$("#workPackageTileContent").empty();			
	openLoaderIcon();
	$('#workPackageTileContent').addClass("ptrNone");	
	
	var urlX='workpackage.executiondetails.productorbuildlevel.list?productBuildId='+productBuildId+'&productId='+prodId+'&testFactoryId='+document.getElementById("treeHdnCurrentTestFactoryId").value;
	url=urlX+"&jtStartIndex="+currentPageNo+"&jtPageSize="+pageCountSelected;
	
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		complete : function(data){
			if(data != undefined){			
				closeLoaderIcon();				
				$('#workPackageTileContent').removeClass("ptrNone");	
				allTestRunPlanFun(startIndex, pageCountSelected);				
			}
		},
		success : function(data) {
			workpackageData = data;
			totalTestPlanList = data.TotalRecordCount;
			displayRecordsTestRunPlan(data);				
		},
		error: function (data){
			closeLoaderIcon();
		}
	});	
}

function displayRecordsTestRunPlan(data){
	var result = data.Records;
	$("#workPackageTileContent").empty();
	$("#badgeTestRunPlan").show();
	$("#selectTestRunPlanRowCount").show();		
	
	if(result.length==0){
		$("#workPackageTileContent").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 400px;'>"+'No Records'+"</b></span>");
		$("#leftDragItemsTotalCount").text(totalTestPlanList);
		$("#badgeTestRunPlan").hide();
		$("#selectTestRunPlanRowCount").hide();
	}else{
		listWorkPacakgeTiles(result);		
		$("#leftDragItemsTotalCount").text(totalTestPlanList);
	}
 }

// -- For DataTable Pagination -----

function allTestRunPlanFunDT(index, pageCountSelected){
	var rowCount = pageCountSelected;			
	var totalCurrentCount=0;
	var currentPageNo=0;
	var page = Number($(paginationTestRunPlanDT).find('span.current').text());			
	currentPageNo=page;
	
	$("#workPackagePaginationDT").show();
	if(totalTestPlanList<pageCountSelectedDT){
		$("#workPackagePaginationDT").hide();
	}
		
	if(page==0){
		currentPageNo=1;
	}
	
	Paging[0] = $("#workPackagePaginationDT .toppagination").paging(totalTestPlanList, {								                    
	onSelect: function(page) {					
		currentPageNo=page;
		if(totalTestPlanList < (rowCount*currentPageNo)){
			totalCurrentCount = totalTestPlanList;
		}else{
			totalCurrentCount = (rowCount*page);
		}
		$("#badgeTestRunPlanDT").text('Showing '+((rowCount*page)-rowCount+1)+' to '+totalCurrentCount+' of '+totalTestPlanList+' entries');		
		openLoaderIcon();
		
		var urlX='workpackage.executiondetails.productorbuildlevel.list?productBuildId='+productBuildId+'&productId='+prodId+'&testFactoryId='+document.getElementById("treeHdnCurrentTestFactoryId").value;
		var url = urlX+"&jtStartIndex="+((rowCount*page)-rowCount)+"&jtPageSize="+rowCount;
		console.log("--"+((rowCount*page)-rowCount));
		
		assignWPDataTableValues(url, "parentTable", "", "");			
		console.log('2 incrementing');
		index++;
	},
			
	onFormat: function (type) {		
		switch (type) {			
			case 'block':				
				if (!this.active)
				return '<span class="fg-button ui-button ui-state-default ui-state-disabled disabled">' + this.value + '<\/span>';
				else if (this.value != this.page)
				return '<em><a class="fg-button ui-button ui-state-default customPagination" href="#' + this.value + '">' + this.value + '<\/a><\/em>';
				return '<span class="current fg-button ui-button ui-state-default customPagination">' + this.value + '<\/span>';
			
			case 'right':
			case 'left':				
				if (!this.active) {
				return '';
				}
				return '<a class="fg-button ui-button ui-state-default" href="#' + this.value + '">' + this.value + '<\/a>';
				
			case 'next':				
				if (this.active) {
				return '<a href="#' + this.value + '" class="next fg-button ui-button ui-state-default customPagination">&raquo;<\/a>';
				}
				return '<span class="fg-button ui-button ui-state-default ui-state-disabled disabled">&raquo;<\/span>';
			
			case 'prev':					
				if (this.active) {
				return '<a href="#' + this.value + '" class="prev fg-button ui-button ui-state-default customPagination">&laquo;<\/a>';
				}
				return '<span class="fg-button ui-button ui-state-default ui-state-disabled disabled">&laquo;<\/span>';
			
			case 'first':				
				if (this.active) {
				return '<a href="#' + this.value + '" class="first fg-button ui-button ui-state-default customPagination">|&lt;<\/a>';
				}
				return '<span class="fg-button ui-button ui-state-default ui-state-disabled disabled">|&lt;<\/span>';
			
			case 'last':				
				if (this.active) {
				return '<a href="#' + this.value + '" class="prev fg-button ui-button ui-state-default customPagination">&gt;|<\/a>';
				}
				return '<span class="fg-button ui-button ui-state-default ui-state-disabled disabled">&gt;|<\/span>';
			
			case 'fill':
				if (this.active) {
					return "...";
				}
		}
		return ""; // return nothing for missing branches
		},
		format: '[< ncnnn! >]',
		perpage: rowCount,
		lapping: 0,
		page: null // we await hashchange() event
	});
	
	Paging[0].setPage(1); // we dropped "page" support and need to run it by hand
	
};

$('#workPackage_dataTable').on( 'length.dt', function ( e, settings, len ) {
    adjustSideBarHeight();		
} );

$('#workPackage_dataTable').on( 'page.dt', function () {
	 adjustSideBarHeight();		
} );

function setDropDownTestRunPlanDT(value){
	var pageSize=0;
	var starting = 0;
	var ending = 0;
	var currentPageNo=0;
	var pageNo = Number($(paginationTestRunPlanDT).find('span.current').text());
	if ((value*pageNo) > totalTestPlanList){
		currentPageNo = 0;
		pageSize = (value*pageNo);
		$('#selectTestRunPlanRowCountDT').find('option[value="'+pageSize+'"]').attr("selected", true);
		
	}else{
		currentPageNo = ((value*pageNo)-value);		
		pageSize = (value*pageNo);
	}	
	allTestRunPlanFunDT(currentPageNo, pageSize);
}

// ----- ended -----

function allTestRunPlanFun(index, pageCountSelected){
	    var rowCount = pageCountSelected;			
		var totalCurrentCount=0;
		var currentPageNo=0;
		var page = Number($(paginationTestRunPlan).find('span.current').text());			
		currentPageNo=page;
		$("#paginationTestRunPlan").show();
		if(totalTestPlanList<rowCount){
			$("#paginationTestRunPlan").hide();
		}			
		if(page==0){
			currentPageNo=1;
		}
		
		Paging[0] = $(".a .toppagination").paging(totalTestPlanList, {								                    
		onSelect: function(page) {					
		    currentPageNo=page;
			if(totalTestPlanList < (rowCount*currentPageNo)){
				totalCurrentCount = totalTestPlanList;
			}else{
				totalCurrentCount = (rowCount*page);
			}
			//unMapListCountAsWhenModified = totalCurrentCount;
		    var count = (((rowCount*page)-rowCount+1)+'/'+(totalCurrentCount));
			$("#badgeTestRunPlan").text(count+" of "+totalTestPlanList);				
			if(index!=0)
			{					
				openLoaderIcon();
				$('#workPackageTileContent').addClass("ptrNone");
				
				//var urlX = 'workpackage.executiondetails.productorbuildlevel.list?productBuildId=-1&productId=-1&testFactoryId='+document.getElementById("treeHdnCurrentTestFactoryId").value;
				var urlX='workpackage.executiondetails.productorbuildlevel.list?productBuildId='+productBuildId+'&productId='+prodId+'&testFactoryId='+document.getElementById("treeHdnCurrentTestFactoryId").value;
				var url = urlX+"&jtStartIndex="+((rowCount*page)-rowCount)+"&jtPageSize="+rowCount;
				console.log("--"+((rowCount*page)-rowCount));
				prevUnMappedRequest = $.ajax({
					type: "POST",
			        contentType: "application/json; charset=utf-8",
					url : url,
					dataType : 'json',
		            beforeSend : function()    {           
		            	if(prevUnMappedRequest!=''){ 
							prevUnMappedRequest.abort();
				        }
		            },					
					complete: function(data){						
						closeLoaderIcon();
						$('#workPackageTileContent').removeClass("ptrNone");	
					},
					success: function(data) {	
						workpackageData = data;
						displayRecordsTestRunPlan(data);						
					}
				});
			}
			console.log('2 incrementing');
			index++;
		},
				
		onFormat: function (type) {		
			switch (type) {			
				case 'block':				
					if (!this.active)
					return '<span class="disabled">' + this.value + '<\/span>';
					else if (this.value != this.page)
					return '<em><a href="#' + this.value + '">' + this.value + '<\/a><\/em>';
					return '<span class="current">' + this.value + '<\/span>';
				
				case 'right':
				case 'left':				
					if (!this.active) {
					return '';
					}
					return '<a href="#' + this.value + '">' + this.value + '<\/a>';
					
				case 'next':				
					if (this.active) {
					return '<a href="#' + this.value + '" class="next">&raquo;<\/a>';
					}
					return '<span class="disabled">&raquo;<\/span>';
				
				case 'prev':					
					if (this.active) {
					return '<a href="#' + this.value + '" class="prev">&laquo;<\/a>';
					}
					return '<span class="disabled">&laquo;<\/span>';
				
				case 'first':				
					if (this.active) {
					return '<a href="#' + this.value + '" class="first">|&lt;<\/a>';
					}
					return '<span class="disabled">|&lt;<\/span>';
				
				case 'last':				
					if (this.active) {
					return '<a href="#' + this.value + '" class="prev">&gt;|<\/a>';
					}
					return '<span class="disabled">&gt;|<\/span>';
				
				case 'fill':
					if (this.active) {
						return "...";
					}
			}
			return ""; // return nothing for missing branches
		},
		format: '[< ncnnn! >]',
		perpage: rowCount,
		lapping: 0,
		page: null // we await hashchange() event
	});

	Paging[0].setPage(1); // we dropped "page" support and need to run it by hand
	
	adjustSideBarHeight();		
};

var clearTimeoutPaginationWKDT='';
function adjustSideBarHeight(){	
	clearTimeoutPaginationWKDT =setTimeout(function(){				
		adjustSideBarHeightDT();
	},1000);
}
	
function adjustSideBarHeightDT(){
	var pageHeight = $('.page-container').css('height');
	$('.page-quick-sidebar-wrapper').css('height', pageHeight);
}
	
	function setDropDownTestRunPlan(value){
		var pageSize=0;
		var starting = 0;
		var ending = 0;
		var currentPageNo=0;
		var pageNo = Number($(paginationTestRunPlan).find('span.current').text());
		if ((value*pageNo) > totalTestPlanList) {
			currentPageNo = 0;
			pageSize = 10;	
			$('#selectTestRunPlanRowCount').find('option[value="'+pageCountSelected+'"]').attr("selected", true);
		}else{
			currentPageNo = ((value*pageNo)-value);		
			pageSize = (value*pageNo);			
		}		
		initWorkpackagePagination(currentPageNo,pageSize);
	}
	
	// ----- ended -----
$("#searchWorkpackagesText").on("keyup", function() {
	var value = $(this).val().toLowerCase();
    $("#workPackageTileContent .tile").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);console.log('tes');
    });
});  

function listWorkPacakgeTiles(data){	
	$('#workPackageTileContent').empty();
	 
	 var availability = "bg-white"; 
	 if(data.length!=0){	            
         for (var key in data) {
         	var botName = data[key].productName;//data[key].botName;
         	var imgSrc="";
     		//imgsrc="css/images/noimage.jpg";
     		imgSrc="css/images/workpackage1.png";
         	var nameBot = data[key].workPackageName;
         	var tpName = data[key].testPlanName;
         	var tsName = data[key].testsuiteName;
         	var botStatus = 'Available';//data[key].botStatus;
         	if(botStatus == null) {
         		botStatus="";
         	}
         	if(nameBot.length > 25){         		
         		nameBot = (data[key].workPackageName).toString().substring(0,20)+'...';         		
         	}else{
         		nameBot = data[key].workPackageName;
         	}
         	if(tpName.length > 10){         		
         		tpName = (data[key].testPlanName).toString().substring(0,7)+'...';         		
         	}else{
         		tpName = data[key].testPlanName;
         	}
         	if(tsName.length > 10){         		
         		tsName = (data[key].testsuiteName).toString().substring(0,7)+'...';         		
         	}else{
         		tsName = data[key].testsuiteName;
         	}
         	var titleSchedule = "<"+data[key].workPackageName+">";
        	var subTitleSchedule = "Product "+$(headerTitle).text();
        	var workPackageId = data[key].workPackageId;
        	//prodId = data[key].productId;
        	//productId = data[key].productId;
        	//prodName = data[key].productName;
        	//productName = prodName;
        	//productBuildId = data[key].productBuildId;
        	//buildIdTemp = data[key].productBuildId;
        	//buildName = data[key].productBuildName;
        	//productVersionId = data[key].productVersionId;
        	//productVersionName = data[key].productVersionName;
        	var startDate = data[key].firstActualExecutionDate;
        	if(startDate != null || startDate != undefined){
        		startDate = '<span title="'+data[key].firstActualExecutionDate+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >'+ data[key].firstActualExecutionDate +'<br /> </span>';
        	}else{
        		startDate = '<span style="color: rgba(53, 65, 66, 0.78); font-size: 14px;word-wrap: break-word;">Date Not Available<br /></span>';
        	}     
        	var wpResult = "";
			if(data[key].result != null || data[key].result != undefined ){
				wpResult = data[key].result;
			}else{
				wpResult = "---";
			}
			var passPercentage = 0;			
			if(data[key].totalWPTestCase == 0){
				passPercentage=0;
			}else{
				passPercentage = (data[key].p2totalPass * 100)/(data[key].totalWPTestCase);
				passPercentage = passPercentage.toFixed(0);
			}
			
			var failedPercentage = 0;			
			if(data[key].totalWPTestCase == 0){
				failedPercentage=0;
			}else{
				failedPercentage = (data[key].p2totalFail * 100)/(data[key].totalWPTestCase);
				failedPercentage = failedPercentage.toFixed(0);
			}
			var notExecuted = data[key].totalWPTestCase - (data[key].p2totalPass +  data[key].p2totalFail);
			
			var notExecutedPercentage = 0;			
			if(data[key].totalWPTestCase == 0){
				notExecutedPercentage=0;
			}else{
				notExecutedPercentage = (notExecuted * 100)/(data[key].totalWPTestCase);
				notExecutedPercentage = notExecutedPercentage.toFixed(0);
			}
			
			var isResultBadge='';
			if(wpResult.includes("Passed")){
				isResultBadge += '<span title="'+wpResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : </span>';
				isResultBadge += '<span id="wppassed" class="badge badge-default" style="background: #076;margin-top: 10px;margin-bottom: 10px;display: inline;margin-right: 5px;">'+wpResult+' <br /> </span>';
			}else if(wpResult == "Failed"){
				isResultBadge += '<span title="'+wpResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : </span>';
				isResultBadge += '<span id="wp" class="badge badge-default" style="background: rgb(231, 80, 90);margin-top: 10px;margin-bottom: 10px;display: inline;">'+wpResult+' <br /> </span>';
			}else if(wpResult == "Restart"){
				isResultBadge += '<span title="'+wpResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : </span>';
				isResultBadge += '<span id="wp" class="badge badge-default" style="background: rgb(231, 80, 90);margin-top: 10px;margin-bottom: 10px;display: inline;">'+wpResult+' <br /> </span>';
			}else if(wpResult == "---"){
				isResultBadge += '<span title="'+wpResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : </span>';
				isResultBadge += '<span id="" class="badge badge-default" style="background: rgb(255, 214, 26);margin-top: 10px;margin-bottom: 10px;display: inline;">In Progress<br /> </span>';
			}
			var isReport;
			var abortWorkPackage="";
			/*var entityTypeIdComments = 10;
			var entityNameComments = "TestRunJob";
			var testRunJobComments = "testRunJobComments";*/
			isReport = '<span style="float: right;padding: 0px 0px;"><img width="13px" height="13px" class="exe" src="css/images/list_metro.png" style="color:black;cursor:pointer;margin-right:0px;margin-top:2px;" title="Test Jobs" onclick="workPackageTestRunPlanFromGrid(\''+data[key].workPackageId+'\',null)""></img></span><br/>'+	
    					'<span style="float: right;padding: 0px 2px;"><i class="fa fa-comments" style="color:black;cursor:pointer" title="Comments" onclick="workPackageCommentsFromGrid(\''+data[key].workPackageId+'\',\''+data[key].workPackageName+'\',null)"></i></span><br/>';
			if((passPercentage != 'NaN' && passPercentage != 0 )|| (failedPercentage != 'NaN' && failedPercentage != 0)){
				//isReport += '<span style="float: right;padding: 0px 2px;"><img width="13px" height="13px" class="exe" src="css/images/pdf_type1.png" style="color:black;cursor:pointer;margin-right:0px;margin-top:2px;"  title="WorkPackage Evidence PDF Report" onclick="exportWPResults(null,null,\''+data[key].workPackageId+'\')"></img></span><br/>'+
				isReport += '<span style="color:black;"><i class="fa fa-desktop" title="WorkPackage Evidence HTML Report"  style="cursor:pointer;font-size:15px;padding-top:5px;margin-left:3px;" onclick="exportWPhtmlEvidenceResults(null,null,'+data[key].workPackageId+');"/></span><br/>';
			}
			if(data[key].wpStatus.includes("---")){
				abortWorkPackage = '<span style="float: right;padding: 0px 2px;"><i class="fa fa-stop" style="color:black;cursor:pointer" title="Abort WorkPackage" onclick="abortWorkPackage('+workPackageId+')"></i></span><br/>';
			}
			var reExecuteWorkPackage = '<span style="float: right;padding: 0px 2px;"><i class="fa fa-repeat" style="color:black;cursor:pointer" title="Repeat Execution" onclick="reRunTestPlan(\''+data[key].testPlanId+'\',\''+data[key].testPlanExecutionMode+'\',\''+data[key].useIntelligentTestPlan+'\')"></i></span><br/>';
        	var brick = '<div class="'+"tile "+ availability + '" style="border:#ddd solid 1px;width:220px !important;height:288px !important;cursor:default;"><div class="tile-body" style="padding:3px 3px;">' +
    	    '<div class="row">'+
    	    	'<div style="position:absolute; float:left;border-right: solid 0px #ddd;"><img width="60px" height="60px" onclick="showWorkpackageSummary(\''+workPackageId+'\',\''+data[key].exeType+'\',\''+data[key].testPlanId+'\',\''+data[key].productId+'\');" title="'+workPackageId+'-'+data[key].workPackageName+'" src="'+ imgSrc +'" onerror="this.src=\'css/images/noimage.jpg\'"></img></div>'+
    	    	'<div style="float:right;">'+    	    	    	    	
    	    	/*'<span style="float: right;padding: 0px 0px;"><img width="13px" height="13px" class="exe" src="css/images/list_metro.png" style="color:grey;cursor:pointer;margin-right:0px;margin-top:2px;" title="Test Jobs" onclick="workPackageTestRunPlanFromGrid(\''+data[key].workPackageId+'\',null)"></img></span><br/>'+	
    	    	'<span style="float: right;padding: 0px 2px;"><i class="fa fa-comments" style="color:grey;cursor:pointer" title="Comments" onclick="workPackageCommentsFromGrid(\''+data[key].workPackageId+'\',\''+data[key].workPackageName+'\',null)"></i></span><br/>'+*/
    	    	isReport+
				'<span style="float: right;padding: 0px 2px;"><i class="fa fa-search-plus" style="color:black;cursor:pointer" title="Audit History" onclick="workPackageAuditHistoryFromGrid(\''+data[key].workPackageId+'\',null)"></i></span><br/>'+
				abortWorkPackage+ reExecuteWorkPackage +
				/*'<span style="float: right;padding: 0px 2px;"><i class="fa fa-search-plus" style="color:black;cursor:pointer" title="WorkPackage Status" onclick="workPackageProgressComponent(\''+data[key].workPackageId+'\',null)"></i></span><br/>'+*/
    	    '</div>'+
    	    '<div class="row" style="text-align:left;margin-top:70px;overflow:hidden;">'+
    	    '<span title="'+workPackageId+'-'+data[key].workPackageName+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >'+ nameBot +'<br /> </span>'+
    	     startDate+  
    	    '<span class="botTileWrap" title="'+data[key].testPlanName+'" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Testplan Name : '+tpName+'<br /></span>'+
    	    '<span class="botTileWrap" title="'+data[key].testsuiteName+'" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Testsuite Name : '+tsName+'<br /></span>'+
    	    '<span class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Mode : '+data[key].executionMode+'<br /></span>'+
    	    '<span title="'+data[key].wpStatus+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Status : '+ data[key].wpStatus +'<br /> </span>'+
    	    isResultBadge+
    	    '<span title="'+data[key].totalWPTestCase+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;display:block;" >Total Testcases : '+ data[key].totalWPTestCase +'<br /> </span>'+
			'<span id="passedTestCases" class="badge badge-default" style="background: #076;margin-top: 10px;margin-bottom: 10px;display: inline;margin-right: 5px;">'+data[key].p2totalPass+' ['+passPercentage+'%]</span>'+
			'<span id="failedTestCases" class="badge badge-default" style="background: rgb(231, 80, 90);margin-top: 10px;margin-bottom: 10px;display: inline;">'+data[key].p2totalFail+' ['+failedPercentage+'%]</span>'+
			'<span id="notExecutedTestCases" class="badge badge-default" style="background: rgb(112,128,144);margin-top: 10px;margin-bottom: 10px;display: inline;">'+notExecuted+' ['+notExecutedPercentage+'%]</span>'+
    	    '</div>'+    	  
    	    '</div>'+
    	    '</div>';    	    
	 		$('#workPackageTileContent').append($(brick));
         }
	 }else{
		 var brickcontainer = '<div class="text-center">No data available!</div>';
		 $('#workPackageTileContent').append($(brickcontainer));
	 }
	 
	 if(!($('#workPackageTileContent').hasClass('tiles'))){
			$('#workPackageTileContent').addClass('tiles').css("padding","2px 10px 10px 15px");
	 } 
}

function testRunJobsTileView(data){	
	$('#wpTestRunJobDTContainerTileView').empty();	 
	 var availability = "bg-white"; 
	 var jobId;
	 if(data.Records.length!=0){	            
         //for (var key in data.Records) {
        for(var i=0;i<data.Records.length;i++){
         	var imgSrc="";
     		//imgsrc="css/images/noimage.jpg";
     		imgSrc="css/images/workpackage1.png";
         	var nameBot = data.Records[i].workPackageName;
         	var jobStatus = 'Available';//data[key].botStatus;
         	if(jobStatus == null) {
         		jobStatus="";
         	}
         	var titleSchedule;
         	//var workPackageId;
         	
         /*	if(nameBot!= undefined && nameBot.length > 25){         		
         		nameBot = (data.Records[i].workPackageName).toString().substring(0,20)+'...';
         		titleSchedule = nameBot;
         	}else if(nameBot!= undefined){
         		nameBot = data.Records[i].workPackageName;
         		titleSchedule = "<"+data.Records[i].workPackageName+">";
         		
         	}  */  
         	
        	var subTitleSchedule = "Product "+$(headerTitle).text();      	
        	//workPackageId = data.Records[i].workPackageId;
        	var startDate = data.Records[i].firstActualExecutionDate;
        	if(startDate != null || startDate != undefined){
        		startDate = '<span title="'+data.Records[i].firstActualExecutionDate+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >'+ data.Records[i].firstActualExecutionDate +'<br /> </span>';
        	}else{
        		startDate = '';
        	}        	
			
			var passPercentage = 0;			
			if(data.Records[i].totalTestCaseCount == 0){
				passPercentage=0;
			}else{
				passPercentage = (data.Records[i].passedCount * 100)/(data.Records[i].totalTestCaseCount);
				passPercentage = passPercentage.toFixed(0);
			}
			
			var failedPercentage = 0;			
			if(data.Records[i].totalTestCaseCount == 0){
				failedPercentage=0;
			}else{
				failedPercentage = (data.Records[i].failedCount * 100)/(data.Records[i].totalTestCaseCount);
				failedPercentage = failedPercentage.toFixed(0);
			}
			var notExecuted = data.Records[i].totalTestCaseCount - (data.Records[i].passedCount +  data.Records[i].failedCount);
			var notExecutedPercentage = 0;			
			if(data.Records[i].totalTestCaseCount == 0){
				notExecutedPercentage=0;
			}else{
				notExecutedPercentage = (notExecuted * 100)/(data.Records[i].totalTestCaseCount);
				notExecutedPercentage = notExecutedPercentage.toFixed(0);
			}
			
			var jobResult = "";
			if(data.Records[i].result != null || data.Records[i].result != undefined ){
				jobResult = data.Records[i].result;
			}else{
				if(data.Records[i].testRunStatusName == "Executing"){
					if(data.Records[i].failedCount > 0)
						jobResult = "Failed";
					else if(data.Records[i].passedCount > 0)
						jobResult = "Passed [Intermediate]";
					else
						jobResult = "---";
				}else
					jobResult = "---";
			}
			
			var envCmbtion = data.Records[i].environmentCombination;
			if(envCmbtion!= undefined && envCmbtion.length > 25){         		
         		envCmbtion = (data.Records[i].environmentCombination).toString().substring(0,22)+'...';         		
         	}
			

			var isReport;
			var entityTypeIdComments = 10;
			var entityNameComments = "TestRunJob";
			var testRunJobComments = "testRunJobComments";
			isReport = '<span style="float: right;padding: 0px 0px;"><img width="13px" height="13px" class="exe" src="css/images/list_metro.png" style="color:black;cursor:pointer;margin-right:0px;margin-top:2px;" title="Testcases" onclick="jobTileTestRunCaseHandler(\''+data.Records[i].testRunJobId+'\',null)"></img></span><br/>'+	
    					'<span style="float: right;padding: 0px 2px;"><i class="fa fa-comments" style="color:black;cursor:pointer" title="Comments" onclick="listComments(\''+entityTypeIdComments+'\',\''+entityNameComments+'\',\''+data.Records[i].testRunJobId+'\',\''+data.Records[i].testRunStatusName+'\',\''+testRunJobComments+'\')"></i></span><br/>';
			if((passPercentage != 'NaN' && passPercentage != 0 )|| (failedPercentage != 'NaN' && failedPercentage != 0)){
				//isReport += '<span style="float: right;padding: 0px 2px;"><img width="13px" height="13px" class="exe" src="css/images/pdf_type1.png" style="color:black;cursor:pointer;margin-right:0px;margin-top:2px;"  title="Job Evidence PDF Report" onclick="exportWPResultsTestRunJob(\''+data.Records[i].testRunJobId+'\')"></img></span><br/>'+
				isReport += '<span style="color:black;"><i class="fa fa-desktop" title="Job Evidence HTML Report"  style="cursor:pointer;font-size:15px;padding-top:5px;margin-left:3px;" onclick="exportWPResultshtmlEvidenceTestRunJob('+data.Records[i].testRunJobId+');"/></span><br/>';
			}
			
			var liveLog = '<span style="float: right;padding: 0px 2px;"><i class="fa fa-file-text" style="color:black;cursor:pointer" title="Live log" onclick="liveLog('+data.Records[i].testRunJobId+','+lastLine+')"></i></span><br/>';
			var abortjob = "";
			if(data.Records[i].testRunStatus != 5 && data.Records[i].testRunStatus != 6 && data.Records[i].testRunStatus != 7 )
				abortjob = '<span style="float: right;padding: 0px 2px;"><i class="fa fa-stop" style="color:black;cursor:pointer" title="Abort Job" onclick="abortjob('+data.Records[i].testRunJobId+')"></i></span><br/>';
			testjobid = data.Records[i].testRunJobId;
			
			var isResultBadge='';
			if(jobResult.includes("Passed")){
				isResultBadge += '<span title="'+jobResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : </span>';
				isResultBadge += '<span id="" class="badge badge-default" style="background: #076;margin-top: 10px;margin-bottom: 10px;display: inline;margin-right: 5px;">'+jobResult+' <br /> </span>';
			}else if(jobResult == "Failed"){
				isResultBadge += '<span title="'+jobResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : </span>';
				isResultBadge += '<span id="" class="badge badge-default" style="background: rgb(231, 80, 90);margin-top: 10px;margin-bottom: 10px;display: inline;">'+jobResult+' <br /> </span>';
			}else if(jobResult == "Restarted"){
				isResultBadge += '<span title="'+jobResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : </span>';
				isResultBadge += '<span id="" class="badge badge-default" style="background: rgb(231, 80, 90);margin-top: 10px;margin-bottom: 10px;display: inline;">'+jobResult+' <br /> </span>';
			}else if(jobResult == "---"){
				isResultBadge += '<span title="'+jobResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : </span>';
				isResultBadge += '<span id="" class="badge badge-default" style="background: rgb(255, 214, 26);margin-top: 10px;margin-bottom: 10px;display: inline;">In Progress <br /> </span>';
			}
			
        	var brick = '<div class="'+"tile "+ availability + '" style="border:#ddd solid 1px;width:240px !important;height:210px !important;cursor:default;"><div class="tile-body" style="padding:3px 3px;">' +
    	    '<div class="row">'+
    	    	'<div style="position:absolute; float:left;border-right: solid 0px #ddd;"><img width="60px" height="60px" title="'+data.Records[i].workPackageName+'" src="'+ imgSrc +'" onerror="this.src=\'css/images/noimage.jpg\'"></img></div>'+
    	    	'<div style="float:right;">'+ isReport + liveLog + abortjob +    		
    	    '</div>'+
    	    '<div class="row" style="text-align:left;margin-top:75px;overflow:hidden;">'+
    	    '<span title="Job Id" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >'+ "Job Id : "+data.Records[i].testRunJobId+'<br /> </span>'+
			'<span title="'+ data.Records[i].environmentCombination+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-break:break-all;" >'+ envCmbtion +'<br /> </span>'+
    	     startDate+ 
    	    '<span title="'+data.Records[i].testRunStatusName+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Status : '+ data.Records[i].testRunStatusName +'<br /> </span>'+
    	    isResultBadge +
    	    '<span title="'+jobResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;display:block;" >Total Testcases : '+ data.Records[i].totalTestCaseCount +'<br /> </span>'+
			'<span id="passedTestCases" class="badge badge-default" style="background: #076;margin-top: 10px;margin-bottom: 10px;display: inline;margin-right: 5px;">'+data.Records[i].passedCount+' ['+passPercentage+'%]</span>'+
			'<span id="failedTestCases" class="badge badge-default" style="background: rgb(231, 80, 90);margin-top: 10px;margin-bottom: 10px;display: inline;">'+data.Records[i].failedCount+' ['+failedPercentage+'%]</span>'+
			'<span id="notExecutedTestCases" class="badge badge-default" style="background: rgb(112,128,144);margin-top: 10px;margin-bottom: 10px;display: inline;">'+notExecuted+' ['+notExecutedPercentage+'%]</span>'+
    	    '</div>'+    	  
    	    '</div>'+
    	    '</div>';    	    
	 		$('#wpTestRunJobDTContainerTileView').append($(brick));
         }
	 }else{
		 var brickcontainer = '<div class="text-center">No data available!</div>';
		 $('#wpTestRunJobDTContainerTileView').append($(brickcontainer));
	 }
	 
	 if(!($('#wpTestRunJobDTContainerTileView').hasClass('tiles'))){
			$('#wpTestRunJobDTContainerTileView').addClass('tiles').css("margin-top","10px");
	 }	 
}
function jobTileTestRunCaseHandler(row, tr){
	var url = 'testcasesexecution.of.testrunjob.Id?testRunJobId='+row+'&jtStartIndex=0&jtPageSize=1000';
	assignWPDataTableValues(url, "childTable3", row, "null");
	$("#wpTestRunCaseContainer").modal();
	$("#wpTestRunCaseContainer h4").text(row+' - Testcases');
}


function showWorkpackageSummaryFromTable(event){
	var id = event.target.id;
	var wpkId = (id).split('~')[0];
	var exeType = (id).split('~')[1];
	var tpId = (id).split('~')[2];
	var pdtId = (id).split('~')[3];
	showWorkpackageSummary(wpkId, exeType, tpId, pdtId);
}

function showWorkpackageSummary(wpkId, exeType, tpId, pdtId){
	nodeType = "WorkPackage";
	$("a[href^=#TeamUtilization]").parent("li").hide();
	if(exeType == "Automated"){
		$("a[href^=#Plan]").parent("li").hide();
		$("a[href^=#Allocate]").parent("li").hide();
	}else {
		$("a[href^=#Plan]").parent("li").show();
		$("a[href^=#Allocate]").parent("li").show();
	}
	$(".wrkPckgTabCntnt").show();
	workPackageId = wpkId;
	document.getElementById("hdnTestRunPlanId").value=tpId;
	document.getElementById("treeHdnCurrentWorkPackageId").value = wpkId;
	document.getElementById("treeHdnCurrentProductId").value=pdtId;
	prodId = pdtId;
	var selectedTab= $("#tablistWP>li.active").index();				    
    utilizationWeekRange = '';
	fetchWPProductType(-1, prodId, wpkId);		
	tabSelection(selectedTab,workPackageId);
	getWeeksName();	
	$(document).off('focusin.modal');
	$('#div_ShowWorkpackageSummary').modal();
}