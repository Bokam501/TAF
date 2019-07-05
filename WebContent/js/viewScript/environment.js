function environments(){
	if(pageType!="HOMEPAGE")setProductNode();	
	$('#Environment').children().show();	
	$("#envRadioGrp>.btn-group>label:first").removeClass("active").addClass("active");
	$("#envRadioGrp>.btn-group>label:last").removeClass("active");
	showEnvironmentCombinationTable(1);
	
	var date = new Date();
    var timestamp = date.getTime();
	 
	 //var envstatus = $("#selectEnvironmentstatus_ul").find('option:selected').val();	
	// urlToGetEnvironmentCombinationByProduct = 'administration.product.environment.list?productMasterId='+productId+"&envstatus="+envstatus+"&timeStamp="+timestamp;
	// listSelectedEnvCombinationByProduct();
}

var currTable = 1;
function showEnvironmentCombinationTable(obj){
	if(obj == 1){
		envCombination();
		$(".jTbEnvCmb").show();
		$(".jTbEnv").hide();
		$("#filter1").show();
		$("#filterForEnvironments").hide();		
		currTable = 1;
	}else{
		$(".jTbEnv").show();
		$(".jTbEnvCmb").hide();
		$("#filter1").hide();
		$("#filterForEnvironments").show();
		
		var envstatus = $("#selectEnvironmentstatus_ul").find('option:selected').val();	
		var url = 'administration.product.environment.list?productMasterId='+productId+"&envstatus="+envstatus+"&timeStamp="+timestamp+"&jtStartIndex=0&jtPageSize=10000";
		listSelectedEnvCombinationByProduct(url, "parentTable");		
		currTable = 2;
	}
}

$(document).on('change','#envCombinationstatus_ul', function() {
	envCombination();
});

function envCombination(){
	var envComstatus = $("#envCombinationstatus_ul").find('option:selected').val();
    urlToGetEnvironmentCombinationByProduct = "environment.combination.list.of.selectedProduct?productVersionId=-1&productId="+productId+"&workpackageId=-1&testRunPlanId=-1&envComstatus="+envComstatus+"&jtStartIndex=0&jtPageSize=10000";
    listEnvironmentCombinationOfSelProd(urlToGetEnvironmentCombinationByProduct, "parentTable"); 
}
 
$(document).on('change','#selectEnvironmentstatus_ul', function() {
	var envstatus = $("#selectEnvironmentstatus_ul").find('option:selected').val();				
	 urlToGetEnvironmentOfSpecifiedProductId = 'administration.product.environment.list?productMasterId='+productId+"&envstatus="+envstatus+"&timeStamp="+timestamp+"&jtStartIndex=0&jtPageSize=10000";
	 listSelectedEnvCombinationByProduct(urlToGetEnvironmentOfSpecifiedProductId, "parentTable");
});
 
