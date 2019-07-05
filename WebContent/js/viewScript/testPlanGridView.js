var testJobId = "";
var lastLine = 1;
var jobLogContent = "";
var TestPlanGridView = function(){	
	var initialise = function(jsonObj){
		initialiseTestPlanGridView(jsonObj);
	};	
		return {
			init : function(jsonObj){
				initialise(jsonObj);
			}
		};
}();

function initialiseTestPlanGridView(jsonObj){
	testrunplanTileView(jsonObj.url+'&jtStartIndex=-1&jtPageSize=-1');
}

var nodeType='Product';
var testCaseExecution='';
var executeEditTrplan='';
//BEGIN - Testrunplan Tile View
function testrunplanTileView(url){
	console.log('testrunplanTileView...');
	openLoaderIcon();
	var response;	
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			if(data.Result == "ERROR"){
				//callAlert(data.Message);
			}else{
				response = data.Records;
				listTestrunTiles(response);
			}
			closeLoaderIcon();
		},
		error : function(data){
			closeLoaderIcon();
		},
		complete : function(data){
			closeLoaderIcon();
		}
	});
}
function listTestrunTiles(data){	
	$('#testrunTileContent').empty();
	 
	 var availability = "bg-white"; 
	 if(data.length!=0){	            
         for (var key in data) {
         	var botName = data[key].productName;//data[key].botName;
         	var imgSrc="";
     		//imgsrc="css/images/noimage.jpg";
     		imgSrc="css/images/testplan.png";
         	var nameBot = data[key].testRunPlanName;
         	var botStatus = 'Available';//data[key].botStatus;
         	if(botStatus == null) {
         		botStatus="";
         	}
         	if(nameBot.length > 25){         		
         		nameBot = (data[key].testRunPlanName).toString().substring(0,20)+'...';         		
         	}else{
         		nameBot = data[key].testRunPlanName;
         	}         	
         	var titleSchedule = "<"+data[key].testRunPlanName+"> "+"Test Plan : Execution Schedule";
        	var subTitleSchedule = "Product ";//+$(headerTitle).text();
        	var testRunPlanID = data[key].testRunPlanId;
       		var testRunPlanType = "TestRunPlan";
       		var testRunPlanGroup = "TestRunPlan";
       		var testRunPlanGroupAudit = "testRunPlanGroupAudit";
       		var entityTypeIdComments = 9;
       		var entityNameComments = "TestRunPlan";
       		var testRunPlanComments = "trpComments";
       		var testRunPlanIdNameStr = testRunPlanID+"~"+data[key].testRunPlanName;
       		document.getElementById("hdnProductName").value=data[key].productName;
       		var result = data[key].testRunPlanId+'~'+data[key].testRunPlanName+'~'+data[key].productId+'~'+data[key].productName+'~'+data[key].productVersionId+'~'+data[key].executionTypeId;	
       		
         	var brick = '<div class="'+"tile "+ availability + '" style="border:#ddd solid 1px;width:170px !important;height:150px !important;"><div class="tile-body" style="padding:3px 3px;">' +
    	    '<div class="row">'+
    	    	'<div style="position:absolute; float:left;border-right: solid 0px #ddd;"><img width="60px" height="60px" src="'+ imgSrc +'" onerror="this.src=\'css/images/noimage.jpg\'"></img></div>'+
    	    	'<div style="float:right;display: flex;"><div><span style="float: right;padding: 0px 0px;"><img id="'+testRunPlanIdNameStr+'" class="exe" src="css/images/execute_metro.png" style="color:black;cursor:pointer;margin-right:0px;margin-top:2px;" title="Execute Test Plan" onclick="executeTestRunPlanFromGrid(\''+testRunPlanID+'\',\''+data[key].automationMode+'\',\''+data[key].productName+'\',\''+data[key].productVersionName+'\',\''+data[key].useIntelligentTestPlan+'\',\''+data[key].productId+'\',event)" ></img></span><br/>'+
    	    	'<span style="float: right;padding: 0px 0px;"><i class="fa fa-clock-o" style="color:black;cursor:pointer" title="Schedule" onclick="scheduleUsingCronGen(\''+titleSchedule+'\',\''+ subTitleSchedule+'\',\''+ testRunPlanID+'\',\''+ testRunPlanType+'\')"></i></span><br/>'+
    	    	//'<span style="float: right;padding: 0px 2px;"><i class="fa fa-history" style="color:grey;cursor:pointer" title="Execution History" onclick="listTRPExecutionHistory(\''+data[key].testRunPlanId+'\')"></i></span><br/>'+
    	    	'<span style="float: right;padding: 0px 0px;"><img width="13px" height="13px" class="" src="css/images/list_metro.png" style="color:black;cursor:pointer;margin-right:1px;margin-top:2px;" title="Test Environment" onclick="testRunPlanForConfigurations(\''+data[key].productVersionId+'\',\''+data[key].testRunPlanId+'\',\''+data[key].productId+'\')" ></img></span><br/>'+    	    	
    	    	'<span style="float: right;padding: 0px 0px;"><img width="13px" height="13px" class="exe" src="css/images/list_metro.png" style="color:black;cursor:pointer;margin-right:0px;margin-top:2px;" title="Test Suite" onclick="testSuiteForTestRunPlans(\''+data[key].productId+'\',\''+data[key].productVersionId+'\',null,null,\''+data[key].testRunPlanId+'\')" ></img></span><br/>'+
    	    	'<span style="float: right;padding: 0px 0px;"><i class="fa fa-paperclip" style="color:black;cursor:pointer" title="Attachments" onclick="testSuiteForTestRunPlanAttachments(\''+data[key].productId+'\',\''+data[key].productVersionId+'\',null,null,\''+data[key].testRunPlanId+'\',\''+data[key].testRunPlanName+'\')" ></i></span><br/>'+
    	    	'<span style="float: right;padding: 0px 0px;"><img width="13px" height="13px" class="exe" src="css/images/list_metro.png" style="color:black;cursor:pointer;margin-right:0px;margin-top:2px;" title="Properties" onclick="testRunPlanConfigurationProperties(\''+data[key].testRunPlanId+'\')" ></img></span><br/>'+
    	    	'</div><div><span style="float: right;padding: 0px 0px;"><img width="13px" height="13px" class="exe" src="css/images/list_metro.png" style="color:black;cursor:pointer;margin-right:0px;margin-top:2px;" title="Test Configuration" onclick="testBedSummary(\''+data[key].testRunPlanId+'\',\''+data[key].productId+'\',\''+data[key].productVersionId+'\')" ></img></span><br/>'+
    	    	'<span style="float: right;padding: 0px 2px;"><i class="fa fa-comments" style="color:black;cursor:pointer" title="Comments" onclick="listComments(\''+entityTypeIdComments+'\',\''+ entityNameComments+'\',\''+ data[key].testRunPlanId+'\',\''+ data[key].testRunPlanName+'\',\''+ testRunPlanComments+'\')"></i></span><br/>'+
    	    	'<span style="float: right;padding: 0px 2px;"><i class="fa fa-search-plus" style="color:black;cursor:pointer" title="Audit History" onclick="listGenericAuditHistory(\''+data[key].testRunPlanId+'\',\''+ testRunPlanGroup+'\',\''+ testRunPlanGroupAudit+'\')"></i></span><br/>'+
    	    	'<span style="float: right;padding: 0px 2px;"><i class="fa fa-history" style="color:black;cursor:pointer" title="Workpackages" onclick="viewCurrentStatus(\''+data[key].testRunPlanId+'\',\''+data[key].productId+'\')"></i></span><br/>'+
    	    	'<span style="float: right;padding: 0px 0px;"><img width="15px" height="15px" class="exe" src="css/images/analytics.jpg" style="color:black;cursor:pointer;margin-right:0px;margin-top:2px;" title="Intelligent TestPlan" onclick="getPlanHandlerTestRunPlanTab(\''+data[key].productId+'\',\''+data[key].productName+'\',\''+data[key].testRunPlanName+'\',\''+data[key].productBuildId+'\',\''+data[key].productBuildName+'\')" ></img></span><br/>'+
    	    '</div></div>'+
    	    '<div class="row" style="text-align:center;margin-top:100px;height:42px;overflow:hidden;"><span id="'+result+'"  onclick="callConfirmTestRunPlan(event)" title="'+testRunPlanID+'-'+data[key].testRunPlanName+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;float:left;" >'+ nameBot +'<br /> </span></div>'+    	  
    	    '</div>'+
    	    '</div>';    	    
	 		$('#testrunTileContent').append($(brick));
         }
	 }else{
		 var brickcontainer = '<div class="text-center">No data available!</div>';
		 $('#testrunTileContent').append($(brickcontainer));
	 }
	 
	 if(!($('#testrunTileContent').hasClass('tiles'))){
			$('#testrunTileContent').addClass('tiles').css("margin-top","10px");
	 } 
}

function executeTestRunPlanFromGrid(trpId, automationMode, productName, productVersionName, useIntlTp, prodId, event){
	if(nodeType == "TestFactory") {
		callAlert("Please select Product or Product Version");
		return false;
	}
	productId=prodId;
	document.getElementById("treeHdnCurrentProductName").value = productName;
	document.getElementById("treeHdnCurrentProductVersionName").value = productVersionName;
	var autoMode = $("#automationMode_ul").find('option:selected').attr('id');
	(autoMode == undefined) ?  trpExecMode = automationMode : trpExecMode = autoMode;
	//trpExecMode = automationMode;
	//testRunPlanForBDD(trpId, event);
	document.getElementById("hdnTestRunPlanId").value = trpId;
	document.getElementById("hdnTestRunPlanDeviceId").value = trpId;
	var url = 'testplan.readiness.check?testPlanId='+trpId;
	 $.ajax({
		 type: "POST",
		 url: url,
		 success: function(data) {  
			 if(data.Result == "ERROR"){
				 return false;
			 }else if(data.Result == "OK"){
				 if(data.Message.toUpperCase() != 'YES') {
					 callAlert(data.exportFileName);
					 return false;
			     }
				 
				if(trpExecMode == 'Attended'){
					//testRunPlanForBDD(testrunPlanId,event);
			    	if(useIntlTp.toString() == "0"){
			    		$('#recommendedHeader').hide();
			    		$('#jTableContainerTestCaseForTestRunPlan').css('margin-top','20px');
			    	}else{
			    		$('#jTableContainerTestCaseForTestRunPlan').css('margin-top','0px');
			    		$('#recommendedHeader').show();
			    	}
					$('#div_PopupRunConfigurationList').modal();
					testCasebyTestRunPlanId();
					testRunPlanTestBeds();
				} else {
					testRunPlanId = trpId;	
					unattendedCallMode(testRunPlanId);
				} 
			 }
		 }
	 });
}

function testRunPlanForConfigurations(productVersionId, testRunPlanId, pdtId){
	if(pageType == "HOMEPAGE"){ 
		nodeType = "ProductVersion"; 
		productId = pdtId;
	}
	/*if(nodeType == "TestFactory" || nodeType == "Product"){
		callAlert("Please select the Product Version");
		return false;
	}*/
	environments();
	$("#envCombSummary").modal();
	$(document).off('focusin.modal');
}

