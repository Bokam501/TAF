var productFeautureOptionsArr=[];
var productFeautureResultArr=[];
var productFeautureOptionsItemCounter=0;

var optionsType_ProductFeature="ProductFeature";
var feauture_editor='';

var featureTCFlag=false;

function features(){
	$("#uploadFileOfProductFeatures").css("display","none");
	setProductNode();
	
	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#Features').children().show();		
		urlToGetFeaturesOfSpecifiedProductId = 'administration.product.feature.by.versionId.or.buildId?productMasterId='+productId+'&versionId='+productVersionId+'&buildId='+buildId+'&featureStatus=1&jtStartIndex=0&jtPageSize=10000';		
		listFeaturesOfSelectedProduct(urlToGetFeaturesOfSpecifiedProductId, "180px", "parentTable", "", productId);
	}	
}

$(document).on('change','#featureStatus_ul', function() {
	productFeatureStautsHandler();
});

function productFeatureStautsHandler(){
	var featureStatus = $("#featureStatus_ul").find('option:selected').val();
    urlToGetFeaturesOfSpecifiedProductId = 'administration.product.feature.by.versionId.or.buildId?productMasterId='+productId+'&versionId='+productVersionId+'&buildId='+buildId+'&featureStatus='+featureStatus+'&jtStartIndex=0&jtPageSize=10000';	
    listFeaturesOfSelectedProduct(urlToGetFeaturesOfSpecifiedProductId, "180px", "parentTable", "", productId);
}

function listFeaturesOfSelectedProduct(url, ScrollValue, tableValue, row, tr){
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
			
			if(tableValue == "parentTable" || tableValue == "childTable1"){										
				productFeautureResults_Container(data, ScrollValue, row, tr, tableValue);
				
			}else{
				console.log("no child");
			}			
			
			/*if(tableValue == "parentTable"){										
				productFeautureResults_Container(data, ScrollValue, row, tr);
				
			}else if(tableValue == "childTable1"){						
				productFeautureChild1Results_Container(data, ScrollValue, row, tr);			
				
			}else{
				console.log("no child");
			}*/
			
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

function productFeautureResults_Container(data, scrollYValue, activityWorkPackageId, productId,tableValue){	
	var atype="default";
	productFeautureOptionsItemCounter=0;
	productFeautureResultArr=[];			
	productFeautureOptionsArr = [{id:"productFeatureId", type:optionsType_ProductFeature, url:'common.list.parentfeature.list?productID='+productId+'&productFeatureId=' + 0+'&actionType='+atype},
	{id:"executionPriorityId", type:optionsType_ProductFeature, url:'administration.feature.execution.priority.option.list'},
	{id:"executionTypeId", type:optionsType_ProductFeature, url:'common.list.executiontypemaster.byentityid?entitymasterid=3'},
	{id:"assigneeId", type:optionsType_ProductFeature, url:'common.user.list.by.resourcepool.id.productId?productId='+productId},
	{id:"reviewerId", type:optionsType_ProductFeature, url:'common.user.list.by.resourcepool.id.productId?productId='+productId},
	{id:"workflowId", type:optionsType_ProductFeature, url:'workflow.master.mapped.to.entity.list.options?productId='+productId+'&entityTypeId=23&entityId=0'}];
	returnOptionsItemForProductFeature(productFeautureOptionsArr[0].url, scrollYValue, data, activityWorkPackageId, productId, tableValue);
}
// ----- Return options ----

function returnOptionsItemForProductFeature(url, scrollYValue, data, row, tr, tableValue){
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
         	productFeautureResultArr.push(json.Options);         	
         	returnOptionsItemForProductFeature(url, scrollYValue, data, row, tr, tableValue); 
         	
         }else{        	        	     	        	
			for(var i=0;i<json.Options.length;i++){
				if(json.Options[i].DisplayText == null)
					json.Options[i].DisplayText = "--";	
				
				json.Options[i].label=json.Options[i].DisplayText;
				json.Options[i].value=json.Options[i].Value;
			}
     	   productFeautureResultArr.push(json.Options);
		   
     	   if(productFeautureOptionsItemCounter<productFeautureOptionsArr.length-1){			   
			   returnOptionsItemForProductFeature(productFeautureOptionsArr[productFeautureResultArr.length].url, scrollYValue, data, row, tr, tableValue);     		  
     	   }else{   
     		  if(tableValue == "parentTable"){										
     			  productFeauture_Container(data, scrollYValue);
     		  }else if(tableValue == "childTable1"){						
     			  productFeautureChild1Results_Container(data, scrollYValue, row, tr);
     		  }
     		  //productFeauture_Container(data, scrollYValue); 
		   }
     	   productFeautureOptionsItemCounter++;     	   
         }
         },
         error: function (data) {
        	 productFeautureOptionsItemCounter++;
        	 
         },
         complete: function(data){
         	//console.log('Completed');
         	
         },	            
   	});	
}

