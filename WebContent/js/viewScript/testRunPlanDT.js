var TestRunPlanDT = function(){	
	var initialise = function(jsonObj){
		initialiseTestRunPlanDT(jsonObj);
	};	
		return {
			init : function(jsonObj){
				initialise(jsonObj);
			}
		};
}();

function initialiseTestRunPlanDT(jsonObj){
    if(pageType == "TESTRUNPLANVIEW"){
    	testPlanDataTable("ParentTable",jsonObj.url);
    }else if(pageType == "PRODUCTMANAGEMENTPLANVIEW"){
    	testPlanDataTable("ParentTable",jsonObj.url);
    }    
}

//BEGIN: ConvertDataTable - TestRunPlanDT
var testPlanDT_oTable='';
var editorTestPlan='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;
var tableType;
//Add new variable for Test Run Plan mode
var trpExecMode;
var tpcId;
var pdtId;
var testConfigurationURL;
var tpURL;
var rnCnfgId,currRnCfgId;
var useIntlTp;
var rnCnfgFlag=false;
var tScriptLevel;
var isReady,isReadyMessage;
var alertWidthFlag=false;
function testPlanDataTable(tableType,urlToGetTestRunPlanOfSpecifiedProductId){
	tpURL = urlToGetTestRunPlanOfSpecifiedProductId;
	optionsItemCounter=0;
	optionsResultArr=[];
	if(tableType == "ParentTable"){
		optionsArr = [{id:"pdtVersionList", url:'common.list.productbuild?productId='+productId+'&productVersionListId='+productVersionId},
		              {id:"testToolList", url:'common.list.testToolMaster.option'},
		              {id:"testScriptTypeList", url:'common.list.scripttype'},
		              {id:"testScriptLevelList", url:'common.multiple.testScriptsLevel.options'},
		              {id:"testSuiteOptionsList", url:'common.multiple.testSuites.options'},
		              {id:"automationModeList", url:'common.automationmode.options'},
		              {id:"executionType", url:'common.list.executiontypemaster.byentityid?entitymasterid=7'}];
	}else if(tableType == "TestConfigurationTable"){
		optionsArr = [{id:"pdtTypeList", url:'common.list.productType'},
		              {id:"envList", url:'common.environment.list?productMasterId='+productId+'&envstatus=1'},
		              {id:"genericList", url:'common.list.product.mapped.genericdevice?productId='+productId},
		              {id:"hostList", url:'common.list.product.mapped.host?productId='+productId},
		              {id:"testToolList", url:'common.list.testToolMaster.option'},
		              {id:"scriptTypeList", url:'common.list.scripttype'},
		              {id:"copyTestPlanTestSuiteList", url:'common.copy.testsuite.from.testplan.options'}];
	}else if(tableType == "TPConfigurationPropertiesTable"){
		optionsArr = [{id:"entityMasterList", url:'common.listEntityConfigPropsMasterByentityConfigPropsMasterId?entityConfigPropertiesMasterId=1&entityMasterId=9'}];
	}
	testPlanOptions_Container(optionsArr, tableType);
}
		
function testPlanOptions_Container(urlArr, tableType){
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
				 testPlanOptions_Container(optionsArr,tableType);
			 }else{
				 testPlanDataTableInit(tableType);
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

function testPlanDataTableInit(tableType){
	var url,jsonObj={};
	if(tableType=="ParentTable"){
		url= tpURL +'&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Test Plan",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Test Plan",
		};
	}else if(tableType == "TestConfigurationTable"){
		url= testConfigurationURL +'&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Test Configuration",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Test Configuration",
		};		
	}else if(tableType == "TPConfigurationPropertiesTable"){
		url= tpURL +'&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Test Configuration Properties",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Test Configuration Properties",
		};		
	}
	testPlanDataTableContainer.init(jsonObj);
}

var testPlanDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		if(jsonObj.componentUsageTitle == "Test Plan"){
 			assignTestPlanDataTableValues(jsonObj, "ParentTable");
 		}else if(jsonObj.componentUsageTitle == "Test Configuration"){
 			assignTestPlanDataTableValues(jsonObj, "TestConfigurationTable");
 		}else if(jsonObj.componentUsageTitle == "Test Configuration Properties"){
 			assignTestPlanDataTableValues(jsonObj, "TPConfigurationPropertiesTable");
 		}
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignTestPlanDataTableValues(jsonObj, tableType){
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
			if(tableType == "ParentTable"){
				testPlanDT_Container(jsonObj);
			}else if(tableType == "WorkpackageTable"){
				workpackageDT_Container(jsonObj);
			}else if(tableType == "TPConfigurationPropertiesTable"){
				tpConfigurationPropertiesDT_Container(jsonObj);
			}else if(tableType == "TestConfigurationTable"){
				testConfigurationDT_Container(jsonObj);
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

function testPlanDataTableHeader(){
	var childDivString ='<table id="testPlan_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Product</th>'+
			'<th>Version</th>'+
			'<th>Build Name</th>'+
			'<th>ID</th>'+
			'<th>Name</th>'+
			'<th>Description</th>'+
			'<th>Test Tool</th>'+
			'<th>Script Type</th>'+
			'<th>Test Script Source</th>'+
			'<th>Script File Location</th>'+
			'<th>Multiple Test Suites</th>'+
			'<th>Automation Mode</th>'+
			'<th>Optimize Test Plan</th>'+
			'<th>Test Script Level</th>'+
			'<th>Test Configuration(s)</th>'+
			'<th>TC Executions[Actual]</th>'+
			'<th>Average Execution Time</th>'+
			'<th>Cron Expression</th>'+
			'<th>Mail</th>'+
			'<th></th>'+
			'<th>Execution Type</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}

function testPlanDT_Container(jsonObj){
	
	try{
		if ($("#jTableContainertestrun").children().length>0) {
			$("#jTableContainertestrun").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = testPlanDataTableHeader(); 			 
	$("#jTableContainertestrun").append(childDivString);
	
	editorTestPlan = new $.fn.dataTable.Editor( {
	    "table": "#testPlan_dataTable",
		//ajax: "workflow.master.add",
		ajaxUrl: "testrunplan.build.update",
		idSrc:  "testRunPlanId",
		i18n: {
	        create: {
	            title:  "Create a new Test Plan",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "Product",
            name: "productName",
        },{
            label: "Version",
            name: "productVersionName",
        },{
            label: "Build Name",
            name: "productBuildId",
            options: optionsResultArr[0],
            "type":"select"
        },{
            label: "Created Date",
            name: "createdDate",
            type:   'datetime',
            format: 'M/D/YYYY',
           // "type":"hidden",
        },{
            label: "ID",
            name: "testRunPlanId",
        },{
            label: "Name",
            name: "testRunPlanName",
        },{
            label: "Description",
            name: "description",
        },{
            label: "Test Tool",
            name: "testToolMasterId",
            options: optionsResultArr[1],
            "type":"select"
        },{
            label: "Script Type",
            name: "testScriptType",
            options: optionsResultArr[2],
            "type":"select"
        },{
            label: "Test Script Source",
            name: "testScriptSource",
        },{
            label: "Script File Location",
            name: "testSuiteScriptFileLocation",
        },{
            label: "Test Script Level",
            name: "testScriptsLevel",
            options: optionsResultArr[3],
            "type":"select"
        },{
            label: "Multiple Test Suites",
            name: "multipleTestSuites",
            options: optionsResultArr[4],
            "type":"select"
        },{
            label: "Automation Mode",
            name: "automationMode",
            options: optionsResultArr[5],
            "type":"select"
        },{
            label: "Optimize Test Plan",
            name: "useIntelligentTestPlan",
            options: optionsResultArr[4],
            "type":"select"
        },{
            label: "Test Configuration Count",
            name: "runConfigurationCount",
        },{
            label: "TC Executions[Actual]",
            name: "totalTestCases",
        },{
            label: "Average Execution Time",
            name: "avgExecutionTime",
        },{
            label: "Mail",
            name: "notifyByMail",
        },{
            label: "Cron Expression",
            name: "testRunCronSchedule",
        },{
            label: "Execution Type",
            name: "executionTypeId",
            options: optionsResultArr[6],
            "type":"select"
        }        
    ]
	});
	
	testPlanDT_oTable = $("#testPlan_dataTable").dataTable( {				 	
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
	    	   	  if(isEngagementLevelFlag){
	  		    	 searchcolumnVisibleIndex = [18,20,21,22,23,24,25,26,27,28,29,30]; // search column TextBox Invisible Column position
	    	   	  }else {
	  		    	 searchcolumnVisibleIndex = [19,21,22,23,24,25,26,27,28,29,30]; // search column TextBox Invisible Column position
	    	   	  }
	     		  $('#testPlan_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeTestPlanDT();
			   },  
		   buttons: [
						{ 
							extend: "create",
							editor: editorTestPlan,
							action: function ( e, dt, node, config ) {
								productMgmtShowTestRunplanFormNew();
			                }
						},	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Test Plan',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Test Plan',
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
           { mData: "productName", className: 'disableEditInline', sWidth: '15%' },	
           { mData: "productVersionName", className: 'disableEditInline', sWidth: '15%' },
           { mData: "productBuildId", className: 'editable', sWidth: '10%', editField: "productBuildId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorTestPlan, 'productBuildId', full.productBuildId); 		           	 
		             return data;
	            },
			},
           { mData: "testRunPlanId", className: 'disableEditInline', sWidth: '12%' },		
           { mData: "testRunPlanName", className: 'disableEditInline', sWidth: '12%',
        	   mRender: function (data, type, full) {
        		   	data = '<a href="' + data + '" class="tPlanCallHandler">' + data + '</a>'; 		           	 
		            return data;
	            },
           },
           { mData: "description", className: 'editable', sWidth: '12%' },
           { mData: "testToolMasterId", className: 'editable', sWidth: '10%', editField: "testToolMasterId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorTestPlan, 'testToolMasterId', full.testToolMasterId); 		           	 
		             return data;
	            },
			},
			{ mData: "testScriptType", className: 'editable', sWidth: '12%', editField: "testScriptType",
	       		mRender: function (data, type, full) {
	       			if(type == "display"){
		           		data = full.testScriptType;
		           	}	           	 
		            return data;
	            },
			},
           { mData: "testScriptSource", className: 'editable', sWidth: '12%' },
           { mData: "testSuiteScriptFileLocation", className: 'editable', sWidth: '12%' },
           { mData: "multipleTestSuites", className: 'editable', sWidth: '10%', editField: "multipleTestSuites",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorTestPlan, 'multipleTestSuites', full.multipleTestSuites); 		           	 
		             return data;
	            },
			},
			{ mData: "automationMode", className: 'editable', sWidth: '12%', editField: "automationMode",
	       		mRender: function (data, type, full) {
	       			if(type == "display"){
		           		data = full.automationMode;
		           	}	           	 
		            return data;
	            },
			},
			{ mData: "useIntelligentTestPlan", className: 'editable', sWidth: '10%', editField: "useIntelligentTestPlan",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorTestPlan, 'useIntelligentTestPlan', full.useIntelligentTestPlan); 		           	 
		             return data;
	            },
			},
           { mData: "testScriptsLevel", className: 'editable', sWidth: '25%', editField: "testScriptsLevel",
	       		mRender: function (data, type, full) {
	       			if(type == "display"){
		           		data = full.testScriptsLevel;
		           	}	           	 
		            return data;
	            },
			},
           { mData: "runConfigurationCount", className: 'disableEditInline', sWidth: '12%' },
           { mData: "totalTestCases", className: 'disableEditInline', sWidth: '12%' },
           { mData: "avgExecutionTime", className: 'disableEditInline', sWidth: '12%' },
           { mData: "testRunCronSchedule", className: 'editable', sWidth: '12%' },
           { mData: "notifyByMail", className: 'editable', sWidth: '12%' },
           { mData: null,				 
           	bSortable: false,
           	mRender: function(data, type, full) {				            	
          		 var img = ('<div style="display: flex;">'+
	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
	       					'<i class="fa fa-clock-o executionScheduleImg" title="Schedule"></i></button>'+
    	       		'</div>');	      		
          		 return img;
           	}
           },
            { mData: "executionTypeId", className: 'editable', sWidth: '12%', editField: "executionTypeId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorTestPlan, 'executionTypeId', full.executionTypeId); 		           	 
		             return data;
	            },
			},
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       				'<img src="css/images/execute_metro.png" id='+full.testRunPlanId+"~"+full.testRunPlanName+' class="executeImg" title="Execute Test Plan" />'+
     	       		'</div>');	      		
           		 return img;
            	}
            },
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       				'<img src="css/images/list_metro.png" class="workpackageImg" title="Workpackage" />'+
     	       		'</div>');	      		
           		 return img;
            	}
            },
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       				'<img src="css/images/list_metro.png" class="testPlanConfigureImg" title="Properties" />'+
     	       		'</div>');	      		
           		 return img;
            	}
            },
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       				'<img src="css/images/list_metro.png" class="runConfigurationImg" title="Test Configuration" />'+
     	       		'</div>');	      		
           		 return img;
            	}
            },
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       				'<img src="css/images/clock.png" class="schedulesImg" title="Schedules" />'+
     	       		'</div>');	      		
           		 return img;
            	}
            },
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<i class="fa fa-search-plus auditHistoryImg" title="Audit History"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<i class="fa fa-comments commentsImg" title="Comments"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       				'<img src="css/images/mapping.png" class="mappingTestDataImg" title="Map Test Data" />'+
     	       		'</div>');	      		
           		 return img;
            	}
            },
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       				'<img src="css/images/analytics.jpg" class="intelligentTestPlanImg" title="Intelligent Test Plan" />'+
     	       		'</div>');	      		
           		 return img;
            	}
            }
       ],
       columnDefs: [							
         { targets: 2, visible: !isEngagementLevelFlag }							 
       ],
       rowCallback: function ( row, data ) {
    	   $('input.editorTestPlan-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#testPlan_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorTestPlan.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#testPlan_dataTable tbody').on('click', 'td .executionScheduleImg', function () {
		var tr = $(this).closest('tr');
    	var row = testPlanDT_oTable.DataTable().row(tr);
    	var titleSchedule = "<"+row.data().testRunPlanName+"> "+"Test Plan : Execution Schedule";
    	var subTitleSchedule = "Product "+$(headerTitle).text();	           		
       	var trpId = row.data().testRunPlanId;
       	var trp = "TestRunPlan";
 		scheduleUsingCronGen(titleSchedule, subTitleSchedule, trpId, trp);
	});
	
	$('#testPlan_dataTable tbody').on('click', 'td .workpackageImg', function () {
		var tr = $(this).closest('tr');
    	var row = testPlanDT_oTable.DataTable().row(tr);
       	var testRunPlanId = row.data().testRunPlanId;
    	var url = 'administration.workPackage.list.bytestRunPlanId?testRunPlanId='+ testRunPlanId +'&jtStartIndex=0&jtPageSize=10';
    	jsonObj={"Title":"Workpackage",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Workpackage",
		};
		assignTestPlanDataTableValues(jsonObj, "WorkpackageTable");
       	$('#workpackageDT_Child_Container').modal();
	});
	
	$('#testPlan_dataTable tbody').on('click', 'td .testPlanConfigureImg', function () {
		var tr = $(this).closest('tr');
    	var row = testPlanDT_oTable.DataTable().row(tr);
       	tpcId = row.data().testRunPlanId;
       	rnCnfgFlag = false;
       	var url = 'entityConfigureProperties.list?entityId='+ tpcId +'&entityMasterId=9&jtStartIndex=0&jtPageSize=10';
       	testPlanDataTable("TPConfigurationPropertiesTable",url);
       	$('#testPlanConfigureDT_Child_Container').modal();
	});
	
	$('#testPlan_dataTable tbody').on('click', 'td .runConfigurationImg', function () {
		var tr = $(this).closest('tr');
    	var row = testPlanDT_oTable.DataTable().row(tr);
       	tpcId = row.data().testRunPlanId;
       	pdtId = row.data().productId;
       	rnCnfgFlag = true;
       	$('#runConfigurationDT_Child_Container').modal();
       	$(document).off('focusin.modal');
       	testConfigurationURL = 'runConfiguration.listbyTestRunPlan?testRunPlanId='+tpcId;
       	testPlanDataTable("TestConfigurationTable",testConfigurationURL);
	});
	
	$('#testPlan_dataTable tbody').on('click', 'td .executeImg', function () {
		var tr = $(this).closest('tr');
    	var row = testPlanDT_oTable.DataTable().row(tr);
		var testrunPlanId=row.data().testRunPlanId;
		tpcId = testrunPlanId;
		tScriptLevel = row.data().testScriptsLevel;
		var testRunPlanName=row.data().testRunPlanName;
		useIntlTp = row.data().useIntelligentTestPlan;
		document.getElementById("hdnTestRunPlanName").value=testRunPlanName;
		document.getElementById("hdnTestRunPlanId").value=testrunPlanId;
		document.getElementById("hdnTestRunPlanDeviceId").value=testrunPlanId;
		testCaseExecution = '';
		trpExecMode = row.data().automationMode;
		var url = 'testplan.readiness.check?testPlanId='+testrunPlanId;
		 $.ajax({
			 type: "POST",
			 url: url,
			 success: function(data) {  
				 if(data.Result == "ERROR"){
					 callAlert(data.Message);
					 return false;
				 }else if(data.Result == "OK"){
					 if(data.Message.toUpperCase() != 'YES') {
						 alertWidthFlag = true;
						 callAlert(data.exportFileName);
						 return false;
				     }					 
					 if(trpExecMode == 'Attended'){
			        	if(useIntlTp.toString() == "0"){
			        		$('#recommendedHeader').hide();
			        		$('#jTableContainerTestCaseForTestRunPlan').css('margin-top','20px');
			        	}else{
			        		$('#jTableContainerTestCaseForTestRunPlan').css('margin-top','0px');
			        		$('#recommendedHeader').show();
			        	}
						$('#div_PopupRunConfigurationList').modal();
						testCasebyTestRunPlanId();
						testRunPlanTestBeds();
					 } else {
						testRunPlanId = testrunPlanId;	
						unattendedCallMode(testRunPlanId);
					 } 
				 }
			 }    
		 });			
	});
	
	$('#testPlan_dataTable tbody').on('click', 'td .schedulesImg', function () {
		var tr = $(this).closest('tr');
    	var row = testPlanDT_oTable.DataTable().row(tr);
		var testRunPlanName=row.data().testRunPlanName;
		initialiseTestRunItems(testRunPlanName,'administration.testrunplan.listbyproductversion?productversionId=218&productId=287');
	});
	
	$('#testPlan_dataTable tbody').on('click', 'td .auditHistoryImg', function () {
		var tr = $(this).closest('tr');
    	var row = testPlanDT_oTable.DataTable().row(tr);
    	listGenericAuditHistory(row.data().testRunPlanId,"TestRunPlan","testRunPlanAudit");
	});
	
	$('#testPlan_dataTable tbody').on('click', 'td .commentsImg', function () {
		var tr = $(this).closest('tr');
    	var row = testPlanDT_oTable.DataTable().row(tr);
		var entityTypeIdComments = 9;
		var entityNameComments = "Test Plan";
		listComments(entityTypeIdComments, entityNameComments, row.data().testRunPlanId, row.data().testRunPlanName, "trpComments");
	});
	
	$('#testPlan_dataTable tbody').on('click', 'td .mappingTestDataImg', function () {
		var tr = $(this).closest('tr');
    	var row = testPlanDT_oTable.DataTable().row(tr);
    	var leftUrl="attachment.count.for.product?productId="+productId+"&productVersionId="+productVersionId+"&testRunPlanId="+row.data().testRunPlanId;							
		var rightUrl = "";
		var leftDefaultUrl="attachment.list.for.product?productId="+productId+"&productVersionId="+productVersionId+"&testRunPlanId="+row.data().testRunPlanId+"&jtStartIndex=0&jtPageSize=50";
		var rightDefaultUrl = "attachment.mapped.for.testrunplan?productId="+productId+"&productVersionId="+productVersionId+"&testRunPlanId="+row.data().testRunPlanId;
		var leftDragUrl = "testrunplan.attachment.mapping?testRunPlanId="+row.data().testRunPlanId;
		var rightDragtUrl = "testrunplan.attachment.mapping?testRunPlanId="+row.data().testRunPlanId;
		var leftPaginationUrl = '';
		var rightPaginationUrl="";									
		jsonTestDataObj={"Title":"Map Repositories to Test Plan",
				"leftDragItemsHeaderName":"Available Repositories to Test Plan",
				"rightDragItemsHeaderName":"Mapped Repositories to Test Plan",
				"leftDragItemsTotalUrl":leftUrl,
				"rightDragItemsTotalUrl":rightUrl,
				"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
				"rightDragItemsDefaultLoadingUrl":rightDefaultUrl,
				"leftDragItemUrl":leftDragUrl,
				"rightDragItemUrl":rightDragtUrl,
				"leftItemPaginationUrl":leftPaginationUrl,
				"rightItemPaginationUrl":rightPaginationUrl,									
				"leftDragItemsPageSize":"50",
				"rightDragItemsPageSize":"50",
				"noItems":"No test data / object repository attachments to show",	
				"componentUsageTitle":"testRunPlanAttachments",											
				};
		
		DragDropListItems.init(jsonTestDataObj);
	});
	
	$('#testPlan_dataTable tbody').on('click', 'td .intelligentTestPlanImg', function () {
		var tr = $(this).closest('tr');
    	var row = testPlanDT_oTable.DataTable().row(tr);
		getPlanHandlerTestRunPlanTab(row.data().productId, row.data().productName, row.data().testRunPlanName, row.data().productBuildId, row.data().productBuildName);
	});
	
	$('#testPlan_dataTable tbody').on('click', 'td .tPlanCallHandler', function () {
		var tr = $(this).closest('tr');
    	var row = testPlanDT_oTable.DataTable().row(tr);
    	pdtId = row.data().productId;
    	productId=pdtId;
    	tpcId = row.data().testRunPlanId;
    	tScriptLevel = row.data().testScriptsLevel;
    	event.preventDefault();
    	callConfirmTestRunPlan(row.data());
    	return false;
	});	
	
	$('#testPlan_dataTable').on( 'change', 'input.editorTestPlan-active', function () {
		editorTestPlan
            .edit( $(this).closest('tr'), false )
            .set( 'readyState', $(this).prop( 'checked' ) ? 1 : 0 )
            .submit();
	});
	
	$("#testPlan_dataTable_length").css('margin-top','8px');
	$("#testPlan_dataTable_length").css('padding-left','35px');		
	
	testPlanDT_oTable.DataTable().columns().every( function () {
	    var that = this;
	    $('input', this.footer() ).on( 'keyup change', function () {
	        if ( that.search() !== this.value ) {
	            that
	            	.search( this.value, true, false )
	                .draw();
	        }
	    } );
	} );
	
	var table = $('#testPlan_dataTable').DataTable();
	var buttons = table.buttons( ['.buttons-create'] );
	 
	if (isEngagementLevelFlag) {
	    buttons.disable();
	}
	else {
	    buttons.enable();
	}

}

var clearTimeoutTestPlanDT='';
function reInitializeTestPlanDT(){
	clearTimeoutTestPlanDT = setTimeout(function(){				
		testPlanDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTestPlanDT);
	},200);
}

function fullScreenHandlerDTTestPlan(){	
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
		$('#testFacMode').css('max-height',displaytestFaceModeResponsive(window.innerWidth));		
		testPlanDTFullScreenHandler(true);
		reInitializeTestPlanDT();
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
		$('#testFacMode').css('max-height','');		
		reInitializeTestPlanDT();				
		testPlanDTFullScreenHandler(false);			
	}
}

