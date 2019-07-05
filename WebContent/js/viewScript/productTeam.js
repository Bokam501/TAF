function productTeamResources(){
	setProductNode();
	
	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#ProductTeamResources').children().show();
		
		// the following url needs to be changed to get data from product Team resource table
		var urlToGetProductTeamResourcesOfSpecifiedProductId = 'product.team.resouces.list?productId='+productId+"&jtStartIndex=0&jtPageSize=10000";
		listProductTeamResourcesOfSelectedProductInPdMgm(urlToGetProductTeamResourcesOfSpecifiedProductId);
	}
}

var productTeamResourcesOptionsItemCounter=0;
var productTeamResourcesResultArr=[];			
var	productTeamResourcesOptionsArr=[];
var productTeamResources_oTable='';
var productTeamResources_editor=''
var productTeamResourcesFlag=false;
var optionsType_productTeamResources="ProductTeamResources";	
var clearTimeoutDTProductTeamResources='';

 function listProductTeamResourcesOfSelectedProductInPdMgm(url){
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
			listProductTeamResources_Container(data);		
			
		  },
		  error : function(data) {
			 closeLoaderIcon();  			
		 },
		 complete: function(data){
			closeLoaderIcon();		
		 }
	});
}

function listProductTeamResources_Container(data){		
	productTeamResourcesOptionsItemCounter=0;
	productTeamResourcesResultArr=[];			
	productTeamResourcesFlag=false;
	var userID = 0;	
	if(data.length == 0 || typeof userID == 'undefined' || userID == null){
		userID = 0;
	}else{
		userID = data[0].userId;
	}
	
	productTeamResourcesOptionsArr = [{id:"userID", type:optionsType_productTeamResources, url:'common.administration.user.userListByResourcePoolId?resourcePoolId=-10'+"&productId="+productId},
		{id:"environmentCategoryId", type:optionsType_productTeamResources, url:'testFactoryManagementControl.administration.user.roleList?userId='+userID},		
	];
	
	returnOptionsItemForProductTeamResources(productTeamResourcesOptionsArr[0].url, data);
}

function returnOptionsItemForProductTeamResources(url, data){
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
	         	productTeamResourcesResultArr.push(json.Options); 
	         	productTeamResourcesOptionsItemCounter++;
	         	//returnOptionsItemForProductTeamResources(url, data);
	         	
	         	 if(productTeamResourcesOptionsItemCounter<productTeamResourcesOptionsArr.length-1){			   
					   returnOptionsItemForProductTeamResources(productTeamResourcesOptionsArr[productTeamResourcesResultArr.length].url, data);     		  
		     	   }else{   			  	      			       			   				  				  
		     		  productTeamResources_Container(data); 
				   }
	         	
	         }else{        	        	     	        	
				for(var i=0;i<json.Options.length;i++){
					if(json.Options[i].DisplayText == null)
						json.Options[i].DisplayText = "--";	
					
					json.Options[i].label=json.Options[i].DisplayText;
					json.Options[i].value=json.Options[i].Value;
				}
	     	   productTeamResourcesResultArr.push(json.Options);
	     	   closeLoaderIcon();
			   
	     	   if(productTeamResourcesOptionsItemCounter<productTeamResourcesOptionsArr.length-1){			   
				   returnOptionsItemForProductTeamResources(productTeamResourcesOptionsArr[productTeamResourcesResultArr.length].url, data);     		  
	     	   }else{   			  	      			       			   				  				  
	     		  productTeamResources_Container(data); 
			   }
	         }
	         productTeamResourcesOptionsItemCounter++;     	   
		 
         },error: function (data) {
        	 productTeamResourcesOptionsItemCounter++;
        	 closeLoaderIcon(); 
         },complete: function(data){
         	closeLoaderIcon(); 
         },	            
   	});	
}

function reInitializeDTProductTeamResources(){
	clearTimeoutDTProductTeamResources = setTimeout(function(){				
		productTeamResources_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTProductTeamResources);
	},200);
}

function productTeamResourcesHeader(){		
	var tr = '<tr>'+			
		'<th>User</th>'+
		'<th>Default Role</th>'+
		'<th>Role</th>'+
		'<th>From Date</th>'+
		'<th>To Date</th>'+
		'<th>% allocation</th>'+
		'<th>Remarks</th>'+
		'<th>Status</th>'+
		'<th></th>'+
	'</tr>';
	return tr;
}

