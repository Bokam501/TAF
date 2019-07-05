
//integration of ilcm atsg editor starts
var isEATSG=false;
var storyVersionData;
var storyVersionDataFull;
var storyId;
var productTypeId;
var initialStoryData;
var isScmsystemAvailable;
function displayATSGPopupHandler(value) {
	openLoaderIcon();
	isEATSG=false;
	totalRepository = 0;
	totalTestData = 0;
	editStoryFalg = false;			
	var arr = value.split('~');
	testCaseId = arr[0];
	testCaseName = arr[1];
	idUnique = testCaseId;
	parameterType = 'ADD';
	//bddScriptHandler(parameterType);
	setTimeout(function(){bddScriptHandler(parameterType)},1000);
}

function display_EATSG_PopupHandler(value){
	openLoaderIcon();
	isEATSG=true;
	totalRepository = 0;
	totalTestData = 0;			
	editStoryEmbeddedFalg=false;
	var arr = value.split('~');
	testCaseId = arr[0];
	testCaseName = arr[1];
	idUnique = testCaseId;
	parameterType = 'ADD';
	bddScriptHandler(parameterType);
}
function bddScriptHandler(parameterType){
	//closeATSGContainer();
	//getTestEngine();
	if(scriptsFor == "TestCase"){
		if(parameterType == 'ADD'){
			var url = "get.atsg.parameters?testCaseId="+idUnique;
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
					if(data.responseJSON.Result=="ERROR"){
						code="";
					}else{
						if(data.responseJSON.TotalRecordCount == 0){
							atsgId = -1;	                		

							$("#testTollMaster_bdd_ul").select2("val",$("#testTollMaster_bdd_ul").find('option')[0].value);
							testToolName = $("#testTollMaster_bdd_ul").find('option:selected').text();
							if(isEATSG){
								testToolName='EDAT';
								$("#testTollMaster_bddEmbedded_ul").select2('val', testToolName);
							}
							saveAtsgParameters(idUnique,testToolName,null,null,null);
							displayBDDTestScriptsFromTestCases(idUnique, "BDD",testToolName,-1,-1,-1);

						}else{
							
							data.responseJSON.Records[0].testEngine = (data.responseJSON.Records[0].testEngine).toLocaleUpperCase();
							
							atsgId = data.responseJSON.Records[0].atsgId;
							/*if(data.responseJSON.Records[0].testEngine == "EDAT"){
	                			 data.responseJSON.Records[0].testEngine = "SELENIUM";
	                		 }*/	                		 
							if(!isEATSG){
								testToolName = data.responseJSON.Records[0].testEngine;
								//testToolName = $("#testTollMaster_bdd_ul").find('option:selected').text()
								//$("#testTollMaster_bdd_ul").select2('val', testToolName);
								
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
							}else{
								testToolName='EDAT';
								$("#testTollMaster_bddEmbedded_ul").select2('val', testToolName);
							}
							displayBDDTestScriptsFromTestCases(idUnique, "BDD",testToolName,-1,-1,-1);
						}
					}			    
				}
			});  
		}
		if(parameterType == 'EDIT'){
			//displayBDDScripts(jsonObjMain.testCaseId,"BDD","",jsonObjMain.productTypeName); 
			saveAtsgParameters(idUnique,testToolName,-1,-1,-1);
			displayBDDTestScriptsFromTestCases(idUnique, "GHERKIN",testToolName,-1,-1,-1);	
		}
	}else if(scriptsFor == "TestSuite"){
		displayBDDScripts(jsonObjMain.testCaseId,"BDD","",jsonObjMain.productTypeName);
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
function displayBDDTestScriptsFromTestCases(idUnique, scriptTypeUnique, testEngineUnique,productTypeName,objectRepOption,testDataOption){
	/*	var productName= document.getElementById("hdnProductName").value ;*/
	var saveAsNewFlag = true;
	//commented as it is calling before creation of story
	///setTimeout(function(){setStoryVersions(testCaseId, testEngineUnique, saveAsNewFlag);},500);
	$("#atsgLoaderIcon").show();
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : 'product.testcase.automationscript.get?testCaseId='+idUnique+'&scriptType='+scriptTypeUnique+'&testEngine='+testEngineUnique+'&productTypeName='+productTypeName+'&objectRepOption='+objectRepOption+'&testDataOption='+testDataOption+'&productId='+productId,
		dataType : 'json',
		success : function(data) {
			if(data.Result=="ERROR"){
				callAlert(data.Message);
				$("#atsgLoaderIcon").hide();
				return false;
			}else{
				if (data.Result == "OK") {
					setStoryVersions(testCaseId, testEngineUnique, saveAsNewFlag);
					$("#atsgLoaderIcon").hide();
					initialStoryData = data;
					if(/* testToolName == "EDAT" && productType == "Embedded" &&  */atsgBtn == "eATSG"){
						displayBDDEmbeddedScriptEditor(data);
					}else{
						$("#trigUploadEditorFile").hide();
						displayBDDScriptEditor(data);								
					}
					storyId = data.Records[0].testCaseAutomationScriptId;
					isScmsystemAvailable = data.Records[0].isSCMSystemAvaialble;
				}
			}
			$("#scriptLocText").text(" ( 0 Lines) ");
			$("#scriptLocTextEDAT").text(" ( 0 Lines) ");
			setScriptLanguageDD(testToolName);
		}
	});	
}
function displayBDDScriptEditor(data){
	var language="BDD";
	if(data.Records[0].scriptType != null){
		language=data.Records[0].scriptType;
	}
	var jsonObj={"Title":"BDD Test Case Script Gherkin Story : "+data.Records[0].testCaseClassName,
			"subTitle": "Testcase ID : "+data.Records[0].testCaseId+" - Testcase Name: "+data.Records[0].testCaseName+" - Test Engine : ", 
			"data": data,		 		 
			"BDDscript": data.Records[0].testCaseScript,
			"mode":"gherkin",
			gutters: ["CodeMirror-lint-markers", "CodeMirror-foldgutter"],
			lint: true,
			save: "product.testcase.automationscript.save",
			execute:"workpackage.execute.singletestcase",
			foldGutter: true
	};
	CodeEditorForBDDScript.init(jsonObj);
}

