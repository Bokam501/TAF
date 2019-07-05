var postBug = 0;
var postTestResult = 0;
var optionsArr= [];
var testDataId;
var objectRepositoryId;
var riskSeveritiesCount=0;
var codeGenerationMode='';
var scriptLanguage='';
var scriptsFor='';
var testrunPlanIdForTestCaseExecution='';
var scriptFileLineofCode='48';
var testCaseExecution = ''; 
var runConfigCheckBoxArrVal = [];
var executeEditTrplan;
var scriptTypeTile='';
var trpId;
var atsgId = -1;
var flag = false;
var jsonTestDataObj = '';
var rnConfigId;
var envCombId;
var trplnId;
var changeRequestToUsecase="NO";
var titleCrUc = "";
var isContinueFlag  = true;

function showCorrespondingTableTestRun(obj) {
	var toShow = obj;
	//$('#form_wizard_1').hide();		
	if(toShow == 1) {
		$("#jTableContainertestrun").show();
		$("#jTableContainertestrunplangroup").hide();			
	}else  {
		$("#jTableContainertestrun").hide();			
		$("#jTableContainertestrunplangroup").show();
	}
}

var key ='';
var nodeType='';
var addorno="yes";
var title='';
var date = new Date();
var timestamp = date.getTime();
var filterVal;
var productTypeId = 18;
var changeRequestTypeId = 42;
var buildId=0;
var isEngagementLevelFlag = true;

jQuery(document).ready(function() {
	QuickSidebar.init(); 
	ComponentsPickers.init();

	// setBreadCrumb(title);   
	createHiddenFieldsForTree();
	createHiddenFieldsForUserDetails();
	setPageTitle("Products");
	//getTreeData("administration.product.tree");
	getTreeData("administration.product.build.node.tree");

//	userRoleId='<%=session.getAttribute("roleId")%>';
//	userId='<%=session.getAttribute("ssnHdnUserId")%>';
	$("#treeContainerDiv").on("loaded.jstree", function(evt, data){
		var defaultNodeId = "";
		$.each($('#treeContainerDiv li'), function(ind, ele){
			if($.jstree.reference('#treeContainerDiv').is_leaf($(ele).attr("id")) == false){
				defaultNodeId = $(ele).attr("id");
				return false;
			}
		});
		$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
	});
	$("#treeContainerDiv").on("select_node.jstree",
			function(evt, data){
		if(data.node != null){

			var entityIdAndType =  data.node.data;
			var arry = entityIdAndType.split("~");
			key = arry[0];
			var type = arry[1];
			title = data.node.text;
			currentNodeId = data.node.id;
			nodeType = type;
			var loMainSelected = data;
			uiGetParents(loMainSelected);
			$("a[href^=#CoreResources]").parent("li").hide();
			if(($("a[href^=#CoreResources]").parent("li").css("display") !== "none") && $("a[href^=#CoreResources]").parent("li").hasClass('active'))
				$("#tabslist>li:first-child>a").trigger('click');

			$("#status_dd").hide();

			$('#toAnimate .portlet .actions').eq(0).css('display','none');
			utilizationWeekRange = '';

			if(nodeType == "TestFactory"){
				isEngagementLevelFlag = true;
				$("#status_dd").show();
				$('#toAnimate .portlet .actions').eq(0).css('display','block');

				$("#testFacMode").show();$("#pdtMode, #tbCntnt").hide();
				testFactoryId = key;
				document.getElementById("treeHdnCurrentTestFactoryId").value=testFactoryId;
				document.getElementById("hdntestFactoryId").value = testFactoryId; 
				productId=0;
				showCoreResourcesTab(testFactoryId,productId,1);			    	

				// testing -----
				var status = 1;
				urlToGetProductsByProductIdAndTestFactoryId = 'administration.testfactory.customer.product.list.bystatus?testFactoryId='+testFactoryId+'&productId='+productId+'&status='+status;		            
				listProductsByTestFactory(urlToGetProductsByProductIdAndTestFactoryId, testFactoryId);
				return false;
			} else {
				$("#pdtMode, #tbCntnt").show();
				$("#testFacMode").hide();					
			}

			if(nodeType == "Product"){
				isEngagementLevelFlag = false;
				productId = key;
				productVersionId=-1;
				buildId=0;
				productNameForWorkflowSummary = title;
				document.getElementById("hdnProductName").value = data.node.text;
				testFactoryId = document.getElementById("treeHdnCurrentTestFactoryId").value;
				document.getElementById("hdnProductId").value=productId;
				document.getElementById("hdntestFactoryId").value = testFactoryId;
				fetchProductType(productVersionId, productId);
				if(productId==null && productId==undefined){
					productId=0;
				}						
				showCoreResourcesTab(testFactoryId,productId,0);
				showRiskTab();						
				//Metronic.handleTabs();

			}else if(nodeType == "ProductVersion"){
				productVersionId = key;
				buildId=0;
				productId = document.getElementById("treeHdnCurrentProductId").value;   
				productNameForWorkflowSummary = document.getElementById("treeHdnCurrentProductName").value;   
				document.getElementById("hdnProductVersionId").value=productVersionId;
				testFactoryId = document.getElementById("treeHdnCurrentTestFactoryId").value;
				document.getElementById("hdntestFactoryId").value = testFactoryId;
				showCoreResourcesTab(testFactoryId,productId,0);
				fetchProductType(productVersionId, productId);
				showRiskTab();
				if(productVersionId==null && productVersionId==undefined){
					productVersionId=0;
				}	
				addorno = "yes";
				isEngagementLevelFlag = false;

			}else if(nodeType == "ProductBuild"){
				buildId = key;
				productId = document.getElementById("treeHdnCurrentProductId").value;   
				productNameForWorkflowSummary = document.getElementById("treeHdnCurrentProductName").value;   
				document.getElementById("hdnProductVersionId").value=productVersionId;
				testFactoryId = document.getElementById("treeHdnCurrentTestFactoryId").value;
				document.getElementById("hdntestFactoryId").value = testFactoryId;
				showCoreResourcesTab(testFactoryId,productId,0);
				fetchProductType(productVersionId, productId);
				showRiskTab();
				if(buildId==null && buildId==undefined){
					buildId=0;
				}	
				addorno = "yes";
				isEngagementLevelFlag = false;
			}   
			
			if(modeSelection=="TAF"){
				$("a[href^=#Decoupling]").parent("li").hide();
				$("a[href^=#UserGroup]").parent("li").hide();
				$("a[href^=#ChangeRequests]").parent("li").hide();
				$("a[href^=#Risks]").parent("li").hide();
			}else{
				$("a[href^=#Decoupling]").parent("li").show();
				$("a[href^=#UserGroup]").parent("li").show();
				$("a[href^=#ChangeRequests]").parent("li").show();
				$("a[href^=#Risks]").parent("li").show();
			}
			
			selectedTab=$("#tabslist>li.active").index();			
			tabSelectionInProduct(selectedTab);	
		}
	});  

	$(document).on('change','#status_ul', function() {
		var status = $("#status_ul").find('option:selected').val();				
		
		urlToGetProductsByProductIdAndTestFactoryId = 'administration.testfactory.customer.product.list.bystatus?testFactoryId='+testFactoryId+'&productId='+productId+'&status='+status;
		listProductsByTestFactory(urlToGetProductsByProductIdAndTestFactoryId, testFactoryId);
	});   

	$(document).on('change','#riskStatus_ul', function() {
		var riskStatus = $("#riskStatus_ul").find('option:selected').val();				
		urlToGetRisksOfSpecifiedProductId = 'list.risks.by.product?productId='+productId+'&riskStatus='+riskStatus,
		listRisksOfSelectedProductDataTable();
	});
	$(document).on('change','#riskSeverityStatus_ul', function() {
		var riskSeverityStatus = $("#riskSeverityStatus_ul").find('option:selected').val();				
		urlToGetRiskSevOfSpecifiedProductId = 'risk.severity.list?productId='+productId+'&riskSevStatus='+riskSeverityStatus,
		listRisksOfSelectedProductDataTable();
	});
	$(document).on('change','#riskLikeHoodStatus_ul', function() {
		var riskLikeStatus = $("#riskLikeHoodStatus_ul").find('option:selected').val();				
		urlToGetRiskLikeOfSpecifiedProductId = 'risk.likehood.list?productId='+productId+'&riskLikeStatus='+riskLikeStatus,
		listRisksOfSelectedProductDataTable();			
	});
	$(document).on('change','#riskMitigationStatus_ul', function() {
		var riskMitigationStatus = $("#riskMitigationStatus_ul").find('option:selected').val();				
		urlToGetRiskMitigationOfSpecifiedProductId = 'risk.mitigation.list?productId='+productId+'&riskMitigationStatus='+riskMitigationStatus,
		listRisksOfSelectedProductDataTable();
	});
	$(document).on('change','#riskRatingStatus_ul', function() {
		var riskRatingStatus = $("#riskRatingStatus_ul").find('option:selected').val();				
		urlToGetRiskRatingOfSpecifiedProductId = 'risk.rating.list?productId='+productId+'&riskRatingStatus='+riskRatingStatus;
		listRisksOfSelectedProductDataTable();
	});
	$(document).on('change','#selectFeaturesStatus_ul', function() {
		var featureStatus = $("#selectFeaturesStatus_ul").find('option:selected').val();				
		var buildId=0;
		urlToGetFeaturesOfSpecifiedProductId = 'administration.product.feature.by.versionId.or.buildId?productMasterId='+productId+'&versionId='+productVersionId+'&buildId='+buildId+'&featureStatus='+featureStatus,
		listFeaturesOfSelectedProduct();
	});
	$(document).on('change','#changeRequeststatus_ul', function() {
		var status = $("#changeRequeststatus_ul").find('option:selected').val();
		$('#ChangeRequests').children().show();
		urlToGetChangeRequestsOfSpecifiedProductId = 'list.change.requests.by.product?productId='+productId+"&status="+status;

		$("#uploadFileChangeRequests").hide();

		var jsonObj={
				"Title": 'ActivityChangeRequest',			          
				"SubTitle": 'ActivityChangeRequest at product level',
				"listURL": urlToGetChangeRequestsOfSpecifiedProductId+'&jtStartIndex=0&jtPageSize=10000',
				"creatURL": 'changerequests.add',
				"updateURL": 'changerequests.update',		
				"containerID": 'jTableContainerChangeRequests',
				"productId": productId,
				"componentUsage": "ProductLevelAtProductManagement",	
		};	 
		ActivityChangeRequest.init(jsonObj);

	});

	$(document).on('change','#testCaseStatusFilter_ul', function() {
		alltestCaseDT(0, defaultPageCountSelectedForTestCase);
	});

});

var productTypehidden = 0;
function fetchProductType(productVersionId, productId){
	var url = 'get.prodcutType.by.versionIdProdIdWorkPackageId?productVersionId='+productVersionId+"&productId="+productId+"&workpackageId=-1";			
	$.ajax({
		type: "POST",
		dataType : 'json',
		url : url,
		success: function(data) {
			var prodTypeData=eval(data);
			productTypehidden = prodTypeData[0].productType;
			if(productTypehidden!=null){
				document.getElementById("hdnproductType").value = productTypehidden;
			}						  	
		}
	});
}
//The following three methods are required for drop down listing (static/dynamic)
//function to set select item in the dropdown
var productId=-1;
var productNameForWorkflowSummary = "";
var productVersionId=-1;
function setTitle(dd,id,text){	
	dv =$(dd).children('div');
	dv.text(text);	
	dv.attr('id',id);	
}

function popupClose() {	
	$("#div_PopupMain").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
	$('.assessmentHistoryBtn').show();
}
function popupTestcaseClose() {	
	$("#div_PopupTestcase").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}
function popupFeatureClose() {	
	$("#div_PopupFeature").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

function popupTestRunPlanMapClose() {	
	$("#div_PopupTestRunPlanGroup").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

function PopupDecouplingToTestCase() {	
	$("#div_PopupDecouplingToTestCase").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

function PopupTestCaseToDecoupling() {	
	$("#div_PopupTestCaseToDecoupling").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

function popupDevicesClose() {	
	$("#div_PopupDevices").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

function popupTerminalClose() {	
	$("#div_PopupTerminal").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

function popupCloseFeatureTestcaseTree() {
	$("#div_PopupMainUserProfile").fadeOut("normal");
	$("#div_PopupMainFilter").fadeOut("normal");
	$("#div_PopupBackground").fadeOut("normal");
}

function popupCloseUserProfile(){
	$("#div_PopupSelectedUserProfile").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

/* Load Poup function */
function loadPopup(divId) {
	$("#" + divId).fadeIn(0500); // fadein popup div
	$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
	// IE7, IE8
	$("#div_PopupBackground").fadeIn(0001);
}

document.onkeydown = function(evt) {
	evt = evt || window.event;
	if (evt.keyCode == 27) {
		if (document.getElementById("div_PopupMain").style.display == 'block') {
			popupClose();
		}
	}
};

function handleContent(num) {
	//TODO
	if(num==1)
		createIndicesForTestcase();
	if(num==3)
		importTestcasesFromTMS();
}

function tabSelectionInProduct(selectedTab){
	$('#toAnimate .portlet .actions').eq(0).css('display','none');
	utilizationWeekRange = '';
	CodeMirror.enableCustomRightClick=false;
	
	if(selectedTab==0){						  
		var firstTab = $(".Products");
		if(!(firstTab.hasClass("active in"))){
			firstTab.addClass("active in");
			firstTab.siblings(".tab-pane").removeClass("active in");
		}
		products();
		showProductSummaryDetails(productId);//Commented as it is already called at NodeLevel above
	}else if (selectedTab==1){
		var selectedIndexValue =$("#allocateRadios label.active").index(); 
		showCorrespondingTableTool(selectedIndexValue+1);
	}else if (selectedTab==2){
	$('#toAnimate .portlet .actions').eq(0).css('display','block');
		features();
	}else if (selectedTab==3){
		showCorrespondingTestsTable($("#testsRadioGrp ").find("label.active").index()+1);
	}else if (selectedTab==4){
		decoupling();
	}else if (selectedTab==5){
		environments();
	}else if (selectedTab==6){
		devicesDetails();
	}else if (selectedTab==7){
		showCorrespondingTeamTable($("#teamRadioGrp ").find("label.active").index()+1);
	}else if (selectedTab==8){						  
		userGroups();		
	}else if (selectedTab==9){						  
		changeRequests();		
	}else if (selectedTab==10){
		riskDetails();
	}
}

//init custom dropdown menu handler
function DropDown(el) {
	this.dd = el;
	this.initEvents();
}
DropDown.prototype = {
		initEvents : function() {
			var obj = this;
			obj.dd.on('click', function(event){
				$(this).toggleClass('active');
				event.stopPropagation();
			});	
		}
},		

//Modified
$(document).on('click', '#tabslist>li', function(){
	selectedTab=$(this).index();
	tabSelectionInProduct(selectedTab);
});

function hideTabs(){
	$("a[href^=#CoreResources]").parent("li").hide();
}

function showCoreResourcesTab(testFactoryId, productId, typeFilter){
	var urltoFilter;
	if (typeFilter != 0){
		urltoFilter = 'testfactory.resourcepool.tab.show.mode?testFactoryId='+testFactoryId;
	}else{
		urltoFilter = 'product.tab.show.mode?testFactoryId='+testFactoryId+'&productId='+productId;
	}
	$.ajax({
		url: urltoFilter,
		method: 'POST',
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {
			if(data.Result == 'OK'){
				$("a[href^=#CoreResources]").parent("li").show();
			}else{
				$("a[href^=#CoreResources]").parent("li").hide();				
				if($("a[href^=#CoreResources]").parent("li").hasClass('active'))
					$("#tabslist>li:first-child>a").trigger('click');				
			}
		},
	});
}

function products(){
	testFactoryId = document.getElementById("treeHdnCurrentTestFactoryId").value;
	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		productId=0;
	}
	if(testFactoryId == null || testFactoryId <= 0 || testFactoryId == 'null' || testFactoryId == ''){
		callAlert("Please select the Test Factory or Test Engagement");
		return false;
	}
	$('#Products').children().show();
	urlToGetProductsByProductIdAndTestFactoryId = 'administration.testfactory.customer.product.list?testFactoryId='+testFactoryId+'&productId='+productId;
	listProductsByTestFactory(urlToGetProductsByProductIdAndTestFactoryId, testFactoryId);

}

function setProductNode() {
	if(nodeType == "") return false;
	if(nodeType == "TestFactory") {
		setChildNode();
	}
	return true;
}

function featurestestcases(){	
	setProductNode();

	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#FeatureTestCases').children().show();
		urlToGetFeatureTestCasesOfSpecifiedProductId = 'product.feature.testcase.defects.mappedlist?productId='+productId;
		listFeatureTestcasesOfSelectedProduct();
	}	
}

function decouplingPlan(){	
	setProductNode();

	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#DecouplingPlan').children().show();
		urlToGetDecouplingPlanOfSpecifiedProductId = 'product.decouplingcategory.testcase.list?productId='+productId;
		listDecouplingPlanOfSelectedProduct();
	}	
}

function changeRequests(){
	setProductNode();
	var status = $("#changeRequeststatus_ul").find('option:selected').val();
	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#ChangeRequests').children().show();
		urlToGetChangeRequestsOfSpecifiedProductId = 'list.change.requests.by.product?productId='+productId+"&status="+status;
		$("#uploadFileChangeRequests").hide();

		var jsonObj={
				"Title": 'ActivityChangeRequest',			          
				"SubTitle": 'ActivityChangeRequest at product level',
				"listURL": urlToGetChangeRequestsOfSpecifiedProductId+'&jtStartIndex=0&jtPageSize=10000',
				"creatURL": 'changerequests.add',
				"updateURL": 'changerequests.update',		
				"containerID": 'jTableContainerChangeRequests',
				"productId": productId,
				"componentUsage": "ProductLevelAtProductManagement",	
		};	 
		ActivityChangeRequest.init(jsonObj);
	}	
}

function riskDetails(){
	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		urlToGetRisksOfSpecifiedProductId = 'list.risks.by.product?productId='+productId+'&riskStatus=1';		
		var indexPosition = $("#riskRadioGrp").find("label.active").index();
		indexPosition = indexPosition+1;		
		showCorrespondingRisks(indexPosition);
	}
}

function defectReport(){
	setProductNode();

	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#defectReport').children().show();
		urlToGetDefectReportOfSpecifiedProductId = 'defect.report.fix.fail.report?productId='+productId;

		var indexPosition = $("#traceabilityReportRiskRadioGrp").find("label.active").index();
		indexPosition = indexPosition+1;
		showCorrespondingTraceabilityReports(indexPosition);
	}	
}

function environmentPlan(){
	if(nodeType == "ProductVersion"){
		productVersionId = key;
	}else{
		console.log('version hdnProductVersionId---'+productVersionId);
	}

	if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == ''){
		callAlert("Please select the Product Version");
		return false;
	}	
	var date = new Date();
	var timestamp = date.getTime();    	
	urlToGetEnvironmentCombinationByProduct = "environment.combination.list.byProductId?productVersionId="+productVersionId+"&productId=-1&workpackageId=-1&testRunPlanId=-1";
	listEnvCombinationByProductPlan(-1,productVersionId);	
}

function productMgmtTestRun(){
	$(".trp_radGrp").find("label:first").addClass("active").siblings("label").removeClass("active");
	showCorrespondingTableTestRun(1);
	if(nodeType == "TestFactory"){
		testFactoryId = key;
		if(testFactoryId == null || testFactoryId <= 0 || testFactoryId == 'null' || testFactoryId == ''){
			callAlert("Please select the TestFactory");
			hideAllTabs();
			return false;
		}
		urlToGetTestRunPlanOfSpecifiedProductId = 'administration.testrunplan.listbytestFactorProductorVersion?productversionId=-1&productId=-1&testFactoryId='+testFactoryId;
	}else if(nodeType == "Product"){
		productId = key;
		if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");
			hideAllTabs();
			return false;
		}
		urlToGetTestRunPlanOfSpecifiedProductId = 'administration.testrunplan.listbytestFactorProductorVersion?productversionId=-1&productId='+productId+"&testFactoryId=-1";
	}else if(nodeType == "ProductVersion"){
		productVersionId = key;
		if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == ''){
			callAlert("Please select the Product Version");
			hideAllTabs();
			return false;
		}
		urlToGetTestRunPlanOfSpecifiedProductId = 'administration.testrunplan.listbytestFactorProductorVersion?productversionId='+productVersionId+"&productId=-1&testFactoryId=-1";
	}

	var date = new Date();
	var timestamp = date.getTime();

	testsuitesTabFlag=false;
	var jsonObj={		
			"url":urlToGetTestRunPlanOfSpecifiedProductId
			//jsonObj.testRunPlanName=TRPlanName;
	};
	TestRunPlanDT.init(jsonObj);

	//productMgmtTestrunplangroup();

}

var repositoryValue=0;
function loadAttachments_Object(productVersionID, idValue_ul){
	repositoryValue=2; // Object Repository	
	$(idValue_ul).empty();

	$(idValue_ul).append('<option id="-1" selected value="NoData">NoData</option>');	

	$.post('common.list.testdata.attachment?versionId='+productVersionID+'&attachmentType='+repositoryValue,function(data) {	
		var ary = (data.Options);
		if(ary != null && ary.length >0){
			$.each(ary, function(index, element) {
				$(idValue_ul).append('<option id="' + element.Value + '" value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
			});
		}else{
			returnNodata(idValue_ul, ary);
		}	 
		//$(idValue_ul).select2();
	});
}

function loadAttachments_Data(productVersionID, idValue_ul){
	repositoryValue = 1; // Data Repository
	$(idValue_ul).empty();

	$(idValue_ul).append('<option id="-1" selected value="NoData">NoData</option>');	
	$.post('common.list.testdata.attachment?versionId='+productVersionID+'&attachmentType='+repositoryValue,function(data) {	
		var ary = (data.Options);	
		if(ary != null && ary.length >0){
			$.each(ary, function(index, element) {
				$(idValue_ul).append('<option id="' + element.Value + '" value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
			});
		}else{
			returnNodata(idValue_ul, ary);
		}
		//$(idValue_ul).select2();		
	});
}

function returnNodata(listObjectId, ary){
	if(ary.length <=0){
		$(listObjectId).append('<option id="-1" selected value="NoData">NoData</option>');		  
	}
}

function userGroups(){
	setProductNode();

	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#UserGroup').children().show();
		//listUserGroups();
		userGroupDataTable();
	}
}

function coreResources(){	
	setProductNode();

	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#CoreResources').children().show();
		urlToGetCoreResourcesOfSpecifiedProductId = 'testfactory.product.coreResouces.list?productId='+productId;
		listCoreResourcesOfSelectedProduct();
		urlToGetTestfactoryCoreResourcesOfSpecifiedProductId = 'administration.testFactory.coreResources.list?testFactoryId=' + testFactoryId, 
		listTestfactoryCoreResourcesOfSelectedProduct();
	}
}

//Load the dynamic versions columns for Jtable
function getVersionsForProduct() {
	var versionNames = new Array();
	var versionIds = new Array();
	var verObj = {};
	$.ajax({
		url : 'administration.product.version.listByGet?productId='+productId,
		dataType : 'json',
		error: function() {
			callAlert("An error occurred");
		},
		success : function(data) {
			var result=data.Records;
			availableVersionsCount=data.Records.length;
			$.each(result, function(i,item){ 
				var version = item.productVersionName;
				versionNames.push(version);
				var versionId = item.productVersionListId;
				versionIds.push(versionId);
				verObj[versionId] =version;
			});
			setVersionNames(versionNames);
			setVersionId(versionIds);
			setVersionObjects(verObj);
			callJtable();
		}
	});   
} 

function hideAllTabs(){
	$('#Products').children().hide();
	$('#ToolIntegration').children().hide();

	$('#Testcases').children().hide();
	$('#Testsuites').children().hide();
	$('#Features').children().hide();
	$('#FeatureTestCases').children().hide();
	$('#Decoupling').children().hide();
	$('#DecouplingPlan').children().hide();
	$('#Environment').children().hide();	
	$('#Devices').children().hide();
	$('#TestRunPlanTab').children().hide();
	$('#VersionTestCaseMapping').children().hide();
	$('#ProductTeamResources').children().hide();
	$('#CoreResources').children().hide();
}

function setVersionNames(versionNames){
	jsVersionFieldDisplayTitles = versionNames;
	jsProductVersionFieldDisplayTitles = versionNames;
}
function setVersionId(versionIds){
	jsVersionFieldIDDisplayTitles = versionIds;
}
function setVersionObjects(verObj){
	jsVersionObjectDisplayTitles = verObj;
}

var jsVersionFieldDisplayTitles = new Array();
var availableVersionsCount = 10;
var jsVersionFieldIDDisplayTitles = new Array();
var jsProductVersionFieldDisplayTitles = new Array();
var jsProductVersionFieldsInJson = ["ver1","ver2","ver3","ver4","ver5","ver6","ver7","ver8","ver9","ver10","ver11","ver12","ver13","ver14","ver15","ver16","ver17","ver18","ver19","ver20"];
var availableProductVersionCount = 20;
function versionMappingToTestcase(){

	if(nodeType!="Product"){
		callAlert("Please select the product");
		hideAllTabs();		
		return false;
	}

	if(nodeType != "Product"){
		productId = key;
		if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");
			return false;
		}
	}
	else{
		$('#VersionTestCaseMapping').children().show();
		var date = new Date();
		var timestamp = date.getTime();

		getVersionsForProduct();
	}
}

function callJtable(){
	var fields = {};
	//Add the static fields
	fields['testCaseName'] =  {title: 'Testcase Name',list:true,edit: false,width:"20%"};

	//Add the Dynamic Env fields
	for(var i=0;i<availableVersionsCount;i++) {
		fields[jsProductVersionFieldsInJson[i]] = {
				title: jsVersionFieldDisplayTitles[i],
				// width: '20%',
				edit: true,
				type : 'checkbox',
				values: {'1' : 'Yes','0' : 'No'},
				defaultValue: '0'
		};
	}

	//Destroy the current jtable so that it is forced to reload with the new workpackage id
	try{
		if ($('#jTableContainerversiontestcase').length>0) {
			$('#jTableContainerversiontestcase').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainerversiontestcase').jtable({

		title: 'Map Test Cases To Product Version',
		//    selecting: true,  //Enable selecting 
		paging: true, //Enable paging
		pageSize: 10, 
		editinline:{enable:true},
		//	 multiselect: true, //Allow multiple selecting
		//   selectingCheckboxes: true, //Show checkboxes on first column
		actions: {
			//	 listAction :  'product.testcase.list?productId='+productId,
			listAction:'product.testcase.version.list?productId='+productId,
			editinlineAction:'testcase.to.productversion.add.bulk'   
		},

		fields : fields
	});
	$('#jTableContainerversiontestcase').jtable('load');
}

function listTestRunPlanBytestRunId(urlToGetTestRunPlanOfSpecifiedProductVersionId){
	try{
		if ($('#jTableContainerTestRunPlanById').length>0) {
			$('#jTableContainerTestRunPlanById').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainerTestRunPlanById').jtable({

		title: 'Test Plan',
		selecting: true, //Enable selecting 
		paging: true, //Enable paging
		pageSize: 10, //Set page size (default: 10)

		actions: {
			listAction: urlToGetTestRunPlanOfSpecifiedProductVersionId
		},

		fields: {
			runconfigId: {
				key: true,
				list: false,
				create: false
			},
			testRunPlanName: {
				title: 'Name',
				list:true
			},
			notifyByMail: {
				title: 'Mail',
				list:true
			},            
			description: {
				title: 'Description',
				list:true
			},
			testRunScheduledStartTime: {
				title: 'Start Date',
				list:true
			},
			testRunScheduledEndTime: {
				title: 'End Date',
				list:true
			},


		},
        //Validate form when it is being submitted
         formSubmitting: function (event, data) {
       	  data.form.find('input[name="deviceId"]').addClass('validate[required, custom[AlphaNumeric_loworup]]');
             data.form.find('input[name="hostId"]').addClass('validate[required]');
             data.form.validationEngine();
            return data.form.validationEngine('validate');
        }, 
         //Dispose validation logic when form is closed
         formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }  
		});
	 $('#jTableContainerTestRunPlanById').jtable('load');
 }

//BEGIN: ConvertDatatable - TestPlanGroup
var testPlanGroupDT_oTable='';
var editorTestPlanGroup='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;

function testplanGroupDT(){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [{id:"executionTypeId", url:'common.list.executiontypemaster.byentityid?entitymasterid=7'},
	              {id:"productVersionId", url:'common.list.productversion?productId='+productId}];

	testPlanGroupOptions_Container(optionsArr);
}

function testPlanGroupOptions_Container(urlArr){
	$.ajax( {
		"type": "POST",
		"url":  urlArr[optionsItemCounter].url,
		"dataType": "json",
		success: function (json) {		        	 
			if(json.Result == "OK"){						 
				if(json.Options.length>0){     		   
					for(var i=0;i<json.Options.length;i++){
						json.Options[i].label=json.Options[i].DisplayText;
						json.Options[i].value=json.Options[i].Value;
					}			   
				}else{
					json.Options=[];
				}     	   
				optionsResultArr.push(json.Options);						 
			}
			optionsItemCounter++;	
			if(optionsItemCounter<optionsArr.length){
				testPlanGroupOptions_Container(optionsArr);
			}else{
				tableType="TPGParentTable";
				testPlanGroupDataTableInit();
			}					 
		},
		error: function (data) {
			optionsItemCounter++;
		},
		complete: function(data){
			//console.log('Completed');
		},	            
	});
}

function testPlanGroupDataTableInit(){
	var url,jsonObj={};
	if(tableType=="TPGParentTable"){
		url= testplanGroupDTURL+'&jtStartIndex=0&jtPageSize=10000';
		jsonObj={"Title":"Test Plan Group","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Test Plan Group"};
	}else if(tableType=="TPGTestCycleTable"){
		url= testplanGroupTestCycleURL;
		jsonObj={"Title":"Test Plan Group Test Cycle","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Test Plan Group Test Cycle"};
	}else{
		url= testplanGroupMappedTCListURL;
		jsonObj={"Title":"Test Plan Group Mapped Testcase List","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Test Plan Group Mapped Testcase List"};
	}
	testPlanGroupDataTableContainer.init(jsonObj);
}

var testPlanGroupDataTableContainer = function() {
	var initialise = function(jsonObj){
		assignTestPlanGroupDataTableValues(jsonObj);
	};
	return {
		//main function to initiate the module
		init: function(jsonObj) {        	
			initialise(jsonObj);
		}		
	};	
}();

function assignTestPlanGroupDataTableValues(jsonObj){
	openLoaderIcon();			
	$.ajax({
		type: "POST",
		url: jsonObj.url,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {		
			closeLoaderIcon();
			if(data.Result=="ERROR"){
				data = [];						
			}else{
				data = data.Records;
			}					
			jsonObj.data = data;
			if(tableType=="TPGParentTable"){
				testPlanGroupDT_Container(jsonObj);
			}else if(tableType=="TPGTestCycleTable"){
				tpgTestCycleDT_Container(jsonObj);
			}else{
				testPlanGroupMappedTCListDT_Container(jsonObj);
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

function testPlanGroupDataTableHeader(){
	var childDivString ='<table id="testPlanGroup_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>Name</th>'+
			'<th>Version</th>'+
			'<th>Description</th>'+
			'<th>Created Date</th>'+
			'<th>Modified Date</th>'+
			'<th>Execution Type</th>'+
			'<th></th>'+					
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+								
		'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function testPlanGroupDT_Container(jsonObj){

	try{
		if ($("#dataTableContainerForTestplanGroup").children().length>0) {
			$("#dataTableContainerForTestplanGroup").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = testPlanGroupDataTableHeader(); 			 
	$("#dataTableContainerForTestplanGroup").append(childDivString);
	
	productVersionId = document.getElementById("hdnProductVersionId").value;
	if(productVersionId==""){
		productVersionId=-1;
	}
	editorTestPlanGroup = new $.fn.dataTable.Editor( {
		"table": "#testPlanGroup_dataTable",
		ajax: "testrunplangroup.list.add?",
		ajaxUrl: "testrunplangroup.list.update",
		idSrc:  "testRunPlanGroupId",
		i18n: {
			create: {
				title:  "Create a new Test Plan Group",
				submit: "Create",
			}
		},
		fields: [
		         {
		        	 label: "testRunPlanGroupId",
		        	 name: "testRunPlanGroupId",
		        	 "type":"hidden"
		         },{
		        	 label: "Name",
		        	 name: "name",
		         },{
		        	 label: "Description",
		        	 name: "description",
		 		},{
					label:"createdDate",
					name:"createdDate",
					"type": "hidden",
		 		},{
					label:"productId",
					name:"productId",
					"type": "hidden",
					"def":productId
		 		},{
					label:"Product Version",
					name:"productVersionId",
					options: optionsResultArr[1],
					"type": "select",					
				},{
					label:"modifiedDate",
					name:"modifiedDate",
					"type": "hidden",
		         },{
		        	 label: "Execution Type",
		        	 name: "executionType",
		        	 options: optionsResultArr[0],
		        	 "type":"select"
		         }     
		         ]
	});

	testPlanGroupDT_oTable = $("#testPlanGroup_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "90%",
		"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = [7]; // search column TextBox Invisible Column position
			$('#testPlanGroup_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeTestPlanGroupDT();
		},  
		buttons: [
		          { 
		        	  extend: "create",
		        	  editor: editorTestPlanGroup
		          },	
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Test Plan Group',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Test Plan Group',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "testRunPlanGroupId", className: 'disableEditInline', sWidth: '10%' },	
		                                   { mData: "name", className: 'editable', sWidth: '15%' },		
		                                   { mData: "productVersionId", className: 'editable', sWidth: '10%',
			                       				mRender: function (data, type, full) {
			                       					 if (full.action == "create" || full.action == "edit"){
			                       						data = optionsValueHandler(editorTestPlanGroup, 'productVersionId', full.productVersionId);						
			                       					 }else if(type == "display"){
			                       						data = full.productVersionName;
			                       					 }	           	 
			                       					 return data;
			                       				 },
			                       	        },
		                                   { mData: "description", className: 'editable', sWidth: '20%' },		                                   
		                                   { mData: "createdDate", className: 'disableEditInline', sWidth: '15%' },
		                                   { mData: "modifiedDate", className: 'disableEditInline', sWidth: '15%' },
		                                   { mData: "executionType", className: 'editable', sWidth: '15%', editField: "executionType",
		                                	   mRender: function (data, type, full) {
		                                		   data = optionsValueHandler(editorTestPlanGroup, 'executionType', full.executionType); 		           	 
		                                		   return data;
		                                	   },
		                                   },		                                   
		                                   { mData: null, className: 'disableEditInline', 				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+		                                				   
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<img src="css/images/execute_metro.png" class="tpgExecuteImg" title="Execute Test Plan Group" /></button>'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<img src="css/images/list_metro.png" class="tpgMappedTCListImg" title="Test Plans" /></button>'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<img src="css/images/list_metro.png" class="tpgTestCycleImg" title="Test cycles" /></button>'+
		                                				   /*'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<img src="css/images/mapping.png" class="tpgMappingImg" title="Test Plan Mapping" />'+*/
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-search-plus tpgAuditHistoryImg" title="Audit History"></i></button>'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-comments tpgCommentsImg" title="Comments"></i></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },
		                                   ],
		                                   columnDefs: [],
		                                   rowCallback: function ( row, data ) {
		                                	  
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 

	// Activate an inline edit on click of a table cell
	$('#testPlanGroup_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorTestPlanGroup.inline( this, {
			submitOnBlur: true
		} );
	});	

	$('#testPlanGroup_dataTable tbody').on('click', 'td .tpgExecuteImg', function () {
		var tr = $(this).closest('tr');
		var row = testPlanGroupDT_oTable.DataTable().row(tr);
		var productName= document.getElementById("treeHdnCurrentProductName").value ;
		var productVersionName=row.data().productVersionName;
		//var date = new Date();
		var timestamp = date.getFullYear()+"-"+date.getMonth()+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
		var workpackageName=productName+'-'+productVersionName+'-'+row.data().name+'-'+timestamp;
		var description=workpackageName + " is created";
		
		document.getElementById("hdnTestRunPlanGroupId").value=row.data().testRunPlanGroupId;
		document.getElementById("wpkg_name").value=workpackageName;
		document.getElementById("wpkg_desc").value=description;
		
		saveWorkpackageDetailGroup(-1);		
	});	
	
	$('#testPlanGroup_dataTable tbody').on('click', 'td .tpgMappedTCListImg', function () {
		var tr = $(this).closest('tr');
		var row = testPlanGroupDT_oTable.DataTable().row(tr);
		testplanGroupMappedTCListURL = 'testrunplangroup_has_testrunplan.mapped.unmapped.list?testRunPlanGroupId='+row.data().testRunPlanGroupId+"&jtStartIndex=0&jtPageSize=10000";
		tableType = "TPGChildTable";
		testPlanGroupDataTableInit();
		$('#tpgDT_Child_Container').modal();
		$('#tpgDT_Child_Container h4').text("Mapped Test Plan Group - "+row.data().name);
		$(document).off('focusin.modal');
	});
	
	$('#testPlanGroup_dataTable tbody').on('click', 'td .tpgTestCycleImg', function () {
		var tr = $(this).closest('tr');
		var row = testPlanGroupDT_oTable.DataTable().row(tr);
		testplanGroupTestCycleURL = 'test.cycle.list?testFactoryId=-1&productId=-1&productVersionId=-1&testPlanGroupId='+row.data().testRunPlanGroupId+"&jtStartIndex=0&jtPageSize=10000";
		tableType = "TPGTestCycleTable";
		testPlanGroupDataTableInit();
		$('#tpgTestCycleDT_Child_Container').modal();
		$(document).off('focusin.modal');
	});

	/*$('#testPlanGroup_dataTable tbody').on('click', 'td .tpgMappingImg', function () {
		var tr = $(this).closest('tr');
		var row = testPlanGroupDT_oTable.DataTable().row(tr);
		testRunPlanMapping(row.data().testRunPlanGroupId);
	});*/	

	$('#testPlanGroup_dataTable tbody').on('click', 'td .tpgAuditHistoryImg', function () {
		var tr = $(this).closest('tr');
		var row = testPlanGroupDT_oTable.DataTable().row(tr);
		listGenericAuditHistory(row.data().testRunPlanGroupId,"TestRunPlanGroup","testRunPlanGroupAudit");
	});

	$('#testPlanGroup_dataTable tbody').on('click', 'td .tpgCommentsImg', function () {
		var tr = $(this).closest('tr');
		var row = testPlanGroupDT_oTable.DataTable().row(tr);		
		var entityTypeIdComments = 61;
		var entityNameComments = "Test Plan Group";
		listComments(entityTypeIdComments, entityNameComments, row.data().testRunPlanGroupId, row.data().name, "trpGroupComments");
	});	

	$("#testPlanGroup_dataTable_length").css('margin-top','8px');
	$("#testPlanGroup_dataTable_length").css('padding-left','35px');		
	
	testPlanGroupDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutTestPlanGroupDT='';
function reInitializeTestPlanGroupDT(){
	clearTimeoutTestPlanGroupDT = setTimeout(function(){				
		testPlanGroupDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTestPlanGroupDT);
	},200);
}

function fullScreenHandlerDTTestPlanGroup(){
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		var height = Metronic.getViewPort().height -
		$('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
		parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
		parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));

		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
		$('#testFacMode').css('max-height',displaytestFaceModeResponsive(window.innerWidth));

		testPlanGroupDTFullScreenHandler(true);
		reInitializeTestPlanGroupDT();
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
		$('#testFacMode').css('max-height','');

		reInitializeTestPlanGroupDT();				
		testPlanGroupDTFullScreenHandler(false);			
	}
}
//END: ConvertDatatable - TestPlanGroup

//BEGIN: ConvertDataTable - Test Plan Group Child Table
function testPlanGroupMappedTCListDataTableHeader(){
	var childDivString ='<table id="testPlanGroupMappedTCList_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th></th>'+
			'<th>Test Plan</th>'+
			'<th>Order</th>'+								
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+								
		'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function testPlanGroupMappedTCListDT_Container(jsonObj){

	try{
		if ($("#dataTableContainerForTestPlanGroupMappedTCList").children().length>0) {
			$("#dataTableContainerForTestPlanGroupMappedTCList").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = testPlanGroupMappedTCListDataTableHeader(); 			 
	$("#dataTableContainerForTestPlanGroupMappedTCList").append(childDivString);

	editorTestPlanGroupMappedTCList = new $.fn.dataTable.Editor( {
		"table": "#testPlanGroupMappedTCList_dataTable",
		//ajax: "testrunplangroup.list.add?productVersionId="+productVersionId,
		ajaxUrl: "testrunplangroup_has_testrunplan.mappedlist.Update",
		idSrc:  "testRunPlanId",
		i18n: {
			create: {
				title:  "Create a new Test Plan Group Mapped Testcase List",
				submit: "Create",
			}
		},
		fields: [
		         {
		        	 label: "testRunPlanGroupId",
		        	 name: "testRunPlanGroupId",
		        	 "type":"hidden"
		         },{
		        	 label: "testRunGroupId",
		        	 name: "testRunGroupId",
		        	 "type": "hidden",
		 		},{
					label:"testRunPlanId",
					name:"testRunPlanId",
					"type": "hidden",
				},{
					label:"testRunPlanName",
					name:"testRunPlanName",
					"type": "hidden",
		         },{
		        	 label: "order",
		        	 name: "order",
		        	// "type":"hidden"
		         }     
		         ]
	});

	testPlanGroupMappedTCListDT_oTable = $("#testPlanGroupMappedTCList_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "90%",
		"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
			$('#testPlanGroupMappedTCList_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeTestPlanGroupMappedTCListDT();
		},  
		buttons: [
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Test Plan Group Mapped Testcase List',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Test Plan Group Mapped Testcase List',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "isSelected", className: 'editable', sWidth: '5%',			
												mRender: function (data, type, full) {
													if ( type === 'display' ) {
														if(data ==1){
															return '<input type="checkbox" class="isSelected-active" checked>';
														}else{
															return '<input type="checkbox" class="isSelected-active">';
														}
													}
													return data;
												},
												className: "dt-body-center"	            
		                                   }, 
		                                   { mData: "testRunPlanName", className: 'disableEditInline', sWidth: '60%' },	
		                                   { mData: "order", className: 'editable', sWidth: '35%'},                                   
		                                   ],
		                                   columnDefs: [],
		                                   rowCallback: function ( row, data ) {
		                                	  
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 

	// Activate an inline edit on click of a table cell
	$('#testPlanGroupMappedTCList_dataTable').on('click', 'tbody td.editable', function (e) {
		editorTestPlanGroupMappedTCList.inline( this, {
			submitOnBlur: true
		} );
	});	

	$("#testPlanGroupMappedTCList_dataTable_length").css('margin-top','8px');
	$("#testPlanGroupMappedTCList_dataTable_length").css('padding-left','35px');		
	
	testPlanGroupMappedTCListDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );
	
	$('#testPlanGroupMappedTCList_dataTable tbody').on( 'change', 'input.isSelected-active', function (e) {
		var tr = $(this).closest('tr');
		var row = testPlanGroupMappedTCListDT_oTable.DataTable().row(tr);
		var url="";
		
		if($(this).is(':checked')){
			url="testrunplangroup_has_testrunplan.add?testRunPlanId="+row.data().testRunPlanId+"&testRunPlanGroupId="+row.data().testRunPlanGroupId+"&maporunmap=map";
		}else{
			url="testrunplangroup_has_testrunplan.add?testRunPlanId="+row.data().testRunPlanId+"&testRunPlanGroupId="+row.data().testRunPlanGroupId+"&maporunmap=unmap";
		}
		$.ajax({
			type: "POST",
			url: url,
			async:false,
			success: function(data) {
				if(data.Result=='ERROR'){
					callAlert(data.Message);
					return false;
	 		    }else{
	 		    	callAlert(data.Message);
	 		    	return true;
	 		    }
	 		 },    
	 		 dataType: "json"
	 	});	
		 
	});
	
	editorTestPlanGroupMappedTCList.on('preSubmit', function ( e, o, action ) {
		if ( action !== 'remove' ) {
			var elementName = editorTestPlanGroupMappedTCList.field('order');
			if ( ! elementName.isMultiValue() ) {
				if ( ! elementName.val() ) {
					elementName.error( 'Please enter order');
					elementName.val('');
				}
			}
			var str = elementName.val();
			if(/^[0-9-_.]*$/.test(str) == false) {
				elementName.error( 'Please enter number' );
				elementName.val('');
			}
		}
		// If any error was reported, cancel the submission so it can be corrected
        if ( this.inError() ) {
            return false;
        }
		
	} );

}

var clearTimeoutTestPlanGroupMappedTCListDT='';
function reInitializeTestPlanGroupMappedTCListDT(){
	clearTimeoutTestPlanGroupMappedTCListDT = setTimeout(function(){				
		testPlanGroupMappedTCListDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTestPlanGroupMappedTCListDT);
	},200);
}
//END: ConvertDataTable - Test Plan Group Child Table

//BEGIN: ConvertDataTable - Test Plan Group - Test cycle Table
function tpgTestCycleDataTableHeader(){
	var childDivString ='<table id="tpgTestCycle_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>Test Plan Group</th>'+
			'<th>Status</th>'+
			'<th>Result</th>'+
			'<th>Start</th>'+
			'<th>End</th>'+
			'<th></th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+			
			'<th></th>'+	
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function tpgTestCycleDT_Container(jsonObj){

	try{
		if ($("#dataTableContainerForTPGTestCycle").children().length>0) {
			$("#dataTableContainerForTPGTestCycle").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = tpgTestCycleDataTableHeader(); 			 
	$("#dataTableContainerForTPGTestCycle").append(childDivString);
	
	editorTPGTestCycle = new $.fn.dataTable.Editor( {
		"table": "#tpgTestCycle_dataTable",
		//ajax: "testrunplangroup.list.add?",
		//ajaxUrl: "testrunplangroup.list.update",
		idSrc:  "testCycleId",
		i18n: {
			create: {
				title:  "Create a new Test Plan Group Test Cycle",
				submit: "Create",
			}
		},
		fields: []
	});

	tpgTestCycleDT_oTable = $("#tpgTestCycle_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "90%",
		"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = [6]; // search column TextBox Invisible Column position
			$('#tpgTestCycle_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeTPGTestCycleDT();
		},  
		buttons: [
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Test Plan Group Test Cycle',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Test Plan Group Test Cycle',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "testCycleId", className: 'disableEditInline', sWidth: '10%' },	
		                                   { mData: "testRunPlanGroupName", className: 'disableEditInline', sWidth: '15%' },		
		                                   { mData: "testCycleStatus", className: 'disableEditInline', sWidth: '10%'},
		                                   { mData: "result", className: 'disableEditInline', sWidth: '20%' },		                                   
		                                   { mData: "startTime", className: 'disableEditInline', sWidth: '15%' },
		                                   { mData: "endTime", className: 'disableEditInline', sWidth: '15%' },
		                                   { mData: null, className: 'disableEditInline', 				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+		                                				   
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<img src="css/images/list_metro.png" class="tpgWorkpackagesImg" title="Workpackages" /></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },
		                                   ],
		                                   columnDefs: [],
		                                   rowCallback: function ( row, data ) {
		                                	  
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 

	// Activate an inline edit on click of a table cell
	$('#tpgTestCycle_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorTPGTestCycle.inline( this, {
			submitOnBlur: true
		} );
	});	

	$("#tpgTestCycle_dataTable_length").css('margin-top','8px');
	$("#tpgTestCycle_dataTable_length").css('padding-left','35px');		
	
	$('#tpgTestCycle_dataTable tbody').on('click', 'td .tpgWorkpackagesImg', function () {
		openLoaderIcon();
		var tr = $(this).closest('tr');
		var row = tpgTestCycleDT_oTable.DataTable().row(tr);
		viewCurrentCycleWorkpackages(row.data().testCycleId);
		document.getElementById("treeHdnCurrentProductId").value=productId;
		$(document).off('focusin.modal');
	});
	
	tpgTestCycleDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutTPGTestCycleDT='';
function reInitializeTPGTestCycleDT(){
	clearTimeoutTPGTestCycleDT = setTimeout(function(){				
		tpgTestCycleDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTPGTestCycleDT);
	},200);
}

function viewCurrentCycleWorkpackages(testCycleId){
	var url="workpackage.executiondetails.testcyclelevel.list?testCycleId="+testCycleId+"&jtStartIndex=0&jtPageSize=1000";	
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			$("#snapshotStatus").modal();
			listWorkPacakgeTiles(data);			
			closeLoaderIcon();
		},
		complete : function(data){
			closeLoaderIcon();	
		},
		error : function(data){
			closeLoaderIcon();	
		}
	});
}
//END: ConvertDataTable - Test Plan Group - Test cycle Table

function saveWorkpackageDetailGroup(flag){
	var testRunPlanGroupId=document.getElementById("hdnTestRunPlanGroupId").value;
	var workpackageName = document.getElementById("wpkg_name").value;
	var description ='';
	description = document.getElementById("wpkg_desc").value;
	var plannedStartDate=document.getElementById("plannedStartDate").value;
	var plannedEndDate=document.getElementById("plannedEndDate").value;	
	var contextpath = (window.location.pathname)
	.split("/", 2);
	var root = window.location.protocol
	+ "//"
	+ window.location.host
	+ "/"
	+ contextpath[1];
	 var productBuildId= $("#productBuild_ul").find(":selected").attr("id");//$("#productBuild_ul").find('option:selected').attr('id');
	if(flag==-1){
		productBuildId=-1;
	}

	if(workpackageName== ""){
		callAlert("Please Enter Workpackage Name");
		return false;
	}	

	var url='administration.workpackage.testrunplangroup';
       var thediv = document.getElementById('reportbox');
       if (thediv.style.display == "none") {
 		$.blockUI({
 		 	theme : true,
 		 	title : 'Please Wait',
 		 	message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
 		 });
 		$.ajax({
 		    type: "POST",
 		    url: url,
 		    data: { 'workpackageName': workpackageName, 'description': description, 'productBuildId': productBuildId,'plannedStartDate':plannedStartDate,'plannedEndDate':plannedEndDate ,'testRunPlanGroupId':testRunPlanGroupId},
 		    success: function(data) {
 		    	$.unblockUI();
 		    	if(data.Result=='ERROR'){
 		    		callAlert(data.Message);
	 		    	return false;
 		    	}else{
 		    		callAlert(data.Message);
 		    		//window.location.replace(root+"/administration.workpackage.plan");	
 		    		//window.location.replace(root+"/administration.workpackage.plan?workpackageId="+data.Record.id);
 		    	}
 		    },    
 		    dataType: "json", // expected return value type
 		}); 
       } else {
		thediv.style.display = "none";
		thediv.innerHTML = '';
	}
}

//start
var testplanGroupDTURL='';
var testplanGroupMappedTCListURL='';
var testplanGroupTestCycleURL='';

function productMgmtTestrunplangroup(){
	var url = "";
	if(nodeType == "ProductVersion"){
		productVersionId = key;
		if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == ''){
			callAlert("Please select the Product Version");
			return false;
		}
		url =  'testrunplangroup.list?productVersionId='+productVersionId+'&productId=0';
	}else if(nodeType == "Product"){
		productId = key;
		if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");
			hideAllTabs();
			return false;
		}
		url =  'testrunplangroup.list?productVersionId=0&productId='+productId;
	}
	testplanGroupDTURL = url;
	testplanGroupDT();
	
	/*BEGIN: Delete - JTable

	try{
		if($('#jTableContainertestrunplangroup').length > 0){
			$('#jTableContainertestrunplangroup').jtable("destroy");
		}
	}catch(e){

	}
	$('#jTableContainertestrunplangroup').jtable({

		title: 'Add/Edit Test Plan Groups ', 
		editinline:{enable:true},
		paging: true, //Enable paging
		pageSize: 10, 
		// toolbarsearch:true,
		recordsLoaded: function(event, data) {
			$(".jtable-edit-command-button").prop("disabled", true);
		},
		actions: {
			listAction:  url,
			editinlineAction: 'testrunplangroup.list.update',  
			createAction: 'testrunplangroup.list.add?productVersionId='+productVersionId
		}, 
		recordUpdated:function(event, data){
			$('#jTableContainertestrunplangroup').find('.jtable-child-table-container').jtable('reload');
		},
		recordAdded: function (event, data) {
			$('#jTableContainertestrunplangroup').find('.jtable-child-table-container').jtable('reload');
		},
		recordDeleted: function (event, data) {
			$('#jTableContainertestrunplangroup').find('.jtable-child-table-container').jtable('reload');
		},
		fields: { 

			testRunPlanGroupId: { 
				title: 'Test Plan Group ID',
				key: true,
				list: true,
				edit:false,
				create: false
			},			
			name: {
				title: 'Name',
				width:"15%",
				list:true,
				create:true,
				edit:true
			},					    
			description: {
				title: 'Description',
				width:"15%",
				list:true,
				create:true,
				edit:true
			},				 
			createdDate: { 
				title: 'Created Date' ,
				inputTitle: 'From Date <font color="#efd125" size="4px">*</font>',
				edit: false,
				create:false,
				list: true,
				type: 'date',
				width: "30%"          	
			},
			modifiedDate: { 
				title: 'Modified Date' ,
				//inputTitle: 'To Date <font color="#efd125" size="4px">*</font>',
				edit: false,
				create:false,
				list: true,
				type: 'date',
				width: "30%"          	
			},
			executionType:{
				title : 'Execution Type',
				width : "10%",
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
			executeImg:{
				display: function (data) {    	            	 
					var $exe = $('<img src="css/images/execute_metro.png" title="Execute Test Plan Group" class="exe"/>');

					$exe.click(function () {

						var ans=confirm("Do you want to execute the Test Plan Group now?");
						if(ans){
							var productName= document.getElementById("treeHdnCurrentProductName").value ;
							var productVersionName=title ;
							var timestamp = date.getFullYear()+"-"+date.getMonth()+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
							var workpackageName=productName+'-'+productVersionName+'-'+data.record.name+'-'+timestamp;
							var description=workpackageName + " is created";

							document.getElementById("hdnTestRunPlanGroupId").value=data.record.testRunPlanGroupId;
							document.getElementById("wpkg_name").value=workpackageName;
							document.getElementById("wpkg_desc").value=description;

							//execution code comes here
							saveWorkpackageDetailGroup(-1);
							//$.post('test.run.parent.execute',{testRunConfigurationParentId:data.record.testRunConfigurationParentId});
						}
					});
					return $exe;
				},
				create:false,
				edit:false,	          			  
			},

			//SAT
			mappedTestCases: {
				title:'',
				width: "2%",
				create:false,
				edit:true,
				display: function (childData) { 
					//Create an image that will be used to open child table 
					var $img = $('<img src="css/images/list_metro.png" title="Mapped TestCase List" />'); 
					//Open child table when user clicks the image 
					$img.click(function (data) { 

						// ----- Closing child table on the same icon click -----
						closeChildTableFlag = closeJtableTableChildContainer($(this), $("#jTableContainertestrunplangroup"));
						if(closeChildTableFlag){
							return;
						}

						$('#jTableContainertestrunplangroup').jtable('openChildTable', 
								$img.closest('tr'), 
								{ 
							title: 'Mapped Test Plan Group',
							paging: true, //Enable paging
							pageSize: 10, //Set page size (default: 10)
							selecting: true, //Enable selecting 
							editinline:{enable:true},					        	  	
							actions: { 
								//listAction: urlToGetTestCasesOfSpecifiedProductId,	      
								listAction: 'testrunplangroup_has_testrunplan.mappedlist?testRunPlanGroupId='+childData.record.testRunPlanGroupId,
								// createAction: 'product.testcase.add',
								editinlineAction: 'testrunplangroup_has_testrunplan.mappedlist.Update',				        	   
							},
							fields: {		      

								testRunPlanId: { 
									title: 'Test PlanId',
									create:false,
									edit: false,
									list:false,
									key: true
								},        	

								testRunPlanGroupId:{
									title: 'Test Plan GroupId', 
									width: "7%",
									type : 'hidden',
									create: false,
									edit : false,
									list : false

								},
								testRunPlanName: { 
									title: 'Test Plan',
									create:false,
									edit: false,
									list:true,
									key: true
								},        	

								order:{
									title: 'Order', 
									width: "7%",                         
									create: false,
									edit : true,
									options: myObj($("#hdnTrpLength").val()),
									list: true

								},
								testRunGroupId:{
									title: 'Test Plan GroupId', 
									//inputTitle: 'Test Case Description <font color="#efd125" size="4px">*</font>',
									width: "40%",                         
									create: true,
									list:false,
									edit:false,
								}

							},

							formSubmitting: function (event, data) {
//								data.form.find('input[name="testCaseName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[minSize[3]], custom[maxSize[25]]');
//								data.form.find('input[name="testCaseDescription"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]');
//								data.form.find('input[name="testCaseCode"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[15]]');
//								data.form.validationEngine();
//								return data.form.validationEngine('validate');
							}, 
							//Dispose validation logic when form is closed
							formClosed: function (event, data) {
								data.form.validationEngine('hide');
								data.form.validationEngine('detach');
							}
								}, 
								function (data) { //opened handler 
									data.childTable.jtable('load'); 
								}); 
					}); 
					return $img; 
				},
			},		          	

			testrunplangroupMapping:{
				title : 'Mapping',
				list : true,
				create : false,
				edit : false,
				width: "10%",
				display:function (data) { 

					//Create an image that will be used to open child table 
					var $img = $('<img src="css/images/mapping.png" title="TestRunPlan Mapping" />'); 
					//Open child table when user clicks the image 
					$img.click(function () {
						testRunPlanMapping(data.record.testRunPlanGroupId);
						//testcaseMapping(data.record.testSuiteId);
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
						listGenericAuditHistory(data.record.testRunPlanGroupId,"TestRunPlanGroup","testRunPlanGroupAudit");
					});
					return $img;
				}
			},
			commentsTRPGroup:{
				title : '',
				list : true,
				create : false,
				edit : false,
				width: "5%",
				display:function (data) { 
					//Create an image for test script popup 
					var $img = $('<i class="fa fa-comments" title="Comments"></i>');
					$img.click(function () {
						var entityTypeIdComments = 61;
						var entityNameComments = "TestRunPlanGroup";
						listComments(entityTypeIdComments, entityNameComments, data.record.testRunPlanGroupId, data.record.name, "trpGroupComments");
					});
					return $img;
				}		
			},
			//SAT					
		}, 
		recordsLoaded: function(event, data) {			 

			$('#jtable-toolbarsearch-mappedTestCases').prop("type", "hidden"); 
			$('#jtable-toolbarsearch-testrunplangroupMapping').prop("type", "hidden"); 

		},

		formSubmitting: function (event, data) {
			//var fromdate=new Date();
			//var todate=new Date();
			createdDate=$("input[name=createdDate]").val();
			//todate=$("input[name=toDate]").val();

			data.form.find('input[name="createdDate"]').addClass('validate[required]');
			//data.form.find('input[name="toDate"]').addClass('validate[required]');

			data.form.validationEngine();
			return data.form.validationEngine('validate');
		},  
		//Dispose validation logic when form is closed
		formClosed: function (event, data) {
			data.form.validationEngine('hide');
			data.form.validationEngine('detach');
		}	    		
	});

	$('#jTableContainertestrunplangroup').jtable('load');

	var jscrolheight = $("#jTableContainertestrunplangroup").height();
	var jscrolwidth = $("#jTableContainertestrunplangroup").width()-20;

	$(".jScrollContainer").on("scroll", function() {
		var lastScrollLeft=0;	
		var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
		if (lastScrollLeft < documentScrollLeft) {
			$("#jTableContainertestrunplangroup").width($(".jtable").width()).height($(".jtable").height());			
			lastScrollLeft = documentScrollLeft;
		}else if(lastScrollLeft >= documentScrollLeft){			
			$("#jTableContainertestrunplangroup").width(jscrolwidth).height(jscrolheight);
		}			
	});	
	END: Delete - JTable
	*/
}
//End

function myObj(len) {
	var obj = {};
	for(var i=1;i<=len;i++){
		obj[""+i] = "" +i;
	}
	return obj;
}

function getPlanHandlerTestRunPlanTab(data){
	var url='getISERecommended.Testcases.by.buildId?buildId='+data.record.productBuildId;
	var title = "Product Name:"+data.record.productName+" - Test Plan Name:"+data.record.testRunPlanName+" - Build Name:"+data.record.productBuildName+" - Recommended Test Plan";
	var jsonObj={"Title":title,		
			"listURL": url,					
			"componentUsageTitle":"recommendedTestPlan",
			"productId": data.record.productId,
			"buildId" : data.record.productBuildId,
			"componentUsageTitle": "ProductTestRunPlan"
	};
	RecommentedTestPlan.init(jsonObj);
	$("#recommendedTestPlanPdMgmContainer").modal();
}

function disableHostOrDevice(){
	var combText = $(this).text();
	if(combText == 'Windows'){
		$(this).next().next().find('span').css('display','none');
		$(this).next().find('span').css('display','block');
	}else if(combText == "Chrome"){
		$(this).next().find('span').css('display','none');
		$(this).next().next().find('span').css('display','block');
	}
}

function productMgmtCallConfirmTestRunPlan(event){
	initializeBootstrapWizard();

	var arr = (event.target.id).split('~');
	var TRPlanId=arr[0];
	var TRPlanName=arr[1];
	var TRProuductId=arr[2];
	var TRProductName=arr[3];
	var TRProductVersionId=arr[4];
	var TRExecutionId=arr[5];

	testRunPlan(TRPlanId ,TRExecutionId);       

	var jsonObj={};		
	jsonObj.Title = "TestRunPlan : "+TRProductName;
	jsonObj.mode ="edit",
	jsonObj.testRunPlanId=TRPlanId;
	jsonObj.testRunPlanName=TRPlanName;
	jsonObj.productId=TRProuductId;
	jsonObj.productName=TRProductName;
	jsonObj.productVersionId=TRProductVersionId;		
	jsonObj.executionTypeId=TRExecutionId;	
	TestRunPlan.init(jsonObj); 	
}

var divClone = $("#TestRunPlan").clone();
var formClone = $("#form_wizard_1").clone();

function listTestRunSelectedProduct(urlToGetDevicesOfSpecifiedProductId){
	callAlert("ok loading TestRuns ");
	try{
		if ($('#jTableContainertestrun').length>0) {
			$('#jTableContainertestrun').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainertestrun').jtable({

		title: 'Add/Edit Test Runs',
		selecting: true,  //Enable selecting 
		paging: true, //Enable paging
		pageSize: 10, //Set page size (default: 10)
		toolbar : {
			items : [{
				text : "Show",
				click : function() {
					productMgmtShowTestRunplanFormNew();
				}
			}]
		},
		actions: {
			listAction: urlToGetDevicesOfSpecifiedProductId
		},
		fields: {
			runconfigId: {
				key: true,
				list: false,
				create: false
			},
			runConfigurationName: {
				title: 'Test Configuration',
				inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
			},

		},
         testDataMapping:{
     	    title : '',
				list : true,
				create : false,
				edit : false,
				width: "1%",
				display: function (data) { 
					//Create an image that will be used to open child table 
	     			var $img = $('<img src="css/images/mapping.png" title="Map Test Data" data-toggle="modal" data-target="#dragListItemsContainer" />');
	     			//Open child table when user clicks the image 
	     			$img.click(function () {
	     				var leftUrl="attachment.count.for.product?productId="+productId+"&productVersionId="+productVersionId+"&testRunPlanId="+data.record.testRunPlanId;							
	     				var rightUrl = "";
	     				var leftDefaultUrl="attachment.list.for.product?productId="+productId+"&productVersionId="+productVersionId+"&testRunPlanId="+data.record.testRunPlanId+"&jtStartIndex=0&jtPageSize=50";
	     				
	     				var rightDefaultUrl = "attachment.mapped.for.testrunplan?productId="+productId+"&productVersionId="+productVersionId+"&testRunPlanId="+data.record.testRunPlanId;
	     				var leftDragUrl = "testrunplan.attachment.mapping?testRunPlanId="+data.record.testRunPlanId;
	     				var rightDragtUrl = "testrunplan.attachment.mapping?testRunPlanId="+data.record.testRunPlanId;
	     				var leftPaginationUrl = '';
	     				var rightPaginationUrl="";									
	     				jsonTestDataObj={"Title":"Map Repositories to Test Plan",
	     						"leftDragItemsHeaderName":"Available Repositories to Test Plan",
	     						"rightDragItemsHeaderName":"Mapped Repositories to Test Plan",
	     						"leftDragItemsTotalUrl":leftUrl,
	     						"rightDragItemsTotalUrl":rightUrl,
	     						"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
	     						"rightDragItemsDefaultLoadingUrl":rightDefaultUrl,
	     						"leftDragItemUrl":leftDragUrl,
	     						"rightDragItemUrl":rightDragtUrl,
	     						"leftItemPaginationUrl":leftPaginationUrl,
	     						"rightItemPaginationUrl":rightPaginationUrl,									
	     						"leftDragItemsPageSize":"50",
	     						"rightDragItemsPageSize":"50",
	     						"noItems":"No test data / object repository attachments to show",	
	     						"componentUsageTitle":"testRunPlanAttachments",											
	     						};
	     				
	     				DragDropListItems.init(jsonTestDataObj);	     				
	     			});
	     			return $img; 
	         	}  
         },
		  runConfigurationId:{
			title : '',
				list : true,
				create : false,
				edit : false,
				width: "1%",
				display: function (data) { 					
					var $img = $('<img src="css/images/analytics.jpg" title="Get Plan" data-toggle="modal"/>');
					$img.click(function () {
						getPlanHandlerTestRunPlanTab(data);						
					});
					return $img; 
				}  			
        },     

		//Validate form when it is being submitted
		formSubmitting: function (event, data) {
			data.form.find('input[name="deviceId"]').addClass('validate[required, custom[AlphaNumeric_loworup]]');
			data.form.find('input[name="hostId"]').addClass('validate[required]');
			data.form.validationEngine();
			return data.form.validationEngine('validate');
		}, 
		//Dispose validation logic when form is closed
		formClosed: function (event, data) {
			data.form.validationEngine('hide');
			data.form.validationEngine('detach');
		}  
	});
	$('#jTableContainertestrun').jtable('load');	
}


function setDateOnDateRangepicker(pkr_id, stDate, edDate){
	$(pkr_id).data("daterangepicker").setStartDate(stDate);
	$(pkr_id).data("daterangepicker").setEndDate(edDate);
	var strtDate = moment();
	strtDate._d = new Date(stDate);
	var endDate = moment();
	endDate._d = new Date(edDate);
	$(pkr_id).data("daterangepicker").cb(strtDate, endDate)
}

function testsuitesplan(){	
	if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == ''){
		callAlert("Please select the Product Version");
		return false;
	}	
	var date = new Date();
	var timestamp = date.getTime();
	type="1";
	testRunPlanId=document.getElementById("hdnTestRunPlanId").value;
	if(testRunPlanId == null || testRunPlanId <= 0 || testRunPlanId == 'null' || testRunPlanId == ''){
		callAlert("Please create Test Plan");
		return false;
	}
	listTestSuiteSelectedProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
	listOfTestCaseProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);

};

function devices(){
	productId=key;
	if(nodeType != "Product"){
		callAlert("Please select the Product");
		hideAllTabs();
		return false;
	}

	if(nodeType != "Product"){
		productId = key;
		if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");
			return false;
		}
	}else{
		$('#Devices').children().show();
		var date = new Date();
		var timestamp = date.getTime();
		urlToGetDevicesOfSpecifiedProductId = 'admin.genericDevices.list?productId='+productId+"&filter=-2&productversionId=-1&environmentCombinationId=-1&testRunPlanId=-1&workpackageId=-1";
		listDevicesSelectedProduct(urlToGetDevicesOfSpecifiedProductId);
	}
}

function terminal(){
	productId=key;
	if(nodeType != "Product"){
		callAlert("Please select the Product");
		hideAllTabs();
		return false;
	}

	if(nodeType != "Product"){
		productId = key;
		if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");
			return false;
		}
	}
	else{
		$('#Terminal').children().show();
		var date = new Date();
		var timestamp = date.getTime();

		urlToGetTerminalOfSpecifiedProductId = 'administration.host.list?testRunPlanId=-1&workpackageId=-1&ecId=-1&filter=-2';
		listTerminalSelectedProduct(urlToGetTerminalOfSpecifiedProductId);
	}	
}

function listTerminalSelectedProduct(urlToGetTerminalOfSpecifiedProductId){
	$('#jTableContainerTerminal').jtable({

		title: 'Add/Edit Hosts',
		selecting: true, //Enable selecting 
		paging: true, //Enable paging
		pageSize: 10, //Set page size (default: 10)
		editinline:{enable:true},

		actions: {
			listAction: urlToGetTerminalOfSpecifiedProductId,
			createAction: 'administration.host.add',
			editinlineAction: 'administration.host.update',
			//deleteAction: 'administration.host.delete'
		},
		fields: {
			hostId: {
				key: true,
				list: false
			},
			hostName: {
				title: 'Host Name',
				inputTitle: 'Host Name <font color="#efd125" size="4px">*</font>',
				width:"20%"

			},
			hostIpAddress: {
				title: 'IP Address',
				inputTitle: 'IP Address <font color="#efd125" size="4px">*</font>',
				width: "20%"
			},
			hostPlatform: {
				title: 'Platform',                  
				display: function (data) { return data.record.hostPlatform;},
				options: 'administration.host.list.platform',
				width: "30%"

			},
			hostType: {
				title: 'Type',
				display: function (data) { return data.record.hostType;},
				options: {'SERVER':'SERVER','TERMINAL':'TERMINAL'},
				width: "10%"
			},
			hostStatus: {
				title: 'Status',
				type : 'checkbox',
				values: {'0' : 'INACTIVE','1' : 'ACTIVE'},
				display: function (data) 
				{
					if(data.record.hostStatus=="ACTIVE")
						return '<img val='+data.record.status+' style="cursor:pointer;" src="css/images/right.jpg">'; 
					else 
						return '<img val='+data.record.status+' style="cursor:pointer;" src="css/images/crossmark.jpg">';	
				},
				// options: {'ACTIVE':'ACTIVE','INACTIVE':'INACTIVE'},
				width: "10%"
			}
		},
		formSubmitting: function (event, data) {
			data.form.find('input[name="hostName"]').addClass('validate[required, custom[AlphaNumeric_loworup_hyphen]]');
			data.form.find('input[name="hostIpAddress"]').addClass('validate[required, custom[ipv4]]'); 
			data.form.validationEngine();
			return data.form.validationEngine('validate');
		}, 
		//Dispose validation logic when form is closed
		formClosed: function (event, data) {
			data.form.validationEngine('hide');
			data.form.validationEngine('detach');
		} 
	});

	$('#jTableContainerTerminal').jtable('load');
}

function listDevicesSelectedProduct(urlToGetDevicesOfSpecifiedProductId){
	productId=key;
	$('#jTableContainerdevices').jtable({

		title: 'Add/Edit Devices',
		selecting: true,  //Enable selecting 
		paging: true, //Enable paging
		pageSize: 10, //Set page size (default: 10)
		actions: {
			listAction: urlToGetDevicesOfSpecifiedProductId,
			createAction: 'admin.genericDevices.add?productId='+productId
		},
		fields: {
			genericsDevicesId: {
				key: true,
				list: false,
				create: false
			},
			name: {
				title: 'Device Name',
				inputTitle: 'Device Name <font color="#efd125" size="4px">*</font>',
				list: true,
				create: true
			},
			description:{
				list:false,
				title:'Description',
				inputTitle: 'Description <font color="#efd125" size="4px">*</font>',
				list: true,
				create: true
			},
			deviceLabId:{
				title: 'Device Lab',
				inputTitle: 'Device Lab <font color="#efd125" size="4px">*</font>',
				list:false,
				create:true,
				options: function(data) {
					if (data.source=='create') {
						return 'common.deviceLabId.list';
					}
				},
			},
			deviceLabName:{
				title: 'Device Lab',
				list:true,
				create:false

			},
			availableStatus: {
				title: 'Available Status' ,
				inputTitle: 'Available Status <font color="#efd125" size="4px">*</font>',
				list:true,
				edit:false,
				create:true,
				type : 'checkbox',
				values: {'0' : 'No','1' : 'Yes'},
				defaultValue: '1'
			}, 
			platformTypeId:{
				title: 'Platform',
				inputTitle: 'Platform <font color="#efd125" size="4px">*</font>',
				list:false,
				create:true,
				options: function(data) {
					if (data.source=='create') {
						return 'common.platformtype.list';
					}
				},
			},
			platformTypeName:{
				title: 'Platform',
				list:true,
				create:false
			},
			UDID: {
				title: 'UDID',
				inputTitle: 'UDID <font color="#efd125" size="4px">*</font>',
				list: true,
				create: true
			},
			deviceModelMasterId: {
				title: 'Model',
				inputTitle: 'Model <font color="#efd125" size="4px">*</font>',
				create: true,
				list:false,
				edit:false,
				options: function(data) {
					if (data.source=='create') {
						return 'common.deviceModelMaster.list';
					}
				},
			}, 
			deviceModelMasterNmae: {
				title: 'Model',
				create: false,
				list:true,
				edit:false
			}, 
			deviceTypeId: {            	   
				title: 'Device Type',
				edit: false,
				list:false,
				create:true,
				options: 'common.list.deviceTypes'
			},  
		},

		//Validate form when it is being submitted
		formSubmitting: function (event, data) {
			data.form.find('input[name="deviceId"]').addClass('validate[required, custom[AlphaNumeric_loworup]],custom[minSize[2]], custom[maxSize[25]]');
			data.form.find('input[name="hostId"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]');
			data.form.find('input[name="name"]').addClass('validate[required],custom[minSize[2]], custom[maxSize[25]]');
			data.form.find('input[name="description"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]');
			data.form.find('input[name="UDID"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]');
			data.form.validationEngine();
			return data.form.validationEngine('validate');
		}, 
		//Dispose validation logic when form is closed
		formClosed: function (event, data) {
			data.form.validationEngine('hide');
			data.form.validationEngine('detach');
		}  
	});
	$('#jTableContainerdevices').jtable('load');	
}
//Product Scope Ends

var testCaseId;
var testCaseName;
var productTypeName = "WEB" ;
var scriptClosePopupFlag = true ; // for automatic save script
var editStoryFalg = false; // 
var objectRepositoryId = -1;
var testDataId = -1;
var atsgId = -1;
var totalRepository = 0;
var totalTestData = 0;
var parameterType = 'ADD';

function addTestCaseComments(record){	

	var entityInstanceName = record.testCaseName;	
	var entityInstanceId = record.testCaseId;
	var modifiedById = record.assigneeId;
	var currentStatusId = record.workflowStatusId;
	var currentStatusName = record.workflowStatusName;
	var currentStatusDisplayName = record.workflowStatusDisplayName;
	var entityTypeId = 3;
	var entityId = 0;
	var actionTypeValue = 0;
	var secondaryStatusId = record.secondaryStatusId;
	var productId=document.getElementById("hdnProductId").value;	
	$("#addCommentsMainDiv").modal();			
	$('#addComments').hide();//Display only histroy of task Effort
	var jsonObj = {
			Title : "Test Case Workflow History: ["+entityInstanceId+"] "+entityInstanceName,	
			entityTypeName : 'Test Case',		
			entityTypeId : entityTypeId,
			entityInstanceName : entityInstanceName,
			entityInstanceId : entityInstanceId,
			modifiedByUrl : 'common.user.list.by.resourcepool.id',		
			modifiedById : modifiedById,
			raisedDate: new Date(),
			comments : "",	
			productId : productId,
			primaryStatusUrl : 'workflow.status.master.option.list?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&currentStatusId='+currentStatusId,
			secondaryStatusUrl : 'workflow.entity.secondary.status.master.option.list?productId='+productId+'&entityTypeId='+entityTypeId+'&statusId='+currentStatusId,
			currentStatusId : currentStatusId,
			currentStatusName : currentStatusDisplayName,
			secondaryStatusId : secondaryStatusId,
			effortListUrl : 'workflow.event.tracker.list?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
			actionTypeValue : actionTypeValue,
			commentsName : '',
			urlToSave : 'workflow.event.tracker.add?productId='+productId+'&entityId='+entityId+'&entityTypeId='+entityTypeId+'&primaryStatusId=[primaryStatusId]&secondaryStatusId=[secondaryStatusId]&effort=[effort]&comments=[comments]&sourceStatusId='+currentStatusId+'&approveAllEntityInstanceIds=[approveAllEntityIds]&entityInstanceId='+entityInstanceId+'&attachmentIds=[attachmentIds]&actionDate=[actionDate]',
			// commentsStatus: "['started','InProgress','Completed']",		
	};
	AddComments.init(jsonObj);
}

/* DragDropListItem Plugin started */ 		 
var jsonFeatureTabObj='';
var jsonTestCaseTabObj = '';
var jsonCompetencyObj = '';
var jsonWorkflowStatusObj = '';
var jsonRiskTCTabObj = '';
var jsonRiskFeatureTabObj = '';
var jsonRiskMitigationTabObj = '';
var jsonUserMappingForGroupObj = '';
var jsonTestCaseScriptObj = '';
var jsonTestCaseListObj = '';

function leftDraggedItemURLChanges(value,type){
	if(type==jsonFeatureTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&testcaseId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonTestCaseTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&productFeatureId='+itemid+''+leftDragUrlConcat;
	} 
	else if(type == jsonCompetencyObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&dimensionId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonWorkflowStatusObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&dimensionId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonRiskTCTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&testcaseId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonRiskFeatureTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&productFeatureId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonRiskMitigationTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&riskMitigationId='+itemid+''+leftDragUrlConcat;
	} 
	else if(type == jsonTestDataObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&attachmentId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonUserMappingForGroupObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&userId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonTestCaseScriptObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&testCaseId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonTestCaseListObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&scriptId='+itemid+''+leftDragUrlConcat;
	}
	else if(type == jsonTestScenarioObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&testCaseId='+itemid+''+leftDragUrlConcat;
	}
	return result;
}

function rightDraggedItemURLChanges(value,type){
	result='';
	if(type==jsonFeatureTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",
		x =  value.split("("),
		itemid = x[0],		
		result ='&testcaseId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonTestCaseTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&productFeatureId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonCompetencyObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&dimensionId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonWorkflowStatusObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&dimensionId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonRiskTCTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&testcaseId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonRiskFeatureTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&productFeatureId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonRiskMitigationTabObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&riskMitigationId='+itemid+''+rightDragtUrlConcat;
	} 
	else if(type == jsonTestDataObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&attachmentId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonUserMappingForGroupObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&userId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonTestCaseScriptObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&testCaseId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonTestCaseListObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&scriptId='+itemid+''+rightDragtUrlConcat;
	}
	else if(type == jsonTestScenarioObj.componentUsageTitle){
		var leftDragUrlConcat="&maporunmap=unmap",
		rightDragtUrlConcat="&maporunmap=map",	    		
		x =  value.split("("),
		itemid = x[0],	
		result ='&testCaseId='+itemid+''+rightDragtUrlConcat;
	}
	return result;
}

function leftItemDislayListItem(item, jsonObj){
	var resultList="";
	var entity_id = item.itemId;
	var entity_name = item.itemName;	
	var entity_dispname = ''

		if(jsonObj.componentUsageTitle==jsonFeatureTabObj.componentUsageTitle){
			var entity_code = item.itemCode;
			if(entity_code == null){
				entity_dispname = entity_id+" ("+entity_name+")";
			}else{
				entity_dispname = entity_id+" ("+entity_code+")"+" ("+entity_name+")";
			}		
			entity_dispname=trim(entity_dispname);
			resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";	
		}
		else if(jsonObj.componentUsageTitle == jsonTestCaseTabObj.componentUsageTitle){	
			entity_dispname = entity_id+" ("+entity_name+")";
			entity_dispname=trim(entity_dispname);
			resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
		}
		else if(jsonObj.componentUsageTitle == jsonCompetencyObj.componentUsageTitle){	
			entity_dispname = entity_id+" ("+entity_name+")";
			entity_dispname=trim(entity_dispname);
			resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
		}
		else if(jsonObj.componentUsageTitle == jsonWorkflowStatusObj.componentUsageTitle){	
			entity_dispname = entity_id+" ("+entity_name+")";
			entity_dispname=trim(entity_dispname);
			resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
		}
		else if(jsonObj.componentUsageTitle == jsonRiskTCTabObj.componentUsageTitle){
			entity_dispname = entity_id+" ("+entity_name+")";
			entity_dispname=trim(entity_dispname);
			resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
		}
		else if(jsonObj.componentUsageTitle == jsonRiskFeatureTabObj.componentUsageTitle){
			entity_dispname = entity_id+" ("+entity_name+")";
			entity_dispname=trim(entity_dispname);
			resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
		}
		else if(jsonObj.componentUsageTitle == jsonRiskMitigationTabObj.componentUsageTitle){
			entity_dispname = entity_id+" ("+entity_name+")";
			entity_dispname=trim(entity_dispname);
			resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
		} else if(jsonObj.componentUsageTitle == jsonTestDataObj.componentUsageTitle){
			entity_dispname = entity_id+" ("+entity_name+")";
			entity_dispname=trim(entity_dispname);
			resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
		}else if(jsonObj.componentUsageTitle == jsonUserMappingForGroupObj.componentUsageTitle){
			entity_dispname = entity_id+" ("+entity_name+")";
			entity_dispname=trim(entity_dispname);
			resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
		}
		else if(jsonObj.componentUsageTitle == jsonTestCaseScriptObj.componentUsageTitle){
			entity_dispname = entity_id+" ("+entity_name+")";
			entity_dispname=trim(entity_dispname);
			resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
		}
		else if(jsonObj.componentUsageTitle == jsonTestCaseListObj.componentUsageTitle){
			entity_dispname = entity_id+" ("+entity_name+")";
			entity_dispname=trim(entity_dispname);
			resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
		}
		else if(jsonObj.componentUsageTitle == jsonTestScenarioObj.componentUsageTitle){
			entity_dispname = entity_id+" ("+entity_name+")";
			entity_dispname=trim(entity_dispname);
			resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
		}
	return resultList;	
}

function rightItemDislayListItem(item, jsonObj){
	var resultList="";
	var entity_id = item.itemId;	
	var entity_name = item.itemName;
	var entity_dispname = ''
	if(jsonObj.componentUsageTitle==jsonFeatureTabObj.componentUsageTitle){
		var entity_code = item.itemCode;
		if(entity_code == null){
			entity_dispname = entity_id+" ("+entity_name+")";
		}else{
			entity_dispname = entity_id+" ("+entity_code+")"+" ("+entity_name+")";
		}		
		entity_dispname=trim(entity_dispname);	
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonTestCaseTabObj.componentUsageTitle){	
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);	
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}	
	else if(jsonObj.componentUsageTitle == jsonCompetencyObj.componentUsageTitle){	
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);	
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle == jsonWorkflowStatusObj.componentUsageTitle){	
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle==jsonRiskTCTabObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);	
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle==jsonRiskFeatureTabObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);	
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle==jsonRiskMitigationTabObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);	
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	} else if(jsonObj.componentUsageTitle==jsonTestDataObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);	
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}else if(jsonObj.componentUsageTitle==jsonUserMappingForGroupObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);	
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle==jsonTestCaseScriptObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);	
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle==jsonTestCaseListObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);	
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	else if(jsonObj.componentUsageTitle==jsonTestScenarioObj.componentUsageTitle){
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);	
		resultList = "<li title='"+item.itemName+"' style='color: black;'>"+entity_dispname+"</li>";
	}
	return resultList;
}
/* DragDropListItem Plugin ended */

//Starts
function listFeatureTestcasesOfSelectedProduct() {
	var columnData=[];
	try{
		if ($('#featureTestCases_dataTable').length>0) {			
			$("#featureTestCasesContent").children().remove();
			$('#featureTestCases_dataTable').remove();			
			var htmlTable = '<table id="featureTestCases_dataTable"><tbody></tbody><tfoot><tr><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr></tfoot></table>' ;

			$("#featureTestCasesContent").append(htmlTable);			  
		} 	
	} catch(e) {}	

	openLoaderIcon();
	var url = urlToGetFeatureTestCasesOfSpecifiedProductId;
	var tcId = 0;
	$.ajax({
		type: "POST",
		dataType : 'json',
		url : url,
		success: function(data) {
			closeLoaderIcon();
			var data1=eval(data);

			$("#featureTestCasesContent").show();

			if(data1[0].DATA != undefined){
				columnData=data1[0].DATA;
			}
			var cols = data1[0].COLUMNS;
			$('#featureTestCases_dataTable').dataTable({
				"paging": true, 
				// "bJQueryUI": true,
				//   "bDeferRender": true,
				"bInfo" : false,
				"bDestroy" : true,
				//   "bFilter" : false,
				"scrollX":true,
				"scrollY":"100%",
				"aaData": columnData,
				"aoColumns": cols,
				searching:false,
				"fnRowCallback": function( nRow, aData, iDisplayIndex) {
					$.each(aData, function(i,item){   						
						if(i == 3){
							tcId = item;					
						}else if(i == 5){
							$('td:eq(5)',nRow).html('<a href="javascript:listTCExectionReport('+tcId+');">'+ item+'</a>');
						}else if(i == 6){
							$('td:eq(6)',nRow).html('<a href="javascript:listTCDefectsReport('+tcId+');">'+ item+'</a>');
						};  
					});
					//console.log("nRow,�aData,�iDisplayIndex--"+nRow+"=="+�aData[2]+"--"+�iDisplayIndex);
					//   console.log("nRow -->"+aData);
					return nRow;                
				},

				"footerCallback": function ( row, data, start, end, display ) {
					if(columnData.length == 0){
						return false;
					}
					var api = this.api(), data;
					console.log("ftrcallback");
					// Remove the formatting to get integer data for summation
					var intVal = function ( i ) {
						return typeof i === 'string' ?
								i.replace(/[\$,]/g, '')*1 :
									typeof i === 'number' ?
											i : 0;
					};

					// Total over all pages
					//TestCaseExecution Count
					shifttotal = api
					.column(5)
					.data()
					.reduce( function (a, b) {
						Number.prototype.padDigit = function () {
							return (this < 10) ? '0' + this : this;
						};
						if(a==0 || a==null){
							a='0';
						}
						if(b==0 || b==null){
							b='0';
						}

						var op1= Number(a);
						var op2 = Number(b);			                    
						return op1+op2;
					} );
					// Total over this page

					// Update footer
					$( api.column(5).footer() ).html(
							shifttotal
					); 
					// Total over all pages
					//Total Defects
					shifttotal1 = api
					.column(6)
					.data()
					.reduce( function (a, b) {
						Number.prototype.padDigit = function () {
							return (this < 10) ? '0' + this : this;
						};
						if(a==0 || a==null){
							a='0';
						}
						if(b==0 || b==null){
							b='0';
						}

						var ref1= Number(a);
						var ref2 = Number(b);			                   
						return ref1+ref2;								
					} );
					// Total over this page

					// Update footer
					$( api.column(6).footer() ).html(
							shifttotal1
					);
				},


			}); 
			/*},*/
		},complete : function(data){
			closeLoaderIcon();	
		},error : function(data){
			closeLoaderIcon();	
		},	   
	});
} 

//TCExecution Report starts
function listTCExectionReport(tcId){
	document.getElementById("div_PopupFeatureTestCaseExecutionResult").style.display = "block";	
	$('#div_PopupFeatureTestCaseExecutionResult').modal({
		backdrop: 'static',   // This disable for click outside event
		//keyboard: true        // This for keyboard event
	});
	$("#div_PopupFeatureTestCaseExecutionResult").find("h4").text("Test Execution Results Statistics of Test Case :- "+ tcId );
	var url = 'product.feature.testcase.execution.result?tcId='+tcId;
	$.ajax({
		type: "POST",
		dataType : 'json',
		url : url,
		success: function(data) {
			var data1=eval(data);
			var columnData=data1[0].DATA;
			var cols = data1[0].COLUMNS;
			$('#featureTestCasesExecutionResult_dataTable').dataTable({
				"bInfo" : false,
				"bDestroy" : true,
				"scrollY":"100%",
				searching:false,
				"aaData": columnData,
				"aoColumns": cols,
			});
		}
	});
}
//TCExecution Report Ends

//TCExecution Defect Report starts
function listTCDefectsReport(tcId){
	document.getElementById("div_PopupFeatureTestCaseExecutionDefects").style.display = "block";
	$('#div_PopupFeatureTestCaseExecutionDefects').modal({
		backdrop: 'static',   // This disable for click outsideevent
		//keyboard: true        // This for keyboard event
	});
	var url = 'product.feature.testcase.execution.defects?tcId='+tcId;			
	$.ajax({
		type: "POST",
		dataType : 'json',
		url : url,
		success: function(data) {
			var data1=eval(data);
			var columnData=data1[0].DATA;
			var cols = data1[0].COLUMNS;
			$('#featureTestCasesExecutionDefects_dataTable').dataTable({
				"bInfo" : false,
				"bDestroy" : true,
				"scrollY":"100%",
				searching:false,
				"aaData": columnData,
				"aoColumns": cols,
			});
		}
	});

}
//TCExecution Defect Report Ends

//Decoupling Plan Scope Starts
function listDecouplingPlanOfSelectedProduct(){
	try{
		if ($('#jTableContainerdecouplingplan').length>0) {
			$('#jTableContainerdecouplingplan').jtable('destroy'); 
		}
	}catch(e){}

	$('#jTableContainerdecouplingplan').jtable({
		title: 'Decoupling Plan',
		selecting: true,  //Enable selecting
		paging: true, //Enable paging
		pageSize: 10, //Set page size (default: 10)
		editinline:{enable:true},
		actions: { 
			listAction: urlToGetDecouplingPlanOfSpecifiedProductId,
			editinlineAction: 'product.decouplingcategory.testcase.update',		        				 
		}, 
		fields: {
			testcaseCode: { 
				title: 'Test Case Code',  
				width: "15%",                        
				edit: false
			},	        	
			testCaseId: { 
				title: 'Test Case ID',
				edit: false,
				list:true,
				key: true
			},        	
			testCaseName: {
				title: 'Test Case Name',
				width: "12%",
				edit: false,
				display: function (data) { 
					return data.record.testCaseName;
				},
			},
			testCaseDescription:{
				title: 'Test Case Description', 
				width: "20%",                         
				edit: false
			},
			decouplingCategoryId: {
				title: 'Decoupling Category',
				width:"15%",
				options: 'product.decouplingcategory.list?productId='+productId
			}

		},  // This is for closing $img.click(function (data) {	      	      
		//Moved Formcreate validation to Form Submitting
		//Validate form when it is being submitted
		formSubmitting: function (event, data) {
			data.form.find('input[name="testCaseName"]').addClass('validate[required, custom[Letters_loworup_noSpec]]');	           
			data.form.validationEngine();
			return data.form.validationEngine('validate');
		}, 
		//Dispose validation logic when form is closed
		formClosed: function (event, data) {
			data.form.validationEngine('hide');
			data.form.validationEngine('detach');
		}

	});
	$('#jTableContainerdecouplingplan').jtable('load');	
}
//Decoupling Plan Scope Ends 

function trim(str) {
	return str.replace(/^\s+|\s+$/g,"");
}

function testcaseMapping(testSuiteId, testSuiteName){
	$('#searchdev').keyup(function() {
		var txt=$('#searchdev').val();		
		$("#listCount_allTestcasesTestSuites").text("0");
		var resArr = [];
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();		
		$('#alltestcases li').show().filter(function() {	    	
			var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
			if(!~text.indexOf(val) == false) resArr.push("item");

			return !~text.indexOf(val);	    
		}).hide();			
		$("#listCount_allTestcasesTestSuites").text(+resArr.length+" / "+$('#alltestcases li').length);
		if(txt==""){
			$("#listCount_allTestcasesTestSuites").text($('#alltestcases li').length);

		}
	});	

	$('#searchmapdev').keyup(function() {
		var txt=$('#searchmapdev').val();
		if($('#searchmapdev').value==''){
			//alert();
			$("#listCount_TestcasesTestSuitesmapped").text($('#testcasemapped1 li').length);
		}	
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
		var resArr = [];
		$('#testcasemapped1 li').show().filter(function() {	    	
			var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
			if(!~text.indexOf(val) == false) resArr.push("item");
			return !~text.indexOf(val);	        
		}).hide();

		$("#listCount_TestcasesTestSuitesmapped").text(+resArr.length+" / "+$('#testcasemapped1 li').length);
		//$("#listCount_alldevices").text(+resArr.length+" / "+$('#alldevices li').length);
		if(txt==""){
			$("#listCount_TestcasesTestSuitesmapped").text($('#testcasemapped1 li').length);		
		}
	});

	$(".tilebody").empty();
	$(".tilebody").remove();

	document.getElementById("div_PopupTestcase").style.display = "block";
	$("#alltestcases").empty();
	var versionId= 0;
	versionId= document.getElementById("hdnProductVersionId").value;
	if( versionId == ""){
		versionId= 0;
	}	
	var tc_length=0;
	var ts_length=0;
	var tc_ts_total = 0;
	$("#div_PopupTestcase").find("h4").text("Map Testcases to TestSuite  :- "+ testSuiteName );
	//var url="test.case.byProductVersion.list?versionId="+versionId+"&testSuiteId="+testSuiteId+"&jtStartIndex=-1&jtPageSize=-1";
	var url = 'product.testcase.list?productId='+productId+'&status=Approved&jtStartIndex=0&jtPageSize=1000';
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {			
			var result = data.Records;
			if(result.length==0){

				tc_length = result.length;
				tc_ts_total = tc_length+ts_length;				 
				$("#listCount_allTestcasesTestSuites").text(tc_ts_total);
			}else{
				$.each(result, function(i,item){ 
					// $("#alltestcases").append("<li title='"+item.testCaseName+"' style='color: white;'>"+item.testCaseId+"</li>");
					var tcid = item.testCaseId;					
					tcid= tcid+"("+item.testCaseName+")";
					tcid=trim(tcid);
					$("#alltestcases").append("<li title='"+item.testCaseName+"' style='color: black;'>"+tcid+"</li>");
					tc_length = result.length;
					tc_ts_total = tc_length+ts_length;
					$("#listCount_allTestcasesTestSuites").text(tc_ts_total);
				});
			}			
		}
	});  

	var tc_mapped_length=0;
	var ts_mapped_length=0;
	var tc_ts_mapped_total = 0;
	url="test.suite.case.list?testSuiteId="+testSuiteId;
	$("#testcasemapped1").empty();	
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {			
			var listOfData = data.Records;			

			if(listOfData.length==0){			
				tc_mapped_length = listOfData.length;
				tc_ts_mapped_total = tc_mapped_length+ts_mapped_length;			
				$("#listCount_TestcasesTestSuitesmapped").text(tc_ts_mapped_total);
				if($("#testcasemapped1").children().length == 0) {
					$("#testcasemapped1").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 46px;'>No Testcases</b></span>");
					$("#listCount_TestcasesTestSuitesmapped").text(0);
				}
			}else{
				$.each(listOfData, function(i,item){					 
					var tc_id = item.testCaseId;					
					tc_id= tc_id+" ("+item.testCaseName+")";
					tc_id=trim(tc_id);
					$("#testcasemapped1").append("<li  title='"+item.testCaseName+"' style='color: black;'>"+tc_id+"</li>");
					tc_mapped_length = listOfData.length;
					tc_ts_mapped_total = tc_mapped_length+ts_mapped_length;
					$("#listCount_TestcasesTestSuitesmapped").text(tc_ts_mapped_total);
				});
			}
		}
	});   

	Sortable.create(document.getElementById("alltestcases"), {			    
		group: "words",
		animation: 150,
		store: {
			get: function (sortable) {
				var order = localStorage.getItem(sortable.options.group);
				return order ? order.split('|') : [];
			},
			set: function (sortable) {
				var order = sortable.toArray();
				localStorage.setItem(sortable.options.group, order.join('|'));
			}
		},		

		onAdd: function (evt){					
			var draggeditem = trim(evt.item.innerText);
			var x =  draggeditem.split("(");
			var itemid = x[0];
			var itemType = x[1].replace(")","");

			var urlUnMapping = "";
			urlUnMapping = 'test.suite.case.delete.bulk?testcaseId='+itemid+"&testSuiteId="+testSuiteId;
			if($("#emptyListAll").length) $("#emptyListAll").remove();
			$("#listCount_allTestcasesTestSuites").text($("#alltestcases").children().length);
			$.ajax({
				type: "POST",			
				url:urlUnMapping,
				success : function(data) {
					if(data.Result=="ERROR"){
						callAlert(data.Message);
						return false;
					}
					else{	
					}
				}					
			});
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){
			if($("#alltestcases").children().length == 0) {
				$("#alltestcases").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 46px;'>No TestCases/TestSuites</b></span>");
				$("#listCount_allTestcasesTestSuites").text(0);
			}else{
				$("#listCount_allTestcasesTestSuites").text($("#alltestcases").children().length);
			}
		},

		onStart:function(evt){},
		onSort:function(evt){ },
		onEnd: function(evt){ }
	});

	Sortable.create(document.getElementById("testcasemapped1"), {
		group: "words",
		animation: 150,
		onAdd: function (evt){								
			var draggeditem = trim(evt.item.innerText);
			var x =  draggeditem.split("(");
			var itemid = x[0];
			var itemType = x[1].replace(")","");								
			var urlMapping = "";
			urlMapping='test.suite.case.add.bulk?testcaseId='+itemid+"&testSuiteId="+testSuiteId;
			if($("#emptyListAll").length) $("#emptyListAll").remove();
			$("#listCount_TestcasesTestSuitesmapped").text($("#testcasemapped1").children().length);
			$.ajax({
				type: "POST",					  
				url:urlMapping,
				success : function(data) {
					if(data.Result=="ERROR"){
						callAlert(data.Message);
						return false;
					}
					else{

					}							
				}						
			});
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){
			if($("#testcasemapped1").children().length == 0) {
				$("#testcasemapped1").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 46px;'>No Testcases</b></span>");
				$("#listCount_TestcasesTestSuitesmapped").text(0);
			}else{
				$("#listCount_TestcasesTestSuitesmapped").text($("#testcasemapped1").children().length);
			}	
		},

		onStart:function(evt){ },
		onEnd: function(evt){}
	});
}
//TestSuite Scope Ends 

function testRunPlanMapping(testRunPlanGroupId){

	$("#listCount_avilabletestRunPlan").text('');
	$('#search_availtestcases1').keyup(function() {
		var txt=$('#search_availtestcases1').val();	
		if($('#search_availtestcases1').value==''){			
			$("#listCount_avilabletestRunPlan").text($('#allUnmappedTestcases1 li').length);
		}	
		var resArr = [];
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();		
		$('#allUnmappedTestcases1 li').show().filter(function() {	    	
			var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
			if(!~text.indexOf(val) == false) resArr.push("item");	        
			return !~text.indexOf(val);	    
		}).hide();			
		$("#listCount_avilabletestRunPlan").text(+resArr.length+" / "+$('#allUnmappedTestcases1 li').length);
		if(txt==""){
			$("#listCount_avilabletestRunPlan").text($('#allUnmappedTestcases1 li').length);			
		}
	});	

	$('#search_mapptestcase1').keyup(function() {
		var txt=$('#search_mapptestcase1').val();
		if($('#search_mapptestcase1').value==''){			
			$("#listCount_mappedtestcases1").text($('#testcasesmappedtofeature1 li').length);
		}	
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
		var resArr = [];
		$('#testcasesmappedtofeature1 li').show().filter(function() {	    	
			var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
			if(!~text.indexOf(val) == false) resArr.push("item");
			return !~text.indexOf(val);	        
		}).hide();		
		$("#listCount_mappedtestcases1").text(+resArr.length+" / "+$('#testcasesmappedtofeature1 li').length);		
		if(txt==""){
			$("#listCount_mappedtestcases1").text($('#testcasesmappedtofeature1 li').length);		
		}
	});

	$(".tilebody").empty();
	$(".tilebody").remove();

	document.getElementById("div_PopupTestRunPlanGroup").style.display = "block";
	$("#allUnmappedTestcases1").empty();	
	var productId = document.getElementById("hdnProductId").value;
	$("#div_PopupTestRunPlanGroup").find("h4").text("Map Test Plan Group  :- " );

	//int jtStartIndex, @RequestParam int jtPageSize
	var url='administration.testrunplangroup.listbyproductversion?productversionId='+productVersionId+"&productId="+productId+"&jtStartIndex=0&jtPageSize=0&testRunPlanGroupId="+testRunPlanGroupId;
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			var result = data.Records;						 
			if(result.length==0){
				$("#allUnmappedTestcases1").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>No Test Plan</b></span>");
				$("#listCount_avilabletestRunPlan").text(result.length);
			}else{			
				$.each(result, function(i,item){
					var tpid = item.testRunPlanId;

					var tpname = item.testRunPlanName;
					tpid = tpid+"("+tpname+")";
					tpid=trim(tpid);
					$("#allUnmappedTestcases1").append("<li title='"+item.tpname+"' style='color: black;'>"+tpid+"</li>");
					$("#listCount_avilabletestRunPlan").text(result.length);
				});
			}
		}
	}); 	

	url="testrunplangroup_has_testrunplan.mappedlist?testRunPlanGroupId="+testRunPlanGroupId;
	$("#testcasesmappedtofeature1").empty();	
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {			
			var listOfData = data.Records;		
			if(listOfData.length==0){			
				$("#testcasesmappedtofeature1").append("<span style='color: black;' id='emptyListAll1' ><b style='margin-left: 101px;'>No Test Plan</b></span>");
				$("#listCount_mappedtestcases1").text(listOfData.length);
			}else{
				$.each(listOfData, function(i,item){
					var tpid = item.testRunPlanId;

					var tpname = item.testRunPlanName;
					console.log(item.testRunPlanName);
					tpid = tpid+"("+tpname+")";
					tpid=trim(tpid);
					$("#testcasesmappedtofeature1").append("<li  title='"+item.testRunPlanName+"' style='color: black;'>"+tpid+"</li>");
					$("#listCount_mappedtestcases1").text(listOfData.length);
				});
			}
		}
	});

	Sortable.create(document.getElementById("allUnmappedTestcases1"), {			    
		group: "words",
		animation: 150,
		store: {
			get: function (sortable) {
				var order = localStorage.getItem(sortable.options.group);
				return order ? order.split('|') : [];
			},
			set: function (sortable) {
				var order = sortable.toArray();
				localStorage.setItem(sortable.options.group, order.join('|'));
			}
		},		

		onAdd: function (evt){					
			var draggeditem = trim(evt.item.innerText);			
			var x =  draggeditem.split("(");
			var itemid = x[0];
			var itemName = x[1].replace(")","");

			var urlUnMapping = "";				
			urlUnMapping= 'testrunplangroup_has_testrunplan.add?testRunPlanId='+itemid+'&testRunPlanGroupId='+testRunPlanGroupId+'&maporunmap=unmap';
			if($("#emptyListAll1").length) $("#emptyListAll1").remove();
			$("#listCount_avilabletestRunPlan").text($("#allUnmappedTestcases1").children().length);
			$.ajax({
				type: "POST",			
				url:urlUnMapping,
				success : function(data) {
					if(data.Result=="ERROR"){
						callAlert(data.Message);
						return false;
					}
					else{							
					}
				}					
			});
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){
			if($("#allUnmappedTestcases1").children().length == 0) {
				$("#allUnmappedTestcases1").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 101px;'>No Test Plan</b></span>");
				$("#listCount_avilabletestRunPlan").text(0);
			}else{
				$("#listCount_avilabletestRunPlan").text($("#allUnmappedTestcases1").children().length);
			}	

		},

		onStart:function(evt){},
		onSort:function(evt){ },
		onEnd: function(evt){ }
	});

	Sortable.create(document.getElementById("testcasesmappedtofeature1"), {
		group: "words",
		animation: 150,
		onAdd: function (evt){								
			var draggeditem = trim(evt.item.innerText);
			var x =  draggeditem.split("(");
			var itemid = x[0];
			var itemType = x[1].replace(")","");
			var urlMapping = "";
			urlMapping = 'testrunplangroup_has_testrunplan.add?testRunPlanId='+itemid+'&testRunPlanGroupId='+testRunPlanGroupId+'&maporunmap=map';

			if($("#emptyListAll1").length) $("#emptyListAll1").remove();
			$("#listCount_mappedtestcases1").text($("#testcasesmappedtofeature1").children().length);

			$.ajax({
				type: "POST",					  
				url:urlMapping,
				success : function(data) {
					if(data.Result=="ERROR"){
						callAlert(data.Message);
						return false;
					}
					else{

					}
				}						
			});				
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){
			if($("#testcasesmappedtofeature1").children().length == 0) {
				$("#testcasesmappedtofeature1").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 101px;'>No Test Plan</b></span>");
				$("#listCount_mappedtestcases1").text(0);
			}else{
				$("#listCount_mappedtestcases1").text($("#testcasesmappedtofeature1").children().length);
			}

		},

		onStart:function(evt){ },
		onEnd: function(evt){}
	});
}

//Decoupling Mapping to TestCase Starts
function decouplingMappingtotestCase(testCaseId, testCaseName){
	$('#searchdev').keyup(function() {
		var txt=$('#searchdev').val();		
		$("#listCount_alltestcasedecoupling").text("0");
		var resArr = [];
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();		
		$('#alldecoupling li').show().filter(function() {	    	
			var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
			if(!~text.indexOf(val) == false) resArr.push("item");

			return !~text.indexOf(val);	    
		}).hide();			
		$("#listCount_alltestcasedecoupling").text(+resArr.length+" / "+$('#alldecoupling li').length);
		if(txt==""){
			$("#listCount_alltestcasedecoupling").text($('#alldecoupling li').length);			
		}
	});

	$('#searchmapdev').keyup(function() {
		var txt=$('#searchmapdev').val();
		if($('#searchmapdev').value==''){
			//alert();
			$("#listCount_testcasedecouplingsmapped").text($('#decouplingsmapped li').length);
		}	
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
		var resArr = [];
		$('#decouplingsmapped li').show().filter(function() {	    	
			var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
			if(!~text.indexOf(val) == false) resArr.push("item");
			return !~text.indexOf(val);	        
		}).hide();

		$("#listCount_testcasedecouplingsmapped").text(+resArr.length+" / "+$('#decouplingsmapped li').length);		
		if(txt==""){
			$("#listCount_testcasedecouplingsmapped").text($('#decouplingsmapped li').length);		
		}
	});

	$(".tilebody").empty();
	$(".tilebody").remove();

	document.getElementById("div_PopupDecouplingToTestCase").style.display = "block";
	$("#alldecoupling").empty();
	var productId = document.getElementById("hdnProductId").value;
	$("#div_PopupDecouplingToTestCase").find("h4").text("Map Decoupling to Testcase  :- "+ testCaseName );

	var url="testcase.unmappeddecoupling.byProduct.list?productId="+productId+"&testCaseId="+testCaseId+"&jtStartIndex=-1&jtPageSize=-1";
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			var result = data.Records;						 
			if(result.length==0){
				$("#alldecoupling").append("<li style='color: black;'>No Decoupling Categories</li>");
				$("#listCount_alltestcasedecoupling").text(result.length);
			}else{			
				$.each(result, function(i,item){
					var dcid = item.decouplingCategoryId;
					var dcname = item.decouplingCategoryName;
					dcid = dcid+"("+dcname+")";
					dcid=trim(dcid);
					$("#alldecoupling").append("<li title='"+item.decouplingCategoryName+"' style='color: black;'>"+dcid+"</li>");
					$("#listCount_alltestcasedecoupling").text(result.length);
				});
			}
		}
	});	

	url="test.case.decoupling.list?testCaseId="+testCaseId;
	$("#decouplingsmapped").empty();	
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {			
			var listOfData = data.Records;		
			if(listOfData.length==0){			
				$("#listCount_testcasedecouplingsmapped").text(listOfData.length);
			}else{
				$.each(listOfData, function(i,item){
					var dcid = item.decouplingCategoryId;	
					var dcname = item.decouplingCategoryName;
					dcid = dcid+"("+dcname+")";
					dcid=trim(dcid);
					$("#decouplingsmapped").append("<li  title='"+item.decouplingCategoryName+"' style='color: black;'>"+dcid+"</li>");
					$("#listCount_testcasedecouplingsmapped").text(listOfData.length);
				});
			}
		}
	});

	Sortable.create(document.getElementById("alldecoupling"), {			    
		group: "words",
		animation: 150,
		store: {
			get: function (sortable) {
				var order = localStorage.getItem(sortable.options.group);
				return order ? order.split('|') : [];
			},
			set: function (sortable) {
				var order = sortable.toArray();
				localStorage.setItem(sortable.options.group, order.join('|'));
			}
		},		

		onAdd: function (evt){					
			var draggeditem = trim(evt.item.innerText);		
			var x =  draggeditem.split("(");
			var itemid = x[0];
			var itemName = x[1].replace(")","");
			var urlUnMapping = "";			
			urlUnMapping = 'test.case.decoupling.mapping?decouplingCategoryId='+itemid+'&testcaseId='+testCaseId+'&maporunmap=unmap';

			$.ajax({
				type: "POST",			
				url:urlUnMapping,
				success : function(data) {
					if(data.Result=="ERROR"){
						callAlert(data.Message);
						return false;
					}
					else{							
					}
				}					
			});
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){ },
		onStart:function(evt){},
		onSort:function(evt){ },
		onEnd: function(evt){ }
	});

	Sortable.create(document.getElementById("decouplingsmapped"), {
		group: "words",
		animation: 150,
		onAdd: function (evt){								
			var draggeditem = trim(evt.item.innerText);				
			var x =  draggeditem.split("(");
			var itemid = x[0];
			var itemType = x[1].replace(")","");				
			var urlMapping = "";
			urlMapping = 'test.case.decoupling.mapping?decouplingCategoryId='+itemid+'&testcaseId='+testCaseId+'&maporunmap=map';

			$.ajax({
				type: "POST",
				url:urlMapping,
				success : function(data) {
					if(data.Result=="ERROR"){
						callAlert(data.Message);
						return false;
					}
					else{

					}
				}						
			});				
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){ },
		onStart:function(evt){ },
		onEnd: function(evt){}
	});
}
//Decoupling Mapping to TestCase Ends

//TestCase Mapping to Decoupling Starts
function testCasetodecouplingMapping(decouplingCategoryId, decouplingCategoryName){
	$('#search_dctc').keyup(function() {
		var txt=$('#search_dctc').val();		
		$("#listCount_alldecouplingtestcase").text("0");
		var resArr = [];
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();		
		$('#alldctestcases li').show().filter(function() {	    	
			var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
			if(!~text.indexOf(val) == false) resArr.push("item");

			return !~text.indexOf(val);	    
		}).hide();			
		$("#listCount_alldecouplingtestcase").text(+resArr.length+" / "+$('#alldctestcases li').length);
		if(txt==""){
			$("#listCount_alldecouplingtestcase").text($('#alldctestcases li').length);			
		}
	});

	$('#search_mapdctc').keyup(function() {
		var txt=$('#search_mapdctc').val();
		if($('#search_mapdctc').value==''){			
			$("#listCount_decouplingtestcasesmapped").text($('#dctestcasesmapped li').length);
		}	
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
		var resArr = [];
		$('#dctestcasesmapped li').show().filter(function() {	    	
			var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
			if(!~text.indexOf(val) == false) resArr.push("item");
			return !~text.indexOf(val);	        
		}).hide();

		$("#listCount_decouplingtestcasesmapped").text(+resArr.length+" / "+$('#dctestcasesmapped li').length);		
		if(txt==""){
			$("#listCount_decouplingtestcasesmapped").text($('#dctestcasesmapped li').length);		
		}
	});

	$(".tilebody").empty();
	$(".tilebody").remove();

	document.getElementById("div_PopupTestCaseToDecoupling").style.display = "block";
	$("#alldctestcases").empty();
	var productId = document.getElementById("hdnProductId").value;
	$("#div_PopupTestCaseToDecoupling").find("h4").text("Map Test Case to Decoupling   :- "+ decouplingCategoryName );

	var url="decoupling.unmappedtestcase.byProduct.list?productId="+productId+"&decouplingCategoryId="+decouplingCategoryId+"&jtStartIndex=-1&jtPageSize=-1";	
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			var result = data.Records;						 
			if(result.length==0){
				$("#alldctestcases").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>No TestCases</b></span>");
				$("#listCount_alldecouplingtestcase").text(result.length);
			}else{			
				$.each(result, function(i,item){
					var tcid = item.testCaseId;
					var tcname = item.testCaseName;
					tcid = tcid+"("+tcname+")";
					tcid=trim(tcid);
					$("#alldctestcases").append("<li title='"+item.testCaseName+"' style='color: black;'>"+tcid+"</li>");
					$("#listCount_alldecouplingtestcase").text(result.length);
				});
			}
		}
	});

	url="decoupling.test.case.list?decouplingCategoryId="+decouplingCategoryId;
	$("#dctestcasesmapped").empty();	
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {			
			var listOfData = data.Records;		
			if(listOfData.length==0){			
				$("#dctestcasesmapped").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>No TestCases</b></span>");
				$("#listCount_decouplingtestcasesmapped").text(listOfData.length);
			}else{
				$.each(listOfData, function(i,item){
					var tcid = item.testCaseId;	
					var tcname = item.testCaseName;
					tcid = tcid+"("+tcname+")";
					tcid=trim(tcid);
					$("#dctestcasesmapped").append("<li  title='"+item.testCaseName+"' style='color: black;'>"+tcid+"</li>");
					$("#listCount_decouplingtestcasesmapped").text(listOfData.length);
				});
			}
		}
	});

	Sortable.create(document.getElementById("alldctestcases"), {			    
		group: "words",
		animation: 150,
		store: {
			get: function (sortable) {
				var order = localStorage.getItem(sortable.options.group);
				return order ? order.split('|') : [];
			},
			set: function (sortable) {
				var order = sortable.toArray();
				localStorage.setItem(sortable.options.group, order.join('|'));
			}
		},		

		onAdd: function (evt){					
			var draggeditem = trim(evt.item.innerText);		
			var x =  draggeditem.split("(");
			var itemid = x[0];
			var itemName = x[1].replace(")","");
			var urlUnMapping = "";			
			urlUnMapping = 'test.case.decoupling.mapping?decouplingCategoryId='+decouplingCategoryId+'&testcaseId='+itemid+'&maporunmap=unmap';

			if($("#emptyListAll").length) $("#emptyListAll").remove();
			$("#listCount_alldecouplingtestcase").text($("#alldctestcases").children().length);
			$.ajax({
				type: "POST",			
				url:urlUnMapping,
				success : function(data) {
					if(data.Result=="ERROR"){
						callAlert(data.Message);
						return false;
					}
					else{							
					}
				}					
			});
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){
			if($("#alldctestcases").children().length == 0) {
				$("#alldctestcases").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 101px;'>No TestCases</b></span>");
				$("#listCount_alldecouplingtestcase").text(0);
			}else{
				$("#listCount_alldecouplingtestcase").text($("#alldctestcases").children().length);
			}
		},

		onStart:function(evt){},
		onSort:function(evt){ },
		onEnd: function(evt){ }
	});

	Sortable.create(document.getElementById("dctestcasesmapped"), {
		group: "words",
		animation: 150,
		onAdd: function (evt){								
			var draggeditem = trim(evt.item.innerText);				
			var x =  draggeditem.split("(");
			var itemid = x[0];
			var itemType = x[1].replace(")","");				
			var urlMapping = "";
			urlMapping = 'test.case.decoupling.mapping?decouplingCategoryId='+decouplingCategoryId+'&testcaseId='+itemid+'&maporunmap=map';

			if($("#emptyListAll").length) $("#emptyListAll").remove();
			$("#listCount_decouplingtestcasesmapped").text($("#dctestcasesmapped").children().length);

			$.ajax({
				type: "POST",
				url:urlMapping,
				success : function(data) {
					if(data.Result=="ERROR"){
						callAlert(data.Message);
						return false;
					}
					else{

					}
				}						
			});				
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){
			if($("#dctestcasesmapped").children().length == 0) {
				$("#dctestcasesmapped").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 101px;'>No Testcase</b></span>");
				$("#listCount_decouplingtestcasesmapped").text(0);
			}else{
				$("#listCount_decouplingtestcasesmapped").text($("#dctestcasesmapped").children().length);
			}

		},

		onStart:function(evt){ },
		onEnd: function(evt){}
	});
}
//TestCase Mapping to Decoupling Ends

function host_change_checkbox_status(flag,hostId,self){
	var url="";
	if(!$(self).is(':checked')){
		url='administration.host.mapTerminal?hostId='+hostId+'&testRunPlanId='+trplnId+'&runconfigId='+rnConfigId+'&type=unmap&ecId='+envCombId;
	}else{
		url='administration.host.mapTerminal?hostId='+hostId+'&testRunPlanId='+trplnId+'&runconfigId='+rnConfigId+'&type=map&ecId='+envCombId;
	}
	$.ajax({
		type: "POST",
		url: url,
		async:false,
		success: function(data) {

			if(data.Result=='ERROR'){
				callAlert(data.Message);
				return false;
			}else{
				callAlert(data.Message);
				return true;
			}
		},    
		dataType: "json"
	});
}

function device_change_checkbox_status(flag,deviceId,self){
	var url="";
	if(!$(self).is(':checked')){
		url="testing?ecid="+envCombId+"&deviceId="+deviceId+"&type=unmap&testRunPlanId="+trplnId+"&runConfigId="+rnConfigId;
	}else{
		url="testing?ecid="+envCombId+"&deviceId="+deviceId+"&type=map&testRunPlanId="+trplnId+"&runConfigId="+rnConfigId;
	}
	$.ajax({
		type: "POST",
		url: url,
		async:false,
		success: function(data) {

			if(data.Result=='ERROR'){
				callAlert(data.Message);
				return false;
			}else{
				callAlert(data.Message);
				return true;
			}
		},    
		dataType: "json"
	});
}

function runconfiguration_change_testsuite_checkbox_status(flag,testSuiteListId,self){
	unselected_id=$('.testsuite_checkbox[value!='+testSuiteListId+']:checked').eq(0).val();	
	unselected_id=$(self).val();
	
	var url="";
	if(!$(self).is(':checked')){
		url="administration.testsuite.mapunmaprunconfig?runConfigId="+rnConfigId+"&testSuiteId="+testSuiteListId+"&action=Remove";
	}else{
		url="administration.testsuite.mapunmaprunconfig?runConfigId="+rnConfigId+"&testSuiteId="+testSuiteListId+"&action=Add";
	}
	$.ajax({
		type: "POST",
		url: url,
		async:false,
		success: function(data) {
			if(data.Result=='ERROR'){
				callAlert(data.Message);
				return false;
			}else{
				callAlert(data.Message);
				return true;
			}
		},    
		dataType: "json"
	});
}

function saveTestSuiteMap(flag,testSuiteListId){
	if(flag==1){
		var url="administration.testsuite.maprunConfiguration?productversionId="+productVersionId+"&productId="+productId +"&testRunPlanId="+testRunPlanId+"&testSuiteId="+testSuiteListId+"&type=Add";
		$.ajax({
			type: "POST",
			url: url,
			success: function(data) {
				if(data.Result=='ERROR'){
					callAlert(data.Message);
					listTestSuiteSelectedProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
					return false;
				}else{
					callAlert(data.Message);
					listTestSuiteSelectedProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
					return true;
				}
			},    
			dataType: "json",
		});
	}else{
		var url="administration.testsuite.maprunConfiguration?productversionId="+productVersionId+"&productId="+productId +"&testRunPlanId="+testRunPlanId+"&testSuiteId="+testSuiteListId+"&type=Remove";
		$.ajax({
			type: "POST",
			url: url,
			success: function(data) {
				if(data.Result=='ERROR'){
					callAlert(data.Message);
					listTestSuiteSelectedProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
					return false;
				}else{
					callAlert(data.Message);
					listTestSuiteSelectedProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
					return true;
				}
			},    
			dataType: "json",
		}); 
	}
}

function saveTestCaseMap(flag,testCaseId){
	if(flag==1){
		var url="administration.testCase.mapTestRunPlan?productversionId="+productVersionId+"&productId="+productId +"&testRunPlanId="+testRunPlanId+"&testCaseId="+testCaseId+"&type=Add";
		$.ajax({
			type: "POST",
			url: url,
			success: function(data) {

				if(data.Result=='ERROR'){
					callAlert(data.Message);
					listOfTestCaseProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
					return false;
				}else{
					callAlert(data.Message);
					listOfTestCaseProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
					return true;
				}
			},    
			dataType: "json",

		});
	}else{
		var url="administration.testCase.mapTestRunPlan?productversionId="+productVersionId+"&productId="+productId +"&testRunPlanId="+testRunPlanId+"&testCaseId="+testCaseId+"&type=Remove";
		$.ajax({
			type: "POST",
			url: url,
			success: function(data) {
				if(data.Result=='ERROR'){
					callAlert(data.Message);
					listOfTestCaseProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
					return false;
				}else{
					callAlert(data.Message);
					listOfTestCaseProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
					return true;
				}
			},    
			dataType: "json",
		}); 
	}
}

function saveFeatureMap(flag,featureId){
	if(flag==1){
		var url="administration.feature.mapTestRunPlan?productversionId="+productVersionId+"&productId="+productId +"&testRunPlanId="+testRunPlanId+"&featureId="+featureId+"&type=Add";
		$.ajax({
			type: "POST",
			url: url,
			success: function(data) {

				if(data.Result=='ERROR'){
					callAlert(data.Message);
					listOfFeatureProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
					return false;
				}else{
					callAlert(data.Message);
					listOfFeatureProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
					return true;
				}
			},    
			dataType: "json",

		});
	}else{
		var url="administration.feature.mapTestRunPlan?productversionId="+productVersionId+"&productId="+productId +"&testRunPlanId="+testRunPlanId+"&featureId="+featureId+"&type=Remove";
		$.ajax({
			type: "POST",
			url: url,
			success: function(data) {

				if(data.Result=='ERROR'){
					callAlert(data.Message);
					listOfFeatureProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
					return false;
				}else{
					callAlert(data.Message);
					listOfFeatureProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
					return true;
				}
			},    
			dataType: "json",

		}); 
	}
}

//TestSuite for TestRunPlan Starts
function testRunPlanTestSuiteList(urlToGetTestSuiteOfSpecifiedTestRunPlanId){

	try{
		if ($('#jTableContainerTestSuiteOfTestRunPlanTable').length>0) {
			$('#jTableContainerTestSuiteOfTestRunPlanTable').jtable('destroy'); 
		}
	}catch(e){}
	$('#jTableContainerTestSuiteOfTestRunPlanTable').jtable({	         
		title: 'TestSuite',
		selecting: true, //Enable selecting 
		paging: true, //Enable paging
		pageSize: 10, //Set page size (default: 10)
		editinline:{enable:false},
		actions: {	       	
			// listAction: 'test.suite.byProductVersion.list?versionId='+productVersionId+"&timeStamp="+timestamp+"&type=-1",
			listAction:urlToGetTestSuiteOfSpecifiedTestRunPlanId	            
		},
		fields: {	           
			productId: { 
				type: 'hidden',  
				list:false,
				defaultValue: productId
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
				title: 'TestCase Count',
				create : false,
				list : true,
				edit : false,
				inputTitle: 'TestCase Count <font color="#efd125" size="4px">*</font>',
				width: "7%"
			},		        
			testStepsCount: { 
				title: 'TestSteps Count',
				inputTitle: 'TestSteps Count <font color="#efd125" size="4px">*</font>',
				width: "10%",
				create : false,
				list:true,			            
				edit : false
			},
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
		},			 
		//Moved Formcreate validation to Form Submitting
		//Validate form when it is being submitted
		formSubmitting: function (event, data) {        
			data.form.find('input[name="environmentName"]').addClass('validate[required, custom[Letters_loworup_noSpec]]');     
			data.form.find('input[name="environmentGroupId"]').addClass('validate[required]');   
			data.form.find('input[name="environmentCategoryId"]').addClass('validate[required]');   

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

	$('#jTableContainerTestSuiteOfTestRunPlanTable').jtable('load');

	var jscrolheight = $("#jTableContainerTestSuiteOfTestRunPlanTable").height();
	var jscrolwidth = $("#jTableContainerTestSuiteOfTestRunPlanTable").width();

	$(".jScrollContainer").on("scroll", function() {
		var lastScrollLeft=0;	
		var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
		if (lastScrollLeft < documentScrollLeft) {
			$("#jTableContainerTestSuiteOfTestRunPlanTable").width($(".jtable").width()).height($(".jtable").height());			
			lastScrollLeft = documentScrollLeft;
		}else if(lastScrollLeft >= documentScrollLeft){			
			$("#jTableContainerTestSuiteOfTestRunPlanTable").width(jscrolwidth).height(jscrolheight);
		}			
	});
}
//TestSuite for TestRunPlan ends

//Device for TestRunPlan Starts
function testRunPlanDeviceList(urlToGetDeviceorTerminalListOfSpecifiedTestRunPlanId){
	try{
		if ($('#jTableContainerDeviceOfTestRunPlanTable').length>0) {
			$('#jTableContainerDeviceOfTestRunPlanTable').jtable('destroy'); 
		}
	} catch(e) {}	
	$('#jTableContainerDeviceOfTestRunPlanTable').jtable({        
		title: 'Target Device',
		selecting: true,  //Enable selecting 
		paging: true, //Enable paging
		pageSize: 10, //Set page size (default: 10)
		actions: {
			listAction: urlToGetDeviceorTerminalListOfSpecifiedTestRunPlanId,
		},
		fields: {
			runconfigId: {
				key: true,
				list: false,
				create: false
			},
			runConfigurationName: {
				title: 'Test Configuration',
				inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
				list:false
			}, 
			environmentcombinationName: {
				title: 'Environment Combination',
				inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
			}, 
			deviceName: {
				title: 'Device Name',
				inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
			},
			hostName:{
				title: 'Host Name',
				inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
			}
		},

		//Validate form when it is being submitted
		formSubmitting: function (event, data) {
			data.form.find('input[name="deviceId"]').addClass('validate[required, custom[AlphaNumeric_loworup]]');
			data.form.find('input[name="hostId"]').addClass('validate[required]');
			data.form.validationEngine();
			return data.form.validationEngine('validate');
		}, 
		//Dispose validation logic when form is closed
		formClosed: function (event, data) {
			data.form.validationEngine('hide');
			data.form.validationEngine('detach');
		}  
	});
	$('#jTableContainerDeviceOfTestRunPlanTable').jtable('load');
}
//Device for TestRunPlan Ends

//environment plan starts
var maptarget_terminal_device_title='';
var listActionUrlpart='';
var terminal_device_viewtitle='';
var terminal_device_target_viewtitle='';

function listEnvCombinationByProductPlan(productId,productVersionId){
	var listActionUrlpart = '';
	productTypehidden = document.getElementById("hdnproductType").value;
	if(productTypehidden == 1 || productTypehidden == 5){
		terminal_device_target_viewtitle = 'Devices';
		terminal_device_viewtitle = 'View Mapped Devices';
		maptarget_terminal_device_title = 'Target Devices';
		listActionUrlpart ='administration.genericdevice.mappedList';
	}else if(productTypehidden == 2 || productTypehidden == 3 || productTypehidden == 4){
		terminal_device_target_viewtitle = 'Servers';
		terminal_device_viewtitle = 'View Mapped Servers';
		maptarget_terminal_device_title = 'Target Servers';
		listActionUrlpart ='administration.runConfiguration.listbyproductversion';
	}else if(productTypehidden == 6){
		terminal_device_target_viewtitle = 'Servers/Devices';
		terminal_device_viewtitle = 'View Mapped Servers/Devices';
		maptarget_terminal_device_title = 'Servers/Devices';
		listActionUrlpart ='administration.runConfiguration.listbyproductversion';
	}
	var testRunPlanId=document.getElementById("hdnTestRunPlanId").value;
	urlToGetEnvironmentCombinationByProduct = "environment.combination.list.byProductId?productVersionId="+productVersionId+"&productId=-1&workpackageId=-1&testRunPlanId="+testRunPlanId;
	try{
		if ($('#jTableContainerenvironmentCombinationPlan').length>0) {
			$('#jTableContainerenvironmentCombinationPlan').jtable('destroy'); 
		}
	}catch(e){}
	$('#jTableContainerenvironmentCombinationPlan').jtable({
		title: 'Test Beds',
		selecting: true,  //Enable selecting 
		paging: true, //Enable paging
		pageSize: 10, 
		actions: {
			listAction: urlToGetEnvironmentCombinationByProduct,
		},  
		fields : {
			envionmentCombinationId:{
				title: 'envionmentCombination Id',
				list:false,
				width:"20%"
			},
			environmentCombinationName:{
				title: 'Test Beds',
				list:true,
				width:"20%"
			},
			deviceMapping:{
				width: "5%",
				edit: false,
				create: false,
				display: function (environmentData) { 
					//Create an image that will be used to open child table 
					var $img = $('<img src="css/images/list_metro.png" title="View Targets"'+terminal_device_target_viewtitle+'" />');

					//Open child table when user clicks the image 	    
					if(environmentData.record.productType==1 || environmentData.record.productType==5){

						$img.click(function (event) { 
							$('#jTableContainerenvironmentCombinationPlan').jtable('openChildTable', 
									$img.closest('tr'), 
									{ 
								title: 'View Target Devices', 

								editinline:{enable:false},
								paging: true, //Enable paging
								pageSize: 10, 
								recordsLoaded: function(event, data) {
									$(".jtable-edit-command-button").prop("disabled", true);
								},
								actions: { 
									/*listAction:  'administration.runConfiguration.listbyproductversion?productversionId='+productVersionId+"&productId="+productId+"&environmentCombinationId="+environmentData.record.envionmentCombinationId+"&testRunPlanId="+testRunPlanId+"&workpackageId=-1", */
									listAction:  listActionUrlpart+'?productversionId='+productVersionId+"&productId="+productId+"&environmentCombinationId="+environmentData.record.envionmentCombinationId+"&testRunPlanId="+testRunPlanId+"&workpackageId=-1",
								}, 
								fields: {
									runconfigId: {
										list: false,create: false

									},

									runConfigurationName: {
										title: 'Test Configuration',
										inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
										list:false,create: false
									}, 
									environmentcombinationName: {
										title: 'Environment Combination',create: false,
										inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
									}, 
									deviceName: {
										title: 'Device Name',create: false,
										inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
									}, 
									runConfiguration:{
										title: '',
										width: "5%",
										edit: true,
										create: false,
										display: function (runConfig) { 
											var $img = $('<img src="css/images/list_metro.png" title=" Test Configuration Properties" />'); 
											$img.click(function (event) { 
												$('#jTableContainertestrun').jtable('openChildTable', 
														$img.closest('tr'), 
														{ 
													title: ' Test Configuration Properties', 
													paging: true, //Enable paging
													pageSize: 10, 
													toolbar : {
														items : [{
															text : "Add",	
															click : function() {
																entityConfigurationProps(27,testRunPlanId, runConfig.record.runconfigId);
															}
														}]
													}, 
													recordsLoaded: function(event, data) {
														$(".jtable-edit-command-button").prop("disabled", true);
													},
													actions: { 
														listAction: 'entityConfigureProperties.list?entityId='+ runConfig.record.runconfigId+'&entityMasterId=27', 
													}, 
													fields: { 
														runconfigId: {
															list: false,create: false

														}, 

														entityConfigPropertiesMasterName: {
															title: 'Property',
															edit : false,create:false
														},	 	
														options: {
															title: 'Value',edit:false,create:false

														}, 

													},

													formSubmitting: function (event, data) {
														data.form.validationEngine();
														return data.form.validationEngine('validate');
													}, 
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
					}else if(environmentData.record.productType==2 || environmentData.record.productType==3 || environmentData.record.productType==4){
						$img.click(function (event) { 
							$('#jTableContainerenvironmentCombinationPlan').jtable('openChildTable', 
									$img.closest('tr'), 
									{ 
								title: 'View Target Servers', 
								editinline:{enable:false},
								paging: true, //Enable paging
								pageSize: 10, 

								recordsLoaded: function(event, data) {
									$(".jtable-edit-command-button").prop("disabled", true);
								},
								actions: { 
									/*listAction:  'administration.runConfiguration.listbyproductversion?productversionId='+productVersionId+"&productId="+productId+"&environmentCombinationId="+environmentData.record.envionmentCombinationId+"&testRunPlanId="+testRunPlanId+"&workpackageId=-1",	*/
									listAction: 'administration.host.mappedList?testRunPlanId='+testRunPlanId+"&ecId="+environmentData.record.envionmentCombinationId+"&workpackageId=-1",							
								}, 
								fields: {
									runconfigId:{
										type:'hidden' ,

									},

									runConfigurationName: {
										title: 'Test Configuration',create:false,
										inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
										list:false
									}, 
									environmentcombinationName: {
										title: 'Environment Combination',create:false,
										inputTitle: 'Environment Combination <font color="#efd125" size="4px">*</font>',
									}, 
									hostName: {
										title: 'Host Name',create:false,
										inputTitle: 'Host Name <font color="#efd125" size="4px">*</font>',
									}, 
									runConfiguration:{
										title: '',
										width: "5%",
										edit: true,
										create: false,
										display: function (runConfig) { 
											var $img = $('<img src="css/images/list_metro.png" title="Run Configure" />'); 
											$img.click(function (event) { 
												$('#jTableContainertestrun').jtable('openChildTable', 
														$img.closest('tr'), 
														{ 
													title: ' Test Configuration Properties', 
													paging: true, //Enable paging
													pageSize: 10, 
													toolbar : {
														items : [{
															text : "Add",	
															click : function() {
																entityConfigurationProps(27,testRunPlanId, runConfig.record.runconfigId);
															}
														}]
													}, 
													recordsLoaded: function(event, data) {
														$(".jtable-edit-command-button").prop("disabled", true);
													},
													actions: { 
														listAction: 'entityConfigureProperties.list?entityId='+ runConfig.record.runconfigId+'&entityMasterId=27', 
													}, 
													fields: { 
														runconfigId: {
															list: false,create: false

														}, 

														entityConfigPropertiesMasterName: {
															title: 'Property',
															edit : false,create:false
														},	 	
														options: {
															title: 'Value',edit:false,create:false

														}, 

													},

													formSubmitting: function (event, data) {
														data.form.validationEngine();
														return data.form.validationEngine('validate');
													}, 
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
					}else if(environmentData.record.productType==6){
						$img.click(function (event) { 
							$('#jTableContainerenvironmentCombinationPlan').jtable('openChildTable', 
									$img.closest('tr'), 
									{ 
								title: 'View Target Terminal/Device', 
								editinline:{enable:false},
								paging: true, //Enable paging
								pageSize: 10, 

								recordsLoaded: function(event, data) {
									$(".jtable-edit-command-button").prop("disabled", true);
								},
								actions: { 
									/*listAction:  'administration.runConfiguration.listbyproductversion?productversionId='+productVersionId+"&productId="+productId+"&environmentCombinationId="+environmentData.record.envionmentCombinationId+"&testRunPlanId="+testRunPlanId+"&workpackageId=-1",	*/
									listAction: 'administration.hostAndDevice.mappedList?testRunPlanId='+testRunPlanId+"&ecId="+environmentData.record.envionmentCombinationId+"&workpackageId=-1",							
								}, 
								fields: {
									runconfigId:{
										type:'hidden' ,

									},

									runConfigurationName: {
										title: 'Test Configuration',create:false,
										inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
										list:false 
									}, 
									environmentcombinationName: {
										title: 'Environment Combination',create:false,
										inputTitle: 'Environment Combination <font color="#efd125" size="4px">*</font>',
									}, 
									hostName: {
										title: 'Host Name',create:false,
										inputTitle: 'Host Name <font color="#efd125" size="4px">*</font>',
									}, 
									deviceName: {
										title: 'Device Name',create:false,
										inputTitle: 'Device Name <font color="#efd125" size="4px">*</font>',
									}, 
									runConfiguration:{
										title: '',
										width: "5%",
										edit: true,
										create: false,
										display: function (runConfig) { 
											var $img = $('<img src="css/images/list_metro.png" title="Run Configure" />'); 
											$img.click(function (event) { 
												$('#jTableContainertestrun').jtable('openChildTable', 
														$img.closest('tr'), 
														{ 
													title: ' Test Configuration Propertie', 
													paging: true, //Enable paging
													pageSize: 10, 
													toolbar : {
														items : [{
															text : "Add",	
															click : function() {
																entityConfigurationProps(27,testRunPlanId, runConfig.record.runconfigId);
															}
														}]
													}, 
													recordsLoaded: function(event, data) {
														$(".jtable-edit-command-button").prop("disabled", true);
													},
													actions: { 
														listAction: 'entityConfigureProperties.list?entityId='+ runConfig.record.runconfigId+'&entityMasterId=27', 
													}, 
													fields: { 
														runconfigId: {
															list: false,create: false

														}, 

														entityConfigPropertiesMasterName: {
															title: 'Property',
															edit : false,create:false
														},	 	
														options: {
															title: 'Value',edit:false,create:false

														}, 

													},

													formSubmitting: function (event, data) {
														data.form.validationEngine();
														return data.form.validationEngine('validate');
													}, 
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
					}
					//Return image 
					return $img; 
				}           
			},
			envionmentCombinationMapping:{
				title : maptarget_terminal_device_title,
				list : true,
				create : false,
				edit : false,
				width: "10%",
				display:function (data) { 
					//Create an image that will be used to open child table
					var $img;
					if(data.record.productType==1 || data.record.productType==5){
						$img = $('<i class="fa fa-mobile" style="margin-right:7px;font-size:19px;" onclick="mobileMapping(\''+data.record.productType+'\',\''+ data.record.status+'\',\''+ data.record.envionmentCombinationId+'\',\''+ productVersionId+'\',\''+ productId+'\')" title="Mobile"></i> '); 
					}else if(data.record.productType==2 || data.record.productType==3 || data.record.productType==4){
						$img = $('<i class="fa fa-desktop" onclick="desktopMapping(\''+data.record.productType+'\',\''+ data.record.status+'\',\''+ data.record.envionmentCombinationId+'\',\''+ productVersionId+'\',\''+ productId+'\')" title="Desktop"></i>'); 
					}else if(data.record.productType==6){
						$img = $('<i class="fa fa-mobile" style="margin-right:7px;font-size:19px;" onclick="mobileMapping(\''+data.record.productType+'\',\''+ data.record.status+'\',\''+ data.record.envionmentCombinationId+'\',\''+ productVersionId+'\',\''+ productId+'\')" title="Mobile"></i> <i class="fa fa-desktop" onclick="desktopMapping(\''+data.record.productType+'\',\''+ data.record.status+'\',\''+ data.record.envionmentCombinationId+'\',\''+ productVersionId+'\',\''+ productId+'\')" title="Desktop"></i>'); 
					}
					return $img;
				}
			},
			status: {
				title: 'Selected',
				list:true,
				create: false,
				edit:true,
				type : 'checkbox',
				values: {'0' : 'No','1' : 'Yes'},
				display:function(data){
					if(data.record.status==1){
						return '<input type="checkbox" id="status" checked onclick=saveEnvCombMap(0,'+data.record.envionmentCombinationId+','+testRunPlanId+','+productVersionId+'); value='+data.record.status+'>';
					}else if(data.record.status==0){
						return '<input type="checkbox" id="status" onclick=saveEnvCombMap(1,'+data.record.envionmentCombinationId+','+testRunPlanId+','+productVersionId+'); value='+data.record.status+'>';
					}else{
						return '<input type="checkbox" id="status" value='+data.record.status+'>';
					}
				}
			}, 
		},  
	});
	$('#jTableContainerenvironmentCombinationPlan').jtable('load'); 
	var jscrolheight = $("#jTableContainerenvironmentCombinationPlan").height();
	var jscrolwidth = $("#jTableContainerenvironmentCombinationPlan").width();

	$(".jScrollContainer").on("scroll", function() {
		var lastScrollLeft=0;	
		var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
		if (lastScrollLeft < documentScrollLeft) {
			$("#jTableContainerenvironmentCombinationPlan").width($(".jtable").width()).height($(".jtable").height());			
			lastScrollLeft = documentScrollLeft;
		}else if(lastScrollLeft >= documentScrollLeft){			
			$("#jTableContainerenvironmentCombinationPlan").width(jscrolwidth).height(jscrolheight);
		}			
	});
}

function mobileMapping(productType, status, envionmentCombinationId, productVersionId, productId){
	if(status==0){
		devicecMapping(envionmentCombinationId,productVersionId,productId);
	}else{
		callAlert("Environment Combinations are Already mapped to test case! To modify Target Device please unselect the Environment Combination.");
	}
}

function desktopMapping(productType, status, envionmentCombinationId, productVersionId, productId){
	if(status==0){
		terminalMapping(envionmentCombinationId,productVersionId,productId);
	}else{
		callAlert("Environment Combinations are Already mapped to test case! To modify Target Server please unselect the Environment Combination.");
	}
}

function terminalMapping(ecId,productVersionId,productId){
	$(".tilebody").empty();
	$(".tilebody").remove();

	document.getElementById("div_PopupTerminal").style.display = "block";
	$("#allterminals").empty();
	$("#terminalsmapped").empty();
	var versionId= document.getElementById("hdnProductVersionId").value;
	var workpackageId=-1;
	var testRunPlanId=document.getElementById("hdnTestRunPlanId").value;
	var url="administration.host.list.mapping?jtStartIndex=-1&jtPageSize=-1&ecId="+ecId+"&workpackageId=-1&testRunPlanId="+testRunPlanId+"&filter=-1";

	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			var result = data.Records;
			if(result.length==0){
				$("#allterminals").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>No Servers</b></span>");
				$("#listCount_allterminals").text(result.length);
			}else{
				$.each(result, function(i,item){ 
					var terminalid = item.hostIpAddress;					
					terminalid=terminalid;					
					$("#allterminals").append("<li id='"+item.hostId +"' title='"+item.hostIpAddress+"' style='color: black;'>"+terminalid+"("+item.hostStatus+")</li>");
				});
				$("#listCount_allterminals").text(result.length);
			}
		}
	});  

	urlmappedDevices='administration.host.mappedList?testRunPlanId='+testRunPlanId+'&ecId='+ecId+'&workpackageId=-1';        	

	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : urlmappedDevices,
		dataType : 'json',
		success : function(data) {
			var listOfData = data.Records;
			if(listOfData.length==0){
				$("#terminalsmapped").append("<span style='color: black;' id='emptyListMapped' disabled><b style='margin-left: 101px;'>No Servers</b></span>");
				$("#listCount_terminalsmapped").text(listOfData.length);
			}else{
				$.each(listOfData, function(i,item){					
					var tsid = item.hostIpAddress;					
					tsid=tsid;
					if(item.hostIpAddress!=null)
						$("#terminalsmapped").append("<li id='"+item.runconfigId +"'  title='"+item.hostIpAddress+"' style='color: black;'>"+tsid+"("+item.hostStatus+")</li>");
				});
				$("#listCount_terminalsmapped").text(listOfData.length);
			}
		}
	});

	Sortable.create(document.getElementById("allterminals"), {			    
		group: "words",
		animation: 150,
		store: {
			get: function (sortable) {
				var order = localStorage.getItem(sortable.options.group);
				return order ? order.split('|') : [];
			},
			set: function (sortable) {
				var order = sortable.toArray();
				localStorage.setItem(sortable.options.group, order.join('|'));
			}
		},		
		onAdd: function (evt){
			var draggeditem = trim(evt.item.id);
			var urlUnMapping = "";
			urlUnMapping = 'administration.host.mapTerminal?hostId=-1&testRunPlanId='+testRunPlanId+'&runconfigId='+draggeditem+'&type=unmap&ecId='+ecId;

			if($("#emptyListAll").length) $("#emptyListAll").remove();
			$("#listCount_allterminals").text($("#allterminals").children().length);

			$.ajax({
				type: "POST",
				url:urlUnMapping,
				success : function(data) {
					if(data.Result=="ERROR"){
						$("#basicAlert").css("z-index", "100001");
						callAlert(data.Message);
						return false;
					}
					else{
						console.log("left added");
						evt.item.id = data.Records[0].hostId;
					}
				}					
			});
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){ 

			if($("#allterminals").children().length == 0) {
				$("#allterminals").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 101px;'>No Servers</b></span>");
				$("#listCount_allterminals").text(0);
			}else{
				$("#listCount_allterminals").text($("#allterminals").children().length);
			}
		},
		onStart:function(evt){},
		onSort:function(evt){ },
		onEnd: function(evt){ }
	});

	Sortable.create(document.getElementById("terminalsmapped"), {
		group: "words",
		animation: 150,
		onAdd: function (evt){ 
			var draggeditem = trim(evt.item.id);
			urlMapping = 'administration.host.mapTerminal?hostId='+draggeditem+'&testRunPlanId='+testRunPlanId+'&runconfigId=-1&type=map&ecId='+ecId;
			if($("#emptyListMapped").length) $("#emptyListMapped").remove();
			$("#listCount_terminalsmapped").text($("#terminalsmapped").children().length);

			$.ajax({
				type: "POST",
				url:urlMapping,
				success : function(data) {
					if(data.Result=="ERROR"){
						$("#basicAlert").css("z-index", "100001");
						callAlert(data.Message);
						return false;
					}
					else{
						console.log("right added");
						evt.item.id = data.Records[0].runconfigId;
					}
				}						
			});				
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){ 
			if($("#terminalsmapped").children().length == 0) {
				$("#terminalsmapped").append("<span style='color: black;' id='emptyListMapped' disabled><b style='margin-left: 101px;'>No Terminal</b></span>");
				$("#listCount_terminalsmapped").text(0);				
			}else{
				$("#listCount_terminalsmapped").text($("#terminalsmapped").children().length);							 		
			}
		},
		onStart:function(evt){ },
		onEnd: function(evt){}
	});
}

function saveEnvCombMap(isSelected,envionmentCombinationId,testRunPlanId,productVersionId){
	var productId=-1;
	var versionId= -1;
	var workpackageId= -1;    

	if(isSelected==1){

		urlMapping = 'administration.maprunConfiguration.testRunPlan?environmentCombinationId='+envionmentCombinationId+'&type=map&testRunPlanId='+testRunPlanId;

	}
	if(isSelected==0){
		urlMapping = 'administration.maprunConfiguration.testRunPlan?environmentCombinationId='+envionmentCombinationId+'&type=unmap&testRunPlanId='+testRunPlanId;
	}
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : urlMapping,
		dataType : 'json',
		success : function(data) {
			if(data.Result=="ERROR"){
				callAlert(data.Message);
				return false;
			}else{
				urlToGetEnvironmentCombinationByProduct = "environment.combination.list.byProductId?productVersionId="+productVersionId+"&productId=-1&workpackageId="+workpackageId+'&testRunPlanId='+testRunPlanId;
				listEnvCombinationByProductPlan(productId,productVersionId); 
			}
		}
	}); 
}

function devicecMapping(envionmentCombinationId,productVersionId,productId){
	$('#search_device').keyup(function() {
		var txt=$('#search_device').val();

		$("#listCount_alldevices").text("0");
		var resArr = [];
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();

		$('#alldevices li').show().filter(function() {

			var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
			if(!~text.indexOf(val) == false) resArr.push("item");

			return !~text.indexOf(val);

		}).hide();

		$("#listCount_alldevices").text(+resArr.length+" / "+$('#alldevices li').length);
		if(txt==""){
			$("#listCount_alldevices").text($('#alldevices li').length);

		}
	});	

	$('#search_mapdev').keyup(function() {
		var txt=$('#search_mapdev').val();
		if($('#search_mapdev').value==''){			
			$("#listCount_devicesmapped").text($('#devicesmapped li').length);
		}

		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
		var resArr = [];
		$('#devicesmapped li').show().filter(function() {

			var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
			if(!~text.indexOf(val) == false) resArr.push("item");
			return !~text.indexOf(val);	        
		}).hide();

		$("#listCount_devicesmapped").text(+resArr.length+" / "+$('#devicesmapped li').length);		
		if(txt==""){
			$("#listCount_devicesmapped").text($('#devicesmapped li').length);

		}
	});

	$(".tilebody").empty();
	$(".tilebody").remove();

	document.getElementById("div_PopupDevices").style.display = "block";
	var testRunPlanId=document.getElementById("hdnTestRunPlanId").value;
	refreshRunConfigurationMappedList(productId, productVersionId, envionmentCombinationId, testRunPlanId)		
	refreshRunConfigurationUnMappedList(productVersionId, productId, envionmentCombinationId, testRunPlanId);

	Sortable.create(document.getElementById("alldevices"), {			    
		group: "words",
		animation: 150,
		store: {
			get: function (sortable) {
				var order = localStorage.getItem(sortable.options.group);
				return order ? order.split('|') : [];
			},
			set: function (sortable) {
				var order = sortable.toArray();
				localStorage.setItem(sortable.options.group, order.join('|'));
			}
		},	
		onAdd: function (evt){
			var draggeditem = trim(evt.item.id);
			var urlUnMapping = "";
			urlUnMapping = 'testing?ecid='+envionmentCombinationId+'&deviceId=-1&type=unmap&testRunPlanId='+testRunPlanId+"&runConfigId="+draggeditem;

			if($("#emptyListAll").length) $("#emptyListAll").remove();
			$("#listCount_alldevices").text($("#alldevices").children().length);

			$.ajax({
				type: "POST",
				url:urlUnMapping,
				success : function(data) {
					if(data.Result=="ERROR"){
						$("#basicAlert").css("z-index", "100001");
						callAlert(data.Message);
						return false;
					}
					else{							
						console.log("left added");
						evt.item.id = data.Records[0].deviceId;							
					}
				}					
			});
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){ 

			if($("#alldevices").children().length == 0) {
				$("#alldevices").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 101px;'>No Devices</b></span>");
				$("#listCount_alldevices").text(0);
			}else{
				$("#listCount_alldevices").text($("#alldevices").children().length);
			}
		},
		onStart:function(evt){},
		onSort:function(evt){ },
		onEnd: function(evt){ }
	});

	Sortable.create(document.getElementById("devicesmapped"), {
		group: "words",
		animation: 150,
		onAdd: function (evt){ 
			var draggeditem = trim(evt.item.id);
			urlMapping = 'testing?ecid='+envionmentCombinationId+'&deviceId='+draggeditem+'&type=map&testRunPlanId='+testRunPlanId+"&runConfigId=-1";

			if($("#emptyListMapped").length) $("#emptyListMapped").remove();
			$("#listCount_devicesmapped").text($("#devicesmapped").children().length);

			$.ajax({
				type: "POST",
				url:urlMapping,
				success : function(data) {
					if(data.Result=="ERROR"){
						$("#basicAlert").css("z-index", "100001");
						callAlert(data.Message);
						return false;
					}
					else{
						console.log("right added");
						evt.item.id = data.Records[0].runconfigId;														
					}
				}						
			});				
		},
		onUpdate: function (evt){  },
		onRemove: function (evt){ 

			if($("#devicesmapped").children().length == 0) {
				$("#devicesmapped").append("<span style='color: black;' id='emptyListMapped' disabled><b style='margin-left: 101px;'>No Devices</b></span>");
				$("#listCount_devicesmapped").text(0);				
			}else{
				$("#listCount_devicesmapped").text($("#devicesmapped").children().length);							 		
			}
		},
		onStart:function(evt){ },
		onEnd: function(evt){}
	});

}

function refreshRunConfigurationUnMappedList( productVersionId, productId, envionmentCombinationId, testRunPlanId){
	$("#alldevices").empty();	 
	var testRunPlanId=document.getElementById("hdnTestRunPlanId").value;
	var url="admin.genericDevices.list.mapping?productId="+productId+"&jtStartIndex=-1&jtPageSize=-1&filter=-1&productversionId="+productVersionId+"&environmentCombinationId="+envionmentCombinationId+"&testRunPlanId="+testRunPlanId+"&workpackageId=-1";

	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			var result = data.Records;
			if(result.length==0){
				$("#alldevices").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>No Devices</b></span>");
				$("#listCount_alldevices").text(result.length);
			}else{
				$.each(result, function(i,item){ 
					var devicesid = item.name;					
					devicesid=trim(devicesid);					
					var tempStatus=item.availableStatus;
					var status='';
					if(tempStatus==1){
						status='ACTIVE';
					}else{
						status='INACTIVE';
					}
					$("#alldevices").append("<li id='"+item.genericsDevicesId +"' title='"+item.UDID+"' style='color: black;'>"+devicesid+"-"+status+")</li>");
				});
				$("#listCount_alldevices").text(result.length);
			}
		}
	}); 
}

function refreshRunConfigurationMappedList(productVersionId, productId, envionmentCombinationId, testRunPlanId){
	$("#devicesmapped").empty();
	urlmappedDevices='administration.genericdevice.mappedList?productversionId='+productVersionId+"&productId="+productId+"&environmentCombinationId="+envionmentCombinationId+"&testRunPlanId="+testRunPlanId+"&workpackageId=-1&jtStartIndex=-1&jtPageSize=-1";
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : urlmappedDevices,
		dataType : 'json',
		success : function(data) {
			var listOfData = data.Records;
			if(listOfData.length==0){
				$("#devicesmapped").append("<span style='color: black;' id='emptyListMapped' disabled><b style='margin-left: 101px;'>No Devices</b></span>");
				$("#listCount_devicesmapped").text(listOfData.length);
			}else{
				$.each(listOfData, function(i,item){					
					var tsid = item.deviceName;					
					tsid=tsid;
					var tempStatus=item.availableStatus;
					var status='';
					if(tempStatus==1){
						status='ACTIVE';
					}else{
						status='INACTIVE';
					}
					if(item.UDID!=null)
						$("#devicesmapped").append("<li id='"+item.runconfigId +"'  title='"+item.UDID+"' style='color: black;'>"+tsid+"-"+status+"&"+item.hostName+"-"+item.hostStatus+")</li>");
				});
				$("#listCount_devicesmapped").text(listOfData.length);
			}
		}
	});
}
//environment plan ends

//Core Resources Starts
function listCoreResourcesOfSelectedProduct(){	
	try{
		if($('#jTableContainercoreresources').length > 0){
			$('#jTableContainercoreresources').jtable("destroy");
		}
	}catch(e){

	}
	$('#jTableContainercoreresources').jtable({

		title: 'Add/Edit Product Core Resources ', 
		editinline:{enable:true},
		paging: true, //Enable paging
		pageSize: 10, 
		recordsLoaded: function(event, data) {
			$(".jtable-edit-command-button").prop("disabled", true);
		},
		actions: { 
			listAction:  urlToGetCoreResourcesOfSpecifiedProductId, 
			editinlineAction: 'testfactory.product.coreResouces.update',  
			createAction: 'testfactory.product.coreResouces.add'
		}, 
		recordUpdated:function(event, data){
			$('#jTableContainercoreresources').find('.jtable-child-table-container').jtable('reload');
		},
		recordAdded: function (event, data) {
			$('#jTableContainercoreresources').find('.jtable-child-table-container').jtable('reload');
		},
		recordDeleted: function (event, data) {
			$('#jTableContainercoreresources').find('.jtable-child-table-container').jtable('reload');
		},
		fields: { 
			productId: { 
				type: 'hidden', 
				defaultValue: productId 
			}, 
			testFactoryId: { 
				type: 'hidden', 
				defaultValue: testFactoryId 
			},
			testFactoryProductCoreResourceId: { 
				key: true,
				list: false,
				create: false
			}, 
			resourcePoolId: { 
				title: 'ResourcePool',
				inputTitle: 'ResourcePool <font color="#efd125" size="4px">*</font>',
				width: "20%",
				list:false,
				create:true,
				options: 'testFactoryManagementControl.testfactory.resource.pool.list?testFactoryId='+testFactoryId
			},
			loginId: {
				title: 'User',
				width:"15%",
				list:true,
				create:false,
				edit:false,
				display: function (data) {
					return $('<a style="color: #0000FF;" href=javascript:showUserProfile('+data.record.userId+','+data.record.userDefaultRoleId+');>' + data.record.loginId + '</a>');
				}
			}, 		 	
			userId: {
				title: 'User',
				inputTitle: 'User <font color="#efd125" size="4px">*</font>',
				width:"15%",
				edit : false,
				list:false,
				dependsOn: 'resourcePoolId', 
				options: function(data) {
					if (data.source=='create') {
						if(data.dependedValues.resourcePoolId!=null){
							return 'common.administration.user.userListByResourcePoolId?resourcePoolId='+data.dependedValues.resourcePoolId+"&productId=-1";
						}
					}
				},
			},
			userDefaultRoleName: {
				title: 'Default Role',
				width:"15%",
				list:true,
				create:false,
				edit:false
			},
			userRoleId: {
				title: 'Role',
				inputTitle: 'Role Type <font color="#efd125" size="4px">*</font>',
				width:"15%",
				//options: 'testFactoryManagementControl.administration.user.roleList'
				dependsOn:'userId',
				options:function(data){
					if(data.dependedValues.userId!=null){
						return 'testFactoryManagementControl.administration.user.roleList?userId='+data.dependedValues.userId;
					}
				}
			}, 
			fromDate: { 
				title: 'From Date' ,
				inputTitle: 'From Date <font color="#efd125" size="4px">*</font>',
				edit: true, 
				list: true,
				type: 'date',
				width: "30%"          	
			},
			toDate: { 
				title: 'To Date' ,
				inputTitle: 'To Date <font color="#efd125" size="4px">*</font>',
				edit: true, 
				list: true,
				type: 'date',
				width: "30%"          	
			},
			remarks: { 
				title: 'Remarks' ,
				//inputTitle: 'Description <font color="#efd125" size="4px">*</font>',
				list:true
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
			commentsCoreResource:{
				title : '',
				list : true,
				create : false,
				edit : false,
				width: "5%",
				display:function (data) { 
					//Create an image for test script popup 
					var $img = $('<i class="fa fa-comments" title="Comments"></i>');
					$img.click(function () {
						var entityTypeIdComments = 55;
						var entityNameComments = "TestFactoryProductCoreResource";
						listComments(entityTypeIdComments, entityNameComments, data.record.testFactoryProductCoreResourceId, data.record.loginId, "tfCoreComments");
					});
					return $img;
				}		
			},

		}, 

		formSubmitting: function (event, data) {
//			var fromdate;
//			var todate;
			fromdate=$("input[name=fromDate]").val();
			todate=$("input[name=toDate]").val();
			if(new Date(fromdate)>new Date(todate))
			{
				$("#basicAlert").css("z-index", "100001");

				callAlert("Warning: From date should be lessthan or equal to Todate");
				return false;
			}
			data.form.find('input[name="fromDate"]').addClass('validate[required]');
			data.form.find('input[name="toDate"]').addClass('validate[required]');

			data.form.validationEngine();
			return data.form.validationEngine('validate');
		},  
		//Dispose validation logic when form is closed
		formClosed: function (event, data) {
			data.form.validationEngine('hide');
			data.form.validationEngine('detach');
		}	    		
	});

	$('#jTableContainercoreresources').jtable('load');

	var jscrolheight = $("#jTableContainercoreresources").height();
	var jscrolwidth = $("#jTableContainercoreresources").width()-20;

	$(".jScrollContainer").on("scroll", function() {
		var lastScrollLeft=0;	
		var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
		if (lastScrollLeft < documentScrollLeft) {
			$("#jTableContainercoreresources").width($(".jtable").width()).height($(".jtable").height());			
			lastScrollLeft = documentScrollLeft;
		}else if(lastScrollLeft >= documentScrollLeft){			
			$("#jTableContainercoreresources").width(jscrolwidth).height(jscrolheight);
		}			
	});

}

function listTestfactoryCoreResourcesOfSelectedProduct(){	
	try{
		if($('#jTableContainertestfactorycoreresources').length > 0){
			$('#jTableContainertestfactorycoreresources').jtable("destroy");
		}
	}catch(e){

	}
	$('#jTableContainertestfactorycoreresources').jtable({

		title: 'Test Factory Core Resources', 
		editinline:{enable:true},
		paging: true, //Enable paging
		pageSize: 10,
		actions: { 
			listAction: urlToGetTestfactoryCoreResourcesOfSpecifiedProductId, 
		}, 
		fields: { 
			testFactoryId: { 
				type: 'hidden', 
				defaultValue: testFactoryId
			}, 
			testFactoryCoreResourceId: { 
				key: true, 
				create: false, 
				edit: false, 
				list: false
			},
			resourcePoolId: { 
				title: 'Resource Pool',
				inputTitle: 'Resource Pool <font color="#efd125" size="4px">*</font>',
				width: "20%",
				list:false,
				create:true,
				edit: false, 
				options: 'testFactoryManagementControl.testfactory.resource.pool.list?testFactoryId=-1'
			},
			loginId: {
				title: 'User Name',
				width:"15%",
				list:true,
				edit:false,
				create:false,
				display: function (data) {
					return $('<a style="color: #0000FF;" href=javascript:showUserProfile('+data.record.userId+','+data.record.userDefaultRoleId+');>' + data.record.loginId + '</a>');
				}
			}, 
			userId: {
				title: 'User Name',
				inputTitle: 'User Name <font color="#efd125" size="4px">*</font>',
				width:"15%",
				edit : false,
				list:false,
				dependsOn: 'resourcePoolId', 
				options: function(data) {
					if (data.source=='create') {
						return 'common.administration.user.userListByResourcePoolId?resourcePoolId='+data.dependedValues.resourcePoolId+"&productId=-1";
					}
				},
			},
			userRoleId: {
				title: 'Role',
				inputTitle: 'Role Type <font color="#efd125" size="4px">*</font>',
				width:"15%",
				list:true,
				edit: false, 
				dependsOn: 'userId', 
				//options:'testFactoryManagementControl.administration.user.roleList'
				options:function(data){
					if(data.dependedValues.userId!=null){
						return 'testFactoryManagementControl.administration.user.roleList?userId='+data.dependedValues.userId;
					}

				}
			}, 
			fromDate: { 
				title: 'From Date' ,
				inputTitle: 'From Date <font color="#efd125" size="4px">*</font>',
				edit: true, 
				list: true,
				type: 'date',
				width: "30%"          	
			},
			toDate: { 
				title: 'To Date' ,
				inputTitle: 'To Date <font color="#efd125" size="4px">*</font>',
				edit: false, 
				list: true,
				width: "30%"          	
			},	
			remarks: { 
				title: 'Remarks' ,
				//inputTitle: 'Description <font color="#efd125" size="4px">*</font>',
				width: "35%",
				edit: false, 
				list:true
			},                  		
			status: {
				title: 'Status' ,
				inputTitle: 'Status <font color="#efd125" size="4px">*</font>',
				width: "10%",  
				list:true,
				edit:false,
				create:false,
				type : 'checkbox',
				values: {'0' : 'Disabled','1' : 'Active'},
				defaultValue: '1'
			},
		}, 

	});

	$('#jTableContainertestfactorycoreresources').jtable('load');

	var jscrolheight = $("#jTableContainertestfactorycoreresources").height();
	var jscrolwidth = $("#jTableContainertestfactorycoreresources").width()-20;

	$(".jScrollContainer").on("scroll", function() {
		var lastScrollLeft=0;	
		var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
		if (lastScrollLeft < documentScrollLeft) {
			$("#jTableContainertestfactorycoreresources").width($(".jtable").width()).height($(".jtable").height());			
			lastScrollLeft = documentScrollLeft;
		}else if(lastScrollLeft >= documentScrollLeft){			
			$("#jTableContainertestfactorycoreresources").width(jscrolwidth).height(jscrolheight);
		}			
	});
}
//Core Resources Ends

//BEGIN: ConvertDataTable - UserGroup
var userGroupDT_oTable='';
var editorUserGroup='';

function userGroupDataTable(){
	var url = 'user.group.list?testFactoryId='+testFactoryId+'&productId='+productId+'&isConsolidated=true&jtStartIndex=0&jtPageSize=10000';
	var jsonObj={"Title":"User Group",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"User Group",
	};
	userGroupDataTableContainer.init(jsonObj);
}

var userGroupDataTableContainer = function() {
	var initialise = function(jsonObj){
		assignUserGroupDataTableValues(jsonObj);
	};
	return {
		//main function to initiate the module
		init: function(jsonObj) {        	
			initialise(jsonObj);
		}		
	};	
}();

function assignUserGroupDataTableValues(jsonObj){
	openLoaderIcon();			
	$.ajax({
		type: "POST",
		url: jsonObj.url,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {		
			closeLoaderIcon();
			if(data.Result=="ERROR"){
				data = [];						
			}else{
				data = data.Records;
			}					
			jsonObj.data = data;
			userGroupDT_Container(jsonObj);
		},
		error : function(data) {
			closeLoaderIcon();  
		},
		complete: function(data){
			closeLoaderIcon();
		}
	});	
}

function userGroupDataTableHeader(){
	var childDivString ='<table id="userGroup_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
	'<tr>'+
	'<th>Name</th>'+
	'<th>Description</th>'+
	'<th>Users Mapping</th>'+
	'</tr>'+		
	'</thead>'+
	'<tfoot>'+
	'<tr>'+
	'<th></th>'+ 
	'<th></th>'+
	'<th></th>'+
	'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function userGroupDT_Container(jsonObj){

	try{
		if ($("#dataTableContainerForUserGroup").children().length>0) {
			$("#dataTableContainerForUserGroup").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = userGroupDataTableHeader(); 			 
	$("#dataTableContainerForUserGroup").append(childDivString);

	editorUserGroup = new $.fn.dataTable.Editor( {
		"table": "#userGroup_dataTable",
		ajax: 'user.group.add?testFactoryId='+testFactoryId+'&productId='+productId,
		ajaxUrl: 'user.group.update',
		idSrc:  "userGroupId",
		i18n: {
			create: {
				title:  "Create a User Group",
				submit: "Create",
			}
		},
		fields: []
	});

	userGroupDT_oTable = $("#userGroup_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "90%",
		"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = [2]; // search column TextBox Invisible Column position
			$('#userGroup_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeUserGroupDT();
		},  
		buttons: [
		          //{ extend: "create", editor: editorUserGroup },								 
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'User Group',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'User Group',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "userGroupName", className: 'disableEditInline', sWidth: '45%' },
		                                   { mData: "description", className: 'disableEditInline', sWidth: '45%' },
		                                   { mData: null,				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
		                                				   '<img src="css/images/mapping.png" class="usersMappingImg" title="Users Mapping" />'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   ],       
		                                   rowCallback: function ( row, data ) {
		                                	   // $('input.editorUserGroup-active', row).prop( 'checked', data.status == 1 );
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 

	// Activate an inline edit on click of a table cell
	$('#userGroup_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorUserGroup.inline( this, {
			submitOnBlur: true
		} );
	});

	$('#userGroup_dataTable tbody').on('click', 'td .usersMappingImg', function () {
		var tr = $(this).closest('tr');
		var row = userGroupDT_oTable.DataTable().row(tr);
		manageUserMappingForGroup('', productId, productNameForWorkflowSummary, row.data().userGroupId, row.data().userGroupName);
	});

	$("#userGroup_dataTable_length").css('margin-top','8px');
	$("#userGroup_dataTable_length").css('padding-left','35px');		

	userGroupDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutUserGroupDT='';
function reInitializeUserGroupDT(){
	clearTimeoutUserGroupDT = setTimeout(function(){				
		userGroupDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutUserGroupDT);
	},200);
}
//END: ConvertDataTable - UserGroup

function showUserProfile(userId, roleId){

	var profileFilter = 1;
	setUserFieldValues(profileFilter,userId, roleId);
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : 'profile.management.plan.content?selecteduserId='+userId,
		dataType : 'html',
		success : function(data) {
			$("#div_SelectedUserProfile").html(data);
		},
		complete : function(data) {
			$("#div_PopupSelectedUserProfile").modal();
		}
	});	
}

var closeChildTableFlag=false;
//Product Team Resources - Ends
$(document).ready(function(){	
	//FormWizard.init();
	if (!jQuery().bootstrapWizard) {
		return;
	}

	$(document).on('change','#devtype_ul', function() {
		var statusid= $("#devstatus_ul").find('option:selected').val();
		var typeid= $("#devtype_ul").find('option:selected').val();
		if(typeid==1){
			urlToGetDevicesOfSpecifiedProductId= 'admin.genericDevices.list.product?productId='+productId+'&filter='+statusid; 
			flag=1;
			devicesNew(urlToGetDevicesOfSpecifiedProductId);

		}else if(typeid==2){
			urlToGetHost='administration.host.list.product?productId='+productId+'&filter='+statusid;
			flag=2;
			hostNew(urlToGetHost);

		}
	});	  
	utilizationWeekRange = '';
	getWeeksName();
});

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
		//$('#form_wizard_1').find('.button-submit').show();
		//displayConfirm();
	} else {
		$('#form_wizard_1').find('.button-next').show();
		//$('#form_wizard_1').find('.button-submit').hide();
	}
	//Metronic.scrollTo($('.page-title'));
}


function initializeBootstrapWizard() {
	$('#form_wizard_1').bootstrapWizard({
		'nextSelector': '.button-next',
		'previousSelector': '.button-previous',
		onTabClick: function (tab, navigation, index, clickedIndex) {
			formWizardNavigation(tab, navigation, clickedIndex);
			return true;
		},
		onNext: function (tab, navigation, index) {   	 
			formWizardNavigation(tab, navigation, index);		 
		},
		onPrevious: function (tab, navigation, index) {
			formWizardNavigation(tab, navigation, index);	  
		},
		onTabShow: function (tab, navigation, index) {
			var total = navigation.find('li').length;
			var current = index + 1;
			var $percent = (current / total) * 100;

			$('#form_wizard_1').find('.progress-bar').css({
				width: $percent + '%'
			});   
		}
	});
}

function formWizardNavigation(tab, navigation, index){
	var currActiveTab = index;
	var valid = "";
	if(currActiveTab==0){
		var firstTab = $("#form_wizard_1").find("#TestRunPlan");
		if(!(firstTab.hasClass("active in"))){
			firstTab.addClass("active in");
			firstTab.siblings(".tab-pane").removeClass("active in");
		}
		$('#form_wizard_1').find('.button-previous').hide();  		  

	}else if (currActiveTab==1){
		valid = testsuitesplan();
		if(valid == false) return false;

	}else if (currActiveTab==2){
		environmentPlan();

	}else if (currActiveTab==3){
		testRunPlanTestBeds();

	}else if(currActiveTab==4){  		  
		testRunPlanId=document.getElementById("hdnTestRunPlanId").value;
		testRunPlanType=document.getElementById("hdnTestRunPlanType").value;
		testRunPlan(testRunPlanId,testRunPlanType);
	}
	handleTitle(tab, navigation, parseInt(currActiveTab));
}

////Traceability - Starts /////////
function showFeatureTestCaseTree(filter){
	// --- start of testing TreePagination -------
	var isDataAvailabletoLoad = false; 
	if(filter == 0){
		console.log("dataTable :"+$('#featureTestCases_dataTable tbody tr').length);
		if ($('#featureTestCases_dataTable tbody tr').length==0) {			  
			callAlert("No Data Available");
		}else{
			isDataAvailabletoLoad=true;
		}
	}else if(filter == 1){
		console.log("jtable : "+$('#jTableContainerfeatures').length);
		if ($('#jTableContainerfeatures table tbody tr').length==1 && 
				$('#jTableContainerfeatures table tbody tr').text() == "No data available!") {			  
			callAlert("No Data Available");
		}else{
			isDataAvailabletoLoad=true;
		}
	}
	var headerName = document.getElementById("hdnProductName").value+" : Entity Traceability";
	if($("#hdnProductId").val() == "" || $("#hdnProductId").val() == null)$("#hdnProductId").val(productId);
	if(isDataAvailabletoLoad){
		var jsonObj={"Title":headerName,			          
				"urlToGetTree": 'product.feature.testcase.mapping.tree?productId='+$("#hdnProductId").val(),
				"urlToGetChildData": "child.features.list.of.parent?parentFeatureId=",	    			
		};	 
		TreePagination.init(jsonObj);


		var parentTemplate = featureAndTestCaseTraceability();		
		$("#treePaginationContainer .modal-body").html('');
		$("#treePaginationContainer .modal-body").append(parentTemplate);
		showTreePaginationForFeatureTraceability(jsonObj);
	}
	// ---- end of testing TreePagination ------
}
////Traceability - Ends /////////

function featureAndTestCaseTraceability(){
	var parentTemplate = '<div id="traceability">'+
	'<ul class="nav nav-tabs toolbarFullScreen" id="tablistActivities">'+
	'<li class="active"><a href="#featureTraceability" data-toggle="tab">Feature Traceabiity</a></li>'+					
	/*'<li><a href="#testcaseTraceability" data-toggle="tab">Testcase Traceability</a></li>'+	*/
	'</ul>'+	
	'<div class="tab-content">'+		
	'<div id="featureTraceability" class="tab-pane fade active in" style="overflow: hidden; height: 100%;">'+	
	'<div class="row">'+
	'<div class="col-lg-4 col-md-4" style="overflow:auto;">'+
	'<div id="parentTree" ></div>'+
	'</div>'+						
	'<div class="col-lg-8 col-md-8">'+
	'<div id="treeTestCaseFeatureMappingContainer"></div>'+
	'</div>'+						
	'<div class="col-lg-8 col-md-8" id="treeJTableDivId">'+
	'<div>'+
	/*'<div id="treeJtableTestCaseExecution"></div>'+*/
	'<div id="executionsTitle" style="font-size:16px;"></div>'+
	'<div id="dataTableContainerForExecutions"></div>'+
	'</div>'+
	'<br/>'+
	'<br/>'+
	'<div>'+
	/*'<div id="treeJtableTestCaseDefect"></div>'+*/
	'<div id="defectDetailsTitle" style="font-size:16px;"></div>'+
	'<div id="dataTableContainerForDefectDetails"></div>'+
	'</div>'+
	'</div>'+
	'<div class="col-lg-8 col-md-8" id="treeEntityDetails"></div>'+
	'<div class="col-lg-8 col-md-8">'+
	'<div id="treeTestScriptContainer"></div>'+
	'</div>'+
	'</div>'+
	'</div>'+
	/*'<div id="testcaseTraceability" class="tab-pane fade" style="overflow: hidden; height: 100%;">'+
	'</div>'+*/
	'</div>'+
	'</div>';		
	return parentTemplate;
}

function showTreePaginationForFeatureTraceability(jsonObj){
	nodeDroppedOnTreePopup=false;
	initializeRightSideHandlers();
	getParentNodes(jsonObj.urlToGetTree, jsonObj.urlToGetChildData);

	$("#treePaginationContainer h4").text("");
	$("#treePaginationContainer h4").text(jsonObj.Title);		

	$("#treeJtableTestCaseExecution, #treeJtableTestCaseDefect").empty();
	$('#treeEntityDetails').empty();
	$('#treeTestCaseFeatureMappingContainer').hide();
}

//////////////////Decoupling Category and Test case Mapping Tree - Begins ////////////////
function showDecouplingCTestCaseTree(){
	// --- start of testing TreePagination -------
	console.log("jtable : "+$('#jTableContainerdecoupling').length);
	if ($('#jTableContainerdecoupling table tbody tr').length==1 && 
			$('#jTableContainerdecoupling table tbody tr').text() == "No data available!") {			  
		callAlert("No Data Available");
	}else{
		var headerName = "Decoupling Category Test cases mapping of "+document.getElementById("hdnProductName").value;
		var jsonObj={"Title":headerName,			          
				"urlToGetTree": 'product.decoulpingCategory.testcase.mapping.tree?productId='+$("#hdnProductId").val(),
				"urlToGetChildData": 'child.decouplingcategories.list.of.parent?parentDcId=',	    			
		};	 
		TreePagination.init(jsonObj);
	}
	// ---- end of testing TreePagination ------
}
//////////////////Decoupling Category and Test case Mapping Tree - Ends ////////////////

//////////////////Test Suite and Test case Mapping Tree - Begins ////////////////
function showTestSuiteTestCaseTree(){
	// --- start of testing TreePagination -------
	console.log("jtable : "+$('#jTableContainertestsuite').length);
	if($("#hdnProductId").val() == "" || $("#hdnProductId").val() == null)$("#hdnProductId").val(productId);
	if ($('#jTableContainertestsuite table tbody tr').length==1 && 
			$('#jTableContainertestsuite table tbody tr').text() == "No data available!") {			  
		callAlert("No Data Available");
	}else{
		//var headerName = "Test Suite Test cases mapping of "+document.getElementById("hdnProductName").value;
		var headerName = "Test Suite Test Cases Mapping : Product ID : "+productId +" Name : "+document.getElementById("hdnProductName").value;
		var jsonObj={"Title":headerName,			          
				"urlToGetTree": 'product.testsuite.testcase.mapping.tree?productId='+productId,
				"urlToGetChildData": 'sub.testsuite.list.of.parent?parentId=',	    			
		};	 
		TreePagination.init(jsonObj);
	}
	// ---- end of testing TreePagination ------
}
//////////////////Test Suite and Test case Mapping Tree - Ends ////////////////

$(document).on('click', '.button-edit', function(){
	$(".button-submit").removeAttr('disabled');
	$(".testRunPlanDiv").find("input ,textarea, select, button").removeAttr('disabled');
});

function showCorrespondingTable(obj) {
	var toShow = $(obj).attr("data-name");
	if(toShow == "Feature") {
		$(".jtbFeature").show();
		$(".jtbCase, .jtbSuite").hide();
	}else if(toShow == "TestCase") {
		$(".jtbCase").show();
		$(".jtbFeature, .jtbSuite").hide();
	}else {
		$(".jtbSuite").show();
		$(".jtbCase, .jtbFeature").hide();
	}
}

function showCorrespondingTeamTable(obj) {
	if(obj == "1") {
		$(".teamTab").show();
		$(".teamUtilizationTab").hide();
		productTeamResources();
	}else if(obj == "2") {		
		$(".teamUtilizationTab").show();
		$(".teamTab").hide();
		getWeeksName();
		showTeamUtilization('TeamUtilization', 0, 0, 0, productId, 0, true,150,0);
	}
}

function showCorrespondingTestsTable(obj) {
	if(obj == "1") {
		$(".testCaseTab").show();
		$(".testScriptTab, .testSuiteTab, .testPlanTab, .testPlanGroupTab, .testDataTab, .objectrepositoryTab, .testDataAttachmentTab, .eDATAttachmentTab").hide();
		$('#toAnimate .portlet .actions').eq(0).css('display','block');
		testcases();
	}else if(obj == "2") {
		testScripts();
		$(".testScriptTab").show();
		$(".testCaseTab, .testSuiteTab, .testPlanTab, .testPlanGroupTab, .testDataTab, .objectrepositoryTab, .testDataAttachmentTab, .eDATAttachmentTab").hide();
	}else if(obj == "3") {
		testsuitesTabFlag=true;
		testsuites(nodeType);
		$(".testSuiteTab").show();
		$(".testCaseTab, .testScriptTab, .testPlanTab, .testPlanGroupTab, .testDataTab, .objectrepositoryTab, .testDataAttachmentTab, .eDATAttachmentTab").hide();
	}else if(obj == "4") {
		productMgmtTestRun();
		$(".testPlanTab").show();
		$(".testCaseTab, .testScriptTab, .testSuiteTab, .testPlanGroupTab, .testDataTab, .objectrepositoryTab, .testDataAttachmentTab, .eDATAttachmentTab").hide();
	}else if(obj == "5") {
		productMgmtTestrunplangroup();
		$(".testPlanGroupTab").show();
		$(".testCaseTab, .testScriptTab, .testSuiteTab,  .testPlanTab,.testDataTab, .objectrepositoryTab, .testDataAttachmentTab, .eDATAttachmentTab").hide();
	}else if(obj == "6") {
		pageName="MYTESTDATA";
		$(".testDataTab").show();
		$(".testCaseTab, .testScriptTab, .testSuiteTab, .testPlanTab, .testPlanGroupTab, .objectrepositoryTab, .testDataAttachmentTab, .eDATAttachmentTab").hide();
		if(nodeType == "Product"){
			productId = key;
			productVersionId = -1;
			if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
				callAlert("Please select the Product");		
				return false;
			}
		}else if(nodeType == "ProductVersion"){
			productVersionId = key;
			if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
				productId=-1;
			}
			if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == ''){
				callAlert("Please select the Product Version");		
				return false;
			}
		}
		listTestDataItemsDataTable(productId);
	}
	else if(obj == "7") {
		pageName="OBJECTREPOSITORY";
		$(".objectrepositoryTab").show();
		$(".testCaseTab, .testScriptTab, .testSuiteTab, .testPlanTab, .testPlanGroupTab, .testDataTab, .testDataAttachmentTab, .eDATAttachmentTab").hide();
		if(nodeType == "Product"){
			productId = key;
			productVersionId = -1;
			if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
				callAlert("Please select the Product");		
				return false;
			}
		}else if(nodeType == "ProductVersion"){
			productVersionId = key;
			if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
				productId=-1;
			}
			if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == ''){
				callAlert("Please select the Product Version");		
				return false;
			}
		}
		objectRepositoryListDataTable(productId);
	}
	else if(obj == "8") {
		pageName = "TESTDATAATTACHMENTS";
		$(".testDataAttachmentTab").show();
		$(".testCaseTab, .testScriptTab, .testSuiteTab, .testPlanTab, .testPlanGroupTab, .objectrepositoryTab, .testDataTab, .eDATAttachmentTab").hide();
		if(nodeType == "Product"){
			productId = key;
			productVersionId = -1;
			if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");		
			return false;
			}
		}else if(nodeType == "ProductVersion"){
			productVersionId = key;
			if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			 productId=-1;
			}
			if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == ''){
				callAlert("Please select the Product Version");		
				return false;
				}
		}
		showTestData(productId, productVersionId);
	}
	else if(obj == "9") {
		pageName = "EDAT";
		$(".eDATAttachmentTab").show();
		$(".testCaseTab, .testScriptTab, .testSuiteTab, .testPlanTab, .testPlanGroupTab, .objectrepositoryTab, .testDataTab, .testDataAttachmentTab").hide();
		eDataDataTable();
	}
}

function listOfFeatureProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId){
	try{
		if ($('#jTableContainerfeaturetestplan').length>0) {
			$('#jTableContainerfeaturetestplan').jtable('destroy'); 
		}
	}catch(e){}
	//init jTable
	$('#jTableContainerfeaturetestplan').jtable({
		title: 'Feature',
		selecting: true, //Enable selecting	
		multiselect : false, //Allow multiple selecting
		selectingCheckboxes : false, //Show checkboxes on first column
		paging: true, //Enable paging
		pageSize: 10, //Set page size (default: 10)
		editinline:{enable:false},	         
		actions: {
			listAction: 'administration.product.feature.list.testrunplan?productMasterId='+productId+'&productVersionId='+productVersionId+'&testRunPlanId='+testRunPlanId+'&type='+type, 
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

						return '<input type="checkbox" id="status" checked onclick=saveFeatureMap(0,'+data.record.productFeatureId+'); value='+data.record.status+'>';
					}else if(data.record.isSelected==0){

						return '<input type="checkbox" id="status" onclick=saveFeatureMap(1,'+data.record.productFeatureId+'); value='+data.record.status+'>';
					}else{
						return '<input type="checkbox" id="status" onclick=saveFeatureMap(1,'+data.record.productFeatureId+'); value='+data.record.status+'>';
					}
				}
			},
			productFeatureId: { 
				key: true,
				type: 'hidden',
				create: false, 
				edit: false, 
				list: false ,
				display: function (data) { 
					return data.record.productFeatureId;
				} 
			}, 
			productFeatureCode : {
				title: 'Feature Code',
				inputTitle: 'Feature Code <font color="#efd125" size="4px">*</font>',
				width:"20%",
				list: true,
				edit : true,
				create : true    	        	
			},		
			productFeatureName: { 
				title: 'Feature Name',
				inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
				list:true,
				width: "20%",
			},
			displayName: { 
				title: 'Display Name',
				width: "20%",
				list:true,
				edit: false,
				create:false
			},
			productFeatureDescription: { 
				title: 'Feature Description' ,
				width: "20%",  
				list:true
			},   				 
			parentFeatureId:{
				title: 'Parent Feature',
				width:"20%",
				list:true,
				edit: true,
				create : true,
				inputTitle: 'Parent Feature <font color="#efd125" size="4px">*</font>',
				dependsOn: 'productFeatureId',
				options: function (data) 
				{	
					if(data.source =='list')
					{
						var atype='list';
						data.clearCache();
						return 'common.list.parentfeature.list?productID='+productId+'&productFeatureId=' + data.dependedValues.productFeatureId+'&actionType='+atype;
					}else if(data.source =='create'){
						var atype='create';
						data.clearCache();
						var childID = data.dependedValues.productFeatureId;
						if(childID != undefined){
							return 'common.list.parentfeature.list?productID='+productId+'&productFeatureId=' + data.dependedValues.productFeatureId+'&actionType='+atype;
						}else{
							return 'common.list.parentfeature.list?productID='+productId+'&productFeatureId=' +0+'&actionType='+atype;
						}
					}else if(data.source =='edit'){
						var atype='edit';
						data.clearCache();
						var childID = data.dependedValues.productFeatureId;
						if(childID != undefined){
							return 'common.list.parentfeature.list?productID='+productId+'&productFeatureId=' + data.dependedValues.productFeatureId+'&actionType='+atype;
						}else{
							return 'common.list.parentfeature.list?productID='+productId+'&productFeatureId=' +0+'&actionType='+atype;
						}
					}
					var atype="default";
					//data.clearCache();
					return 'common.list.parentfeature.list?productID='+productId+'&productFeatureId=' + 0+'&actionType='+atype;
				}
			},
			mappedTestCases: {
				title:'',
				width: "2%",
				create:false,
				edit:true,
				display: function (childData) { 
					//Create an image that will be used to open child table 
					var $img = $('<img src="css/images/list_metro.png" title="Mapped TestCase List" />'); 
					//Open child table when user clicks the image 
					$img.click(function (data) { 
						$('#jTableContainerfeaturetestplan').jtable('openChildTable', 
								$img.closest('tr'), 
								{ 
							title: 'Mapped Test Cases',
							paging: true, //Enable paging
							pageSize: 10, //Set page size (default: 10)
							selecting: true, //Enable selecting 
							editinline:{enable:false},					        	  	
							actions: { 
								listAction: 'product.feature.testcase.mappedlist?productFeatureId='+childData.record.productFeatureId,
								recordUpdated:function(event, data){
									$('#jTableContainerfeaturetestplan').find('.jtable-main-container').jtable('reload');
								},
								recordAdded: function (event, data){
									$('#jTableContainerfeaturetestplan').find('.jtable-main-container').jtable('reload');
								},
							},
							fields: {		      
								productId:{
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
								testCaseName: {
									title: 'Test Case Name ',
									inputTitle: 'Test Case Name <font color="#efd125" size="4px">*</font>',
									width: "12%",
									create : true,
									edit :true
								},
								testSuiteId:{
									title: 'Test Suite', 
									width: "7%",
									type : 'hidden',
									create: false,
									edit : false,
									list : false,
								},
								testSuiteName:{
									title: 'TestSuite Name', 
									width: "7%",                         
									create: false,
									list: false
								},
								testCaseDescription:{
									title: 'Test Case Description', 
									inputTitle: 'Test Case Description <font color="#efd125" size="4px">*</font>',
									width: "40%",                         
									create: true   
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
									inputTitle: 'Test Case Code <font color="#efd125" size="4px">*</font>',
									width: "10%",                        
									create: true
								},
								testCaseScriptQualifiedName:{
									title: 'Script Package Name',  
									width: "10%",
									create: true,
									list : true, 
									edit : false
								},
								testCaseScriptFileName:{
									title: 'Script File Name',  
									width: "10%",
									create: true,
									list : true, 
									edit : true
								}, 
								executionTypeId:{
									title : 'Execution Type',
									width : "10%",
									create : true,
									list : true,
									edit : true,
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
									width : "10%",
									create : true,
									list : true,
									edit : true,
									options:function(data){
										if(data.source =='list'){	      				
											return 'common.list.testcasepriority';	
										}else if(data.source == 'create'){	      				
											return 'common.list.testcasepriority';
										}
									}
								},
								testcaseTypeId:{
									title : 'Test Case Type',
									width : "13%",
									create : true,
									list : true,
									edit : true,
									options:function(data){
										if(data.source =='list'){	      				
											return 'common.list.testcasetype';	
										}else if(data.source == 'create'){	      				
											return 'common.list.testcasetype';
										}
									}
								},
								productFeatureId: {
									title: 'Features',
									width:"15%",
									list:true,
									edit: true,
									create : true,
									options:function(data){		      				
										if(data.source =='list'){	      				
											return 'product.feature.list?productId='+productId;	
										}else if(data.source == 'create'){	      				
											return 'product.feature.list?productId='+productId;
										}
									}		            
								},				         
							},
							// This is for closing $img.click(function (data) { 
							//Moved Formcreate validation to Form Submitting
							//Validate form when it is being submitted
							formSubmitting: function (event, data) {
								data.form.find('input[name="testCaseName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[minSize[3]], custom[maxSize[25]]');
								data.form.find('input[name="testCaseDescription"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[25]]');
								data.form.find('input[name="testCaseCode"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[15]]');
								data.form.validationEngine();
								return data.form.validationEngine('validate');
							}, 
							//Dispose validation logic when form is closed
							formClosed: function (event, data) {
								data.form.validationEngine('hide');
								data.form.validationEngine('detach');
							}
								}, 
								function (data) { //opened handler 
									data.childTable.jtable('load'); 
								}); 
					}); 
					return $img; 
				},
			},	

		},	          	
		//Moved Formcreate validation to Form Submitting
		//Validate form when it is being submitted
		formSubmitting: function (event, data) {
			data.form.find('input[name="productFeatureName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[minSize[3]]');
			data.form.find('input[name="productFeatureCode"]').addClass('validate[required, custom[minSize[3]], custom[maxSize[20]]');
			data.form.validationEngine();
			return data.form.validationEngine('validate');
		}, 
		//Dispose validation logic when form is closed
		formClosed: function (event, data) {
			data.form.validationEngine('hide');
			data.form.validationEngine('detach');
		}
	});		 
	$('#jTableContainerfeaturetestplan').jtable('load');		 
}

function listOfTestCaseProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId){
	try{
		//alert("productId----"+productId);
		if ($('#jTableContainertestcasetestplan').length>0) {
			$('#jTableContainertestcasetestplan').jtable('destroy'); 
		}
	}catch(e){}
	//init jTable
	$('#jTableContainertestcasetestplan').jtable({
		title: 'Test Cases',
		paging: true, //Enable paging
		pageSize: 10, //Set page size (default: 10)
		editinline:{enable:true},	      
		actions: { 
			listAction: 'product.testcase.list.testRunPlan?productMasterId='+productId+'&productVersionId='+productVersionId+'&testRunPlanId='+testRunPlanId+'&type='+type,	        	

		}, 
		fields: { 
			isSelected: {
				title: 'Selected',
				list:true,
				create: false,
				edit:true,
				type : 'checkbox',
				values: {'0' : 'No','1' : 'Yes'},
				display:function(data){
					if(data.record.isSelected==1){

						return '<input type="checkbox" id="status" checked onclick=saveTestCaseMap(0,'+data.record.testCaseId+'); value='+data.record.status+'>';
					}else if(data.record.isSelected==0){

						return '<input type="checkbox" id="status" onclick=saveTestCaseMap(1,'+data.record.testCaseId+'); value='+data.record.status+'>';
					}else{
						return '<input type="checkbox" id="status" onclick=saveTestCaseMap(1,'+data.record.testCaseId+'); value='+data.record.status+'>';
					}
				}
			},

			testStepsDetails: {
				title:'',
				width: "2%",
				create:false,
				edit:true,
				display: function (childData) { 
					//Create an image that will be used to open child table 
					var $img = $('<img src="css/images/list_metro.png" title="Test Step List" />'); 
					//Open child table when user clicks the image 
					$img.click(function (data) { 
						$('#jTableContainertestcasetestplan').jtable('openChildTable', 
								$img.closest('tr'), 
								{ 
							title: 'Test Steps',
							editinline:{enable:true},	
							actions: { 
								listAction: 'testcase.teststep.list?testCaseId='+childData.record.testCaseId,
								recordUpdated:function(event, data){
									$('#jTableContainertestcasetestplan').find('.jtable-child-table-container').jtable('reload');
								},
								recordAdded: function (event, data) {
									$('#jTableContainertestcasetestplan').find('.jtable-child-table-container').jtable('reload');
								}
							}, 
							fields: { 
								testCaseId:{
									type: 'hidden',  
									list:false,
									defaultValue: childData.record.testCaseId
								},
								testStepName: { 
									title: 'Test Step Name', 
									inputTitle: 'Test Step Name <font color="#efd125" size="4px">*</font>',
									width: "20%",                          
									create: true,
									edit: true
								},
								testStepDescription:{
									title: 'Test Step Description',  
									inputTitle: 'Test Step Description <font color="#efd125" size="4px">*</font>',
									width: "20%",                          
									create: true,
									edit: true
								},
								testStepInput: { 
									title: 'Test Step Input', 
									inputTitle: 'Test Step Input <font color="#efd125" size="4px">*</font>',
									width: "10%",                         
									create: true,
									edit: true
								},
								testStepExpectedOutput: { 
									title: 'Test Step Expected Output',  
									width: "15%",                        
									create: true,
									edit: true
								},
								testStepSource: { 
									title: 'Test Step Source',  
									width: "7%",         
									create: true							                 
								},
								testStepCode: { 
									title: 'Test Step Code',
									inputTitle: 'Test Step Code <font color="#efd125" size="4px">*</font>',
									width: "7%",         
									create: true,
									edit: true
								},
								testStepId:{
									type: 'hidden',  
									list:false,
								},
							},
							// This is for closing $img.click(function (data) { 
							//Moved Formcreate validation to Form Submitting
							//Validate form when it is being submitted
							formSubmitting: function (event, data) {
								data.form.find('input[name="testStepName"]').addClass('validate[required, custom[Letters_loworup_noSpec]]');
								data.form.find('input[name="testStepDescription"]').addClass('validate[required]');
								data.form.find('input[name="testStepInput"]').addClass('validate[required]');
								data.form.find('input[name="testStepCode"]').addClass('validate[required]');
								data.form.validationEngine();
								return data.form.validationEngine('validate');
							}, 
							//Dispose validation logic when form is closed
							formClosed: function (event, data) {
								data.form.validationEngine('hide');
								data.form.validationEngine('detach');
							}
								}, 
								function (data) { //opened handler 
									data.childTable.jtable('load'); 
								}); 
					}); 
					return $img; 
				}, 
			},
			productId:{
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
			testCaseName: {
				title: 'Test Case Name ',
				inputTitle: 'Test Case Name <font color="#efd125" size="4px">*</font>',
				width: "12%",
				create : true,
				edit :true/* ,
                display: function (data) { 
    			return data.record.testCaseName;
                }, */
			},
			testSuiteId:{
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
				create: false,
				list: false
			},
			testCaseDescription:{
				title: 'Test Case Description', 
				inputTitle: 'Test Case Description <font color="#efd125" size="4px">*</font>',
				width: "40%",                         
				create: true   
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
				inputTitle: 'Test Case Code <font color="#efd125" size="4px">*</font>',
				width: "10%",                        
				create: true
			},
			testCaseScriptQualifiedName:{
				title: 'Script Package Name',  
				width: "10%",
				create: true,
				list : true, 
				edit : false
			},
			testCaseScriptFileName:{
				title: 'Script File Name',  
				width: "10%",
				create: true,
				list : true, 
				edit : true
			}, 
			executionTypeId:{
				title : 'Execution Type',
				width : "10%",
				create : true,
				list : true,
				edit : true,
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
				width : "10%",
				create : true,
				list : true,
				edit : true,
				options:function(data){
					if(data.source =='list'){	      				
						return 'common.list.testcasepriority';	
					}else if(data.source == 'create'){	      				
						return 'common.list.testcasepriority';
					}
				}
			},   

			testcaseTypeId:{
				title : 'Test Case Type',
				width : "13%",
				create : true,
				list : true,
				edit : true,
				options:function(data){
					if(data.source =='list'){	      				
						return 'common.list.testcasetype';	
					}else if(data.source == 'create'){	      				
						return 'common.list.testcasetype';
					}
				}
			},
			productFeatureId: {
				title: 'Features',
				width:"15%",
				list: false,
				edit: true,
				create : true,
				options:function(data){		      				
					if(data.source =='list'){	      				
						return 'product.feature.list?productId='+productId;	
					}else if(data.source == 'create'){	      				
						return 'product.feature.list?productId='+productId;
					}
				}		            
			},

		},  // This is for closing $img.click(function (data) { 
		//Moved Formcreate validation to Form Submitting
		//Validate form when it is being submitted
		formSubmitting: function (event, data) {
			data.form.find('input[name="testCaseName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[minSize[3]], custom[maxSize[25]]');
			data.form.find('input[name="testCaseDescription"]').addClass('validate[required],custom[minSize[3]]');
			data.form.find('input[name="testCaseCode"]').addClass('validate[required],custom[minSize[3]], custom[maxSize[15]]');
			data.form.validationEngine();
			return data.form.validationEngine('validate');
		}, 
		//Dispose validation logic when form is closed
		formClosed: function (event, data) {
			data.form.validationEngine('hide');
			data.form.validationEngine('detach');
		}
	});
	$('#jTableContainertestcasetestplan').jtable('load');   
}

function createIndicesForTestcase(){
	if(nodeType == "Product"){
		productId = key;
		if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");
			return false;
		}
	}

	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : 'elastic.search.testcase?productId='+productId,
		dataType : 'json',
		success : function(data) {
			if(data.Result=="ERROR"){
				callAlert(data.Message);
				return false;
			}else{

				if(data.Result=="SUCCESS"){
					callAlert("Successfully testcase collection Index created");
					testcases();
				}
			}

		}
	});
}

function showTestData(productId, productVersionId){
	var headerName = 'TestData';
	var jsonObj={"Title":headerName,			          
			"urlToGetTree": 'product.version.testobjectdata.attachments.list?versionId='+productVersionId+'&productId='+productId,	    					 
			"versionId":productVersionId,
			"productId":productId,
	};	 
	TestDatOfJobsPagination.init(jsonObj);	    
}

function listTestDataAuditHistory(attachmentId){
	clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=Attachment&sourceEntityId='+attachmentId+'&jtStartIndex=0&jtPageSize=1000';
	var jsonObj={"Title":"Attachment Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":1000,
			"componentUsageTitle":"attachmentAudit",
	};
	SingleDataTableContainer.init(jsonObj);
}

function showCorrespondingTableTool(obj) {
	var toShow = obj;
	$(".jtbToolTMS, .jtbToolDMS, .jtbToolAS, .jtbToolSCMS").hide()
	if(toShow == 1) {
		$(".jtbToolTMS").show();
		$(".jtbToolDMS, .jtbToolAS, .jtbToolSCMS").hide();
		tableType = "TMSTable";
		testManagementSystemDataTable();
	}else if(toShow == 2) {
		$(".jtbToolDMS").show();
		$(".jtbToolTMS, .jtbToolAS, .jtbToolSCMS").hide();
		tableType = "DMSTable";
		defectManagementSystemDataTable();		
	}else if(toShow == 3) {
		$(".jtbToolSCMS").show();
		$(".jtbToolTMS, .jtbToolAS, .jtbToolDMS").hide();
		tableType = "SCMSTable";
		scmManagementSystemDataTable();
	}else { 
		$(".jtbToolAS").show();
		$(".jtbToolDMS, .jtbToolTMS, .jtbToolSCMS").hide();
		listAnalyticSystem();
	}
}

//BEGIN: ConvertDataTable - TestManagementSystem
var testManagementSystemDT_oTable='';
var editorTestManagementSystem='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;
var tableType;
var tmsId,dmsId,tmsMappingId,dmsMappingId;
var editorOpenFlag=false;

function testManagementSystemDataTable(){
	optionsItemCounter=0;
	optionsResultArr=[];
	if(tableType == "TMSTable"){
		optionsArr = [{id:"systemTypeList", url:'common.systemType.list'},{id:"toolIntagrationId", url:'administration.tool.intagration.master.option.list?typeId=1'}];
	}else if(tableType == "TMSMappingTable"){
		optionsArr = [{id:"pdtTypeList", url:'common.list.entities?entityType=Product&productId='+productId},{id:"toolIntagrationId", url:'administration.tool.intagration.master.option.list?typeId=2'}];
	}else if(tableType == "DMSMappingTable"){
		optionsArr = [{id:"pdtTypeList", url:'common.list.entities?entityType=Product&productId='+productId},{id:"toolIntagrationId", url:'administration.tool.intagration.master.option.list?typeId=3'}];
	}
	testManagementSystemOptions_Container(optionsArr);
}

function testManagementSystemOptions_Container(urlArr){
	$.ajax( {
		"type": "POST",
		"url":  urlArr[optionsItemCounter].url,
		"dataType": "json",
		success: function (json) {		        	 
			if(json.Result == "OK"){						 
				if(json.Options.length>0){     		   
					for(var i=0;i<json.Options.length;i++){
						json.Options[i].label=json.Options[i].DisplayText;
						json.Options[i].value=json.Options[i].Value;
					}			   
				}else{
					json.Options=[];
				}     	   
				optionsResultArr.push(json.Options);						 
			}
			optionsItemCounter++;	
			if(optionsItemCounter<optionsArr.length){
				testManagementSystemOptions_Container(optionsArr);
			}else{
				testManagementSystemDataTableInit();
			}					 
		},
		error: function (data) {
			optionsItemCounter++;
		},
		complete: function(data){
			//console.log('Completed');
		},	            
	});
}

function defectManagementSystemDataTable(){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [{id:"toolIntagrationId", url:'administration.tool.intagration.master.option.list?typeId=2'}];
	defectManagementSystemOptions_Container(optionsArr);
}

function defectManagementSystemOptions_Container(urlArr){
	$.ajax( {
		"type": "POST",
		"url":  urlArr[optionsItemCounter].url,
		"dataType": "json",
		success: function (json) {		        	 
			if(json.Result == "OK"){						 
				if(json.Options.length>0){     		   
					for(var i=0;i<json.Options.length;i++){
						json.Options[i].label=json.Options[i].DisplayText;
						json.Options[i].value=json.Options[i].Value;
					}			   
				}else{
					json.Options=[];
				}     	   
				optionsResultArr.push(json.Options);						 
			}
			optionsItemCounter++;	
			if(optionsItemCounter<optionsArr.length){
				defectManagementSystemOptions_Container(optionsArr);
			}else{
				testManagementSystemDataTableInit();
			}					 
		},
		error: function (data) {
			optionsItemCounter++;
		},
		complete: function(data){
			//console.log('Completed');
		},	            
	});
}



function scmManagementSystemDataTable(){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [{id:"toolIntagrationId", url:'administration.tool.intagration.master.option.list?typeId=3'}];

	scmManagementSystemOptions_Container(optionsArr);
}

function scmManagementSystemOptions_Container(urlArr){
	$.ajax( {
		"type": "POST",
		"url":  urlArr[optionsItemCounter].url,
		"dataType": "json",
		success: function (json) {		        	 
			if(json.Result == "OK"){						 
				if(json.Options.length>0){     		   
					for(var i=0;i<json.Options.length;i++){
						json.Options[i].label=json.Options[i].DisplayText;
						json.Options[i].value=json.Options[i].Value;
					}			   
				}else{
					json.Options=[];
				}     	   
				optionsResultArr.push(json.Options);						 
			}
			optionsItemCounter++;	
			if(optionsItemCounter<optionsArr.length){
				scmManagementSystemOptions_Container(optionsArr);
			}else{
				testManagementSystemDataTableInit();
			}					 
		},
		error: function (data) {
			optionsItemCounter++;
		},
		complete: function(data){
			//console.log('Completed');
		},	            
	});
}

function testManagementSystemDataTableInit(){
	var url,jsonObj={};
	if(tableType=="TMSTable"){
		url= 'administration.test.management.system.list?productId='+ productId +'&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Test Management System","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Test Management System"};
	}else if(tableType == "TMSMappingTable"){
		url= 'administration.test.management.mapping.list?testManagementSystemId='+tmsId+'&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Test Management System Mapping","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Test Management System Mapping"};		
	}else if(tableType=="DMSTable"){
		url= 'administration.defect.management.system.list?productId='+ productId +'&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Defect Management System","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Defect Management System"};
	}else if(tableType == "DMSMappingTable"){
		url= 'administration.defect.management.mapping.list?defectManagementSystemId='+dmsId+'&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Defect Management System Mapping","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Defect Management System Mapping"};		
	}else if(tableType=="SCMSTable"){
		var scmSystemStatus = $("#scmSystemStatus_ul").find('option:selected').val();
		url= 'administration.scm.management.list.by.product.and.status?productId='+ productId +'&status='+scmSystemStatus;
		jsonObj={"Title":"SCM System","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"SCM System"};
	}
	testManagementSystemDataTableContainer.init(jsonObj);
}

$(document).on('change','#scmSystemStatus_ul', function() {
	tableType = "SCMSTable";
	testManagementSystemDataTableInit();
});

var testManagementSystemDataTableContainer = function() {
	var initialise = function(jsonObj){
		assignTestManagementSystemDataTableValues(jsonObj);
	};
	return {
		//main function to initiate the module
		init: function(jsonObj) {        	
			initialise(jsonObj);
		}		
	};	
}();

function assignTestManagementSystemDataTableValues(jsonObj){
	openLoaderIcon();			
	$.ajax({
		type: "POST",
		url: jsonObj.url,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {		
			closeLoaderIcon();
			if(data.Result=="ERROR"){
				data = [];						
			}else{
				data = data.Records;
			}					
			jsonObj.data = data;
			if(tableType == "TMSTable"){
				testManagementSystemDT_Container(jsonObj);
			}else if(tableType == "TMSMappingTable"){
				tmsMappingDT_Container(jsonObj);
			}else if(tableType == "DMSTable"){
				defectManagementSystemDT_Container(jsonObj);
			}else if(tableType == "DMSMappingTable"){
				dmsMappingDT_Container(jsonObj);
			}else if(tableType == "SCMSTable"){
				scmSystemDT_Container(jsonObj);
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

function testManagementSystemDataTableHeader(){
	var childDivString ='<table id="testManagementSystem_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
	'<tr>'+
	'<th>Title</th>'+
	'<th>Description</th>'+
	'<th>Schedule</th>'+
	'<th>Test System</th>'+
	'<th>System Type</th>'+
	'<th>Primary</th>'+
	'<th>Version</th>'+
	'<th>Connection URL</th>'+
	'<th>Username</th>'+
	'<th>Password</th>'+
	'<th>Domain</th>'+
	'<th>Test Set Folder Filter(ID)</th>'+
	'<th>Test Set Folder Filter(Name)</th>'+
	'<th></th>'+
	/*'<th>Test Connection</th>'+
	'<th>Add/Remove TMS Mapping</th>'+
	'<th>Audit History</th>'+
	'<th>Comments</th>'+*/
	'</tr>'+		
	'</thead>'+
	'<tfoot>'+
	'<tr>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+ 
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+ 
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+ 
	'<th></th>'+
	'<th></th>'+
	/*'<th></th>'+ 
	'<th></th>'+
	'<th></th>'+*/
	'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function testManagementSystemDT_Container(jsonObj){

	try{
		if ($("#dataTableContainerForTMS").children().length>0) {
			$("#dataTableContainerForTMS").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = testManagementSystemDataTableHeader(); 			 
	$("#dataTableContainerForTMS").append(childDivString);

	editorTestManagementSystem = new $.fn.dataTable.Editor( {
		"table": "#testManagementSystem_dataTable",
		ajax: "administration.test.management.system.add",
		ajaxUrl: "administration.test.management.system.update",
		idSrc:  "testManagementSystemId",
		i18n: {
			create: {
				title:  "Create a new Test Management System",
				submit: "Create",
			}
		},
		fields: [
		         {
		        	 label: "Product",
		        	 name: "productId",
		        	 "def":productId,
		        	 "type":"hidden"
		         },{
		        	 label: "TestManagementSystemId",
		        	 name: "testManagementSystemId",
		        	 "def":tmsId,
		        	 "type":"hidden"
		         },{
		        	 label: "Title *",
		        	 name: "title",
		         },{
		        	 label: "Description",
		        	 name: "description",
		         },{
		        	 label: "Test System",
		        	 name: "toolIntagrationId",
		        	 options: optionsResultArr[1],
		        	 "type":"select"
		         },/*{
		        	 label: "Test System",
		        	 name: "testSystemName",
		        	 options: [{ label: 'HPQC', value: "HPQC" },
		        	           { label: 'TFS', value: "TFS" },
		        	           { label: 'SVN', value: "SVN" }],
		        	           "type":"select"
		         },*/{
		        	 label: "System Type",
		        	 name: "testSystemTypeId",
		        	 options: optionsResultArr[0],
		        	 "type":"select"
		         },{
		        	 label: "Primary",
		        	 name: "isPrimary",
		        	 "type":"checkbox",
		        	 separator: "|",
		        	 options:   [{ label: '', value: 1 }]
		         },{
		        	 label: "Version *",
		        	 name: "testSystemVersion",
		         },{
		        	 label: "Connection URL *",
		        	 name: "connectionUri",
		         },{
		        	 label: "Username *",
		        	 name: "connectionUserName",
		         },{
		        	 label: "Password *",
		        	 name: "connectionPassword",
		         },{
		        	 label: "Domain",
		        	 name: "connectionProperty1",
		         },{
		        	 label: "Test Set Folder Filter(ID)",
		        	 name: "connectionProperty2",
		         },{
		        	 label: "Test Set Folder Filter(Name)",
		        	 name: "connectionProperty3",
		         },{
		        	 label: "Test Case Filter(ID)",
		        	 name: "connectionProperty4",
		         },{
		        	 label: "Resource Filter(ID)",
		        	 name: "connectionProperty5",
		         }        
		         ]
	});

	testManagementSystemDT_oTable = $("#testManagementSystem_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "90%",
		"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = [2,5]; // search column TextBox Invisible Column position
			$('#testManagementSystem_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeTestManagementSystemDT();
		},  
		buttons: [
		          { 
		        	  extend: "create",
		        	  editor: editorTestManagementSystem
		          },	
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Test Management System',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Test Management System',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "title", className: 'editable', sWidth: '15%' },	
		                                   { mData: "description", className: 'editable', sWidth: '15%' },
		                                   { mData: null, className:'disableEditInline',				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<img src="css/images/clock.png" class="tmsSchedulesImg" title="Schedules" />'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },
		                                  // { mData: "testSystemName", className: 'editable', sWidth: '12%' },
		                                   { mData: "toolIntagrationId", className: 'editable', sWidth: '10%', editField: "toolIntagrationId",
		                                	   mRender: function (data, type, full) {
		                                		   data = optionsValueHandler(editorTestManagementSystem, 'toolIntagrationId', full.toolIntagrationId); 		           	 
		                                		   return data;
		                                	   },
		                                   },
		                                   { mData: "testSystemTypeId", className: 'editable', sWidth: '10%', editField: "testSystemTypeId",
		                                	   mRender: function (data, type, full) {
		                                		   data = optionsValueHandler(editorTestManagementSystem, 'testSystemTypeId', full.testSystemTypeId); 		           	 
		                                		   return data;
		                                	   },
		                                   },
		                                   { mData: "isPrimary",
		                                	   mRender: function (data, type, full) {
		                                		   if ( type === 'display' ) {
		                                			   return '<input type="checkbox" class="editorTestManagementSystem-active">';
		                                		   }
		                                		   return data;
		                                	   },
		                                	   className: "dt-body-center"
		                                   },
		                                   { mData: "testSystemVersion", className: 'editable', sWidth: '12%' },
		                                   { mData: "connectionUri", className: 'editable', sWidth: '12%' },
		                                   { mData: "connectionUserName", className: 'editable', sWidth: '10%' },
		                                   { mData: "connectionPassword", className: 'editable', sWidth: '12%' },
		                                   { mData: "connectionProperty1", className: 'editable', sWidth: '25%' },
		                                   { mData: "connectionProperty2", className: 'editable', sWidth: '12%' },
		                                   { mData: "connectionProperty3", className: 'editable', sWidth: '12%' },
		                                   { mData: null, className: 'disableEditInline', 				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<img src="css/images/testconnection.gif" class="tmsTestConnectionImg" title="Test Connection" />'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<img src="css/images/execute_metro.png" class="tmsSyndataImg" title="Data Sync" /></button>'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<img src="css/images/list_metro.png" class="tmsMappingImg" title="Add/Remove Test Management System Mapping" />'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-search-plus tmsAuditHistoryImg" title="Audit History"></i></button>'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-comments tmsCommentsImg" title="Comments"></i></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },
		                                   ],
		                                   columnDefs: [],
		                                   rowCallback: function ( row, data ) {
		                                	   $('input.editorTestManagementSystem-active', row).prop( 'checked', data.isPrimary == 1 );
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 

	// Activate an inline edit on click of a table cell
	$('#testManagementSystem_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorTestManagementSystem.inline( this, {
			submitOnBlur: true
		} );
	});	

	$('#testManagementSystem_dataTable tbody').on('click', 'td .tmsSchedulesImg', function () {
		var tr = $(this).closest('tr');
		var row = testManagementSystemDT_oTable.DataTable().row(tr);
		var tooltipSchedule = "Schedule For "+row.data().testSystemName;	
		var titleSchedule = "<"+row.data().testSystemName+"> Test Management System : Sync Schedule";
		var subTitleSchedule = "Product "+$(headerTitle).text();
		var testRunPlanID = "";
		var testRunPlanType = "TestRunPlan";
		scheduleUsingCronGen(titleSchedule, subTitleSchedule, testRunPlanID, testRunPlanType);
	});	

	$('#testManagementSystem_dataTable tbody').on('click', 'td .tmsTestConnectionImg', function () {
		var tr = $(this).closest('tr');
		var row = testManagementSystemDT_oTable.DataTable().row(tr);
		testTMSConnection(row.data().testManagementSystemId);
	});
	
	$('#testManagementSystem_dataTable tbody').on('click', 'td .tmsSyndataImg', function () {
		var tr = $(this).closest('tr');
		var row = testManagementSystemDT_oTable.DataTable().row(tr);
		displayTestmanagementSyndata(row.data().testManagementSystemId,"Test Management Systems");
		
	});

	$('#testManagementSystem_dataTable tbody').on('click', 'td .tmsMappingImg', function () {
		var tr = $(this).closest('tr');
		var row = testManagementSystemDT_oTable.DataTable().row(tr);
		tmsId = row.data().testManagementSystemId;
		tableType = "TMSMappingTable";
		testManagementSystemDataTable();
		$('#tmsDT_Child_Container').modal();
		$(document).off('focusin.modal');
	});

	$('#testManagementSystem_dataTable tbody').on('click', 'td .tmsAuditHistoryImg', function () {
		var tr = $(this).closest('tr');
		var row = testManagementSystemDT_oTable.DataTable().row(tr);
		listGenericAuditHistory(row.data().testManagementSystemId,"TestManagementSystem","testManagementSystemAudit");
	});

	$('#testManagementSystem_dataTable tbody').on('click', 'td .tmsCommentsImg', function () {
		var tr = $(this).closest('tr');
		var row = testManagementSystemDT_oTable.DataTable().row(tr);
		var entityTypeIdComments = 11;
		var entityNameComments = "TestManagementSystem";
		listComments(entityTypeIdComments, entityNameComments, row.data().testManagementSystemId, row.data().title, "testMSComments");
	});	

	$('#testManagementSystem_dataTable').on( 'change', 'input.editorTestManagementSystem-active', function () {
		editorTestManagementSystem
		.edit( $(this).closest('tr'), false )
		.set( 'isPrimary', $(this).prop( 'checked' ) ? 1 : 0 )
		.submit();
	});

	$("#testManagementSystem_dataTable_length").css('margin-top','8px');
	$("#testManagementSystem_dataTable_length").css('padding-left','35px');		
	
	editorTestManagementSystem.on( 'preSubmit', function ( e, o, action ) {
        if ( action !== 'remove' ) {
        	var validationArr = ['title','connectionUserName','connectionPassword','testSystemVersion','connectionUri'];
        	var inputText;
        	for(var i=0;i<validationArr.length;i++){
	            inputText = this.field(validationArr[i]);
	            if ( ! inputText.isMultiValue() ) {
	                if ( inputText.val() ) {
                	}else{
	                	if(validationArr[i] == "title"){
	                		inputText.error( 'Please enter Title' );
	                	}if(validationArr[i] == "connectionUserName"){
	                		inputText.error( 'Please enter Username' );
	                	}if(validationArr[i] == "connectionPassword"){
	                		inputText.error( 'Please enter Password' );
						}if(validationArr[i] == "testSystemVersion"){
	                		inputText.error( 'Please enter Version' );
						}if(validationArr[i] == "connectionUri"){
	                		inputText.error( 'Please enter URL' );
						}	
                	}
	            }
        	}

            // If any error was reported, cancel the submission so it can be corrected
            if ( this.inError() ) {
                return false;
            }
        }
    } );

	testManagementSystemDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutTestManagementSystemDT='';
function reInitializeTestManagementSystemDT(){
	clearTimeoutTestManagementSystemDT = setTimeout(function(){				
		testManagementSystemDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTestManagementSystemDT);
	},200);
}

function fullScreenHandlerDTTestManagementSystem(){
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		var height = Metronic.getViewPort().height -
		$('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
		parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
		parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));

		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
		$('#testFacMode').css('max-height',displaytestFaceModeResponsive(window.innerWidth));

		testManagementSystemDTFullScreenHandler(true);
		reInitializeTestManagementSystemDT();
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
		$('#testFacMode').css('max-height','');

		reInitializeTestManagementSystemDT();				
		testManagementSystemDTFullScreenHandler(false);			
	}
}

function testManagementSystemDTFullScreenHandler(flag){
	if(flag){
		reInitializeTestManagementSystemDT();
		$("#testManagementSystem_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
	}else{
		reInitializeTestManagementSystemDT();
		$("#testManagementSystem_dataTable_wrapper .dataTables_scrollBody").css('max-height','450px');
	}
}

function displaytestFaceModeResponsive(widthValue){
	var resultWidth="";
	if(widthValue<768){
		resultWidth = 200;			
	}else if(widthValue<992){
		resultWidth = 300;
	}else if(widthValue<1200){
		resultWidth = 400;
	}else if(widthValue<1500){
		resultWidth = 500;			
	}else if(widthValue<1600){
		resultWidth = 600;
	}else if(widthValue<1800){
		resultWidth = 700;
	}else if(widthValue<2050){
		resultWidth = 750;
	}else if(widthValue<2400){
		resultWidth = 850;
	}else if(widthValue<3000){
		resultWidth = 1100;
	}else if(widthValue<4000){
		resultWidth = 1300;
	}else if(widthValue<5000){
		resultWidth = 1500;
	}

	return resultWidth+'px';
}
//END: ConvertDataTable - TestManagementSystem

//BEGIN: ConvertDataTable - TMSMapping
function tmsMappingDataTableHeader(){
	var childDivString ='<table id="tmsMapping_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
	'<tr>'+
	'<th>Mapping Entity</th>'+
	'<th>Entity Name</th>'+
	'<th>Mapping Value</th>'+
	'<th>Description</th>'+
	'</tr>'+		
	'</thead>'+
	'<tfoot>'+
	'<tr>'+
	'<th></th>'+
	'<th></th>'+ 
	'<th></th>'+
	'<th></th>'+
	'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function tmsMappingDT_Container(jsonObj){

	try{
		if ($("#dataTableContainerForTMSMapping").children().length>0) {
			$("#dataTableContainerForTMSMapping").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = tmsMappingDataTableHeader(); 			 
	$("#dataTableContainerForTMSMapping").append(childDivString);

	editorTMSMapping = new $.fn.dataTable.Editor( {
		"table": "#tmsMapping_dataTable",
		ajax: "administration.test.management.mapping.add",
		ajaxUrl: 'administration.test.management.mapping.update?testManagementSystemId='+tmsId+'&productId='+productId,
		idSrc:  "testManagementSystemMappingId",
		i18n: {
			create: {
				title:  "Create a new Test Management System Mapping",
				submit: "Create",
			}
		},
		fields: [
		         {
		        	 label: "Product",
		        	 name: "productId",
		        	 "def":productId,
		        	 "type":"hidden"
		         },{
		        	 label: "Mapping Entity",
		        	 name: "mappingType",
		        	 options: [{ label: 'Product', value: "Product" },
		        	           { label: 'Product Version', value: "Product Version" },
		        	           { label: 'Test Suite', value: "Test Suite" }],
		        	           "type":"select"
		         },{
		        	 label: "Entity Name",
		        	 name: "mappedEntityNameInTAFOptions",
		        	 options: optionsResultArr[0],
		        	 "type":"select"
		         },{
		        	 label: "Mapping Value",
		        	 name: "mappingValue",
		         },{
		        	 label: "Description",
		        	 name: "mappingValueDescription",
		         },{
		        	 label: "testManagementSystemId",
		        	 name: "testManagementSystemId",
		        	 "def":tmsId,
		        	 "type":"hidden"
		         },{
		        	 label: "testManagementSystemMappingId",
		        	 name: "testManagementSystemMappingId",
		        	 "def":tmsMappingId,
		        	 "type":"hidden"
		         }        
		         ]
	});

	tmsMappingDT_oTable = $("#tmsMapping_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "90%",
		"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
			$('#tmsMapping_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeTMSMappingDT();
		},  
		buttons: [
		          { 
		        	  extend: "create",
		        	  editor: editorTMSMapping
		          },	
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Test Management System Mapping',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Test Management System Mapping',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "mappingType", className: 'disableEditInline', sWidth: '25%' },	
		                                   { mData: "mappedEntityNameInTAFOptions", className: 'disableEditInline', sWidth: '25%' },
		                                   { mData: "mappingValue", className: 'editable', sWidth: '25%' },
		                                   { mData: "mappingValueDescription", className: 'editable', sWidth: '25%' },
		                                   ],
		                                   columnDefs: [],
		                                   rowCallback: function ( row, data ) {
		                                	   // $('input.editorTMSMapping-active', row).prop( 'checked', data.isPrimary == 1 );
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 

	// Activate an inline edit on click of a table cell
	$('#tmsMapping_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorTMSMapping.inline( this, {
			submitOnBlur: true
		} );
	});	

	$("#tmsMapping_dataTable_length").css('margin-top','8px');
	$("#tmsMapping_dataTable_length").css('padding-left','35px');

	editorTMSMapping.dependent('mappingType',function ( val, data, callback ) {        	
		var entityId = 0;					       								
		if(val != undefined && editorOpenFlag){			
			entityId = val;
			var url = 'common.list.entities?entityType='+entityId+'&productId='+productId;
			$.ajax( {
				url: url,
				type: "POST",
				dataType: 'json',
				success: function ( json ) {			    	        	
					for(var i=0;i<json.Options.length;i++){
						json.Options[i].label=json.Options[i].DisplayText;
						json.Options[i].value=json.Options[i].Value;
					}
					json.url = url;
					editorTMSMapping.set('mappedEntityNameInTAFOptions',json.Options);
					editorTMSMapping.field('mappedEntityNameInTAFOptions').update(json.Options);
				}
			} );
		}
	});
	
	editorTMSMapping.on( 'open', function ( e, json, data ) {
		if(data=="create"){
			editorOpenFlag=true;
		}
	} );
	
	editorTMSMapping.on( 'close', function ( e, json, data ) {
		editorOpenFlag=false;
	} );
	
	editorTMSMapping.on( 'create', function ( e, json, data ) {
	    tmsMappingId = json.Record[0].testManagementSystemMappingId;
	    tableType = "TMSMappingTable";
		testManagementSystemDataTable();
	} );

	tmsMappingDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutTMSMappingDT='';
function reInitializeTMSMappingDT(){
	clearTimeoutTMSMappingDT = setTimeout(function(){				
		tmsMappingDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTMSMappingDT);
	},200);
}
//END: ConvertDataTable - TMSMapping

//BEGIN: ConvertDataTable - DefectManagementSystem
function defectManagementSystemDataTableHeader(){
	var childDivString ='<table id="defectManagementSystem_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Title</th>'+
			'<th>Description</th>'+
			'<th>Defect System</th>'+
			'<th>Version</th>'+
			'<th>Connection URL</th>'+
			'<th>Primary</th>'+
			'<th>Username</th>'+
			'<th>Password</th>'+
			'<th></th>'+
			/*'<th>Test Connection</th>'+
			'<th>Add/Remove DMS Mapping</th>'+
			'<th>Audit History</th>'+
			'<th>Comments</th>'+*/
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			/*'<th></th>'+
			'<th></th>'+
			'<th></th>'+ */
		'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function defectManagementSystemDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForDMS").children().length>0) {
			$("#dataTableContainerForDMS").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = defectManagementSystemDataTableHeader(); 			 
	$("#dataTableContainerForDMS").append(childDivString);

	editorDefectManagementSystem = new $.fn.dataTable.Editor( {
		"table": "#defectManagementSystem_dataTable",
		ajax: "administration.defect.management.system.add",
		ajaxUrl: "administration.defect.management.system.update",
		idSrc:  "defectManagementSystemId",
		i18n: {
			create: {
				title:  "Create a new Defect Management System",
				submit: "Create",
			}
		},
		fields: [
		         {
		        	 label: "Product",
		        	 name: "productId",
		        	 "def":productId,
		        	 "type":"hidden"
		         },{
		        	 label: "defectManagementSystemId",
		        	 name: "defectManagementSystemId",
		        	 "def":dmsId,
		        	 "type":"hidden"
		         },{
		        	 label: "Title *",
		        	 name: "title",
		         },{
		        	 label: "Description",
		        	 name: "description",
		         },/*{
		        	 label: "Defect System",
		        	 name: "defectSystemName",
		        	 options: [{ label: 'JIRA', value: "JIRA" },
		        	           { label: 'HPQC', value: "HPQC" },
		        	           { label: 'TFS', value: "TFS" }],
		        	           "type":"select"
		         },*/{
		        	 label: "Defect System",
		        	 name: "toolIntagrationId",
		        	 options: optionsResultArr[0],
		        	 "type":"select"
		         },{
		        	 label: "Version *",
		        	 name: "defectSystemVersion",
		         },{
		        	 label: "Connection URL *",
		        	 name: "connectionUri",
		         },{
		        	 label: "Primary",
		        	 name: "isPrimary",
		        	 "type":"checkbox",
		        	 separator: "|",
		        	 options:   [{ label: '', value: 1 }]
		         },{
		        	 label: "Username *",
		        	 name: "connectionUserName",
		         },{
		        	 label: "Password *",
		        	 name: "connectionPassword",
		         },{
		        	 label: "Property 1",
		        	 name: "connectionProperty1",
		         },{
		        	 label: "Property 2",
		        	 name: "connectionProperty2",
		         },{
		        	 label: "Property 3",
		        	 name: "connectionProperty3",
		         },{
		        	 label: "Property 4",
		        	 name: "connectionProperty4",
		         }        
		         ]
	});

	defectManagementSystemDT_oTable = $("#defectManagementSystem_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "90%",
		"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = [5]; // search column TextBox Invisible Column position
			$('#defectManagementSystem_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeDefectManagementSystemDT();
		},  
		buttons: [
		          { 
		        	  extend: "create",
		        	  editor: editorDefectManagementSystem
		          },	
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Defect Management System',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Defect Management System',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "title", className: 'editable', sWidth: '15%' },	
		                                   { mData: "description", className: 'editable', sWidth: '15%' },
		                                   //{ mData: "defectSystemName", className: 'editable', sWidth: '12%' },
		                                   { mData: "toolIntagrationId", className: 'editable', sWidth: '10%', editField: "toolIntagrationId",
		                                	   mRender: function (data, type, full) {
		                                		   data = optionsValueHandler(editorDefectManagementSystem, 'toolIntagrationId', full.toolIntagrationId); 		           	 
		                                		   return data;
		                                	   },
		                                   },
		                                   { mData: "defectSystemVersion", className: 'editable', sWidth: '12%' },
		                                   { mData: "connectionUri", className: 'editable', sWidth: '12%' },
		                                   { mData: "isPrimary",
		                                	   mRender: function (data, type, full) {
		                                		   if ( type === 'display' ) {
		                                			   return '<input type="checkbox" class="editorDefectManagementSystem-active">';
		                                		   }
		                                		   return data;
		                                	   },
		                                	   className: "dt-body-center"
		                                   },
		                                   { mData: "connectionUserName", className: 'editable', sWidth: '10%' },
		                                   { mData: "connectionPassword", className: 'editable', sWidth: '12%' },
		                                   { mData: null, className: 'disableEditInline', 				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<img src="css/images/testconnection.gif" class="dmsTestConnectionImg" title="Test Connection" />'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<img src="css/images/execute_metro.png" class="dmsSyndataImg" title="Data Sync" /></button>'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<img src="css/images/list_metro.png" class="dmsMappingImg" title="Add/Remove Defect Management System Mapping" />'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-search-plus dmsAuditHistoryImg" title="Audit History"></i></button>'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-comments dmsCommentsImg" title="Comments"></i></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },
		                                   
		                                   ],
		                                   columnDefs: [],
		                                   rowCallback: function ( row, data ) {
		                                	   $('input.editorDefectManagementSystem-active', row).prop( 'checked', data.isPrimary == 1 );
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 

	// Activate an inline edit on click of a table cell
	$('#defectManagementSystem_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorDefectManagementSystem.inline( this, {
			submitOnBlur: true
		} );
	});	
	
	editorDefectManagementSystem.on( 'create', function ( e, json, data ) {
		dmsId = json.Record.defectManagementSystemId;
	} );

	$('#defectManagementSystem_dataTable tbody').on('click', 'td .dmsTestConnectionImg', function () {
		var tr = $(this).closest('tr');
		var row = defectManagementSystemDT_oTable.DataTable().row(tr);
		testDMSConnection(row.data().defectManagementSystemId);
	});
	
	$('#defectManagementSystem_dataTable tbody').on('click', 'td .dmsSyndataImg', function () {
		var tr = $(this).closest('tr');
		var row = defectManagementSystemDT_oTable.DataTable().row(tr);
		displayTestmanagementSyndata(row.data().defectManagementSystemId,"Defect Management System");
	});

	$('#defectManagementSystem_dataTable tbody').on('click', 'td .dmsMappingImg', function () {
		var tr = $(this).closest('tr');
		var row = defectManagementSystemDT_oTable.DataTable().row(tr);
		if(row.data().defectManagementSystemId=="" || row.data().defectManagementSystemId== undefined){
			row.data().defectManagementSystemId = dmsId;
		}
		dmsId = row.data().defectManagementSystemId;
		tableType = "DMSMappingTable";
		testManagementSystemDataTable();
		$('#dmsDT_Child_Container').modal();
		$(document).off('focusin.modal');
	});

	$('#defectManagementSystem_dataTable tbody').on('click', 'td .dmsAuditHistoryImg', function () {
		var tr = $(this).closest('tr');
		var row = defectManagementSystemDT_oTable.DataTable().row(tr);
		listGenericAuditHistory(row.data().defectManagementSystemId,"DefectManagementSystem","defectManagementSystemAudit");
	});

	$('#defectManagementSystem_dataTable tbody').on('click', 'td .dmsCommentsImg', function () {
		var tr = $(this).closest('tr');
		var row = defectManagementSystemDT_oTable.DataTable().row(tr);
		var entityTypeIdComments = 12;
		var entityNameComments = "DefectManagementSystem";
		listComments(entityTypeIdComments, entityNameComments, row.data().defectManagementSystemId, row.data().title, "defectMSComments");
	});	

	$('#defectManagementSystem_dataTable').on( 'change', 'input.editorDefectManagementSystem-active', function () {
		editorDefectManagementSystem
		.edit( $(this).closest('tr'), false )
		.set( 'isPrimary', $(this).prop( 'checked' ) ? 1 : 0 )
		.submit();
	});

	$("#defectManagementSystem_dataTable_length").css('margin-top','8px');
	$("#defectManagementSystem_dataTable_length").css('padding-left','35px');	
	
	editorDefectManagementSystem.on( 'preSubmit', function ( e, o, action ) {
        if ( action !== 'remove' ) {
        	var validationArr = ['title','connectionUserName','connectionPassword','defectSystemVersion','connectionUri'];
        	var inputText;
        	for(var i=0;i<validationArr.length;i++){
	            inputText = this.field(validationArr[i]);
	            if ( ! inputText.isMultiValue() ) {
	                if ( inputText.val() ) {
                	}else{
	                	if(validationArr[i] == "title"){
	                		inputText.error( 'Please enter Title' );
	                	}if(validationArr[i] == "connectionUserName"){
	                		inputText.error( 'Please enter Username' );
	                	}if(validationArr[i] == "connectionPassword"){
	                		inputText.error( 'Please enter Password' );
						}if(validationArr[i] == "defectSystemVersion"){
	                		inputText.error( 'Please enter Version' );
						}if(validationArr[i] == "connectionUri"){
	                		inputText.error( 'Please enter URL' );
						}	
                	}
	            }
        	}

            // If any error was reported, cancel the submission so it can be corrected
            if ( this.inError() ) {
                return false;
            }
        }
    } );

	defectManagementSystemDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutDefectManagementSystemDT='';
function reInitializeDefectManagementSystemDT(){
	clearTimeoutDefectManagementSystemDT = setTimeout(function(){				
		defectManagementSystemDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDefectManagementSystemDT);
	},200);
}
//END: ConvertDataTable - DefectManagementSystem

//BEGIN: ConvertDataTable - DMSMapping
function dmsMappingDataTableHeader(){
	var childDivString ='<table id="dmsMapping_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Mapping Entity</th>'+
			'<th>Entity Name</th>'+
			'<th>Mapping Value</th>'+
			'<th>Description</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function dmsMappingDT_Container(jsonObj){
	try{
		if ($("#dataTableContainerForDMSMapping").children().length>0) {
			$("#dataTableContainerForDMSMapping").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = dmsMappingDataTableHeader(); 			 
	$("#dataTableContainerForDMSMapping").append(childDivString);

	editorDMSMapping = new $.fn.dataTable.Editor( {
		"table": "#dmsMapping_dataTable",
		ajax: "administration.defect.management.mapping.add",
		ajaxUrl: 'administration.defect.management.mapping.update?defectManagementSystemId='+dmsId+'&productId='+productId,
		idSrc:  "defecManagementSystemMappingId",
		i18n: {
			create: {
				title:  "Create a new Defect Management System Mapping",
				submit: "Create",
			}
		},
		fields: [
		         {
		        	 label: "Product",
		        	 name: "productId",
		        	 "def":productId,
		        	 "type":"hidden"
		         },{
		        	 label: "Mapping Entity",
		        	 name: "mappingType",
		        	 options: [{ label: 'Product', value: "Product" },
		        	           { label: 'Product Version', value: "Product Version" }],
		        	           "type":"select"
		         },{
		        	 label: "Entity Name",
		        	 name: "mappedEntityNameInTAFOptions",
		        	 options: optionsResultArr[0],
		        	 "type":"select"
		         },{
		        	 label: "Mapping Value",
		        	 name: "mappingValue",
		         },{
		        	 label: "Description",
		        	 name: "mappingValueDescription",
		         },{
		        	 label: "defectManagementSystemId",
		        	 name: "defectManagementSystemId",
		        	 "def":dmsId,
		        	 "type":"hidden"
		         },{
		        	 label: "defecManagementSystemMappingId",
		        	 name: "defecManagementSystemMappingId",
		        	 "def":dmsMappingId,
		        	 "type":"hidden"
		         }         
		         ]
	});

	dmsMappingDT_oTable = $("#dmsMapping_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "90%",
		"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
			$('#dmsMapping_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeDMSMappingDT();
		},  
		buttons: [
		          { 
		        	  extend: "create",
		        	  editor: editorDMSMapping
		          },	
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Defect Management System Mapping',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Defect Management System Mapping',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "mappingType", className: 'disableEditInline', sWidth: '25%' },	
		                                   { mData: "mappedEntityNameInTAFOptions", className: 'disableEditInline', sWidth: '25%' },
		                                   { mData: "mappingValue", className: 'editable', sWidth: '25%' },
		                                   { mData: "mappingValueDescription", className: 'editable', sWidth: '25%' },
		                                   ],
		                                   columnDefs: [],
		                                   rowCallback: function ( row, data ) {
		                                	   // $('input.editorDMSMapping-active', row).prop( 'checked', data.isPrimary == 1 );
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 


	// Activate an inline edit on click of a table cell
	$('#dmsMapping_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorDMSMapping.inline( this, {
			submitOnBlur: true
		} );
	});	

	$("#dmsMapping_dataTable_length").css('margin-top','8px');
	$("#dmsMapping_dataTable_length").css('padding-left','35px');

	editorDMSMapping.dependent('mappingType',function ( val, data, callback ) {        	
		var entityId = 0;					       								
		if(val != undefined && editorOpenFlag){			
			entityId = val;
			var url = 'common.list.entities?entityType='+entityId+'&productId='+productId;
			$.ajax( {
				url: url,
				type: "POST",
				dataType: 'json',
				success: function ( json ) {			    	        	
					for(var i=0;i<json.Options.length;i++){
						json.Options[i].label=json.Options[i].DisplayText;
						json.Options[i].value=json.Options[i].Value;
					}
					json.url = url;
					editorDMSMapping.set('mappedEntityNameInTAFOptions',json.Options);
					editorDMSMapping.field('mappedEntityNameInTAFOptions').update(json.Options);
				}
			} );
		}
	});
	
	editorDMSMapping.on( 'open', function ( e, json, data ) {
		if(data=="create"){
			editorOpenFlag=true;
		}
	} );
	
	editorDMSMapping.on( 'close', function ( e, json, data ) {
		editorOpenFlag=false;
	} );
	
	editorDMSMapping.on( 'create', function ( e, json, data ) {
	    dmsMappingId = json.Record[0].defecManagementSystemMappingId;
	    tableType = "DMSMappingTable";
		defectManagementSystemDataTable();
	} );

	dmsMappingDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutDMSMappingDT='';
function reInitializeDMSMappingDT(){
	clearTimeoutDMSMappingDT = setTimeout(function(){				
		dmsMappingDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDMSMappingDT);
	},200);
}
//END: ConvertDataTable - DMSMapping

//BEGIN: ConvertDataTable - SCMSystem
function scmSystemDataTableHeader(){
	var childDivString ='<table id="scmSystem_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>Title</th>'+
			'<th>Description</th>'+
			'<th>SCM System</th>'+
			'<th>Version</th>'+
			'<th>Connection URL</th>'+
			'<th>Primary</th>'+
			'<th>Username</th>'+
			'<th>Password</th>'+
			'<th>Status</th>'+
			'<th>Property 1</th>'+
			'<th>Property 2</th>'+
			'<th></th>'+
		/*	'<th>Audit History</th>'+
			'<th>Comments</th>'+*/
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			/*'<th></th>'+
			'<th></th>'+*/
		'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}

function scmSystemDT_Container(jsonObj){
	try{
		if ($("#dataTableContainerForSCMS").children().length>0) {
			$("#dataTableContainerForSCMS").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = scmSystemDataTableHeader(); 			 
	$("#dataTableContainerForSCMS").append(childDivString);

	editorSCMSystem = new $.fn.dataTable.Editor( {
		"table": "#scmSystem_dataTable",
		ajax: "administration.scm.management.system.add",
		ajaxUrl: "administration.scm.management.system.update",
		idSrc:  "id",
		i18n: {
			create: {
				title:  "Create a new SCM System",
				submit: "Create",
			}
		},
		fields: [
		         {
		        	 label: "Product",
		        	 name: "productId",
		        	 "def":productId,
		        	 "type":"hidden"
		         },{
		        	 label: "ID",
		        	 name: "id",
		        	 "type":"hidden",
		         },{
		        	 label: "Title *",
		        	 name: "title",
		         },{
		        	 label: "Description",
		        	 name: "description",
		         },/*{
		        	 label: "SCM System",
		        	 name: "scmSystemName",
		        	 options: [{ label: 'SVN', value: "SVN" },
		        	           { label: 'GitHub', value: "GitHub" },
		        	           { label: 'BitBucket', value: "BitBucket" }],
		        	           "type":"select"
		         },*/{
		        	 label: "SCM System",
		        	 name: "toolIntagrationId",
		        	 options: optionsResultArr[0],
		        	 "type":"select"
		         },{
		        	 label: "Version *",
		        	 name: "scmSystemVersion",
		         },{
		        	 label: "Connection URL *",
		        	 name: "connectionUri",
		         },{
		        	 label: "Primary",
		        	 name: "isPrimary",
		        	 "type":"checkbox",
		        	 separator: "|",
		        	 options:   [{ label: '', value: 1 }]
		         },{
		        	 label: "Username *",
		        	 name: "connectionUserName",
		         },{
		        	 label: "Password *",
		        	 name: "connectionPassword",
		         },{
		        	 label: "Status",
		        	 name: "status",
		        	 "def":'1',
					 "type":"hidden",
		         },{
		        	 label: "Property 1",
		        	 name: "connectionProperty1",
		         },{
		        	 label: "Property 2",
		        	 name: "connectionProperty2",
		         },{
		        	 label: "Property 3",
		        	 name: "connectionProperty3",
		         },{
		        	 label: "Property 4",
		        	 name: "connectionProperty4",
		         }        
		         ]
	});

	scmSystemDT_oTable = $("#scmSystem_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "90%",
		"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = [6]; // search column TextBox Invisible Column position
			$('#scmSystem_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeSCMSystemDT();
		},  
		buttons: [
		          { 
		        	  extend: "create",
		        	  editor: editorSCMSystem
		          },	
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'SCM System',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'SCM System',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "id", className: 'disableEditInline', sWidth: '10%'},
		                                   { mData: "title", className: 'editable', sWidth: '15%' },	
		                                   { mData: "description", className: 'editable', sWidth: '15%' },
		                                   //{ mData: "scmSystemName", className: 'editable', sWidth: '12%' },
		                                   { mData: "toolIntagrationId", className: 'editable', sWidth: '10%', editField: "toolIntagrationId",
		                                	   mRender: function (data, type, full) {
		                                		   data = optionsValueHandler(editorSCMSystem, 'toolIntagrationId', full.toolIntagrationId); 		           	 
		                                		   return data;
		                                	   },
		                                   },
		                                   { mData: "scmSystemVersion", className: 'editable', sWidth: '12%' },
		                                   { mData: "connectionUri", className: 'editable', sWidth: '12%' },
		                                   { mData: "isPrimary",
		                                	   mRender: function (data, type, full) {
		                                		   if ( type === 'display' ) {
		                                			   return '<input type="checkbox" class="editorSCMSystem-active">';
		                                		   }
		                                		   return data;
		                                	   },
		                                	   className: "dt-body-center"
		                                   },
		                                   { mData: "connectionUserName", className: 'editable', sWidth: '10%' },
		                                   { mData: "connectionPassword", className: 'editable', sWidth: '12%' },
		                                   { mData: "status", className: 'editable', sWidth: '12%',
		                                	   mRender: function (data, type, full) {
		                                     	  if ( type === 'display' ) {
		                                                 return '<input type="checkbox" class="editor-active-status">';
		                                             }
		                                             return data;
		                         				},
		                                         className: "dt-body-center"
		                                   },
		                                   { mData: "connectionProperty1", className: 'editable', sWidth: '12%' },
		                                   { mData: "connectionProperty2", className: 'editable', sWidth: '12%' },
		                                   { mData: null, className: 'disableEditInline',				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<img src="css/images/execute_metro.png" class="scmSyndataImg" title="Data Sync" /></button>'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-search-plus scmAuditHistoryImg" title="Audit History"></i></button>'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-comments scmCommentsImg" title="Comments"></i></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   ],
		                                   columnDefs: [],
		                                   rowCallback: function ( row, data ) {
		                                	   $('input.editorSCMSystem-active', row).prop( 'checked', data.isPrimary == 1 );
											   $('input.editor-active-status', row).prop( 'checked', data.status == 1 );
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 
	// Activate an inline edit on click of a table cell
	$('#scmSystem_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorSCMSystem.inline( this, {
			submitOnBlur: true
		} );
	});	

	$('#scmSystem_dataTable').on( 'change', 'input.editorSCMSystem-active', function () {
		editorSCMSystem
		.edit( $(this).closest('tr'), false )
		.set( 'isPrimary', $(this).prop( 'checked' ) ? 1 : 0 )
		.submit();
	});	

	$('#scmSystem_dataTable').on( 'change', 'input.editor-active-status', function () {
		editorSCMSystem
		.edit( $(this).closest('tr'), false )
		.set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
		.submit();
	});	
	
	$('#scmSystem_dataTable tbody').on('click', 'td .scmAuditHistoryImg', function () {
		var tr = $(this).closest('tr');
		var row = scmSystemDT_oTable.DataTable().row(tr);
		listGenericAuditHistory(row.data().id,"SCMManagementSystem","scmSystemAudit");
	});

	$('#scmSystem_dataTable tbody').on('click', 'td .scmSyndataImg', function () {
		var tr = $(this).closest('tr');
		var row = scmSystemDT_oTable.DataTable().row(tr);
		displayTestmanagementSyndata(row.data().id,"SCM Systems");
	});

	$('#scmSystem_dataTable tbody').on('click', 'td .scmCommentsImg', function () {
		var tr = $(this).closest('tr');
		var row = scmSystemDT_oTable.DataTable().row(tr);
		var entityTypeIdComments = 13;
		var entityNameComments = "SCM System";
		listComments(entityTypeIdComments, entityNameComments, row.data().id, row.data().title, "SCMComments");
	});

	$("#scmSystem_dataTable_length").css('margin-top','8px');
	$("#scmSystem_dataTable_length").css('padding-left','35px');	
	
	editorSCMSystem.on( 'preSubmit', function ( e, o, action ) {
        if ( action !== 'remove' ) {
        	var validationArr = ['title','connectionUserName','connectionPassword','scmSystemVersion','connectionUri'];
        	var inputText;
        	for(var i=0;i<validationArr.length;i++){
	            inputText = this.field(validationArr[i]);
	            if ( ! inputText.isMultiValue() ) {
	                if ( inputText.val() ) {
                	}else{
	                	if(validationArr[i] == "title"){
	                		inputText.error( 'Please enter Title' );
	                	}if(validationArr[i] == "connectionUserName"){
	                		inputText.error( 'Please enter Username' );
	                	}if(validationArr[i] == "connectionPassword"){
	                		inputText.error( 'Please enter Password' );
						}if(validationArr[i] == "scmSystemVersion"){
	                		inputText.error( 'Please enter Version' );
						}if(validationArr[i] == "connectionUri"){
	                		inputText.error( 'Please enter URL' );
						}
                	}
	            }
        	}

            // If any error was reported, cancel the submission so it can be corrected
            if ( this.inError() ) {
                return false;
            }
        }
    } );
	
	editorSCMSystem.on( 'submit', function ( e, data, action ) {
		testManagementSystemDataTableInit(); 
	 });

	scmSystemDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutSCMSystemDT='';
function reInitializeSCMSystemDT(){
	clearTimeoutSCMSystemDT = setTimeout(function(){				
		scmSystemDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutSCMSystemDT);
	},200);
}
//END: ConvertDataTable - SCMSystem

function testDMSConnection(dmsId){
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : 'test.DMS.connection?dmsId='+dmsId,
		dataType : 'json',        
		success : function(data) {
			if(data.Result=="Error"){
				callAlert(data.Message);
				return false;
			} else if(data.Result=="SUCCESS"){
				callAlert(data.Message);
			}            
		}
	});
}

function testTMSConnection(tmsId){
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : 'test.TMS.connection.rest?tmsId='+tmsId,
		dataType : 'json',
		success : function(data) {        
			if(data != null && data.Result=="Error"){
				callAlert(data.Message);
				return false;
			}else { 	 
				if(data != null && data.Result=="SUCCESS"){
					callAlert(data.Message);
				} else {
					callAlert("Connection Failure");
				}
			}               
		}
	});
}

function importTestcasesFromTMS_PopUp() {
	if(nodeType == "Product"){
		productId = key;
		if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");
			return false;
		}
	}
	var urlMapping = 'administration.test.management.system.list?productId='+productId;
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : urlMapping,
		dataType : 'json',
		success : function(data) {
			if(data.Result=="OK"){
				var listOfData=data.Records
				$('#expRes_tc_types').empty();
				$.each(listOfData, function(i,item){	
					var tmsid = item.testManagementSystemId;					
					var tmsName=item.title;
					$('#expRes_tc_types').append('<label><input type="checkbox" name="'+tmsName+'"  class="icheck" id="' + tmsid + '" data-radio="iradio_flat-grey">'+tmsName+'</label>');

				});
				$("#div_PopupExportResTestcase").modal();
			}
		}
	}); 
}

function importTestcasesFromTMS_Bulk(bool){
	if(nodeType == "Product"){
		productId = key;
		if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");
			return false;
		}
	}
	if(bool == 0) {
		$("#div_PopupExportResTestcase").modal("hide");
		return false;
	}
	var checkboxValues = [];
	$("#div_PopupExportResTestcase input:checked").each(function(){ 
		checkboxValues.push($(this).attr('id'));
	});
	if(checkboxValues.length > 0){
		console.log("checkboxValues :"+checkboxValues);	
		$("#expSubmit").attr('disabled', true);
		$.ajax({
			type: "POST",
			contentType: "application/json; charset=utf-8",
			url : 'test.import.testcases.rest?productId='+productId+'&tmsIdValues='+checkboxValues,
			dataType : 'json',
			success : function(data) {
				if(data.Result=="ERROR"){
					callAlert(data.Message); 
					$("#expSubmit").attr('disabled', false);
					return false;
				}else{
					if(data.Result=="SUCCESS"){
						callAlert("Import testCases Completed.");
						$("#expSubmit").attr('disabled', false);
						$("#div_PopupExportResTestcase").modal("hide");
						testcases();
					}else{
						callAlert(data.Message);
						$("#expSubmit").attr('disabled', false);
						$("#div_PopupExportResTestcase").modal("hide");
					}
				}               
			}
		});
	} else {
		callAlert("Please select the TMS");
	}
}

function importTestcasesFromTMS(){
	if(nodeType == "Product"){
		productId = key;
		if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");
			return false;
		}
	}

	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : 'test.import.testcases.rest?productId='+productId,
		dataType : 'json',
		success : function(data) {
			if(data.Result=="ERROR"){
				callAlert(data.Message);
				return false;
			}else{
				if(data.Result=="SUCCESS"){
					callAlert("Import testCases Completed.");
					testcases();
				}
			}
		}
	});
}
//commented for ilcm astg editor integration

function displayBDDScriptEditor(data){
	var language="GHERKIN";
	if(data.Records[0].scriptType != null){
		language=data.Records[0].scriptType;
	}
	var jsonObj={"Title":"BDD Test Case Script Gherkin Story : "+data.Records[0].testCaseClassName,
			"subTitle": "Testcase ID : "+data.Records[0].testCaseId+" - Testcase Name: "+ data.Records[val].testCaseName+" - Test Engine : ", 
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


var idUnique;
var scriptsForUnique;
var scriptTypeUnique = "BDD";
var testEngineUnique;
var testStepOption;
var scriptExecutionType;

var scriptTestCaseName = "TestCase";
var scriptTestSuiteName = "TestSuite";
var scriptViewName = "View";
var scriptDownloadName = "Download";

var testrunPlanIdDevice="";
var testrunPlanNameDevice="";
var productTypeName;
var testToolName;
var executeEditTrplan = '';

function testRunPlanSubmit(bool){
	if(bool == 0) {
		$("#div_PopupTestRunPLanList").modal("hide");
		return false;
	}

	var checkboxValues = [];
	$("#div_PopupTestRunPLanList #testRunPlan_list input:checked").each(function(){ 
		checkboxValues.push($(this).attr('id'));
	});
	console.log("checkboxValues :"+checkboxValues);
	if(testCaseExecution == "TestCaseExecution") trpExecMode = 'Attended';
	testrunPlanIdForTestCaseExecution=checkboxValues[0];
	runConfig(null);

}

function downloadBDDScript(testSuiteId){
	var scriptsFor="TestSuite";
	var scriptType="GHERKIN";
	var testEngine="";

	location.href="product.testcase.automationscripts.download?id="+testSuiteId+"&scriptsFor="+scriptsFor+"&scriptType="+scriptType+"&testEngine="+testEngine;	
}

function downloadTemplate(){
	var mappingUrl = getContextCompletePath();
	var completeURL = mappingUrl+'/templates/iLCM_TESTCASE_IMPORT_TEMPLATE.xlsx';
	document.location.href = completeURL;
}

function manageUserMappingForGroup($img, productId, productName, userGroupId, userGroupName){

	var leftUrl="users.available.to.map.with.group.count?productId="+productId+"&userGroupId="+userGroupId;							
	var rightUrl = "";
	var leftDefaultUrl="users.available.to.map.with.group?productId="+productId+"&userGroupId="+userGroupId+"&jtStartIndex=0&jtPageSize=50";
	var rightDefaultUrl = "users.already.mapped.with.group?productId="+productId+"&userGroupId="+userGroupId+"&jtStartIndex=0&jtPageSize=50";									
	var leftDragUrl = "user.map.or.unmap.with.group?productId="+productId+"&userGroupId="+userGroupId;
	var rightDragtUrl = "user.map.or.unmap.with.group?productId="+productId+"&userGroupId="+userGroupId;
	var leftPaginationUrl = "users.available.to.map.with.group?productId="+productId+"&userGroupId="+userGroupId;
	var rightPaginationUrl="";									
	jsonUserMappingForGroupObj={
			"Title":"User Mapping to Group - "+userGroupName+" for Product - "+productName,
			"leftDragItemsHeaderName":"Available User",
			"rightDragItemsHeaderName":"Mapped User",
			"leftDragItemsTotalUrl":leftUrl,
			"rightDragItemsTotalUrl":rightUrl,
			"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
			"rightDragItemsDefaultLoadingUrl":rightDefaultUrl,
			"leftDragItemUrl":leftDragUrl,
			"rightDragItemUrl":rightDragtUrl,
			"leftItemPaginationUrl":leftPaginationUrl,
			"rightItemPaginationUrl":rightPaginationUrl,									
			"leftDragItemsPageSize":"50",
			"rightDragItemsPageSize":"50",
			"noItems":"No User to show",	
			"componentUsageTitle":"userForGroup",											
	};

	DragDropListItems.init(jsonUserMappingForGroupObj); 
}

function manageCompetenciesMapping($img, productId, productName){

	var leftUrl="dimension.available.for.product.count?productId="+productId+"&dimensionTypeId=1";							
	var rightUrl = "";
	var leftDefaultUrl="dimension.available.for.product?productId="+productId+"&dimensionTypeId=1&jtStartIndex=0&jtPageSize=50";
	var rightDefaultUrl = "dimension.list.for.product?productId="+productId+"&dimensionTypeId=1&jtStartIndex=0&jtPageSize=50";									
	var leftDragUrl = "dimension.for.product.mapping?productId="+productId;
	var rightDragtUrl = "dimension.for.product.mapping?productId="+productId;
	var leftPaginationUrl = "dimension.available.for.product?productId="+productId+"&dimensionTypeId=1";
	var rightPaginationUrl="";									
	jsonCompetencyObj={"Title":"Competency Mapping for Product - "+productName,
			"leftDragItemsHeaderName":"Available Competencies",
			"rightDragItemsHeaderName":"Mapped Competencies",
			"leftDragItemsTotalUrl":leftUrl,
			"rightDragItemsTotalUrl":rightUrl,
			"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
			"rightDragItemsDefaultLoadingUrl":rightDefaultUrl,
			"leftDragItemUrl":leftDragUrl,
			"rightDragItemUrl":rightDragtUrl,
			"leftItemPaginationUrl":leftPaginationUrl,
			"rightItemPaginationUrl":rightPaginationUrl,									
			"leftDragItemsPageSize":"50",
			"rightDragItemsPageSize":"50",
			"noItems":"No Competencies to show",	
			"componentUsageTitle":"competencyForProducts",											
	};

	DragDropListItems.init(jsonCompetencyObj); 
}

function manageWorkflowStatusMapping($img, productId, productName){
	var leftUrl="dimension.available.for.product.count?productId="+productId+"&dimensionTypeId=2";							
	var rightUrl = "";
	var leftDefaultUrl="dimension.available.for.product?productId="+productId+"&dimensionTypeId=2&jtStartIndex=0&jtPageSize=50";
	var rightDefaultUrl = "dimension.list.for.product?productId="+productId+"&dimensionTypeId=2&jtStartIndex=0&jtPageSize=50";									
	var leftDragUrl = "dimension.for.product.mapping?productId="+productId;
	var rightDragtUrl = "dimension.for.product.mapping?productId="+productId;
	var leftPaginationUrl = "dimension.available.for.product?productId="+productId+"&dimensionTypeId=2";
	var rightPaginationUrl="";									
	jsonWorkflowStatusObj={"Title":"Workflow Status Mapping for Product - "+productName,
			"leftDragItemsHeaderName":"Available Workflow status",
			"rightDragItemsHeaderName":"Mapped Workflow status",
			"leftDragItemsTotalUrl":leftUrl,
			"rightDragItemsTotalUrl":rightUrl,
			"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
			"rightDragItemsDefaultLoadingUrl":rightDefaultUrl,
			"leftDragItemUrl":leftDragUrl,
			"rightDragItemUrl":rightDragtUrl,
			"leftItemPaginationUrl":leftPaginationUrl,
			"rightItemPaginationUrl":rightPaginationUrl,									
			"leftDragItemsPageSize":"50",
			"rightDragItemsPageSize":"50",
			"noItems":"No Workflow status to show",	
			"componentUsageTitle":"workflowStatusForProducts",											
	};
	DragDropListItems.init(jsonWorkflowStatusObj); 
}

function showCorrespondingRisks(obj) {	
	var toShow = obj;
	$(".jtbToolRisk, .jtbToolSeverity, .jtbToolLikeHood, .jtbToolMitigation, .jtbToolRating, .jtbToolMatrix").hide();

	if(toShow == 1) {
		$(".jtbToolRisk").show();
		tType = "RiskTable";
		listRisksOfSelectedProductDataTable();
	}else if(toShow == 2) {
		$(".jtbToolMitigation").show();
		urlToGetRiskMitigationOfSpecifiedProductId = 'risk.mitigation.list?productId='+productId+'&riskMitigationStatus='+1;
		tType = "RiskMitigationTable";
		listRisksOfSelectedProductDataTable();
	}else if(toShow == 3) {
		$(".jtbToolSeverity").show();
		urlToGetRiskSevOfSpecifiedProductId = 'risk.severity.list?productId='+productId+'&riskSevStatus='+1;
		tType = "RiskSeverityTable";
		listRisksOfSelectedProductDataTable();
	}else if(toShow == 4) {
		$(".jtbToolLikeHood").show();
		urlToGetRiskLikeOfSpecifiedProductId = 'risk.likehood.list?productId='+productId+'&riskLikeStatus='+1;
		tType = "RiskLikeHoodTable";
		listRisksOfSelectedProductDataTable();
	}else if(toShow == 5) {
		$(".jtbToolRating").show();
		urlToGetRiskRatingOfSpecifiedProductId = 'risk.rating.list?productId='+productId+'&riskRatingStatus='+1;
		tType = "RiskRatingTable";
		listRisksOfSelectedProductDataTable();
	}else if(toShow == 6) {
		$(".jtbToolMatrix").show();
		showHeaderNames();
		riskMatrixOption();
	}else if(toShow == 7) {
		$(".jtbRiskTraceability").show();
		url = 'risk.hazard.traceability.matrix?productId='+productId;
		showRiskHazardTraceability(url);
	}
}

function showCorrespondingTraceabilityReports(obj) {	
	$(".jtbToolRiskReport, .jtbToolDefectReport, .jtbToolTestReport").hide();	
	$("#noDataTable").hide();

	var toShow = obj;
	if(toShow == 2) {
		$(".jtbToolRiskReport").show();		
		url = 'risk.hazard.traceability.matrix?productId='+productId;
		showRiskHazardTraceability(url);
	}
	else if(toShow == 3) {
		$(".jtbToolDefectReport").show();		
		listDefectReportDataTable();
	}else if(toShow == 1) {
		$(".jtbToolTestReport").show();
		url = 'test.fix.fail.details.report?productId='+productId;
		traceabilityTestReport(url);
	}
}

//BEGIN: ConvertDataTable - Risk
var riskDT_oTable='';
var editorRisk='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;
var tType;
var rskId,pdtId;

function listRisksOfSelectedProductDataTable(){
	optionsItemCounter=0;
	optionsResultArr=[];
	if(tType == "RiskTable" || tType == "RiskMitigationTable" || tType == "RiskSeverityTable" || tType == "RiskLikeHoodTable" || tType == "RiskRatingTable"){
		riskDataTableInit();
	}else if(tType == "RiskAssessmentTable" || tType == "AssessmentHistoryTable"){
		optionsArr = [ {id:"severityList", url:'risk.severity.list.for.options?productId='+pdtId},
		               {id:"likehoodList", url:'risk.likehood.list.for.options?productId='+pdtId},
		               {id:"ratingList", url:'risk.rating.list.for.options?productId='+pdtId},
		               {id:"lifecycleList", url:'lifecycle.list.for.options'}];
		riskOptions_Container(optionsArr);
	}
}

function riskOptions_Container(urlArr){
	$.ajax( {
		"type": "POST",
		"url":  urlArr[optionsItemCounter].url,
		"dataType": "json",
		success: function (json) {		        	 
			if(json.Result == "OK"){						 
				if(json.Options.length>0){     		   
					for(var i=0;i<json.Options.length;i++){
						json.Options[i].label=json.Options[i].DisplayText;
						json.Options[i].value=json.Options[i].Value;
					}			   
				}else{
					json.Options=[];
				}     	   
				optionsResultArr.push(json.Options);						 
			}
			optionsItemCounter++;	
			if(optionsItemCounter<optionsArr.length){
				riskOptions_Container(optionsArr);
			}else{
				riskDataTableInit();	
			}					 
		},
		error: function (data) {
			optionsItemCounter++;
		},
		complete: function(data){
			//console.log('Completed');
		},	            
	});
}

function riskDataTableInit(){
	if(tType == "RiskTable"){
		url= urlToGetRisksOfSpecifiedProductId +'&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Risk","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Risk"};
	}else if(tType == "RiskAssessmentTable"){
		url= 'risk.assessment.list?riskId='+rskId+'&filter=1&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Risk Assessment","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Risk Assessment"};
	}else if(tType == "AssessmentHistoryTable"){
		url= 'risk.assessment.list?riskId='+rskId+'&filter=2&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Assessment History","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Assessment History"};
	}else if(tType == "RiskMitigationTable"){
		url= urlToGetRiskMitigationOfSpecifiedProductId +'&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Risk Mitigation","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Risk Mitigation"};
	}else if(tType == "RiskSeverityTable"){
		url= urlToGetRiskSevOfSpecifiedProductId +'&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Risk Severity","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Risk Severity"};
	}else if(tType == "RiskLikeHoodTable"){
		url= urlToGetRiskLikeOfSpecifiedProductId +'&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Risk LikeHood","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Risk LikeHood"};
	}else if(tType == "RiskRatingTable"){
		url= urlToGetRiskRatingOfSpecifiedProductId +'&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Risk Rating","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Risk Rating"};
	}
	riskDataTableContainer.init(jsonObj);
}

var riskDataTableContainer = function() {
	var initialise = function(jsonObj){
		assignRiskDataTableValues(jsonObj);
	};
	return {
		//main function to initiate the module
		init: function(jsonObj) {        	
			initialise(jsonObj);
		}		
	};	
}();

function assignRiskDataTableValues(jsonObj){
	openLoaderIcon();			
	$.ajax({
		type: "POST",
		url: jsonObj.url,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {		
			closeLoaderIcon();
			if(data.Result=="ERROR"){
				data = [];						
			}else{
				data = data.Records;
			}					
			jsonObj.data = data;
			if(tType == "RiskTable"){
				riskDT_Container(jsonObj);
			}else if(tType == "RiskAssessmentTable" || tType == "AssessmentHistoryTable"){
				riskAssessmentDT_Container(jsonObj);
			}else if(tType == "RiskMitigationTable"){
				riskMitigationDT_Container(jsonObj);
			}else if(tType == "RiskSeverityTable"){
				riskSeverityDT_Container(jsonObj);
			}else if(tType == "RiskLikeHoodTable"){
				riskLikeHoodDT_Container(jsonObj);
			}else if(tType == "RiskRatingTable"){
				riskRatingDT_Container(jsonObj);
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

function riskDataTableHeader(){
	var childDivString ='<table id="risk_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Risk Name</th>'+
			'<th>Description</th>'+
			'<th>Label</th>'+
			'<th>Test Cases</th>'+
			'<th>Features</th>'+
			'<th>Mitigations</th>'+
			'<th>Created Date</th>'+
			'<th>Status</th>'+	
			'<th>Risk Assesment</th>'+
			'<th>Audit History</th>'+
			'<th>Comments</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function riskDT_Container(jsonObj){
	try{
		if ($("#dataTableContainerForRisk").children().length>0) {
			$("#dataTableContainerForRisk").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = riskDataTableHeader(); 			 
	$("#dataTableContainerForRisk").append(childDivString);

	editorRisk = new $.fn.dataTable.Editor( {
		"table": "#risk_dataTable",
		ajax: "risks.by.product.add",
		ajaxUrl: "risks.by.product.update",
		idSrc:  "riskId",
		i18n: {
			create: {
				title:  "Create a new Risk",
				submit: "Create",
			}
		},
		fields: [{								
			label:"productId",
			name:"productId",					
			type: 'hidden',				
			"def": productId
		},{								
			label:"Risk Name *",
			name:"riskName",		
		},{
			label:"Description *",
			name:"description",									
		},{
			label:"Label",
			name:"riskLabel",									
		},{
			label:"createdDate",
			name:"createdDate",
			"type": "hidden",
		},{
			label:"status",
			name:"status",
			"type": "hidden",
		},  
		]
	});	

	riskDT_oTable = $("#risk_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		// "sScrollX": "100%",
		//"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = [3,4,5,7,8,9,10]; // search column TextBox Invisible Column position
			$('#risk_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeRiskDT();
		},  
		buttons: [
		          { extend: "create", editor: editorRisk },	
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Risk',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Risk',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "riskName",className: 'editable', sWidth: '15%' },			
		                                   { mData: "description",className: 'editable', sWidth: '15%' },
		                                   { mData: "riskLabel",className: 'editable', sWidth: '15%' },			
		                                   { mData: null, className: 'disableEditInline',				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
		                                				   '<img src="css/images/mapping.png" class="tcMappingImg" title="Test Case Mapping" />'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   { mData: null, className: 'disableEditInline',				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
		                                				   '<img src="css/images/mapping.png" class="featureMappingImg" title="Feature Mapping" />'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   { mData: null, className: 'disableEditInline',				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
		                                				   '<img src="css/images/mapping.png" class="mitigationMappingImg" title="Mitigation Mapping" />'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   { mData: "createdDate",className: 'disableEditInline', sWidth: '15%' },			
		                                   { mData: "status",
		                                	   mRender: function (data, type, full) {
		                                		   if ( type === 'display' ) {
		                                			   return '<input type="checkbox" class="editorRisk-active">';
		                                		   }
		                                		   return data;
		                                	   },
		                                	   className: "dt-body-center"
		                                   },
		                                   { mData: null, className: 'disableEditInline',				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
		                                				   '<img src="css/images/list_metro.png" class="riskAssessmentImg" title="Risk Assessment" />'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   { mData: null, className: 'disableEditInline',					 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-search-plus riskAuditHistoryImg" title="Audit History"></i></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   { mData: null, className: 'disableEditInline',				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-comments riskCommentsImg" title="Comments"></i></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   ],       
		                                   rowCallback: function ( row, data ) {
		                                	   $('input.editorRisk-active', row).prop( 'checked', data.status == 1 );
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 	

	// Activate an inline edit on click of a table cell
	$('#risk_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorRisk.inline( this, {
			submitOnBlur: true
		} );
	});

	$('#risk_dataTable tbody').on('click', 'td .riskAssessmentImg', function () {
		var tr = $(this).closest('tr');
		var row = riskDT_oTable.DataTable().row(tr);
		rskId = row.data().riskId;
		pdtId = row.data().productId;
		$('#riskAssessmentDT_Child_Container').modal();
		$(document).off('focusin.modal');
		tType = "RiskAssessmentTable";
		listRisksOfSelectedProductDataTable();
	});

	$('#risk_dataTable tbody').on('click', 'td .featureMappingImg', function () {
		var tr = $(this).closest('tr');
		var row = riskDT_oTable.DataTable().row(tr);
		var testCasesProductRiskID = row.data().riskId;
		var riskName = row.data().riskName;
		var productId =	row.data().productId;					
		// ----- DragDrop Testing started----	
		var leftUrl="test.case.feature.unmappedto.risk.count?productId="+productId+"&productRiskId="+testCasesProductRiskID+"&filter=2";							
		var rightUrl = "";
		var leftDefaultUrl="risks.unmappedtestcaseorfeature.byProduct.list?productId="+productId+"&productRiskId="+testCasesProductRiskID+"&filter=2&jtStartIndex=0&jtPageSize=50";
		var rightDefaultUrl = "risks.test.case.feature.list?productRiskId="+testCasesProductRiskID+"&productId="+productId+"&filter=2";								
		var leftDragUrl = 'feature.risks.mapping?&productRiskId='+testCasesProductRiskID;
		var rightDragtUrl = 'feature.risks.mapping?productRiskId='+testCasesProductRiskID;
		var leftPaginationUrl = "risks.unmappedtestcaseorfeature.byProduct.list?productId="+productId+"&productRiskId="+testCasesProductRiskID+"&filter=2";
		var rightPaginationUrl="";									
		jsonRiskFeatureTabObj={"Title":"Map Features to Risks :- "+riskName,
				"leftDragItemsHeaderName":"Available Features",
				"rightDragItemsHeaderName":"Mapped Features",
				"leftDragItemsTotalUrl":leftUrl,
				"rightDragItemsTotalUrl":rightUrl,
				"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
				"rightDragItemsDefaultLoadingUrl":rightDefaultUrl,
				"leftDragItemUrl":leftDragUrl,
				"rightDragItemUrl":rightDragtUrl,
				"leftItemPaginationUrl":leftPaginationUrl,
				"rightItemPaginationUrl":rightPaginationUrl,									
				"leftDragItemsPageSize":"50",
				"rightDragItemsPageSize":"50",
				"noItems":"No Features",	
				"componentUsageTitle":"Risk-FeaturetoRisk",											
		};
		DragDropListItems.init(jsonRiskFeatureTabObj); 							
		// DragDrop Testing ended----	           	
	});

	$('#risk_dataTable tbody').on('click', 'td .tcMappingImg', function () {
		var tr = $(this).closest('tr');
		var row = riskDT_oTable.DataTable().row(tr);
		var testCasesProductRiskID = row.data().riskId;
		var riskName = row.data().riskName;
		var productId =	row.data().productId;					
		// ----- DragDrop Testing started----		
		var leftUrl="test.case.feature.unmappedto.risk.count?productId="+productId+"&productRiskId="+testCasesProductRiskID+"&filter=1";							
		var rightUrl = "";							
		var leftDefaultUrl="risks.unmappedtestcaseorfeature.byProduct.list?productId="+productId+"&productRiskId="+testCasesProductRiskID+"&filter=1&jtStartIndex=0&jtPageSize=50";							
		var rightDefaultUrl="risks.test.case.feature.list?productRiskId="+testCasesProductRiskID+"&productId="+productId+"&filter=1";
		var leftDragUrl = "test.case.risks.mapping?productRiskId="+testCasesProductRiskID;
		var rightDragtUrl = "test.case.risks.mapping?productRiskId="+testCasesProductRiskID;						
		var leftPaginationUrl = "risks.unmappedtestcaseorfeature.byProduct.list?productId="+productId+"&productRiskId="+testCasesProductRiskID+"&filter=1";
		var rightPaginationUrl="";	

		jsonRiskTCTabObj={"Title":"Map Test Cases to Risks :- "+riskName,
				"leftDragItemsHeaderName":"Available Test Cases",
				"rightDragItemsHeaderName":"Mapped Test Cases",
				"leftDragItemsTotalUrl":leftUrl,
				"rightDragItemsTotalUrl":rightUrl,
				"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
				"rightDragItemsDefaultLoadingUrl":rightDefaultUrl,
				"leftDragItemUrl":leftDragUrl,
				"rightDragItemUrl":rightDragtUrl,									
				"leftItemPaginationUrl":leftPaginationUrl,
				"rightItemPaginationUrl":rightPaginationUrl,									
				"leftDragItemsPageSize":"50",
				"rightDragItemsPageSize":"50",
				"noItems":"No Test Cases",
				"componentUsageTitle":"Risk-TCtoRisk",
		};
		DragDropListItems.init(jsonRiskTCTabObj);							
		// DragDrop Testing ended----	
	});

	$('#risk_dataTable tbody').on('click', 'td .mitigationMappingImg', function () {
		var tr = $(this).closest('tr');
		var row = riskDT_oTable.DataTable().row(tr);
		var productRiskId = row.data().riskId;
		var riskName = row.data().riskName;
		var productId =	row.data().productId;					
		// ----- DragDrop Testing started----	
		var leftUrl="test.case.feature.unmappedto.risk.count?productId="+productId+"&productRiskId="+productRiskId+"&filter=3";							
		var rightUrl = "";
		var leftDefaultUrl="risks.unmappedtestcaseorfeature.byProduct.list?productId="+productId+"&productRiskId="+productRiskId+"&filter=3&jtStartIndex=0&jtPageSize=50";
		var rightDefaultUrl = "risks.test.case.feature.list?productRiskId="+productRiskId+"&productId="+productId+"&filter=3";								
		var leftDragUrl = 'mitigation.risks.mapping?&productRiskId='+productRiskId;
		var rightDragtUrl = 'mitigation.risks.mapping?productRiskId='+productRiskId;
		var leftPaginationUrl = "risks.unmappedtestcaseorfeature.byProduct.list?productId="+productId+"&productRiskId="+productRiskId+"&filter=3";
		var rightPaginationUrl="";									
		jsonRiskMitigationTabObj={"Title":"Map Risk Mitigation to Risks :- "+riskName,
				"leftDragItemsHeaderName":"Available Mitigations",
				"rightDragItemsHeaderName":"Mapped Mitigations",
				"leftDragItemsTotalUrl":leftUrl,
				"rightDragItemsTotalUrl":rightUrl,
				"leftDragItemsDefaultLoadingUrl":leftDefaultUrl,
				"rightDragItemsDefaultLoadingUrl":rightDefaultUrl,
				"leftDragItemUrl":leftDragUrl,
				"rightDragItemUrl":rightDragtUrl,
				"leftItemPaginationUrl":leftPaginationUrl,
				"rightItemPaginationUrl":rightPaginationUrl,									
				"leftDragItemsPageSize":"50",
				"rightDragItemsPageSize":"50",
				"noItems":"No Features",	
				"componentUsageTitle":"Risk-MitigationtoRisk",											
		};
		DragDropListItems.init(jsonRiskMitigationTabObj); 							
		// DragDrop Testing ended----
	});	

	$('#risk_dataTable tbody').on('click', 'td .riskAuditHistoryImg', function () {
		var tr = $(this).closest('tr');
		var row = riskDT_oTable.DataTable().row(tr);
		listGenericAuditHistory(row.data().riskId,"Risk","riskAudit");
	});

	$('#risk_dataTable tbody').on('click', 'td .riskCommentsImg', function () {
		var tr = $(this).closest('tr');
		var row = riskDT_oTable.DataTable().row(tr);
		var entityTypeIdComments = 43;
		var entityNameComments = "Risk";
		listComments(entityTypeIdComments, entityNameComments, row.data().riskId, row.data().riskName, "riskComments");
	});

	$("#risk_dataTable_length").css('margin-top','8px');
	$("#risk_dataTable_length").css('padding-left','35px');

	$("#risk_dataTable_filter").css('margin-right','6px');

	riskDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutRiskDT='';
function reInitializeRiskDT(){
	clearTimeoutRiskDT = setTimeout(function(){				
		riskDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutRiskDT);
	},200);
}
//END: ConvertDataTable - Risks

//BEGIN: ConvertDataTable - RiskAssessment
var riskAssessmentDT_oTable='';
var editorRiskAssessment='';
function riskAssessmentDataTableHeader(){
	var childDivString ='<table id="riskAssessment_dataTable'+tType+'" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Risk Severity</th>'+
			'<th>Risk LikeHood</th>'+
			'<th>Rating</th>'+
			'<th>When</th>'+
			'<th>Create Date</th>'+
			'<th>Life Cycle Phase</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function riskAssessmentDT_Container(jsonObj){
	(tType == "RiskAssessmentTable") ? divId = '#dataTableContainerForRiskAssessment': divId = '#dataTableContainerForAssessmentHistory';
	try{
		if(tType == "RiskAssessmentTable"){
			if ($("#dataTableContainerForRiskAssessment").children().length>0) {
				$("#dataTableContainerForRiskAssessment").children().remove();
			}
		}else{
			if ($('#dataTableContainerForAssessmentHistory').children().length>0) {
				$('#dataTableContainerForAssessmentHistory').children().remove();
			}
		}
	} 
	catch(e) {}

	var childDivString = riskAssessmentDataTableHeader(); 			 
	$(divId).append(childDivString);

	editorRiskAssessment = new $.fn.dataTable.Editor( {
		"table": "#riskAssessment_dataTable"+tType,
		ajax: 'risk.assessment.add?riskId='+rskId,
		ajaxUrl: 'risk.assessment.update',
		idSrc:  "riskAssessmentId",
		i18n: {
			create: {
				title:  "Create a new Risk Assessment",
				submit: "Create",
			}
		},
		fields: [
		         {
		        	 label: "riskAssessmentId",
		        	 name: "riskAssessmentId",
		        	 "type": "hidden",
		         },{
		        	 label: "Risk Severity",
		        	 name: "riskSeverityId",
		        	 options: optionsResultArr[0],
		        	 "type":"select",             
		         },{
		        	 label: "Risk LikeHood",
		        	 name: "riskLikeHoodId",
		        	 options: optionsResultArr[1],
		        	 "type":"select",
		         },{
		        	 label: "Rating",
		        	 name: "riskRatingId",
		        	 options: optionsResultArr[2],
		        	 "type":"select",
		         },{
		        	 label: "When",
		        	 name: "mitigationType",
		        	 options: [{ label: 'Pre-Mitigation', value: "1" ,"DisplayText":"Pre-Mitigation"},
		        	           { label: 'Post-Mitigation', value: "2" ,"DisplayText":"Post-Mitigation"}],
		        	           "type":"select",
		         },{
		        	 label: "assessmentDate",
		        	 name: "assessmentDate",
		        	 "type":"hidden",
		         },{
		        	 label: "Life Cycle Phase",
		        	 name: "lifeCyclePhaseId",
		        	 options: optionsResultArr[3],
		        	 "type":"select",
		         }        
		         ]
	});

	riskAssessmentDT_oTable = $("#riskAssessment_dataTable"+tType).dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		// "sScrollX": "90%",
		//"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
			$('#riskAssessment_dataTable'+tType+'_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeRiskAssessmentDT();
		},  
		buttons: [
		          { extend: "create", editor: editorRiskAssessment },	
		          {
		        	  text: 'Assessment History',
		        	  className: 'assessmentHistoryBtn',
		        	  action: function ( e, dt, node, config ) {
		        		  tType = "AssessmentHistoryTable";
		        		  listRisksOfSelectedProductDataTable();
		        		  $("#div_AssessmentHistory").modal();
		        	  }
		          },
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Risk Assessment',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Risk Assessment',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "riskSeverityId", className: 'editable', sWidth: '18%',editField: "riskSeverityId",
		                                	   mRender: function (data, type, full) {
		                                		   data = optionsValueHandler(editorRiskAssessment, 'riskSeverityId', full.riskSeverityId); 		           	 
		                                		   return data;
		                                	   },
		                                   },	
		                                   { mData: "riskLikeHoodId", className: 'editable', sWidth: '18%',editField: "riskLikeHoodId",
		                                	   mRender: function (data, type, full) {
		                                		   data = optionsValueHandler(editorRiskAssessment, 'riskLikeHoodId', full.riskLikeHoodId); 		           	 
		                                		   return data;
		                                	   },
		                                   },	
		                                   { mData: "riskRatingId", className: 'editable', sWidth: '18%',editField: "riskRatingId",
		                                	   mRender: function (data, type, full) {
		                                		   data = optionsValueHandler(editorRiskAssessment, 'riskRatingId', full.riskRatingId); 		           	 
		                                		   return data;
		                                	   },
		                                   },	
		                                   { mData: "mitigationType", className: 'disableEditInline', sWidth: '15%',editField: "mitigationType",
		                                	   mRender: function (data, type, full) {
		                                		   data = optionsValueHandler(editorRiskAssessment, 'mitigationType', full.mitigationType); 		           	 
		                                		   return data;
		                                	   },
		                                   },	
		                                   { mData: "assessmentDate", className: 'disableEditInline', sWidth: '15%' },		
		                                   { mData: "lifeCyclePhaseId", className: 'editable', sWidth: '18%',editField: "lifeCyclePhaseId",
		                                	   mRender: function (data, type, full) {
		                                		   data = optionsValueHandler(editorRiskAssessment, 'lifeCyclePhaseId', full.lifeCyclePhaseId); 		           	 
		                                		   return data;
		                                	   },
		                                   },	
		                                   ],       
		                                   rowCallback: function ( row, data ) {
		                                	   //$('input.editorRiskAssessment-active', row).prop( 'checked', data.status == 1 );
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 

	// Activate an inline edit on click of a table cell
	$('#riskAssessment_dataTable'+tType).on( 'click', 'tbody td.editable', function (e) {
		editorRiskAssessment.inline( this, {
			submitOnBlur: true
		} );
	});

	$("#riskAssessment_dataTable"+tType+"_length").css('margin-top','8px');
	$("#riskAssessment_dataTable"+tType+"_length").css('padding-left','35px');		
	$("#riskAssessment_dataTable"+tType+"_filter").css('margin-right','6px');

	var buttons = riskAssessmentDT_oTable.DataTable().buttons( ['.buttons-create'] );	 
	if (tType == "AssessmentHistoryTable") {
		buttons.remove();
		$('.assessmentHistoryBtn').hide();
	}else{
		$('.assessmentHistoryBtn').show();
	}

	riskAssessmentDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutRiskAssessmentDT='';
function reInitializeRiskAssessmentDT(){
	clearTimeoutRiskAssessmentDT = setTimeout(function(){				
		riskAssessmentDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutRiskAssessmentDT);
	},200);
}
//END: ConvertDataTable - RiskAssessment

//BEGIN: ConvertDataTable - RiskMitigation
function riskMitigationDataTableHeader(){
	var childDivString ='<table id="riskMitigation_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>RM ID</th>'+
			'<th>Mitigation Measure</th>'+
			'<th>Project Record</th>'+
			'<th>Available</th>'+
			'<th>Mitigation Date</th>'+
			'<th>Test Report</th>'+
			'<th>Created Date</th>'+
			'<th>Status</th>'+	
			'<th>Audit History</th>'+
			'<th>Comments</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
	'<tr>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+ 
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+ 
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function riskMitigationDT_Container(jsonObj){
	try{
		if ($("#dataTableContainerForRiskMitigation").children().length>0) {
			$("#dataTableContainerForRiskMitigation").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = riskMitigationDataTableHeader(); 			 
	$("#dataTableContainerForRiskMitigation").append(childDivString);

	editorRiskMitigation = new $.fn.dataTable.Editor( {
		"table": "#riskMitigation_dataTable",
		ajax: "risk.mitigation.add",
		ajaxUrl: "risk.mitigation.update",
		idSrc:  "riskMitigationId",
		i18n: {
			create: {
				title:  "Create a new RiskMitigation",
				submit: "Create",
			}
		},
		fields: [{								
			label:"productId",
			name:"productId",					
			type: 'hidden',				
			"def": productId
		},{								
			label:"RM ID *",
			name:"rmCode",		
		},{
			label:"Mitigation Measure *",
			name:"mitigationMeasure",									
		},{
			label:"Project Record *",
			name:"projectRecord",									
		},{
			label:"Test Report *",
			name:"testReport",
		},{
			label:"Mitigation Date",
			name:"mitigationDate",
			"type":"hidden",
		},{
			label:"Created Date",
			name:"createdDate",
			"type":"hidden",
		},{
			label:"Available",
			name:"isAvailable",
			"type":"hidden",
		},{
			label:"Status",
			name:"status",
			"type":"hidden",
		},  
		]
	});	

	riskMitigationDT_oTable = $("#riskMitigation_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		// "sScrollX": "100%",
		//"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = [3,7,8,9]; // search column TextBox Invisible Column position
			$('#riskMitigation_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeRiskMitigationDT();
		},  
		buttons: [
		          { extend: "create", editor: editorRiskMitigation },	
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Risk Mitigation',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Risk Mitigation',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "rmCode",className: 'editable', sWidth: '15%' },			
		                                   { mData: "mitigationMeasure",className: 'editable', sWidth: '15%' },
		                                   { mData: "projectRecord",className: 'editable', sWidth: '15%' },			
		                                   { mData: "isAvailable",
		                                	   mRender: function (data, type, full) {
		                                		   if ( type === 'display' ) {
		                                			   return '<input type="checkbox" class="editorRiskMitigation-active">';
		                                		   }
		                                		   return data;
		                                	   },
		                                	   className: "dt-body-center"
		                                   },
		                                   { mData: "mitigationDate",className: 'disableEditInline', sWidth: '10%' },			
		                                   { mData: "testReport",className: 'editable', sWidth: '15%' },			
		                                   { mData: "createdDate",className: 'disableEditInline', sWidth: '10%' },			
		                                   { mData: "status",
		                                	   mRender: function (data, type, full) {
		                                		   if ( type === 'display' ) {
		                                			   return '<input type="checkbox" class="editorRiskMitigation-active">';
		                                		   }
		                                		   return data;
		                                	   },
		                                	   className: "dt-body-center"
		                                   },
		                                   { mData: null, className: 'disableEditInline',					 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-search-plus riskMitigationAuditHistoryImg" title="Audit History"></i></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   { mData: null, className: 'disableEditInline',				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-comments riskMitigationCommentsImg" title="Comments"></i></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   ],       
		                                   rowCallback: function ( row, data ) {
		                                	   $('input.editorRiskMitigation-active', row).prop( 'checked', data.status == 1 );
		                                	   $('input.editorRiskMitigation-active', row).prop( 'checked', data.isAvailable == 1 );
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 	

	// Activate an inline edit on click of a table cell
	$('#riskMitigation_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorRiskMitigation.inline( this, {
			submitOnBlur: true
		} );
	});

	$('#riskMitigation_dataTable tbody').on('click', 'td .riskMitigationAuditHistoryImg', function () {
		var tr = $(this).closest('tr');
		var row = riskMitigationDT_oTable.DataTable().row(tr);
		listGenericAuditHistory(row.data().riskMitigationId,"RiskMitigation","riskMitigationAudit");
	});

	$('#riskMitigation_dataTable tbody').on('click', 'td .riskMitigationCommentsImg', function () {
		var tr = $(this).closest('tr');
		var row = riskMitigationDT_oTable.DataTable().row(tr);
		var entityTypeIdComments = 59;
		var entityNameComments = "RiskMitigation";
		listComments(entityTypeIdComments, entityNameComments, row.data().riskMitigationId, row.data().rmCode, "riskMitigationComments");
	});

	$("#riskMitigation_dataTable_length").css('margin-top','8px');
	$("#riskMitigation_dataTable_length").css('padding-left','35px');

	$("#riskMitigation_dataTable_filter").css('margin-right','6px');

	$('#riskMitigation_dataTable').on( 'change', 'input.editorRiskMitigation-active', function () {
		editorRiskMitigation
		.edit( $(this).closest('tr'), false )
		.set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
		.submit();
	});

	$('#riskMitigation_dataTable').on( 'change', 'input.editorRiskMitigation-active', function () {
		editorRiskMitigation
		.edit( $(this).closest('tr'), false )
		.set( 'isAvailable', $(this).prop( 'checked' ) ? 1 : 0 )
		.submit();
	});

	riskMitigationDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutRiskMitigationDT='';
function reInitializeRiskMitigationDT(){
	clearTimeoutRiskMitigationDT = setTimeout(function(){				
		riskMitigationDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutRiskMitigationDT);
	},200);
}
//END: ConvertDataTable - RiskMitigation

//BEGIN: ConvertDataTable - RiskSeverity
function riskSeverityDataTableHeader(){
	var childDivString ='<table id="riskSeverity_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Risk Severity Name</th>'+
			'<th>Rating</th>'+
			'<th>Expected Events</th>'+
			'<th>Created Date</th>'+
			'<th>Status</th>'+	
			'<th>Audit History</th>'+
			'<th>Comments</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function riskSeverityDT_Container(jsonObj){
	try{
		if ($("#dataTableContainerForRiskSeverity").children().length>0) {
			$("#dataTableContainerForRiskSeverity").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = riskSeverityDataTableHeader(); 			 
	$("#dataTableContainerForRiskSeverity").append(childDivString);

	editorRiskSeverity = new $.fn.dataTable.Editor( {
		"table": "#riskSeverity_dataTable",
		ajax: "risk.severity.add",
		ajaxUrl: "risk.severity.update",
		idSrc:  "riskSeverityId",
		i18n: {
			create: {
				title:  "Create a new Risk Severity",
				submit: "Create",
			}
		},
		fields: [{								
			label:"productId",
			name:"productId",					
			type: 'hidden',				
			"def": productId
		},{								
			label:"Risk Severity Name *",
			name:"severityName",		
		},{
			label:"Rating *",
			name:"severityRating",									
		},{
			label:"Expected Events *",
			name:"expectedEvents",									
		},{
			label:"Created Date",
			name:"createdDate",
			"type":"hidden",
		},{
			label:"Status",
			name:"status",
			"type":"hidden",
		},  
		]
	});	

	riskSeverityDT_oTable = $("#riskSeverity_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		// "sScrollX": "100%",
		//"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = [4,5,6]; // search column TextBox Invisible Column position
			$('#riskSeverity_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeRiskSeverityDT();
		},  
		buttons: [
		          { extend: "create", editor: editorRiskSeverity },	
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Risk Severity',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Risk Severity',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "severityName",className: 'editable', sWidth: '20%' },			
		                                   { mData: "severityRating",className: 'editable', sWidth: '20%' },
		                                   { mData: "expectedEvents",className: 'editable', sWidth: '20%' },			
		                                   { mData: "createdDate",className: 'disableEditInline', sWidth: '10%' },			
		                                   { mData: "status",
		                                	   mRender: function (data, type, full) {
		                                		   if ( type === 'display' ) {
		                                			   return '<input type="checkbox" class="editorRiskSeverity-active">';
		                                		   }
		                                		   return data;
		                                	   },
		                                	   className: "dt-body-center"
		                                   },
		                                   { mData: null, className: 'disableEditInline',					 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-search-plus riskSeverityAuditHistoryImg" title="Audit History"></i></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   { mData: null, className: 'disableEditInline',				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-comments riskSeverityCommentsImg" title="Comments"></i></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   ],       
		                                   rowCallback: function ( row, data ) {
		                                	   $('input.editorRiskSeverity-active', row).prop( 'checked', data.status == 1 );
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 	

	// Activate an inline edit on click of a table cell
	$('#riskSeverity_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorRiskSeverity.inline( this, {
			submitOnBlur: true
		} );
	});

	$('#riskSeverity_dataTable tbody').on('click', 'td .riskSeverityAuditHistoryImg', function () {
		var tr = $(this).closest('tr');
		var row = riskSeverityDT_oTable.DataTable().row(tr);
		listGenericAuditHistory(row.data().riskSeverityId,"RiskSeverity","riskSeverityAudit");
	});

	$('#riskSeverity_dataTable tbody').on('click', 'td .riskSeverityCommentsImg', function () {
		var tr = $(this).closest('tr');
		var row = riskSeverityDT_oTable.DataTable().row(tr);
		var entityTypeIdComments = 57;
		var entityNameComments = "RiskSeverityMaster";
		listComments(entityTypeIdComments, entityNameComments, row.data().riskSeverityId, row.data().severityName, "riskSeverityComments");
	});

	$("#riskSeverity_dataTable_length").css('margin-top','8px');
	$("#riskSeverity_dataTable_length").css('padding-left','35px');

	$("#riskSeverity_dataTable_filter").css('margin-right','6px');

	$('#riskSeverity_dataTable').on( 'change', 'input.editorRiskSeverity-active', function () {
		editorRiskSeverity
		.edit( $(this).closest('tr'), false )
		.set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
		.submit();
	});

	riskSeverityDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutRiskSeverityDT='';
function reInitializeRiskSeverityDT(){
	clearTimeoutRiskSeverityDT = setTimeout(function(){				
		riskSeverityDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutRiskSeverityDT);
	},200);
}
//END: ConvertDataTable - RiskSeverity

//BEGIN: ConvertDataTable - RiskLikeHood
function riskLikeHoodDataTableHeader(){
	var childDivString ='<table id="riskLikeHood_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Risk LikeHood Name</th>'+
			'<th>Rating</th>'+
			'<th>Expected Frequency</th>'+
			'<th>Created Date</th>'+
			'<th>Status</th>'+	
			'<th>Audit History</th>'+
			'<th>Comments</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function riskLikeHoodDT_Container(jsonObj){
	try{
		if ($("#dataTableContainerForRiskLikeHood").children().length>0) {
			$("#dataTableContainerForRiskLikeHood").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = riskLikeHoodDataTableHeader(); 			 
	$("#dataTableContainerForRiskLikeHood").append(childDivString);

	editorRiskLikeHood = new $.fn.dataTable.Editor( {
		"table": "#riskLikeHood_dataTable",
		ajax: "risk.likehood.add",
		ajaxUrl: "risk.likehood.update",
		idSrc:  "riskLikeHoodId",
		i18n: {
			create: {
				title:  "Create a new Risk LikeHood",
				submit: "Create",
			}
		},
		fields: [{								
			label:"productId",
			name:"productId",					
			type: 'hidden',				
			"def": productId
		},{								
			label:"Risk LikeHood Name *",
			name:"likeHoodName",		
		},{
			label:"Rating *",
			name:"likeHoodRating",									
		},{
			label:"Expected Frequency *",
			name:"expectedFrequency",									
		},{
			label:"Created Date",
			name:"createdDate",
			"type":"hidden",
		},{
			label:"Status",
			name:"status",
			"type":"hidden",
		},  
		]
	});	

	riskLikeHoodDT_oTable = $("#riskLikeHood_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		// "sScrollX": "100%",
		//"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = [4,5,6]; // search column TextBox Invisible Column position
			$('#riskLikeHood_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeRiskLikeHoodDT();
		},  
		buttons: [
		          { extend: "create", editor: editorRiskLikeHood },	
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Risk LikeHood',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Risk LikeHood',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "likeHoodName",className: 'editable', sWidth: '20%' },			
		                                   { mData: "likeHoodRating",className: 'editable', sWidth: '20%' },
		                                   { mData: "expectedFrequency",className: 'editable', sWidth: '20%' },			
		                                   { mData: "createdDate",className: 'disableEditInline', sWidth: '10%' },			
		                                   { mData: "status",
		                                	   mRender: function (data, type, full) {
		                                		   if ( type === 'display' ) {
		                                			   return '<input type="checkbox" class="editorRiskLikeHood-active">';
		                                		   }
		                                		   return data;
		                                	   },
		                                	   className: "dt-body-center"
		                                   },
		                                   { mData: null, className: 'disableEditInline',					 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-search-plus riskLikeHoodAuditHistoryImg" title="Audit History"></i></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   { mData: null, className: 'disableEditInline',				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-comments riskLikeHoodCommentsImg" title="Comments"></i></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   ],       
		                                   rowCallback: function ( row, data ) {
		                                	   $('input.editorRiskLikeHood-active', row).prop( 'checked', data.status == 1 );
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 	

	// Activate an inline edit on click of a table cell
	$('#riskLikeHood_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorRiskLikeHood.inline( this, {
			submitOnBlur: true
		} );
	});

	$('#riskLikeHood_dataTable tbody').on('click', 'td .riskLikeHoodAuditHistoryImg', function () {
		var tr = $(this).closest('tr');
		var row = riskLikeHoodDT_oTable.DataTable().row(tr);
		listGenericAuditHistory(row.data().riskLikeHoodId,"RiskLikeHood","riskLikeHoodAudit");
	});

	$('#riskLikeHood_dataTable tbody').on('click', 'td .riskLikeHoodCommentsImg', function () {
		var tr = $(this).closest('tr');
		var row = riskLikeHoodDT_oTable.DataTable().row(tr);
		var entityTypeIdComments = 58;
		var entityNameComments = "RiskLikeHood";
		listComments(entityTypeIdComments, entityNameComments, row.data().riskLikeHoodId, row.data().likeHoodName, "riskLikeHoodComments");
	});

	$("#riskLikeHood_dataTable_length").css('margin-top','8px');
	$("#riskLikeHood_dataTable_length").css('padding-left','35px');

	$("#riskLikeHood_dataTable_filter").css('margin-right','6px');

	$('#riskLikeHood_dataTable').on( 'change', 'input.editorRiskLikeHood-active', function () {
		editorRiskLikeHood
		.edit( $(this).closest('tr'), false )
		.set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
		.submit();
	});

	riskLikeHoodDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutRiskLikeHoodDT='';
function reInitializeRiskLikeHoodDT(){
	clearTimeoutRiskLikeHoodDT = setTimeout(function(){				
		riskLikeHoodDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutRiskLikeHoodDT);
	},200);
}
//END: ConvertDataTable - RiskLikeHood

//BEGIN: ConvertDataTable - RiskRating
function riskRatingDataTableHeader(){
	var childDivString ='<table id="riskRating_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Rating Name</th>'+
			'<th>Description</th>'+
			'<th>Colour</th>'+
			'<th>Created Date</th>'+
			'<th>Status</th>'+	
			'<th>Audit History</th>'+
			'<th>Comments</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function riskRatingDT_Container(jsonObj){
	try{
		if ($("#dataTableContainerForRiskRating").children().length>0) {
			$("#dataTableContainerForRiskRating").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = riskRatingDataTableHeader(); 			 
	$("#dataTableContainerForRiskRating").append(childDivString);

	editorRiskRating = new $.fn.dataTable.Editor( {
		"table": "#riskRating_dataTable",
		ajax: "risk.rating.add",
		ajaxUrl: "risk.rating.update",
		idSrc:  "riskRatingId",
		i18n: {
			create: {
				title:  "Create a new Risk Rating",
				submit: "Create",
			}
		},
		fields: [{								
			label:"productId",
			name:"productId",					
			type: 'hidden',				
			"def": productId
		},{								
			label:"Rating Name *",
			name:"ratingName",		
		},{
			label:"Description *",
			name:"ratingDescription",									
		},{
			label:"Colour *",
			name:"colour",									
			options: [{ label: 'Red', value: "1", "DisplayText":"Red" },
			          { label: 'Green', value: "2", "DisplayText":"Green" },
			          { label: 'Amber', value: "3", "DisplayText":"Amber" }],
			          "type":"select",
		},{
			label:"Created Date",
			name:"createdDate",
			"type":"hidden",
		},{
			label:"Status",
			name:"status",
			"type":"hidden",
		},  
		]
	});	

	riskRatingDT_oTable = $("#riskRating_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		// "sScrollX": "100%",
		//"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = [4,5,6]; // search column TextBox Invisible Column position
			$('#riskRating_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeRiskRatingDT();
		},  
		buttons: [
		          { extend: "create", editor: editorRiskRating },	
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Risk Rating',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Risk Rating',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "ratingName",className: 'editable', sWidth: '20%' },			
		                                   { mData: "ratingDescription",className: 'editable', sWidth: '20%' },
		                                   { mData: "colour", className: 'editable', sWidth: '15%',editField: "colour",
		                                	   mRender: function (data, type, full) {
		                                		   data = optionsValueHandler(editorRiskRating, 'colour', full.colour); 		           	 
		                                		   return data;
		                                	   },
		                                   },			
		                                   { mData: "createdDate",className: 'disableEditInline', sWidth: '10%' },			
		                                   { mData: "status",
		                                	   mRender: function (data, type, full) {
		                                		   if ( type === 'display' ) {
		                                			   return '<input type="checkbox" class="editorRiskRating-active">';
		                                		   }
		                                		   return data;
		                                	   },
		                                	   className: "dt-body-center"
		                                   },
		                                   { mData: null, className: 'disableEditInline',					 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-search-plus riskRatingAuditHistoryImg" title="Audit History"></i></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   { mData: null, className: 'disableEditInline',				 
		                                	   bSortable: false,
		                                	   mRender: function(data, type, full) {				            	
		                                		   var img = ('<div style="display: flex;">'+
		                                				   '<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
		                                				   '<i class="fa fa-comments riskRatingCommentsImg" title="Comments"></i></button>'+
		                                		   '</div>');	      		
		                                		   return img;
		                                	   }
		                                   },	
		                                   ],       
		                                   rowCallback: function ( row, data ) {
		                                	   $('input.editorRiskRating-active', row).prop( 'checked', data.status == 1 );
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 	

	// Activate an inline edit on click of a table cell
	$('#riskRating_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorRiskRating.inline( this, {
			submitOnBlur: true
		} );
	});

	$('#riskRating_dataTable tbody').on('click', 'td .riskRatingAuditHistoryImg', function () {
		var tr = $(this).closest('tr');
		var row = riskRatingDT_oTable.DataTable().row(tr);
		listGenericAuditHistory(row.data().riskRatingId,"RiskRating","riskRatingAudit");
	});

	$('#riskRating_dataTable tbody').on('click', 'td .riskRatingCommentsImg', function () {
		var tr = $(this).closest('tr');
		var row = riskRatingDT_oTable.DataTable().row(tr);
		var entityTypeIdComments = 60;
		var entityNameComments = "RiskRating";
		listComments(entityTypeIdComments, entityNameComments, row.data().riskRatingId, row.data().ratingName, "riskRatingComments");	
	});

	$("#riskRating_dataTable_length").css('margin-top','8px');
	$("#riskRating_dataTable_length").css('padding-left','35px');

	$("#riskRating_dataTable_filter").css('margin-right','6px');

	$('#riskRating_dataTable').on( 'change', 'input.editorRiskRating-active', function () {
		editorRiskRating
		.edit( $(this).closest('tr'), false )
		.set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
		.submit();
	});

	riskRatingDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutRiskRatingDT='';
function reInitializeRiskRatingDT(){
	clearTimeoutRiskRatingDT = setTimeout(function(){				
		riskRatingDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutRiskRatingDT);
	},200);
}
//END: ConvertDataTable - RiskRating

function assessmentHistory_PopUp(riskID){
	$("#div_AssessmentHistory").modal();
	try{
		if ($('#jTableAssessmentHistory').length>0) {
			$('#jTableAssessmentHistory').jtable('destroy'); 
		}
	}catch(e){}
	//init jTable
	$('#jTableAssessmentHistory').jtable({
		title: 'Assessment History',
		selecting: true, //Enable selecting run
		paging: true, //Enable paging
		pageSize: 10, //Set page size (default: 10)
		editinline:{enable:true},	         
		actions: {
			listAction: 'risk.assessment.list?riskId='+riskID+'&filter=2',
			editinlineAction: 'risk.assessment.update',
		},
		fields: {
			riskAssessmentId: { 
				key: true,
				type: 'hidden',
				create: false, 
				edit: false, 
				list: false
			}, 
			riskSeverityId: { 
				title: 'Risk Severity' ,
				inputTitle: 'Risk Severity <font color="#efd125" size="4px">*</font>',
				width: "5%", 
				list:true,
				options: 'risk.severity.list.for.options?productId='+productId,
			},
			riskLikeHoodId: { 
				title: 'Risk LikeHood' ,
				inputTitle: 'Risk LikeHood <font color="#efd125" size="4px">*</font>',
				width: "5%", 
				list:true,
				options: 'risk.likehood.list.for.options?productId='+productId,
			},	
			riskRatingId: { 
				title: 'Rating',
				inputTitle: 'Rating <font color="#efd125" size="4px">*</font>',
				width: "5%",
				list:true,
				options: 'risk.rating.list.for.options?productId='+productId,
			},
			mitigationType: { 
				title: 'When' ,
				inputTitle: 'When <font color="#efd125" size="4px">*</font>',
				width: "5%",  
				list:true,
				edit: false, 
				options: {'1' : 'Pre-Mitigation','2' : 'Post-Mitigation'},
			},	
			assessmentDate: { 
				title: 'Date' ,
				inputTitle: 'Date <font color="#efd125" size="4px">*</font>',
				width: "5%", 
				create:false,
				edit:false
			},
			lifeCyclePhaseId: { 
				title: 'Life Cycle Phase' ,
				inputTitle: 'Life Cycle Phase <font color="#efd125" size="4px">*</font>',
				width: "5%", 
				list:true,
				options: 'lifecycle.list.for.options',
			},
		},	          	
		//Moved Formcreate validation to Form Submitting
		//Validate form when it is being submitted
		formSubmitting: function (event, data) {
			data.form.validationEngine();
			return data.form.validationEngine('validate');
		}, 
		//Dispose validation logic when form is closed
		formClosed: function (event, data) {
			data.form.validationEngine('hide');
			data.form.validationEngine('detach');
		}
	});		 
	$('#jTableAssessmentHistory').jtable('load');	
}

function showRiskTab(){
	var enableTabs=document.getElementById("toenableTabs").value; 
	if (enableTabs.toLowerCase() == 'yes'){
		$("a[href^=#Risks]").parent("li").show();
		$("a[href^=#Decoupling]").parent("li").show();
		$("a[href^=#ChangeRequests]").parent("li").show();
	}else{
		$("a[href^=#Risks]").parent("li").hide();
		$("a[href^=#Decoupling]").parent("li").hide();
		$("a[href^=#ChangeRequests]").parent("li").hide();
	}
}

var fields = {};
function showRiskMatrix(fields,data){
	try{
		if ($('#jTableContainerRiskMatrix').length>0) {
			$('#jTableContainerRiskMatrix').jtable('destroy'); 
		}
	}catch(e){}

	$('#jTableContainerRiskMatrix').jtable({
		title: 'Risk Rating Matrix',
		selecting: true, //Enable selecting 
		//toolbarsearch:true,
		paging: true, //Enable paging
		pageSize: 10, //Set page size (default: 10)
		editinline:{enable:true},	         
		actions: {
			editinlineAction: 'risk.matrix.update?productId='+productId,
			listAction: function (postData, jtParams) {
				return {
					"Result": "OK",
					"Records": data
					,"TotalRecordCount": riskSeveritiesCount
				}
			},
		},

		fields:fields,

		//Moved Formcreate validation to Form Submitting
		//Validate form when it is being submitted
		formSubmitting: function (event, data) {
			//data.form.find('input[name="ratingName"]').addClass('validate[required]');
			data.form.validationEngine();
			return data.form.validationEngine('validate');
		}, 
		//Dispose validation logic when form is closed
		formClosed: function (event, data) {
			data.form.validationEngine('hide');
			data.form.validationEngine('detach');
		}
	});		 
	$('#jTableContainerRiskMatrix').jtable('load');
}

function showHeaderNames(){
	var columnData;
	fields['riskSeverityId'] =  {title: 'Severity Id',list:true,edit: false,type: 'hidden'};
	fields['severityName'] =  {title: 'Severity',list:true,edit: false,width:"5%"};
	fields['severityRating'] =  {title: 'Severity Rating',list:true,edit: false,width:"1%"};
	$.ajax({
		url : 'risk.matrix.for.list?productId='+productId+'&riskMatStatus='+1,
		type: 'post',
		error: function() {
			callAlert("An error occurred");
		},
		success : function(data) {
			var data1=eval(data);
			var headerName=data1[0].COLUMNS;
			columnData=data1[0].DATA;
			riskSeveritiesCount= data1[0].RiskSeveritiesCount;
			$.each(headerName, function(i,item){ 
				fields[item.Id] =  {
						title: item.title,list:true,edit: true,width:"1%",
						options:function () {
							return  optionsArr; //Cache results and return options
						} 
				};
			});
			showRiskMatrix(fields,columnData);
		}
	}); 
}

function riskMatrixOption(){
	$.ajax({ //Not found in cache, get from server
		url: 'risk.rating.list.for.options?productId='+productId,
		type: 'POST',
		dataType: 'json',
		async: false,
		success: function (data) {
			if (data.Result != 'OK') {
				alert(data.Message);
				return;
			}
			optionsArr = data.Options;
		}
	});
	return optionsArr;
}

//TCExecution Report starts
function listTCExecutionSummaryHistoryView(testCaseId, testCaseName, dataLevel){	
	prodId = document.getElementById("treeHdnCurrentProductId").value;
	if(nodeType=='WorkPackage'){
		workPackageId = key;		
	}			
	var jsonObj={"Title":"TestCase Execution ["+ testCaseId + "] "+testCaseName,
			"testCaseID":testCaseId,
			"testCaseName":testCaseName,
			"testCaseExectionSummayTitle":"Execution Summary",
			"testCaseExectionSummayUrl": 'result.testcase.execution.summary?tcId='+testCaseId+'&workPackageId=-1&productId='+prodId+'&dataLevel='+dataLevel,
			"testCaseExectionHistoryTitle":"Execution History",
			"testCaseExectionHistoryUrl":"testcase.execution.history?testCaseId="+testCaseId+'&workPackageId=-1&dataLevel='+dataLevel,
			"componentUsageTitle":dataLevel,
	};
	TestCaseExecutionDetails.init(jsonObj);	
}

function showRiskHazardTraceability(url){
	scrollValue = clearDataTableValues('#filter_dataTable_RiskTraceability');			
	openLoaderIcon();
	$.ajax({
		type: "POST",
		url : url,
		success: function(data) {					
			closeLoaderIcon();
			var finalArr = new Array();
			if(data.Result=="ERROR"){
			}else{
				data = data.Records;
			}
			$("#dataTableContainerRiskTraceability").show();
			$("#environmentContent, #priorityWiseContent, #featureWiseContent,#decoupleWiseContent, #TargTestBeds,#testbedFeature,#featureByTestbeds").hide();
			$("#noDataTable").hide();
			$("#dataTableContainerRiskTraceability #filter_dataTable_RiskTraceability").show();				    		
			var table=	$('#filter_dataTable_RiskTraceability').dataTable( {
				paging: false,				    				
				destroy: true,
				searching: true,
				bJQueryUI: false,
				"scrollY":"100%",
				"scrollX":true,
				//  "scrollY":scrollValue,
				aaData:data,
				"columnDefs": [ {
					"visible": false,
				} ],
				bAutoWidth : true,
				bSort : false,
				dom: 'T<"clear">lfrtip',
				tableTools: {
					"sSwfPath": "//cdn.datatables.net/tabletools/2.2.2/swf/copy_csv_xls_pdf.swf"
				},
				oTableTools: {
					"aButtons": [
					             "copy",
					             "print",
					             {
					            	 "sExtends":    "collection",
					            	 "sButtonText": "Save",
					            	 "aButtons":    [ "csv", "xls", "pdf" ]
					             }
					             ]
				},
				aoColumns: [
				            { mData: "riskId" },
				            { mData: "featureId"	}, 
				            { mData: "riskName" },		    				           
				            { mData: "severityName1" },
				            { mData: "likeliHoodName1" },
				            { mData: "riskPriority1" },
				            { mData: "RiskMitigationCode" },		    				           
				            { mData: "severityName2" },
				            { mData: "likeliHoodName2" },    
				            { mData: "riskPriority2"}, 
				            //{ mData: "testCaseId" },									   						
				            { mData: "testCaseId",  "render": function ( overalldata, type, full ) {		    				              
				            	var tcidexehistory = overalldata;
				            	var tcnameexehistory = findTestCaseNameByID(data,overalldata);
				            	console.log("id:name :"+tcidexehistory," - ",tcnameexehistory);	
				            	var dataLevel = "productLevel";
				            	return '<a href="javascript:listTCExecutionSummaryHistoryView(\''+tcidexehistory+'\',\'' + tcnameexehistory+ '\',\'' +dataLevel+'\');">'+overalldata+'</a>';
				            }
				            },	
				            { mData: "testcaseexeid" },
				            { mData: "result" },
				            { mData: "iterationNumber" },	
				            { mData: "lifecyclephase" },
				            { mData: "date" },
				            { mData: null,				 
				            	bSortable: false,
				            	mRender: function(data, type, full) {				            	
				            		var img = ('<div style="display: flex;">'+
				            				'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
				            				'<i class="fa fa-comments img1" title="Comments"></i></button>'+
				            		'</div>');	      		
				            		return img;
				            	}
				            },

				            ]
			});
			$(function(){ // this will be called when the DOM is ready 
				// ----- product Comments child table -----
				$('#filter_dataTable_RiskTraceability tbody').on('click', 'td button .img1', function () {
					var tr = $(this).closest('tr');
					var row = table.DataTable().row(tr);
					var entityTypeIdComments = 43;
					var entityNameComments = "Risk ";
					listComments(entityTypeIdComments, entityNameComments, row.data().riskId, row.data().riskName, "riskComments");
				});
			});
		},
		error : function(data){					
			closeLoaderIcon();
		},
		complete : function(data){				
			closeLoaderIcon();
			$("#ToolTables_filter_dataTable_RiskTraceability_0").hide();
			$("#ToolTables_filter_dataTable_RiskTraceability_4").hide();
		}
	});	 
}

function traceabilityTestReport(url){
	scrollValue = clearDataTableValues('#filter_dataTable_TestReport');			
	openLoaderIcon();
	$.ajax({
		type: "POST",
		url : url,
		success: function(data) {					
			closeLoaderIcon();
			var finalArr = new Array();
			if(data.Result=="ERROR"){
			}else{
				data = data.Records;
			}
			$("#dataTableContainerTestReport").show();	
			$("#dataTableContainerTestReport #filter_dataTable_TestReport").show();				    		
			var table=	$('#filter_dataTable_TestReport').dataTable( {
				paging: false,				    				
				destroy: true,
				searching: true,
				bJQueryUI: false,
				"scrollX":true,
				"scrollY":"100%",
				aaData:data,
				"columnDefs": [ {
					"visible": false,
				} ],
				bAutoWidth : true,
				bSort : false,
				dom: 'T<"clear">lfrtip',
				tableTools: {
					"sSwfPath": "//cdn.datatables.net/tabletools/2.2.2/swf/copy_csv_xls_pdf.swf"
				},
				oTableTools: {
					"aButtons": [
					             "copy",
					             "print",
					             {
					            	 "sExtends":    "collection",
					            	 "sButtonText": "Save",
					            	 "aButtons":    [ "csv", "xls", "pdf" ]
					             }
					             ]
				},
				aoColumns: [
				            { mData: "featureId" },
				            { mData: "testCaseId",  "render": function ( overalldata, type, full ) {		    				              
				            	var tcidexehistory = overalldata;
				            	var tcnameexehistory = findTestCaseNameByID(data,overalldata);
				            	console.log("id:name :"+tcidexehistory," - ",tcnameexehistory);	
				            	var dataLevel = "productLevel";
				            	return '<a href="javascript:listTCExecutionSummaryHistoryView(\''+tcidexehistory+'\',\'' + tcnameexehistory+ '\',\'' +dataLevel+'\');">'+overalldata+'</a>';
				            }
				            },
				            { mData: "testcaseexeid" },
				            { mData: "scriptId" },		    				           
				            { mData: "status" },
				            { mData: "scriptdate" },
				            { mData: "lifecyclephase" },
				            { mData: "iterationNumber" },		    				           
				            { mData: "date" },
				            { mData: "result" },    
				            { mData: "bugid"}, 		    				          	
				            { mData: "stageName" },
				            { mData: "reOpenDate" },
				            { mData: "resolution" },	
				            ]
			});
		},
		error : function(data){					
			closeLoaderIcon();
		},
		complete : function(data){				
			closeLoaderIcon();
			$("#ToolTables_filter_dataTable_TestReport_0").hide(); // -- copy button
			$("#ToolTables_filter_dataTable_TestReport_4").hide(); // - print button
		}
	});	 
}

function findTestCaseNameByID(recordArr, testCaseID){
	var testCaseName='';
	for(var i=0;i<recordArr.length;i++){
		if(recordArr[i].testCaseId == testCaseID){
			testCaseName = recordArr[i].testcasename;
			break;
		}
	}
	return testCaseName;
}

function clearDataTableValues(idValue){
	try{		
		if ($(idValue).length>0) {			
			$(idValue).dataTable().find('tbody').html("");
		}
	} catch(e) {}
	var scrollValue=0;
	return scrollValue;
}

//BEGIN: ConvertDataTable - DefectTraceability
var defectTraceabilityDT_oTable='';
var editorDefectTraceability='';

function listDefectReportDataTable(){
	url= urlToGetDefectReportOfSpecifiedProductId +'&jtStartIndex=0&jtPageSize=10';
	jsonObj={"Title":"Defect Traceability","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Defect Traceability"};
	defectTraceabilityDataTableContainer.init(jsonObj);
}

var defectTraceabilityDataTableContainer = function() {
	var initialise = function(jsonObj){
		assignDefectTraceabilityDataTableValues(jsonObj);
	};
	return {
		//main function to initiate the module
		init: function(jsonObj) {        	
			initialise(jsonObj);
		}		
	};	
}();

function assignDefectTraceabilityDataTableValues(jsonObj){
	openLoaderIcon();			
	$.ajax({
		type: "POST",
		url: jsonObj.url,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {		
			closeLoaderIcon();
			if(data.Result=="ERROR"){
				data = [];						
			}else{
				data = data.Records;
			}					
			jsonObj.data = data;
			defectTraceabilityDT_Container(jsonObj);
		},
		error : function(data) {
			closeLoaderIcon();  
		},
		complete: function(data){
			closeLoaderIcon();
		}
	});	
}

function defectTraceabilityDataTableHeader(){
	var childDivString ='<table id="defectTraceability_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Defect ID</th>'+
			'<th>Date Raised</th>'+
			'<th>Test Case ID</th>'+
			'<th>Execution ID</th>'+
			'<th>Test Script ID</th>'+
			'<th>Test Execution Phase</th>'+
			'<th>Iteration</th>'+
			'<th>Date</th>'+	
			'<th>Severity</th>'+
			'<th>Current Status</th>'+
			'<th>Re-Open Date</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}
function defectTraceabilityDT_Container(jsonObj){
	try{
		if ($("#dataTableContainerForDefectTraceability").children().length>0) {
			$("#dataTableContainerForDefectTraceability").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = defectTraceabilityDataTableHeader(); 			 
	$("#dataTableContainerForDefectTraceability").append(childDivString);

	editorDefectTraceability = new $.fn.dataTable.Editor( {
		"table": "#defectTraceability_dataTable",
		idSrc:  "bugid",
		i18n: {
			create: {
				title:  "Create a new Defect",
				submit: "Create",
			}
		},
		fields: []
	});	

	defectTraceabilityDT_oTable = $("#defectTraceability_dataTable").dataTable( {				 	
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		// "sScrollX": "100%",
		//"sScrollXInner": "100%",
		"scrollY":"100%",
		"bScrollCollapse": true,	 
		//"aaSorting": [[4,'desc']],
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
			$('#defectTraceability_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeDefectTraceabilityDT();
		},  
		buttons: [
		          {
		        	  extend: 'collection',
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Defect Traceablity',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Defect Traceablity',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],	                
		          },
		          'colvis',
		          ], 
		          columnDefs: [
		                       ],
		                       aaData:jsonObj.data,		    				 
		                       aoColumns: [	        	        
		                                   { mData: "bugid",className: 'disableEditInline', sWidth: '8%' },			
		                                   { mData: "bugCreationTime",className: 'disableEditInline', sWidth: '10%' },
		                                   { mData: "testCaseId",className: 'disableEditInline', sWidth: '8%' },			
		                                   { mData: "testcaseexeid",className: 'disableEditInline', sWidth: '8%' },
		                                   { mData: "scriptId",className: 'disableEditInline', sWidth: '10%' },
		                                   { mData: "lifecyclephase",className: 'disableEditInline', sWidth: '10%' },		
		                                   { mData: "iterationNumber",className: 'disableEditInline', sWidth: '5%' },           
		                                   { mData: "date", className: 'disableEditInline', sWidth: '10%'},	
		                                   { mData: "severityName", className: 'disableEditInline', sWidth: '10%'},
		                                   { mData: "result", className: 'disableEditInline', sWidth: '10%'},
		                                   { mData: "reOpenDate", className: 'disableEditInline', sWidth: '10%'},
		                                   ],       
		                                   rowCallback: function ( row, data ) {
		                                	   //$('input.editorDefectTraceability-active', row).prop( 'checked', data.isSelected == 1 );
		                                   },
		                                   "oLanguage": {
		                                	   "sSearch": "",
		                                	   "sSearchPlaceholder": "Search all columns"
		                                   },   
	}); 

	// Activate an inline edit on click of a table cell
	$('#defectTraceability_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorDefectTraceability.inline( this, {
			submitOnBlur: true
		} );
	});

	$("#defectTraceability_dataTable_length").css('margin-top','8px');
	$("#defectTraceability_dataTable_length").css('padding-left','35px');

	$("#defectTraceability_dataTable_filter").css('margin-right','6px');

	defectTraceabilityDT_oTable.DataTable().columns().every( function () {
		var that = this;
		$('input', this.footer() ).on( 'keyup change', function () {
			if ( that.search() !== this.value ) {
				that
				.search( this.value, true, false )
				.draw();
			}
		} );
	} );

}

var clearTimeoutDefectTraceabilityDT='';
function reInitializeDefectTraceabilityDT(){
	clearTimeoutDefectTraceabilityDT = setTimeout(function(){				
		defectTraceabilityDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDefectTraceabilityDT);
	},200);
}
//END: ConvertDataTable - DefectTraceability

function executeEditTestrunplan(){
	executeEditTrplan = 'ExecuteEditTestRunPlan';
	testRunPlanForBDD(trpId,null);
}

function removeEntityConfProperty(entityConfigId, entityMasterId, trpId){
	testRunPlanId = trpId;
	var url = 'entityconfigproperty.remove?entityConfigId='+entityConfigId+'&entityMasterId='+entityMasterId+'&entityId='+testRunPlanId;
	$.ajax({
		type: "POST",
		url: url,
		success: function(data) {		           			
			if(data.Result == "ERROR"){
				callAlert(data.Message);
				return false;
			}	else if(data.Result == "OK"){						
				callAlert(data.Message);			
			}
		}        
	});  
}

function removeRunConfiguration(runConfigId, testRunPlanId){
	var url = 'administration.remove.runconfiguration?runConfigId='+runConfigId+'&testRunPlanId='+testRunPlanId;
	$.ajax({
		type: "POST",
		url: url,
		success: function(data) {		           			
			if(data.Result == "ERROR"){
				callAlert(data.Message);
				return false;
			}	else if(data.Result == "OK"){						
				callAlert(data.Message);			
			}
		}        
	});  
}

function saveRunConfigTestSuiteTestCaseMap(flag,testCaseId,testSuiteListId,selectedValue){
	if(!$(self).is(':checked')){
		url="administration.testsuite.mapunmaprunconfig?runConfigId="+rnConfigId+"&testSuiteId="+testSuiteListId+"&action=Remove";		 
	}else{
		url="administration.testsuite.mapunmaprunconfig?runConfigId="+rnConfigId+"&testSuiteId="+testSuiteListId+"&action=Add";
	}

	if(flag==1){
		var url="administration.testsuite.testcase.maprunconfig?testCaseId="+testCaseId+"&runConfigId="+rnConfigId+"&testSuiteId="+testSuiteListId+"&type=Add";		 
		$.ajax({
			type: "POST",
			url: url,
			success: function(data) {
				if(data.Result=='ERROR'){
					callAlert(data.Message);
					listTestSuiteSelectedProductVersionPlan(productId, productVersionId, timestamp,"",testRunPlanId);
					return false;
				}else{
					callAlert(data.Message);
					listTestSuiteSelectedProductVersionPlan(productId, productVersionId, timestamp,"",testRunPlanId);
					return true;
				}
			},    
			dataType: "json",
		});
	}else{
		var url="administration.testsuite.testcase.maprunconfig?testCaseId="+testCaseId+"&runConfigId="+rnConfigId+"&testSuiteId="+testSuiteListId+"&type=Remove";
		$.ajax({
			type: "POST",
			url: url,
			success: function(data) {
				if(data.Result=='ERROR'){
					callAlert(data.Message);
					listTestSuiteSelectedProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
					return false;
				}else{
					callAlert(data.Message);
					listTestSuiteSelectedProductVersionPlan(productId, productVersionId, timestamp,type,testRunPlanId);
					return true;
				}
			},    
			dataType: "json",
		}); 
	}
}

var toolIdRowValue;
var toolTypeNameRowValue="";
function displayTestmanagementSyndata(toolId,toolTypeName) {
	if(toolTypeName == "Test Management Systems") {
		//$('#defectManagementCheckBoxs').hide();
		$("#defectManagementCheckBoxs").css('opacity',0);
		$('#testManagementCheckBoxes').show();
		$('#testManagementMappingsCheckBoxs').show();
		$("#div_testManagementSynData").modal();
	} else if(toolTypeName == "Defect Management System") {
		$('#testManagementCheckBoxes').hide();
		$('#testManagementMappingsCheckBoxs').hide();
		//$('#defectManagementCheckBoxs').show();
		$("#defectManagementCheckBoxs").css('opacity',1);
		$("#div_testManagementSynData").modal();
	} else if("SCM Systems") {
		callAlert("Service not Available");
		$('#testManagementCheckBoxes').hide();
		$('#testManagementMappingsCheckBoxs').hide();
		//$('#defectManagementCheckBoxs').hide();
		$("#defectManagementCheckBoxs").css('opacity',0);
		//$("#div_testManagementSynData").modal();
	}
	toolIdRowValue=toolId;
	toolTypeNameRowValue=toolTypeName;
}
function submitSyncTestmanagement() {
	
	var synFeatures=$('#uniform-syncFeatures span').hasClass('checked');
	var synTestcases=$('#uniform-syncTestcases span').hasClass('checked');
	var synTestsuites=$('#uniform-syncTestsuites span').hasClass('checked');
	var syncTestcasesToTestsuitesMapping=$('#uniform-syncTestcasesToTestsuitesMapping span').hasClass('checked');
	var syncFeaturesToTestcasesMapping=$('#uniform-syncFeaturesToTestcasesMapping span').hasClass('checked');
	var exportDefectes = $('#uniform-reportDefects span').hasClass('checked');
	//var exportResults=$('#uniform-reportResults span').hasClass('checked');
	var exportResults=true;
	var url='process.tool.management.syn.data?productId='+productId+'&toolId='+toolIdRowValue+'&toolTypeName='+toolTypeNameRowValue+'&synFeatures='+synFeatures+'&synTestcases='+synTestcases+'&synTestsuites='+synTestsuites+'&syncTestcasesToTestsuitesMapping='+syncTestcasesToTestsuitesMapping+'&syncFeaturesToTestcasesMapping='+syncFeaturesToTestcasesMapping+'&exportDefectes='+exportDefectes+'&exportResults='+exportResults;
	/*$.post('process.tool.management.syn.data?productId='+productId+'&toolId='+toolIdRowValue+'&toolTypeName='+toolTypeNameRowValue+'&synFeatures='+synFeatures+'&synTestcases='+synTestcases+'&synTestsuites='+synTestsuites+'&syncTestcasesToTestsuitesMapping='+syncTestcasesToTestsuitesMapping+'&syncFeaturesToTestcasesMapping='+syncFeaturesToTestcasesMapping+'&exportDefectes='+exportDefectes+'&exportResults='+exportResults, function(data) {
	});*/
	
	/*$.ajax({
		type: "POST",
        contentType: "application/json",
		url : url,
		dataType : 'json',		
		success : function(data) {				
			if(data.Result=="OK"){
				callAlert(data.Message);
			}
			else if(data.Result=="ERROR"){
				callAlert(data.Message);
			}
			
		},error : function(data){
		},
		complete: function(data){
		 }
	});
	*/
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {        
			if(data != null && (data.Result=="Error" || data.Result == "ERROR")){
				callAlert(data.Message);
				return false;
			}else { 	 
				if(data != null && (data.Result=="OK" || data.Result=="Ok")){
					callAlert(data.Message);
				} 
			}               
		}
	});
	$("#div_testManagementSynData").modal('hide');
	
}

function popupCloseSyncTestmanagementHandler(){
	$("#div_testManagementSynData").modal('hide');
}
