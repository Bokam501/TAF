var optionsCustomerUserArr=[];
var optionsCustomerUserResultArr=[];
var optionsCustomerUserItemCounter=0;
var optionType_userType="regularList";
var optionType_CustomeruserType="regularCustomerList";

function customerUserList_Container(data, scrollYValue){
	optionsCustomerUserItemCounter=0;
	optionsCustomerUserResultArr=[];
	optionsCustomerUserArr = [
	              {id:"userTypeId", type:optionType_userType, url:'administration.product.user.userType?typeFilter=2'},
	              {id:"customerId", type:optionType_userType, url:'administration.customer.option.list'},
	              {id:"userRoleLabel", type:optionType_userType, url:'administration.product.user.listUserRole?typeFilter=2'},
	              {id:"languageId", type:optionType_userType, url:'common.list.languages'},
                  {id:"authenticationTypeId", type:optionType_userType, url:'common.list.user.authentication.types'}
	 ];
	returnOptionsCustomerUserItem(optionsCustomerUserArr[0].url, scrollYValue, data, "");
}

function customerUserCustomerResults_Container(data, row, tr){
	optionsCustomerUserItemCounter=0;
	optionsCustomerUserResultArr=[];
	optionsCustomerUserArr = [{id:"customerId", type:optionType_CustomeruserType, url:'administration.customer.option.list'},	               
	              ];
	returnOptionsCustomerUserItem(optionsCustomerUserArr[0].url, row, data, tr);	
}

