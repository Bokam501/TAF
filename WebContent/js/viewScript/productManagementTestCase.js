var productTestCaseOptionsArr=[];
var productTestCaseResultArr=[];
var productTestCaseOptionsItemCounter=0;
var testStepTestcaseId=0;
var optionsType_ProductTestCase="ProductTestCase";
var testCase_editor='';
var objectRepositoryId = -1;
var testDataId = -1;
var atsgId = -1;
var totalRepository = 0;
var totalTestData = 0;
var projectName;
var testMethodSingleInBDD ;
var testClasssingle;
var testFilterId = 0 ;
var testCaseStatus = -1 ;
var statusListingFlag = true;
var ATTModeEnabled = true;
var currentUserIdentity='';
var myTestcasesDT_oTable='';
var editorTestcases='';
var pageName = "MYTESTCASES";
var testCaseIdArr = [];
var testEngineArr = [];
var productType = '';
var loginUser='';
var atsgBtn = ''; 
var testCaseChecked = false;
var atsgEditorTimer;

//-- For DataTable Pagination -----
var defaultPageCountSelectedForTestCase=1000;
var totalTestCaseList = 999;

function testcases(){		
	$("#uploadFileTestCase").css("display","none");
	$("#uploadFileTestSteps").css("display","none");
	$("#testCaseGroup").css("display","none");
	setProductNode();
	
	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#Testcases').children().show();		
		//var status = $("#testCaseStatusFilter_ul").find('option:selected').val();
		//urlToGetTestCasesOfSpecifiedProductId = 'product.testcase.list?productId='+productId+'&status='+status+'&jtStartIndex=0&jtPageSize=10000';		
		//listTestCasesOfSelectedProduct(urlToGetTestCasesOfSpecifiedProductId, "100%", "parentTable", "", "");
		
		alltestCaseDT(0, defaultPageCountSelectedForTestCase);
	}	
}

function alltestCaseDT(index, pageCountSelected){
	var rowCount = pageCountSelected;			
	var totalCurrentCount=0;
	var currentPageNo=0;
	var page = Number($(productMgtTestCasePaginationDT).find('span.current').text());			
	currentPageNo=page;
	
	$("#productMgtTestCasePaginationContainerDT").show();
	if(totalTestCaseList<defaultPageCountSelectedForTestCase){
		$("#productMgtTestCasePaginationContainerDT").hide();
	}
		
	if(page==0){
		currentPageNo=1;
	}
	
	Paging[0] = $("#productMgtTestCasePaginationContainerDT .toppagination").paging(totalTestCaseList, {								                    
	onSelect: function(page) {					
		currentPageNo=page;
		if(totalTestCaseList < (rowCount*currentPageNo)){
			totalCurrentCount = totalTestCaseList;
		}else{
			totalCurrentCount = (rowCount*page);
		}
		$("#productMgtTestCaseBadgeDT").text('Showing '+((rowCount*page)-rowCount+1)+' to '+totalCurrentCount+' of '+totalTestCaseList+' entries');		
		openLoaderIcon();
		
		var status = $("#testCaseStatusFilter_ul").find('option:selected').val();
		var urlX = 'product.testcase.list?productId='+productId+'&status='+status;		
		var url = urlX+"&jtStartIndex="+((rowCount*page)-rowCount)+"&jtPageSize="+rowCount;		
		listTestCasesOfSelectedProduct(url, "100%", "parentTable", "", "");		
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

$('#testCases_dataTable').on( 'length.dt', function ( e, settings, len ) {
    adjustSideBarTestCaseHeight();		
} );

$('#testCases_dataTable').on( 'page.dt', function () {
	 adjustSideBarTestCaseHeight();		
} );

var clearTimeoutPaginationWKDT='';
function adjustSideBarTestCaseHeight(){	
	clearTimeoutPaginationWKDT =setTimeout(function(){				
		adjustSideBarHeightDT();
	},1000);
}
	
function adjustSideBarHeightDT(){
	var pageHeight = $('.page-container').css('height');
	$('.page-quick-sidebar-wrapper').css('height', pageHeight);
}

function setDropDownProductMgtTestCaseDT(value){
	var pageSize=0;
	var starting = 0;
	var ending = 0;
	var currentPageNo=0;
	var pageNo = Number($(productMgtTestCasePaginationDT).find('span.current').text());
	if ((value*pageNo) > totalTestCaseList){
		currentPageNo = 0;
		pageSize = (value*pageNo);
		$('#selectProductMgtTestCaseCountDT').find('option[value="'+pageSize+'"]').attr("selected", true);
		
	}else{
		currentPageNo = ((value*pageNo)-value);		
		pageSize = (value*pageNo);
	}	
	alltestCaseDT(currentPageNo, pageSize);
}

// ----- ended -----


function loadTestcaseMetirics(testcaseId,testcaseName){
	$('#div_PopupTestcaseAnalytics').modal();
	
	var testCaseTitle="";
	if(testcaseName.length > 25){         		
		testCaseTitle = (testcaseName).toString().substring(0,20)+'...';         		
 	} else {
 		testCaseTitle =testcaseName;
 	}
	
	$("#div_PopupTestcaseAnalytics .modal-header h4").text("Testcase Analytics: ["+testcaseId+"] "+testCaseTitle);
	$("#div_PopupTestcaseAnalytics .modal-header h4").attr('title',testcaseName);
	/*$("#div_PopupTestcaseAnalytics .green-haze").text("Testcase Analytics");*/
	
	
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : 'testcase.metrics.details?testCaseId='+testcaseId,
		dataType : 'json',
		success : function(data) {
			var result=data.Records;
			var testcaseSuccessRate='';
			var featureCoverage='';
			var buildCoverage='';
			var testCaseAvgExecutionTime='';
			var testCaseAge='';
			var testcaseQualityIndex='';
			var testCasePercentage='';
			$("#div_PopupTestcaseAnalytics #testcaseSuccessRate").empty();
			$("#div_PopupTestcaseAnalytics #featureCoverage").empty();
			$("#div_PopupTestcaseAnalytics #buildCoverage").empty();
			$("#div_PopupTestcaseAnalytics #testCaseAvgExecutionTime").empty();
			$("#div_PopupTestcaseAnalytics #testCaseAge").empty();
			$("#div_PopupTestcaseAnalytics #testcaseQualityIndex").empty();

			
			$("#div_PopupTestcaseAnalytics #testCasePercentage").empty();
			
			
			$("#div_PopupTestcaseAnalytics #testcaseSuccessRate").text('--');
			$("#div_PopupTestcaseAnalytics #featureCoverage").text('--');
			$("#div_PopupTestcaseAnalytics #buildCoverage").text('--');
			
			$("#div_PopupTestcaseAnalytics #testCaseAvgExecutionTime").text('0');
			$("#div_PopupTestcaseAnalytics #testCaseAge").text('--');
			$("#div_PopupTestcaseAnalytics #testcaseQualityIndex").text('0');
			$("#div_PopupTestcaseAnalytics #testCasePercentage").text('0/0[0%]');
			$("#div_PopupTestcaseAnalytics #testcasePriority").text('--');
	
			
			$.each(result, function(i,item){				
				if(item.testcaseSuccessRate != null){
					$("#div_PopupTestcaseAnalytics #testcaseSuccessRate").text(item.testcaseSuccessRate);		
				}
				if(item.featureCoverage != null){
					$("#div_PopupTestcaseAnalytics #featureCoverage").text(item.featureCoverage);	
				}
				if(item.buildCoverage != null){
					$("#div_PopupTestcaseAnalytics #buildCoverage").text(item.buildCoverage);
				}
				if(item.testCaseAvgExecutionTime != null){
					$("#div_PopupTestcaseAnalytics #testCaseAvgExecutionTime").text(item.testCaseAvgExecutionTime);
				}
				if(item.testCaseAge != null){
					$("#div_PopupTestcaseAnalytics #testCaseAge").text(item.testCaseAge);	
				}
				if(item.testcaseQualityIndex != null){
					$("#div_PopupTestcaseAnalytics #testcaseQualityIndex").text(item.testcaseQualityIndex);
				}
				if(item.testCasePercentage != null){
					$("#div_PopupTestcaseAnalytics #testCasePercentage").text(testCasePercentage);
				}				
				
				
			});
		}
	});	
	
}

function listTestCasesOfSelectedProduct(url, ScrollValue, tableValue, row, tr){
	openLoaderIcon();
	
	//if(tableValue == "parentTable")
		//$("#addCommentsLoaderIcon").show();	
	
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			//$("#addCommentsLoaderIcon").hide();
			if(data == null || data.Result=="ERROR"){
      		    data = [];						
			}else{
				totalTestCaseList = data.TotalRecordCount;
				data = data.Records;
			}
			
			if(tableValue == "parentTable"){										
				productTestCaseResults_Container(data, ScrollValue, row, tr);
				
			}else if(tableValue == "childTable1"){						
				productTestCaseChild1Results_Container(data, ScrollValue, row, tr);			
				
			}else if(tableValue == "childTable2"){						
				productTestCaseChild2Results_Container(data, ScrollValue, row, tr);			
				
			}else{
				console.log("no child");
			}			
		  },
		  error : function(data) {
			 closeLoaderIcon();  
			 //$("#addCommentsLoaderIcon").hide();
		 },
		 complete: function(data){
			closeLoaderIcon();
			//$("#addCommentsLoaderIcon").hide();
		 }
	});	
}

