var workPackageType ="";
var key='';
var nodeType="";
var prodId ='';
var title ='';
var editableFlag=true;
var defaultNodeId = "";
var isFirstLoad = true;
var selectedShiftName = '';
var weekDetails = "";
var demandRecursive = 0;
var selectedDemandDetails = {};
var selectedWorkWeekForDemand = 0;
var selectedShiftIdForDemand = 0;
var wpDemandProjectionIdSource = 0;
var demandResource = 0;
var demandResourceWeeks = 0;
var popupTitle_RaiseResourceDemandFor ="Raise Resource Demand for";
var workPackageName = "";
var popupTitle_RaiseResourceDemand ="Raise Resource Demand";
var popupTitle_AvailableResource="Available Resources For Reservation";
var popupTitle_EnvironmentCombination ="Environment Combinations";
var reservationDate;
var availablilityType = "Booked";
var currentView;
var resourceDemandCount;
var usersCopyData="";
var wpId;
var spShift;
var spSkill;
var spWorkYear;
var spUserTypeId;
var demandId;

function raiseResourceDemandForWeekly(workPackageId, shiftId, workWeek,skillId,roleId,userTypeId){
	if(skillId == null){
		skillId = 0;
	}
	if(roleId == null){
		roleId= 0;
	}
	if(userTypeId == null){
		userTypeId= 0;
	}
	selectedDemandDetails = {
		'workPackageId' : workPackageId,
		'shiftId' : shiftId,
		'workWeek' : workWeek,
		'skillId' : skillId,
		'roleId' : roleId,
		'userTypeId' : userTypeId
	};
		
//	 var currentDemandShiftFieldName = "currentDemandShiftName"+index+shiftId;
//	 var demandForShift = document.getElementById(currentDemandShiftFieldName).value;
//	 document.getElementById("currentDemandShiftName").value = demandForShift;
	if(demandRecursive == 0 || demandWeekFlag){
		demandRecursive = workWeek;
		$("#demandRecursive").val(demandRecursive);
	}else {
		 demandRecursive = $("#demandRecursive").val();
	} 
	isValidWorkPackageStatusForWeekly(workPackageId, shiftId, workWeek,skillId,roleId,userTypeId);
}

function isValidWorkPackageStatusForWeekly(workPackageId,shiftId, workWeek,skillId,roleId,userTypeId){
	$.post('is.valid.workpackage.status?workPackageId='+workPackageId+'&type=RaiseDemand',function(data) {
		if(data.Result=="ERROR"){
			callAlert(data.Message);
    		return false;
   		}else{
   			raiseResourceDemandForWorkPackageForWeekly(workPackageId, shiftId, workWeek,skillId,roleId,userTypeId);
   		}
	});
}

var selectedDetails = {};
function getResourceForBlockingGridViewForWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,reservedCount,demandCount,groupDemandId,userTypeId, workPackageName, workWeekName, userType, userRole, userSkill){
	if(skillId == null){
		skillId = 0;
	}
	if(userRoleId == null){
		userRoleId= 0;
	}
	if(userTypeId == null){
		userTypeId= 0;
	}
	selectedDetails = {};
	selectedDetails["selectedWorkPackageName"] = workPackageName;
	selectedDetails["selectedWorkWeekName"] = workWeekName;
	selectedDetails["selectedUserType"] = userType;
	selectedDetails["selectedUserRole"] = userRole;
	selectedDetails["selectedUserSkill"] = userSkill;
	
	 $("#currentShiftName").val($("#currentShiftName" +shiftId).val());
	isValidWorkPackageForReservationForWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,reservedCount,demandCount,groupDemandId,userTypeId);
}

function isValidWorkPackageForReservationForWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,reservedCount,demandCount,groupDemandId,userTypeId){
	$.post('is.valid.workpackage.status?workPackageId='+workPackageId+'&type=Reservation',function(data) {
		if(data.Result=="ERROR"){
			callAlert(data.Message);
    		return false;
   		}else{
   			getResourceForBlockingGridViewReservationForWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,reservedCount,demandCount,groupDemandId,userTypeId);   			
   		}
	});
}

function reservationDatePickerHandler(event){
	var getID = event.target.id; 
	var id = getID.split('_')[0];
	var oldValue = event.target.defaultValue;
	var modifiedValue = event.target.value;	
	//	sendJsonCustomFieldObject(id, oldValue, modifiedValue);
}

function getResourceForBlockingGridViewReservationForWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,reservedCount,demandCount,groupDemandId,userTypeId){
	initializeView();
	var loaddiv = '<div id="div_PopupMain" class="modal " tabindex="-1" aria-hidden="true"><div class="modal-dialog modal-lg" ><div class="modal-content" ><div class="modal-header"><button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupCloseReserve()" aria-hidden="true"></button><h4 class="modal-title">Reserve Resources for Work Package</h4></div>'+
		'<div class="modal-body"><div class="scroller" style="overflow: auto;" data-always-visible="1" data-rail-visible1="1">'+
	    '<div class="row" style="margin-bottom:10px">'+
	    '<div class="col-md-6"><div id="availabilityFilter" class="radio-toolbar"><div class=" btn-group" data-toggle="buttons" style="width:100%"><label id= "weeklyBooked" class="btn darkblue active" onclick="getBookedResources(1);">'+
	    '<input type="radio" class="toggle" >Booked</label><label id = "weeklyBookedOrAvailable" class="btn darkblue" onclick="getBookedOrAvailableResources(1);">'+
	    '<input type="radio" class="toggle" >Booked or Available</label><label id="weeklyBookedOrAvailableAll" class="btn darkblue" onclick="getUnAvailableResources(1);">'+
	    '<input type="radio" class="toggle" >All</label></div></div></div>'+			
		'<div class="col-md-3"><div id="viewFilter" class="radio-toolbar" style="display:none"><div class=" btn-group" data-toggle="buttons" style="width:100%"><label class="btn darkblue active" onclick="showGridView(1);"><input type="radio" class="toggle" >Grid</label><label class="btn darkblue" onclick="showTableView(1);"><input type="radio" class="toggle" >Table</label></div></div></div>'+
		'<div class="col-md-3"><label>Resources : &nbsp</label><span class="badge " style="background:#95A5A6" id="sp_demandCount">0/0</span></div></div>'+		
		'<div class="row" id="plannedDateDiv" style="margin-bottom:10px;display: flex;">'+		
		'<div class="input-group" style="padding-top: 5px;padding-right: 5px;"><label>Search :</label></div>'+
		'<div class="input-group"><div class=""><input id="searchText" class="form-control input-small" onkeyUp="searchNameTileHanlder(event);"></div></div>'+
		
		'<div class="input-group"><div class=""><form action="#" class="form-horizontal">'+
		'<div class="form-group"><label class="control-label col-md-2">Date Ranges</label>'+
		'<div class="col-md-10" style="display: flex;"><div class="input-group input-large" id="defaultrange_modal"><input type="text" class="form-control">'+
		'<span class="input-group-btn"><button class="btn default date-range-toggle" type="button"><i class="fa fa-calendar"></i></button></span>'+
		'</div><div class="input-group" id="copyrange_modal" style="padding: 6px 9px;"><span class="input-group-btn"><button class="fa fa-files-o" type="button" title="Copy reservation of all users to the selected week range"></button></span></div></div></div></form></div></div>'
		
		/*'<div class="input-group" style="padding-top: 5px;padding-right: 5px;padding-left: 5px"><label> Start Date:</label></div>'+		
		'<div class="input-group"><div class=""><input id="reservationStartDateId" name="reservationStartDate" class="form-control input-small  date-picker" onchange="reservationDatePickerHandler(event);"></div></div>'+
		'<div class="input-group" style="padding-top: 5px;padding-right: 5px;padding-left: 5px;"><label>End Date:</label></div>'+
		'<div class="input-group"><div class=""><input id="reservationEndDateId" name="reservationEndDate" class="form-control input-small  date-picker" onchange="reservationDatePickerHandler(event);"></div></div></div></div>'+*/
		
		'<div id="div_Table1" ></div><div id="div_Table2" ></div><div id="div_Table3" style="border: 1px solid rgb(204, 204, 204);"></div>'+
		'</div></div></div></div></div>';	
	$("#div_PopupMain").replaceWith(loaddiv);
	$("#reserveRecursive").val(workWeek); 	
 	getResourceDetailsForBlockingGridViewWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,reservedCount,demandCount,groupDemandId,userTypeId);
 	resetTabSelection();
 	
 	var d = new Date();
 	var month = new Array();
 	month=["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
 	var n = month[d.getMonth()];
 	
 	var currentWeek = (weeksNameAll[workWeek-1]).split('-');
 	var dateD = parseInt(currentWeek[0]);
 	var monthM = parseInt(month.indexOf(currentWeek[1]));
 	var yearY = 2000 + parseInt(currentWeek[2]);
 	
 	/*if(workWeek == weeksNameAll.length){
 		workWeek=0;
 		yearY = 2001 + parseInt(currentWeek[2]);
 	}*/
 	
 	//var currentWeekEnd = (weeksName[workWeek]).split('-');
 	var currentWeekedEnd = (weekEndNameAll[workWeek-1]).split('-');
 	var dateEnd = parseInt(currentWeekedEnd[0]);
 	var monthEnd = parseInt(month.indexOf(currentWeekedEnd[1])); 			
 	 	 	
 	$("#defaultrange_modal").daterangepicker({
 	    opens: App.isRTL() ? "left" : "right",
 	    format: "MM/DD/YYYY",
 	    separator: " to ",
 	    startDate: [yearY, monthM, dateD],
 	    endDate: [yearY, monthEnd, dateEnd],
 	    minDate: '01/01/'+yearY,
 	    maxDate: '12/31/'+yearY,
 	},function(t, e) {
 	    $("#defaultrange_modal input").val(t.format("MMMM D, YYYY") + " - " + e.format("MMMM D, YYYY")) 	    
 	    resourceReserveHandler(t.format("MM/DD/YYYY"), e.format("MM/DD/YYYY"));
 	}),
 	$("#defaultrange_modal").on("click", function() {
 	    $("#div_PopupMain").is(":visible") && 0 == $("body").hasClass("modal-open") && $("body").addClass("modal-open");
 	});
 	
 	var startDateRange = $('#defaultrange_modal').data("daterangepicker").startDate.format("MMMM D, YYYY");
 	var endDateRange = $('#defaultrange_modal').data("daterangepicker").endDate.format("MMMM D, YYYY"); 	
 	$("#defaultrange_modal input").val(startDateRange + " - " + endDateRange);
 	
 	$("#copyrange_modal .fa-files-o").on("click", function() { 	    
 		reserveResourcesForFutureWeeks();
 	});
} 

