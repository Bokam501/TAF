
var optionsRegularUserArr=[];
var optionsRegularUserResultArr=[];
var optionsRegularUserItemCounter=0;
var optionType_userType="regularList";
var optionType_CustomeruserType="regularCustomerList";

function userList_Container(data, scrollYValue){
	optionsRegularUserItemCounter=0;
	optionsRegularUserResultArr=[];
	optionsRegularUserArr = [
	              {id:"userTypeId", type:optionType_userType, url:'administration.user.userType'},
	              {id:"userRoleLabel", type:optionType_userType, url:'administration.product.user.listUserRole?typeFilter=1'},
	              {id:"languageId", type:optionType_userType, url:'common.list.languages'},
                  {id:"authenticationTypeId", type:optionType_userType, url:'common.list.user.authentication.types'}
	 ];
	returnOptionsUserItem(optionsRegularUserArr[0].url, scrollYValue, data, "");
}

function regularUserCustomerResults_Container(data, row, tr){
	optionsRegularUserItemCounter=0;
	optionsRegularUserResultArr=[];
	optionsRegularUserArr = [{id:"customerId", type:optionType_CustomeruserType, url:'administration.customer.option.list'},	               
	              ];
	returnOptionsUserItem(optionsRegularUserArr[0].url, row, data, tr);	
}

function returnOptionsUserItem(url, scrollYValue, data, tr){
	$.ajax( {
 	   "type": "POST",
        "url":  url,
        "dataType": "json",
         success: function (json) {
	         if(json.Result == "Error" || json.Options == null){
	         	callAlert(json.Message);
	         	json.Options=[];
	         	optionsRegularUserResultArr.push(json.Options);         	
	         	if(optionsRegularUserArr[0].type == optionType_userType){	     			   
     			  listUserByTypeDT(data, scrollYValue);
     		   }else if(optionsRegularUserArr[0].type == optionType_CustomeruserType){
     			  regularUserListChildTable_Container(data, scrollYValue);
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
	     	   optionsRegularUserResultArr.push(json.Options);
	     	   
	     	   if(optionsRegularUserItemCounter<optionsRegularUserArr.length-1){
	     		  if(optionsRegularUserArr[0].type == optionType_userType){
	     			 optionsRegularUserArr[optionsRegularUserResultArr.length].url = optionsRegularUserArr[optionsRegularUserResultArr.length].url;
	     		  
	     		  }else if(optionsRegularUserArr[0].type == optionType_CustomeruserType){
	     			 optionsRegularUserArr[optionsRegularUserResultArr.length].url = optionsRegularUserArr[optionsRegularUserResultArr.length].url;
	     		  }
	     		 returnOptionsUserItem(optionsRegularUserArr[optionsRegularUserResultArr.length].url, scrollYValue, data, tr);     		  
	     	   }else{
	     		   if(optionsRegularUserArr[0].type == optionType_userType){	     			   
	     			  listUserByTypeDT(data, scrollYValue);
	     		   }else if(optionsRegularUserArr[0].type == optionType_CustomeruserType){
	     			  regularUserListChildTable_Container(data, scrollYValue);
	     		   }  
	     	   }
	     	   optionsRegularUserItemCounter++;     	   
	         }
         },error: function (data) {
        	 optionsRegularUserItemCounter++;
        	 
         },complete: function(data){
         	//console.log('Completed');         	
         },	            
   	});	
}

var userListRegular_oTable='';
var userListRegularChildTable_oTable='';
var clearTimeoutDTuserListRegular=0;
var clearTimeoutDTuserListRegularCustomer=0;
var userlist_editor='';
var userlistCutomer_editor='';
var regularUserListFlag=false;
var userStatus='';

function reInitializeDTUserListRegular(){
	clearTimeoutDTuserListRegular = setTimeout(function(){				
		userListRegular_oTable.DataTable().columns.adjust().draw();
	clearTimeout(clearTimeoutDTuserListRegular);
	},500);
}

function reInitializeRegularUserCutomerList(){
	clearTimeoutDTuserListRegularCustomer = setTimeout(function(){				
		userListRegularChildTable_oTable.DataTable().columns.adjust().draw();
	clearTimeout(clearTimeoutDTuserListRegularCustomer);
	},500);
}

function fullScreenHandlerDTRegularUser(){
	var selectedTab = $("#tabslist>li.active").index();
	
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);
		$("#tabslist").show();
			
		if(selectedTab==0){
			regularUserListDTFullScreenHandler(true);		
		}else{
			customerUserListDTFullScreenHandler(true);
		}
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');						
		$("#tabslist").hide();
		
		if(selectedTab==0){
			regularUserListDTFullScreenHandler(false);
		}else{
			customerUserListDTFullScreenHandler(false);
		}
	}
}