function productTestCaseResults_Container(data, scrollYValue, row, tr){		
	productTestCaseOptionsItemCounter=0;
	productTestCaseResultArr=[];			
	productTestCaseOptionsArr = [{id:"executionTypeId", type:optionsType_ProductTestCase, url:'common.list.executiontypemaster.byentityid?entitymasterid=3'},
	{id:"productTypes", type:optionsType_ProductTestCase, url:'getproducttype.list'},
	{id:"testcasePriorityId", type:optionsType_ProductTestCase, url:'common.list.testcasepriority'},
	{id:"testcaseTypeId", type:optionsType_ProductTestCase, url:'common.list.testcasetype'},
	{id:"productFeatureId", type:optionsType_ProductTestCase, url:'product.feature.list?productId='+productId},
	{id:"workflowId", type:optionsType_ProductTestCase, url:'workflow.master.mapped.to.entity.list.options?productId='+productId+'&entityTypeId=3&entityId=0'},
	{id:"decouplingCategoryId", type:optionsType_ProductTestCase, url:'product.decouplingcategory.list?productId='+productId},
	];
	
	returnOptionsItemForProductTestCase(productTestCaseOptionsArr[0].url, scrollYValue, data, row, productId);
}
// ----- Return options ----

function returnOptionsItemForProductTestCase(url, scrollYValue, data, row, tr){
	openLoaderIcon();
	
	$.ajax( {
 	   "type": "POST",
        "url":  url,
        "dataType": "json",
         success: function (json) {
         if(json == null || json.Result == "ERROR" || json.Result == "Error"){
         	if(json.Message !=null) {
         		callAlert(json.Message);
         	}
         	json.Options=[];
         	productTestCaseResultArr.push(json.Options);         	
         	returnOptionsItemForProductTestCase(url, scrollYValue, data, row, tr); 
         	
         }else{        	        	     	        	
			for(var i=0;i<json.Options.length;i++){
				if(json.Options[i].DisplayText == null)
					json.Options[i].DisplayText = "--";	
				
				json.Options[i].label=json.Options[i].DisplayText;
				json.Options[i].value=json.Options[i].Value;
			}
     	   productTestCaseResultArr.push(json.Options);
		   
     	   if(productTestCaseOptionsItemCounter<productTestCaseOptionsArr.length-1){			   
			   returnOptionsItemForProductTestCase(productTestCaseOptionsArr[productTestCaseResultArr.length].url, scrollYValue, data, row, tr);     		  
     	   }else{   			  	      			       			   				  				  
     		  productTestCase_Container(data, scrollYValue); 
		   }
     	   productTestCaseOptionsItemCounter++;     	   
         }
		 
			closeLoaderIcon();
         },
         error: function (data) {
        	 productTestCaseOptionsItemCounter++;
        	 closeLoaderIcon(); 
         },
         complete: function(data){
         	//console.log('Completed');
         	closeLoaderIcon(); 
         },	            
   	});	
}

