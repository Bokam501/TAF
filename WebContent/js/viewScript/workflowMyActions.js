var newnodetype = "";
var currentProductId = "";
var isFirstLoad = true;
var selectedTab = 0;
var raisedby = $("#userdisplayname").text().split('[')[0].trim();
var entityTypes=[33,34,30,3];
jQuery(document).ready(function() {	
	
	QuickSidebar.init(); // init quick sidebar
	setBreadCrumb(userRole);
	createHiddenFieldsForTree();
	setPageTitle("My Actions");
	$('#treeStructure-portlet-light .portlet-title .tools').css('margin-right','12px');
	getTreeData('workflow.my.actions.product.tree');
	setDefaultnode("j1_1");
	$("#addCommentsDateTimePickerBox").show();
	
	$("#treeContainerDiv").on("select_node.jstree",
	     function(evt, data){		
			if(data.node != undefined){
				
	   			var entityIdAndType =  data.node.data;
	   			var arry = entityIdAndType.split("~");
	   			var key = arry[0];
	   			var type = arry[1];
	   			var name = arry[2];
			    var nodeType = type;
			    var parent = data.node.original.parent;
			    var loMainSelected = data;
		        uiGetParents(loMainSelected);
		        
		        if(nodeType == 'Product' || nodeType == 'product'){
		        	currentProductId = key;		        	
		        	//  ----- Commented code: getting total count url is taking more time than need to be fixed once done this code will  be reverted back
		        	$('.pendingActionCount').text(0);
		        	$.each(entityTypes, function(index, value){
		        		getWorkflowMyActionCounts(currentProductId, value);
		        	});
		        }
		        
				if(parent == "#"){
					//urlToCompetencyDetails = 'dimension.list.by.status?status='+id+'&dimensionTypeId=1';
					newnodetype = nodeType;
					//listCompetencies();
					setDefaultnode(data.node.id);
				}else{
					if(name.toLowerCase() != "no competency available"){
						selectedCompetency = key;
						$('#tabslistWorkflowActions li').first().find("a").trigger("click");
						$("#workflowActionsTabs").show();
						$("#tbCntnt").show();
					}
				}
			}
		}
	);
	
});

$(document).on('click', '#tabslistWorkflowActions>li', function(){
	selectedTab=$(this).index();
	workflowActionsTabSelection(selectedTab);
});


function workflowActionsTabSelection(selectedTab){
	if (selectedTab == 0){
		//listActivities();				
		var url = 'workflow.activities.pending.my.action?productId='+currentProductId+'&entityLevel=Product&entityLevelId=33&jtStartIndex=0&jtPageSize=10000';
		assignDataTableValues(url, "parentTable", "", "");
		
	}else if (selectedTab == 1){
		listActivityWorkpackages();
	}else if (selectedTab == 2){
		listTask();
	}else if (selectedTab == 3){
		listTestCase();
	}
}

function listTask(){
	
	try{
		if ($('#jTableContainerTask').length>0) {
			$('#jTableContainerTask').jtable('destroy'); 
		}
	}catch(e){}
	
	 //init jTable
	 $('#jTableContainerTask').jtable({
         title: 'Tasks',
         selecting: true, //Enable selecting 
         paging: true, //Enable paging
         pageSize: 10, //Set page size (default: 10)
         editinline:{enable:true},         
         actions: {
             listAction: 'workflow.tasks.pending.my.action?productId='+currentProductId+'&entityLevel=Product&entityLevelId=30',
         },
         fields: {
         	activityTaskId : {
         		title: 'Task Id',
 				key : true,
 				list : true,
 				edit:false,
 				create : false
 			},									
 			activityTaskName:{
 	             title: 'Task Name',
 	          	 list:true,
 	           	 edit:false,
 	           	 create:false,
 	             width:"20%"
 	        },
 	       activityName:{
	             title: 'Activity Name',
	          	 list:true,
	           	 edit:false,
	           	 create:false,
	             width:"20%"
	        },
	        activityWorkpackageName:{
	             title: 'Workpackage Name',
	          	 list:true,
	           	 edit:false,
	           	 create:false,
	             width:"20%"
	        },
         	activityTaskTypeId : {
				title : 'Task Type',
				create : false,
				edit : false,
				width : "20%",
				options : 'common.list.activity.tasktype?productId='+currentProductId,
			},			
			statusDisplayName : {
				title : 'Current Status',
				create : false,
				list : true,
				edit : false,
				width : "20%",
				display: function(data){
					return data.record.statusDisplayName+"&nbsp;"+data.record.workflowStatusCategoryName;
				}
			},
			actors : {
				title : 'Pending With',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},
			completedBy : {
				title : 'Complete By',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},
			remainingHrsMins : {
				title : 'Time Left',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},			 
			workflowIndicator : {
				title : '',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},
			totalEffort : {
	        	title : 'Total Effort',
	        	create : false,
	        	edit : false,
	        	width : "20%",
	        	
	         },
			workflowEntityEffortTrackerId:{
	        	title : '',
	        	list : true,
	        	create : false,
	        	edit : false,
	        	width: "10%",
	        	display:function (data) { 
	           		//Create an image for test script popup 
					var $img = $('<i class="fa fa-th" title="Event tracker"></i>');
           			//Open Testscript popup  
           			$img.click(function () {
           				//displayTestScriptsFromTestCases(data.record.testCaseId,"TestCase");
           				addTaskComments(data.record);			           				
           		  });
           			return $img;
	        	}
	        },
	        changeRequests:{
	        	title : '',
	        	list : true,
	        	create : false,
	        	edit : false,
	        	width: "10%",
	        	display:function (data) { 
	           		//Create an image for Change Request popup 
					var $img = $('<img src="css/images/list_metro.png" title="Change Requests" class="showHandCursor">');
           			//Open ActiveChangeRequest popup  
           			$img.click(function () {           							           			
           				showChangeRequestOrClarification(currentProductId, 29, data.record.activityTaskId, data.record.activityTaskName, 'Task\'s', "Change Request", data.record.plannedTaskSize);			           				
           		  });
           			return $img;
	        	}
	        },
	        clarificationTrackers:{
	        	title : '',
	        	list : true,
	        	create : false,
	        	edit : false,
	        	width: "10%",
	        	display:function (data) { 
	           		//Create an image for Clarification popup 
					var $img = $('<img src="css/images/list_metro.png" title="Clarifications" class="showHandCursor">');
           			//Open Clarification popup  
           			$img.click(function () {
           				showChangeRequestOrClarification(currentProductId, 29, data.record.activityTaskId, data.record.activityTaskName, 'Task\'s', "Clarification", data.record.plannedTaskSize);			           				
           		  });
           			return $img;
	        	}
	        },
			attachment: { 
				title: '', 
				list: true,
				width: "5%",
				display:function (data) {	        		
	           		//Create an image that will be used to open child table 
					var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>'); 
	       			$img.click(function () {
	       				isViewAttachment = false;
	       				var jsonObj={"Title":"Attachments for Task",			          
	       					"SubTitle": 'Task : ['+data.record.activityTaskId+'] '+data.record.activityTaskName,
	    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+currentProductId+'&entityTypeId=29&entityInstanceId='+data.record.activityTaskId,
	    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+currentProductId+'&entityTypeId=29&entityInstanceId='+data.record.activityTaskId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
	    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
	    	    			 "updateURL": 'update.attachment.for.entity.or.instance',
	    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=29',
	    	    			"multipleUpload":true,
	    	    		};	 
		        		Attachments.init(jsonObj);
	       		  });
	       			return $img;
	        	}				
			},
         },
     });		 
	 $('#jTableContainerTask').jtable('load');		 
}


