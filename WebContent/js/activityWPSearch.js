
function ResetSearchDetail(){		
	$('#wpStatusSearch_ul').select2('data', null);	
	$('#wpAssigneeSearch_ul').select2('data', null);
	$('#wpPrioritySearch_ul').select2('data', null);	
	 listWorkRequestsOfSelectedProductBuild("yes");
    return false;
	}
function ActivitySearchInitloadList(){  	
	loadSearchStatusList();
	loadSearchAssigneeList();				
	loadSearchExecutionPriorityList();
}

function loadSearchStatusList(){	
$('#wpStatusSearch_ul').empty();	
$.post('status.category.option.list',function(data) {		
    var ary = (data.Options);
    $.each(ary, function(index, element) {
        $('#wpStatusSearch_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
		});
		});
}


function loadSearchAssigneeList(){
$('#wpAssigneeSearch_ul').empty();	
$.post('common.user.list.by.resourcepool.id.productId?productId='+document.getElementById("treeHdnCurrentProductId").value,function(data) {
	
	//$('#wpassigneeSearch_ul').append('<option selected value="-">-</option> ');
	
    var ary = (data.Options);
    $.each(ary, function(index, element) {
        $('#wpAssigneeSearch_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
		});
		});
} 



function loadSearchExecutionPriorityList(){
$('#wpPrioritySearch_ul').empty();	
$.post('administration.executionPriorityList',function(data) {		
    var ary = (data.Options);
    $.each(ary, function(index, element) {
        $('#wpPrioritySearch_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
		});
		});
} 

function SearchDetail(){	
	
	 var status=$("#wpStatus_dd .select2-chosen").text();
	 var assignee=$("#wpAssignee_dd .select2-chosen").text();		
	 var priority=  $("#wpPriorityList_dd .select2-chosen").text();		 
	var reviewfilter =-1;
	var statusId ="";
	var searchFlag =1;
	if((status!="-")&&(status!="")){
		statusId = $("#wpStatusSearch_ul").find('option:selected').attr('id');
	}else{
		statusId=-1;
	}		
	
	 var assigneeId="";
	 if((assignee!="-")&&(assignee!="")){
		 assigneeId= $("#wpAssigneeSearch_ul").find('option:selected').attr('id');
	 }else{
		 assigneeId=-1;
	 }
			
	var priorityId ="";
	if((priority!="-")&&(priority!="")){
		priorityId = $("#wpPrioritySearch_ul").find('option:selected').attr('id');
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
