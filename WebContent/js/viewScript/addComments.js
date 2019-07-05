var entityInstanceId; 
var AddComments = function(){	
	var initialise = function(jsonObj){
		initialiseAddCommentsValue(jsonObj);
	};	
		return {
			init : function(jsonObj){
				initialise(jsonObj);
			}
		};
}();

var jsonObjMain='';
function initialiseAddCommentsValue(jsonObj){
	jsonObjMain = jsonObj;
	selectedProductId = jsonObjMain.productId;
	currentEntityTypeId=jsonObjMain.entityTypeId;
	entityInstanceId = jsonObjMain.entityInstanceId;
	console.log(" Entity Instance Id:"+entityInstanceId);
	$("#addCommentsMainDiv h4").text(jsonObj.Title);
    displayCommentsHandler(jsonObj.effortListUrl);
    
    if($('#addComments').css('display') != "none")
    	addCommentsAjax("#activityStatus_ul", jsonObj.primaryStatusUrl);
    
    if(jsonObj.entityTypeId == 33) {
    	$('#eventEffortActualSizeDiv').show();
    } else {
    	$('#eventEffortActualSizeDiv').hide();
    }
    
    $("#comments").val('');
    $('#effort').val("");
    $('#effortCurrentStatus').text(jsonObj.currentStatusName);
    $('.statusCategory').text(""); 
    
    $('#planned_fromTodatepicker1').datetimepicker('setDate',new Date());
    $('#actualSize').val("");
}

$("#activityStatus_ul").change(function() { 
	var primaryStatusId = $("#activityStatus_ul").find(":selected").attr("id");
	console.log("Category ---> "+primaryStatusId+" : "+categories[primaryStatusId] );	
	if(typeof categories[0] != 'undefined' && categories[0] != ''){
		$('#currentStatusCategory').text(" [ "+categories[0]+" ]"); 
	}
	if(typeof categories[primaryStatusId] != 'undefined' && categories[primaryStatusId] != ''){
		$('#targetStatusCategory').text(" [ "+categories[primaryStatusId]+" ]");
	}
	//retriveSecondaryStatusHandler();
	//selectedResourcePoolId = $('select#resourcePools_dd').find('option:nth-child(0)').val();console.log(selectedResourcePoolId);//.val();
	//loadDashBoard(hdnselectedDateValue,selectedTestFactoryLab,selectedResourcePoolId);
});