function resourceReserveHandler(startDate, endDate){
	console.log("startDate : "+startDate+" endDate : "+endDate);
	
	workPackageId=document.getElementById("currentWorkPackage").value;
	shiftId=document.getElementById("currentReservationShift").value; 
	userRoleId=document.getElementById("userRoleId").value;
	workYear = document.getElementById("workYear").value;
	skillId=document.getElementById("skillId").value;
    userTypeId=document.getElementById("userTypeId").value;
    
    
    
    reservationDate=document.getElementById("currentReservationDate").value;
    resourceDemandCount=document.getElementById("currentResourceDemandCount").value ;
    availablilityType=document.getElementById("currentAvailabilityType").value;
    currentView=document.getElementById("currentViewType").value;
    groupDemandId=document.getElementById("groupDemandId").value;
    workYear=document.getElementById("workYear").value;
	
	$("#weeklyBooked").text("Matching");
	
	$("#weeklyBookedOrAvailable").hide();
	
	weeklyEnable = "true";
	
    
	currentWorkPackageName = selectedDetails["selectedWorkPackageName"];
	//weekDetails = selectedDetails["selectedWorkWeekName"];
	userTypeName = selectedDetails["selectedUserType"];
	userRoleName = selectedDetails["selectedUserRole"];
	skillName = selectedDetails["selectedUserSkill"];
	
	workWeek=getWeekNumber(new Date(startDate));
	document.getElementById("workWeek").value = workWeek;
	weekDetails = weeksName[workWeek-1];
	
	var details = "Reserve for - WP : "+currentWorkPackageName+", Week : "+weekDetails+", Role : "+userRoleName+", Type : "+userTypeName+", Skill : "+skillName;
	urlToGetResourcesOfWorkPackageWeekly = "workPackage.block.resources.weekly?workPackageId="+workPackageId+"&shiftId="+shiftId+"&workWeek="+workWeek+"&workYear="+workYear+"&userRoleId="+userRoleId+"&skillId="+skillId+"&userTypeId="+userTypeId+"&filter="+availablilityType;
	showResourcesWeekly(urlToGetResourcesOfWorkPackageWeekly,details,reservationDate,selectedShiftName,workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,currentView,groupDemandId,reservedCount,resourceDemandCount,userTypeId);
	
	
}

function getWeekNumber(d) {
    // Copy date so don't modify original
    d = new Date(+d);
    d.setHours(0,0,0,0);
    // Set to nearest Thursday: current date + 4 - current day number
    // Make Sunday's day number 7
    d.setDate(d.getDate() + 4 - (d.getDay()||7));
    // Get first day of year
    var yearStart = new Date(d.getFullYear(),0,1);
    // Calculate full weeks to nearest Thursday
    var weekNo = Math.ceil(( ( (d - yearStart) / 86400000) + 1)/7);
    // Return array of year and week number
    return weekNo;
}

function searchNameTileHanlder(event){
	var txtValue = (event.target.value).toString().toLowerCase();
	var resultValue='';
	for(var i=0;i<$(userNameResourceMatchJson).length;i++){
		resultValue = $(userNameResourceMatchJson[i]).text().toLowerCase();		
		if(resultValue.indexOf(txtValue) != -1 && resultValue.indexOf(txtValue) == 0) {
			//console.log("match :"+resultValue);			
			$('#blockedResource_Popup .tile').eq(i).show();
		}else{
			$('#blockedResource_Popup .tile').eq(i).hide();
		}		
	}
}

function getResourceDetailsForBlockingGridViewWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,reservedCount,resourceDemandCount,groupDemandId,userTypeId){
	document.getElementById("currentWorkPackage").value = workPackageId;
	document.getElementById("currentReservationShift").value = shiftId;
	document.getElementById("currentReservationDate").value = reservationDate;
	document.getElementById("currentResourceDemandCount").value = resourceDemandCount;
	document.getElementById("currentAvailabilityType").value = availablilityType;
	document.getElementById("currentViewType").value = currentView;
	document.getElementById("groupDemandId").value = groupDemandId;
	document.getElementById("skillId").value = skillId;
	document.getElementById("userRoleId").value = userRoleId;
	document.getElementById("workWeek").value = workWeek;
	document.getElementById("workYear").value = workYear;
	document.getElementById("userTypeId").value = userTypeId;
	
	$("#weeklyBooked").text("Matching");
	
	$("#weeklyBookedOrAvailable").hide();
	
	weeklyEnable = "true";
	//currentWorkPackageName = document.getElementById("currentWorkPackageName").value;
	
	weekDetails = weeksName[workWeek-1];
	
	currentWorkPackageName = selectedDetails["selectedWorkPackageName"];
	weekDetails = selectedDetails["selectedWorkWeekName"];
	userTypeName = selectedDetails["selectedUserType"];
	userRoleName = selectedDetails["selectedUserRole"];
	skillName = selectedDetails["selectedUserSkill"];
	
	var details = "Reserve for - WP : "+currentWorkPackageName+", Week : "+weekDetails+", Role : "+userRoleName+", Type : "+userTypeName+", Skill : "+skillName;
	var date = new Date();
    var timestamp = date.getTime();
	urlToGetResourcesOfWorkPackageWeekly = "workPackage.block.resources.weekly?workPackageId="+workPackageId+"&shiftId="+shiftId+"&workWeek="+workWeek+"&workYear="+workYear+"&userRoleId="+userRoleId+"&skillId="+skillId+"&userTypeId="+userTypeId+"&filter="+availablilityType;
	showResourcesWeekly(urlToGetResourcesOfWorkPackageWeekly,details,reservationDate,selectedShiftName,workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,currentView,groupDemandId,reservedCount,resourceDemandCount,userTypeId);
	loadPopup("div_PopupMain"); 
}

function divPopupMainTitle(name){
	$("#div_PopupMain_Allocate").find('h4').text("");
	$("#div_PopupMain_Allocate").find('h4').text(name);
	$("#div_PopupMain_Allocate").modal();
}

$(document).ready(function(){
	var demandYears = '';
	var currentYear = new Date().getFullYear();
	for(var i = (currentYear - 10); i <= (currentYear + 10); i++){
		 demandYears += '<option value="'+i+'">'+i+'</option>';
	}
	$('#demandYears').append(demandYears);
	$('#demandYears').val(currentYear);
	$(document).on('change','#demandYears', function() {
		 if(operationYear != $('#demandYears').val()){
			 operationYear = $('#demandYears').val();
			 raiseResourceDemandForWeekly(selectedDemandDetails['workPackageId'], selectedDemandDetails['shiftId'], selectedDemandDetails['workWeek'], selectedDemandDetails['skillId'], selectedDemandDetails['roleId'], selectedDemandDetails['userTypeId']);
		 }
	});	
});

$(document).on('blur','#demandRecursive', function() {
	raiseResourceDemandPopupData();
});