function productTestCaseResultsEditor(){	
    
	testCase_editor = new $.fn.dataTable.Editor( {
			"table": "#testCases_dataTable",
    		ajax: "product.testcase.add",
    		ajaxUrl: "product.testcase.update",
    		idSrc:  "testCaseId",
    		i18n: {
    	        create: {
    	            title:  "Create a new Testcase",
    	            submit: "Create",
    	        }
    	    },
    		fields: [{								
				label:"productId",
				name:"productId",					
				type: 'hidden',	
                "def":productId, 				
			},{								
				label:"testCaseId",
				name:"testCaseId",					
				type: 'hidden',	
						
			},{
                label: "Testcase Code *",
                name: "testCaseCode",                
            },{
                label: "Testcase Name *",
                name: "testCaseName",                
            },{
                label: "Testcase Description *",
                name: "testCaseDescription",                
            },{								
				label:"Execution Order",
				name:"testCaseExecutionOrder",													
			},{
                label: "Execution Type",
                name: "executionTypeId",
                 options: productTestCaseResultArr[0],
                "type"  : "select",
            },{
                label: "Testcase Type",
                name: "productTypeId",
                 options: productTestCaseResultArr[1],
                "type"  : "select",
            },{
                label: "Script Package Name",
                name: "testCaseScriptQualifiedName",                
            },{
                label: "Script File Name",
                name: "testCaseScriptFileName",                
            },{
                label: "Priority",
                name: "testcasePriorityId",
                 options: productTestCaseResultArr[2],
                "type"  : "select",
            },{
                label: "Type",
                name: "testcaseTypeId",
                 options: productTestCaseResultArr[3],
                "type"  : "select",
            },{
                label: "Features",
                name: "productFeatureId",
                 options: productTestCaseResultArr[4],
                "type"  : "select",
            },{
                label: "WorkFlow Template",
                name: "workflowId",
                 options: productTestCaseResultArr[5],
                "type"  : "select",
            },{
                label: "Decoupling Category",
                name: "decouplingCategoryId",
                 options: productTestCaseResultArr[6],
                "type"  : "select",
            },{								
				label:"Current Status",
				name:"workflowStatusDisplayName",					
				type: 'hidden',					
			},{								
				label:"actors",
				name:"actors",					
				type: 'hidden',					
			},{								
				label:"completedBy",
				name:"completedBy",					
				type: 'hidden',					
			},{								
				label:"remainingHrsMins",
				name:"remainingHrsMins",					
				type: 'hidden',					
			},{								
				label:"totalEffort",
				name:"totalEffort",					
				type: 'hidden',					
			},{
				label:"Predecessors",
				name:"testCaePredecessors",				
			},{								
				label:"status",
				name:"status",					
				type: 'hidden',					
			},{								
				label:"workflowIndicator",
				name:"workflowIndicator",					
				type: 'hidden',					
			},
        ]
    	});	
}

var clearTimeoutDTProductTestCases='';
function reInitializeDTProductTestCases(){
	clearTimeoutDTProductTestCases = setTimeout(function(){				
		productTestCase_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTProductTestCases);
	},200);
}

