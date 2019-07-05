var TestCaseGroup = function() {
  
   var initialise = function(jsonObj){
	   testCaseScenarioResults_Container(jsonObj);
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

var testCaseScenarioOptionsArr=[];
var testCaseScenarioTabResultArr=[];
var optionsType_TestCaseScenario="TestCaseScenarioTab";
var optionsType_TestCaseScenarioParent="TestCaseScenarioParent";
var optionsType_TestCaseType="TestCaseType";
var testCaseGroupJsonObj={};
function testCaseScenarioResults_Container(jsonObj){
	testCaseGroupJsonObj = jsonObj;
	testCaseScenarioTabResultArr=[];
	testCaseScenarioOptionsArr = [{id:"parentEntityGroupId", type:optionsType_TestCaseScenario, url:'common.list.parentTestCaseEntitytGroup.list?productId='+productId+'&testCaseEntityGroupId=0&actionType=create'},
	                              {id:"testCaseTypeId", type:optionsType_TestCaseType, url:'common.list.testcasetype'}];
	assignTestCaseGroupDataTableValues(jsonObj, testCaseScenarioOptionsArr[0].url, "parentEntity");	
}

function assignTestCaseGroupDataTableValues(jsonObj, url, type){	
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			
			if(type == "parentEntity"){
				if(data.Result=="ERROR"){
					data = [];						
				}else{
					for(var i=0;i<data.Options.length;i++){
						data.Options[i].label=data.Options[i].DisplayText;
						data.Options[i].value=data.Options[i].Value;
					}
					data = data.Options;
				}				
				testCaseScenarioTabResultArr[0] = data;
				assignTestCaseGroupDataTableValues(jsonObj, testCaseScenarioOptionsArr[1].url, "testCaseType");
			
			}else if(type == "testCaseType"){
				if(data.Result=="ERROR"){
					data = [];						
				}else{
					for(var i=0;i<data.Options.length;i++){
						data.Options[i].label=data.Options[i].DisplayText;
						data.Options[i].value=data.Options[i].Value;
					}
					data = data.Options;
				}			
				testCaseScenarioTabResultArr[1] = data;
				assignTestCaseGroupDataTableValues(jsonObj,jsonObj.url,"");				

			}else{
				jsonObj.data = data.Records;
				testCaseDT_Container(jsonObj);			
			}		
		  },
		  error : function(data) {
			  console.log("error");
		 },
		 complete: function(data){
			 console.log("complete");
		 }
	});	
}

// -- Audit History for the Product Management Plan ----
function testCaseGroupTable(){
	var childDivString = '<table id="testCaseGroup_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>Name</th>'+
			'<th>Description</th>'+
			'<th>Type</th>'+
			'<th>Parent Scenario</th>'+			
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
		'</tr>'+
	'</tfoot>'+
	'</table>';		
	
	return childDivString;	
}

var testCaseGroupDT_oTable='';
var testCaseGroupDT_editor='';

function createEditorForTestCaseGroup(){	
	testCaseGroupDT_editor = new $.fn.dataTable.Editor( {
			    "table": "#testCaseGroup_dataTable",
				ajax: "testCase.entity.group.add",
				ajaxUrl: "testCase.entity.group.update",
				"idSrc":  "testCaseEntityGroupId",
				i18n: {
	    	        create: {
	    	            title:  'Add Test Case Scenario',
	    	            submit: "Create",
	    	        }
	    	    },
				fields: [ {
					label: 'testCaseEntityGroupId',
		            name: "testCaseEntityGroupId",
					"type":"hidden",					
		        },
				{
		        label: 'productId',
	            name: "productId",
				"type":"hidden",
				"def":productId,
	        	},{
		            label: "Name",
		            name: "testCaseEntityGroupName",
		        },		        
		        {
		            label: "Description",
		            name: "description",
		        },{
		            label: "Type",
		            name: "testCaseTypeId",
		            options: testCaseScenarioTabResultArr[1],
		            "type" : "select",
		        },{
		            label: "Parent Scenario",
		            name: "parentEntityGroupId",
					options: testCaseScenarioTabResultArr[0],
		            "type"  : "select",	
		        },       
		    ]
			});
			
		// ----- focus area -----	
			
		$( 'input', testCaseGroupDT_editor.node()).on( 'focus', function () {
			this.select();
		});
}

var clearTimeoutDTTestCaseGroup='';
function reInitializeDTTestCaseGroup(){
	clearTimeoutDTTestCaseGroup = setTimeout(function(){				
		testCaseGroupDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTTestCaseGroup);
	},200);
	
}