var activityId='';
var reviewfilter='';
var commentsEffort="Effort";
var commentsReviewActivity="ReviewActivity";
var selectedProductId = '';
var isSave = false;
var categories = {};
var currentEntityTypeId='';
function addCommentsHandler(){
	var resultEntity='';	
	var resultModifiedBy='';	
	var resultPrimaryStatus='';	
	var resultsecondaryStatus='';	
	var resultEffort='';	
	var resultComments='';
	var actionDate='';
	var actualSize='';
	
	entityInstanceId = jsonObjMain.entityInstanceId;
	var sourceStatus = jsonObjMain.currentStatusName;
	var approveAllTaskIds = jsonObjMain.reviewItemsListsFromUI;
	var entityTypeId=jsonObjMain.entityTypeId;
	
	var urlToSave = "";
	if(typeof jsonObjMain.urlToSave != 'undefined'){
		urlToSave = jsonObjMain.urlToSave;
	}
	isSave = true;
	
	var primaryStatusId = $("#activityStatus_ul").find(":selected").attr("id");
	if(primaryStatusId != -1){
		resultPrimaryStatus = $("#activityStatus_ul").find(":selected").text();
	}
	var secondaryStatusId = '';
	//$("#secondaryStatus_ul").find(":selected").attr("id");
	/*if(secondaryStatusId != -1){
		resultsecondaryStatus = $("#secondaryStatus_ul").find(":selected").text();
	}*/
	
	resultEffort = document.getElementById("effort").value;
	if(resultEffort == 'undefined' || resultEffort == null || resultEffort == ''){
		resultEffort = 0;
	}
	resultComments = $("#comments").val();	
	if(resultComments == 'undefined' || resultComments == null){
		resultComments = '';
	}
	console.log(entityInstanceId,"--",resultEntity," -- ",resultModifiedBy," -- ",sourceStatus,"--",resultPrimaryStatus," -- ",resultsecondaryStatus," -- ",resultEffort," -- ",resultComments," -- " ,approveAllTaskIds);
	
	actionDate = $("#planned_fromTodatepicker1").val();
	if(actionDate == 'undefined' || actionDate == null){
		actionDate = '';
	}
	
	actualSize=$("#actualSize").val();	
	if(actualSize == 'undefined' || actualSize == null || actualSize == ''){
		actualSize = 0;
	}
	
	if(typeof urlToSave != 'undefined' && urlToSave != null && urlToSave != ''){
		approveAllTaskIds = entityInstanceId;
		if(typeof secondaryStatusId == 'undefined' || secondaryStatusId == ''){
			secondaryStatusId = primaryStatusId;
		}
		urlToSave = urlToSave.replace("[primaryStatusId]", primaryStatusId);
		urlToSave = urlToSave.replace("[secondaryStatusId]", secondaryStatusId);
		urlToSave = urlToSave.replace("[effort]", resultEffort);
		urlToSave = urlToSave.replace("[comments]", resultComments);
		urlToSave = urlToSave.replace("[approveAllEntityIds]", approveAllTaskIds);
		urlToSave = urlToSave.replace("[attachmentIds]", attachmentIds);
		urlToSave = urlToSave.replace("[actionDate]", actionDate);
		urlToSave = urlToSave.replace("[actualSize]", actualSize);
		
		console.log("urlToSave -->> "+urlToSave);
		
		if(entityInstanceId != -1 && resultEntity!=null && resultPrimaryStatus!=null && resultsecondaryStatus!=null && resultEffort!=-1 && resultComments!=null  ){
			$("#addCommentsCancel").trigger('click');
			
			activityId=document.getElementById("treeHdnCurrentActivityId").value;
			reviewfilter = $("#reviewFilter_ul").find('option:selected').val();        
			productId=document.getElementById("treeHdnCurrentProductId").value;
			productVersionId=document.getElementById("treeHdnCurrentProductVersionId").value;
			productBuildId=document.getElementById("treeHdnCurrentProductBuildId").value;
			activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
			addCommentsAjax("save", urlToSave);			
			
		}else if(entityInstanceId == -1 && resultPrimaryStatus!=null && resultsecondaryStatus!=null && resultEffort!=-1 && resultComments!=null  ){ 
			$("#addCommentsCancel").trigger('click');
			addCommentsAjax("save", urlToSave);
			
		}else{
			callAlert("Please Fill The Mandatory Fields");				
		}
	}else{
		callAlert("Please provide url to save");	
	}
	
}

function reloadActivityDataTableHandler(){	
	var entityTypeId=jsonObjMain.entityTypeId;
	activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
	
	if(jTableContainerDataTable == "assigneeActivitiesContainer") {
		level =0;		
		url = 'my.activity.list?testFactoryId='+testFactoryId+'&productId='+currentProductId+'&activityWorkPackageId='+activityWorkPackageId+'&tabValue='+level+'&jtStartIndex=0&jtPageSize=1000';
		assignDataTableValuesInActivityManagementTab(url, "160px", "parentTable", 0, currentProductId );
		
	}else if(jTableContainerDataTable == "pendingWithActivitiesContainer"){
		 level = 1;
		url = 'my.activity.list?testFactoryId='+testFactoryId+'&productId='+currentProductId+'&activityWorkPackageId='+activityWorkPackageId+'&tabValue='+level+'&jtStartIndex=0&jtPageSize=1000';
		assignDataTableValuesInActivityManagementTab(url, "160px", "parentTable", 0, currentProductId );
		
	}else if(jTableContainerDataTable == "passingThroughContainer" ){
		level = 2;
		url = 'my.activity.list?testFactoryId='+testFactoryId+'&productId='+currentProductId+'&activityWorkPackageId='+activityWorkPackageId+'&tabValue='+level+'&jtStartIndex=0&jtPageSize=1000';
		assignDataTableValuesInActivityManagementTab(url, "160px", "parentTable", 0, currentProductId );
		
	}else {
		if((jsonObjMain.componentUsageTitle !=undefined && jsonObjMain.componentUsageTitle == "activity")&& entityTypeId == 33 ){
			var activityWPId=0;
			var enableAddOrNot="yes";
			document.getElementById("treeHdnCurrentProductId").value = productId;
			listActivitiesOfSelectedAWP_DT(0,0,0,activityWorkPackageId,1,enableAddOrNot, jTableContainerDataTable);
		}
	}
}

