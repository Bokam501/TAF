var productTestSuiteOptionsArr=[];
var productTestSuiteResultArr=[];
var productTestSuiteOptionsItemCounter=0;
var productTestSuite_oTable='';
var optionsType_ProductTestSuite="ProductTestSuite";
var testSuite_editor='';

function testsuites(nodeType){
	addorno="yes";
	var urlToGetTestSuitesOfSpecifiedProductId='';
	if(nodeType == "Product" || nodeType == "TestFactory"){
		//productId = key;
		addorno="no";
		if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");
			return false;
		}else{
			addorno="no";
			$('#Testsuites').children().show();
			var date = new Date();
		    var timestamp = date.getTime();
			
			//urlToGetTestSuitesOfSpecifiedProductId = 'test.suite.byProduct.list?productMasterId='+productId+'&jtStartIndex=0&jtPageSize=10000';
		    if(!testsuitesTabFlag){
		    	urlToGetTestSuitesOfSpecifiedProductId = 'test.suite.byProduct.with.mappings.list?productMasterId='+productId+"&testRunPlanId=-1&runConfigId="+rnCnfgId+"&jtStartIndex=0&jtPageSize=10000";
			}else{
				urlToGetTestSuitesOfSpecifiedProductId = 'test.suite.byProduct.with.mappings.list?productMasterId='+productId+"&testRunPlanId=-1&runConfigId=-1&jtStartIndex=0&jtPageSize=10000";
			}
		    
			listTestSuiteSelectedProductVersion(productId, productVersionId, timestamp, urlToGetTestSuitesOfSpecifiedProductId, addorno);			
		}
	}else if(nodeType == "ProductVersion"){
		productId = document.getElementById("treeHdnCurrentProductId").value;
		$('#Testsuites').children().show();
		addorno = "yes";
		//productVersionId = key;
		if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == ''){
			callAlert("Please select the Product Version");
			return false;
		}else{
			var date = new Date();
		    var timestamp = date.getTime();		  
		    if(!testsuitesTabFlag){
				if(pageType == "HOMEPAGE"){
					if(rnCnfgId != -1)
						urlToGetTestSuitesOfSpecifiedProductVersionId = 'test.suite.byProductVersion.list?versionId='+productVersionId+"&testRunPlanId=-1&runConfigId="+rnCnfgId+"&jtStartIndex=0&jtPageSize=10000";
					else
						urlToGetTestSuitesOfSpecifiedProductVersionId = 'test.suite.byProductVersion.list?versionId='+productVersionId+"&testRunPlanId="+tpcId+"&runConfigId="+rnCnfgId+"&jtStartIndex=0&jtPageSize=10000";
				}else{
					urlToGetTestSuitesOfSpecifiedProductVersionId = 'test.suite.byProductVersion.list?versionId='+productVersionId+"&testRunPlanId=-1&runConfigId="+rnCnfgId+"&jtStartIndex=0&jtPageSize=10000";
				}
			}else{
				urlToGetTestSuitesOfSpecifiedProductVersionId = 'test.suite.byProductVersion.list?versionId='+productVersionId+"&testRunPlanId=-1&runConfigId=-1&jtStartIndex=0&jtPageSize=10000";
			}
		    listTestSuiteSelectedProductVersion(productId, productVersionId, timestamp, urlToGetTestSuitesOfSpecifiedProductVersionId, addorno);
		}
	}else{
		$('#Testsuites').children().hide();
		callAlert("Please select the Product or Product Version");
	}	
	
}

