var TestCaseOverallViewDetails = function() {
  
   var initialise = function(jsonObj){
	   listTestCaseDetailsOverall(jsonObj);
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
var testcaseOverallJsonObj='';
var testcaseIdObj;
function listTestCaseDetailsOverall(jsonObj){	
	testcaseOverallJsonObj=jsonObj;
	testcaseIdObj=jsonObj.testCaseID;
	$("#testCaseDetailedViewsModal").find("h4:first").text(jsonObj.Title);
	$("#testCaseDetailedViewsModal").find("h4:first").text("TestCase Details :- "+ jsonObj.testCaseID + "- "+jsonObj.testCaseName );
	getTestCaseDetailsForResultOverall();	
		
	$("#tablistRelatedTestcase li").removeClass('active');
	$("#tablistRelatedTestcase li").eq(0).addClass('active');	
	
	hideRelatedTestCaseTabs();
	$("#relatedTestCaseDetails").show();
	
	$("#relatedFeatures").removeClass("active in");
	$("#relatedTestScripts").removeClass("active in");
	$("#relatedTestCaseDetails").addClass("active in");
	$("#similarToTestcase").removeClass("active in");
	
	$("#testCaseDetailedViewsModal").modal();		
}

function hideRelatedTestCaseTabs(){
	$("#relatedTestCaseDetails").hide();
	$("#relatedTestScriptsDetails").hide();
	$("#relatedFeaturesDetails").hide();
	$("#predecessorsTestcaseDetails").hide();
	$("#similarToTestcaseDetails").hide();
	$("#similarToTestcase").hide();
}

function getRelatedTestFeatures(){
	hideRelatedTestCaseTabs();	
	$("#relatedFeaturesDetails").show();
	
	var jsonObj=testcaseOverallJsonObj;
	$("#relatedFeaturesDetails").html('');
	$("#relatedFeaturesDetails").html('<div id="relatedFeaturesDetailsChild1"></div>');
	
	var url = 'administration.testcase.feature.mappedlist?testCaseId='+jsonObj.testCaseID+'&jtStartIndex=0&jtPageSize=10000';		
	openLoaderIcon();
	
	$.ajax({
	    type: "POST",
	    url : url,
	    cache:false,
	    success: function(data) {
			if(data != null){
				data = data.Records;
				relatedProductTestcase(data);
			}
	    	closeLoaderIcon();
	    	
	    },error : function(data){
			closeLoaderIcon();
		}
	});   
}

function getRelatedTestScript(){
	hideRelatedTestCaseTabs();
	$("#relatedTestScriptsDetails").show();
	
	var jsonObj=testcaseOverallJsonObj;
	var testCaseId;
	$("#relatedTestScriptsDetails").html('');
	$("#relatedTestScriptsDetails").html('<div id="relatedFeaturesDetailsChild2"></div>');
	if(jsonObj.testCaseID.includes('F') || jsonObj.testCaseID.includes('S') || jsonObj.testCaseID.includes('C')) {
		testCaseId = jsonObj.testCaseID.substring(1,jsonObj.testCaseID.length);
	}
	var url ='get.mapped.testscripts.by.testcaseId?testcaseId='+testCaseId+'&jtStartIndex=0&jtPageSize=10000';
	openLoaderIcon();
	
	$.ajax({
	    type: "POST",
	    url : url,
	    cache:false,
	    success: function(data) {
			if(data != null){
				data = data.Records;
				relatedProductTestScript(data);
			}
	    	closeLoaderIcon();
	    	
	    },error : function(data){
			closeLoaderIcon();
		}
	});   
}

function getTestCaseDetailsForResultOverall(){
	$("#relatedTestCaseDetails").show();
	
	$("#relatedFeatures").removeClass("active in");
	$("#relatedTestScripts").removeClass("active in");
	$("#relatedTestCaseDetails").addClass("active in");	
	
	var jsonObj=testcaseOverallJsonObj;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : jsonObj.testCaseOverAllDetailsUrl,
		dataType : 'json',
		success : function(data) {
			var result=data.Records;
			var executionName="";
			var executionTime="";
			var executionStatus="";
			var isapproved = "";
			$("#testCaseDetailedViewsModal #currCaseName, #currCaseId").text("");
			$("#testCaseDetailedViewsModal #testcaseId").empty();
			$("#testCaseDetailedViewsModal #testcaseName").empty();
			$("#testCaseDetailedViewsModal #preconditions").empty();
			$("#testCaseDetailedViewsModal #expectedOutput").empty();
			$("#testCaseDetailedViewsModal #testcaseCode").empty();
			$("#testCaseDetailedViewsModal #description").empty();
			$("#testCaseDetailedViewsModal #executionPriority").empty();
			$("#testCaseDetailedViewsModal #mappedTestscriptCount").empty();
			$("#testCaseDetailedViewsModal #mappedFeatureCount").empty();
			
			//////////////////////// - modified started -			
			$("#testCaseDetailedViewsModal #productName").text('--');
			$("#testCaseDetailedViewsModal #productVersionName").text('--');
			$("#testCaseDetailedViewsModal #productBuildName").text('--');
			
			$("#testCaseDetailedViewsModal #testcaseId").text('--');
			$("#testCaseDetailedViewsModal #testcaseCode").text('--');
			$("#testCaseDetailedViewsModal #testcaseName").text('--');
			$("#testCaseDetailedViewsModal #testcasetype").text('--');
			$("#testCaseDetailedViewsModal #testcasePriority").text('--');
	
			//mamtha comments
			$("#testCaseDetailedViewsModal #testcaseInput").text('--');
			$("#testCaseDetailedViewsModal #expectedOutput").text('--');
			$("#testCaseDetailedViewsModal #preconditions").text('--');
			
			$("#testCaseDetailedViewsModal #createdDate").text('--');
			$("#testCaseDetailedViewsModal #modifiedDate").text('--');
			$("#testCaseDetailedViewsModal #totalWpTs").text('--');			
						
			$("#testCaseDetailedViewsModal #exeType").text('--');	
			$("#testCaseDetailedViewsModal #sourceType").text('--');
			
			$("#testCaseDetailedViewsModal #testCaseScriptFileName").text('--');	
			$("#testCaseDetailedViewsModal #testCaseScriptQualifiedName").text('--');
			
			$("#testCaseDetailedViewsModal #mappedTestscriptCount").text('--');
			$("#testCaseDetailedViewsModal #mappedFeatureCount").text('--');
			//mamtha comments			
			//////////////////////// - modified ended -
			
			$.each(result, function(i,item){				
				if(item.testcaseId != null){
					$("#testCaseDetailedViewsModal #currCaseId").text(item.testcaseId);		
				}
				if(item.testcaseName != null){
					$("#testCaseDetailedViewsModal #currCaseName").text(item.testcaseName);	
				}
				if(item.expectedOutput != null){
					$("#testCaseDetailedViewsModal #expectedOutput").append("<div style='font-size:small;'  >"+item.expectedOutput+"</div>");	
				}
				if(item.testcaseCode != null){
					$("#testCaseDetailedViewsModal #testcaseCode").append("<div style='font-size:small;'  >"+item.testcaseCode+"</div>");
				}
				if(item.testcaseDescription != null){
					$("#testCaseDetailedViewsModal #description").append("<div style='font-size:small;'  >"+item.testcaseDescription+"</div>");
				}				
				
				////////////////////////// - modified started -
				if(item.productName != null){
					$("#testCaseDetailedViewsModal #productName").text(item.productName);
				}
				if(item.productVersionName != null){
					$("#testCaseDetailedViewsModal #productVersionName").text(item.productVersionName);
				}
				if(item.productBuildName != null){
					$("#testCaseDetailedViewsModal #productBuildName").text(item.productBuildName);	
				}
				if(item.testcaseId != null){
					$("#testCaseDetailedViewsModal #testcaseId").text(item.testcaseId);
				}
				var tcCode = "";
				if(item.testcaseCode != null){
					tcCode = item.testcaseCode;
				}				
				$("#testCaseDetailedViewsModal #testcaseCode").text(tcCode);				
				if(item.testcaseName != null){
					$("#testCaseDetailedViewsModal #testcaseName").text(item.testcaseName);
				}
				if(item.testcaseType != null){
					$("#testCaseDetailedViewsModal #testcasetype").text(item.testcaseType);
				}
				if(item.testcasePriority != null){
					$("#testCaseDetailedViewsModal #testcasePriority").text(item.testcasePriority);
				}				
	
				//mamtha comments
				if(item.testcaseInput != null){
					$("#testCaseDetailedViewsModal #testcaseInput").text(item.testcaseInput);
				}			
				if(item.expectedOutput != null){
					$("#testCaseDetailedViewsModal #expectedOutput").text(item.expectedOutput);
				}			
				if(item.preconditions != null){
					$("#testCaseDetailedViewsModal #preconditions").text(item.preconditions);
				}			
				if(item.exeType != null){
					$("#testCaseDetailedViewsModal #exeType").text(item.exeType);
				}			
				if(item.sourceType != null){
					$("#testCaseDetailedViewsModal #sourceType").text(item.sourceType);
				}
				if(item.testCaseScriptFileName != null){
					$("#testCaseDetailedViewsModal #testCaseScriptFileName").text(item.testCaseScriptFileName);
				}			
				if(item.testCaseScriptQualifiedName != null){
					$("#testCaseDetailedViewsModal #testCaseScriptQualifiedName").text(item.testCaseScriptQualifiedName);
				}				
				//mamtha comments
				
				if(item.createdDate != null){
					$("#testCaseDetailedViewsModal #createdDate").text(item.createdDate);	
				}
				if(item.modifiedDate != null){
					$("#testCaseDetailedViewsModal #modifiedDate").text(item.modifiedDate);	
				}
				if(item.totalWpTs != null){
					$("#testCaseDetailedViewsModal #totalWpTs").text(item.totalWpTs);	
				}				
				
				if(item.result != null){					
					$("#testCaseDetailedViewsModal #exeResult").text(item.result);	
				}
				if(item.expectedOutput != null){
					$("#testCaseDetailedViewsModal #expOutput").text(item.expectedOutput);	
				}
				if(item.observedOutput != null){
					$("#testCaseDetailedViewsModal #obsOutput").text(item.observedOutput);		
				}
				if(item.startTime != null){
					$("#testCaseDetailedViewsModal #exeStartTime").text(item.startTime);				
				}
				if(item.endTime != null){
					$("#testCaseDetailedViewsModal #exeEndTime").text(item.endTime);					
				}
				if(item.jobFailureMessage != null){
					$("#testCaseDetailedViewsModal #failRemarks").text(item.jobFailureMessage);		
				}
				
				if(item.mappedFeatureCount != null){
					$("#testCaseDetailedViewsModal #mappedFeatureCount").text(item.mappedFeatureCount);		
				}
				if(item.mappedTestscriptCount != null) {
					$("#testCaseDetailedViewsModal #mappedTestscriptCount").text(item.mappedTestscriptCount);		
				}
				
				if(item.executionStatus==3)
					executionName="NotStarted";
				else if(item.executionStatus==1)
					executionName="Assigned";
				else if(item.executionStatus==2)
					executionName="Completed";
				
				$("#testCaseDetailedViewsModal #executionStatus").text(executionName);				
							
				if(document.getElementById('tcerIdhidden')!=null)
					document.getElementById('tcerIdhidden').value=item.testCaseExecutionResultId;				
				
				loadTestStepViewDataTable(item.testcaseId);
				loadTestSuitesViewDataTable(item.testcaseId);
			});
			
			$('.hover-star').rating({ 
			 }); 					
		
			if(document.getElementById("paginationButton")!=null)
			document.getElementById("paginationButton").style.display = "block";	
			$("#testCaseDetailedViewsModal").modal();			
		}
	});	
}

