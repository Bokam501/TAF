var newnodetype = "";
var selectedStatus = "";
var status = "2";
var primaryStatusActive = "1";
var secondaryStatusActive = "1";

var urlToStatusDetails = "";
var urlToPrimaryStatusDetails = "status.primary.by.status";
var urlToSecondaryStatusDetails = "status.secondary.by.status";

jQuery(document).ready(function() {	
	
	QuickSidebar.init(); // init quick sidebar
	setBreadCrumb(userRole);
	createHiddenFieldsForTree();
	setPageTitle("Manage Status");
	$('#treeStructure-portlet-light .portlet-title .tools').css('margin-right','12px');
	getTreeData('dimension.type.tree?dimensionTypeId=2');
	
	urlToStatusDetails = 'dimension.list.by.status?status=1&dimensionTypeId=2';
	
	if(userRole != 'TESTMANAGER' && userRole != 'TestManager'){
		listStatus();		
	}
	
	$("#treeContainerDiv").on("loaded.jstree", function(evt, data){
	   var defaultNodeId = "j1_1";
	   $.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
   });
	
	$("#treeContainerDiv").on("select_node.jstree",
	     function(evt, data){
			
   			var entityIdAndType =  data.node.data;
   			var arry = entityIdAndType.split("~");
   			var key = arry[0];
   			var type = arry[1];
   			var name = arry[2];
   			var title = data.node.text;
			var date = new Date();
		    var timestamp = date.getTime();
		    var nodeType = type;
		    var parent = data.node.original.parent;
		    var loMainSelected = data;
	        uiGetParents(loMainSelected);
	        
	        var id = $("#status_ul_status").find('option:selected').val();
			if(parent == "#"){
				urlToStatusDetails = 'dimension.list.by.status?status='+id+'&dimensionTypeId=2';
				newnodetype=nodeType;
				listStatus();
			}else{
				if(name.toLowerCase() != "no status available"){
					selectedStatus = key;
					$("#statusList").hide();
					$('#tabslistStatus li').first().find("a").trigger("click");
					$("#statusTabs").show();
					$("#tbCntnt").show();
				}else{
					$("#statusList").hide();
				}
			}
		}
	);
	
	$(document).on('change','#status_ul_status', function() {
		var id = $("#status_ul_status").find('option:selected').val();
	    urlToStatusDetails = 'dimension.list.by.status?status='+id+'&dimensionTypeId=2';
	    listStatus();
	});
	
	$(document).on('change','#status_ul_primaryStatus', function() {
		primaryStatusActive = $("#status_ul_primaryStatus").find('option:selected').val();
	    listPrimaryStatus();
	});
	
	$(document).on('change','#status_ul_secondaryStatus', function() {
		secondaryStatusActive = $("#status_ul_secondaryStatus").find('option:selected').val();
		listSecondaryStatus();
	});
	
});


function listStatus(){
	$("#statusTabs").hide();
	$("#tbCntnt").hide();
	$("#StatusList").show();
	try{
		if ($('#jTableContainerStatus').length>0) {
			$('#jTableContainerStatus').jtable('destroy'); 
		}
	}catch(e){}
	
	 //init jTable
	 $('#jTableContainerStatus').jtable({
         title: 'Status',
         selecting: true, //Enable selecting 
         paging: true, //Enable paging
         pageSize: 10, //Set page size (default: 10)
         editinline:{enable:true},	         
         //sorting: true, //Enable sortin
         /* saveUserPreferences: false, */
         recordsLoaded: function(event, data) {
        	 $(".jtable-edit-command-button").prop("disabled", true);
         },
         actions: {
             listAction: urlToStatusDetails,
     		 //createAction: 'dimension.add',
             editinlineAction: 'dimension.update'
             //deleteAction: 'dimension.delete'
         },
         fields: {
         				   		
   			dimensionId: { 
   				key: true,
   				type: 'hidden',
   				create: false, 
   				edit: false, 
   				list: false,
   				display: function (dataStatus) {return dataStatus.record.dimensionId;},	   			 
   			}, 
   			name: { 
     	  		title: 'Name',
     	  		width: '20%',
     	  		edit: true,
     	  		inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
     	  		list:true
  			},
  		  	description: { 
       			title: 'Description' ,
       			width: '30%',
       			edit: true,
     		  	list:true
    	  	}, 
    	  	parentId:{
            	title: 'Parent Status',
            	width: '25%',
            	list:false,
            	edit: false,
            	create:false,
            	options:function(data){
		        	return 'dimension.list.options?dimensionTypeId=2';
		        },		        	 	
            },  
			status: {	      
		       	title: 'Status' ,
		       	width: '15%',
	        	list:true,
	        	edit:true,
	        	create:false,
	        	type : 'checkbox',
	        	values: {'0' : 'No','1' : 'Yes'},
		   		defaultValue: '1'
		    },
		    ownerName: { 
       			title: 'Owner' ,
       			width: '30%',
       			edit: false,
     		  	create: false,
     		  	list:true
    	  	}, 
    	  	
         },
         
         //Moved Formcreate validation to Form Submitting
         //Validate form when it is being submitted
         formSubmitting: function (event, data) {
        	 data.form.find('input[name="name"]').addClass('validate[required, custom[Letters_loworup_noSpec]]');
             data.form.validationEngine();
             return data.form.validationEngine('validate');
         }, 
         
         //Dispose validation logic when form is closed
         formClosed: function (event, data) {
             data.form.validationEngine('hide');
             data.form.validationEngine('detach');
         }
     });		 
	 $('#jTableContainerStatus').jtable('load');		 
}

