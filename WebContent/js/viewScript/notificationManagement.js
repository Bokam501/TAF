var key ='';
var nodeType='';

var testFactoryId = 0;
var productId = 0;
var editorNotification="";
var engagementName="";
var productName="";

jQuery(document).ready(function() {	
   QuickSidebar.init(); // init quick sidebar
   ComponentsPickers.init();
   setBreadCrumb("Notifications");
  // createHiddenFieldsForTree();
   setPageTitle("Notifications");
   getTreeData("administration.productWithTF.tree");
   //setDefaultnode("j1_1");   

	
	/*$("#treeContainerDiv").on("loaded.jstree", function(evt, data){
	   var defaultNodeId = "j1_1";
	   $.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
  });*/
   
   $("#treeContainerDiv").on("select_node.jstree",
		     function(evt, data){
	   			var entityIdAndType =  data.node.data;
	   			var arry = entityIdAndType.split("~");
	   			 key = arry[0];
	   			var type = arry[1];
			    nodeType = type;
			    var loMainSelected = data;
		        uiGetParents(loMainSelected);
		        
		         if(nodeType == "TestFactory"){
		        	 testFactoryId = key;
		        	 productId = 0;
		        	 engagementName=data.node.text;
		        	 //setDefaultnode(data.node.id);
			    }
			    if(nodeType == "Product"){
			    	productId = key;
			    	testFactoryId = data.node.original.parent;
			    	productName=data.node.text;
			   	}	 
			  //  $('#tabslist li').first().find("a").trigger("click");
			    notificationDTFlag=false;
			    var url = 'notification.policy.management.list?isActive=1&productId='+productId+'&jtStartIndex=0&jtPageSize=10000';
			    assignNotificatiionDataTableValues(url, "parentTable", "", "");
	     	}
		);
  // loadExecutionTypes();
   
	 /*  $( document ).tooltip({
			 track: true,
		      position: {
		        my: "center bottom-20",
		        at: "center top",
		        using: function( position, feedback ) {
		          $( this ).css( position );
		          $( "<div>" )
		            .addClass( "arrow" )
		            .addClass( feedback.vertical )
		            .addClass( feedback.horizontal )
		            .appendTo( this );
		        }
		      }
		    });*/
	    
}); 

/*var isFirstLoad=true;
function setDefaultnode(defaultNodeId) {			
	if(isFirstLoad) {
		$("#treeContainerDiv").on("loaded.jstree",function(evt, data) {
			$.each($('#treeContainerDiv li'), function(ind, ele){
				if($.jstree.reference('#treeContainerDiv').is_parent($(ele).attr("id"))){
					defaultNodeId = $(ele).attr("id");							
					isFirstLoad = false;
					return false;
				}
			});	
			setDefaultnode(defaultNodeId);
		});
	} else {
		defaultNodeId = $.jstree.reference('#treeContainerDiv').get_node(defaultNodeId).children[0];
		$.jstree.reference('#treeContainerDiv').deselect_all();
		$.jstree.reference('#treeContainerDiv').close_all();
		$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
		$.jstree.reference('#treeContainerDiv').trigger("select_node");
	}
}*/


var notificationJsonData='';
var notificationDTFlag=false;
var notification_oTable='';

function assignNotificatiionDataTableValues(url,tableValue, row, tr){
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
				notificationJsonData = data;
				if(!notificationDTFlag)
					notificationContainer(data, "240px");
				else				
					reloadDataTableHandler(data, notification_oTable);
				
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

var optionsNotificationArr=[];
var optionsResultArrNotify=[];
var optionsItemCounterNotify=0;
var optionsType_notificationRecipients="Notification";

function notificationContainer(data, scrollYValue){
	optionsItemCounterNotify=0;
	optionsResultArrNotify=[];
	optionsNotificationArr = [{id:"primaryRecipients", type:optionsType_notificationRecipients, url:'notification.user.list.by.resourcepool.product?productId='+productId},
	              {id:"secondaryRecipients", type:optionsType_notificationRecipients, url:'notification.user.list.by.resourcepool.product?productId='+productId},
	              {id:"notificationId", type:optionsType_notificationRecipients, url:'notification.management.option.list?activeState=1'},
	              ];
	returnOptionsItem(optionsNotificationArr[0].url, scrollYValue, data, "");
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
         	optionsResultArrNotify.push(json.Options);
         	
         	if(optionsNotificationArr[0].type == optionsType_notificationRecipients){
         		notification_Container(data, scrollYValue);     			   
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
     	  optionsResultArrNotify.push(json.Options);
     	   
     	   if(optionsItemCounterNotify<optionsNotificationArr.length-1){
     			 optionsNotificationArr[optionsResultArrNotify.length].url = optionsNotificationArr[optionsResultArrNotify.length].url;		
     		     returnOptionsItem(optionsNotificationArr[optionsResultArrNotify.length].url, scrollYValue, data, tr);     		  
     	   }else{
     		   if(optionsNotificationArr[0].type == optionsType_notificationRecipients){
     			  notification_Container(data, scrollYValue);     			   
     		   }
     		}
     	  optionsItemCounterNotify++;     	   
         }
         },
         error: function (data) {
        	 optionsItemCounterNotify++;
        	 
         },
         complete: function(data){
         	console.log('Completed');
         	
         },	            
   	});	
	
}

