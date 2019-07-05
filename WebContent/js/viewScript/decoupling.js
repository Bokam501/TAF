function decoupling(){	
	setProductNode();

	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#Decoupling').children().show();
		var url = 'administration.product.decouplingcategory.list?productMasterId='+productId;
		listDecouplingCategoriesOfSelectedProduct(url, "parentTable", "", "");
	}	
}

function listDecouplingCategoriesOfSelectedProduct(url, tableValue, row, tr){
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
			productDecoupling_Container(data);			
		}else{
			
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

var productDecouplingOptionsArr=[];
var productDecouplingResultArr=[];
var productDecouplingOptionsItemCounter=0;
var optionsType_productDecouplingString="productDecouplingString";

var clearTimeoutDTProductDecoupling='';
var productDecoupling_oTable='';
var productDecoupling_editor='';
function reInitializeDTProductDecoupling(){
	clearTimeoutDTProductDecoupling = setTimeout(function(){				
		productDecoupling_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTProductDecoupling);
	},200);
}

function productDecouplingResultsEditor(){	
    
	productDecoupling_editor = new $.fn.dataTable.Editor( {
			"table": "#productDecoupling_dataTable",
    		ajax: 'administration.product.decouplingcategory.add',
    		ajaxUrl: 'administration.product.decouplingcategory.update',
    		idSrc:  "decouplingCategoryId",
    		i18n: {
    	        create: {
    	            title:  "Decoupling Category",
    	            submit: "Create",
    	        }
    	    },
    		fields: [{								
				label:"productMasterId",
				name:"productMasterId",					
				type: 'hidden',				
				"def": productId
			},{								
				label:"decouplingCategoryId",
				name:"decouplingCategoryId",		
				type: 'hidden',					
			},{
                label:"DC Name",
				name:"decouplingCategoryName",									
            },{
                label:"Display Name",
				name:"displayName",									
            },{
                label:"Description",
				name:"description",									
            },{
                label: "Parent Category *",
                name: "parentCategoryId", 
				options: productDecouplingResultArr[0],
                "type"  : "select",	
            },{								
				label:"userTypeId",
				name:"userTypeId",		
				type: 'hidden',					
			},{
                label: "Type",
                name: "userTypeLabel", 
				options: productDecouplingResultArr[1],
                "type"  : "select",	
            },{								
				label:"status",
				name:"status",		
				type: 'hidden',	
				"def":1	
			},
        ]
    	});	
}

function productDecoupling_Container(data){
	productDecouplingOptionsItemCounter=0;
	productDecouplingResultArr=[];			
	productDecouplingOptionsArr = [{id:"parentCategoryId", type:optionsType_productDecouplingString, url:'common.list.parentdecouplingcategory.list?productID='+productId+'&decouplingCategoryID=0'},
		{id:"userTypeLabel", type:optionsType_productDecouplingString, url:'common.list.usertypes.list'},		
	];
		
	returnOptionsItemForProductDecoupling(productDecouplingOptionsArr[0].url, data);	
}

// ----- Return options ----

function returnOptionsItemForProductDecoupling(url, data){
	$.ajax( {
 	   "type": "POST",
        "url":  url,
        "dataType": "json",
         success: function (json) {
			 if(json == null || json.Result == "ERROR" || json.Result == "Error"){
				if(json.Message !=null) {
					//callAlert(json.Message);
				}
				json.Options=[];
				productDecouplingResultArr.push(json.Options);         	
				returnOptionsItemForProductDecoupling(url, data);         	
			 
			 }else{ 				
				for(var i=0;i<json.Options.length;i++){
					if(json.Options[i].DisplayText == null)
						json.Options[i].DisplayText = "--";	
					
					json.Options[i].label=json.Options[i].DisplayText;
					json.Options[i].value=json.Options[i].Value;
				}
			   productDecouplingResultArr.push(json.Options);
			   
			   if(productDecouplingOptionsItemCounter<productDecouplingOptionsArr.length-1){			   
				   returnOptionsItemForProductDecoupling(productDecouplingOptionsArr[productDecouplingResultArr.length].url, data);     		  
			   }else{					
					productDecoupling_ResultContainer(data);
			   }
			   productDecouplingOptionsItemCounter++;         
			 }
		 },
         error: function (data) {
        	 productDecouplingOptionsItemCounter++;        	 
         },
         complete: function(data){
         	//console.log('Completed');         	
         },
   	});	
}