$(document).on('click', '#tabslistStatus>li', function(){
	selectedTab=$(this).index();
	statusTabSelection(selectedTab);
});

function statusTabSelection(selectedTab){
	status = "2";
	if(selectedTab==0){						  
		var firstTab = $(".Summary");
		if(!(firstTab.hasClass("active in"))){
			firstTab.addClass("active in");
			firstTab.siblings(".tab-pane").removeClass("active in");
		}
		showStatusSummaryDetails(selectedStatus);
	  
	 }else if (selectedTab==1){
		primaryStatusActive = 1;
		 $('select option[value="1"]').attr("selected",true);
		listPrimaryStatus();
	}else if (selectedTab==2){
		secondaryStatusActive = 1;
		$('select option[value="1"]').attr("selected",true);
		listSecondaryStatus();
	}
}


function listPrimaryStatus(){
	
	try{
		if ($('#jTableContainerPrimaryStatus').length>0) {
			$('#jTableContainerPrimaryStatus').jtable('destroy'); 
		}
	}catch(e){}
	
	 //init jTable
	 $('#jTableContainerPrimaryStatus').jtable({
         title: 'Primary Status',
         selecting: true, //Enable selecting 
         paging: true, //Enable paging
         pageSize: 10, //Set page size (default: 10)
         editinline:{enable:true},	         
         //sorting: true, //Enable sortin
         /* saveUserPreferences: false, */
         recordsLoaded: function(event, data) {
        	 $(".jtable-edit-command-button").prop("disabled", true);
         },
         recordUpdated:function(event, data){
 			$('#jTableContainerPrimaryStatus').find('.jtable-child-table-container').jtable('reload');
 		 },
 		 recordAdded: function (event, data) {
 			$('#jTableContainerPrimaryStatus').find('.jtable-child-table-container').jtable('reload');
 			listPrimaryStatus();
 		 },
 		 recordDeleted: function (event, data) {
 			$('#jTableContainerPrimaryStatus').find('.jtable-child-table-container').jtable('reload');
 		 },
         actions: {
             listAction: urlToPrimaryStatusDetails+'?statusId='+selectedStatus+'&activeStatus='+primaryStatusActive,
     		 createAction: 'status.primary.add?statusId='+selectedStatus,
             editinlineAction: 'status.primary.update?statusId='+selectedStatus,
             //deleteAction: 'status.delete'
         },
         fields: {
         				   		
        	 activityStatusId: { 
				key: true,
				type: 'hidden',
				create: false, 
				edit: false, 
				list: false,
			},
			activityStatusName: { 
	  	  		title: 'Name',
	  	  		inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
	  	  		width: '20%',
	  	  		create: true,
	  	  		edit: true,
	  	  		list:true,
   			},
   			activityStatusDescription: { 
	  	  		title: 'Description',
	  	  		inputTitle: 'Description <font color="#efd125" size="4px">*</font>',
	  	  		width: '20%',
	  	  		create: true,
	  	  		edit: true,
	  	  		list:true,
   			},
   			statusCategoryId: { 
	  	  		title: 'Status Category',
	  	  		inputTitle: 'Status Category <font color="#efd125" size="4px">*</font>',
	  	  		width: '20%',
	  	  		create: true,
	  	  		edit: true,
	  	  		list:true,
	  	  		defaultValue : 1,
		  	  	options:function(data){
				 	return 'status.category.option.list';
	     		}
   			},
   			secondaryStatus:{ 
   	          	title: '',
   	          	width: "5%",
   	          	edit: true,
   	          	create: false,
   	          
   	     	 	display: function (primaryStatusData) { 
   	     		//Create an image that will be used to open child table 
   	     			var $img = $('<img src="css/images/mapping.png" title="Map / Unmap Secondary status" data-toggle="modal" data-target="#dragListItemsContainer" />');
   	     			//Open child table when user clicks the image 
   	     			$img.click(function () {
   	     				$img = manageSecondaryStatusMapping($img, primaryStatusData.record.activityStatusId, primaryStatusData.record.activityStatusName);
   	     			});
   	     			return $img; 
   	         	} 
   	 	  	},
	   	 	activeStatus: {	      
		       	title: 'Status' ,
	        	list:true,
	        	edit:true,
	        	create:false,
	        	type : 'checkbox',
	        	values: {'0' : 'No','1' : 'Yes'},
		   		defaultValue: '1'
		    },
         },
         
         //Moved Formcreate validation to Form Submitting
         //Validate form when it is being submitted
         formSubmitting: function (event, data) {
        	 data.form.find('input[name="activityStatusName"]').addClass('validate[required], custom[minSize[3]], custom[maxSize[50]]');
        	 data.form.find('input[name="activityStatusDescription"]').addClass('validate[required], custom[minSize[3]], custom[maxSize[500]]');
             data.form.validationEngine();
             return data.form.validationEngine('validate');
         }, 
         //Dispose validation logic when form is closed
         formClosed: function (event, data) {
             data.form.validationEngine('hide');
             data.form.validationEngine('detach');
         }
     });		 
	 $('#jTableContainerPrimaryStatus').jtable('load');		 
}


