var trpData;
function testCasebyTestRunPlanId(){
	testRunPlanId = document.getElementById("hdnTestRunPlanId").value;//testrunPlanIdDevice;
	var url='test.suite.case.testrunplan.list?testSuiteId=-1&testRunPlanId='+testRunPlanId+'&jtStartIndex=0&jtPageSize=10000';
	
	$.ajax({
		type: "POST",
		url:url,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {					
			if(data == null || data.Result=="ERROR"){
				data = [];						
			}else{
				data = data.Records;
			}
			trpData = data; 
			executionTitleText();
		},
		error : function(data) {
		},
		complete: function(data){	
		}
	});
}

function testCaseConfigurationHeader(){		
	var tr = '<tr>'+			
		'<th>Selected</th>'+
		'<th>Test Suite Name</th>'+
		'<th>ID</th>'+
		'<th>Test Case Name</th>'+
		'<th>Test Case Description</th>'+
		'<th>Code</th>'+
		'<th>Recommended</th>'+
		'<th>Category</th>'+
		'<th>Probability</th>'+		
	'</tr>';
	return tr;
}

var testCaseConfiguration_oTable='';
var clearTimeoutDTTestCaseConfiguration='';
//var testRunPlanConfiguration_editor='';
function reInitializeDTTestCaseConfiguration(){
	clearTimeoutDTTestCaseConfiguration = setTimeout(function(){				
		testCaseConfiguration_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTTestCaseConfiguration);
	},1000);
}

function testCaseExecutionResults_Container(data){
	try{
		if ($("#jTableContainerTestCaseForTestRunPlan").children().length>0) {
			$("#jTableContainerTestCaseForTestRunPlan").children().remove();
		}
	} 
	catch(e) {}
	  var emptytr = emptyTableRowAppending(9);  // total coulmn count				  
	  var childDivString = '<table id="testCaseConfiguration_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#jTableContainerTestCaseForTestRunPlan").append(childDivString); 						  
	  
	  $("#testCaseConfiguration_dataTable thead").html('');
	  $("#testCaseConfiguration_dataTable thead").append(testCaseConfigurationHeader());
	  
	  $("#testCaseConfiguration_dataTable tfoot tr").html('');     			  
	  $("#testCaseConfiguration_dataTable tfoot tr").append(emptytr);
			
	testCaseConfiguration_oTable = $("#testCaseConfiguration_dataTable").dataTable( {
		 	dom: "Bfrtilp",
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY": "100%",
	       "bSort": false,
	       "bScrollCollapse": true,	      
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex = [0]; // search column TextBox Invisible Column position
     		  $("#testCaseConfiguration_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
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
			   reInitializeDTTestCaseConfiguration();			   
		   },  
		   select: true,
		   buttons: [	             	
			         //{ extend: "create", editor: testRunPlanConfiguration_editor },
					 {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "TestRunplan Configuration",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "TestRunplan Configuration",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "TestRunplan Configuration",
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
		   { mData: "isSelected", className: 'editable', sWidth: '5%',			
				mRender: function (data, type, full) {
					if ( type === 'display' ) {
						if(data ==1){
							return '<input type="checkbox" class="isSelected-active" checked>';
						}else{
							return '<input type="checkbox" class="isSelected-active">';
						}
					}
					return data;
				},
				className: "dt-body-center"	            
           },
           { mData: "testSuiteName",className: 'disableEditInline', sWidth: '20%' },
           { mData: "testCaseId",className: 'disableEditInline', sWidth: '5%' },		
           { mData: "testCaseName",className: 'disableEditInline', sWidth: '25%' },
           { mData: "testCaseDescription",className: 'disableEditInline', sWidth: '25%' },           
		   { mData: "testCaseCode",className: 'disableEditInline', sWidth: '5%' },           
		   { mData: "iseRecommended",className: 'disableEditInline', sWidth: '5%' },           		  
		   { mData: "recommendedCategory",className: 'disableEditInline', sWidth: '5%' }, 
		   { mData: "probability",className: 'disableEditInline', sWidth: '5%' }, 		   
       ], 
		rowCallback: function ( row, data ) {
	           //$('input.isSelected-active', row).prop( 'checked', data.isSelected == 1 );
	    },	   
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		  
		 $("#testCaseConfiguration_dataTable_length").css('margin-top','8px');
		 $("#testCaseConfiguration_dataTable_length").css('padding-left','35px');
		

		 $('#testCaseConfiguration_dataTable tbody').on( 'change', 'input.isSelected-active', function () {
			 var tr = $(this).closest('tr');
			 var row = testCaseConfiguration_oTable.DataTable().row(tr);
			 var flagValue=0;
			
			 if(!$(this).prop( 'checked' )){
				 flagValue=0;
			 }else{
				 flagValue=1;
			 }	
			 saveTestSuiteTestCaseMap(flagValue, row);			 
		 });
		 	 
		testCaseConfiguration_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
		} );
		setTimeout(function(){executionTitleText();},2000);
	});
}

function saveTestSuiteTestCaseMap(flag,row){
	var testCaseIdValue = row.data().testCaseId;
	var testSuiteListIdValue = row.data().testSuiteId;	
	var type;

	if(flag==1){
		 var url="administration.testsuite.testcase.maptestrunplan?testCaseId="+testCaseIdValue+"&testRunPlanId="+testRunPlanId+"&testSuiteId="+testSuiteListIdValue+"&type=Add";
		 type="Add";
		 $.ajax({
 		    type: "POST",
 		    url: url,
 		    success: function(data) {	 		    	
 		    	if(data.Result=='ERROR'){
 		    		callAlert(data.Message);		 		    		
	 		    	return false;
 		    	}else{
 		    		callAlert(data.Message);
 		    		listTestSuiteSelectedProductVersionPlanDataTable(row.data().productId, productVersionId, timestamp,type,testRunPlanId);
	 		    	return true;
 		    	}
 		    },    
 		    dataType: "json",		 		    
	 	});
	}else{
		var url="administration.testsuite.testcase.maptestrunplan?testCaseId="+testCaseIdValue+"&testRunPlanId="+testRunPlanId+"&testSuiteId="+testSuiteListIdValue+"&type=Remove";
		type="Remove";
		 $.ajax({
 		    type: "POST",
 		    url: url,
 		    success: function(data) {	 		    	
 		    	if(data.Result=='ERROR'){
 		    		callAlert(data.Message);		 		    		
	 		    	return false;
 		    	}else{
 		    		callAlert(data.Message);
	 		    	return true;
 		    	}
 		    },    
 		    dataType: "json",	 		    
	 	}); 
	}
}

function executionTitleText(){
	if(trpData != null && trpData.length>0){
		if(trpData[trpData.length-1].recommendedTestCaseCount == null)trpData[trpData.length-1].recommendedTestCaseCount = '0';
		if(trpData[trpData.length-1].totalTestCaseCount == null)trpData[trpData.length-1].totalTestCaseCount = '0';
		$('#executionTitle').html('<Bold>'+ trpData[trpData.length-1].recommendedTestCaseCount +"</Bold> testcases recommended out of <Bold>"+ trpData[trpData.length-1].totalTestCaseCount+"</Bold> for execution<br/>");
	}else{
		$('#executionTitle').html('<Bold> 0 </Bold> testcases recommended out of <Bold> 0 </Bold> for execution<br/>');
	}
}