function testPlanDTFullScreenHandler(flag){
	if(flag){
		reInitializeTestPlanDT();
		$("#testPlan_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
	}else{
		reInitializeTestPlanDT();
		$("#testPlan_dataTable_wrapper .dataTables_scrollBody").css('max-height','450px');
	}
}

function displaytestFaceModeResponsive(widthValue){
	var resultWidth="";
	if(widthValue<768){
		resultWidth = 200;			
	}else if(widthValue<992){
		resultWidth = 300;
	}else if(widthValue<1200){
		resultWidth = 400;
	}else if(widthValue<1500){
		resultWidth = 500;			
	}else if(widthValue<1600){
		resultWidth = 600;
	}else if(widthValue<1800){
		resultWidth = 700;
	}else if(widthValue<2050){
		resultWidth = 750;
	}else if(widthValue<2400){
		resultWidth = 850;
	}else if(widthValue<3000){
		resultWidth = 1100;
	}else if(widthValue<4000){
		resultWidth = 1300;
	}else if(widthValue<5000){
		resultWidth = 1500;
	}
	
	return resultWidth+'px';
}
//END: ConvertDataTable - TestRunPlanDT

function hideAllTabs(){
	$('#Products').children().hide();
	$('#ToolIntegration').children().hide();
	$('#Testcases').children().hide();
	$('#Testsuites').children().hide();
	$('#Features').children().hide();
	$('#FeatureTestCases').children().hide();
	$('#Decoupling').children().hide();
	$('#DecouplingPlan').children().hide();
	$('#Environment').children().hide();	
	$('#Devices').children().hide();
	$('#TestRunPlanTab').children().hide();
	$('#VersionTestCaseMapping').children().hide();
	$('#ProductTeamResources').children().hide();
	$('#CoreResources').children().hide();
}

function scheduleUsingCronGen(title, subTitle, testRunPlanID, rowType){	
	var	jsonTestCaseTabObj={"Title": title,	
							"SubTitle": subTitle,
							"rowID": testRunPlanID,
							"rowType": rowType,
	};	
	Scheduling.init(jsonTestCaseTabObj);	
}

function testRunPlanForBDD(trpId,event){
	 testRunPlanId = trpId;
	 if(testCaseExecution == 'TestCaseExecution' ){
		 var readyURL = 'testplan.readiness.check?testPlanId='+testRunPlanId;
		 $.ajax({
			 type: "POST",
			 url: readyURL,
			 success: function(data) {  
				 if(data.Result == "ERROR"){
					 callAlert(data.Message);
					 return false;
				 }else if(data.Result == "OK"){
					 if(data.Message.toUpperCase() != 'YES') {
						 alertWidthFlag = true;
						 callAlert(data.exportFileName);
						 closeLoaderIcon();
						 return false;
				     }else{
				    	 var url = 'workpackage.execute.singletestcase';
						 executeTestCase(url, 'testCase',runConfigCheckBoxArrVal.toString());
				     }
				 }				
			 }    
		 });	
	 }else if(testCaseExecution == 'TestSuiteExecution'){
		 runConfigurationSubmit(1);
	 }else if(executeEditTrplan == 'ExecuteEditTestRunPlan'){
		 runConfig(2);
	 }else{
		 runConfig(event);
	 }
}

function runConfigurationSubmit(bool){
	if(bool == 0) {
		$("#div_PopupRunConfigurationList").modal("hide");
		return false;
	}
	var checkboxValues = [];
	if(testCaseExecution == 'TestSuiteExecution'){
		checkboxValues = runConfigCheckBoxArrVal ;
		document.getElementById("hdnTestRunPlanDeviceId").value = testrunPlanIdForTestCaseExecution;
	}else{
		$("#div_PopupRunConfigurationList .selectAllCheckboxes:input:checked").each(function(){ 
			checkboxValues.push($(this).attr('id'));
		});
	}
	console.log("checkboxValues :"+checkboxValues);
	
	var testConfigurationData={};
	var testConfigurationsArr=[];
	for(var k=0;k<checkboxValues.length;k++){
		for(var i=2;i<testBedCols.length;i++){
			if(checkboxValues[k]==testBedCols[i].id){
				var tcString = '{"testConfigurationId":"'+testBedCols[i].id+'","device":"'+testBedCols[i].title.split('/>')[1]+'","testcaseIds":"';
				var tcaseString = "";
				for(var j=0;j<testBedColumnData.length;j++){
					//if(testBedColumnData[j][i].startsWith("NA")){
					if($(testRunPlanTestBedRequest_oTable.fnGetNodes()).eq(j).find('td').eq(i).find('option:selected').val().toLowerCase()=="yes"){	
						tcaseString += testBedColumnData[j][1].substring(testBedColumnData[j][1].indexOf('[')+1,testBedColumnData[j][1].indexOf(']'))+",";				
					}
				}
				tcString += tcaseString.substring(0,tcaseString.lastIndexOf(','))+'$a}';
				testConfigurationsArr.push(tcString);
				break;
			}
		}
	}

	testConfigurationData['"testPlanId"']='\"'+testRunPlanId+'\"';
	testConfigurationData['"productId"']='\"'+productId+'\"';
	testConfigurationData['"testConfigurations"']=testConfigurationsArr;
	
	var loginUser =	$('#userdisplayname').text().split('[')[0].trim().toLowerCase();
	testConfigurationData['"user"']='\"'+loginUser+'\"';
	
	var productName= document.getElementById("treeHdnCurrentProductName").value ;
	if(pageType=="HOMEPAGE")title='';
	var productVersionName=title ;
	var month=0;
	var date = new Date();
	if((date.getMonth()<=9)){
		month="0"+(date.getMonth()+1);
	}else{
		month=date.getMonth()+1;
	}
	testrunPlanNameDevice='';
	var timestamp = date.getFullYear()+"-"+month+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	var workpackageName=productName+'-'+productVersionName+'-'+testrunPlanNameDevice+'-'+timestamp;
	var description=workpackageName + " is created";
	document.getElementById("hdnTestRunPlanId").value=document.getElementById("hdnTestRunPlanDeviceId").value;
	document.getElementById("wpkg_name").value=workpackageName;
	document.getElementById("wpkg_desc").value=description;	
	bootbox.confirm("Do you want to execute the Test Run now?", function(result){
		if(result){			
			if(executeEditTrplan == 'ExecuteEditTestRunPlan'){
				saveWorkpackageDetail(1);
			}else{				
				saveWorkpackageDetailDevice(checkboxValues.toString(),testConfigurationData);
			}
			executeEditTrplan ='';
			testCaseExecution = '';
		}	
	});
}

function runConfig(event) {	
	var childDivId;
	var mainDivId;
	if(event != null){
		if(event == 2){
			executeEditTrplan = 'ExecuteEditTestRunPlan';
			document.getElementById("hdnTestRunPlanId").value  = trpId;
			testrunPlanIdDevice = trpId;
		}else{
			testrunPlanIdDevice=event.target.id.split('~')[0];
			testrunPlanNameDevice = event.target.id.split('~')[1];
			document.getElementById("hdnTestRunPlanDeviceId").value=testrunPlanIdDevice;
		}
		childDivId='#run_configuration_list';
		mainDivId='#div_PopupRunConfigurationList';
		$('#jTableContainerTestCaseForTestRunPlan').empty();
		if(trpExecMode == 'Attended'){
			testCasebyTestRunPlanId();
		} else {
			unattendedCallMode(testRunPlanId);
			//testCasebyTestRunPlanIdForUnattended("","","",testrunPlanIdDevice,"");
		}
	}else{
		testrunPlanIdDevice = testrunPlanIdForTestCaseExecution;
		childDivId='#runConfig_list';
		mainDivId='#div_PopupTestRunPLanList';
	}
	var urlMapping = "runConfiguration.listbyTestRunPlan?testRunPlanId="+testrunPlanIdDevice+"&jtStartIndex=0&jtPageSize=50";
	$.ajax({
		type: "POST",
  		contentType: "application/json; charset=utf-8",
        url : urlMapping,
        dataType : 'json',
        success : function(data) {
        	if(data.Result=="OK"){
        		if(trpExecMode == 'Attended'){
	        		var listOfData=data.Records;
	            	$(childDivId).empty();
	            	$.each(listOfData, function(i,item){	
	            		var runconfigid = item.runconfigId;					
	 					var runconfigName=item.runConfigurationName;
	 					$(childDivId).append('<label style="word-break:break-all;"><input type="checkbox" name="'+runconfigName+'"  class="icheck" id="' + runconfigid + '" data-radio="iradio_flat-grey" checked/>'+runconfigName+'</label>');
	 				});
	            	$(mainDivId).modal();
        		} else {
        			
        		}
             }
        }
	});	
}

function saveWorkpackageDetail(flag){
	var testRunPlanId=document.getElementById("hdnTestRunPlanId").value;
	var workpackageName = document.getElementById("wpkg_name").value;
	var description ='';
	description = document.getElementById("wpkg_desc").value;
	var plannedStartDate=document.getElementById("plannedStartDate").value;
	var plannedEndDate=document.getElementById("plannedEndDate").value;	
	var contextpath = (window.location.pathname).split("/", 2);
	var root = window.location.protocol + "//" + window.location.host + "/" + contextpath[1];
	var productBuildId= $("#productBuild_ul").find(":selected").attr("id");
	
	if(productBuildId==undefined){
		productBuildId=-1;
	}
	
	if(workpackageName== ""){
		callAlert("Please Enter Workpackage Name");
		return false;
	}	
	
	var url='administration.workpackage.testrunplan';
	var thediv = document.getElementById('reportbox');
	if (thediv.style.display == "none") {
		$.blockUI({
			theme : true,
		 	title : 'Please Wait',
		 	message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
		});
		$.ajax({
		    type: "POST",
		    url: url,
		    data: { 'workpackageName': workpackageName, 'description': description, 'productBuildId': productBuildId,'plannedStartDate':plannedStartDate,'plannedEndDate':plannedEndDate ,'testRunPlanId':testRunPlanId},
		    success: function(data) {
				var msg = data.Message;
		    	$.unblockUI();
		    	if(data.Result=='ERROR'){
		    		callAlert(data.Message);
	 		    	return false;
		    	}else{
					window.location.replace(root+"/administration.workpackage.plan?workpackageId="+data.Record.id);
		    		var matchWord = "Workpackage ID";
		    		var workpackageStr = msg.lastIndexOf(matchWord);
		    		var workpackageStrMatch = msg.substring((workpackageStr+matchWord.length),msg.length).trim();
		    		var workpackageID = workpackageStrMatch.split(' ')[0];
		    		executeTestRunPlanNaviagationHandler(root,msg,workpackageID);
		    		var matchWord = "Workpackage ID";
		    		var workpackageStr = msg.lastIndexOf(matchWord);
		    		var workpackageStrMatch = msg.substring((workpackageStr+matchWord.length),msg.length).trim();
		    		var workpackageID = workpackageStrMatch.split(' ')[0];
		    		executeTestRunPlanNaviagationHandler(root,msg,workpackageID);
					window.location.replace(root+"/administration.workpackage.plan?workpackageId="+data.Record.id);
		    	}
		    },    
		    dataType: "json", // expected return value type
		}); 
	}else {
		thediv.style.display = "none";
    	thediv.innerHTML = '';
	}
}

function saveWorkpackageDetailDevice(checkboxValues,testConfigurationData){
	var testRunPlanId=document.getElementById("hdnTestRunPlanDeviceId").value;
	var workpackageName = document.getElementById("wpkg_name").value;
	var description ='';
	description = document.getElementById("wpkg_desc").value;
	var plannedStartDate=document.getElementById("plannedStartDate").value;
	var plannedEndDate=document.getElementById("plannedEndDate").value;
	var contextpath = (window.location.pathname).split("/", 2);
	var root = window.location.protocol + "//" + window.location.host + "/" + contextpath[1];
	var productBuildId='';
	productBuildId= $("#productBuild_ul").find(":selected").attr("id");// $("#productBuild_ul").find('option:selected').attr('id');	
	if(productBuildId==undefined){
		productBuildId=-1;
	}
	if(workpackageName== ""){
		callAlert("Please Enter Workpackage Name");
		return false;
	}	
	
	
	//var  url = 'administration.workpackage.testrunplan.devices';
	var url = 'rest/atlas/taf/testExecution/query/executeSelectiveTestcasesTestPlan';
	var thediv = document.getElementById('reportbox');
    if (thediv.style.display == "none") {
		$.blockUI({
		 	theme : true,
		 	title : 'Please Wait',
		 	message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
		});
		$.ajax({
		    type: "POST",
		    url: url,
		    cache: false,
		    dataType: "JSON",
		    headers: { 
		    	'Accept': 'application/json',
		    	'Content-Type': 'application/json' 
		    },
		    //data: {'testConfigurationData':JSON.stringify(testConfigurationData)},
		    data: JSON.stringify(testConfigurationData),
		    //data: { 'workpackageName': workpackageName, 'description': description, 'productBuildId': productBuildId,'plannedStartDate':plannedStartDate,'plannedEndDate':plannedEndDate ,'testRunPlanId':testRunPlanId,'runconfigId':checkboxValues},
		    success: function(data) {
		    	$.unblockUI();
		    	var msg = data.message;		    	
		    	if(msg=="")msg=data.wpMessage;
		    	if(data.result=='ERROR'){
		    		callAlert(msg);
	 		    	return false;
		    	}else{
		    		//var matchWord = "Workpackage ID";
		    		//var workpackageStr = msg.lastIndexOf(matchWord);
		    		//var workpackageStrMatch = msg.substring((workpackageStr+matchWord.length),msg.length).trim();
		    		//var workpackageID = workpackageStrMatch.split(' ')[0];
		    		var responseData = JSON.parse(data.data);
		    		var workpackageID = responseData["Workpackage ID"];
		    		if(trpExecMode == 'Attended'){
		    			executeTestRunPlanNaviagationHandler(root,msg,workpackageID);
		    		}
		    	}
		    },
		    error: function(data){
		    	console.log("Error in updation");
		    },
		    complete: function(data){
		    	console.log("Complete in updation");
		    },
		    dataType: "json", // expected return value type
		}); 
    }else {
   	   	thediv.style.display = "none";
   	   	thediv.innerHTML = '';
    }      
}

function executeTestRunPlanNaviagationHandler(root,msg,workpackageID){
	 bootbox.confirm({
		 	title: "Test Plan Execution",
		 	size: "large",
		    message: msg,
		    buttons: {
		        cancel: {
		            label: 'OK',
		        },
		        confirm: {
		            label: 'Go to Workpackages page',
		        }
		    },
		    callback: function (result) {
		    	if(result){			 
		    		$('#tpTileView_Child_Container').modal('hide');
		    		window.location.replace(root+"/administration.workpackage.plan");
				}
		    }
		});
}

function getPlanHandlerTestRunPlanTab(productId,productName,testRunPlanName,productBuildId,productBuildName){
	var url='getISERecommended.Testcases.by.buildId?buildId='+productBuildId;
	var title = "Product Name:"+productName+" - Test Plan Name:"+testRunPlanName+" - Build Name:"+productBuildName+" - Recommended Test Cases";
	var jsonObj={"Title":title,		
			"listURL": url,					
			"componentUsageTitle":"recommendedTestPlan",
			"productId": productId,
			"buildId" : productBuildId,
			"componentUsageTitle": "ProductTestRunPlan"
	};
	RecommentedTestPlan.init(jsonObj);
	$("#recommendedTestPlanPdMgmContainer").modal();
}

//BEGIN: ConvertDataTable - Workpackage - ChildTable
var workpackageDT_oTable='';
function workpackageDataTableHeader(){
	var childDivString ='<table id="workpackage_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Name</th>'+
			'<th>Description</th>'+
			'<th>Planned Start Date</th>'+
			'<th>Planned End Date</th>'+
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
function workpackageDT_Container(jsonObj){	
	try{
		if ($("#dataTableContainerForWorkpackage").children().length>0) {
			$("#dataTableContainerForWorkpackage").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = workpackageDataTableHeader(); 			 
	$("#dataTableContainerForWorkpackage").append(childDivString);
	
	workpackageDT_oTable = $("#workpackage_dataTable").dataTable( {				 	
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
	     		  $('#workpackage_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeWorkpackageDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Workpackage',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Workpackage',
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
           { mData: "name", className: 'disableEditInline', sWidth: '35%' },	
           { mData: "description", className: 'disableEditInline', sWidth: '35%' },	
           { mData: "plannedStartDate", className: 'disableEditInline', sWidth: '15%' },
           { mData: "plannedEndDate", className: 'disableEditInline', sWidth: '15%' },
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorWorkpackage-active', row).prop( 'checked', data.readyState == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#workpackage_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorWorkpackage.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#workpackage_dataTable_length").css('margin-top','8px');
	$("#workpackage_dataTable_length").css('padding-left','35px');		
	
	workpackageDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutWorkpackageDT='';
function reInitializeWorkpackageDT(){
	clearTimeoutWorkpackageDT = setTimeout(function(){				
		workpackageDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutWorkpackageDT);
	},200);
}
//END: ConvertDataTable - Workpackage - ChildTable

//BEGIN: ConvertDataTable - TestPlanConfigurationProperties - ChildTable
var tpConfigurationPropertiesDT_oTable='';
var editorTPConfigurationProperties='';
function tpConfigurationPropertiesDataTableHeader(){
	var childDivString ='<table id="tpConfigurationProperties_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Property</th>'+
			'<th>Key</th>'+
			'<th>Value</th>'+
			'<th></th>'+
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

function tpConfigurationPropertiesDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForTPConfigurationProperties").children().length>0) {
			$("#dataTableContainerForTPConfigurationProperties").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = tpConfigurationPropertiesDataTableHeader(); 			 
	$("#dataTableContainerForTPConfigurationProperties").append(childDivString);
	
	editorTPConfigurationProperties = new $.fn.dataTable.Editor( {
	    "table": "#tpConfigurationProperties_dataTable",
		//ajax: "",
		ajaxUrl: "entityconfigproperty.update?entityConfig",
		idSrc:  "entityConfigPropertyId",
		i18n: {
	        create: {
	            title:  "Create a Test Configuration Properties",
	            submit: "Create",
	        }
	    },
		fields: [
		{
             label: "entityId",
             name: "entityId",
             "type":"hidden"
          },{
            label: "entityMasterId",
            name: "entityMasterId",
            "type":"hidden"
        },{
            label: "entityConfigPropertyId",
            name: "entityConfigPropertyId",
            "type":"hidden"
        },{
            label: "entityConfigPropertiesMasterId",
            name: "entityConfigPropertiesMasterId", 
            "type": "hidden"
        },{
            label: "property",
            name: "property", 
        },{
            label: "value",
            name: "value",
            options: optionsResultArr[0],
            "type": "select"
        }        
    ]
	});
	
	tpConfigurationPropertiesDT_oTable = $("#tpConfigurationProperties_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [3]; // search column TextBox Invisible Column position
	     		  $('#tpConfigurationProperties_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeTPConfigurationPropertiesDT();
			   },  
		   buttons: [
		             	{
						    text: 'New',
						    action: function ( e, dt, node, config ) {
						    	if(pageType=="HOMEPAGE")$('#divPopUpEntityConfigurationProps').css('z-index','10052');
						    	entityConfigurationProps(9,tpcId,null);
						    }
		             	},
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Test Configuration Properties',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Test Configuration Properties',
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
           { mData: "entityConfigPropertiesMasterName", className: 'disableEditInline', sWidth: '40%' },	
           { mData: "property", className: 'editable', sWidth: '30%' },	
           { mData: "value", className: 'disableEditInline', sWidth: '30%' ,			
				mRender: function (data, type, full) {
					if ( type === 'display' ) {
						data = full.value;
					}
					return data;
				},
           },
           { mData: null,				 
        		bSortable: false,
        		sWidth: '5%',
        		mRender: function(data, type, full) {			
        		 var img = ('<div style="display: flex;">'+
        			'<button style="border: none; background-color: transparent; outline: none;">'+
        						'<i class="fa fa-trash-o details-control" onClick="removeEntityConfProperty('+data.entityConfigPropertyId+', 9,'+data.testRunPlanId+')" title="Delete Properties" style="padding-left: 0px;"></i></button>'+		
        			'</div>');	      		
        		 return img;
        		}
        	},		           
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorTPConfigurationProperties-active', row).prop( 'checked', data.readyState == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#tpConfigurationProperties_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorTPConfigurationProperties.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#tpConfigurationProperties_dataTable_length").css('margin-top','8px');
	$("#tpConfigurationProperties_dataTable_length").css('padding-left','35px');		
	
	tpConfigurationPropertiesDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutTPConfigurationPropertiesDT='';
function reInitializeTPConfigurationPropertiesDT(){
	clearTimeoutTPConfigurationPropertiesDT = setTimeout(function(){				
		tpConfigurationPropertiesDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTPConfigurationPropertiesDT);
	},200);
}

function removeEntityConfProperty(entityConfigId, entityMasterId, trpId){
	if(rnCnfgFlag)entityMasterId='27';
	if(trpId == null)trpId=tpcId;
	var url;
	var id;
	if(rnCnfgFlag){
		url = 'entityconfigproperty.remove?entityConfigId='+entityConfigId+'&entityMasterId='+entityMasterId+'&entityId='+rnCnfgId;
		id=rnCnfgId;
	}else{
		url = 'entityconfigproperty.remove?entityConfigId='+entityConfigId+'&entityMasterId='+entityMasterId+'&entityId='+trpId;
		id=trpId;
	}
	$.ajax({
		type: "POST",
		url: url,
		success: function(data) {		           			
			if(data.Result == "ERROR"){
				callAlert(data.Message);
				return false;
			}else if(data.Result == "OK"){						
				var url = 'entityConfigureProperties.list?entityId='+ id +'&entityMasterId='+entityMasterId+'&jtStartIndex=0&jtPageSize=10000';
		       	testPlanDataTable("TPConfigurationPropertiesTable",url);
			}
		}        
	});  
}
//END: ConvertDataTable - TestPlanConfigurationProperties - ChildTable

//BEGIN: ConvertDataTable - TestConfiguration - ChildTable
function testConfigurationDataTableHeader(){
	var childDivString ='<table id="testConfiguration_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Selected</th>'+
			'<th>ID</th>'+
			'<th>Product Type</th>'+
			'<th>Environment Combination Name</th>'+
			'<th>Device</th>'+
			'<th>Host</th>'+
			'<th>Test Tool Name</th>'+
			'<th>Script Type Name</th>'+
			'<th>Script File Location</th>'+
			'<th>Copy TestSuite from TestPlan</th>'+
			'<th>Properties</th>'+
			'<th>Test Suite</th>'+
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

function testConfigurationDT_Container(jsonObj){
	var divId;
	if($('#runConfigurationDT_Child_Container').is(':visible')){
		divId = '#runConfigurationDT_Child_Container #dataTableContainerForTestConfiguration';
	}else if($('#testRunPlanNameLinkContainer').is(':visible')){
		divId = '#TargDevices #dataTableContainerForTestConfiguration';
	}else{
		divId = '#showTestBedSummary #dataTableContainerForTestConfiguration';
	}
	try{
		if ($('#runConfigurationDT_Child_Container #dataTableContainerForTestConfiguration').children().length>0) {
			$('#runConfigurationDT_Child_Container #dataTableContainerForTestConfiguration').children().remove();
		}
		if ($('#TargDevices #dataTableContainerForTestConfiguration').children().length>0) {
			$('#TargDevices #dataTableContainerForTestConfiguration').children().remove();
		}
		if ($('#showTestBedSummary #dataTableContainerForTestConfiguration').children().length>0) {
			$('#showTestBedSummary #dataTableContainerForTestConfiguration').children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = testConfigurationDataTableHeader(); 			 
	$(divId).append(childDivString);
	
	editorTestConfiguration = new $.fn.dataTable.Editor( {
	    "table": "#testConfiguration_dataTable",
		ajax: 'administration.runconfig.add?testRunplanId='+tpcId,
		ajaxUrl: 'runconfiguration.testtool.update?testRunplanId='+tpcId,
		idSrc:  "runconfigId",
		i18n: {
	        create: {
	            title:  "Create a new Test Configuration",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "testRunPlanId",
            name: "testRunPlanId",
            "type": "hidden",
            "def": tpcId
        },{
            label: "runconfigId",
            name: "runconfigId",
            "type": "hidden",
        },{
            label: "productId",
            name: "productId",
            "type": "hidden",
            "def": productId,
        },{
            label: "Status",
            name: "isSelected",
            "type": "hidden",
        },{
            label: "Product Type",
            name: "productType",
            options: optionsResultArr[0],
            "type":"select"
        },{
            label: "Environment Combination Name",
            name: "environmentcombinationId",
            options: optionsResultArr[1],
            "type":"select"
        },{
            label: "Device",
            name: "deviceId",
            options: optionsResultArr[2],
            "type":"select"
        },{
            label: "Host",
            name: "hostId",
            options: optionsResultArr[3],
            "type":"select"
        },{
            label: "Test Tool Name",
            name: "testToolId",
            options: optionsResultArr[4],
            "type":"select"
        },{
            label: "Script Type",
            name: "testScriptType",
            options: optionsResultArr[5],
            "type":"select"
        },{
            label: "Script File Location",
            name: "testScriptFileLocation",
        },{
            label: "Copy Test Suites from Test Plan",
            name: "copyTestPlanTestSuite",
            options: optionsResultArr[6],
            "type":"select"
        }        
    ]
	});
	
	testConfigurationDT_oTable = $("#testConfiguration_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [0,10,11]; // search column TextBox Invisible Column position
	     		  $('#testConfiguration_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeTestConfigurationDT();
			   },  
		   buttons: [
						{ 
							extend: "create",
							editor: editorTestConfiguration
						},	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Test Configuration',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Test Configuration',
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
			{ mData: "isSelected", className: 'editable', sWidth: '5%',			
				mRender: function (data, type, full) {
					if ( type === 'display' ) {
						if (data ==1) { return '<input type="checkbox" class="isSelected-active" checked>'; }
						else { return '<input type="checkbox" class="isSelected-active">'; }
					}
					return data;
				},
				className: "dt-body-center"	            
			},
			{ mData: "runconfigId", className: 'disableEditInline', sWidth: '5%'}, 
			{ mData: "productType", className: 'productType editable', sWidth: '15%', editField: "productType",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorTestConfiguration, 'productType', full.productType); 		           	 
		             return data;
	            },
           },
           { mData: "environmentcombinationId", className: 'environmentcombinationId editable', sWidth: '15%', editField: "environmentcombinationId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorTestConfiguration, 'environmentcombinationId', full.environmentcombinationId); 		           	 
		             return data;
	            },
           },
           { mData: "deviceId", className: 'deviceId editable', sWidth: '12%', editField: "deviceId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorTestConfiguration, 'deviceId', full.deviceId); 		           	 
		             return data;
	            },
			},
            { mData: "hostId", className: 'hostId editable', sWidth: '12%', editField: "hostId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorTestConfiguration, 'hostId', full.hostId); 		           	 
		             return data;
	            },
			},
            { mData: "testToolId", className: 'testToolId editable', sWidth: '15%', editField: "testToolId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorTestConfiguration, 'testToolId', full.testToolId); 		           	 
		             return data;
	            },
			},
            { mData: "testScriptType", className: 'testScriptType editable', sWidth: '15%', editField: "testScriptType",
	       		mRender: function (data, type, full) {
	       			if(type == "display"){
		           		data = full.testScriptType;
	       			} 		           	 
	       			return data;
	            },
			},
            { mData: "testScriptFileLocation", className: 'testScriptFileLocation editable', sWidth: '10%'},
            { mData: "copyTestPlanTestSuite", className: 'disableEditInline', sWidth: '10%', editField: "copyTestPlanTestSuite",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorTestConfiguration, 'copyTestPlanTestSuite', full.copyTestPlanTestSuite); 		           	 
		             return data;
	            },
			},
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       				'<img src="css/images/list_metro.png" class="runConfigureImg" title="Properties" />'+
     	       		'</div>');	      		
           		 return img;
            	}
            },
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       				'<img src="css/images/list_metro.png" class="testSuiteImg" title="Test Suite" />'+
     	       		'</div>');	      		
           		 return img;
            	}
            },
       ],
       columnDefs: [],
       rowCallback: function ( row, data ) {
    	   //$('input.editorTestConfiguration-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#testConfiguration_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorTestConfiguration.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#testConfiguration_dataTable tbody').on( 'change', 'input.isSelected-active', function () {
		var tr = $(this).closest('tr');
		var row = testConfigurationDT_oTable.DataTable().row(tr);
		var urlMapping;
		if(row.data().runconfigId==""){
			row.data().runconfigId = currRnCfgId;
		}
		if(!$(this).prop( 'checked' )){
	    	urlMapping = 'administration.map.testConfiguration.testRunPlan?testRunPlanId='+row.data().testRunPlanId+'&type=unmap&runConfigId='+row.data().runconfigId;
		}else{
			urlMapping = 'administration.map.testConfiguration.testRunPlan?testRunPlanId='+row.data().testRunPlanId+'&type=map&runConfigId='+row.data().runconfigId;
		}	
	    $.ajax({
	           type: "POST",
	           contentType: "application/json; charset=utf-8",
	           url : urlMapping,
	           dataType : 'json',
	           success : function(data) {
	        	   if(data.Result=="ERROR"){
	        		   callAlert(data.Message);
	        		   return false;
	        	   }else{
	        		   callAlert(data.Message);
	        		   return true;
	        	   }
	         }
	    }); 
	});	
	
	editorTestConfiguration.on( 'create', function ( e, json, data ) {
	    currRnCfgId = json.Record[0].runconfigId;
		var testConfigurationURL = 'runConfiguration.listbyTestRunPlan?testRunPlanId='+tpcId;
       	testPlanDataTable("TestConfigurationTable",testConfigurationURL);
	} );
	
	$('#testConfiguration_dataTable tbody').on('click', 'td .runConfigureImg', function () {
    	event.preventDefault();
    	var tr = $(this).closest('tr');
    	var row = testConfigurationDT_oTable.DataTable().row(tr);
    	if(row.data().runconfigId==""){
			row.data().runconfigId = currRnCfgId;
		}
    	rnCnfgId = row.data().runconfigId;
       	var url = 'entityConfigureProperties.list?entityId='+ rnCnfgId +'&entityMasterId=27&jtStartIndex=0&jtPageSize=10';
       	rnCnfgFlag = true;
		testPlanDataTable("TPConfigurationPropertiesTable",url);
       	$('#testPlanConfigureDT_Child_Container').modal();
	});
	
	$('#testConfiguration_dataTable tbody').on('click', 'td .testSuiteImg', function () {
		event.preventDefault();
    	var tr = $(this).closest('tr');
    	var row = testConfigurationDT_oTable.DataTable().row(tr);
    	if(row.data().runconfigId==""){
			row.data().runconfigId = currRnCfgId;
		}
		rnCnfgId = row.data().runconfigId;
		tpcId = row.data().testRunPlanId;
		productId = row.data().productId;
		document.getElementById("treeHdnCurrentProductId").value = productId;
		testsuites(nodeType);
		$('#testSuiteDT_Child_Container').modal();
		$(document).off('focusin.modal');
	});
	
	$("#testConfiguration_dataTable_length").css('margin-top','8px');
	$("#testConfiguration_dataTable_length").css('padding-left','35px');	
	
	editorTestConfiguration.off('preSubmit').on( 'preSubmit', function ( e, o, action ) {
        if ( action !== 'remove' ) {
        	var productType = this.field('productType').val();
        	if(productType == '2' || productType == '3' || productType == '4') {
        		var hostId = this.field( 'hostId' );
	            if ( ! hostId.isMultiValue() ) {
	                if ( hostId.val() ) {
	                	var str = hostId.val();
	                	if(str == null) {
	                		hostId.error( 'Please select host' );
	                	}else if(str == "-1"){
	                		if(action=="edit"){
								var currField = editorTestConfiguration.modifier(this).className.trim().split(' ')[0];
								this.field(currField).error( 'Host is not mapped. Please map the host and create.' );
                    		}else{
                    			hostId.error( 'Host is not mapped. Please map the host and create.' );
                    		}
                    	}
	                }else{
	                	hostId.error( 'Host is not mapped. Please map the host and create.' );
	            	}	               
	            }
	            
	            // If any error was reported, cancel the submission so it can be corrected
                if ( this.inError() ) {
                    return false;
                }
        	}else if(productType == '1' || productType == '5') {
        		var deviceId = this.field( 'deviceId' );
                if ( ! deviceId.isMultiValue() ) {
                    if ( deviceId.val() ) {
                    	var str = deviceId.val();
                    	if(str == null) {
                    		deviceId.error( 'Please select device' );
                    	}
                    	else if(str == "-1"){
                    		if(action=="edit"){
								var currField = editorTestConfiguration.modifier(this).className.trim().split(' ')[0];							
								this.field(currField).error( 'Device is not mapped. Please map the Device and create.' );
                    		}else{
                    			deviceId.error( 'Device is not mapped. Please map the Device and create.' );
                    		}
                    	}
                    }else{
                    	deviceId.error( 'Device is not mapped. Please map the Device and create.' );
                	}
                }
                // If any error was reported, cancel the submission so it can be corrected
                if ( this.inError() ) {
                    return false;
                }
        	}           
        }
    } );
	
	testConfigurationDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutTestConfigurationDT='';