function notification_Container(data, scrollYValue){
	/*try{
		if ($('#notificationListContainer').length>0) {
			$('#notificationListContainer').dataTable().fnDestroy(); 
		}
	} catch(e) {}*/
	
	
	
	// ----- Notification DataTable Creation Started -----

		editorNotification = new $.fn.dataTable.Editor( {
	    	    "table": "#notifications_dataTable",
	    		/*ajax: "notification.policy.management.add",*/
	    		ajaxUrl: "notification.policy.management.update",
	    		idSrc:  "notificationPolicyId",
	    		i18n: {
	    	       /* create: {
	    	            title:  "Create a Notification",
	    	            submit: "Create",
	    	        }*/
	    	    },
	    		fields: [{
	                label: "NotificationPolicyId",
	                name: "notificationPolicyId",
	                "type": "hidden",
	            },{
	                label: "ProductId",
	                name: "productId",
	                "type": "hidden",
	                "default": productId,
	            },  {
	                label: "Product",
	                name: "productName",
	                "type": "hidden",
	                "default":productName,
	            },{
	                label: "Entity",
	                name: "entityTypeName",
	                "type": "hidden",
	                
	            },{
	                label: "Notification",
	                name: "notificationId",
	                options: optionsResultArrNotify[2],            
	                "type"  : "select",
	            },{
	                label: "Notification Name:",
	                name: "notificationName",
	                options: optionsResultArrNotify[2],            
	                "type"  : "hidden",
	            },{
	                label: "Primary Recipients:",
	                name: "primaryRecipients",
	                options: optionsResultArrNotify[0],            
	                "type"  : "select",
	            },{
	                label: "Secondary Recipients",
	                name: "secondaryRecipients",
	                options: optionsResultArrNotify[1],
	                "type"  : "select",
	            },{
	                label: "Email:",    
	                name: "email",
	                type: "checkbox",
	                separator: "|",
	                options:[{ label: '', value: 1 }],
	            },{
	                label: "Sms:",
	                name: "sms",
	                type: "checkbox",
	                separator: "|",
	                options:[{ label: '', value: 1 }],
	            },{
	                label: "WhatsApp:",
	                name: "whatsapp",	
	                type: "checkbox",
	                separator: "|",
	                options:[{ label: '', value: 1 }],
	            },{
	                label: "Twitter:",
	                name: "twitter",
	                type: "checkbox",
	                separator: "|",
	                options:[{ label: '', value: 1 }],
	            },{
	                label: "Facebook:",
	                name: "facebook",
	                type: "checkbox",
	                separator: "|",
	                options:[{ label: '', value: 1}],
	            },  
	        ]
	    	});
	
	notification_oTable = $('#notifications_dataTable').dataTable( {
		"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"bScrollCollapse": true,
		autoWidth: false,
		bAutoWidth:false,
		"sScrollX": "100%",
       "sScrollXInner": "100%",
       "scrollY":"100%",
       "fnInitComplete": function(data) {
    	   var searchcolumnVisibleIndex = [5,6,7,8,9,10,11]; // search column TextBox Invisible Column position
    	   if(!notificationDTFlag){
    		   $('#notifications_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
    	   notificationDTFlag=true;
    	   reInitializeDTNotification();
	   },  
	   buttons: [
		        /* { extend: "create", editor: editorNotification},*/
		         {
		          extend: "collection",	 
		          text: 'Export',
	              buttons: [
		          {
                    	extend: 'excel',
                    	title: 'Notifications',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'csv',
                    	title: 'Notifications',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'pdf',
                    	title: 'Notifications',
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
         columnDefs: [
              /*{ targets: [0,1,2,3,4,5,6,7,8,9,10], visible: true},
              { targets: '_all', visible: false }*/
          ],
                  
         aaData:data,                 
	    aoColumns: [	        
	       { mData: "productName", className: 'disableEditInline' , sWidth: '10%'},
	       { mData: "entityTypeName", className: 'disableEditInline' , sWidth: '10%'},
	       { mData: "notificationName", className: 'disableEditInline', sWidth: '10%', editField: "notificationId",
           	mRender: function (data, type, full) {
		           	 if (full.action == "edit"){
		           		data = optionsValueHandler(editorNotification, 'notificationId', full.notificationId);
		           	 }
		           	 else if(type == "display"){
		           		data = full.notificationName;
		           	 }	           	 
		             return data;
	             },
	       },
           { mData: "primaryRecipients", className: 'editable', sWidth: '10%', editField: "primaryRecipients",
           	mRender: function (data, type, full) {
		           	 if (full.action == "edit"){
		           		data = optionsValueHandler(editorNotification, 'primaryRecipients', full.primaryRecipients);
		           	 }
		           	 else if(type == "display"){
		           		data = full.primaryRecipients;
		           	 }	     
		           	data = full.primaryRecipients;
		             return data;
	             },
           },	
           { mData: "secondaryRecipients", className: 'editable' , sWidth: '10%', editField: "secondaryRecipients",
        		mRender: function (data, type, full) {
		           	if (full.action == "edit"){
		           		data = optionsValueHandler(editorNotification, 'secondaryRecipients', full.secondaryRecipients);
		           	 }
		           	 else if(type == "display"){
		           		data = full.secondaryRecipients;
		           	 }	
		           	 data = full.secondaryRecipients;
		             return data;
	             }, 
        	   
        	   
           },	
           { mData: "email", sWidth: '5%',
               mRender: function (data, type, full) {
             	  if ( type === 'display' ){
                         return '<input type="checkbox" class="editorNotification-active1">';
                     }
                     return data;
                 },
                 className: "dt-body-center"
             },
           
             { mData: "sms", sWidth: '5',
                 mRender: function (data, type, full) {
               	  if ( type === 'display' ){
                           return '<input type="checkbox" class="editorNotification-active2">';
                       }
                       return data;
                   },
                   className: "dt-body-center"
               },
               
	       { mData: "whatsapp", sWidth: '5',
	           mRender: function (data, type, full) {
	         	  if ( type === 'display' ){
	                     return '<input type="checkbox" class="editorNotification-active3">';
	                 }
	                 return data;
	             },
	             className: "dt-body-center"
	         },
           
          { mData: "twitter", sWidth: '5',
             mRender: function (data, type, full) {
           	  if ( type === 'display' ){
                       return '<input type="checkbox" class="editorNotification-active4">';
                   }
                   return data;
               },
               className: "dt-body-center"
           },
           { mData: "facebook",sWidth: '5',
               mRender: function (data, type, full) {
             	  if ( type === 'display' ){
                         return '<input type="checkbox" class="editorNotification-active5">';
                     }
                     return data;
                 },
                 className: "dt-body-center"
             },
             
       ],
       rowCallback: function ( row, data ) {
    	   $(".editorNotification-active2").attr("disabled", true);
		    $(".editorNotification-active3").attr("disabled", true);
		    $(".editorNotification-active4").attr("disabled", true);
		    $(".editorNotification-active5").attr("disabled", true);
		    
           $('input.editorNotification-active1', row).prop( 'checked', data.email == 1 );
           /*$('input.editorNotification-active2', row).prop( 'checked', data.sms == 1 );
           $('input.editorNotification-active3', row).prop( 'checked', data.whatsapp == 1 );
           $('input.editorNotification-active4', row).prop( 'checked', data.twitter == 1 );
           $('input.editorNotification-active5', row).prop( 'checked', data.facebook == 1 );*/
       },
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },
	});
	 

    
	//-----------------
	 $(function(){ // this will be called when the DOM is ready 
		 $("#notifications_dataTable_length").css('margin-top','8px');
			$("#notifications_dataTable_length").css('padding-left','35px');
			
			$(".editorNotification-active2").attr("disabled", true);
		    $(".editorNotification-active3").attr("disabled", true);
		    $(".editorNotification-active4").attr("disabled", true);
		    $(".editorNotification-active5").attr("disabled", true);
		
		 // Activate an inline edit on click of a table cell
		    $('#notificationListContainer').on( 'click', 'tbody td.editable', function (e) {
		    	editorNotification.inline( this, {
		            submitOnBlur: true
		        } );
		    });
	    
		    for(var i=1;i<=5;i++){
			    $('#notifications_dataTable').on( 'change', 'input.editorNotification-active'+i, function () {
			    	editorNotification
			            .edit( $(this).closest('tr'), false )
			            .set( 'email', $(this).prop( 'checked' ) ? 1 : 0 )
			            .set( 'sms', $(this).prop( 'checked' ) ? 1 : 0 )
			            .set( 'whatsapp', $(this).prop( 'checked' ) ? 1 : 0 )
			            .set( 'twitter', $(this).prop( 'checked' ) ? 1 : 0 )
			            .set( 'facebook', $(this).prop( 'checked' ) ? 1 : 0 )
			            .submit();
				 });	
		    }
		    
		 notification_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
	    } );
		 
		
		// Use the elements to store their own index  
		 	
	} ); 
}

var clearTimeoutDTNotification='';
function reInitializeDTNotification(){
	clearTimeoutDTPMP = setTimeout(function(){				
		notification_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTNotification);
	},200);
}