var selectedEnvironmentOptionsItemCounter=0;
var selectedEnvironmentResultArr=[];			
var	selectedEnvironmentOptionsArr=[];
var optionsType_selectedEnvironment="selectedEnvironment";	
 function listSelectedEnvCombinationByProduct(url, tableValue){
	openLoaderIcon();
	
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();		
			
			if(data == null || data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			
			if(tableValue == "parentTable"){										
				selectedEnvironmentResults_Container(data);
				
			}else{
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

function selectedEnvironmentResults_Container(data){		
	selectedEnvironmentOptionsItemCounter=0;
	selectedEnvironmentResultArr=[];			
	selectedEnvironmentFlag=false;
	var environmentGroupID = 0;	
	if(data.length == 0 || typeof environmentGroupID == 'undefined' || environmentGroupID == null){
		environmentGroupID = 0;
	}else{
		environmentGroupID = data[0].environmentGroupId;
	}
	
	selectedEnvironmentOptionsArr = [{id:"environmentGroupId", type:optionsType_selectedEnvironment, url:'administration.environment.group.list'},
		{id:"environmentCategoryId", type:optionsType_selectedEnvironment, url:'common.environment.group.category.list'},
		{id:"isStandAloneEnvironment", type:optionsType_selectedEnvironment, url:'common.standaloane.environment.options'},	
	];
	
	returnOptionsItemForselectedEnvironment(selectedEnvironmentOptionsArr[0].url, data);
}
// ----- Return options ----

function returnOptionsItemForselectedEnvironment(url, data){
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
	         	selectedEnvironmentResultArr.push(json.Options);         	
	         	returnOptionsItemForselectedEnvironment(url, data); 
	         	
	         }else{        	        	     	        	
				for(var i=0;i<json.Options.length;i++){
					if(json.Options[i].DisplayText == null)
						json.Options[i].DisplayText = "--";	
					
					json.Options[i].label=json.Options[i].DisplayText;
					json.Options[i].value=json.Options[i].Value;
				}
	     	   selectedEnvironmentResultArr.push(json.Options);
	     	   closeLoaderIcon();
			   
	     	   if(selectedEnvironmentOptionsItemCounter<selectedEnvironmentOptionsArr.length-1){			   
				   returnOptionsItemForselectedEnvironment(selectedEnvironmentOptionsArr[selectedEnvironmentResultArr.length].url, data);     		  
	     	   }else{   			  	      			       			   				  				  
	     		  selectedEnvironment_Container(data); 
			   }
	     	   selectedEnvironmentOptionsItemCounter++;     	   
	         }
		 
         },error: function (data) {
        	 selectedEnvironmentOptionsItemCounter++;
        	 closeLoaderIcon(); 
         },complete: function(data){
         	closeLoaderIcon(); 
         },	            
   	});	
}

var clearTimeoutDTSelectedEnvironment='';
var selectedEnvironment_oTable='';
var selectedEnvironment_editor=''
var selectedEnvironmentFlag=false;
function reInitializeDTSelectedEnvironment(){
	clearTimeoutDTSelectedEnvironment = setTimeout(function(){				
		selectedEnvironment_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTSelectedEnvironment);
	},200);
}

function selectedEnvironmentHeader(){		
	var tr = '<tr>'+			
		'<th>Name</th>'+
		'<th>Description</th>'+
		'<th>Environment Group Name</th>'+
		'<th>Environment Category Name</th>'+
		'<th>Standalone Environment</th>'+
		'<th>Status</th>'+
		'<th></th>'+
	'</tr>';
	return tr;
}

function selectedEnvironmentEditor(){	
    
	selectedEnvironment_editor = new $.fn.dataTable.Editor( {
			"table": "#selectedEnvironment_dataTable",
    		ajax: "administration.product.environment.add",
    		ajaxUrl: "administration.product.environment.update",
    		idSrc:  "environmentId",
    		i18n: {
    	        create: {
    	            title:  "Create a new Environment",
    	            submit: "Create",
    	        }
    	    },
    		fields: [{								
				label:"productMasterId",
				name:"productMasterId",					
				type: 'hidden',	
                "def":productId, 				
			},{								
				label:"environmentId",
				name:"environmentId",					
				type: 'hidden',	
						
			},{								
				label:"environmentGroupName",
				name:"environmentGroupName",					
				type: 'hidden',	
						
			},{								
				label:"environmentCategoryDisplayName",
				name:"environmentCategoryDisplayName",					
				type: 'hidden',	
						
			},{
                label: "Status ",
                name: "status",
				type: 'hidden',	                
				"def":1
            },{								
				label:"createdDate",
				name:"createdDate",					
				type: 'hidden',	
						
			},{								
				label:"modifiedDate",
				name:"modifiedDate",					
				type: 'hidden',	
						
			},{
                label: "Name*",
                name: "environmentName",                
            },{
                label: "Description",
                name: "description",                
            },{
                label: "Environment Group Name*",
                name: "environmentGroupId",
				options: selectedEnvironmentResultArr[0],
                "type"  : "select",	
            },{								
				label:"Environment Category Name*",
				name:"environmentCategoryId",
				options: selectedEnvironmentResultArr[1],
                "type"  : "select",	
			},{
                label: "Standalone Environment*",
                name: "isStandAloneEnvironment",
                 options: selectedEnvironmentResultArr[2],
                "type"  : "select",
            }
        ]
    	});	
}