function getActivityListyByActivityIDHandler(){

	if(currentEntityTypeId == 33) {
		var getActivityLength = activityManagementTab_oTable.fnGetData().length;
		for(var i=0;i<getActivityLength;i++){
			if(jsonObjMain.entityInstanceId == activityManagementTab_oTable.fnGetData(i).activityId){
				
				var resultPrimaryStatus='';
				var primaryStatusId = $("#activityStatus_ul").find(":selected").attr("id");
				if(primaryStatusId != -1){
					resultPrimaryStatus = $("#activityStatus_ul").find(":selected").text();
				}
				
				var resultActivitySingleObj='';
				resultActivitySingleObj = activityManagementTab_oTable.fnGetData(i);
				resultActivitySingleObj['statusId'] = primaryStatusId;
				resultActivitySingleObj['statusName'] = resultPrimaryStatus;	
				
				resultActivitySingleObj['oldFieldID'] = '';
				resultActivitySingleObj['modifiedFieldID'] = 'statusId';
				resultActivitySingleObj['oldFieldValue'] = '';
				resultActivitySingleObj['modifiedFieldValue'] = '';
				resultActivitySingleObj['modifiedField'] = 'statusId';
				resultActivitySingleObj['modifiedFieldTitle'] = '';				
				updataActivityHandler(resultActivitySingleObj, activityManagementTab_oTable.fnGetData(i).activityId);
				
				break;
			}
		}
	} else {
		productId=selectedProductId;
		features();
	}
}

function updataActivityHandler(allData, entityInstanceId){
	var fd = new FormData();
	var getKey, getValue='';
	for(var key in allData){	
		getKey = key.toString();
		getValue = allData[key];
		fd.append(getKey, getValue);
	}
	
	var dateFlag=false;
	//var activityUpdateUrl = "process.activity.update";
	var activityUpdateUrl = "process.get.activity.by.id?activityId="+entityInstanceId;
	$.ajax({
		url : activityUpdateUrl,
		data : fd,
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != '' || data.Message == "INFORMATION"){
				callAlert(data.Message);
				
				if(jsonActivityUpdateData.modifiedField == "plannedStartDate"){
					jsonActivityUpdateData = updateOnActivityList(jsonActivityUpdateData.activityId);
					document.getElementById("activityPlannedStartDate").value = jsonActivityUpdateData.oldFieldValue;
					$("#activityPlannedStartDate").datepicker('setDate', jsonActivityUpdateData.oldFieldValue);
					$(".datepicker").hide();
					dateFlag=true;
				
				}else if(jsonActivityUpdateData.modifiedField == "plannedEndDate"){
					jsonActivityUpdateData = updateOnActivityList(jsonActivityUpdateData.activityId);
					document.getElementById("activityPlannedEndDate").value = jsonActivityUpdateData.oldFieldValue;
					$("#activityPlannedEndDate").datepicker('setDate', jsonActivityUpdateData.oldFieldValue);
					$(".datepicker").hide();
					dateFlag=true;
				
				}else if(jsonActivityUpdateData.modifiedField == "actualStartDate"){
					jsonActivityUpdateData = updateOnActivityList(jsonActivityUpdateData.activityId);
					document.getElementById("activityActualStartDate").value = jsonActivityUpdateData.oldFieldValue;
					$("#activityActualStartDate").datepicker('setDate', jsonActivityUpdateData.oldFieldValue);
					$(".datepicker").hide();
					dateFlag=true;
				
				}else if(jsonActivityUpdateData.modifiedField == "actualEndDate"){
					jsonActivityUpdateData = updateOnActivityList(jsonActivityUpdateData.activityId);
					document.getElementById("activityActualEndDate").value = jsonActivityUpdateData.oldFieldValue;
					$("#activityActualEndDate").datepicker('setDate', jsonActivityUpdateData.oldFieldValue);
					$(".datepicker").hide();
					dateFlag=true;
				}
			};
			
			if(jsonActivityUpdateData.modifiedField == "plannedStartDate"){				
				$(".datepicker").hide();				
			
			}else if(jsonActivityUpdateData.modifiedField == "plannedEndDate"){				
				$(".datepicker").hide();
			
			}else if(jsonActivityUpdateData.modifiedField == "actualStartDate"){				
				$(".datepicker").hide();
			
			}else if(jsonActivityUpdateData.modifiedField == "actualEndDate"){
				$(".datepicker").hide();
			}
			
			if(!dateFlag){
				reloadActivityDataTableHandler();
			}
		},
		error : function(data) {
			console.log("error in ajax call");
		},
		complete: function(data){
			//console.log("complete in ajax call");
		},
	});	
}


