var CustomFieldSingleGroping = function(){  
	var initialise = function(jsonObj){	    
	   displayCustomSinglGroupings(jsonObj);
	};
	return {
        init: function(jsonObj) {        	
       	initialise(jsonObj);
        }		
	};	
}();

var CustomFieldSingleGropingJsonObj='';
var customFieldSelectOptionsURLList=[];
var customFieldsOptionsItemCounter=0;
var isCustomFieldValueChanged = false;

function displayCustomSinglGroupings(jsonObj){
	var container='';
	CustomFieldSingleGropingJsonObj = jsonObj;
	container = '<div id="customFieldSingleDataContainer" class="scroller" style="overflow-y:auto; height:'+defineCustomFieldContainerSizeSingle(CustomFieldSingleGropingJsonObj)+'" data-always-visible="1" data-rail-visible1="1"></div>';
	$("#customFieldSingleContainerID").html('');
	$("#customFieldSingleContainerID").append(container);	
	
	getSingleFrequencyCustomFieldSingle(jsonObj);
	$("#customFieldSingleGroupId").modal();
}

var singleFrequencyJsonResponseValue = {};
function getSingleFrequencyCustomFieldSingle(jsonObj){
	var singleFrequencyFields = {};
	 $.ajax({
		  type: "POST",
		  url : jsonObj.url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			
			if(data.Result=="ERROR"){
				singleFrequencyFields = {};					
			}else{
				singleFrequencyFields = data;
			}
			singleFrequencyJsonResponseValue = data.Records[0].fieldGroup;
			displayCustomFieldSingle(singleFrequencyFields);		
		  },
		  error : function(data) {
		 },
		 complete: function(data){
		 }
	 });
	return singleFrequencyFields;
}

function displayCustomFieldSingle(dataFull){
	populateSelectListSingle(dataFull);
}

function populateSelectListSingle(dataFull){
	var data = [];
	if(typeof dataFull.Records[0] != 'undefined'){
		data = dataFull.Records[0].fieldGroup;
	}
	
	customFieldSelectOptionsURLList=[];
	customFieldsOptionsItemCounter=0;
	var customGroupItem='';
	var fieldGroupItem='';
	var obj={};
	
	customGroupItem = data[0];	
	fieldGroupItem = customGroupItem.groupFields[0];
	
	var fieldType = myfunTrim(fieldGroupItem.fieldControlType).toLowerCase();
	var url = fieldGroupItem.fieldOptionsURL;
	
	if(fieldType == "select" && fieldGroupItem.fieldOptions.length==0 && url != null && url != ""){
		obj = {id: fieldGroupItem.customFieldId , type: fieldType, url: fieldGroupItem.fieldOptionsURL, data: {}};
		customFieldSelectOptionsURLList.push(obj);
	
	}else if(fieldType == "radiobutton" && fieldGroupItem.fieldOptions.length==0 && url != null && url != ""){
		obj = {id: fieldGroupItem.customFieldId , type: fieldType, url: fieldGroupItem.fieldOptionsURL, data: {}};
		customFieldSelectOptionsURLList.push(obj);
	}
	//console.log("customFieldSelectOptionsURLList "+customFieldSelectOptionsURLList);
	
	if(customFieldSelectOptionsURLList.length>0){
		returnCustomFieldOptionsList(dataFull, customFieldSelectOptionsURLList[0]);
	}else{
		var containerGroup = defineCustomFieldGroupSingle(dataFull.Records[0]);
	    $("#customFieldSingleDataContainer").html('');
	    $("#customFieldSingleDataContainer").append(containerGroup);
	    initializeDateTimePickercustomFieldValuesSingle();
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
     		   var containerGroup = defineCustomFieldGroupSingle(dataFull.Records[0]);
     		   $("#customFieldSingleDataContainer").html('');
     		   $("#customFieldSingleDataContainer").append(containerGroup);
     		   
     		  initializeDateTimePickercustomFieldValuesSingle();     		 
     	   }
     	   customFieldsOptionsItemCounter++;     	   
         }
         },
         error: function (data) {
        	 customFieldsOptionsItemCounter++;
        	 
         },
         complete: function(data){
         	console.log('Completed');
         	
         },	            
   	});		
}

function initializeDateTimePickercustomFieldValuesSingle(){
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
					$("#"+idPicker+"_datePickerSingle").datetimepicker('setDate',customGroupItem.groupFields[j].fieldValue);
				else
					$("#"+idPicker+"_datePickerSingle").datetimepicker('setDate',new Date());
				 
			}else if(fieldType == "datepickerTimePicker"){
				if(customGroupItem.groupFields[j].fieldValue != "")
					$("#"+idPicker+"_dateTimePickerSingle").datetimepicker('setDate',customGroupItem.groupFields[j].fieldValue);
				else
					$("#"+idPicker+"_dateTimePickerSingle").datetimepicker('setDate',new Date());
			}
		}
	}	
}