function selectedEnvironment_Container(data){
	try{
		if ($("#jTableContainerProductEnvironment").children().length>0) {
			$("#jTableContainerProductEnvironment").children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(7);  // total coulmn count		  
	  var childDivString = '<table id="selectedEnvironment_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#jTableContainerProductEnvironment").append(childDivString); 						  
	  
	  $("#selectedEnvironment_dataTable thead").html('');
	  $("#selectedEnvironment_dataTable thead").append(selectedEnvironmentHeader());
	  
	  $("#selectedEnvironment_dataTable tfoot tr").html('');     			  
	  $("#selectedEnvironment_dataTable tfoot tr").append(emptytr);
	
	// --- editor for the activity Change Request started -----
	selectedEnvironmentEditor();
	
	if(data.length>0){
		for(var i=0;i<data.length;i++){
			if(data[i]["createdDate"] == null || data[i]["modifiedDate"] == null){				
				data[i]["createdDate"] = null;
				data[i]["modifiedDate"] = null;
				
			}else{
				data[i]["createdDate"] = convertDTDateFormatAdd(data[i]["createdDate"]);
				data[i]["modifiedDate"] = convertDTDateFormatAdd(data[i]["modifiedDate"]);		
			}			
		}
	}
			
	selectedEnvironment_oTable = $("#selectedEnvironment_dataTable").dataTable( {
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
			  var searchcolumnVisibleIndex = [5,6]; // search column TextBox Invisible Column position
     		  $("#selectedEnvironment_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
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
			   reInitializeDTSelectedEnvironment();			   
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: selectedEnvironment_editor },	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "Selected Environment",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "Selected Environment",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "Selected Environment",
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
           { mData: "environmentName",className: 'disableEditInline', sWidth: '3%' },
           { mData: "description",className: 'editable', sWidth: '20%' },           
		   { mData: "environmentGroupId",className: 'disableEditInline', sWidth: '25%',
        	   mRender: function (data, type, full) {
        		   data = optionsValueHandler(selectedEnvironment_editor, 'environmentGroupId', full.environmentGroupId); 		           	 
        		   return data;
        	   },
	       },           
		   { mData: "environmentCategoryId",className: 'disableEditInline', sWidth: '5%',
        	   mRender: function (data, type, full) {
       			   data = optionsValueHandler(selectedEnvironment_editor, 'environmentCategoryId', full.environmentCategoryId);
        		   return data;
        	   },
	       },
		   { mData: "isStandAloneEnvironment",className: 'disableEditInline', sWidth: '20%' },
		   { mData: "status",
			sWidth: '3%',
              mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editor-active-selectedEnvironment">';
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
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="fa fa-search-plus selectedEnvironmentImg1" title="Audit History"></i></button>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						'<i class="fa fa-comments selectedEnvironmentImg2" title="Comments"></i></button>'+						
     	       		'</div>');	      		
           		 return img;
            	}
            },
       ], 
		rowCallback: function ( row, data ) {
	           $('input.editor-active-selectedEnvironment', row).prop( 'checked', data.status == 1 );
	    },	   
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		 	 
		 $("#selectedEnvironment_dataTable_length").css('margin-top','8px');
		 $("#selectedEnvironment_dataTable_length").css('padding-left','35px');
		 
		  $('#selectedEnvironment_dataTable').on( 'change', 'input.editor-active-selectedEnvironment', function () {
			 selectedEnvironment_editor
					.edit( $(this).closest('tr'), false )
					.set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
					.submit();
			});
		 
		 // Activate an inline edit on click of a table cell
        $('#selectedEnvironment_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	selectedEnvironment_editor.inline( this, {
        		submitOnBlur: true
			}); 
		});
		
        selectedEnvironment_editor.on( 'preSubmit', function ( e, o, action ) {
	        if ( action !== 'remove' ) {
	        	var validationArr = ['environmentName'];
	        	var testCaseName;
	        	for(var i=0;i<validationArr.length;i++){
		            testCaseName = this.field(validationArr[i]);
		            if ( ! testCaseName.isMultiValue() ) {
		                if ( testCaseName.val() ) {
	                	}else{
		                	if(validationArr[i] == "environmentName")
		                		testCaseName.error( 'Please enter environment name' );
	                	}
		            }
	        	}

	            // If any error was reported, cancel the submission so it can be corrected
	            if ( this.inError() ) {
	                return false;
	            }
	        }
	    } ); 
        
        selectedEnvironment_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );
			
		// ----- dependent Values -----
        
        selectedEnvironment_editor.dependent('environmentGroupId',function ( val, data, callback ) {        	
			var entityId = 0;
			if(val != undefined){  
				
				if(!selectedEnvironmentFlag){
					entityId = data.values['environmentGroupId'];
					selectedEnvironmentFlag=true;
				}else{
					entityId = val;
				}
				
				var url = 'administration.environment.group.category.list?environmentGroupId='+entityId;
				$.ajax( {
					url: url,
					type: "POST",
					dataType: 'json',
					success: function ( json ) {			    	        	
						
						for(var i=0;i<json.Options.length;i++){
							json.Options[i].label=json.Options[i].DisplayText;
							json.Options[i].value=json.Options[i].Value;
						}
						
						json.url = url;
						
						if(selectedEnvironment_editor.s.action == "create"){
							selectedEnvironment_editor.set('environmentCategoryId',json.Options);
							selectedEnvironment_editor.field('environmentCategoryId').update(json.Options);
						}
					}
				} );
			}			
        });
	        
	        // ----- ended -----	
			
		// -----		
		$('#selectedEnvironment_dataTable tbody').on('click', 'td button .selectedEnvironmentImg1', function () {
			var tr = $(this).closest('tr');
			var row = selectedEnvironment_oTable.DataTable().row(tr);

			listGenericAuditHistory(row.data().environmentId,"Environment","environmentAudit");				
			
		});			
		// ----- Comments -----		
		 
		 $('#selectedEnvironment_dataTable tbody').on('click', 'td button .selectedEnvironmentImg2', function () {
			var tr = $(this).closest('tr');
			var row = selectedEnvironment_oTable.DataTable().row(tr);			    				
			
			var entityTypeIdComments = 36;
			var entityNameComments = "Environment";
			listComments(entityTypeIdComments, entityNameComments, row.data().environmentId, row.data().environmentName, "environmentComments");			
			    
		 }); 
	});
}
 
 // -----