function listExecutionHistory(testRunPlanId){
	if(nodeType == "TestFactory"){
		callAlert("Please select Product or Product Version");
		return false;
	} else if(nodeType == "Product"){
		productVersionId = -1;
		url='workpackage.executiondetails.productorbuildlevel.list?productBuildId='+productVersionId+'&productId='+productId+'&testFactoryId=-1&jtStartIndex=0&jtPageSize=10000';
		assignWPDataTableValues(url, "parentTable", "", "");
	} else if(nodeType == "ProductVersion"){
		productId = -1;
		url='workpackage.executiondetails.productorbuildlevel.list?productBuildId='+productVersionId+'&productId='+productId+'&testFactoryId=-1&jtStartIndex=0&jtPageSize=10000';
		assignWPDataTableValues(url, "parentTable", "", "");
	}
	$("#showWorkpackageContainer").modal();
}

function testSuiteForTestRunPlans(prodId, versionId,timestamp, type, runPlanId){
	if(pageType=="HOMEPAGE"){
		nodeType = "ProductVersion";
		key = prodId;
		document.getElementById("treeHdnCurrentProductId").value = prodId;
		productVersionId = versionId;
		rnCnfgFlag=false;
	}else{
		productId = prodId;
	}
	rnCnfgId=-1;
	tpcId = runPlanId;
	testsuites(nodeType);
	$('#testSuiteDT_Child_Container').modal();
	$(document).off('focusin.modal');
}

function testSuiteForTestRunPlanAttachments(productId, versionId,timestamp, type, runPlanId, testRunPlanName){
	var jsonObj={"Title":"Attachments for Test Plan",			          
			"SubTitle": 'TestRunPlan : ['+runPlanId+'] '+testRunPlanName,
			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=9&entityInstanceId='+runPlanId,
			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=9&entityInstanceId='+runPlanId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
			"deleteURL": 'delete.attachment.for.entity.or.instance',
			"updateURL": 'update.attachment.for.entity.or.instance',
			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=9',
			"multipleUpload":true,
	};	 
	$('#attachmentPopup').css('z-index','10051');
	Attachments.init(jsonObj);
}

function listComments(entityTypeId, entityName, instanceId, instanceName, componentUsageTitle){
	// testing 
	var entityTypeId=9;
	var componentUsageTitle="testrunplanComments";

	var url='comments.for.entity.or.instance.list?productId=0&entityTypeId='+entityTypeId+'&entityInstanceId='+instanceId+'&jtStartIndex=0&jtPageSize=10000';
	//var instanceId = row.data().productId;
	var jsonObj={"Title":"Comments on "+entityName+ ": " +instanceName,
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,
			"componentUsageTitle":componentUsageTitle,			
			"entityTypeId":entityTypeId,
			"entityInstanceId":instanceId,
			"productId":productId,
	};
	CommentsMetronicsUI.init(jsonObj);
}

function showTestBedSummary(arg){
	if(arg == '1'){
		$("#environmentCombinationPlan").show();
		$("#hostdeviceMap").hide();
		testBed();
	} 
	
	if(arg == '2'){
		if(nodeType == 'ProductVersion' || nodeType == 'TestFactory'){
			callAlert("Please select Product");
			return false;
		}
		$("#environmentCombinationPlan").hide();
		$("#hostdeviceMap").show();
	}
}

function testBedSummary(trpId,pId,pvId){
	testrunPlanId = trpId;
	tpcId = testrunPlanId;
	productId = pId;
	rnCnfgFlag = true;
	if(pageType=="HOMEPAGE"){
		key=productId;
		productVersionId=pvId;
	}
	testConfigurationURL = 'runConfiguration.listbyTestRunPlan?testRunPlanId='+testrunPlanId;
	testPlanDataTable("TestConfigurationTable",testConfigurationURL);
	$("#showTestBedSummary").modal();
}
function showTestSuiteGrp(obj){
	if(obj == 1){
		$("#createts").show();
		$("#testsuiteTestRunPlan").hide();
	}else{
		$("#createts").hide();
		$("#testsuiteTestRunPlan").show();
	}
}

function testBed(){
	if(pageType=="HOMEPAGE")productVersionId='1';
	if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == ''){
		callAlert("Please select the Product Version");
		return false;
	}	
	var date = new Date();
	var timestamp = date.getTime();    	
	urlToGetEnvironmentCombinationByProduct = "environment.combination.list.byProductId?productVersionId="+productVersionId+"&productId=-1&workpackageId=-1&testRunPlanId="+testRunplanId;
	listEnvCombinationByProductPlan(-1,productVersionId);	
}

//Current Status updates on Test Plan WorkPackage Status
var currPdtId;
function viewCurrentStatus(testRunPlanId,pdtId){
	currPdtId = pdtId;
	var url="workpackage.executiondetails.testrunplan.list?testRunPlanId="+testRunPlanId+"&jtStartIndex=-1&jtPageSize=-1";	
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			$("#snapshotStatus").show();
			listWorkPacakgeTiles(data);			
			closeLoaderIcon();
		}
	});
}

function listWorkPacakgeTiles(data){	
	$('#workPackageTileContent').empty();	 
	 var availability = "bg-white"; 
	 if(data.Records.length!=0){     
         //for (var key in data.Records) {
        for(var i=0;i<data.Records.length;i++){
         	var botName = data.Records[i].productName;//data[key].botName;
         	var imgSrc="";
     		//imgsrc="css/images/noimage.jpg";
     		imgSrc="css/images/workpackage1.png";
         	var nameBot = data.Records[i].workPackageName;
         	var tpName = data.Records[i].testPlanName;
         	var tsName = data.Records[i].testsuiteName;
         	var botStatus = 'Available';//data[key].botStatus;
         	if(botStatus == null) {
         		botStatus="";
         	}
         	var titleSchedule;
         	var workPackageId;
         	if(nameBot!= undefined && nameBot.length > 25){         		
         		nameBot = (data.Records[i].workPackageName).toString().substring(0,20)+'...';
         		titleSchedule = nameBot;
         	}else if(nameBot!= undefined){
         		nameBot = data.Records[i].workPackageName;
         		titleSchedule = "<"+data.Records[i].workPackageName+">";
         		
         	}
         	if(tpName != null && tpName.length > 10){         		
         		tpName = (data.Records[i].testPlanName).toString().substring(0,7)+'...';         		
         	}else{
         		tpName = data.Records[i].testPlanName;
         	}
         	if(tsName != null && tsName.length > 10){         		
         		tsName = (data.Records[i].testsuiteName).toString().substring(0,7)+'...';         		
         	}else{
         		tsName = data.Records[i].testsuiteName;
         	}
        	var subTitleSchedule = "Product ";//+$(headerTitle).text();      	
        	workPackageId = data.Records[i].workPackageId;
        	var startDate = data.Records[i].firstActualExecutionDate;
        	if(startDate != null || startDate != undefined){
        		startDate = '<span title="'+data.Records[i].firstActualExecutionDate+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >'+ data.Records[i].firstActualExecutionDate +'<br /> </span>';
        	}else{
        		startDate = '';
        	}        	
        	var wpResult = "";
			if(data.Records[i].result != null || data.Records[i].result != undefined ){
				wpResult = data.Records[i].result;
			}else{
				wpResult = "---";
			}
			var passPercentage = 0;			
			if(data.Records[i].totalWPTestCase == 0){
				passPercentage=0;
			}else{
				passPercentage = (data.Records[i].p2totalPass * 100)/(data.Records[i].totalWPTestCase);
				passPercentage = passPercentage.toFixed(0);
			}
			
			var failedPercentage = 0;			
			if(data.Records[i].totalWPTestCase == 0){
				failedPercentage=0;
			}else{
				failedPercentage = (data.Records[i].p2totalFail * 100)/(data.Records[i].totalWPTestCase);
				failedPercentage = failedPercentage.toFixed(0);
			}
			var notExecuted = data.Records[i].totalWPTestCase - (data.Records[i].p2totalPass +  data.Records[i].p2totalFail);
			
			var notExecutedPercentage = 0;			
			if(data.Records[i].totalWPTestCase == 0){
				notExecutedPercentage=0;
			}else{
				notExecutedPercentage = (notExecuted * 100)/(data.Records[i].totalWPTestCase);
				notExecutedPercentage = notExecutedPercentage.toFixed(0);
			}
			var isResultBadge='';
			if(wpResult.includes("Passed")){
				isResultBadge += '<span title="'+wpResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : </span>';
				isResultBadge += '<span id="wppassed" class="badge badge-default" style="background: #076;margin-top: 10px;margin-bottom: 10px;display: inline;margin-right: 5px;">'+wpResult+' <br /> </span>';
			}else if(wpResult == "Failed"){
				isResultBadge += '<span title="'+wpResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : </span>';
				isResultBadge += '<span id="wp" class="badge badge-default" style="background: rgb(231, 80, 90);margin-top: 10px;margin-bottom: 10px;display: inline;">'+wpResult+' <br /> </span>';
			}else if(wpResult == "---"){
				isResultBadge += '<span title="'+wpResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : </span>';
				isResultBadge += '<span id="" class="badge badge-default" style="background: rgb(255, 214, 26);margin-top: 10px;margin-bottom: 10px;display: inline;">In Progress<br /> </span>';
			}
			var isReport;
			isReport = '<span style="float: right;padding: 0px 0px;"><img width="13px" height="13px" class="exe" src="css/images/list_metro.png" style="color:grey;cursor:pointer;margin-right:0px;margin-top:2px;" title="Test Job" onclick="workPackageTestRunPlanFromGrid(\''+data.Records[i].workPackageId+'\',null)"></img></span><br/>'+	
    					'<span style="float: right;padding: 0px 2px;"><i class="fa fa-comments" style="color:black;cursor:pointer" title="Comments" onclick="workPackageCommentsFromGrid(\''+data.Records[i].workPackageId+'\',\''+data.Records[i].workPackageName+'\',null)"></i></span><br/>'+				
    					'<span style="float: right;padding: 0px 2px;"><i class="fa fa-search-plus" style="color:black;cursor:pointer" title="Audit History" onclick="workPackageAuditHistoryFromGrid(\''+data.Records[i].workPackageId+'\',null)"></i></span><br/>';
			if((passPercentage != 'NaN' && passPercentage != 0 )|| (failedPercentage != 'NaN' && failedPercentage != 0)){
				isReport += /*'<span style="float: right;padding: 0px 2px;"><img width="13px" height="13px" class="exe" src="css/images/pdf_type1.png" style="color:black;cursor:pointer;margin-right:0px;margin-top:2px;"  title="WP PDF Report" onclick="workPackageReportFromGrid(\''+data.Records[i].workPackageId+'\',null)"></img></span><br/>'+*/
				'<span style="color:black;"><i class="fa fa-desktop" title="View WP HTML Report"  style="cursor:pointer;font-size:15px;padding-top:5px;margin-left:3px;" onclick="exportWPhtmlEvidenceResults(null,null,'+data.Records[i].workPackageId+');"/></span><br/>';
			}
    	
        	var brick = '<div class="'+"tile "+ availability + '" style="border:#ddd solid 1px;width:245px !important;height:272px !important;"><div class="tile-body" style="padding:3px 3px;">' +
    	    '<div class="row">'+
    	    	'<div style="position:absolute; float:left;border-right: solid 0px #ddd;"><img width="60px" height="60px" onclick="showWorkpackageSummary(\''+workPackageId+'\',\''+data.Records[i].exeType+'\');" title="'+workPackageId+'-'+data.Records[i].workPackageName+'" src="'+ imgSrc +'" onerror="this.src=\'css/images/noimage.jpg\'"></img></div>'+
    	    	'<div style="float:right;">'+ isReport +  	    		
    	    '</div>'+
    	    '<div class="row" style="text-align:left;margin-top:75px;overflow:hidden;">'+
    	    '<span title="'+workPackageId+'-'+data.Records[i].workPackageName+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >'+ nameBot +'<br /> </span>'+
    	     startDate+ 
     	    '<span class="botTileWrap" title="'+data.Records[i].testPlanName+'" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Testplan Name : '+tpName+'<br /></span>'+
    	    '<span class="botTileWrap" title="'+data.Records[i].testsuiteName+'" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Testsuite Name : '+tsName+'<br /></span>'+
    	     '<span class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Mode : '+data.Records[i].executionMode+'<br /></span>'+
    	    '<span title="'+data.Records[i].wpStatus+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Status : '+ data.Records[i].wpStatus +'<br /> </span>'+
    	    /*'<span title="'+data.Records[i].result+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : '+ data.Records[i].result +'<br /> </span>'+*/
    	    isResultBadge+
    	    '<span title="'+data.Records[i].totalWPTestCase+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;display:block;" >Total Testcases : '+ data.Records[i].totalWPTestCase +'<br /> </span>'+
			'<span id="passedTestCases" class="badge badge-default" style="background: #076;margin-top: 10px;margin-bottom: 10px;display: inline;margin-right: 5px;">'+data.Records[i].p2totalPass+' ['+passPercentage+'%]</span>'+
			'<span id="failedTestCases" class="badge badge-default" style="background: rgb(231, 80, 90);margin-top: 10px;margin-bottom: 10px;display: inline;">'+data.Records[i].p2totalFail+' ['+failedPercentage+'%]</span>'+
			'<span id="notExecutedTestCases" class="badge badge-default" style="background: rgb(112,128,144);margin-top: 10px;margin-bottom: 10px;display: inline;">'+notExecuted+' ['+notExecutedPercentage+'%]</span>'+
    	    '</div>'+    	  
    	    '</div>'+
    	    '</div>';    	    
	 		$('#workPackageTileContent').append($(brick));
         }
	 }else{
		 var brickcontainer = '<div class="text-center">No data available!</div>';
		 $('#workPackageTileContent').append($(brickcontainer));
	 }
	 
	 if(!($('#workPackageTileContent').hasClass('tiles'))){
			$('#workPackageTileContent').addClass('tiles').css("margin-top","10px");
	 } 
}