function productTeamResourcesEditor(){	
    
	productTeamResources_editor = new $.fn.dataTable.Editor( {
			"table": "#productTeamResources_dataTable",
    		ajax: "product.team.user.add",
    		ajaxUrl: "product.team.user.update",
    		idSrc:  "productTeamResourceId",
    		i18n: {
    	        create: {
    	            title:  "Add/Edit Product Team Resources",
    	            submit: "Create",
    	        }
    	    },
    		fields: [{								
				label:"productId",
				name:"productId",					
				type: 'hidden',	
                "def":productId, 				
			},{								
				label:"productTeamResourceId",
				name:"productTeamResourceId",					
				type: 'hidden',	
						
			},{								
				label:"loginId",
				name:"loginId",	
				type:"hidden",	
						
			},{								
				label:"User *",
				name:"userId",	
				options: productTeamResourcesResultArr[0],
                "type"  : "select",	
						
			},{								
				label:"userDefaultRoleName",
				name:"userDefaultRoleName",					
				type: 'hidden',	
						
			},{								
				label:"Role *",
				name:"productSpecificUserRoleId",					
				options: productTeamResourcesResultArr[1],
                "type"  : "select",
			},{								
				label:"productSpecificUserRoleName",
				name:"productSpecificUserRoleName",					
				type: 'hidden',	
						
			},{
                label: "From Date *",
                name: "fromDate",				
                type:  'datetime',			
				format: 'M/D/YYYY',	
            },{
                label: "To Date *",
                name: "toDate",				
                type:  'datetime',			
				format: 'M/D/YYYY',
            },{
                label: " % allocation *",
                name: "percentageofallocation",                
            },{
                label: "Remarks ",
                name: "remarks",                
            },{
                label: "status",
                name: "status", 
				"type": "hidden",	
				"def":1
			}
        ]
    	});	
}

