
var newnodetype = "";
var currentProductId = "";
var testFactoryId=0;
var isFirstLoad = true;
var selectedTab = 0;
var raisedby = $("#userdisplayname").text().split('[')[0].trim();
var entityTypes=[33,34,30,3];

var nodeType='';
var engagementId=0
var loginUserId;

jQuery(document).ready(function() {	
	
	QuickSidebar.init(); // init quick sidebar
	setBreadCrumb(userRole);
	createHiddenFieldsForTree();
	setPageTitle("My Activities");
	$('#treeStructure-portlet-light .portlet-title .tools').css('margin-right','12px');	
	
	//getTreeData('workflow.my.actions.product.tree');
	//setDefaultnode("j1_1");
	
	showWorkPackageTreeData();	
	var elementDivId = "#treeContainerDiv";
	 $(elementDivId).on("loaded.jstree", function(evt, data){
		 setDefaultnode();		
    });	
	$("#addCommentsDateTimePickerBox").show();
	
	setLoginUserId();
	
	$("#treeContainerDiv").on("select_node.jstree",
		     function(evt, data){		
				if(data.node != undefined){
					
		   			var entityIdAndType =  data.node.data;
		   			var arry = entityIdAndType.split("~");
		   			var key = arry[0];
		   			var type = arry[1];
		   			var name = arry[2];
				    //var nodeType = type;
		   			nodeType = type.toLowerCase();
				    var parent = data.node.original.parent;
				    var loMainSelected = data;
			        uiGetParents(loMainSelected);
			        var selectedIndex = $("#tabslistmyActivities >li.active").index();
			        
			        if(nodeType == "TestFactory".toLowerCase()){
			        	newnodetype = nodeType;			        	
			        	currentProductId=-1;
			        	productId=-1;
			        	activityWorkPackageId=0;
			        	testFactoryId = key;			        	
			        	engagementId = testFactoryId;			        	
			        	showMyActivities(selectedIndex);
			        	$("#myActivitiesTab").show();
						$("#tbCntnt").show();						
			        
			        }else if(nodeType == 'Product' || nodeType == 'product' || nodeType == "activityworkpackageicon"){
			        	currentProductId = key;
			        	productId=key;
			        	activityWorkPackageId=0;
		        	
			        	//  ----- Commented code: getting total count url is taking more time than need to be fixed once done this code will  be reverted back
			        	$('.pendingActionCount').text(0);		        	
			        	showMyActivities(selectedIndex);
			        	$("#myActivitiesTab").show();
						$("#tbCntnt").show();
			        
			        }else if(nodeType == 'activityworkpackage' || nodeType == 'activityicon'){
			        	activityWorkPackageId = key;
			        	document.getElementById("treeHdnCurrentActivityWorkPackageId").value = key;
			        	showMyActivities(selectedIndex);
			        	$("#myActivitiesTab").show();
						$("#tbCntnt").show();
		        				        	
			        }else{			        	
						if(name.toLowerCase() != "no competency available"){
							selectedCompetency = key;						
							$("#myActivitiesTab").show();
							$("#tbCntnt").show();
						}
					}
				}
			}
		);
});

function showWorkPackageTreeData(){	
	if(userRole == 'Tester'){
		var jsonObj={"Title":"",				
	    	 	"urlToGetTree": 'administration.activity.workpackage.grouping.tree?type=2',
	    	 	"urlToGetChildData": 'administration.activity.workpackage.grouping.tree?type=2',	    	 	
		};	
	}else{
		var jsonObj={"Title":"",				
	    	 	"urlToGetTree": 'administration.activity.workpackage.grouping.tree?type=1',
	    	 	"urlToGetChildData": 'administration.activity.workpackage.grouping.tree?type=1',	    	 	
		};	
	}
	 
	 TreeLazyLoading.init(jsonObj);	 
}

function setDefaultnode() {		
	
	defaultNodeId = $('#treeContainerDiv li').eq(0);
	$.jstree.reference('#treeContainerDiv').deselect_all();
	$.jstree.reference('#treeContainerDiv').close_all();
	$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
	$.jstree.reference('#treeContainerDiv').trigger("select_node");
	
}