function workPackageTestRunPlanFromGrid(workpackId){
	workpackageId = workpackId;
	var url = 'workpackage.testcase.execution.summary.build.list?workPackageId='+workpackId+'&productBuildId=-1&jtStartIndex=0&jtPageSize=1000';
	assignWPDataTableValues(url, "childTable2", "", "");
	$('#wpTestRunJobDTContainer').hide();
	$('#wpTestRunJobDTContainerTileView').show();
	$("#wpTestRunJobContainer").modal();
	$('#wpTestRunJobContainer h4').text(workpackageId+' - Test Job');
}

function workPackageCommentsFromGrid(workpackId, workpackageName){
	var entityTypeIdComments = 2;
	var entityNameComments = "Workpackage";
	listComments(entityTypeIdComments, entityNameComments, workpackId, workpackageName, "workPackageComments");
	
	$("#div_CommentsUI .green-haze").show();
}

function workPackageAuditHistoryFromGrid(workpackId){
	var url='administration.event.list?sourceEntityType=WorkPackage&sourceEntityId='+workpackId+'&jtStartIndex=0&jtPageSize=1000';
	var jsonObj={"Title":"WorkPackage Audit History:",
			"url": url,	 
			"jtStartIndex":0,
			"jtPageSize":1000,
			"componentUsageTitle":"workPackageAudit",
	};
	SingleDataTableContainer.init(jsonObj);
}

function workPackageReportFromGrid(workPackageId) {
	var vURL = "report.run.pdf?testRunNo=" + workPackageId
			+ "&testRunConfigurationChildId=-1&deviceId=&testRunJobId=-1"
			+ "&reportType=No&viewType=PDF";
	var thediv = document.getElementById('reportbox');
	if (thediv.style.display == "none") {
		openLoaderIcon();
		$('.blockUI.blockOverlay').css('z-index','1000000');
		$('.blockUI.blockMsg.blockPage').css('z-index','1000001');
		$.post(vURL, function(data) {
			var fileName = '';
			if (data.Result == 'Ok') {
				fileName = data.exportFileName;
			} else {
				callAlert(data.Message);				
				return false;
			}
			thediv.style.display = "";
			loadPopupTestRunJobRes(fileName);
			$('.blockUI.blockOverlay').css('z-index','1000');
        	$('.blockUI.blockMsg.blockPage').css('z-index','1001');
        	closeLoaderIcon();
		});
	} else {
		thediv.style.display = "none";
		thediv.innerHTML = '';
	}
	return false;
}
function snapshotStatusCloseHandler(){
	$('#snapshotStatus').hide();
}
var currTable = 1;
var date = new Date();
var timestamp = date.getTime();
function showEnvorEnvCombinationTable(obj){
	if(obj == 1){
		$(".jTbEnvCmb").show();
		$(".jTbEnv").hide();
		$("#filter1").show();
		currTable = 1;
	}else{
		$(".jTbEnvCmb").hide();
		$(".jTbEnv").show();
		$("#filter1").hide();
		$("#filterForEnvironments").show();
		var envstatus = $("#selectEnvironmentstatus_ul").find('option:selected').val();		
		if(productId == -1){
			callAlert("Please select Product");
			return false;
		}
		urlToGetEnvironmentOfSpecifiedProductId = 'administration.product.environment.list?productMasterId='+productId+"&envstatus="+envstatus+"&timeStamp="+timestamp;
		listEnvironmentSelectedProduct();
		currTable = 2;
	}
}
function listEnvironmentSelectedProduct(){
	
	try{
		if ($('#jTableContainerenvironment').length>0) {
			 $('#jTableContainerenvironment').jtable('destroy'); 
		}
		}catch(e){}
		$('#jTableContainerenvironment').jtable({
	         
	         title: 'Environment',
	         selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: 10, //Set page size (default: 10)
	         editinline:{enable:true},
	      /*    
	 		toolbar : {
	 			items : [
	 				
	 				{
	 					text : "Environment Combination",
	 					click : function() {
	 						listEnvCombinationByProduct();
	 					}
	 				} ,
	 		]
	 		}, */
	         actions: {
	             listAction: urlToGetEnvironmentOfSpecifiedProductId,
	             createAction: 'administration.product.environment.add',
	             editinlineAction: 'administration.product.environment.update'
	            // deleteAction: 'administration.product.environment.delete'
	         },
	         fields: {	           
	            productMasterId: { 
        			type: 'hidden',  
        			list:false,
        			defaultValue: productId
        		}, 
	        	environmentId: { 
       				key: true,
       				type: 'hidden',
       				create: false, 
       				edit: false, 
       				list: false
	        	}, 
	        	environmentName: { 
	            	title: 'Name',
	            	inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
	            	width: "20%",
	            	list:true,
	            	edit:false
	        	},
	        	description: { 
	            	title: 'Description' ,
	            	width: "35%",  
	            		list:true
	            }, 
	            environmentGroupId: { 
	            	title: 'Environment Group Name' ,
	            	width: "35%",  
	            	inputTitle: 'Environment Group Name <font color="#efd125" size="4px">*</font>',
	            	edit:false,
	            	 options:'administration.environment.group.list'
	            }, 
	            environmentGroupName: { 
	            	title: 'Environment Group Name' ,
	            	width: "35%",  
	            	list:false,
            		create:false,
            		edit:false
	            },
	            environmentCategoryId: { 
	            	title: 'Environment Category Name' ,
	            	width: "35%",  
	            	edit:false,
	            	dependsOn: 'environmentGroupId',
	            	inputClass: 'validate[required]',
	            	inputTitle: 'Environment Category Name <font color="#efd125" size="4px">*</font>',
	            	 options:function(data){
	            		 if(data.source =='list'){
		            	 	return 'administration.environment.group.category.list?environmentGroupId='+data.record.environmentGroupId;
	            		 }else if(data.source =='create'){
	            			 data.clearCache();
	                		var xx = data.dependedValues.environmentGroupId;
	                		if(xx != undefined){
	                			return 'administration.environment.group.category.list?environmentGroupId='+ data.dependedValues.environmentGroupId;
	                		}else{
	                			return 'administration.environment.group.category.list?environmentGroupId=' +0;
	                		}
	            		 }
	            	 }
	            }, 
	            environmentCategoryDisplayName: { 
	            	title: 'Environment Category Name' ,
	            	width: "35%",  
            		list:false,
            		create:false,
            		edit:false
	            }, 
	            status: {
					title: 'Status' ,
					inputTitle: 'Status <font color="#efd125" size="4px">*</font>',
					width: "10%",  
					list:true,
					edit:true,
					create:false,
					type : 'checkbox',
					values: {'0' : 'No','1' : 'Yes'},
			    	defaultValue: '1'
			    	},
			    createdDate: {            	   
	            	title: 'CreatedDate',
	                create: false, 
    				edit: false, 
    				list: false,
	                width:"20%"      	                	
	        	},
	        	modifiedDate:{
	        		title: 'ModifiedDate',
	                create: false, 
    				edit: false, 
    				list: false,
	                width:"20%"
	        	}
	       },
			 
           //Moved Formcreate validation to Form Submitting
           //Validate form when it is being submitted
            formSubmitting: function (event, data) {        
           	 data.form.find('input[name="environmentName"]').addClass('validate[required]');     
           	 data.form.find('input[name="environmentGroupId"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[15]]');   
           	 data.form.find('input[name="environmentCategoryId"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]');   
           	 
           //  data.form.find('input[name="status"]').addClass('validate[required]');
             data.form.validationEngine();
             return data.form.validationEngine('validate');
           }, 
            //Dispose validation logic when form is closed
            formClosed: function (event, data) {
            	listEnvCombinationByProduct();
               data.form.validationEngine('hide');
               data.form.validationEngine('detach');
           }  
	   });
		 
	   $('#jTableContainerenvironment').jtable('load');		
}
//Environments Scope Ends 
function listEnvCombinationByProduct(){	
	try{
		if ($('#envCombTestRunPlan').length>0) {
			 $('#envCombTestRunPlan').jtable('destroy'); 
		}
		}catch(e){}
	$('#envCombTestRunPlan').jtable({
		title: 'Environment Combination',
        selecting: true,  //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, 
        editinline:{enable:true},
        //toolbarsearch:true,
        actions: {
       	 	listAction: urlToGetEnvironmentCombinationByProduct,
       	    editinlineAction: 'administration.environment.combination.update',
        },  
        fields : {
        	envionmentCombinationId:{
	                 title: 'envionmentCombination Id',
	            	 list:false,
	                 width:"20%"
	             },
	             environmentCombinationName:{
	                 title: 'Environment Combination',
	            	 list:true,
	            	 edit:false,
	                 width:"20%"
	             },
	            /*  productType:{
	                 title: 'Product Type',
	            	 list:true,
	                 width:"20%"
	             }, */
	             envionmentCombinationStatus: {
						title: 'Status' ,
						inputTitle: 'Status <font color="#efd125" size="4px">*</font>',
						width: "10%",  
						list:true,
						edit:true,
						create:false,
						type : 'checkbox',
						values: {'0' : 'No','1' : 'Yes'},
				    	defaultValue: '1'
				},
        	}, 
        	/*recordsLoaded: function(event, data) {			 
        		$('#jtable-toolbarsearch-envionmentCombinationStatus').prop("type", "hidden");  	
        	},*/

		});
	$('#envCombTestRunPlan').jtable('load'); 
	var jscrolheight = $("#envCombTestRunPlan").height();
	var jscrolwidth = $("#envCombTestRunPlan").width();
	  
	$(".jScrollContainer").on("scroll", function() {
		 var lastScrollLeft=0;	
		 var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
		 if (lastScrollLeft < documentScrollLeft) {
		    	$("#v").width($(".jtable").width()).height($(".jtable").height());			
		        lastScrollLeft = documentScrollLeft;
		  }else if(lastScrollLeft >= documentScrollLeft){			
		    	$("#envCombTestRunPlan").width(jscrolwidth).height(jscrolheight);
		  }			
	});
}