function regularUserListDTFullScreenHandler(flag){
	if(flag){
		reInitializeDTUserListRegular();
		$("#regularUsersContainerDT_wrapper .dataTables_scrollBody").css('max-height','185px');
	}else{
		reInitializeDTUserListRegular();
		$("#regularUsersContainerDT_wrapper .dataTables_scrollBody").css('max-height','450px');
	}
}

function updateUserList(url,tableValue, row, tr){
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			
			if(data.Result=="ERROR" || data.Result == "INFORMATION"){
      		    data = [];		
      		    callAlert(data.Message);
			}else{
				data = data.Records;
			}
			
			if(tableValue == "parentTable"){
				if(!regularUserListFlag){
					userList_Container(data, "185px");
				}else{				
					reloadDataTableHandler(data, userListRegular_oTable);
				}			
				
			}else if(tableValue == "childTable1"){
				data = convertDTDateFormat(data, ["validFromDate"]);
				data = convertDTDateFormat(data, ["validTillDate"]);
				regularUserCustomerResults_Container(data, row, tr);
				
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

function userListEditor(){
	
	userlist_editor = new $.fn.dataTable.Editor( {
		"table": "#regularUsersContainerDT",
    	ajax: "administration.user.add?resourcesType=1",
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
                options: optionsRegularUserResultArr[0],
                "type": "select",			
            },{
                label: "User Role",
                name: "userRoleLabel",
                options: optionsRegularUserResultArr[1],
                "type": "select",	
            },{
                label: "Languages",
                name: "languageId",
                options: optionsRegularUserResultArr[2],
                "type": "select",			   
            },{    	
                label: "Authentication Type",
                name: "authenticationTypeId",
                options: optionsRegularUserResultArr[3],
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

var editorInStanceUserListDT='';

function listUserByTypeDT(data, scrollYValue){	
	userListEditor();	
		
	userListRegular_oTable = $('#regularUsersContainerDT').dataTable({	 
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
 		   
     	   if(!regularUserListFlag){
     		  $('#regularUsersContainerDT_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
     	   regularUserListFlag=true;
           reInitializeDTUserListRegular();
 	   }, 
		buttons: [
		         { extend: "create", editor: userlist_editor },
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
					text: '<i class="fa fa-upload showHandCursor trigUploadUserList" title="Upload User List"></i>',
					action: function ( e, dt, node, config ) {						
						//importUsersList('FTE');
						triggerUserListUpload();
					}
				},
				{					
					text: '<i class="fa fa-download showHandCursor downloadUserListTemplate" title="Download User Template"></i>',
					action: function ( e, dt, node, config ) {					
						downloadTemplateUserList();
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
			           		data = optionsValueHandler(userlist_editor, 'userTypeId', full.userTypeId);
			           	 }
			           	 else if(type == "display"){
			           		data = full.userTypeLabel;
			           	 }	           	 
			             return data;
		             },
                },
                { mData: "userRoleLabel", className: 'editable', sWidth: '5%', editField: "userRoleLabel",
                	mRender: function (data, type, full) {
			           	 if (full.action == "create" || full.action == "edit"){
			           		//data = optionsValueHandler(userlist_editor, 'userRoleId', full.userRoleId);
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
			           		data = optionsValueHandler(userlist_editor, 'languageId', full.languageId);
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
			           		data = optionsValueHandler(userlist_editor, 'authenticationTypeId', full.authenticationTypeId);			           		
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
         	       				'<img src="css/images/reset-icon.png" class="details-control img1" title="Reset Password" style="margin-left: 5px;"></button>'+
         	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
         	       				'<img src="css/images/list_metro.png" class="details-control img2" title="Add/Edit User Customer Account"></button>'+
     	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
     	       					'<i class="fa fa-search-plus img3" title="Audit History"></i></button>'+ 
     	       			'<button style="border: none; background-color: transparent; outline: none;">'+
   								'<i class="fa fa-trash-o details-control img4" onClick="displayUserDeleteNotification('+data.userId+')" title="Delete User" style="padding-left: 0px;"></i></button>'+
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
		 
		 $('#regularUsersContainerDT tbody').on( 'click', 'td', function (e) {
			 editorInStanceUserListDT = userListRegular_oTable.DataTable().cell(this);
	        } );
		 
		 userlist_editor	    	
	    	.on("submitComplete", function( e ) {
	    		usertypeId = 0;	    		
				regularUserListFlag=false;				
	    		urlToListUserDetails = 'administration.user.list.by.options.status?userTypeId='+usertypeId+"&resourcePoolId="+rpId+"&testFactoryLabId="+tflId+"&timeStamp="+timestamp+"&statusID="+id;	  
	    		updateUserList(urlToListUserDetails+'&jtStartIndex=0&jtPageSize=10000', "parentTable", "", "");	    		
	    	});
		  		 
    	$("#regularUsersContainerDT_length").css('margin-top','8px');
		$("#regularUsersContainerDT_length").css('padding-left','35px');		
		//$(".select2-drop").css('z-index','100000');				
		
		 $('#regularUsersContainerDT').on( 'change', 'input.editor-active', function () {
			 userStatus = $(this); 
			 
			 var tr = $(this).closest('tr');
			 var row = userListRegular_oTable.DataTable().row(tr);  
			 		 
			 displayStatusChangeNotification(row.data().userId,row.data().status);		 
			 			 			 
		    });
		
		userListRegular_oTable.DataTable().columns().every( function () {
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
        	
	 $('#regularUsersContainerDT tbody').on('click', 'td button .img1', function () {
    	var tr = $(this).closest('tr');
    	var row = userListRegular_oTable.DataTable().row(tr);    	
    	resetUserList(row.data().userId);
	});	 
	 	 
	 // ----- User Customer Account -----
	 $('#regularUsersContainerDT tbody').on('click', 'td button .img2', function () {
	    	var tr = $(this).closest('tr');
	    	var row = userListRegular_oTable.DataTable().row(tr);
	    	    	
	    	userListAccounttHandler(row.data().userId, row, tr);
	    	$("#regularUserCustomerContainer").modal();
		});
	 
	 $('#regularUsersContainerDT tbody').on('click', 'td button .img3', function () {
	    	var tr = $(this).closest('tr');
	    	var row = userListRegular_oTable.DataTable().row(tr);
	    	
	    	listGenericAuditHistory(row.data().userId,"User","regularUserAudit");	    	
		});
	 
	// Activate an inline edit on click of a table cell
     $('#regularUsersContainerDT').on( 'click', 'tbody td.editable', function (e) {
    	 userlist_editor.inline( this, {
             submitOnBlur: true
         } );
     } ); 
     
     userlist_editor.on( 'preSubmit', function ( e, o, action ) {
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

function regularUserListCustomerEditor(row){
	userlistCutomer_editor = new $.fn.dataTable.Editor( {
	    "table": "#regularUserChild_dataTable",
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
            options: optionsRegularUserResultArr[0],
            "type": "select",			
        },{
	        label: "Customer User Name",
	        name: "userCustomerName",
	    }, {
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
	
	$( 'input', userlistCutomer_editor.node()).on( 'focus', function () {
		this.select();
	});
	
	// ----- ended -----
}

function regularUserListChildTable_Container(data, row){
	
	try{
		if ($("#regularUserCustomerDTContainer").children().length>0) {
			$("#regularUserCustomerDTContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = regularUserChildTable(); 			 
	$("#regularUserCustomerDTContainer").append(childDivString);
	
	regularUserListCustomerEditor(row);
			
	userListRegularChildTable_oTable = $("#regularUserChild_dataTable").dataTable( {
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
		     	  var headerItems = $('#regularUserChild_dataTable_wrapper tfoot tr th');
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
		    	   reInitializeRegularUserCutomerList();
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: userlistCutomer_editor },	  
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
		           		data = optionsValueHandler(userlistCutomer_editor, 'customerId', full.customerId);
		           	 }
		           	 else if(type == "display"){
		           		data = full.customerName;
		           	 }	           	 
		             return data;
	             },
            },		
           { mData: "userCustomerName",className: 'editable', sWidth: '20%' },		
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
		 
		$("#regularUserChild_dataTable_length").css('margin-top','8px');
		$("#regularUserChild_dataTable_length").css('padding-left','35px');
		
		$('#regularUserChild_dataTable').DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });
		
		 $('#regularUserChild_dataTable').on( 'click', 'tbody td.editable', function (e) {
			 userlistCutomer_editor.inline( this, {
		            submitOnBlur: true
		        } );
		    });	
	
	 });
}

// --- Regular user child table Started -----

function regularUserChildTable(){
	var childDivString = '<table id="regularUserChild_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead>'+
			'<tr>'+
				'<th>Customer</th>'+
				'<th>Customer User Name</th>'+
				'<th>Customer User Code</th>'+
				'<th>Email Id</th>'+
				'<th>From Date</th>'+
				'<th>To Date</th>'+
			'</tr>'+
		'</thead>'+
		'<tfoot>'+
			'<tr><th></th><th></th><th></th><th></th><th></th><th></th></tr>'+			
		'</tfoot>'+
	'</table>';		
	
	return childDivString;	
}
 // -----

function userListAccounttHandler(userId, row, tr){
	var url = 'resourceManagement.user.customerAccount.list?userId='+userId+'&jtStartIndex=0&jtPageSize=10000';
	updateUserList(url, "childTable1", row, tr);	
}

var usersId;
var statusValue;
function displayStatusChangeNotification(userId,status){
	$('textarea#statusChangeInput').val("");
	$("#div_StatusChangeNotifications").modal();
	usersId = userId;
	statusValue=status;
	var statusChangeTitle="Status Change Comments";
	
	$("#div_StatusChangeNotifications .modal-header h4").text(statusChangeTitle);
	$("#notifyStatusChange").empty();
	$('#notifyStatusChange').show();
	
}

function submitStatusChangeNotificationHandler(){
	
	var statusMessageFromUI = $('textarea#statusChangeInput').val();
	var fieldName = "Status";
	var oldField = "";
	var newField = "";
	if(statusValue == 1) {
		oldField = "Active";
		newField = "InActive";
	}	else {
		oldField = "InActive";
		newField = "Active";
	}
	
	var selectedTab = $("#tabslist>li.active").index();
	
	if(statusMessageFromUI == "") {
		callAlert("Please give a reason for making user Active/Inactive");
		return;
	
	}else{				
		if(selectedTab==0){		
			userlist_editor
				.edit( userStatus.closest('tr'), false )
				.set( 'status', userStatus.prop( 'checked' ) ? 1 : 0 )
				.submit();
			
		}else{
			cutomerUserlist_editor
	 			.edit( customerUserStatus.closest('tr'), false )
	 			.set( 'status', customerUserStatus.prop( 'checked' ) ? 1 : 0 )
	 			.submit();
		}
	}
		
	openLoaderIcon();
	$.ajax({
		url : 'user.group.status.reason.audit?userId='+usersId+'&message='+statusMessageFromUI+'&fieldName='+fieldName+'&oldField='+oldField+'&newField='+newField,		
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data){			
			closeLoaderIcon();
		},
		error : function(data){
			closeLoaderIcon();  
		},
	});
	
	$("#div_StatusChangeNotifications").modal('hide');	
}

function popupCloseStatusChangeNotificationHandler(){
	$("#div_StatusChangeNotifications").modal('hide');
	console.log("isChecked : "+!userStatus.prop('checked'));	
	
	var selectedTab = $("#tabslist>li.active").index();	
	if(selectedTab==0){
		editorInStanceUserListDT.data(userStatus.prop( 'checked' ) ? 0 : 1).draw('page');
	}else{
		editorInStanceCustomerUserListDT.data(userStatus.prop( 'checked' ) ? 0 : 1).draw('page');
	}
}

function displayUserDeleteNotification(userId){
	$('textarea#userDeleteInput').val("");
	$("#div_UserDeleteNotifications").modal();
	usersId = userId;
	var userDeleteTitle="User Deletion Comments";
	
	$("#div_UserDeleteNotifications .modal-header h4").text(userDeleteTitle);
	$("#notifyUserDelete").empty();
	$('#notifyUserDelete').show();
	
}

function deleteUserListingItem(userId){
	var fd = new FormData();
	fd.append("userId", userId);
	
	openLoaderIcon();
	$.ajax({
		url : 'administration.user.delete',
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

function submitUserDeleteNotificationHandler(){
	var userDeleteMessage = $('textarea#userDeleteInput').val();
	if(userDeleteMessage == ""){
		//userDeleteMessage = "User is Deactivated without reason";
		callAlert("Please give a reason for making user Delete/Active");
		return;
	}
	var fieldName = "Delete";
	var oldField = "Active";
	var newField = "Delete";
	$.post('user.group.status.reason.audit?userId='+usersId+'&message='+userDeleteMessage+'&fieldName='+fieldName+'&oldField='+oldField+'&newField='+newField, function(data) {	
		console.log("success "+data.Message);
	});
	
	$("#div_UserDeleteNotifications").modal('hide');
	deleteUserListingItem(usersId);
}

function popupCloseUserDeleteNotificationHandler(){
	$("#div_UserDeleteNotifications").modal('hide');
}