function productFeatureResultsEditor(row){	
    
	feauture_editor = new $.fn.dataTable.Editor( {
			"table": "#featuresParent_dataTable",
    		ajax: "administration.product.feature.add",
    		ajaxUrl: "administration.product.feature.update",
    		idSrc:  "productFeatureId",
    		i18n: {
    	        create: {
    	            title:  "Create a new Feature",
    	            submit: "Create",
    	        }
    	    },
    		fields: [{								
				label:"productId",
				name:"productId",					
				type: 'hidden',				
				def: productId
			},{
                label: "Feature Code  ",
                name: "productFeatureCode",                
            },{
                label: "Name  *",
                name: "productFeatureName",                
            },{
                label: "Feature Description",
                name: "productFeatureDescription",                
            },{
                label: "Priority",
                name: "executionPriorityId",
                 options: productFeautureResultArr[1],
                "type"  : "select",
            },{
                label: "Parent",
                name: "parentFeatureId",
                 options: productFeautureResultArr[0],
                "type"  : "select",
            },{								
				label:"ABBR",
				name:"abbr",					
			},{
                label: "Current Status:",
                name: "statusDisplayName",	
				"type": "hidden",
            },{
                label: "Assignee",
                name: "assigneeId",
                type : "select",
                options: productFeautureResultArr[3],
            },{
                label: "Reviewer",
                name: "reviewerId",
                type : "select",
                options: productFeautureResultArr[3],
            },
			{
                label: "Workflow Template:",
                name: "workflowId",
                type : "select",
                options: productFeautureResultArr[5],
            },{								
				label:"displayName",
				name:"displayName",					
				type: 'hidden',				
			},{								
				label:"productFeatureId",
				name:"productFeatureId",					
				type: 'hidden',				
			},{								
				label:"parentFeatureStatus",
				name:"parentFeatureStatus",					
				type: 'hidden',				
			},{								
				label:"leftIndex",
				name:"leftIndex",					
				type: 'hidden',				
			},{								
				label:"rightIndex",
				name:"rightIndex",					
				type: 'hidden',				
			},
        ]
    	});	
}

var clearTimeoutDTProductFeature='';
function reInitializeDTProductFeature(){
	clearTimeoutDTProductFeature = setTimeout(function(){				
		productFeature_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTProductFeature);
	},200);
}