function testsuites(nodeType){
	addorno="yes";

	if(nodeType == "Product"){
		//productId = key;
		addorno="yes";
		/*if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");
			return false;
		}else{*/
			addorno="yes";
			//$('#Testsuites').children().show();
			var date = new Date();
		    var timestamp = date.getTime();
			urlToGetTestSuitesOfSpecifiedProductId = 'test.suite.byProduct.list?productMasterId='+productId;
			listTestSuiteSelectedProductVersion(productId, '', timestamp, urlToGetTestSuitesOfSpecifiedProductId, addorno);			
		//}
	}else if(nodeType == "ProductVersion"){
		//$('#Testsuites').children().show();
		addorno = "yes";
		productVersionId = key;
		if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == ''){
			callAlert("Please select the Product Version");
			return false;
		}else{
			var date = new Date();
		    var timestamp = date.getTime();		  
		    urlToGetTestSuitesOfSpecifiedProductVersionId = 'test.suite.byProductVersion.list?versionId='+productVersionId+"&testRunPlanId=-1&runConfigId=-1";
		    listTestSuiteSelectedProductVersion(productId, productVersionId, timestamp, urlToGetTestSuitesOfSpecifiedProductVersionId, addorno);
		}
	}else{
		//$('#Testsuites').children().hide();
		callAlert("Please select the Product or Product Version");
	}	
	
}

//TestSuite Scope Starts

function listTestSuiteSelectedProductVersion(productId, productVersionId, timestamp, urlTestSuite, addorno){	 
	
	try{
		if ($('#jTableContainertestsuite').length>0) {
			 $('#jTableContainertestsuite').jtable('destroy'); 
		}
		}catch(e){}
		
		$('#jTableContainertestsuite').jtable({
	         
	         title: 'Test Suite',
	         selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: 10, //Set page size (default: 10)
	         editinline:{enable:true},
	         //toolbarsearch:true,
	         recordsLoaded: function(event, data) {
				 if(addorno == "no"){
	 		 		//alert("Please Select Test Factory Lab to add TestFactory");
	 		 		$('#jTableContainertestsuite .jtable-toolbar-item-add-record').show();
				 }
	         },
	        toolbar : {
				items : [ 
					{
						icon : '',
						cssClass: 'fa fa-tree',
						click : function() {
							showTestSuiteTestCaseTree();
						}
					}
				]
			},
			actions : {
				listAction: urlTestSuite,
				createAction: 'test.suite.add',
				editinlineAction: 'test.suite.update'
				},
	         fields: {	           
	        	 productId: { 
        			type: 'hidden',  
        			list:false,
        			defaultValue: productId
        		},        		
        		testSuiteId: { 
        			title: 'Test Suite ID',
        			key: true,
       				//type: 'hidden',
        			width: "8%",
       				create: false, 
       				edit: false, 
       				list: true
	        	},
	        	testSuiteCode: { 
	            	title: 'Test Suite Code',
	            	inputTitle: 'Suite Code <font color="#efd125" size="4px">*</font>',
	            	width: "10%",
	            	list:true
	        	},
	        	testSuiteName: { 
	            	title: 'Test Suite Name',
	            	inputTitle: 'Test Suite Name <font color="#efd125" size="4px">*</font>',
	            	width: "10%",
	            	list:true
	        	},
	        	productVersionListId: { 
         			title: 'Version',
         			inputTitle: 'Version <font color="#efd125" size="4px">*</font>',
         			width: "20%",
         			list: true,
         			create : true,
         			edit : true,         			 
         			options: 'common.list.productversion?productId='+ productId,
					inputClass: 'validate[required]',
         			defaultValue : productVersionId
         			
         		},         	
	        	testScriptType: { 
	            	title: 'Script Type' ,
	            	width: "10%",  
	            	list:true,
	            	create : true,
	            	edit : true,
	            	options : 'common.list.scripttype'
	            }, 
	            testScriptSource:{
	            	title: 'Script Source' ,
	            	width: "10%",  
	            	list:true,
	            	create : true,
	            	edit : true,
	            	 options: {
	              		'TAF Server ' : 'TAF Server',
	              		'HPQC_TEST_PLAN' : 'HPQC_TEST_PLAN',
	              		'HPQC_TEST_RESOURCES' : 'HPQC_TEST_RESOURCES',
	              		'JENKINS' : 'JENKINS',
	              		'HUDSON' : 'HUDSON',
	              		},    
	            },
	            testSuiteScriptFileLocation:{
	            	title: 'Script File Location' ,
	            	width: "10%",  
	            	list:true,
	            	create : true,
	            	edit : true,
            	
	            },
	            scriptPlatformLocation:{
        			title: 'Platform',
	            	inputTitle: 'Platform <font color="#efd125" size="4px">*</font>',
	            	width: "10%",
	            	create : true,
         			edit : true,
         			options: {
	              		'Terminal' : 'Terminal',
	              		'Server' : 'Server'
	              		}
        		},
	        	testCaseCount: { 
		            	title: 'Test Case Count',
		            	create : false,
		            	list : true,
		            	edit : false,
		            	inputTitle: 'TestCase Count <font color="#efd125" size="4px">*</font>',
		            	width: "7%"
		        	},		        
		        testStepsCount: { 
			            title: 'Test Steps Count',
			            inputTitle: 'TestSteps Count <font color="#efd125" size="4px">*</font>',
			            width: "10%",
			            create : false,
			            list:true,			            
			            edit : false
			        },
		        /* testCaseName:{
		        	title : 'TestCase',
		        	list : true,
		        	edit : false,
		        	width: "10%"
		        }, */
		         executionTypeId:{
	        		title : 'Execution Type',
	        		width : "15%",
	        		create : true,
	        		list : true,
	        		edit : true,
	      			options:function(data){
	      				if(data.source =='list'){	      				
	      					return 'common.list.executiontypemaster.byentityid?entitymasterid=7';	
	      				}else if(data.source == 'create'){	      				
	      					return 'common.list.executiontypemaster.byentityid?entitymasterid=7';
	      				}
	      			}
	        	},  
	        	
		        testCaseIdMapping:{
		        	title: 'View Test Case',
                	width: "5%",
                	edit: true,
                	create: false,
           	 	display: function (testCaseData) { 
           		//Create an image that will be used to open child table 
           			var $img = $('<img src="css/images/list_metro.png" title="View TestCase" />'); 
           			//Open child table when user clicks the image 
           			$img.click(function () {

						// ----- Closing child table on the same icon click -----
						closeChildTableFlag = closeJtableTableChildContainer($(this), $("#jTableContainertestsuite"));
						if(closeChildTableFlag){
							return;
						}
					
           				$('#jTableContainertestsuite').jtable('openChildTable', 
           				$img.closest('tr'), 
           					{ 
           					title: 'View TestCase', 
           					 editinline:{enable:true},
           					recordsLoaded: function(event, data) {
           						//productVersionId = data.record.productVersionId;
           		        	 $(".jtable-edit-command-button").prop("disabled", true);
           		        	 
           		         },
           					actions: {
           					 listAction: 'test.suite.case.list?testSuiteId='+testCaseData.record.testSuiteId,
        	        		 editinlineAction: 'product.testcase.update'
        	        		// createAction: 'product.testcase.add',
        	        	    // updateAction: 'product.testcase.update',
           							}, 
           	 				 recordUpdated:function(event, data){
                    				$('#jTableContainertestsuite').find('.jtable-child-table-container').jtable('reload');
                				},
                			/*	recordAdded: function (event, data) {
                					$('#jTableContainerproducts').find('.jtable-child-table-container').jtable('reload');
                				},
                				recordDeleted: function (event, data) {
                					$('#jTableContainerproducts').find('.jtable-child-table-container').jtable('reload');
                				}, */
           					fields: {productId:{
           			        	type: 'hidden',  
           		   				list:false,
           		   				defaultValue: productId
           			       			},
           		            testCaseId: { 
           		       			title: 'Test Case ID',
           		        		create:false,
           		        		edit: false,
           		        		list:true,
           		        		key: true
           		       			},
           		       		testCaseCode: { 
           		        		title: 'Test Case Code',  
           		        		width: "15%",                        
           		                create: false,
           		                edit : false,
           		        	},
           		        	testCaseName: {
           		                title: 'Test Case Name',
           		                width: "12%",
           		                create : false,
           		                edit :false/* ,
           		                display: function (data) { 
           		    			return data.record.testCaseName;
           		                }, */
           		        	},
           		        	/* testSuiteId:{
           		        		title: 'Test Suite', 
           		        		width: "7%",
           		        		type : 'hidden',
           		                create: false,
           		                edit : false,
           		                list : false,
           		            //    options: 'common.list.testsuite.byproductId?productId='+ productId,              
           		        	},
           		        	testSuiteName:{
           		        		title: 'TestSuite Name', 
           		        		width: "7%",                         
           		                create: false   
           		        	}, */
           		        	testCaseDescription:{
           		        		title: 'Test Case Description', 
           		        		width: "20%",                         
           		                create: false   
           		        	},
           		        	testCaseExecutionOrder:{
           		        		title : 'Execution Order',
           		        		width : "10%",
           		        		list : true,
           		        		edit : true
           		        	},
           		        	testCaseSource:{
           		        		title: 'Test Case Source', 
           		        		width: "20%",                         
           		                create: false,
           		                list: false,
           		                edit: false
           		        	},           		        	
           		        	testCasePriority:{
           		        		title: 'Test Case Priority',  
           		        		width: "15%",    
           		        		create: false,
           		        		edit : false,
           		        		list : false
           		        	},
           		        	testCaseScriptQualifiedName:{
           		        		title: 'Script Package Name',  
           		        		width: "15%",
           		        		create: false,
           		        		list : false, 
           		        		edit : false
           		        	},
           		        	testCaseScriptFileName:{
           		        		title: 'Script File Name',  
           		        		width: "15%",
           		        		create: false,
           		        		list : false, 
           		        		edit : false
           		        	}, 
           		        	testcaseExecutionType:{
           		        		title: 'Execution Type',  
           		        		width: "15%",
           		        		create: false,
           		        		list : false,           		        		
           		        		options : {
           							'1' : 'Manual',
           							'2' : 'Automated',
           							'3' : 'Mixed'
           						}
           		        	}, 
           		        	executionTypeId:{
           		        		title : 'Execution Type',
           		        		width : "15%",
           		        		create : false,
           		        		list : true,
           		        		edit : false,
           		      			options:function(data){
           		      				if(data.source =='list'){	      				
           		      					return 'common.list.executiontypemaster.byentityid?entitymasterid=3';	
           		      				}else if(data.source == 'create'){	      				
           		      					return 'common.list.executiontypemaster.byentityid?entitymasterid=3';
           		      				}
           		      			}
           		        	},
           		        	testcasePriorityId:{
           		        		title : 'Priority',
           		        		width : "15%",
           		        		create : false,
           		        		list : true,
           		        		edit : false,
           		      			options:function(data){
           		      				if(data.source =='list'){	      				
           		      					return 'common.list.testcasepriority';	
           		      				}else if(data.source == 'create'){	      				
           		      					return 'common.list.testcasepriority';
           		      				}
           		      			}
           		        	},
							tcexecutionSummaryHistory:{
								title : 'Execution History',
								list : true,
								create : false,
								edit : false,
								width: "10%",
								display:function (data) { 
									//Create an image for test script popup 
									var $img = $('<i class="fa fa-history" title="Execution Summary and History"></i>');
									//Open Testscript popup  
									$img.click(function () {
										var dataLevel = "productLevel";
										listTCExecutionSummaryHistoryView(data.record.testCaseId, data.record.testCaseName, dataLevel);
									});
									return $img;
								}
							}, 
           		},
           	
                 formSubmitting: function (event, data) {  
                 data.form.find('input[name="testSuiteName"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]');
                 data.form.validationEngine();
                    return data.form.validationEngine('validate');
                }, 
                 //Dispose validation logic when form is closed
                 formClosed: function (event, data) {
                    data.form.validationEngine('hide');
                    data.form.validationEngine('detach');
                }
           	}, function (data) { //opened handler 
           	data.childTable.jtable('load'); 
           	}); 
           	}); 
           	//Return image 
           	return $img; 
           	}           
		       	 },
				 
				 executeTestCases:{
			        	title : 'Execute',
			        	list : true,			        	
			        	width: "5%",
						 create:false,
		        	     edit:false,
			        	display:function (data) {
                           				
		           		var $img = $('<img src="css/images/execute_metro.png" title="Execute TestCase"  class="exe"  />');		           			 
		           			$img.click(function () {
							testCaseId =	data.record.testCaseId;	
							testCaseExecution = 'TestSuiteExecution';
							testRunPLanListByTestCaseId();
							
								
		           	});
		           			return $img;
			        	}
		          	
		         
		        			  
			        },
				 
		       	downloadTestScripts:{
		        	title : 'Scripts',
		        	list : true,
		        	create : false,
		        	edit : false,
		        	width: "10%",
		        	display:function (data) { 
		           		//Create an image for test script popup 
						var $img = $('<i class="fa fa-file-archive-o showHandCursor" title="Download Skeleton Test Scripts"></i>');
	           			//Open Testscript popup  
	           			$img.click(function () {	           				
							displayDownloadTestScriptsFromTestCases(data.record.testSuiteId, scriptTestSuiteName, scriptDownloadName);
	           		  });
	           			return $img;
		        	}
		        },

		        downloadTestScripts:{
		       				        	title : '',
		       				        	/*list : enableATSGFlag,*//*
		       				        	list : enableATSGFlag,*/
		       				        	create : false,
		       				        	edit : false,
		       				        	width: "6%",
		       				        	display:function (data) {
		       				        		var result = (data.record.testSuiteId+'~'+data.record.testSuiteName+'~'+null).toString();
		       				        		scriptsFor="TestSuite";
											scriptTypeTile = "Test Suite "
		       				        		var $img = $('<img src="css/images/ATSG.JPG" title="ATSG" style="width:50px"/>');
		       			           			$img.click(function () {
		       			           			displayATSGPopupHandler(result); 			           				
		       			           			});
		       			           			return $img;
		       				        		
		       		                       //return $('<a style="color: #0000FF;" href=javascript:displayATSGPopupHandler("'+result+'");>ATSG</a>');
		       								/*var $img = $('<i class="fa fa-file-archive-o" title="Download Skeleton Test Script"></i>');
		       			           			$img.click(function () {
		       			           				displayDownloadTestScriptsFromTestCases(data.record.testCaseId, scriptTestCaseName, scriptDownloadName); 			           				
		       			           			});
		       			           			return $img;*/
		       				        	}
		       				        },
		       /* displayTestScripts:{
		        	title : 'View Scripts',
		        	list : true,
		        	create : false,
		        	edit : false,
		        	width: "10%",
		        	display:function (data) { 
		           		//Create an image for test script popup 
						var $img = $('<i class="fa fa-file-code-o" title="Display Skeleton Test Script"></i>');
	           			//Open Testscript popup  
	           			$img.click(function () {
	           				//displayTestScriptsFromTestCases(data.record.testCaseId,"TestCase"); 			           				
	           				displayDownloadTestScriptsFromTestCases(data.record.testSuiteId, scriptTestSuiteName, scriptViewName);			           				
	           		  });
	           			return $img;
		        	}
		        },
				downloadBDDTestScripts:{
		        	title : 'BDDScripts',
		        	list : isAdmin,  // true only for admin.
		        	create : false,
		        	edit : false,
		        	width: "10%",
		        	display:function (data) { 
		           		//Create an image for test script popup 
						var $img = $('<i class="fa fa-file-archive-o" title="Download BDD Scripts"></i>');
	           			//Open Testscript popup  
	           			$img.click(function () {	           				
	           				downloadBDDScript(data.record.testSuiteId);         				
	           		  });
	           			return $img;
		        	}
		        },*/
		        testCaseId:{
		        	title : 'Mapping',
		        	list : true,
		        	create : false,
		        	edit : false,
		        	width: "10%",
		        	display:function (data) { 
		           		//Create an image that will be used to open child table 
	           			var $img = $('<img src="css/images/mapping.png" title="TestCase Mapping" xdata-toggle="modal" xdata-target="#div_PopupTestcase" />'); 
	           			//Open child table when user clicks the image 
	           			$img.click(function () {
	           				
	           				testcaseMapping(data.record.testSuiteId, data.record.testSuiteName);
	           		  });
	           			return $img;
		        	}
		        },
				auditionHistory:{
					title : 'Audit History',
					list : true,
					create : false,
					edit : false,
					width: "10%",
					display:function (data) { 
						//Create an image for test script popup 
						var $img = $('<i class="fa fa-search-plus" title="Audit History"></i>');
						//Open Testscript popup  
						$img.click(function () {
							listGenericAuditHistory(data.record.testSuiteId,"TestSuite","testSuiteAudit");
						});
						return $img;
					}
				}, 
				commentsTestSuite:{
					title : '',
					list : true,
					create : false,
					edit : false,
					width: "5%",
					display:function (data) { 
						//Create an image for test script popup 
						var $img = $('<i class="fa fa-comments" title="Comments"></i>');
							$img.click(function () {
								var entityTypeIdComments = 7;
								var entityNameComments = "TestSuite";
								listComments(entityTypeIdComments, entityNameComments, data.record.testSuiteId, data.record.testSuiteName, "tSuiteComments");
							});
							return $img;
					}		
				},  
			        
	       },
	      /* recordsLoaded: function(event, data) {			 
	    		$('#jtable-toolbarsearch-testCaseIdMapping').prop("type", "hidden");  
	    		$('#jtable-toolbarsearch-downloadTestScripts').prop("type", "hidden");  	
	    		$('#jtable-toolbarsearch-displayTestScripts').prop("type", "hidden");  	
	    		$('#jtable-toolbarsearch-testCaseId').prop("type", "hidden");  	
	       },*/

			 
           //Moved Formcreate validation to Form Submitting
           //Validate form when it is being submitted
            formSubmitting: function (event, data) { 
              	 data.form.find('input[name="testSuiteCode"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]');   
               	 data.form.find('input[name="testSuiteName"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]'); 

            	data.form.find('input[name="environmentName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[minSize[3]], custom[maxSize[25]]');     
           	 data.form.find('input[name="environmentGroupId"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]');   
           	 data.form.find('input[name="environmentCategoryId"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]'); 
                      	 
           //  data.form.find('input[name="status"]').addClass('validate[required]');
             data.form.validationEngine();
             return data.form.validationEngine('validate');
           }, 
            //Dispose validation logic when form is closed
            formClosed: function (event, data) {
               data.form.validationEngine('hide');
               data.form.validationEngine('detach');
           }  
	   });
	
		$('#jTableContainertestsuite').jtable('load');	
}

