var CommentsMetronicsUI = function() {
  
   var initialise = function(jsonObj){
	   assignMetronicsCommentsUI(jsonObj);
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();
var editorComments;
var instanceId = 0;
var jsonObjtoReload ;
var productId=0;

//var singleDataTableJsonObject='';
function assignMetronicsCommentsUI(jsonObj){
	jsonObjtoReload = jsonObj;
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:jsonObj.url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				instanceId = jsonObj.entityInstanceId;
				entityTypeId = jsonObj.entityTypeId;
				productId=jsonObj.productId;
				jsonObj.TotalRecordCount = data.TotalRecordCount;
				data = data.Records;				
				//jsonObj.data = data;
				jsonObj.data = data.reverse();
				metronicsCommentsUI(jsonObj);
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

function metronicsCommentsUI(jsonObj){	
//	$("#div_SingleDataTableSummary").find("h4").text(jsonObj.Title);
	$("#div_CommentsUI").find("h4").text(jsonObj.Title);
	var compUsageTitle = jsonObj.componentUsageTitle;
	if(compUsageTitle == "productComments"){		
		//commentsListDT_Container(jsonObj);	
		//add method to display
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "productVersionComments"){		
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "productBuildComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "testFactoryLevelAWPComments" || compUsageTitle == "productLevelAWPComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "productLevelclarificationTrackerComments" || compUsageTitle == "awpLevelclarificationTrackerComments" || compUsageTitle == "activityLevelCTComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "productLevelCRComments" || compUsageTitle == "awpLevelCRComments" || compUsageTitle == "activityLevelCRComments" || compUsageTitle == "CRComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "activityComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "wflowComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "activityTaskComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "activityLevelWFStatusComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "actLevelTaskComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "tCaseComments" || compUsageTitle == "tStepsComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "featureComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "DCComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "environmentComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "tSuiteComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "tfCoreComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "riskComments" || compUsageTitle == "riskSeverityComments" ){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "riskLikeHoodComments" || compUsageTitle == "riskMitigationComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "riskRatingComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "attachComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "trpComments" || compUsageTitle == "trpGroupComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "testMSComments" || compUsageTitle == "defectMSComments" || compUsageTitle == "SCMComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "customerComments" ){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "dimensionComments" || compUsageTitle == "prodCompetencyComments" || compUsageTitle == "teamCompetencyComments"){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "extractorComments" ){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "actTypesComments" ){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "actTaskTypesComments" ){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "customFieldsComments" ){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "workFlowComments" ){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "workFlowEntMapComments" ){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "workPackageComments" ){
		listingCommentsMetronicsUI(jsonObj);
	}	
	else if(compUsageTitle == "testRunJobComments" ){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "testrunplanComments" ){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "reportIssueComments" ){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "attachmentComments" ){
		listingCommentsMetronicsUI(jsonObj);
	}
	else if(compUsageTitle == "testCaseExecutionResultComments" ){
		listingCommentsMetronicsUI(jsonObj);
	}	
	else{
		console.log("add custom Jtable");
	}				
	$("#div_CommentsUI").modal();	
	$("#div_CommentsUI").find('.btn-circle').css("display","block");
	
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
		    		assignMetronicsCommentsUI(jsonObjtoReload);
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

function commentsDataTable(){
	var childDivString = '<table id="comments_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			/*'<th class="dataTableChildHeaderTitleTH">CommentsId</th>'+
			'<th class="dataTableChildHeaderTitleTH">Entity</th>'+
			'<th class="dataTableChildHeaderTitleTH">EntityId</th>'+*/
			'<th class="dataTableChildHeaderTitleTH">Comments</th>'+
			'<th class="dataTableChildHeaderTitleTH">CreatedBy</th>'+
			'<th class="dataTableChildHeaderTitleTH">CreatedOn</th>'+
		'</tr>'+
	'</thead>'+
	'</table>';		
	
	return childDivString;	
}

function commentsListDT_Container(jsonObj){	
	
	
	try{
		if ($("#dataTableSingleContainer").children().length>0) {
			$("#dataTableSingleContainer").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = commentsDataTable(); 			 
	$("#dataTableSingleContainer").append(childDivString);
	 
	editorComments = new $.fn.dataTable.Editor( {
	    "table": "#comments_dataTable",
		ajax: "comments.for.entity.or.instance.add?productId=0&entityTypeId="+jsonObj.entityTypeId+"&entityInstanceId="+jsonObj.entityInstanceId,
	//	ajax: "comments.for.entity.or.instance.add",
		//ajaxUrl: "administration.product.update",
	   idSrc:  "commentId",
		i18n: {
	        create: {
	            title:  "Post Comments",
	            submit: "Create",
	        }
	    },
		fields: [{
            label: "commentsText",
            name: "commentsText",
           
        },/*{            
            name: "commentId",
            "type": "hidden",
        },{            
            name: "entityMasterName",
            "type": "hidden",
        },{            
            name: "entityPrimaryId",
            "type": "hidden",
           
        },*/{ 
        	label: "CreatedBy:",
            name: "createrName", 
            "type": "hidden",
        },{            
            name: "createdDate",
            "type": "hidden",
            
        },/*,{
            label: "Product",
            name: "productId",
            "type": "hidden",
            "default": 0,
        }, */       
    ]
	});
	
	$( 'input', editorComments.node()).on( 'focus', function () {
		this.select();
	});
	
	commentsListSingleDT_oTable = $("#comments_dataTable").dataTable( {
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
		   buttons: [{ extend: "create", editor: editorComments },
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
       ],      
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
}


//----- Comments Ended -----





var commentsData = {};
function getCommentsOfEntity(engId,prId,awpId,entityTypeId){
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
	
	var allComments_dtColumns = ['Activity Id', 'Activity Name', 'Comments', 'Commented By', 'Commented Date'];
	
	var allCommentsTable = '<table id="allComments_dataTable"  class="cell-border compact row-border" cellspacing="0" width="100%">';
	var allCommentsTable_thead = '<thead><tr>';
	var allCommentsTable_tfoot = '<tfoot><tr id="allComments_dataTable_filterRow">';
	$.each(allComments_dtColumns, function(index, value){
		allCommentsTable_thead += '<th class="sorting">'+value+'</th>';
		allCommentsTable_tfoot += '<th></th>';
	});
	allCommentsTable += allCommentsTable_thead + '</tr></thead>'+ allCommentsTable_tfoot + '</tr></tfoot></table>';
	detailedCommentsContainer.append(allCommentsTable);
		$("#div_allCommentsPopup").modal();
}

function getCommentsColumnMappings(){
	var allCommentsColumns = [];
	allCommentsColumns.push({ mData: "entityPrimaryId",className: 'disableEditInline', sWidth: '0.5%',"render": function (tcData,type,full) {	        
 		var entityInstanceId = full.entityPrimaryId;					
 		return ('<a onclick="displayTabActivitySummaryHandler('+entityInstanceId+')">'+full.entityPrimaryId+'</a>');		        
	},});
	allCommentsColumns.push({ mData: "entityName",className: 'disableEditInline', sWidth: '2%'});
	allCommentsColumns.push({ mData: "commentsText",className: 'disableEditInline', sWidth: '8%'});
	allCommentsColumns.push({ mData: "createrName", className: 'disableEditInline', sWidth: '0.5%'});
	allCommentsColumns.push({ mData: "createdDate", className: 'disableEditInline',  sWidth: '0.5%'});
	
	allCommentsColumsAvailableIndex = [0, 1, 2, 3,4];
	
	
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
