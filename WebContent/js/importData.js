var importTestCaseStr = "testCases";
var importTestStepsStr = "teststeps";
var importProductFeatureStr = "productFeature";
var importDefectsStr = "defectBugnizerData";
var importTMSProductFeatureStr = "TMSProductFeature";
var uploadReportsToTMS	= "TMSReportsUpload";//Changes for Report Attachment to TMS :Bugzilla id: 777
var importActivitiesStr = "activities";
var importActivityTaskStr = "activityTasks";
var importChangeRequestsStr = "ChangeRequests";
var importUserListsStr = "UsersList"
var importCustomerListsStr="Customers";
var importWorkflowTemplateStatusStr = "WorkflowStatus"
var importStatusPolicyStr = "StatusPolicy"
var importTestDataStr = "TestDataRepository";
var activityWorkPackageId;
var testFactId;
var productId;
var demandUploadPermission;

var importSkill = "Skills";
//Import Product


$(document).on('click', '#trigUploadOfProductFeatures', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadFileOfProductFeatures").trigger("click");
	}
});

//Import Test Cases and Test Steps	        		

$(document).on('click', '#trigUploadTestCase', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadFileTestCase").trigger("click");
	}	
});	


$(document).on('click', '#trigUploadTestSteps', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadFileTestSteps").trigger("click");
	}	
});

//Import Review Defects

$(document).on('click', '#trigUploadAnalyseDefects', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadFileOfAnalyseDefects").trigger("click");
	}	
});

// Import Activities

$(document).on('click', '#trigUploadActivities', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadFileActivities").trigger("click");
	}	
});

// Import Change Requests

$(document).on('click', '#trigUploadChangeRequests', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadFileChangeRequests").trigger("click");
	}	
});

// Import Users List

$(document).on('click', '#trigUploadUserList', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadUserListFile").trigger("click");
	}	
});

//Import Status Policy for user and role

$(document).on('click', '#trigUploadStatusPolicy', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadStatusPolicy").trigger("click");
	}	
});


//Import Workflow Template status

$(document).on('click', '#trigUploadWorkflowTemplateStatus', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadWorkflowTemplateStatus").trigger("click");
	}	
});



//Import Activity task

$(document).on('click', '#trigUploadActivityTask', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadFileActivityTask").trigger("click");
	}	
});

//Import Activity task

$(document).off('click').on('click', '#testDataUpload', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadFileOftestData").trigger("click");
	}	
});

$(document).on('click', '#keywordJARUpload', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadKeywordJAR").trigger("click");
	}	
});

//Changes for Bug 777 - Report attachment to HPQC TestSet -starts
$(document).on('click', '#trigUploadOfReports', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadFileOfReports").trigger("click");
	}	
});

//Import My Skills

$(document).on('click', '#trigUploadSkills', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadFileSkills").trigger("click");
	}	
});

function triggerDemandUpload(message){
	demandUploadPermission = message;
	 $("#uploadDemand").trigger("click");
}

function triggerActivityCRTabUpload(){
	$("#uploadFileChangeRequests").trigger("click");
}

function triggerActivityUpload(){
	 $("#uploadFileActivities").trigger("click");
}

function triggerActivityTaskUpload(){
	$("#uploadFileActivityTask").trigger("click");
}

function triggerProductFeatureUpload(){
	 $("#uploadFileOfProductFeatures").trigger("click");
}

function triggerProductTestCaseUpload(){
	 $("#uploadFileTestCase").trigger("click");
}

function triggerProductTestStepsUpload(){
	$("#uploadFileTestSteps").trigger("click");
}

function triggerUserListUpload(){
	 $("#uploadUserListFile").trigger("click");
}

function triggerCustomerUserListUpload(){
	 $("#uploadCustomerFile").trigger("click");
}


//Changes for Bug 777 - Report attachment to HPQC TestSet - Ends

/* Import functionality starts */

function importProductFeatures() {   
    var uploadProductID = "uploadFileOfProductFeatures";
    var uploadFileName = "productFeaturefileName";
    
	importFeaturesInitialStep(uploadProductID, uploadFileName);
	importFeatureFinalStep("product.features.import?productId="+productId, importProductFeatureStr, uploadProductID, uploadFileName);	
}
//Changes for Bug : 717
//TAF-iLCM integration
function importProductFeaturesTMS() {   
    console.log("import console log");
    
    openLoaderIcon();
    importAjaxCall("hpqc.import.features.rest?productId="+productId, importProductFeatureStr);
}

