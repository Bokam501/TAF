var CodeEditorBDDJsonObj="";
var saveScript;
var keywords = [];
var isBeingEdited ;
var isTestEngineChanged=false;
var selectedXmlFileName="";
var CodeEditorForBDDScript = function() {

	var initialise = function(jsonObj){
		CodeEditorBDDJsonObj = jsonObj;
		isTestEngineChanged=false;

		// ----- uiObjectsList -----		
		CodeMirror.uiObjectsList=[];
		var url= "list.testData.and.objectRepository.FileContent?productId="+productId+"&attachmentId=-1&type=ObjectRepository&filter="+objectRepositoryId+"&toolName="+testToolName;
		loadKeywordObjects(url, "objectRepository", jsonObj);	   
	};
	return {
		//main function to initiate the module
		init: function(jsonObj) {        	
			initialise(jsonObj);
		}		
	};	
}();

var editorBDD="";
function loadAttachments_Object(productId, idValue_ul,repositoryValue){	
	$(idValue_ul).empty();	
	$(idValue_ul).append('<option id=-"1" value= "-1" ><a href="#">ALL</a></option>');
	if("EDAT" == testToolName){


	}else{
		//$(idValue_ul).append('<option id=-"1" value= "-1" ><a href="#">ALL</a></option>');

		$.post('common.list.testdata.attachment?productId='+productId+'&attachmentType='+repositoryValue,function(data) {	
			var ary = (data.Options);
			if(ary != null && ary.length >0){
				$.each(ary, function(index, element) {
					$(idValue_ul).append('<option id="' + element.Value + '" value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
				});
			}else{				
				returnNodata(idValue_ul, ary);
			}	 
			$(idValue_ul).select2();

			if(repositoryValue == 2){
				var objectRepOption =  $("#objectrepositoryEditor_bdd_ul").find('option:selected').val();
				objectRepositoryId = objectRepOption;		

			}else{
				var testDataOption =  $("#testDataEditorRepository_bdd_ul").find('option:selected').val();	
				testDataId=testDataOption;		
			}
			productId = $("#projectCodes_ul").find('option:selected').val();
		});
	}
}

var isTestEngineChanged=false;
$(document).on('change','#testTollMaster_bdd_ul', function() {
	scriptLanguageSelectedVal = "";
	isTestEngineChanged=true;
	testEngineUnique =  $("#testTollMaster_bdd_ul").find('option:selected').val();	
	testToolName = $("#testTollMaster_bdd_ul").find('option:selected').val();
	parameterType = 'EDIT';
	isTestEngineChanged=true;
	saveAtsgParameters(idUnique,testToolName,null,null,null);
	keywordslist();
	setLanguages(testToolName);

	if(objectRepositoryId != -1 && objectRepositoryId != 'NoData' && objectRepositoryId != undefined){
		objectRepositoryFileContent();
	}else{
		objectRepositoryFileContent();
	}

	if(testDataId !=-1 && testDataId != 'NoData' && testDataId != undefined){
		testDataFileContent();
	}

	initializeBDDObjects(testToolName);	 
	displayBDDKeywords(idUnique, "BDD",testToolName,-1,objectRepositoryId,testDataId);	 
});

function setLanguages(testToolName){
	$("#scriptlanguage_ul").empty();
	$("#testTollMaster_bdd_scriptLanguage_ul").empty();
	$.post('getlanguage.list',function(data) {
		var ary = (data.Options);		
		var arr= [];
		var isWebTC = false;
		if(testToolName == "TESTCOMPLETE" && productType == "Web")
			isWebTC = true;
		$.each(ary, function(index, element) {
			arr = element.options1;
			eModeArr = element.options2;			
			if(testToolName == element.DisplayText){
				$.each( arr, function( key, value ) {
					$("#scriptlanguage_ul").append('<option id="' + value + '"  value="' + value + '" ><a href="#">' + value + '</a></option>');
					$("#testTollMaster_bdd_scriptLanguage_ul").append('<option id="' + value + '"  value="' + value + '" ><a href="#">' + value + '</a></option>');
				});
				if(scriptLanguageSelectedVal != "" && scriptLanguageSelectedVal != undefined && !testCaseChecked){
					$("#scriptlanguage_ul").select2('val', scriptLanguageSelectedVal);
					$("#testTollMaster_bdd_scriptLanguage_ul").select2('val', scriptLanguageSelectedVal);
					$("#testTollMaster_bdd_scriptLanguage_ul_EDAT").select2('val', scriptLanguageSelectedVal);
				}else{
					$("#scriptlanguage_ul").select2();
					$("#testTollMaster_bdd_scriptLanguage_ul").select2();
				}
				//----- radio button display -----
				var radioLength=$("#radioHandlerBtn label").length;
				for(var i=0;i<radioLength;i++){					
					$("#radioHandlerBtn label")[i].style.display = 'none';
				}				
				$.each( eModeArr, function( key, value ) {
					var keyValue = returnLableIndex(value);
					if(value == "SCRIPTLESS"){
						$("#radioHandlerBtn label")[keyValue].style.display = 'block';
					}else if(value == "Generic"){
						$("#radioHandlerBtn label")[keyValue].style.display = 'block';
					}else if(value == "TAF-MODE" && !isWebTC){
						$("#radioHandlerBtn label")[keyValue].style.display = 'block';
					}else if(value == "Test Execution"){
						$("#radioHandlerBtn label")[keyValue].style.display = 'block';
					}
				});				
				$("#radioHandlerBtn span").removeClass('checked');
				//$("#radioHandlerBtn span").eq(1).addClass('checked'); // GENERIC-MODE	
				var radioLen = $("#radioHandlerBtn input:visible").length;
				var prevSelectedVal = returnLableIndex(mode);
				radioSelectedOptionFlag = false;
				for(var i=0; i<radioLen;i++){
					if((mode != "") && (mode == $($("#radioHandlerBtn input:visible")[i]).attr('id'))){
						radioSelectedOptionFlag = true;
						break;
					}
				}
				if(radioSelectedOptionFlag && prevSelectedVal != 0){
					$("#radioHandlerBtn span").eq(prevSelectedVal).addClass('checked');
				}else {
					mode = "GENERIC-MODE";
					$("#radioHandlerBtn span").eq(1).addClass('checked');
				}
				if(mode=="TAF-MODE"){
					$("#package").val('com.hcl.taf');
					$('#package').attr('readonly','true');
				}else {
					$("#package").val('com.hcl.ers.atsg.script.output');
					$('#package').attr('readonly',false);
				}
			}
		});	
	});
}

function returnLableIndex(label){
	var indexValue=0;
	var radioLength = $("#radioHandlerBtn input").length;
	for(var i=0;i<radioLength;i++){
		var temp = $("#radioHandlerBtn input").eq(i).attr('radio-val');
		if(label == temp){
			indexValue = i;
			break;
		}		
	}		
	return indexValue; 
}
function initializeBDDObjects(testToolName){
	$("#toolMaindiv .nav-tabs li").removeClass('active');	 
	$("#toolMaindiv .nav-tabs li").eq(0).addClass('active');

	$("#toolMaindiv .tab-content #leftDragItemsHeader").addClass('active');
	$("#toolMaindiv .tab-content #rightDragItemsHeader").removeClass('active');

	$("#trigUploadEditorFile").hide();
	$("#toolMaindiv .nav-tabs li a").eq(0).text("UI Objects");

	$("#toolMaindiv .nav-tabs li").eq(1).show();
	$("#codeBDDContainerTest #rightDragItemsHeader").hide();			
	$("#leftDragItemsHeader #repositoryEditor_bdd_dd").hide();
	$("#repositoryEditor_bdd_dd").hide();
	$("#testEditorObjectRepository_bdd_dd").hide();	
}

$(document).on('change','#testDataEditorRepository_bdd_ul', function() {

	var testDataOption =  $("#testDataEditorRepository_bdd_ul").find('option:selected').val();	
	testDataId = testDataOption;
	testDataFileContent();
	displayBDDKeywords(idUnique, "BDD",testToolName,-1,objectRepositoryId,testDataId);
});

$(document).on('change','#objectrepositoryEditor_bdd_ul', function() {
	var objectRepOption =  $("#objectrepositoryEditor_bdd_ul").find('option:selected').val();
	objectRepositoryId = objectRepOption;

	objectRepositoryFileContent();
	displayBDDKeywords(idUnique, "BDD",testToolName,-1,objectRepositoryId,testDataId);
});

function loadKeywordObjects(url, type, jsonObj){		
	$.ajax({
		type: "POST",
		contentType: "application/json; ",
		url : url,		
		dataType : 'json',       
		success : function(data) {
			if(data.Result=="OK"){
				var dataItemsList = data.Records;
				var dataItemsListItems=[];
				if(dataItemsList != undefined && dataItemsList !=null && dataItemsList != ''){        			        			
					var value = '';        			
					for(var i=0;i<dataItemsList.length;i++){
						if(dataItemsList[i].split('~').length>1){
							value = dataItemsList[i].split('~')[1];
						}else{
							value = dataItemsList[i];
						}        				
						dataItemsListItems.push(value);
					}

					if(type == "testData"){
						CodeMirror.testDataItemsList = dataItemsListItems;

						if(!isTestEngineChanged){
							initializeCodeEditorBDD(jsonObj, jsonObj.subTitle);
						}
						displayCodeBDD(jsonObj, jsonObj.BDDscript);

					}else if(type == "objectRepository"){
						CodeMirror.uiObjectsList = dataItemsListItems;

						// ----- testDataItemsList -----
						CodeMirror.testDataItemsList=[];
						var url= "list.testData.and.objectRepository.FileContent?productId="+productId+"&attachmentId=-1&type=TestData&filter="+testDataId+"&toolName="+testToolName;
						loadKeywordObjects(url, "testData", jsonObj);
					}
				} else {
					if(type == "testData"){
						CodeMirror.testDataItemsList = dataItemsListItems;	        				

						if(!isTestEngineChanged){
							initializeCodeEditorBDD(jsonObj, jsonObj.subTitle);
						}
						displayCodeBDD(jsonObj, jsonObj.BDDscript);

					}else if(type == "objectRepository"){
						CodeMirror.uiObjectsList = dataItemsListItems;

						// ----- testDataItemsList -----
						CodeMirror.testDataItemsList=[];
						var url= "list.testData.and.objectRepository.FileContent?productId="+productId+"&attachmentId=-1&type=TestData&filter="+testDataId+"&toolName="+testToolName;
						loadKeywordObjects(url, "testData", jsonObj);
					}
				}

				$("#leftDragItemsHeader #repositoryEditor_bdd_dd").hide();  			        		
			}
		},
		error: function(data){
			console.log("error in ajax call");
		},
		complete: function(data){
			//console.log("complete in ajax call");
		}
	});	
}

function initializeCodeEditorBDD(jsonObj, subTitle){
	$("#codeMirrorTextEditorBDD h4").text("");
	$("#codeMirrorTextEditorBDD h4").text(jsonObj.Title);
	$("#codeMirrorTextEditorBDD #editorBDDsubTitle").text("");
	$("#codeMirrorTextEditorBDD #editorBDDsubTitle").text(subTitle);
	var leftSpan = $("#codeBDDContainerTest #leftDragItemsHeader").find("h5 span");	
	$("#codeBDDContainerTest .tabbable-custom #toolMenu").text("Object Repository");
	$("#codeBDDContainerTest .tabbable-custom .nav-tabs li a").eq(0).text("UI Objects");
	$("#codeBDDContainerTest #leftDragItemsHeader").find("h5").append(leftSpan).append("<span class='badge badge-default' id='#leftDragItemsTotalCount' style='float:right;background:#a294bb'>"+totalRepository+"</span>");

	var rightSpan = $("#codeBDDContainerTest #rightDragItemsHeader").find("h5 span");
	$("#codeBDDContainerTest #rightDragItemsHeader").find("h5").text("Test Data");
	$("#codeBDDContainerTest #rightDragItemsHeader").find("h5").append(rightSpan).append("<span class='badge badge-default' id='#rightDragItemsTotalCount' style='float:right;background:#a294bb'>"+totalTestData+"</span>");
	$('#codeEditorBDDSave, #codeEditorBDDSaveAsNewVersion, #codeEditorBDDCommit').prop('disabled', true);
	closeEditorFlag = false;
	$("#codeMirrorTextEditorBDD").modal();
}

var clearTimeoutBDD='';
function setColorForObject(lineNo){
	clearTimeoutBDD = setTimeout(function() {
		var testDataToolTip_title="TestData Object";
		var uiObjToolTip_title="UI Object";
		var testDataTextColor="firebrick";
		var uiObjTextColor="blue";

		if(lineNo == -1){		
			// ---- testDataItems -----
			$('#codeBDDContainer .marked-text-testData').attr('title', testDataToolTip_title);
			$('#codeBDDContainer .marked-text-testData').css('color', testDataTextColor);

			// ---- uiObject Items -----
			$('#codeBDDContainer .marked-text-uiObject').attr('title', uiObjToolTip_title);
			$('#codeBDDContainer .marked-text-uiObject').css('color', uiObjTextColor);

		}else{
			if($("#codeBDDContainer .CodeMirror-lines .CodeMirror-code").children().length > lineNo){

				if(currentBDDCursorKeyCode == 13){
					lineNo = lineNo-1;
				}				
				var tmp = $("#codeBDDContainer .CodeMirror-lines .CodeMirror-code").children()[lineNo];
				var currentTestDataLine = $(tmp).find('.CodeMirror-line .marked-text-testData');
				var currentUIObjLine = $(tmp).find('.CodeMirror-line .marked-text-uiObject');
				//---- testDataItems -----
				$(currentTestDataLine).attr('title', testDataToolTip_title);
				$(currentTestDataLine).css('color', testDataTextColor);

				// ---- uiObject Items -----
				$(currentUIObjLine).attr('title', uiObjToolTip_title);
				$(currentUIObjLine).css('color', uiObjTextColor);
			}
		}	
		clearTimeout(clearTimeoutBDD);
	},500);
}

function searchIndex(str, searchValue, isCaseSensitive) {
	var modifiers = isCaseSensitive ? 'gi' : 'g';
	var regExpValue = new RegExp(searchValue, modifiers);
	var matches = [];
	var startIndex = 0;
	var arr = str.match(regExpValue);

	if(arr != null){
		[].forEach.call(arr, function(element) {
			startIndex = str.indexOf(element, startIndex);
			matches.push(startIndex++);
		});		  
		//console.log("Match word found : "+searchValue+" found in line :"+str);
	}else{
		//console.log("No match word : "+searchValue+" found in line :"+str);
	}

	return matches;
}

var currentBDDCursorKeyCode=0;
function displayCodeBDD(jsonObj, code){
	//$('.generatedScriptTab > a').css('text-align','center');
	$('#scriptLanguageDD, #generationModeDD').addClass('hidden');
	$('#testTollMaster_bdd_version_dd_container').removeClass('hidden');
	$('.testStoryTab').addClass('active').css('width','50%');
	$('.generatedScriptTab').removeClass('active').css('width','50%');
	$('#codeBDDContainer').show();
	$('#codeMirrorTextEditorGeneratedScript, #downloadScriptBtn, #codeEditorBDDGenerateScript, #codeEditorBDDFormat').addClass('hidden');
	$('#codeEditorBDDSaveAsNewVersion').removeClass('hidden');

	var value = decodeURIComponent(code);
	var gherkinKeyWords = ['Feature:','Scenario:','In order to ','to ','As ','I ','Given ','When ','Then ','And ','Examples: '];
	keywords = jsonObj.data.Records[0].keywords;
	if(keywords == undefined)keywords=[];
	var gerkinScriptKeywords = keywords;	
	isBeingEdited = false;//jsonObj.data.Records[0].isBeingEdited;

	/*if(isBeingEdited){		
		CodeMirror.gerkinScriptKeywords = [];
	}else{		
		CodeMirror.gerkinScriptKeywords = gerkinScriptKeywords.concat(gherkinKeyWords);	
	}*/
	CodeMirror.gerkinScriptKeywords = gerkinScriptKeywords.concat(gherkinKeyWords);

	// ----- gerkinScriptRegularExpressions -----
	var gerkinScriptRegularExpressions = jsonObj.data.Records[0].keywordRegularExpressions; 
	CodeMirror.gerkinScriptRegularExpressions=[];

	if(gerkinScriptRegularExpressions != undefined && gerkinScriptRegularExpressions !=null && gerkinScriptRegularExpressions != ''){
		CodeMirror.gerkinScriptRegularExpressions = gerkinScriptRegularExpressions; 
	}

	if(editorBDD!=""){

		editorBDD=null;
		$("#codeBDDContainer").html('');									
		$("#codeBDDContainer").append('<textarea id="codeEditorArea_BDD" style="display: none;"></textarea>');

		editorBDD = CodeMirror.fromTextArea(document.getElementById("codeEditorArea_BDD"), {
			value: code,
			readOnly : isBeingEdited,
			lineNumbers: true,
			lineWrapping: true,
			mode: {name: "gherkin", globalVars: true},		      
			matchBrackets: true,
			hintOptions: {container: $("#codeMirrorTextEditorBDD .modal-content")},
			extraKeys: {"Ctrl-Space": "autocomplete"},
			gutters: ["CodeMirror-lint-markers", "CodeMirror-foldgutter"],
			lint: true,
			foldGutter: true,			 	      
		});		
		editorBDD.getDoc().setValue(value);

	}else{	 
		editorBDD = CodeMirror.fromTextArea(document.getElementById("codeEditorArea_BDD"), {
			value: code,
			readOnly : isBeingEdited,
			lineNumbers: true,
			lineWrapping: true,
			mode: {name: jsonObj.mode, globalVars: true},		      
			matchBrackets: true,
			hintOptions: {container: $("#codeMirrorTextEditorBDD .modal-content")},
			extraKeys: {"Ctrl-Space": "autocomplete"},
			gutters: ["CodeMirror-lint-markers", "CodeMirror-foldgutter"],
			lint: true,
			foldGutter: true,			 	      
		});		
		editorBDD.getDoc().setValue(code);

	}

	$("#editorBadgeLineNumber").text((1+editorBDD.getDoc().cm.lastLine())+" Lines");
	$("#storyLocText").text(" ( "+(1+editorBDD.getDoc().cm.lastLine())+" Lines )");
	if (isBeingEdited) {
		$("#editorBadgeReadOnly").text("Read Only");
		$("#editorBadgeReadOnly").prop('title',currentUserIdentity);
		// $("#codeEditorBDDSave").hide();
	} else {
		$("#editorBadgeReadOnly").text("Editable");
		$("#editorBadgeReadOnly").prop('title','');
		$("#codeEditorBDDSave").show();
	}

	var testDataItems = CodeMirror.testDataItemsList;
	var uiObjectsList = CodeMirror.uiObjectsList;
	var testDataItemsLength = testDataItems.length;	
	var uiObjectsListLength = uiObjectsList.length;		
	var testDataResults = new Array();
	var uiObjectResults = new Array();
	var testDataResultsLength=0;
	var uiObjectResultsLength=0;
	var closingBracketEndPosition='';

	var stIndex=0;			 
	var items='';
	var textLine,charStPosition,charEndPosition=0;		 
	var totalLength = editorBDD.getDoc().cm.lastLine();	  

	editorBDD.getDoc().cm.on("change", function(cm, change) {		
		//console.log("line num :"+editorBDD.getDoc().cm.lastLine());
		$("#editorBadgeLineNumber").text((1+(editorBDD.getDoc().cm.lastLine()))+" Lines");
		$("#storyLocText").text(" ( "+(1+(editorBDD.getDoc().cm.lastLine()))+" Lines ) ");		  
		var currentLineNo = editorBDD.getCursor().line;		  	
		charStPosition=0,charEndPosition=0;
		var markerArr = [];
		markerArr = editorBDD.getDoc().findMarks({line: currentLineNo,ch: 0}, { line: currentLineNo, ch: editorBDD.getLine(currentLineNo).length});			
		for(var i=0;i<markerArr.length;i++){
			markerArr[i].clear();				 
		}			
		textLine = editorBDD.getLine(currentLineNo);

		if($("#codeBDDContainer .CodeMirror-lines .CodeMirror-code").children().length > currentLineNo){
			var tmp = $("#codeBDDContainer .CodeMirror-lines .CodeMirror-line ").children()[currentLineNo];				 
			$(tmp).find('.marked-text-testData').css('color', '');
			$(tmp).find('.marked-text-uiObject').css('color', '');				 
			$(tmp).find('.marked-text-testData').removeClass();
			$(tmp).find('.marked-text-uiObject').removeClass();
		}

		// ---- testDataItems -----			
		var j=0,m=0,k=0,n=0;			 
		for(j=0;j<testDataItemsLength;j++){
			testDataResults=[];
			items = testDataItems[j];				 
			stIndex = searchIndex(textLine, items, true);	    	 
			if(stIndex > 0)
				testDataResults.push(stIndex);

			testDataResultsLength = testDataResults.length; 
			for(m=0; m<testDataResultsLength; m++){				 
				charStPosition = parseInt(testDataResults[m]);
				if(charStPosition != -1 && charStPosition>0 && (textLine.substr(charStPosition-1,1)==" " || textLine.substr(textLine.length,1) == "")
						&& (textLine.substr(charStPosition+items.length,1)==" " || (textLine.substr(charStPosition+items.length,1))=="[" || (charStPosition+items.length == textLine.length))){

					if(textLine.substr(charStPosition+items.length,1)=="["){
						closingBracketEndPosition = (textLine.indexOf(']',charStPosition+items.length+1));
						charEndPosition = closingBracketEndPosition+1;

					}else{
						charEndPosition = parseInt(testDataResults[m]) + items.length;						 
					}
					editorBDD.getDoc().markText({line: currentLineNo,ch: charStPosition}, { line: currentLineNo, ch: charEndPosition}, {className: 'marked-text-testData'});
				}
			}
		}				 
		// ---- uiObject Items -----			 
		stIndex=0;			 
		uiObjectResultsLength=0;
		for(k=0;k<uiObjectsListLength;k++){
			uiObjectResults=[];
			items = uiObjectsList[k];								 
			stIndex = searchIndex(textLine, items, true);
			if(stIndex > 0)
				uiObjectResults.push(stIndex);

			uiObjectResultsLength = uiObjectResults.length; 
			for(n=0; n<uiObjectResultsLength; n++){				 
				charStPosition = parseInt(uiObjectResults[n]);
				if(charStPosition != -1 && charStPosition>0 && textLine.substr(charStPosition-1,1)==" " 
					&& (textLine.substr(charStPosition+items.length,1)==" " || (charStPosition+items.length == textLine.length))){
					charEndPosition = parseInt(uiObjectResults[n]) + items.length;						 
					editorBDD.getDoc().markText({line: currentLineNo,ch: charStPosition}, { line: currentLineNo, ch: charEndPosition}, {className: 'marked-text-uiObject'});					 
				}
			}
		}			 
		setColorForObject(currentLineNo);		 
	});

	$( "#codeMirrorTextEditorBDD" ).keyup(function() {
		if(event.target.id != "searchLeftDragItems"){
			editStoryFalg = true;
			$('#codeEditorBDDSave, #codeEditorBDDSaveAsNewVersion').prop('disabled', false);
			if(editStoryFalg && isScmsystemAvailable.toUpperCase()=="YES"){
				$('#codeEditorBDDCommit').prop('disabled', false);
			}else{
				$('#codeEditorBDDCommit').prop('disabled', true);
			}
		}else {
			editStoryFalg = false;
			$('#codeEditorBDDSave, #codeEditorBDDSaveAsNewVersion, #codeEditorBDDCommit').prop('disabled', true);
		}
	});

	$( "#codeMirrorTextEditorBDD" ).keydown(function(event) {
		currentBDDCursorKeyCode=event.keyCode; 
	});

	if(parameterType == 'ADD'){		
		keywordslist();	 

		if(objectRepositoryId != -1 && objectRepositoryId != 'NoData' && objectRepositoryId != undefined){
			objectRepositoryFileContent();
		}else{
			objectRepositoryFileContent();
		}

		if(testDataId !=-1 && testDataId != 'NoData' && testDataId != undefined){
			testDataFileContent();
		}   
	}

	initializeBDDObjects(testToolName);	 

	// ---- testDataItems -----
	var j=0,m=0,k=0,n=0;

	for(var i=0;i<totalLength;i++){
		textLine = editorBDD.getLine(i);

		stIndex=0;
		testDataResultsLength=0;
		// ---- testDataItems -----		 		 
		for(j=0;j<testDataItemsLength;j++){
			testDataResults=[];
			items = testDataItems[j];			 
			stIndex = searchIndex(textLine, items, true);	    	 
			if(stIndex > 0)
				testDataResults.push(stIndex);	 

			testDataResultsLength = testDataResults.length;
			if(testDataResultsLength>0){
				for(m=0;m<testDataResultsLength;m++){				 
					charStPosition = parseInt(testDataResults[m]);
					if(charStPosition != -1 && charStPosition>0 && textLine.substr(charStPosition-1,1)==" " && (textLine.substr(charStPosition+items.length,1)==" " || (charStPosition+items.length == textLine.length))){
						charEndPosition = parseInt(testDataResults[m]) + items.length;						 
						editorBDD.getDoc().markText({line: i,ch: charStPosition}, { line: i, ch: charEndPosition}, {className: 'marked-text-testData'});					 			 
					}
				}
			}
		}				 
		// ---- uiObject Items -----		 
		stIndex=0;
		uiObjectResultsLength=0;
		var stIndexArr = [];
		for(k=0;k<uiObjectsListLength;k++){
			uiObjectResults=[];
			items = uiObjectsList[k];
			/*stIndex = searchIndex(textLine, items, true);	    	 
			 if(stIndex > 0)
	    		 uiObjectResults.push(stIndex);*/		
			stIndexArr = searchIndex(textLine, items, true);
			for(var m=0;m<stIndexArr.length;m++){
				if(stIndexArr[m] > 0)
					uiObjectResults.push(stIndexArr[m]);
			}

			uiObjectResultsLength = uiObjectResults.length; 
			for(n=0; n<uiObjectResultsLength; n++){				 
				charStPosition = parseInt(uiObjectResults[n]);
				if(charStPosition != -1 && charStPosition>0 && textLine.substr(charStPosition-1,1)==" " && (textLine.substr(charStPosition+items.length,1)==" " || (charStPosition+items.length == textLine.length))){
					charEndPosition = parseInt(uiObjectResults[n]) + items.length;						 
					editorBDD.getDoc().markText({line: i,ch: charStPosition}, { line: i, ch: charEndPosition}, {className: 'marked-text-uiObject'});					 
				}
			}
		}		 

	}	 
	setColorForObject(-1); 	 
	closeLoaderIcon();
}

var saveTestCase = 'SaveTestCase';
function codeEditorBDDExecuteHandler(){
	testRunPLanListByTestCaseId();
}
function codeEditorBDDHandlerForSaveAsNewVersion(urlValue){
	editStoryFalg = false;
	var alertMsg = "Test story has been changed, do you want to save as new version ?";
	if(closeEditorFlag){
		closeEditorFlag = false;
		var flag = bootbox.confirm(alertMsg, function(result) {
			if(result){
				callScriptGenarateHandlerForSaveAsNewVersion(urlValue);
			}
		}) ;
		callConfirm(flag);
	}else{
		callScriptGenarateHandlerForSaveAsNewVersion(urlValue);
	}
}

function callScriptGenarateHandlerForSaveAsNewVersion(urlValue){
	var testEngine = $("#testTollMaster_bdd_ul").val();
	$.ajax({
		type: "POST",				
		url : urlValue,		
		dataType : 'json',
		data: { 'testCaseId': CodeEditorBDDJsonObj.data.Records[0].testCaseId,'script':saveScript,'testEngine':testEngine,'selectedConfigFile':'edatconfig'},
		success : function(data) {
			if(data.Result=="OK"){
				var saveAsNewFlag = true;
				setStoryVersions(CodeEditorBDDJsonObj.data.Records[0].testCaseId, testEngine, saveAsNewFlag);
				var alertMsg = data.Message + " and generating script.";
				bootbox.alert({ 
					closeButton: false,
					message: alertMsg, 
					callback: function(){ 
						$("#scriptLocText").text(" ( 0 Lines) ");
						$("#scriptLocTextEDAT").text(" ( 0 Lines) ");
						setTimeout(function(){generateScriptSouceFromStory();},1000);
					}
				});
			}else if(data.Result == "ERROR"){
				$('#codeEditorBDDSaveAsNewVersion').prop('disabled', false);
				callAlert(data.Message);			        	   
			}
		},
		error: function(data){
			saveAndCloseCodeEditor();
		},
		complete: function(data){
			callConfirm(flag);
		}
	});		
}
function codeEditorSaveGeneratedScript(urlValue, value){
	closeLoaderIcon();
	editStoryFalg = false;
	var alertMsg = "Generated script has been changed, do you want to save it ?";
	if(closeEditorFlag){
		closeEditorFlag = false;
		var flag = bootbox.confirm(alertMsg, function(result) {		 
			if(result){
				callGeneratedScriptSaveHandler(urlValue);
			}      
			$('#codeEditorBDDSave').prop('disabled', false);
			return true;
		}) ;
		callConfirm(flag);
	}else{
		callGeneratedScriptSaveHandler(urlValue);
	}
}

function callGeneratedScriptSaveHandler(urlValue){
	var scriptLanguageName = $("#testTollMaster_bdd_scriptLanguage_ul").val();
	var codeGenerationMode = $("#generationModeDD_ul").find("option:selected").val();
	$.ajax({
		type: "POST",				
		url : urlValue,		
		dataType : 'json',
		data: { 'generatedScriptId': storyId,'updatedScript':saveScript,'languageName':scriptLanguageName,'testToolName':testToolName,'codeGenerationMode':codeGenerationMode},
		success : function(data) {
			if(data.Result=="OK"){
				jsonObj={"Title":"Script Code: ",
						"SubTitle": "Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName, 
						"data":"",
						"code_RadioBtn1Name":"Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName,
						"code_RadioBtn1":data.Record.testScript,
						"code_RadioBtn2Name":"Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName,
						"mode":"text/x-java",
				};
				displayCode(jsonObj, jsonObj.code_RadioBtn1Name, jsonObj.code_RadioBtn1);	
				if(data.Message.includes("Authentication Failed.")){
					callAlert(data.Message+"! Script Updated Successfully.");
				}
				else{
					if(commitFlag){
						codeEditorBDDCommitHandler();
					}else{
						callAlert(data.Message+"Script Updated Successfully.");
					}
				}
			}else if(data.Result == "ERROR"){
				$('#codeEditorBDDSave').prop('disabled', false);
				callAlert(data.Message);
			}
		},
		error: function(data){
			saveAndCloseCodeEditor();
		},
		complete: function(data){
			$('#codeEditorBDDSave').prop('disabled', false);
		}
	});
}
var versionSelectedVal;
function codeEditorBDDHandler(urlValue, value){
	closeLoaderIcon();
	editStoryFalg = false;
	versionSelectedVal = $("#testTollMaster_bdd_version_ul").find('option:selected').val();
	var alertMsg;
	if(closeEditorFlag){
		closeEditorFlag = false;
		alertMsg = "Test story has been changed, do you want to save it ?";
		var flag = bootbox.confirm(alertMsg, function(result) {
			if(result){
				callScriptGenerateHandler(urlValue);
			}
		}) ;
	}else{
		callScriptGenerateHandler(urlValue);
	}
	callConfirm(flag);
}

function callScriptGenerateHandler(urlValue){
	var testEngine = $("#testTollMaster_bdd_ul").val();
	$.ajax({
		type: "POST",				
		url : urlValue,		
		dataType : 'json',
		data: { 'testCaseId': CodeEditorBDDJsonObj.data.Records[0].testCaseId,'versionId':versionSelectedVal, 'testEngine':testEngine,'testRunPlanId': testrunPlanIdForTestCaseExecution,'runconfigId':runConfigCheckBoxArrVal,'script':saveScript,'testCaseAutomationScriptId':storyId},
		success : function(data) {
			if(data.Result=="OK"){
				var alertMsg = data.Message + " and generating script.";
				bootbox.alert({ 
					closeButton: false,
					message: alertMsg, 
					callback: function(){ setTimeout(function(){generateScriptSouceFromStory();},500); }
				});
			}else if(data.Result == "ERROR"){
				callAlert(data.Message);			        	   
			}
		},
		error: function(data){
			saveAndCloseCodeEditor();
		},
		complete: function(data){
			setTimeout(function(){callConfirm(flag);},100);
			$("#scriptLocText").text(" ( "+(editor.getDoc().cm.lastLine())+" LOC )");
			$("#scriptLocTextEDAT").text(" ( 0 Lines) ");
			$('#codeEditorBDDSave').prop('disabled', false);
		}
	});	
}

function generateScriptSouceFromStory(){
	openLoaderIcon();
    testCaseIdList = [];
    if(testCaseIdArr.length > 0 && (idUnique == null || idUnique == undefined)){
           testCaseIdList = testCaseIdArr;
           type = 'MultipleTestCases';
           testToolName = $('#testTollMaster_bdd_multiple_ul').find('option:selected').val();
    }else{
           testCaseIdList.push(idUnique);
           type = 'SingleTestCase';
    }

    if($('#codeMirrorTextEditorBDD').is(':visible')){
           testToolName = $('#testTollMaster_bdd_ul').find('option:selected').val();
    }
    else if($('#codeMirrorTextEditorBDDEmbedded').is(':visible')){
           testToolName = $('#testTollMaster_bddEmbedded_ul').find('option:selected').val();
    }
    scriptClosePopupFlag = false;
    $("#codeEditorBottomConatiner").show(); 
    $("#atsgLoaderIcon").show();  

    testMethodSingleInBDD = $("#radioHandlerBtn1 .checked input").attr('id');
    packageName = "com.hcl.taf";
    codeGenerationMode = "TAF-MODE";
    if(testMethodSingleInBDD == 'TEST_METHOD_SINGLE_STORY'){
           testMethodSingleInBDD = 'TEST_METHOD_SINGLE';
           testClasssingle = 'TEST_CLASS_SINGLE' ;

    }else if(testMethodSingleInBDD == 'TEST_METHOD_PER_STEP'){
        testMethodSingleInBDD = 'TEST_METHOD_PER_STEP'; 
        testClasssingle = 'TEST_CLASS_SINGLE' ;

    }else if(testMethodSingleInBDD == 'SCENARIO_LEVEL'){ 
           testClasssingle = $("#radioHandlerBtn2 .checked input").attr('id') ;

           if(testClasssingle == 'NEW_METHOD'){
                  testMethodSingleInBDD = 'TEST_METHOD_PER_SCENARIO';
                  testClasssingle = 'TEST_CLASS_SINGLE' ;

           }else if(testClasssingle == 'NEW_CLASS'){
                  testMethodSingleInBDD = 'TEST_METHOD_SINGLE';
                  testClasssingle = 'TEST_CLASS_PER_SCENARIO' ;
           }
    }

    scriptsFor = "TestCase";
    var jsonObj='';
    var versionId;
    if(testToolName != "EDAT"){
           versionId = $("#testTollMaster_bdd_version_ul").val();
           if(versionId == null)versionId = currVerId;
    }else{
           versionId = $("#testTollMaster_bdd_version_ul_EDAT").val();
           if(versionId == null)versionId = versionSelectedValEDAT;
    }
    if(type == "MultipleTestCases"){
           versionId = -1;
    }
    //adding newly
    scriptLanguage = "JAVA_TESTNG";
    if(testToolName == "PROTRACTOR"){
    	codeGenerationMode = "TAF-MODE";
    	 packageName = "com.hcl.taf";
    	scriptLanguage = "JAVASCRIPT";
    }else if(testToolName == "CODEDUI"){
        scriptLanguage = "CSHARP";
    }else if(testToolName == "TESTCOMPLETE" && productType == "Desktop"){
        codeGenerationMode = "TAF-MODE";
        scriptLanguage = "JSCRIPT";
        packageName = "com.hcl.taf";
    }else if(testToolName == "TESTCOMPLETE" && productType == "Web"){
    	 codeGenerationMode = "GENERIC-MODE";
         scriptLanguage = "JSCRIPT";
         packageName = "com.hcl.ers.atsg.script.output";
    }else if(testToolName == "CUSTOM_CISCO" && productType == "Device"){
	   	 codeGenerationMode = "GENERIC-MODE";
	     scriptLanguage = "PYTHON";
	     packageName = "com.hcl.ers.atsg.script.utils.ATSG_Keywords_Python_CustomCisco";
	}
    if(testCaseIdList[0] == null || testCaseIdList[0] == undefined)testCaseIdList[0] = testCaseId;
    var url= "product.testcase.scriptsource?ids="+testCaseIdList+"&type="+type+"&versionId="+versionId;       
    $.ajax({
           type: "POST",
           url: url,
           dataType: "json", // expected return value type      
           data: { 'scriptsFor': scriptsFor,'scriptType':"GHERKIN",'testEngine':testToolName,'name':testCaseName,'codeGenerationMode':codeGenerationMode,'scriptLanguage':scriptLanguage,'productId':productId,'testMethodSingle':testMethodSingleInBDD,'testClasssingle':testClasssingle,'packageName':packageName,'storyId':storyId},
           success: function(data) {               
                  $("#atsgLoaderIcon").hide(); 
                  $('#submitButton').prop('disabled', false);
                  $("#generateScriptLoaderIcon").hide();

           },
           error: function(data) {
                  $("#atsgLoaderIcon").hide();            
                  $('#submitButton').prop('disabled', false);
                  $("#generateScriptLoaderIcon").hide();
                  closeGenerateScriptHandler();

                  $("#codeMirrorTextEditorBDD").css("display","block");
                  scriptClosePopupFlag = true;
                  callAlert(data.statusText);
                  return false;
           },
           complete: function(data){
                  $("#atsgLoaderIcon").hide();
                  closeLoaderIcon();
                  $('#codeEditorBDDSaveAsNewVersion').prop('disabled', false);
                  $('#codeEditorBDDSave').prop('disabled', false);

                  if(data.responseJSON.Result=="ERROR"){
                        code="";

                        $("#generateScriptLoaderIcon").hide();
                        $('#submitButton').prop('disabled', false);

                        closeGenerateScriptHandler();                  

                        if($('#codeMirrorTextEditorBDD').is(':visible')){
                               $("#codeMirrorTextEditorBDD").css("display","block");
                        }
                        else if($('#codeMirrorTextEditorBDDEmbedded').is(':visible')){
                               $("#codeMirrorTextEditorBDDEmbedded").css("display","block");
                        }

                        scriptClosePopupFlag = true;
                        callAlert(data.responseJSON.Message);
                        return false;
                  }else{
                        downloadPath=data.responseJSON.Records[0].downloadPath;
                  }

                  if(type == 'SingleTestCase'){
                        scriptFileCode = data.responseJSON.Records[0].scriptFileCode;
                        mainFileCode =      data.responseJSON.Records[0].mainFileCode;
                        code_testCaseClassName = data.responseJSON.Records[0].testCaseClassName;                                
                        code_testCaseScript = data.responseJSON.Records[0].testCaseScript;

                        jsonObj={"Title":"Script Code: ",
                                      "SubTitle": "Test Case ID : "+idUnique+" - Language: "+scriptLanguage+" - Test Engine : "+testToolName, 
                                      "data":data,
                                      "code_RadioBtn1Name":"Test Case ID : "+idUnique+" - Language: "+scriptLanguage+" - Test Engine : "+testToolName,
                                      "code_RadioBtn1":scriptFileCode,
                                      "code_RadioBtn2Name":"Test Case ID : "+idUnique+" - Language: "+scriptLanguage+" - Test Engine : "+testToolName,
                                      "code_RadioBtn2":mainFileCode,
                                      "mode":"text/x-java",
                        };
                        
                        //saveGeneratedScript(storyId, scriptFileCode, testToolName, scriptLanguage,downloadPath,codeGenerationMode);
                        if(testToolName!="EDAT"){
                               CodeEditor.init(jsonObj);
                        }else{
                               displayCodeEDAT(jsonObj, jsonObj.code_RadioBtn1Name, jsonObj.code_RadioBtn1);
                        }

                        /* $("#scriptBadgeLineNumber").text(scriptFileLineofCode+" LOC");*/
                        //$("#editorBadgeLineNumber").text((editor.getDoc().cm.lastLine())+" LOC");
                        if(testToolName != "EDAT"){
                               if(testToolName == "PROTRACTOR" || testToolName == "TESTCOMPLETE"){
                                      $("#scriptLocText").text(" ( " +(1+editor.getDoc().cm.lastLine())+" LOC )");
                               }else{
                                      $("#scriptLocText").text(" ( "+(editor.getDoc().cm.lastLine())+" LOC )");
                               }
                        }else{
                               $("#scriptLocTextEDAT").text(" ( "+(editorEDAT.getDoc().cm.lastLine())+" LOC )");
                        }
                        $("#codeEditorRadios label ").eq(0).empty();
                        $("#codeEditorRadios label ").eq(1).empty();
                        $("#codeEditorRadios  label").eq(0).append("<input type='radio' class='toggle'  >"+testCaseName);
                        /*$("#codeEditorRadios  label").eq(1).append("<input type='radio' class='toggle'  >Main");*/
                        $("#codeEditorRadios  label").eq(1).hide();

                        if($('#codeMirrorTextEditorBDD').is(':visible')){
                               $("#codeMirrorTextEditorBDD").css("display","block");
                        }
                        else if($('#codeMirrorTextEditorBDDEmbedded').is(':visible')){
                               $("#codeMirrorTextEditorBDDEmbedded").css("display","block");
                        }

                        $("#codeEditorRadios label input").show();
                        if(commitFlag) codeEditorBDDCommitHandler();
                  }else{

                        downloadGenerateScript();
                  }                    

                  $("#generateScriptLoaderIcon").hide();
                  $('#submitButton').prop('disabled', false);
                  if(testToolName != "EDAT"){
                        $('#java-code').css('display','none');
                        $('.CodeMirror.cm-s-default').removeClass('hidden');
                        $("#codeMirrorTextEditorGeneratedScript").css('z-index',100052);
                        $("#codeMirrorTextEditorGeneratedScript").removeClass('hidden');               
                        $("#codeBDDContainer").hide();
                        $('.testStoryTab').removeClass('active').css('width','25%');
                        $('.generatedScriptTab').addClass('active').css('width','75%');
                        
                    	$('#testTollMaster_bdd_version_dd_container, #codeEditorBDDSaveAsNewVersion, #codeEditorBDDExecuteHandler').addClass('hidden');
                    	$('#codeMirrorTextEditorGeneratedScript, #scriptLanguageDD, #generationModeDD, #downloadScriptBtn, #codeEditorBDDGenerateScript, #codeEditorBDDFormat').removeClass('hidden');
                    	if(testToolName == "TESTCOMPLETE" && productType == "Web"){
                    		$('#generationModeDD_ul').select2('val','GENERIC-MODE');
                    	}
                  }else{
                        $('#java-code').css('display','none');
                        $('.CodeMirror.cm-s-default').removeClass('hidden');
                        $("#codeMirrorTextEditorGeneratedScriptEDAT").css('z-index',100052);
                        $("#codeMirrorTextEditorGeneratedScriptEDAT").removeClass('hidden');                    
                        $("#codeBDDEmbeddedContainer").hide();
                        $('.testStoryTabEDAT').removeClass('active');
                        $('.generatedScriptTabEDAT').addClass('active');
                  }
                  closeLoaderIcon();
                  closeGenerateScriptHandler();
                  scriptClosePopupFlag = true;             
           }
	});
}

function callConfirm(flag){	
	if(flag){
		$(".bootbox-confirm button").eq(1).removeClass("btn-default");
		$(".bootbox-confirm button").eq(1).addClass("grey-cascade");		

		$(".bootbox-confirm button").eq(2).removeClass("btn-primary");
		$(".bootbox-confirm button").eq(2).addClass("green-haze");
		
		$(".bootbox-alert .modal-footer button").removeClass("btn-primary");
		$(".bootbox-alert .modal-footer button").addClass("green-haze");
	}
}

var commitFlag = false;
var closeEditorFlag = false;
function codeEditorBDDCommitHandlerInit(){
	commitFlag = true;
	codeEditorBDDSaveHandler();
}

function codeEditorBDDCommitHandler(){
	commitFlag = false;
	var alertMsg = "Do you want to commit the changes ?";
	var isStoryTabActive = $('.testStoryTab').hasClass('active');
	var checkInUrl;
	var jsonData;
	if(isStoryTabActive){
		checkInUrl = "product.testcase.automationscript.svn.checkin";
		jsonData = {'testCaseId': CodeEditorBDDJsonObj.data.Records[0].testCaseId,'testCaseAutomationScriptId':storyId};
	}
	else{
		checkInUrl = "generatedscript.svn.checkin";
		jsonData = {'generatedScriptId':storyId};
	}
		
	var flag = bootbox.confirm({
	    title: alertMsg,
	    message: '<input type="text" id="commitComments" style="margin-left:15px;margin-bottom:10px;width:95%;" placeholder="comments"><span><input type="checkbox" id="isTestData" style="margin-left:15px;" checked>Test Data</span><span><input type="checkbox" id="isObjectRepository" style="margin-left:15px;" checked>Object Repository</span>', 
	    callback: function (result) {
			if(result){
				var comments = $('#commitComments').val();
				var isObjectRepository = $('#isObjectRepository').prop('checked');
				var isTestdata = $('#isTestData').prop('checked');	
				if(comments != ""){
					jsonData.comments = comments;
					jsonData.isObjectRepository = isObjectRepository;
					jsonData.isTestdata = isTestdata;
					$.ajax({
						type: "POST",				
						url: checkInUrl,
						dataType : 'json',
						data: jsonData,
						success : function(data) {
							if(data.Result=="OK"){
								callAlert(data.Message);
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
				}else{
					if($('#commentsError').length == 0)
					$('#commitComments').after('<span id="commentsError" class="help-block" style="color:red;margin-left:15px;">Please enter comments</span>');
					return false;
				}
			}
		}
	});
	callConfirm(flag);
}

function codeEditorBDDSaveHandler(){
	openLoaderIcon();
	$('#codeEditorBDDSave').prop('disabled', true);
	//var dataSave = CodeEditorBDDJsonObj.data.Records[0];
	var activeTab = $('.generatedScriptTab').hasClass('active');
	var lineItem='';
	testToolName = $("#testTollMaster_bdd_ul").find('option:selected').val();
	saveAtsgParameters(idUnique,testToolName,null,null,null);
	if(activeTab){ // ScriptTab
		//if(editor != ""){
		if($('.scriptTextArea').css('display') != 'block'){
			saveScript = editor.getValue(false);
			saveScript = saveScript.join('\\n');	
			var onSave = 'generatedscript.update';//CodeEditorBDDJsonObj.save;	
			codeEditorSaveGeneratedScript(onSave,saveTestCase);
		}else {
			$('#codeEditorBDDSave').prop('disabled', false);
			callAlert("please generate script before saving ..!");
			closeLoaderIcon();
			return false;
		}
	}else{
		lineItem='';
		saveScript = editorBDD.getValue(false);
		saveScript = saveScript.join('\\n');	
		var onSave = CodeEditorBDDJsonObj.save;	
		//console.log("Script Content : \n"+lineItem);	
		setTimeout(function(){codeEditorBDDHandler(onSave,saveTestCase);},3000);
	}

	if(isTestEngineChanged){
		myTestCaseListDataTable();
	}	
	isTestEngineChanged=false;
}

function codeEditorBDDSaveAsNewVersionHandler(){
	commitFlag = false;
	$('#codeEditorBDDSaveAsNewVersion').prop('disabled', true);
	var lineItem='';
	saveScript = editorBDD.getValue(false);
	saveScript = saveScript.join('\\n');	
	var url = 'teststory.newversion.save';	
	codeEditorBDDHandlerForSaveAsNewVersion(url);

	if(isTestEngineChanged){
		myTestCaseListDataTable();
	}	
	isTestEngineChanged=false;
}

function codeEditorBDDCancelHandler(){	
	closeCodeMirrorTextEditorBDD();	
}

//BEGIN: GENERATED TAB
function testStoryContentHandler(){
	$('.testStoryTab').css('width','50%');
	$('.generatedScriptTab').css('width','50%');
	$('#codeBDDContainer').show();
	$('.CodeMirror.cm-s-default').removeClass('hidden');
	$('#testTollMaster_bdd_version_dd_container').removeClass('hidden');
	$('#scriptLanguageDD, #generationModeDD').addClass('hidden');
	$('#codeEditorBDDSaveAsNewVersion, #codeEditorBDDExecuteHandler').removeClass('hidden');
	$('#codeMirrorTextEditorGeneratedScript').addClass('hidden');
	$('#downloadScriptBtn, #codeEditorBDDGenerateScript, #codeEditorBDDFormat').addClass('hidden');
	$("#editorBadgeLineNumber").text((1+editorBDD.getDoc().cm.lastLine())+" Lines");
	$("#storyLocText").text(" ( "+(1+editorBDD.getDoc().cm.lastLine())+" Lines )");	
}

function testStoryContent(){
	if(!$('.testStoryTab').hasClass('active')){
		testStoryContentHandler();
	}
}

function generatedScriptContent(){
	if(!$('.generatedScriptTab').hasClass('active')){
		generatedScriptContentHandler();
	}
}

function generatedScriptContentHandler(){
	openLoaderIcon();
	$('.testStoryTab').css('width','25%');
	$('.generatedScriptTab').css('width','75%');
	$('#codeBDDContainer').hide();
	$('#codeMirrorTextEditor').modal('hide');
	$('#scriptLanguageDD ,#generationModeDD').removeClass('hidden');
	$('#testTollMaster_bdd_version_dd_container').addClass('hidden');
	$('#codeEditorBDDSaveAsNewVersion, #codeEditorBDDExecuteHandler').addClass('hidden');
	$('#downloadScriptBtn, #codeEditorBDDGenerateScript, #codeEditorBDDFormat').removeClass('hidden');
	$('#codeMirrorTextEditorGeneratedScript').removeClass('hidden');
	$('#java-code').text('Script Not Generated');
	$('#java-code').addClass('scriptTextArea');
	var scriptLanguageName = $("#testTollMaster_bdd_scriptLanguage_ul").val();
	var testCaseAutomationScriptId = storyId;
	var genScript;
	if(testToolName == "TESTCOMPLETE" && productType == "Web"){
		$('#generationModeDD_ul').select2('val','GENERIC-MODE');
	}else {
		$('#generationModeDD_ul').select2('val','TAF-MODE');
	}
	if(editStoryFalg){
		codeEditorBDDSaveHandler();
		editStoryFalg = false;
	}else{
		$.ajax({
			type: "POST",
			url:"generatedscript.list?testCaseStoryId="+testCaseAutomationScriptId+'&languageName='+scriptLanguageName+'&testEngine='+testToolName+'&codeGenerationMode=TAF-MODE',
			contentType: "application/json; charset=utf-8",
			dataType : 'json',
			success : function(data) {		
				if(data.Result=="ERROR"){
					//editor.doc.cm.setValue("");
					//editor.doc.cm.clearHistory();
					$('.CodeMirror.cm-s-default').addClass('hidden');
					$('#java-code').css('display','block');
					$('#java-code').text('');
					$('#java-code').text(data.Message);
					$('#java-code').addClass('scriptTextArea');
					//editor="";
				}else{
					$('#java-code').css('display','none');
					$('.CodeMirror.cm-s-default').removeClass('hidden');
					//editor="";
					genScript = data.Record.testScript;
					if(genScript == undefined || genScript == null || genScript == "") genScript = data.Record.testScriptForGeneric;
					jsonObj={"Title":"Script Code: ",
							"SubTitle": "Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName, 
							"data":"",
							"code_RadioBtn1Name":"Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName,
							"code_RadioBtn1":genScript,
							"code_RadioBtn2Name":"Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName,
							// "code_RadioBtn2":mainFileCode,
							"mode":"text/x-java",
					};
	
					//CodeEditor.init(jsonObj);
					displayCode(jsonObj, jsonObj.code_RadioBtn1Name, jsonObj.code_RadioBtn1);		        	
				}
			},
			error : function(data) {
				//
			},
			complete: function(data){
				closeLoaderIcon();
				//if(editor == ""){
				if($('.scriptTextArea').css('display') != 'none'){	
					$("#editorBadgeLineNumber").text("0 Lines");
					$("#scriptLocText").text(" ( 0 Lines) ");
				}else{
					$("#editorBadgeLineNumber").text((editor.getDoc().cm.lastLine())+" LOC");
					if(testToolName == "PROTRACTOR" || testToolName == "TESTCOMPLETE"){
						$("#scriptLocText").text(" ( " +(1+editor.getDoc().cm.lastLine())+" LOC )");
					}else{
						$("#scriptLocText").text(" ( "+(editor.getDoc().cm.lastLine())+" LOC )");
					}
				}
			}
		});
	}
}

$(document).on('change','#generationModeDD_ul',function(){
	openLoaderIcon();
	var generationMode = $('#generationModeDD_ul').find('option:selected').val();
	var scriptLanguageName = $("#testTollMaster_bdd_scriptLanguage_ul").val();
	var testCaseAutomationScriptId = storyId;
	$.ajax({
		type: "POST",
		url:"generatedscript.list?testCaseStoryId="+testCaseAutomationScriptId+'&languageName='+scriptLanguageName+'&testEngine='+testToolName+'&codeGenerationMode='+generationMode,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {		
			if(data.Result=="ERROR"){
				$('.CodeMirror.cm-s-default').addClass('hidden');
				$('#java-code').css('display','block');
				$('#java-code').text(''); 
				$('#java-code').text(data.Message);
				$('#java-code').addClass('scriptTextArea');
				//editor="";
			}else{
				$('#java-code').css('display','none');
				$('.CodeMirror.cm-s-default').removeClass('hidden');
				if(generationMode == "TAF-MODE"){
					genScript = data.Record.testScript;
				}else if((generationMode == "GENERIC-MODE" ) && (data.Record.testScriptForGeneric != undefined || data.Record.testScriptForGeneric != null)){
					genScript = data.Record.testScriptForGeneric;
				}
				
				if(genScript==null || genScript==undefined){
					//$('.CodeMirror.cm-s-default').addClass('hidden');
					$('#java-code').css('display','block');
					$('#java-code').text('');
					$('#java-code').text("Script is not available for the story");
					$('#java-code').addClass('scriptTextArea');
					$("#scriptLocText").text(" ( 0 LOC )");
					//editor="";
					return false;
				}
				jsonObj={"Title":"Script Code: ",
						"SubTitle": "Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName, 
						"data":"",
						"code_RadioBtn1Name":"Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName,
						"code_RadioBtn1":genScript,
						"code_RadioBtn2Name":"Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName,
						"mode":"text/x-java",
				};
				displayCode(jsonObj, jsonObj.code_RadioBtn1Name, jsonObj.code_RadioBtn1);		        	
			}
		},
		error : function(data) {
			//
		},
		complete: function(data){
			closeLoaderIcon();
			//if(editor == ""){
			if($('.scriptTextArea').css('display') != 'none'){	
				$("#editorBadgeLineNumber").text("0 Lines");
				$("#scriptLocText").text(" ( 0 Lines) ");
			}else{
				$("#editorBadgeLineNumber").text((editor.getDoc().cm.lastLine())+" LOC");
				if(testToolName == "PROTRACTOR" || testToolName == "TESTCOMPLETE"){
					$("#scriptLocText").text(" ( " +(1+editor.getDoc().cm.lastLine())+" LOC )");
				}else{
					$("#scriptLocText").text(" ( "+(editor.getDoc().cm.lastLine())+" LOC )");
				}
			}
		}
	});	
});
//END: GENERATED TAB

//BEGIN - addUIObjectsFromEditor
function addUIObjectsFromEditor(){
	if($('.uiobjectTab').hasClass('active')){
		$('#myUIObjectForm input[type=text]').val('');
		$('#myUIObjectForm textarea').val('');
		if(productType=="Web"){
			$('#webContainer').removeClass('hidden');
			$('#mobileContainer').addClass('hidden');
			$('#desktopContainer').addClass('hidden');
		}else if(productType=="Mobile" || productType=="Device"){
			$('#mobileContainer').removeClass('hidden');
			$('#webContainer').addClass('hidden');
			$('#desktopContainer').addClass('hidden');
		}else if(productType=="Desktop"){
			$('#desktopContainer').removeClass('hidden');
			$('#mobileContainer').addClass('hidden');
			$('#webContainer').addClass('hidden');
		}
		$('#addUIObjectsFromEditor, #addUIObjectsFromEditorContainer').removeClass('hidden');
		$("#addUIObjectsFromEditor").modal('show');
		$("#addUIObjectsFromEditor").css('display','block');
		$('#uiObjectNameError').addClass('hidden');
		$("#idTypeDD").val($("#idTypeDD option:first").val());
		$('input[name=chrome]').val('');
		$('input[name=firefox]').val('');
		$('input[name=ie]').val('');
		$('input[name=safari]').val('');
		$('input[name=firefoxgecko]').val('');
		$('input[name=edge]').val('');
		$('#addUIObjectsFromEditor #chrome').addClass('hidden');
		$('#addUIObjectsFromEditor #firefox').addClass('hidden');
		$('#addUIObjectsFromEditor #ie').addClass('hidden');
		$('#addUIObjectsFromEditor #safari').addClass('hidden');
		$('#addUIObjectsFromEditor #firefoxgecko').addClass('hidden');
		$('#addUIObjectsFromEditor #edge').addClass('hidden');
	}else if($('.testDataTab').hasClass('active')){
		$('#myTestDataForm input[type=text]').val('');
		$('#myTestDataForm textarea').val('');
		$('#addTestDataFromEditor, #addTestDataFromEditorContainer').removeClass('hidden');
		$("#addTestDataFromEditor").modal('show');
		$("#addTestDataFromEditor").css('display','block');
		$('#testDataNameError').addClass('hidden');
	}
	$(document).off('focusin.modal');
}

$(document).on('change', '#idTypeDD', function() {
	var selectedVal = $(this).find('option:selected').text();
	if(selectedVal == 'xpath' || selectedVal == 'cssSelector'){
		$('#addUIObjectsFromEditor #chrome').removeClass('hidden');
		$('#addUIObjectsFromEditor #firefox').removeClass('hidden');
		$('#addUIObjectsFromEditor #ie').removeClass('hidden');
		$('#addUIObjectsFromEditor #safari').removeClass('hidden');
		$('#addUIObjectsFromEditor #firefoxgecko').removeClass('hidden');
		$('#addUIObjectsFromEditor #edge').removeClass('hidden');
	}else{
		$('#addUIObjectsFromEditor #chrome').addClass('hidden');
		$('#addUIObjectsFromEditor #firefox').addClass('hidden');
		$('#addUIObjectsFromEditor #ie').addClass('hidden');
		$('#addUIObjectsFromEditor #safari').addClass('hidden');
		$('#addUIObjectsFromEditor #firefoxgecko').addClass('hidden');
		$('#addUIObjectsFromEditor #edge').addClass('hidden');
	}
});

function addUIObjectsFormSubmit(){
	event.preventDefault();
	var uiObjectName = $('#uiObjectName').val();
	if(uiObjectName==""){
		$('#uiObjectNameError').text( 'Please enter name' );
		$('#uiObjectNameError').removeClass('hidden');
		$('#addUIObjectsFromEditor').scrollTop(0);
		return false;
	}else{
		if(/^[a-zA-Z0-9-_. ]*$/.test(uiObjectName) == false) {
			$('#uiObjectNameError').text( 'Please enter Valid name' );
			$('#uiObjectNameError').removeClass('hidden');
			$('#addUIObjectsFromEditor').scrollTop(0);
			return false;
		}else if(/^[a-zA-Z0-9-_.]*$/.test(uiObjectName) == false) {
			$('#uiObjectNameError').text( 'Please enter name without space.');
			$('#uiObjectNameError').removeClass('hidden');
			$('#addUIObjectsFromEditor').scrollTop(0);
			return false;
		}else{
			if(totalRepositoryData != undefined){
				for(var i=0;i<totalRepositoryData.length;i++){
					if(uiObjectName == totalRepositoryData[i].split('~')[1]){
						$('#uiObjectNameError').text( 'The name already exists...' );
						$('#uiObjectNameError').removeClass('hidden');
						$('#addUIObjectsFromEditor').scrollTop(0);
						return false;
					}
				}
			}
			//$('#uiObjectNameError').addClass('hidden');
		}
	}
	var $form = $("#myUIObjectForm");	
	var formData = getFormData($form);
	(formData.isShare == "no") ? formData['isShare']='0' : formData['isShare']='1';
	formData['productId'] = productId;
	$.ajax({
		type: "POST",
		url: 'uiObjects.save.and.update',
		dataType: 'json',
		data: formData,
		success: function(data) {  
			if(data.Result == "ERROR"){
				callAlert("Name already exists in other project");
			}else if(data.Result == "OK"){
				objectRepositoryFileContent();
				closeAddUIObjectsFromEditorPopup();
			}
		},
		error: function(data){
			console.log("Error");		   
		},
		complete: function(data) {

		}
	});	
}
function addTestDataFormSubmit(){
	event.preventDefault();
	var testDataName = $('#testDataName').val();
	if(testDataName==""){
		$('#testDataNameError').text( 'Please enter Data name' );
		$('#testDataNameError').removeClass('hidden');
		return false;
	}else{
		if(/^[a-zA-Z0-9-_. ]*$/.test(testDataName) == false) {
			$('#testDataNameError').text( 'Please enter Valid Data name' );
			$('#testDataNameError').removeClass('hidden');
			return false;
		}else if(/^[a-zA-Z0-9-_.]*$/.test(testDataName) == false) {
			$('#testDataNameError').text( 'Please enter Data name without space.');
			$('#testDataNameError').removeClass('hidden');
			return false;
		}else{
			if(totalTestDataData != undefined){
				for(var i=0;i<totalTestDataData.length;i++){
					if(testDataName == totalTestDataData[i].split('~')[1]){
						$('#testDataNameError').text( 'The name already exists...' );
						$('#testDataNameError').removeClass('hidden');
						return false;
					}
				}
			}
			//$('#testDataNameError').addClass('hidden');
		}
	}
	var $form = $("#myTestDataForm");	
	var formData = getFormData($form);
	(formData.isShare == "no") ? formData['isShare']='0' : formData['isShare']='1';
	formData['productId'] = productId;
	$.ajax({
		type: "POST",
		url: 'testDataItems.save.and.update',
		dataType: 'json',
		data: formData,
		success: function(data) {  
			if(data.Result == "ERROR"){
				//callAlert(data.Message);
			}else if(data.Result == "OK"){
				testDataFileContent();
				closeAddTestDataFromEditorPopup();
			}
		},
		error: function(data){
			console.log("Error");		   
		},
		complete: function(data) {

		}
	});	
}
function getFormData($form){
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i){
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

function closeAddUIObjectsFromEditorPopup() {
	$("#addUIObjectsFromEditor").modal('hide');
	$("#addUIObjectsFromEditor").css('display','none');
	$('#addUIObjectsFromEditorContainer').addClass('hidden');
}
function closeAddTestDataFromEditorPopup() {
	$("#addTestDataFromEditor").modal('hide');
	$("#addTestDataFromEditor").css('display','none');
	$('#addTestDataFromEditorContainer').addClass('hidden');	
}
//END - addUIObjectsFromEditor

function closeCodeMirrorTextEditorBDD() {
	clearInterval(atsgEditorTimer);
	closeEditorFlag = true;
	idUnique=null;
	testCaseIdList = [];
	$("#div_PopupBackground").fadeOut("normal");	
	if(editStoryFalg){
		codeEditorBDDSaveHandler();
		editStoryFalg = false;
	}
	saveAndCloseCodeEditor();
	$("#codeMirrorTextEditorBDD").modal('hide');
	$("#codeMirrorTextEditorBDD").css('display','none');

}

function saveAndCloseCodeEditor(){
	var fd = new FormData();
	fd.append("testCaseId", CodeEditorBDDJsonObj.data.Records[0].testCaseId);

	$.ajax({
		url : 'product.testcase.automationscript.close',
		data : fd,
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
				callAlert(data.Message);
			}
		},
		error : function(data) {
			console.log("error in ajax call");
		},
		complete: function(data){
			//console.log("complete in ajax call");
		}
	});	
}

function displayATSGHelpPopupHandler(){
	var url='bddkeywordsphrases.list?productType='+productTypeName+'&testTool='+testToolName+'&status=2&jtStartIndex=0&jtPageSize=1000';
	var jsonObj={"Title":"BDD Keywords:",
			"url": url,
			"componentUsageTitle":"BDDkeywords",
	};
	SingleJtableContainer.init(jsonObj);
}

function displayBDDKeywords(idUnique, scriptTypeUnique, testEngineUnique,productTypeName,objectRepOption,testDataOption){

	$("#atsgLoaderIcon").show();
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",					 
		url : 'product.testcase.automationscript.get?testCaseId='+idUnique+'&scriptType='+scriptTypeUnique+'&testEngine='+testEngineUnique+'&productTypeName=-1&objectRepOption='+objectRepositoryId+'&testDataOption='+testDataId+'&productId='+productId,
		dataType : 'json',
		success : function(data) {
			$("#atsgLoaderIcon").hide();
			if(data.Result=="ERROR"){
				callAlert(data.Message);
				return false;							 
			}else{								 
				if(data.Result=="OK"){			

					CodeEditorBDDJsonObj.BDDscript = data.Records[0].testCaseScript;
					CodeEditorBDDJsonObj.data.Records[0].keywords = data.Records[0].keywords;
					CodeEditorBDDJsonObj.data.Records[0].keywordRegularExpressions =data.Records[0].keywordRegularExpressions;

					// ----- uiObjectsList -----		
					CodeMirror.uiObjectsList=[];
					var url= "list.testData.and.objectRepository.FileContent?productId="+productId+"&attachmentId=-1&type=ObjectRepository&filter="+objectRepositoryId+"&toolName="+testToolName;
					loadKeywordObjects(url, "objectRepository", CodeEditorBDDJsonObj);									 									
				}
			}

		},
		error : function(data){
			$("#atsgLoaderIcon").hide();
		},
		complete : function(data){
			$("#atsgLoaderIcon").hide();
		}
	});	

}


var objectRepOptionSelected='';
function listEDataOption(){
	var idValue_ul = "objectrepositoryEditor_bdd_ul";
	$("#leftDragItemsHeader #repositoryEditor_bdd_dd").empty();
	$("#leftDragItemsHeader #repositoryEditor_bdd_dd").append('<select class="input-medium  select2me"  id="'+idValue_ul+'"  data-placeholder="Select..." ></select>');
	$("#"+idValue_ul).html('');
	$.post('edatconfig.list.option?productId='+productId+'&testCaseId='+testCaseId,function(data){	
		var ary = (data.Options);		
		if(ary != null && ary.length >0){
			//console.log(" 0 index : "+data.Options[0].DisplayText);
			$.each(ary, function(index, element) {				
				//console.log("idValue_ul:"+idValue_ul+"element :"+element);
				$("#"+idValue_ul).append('<option id="' + element.Value + '" value="' + element.Value + '" ><a href="#">' + element.DisplayText.split('.xml')[0]+ '</a></option>');
			});
		}else{
			$("#"+idValue_ul).append('<option id="-1" value="NoData">NoData</option>');	
			//returnNodata("#"+idValue_ul, ary);
		}		
		$("#"+idValue_ul).select2();

		if(objectRepOptionSelected !='')
			$("#"+idValue_ul).select2('val', objectRepOptionSelected);
	});

	$('#objectrepositoryEditor_bdd_ul').off('change').on('change', function(){	
		var objectRepOption =  $("#objectrepositoryEditor_bdd_ul").find('option:selected').text();
		objectRepositoryId = objectRepOption;
		objectRepOptionSelected = $("#objectrepositoryEditor_bdd_ul").find('option:selected').val();
		//console.log("objectRepositoryId :"+objectRepositoryId);
		updateOnEDATChange(objectRepositoryId);		
	});
}

function updateOnEDATChange(value){	
	$.ajax({
		url: 'load.configfile.option?testCaseId='+idUnique+'&productId='+productId+'&xmlFileName='+value,		    
		method: 'POST',
		contentType: false,		    
		dataType:'json',		    
		success : function(data) {
			if(data.Result=="ERROR"){
				callAlert(data.Message);
				return false;
			} else {
				objectRepositoryFileContent();
				//callAlert(data.Message);
				return false;
			}
		}, error: function(data){
			callAlert(data.Message);
			return false;
		}
	});
}

function insertTextAtCursor(editor, text) {
	var doc = editor.getDoc();
	var cursor = doc.getCursor();
	doc.replaceRange(text, cursor);
}

function pasteSelectedKeywordItemInEditor(selectedItem, titleName){
	var currentLineNo = editorBDD.getCursor().line;
	var currentCharStPos = editorBDD.getCursor().ch;
	var currentLine = editorBDD.getLine(currentLineNo);
	//console.log("currentLineNo "+(currentLineNo+1)+ " character Position At : "+currentCharStPos);
	//console.log("selectedItem "+selectedItem);

	var stPos=0;endPos=0;
	var flag=false;
	if(titleName == 'Keywords'){		
		insertTextAtCursor(editorBDD, selectedItem);

	}else if(titleName == 'Test' || titleName == 'Object'){
		var endLinePosition = editorBDD.getLine(currentLineNo).length;
		flag=false;
		for(var i=currentCharStPos;i>0;i--){
			if(currentLine.substr(i,1) == "["){
				flag=true;
				stPos=i;
				stPos={line: currentLineNo,ch: stPos};				
				break;
			}
		}

		for(var i=currentCharStPos;i<endLinePosition;i++){
			if(currentLine.substr(i,1) == "]"){
				enPos = i+1;
				enPos={line: currentLineNo,ch: enPos};	
				break;
			}
		}

		if(!flag){			
			for(var i=currentCharStPos;i>0;i--){
				if(currentLine.substr(i,1) == " "){					
					stPos=i+1;
					stPos={line: currentLineNo,ch: stPos};				
					break;
				}
			}

			for(var j=currentCharStPos;j<endLinePosition;j++){
				if(j == endLinePosition-1 || currentLine.substr(j,1) == " "){
					enPos=j+1;
					enPos={line: currentLineNo,ch: enPos};	
					break;
				}
			}
			var endStrLen = enPos.ch-stPos.ch;
			var resultStr = currentLine.substr(stPos.ch, endStrLen).trim();			
			//console.log("selected text "+resultStr);			

			var uiObjectResults = CodeMirror.uiObjectsList;
			var itemsLength = uiObjectResults.length;
			for(var k=0;k<itemsLength;k++){
				if(resultStr == uiObjectResults[k]){
					editorBDD.getDoc().replaceRange(" ", stPos, enPos);		
					stPos=stPos.ch;
					stPos={line: currentLineNo,ch: stPos};				 					 
					editorBDD.getDoc().replaceRange(selectedItem, stPos);
					break;
				}			 
			}

			var testDataResults = CodeMirror.testDataItemsList;
			itemsLength = testDataResults.length;
			for(var m=0;m<itemsLength;m++){
				/*if(resultStr.indexOf('[') !=-1){
					 resultStr = resultStr.substr(resultStr,resultStr.indexOf('['));
				 }*/
				if(resultStr == testDataResults[m]){
					editorBDD.getDoc().replaceRange(" ", stPos, enPos);
					stPos=stPos.ch;
					stPos={line: currentLineNo,ch: stPos};
					editorBDD.getDoc().replaceRange(selectedItem, stPos);
					break;
				}			 
			}			 

		}else{
			editorBDD.getDoc().replaceRange(selectedItem, stPos, enPos);
		}	

	}	
}
