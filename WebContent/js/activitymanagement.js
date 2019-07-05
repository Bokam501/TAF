
function ResetActivityDetail(){	
	datepickerenddate.value="";	
	$('#reviewer_ul').select2('data', null);
	$('#category_ul').select2('data', null);
	$('#assignee_ul').select2('data', null);
	$('#executionPriorityList_ul').select2('data', null);	
    return false;
	}

function ActivityInitloadList(){
	    $("#datepickerenddate").datepicker('setDate','');
		loadReviewerList();
		loadAssigneeList();								
		loadCategoryList();							
		loadExecutionPriorityList();
}

function loadReviewerList(){	
	$('#reviewer_ul').empty();	
	$.post('common.user.list.by.resourcepool.id.productId?productId='+document.getElementById("treeHdnCurrentProductId").value,function(data) {		
        var ary = (data.Options);
        $.each(ary, function(index, element) {
	        $('#reviewer_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
			});
 		});
}


function loadAssigneeList(){
	$('#assignee_ul').empty();	
	$.post('common.user.list.by.resourcepool.id.productId?productId='+document.getElementById("treeHdnCurrentProductId").value,function(data) {	
        var ary = (data.Options);
        $.each(ary, function(index, element) {
	        $('#assignee_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
			});
 		});
} 


function loadCategoryList(){	
	$('#category_ul').empty();	
	$.post('common.list.executiontypemaster.byentityid?entitymasterid=1',function(data) {		
        var ary = (data.Options);
        $.each(ary, function(index, element) {
	        $('#category_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
			});
 		});
}


function loadExecutionPriorityList(){
	$('#executionPriorityList_ul').empty();	
	$.post('administration.executionPriorityList',function(data) {		
        var ary = (data.Options);
        $.each(ary, function(index, element) {
	        $('#executionPriorityList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
			});
 		});
} 


/* DragDropListItem Plugin started */ 		 
var jsonActivityObj='';
var jsonTestCaseTabObj = '';
function leftDraggedItemURLChanges(value,type){	
	if(type==jsonActivityObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",	    		
		    x =  value.split("("),
		    itemid = x[0],	
		    result ='&enviCombiId='+itemid+''+leftDragUrlConcat;
	}
	else if(type==jsonUserMappingForWP.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
	    x =  value.split("("),
	    itemid = x[0],	
	    result ='&userId='+itemid+''+leftDragUrlConcat;
	}
	else if(type==jsonActivityChangeRequestObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
	    x =  value.split("("),
	    itemid = x[0],	
	    result ='&changeRequestId='+itemid+''+leftDragUrlConcat;
	}
	return result;
}

function rightDraggedItemURLChanges(value,type){
	result='';
	if(type==jsonActivityObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
			rightDragtUrlConcat="&maporunmap=map",
		    x =  value.split("("),
		    itemid = x[0],		
		    result ='&enviCombiId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type==jsonUserMappingForWP.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",
	    x =  value.split("("),
	    itemid = x[0],		
	    result ='&userId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type==jsonActivityChangeRequestObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",
	    x =  value.split("("),
	    itemid = x[0],		
	    result ='&changeRequestId='+itemid+''+rightDragtUrlConcat;
	}
	return result;
}
function trim(str) {
    return str.replace(/^\s+|\s+$/g,"");
}
function leftItemDislayListItem(item, jsonObj){
	var resultList="";
	var entity_id = item.itemId;
	var entity_name = item.itemName;	
	var entity_dispname = ''
		
	if(jsonObj.componentUsageTitle==jsonActivityObj.componentUsageTitle){
		//var entity_code = item.itemCode;
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";	
	}
	
	else if(jsonObj.componentUsageTitle == jsonUserMappingForWP.componentUsageTitle){	
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	
	else if(jsonObj.componentUsageTitle == jsonActivityChangeRequestObj.componentUsageTitle){	
		entity_dispname = entity_id+" ("+entity_name+")";
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
		
	if(jsonObj.componentUsageTitle==jsonActivityObj.componentUsageTitle){
		 //var entity_code = item.itemCode;
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonUserMappingForWP.componentUsageTitle){	
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}	
	else if(jsonObj.componentUsageTitle == jsonActivityChangeRequestObj.componentUsageTitle){	
		 entity_dispname = entity_id+" ("+entity_name+")";
		 entity_dispname=trim(entity_dispname);	
		 resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}	
	
	
	return resultList;
}
	 	
function listActivityAuditHistory(activityId){
	clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=Activity&sourceEntityId='+activityId+'&jtStartIndex=0&jtPageSize=1000';
	//var url='administration.event.list?sourceEntityType=Activity&sourceEntityId='+activityId;
	var jsonObj={"Title":"Activity Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":1000,	    
			"componentUsageTitle":"activityAudit",
	};
	SingleDataTableContainer.init(jsonObj);
	//SingleJtableContainer.init(jsonObj);
}
