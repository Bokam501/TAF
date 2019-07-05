var filter;
var executeScript='ExecuteScript';
var generateScriptSource='GenerateScriptSource';
var downloadPath;
var mainFileCode;
var scriptFileCode;
var identityFileCode;
var packageName;
var codeGenerationMode='';
var type = '';
var engine = '';
var mode = '';
$(document).ready(function(){	
	//setTestEngine();
});
$(document).on('change','#downloadScriptType_ul', function() {  
	scriptTypeUnique = $("#downloadScriptType_ul").find('option:selected').val();		
});

$(document).on('change','#downloadTestEngineType_ul', function() {
	testEngineUnique =  $("#downloadTestEngineType_ul").find('option:selected').val();	;
});

$(document).on('change','#downloadTestEngineType_ul', function() {
	testEngineUnique =  $("#downloadTestEngineType_ul").find('option:selected').val();	;
});


$(document).on('change','#downloadTestStepOption_ul', function() {
	testStepOption =  $("#downloadTestStepOption_ul").find('option:selected').val();		
});

$(document).on('change','#version_bdd_ul', function() {
	var versionValue= $("#version_bdd_ul").find('option:selected').attr('id');
	// ----- BDD Data ------			
	loadAttachments_Object(versionValue, '#objectrepository_bdd_ul');		
	loadAttachments_Data(versionValue, '#testDataRepository_bdd_ul');

});
$(document).on('change','#testTollMaster_bdd_multiple_ul', function() {
	if($('#codeMirrorTextEditorBDD').is(':visible')){
		testToolFromCodeEditor = $("#testTollMaster_bdd_ul").find('option:selected').val();

	}else if($('#codeMirrorTextEditorBDDEmbedded').is(':visible')){
		testToolFromCodeEditor = $("#testTollMaster_bddEmbedded_ul").find('option:selected').val();

	}else{
		testToolFromCodeEditor =  $("#testTollMaster_bdd_multiple_ul").find('option:selected').val();

	}
	setLanguages(testToolFromCodeEditor);
	setPackageAndScriptStructure(testToolFromCodeEditor);	
});

var repositoryValue=0;
var testToolName;
var productArr;
function setTestEngine(productType){
	$("#testTollMaster_bdd_ul").empty();
	$("#testTollMaster_bdd_multiple_ul").empty();
	$.post('getproducttype.list',function(data) {
		var ary = (data.Options);
		var arr= [];
		$.each(ary, function(index, element) {
			if(element.DisplayText == productType){
				productArr = [];
				productArr = (element.options1);
				productArr.sort();
				productArr.reverse();
				$.each(productArr, function(index, element){
					$("#testTollMaster_bdd_ul").append('<option id="' + element + '"  value="' + element + '" ><a href="#">' + element + '</a></option>');
					$("#testTollMaster_bdd_multiple_ul").append('<option id="' + element + '"  value="' + element + '" ><a href="#">' + element + '</a></option>');
				});
    			var testEngineFlag = false; 
				for(var i=0;i<productArr.length;i++){
    				if(testToolName == productArr[i]){
    					testEngineFlag = true;
							$("#testTollMaster_bdd_ul").select2('val', testToolName);
        				break;
    				}
				} 
				if(!testEngineFlag ){
					$("#testTollMaster_bdd_ul").select2('val',$("#testTollMaster_bdd_ul").find('option')[0].value);
				}
				
				$("#testTollMaster_bdd_multiple_ul").select2('val', testEngineFromDB);
				/*if(!$('#codeMirrorTextEditorBDD').is(':visible') && (testEngineFromDB=="" || testEngineFromDB == null || testEngineFromDB == undefined)){
					$("#testTollMaster_bdd_multiple_ul").select2('val',$("#testTollMaster_bdd_multiple_ul").find('option')[0].text);
					setLanguages($("#testTollMaster_bdd_multiple_ul").find('option')[0].text);
				}*/
			}
		});				
	});
}

function setPackageAndScriptStructure(engine){
	if(engine == "EDAT"){			
		$("#packageDiv").hide();
		$("#mainDiv").hide();
	}else if(engine == "SELENIUM" || engine == "APPIUM" || engine == "SEETEST"){
		$("#packageDiv").show();
		$("#mainDiv").show();
	}else if(engine == "PROTRACTOR" || engine == "CODEDUI" || engine == "TESTCOMPLETE"){
		$("#packageDiv").hide();
		$("#mainDiv").hide();
	}else{
		$("#packageDiv").show();
		$("#mainDiv").show();
	}
}