/*function retriveSecondaryStatusHandler(){
	var primaryStatus = $("#activityStatus_ul").find(":selected").attr("id");
	if(primaryStatus != -1){
		resultPrimaryStatus = parseInt($("#activityStatus_ul").find(":selected").attr('id'));
		console.log(resultPrimaryStatus," -- ",primaryStatus);
	    addCommentsAjax("#secondaryStatus_ul",'activity.secondary.status.master.option.list?statusId='+(resultPrimaryStatus));
	}else{
		callAlert("Please select the primary status");				
	}
}*/

function addCommentsMainDivClose(){
	removeAttachmentsOnCancel();
	$("#addCommentsMainDiv").fadeOut("normal");
}

function closeAddCommentsPopup(){
	removeAttachmentsOnCancel();
	popupClose();
}

function popupClose() {	
	if(jsonObjMain.isFromMyActions && isSave){
		refreshTabCountAndContent(jsonObjMain.entityTypeId);
	}
	$("#div_PopupMain").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

function removeAttachmentsOnCancel(){
	if(typeof attachmentIds != undefined && attachmentIds.length > 0 && !isSave){
		$.ajax({
			type: "POST",
			contentType: "application/json; charset=utf-8",
			url : 'delete.attachment.for.multiple.entity.or.instance?attachmentIds='+attachmentIds,
			dataType : 'json',
			success : function(data) {
						   
			},
			error: function(data){
				
			},

			complete: function(data){
				
			},
		});
	}
}

function viewCommentsHandler(){
	if($("#addComments").css('display') == "none"){
		displayComments(true);		
	}else{
		displayComments(false);		
	}	
}

function viewCommentsClose(){
	displayComments(true);
}

function displayComments(flag){
	if(flag){
		$("#addComments").show();
		$("#viewComments").hide();
		$("#addCommentsMainDiv .modal-header .actions").attr('title','Add Comments');
		
	}else{
		$("#addComments").hide();
		$("#viewComments").show();		
		$("#addCommentsMainDiv .modal-header .actions").attr('title','Existing Comments');
	}	
}

function addCommentsAjax(IDValue, url){
	$("#addCommentsLoaderIcon").show();
	openLoaderIcon();
	
	 $.ajax({
      type: "POST",
	  contentType: "application/json; charset=utf-8",
      url : url,
      dataType : 'json',
      success : function(data) {
    	  closeLoaderIcon();
    	  
           if(IDValue == "save"){
        	   getActivityListyByActivityIDHandler();
        	   $("#addCommentsLoaderIcon").hide();
        	   
        	  if(jsonObjMain.commentsName == commentsEffort){        	  
        	  }else if(jsonObjMain.commentsName == commentsReviewActivity){
        	  }
        	  
           }else if(data.Result=="OK" && typeof data.Records != 'undefined' && data.Records.length>0){        	   
				dropDownHandler(IDValue, data.Records, "Records");
				
		   }else if(data.Result=="OK" && typeof data.Options != 'undefined' && data.Options.length>0){        	   
				dropDownHandler(IDValue, data.Options, "Options");
		   }	
      },
      error: function(data){
    	   closeLoaderIcon();
		   console.log("error in ajax call");		  
           $("#addCommentsLoaderIcon").hide();		   
      },      
      complete: function(data){
    	  closeLoaderIcon();
    	  	if(jsonObjMain.isFromMyActions){
  				refreshTabCountAndContent(jsonObjMain.entityTypeId);
  			}
    	  	$("#addCommentsLoaderIcon").hide();
      	},
	 });
		
}

var activityAddCommentsTab_oTable='';
var clearTimeoutDTActivityAddCommentsTab='';
var containerAddCoommentsID='jTableContainerComments';
function reInitializeDTActivityAddCommentsTab(){
	clearTimeoutDTActivityAddCommentsTab = setTimeout(function(){				
		activityAddCommentsTab_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTActivityAddCommentsTab);
	},200);
}

