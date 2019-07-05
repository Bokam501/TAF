
var optionsRegularUserArr=[];
var optionsRegularUserResultArr=[];
var optionsRegularUserItemCounter=0;
var optionType_userType="regularList";
var optionType_CustomeruserType="regularCustomerList";
var modifiedField;
var modifiedFieldTitle;
var oldFieldValue;
var newFieldValue;
var urlToListOnboardUserDetails;


function userList_Container(data, scrollYValue){
	optionsRegularUserItemCounter=0;
	optionsRegularUserResultArr=[];
	optionsRegularUserArr = [
	              
	              {id:"userRoleLabel", type:optionType_userType, url:'administration.product.user.listUserRole?typeFilter=1'},
	              {id:"testFactoryId", type:optionType_userType, url:'common.testFactory.list.byLabId?testFactoryLabId=1&engagementTypeId=0'},
	              {id:"productId", type:optionType_userType, url:'common.list.product.byTestFactoryId?testFactoryId=1'},
	              {id:"workPackageId", type:optionType_userType, url:'workpackage.type.option.byProductId?productId=1'}
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
	     		   } 
	     	   }
	     	   optionsRegularUserItemCounter++;     	   
	         }
         },error: function (data) {
        	 optionsRegularUserItemCounter++;
        	 
         },complete: function(data){
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

function reInitializeDTUserListRegular(){
	/*clearTimeoutDTuserListRegular = setTimeout(function(){				
		userListRegular_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTuserListRegular);
	},500);*/
}

function reInitializeRegularUserCutomerList(){
	clearTimeoutDTuserListRegularCustomer = setTimeout(function(){				
		userListRegularChildTable_oTable.DataTable().columns.adjust().draw();
	clearTimeout(clearTimeoutDTuserListRegularCustomer);
	},500);
}

function fullScreenHandlerDTRegularUser(){
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);
		$("#tabslist").show();
		regularUserListDTFullScreenHandler(true);		
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');						
		$("#tabslist").hide();
		regularUserListDTFullScreenHandler(false);
	}
}

function regularUserListDTFullScreenHandler(flag){
	if(flag){
		reInitializeDTUserListRegular();
		$("#usersApprovalContainerDT_wrapper .dataTables_scrollBody").css('max-height','220px');
	}else{
		reInitializeDTUserListRegular();
		$("#usersApprovalContainerDT_wrapper .dataTables_scrollBody").css('max-height','450px');
	}
}