function listSecondaryStatus(){
	
	try{
		if ($('#jTableContainerSecondaryStatus').length>0) {
			$('#jTableContainerSecondaryStatus').jtable('destroy'); 
		}
	}catch(e){}
	
	 //init jTable
	 $('#jTableContainerSecondaryStatus').jtable({
         title: 'Secondary Status',
         selecting: true, //Enable selecting 
         paging: true, //Enable paging
         pageSize: 10, //Set page size (default: 10)
         editinline:{enable:true},	         
         //sorting: true, //Enable sortin
         /* saveUserPreferences: false, */
         recordsLoaded: function(event, data) {
        	 $(".jtable-edit-command-button").prop("disabled", true);
         },
         recordUpdated:function(event, data){
 			$('#jTableContainerSecondaryStatus').find('.jtable-child-table-container').jtable('reload');
 		 },
 		 recordAdded: function (event, data) {
 			$('#jTableContainerSecondaryStatus').find('.jtable-child-table-container').jtable('reload');
 			listPrimaryStatus();
 		 },
 		 recordDeleted: function (event, data) {
 			$('#jTableContainerSecondaryStatus').find('.jtable-child-table-container').jtable('reload');
 		 },
         actions: {
             listAction: urlToSecondaryStatusDetails+'?statusId='+selectedStatus+'&activeStatus='+secondaryStatusActive,
     		 createAction: 'status.secondary.add?statusId='+selectedStatus,
             editinlineAction: 'status.secondary.update?statusId='+selectedStatus,
             //deleteAction: 'status.delete'
         },
         fields: {
         				   		
        	 activitySecondaryStatusId: { 
				key: true,
				type: 'hidden',
				create: false, 
				edit: false, 
				list: false,
			},
			activitySecondaryStatusName: { 
	  	  		title: 'Name',
	  	  		inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
	  	  		width: '20%',
	  	  		create: true,
	  	  		edit: true,
	  	  		list:true,
   			},
   			description: { 
	  	  		title: 'Description',
	  	  		inputTitle: 'Description <font color="#efd125" size="4px">*</font>',
	  	  		width: '20%',
	  	  		create: true,
	  	  		edit: true,
	  	  		list:true,
   			},
   			activeStatus: {	      
		       	title: 'Status' ,
	        	list:true,
	        	edit:true,
	        	create:false,
	        	type : 'checkbox',
	        	values: {'0' : 'No','1' : 'Yes'},
		   		defaultValue: '1'
		    },
         },
         
         //Moved Formcreate validation to Form Submitting
         //Validate form when it is being submitted
         formSubmitting: function (event, data) {
        	 data.form.find('input[name="activitySecondaryStatusName"]').addClass('validate[required], custom[minSize[3]], custom[maxSize[50]]');
        	 data.form.find('input[name="description"]').addClass('validate[required], custom[minSize[3]], custom[maxSize[500]]');
             data.form.validationEngine();
             return data.form.validationEngine('validate');
         }, 
         //Dispose validation logic when form is closed
         formClosed: function (event, data) {
             data.form.validationEngine('hide');
             data.form.validationEngine('detach');
         }
     });		 
	 $('#jTableContainerSecondaryStatus').jtable('load');			 
}