function productFeauture_Container(data, row, tr){
	try{
		if ($("#jTableContainerfeatures").children().length>0) {
			$("#jTableContainerfeatures").children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(15);  // total coulmn count				  
	  var childDivString = '<table id="featuresParent_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#jTableContainerfeatures").append(childDivString); 						  
	  
	  $("#featuresParent_dataTable thead").html('');
	  $("#featuresParent_dataTable thead").append(productFeatureHeader());
	  
	  $("#featuresParent_dataTable tfoot tr").html('');     			  
	  $("#featuresParent_dataTable tfoot tr").append(emptytr);
	
	// --- editor for the activity Change Request started -----
	productFeatureResultsEditor(row);
			
	productFeature_oTable = $("#featuresParent_dataTable").dataTable( {
		 	dom: "Bfrtilp",
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY": "100%",
	       "bSort": false,
	       "iDisplayLength":100,
	       "bScrollCollapse": true,
	       /*fixedColumns:   {
	           leftColumns: 1,
	       }, */
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex = [10]; // search column TextBox Invisible Column position
     		  $("#featuresParent_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
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
			   reInitializeDTProductFeature();			   
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: feauture_editor },	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "ProductFeature",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "ProductFeature",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "ProductFeature",
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
						text: '<i class="fa fa-upload showHandCursor" title="Product Features"></i>',
						action: function ( e, dt, node, config ) {					
							triggerProductFeatureUpload();
						}
					},
					{					
						text: '<i class="fa fa-download showHandCursor" title="Download Template - Product Features"></i>',
						action: function ( e, dt, node, config ) {					
							downloadTemplateProductFeature();
						}
					},
					{
						text: '<span>Mapping Feature To Build<i id="mappingFeatureToBuild" title="Mapping Feature To Build"></i></span>',
						action: function ( e, dt, node, config ) {					
							mappingFeatureToBuidHandler(e);
						}
					},
					{
						text: '<i class="fa fa-indent" title="'+document.getElementById("hdnProductName").value+' : Entity Traceability"></i>',
						action: function ( e, dt, node, config ) {	
							$('#treePaginationContainer').css('z-index','10050');
							featureTCFlag = true;
							showFeatureTestCaseTree(1);
						}
					},					
				], 
					columnDefs: [ {targets: [10,12,13] ,visible: false}],
        aaData:data,		    				 
	    aoColumns: [
	                
			{ mData: "productFeatureId", sWidth: '3%', "render": function (data,type,full) {	        
					var featureId = full.productFeatureId;
				var featureName = full.productFeatureName;	            		
				var result = featureId+'~@'+featureName;	
				return ('<a id="'+result+'"  onclick="featureOverAllView(event)">'+featureId+'</a>');			            	
			},
			}, 
           /*{ mData: "productFeatureId",className: 'disableEditInline', sWidth: '3%' },	*/	
           { mData: "productFeatureCode",className: 'editable', sWidth: '3%' },
           { mData: "productFeatureName",className: 'editable', sWidth: '30%' },           
		   /*{ mData: "displayName",className: 'disableEditInline', sWidth: '10%' },*/           
		   { mData: "productFeatureDescription",className: 'editable', sWidth: '25%' },
		   { mData: "executionPriorityName", className: 'editable', sWidth: '20%', editField: "executionPriorityId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(feauture_editor, 'executionPriorityId', full.executionPriorityId);
					 }
					 else if(type == "display"){
						data = full.executionPriorityName;
					 }	           	 
					 return data;
				 },
			},
			{ mData: "parentFeatureName", className: 'editable', sWidth: '20%', editField: "parentFeatureId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(feauture_editor, 'parentFeatureId', full.parentFeatureId);
					 }
					 else if(type == "display"){
						data = full.parentFeatureName;
					 }	           	 
					 return data;
				 },
			},					   	   
			{ mData: "abbr",className: 'editable', sWidth: '25%' },
			
			{ mData: "statusDisplayName", sWidth: '7%', "render": function (tcData,type,full) {	        
			 	var entityInstanceId = full.productFeatureId;
				var entityInstanceName = full.productFeatureName;	
				var modifiedById = full.assigneeId;
				var currentStatusId = full.statusId;
				var currentStatusDisplayName="";
				if(full.statusDisplayName != null) {
					currentStatusDisplayName =full.statusDisplayName;	
				}
				 
				var entityId =0;	
				var secondaryStatusId = 0;
				var visibleEventComment=true;
				var prodcutId=full.productId;					
				return ('<a onclick="addFeatureComments('+prodcutId+','+entityInstanceId+',\''+entityInstanceName+'\','+modifiedById+','+currentStatusId+',\''+currentStatusDisplayName+'\','+entityId+','+secondaryStatusId+','+visibleEventComment+')">'+currentStatusDisplayName+'</a>');		        
		      },
        },
			{ mData: "assigneeName", className: 'editable', sWidth: '10%', editField: "assigneeId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(feauture_editor, 'assigneeId', full.assigneeId);
		           	}else if(type == "display"){
						data = full.assigneeName;
					 }	           	 
					 return data;
				 },
			},	            
			{ mData: "reviewerName", className: 'editable', sWidth: '10%', editField: "reviewerId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(feauture_editor, 'reviewerId', full.reviewerId);
		           	 }
					 else if(type == "display"){
						data = full.reviewerName;
					 }	           	 
					 return data;
				 },
			},
			{ mData: "workflowId",className: 'disableEditInline', sWidth: '3%' },
           { mData: "parentFeatureStatus", className: 'disableEditInline', sWidth: '5%',			
			  mRender: function (data, type, full) {
				  if ( type === 'display' ) {
						return '<input type="checkbox" class="productFeatureEditor-active">';
				  }
					return data;
				},
				className: "dt-body-center"	            
           },
           { mData: "leftIndex",className: 'disableEditInline', sWidth: '5%' },
           { mData: "rightIndex",className: 'disableEditInline', sWidth: '5%' },
			{ mData: null,				 
            	bSortable: false,
				sWidth: '10%',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
           			'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
	       					'<img src="css/images/workflow.png" class="productFeatureImg5" title="Configure Workflow" /></button>'+	
   					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
 	       					'<img src="css/images/list_metro.png" class="productFeatureImg1" title="Mapped TestCase List" /></button>'+
     	       		'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
							'<img src="css/images/mapping.png" class="productFeatureImg2" title="TestCase Mapping" data-toggle="modal" /></button>'+
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="fa fa-search-plus productFeatureImg3" title="Audit History"></i></button>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						'<i class="fa fa-comments productFeatureImg4" title="Comments"></i></button>'+	
     	       		'</div>');	      		
           		 return img;
            	}
            },
       ], 
		rowCallback: function ( row, data ) {
	           $('input.productFeatureEditor-active', row).prop( 'checked', data.parentFeatureStatus == 1 );
	    },	   
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		 
		 //$("#activityTabLoaderIcon").hide();
		 
		 $("#featuresParent_dataTable_length").css('margin-top','8px');
		 $("#featuresParent_dataTable_length").css('padding-left','35px');
		 
		 $('#featuresParent_dataTable').on( 'change', 'input.productFeatureEditor-active', function () {
			 feauture_editor
					.edit( $(this).closest('tr'), false )
					.set( 'parentFeatureStatus', $(this).prop( 'checked' ) ? 1 : 0 )
					.submit();
			});
		 	 
		 // Activate an inline edit on click of a table cell
        $('#featuresParent_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	feauture_editor.inline( this, {
        		submitOnBlur: true
			}); 
		});
		
        feauture_editor.on( 'preSubmit', function ( e, o, action ) {
	        if ( action !== 'remove' ) {
	        	var validationArr = ['productFeatureCode','productFeatureName'];
	        	var testCaseName;
	        	for(var i=0;i<validationArr.length;i++){
		            testCaseName = this.field(validationArr[i]);
		            if ( ! testCaseName.isMultiValue() ) {
		                if ( testCaseName.val() ) {
	                	}else{
		                	/*if(validationArr[i] == "productFeatureCode")
		                		testCaseName.error( 'Please enter Feature code' );*/
		                	if(validationArr[i] == "productFeatureName")
		                		testCaseName.error( 'Please enter Feature name' );
	                	}
		            }
	        	}

	            // If any error was reported, cancel the submission so it can be corrected
	            if ( this.inError() ) {
	                return false;
	            }
	        }
	    } );
        
		productFeature_oTable.DataTable().columns().every( function () {
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
		$('#featuresParent_dataTable tbody').on('click', 'td button .productFeatureImg1', function () {
			var tr = $(this).closest('tr');
			var row = productFeature_oTable.DataTable().row(tr);
			
			var containerID = "productFeatureChild1";
			urlToGetFeaturesOfSpecifiedProductId = 'product.feature.testcase.mappedlist?productFeatureId='+row.data().productFeatureId+'&jtStartIndex=0&jtPageSize=10000';		
			listFeaturesOfSelectedProduct(urlToGetFeaturesOfSpecifiedProductId, "240px", "childTable1", containerID, row.data().productId);
			$("#productFeaturePopupChild1").html('');
			$("#productFeaturePopupChild1").html('<div id="'+containerID+'"></div>');
			$("#productFeatureChild1_Container").modal();		
		});			
		// -----
		 
		 $('#featuresParent_dataTable tbody').on('click', 'td button .productFeatureImg2', function () {
				var tr = $(this).closest('tr');
			    var row = productFeature_oTable.DataTable().row(tr);
			    				
				var testCasesProductFeatureID = row.data().productFeatureId;
				var productId =	row.data().productId;					
				// ----- DragDrop Testing started----		
				//var productId = document.getElementById("hdnProductId").value;												
				var leftUrl="test.case.unmappedto.feature.count?productId="+productId+"&productFeatureId="+testCasesProductFeatureID;							
				var rightUrl = "";							
				var leftDefaultUrl="features.unmappedtestcase.byProduct.list?productId="+productId+"&productFeatureId="+testCasesProductFeatureID+"&jtStartIndex=0&jtPageSize=50";							
				var rightDefaultUrl="feature.test.case.list?productFeatureId="+testCasesProductFeatureID+"&productId="+productId;
				var leftDragUrl = "test.case.feature.mapping?productFeatureId="+testCasesProductFeatureID;			
				var rightDragtUrl = "test.case.feature.mapping?productFeatureId="+testCasesProductFeatureID;						
				var leftPaginationUrl = "features.unmappedtestcase.byProduct.list?productId="+productId+"&productFeatureId="+testCasesProductFeatureID;
				
				var rightPaginationUrl="";							
				
				jsonFeatureTabObj={"Title":"Map Testcases to Feature :- "+row.data().productFeatureName,
						"leftDragItemsHeaderName":"Available Testcases",
						"rightDragItemsHeaderName":"Mapped Testcases",
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
						"componentUsageTitle":"FeatureTab-TCtoFeature",
						};
				
				DragDropListItems.init(jsonFeatureTabObj);							
				// DragDrop Testing ended----
			    
		 });
		 // ----- ended -----
		 
		 // ----- Audit History -----		 
		 $('#featuresParent_dataTable tbody').on('click', 'td button .productFeatureImg3', function () {			 
			 var tr = $(this).closest('tr');
			 var row = productFeature_oTable.DataTable().row(tr);
				
			 listGenericAuditHistory(row.data().productFeatureId,"ProductFeature","productFeatureAudit");			 
		 });		 
		 // ----- ended -----
		 
		// ----- Comments -----		 
		 $('#featuresParent_dataTable tbody').on('click', 'td button .productFeatureImg4', function () {			 
			 var tr = $(this).closest('tr');
			 var row = productFeature_oTable.DataTable().row(tr);
				
			var entityTypeIdComments = 23;
			var entityNameComments = "ProductFeature";
			listComments(entityTypeIdComments, entityNameComments, row.data().productFeatureId, row.data().productFeatureName, "featureComments");			 
		 });		 
		 // ----- ended -----	
		 
		 // ----- WorkFlow -----
		 
		 $('#featuresParent_dataTable tbody').on('click', 'td button .productFeatureImg5', function () {	
	    	var tr = $(this).closest('tr');
	    	var row =  productFeature_oTable.DataTable().row(tr);
	    	
	    	workflowId = 0;
			entityTypeId = 23;
			if(row.data().productId != null){
				productId=row.data().productId;
			} 
			if(row.data().workflowId != null) {
				workflowId=row.data().workflowId;
			}
			statusPolicies(productId, workflowId, entityTypeId, 0, row.data().productFeatureId, row.data().productFeatureName, "Feature", row.data().statusId);	    	
		 });	
		 // ----- ended -----
		 
	});
	// ------	
}