function importTestCase(){
    var uploadTestCaseID = 	"uploadFileTestCase";
    var uploadFileName = "fileNameTestCase";
	
    importFeaturesInitialStep(uploadTestCaseID, uploadFileName);
	importFeatureFinalStep("testcase.import?productId="+productId, importTestCaseStr, uploadTestCaseID, uploadFileName);
}

function importTestSteps(){
	var uploadTestStepsID = "uploadFileTestSteps";
    var uploadFileName = "fileNameTestSteps";
    
	importFeaturesInitialStep(uploadTestStepsID, uploadFileName);
	importFeatureFinalStep("teststeps.import?productId="+productId, importTestStepsStr, uploadTestStepsID, uploadFileName);
}

function importAnalyseDefects(){
	var uploadDefectsBugnizerID = "uploadFileOfAnalyseDefects";
    var uploadFileName = "fileNameAnalyseDefects";
    
	importFeaturesInitialStep(uploadDefectsBugnizerID, uploadFileName);
	importFeatureFinalStep("defect.bugnizer.import", importDefectsStr, uploadDefectsBugnizerID, uploadFileName);
}

function importActivityWorkpackage(){
	var uploadActivitiesID = "uploadFileActivities";
    var uploadFileName = "fileNameActivities";
 //   var workPackageId;
   // productBuildId = document.getElementById("treeHdnCurrentProductBuildId").value;
    //activityWorkPackageId=document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
    testFactId = document.getElementById("treeHdnCurrentTestFactoryId").value;
    productId = document.getElementById("treeHdnCurrentProductId").value;
	importFeaturesInitialStep(uploadActivitiesID, uploadFileName);
	//importFeatureFinalStep("activities.import?productBuildId="+productBuildId, importActivitiesStr, uploadActivitiesID, uploadFileName);
	importFeatureFinalStep("activities.import?testFactoryId="+testFactId+"&productId="+productId, importActivitiesStr, uploadActivitiesID, uploadFileName);
}
function importActivityTask(){
	var uploadActivitiesID = "uploadFileActivityTask";
    var uploadFileName = "fileNameActivityTask";
    activityWorkPackageId=document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
	importFeaturesInitialStep(uploadActivitiesID, uploadFileName);
	importFeatureFinalStep("activityTasks.import?activityWorkPackageId="+activityWorkPackageId, importActivityTaskStr, uploadActivitiesID, uploadFileName);
}

function importChangeRequests(){
	var uploadChangeRequestID = "uploadFileChangeRequests";
    var uploadFileName = "fileNameChangeRequests";
  	importFeaturesInitialStep(uploadChangeRequestID, uploadFileName);
	importFeatureFinalStep("change.requests.import?productId="+productId+"&entityType=18", importChangeRequestsStr, uploadChangeRequestID, uploadFileName);
}

function importUserList(userType){
	var uploadUserListID = "uploadUserListFile";
    var uploadFileName = "uploadUsersfileName";
  	importFeaturesInitialStep(uploadUserListID, uploadFileName);	
  	
  	var importUserListsStr="Users";
  	importFeatureFinalStep("userList.import?userType="+userType, importUserListsStr, uploadUserListID, uploadFileName);  	
}

function importCustomerList(userType){
	var uploadUserListID = "uploadCustomerUsersfileName";
    var uploadFileName = "uploadFile";
  	importFeaturesInitialStep(uploadUserListID, uploadFileName);
	  	
  	var importCustomerListsStr="Customers";
  	importFeatureFinalStep("userList.validation.for.import?userType="+userType, importCustomerListsStr, uploadUserListID, uploadFileName);  	
}

function importWorkflowTemplateStatus(){
	var uploadWorkflowTemplateStatusID = "uploadWorkflowTemplateStatus";
    var uploadFileName = "WorkflowTemplateStatus";
  	importFeaturesInitialStep(uploadWorkflowTemplateStatusID, uploadFileName);
	importFeatureFinalStep("workflow.template.status.import", importWorkflowTemplateStatusStr, uploadWorkflowTemplateStatusID, uploadFileName);
}