function raiseResourceDemandPopupData() {
	demandWeekFlag = false;
	 var newDemandRecursive = $("#demandRecursive").val();
	 if(typeof newDemandRecursive == 'undefined' || newDemandRecursive == null || newDemandRecursive.trim() == '' || newDemandRecursive <= 0 || demandRecursive == newDemandRecursive){
		 $("#demandRecursive").val(demandRecursive);
	 }else{
		 demandRecursive = newDemandRecursive;
		 raiseResourceDemandForWeekly(selectedDemandDetails['workPackageId'], selectedDemandDetails['shiftId'], selectedDemandDetails['workWeek'], selectedDemandDetails['skillId'], selectedDemandDetails['roleId'], selectedDemandDetails['userTypeId']);
	 }
	
}
function initializeView()
{
	
	availablilityType = "Booked"; 
 	currentView = "Grid";
}

var reservedUserStatus = {};
var userNameResourceMatchJson={};
function showResourcesWeekly(urlToGetResourcesOfWorkPackageWeekly,details,reservationDate,currentShiftName,workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,currentView,groupDemandId,reservedCount,demandCount,userTypeId) {
	reservedUserStatus = {};
	usersCopyData="";
	try{
			 if($('#div_PopupMain').length>0){           				
			    $('#div_PopupMain').jtable('destroy');           					
			 };          			
			}catch(e){			
				$("#div_PopupMain .scroller").append('<div id="blockedResource_Popup"></div>');

			}     		
		    $('#blockedResource_Popup').show();		

		var thediv = document.getElementById('reportbox');
		openLoaderIcon();
		if (thediv.style.display == "none") {
			$.blockUI({
			 	theme : true,
			 	title : 'Please Wait',
			 	message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
			 });
			
		userNameResourceMatchJson={};	
	   	$.ajax({
	        type: "POST",
	        contentType: "application/json; charset=utf-8",
	        url: urlToGetResourcesOfWorkPackageWeekly,
	        dataType: 'json',
	        success: function(jsonData) {
	        	closeLoaderIcon();
	             var data = jsonData.Records;              
	             var reserveCount = 0;
	             
	         if(data.length!=0){ 
	            $("#blockedResource_Popup").empty();          
	            $("#div_PopupMain").find("h4").text(details);
	            for (var key in data) {
	            	var userId = data[key].userId;
	            	var roleId = data[key].userRoleId;
	            	var userReservationStatus = data[key].reserve;
	            	var isReserved = "";
	            	var productCoreRes = "";
	            	var tfCoreRes = "";
	            	var checkboxToolTip = "";
	            	var remainingShiftBookingMsg = "";
	            	var remainingTimeSheetBookingMsg = "";
	            	var loginId = data[key].loginId;
	            	var userRoleLabel = data[key].userRoleLabel;
	            	var userTypeLabel = data[key].userTypeLabel;
	            	var timeSheetHours = data[key].timeSheetHours;
	            	var tsHrs = "";
	            	var tsMins = "";
	            	var shiftBookingHours = data[key].bookedHrs;
	            	var userAvailability = data[key].available;
	            	var userLoginId = data[key].loginId;
	            	var userBookingStatus = data[key].booked;
	            	var userAvailabilityToolTip = data[key].available;
	            	var userBookingStatusToolTip = data[key].booked;
	            	var resourcePercentage = 0;
	            	var totalReservationPercentage = 0;
	            	wpId = workPackageId;
	            	spShift = shiftId;
	            	spSkill = skillId;
	            	spWorkYear = workYear;
	            	spUserTypeId = userTypeId;
	            	demandId = groupDemandId;
	            	
	            	usersCopyData += "UserId:"+userId+";RoleId:"+roleId;
	            	
	            	if(data[key].reservationPercentage != null){
	            		resourcePercentage = data[key].reservationPercentage;
	            		usersCopyData += ";ReservePercent:"+resourcePercentage;
	            	}else{
	            		usersCopyData += ";ReservePercent:0";
	            	}
	            	
	            	
	            	if(data[key].totalReservationPercentage != null){
	            		totalReservationPercentage = data[key].totalReservationPercentage;
	            	}
	            	
	                if(userAvailability != null){
	                	if(userAvailability=="A"){
	                		userAvailabilityToolTip = userLoginId+ " is available on "+reservationDate+" for Shift "+currentShiftName;
	                	}else if(userAvailability == "NA"){
	                		userAvailabilityToolTip = userLoginId+ " is not available on "+reservationDate+" for Shift "+currentShiftName;
	                    }
	                }
	                
	                if(userBookingStatus != null){
	                	if(userBookingStatus=="B"){
	                		userBookingStatusToolTip = userLoginId+ " is booked for "+reservationDate+" for Shift "+currentShiftName;
	                	}else if(userBookingStatus == "NB"){
	                		userBookingStatusToolTip = userLoginId+ " is not booked for "+reservationDate+" for Shift "+currentShiftName;
	                    }
	                }
	            	
	            	if (timeSheetHours == null) {
	                	timeSheetHours = 40;
	                    remainingTimeSheetBookingMsg = "40h 00m remaining in the week as per current time sheet bookings";
	                }else{
	              		if(timeSheetHours.indexOf(":") > -1){
	              			tsHrs = timeSheetHours.split(":")[0];
	              			tsMins = timeSheetHours.split(":")[1];
	              			remainingTimeSheetBookingMsg = tsHrs+"h "+tsMins+"m remaining in the week as per current time sheet bookings";
	              		}else{
	              			remainingTimeSheetBookingMsg = timeSheetHours+"h 00m remaining in the week as per current time sheet bookings";
	              		}
	                }
	                if (shiftBookingHours == null) {
	                	shiftBookingHours = 40;
	                    remainingShiftBookingMsg = "40h 00m remaining in the week as per current shift bookings";
	                }else{
	                	remainingShiftBookingMsg = shiftBookingHours+"h 00m remaining in the week as per current shift bookings";
	                }

	                if (userReservationStatus) {
	                	//reserveCount ++;
	                	isReserved = "checked=checked";
	                	checkboxToolTip = "Uncheck to Release";
	                } else { 
	                	isReserved = "";
	                	checkboxToolTip = "Check to Reserve";
	                }
	                if(data[key].imageURI=="" || data[key].imageURI=="/Profile/" || data[key].imageURI == null){
	                	data[key].imageURI="css/images/noimage.jpg";
	                }else{
	                	data[key].imageURI="/Profile/"+data[key].imageURI;
	                }
	                if(loginId.length >= 8){
	                	loginId = loginId.substring(0,8)+"...";
	                }
	                if(userRoleLabel != 'undefined' && userRoleLabel != null && userRoleLabel.length >= 8){
	                	userRoleLabel = userRoleLabel.replace(/ /g, "&nbsp;");
	                	userRoleLabel = userRoleLabel.substring(0,8)+"...";
	                }
	                if(userTypeLabel != 'undefined' && userTypeLabel != null && userTypeLabel.length >= 8){
	                	userTypeLabel = userTypeLabel.replace(/ /g, "&nbsp;");
	                	userTypeLabel = userTypeLabel.substring(0,8)+"...";
	                }
	                productCoreRes = data[key].productCoreResource;
	                tfCoreRes = data[key].tfCoreResource;
	                var responses = fileExists(data[key].imageURI);
	                if (responses == 404){
	                	data[key].imageURI = "css/images/noimage.jpg";
	                }
	                var bgColor = "green";
	                if(totalReservationPercentage > 100){
	                	bgColor = "red";
	                }else if(totalReservationPercentage < 100 && totalReservationPercentage > 0){
	                	bgColor = "orange";
	                }else if(totalReservationPercentage == 0){
	                	bgColor = "#5d6b7a";
//	                	bgColor = "blue";
	                }
	            	var isFullView = true;
	            	var isCreatePopup = true;
	                var showRp = true;
	                var scrolValue = 150;
	                var brick = '<div class="tile bg-grey-cascade" style="width:130px !important;height:135px !important;"><div class="tile-body" style="padding:3px 1px;">'+
							'<img src=' + data[key].imageURI + ' width = "50%" height="50%">'+
							'<div style="text-align:center"><span id="totalReservation_'+userId+'" class="badge badge-default bgCell" style="font-weight:bold;background-color: '+bgColor+';" onclick="showDetailedUtilizationOfResource(\'resourceReservationAndDemandContainer\', 0, 0, 0, 0, 0, '+userId+', 0, '+workYear+', \''+data[key].loginId+'\', \''+data[key].userRoleLabel+'\', \''+data[key].userTypeLabel+'\',\''+isFullView+'\',\''+isCreatePopup+'\');" title="Total Reservation ">' + totalReservationPercentage+ '%'+'</span>'+
	                        '</div>';
	                
							if((productCoreRes !== null) && (typeof productCoreRes !=='undefined')){
								brick += '<span class="badge badge-roundless " style="padding:3px;  title="Product Core Resource">'+ productCoreRes +'</span>&nbsp';								
								brick += '<div><a style="color:white;" data-target="#div_PopupMainUserProfile" data-toggle="modal"  onclick="javascript:showUserProfile('+userId+','+roleId+');">&nbsp<b>'+loginId+'</b></a>&nbsp;<input type="hidden" name="hdndiv_'+userId+'" value="'+userReservationStatus+'" id="hdndiv_'+userId+'" /></div>';
								brick += '<div><label style="margin-left:5px">'+ data[key].userRoleLabel + '</label></div></div></div>'								
							} else{
								brick += '<div title="'+data[key].loginId+'" style="margin-bottom:5px;position: absolute;margin-left: 35px;">';							
							}
							brick += '<ul style="list-style-type: none;">'+
							'<li style="height:18px; font-size:10px;"><input type="radio" style="margin-top: -1px;vertical-align: middle;" title="100%"  name="div_'+userId+'" value="100" id="div_'+userId+'100" '+isReserved+' onclick="blockOrUnBlockResourceWeekly('+userId+','+groupDemandId+',100)"/>100%</input></li>'+
							'<li style="height:18px; font-size:10px;"><input type="radio" style="margin-top: -1px;vertical-align: middle;" title="75%"  name="div_'+userId+'" value="75" id="div_'+userId+'75" '+isReserved+' onclick="blockOrUnBlockResourceWeekly('+userId+','+groupDemandId+',75)"/>75%</input></li>'+
							'<li style="height:18px; font-size:10px;"><input type="radio" style="margin-top: -1px;vertical-align: middle;" title="50%"  name="div_'+userId+'" value="50" id="div_'+userId+'50" '+isReserved+' onclick="blockOrUnBlockResourceWeekly('+userId+','+groupDemandId+',50)"/>50%</input></li>'+
							'<li style="height:18px; font-size:10px;"><input type="radio" style="margin-top: -1px;vertical-align: middle;" title="25%"  name="div_'+userId+'" value="25" id="div_'+userId+'25" '+isReserved+' onclick="blockOrUnBlockResourceWeekly('+userId+','+groupDemandId+',25)"/>25%</input></li>'+
							'<li style="height:18px; font-size:10px;"><input type="radio" style="margin-top: -1px;vertical-align: middle;" title="0%"  name="div_'+userId+'" value="0" id="div_'+userId+'0" '+isReserved+' onclick="blockOrUnBlockResourceWeekly('+userId+','+groupDemandId+',0)"/>0%</input></li></ul>'+
							'</div>';
							
							brick += '<div style="width:50%; font-size:11px;cursor:default;">'+
							'<div class="userNameDemandAndReserve" title="'+data[key].loginId+'" style="">'+
							'<a style="color:white;cursor:pointer;" data-target="#div_PopupMainUserProfile" data-toggle="modal"  onclick="javascript:showUserProfile('+userId+','+roleId+');"><b>'+loginId+'</b></a><input type="hidden" name="hdndiv_'+userId+'" value="'+userReservationStatus+'" id="hdndiv_'+userId+'" />'+
							'</div>'+
							'<div title="'+data[key].userRoleLabel+'">'+ userRoleLabel + '</div>'+
							'<div title="'+data[key].userTypeLabel+'">'+ userTypeLabel + '</div>' +
							
							'</div>';
	                if(!($('#blockedResource_Popup').hasClass('tiles'))){
	                	$('#blockedResource_Popup').addClass('tiles');
	                }              
	                $('#blockedResource_Popup').append($(brick));
	                $('#div_'+userId+data[key].reservationPercentage).attr('checked', true);
	                
	                if(typeof data[key].reservationPercentage != 'undefined' && data[key].reservationPercentage != null && data[key].reservationPercentage > 0){
	                	reservedUserStatus[userId] = data[key].reservationPercentage / 100;
	                	reserveCount += data[key].reservationPercentage / 100;
	                }
	                
	                userNameResourceMatchJson = $('#blockedResource_Popup .tile-body .userNameDemandAndReserve a');
	                usersCopyData += "~";
	            }	            	        
	         }else{
	        	if(availablilityType == "All"){
	        		callAlert(" Resource Pool are not mapped to Test Factory or Resources are not available for Selected Period.");
	        		popupClose();
	        	}else{
	        		callAlert("Resources are not available for Reservation. Check with Resource Manager or Reserve unavailable resources");
	               	workPackageId = document.getElementById("currentWorkPackage").value;
	               	shiftId = document.getElementById("currentReservationShift").value;
	               	reservationDate = document.getElementById("currentReservationDate").value;
	               	resourceDemandCount = document.getElementById("currentResourceDemandCount").value;
	               	groupDemandId = document.getElementById("groupDemandId").value;
	               	availablilityType = "All";
	               	//$('#radio3').attr('checked',true);
	               	$("#availabilityFilter>div").find("label:last").addClass("active").siblings().removeClass("active");
	               	currentView = document.getElementById("currentViewType").value;
	               	getResourceDetailsForBlockingGridViewWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,reservedCount,resourceDemandCount,groupDemandId,userTypeId);
	               	
	        	}
	         }
	           	    $("#sp_demandCount").text(reserveCount + "/" + $("#currentResourceDemandCount").val());
	           	    $("#sp_demandCount").attr("title", "Number of Reserved Resources are " + reserveCount + " for demand count of " + resourceDemandCount);
	                $('.bgCell').each(function(){
	                    var thisCell = $(this);
	                    var cellValue = parseInt(thisCell.text());

	                    if (!isNaN(cellValue) && (cellValue > 8)) {
	                        thisCell.addClass('badge-success');//css("background-color", "#006600");
	                    } else if (!isNaN(cellValue) && (cellValue <= 8) && (cellValue > 0)) {
	                        thisCell.addClass('badge-warning');//css("background-color", "#ffa500");
	                    } else {
	                        thisCell.addClass('badge-danger');//css("background-color", "#FF0000");
	                    }            	
	                });
	                $('.test').each(function() {
	                    var thisCell = $(this);
	                    if (thisCell.text() === "A" || thisCell.text() === "B") {
	                        thisCell.addClass('badge-success');//css("background-color", "#006600");
							//thisCell.css("margin-left","3px");                      
	                    } else {
	                        thisCell.addClass('badge-danger');//.css("background-color", "#E00000");
	                    }
	                });           
	         
	            $('#blockedResource_Popup').addClass("scrollbarPopup").css("background","#eff3f8");
	           	$.unblockUI();
	        }
	    });
		} else {
			thediv.style.display = "none";
			thediv.innerHTML = '';
		}		
		
	}

