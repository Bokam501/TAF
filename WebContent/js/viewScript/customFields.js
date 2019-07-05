var CustomFieldGropings = function(){  
	var initialise = function(jsonObj){	    
	   displayCustomGroupings(jsonObj);
	};
	return {
		//main function to initiate the module
        init: function(jsonObj) {        	
       	initialise(jsonObj);
        }		
	};	
}();

var editorInStanceAllCell='';
var customFieldGroupingsJsonObj='';
var multiFrequenceyContainer='';
var customFieldSelectOptionsURLList=[];
var customFieldsOptionsItemCounter=0;
var customFieldActivityCreationFlag=false;

function displayCustomGroupings(jsonObj){
	var container='';
	multiFrequenceyContainer='';
	customFieldGroupingsJsonObj = jsonObj;
	customFieldActivityCreationFlag=false;
	
	$("#customFieldGroupId h4").text("");
	$("#customFieldGroupId h4").text(customFieldGroupingsJsonObj.Title);
	$("#customFieldGroupId h5").text("");
	$("#customFieldGroupId h5").text(customFieldGroupingsJsonObj.subTitle);
	
	$("#customFieldGroupId .modal-body .radio-toolbar .btn-group label").removeClass('active');
	$("#customFieldGroupId .modal-body .radio-toolbar .btn-group label").eq(0).addClass('active');
	
	if('#'+customFieldGroupingsJsonObj.multiFrequency != undefined)
		$('#'+customFieldGroupingsJsonObj.multiFrequency).html('');
		
	/*container = '<div id="'+customFieldGroupingsJsonObj.singleFrequency+'" class="scroller" style="overflow-y:auto; height:'+defineCustomFieldContainerSize(customFieldGroupingsJsonObj)+'" data-always-visible="1" data-rail-visible1="1"></div>';
	$("#customFieldSingleFrequencyID").html('');
	$("#customFieldSingleFrequencyID").append(container);	
	getSingleFrequencyCustomFields(customFieldGroupingsJsonObj.entityId, customFieldGroupingsJsonObj.entityTypeId, customFieldGroupingsJsonObj.entityInstanceId, customFieldGroupingsJsonObj.engagementId, customFieldGroupingsJsonObj.productId);*/
	
	var container = '<div id="customFieldMultiFrequencyID"></div>';
	$("#activityMultiSeriesContainer").html('');
	$("#activityMultiSeriesContainer").append(container);
	multiFrequenceyContainer='';
	//customFieldGroupingsJsonObj = activityCreationJsonObj(0);
	customFieldGroupingsJsonObj['componentUsageTitle']= 'CustomFieldSeries';
	customFieldSeriesFrequancyHandler();
	
	$("#customFieldValuesLoaderIcon").hide();
	$("#customFieldGroupId").modal();
}

var singleFrequencyJsonResponseValue = {};
function getSingleFrequencyCustomFields(entityId, entityTypeId, entityInstanceId, engagementId, productId){
	var singleFrequencyFields = {};
	 $.ajax({
		  type: "POST",
		  url : 'get.all.custom.field.exist.for.entityType?entityId='+entityId+'&entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId+'&engagementId='+engagementId+'&productId='+productId+'&frequencyType=Single&frequency=Single&frequencyMonth=-1&frequencyYear=0',
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			
			if(data.Result=="ERROR"){
				singleFrequencyFields = {};					
			}else{
				singleFrequencyFields = data;
			}
			singleFrequencyJsonResponseValue = data.Records[0].fieldGroup;
			
			// for creating new activity - entityInstanceId - 0;
			var customGroupItem=[];
			if(entityInstanceId == 0){
				for(var i=0;i<singleFrequencyJsonResponseValue.length;i++){
					customGroupItem = singleFrequencyJsonResponseValue[i];
					if(customGroupItem.groupFields != undefined){
						for(var j=0;j<customGroupItem.groupFields.length;j++){						
							if(customFieldActivityCreationFlag){
								//singleFrequencyJsonResponseValue[i].groupFields[j].fieldValue='';
								singleFrequencyJsonResponseValue[i].groupFields[j].fieldValue = singleFrequencyJsonResponseValue[i].groupFields[j].defaultValue;
							}
						}
					}
				}
			}			
			// -----
			
			displayCustomFields(singleFrequencyFields);		
			customFieldSingleFrequancyHandler();
		  },
		  error : function(data) {
		 },
		 complete: function(data){
		 }
	 });
	return singleFrequencyFields;
}

function customFieldSingleFrequancyHandler(){
	$("#customFieldSingleFrequencyID").show();
	$("#customFieldMultiFrequencyID").hide();
}

function customFieldSeriesFrequancyHandler(){	
	if(multiFrequenceyContainer == ''){
		multiFrequenceyContainer = '<div id="'+customFieldGroupingsJsonObj.multiFrequency+'" class="scroller" style="overflow-y:auto; height:'+defineCustomFieldContainerSize(customFieldGroupingsJsonObj)+'" data-always-visible="1" data-rail-visible1="1">'
		+'<div id="DailyCustomFieldContainer"></div>'
		+'<div id="WeeklyCustomFieldContainer"></div>'
		+'<div id="MonthlyCustomFieldContainer"></div>'
		+'<div id="QuaterlyCustomFieldContainer"></div>'
		+'<div id="Half-yearlyCustomFieldContainer"></div>'
		+'<div id="AnnualCustomFieldContainer"></div>'
		+'</div>';
		$("#customFieldMultiFrequencyID").html('');
		$("#customFieldMultiFrequencyID").append(multiFrequenceyContainer);
		getCustomFieldsSeriesFrequencyType(customFieldGroupingsJsonObj.multiFrequency, customFieldGroupingsJsonObj.entityId, customFieldGroupingsJsonObj.entityTypeId, customFieldGroupingsJsonObj.entityInstanceId, customFieldGroupingsJsonObj.engagementId, customFieldGroupingsJsonObj.productId);
	}
	
	$("#customFieldMultiFrequencyID").show();
	$("#customFieldSingleFrequencyID").hide();
}

