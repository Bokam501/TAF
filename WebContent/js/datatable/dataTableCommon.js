

// ----- appending column values to the table header ----- 
function tableRowAppending(columnArray){
	var rowHeading='';
	$.each(columnArray, function(index, value){
		rowHeading+='<th>'+value+'</th>';
	});	
	return rowHeading;
}

// ----- appending empty column values to the table header -----
function emptyTableRowAppending(columnNos){
	var emptyRowHeading='';
	for(var i=0;i<columnNos;i++){
		emptyRowHeading+='<th></th>';		
	}
	return emptyRowHeading;
}

// ----- Convert date format from DD-MM-YYY to MM/DD/YYYY -----
function convertDTDateFormat(jsonObj, dateFieldArr){
	var resultDate='';
	var dateArr='';
	var getFormattedDate='';
	
	for(var i=0;i<jsonObj.length;i++){
		for(var j=0;j<dateFieldArr.length;j++){		
			resultDate = dateFieldArr[j];
			if(jsonObj[i].hasOwnProperty(resultDate)){
				dateArr = jsonObj[i][resultDate].split('-');
				if(dateArr.length>0){
					getFormattedDate = dateArr[1]+'/'+dateArr[0]+'/'+dateArr[2];
					jsonObj[i][resultDate] = getFormattedDate;
				}
			}
		}
	}
	return jsonObj;
}

function fixDataTablePopupHeight(elementID){
	$("#"+elementID+"_wrapper .DTFC_ScrollWrapper .dataTables_scroll").addClass("popupFullScrollHeight");
   	$("#"+elementID+"_wrapper .DTFC_ScrollWrapper .DTFC_LeftWrapper .DTFC_LeftBodyWrapper").addClass("popupFullScrollHeight");
   	$("#"+elementID+"tsResults_dataTable_wrapper .DTFC_ScrollWrapper .DTFC_RightWrapper .DTFC_RightBodyWrapper").addClass("popupFullScrollHeight");
   	
   	$("#"+elementID+"_wrapper .DTFC_ScrollWrapper").addClass("popupFullScrollFullHight");
}

//----- Convert date format from DD-MM-YYY to MM/DD/YYYY -----
function convertDTDateFormatAdd(date){
	var resultFormat='';
	var dateArr = date.split('-');
	resultFormat = dateArr[1]+'/'+dateArr[0]+'/'+dateArr[2];
	
	return resultFormat;	
}

var clearTimeoutDT='';
function reInitializeDataTable(dataTable){
	clearTimeoutDT = setTimeout(function(){				
		dataTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDT);
	},200);
}

// --- Reloading the dataTable with only with the Json data -----
function reloadDataTableHandler(data, dataTable){
	dataTable.fnClearTable();
	if(data.length>0)
		dataTable.fnAddData(data);
	
	reInitializeDataTable(dataTable);
}

function reArrangeDropDownIndex(idValue, targetValue){
	var selectOptionsList = $('#'+idValue)[0];
	if(selectOptionsList.length>0){
		for(var i=0;i<selectOptionsList.length;i++){
			if(selectOptionsList[i].value == targetValue){
				selectOptionsList.selectedIndex = i;
				break;
			}
		}
	}
}

// ----- Options Value Handler -----
function optionsValueHandler(editorID, parameter, selectedValue){
	var resultValue='';
	
	var sel = editorID.s.fields[parameter].s.opts._input;
	var optionsArr = $(sel).find('option');
	
	for(var i=0;i<optionsArr.length;i++){
		if(optionsArr[i].value == selectedValue){
			resultValue = optionsArr[i].text;
			break;
		}
	}
	
	return resultValue;
}

function optionDisplayTextHandler(optionArr, valueID){
	var resultDisplayText='';
	if(optionArr.length>0){
		for(var i=0;i<optionArr.length;i++){
			if(optionArr[i].value == valueID){
				resultDisplayText = optionArr[i].DisplayText;
				break;
			}
		}
	}
	
	return resultDisplayText;
}