function reInitializeTestConfigurationDT(){
	clearTimeoutTestConfigurationDT = setTimeout(function(){				
		testConfigurationDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTestConfigurationDT);
	},200);
}
//END: ConvertDataTable - TestConfiguration

function runconfiguration_change_testsuite_checkbox_status(flag,testSuiteListId,self){
	unselected_id=$('.testsuite_checkbox[value!='+testSuiteListId+']:checked').eq(0).val();	
	unselected_id=$(self).val();
	
	var url="";
	if(!$(self).is(':checked')){
		url="administration.testsuite.mapunmaprunconfig?runConfigId="+rnConfigId+"&testSuiteId="+testSuiteListId+"&action=Remove";
	}else{
		url="administration.testsuite.mapunmaprunconfig?runConfigId="+rnConfigId+"&testSuiteId="+testSuiteListId+"&action=Add";
	}
	$.ajax({
		type: "POST",
	    url: url,
		async:false,
	    success: function(data) {
	    	if(data.Result=='ERROR'){
	    		callAlert(data.Message);
 		    	return false;
	    	}else{
	    		callAlert(data.Message);
 		    	return true;
	    	}
	    },    
	    dataType: "json"
	});
}

function productMgmtShowTestRunplanFormNew() {
	productMgmtReInitializeFormWizard();
	isReady='';
	isReadyMessage='';
	var jsonObj={};		
	jsonObj.productId=productId;
	jsonObj.Title = "Add New Test Plan";
	jsonObj.productVersionListId=productVersionId;
	jsonObj.testRunPlanId=testRunPlanId;
	jsonObj.testRunPlanName="";
	jsonObj.executionTypeId='';
	TestRunPlan.init(jsonObj);	
}