function displayCustomFields(dataFull){
	populateSelectList(dataFull);
}

function populateSelectList(dataFull){
	var data = [];
	if(typeof dataFull.Records[0] != 'undefined'){
		data = dataFull.Records[0].fieldGroup;
	}
	
	customFieldSelectOptionsURLList=[];
	customFieldsOptionsItemCounter=0;
	var customGroupItem='';
	var fieldGroupItem='';
	var obj={};
	
	for(var i=0;i<data.length;i++){
		customGroupItem = data[i];
		
		for(var j=0;j<customGroupItem.groupFields.length;j++){		
			fieldGroupItem = customGroupItem.groupFields[j];
			
			var fieldType = myfunTrim(fieldGroupItem.fieldControlType).toLowerCase();
			var url = fieldGroupItem.fieldOptionsURL;
			
			if(fieldType == "select" && fieldGroupItem.fieldOptions.length==0 && url != null && url != ""){
				obj = {id: fieldGroupItem.customFieldId , type: fieldType, url: fieldGroupItem.fieldOptionsURL, data: {}};
				customFieldSelectOptionsURLList.push(obj);
			
			}else if(fieldType == "radiobutton" && fieldGroupItem.fieldOptions.length==0 && url != null && url != ""){
				obj = {id: fieldGroupItem.customFieldId , type: fieldType, url: fieldGroupItem.fieldOptionsURL, data: {}};
				customFieldSelectOptionsURLList.push(obj);
			}
		}
	}
	
	if(customFieldSelectOptionsURLList.length>0){
		returnCustomFieldOptionsList(dataFull, customFieldSelectOptionsURLList[0]);
	}else{
		var records =  reArrangeFieldGroupsAndFields(dataFull.Records[0]);
		var containerGroup = defineCustomFieldGroups(records);
	    $('#'+customFieldGroupingsJsonObj.singleFrequency).html('');
	    $('#'+customFieldGroupingsJsonObj.singleFrequency).append(containerGroup);
	    initializeDateTimePickercustomFieldValues();
	}
}

function returnCustomFieldOptionsList(data, fieldOptionsObj){
	$.ajax( {
 	   "type": "POST",
        "url":  fieldOptionsObj.url,
        "dataType": "json",
         success: function (json) {
         if(json.Result == "Error" || json.Options == null){
         	callAlert(json.Message);
         	json.Options=[];
         	
         }else{
     	   if(json.Options.length>0){     		   
			   for(var i=0;i<json.Options.length;i++){
				   json.Options[i].label=json.Options[i].DisplayText;
				   json.Options[i].value=json.Options[i].Value;
			   }			   
     	   }else{
     		  json.Options=[];
     	   }     	   
     	  fieldOptionsObj.data = json.Options;
     	   
     	   if(customFieldsOptionsItemCounter < customFieldSelectOptionsURLList.length){     		  
     		 returnCustomFieldOptionsList(data, customFieldSelectOptionsURLList[customFieldSelectOptionsURLList.length-1]);
     		 
	     		for(var i=0;i<data.Records[0].fieldGroup.length;i++){
	 				var customGroupItem = data.Records[0].fieldGroup[i];
	 				
	 				for(var j=0;j<customGroupItem.groupFields.length;j++){		
	 					var fieldGroupItem = customGroupItem.groupFields[j];    	
	 					
	 					if(fieldOptionsObj.id == fieldGroupItem.customFieldId){
	 						fieldGroupItem.fieldOptions = fieldOptionsObj.data;
	 						break;
	 					}	 					
	 				}
	 		  }
     		 
     	   }else{
     		   var records = reArrangeFieldGroupsAndFields(dataFull.Records[0]);
     		   var containerGroup = defineCustomFieldGroups(records);
     		   $('#'+customFieldGroupingsJsonObj.singleFrequency).html('');
     		   $('#'+customFieldGroupingsJsonObj.singleFrequency).append(containerGroup);
     		   
     		  initializeDateTimePickercustomFieldValues();     		 
     	   }
     	   customFieldsOptionsItemCounter++;     	   
         }
         },
         error: function (data) {
        	 customFieldsOptionsItemCounter++;
        	 
         },
         complete: function(data){
         	
         },	            
   	});		
}

function initializeDateTimePickercustomFieldValues(){
	var data = singleFrequencyJsonResponseValue;
	var customGroupItem=[];
	var fieldType='';
	for(var i=0;i<data.length;i++){
		customGroupItem = data[i];
		
		for(var j=0;j<customGroupItem.groupFields.length;j++){
			fieldType = myfunTrim(customGroupItem.groupFields[j].fieldControlType).toLowerCase();
			var idPicker = customGroupItem.groupFields[j].customFieldId;
			if(fieldType == "datepicker"){
				if(customGroupItem.groupFields[j].fieldValue != "")
					$("#"+idPicker+"_datePicker").datetimepicker('setDate',customGroupItem.groupFields[j].fieldValue);
				else
					$("#"+idPicker+"_datePicker").datetimepicker('setDate',new Date());
				 
			}else if(fieldType == "datepickerTimePicker"){
				if(customGroupItem.groupFields[j].fieldValue != "")
					$("#"+idPicker+"_dateTimePicker").datetimepicker('setDate',customGroupItem.groupFields[j].fieldValue);
				else
					$("#"+idPicker+"_dateTimePicker").datetimepicker('setDate',new Date());
			}
		}
	}	
}