function defineCustomFieldGroupSingle(data){
	var fieldGroups='';
	var fieldGroupArr=[];
	fieldGroupArr = data.fieldGroup[0];
	
	fieldGroups += '<div class="row">';
	fieldGroups +=	defineColumnGroupingSingle();
	
	fieldGroups +=	'<div class="table-scrollable">'+
	'<table class="table table-striped table-hover">'+
	'<tbody>';		
	fieldGroups += defineGroupSingle(fieldGroupArr['groupFields']);
	fieldGroups += '</tbody></table></div>';
	fieldGroups += '</div>';

	return fieldGroups;
}

function defineGroupSingle(groupFields){
	var groupField='';
	
	$("#customFieldSingleGroupId h4").text("");
	$("#customFieldSingleGroupId h4").text(groupFields[0].customFieldName);
	groupField += '<tr>';
	groupField += defineCustomFieldSingle(groupFields[0]);
	groupField += '<td><button type="button" class="btn green-haze" onclick="closeCustomFieldSingle(event)" data-dismiss="modal">Update</button>';
	groupField += '</tr>';
	return groupField;
}

// ----- update for the text input field -----
function customFieldTextUpateHandlerSingle(event){
	var id = event.target.id;
	var oldValue = event.target.defaultValue;
	var modifiedValue = event.target.value;	
	sendJsonCustomFieldObjectSingle(id, oldValue, modifiedValue);
}

// ----- update for the select dropDown field -----
function customFieldSelectUpdateHandlerSingle(event){
	var id = event.target.id;	
	var oldValue = "";
	var modifiedValue = event.currentTarget.selectedOptions[0].value;	
	sendJsonCustomFieldObjectSingle(id, oldValue, modifiedValue);	
}

// ----- update for the datepicker field -----
function customFieldDatePickerHandlerSingle(event){
	var getID = event.target.id; 
	var id = getID.split('_')[0];
	var oldValue = event.target.defaultValue;
	var modifiedValue = event.target.value;	
	sendJsonCustomFieldObjectSingle(id, oldValue, modifiedValue);
}

//----- update for the datepicker field -----
function customFieldDateTimePickerHandlerSingle(event){
	var getID = event.target.id; 
	var id = getID.split('_')[0];
	var oldValue = event.target.defaultValue;
	var modifiedValue = event.target.value;	
	sendJsonCustomFieldObjectSingle(id, oldValue, modifiedValue);
}

// ----- update for the radioButton -----
function customFieldRadioHandlerSingle(event){
	var id = event.target.id;
	var oldValue = event.target.defaultValue+" ";
	var modifiedValue = event.target.value;	
	sendJsonCustomFieldObjectSingle(id, oldValue, modifiedValue);
}

//----- update for the checkBoxButton -----
function customFieldCheckBoxHandlerSingle(event){
	var id = event.target.id;
	var oldValue = event.target.defaultValue;
	var modifiedValue = "";	
	if(event.target.checked){
		oldValue = "";		
		modifiedValue = event.target.value;
	}
	sendJsonCustomFieldObjectSingle(id, oldValue, modifiedValue);
}

var resultSingleObj='';

function sendJsonCustomFieldObjectSingle(idValue, oldValue, modifiedValue){
	if(oldValue != modifiedValue){	
		isCustomFieldValueChanged = true;
		//resultSingleObj = getJsonCustomFieldObjectSingle(idValue, CustomFieldSingleGropingJsonObj.entityInstanceId);
		resultSingleObj = getJsonCustomFieldObjectSingle(idValue);
		//resultSingleObj['customFieldId'] = resultSingleObj['fieldValueId'];
		resultSingleObj.fieldValueId = idValue;
		resultSingleObj.fieldValue = modifiedValue;
		
		resultSingleObj['oldFieldID'] = '';
		resultSingleObj['modifiedFieldID'] = idValue;
		resultSingleObj['oldFieldValue'] = oldValue;
		resultSingleObj['modifiedFieldValue'] = modifiedValue;
		resultSingleObj['modifiedField'] = resultSingleObj['customFieldName'];
		resultSingleObj['modifiedFieldTitle'] = resultSingleObj['customFieldName'];
		resultSingleObj['entityInstanceId'] = CustomFieldSingleGropingJsonObj.entityInstanceId; 
	}else{
		isCustomFieldValueChanged = false;
	}
	
}

function closeCustomFieldSingle(event){
	if(isCustomFieldValueChanged){
		var updateCustomFieldSingleFrequencyURL = "custom.field.value.save.or.update.for.instance";
		$.post(updateCustomFieldSingleFrequencyURL, resultSingleObj).done(function (data) {
			var result = data.Result;
			if(result == "ERROR"){
				callAlert(data.Message);
			}else if(result == "OK"){
				console.log("sucessfully saved");		
				editorInStanceAllCell.data(resultSingleObj.fieldValue).draw('page');
			}
		});	
	}
	$("#customFieldSingleGroupId").modal('hide');
}