function approveUser(url,tableValue, row, tr){
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			if(data.Message=="Success"){
				callAlert("User Approved successfully");
			}
			else if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			
			if(tableValue == "parentTable"){
				if(!regularUserListFlag){
					userList_Container(data, "220px");
				}else{				
					reloadDataTableHandler(data, userListRegular_oTable);
				}			
				
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

function rejectUser(url,tableValue, row, tr){
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			if(data.Message=="Success"){
				callAlert("User Rejected");
			}
			else if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			
			if(tableValue == "parentTable"){
				if(!regularUserListFlag){
					userList_Container(data, "220px");
				}else{				
					reloadDataTableHandler(data, userListRegular_oTable);
				}			
				
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
		"table": "#usersApprovalContainerDT",
	 	ajax: "process.activity.add",
		ajaxUrl: "onboard.user.request.access.approval.update",
		idSrc:  "onboardUserId",
		i18n: {
        /*create: {
	            title:  "Create a new Onboard User",
	            submit: "Create",
	        }*/
	    },
		fields: [/*{								
			label:"Onboard User ID",
			name:"onboardUserId",
			 "type"  : "hidden",
			
		},*/{
            label: "Onboard User Name",
            name: "onboardUserName",
        },{
            label: "Email ID",
            name: "emailId",                
        },{
            label: "User Role",
            name: "userRoleId",
            options: optionsRegularUserResultArr[0],
            "type"  : "select",
           
        },{
            label: "TestFactory",
            name: "testFactoryId",
            options: optionsRegularUserResultArr[1],
            "type"  : "select",
        },{
            label: "Product",
            name: "productId",
             options: optionsRegularUserResultArr[2],
            "type"  : "select",
        }, {
            label: "ActivityWorkPackage",
            name: "activityWorkpackageId",
            options: optionsRegularUserResultArr[3],
            "type"  : "select",
        },/*{
            label: "Status",
            name: "status",
            "type": "checkbox",
           
        }, */
    ]
	});

}

function listUserByTypeDT(data, scrollYValue){	
	userListEditor();	
	userListRegular_oTable = $('#usersApprovalContainerDT').dataTable({	 
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
     	   var searchcolumnVisibleIndex = [6]; // search column TextBox Invisible Column position
 		   
     	   if(!regularUserListFlag){
     		  $('#usersApprovalContainerDT_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
 	   select: true,
	   buttons: [
            	{ extend: "create", editor: userlist_editor },	         
	            'colvis'
        ],					        
        aaData:data,		    				 
	    aoColumns: [	    
	            
	           /* { mData: "onboardUserId", className: 'editable', sWidth: '10%'},*/
	            { mData: "onboardUserName", className: 'editable', sWidth: '10%'},
                { mData: "emailId", className: 'editable', sWidth: '10%'},      
                { mData: "userRoleId", className: 'editable', sWidth: '10%', editField: "userRoleId",
                	mRender: function (data, type, full) {
			           	 if (full.action == "create" || full.action == "edit"){
			           		data = optionsValueHandler(userlist_editor, 'userRoleId', full.userRoleId);
			           	 }else if(type == "display"){
			           		data = full.userRoleName;
			           	 }	           	 
			             return data;
		             },               	
                },
               
                { mData: "testFactoryId", className: 'editable', sWidth: '20%', editField: "testFactoryId",
                	mRender: function (data, type, full) {
			           	 if (full.action == "create" || full.action == "edit"){
			           		data = optionsValueHandler(userlist_editor, 'testFactoryId', full.testFactoryId);
			           	 }
			           	 else if(type == "display"){
			           		data = full.testFactoryName;
			           	 }	           	 
			             return data;
		             },               	
                },	
                { mData: "productId", className: 'editable', sWidth: '20%', editField: "productId",
                	mRender: function (data, type, full) {
			           	 if (full.action == "create" || full.action == "edit"){
			           		data = optionsValueHandler(userlist_editor, 'productId', full.productId);
			           	 }
			           	 else if(type == "display"){
			           		data = full.productName;
			           	 }	           	 
			             return data;
		             },               	
                },	
                { mData: "activityWorkpackageId", className: 'editable', sWidth: '20%', editField: "activityWorkpackageId",
                	mRender: function (data, type, full) {
			           	 if (full.action == "create" || full.action == "edit"){
			           		data = optionsValueHandler(userlist_editor, 'activityWorkpackageId', full.activityWorkpackageId);
			           	 }
			           	 else if(type == "display"){
			           		data = full.activityWorkpackageName;
			           	 }	           	 
			             return data;
		             },               	
                },
               /* { mData: "status",
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
	           			'<button style="border: none; color:green; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       						'<i class="fa fa-check img1" title="User Onboard"></i></button>'+
     	       			'<button style="border: none; color:red; background-color: transparent; outline: none;margin-left: 5px;">'+
     	       					'<i class="fa fa-times img2" title="Reject"></i></button>'+      	       			
         	       		'</div>');	      		
               		 return img;
	            	}
	            },            
              ], 
              rowCallback: function ( row, data ) {
  	            $('input.editor-active', row).prop( 'checked', data.status == 2 );
  	        },
         "oLanguage": {
        	"sSearch": "",
        	"sSearchPlaceholder": "Search all columns"
         },
	});
	
	 $(function(){ // this will be called when the DOM is ready 
		/* userlist_editor	    	
	    	.on("submitComplete", function( e ) {
	    		//urlToListUserDetails = 'administration.user.list.by.options.status?userTypeId='+usertypeId+"&resourcePoolId="+rpId+"&testFactoryLabId="+tflId+"&timeStamp="+timestamp+"&statusID="+id;
	    		urlToListOnboardUserDetails ='onboard.user.request.access.list?status='+statusFilter;	    		
	    		approveUser(urlToListOnboardUserDetails+'&jtStartIndex=0&jtPageSize=10000', "parentTable", "", "");	    		
	    	});*/
		 
		 
		  		 
    	$("#usersApprovalContainerDT_length").css('margin-top','8px');
		$("#usersApprovalContainerDT_length").css('padding-left','35px');		
		//$(".select2-drop").css('z-index','100000');				
		
		 /*$('#usersApprovalContainerDT').on( 'change', 'input.editor-active', function () {
			 var tr = $(this).closest('tr');
			 var row = userListRegular_oTable.DataTable().row(tr);  
			 if(!$(this).prop('checked')){				   	
				 displayStatusChangeNotification(row.data().userId);
				 
				 
			 } else {
				 userlist_editor
		            .edit( $(this).closest('tr'), false )
		            .set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
		            .submit(); 
			 }
			 			 
		    });*/
		
		 $("#usersApprovalContainerDT").on( 'click', 'tbody td.editable', function (e) {
			 userlist_editor.inline( this, {
	                submitOnBlur: true
	            } );
	        } );
		
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
		$("#usersApprovalContainerDT_wrapper").find(".buttons-create").hide();			
		userlist_editor.dependent('testFactoryId',function ( val, data, callback ) {
     	
		if(val != undefined){
 										
			var url = 'common.list.product.byTestFactoryId?testFactoryId='+val;
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
					
					if(userlist_editor.s.action == "create" || userlist_editor.s.action == "edit"){
						userlist_editor.set('productId',json.Options);
						userlist_editor.field('productId').update(json.Options);
					}
				}
			} );
		}
 });
 
		userlist_editor.dependent('productId',function ( val, data, callback ) {
     	
		if(val != undefined){
										
			var url = 'workpackage.type.option.byProductId?productId='+val;
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
					
					if(userlist_editor.s.action == "create" || userlist_editor.s.action == "edit"){
						userlist_editor.set('activityWorkpackageId',json.Options);
						userlist_editor.field('activityWorkpackageId').update(json.Options);
					}
				}
			} );
		}
});
		
		 $('#usersApprovalContainerDT tbody').on('click', 'td button .img1', function () {
		    	var tr = $(this).closest('tr');
		    	var row = userListRegular_oTable.DataTable().row(tr);
		    	    	
		    	userApprovalHandler(row.data().onboardUserName,row.data().productId, row, tr);
		    	$("#regularUserCustomerContainer").modal();
			});
		 
		 $('#usersApprovalContainerDT tbody').on('click', 'td button .img2', function () {
		    	var tr = $(this).closest('tr');
		    	var row = userListRegular_oTable.DataTable().row(tr);
		    	    	
		    	//userRejectHandler(row.data().userId, row, tr);
		    	userRejectHandler(row.data().onboardUserName, row, tr);
		    	$("#regularUserCustomerContainer").modal();
			}); 
		
	 });
	 }
	 	 
	 
	 
	 
	// ----- dependent Values -----