function reArrangeFieldGroupsAndFields(data){
	var flag = false;
	var jsonSortedObj = data;
	var fieldGroupArr=[];
	// -- any error in json - will default no sorting -----
	for(var i=0;i<data.fieldGroup.length;i++){
		var groupOrder = data.fieldGroup[i].groupOrder;
		if(isNaN(groupOrder) || isNaN(groupOrder)){
			flag=true;
			break;
		}				
	}
	// ---
	
	if(flag){
		jsonSortedObj = data;
	}else{
		for(groupOrder in data.fieldGroup){
			fieldGroupArr.push(data.fieldGroup[groupOrder]);
		}
		fieldGroupArr.sort(function(a, b) {
		    return parseFloat(a.groupOrder) - parseFloat(b.groupOrder);
		});
		
		jsonSortedObj.fieldGroup = fieldGroupArr; 
	}
	return jsonSortedObj;
}

function defineColumnGroupTwo(data){
	var fieldGroupArr=[];
	for(var i=0;i<customFieldGroupingsJsonObj.columnGrouping;i++){
		fieldGroupArr[i]=[];
	}
	for(var j=0;j<data.fieldGroup.length;j++){
		if(j % 2 == 0)
			fieldGroupArr[0].push(data.fieldGroup[j]);
		else
			fieldGroupArr[1].push(data.fieldGroup[j]);
	}		
	
	return fieldGroupArr;
}

function defineCustomFieldGroups(data){
	var fieldGroups='';

	var fieldGroupArr=[];
	if(customFieldGroupingsJsonObj.columnGrouping == 2){
		fieldGroupArr = defineColumnGroupTwo(data);
	}else{
		fieldGroupArr = data.fieldGroup;
	}	
	
	fieldGroups += '<div class="row">';
	for(var i=0;i<fieldGroupArr.length;i++){
		fieldGroups +=	defineColumnGrouping(customFieldGroupingsJsonObj.columnGrouping);	
		
		if(customFieldGroupingsJsonObj.columnGrouping == 2){
			for(var j=0;j<fieldGroupArr[i].length;j++){
				fieldGroups +=	'<div class="table-scrollable">'+
				'<table class="table table-striped table-hover">'+
				'<thead>'+
					'<tr height="30"><th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">'+fieldGroupArr[i][j].groupName+'</th>'+
					'<th></th>'+
					'</tr>'+
				'</thead>'+
				'<tbody>';		
				fieldGroups += defineGroups(fieldGroupArr[i][j].groupFields);
				fieldGroups += '</tbody></table></div>';
			}			
		}else{
			fieldGroups +=	'<div class="table-scrollable">'+
				'<table class="table table-striped table-hover">'+
				'<thead>'+
					'<tr height="30"><th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">'+fieldGroupArr[i].groupName+'</th>'+
					'<th></th>'+
					'</tr>'+
				'</thead>'+
				'<tbody>';		
			fieldGroups += defineGroups(fieldGroupArr[i].groupFields);
			fieldGroups += '</tbody></table></div>';
		}
		
		fieldGroups += '</div>';
	}

	return fieldGroups;
}

function defineGroups(groupFields){
	var groupField='';
	var labelColor = '#333';
	for(var i=0;i<groupFields.length;i++){
		if(groupFields[i].isModified){
			labelColor = 'orange';
		}else{
			labelColor = '#333';
		}
		groupField += '<tr><td style="color:'+labelColor+'">'+groupFields[i].customFieldName+':</td>';
		groupField += defineCustomFields(groupFields[i]);		
		groupField += '</tr>';
	}	
	return groupField;
}

// ----- update for the text input field -----
function customFieldTextUpateHandler(event){
	var id = event.target.id;
	var oldValue = event.target.defaultValue;
	var modifiedValue = event.target.value;	
	sendJsonCustomFieldObject(id, oldValue, modifiedValue);	
}

// ----- update for the select dropDown field -----
function customFieldSelectUpdateHandler(event){
	var id = event.target.id;	
	var oldValue = "";
	var modifiedValue = event.currentTarget.selectedOptions[0].value;	
	sendJsonCustomFieldObject(id, oldValue, modifiedValue);	
}

// ----- update for the datepicker field -----
function customFieldDatePickerHandler(event){
	var getID = event.target.id; 
	var id = getID.split('_')[0];
	var oldValue = event.target.defaultValue;
	var modifiedValue = event.target.value;	
	sendJsonCustomFieldObject(id, oldValue, modifiedValue);
}

//----- update for the datepicker field -----
function customFieldDateTimePickerHandler(event){
	var getID = event.target.id; 
	var id = getID.split('_')[0];
	var oldValue = event.target.defaultValue;
	var modifiedValue = event.target.value;	
	sendJsonCustomFieldObject(id, oldValue, modifiedValue);
}

// ----- update for the radioButton -----
function customFieldRadioHandler(event){
	var id = event.target.id;	
	var parentID='';
	if(id == ''){
		parentID = event.target.parentElement.parentElement.id
		id = parentID.split('-')[0];
	}
	
	var oldValue = event.target.defaultValue+" ";
	var modifiedValue = event.target.value;	
	sendJsonCustomFieldObject(id, oldValue, modifiedValue);
}