function displayBDDEmbeddedScriptEditor(data) {		
	var language = "BDD";
	if (data.Records[0].scriptType != null) {
		language = data.Records[0].scriptType;
	}
	var jsonObj = {
		"Title" : "eDAT Story Editor ",
		//"subHeadingTitle" : "Testcase Name : " + testCaseName,				
		"subTitle" : "Testcase ID : " + data.Records[0].testCaseId
				+ " - Testcase Name : " + data.Records[0].testCaseName 
				+ " - Test Engine : ",
		"data" : data,
		"BDDscript" : data.Records[0].testCaseScript,
		"mode" : "gherkin",
		gutters : [ "CodeMirror-lint-markers", "CodeMirror-foldgutter" ],
		lint : true,
		save : "product.testcase.automationscript.save",
		execute : "workpackage.execute.singletestcase",
		foldGutter : true
	};
	codeEditorForBDDScriptEmbedded.init(jsonObj);
}
var currVerId;
function setStoryVersions(testCaseId, testEngineUnique, saveAsNewFlag){
	if(testToolName != "EDAT"){
		$("#testTollMaster_bdd_version_ul").empty();
	}else {
		$("#testTollMaster_bdd_version_ul_EDAT").empty();
	}
	$.ajax({
		type: "POST",
		url:"testcase.versions.list?testCaseId="+testCaseId+'&scriptType=' + scriptTypeUnique
		+ '&testEngine=' + testEngineUnique
		+ '&productId=' + productId,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {		
			if(data.Result=="ERROR"){

			}else{
				//data = data.Records;
				storyVersionDataFull = data;
				storyVersionData = data.Records;
				var arr= [];
				var versionId = 1;
				if(testToolName != "EDAT"){
					$.each(storyVersionData, function(index, element) {
						$("#testTollMaster_bdd_version_ul").append('<option id="' + element.versionId + '"  value="' + element.versionId + '" ><a href="#">' + element.versionId + '</a></option>');
						versionId = element.versionId;
						storyId = element.testCaseAutomationScriptId;
						productTypeId = element.productTypeId;
					});
				}else{
					$.each(storyVersionData, function(index, element) {
						$("#testTollMaster_bdd_version_ul_EDAT").append('<option id="' + element.versionId + '"  value="' + element.versionId + '" ><a href="#">' + element.versionId + '</a></option>');
						versionId = element.versionId;
						storyId = element.testCaseAutomationScriptId;
						productTypeId = element.productTypeId;
					});	
				}
				if(saveAsNewFlag){
					if(testToolName != "EDAT"){
						currVerId = versionId;
						$("#testTollMaster_bdd_version_ul").select2('val', versionId);
					}else{
						versionSelectedValEDAT = versionId;
						$("#testTollMaster_bdd_version_ul_EDAT").select2('val', versionId);
					}
				}else{
					if(versionSelectedVal == undefined){
						$("#testTollMaster_bdd_version_ul").select2('val', $("#testTollMaster_bdd_version_ul").find('option:selected').val());
						$("#testTollMaster_bdd_version_ul_EDAT").select2('val', $("#testTollMaster_bdd_version_ul_EDAT").find('option:selected').val());
					}else{
						$("#testTollMaster_bdd_version_ul").select2('val', versionSelectedVal);
						$("#testTollMaster_bdd_version_ul_EDAT").select2('val', versionSelectedVal);
					}
				}
			}
		},
		error : function(data) {
			//
		},
		complete: function(data){
			//closeLoaderIcon();
			//$("#scriptLocText").text(" ( 0 Lines) ");
			//$("#scriptLocTextEDAT").text(" ( 0 Lines) ");
		}
	});
}
function setScriptLanguageDD(testToolName){
	if(testToolName != "EDAT"){
		$("#testTollMaster_bdd_scriptLanguage_ul").empty();
	}else{
		$("#testTollMaster_bdd_scriptLanguage_ul_EDAT").empty();
	}
	$.post('getlanguage.list',function(data) {
		var ary = data.Options;
		var arr= [];
		$.each(ary, function(index, element) {
			arr = element.options1;
			if(testToolName == element.DisplayText){
				$.each( arr, function( key, value ) {
					if(testToolName != "EDAT"){
						$("#testTollMaster_bdd_scriptLanguage_ul").append('<option id="' + value + '"  value="' + value + '" ><a href="#">' + value + '</a></option>');
					}else{
						$("#testTollMaster_bdd_scriptLanguage_ul_EDAT").append('<option id="' + value + '"  value="' + value + '" ><a href="#">' + value + '</a></option>');
					}
				});
				if(testToolName != "EDAT"){
					$("#testTollMaster_bdd_scriptLanguage_ul").select2();
				}else {
					$("#testTollMaster_bdd_scriptLanguage_ul_EDAT").select2();
				}
			}
		});
	});
}
var val;
$("#testTollMaster_bdd_version_ul").on('change', function(){
	val = parseInt($('#testTollMaster_bdd_version_ul').val());
	$.ajax({
		type: "POST",
		url:"testcase.versions.list?testCaseId="+testCaseId+'&scriptType=' + scriptTypeUnique
		+ '&testEngine=' + testEngineUnique
		+ '&productId=' + productId,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {		
			if(data.Result=="ERROR"){

			}else{
				$.each(data.Records, function(index, element) {
					if(element.versionId==val){
						val = parseInt(val)-1;
						storyId = data.Records[val].testCaseAutomationScriptId;
						var jsonObj = {
								"Title" : "BDD Test Case  Story : " + testCaseName,				
								"subTitle" : "Testcase ID : " + data.Records[val].testCaseId
								+ " - Testcase Name: "+ data.Records[val].testCaseName,
								"data" : data,
								"BDDscript" : data.Records[val].testCaseScript,
								"mode" : "gherkin",
								gutters : [ "CodeMirror-lint-markers", "CodeMirror-foldgutter" ],
								lint : true,
								save : "product.testcase.automationscript.save",
								execute : "workpackage.execute.singletestcase",
								foldGutter : true
						};
						CodeEditorForBDDScript.init(jsonObj);	
						$("#scriptLocText").text(" ( 0 Lines) ");
						$("#scriptLocTextEDAT").text(" ( 0 Lines) ");
						//break;
					}
				});	
			}
		}
	});
});