/* DragDropListItem Plugin started */ 		 
var jsonSecondaryStatusObj='';

function leftDraggedItemURLChanges(value,type){
	if(type==jsonSecondaryStatusObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
	    x =  value.split("("),
	    itemid = x[0].trim(),	
	    result = '&secondaryStatusId='+itemid+''+leftDragUrlConcat;
	}
	return result;
}

function rightDraggedItemURLChanges(value,type){
	result='';
	if(type==jsonSecondaryStatusObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",
	    x =  value.split("("),
	    itemid = x[0].trim(),		
	    result = '&secondaryStatusId='+itemid+''+rightDragtUrlConcat;
	}
	return result;
}

function leftItemDislayListItem(item, jsonObj){
	var resultList="";
	var entity_id = item.itemId;
	var entity_name = item.itemName;	
	var entity_dispname = ''
		
	if(jsonObj.componentUsageTitle==jsonSecondaryStatusObj.componentUsageTitle){
		var entity_code = item.itemCode;
		if(entity_code == null){
			entity_dispname = entity_id+" ("+entity_name+")";
		}else{
			entity_dispname = entity_id+" ("+entity_code+")"+" ("+entity_name+")";
		}		
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";	
	}
	return resultList;	
}

function rightItemDislayListItem(item, jsonObj){
	var resultList="";
	var entity_id = item.itemId;	
	var entity_name = item.itemName;
	var entity_dispname = '';
		
	if(jsonObj.componentUsageTitle==jsonSecondaryStatusObj.componentUsageTitle){
		 var entity_code = item.itemCode;
		 if(entity_code == null){
				entity_dispname = entity_id+" ("+entity_name+")";
			}else{
				entity_dispname = entity_id+" ("+entity_code+")"+" ("+entity_name+")";
			}		
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	return resultList;
}

function trim(str) {
    return str.replace(/^\s+|\s+$/g,"");
}

/* DragDropListItem Plugin ended */

function manageSecondaryStatusMapping($img, primaryStatusId, primaryStatusName){
	
	var leftUrl="secondary.status.available.for.primary.status.count?primaryStatusId="+primaryStatusId+"&statusId="+selectedStatus;							
	var rightUrl = "";
	var leftDefaultUrl="secondary.status.available.for.primary.status?primaryStatusId="+primaryStatusId+"&statusId="+selectedStatus+"&jtStartIndex=0&jtPageSize=50";
	var rightDefaultUrl = "secondary.status.list.for.primary.status?primaryStatusId="+primaryStatusId+"&statusId="+selectedStatus+"&jtStartIndex=0&jtPageSize=50";									
	var leftDragUrl = "secondary.status.for.primary.status.mapping?primaryStatusId="+primaryStatusId;
	var rightDragtUrl = "secondary.status.for.primary.status.mapping?primaryStatusId="+primaryStatusId;
	var leftPaginationUrl = "secondary.status.available.for.primary.status?primaryStatusId="+primaryStatusId+"&statusId="+selectedStatus;
	var rightPaginationUrl="";									
	jsonSecondaryStatusObj={"Title":"Secondary Status mapping for - "+primaryStatusName,
		"leftDragItemsHeaderName":"Available Secondary Status",
		"rightDragItemsHeaderName":"Mapped Secondary Status",
		"leftDragItemsTotalUrl":leftUrl,
		"rightDragItemsTotalUrl":rightUrl,
		"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
		"rightDragItemsDefaultLoadingUrl":rightDefaultUrl,
		"leftDragItemUrl":leftDragUrl,
		"rightDragItemUrl":rightDragtUrl,
		"leftItemPaginationUrl":leftPaginationUrl,
		"rightItemPaginationUrl":rightPaginationUrl,									
		"leftDragItemsPageSize":"50",
		"rightDragItemsPageSize":"50",
		"noItems":"No Secondary status to show",	
		"componentUsageTitle":"secondaryStatusForPrimaryStatus",											
	};
	
	DragDropListItems.init(jsonSecondaryStatusObj); 
		
}