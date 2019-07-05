//var singleDataTableJsonObject='';
function assignDemandDataTableValues(url){
	//jsonObjtoReload = jsonObj;
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
				var jsonObj={};
				data = data.Records;				
				jsonObj.data = data.reverse();
				demandDataTableContainerSummary(jsonObj);
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

var clearTimeoutSingleDataTable;

function reInitializeDTCommentsList(){
	clearTimeoutSingleDataTable = setTimeout(function(){				
		commentsListSingleDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutSingleDataTable);
	},200);
}

function demandDataTableContainerSummary(jsonObj){	
	$("#div_demandUploadInfoDTSummary").find("h4").text(jsonObj.Title);
	demandUploadHistoryDT_Container(jsonObj);
	$("#div_demandUploadInfoDTSummary").modal();		
}

// ----- Comments Started -----

function listingCommentsMetronicsUI(jsonObj){
	$("#div_CommentsUI .timeline").html('');
	var dataLen = jsonObj.TotalRecordCount;
	if(dataLen >0){
		var timeLineItem='';
		for(var i=0;i<=dataLen-1;i++){								 				
			timeLineItem += '<div class="timeline-item">'+                                            
               '<div class="timeline-body" style="margin-left: 50px;">'+
                   '<div class="timeline-body-arrow"> </div>'+
                   '<div class="timeline-body-head">'+
                       '<div class="timeline-body-head-caption">'+
                           '<a href="javascript:;" id="commenter" class="timeline-body-title font-blue-madison">'+jsonObj.data[i].createrName+'</a>'+
                           '<span> Posted at </span>'+
                           '<span id="commentedTime" class="timeline-body-time font-grey-cascade">'+jsonObj.data[i].createdDate+'</span>'+
                       '</div>'+
                   '</div>'+
                   '<div id="commentsText" class="timeline-body-content">'+
                       '<span class="font-grey-cascade">'+jsonObj.data[i].commentsText+'</span>'+
                   '</div>'+
                   '<div id="result" class="timeline-body-content">'+
                   '<span class="font-grey-cascade">'+jsonObj.data[i].result+'</span>'+
               '</div>'+
               '</div>'+
               '</div>';			
		}	
		
	//	$("#div_CommentsUI .timeline").html('');
		$("#div_CommentsUI .timeline").html(timeLineItem);
	}
	
}

function addCommentsOfCommenter(){
	var addURL = 'comments.for.entity.or.instance.add';
	//var entityTypeId =18;
	var entityInstanceId = instanceId;
	var activityProductId=productId;
	var comments = $('#addCommentsDiv textarea').val().trim();	
	if(comments == ""){
		callAlert("Please enter some comments.");
		return;
	}
	openLoaderIcon();
		$.ajax({
		    type: "POST",
		    url: addURL,
		    data: { 'productId': activityProductId, 'commentsText': comments, 'entityTypeId': entityTypeId, 'entityInstanceId': entityInstanceId},
		    success: function(data) {
		    	closeLoaderIcon();  
		    	if(data > 0){
		    		closeCommentsPopup();		    		
		    		assignDemandDataTableValues(jsonObjtoReload);
		    		return true;
		    	}
		    	else{
		    		callAlert("Comments not posted");
	 		    	return false;
		    	}
		    },
			  error : function(data) {
				 closeLoaderIcon();  
			 },
			 complete: function(data){
				closeLoaderIcon();
			 },
		    dataType: "json", // expected return value type
		}); 
}


function closeCommentsPopup() {	
	$("#add_CommentsUI").modal('hide');
	
}

var commentsListSingleDT_oTable='';

function demandDataTable(){
	var childDivString = '<table id="demand_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th class="dataTableChildHeaderTitleTH">Uploaded File Name</th>'+
			'<th class="dataTableChildHeaderTitleTH">CreatedBy</th>'+
			'<th class="dataTableChildHeaderTitleTH">CreatedOn</th>'+
			'<th class="dataTableChildHeaderTitleTH">Result</th>'+
		'</tr>'+
	'</thead>'+
	'</table>';		
	
	return childDivString;	
}

