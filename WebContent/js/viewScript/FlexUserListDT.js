
var optionsRegularUserArr=[];
var optionsRegularUserResultArr=[];
var optionsRegularUserItemCounter=0;
var optionType_userType="regularList";
var optionType_CustomeruserType="regularCustomerList";
var optionType_ResourcePoolType ="resoucePoolMaping";
var resPoolId =0;
var userStatus='';

function userList_Container(data, scrollYValue){
	resPoolId = document.getElementById("hdnResPoolId").value;
	if(resPoolId=='')
		resPoolId=0;
	
	optionsRegularUserItemCounter=0;
	optionsRegularUserResultArr=[];
	optionsRegularUserArr = [
	              {id:"userTypeId", type:optionType_userType, url:'administration.user.userType'},
	              {id:"userRoleLabel", type:optionType_userType, url:'administration.user.listUserRole'},	             
	              {id:"languageId", type:optionType_userType, url:'common.list.languages'},
                  {id:"authenticationTypeId", type:optionType_userType, url:'common.list.user.authentication.types'},
	              {id:"resourcePoolId", type:optionType_userType, url:'common.list.resourcepool.list?resourcePoolId='+resPoolId},
	              {id:"vendorId", type:optionType_userType, url:'common.list.vendor.list'}
	              
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

function flexUserResourcePoolMapping_Container(data, row, tr){
	optionsRegularUserItemCounter=0;
	optionsRegularUserResultArr=[];
	optionsRegularUserArr = [{id:"resourcepoolId", type:optionType_ResourcePoolType, url:'common.list.resourcepool.list?resourcePoolId='+row.data().resourcePoolId},	               
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
     		   }else if(optionsRegularUserArr[0].type == optionType_ResourcePoolType){
     			   flexUserResourcePoolChildTable_Container(data,scrollYValue);
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
	     		  }else if(optionsRegularUserArr[0].type == optionType_ResourcePoolType){
	     			 optionsRegularUserArr[optionsRegularUserResultArr.length].url = optionsRegularUserArr[optionsRegularUserResultArr.length].url; 
	     		  }
	     		 returnOptionsUserItem(optionsRegularUserArr[optionsRegularUserResultArr.length].url, scrollYValue, data, tr);     		  
	     	   }else{
	     		   if(optionsRegularUserArr[0].type == optionType_userType){	     			   
	     			  listUserByTypeDT(data, scrollYValue);
	     		   }else if(optionsRegularUserArr[0].type == optionType_CustomeruserType){
	     			  regularUserListChildTable_Container(data, scrollYValue);
	     		   } else if(optionsRegularUserArr[0].type == optionType_ResourcePoolType){
	     			  flexUserListChildTable_Container(data, scrollYValue);
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

var userListFlex_oTable='';
var userListRegularChildTable_oTable='';
var flexUserListChildTable_oTable='';
var clearTimeoutDTuserListRegular=0;
var clearTimeoutDTuserListRegularCustomer=0;
var clearTimeoutDTuserListFlexCustomer=0;
var userlist_editor='';
var userlistCutomer_editor='';
var userlistChild2Cutomer_editor='';
var flexUserListFlag=false;

function reInitializeDTUserListRegular(){
	clearTimeoutDTuserListRegular = setTimeout(function(){				
		userListFlex_oTable.DataTable().columns.adjust().draw();
	clearTimeout(clearTimeoutDTuserListRegular);
	},500);
}

function reInitializeRegularUserCutomerList(){
	clearTimeoutDTuserListRegularCustomer = setTimeout(function(){				
		userListRegularChildTable_oTable.DataTable().columns.adjust().draw();
	clearTimeout(clearTimeoutDTuserListRegularCustomer);
	},500);
}

function reInitializeFlexUserCutomerList(){
	clearTimeoutDTuserListFlexCustomer = setTimeout(function(){				
		flexUserListChildTable_oTable.DataTable().columns.adjust().draw();
	clearTimeout(clearTimeoutDTuserListFlexCustomer);
	},500);
}

function flexUserListDTFullScreenHandler(flag){
	if(flag){
		reInitializeFlexUserCutomerList();
		$("#flexUsersContainerDT_wrapper .dataTables_scrollBody").css('max-height','220px');
	}else{
		reInitializeFlexUserCutomerList();
		$("#flexUsersContainerDT_wrapper .dataTables_scrollBody").css('max-height','450px');
	}
}

function fullScreenHandlerDTFlexUser(){
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);
		flexUserListDTFullScreenHandler(true);		
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');						
		flexUserListDTFullScreenHandler(false);
	}
}

/*function regularUserListDTFullScreenHandler(flag){
	if(flag){
		reInitializeDTUserListRegular();
		$("#flexUsersContainerDT_wrapper .dataTables_scrollBody").css('max-height','220px');
	}else{
		reInitializeDTUserListRegular();
		$("#flexUsersContainerDT_wrapper .dataTables_scrollBody").css('max-height','450px');
	}
}*/

function updateFlexUserList(url,tableValue, row, tr){
	openLoaderIcon();
	myArr=[];
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
			myArr.push(data);
			
			if(tableValue == "parentTable"){
				if(!flexUserListFlag){
					userList_Container(data, "220px");
				}else{				
					reloadDataTableHandler(data, userListFlex_oTable);
				}			
				
			}else if(tableValue == "childTable1"){
				data = convertDTDateFormat(data, ["validFromDate"]);
				data = convertDTDateFormat(data, ["validTillDate"]);
				regularUserCustomerResults_Container(data, row, tr);
				
			}else if(tableValue == "childTable2"){
				//data = convertDTDateFormat(data, ["fromDate"]);
				//data = convertDTDateFormat(data, ["toDate"]);
				flexUserResourcePoolMapping_Container(data, row, tr);
			}
			else{
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
		"table": "#flexUsersContainerDT",
    	ajax: "administration.user.add?resourcesType=0",
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
                label: "userId",
                name: "userId",
                "type": "hidden",
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
                label: "Resource Pool",
                name: "resourcePoolId",
                options: optionsRegularUserResultArr[4],
                "type": "select",	
            },{    	
                label: "Vendor",
                name: "vendorId",
                options: optionsRegularUserResultArr[5],
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

var editorInStanceFlexUserListDT='';

function listUserByTypeDT(data, scrollYValue){	
	userListEditor();	
		
	userListFlex_oTable = $('#flexUsersContainerDT').dataTable({	 
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
     	   var searchcolumnVisibleIndex = [13, 18]; // search column TextBox Invisible Column position
 		   
     	  if(!flexUserListFlag){
     		  $('#flexUsersContainerDT_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
     	   flexUserListFlag=true;
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
                        pageSize: 'A2'
                    
                    },	                    
                   ],
		         },
		         'colvis',
		         {					
					text: '<i class="fa fa-upload showHandCursor flexUserUpload" title="Upload User List"></i>',
					action: function ( e, dt, node, config ) {						
						//importUsersList('FTE');
						triggerUserListUpload();
					}
				},
				{					
					text: '<i class="fa fa-download showHandCursor downloadFlexUserTemplate" title="Download User Template"></i>',
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
					/*{ mData: "userRoleLabel", className: 'editable', sWidth: '5%', editField: "userRoleId",
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
					},*/
					
					{ mData: "userRoleLabel", className: 'disableEditInline', sWidth: '5%', editField: "userRoleLabel",
						mRender: function (data, type, full) {
							 var userId = full.userId;
							 var userRoleId = full.userRoleId;
							 return ('<a style="color:  #0000FF;" href=javascript:showUserRolesPopup('+userId+','+userRoleId+');>' + full.userRoleLabel + 

									 '</a>');
					       		           	 
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
					
					{ mData: "resourcePoolName", className: 'editable', sWidth: '5%', editField: "resourcePoolId",
						mRender: function (data, type, full) {
					       	 if (full.action == "create" || full.action == "edit"){					       		
					       		data = optionsValueHandler(userlist_editor, 'resourcePoolId', full.resourcePoolId);
					       	 }else if(type == "display"){					       		
					       		data = full.resourcePoolName;
					       	 }	           	 
					         return data;
					     },				
					},
					
					/*{ mData: "vendorId", className: 'disableEditInline', sWidth: '5%'},*/
					
					{ mData: "vendorId", className: 'editable', sWidth: '5%',
						mRender: function (data, type, full) {
					       	 if (full.action == "create" || full.action == "edit"){
					       		data = optionsValueHandler(userlist_editor, 'vendorId', full.vendorId);
					       	 }
					       	 else if(type == "display"){
					       		data = full.vendorName;
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
					
					 { mData: null, sWidth: '7%', "render": function (data,type,full) {	        
						 var userId=full.userId;
						 var roleId=full.roleId;
						 var loginId=full.loginId;
						 
						 return ('<a style="color: #0000FF;" href=javascript:showUserProfile('+userId+','+roleId+');>' + loginId + '</a>');
					      },
		            },
		            
					{ mData: null,				 
						bSortable: false,
						mRender: function(data, type, full) {				            	
							 var img = ('<div style="display: flex;">'+
					    		'<button style="border: none; background-color: transparent; outline: none;">'+
					    				'<img src="css/images/reset-icon.png" class="details-control img1" title="Reset Password" style="margin-left: 5px;"></button>'+
					    		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
										'<i class="fa fa-search-plus img3" title="Audit History"></i></button>'+ 
					    		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
					    				'<img src="css/images/list_metro.png" class="details-control img2" title="Add/Edit User Customer Account"></button>'+
					    		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
					    				'<img src="css/images/list_metro.png" class="details-control img4" title="Mapping Resourcepool to user"></button>'+
					    		'<button style="border: none; background-color: transparent; outline: none;">'+
		   								'<i class="fa fa-trash-o details-control img5" onClick="displayUserDeleteFlex('+data.userId+')" title="Delete User" style="padding-left: 0px;"></i></button>'+
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
		  		 
    	$("#flexUsersContainerDT_length").css('margin-top','8px');
		$("#flexUsersContainerDT_length").css('padding-left','35px');		
		//$(".select2-drop").css('z-index','100000');
		
		$('#flexUsersContainerDT tbody').on( 'click', 'td', function (e) {
			editorInStanceFlexUserListDT = userListFlex_oTable.DataTable().cell(this);
	        } );
		
		$('#flexUsersContainerDT').on( 'change', 'input.editor-active', function () {
			 userStatus = $(this);
			 var tr = $(this).closest('tr');
			 var row = userListFlex_oTable.DataTable().row(tr);  
			 //if(!$(this).prop('checked')){				   	
				 displayStatusChangeFlex(row.data().userId,row.data().status);
			//} 
		/*else {
				 displayStatusChangeFlex(row.data().userId,row.data().status);
				 userlist_editor
		            .edit( $(this).closest('tr'), false )
		            .set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
		            .submit(); 
			 }*/
			 			 
		    });
		
		userListFlex_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );
		

		 $('#flexUsersContainerDT tbody').on('click', 'td button .img1', function () {
	    	var tr = $(this).closest('tr');
	    	var row = userListFlex_oTable.DataTable().row(tr);    	
	    	remove(row.data().userId);
		});	 
		 
		 
		 // ----- User Customer Account -----
		 $('#flexUsersContainerDT tbody').on('click', 'td button .img2', function () {
		    	var tr = $(this).closest('tr');
		    	var row = userListFlex_oTable.DataTable().row(tr);
		    	    	
		    	userCustomerAccounttHandler(row.data().userId, row, tr);
		    	$("#regularUserCustomerContainer").modal();
			});
		 
		 $('#flexUsersContainerDT tbody').on('click', 'td button .img3', function () {
		    	var tr = $(this).closest('tr');
		    	var row = userListFlex_oTable.DataTable().row(tr);
		    	
		    	listGenericAuditHistory(row.data().userId,"User","regularUserAudit");	    	
			});
		 
		 $('#flexUsersContainerDT tbody').on('click', 'td button .img4', function () {
		    	var tr = $(this).closest('tr');
		    	var row = userListFlex_oTable.DataTable().row(tr);
		    	    	
		    	userCustomerResourcePoolHandler(row.data().userId, row, tr);
		    	$("#flexUserResourcePoolContainer").modal();
			});
		 
		// Activate an inline edit on click of a table cell
	     $('#flexUsersContainerDT').on( 'click', 'tbody td.editable', function (e) {
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
		
	 });
        	
	
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
		                        pageSize: 'A2'
		                    },	                    
		                ],	                
		            },
		            'colvis',
		            {					
						text: '<i class="fa fa-upload showHandCursor flexUserUpload" title="Upload User List"></i>',
						action: function ( e, dt, node, config ) {						
							importUsersList('FTE');
						}
					},
					{					
						text: '<i class="fa fa-download showHandCursor downloadFlexUserTemplate" title="Download User Template"></i>',
						action: function ( e, dt, node, config ) {					
							downloadTemplateUserList();
						}
					},
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
				'<th class="dataTableChildHeaderTitleTH">Customer</th>'+
				'<th class="dataTableChildHeaderTitleTH">Customer User Name</th>'+
				'<th class="dataTableChildHeaderTitleTH">Customer User Code</th>'+
				'<th class="dataTableChildHeaderTitleTH">Email Id</th>'+
				'<th class="dataTableChildHeaderTitleTH">From Date</th>'+
				'<th class="dataTableChildHeaderTitleTH">To Date</th>'+
			'</tr>'+
		'</thead>'+
		'<tfoot>'+
			'<tr><th></th><th></th><th></th><th></th><th></th><th></th></tr>'+			
		'</tfoot>'+
	'</table>';		
	
	return childDivString;	
}

function regularUserChild2Table(){
	var childDivString = '<table id="regularUserChild2_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead>'+
			'<tr>'+
				'<th class="dataTableChildHeaderTitleTH">ResourcePool Name</th>'+
				'<th class="dataTableChildHeaderTitleTH">From Date</th>'+
				'<th class="dataTableChildHeaderTitleTH">To Date</th>'+
				'<th class="dataTableChildHeaderTitleTH">Remarks</th>'+				
			'</tr>'+
		'</thead>'+
		'<tfoot>'+
			'<tr><th></th><th></th><th></th><th></th></tr>'+			
		'</tfoot>'+
	'</table>';		
	
	return childDivString;	
}
 // -----

function userCustomerAccounttHandler(userId, row, tr){
	var url = 'resourceManagement.user.customerAccount.list?userId='+userId+'&jtStartIndex=0&jtPageSize=10000';
	updateFlexUserList(url, "childTable1", row, tr);	
}

function userCustomerResourcePoolHandler(userId, row, tr){
	var url = 'administration.resourcepoolmapping.list?userId='+userId+'&jtStartIndex=0&jtPageSize=10000';
	updateFlexUserList(url, "childTable2", row, tr);	
}


function flexUserResourcePoolMap(row){

	userlistChild2Cutomer_editor = new $.fn.dataTable.Editor( {
	    "table": "#regularUserChild2_dataTable",
		ajax: "administration.resourcepoolmapping.add",
		ajaxUrl: "administration.resourcepoolmapping.update",
		"idSrc":  "resourcePoolMappingId",
		i18n: {
	        create: {
	            title:  "Mapping Resourcepool to user ",
	            submit: "Create",
	        }
	    },
		fields: [ {
	        label: "userId",
	        name:  "userId",
	        "type": "hidden",
	        "default": row.data().userId,
	    },{
            label: "resourcepoolId",
            name: "resourcepoolId",
            options: optionsRegularUserResultArr[0],
            "type": "select",			
        },{
	        label: "Remarks",
	        name: "remarks",
	    },{
	        label: "From Date",
	        name: "fromDate",
	        type:  'datetime',
	        def:    function () { return new Date(); },
	        format: 'M/D/YYYY',
	    }, {
	        label: "To Date",
	        name: "toDate",
	        type:  'datetime',
	        def:    function () { return new Date(); },
	        format: 'M/D/YYYY',
	    },{
	        label: "resourcePoolName",
	        name:  "resourcePoolName",
	        "type": "hidden",
	    } 
	]
	});
	
	$( 'input', userlistChild2Cutomer_editor.node()).on( 'focus', function () {
		this.select();
	});
	
	// ----- ended -----
}

function flexUserListChildTable_Container(data, scrollYValue){	
	try{
		if ($("#flexUserResourcePoolDTContainer").children().length>0) {
			$("#flexUserResourcePoolDTContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = regularUserChild2Table(); 			 
	$("#flexUserResourcePoolDTContainer").append(childDivString);
	
	flexUserResourcePoolMap(scrollYValue);
			
	flexUserListChildTable_oTable = $("#regularUserChild2_dataTable").dataTable( {
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
		     	  var headerItems = $('#regularUserChild2_dataTable_wrapper tfoot tr th');
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
		     	 reInitializeFlexUserCutomerList();
		   },  
		   select: true,
		   buttons: [
	             	{ extend: "create", editor: userlistChild2Cutomer_editor },	  
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
		                        pageSize: 'A2'
		                    },	                    
		                ],	                
		            },
		            'colvis'
	         ],	         
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "resourcepoolId", className: 'editable', sWidth: '5%', editField: "resourcepoolId",
            	mRender: function (data, type, full) {
		           	 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(userlistChild2Cutomer_editor, 'resourcepoolId', full.resourcepoolId);
		           	 }
		           	 else if(type == "display"){
		           		data = full.resourcepoolName;
		           	 }	           	 
		             return data;
	             },
            },		
                 
           { mData: "fromDate", className: 'editable', sWidth: '15%',
           		mRender: function (data, type, full) {
		           	 if (full.action == "create"){
		           		//data = convertDTDateFormatAdd(data, ["fromDate"]);
		           	 }else if(type == "display"){
		           		data = full.fromDate;
		           	 }	           	 
		             return data;
	             }
           },
           { mData: "toDate", className: 'editable', sWidth: '15%',
          		mRender: function (data, type, full) {
		           	 if (full.action == "create"){
		           		//data = convertDTDateFormatAdd(data, ["toDate"]);
		           	 }else if(type == "display"){
		           		data = full.toDate;
		           	 }	           	 
		             return data;
	             }
          },  
          { mData: "remarks",className: 'editable', sWidth: '20%' },          
       ],       
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	// ------
	 $(function(){ // this will be called when the DOM is ready
		 
		$("#regularUserChild2_dataTable_length").css('margin-top','8px');
		$("#regularUserChild2_dataTable_length").css('padding-left','35px');
		
		$('#regularUserChild2_dataTable').DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });
		
		 $('#regularUserChild2_dataTable').on( 'click', 'tbody td.editable', function (e) {
			 userlistChild2Cutomer_editor.inline( this, {
		            submitOnBlur: true
		        } );
		    });	
	
	 });
	
}

var usersId;
var statusValue;
function displayStatusChangeFlex(userId,status){
	$('textarea#statusChangeInputFlex').val("");
	$("#div_StatusChangeFlexUsers").modal();
	usersId = userId;
	statusValue=status;
	var statusChangeTitle="Status Change Comments";
	
	$("#div_StatusChangeFlexUsers .modal-header h4").text(statusChangeTitle);
	$("#notifyStatusChangeFlex").empty();
	$('#notifyStatusChangeFlex').show();
	
}

function submitStatusChangeFlexHandler(){
	
	var statusMessageFromUI = $('textarea#statusChangeInputFlex').val();
	if(statusMessageFromUI == "") {
		callAlert("Please give a reason for making user Active/Inactive");
		return;
	}else{	
		userlist_editor
			.edit( userStatus.closest('tr'), false )
			.set( 'status', userStatus.prop( 'checked' ) ? 1 : 0 )
			.submit();
	}
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
	

	openLoaderIcon();
	$.ajax({
		url : 'user.group.status.reason.audit?userId='+usersId+'&message='+statusMessageFromUI+'&fieldName='+fieldName+'&oldField='+oldField+'&newField='+newField,		
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			closeLoaderIcon();
		},
		error : function(data) {
			closeLoaderIcon();  
		},
	});
	
	$("#div_StatusChangeFlexUsers").modal('hide');	
}

function popupCloseStatusChangeFlexHandler(){
	$("#div_StatusChangeFlexUsers").modal('hide');
}

function displayUserDeleteFlex(userId){
	$('textarea#userDeleteInputFlex').val("");
	$("#div_UserDeleteFlex").modal();
	usersId = userId;
	var userDeleteTitle="User Deletion Comments";
	
	$("#div_UserDeleteFlex .modal-header h4").text(userDeleteTitle);
	$("#notifyUserDeleteFlex").empty();
	$('#notifyUserDeleteFlex').show();
	
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

function submitUserDeleteFlexHandler(){
	var userDeleteMessage = $('textarea#userDeleteInputFlex').val();
	if(userDeleteMessage == ""){
		callAlert("Please give a reason for making user Delete/Active");
		return;
	}
	var fieldName = "Status";
	var oldField = "Active";
	var newField = "Delete";
	$.post('user.group.status.reason.audit?userId='+usersId+'&message='+userDeleteMessage+'&fieldName='+fieldName+'&oldField='+oldField+'&newField='+newField, function(data) {	
		console.log("success "+data.Message);
	});
	
	$("#div_UserDeleteFlex").modal('hide');
	deleteUserListingItem(usersId);
}

function popupCloseUserDeleteFlexHandler(){
	$("#div_UserDeleteFlex").modal('hide');
}