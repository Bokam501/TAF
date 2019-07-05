var editorProduct;
var editorProductVersion='';
var editorProductBuild='';
var editorProductUserPermission='';

var productMgmProduct_oTable='';
var productMgmProductVersion_oTable='';
var productMgmProductVersionBuild_oTable='';
var productMgmProductUserPermission_oTable='';

var productManagementDTJsonData='';
var productManagementFlag=false;

var srcProductName="";

function productManageMentDTFullScreenHandler(flag){
	if(nodeType == "TestFactory"){	
		if(flag){					
			reInitializeDTProductManagementPlan();
			$("#products_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');		
		}else{				
			reInitializeDTProductManagementPlan();
			$("#products_dataTable_wrapper .dataTables_scrollBody").css('max-height','450px');
		}		
	}
}

// ----- Making ajax request for the dataTable ----- 

function assignDataTableValues(url,tableValue, row, tr){
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			
			if(tableValue == "parentTable"){
				productManagementDTJsonData = data;
				if(!productManagementFlag){
					productVersionResults_Container(data, "240px", row);
				}else{				
					reloadDataTableHandler(data, productMgmProduct_oTable);
				}
				
			}else if(tableValue == "childTable1"){
				data = convertDTDateFormat(data, ["releaseDate"]);				
				productMgmProductVersion_Container(data, row, tr);
				
			}else if(tableValue == "childTable2"){
				productUserPermissionResults_Container(data, row, tr);
				
			}else if(tableValue == "childTable3"){
				data = convertDTDateFormat(data, ["productBuildDate"]);
				productVersionBuildResults_Container(data, row, tr);
				
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

//----- product table started -----
function listProductsByTestFactory(urlToGetProductsByProductIdAndTestFactoryId, testFactoryId){
	var url = urlToGetProductsByProductIdAndTestFactoryId+'&jtStartIndex=0&jtPageSize=10000';
	assignDataTableValues(url, "parentTable", testFactoryId, "");
}

//----- product Version child table started -----
function productVersionResultHandler(productId, row, tr){
	var url ='administration.product.version.list?productId='+productId+'&jtStartIndex=0&jtPageSize=1000';
	assignDataTableValues(url, "childTable1", row, tr);	
}

//----- User Permission child table started -----
function productUserPermissionResultHandler(productId, row, tr){
	var url ='administration.product.listUserRole?productId='+productId+'&jtStartIndex=0&jtPageSize=1000';
	assignDataTableValues(url, "childTable2", row, tr);		
}

//----- product version build child table started -----
function productVersionBuildResultHandler(productVersionListId, row, tr){
	var url ='administration.product.build.list?productVersionListId='+productVersionListId+'&jtStartIndex=0&jtPageSize=1000';
	assignDataTableValues(url, "childTable3", row, tr);		
}

var productVersionSelectedTr='';
var productVersionSelectedRow='';
var productSelectedTr='';
var productSelectedRow='';

var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;
var optionsType_product="Product";
var optionsType_productBuild = "ProductBuild";
var optionsType_productUserPermission="ProductUserPermission";

function productVersionResults_Container(data, scrollYValue, testFactoryId){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [{id:"testFactoryId", type:optionsType_product, url:'administration.testFactory.list.option?testFactoryLabId=0&userId='+userId+'&testFactoryId='+testFactoryId+'&status=1&userRoleId='+userRoleId},
	               {id:"customerId", type:optionsType_product, url:'administration.customer.option.list'},
	               {id:"productTypeId", type:optionsType_product, url:'common.list.productType'},
	               {id:"modeId", type:optionsType_product, url:'administration.mode.list.option?testFactoryId='+testFactoryId}
	             ];
	returnOptionsItem(optionsArr[0].url, scrollYValue, data, "");
}

function returnOptionsItem(url, scrollYValue, data, tr){
	$.ajax( {
 	   "type": "POST",
        "url":  url,
        "dataType": "json",
         success: function (json) {
         if(json.Result == "Error" || json.Options == null){
         	callAlert(json.Message);
         	json.Options=[];
         	optionsResultArr.push(json.Options);
         	
         	if(optionsArr[0].type == optionsType_product){
  			   productVersionResultsContainer(data, scrollYValue);     			   
  		   }else if(optionsArr[0].type == optionsType_productBuild){
  			  productMgmProductVersionBuildContainer(data, scrollYValue, tr);
  		   }else if(optionsArr[0].type == optionsType_productUserPermission){
  			  productMgmProductUserPermission_Container(data, scrollYValue, tr);
  		   } 
         	
         }else{
     	   if(json.Options.length>0){     		   
			   for(var i=0;i<json.Options.length;i++){
				   json.Options[i].label=json.Options[i].DisplayText;
				   json.Options[i].value=json.Options[i].Value;
			   }			   
     	   }else{
     		  json.Options=[];
     	   }     	   
     	   optionsResultArr.push(json.Options);
     	   if(optionsItemCounter<optionsArr.length-1){
     		  if(optionsArr[0].type == optionsType_productUserPermission && optionsArr[optionsResultArr.length].id == 'roleId'){
     			 optionsArr[optionsResultArr.length].url = optionsArr[optionsResultArr.length].url+''+json.Options[0].value;
     		  }
     		  returnOptionsItem(optionsArr[optionsResultArr.length].url, scrollYValue, data, tr);     		  
     	   }else{
     		   if(optionsArr[0].type == optionsType_product){
     			   productVersionResultsContainer(data, scrollYValue);     			   
     		   }else if(optionsArr[0].type == optionsType_productBuild){
     			  productMgmProductVersionBuildContainer(data, scrollYValue, tr);
     		   }else if(optionsArr[0].type == optionsType_productUserPermission){
     			  productMgmProductUserPermission_Container(data, scrollYValue, tr);
     		   }  
     	   }
     	   optionsItemCounter++;     	   
         }
         },
         error: function (data) {
        	 optionsItemCounter++;
        	 
         },
         complete: function(data){
         	console.log('Completed');
         	
         },	            
   	});	
	
}

// ----- Product DataTable Creation Started -----

function productVersionResultsContainer(data, scrollYValue){	
    	$("#testFacMode").show();
    	var productTitle = "";
    	var lblEngagement = "";
    	var lblProduct = "";
    		productTitle = "Create a new "+prodcuctplanmgmtLabel;
    		lblEngagement = engageMentPlanmgmDTLable;
    		lblProduct= prodcuctplanmgmtLabel + ' *';
    		
    	editorProduct = new $.fn.dataTable.Editor( {
    	    "table": "#products_dataTable",
    		ajax: "administration.product.add",
    		ajaxUrl: "administration.product.update",
    		idSrc:  "productId",
    		i18n: {
    	        create: {
    	          //  title:  "Create a new Product",
    	        	title:productTitle,
    	            submit: "Create",
    	        }
    	    },
    		fields: [{
                label: "testFactoryName",
                name: "testFactoryName",
                "type": "hidden",
            },{
                label: "Product",
                name: "productId",
                "type": "hidden",
                "default": 0,
            },{
                label: lblEngagement,
                name: "testFactoryId",
                 options: optionsResultArr[0],
                "type"  : "select",
            }, {
                label: "Customer:",
                name: "customerId",
                options: optionsResultArr[1],
                "type"  : "select",
            },{
                label: "customerName",
                name: "customerName",
                "type": "hidden",
            },{
                label: "modeName",
                name: "modeName",
                "type": "hidden",
            },{
                label: "typeName",
                name: "typeName",
                "type": "hidden",
            },{
                label: lblProduct,
                name: "productName",	
            }, {
                label: "Description:",
                name: "productDescription",
                type: "textarea"
            }, {
                label: "Ref Project Code:",
                name: "projectCode",
            }, {
                label: "Ref Project:",
                name: "projectName"
            },{
                label: "Type:",
                name: "productTypeId",
                options: optionsResultArr[2],
                type: "select",			                
            },
            {
                label: "Mode:",
                name: "modeId",
                type : "select",
                options: optionsResultArr[3],
            },
            {
                label: "status",
                name: "status",
                "type": "hidden",
            },
            
            /*Testing for checkbox*/
            /*{
                label: "status",
                name: "status",
                type: "checkbox",
		        options:[
	                 { label: '', value: 1 }
                ],
            },*/
            
        ]
    	});
    	
    	// ----- Testing server side ------
    	//var productManagementUrl = urlToGetProductsByProductIdAndTestFactoryId+'&jtStartIndex=0&jtPageSize=10';
    	 
		 productMgmProduct_oTable = $('#products_dataTable').dataTable( {
			/*"processing": true,
			"serverSide": true,
			"sServerMethod": 'POST',
			"ajax": productManagementUrl,*/
			 
			dom: "Bfrtilp",
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
			"sScrollX": "90%",
	        "sScrollXInner": "100%",
	        "scrollY":"100%",
	        "bScrollCollapse": true,
	        fixedColumns: {
	            leftColumns: 1,
	            rightColumns: 1,
	        },
	        "fnInitComplete": function(data) {
	     	   var searchcolumnVisibleIndex = [0,9,10]; // search column TextBox Invisible Column position
     		   
	     	   if(!productManagementFlag){
	     		  $('#products_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
	     	   }
	     	   productManagementFlag=true;
	     	   reInitializeDTProductManagementPlan();
	 	   }, 
			buttons: [
			         { extend: "create", editor: editorProduct },
			         {
			          extend: "collection",	 
			          text: 'Export',
		              buttons: [
			          {
	                    	extend: 'excel',
	                    	title: 'Product',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Product',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Product',
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
	            { mData: "testFactoryName", className: 'disableEditInline', sWidth: '10%', editField: "testFactoryId",
	            	mRender: function (data, type, full) {
			           	 if (full.action == "create" || full.action == "edit"){
			           		data = optionsValueHandler(editorProduct, 'testFactoryId', full.testFactoryId);
			           	 }
			           	 else if(type == "display"){
			           		data = full.testFactoryName;
			           	 }	           	 
			             return data;
		             },
	            },	
	            { mData: "customerName", className: 'disableEditInline', sWidth: '10%', editField: "customerId",
	            	mRender: function (data, type, full) {
			           	 if (full.action == "create" || full.action == "edit"){
			           		data = optionsValueHandler(editorProduct, 'customerId', full.customerId);
			           	 }
			           	 else if(type == "display"){
			           		data = full.customerName;
			           	 }	           	 
			             return data;
		             },
		         },	
	            { mData: "productId", className: 'disableEditInline', sWidth: '10%'},			    				            
	            { mData: "productName", className: 'editable', sWidth: '10%'},			
	            { mData: "productDescription", className: 'editable', sWidth: '10%'},			
	            { mData: "projectCode", className: 'editable', sWidth: '10%'},			
	            { mData: "projectName", className: 'editable', sWidth: '10%'},			
	            { mData: "typeName", className: 'editable', sWidth: '10%', editField: "productTypeId",
	            	mRender: function (data, type, full) {
			           	 if (full.action == "create" || full.action == "edit"){
			           		data = optionsValueHandler(editorProduct, 'productTypeId', full.productTypeId);
			           	 }
			           	 else if(type == "display"){
			           		data = full.typeName;
			           	 }	           	 
			             return data;
		             },
	            },
	            { mData: "modeName", className: 'disableEditInline', sWidth: '10%'},
	            { mData: null,
	              mRender: function (data, type, full) {
	            	  if ( type === 'display' ) {
	                        return '<input type="checkbox" class="editor-active">';
	                    }
	                    return data;
	                },
	                className: "dt-body-center"
	            },		
	            
	            /*Testing for checkbox*/
	            /*{ mData: "status",
	              mRender: function (data, type, full) {
	            	  if ( type === 'display' ) {
	                        return '<input type="checkbox" class="editor-active">';
	                  }
	                    return data;
	                },
	                className: "dt-body-center"
	            },*/
	            { mData: null,				 
	            	bSortable: false,
	            	mRender: function(data, type, full) {				            	
	           		 var img = ('<div style="display: flex;">'+
         	       		'<button style="border: none; background-color: transparent; outline: none;">'+
         	       				'<img src="css/images/list_metro.png" class="details-control img1" title="Add/Edit Product Version" style="margin-left: 5px;"></button>'+
         	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
         	       				'<img src="css/images/user.png" class="details-control img2" title="User Permissions"></button>'+
         	       			/*'<button style="border: none; background-color: transparent; outline: none;">'+
        							'<img src="css/images/cloning.jpg" style="width: 24px;height: 24px;" class="productcloningImg7" title="Cloning Product" data-toggle="modal" /></button>'+*/
     	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
     	       					'<i class="fa fa-search-plus img3" title="Audit History"></i></button>'+
     	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
     	       					'<i class="fa fa-comments img4" title="Comments"></i></button>'+
     	       			/*'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       						'<img src="css/images/mapping.png" class="details-control img5" title="Map Competencies"></button>'+
   						'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   								'<img src="css/images/mapping.png" class="details-control img6" title="Add/Edit Workflow status"></button>'+*/
         	       		'</div>');	      		
               		 return img;
	            	}
	            },				            
	        ],
	        rowCallback: function ( row, data ) {
	            $('input.editor-active', row).prop( 'checked', data.status == 1 );
	        },
	        "oLanguage": {
	        	"sSearch": "",
	        	"sSearchPlaceholder": "Search all columns"
	        },
		});	    		
		
		// ------
		 $(function(){ // this will be called when the DOM is ready 
			
			 
			 $("#products_dataTable_wrapper .buttons-create").click(function(e){
		    	reArrangeDropDownIndex('DTE_Field_testFactoryId',testFactoryId);
		    	
		    });
			 
			// Activate an inline edit on click of a table cell
	        $('#products_dataTable').on( 'click', 'tbody td.editable', function (e) {
	        	editorProduct.inline( this, {
	                submitOnBlur: true
	            } );
	        } );
			 
	    	$("#products_dataTable_length").css('margin-top','8px');
			$("#products_dataTable_length").css('padding-left','35px');		
			$(".select2-drop").css('z-index','100000');
			
			$('#products_dataTable_wrapper .DTFC_RightWrapper, #products_dataTable_wrapper .DTFC_LeftWrapper').addClass('hidden');
			 
			 $('#products_dataTable').on( 'change', 'input.editor-active', function () {
				 editorProduct
			            .edit( $(this).closest('tr'), false )
			            .set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
			            .submit();
			    });
			 
			productMgmProduct_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );
	 	
	 	editorProduct.dependent('testFactoryId',function ( val, data, callback ) {
    		var url = 'administration.mode.list.option?testFactoryId='+data.values.testFactoryId;
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
    	           editorProduct.set('modeId',json.Options);
    	           editorProduct.field('modeId').update(json.Options);
    	        }
    	    } );
    	});
		
	 	editorProduct.on( 'preSubmit', function ( e, o, action ) {
	 		if(action == 'create'){
				for(var i=0;i<productMgmProduct_oTable.DataTable().data().length;i++){
					if(o.data[0].productName == productMgmProduct_oTable.DataTable().data()[i].productName){
						editorProduct.error("Active product with this given name already exists. Please use another unique name.");
						return false;
					}
				}
			}
	        
	        if ( action !== 'remove' ) {
				var productName = this.field( 'productName' );
				if ( ! productName.isMultiValue() ) {
					if ( productName.val() ) {
						
					}else{
						productName.error( 'Please enter Product name' );
						return false;
					}
				}
			}
	        
	    } );
	 	
		// ----- productVersion child table -----
	    $('#products_dataTable tbody').on('click', 'td button .img1', function () {
	    	
	    	
	    	var tr = $(this).closest('tr');
	    	var row = productMgmProduct_oTable.DataTable().row(tr);
	    	
	    	productSelectedTr = tr;
	    	productSelectedRow = row;
	    	
	    	productVersionResultHandler(row.data().productId, row, tr);
	    	
	    	$("#productVersionPdMgmContainer").modal();
   		});
	    
	    // ----- product UserPermission child table -----
	    $('#products_dataTable tbody').on('click', 'td button .img2', function () {
	    	var tr = $(this).closest('tr');
	    	var row = productMgmProduct_oTable.DataTable().row(tr);
	    	
	    	productSelectedTr = tr;
	    	productSelectedRow = row;
	    	
	    	productUserPermissionResultHandler(row.data().productId, row, tr);
	    	$("#productUserPermissionPdMgmContainer").modal();
   		});
	    
	    // ----- product Audit History child table -----
	    $('#products_dataTable tbody').on('click', 'td button .img3', function () {
	    	var tr = $(this).closest('tr');
	    	var row = productMgmProduct_oTable.DataTable().row(tr);
	    	listProductAuditHistory(row, tr);
   		});	    

	    // ----- product Comments child table -----
	    $('#products_dataTable tbody').on('click', 'td button .img4', function () {
	    	var tr = $(this).closest('tr');
	    	var row = productMgmProduct_oTable.DataTable().row(tr);
	    	//listComments1(row, tr);
	    	var entityTypeIdComments = 18;
	    	var entityNameComments = "Product ";
	    	listComments(entityTypeIdComments, entityNameComments, row.data().productId, row.data().productName, "productComments");
	    });
	    
	    // ----- product Map Competencies child table -----
	    $('#products_dataTable tbody').on('click', 'td button .img5', function () {
	    	var tr = $(this).closest('tr');
	    	var row = productMgmProduct_oTable.DataTable().row(tr);
	    	manageCompetenciesMapping("",row.data().productId, row.data().productName);	    	
   		});
	    
	    // ----- product Workflow status child table -----
	    $('#products_dataTable tbody').on('click', 'td button .img6', function (event) {
	    	var tr = $(this).closest('tr');
	    	var row = productMgmProduct_oTable.DataTable().row(tr);
    		manageWorkflowStatusMapping("",row.data().productId, row.data().productName);
   		});   
	    
	    
	 // ----- Cloning -----
		 
		 $('#products_dataTable tbody').on('click', 'td button .productcloningImg7', function (event) {
			 var tr = $(this).closest('tr');
		    var row = productMgmProduct_oTable.DataTable().row(tr);
		    srcProductName=row.data().productName;
		    $('#div_productCloning').show();
		    displayProductIdProductName(row.data().productId,row.data().productName);
		});
		  
		 });
}

var clearTimeoutDTPMP='';
function reInitializeDTProductManagementPlan(){
	clearTimeoutDTPMP = setTimeout(function(){				
		productMgmProduct_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTPMP);
	},500);
}

// ----- Product Version Started -----

function productMgmChild1Table(){
	var childDivString = '<table id="productVersion_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th id="productVersion">Product Version</th>'+
			'<th>Product Version ID</th>'+
			'<th>Description</th>'+
			'<th>Web Application URL</th>'+
			'<th>Source Location</th>'+
			'<th>Binary Location</th>'+
			'<th>Release Date</th>'+
			'<th>Status</th>'+
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
		'</tr>'+
	'</tfoot>'+	
'</table>';		
	
	return childDivString;	
}

function productMgmProductVersion_Container(data, row, tr){
	
	
	var titleLable = prodcuctplanmgmtLabel +" Version *";
	try{
		if ($("#productVersionPdMgmDTContainer").children().length>0) {
			$("#productVersionPdMgmDTContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = productMgmChild1Table(); 			 
	$("#productVersionPdMgmDTContainer").append(childDivString);
		
	// --- editor for the product Version started -----
	
	editorProductVersion = new $.fn.dataTable.Editor( {
	    "table": "#productVersion_dataTable",
		ajax: 'administration.product.version.add',
		ajaxUrl: "administration.product.version.update",
		"idSrc":  "productVersionListId",
		i18n: {
	        create: {
	            title:  "Create a new Version ",
	            submit: "Create",
	        }
	    },
		fields: [ {
            label: "Product",
            name: "productId",
            "type": "hidden",
            "default": row.data().productId,
        },
        {
            label: "productVersionListId",
            name: "productVersionListId",
            "type": "hidden",
            "default": row.data().productVersionListId,
        },        
        {
            label: "createdDate",
            name: "createdDate",
            "type": "hidden",
            "default": row.data().createdDate,
        },
        {
            label: "statusChangeDate",
            name: "statusChangeDate",
            "type": "hidden",
            "default": row.data().statusChangeDate,
        },
        {
            label: "productName",
            name: "productName",
            "type": "hidden",
            "default": row.data().productName,
        },
        {
            label: "testFactory",
            name: "testFactory",
            "type": "hidden",
            "default": row.data().testFactory,
        },
        {
            label: "status",
            name: "status",
            "type": "hidden",
            "default": row.data().status,
        },
        {
            label: titleLable,
            name: "productVersionName",
        }, {
            label: "Description",
            name: "productVersionDescription",
        },{
            label: "Web Application URL",
            name: "webAppURL",			                
        }, {
            label: "Source Location",
            name: "targetSourceLocation",
        }, {
            label: "Binary Location",
            name: "targetBinaryLocation",
        }, {
            label: "Release Date",
            name: "releaseDate",
            type:  'datetime',
            def:    function () { return new Date(); },
            format: 'M/D/YYYY',
        }
    ]
	});
	
	$( 'input', editorProductVersion.node()).on( 'focus', function () {
		this.select();
	});
	
	// ----- ended -----
	
	productMgmProductVersion_oTable = $("#productVersion_dataTable").dataTable( {
		 	"dom": '<"top"Bf<"clear">>rt<"bottom"ip<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,
	       /*fixedColumns:   {
	           leftColumns: 1,
	       }, */
	       "fnInitComplete": function(data) {
				  var searchcolumnVisibleIndex = [7,8]; // search column TextBox Invisible Column position
	     		  $("#productVersion_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
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
	     		 reInitializeDTProductMgmProductVersion();			   
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: editorProductVersion },	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Product Version',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Product Version',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Product Version',
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
	         columnDefs: [
	              //{ targets: [0,1,2,3,4,5,6,7,8,9,10,11,12], visible: true},
	              //{ targets: '_all', visible: false }
	          ],
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "productVersionName",className: 'editable', sWidth: '15%' },
           { mData: "productVersionListId",className: 'disableEditInline', sWidth: '15%' },
           { mData: "productVersionDescription",className: 'editable', sWidth: '20%' },		
           { mData: "webAppURL",className: 'editable', sWidth: '20%' },		
           { mData: "targetSourceLocation",className: 'editable', sWidth: '12%' },		
           { mData: "targetBinaryLocation",className: 'editable', sWidth: '12%' },	
           { mData: "releaseDate", className: 'editable', sWidth: '15%',
           		mRender: function (data, type, full) {
		           	 if (full.action == "create"){
		           		data = convertDTDateFormatAdd(data, ["releaseDate"]);
		           		productVersionResultHandler(full.productId, productSelectedRow, productSelectedTr);
		           	 }else if(type == "display"){
		           		data = full.releaseDate;
		           	 }	           	 
		             return data;
	             }
           },
           { mData: null,
              mRender: function (data, type, full) {
            	  if ( type === 'display' ){
                        return '<input type="checkbox" class="editorProductVersion-active">';
                    }
                    return data;
                },
                className: "dt-body-center"
            },
            { mData: null,				 
            	bSortable: false,
            	sWidth: '4%',
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
  	       		'<button style="border: none; background-color: transparent; outline: none;">'+
  	       				'<img src="css/images/list_metro.png" class="details-control imgVersion1" title="Add/Edit Product Build" style="margin-left: 5px;"></button>'+
  	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
  	       				  '<i class="fa fa-search-plus imgVersion2 showHandCursor" title="Audit History"></i></button>'+
  	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
	       					'<i class="fa fa-comments imgVersion3" title="Comments"></i></button>'+
  	       		'</div>');	      		
        		 return img;
            	}
            },
       ],
       rowCallback: function ( row, data ) {
           $('input.editorProductVersion-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	// ------
	 $(function(){ // this will be called when the DOM is ready
		 
		
	    		$("#productVersion").text(prodcuctplanmgmtLabel+" Version");
	    		$(".img1").attr('title',"Add/Edit "+ prodcuctplanmgmtLabel+" Vesion");
	    		$(".imgVersion1").attr('title',"Add/Edit "+prodcuctplanmgmtLabel +" Build")
	    		$("#productVersionBuildPdMgmContainer .modal-title").text("Add/Edit "+prodcuctplanmgmtLabel+" Build");
	    		$(".productcloningImg7").attr('title',"Cloning"+ prodcuctplanmgmtLabel);
	    	 
		 
	// Activate an inline edit on click of a table cell
    $('#productVersionPdMgmDTContainer').on( 'click', 'tbody td.editable', function (e) {
    	editorProductVersion.inline( this, {
            submitOnBlur: true
        } );
    });
	
    productMgmProductVersion_oTable.DataTable().columns().every( function () {
        var that = this;
        $('input', this.footer() ).on( 'keyup change', function () {
            if ( that.search() !== this.value ) {
                that
                	.search( this.value, true, false )
                    .draw();
            }
        } );
	} );
	
    editorProductVersion.on( 'preSubmit', function ( e, o, action ) {
        if ( action !== 'remove' ) {
        	var validationArr = ['productVersionName'];
        	var productVersionName;
        	for(var i=0;i<validationArr.length;i++){
        		productVersionName = this.field(validationArr[i]);
	            if ( ! productVersionName.isMultiValue() ) {
	                if ( productVersionName.val() ) {
                	}else{
	                	if(validationArr[i] == "productVersionName"){
	                		productVersionName.error( 'Please enter Product Version Name' );
	                	}
                	}
	            }
        	}

            // If any error was reported, cancel the submission so it can be corrected
            if ( this.inError() ) {
                return false;
            }
        }
    } );
    
    $('#productVersion_dataTable').on( 'change', 'input.editorProductVersion-active', function () {
		 editorProductVersion
            .edit( $(this).closest('tr'), false )
            .set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
            .submit();
	 });		 
	 
	// ----- product UserPermission child table -----
	  $('#productVersionPdMgmDTContainer').on('click', 'td button .imgVersion1', function () {
		  var tr = $(this).closest('tr');
		  var row = productMgmProductVersion_oTable.DataTable().row(tr);
		  
		  productVersionSelectedTr = tr;		  
		  productVersionSelectedRow = row;
		  
		  productVersionBuildResultHandler(row.data().productVersionListId, row, tr);
		  $("#productVersionBuildPdMgmContainer").modal();
	});
    
 // ----- product Audit History child table -----
    $('#productVersionPdMgmDTContainer').on('click', 'td button .imgVersion2', function () {
    	var tr = $(this).closest('tr');
    	var row = productMgmProductVersion_oTable.DataTable().row(tr);
    	listProductVersionAuditHistory(row, tr);
		});
	 
  // ----- product Comments child table -----
    $('#productVersionPdMgmDTContainer').on('click', 'td button .imgVersion3', function () {
    	var tr = $(this).closest('tr');
    	var row = productMgmProductVersion_oTable.DataTable().row(tr);
    	var entityTypeIdComments = 19;
    	var entityNameComments = "Product Version";
    	listComments(entityTypeIdComments, entityNameComments, row.data().productVersionListId, row.data().productVersionName, "productVersionComments");
		});
    
	 });
}

var clearTimeoutDTProductMgmProductVersion='';
function reInitializeDTProductMgmProductVersion(){
	clearTimeoutDTProductMgmProductVersion = setTimeout(function(){				
		productMgmProductVersion_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTProductMgmProductVersion);
	},200);
}


// --- Product User Permission Started -----

function productMgmChild2Table(){
	var childDivString = '<table id="productUserPermission_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>User Name</th>'+
			'<th>Role</th>'+
			'<th>Status</th>'+
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

function productUserPermissionResults_Container(data, row, tr){
	$('#userPermissionListItemLoaderIcon').show();
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [{id:"userId", type:optionsType_productUserPermission, url:'list.user.by.specific.role?testFactoryId='+row.data().testFactoryId+'&productId='+row.data().productId,},
	              {id:"roleId", type:optionsType_productUserPermission, url:'administration.user.roleList?userId='},
	              //{id:"userGroupId", type:optionsType_productUserPermission, url:'user.group.list.options?testFactoryId='+row.data().testFactoryId+'&productId='+row.data().productId+'&isConsolidated=true'},
	              ];
	returnOptionsItem(optionsArr[0].url, row, data, tr);
}

function productMgmProductUserPermission_Container(data, row, tr){
	
	try{
		if ($("#productUserPermissionPdMgmDTContainer").children().length>0) {
			$("#productUserPermissionPdMgmDTContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = productMgmChild2Table(); 			 
	$("#productUserPermissionPdMgmDTContainer").append(childDivString);
		
		editorProductUserPermission = new $.fn.dataTable.Editor( {
			    "table": "#productUserPermission_dataTable",
				ajax: "administration.product.addUserRole",
				ajaxUrl: "administration.product.updateUserRole",
				"idSrc":  'productUserRoleId',
				i18n: {
	    	        create: {
	    	            title:  "Grant Access User Permission",
	    	            submit: "Create",
	    	        }
	    	    },
				fields: [ {
					label: "testFactoryId:",
		            name: "testFactoryId",
		            "type"  : "hidden",
				    "default": row.data().testFactoryId,
		        },{
					label: "productUserRoleId:",
		            name: "productUserRoleId",
		            "type"  : "hidden",
		        },{
					label: "name:",
		            name: "name",
		            "type"  : "hidden",
		        },{
					label: "description:",
		            name: "description",
		            "type"  : "hidden",
		        },{
					label: "createdDate:",
		            name: "createdDate",
		            "type"  : "hidden",
		        },{
					label: "modifiedDate:",
		            name: "modifiedDate",
		            "type"  : "hidden",
		        },{
		            label: "Product",
		            name: "productId",
		            "type": "hidden",
		            "default": row.data().productId,
		        },{
		            label: "Product Name",
		            name: "productName",
		            "type": "hidden",
		            "default": row.data().productName,
		        },		        
		        {
		            label: "User Name:",
		            name: "userId",
		            //options: 'list.user.by.specific.role?testFactoryId='+row.data().testFactoryId+'&productId='+row.data().productId,
		            options: optionsResultArr[0],
		            "type"  : "select",
		        },{
		            label: "Role:",
		            name: "roleId",
		             options: optionsResultArr[1],
		            "type"  : "select",
		        },/*{
		            label: "Group:",
		            name: "userGroupId",
		             options: optionsResultArr[2],
		            "type"  : "select",
		        },*/
		        {
		        	label: "status",
		        	name: "status",
		        	"type": "hidden",
		        	"default": row.data().status,
		        }
		    ]
			});
		
	productMgmProductUserPermission_oTable = $("#productUserPermission_dataTable").dataTable( {
		 	"dom": '<"top"Bf<"clear">>rt<"bottom"ip<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex = [3]; // search column TextBox Invisible Column position
			  $('#productUserPermission_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
	    	   reInitializeDTProductManagementPlanUserPermission();  
	    	   
	    	   		   },  
		   buttons: [
		             { extend: "create", editor: editorProductUserPermission },
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Product User Permission',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Product User Permission',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Product User Permission',
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
        aaData:data,		    				 
	    aoColumns: [
	       { mData: "productUserRoleId",className: 'disableEditInline' , sWidth: '5%' },
           { mData: "userName", sWidth: '45%', editField: "userId",
            	mRender: function (data, type, full) {
		           	 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(editorProductUserPermission, 'userId', full.userId);
		           	 }
		           	 else if(type == "display"){
		           		data = full.loginId;
		           	 }	           	 
		             return data;
	             },
            },
            { mData: "roleName", sWidth: '45%', editField: "roleId",
            	mRender: function (data, type, full) {
		           	 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(editorProductUserPermission, 'roleId', full.roleId);
		           	 }
		           	 else if(type == "display"){
		           		data = full.roleName;
		           	 }	           	 
		             return data;
	             },
            },
           { mData: null,
               mRender: function (data, type, full) {
             	  if ( type === 'display' ){
                         return '<input type="checkbox" class="editorProductUserPermission-active">';
                     }
                     return data;
                 },
                 className: "dt-body-center"
             },
             
       ],
       rowCallback: function ( row, data ) {
           $('input.editorProductUserPermission-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
    	   "sSearch": "",
    		"sSearchPlaceholder": "Search all columns"
       },     
	});
	
	// -----
	
	$(function(){ // this will be called when the DOM is ready
		$('#userPermissionListItemLoaderIcon').hide();
		
		$('#productUserPermission_dataTable').on( 'change', 'input.editorProductUserPermission-active', function () {
			editorProductUserPermission
				.edit( $(this).closest('tr'), false )
				.set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
				.submit();
		});
		
		productMgmProductUserPermission_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
		} );
		
		editorProductUserPermission.dependent('userId',function ( val, data, callback ) {
			if(data.values.userId != undefined){		
				
    		var url = 'administration.user.roleList?userId='+data.values.userId;
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
    	        	editorProductUserPermission.set('roleId',json.Options);
    	        	editorProductUserPermission.field('roleId').update(json.Options);   	        	
    	        }   	  
        	
    	    } );
		}});
		
	});
	
	
}

function reInitializeDTProductManagementPlanUserPermission(){
	clearTimeoutDTPMP = setTimeout(function(){				
		productMgmProductUserPermission_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTPMP);
	},100);
}

//--- Product Build started -----

function productMgmChild3Table(){
	var childDivString = '<table id="productVersionBuild_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Build No</th>'+
			'<th>Build ID</th>'+
			'<th>Name</th>'+
			'<th>Description</th>'+
			'<th>Build Date</th>'+
			'<th>Build Type</th>'+
			'<th>Cloned Build</th>'+			
			'<th>Status</th>'+
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
		'</tr>'+
'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}


function productVersionBuildResults_Container(data, row, tr){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [{id:"buildTypeId", type:optionsType_productBuild, url:'common.list.defect.identification.stages.list'},	               
	              ];
	returnOptionsItem(optionsArr[0].url, row, data, tr);	
}

function createEditorForProductVersionBuild(data, row){
	
	//---  in parent url - buildTypeId is missing --- temporary fix ----
    if(data.length>0){
    	for(var i=0;i<data.length;i++){
    		if(data[i].productVersionListId == row.data().productVersionListId){
    			row.data()['buildTypeId'] = data[i].buildTypeId;
    			break;
    		};
    	};
    }
		
	editorProductBuild = new $.fn.dataTable.Editor( {
	    "table": "#productVersionBuild_dataTable",
		ajax: "administration.product.build.add",
		ajaxUrl: "administration.product.build.update",
		"idSrc":  'productBuildId',
		i18n: {
	        create: {
	            title:  "Create a new Build",
	            submit: "Create",
	        }
	    },
		fields: [{
            label: "Build No *",
            name: "productBuildNo",
        },{
            label: "Name *",
            name: "productBuildName",
        },{
            label: "Description",
            name: "productBuildDescription",
        },{
			label: "productBuildId:",
            name: "productBuildId",
            "type"  : "hidden",
        },{
            label: "Build Date",
            name: "productBuildDate",
            type:  'datetime',
            def:    function () { return new Date(); },
            format: 'M/D/YYYY',
            //fieldInfo: "MM/DD/YYYY",
        },		        
        {
            label: "Build Type:",
            name: "buildTypeId",
            //options: 'common.list.defect.identification.stages.list',
             options: optionsResultArr[0],
            "type"  : "select",
        },{
			label: "ProductVersionId:",
            name: "productVersionListId",
            "type"  : "hidden",
		    "default": row.data().productVersionListId,
        },{
			label: "clonedProductBuildName:",
            name: "clonedProductBuildName",
            "type"  : "hidden",
		    "default": row.data().clonedProductBuildName,
        },{
			label: "status:",
            name: "status",
            "type"  : "hidden",
		    "default": row.data().status,
        },
    ]
	});
	
	// ----- inline edit functionality -----
	
	$( 'input', editorProductBuild.node()).on( 'focus', function () {
		this.select();
	});
	
}

function productMgmProductVersionBuildContainer(data, row, tr){
	
	try{
		if ($("#productVersionBuildPdMgmDTContainer").children().length>0) {
			$("#productVersionBuildPdMgmDTContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = productMgmChild3Table(); 			 
	$("#productVersionBuildPdMgmDTContainer").append(childDivString);

	createEditorForProductVersionBuild(data, row);
	
	productMgmProductVersionBuild_oTable = $("#productVersionBuild_dataTable").dataTable( {
		 	"dom": '<"top"Bf<"clear">>rt<"bottom"ip<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,
	       /*fixedColumns:   {
	           leftColumns: 1,
	       }, */
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex = [7,8]; // search column TextBox Invisible Column position
			  $('#productVersionBuild_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			  reInitializeDTProductMgmProductVersionBuild();
		   },  
		   buttons: [
		             { extend: "create", editor: editorProductBuild },
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Product Version Build',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Product Version Build',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Product Version Build',
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
	         columnDefs: [
	              //{ targets: [0,1,2,3,4,5,6,7,8,9,10,11,12], visible: true},
	              //{ targets: '_all', visible: false }
	          ],
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "productBuildNo",className: 'editable', sWidth: '15%' },
           { mData: "productBuildId",className: 'disableEditInline', sWidth: '15%' },
           { mData: "productBuildName",className: 'editable' , sWidth: '15%' },	
           { mData: "productBuildDescription",className: 'editable' , sWidth: '15%' },
           { mData: "productBuildDate", className: 'editable', sWidth: '15%',
          		mRender: function (data, type, full) {
		           	 if (full.action == "create"){
		           		data = convertDTDateFormatAdd(data, ["productBuildDate"]);			           		
		           		productVersionBuildResultHandler(full.productVersionListId, productVersionSelectedRow, productVersionSelectedTr);
		           	 }else if(type == "display"){
		           		data = full.createdDate;
		           	 }	           	 
		             return data;
	             }
           },
           { mData: "buildTypeName",className: 'editable' , sWidth: '5%', editField: "buildTypeId",
	    		 mRender: function (data, type, full) {
		           	 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(editorProductBuild, 'buildTypeId', full.buildTypeId);
		           	 }
		           	 else if(type == "display"){
		           		data = full.buildTypeName;
		           	 }	           	 
		             return data;
	             },		   
	       },
           { mData: "clonedProductBuildName",className: 'disableEditInline' , sWidth: '10%' },
           { mData: null,
        	 sWidth: '2%',
             mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editorProductBuild-active">';
                    }
                    return data;
                },
                className: "dt-body-center"
            },
            { mData: null,				 
            	bSortable: false,
            	sWidth: '4%',
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
  	       		/*'<button style="border: none; background-color: transparent; outline: none;">'+
  	       				'<i class="fa fa-copy showHandCursor imgVersionBuild1" title="Clone Build" style="margin-left: 5px;"></i></button>'+*/
  	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
  	       				  '<i class="fa fa-search-plus imgVersionBuild2 showHandCursor" title="Audit History"></i></button>'+
       	       	'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
	       					'<i class="fa fa-comments imgVersionBuild3" title="Comments"></i></button>'+				
				'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       						'<img src="css/images/analytics.jpg" class="details-control imgVersionBuild4" title="Intelligent TestPlan"></button>'+					
  	       		'</div>');	      		
        		 return img;
            	}
            },
       ],
       rowCallback: function ( row, data ) {
           $('input.editorProductBuild-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	});
	
	// ------
	 $(function(){ // this will be called when the DOM is ready
		 
	// Activate an inline edit on click of a table cell
    $('#productVersionBuildPdMgmDTContainer').on( 'click', 'tbody td.editable', function (e) {
    	editorProductBuild.inline( this, {
            submitOnBlur: true
        } );
    });	
    
    $('#productVersionBuildPdMgmDTContainer').on( 'change', 'input.editorProductBuild-active', function () {
    	editorProductBuild
            .edit( $(this).closest('tr'), false )
            .set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )            
            .submit();
    });
	
    editorProductBuild.on( 'preSubmit', function ( e, o, action ) {
        if ( action !== 'remove' ) {
        	var validationArr = ['productBuildNo','productBuildName'];
        	var inputText;
        	for(var i=0;i<validationArr.length;i++){
        		inputText = this.field(validationArr[i]);
	            if ( ! inputText.isMultiValue() ) {
	                if ( inputText.val() ) {
                	}else{
	                	if(validationArr[i] == "productBuildNo"){
	                		inputText.error( 'Please enter Product Build Number' );
	                	}
	                	if(validationArr[i] == "productBuildName"){
	                		inputText.error( 'Please enter Product Build Name' );
	                	}
                	}
	            }
        	}

            // If any error was reported, cancel the submission so it can be corrected
            if ( this.inError() ) {
                return false;
            }
        }
    } );
    
    productMgmProductVersionBuild_oTable.DataTable().columns().every( function () {
        var that = this;
        $('input', this.footer() ).on( 'keyup change', function () {
            if ( that.search() !== this.value ) {
                that
                	.search( this.value, true, false )
                    .draw();
            }
        } );
	} );
	 
	// ----- product version Build Clone child table -----
	 $('#productVersionBuildPdMgmDTContainer').on('click', 'td button .imgVersionBuild1', function () {
	   	var tr = $(this).closest('tr');
	   	var row = productMgmProductVersionBuild_oTable.DataTable().row(tr);
	   	cloneBuild(row.data().productBuildId); 
	});
   
	 // ----- product Version BuildAudit History child table -----
   $('#productVersionBuildPdMgmDTContainer').on('click', 'td button .imgVersionBuild2', function () {
   		var tr = $(this).closest('tr');
   		var row = productMgmProductVersionBuild_oTable.DataTable().row(tr);
   		listProductBuildAuditHistory(row, tr);
	});	
	 
   // ----- product Comments child table -----
   $('#productVersionBuildPdMgmDTContainer').on('click', 'td button .imgVersionBuild3', function () {
   	var tr = $(this).closest('tr');
   	var row = productMgmProductVersionBuild_oTable.DataTable().row(tr);
   	var entityTypeIdComments = 20;
	var entityNameComments = "Product Build";
   	listComments(entityTypeIdComments, entityNameComments, row.data().productBuildId, row.data().productBuildName, "productBuildComments");
		});
   
	 });
	 
	  // ----- product Comments child table -----
   $('#productVersionBuildPdMgmDTContainer').on('click', 'td button .imgVersionBuild4', function () {
   	var tr = $(this).closest('tr');
   	var row = productMgmProductVersionBuild_oTable.DataTable().row(tr);
   
		var url='getISERecommended.Testcases.by.buildId?buildId='+row.data().productBuildId;
		var title = "Product Name:"+row.data().productName+" - Build Name:"+row.data().productBuildName+" - Recommended Test Cases";
		var jsonObj={"Title":title,		
				"listURL": url,					
				"componentUsageTitle":"recommendedTestPlan",
				"productId": row.data().productId,
				"buildId" : row.data().productBuildId,
				"componentUsageTitle": "ProductFeature"
		};
		RecommentedTestPlan.init(jsonObj);
		$("#recommendedTestPlanPdMgmContainer").modal();
	});
}
var clearTimeoutDTProductMgmProductVersionBuild='';
function reInitializeDTProductMgmProductVersionBuild(){
	clearTimeoutDTProductMgmProductVersionBuild = setTimeout(function(){				
		productMgmProductVersionBuild_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTProductMgmProductVersionBuild);
	},200);
}

//------ Audit History Started -----

function listProductAuditHistory(row){
	var url='administration.event.list?sourceEntityType=Product&sourceEntityId='+row.data().productId+'&jtStartIndex=0&jtPageSize=10000';
	var jsonObj={"Title":"Product Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,
			"componentUsageTitle":"productAudit",
	};
	SingleDataTableContainer.init(jsonObj);
}

function listComments1(row, tr){
	var url='comments.for.entity.or.instance.list?productId=0&entityTypeId=18&entityInstanceId='+row.data().productId+'&jtStartIndex=0&jtPageSize=10000';
	var instanceId = row.data().productId;
	var jsonObj={"Title":"Product Comments:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,
			"componentUsageTitle":"productComments",
			"tr":tr,
			"row":row,
			"entityTypeId":18,
			"entityInstanceId":instanceId,
	};
	SingleDataTableContainer.init(jsonObj);
}

function listComments(entityTypeId, entityName, instanceId, instanceName, componentUsageTitle){

	var url='comments.for.entity.or.instance.list?productId=0&entityTypeId='+entityTypeId+'&entityInstanceId='+instanceId+'&jtStartIndex=0&jtPageSize=10000';
	//var instanceId = row.data().productId;
	var jsonObj={"Title":"Comments on "+entityName+ ": " +instanceName,
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,
			"componentUsageTitle":componentUsageTitle,			
			"entityTypeId":entityTypeId,
			"entityInstanceId":instanceId,
			"productId":productId,
	};
	CommentsMetronicsUI.init(jsonObj);
}


function listProductVersionAuditHistory(row){
	var url='administration.event.list?sourceEntityType=ProductVersion&sourceEntityId='+row.data().productVersionListId+'&jtStartIndex=0&jtPageSize=10000';
	var jsonObj={"Title":"Product Version Audit History",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,
			"componentUsageTitle":"productVersionAudit",
	};
	SingleDataTableContainer.init(jsonObj);
}

function listProductBuildAuditHistory(row){
	var url='administration.event.list?sourceEntityType=ProductBuild&sourceEntityId='+row.data().productBuildId+'&jtStartIndex=0&jtPageSize=10000';
	var jsonObj={"Title":"Product Build Audit History",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,
			"componentUsageTitle":"productBuildAudit",
	};
	SingleDataTableContainer.init(jsonObj);
}

// ----product cloning -----

function submitProductCloneHandler() {

	var destProductName=$("#cloningProductName").val();
	var startDate=$("#startDate").val();
	var endDate=$("#endDate").val();	
	var team =	$('#team_clone').val();
	var userpremssion =	$('#userPermission_clone').val();
	var workflow =	$('#workFlow_clone').val();
	var workpackage =	$('#workpackage_clone').val();
	
	$.get('rest/restAPIService/cloneProduct?srcProductName='+srcProductName+'&destProductName='+destProductName+'&startDate='+startDate+'&endDate='+endDate+'&team='+team+'&userpremssion='+userpremssion+'&workflow='+workflow+'&workpackage='+workpackage ,function(data) {	
		console.log("success "+data);
		callAlert(data);
	});

	$("#div_productCloning").modal('hide');// $("#div_productCloning").empty();
	
	$('#cloningProductName').val("");
	$('#startDate').val("");
	$('#endDate').val("");
	
	$('#uniform-team_clone span').attr("class", "");
	$('#uniform-userPermission_clone span').attr("class", "");
	$('#uniform-workFlow_clone span').attr("class", "");
	$('#uniform-build_clone span').attr("class", "");
	$('#uniform-version_clone span').attr("class", "");
	$('#uniform-workpackage_clone span').attr("class", "");
	
}

function popupCloseProductCloneHandler(){
	$("#div_productCloning").modal('hide');
	$('#cloningProductName').val("");
	$('#startDate').val("");
	$('#endDate').val("");
	$('#uniform-team_clone span').attr("class", "");
	$('#uniform-userPermission_clone span').attr("class", "");
	$('#uniform-workFlow_clone span').attr("class", "");
	$('#uniform-build_clone span').attr("class", "");
	$('#uniform-version_clone span').attr("class", "");
	$('#uniform-workpackage_clone span').attr("class", "");
	
	
	}

function displayProductIdProductName(productId,productName){
	
$("#div_productCloning").modal();
	
	var productNameTitle="";
	if(productName.length > 25){         		
		productNameTitle = (productName).toString().substring(0,20)+'...';         		
 	} else {
 		productNameTitle =productName;
 	}
	$("#div_productCloning .modal-header h4").text("Clone Product : ["+productId+"] "+productNameTitle);
	$("#div_productCloning .modal-header h4").attr('title',productName);
}
function closeCloningContainer(){
	$("#div_productCloning").modal('hide');
}