function returnNodata(listObjectId, ary){
	if(ary.length <=0){
		$(listObjectId).append('<option id="-1" selected value="NoData">NoData</option>');		  
	}

}
function displayDownloadTestScriptsFromTestCases(id, scriptFor, type){		
	$("#divPopUpDownloadTestScriptsFromTestCases h4").text("");
	$("#divPopUpDownloadTestScriptsFromTestCases h4").text("Specify Parameters");
	$("#divPopUpDownloadTestScriptsFromTestCases").modal();
	idUnique = id;
	scriptsForUnique = scriptFor;
	scriptExecutionType = type;

	scriptTypeUnique = $("#downloadScriptType_ul").find('option:selected').val();	
	testEngineUnique =  $("#downloadTestEngineType_ul").find('option:selected').val();
	testStepOption =  $("#downloadTestStepOption_ul").find('option:selected').val();

	displayScriptBtnStatus();
}

function displayBDDScripts(id, scriptTypeUnique, testEngineUnique,prodTypeName){
	//loadVersions('#version_bdd_ul');
	//loadTestToolMaster('#testTollMaster_bdd_ul');
	/*loadAttachments_Object(projectCodeId, '#objectrepository_bdd_ul',2);	
	loadAttachments_Object(projectCodeId, '#testDataRepository_bdd_ul',1);*/

	loadAttachments_Object(productId, '#objectrepository_bdd_ul',2);	
	loadAttachments_Object(productId, '#testDataRepository_bdd_ul',1);


	$("#divPopUpBDDTestScripts h4").text("");
	$("#divPopUpBDDTestScripts h4").text("Specify Parameters");
	$("#divPopUpBDDTestScripts").modal();

	idUnique = id;
	productTypeName=prodTypeName;


	displayScriptBtnStatus();
}

function displayScriptBtnStatus(){
	if(scriptExecutionType == scriptViewName){
		$("#scriptBtn").removeClass("btn blue");		
		$("#scriptBtn").addClass("btn green haze");
		$("#scriptBtn").val("Confirm");

	}else{ // type == scriptDownloadName
		$("#scriptBtn").removeClass("btn green haze");
		$("#scriptBtn").addClass("btn blue");
		$("#scriptBtn").val("Download");
	} 

	$("#scriptBtn").click(executeScriptBtn);
}

function executeScriptBtn(){
	$("#scriptBtn").unbind( "click" );
	if(scriptExecutionType == scriptViewName){
		displayTestScriptsFromTestCases();
	}else{
		downloadTestCaseScript();
	}
}


function executeScriptBDDBtn(){
	var prodVersionOption = $("#version_bdd_ul").find('option:selected').val();
	var productVersionOptionName = $("#version_bdd_ul").find('option:selected').text();
	/*var testEngineOption = $("#testTollMaster_bdd_ul").find('option:selected').val();*/	
	var testEngineOption =$("#testTollMaster_bdd_ul").find('option:selected').text();
	var objectRepOption	 =  $("#objectrepository_bdd_ul").find('option:selected').val();
	var testDataOption =  $("#testDataRepository_bdd_ul").find('option:selected').val();

	if(testDataOption == 'NoData' || testDataOption == undefined){
		testDataOption=-1;
	}
	if(objectRepOption == 'NoData' || objectRepOption == undefined){
		objectRepOption=-1;
	}
	testToolName=testEngineOption;
	if(testEngineOption == 'NoData' || testEngineOption == undefined ){
		testToolName=null;
	}
	if(productVersionOptionName == 'NoData'){
		productVersionOptionName=null;
	}
	testDataId=testDataOption;
	objectRepositoryId= objectRepOption;
	// alert("productVersionOption="+productVersionOption+"testEngineOption=>"+testEngineOption+"objectRepOption="+objectRepOption+"testDataOption=>"+testDataOption+"testToolId==>"+testToolId);
	if(scriptsFor == "TestCase"){
		saveAtsgParameters(idUnique,testToolName,objectRepOption,testDataOption,prodVersionOption);
		displayBDDTestScriptsFromTestCases(idUnique, "BDD",testToolName,productTypeName,objectRepOption,testDataOption);	
	}else if(scriptsFor == "TestSuite"){
		generateScriptSouce();
	}

}

