var CodeEditorBDDJsonEmbeddedObj="";
var saveScript;
var keywords = [];
var isBeingEdited ;
var isTestEngineChanged=false;
var selectedXmlFileName="";
var codeEditorForBDDScriptEmbedded = function() {
  
   var initialise = function(jsonObj){
	   CodeEditorBDDJsonEmbeddedObj = jsonObj;
	   isTestEngineChanged=false;
	   saveScript='';
	   	   
		CodeMirror.uiObjectsList=[];
		var url= "list.testData.and.objectRepository.FileContent?productId="+productId+"&attachmentId=-1&type=ObjectRepository&filter="+objectRepositoryId+"&toolName="+testToolName;
		loadEmbeddedKeywordObjects(url, "objectRepository", jsonObj);	   
    };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

var editorBDDEmbedded="";
function loadAttachments_Object(projectCodeId, idValue_ul,repositoryValue){	
	$(idValue_ul).empty();	
	$(idValue_ul).append('<option id=-"1" value= "-1" ><a href="#">ALL</a></option>');
	if("EDAT" == testToolName){
		
		
	}else{
	
	$.post('common.list.testdata.attachment?projectCodeId='+projectCodeId+'&attachmentType='+repositoryValue,function(data) {	
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
			var objectRepOption =  $("#objectrepositoryEditorEmbedded_bdd_ul").find('option:selected').val();
			 objectRepositoryId = objectRepOption;				
		}		
		projectCodeId = $("#projectCodes_ul").find('option:selected').val();
	});
 }
}

function setLanguagesEmbedded(testToolName){
	$("#scriptlanguage_ul").empty();
	$.post('getlanguage.list',function(data) {
		var ary = (data.Options);
		var arr= [];
		$.each(ary, function(index, element) {
			arr = element.options1;
			eModeArr = element.options2;
			if(testToolName == element.DisplayText){
				$.each( arr, function( key, value ) {
					$("#scriptlanguage_ul").append('<option id="' + value + '"  value="' + value + '" ><a href="#">' + value + '</a></option>');
				});
				$("#scriptlanguage_ul").select2();
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
					}else if(value == "TAF-MODE"){
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
				}else {
					$("#package").val('com.hcl.ers.atsg.script.output');
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
function initializeBDDObjectsEmbedded(testToolName){
	$("#trigUploadEditorFile").show();		
	$("#codeBDDEmbeddedContainerTest .tabbable-custom .nav-tabs li a").eq(0).text("Device Objects");
	$("#testEditorObjectRepository_bdd_dd").show();
	listEDataOption();	
}

$(document).on('change','#objectrepositoryEditorEmbedded_bdd_ul', function() {
	 var objectRepOption =  $("#objectrepositoryEditorEmbedded_bdd_ul").find('option:selected').val();
	objectRepositoryId = objectRepOption;
	
	objectRepositoryFileContentEmbedded();
	displayBDDEmbeddedKeywords(idUnique, "BDD",testToolName,-1,objectRepositoryId,testDataId);
});

function loadEmbeddedKeywordObjects(url, type, jsonObj){		
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
	    				value = dataItemsList[i];
	    				dataItemsListItems.push(value);
    				}        			
        		} else {        			
        			dataItemsListItems = [];
        			
        		}
        		CodeMirror.uiObjectsList = dataItemsListItems;
        		
        		if(!isTestEngineChanged){
        			initializeCodeEditorBDDEmbedded(jsonObj, jsonObj.subTitle);
        		}
        		displayCodeBDDEmbedded(jsonObj, jsonObj.BDDscript);        			
        		$("#trigUploadEditorFile").show();
           }
      },
      error: function(data){
    	  console.log("error in ajax call");
      }
 });	
}

function initializeCodeEditorBDDEmbedded(jsonObj, subTitle){
	$("#codeMirrorTextEditorBDDEmbedded h4").text("");
	$("#codeMirrorTextEditorBDDEmbedded h4").text(jsonObj.Title);
	$("#codeMirrorTextEditorBDDEmbedded .modal-header h5").text("");
	$("#codeMirrorTextEditorBDDEmbedded .modal-header h5").text(jsonObj.subHeadingTitle);
	$("#codeMirrorTextEditorBDDEmbedded .belowHeader h5").text("");
	$("#codeMirrorTextEditorBDDEmbedded .belowHeader h5").text(jsonObj.subTitle);
	
	var leftSpan = $("#leftDragItemsHeaderEmbedded").find("h5 span");
	$("#codeBDDEmbeddedContainerTest .tabbable-custom #toolMenu").text("Tools");
	$("#codeBDDEmbeddedContainerTest .tabbable-custom .nav-tabs li a").eq(0).text("Device Objects");
	$("#leftDragItemsHeaderEmbedded").find("h5").append(leftSpan).append("<span class='badge badge-default' id='#leftDragItemsTotalCount' style='float:right;background:#a294bb'>"+totalRepository+"</span>");
	$('#codeEditorBDDEmbeddedSave, #codeEditorBDDSaveAsNewVersionEDAT, #codeEditorBDDEmbeddedCommit').prop('disabled', true);	
	closeEditorEmbeddedFlag=false;	
	$("#codeMirrorTextEditorBDDEmbedded").modal();
}