function productTestCase_Container(data, row, tr){
	try{
		if ($("#jTableContainertest").children().length>0) {
			$("#jTableContainertest").children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(16);  // total coulmn count		  
	  var childDivString = '<table id="testCases_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#jTableContainertest").append(childDivString); 						  
	  
	  $("#testCases_dataTable thead").html('');
	  $("#testCases_dataTable thead").append(productTestCaseHeader());
	  
	  $("#testCases_dataTable tfoot tr").html('');     			  
	  $("#testCases_dataTable tfoot tr").append(emptytr);
	
	// --- editor for the activity Change Request started -----
	productTestCaseResultsEditor();
			
	productTestCase_oTable = $("#testCases_dataTable").dataTable( {
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
	       /*fixedColumns:   {
	           leftColumns: 1,
	       }, */
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex = [0,11,12,13,14,15,21,23,24]; // search column TextBox Invisible Column position
     		  $("#testCases_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
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
			   reInitializeDTProductTestCases();			   
				adjustSideBarTestCaseHeight();		
				
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: testCase_editor },
	             	{
					    text: 'Download Eclipse Project Templates',
					    action: function ( e, dt, node, config ) {
							var toolName = $("#downloadProjectRadio .checked input").attr('id');
							$("#div_projectTemplates").modal();
							$("#div_projectTemplates .modal-title").text("Specify Parameters");
					    }
					 },
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "ProductTestCases",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "ProductTestCases",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "ProductTestCases",
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
						text: '<i class="fa fa-upload showHandCursor" title="Upload Test Cases"></i>',
						action: function ( e, dt, node, config ) {					
							triggerProductTestCaseUpload();
						}
					},
					{					
						text: '<i class="fa fa-download showHandCursor" title="Download Template - Test Cases"></i>',
						action: function ( e, dt, node, config ) {					
							downloadTemplateTestCase();
						}
					},
					{
						text: '<i class="fa fa-upload showHandCursor" title="Upload Test Steps"></i>',
						action: function ( e, dt, node, config ) {					
							triggerProductTestStepsUpload(e);
						}
					},
					{
						text: '<i class="fa fa-download showHandCursor" title="Download Template - Test Steps"></i>',
						action: function ( e, dt, node, config ) {					
							downloadTemplateTestSteps();
						}
					},
					{
						text: '<span>Test Scenario<i title="Test Scenario"></i></span>',
						action: function ( e, dt, node, config ) {					
							testCaseGroupHandler(e);
						}
					},
					{
						text: '<i class="fa fa-download showHandCursor" title="Download Test Scripts"></i>',
						action: function ( e, dt, node, config ) {					
							displayDownloadTestScriptsFromTestCases(productId,'Product','Download');
						}
					},{
						text: '<i class="fa fa-upload showHandCursor" title="Import Test Management System"></i>',
						action: function ( e, dt, node, config ) {					
							importTestcasesFromTMS_PopUp();
						}
					},{
						text: '<i class="fa fa-download showHandCursor" title="Download Connector File"></i>',
						action: function ( e, dt, node, config ) {					
							downloadConnectorFile();
						}
					},{
						text: '<span>Mapping Version To Testcase<i id="mappingVersionToTestcase" title="Mapping Version To Testcase"></i></span>',
						action: function ( e, dt, node, config ) {					
							mappingVersionToTestcaseHandler(e);
						}
					},
				], 	         
        aaData:data,		    				 
	    aoColumns: [	
			{ mData: null,				 
            	bSortable: false,
				sWidth: '10%',
            	mRender: function(data, type, full) {						
           		 var img = ('<div style="display: flex;">'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
 	       					'<img src="css/images/list_metro.png" class="productTestCaseImg1" title="Test Step List" /></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },{ mData: "testCaseId", sWidth: '3%', "render": function (data,type,full) {	        
			 		var tcId = full.testCaseId;
					var tcName = full.testCaseName;	            		
					var result = tcId+'~@'+tcName;	
					return ('<a id="'+result+'"  onclick="testCaseOverAllView(event)">'+tcId+'</a>');			            	
		    	},
		    },	
           { mData: "testCaseCode",className: 'editable', sWidth: '3%' },
           { mData: "testCaseName",className: 'editable', sWidth: '20%' },           
		   { mData: "testCaseDescription",className: 'editable', sWidth: '25%' },           
		   { mData: "testCaseExecutionOrder",className: 'editable', sWidth: '3%' },		   		   
		   { mData: "executionTypeId", className: 'editable', sWidth: '15%', editField: "executionTypeId",
				mRender: function (data, type, full) {
					 data = optionsValueHandler(testCase_editor, 'executionTypeId', full.executionTypeId);
					 return data;
				 },
	        },
	        { mData: "productTypeId", className: 'editable', sWidth: '10%', editField: "productTypeId",
            	mRender: function (data, type, full) {
	           		data = optionsValueHandler(testCase_editor, 'productTypeId', full.productTypeId);
		            return data;
	             },
            },
			{ mData: "testCaseScriptQualifiedName",className: 'editable', sWidth: '20%' },
			{ mData: "testCaseScriptFileName",className: 'editable', sWidth: '20%' },						
           { mData: "testcasePriorityId", className: 'editable', sWidth: '3%',		
			  mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(testCase_editor, 'testcasePriorityId', full.testcasePriorityId);
					 }
					 else if(type == "display"){
						 data = optionsValueHandler(testCase_editor, 'testcasePriorityId', full.testcasePriorityId);
					 }	           	 
					 return data;
				 },	            
           },{ mData: "testcaseTypeId", className: 'editable', sWidth: '3%',			
			  mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(testCase_editor, 'testcaseTypeId', full.testcaseTypeId);
					 }
					 else if(type == "display"){
						 data = optionsValueHandler(testCase_editor, 'testcaseTypeId', full.testcaseTypeId);
					 }	           	 
					 return data;
				 },	            
           },
           { mData: "testCaePredecessors", className: 'editable', sWidth: '3%'},
           { mData: null,				 
            	bSortable: false,
				sWidth: '10%',
            	mRender: function(data, type, full) {	
					/*var img = ('<div style="display: flex;">'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
 	       					'<img src="css/images/ATSG.JPG" style="width:50px" class="productTestCaseImg6" title="ATSG"/></button>'+    	       			
     	       		'</div>');				
					return img;	*/
            		
            		 var img = ('<div class="btn-toolbar "><div class="btn-group" style="display: flex;">'+
                             '<button type="button" class="btn btn-default productTestCaseImg6">ATSG</button>'+
                             '<button type="button" class="btn btn-default productTestCaseImg6_2" style="margin-left: 8px;">eATSG</button>'+                                 
                         	'</div>');
               		return img;
	            	},
	            	className: 'disableEditInline alignImageCenter', sWidth: '6%'            			
            },			
			/*{ mData: "decouplingCategoryId", className: 'disableEditInline', sWidth: '5%',		
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(testCase_editor, 'decouplingCategoryId', full.decouplingCategoryId);
					 }else if(type == "display"){
						data = full.decouplingCategoryId;
					 }	           	 
					 return data;
				 },	            
            },
			{ mData: "workflowStatusDisplayName", className: 'disableEditInline', sWidth: '3%'},
			{ mData: "actors", className: 'disableEditInline', sWidth: '5%'},
			{ mData: "completedBy", className: 'disableEditInline', sWidth: '3%'},
			{ mData: "remainingHrsMins", className: 'disableEditInline', sWidth: '3%'},
			{ mData: "workflowIndicator", className: 'disableEditInline', sWidth: '2%'},*/
			/*{ mData: null,				 
            	bSortable: false,
				sWidth: '10%',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
 	       					'<img src="css/images/list_metro.png" class="productFeatureImg1" title="Mapped TestCase List" /></button>'+    	       			
     	       		'</div>');	      		
           		 return img;
            	}
            },*/
			//{ mData: "totalEffort", className: 'disableEditInline', sWidth: '3%'},
            
			{ mData: "status", className: 'disableEditInline', sWidth: '4%',			
			  mRender: function (data, type, full) {
				  if ( type === 'display' ) {
						return '<input type="checkbox" class="productTestCaseEditor-active">';
				  }
					return data;
				},
				className: "dt-body-center"	            
           },
           
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
       				'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;  margin-left: 5px;"">'+
						'<img src="css/images/list_metro.png" class="productTestCaseImg2" title="Mapped Product Features" /></button>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;  margin-left: 5px;"">'+
						'<img src="css/images/analytics.jpg" class="productTestCaseImg12" title="Test Case Analytics" /></button>'+  	
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;  margin-left: 5px;"">'+
						'<img src="css/images/execute_metro.png" class="productTestCaseImg3" title="Execute TestCase" /></button>'+    	       			
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;  margin-left: 5px;"">'+
						'<img src="css/images/mapping.png" class="productTestCaseImg4" title="Feature Mapping" data-toggle="modal" data-target="#dragListItemsContainer"/></button>'+    	       			
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;  margin-left: 5px;"">'+
 	       					'<img src="css/images/mapping.png" class="productTestCaseImg5" title="TestScript Mapping" data-toggle="modal" data-target="#dragListItemsContainer"/></button>'+	 
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px; margin-left: 5px;">'+
 	       					'<i class="fa fa-history productTestCaseImg7" title="Execution Summary and History"></i></button>'+
     	       		'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px; margin-left: 5px;">'+
							'<img src="css/images/workflow.png" class="productTestCaseImg8" title="Configure Workflow"/></button>'+
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="fa fa-search-plus productTestCaseImg9" title="Audit History"></i></button>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						'<i class="fa fa-comments productTestCaseImg10" title="Comments"></i></button>'+
					'<button style="border: none; background-color: transparent; outline: none;">'+
       							'<i class="fa fa-trash-o details-control productTestCaseImg11" onClick="deleteTestCase('+data.testCaseId+')" title="Delete Testcase" style="padding-left: 0px;"></i></button>'+		
     	       		'</div>');	      		
           		 return img;
            	}
            },
       ], 
		rowCallback: function ( row, data ) {
	           $('input.productTestCaseEditor-active', row).prop( 'checked', data.status == 1 );
	           if(data.productTypeId != 3){//Embedded
	        	   $(row).find('.productTestCaseImg6_2').attr('disabled','disabled');
	        	   $(row).find('.productTestCaseImg6').removeAttr('disabled','disabled');
	           }else{
	        	   $(row).find('.productTestCaseImg6').attr('disabled','disabled');
	        	   $(row).find('.productTestCaseImg6_2').removeAttr('disabled','disabled');
	           }
	    },	   
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		 
		 //$("#activityTabLoaderIcon").hide();	 
		adjustSideBarTestCaseHeight();
		 
		 $("#testCases_dataTable_length").css('margin-top','8px');
		 $("#testCases_dataTable_length").css('padding-left','35px');
		 
		 $('#testCases_dataTable').on( 'change', 'input.productFeatureEditor-active', function () {
			 testCase_editor
					.edit( $(this).closest('tr'), false )
					.set( 'parentFeatureStatus', $(this).prop( 'checked' ) ? 1 : 0 )
					.submit();
			});
		 	 
		 // Activate an inline edit on click of a table cell
        $('#testCases_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	testCase_editor.inline( this, {
        		submitOnBlur: true
			}); 
		});
        
        testCase_editor.on( 'preSubmit', function ( e, o, action ) {
	        if ( action !== 'remove' ) {
	        	var validationArr = ['testCaseCode','testCaseName','testCaseDescription'];
	        	var testCaseName;
	        	for(var i=0;i<validationArr.length;i++){
		            testCaseName = this.field(validationArr[i]);
		            if ( ! testCaseName.isMultiValue() ) {
		                if ( testCaseName.val() ) {
	                	}else{
		                	if(validationArr[i] == "testCaseCode")
		                		testCaseName.error( 'Please enter Testcase code' );
		                	if(validationArr[i] == "testCaseName")
		                		testCaseName.error( 'Please enter Testcase name' );
		                	if(validationArr[i] == "testCaseDescription")
		                		testCaseName.error( 'Please enter Testcase description' );
	                	}
		            }
	        	}

	            // If any error was reported, cancel the submission so it can be corrected
	            if ( this.inError() ) {
	                return false;
	            }
	        }
	    } );        
		
		productTestCase_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );
			
		// -----		
		$('#testCases_dataTable tbody').on('click', 'td button .productTestCaseImg1', function () {
			var tr = $(this).closest('tr');
			var row = productTestCase_oTable.DataTable().row(tr);
			testStepTestcaseId=row.data().testCaseId;
			var urlToTestSteps = 'testcase.teststep.list.by.status?testCaseId='+row.data().testCaseId+'&status=1&jtStartIndex=0&jtPageSize=10000';		
			listTestCasesOfSelectedProduct(urlToTestSteps, "240px", "childTable1", row, tr);
			$("#productTestCaseChild1_Container").modal();				
			
		});
		
		$(document).on('change','#testStepStatusFilter_ul', function() {
			var testStepStatus = $("#testStepStatusFilter_ul").find('option:selected').val();			
			var urlToTestSteps = 'testcase.teststep.list.by.status?testCaseId='+testStepTestcaseId+'&status='+testStepStatus+'&jtStartIndex=0&jtPageSize=10000';
			listTestCasesOfSelectedProduct(urlToTestSteps, "240px", "childTable1", "", "");					
		    
		});
		
		
		// -----
		 
		 $('#testCases_dataTable tbody').on('click', 'td button .productTestCaseImg2', function () {
			var tr = $(this).closest('tr');
			var row = productTestCase_oTable.DataTable().row(tr);			    				
			
			var urlToGetFeaturesOfSpecifiedProductId = 'administration.testcase.feature.mappedlist?testCaseId='+row.data().testCaseId+'&jtStartIndex=0&jtPageSize=10000';		
		    listTestCasesOfSelectedProduct(urlToGetFeaturesOfSpecifiedProductId, "180px", "childTable2", row, tr);
			$("#productTestCaseChild2_Container").modal();			
			    
		 });
		 // ----- ended -----
		 
		 // ----- Execute -----		 
		 $('#testCases_dataTable tbody').on('click', 'td button .productTestCaseImg3', function () {			 
			 var tr = $(this).closest('tr');
			 var row = productTestCase_oTable.DataTable().row(tr);
			
			testCaseId =	row.data().testCaseId;	
			testCaseExecution = 'TestCaseExecution';
			atsgId = -1;
			testRunPLanListByTestCaseId();
			 
		 });		 
		 // ----- ended -----
		 
		 $('#testCases_dataTable tbody').on('click', 'td button .productTestCaseImg12', function () {			 
			 var tr = $(this).closest('tr');
			 var row = productTestCase_oTable.DataTable().row(tr);
			
			testCaseId =row.data().testCaseId;
			var testCaseName=row.data().testCaseName;
			loadTestcaseMetirics(testCaseId,testCaseName);
			 
		 });
		// ----- Feature Mapping -----		 
		 $('#testCases_dataTable tbody').on('click', 'td button .productTestCaseImg4', function () {			 
			 var tr = $(this).closest('tr');
			 var row = productTestCase_oTable.DataTable().row(tr);	

			var tcId = row.data().testCaseId;
			var tcName = row.data().testCaseName;
			var productId =	row.data().productId;					
			// ----- DragDrop Testing started----		
			//var productId = document.getElementById("hdnProductId").value;												
			var leftUrl="feature.unmappedto.testcase.count?productId="+productId+"&testCaseId="+tcId;							
			var rightUrl = "";
			var leftDefaultUrl="testcase.unmappedfeatures.byProduct.list?productId="+productId+"&testCaseId="+tcId+"&jtStartIndex=0&jtPageSize=50";
			var rightDefaultUrl = "test.case.feature.list?testCaseId="+tcId+"&productId="+productId;									
			var leftDragUrl = 'test.case.feature.mapping?&testcaseId='+tcId;
			var rightDragtUrl = 'test.case.feature.mapping?testcaseId='+tcId;
			var leftPaginationUrl = "testcase.unmappedfeatures.byProduct.list?productId="+productId+"&testCaseId="+tcId;
			var rightPaginationUrl="";									
			 jsonTestCaseTabObj={"Title":"Map Feature to Test Cases :- "+tcName,
					"leftDragItemsHeaderName":"Available Features",
					"rightDragItemsHeaderName":"Mapped Features",
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
					"noItems":"No Features",	
					"componentUsageTitle":"TestcaseTab-FeaturetoTC",											
					};
			
			DragDropListItems.init(jsonTestCaseTabObj); 							
			// DragDrop Testing ended----	
						 
		 });		 
		 // ----- ended -----	

		// ----- TestScripts -----		 
		 $('#testCases_dataTable tbody').on('click', 'td button .productTestCaseImg5', function () {			 
			 var tr = $(this).closest('tr');
			 var row = productTestCase_oTable.DataTable().row(tr);	

			var testCasesListId = row.data().testCaseId;
			var productId =	row.data().productId;					
			// ----- DragDrop Testing started----		
			//var productId = document.getElementById("hdnProductId").value;												
			var leftUrl="unmapped.testScripts.count?productId="+productId+"&testCaseId="+testCasesListId;							
			var rightUrl = "";							
			var leftDefaultUrl="unmapped.testScripts.list?productId="+productId+"&testCaseId="+testCasesListId+"&jtStartIndex=0&jtPageSize=50";							
			var rightDefaultUrl="testScripts.mapped.to.testCase.list?testCaseId="+testCasesListId;
			var leftDragUrl = "testCase.map.or.unmap.to.testCaseScript?testCaseId="+testCasesListId;
			var rightDragtUrl = "testCase.map.or.unmap.to.testCaseScript?testCaseId="+testCasesListId;						
			var leftPaginationUrl = "unmapped.testScripts.list?productId="+productId+"&testCaseId="+testCasesListId;
			
			var rightPaginationUrl="";							
			
			jsonTestCaseListObj={"Title":"Map Test Scripts to TestCase :- "+row.data().testCaseName,
					"leftDragItemsHeaderName":"Available Test Scripts",
					"rightDragItemsHeaderName":"Mapped Test Scripts",
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
					"noItems":"No Test Scripts",
					"componentUsageTitle":"TestScripts-TestCase",
					};
			
			DragDropListItems.init(jsonTestCaseListObj);
						 
		 });		 
		 // ----- ended -----
		
		// ----- TestScripts -----		 
		/* $('#testCases_dataTable tbody').on('click', 'td button .productTestCaseImg6', function () {			 
			var tr = $(this).closest('tr');
			var row = productTestCase_oTable.DataTable().row(tr);		 
		 
			var result = (row.data().testCaseId+'~'+row.data().testCaseName+'~'+row.data().productTypeName).toString();
			scriptsFor="TestCase";
			scriptTypeTile = "Test Case";
			testCaseExecution = 'TestCaseExecution';			 
			displayATSGPopupHandler(result);
			
		});*/
		 $('#testCases_dataTable tbody').on('click', 'td .productTestCaseImg6', function () {
				var tr = $(this).closest('tr');
				var row = productTestCase_oTable.DataTable().row(tr);	
				atsgTimer();
				atsgEditorTimer = setInterval(atsgTimer, 10*60*1000);
				$.ajax({
					type: "POST",
					url: 'administration.validate.license.expiry',
					success: function(data) {  
						if(data.Result == "ERROR"){
							callAlert(data.Message);
							return false;
						}else if(data.Result == "OK"){
							productType = optionsValueHandler(testCase_editor, 'productTypeId', row.data().productTypeId);
							if(productType == "" || productType == undefined){
					    		callAlert('Please select test case type ..!');
					    		return false;
					    	}
							else if(productType == "Composite" ){
					    		callAlert('ATSG is not supported for Composite ..!');
					    		return false;
					    	}
					    	setTestEngine(productType);
					    	
							atsgBtn = "ATSG";
							$('.multiTestcaseCheckbox input').prop('checked',false);
							testCaseChecked = false;
							testCaseIdArr = [];
							testEngineArr = [];

							testToolName = row.data().testToolName;
					    	var result = (row.data().testCaseId
									+ '~'
									+ row.data().testCaseName
									+ '~' + 'web').toString();
					    	scriptsFor = "TestCase";
							displayATSGPopupHandler(result);
							
							
							data = row.data();
							var isBeingEdit = '<i class="fa fa-pencil" title="'+data.editingUser+'"></i><text>....</text>';

							if (!data.isBeingEdited) {
								isBeingEdit = '';
							}
							// scriptClosePopupFlag = false ;
							var result = (data.testCaseId
									+ '~'
									+ data.testCaseName
									+ '~' + 'web').toString();
							scriptsFor = "TestCase";
							scriptTypeTile = "Test Case";
							var $img = $('<img src="css/images/ATSG.JPG" title="ATSG" style="width:50px"/>'
									+ isBeingEdit);
							$img
									.click(function() {
										currentUserIdentity = data.editingUser;
										displayATSGPopupHandler(result);
									});
							return $img;
						}				
					 }    
				});
	   		});
		 
		// ----- editor EDAT

		 $('#testCases_dataTable tbody').on('click', 'td .productTestCaseImg6_2', function () {
		 	atsgBtn = "eATSG";
		 	var tr = $(this).closest('tr');
		 	var row = productTestCase_oTable.DataTable().row(tr);
		 	atsgTimer();
			atsgEditorTimer = setInterval(atsgTimer, 10*60*1000);
		 	$.ajax({
				 type: "POST",
				 url: 'administration.validate.license.expiry',
				 success: function(data) {  
					 if(data.Result == "ERROR"){
						 callAlert(data.Message);
						 return false;
					 }else if(data.Result == "OK"){
						 	productType = optionsValueHandler(testCase_editor, 'productTypeId', row.data().productTypeId);
						 	testToolName = row.data().testToolName;
						 	if(productType != "Embedded"){
						 		callAlert('eATSG is not supported for this product. Please click ATSG.');
						 		return false;
						 	}
						 	$('.multiTestcaseCheckbox input').prop('checked',false);
						 	testCaseChecked = false;
						 	testCaseIdArr = [];
						 	testEngineArr = [];
						 	var result = (row.data().testCaseId
						 			+ '~'
						 			+ row.data().testCaseName
						 			+ '~' + 'web').toString();
						 	scriptsFor = "TestCase";
						 	display_EATSG_PopupHandler(result);
						 	
						 	data = row.data();
						 	var isBeingEdit = '<i class="fa fa-pencil" title="'+data.editingUser+'"></i><text>....</text>';

						 	if (!data.isBeingEdited) {
						 		isBeingEdit = '';
						 	}
						 	// scriptClosePopupFlag = false ;
						 	var result = (data.testCaseId
						 			+ '~'
						 			+ data.testCaseName
						 			+ '~' + 'web').toString();
						 	scriptsFor = "TestCase";
						 	scriptTypeTile = "Test Case";
						 	var $img = $('<img src="css/images/EATSG.JPG" title="E-ATSG" style="width:50px"/>'
						 			+ isBeingEdit);
						 	$img
						 			.click(function() {
						 				currentUserIdentity = data.editingUser;
						 				display_EATSG_PopupHandler(result);
						 			});
						 	return $img;
					 }				
				 }    
			});
		 	
		 	
		 });			
		
		// ----- Execution Summary -----		 
		 $('#testCases_dataTable tbody').on('click', 'td button .productTestCaseImg7', function () {			 
			var tr = $(this).closest('tr');
			var row = productTestCase_oTable.DataTable().row(tr);		 
		 
			var dataLevel = "productLevel";
			listTCExecutionSummaryHistoryView(row.data().testCaseId, row.data().testCaseName, dataLevel);
		});
		
		// ----- Configure Workflow -----		 
		 $('#testCases_dataTable tbody').on('click', 'td button .productTestCaseImg8', function () {			 
			var tr = $(this).closest('tr');
			var row = productTestCase_oTable.DataTable().row(tr);		 
		 
			workflowId = 0;
			entityTypeId = 3;
			entityId = 0;			
			statusPolicies(productId, workflowId, entityTypeId, entityId, row.data().testCaseId, row.data().testCaseName, "Test Case", row.data().workflowStatusId);
		});
		
		// ----- Audit History -----		 
		 $('#testCases_dataTable tbody').on('click', 'td button .productTestCaseImg9', function () {			 
			var tr = $(this).closest('tr');
			var row = productTestCase_oTable.DataTable().row(tr);		 
		 
			listGenericAuditHistory(row.data().testCaseId,"TestCase","testCaseAudit");
		});
		
		// ----- Comments -----		 
		 $('#testCases_dataTable tbody').on('click', 'td button .productTestCaseImg10', function () {			 
			var tr = $(this).closest('tr');
			var row = productTestCase_oTable.DataTable().row(tr);		 
		 
			var entityTypeIdComments = 3;
			var entityNameComments = "TestCase";
			listComments(entityTypeIdComments, entityNameComments, row.data().testCaseId, row.data().testCaseName, "tCaseComments");
		});
	});
	// ------	
}