function returnOptionsCustomerUserItem(url, scrollYValue, data, tr){
	$.ajax( {
 	   "type": "POST",
        "url":  url,
        "dataType": "json",
         success: function (json) {
	         if(json.Result == "Error" || json.Options == null){
	         	callAlert(json.Message);
	         	json.Options=[];
	         	optionsCustomerUserResultArr.push(json.Options);         	
	         	if(optionsCustomerUserArr[0].type == optionType_userType){	     			   
     			  listCustomerUserByTypeDT(data, scrollYValue);
     		   }else if(optionsCustomerUserArr[0].type == optionType_CustomeruserType){
     			  customerUserListChildTable_Container(data, scrollYValue);
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
	     	   optionsCustomerUserResultArr.push(json.Options);
	     	   
	     	   if(optionsCustomerUserItemCounter<optionsCustomerUserArr.length-1){
	     		  if(optionsCustomerUserArr[0].type == optionType_userType){
	     			 optionsCustomerUserArr[optionsCustomerUserResultArr.length].url = optionsCustomerUserArr[optionsCustomerUserResultArr.length].url;
	     		  
	     		  }else if(optionsCustomerUserArr[0].type == optionType_CustomeruserType){
	     			 optionsCustomerUserArr[optionsCustomerUserResultArr.length].url = optionsCustomerUserArr[optionsCustomerUserResultArr.length].url;
	     		  }
	     		 returnOptionsCustomerUserItem(optionsCustomerUserArr[optionsCustomerUserResultArr.length].url, scrollYValue, data, tr);     		  
	     	   }else{
	     		   if(optionsCustomerUserArr[0].type == optionType_userType){	     			   
	     			  listCustomerUserByTypeDT(data, scrollYValue);
	     		   }else if(optionsCustomerUserArr[0].type == optionType_CustomeruserType){
	     			  customerUserListChildTable_Container(data, scrollYValue);
	     		   }  
	     	   }
	     	   optionsCustomerUserItemCounter++;     	   
	         }
         },error: function (data) {
        	 optionsCustomerUserItemCounter++;
        	 
         },complete: function(data){
         	//console.log('Completed');         	
         },	            
   	});	
}

var customerUserListRegular_oTable='';
var userListCustomerChildTable_oTable='';
var clearTimeoutDTuserListRegular=0;
var clearTimeoutDTuserListRegularCustomer=0;
var cutomerUserlist_editor='';
var userCutomer_editor='';
var customerUserListFlag=false;
var customerUserStatus='';

function reInitializeDTCustomerUserListRegular(){
	clearTimeoutDTuserListRegular = setTimeout(function(){				
		customerUserListRegular_oTable.DataTable().columns.adjust().draw();
	clearTimeout(clearTimeoutDTuserListRegular);
	},500);
}

function reInitializeCustomerUserCutomerList(){
	clearTimeoutDTuserListRegularCustomer = setTimeout(function(){				
		userListCustomerChildTable_oTable.DataTable().columns.adjust().draw();
	clearTimeout(clearTimeoutDTuserListRegularCustomer);
	},500);
}

function customerUserListDTFullScreenHandler(flag){
	if(flag){
		reInitializeDTCustomerUserListRegular();
		$("#customerUsersContainerDT_wrapper .dataTables_scrollBody").css('max-height','185px');
	}else{
		reInitializeDTCustomerUserListRegular();
		$("#customerUsersContainerDT_wrapper .dataTables_scrollBody").css('max-height','450px');
	}
}

function updateCustomerUserList(url,tableValue, row, tr){
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
				if(!customerUserListFlag){
					customerUserList_Container(data, "185px");
				}else{				
					reloadDataTableHandler(data, customerUserListRegular_oTable);
				}					
				
			}else if(tableValue == "childTable1"){
				data = convertDTDateFormat(data, ["validFromDate"]);
				data = convertDTDateFormat(data, ["validTillDate"]);
				customerUserCustomerResults_Container(data, row, tr);
				
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

function customerUserListEditor(){
	
	cutomerUserlist_editor = new $.fn.dataTable.Editor( {
		"table": "#customerUsersContainerDT",
    	ajax: "administration.user.add?resourcesType=2",
    	ajaxUrl: "administration.user.update",
    	idSrc:  "userId",
    	i18n: {
    	        create: {
    	            title:  "Create a new User",
    	            submit: "Create",
    	        }
    	    },
    		fields: [{
                label: "First Name *",
                name: "firstName",
            },{
                label: "Middle Name",
                name: "middleName",                
            },{
                label: "Last Name *",
                name: "lastName",
            },{
                label: "Login ID *",
                name: "loginId",
            },{
                label: "Password",
                name: "userPassword",
                "type" : "password",
            },{
                label: "Display Name",
                name: "userDisplayName",
            }, {
                label: "User Code *",
                name: "userCode",
            },{
                label: "Email ID *",
                name: "emailId",  
            },{
                label: "Contact Number",
                name: "contactNumber",
            },{
                label: "User Type",
                name: "userTypeId",
                options: optionsCustomerUserResultArr[0],
                "type": "select",			
            },{
                label: "User Role",
                name: "userRoleLabel",
                options: optionsCustomerUserResultArr[2],
                "type": "select",	
            },{
                label: "Customer",
                name: "customerId",
                options: optionsCustomerUserResultArr[1],
                "type": "select",	
            },{
                label: "Languages",
                name: "languageId",
                options: optionsCustomerUserResultArr[3],
                "type": "select",			   
            },{    	
                label: "Authentication Type",
                name: "authenticationTypeId",
                options: optionsCustomerUserResultArr[4],
                "type": "select",	
            },{
                label: "skillName",
                name: "skillName",
                "type": "hidden",
            },{
                label: "Authentication Mode",
                name: "authenticationModeId",
                "type": "hidden",
                "default": 1,
            },{
                label: "status",
                name: "status",
                "type": "hidden",
            },
        ]
    });
}

var editorInStanceCustomerUserListDT='';

function listCustomerUserByTypeDT(data, scrollYValue){	
	customerUserListEditor();	
		
	customerUserListRegular_oTable = $('#customerUsersContainerDT').dataTable({	 
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
        /*fixedColumns: {
            leftColumns: 1,
            rightColumns: 1,
        },*/
        "fnInitComplete": function(data) {
     	   var searchcolumnVisibleIndex = [10, 11,15]; // search column TextBox Invisible Column position
 		   
     	   if(!customerUserListFlag){
     		  $('#customerUsersContainerDT_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
     	   customerUserListFlag=true;
           reInitializeDTCustomerUserListRegular();
 	   }, 
		buttons: [
		         { extend: "create", editor: cutomerUserlist_editor },
		         {
		          extend: "collection",	 
		          text: 'Export',
	              buttons: [
		          {
                    	extend: 'excel',
                    	title: 'User',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'csv',
                    	title: 'User',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'pdf',
                    	title: 'User',
                    	exportOptions: {
                            columns: ':visible',	                            
                        },
                        orientation: 'landscape',
                        pageSize: 'A2',
                        footer: true                    
                    },	                    
                   ],
		         },
		         'colvis',
		         {					
					text: '<i class="fa fa-upload showHandCursor trigUploadCustomerList" title="Upload Customer List"></i>',
					action: function ( e, dt, node, config ) {						
						//importUsersList('FTE');
						triggerCustomerUserListUpload();
					}
				},				
               ],			        
        aaData:data,		    				 
	    aoColumns: [	    
	            { mData: "firstName", className: 'editable', sWidth: '5%'}, 
	            { mData: "middleName", className: 'editable', sWidth: '5%'},
	            { mData: "lastName", className: 'editable', sWidth: '5%'},
	            { mData: "loginId", className: 'editable', sWidth: '5%'},	            
                { mData: "userDisplayName", className: 'editable', sWidth: '5%'},	                
                { mData: "userCode", className: 'editable', sWidth: '5%'},		                
                { mData: "emailId", className: 'editable', sWidth: '5%'},      
                { mData: "contactNumber", className: 'editable', sWidth: '5%'},	
                { mData: "userTypeLabel", className: 'editable', sWidth: '5%', editField: "userTypeId",
                	mRender: function (data, type, full) {
			           	 if (full.action == "create" || full.action == "edit"){
			           		data = optionsValueHandler(cutomerUserlist_editor, 'userTypeId', full.userTypeId);
			           	 }
			           	 else if(type == "display"){
			           		data = full.userTypeLabel;
			           	 }	           	 
			             return data;
		             },
                },
				{ mData: "customerName", className: 'editable', sWidth: '5%', editField: "customerId",
                	mRender: function (data, type, full) {
			           	 if (full.action == "create" || full.action == "edit"){
			           		data = optionsValueHandler(cutomerUserlist_editor, 'customerId', full.customerId);
			           	 }
			           	 else if(type == "display"){
			           		data = full.customerName;
			           	 }	           	 
			             return data;
		             },
                },
                { mData: "userRoleLabel", className: 'editable', sWidth: '5%', editField: "userRoleLabel",
                	mRender: function (data, type, full) {
			           	 if (full.action == "create" || full.action == "edit"){
			           		//data = optionsValueHandler(cutomerUserlist_editor, 'userRoleId', full.userRoleId);
			           		data = full.userRoleLabel;
			           	 }
			           	 else if(type == "display"){
			           		data = full.userRoleLabel;
			           	 }	           	 
			             return data;
		             },               	
                },	
                { mData: "languageName", className: 'editable', sWidth: '5%', editField: "languageId",
                	mRender: function (data, type, full) {
			           	 if (full.action == "create" || full.action == "edit"){
			           		data = optionsValueHandler(cutomerUserlist_editor, 'languageId', full.languageId);
			           	 }
			           	 else if(type == "display"){
			           		data = full.languageName;
			           	 }	           	 
			             return data;
		             },               	
                },                
                { mData: null,
                	mRender: function (data, type, full) {
  	            	  if ( type === 'display' ) {
  	                        return '<input type="checkbox" class="editor-active">';
  	                  }
  	                    return data;
  	                },
  	                className: "dt-body-center"
                },
                { mData: "skillName", className: 'disableEditInline', sWidth: '5%'},	
                { mData: "authenticationTypeName", className: 'editable', sWidth: '5%', editField: "authenticationTypeId",
                	mRender: function (data, type, full) {
			           	 if (full.action == "create" || full.action == "edit"){
			           		data = optionsValueHandler(cutomerUserlist_editor, 'authenticationTypeId', full.authenticationTypeId);			           		
			           	 }
			           	 else if(type == "display"){
			           		data = full.authenticationTypeName;
			           	 }	           	 
			             return data;
		             },
                },	
                { mData: "authenticationModeId", className: 'disableEditInline', sWidth: '5%',                	
                	mRender: function (data, type, full) {
                		if(full.authenticationModeName == null)
		           			full.authenticationModeName='';
		           		data = full.authenticationModeName;		           		
			            return data;
		             },
                },
                { mData: null,				 
	            	bSortable: false,
	            	mRender: function(data, type, full) {				            	
	           		 var img = ('<div style="display: flex;">'+
         	       		'<button style="border: none; background-color: transparent; outline: none;">'+
         	       				'<img src="css/images/reset-icon.png" class="details-control imgCustomer1" title="Reset Password" style="margin-left: 5px;"></button>'+
         	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
         	       				'<img src="css/images/list_metro.png" class="details-control imgCustomer2" title="Add/Edit User Customer Account"></button>'+
     	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
     	       					'<i class="fa fa-search-plus imgCustomer3" title="Audit History"></i></button>'+ 
     	       			'<button style="border: none; background-color: transparent; outline: none;">'+
   								'<i class="fa fa-trash-o details-control " onClick="displayUserDeleteNotification('+data.userId+')" title="Delete User" title="Delete User" style="padding-left: 0px;"></i></button>'+
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
	 	
	 $(function(){ // this will be called when the DOM is ready		
		 
		 $('#customerUsersContainerDT tbody').on( 'click', 'td', function (e) {
			 editorInStanceCustomerUserListDT = customerUserListRegular_oTable.DataTable().cell(this);
	        } );
		 
		 cutomerUserlist_editor	    	
	    	.on("submitComplete", function( e ) {
	    		usertypeId = 4;	 
				customerUserListFlag=false;	
	    		urlToListUserDetails = 'administration.user.list.by.options.status?userTypeId='+usertypeId+"&resourcePoolId="+rpId+"&testFactoryLabId="+tflId+"&timeStamp="+timestamp+"&statusID="+id;	  
	    		updateCustomerUserList(urlToListUserDetails+'&jtStartIndex=0&jtPageSize=10000', "parentTable", "", "");	    		
	    	});
		  		 
    	$("#customerUsersContainerDT_length").css('margin-top','8px');
		$("#customerUsersContainerDT_length").css('padding-left','35px');			
		
		 $('#customerUsersContainerDT').on( 'change', 'input.editor-active', function () {
			 customerUserStatus = $(this); 
			 
			 var tr = $(this).closest('tr');
			 var row = customerUserListRegular_oTable.DataTable().row(tr);  
			 		 
			 displayStatusChangeNotification(row.data().userId,row.data().status);						 			 
		});
		
		customerUserListRegular_oTable.DataTable().columns().every( function () {
				var that = this;
				$('input', this.footer() ).on( 'keyup change', function () {
					if ( that.search() !== this.value ) {
						that
							.search( this.value, true, false )
							.draw();
					}
				} );
		} );		
	 });
        	
	 $('#customerUsersContainerDT tbody').on('click', 'td button .imgCustomer1', function () {
    	var tr = $(this).closest('tr');
    	var row = customerUserListRegular_oTable.DataTable().row(tr);    	
    	resetUserList(row.data().userId);
	});	 
	 	 
	 // ----- User Customer Account -----
	 $('#customerUsersContainerDT tbody').on('click', 'td button .imgCustomer2', function () {
	    	var tr = $(this).closest('tr');
	    	var row = customerUserListRegular_oTable.DataTable().row(tr);
	    	    	
	    	userCustomerAccounttHandler(row.data().userId, row, tr);
	    	$("#customerUserCustomerContainer").modal();
		});
	 
	 $('#customerUsersContainerDT tbody').on('click', 'td button .imgCustomer3', function () {
	    	var tr = $(this).closest('tr');
	    	var row = customerUserListRegular_oTable.DataTable().row(tr);	    	
	    	listGenericAuditHistory(row.data().userId,"User","customerUserAudit");	    	
		});
	 
	// Activate an inline edit on click of a table cell
     $('#customerUsersContainerDT').on( 'click', 'tbody td.editable', function (e) {
    	 cutomerUserlist_editor.inline( this, {
             submitOnBlur: true
         } );
     } ); 
     
     cutomerUserlist_editor.on( 'preSubmit', function ( e, o, action ) {
         if ( action !== 'remove' ) {
         	var validationArr = ['firstName','lastName','loginId','userCode','emailId'];
         	var str;
         	for(var i=0;i<validationArr.length;i++){
 	            str = this.field(validationArr[i]);
 	            if ( ! str.isMultiValue() ) {
 	                if ( str.val() ) {
                 	}else{
 	                	if(validationArr[i] == "firstName")
 	                		str.error("Please enter First Name");
 	                	if(validationArr[i] == "lastName")
 	                		str.error("Please enter Last Name");
 	                	if(validationArr[i] == "loginId")
 	                		str.error("Please enter Login ID");
 	                	if(validationArr[i] == "userCode")
 	                		str.error("Please enter User Code");
 	                	if(validationArr[i] == "emailId")
 	                		str.error("Please enter Email ID");
                 	}
 	            }
         	}

             // If any error was reported, cancel the submission so it can be corrected
             if ( this.inError() ) {
                 return false;
             }
         }
     } );
	
}

// ----- regular user list child table -----

function customerUserListCustomerEditor(row){
	userCutomer_editor = new $.fn.dataTable.Editor( {
	    "table": "#customerUserChild_dataTable",
		ajax: 'resourceManagement.user.customerAccount.add',
		ajaxUrl: "resourceManagement.user.customerAccount.update",
		"idSrc":  "userCustomerAccountId",
		i18n: {
	        create: {
	            title:  "Create a new Cutomer ",
	            submit: "Create",
	        }
	    },
		fields: [ {
	        label: "userId",
	        name:  "userId",
	        "type": "hidden",
	        "default": row.data().userId,
	    },{
	        label: "userCustomerAccountId",
	        name:  "userCustomerAccountId",
	        "type": "hidden",	        
	    },{
            label: "Customer",
            name: "customerId",
            options: optionsCustomerUserResultArr[0],
            "type": "select",			
        },{
	        label: "Customer User Code",
	        name: "userCustomerCode",
	    },{
	        label: "Email Id",
	        name: "userCustomerEmailId",			                
	    }, {
	        label: "From Date",
	        name: "validFromDate",
	        type:  'datetime',
	        def:    function () { return new Date(); },
	        format: 'M/D/YYYY',
	    }, {
	        label: "To Date",
	        name: "validTillDate",
	        type:  'datetime',
	        def:    function () { return new Date(); },
	        format: 'M/D/YYYY',
	    }
	]
	});
	
	$( 'input', userCutomer_editor.node()).on( 'focus', function () {
		this.select();
	});
	
	// ----- ended -----
}

function customerUserListChildTable_Container(data, row){
	
	try{
		if ($("#customerUserCustomerDTContainer").children().length>0) {
			$("#customerUserCustomerDTContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = customerUserChildTable(); 			 
	$("#customerUserCustomerDTContainer").append(childDivString);
	
	customerUserListCustomerEditor(row);
			
	userListCustomerChildTable_oTable = $("#customerUserChild_dataTable").dataTable( {
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
	    	   
	    	   var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
		     	  var headerItems = $('#customerUserChild_dataTable_wrapper tfoot tr th');
		     	  headerItems.each( function () {   
		   	    	    var i=$(this).index();
		   	    	    var flag=false;
		   	    	    var singleItem = $(headerItems).eq(i).find('div'); 
		   	    	    for(var j=0; j < searchcolumnVisibleIndex.length; j++){
		   	    	    	if(i == searchcolumnVisibleIndex[j]){
		   	    	    		flag=true;
		   	    	    		$(singleItem).css('height','0px');
		    	    	    	$(singleItem).css('color','#4E5C69');
		    	    	    	$(singleItem).css('line-height','12px');
		   	    	    		break;
		   	    	    	}else{
		   	    	    		$(singleItem).css('height','0px');
		    	    	    	$(singleItem).css('color','#4E5C69');
		    	    	    	$(singleItem).css('line-height','12px');
		   	    	    	}
		   	    	    }
		   	    	    
		   	    	    if(searchcolumnVisibleIndex.length==0){
		   	    	    	$(singleItem).css('height','0px');
		     	    		$(singleItem).css('color','#4E5C69');
		     	    		$(singleItem).css('line-height','12px');
		   	    	    }
		   	    	    
		   	    	    if(!flag){
		   	    	    	$(this).prepend( '<input type="text" name="'+data.aoColumns[i].mData+'" value="" style="width:100%" class="search_init" />');
		   	    	    }
		     	   });	 	  
		    	   reInitializeCustomerUserCutomerList();
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: userCutomer_editor },	  
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
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "customerId", className: 'editable', sWidth: '5%', editField: "customerId",
            	mRender: function (data, type, full) {
		           	 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(userCutomer_editor, 'customerId', full.customerId);
		           	 }
		           	 else if(type == "display"){
		           		data = full.customerName;
		           	 }	           	 
		             return data;
	             },
            },		           		
           { mData: "userCustomerCode",className: 'editable', sWidth: '20%' },		
           { mData: "userCustomerEmailId",className: 'editable', sWidth: '12%' },          
           { mData: "validFromDate", className: 'editable', sWidth: '15%',
           		mRender: function (data, type, full) {
		           	 if (full.action == "create"){
		           		data = convertDTDateFormatAdd(data, ["validFromDate"]);
		           		//productVersionResultHandler(full.productId, productSelectedRow, productSelectedTr);
		           	 }else if(type == "display"){
		           		data = full.validFromDate;
		           	 }	           	 
		             return data;
	             }
           },
           { mData: "validTillDate", className: 'editable', sWidth: '15%',
          		mRender: function (data, type, full) {
		           	 if (full.action == "create"){
		           		data = convertDTDateFormatAdd(data, ["validTillDate"]);
		           		//productVersionResultHandler(full.productId, productSelectedRow, productSelectedTr);
		           	 }else if(type == "display"){
		           		data = full.validTillDate;
		           	 }	           	 
		             return data;
	             }
          },           
       ],       
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	// ------
	 $(function(){ // this will be called when the DOM is ready
		 
		$("#customerUserChild_dataTable_length").css('margin-top','8px');
		$("#customerUserChild_dataTable_length").css('padding-left','35px');
		
		$('#customerUserChild_dataTable').DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });
		
		 $('#customerUserChild_dataTable').on( 'click', 'tbody td.editable', function (e) {
			userCutomer_editor.inline( this, {
				submitOnBlur: true
			} );
		});	
	
	 });
}

// --- Regular user child table Started -----

function customerUserChildTable(){
	var childDivString = '<table id="customerUserChild_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead>'+
			'<tr>'+
				'<th class="dataTableChildHeaderTitleTH">Customer</th>'+
				'<th class="dataTableChildHeaderTitleTH">Customer Code</th>'+				
				'<th class="dataTableChildHeaderTitleTH">Email Id</th>'+
				'<th class="dataTableChildHeaderTitleTH">From Date</th>'+
				'<th class="dataTableChildHeaderTitleTH">To Date</th>'+
			'</tr>'+
		'</thead>'+
		'<tfoot>'+
			'<tr><th></th><th></th><th></th><th></th><th></th></tr>'+			
		'</tfoot>'+
	'</table>';		
	
	return childDivString;	
}
 // -----

function userCustomerAccounttHandler(userId, row, tr){
	var url = 'resourceManagement.user.customerAccount.list?userId='+userId+'&jtStartIndex=0&jtPageSize=10000';
	updateCustomerUserList(url, "childTable1", row, tr);	
}