// Test suite for Test Plan - Test Plan Tile view starts
var testRunplanId = '';
function listTestSuiteSelectedProductVersionTestRunPlan(productId, productVersionId, timestamp,type,testRunPlanId){
	try{
		productId = productId;
		productVersionId = productVersionId;
		testRunplanId = testRunPlanId;
		if ($('#testsuiteTestRunPlan').length>0) {
			 $('#testsuiteTestRunPlan').jtable('destroy'); 
		}
		}catch(e){}
		$('#testsuiteTestRunPlan').jtable({
	         
	         title: 'TestSuite',
	          
	         paging: true, //Enable paging
	         pageSize: 10, //Set page size (default: 10)
	         selecting: true, //Enable selecting	
			multiselect : false, //Allow multiple selecting
	 		selectingCheckboxes : false, //Show checkboxes on first column
	         editinline:{enable:true},
	         actions: {
	        	 listAction: 'test.suite.byProductVersion.list?versionId='+productVersionId+"&timeStamp="+timestamp+"&testRunPlanId=-1&runConfigId=-1",	        		
	           },
	         fields: {	           
	        	 productId: { 
        			type: 'hidden',  
        			list:false,
        			defaultValue: productId
        		}, 
        		isSelected: {
                     title: 'Selected',
                     list:true,
                     create: false,
                     edit:true,
                     type : 'checkbox',
                       values: {'0' : 'No','1' : 'Yes'},
                        display:function(data){
                               if(data.record.isSelected==1){
                                    
                                   return '<input type="checkbox" id="status" class="testsuite_checkbox" checked onclick=change_testsuite_checkbox_status(0,'+data.record.testSuiteId+',this); value='+data.record.testSuiteId+'>';
                                  }else if(data.record.isSelected==0){

                                         return '<input type="checkbox" id="status" class="testsuite_checkbox"  onclick=change_testsuite_checkbox_status(1,'+data.record.testSuiteId+',this); value='+data.record.testSuiteId+'>';
                                  }else{
                                       return '<input type="checkbox" id="status" class="testsuite_checkbox"  onclick=change_testsuite_checkbox_status(1,'+data.record.testSuiteId+',this); value='+data.record.testSuiteId+'>';
                                  }
                         }
        		 },

        		testSuiteId: { 
       				key: true,
       				type: 'hidden',
       				create: false, 
       				edit: false, 
       				list: false
	        	}, 
	        	testSuiteName: { 
	            	title: 'TestSuite Name',
	            	inputTitle: 'TestSuite Name <font color="#efd125" size="4px">*</font>',
	            	width: "10%",
	            	list:true
	        	},
	        	productVersionListId: { 
         			type: 'hidden',
         			title: 'Version ID',
         			list: true,
         			edit : false,         			 
         			defaultValue : productVersionId
         		},
         		productVersionListName:{
         			title :'Version',
         			list : false,
         			create : false,
         			edit : false,         			
         		},
	        	testScriptType: { 
	            	title: 'ScriptType' ,
	            	width: "10%",  
	            	list:true,
	            	create : true,
	            	edit : false,
	            	options : 'common.list.scripttype'
	            }, 
	            testScriptSource:{
	            	title: 'Script Source' ,
	            	width: "10%",  
	            	list:true,
	            	create : true,
	            	edit : false,
            	
	            },
	            testSuiteScriptFileLocation:{
	            	title: 'Script File Location' ,
	            	width: "10%",  
	            	list:true,
	            	create : true,
	            	edit : false,
            	
	            },
	            testSuiteCode: { 
	            	title: 'Suite Code',
	            	inputTitle: 'Suite Code <font color="#efd125" size="4px">*</font>',
	            	width: "10%",
	            	list:true
	        	},
	        	testCaseCount: { 
		            	title: 'Test Case Count',
		            	create : false,
		            	list : true,
		            	edit : false,
		            	inputTitle: 'Test Case Count <font color="#efd125" size="4px">*</font>',
		            	width: "7%"
		        	},		        
		        testStepsCount: { 
			            title: 'Test Steps Count',
			            inputTitle: 'Test Steps Count <font color="#efd125" size="4px">*</font>',
			            width: "10%",
			            create : false,
			            list:true,			            
			            edit : false
			        },
		        /* testCaseName:{
		        	title : 'TestCase',
		        	list : true,
		        	edit : false,
		        	width: "10%"
		        }, */
		         executionTypeId:{
	        		title : 'Execution Type',
	        		width : "15%",
	        		create : true,
	        		list : true,
	        		edit : true,
	      			options:function(data){
	      				if(data.source =='list'){	      				
	      					return 'common.list.executiontypemaster.byentityid?entitymasterid=7';	
	      				}else if(data.source == 'create'){	      				
	      					return 'common.list.executiontypemaster.byentityid?entitymasterid=7';
	      				}
	      			}
	        	},  
	        	
		        testCaseIdMapping:{
		        	title: 'View TestCase',
                	width: "5%",
                	edit: false,
                	create: false,
           	 	display: function (testCaseData) { 
           		//Create an image that will be used to open child table 
           			var $img = $('<img src="css/images/list_metro.png" title="View TestCase" />'); 
           			//Open child table when user clicks the image 
           			$img.click(function () { 
           				$('#testsuiteTestRunPlan').jtable('openChildTable', 
           				$img.closest('tr'), 
           					{ 
           					title: 'View TestCase', 
           					 editinline:{enable:true},
           					 paging: true, //Enable paging
           			         pageSize: 10, //Set page size (default: 10)
           					recordsLoaded: function(event, data) {
           		        	 $(".jtable-edit-command-button").prop("disabled", true);
           		         },
           					actions: { 
           						//listAction: 'administration.product.build.list?productVersionListId=' + productVersionData.record.productVersionListId ,           						            					
           						//createAction: 'administration.product.build.add'
           					 listAction: 'test.suite.case.testrunplan.list?testSuiteId='+testCaseData.record.testSuiteId+"&testRunPlanId="+testRunPlanId,
        	        		// createAction: 'product.testcase.add',
        	        	    // updateAction: 'product.testcase.update',
           							}, 
           	 				/* recordUpdated:function(event, data){
           	 					$('#testsuiteTestRunPlan').find('.jtable-child-table-container').jtable('reload');
                				},
                				recordAdded: function (event, data) {
                					$('#jTableContainerproducts').find('.jtable-child-table-container').jtable('reload');
                				},
                				recordDeleted: function (event, data) {
                					$('#jTableContainerproducts').find('.jtable-child-table-container').jtable('reload');
                				}, */
           					fields: {productId:{
           			        	type: 'hidden',  
           		   				list:false,
           		   				defaultValue: productId
           			       			},
           			       			
           			       		isSelected: {
           	                     title: 'Selected',
           	                     list:true,
           	                     create: false,
           	                     edit:true,
           	                     type : 'checkbox',
           	                       values: {'0' : 'No','1' : 'Yes'},
           	                        display:function(data){
     	                               if(data.record.isSelected==1){
     	                                   return '<input type="checkbox" id="status" checked onclick=saveTestSuiteTestCaseMap(0,'+data.record.testCaseId+','+testCaseData.record.testSuiteId+','+testCaseData.record.isSelected+'); value='+data.record.status+'>';
     	                                  }else if(data.record.isSelected==0){
     	                                         return '<input type="checkbox" id="status" onclick=saveTestSuiteTestCaseMap(1,'+data.record.testCaseId+','+testCaseData.record.testSuiteId+','+testCaseData.record.isSelected+'); value='+data.record.status+'>';
     	                                  }/*else{
     	                                       return '<input type="checkbox" id="status" onclick=saveTestSuiteTestCaseMap(1,'+data.record.testCaseId+','+testCaseData.record.testSuiteId+','+testCaseData.record.isSelected+'); value='+data.record.status+'>';
     	                                  }*/
     	                         }
         	        		 },

           		            testCaseId: { 
           		       			title: 'Test Case ID',
           		        		create:false,
           		        		edit: false,
           		        		list:true,
           		        		key: true
           		       			},        	
           		        	testCaseName: {
           		                title: 'Test Case Name',
           		                width: "12%",
           		                create : false,
           		                edit :false/* ,
           		                display: function (data) { 
           		    			return data.record.testCaseName;
           		                }, */
           		        	},
           		        	/* testSuiteId:{
           		        		title: 'Test Suite', 
           		        		width: "7%",
           		        		type : 'hidden',
           		                create: false,
           		                edit : false,
           		                list : false,
           		            //    options: 'common.list.testsuite.byproductId?productId='+ productId,              
           		        	},
           		        	testSuiteName:{
           		        		title: 'TestSuite Name', 
           		        		width: "7%",                         
           		                create: false   
           		        	}, */
           		        	testCaseDescription:{
           		        		title: 'Test Case Description', 
           		        		width: "20%",                         
           		                create: false   
           		        	},
           		        	testCaseSource:{
           		        		title: 'Test Case Source', 
           		        		width: "20%",                         
           		                create: false,
           		                list: false,
           		                edit: false
           		        	},
           		        	testCaseCode: { 
           		        		title: 'Test Case Code',  
           		        		width: "15%",                        
           		                create: false,
           		                edit : false,
           		        	},
           		        	testCasePriority:{
           		        		title: 'Test Case Priority',  
           		        		width: "15%",    
           		        		create: false,
           		        		edit : false,
           		        		list : false
           		        	},
           		        	testCaseScriptQualifiedName:{
           		        		title: 'Script Package Name',  
           		        		width: "15%",
           		        		create: false,
           		        		list : false, 
           		        		edit : false
           		        	},
           		        	testCaseScriptFileName:{
           		        		title: 'Script File Name',  
           		        		width: "15%",
           		        		create: false,
           		        		list : false, 
           		        		edit : false
           		        	}, 
           		        	testcaseExecutionType:{
           		        		title: 'Execution Type',  
           		        		width: "15%",
           		        		create: false,
           		        		list : false,           		        		
           		        		options : {
           							'1' : 'Manual',
           							'2' : 'Automated',
           							'3' : 'Mixed'
           						}
           		        	}, 
           		        	executionTypeId:{
           		        		title : 'Execution Type',
           		        		width : "15%",
           		        		create : false,
           		        		list : true,
           		        		edit : false,
           		      			options:function(data){
           		      				if(data.source =='list'){	      				
           		      					return 'common.list.executiontypemaster.byentityid?entitymasterid=3';	
           		      				}else if(data.source == 'create'){	      				
           		      					return 'common.list.executiontypemaster.byentityid?entitymasterid=3';
           		      				}
           		      			}
           		        	}, 
           		        	testcasePriorityId:{
           		        		title : 'Priority',
           		        		width : "15%",
           		        		create : false,
           		        		list : true,
           		        		edit : false,
           		      			options:function(data){
           		      				if(data.source =='list'){	      				
           		      					return 'common.list.testcasepriority';	
           		      				}else if(data.source == 'create'){	      				
           		      					return 'common.list.testcasepriority';
           		      				}
           		      			}
           		        	}, 
           		},
           	
                 formSubmitting: function (event, data) {               	
                     data.form.validationEngine();
                    return data.form.validationEngine('validate');
                }, 
                 //Dispose validation logic when form is closed
                 formClosed: function (event, data) {
                    data.form.validationEngine('hide');
                    data.form.validationEngine('detach');
                }
           	}, function (data) { //opened handler 
           	data.childTable.jtable('load'); 
           	}); 
           	}); 
           	//Return image 
           	return $img; 
           	}           
		       	 },		        
	       },          
           //Moved Formcreate validation to Form Submitting
           //Validate form when it is being submitted
            formSubmitting: function (event, data) {          	 
           //  data.form.find('input[name="status"]').addClass('validate[required]');
             data.form.validationEngine();
             return data.form.validationEngine('validate');
           }, 
		   recordsLoaded:function(event,data){
			   if($('.testsuite_checkbox:checked').length==0){
				  $('.testsuite_checkbox').eq(0).click(); 
				   
			   }
		   },
            //Dispose validation logic when form is closed
            formClosed: function (event, data) {
               data.form.validationEngine('hide');
               data.form.validationEngine('detach');
           }           
	   });		 
	   $('#testsuiteTestRunPlan').jtable('load');
}

