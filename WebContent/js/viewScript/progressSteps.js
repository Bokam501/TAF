var ProgressSteps = function() {
  
   var initialise = function(jsonObj){
	   progressStepsHandler(jsonObj);
   };
		return {        
	        init: function(jsonObj) {        	
	        	initialise(jsonObj);
        }		
	};	
}();

function progressStepsHandler(jsonObj){	
	$("#progressStepsContainer h4").text("");
	$("#progressStepsContainer h4").text(jsonObj.Title);
	$("#widgetSummary").empty();
	var divId = '';	
	
	var stageOne = '';
	var stageTwo = '';
	var stageThree = '';
	var stageFour = '';
	
	var flag = false;	
	var stageArray = [];	
	var productId = '';
	
	if(jsonObj.Records != undefined && jsonObj.Records.length > 1) {
		for( var i = 0 ; i < jsonObj.Records.length ; i++ ){		
			stageArray.push(jsonObj.Records[i].Remarks);
			productId = jsonObj.productId;
		}
		
		for( var i = 0 ; i < stageArray.length ; i++ ){
			if(stageArray.length > 4) {
				stageOne = stageArray[0];
				stageTwo = stageArray[1];
				stageThree = stageArray[2];
				stageFour = stageArray[4];
			} else if(stageArray.length == 4) {
				stageOne = stageArray[0];
				stageTwo = stageArray[1];
				stageThree = stageArray[2];
				stageFour = stageArray[3];
			} else if(stageArray.length == 3) {
				stageOne = stageArray[0];
				stageTwo = stageArray[1];
				stageThree = stageArray[2];
			} else if(stageArray.length == 2) {
				stageOne = stageArray[0];
				stageTwo = stageArray[1];
			} else if(stageArray.length == 1) {
				stageOne = stageArray[0];
			} else {
				stageOne = "Processing...";
			}
			console.log("Total JSON Records : "+stageArray.length);
			console.log("	Stage One : "+stageOne+" ; StageTwo : "+stageTwo+" ; StageThree : "+stageThree+" ; StageFour : "+stageFour);
			console.log("---------------------------------------------------------------------------------------------");	
		}
	} else {
		stageOne = "Processing...";
	}
	
	divId = "<div class='col-md-3 mt-step-col first done'><div class='mt-step-number bg-white style='top:0%;left:-6%;'>1</div><div class='mt-step-title uppercase font-grey-cascade'>Create Workpackage</div><div class='mt-step-content font-grey-cascade' style='text-align: left;'>"+stageOne+"</div></div>";
	$("#widgetSummary").append(divId);
	
	if(stageTwo == '')
		divId = '<div class="col-md-3 mt-step-col" style="color: #95A5A6 !important;"><div class="mt-step-number bg-white" style="color: #95A5A6 !important;top:0%;left:-6%;">2</div><div class="mt-step-title uppercase font-grey-cascade" style="color: #95A5A6 !important;">Intelligent Test Plan</div><div class="mt-step-content font-grey-cascade" style="color: #95A5A6 !important;text-align: left;">'+stageTwo+'</div></div>';
	else 
		divId = '<div class="col-md-3 mt-step-col done"><div class="mt-step-number bg-white" style="top:0%;left:-6%;">2</div><div class="mt-step-title uppercase font-grey-cascade">Get Test Plan Recommendations</div><div class="mt-step-content font-grey-cascade" style="text-align: left;">'+stageTwo+'</div></div>';
	$("#widgetSummary").append(divId);
	
	if(stageThree == '')
		divId = '<div class="col-md-3 mt-step-col" style="color: #95A5A6 !important;"><div class="mt-step-number bg-white" style="color: #95A5A6 !important;top:0%;left:-6%;">3</div><div class="mt-step-title uppercase font-grey-cascade" style="color: #95A5A6 !important;">Update Test Plan</div><div class="mt-step-content font-grey-cascade" style="color: #95A5A6 !important;text-align: left;">'+stageThree+'</div></div>';
	else 
		divId = '<div class="col-md-3 mt-step-col done"><div class="mt-step-number bg-white" style="top:0%;left:-6%;">3</div><div class="mt-step-title uppercase font-grey-cascade">Update Test Plan</div><div class="mt-step-content font-grey-cascade" style="text-align: left;">'+stageThree+'</div></div>';
	$("#widgetSummary").append(divId);	
	
	if(stageFour == '')
		divId = "<div class='col-md-3 mt-step-col last'><div class='mt-step-number bg-white' style='color: #95A5A6 !important;top:0%;left:-6%;'>4</div><div class='mt-step-title uppercase font-grey-cascade' style='color: #95A5A6 !important;'>Execute Workpackage</div><div class='mt-step-content font-grey-cascade' style='color: #95A5A6 !important;text-align: left;'>"+stageFour+"</div></div>";
	else{ 
		if(stageFour.startsWith("Executing")) {
			divId = "<div class='col-md-3 mt-step-col last'><div class='mt-step-number bg-white' style='top:0%;left:-6%;color:#FFA500 !important;'>4</div><div class='mt-step-title uppercase font-grey-cascade' style='color:#FFA500 !important;'>Execute Workpackage</div><div class='mt-step-content font-grey-cascade' style='color:#FFA500 !important;text-align: left;'>"+stageFour+"</div></div>";
	    } else if(stageFour.startsWith("Completed") || stageFour.startsWith("Aborted")){	    	
			divId = "<div class='col-md-3 mt-step-col last done' style='border-color:#26C281 !important; color:#26C281 !important;'><div class='mt-step-number bg-white' style='top:0%;left:-6%;color:#26C281 !important;'>4</div><div class='mt-step-title uppercase font-grey-cascade' style='color:#26C281 !important;'>Workpackage Execution</div><div class='mt-step-content font-grey-cascade' style='color:#26C281 !important;text-align: left;'>"+stageFour+"</div></div>";							
		}
	}
    $("#widgetSummary").append(divId);	
}