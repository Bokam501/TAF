// ATSG Parameters


var jsonObjMain='';

var ATSGComponent = function(){	
	var initialise = function(jsonObj){
		initialiseValue(jsonObj);
	};	
		return {
			init : function(jsonObj){
				initialise(jsonObj);
			}
		};
}();


function initialiseValue(jsonObj){
	jsonObjMain =jsonObj;
	displayATSG(jsonObj);
	//bddScriptHandler("ADD");
}

function displayATSG(jsonObj){
	$("#atsgContainer h4").text("");
	$("#atsgContainer h4").text(jsonObj.Title);
	$("#atsgContainer h5").text("");
	$("#atsgContainer h5").text(jsonObj.subTitle);	
	
	if($("#codeEditorRadiosBDD label").hasClass('active')){
		$("#codeEditorRadiosBDD label").removeClass('active');
	}	
	$("#atsgContainer").modal();
}

function boilerPlateCodeHandler(){
	closeATSGContainer();
	displayDownloadTestScriptsFromTestCases(jsonObjMain.testCaseId, scriptTestCaseName, scriptViewName);
}

//commented for atsg-ilcm editor integration

/*function bddScriptHandler(parameterType){
	//closeATSGContainer();
	$('#atsgContainer').modal('hide');
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
                		//displayBDDScripts(jsonObjMain.testCaseId,"GHERKIN","",jsonObjMain.productTypeName);
                		// testToolName ="SELENIUM";
                		 testToolName = $("#testTollMaster_bdd_ul").find('option:selected').text();	
                		 displayBDDTestScriptsFromTestCases(idUnique, "GHERKIN",testToolName,-1,-1,-1);	
                	}else{
                		atsgId = data.responseJSON.Records[0].atsgId;
                		//idUnique = idUnique;
                		// testDataId=data.responseJSON.Records[0].testDataId;
                		 testToolName = data.responseJSON.Records[0].testEngine;
                		 if(testToolName == 'SELENIUM'){

                			 //$("#testTollMaster_bdd_ul").select("val",$("#testTollMaster_bdd_ul").find('option')[0].value);
                			 $("#testTollMaster_bdd_ul").val($("#testTollMaster_bdd_ul").find('option')[0].value);
                		 }else if(testToolName == 'APPIUM'){

                			 //$("#testTollMaster_bdd_ul").select("val",$("#testTollMaster_bdd_ul").find('option')[1].value);
                			 $("#testTollMaster_bdd_ul").val($("#testTollMaster_bdd_ul").find('option')[1].value);
                		 } else if(testToolName == 'SEETEST'){

                			 //$("#testTollMaster_bdd_ul").select("val",$("#testTollMaster_bdd_ul").find('option')[1].value);
                			 $("#testTollMaster_bdd_ul").val($("#testTollMaster_bdd_ul").find('option')[2].value);
                		 }
                		  //  objectRepositoryId= data.responseJSON.Records[0].objectRepositoryId;
                		displayBDDTestScriptsFromTestCases(idUnique, "GHERKIN",testToolName,-1,-1,-1);
                		}
                }	     
		    	
		    
		    
		    }
		});  
	}
	if(parameterType == 'EDIT'){
	//displayBDDScripts(jsonObjMain.testCaseId,"GHERKIN","",jsonObjMain.productTypeName); 
		 saveAtsgParameters(idUnique,testToolName,-1,-1,-1);
		 displayBDDTestScriptsFromTestCases(idUnique, "GHERKIN",testToolName,-1,-1,-1);	
	}
	}else if(scriptsFor == "TestSuite"){
		displayBDDScripts(jsonObjMain.testCaseId,"GHERKIN","",jsonObjMain.productTypeName);
	}
}*/



function bddScriptGenerationHandler(){
	closeATSGContainer();
	displayDownloadTestScriptsFromTestCases(jsonObjMain.testCaseId, scriptTestCaseName, scriptDownloadName);
}

function closeATSGContainer(){
	$("#divPopUpDownloadTestScriptsFromTestCases").modal("hide");
	$("#divPopUpBDDTestScripts").modal("hide");	
}