function getJsonCustomFieldObjectSingle(idValue, entityInstanceId){
	var data = singleFrequencyJsonResponseValue;
	var customGroupItem=[];
	var fieldGroupItem=[];
	
	for(var i=0;i<data.length;i++){
		customGroupItem = data[i];
		
		for(var j=0;j<customGroupItem.groupFields.length;j++){		
			fieldGroupItem = customGroupItem.groupFields[j];
			if(fieldGroupItem.customFieldId == idValue){
				return fieldGroupItem;
				break;
			}
		}
	}	
	return fieldGroupItem;	
}

function defineCustomFieldSingle(data){
	var fieldType = myfunTrim(data.fieldControlType).toLowerCase();
	var resultType ='';
	$("#customFieldSingleDataContainer").css('height', "75px");
	
	if(fieldType == "text"){
		resultType='<td>'+data.fieldValue+'</td>';
	
	}else if(fieldType == "select"){
		resultType='<td><span><select id="'+data.customFieldId+'" onchange="customFieldSelectUpdateHandlerSingle(event)">'
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
		 '<input id="'+data.customFieldId+'" class="form-control input" onblur="customFieldTextUpateHandlerSingle(event)" value="'+data.fieldValue+'" />'+
		 '</span></td>'; 
	
	}else if(fieldType == "password"){
		resultType +='<td><span>'+
		 '<input id="'+data.customFieldId+'" class="form-control input" type="password" onblur="customFieldTextUpateHandler(event)" value="'+data.fieldValue+'" />'+
		 '</span></td>'; 
	
	}else if(fieldType == "datepicker"){
		resultType +='<td><span>'+
		 '<input id="'+data.customFieldId+'_datePickerSingle" class="form-control input-small date-picker col-md-2" onchange="customFieldDatePickerHandlerSingle(event);" size="10" type="text" value="'+data.fieldValue+'" style="width: 175px !important;"/>'+
		 '</span></td>';			
	
	}else if(fieldType == "datepickerTimePicker"){
		resultType +='<td><span>'+
		 '<input id="'+data.customFieldId+'_dateTimePickerSingle" class="form-control input-small date-picker col-md-2" onchange="customFieldDateTimePickerHandlerSingle(event);" size="10" type="text" value="'+data.fieldValue+'" style="width: 175px !important;"/>'+
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
			resultType += '<div class="radio-list" style="margin-left: 20px;">';
			var customFieldSelectedOption='';
			$.each(data.fieldOptions, function(index, element) {
				customFieldSelectedOption="";
				if(data.fieldValue == element.Value){
					customFieldSelectedOption = "checked";
				}else{
					customFieldSelectedOption = "";
				}
				resultType += '<label class="'+align+'"><input id="'+data.customFieldId+'" '+customFieldSelectedOption+' type="radio" onclick="customFieldRadioHandlerSingle(event);" name="selection'+data.customFieldId+'" value="' + element.Value + '"> &nbsp ' + element.DisplayText + '</label>';
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
			resultType += '<div class="radio-list">';
			$.each(data.fieldOptions, function(index, element) {
				customFieldSelectedOption="";
				var customFieldSelectedOption='';
				if(data.fieldValue == element.Value){
					customFieldSelectedOption = "checked";
				}else{
					customFieldSelectedOption = "";
				}

				resultType += '<label class="'+align+'"><input id="'+data.customFieldId+'" '+customFieldSelectedOption+' type="checkBox" name="selection'+data.customFieldId+'" onclick="customFieldCheckBoxHandlerSingle(event);" value="' + element.Value + '" checked"> &nbsp ' + element.DisplayText + '</label>';
			});
			resultType += '</div>';
			
		}else{
			resultType += '';
		}
		resultType += '<span></td>';	
	
	}else if(fieldType == "textarea"){
		$("#customFieldSingleDataContainer").css('height', "100px");
		
		resultType +='<td><span>'+
		 '<textarea id="'+data.customFieldId+'" class="form-control" onblur="customFieldTextUpateHandlerSingle(event)" name="input">'+data.fieldValue+'</textarea>'+
		 '</span></td>';		
	}
	
	return resultType;
}

//----- Resizing the popup window - small, medium and large(Default) -----

function defineCustomFieldContainerSizeSingle(jsonObj){
	var resultHeight='0px';
	var widthValue='';
	var leftValue='';
	var topValue='';
		
	widthValue = "35%";
	leftValue = "35%";
	topValue = "40%";
	resultHeight = "75px";	
	
	$("#customFieldSingleGroupId").css('width', widthValue);
	$("#customFieldSingleGroupId").css('left', leftValue);
	$("#customFieldSingleGroupId").css('top', topValue);
	
	return resultHeight;
}

// ----- Grouping the row into column groups - 1, 2, 3 and 4 (max) column groups -----

function defineColumnGroupingSingle(){
	var columnGroup='';	
	columnGroup = '<div class="col-lg-12 col-md-12">';		
	return columnGroup;	
}