function loadTestcasePredecessors(){
	hideRelatedTestCaseTabs();	
	$("#predecessorsTestcaseDetails").show();	
	var containerID = "predecessorsTestcasesChild1";
	var urlToGetPredecessorTestcase = 'get.predecessors.testcase.list?testCaseId='+testcaseIdObj;		
	listFeaturesOfSelectedProduct(urlToGetPredecessorTestcase,"240px" ,"childTable1" ,containerID ,0);
	$("#predecessorsTestcaseDetails").html('');
	$("#predecessorsTestcaseDetails").html('<div id="'+containerID+'"></div>');	
}

function loadSimilarToTestcase(){
	hideRelatedTestCaseTabs();	
	$("#similarToTestcaseDetails").show();	
	var containerID = "similarToTestcasesChild1";
	var urlToGetSimilarToTestcases = 'get.ise.similar.to.testcase.list?productId='+productId+'&testCaseId='+testcaseIdObj+'&jtStartIndex=0&jtPageSize=10000';		
	listSimilarTestcasesList(urlToGetSimilarToTestcases);
}

function listSimilarTestcasesList(url){
	$("#similarToTestcase").show();	
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			
			if(data == null || data.Result=="ERROR" || Object.keys(data).length==0){
      		    data = [];						
			}else{
				data = data.DATA[0];				
			}
			
			var checkBoxCollectionStr = '<div class="form-group"><label class="col-lg-2 control-label">Similar To : </label>';
			checkBoxCollectionStr += '<div class="col-lg-10">';
			
			for(var item in data){
				  checkBoxCollectionStr += '<label class="checkbox-inline">';
				  checkBoxCollectionStr += '<input type="checkbox" name="radio" checked="checked">'+data[item];
				  checkBoxCollectionStr += '</label>';		     
			};		    	
			checkBoxCollectionStr += '</div></div>';
		  
			 $("#similarToTestcaseDetails").html('');
			 if(data.length==0){
				 $("#similarToTestcaseDetails").html('<p style="text-align:center;">No data Available</p>');
			 }else{
				 $("#similarToTestcaseDetails").html(checkBoxCollectionStr);
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

//BEGIN: ConvertDataTable - TestSteps
var testStepDT_oTable='';
function loadTestStepViewDataTable(testcaseId){
	 url= 'testcase.teststep.list?testCaseId='+testcaseId+'&jtStartIndex=0&jtPageSize=10';
	 jsonObj={"Title":"Test Step",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"Test Step",
	};
	testStepDataTableContainer.init(jsonObj);
}

var testStepDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		assignTestStepDataTableValues(jsonObj, "TestStep");
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignTestStepDataTableValues(jsonObj, tType){
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
				testStepDT_Container(jsonObj);
			}else if(tType == "TestSuite"){
				testSuiteDT_Container(jsonObj);
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

function testStepDataTableHeader(){
	var childDivString ='<table id="testStep_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Test Step ID</th>'+
			'<th>Test Step Code</th>'+
			'<th>Test Step Name</th>'+
			'<th>Test Step Description</th>'+
			'<th>Test Step Input</th>'+
			'<th>Test Step Expected Output</th>'+
			'<th>Test Step Source</th>'+
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
function testStepDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForTestStep").children().length>0) {
			$("#dataTableContainerForTestStep").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = testStepDataTableHeader(); 			 
	$("#dataTableContainerForTestStep").append(childDivString);
	
	testStepDT_oTable = $("#testStep_dataTable").dataTable( {				 	
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
	     		  $('#testStep_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeTestStepDT();
			   },  
		   buttons: [
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
           { mData: "testStepId", className: 'disableEditInline', sWidth: '8%' },	
           { mData: "testStepCode", className: 'disableEditInline', sWidth: '8%' },	
           { mData: "testStepName", className: 'disableEditInline', sWidth: '18%' },
           { mData: "testStepDescription", className: 'disableEditInline', sWidth: '18%' },
           { mData: "testStepInput", className: 'disableEditInline', sWidth: '18%' },
           { mData: "testStepExpectedOutput", className: 'disableEditInline', sWidth: '18%' },
           { mData: "testStepSource", className: 'disableEditInline', sWidth: '12%' },
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorTestStep-active', row).prop( 'checked', data.readyState == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#testStep_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorTestStep.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#testStep_dataTable_length").css('margin-top','8px');
	$("#testStep_dataTable_length").css('padding-left','35px');		
	
	testStepDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutTestStepDT='';
function reInitializeTestStepDT(){
	clearTimeoutTestStepDT = setTimeout(function(){				
		testStepDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTestStepDT);
	},200);
}
//END: ConvertDataTable - TestSteps

//BEGIN: ConvertDataTable - TestSuite
var testSuiteDT_oTable='';
var editorTestSuite='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;

function loadTestSuitesViewDataTable(testcaseId){
	testSuiteURL = 'test.suite.byTestCase.list?testCaseId='+testcaseId;
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [	{id:"productVersionId", type:optionsType_ProductTestSuite, url:'common.list.productversion?productId='+productId},
	      			{id:"testScriptType", type:optionsType_ProductTestSuite, url:'common.list.scripttype'},
	      			{id:"executionTypeId", type:optionsType_ProductTestSuite, url:'common.list.executiontypemaster.byentityid?entitymasterid=7'}
	              ];
	testSuiteOptions_Container(optionsArr);
}
		
function testSuiteOptions_Container(urlArr){
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
				 testSuiteOptions_Container(optionsArr);
			 }else{
				var Options = [{ "value": "TAF Server", "Number": null, "label": "TAF Server"},
					{ "value": "HPQC_TEST_PLAN", "Number": null, "label": "HPQC_TEST_PLAN"},
					{ "value": "HPQC_TEST_RESOURCES", "Number": null, "label": "HPQC_TEST_RESOURCES"},
					{ "value": "JENKINS", "Number": null, "label": "JENKINS"},
					{ "value": "HUDSON", "Number": null, "label": "HUDSON"}
				];
				json.Options = Options;			
				optionsResultArr.push(json.Options);
				testSuiteDataTableInit();	
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

function testSuiteDataTableInit(){	
	 url= testSuiteURL +'&jtStartIndex=0&jtPageSize=10';
	 jsonObj={"Title":"Test Suite",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"Test Suite",
	};
	testSuiteDataTableContainer.init(jsonObj);
}

var testSuiteDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		assignTestStepDataTableValues(jsonObj, "TestSuite");
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function testSuiteDataTableHeader(){
	var childDivString ='<table id="testSuiteDetails_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Test Suite ID</th>'+
			'<th>Test Suite Code</th>'+
			'<th>Test Suite Name</th>'+
			'<th>Version</th>'+
			'<th>Script Type</th>'+
			'<th>Script Source</th>'+
			'<th>Script File Location</th>'+	
			'<th>Test Case Count</th>'+
			'<th>Test Steps Count</th>'+	
			'<th>Execution Type</th>'+
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

function testSuiteDT_Container(jsonObj){	
	try{
		if ($("#dataTableContainerForTestSuite").children().length>0) {
			$("#dataTableContainerForTestSuite").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = testSuiteDataTableHeader(); 			 
	$("#dataTableContainerForTestSuite").append(childDivString);
	
	editorTestSuite = new $.fn.dataTable.Editor( {
		"table": "#testSuiteDetails_dataTable",
		//ajax: "test.suite.add",
		//ajaxUrl: "test.suite.update",
		idSrc:  "testSuiteId",
		i18n: {
	        create: {
	            title:  "Create a new test suite",
	            submit: "Create",
	        }
	    },
		fields: [{								
			label:"productId",
			name:"productId",					
			type: 'hidden',				
			"def": productId
		},{								
			label:"testSuiteId",
			name:"testSuiteId",		
			type: 'hidden',					
		},{
            label:"Suite Code *",
			name:"testSuiteCode",									
        },{
            label:"Test Suite Name *",
			name:"testSuiteName",									
        },{
            label: "Version *",
            name: "productVersionListId", 
			options: optionsResultArr[0],
            "type"  : "select",	
        },{
            label: "Script Type",
            name: "testScriptType", 
			options: optionsResultArr[1],
            "type"  : "select",	
        },{
            label: "Script Source",
            name: "testScriptSource", 
			options: optionsResultArr[3],
            "type"  : "select",	
        },{
            label: "Script File Location",
            name: "testSuiteScriptFileLocation",                
        },{
            label: "Test Case Count",
            name: "testCaseCount",   
			type: 'hidden',					
        },{
            label: "Test Steps Count",
            name: "testStepsCount",   
			type: 'hidden',		
        },{
            label: "Execution Type",
            name: "executionTypeId", 
			options: optionsResultArr[2],
            "type"  : "select",	
        },  
    ]
	});	
	
	testSuiteDT_oTable = $("#testSuiteDetails_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		   // "sScrollX": "100%",
	       //"sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     		  $('#testSuiteDetails_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeTestSuiteDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Test Suite',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Test Suite',
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
			{ mData: "testSuiteId",className: 'disableEditInline', sWidth: '5%' },		
			{ mData: "testSuiteCode",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testSuiteName",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "productVersionListId", className: 'disableEditInline', sWidth: '10%',
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(editorTestSuite, 'productVersionListId', full.productVersionListId);						
					 }else if(type == "display"){
						data = full.productVersionListName;
					 }	           	 
					 return data;
				 },
			},	
			{ mData: "testScriptType",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testScriptSource",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "testSuiteScriptFileLocation",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testCaseCount",className: 'disableEditInline', sWidth: '5%' },		
			{ mData: "testStepsCount",className: 'disableEditInline', sWidth: '15%' },           
			{ mData: "executionTypeId", className: 'disableEditInline', sWidth: '10%',
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(editorTestSuite, 'executionTypeId', full.executionTypeId);						
					 }else if(type == "display"){
						data = optionsValueHandler(editorTestSuite, 'executionTypeId', full.executionTypeId);
					 }	           	 
					 return data;
				 },
			 },	
       ],       
       rowCallback: function ( row, data ) {
    	  // $('input.editorTestSuite-active', row).prop( 'checked', data.readyState == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#testSuiteDetails_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorTestSuite.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#testSuiteDetails_dataTable_length").css('margin-top','8px');
	$("#testSuiteDetails_dataTable_length").css('padding-left','35px');
	
	$("#testSuiteDetails_dataTable_filter").css('margin-right','6px');
	
	testSuiteDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutTestSuiteDT='';
function reInitializeTestSuiteDT(){
	clearTimeoutTestSuiteDT = setTimeout(function(){				
		testSuiteDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTestSuiteDT);
	},200);
}
//END: ConvertDataTable - TestSuite

function loadTestResults(data){		
	var result=data.Records;
	$("#testCaseDetailedViewsModal #preconditions").empty();
	$("#testCaseDetailedViewsModal #expectedOutput").empty();
	$("#testCaseDetailedViewsModal #testcaseInput").empty();
	$("#testCaseDetailedViewsModal #observedOutput").empty();
	$("#testCaseDetailedViewsModal #comments").empty();
	var selectedShift="";
	
	$('#testCaseDetailedViewsModal #actualshift_options').empty(); 
	$.each(result, function(i,item){ 
		if(item.preconditions==null)
			$("#testCaseDetailedViewsModal #preconditions").append("<div style='font-size:small;'  > NA </div>");
		else
			$("#testCaseDetailedViewsModal #preconditions").append("<div style='font-size:small;'  >"+item.preconditions+"</div>");
		if(item.expectedOutput==null){
			$("#testCaseDetailedViewsModal #expectedOutput").append("<div style='font-size:small;' >NA</div>");
			}
		else{
			exceptedOutput=item.expectedOutput;
			$("#testCaseDetailedViewsModal #expectedOutput").append("<div style='font-size:small;' >"+item.expectedOutput+"</div>");
		}
		if(item.testcaseInput==null)
			$("#testCaseDetailedViewsModal #testcaseInput").append("<div style='font-size:small;'  >NA</div>");
		else
			$("#testcaseInput").append("<div style='font-size:small;'  >"+item.testcaseInput+"</div>");

		if(item.observedOutput==null){
				$("#testCaseDetailedViewsModal #observedOutput").val("").attr("disabled",true);
		}
		else{
				$("#testCaseDetailedViewsModal #observedOutput").val(item.observedOutput).attr("disabled",true);
		}
		if(item.comments==null){
				$("#testCaseDetailedViewsModal #comments").val("").attr("disabled",true);
		}
		else{
				$("#testCaseDetailedViewsModal #comments").val(item.comments).attr("disabled",true);
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
			$("#testCaseDetailedViewsModal #blocked").removeClass("active").siblings("label").removeClass("active");
		}
		if(item.actualExecutionDate == null){
			isLoad = true;
			$("#testCaseDetailedViewsModal #datepickerAC").datepicker('setDate','today');
			currActExecDate = $("#datepickerAC").val();
		}else{
			isLoad = true;
			currActExecDate = setFormattedDate(item.actualExecutionDate);
			$("#testCaseDetailedViewsModal #datepickerAC").datepicker('setDate', setFormattedDate(item.actualExecutionDate));
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

function loadEvidenceDetails(){
	if(document.getElementById('tcerIdhidden')!=null)
		tcerId=document.getElementById('tcerIdhidden').value;
	else
		tcerId=0;
	
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
			/*	$("#evidenceUplaoded").append("<div style=font-size:small;> <a href=javascript:downloadEvidenceinTestCaseDetails('evidenceFilename"+i+"');>"+item.evidencename+"</a></div>"); */
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

// ----- related testcase ------
var clearTimeoutDTRelatedTestCase='';
var relatedTestcase_oTable=''
function reInitializeDTRelatedTestcase(){
	clearTimeoutDTRelatedTestCase = setTimeout(function(){				
		relatedTestcase_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTRelatedTestCase);
	},200);
}

var clearTimeoutDTRelatedTestScript='';
var relatedTestScript_oTable=''
function reInitializeDTRelatedTestScript(){
	clearTimeoutDTRelatedTestScript = setTimeout(function(){				
		relatedTestScript_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTRelatedTestScript);
	},200);
}

function relatedProductTestcase(data){
	try{
		if ($("#relatedFeaturesDetailsChild1").children().length>0) {
			$("#relatedFeaturesDetailsChild1").children().remove();
		}
	} 
	catch(e) {}
	
    	var emptytr = emptyTableRowAppending(7);  // total coulmn count				  
	  var childDivString = '<table id="relatedTestCase_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#relatedFeaturesDetailsChild1").append(childDivString); 						  
	  
	  $("#relatedTestCase_dataTable thead").html('');
	  $("#relatedTestCase_dataTable thead").append(productTestCaseChild2Header());
	  
	  $("#relatedTestCase_dataTable tfoot tr").html('');     			  
	  $("#relatedTestCase_dataTable tfoot tr").append(emptytr);
	  				
	relatedTestcase_oTable = $("#relatedTestCase_dataTable").dataTable( {
			dom: "Bfrtilp",
		 	paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bSort": false,	       
	       "bScrollCollapse": true,	
			select: true,		   
	       "fnInitComplete": function(data) {
			   
			  var searchcolumnVisibleIndex = [6]; // search column TextBox Invisible Column position
	     	  var headerItems = $('#relatedTestCase_dataTable_wrapper tfoot tr th');
	     	  headerItems.each( function () {   
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
	   	    	    	$(this).prepend( '<input type="text" name="'+data.aoColumns[i].mData+'" value="" style="width:100%" class="search_init" />');
	   	    	    }
	     	   });
			   
			   reInitializeDTRelatedTestcase();			   
		   },  
		   select: true,
		   buttons: [					 
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "TestCases",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "TestCases",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "TestCases",
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
           { mData: "productFeatureId", sWidth: '5%', "render": function (tcData,type,full) {	        
		 		var featureId = full.productFeatureId;	
		 		var featureName = full.productFeatureName;
		 		return ('<a onclick="displayProductFeatureDetails('+featureId+',\''+featureName+'\')">'+featureId+'</a>');		        
	    	},
		   },
           { mData: "productFeatureCode",className: 'disableEditInline', sWidth: '20%' },           
		   { mData: "productFeatureName",className: 'disableEditInline', sWidth: '20%' },           
		   { mData: "displayName",className: 'disableEditInline', sWidth: '20%' },           
		   { mData: "productFeatureDescription",className: 'disableEditInline', sWidth: '10%' },           			
		   { mData: "parentFeatureId", sWidth: '5%', "render": function (tcData,type,full) {	        
		 		var parentFeatureId = full.parentFeatureId;	
		 		var parentFeatureName = full.parentFeatureName;
		 		return ('<a onclick="displayProductFeatureDetails('+parentFeatureId+',\''+parentFeatureName+'\')">'+parentFeatureId+'</a>');		        
	    	},
		   },
		   { mData: "parentFeatureStatus", className: 'disableEditInline', sWidth: '10%',
        	   mRender: function (data, type, full) {
					if ( type === 'display' ) {
						return '<input type="checkbox" class="editorRelatedFeatures-active">';
					}
					return data;
        	   },
        	   className: "dt-body-center",
		   },            	
       ],
       rowCallback: function ( row, data ) {
    	   $('input.editorRelatedFeatures-active', row).prop( 'checked', data.parentFeatureStatus == 1 );
       },
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 		 		 
		 $("#relatedTestCase_dataTable_length").css('margin-top','8px');
		 $("#relatedTestCase_dataTable_length").css('padding-left','35px');	
		 
		 $('#relatedTestCase_dataTable').DataTable().columns().every( function () {
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
	// ------	
}

function displayProductFeatureDetails(featureId,featureName){
	var jsonObj={"Title":"Feature Details :- "+ featureId + "- "+featureName,
		 "featureId":featureId,
		 "featureName":featureName,
		 "featureDetailsTitle":"Feature Details",
		 "containerId":"relatedTestcaseDetails",
		 "featureOverAllDetailsUrl":"product.feature.overall.details?featureId="+featureId,
	};
	FeatureOverallViewDetails.init(jsonObj);	
}

// ----- related TestScript -----
function relatedProductTestScript(data){	
	try{
	if ($("#relatedFeaturesDetailsChild2").children().length>0) {
			$("#relatedFeaturesDetailsChild2").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = '<table id="relatedTestScript_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	$("#relatedFeaturesDetailsChild2").append(childDivString);
	
	var emptytr = emptyTableRowAppending(8);  // total coulmn count			
	$("#relatedTestScript_dataTable thead").html('');
	$("#relatedTestScript_dataTable thead").append(treeTestScript_Header());
	  
	$("#relatedTestScript_dataTable tfoot tr").html('');     			  
	$("#relatedTestScript_dataTable tfoot tr").append(emptytr);
	
	relatedTestScript_oTable = $("#relatedTestScript_dataTable").dataTable( {
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
			  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
     		  $("#relatedTestScript_dataTable_wrapper tfoot tr th").each( function () {
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
			   reInitializeDTRelatedTestScript();			   
		   },  
		   select: true,
		   buttons: [	             		  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "TestScript",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "TestScript",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "TestScript",
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
           { mData: "scriptId",className: 'disableEditInline', sWidth: '3%' },		
           { mData: "scriptName",className: 'disableEditInline', sWidth: '3%' },
           { mData: "scriptQualifiedName",className: 'disableEditInline', sWidth: '30%' }, 
			{ mData: "language",className: 'disableEditInline', sWidth: '10%' },		
           { mData: "source",className: 'disableEditInline', sWidth: '10%' },
           { mData: "scriptVersion",className: 'disableEditInline', sWidth: '10%' },
		   { mData: "revision",className: 'disableEditInline', sWidth: '10%' },		
           { mData: "uri",className: 'disableEditInline', sWidth: '10%' },          		   
		  
       ], 			   
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 		 
		 $("#treeTestScript_dataTable_length").css('margin-top','8px');
		 $("#treeTestScript_dataTable_length").css('padding-left','35px');
			
		relatedTestScript_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
		} );		
	});
}