function change_testsuite_checkbox_status(flag,testSuiteListId,self){	
	
	if(productId == undefined || productId == 0){
		callAlert("Please select Product");
		return false;
	}
	
	if(productVersionId == undefined || productVersionId == 0){
		callAlert("Please select Product Version");
		return false;
	}
	
	if(testRunplanId == undefined || testRunplanId == 0){
		callAlert("Please select Test Plan");
		return false;
	}
	
	if(!$(self).is(':checked')){
		$(self).prop("checked",true);
		//return false;
	}	
	$('.testsuite_checkbox').prop("checked",false);	
	$(self).prop("checked",true);
		
	var url="administration.testsuite.mapunmaprunConfiguration?productversionId="+productVersionId+"&productId="+productId +"&testRunPlanId="+testRunplanId+"&testSuiteId="+testSuiteListId+"&type=Add";
	 $.ajax({
 		    type: "POST",
 		    url: url,
			async:false,
 		    success: function(data) { 		    	
 		    	if(data.Result=='ERROR'){
 		    		callAlert(data.Message);
 		    		//listTestSuiteSelectedProductVersionTestRunPlan(productId, productVersionId, timestamp,type,testRunplanId);
	 		    	return false;
 		    	}else{
 		    		callAlert(data.Message);
 		    		//listTestSuiteSelectedProductVersionTestRunPlan(productId, productVersionId, timestamp,type,testRunplanId);
	 		    	return true;
 		    	}
 		    },    
 		    dataType: "json" 		    
 	});
}
//Test suite for Test Plan - Test Plan Tile view ends

function testRunPlanConfigurationProperties(testRunPlanId){
	rnCnfgFlag = false;
	tpcId = testRunPlanId;
	var url = 'entityConfigureProperties.list?entityId='+ tpcId +'&entityMasterId=9&jtStartIndex=0&jtPageSize=10';
	testPlanDataTable("TPConfigurationPropertiesTable",url);
	$("#testPlanConfigureDT_Child_Container").modal();
}
//END - Testrunplan Tile View