function downloadTestCaseScript(){
	console.log(idUnique," --- ",scriptsForUnique);	
	console.log(scriptTypeUnique," --- ",testEngineUnique," ----- ",testStepOption);
	location.href="product.testscripts.download?id="+idUnique+"&scriptsFor="+scriptsForUnique+"&scriptType=BDD&testEngine="+testEngineUnique+"&testStepOptions="+testStepOption;
	//location.href="product.testscripts.download?id="+idUnique+"&scriptsFor="+scriptsForUnique+"&scriptType="+scriptTypeUnique+"&testEngine="+testEngineUnique+"&testStepOptions="+testStepOption;
	popUpDownloadTestScriptsclose();
}

function displayTestScriptsFromTestCases(){
	var jsonObj="";
	var code_testCaseClassName,code_testCaseScript,code_mainClassName,code_mainClassScript = "";

	scriptTypeUnique = $("#downloadScriptType_ul").find('option:selected').val();	
	testEngineUnique =  $("#downloadTestEngineType_ul").find('option:selected').val();
	testStepOption =  $("#downloadTestStepOption_ul").find('option:selected').val();

	var downloadURL;
	$("#codeEditorRadios label ").eq(0).empty();
	$("#codeEditorRadios label ").eq(1).empty();
	if(scriptsFor == scriptTestCaseName){
		downloadURL = "product.testcase.script.view";
		/*$("#codeEditorRadios  label").eq(0).text("Test Case123");*/

		$("#codeEditorRadios  label").eq(0).append("<input type='radio' class='toggle'  >Test Case");
		$("#codeEditorRadios  label").eq(1).append("<input type='radio' class='toggle'  >Main");

	}else{
		downloadURL = "product.testsuite.script.ref.view";
		$("#codeEditorRadios  label").eq(0).append("<input type='radio' class='toggle'  >Test Cases Ref");
		$("#codeEditorRadios  label").eq(1).append("<input type='radio' class='toggle'  >Main");
	}

	var url= downloadURL+"?id="+idUnique+"&scriptsFor="+scriptsFor+"&scriptType="+scriptTypeUnique+"&testEngine="+testEngineUnique+"&testStepOptions="+testStepOption;
	$.ajax({
		type: "POST",
		url: url,
		dataType: "json", // expected return value type		    
		success: function(data) {		    	
			//alert("success");
		},
		error: function(data) {
			//alert("error");
		},
		complete: function(data){		    		
			downloadPath=data.responseJSON.Records[0].downloadPath;
			if(scriptsFor == "TestCase"){
				if(data.responseJSON.Result=="ERROR"){
					code="";
				}else{

					if(scriptsForUnique == scriptTestCaseName){
						code_testCaseClassName = data.responseJSON.Records[0].testCaseClassName;		    			
						code_testCaseScript = data.responseJSON.Records[0].testCaseScript;
					}else{
						code_testCaseClassName = data.responseJSON.Records[0].testCaseRefClassName;		    			
						code_testCaseScript = data.responseJSON.Records[0].testCaseRefClassScript;
					}

					code_mainClassName = data.responseJSON.Records[0].code_mainClassName;
					code_mainClassScript = data.responseJSON.Records[0].mainClassScript;
				}	     

				jsonObj={"Title":"Code: ",
						"SubTitle": "Test Case ID : "+idUnique+" - Language: "+scriptTypeUnique+" - Test Engine : "+testToolName, 
						"data":data,
						"code_RadioBtn1Name":"TestCase ID : "+idUnique+" - Language: "+scriptTypeUnique+" - Test Engine : "+testToolName,
						"code_RadioBtn1":code_testCaseScript,
						"code_RadioBtn2Name":"TestCase ID : "+idUnique+" - Language: "+scriptTypeUnique+" - Test Engine : "+testToolName,
						"code_RadioBtn2":code_mainClassScript,
						"mode":"text/x-java",
				};

				CodeEditor.init(jsonObj);

				//$("#codeMirrorTextEditor .radio-toolbar").show();
				$("#scriptBadgeLineNumber").hide();
				/*$("#codeEditorBottomConatiner").hide();*/
				$("#codeEditorBottomConatiner").show();
			}else{
				downloadGenerateScript();
			}
		}
	});    	
}

function popUpDownloadTestScriptsclose() {	
	$("#divPopUpDownloadTestScriptsFromTestCases").modal("hide");	
}

function popUpDownloadTestScriptsBDDclose() {	
	$("#divPopUpBDDTestScripts").modal("hide");
}