function productTeamResources_Container(data){
	try{
		if ($("#jTableContainerProductTeamResources").children().length>0) {
			$("#jTableContainerProductTeamResources").children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(9);  // total coulmn count		  
	  var childDivString = '<table id="productTeamResources_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#jTableContainerProductTeamResources").append(childDivString); 						  
	  
	  $("#productTeamResources_dataTable thead").html('');
	  $("#productTeamResources_dataTable thead").append(productTeamResourcesHeader());
	  
	  $("#productTeamResources_dataTable tfoot tr").html('');     			  
	  $("#productTeamResources_dataTable tfoot tr").append(emptytr);
	
	// --- editor for the activity Change Request started -----
	productTeamResourcesEditor();
	
	if(data.length>0){
		for(var i=0;i<data.length;i++){
			if(data[i]["fromDate"] == null || data[i]["toDate"] == null){				
				data[i]["fromDate"] = null;
				data[i]["toDate"] = null;
				
			}else{
				data[i]["fromDate"] = convertDTDateFormatAdd(data[i]["fromDate"]);
				data[i]["toDate"] = convertDTDateFormatAdd(data[i]["toDate"]);		
			}
			
			if(data[i]["createdDate"] == null || data[i]["modifiedDate"] == null){				
				data[i]["createdDate"] = null;
				data[i]["modifiedDate"] = null;
				
			}else{
				data[i]["createdDate"] = convertDTDateFormatAdd(data[i]["createdDate"]);
				data[i]["modifiedDate"] = convertDTDateFormatAdd(data[i]["modifiedDate"]);		
			}			
		}
	}
			
	productTeamResources_oTable = $("#productTeamResources_dataTable").dataTable( {
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
			  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
     		  $("#productTeamResources_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
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
			   reInitializeDTProductTeamResources();			   
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: productTeamResources_editor },	  
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
		   { mData: "userId", className: 'disableEditInline', sWidth: '10%',
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(productTeamResources_editor, 'userId', full.userId);						
					 }else if(type == "display"){
						data = full.loginId;
					 }	           	 
					 return data;
				 },
	        },
           //{ mData: "userDefaultRoleName",className: 'disableEditInline', sWidth: '20%' },           		            
		   { mData: "userDefaultRoleName", className: 'editable', sWidth: '10%',editField: "userId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(productTeamResources_editor, 'userId', full.userId);						
					 }else if(type == "display"){
						data = full.userDefaultRoleName;
					 }	           	 
					 return data;
				 },
	        },
		   { mData: "productSpecificUserRoleName", className: 'editable', sWidth: '10%',editField: "productSpecificUserRoleId",
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(productTeamResources_editor, 'productSpecificUserRoleId', full.productSpecificUserRoleId);						
					 }else if(type == "display"){
						data = full.productSpecificUserRoleName;
					 }	           	 
					 return data;
				 },
	        },
		   { mData: "fromDate", className: 'editable', sWidth: '15%',
				mRender: function (data, type, full) {
					 if (full.action == "create"){						
						 data = convertDTDateFormatAdd(data, ["fromDate"]);						
					 }else if(type == "display"){
						data = full.fromDate;
					 }	           	 
					 return data;
				 }
			},
			{ mData: "toDate", className: 'editable', sWidth: '15%',
				mRender: function (data, type, full) {
					 if (full.action == "create"){						
						 data = convertDTDateFormatAdd(data, ["toDate"]);						
					 }else if(type == "display"){
						data = full.toDate;
					 }	           	 
					 return data;
				 }
			},
		   { mData: "percentageofallocation",className: 'editable', sWidth: '3%' },	   
		   { mData: "remarks",className: 'editable', sWidth: '20%' },
		   { mData: "status",
			sWidth: '3%',
              mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editor-active-productTeamResources">';
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
   					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
   						'<i class="fa fa-search-plus productTeamResourcesImg1" title="Audit History"></i></button>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						'<i class="fa fa-comments productTeamResourcesImg2" title="Comments"></i></button>'+						
     	       		'</div>');	      		
           		 return img;
            	}
            },
       ], 
		rowCallback: function ( row, data ) {
	           $('input.editor-active-productTeamResources', row).prop( 'checked', data.status == 1 );
	    },	   
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		 	 
		 $("#productTeamResources_dataTable_length").css('margin-top','8px');
		 $("#productTeamResources_dataTable_length").css('padding-left','35px');
		 
		  $('#productTeamResources_dataTable').on( 'change', 'input.editor-active-productTeamResources', function () {
			 productTeamResources_editor
					.edit( $(this).closest('tr'), false )
					.set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
					.submit();
			}); 
		 
		 // Activate an inline edit on click of a table cell
        $('#productTeamResources_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	productTeamResources_editor.inline( this, {
        		submitOnBlur: true
			}); 
		});
		
		productTeamResources_oTable.DataTable().columns().every( function () {
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
        
        productTeamResources_editor.dependent('userId',function ( val, data, callback ) {        	
			var entityId = 0;
			if(val != undefined){  
				
				if(!productTeamResourcesFlag){
					entityId = data.values['userId'];
					productTeamResourcesFlag=true;
				}else{
					entityId = val;
				}
				
				var url = 'testFactoryManagementControl.administration.user.roleList?userId='+entityId;
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
						
						if(productTeamResources_editor.s.action == "create"){
							productTeamResources_editor.set('productSpecificUserRoleId',json.Options);
							productTeamResources_editor.field('productSpecificUserRoleId').update(json.Options);
						}
					}
				} );
			}			
        });
	        
	        // ----- ended -----	
			
		// -----		
		$('#productTeamResources_dataTable tbody').on('click', 'td button .productTeamResourcesImg1', function () {
			var tr = $(this).closest('tr');
			var row = productTeamResources_oTable.DataTable().row(tr);

			listGenericAuditHistory(row.data().productTeamResourceId,"ProductTeamResources","productTeamResourcesAudit");			
		});			
		// ----- Comments -----		
		 
		 $('#productTeamResources_dataTable tbody').on('click', 'td button .productTeamResourcesImg2', function () {
			var tr = $(this).closest('tr');
			var row = productTeamResources_oTable.DataTable().row(tr);			    				
					
			var entityTypeIdComments = 56;
			var entityNameComments = "ProductTeamResources";
			listComments(entityTypeIdComments, entityNameComments, row.data().productTeamResourceId, row.data().loginId, "awpLevelCRComments");
			    
		 }); 
		 
		 productTeamResources_editor.on( 'preSubmit', function ( e, o, action ) {
		        if ( action !== 'remove' ) {
		        	var validationArr = ['fromDate','toDate','percentageofallocation'];
		        	var inputText;
		        	for(var i=0;i<validationArr.length;i++){
			            inputText = this.field(validationArr[i]);
			            if ( ! inputText.isMultiValue() ) {
			                if ( inputText.val() ) {
		                	}else{
			                	if(validationArr[i] == "fromDate"){
			                		inputText.error( 'Please enter From Date' );
								}if(validationArr[i] == "toDate"){
			                		inputText.error( 'Please enter To Date' );
								}if(validationArr[i] == "percentageofallocation"){
			                		inputText.error( 'Please enter Percentage Allocation' );
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
	});
}
 
 // -----