function demandUploadHistoryDT_Container(jsonObj){	
	
	try{
		if ($("#demand_dataTable").children().length>0) {
			$("#demand_dataTable").children().remove();
		}
	} 
	catch(e) {}
	
	$("#dataTableDemandUploadInfo").html('');
	var childDivString = demandDataTable(); 			 
	$("#dataTableDemandUploadInfo").append(childDivString);
	
	commentsListSingleDT_oTable = $("#demand_dataTable").dataTable( {
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
	    	   reInitializeDTCommentsList();
		   },  
		   buttons: [
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
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           /*{ mData: "commentId", className: 'disableEditInline', sWidth: '15%' },		
           { mData: "entityMasterName", className: 'disableEditInline', sWidth: '20%' },		
           { mData: "entityPrimaryId", className: 'disableEditInline', sWidth: '20%' },		*/
           { mData: "commentsText", className: 'editable', sWidth: '40%' },
           { mData: "createrName", className: 'disableEditInline', sWidth: '12%' },	
           { mData: "createdDate", className: 'disableEditInline', sWidth: '12%' },
           { mData: "result", className: 'disableEditInline', sWidth: '10%',
				mRender: function (data, type, full) {
					
					if(type == "display"){
						if(typeof full.result != 'undefined') {
							   var resultDetails = full.result.split(",");
							   var successResult=resultDetails[0];
							   var failureResult=resultDetails[1];
							   data="Success Count:"+successResult+" Failure Count:"+failureResult;							   							
						}
					 }	           	 
					 return data;
				 },
			},
       ],      
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
}


//----- Comments Ended -----

var commentsData = {};
function getDemandUploadHistory(engId,prId,awpId,entityTypeId){
	if(engId !="" && prId =="" && awpId == ""){
		prId = 0;
		awpId = 0;
	}else if(engId !="" && prId !="" && awpId == ""){
		engId = 0;
		awpId = 0;
	}else{
		prId = 0;
		engId = 0;
	}
	
	if(prId ==0) {
		if(typeof currentProductId != 'undefined' ) {
			prId=currentProductId;
		}
	} 
	var fromDate ="";
	var todate = "";
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url : 'comments.for.entity.list?testFactoryId='+engId+'&productId='+prId+'&workPackageId='+awpId+'&entityTypeId='+entityTypeId+'&fromDate='+fromDate+'&toDate='+todate,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			
			if(data.Result=="ERROR"){
     		    data = [];						
			}else{
				commentsData = data.Records;
											
				getCommentsDataTable();
				var columnValueMapping = getCommentsColumnMappings();
				getAllComments( columnValueMapping, commentsData);
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



function getCommentsDataTable(){
	var detailedCommentsContainer;
	detailedCommentsContainer = $('#allComments');
		
	
	if(detailedCommentsContainer != 'undefined' && detailedCommentsContainer != null){
		detailedCommentsContainer.empty();
	}
	$('#allComments_dataTable').remove();
	
	var allComments_dtColumns = ['Comments', 'Commented By', 'Commented Date'];
	
	var allCommentsTable = '<table id="allComments_dataTable"  class="cell-border compact row-border" cellspacing="0" width="100%">';
	var allCommentsTable_thead = '<thead><tr>';
	var allCommentsTable_tfoot = '<tfoot><tr id="allComments_dataTable_filterRow">';
	$.each(allComments_dtColumns, function(index, value){
		allCommentsTable_thead += '<th class="sorting">'+value+'</th>';
		allCommentsTable_tfoot += '<th></th>';
	});
	allCommentsTable += allCommentsTable_thead + '</tr></thead>'+ allCommentsTable_tfoot + '</tr></tfoot></table>';
	detailedCommentsContainer.append(allCommentsTable);
		$("#div_demandUploadInfoDTSummary").modal();
}

function getCommentsColumnMappings(){
	var allCommentsColumns = [];
	/*allCommentsColumns.push({ mData: "entityPrimaryId",className: 'disableEditInline', sWidth: '0.5%',"render": function (tcData,type,full) {	        
 		var entityInstanceId = full.entityPrimaryId;					
 		return ('<a onclick="displayTabActivitySummaryHandler('+entityInstanceId+')">'+full.entityPrimaryId+'</a>');		        
	},});
	allCommentsColumns.push({ mData: "entityName",className: 'disableEditInline', sWidth: '2%'});*/
	allCommentsColumns.push({ mData: "commentsText",className: 'disableEditInline', sWidth: '8%'});
	allCommentsColumns.push({ mData: "createrName", className: 'disableEditInline', sWidth: '0.5%'});
	allCommentsColumns.push({ mData: "createdDate", className: 'disableEditInline',  sWidth: '0.5%'});
	allCommentsColumns.push({ mData: "result", className: 'disableEditInline',  sWidth: '0.5%'});
	
	allCommentsColumsAvailableIndex = [0, 1, 2, 3];
	
	
	return allCommentsColumns;
}





var allComments_oTable = '';
var allCommentsColumsAvailableIndex = [];
function getAllComments(commentsColumns, commentsData){
	try{
		if ($('#allComments_dataTable').length > 0) {
			$('#allComments_dataTable').dataTable().fnDestroy(); 
		}
	} catch(e) {}
	
	allComments_oTable = $('#allComments_dataTable').dataTable( {
		 "dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		sorting:false,
		"bScrollCollapse": true,
		autoWidth: false,
		bAutoWidth:false,
		"sScrollX": "100%",
       "sScrollXInner": "100%",
       "scrollY":"100%",
       "fnInitComplete": function(data) {
    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
    	  
    	  var headerItems = $('#allComments_dataTable_wrapper tfoot tr th');
    	  
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
	  
    	  reInitializeDataTableComments();
	   },  
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'Comments',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Comments',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Comments',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        orientation: 'landscape',
	                        pageSize: 'LEGAL',
	                        footer: true
	                    },	                   
	                ],	                
	            },
	            'colvis'
         ],               
        aaData : commentsData,                 
	    aoColumns : commentsColumns,
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },
    
	});
	
	$(function(){ // this will be called when the DOM is ready 
	    
		allComments_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });
		
		$("#allComments_dataTable_length").css('margin-top','8px');
		$("#allComments_dataTable_length").css('padding-left','35px');
	}); 
}



function reInitializeDataTableComments(){
	clearTimeoutDTActivityChangeRequest = setTimeout(function(){				
		allComments_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTActivityChangeRequest);
	},200);
}