function testCaseDT_Container(jsonObj){	
	try{
		if ($("#div_dataTableTestCaseGroup").children().length>0) {
			$("#div_dataTableTestCaseGroup").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = testCaseGroupTable(); 			 
	$("#div_dataTableTestCaseGroup").append(childDivString);	
	createEditorForTestCaseGroup();
	
	testCaseGroupDT_oTable = $("#testCaseGroup_dataTable").dataTable( {
		 	"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       "fnInitComplete": function(data) {	    	        	  
	    	   var searchcolumnVisibleIndex = [5]; // search column TextBox Invisible Column position
	    	   $('#testCaseGroup_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
				reInitializeDTTestCaseGroup();
		   },		   
		   buttons: [
		             { extend: "create", editor: testCaseGroupDT_editor },
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'TestCase Scenario',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'TestCase Scenario',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'TestCase Scenario',
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
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "testCaseEntityGroupId", className: 'disableEditInline', sWidth: '10%' },		
           { mData: "testCaseEntityGroupName", className: 'editable', sWidth: '20%' },		
           { mData: "description", className: 'editable', sWidth: '25%' },		
           { mData: "testCaseTypeName", className: 'editable', sWidth: '30%', editField: "testCaseTypeId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(testCaseGroupDT_editor, 'testCaseTypeId', full.testCaseTypeId);
					 }
					 else if(type == "display"){
						data = full.testCaseTypeName;
					 }	           	 
					 return data;
			   },		
      	   
         },
           { mData: "parentEntityGroupName", className: 'editable', sWidth: '30%', editField: "parentEntityGroupId",
 				mRender: function (data, type, full) {
 					 if (full.action == "create" || full.action == "edit"){
 						data = optionsValueHandler(testCaseGroupDT_editor, 'parentEntityGroupId', full.parentEntityGroupId);
 					 }
 					 else if(type == "display"){
 						data = full.parentEntityGroupName;
 					 }	           	 
 					 return data;
 			   },		
       	   
          },
          { mData: null,				 
          	bSortable: false,
          	mRender: function(data, type, full) {			
         		 var img = ('<div style="display: flex;">'+
         				'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
						'<img src="css/images/mapping.png" class="TestScenarioImg1" title="Test Case Mapping" data-toggle="modal" /></button>'+	
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
		$("#testCaseGroup_dataTable_length").css('margin-top','8px');
		$("#testCaseGroup_dataTable_length").css('padding-left','35px');
		
		$( 'input', testCaseGroupDT_editor.node()).on( 'focus', function () {
			this.select();
		});
		
		$('#testCaseGroup_dataTable').on( 'click', 'tbody td.editable', function (e) {
			testCaseGroupDT_editor.inline( this, {
        			submitOnBlur: true
			}); 
		});

		$('#testCaseGroup_dataTable').DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });		
		
		testCaseGroupDT_editor.on('onPostCreate', function(e, data, action) {			
			testCaseScenarioResults_Container(testCaseGroupJsonObj);
		});			
		
	});
	
	$("#testCaseGroup_dataTable tbody").on('click', 'td button .TestScenarioImg1', function () {
		var tr = $(this).closest('tr');
		var row = testCaseGroupDT_oTable.DataTable().row(tr);
		
		var testCaseEntityGroupId = row.data().testCaseEntityGroupId;
		var productId = row.data().productId;
				
		var leftUrl="unmapped.testCases.count.for.testScenario?productId="+productId+"&testScenarioId="+testCaseEntityGroupId+"&filter=1";							
		var rightUrl = "";							
		var leftDefaultUrl="unmapped.testCases.list.for.testScenario?productId="+productId+"&testScenarioId="+testCaseEntityGroupId+"&filter=1&jtStartIndex=0&jtPageSize=50";							
		var rightDefaultUrl="testCases.mapped.to.testScenario.list?testScenarioId="+testCaseEntityGroupId+"&filter=1";
		var leftDragUrl = "testCase.map.or.unmap.to.testScenario?testScenarioId="+testCaseEntityGroupId;
		var rightDragtUrl = "testCase.map.or.unmap.to.testScenario?testScenarioId="+testCaseEntityGroupId;						
		var leftPaginationUrl = "unmapped.testCases.list.for.testScenario?productId="+productId+"&testScenarioId="+testCaseEntityGroupId+"&filter=1";
		var rightPaginationUrl="";	
		
		jsonTestScenarioObj={"Title":"Map Test Cases to TestScenario :- "+row.data().testCaseEntityGroupName,
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
				"componentUsageTitle":"TestCase-TestScenario",
				};
		DragDropListItems.init(jsonTestScenarioObj);
	});
}
// ----- ended -----

function testCaseGroupHandler(event){
	$("#div_testCaseGroupContainer").modal();	
	var url='testcase.entity.group.list?productId='+productId+'&jtStartIndex=0&jtPageSize=1000';
	var jsonObj={"Title":"Test Case Scenario :",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":1000,
			"componentUsageTitle":"testCaseGroupinTestData",
	};
	TestCaseGroup.init(jsonObj);	
}