function resetTabSelection()
{	
	$("#availabilityFilter>div").find("label:first").addClass("active").siblings().removeClass("active");
	$('#girdViewRadio').attr('checked',true);
}

function fileExists(fileLocation) {
    var response = $.ajax({
        url: fileLocation,
        type: 'HEAD',
        async: false
    }).status;
    return response;
}

function blockOrUnBlockResourceWeekly(userId,groupDemandId,reservationPercentage){
	var reservationStatus = document.getElementById('hdndiv_'+userId).value;
	var reservationStatusId = 'hdndiv_'+userId;
	var checkBoxDivId = "#div_"+userId;
	var currentAvailability = document.getElementById("currentAvailabilityType").value;	
	reserveSelectedResourceWeekly(userId,checkBoxDivId,reservationStatusId,currentAvailability,groupDemandId,reservationPercentage);
	
}

function reserveSelectedResourceWeekly(userId,checkBoxDivId,reservationStatusId,currentAvailability,groupDemandId,reservationPercentage){	
	workPackageId = document.getElementById("currentWorkPackage").value;
	shiftId = document.getElementById("currentReservationShift").value;
	resourceDemandCount = document.getElementById("currentResourceDemandCount").value;
	workWeek =  document.getElementById("workWeek").value;
	workYear = document.getElementById("workYear").value;
	skillId = document.getElementById("skillId").value; 
	userRoleId = document.getElementById("userRoleId").value; 
	userTypeId = document.getElementById("userTypeId").value; 
	
//	groupDemandId = document.getElementById("groupDemandId").value; 
	
	var reserveWeeks = $("#reserveRecursive").val();
	if(typeof reserveWeeks == 'undefined' || reserveWeeks == null || reserveWeeks == '' || reserveWeeks == '0'){
		reserveWeeks = (workWeek);
    }
	openLoaderIcon();
	document.getElementById("currentAvailabilityType").value = currentAvailability;
	
	var percentage = reservationPercentage / 100;	
	var overOccupancy = "NO";
	var reservationExceed = "NO";
	//var startDate = $("#reservationStartDateId").val();
	//var endDate = $("#reservationEndDateId").val();	
	var startDate = $('#defaultrange_modal').data("daterangepicker").startDate.format("MM/DD/YYYY");
	var endDate = $('#defaultrange_modal').data("daterangepicker").endDate.format("MM/DD/YYYY");
	var percentAndDemandCount = $("#sp_demandCount").text();
	var urlToReserveOrReleaseResourceForWeek='workpackage.save.resources.for.workpackage.for.weekly?workpackageId='+workPackageId+'&workWeek='+workWeek+'&workYear='+workYear+'&shiftId='+shiftId+'&skillId='+skillId+'&userRoleId='+userRoleId+'&groupDemandId='+groupDemandId+'&demand='+resourceDemandCount+'&userId='+userId+' &reserveOrUnreserve=Reserve&currentAvailability='+currentAvailability+'&startDate='+startDate+'&endDate='+endDate+'&reservationPercentage='+reservationPercentage+'&userTypeId='+userTypeId+'&overOccupancy='+overOccupancy+'&reservationExceed='+reservationExceed+'&percentAndDemandCount='+percentAndDemandCount;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlToReserveOrReleaseResourceForWeek,
		dataType : 'json',
		success : function(data) {
			closeLoaderIcon();
			if(data.Result=="ERROR"){
				var exdOccupancyMsg = (data.Message).split(':');
				var condCheck;
				if(exdOccupancyMsg[0] == "occupancy exceeded"){
				var alertMsg = "Rersource Booking is "+exdOccupancyMsg[1]+"%, do you still want to reserve the resource ?";
				condCheck = "OccupancyCheck";
				}
				else if(exdOccupancyMsg[0] == "Reservation may exceeds Demand"){
					var alertMsg = "Reservation: "+exdOccupancyMsg[1]+" may exceeds Demand: "+exdOccupancyMsg[2]+", would you like to continue?";
					condCheck = "ReservationExceedCheck";
				}
				
					 var flag = bootbox.confirm(alertMsg, function(result) {		 
						 if(result){
							 if(condCheck == "OccupancyCheck"){
								 overOccupancy = "YES";
								 callForOverOccupancy(overOccupancy,reservationExceed,workPackageId,workWeek,workYear,shiftId,skillId,userRoleId,groupDemandId,resourceDemandCount,userId,currentAvailability,reserveWeeks,reservationPercentage,userTypeId);
							 }else{
								 reservationExceed = "YES";
								 callForOverOccupancy(overOccupancy,reservationExceed,workPackageId,workWeek,workYear,shiftId,skillId,userRoleId,groupDemandId,resourceDemandCount,userId,currentAvailability,reserveWeeks,reservationPercentage,userTypeId);
							 }
						 }else{
							// $(checkBoxDivId).prop('checked',false);
						 }						 
												 
						 if($("#weeklyBooked").hasClass('active')){
							 $("#weeklyBooked").trigger('click');
							 $("#weeklyBooked").addClass('active');
							 
						 }else if($("#weeklyBookedOrAvailableAll").hasClass('active')){
							 $("#weeklyBookedOrAvailableAll").trigger('click');
							 $("#weeklyBookedOrAvailableAll").addClass('active');
						 }
						
						return true;
					}) ;
				
				callConfirm(flag);
				
				
	    		//$(checkBoxDivId).prop('checked',false);
	    	}else{
	    		document.getElementById(reservationStatusId).value = "1";
	    		var manageCount = $("#sp_demandCount").text().split("/")[0];
	    		if(typeof reservedUserStatus[userId] != 'undefined' && reservedUserStatus[userId] != null){
	    			manageCount = parseFloat(manageCount);
	    			manageCount = manageCount - reservedUserStatus[userId];
	    			manageCount = manageCount + percentage;
	    			reservedUserStatus[userId] = percentage;
	    		}else{
	    			manageCount = parseFloat(manageCount);
	    			manageCount = manageCount + percentage;
	    			reservedUserStatus[userId] = percentage;
	    		}
	    		$("#sp_demandCount").text(manageCount + "/" + $("#currentResourceDemandCount").val());
	    		$("#sp_demandCount").attr("title", "Number of Reserved Resources are " + manageCount + " for demand count of " + $("#currentResourceDemandCount").val());
	    	}
		},
	});
}