function activityAddCommentsTabHeader(){
	var childDivString = '<table id="activityAddCommentsTab_'+containerAddCoommentsID+'_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Id</th>'+
			'<th>From Status</th>'+
			'<th>To Status</th>'+			
			'<th>Effort(in Hrs)</th>'+	
			'<th>Action By</th>'+
			'<th>Action Date</th>'+
			'<th>Comments</th>'+
			'<th>SLA Planned(Hrs)</th>'+
			'<th>Actual Size</th>'+
			'<th>SLA Actual(Hrs)</th>'+		
			'<th></th>'+			
			'<th></th>'+			
		'</tr>'+
	'</thead>'+
	'<tfoot>'+
		'<tr></tr>'+
	'</tfoot>'+
	'</table>';		
	
	return childDivString;
}

function displayCommentsHandler(effortList){
	var arr = effortList;
	var tempStr = '';
	for(var i=0;i<arr.length;i++){
		tempStr += arr[i].comment;
		tempStr +='\n';
	}
	$("#viewComments textArea").text(tempStr);
	
	$("#addCommentsLoaderIcon").show();
	openLoaderIcon();
	
	 $.ajax({
      type: "POST",
	  contentType: "application/json; charset=utf-8",
      url : effortList,
      dataType : 'json',
      success : function(data) {
    	  closeLoaderIcon();
		  $("#addCommentsLoaderIcon").hide();
		  displayCommentsResultHandler(data.Records);
    	  	
      },
      error: function(data){
    	   closeLoaderIcon();
		   $("#addCommentsLoaderIcon").hide();
      },      
      complete: function(data){
    	  closeLoaderIcon();
		  $("#addCommentsLoaderIcon").hide();
    	  	
      	},
	 });		
}

