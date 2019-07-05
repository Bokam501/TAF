
function ResetSearchDetail(){		
	$('#statusSearch_ul').select2('data', null);	
	$('#assigneeSearch_ul').select2('data', null);
	$('#prioritySearch_ul').select2('data', null);	
    enableAddOrNot = "yes";
		productId=document.getElementById("treeHdnCurrentProductId").value;
		productVersionId=document.getElementById("treeHdnCurrentProductVersionId").value;
		productBuildId=document.getElementById("treeHdnCurrentProductBuildId").value;
		activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
		activityId = document.getElementById("treeHdnCurrentActivityId").value;		
		var isActive = $("#isActiveTask_ul").find('option:selected').val();		
		listActivitiesOfSelectedActivity(productId,productVersionId,productBuildId,activityWorkPackageId,activityId,isActive,enableAddOrNot,0,0);   
    return false;
	}
function ActivitySearchInitloadList(){  	
	loadStatusList();
	loadAssigneeList();				
	loadExecutionPriorityList();
}

function loadStatusList(){	
$('#statusSearch_ul').empty();	
$.post('activity.primary.status.master.option.list?productId='+document.getElementById("treeHdnCurrentProductId").value,function(data) {		
    var ary = (data.Options);
    $.each(ary, function(index, element) {
	    $('#statusSearch_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');   
	    });
		});
}


function loadAssigneeList(){
$('#assigneeSearch_ul').empty();	
$.post('common.user.list.by.resourcepool.id.productId?productId='+document.getElementById("treeHdnCurrentProductId").value,function(data) {	
    var ary = (data.Options);
    $.each(ary, function(index, element) {
        $('#assigneeSearch_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
		});
		});
} 



function loadExecutionPriorityList(){
$('#prioritySearch_ul').empty();	
$.post('administration.executionPriorityList',function(data) {		
    var ary = (data.Options);
    $.each(ary, function(index, element) {
        $('#prioritySearch_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
		});
		});
} 

function SearchDetail(){	
	
	 var status=$("#searchStatus_dd .select2-chosen").text();
	 var assignee=$("#searchAssignee_dd .select2-chosen").text();		
	 var priority=  $("#searchPriorityList_dd .select2-chosen").text();	 
	 var statusId ="";
		var searchFlag =1;
		if((status!="-")&&(status!="")){
			statusId = $("#statusSearch_ul").find('option:selected').attr('id');
		}else{
			statusId=-1;
		}	
	
	var assigneeId="";
	 if((assignee!="-")&&(assignee!="")){
		 assigneeId= $("#assigneeSearch_ul").find('option:selected').attr('id');
	 }else{
		 assigneeId=-1;
	 }
			
	var priorityId ="";
	if((priority!="-")&&(priority!="")){
		priorityId = $("#prioritySearch_ul").find('option:selected').attr('id');
	}else{
		priorityId=-1;
	}	
  
	 if(assigneeId==-1 && statusId==-1&& priorityId==-1){
			callAlert("Select atleast one Option","ok");
		}
		
		else{			
			$('#jTableContainer').jtable('load',
				{				
				searchassigneeId: assigneeId,
				searchstatusId: statusId,
				searchpriority:priorityId,
				searchFlag:searchFlag
			});			
		}
	
}