function listTestCase(){
	try{
		if ($('#jTableContainerTestCase').length>0) {
			$('#jTableContainerTestCase').jtable('destroy'); 
		}
	}catch(e){}
	
	 //init jTable
	 $('#jTableContainerTestCase').jtable({
         title: 'Test Cases',
         selecting: true, //Enable selecting 
         paging: true, //Enable paging
         pageSize: 10, //Set page size (default: 10)
         editinline:{enable:true},         
         /* saveUserPreferences: false, */
         recordsLoaded: function(event, data) {
        	 $(".jtable-edit-command-button").prop("disabled", true);
         },
         recordUpdated:function(event, data){
			$('#jTableContainerTeam').find('.jtable-child-table-container').jtable('reload');
		 },
		 recordAdded: function (event, data) {
			$('#jTableContainerTeam').find('.jtable-child-table-container').jtable('reload');
			listTeam();
		 },
		 recordDeleted: function (event, data) {
			$('#jTableContainerTeam').find('.jtable-child-table-container').jtable('reload');
		 },
         actions: {
             listAction: 'workflow.testcases.pending.my.action?productId='+currentProductId+"&entityLevel=Product&entityLevelId=3",
         },
         
         fields: {
         				   		
        	 testCaseId: { 
        		title: 'Test Case ID',
         		create:false,
         		edit: false,
         		list:true,
         		key: true
        	},
  			testCaseName: {
                title: 'Test Case Name ',
                inputTitle: 'Test Case Name <font color="#efd125" size="4px">*</font>',
                width: "12%",
                create : false,
                edit :false
        	},
        	testCaseDescription:{
        		title: 'Test Case Description', 
        	    width: "40%", 
        	    create : false,
                edit :false
        	},
        	executionTypeId:{
        		title : 'Execution Type',
        		width : "10%",
        		create : false,
                edit :false,
      			options:function(data){
      				return 'common.list.executiontypemaster.byentityid?entitymasterid=3';
      			}
        	},
        	testcasePriorityId:{
        		title : 'Priority',
        		width : "10%",
        		create : false,
        		edit : false,
      			options:function(data){
      				return 'common.list.testcasepriority';
      			}
        	},   
        	testcaseTypeId:{
        		title : 'Test Case Type',
        		width : "13%",
        		create : false,
        		edit : false,
      			options:function(data){
      				return 'common.list.testcasetype';
      			}
        	},
        	tcexecutionSummaryHistory:{
	        	title : 'Execution History',
	        	list : false,
	        	create : false,
	        	edit : false,
	        	width: "10%",
	        	display:function (data) { 
					var $img = $('<i class="fa fa-history" title="Execution Summary and History"></i>');
           			$img.click(function () {
           				var dataLevel = "productLevel";
           				listTCExecutionSummaryHistoryView(data.record.testCaseId, data.record.testCaseName, dataLevel);
           			});
           			return $img;
	        	}
	        },	        
	        workflowStatusDisplayName : {
				title : 'Current Status',
				create : false,
				list : true,
				edit : false,
				width : "20%",
				display: function(data){
					return data.record.workflowStatusDisplayName+"&nbsp;"+data.record.workflowStatusCategoryName;
				}
			},
			actors : {
				title : 'Pending With',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},
			completedBy : {
				title : 'Complete By',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},
			remainingHrsMins : {
				title : 'Time Left',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},			
			workflowIndicator : {
				title : '',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},
			workflowEntityEffortTrackerId:{
	        	title : '',
	        	create : false,
	        	edit : false,
	        	width: "10%",
	        	display:function (data) { 
					var $img = $('<i class="fa fa-th" title="Event tracker"></i>');
           			$img.click(function () {
           				addTestCaseComments(data.record);			           				
           		  });
           			return $img;
	        	}
	        },attachment: { 
				title: '', 
				list: true,
				width: "5%",
				display:function (data) {	        		
	           		//Create an image that will be used to open child table 
					var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>');
	       			$img.click(function () {
	       				isViewAttachment = false;
	       				var jsonObj={"Title":"Attachments for Testcase",			          
	       					"SubTitle": 'Testcase : ['+data.record.testCaseId+'] '+data.record.testCaseName,
	    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+currentProductId+'&entityTypeId=3&entityInstanceId='+data.record.testCaseId,
	    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+currentProductId+'&entityTypeId=3&entityInstanceId='+data.record.testCaseId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
	    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
	    	    			 "updateURL": 'update.attachment.for.entity.or.instance',
	    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=3',
	    	    			"multipleUpload":true,
	    	    		};	 
		        		Attachments.init(jsonObj);
	       		  });
	       			return $img;
	        	}				
			},
    	  	
         },
     });		 
	 $('#jTableContainerTestCase').jtable('load');		 
}

// ----- This code will be removed in the next release -----
// ----- Jtable Started - Activities -----