//----- update for the checkBoxButton -----
function customFieldCheckBoxHandler(event){
	var id = event.target.id;	
	var parentID='';
	if(id == ''){
		parentID = event.target.parentElement.parentElement.id
		id = parentID.split('-')[0];
	}
	var oldValue = event.target.defaultValue;
	var modifiedValue = "";	
	if(event.target.checked){
		oldValue = "";
		modifiedValue = event.target.value;
	}
	
	var chkArray=[]; 
	$('"#"'+parentID +'input:checked').each(function() {
		chkArray.push($(this).val());
	});
	console.log("checkbox selection :"+chkArray);
	
	sendJsonCustomFieldObject(id, oldValue, chkArray);
}

function sendJsonCustomFieldObject(idValue, oldValue, modifiedValue){
	if(oldValue != modifiedValue && !customFieldActivityCreationFlag){	
		$("#customFieldValuesLoaderIcon").show();
		//var resultObj = getJsonCustomFieldObject(idValue, customFieldGroupingsJsonObj.entityInstanceId);
		var resultObj = getJsonCustomFieldObject(idValue);
		resultObj.fieldValue = modifiedValue;		
		resultObj['oldFieldID'] = '';
		resultObj['oldFieldValue'] = oldValue;
		resultObj['modifiedFieldValue'] = modifiedValue;
		resultObj['modifiedField'] = resultObj['customFieldName'];
		resultObj['modifiedFieldTitle'] = resultObj['customFieldName'];
		resultObj['entityInstanceId'] = customFieldGroupingsJsonObj.entityInstanceId; 
	
		var updateCustomFieldSingleFrequencyURL = "custom.field.value.save.or.update.for.instance";
		$.post(updateCustomFieldSingleFrequencyURL, resultObj).done(function (data) {
			var result = data.Result;
			if(result == "ERROR"){
				callAlert(data.Message);
			}else{
				if(typeof data.Message != 'undefined' && data.Message != null && data.Message != ''){
					activitySummaryFlag=true;
					callAlert(data.Message);
				}
			}
			$("#customFieldValuesLoaderIcon").hide();
		});	
	}
}

function getJsonCustomFieldObject(idValue, name, allActivities){
	var idValueArr = String(idValue).split('-');
	if(idValueArr.length>1){
		idValue = idValueArr[0];
		name = idValueArr[1];
	}else if(name == undefined){
		name = '';
	}
	
	var data = singleFrequencyJsonResponseValue;
	var idValuePair='';
	var customFieldIdValue='';
	var keyValue='';keyItem='';
	
		for(var i=0;i<data.length;i++){
			for(var key in data[i]){
				 keyValue = data[i][key]; 
				 keyItem = key.replace(/ +/g, "");
				 data[i][keyItem] = keyValue;
			 }
		}

		if(!customFieldActivityCreationFlag && allActivities != undefined){	
			for(var i=0;i<data.length;i++){				
				name = name.replace(/ +/g, "");
				customFieldIdValue = String(data[i]['idsOf'+name]).split('-')[0];
				customFieldIdValue = customFieldIdValue.replace(/ +/g, "");
				//console.log(" customField Id : "+idValue+" value : "+name);
				
				for(var k=0;k<allActivities.length;k++){				
					//console.log(data[i].Id," -- "," verify : "+idValue+" value : "+customFieldIdValue);
					if(allActivities[k].activityId == data[i].Id && customFieldIdValue == idValue){
						idValuePair = String(idValue+'-'+name);
						idValuePair = idValuePair.replace(/ +/g, "");
						allActivities[k][idValuePair] = String(data[i][name]);					
						//console.log('Activity Id : '+allActivities[k].activityId+" customField Id : "+idValue+" idValuePair : "+idValuePair+" value : "+String(data[i][name]));				
					}
				}
				// -----		
				if(allActivities.length==0){
					if(customFieldIdValue == idValue){
						console.log("No custom fields : "+String(data[i][name]));					
					}
				}
				//-----		
			}	
		}else{
			
			var customGroupItem=[];
			var fieldGroupItem=[];
			
			for(var i=0;i<data.length;i++){
				customGroupItem = data[i];
				if(customGroupItem.groupFields != undefined){
					for(var j=0;j<customGroupItem.groupFields.length;j++){		
						fieldGroupItem = customGroupItem.groupFields[j];
						if(fieldGroupItem.customFieldId == idValue){
							return fieldGroupItem;
							break;
						}
					}
				}
			}	
			return fieldGroupItem;
		}
	return;	
}