function leftItemDislayListItemRecentBuilds(item, jsonObj){
	var resultList="";
	var entity_id = item.productBuildName;
	var entity_name = item.productName;	
	var build_date = item.productBuildDate;	
	var entity_dispname = '';
		
	if(jsonObj.componentUsageTitle == jsonTestCaseTabObj.componentUsageTitle){	
		entity_dispname = entity_id+" ~ "+entity_name+" ~ "+build_date;
		entity_dispname=trim(entity_dispname);
		resultList = "<li pdtId='"+item.productId+"' pdtBldId='"+item.productBuildId+"' pdtName='"+item.productName+"' pdtBldName='"+item.productBuildName+"' title='"+item.productBuildName+"' style='color: black;cursor:default;user-select:none;'>"+entity_dispname+" ~ <img src='css/images/testplan.png' style='width:18px;height:20px;margin-top:-2px;margin-left:10px;' class='testPlanMappingImg' onclick='testPlanMapping("+item.productId+")' title='Test Plans' /><img style='padding-left:15px;' src='css/images/analytics.jpg' class='details-control pdtBuildImg' onclick='pdtBuild(\""+item.productId+"\",\""+item.productBuildId+"\",\""+item.productName+"\",\""+item.productBuildName+"\")' title='Test Plan Analysis'></li>";
	}
	
	return resultList;	
}

function leftItemDislayListItemWorkpackage(item, jsonObj){
	var resultList="";
	var wpName = item.workPackageName;
	var pdtName = item.productName;	
	var wpResult = item.result;	
	var entity_dispname = '';
		
	if(jsonObj.componentUsageTitle == jsonTestCaseTabObj.componentUsageTitle){	
		entity_dispname = wpName+" ~ "+pdtName+" ~ "+wpResult;
		entity_dispname=trim(entity_dispname);
		resultList = "<li pdtId='"+item.productId+"' pdtBldId='"+item.productBuildId+"' pdtName='"+item.productName+"' pdtBldName='"+item.productBuildName+"' title='"+item.workPackageName+"' style='color: black;cursor:default;user-select:none;'>"+entity_dispname+"</li>";
	}
	
	return resultList;	
}

//$(document).on('click','.testPlanMappingImg', function() {
function testPlanMapping(pdtId){
	var jsonObj={
		"url":'administration.testrunplan.listbytestFactorProductorVersion?productversionId=-1&productId='+pdtId+"&testFactoryId=-1"
	};
	$('#tpTileView_Child_Container').modal();
	TestPlanGridView.init(jsonObj);
}

//$(document).on('click','.pdtBuildImg', function() {
function pdtBuild(pdtId,pdtBldId,pdtName,pdtBldName){	
	var url='getISERecommended.Testcases.by.buildId?buildId='+pdtBldId;
	var title = "Product Name:"+pdtName+" - Build Name:"+pdtBldName+" - Recommended Test Plan";
	var jsonObj={"Title":title,		
			"listURL": url,					
			"componentUsageTitle":"recommendedTestPlan",
			"productId": pdtId,
			"buildId" : pdtBldId,
			"componentUsageTitle": "ProductFeature"
	};
	RecommentedTestPlan.init(jsonObj);
	$("#recommendedTestPlanPdMgmContainer").modal();
}


function trim(str) {
    return str.replace(/^\s+|\s+$/g,"");
}

var handleTitle = function(tab, navigation, index) {
    var total = navigation.find('li').length;
    var current = index + 1;
    // set done steps
    jQuery('li', $('#form_wizard_1')).removeClass("done");
    var li_list = navigation.find('li');
    for (var i = 0; i < index; i++) {
        jQuery(li_list[i]).addClass("done");
    }
    $('#form_wizard_1').find('.button-submit').hide();
    $('#form_wizard_1').find('.button-next').removeClass('disabled');
    $('#form_wizard_1').find('.button-previous').removeClass('disabled');

    if (current == 1) {
        $('#form_wizard_1').find('.button-submit').show();
        $('#form_wizard_1').find('.button-previous').hide();
    } else {
        $('#form_wizard_1').find('.button-previous').show();
    }

    if (current >= total) {
        $('#form_wizard_1').find('.button-next').hide();
    } else {
        $('#form_wizard_1').find('.button-next').show();
    }
    //Metronic.scrollTo($('.page-title'));
}

function testCaseDetailsForResult(event){
	 var rawdata = event.target.id;
		var splitdata = rawdata.split(/[~@]+/);
		var tcexeId = splitdata[0];
		var testCaseId = splitdata[1];
		var testCaseName = splitdata[2];
	var jsonObj={"Title":"Testcase Details :- "+ testCaseId + "- "+testCaseName,
			     "testCaseID":testCaseId,
				 "testCaseName":testCaseName ,				 
				 "testCaseDetailsTitle":"Details",
				 "executionId":tcexeId,
				 "testCaseDetailsUrl":"workpackage.result.testcase.details?testCaseExecutionResultId="+tcexeId+"&testcaseId="+testCaseId,
	};
	TestCaseDetails.init(jsonObj);	
}