var testCaseIdList=[];
function generateScriptSouceHandler(){	
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
	console.log("testToolName : "+testToolName);
	scriptClosePopupFlag = false;
	//editStoryFalg = false;
	$("#codeEditorBottomConatiner").show();	
	$("#atsgLoaderIcon").show();  

	codeGenerationMode = $("#radioHandlerBtn .checked input").attr('id');

	testMethodSingleInBDD = $("#radioHandlerBtn1 .checked input").attr('id');
	packageName = $("#package").val();

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
	}else{
		versionId = $("#testTollMaster_bdd_version_ul_EDAT").val();
		if(versionId == null)versionId = versionSelectedValEDAT;
	}
	if(type == "MultipleTestCases"){
		versionId = -1;
	}

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
				//downloadGenerateScript();
			}

			if(type == 'SingleTestCase'){
				//identityFileCode =	data.responseJSON.Records[0].identityFileCode;
				scriptFileCode = data.responseJSON.Records[0].scriptFileCode;
				mainFileCode =	data.responseJSON.Records[0].mainFileCode;
				code_testCaseClassName = data.responseJSON.Records[0].testCaseClassName;		    			
				code_testCaseScript = data.responseJSON.Records[0].testCaseScript;
				/*scriptFileLineofCode=data.responseJSON.Records[0].scriptFileLineofCode;	*/                  

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
					if(scriptLanguage=="PYTHON"){
						jsonObj.mode='text/x-python';
					}else if(scriptLanguage=="POWERSHELL"){
						jsonObj.mode='text/x-powershell';
					}
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
			}else{

				downloadGenerateScript();
			}		    	
			$('#generationModeDD_ul').select2('val',codeGenerationMode);			
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
			}else{
				$('#java-code').css('display','none');
				$('.CodeMirror.cm-s-default').removeClass('hidden');
				$("#codeMirrorTextEditorGeneratedScriptEDAT").css('z-index',100052);
				$("#codeMirrorTextEditorGeneratedScriptEDAT").removeClass('hidden');		      
				$("#codeBDDEmbeddedContainer").hide();
				$('.testStoryTabEDAT').removeClass('active');
				$('.generatedScriptTabEDAT').addClass('active');
			}
			closeGenerateScriptHandler();
			scriptClosePopupFlag = true;	        
		}
	});
}