function addFeatureComments(prodId,entityInstanceId,entityInstanceName,modifiedById,currentStatusId,currentStatusDisplayName,entityId,secondaryStatusId,visibleEventComment){	
	
	var entityTypeId = 23;//Feature type
	var actionTypeValue = 0;
	$("#addCommentsMainDiv").modal();
	$("#addCommentsDateTimePickerBox").show();
	isSave= true;
	if(!visibleEventComment) {
		$('#addComments').hide();//Display only histroy of task Effort
		$("#viewComments .slimScrollDiv").css("height", "450px");
		$("#viewComments .scroller").css("height", "450px");
	}else {
		$('#addComments').show();
		$("#viewComments .slimScrollDiv").css("height", "200px");
		$("#viewComments .scroller").css("height", "200px");
	}
	var jsonObj = {
			Title : "Feature Workflow History: ["+entityInstanceId+"] "+entityInstanceName,	
			entityTypeName : 'Feature',		
			entityTypeId : entityTypeId,
			entityInstanceName : entityInstanceName,
			entityInstanceId : entityInstanceId,
			modifiedByUrl : 'common.user.list.by.resourcepool.id',		
			modifiedById : modifiedById,
			raisedDate : new Date(),
			comments : "",
			productId : prodId,
			primaryStatusUrl : 'workflow.status.master.option.list?productId='+prodId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&currentStatusId='+currentStatusId,
			secondaryStatusUrl : 'workflow.entity.secondary.status.master.option.list?productId='+prodId+'&entityTypeId='+entityTypeId+'&statusId='+currentStatusId,
			currentStatusId : currentStatusId,
			currentStatusName : currentStatusDisplayName,
			secondaryStatusId : secondaryStatusId,
			effortListUrl : 'workflow.event.tracker.list?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
			actionTypeValue : actionTypeValue,
			commentsName : commentsReviewActivity,
			"componentUsageTitle":"Feature",
			urlToSave : 'workflow.event.tracker.add?productId='+prodId+'&entityId='+entityId+'&entityTypeId='+entityTypeId+'&primaryStatusId=[primaryStatusId]&secondaryStatusId=[secondaryStatusId]&effort=[effort]&comments=[comments]&sourceStatusId='+currentStatusId+'&approveAllEntityInstanceIds=[approveAllEntityIds]&entityInstanceId='+entityInstanceId+'&attachmentIds=[attachmentIds]&actionDate=[actionDate]&actualSize=[actualSize]',				
	};
	AddComments.init(jsonObj);
}