function productDecoupling_ResultContainer(data){
	try{
		if ($("#jTableContainerdecoupling").children().length>0) {
			$("#jTableContainerdecoupling").children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(8);  // total coulmn count				  
	  var childDivString = '<table id="productDecoupling_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#jTableContainerdecoupling").append(childDivString); 						  
	  
	  $("#productDecoupling_dataTable thead").html('');
	  $("#productDecoupling_dataTable thead").append(productDecouplingHeader());
	  
	  $("#productDecoupling_dataTable tfoot tr").html('');     			  
	  $("#productDecoupling_dataTable tfoot tr").append(emptytr);
	
	// --- editor for the activity Change Request started -----
	productDecouplingResultsEditor();
	
	if(data.length>0){
		for(var i=0;i<data.length;i++){
			data[i]["createdDate"] = convertDTDateFormatAdd(data[i]["createdDate"]);
			data[i]["modifiedDate"] = convertDTDateFormatAdd(data[i]["modifiedDate"]);		  	  
		}
	}
			
	productDecoupling_oTable = $("#productDecoupling_dataTable").dataTable( {
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
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
     		  $("#productDecoupling_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
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
			   reInitializeDTProductDecoupling();			   
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: productDecoupling_editor },					
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "Test Data",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "Test Data",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "Test Data",
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
							showDecouplingCTestCaseTree();
						}
					}, 	
				], 	         
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "decouplingCategoryId",className: 'disableEditInline', sWidth: '15%' },
		   { mData: "decouplingCategoryName",className: 'editable', sWidth: '15%' },		
           { mData: "displayName",className: 'disableEditInline', sWidth: '15%' },
           { mData: "description",className: 'editable', sWidth: '25%' },           
		   { mData: "parentCategoryId", className: 'editable', sWidth: '10%',
				mRender: function (data, type, full) {
					 if(full.parentCategoryName == null)
						 full.parentCategoryName='--';
					
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(productDecoupling_editor, 'parentCategoryId', full.parentCategoryId);						
					 }else if(type == "display"){
						data = full.parentCategoryName;
					 }	           	 
					 return data;
				 },
	        },          					          
		   { mData: "userTypeLabel", className: 'editable', sWidth: '10%',editField: "userTypeId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(productDecoupling_editor, 'userTypeId', full.userTypeId);						
					 }else if(type == "display"){
						data = full.userTypeLabel;
					 }	           	 
					 return data;
				 },
	        },
		   { mData: "status",
			  sWidth: '3%',
              mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editor-decoupling">';
                    }
                    return data;
                },
                className: "dt-body-center"
            },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+					
     	       		'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
 	       					'<img src="css/images/list_metro.png" class="productDecouplingImg1" title="Mapped TestCase List"/></button>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
							'<img src="css/images/mapping.png" class="productDecouplingImg2" title="TestCase Mapping" data-toggle="modal" /></button>'+		
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						'<i class="fa fa-comments productDecouplingImg3" title="Comments"></i></button>'+	
     	       		'</div>');	      		
           		 return img;
            	},
            },			
       ],rowCallback: function ( row, data ) {
            $('input.editor-decoupling', row).prop( 'checked', data.status == 1 );      	
        }, 			   
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready  
		 
		 $("#productDecoupling_dataTable_length").css('margin-top','8px');
		 $("#productDecoupling_dataTable_length").css('padding-left','35px');
		 		 
		 // Activate an inline edit on click of a table cell
        $('#productDecoupling_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	productDecoupling_editor.inline( this, {
        		submitOnBlur: true
			}); 
		});
		 		
		productDecoupling_oTable.DataTable().columns().every( function () {
			var that = this;
			$('input', this.footer() ).on( 'keyup change', function () {
				if ( that.search() !== this.value ) {
					that
						.search( this.value, true, false )
						.draw();
				}
			});
		});	

		 // ----- Mapped TestCase List -----
		 
		 $('#productDecoupling_dataTable tbody').on('click', 'td button .productDecouplingImg1', function () {
			var tr = $(this).closest('tr');
			var row = productDecoupling_oTable.DataTable().row(tr);
							
			//$("#productFeatureChild1_Container .modal-title").text("Mapped Testcases List");
			
			var containerID = "productDecouplingChild1";
			//var url = 'product.feature.testcase.mappedlist?productFeatureId='+row.data().productFeatureId+'&jtStartIndex=0&jtPageSize=10000';	
			var url = 'decoupling.test.case.list?decouplingCategoryId='+row.data().decouplingCategoryId+'&jtStartIndex=0&jtPageSize=10000';	
			listFeaturesOfSelectedProduct(url, "240px", "childTable1", containerID, row.data().productMasterId);
			$("#productFeaturePopupChild1").html('');
			$("#productFeaturePopupChild1").html('<div id="'+containerID+'"></div>');
			$("#productFeatureChild1_Container").modal();
		 });
		 
		 // ----- Mapping -----
		 
		 $('#productDecoupling_dataTable tbody').on('click', 'td button .productDecouplingImg2', function () {
			var tr = $(this).closest('tr');
			var row = productDecoupling_oTable.DataTable().row(tr);
						
			testCasetodecouplingMapping(row.data().decouplingCategoryId, row.data().decouplingCategoryName); 				    
		 });
		 
		 // ----- Comments  -----
		 
		 $('#productDecoupling_dataTable tbody').on('click', 'td button .productDecouplingImg3', function () {
			var tr = $(this).closest('tr');
			var row = productDecoupling_oTable.DataTable().row(tr);
						
			var entityTypeIdComments = 24;
			var entityNameComments = "DecouplingCategory";
			listComments(entityTypeIdComments, entityNameComments, row.data().decouplingCategoryId, row.data().decouplingCategoryName, "DCComments");						
		 });
		 
	});
}