function defineCustomFields(data){
	var fieldType = myfunTrim(data.fieldControlType).toLowerCase();
	var resultType ='';
	var fieldName = data.customFieldName.replace(/ +/g, "");
	
	if(fieldType == "text"){
		resultType='<td>'+data.fieldValue+'</td>';
	
	}else if(fieldType == "select"){
		resultType='<td><span><select id="'+data.customFieldId+'-'+fieldName+'" onchange="customFieldSelectUpdateHandler(event)">';
		var customFieldSelectedOption='';
		if(data.fieldOptions.length>0){
			$.each(data.fieldOptions, function(index, element) {
				customFieldSelectedOption="";
				if(data.fieldValue == element.Value){
					customFieldSelectedOption = "selected";
				}else{
					customFieldSelectedOption = "";
				}
				resultType += '<option '+customFieldSelectedOption+' value="' + element.Value + '" >'+ element.DisplayText + '</option>'; 
			});
			
		}else{
			resultType += '<option value="--">No Data</option>';
		}
		resultType += '</select><span></td>';
		
	}else if(fieldType == "dateTimepicker"){
		
		
	}else if(fieldType == "textbox"){
		resultType +='<td><span>'+
		 '<input id="'+data.customFieldId+'-'+fieldName+'" class="form-control input" onblur="customFieldTextUpateHandler(event)" value="'+data.fieldValue+'" />'+
		 '</span></td>'; 
	
	}else if(fieldType == "password"){
		resultType +='<td><span>'+
		 '<input id="'+data.customFieldId+'-'+fieldName+'" class="form-control input" type="password" onblur="customFieldTextUpateHandler(event)" value="'+data.fieldValue+'" />'+
		 '</span></td>'; 
	
	}else if(fieldType == "datepicker"){
		resultType +='<td><span>'+
		 '<input id="'+data.customFieldId+'-'+fieldName+'_datePicker" class="form-control input-small date-picker col-md-2" onchange="customFieldDatePickerHandler(event);" size="10" type="text" value="'+data.fieldValue+'" style="width: 175px !important;"/>'+
		 '</span></td>';			
	
	}else if(fieldType == "datepickerTimePicker"){
		resultType +='<td><span>'+
		 '<input id="'+data.customFieldId+'-'+fieldName+'_dateTimePicker" class="form-control input-small date-picker col-md-2" onchange="customFieldDateTimePickerHandler(event);" size="10" type="text" value="'+data.fieldValue+'" style="width: 175px !important;"/>'+
		 '</span></td>';	
	
	}else if(fieldType == "radiobutton"){
		resultType='<td><span>';
		
		if(data.fieldAlignment == undefined || data.fieldAlignment == null)
			data.fieldAlignment = "";
		
		var fieldAlignment = myfunTrim(data.fieldAlignment).toLowerCase();
		
		if(fieldAlignment == "vertical"){
			align = "";
		}else{
			align = "radio-inline";
		}
		
		if(data.fieldOptions.length>0){
			resultType += '<div id="'+data.customFieldId+'-'+fieldName+'" class="radio-list">';
			var customFieldSelectedOption='';
			$.each(data.fieldOptions, function(index, element) {
				customFieldSelectedOption="";
				if(data.fieldValue == element.Value){
					customFieldSelectedOption = "checked";
				}else{
					customFieldSelectedOption = "";
				}
				resultType += '<label class="'+align+'"><input '+customFieldSelectedOption+' type="radio" onclick="customFieldRadioHandler(event);" name="selection'+data.customFieldId+'" value="' + element.Value + '"> &nbsp ' + element.DisplayText + '</label>';
			});
			resultType += '</div>';
			
		}else{
			resultType += '';
		}
		resultType += '<span></td>';	
	
	}else if(fieldType == "checkbox"){
		resultType='<td><span>';
		
		if(data.fieldAlignment == undefined || data.fieldAlignment == null)
			data.fieldAlignment = "";
		
		var fieldAlignment = myfunTrim(data.fieldAlignment).toLowerCase();
		
		if(fieldAlignment == "vertical"){
			align = "";
		}else{
			align = "radio-inline";
		}
		
		if(data.fieldOptions.length>0){
			resultType += '<div id="'+data.customFieldId+'-'+fieldName+'" class="radio-list">';
			$.each(data.fieldOptions, function(index, element) {
				customFieldSelectedOption="";
				var customFieldSelectedOption='';
				if(data.fieldValue == element.Value){
					customFieldSelectedOption = "checked";
				}else{
					customFieldSelectedOption = "";
				}

				resultType += '<label class="'+align+'"><input '+customFieldSelectedOption+' type="checkBox" name="selection'+data.customFieldId+'" onclick="customFieldCheckBoxHandler(event);" value="' + element.Value + '" checked"> &nbsp ' + element.DisplayText + '</label>';
			});
			resultType += '</div>';
			
		}else{
			resultType += '';
		}
		resultType += '<span></td>';	
	
	}else if(fieldType == "textarea"){
		resultType +='<td><span>'+
		 '<textarea id="'+data.customFieldId+'-'+fieldName+'" class="form-control" onblur="customFieldTextUpateHandler(event)" name="input">'+data.fieldValue+'</textarea>'+
		 '</span></td>';		
	}
	
	return resultType;
}

var optionsUrlValues=[];

function customFieldAjaxHanlder(type, url){	
	 $.ajax({
		 type: "POST",
		contentType: "application/json; charset=utf-8",
		url : url,		
		dataType : 'json',
		success : function(data) {
          if(data.Result=="OK"){
          		//callAlert(data.Message);
        	  if(type == "select"){
        		  
        	  }
          }else if(data.Result == "ERROR"){
       	   callAlert(data.Message);			        	   
          }
	     },
	    error: function(data){
	   	  console.log("error in ajax call");
	     },
	    complete: function(data){
	   	  console.log("complete in ajax call");
	    }
	});		
}

// ----- Resizing the popup window - small, medium and large(Default) -----

function defineCustomFieldContainerSize(jsonObj){
	var containerSize = myfunTrim(jsonObj.containerSize).toLowerCase();
	var resultHeight='0px';
	var widthValue='';
	var leftValue='';
	var topValue='';
	
	if(containerSize == "small"){
		widthValue = "50%";
		leftValue = "25%";
		topValue = "20%";
		resultHeight = "240px";
		
	}else if(containerSize == "medium"){
		widthValue = "80%";
		leftValue = "10%";
		topValue = "10%";
		resultHeight = "360px";
		
	}else{
		// default "large"
		widthValue = "96%";
		leftValue = "2%";
		topValue = "2%";
		resultHeight = "450px";
	} 
	$("#customFieldGroupId").css('width', widthValue);
	$("#customFieldGroupId").css('left', leftValue);
	$("#customFieldGroupId").css('top', topValue);
	
	return resultHeight;
}