var clearTimeoutBDDEmbedded='';
function setColorForObjectEmbedded(lineNo){
	clearTimeoutBDDEmbedded = setTimeout(function() {
		var uiObjToolTip_title="UI Object";
		var uiObjTextColor="blue";
		
		if(lineNo == -1){		 
			// ---- uiObject Items -----
			$('#codeBDDEmbeddedContainer .marked-text-uiObject').attr('title', uiObjToolTip_title);
			$('#codeBDDEmbeddedContainer .marked-text-uiObject').css('color', uiObjTextColor);
		
		}else{
			if($("#codeBDDEmbeddedContainer .CodeMirror-lines .CodeMirror-code").children().length > lineNo){
				
				if(currentBDDCursorKeyCode == 13){
					lineNo = lineNo-1;
				}				
				var tmp = $("#codeBDDEmbeddedContainer .CodeMirror-lines .CodeMirror-code").children()[lineNo];
				var currentUIObjLine = $(tmp).find('.CodeMirror-line .marked-text-uiObject');
			 
				// ---- uiObject Items -----
				$(currentUIObjLine).attr('title', uiObjToolTip_title);
				$(currentUIObjLine).css('color', uiObjTextColor);
			}
		}
		clearTimeout(clearTimeoutBDDEmbedded);
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
var testEngineConfigFile='';
function displayCodeBDDEmbedded(jsonObj, code){
	closeLoaderIcon();
	$('#scriptLanguageDDEDAT').addClass('hidden');
	//$('.generatedScriptTabEDAT > a').css('text-align','center');
	$('#testTollMaster_bdd_version_dd_container_EDAT').removeClass('hidden');
    $('.testStoryTabEDAT').addClass('active');
    $('.generatedScriptTabEDAT').removeClass('active');
    $('#codeBDDEmbeddedContainer').show();
	$('#codeMirrorTextEditorGeneratedScriptEDAT, #downloadScriptBtnEDAT, #codeEditorBDDGenerateScriptEDAT').addClass('hidden');
	$('#codeEditorBDDSaveAsNewVersionEDAT').removeClass('hidden');
	
	var value = decodeURIComponent(code);
	var gherkinKeyWords = ['Feature:','Scenario:','In order to ','to ','As ','I ','Set ','Get ','And ','Examples: '];
	keywords = jsonObj.data.Records[0].keywords;
	if(keywords == undefined)keywords=[];
	var gerkinScriptKeywords = keywords;	
	 isBeingEdited = jsonObj.data.Records[0].isBeingEdited;
	
	if(isBeingEdited){		
		CodeMirror.gerkinScriptKeywords = [];
	}else{		
		CodeMirror.gerkinScriptKeywords = gerkinScriptKeywords.concat(gherkinKeyWords);	
	}
	
	// ----- gerkinScriptRegularExpressions -----
	var gerkinScriptRegularExpressions = jsonObj.data.Records[0].keywordRegularExpressions; 
	CodeMirror.gerkinScriptRegularExpressions=[];
	
	if(gerkinScriptRegularExpressions != undefined && gerkinScriptRegularExpressions !=null && gerkinScriptRegularExpressions != ''){
		CodeMirror.gerkinScriptRegularExpressions = gerkinScriptRegularExpressions; 
	}
		
	 if(editorBDDEmbedded!=""){	 		 
			editorBDDEmbedded=null;
			$("#codeBDDEmbeddedContainer").html('');									
			$("#codeBDDEmbeddedContainer").append('<textarea id="codeEditorArea_BDDEmbedded" style="display: none;"></textarea>');
			
			editorBDDEmbedded = CodeMirror.fromTextArea(document.getElementById("codeEditorArea_BDDEmbedded"), {
				  value: code,
				  readOnly : isBeingEdited,
			      lineNumbers: true,
			      lineWrapping: true,
			      mode: {name: "gherkin", globalVars: true},		      
			      matchBrackets: true,
			      hintOptions: {container: $("#codeMirrorTextEditorBDDEmbedded .modal-content")},
			      extraKeys: {"Ctrl-Space": "autocomplete"},
			      gutters: ["CodeMirror-lint-markers", "CodeMirror-foldgutter"],
				  lint: true,
				  foldGutter: true,			 	      
			    });		
			 editorBDDEmbedded.getDoc().setValue(value);
			 
	 }else{	 
		 editorBDDEmbedded = CodeMirror.fromTextArea(document.getElementById("codeEditorArea_BDDEmbedded"), {
			  value: code,
			  readOnly : isBeingEdited,
		      lineNumbers: true,
		      lineWrapping: true,
		      mode: {name: jsonObj.mode, globalVars: true},		      
		      matchBrackets: true,
		      hintOptions: {container: $("#codeMirrorTextEditorBDDEmbedded .modal-content")},
		      extraKeys: {"Ctrl-Space": "autocomplete"},
		      gutters: ["CodeMirror-lint-markers", "CodeMirror-foldgutter"],
			  lint: true,
			  foldGutter: true,			 	      
		    });		
		 editorBDDEmbedded.getDoc().setValue(code);
		
	 }
	 
	 $("#editorBadgeLineNumberEmbedded").text((1+editorBDDEmbedded.getDoc().cm.lastLine())+" Lines");
	 $("#storyLocTextEDAT").text(" ( "+(1+editorBDDEmbedded.getDoc().cm.lastLine())+" Lines )");
	 if (isBeingEdited) {
		$("#editorBadgeReadOnlyEmbedded").text("Read Only");
		$("#editorBadgeReadOnlyEmbedded").prop('title',currentUserIdentity);
		 //$("#codeEditorBDDEmbeddedSave").hide();
	 } else {
		$("#editorBadgeReadOnlyEmbedded").text("Editable");
		$("#editorBadgeReadOnlyEmbedded").prop('title','');
		$("#codeEditorBDDEmbeddedSave").show();
	 }
	 
	 var uiObjectsList = CodeMirror.uiObjectsList;
	 var uiObjectsListLength = uiObjectsList.length;		
	 var uiObjectResults = new Array();
	 var uiObjectResultsLength=0;
	 
	 var stIndex=0;			 
	 var items='';
	 var textLine,charStPosition,charEndPosition=0;		 
	 var totalLength = editorBDDEmbedded.getDoc().cm.lastLine();	  
	 
	 editorBDDEmbedded.getDoc().cm.on("change", function(cm, change) {		
		  //console.log("line num :"+editorBDDEmbedded.getDoc().cm.lastLine());
		  $("#editorBadgeLineNumberEmbedded").text((1+(editorBDDEmbedded.getDoc().cm.lastLine()))+" Lines");
		  $("#storyLocTextEDAT").text(" ( "+(1+(editorBDDEmbedded.getDoc().cm.lastLine()))+" Lines )");		  
		  	var currentLineNo = editorBDDEmbedded.getCursor().line;		  	
		  	charStPosition=0,charEndPosition=0;
			 var markerArr = [];
			 markerArr = editorBDDEmbedded.getDoc().findMarks({line: currentLineNo,ch: 0}, { line: currentLineNo, ch: editorBDDEmbedded.getLine(currentLineNo).length});			
			 for(var i=0;i<markerArr.length;i++){
				 markerArr[i].clear();				 
			 }			
			 textLine = editorBDDEmbedded.getLine(currentLineNo);
			 
			 if($("#codeBDDEmbeddedContainer .CodeMirror-lines .CodeMirror-code").children().length > currentLineNo){
				 var tmp = $("#codeBDDEmbeddedContainer .CodeMirror-lines .CodeMirror-line ").children()[currentLineNo];				 
				 $(tmp).find('.marked-text-uiObject').css('color', '');				 
  				 $(tmp).find('.marked-text-uiObject').removeClass();
			}
			 			 			 
			 // ---- uiObject Items -----			 
			 stIndex=0;			 
			 uiObjectResultsLength=0;
			 for(var k=0;k<uiObjectsListLength;k++){
				 uiObjectResults=[];
				 items = uiObjectsList[k];								 
				 stIndex = searchIndex(textLine, items, true);	    	 
				 if(stIndex > 0)
					 uiObjectResults.push(stIndex);
				 
				 uiObjectResultsLength = uiObjectResults.length; 
				 for(var n=0; n<uiObjectResultsLength; n++){				 
					 charStPosition = parseInt(uiObjectResults[n]);
					 if(charStPosition != -1 && charStPosition>0 && textLine.substr(charStPosition-1,1)==" " 
						 && (textLine.substr(charStPosition+items.length,1)==" " || (charStPosition+items.length == textLine.length))){
						 charEndPosition = parseInt(uiObjectResults[n]) + items.length;						 
						 editorBDDEmbedded.getDoc().markText({line: currentLineNo,ch: charStPosition}, { line: currentLineNo, ch: charEndPosition}, {className: 'marked-text-uiObject'});					 
					 }
				}
			 }			 
			 setColorForObjectEmbedded(currentLineNo);		 
		});
	 
	 $( "#codeMirrorTextEditorBDDEmbedded" ).keyup(function() {
		 if(event.target.id != "searchLeftDragItemsEmbedded"){
			 editStoryEmbeddedFalg = true;
			 $('#codeEditorBDDEmbeddedSave, #codeEditorBDDSaveAsNewVersionEDAT').prop('disabled', false);
			 if(editStoryEmbeddedFalg && isScmsystemAvailable.toUpperCase()=="YES"){
				 $('#codeEditorBDDEmbeddedCommit').prop('disabled', false);
			 }else{
				 $('#codeEditorBDDEmbeddedCommit').prop('disabled', true);
			 }
		 }else {
			 editStoryEmbeddedFalg = false;
			 $('#codeEditorBDDEmbeddedSave, #codeEditorBDDSaveAsNewVersionEDAT, #codeEditorBDDEmbeddedCommit').prop('disabled', true);
		 }
	});	 
	 
	 $( "#codeMirrorTextEditorBDDEmbedded" ).keydown(function(event) {
		 currentBDDCursorKeyCode=event.keyCode; 
	});
	 
	 if(parameterType == 'ADD'){		
		 keywordslistForEmbedded();	 
		 
		 if(objectRepositoryId != -1 && objectRepositoryId != 'NoData' && objectRepositoryId != undefined){
			 objectRepositoryFileContentEmbedded();
		 }else{
			 objectRepositoryFileContentEmbedded();
		 }		 
	 }
	 testEngineConfigFile = jsonObj.data.Records[0].testEngineConfigFile;
	 initializeBDDObjectsEmbedded(testToolName);	 

	 var j=0,k=0,n=0;
	 	 
	 for(var i=0;i<totalLength;i++){
		 textLine = editorBDDEmbedded.getLine(i);
	 			 
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
				 if(charStPosition != -1 && charStPosition>0 && textLine.substr(charStPosition-1,1)==" " && (textLine.substr(charStPosition+items.length,1)==" " || (charStPosition+items.length == textLine.length))){
					 charEndPosition = parseInt(uiObjectResults[n]) + items.length;						 
					 editorBDDEmbedded.getDoc().markText({line: i,ch: charStPosition}, { line: i, ch: charEndPosition}, {className: 'marked-text-uiObject'});					 
				 }
			}
		 }		 
		 
	 }	 
	 setColorForObjectEmbedded(-1);
}

//var executeTestCase = 'ExecuteTestCase';
var saveTestCase = 'SaveTestCase';
function codeEditorBDDExecuteHandler(){
	testRunPLanListByTestCaseId();
}

function codeEditorBDDEmbeddedHandlerForSaveAsNewVersion(urlValue){
	editStoryEmbeddedFalg = false;
	var alertMsg = "Test story has been changed, do you want to save as new version ?";
	if(closeEditorEmbeddedFlag){
		closeEditorEmbeddedFlag = false;
		var flag = bootbox.confirm(alertMsg, function(result) {
			if(result){
				callScriptGenarateEmbeddedHandlerForSaveAsNewVersion(urlValue);
			}
		}) ;
		callConfirm(flag);
	}else{
		callScriptGenarateEmbeddedHandlerForSaveAsNewVersion(urlValue);
	}
}

function callScriptGenarateEmbeddedHandlerForSaveAsNewVersion(urlValue){
	var testEngine = $("#testTollMaster_bddEmbedded_ul").val();
	var selectedConfigFile = $('#s2id_objectrepositoryEDAT_bdd_ul').text().trim();
	var alertMsg = "Test story has been changed, do you want to save as new version ?";
	$.ajax({
		type: "POST",				
		url : urlValue,		
		dataType : 'json',
		data: { 'testCaseId': CodeEditorBDDJsonEmbeddedObj.data.Records[0].testCaseId,'script':saveScript,'testEngine':testEngine,'selectedConfigFile':selectedConfigFile},
		success : function(data) {
			if(data.Result=="OK"){
				var saveAsNewFlag = true;
				setStoryVersions(CodeEditorBDDJsonEmbeddedObj.data.Records[0].testCaseId, testEngine, saveAsNewFlag);						
				alertMsg = data.Message + " and generating script.";
				bootbox.alert({ 
					closeButton: false,
					message: alertMsg, 
					callback: function(){ 
						$("#scriptLocText").text(" ( 0 Lines) ");
						$("#scriptLocTextEDAT").text(" ( 0 Lines) ");
						setTimeout(function(){generateScriptSouceFromStoryEDAT();},1000);
					}
				});
			}else if(data.Result == "ERROR"){
				$('#codeEditorBDDSaveAsNewVersionEDAT').prop('disabled', false);
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

function codeEditorEmbeddedSaveGeneratedScript(urlValue, value){
	closeLoaderIcon();
	editStoryEmbeddedFalg = false;	
	var alertMsg = "Test script has been changed, do you want to save it ?";
	if(closeEditorEmbeddedFlag){
		closeEditorEmbeddedFlag = false;
		var flag = bootbox.confirm(alertMsg, function(result) {		 
			if(result){
				callGeneratedScriptEmbeddedSaveHandler(urlValue);
			}      
			$('#codeEditorBDDEmbeddedSave').prop('disabled', false);
			return true;
		}) ;
		callConfirm(flag);
	}else{
		callGeneratedScriptEmbeddedSaveHandler(urlValue);
	}
}

function callGeneratedScriptEmbeddedSaveHandler(urlValue){
	var scriptLanguageName = $("#testTollMaster_bdd_scriptLanguage_ul_EDAT").val();
	//var codeGenerationMode = $("#generationModeDD_ul").find("option:selected").val(); -- TODO it needs to be added in url for 
	var codeGenerationMode = "TAF-MODE";	
	$.ajax({
		type: "POST",				
		url : urlValue,		
		dataType : 'json',
		data: { 'generatedScriptId': storyId,'updatedScript':saveScript,'languageName':scriptLanguageName,'testToolName':testToolName,'codeGenerationMode':codeGenerationMode},
		success : function(data) {
			$('#codeEditorBDDEmbeddedSave').prop('disabled', false);
		   if(data.Result=="OK"){
				jsonObj={"Title":"Script Code: ",
						 "SubTitle": "Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName, 
						 "data":"",
						 "code_RadioBtn1Name":"Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName,
						 "code_RadioBtn1":data.Record.testScript,
						 "code_RadioBtn2Name":"Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName,
						// "code_RadioBtn2":mainFileCode,
						 "mode":"text/x-java",
						};
			
					//CodeEditor.init(jsonObj);
				displayCode(jsonObj, jsonObj.code_RadioBtn1Name, jsonObj.code_RadioBtn1);	
				if(data.Message.includes("Authentication Failed.")){
					callAlert(data.Message+"! Script Updated Successfully.");
				}
				else{
					if(commitEmbeddedFlag){
						codeEditorBDDEmbeddedCommitHandler();
					}else{
						callAlert(data.Message+"Script Updated Successfully.");
					}
				}	        					
		   }else if(data.Result == "ERROR"){
			   callAlert(data.Message);			        	   
		   }
		   //saveAndCloseCodeEditor()
	  },
	  error: function(data){
		  $('#codeEditorBDDEmbeddedSave').prop('disabled', false);
		  saveAndCloseCodeEditor();
	  },
	  complete: function(data){
		  $('#codeEditorBDDEmbeddedSave').prop('disabled', false);
	  }
	});
}

var versionSelectedVal;
function codeEditorBDDEmbeddedHandler(urlValue, value){
	closeLoaderIcon();
	editStoryEmbeddedFalg = false;
	var alertMsg;
	if(closeEditorEmbeddedFlag){
		closeEditorEmbeddedFlag = false;
		alertMsg = "Test story has been changed, do you want to save it ?";
		var flag = bootbox.confirm(alertMsg, function(result) {
			if(result){
				callScriptEmbeddedGenerateHandler(urlValue);
			}
		}) ;
	}else{
		callScriptEmbeddedGenerateHandler(urlValue);
	}
	callConfirm(flag);
}

function callScriptEmbeddedGenerateHandler(urlValue){
	versionSelectedVal = $("#testTollMaster_bdd_version_ul_EDAT").val();
	var testEngine = $("#testTollMaster_bdd_ul").val();
	var selectedConfigFile = $('#s2id_objectrepositoryEDAT_bdd_ul').text().trim();
	$.ajax({
		type: "POST",				
		url : urlValue,		
		dataType : 'json',
		data: { 'testCaseId': CodeEditorBDDJsonEmbeddedObj.data.Records[0].testCaseId, 'versionId':versionSelectedVal, 'testEngine':testEngine, 'testRunPlanId': testrunPlanIdForTestCaseExecution,'runconfigId':runConfigCheckBoxArrVal,'script':saveScript,'testCaseAutomationScriptId':CodeEditorBDDJsonEmbeddedObj.data.Records[0].testCaseAutomationScriptId,'selectedConfigFile':selectedConfigFile},
		success : function(data) {
		   if(data.Result=="OK"){
				alertMsg = data.Message + " and generating script.";
				bootbox.alert({ 
					closeButton: false,
					message: alertMsg, 
					callback: function(){ setTimeout(function(){generateScriptSouceFromStoryEDAT();},500); }
				});
		   }else if(data.Result == "ERROR"){
			   callAlert(data.Message);			        	   
		   }
	  },
	  error: function(data){
		  saveAndCloseCodeEditorEmbedded();
	  },
	  complete: function(data){
		  $('#codeEditorBDDEmbeddedSave').prop('disabled', false);
	  }
	});
}

var commitEmbeddedFlag = false;
var closeEditorEmbeddedFlag = false;
function codeEditorBDDEmbeddedCommitHandlerInit(){
	commitEmbeddedFlag = true;
	codeEditorBDDEmbeddedSaveHandler();
}

function codeEditorBDDEmbeddedCommitHandler(){
	commitEmbeddedFlag = false;
	var alertMsg = "Do you want to commit the changes ?";
	var isStoryTabActive = $('.testStoryTabEDAT').hasClass('active');
	var checkInUrl;
	var jsonData;
	if(isStoryTabActive){
		checkInUrl = "product.testcase.automationscript.svn.checkin";
		jsonData = {'testCaseId': CodeEditorBDDJsonEmbeddedObj.data.Records[0].testCaseId,'testCaseAutomationScriptId':storyId};
	}
	else{
		checkInUrl = "generatedscript.svn.checkin";
		jsonData = {'generatedScriptId':storyId};
	}
		
	var flag = bootbox.confirm({
	    title: alertMsg,
	    message: '<input type="text" id="commitComments" style="margin-left:15px;width:95%;" placeholder="comments">', 
	    callback: function (result) {
			if(result){
				var comments = $('#commitComments').val();
				if(comments != ""){
					jsonData.comments = comments;
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
					if($('#commitComments').next().length == 0)
					$('#commitComments').after('<span id="commentsError" class="help-block" style="color:red;margin-left:15px;">Please enter comments</span>');
					return false;
				}
			}
		}
	});
	callConfirm(flag);
}

function callConfirm(flag){	
	if(flag){
		$(".bootbox-confirm button").eq(1).removeClass("btn-default");
		$(".bootbox-confirm button").eq(1).addClass("grey-cascade");		
	
		$(".bootbox-confirm button").eq(2).removeClass("btn-primary");
		$(".bootbox-confirm button").eq(2).addClass("green-haze");
	}
}

function codeEditorBDDEmbeddedSaveHandler(){
	$('#codeEditorBDDEmbeddedSave').prop('disabled', true);
	//var dataSave = CodeEditorBDDJsonEmbeddedObj.data.Records[0];
	var activeTab = $('.generatedScriptTabEDAT').hasClass('active');
	var lineItem='';
	testToolName = $("#testTollMaster_bddEmbedded_ul").find('option:selected').val();
	saveAtsgParameters(idUnique,testToolName,null,null,null);
	if(activeTab){ // ScriptTab
		if(editorEDAT != ""){
			saveScript = editorEDAT.getValue(false);
			saveScript = saveScript.join('\\n');	
			var onSave = 'generatedscript.update';//CodeEditorBDDJsonObj.save;	
			codeEditorEmbeddedSaveGeneratedScript(onSave,saveTestCase);
		}else {
			$('#codeEditorBDDEmbeddedSave').prop('disabled', false);
			callAlert("script is not available to save ..!");
			return false;
		}
	}else{
		lineItem='';
		saveScript = editorBDDEmbedded.getValue(false);
		saveScript = saveScript.join('\\n');	
		var onSave = CodeEditorBDDJsonEmbeddedObj.save;	
		codeEditorBDDEmbeddedHandler(onSave,saveTestCase);
	}
}
function codeEditorBDDEmbeddedSaveAsNewVersionHandler(){
	commitEmbeddedFlag = false;
	$('#codeEditorBDDSaveAsNewVersionEDAT').prop('disabled', true);
	var lineItem='';
	saveScript = editorBDDEmbedded.getValue(false);
	saveScript = saveScript.join('\\n');	
	var url = 'teststory.newversion.save';	
	codeEditorBDDEmbeddedHandlerForSaveAsNewVersion(url);
}
function codeEditorBDDEmbeddedCancelHandler(){	
	closecodeMirrorTextEditorBDDEmbedded();	
}

function closecodeMirrorTextEditorBDDEmbedded() {
	clearInterval(atsgEditorTimer);
	closeEditorEmbeddedFlag = true;
	idUnique=null;
	testCaseIdList = [];
	$("#div_PopupBackground").fadeOut("normal");	
    if(editStoryEmbeddedFalg){
		codeEditorBDDEmbeddedSaveHandler();
		editStoryEmbeddedFalg = false;
	}
    saveAndCloseCodeEditorEmbedded();
	$("#codeMirrorTextEditorBDDEmbedded").modal('hide');
	$("#codeMirrorTextEditorBDDEmbedded").css('display','none');	
}

function saveAndCloseCodeEditorEmbedded(){
	var fd = new FormData();
	fd.append("testCaseId", CodeEditorBDDJsonEmbeddedObj.data.Records[0].testCaseId);
	
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

function generateScriptSouceFromStoryEDAT(){
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
    scriptLanguage = "PYTHON";    
    if(testToolName == "EDAT"){
    	codeGenerationMode = "TAF-MODE";
    	packageName = "com.hcl.taf";
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
        	   	  closeLoaderIcon();	
                  $("#atsgLoaderIcon").hide();
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
                        
                        //saveGeneratedScript(storyId, scriptFileCode, testToolName, scriptLanguage,downloadPath);
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
						if(commitEmbeddedFlag) codeEditorBDDEmbeddedCommitHandler();
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
                        $('.testStoryTab').removeClass('active');
                        $('.generatedScriptTab').addClass('active');
                        
                    	$('#testTollMaster_bdd_version_dd_container, #codeEditorBDDSaveAsNewVersion, #codeEditorBDDExecuteHandler').addClass('hidden');
                    	$('#codeMirrorTextEditorGeneratedScript, #scriptLanguageDD, #downloadScriptBtn, #codeEditorBDDGenerateScript, #codeEditorBDDFormat').removeClass('hidden');
                  }else{
                        $('#java-code').css('display','none');
                        $('.CodeMirror.cm-s-default').removeClass('hidden');
                        $("#codeMirrorTextEditorGeneratedScriptEDAT").css('z-index',100052);
                        $("#codeMirrorTextEditorGeneratedScriptEDAT").removeClass('hidden');                    
                        $("#codeBDDEmbeddedContainer").hide();
                        $('.testStoryTabEDAT').removeClass('active');
                        $('.generatedScriptTabEDAT').addClass('active');
                        generatedScriptContentEDAT();
                  }
                  closeLoaderIcon();
                  closeGenerateScriptHandler();
                  scriptClosePopupFlag = true;             
           }
	});
}

//BEGIN: GENERATED TAB
function testStoryContentEDAT(){
	//$('.generatedScriptTabEDAT > a').css('text-align','center');
	$('#codeBDDEmbeddedContainer').show();
	$('.CodeMirror.cm-s-default').removeClass('hidden');
	$('#testTollMaster_bdd_version_dd_container_EDAT').removeClass('hidden');
	$('#scriptLanguageDDEDAT').addClass('hidden');
	$('#codeEditorBDDSaveAsNewVersionEDAT').removeClass('hidden');
	$('#codeMirrorTextEditorGeneratedScriptEDAT').addClass('hidden');
	$('#downloadScriptBtnEDAT, #codeEditorBDDGenerateScriptEDAT').addClass('hidden');
	$("#editorBadgeLineNumber").text((1+editorBDDEmbedded.getDoc().cm.lastLine())+" Lines");
	$("#storyLocTextEDAT").text(" ( "+(1+editorBDDEmbedded.getDoc().cm.lastLine())+" Lines )");
	//displayBDDScriptEditor(initialStoryData);
}

var versionSelectedValEDAT;
function generatedScriptContentEDAT(){
	//$('.testStoryTabEDAT > a').css('text-align','center');
	versionSelectedValEDAT = $("#testTollMaster_bdd_version_ul_EDAT").val();
	$('#codeBDDEmbeddedContainer').hide();
	$('#codeMirrorTextEditor').modal('hide');
	$('#scriptLanguageDDEDAT').removeClass('hidden');
	$('#testTollMaster_bdd_version_dd_container_EDAT').addClass('hidden');
	$('#codeEditorBDDSaveAsNewVersionEDAT').addClass('hidden');
	$('#downloadScriptBtnEDAT, #codeEditorBDDGenerateScriptEDAT').removeClass('hidden');
	$('#codeMirrorTextEditorGeneratedScriptEDAT').removeClass('hidden');
	$('#codeMirrorTextEditorGeneratedScriptEDAT #java-code-edat').text('Script Not Generated');
	$('#codeMirrorTextEditorGeneratedScriptEDAT #java-code-edat').addClass('scriptTextArea');
	var scriptLanguageName = $("#testTollMaster_bdd_scriptLanguage_ul_EDAT").val();
	var testCaseAutomationScriptId = storyId;
	var genScriptEDAT;
	if(editStoryEmbeddedFalg){
		codeEditorBDDEmbeddedSaveHandler();
		editStoryEmbeddedFalg = false;
	}
	 $.ajax({
		  type: "POST",
		  url:"generatedscript.list?testCaseStoryId="+testCaseAutomationScriptId+'&languageName=' + scriptLanguageName+'&testEngine=' + testToolName+'&codeGenerationMode=TAF-MODE',
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			if(data.Result=="ERROR"){
				//editorEDAT.doc.cm.setValue("");
				//editorEDAT.doc.cm.clearHistory();
				$('.CodeMirror.cm-s-default').addClass('hidden');
				$('#codeMirrorTextEditorGeneratedScriptEDAT #java-code-edat').css('display','block');
				$('#codeMirrorTextEditorGeneratedScriptEDAT #java-code-edat').text('');
				$('#codeMirrorTextEditorGeneratedScriptEDAT #java-code-edat').text(data.Message);
				$('#codeMirrorTextEditorGeneratedScriptEDAT #java-code-edat').addClass('scriptTextArea');
				editorEDAT="";
			}else{
				$('#codeMirrorTextEditorGeneratedScriptEDAT #java-code-edat').css('display','none');
				$('#codeMirrorTextEditorGeneratedScriptEDAT .CodeMirror.cm-s-default').removeClass('hidden');
				//editorEDAT="";
				genScriptEDAT = data.Record.testScript;
				if(genScriptEDAT == undefined || genScriptEDAT == null || genScriptEDAT == "") genScriptEDAT = data.Record.testScriptForGeneric;
				jsonObj={"Title":"Script Code: ",
				         "SubTitle": "Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName, 
		    			 "data":"",
		    			 "code_RadioBtn1Name":"Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName,
		    			 "code_RadioBtn1":genScriptEDAT,
		    			 "code_RadioBtn2Name":"Test Case ID : "+idUnique+" - Language: "+scriptLanguageName+" - Test Engine : "+testToolName,
		    			// "code_RadioBtn2":mainFileCode,
		    			 "mode":"text/x-java",
		    			};
		    
		        	//CodeEditor.init(jsonObj);
					if(testToolName!="EDAT"){
						displayCode(jsonObj, jsonObj.code_RadioBtn1Name, jsonObj.code_RadioBtn1);
					}else{
						displayCodeEDAT(jsonObj, jsonObj.code_RadioBtn1Name, jsonObj.code_RadioBtn1);
					}
		        }
		  },
		 error : function(data) {
			 //
		 },
		 complete: function(data){
			//closeLoaderIcon();
			 if(editorEDAT == ""){
				 $("#editorBadgeLineNumber").text("0 Lines");
				 $("#scriptLocTextEDAT").text(" ( 0 Lines )");
			}else{
				$("#editorBadgeLineNumber").text((editorEDAT.getDoc().cm.lastLine())+" LOC");
				$("#scriptLocTextEDAT").text(" ( "+(editorEDAT.getDoc().cm.lastLine())+" LOC )");
			}
		 }
	});
	//$('#codeMirrorTextEditorGeneratedScript').empty().removeClass('hidden');	
}
//END: GENERATED TAB


function displayATSGHelpPopupHandler(){
	var url='bddkeywordsphrases.list?productType='+productTypeName+'&testTool='+testToolName+'&status=2';
	var jsonObj={"Title":"BDD Keywords:",
			"url": url,
			 "componentUsageTitle":"BDDkeywords",
	};
	SingleJtableContainer.init(jsonObj);
}

function displayBDDEmbeddedKeywords(idUnique, scriptTypeUnique, testEngineUnique,productTypeName,objectRepOption,testDataOption){
		 
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
									 
									CodeEditorBDDJsonEmbeddedObj.BDDscript = data.Records[0].testCaseScript;
									CodeEditorBDDJsonEmbeddedObj.data.Records[0].keywords = data.Records[0].keywords;
									CodeEditorBDDJsonEmbeddedObj.data.Records[0].keywordRegularExpressions =data.Records[0].keywordRegularExpressions;
																		 
									// ----- uiObjectsList -----		
									CodeMirror.uiObjectsList=[];
									var url= "list.testData.and.objectRepository.FileContent?productId="+productId+"&attachmentId=-1&type=ObjectRepository&filter="+objectRepositoryId+"&toolName="+testToolName;
									loadEmbeddedKeywordObjects(url, "objectRepository", CodeEditorBDDJsonEmbeddedObj);									 									
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
	var idValue_ul = "objectrepositoryEDAT_bdd_ul";
	$("#leftDragItemsHeaderEmbedded #repositoryEditor_bdd_dd").empty();
	$("#leftDragItemsHeaderEmbedded #repositoryEditor_bdd_dd").append('<select class="input-medium  select2me"  id="'+idValue_ul+'"  data-placeholder="Select..." ></select>');
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
		
		/*if(objectRepOptionSelected !='')
			$("#"+idValue_ul).select2('val', objectRepOptionSelected);
		
		 if(testEngineConfigFile == null){
			 $('#objectrepositoryEDAT_bdd_ul').select2();
			 testEngineConfigFile = $("#objectrepositoryEDAT_bdd_ul").find('option:selected').text();
		 }else{
			 $($('#s2id_objectrepositoryEDAT_bdd_ul span')[0]).text(testEngineConfigFile);
			 //$("#objectrepositoryEDAT_bdd_ul").select2('val', testEngineConfigFile);
		 }*/
		 updateOnEDATChange($("#objectrepositoryEDAT_bdd_ul").find('option:selected').text());	
	});
	
	$('#objectrepositoryEDAT_bdd_ul').off('change').on('change', function(){	
		var objectRepOption =  $("#objectrepositoryEDAT_bdd_ul").find('option:selected').text();
		objectRepositoryId = objectRepOption;
		objectRepOptionSelected = $("#objectrepositoryEDAT_bdd_ul").find('option:selected').val();
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
		    		objectRepositoryFileContentEmbedded();
		    		//callAlert(data.Message);
		    		return false;
		    	}
		    }, error: function(data){
		    	callAlert(data.Message);
		    	return false;
		    }
	});
}