function importStatusPolicyforUserandRole(){
	var uploadStatusPolicyforUserandRoleID = "uploadStatusPolicy";
    var uploadFileName = "uploadStatusPolicy";
  	importFeaturesInitialStep(uploadStatusPolicyforUserandRoleID, uploadFileName);
	importFeatureFinalStep("entity.workflow.status.policy.import?productId="+productId, importStatusPolicyStr, uploadStatusPolicyforUserandRoleID, uploadFileName);
}

function importDemand(){
	var uploadDemandId = "uploadDemand";
    var uploadFileName = "Demand";
    
    workPackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
    if(workPackageId == ""){
    	workPackageId = 0;
    }
  	importFeaturesInitialStep(uploadDemandId, uploadFileName);
	importDemandStep("resource.demand.import?workPackageId="+workPackageId+"&shiftId=1&demandUploadPermission="+demandUploadPermission, importWorkflowTemplateStatusStr, uploadDemandId, uploadFileName);			
}

function importTestData(event){
	var filename = event.target.value;
	var file = filename.split('\\');
	var result = file[file.length-1];	
	$('#testDataAttachmentID').val(result);
	
	console.log("filename :"+result);
	console.log("location :"+filename);
}

function importKeywordJAR(event){
	var filename = event.target.value;
	var file = filename.split('\\');
	var result = file[file.length-1];	
	$('#keywordJARAttachmentID').val(result);
	
	console.log("filename :"+result);
	console.log("location :"+filename);
}

/*function submitTestData(){
	var uploadTestDataID = "uploadFileOftestData";
    var uploadFileName = "testDataFileName";
  	//importFeaturesInitialStep(uploadTestDataID, uploadFileName);
	importFeatureFinalStep("change.requests.import?productId="+productId, importTestDataStr, uploadTestDataID, uploadFileName);
}*/


/* Import functionality ends */
//Changes for Report Attachment to TMS :Bugzilla id: 777 - starts
function uploadReports(testRunJobId){
	var uploadReportsId = "uploadFileOfReports";
    var uploadFileName = "uploadFile";
    
	importFeaturesInitialStep(uploadReportsId, uploadFileName);	
	importFeatureFinalStep("hpqc.upload.reports.rest?testRunJobId="+testRunJobId,uploadReportsToTMS,uploadReportsId,uploadFileName);
}
//Changes for Report Attachment to TMS :Bugzilla id: 777 - ends

/* Importing coding structure starts - common */

function importFeaturesInitialStep(divName,fileName){	
	openLoaderIcon();
	console.log("fromIntialalsetup");
	// Activies should be imported for build so included the following if -else condition.
	// Navigation Path: Plan --> Activity Management --> Activity Work Package
	if(divName == "uploadFileActivities"){
		/*if(activityWorkPackageId==null || activityWorkPackageId <=0 || activityWorkPackageId == 'null'){
			callAlert("Please select the product Build");
			return false;
		}*/
	} else if(divName == "uploadFileActivityTask"){
		/*if(activityWorkPackageId==null || activityWorkPackageId <=0 || activityWorkPackageId == 'null'){
			callAlert("Please select the product Build");
			return false;
		}*/
	}else if(divName == "uploadFileSkills"){
		
		
	}else if(divName == "uploadDemand"){
		
		
	} else{
		if(divName == "uploadUserListFile") {
			productId =4243243;//Dummy product Id
		}
		if(divName != "uploadWorkflowTemplateStatus" && (typeof productId == 'undefined' || productId==null || productId <=0 || productId == 'null')){
			callAlert("Please select the product");
			return false;
		}
	}
	
    var x = document.getElementById(divName);
    if ('files' in x) {
        if (x.files.length > 0) {
            for (var i = 0; i < x.files.length; i++) {
                var file = x.files[i];
                if ('name' in file) {
                	$(fileName).text(file.name);
                }
            }
        }
    }  
    
}