function atsgTimer(){
 	$.ajax({
		 type: "POST",
		 url: 'keep.atsg.alive',
		 success: function(data) {  
			 if(data.Result == "ERROR"){
				 callAlert(data.Message);
				 return false;
			 }else if(data.Result == "OK"){
				 
			 }				
		 }    
	});
}

function mappingVersionToTestcaseHandler(event){
	$("#versionToTestcaseMappingContainer").modal();
	var url = 'get.testcase.version.mappedList?productId='+productId+'&versionId='+productVersionId+'&buildId='+buildId+'&jtStartIndex=0&jtPageSize=10000';
    mappingVersionToTestCase(url);
}

// ----- Deletion Activity Items Started -----

function deleteTestCase(testCaseId){
	var fd = new FormData();
	fd.append("testCaseId", testCaseId);
	
	openLoaderIcon();
	$.ajax({
		url : 'product.testcase.delete',
		data : fd,
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
				callAlert(data.Message);
			}
			closeLoaderIcon();
		},
		error : function(data) {
			closeLoaderIcon();  
		},
		complete: function(data){
			closeLoaderIcon();
		}
	});
}

function productTestCaseHeader(){		
	var tr = '<tr>'+			
		'<th></th>'+
		'<th>ID</th>'+
		'<th>Code</th>'+
		'<th>Name</th>'+
		'<th>Description</th>'+
		'<th>Order</th>'+
		'<th>Execution</th>'+
		'<th>Test Case Type</th>'+
		'<th>Script Package Name</th>'+
		'<th>Script File Name</th>'+
		'<th>Priority</th>'+		
		'<th>Type</th>'+
/*		'<th>Mapped Product Features</th>'+
		'<th>Execute</th>'+
		'<th>Feature Mapping</th>'+
		'<th>TestScripts</th>'+*/
		'<th>Predecessors</th>'+
		'<th></th>'+
		/*'<th>Decoupling Categories</th>'+		
		'<th>Current Status</th>'+
		'<th>Status Pending With</th>'+
		'<th>Status Complete By</th>'+
		'<th>Status Time Left</th>'+
		'<th></th>'+
		'<th>Total Effort</th>'+*/
		'<th>Active</th>'+
		'<th></th>'+
	'</tr>';
	return tr;
}