function productFeatureHeader(){		
	var tr = '<tr>'+			
		'<th>ID</th>'+
		'<th>Code</th>'+
		'<th>Name</th>'+
		/*'<th>Display Name</th>'+*/
		'<th>Description</th>'+
		'<th>Priority</th>'+		
		'<th>Parent</th>'+
		'<th>ABBR</th>'+
		'<th>Workflow Status</th>'+
		'<th>Assignee</th>'+
		'<th>Reviewer</th>'+
		'<th>WorkflowId</th>'+
		'<th>Status</th>'+
		'<th></th>'+
		'<th></th>'+
		'<th></th>'+
	'</tr>';
	return tr;
}

function mappingFeatureToBuidHandler(event){
	$("#featureToBuildMappingContainer").modal();
	mappingFeatureToBuild();
}

// ----- Ended -----

// ----- ChildTable -----

function productFeautureChild1Results_Container(data, scrollYValue, activityWorkPackageId, productId){	
	productFeautureChild1_Container(data, activityWorkPackageId, productId);
}


var clearTimeoutDTProductFeatureChild1='';
var productFeaturechild1_oTable=''
function reInitializeDTProductFeatureChild1(){
	clearTimeoutDTProductFeatureChild1 = setTimeout(function(){				
		productFeaturechild1_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTProductFeatureChild1);
	},200);
}