function importFeatureFinalStep(url,name,btnName,fileName) {	
    var fileContent = document.getElementById(btnName).files[0];
	var formdata = new FormData();
	formdata.append(btnName, fileContent);
	 $.ajax({
		    url: url,
		    method: 'POST',
		    contentType: false,
		    data: formdata,
		    dataType:'json',
		    processData: false,
			complete : function(data){
				if(data != undefined){			
					callAlert(data.Message);
					closeLoaderIcon();
				}
			},
		    success : function(data) {		    	
		    	if(data.Result=="ERROR"){
		    		closeLoaderIcon();		    	
		    		callAlert(data.Message);
		    		return false;
		    	}	
		    	closeLoaderIcon();
				importSucessHandler(name);
					
		    	$(fileName).text("");
		    	$(btnName).val("").clone($(btnName));
		    	callAlert(data.Message);
		    	/*if(!url.includes("testcase.import")){
		    		downloadUploadedReport(data.exportFileName);
		    	}*/
		    },error: function (data){
				closeLoaderIcon();
			}
		   
		});
}

function importAjaxCall(url,name){
	$.ajax({
	    url: url,
	    method: 'POST',
	    contentType: false,	    
	    dataType:'json',
	    processData: false,
		complete : function(data){
			if(data != undefined){			
				closeLoaderIcon();
			}
		},
	    success : function(data) {
	    	if(data.Result=="ERROR"){
	    		closeLoaderIcon();		    	
	    		callAlert(data.Message);
	    		return false;
	    	}				
			importSucessHandler(name);	    	
	    },
		error: function (data){
			closeLoaderIcon();
		}
	});
}

function importSucessHandler(name){
	if(name == importTestCaseStr || name == importTestStepsStr){
		listTestCasesOfSelectedProduct();
		
	}else if(name == importProductFeatureStr){
		listFeaturesOfSelectedProduct();
	}else if(name == importTMSProductFeatureStr){
		alert("Importing Features");
	}else if (name == importDefectsStr){
		listAnalyseDefects();		
	}else if (name == importActivitiesStr){
		listActivitiesOfSelectedAWP(0,0,0,activityWorkPackageId,1,'yes', '#jTableContainerWPActivities'); 	
	}else if(name==importActivityTaskStr){
		productId=document.getElementById("treeHdnCurrentProductId").value;
		productVersionId=document.getElementById("treeHdnCurrentProductVersionId").value;
		productBuildId=document.getElementById("treeHdnCurrentProductBuildId").value;
		activityWorkPackageId = document.getElementById("treeHdnCurrentActivityWorkPackageId").value;
		activityId	= document.getElementById("treeHdnCurrentActivityId").value;
		
		listActivitiesOfSelectedActivity(productId,productVersionId,productBuildId,activityWorkPackageId,activityId,1,'yes',activityAssigneeId,activityReviewerId);
		
	}else if(name == importChangeRequestsStr){
		var value=$("#tablistProductACTWP>li.active").index();
		if(value == 5 || value == 0) {
			productActivityWPTabSelection(value);
		} else {
			changeRequests();
		}
	}
}

function importSkills(){	
	var uploadSkillsID = "uploadFileSkills";
    var uploadFileName = "fileNameSkills";   
	importFeaturesInitialStep(uploadSkillsID, uploadFileName);
	importFeatureFinalStep("user.skills.import", importSkill, uploadSkillsID, uploadFileName);
}

function importDemandStep(url,name,btnName,fileName) {	
    var fileContent = document.getElementById(btnName).files[0];
	var formdata = new FormData();
	formdata.append(btnName, fileContent);
	 $.ajax({
		    url: url,
		    method: 'POST',
		    contentType: false,
		    data: formdata,
		    dataType:'json',
		    processData: false,
			complete : function(data){
				if(data != undefined){			
					closeLoaderIcon();
				}
			},
		    success : function(data) {
		    	
		    	if(data.Result=="ERROR"){
		    		closeLoaderIcon();		    	
		    		callAlert(data.Message);
		    		return false;
		    	}	
		    	closeLoaderIcon();
				importSucessHandler(name);
					
		    	$(fileName).text("");
		    	$(btnName).val("").clone($(btnName));
		    	callAlert(data.Message);
		   	 	downloadUploadedReport(data.exportFileName);
		    },complete : function(data){
		    	closeLoaderIcon();	
		    },error: function (data){
				closeLoaderIcon();
			}		   
		});
}


function downloadUploadedReport(reportURL){
	var urlfinal="rest/download/evidence?fileName="+reportURL;
  	parent.window.location.href=urlfinal;
}

/* Importing coding structure Ends - common */
