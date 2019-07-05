var productTestScriptOptionsArr=[];
var productTestScriptResultArr=[];
var productTestScriptOptionsItemCounter=0;
var productTestScript_oTable='';
var optionsType_ProductTestScript="ProductTestScript";
var testScript_editor='';

function testScripts(){		
	setProductNode();
	
	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#Testcasescripts').children().show();		
		var url='get.testcase.script.list?productId='+productId+'&jtStartIndex=0&jtPageSize=10000';		
		listTestCaseScriptOfSelectedProduct(url, "180px", "parentTable", "", productId);
	}	
}

function listTestCaseScriptOfSelectedProduct(url, ScrollValue, tableValue, row, tr){
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
				data = data.Records;
			}			
			productTestScriptResults_Container(data, ScrollValue, row, tr);			
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

function productTestScriptResults_Container(data, scrollYValue, row, tr){	
	var atype="default";
	productTestScriptOptionsItemCounter=0;
	productTestScriptResultArr=[];			
	productTestScriptOptionsArr = [{id:"language", type:optionsType_ProductTestScript, url:'common.list.scripttype'},
		{id:"source", type:optionsType_ProductTestScript, url:'common.list.scripttype'},
		//{id:"scriptVersion", type:optionsType_ProductTestScript, url:'common.list.productversion?productId='+productId},
		{id:"primaryTestEngineId", type:optionsType_ProductTestScript, url:'common.list.testToolMaster.option'}];
		
		returnOptionsItemForProductTestScript(productTestScriptOptionsArr[0].url, scrollYValue, data, row, tr);
}
// ----- Return options ----

function returnOptionsItemForProductTestScript(url, scrollYValue, data, row, tr){
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
				productTestScriptResultArr.push(json.Options);         	
				returnOptionsItemForProductTestScript(url, scrollYValue, data, row, tr);         	
			 
			 }else{ 
				if(productTestScriptOptionsArr[productTestScriptResultArr.length].id == 'source'){
					json.Options = [{"Value": "TAF Server","Number": null,"DisplayText": "TAF Server"},
						{"Value": "HPQC_TEST_PLAN","Number": null,"DisplayText": "HPQC_TEST_PLAN"},
						{"Value": "HPQC_TEST_RESOURCES","Number": null,"DisplayText": "HPQC_TEST_RESOURCES"},
						{"Value": "JENKINS","Number": null,"DisplayText": "JENKINS"},
						{"Value": "HUDSON","Number": null,"DisplayText": "HUDSON"},					
					];
				}
				
				for(var i=0;i<json.Options.length;i++){
					if(json.Options[i].DisplayText == null)
						json.Options[i].DisplayText = "--";	
					
					json.Options[i].label=json.Options[i].DisplayText;
					json.Options[i].value=json.Options[i].Value;
				}
			   productTestScriptResultArr.push(json.Options);
			   
			   if(productTestScriptOptionsItemCounter<productTestScriptOptionsArr.length-1){			   
				   returnOptionsItemForProductTestScript(productTestScriptOptionsArr[productTestScriptResultArr.length].url, scrollYValue, data, row, tr);     		  
			   }else{   			  	      			       			   				  				  
				  productTestScript_Container(data, scrollYValue); 
			   }
			   productTestScriptOptionsItemCounter++;         
			 }
		 },
         error: function (data) {
        	 productTestScriptOptionsItemCounter++;        	 
         },
         complete: function(data){
         	//console.log('Completed');         	
         },
   	});	
}

function productTestScriptResultsEditor(row){	
    
	testScript_editor = new $.fn.dataTable.Editor( {
			"table": "#testScript_dataTable",
    		ajax: "testcase.script.add",
    		ajaxUrl: "testcase.script.update",
    		idSrc:  "scriptId",
    		i18n: {
    	        create: {
    	            title:  "Create a new TestCase Script",
    	            submit: "Create",
    	        }
    	    },
    		fields: [{								
				label:"productId",
				name:"productId",					
				type: 'hidden',				
				def: productId
			},{								
				label:"scriptId",
				name:"scriptId",					
				type: 'hidden',								
			},{
                label: "Testcase Script Name  *",
                name: "scriptName",                
            },{
                label: "Script Qualified Name  *",
                name: "scriptQualifiedName",                
            },{
                label: "Language",
                name: "language", 
				options: productTestScriptResultArr[0],
                "type"  : "select",	
            },{
                label: "Source",
                name: "source",
                 options: productTestScriptResultArr[1],
                "type"  : "select",
            },{								
				label:"Script Version",
				name:"scriptVersion",					
				//options: productTestScriptResultArr[2],
                //"type"  : "select",			
			},{								
				label:"Revision",
				name:"revision",												
			},{								
				label:"URI",
				name:"uri",								
			},{
                label: "Test Tool",
                name: "primaryTestEngineId",
                options: productTestScriptResultArr[2],
                "type"  : "select",
            }   
        ]
    	});	
}

