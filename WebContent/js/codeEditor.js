var CodeEditorJsonObj="";
var CodeEditor = function() {
  
   var initialise = function(jsonObj){
	   CodeEditorJsonObj = jsonObj;
	//   $("#codeEditorRadios").find("label:first").addClass("active").siblings().removeClass("active");
	   displayCode(jsonObj, jsonObj.code_RadioBtn1Name, jsonObj.code_RadioBtn1);
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

function listBDDKeywords(){	
	var url='bddkeywordsphrases.list?productType='+productTypeName+'&testTool='+testToolName+'&status=2';
	var jsonObj={"Title":"BDD Keywords:",
			"url": url,
		    // "testCaseID":testCaseId,
			// "testCaseName":testCaseName,
			// "testCaseExectionSummayTitle":"Execution Summary",
			// "testCaseExectionHistoryTitle":"Execution History",
			// "testCaseExectionHistoryUrl":"testcase.execution.history?testCaseId="+testCaseId+'&workPackageId='+workPackageId+'&dataLevel='+dataLevel,
			// "workPackageId":workPackageId,
			// "componentUsageTitle":dataLevel,
	};
	SingleJtableContainer.init(jsonObj);
	clearSingleJTableDatas();
	bddkeywordsphraseslist(jsonObj);	
}

function codeRadioBtn1(){
	displayCode(CodeEditorJsonObj, CodeEditorJsonObj.code_RadioBtn1Name, CodeEditorJsonObj.code_RadioBtn1);
}

function codeRadioBtn2(){
	displayCode(CodeEditorJsonObj, CodeEditorJsonObj.code_RadioBtn2Name, CodeEditorJsonObj.code_RadioBtn2);
}

/*function codeRadioBtn3(){
	$("#codeEditorRadios").eq(2).show();
	displayCode(CodeEditorJsonObj, CodeEditorJsonObj.code_RadioBtn3Name, CodeEditorJsonObj.code_RadioBtn3);
}*/

var editor="";
var editorEDAT="";
function displayCode(jsonObj, subTitle, code){
	//$("#codeMirrorTextEditor h4").text("");
	//$("#codeMirrorTextEditor h4").text(jsonObj.Title);
	//$("#codeMirrorTextEditor h5").text("");
	//$("#codeMirrorTextEditor h5").text(subTitle);
	//$("#codeMirrorTextEditor h5").text("Script Line Of Code"+scriptFileLineofCode);
	//$("#codeMirrorTextEditor").modal();
	$('#codeEditorBDDSaveAsNewVersionEDAT').prop('disabled',false);
	$('#codeEditorBDDEmbeddedSave').prop('disabled',false);
	//$('#codeEditorBDDSaveAsNewVersion').prop('disabled',false);
	//$('#codeEditorBDDSave').prop('disabled',false);
	
	$("#codeEditorRadios").find("label:first").addClass("active").siblings().removeClass("active");
	//document.getElementById("java-code").innerText = "";
	//document.getElementById("java-code-edat").innerText = "";
	//code='<tr><td>hai</td><td> welcome</td></tr>';
	//document.getElementById("java-code").innerText = code;
	console.log("editor"+editor);
	
	 if(editor!=""){
		 editor.doc.cm.setValue("");
		 editor.doc.cm.clearHistory();
		 //editor.getTextArea().innerText="";
		 //editor.getTextArea().innerText= code;
		 $('#java-code').innerText="";
		 $('#java-code').innerText=code;
		 if(jsonObj.code_testCase!=""){
			 editor.getDoc().setValue(code);
		 }
		
		 
	 }else{
		 
		 editor = CodeMirror.fromTextArea(document.getElementById("java-code"), {
			 value: code,
			 readOnly : false,
			 lineNumbers: true,
			lineWrapping: true,
		      mode: jsonObj.mode,
		      matchBrackets: true,
		      hintOptions: {container: $("#codeMirrorTextEditorBDD .modal-content")},
			  extraKeys: {"Ctrl-Space": "autocomplete"},
			  gutters: ["CodeMirror-lint-markers", "CodeMirror-foldgutter"],
			  lint: true,
			  foldGutter: true,
		      
		    });
		 editor.getDoc().setValue(code);
	 }	
	 
	 var totalLines = editor.lineCount();  
	 editor.autoFormatRange({line:0, ch:0}, {line:totalLines});
	 
	 $("#scriptBadgeLineNumber").text((editor.getDoc().cm.lastLine())+" LOC");
	 $("#scriptLocText").text((editor.getDoc().cm.lastLine())+" LOC");
	// autoFormatSelectionEditor();	
	 setTimeout(function() {editor.refresh();},1);
	 editor.scrollTo(0,0);
}


function codeEditorBDDFormatHandler(){
	var totalLines = editor.lineCount();  
	editor.autoFormatRange({line:0, ch:0}, {line:totalLines});
	
	$("#scriptBadgeLineNumber").text((editor.getDoc().cm.lastLine())+" LOC");
	$("#scriptLocText").text((editor.getDoc().cm.lastLine())+" LOC");	 
}

function displayCodeEDAT(jsonObj, subTitle, code){
	$("#codeEditorRadios").find("label:first").addClass("active").siblings().removeClass("active");
	document.getElementById("java-code").innerText = "";
	document.getElementById("java-code-edat").innerText = "";
	//code='<tr><td>hai</td><td> welcome</td></tr>';
	document.getElementById("java-code-edat").innerText = code;
	console.log("editorEDAT"+editorEDAT);
	 if(editorEDAT!=""){
		 editorEDAT.doc.cm.setValue("");
		 editorEDAT.doc.cm.clearHistory();
		 //editorEDAT.getTextArea().innerText="";
		 //editorEDAT.getTextArea().innerText= code;
		 $('#java-code-edat').innerText="";
		 $('#java-code-edat').innerText=code;
		 if(jsonObj.code_testCase!=""){
			 editorEDAT.getDoc().setValue(code);
		 }
		 
	 }else{		 
		 editorEDAT = CodeMirror.fromTextArea(document.getElementById("java-code-edat"), {
			 readOnly : false,
		      lineNumbers: true,
		      mode: jsonObj.mode,
		      //value: code,
		      matchBrackets: true,
		      autoRefresh: true
		 });
		 editorEDAT.doc.cm.setValue("");
		 editorEDAT.doc.cm.clearHistory();
		 $('#java-code-edat').innerText="";
		 $('#java-code-edat').innerText=code;
		 if(jsonObj.code_testCase!=""){
			 editorEDAT.getDoc().setValue(code);
		 }
	 }	
	 $("#scriptBadgeLineNumber").text((editorEDAT.getDoc().cm.lastLine())+" LOC");	 
	 //autoFormatSelectionEditorEDAT();	
}

	function autoFormatSelectionEditorEDAT() {
		var range = getSelectedRangeEditorEDAT();
		editorEDAT.autoFormatRange(range.from, range.to);
		editorEDAT.setCursor({line: 0, ch: 0});
  }
  
   function getSelectedRangeEditorEDAT() {
	  var  startObj={line:0, ch:0};
	  var lastline = editorEDAT.getLine(editorEDAT.getDoc().cm.lastLine()-1);
	  var endObj={line:editorEDAT.getDoc().cm.lastLine() ,ch: lastline.length};
	   
    //return { from: editor.getCursor(true), to: editor.getCursor(false) };
	  return { from: startObj, to: endObj };
  }
   
   function autoFormatSelectionEditor() {
		var range = getSelectedRangeEditor();
		editor.autoFormatRange(range.from, range.to);
		editor.setCursor({line: 0, ch: 0});
 }

  function getSelectedRangeEditor() {
	  var  startObj={line:0, ch:0};
	  var lastline = editor.getLine(editor.getDoc().cm.lastLine()-1);
	  var endObj={line:editor.getDoc().cm.lastLine() ,ch: lastline.length};
	   
   //return { from: editor.getCursor(true), to: editor.getCursor(false) };
	  return { from: startObj, to: endObj };
 }
  

function closeCodeMirrorTextEditor() {	
	$("#div_PopupBackground").fadeOut("normal");	
	$("#codeMirrorTextEditor").fadeOut("normal");
}

function downloadScriptHandler(){
	downloadGenerateScript(); // code in scriptPromtWindow file.
}

function closeDownloadScriptHandler(){
	$("#codeMirrorTextEditor button").trigger('click');
}