function getUnAvailableResources(value){	
	getHiddenFieldValues();
	availablilityType = "All";	
	if(value == 0){
		weeklyEnable = "false"
	}
	displayResoucesAvailability(value);	
}

function getBookedResources(value){	
	getHiddenFieldValues();
	availablilityType = "Booked";	
	displayResoucesAvailability(value);	
}

function getHiddenFieldValues(){
	workPackageId = document.getElementById("currentWorkPackage").value;
	shiftId = document.getElementById("currentReservationShift").value;
	reservationDate = document.getElementById("currentReservationDate").value;
	resourceDemandCount = document.getElementById("currentResourceDemandCount").value;
	availablilityType = document.getElementById("currentAvailabilityType").value;
	currentView = document.getElementById("currentViewType").value;
	skillId = document.getElementById("skillId").value;
	userRoleId = document.getElementById("userRoleId").value;
	workWeek =  document.getElementById("workWeek").value;
	workYear =  document.getElementById("workYear").value;
	userTypeId = document.getElementById("userTypeId").value;
}


function displayResoucesAvailability(value)
{	
	document.getElementById("currentAvailabilityType").value = availablilityType;	
//	currentWorkPackageName = document.getElementById("currentWorkPackageName").value;
	getHiddenFieldValues();
	groupDemandId = document.getElementById("groupDemandId").value;
	console.log("---->> "+weeklyEnable);
	if (weeklyEnable == "true"){
		value = 1;
	}
	if(currentView == "Grid")
	{
		removeGridItems();
		if(value == 0){
			getResourceDetailsForBlockingGridView(workPackageId, shiftId, resourceDemandCount,reservationDate,availablilityType,currentView);
		}else{
			getResourceDetailsForBlockingGridViewWeekly(workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,reservedCount,resourceDemandCount,groupDemandId,userTypeId);
			
		}
	}else if(currentView == "Table"){
		
		if(value == 0){
			getResourcesForReservation(currentWorkPackageName,workPackageId, shiftId, resourceDemandCount,reservationDate, availablilityType);
		}else{
			getResourcesForReservationWeekly(currentWorkPackageName,workPackageId,shiftId,skillId,userRoleId,workWeek,workYear,availablilityType,currentView,resourceDemandCount,groupDemandId,userTypeId)
		}
	}
}


