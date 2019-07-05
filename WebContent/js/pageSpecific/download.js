function getContextCompletePath(){
	var activeUrl = window.location.href;
	var activeJsp = activeUrl.split('/');	
	activeJsp = activeJsp[activeJsp.length - 1];
	var mappingUrl = activeUrl.replace(activeJsp,'');
	return mappingUrl;
}

function downloadTemplateProductFeature(){
	var mappingUrl = getContextCompletePath();
	var completeURL = mappingUrl+'/templates/iLCM_TEMPLATE_PRODUCT_FEATURE.xlsx';
	document.location.href = completeURL;
}

function downloadTemplateTestCase(){
	var mappingUrl = getContextCompletePath();
	var completeURL = mappingUrl+'/templates/iLCM_TESTCASE_IMPORT_TEMPLATE.xlsx';
	document.location.href = completeURL;
}

function downloadTemplateTestSteps(){
	var mappingUrl = getContextCompletePath();
	var completeURL = mappingUrl+'/templates/iLCM_TEMPLATE_TEST_STEPS.xlsx';
	document.location.href = completeURL;
}

function downloadTemplateChangeRequest(){
	var mappingUrl = getContextCompletePath();
	var completeURL = mappingUrl+'/templates/iLCM_TEMPLATE_CHANGE_REQUESTS.xlsx';
	document.location.href = completeURL;
}

function downloadTemplateUserList(){
	var mappingUrl = getContextCompletePath();
	
	if(userDownLoadTemplateXerox == "YES"){
		var completeURL = mappingUrl+'/templates/iLCM_TEMPLATE_USER_RESOURCEPOOL.xlsx';
		document.location.href = completeURL;
	}else{
		var completeURL = mappingUrl+'/templates/iLCM_TEMPLATE_USER.xlsx';
		document.location.href = completeURL;
	}
}

function downloadTemplateNewUserList(){
	var mappingUrl = getContextCompletePath();
	var completeURL = mappingUrl+'/templates/iLCM_TEMPLATE_USER.xlsx';
	document.location.href = completeURL;
}

function downloadTemplateActivity(){
	var mappingUrl = getContextCompletePath();
	var completeURL = mappingUrl+'/templates/iLCM_TEMPLATE_ACTIVITY.xlsx';
	document.location.href = completeURL;
}

function downloadTemplateActivityTask(){
	var mappingUrl = getContextCompletePath();
	var completeURL = mappingUrl+'/templates/iLCM_TEMPLATE_ACTIVITY_TASK.xlsx';
	document.location.href = completeURL;
}

function downloadTemplateFromWebHelp(){
	var activeUrl = getContextCompletePath();
	var activeJsp = activeUrl.split('/');
	activeJsp = activeJsp.filter(function(v){return v!==''});
	activeJsp = activeJsp[activeJsp.length - 1];
	mappingUrl = activeUrl.replace(activeJsp,'');
	
	var completeURL = mappingUrl+'/templates/iLCM_TESTCASE_IMPORT_TEMPLATE.xlsx';
	document.location.href = completeURL;
}


function downloadWorFlowTemplate(){
	
	var mappingUrl = getContextCompletePath();
	var completeURL = mappingUrl+'/templates/iLCM_TEMPLATE_WORKFLOW_STATUS.xlsx';
	document.location.href = completeURL;
	
}


function downloadEntityWorkflowMappingStatusPolicyForUserandRole(){
	
	var mappingUrl = getContextCompletePath();
	var completeURL = mappingUrl+'/templates/iLCM_TEMPLATE_ENTITY_WORKFLOW_MAPPINGSTATUSPOLICTFOR-USERandROLE.xlsx';
	document.location.href = completeURL;
	
	
	
}

function downloadTemplateSkills(){	
	var mappingUrl = getContextCompletePath();
	var completeURL = mappingUrl+'/templates/iLCM_TEMPLATE_skill_PROFILE.xlsx';
	document.location.href = completeURL;	
}