function listTestSuiteSelectedProductVersion(productId, productVersionId, timestamp, urlTestSuite, addorno){	 
	var isAdmin=false;
	if(getUserRole() == "Admin"){
		isAdmin = true;
	}
	/*if(productVersionId == -1){
		callAlert('Please choose a ProductVersion or Add Version to Product before Adding TestSuite.');
		return false;
	}*/
	
	openLoaderIcon();
	
	//if(tableValue == "parentTable")
		//$("#addCommentsLoaderIcon").show();	
	
	 $.ajax({
		  type: "POST",
		  url:urlTestSuite,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			//$("#addCommentsLoaderIcon").hide();
			
			if(data == null || data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}			
			productTestSuiteResults_Container(data);			
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

function productTestSuiteResults_Container(data){	
	var atype="default";
	productTestSuiteOptionsItemCounter=0;
	productTestSuiteResultArr=[];	
	productTestSuiteOptionsArr = [{id:"productVersionId", type:optionsType_ProductTestSuite, url:'common.list.productversion?productId='+productId},
		{id:"testScriptType", type:optionsType_ProductTestSuite, url:'common.list.scripttype'},
		{id:"scmSystemId", type:optionsType_ProductTestSuite, url:'product.scmsystem.list?productId='+productId},
		{id:"executionTypeId", type:optionsType_ProductTestSuite, url:'common.list.executiontypemaster.byentityid?entitymasterid=7'}
		];
		
		returnOptionsItemForProductTestSuite(productTestSuiteOptionsArr[0].url, data);
}
// ----- Return options ----

function returnOptionsItemForProductTestSuite(url, data){
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
				productTestSuiteResultArr.push(json.Options);         	
				returnOptionsItemForProductTestSuite(url, data);         	
			 
			 }else{ 				
				for(var i=0;i<json.Options.length;i++){
					if(json.Options[i].DisplayText == null)
						json.Options[i].DisplayText = "--";	
					
					json.Options[i].label=json.Options[i].DisplayText;
					json.Options[i].value=json.Options[i].Value;
				}
			   productTestSuiteResultArr.push(json.Options);
			   
			   if(productTestSuiteOptionsItemCounter<productTestSuiteOptionsArr.length-1){			   
				   returnOptionsItemForProductTestSuite(productTestSuiteOptionsArr[productTestSuiteResultArr.length].url, data);     		  
			   }else{
					
					// ------ testScriptSource ------
					var json = {};					
					var Options = [{ "value": "TAF Server", "Number": null, "label": "TAF Server"},
						{ "value": "HPQC_TEST_PLAN", "Number": null, "label": "HPQC_TEST_PLAN"},
						{ "value": "HPQC_TEST_RESOURCES", "Number": null, "label": "HPQC_TEST_RESOURCES"},
						{ "value": "JENKINS", "Number": null, "label": "JENKINS"},
						{ "value": "HUDSON", "Number": null, "label": "HUDSON"},
					]
					json.Options = Options;			
					productTestSuiteResultArr.push(json.Options);				
						
					// ----- scriptPlatformLocation -----						
					json = {};					
					Options = [{ "value": "Terminal", "Number": null, "label": "Terminal"},
						{ "value": "Server", "Number": null, "label": "Server"}];						
					json.Options = Options;			
					
					productTestSuiteResultArr.push(json.Options);	
						
				  // ----- end -----
				  
				  productTestSuite_Container(data); 
			   }
			   productTestSuiteOptionsItemCounter++;         
			 }
		 },
         error: function (data) {
        	 productTestSuiteOptionsItemCounter++;        	 
         },
         complete: function(data){
         	//console.log('Completed');         	
         },
   	});	
}

function productTestSuiteTestcaseResultsEditor(){	
    
	testSuite_editor = new $.fn.dataTable.Editor( {
			"table": "#testSuite_dataTable",
    		ajax: "test.suite.add",
    		ajaxUrl: "test.suite.update",
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
				label:"isSelected",
				name:"isSelected",		
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
				options: productTestSuiteResultArr[0],
                "type"  : "select",	
            },{
                label: "Script Type",
                name: "testScriptType", 
				options: productTestSuiteResultArr[1],
                "type"  : "select",	
            },{
                label: "Script Source",
                name: "testScriptSource", 
				options: productTestSuiteResultArr[4],
                "type"  : "select",	
            },{
                label: "SCM System",
                name: "scmSystemId", 
				options: productTestSuiteResultArr[2],
                "type"  : "select",	
            },{
                label: "Script File Location",
                name: "testSuiteScriptFileLocation",                
            },{
                label: "Platform *",
                name: "scriptPlatformLocation", 
				options: productTestSuiteResultArr[5],
                "type"  : "select",	
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
				options: productTestSuiteResultArr[3],
                "type"  : "select",	
            },  
        ]
    	});	
}

var clearTimeoutDTProductTestSuite='';
function reInitializeDTProductTestSuite(){
	clearTimeoutDTProductTestSuite = setTimeout(function(){				
		productTestSuite_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTProductTestSuite);
	},200);
}