function popupTestcaseClose() {	
	$("#div_PopupTestcase").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

function scheduleUsingCronGen(title, subTitle, testRunPlanID, rowType){	
	var	jsonTestCaseTabObj={"Title": title,	
							"SubTitle": subTitle,
							"rowID": testRunPlanID,
							"rowType": rowType,
		};
	
	 Scheduling.init(jsonTestCaseTabObj);	
}

function showTestSuiteTestCaseTree(){
	// --- start of testing TreePagination -------
	console.log("jtable : "+$('#jTableContainertestsuite').length);
    if ($('#jTableContainertestsuite table tbody tr').length==1 && 
		 $('#jTableContainertestsuite table tbody tr').text() == "No data available!") {			  
		callAlert("No Data Available");
	}else{
	 var headerName = "Test Suite Test Cases Mapping : Product ID : "+productId +" Name : "+document.getElementById("hdnProductName").value;
	 if($("#hdnProductId").val() == "")$("#hdnProductId").val(productId);
	 var jsonObj={"Title":headerName,			          
	    			"urlToGetTree": 'product.testsuite.testcase.mapping.tree?productId='+productId,
	    			"urlToGetChildData": 'sub.testsuite.list.of.parent?parentId=',	    			
	    			};	 
	 TreePagination.init(jsonObj);
   }
 // ---- end of testing TreePagination ------
}

//Workpackage Execution Summary Starts
var test = '';
var workPackageJsonData='';
var testRunJobJsonData='';
function assignWPDataTableValues(url,tableValue, row, tr){
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				testRunJobJsonData = data;
				data = data.Records;
			}
			
			if(tableValue == "parentTable"){
				workPackageJsonData = data;
				if(!workPackageDTFlag)
					workPackage_Container(data, "240px");
				else				
					reloadDataTableHandler(data, workPackage_oTable);
				
			}else if(tableValue == "childTable1"){
				wpAuditHistory_Container(data, row, tr);
			}else if(tableValue == "childTable2"){
				var gridViewFlag = $($('#workPacakgeRadioGroup label')[0]).hasClass('active');
				if(gridViewFlag){
					wpTestRunJobs_Container(data, row, tr);
				}else{
					testRunJobsTileView(testRunJobJsonData);
				}
			
			}else if(tableValue == "childTable3"){
				wpTestRunCases_Container(data, row, tr);				
			}else if(tableValue == "childTable4"){
				wpAuditHistory_Container(data, row, tr);
			}
			else{
				console.log("no child");
			}
			
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function testRunJobsTileView(data){	
	$('#wpTestRunJobDTContainerTileView').empty();	 
	 var availability = "bg-white"; 
	 var jobId;
	 if(data.Records.length!=0){	            
        for(var i=0;i<data.Records.length;i++){
         	var imgSrc="";
     		imgSrc="css/images/workpackage1.png";
         	var nameBot = data.Records[i].workPackageName;
         	var jobStatus = 'Available';//data[key].botStatus;
         	if(jobStatus == null) {
         		jobStatus="";
         	}
         	var titleSchedule;
        	var subTitleSchedule = "Product ";//+$(headerTitle).text();      	
        	var startDate = data.Records[i].firstActualExecutionDate;
        	if(startDate != null || startDate != undefined){
        		startDate = '<span title="'+data.Records[i].firstActualExecutionDate+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >'+ data.Records[i].firstActualExecutionDate +'<br /> </span>';
        	}else{
        		startDate = '';
        	}        	
			
			var passPercentage = 0;			
			if(data.Records[i].totalTestCaseCount == 0){
				passPercentage=0;
			}else{
				passPercentage = (data.Records[i].passedCount * 100)/(data.Records[i].totalTestCaseCount);
				passPercentage = passPercentage.toFixed(0);
			}
			
			var failedPercentage = 0;			
			if(data.Records[i].totalTestCaseCount == 0){
				failedPercentage=0;
			}else{
				failedPercentage = (data.Records[i].failedCount * 100)/(data.Records[i].totalTestCaseCount);
				failedPercentage = failedPercentage.toFixed(0);
			}
			var notExecuted = data.Records[i].totalTestCaseCount - (data.Records[i].passedCount +  data.Records[i].failedCount);
			var notExecutedPercentage = 0;			
			if(data.Records[i].totalTestCaseCount == 0){
				notExecutedPercentage=0;
			}else{
				notExecutedPercentage = (notExecuted * 100)/(data.Records[i].totalTestCaseCount);
				notExecutedPercentage = notExecutedPercentage.toFixed(0);
			}
			var jobResult = "";
			if(data.Records[i].result != null || data.Records[i].result != undefined ){
				jobResult = data.Records[i].result;
			}else{
				if(data.Records[i].testRunStatusName == "Executing"){
					if(data.Records[i].failedCount > 0)
						jobResult = "Failed";
					else if(data.Records[i].passedCount > 0)
						jobResult = "Passed [Intermediate]";
					else
						jobResult = "---";
				}else
					jobResult = "---";
			}

			var isReport;
			var entityTypeIdComments = 10;
			var entityNameComments = "TestJob";
			var testRunJobComments = "testJobComments";
			isReport = '<span style="float: right;padding: 0px 0px;"><img width="13px" height="13px" class="exe" src="css/images/list_metro.png" style="color:black;cursor:pointer;margin-right:0px;margin-top:2px;" title="Test Case" onclick="jobTileTestRunCaseHandler(\''+data.Records[i].testRunJobId+'\',null)"></img></span><br/>'+	
    					'<span style="float: right;padding: 0px 2px;"><i class="fa fa-comments" style="color:black;cursor:pointer" title="Comments" onclick="listComments(\''+entityTypeIdComments+'\',\''+entityNameComments+'\',\''+data.Records[i].testRunJobId+'\',\''+data.Records[i].testRunStatusName+'\',\''+testRunJobComments+'\')"></i></span><br/>';
			if((passPercentage != 'NaN' && passPercentage != 0 )|| (failedPercentage != 'NaN' && failedPercentage != 0)){
				isReport += /*'<span style="float: right;padding: 0px 2px;"><img width="13px" height="13px" class="exe" src="css/images/pdf_type1.png" style="color:black;cursor:pointer;margin-right:0px;margin-top:2px;"  title="Job Evidence PDF Report" onclick="exportWPResultsTestRunJob(\''+data.Records[i].testRunJobId+'\')"></img></span><br/>'+*/
				'<span style="color:black;"><i class="fa fa-desktop" title="Job Evidence HTML Report"  style="cursor:pointer;font-size:15px;padding-top:5px;margin-left:3px;" onclick="exportWPResultshtmlEvidenceTestRunJob('+data.Records[i].testRunJobId+');"/></span><br/>';
			}
			var liveLog = '<span style="float: right;padding: 0px 2px;"><i class="fa fa-file-text" style="color:black;cursor:pointer" title="Live log" onclick="liveLog('+data.Records[i].testRunJobId+','+lastLine+')"></i></span><br/>';
			testjobid = data.Records[i].testRunJobId;
			var isResultBadge='';
			if(jobResult == "Passed"){
				isResultBadge += '<span title="'+jobResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : </span>';
				isResultBadge += '<span id="" class="badge badge-default" style="background: #076;margin-top: 10px;margin-bottom: 10px;display: inline;margin-right: 5px;">'+jobResult+' <br /> </span>';
			}else if(jobResult == "Failed"){
				isResultBadge += '<span title="'+jobResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : </span>';
				isResultBadge += '<span id="" class="badge badge-default" style="background: rgb(231, 80, 90);margin-top: 10px;margin-bottom: 10px;display: inline;">'+jobResult+' <br /> </span>';
			}else if(jobResult == "---"){
				isResultBadge += '<span title="'+jobResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Result : </span>';
				isResultBadge += '<span id="" class="badge badge-default" style="background: rgb(255, 214, 26);margin-top: 10px;margin-bottom: 10px;display: inline;">In Progress <br /> </span>';
			}
			

    	
        	var brick = '<div class="'+"tile "+ availability + '" style="border:#ddd solid 1px;width:240px !important;height:210px !important;cursor:default;"><div class="tile-body" style="padding:3px 3px;">' +
    	    '<div class="row">'+
    	    	'<div style="position:absolute; float:left;border-right: solid 0px #ddd;"><img width="60px" height="60px" title="'+data.Records[i].workPackageName+'" src="'+ imgSrc +'" onerror="this.src=\'css/images/noimage.jpg\'"></img></div>'+
    	    	'<div style="float:right;">'+ isReport + liveLog + 	    		
    	    '</div>'+
    	    '<div class="row" style="text-align:left;margin-top:75px;overflow:hidden;">'+
    	    '<span title="Job Id" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >'+ "Job Id : "+data.Records[i].testRunJobId+'<br /> </span>'+
    	     startDate+    	    
    	    '<span title="'+data.Records[i].testRunStatusName+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Status : '+ data.Records[i].testRunStatusName +'<br /> </span>'+
    	    isResultBadge +
    	    '<span title="'+jobResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;display:block;" >Total Test Case : '+ data.Records[i].totalTestCaseCount +'<br /> </span>'+
			'<span id="passedTestCases" class="badge badge-default" style="background: #076;margin-top: 10px;margin-bottom: 10px;display: inline;margin-right: 5px;">'+data.Records[i].passedCount+' ['+passPercentage+'%]</span>'+
			'<span id="failedTestCases" class="badge badge-default" style="background: rgb(231, 80, 90);margin-top: 10px;margin-bottom: 10px;display: inline;">'+data.Records[i].failedCount+' ['+failedPercentage+'%]</span>'+
			'<span id="notExecutedTestCases" class="badge badge-default" style="background: rgb(112,128,144);margin-top: 10px;margin-bottom: 10px;display: inline;">'+notExecuted+' ['+notExecutedPercentage+'%]</span>'+
    	    '</div>'+    	  
    	    '</div>'+
    	    '</div>';    	    
	 		$('#wpTestRunJobDTContainerTileView').append($(brick));
         }
	 }else{
		 var brickcontainer = '<div class="text-center">No data available!</div>';
		 $('#wpTestRunJobDTContainerTileView').append($(brickcontainer));
	 }
	 
	 if(!($('#wpTestRunJobDTContainerTileView').hasClass('tiles'))){
			$('#wpTestRunJobDTContainerTileView').addClass('tiles').css("margin-top","10px");
	 } 
}
function jobTileTestRunCaseHandler(row, tr){
	var url = 'testcasesexecution.of.testrunjob.Id?testRunJobId='+row+'&jtStartIndex=0&jtPageSize=1000';
	assignWPDataTableValues(url, "childTable3", row, "null");
	$("#wpTestRunCaseContainer").modal();
	$("#wpTestRunCaseContainer h4").text(row+' - Test cases');
}

function wpTestRunCases_Container(data, row, tr){
	wpkResults_Container(data, "350px");
}

// -----

function wpkResults_Container(data, scrollYValue){
	try{
		if ($('#workPackageTestRun_dataTable').length>0) {
			$('#workPackageTestRun_dataTable').dataTable().fnDestroy(); 
		}
	} catch(e) {}
	
	 wkpResults_oTable = $('#workPackageTestRun_dataTable').dataTable( {
		 "dom": '<"top"Bf<"clear">>rt<"bottom"ip<"clear">>',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"bScrollCollapse": true,
		autoWidth: false,
		bAutoWidth:false,
		"sScrollX": "100%",
       "sScrollXInner": "100%",
       "scrollY":"100%",
       fixedColumns:   {
           leftColumns: 2,
           rightColumns: 1
       }, 
       "fnInitComplete": function(data) {  
    	   var searchcolumnVisibleIndex = [12]; // search column TextBox Invisible Column position
    	   $('#workPackageTestRun_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
	    	    var i=$(this).index();
	    	    var flag=false;
	    	    for(var j=0;j<searchcolumnVisibleIndex.length;j++){
	    	    	if(i == searchcolumnVisibleIndex[j]){
	    	    		flag=true;
	    	    		break;
	    	    	}
	    	    }
	    	    if(!flag){
	    	    	$(this).html('');
	    	    	$(this).append( '<input type="text" name="'+data.aoColumns[i].mData+'" value="" style="width:100%" class="search_init" />');
	    	    }
       	   });	  
    	   reInitializeWPKDataTableTC();
	   },  
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'Test Case Result',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Test Case Result',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Test Case Result',
	                    	exportOptions: {
	                            columns: ':visible'
	                        },
	                        orientation: 'landscape',
	                        pageSize: 'LEGAL'
	                    },	                    
	                ],	                
	            },
	            'colvis',
         ], 
         columnDefs: [
              { targets: [0,1,2,3,4,5,6,7,8,9,10,11,12], visible: true},
              { targets: '_all', visible: false }
          ],
                  
         aaData:data,                 
	    aoColumns: [	        
	        { mData: "testcaseId", sWidth: '7%', "render": function (tcData,type,full) {
	        	var dataLevel = "workPackageLevel";
				var tcId = full.testcaseId;
				var tcName = full.testcaseName;						
				var result = tcId+'~@'+dataLevel+'~@'+tcName;						
				document.getElementById("treeHdnCurrentWorkPackageId").value = workpackageId;
				return ('<a style="color: #0000FF;" id="'+result+'" onclick="listTCExecutionSummaryHistory(event)">'+tcId+'</a>');
	        	},
	        },	        
	       { mData: "testCaseExecutionResultId", sWidth: '7%', "render": function (tcData,type,full) {	        
		        var exeId = full.testCaseExecutionResultId;
				var tcId = full.testcaseId;
				var tcName = full.testcaseName;
				var result = exeId+'~@'+tcId+'~@'+tcName;		
	    		return ('<a style="color: #0000FF;" id="'+result+'" onclick="testCaseDetailsForResult(event)">'+exeId+'</a>');		        
		        },
	        },	        
           { mData: "testcaseName", className: 'disableEditInline' , sWidth: '10%'},		    				            
           { mData: "testcaseCode", className: 'disableEditInline' , sWidth: '7%'},		
           { mData: "startTime", className: 'disableEditInline' , sWidth: '7%'},		
           { mData: "endTime", className: 'disableEditInline' , sWidth: '7%'},	
           { mData: "testerName", className: 'disableEditInline' , sWidth: '10%'},		
           { mData: "result", className: 'disableEditInline' , sWidth: '10%'},	
           { mData: "comments", className: 'disableEditInline' , sWidth: '10%'},
           { mData: "runConfigurationName", className: 'disableEditInline' , sWidth: '10%'},
           { mData: "teststepcount", className: 'disableEditInline' , sWidth: '5%'},
           { mData: "defectsCount", className: 'disableEditInline' , sWidth: '6%'},          
           { 
	    	   mData: '',				 
	    	   sWidth: '2%',
			   "orderable":      false,
			   "data":           data,
           	   mRender: function(data, type, full) {				            	
	       		var img = ('<div style="display: flex;">'+
	       		'<button style="border: none; background-color: transparent; outline: none;">'+
	       				'<img src="css/images/list_metro.png" title="Test Step" onclick="wpkTestCaseDetailsTSResult(event)" style="margin-left: 5px;"></button>'+
	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
	       				'<img src="css/images/list_metro.png" title="Export System Details" onclick="wpkTestCaseDetailsExportResult(event)"></button>'+
	       		'</div>');	      		
          		 return img;
           	}
           },
       ],
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },
	});
	 
	//-----------------
	 $(function(){ // this will be called when the DOM is ready 
	    
	    wkpResults_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                    .search( this.value, true, false )
	                    .draw();
	            }
	        } );
	    } ); 
	    
	} ); 
}

function wpkTestCaseDetailsTSResult(event){
	var tr = event.target.closest('tr');
	var row = wkpResults_oTable.DataTable().row(tr);
	tcResultHandler(row, tr);
}

function wpkTestCaseDetailsExportResult(event){
	var tr = event.target.closest('tr');
	var row = wkpResults_oTable.DataTable().row(tr);
	tcDefectsResultHandler(row, tr);
}

function reInitializeWPKDataTableTC(){
	
	setTimeout(function(){				
		wkpResults_oTable.DataTable().columns.adjust().draw();		
	},200);
}

function tcResultHandler(row, tr){
	var url ='teststepsexecution.of.testcaseexecutionId?wptcepId='+row.data().testCaseExecutionResultId+'&jtStartIndex=0&jtPageSize=1000';
	assignDataTableValues(url, "childTable1", row, tr);	
	$("#testCaseTestStepsContainer").modal();
}
function showWorkpackageSummary(wpkId, exeType){
	nodeType = "WorkPackage";
	$("a[href^=#TeamUtilization]").parent("li").hide();
	$("a[href^=#Demand]").parent("li").hide();
	$("a[href^=#Block]").parent("li").hide();
	if(exeType == "Automated"){
		$("a[href^=#Plan]").parent("li").hide();
		$("a[href^=#Allocate]").parent("li").hide();
	}else {
		$("a[href^=#Plan]").parent("li").show();
		$("a[href^=#Allocate]").parent("li").show();
	}
	$("#div_ShowWorkpackageSummary .wrkPckgTabCntnt").show();
	workPackageId = wpkId;
	document.getElementById("treeHdnCurrentWorkPackageId").value = wpkId;
	var selectedTab= $("#tablistWP>li.active").index();				    
    utilizationWeekRange = '';
	fetchWPProductType(-1, currPdtId, wpkId);		
	tabSelection(selectedTab,workPackageId);
	//getWeeksName();	
	$(document).off('focusin.modal');
	$('#div_ShowWorkpackageSummary').modal();
}

$(document).on('click', '#div_ShowWorkpackageSummary #tablistWP>li', function(){	
	if($("#clearDataMessage").css("display") == "block") return;
	var workPackageId=document.getElementById("treeHdnCurrentWorkPackageId").value;
		
	var selectedTab = $(this).index();
 	if(workPackageId==null || workPackageId <=0 || workPackageId == 'null' || workPackageId==''){
 		hideAllTabs();	
 		callAlert("Please select the workPackage");
		return false;
	}				
 	tabSelection(selectedTab,workPackageId);			 				  
}); 