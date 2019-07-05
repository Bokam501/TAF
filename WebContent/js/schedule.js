var Scheduling = function() {
  
   var initialise = function(jsonObj){	    
	   scheduleCronGen(jsonObj);	   
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

var jsonObjScheduling="";

var viewSchedule = "ViewSchedule";
var deleteSchedule = "deleteSchedule";

function scheduleCronGen(jsonObj){
	
	$("#schedulingID .modal-header h4").text("");
	$("#schedulingID .modal-header h4").text(jsonObj.Title);
	$("#schedulingID .modal-header h5").text("");	
	$("#schedulingID .modal-header h5").text(jsonObj.SubTitle);
	$("#schedulingID").find(".input-group").remove();
	
	$("#schedulingID").modal();	    
	$("#cron").cronGen();
	
	jsonObjScheduling = jsonObj;	
	displayListScheduling(jsonObjScheduling.rowID, jsonObjScheduling.rowType);
}

function displayListScheduling(rowID, rowType){
	var url ='entity.schedule.view?entityId='+rowID+'&entityType='+rowType;
	listScheduling(viewSchedule, url);			
}

function closeScheduleCronGen() {	
	$("#div_PopupBackground").fadeOut("normal");	
	$("#codeMirrorTextEditor").fadeOut("normal");	
	
	var selectedTab=$("#tabslist>li.active").index();
	tabSelection(selectedTab);
}

function deleteCronExpression(){	
	var url ='entity.schedule.delete?entityId='+jsonObjScheduling.rowID+'&entityType='+jsonObjScheduling.rowType;
	listScheduling(deleteSchedule, url);	
}

function saveCodeMirrorTextEditor(jsonObj){	
	var cronExp = $("#schedulingID").find(".form-control").val();
	var startDate= document.getElementById("scheduleStartDatePicker").value;
	var endDate= document.getElementById("scheduleEndDatePicker").value;
	
	var url ='entity.schedule.addupdate?entityId='+jsonObjScheduling.rowID+'&entityType='+jsonObjScheduling.rowType+'&cronExpression='+cronExp+'&startDate='+startDate+'&endDate='+endDate;
	$.ajax({
		type:"POST",
		contentType: "application/json; charset=utf-8",
		url:url,
		dataType : 'json',
	    sucess:function(data){
	    	console.log("success in scheduling");
	    	
	    },error:function(data){
	    	console.log("error in scheduling:"+data);
	    	
	    },complete:function(data){
	    	console.log("schedule save completed :");
	    	if(data.responseJSON.Result == 'OK' &&  typeof data.responseJSON.Record != 'undefined' && typeof data.responseJSON.Record.message != 'undefined' && data.responseJSON.Record.message != ''){
	    		callAlert(data.responseJSON.Record.message);
	    	}else if(data.responseJSON.Result == 'ERROR' || data.responseJSON.Result == 'Error'){
	    		callAlert(data.responseJSON.Message);
	    	}
	    	displayListScheduling(jsonObjScheduling.rowID, jsonObjScheduling.rowType);
	    }		
	});
	
	//$("#scheduleCancel").trigger('click');
}

function listScheduling(value, url){
	
	 $.ajax({
       type: "POST",
 		contentType: "application/json; charset=utf-8",
       url : url,
       dataType : 'json',
       success : function(data) {
            if(data.Result=="OK"){
            	if(value == viewSchedule){
            		displayUpComingFireTimes(data.Record);
            		
            	}else if(value == deleteSchedule){
            		console.log("Schedule Deleted Successfully", data);
            		if(data.Result == 'OK' &&  typeof data.Record != 'undefined' && typeof data.Record.message != 'undefined' && data.Record.message != ''){
        	    		callAlert(data.Record.message);
        	    	}else if(data.Result == 'ERROR' || data.Result == 'Error'){
        	    		callAlert(data.Message);
        	    	}
            		var url ='entity.schedule.view?entityId='+jsonObjScheduling.rowID+'&entityType='+jsonObjScheduling.rowType;
            		listScheduling(viewSchedule, url);
            		
            		//$("#closeSchedule").trigger('click');
            	}
            }
       },
       error: function(data){
    	   console.log("error in ajax call");
       },
       complete: function(data){
    	   console.log("completed");
       }
	 });
		
}

function displayUpComingFireTimes(jsonObj){
	
	updateDateRecords(jsonObj);
	if(jsonObj.upcomingFireTimes != null){
		
		$("#upComingTriggersID").empty();		
		var result = jsonObj.upcomingFireTimes;
		
		if(result.length==0){
			$("#upComingTriggersID").append("<span style='color: black;' ><b style='margin-left: 101px;'>No Data</b></span>");
			$("#upComingFireTimesItemsLength").text("0");
		}else{
			$("#upComingFireTimesItemsLength").text(result.length);
			$.each(result, function(i,item){ 
				$("#upComingTriggersID").append("<li class='dragDropContainerListItem' style='color: black;'>"+item+"</li>");
		 	});			
		}
	}else{
		$("#upComingTriggersID").empty();	
	}
}

function updateDateRecords(jsonObj){
	if(jsonObj.startDate == null && jsonObj.endDate == null){
		$("#scheduleStartDatePicker").datepicker('setDate','today');
		
		var date = new Date();
		date.setDate(date.getDate()+30);  // display next month
		$("#scheduleEndDatePicker").datepicker('setDate', date);	
		$("#scheduleEndDatePicker").datepicker('update');
	}else{
		if(jsonObj.startDate != null){
			document.getElementById("scheduleStartDatePicker").value = setFormattedDate(jsonObj.startDate);		
			var realStartDate = document.getElementById("scheduleStartDatePicker").value;
			var startDateArr = realStartDate.split('/');
			realStartDate = new Date(startDateArr[2], startDateArr[0]-1, startDateArr[1]);
			$('#scheduleStartDatePicker').datepicker({ dateFormat: 'yy-mm-dd' }); // format to show		
			$("#scheduleStartDatePicker").datepicker('setDate', realStartDate);
		}
		
		if(jsonObj.endDate != null){
			document.getElementById("scheduleEndDatePicker").value = setFormattedDate(jsonObj.endDate);
			var realEndDate = document.getElementById("scheduleEndDatePicker").value;
			var endDateArr = realEndDate.split('/');
			realEndDate = new Date(endDateArr[2], endDateArr[0]-1, endDateArr[1]);
			$('#scheduleEndDatePicker').datepicker({ dateFormat: 'yy-mm-dd' }); // format to show		
			$("#scheduleEndDatePicker").datepicker('setDate', realEndDate);
		}
		
		$("#schedulingID").find(".input-group input").val(jsonObj.cronExpression);
	}
}