function productTestSuite_Container(data){
	if(testsuitesTabFlag){
		divId = '.testSuiteTab #jTableContainertestsuite';
	}else{
		divId = '#testSuiteDT_Child_Container #jTableContainertestsuite';
	}
	try{
		if ($('.testSuiteTab #jTableContainertestsuite').children().length>0) {
			$('.testSuiteTab #jTableContainertestsuite').children().remove();
		}
		if ($('#testSuiteDT_Child_Container #jTableContainertestsuite').children().length>0) {
			$('#testSuiteDT_Child_Container #jTableContainertestsuite').children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(14);  // total coulmn count				  
	  var childDivString = '<table id="testSuite_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $(divId).append(childDivString); 						  
	  
	  $("#testSuite_dataTable thead").html('');
	  $("#testSuite_dataTable thead").append(productTestSuiteHeader());
	  
	  $("#testSuite_dataTable tfoot tr").html('');     			  
	  $("#testSuite_dataTable tfoot tr").append(emptytr);
	
	// --- editor for the activity Change Request started -----
	productTestSuiteTestcaseResultsEditor();
			
	productTestSuite_oTable = $("#testSuite_dataTable").dataTable( {
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
	       /*fixedColumns:   {
	           leftColumns: 1,
	       }, */
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex; // search column TextBox Invisible Column position
			  (testsuitesTabFlag) ? searchcolumnVisibleIndex = [12] : searchcolumnVisibleIndex = [0,13];
     		  $("#testSuite_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
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
			   reInitializeDTProductTestSuite();			   
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: testSuite_editor },	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "Test Suite",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "Test Suite",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "Test Suite",
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
					text: '<i class="fa fa-tree" title="Testsuite Testcasestree"></i>',
					action: function ( e, dt, node, config ) {					
							//document.getElementById("hdnProductName").value = productId;
							$('#treePaginationContainer').css('z-index','10053');
							featureTCFlag=false;
							showTestSuiteTestCaseTree();
						}
					},	
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
           { mData: "testSuiteId",className: 'disableEditInline', sWidth: '5%' },		
           { mData: "testSuiteCode",className: 'editable', sWidth: '15%' },
           { mData: "testSuiteName",className: 'editable', sWidth: '15%' },			
			{ mData: "productVersionListId", className: 'editable', sWidth: '10%',
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(testSuite_editor, 'productVersionListId', full.productVersionListId);						
					 }else if(type == "display"){
						data = full.productVersionListName;
					 }	           	 
					 return data;
				 },
	        },	
           { mData: "testScriptType",className: 'editable', sWidth: '15%' },
           { mData: "testScriptSource",className: 'editable', sWidth: '15%' },			
			{ mData: "scmSystemId", className: 'editable', sWidth: '10%',
				mRender: function (data, type, full) {
					data = optionsValueHandler(testSuite_editor, 'scmSystemId', full.scmSystemId);						
					return data;
				 },
	        },
           { mData: "testSuiteScriptFileLocation",className: 'editable', sWidth: '15%' },
           { mData: "scriptPlatformLocation",className: 'editable', sWidth: '15%' },
		   { mData: "testCaseCount",className: 'disableEditInline', sWidth: '5%' },		
           { mData: "testStepsCount",className: 'disableEditInline', sWidth: '15%' },           
			{ mData: "executionTypeId", className: 'editable', sWidth: '10%',
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(testSuite_editor, 'executionTypeId', full.executionTypeId);						
					 }else if(type == "display"){
						data = optionsValueHandler(testSuite_editor, 'executionTypeId', full.executionTypeId);
					 }	           	 
					 return data;
				 },
	        },		  
			{ mData: null,				 
            	bSortable: false,
				sWidth: '2%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+					
     	       		'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
							'<img src="css/images/list_metro.png" class="productTestSuiteImg1" title="View TestCase" data-toggle="modal" /></button>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
							/*'<img src="css/images/execute_metro.png" class="productTestSuiteImg2" title="Execute TestCase" data-toggle="modal" /></button>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+*/
							'<img src="css/images/ATSG.JPG" style="width:50px;display:none" class="productTestSuiteImg3" title="ATSG" data-toggle="modal" /></button>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
							'<img src="css/images/mapping.png" class="productTestSuiteImg4" title="TestCase Mapping" data-toggle="modal" /></button>'+
					'<button style="border: none; background-color: transparent; outline: none;">'+
       							'<i class="fa fa-search-plus details-control productTestSuiteImg5" title="Audit History" style="padding-left: 0px;"></i></button>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
							'<img src="css/images/dt_download_features.png" class="icon_imgSize productTestSuiteImg6" title="Git Download" data-toggle="modal" /></button>'+
					'<button style="border: none; background-color: transparent; outline: none;">'+
       							'<i class="fa fa-comments details-control productTestSuiteImg7" title="Comments" style="padding-left: 0px;"></i></button>'+							
     	       		'</div>');	      		
           		 return img;
            	},
            },
       ], 
       columnDefs: [{ targets: 0, visible: !testsuitesTabFlag }],
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		 
		 $("#testSuite_dataTable_length").css('margin-top','8px');
		 $("#testSuite_dataTable_length").css('padding-left','35px');
		 		  	 
		 // Activate an inline edit on click of a table cell
        $('#testSuite_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	testSuite_editor.inline( this, {
        		submitOnBlur: true
			}); 
		});
		
        testSuite_editor.on( 'preSubmit', function ( e, o, action ) {
	        if ( action !== 'remove' ) {
	        	var validationArr = ['testSuiteCode','testSuiteName'];
	        	var testCaseName;
	        	for(var i=0;i<validationArr.length;i++){
		            testCaseName = this.field(validationArr[i]);
		            if ( ! testCaseName.isMultiValue() ) {
		                if ( testCaseName.val() ) {
	                	}else{
		                	if(validationArr[i] == "testSuiteCode")
		                		testCaseName.error( 'Please enter Testsuite code' );
		                	if(validationArr[i] == "testSuiteName")
		                		testCaseName.error( 'Please enter Testsuite name' );
	                	}
		            }
	        	}

	            // If any error was reported, cancel the submission so it can be corrected
	            if ( this.inError() ) {
	                return false;
	            }
	        }
	    } ); 
        
        productTestSuite_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
		} );
		
		$('#testSuite_dataTable tbody').on( 'change', 'input.isSelected-active', function (e) {
			var tr = $(this).closest('tr');
			var row = productTestSuite_oTable.DataTable().row(tr);
			var url="";
			var testSuiteData = $(productTestSuite_oTable.fnGetNodes());
			var tSuiteArr=[];
			
			for(var i=0;i<testSuiteData.length;i++){
				if(testSuiteData.eq(i).find('td input').attr('checked') == "checked"){
					tSuiteArr.push(testSuiteData.eq(i).find('td').eq(1).html);
				}
			}
			
			if(tSuiteArr.length>1){
				e.target.checked=false;
				productTestSuite_oTable.DataTable().rows().deselect();
				callAlert('Multiple Test Suite is not allowed. Please unmap the already selected Test Suite.');
				return false;
			}
			
			if(!$(this).is(':checked')){
				if(rnCnfgFlag){
					url="administration.testsuite.mapunmaprunconfig?runConfigId="+rnCnfgId+"&testSuiteId="+row.data().testSuiteId+"&action=Remove";
				}else{
					url="administration.testsuite.mapunmaprunConfiguration?productversionId="+row.data().productVersionListId+"&productId="+row.data().productId+"&testRunPlanId="+tpcId+"&testSuiteId="+row.data().testSuiteId+"&type=Remove";
				}
			}else{
				if(rnCnfgFlag){
					url="administration.testsuite.mapunmaprunconfig?runConfigId="+rnCnfgId+"&testSuiteId="+row.data().testSuiteId+"&action=Add";
				}else{
					url="administration.testsuite.mapunmaprunConfiguration?productversionId="+row.data().productVersionListId+"&productId="+row.data().productId+"&testRunPlanId="+tpcId+"&testSuiteId="+row.data().testSuiteId+"&type=Add";
				}
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
		 
		 // ----- productTestSuiteImg1 -----
		 
		 $('#testSuite_dataTable tbody').on('click', 'td button .productTestSuiteImg1', function () {
				var tr = $(this).closest('tr');
			    var row = productTestSuite_oTable.DataTable().row(tr);
				
				$("#testSuiteTestCaseListChild_Container").modal();
				
				testSuiteTestCaseListChildOptionsItemCounter=0;
				testSuiteTestCaseListChildResultArr=[];
	
				testSuiteTestCaseListChildOptionsArr = [	                                  					   	
				   {id:"executionTypeId", type:optionsType_testSuiteTestCaseListChild, url:'common.list.executiontypemaster.byentityid?entitymasterid=3'},
				   {id:"testcasePriorityId", type:optionsType_testSuiteTestCaseListChild, url:'common.list.testcasepriority'},				   
	             ];
				testSuitecaseslistOptions(testSuiteTestCaseListChildOptionsArr[0].url, row.data().testSuiteId);
			    				
			   // var url='test.suite.case.list?testSuiteId='+row.data().testSuiteId+'&jtStartIndex=0&jtPageSize=10000';		
				//testSuitecaseslistOfSelectedProduct(url);				    
		 });
		 
		 // ----- Execute TestCase -----
		 
		 $('#testSuite_dataTable tbody').on('click', 'td button .productTestSuiteImg2', function () {
			var tr = $(this).closest('tr');
			var row = productTestSuite_oTable.DataTable().row(tr);
			
			testCaseId =	row.data().testCaseId;	
			testCaseExecution = 'TestSuiteExecution';
			testRunPLanListByTestCaseId();								    
		 });
		 
		 // ----- Download Skeleton Test Scripts -----
		 /*
		 $('#testSuite_dataTable tbody').on('click', 'td button .productTestSuiteImg3', function () {
			var tr = $(this).closest('tr');
			var row = productTestSuite_oTable.DataTable().row(tr);
							
			displayDownloadTestScriptsFromTestCases(row.data().testSuiteId, scriptTestSuiteName, scriptDownloadName);				    
		 });
		 */
		 // ----- ATSG -----
		 
		 $('#testSuite_dataTable tbody').on('click', 'td button .productTestSuiteImg3', function () {
			var tr = $(this).closest('tr');
			var row = productTestSuite_oTable.DataTable().row(tr);
			
			var result = (row.data().testSuiteId+'~'+row.data().testSuiteName+'~'+null).toString();
			scriptsFor="TestSuite";
			scriptTypeTile = "Test Suite ";	
			displayATSGPopupHandler(result);		    												    
		 });
		 
		 // ----- TestCase Mapping -----
		 
		 $('#testSuite_dataTable tbody').on('click', 'td button .productTestSuiteImg4', function () {
			var tr = $(this).closest('tr');
			var row = productTestSuite_oTable.DataTable().row(tr);
							
			testcaseMapping(row.data().testSuiteId, row.data().testSuiteName); 				    
		 });
		 
		 // ----- Audit History -----
		 
		 $('#testSuite_dataTable tbody').on('click', 'td button .productTestSuiteImg5', function () {
			var tr = $(this).closest('tr');
			var row = productTestSuite_oTable.DataTable().row(tr);
			$('#div_SingleDataTableSummary').css({'z-index':'10054','top':'2%'});				
			listGenericAuditHistory(row.data().testSuiteId,"TestSuite","testSuiteAudit");				    
		 });
		 
		 // ----- Git Download -----
		 
		 $('#testSuite_dataTable tbody').on('click', 'td button .productTestSuiteImg6', function () {
			var tr = $(this).closest('tr');
			var row = productTestSuite_oTable.DataTable().row(tr);
							
			gitDownload(row.data().testSuiteId, row.data().productId,row.data().scmSystemId);				    
		 });
		 
		 // ----- Comments -----
		 
		 $('#testSuite_dataTable tbody').on('click', 'td button .productTestSuiteImg7', function () {
			var tr = $(this).closest('tr');
			var row = productTestSuite_oTable.DataTable().row(tr);
							
			var entityTypeIdComments = 7;
			var entityNameComments = "TestSuite";
			listComments(entityTypeIdComments, entityNameComments, row.data().testSuiteId, row.data().testSuiteName, "tSuiteComments");
		 });
		 // ----- ended -----
		 
		/* testSuite_editor.on('onPostCreate', function(e, data, action) {			
			var url='get.testcase.script.list?productId='+productId+'&jtStartIndex=0&jtPageSize=10000';		
			listTestCaseScriptOfSelectedProduct(url, "180px", "parentTable", "", productId);
		});	
		*/
		 	 		 		
	// ------	
	});
}