function productMgmtReInitializeFormWizard() {
	$('#form_wizard_1').replaceWith(formClone.clone());
	var jsonObj={};		
	TestRunPlanDT.init(jsonObj);
	document.getElementById("hdnTestRunPlanId").value="";
	document.getElementById("hdnTestRunPlanType").value="";
	initializeBootstrapWizard();
}

function initializeBootstrapWizard() {
	$('#form_wizard_1').bootstrapWizard({
	    'nextSelector': '.button-next',
	    'previousSelector': '.button-previous',
	    onTabClick: function (tab, navigation, index, clickedIndex) {
	        formWizardNavigation(tab, navigation, clickedIndex);
	        formWizardNavigationBar(tab, navigation, clickedIndex);
	        return true;
	    },
	    onNext: function (tab, navigation, index) {   	 
			formWizardNavigation(tab, navigation, index);		 
	    },
	    onPrevious: function (tab, navigation, index) {
			formWizardNavigation(tab, navigation, index);	  
	    },
	    onTabShow: function (tab, navigation, index) {
	        var $total = navigation.find('li').length;
			var $current = index+1;
			var $percent = ($current/$total) * 100;
			$('#form_wizard_1 .progress-bar').css({width:$percent+'%'});
	    }
	});
}
function formWizardNavigationBar(tab, navigation, index){
	var total = navigation.find('li').length;
	var current = index + 1;
	var $percent = ( (current) / total) * 100;
	
	$('#form_wizard_1').find('.progress-bar').css({
		width: $percent + '%'
	});  	
}
function formWizardNavigation(tab, navigation, index){
	var currActiveTab = index;
	var valid = "";
	if(currActiveTab==0){
		var firstTab = $("#form_wizard_1").find("#TestRunPlan");
		if(!(firstTab.hasClass("active in"))){
			firstTab.addClass("active in");
			firstTab.siblings(".tab-pane").removeClass("active in");
		}
		$('#form_wizard_1').find('.button-previous').hide();  		  
	 
	}else if (currActiveTab==1){
		valid = testsuitesplan();
		if(valid == false) return false;
	}else if (currActiveTab==2){
		testRunPlanId=document.getElementById("hdnTestRunPlanId").value;
		testConfigurationURL = 'runConfiguration.listbyTestRunPlan?testRunPlanId='+testRunPlanId;
       	testPlanDataTable("TestConfigurationTable",testConfigurationURL);		
	}else if (currActiveTab==3){
		testRunPlanTestBeds();
	}else if(currActiveTab==4){  		  
		testRunPlanId=document.getElementById("hdnTestRunPlanId").value;
		testRunPlanType=document.getElementById("hdnTestRunPlanType").value;
		testRunPlan(testRunPlanId,testRunPlanType);
	}
	handleTitle(tab, navigation, parseInt(currActiveTab));
}

function testsuitesplan(){	
	var date = new Date();
    var timestamp = date.getTime();
    type="1";
    testRunPlanId=document.getElementById("hdnTestRunPlanId").value;
    if(testRunPlanId == null || testRunPlanId <= 0 || testRunPlanId == 'null' || testRunPlanId == ''){
    	callAlert("Please create Test Plan");
		return false;
	}
    tType = "TestSuiteMapTable";
    listTestSuiteSelectedProductVersionPlanDataTable(productId, productVersionId, timestamp,type,testRunPlanId);
};

//BEGIN: ConvertDataTable - TestSuiteMap
var testSuiteMapDT_oTable='';
var editorTestSuiteMap='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;
var tType;

function listTestSuiteSelectedProductVersionPlanDataTable(productId, productVersionId, timestamp,type,testRunPlanId){
	optionsItemCounter=0;
	optionsResultArr=[];
	if(tType == "TestSuiteMapTable"){			
		if(productVersionId == -1)
			testSuiteMapURL = 'test.suite.byProduct.with.mappings.list?productMasterId='+productId+"&testRunPlanId="+testRunPlanId+"&runConfigId=-1";
		else
			testSuiteMapURL = 'test.suite.byProductVersion.list?versionId='+productVersionId+"&timeStamp="+timestamp+"&testRunPlanId="+testRunPlanId+"&runConfigId=-1";
		optionsArr = [ {id:"executionTypeId", url:'common.list.executiontypemaster.byentityid?entitymasterid=7'} ];
	}else if(tType == "ViewTestCasesTable"){
		viewTestCasesURL = 'test.suite.case.testplan.list?testSuiteId='+type+"&testRunPlanId="+testRunPlanId;
		optionsArr = [ {id:"executionTypeId", url:'common.list.executiontypemaster.byentityid?entitymasterid=3'},
		               {id:"testcasePriority", url:'common.list.testcasepriority'}];
	}
	testSuiteMapOptions_Container(optionsArr);
}