/*function listActivities(){
	var plannedActivitySizeHeaderTitle="Planned Activity Size";
	var actualActivitySizeHeaderTitle="Actual Activity Size";
	if(customActivityListHeaderFieldsEnable == "YES") {
		plannedActivitySizeHeaderTitle="Estimated Savings";
		actualActivitySizeHeaderTitle="Realized Savings";
	} 
	try{
		if ($('#jTableContainerActivity').length>0) {
			$('#jTableContainerActivity').jtable('destroy'); 
		}
	}catch(e){}
	
	 //init jTable
	 $('#jTableContainerActivity').jtable({
         title: 'Activities',
         selecting: true, //Enable selecting 
         paging: true, //Enable paging
         pageSize: 10, //Set page size (default: 10)
         editinline:{enable:true},         
         actions: {
             listAction: 'workflow.activities.pending.my.action?productId='+currentProductId+'&entityLevel=Product&entityLevelId=33',
         },
         fields: {
         	activityId : {
         		title: 'Activity Id',
 				key : true,
 				list : true,
 				edit:false,
 				create : false
 			},									
 			activityName:{
 	             title: 'Activity Name',
 	          	 list:true,
 	           	 edit:false,
 	           	 create:false,
 	             width:"20%"
 	        },
 	       activityWorkPackageName:{
	             title: 'Workpackage Name',
	          	 list:true,
	           	 edit:false,
	           	 create:false,
	             width:"20%"
	        },
         	activityMasterId : {
				title : 'Activity Type',
				create : false,
				edit : false,
				list: false,
				width : "20%",
				options : 'common.list.activity.tasktype?productId='+currentProductId
			},
			activityMasterName : {
				title : 'Activity Type',
				create : false,
				edit : false,
				width : "20%",
			},			
			statusDisplayName : {
				title : 'Current Status',
				create : false,
				list : true,
				edit : false,
				width : "20%",
				display: function(data){
					return data.record.statusDisplayName+"&nbsp;"+data.record.workflowStatusCategoryName;
				}
			},
			actors : {
				title : 'Pending With',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},
			completedBy : {
				title : 'Complete By',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},
			remainingHrsMins : {
				title : 'Time Left',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},
			workflowIndicator : {
				title : '',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},
			plannedActivitySize : {
				//title : 'Planned Activity Size',
				title : plannedActivitySizeHeaderTitle,
				list : true,				    										
				create : true,
				edit : true,
				width : "20%",
				defaultValue : 1,										
			},
			actualActivitySize : {
				//title : 'Actual Activity Size',
				title : actualActivitySizeHeaderTitle,		
				list : true,				    										
				create : false,
				edit : false,
				width : "20%",
				//defaultValue :plannedActivitySize,
			},
			percentageCompletion : {
				title : '% Completion',
				create : false,
				list : true,
				edit : true,
				width : "4%"
			},
			totalEffort : {
	        	title : 'Total Effort',
	        	create : false,
	        	edit : false,
	        	width : "20%",
	        	
	         },
			workflowEntityEffortTrackerId:{
	        	title : '',
	        	list : true,
	        	create : false,
	        	edit : false,
	        	width: "10%",
	        	display:function (data) { 
	           		//Create an image for test script popup 
					var $img = $('<i class="fa fa-th" title="Event tracker"></i>');
           			//Open Testscript popup  
           			$img.click(function () {
           				//displayTestScriptsFromTestCases(data.record.testCaseId,"TestCase"); 			           				
           				addActivityCommentsWorkflow(data.record);			           				
           		  });
           			return $img;
	        	}
	        },
	        changeRequests:{
	        	title : '',
	        	list : true,
	        	create : false,
	        	edit : false,
	        	width: "10%",
	        	display:function (data) { 
	           		//Create an image for Change Request popup 
					var $img = $('<img src="css/images/list_metro.png" title="Change Requests" class="showHandCursor">');
           			//Open ChangeRequest popup  
           			$img.click(function () {           							           			
           				showChangeRequestOrClarification(currentProductId, 28, data.record.activityId, data.record.activityName, 'Activity\'s', "Change Request", data.record.plannedActivitySize);			           				
           		  });
           			return $img;
	        	}
	        },
			clarificationTrackers:{
	        	title : '',
	        	list : true,
	        	create : false,
	        	edit : false,
	        	width: "10%",
	        	display:function (data) { 
	           		//Create an image for Clarification popup 
					var $img = $('<img src="css/images/list_metro.png" title="Clarifications" class="showHandCursor">');
           			//Open Clarification popup  
           			$img.click(function () {
           				showChangeRequestOrClarification(currentProductId, 28, data.record.activityId, data.record.activityName, 'Activity\'s', "Clarification", data.record.plannedActivitySize);			           				
           		  });
           			return $img;
	        	}
	        },
	        attachment: { 
				title: '', 
				list: true,
				width: "5%",
				display:function (data) {	        		
	           		//Create an image that will be used to open child table 
					var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>');
	       			$img.click(function () {
	       				isViewAttachment = false;
	       				var jsonObj={"Title":"Attachments for Activity",			          
	       					"SubTitle": 'Activity : ['+data.record.activityId+'] '+data.record.activityName,
	    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+currentProductId+'&entityTypeId=28&entityInstanceId='+data.record.activityId,
	    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+currentProductId+'&entityTypeId=28&entityInstanceId='+data.record.activityId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
	    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
	    	    			"updateURL": 'update.attachment.for.entity.or.instance',
	    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=28',
	    	    			"multipleUpload":true,
	    	    		};	 
		        		Attachments.init(jsonObj);
	       		  });
	       			return $img;
	        	}				
			},
         },
     });		 
	 $('#jTableContainerActivity').jtable('load');		 
}*/

//----- Ended  -----

// ----- DataTable Started -----
//----- Making ajax request for the dataTable ----- 