function removeGridItems(){
	$('#blockedResource_Popup').remove();
	$('#resourceReservation_Popup').remove();
}



function showTableView(value){
	removeGridItems();	

	getHiddenFieldValues();
	document.getElementById("currentViewType").value = "Table";
	currentView="Table"
	$('#div_Table3').show();
	
	displayResoucesAvailability(value); 	
}

function showGridView(value){
	getHiddenFieldValues();
	document.getElementById("currentViewType").value = "Grid";		
	currentView="Grid";
	
	removetable();	
	displayResoucesAvailability(value);
}

function removetable(){	
	$('#div_Table3').hide();
}

function callForOverOccupancy(overOccupancy,reservationExceed,workPackageId,workWeek,workYear,shiftId,skillId,userRoleId,groupDemandId,resourceDemandCount,userId,currentAvailability,reserveWeeks,reservationPercentage,userTypeId) {
	//var startDate = $("#reservationStartDateId").val();
	//var endDate = $("#reservationEndDateId").val();
	var startDate = $('#defaultrange_modal').data("daterangepicker").startDate.format("MM/DD/YYYY");
	var endDate = $('#defaultrange_modal').data("daterangepicker").endDate.format("MM/DD/YYYY");
	var percentAndDemandCount = $("#sp_demandCount").text();
	var urlToOverOccupyResourceForWeek='workpackage.save.resources.for.workpackage.for.weekly?workpackageId='+workPackageId+'&workWeek='+workWeek+'&workYear='+workYear+'&shiftId='+shiftId+'&skillId='+skillId+'&userRoleId='+userRoleId+'&groupDemandId='+groupDemandId+'&demand='+resourceDemandCount+'&userId='+userId+' &reserveOrUnreserve=Reserve&currentAvailability='+currentAvailability+'&startDate='+startDate+'&endDate='+endDate+'&reservationPercentage='+reservationPercentage+'&userTypeId='+userTypeId+'&overOccupancy='+overOccupancy+'&reservationExceed='+reservationExceed+'&percentAndDemandCount='+percentAndDemandCount;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlToOverOccupyResourceForWeek,
		dataType : 'json',
		success : function(data) {
			closeLoaderIcon();
			if(data.Result=="OK"){
				$("#sp_demandCount").text();
				//callAlert(data.Message);
				//$('#totalReservation_'+userId).text(total+'%');
			}
			//},error: function(data){
			else if(data.Result=="ERROR"){
					var exdOccupancyMsg = (data.Message).split(':');
					var condCheck;
					if(exdOccupancyMsg[0] == "occupancy exceeded"){
					var alertMsg = "Rersource Booking is "+exdOccupancyMsg[1]+"%, do you still want to reserve the resource ?";
					condCheck = "OccupancyCheck";
					}
					else if(exdOccupancyMsg[0] == "Reservation may exceeds Demand"){
						var alertMsg = "Reservation: "+exdOccupancyMsg[1]+" may exceeds Demand: "+exdOccupancyMsg[2]+", would you like to continue?";
						condCheck = "ReservationExceedCheck";
					}
					 var flag = bootbox.confirm(alertMsg, function(result) {		 
						 if(result){
							 if(condCheck == "OccupancyCheck"){
								 overOccupancy = "YES";
								 callForOverOccupancy(overOccupancy,reservationExceed,workPackageId,workWeek,workYear,shiftId,skillId,userRoleId,groupDemandId,resourceDemandCount,userId,currentAvailability,reserveWeeks,reservationPercentage,userTypeId);
							 }else{
								 reservationExceed = "YES";
								 callForOverOccupancy(overOccupancy,reservationExceed,workPackageId,workWeek,workYear,shiftId,skillId,userRoleId,groupDemandId,resourceDemandCount,userId,currentAvailability,reserveWeeks,reservationPercentage,userTypeId);
							 }
						 }else{
							// $(checkBoxDivId).prop('checked',false);
						 }
						
						if($("#weeklyBooked").hasClass('active')){
							$("#weeklyBooked").trigger('click');
							$("#weeklyBooked").addClass('active')
						 
						}else if($("#weeklyBookedOrAvailableAll").hasClass('active')){
							$("#weeklyBookedOrAvailableAll").trigger('click');
							$("#weeklyBookedOrAvailableAll").addClass('active')
						}
												 
				        return true;
					}) ;
					callConfirm(flag);					
		    		//$(checkBoxDivId).prop('checked',false);
		    	}
			  //}
			}
	});
}

function reserveResourcesForFutureWeeks() {
	var startDate = $('#defaultrange_modal').data("daterangepicker").startDate.format("MM/DD/YYYY");
	var endDate = $('#defaultrange_modal').data("daterangepicker").endDate.format("MM/DD/YYYY");
	var urlToCopyResourcesForFutureWeeks = "workpackage.copy.resources.for.specified.week.range?blockedResourcesData="+usersCopyData+"&workPackageId="+wpId+"&shiftId="+spShift+"&skillId="+spSkill+"&workYear="+spWorkYear+"&userTypeId="+spUserTypeId+"&groupDemandId="+demandId+"&startDate="+startDate+"&endDate="+endDate;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlToCopyResourcesForFutureWeeks,
		dataType : 'json',
		success : function(data) {
			closeLoaderIcon();
			if(data.Result=="OK"){
				callAlert(data.Message);
			}
			else if(data.Result=="ERROR"){
				callAlert(data.Message);
	    		return false;
	   		}
		}
	});
}