var valEDAT;
$("#testTollMaster_bdd_version_ul_EDAT").on('change', function(){
	valEDAT = parseInt($('#testTollMaster_bdd_version_ul_EDAT').val());
	$.ajax({
		type: "POST",
		url:"testcase.versions.list?testCaseId="+testCaseId+'&scriptType=' + scriptTypeUnique
		+ '&testEngine=' + testEngineUnique
		+ '&productId=' + productId,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {		
			if(data.Result=="ERROR"){

			}else{
				$.each(data.Records, function(index, element) {
					if(element.versionId==valEDAT){
						valEDAT = parseInt(valEDAT)-1;
						storyId = data.Records[valEDAT].testCaseAutomationScriptId;
						var jsonObj = {
								"Title" : "eDAT Story Editor ",			
								"subTitle" : "Testcase ID : " + data.Records[valEDAT].testCaseId
								+ " - Testcase Name :  "+data.Records[valEDAT].testCaseName+"  - Test Engine : ",
								//"subHeadingTitle" : "TestCase Story Name : " + testCaseName,		
								"data" : data,
								"BDDscript" : data.Records[valEDAT].testCaseScript,
								"mode" : "gherkin",
								gutters : [ "CodeMirror-lint-markers", "CodeMirror-foldgutter" ],
								lint : true,
								save : "product.testcase.automationscript.save",
								execute : "workpackage.execute.singletestcase",
								foldGutter : true
						};
						codeEditorForBDDScriptEmbedded.init(jsonObj);			
						//break;
					}
				});
			}
		}
	});
});

$("#testTollMaster_bdd_scriptLanguage_ul_EDAT").on('change', function(){
	generatedScriptContentEDAT();
});

$("#testTollMaster_bdd_scriptLanguage_ul").on('change', function(){
	generatedScriptContent();
});