// ----- Grouping the row into column groups - 1, 2, 3 and 4 (max) column groups -----

function defineColumnGrouping(columnGrouping){
	var columnGroup='';
	
	if(columnGrouping<0 || columnGrouping>4){
		columnGroup = '<div class="col-lg-12 col-md-12">';
		return columnGroup;
	}
	
	if(columnGrouping == 1){
		columnGroup = '<div class="col-lg-12 col-md-12">';
		
	}else if(columnGrouping == 2){
		columnGroup = '<div class="col-lg-6 col-md-6">';
		
	}else if(columnGrouping == 3){
		columnGroup = '<div class="col-lg-4 col-md-4">';
		
	}else{
		columnGroup = '<div class="col-lg-3 col-md-3">';		
	} 
	
	return columnGroup;	
}

// -------- Multi frequency custom fields -------------

var initialFrequencyYear = new Date().getFullYear();
var yearOfFrequency = {
	"Single" : "",
	"Daily" : initialFrequencyYear,
	"Weekly" : initialFrequencyYear,
	"Monthly" : initialFrequencyYear,
	"Quaterly" : initialFrequencyYear,
	"Half-yearly" : initialFrequencyYear,
	"Annual" : initialFrequencyYear,
};

var monthOfFrequency = {
	"Single" : "",
	"Daily" : new Date().getMonth(),
	"Weekly" : -1,
	"Monthly" : -1,
	"Quaterly" : -1,
	"Half-yearly" : -1,
	"Annual" : -1,
};

var frequencyTypes = ["Daily", "Weekly", "Monthly", "Quaterly", "Half-yearly", "Annual"];
var dailyColumnSeries = [];
var weeklyColumnSeries = [];
var monthlyColumnSeries = [];
var quaterlyColumnSeries = [];
var halfYearlyColumnSeries = [];
var annualColumnSeries = [];
var customFieldsMultiFrequencyCounter = 0;

function getCustomFieldsSeriesFrequencyType(containerId, entityId, entityTypeId, entityInstanceId, engagementId, productId){
	customFieldsMultiFrequencyCounter = 0;
	$("#customFieldLoaderIcon").show();
	
	$.ajax({
		url : 'get.all.series.custom.field.count?entityId='+entityId+'&entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId+'&engagementId='+engagementId+'&productId='+productId+'&frequencyType=Series',
		dataType : 'json',
		type:'POST',
		error: function() {
			console.log("Error");
		},
		success : function(jsonData) {
			if(typeof jsonData.Records != 'undefined'){
				frequencyTypes = [];
				var dataArray = jsonData.Records[0];
				$.each(dataArray, function(index, value){
					if(value>0){
						frequencyTypes.push(index);
					}
				});
				
				if(frequencyTypes.length>0){
					getCustomFieldsOfFrequencyType(frequencyTypes[0], containerId, entityId, entityTypeId, entityInstanceId, engagementId, productId);
				}
			}
		}
	});
	
}

var frequencyColumnChecker='';
function getCustomFieldsOfFrequencyType(frequencyType, containerId, entityId, entityTypeId, entityInstanceId, engagementId, productId){
	if(frequencyType == 'Daily' || frequencyType == 'Weekly'){
		getColumnsByFrequencyType(frequencyType);
	}else if(frequencyType == 'Monthly'){
		monthlyColumnSeries = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
	}else if(frequencyType == 'Quaterly'){
		quaterlyColumnSeries = ["First", "Second", "Third", "Forth"];
	}else if(frequencyType == 'Half-yearly'){
		halfYearlyColumnSeries = ["First", "Second"];
	}else if(frequencyType == 'Annual'){
		var selectedYear = yearOfFrequency['Annual'];
		annualColumnSeries = [(selectedYear - 2), (selectedYear - 1), (selectedYear), (selectedYear + 1), (selectedYear + 2)];
	}
	
	var frequencyColumnSeries = getFrequencyColumnSeriesByType(frequencyType);
	if(typeof frequencyColumnSeries != 'undefined' && frequencyColumnSeries.length > 0){	
		getMultiFrequencyCustomFields(frequencyType, containerId, entityId, entityTypeId, entityInstanceId, engagementId, productId, "150");
	};	 
}

function getColumnsByFrequencyType(frequencyType) {
	if(frequencyType == 'Daily'){
		dailyColumnSeries = [];
	}else if(frequencyType == 'Weekly'){
		weeklyColumnSeries = [];
	} 
	$.ajax({
		url : 'common.list.column.series.by.frequency?frequencyType='+frequencyType+'&frequencyYear='+yearOfFrequency[frequencyType]+'&frequencyMonth='+monthOfFrequency[frequencyType],
		dataType : 'json',
		error: function() {
			callAlert("Unable to list Column for frequency - "+frequencyType);
		},
		success : function(jsonData) {
			if(typeof jsonData.Records != 'undefined'){
				if(frequencyType == 'Daily'){
				 	for (var i = 0; i < jsonData.Records.length; i++) {
				 		dailyColumnSeries.push(jsonData.Records[i].option);
				 	}
				}else if(frequencyType == 'Weekly'){
					for (var i = 0; i < jsonData.Records.length; i++) {
						weeklyColumnSeries.push(jsonData.Records[i].option);
				 	}
				} 
			}
		}
	});
};

