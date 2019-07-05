var TestCaseExecutionDetails = function() {
  
   var initialise = function(jsonObj){
	   listTestCaseExecutionSummaryHistory(jsonObj);
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

function listTestCaseExecutionSummaryHistory(jsonObj){	
	$("#div_PopupTestCaseExecutionDetails").find("h4").text(jsonObj.Title);
	
	if(jsonObj.componentUsageTitle == "defectDetails"){
		listDefectPopupSummaryTable(jsonObj);	
		loadDefectPopupInfo(jsonObj);
	}else if(jsonObj.componentUsageTitle == "workPackageLevel"){
		executionSummaryDataTableInit(jsonObj);
		executionHistoryDataTableInit(jsonObj);
	}else{
		executionSummaryDataTableInit(jsonObj);
		executionHistoryDataTableInit(jsonObj);
	}
	$("#div_PopupTestCaseExecutionDetails").modal();	
}

//BEGIN: ConvertDataTable - ExecutionSummary
var dataObj;
function executionSummaryDataTableInit(jsonObj){
	dataObj = jsonObj;
	jsonObj.url= jsonObj.testCaseExectionSummayUrl +'&jtStartIndex=0&jtPageSize=100';
	executionSummaryDataTableContainer.init(jsonObj);
}

var executionSummaryDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		assignExecutionSummaryDataTableValues(jsonObj, "SummaryTable");
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignExecutionSummaryDataTableValues(jsonObj, tType){
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
			if(tType == "SummaryTable"){
				executionSummaryDT_Container(jsonObj);
			}else if(tType == "HistoryTable"){
				executionHistoryDT_Container(jsonObj);
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
var executionSummaryDT_oTable='';
var editorExecutionSummary='';
function executionSummaryDataTableHeader(){
	var childDivString ='<table id="executionSummary_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Product Version</th>'+
			'<th>Product Build Name</th>'+
			'<th>Pass</th>'+
			'<th>Pass %</th>'+
			'<th>Fail</th>'+
			'<th>Fail %</th>'+
			'<th>No Run</th>'+
			'<th>No Run %</th>'+
			'<th>Blocked</th>'+
			'<th>Blocked %</th>'+
			'<th>Executed</th>'+
			'<th>Executed %</th>'+
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
function executionSummaryDT_Container(jsonObj){	
	try{
		if ($("#dataTableContainerForExecutionSummary").children().length>0) {
			$("#dataTableContainerForExecutionSummary").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = executionSummaryDataTableHeader(); 			 
	$("#dataTableContainerForExecutionSummary").append(childDivString);
	
	editorExecutionSummary = new $.fn.dataTable.Editor( {
	    "table": "#executionSummary_dataTable",
		ajax: "",
		ajaxUrl: "",
		idSrc:  "",
		i18n: {
	        create: {
	            title:  "Create a new Execution Summary",
	            submit: "Create",
	        }
	    },
		fields: []
	});
	
	executionSummaryDT_oTable = $("#executionSummary_dataTable").dataTable( {				 	
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
	     		  $('#executionSummary_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeExecutionSummaryDT();
			   },  
		   buttons: [
						//{ extend: "create", editor: editorExecutionSummary },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Execution Summary',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Execution Summary',
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
           { mData: "productVersionName", className: 'disableEditInline', sWidth: '15%' },	
           { mData: "productBuildName", className: 'disableEditInline', sWidth: '15%' },		
           { mData: "totalPass",  className: 'disableEditInline', sWidth: '7%' },
           { mData: null, className: 'disableEditInline', sWidth: '7%',
	       		mRender: function (data, type, full) {
		           	var totVal = full.totalBlock + full.totalFail + full.totalNoRun + full.totalPass + full.notExecuted;
	            	var data = Math.round((full.totalPass / totVal) * 100);
		            return data;
	            },
           },
           { mData: "totalFail",  className: 'disableEditInline', sWidth: '7%' },
           { mData: null, className: 'disableEditInline', sWidth: '7%',
	       		mRender: function (data, type, full) {
		           	var totVal = full.totalBlock + full.totalFail + full.totalNoRun + full.totalPass + full.notExecuted;
	            	var data = Math.round((full.totalFail / totVal) * 100);
		            return data;
	            },
           },
           { mData: "totalNoRun",  className: 'disableEditInline', sWidth: '7%' },
           { mData: null, className: 'disableEditInline', sWidth: '7%',
	       		mRender: function (data, type, full) {
		           	var totVal = full.totalBlock + full.totalFail + full.totalNoRun + full.totalPass + full.notExecuted;
	            	var data = Math.round((full.totalNoRun / totVal) * 100);
		            return data;
	            },
           },
           { mData: "totalBlock",  className: 'disableEditInline', sWidth: '7%' },
           { mData: null, className: 'disableEditInline', sWidth: '7%',
	       		mRender: function (data, type, full) {
		           	var totVal = full.totalBlock + full.totalFail + full.totalNoRun + full.totalPass + full.notExecuted;
	            	var data = Math.round((full.totalBlock / totVal) * 100);
		            return data;
	            },
           },
           { mData: "notExecuted",  className: 'disableEditInline', sWidth: '7%' },           
           { mData: null, className: 'disableEditInline', sWidth: '7%',
	       		mRender: function (data, type, full) {
		           	var totVal = full.totalBlock + full.totalFail + full.totalNoRun + full.totalPass + full.notExecuted;
	            	var data = Math.round((full.notExecuted / totVal) * 100);
		            return data;
	            },
           },
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorExecutionSummary-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#executionSummary_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorExecutionSummary.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#executionSummary_dataTable_length").css('margin-top','8px');
	$("#executionSummary_dataTable_length").css('padding-left','35px');		
	
	executionSummaryDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutExecutionSummaryDT='';
function reInitializeExecutionSummaryDT(){
	clearTimeoutExecutionSummaryDT = setTimeout(function(){				
		executionSummaryDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutExecutionSummaryDT);
	},200);
}
//END: ConvertDataTable - ExecutionSummary

//BEGIN: ConvertDataTable - ExecutionHistory
function executionHistoryDataTableInit(jsonObj){
	 url= jsonObj.testCaseExectionHistoryUrl +'&jtStartIndex=0&jtPageSize=100';
	 jsonObj={"Title": jsonObj.testCaseExectionHistoryTitle,
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle": url,
	};
	executionHistoryDataTableContainer.init(jsonObj);
}

var executionHistoryDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		assignExecutionSummaryDataTableValues(jsonObj, "HistoryTable");
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

var executionHistoryDT_oTable='';
var editorExecutionHistory='';
function executionHistoryDataTableHeader(){
	var childDivString ='<table id="executionHistory_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Execution Id</th>'+
			'<th>Environment</th>'+
			'<th>Device</th>'+
			'<th>Execution Type</th>'+
			'<th>Status</th>'+
			'<th>Execution Date</th>'+
			'<th>Test Job</th>'+
			'<th>Product</th>'+
			'<th>Product Version</th>'+
			'<th>Build</th>'+
			'<th>Workpackage Id</th>'+
			'<th>Workpackage</th>'+
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
function executionHistoryDT_Container(jsonObj){	
	try{
		if ($("#dataTableContainerForExecutionHistory").children().length>0) {
			$("#dataTableContainerForExecutionHistory").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = executionHistoryDataTableHeader(); 			 
	$("#dataTableContainerForExecutionHistory").append(childDivString);
	
	editorExecutionHistory = new $.fn.dataTable.Editor( {
	    "table": "#executionHistory_dataTable",
		ajax: "",
		ajaxUrl: "",
		idSrc:  "",
		i18n: {
	        create: {
	            title:  "Create a new Execution History",
	            submit: "Create",
	        }
	    },
		fields: []
	});
	
	executionHistoryDT_oTable = $("#executionHistory_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "90%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,
	       "bSort": false,
	       //"aaSorting": [[4,'desc']],
	       
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     		  $('#executionHistory_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeExecutionHistoryDT();
			   },  
		   buttons: [
						//{ extend: "create", editor: editorExecutionHistory },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Execution History',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Execution History',
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
           { mData: "testCaseExecutionResultId", className: 'disableEditInline', sWidth: '7%',
	           	mRender: function (data, type, full) {
					var tcexeId = full.testCaseExecutionResultId;
					var tcId = dataObj.testCaseID;
					var tcName = dataObj.testCaseName;
					var result = tcexeId+'~@'+tcId+'~@'+tcName;		
            		data = ('<a style="color: #0000FF;" id='+result+' onclick="testCaseDetailsForResult(event)">'+data+'</a>');
		            return data;
            	},
           },    	
           { mData: "environmentCombinationName", className: 'disableEditInline', sWidth: '7%' },		
           { mData: "deviceName",  className: 'disableEditInline', sWidth: '7%' },
           { mData: "exeType",  className: 'disableEditInline', sWidth: '7%' },
           { mData: "result",  className: 'disableEditInline', sWidth: '7%' },
           { mData: "actualExecutionDate",  className: 'disableEditInline', sWidth: '7%' },
           { mData: "testRunJobId",  className: 'disableEditInline', sWidth: '7%' },           
           { mData: "productName", className: 'disableEditInline', sWidth: '7%' },	
           { mData: "productVersionName", className: 'disableEditInline', sWidth: '7%' },		
           { mData: "productBuildName",  className: 'disableEditInline', sWidth: '7%' },
           { mData: "workPackageId",  className: 'disableEditInline', sWidth: '7%' },
           { mData: "workPackageName",  className: 'disableEditInline', sWidth: '23%' },
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorExecutionHistory-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#executionHistory_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorExecutionHistory.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#executionHistory_dataTable_length").css('margin-top','8px');
	$("#executionHistory_dataTable_length").css('padding-left','35px');		
	
	executionHistoryDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutExecutionHistoryDT='';
function reInitializeExecutionHistoryDT(){
	clearTimeoutExecutionHistoryDT = setTimeout(function(){				
		executionHistoryDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutExecutionHistoryDT);
	},200);
}
//END: ConvertDataTable - ExecutionHistory

function listDefectPopupSummaryTable(jsonObj){	
	try{
		if ($("#JtableTestCaseExecutionSummary").length>0) {
			 $('#JtableTestCaseExecutionSummary').jtable('destroy');
		}
	} catch(e) {}
    $('#JtableTestCaseExecutionSummary').jtable({
		title: jsonObj.testCaseDefectsTitle,
        selecting: true,  //Enable selecting 
     //   editinline : {enable : true},
        actions: {
       	 	listAction: jsonObj.testCaseExectionSummayUrl
        },  
        fields : {
             productVersionName: {
                 title: 'Product Version',
                 create:false,
                 edit:false,
                 list:true,
                 width: "8%"
 			 },
 			openDefects: {
                key: true,
                title: 'Open',
                create:false,
                edit:false,
                list:true,
                width: "8%"
             },
             reviewedDefects: {
                 title: 'Reviewed',
                 create:false,
                 edit:false,
                 list:true,
                 width: "8%"
 			},
 			referBackDefects: {
                 title: 'Refer Back',
                 create:false,
                 edit:false,
                 list:false
 			},
 			approvedDefects: {
                 title: 'Approved',
                 create:false,
                 edit:false,
                 list:true,
                 width: "8%"
 			},
 			closedDefects: {
                 title: 'Closed',
                 create:false,
                 edit:false,
                 list:true,
                 width: "8%"
 			},
        	}
		});
	$('#JtableTestCaseExecutionSummary').jtable('load'); 
}

//Defect Details
function loadDefectPopupInfo(jsonObj){	
	try{
		if ($('#JtableTestCaseExecutionHistory').length>0) {
	
			 $('#JtableTestCaseExecutionHistory').jtable('destroy'); 
		}
	} catch(e) {}
	$('#JtableTestCaseExecutionHistory').jtable({
       title: 'Defect Details',
        selecting: true,  //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, 
          actions: {
        	listAction: jsonObj.testCaseExectionHistoryUrl,
        	editinlineAction: 'defects.for.analyse.update'
        },  
        fields : {
        	testExecutionResultBugId: { 
        		title: 'Bug Id',
        		width: "5%",         
                list: true	,
                create:false,
                edit:false
            },
        	bugTitle: { 
        		title: 'Bug Title',  
        		width: "20%",                          
                create: true,
                list:true,
                edit:false 
        	},
        	testCaseExecutionResultId: { 
	        		title: 'id',
	        		width: "7%",         
	                list: false	,
	                create:false,
	                edit:false
	            },
        	bugDescription:{
        		title: 'Description',  
        		width: "30%",                          
	                list:true,
	                edit:false 
        	},        	
        	testcaseId:{
        		title: 'Testcase Id',  
        		width: "7%",                          
	                list:true,
	                edit:false 
        	},
        	testcaseName:{
        		title: 'Testcase Name',  
        		width: "15%",                          
	                list:true,
	                edit:false 
        	},
            bugFilingStatusId: { 
        		title: 'Filing Status',
        		width: "5%",         
        		 create: true,
	                list:true,
	                edit:false ,
	                options : 'administration.workFlow.list?entityType=1'
            },
            severityId:{		        		 
        		title: 'Severity',
        		width: "7%",         
        		 create: true,
	                list:true,
	                edit:false,
	                options : 'common.list.defectSeverity'            
        	},
            remarks: { 
        		title: 'Remarks',
        		width: "7%",         
        		 create: true,
	                list:true,
	                edit:false 						                
            },
            userId:{
            	title: 'Raised By', 
        		width: "10%",                         
        		 create: true,
	                list:true,
	                edit:false          
            },
           bugCreationTime: { 
        		title: 'Creation Time',
        		width: "7%",         
        		 create: false,
	                list:true,
            } ,
            bugManagementSystemName: { 
        		title: 'System Name', 
        		width: "20%",                         
        		 create: false,
	                list:false,
	                edit:false 
           },
           bugManagementSystemBugId: { 
        		title: 'System Bug Id',  
        		width: "25%",                        
        		 create: false,
	                list:false,
	                edit:false 
            },
            fileBugInBugManagementSystem: { 
        		title: 'File Bug',  
        		width: "7%",         
        		 create: false,
	                list:false,
	                edit:false 
            },
              exportDefects1: { 
              	title: '',
              	width: "15%",
              	edit: false,
              	list:true,
              	display:function (defData) { 
           			var $img = $('<img src="css/images/list_metro.png" title="DefectSystem" class="showHandCursor" style="width:16px;height:16px;" />'); 
           			//Open child table when user clicks the image 
           			$img.click(function (data) { 
			        	$('#JtableTestCaseExecutionHistory').jtable('openChildTable', 
					        	$img.closest('tr'), 
					        	{ 
					        	title: 'Defect System Details',
					        	paging: true, //Enable paging
					            pageSize: 10, //Set page size (default: 10)
					            selecting: true, //Enable selecting 
					        	actions: {
					        		listAction: 'defect.system.name.code.list?testExecutionResultsBugId='+defData.record.testExecutionResultBugId,
									recordUpdated:function(event, data){
					        	        	$('#JtableTestCaseExecutionHistory').find('.jtable-main-container').jtable('reload');
					        	        },
					        	     recordAdded: function (event, data){
					        	         	$('#JtableTestCaseExecutionHistory').find('.jtable-main-container').jtable('reload');
					        	        },
					            },
					        	fields: {		      
						   	            defectSystemName: {
							                 title: 'Defect System Name ',
							                 inputTitle: 'Defect System Name <font color="#efd125" size="4px">*</font>',
							                 width: "12%",
							                 create : true,
							                 edit :true ,
							         	},
							         	defectSystemCode: {
							                 title: 'Defect System Code ',
							                 inputTitle: 'Defect System Code <font color="#efd125" size="4px">*</font>',
							                 width: "12%",
							                 create : true,
							                 edit :true ,
							         	},	
							         	defectExportDate: {
							                 title: 'Defect Export Date ',
							                 inputTitle: 'Defect Export Date <font color="#efd125" size="4px">*</font>',
							                 width: "12%",
							                 create : true,
							                 edit :true ,
							         	},
								    	},
						    	 // This is for closing $img.click(function (data) { 							
							}, 
		        			function (data) { //opened handler 
				        	data.childTable.jtable('load'); 
				        	}); 
			        	});  
           			return  $img;			
	        	}            
              },              
            defectTypeId: { 
        		title: 'Defect Type',  
        		width: "7%",  
        		edit:false,
	            options:'common.list.defect.types.list'
            },
            defectIdentifiedInStageId: { 
        		title: 'Found In',  
        		width: "7%",  
        		edit:false,
	            options:'common.list.defect.identification.stages.list'
            },            
            reportedInBuildId: { 
        		title: 'Reported Build',  
        		width: "15%",  
        		edit:false,
        		list:true,
	            options:'process.list.builds.by.product?productId='+prodId
            },            
            fixedInBuildId: { 
        		title: 'Fixed Build',  
        		width: "15%",  
        		edit:true,
        		list:true,
        		  options:'process.list.builds.by.product?productId='+prodId
            },
            sourceFilesModifiedForFixing: { 
        		title: 'Source File Modified',  
        		width: "10%",  
        		edit:true,
        		list:true,
            },
        },  // This is for closing $img.click(function (data) {  
	      
		});
	 $('#JtableTestCaseExecutionHistory').jtable('load');
}