function productTestSuiteHeader(){		
	var tr = '<tr>'+			
		'<th></th>'+
		'<th>ID</th>'+
		'<th>Code</th>'+
		'<th>Name</th>'+
		'<th>Version</th>'+
		'<th>Script Type</th>'+
		'<th>Script Source</th>'+
		'<th>SCM System</th>'+
		'<th>Script File Location</th>'+	
		'<th>Platform</th>'+	
		'<th>Testcases Count</th>'+
		'<th>Teststeps Count</th>'+	
		'<th>Execution Type</th>'+	
		'<th></th>'+	
	'</tr>';
	return tr;
}

var testSuiteTestCaseListChildOptionsArr=[];
var testSuiteTestCaseListChildResultArr=[];
var testSuiteTestCaseListChildOptionsItemCounter=0;
var optionsType_testSuiteTestCaseListChild="TestSuiteTestCaseListChild";

function testSuitecaseslistOptions(url, testSuiteID){
	//$("#testSuiteTestCaseListChildLoaderIcon").show();	
	
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(json) {					
			//$("#testSuiteTestCaseListChildLoaderIcon").hide();			
			
			if(json.Options.length>0){     		   
			   for(var i=0;i<json.Options.length;i++){
				   json.Options[i].label=json.Options[i].DisplayText;
				   json.Options[i].value=json.Options[i].Value;
			   }			   
     	   }else{
     		  json.Options=[];
     	   }     	   
     	   testSuiteTestCaseListChildResultArr.push(json.Options);
     	   		   
     	   if(testSuiteTestCaseListChildOptionsItemCounter<testSuiteTestCaseListChildOptionsArr.length-1){
     		 testSuitecaseslistOptions(testSuiteTestCaseListChildOptionsArr[testSuiteTestCaseListChildOptionsArr.length-1].url, testSuiteID);     		       	  			 
		  }else{
			 var url; 
			 if(testsuitesTabFlag){ 
				 url='test.suite.case.list?testSuiteId='+testSuiteID+'&jtStartIndex=0&jtPageSize=10000';
			 }else{
				 url='test.suite.case.runconfig.list?runConfigId='+rnCnfgId+'&testSuiteId='+testSuiteID+'&jtStartIndex=0&jtPageSize=10000';
			 }
			 
			 testSuitecaseslistOfSelectedProduct(url);			 
		  }
			testSuiteTestCaseListChildOptionsItemCounter++;		  
		  
		  },
		  error : function(data) {			 
			testSuiteTestCaseListChildOptionsItemCounter++;
			// $("#testSuiteTestCaseListChildLoaderIcon").hide();
		 },
		 complete: function(data){			
			//$("#testSuiteTestCaseListChildLoaderIcon").hide();
		 }
	});
}