function listEnvironmentCombinationOfSelProd(url, tableValue){
	openLoaderIcon();
	
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();		
			
			if(data == null || data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			
			if(tableValue == "parentTable"){										
				environmentResults_Container(data); 
				
			}else if(tableValue == "childTable1"){						
				environmentChild1Results_Container(data);			
				
			}else{
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

function environmentChild1Header(){		
	var tr = '<table id="enviornmentChild_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+			
			'<th>Environment Name</th>'+				
			'<th>Description</th>'+	
			'<th>Environment Category Name</th>'+				
			'<th>Environment Group Name</th>'+					
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
	return tr;
}

var clearTimeoutDTEnviornmentChild='';
var enviornmentChild1_oTable='';
function reInitializeDTEnviornmentChild(){
	clearTimeoutDTnviornmentChild = setTimeout(function(){				
		enviornmentChild1_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTnviornmentChild);
	},200);
}

function environmentChild1Results_Container(data){
	try{
		if ($("#enviornmentChild_Content").children().length>0) {
			$("#enviornmentChild_Content").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = environmentChild1Header(); 			 
	$("#enviornmentChild_Content").append(childDivString);
	
	enviornmentChild1_oTable = $('#enviornmentChild_dataTable').dataTable( {
		dom: "Bfrtilp",
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"scrollY":"100%",
		"sScrollX": "90%",
        "sScrollXInner": "100%",        
        "bScrollCollapse": true,
        "bSort": false,
        "fnInitComplete": function(data) { 

		  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
		  var headerItems = $('#enviornmentChild_dataTable_wrapper tfoot tr th');
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
		   
		   reInitializeDTEnviornmentChild();
 	   }, 
		buttons: [		         
		         {
		          extend: "collection",	 
		          text: 'Export',
	              buttons: [
		          {
                    	extend: 'excel',
                    	title: 'Environment List',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'csv',
                    	title: 'Environment List',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'pdf',
                    	title: 'Environment List',
                    	exportOptions: {
                            columns: ':visible',	                            
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
            { mData: "environmentName", className: 'disableEditInline', sWidth: '20%'},
			{ mData: "description", className: 'disableEditInline', sWidth: '25%'},			
			{ mData: "environmentCategoryName", className: 'disableEditInline', sWidth: '20%'},
			{ mData: "environmentGroupName", className: 'disableEditInline', sWidth: '20%'},			
        ],       
        "oLanguage": {
        	"sSearch": "",
        	"sSearchPlaceholder": "Search all columns"
        },
	});	 
	
	$(function(){ // this will be called when the DOM is ready 
		 
		 $("#enviornmentChild_dataTable_length").css('margin-top','8px');
		 $("#enviornmentChild_dataTable_length").css('padding-left','35px');
		 	 
		 
		 $('#enviornmentChild_dataTable').DataTable().columns().every( function () {
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
}

function environmentHeader(){		
	var tr = '<tr>'+			
		'<th>Environment Combination</th>'+				
		'<th>Status</th>'+		
		'<th></th>'+		
	'</tr>';
	return tr;
}

var environment_oTable='';
var clearTimeoutDTEnvironment='';
var environment_editor='';

function reInitializeDTEnvironment(){
	clearTimeoutDTEnvironment = setTimeout(function(){				
		environment_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTEnvironment);
	},200);
}

function environmentResultsEditor(){	    
	environment_editor = new $.fn.dataTable.Editor( {
			"table": "#environmentCmbnation_dataTable",
    		ajax: "administration.environment.combination.add",
    		ajaxUrl: "administration.environment.combination.update",
    		idSrc:  "envionmentCombinationId",
    		i18n: {
    	        create: {
    	            title:  "Create a new Feature",
    	            submit: "Create",
    	        }
    	    },
    		fields: [{								
				label:"envionmentCombinationId",
				name:"envionmentCombinationId",					
				type: 'hidden',	                				
			},{								
				label:"environmentCombinationName",
				name:"environmentCombinationName",					
				type: 'hidden',							
			},{								
				label:"envionmentCombinationStatus",
				name:"envionmentCombinationStatus",					
				type: 'hidden',	
				"def":1,	
			},
			
        ]
    	});	
}

function environmentResults_Container(data){
	try{
		if ($("#jTableContainerenvironmentCombination").children().length>0) {
			$("#jTableContainerenvironmentCombination").children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(3);  // total coulmn count				  
	  var childDivString = '<table id="environmentCmbnation_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#jTableContainerenvironmentCombination").append(childDivString); 						  
	  
	  $("#environmentCmbnation_dataTable thead").html('');
	  $("#environmentCmbnation_dataTable thead").append(environmentHeader());
	  
	  $("#environmentCmbnation_dataTable tfoot tr").html('');     			  
	  $("#environmentCmbnation_dataTable tfoot tr").append(emptytr);
	  
	  // --- editor for the environment started -----
		environmentResultsEditor();
		
	environment_oTable = $("#environmentCmbnation_dataTable").dataTable( {
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
				var searchcolumnVisibleIndex = [1,2]; // search column TextBox Invisible Column position
     		  $("#environmentCmbnation_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
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
				reInitializeDTEnvironment();
		   },  
		   select: true,
		   buttons: [	
					{ extend: "create", editor: environment_editor },	
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Environment',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Environment',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Environment',
		                    	exportOptions: {
		                            columns: ':visible'
		                        },
		                        orientation: 'landscape',
		                        pageSize: 'LEGAL'
		                    }	                    
		                ],	                
		            },
		            'colvis'
	         ], 	         
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "environmentCombinationName",className: 'disableEditInline', sWidth: '25%' },
		   { mData: "envionmentCombinationStatus",
			sWidth: '3%',
              mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editor-active-environment">';
                    }
                    return data;
				},
                className: "dt-body-center"
            },
			{ mData: null,			
				sWidth: '3%',			
            	bSortable: false,
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
     	       				'<img src="css/images/list_metro.png" class="environmentImg1" title="Activity Tasks" /></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },
       ],
        rowCallback: function ( row, data ) {
            $('input.editor-active-environment', row).prop( 'checked', data.envionmentCombinationStatus == 1 );
        },       
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
			 
		 $("#environmentCmbnation_dataTable_length").css('margin-top','8px');
		 $("#environmentCmbnation_dataTable_length").css('padding-left','35px');
		 
		 $("#environmentCmbnation_dataTable_wrapper").find(".buttons-create").hide();
		 
		 $('#environmentCmbnation_dataTable').on( 'change', 'input.editor-active-environment', function () {
			 environment_editor
					.edit( $(this).closest('tr'), false )
					.set( 'envionmentCombinationStatus', $(this).prop( 'checked' ) ? 1 : 0 )
					.submit();
			});
		 
		  environment_oTable.DataTable().columns().every( function () {
		        var that = this;
		        $('input', this.footer() ).on( 'keyup change', function () {
		            if ( that.search() !== this.value ) {
		                that
		                    .search( this.value, true, false )
		                    .draw();
		            }
		        });
		    });
							
		// ----- Activity Task -----
		 
		  $("#environmentCmbnation_dataTable tbody").on('click', 'td button .environmentImg1', function () {
				var tr = $(this).closest('tr');
				var row = environment_oTable.DataTable().row(tr);		
				
				var url = 'get.environment.details.by.environmentCombinationId?environmentCombinationId='+row.data().envionmentCombinationId+
				'&jtStartIndex=0&jtPageSize=10000';
				
				listEnvironmentCombinationOfSelProd(url, "childTable1"); 
				
				$("#enviornmentChild_Container .modal-header .modal-title").text("Environment List");
				$("#enviornmentChild_Container").modal();
		 });
		 
		 // ----- ended -----
		 
	});
		// ------	
}

//Environments Scope Starts
/*
function listEnvironmentCombinationOfSelProd(){
	
	try{
		if ($('#jTableContainerProductEnvironment').length>0) {
			 $('#jTableContainerProductEnvironment').jtable('destroy'); 
		}
		}catch(e){}
		$('#jTableContainerProductEnvironment').jtable({
	         
	         title: 'Environment',
	         selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: 10, //Set page size (default: 10)
	         editinline:{enable:true},
	         //toolbarsearch:true,
	          
	 	//	toolbar : {
	 	//		items : [
	 	//			
	 	//			{
	 	//				text : "Environment Combination",
	 	//				click : function() {
	 	//					listSelectedEnvCombinationByProduct();
	 	//				}
	 	//			} ,
	 	//	]
	 	//	}, 
	         actions: {
	             listAction: urlToGetEnvironmentOfSpecifiedProductId,
	             createAction: 'administration.product.environment.add',
	             editinlineAction: 'administration.product.environment.update'
	            // deleteAction: 'administration.product.environment.delete'
	         },
	         fields: {	           
	            productMasterId: { 
        			type: 'hidden',  
        			list:false,
        			defaultValue: productId
        		}, 
	        	environmentId: { 
       				key: true,
       				type: 'hidden',
       				create: false, 
       				edit: false, 
       				list: false
	        	}, 
	        	environmentName: { 
	            	title: 'Name',
	            	inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
	            	width: "20%",
	            	list:true,
	            	edit:false
	        	},
	        	description: { 
	            	title: 'Description' ,
	            	width: "35%",  
	            		list:true
	            }, 
	            environmentGroupId: { 
	            	title: 'Environment Group Name' ,
	            	width: "35%",  
	            	inputTitle: 'Environment Group Name <font color="#efd125" size="4px">*</font>',
	            	edit:false,
	            	 options:'administration.environment.group.list'
	            }, 
	            environmentGroupName: { 
	            	title: 'Environment Group Name' ,
	            	width: "35%",  
	            	list:false,
            		create:false,
            		edit:false
	            },
	            environmentCategoryId: { 
	            	title: 'Environment Category Name' ,
	            	width: "35%",  
	            	edit:false,
	            	dependsOn: 'environmentGroupId',
	            	inputClass: 'validate[required]',
	            	inputTitle: 'Environment Category Name <font color="#efd125" size="4px">*</font>',
	            	 options:function(data){
	            		 if(data.source =='list'){
		            	 	return 'administration.environment.group.category.list?environmentGroupId='+data.record.environmentGroupId;
	            		 }else if(data.source =='create'){
	            			 data.clearCache();
	                		var xx = data.dependedValues.environmentGroupId;
	                		if(xx != undefined){
	                			return 'administration.environment.group.category.list?environmentGroupId='+ data.dependedValues.environmentGroupId;
	                		}else{
	                			return 'administration.environment.group.category.list?environmentGroupId=' +0;
	                		}
	            		 }
	            	 }
	            }, 
	            environmentCategoryDisplayName: { 
	            	title: 'Environment Category Name' ,
	            	width: "35%",  
            		list:false,
            		create:false,
            		edit:false
	            }, 
	            isStandAloneEnvironment: { 
	       			title: 'Standalone Environment' ,
	       			inputTitle: 'Standalone Environment <font color="#efd125" size="4px">*</font>',
	     	  		list:true,
		     	  	create:true,
		     	  	edit:false,
		     	  	options : function(data) {
		     	  		if(data.source =='list'){
		     	  			
		     	  		}else if(data.source == 'create'){
		     	  			
		     	  		}
						return 'common.standaloane.environment.options';
					}
	    	   },
	            status: {
					title: 'Status' ,
					inputTitle: 'Status <font color="#efd125" size="4px">*</font>',
					width: "10%",  
					list:true,
					edit:true,
					create:false,
					type : 'checkbox',
					values: {'0' : 'No','1' : 'Yes'},
			    	defaultValue: '1'
			    	},
			    createdDate: {            	   
	            	title: 'CreatedDate',
	                create: false, 
    				edit: false, 
    				list: false,
	                width:"20%"      	                	
	        	},
	        	modifiedDate:{
	        		title: 'ModifiedDate',
	                create: false, 
    				edit: false, 
    				list: false,
	                width:"20%"
	        	},
				auditionHistory:{
					title : 'Audit History',
					list : true,
					create : false,
					edit : false,
					width: "10%",
					display:function (data) { 
						//Create an image for test script popup 
						var $img = $('<i class="fa fa-search-plus" title="Audit History"></i>');
						//Open Testscript popup  
						$img.click(function () {
							listGenericAuditHistory(data.record.environmentId,"Environment","environmentAudit");
						});
						return $img;
					}
				}, 
				commentsEnviron:{
					title : '',
					list : true,
					create : false,
					edit : false,
					width: "5%",
					display:function (data) { 
					//Create an image for test script popup 
						var $img = $('<i class="fa fa-comments" title="Comments"></i>');
							$img.click(function () {
								var entityTypeIdComments = 36;
								var entityNameComments = "Environment";
								listComments(entityTypeIdComments, entityNameComments, data.record.environmentId, data.record.environmentName, "environmentComments");
							});
							return $img;
					}		
				},
	       },  

	       //Moved Formcreate validation to Form Submitting
           //Validate form when it is being submitted
            formSubmitting: function (event, data) {        
           	 data.form.find('input[name="environmentName"]').addClass('validate[required]');     
           	 data.form.find('input[name="environmentGroupId"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[15]]');   
           	 data.form.find('input[name="environmentCategoryId"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]');   
           	 
           //  data.form.find('input[name="status"]').addClass('validate[required]');
             data.form.validationEngine();
             return data.form.validationEngine('validate');
           }, 
            //Dispose validation logic when form is closed
            formClosed: function (event, data) {
            	listSelectedEnvCombinationByProduct();
               data.form.validationEngine('hide');
               data.form.validationEngine('detach');
           }  
	   });
		 
	   $('#jTableContainerProductEnvironment').jtable('load');		
}
*/

/*
function listSelectedEnvCombinationByProduct(){
	//var productId = document.getElementById("hdnProductId").value;
	
	try{
		if ($('#jTableContainerenvironmentCombination').length>0) {
			 $('#jTableContainerenvironmentCombination').jtable('destroy'); 
		}
		}catch(e){}
	$('#jTableContainerenvironmentCombination').jtable({
		title: 'Environment Combination',
        selecting: true,  //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, 
        editinline:{enable:true},
        //toolbarsearch:true,
        actions: {
       	 	listAction: urlToGetEnvironmentCombinationByProduct,
       	    editinlineAction: 'administration.environment.combination.update',
        },  
        fields : {
        	envionmentCombinationId:{
	                 title: 'envionmentCombination Id',
	            	 list:false,
	                 width:"20%"
	             },
	             environmentCombinationName:{
	                 title: 'Environment Combination',
	            	 list:true,
	            	 edit:false,
	                 width:"20%"
	             },	            
	             envionmentCombinationStatus: {
						title: 'Status' ,
						inputTitle: 'Status <font color="#efd125" size="4px">*</font>',
						width: "10%",  
						list:true,
						edit:true,
						create:false,
						type : 'checkbox',
						values: {'0' : 'No','1' : 'Yes'},
				    	defaultValue: '1'
				},
				
				environment:{
                	title: '',
                	width: "5%",
                	edit: true,
                	create: false,
                	display: function (environmentData) { 
                   		var $img = $('<img src="css/images/list_metro.png" title="Environment" />'); 
                   			
                   			$img.click(function () {
                   			
                   				// ----- Closing child table on the same icon click -----
                   			    closeChildTableFlag = closeJtableTableChildContainer($(this), $('#jTableContainerenvironmentCombination'));
                   				if(closeChildTableFlag){
                   					return;
                   				} 		                   						
                   				
                   				$('#jTableContainerenvironmentCombination').jtable('openChildTable', 
                   				$img.closest('tr'), 
                   					{						                   	      	   
                   						title: 'Environment List',							                   	      
                   						editinline:{enable:false},
                   						recordsLoaded: function(event, data) {
                   		        		$(".jtable-edit-command-button").prop("disabled", true);
                   		         	},
                   					actions: { 
                   						listAction: 'get.environment.details.by.environmentCombinationId?environmentCombinationId='+environmentData.record.envionmentCombinationId,
                   						createAction : '',
    									editinlineAction : '',					    								
                   						}, 
                   						recordsLoaded: function(event, data) {
                   							$('.jtable-toolbar-item-add-record').click(function(){
	                   							$("#Edit-raisedDate").datepicker('setDate','today');
           									});				                   												                   							
                   						},
                   					fields: { 
                   						
                   						environmentId: { 					                   							
                   							type:'hidden',
                   							key: true, 
                       						create: false, 
        					                edit: true, 
                       						list: true
                       					},
                       					environmentName:{
											title : 'Environment Name',
											create: false, 
        					                edit: false, 
                       						list: true,
											
										},	
										description:{
											title : 'Description',
											create: false, 
        					                edit: false, 
                       						list: true,
											
										},	
										environmentCategoryName:{
											title : 'Environment Category Name',
											create: false, 
        					                edit: false, 
                       						list: true,
											
										},	
										environmentGroupName:{
											title : 'Environment Group Name',
											create: false, 
        					                edit: false, 
                       						list: true,
											
										},	
                   					},					                   	
                   					
                   				}, function (data) { //opened handler 
                   						data.childTable.jtable('load'); 
                   				}); 
                 	  	}); 
                   	
                   	return $img; 				                   	
                	}
                },
        	},        	

		});	
}
//Environments Scope Ends 

*/