// ----- TestCases Child DataTable -----

// ----- Child1Table -----

function productTestCaseChild1Results_Container(data, scrollYValue, row, tr){	
	productTestCaseChild1_Container(data, row, tr);
}

// ----- Child2Table -----

function productTestCaseChild2Results_Container(data, scrollYValue, row, tr){	
	productTestCaseChild2_Container(data, row, tr);
}

var clearTimeoutDTproductTestCaseChild1='';
var productTestCaseChild1_oTable=''
function reInitializeDTproductTestCaseChild1(){
	clearTimeoutDTproductTestCaseChild1 = setTimeout(function(){				
		productTestCaseChild1_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTproductTestCaseChild1);
	},200);
}

function productTestCaseChildHeader(){		
	var tr = '<tr>'+			
		'<th>Test Step ID</th>'+
		'<th>Test Step Code</th>'+
		'<th>Test Step Name</th>'+
		'<th>Test Step Description</th>'+
		'<th>Test Step Input</th>'+
		'<th>Test Step Expected Output</th>'+
		'<th>Test Step Source</th>'+
		'<th>Status</th>'+
		'<th></th>'+				
	'</tr>';
	return tr;
}

var clearTimeoutDTproductTestCaseChild2='';
var productTestCaseChild2_oTable=''
function reInitializeDTproductTestCaseChild2(){
	clearTimeoutDTproductTestCaseChild2 = setTimeout(function(){				
		productTestCaseChild2_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTproductTestCaseChild2);
	},200);
}