function productFeatureChildHeader(){		
	var tr = '<tr>'+			
		'<th>ID</th>'+
		'<th>Code</th>'+
		'<th>Name</th>'+
		'<th>Description</th>'+
		'<th>Script Package Name</th>'+
		'<th>Script File Name</th>'+
		'<th>Execution Type</th>'+
		'<th>Priority</th>'+
		'<th>Type</th>'+		
		'<th>Features</th>'+		
		'<th>Execution History</th>'+		
	'</tr>';
	return tr;
}

var feautureTestcase_editor='';
function productFeatureRelatedTestcaseEditor(row){	
    
	feautureTestcase_editor = new $.fn.dataTable.Editor( {
			"table": '#featuresChild1_dataTable_'+row,
    		ajax: "",
    		ajaxUrl: "",
    		idSrc:  "testCaseId",
    		i18n: {
    	        create: {
    	            title:  "",
    	            submit: "Create",
    	        }
    	    },
    		fields: [{								
                label: "executionTypeId",
                name: "executionTypeId",
                 options: productFeautureResultArr[2],
                "type"  : "select",
            } ]
    	});	
}

function productFeautureChild1_Container(data, row, tr){
	
	try{
		if ($("#"+row).children().length>0) {
			$("#"+row).children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(11);  // total coulmn count				  
	  var childDivString = '<table id="featuresChild1_dataTable_'+row+'" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#"+row).append(childDivString); 						  
	  
	  $("#featuresChild1_dataTable_"+row+" thead").html('');
	  $("#featuresChild1_dataTable_"+row+" thead").append(productFeatureChildHeader());
	  
	  $("#featuresChild1_dataTable_"+row+" tfoot tr").html('');     			  
	  $("#featuresChild1_dataTable_"+row+" tfoot tr").append(emptytr);
	  
	  productFeatureRelatedTestcaseEditor(row);
				
	  productFeaturechild1_oTable='';
	  productFeaturechild1_oTable= $("#featuresChild1_dataTable_"+row).dataTable( {
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
			   
			  var searchcolumnVisibleIndex = [10]; // search column TextBox Invisible Column position
	     	  var headerItems = $('#featuresChild1_dataTable_'+row+'_wrapper tfoot tr th');
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
			   
			   reInitializeDTProductFeatureChild1();			   
		   },  
		   select: true,
		   buttons: [	             		  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "ProductFeature",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "ProductFeature",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "ProductFeature",
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
           { mData: "testCaseId",className: 'disableEditInline', sWidth: '5%' },		
           { mData: "testCaseCode",className: 'disableEditInline', sWidth: '5%' },
           { mData: "testCaseName",className: 'disableEditInline', sWidth: '15%' },           
		   { mData: "testCaseDescription",className: 'disableEditInline', sWidth: '20%' },           
		   { mData: "testCaseScriptQualifiedName",className: 'disableEditInline', sWidth: '20%' },           
		   { mData: "testCaseScriptFileName",className: 'disableEditInline', sWidth: '10%' },           			
           /*{ mData: "executionTypeId", className: 'disableEditInline', sWidth: '5%'},
		   { mData: "testcasePriorityId", className: 'disableEditInline', sWidth: '5%'},
		   { mData: "testcaseTypeId", className: 'disableEditInline', sWidth: '5%'},
		   { mData: "productFeatureId", className: 'disableEditInline', sWidth: '5%'},*/
			{ mData: "executionTypeId", className: 'disableEditInline', sWidth: '20%', editField: "executionTypeId",
				mRender: function (data, type, full) {
					data = optionsValueHandler(feautureTestcase_editor, 'executionTypeId', full.executionTypeId);
					return data;
				},
			},
		{ mData: "testCasePriority", className: 'disableEditInline', sWidth: '20%', editField: "testcasePriorityId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(feauture_editor, 'testcasePriorityId', full.testcasePriorityId);
					 }
					 else if(type == "display"){
						data = full.testCasePriority;
					 }	           	 
					 return data;
				 },
			},
			{ mData: "testCaseType", className: 'disableEditInline', sWidth: '20%', editField: "testcaseTypeId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(feauture_editor, 'testcaseTypeId', full.testcaseTypeId);
					 }
					 else if(type == "display"){
						data = full.testCaseType;
					 }	           	 
					 return data;
				 },
			},						
			{ mData: "productFeatureId", className: 'disableEditInline', sWidth: '5%', 
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(feauture_editor, 'productFeatureId', full.productFeatureId);
					 }
					 else if(type == "display"){
						data = full.productFeatureId;
					 }	           	 
					 return data;
				 },
	        },
		   { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px; padding-right: 2px;">'+
 	       					'<i class="fa fa-history productFeatureChildImg1" title="Execution Summary and History"></i></button>');
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
		 $("#featuresChild1_dataTable_"+row+"_length").css('margin-top','8px');
		 $("#featuresChild1_dataTable_"+row+"_length").css('padding-left','35px');	
		 
		 $('#featuresChild1_dataTable_'+row).DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });

		// ----- Execution History -----
		 
		 $('#featuresChild1_dataTable_'+row+' tbody').on('click', 'td button .productFeatureChildImg1', function () {			 
			 var tr = $(this).closest('tr');
			 var row = productFeaturechild1_oTable.DataTable().row(tr);
				
			var dataLevel = "productLevel";
			listTCExecutionSummaryHistoryView(row.data().testCaseId, row.data().testCaseName, dataLevel);
			 
		 });
		 
		 // ----- ended -----	
		
	});
	// ------	
}