var defaultActivityWorkpackagePlannedStartDate = new Date();
var defaultActivityWorkpackagePlannedEndDate = new Date(); 
var jTableContainerDataTable='';
var engagementId =0;
var entityTypeId = 33;
var myActivitiesSelectedTabUrl='';
function showMyActivities(level){
	// level 0 : Assignee level
	// level 1 : PendingWith level
	// level 2 : Passing Through
	var activityTable=''; 
	if(level == 0){		
		activityTable = '<table id="assigneeActivitiesContainer"  class="cell-border compact row-border" cellspacing="0" width="100%">'+
			'<thead></thead><tfoot><tr></tr></tfoot></table>';	
		$("#assigneeActivitiesContainerParent").html('');
		$("#assigneeActivitiesContainerParent").append(activityTable);
		
		jTableContainerDataTable = "assigneeActivitiesContainer";
		myActivitiesSelectedTabUrl = 'my.activity.list?testFactoryId='+testFactoryId+'&productId='+currentProductId+'&activityWorkPackageId='+activityWorkPackageId+'&tabValue='+level;
		
	}else if(level == 1){
		activityTable = '<table id="pendingWithActivitiesContainer"  class="cell-border compact row-border" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';	
		$("#pendingWithActivitiesContainerParent").html('');
		$("#pendingWithActivitiesContainerParent").append(activityTable);
	
		jTableContainerDataTable = "pendingWithActivitiesContainer";
		myActivitiesSelectedTabUrl = 'my.activity.list?testFactoryId='+testFactoryId+'&productId='+currentProductId+'&activityWorkPackageId='+activityWorkPackageId+'&tabValue='+level;
		
	}else if(level == 2){
		activityTable = '<table id="passingThroughContainer"  class="cell-border compact row-border" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';	
		$("#passingThroughContainerParent").html('');
		$("#passingThroughContainerParent").append(activityTable);
	
		jTableContainerDataTable = "passingThroughContainer";
		myActivitiesSelectedTabUrl = 'my.activity.list?testFactoryId='+testFactoryId+'&productId='+currentProductId+'&activityWorkPackageId='+activityWorkPackageId+'&tabValue='+level;		
	}	
	
	var activityWPId=0;
	var enableAddOrNot="yes";
	
	engagementId = testFactoryId;
	document.getElementById("treeHdnCurrentProductId").value = currentProductId;
	
	listActivitiesOfSelectedAWP_DT(currentProductId,0,0,activityWPId,1,enableAddOrNot, jTableContainerDataTable);
}

function createCustomFields(){
	var jsonObj={"Title":"Custom Fields For Activity : ["+activitiesJsonObj.activityId+"] "+activitiesJsonObj.activityName,
			"subTitle": "",
			"url": "data/data.json",
			"columnGrouping":2,
			"containerSize": "large",
			"componentUsageTitle":"customFields",
			"entityId":"28",
			"entityTypeId":activitiesJsonObj.activityTypeId,
			"entityInstanceId":activitiesJsonObj.activityId,
			"parentEntityId":"18",
			"parentEntityInstanceId":activitiesJsonObj.productId,
			"engagementId":activitiesJsonObj.testFactId,
			"productId":activitiesJsonObj.productId,
			"singleFrequency":"customFieldSingleFrequencyContainer",
			"multiFrequency":"customFieldMultiFrequencyContainer",
			
	};
	//CustomFieldGropings.init(jsonObj);	
	return jsonObj;
}

function fullScreenHandlerDTMyActivities(){
	
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
		
		$('#activityEngagementToggle .toolbarFullScreen').css('display','block');		
		$('#ProductACTWPSummaryPage .toolbarFullScreen').css('display','block');
		$('#ACTWPSummaryPage .toolbarFullScreen').css('display','block');
		$('#tabslistmyActivities').show();
		
		myActivitiesDTFullScreenHandler(true);
		
	}else{		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
		
		$('#activityEngagementToggle .toolbarFullScreen').css('display','none');		
		$('#ProductACTWPSummaryPage .toolbarFullScreen').css('display','none');
		$('#ACTWPSummaryPage .toolbarFullScreen').css('display','none');
		$('#tabslistmyActivities').hide();
		
		myActivitiesDTFullScreenHandler(false);								
	}
}

function myActivitiesDTFullScreenHandler(flag){
	if(flag){
		reInitializeDTMyActivityTab();
		$("#"+jTableContainerDataTable+"_wrapper .dataTables_scrollBody").css('max-height','160px');
	}else{
		reInitializeDTMyActivityTab();
		$("#"+jTableContainerDataTable+"_wrapper .dataTables_scrollBody").css('max-height','350px');
	}
}

function reInitializeDTMyActivityTab(){
	clearTimeoutDTActivitytab = setTimeout(function(){
		if(activityManagementTab_oTable != ""){
			activityManagementTab_oTable.DataTable().columns.adjust().draw();
			clearTimeout(clearTimeoutDTActivitytab);
		}
	},200);
}