function testSuitecaseslistOfSelectedProduct(url){
	$("#testSuiteTestCaseListChildLoaderIcon").show();	
	
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {					
			$("#testSuiteTestCaseListChildLoaderIcon").hide();
			
			if(data == null || data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			testSuitecaseslistOfSelectedProduct_content(data);
			
		  },
		  error : function(data) {			 
			 $("#testSuiteTestCaseListChildLoaderIcon").hide();
		 },
		 complete: function(data){			
			$("#testSuiteTestCaseListChildLoaderIcon").hide();
		 }
	});
}

var testsuitetestcaseslist_oTable='';
var clearTimeoutDTTestsuitetestcaseslist='';
var testSuiteTestCaseListChild_editor='';

function reInitializeDTTestsuiteTestcaseslist(){
	clearTimeoutDTTestsuitetestcaseslist = setTimeout(function(){				
		testsuitetestcaseslist_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTTestsuitetestcaseslist);
	},200);
}

function testSuiteTestCaseListChildEditor(){	
    
	testSuiteTestCaseListChild_editor = new $.fn.dataTable.Editor( {
			"table": "#testsuitetestcaseslist_dataTable",
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
			},{
                label: "Test Case Code *",
                name: "testCaseCode",                
            },{
                label: "Test Case Name *",
                name: "testCaseName",                
            },{
                label: "Test Case Description *",
                name: "testCaseDescription",                
            },{								
				label:"Execution Order",
				name:"testCaseExecutionOrder",													
			},{
                label: "Execution Type",
                name: "executionTypeId",
                 options: testSuiteTestCaseListChildResultArr[0],
                "type"  : "select",
            },{
                label: "Priority",
                name: "testcasePriorityId",
                 options: testSuiteTestCaseListChildResultArr[1],
                "type"  : "select",
            },
        ]
    	});	
}