function saveGeneratedScript(testCaseStoryId, scriptFileCode, testToolName, scriptLanguage, downloadPath,codeGenerationMode){
	$.ajax({
		type: "POST",
		data: {'script':scriptFileCode,'testCaseStoryId':testCaseStoryId,'testEngine':testToolName,'languageName':scriptLanguage,'downloadPath':downloadPath,'codeGenerationMode':codeGenerationMode},
		url:"generatedscript.save",
		dataType : 'json',
		success : function(data) {		
			if(data.Result=="ERROR"){

			}else{
				jsonObj={"Title":"Script Code: ",
						"SubTitle": "Test Case ID : "+idUnique+" - Language: "+scriptLanguage+" - Test Engine : "+testToolName, 
						"data":"",
						"code_RadioBtn1Name":"Test Case ID : "+idUnique+" - Language: "+scriptLanguage+" - Test Engine : "+testToolName,
						"code_RadioBtn1":scriptFileCode,
						"code_RadioBtn2Name":"Test Case ID : "+idUnique+" - Language: "+scriptLanguage+" - Test Engine : "+testToolName,
						"code_RadioBtn2":mainFileCode,
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
		}
	});
}

function submitRadioBtnHandler(){
	if(  $("#package").val() == ""){
		alert("Please enter package name");
		return false;
	}

	$("#generateScriptLoaderIcon").show();
	$('#submitButton').prop('disabled', true);
	scriptClosePopupFlag = false;
	/* displayBDDTestScriptsFromTestCases(idUnique, "BDD",testToolName,productTypeName,objectRepOption,testDataOption);	*/

	if( $("#package").val() != ""){
		scriptLanguage = $("#scriptlanguage_ul").find('option:selected').text();
		if(scriptLanguage == "JAVASCRIPT" && testToolName == "PROTRACTOR"){				 
			generateScriptSouceHandler();		 
		}else if(scriptLanguage != "JAVASCRIPT" && testToolName == "PROTRACTOR"){
			callAlert("Please select JAVASCRIPT as the language to the Test Engine : "+testToolName);
			$("#generateScriptLoaderIcon").hide();
			$('#submitButton').prop('disabled', false);
		} else if(testToolName != "PROTRACTOR"){				 
			generateScriptSouceHandler();
		}
	}
}

$(document).on('change', '#scriptlanguage_ul', function() {
	scriptLanguage = $("#scriptlanguage_ul").find('option:selected').text();
});
var testEngineFromDB='';
var testToolFromCodeEditor = '';
var scriptLanguageSelectedVal = '';
function generateScriptSouce(){
	//browsers='chrome';
	//codeGenerationMode='TAF';	
	testEngineFromDB='';
	testEngineFromDB=testEngineArr[0];
	if($('#codeMirrorTextEditorBDD').is(':visible')){
		testToolFromCodeEditor = $("#testTollMaster_bdd_ul").find('option:selected').val();
	}
	else if($('#codeMirrorTextEditorBDDEmbedded').is(':visible')){
		testToolFromCodeEditor = $("#testTollMaster_bddEmbedded_ul").find('option:selected').val();
	}	
	else{
		$("#testTollMaster_bdd_multiple_ul").select2('val', testEngineFromDB);
		testToolFromCodeEditor =  $("#testTollMaster_bdd_multiple_ul").find('option:selected').val();
		if(testToolFromCodeEditor == undefined)
			testToolFromCodeEditor = testEngineFromDB;
	}
	if(testToolName != "EDAT"){
		scriptLanguageSelectedVal = $("#testTollMaster_bdd_scriptLanguage_ul").find('option:selected').val();
	}else{
		scriptLanguageSelectedVal = $("#testTollMaster_bdd_scriptLanguage_ul_EDAT").find('option:selected').val();
	}
	setLanguages(testToolFromCodeEditor);
	setPackageAndScriptStructure(testToolFromCodeEditor);

	if($('#codeMirrorTextEditorBDD').is(':visible')){
		$('.testTollMaster_bdd_multiple_testcase_container').addClass('hidden');   	

	}else if($('#codeMirrorTextEditorBDDEmbedded').is(':visible')){
		$('.testTollMaster_bdd_multiple_testcase_container').addClass('hidden');		
	}	

/*	if(editStoryFalg){
		codeEditorBDDSaveHandler();
		editStoryFalg = false;
	}

	if(editStoryEmbeddedFalg){
		codeEditorBDDEmbeddedSaveHandler();
		editStoryEmbeddedFalg = false;
	}*/

	testMethodSingleInBDD = $("#radioHandlerBtn1 .checked input").attr('id');

	$("#radioHandlerBtn input").on('click', function(){
		mode = $(this).attr('id');
		if(mode=="TAF-MODE"){
			$("#package").val('com.hcl.taf');
			}else {
			$("#package").val('com.hcl.ers.atsg.script.output');
			}
	});

	packageName = $("#package").val();

	if(testMethodSingleInBDD == 'TEST_METHOD_SINGLE_STORY'){
		testMethodSingleInBDD = 'TEST_METHOD_SINGLE';
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

	$("#div_generateScript").modal();

	$("#div_generateScript .modal-title").text("Specify Parameters");	
	if (ATTModeEnabled) {
		$("#radioHandlerBtn label")[0].style.display = 'none';
		$("#radioHandlerBtn label")[1].style.display = 'none';
		$("#radioHandlerBtn label")[2].style.display = 'block' ;
		$("#uniform-TAF-MODE span").removeClass('checked');
		$("#uniform-ATT-MODE span").addClass('checked');

		if(testToolName == 'PROTRACTOR' || testToolName == 'APPIUM' || testToolName == 'SEETEST'){
			$("#radioHandlerBtn label")[2].style.display = 'none' ;
			if(testToolName == 'PROTRACTOR'){				 				 
				$('#package').closest('div').hide();
			} else {				 
				$('#package').closest('div').show();
			}
			$('#mainDiv').hide();			 
			$("#radioHandlerBtn label")[1].style.display = 'block' ;
			$("#radioHandlerBtn span").removeClass('checked');
			$("#radioHandlerBtn span").eq(1).addClass('checked');
			// 0 - TAF Mode, 1 - Generic Mode and 2 - ATT-Mode.		 
		} else{
			$('#package').closest('div').show();			 	
		}
		hideOptions(1);
	} else {
		$("#radioHandlerBtn label")[0].style.display = 'block';
		$("#radioHandlerBtn label")[1].style.display = 'block' ;
		$("#radioHandlerBtn label")[2].style.display = 'none' ;
	}

	codeGenerationMode = $("#radioHandlerBtn .checked input").attr('id');

	//testToolName = $("#testTollMaster_bdd_ul").find('option:selected').val();
	if (!ATTModeEnabled) {
		if(engine == 'PROTRACTOR'){			 		
			$('#mainDiv').hide();
			$('#package').closest('div').hide();		 
			$("#radioHandlerBtn span").removeClass('checked');
			$("#radioHandlerBtn span").eq(1).addClass('checked');
			// 0 - TAF Mode, 1 - Generic Mode and 2 - ATT-Mode.		 
		}else {			 		
			$('#mainDiv').show();
			$('#package').closest('div').show();
		}
	}

	scriptLanguage = $("#scriptlanguage_ul").find('option:selected').text();	

	// ---  language types to be selected -----
	var selectedData = $("#testTollMaster_bddEmbedded_ul").find('option:selected').val();
	if($('#codeMirrorTextEditorBDDEmbedded').is(':visible') && selectedData == "EDAT"){		 
		$("#packageDiv").hide();
		$("#mainDiv").hide();

		$("#uniform-GENERIC-MODE span").addClass('checked');			 

	}else{	 
		selectedData = $("#testTollMaster_bdd_ul").find('option:selected').val();
		if(selectedData == "SELENIUM" || selectedData == "APPIUM" || selectedData == "SEETEST"){					 
			$("#packageDiv").show();
			$("#mainDiv").show();

		}else{			
			$("#packageDiv").hide();
			$("#mainDiv").hide();
		}
		if(testToolFromCodeEditor == "SELENIUM"){
			$('.tafMode').removeClass('hidden');

		}else {
			$('.tafMode').addClass('hidden');
			$("#package").val('com.hcl.ers.atsg.script.output');
		}

	}
	// ------
	setPackageAndScriptStructure(testToolFromCodeEditor);
	if(!$('#codeMirrorTextEditorBDD').is(':visible')){
		$("#testTollMaster_bdd_multiple_ul").select2('val', testEngineFromDB);
	}
	setTestEngine(productType);
	//scriptLanguageSelectedVal="";
}

function downloadGenerateScript(){
	var languageName = "";
	if(testToolName=="EDAT"){
		editor = editorEDAT;
		languageName = $("#testTollMaster_bdd_scriptLanguage_ul_EDAT").find('option:selected').text();
	} else {
		languageName = $("#testTollMaster_bdd_scriptLanguage_dd").find('option:selected').text();
	}
	
	if(editor != "" && (idUnique != null || idUnique != undefined) ){	
		 
		var codeGenerationMode = $("#generationMode_dd").find('option:selected').text();
		$.ajax({
			type: "POST",
			url:"generatedscript.download?testCaseStoryId="+storyId+'&languageName=' + languageName+'&testEngine=' + testToolName+'&codeGenerationMode='+codeGenerationMode,
			contentType: "application/json; charset=utf-8",
			dataType : 'json',
			success: function(data) {  
				if(data.Result == "ERROR"){
					callAlert("Script is not available to download!");
					return false;
				}else if(data.Result == "OK"){
					downloadPath=data.Record.downloadPath;
					
					if(downloadPath == undefined){
						callAlert("Script is not generated in your file system to download!");
						return false;
					}					
					var url=downloadPath;
					var urlfinal="rest/download/testScript?fileName="+url;
					parent.window.location.href=urlfinal;
				}
			},
			error: function(data){
				console.log("Error");		   
			}
		});	
		
	}else if(testCaseIdArr.length > 0 ){
		if(downloadPath == undefined){
			callAlert("script is not generated in your file system to download!");
			return false;
		}
		var url=downloadPath+".zip";
		var urlfinal="rest/download/testScript?fileName="+url;
		parent.window.location.href=urlfinal;
	}
	else{
		callAlert("script is not available to download ..!");
		return false;
	}
}

function saveAtsgParameters(idUnique,testToolName,objectRepOption,testDataOption,prodVersionOption){
	var url='update.orsave.atsg.parameteres';
	$.ajax({
		type: "POST",
		url: url,
		dataType: 'json',
		data: { 'testCaseId': idUnique, 'testToolName': testToolName,'objectRepositoryId':objectRepOption,'testDataId':testDataOption,'prodVersionId':prodVersionOption,'atsgParameterId':atsgId},
		success: function(data) {  

			if(data.Result == "ERROR"){
				//jAlert(data.Message, "ok");
				//callAlert(data.Message);
			}else if(data.Result == "OK"){
				//callAlert(data.Message);
			}

		},
		error: function(data){
			console.log("Error");		   
		},
		complete: function(data) {

		}
	});	
}