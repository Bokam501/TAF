var TestCaseDetails = function() {
  
   var initialise = function(jsonObj){
	   listTestCaseDetails(jsonObj);
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

var isLoad = false;			
var exceptedOutput='';

function listTestCaseDetails(jsonObj){	
	$("#testCaseDetailsModal").find("h4:first").text(jsonObj.Title);
	$("#testCaseDetailsModal").find("h4:first").text("TestCase Execution Summary :- "+ jsonObj.testCaseID + "- "+jsonObj.testCaseName );
	getTestCaseDetailsForResult(jsonObj);			
	$("#testCaseDetailsModal").modal();	
}

function getTestCaseDetailsForResult(jsonObj){
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : jsonObj.testCaseDetailsUrl,
		dataType : 'json',
		success : function(data) {
			var result=data.Records;
			var executionName="";
			var executionTime="";
			var executionStatus="";
			var isapproved = "";
			$("#testCaseDetailsModal #currCaseName, #currCaseId").text("");
			$("#testCaseDetailsModal #preconditions").empty();
			$("#testCaseDetailsModal #expectedOutput").empty();
			$("#testCaseDetailsModal #testcaseCode").empty();
			$("#testCaseDetailsModal #description").empty();
			$("#testCaseDetailsModal #plannedDate").empty();
			$("#testCaseDetailsModal #actualDate").empty();
			$("#testCaseDetailsModal #tcerIdhidden").empty();
			$("#testCaseDetailsModal #execTimerVal").empty();
			$("#testCaseDetailsModal #executionPriority").empty();
			
			//////////////////////// - modified started -
			
			$("#testCaseDetailsModal #productName").text('--');
			$("#testCaseDetailsModal #productVersionName").text('--');
			$("#testCaseDetailsModal #productBuildName").text('--');
			
			$("#testCaseDetailsModal #testcaseId").text('--');
			$("#testCaseDetailsModal #testcaseCode").text('--');
			$("#testCaseDetailsModal #testcaseName").text('--');
			$("#testCaseDetailsModal #testcasetype").text('--');
			$("#testCaseDetailsModal #testcasePriority").text('--');
			$("#testCaseDetailsModal #workPackageName").text('--');
			$("#testCaseDetailsModal #wpJob").text('');
			
			$("#testCaseDetailsModal #environment").text('--');
			$("#testCaseDetailsModal #plannedShift").text('--');
			$("#testCaseDetailsModal #actualDate").text('--');
			
			$("#testCaseDetailsModal #environment").text('--');
			$("#testCaseDetailsModal #environmentName").text('--');
			$("#testCaseDetailsModal #deviceId").text('--');
			$("#testCaseDetailsModal #devPlatform").text('--');
			$("#testCaseDetailsModal #hostName").text('--');
			$("#testCaseDetailsModal #hostIp").text('--');
			
			$("#testCaseDetailsModal #executionStatus").text('--');
			$("#testCaseDetailsModal #resTable #exeResult").text('--');	
			$("#testCaseDetailsModal #obsOutput").text('--');	
			$("#testCaseDetailsModal #exeStartTime").text('--');
			$("#testCaseDetailsModal #exeEndTime").text('--');
			$("#testCaseDetailsModal #expOutput").text('--');	
			$("#testCaseDetailsModal #failRemarks").text('--');			
			$("#testCaseDetailsModal #plannedDate").text('--');
			
			//////////////////////// - modified ended -
			
			$.each(result, function(i,item){				
				if(item.testcaseId != null){
					$("#testCaseDetailsModal #currCaseId").text(item.testcaseId);		
				}
				if(item.testcaseName != null){
					$("#testCaseDetailsModal #currCaseName").text(item.testcaseName);	
				}
				if(item.expectedOutput != null){
					$("#testCaseDetailsModal #expectedOutput").append("<div style='font-size:small;'  >"+item.expectedOutput+"</div>");	
				}
				if(item.testcaseCode != null){
					$("#testCaseDetailsModal #testcaseCode").append("<div style='font-size:small;'  >"+item.testcaseCode+"</div>");
				}
				if(item.testcaseDescription != null){
					$("#testCaseDetailsModal #description").append("<div style='font-size:small;'  >"+item.testcaseDescription+"</div>");
				}
				if(item.plannedExecutionDate != null){
					$("#testCaseDetailsModal #plannedDate").append("<div style='font-size:small;' >"+item.plannedExecutionDate+"</div>");	
				}
				if(item.actualExecutionDate != null){
					$("#testCaseDetailsModal #actualDate").append("<div  style='font-size:small;' >"+item.actualExecutionDate+"</div>");
				}
				////////////////////////// - modified started -
				if(item.productName != null){
					$("#testCaseDetailsModal #productName").text(item.productName);
				}
				if(item.productVersionName != null){
					$("#testCaseDetailsModal #productVersionName").text(item.productVersionName);
				}
				if(item.productBuildName != null){
					$("#testCaseDetailsModal #productBuildName").text(item.productBuildName);	
				}
				if(item.testcaseId != null){
					$("#testCaseDetailsModal #testcaseId").text(item.testcaseId);
				}
				var tcCode = "";
				if(item.testcaseCode != null){
					tcCode = item.testcaseCode;
				}				
				$("#testCaseDetailsModal #testcaseCode").text(tcCode);
				
				if(item.testcaseName != null){
					$("#testCaseDetailsModal #testcaseName").text(item.testcaseName);
				}
				if(item.testcaseType != null){
					$("#testCaseDetailsModal #testcasetype").text(item.exeType);
				}
				if(item.testcasePriority != null){
					$("#testCaseDetailsModal #testcasePriority").text(item.testcasePriority);
				}				
				if(item.featureName != null){
					$("#testCaseDetailsModal #testCaseFeature").text(item.featureName);
				}
				if(item.workPackageName != null){
					$("#testCaseDetailsModal #workPackageName").text(item.workPackageName);
				}
				if(item.testRunJobId != null){
					$("#testCaseDetailsModal #wpJob").text(item.testRunJobId);	
				}
				if(item.runConfigurationName != null){
					$("#testCaseDetailsModal #environment").text(item.runConfigurationName);	
				}
				if(item.actualExecutionDate != null){
					$("#testCaseDetailsModal #actualDate").text(item.actualExecutionDate);	
				}
				if(item.environmentCombinationName != null){
					$("#testCaseDetailsModal #environmentName").text(item.environmentCombinationName);	
				}
				if(item.deviceName != null){
					$("#testCaseDetailsModal #deviceId").text(item.deviceName);	
				}
				if(item.devplatformTypeName != null){
					$("#testCaseDetailsModal #devPlatform").text(item.devplatformTypeName);	
				}
				if(item.hostName != null){
					$("#testCaseDetailsModal #hostName").text(item.hostName);	
				}
				if(item.hostIPAddress != null){
					$("#testCaseDetailsModal #hostIp").text(item.hostIPAddress);	
				}
				
				////////////////////////  - modified ended -
				if(jsonObj.executionId != null){
					$("#testCaseDetailsModal #exeId").text(jsonObj.executionId);	
				}
				if(item.result != null){					
					$("#testCaseDetailsModal #exeResult").text(item.result);	
				}
				if(item.expectedOutput != null){
					$("#testCaseDetailsModal #expOutput").text(item.expectedOutput);	
				}
				if(item.observedOutput != null){
					$("#testCaseDetailsModal #obsOutput").text(item.observedOutput);		
				}
				if(item.startTime != null){
					$("#testCaseDetailsModal #exeStartTime").text(item.startTime);				
				}
				if(item.endTime != null){
					$("#testCaseDetailsModal #exeEndTime").text(item.endTime);					
				}
				var evidenceHTML = '';
				if(item.evidenceLabel != null){
					var temp=	item.evidenceLabel.split(",");
					for(var i=0; i < temp.length ;i++){
						var evidence = temp[i];
						var evedenceArr = evidence.split("@");
						var url = evedenceArr[0].split('\\').join('\\\\');
						evidenceHTML = evidenceHTML +"<a href='#' onclick='downloadEvidenceinTestCaseDetails(\""+url+"\")'>"+evedenceArr[1]+"</a><br></br>";
					}					
					$("#testCaseDetailsModal #evidence").append(evidenceHTML);					
				}
				if(item.jobFailureMessage != null){
					$("#testCaseDetailsModal #failRemarks").text(item.jobFailureMessage);		
				}
				
				if(item.executionStatus==3)
					executionName="NotStarted";
				else if(item.executionStatus==1)
					executionName="Assigned";
				else if(item.executionStatus==2)
					executionName="Completed";
				
				$("#testCaseDetailsModal #executionStatus").text(executionName);				
				
				var inputId = item.id+item.testcaseId;
				var dataid = item.executionPriorityId;
				if(dataid == 5){
					$("#testCaseDetailsModal #executionPriority").append('<input name="star'+inputId+'" disabled type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" checked="checked"/> <input name="star'+inputId+'" type="radio" class="hover-star"   value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'"   type="radio" class="hover-star"  value="5~'+inputId+'" title="P0"/> ');
				}else if(dataid == 4){
					$("#testCaseDetailsModal #executionPriority").append('<input name="star'+inputId+'"  disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" /> <input name="star'+inputId+'" type="radio" class="hover-star"   value="2~'+inputId+'" title="P3" checked="checked"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="5~'+inputId+'" title="P0"/>  ');
				}else if(dataid == 3){
					$("#testCaseDetailsModal #executionPriority").append('<input name="star'+inputId+'"  disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" /> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2" checked="checked"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="5~'+inputId+'" title="P0"/>  ');
				}else if(dataid == 2){
					$("#testCaseDetailsModal #executionPriority").append('<input name="star'+inputId+'" disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" /> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1" checked="checked"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="5~'+inputId+'" title="P0"/> ');
				}else if(dataid == 1){
					$("#testCaseDetailsModal #executionPriority").append('<input name="star'+inputId+'" disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4" /> <input name="star'+inputId+'" type="radio" class="hover-star"  value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'" type="radio"  class="hover-star"  value="5~'+inputId+'" title="P0" checked="checked"/> ');
				}else {
					$("#testCaseDetailsModal #executionPriority").append('<input name="star'+inputId+'" disabled  type="radio" class="hover-star"  value="1~'+inputId+'" title="P4"/> <input name="star'+inputId+'" type="radio" class="hover-star"   value="2~'+inputId+'" title="P3"/> <input name="star'+inputId+'"  type="radio" class="hover-star" value="3~'+inputId+'" title="P2"/> <input name="star'+inputId+'"  type="radio" class="hover-star"  value="4~'+inputId+'" title="P1"/> <input name="star'+inputId+'" type="radio"  class="hover-star"  value="5~'+inputId+'" title="P0"/>  ');
				}			
				
				if(document.getElementById('tcerIdhidden')!=null)
					document.getElementById('tcerIdhidden').value=item.testCaseExecutionResultId;				
				
				loadTestStepViewTSViewDataTableInit(item.testcaseId,item.testCaseExecutionResultId);
				loadTCExeIdExportDataViewDataTableInit(item.testCaseExecutionResultId);
				loadDefectInfoDataTable(item.testCaseExecutionResultId);
				loadTestCaseFeaturesDataTableInit(item.testcaseId);
				loadTestResults(data);
				loadEvidenceDetails();				
			});
			
			$('.hover-star').rating({ 
			 }); 					
		
			if(document.getElementById("paginationButton")!=null)
			document.getElementById("paginationButton").style.display = "block";	
			$("#testCaseDetailsModal").modal();			
		}
	});	
}

//BEGIN: ConvertDataTable - TestStepViewTSView
function loadTestStepViewTSViewDataTableInit(testcaseId,tcerId){
	url= 'teststep.plan.list?testCaseId='+testcaseId+"&tcerId="+tcerId+'&jtStartIndex=0&jtPageSize=10';
	jsonObj={"Title":"Test Steps",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"Test Steps",
	};
	testStepViewTSViewDataTableContainer.init(jsonObj);
}

var testStepViewTSViewDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		assignTestStepViewTSViewDataTableValues(jsonObj, "TestStep");
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignTestStepViewTSViewDataTableValues(jsonObj, tType){
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
			if(tType == "TestStep"){
				testStepViewTSViewDT_Container(jsonObj);
			}else if(tType == "EvidenceList"){
				evidenceListDT_Container(jsonObj);
			}else if(tType == "TestcaseExportData"){
				testcaseExportDataDT_Container(jsonObj);
			}else if(tType == "Features"){
				featuresDT_Container(jsonObj);
			}else if(tType == "Defects"){
				defectInfoDT_Container(jsonObj);
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
var testStepViewTSViewDT_oTable='';
var editorTestStepViewTSView='';
function testStepViewTSViewDataTableHeader(){
	var childDivString ='<table id="testStepViewTSView_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>Name</th>'+
			'<th>Description</th>'+
			'<th>Test Step Input</th>'+
			'<th>Expected Output</th>'+
			'<th>Observed Output</th>'+
			'<th>Result</th>'+
			'<th>Comments</th>'+
			'<th>Evidence</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function testStepViewTSViewDT_Container(jsonObj){	
	try{
		if ($("#dataTableContainerForTestStepViewTSView").children().length>0) {
			$("#dataTableContainerForTestStepViewTSView").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = testStepViewTSViewDataTableHeader(); 			 
	$("#dataTableContainerForTestStepViewTSView").append(childDivString);
	
	editorTestStepViewTSView = new $.fn.dataTable.Editor( {
	    "table": "#testStepViewTSView_dataTable",
		ajax: "",
		ajaxUrl: "",
		idSrc:  "",
		i18n: {
	        create: {
	            title:  "Create a new Test Step",
	            submit: "Create",
	        }
	    },
		fields: []
	});
	
	testStepViewTSViewDT_oTable = $("#testStepViewTSView_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [8]; // search column TextBox Invisible Column position
	     		  $('#testStepViewTSView_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeTestStepViewTSViewDT();
			   },  
		   buttons: [
						//{ extend: "create", editor: editorTestStepViewTSView },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Test Step',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Test Step',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		'colvis',
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "testStepsId", className: 'disableEditInline', sWidth: '7%' },
           { mData: "testStepsName", className: 'disableEditInline', sWidth: '25%' },
           { mData: "description", className: 'disableEditInline', sWidth: '25%' },		
           { mData: "input",  className: 'disableEditInline', sWidth: '7%' },
           { mData: "expectedOutput",  className: 'disableEditInline', sWidth: '7%' },
           { mData: "observedOutput",  className: 'disableEditInline', sWidth: '7%' },
           { mData: "result",  className: 'disableEditInline', sWidth: '7%' },
           { mData: "comments",  className: 'disableEditInline', sWidth: '7%' },           
           { mData: null,				 
	           	bSortable: false,
	           	mRender: function(data, type, full) {				            	
	          		 var img = ('<div style="display: flex;">'+
		       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
		       				'<img src="css/images/list_metro.png" class="evidenceImg" title="Evidence" />'+
	    	       		'</div>');	      		
	          		 return img;
	           	}
           },	
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorTestStepViewTSView-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#testStepViewTSView_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorTestStepViewTSView.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#testStepViewTSView_dataTable tbody').on('click', 'td .evidenceImg', function () {
		var tr = $(this).closest('tr');
    	var row = testStepViewTSViewDT_oTable.DataTable().row(tr);
    	url= 'testcase.list.eveidence?tcerId='+row.data().teststepexecutionresultid+'&type=teststep&jtStartIndex=0&jtPageSize=10';
    	jsonObj={"Title":"Evidence List",
    			"url": url,	
    			"jtStartIndex":0,
    			"jtPageSize":10000,				
    			"componentUsageTitle":"Evidence List",
    	};
		$('#testStepViewTSViewDT_Child_Container').modal();
		$(document).off('focusin.modal');
		assignTestStepViewTSViewDataTableValues(jsonObj, "EvidenceList");
	});
	
	$("#testStepViewTSView_dataTable_length").css('margin-top','8px');
	$("#testStepViewTSView_dataTable_length").css('padding-left','35px');		
	
	testStepViewTSViewDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutTestStepViewTSViewDT='';
function reInitializeTestStepViewTSViewDT(){
	clearTimeoutTestStepViewTSViewDT = setTimeout(function(){				
		testStepViewTSViewDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTestStepViewTSViewDT);
	},200);
}
//END: ConvertDataTable - TestStepViewTSView

//BEGIN: ConvertDataTable - EvidenceList
var evidenceListDT_oTable='';
var editorEvidenceList='';
function evidenceListDataTableHeader(){
	var childDivString ='<table id="evidenceList_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Evidence Name</th>'+
			'<th>Description</th>'+
			'<th>File Type</th>'+
			'<th>Size</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function evidenceListDT_Container(jsonObj){	
	try{
		if ($("#dataTableContainerForEvidenceList").children().length>0) {
			$("#dataTableContainerForEvidenceList").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = evidenceListDataTableHeader(); 			 
	$("#dataTableContainerForEvidenceList").append(childDivString);
	
	editorEvidenceList = new $.fn.dataTable.Editor( {
	    "table": "#evidenceList_dataTable",
		ajax: "",
		ajaxUrl: "",
		idSrc:  "",
		i18n: {
	        create: {
	            title:  "Create a new Evidence List",
	            submit: "Create",
	        }
	    },
		fields: []
	});
	
	evidenceListDT_oTable = $("#evidenceList_dataTable").dataTable( {				 	
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
	     		  $('#evidenceList_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeEvidenceListDT();
			   },  
		   buttons: [
						//{ extend: "create", editor: editorEvidenceList },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Evidence List',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Evidence List',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		'colvis',
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "evidencename", className: 'disableEditInline', sWidth: '35%',
	           	mRender: function (data, type, full) {	           		
	           		$("#evidenceUplaoded").append("<div style=display:none; id=evidenceTestStepFilename"+full.evidenceid+"></div>");
    				//document.getElementById("evidenceTestStepFilename"+full.evidenceid).innerHTML = full.fileuri;
    				if(full.evidencename != "N/A"){
    					data = ("<a style='color: #0000FF'; href=javascript:displayInBrowser('"+full.evidenceid+"');>" + full.evidencename + "</a>");
    					return data;
    				}
    				else{
    					data = ("<a style='color: #0000FF'; href=#>" + full.evidencename + "</a>");
    					return data;
    				}
            	},
           },	
           { mData: "description", className: 'disableEditInline', sWidth: '35%' },		
           { mData: "filetype",  className: 'disableEditInline', sWidth: '15%' },
           { mData: "size",  className: 'disableEditInline', sWidth: '15%' },
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorEvidenceList-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#evidenceList_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorEvidenceList.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#evidenceList_dataTable_length").css('margin-top','8px');
	$("#evidenceList_dataTable_length").css('padding-left','35px');		
	
	evidenceListDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutEvidenceListDT='';
function reInitializeEvidenceListDT(){
	clearTimeoutEvidenceListDT = setTimeout(function(){				
		evidenceListDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutEvidenceListDT);
	},200);
}
//END: ConvertDataTable - EvidenceList

//BEGIN: ConvertDataTable - ExportData
function loadTCExeIdExportDataViewDataTableInit(tcerId){
	url= 'export.name.code.list?testCaseExecutionResultId='+tcerId+'&jtStartIndex=0&jtPageSize=10';
	jsonObj={"Title":"Testcase Export Data",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"Testcase Export Data",
	};
	testcaseExportDataDataTableContainer.init(jsonObj);
}

var testcaseExportDataDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		assignTestStepViewTSViewDataTableValues(jsonObj, "TestcaseExportData");
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

var testcaseExportDataDT_oTable='';
var editorTestcaseExportData='';
function testcaseExportDataDataTableHeader(){
	var childDivString ='<table id="testcaseExportData_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Test Management System</th>'+
			'<th>Export System Result Id</th>'+
			'<th>Export Date</th>'+
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
function testcaseExportDataDT_Container(jsonObj){	
	try{
		if ($("#dataTableContainerForTestcaseExportData").children().length>0) {
			$("#dataTableContainerForTestcaseExportData").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = testcaseExportDataDataTableHeader(); 			 
	$("#dataTableContainerForTestcaseExportData").append(childDivString);
	
	editorTestcaseExportData = new $.fn.dataTable.Editor( {
	    "table": "#testcaseExportData_dataTable",
		ajax: "",
		ajaxUrl: "",
		idSrc:  "",
		i18n: {
	        create: {
	            title:  "Create a new Testcase Export Data",
	            submit: "Create",
	        }
	    },
		fields: []
	});
	
	testcaseExportDataDT_oTable = $("#testcaseExportData_dataTable").dataTable( {				 	
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
	     		  $('#testcaseExportData_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeTestcaseExportDataDT();
			   },  
		   buttons: [
						//{ extend: "create", editor: editorTestcaseExportData },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Testcase Export Data',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Testcase Export Data',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		'colvis',
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "testSystemName", className: 'disableEditInline', sWidth: '40%' },	
           { mData: "resultCode", className: 'disableEditInline', sWidth: '40%' },		
           { mData: "exportedDate",  className: 'disableEditInline', sWidth: '20%' },
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorTestcaseExportData-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#testcaseExportData_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorTestcaseExportData.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#testcaseExportData_dataTable_length").css('margin-top','8px');
	$("#testcaseExportData_dataTable_length").css('padding-left','35px');		
	
	testcaseExportDataDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutTestcaseExportDataDT='';
function reInitializeTestcaseExportDataDT(){
	clearTimeoutTestcaseExportDataDT = setTimeout(function(){				
		testcaseExportDataDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTestcaseExportDataDT);
	},200);
}
//END: ConvertDataTable - ExportData

//BEGIN: ConvertDataTable - Features
function loadTestCaseFeaturesDataTableInit(testcaseId){
	url= 'administration.testcase.feature.mappedlist?testCaseId='+testcaseId+'&jtStartIndex=0&jtPageSize=10';
	jsonObj={"Title":"Features",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"Features",
	};
	featuresDataTableContainer.init(jsonObj);
}

var featuresDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		assignTestStepViewTSViewDataTableValues(jsonObj, "Features");
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

var featuresDT_oTable='';
var editorFeatures='';
function featuresDataTableHeader(){
	var childDivString ='<table id="features_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Feature ID</th>'+
			'<th>Feature Code</th>'+
			'<th>Feature Name</th>'+
			'<th>Display Name</th>'+
			'<th>Feature Description</th>'+
			'<th>Parent Feature</th>'+
			'<th>Status</th>'+
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
function featuresDT_Container(jsonObj){	
	try{
		if ($("#dataTableContainerForFeatures").children().length>0) {
			$("#dataTableContainerForFeatures").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = featuresDataTableHeader(); 			 
	$("#dataTableContainerForFeatures").append(childDivString);
	
	editorFeatures = new $.fn.dataTable.Editor( {
	    "table": "#features_dataTable",
		ajax: "",
		ajaxUrl: "",
		idSrc:  "",
		i18n: {
	        create: {
	            title:  "Create a new Feature",
	            submit: "Create",
	        }
	    },
		fields: []
	});
	
	featuresDT_oTable = $("#features_dataTable").dataTable( {				 	
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
	     		  $('#features_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeFeaturesDT();
			   },  
		   buttons: [
						//{ extend: "create", editor: editorFeatures },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Feature',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Feature',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		'colvis',
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "productFeatureId", className: 'disableEditInline', sWidth: '10%' },	
           { mData: "productFeatureCode", className: 'disableEditInline', sWidth: '10%' },		
           { mData: "productFeatureName",  className: 'disableEditInline', sWidth: '20%' },
           { mData: "displayName", className: 'disableEditInline', sWidth: '20%' },		
           { mData: "productFeatureDescription",  className: 'disableEditInline', sWidth: '20%' },
           { mData: "parentFeatureName", className: 'disableEditInline', sWidth: '10%' },		
           { mData: "parentFeatureStatus",  className: 'disableEditInline', sWidth: '10%', 
        	   mRender: function (data, type, full) {
        		   data = (data == 1) ? "YES" : "NO"; 		           	 
        		   return data;
	           },
           },
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorFeatures-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	});	
	
	// Activate an inline edit on click of a table cell
	$('#features_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorFeatures.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#features_dataTable_length").css('margin-top','8px');
	$("#features_dataTable_length").css('padding-left','35px');		
	
	featuresDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutFeaturesDT='';
function reInitializeFeaturesDT(){
	clearTimeoutFeaturesDT = setTimeout(function(){				
		featuresDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutFeaturesDT);
	},200);
}
//END: ConvertDataTable - Features

//BEGIN: ConvertDataTable - Defects
var defectInfoDT_oTable='';
var editorDefectInfo='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;
var urlForDefectInfo;

function loadDefectInfoDataTable(tcerId){
	urlForDefectInfo = 'testcase.defect.list?tcerId='+tcerId;
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [{id:"defectSeverity", url:'common.list.defectSeverity'},
		              {id:"bugFilingStatus", url:'administration.workFlow.list?entityType=1'},
		              {id:"reportedInBuildId", url:'process.list.builds.by.product?productId='+prodId},
		              {id:"reportedInBuildId", url:'process.list.builds.by.product?productId='+prodId}
		              ];
	defectInfoOptions_Container(optionsArr);
}
		
function defectInfoOptions_Container(urlArr){
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
				 defectInfoOptions_Container(optionsArr);
			 }else{
				defectInfoDataTableInit();	
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

function defectInfoDataTableInit(){
	 url= urlForDefectInfo +'&jtStartIndex=0&jtPageSize=10';
	 jsonObj={"Title":"Defects",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"Defects",
	};
	defectInfoDataTableContainer.init(jsonObj);
}

var defectInfoDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		assignTestStepViewTSViewDataTableValues(jsonObj, "Defects");
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function defectInfoDataTableHeader(){
	var childDivString ='<table id="defectInfo_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Id</th>'+
			'<th>Bug Title</th>'+
			'<th>Description</th>'+
			'<th>Severity</th>'+
			'<th>Filing Status</th>'+
			'<th>Remarks</th>'+
			'<th>Creation Time</th>'+
			'<th>System Name</th>'+
			'<th>System Bug Id</th>'+
			'<th>File Bug</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function defectInfoDT_Container(jsonObj){	
	try{
		if ($("#dataTableContainerForDefectInfo").children().length>0) {
			$("#dataTableContainerForDefectInfo").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = defectInfoDataTableHeader(); 			 
	$("#dataTableContainerForDefectInfo").append(childDivString);
	
	editorDefectInfo = new $.fn.dataTable.Editor( {
	    "table": "#defectInfo_dataTable",
		ajax: "",
		ajaxUrl: "defects.for.analyse.update",
		idSrc:  "testExecutionResultBugId",
		i18n: {
	        create: {
	            title:  "Create a new Defects",
	            submit: "Create",
	        }
	    },
		fields: []
	});
	
	defectInfoDT_oTable = $("#defectInfo_dataTable").dataTable( {				 	
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
	     		  $('#defectInfo_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeDefectInfoDT();
			   },  
		   buttons: [
						//{ extend: "create", editor: editorDefectInfo },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Defects',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Defects',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		'colvis',
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "testExecutionResultBugId", className: 'disableEditInline', sWidth: '7%' },	
           { mData: "bugTitle", className: 'editable', sWidth: '8%' },	
           { mData: "bugDescription", className: 'editable', sWidth: '20%' },		
           { mData: "severityName", sWidth: '7%'},
           { mData: "bugFilingStatusName", sWidth: '7%' },
           { mData: "remarks", className: 'editable', sWidth: '7%' },
           { mData: "bugCreationTime", className: 'editable', sWidth: '7%' },
           { mData: "bugManagementSystemName", className: 'editable', sWidth: '15%' },
           { mData: "bugManagementSystemBugId", className: 'editable', sWidth: '7%' },
           { mData: "fileBugInBugManagementSystem", className: 'editable', sWidth: '7%' },
       ],       
       rowCallback: function ( row, data ) {
    	  // $('input.editorDefectInfo-active', row).prop( 'checked', data.readyState == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#defectInfo_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorDefectInfo.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#defectInfo_dataTable_length").css('margin-top','8px');
	$("#defectInfo_dataTable_length").css('padding-left','35px');		
	
	defectInfoDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutDefectInfoDT='';
function reInitializeDefectInfoDT(){
	clearTimeoutDefectInfoDT = setTimeout(function(){				
		defectInfoDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDefectInfoDT);
	},200);
}
//END: ConvertDataTable - Defects

function loadTestResults(data){		
	var result=data.Records;
	$("#testCaseDetailsModal #preconditions").empty();

	$("#testCaseDetailsModal #expectedOutput").empty();

	$("#testCaseDetailsModal #testcaseInput").empty();
	$("#testCaseDetailsModal #observedOutput").empty();
	$("#testCaseDetailsModal #comments").empty();
	var selectedShift="";
	
	$('#testCaseDetailsModal #actualshift_options').empty(); 
	$.each(result, function(i,item){ 
		if(item.preconditions==null)
			$("#testCaseDetailsModal #preconditions").append("<div style='font-size:small;'  > NA </div>");
		else
			$("#testCaseDetailsModal #preconditions").append("<div style='font-size:small;'  >"+item.preconditions+"</div>");
		if(item.expectedOutput==null){
			$("#testCaseDetailsModal #expectedOutput").append("<div style='font-size:small;' >NA</div>");
			}
		else{
			exceptedOutput=item.expectedOutput;
			$("#testCaseDetailsModal #expectedOutput").append("<div style='font-size:small;' >"+item.expectedOutput+"</div>");
		}
		if(item.testcaseInput==null)
			$("#testCaseDetailsModal #testcaseInput").append("<div style='font-size:small;'  >NA</div>");
		else
			$("#testcaseInput").append("<div style='font-size:small;'  >"+item.testcaseInput+"</div>");

		if(item.observedOutput==null){
				$("#testCaseDetailsModal #observedOutput").val("").attr("disabled",true);
		}
		else{
				$("#testCaseDetailsModal #observedOutput").val(item.observedOutput).attr("disabled",true);
		}
		if(item.comments==null){
				$("#testCaseDetailsModal #comments").val("").attr("disabled",true);
		}
		else{
				$("#testCaseDetailsModal #comments").val(item.comments).attr("disabled",true);
		}
		
		if(item.result=="PASSED"){
			$("#exeResult").css("background-color",'rgba(0, 128, 0, 0.78)');
		}else if(item.result=="FAILED"){
			$("#exeResult").css("background-color",'#cb5a5e');
		}else if(item.result=="NORUN"){
			$("#exeResult").css("background-color",'#f3c200');
		} else if(item.result=="BLOCKED"){
			$("#exeResult").css("background-color",'#cb5a5e');
		}	
		
		if((item.isExecuted == 0) || (item.isExecuted == 0)) {
			$("#testCaseDetailsModal #blocked").removeClass("active").siblings("label").removeClass("active");
		}
		if(item.actualExecutionDate == null){
			isLoad = true;
			$("#testCaseDetailsModal #datepickerAC").datepicker('setDate','today');
			currActExecDate = $("#datepickerAC").val();
		}else{
			isLoad = true;
			currActExecDate = setFormattedDate(item.actualExecutionDate);
			$("#testCaseDetailsModal #datepickerAC").datepicker('setDate', setFormattedDate(item.actualExecutionDate));
		}
		if(item.actualShiftId==null){
			selectedShift=-1;
		}else{
			selectedShift=item.actualShiftId
		}			
	});
	isLoad = false;
	
		if(selectedShift!=-1){
			$('select[name="actualshift_options"] option[value="'+selectedShift+'"]').attr("selected","selected");
       	}		
}

function showGridView(){
	getHiddenFieldValues();
	document.getElementById("currentViewType").value = "Grid";		
	currentView="Grid";	
	removetable();	
	displayResoucesAvailability();
}

function showTableView(){
	removeGridItems();
	getHiddenFieldValues();
	document.getElementById("currentViewType").value = "Table";
	currentView="Table"
	$('#div_Table3').show();	
	displayResoucesAvailability(); 	
}

function setActiveValue(val) {
	activeResultVal = val;
}

function saveTestCaseResult(modifiedField){ 
	var productMode = document.getElementById("hdnProductMode").value;
	var value="";
	if(document.getElementById('tcerIdhidden')!=null)
		tcerId=document.getElementById('tcerIdhidden').value;
	 var thediv = document.getElementById('reportbox');
	 var url="";
	 var executionTime=document.getElementById('executionTime').value;
	
	 if(executionTime==null)
		 executionTime="";
	 
	 if(modifiedField!='image'){
		 if(modifiedField=='result' ){
			    value=activeResultVal;//$.trim($(".radio-toolbar>.btn-group>label.active").text());
				console.log(value);
				$('.timer').timer('pause');
				$("#pause").hide();
				$("#remove").hide();
				$("#resume").hide();
				$("#start").hide();
				
				if(value=='Pass'){
					if(exceptedOutput=='')
						document.getElementById('observedOutput').value="Passed";
					else
						document.getElementById('observedOutput').value=exceptedOutput;
				}else if(value=='Fail'){
					document.getElementById('observedOutput').value="Failed";
				}else if(value=='No Run'){
					document.getElementById('observedOutput').value="No Run";
				}else if(value=='Blocked'){
					document.getElementById('observedOutput').value="Blocked";
				}
				//$('.ToggleSwitch').toggleCheckedState(true);
				if($(".make-switch").bootstrapSwitch('state') == false)
					$(".make-switch").bootstrapSwitch('toggleState');
				if($("#" + $("#hdnCurrentSelectedTestCaseID").val()).find('span.badge').length == 0)
					$("#" + $("#hdnCurrentSelectedTestCaseID").val()+" div").append('<span class="badge badge-success" title="Executed" style="float:right">&#9989</span>');
		 
		 }else if(modifiedField=='actualDate'){
				value=datepickerAC.value;
				currActExecDate = value;
		 
		 }else if(modifiedField=='actualshift'){
				value= $('#actualshift_options').val();
				if(value==-1){
					callAlert("Please select Actual Shift");
					return false;
				}
		 
		 }else if(modifiedField=='executionStatus'){
			 var copyDataType = "";
			 $(':checkbox:checked').each(function(i){
			    	copyDataType = $(this).val();
			    });
			 
				 if(copyDataType=="on"){
					 value="1";
					 if(document.getElementById("timer")!=null)
							document.getElementById("timer").style.display = "none";
					 if(document.getElementById("timer1")!=null)
							document.getElementById("timer1").style.display = "none";
					 if(document.getElementById("timer2")!=null)
							document.getElementById("timer2").style.display = "none";
					 if(document.getElementById("execTimer")!=null)
							document.getElementById("execTimer").style.display = "block";
					 if(executionTime==null){
						if(document.getElementById('execTimerVal')!=null)
							document.getElementById('execTimerVal').value = "0 sec" ;
					 }else{
						if(document.getElementById('execTimerVal')!=null)
							document.getElementById('execTimerVal').value = executionTime ;
					 }
				 	
				 }else{
					 value="0";
					 document.getElementById("timer").style.display = "block";
					 document.getElementById("timer1").style.display = "block";
					 document.getElementById("timer2").style.display = "block";
					 document.getElementById("execTimer").style.display = "none";
					 document.getElementById('execTimerVal').value="";
						
						if(executionTime!=null && executionTime!=0 && executionTime!="" && executionTime!="null"){
							$('.timer').timer('reset');
							$('.timer').timer({
							    seconds: executionTime 
							});
							jQuery('#start').click();
						}else{
							$('.timer').timer('reset');
							
							jQuery('#start').click();
						}
					 //from old end
						$("#" + $("#hdnCurrentSelectedTestCaseID").val()).find('span.badge').remove();
				 }
		 	}
		 
		    url='testcase.results.update?tcerId='+tcerId+"&modifiedfField="+modifiedField+"&modifiedValue="+value+"&executionTime="+executionTime;
			if (thediv.style.display == "none") {
				 $.post(url,function(data) {
					 $.unblockUI();
					});
				} else {
					thediv.style.display = "none";
					thediv.innerHTML = '';
				}
			 if(modifiedField=='actualDate'){
				 loadActualShift(-1);      
			 }
	 }else if(modifiedField=='image'){
		value=document.getElementById("uploadImage").files[0];
	
		 var formdata = new FormData();
		 formdata.append("uploadImage", value);
		 
	     url='testcase.results.update.evidence?tcerId='+tcerId+"&modifiedfField="+modifiedField+"&modifiedValue="+value+"&type=testcase"+"&executionTime="+executionTime;
	     if (thediv.style.display == "none") {
		     $.ajax({
				    url:url,
				    method: 'POST',
				    contentType: false,
				    data: formdata,
				    dataType:'json',
				    processData: false,
				    success : function(data) {
				    	if(data.Result=="ERROR"){
				    		callAlert(data.Message);
				    		return false;
				    	}
				    	 loadEvidenceDetails();
				    	 $.unblockUI();
				    },
				});
	     } else {
				thediv.style.display = "none";
				thediv.innerHTML = '';
			}
	}else{
		return false;
	}
}

function loadEvidenceDetails(){
	if(document.getElementById('tcerIdhidden')!=null)
		tcerId=document.getElementById('tcerIdhidden').value;
	else
		tcerId=6;
	
	var evidenceURL="testcase.list.eveidence?tcerId="+tcerId+"&type=testcase";
	var root="";
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : evidenceURL,
		dataType : 'json',
		success : function(data) {
			var result=data.Records;
			$("#evidenceUplaoded").empty();
			
			$.each(result, function(i,item){ 
				root=item.fileuri;
				$("#evidenceUplaoded").append("<div style=display:none; id=evidenceFilename"+i+"></div>");
				document.getElementById("evidenceFilename"+i).innerHTML = root;
				$("#evidenceUplaoded").append("<div style=font-size:small;><a href=javascript:downloadEvidenceinTestCaseDetails('evidenceFilename"+i+"');>"+item.evidencename+"</a>" +
				"<span onclick='javascript:deleteEvidence("+item.evidenceid+")'; style='padding-left: 5px;padding-top: 5px;'><i class='fa fa-close showHandCursor'  title='Delete Evidence'></i></span></div>");
			});
		}		
	});
}

function downloadEvidenceinTestCaseDetails(urlId){
	var urlfinal="rest/download/evidence?fileName="+urlId;
  	parent.window.location.href=urlfinal;  	
}

//Added for displaying the file in browser
function displayInBrowser(evidenceId){
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : 'getEvidenceId.filename?evidenceId='+evidenceId,
		dataType : 'json',
		success : function(data) {
			var evidenceNameLen = (data.exportFileName).split("\\").length;
			var evidenceName = (data.exportFileName).split("\\")[evidenceNameLen-1];
			if(evidenceName == "null" || evidenceName == ""){
				callAlert('No Screenshot available for the teststep');
				return false;
			}else{
				if(atlasIframeFlag){
					var messageData = {};
					messageData["action-request"] = "evidence-link";
					messageData["evidence-link-path"] = data.exportFileName;
					parent.postMessage(messageData, '*');
				}else{
					parent.window.open(data.exportFileName);
				}

			}
		}	
	});
 }