function productTestCaseChild2Header(){		
	var tr = '<tr>'+			
		'<th>Feature ID</th>'+
		'<th>Feature Code</th>'+
		'<th>Feature Name</th>'+
		'<th>Display Name</th>'+
		'<th>Feature Description</th>'+
		'<th>Parent</th>'+
		'<th>Status</th>'+		
	'</tr>';
	return tr;
}

var testCaseChild1_editor='';

function productTestCaseChild1ResultsEditor(row){	
    
	testCaseChild1_editor = new $.fn.dataTable.Editor( {
			"table": "#testCaseChild1_dataTable",
    		ajax: "testcase.teststep.add",
    		ajaxUrl: "testcase.teststep.update",
    		idSrc:  "testStepId",
    		i18n: {
    	        create: {
    	            title:  "Create a new Feature",
    	            submit: "Create",
    	        }
    	    },
    		fields: [{								
				label:"testCaseId",
				name:"testCaseId",					
				type: 'hidden',
                "def":testStepTestcaseId,  				
			},{								
				label:"testStepId",
				name:"testStepId",					
				type: 'hidden',								
			},{
                label: "Test Step Code *",
                name: "testStepCode",                
            },{
                label: "Test Step Name *",
                name: "testStepName",                
            },{
                label: "Test Step Description *",
                name: "testStepDescription",                
            },{								
				label:"Test Step Input",
				name:"testStepInput",													
			},{
                label: "Test Step Expected Output",
                name: "testStepExpectedOutput",                 
            },{
                label: "Test Step Source",
                name: "testStepSource",                
            },{
                label: "Status",
                name: "status",
                type:"hidden",
                "def":'1',
            }, 
        ]
    	});

}