var clearTimeoutDTProductTestScript='';
function reInitializeDTProductTestScript(){
	clearTimeoutDTProductTestScript = setTimeout(function(){				
		productTestScript_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTProductTestScript);
	},200);
}

function productTestScript_Container(data, row, tr){
	try{
		if ($("#jTableContainerTestCaseScript").children().length>0) {
			$("#jTableContainerTestCaseScript").children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(10);  // total coulmn count				  
	  var childDivString = '<table id="testScript_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#jTableContainerTestCaseScript").append(childDivString); 						  
	  
	  $("#testScript_dataTable thead").html('');
	  $("#testScript_dataTable thead").append(productTestScriptHeader());
	  
	  $("#testScript_dataTable tfoot tr").html('');     			  
	  $("#testScript_dataTable tfoot tr").append(emptytr);
	
	// --- editor for the activity Change Request started -----
	productTestScriptResultsEditor(row);
			
	productTestScript_oTable = $("#testScript_dataTable").dataTable( {
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
			  var searchcolumnVisibleIndex = [9]; // search column TextBox Invisible Column position
     		  $("#testScript_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
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
			   reInitializeDTProductTestScript();			   
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: testScript_editor },	  
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
           { mData: "scriptId",className: 'disableEditInline', sWidth: '5%' },		
           { mData: "scriptName",className: 'editable', sWidth: '15%' },
           { mData: "scriptQualifiedName",className: 'editable', sWidth: '15%' },           		   
			{ mData: "language", className: 'editable', sWidth: '10%',
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(testScript_editor, 'language', full.language);
						//data = full.language;
					 }else if(type == "display"){
						data = full.language;
					 }	           	 
					 return data;
				 },
	        },		             		  
		   { mData: "source", className: 'editable', sWidth: '10%',
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						//data = optionsValueHandler(testScript_editor, 'source', full.source);
						data = full.source;
					 }else if(type == "display"){
						data = full.source;
					 }	           	 
					 return data;
				 },
	        },		              
		 /*  { mData: "scriptVersion", className: 'editable', sWidth: '10%', editField: "scriptVersion",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(testScript_editor, 'scriptVersion', full.scriptVersion);
					 }else if(type == "display"){
						data = full.scriptVersion;
					 }	           	 
					 return data;
				 },
	        },*/
	        { mData: "scriptVersion",className: 'editable', sWidth: '10%' },   
		   { mData: "revision",className: 'editable', sWidth: '10%' },   
		   { mData: "uri",className: 'editable', sWidth: '10%' },   		   
			{ mData: "primaryTestEngineId", className: 'editable', sWidth: '10%', editField: "primaryTestEngineId",
				mRender: function (data, type, full) {
					data = optionsValueHandler(testScript_editor, 'primaryTestEngineId', full.primaryTestEngineId);
					return data;
				 },
	        },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '2%',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+					
     	       		'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
							'<img src="css/images/mapping.png" class="productTestScriptImg1" title="TestCase Mapping" data-toggle="modal" /></button>'+
					'<button style="border: none; background-color: transparent; outline: none;">'+
       							'<i class="fa fa-trash-o details-control" onClick="deleteTestScript('+data.scriptId+')" title="Delete TestCase" style="padding-left: 0px;"></i></button>'+	
     	       		'</div>');	      		
           		 return img;
            	},
            },
       ], 			   
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		 
		 //$("#activityTabLoaderIcon").hide();
		 
		 $("#testScript_dataTable_length").css('margin-top','8px');
		 $("#testScript_dataTable_length").css('padding-left','35px');
		 		  	 
		 // Activate an inline edit on click of a table cell
        $('#testScript_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	testScript_editor.inline( this, {
        		submitOnBlur: true
			}); 
		});

        testScript_editor.on( 'preSubmit', function ( e, o, action ) {
	        if ( action !== 'remove' ) {
	        	var validationArr = ['scriptName','scriptQualifiedName'];
	        	var testCaseName;
	        	for(var i=0;i<validationArr.length;i++){
		            testCaseName = this.field(validationArr[i]);
		            if ( ! testCaseName.isMultiValue() ) {
		                if ( testCaseName.val() ) {
	                	}else{
		                	if(validationArr[i] == "scriptName")
		                		testCaseName.error( 'Please enter Script name' );
		                	if(validationArr[i] == "scriptQualifiedName")
		                		testCaseName.error( 'Please enter Script qualified name' );
	                	}
		            }
	        	}

	            // If any error was reported, cancel the submission so it can be corrected
	            if ( this.inError() ) {
	                return false;
	            }
	        }
	    } );
        
		productTestScript_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );			
		 
		 $('#testScript_dataTable tbody').on('click', 'td button .productTestScriptImg1', function () {
				var tr = $(this).closest('tr');
			    var row = productTestScript_oTable.DataTable().row(tr);
			    				
				var testCaseScriptId = row.data().scriptId;								
				// ----- DragDrop Testing started----		
				var leftUrl="unmapped.testcases.count?productId="+productId+"&scriptId="+testCaseScriptId+"&filter=1";							
				var rightUrl = "";							
				var leftDefaultUrl="unmapped.testcases.list?productId="+productId+"&scriptId="+testCaseScriptId+"&filter=1&jtStartIndex=0&jtPageSize=50";							
				var rightDefaultUrl="testCases.mapped.to.testScript.list?scriptId="+testCaseScriptId+"&filter=1";
				var leftDragUrl = "testCaseScript.map.or.unmap.to.testCase?scriptId="+testCaseScriptId;
				var rightDragtUrl = "testCaseScript.map.or.unmap.to.testCase?scriptId="+testCaseScriptId;						
				var leftPaginationUrl = "unmapped.testcases.list?productId="+productId+"&scriptId="+testCaseScriptId+"&filter=1";
				var rightPaginationUrl="";	
				
				jsonTestCaseScriptObj={"Title":"Map Test Cases to TestCase Script :- "+row.data().scriptName,
						"leftDragItemsHeaderName":"Available Test Cases",
						"rightDragItemsHeaderName":"Mapped Test Cases",
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
						"noItems":"No Test Cases",
						"componentUsageTitle":"TestCaseScript-TestCaseList",
						};
				DragDropListItems.init(jsonTestCaseScriptObj);							
				// DragDrop Testing ended----							    
		 });
		 // ----- ended -----
		 
		 testScript_editor.on('onPostCreate', function(e, data, action) {			
			var url='get.testcase.script.list?productId='+productId+'&jtStartIndex=0&jtPageSize=10000';		
			listTestCaseScriptOfSelectedProduct(url, "180px", "parentTable", "", productId);
		});	
		 	 		 		
	// ------	
	});
}

function productTestScriptHeader(){		
	var tr = '<tr>'+			
		'<th>ID</th>'+
		'<th>Name</th>'+
		'<th>Qualified Name</th>'+
		'<th>Language</th>'+
		'<th>Source</th>'+
		'<th>Version</th>'+
		'<th>Revision</th>'+
		'<th>URI</th>'+	
		'<th>Test Tool</th>'+	
		'<th></th>'+			
	'</tr>';
	return tr;
}

// ----- Deletion Activity Items Started -----

function deleteTestScript(scriptId){
	var fd = new FormData();
	fd.append("scriptId", scriptId);
	
	openLoaderIcon();
	$.ajax({
		url : 'testcase.script.delete',
		data : fd,
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
				callAlert(data.Message);

			}else if(data.Result == "OK"){
				var url='get.testcase.script.list?productId='+productId+'&jtStartIndex=0&jtPageSize=10000';		
				listTestCaseScriptOfSelectedProduct(url, "180px", "parentTable", "", productId);
				
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