function productDecouplingHeader(){		
	var tr = '<tr>'+			
		'<th>ID</th>'+
		'<th>Name</th>'+
		'<th>Display Name</th>'+
		'<th>Description</th>'+
		'<th>Parent Category</th>'+
		'<th>Type</th>'+
		'<th>Status</th>'+					
		'<th></th>'+	
	'</tr>';
	return tr;
}

/*
function listDecouplingCategoriesOfSelectedProduct(){
	try{
		if ($('#jTableContainerdecoupling').length>0) {
			 $('#jTableContainerdecoupling').jtable('destroy'); 
		}
		}catch(e){}
		//init jTable
		 $('#jTableContainerdecoupling').jtable({
	         title: 'Decoupling Category',
	         selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: 10, //Set page size (default: 10)
	         editinline:{enable:true},	
	         //toolbarsearch:true,
	         //sorting: true, //Enable sorting	         
	         toolbar : {
				items : [ 
					{
						icon : '',
						cssClass: 'fa fa-tree',
						click : function() {
							showDecouplingCTestCaseTree();
						}
					}
				]
			 },
	         actions: {
	             listAction: urlToGetDecouplingCategoriesOfSpecifiedProductId,
	             createAction: 'administration.product.decouplingcategory.add',
	             editinlineAction: 'administration.product.decouplingcategory.update',
	             //deleteAction: 'administration.product.delete'
	         },
	         fields: {
		productMasterId: { 
   			 type: 'hidden',  
   			list:false,
   			defaultValue: productId
   			}, 
	   	decouplingCategoryId: { 
			key: true,
			type: 'hidden',
			create: false, 
			edit: false, 
			list: false,
			display: function (data) { return data.record.decouplingCategoryId;},
			}, 
	   	decouplingCategoryName: { 
     	  	title: 'DC Name',
     	  	inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
     	  	width: "20%",
     	  	list:true
  			 	},
		displayName: { 
	     	title: 'Display Name',
	     	width: "20%",
	     	list:true,
	     	edit: false,
	     	create:false
	  	 	},
	 	description: { 
    		title: 'Description' ,
  		  	width: "35%",  
  	  		list:true
   	   		},	       
		parentCategoryId:{
           	title: 'Parent Category',
           	list:true,
           	edit: true,
           	create : true,
           	inputTitle: 'Parent Category <font color="#efd125" size="4px">*</font>',
               dependsOn: 'decouplingCategoryId',
               options: function (data) 
               {
               	if(data.source =='list')
               	{	
               		return 'common.list.parentdecouplingcategory.list?productID='+productId+'&decouplingCategoryID=' + data.dependedValues.decouplingCategoryId;
               	} else if(data.source =='create'){
               		data.clearCache();
               		var xx = data.dependedValues.decouplingCategoryId;
               		if(xx != undefined){
               			return 'common.list.parentdecouplingcategory.list?productID='+productId+'&decouplingCategoryID=' + data.dependedValues.decouplingCategoryId;
               		}else{
               			return 'common.list.parentdecouplingcategory.list?productID='+productId+'&decouplingCategoryID=' +0;
               		}
               	}
               	return 'common.list.parentdecouplingcategory.list?productID='+productId+'&decouplingCategoryID=' + 0;
               }
           },
		userTypeId : {
			title : 'User Type Id',
			type : 'hidden'
			},
		userTypeLabel : {
			title : 'Type',
			width : "20%",
			edit : true,	
			create : true,
			options : 'common.list.usertypes.list'
			},
		status: {	      
	       	title: 'Status' ,
        	width: "35%",  
        	list:true,
        	edit:true,
        	create:false,
        	type : 'checkbox',
        	values: {'0' : 'No','1' : 'Yes'},
	   		defaultValue: '1'
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
					
					// ----- Closing child table on the same icon click -----
					closeChildTableFlag = closeJtableTableChildContainer($(this), $("#jTableContainerdecoupling"));
					if(closeChildTableFlag){
						return;
					}
					
			        	$('#jTableContainerdecoupling').jtable('openChildTable', 
			        	$img.closest('tr'), 
			        	{ 
			        	title: 'Mapped Test Cases',
			        	paging: true, //Enable paging
			            pageSize: 10, //Set page size (default: 10)
			            selecting: true, //Enable selecting 
			            editinline:{enable:false},					        	  	
			        	actions: { 
			        		 //listAction: urlToGetTestCasesOfSpecifiedProductId,	      
			        		 listAction: 'decoupling.test.case.list?decouplingCategoryId='+childData.record.decouplingCategoryId,
			        		// createAction: 'product.testcase.add',
			        		// editinlineAction: 'product.testcase.update',
			        	     recordUpdated:function(event, data){
			        	        	$('#jTableContainerdecoupling').find('.jtable-main-container').jtable('reload');
			        	        },
			        	     recordAdded: function (event, data){
			        	         	$('#jTableContainerdecoupling').find('.jtable-main-container').jtable('reload');
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
					        			testCaseCode: { 
							         		title: 'Test Case Code',  
							         		inputTitle: 'Test Case Code <font color="#efd125" size="4px">*</font>',
							         		width: "10%",                        
							                 create: true
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
					             //    options: 'common.list.testsuite.byproductId?productId='+ productId,              
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
	          	testCasetoDecouplingMapping:{
		        	title : 'Mapping',
		        	list : true,
		        	create : false,
		        	edit : false,
		        	width: "10%",
		        	display:function (data) { 
		           		//Create an image that will be used to open child table 
	           			var $img = $('<img src="css/images/mapping.png" title="TestCase Mapping" data-toggle="modal" data-target="#div_PopupTestCaseToDecoupling" />'); 
	           			//Open child table when user clicks the image 
	           			$img.click(function () {
	           				testCasetodecouplingMapping(data.record.decouplingCategoryId, data.record.decouplingCategoryName);
	           				//testcaseMapping(data.record.testSuiteId);
	           		  });
	           			return $img;
		        	}
		        }, 
				commentsDecouplingCat:{
					title : '',
					list : true,
					create : false,
					edit : false,
					width: "5%",
					display:function (data) { 
					//Create an image for test script popup 
					var $img = $('<i class="fa fa-comments" title="Comments"></i>');
					$img.click(function () {
						var entityTypeIdComments = 24;
						var entityNameComments = "DecouplingCategory";
						listComments(entityTypeIdComments, entityNameComments, data.record.decouplingCategoryId, data.record.decouplingCategoryName, "DCComments");
					});
					return $img;
					}		
				},
	        },	        
	      //Moved Formcreate validation to Form Submitting
	      //Validate form when it is being submitted
          formSubmitting: function (event, data) {
        	  data.form.find('input[name="decouplingCategoryName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[minSize[3]], custom[maxSize[25]]');	           
              data.form.validationEngine();
             return data.form.validationEngine('validate');
         }, 
          //Dispose validation logic when form is closed
          formClosed: function (event, data) {
             data.form.validationEngine('hide');
             data.form.validationEngine('detach');
         }
	     });
		 
   $('#jTableContainerdecoupling').jtable('load');		 
}
*/
//Decoupling Scope Ends 