function assignDataTableValues(url,tableValue, row, tr){
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
				//productManagementDTJsonData = data;				
				//productVersionResults_Container(data, "240px", row);
				listActivitiesDT(data);
				
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

// ----- Activites DataTable Started -----

function activityWorkflowDTTable(){
	var plannedActivitySizeHeaderTitle="Planned Activity Size";
	var actualActivitySizeHeaderTitle="Actual Activity Size";
	if(customActivityListHeaderFieldsEnable == "YES") {
		plannedActivitySizeHeaderTitle="Estimated Savings";
		actualActivitySizeHeaderTitle="Realized Savings";
	} 
	
	var childDivString = '<table id="activityWorkflow_dataTable"  class="cell-border compact row-border" cellspacing="0" width="100%">'+
							'<thead>'+
								'<tr>'+
									'<th>Activity Id</th>'+
									'<th>Activity Name</th>'+
									'<th>Workpackage Name</th>'+
									'<th>Activity Type</th>'+
									'<th>Current Status</th>'+
									'<th>Pending With</th>'+
									'<th>Complete By</th>'+
									'<th>Time Left</th>'+
									'<th></th>'+
									'<th>'+plannedActivitySizeHeaderTitle+'</th>'+
									'<th>'+actualActivitySizeHeaderTitle+'</th>'+
									'<th>% Completion</th>'+
									'<th>Total Effort</th>'+
									'<th></th>'+
								'</tr>'+
							'</thead>'+
							'<tfoot>'+
								'<tr>'+
								'</tr>'+
							'</tfoot>'+
						'</table>';
	return childDivString;	
}

var activityWorkflowDT_oTable='';
var activityWorkflowDTFlag=false;

function listActivitiesDT(data){
	activityWorkflowDTFlag=false;
	
	try{
		if ($("#jTableContainerActivity").children().length>0) {
			$("#jTableContainerActivity").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = activityWorkflowDTTable();
	$("#jTableContainerActivity").append(childDivString);
	
	$("#activityWorkflow_dataTable tfoot tr").html('');
	var emptytr = emptyTableRowAppending(14);
	$("#activityWorkflow_dataTable tfoot tr").append(emptytr);
	
	activityWorkflowDT_oTable = $("#activityWorkflow_dataTable").dataTable( {
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
    	   var searchcolumnVisibleIndex = [7, 8, 9, 10, 11, 12, 13]; // search column TextBox Invisible Column position
 		   
     	   if(!activityWorkflowDTFlag){
     		  $('#activityWorkflow_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
     	   activityWorkflowDTFlag=true;     	   
    	   reInitializeDTWorkflowMyAction();
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
	    aaData:data,		    				 
	    aoColumns: [	        	        
	       //{ mData: "activityId",className: 'disableEditInline', sWidth: '5%' },
	       { mData: "activityId", sWidth: '5%', "render": function (tcData,type,full) {	        
			 		var entityInstanceId = full.activityId;					
			 		return ('<a onclick="displayTabActivitySummaryHandler('+entityInstanceId+')">'+full.activityId+'</a>');		        
		    	},
		    },
	       { mData: "activityName",className: 'disableEditInline', sWidth: '15%' },		
	       { mData: "activityWorkPackageName",className: 'disableEditInline', sWidth: '15%' },		
	       { mData: "activityMasterName",className: 'disableEditInline', sWidth: '10%' },		
	       { mData: "statusDisplayName",className: 'disableEditInline', sWidth: '10%', 
           	mRender: function (data, type, full) {		           	 
		             return full.statusDisplayName+"&nbsp;"+full.workflowStatusCategoryName;
	             },
           },       
		   { mData: "actors",className: 'disableEditInline', sWidth: '15%' },		
	       { mData: "completedBy",className: 'disableEditInline', sWidth: '10%' },		
	       { mData: "remainingHrsMins",className: 'disableEditInline', sWidth: '10%' },		
	       { mData: "workflowIndicator",className: 'disableEditInline', sWidth: '5%' },		
	       { mData: "plannedActivitySize",className: 'disableEditInline', sWidth: '10%' },
			{ mData: "actualActivitySize",className: 'disableEditInline', sWidth: '10%' },		
	       { mData: "percentageCompletion",className: 'disableEditInline', sWidth: '5%' },		
	       { mData: "totalEffort",className: 'disableEditInline', sWidth: '5%' },		
	       { mData: null,				 
	        	bSortable: false,	        	
	        	mRender: function(data, type, full) {				            	
	       		 var img = ('<div style="display: flex;">'+
		       		'<button style="border: none; background-color: transparent; outline: none;">'+		       		
		       				  '<i class="fa fa-th imgActivityWorkflow" title="Event tracker"></i></button>'+
		       		'<button style="border: none; background-color: transparent; outline: none;">'+
		       				  '<img src="css/images/list_metro.png" title="Change Requests" class="showHandCursor imgActivityWorkflowCR"/></button>'+
		       		'<button style="border: none; background-color: transparent; outline: none;">'+
	       					'<img src="css/images/list_metro.png" title="Clarifications" class="showHandCursor imgActivityWorkflowClarification"/></button>'+
	       			'<button style="border: none; background-color: transparent; outline: none;">'+
	       					'<img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;" class="imgActivityWorkflowAttachment"/></button>'+		
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
	// ------
	 $(function(){ // this will be called when the DOM is ready		
		 
   		$("#activityWorkflow_dataTable_length").css('margin-top','8px');
		$("#activityWorkflow_dataTable_length").css('padding-left','35px');
		
		activityWorkflowDT_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
		});
				
		// ----- activity workflow table -----
		 $('#jTableContainerActivity').on('click', 'td button .imgActivityWorkflow', function () {
		   	 var tr = $(this).closest('tr');
		   	 var row = activityWorkflowDT_oTable.DataTable().row(tr);
		   	 addActivityCommentsWorkflow(row.data()); 
		});
		 
		// ----- activity workflow CR -----
		 $('#jTableContainerActivity').on('click', 'td button .imgActivityWorkflowCR', function () {
		   	 var tr = $(this).closest('tr');
		   	 var row = activityWorkflowDT_oTable.DataTable().row(tr);
		   	 showChangeRequestOrClarification(currentProductId, 28, row.data().activityId, row.data().activityName, 'Activity\'s', "Change Request", row.data().plannedActivitySize);
		});
		 
		// ----- activity workflow Clarifications -----
		 $('#jTableContainerActivity').on('click', 'td button .imgActivityWorkflowClarification', function () {
		   	 var tr = $(this).closest('tr');
		   	 var row = activityWorkflowDT_oTable.DataTable().row(tr);
		   	 
		   	showChangeRequestOrClarification(currentProductId, 28, row.data().activityId, row.data().activityName, 'Activity\'s', "Clarification", row.data().plannedActivitySize);	
		});
		 
		// ----- activity workflow Clarifications -----
		 $('#jTableContainerActivity').on('click', 'td button .imgActivityWorkflowAttachment', function () {
		   	 var tr = $(this).closest('tr');
		   	 var row = activityWorkflowDT_oTable.DataTable().row(tr);
		   	 
		   	isViewAttachment = false;
			var jsonObj={"Title":"Attachments for Activity",			          
				"SubTitle": 'Activity : ['+row.data().activityId+'] '+row.data().activityName,
				"listURL": 'attachment.for.entity.or.instance.list?productId='+currentProductId+'&entityTypeId=28&entityInstanceId='+row.data().activityId,
				"creatURL": 'upload.attachment.for.entity.or.instance?productId='+currentProductId+'&entityTypeId=28&entityInstanceId='+row.data().activityId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
				"deleteURL": 'delete.attachment.for.entity.or.instance',
				"updateURL": 'update.attachment.for.entity.or.instance',
				"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=28',
				"multipleUpload":true,
			};	 
			Attachments.init(jsonObj);	
		});
		 
	 
	 });
}

var clearTimeoutDTActivityWorkflow='';
function reInitializeDTWorkflowMyAction(){
	clearTimeoutDTActivityWorkflow = setTimeout(function(){				
		activityWorkflowDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTActivityWorkflow);
	},200);
}

// ----- Ended -----

function listActivityWorkpackages(){
	try{
		if ($('#jTableContainerActivityWorkpackage').length>0) {
			$('#jTableContainerActivityWorkpackage').jtable('destroy'); 
		}
	}catch(e){}
	
	 //init jTable
	 $('#jTableContainerActivityWorkpackage').jtable({
         title: 'Activity Workpackages',
         selecting: true, //Enable selecting 
         paging: true, //Enable paging
         pageSize: 10, //Set page size (default: 10)
         editinline:{enable:true},        
         actions: {
             listAction: 'workflow.activity.workpackages.pending.my.action?productId='+currentProductId+'&entityLevel=Product&entityLevelId=34',
         },
         fields: {
         	activityWorkPackageId : {
         		title: 'Activity Workpackage Id',
 				key : true,
 				list : true,
 				edit:false,
 				create : false
 			},									
 			activityWorkPackageName:{
 	             title: 'Activity Workpackage Name',
 	          	 list:true,
 	           	 edit:false,
 	           	 create:false,
 	             width:"20%"
 	        }, 	        
			statusDisplayName : {
				title : 'Current Status',
				create : false,
				list : true,
				edit : false,
				width : "20%",
				display: function(data){
					return data.record.statusDisplayName+"&nbsp;"+data.record.workflowStatusCategoryName;
				}
			},
			actors : {
				title : 'Pending With',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},
			completedBy : {
				title : 'Complete By',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},
			remainingHrsMins : {
				title : 'Time Left',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},		 
			workflowIndicator : {
				title : '',
				create : false,
				list : true,
				edit : false,
				width : "20%"
			},
			totalEffort : {
	        	title : 'Total Effort',
	        	create : false,
	        	edit : false,
	        	width : "20%",
	        	
	         },
			workflowEntityEffortTrackerId:{
	        	title : '',
	        	list : true,
	        	create : false,
	        	edit : false,
	        	width: "10%",
	        	display:function (data) { 
	           		//Create an image for test script popup 
					var $img = $('<i class="fa fa-th" title="Event tracker"></i>');
           			//Open Testscript popup  
           			$img.click(function () {
           				//displayTestScriptsFromTestCases(data.record.testCaseId,"TestCase"); 			           				
           				addActivityWorkpackageComments(data.record);			           				
           		  });
           			return $img;
	        	}
	        },attachment: { 
				title: '', 
				list: true,
				width: "5%",
				display:function (data) {	        		
	           		//Create an image that will be used to open child table 
					var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>');
	       			$img.click(function () {
	       				isViewAttachment = false;
	       				var jsonObj={"Title":"Attachments for Activity Workpackage",			          
	       					"SubTitle": 'Activity Workpackage : ['+data.record.activityWorkPackageId+'] '+data.record.activityWorkPackageName,
	    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+currentProductId+'&entityTypeId=34&entityInstanceId='+data.record.activityWorkPackageId,
	    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+currentProductId+'&entityTypeId=34&entityInstanceId='+data.record.activityWorkPackageId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
	    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
	    	    			"updateURL": 'update.attachment.for.entity.or.instance',
	    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=34',
	    	    			"multipleUpload":true,
	    	    		};	 
		        		Attachments.init(jsonObj);
	       		  });
	       			return $img;
	        	}				
			},
         },
     });		 
	 $('#jTableContainerActivityWorkpackage').jtable('load');		 
}

function listTCExecutionSummaryHistoryView(testCaseId, testCaseName, dataLevel){	
	prodId = document.getElementById("treeHdnCurrentProductId").value;
	var jsonObj={"Title":"TestCase Execution ["+ testCaseId + "] "+testCaseName,
			     "testCaseID":testCaseId,
				 "testCaseName":testCaseName,
				 "testCaseExectionSummayTitle":"Execution Summary",
				 "testCaseExectionSummayUrl": 'result.testcase.execution.summary?tcId='+testCaseId+'&workPackageId=-1&productId='+prodId+'&dataLevel='+dataLevel,
				 "testCaseExectionHistoryTitle":"Execution History",
				 "testCaseExectionHistoryUrl":"testcase.execution.history?testCaseId="+testCaseId+'&workPackageId=-1&dataLevel='+dataLevel,
				 "componentUsageTitle":dataLevel,
	};
	TestCaseExecutionDetails.init(jsonObj);	
}

function addTaskComments(record){	

	var entityInstanceName = record.activityTaskName;	
	var entityInstanceId = record.activityTaskId;
	var modifiedById = record.assigneeId;
	var currentStatusId = record.statusId;
	var currentStatusName = record.statusName;
	var currentStatusDisplayName = record.statusDisplayName;
	var entityTypeId = 30;
	var entityId = record.activityTaskTypeId;
	var actionTypeValue = 0;
	var secondaryStatusId = record.secondaryStatusId;
	var actualSize=record.actualSize;
	$("#addCommentsMainDiv").modal();			

	var jsonObj = {
			Title : "Task Workflow History: ["+entityInstanceId+"] "+entityInstanceName,	
			entityTypeName : 'Task',
			entityTypeId : entityTypeId,
			entityInstanceName : entityInstanceName,
			entityInstanceId : entityInstanceId,
			modifiedByUrl : 'common.user.list.by.resourcepool.id',		
			modifiedById : modifiedById,
			raisedDate : new Date(),
			comments : "",	 
			productId : currentProductId,
			primaryStatusUrl : 'workflow.status.master.option.list?productId='+currentProductId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&currentStatusId='+currentStatusId,
			secondaryStatusUrl : 'workflow.entity.secondary.status.master.option.list?productId='+currentProductId+'&entityTypeId='+entityTypeId+'&statusId='+currentStatusId,
			currentStatusId : currentStatusId,
			currentStatusName : currentStatusDisplayName,
			secondaryStatusId : secondaryStatusId,
			effortListUrl : 'workflow.event.tracker.list?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
			actionTypeValue : actionTypeValue,
			commentsName : commentsReviewActivity,
			urlToSave : 'workflow.event.tracker.add?productId='+currentProductId+'&entityId='+entityId+'&entityTypeId='+entityTypeId+'&primaryStatusId=[primaryStatusId]&secondaryStatusId=[secondaryStatusId]&effort=[effort]&comments=[comments]&sourceStatusId='+currentStatusId+'&approveAllEntityInstanceIds=[approveAllEntityIds]&entityInstanceId='+entityInstanceId+'&attachmentIds=[attachmentIds]&actionDate=[actionDate]&actualSize=[actualSize]',
			// commentsStatus: "['started','InProgress','Completed']",	
			isFromMyActions : true
	};
	AddComments.init(jsonObj);
	
	
}

function addTestCaseComments(record){	
	
	var entityInstanceName = record.testCaseName;	
	var entityInstanceId = record.testCaseId;
	var modifiedById = record.assigneeId;
	var currentStatusId = record.workflowStatusId;
	var currentStatusName = record.workflowStatusName;
	var currentStatusDisplayName = record.workflowStatusDisplayName;
	var entityTypeId = 3;
	var entityId = 0;
	var actionTypeValue = 0;
	var secondaryStatusId = record.secondaryStatusId;
	var actualSize=record.actualSize;
	$("#addCommentsMainDiv").modal();			

	var jsonObj = {
		Title : "Test Case Workflow History: ["+entityInstanceId+"] "+entityInstanceName,	
		entityTypeName : 'Test Case',		
		entityTypeId : entityTypeId,
		entityInstanceName : entityInstanceName,
		entityInstanceId : entityInstanceId,
	    modifiedByUrl : 'common.user.list.by.resourcepool.id',		
	    modifiedById : modifiedById,
	    raisedDate: new Date(),
	    comments : "",	   
	    productId : currentProductId,
	    primaryStatusUrl : 'workflow.status.master.option.list?productId='+currentProductId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&currentStatusId='+currentStatusId,
		secondaryStatusUrl : 'workflow.entity.secondary.status.master.option.list?productId='+currentProductId+'&entityTypeId='+entityTypeId+'&statusId='+currentStatusId,
		currentStatusId : currentStatusId,
		currentStatusName : currentStatusDisplayName,
		secondaryStatusId : secondaryStatusId,
		effortListUrl : 'workflow.event.tracker.list?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
		actionTypeValue : actionTypeValue,
		commentsName : commentsReviewActivity,
		urlToSave : 'workflow.event.tracker.add?productId='+currentProductId+'&entityId='+entityId+'&entityTypeId='+entityTypeId+'&primaryStatusId=[primaryStatusId]&secondaryStatusId=[secondaryStatusId]&effort=[effort]&comments=[comments]&sourceStatusId='+currentStatusId+'&approveAllEntityInstanceIds=[approveAllEntityIds]&entityInstanceId='+entityInstanceId+'&attachmentIds=[attachmentIds]&actionDate=[actionDate]&actualSize=[actualSize]',
	  // commentsStatus: "['started','InProgress','Completed']",
		isFromMyActions : true
	};
	AddComments.init(jsonObj);
}
var activityId = "";
function addActivityCommentsWorkflow(record){	
	activityId = record.activityId;
	var entityInstanceName = record.activityName;	
	var entityInstanceId = record.activityId;
	var modifiedById = record.assigneeId;
	var currentStatusId = record.statusId;
	var currentStatusName = record.statusName;
	var currentStatusDisplayName = record.statusDisplayName;
	var entityTypeId = 33;											//Activity type
	var entityId = record.activityMasterId;
	var actionTypeValue = 0;
	var secondaryStatusId = record.secondaryStatusId;
	$("#addCommentsMainDiv").modal();			
	
	var jsonObj = {
			Title : "Activity Workflow History: ["+entityInstanceId+"] "+entityInstanceName,	
			entityTypeName : 'Activity',		
			entityTypeId : entityTypeId,
			entityInstanceName : entityInstanceName,
			entityInstanceId : entityInstanceId,
			modifiedByUrl : 'common.user.list.by.resourcepool.id',		
			modifiedById : modifiedById,
			raisedDate : new Date(),
			comments : "",
			productId : currentProductId,
			primaryStatusUrl : 'workflow.status.master.option.list?productId='+currentProductId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&currentStatusId='+currentStatusId,
			secondaryStatusUrl : 'workflow.entity.secondary.status.master.option.list?productId='+currentProductId+'&entityTypeId='+entityTypeId+'&statusId='+currentStatusId,
			currentStatusId : currentStatusId,
			currentStatusName : currentStatusDisplayName,
			secondaryStatusId : secondaryStatusId,
			effortListUrl : 'workflow.event.tracker.list?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
			actionTypeValue : actionTypeValue,
			commentsName : commentsReviewActivity,
			urlToSave : 'workflow.event.tracker.add?productId='+currentProductId+'&entityId='+entityId+'&entityTypeId='+entityTypeId+'&primaryStatusId=[primaryStatusId]&secondaryStatusId=[secondaryStatusId]&effort=[effort]&comments=[comments]&sourceStatusId='+currentStatusId+'&approveAllEntityInstanceIds=[approveAllEntityIds]&entityInstanceId='+entityInstanceId+'&attachmentIds=[attachmentIds]&actionDate=[actionDate]&actualSize=[actualSize]',
			isFromMyActions : true
	};
	AddComments.init(jsonObj);
}

function addActivityWorkpackageComments(record){	

	var entityInstanceName = record.activityWorkPackageName;	
	var entityInstanceId = record.activityWorkPackageId;
	var modifiedById = record.ownerId;
	var currentStatusId = record.statusId;
	var currentStatusName = record.statusName;
	var currentStatusDisplayName = record.statusDisplayName;
	var entityTypeId = 34;//Activity type
	var entityId = 0;
	var actionTypeValue = 0;
	var secondaryStatusId = record.secondaryStatusId;
	var actualSize=record.actualSize;
	$("#addCommentsMainDiv").modal();			
	
	var jsonObj = {
			Title : "Activity Workpackage Workflow History: ["+entityInstanceId+"] "+entityInstanceName,	
			entityTypeName : 'Activity Workpackage',		
			entityTypeId : entityTypeId,
			entityInstanceName : entityInstanceName,
			entityInstanceId : entityInstanceId,
			modifiedByUrl : 'common.user.list.by.resourcepool.id',		
			modifiedById : modifiedById,
			raisedDate : new Date(),
			comments : "",
			productId : currentProductId,
			primaryStatusUrl : 'workflow.status.master.option.list?productId='+currentProductId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&currentStatusId='+currentStatusId,
			secondaryStatusUrl : 'workflow.entity.secondary.status.master.option.list?productId='+currentProductId+'&entityTypeId='+entityTypeId+'&statusId='+currentStatusId,
			currentStatusId : currentStatusId,
			currentStatusName : currentStatusDisplayName,
			secondaryStatusId : secondaryStatusId,
			effortListUrl : 'workflow.event.tracker.list?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
			actionTypeValue : actionTypeValue,
			commentsName : commentsReviewActivity,
			urlToSave : 'workflow.event.tracker.add?productId='+currentProductId+'&entityId='+entityId+'&entityTypeId='+entityTypeId+'&primaryStatusId=[primaryStatusId]&secondaryStatusId=[secondaryStatusId]&effort=[effort]&comments=[comments]&sourceStatusId='+currentStatusId+'&approveAllEntityInstanceIds=[approveAllEntityIds]&entityInstanceId='+entityInstanceId+'&attachmentIds=[attachmentIds]&actionDate=[actionDate]&actualSize=[actualSize]',			
			isFromMyActions : true
	};
	AddComments.init(jsonObj);
}

function getWorkflowMyActionCounts(currentProductId, entityTypeId){
	//  ----- Commented code: getting activity count url is taking more time than need to be fixed once done this code will  be reverted back 
	
	//openLoaderIcon();
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : 'workflow.pending.my.action.count?productId='+currentProductId+'&entityTypeId='+entityTypeId,
		dataType : 'json',
		success : function(data) {
			//closeLoaderIcon();
			if(data.Result == 'OK' || data.Result == 'Ok'){
				var records = data.Records;
				//$('.pendingActionCount').text(0);
				$.each(records, function(index, value){
					var record = value;
					$('#'+Object.keys(record)[0]+'Count').text(record[Object.keys(record)[0]]);
				});
			}
		},
		error: function(data){
			//closeLoaderIcon();
			console.log("error in ajax call");
		},

		complete: function(data){
			//closeLoaderIcon();
		},
	});
}

function initializeActivityTab(){
	
	$("#tablistActivities li").removeClass('active');
	$("#tablistActivities li").eq(0).addClass('active');
	
	$("#tbCntnt #ActivitySummary").addClass('active in');	
	$("#tbCntnt #ActivityTasks").removeClass('active in');
	$("#tbCntnt #ChangeRequests").removeClass('active in');
	$("#tbCntnt #Clarifications").removeClass('active in');
	$("#tbCntnt #attachmentsOfAct").removeClass('active in');
	$("#tbCntnt #StatusPlan").removeClass('active in');
	$("#tbCntnt #auditHistoryOfAct").removeClass('active in');	
	
	$("#customFieldActivitySingleFrequencyContainer").html('');
}

function displayTabActivitySummaryHandler(activityId){
	document.getElementById("treeHdnCurrentActivityId").value = activityId; 
	$("#activityTabSummaryContainer .modal-header h4").text("Activity");
	$("#activityTabSummaryContainer").modal();	
	$("#activitySummaryContainer").append($("#activitesSummaryPage"));
	
	initializeActivityTab();
	
	showMyActivitySummaryDetails(activityId);	
	$("#activitySummaryContainer").show();
	$("#myActivityStatusSummaryView").css("max-height", "450px");
}


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
}

function refreshTabCountAndContent(entityTypeId){
	getWorkflowMyActionCounts(currentProductId, entityTypeId);
	workflowActionsTabSelection(selectedTab);
}

function showChangeRequestOrClarification(productId, entityTypeId, entityInstanceId, entityInstanceName, entityTypeName, processType, plannedSize){
	var jsonObj={
			"Title":entityTypeName+" "+processType +" : ["+entityInstanceId+"] "+entityInstanceName,
			"url": '',            
	};
	SingleJtableContainer.init(jsonObj);
	clearSingleJTableDatas();
	
	if(processType == 'Change Request'){
		showChangeRequest(productId, entityTypeId, entityInstanceId, 42, plannedSize);
	}else if(processType == 'Clarification'){
		showClarification(productId, entityTypeId, entityInstanceId, 31, plannedSize);
	}else{
		callAlert("Invalid call");
	}
	$("#div_SingleJTableSummary .modal-header .row").css('display','none');       

}

function showChangeRequest(productId, entityTypeId, entityInstanceId, targetEntityTypeId, plannedSize){
	clearSingleJTableDatas();
	try {
		if ($('#JtableSingleContainer').length > 0) {
			$('#JtableSingleContainer').jtable('destroy');
		}
	} catch (e) {
	}
	$('#JtableSingleContainer').jtable( 
	{						                   	      	   
		title: 'Change Requests',							                   	      
		editinline:{enable:true},       
		paging : true, //Enable paging								
		pageSize : 10,
		actions: { 
			listAction: 'list.changerequests.by.entityTypeAndInstanceId?entityType1='+entityTypeId+'&entityInstance1='+entityInstanceId,
			createAction : 'changerequests.add.by.entityTypeAndInstanceId?entityInstanceId='+entityInstanceId,
			editinlineAction : 'list.change.requests.by.activity.update?activityId='+activityId,
		},
		recordsLoaded: function(event, data) {
			$(".jtable-edit-command-button").prop("disabled", true);
		},	
		fields: { 
			productId: { 
			type: 'hidden', 
			create: true, 
			edit:true 
		},			
			changeRequestId: { 					                   							
				title : 'ID',
				key: true, 
			create: false, 
            edit: false, 
			list: true
		},
		entityType1:{
			type: 'hidden',
			create: true, 
            edit: true, 
			list: true,
			defaultValue: entityTypeId
		},
		entityType2:{
			type: 'hidden',
			create: true, 
            edit: false, 
			list: false,
			defaultValue: targetEntityTypeId
		},
		entityInstance1:{
			type: 'hidden',
			create: true, 
            edit: false, 
			list: false,
			defaultValue: entityInstanceId,
		},
		changeRequestName: { 					                   							
				title : 'Name',
				inputTitle : 'Change Request Name <font color="#efd125" size="4px">*</font>',
				key: true, 
			create: true, 
            edit: true, 
			list: true
		},
		description: {
			 title: 'Description',
             list:true,
             type: 'textarea',    
             create:true,
             edit:true,
             width:"20%"
             },
	 	priorityId : {
			title : 'Priority',					    										
			list : true,
			create : true,
			edit : true,
			width : "20%",
			options : 'administration.executionPriorityList'
		},
		planExpectedValue: {
			title : 'Planned Value',										
			inputTitle : 'Planned Value <font color="#efd125" size="4px"></font>',
			edit : true,
			list : true,
			create : true,								
			width : "20%",
			defaultValue:plannedSize,
		},
		statusCategoryId : {
			title : 'Status',
			create : false,
			list : false,
			edit : false,
			width : "20%",
			options: 'status.category.option.list'
					
		}, 
			ownerId : {
			title : 'Owner',					    										
			list : true,
			create : true,
			edit : true,
			options : function(data) {
				if(productId != null){
					return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
				}
			}
		},
		raisedDate : {
			title : 'Raised Date',
			inputTitle : 'Raised Date <font color="#efd125" size="4px">*</font>',
			edit : true,
			list : true,
			create : true,
			type : 'date',
			width : "20%",
			defaultValue : (new Date().getMonth()+1)+"/"+new Date().getDate()+"/"+new Date().getFullYear(),
		}, 						    								
		},				                   	
		formSubmitting: function (event, data) {
      	    data.form.find('input[name="changeRequestName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[maxSize[100]');
      	    data.form.find('input[name="raisedDate"]').addClass('validate[required]');
            data.form.validationEngine();
            return data.form.validationEngine('validate');
          },   
     //Dispose validation logic when form is closed
     formClosed: function (event, data) {
        data.form.validationEngine('hide');
        data.form.validationEngine('detach');
    }
	}); 
	$('#JtableSingleContainer').jtable('load');
}

function showClarification(productId, entityTypeId, entityInstanceId, targetEntityTypeId, plannedSize) {
	clearSingleJTableDatas();		
	try {
		if ($('#JtableSingleContainer').length > 0) {
			$('#JtableSingleContainer').jtable('destroy');
		}
	} catch (e) {
	}
	$('#JtableSingleContainer').jtable( 
					{ 
					title: 'Clarifications', 
					editinline:{enable:true},
					editInlineRowRequestModeDependsOn: true,
					paging : true, //Enable paging								
					pageSize : 10,
					actions: { 
						listAction : 'list.clarificationtracker.by.entityTypeAndInstanceIds?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
						createAction : 'add.clarificationtracker.by.activity',	    									
						editinlineAction : 'update.clarificationtracker.by.activity',	
						},
					recordsLoaded: function(event, data) {
		        		$(".jtable-edit-command-button").prop("disabled", true);      		        		
		         	},		         	
					fields: { 
					activityId: { 
   						type: 'hidden', 
   						defaultValue: entityInstanceId 
   					},
					 productId: { 
						type: 'hidden', 
						create: true, 
						edit:true,
						defaultValue: productId
					 },			 
				 entityTypeId:{
						type: 'hidden',
						create: true, 
		                edit: true, 
						list: true,
						defaultValue: entityTypeId
					},
					entityTypeId2:{
						type: 'hidden',
						create: true, 
		                edit: false, 
						list: false,
						defaultValue: targetEntityTypeId
					},							
					 entityInstanceId: {
						type: 'hidden',
						create: true, 
		                edit: false, 
						list: false,
						defaultValue: entityInstanceId
					}, 
						clarificationTrackerId: { 
   						key: true, 
   						title : 'ID',
   						create: false, 
		                edit: false, 
   						list: true
   					},
   					clarificationTitle : {
						title : 'Title',
						inputTitle : 'Clarification Title <font color="#efd125" size="4px">*</font>',
						list : true,				    										
						create : true,
						edit : true,
						width : "20%"						
					},
					clarificationDescription:{
			             title: 'Description',
			             list:true,
			             type: 'textarea',    
			             create:true,
			             edit:true,
			             width:"20%"
			             },					
			        priorityId : {
						title : 'Priority',				    										
						list : true,
						create : true,
						edit : true,
						width : "20%",
						options : 'administration.executionPriorityList'
					},					
					clarificationTypeId : {								
						title : 'Type',	
						inputTitle : 'Clarification Type <font color="#efd125" size="4px">*</font>',
						list : true,
						create : true,
						edit : true,
						width : "20%",
						options : 'list.clarification.type.option'															
					},
					clarificationScopeId : {
						title : 'Scope',
						inputTitle : 'Clarification Scope <font color="#efd125" size="4px">*</font>',
						list : true,				    										
						create : true,
						edit : true,
						width : "20%",
						options : 'list.clarification.scope.option'						
					},
					planExpectedValue: {
						title : 'Planned Value',										
						inputTitle : 'Planned Value <font color="#efd125" size="4px"></font>',
						edit : true,
						list : true,
						create : true,								
						width : "20%",
						defaultValue:plannedSize,
					},
					workflowStatusId : {
						title : 'Status',
						create : false,
						list : true,
						edit : true,
						width : "20%",
						options: function(data){
							return 'clarification.status.option.list?entityTypeId=31';
						}
					},
					 resolution : {
						title : 'Resolution',
						create : false,
						list : true,
						edit : true,
						width : "20%",
						dependsOn:'workflowStatusId',									
							options:function(data){
								return 'clarification.resolution.option.list?parentStatusId='+data.dependedValues.workflowStatusId;															
							}	 									
					}, 					    
					ownerId : {
						title : 'Owner',				    									
						list : true,
						width : "20%",
						create : true,
						edit : true,
						options : 'common.allusers.list.by.resourcepool.id'
					},  							   
					raisedById : {
						title : 'Raised By',
						inputTitle : 'Raised By <font color="#efd125" size="4px">*</font>',
						list : true,
						create : true,
						width : "20%",
						edit : true,
						defaultValue:raisedby,
						options : 'common.allusers.list.by.resourcepool.id'						
					},
					raisedDate : {
						title : 'Raised Date',
						inputTitle : 'Raised Date <font color="#efd125" size="4px">*</font>',
						edit : true,
						list : true,
						create : true,
						type : 'date',
						width : "20%",
						defaultValue : (new Date().getMonth()+1)+"/"+new Date().getDate()+"/"+new Date().getFullYear(),
					},
					plannedEndDate : {
						title : 'Expected End Date',										
						inputTitle : 'Expected End Date <font color="#efd125" size="4px"></font>',
						edit : true,
						list : true,
						create : true,
						type : 'date',
						width : "20%",
						defaultValue : (new Date().getMonth()+1)+"/"+new Date().getDate()+"/"+new Date().getFullYear(),
					},
					plannedStartDate : {
						title : 'Planned Start Date',										
						inputTitle : 'Planned Start Date <font color="#efd125" size="4px"></font>',
						edit : false,
						create : false,
						list : false,
						type : 'date',
						width : "20%",
						defaultValue : (new Date().getMonth()+1)+"/"+new Date().getDate()+"/"+new Date().getFullYear(),
					},actualStartDate : {
						title : 'Actual Start Date',
						create : false,
						edit : false,
						list : false,
						type : 'date',
						width : "20%"
					},
					actualEndDate : {
						title : 'Actual End Date',
						create : false,
						edit : false,
						list : false,
						type : 'date',
						width : "20%"
					}, 
					
				},
				formSubmitting: function (event, data) {
		      	    data.form.find('input[name="clarificationTitle"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[maxSize[50]]');
		            data.form.find('input[name="raisedDate"]').addClass('validate[required]');					  							           
		            data.form.validationEngine();
		            return data.form.validationEngine('validate');
		          },   
                 //Dispose validation logic when form is closed
                 formClosed: function (event, data) {
                    data.form.validationEngine('hide');
                    data.form.validationEngine('detach');
                }, 
			 	}); 
	$('#JtableSingleContainer').jtable('load');
}