function userApprovalHandler(onboardUserName,productId, row, tr){
	/*modifiedField="status";
	modifiedFieldTitle="Status";
	oldFieldValue="Pending";
	newFieldValue="Approved";
	var url = 'administration.user.approve?userId='+userId+'&status=1&modifiedField='+modifiedField
			  +'&modifiedFieldTitle='+modifiedFieldTitle+'&oldFieldValue='+oldFieldValue+'&newFieldValue='+newFieldValue+'&jtStartIndex=0&jtPageSize=10000';*/
	var url = "onboard.user.request.access.approval?productId="+productId+"&userName="+onboardUserName;
	approveUser(url, "parentTable", row, tr);	
}

function userRejectHandler(onboardUserName, row, tr){
	modifiedField="status";
	modifiedFieldTitle="Status";
	oldFieldValue="Pending";
	newFieldValue="Rejected";
	var url = 'administration.onboard.user.reject?onboardUserName='+onboardUserName+'&status=-1&modifiedField='+modifiedField
	  +'&modifiedFieldTitle='+modifiedFieldTitle+'&oldFieldValue='+oldFieldValue+'&newFieldValue='+newFieldValue+'&jtStartIndex=0&jtPageSize=10000';
	/*
	var url = 'administration.user.reject?onboardUserId='+onboardUserId+'&status=-1&modifiedField='+modifiedField
	  +'&modifiedFieldTitle='+modifiedFieldTitle+'&oldFieldValue='+oldFieldValue+'&newFieldValue='+newFieldValue+'&jtStartIndex=0&jtPageSize=10000'; */
	
	rejectUser(url, "parentTable", row, tr);
}
