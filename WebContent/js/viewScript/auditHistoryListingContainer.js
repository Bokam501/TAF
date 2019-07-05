var auditHistoryTabDT_oTable='';

//-- Audit History for the Product Management Plan ----
function auditHistoryTabDataTable(containerID){
	var childDivString = '<table id="auditHistoryTab_'+containerID+'dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>Event</th>'+
			'<th>Description</th>'+
			'<th>Remarks</th>'+
			'<th>User</th>'+
			'<th>Time</th>'+
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
		'</tr>'+
	'</tfoot>'+
	'</table>';		
	
	return childDivString;	
}

function auditHistoryCompListing(url, containerID){
	var urlValue = url+'&jtStartIndex=0&jtPageSize=10000';
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:urlValue,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			auditHistoryTabCompListing(containerID, data);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

var clearTimeoutTabDataTable;
function reInitializeTabDTAuditHistory(){
	clearTimeoutTabDataTable = setTimeout(function(){				
		auditHistoryTabDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTabDataTable);
	},200);
}

function auditHistoryTabCompListing(containerID, data){
	
	try{
		if ($("#"+containerID).children().length>0) {
			$("#"+containerID).children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = auditHistoryTabDataTable(containerID); 			 
	$("#"+containerID).append(childDivString);
	
	auditHistoryTabDT_oTable = $('#auditHistoryTab_'+containerID+'dataTable').dataTable( {
		 	"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       "aaSorting": [[5,'desc']],
	       "fnInitComplete": function(data) {
	    	   
	    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     	  var headerItems = $('#auditHistoryTab_'+containerID+'dataTable_wrapper tfoot tr th');
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
	    	   reInitializeTabDTAuditHistory();
		   },  
		   buttons: [
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Audit History',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Audit History',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Audit History',
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
	       { mData: "eventId", className: 'disableEditInline', sWidth: '5%' },
           { mData: "eventName", className: 'disableEditInline', sWidth: '10%' },		
           { mData: "eventDescription", className: 'disableEditInline', sWidth: '35%' },		
           { mData: "sourceEntityName", className: 'disableEditInline', sWidth: '30%' },		
           { mData: "userName", className: 'disableEditInline', sWidth: '10%' },		
           { mData: "endTime", className: 'disableEditInline', sWidth: '10%' },	
       ],       
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		
		$('#auditHistoryTab_'+containerID+'dataTable_length').css('margin-top','8px');
		$('#auditHistoryTab_'+containerID+'dataTable_length').css('padding-left','35px');
		
		$('#auditHistoryTab_'+containerID+'dataTable').DataTable().columns().every( function () {
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

/*var jtableDIVId = "";
function auditHistoryCompListing(listURL, jtableDIV){
	jtableDIVId = jtableDIV;
	try{
		if ($(jtableDIV).length>0) {
			 $(jtableDIV).jtable('destroy');
		}
	} catch(e) {};	
		$(jtableDIV).jtable({
	         title: 'Audit History',
	        // selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: 10, //Set page size (default: 10)
	         editinline:{enable:false},	        
	         actions: {
	           listAction:listURL,
	         },
	         fields: {        	
	        	eventId: { 
	            	title: 'Event Id' ,	            	
	            	list:true,
	            	edit:false
	            },
//	            eventSourceComponent: { 
//	            	title: 'Source Component' ,
//	            	list:true,
//	            	edit:false
//	            },
	            eventName: { 
	            	title: 'Event Name',
	            	list:true,
	            	edit:false
	        	},
	            eventDescription: { 
	            	title: 'Event Description',
	            	list:true,
	            	edit:false
	        	},
	        	sourceEntityType: { 
	            	title: 'Entity Type',
	                list:true,
	            	edit:false
	            },
	        	sourceEntityName: { 
	            	title: 'Remarks',
	                list:true,
	            	edit:false
	            },
	            userName: { 
	            	title: 'User',
	                list:true,
	            	edit:false
	            },
	            endTime: { 
	            	title: 'Time',
	                list:true,
	            	edit:false
	            },		
	       },			 
	        formSubmitting: function (event, data) {    
			data.form.validationEngine();
	      	return data.form.validationEngine('validate');
	       }, 
	        //Dispose validation logic when form is closed
	        formClosed: function (event, data) {
	           data.form.validationEngine('hide');
	           data.form.validationEngine('detach');
	       },
	    });		 
	   $(jtableDIV).jtable('load');	
}*/