function testSuitecaseslistOfSelectedProduct_content(data){
	try{
		if ($("#testSuiteTestCaseListChild_Content").children().length>0) {
			$("#testSuiteTestCaseListChild_Content").children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(9);  // total coulmn count		  
	  var childDivString = '<table id="testsuitetestcaseslist_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#testSuiteTestCaseListChild_Content").append(childDivString); 						  
	  
	  $("#testsuitetestcaseslist_dataTable thead").html('');
	  $("#testsuitetestcaseslist_dataTable thead").append(testSuiteTestCaseListChild_ContentHeader());
	  
	  $("#testsuitetestcaseslist_dataTable tfoot tr").html('');     			  
	  $("#testsuitetestcaseslist_dataTable tfoot tr").append(emptytr);
	  
	  // --- editor for the activity Change Request started -----
	testSuiteTestCaseListChildEditor();
	  
	  testsuitetestcaseslist_oTable = $("#testsuitetestcaseslist_dataTable").dataTable( {
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
			  var searchcolumnVisibleIndex = [0,8]; // search column TextBox Invisible Column position
     		  $("#testsuitetestcaseslist_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
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
			   reInitializeDTTestsuiteTestcaseslist();			   
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: testSuiteTestCaseListChild_editor },	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "TestsuiteTestcaseslist",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "TestsuiteTestcaseslist",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "TestsuiteTestcaseslist",
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
		   { mData: "testCaseId",className: 'disableEditInline', sWidth: '3%' },
           { mData: "testCaseCode",className: 'disableEditInline', sWidth: '10%' },
           { mData: "testCaseName",className: 'disableEditInline', sWidth: '20%' },           
		   { mData: "testCaseDescription",className: 'editable', sWidth: '25%' },           
		   { mData: "testCaseExecutionOrder",className: 'editable', sWidth: '3%' },		   		   
		   { mData: "executionTypeId", className: 'disableEditInline', sWidth: '10%',
				mRender: function (data, type, full) {
					 data = optionsValueHandler(testSuiteTestCaseListChild_editor, 'executionTypeId', full.executionTypeId);
					 return data;
				 },
	       },									
           { mData: "testcasePriorityId", className: 'disableEditInline', sWidth: '10%',		
			  mRender: function (data, type, full) {
					 data = optionsValueHandler(testSuiteTestCaseListChild_editor, 'testcasePriorityId', full.testcasePriorityId);	           	 
					 return data;
				 },	            
            },                     
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
       				'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px; margin-left: 5px;">'+
 	       					'<i class="fa fa-history testsuitetestcaseslistChildImg1" title="Execution Summary and History"></i></button>'+		
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
		 $("#testsuitetestcaseslist_dataTable_length").css('margin-top','8px');
		 $("#testsuitetestcaseslist_dataTable_length").css('padding-left','35px');
		 
		 $("#testsuitetestcaseslist_dataTable_wrapper").find(".buttons-create").hide();
		 
		 $('#testsuitetestcaseslist_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	testSuiteTestCaseListChild_editor.inline( this, {
        		submitOnBlur: true
			}); 
		});
		
		// ----- focus area -----	
			
		$( 'input', testSuiteTestCaseListChild_editor.node()).on( 'focus', function () {
			this.select();
		});
		
		testsuitetestcaseslist_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
		} );
		
		$('#testsuitetestcaseslist_dataTable').on( 'change', 'input.isSelected-active', function () {
			var tr = $(this).closest('tr');
	    	var row = testsuitetestcaseslist_oTable.DataTable().row(tr);
			var url="";
			if(!$(this).is(':checked')){
				url="administration.testsuite.testcase.maprunconfig?testCaseId="+row.data().testCaseId+"&runConfigId="+rnCnfgId+"&testSuiteId="+row.data().testSuiteId+"&type=Remove";
			}else{
				url="administration.testsuite.testcase.maprunconfig?testCaseId="+row.data().testCaseId+"&runConfigId="+rnCnfgId+"&testSuiteId="+row.data().testSuiteId+"&type=Add";
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
		 
		 // ----- Execution Summary -----
		 
		 $('#testsuitetestcaseslist_dataTable tbody').on('click', 'td button .testsuitetestcaseslistChildImg1', function () {
			var tr = $(this).closest('tr');
			var row = testsuitetestcaseslist_oTable.DataTable().row(tr);
			
			var dataLevel = "productLevel";
			listTCExecutionSummaryHistoryView(row.data().testCaseId, row.data().testCaseName, dataLevel);
			
		 });	
	});
	
}