function displayCommentsResultHandler(data){	
	try{
		if ($("#"+containerAddCoommentsID).children().length>0) {
			$("#"+containerAddCoommentsID).children().remove();
		}
	} 
	catch(e) {}
	
	var emptytr = emptyTableRowAppending(12);  // total coulmn count		  
	var childDivString = activityAddCommentsTabHeader(); 			 
	$("#"+containerAddCoommentsID).append(childDivString);
	
	$('#'+containerAddCoommentsID+' tfoot tr').html('');     			  
	$('#'+containerAddCoommentsID+' tfoot tr').append(emptytr);
	
	activityAddCommentsTab_oTable = $('#activityAddCommentsTab_'+containerAddCoommentsID+'_dataTable').dataTable( {
		 	dom: "Bfrtilp",
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
			"sScrollX": "90%",
			"sScrollXInner": "100%",
			"scrollY":"100%",
			//"bSort": true,
			"bScrollCollapse": true,
			 "aaSorting": [[0,'desc']],
	       "fnInitComplete": function(data) {
			   
			   var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position			   
     		  $('#activityAddCommentsTab_'+containerAddCoommentsID+'_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			   
			   reInitializeDTActivityAddCommentsTab();			   
		   },  
		   select: true,
		   buttons: [	             	
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "Workflow Events",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "Workflow Events",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "Workflow Events",
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

	        { mData: "workflowEventId",className: 'disableEditInline', sWidth: '10%' },
           { mData: "currentStatusName",className: 'disableEditInline', sWidth: '10%' },
		   { mData: "targetStatusName",className: 'disableEditInline', sWidth: '10%' },		   
		   { mData: "actualEffort",className: 'disableEditInline', sWidth: '5%' },		   			
           { mData: "modifiedByName",className: 'disableEditInline', sWidth: '10%' },
           { mData: "lastUpdatedDate",className: 'disableEditInline', sWidth: '10%' }, 
			{ mData: "comments",className: 'disableEditInline', sWidth: '10%' },
		   { mData: "slaDurationPlanned",className: 'disableEditInline', sWidth: '5%' },		   
		   { mData: "actualSize",className: 'disableEditInline', sWidth: '5%' },		   			
           { mData: "slaDurationActual",className: 'disableEditInline', sWidth: '5%' },
           { mData: "slaRAG",className: 'disableEditInline', sWidth: '5%' }, 
			{ mData: null,				 
            	bSortable: false,
				sWidth: '5%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+
     	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<img src="css/images/attachment.png" class="activityAddCommentsTabImg1" title="Attachment" style="width: 18px;height: 18px;">&nbsp;['+data.attachmentCount+']&nbsp;</img></button>'+   					
     	       		'</div>');	      		
           		 return img;
            	}
            },
       ],		 
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready 
		  
		 $('#activityAddCommentsTab_'+containerAddCoommentsID+'_dataTable_length').css('margin-top','8px');
		 $('#activityAddCommentsTab_'+containerAddCoommentsID+'_dataTable_length').css('padding-left','35px');
		 
		 $('#activityAddCommentsTab_'+containerAddCoommentsID+'_dataTable tbody').on('click', 'td button .activityAddCommentsTabImg1', function () {
				var tr = $(this).closest('tr');
			    var row = activityAddCommentsTab_oTable.DataTable().row(tr);
			    
			    isViewAttachment = false;
				var jsonObj={"Title":"Attachments for Activity",			          
	    			"SubTitle": 'Activity : ['+row.data().workflowEventId+'] '+row.data().currentStatusName,
	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=32&entityInstanceId='+row.data().workflowEventId,
	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=32&entityInstanceId='+row.data().workflowEventId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
	    			"updateURL": 'update.attachment.for.entity.or.instance',
	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=32',
	    			"multipleUpload":true,
	    		};	 
  		Attachments.init(jsonObj);
			    
		 });
	 				 		 
		 activityAddCommentsTab_oTable.DataTable().columns().every( function () {
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
}

/*
function displayCommentsHandler(effortList){		  
	var arr = effortList;
	var tempStr = '';
	for(var i=0;i<arr.length;i++){
		tempStr += arr[i].comment;
		tempStr +='\n';
	}
	$("#viewComments textArea").text(tempStr);
	
	try {
		if ($('#jTableContainerComments').length > 0) {
			$('#jTableContainerComments').jtable('destroy');
		}
	} catch (e) {
	}
		
	$('#jTableContainerComments').jtable({
		title: 'Workflow Events',
        selecting: true,  //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, 
          actions: {
       	 	listAction: effortList,
        },        
	fields: {	 
		workflowEntityEffortTrackerId: { 					                   							
			title : 'EffortTrackerId',			
			create: false, 
			edit: false, 
			list: false
		},
		entityId: { 					                   							
			title : 'Entity ID',					                   							
			create: true, 
			edit: false, 
			list: false
		},
		entityTypeName : {
			title : 'Entity Type',					    															    										
			list : false,
			create : true,
			edit : false,
			width : "5%"			
		},
		currentStatusName : {
			title : 'From Status',
			create : true,
			list : true,
			edit : false,
			width : "20%"			
		},
		targetStatusName : {
			title : 'To Status',
			create : true,
			list : true,
			edit : false,
			width : "20%",
		},
		actualEffort : {
			title : 'Effort(in Hrs)',
			create : true,
			list : true,
			edit : false,
			width : "10%",
		},
		modifiedByName : {
			title : 'Action By',					    										
			list : true,
			create : true,
			edit : false,
			width : "15%",
		},
		lastUpdatedDate : {
			title : 'Action Date',					    										
			edit : false,
			list : true,
			create : true,
			width : "15%",
		},
		comments:{
			title : 'Comments',					    										
			edit : false,
			list : true,
			create : true,
			width : "25%",
		},
		slaDurationPlanned:{
			title : 'SLA Planned(Hrs)',					    										
			edit : false,
			list : true,
			create : true,
			width : "5%",
		},
		actualSize:{
			title : 'Actual Size',					    										
			edit : false,
			list : true,
			create : true,
			width : "5%",
		},
		slaDurationActual:{
			title : 'SLA Actual(Hrs)',					    										
			edit : false,
			list : true,
			create : true,
			width : "5%",
		},
		slaRAG:{
			title : '',					    										
			edit : false,
			list : true,
			create : true,
			width : "5%",
		},
		attachment: { 
			title: '', 
			list: true,
			width: "5%",
			display:function (data) {	        		
				var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>'); 
       			$img.click(function () {
       				isViewAttachment = true;
       				var jsonObj={"Title":"Attachments for Workflow Event",			          
    	    			"SubTitle": 'Workflow Event  : ['+data.record.workflowEntityEffortTrackerId+'] of ['+jsonObjMain.entityInstanceId+'] '+jsonObjMain.entityInstanceName,
    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+selectedProductId+'&entityTypeId=32&entityInstanceId='+data.record.workflowEntityEffortTrackerId,
    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+selectedProductId+'&entityTypeId=32&entityInstanceId='+data.record.workflowEntityEffortTrackerId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
    	    			"updateURL": 'update.attachment.for.entity.or.instance',
    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=32',
    	    			"multipleUpload":true,
    	    		};	 
	        		Attachments.init(jsonObj);
       		  });
       			return $img;
        	}				
		}, 
		auditionHistory:{
			title : 'Audit History',
			list : false,
			create : false,
			edit : false,
			width: "10%",
			display:function (data) { 
				var $img = $('<i class="fa fa-search-plus" title="Audit History"></i>');
				$img.click(function () {						
					listWorkFlowEventsAuditHistory(data.record.workflowEntityEffortTrackerId);
				});
				return $img;
		 }
		}, 
		
	},  
		});
	$('#jTableContainerComments').jtable('load');
}
*/

function listWorkFlowEventsAuditHistory(workflowEntityEffortTrackerId){
	clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=WorkFlowEvents&sourceEntityId='+workflowEntityEffortTrackerId;
	var jsonObj={"Title":"WorkFlowEvents Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10,	    
			"componentUsageTitle":"workFlowEventsAudit",
	};
	SingleJtableContainer.init(jsonObj);
}

function dropDownHandler(itemID, item, type){		
	$(itemID).empty();	
	
	if(type == "Records"){
		for(var i=0; i<item.length; i++){
			$(itemID).append('<option id='+item[i].Id+' value='+item[i].Value+'><a href="#">'+item[i].DisplayText+'</a></option>');
			categories[item[i].Id] = item[i].Category;
	 	}
	}else if(type == "Options"){
		for(var i=0; i<item.length; i++){
			$(itemID).append('<option id='+item[i].Value+' value='+item[i].DisplayText+'><a href="#">'+item[i].DisplayText+'</a></option>');
	 	}
	}
	
	if(typeof categories[0] != 'undefined' && categories[0] != ''){
		$('#currentStatusCategory').text(" [ "+categories[0]+" ]"); 
	}
	if(typeof item[0] != 'undefined' && typeof item[0].Id != 'undefined' && typeof categories[item[0].Id] != 'undefined' && categories[item[0].Id] != ''){
		$('#targetStatusCategory').text(" [ "+categories[item[0].Id]+" ]");
	}
	
	 $(itemID).select2();
	 
	 if(itemID == "#entity_ul"){
		dispalyDefaultValue(itemID,jsonObjMain.entityTypeDefault);			 
	 
	 }
	 else if(itemID == "#activityStatus_ul"){
		dispalyDefaultValue(itemID,jsonObjMain.activityStatusDefault);
		var primaryStatus = $("#activityStatus_ul").find(":selected").attr("id");
		if(primaryStatus != -1){
			resultPrimaryStatus = parseInt($("#activityStatus_ul").find(":selected").attr('id'));
			console.log(resultPrimaryStatus," -- ",primaryStatus);
			jsonObjMain.secondaryStatusId=resultPrimaryStatus;
		}
	 }
	 else if(itemID == "#secondaryStatus_ul"){
			dispalyDefaultValue(itemID,jsonObjMain.secondaryStatusId);
	}
}

function dispalyDefaultValue(itemID, value){
	
	 var selectedValue = parseInt(value);
	 var itemIDStr = itemID+'_'+selectedValue;	 
	 
	 var itemArray = $(itemID).find('option');
	for(var i=0; i< itemArray.length; i++){ 
	  if(value == itemArray[i].id){			  
		  $(itemID).select2("val",$(itemID).find('option')[i].value);
		  break;
	  }
	} 
}

function addAttachment(){
	isViewAttachment = false;
	var jsonObj={"Title":"Attachments for Workflow Event",			          
		"SubTitle": 'Workflow Event  : [New] of ['+jsonObjMain.entityInstanceId+'] '+jsonObjMain.entityInstanceName,
		"listURL": 'attachment.for.entity.or.instance.list?productId='+selectedProductId+'&entityTypeId=32&entityInstanceId=0',
		"creatURL": 'upload.attachment.for.entity.or.instance?productId='+selectedProductId+'&entityTypeId=32&entityInstanceId=0&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
		"deleteURL": 'delete.attachment.for.entity.or.instance',
		"updateURL": 'update.attachment.for.entity.or.instance',
		"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=32',
		"multipleUpload":true,
	};	 
	Attachments.init(jsonObj);
}


/*function updateEffortHandler(){
	entityInstanceId = jsonObjMain.entityInstanceId;
	var entityTypeId=jsonObjMain.entityTypeId;
	var resultEffort = document.getElementById("effort").value;
	if(resultEffort == 'undefined' || resultEffort == null || resultEffort == ''){
		resultEffort = 0;
	}
	var url="workflow.event.tracker.update.by.eventId";
	
	$.post(url+$'?entityInstanceId='+entityInstanceId+'&entityTypeId='+entityTypeId+'&effort='+resultEffort,function(data) {
		if(data.Result=="SUCCESS"){
			callAlert(data.Message);
		}
		else if(data.Result=="ERROR"){
			callAlert(data.Message);
		}
	});
	
	$("#addCommentsMainDiv").modal('hide');
	
}*/

function updateEffortHandler(){
	entityInstanceId = jsonObjMain.entityInstanceId;
	var entityTypeId=jsonObjMain.entityTypeId;
	var resultEffort = document.getElementById("effort").value;
	if(resultEffort == 'undefined' || resultEffort == null || resultEffort == ''){
		resultEffort = 0;
	}
	var url="workflow.event.tracker.update.by.eventId";
	var fd = new FormData();
	fd.append("entityInstanceId", entityInstanceId);
	fd.append("entityTypeId", entityTypeId);
	fd.append("effort", resultEffort);
	
	openLoaderIcon();
	$.ajax({
		url : url,
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
	reloadActivityDataTableHandler();
	$("#addCommentsMainDiv").modal('hide');
}