var multiFrquencyCustomFieldColumsAvailableIndex = [];
function getMultiFrequencyCustomFields(frequencyType, containerId, entityId, entityTypeId, entityInstanceId, engagementId, productId, scrollYValue){
	
	 $.ajax({
		  type: "POST",
		  url : 'get.all.custom.field.exist.for.entityType?entityId='+entityId+'&entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId+'&engagementId='+engagementId+'&productId='+productId+'&frequencyType=Series&frequency='+frequencyType+'&frequencyMonth='+monthOfFrequency[frequencyType]+'&frequencyYear='+yearOfFrequency[frequencyType],
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			  customFieldsMultiFrequencyCounter++;
			  
			if(data.Result=="ERROR"){
     		    data = [];						
			}else{
				var multiFrequencyCustomFieldData = data.Records;
				getMultiFrquencyCustomFieldHeaderAndFooterColumns(frequencyType, containerId);
				var multiFrequencyCustomFieldColumnValueMapping = getMultiFrquencyCustomFieldColumnMappings(frequencyType, entityInstanceId);
				showMultiFrequencyCustomFields(containerId, entityId, entityTypeId, entityInstanceId, scrollYValue, frequencyType, multiFrequencyCustomFieldColumnValueMapping, multiFrequencyCustomFieldData);
			}
			
			if(customFieldsMultiFrequencyCounter < frequencyTypes.length){
				getCustomFieldsOfFrequencyType(frequencyTypes[customFieldsMultiFrequencyCounter], containerId, entityId, entityTypeId, entityInstanceId, engagementId, productId);
			}else{
				$("#customFieldLoaderIcon").hide();
			}			
		  },
		  error : function(data) {
			 customFieldsMultiFrequencyCounter++;
		 },
		 complete: function(data){
			 //console.log("complete");
		 }
	 });
	
}

var dataTableInstance='';
function showMultiFrequencyCustomFields(containerId, entityId, entityTypeId, entityInstanceId, scrollYValue, frequencyType, multiFrequencyCustomFieldColumnValueMapping, multiFrequencyCustomFieldData){
	try{
		if ($('#'+frequencyType+'_dataTable').length > 0) {
			$('#'+frequencyType+'_dataTable').dataTable().fnDestroy(); 
		}
	} catch(e) {};
	
	dataTableInstance = $('#'+frequencyType+'_dataTable').dataTable( {
		 "dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"bScrollCollapse": true,
		autoWidth: false,
		bAutoWidth:false,
		"sScrollX": "100%",
       "sScrollXInner": "100%",
       "scrollY":"100%",
       "fnInitComplete": function(data) {
    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
    	  var headerItems = $('#'+frequencyType+'_dataTable_wrapper tfoot tr#'+frequencyType+'_dataTable_filterRow th');
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
	  
    	  reInitializeDataTabletMultiFrquencyCustomField();
	   },  
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: frequencyType+' Custom Field Summay',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: frequencyType+' Custom Field Summay',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: frequencyType+' Custom Field Summay',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        orientation: 'landscape',
	                        pageSize: 'A0',
	                        footer: true
	                    },	                    
	                ],	                
	            },
	            'colvis'
         ], 
         columnDefs: [
              { targets: multiFrquencyCustomFieldColumsAvailableIndex, visible: true},
              { targets: '_all', visible: false }
          ],
                  
        aaData : multiFrequencyCustomFieldData,                 
	    aoColumns : multiFrequencyCustomFieldColumnValueMapping,
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },
    	   
	});
	
	$(function(){ // this will be called when the DOM is ready 
	    
		// Activate an inline edit on click of a table cell
        $('#'+frequencyType+'_dataTable tbody').on( 'click', 'td', function (e) {
        	editorInStanceAllCell = $('#'+frequencyType+'_dataTable').DataTable().cell(this);
        } );
        
		//dailyField_oTable.DataTable().columns().every( function () {
		$('#'+frequencyType+'_dataTable').DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });
		
		$("#"+frequencyType+"_dataTable_length").css('margin-top','8px');
		$("#"+frequencyType+"_dataTable_length").css('padding-left','35px');
	    
		if(frequencyType == 'Daily'){
			var monthFilter = '<span><label style="margin: 5px;">Month :</label><select id="'+frequencyType+'MonthFilter" style="margin: 5px;">';
			var selectedMonth = monthOfFrequency[frequencyType];
			var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
			$.each(months, function(index, value){
				if(selectedMonth == index){
					monthFilter += '<option value="'+index+'" selected>'+value+'</option>';
				}else{
					monthFilter += '<option value="'+index+'">'+value+'</option>';
				}
			});
			monthFilter += '</select><span>';
			$('#'+frequencyType+'_dataTable_filter').append(monthFilter);
			
			$(document).off('change','#'+frequencyType+'MonthFilter');
			$(document).on('change','#'+frequencyType+'MonthFilter', function() {
				monthOfFrequency[frequencyType] =  $('#'+frequencyType+'MonthFilter').val();
				getCustomFieldsOfFrequencyType(frequencyType, containerId, entityId, entityTypeId, entityInstanceId, engagementId, productId);
			});
		}
		
		var currentYear = new Date().getFullYear();
		var yearFilter = '<span><label style="margin: 5px;">Year :</label><select id="'+frequencyType+'YearFilter" style="margin: 5px;">';
		var selectedYear = yearOfFrequency[frequencyType];
		for(var i = (currentYear - 10); i <= (currentYear + 10); i++){
			if(selectedYear == i){
				yearFilter += '<option value="'+i+'" selected>'+i+'</option>';
			}else{
				yearFilter += '<option value="'+i+'">'+i+'</option>';
			}
		}
		yearFilter += '</select><span>';
		$('#'+frequencyType+'_dataTable_filter').append(yearFilter);
		
		$(document).off('change','#'+frequencyType+'YearFilter');
		$(document).on('change','#'+frequencyType+'YearFilter', function() {
			yearOfFrequency[frequencyType] =  $('#'+frequencyType+'YearFilter').val();
			getCustomFieldsOfFrequencyType(frequencyType, containerId, entityId, entityTypeId, entityInstanceId, engagementId, productId);
		});
	}); 
}