function insertTextAtCursorEmbedded(editor, text) {
    var doc = editor.getDoc();
    var cursor = doc.getCursor();
    doc.replaceRange(text, cursor);
}

function pasteSelectedKeywordItemInEditorEmbedded(selectedItem, titleName){
	var currentLineNo = editorBDDEmbedded.getCursor().line;
	var currentCharStPos = editorBDDEmbedded.getCursor().ch;
	var currentLine = editorBDDEmbedded.getLine(currentLineNo);
	//console.log("currentLineNo "+(currentLineNo+1)+ " character Position At : "+currentCharStPos);
	//console.log("selectedItem "+selectedItem);
	
	var stPos=0;endPos=0;
	var flag=false;
	if(titleName == 'Keywords'){		
		insertTextAtCursorEmbedded(editorBDDEmbedded, selectedItem);
	
	}else if(titleName == 'Test' || titleName == 'Object'){
		var endLinePosition = editorBDDEmbedded.getLine(currentLineNo).length;
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
					 editorBDDEmbedded.getDoc().replaceRange(" ", stPos, enPos);		
					 stPos=stPos.ch;
					 stPos={line: currentLineNo,ch: stPos};				 					 
					 editorBDDEmbedded.getDoc().replaceRange(selectedItem, stPos);
					 break;
				 }			 
			 }			 
		
		}else{
			editorBDDEmbedded.getDoc().replaceRange(selectedItem, stPos, enPos);
		}	
		
	}	
}