function testSuiteTestCaseListChild_ContentHeader(){		
	var tr = '<tr>'+					
		'<th>Selected</th>'+
		'<th>ID</th>'+
		'<th>Code</th>'+
		'<th>Name</th>'+
		'<th>Description</th>'+
		'<th>Execution Order</th>'+
		'<th>Execution Type</th>'+		
		'<th>Priority</th>'+				
		'<th></th>'+
	'</tr>';
	return tr;
}

function gitDownload(testSuiteId, productId, scmSystemId){
	if(scmSystemId == null){
		callAlert('Please select SCM System');
		return false;
	}
	openLoaderIcon();
	var url = "git.download.script?testSuiteId="+testSuiteId+'&productId='+productId+'&scmSystemId='+scmSystemId;
	$.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
						
			if(data.Result=="ERROR"){
    		    data = [];	
    		    callAlert("Not Downloaded");
    		    closeLoaderIcon();
			}else{
				if(data.Result =="OK") {
					closeLoaderIcon();
					callAlert(data.Message);
					displayTabActivitySummaryHandler(activityId);
				} else {
					callAlert("Not Downloaded");
					closeLoaderIcon();
				}
			}
		  },
		  error : function(data) {
			  callAlert("Not Downloaded");
			  closeLoaderIcon();
		 },
		
	});	
}	