function getMultiFrquencyCustomFieldColumnMappings(frequencyType, entityInstanceId){
	var frequencyColumnSeries = getFrequencyColumnSeriesByType(frequencyType);
	var multiFrquencyCustomFieldColumns = [];
	multiFrquencyCustomFieldColumns.push({ mData: "fieldName", sWidth: '5%'});
	
	multiFrquencyCustomFieldColumsAvailableIndex = [];
	multiFrquencyCustomFieldColumsAvailableIndex = [0];
	var multiFrquencyCustomFieldColumsIndex = 0;
	
	$.each(frequencyColumnSeries, function(index, value){
		multiFrquencyCustomFieldColumsIndex++;
		multiFrquencyCustomFieldColumsAvailableIndex.push(multiFrquencyCustomFieldColumsIndex);
		columnMapping =	{ 
			mData: frequencyType+(index+1),
			sWidth: '2%',
			"render" : function (multiFrequencyData, multiFrequencyType, multiFrequencyFull) {
				return ('<span style="cursor : pointer;" onclick="getMultiFrequencyIndividualFieldDetailSingle(\''+multiFrequencyFull[frequencyType+(index+1)+'Id']+'\',\''+multiFrequencyFull.fieldId+'\',\''+frequencyType+'\',\''+(index+1)+'\',\''+entityInstanceId+'\')">'+multiFrequencyData+'</span>');
			},
        };
		multiFrquencyCustomFieldColumns.push(columnMapping);
	});
	
	return multiFrquencyCustomFieldColumns;
}

function getMultiFrquencyCustomFieldHeaderAndFooterColumns(frequencyType, containerId){
	var frequencyColumnSeries = getFrequencyColumnSeriesByType(frequencyType);
	var multiFrquencyCustomFieldContainer = $('#'+containerId);
	var frequencyBasedcustomFieldContainer = multiFrquencyCustomFieldContainer.find('#'+frequencyType+"CustomFieldContainer");
	if(frequencyBasedcustomFieldContainer != 'undefined' && frequencyBasedcustomFieldContainer != null){
		frequencyBasedcustomFieldContainer.empty();
	}
	
	var multiFrquencyCustomField_dtColumns = ['Field&nbsp;Name'];
	$.each(frequencyColumnSeries, function(index, value){
		multiFrquencyCustomField_dtColumns.push(value);
	});
	
	var multiFrquencyCustomFieldTable = '<table id="'+frequencyType+'_dataTable"  class="cell-border compact row-border" cellspacing="0" width="100%">';
	var multiFrquencyCustomField_thead = '<thead><tr>';
	var multiFrquencyCustomField_tfoot = '<tfoot><tr id="'+frequencyType+'_dataTable_filterRow">';
	$.each(multiFrquencyCustomField_dtColumns, function(index, value){
		multiFrquencyCustomField_thead += '<th>'+value+'</th>';
		multiFrquencyCustomField_tfoot += '<th></th>';
	});
	multiFrquencyCustomFieldTable += multiFrquencyCustomField_thead + '</tr></thead>'+ multiFrquencyCustomField_tfoot + '</tr></tfoot></table>'; //' + multiFrquencyCustomField_filter_row + '</tr>
	frequencyBasedcustomFieldContainer.append(multiFrquencyCustomFieldTable);
}

var customFieldMultiFrequencyChecker='';
function reInitializeDataTabletMultiFrquencyCustomField(){
	customFieldMultiFrequencyChecker = setTimeout(function(){				
		clearInterval(customFieldMultiFrequencyChecker);
		dataTableInstance.DataTable().columns.adjust().draw();
	},200);
}

function getFrequencyColumnSeriesByType(frequencyType){
	var frequencyColumnSeries = [];
	if(frequencyType == 'Daily'){
		frequencyColumnSeries = dailyColumnSeries;
	}else if(frequencyType == 'Weekly'){
		frequencyColumnSeries = weeklyColumnSeries;
	}else if(frequencyType == 'Monthly'){
		frequencyColumnSeries = monthlyColumnSeries;
	}else if(frequencyType == 'Quaterly'){
		frequencyColumnSeries = quaterlyColumnSeries;
	}else if(frequencyType == 'Half-yearly'){
		frequencyColumnSeries = halfYearlyColumnSeries;
	}else if(frequencyType == 'Annual'){
		frequencyColumnSeries = annualColumnSeries;
	}
	
	return frequencyColumnSeries;
} 

function getMultiFrequencyIndividualFieldDetailSingle(fieldValueId, customFieldId, frequencyType, frequencyOrder, entityInstanceId){	
	var url= 'custom.field.individual.details?customFieldValueId='+fieldValueId+'&customFieldId='+customFieldId+'&frequencyOrder='+frequencyOrder+'&frequencyMonth='+monthOfFrequency[frequencyType]+'&frequencyYear='+yearOfFrequency[frequencyType];
	var jsonObj={"Title":"Product Audit History:",
		"url": url,	
		"entityInstanceId": entityInstanceId,
		"componentUsageTitle":"customFieldSingle",
	};
	CustomFieldSingleGroping.init(jsonObj);	
}