function testSuiteMapOptions_Container(urlArr){
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
				 testSuiteMapOptions_Container(optionsArr);
			 }else{
				testSuiteMapDataTableInit();	
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

function testSuiteMapDataTableInit(){
	if(tType == "TestSuiteMapTable"){
		url= testSuiteMapURL +'&jtStartIndex=0&jtPageSize=1000';
		jsonObj={"Title":"Test Suite","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Test Suite"};
	}else if(tType == "ViewTestCasesTable"){
		url= viewTestCasesURL +'&jtStartIndex=0&jtPageSize=1000';
		jsonObj={"Title":"View Test Cases","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"View Test Cases"};
	}
	testSuiteMapDataTableContainer.init(jsonObj);
}

var testSuiteMapDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		assignTestSuiteMapDataTableValues(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignTestSuiteMapDataTableValues(jsonObj){
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
			if(tType == "TestSuiteMapTable"){
				testSuiteMapDT_Container(jsonObj);
			}else if(tType == "ViewTestCasesTable"){
				viewTestCasesDT_Container(jsonObj);
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

function testSuiteMapDataTableHeader(){
	var childDivString ='<table id="testSuiteMap_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Selected</th>'+
			'<th>Test Suite Name</th>'+
			'<th>Script Type</th>'+
			'<th>Script Source</th>'+
			'<th>Script File Location</th>'+
			'<th>Suite Code</th>'+
			'<th>Test Case Count</th>'+
			'<th>Test Steps Count</th>'+	
			'<th>Execution Type</th>'+
			'<th>View Test Case</th>'+
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
function testSuiteMapDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForTestSuiteMap").children().length>0) {
			$("#dataTableContainerForTestSuiteMap").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = testSuiteMapDataTableHeader(); 			 
	$("#dataTableContainerForTestSuiteMap").append(childDivString);
	
	editorTestSuiteMap = new $.fn.dataTable.Editor( {
		"table": "#testSuiteMap_dataTable",
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
            label:"Test Suite Name",
			name:"testSuiteName",									
        },{
            label:"Script Type",
			name:"testScriptType",									
        },{
            label: "Script Source",
            name: "testScriptSource", 
        },{
            label: "Script File Location",
            name: "testSuiteScriptFileLocation",                
        },{
            label:"Suite Code",
			name:"testSuiteCode",									
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
			options: optionsResultArr[0],
            "type"  : "select",	
        },  
    ]
	});	
	
	testSuiteMapDT_oTable = $("#testSuiteMap_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [0,9]; // search column TextBox Invisible Column position
	     		  $('#testSuiteMap_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeTestSuiteMapDT();
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
	        { mData: "isSelected",
                mRender: function (data, type, full) {
              	  if ( type === 'display' ) {
                          return '<input type="checkbox" class="editorTestSuiteMap-active">';
                      }
                      return data;
                  },
                  className: "dt-body-center"
	        },
			{ mData: "testSuiteName",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "testScriptType",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testScriptSource",className: 'disableEditInline', sWidth: '15%' },			
			{ mData: "testSuiteScriptFileLocation",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testSuiteCode",className: 'disableEditInline', sWidth: '15%' },
			{ mData: "testCaseCount",className: 'disableEditInline', sWidth: '5%' },		
			{ mData: "testStepsCount",className: 'disableEditInline', sWidth: '5%' },           
			{ mData: "executionTypeId", className: 'disableEditInline', sWidth: '10%',
				mRender: function (data, type, full) {
					data = optionsValueHandler(editorTestSuiteMap, 'executionTypeId', full.executionTypeId);
					return data;
				 },
			},	
	        { mData: null,				 
              	bSortable: false,
              	mRender: function(data, type, full) {				            	
             		 var img = ('<div style="display: flex;">'+
   	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
   	       				'<img src="css/images/list_metro.png" class="viewTCImg" title="View Test Case" />'+
       	       		'</div>');	      		
             		 return img;
              	}
	        },	
       ],       
       rowCallback: function ( row, data ) {
    	   $('input.editorTestSuiteMap-active', row).prop( 'checked', data.isSelected == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#testSuiteMap_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorTestSuiteMap.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#testSuiteMap_dataTable tbody').on('click', 'td .viewTCImg', function () {
		var tr = $(this).closest('tr');
    	var row = testSuiteMapDT_oTable.DataTable().row(tr);
    	$('#testSuiteMapDT_Child_Container').modal();
		$(document).off('focusin.modal');
		event.preventDefault();
		tType = "ViewTestCasesTable";
		listTestSuiteSelectedProductVersionPlanDataTable(productId, productVersionId, timestamp,row.data().testSuiteId,testRunPlanId);
	});

	$('#testSuiteMap_dataTable').on( 'change', 'input.editorTestSuiteMap-active', function (e) {
		var tr = $(this).closest('tr');
    	var row = testSuiteMapDT_oTable.DataTable().row(tr);
		var url="";
		var testSuiteData = $(testSuiteMapDT_oTable.fnGetNodes());
		var tSuiteArr=[];
		
		for(var i=0;i<testSuiteData.length;i++){
			if(testSuiteData.eq(i).find('td input').attr('checked') == "checked"){
				tSuiteArr.push(testSuiteData.eq(i).find('td').eq(1).html);
			}
		}
		
		if(tSuiteArr.length>1){
			e.target.checked=false;
			testSuiteMapDT_oTable.DataTable().rows().deselect();
			callAlert('Multiple Test Suite is not allowed. Please unmap the already selected Test Suite.');
			return false;
		}
		
		if(!$(this).is(':checked')){
			 url="administration.testsuite.mapunmaprunConfiguration?productversionId="+productVersionId+"&productId="+productId +"&testRunPlanId="+testRunPlanId+"&testSuiteId="+row.data().testSuiteId+"&type=Remove";
		}else{
			 url="administration.testsuite.mapunmaprunConfiguration?productversionId="+productVersionId+"&productId="+productId +"&testRunPlanId="+testRunPlanId+"&testSuiteId="+row.data().testSuiteId+"&type=Add";
		}
		$.ajax({
			type: "POST",
			url: url,
			async:false,
			success: function(data) {
				if(data.Result=='ERROR'){
					callAlert(data.Message);
					return false;
	 		    }else{
	 		    	callAlert(data.Message);
	 		    	return true;
	 		    }
	 		 },    
	 		 dataType: "json"
	 	});
	});
	
	$("#testSuiteMap_dataTable_length").css('margin-top','8px');
	$("#testSuiteMap_dataTable_length").css('padding-left','35px');
	
	$("#testSuiteMap_dataTable_filter").css('margin-right','6px');
	
	testSuiteMapDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutTestSuiteMapDT='';
function reInitializeTestSuiteMapDT(){
	clearTimeoutTestSuiteMapDT = setTimeout(function(){				
		testSuiteMapDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTestSuiteMapDT);
	},200);
}
//END: ConvertDataTable - TestSuiteMap

//BEGIN: ConvertDataTable - ViewTestCases
function viewTestCasesDataTableHeader(){
	var childDivString ='<table id="viewTestCases_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Selected</th>'+
			'<th>Test Case ID</th>'+
			'<th>Test Case Name</th>'+
			'<th>Test Case Description</th>'+
			'<th>Test Case Code</th>'+
			'<th>Execution Type</th>'+
			'<th>Priority</th>'+
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

function viewTestCasesDT_Container(jsonObj){	
	try{
		if ($("#dataTableContainerForViewTestCases").children().length>0) {
			$("#dataTableContainerForViewTestCases").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = viewTestCasesDataTableHeader(); 			 
	$("#dataTableContainerForViewTestCases").append(childDivString);
	
	editorViewTestCases = new $.fn.dataTable.Editor( {
		"table": "#viewTestCases_dataTable",
		//ajax: "test.suite.add",
		//ajaxUrl: "test.suite.update",
		idSrc:  "testCaseId",
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
			label:"testCaseId",
			name:"testCaseId",		
			type: 'hidden',					
		},{
            label:"testCaseName",
			name:"testCaseName",									
        },{
            label:"testCaseDescription",
			name:"testCaseDescription",									
        },{
            label: "testCaseCode",
            name: "testCaseCode", 
        },{
            label: "executionTypeId",
            name: "executionTypeId",     
            options: optionsResultArr[0],
            "type"  : "select",	
        },{
            label:"testcasePriorityId",
			name:"testcasePriorityId",	
			options: optionsResultArr[1],
            "type"  : "select",	
        },  
    ]
	});	
	
	viewTestCasesDT_oTable = $("#viewTestCases_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [0]; // search column TextBox Invisible Column position
	     		  $('#viewTestCases_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeViewTestCasesDT();
			   },  
		   buttons: [
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'View Test Case',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'View Test Case',
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
	        { mData: "isSelected",
                mRender: function (data, type, full) {
              	  if ( type === 'display' ) {
                          return '<input type="checkbox" class="editorViewTestCases-active">';
                      }
                      return data;
                  },
                  className: "dt-body-center"
	        },
			{ mData: "testCaseId",className: 'disableEditInline', sWidth: '10%' },			
			{ mData: "testCaseName",className: 'disableEditInline', sWidth: '25%' },
			{ mData: "testCaseDescription",className: 'disableEditInline', sWidth: '25%' },			
			{ mData: "testCaseCode",className: 'disableEditInline', sWidth: '10%' },
			{ mData: "executionTypeId", className: 'disableEditInline', sWidth: '15%',
				mRender: function (data, type, full) {
					data = optionsValueHandler(editorViewTestCases, 'executionTypeId', full.executionTypeId);
					return data;
				 },
			},	
			{ mData: "testcasePriorityId", className: 'disableEditInline', sWidth: '15%',
				mRender: function (data, type, full) {
					data = optionsValueHandler(editorViewTestCases, 'testcasePriorityId', full.testcasePriorityId);
					return data;
				 },
			},	
       ],       
       rowCallback: function ( row, data ) {
    	   $('input.editorViewTestCases-active', row).prop( 'checked', data.isSelected == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	// Activate an inline edit on click of a table cell
	$('#viewTestCases_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorViewTestCases.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#viewTestCases_dataTable').on( 'change', 'input.editorViewTestCases-active', function () {
		var tr = $(this).closest('tr');
    	var row = viewTestCasesDT_oTable.DataTable().row(tr);
		var url="";
		if(!$(this).is(':checked')){
			 url="administration.testsuite.testcase.maptestrunplan?testCaseId="+row.data().testCaseId+"&testRunPlanId="+testRunPlanId+"&testSuiteId="+row.data().testSuiteId+"&type=Remove";
		}else{
			 url="administration.testsuite.testcase.maptestrunplan?testCaseId="+row.data().testCaseId+"&testRunPlanId="+testRunPlanId+"&testSuiteId="+row.data().testSuiteId+"&type=Add";
		}
		$.ajax({
			type: "POST",
			url: url,
			async:false,
			success: function(data) {
				if(data.Result=='ERROR'){
					callAlert(data.Message);
					return false;
	 		    }else{
	 		    	callAlert(data.Message);
	 		    	listTestSuiteSelectedProductVersionPlan(row.data().productId, productVersionId, timestamp,type,testRunPlanId);
	 		    	return true;
	 		    }
	 		 },    
	 		 dataType: "json"
	 	});
	});
	
	$("#viewTestCases_dataTable_length").css('margin-top','8px');
	$("#viewTestCases_dataTable_length").css('padding-left','35px');
	
	$("#viewTestCases_dataTable_filter").css('margin-right','6px');
	
	viewTestCasesDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutViewTestCasesDT='';
function reInitializeViewTestCasesDT(){
	clearTimeoutViewTestCasesDT = setTimeout(function(){				
		viewTestCasesDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutViewTestCasesDT);
	},200);
}
//END: ConvertDataTable - ViewTestCases

function fetchProductVersionIdOfProductBuild(buildId){
	if(buildId != -1){
		var url = 'get.versionId.of.productBuild?productBuildId='+buildId;			
		$.ajax({
			type: "POST",
		    dataType : 'json',
			url : url,
			success: function(data) {
				var prodVersionIdData=eval(data);
				productVersionIdhidden = prodVersionIdData[0].versionId;
				if(productVersionIdhidden!=null){
					productVersionId  = productVersionIdhidden;					
				}						  	
			}
			});
	}else{
		callAlert("Please select the Product Build");
		return false;
	}
}

function environmentPlan(){
	 if(nodeType == "ProductVersion"){
	    	productVersionId = key;
	 }else{
		 if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == '')
		 {
			 if(document.getElementById("hdnProductVersionId") != null){
				 productVersionId = document.getElementById("hdnProductVersionId").value;
			 }else if(document.getElementById("treeHdnCurrentProductVersionId") != null 
				&& document.getElementById("treeHdnCurrentProductVersionId").value != ""){
				 productVersionId=document.getElementById("treeHdnCurrentProductVersionId").value;
			 }
			 if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == ''){
				//call url for product version of the build
					if(productBuildId !=  null || productBuildId !=0 || productBuildId !='null' || productBuildId != ''){
						fetchProductVersionIdOfProductBuild(productBuildId);
						productVersionId = productVersionId;
						console.log('version hdnProductVersionId---'+productVersionId);
					}else{
						callAlert("Please select the Product Build or Product Version");
						return false;
					} 
			 }
				
				
		}	
	 }
	
//	if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == ''){
//		callAlert("Please select the Product Version");
//		return false;
//	}	
	var date = new Date();
	var timestamp = date.getTime();    	
	urlToGetEnvironmentCombinationByProduct = "environment.combination.list.byProductId?productVersionId="+productVersionId+"&productId=-1&workpackageId=-1&testRunPlanId=-1";
	listEnvCombinationByProductPlan(-1,productVersionId);	
}

/*function testBeds(){
	 try{
		 if ($('#example').length>0) {
			 $("#TargTestPlanBeds").empty();
		 }
	 } catch(e) {}
	 
	var content = '<table id="example" style="font-size: 12px;" class="display" cellspacing="0" width="100%">';
		 content+= '<thead><tr></tr></thead>';
	     content+= '<tfoot></tfoot></table>';
	$("#TargTestPlanBeds").append(content);	 

	var testRunPlanId=document.getElementById("hdnTestRunPlanId").value;
	 
	$.ajax({
	    type: "POST",
	    url : "administration.testrunplan.testbed?testRunPlanId="+testRunPlanId,
	    cache:false,
	    success: function(data) {
	    	var data1=eval(data);
	    	//{"COLUMNS":[{"title":"TestSuite"},{"title":"TestCase"},{"title":"Chrome~DS-54BEF757DE0D"}],"DATA":["TS1","ts1","Chrome~DS-54BEF757DE0D"]}
	    	//data=eval('[{"COLUMNS":[{"title":"TESTSUITE"},{"title":"TESTCASE"}],"DATA":[["1","1"],["2","2"]]}]');
	  			
	  		columnData=data1[0].DATA;
	  		var cols = data1[0].COLUMNS;
	  		//console.log(data1[0].COLUMNS);	  		
	  	
	   			$('#example').dataTable({
	   		        "bJQueryUI": true,
	   		        "bDeferRender": true,
	   		        "bInfo" : false,
	   		        "bSort" : false,
	   		     "scrollY":"100%",
	   		        paging: true,	   		     	
	   		        destroy: true,
	   		        //"bDestroy" : true,
	   		        "bFilter" : false,
	                "fnRowCallback": function( nRow, aData, iDisplayIndex) {
	                	$.each(aData, function(i,item){
	                		if(i>1)
			                	$('td:eq('+i+')',nRow).html('<input type="checkbox" selected/>');
	                	});	                
	                },
	   		        "bPagination" : false,
	   		        "aaData": columnData,
	   		        "aoColumns": cols
	   		    });	   			
	  			 $("div.toolbar").html('<b>Test Beds Distribution</b>');	  			
	  		}	   	
	});
}*/

function listOfFeatureProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId){
	try{
		if ($('#jTableContainerfeaturetestplan').length>0) {
			 $('#jTableContainerfeaturetestplan').jtable('destroy'); 
		}
		}catch(e){}
		//init jTable
		 $('#jTableContainerfeaturetestplan').jtable({
	         title: 'Feature',
	         selecting: true, //Enable selecting	
	 		multiselect : false, //Allow multiple selecting
	 	 		selectingCheckboxes : false, //Show checkboxes on first column
	         paging: true, //Enable paging
	         pageSize: 10, //Set page size (default: 10)
	         editinline:{enable:false},	         
	         actions: {
	             listAction: 'administration.product.feature.list.testrunplan?productMasterId='+productId+'&productVersionId='+productVersionId+'&testRunPlanId='+testRunPlanId+'&type='+type, 
	         },
	        fields: {
		        productId: { 
		   				type: 'hidden',  
		   				list:false,
		   				defaultValue: productId
		   				}, 
		   				isSelected: {
		                     title: 'Selected',
		                     list:true,
		                     create: false,
		                     edit:true,
		                     type : 'checkbox',
		                       values: {'0' : 'No','1' : 'Yes'},
		                        display:function(data){
		                               if(data.record.isSelected==1){		                                    
		                                   return '<input type="checkbox" id="status" checked onclick=saveFeatureMap(0,'+data.record.productFeatureId+'); value='+data.record.status+'>';
		                                  }else if(data.record.isSelected==0){
		                                         return '<input type="checkbox" id="status" onclick=saveFeatureMap(1,'+data.record.productFeatureId+'); value='+data.record.status+'>';
		                                  }else{
		                                       return '<input type="checkbox" id="status" onclick=saveFeatureMap(1,'+data.record.productFeatureId+'); value='+data.record.status+'>';
		                                  }
		                         }
		        		 },

		   		productFeatureId: { 
		   				key: true,
		   				type: 'hidden',
		   				create: false, 
		   				edit: false, 
		   				list: false ,
		   				display: function (data) { 
		    				 return data.record.productFeatureId;
		                 } 
		   				}, 
   				productFeatureCode : {
	   				title: 'Feature Code',
	      	 		inputTitle: 'Feature Code <font color="#efd125" size="4px">*</font>',
	  	    		width:"20%",
	  	    		list: true,
					edit : true,
					create : true    	        	
	       			 },		
	   			productFeatureName: { 
		     	  		title: 'Feature Name',
		     	  		inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
		     	  		list:true,
		     	  		width: "20%",
		  			 	},
	  			displayName: { 
		  		     	title: 'Display Name',
		  		     	width: "20%",
		  		     	list:true,
		  		     	edit: false,
		  		     	create:false
		  		  	 	},
	  			productFeatureDescription: { 
		       			title: 'Feature Description' ,
		     		  	width: "20%",  
		     	  		list:true
		    	  		 },   				 
  				parentFeatureId:{
		            	title: 'Parent Feature',
		            	width:"20%",
		            	list:true,
		            	edit: true,
		            	create : true,
		            	inputTitle: 'Parent Feature <font color="#efd125" size="4px">*</font>',
		                dependsOn: 'productFeatureId',
						options: function (data) 
		                {	
		                	if(data.source =='list')
		                	{//	alert("Listingggggg");
		                		var atype='list';
		                		data.clearCache();
		                		return 'common.list.parentfeature.list?productID='+productId+'&productFeatureId=' + data.dependedValues.productFeatureId+'&actionType='+atype;
		                	} else if(data.source =='create'){
		                		var atype='create';
		                		data.clearCache();
		                		var childID = data.dependedValues.productFeatureId;
		                		if(childID != undefined){
		                			return 'common.list.parentfeature.list?productID='+productId+'&productFeatureId=' + data.dependedValues.productFeatureId+'&actionType='+atype;
		                		}else{
		                			return 'common.list.parentfeature.list?productID='+productId+'&productFeatureId=' +0+'&actionType='+atype;
		                		}
		                	}else if(data.source =='edit'){
		                		var atype='edit';
		                		data.clearCache();
		                		var childID = data.dependedValues.productFeatureId;
		                		if(childID != undefined){
		                			return 'common.list.parentfeature.list?productID='+productId+'&productFeatureId=' + data.dependedValues.productFeatureId+'&actionType='+atype;
		                		}else{
		                			return 'common.list.parentfeature.list?productID='+productId+'&productFeatureId=' +0+'&actionType='+atype;
		                		}
		                	}
		                	var atype="default";
		                	return 'common.list.parentfeature.list?productID='+productId+'&productFeatureId=' + 0+'&actionType='+atype;
		                }
			            },
	        	mappedTestCases: {
			        title:'',
			        width: "2%",
			        create:false,
			        edit:true,
			        display: function (childData) { 
	       				//Create an image that will be used to open child table 
			        	var $img = $('<img src="css/images/list_metro.png" title="Mapped TestCase List" />'); 
			        	//Open child table when user clicks the image 
			        	$img.click(function (data) { 
				        	$('#jTableContainerfeaturetestplan').jtable('openChildTable', 
				        	$img.closest('tr'), 
				        	{ 
				        	title: 'Mapped Test Cases',
				        	paging: true, //Enable paging
				            pageSize: 10, //Set page size (default: 10)
				            selecting: true, //Enable selecting 
				            editinline:{enable:false},					        	  	
				        	actions: { 
				        		 listAction: 'product.feature.testcase.mappedlist?productFeatureId='+childData.record.productFeatureId,
				        	     recordUpdated:function(event, data){
				        	        	$('#jTableContainerfeaturetestplan').find('.jtable-main-container').jtable('reload');
				        	        },
				        	     recordAdded: function (event, data){
				        	         	$('#jTableContainerfeaturetestplan').find('.jtable-main-container').jtable('reload');
				        	        },
				            },
				        	fields: {		      
					   	         productId:{
						 	        	type: 'hidden',  
						    				list:false,
						    				defaultValue: productId
						 	       			},
						             testCaseId: { 
						        			title: 'Test Case ID',
						         		create:false,
						         		edit: false,
						         		list:true,
						         		key: true
						        			},        	
						         	testCaseName: {
						                 title: 'Test Case Name ',
						                 inputTitle: 'Test Case Name <font color="#efd125" size="4px">*</font>',
						                 width: "12%",
						                 create : true,
						                 edit :true
						         	},
						         	testSuiteId:{
						         		title: 'Test Suite', 
						         		width: "7%",
						         		type : 'hidden',
						                 create: false,
						                 edit : false,
						                 list : false,
						         	},
						         	testSuiteName:{
						         		title: 'TestSuite Name', 
						         		width: "7%",                         
						                 create: false,
						                 list: false
						         	},
						         	testCaseDescription:{
						         		title: 'Test Case Description', 
						         		inputTitle: 'Test Case Description <font color="#efd125" size="4px">*</font>',
						         	    width: "40%",                         
						                 create: true   
						         	},
						         	testCaseSource:{
						         		title: 'Test Case Source', 
						         		width: "20%",                         
						                 create: false,
						                 list: false,
						                 edit: false
						         	},
						         	testCaseCode: { 
						         		title: 'Test Case Code',  
						         		inputTitle: 'Test Case Code <font color="#efd125" size="4px">*</font>',
						         		width: "10%",                        
						                 create: true
						         	},
						         	testCaseScriptQualifiedName:{
						         		title: 'Script Package Name',  
						         		width: "10%",
						         		create: true,
						         		list : true, 
						         		edit : false
						         	},
						         	testCaseScriptFileName:{
						         		title: 'Script File Name',  
						         		width: "10%",
						         		create: true,
						         		list : true, 
						         		edit : true
						         	}, 
						         	executionTypeId:{
						         		title : 'Execution Type',
						         		width : "10%",
						         		create : true,
						         		list : true,
						         		edit : true,
						       			options:function(data){
						       				if(data.source =='list'){	      				
						       					return 'common.list.executiontypemaster.byentityid?entitymasterid=3';	
						       				}else if(data.source == 'create'){	      				
						       					return 'common.list.executiontypemaster.byentityid?entitymasterid=3';
						       				}
						       			}
						         	}, 
						         	 testcasePriorityId:{
						 	        		title : 'Priority',
						 	        		width : "10%",
						 	        		create : true,
						 	        		list : true,
						 	        		edit : true,
						 	      			options:function(data){
						 	      				if(data.source =='list'){	      				
						 	      					return 'common.list.testcasepriority';	
						 	      				}else if(data.source == 'create'){	      				
						 	      					return 'common.list.testcasepriority';
						 	      				}
						 	      			}
						 	        	},
						 	        	testcaseTypeId:{
						 	        		title : 'Test Case Type',
						 	        		width : "13%",
						 	        		create : true,
						 	        		list : true,
						 	        		edit : true,
						 	      			options:function(data){
						 	      				if(data.source =='list'){	      				
						 	      					return 'common.list.testcasetype';	
						 	      				}else if(data.source == 'create'){	      				
						 	      					return 'common.list.testcasetype';
						 	      				}
						 	      			}
						 	        	},
						 	        	productFeatureId: {
						 		             title: 'Features',
						 		             width:"15%",
						 		             list:true,
						 		             edit: true,
						 		             create : true,
						 		             options:function(data){		      				
						 		            	 if(data.source =='list'){	      				
						 		      					return 'product.feature.list?productId='+productId;	
						 		      				}else if(data.source == 'create'){	      				
						 		      					return 'product.feature.list?productId='+productId;
						 		      				}
						 		      			}		            
						 		        },				         
							    	 },
					    	 // This is for closing $img.click(function (data) { 
							 //Moved Formcreate validation to Form Submitting
				         	 //Validate form when it is being submitted
				       		 formSubmitting: function (event, data) {
				        	  data.form.find('input[name="testCaseName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[minSize[3]], custom[maxSize[25]]');
				              data.form.find('input[name="testCaseDescription"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]');
				              data.form.find('input[name="testCaseCode"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[15]]');
				              data.form.validationEngine();
				             return data.form.validationEngine('validate');
				            }, 
				            //Dispose validation logic when form is closed
				           formClosed: function (event, data) {
				             data.form.validationEngine('hide');
				             data.form.validationEngine('detach');
				           }
						}, 
	        			function (data) { //opened handler 
			        	data.childTable.jtable('load'); 
			        	}); 
		        	}); 
		        		return $img; 
		        	},
		          	},	
		          	
	         },	          	
	       //Moved Formcreate validation to Form Submitting
	         //Validate form when it is being submitted
	          formSubmitting: function (event, data) {
	        	  data.form.find('input[name="productFeatureName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[minSize[3]]');
	              data.form.find('input[name="productFeatureCode"]').addClass('validate[required, custom[minSize[3]], custom[maxSize[20]]');
	              data.form.validationEngine();
	             return data.form.validationEngine('validate');
	         }, 
	          //Dispose validation logic when form is closed
	          formClosed: function (event, data) {
	             data.form.validationEngine('hide');
	             data.form.validationEngine('detach');
	         }
	     });		 
	$('#jTableContainerfeaturetestplan').jtable('load');		 
}

function callConfirmTestRunPlan(event){
	openLoaderIcon();
	setTimeout(function(){	callConfirmTestRunPlanHandler(event);return false;},1000);
}

function callConfirmTestRunPlanHandler(data){
	if(pageType=="HOMEPAGE" || data.type == 'click'){
		data.testRunPlanId=(data.target.id).split('~')[0];
		data.testRunPlanName=(data.target.id).split('~')[1];
		data.productId=(data.target.id).split('~')[2];
		data.productVersionId=(data.target.id).split('~')[4];
		data.executionTypeId=(data.target.id).split('~')[5];
		productVersionId=data.productVersionId;
		productId=data.productId;
		document.getElementById("hdnProductVersionId").value = productVersionId;
		tpcId = data.testRunPlanId;
	}else{
		isReady = data.isReady;
		isReadyMessage = data.message;
	}

	testRunPlan(data.testRunPlanId ,data.executionTypeId); 
	var jsonObj={};
	jsonObj.Title = "Test Plan : "+data.testRunPlanName;
	jsonObj.mode ="edit",
	jsonObj.testRunPlanId=data.testRunPlanId;
	jsonObj.testRunPlanName=data.testRunPlanName;
	jsonObj.productId=data.productId;
	jsonObj.productName=data.productName;
	jsonObj.productVersionId=data.productVersionId;		
	jsonObj.executionTypeId=data.executionTypeId;
	TestRunPlan.init(jsonObj);
}

function unattendedCallMode(testRunPlID){
	$("#progressStepsContainer").modal();
	var checkboxValues = [];
	var productName= document.getElementById("treeHdnCurrentProductName").value ;
	if(pageType=="HOMEPAGE")title=document.getElementById("treeHdnCurrentProductVersionName").value;
	var productVersionName=title ;
	var month=0;
	var date = new Date();
	if((date.getMonth()<=9)){
		month="0"+(date.getMonth()+1);
	}else{
		month=date.getMonth()+1;
	}
	testrunPlanNameDevice='';
	var timestamp = date.getFullYear()+"-"+month+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	var workpackageName=productName+'-'+productVersionName+'-'+testrunPlanNameDevice+'-'+timestamp;
	var description=workpackageName + " is created";
	document.getElementById("hdnTestRunPlanId").value=document.getElementById("hdnTestRunPlanDeviceId").value;
	document.getElementById("wpkg_name").value=workpackageName;
	document.getElementById("wpkg_desc").value=description;	
	
	//Calling new controller method
	//var testRunPlanId=document.getElementById("hdnTestRunPlanDeviceId").value;
	var workpackageName = document.getElementById("wpkg_name").value;
	var description ='';
	description = document.getElementById("wpkg_desc").value;
	var plannedStartDate=document.getElementById("plannedStartDate").value;
	var plannedEndDate=document.getElementById("plannedEndDate").value;
	var contextpath = (window.location.pathname).split("/", 2);
	var root = window.location.protocol + "//" + window.location.host + "/" + contextpath[1];
	var productBuildId='';
	
	productBuildId= $("#productBuild_ul").find(":selected").attr("id");// $("#productBuild_ul").find('option:selected').attr('id');	
	if(productBuildId==undefined){
		productBuildId=-1;
	}
	
	if(workpackageName== ""){
		callAlert("Please Enter Workpackage Name");
		return false;
	}
	
	var url = 'administration.workpackage.testrunplan.devices';
	var thediv = document.getElementById('reportbox'); 
	$.ajax({
	    type: "POST",
	    url: url,
	    dataType: "json",
	    data: { 'workpackageName': workpackageName, 'description': description, 'productBuildId': productBuildId,'plannedStartDate':plannedStartDate,'plannedEndDate':plannedEndDate ,'testRunPlanId':testRunPlID,'runconfigId':checkboxValues},
	    success: function(data) {
	    	//$.unblockUI();
	    	var msg = data.Message;	    	
	    	if(data.Result=='ERROR'){
	    		callAlert(msg);
 		    	return false;
	    	}else{
	    		//TODO : need to call mechanism to execute trp for the associated worpackage
				if(testCaseExecution == 'TestSuiteExecution'){
					checkboxValues = runConfigCheckBoxArrVal ;
					document.getElementById("hdnTestRunPlanDeviceId").value = testrunPlanIdForTestCaseExecution;
				}else{
					$("#div_PopupRunConfigurationList #run_configuration_list input:checked").each(function(){ 
						checkboxValues.push($(this).attr('id'));
					});
				}
				console.log("checkboxValues :"+checkboxValues);
				if(trpExecMode == "UnAttended")
					msg = msg.replace("Workpackage ID :","");
				else
					msg = msg.substring(0,msg.indexOf("[")).replace("Test Run Plan execution initiated. Workpackage ","");
				//$("#progressStepsContainer").modal();
				progressSteps(msg);
				testCasebyTestRunPlanIdForUnattended(msg, description, productBuildId, testRunPlID, checkboxValues.toString());				
	    	}
	    },
	    error: function(data){
	    	console.log("Error in updation");
	    },
	    complete: function(data){
	    	console.log("Complete in updation");
	    }
	});	   
}

function testCasebyTestRunPlanIdForUnattended(msg, description, productBuildId, testRunPlanId, checkboxValues){	
	var url='test.suite.case.testrunplan.unattended?testSuiteId=-1&testRunPlanId='+testRunPlanId+'&jtStartIndex=0&jtPageSize=10000&wpId='+msg;	
	$.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  data:{'wpId': msg},
		  success : function(data) {					
			if(data == null || data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}			
		  },
		  error : function(data) {
			 
		  },
		  complete: function(data){	
			
		  }
	});
}

function progressSteps(wpId) {	
	//Keep retrieving the workpackage status and update it in the status template tracker UI component
	var url = 'workpackage.summary?wpId='+wpId;
	$.ajax({
	    type: "POST",
	    url: url,
	    dataType: "json",	    
	    success: function(data) {	    	
	    	var msg = $.parseJSON(data);
	    	var flag = false;
	    	if(data.Result=='ERROR'){	    		
 		    	return false;
	    	}else{
	    		ProgressSteps.init(msg);
    			setTimeout(function(){
	    			if($('#progressStepsContainer').is(':visible')){
	    				progressSteps(wpId);
	    			}
    			},5000);
	    	}
	    },
	    error: function(data){
	    	
	    }
	}); 
}

/* DragDropListItem Plugin started */ 		 
var jsonFeatureTabObj='';
var jsonTestCaseTabObj = '';
var jsonCompetencyObj = '';
var jsonWorkflowStatusObj = '';
var jsonRiskTCTabObj = '';
var jsonRiskFeatureTabObj = '';
var jsonRiskMitigationTabObj = '';
var jsonUserMappingForGroupObj = '';
var jsonTestCaseScriptObj = '';
var jsonTestCaseListObj = '';

function leftDraggedItemURLChanges(value,type){
	if(type==jsonFeatureTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
		    result ='&testcaseId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonTestCaseTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
			result ='&productFeatureId='+itemid+''+leftDragUrlConcat;
	} 
	else if(type == jsonCompetencyObj.componentUsageTitle){
	var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
	    x =  value.split("("),
	    itemid = x[0],	
		result ='&dimensionId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonWorkflowStatusObj.componentUsageTitle){
	var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
	    x =  value.split("("),
	    itemid = x[0],	
		result ='&dimensionId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonRiskTCTabObj.componentUsageTitle){
	var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
	    x =  value.split("("),
	    itemid = x[0],	
		result ='&testcaseId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonRiskFeatureTabObj.componentUsageTitle){
	var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
	    x =  value.split("("),
	    itemid = x[0],	
		result ='&productFeatureId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonRiskMitigationTabObj.componentUsageTitle){
	var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
	    x =  value.split("("),
	    itemid = x[0],	
		result ='&riskMitigationId='+itemid+''+leftDragUrlConcat;
	} 
	else if(type == jsonTestDataObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
	    x =  value.split("("),
	    itemid = x[0],	
		result ='&attachmentId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonUserMappingForGroupObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
			result ='&userId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonTestCaseScriptObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
			result ='&testCaseId='+itemid+''+leftDragUrlConcat;
		}
	else if(type == jsonTestCaseListObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
			result ='&scriptId='+itemid+''+leftDragUrlConcat;
		}
	else if(type == jsonTestScenarioObj.componentUsageTitle){
	var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&testCaseId='+itemid+''+leftDragUrlConcat;
	}
	return result;
}

function rightDraggedItemURLChanges(value,type){
	result='';
	if(type==jsonFeatureTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",
		    x =  value.split("("),
		    itemid = x[0],		
		    result ='&testcaseId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonTestCaseTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
			result ='&productFeatureId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonCompetencyObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
			result ='&dimensionId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonWorkflowStatusObj.componentUsageTitle){
	var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
	    x =  value.split("("),
	    itemid = x[0],	
		result ='&dimensionId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonRiskTCTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
			result ='&testcaseId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonRiskFeatureTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
			result ='&productFeatureId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonRiskMitigationTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
			result ='&riskMitigationId='+itemid+''+rightDragtUrlConcat;
	} 
	else if(type == jsonTestDataObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
			result ='&attachmentId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonUserMappingForGroupObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
		    result ='&userId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonTestCaseScriptObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
			result ='&testCaseId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonTestCaseListObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
			result ='&scriptId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonTestScenarioObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
			result ='&testCaseId='+itemid+''+rightDragtUrlConcat;
	}
	return result;
}

function leftItemDislayListItem(item, jsonObj){
	var resultList="";
	var entity_id = item.itemId;
	var entity_name = item.itemName;	
	var entity_dispname = ''
		
	if(jsonObj.componentUsageTitle==jsonFeatureTabObj.componentUsageTitle){
		var entity_code = item.itemCode;
		if(entity_code == null){
			entity_dispname = entity_id+" ("+entity_name+")";
		}else{
			entity_dispname = entity_id+" ("+entity_code+")"+" ("+entity_name+")";
		}		
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";	
		}
	else if(jsonObj.componentUsageTitle == jsonTestCaseTabObj.componentUsageTitle){	
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonCompetencyObj.componentUsageTitle){	
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonWorkflowStatusObj.componentUsageTitle){	
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonRiskTCTabObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonRiskFeatureTabObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonRiskMitigationTabObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	} else if(jsonObj.componentUsageTitle == jsonTestDataObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}else if(jsonObj.componentUsageTitle == jsonUserMappingForGroupObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonTestCaseScriptObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonTestCaseListObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonTestScenarioObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	
	return resultList;	
}

function rightItemDislayListItem(item, jsonObj){
	var resultList="";
	var entity_id = item.itemId;	
	var entity_name = item.itemName;
	var entity_dispname = ''
		
	if(jsonObj.componentUsageTitle==jsonFeatureTabObj.componentUsageTitle){
		 var entity_code = item.itemCode;
		 if(entity_code == null){
				entity_dispname = entity_id+" ("+entity_name+")";
			}else{
				entity_dispname = entity_id+" ("+entity_code+")"+" ("+entity_name+")";
			}		
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonTestCaseTabObj.componentUsageTitle){	
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}	
	else if(jsonObj.componentUsageTitle == jsonCompetencyObj.componentUsageTitle){	
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);	
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonWorkflowStatusObj.componentUsageTitle){	
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle==jsonRiskTCTabObj.componentUsageTitle){
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle==jsonRiskFeatureTabObj.componentUsageTitle){
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle==jsonRiskMitigationTabObj.componentUsageTitle){
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	} else if(jsonObj.componentUsageTitle==jsonTestDataObj.componentUsageTitle){
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}else if(jsonObj.componentUsageTitle==jsonUserMappingForGroupObj.componentUsageTitle){
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle==jsonTestCaseScriptObj.componentUsageTitle){
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle==jsonTestCaseListObj.componentUsageTitle){
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle==jsonTestScenarioObj.componentUsageTitle){
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	
	return resultList;
}
/* DragDropListItem Plugin ended */