function testcaseMapping(testSuiteId, testSuiteName){
	$('#searchdev').keyup(function() {
		var txt=$('#searchdev').val();		
		$("#listCount_allTestcasesTestSuites").text("0");
		var resArr = [];
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();		
			$('#alltestcases li').show().filter(function() {	    	
	    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	        if(!~text.indexOf(val) == false) resArr.push("item");
	        
	    	  return !~text.indexOf(val);	    
	    }).hide();			
			$("#listCount_allTestcasesTestSuites").text(+resArr.length+" / "+$('#alltestcases li').length);
			if(txt==""){
				$("#listCount_allTestcasesTestSuites").text($('#alltestcases li').length);
			
			}
	});	
	
	$('#searchmapdev').keyup(function() {
		var txt=$('#searchmapdev').val();
		if($('#searchmapdev').value==''){
			$("#listCount_TestcasesTestSuitesmapped").text($('#testcasemapped1 li').length);
		}	
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
	    var resArr = [];
		$('#testcasemapped1 li').show().filter(function() {	    	
	    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	    	 if(!~text.indexOf(val) == false) resArr.push("item");
	        return !~text.indexOf(val);	        
	    }).hide();
		
		$("#listCount_TestcasesTestSuitesmapped").text(+resArr.length+" / "+$('#testcasemapped1 li').length);
		if(txt==""){
			$("#listCount_TestcasesTestSuitesmapped").text($('#testcasemapped1 li').length);		
		}
	});
	
	$(".tilebody").empty();
	$(".tilebody").remove();
	
	document.getElementById("div_PopupTestcase").style.display = "block";
	$("#alltestcases").empty();
	var versionId= 0;
	versionId= document.getElementById("hdnProductVersionId").value;
	if( versionId == ""){
		versionId= 0;
	}	
	var tc_length=0;
	var ts_length=0;
	var tc_ts_total = 0;
	$("#div_PopupTestcase").find("h4").text("Map Testcases to TestSuite  :- "+ testSuiteName );
	var url = 'product.testsuite.unmapped.testcase.list?productId='+productId+"&testSuiteId="+testSuiteId+'&status=Approved&jtStartIndex=0&jtPageSize=1000';
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {			
			var result = data.Records;
			if(result.length==0){
				
				 tc_length = result.length;
				 tc_ts_total = tc_length+ts_length;				 
				 $("#listCount_allTestcasesTestSuites").text(tc_ts_total);
			}else{
				$.each(result, function(i,item){ 
					// $("#alltestcases").append("<li title='"+item.testCaseName+"' style='color: white;'>"+item.testCaseId+"</li>");
					var tcid = item.testCaseId;					
					tcid= tcid+"("+item.testCaseName+")";
					tcid=trim(tcid);
					 $("#alltestcases").append("<li title='"+item.testCaseName+"' style='color: black;'>"+tcid+"</li>");
					 tc_length = result.length;
					 tc_ts_total = tc_length+ts_length;
					 $("#listCount_allTestcasesTestSuites").text(tc_ts_total);
			 	});
			}			
		}
	});  
	
	var tc_mapped_length=0;
	var ts_mapped_length=0;
	var tc_ts_mapped_total = 0;
	url="test.suite.case.list?testSuiteId="+testSuiteId;
	 $("#testcasemapped1").empty();	
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {			
			var listOfData = data.Records;			
			
			if(listOfData.length==0){			
				 tc_mapped_length = listOfData.length;
				 tc_ts_mapped_total = tc_mapped_length+ts_mapped_length;			
				 $("#listCount_TestcasesTestSuitesmapped").text(tc_ts_mapped_total);
			}else{
				$.each(listOfData, function(i,item){					 
					 var tc_id = item.testCaseId;					
					 tc_id= tc_id+" ("+item.testCaseName+")";
					 tc_id=trim(tc_id);
					 $("#testcasemapped1").append("<li  title='"+item.testCaseName+"' style='color: black;'>"+tc_id+"</li>");
					 tc_mapped_length = listOfData.length;
					 tc_ts_mapped_total = tc_mapped_length+ts_mapped_length;
					 $("#listCount_TestcasesTestSuitesmapped").text(tc_ts_mapped_total);
			 	});
			}
		}
	});   
	urlmappedTestSuite="test.suite.of.testsuite.list?testSuiteId="+testSuiteId;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlmappedTestSuite,
		dataType : 'json',
		success : function(data) {			
			var listOfData = data.Records;	
			if(listOfData.length==0){			
				ts_mapped_length = listOfData.length;
				tc_ts_mapped_total = tc_mapped_length+ts_mapped_length;	
				$("#listCount_TestcasesTestSuitesmapped").text(tc_ts_mapped_total);
			}else{
				$.each(listOfData, function(i,item){					
					var tsid = item.testSuiteId;					
					tsid= tsid+"(TestSuite)";
					tsid=trim(tsid);
					$("#testcasemapped1").append("<li  title='"+item.testSuiteName+"' style='color: black;'>"+tsid+"</li>");
					ts_mapped_length = listOfData.length;
					tc_ts_mapped_total = tc_mapped_length+ts_mapped_length;
					$("#listCount_TestcasesTestSuitesmapped").text(tc_ts_mapped_total);					
			 	});
			}
		}
	});	
	
	Sortable.create(document.getElementById("alltestcases"), {			    
		group: "words",
		animation: 150,
		store: {
			get: function (sortable) {
				var order = localStorage.getItem(sortable.options.group);
				return order ? order.split('|') : [];
			},
			set: function (sortable) {
				var order = sortable.toArray();
				localStorage.setItem(sortable.options.group, order.join('|'));
			}
		},		
		
		onAdd: function (evt){					
			var draggeditem = trim(evt.item.innerText);
			var x =  draggeditem.split("(");
			var itemid = x[0];
			var itemType = x[1].replace(")","");
						
		 	var urlUnMapping = "";
			if(itemType == "TestSuite"){				
				urlUnMapping = 'test.suite.delete.testsuite.bulk?testSuiteId='+testSuiteId+"&testSuiteIdtobeUnMapped="+itemid;
			}else{
				urlUnMapping = 'test.suite.case.delete.bulk?testcaseId='+itemid+"&testSuiteId="+testSuiteId;
			}
			 
			 if($("#emptyListAll").length) $("#emptyListAll").remove();
				$("#listCount_allTestcasesTestSuites").text($("#alltestcases").children().length);
			 $.ajax({
				  type: "POST",			
					url:urlUnMapping,
					success : function(data) {
						if(data.Result=="ERROR"){
							callAlert(data.Message);
				    		return false;
				    	}
						else{	
							}
						}					
				});
			},
		onUpdate: function (evt){  },
		onRemove: function (evt){
			if($("#alltestcases").children().length == 0) {
		 		$("#alltestcases").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 46px;'>No TestCases/TestSuites</b></span>");
		 		$("#listCount_allTestcasesTestSuites").text(0);
		 	}else{
				$("#listCount_allTestcasesTestSuites").text($("#alltestcases").children().length);
		 	}
		},
		
		onStart:function(evt){},
		onSort:function(evt){ },
		onEnd: function(evt){ }
	});
	
	 Sortable.create(document.getElementById("testcasemapped1"), {
			group: "words",
			animation: 150,
			onAdd: function (evt){								
				var draggeditem = trim(evt.item.innerText);
				var x =  draggeditem.split("(");
				var itemid = x[0];
				var itemType = x[1].replace(")","");								
			 	var urlMapping = "";
			 	var itm = evt.item;
				 if(itemType == "TestSuite"){			
					 urlMapping='test.suite.to.testsuite.add.bulk?testSuiteId='+testSuiteId+"&testSuiteIdtobeMapped="+itemid;
				}else{
					urlMapping='test.suite.case.add.bulk?testcaseId='+itemid+"&testSuiteId="+testSuiteId;
				}
				 if($("#emptyListAll").length) $("#emptyListAll").remove();
					$("#listCount_TestcasesTestSuitesmapped").text($("#testcasemapped1").children().length);
				 $.ajax({
					  type: "POST",					  
						url:urlMapping,
						success : function(data) {
							if(data.Result=="ERROR"){
								$(itm).remove();
								callAlert(data.Message);
					    		return false;
					    	}
							else{
								 
								}							
							}						
					});
			},
			onUpdate: function (evt){  },
			onRemove: function (evt){
				if($("#testcasemapped1").children().length == 0) {
			 		$("#testcasemapped1").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 46px;'>No Testcases/TestSuites</b></span>");
			 		$("#listCount_TestcasesTestSuitesmapped").text(0);
			 	}else{
					$("#listCount_TestcasesTestSuitesmapped").text($("#testcasemapped1").children().length);
				}	
			},
			
			onStart:function(evt){ },
			onEnd: function(evt){}
		});
}
 