function productTestCaseChild1_Container(data, row, tr){
	try{
		if ($("#productTestCaseChild1").children().length>0) {
			$("#productTestCaseChild1").children().remove();
		}
	} 
	catch(e) {}
	
    	var emptytr = emptyTableRowAppending(9);  // total coulmn count				  
	  var childDivString = '<table id="testCaseChild1_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#productTestCaseChild1").append(childDivString); 						  
	  
	  $("#testCaseChild1_dataTable thead").html('');
	  $("#testCaseChild1_dataTable thead").append(productTestCaseChildHeader());
	  
	  $("#testCaseChild1_dataTable tfoot tr").html('');     			  
	  $("#testCaseChild1_dataTable tfoot tr").append(emptytr);
	  
	   productTestCaseChild1ResultsEditor(row);
				
	productTestCaseChild1_oTable = $("#testCaseChild1_dataTable").dataTable( {
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
			//select: true,		   
	       "fnInitComplete": function(data) {
			   
			  var searchcolumnVisibleIndex = [7,8]; // search column TextBox Invisible Column position
	     	  var headerItems = $('#testCaseChild1_dataTable_wrapper tfoot tr th');
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
			   
			   reInitializeDTproductTestCaseChild1();			   
		   },  
		   select: true,
		   buttons: [
					 { extend: "create", editor: testCaseChild1_editor },		   
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
           { mData: "testStepId",className: 'disableEditInline', sWidth: '5%' },		
           { mData: "testStepCode",className: 'editable', sWidth: '10%' },
           { mData: "testStepName",className: 'editable', sWidth: '20%' },           
		   { mData: "testStepDescription",className: 'editable', sWidth: '20%' },           
		   { mData: "testStepInput",className: 'editable', sWidth: '20%' },           
		   { mData: "testStepExpectedOutput",className: 'editable', sWidth: '10%' },           			
           { mData: "testStepSource", className: 'editable', sWidth: '10%'},
           { mData: "status", className: 'disableEditInline', sWidth: '4%',			
 			  mRender: function (data, type, full) {
 				  if ( type === 'display' ) {
 						return '<input type="checkbox" class="productTestStepEditor-active">';
 				  }
 					return data;
 				},
 				className: "dt-body-center"	            
            },
		   { mData: null,				 
            	bSortable: false,
				sWidth: '5%',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
 	       					'<i class="fa fa-search-plus productFeatureChildImg1" title="Audit History"></i></button>');
           		 return img;
            	}
            },
       ], 
       rowCallback: function ( row, data ) {
           $('input.productTestStepEditor-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 		 		 
		 $("#testCaseChild1_dataTable_length").css('margin-top','8px');
		 $("#testCaseChild1_dataTable_length").css('padding-left','35px');	
		 
		   $('#testCaseChild1_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	testCaseChild1_editor.inline( this, {
        		submitOnBlur: true
			}); 
		});
		 
		 $('#testCaseChild1_dataTable').DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });
		 
		 testCaseChild1_editor.on( 'open', function ( e, json, data ) {
			 $(document).off('focusin.modal');
		 } );

		 testCaseChild1_editor.on( 'preSubmit', function ( e, o, action ) {
	        if ( action !== 'remove' ) {
	        	var validationArr = ['testStepCode','testStepName','testStepDescription'];
	        	var str;
	        	for(var i=0;i<validationArr.length;i++){
		            str = this.field(validationArr[i]);
		            if ( ! str.isMultiValue() ) {
		                if ( str.val() ) {
	                	}else{
		                	if(validationArr[i] == "testStepCode")
		                		str.error( 'Please enter Test Step code' );
		                	if(validationArr[i] == "testStepName")
		                		str.error( 'Please enter Test Step name' );
		                	if(validationArr[i] == "testStepDescription")
		                		str.error( 'Please enter Test Step description' );
	                	}
		            }
	        	}

	            // If any error was reported, cancel the submission so it can be corrected
	            if ( this.inError() ) {
	                return false;
	            }
	        }
	    } ); 
		 // ----- Execution History -----
		 
		 $('#testCaseChild1_dataTable tbody').on('click', 'td button .productFeatureChildImg1', function () {			 
			 var tr = $(this).closest('tr');
			 var row = productTestCaseChild1_oTable.DataTable().row(tr);
				
			listGenericAuditHistory(row.data().testStepId,"TestStep","testCaseStepsAudit");			 
		 });
		 
		 $('#testCaseChild1_dataTable').on( 'change', 'input.productTestStepEditor-active', function () {
			 testCaseChild1_editor
					.edit( $(this).closest('tr'), false )
					.set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
					.submit();
			});
		 
		 
		 // ----- ended -----	
		
	});
	// ------	
}

// ----- Child2Table -----

function productTestCaseChild2_Container(data, row, tr){
	try{
		if ($("#productTestCaseChild2").children().length>0) {
			$("#productTestCaseChild2").children().remove();
		}
	} 
	catch(e) {}
	
    	var emptytr = emptyTableRowAppending(7);  // total coulmn count				  
	  var childDivString = '<table id="testCaseChild2_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#productTestCaseChild2").append(childDivString); 						  
	  
	  $("#testCaseChild2_dataTable thead").html('');
	  $("#testCaseChild2_dataTable thead").append(productTestCaseChild2Header());
	  
	  $("#testCaseChild2_dataTable tfoot tr").html('');     			  
	  $("#testCaseChild2_dataTable tfoot tr").append(emptytr);
	  				
	productTestCaseChild2_oTable = $("#testCaseChild2_dataTable").dataTable( {
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
			   
			  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     	  var headerItems = $('#testCaseChild2_dataTable_wrapper tfoot tr th');
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
			   
			   reInitializeDTproductTestCaseChild2();			   
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
           { mData: "productFeatureId",className: 'disableEditInline', sWidth: '10%' },
           { mData: "productFeatureCode",className: 'disableEditInline', sWidth: '20%' },           
		   { mData: "productFeatureName",className: 'disableEditInline', sWidth: '20%' },           
		   { mData: "displayName",className: 'disableEditInline', sWidth: '20%' },           
		   { mData: "productFeatureDescription",className: 'disableEditInline', sWidth: '10%' },           			
           { mData: "parentFeatureId", className: 'disableEditInline', sWidth: '10%'},		   	   
		   { mData: "parentFeatureStatus", className: 'disableEditInline', sWidth: '10%'},            	
       ],       
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 		 		 
		 $("#testCaseChild2_dataTable_length").css('margin-top','8px');
		 $("#testCaseChild2_dataTable_length").css('padding-left','35px');	
		 
		 $('#testCaseChild2_dataTable').DataTable().columns().every( function () {
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

function downloadProjectTemplate() {
	var toolName = $(
			"#div_projectTemplates #downloadProjectRadio .checked input")
			.attr('id');

	var urlfinal;

	urlfinal = "rest/download/excelReport?fileName=" + toolName;
	parent.window.location.href = urlfinal;
	$("#div_projectTemplates").modal("hide");
}

function downloadConnectorFile() {
	var mappingUrl = getContextCompletePath();
	var completeURL = mappingUrl+'/templates/StoryScriptDownloadConnector.zip';
	document.location.href = completeURL;
}
