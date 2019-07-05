var postBug = 0;
var postTestResult = 0;
var testRunPlanName ='' ;
var testrunplanId = 0;
var executeEditTrplan ='' ;
var productId=0;		
var productVersionId = 0;
var saveScript;
var testCaseExecution = ''; 
var testrunPlanIdForTestCaseExecution='';
var testrunPlanId;

function showCorrespondingTableTestRun(obj) {
	var toShow = obj;
	//$('#form_wizard_1').hide();		
	if(toShow == 1) {
		$("#jTableContainertestrun").show();
		$("#jTableContainertestrunplangroup").hide();
		$("#jTableContainertestrunTileView").hide();
	}else{
		$("#jTableContainertestrun").hide();			
		$("#jTableContainertestrunplangroup").show();
		$("#jTableContainertestrunTileView").hide();
	}
}	
	
function showCorrespondingTableTestRunTileView() {
	//$('#form_wizard_1').hide();		
	$("#jTableContainertestrun").hide();
	$("#jTableContainertestrunplangroup").hide();
	$("#jTableContainertestrunTileView").show();
}	

var key ='';
var nodeType='';
var addorno="yes";
//var userRoleId='-1';
//var userId='-1';
var title='';
var date = new Date();
var timestamp = date.getTime();
var filterVal;
var isEngagementLevelFlag = true;

jQuery(document).ready(function() {
   QuickSidebar.init(); // init quick sidebar
   ComponentsPickers.init();
   setBreadCrumb("Manage Products");
   createHiddenFieldsForTree();
   createHiddenFieldsForUserDetails();
   setPageTitle("Products");
   getTreeData("administration.product.tree");
   
   //getTreeData('administration.testrunplan.tree?type=1');
   //userRoleId='<%=session.getAttribute("roleId")%>';
   //userId='<%=session.getAttribute("ssnHdnUserId")%>';
   
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
	   			if(data.node.icon=='fa fa-close' || data.node.icon=='fa fa-lock'){
   					editableFlag=false;
	   			}else{
   					editableFlag=true;
				}
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

			    if(nodeType == "Product"){
			    		productId = key;
			    		document.getElementById("hdnProductName").value = data.node.text;
			    		testFactoryId = document.getElementById("treeHdnCurrentTestFactoryId").value;
			    		document.getElementById("hdnProductId").value=productId;
			    		document.getElementById("hdntestFactoryId").value = testFactoryId;
						fetchProductType(productVersionId, productId);
			    		if(productId==null && productId==undefined){
			    			productId=0;
			    		}
			    		productVersionId=-1;
			    }else if(nodeType == "ProductVersion"){
			    	productVersionId = key;
			    	productId = document.getElementById("treeHdnCurrentProductId").value;    	
					document.getElementById("hdnProductVersionId").value=productVersionId;
					testFactoryId = document.getElementById("treeHdnCurrentTestFactoryId").value;
					document.getElementById("hdntestFactoryId").value = testFactoryId;
					fetchProductType(productVersionId, productId);
					if(productVersionId==null && productVersionId==undefined){
						productVersionId=0;
					}	
					addorno = "yes";
				}else{
			    	prodId = document.getElementById("treeHdnCurrentProductId").value;
			    	productBuildId = document.getElementById("treeHdnCurrentProductBuildId").value;
			    }
			    selectedTab=$("#tabslist>li.active").index();
			    tabSelection(selectedTab);	  
	     	});
	   

	   $(document).on('change','#status_ul', function() {
			var status = $("#status_ul").find('option:selected').val();				
		    urlToGetProductsByProductIdAndTestFactoryId = 'administration.testfactory.customer.product.list.bystatus?testFactoryId='+testFactoryId+'&productId='+productId+'&status='+status;
			listProductsByTestFactory(urlToGetProductsByProductIdAndTestFactoryId);
		 });
		    
	   $(document).on('change','#envCombinationstatus_ul', function() {
			var envComstatus = $("#envCombinationstatus_ul").find('option:selected').val();				
			 urlToGetEnvironmentCombinationByProduct = "environment.combination.list.of.selectedProduct?productVersionId=-1&productId="+productId+"&workpackageId=-1&testRunPlanId=-1&envComstatus="+envComstatus;
			 listEnvCombinationByProduct(); 
		 });
	   
	   $(document).on('change','#selectEnvironmentstatus_ul', function() {
			var envstatus = $("#selectEnvironmentstatus_ul").find('option:selected').val();		 
			 urlToGetEnvironmentOfSpecifiedProductId = 'administration.product.environment.list?productMasterId='+productId+"&envstatus="+envstatus+"&timeStamp="+timestamp;
			 listEnvironmentSelectedProduct();			 
	   });
	   
	   $(document).on('change','#downloadScriptType_ul', function() {  
			scriptTypeUnique = $("#downloadScriptType_ul").find('option:selected').val();		
		});

		$(document).on('change','#downloadTestEngineType_ul', function() {
			testEngineUnique =  $("#downloadTestEngineType_ul").find('option:selected').val();		
		});

		$(document).on('change','#downloadTestStepOption_ul', function() {
			testStepOption =  $("#downloadTestStepOption_ul").find('option:selected').val();		
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
var testFactoryId=-1;
var productId=-1;
var productVersionId=-1;
function setTitle(dd,id,text){	
	dv =$(dd).children('div');
	dv.text(text);	
	dv.attr('id',id);	
}

function popupClose() {	
	$("#div_PopupMain").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
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



function popupDevicesClose() {	
	$("#div_PopupDevices").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

function popupTerminalClose() {	
	$("#div_PopupTerminal").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

function popUpDownloadTestScriptsclose() {	
	$("#divPopUpDownloadTestScriptsFromTestCases").fadeOut("normal");	
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

function tabSelection(selectedTab){
	 if(selectedTab==0){						  
		var firstTab = $("#TestRunPlanTab");		  
		if(!(firstTab.hasClass("active in"))){
			firstTab.addClass("active in");
			firstTab.siblings(".tab-pane").removeClass("active in");
		}		  
		testRun();		
	 }else if (selectedTab==7){
		 environments();	  
	 }else if (selectedTab==8){
		 devicesDetails();	  
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
	tabSelection(selectedTab);
});

function hideTabs(){
	$("a[href^=#CoreResources]").parent("li").hide();
	testRun
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
	listProductsByTestFactory(urlToGetProductsByProductIdAndTestFactoryId);	
}

function testcases(){		
	setProductNode();
	
	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#Testcases').children().show();
		urlToGetTestCasesOfSpecifiedProductId = 'product.testcase.list?productId='+productId;
		listTestCasesOfSelectedProduct();
	}	
}

function setProductNode() {
	if(nodeType == "") return false;
	if(nodeType == "TestFactory") {
		setChildNode();
	}else if(nodeType == "ProductVersion") {
		setParentNode();
	}
	return true;
}

function features(){
	setProductNode();
	
	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#Features').children().show();
		urlToGetFeaturesOfSpecifiedProductId = 'administration.product.feature.list?productMasterId='+productId,
		listFeaturesOfSelectedProduct();
	}	
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

function decoupling(){	
	setProductNode();

	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#Decoupling').children().show();
		urlToGetDecouplingCategoriesOfSpecifiedProductId = 'administration.product.decouplingcategory.list?productMasterId='+productId,
		listDecouplingCategoriesOfSelectedProduct();
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
	
	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#ChangeRequests').children().show();
		urlToGetChangeRequestsOfSpecifiedProductId = 'list.change.requests.by.product?productId='+productId,
		listChangeRequestsOfSelectedProduct();
	}	
}

var currTable = 1;
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

function environmentPlan(){
	if(nodeType == "ProductVersion"){
	   	productVersionId = key;		
	}else{
		productVersionId = hdnProductVersionId;
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

function testRun(){
	$(".trp_radGrp").find("label:first").addClass("active").siblings("label").removeClass("active");
	showCorrespondingTableTestRun(1);
	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		productId=0;
	}
	
	if(nodeType == "TestFactory"){
		isEngagementLevelFlag = true;
		testFactoryId = key;
		if(testFactoryId == null || testFactoryId <= 0 || testFactoryId == 'null' || testFactoryId == ''){
			callAlert("Please select the TestFactory");
			return false;
		}
		$('#TestRunPlanTab').children().not("#form_wizard_1").show();
		var date = new Date();
	    var timestamp = date.getTime();
		urlToGetTestRunPlanOfSpecifiedProductId = 'administration.testrunplan.listbytestFactorProductorVersion?productversionId=-1&productId=-1&testFactoryId='+testFactoryId;	
	}else if(nodeType == "Product"){
		isEngagementLevelFlag = false;
		productId = key;
		if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");
			return false;
		}
		$('#TestRunPlanTab').children().not("#form_wizard_1").show();
		var date = new Date();
	    var timestamp = date.getTime();
		urlToGetTestRunPlanOfSpecifiedProductId = 'administration.testrunplan.listbytestFactorProductorVersion?productversionId=-1&productId='+productId+"&testFactoryId=-1";	
	}else if(nodeType == "ProductVersion"){
		isEngagementLevelFlag = false;
	    	productVersionId = key;
		if(productVersionId == null || productVersionId <= 0 || productVersionId == 'null' || productVersionId == ''){
			callAlert("Please select the ProductVersion");
			return false;
		}		
		$('#TestRunPlanTab').children().not("#form_wizard_1").show();
		var date = new Date();
	    var timestamp = date.getTime();
	   urlToGetTestRunPlanOfSpecifiedProductId = 'administration.testrunplan.listbytestFactorProductorVersion?productversionId='+productVersionId+"&productId=-1&testFactoryId=-1";	
	}
	 
	 // ----- testrunplan table view -----	
	 testsuitesTabFlag=false;
	 var jsonObj={		
		"url":urlToGetTestRunPlanOfSpecifiedProductId
	 };
	 TestRunPlanDT.init(jsonObj);

	var jsonObj={
		"url":urlToGetTestRunPlanOfSpecifiedProductId
	};
	TestPlanGridView.init(jsonObj);
}

var urlToGetProductTeamResourcesOfSpecifiedProductId;
function productTeamResources(){
	setProductNode();
	
	if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
		callAlert("Please select the Product");
		return false;
	}
	else{
		$('#ProductTeamResources').children().show();
		// the following url needs to be changed to get data from product Team resource table
		urlToGetProductTeamResourcesOfSpecifiedProductId = 'product.team.resouces.list?productId='+productId;
		listProductTeamResourcesOfSelectedProduct();
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
         paging: true, //Enable paging
         pageSize: 10, 
         editinline:{enable:true},
         actions: {
        	 listAction:'product.testcase.version.list?productId='+productId,
    		 editinlineAction:'testcase.to.productversion.add.bulk'   
         },
        
         fields : fields
		});
	 $('#jTableContainerversiontestcase').jtable('load');
 }

//BEGIN: ConvertDataTable - Execution History
var executionHistoryDT_oTable='';
function executionHistoryDataTableContainer(jsonObj){
	try{
		if ($("#executionHistoryTableContainer").children().length>0) {
			$("#executionHistoryTableContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = executionHistoryDataTableHeader(); 			 
	$("#executionHistoryTableContainer").append(childDivString);
	
	executionHistoryDT_oTable = $("#executionHistory_dataTable").dataTable( {
	 	//"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
	 	"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
	    "sScrollX": "100%",
       "sScrollXInner": "100%",
       "scrollY":"100%",
       "bScrollCollapse": true,	      
      // "aaSorting": [[5,'desc']],
       "fnInitComplete": function(data) {
    	   var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
    	   $('#executionHistory_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
      	  reInitializeExecutionHistoryDT();
	   },  
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'Execution History',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Execution History',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Execution History',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },	                    
	                ],	                
	            },
	           // 'colvis'
	           ], 
          aaData:jsonObj.data,		    				 
          aoColumns: [	        	        
		       { mData: "name", className: 'disableEditInline', sWidth: '25%' },		
		       { mData: "description", className: 'disableEditInline', sWidth: '25%' },		
		       { mData: "plannedStartDate", className: 'disableEditInline', sWidth: '25%' },		
		       { mData: "plannedEndDate", className: 'disableEditInline', sWidth: '25%' },		
		   ],
		   rowCallback: function ( row, data ) {
			  if(data['isModified']) {
				  $(row).find('td:eq(1)').css('backgroundColor', 'orange');
		      }
		   },
		   "oLanguage": {
			   "sSearch": "",
				"sSearchPlaceholder": "Search all columns"
		   },     
	});
	
	$(function(){ // this will be called when the DOM is ready 
		
		$("#executionHistory_dataTable_length").css('margin-top','8px');
		$("#executionHistory_dataTable_length").css('padding-left','35px');
	});
	
	$('#executionHistory_dataTable').DataTable().columns().every( function () {
        var that = this;
        $('input', this.footer() ).on( 'keyup change', function () {
            if ( that.search() !== this.value ) {
                that
                	.search( this.value, true, false )
                    .draw();
            }
        });
    });
}

var clearTimeoutExecutionHistoryDT='';
function reInitializeExecutionHistoryDT(){
	clearTimeoutExecutionHistoryDT = setTimeout(function(){				
		executionHistoryDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutExecutionHistoryDT);
	},200);
}

function executionHistoryDataTable(testRunPlanId){
	var url = 'administration.workPackage.list.bytestRunPlanId?testRunPlanId='+testRunPlanId+'&jtStartIndex=0&jtPageSize=10000';
	var jsonObj = 	{
						"Title":"Test Configuration",
						"url":url,
						"jtStartIndex":0,
						"jtPageSize":1000,
						"componentUsageTitile":"Test Configurations"
					};
	assignExecutionHistoryDataTableValues(jsonObj);
};

function assignExecutionHistoryDataTableValues(jsonObj){
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:jsonObj.url,
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
			executionHistoryDataTableContainer(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function executionHistoryDataTableHeader(){
	var childDivString = '<table id="executionHistory_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
				'<th class="dataTableChildHeaderTitleTH">Name</th>'+
				'<th class="dataTableChildHeaderTitleTH">Description</th>'+
				'<th class="dataTableChildHeaderTitleTH">Planned Start Date</th>'+
				'<th class="dataTableChildHeaderTitleTH">Planned End Date</th>'+
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
//END: ConvertDataTable - Execution History

//BEGIN: ConvertDataTable - Test Configurations
var runConfigurationsDT_oTable='';
function runConfigurationsDataTableContainer(jsonObj){
	try{
		if ($("#dataTableRunConfigurationsContainer").children().length>0) {
			$("#dataTableRunConfigurationsContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = runConfigurationsDataTableHeader(); 			 
	$("#dataTableRunConfigurationsContainer").append(childDivString);
	
	runConfigurationsDT_oTable = $("#runConfigurations_dataTable").dataTable( {
	 	//"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
	 	"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
	    "sScrollX": "100%",
     "sScrollXInner": "100%",
     "scrollY":"100%",
     "bScrollCollapse": true,	      
    // "aaSorting": [[5,'desc']],
     "fnInitComplete": function(data) {
  	   var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
  	   $('#runConfigurations_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
    	  reInitializeRunConfigurationsDT();
	   },  
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'Test Configurations',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Test Configurations',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Test Configurations',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },	                    
	                ],	                
	            },
	           // 'colvis'
	           ], 
        aaData:jsonObj.data,		    				 
        aoColumns: [	        	        
		       { mData: "environmentcombinationName", className: 'disableEditInline', sWidth: '50%' },		
		       { mData: "deviceName", className: 'disableEditInline', sWidth: '50%' },		
		   ],
		   rowCallback: function ( row, data ) {
			  if(data['isModified']) {
				  $(row).find('td:eq(1)').css('backgroundColor', 'orange');
		      }
		   },
		   "oLanguage": {
			   "sSearch": "",
				"sSearchPlaceholder": "Search all columns"
		   },     
	});
	
	$(function(){ // this will be called when the DOM is ready 
		
		$("#runConfigurations_dataTable_length").css('margin-top','8px');
		$("#runConfigurations_dataTable_length").css('padding-left','35px');
	});
	
	$('#runConfigurations_dataTable').DataTable().columns().every( function () {
      var that = this;
      $('input', this.footer() ).on( 'keyup change', function () {
          if ( that.search() !== this.value ) {
              that
              	.search( this.value, true, false )
                  .draw();
          }
      });
  });
}

var clearTimeoutRunConfigurationsDT='';
function reInitializeRunConfigurationsDT(){
	clearTimeoutRunConfigurationsDT = setTimeout(function(){				
		runConfigurationsDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutRunConfigurationsDT);
	},200);
}

function runConfigurationsDataTable(testRunPlanId){
	var url = 'runConfiguration.listbyTestRunPlan?testRunPlanId='+testRunPlanId+'&jtStartIndex=0&jtPageSize=10000';
	var jsonObj = 	{
						"Title":"Test Configuration",
						"url":url,
						"jtStartIndex":0,
						"jtPageSize":1000,
						"componentUsageTitile":"Test Configurations"
					};
	assignRunConfigurationsDataTableValues(jsonObj);
};

function assignRunConfigurationsDataTableValues(jsonObj){
	openLoaderIcon();
	$.ajax({
		type: "POST",
		url:jsonObj.url,
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
			runConfigurationsDataTableContainer(jsonObj);
		 },
		 error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});
}

function runConfigurationsDataTableHeader(){
	var childDivString = '<table id="runConfigurations_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
				'<th class="dataTableChildHeaderTitleTH">Environment Configuration</th>'+
				'<th class="dataTableChildHeaderTitleTH">Device</th>'+
		'</tr>'+
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
//END: ConvertDataTable - Test Configurations

function myObj(len) {	
	var obj = {};
	for(var i=1;i<=len;i++){
		obj[""+i] = "" +i;
	}
	return obj;
}

function callConfirmTestRunPlan(event){
	var arr = (event.target.id).split('~');
	var TRPlanId=arr[0];
	var TRPlanName=arr[1];
	var TRProuductId=arr[2];
	var TRProductName=arr[3];
	var TRProductVersionId=arr[4];
	var TRExecutionId=arr[5];
	
	testRunPlan(TRPlanId ,TRExecutionId);     	
		
	var jsonObj={};
		jsonObj.Title = "TestRunPlan : "+TRPlanName;
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
function reInitializeFormWizard() {
	$('#form_wizard_1').replaceWith(formClone.clone());
	testRun();
	document.getElementById("hdnTestRunPlanId").value="";
	document.getElementById("hdnTestRunPlanType").value="";
	initializeBootstrapWizard();
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
         title: 'Add/Edit Host',
         selecting: true, //Enable selecting 
         paging: true, //Enable paging
         pageSize: 10, //Set page size (default: 10)
         editinline:{enable:true},
         actions: {
             listAction: urlToGetTerminalOfSpecifiedProductId,
             createAction: 'administration.host.add',
             editinlineAction: 'administration.host.update',
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
 	    		 display: function (data){
 	    			 if(data.record.hostStatus=="ACTIVE")
 	    				 return '<img val='+data.record.status+' style="cursor:pointer;" src="css/images/right.jpg">'; 
 	    			 else 
 	    				 return '<img val='+data.record.status+' style="cursor:pointer;" src="css/images/crossmark.jpg">';	
      			 },
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
        title: 'Add/Edit Device',
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
	         actions: {
	             listAction: urlToGetEnvironmentOfSpecifiedProductId,
	             createAction: 'administration.product.environment.add',
	             editinlineAction: 'administration.product.environment.update'
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

function trim(str) {
    return str.replace(/^\s+|\s+$/g,"");
}

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

function refreshRunConfigurationUnMappedList( productVersionId, productId, envionmentCombinationId, testRunPlanId){
	 $("#alldevices").empty();
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
						
			}, 
			
			formSubmitting: function (event, data) {
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

//Product Team Resources -Begin
function listProductTeamResourcesOfSelectedProduct(){	
	try{
		if($('#jTableContainerProductTeamResources').length > 0){
			$('#jTableContainerProductTeamResources').jtable("destroy");
		}
	}catch(e){		
	}
	
	$('#jTableContainerProductTeamResources').jtable({
					title: 'Add/Edit Product Team Resources ', 
					editinline:{enable:true},
					paging: true, //Enable paging
				    pageSize: 10, 
					recordsLoaded: function(event, data) {
			        	 $(".jtable-edit-command-button").prop("disabled", true);
			         },
					actions: { 
						listAction:  urlToGetProductTeamResourcesOfSpecifiedProductId, 
						editinlineAction: 'product.team.user.update',  
						createAction: 'product.team.user.add'
							}, 
						recordUpdated:function(event, data){
							$('#jTableContainerProductTeamResources').find('.jtable-child-table-container').jtable('reload');
						},
						recordAdded: function (event, data) {
							$('#jTableContainerProductTeamResources').find('.jtable-child-table-container').jtable('reload');
						},
						recordDeleted: function (event, data) {
							$('#jTableContainerProductTeamResources').find('.jtable-child-table-container').jtable('reload');
						},
			fields: { 
				productId: { 
					type: 'hidden', 
					defaultValue: productId 
					}, 
				productTeamResourceId: { 
					key: true,
				    list: false,
				    create: false
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
				    options: function(data) {
				        if (data.source=='create') {
				        	return 'common.administration.user.userListByResourcePoolId?resourcePoolId=-10'+"&productId="+productId;
				        	
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
			    productSpecificUserRoleId: {
				    title: 'Role',
				   	inputTitle: 'Role <font color="#efd125" size="4px">*</font>',
				    width:"15%",
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
			}, 
			
			formSubmitting: function (event, data) {
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
	 $('#jTableContainerProductTeamResources').jtable('load');
	 
	var jscrolheight = $("#jTableContainerProductTeamResources").height();
	var jscrolwidth = $("#jTableContainerProductTeamResources").width()-20;
	  
	$(".jScrollContainer").on("scroll", function() {
		var lastScrollLeft=0;	
			var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
		 	if (lastScrollLeft < documentScrollLeft) {
			   	$("#jTableContainerProductTeamResources").width($(".jtable").width()).height($(".jtable").height());			
			    lastScrollLeft = documentScrollLeft;
		    }else if(lastScrollLeft >= documentScrollLeft){			
		    	$("#jTableContainerProductTeamResources").width(jscrolwidth).height(jscrolheight);
		    }			
	 });	
}

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
    } else {
        $('#form_wizard_1').find('.button-next').show();
    }
    //Metronic.scrollTo($('.page-title'));
}
initializeBootstrapWizard();

function initializeBootstrapWizard() {
var total='';
$('#form_wizard_1').bootstrapWizard({
    'nextSelector': '.button-next',
    'previousSelector': '.button-previous',
    onTabClick: function (tab, navigation, index, clickedIndex) {
		formWizardNavigation(tab, navigation, clickedIndex);
		formWizardNavigationBar(tab, navigation, clickedIndex);
        return true;
    },
    onNext: function (tab, navigation, index) {
		formWizardNavigation(tab, navigation, index);		
    },
    onPrevious: function (tab, navigation, index) {		
		formWizardNavigation(tab, navigation, index);	
    },
    onTabShow: function (tab, navigation, index) {
    	var $total = navigation.find('li').length;
		var $current = index+1;
		var $percent = ($current/$total) * 100;
		$('#form_wizard_1 .progress-bar').css({width:$percent+'%'});
    }
});
}

function formWizardNavigationBar(tab, navigation, index){
	var total = navigation.find('li').length;
	var current = index + 1;
	var $percent = ( (current) / total) * 100;	
	$('#form_wizard_1').find('.progress-bar').css({
		width: $percent + '%'
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
        handleTitle(tab, navigation, parseInt(index));
}

////  Traceability - Starts /////////
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
	 	var headerName = "Feature Test cases Mapping of "+document.getElementById("hdnProductName").value;
	 	if(isDataAvailabletoLoad){
	 		var jsonObj={"Title":headerName,			          
	    			"urlToGetTree": 'product.feature.testcase.mapping.tree?productId='+$("#hdnProductId").val(),
	    			"urlToGetChildData": "child.features.list.of.parent?parentFeatureId=",	    			
	    			};	 
	 		TreePagination.init(jsonObj);
	 	}
	 // ---- end of testing TreePagination ------
}
////Traceability - Ends /////////

////////////////// Decoupling Category and Test case Mapping Tree - Begins ////////////////
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
	   			productFeatureCode : {
		   				title: 'Feature Code',
		      	 		inputTitle: 'Feature Code <font color="#efd125" size="4px">*</font>',
		  	    		width:"20%",
		  	    		list: true,
						edit : true,
						create : true    	        	
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
		                	} else if(data.source =='create'){
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
		if ($('#jTableContainertestcasetestplan').length>0) {
			 $('#jTableContainertestcasetestplan').jtable('destroy'); 
		}
		}catch(e){}
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
		        
			},
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

function showCorrespondingTableTool(obj) {
	var toShow = obj;
	$(".jtbToolTMS, .jtbToolDMS, .jtbToolAS").hide()
	if(toShow == 1) {
		$(".jtbToolTMS").show();
		$(".jtbToolDMS, .jtbToolAS").hide();
		listTestManagementSystem();
	}else if(toShow == 2) {
		$(".jtbToolDMS").show();
		$(".jtbToolTMS, .jtbToolAS").hide();
		listDefectManagementSystem();
	}else { 
		$(".jtbToolAS").show();
		$(".jtbToolDMS, .jtbToolTMS").hide();
		listAnalyticSystem();
	}
}

function listDefectManagementSystem(){
	if(nodeType == "Product"){
		productId = key;
		if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");
			return false;
		}
	}
	try{
		if ($("#jTableContainerDMS").length>0) {
			 $('#jTableContainerDMS').jtable('destroy');
		}
	} catch(e) {}
    $('#jTableContainerDMS').jtable({
		title: 'Add/Edit Defect Management System', 
		editinline:{enable:true},
		actions: { 
			listAction: 'administration.defect.management.system.list?productId=' +productId, 
			editinlineAction: 'administration.defect.management.system.update', 
			createAction: 'administration.defect.management.system.add'
		},
		fields: { 
			productId: { 
				type: 'hidden', 
				defaultValue: productId
			}, 
			defectManagementSystemId:{
				key: true,    		            			
				list:false,
				edit:false,
				create:false
			},
			title: { 
				title: 'Title',
				inputTitle: 'Title <font color="#efd125" size="4px">*</font>',
				width: "5%"
			},
			description: { 
    			title: 'Description' ,
    			inputTitle: 'Description <font color="#efd125" size="4px">*</font>',
    			width: "5%"          	
    		}, 
          defectSystemName:{
            	  title: 'Defect System',
                  width: "15%",
                  options:{
                	  'JIRA':'JIRA',
                	  'HPQC':'HPQC'
                  }
              },
          defectSystemVersion:{
            	  title: 'Version',
            	  inputTitle: 'Version <font color="#efd125" size="4px">*</font>',
                  width: "15%"
              }, 
    		connectionUri: {            	   
    			title: 'Connection URL',
    			inputTitle: 'Connection URL <font color="#efd125" size="4px">*</font>',
    			width:"15%", 
			},
			isPrimary: { 
	            	title: 'Primary' ,
	            	width: "10%",  
	            	list:true,
	            	create:true,
	            	edit:true,
	            	type : 'checkbox',
	            	values: {'0' : 'No','1' : 'Yes'},
		    		},
			connectionUserName:{
    			title:'Username',
    			inputTitle: 'Username <font color="#efd125" size="4px">*</font>',
    			width:"5%"
			}, 
        	connectionPassword:{
            	title: 'Password',
            	type : 'password',
            	inputTitle: 'Password <font color="#efd125" size="4px">*</font>',
            	display : function(data) {
					return '******';
				}
        	},
        	connectionProperty1: { 
            	title: 'Property 1',
                width: "15%",
                list:false,            	
             },
             connectionProperty2: { 
            	title: 'Property 2',
            	width: "15%",
            	list:false,
            	edit:false
            },
             connectionProperty3: { 
             	title: 'Property 3',
             	width: "15%",
             	edit: false,
             	list:false
             },             
              connectionProperty4: { 
              	title: 'Property 4',
              	width: "15%",
              	edit: false,
              	list:false,
              },
              testConnection: { 
                	title: 'TestConnection',
                	width: "15%",
                	edit: false,
                	list:true,
                	create:false,
                	display:function (data) { 		        		
		           		//Create an image that will be used to open child table 
	           			var $img = $('<img src="css/images/testconnection.gif" title="TestConnection" />'); 
	           			//Open child table when user clicks the image 
	           			$img.click(function () {
	           				testDMSConnection(data.record.defectManagementSystemId);
	           		  });
	           			return $img;
		        	}
                },
             //Defect Management Mappping starts 
                DefectManagementMappping:{
		             title: '',
	                 width: "5%",
	                 edit: true,
	                 create: false,
	              	 display: function (parentData) { 
	              	//Create an image that will be used to open child table 
	              	var $img = $('<img src="css/images/list_metro.png" title="Add/Remove Defect Management System Mapping" />'); 
	              	//Open child table when user clicks the image 
	              	$img.click(function (data) { 
	              	$('#jTableContainerDMS').jtable('openChildTable', 
	              	$img.closest('tr'), 
	              	{ 
	              	title: 'Add/Remove Defect Management System Mapping', 
	              	editinline:{enable:true},
					actions: { 
	              		 listAction: 'administration.defect.management.mapping.list?defectManagementSystemId='+parentData.record.defectManagementSystemId,
	                       createAction: 'administration.defect.management.mapping.add',
	                       editinlineAction: 'administration.defect.management.mapping.update?defectManagementSystemId='+parentData.record.defectManagementSystemId+'&productId='+productId,
	              	}, 
	              	 recordUpdated:function(event, data){
	                     $('#jTableContainerDMS').find('.jtable-child-table-container').jtable('reload');
	                 },
	                 recordAdded: function (event, data) {
	                 	$('#jTableContainerDMS').find('.jtable-child-table-container').jtable('reload');
	                 },	                
	              	fields: {
	              		defectManagementSystemId: { 
	                      	type: 'hidden', 
	                      	edit: false,
	                      	defaultValue: parentData.record.defectManagementSystemId 
	                      }, 
	                      productId: { 
	          				type: 'hidden',
	          				edit: false,
	          				defaultValue: productId
	          			},
	                      defecManagementSystemMappingId: { 
	                      	key: true,     
	                      	list: false,
	          				edit: false,
	          				create: false
	                      }, 
	                      mappingType: {
	  	               		title: 'Mapping Entity',
	  	               		edit:false,
	  	                      options:{
	  	                    	  'Product':'Product',
	  	                    	  'Product Version' : 'Product Version',	  	                    	  
	  	                      }
	                     }, 
	                     mappedEntityIdInTAF: {
	  	                     title: 'Entity Id',
	  	                     width: "15%",
	  	                     list: false,
	  	                     edit : false,
	  	                     create: false,
	      			   },	      			   	   
	      			   mappedEntityNameInTAFOptions: {
	  	                     title: 'Entity Name',
	  	                     edit : false,
	  	                     list : false,
	  	                 	 dependsOn: 'mappingType',
	  	                	 options: function(data){if(data.source == 'create'){return 'common.list.entities?entityType='+data.dependedValues.mappingType+'&productId='+productId;}}
	  	  			   },
	  	  			   mappedEntityNameInTAFId: {
	                       title: 'Entity Name',
	                       create : false,
	                       edit : false,
	          	  	   },
	      				mappingValue : {
	      				    title: 'Mapping Value',
	      				    inputTitle: 'Mapping Value <font color="#efd125" size="4px">*</font>',
	      				},
	      				mappingValueDescription : {
	  	    				title : 'Description',
	  	    				type  : 'textarea', 
	  	    				width : "20%"
	      				}, 
	      				product:{
	  	    				title : 'ProductId',
	  	    				width : "20%",
	  	    				create:false,
	  	    				list: false,
	  	    				edit: false
	      				},
            		},
            		 formSubmitting: function (event, data) {
                    	 data.form.find('input[name="mappingValue"]').addClass('validate[required]');
	                       data.form.validationEngine();
                        return data.form.validationEngine('validate');
                    }, 
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
	 formSubmitting: function (event, data) {
    	 data.form.find('input[name="title"]').addClass('validate[required]');
         data.form.find('input[name="description"]').addClass('validate[required]');
         data.form.find('input[name="defectSystemVersion"]').addClass('validate[required]');
         data.form.find('input[name="connectionUri"]').addClass('validate[required]');
         data.form.find('input[name="connectionUserName"]').addClass('validate[required]');
         data.form.find('input[name="connectionPassword"]').addClass('validate[required]');
         data.form.validationEngine();
        return data.form.validationEngine('validate');
    }, 
     //Dispose validation logic when form is closed
     formClosed: function (event, data) {
        data.form.validationEngine('hide');
        data.form.validationEngine('detach');
    }
 			});
	$('#jTableContainerDMS').jtable('load'); 
}

function testDMSConnection(dmsId){
	 $.ajax({
        type: "POST",
  		contentType: "application/json; charset=utf-8",
        url : 'test.DMS.connection?dmsId='+dmsId,
        dataType : 'json',        
        success : function(data) {
               if(data.Result=="ERROR"){
                     callAlert(data.Message);
               return false;
		         }else{		        	 
		        	 if(data.Result=="SUCCESS"){
		        		 callAlert(data.Message);
		        	 }
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
         error: function() {
				callAlert("Connection Failed.Please check the connection parameter");
			},
         success : function(data) {        
                if(data.Result=="Error"){
                      callAlert(data.Message);
                return false;
		         }else{		        	 
		        	 if(data.Result=="SUCCESS"){
		        		 callAlert(data.Message);
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
	 console.log("checkboxValues :"+checkboxValues);
	 $.ajax({
         type: "POST",
   		contentType: "application/json; charset=utf-8",
         url : 'test.import.testcases.rest?productId='+productId+'&tmsIdValues='+checkboxValues,
         dataType : 'json',
         success : function(data) {
                if(data.Result=="ERROR"){
                      callAlert(data.Message);
                      return false;
		         }else{
		        	 if(data.Result=="SUCCESS"){
		        		 callAlert("Import testCases Completed.");
		        		 $("#div_PopupExportResTestcase").modal("hide");
	        	 		testcases();
		        	 }else{
		        		 callAlert(data.Message);
		        		 $("#div_PopupExportResTestcase").modal("hide");
		        	 }
	         	}               
         }
  });
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

var idUnique;
var scriptsForUnique;
var scriptTypeUnique;
var testEngineUnique;
var testStepOption;
var scriptExecutionType;
var scriptTestCaseName = "TestCase";
var scriptTestSuiteName = "TestSuite";
var scriptViewName = "View";
var scriptDownloadName = "Download";
function displayDownloadTestScriptsFromTestCases(id, scriptsFor, type){		
	$("#divPopUpDownloadTestScriptsFromTestCases h4").text("");
	$("#divPopUpDownloadTestScriptsFromTestCases h4").text("Product");
	$("#divPopUpDownloadTestScriptsFromTestCases").modal();
	idUnique = id;
	scriptsForUnique = scriptsFor;
	scriptExecutionType = type;	
	scriptTypeUnique = $("#downloadScriptType_ul").find('option:selected').val();	
	testEngineUnique =  $("#downloadTestEngineType_ul").find('option:selected').val();
	testStepOption =  $("#downloadTestStepOption_ul").find('option:selected').val();		
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

function displayTestScriptsFromTestCases(){
    var jsonObj="";
    var code_testCaseClassName,code_testCaseScript,code_mainClassName,code_mainClassScript = "";
    
	scriptTypeUnique = $("#downloadScriptType_ul").find('option:selected').val();	
	testEngineUnique =  $("#downloadTestEngineType_ul").find('option:selected').val();
	testStepOption =  $("#downloadTestStepOption_ul").find('option:selected').val();
	
	var downloadURL;
	if(scriptsForUnique == scriptTestCaseName){
		downloadURL = "product.testcase.script.view";
		$("#codeEditorRadios  label").eq(0).text("Test Case")
	}else{
		downloadURL = "product.testsuite.script.ref.view";
		$("#codeEditorRadios  label").eq(0).text("Test Cases Ref")
	}
	
    var url= downloadURL+"?id="+idUnique+"&scriptsFor="+scriptsForUnique+"&scriptType="+scriptTypeUnique+"&testEngine="+testEngineUnique+"&testStepOptions="+testStepOption;
    $.ajax({
		    type: "POST",
		    url: url,
		    dataType: "json", // expected return value type		    
		    success: function(data) {		    	
		    },
		    error: function(data) {
		    },
		    complete: function(data){		    		
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
				         "SubTitle": "TestCase ID : "+idUnique+" - Language: "+scriptTypeUnique+" - Test Engine : "+testEngineUnique, 
		    			 "data":data,
		    			 "code_RadioBtn1Name":"TestCase ID : "+idUnique+" - Language: "+scriptTypeUnique+" - Test Engine : "+testEngineUnique,
		    			 "code_RadioBtn1":code_testCaseScript,
		    			 "code_RadioBtn2Name":"TestCase ID : "+idUnique+" - Language: "+scriptTypeUnique+" - Test Engine : "+testEngineUnique,
		    			 "code_RadioBtn2":code_mainClassScript,
		    			 "mode":"text/x-java",
		    			};		    
		    	CodeEditor.init(jsonObj); 	
		    }
		});    	
}


var testrunPlanIdDevice="";
var testrunPlanNameDevice="";
function downloadTestCaseScript(){
	console.log(idUnique," --- ",scriptsForUnique);	
	console.log(scriptTypeUnique," --- ",testEngineUnique," ----- ",testStepOption);
	
	location.href="product.testscripts.download?id="+idUnique+"&scriptsFor="+scriptsForUnique+"&scriptType="+scriptTypeUnique+"&testEngine="+testEngineUnique+"&testStepOptions="+testStepOption;
	popUpDownloadTestScriptsclose();
}

function downloadTemplate(){
	var mappingUrl = getContextCompletePath();
	var completeURL = mappingUrl+'/templates/iLCM_TESTCASE_IMPORT_TEMPLATE.xlsx';
	document.location.href = completeURL;
}

function listChangeRequestsOfSelectedProduct(){
	try{
		if ($('#jTableContainerChangeRequests').length>0) {
			 $('#jTableContainerChangeRequests').jtable('destroy'); 
		}
		}catch(e){}
		 $('#jTableContainerChangeRequests').jtable({
	         title: 'Change Requests',
	         selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: 10, //Set page size (default: 10)
	         editinline:{enable:true},	         
	         toolbar : {
			 },
	         actions: {
	             listAction: urlToGetChangeRequestsOfSpecifiedProductId ,
	             createAction: 'changerequests.add',
	             editinlineAction : 'changerequests.update',
	         },
	        fields: {
		        productId: { 
		   				type: 'hidden',  
		   				list:false,
		   				defaultValue: productId
		   				}, 
		   		changeRequestId: { 
		   				key: true,
		   				type: 'hidden',
		   				create: false, 
		   				edit: false, 
		   				list: false ,
		   				}, 
	   			changeRequestName: { 
		     	  		title: 'Change Request',
		     	  		inputTitle: 'Change Request <font color="#efd125" size="4px">*</font>',
		     	  		create: true, 
		   				edit: true, 
		     	  		list:true,
		     	  		width: "20%",
		  			 	},
	  			description: { 
		       			title: 'Description' ,
		       			type: 'textarea', 
		     		  	width: "20%",  
		     		  	create: true, 
		   				edit: true, 
		     	  		list:true
		    	 },	
				 ownerId : {
						title : 'Owner',
						list : true,
						create : true,
						edit : true,
						width : "20%",
						options : function(data) {
							return 'common.user.list.by.resourcepool.id';
						}
					},
				priorityId : {
					title : 'Priority',
					inputTitle : 'Priority <font color="#efd125" size="4px">*</font>',
					list : true,
					create : true,
					edit : true,
					width : "20%",
					options : 'administration.executionPriorityList'
				},
				statusId : {
					title : 'Status',
					create : false,
					list : true,
					edit : true,
					width : "20%",
					options : 'process.list.process.statuses'
				},
				isActive: { 
                   	title: 'IsActive',
                   	list:true,
       				edit:true,
    				create:false,
    				type : 'checkbox',
    				values: {'0' : 'No','1' : 'Yes'},
    		    	defaultValue: '1'
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
							listGenericAuditHistory(data.record.changeRequestId,"ChangeRequest","changeRequestAudit");
						});
						return $img;
					}
				},	
	        },	          	
	       //Moved Formcreate validation to Form Submitting
	         //Validate form when it is being submitted
	          formSubmitting: function (event, data) {
	        	  data.form.find('input[name="changeRequestName"]').addClass('validate[required]');
	              data.form.validationEngine();
	             return data.form.validationEngine('validate');
	         }, 
	          //Dispose validation logic when form is closed
	          formClosed: function (event, data) {
	             data.form.validationEngine('hide');
	             data.form.validationEngine('detach');
	         }
	     });		 
	$('#jTableContainerChangeRequests').jtable('load');		 
}

function executeEditTestrunplan(){
   // var testRunPlanName= data.record.testRunPlanName;
		document.getElementById("hdnTestRunPlanName").value=testRunPlanName;
		//var testRunPlanIdNameStr = testrunplanId+"~"+testRunPlanName;
		executeEditTrplan = "ExecuteEditTestRunPlan";
	testRunPlanForBDD(testrunplanId,null);
	
}

//Environment for Test Plan - Test Plan Tile View Starts
$(document).on('change','#envCombinationstatus_ul', function() {
	var envComstatus = $("#envCombinationstatus_ul").find('option:selected').val();				
	 urlToGetEnvironmentCombinationByProduct = "environment.combination.list.of.selectedProduct?productVersionId=-1&productId="+productId+"&workpackageId=-1&testRunPlanId=-1&envComstatus="+envComstatus;
	 listEnvCombinationByProduct(); 
 });

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

function showTestSuiteGrp(obj){
	if(obj == 1){
		$("#createts").show();
		$("#testsuiteTestRunPlan").hide();
	}else{
		$("#createts").hide();
		$("#testsuiteTestRunPlan").show();
	}
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

var workPackageDTFlag=false;
var workPackage_oTable='';
var wpTestRunJob_oTable='';
var wpTestRuncase_oTable='';
var wkpResults_oTable='';

function workPackage_Container(data, scrollYValue){
	try{
		if ($('#workPackage_dataTable').length>0) {
			$('#workPackage_dataTable').dataTable().fnDestroy(); 
		}
	} catch(e) {}
	
	workPackage_oTable = $('#workPackage_dataTable').dataTable( {
		"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
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
           leftColumns: 3,
           rightColumns: 1
       }, 
       "fnInitComplete": function(data) {
    	   var searchcolumnVisibleIndex = [2,18,19]; // search column TextBox Invisible Column position
    	   if(!workPackageDTFlag){
    		   $('#workPackage_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
    	   }
    	   workPackageDTFlag=true;
		   reInitializeDataTableWKP();
	   },  
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'WorkPackages',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'WorkPackages',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'WorkPackages',
	                    	exportOptions: {
	                            columns: ':visible'
	                        },
	                        orientation: 'landscape',
	                        pageSize: 'LEGAL'
	                    },	                    
	                ],	                
	            },
	            'colvis'
         ], 
         columnDefs: [
              { targets: [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18], visible: true},
              { targets: '_all', visible: false }
          ],
                  
         aaData:data,                 
	    aoColumns: [	        
	       { mData: "workPackageId", className: 'disableEditInline' , sWidth: '5%'},	        
	       { mData: "workPackageName", className: 'disableEditInline' , sWidth: '6%'},	        
           { mData: "productName", className: 'disableEditInline' , sWidth: '6%'},		    				            
           { mData: "exeType", className: 'disableEditInline' , sWidth: '5%'},		
           { mData: "wpStatus", className: 'disableEditInline' , sWidth: '5%'},		
           { mData: "result", className: 'disableEditInline' , sWidth: '5%'},	
           { mData: "firstActualExecutionDate", className: 'disableEditInline' , sWidth: '5%'},		
           { mData: "lastActualExecutionDate", className: 'disableEditInline' , sWidth: '5%'},	
           { mData: "jobsCount", className: 'disableEditInline' , sWidth: '4%'},
           { mData: "p2totalPass", className: 'disableEditInline' , sWidth: '4%'},
           { mData: "p2totalFail", className: 'disableEditInline' , sWidth: '4%'},
           { mData: "p2totalBlock", className: 'disableEditInline' , sWidth: '4%'},   
           { mData: "p2totalNoRun", className: 'disableEditInline' , sWidth: '4%'},
           { mData: "notExecuted", className: 'disableEditInline' , sWidth: '4%'},
           { mData: "totalExecutedTesCases", className: 'disableEditInline' , sWidth: '5%'},
           { mData: "testCaseCountOfRunconfig", className: 'disableEditInline' , sWidth: '5%'},
           { mData: "teststepcount", className: 'disableEditInline' , sWidth: '5%'},
           { mData: "defectsCount", className: 'disableEditInline' , sWidth: '5%'},
           { 
	    	   mData: '',				 
	    	   sWidth: '15%',
			   "orderable":      false,
			   "data":           data,
           	   mRender: function(data, type, full) {				            	
	       		var img = ('<div style="display: flex;">'+
	       		'<button style="border: none; background-color: transparent; outline: none;">'+
	       				'<i class="fa fa-search-plus wp-details-control" title="Audit History"></i></button>'+
	       		'<button style="border: none; background-color: transparent; outline: none;">'+
	       				'<i class="fa fa-comments wp-details-control2" title="Comments"></i></button>'+
	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
	       				'<img src="css/images/list_metro.png" class="wp-details-control3" title="Test Job"></button>'+
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
	    
		 workPackage_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
	    } );
	   
		/* Use the elements to store their own index */
	   $("#workPackage_dataTable_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input").each( function (i) {
			this.visibleIndex = i;
		} );
		
	 	$("#workPackage_dataTable_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input").keyup( function () {
			/* If there is no visible index then we are in the cloned node */
			var visIndex = typeof this.visibleIndex == 'undefined' ? 0 : this.visibleIndex;
			
			/* Filter on the column (the index) of this element */
			workPackage_oTable.fnFilter( this.value, visIndex );
		} );
	    
	    // ----- test step child table -----
	    $('#workPackage_dataTable tbody').on('click', 'td button .wp-details-control', function () {
	    	var tr = $(this).closest('tr');
	    	var row = workPackage_oTable.DataTable().row(tr);
	    	wpAuditHistoryHandler(row, tr);
   		});	    
		
	    // ----- comments child table -----
	    $('#workPackage_dataTable tbody').on('click', 'td button .wp-details-control2', function () {
	    	var tr = $(this).closest('tr');
	    	var row = workPackage_oTable.DataTable().row(tr);
	    	//wpAuditHistoryHandler(row, tr);
				var entityTypeIdComments = 2;
				var entityNameComments = "Workpackage";
				listComments(entityTypeIdComments, entityNameComments, row.data().workPackageId, row.data().workPackageName, "workPackageComments");
			
   		});
		
	    // ----- export child table -----
	    $('#workPackage_dataTable tbody').on('click', 'td button .wp-details-control3', function () {
	    	var tr = $(this).closest('tr');
	    	var row = workPackage_oTable.DataTable().row(tr);
	    	wpTestRunJobHandler(row, tr);    		
	    	
   		});	  
	    
	    $("#workPackage_dataTable_length").css('margin-top','8px');
		$("#workPackage_dataTable_length").css('padding-left','35px');
	} ); 
}

var clearTimeoutDTWk='';
function reInitializeDataTableWKP(){	
	clearTimeoutDTWk =setTimeout(function(){				
		workPackage_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTWk);
	},200);
}

function workPackageDTFullScreenHandler(flag){
	reInitializeDataTableWKP();
	
	if(flag){
		$("#workPackage_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
	}else{
		$("#workPackage_dataTable_wrapper .dataTables_scrollBody").css('max-height','400px');
	}	
}

function wpAuditHistoryHandler(row, tr){
	var url='administration.event.list?sourceEntityType=WorkPackage&sourceEntityId='+row.data().workPackageId+'&jtStartIndex=0&jtPageSize=1000';
	var jsonObj={"Title":"WorkPackage Audit History:",
			"url": url,	 
			"jtStartIndex":0,
			"jtPageSize":1000,
			"componentUsageTitle":"workPackageAudit",
	};
	SingleDataTableContainer.init(jsonObj);
}

function wpTestRunJobHandler(row, tr){
	workpackageId = row.data().workPackageId;
	var url = 'workpackage.testcase.execution.summary.build.list?workPackageId='+row.data().workPackageId+'&productBuildId='+productVersionId+'&jtStartIndex=0&jtPageSize=1000';
	assignWPDataTableValues(url, "childTable2", row, tr);
	
	$("#wpTestRunJobContainer").modal();
}

function wpChild2Table(){
	var childDivString = '<table id="wpTestRunJobs_dataTable"  class="cell-border compact" cellspacing="0" width="100%">'+
	'<caption class="dataTableChildHeaderTitle" >Test Job:</caption>'+
	'<thead>'+
		'<tr>'+
			'<th class="dataTableChildHeaderTitleTH">Id</th>'+
			'<th class="dataTableChildHeaderTitleTH">Status</th>'+
			'<th class="dataTableChildHeaderTitleTH">Result</th>'+
			'<th class="dataTableChildHeaderTitleTH">Comments</th>'+
			'<th class="dataTableChildHeaderTitleTH">Evidence</th>'+
			'<th class="dataTableChildHeaderTitleTH">Passed</th>'+
			'<th class="dataTableChildHeaderTitleTH">Failed</th>'+
			'<th class="dataTableChildHeaderTitleTH">Blocked</th>'+
			'<th class="dataTableChildHeaderTitleTH">Not Run</th>'+
			'<th class="dataTableChildHeaderTitleTH">Not executed</th>'+
			'<th class="dataTableChildHeaderTitleTH">Total TC Executed</th>'+
			'<th class="dataTableChildHeaderTitleTH">Total TC</th>'+
			'<th class="dataTableChildHeaderTitleTH">Test Steps</th>'+
			'<th class="dataTableChildHeaderTitleTH">Defects</th>'+
			'<th class="dataTableChildHeaderTitleTH">Test Cases</th>'+			
		'</tr>'+
	'</thead>'+
	'</table>';		
	
	return childDivString;	
}

function wpTestRunJobs_Container(data, row, tr){
try{
	if ($("#wpTestRunJobDTContainer").children().length>0) {
		$("#wpTestRunJobDTContainer").children().remove();
	}
} 
catch(e) {}

var childDivString = wpChild2Table(); 			 
$("#wpTestRunJobDTContainer").append(childDivString);

wpTestRunJob_oTable = $("#wpTestRunJobs_dataTable").dataTable( {
		"dom": '<"top"Bf<"clear">>rt<"bottom"ip<"clear">>',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		autoWidth: false,
		bAutoWidth:false,
		"sScrollX": "100%",
       "sScrollXInner": "100%",
       "scrollY":"100%",
       fixedColumns:   {
           leftColumns: 2,
       },
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'Test Jobs',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Test Jobs',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Test Jobs',
	                    	exportOptions: {
	                            columns: ':visible'
	                        },
	                        orientation: 'landscape',
	                        pageSize: 'LEGAL'
	                    },	                    
	                ],	                
	            },
	            'colvis'
         ], 
         columnDefs: [
          ],      			        
    aaData:data,		    				 
    aoColumns: [	        	        
        { mData: "testRunJobId", className: 'disableEditInline' , sWidth: '7%' },		
        { mData: "testRunStatusName", className: 'disableEditInline' , sWidth: '7%' },		
        { mData: "result", className: 'disableEditInline' , sWidth: '7%' },
        { mData: "testRunFailureMessage", className: 'disableEditInline' , sWidth: '7%' },
        { mData: "testRunEvidenceMessage", className: 'disableEditInline' , sWidth: '7%' },
        { mData: "passedCount" , className: 'disableEditInline' , sWidth: '5%'},
        { mData: "failedCount", className: 'disableEditInline' , sWidth: '5%'},
        { mData: "blockedCount", className: 'disableEditInline', sWidth: '5%' },
        { mData: "norunCount", className: 'disableEditInline' , sWidth: '5%'},
        { mData: "notexecutedCount", className: 'disableEditInline', sWidth: '5%' },
        { mData: "totalTestCaseForExecutionCount", className: 'disableEditInline', sWidth: '5%' },
        { mData: "totalTestCaseCount", className: 'disableEditInline', sWidth: '5%' },
        { mData: "teststepcount", className: 'disableEditInline', sWidth: '5%' },
        { mData: "defectsCount", className: 'disableEditInline', sWidth: '5%' },
        { 
	    	   mData: '',				 
	    	   sWidth: '6%',
			   "orderable":      false,
			   "data":           data,
        	   mRender: function(data, type, full) {				            	
	       		var img = ('<div style="display: flex;">'+
	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
	       				'<img src="css/images/list_metro.png" class="wp-details-control3" title="Test Run cases"></button>'+
	       		'<button style="border: none; background-color: transparent; outline: none;">'+
	       				'<i class="fa fa-comments wp-details-control4" title="Comments"></i></button>'+
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

$(function(){

	//----- testcases  child table -----
	$('#wpTestRunJobs_dataTable tbody').on('click', 'td button .wp-details-control3', function () {
		var tr = $(this).closest('tr');
		var row = wpTestRunJob_oTable.DataTable().row(tr);
		wpTestRunCaseHandler(row, tr);
		});
		
	   $('#wpTestRunJobs_dataTable tbody').on('click', 'td button .wp-details-control4', function () {
	   	var tr = $(this).closest('tr');
	   	var row = wpTestRunJob_oTable.DataTable().row(tr);
	   	//wpAuditHistoryHandler(row, tr);
			var entityTypeIdComments = 10;
			var entityNameComments = "TestRunJob";
			listComments(entityTypeIdComments, entityNameComments, row.data().testRunJobId, row.data().testRunStatusName, "testRunJobComments");			
   	});
  });
}

function wpTestRunCaseHandler(row, tr){
	var url = 'testcasesexecution.of.testrunjob.Id?testRunJobId='+row.data().testRunJobId+'&jtStartIndex=0&jtPageSize=1000';
	assignWPDataTableValues(url, "childTable3", row, tr);
	$("#wpTestRunCaseContainer").modal();	
}

function wpTestRunCases_Container(data, row, tr){
	wpkResults_Container(data, "350px");
}

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

function tsChild1Table(){
		var childDivString = '<table id="tsResults_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead>'+
			'<tr>'+
				'<th class="dataTableChildHeaderTitleTH">Test Step Id</th>'+
				'<th class="dataTableChildHeaderTitleTH">TestStep Execution Id</th>'+
				'<th class="dataTableChildHeaderTitleTH">Test Step Name</th>'+
				'<th class="dataTableChildHeaderTitleTH">TestCase Code</th>'+
				'<th class="dataTableChildHeaderTitleTH">Description</th>'+
				'<th class="dataTableChildHeaderTitleTH">Expected Output</th>'+
				'<th class="dataTableChildHeaderTitleTH">Observed Output</th>'+
				'<th class="dataTableChildHeaderTitleTH">Status</th>'+
				'<th class="dataTableChildHeaderTitleTH">Start Time</th>'+		
				'<th class="dataTableChildHeaderTitleTH">End Time</th>'+
				'<th class="dataTableChildHeaderTitleTH">Remarks</th>'+	      	
			'</tr>'+
		'</thead>'+
		'</table>';		
		
		return childDivString;	
}

function tsResults_Container(data, row, tr){	
	try{
		if ($("#testCaseTestStepsDTContainer").children().length>0) {
			$("#testCaseTestStepsDTContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = tsChild1Table(); 			 
	$("#testCaseTestStepsDTContainer").append(childDivString);
	
	 tsResults_oTable = $("#tsResults_dataTable").dataTable( {  		   
		 	"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
			autoWidth: false,
			bAutoWidth:false,
			"sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       /*fixedColumns:   {
	           leftColumns: 3,
	       }, */
	       "fnInitComplete": function(data) {    	   	  
		   },  
		   buttons: [
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Test Steps',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Test Steps',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Test Steps',
		                    	exportOptions: {
		                            columns: ':visible'
		                        },
		                        orientation: 'landscape',
		                        pageSize: 'LEGAL'
		                    },	                    
		                ],	                
		            },
		            'colvis'
	         ], 
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "testStepId" ,className: 'disableEditInline'},		
           { mData: "teststepexecutionresultId" ,className: 'disableEditInline'},		
           { mData: "testStepName" ,className: 'disableEditInline'},		
           { mData: "testStepCode" ,className: 'disableEditInline'},		
           { mData: "testStepDescription" ,className: 'disableEditInline'},	
           { mData: "testStepExpectedOutput" ,className: 'disableEditInline'},
           { mData: "testStepObservedOutput" ,className: 'disableEditInline'},
           { mData: "testResultStatus" ,className: 'disableEditInline'},
           { mData: "startTime" ,className: 'disableEditInline'},
           { mData: "endTime" ,className: 'disableEditInline'},
           { mData: "executionRemarks" ,className: 'disableEditInline'},
       ],
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	 
	 $(function(){ // this will be called when the DOM is ready
			
		$("#tsResults_dataTable_length").css('margin-top','8px');
		$("#tsResults_dataTable_length").css('padding-left','35px');
	});
}

function tcDefectsResultHandler(row, tr){
	var url ='export.name.code.list?testCaseExecutionResultId='+row.data().testCaseExecutionResultId+'&jtStartIndex=0&jtPageSize=50';
	assignDataTableValues(url, "childTable2", row, tr);	
	$("#testCaseExportDetailsContainer").modal();
}

function tsChild2Table(){
		var childDivString = '<table id="tcDefectsResults_dataTable"  class="cell-border compact" cellspacing="0" width="100%">'+
		'<caption class="dataTableChildHeaderTitle" >Export System Details:</caption>'+
		'<thead>'+
			'<tr>'+
				'<th class="dataTableChildHeaderTitleTH">Test Management System</th>'+
				'<th class="dataTableChildHeaderTitleTH">Export System Result Id</th>'+
				'<th class="dataTableChildHeaderTitleTH">Exported Date</th>'+
			'</tr>'+
		'</thead>'+
		'</table>';		
		
		return childDivString;	
}

function tcDefectsResults_Container(data, row, tr){
	try{
		if ($("#testCaseExportDetailsDTContainer").children().length>0) {
			$("#testCaseExportDetailsDTContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = tsChild2Table(); 			 
	$("#testCaseExportDetailsDTContainer").append(childDivString);
	
	tcDefecResults_oTable = $("#tcDefectsResults_dataTable").dataTable( {
  		   //"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
			"dom": '<"top"Bf<"clear">>rt<"bottom"ip<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
			autoWidth: false,
			bAutoWidth:false,
			"sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       /*fixedColumns:   {
	           leftColumns: 3,
	           rightColumns: 1
	       },*/ 
	       "fnInitComplete": function(data) {
		   },  
		   buttons: [
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Export System Details',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Export System Details',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Export System Details',
		                    	exportOptions: {
		                            columns: ':visible'
		                        },
		                        orientation: 'landscape',
		                        pageSize: 'LEGAL'
		                    },	                    
		                ],	                
		            },
		            'colvis'
	         ], 
	         columnDefs: [
	          ],      			        
        aaData:data,		    				 
	    aoColumns: [	        	        
            { mData: "testSystemName" ,className: 'disableEditInline'},		
            { mData: "resultCode" ,className: 'disableEditInline'},		
            { mData: "exportedDate" ,className: 'disableEditInline'},
       ],
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },      
	});
	
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

//environment plan starts
var maptarget_terminal_device_title='';
var listActionUrlpart='';
var terminal_device_viewtitle='';
var terminal_device_target_viewtitle='';

function listEnvCombinationByProductPlan(productId,productVersionId){
	//var productId = document.getElementById("hdnProductId").value;
	var listActionUrlpart = '';
	productTypehidden = document.getElementById("hdnproductType").value;
		if(productTypehidden == 1 || productTypehidden == 5){
		terminal_device_target_viewtitle = 'Devices';
		terminal_device_viewtitle = 'View Mapped Devices';
		maptarget_terminal_device_title = 'Map Target Devices';
		listActionUrlpart ='administration.genericdevice.mappedList';
	}else if(productTypehidden == 2 || productTypehidden == 3 || productTypehidden == 4){
		terminal_device_target_viewtitle = 'Terminals';
		terminal_device_viewtitle = 'View Mapped Terminals';
		maptarget_terminal_device_title = 'Map Target Terminals';
		listActionUrlpart ='administration.runConfiguration.listbyproductversion';
	}else if(productTypehidden == 6){
		terminal_device_target_viewtitle = 'Terminals/Device';
		terminal_device_viewtitle = 'View Mapped Terminals/Devices';
		maptarget_terminal_device_title = 'Map Target Terminals/Devices';
	}

	var testRunPlanId='';
	if(document.getElementById("hdnTestRunPlanId").value != undefined && document.getElementById("hdnTestRunPlanId").value != ""){
		testRunPlanId = document.getElementById("hdnTestRunPlanId").value;
	} 
	
	if(testrunPlanId != undefined && testrunPlanId != null && testrunPlanId != "") {
		testRunPlanId=testrunPlanId;
	}
		
	urlToGetEnvironmentCombinationByProduct = "environment.combination.list.byProductId?productVersionId="+productVersionId+"&productId=-1&workpackageId=-1&testRunPlanId="+testRunPlanId;
	try{
		if ($('#environmentCombinationPlan').length>0) {
			 $('#environmentCombinationPlan').jtable('destroy'); 
		}
		}catch(e){}
	$('#environmentCombinationPlan').jtable({
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
	           				$('#environmentCombinationPlan').jtable('openChildTable', 
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
	           	 	}else if(environmentData.record.productType==2 || environmentData.record.productType==3 || environmentData.record.productType==4){
		           	 	$img.click(function (event) { 
	           				$('#environmentCombinationPlan').jtable('openChildTable', 
	           				$img.closest('tr'), 
	           					{ 
	           					title: 'View Target Terminals', 
	           					 editinline:{enable:false},
	           					 paging: true, //Enable paging
	           			        pageSize: 10, 
								 
	           					recordsLoaded: function(event, data) {
	           		        	 $(".jtable-edit-command-button").prop("disabled", true);
	           		         },
	           					actions: { 
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
           				$('#environmentCombinationPlan').jtable('openChildTable', 
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
			           			//Open child table when user clicks the image 
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
	$('#environmentCombinationPlan').jtable('load'); 
	var jscrolheight = $("#environmentCombinationPlan").height();
	var jscrolwidth = $("#environmentCombinationPlan").width();
	  
	$(".jScrollContainer").on("scroll", function() {
		 var lastScrollLeft=0;	
		 var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
		 if (lastScrollLeft < documentScrollLeft) {
		    	$("#environmentCombinationPlan").width($(".jtable").width()).height($(".jtable").height());			
		        lastScrollLeft = documentScrollLeft;
		  }else if(lastScrollLeft >= documentScrollLeft){			
		    	$("#environmentCombinationPlan").width(jscrolwidth).height(jscrolheight);
		  }			
	});
	try{
		if ($('#jTableContainerenvironmentCombinationPlan').length>0) {
			 $('#jTableContainerenvironmentCombinationPlan').jtable('destroy'); 
		}
	}catch(e){}
	var environmentCombinationPlanContent = $("#environmentCombinationPlan").contents();
	if($('#showTestBedSummary').css('display') == "none")
	$("#jTableContainerenvironmentCombinationPlan").html(environmentCombinationPlanContent);
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
	var versionId= productVersionId;
	var workpackageId=-1;
	var testRunPlanId=testrunPlanId;
	var url="administration.host.list.mapping?jtStartIndex=-1&jtPageSize=-1&ecId="+ecId+"&workpackageId=-1&testRunPlanId="+testRunPlanId+"&filter=-1";
	
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {			
			var result = data.Records;			
			if(result.length==0){
				$("#allterminals").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>No Terminals</b></span>");
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
				$("#terminalsmapped").append("<span style='color: black;' id='emptyListMapped' disabled><b style='margin-left: 101px;'>No Terminals</b></span>");
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
		 		$("#allterminals").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 101px;'>No Terminals</b></span>");
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
	 var testRunPlanId=testrunPlanId;
	 refreshRunConfigurationMappedList(productId, productVersionId, envionmentCombinationId, testRunPlanId);	
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
//Test Beds Distribution - ends

function loadPopupTestRunJobRes(urlId){
	var urlfinal="rest/download/evidence?fileName="+urlId;
  	parent.window.location.href=urlfinal;  	
}

function snapshotStatusCloseHandler(){
	$('#snapshotStatus').hide();
}

function displayATSGPopupHandler(value){
	totalRepository  = 0;
	totalTestData = 0;
	editStoryFalg = false;
	var arr = value.split('~');
	testCaseId = arr[0];
	testCaseName = arr[1];
	idUnique = testCaseId;
 	var productTypeName = arr[2];
	productType=productTypeName;
	var jsonObj={"Title":"ATSG",	
			"subTitle": scriptTypeTile +" : [ "+testCaseId+" ] "+testCaseName,
			"testCaseId": testCaseId,
			"testCaseName": testCaseName,
			"productTypeName": productTypeName,
			
	};	 
	ATSGComponent.init(jsonObj);
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
        	var subTitleSchedule = "Product "+$(headerTitle).text();      	
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
			var jobResult = "";
			if(data.Records[i].result != null || data.Records[i].result != undefined ){
				jobResult = data.Records[i].result;
			}else{
				jobResult = "---";
			}

			var isReport;
			var entityTypeIdComments = 10;
			var entityNameComments = "TestRunJob";
			var testRunJobComments = "testRunJobComments";
			isReport = '<span style="float: right;padding: 0px 0px;"><img width="13px" height="13px" class="exe" src="css/images/list_metro.png" style="color:black;cursor:pointer;margin-right:0px;margin-top:2px;" title="Test Case" onclick="jobTileTestRunCaseHandler(\''+data.Records[i].testRunJobId+'\',null)"></img></span><br/>'+	
    					'<span style="float: right;padding: 0px 2px;"><i class="fa fa-comments" style="color:black;cursor:pointer" title="Comments" onclick="listComments(\''+entityTypeIdComments+'\',\''+entityNameComments+'\',\''+data.Records[i].testRunJobId+'\',\''+data.Records[i].testRunStatusName+'\',\''+testRunJobComments+'\')"></i></span><br/>';
			if((passPercentage != 'NaN' && passPercentage != 0 )|| (failedPercentage != 'NaN' && failedPercentage != 0)){
				isReport += /*'<span style="float: right;padding: 0px 2px;"><img width="13px" height="13px" class="exe" src="css/images/pdf_type1.png" style="color:black;cursor:pointer;margin-right:0px;margin-top:2px;"  title="Job Evidence PDF Report" onclick="exportWPResultsTestRunJob(\''+data.Records[i].testRunJobId+'\')"></img></span><br/>'+*/
				'<span style="color:black;"><i class="fa fa-desktop" title="Job Evidence HTML Report"  style="cursor:pointer;font-size:15px;padding-top:5px;margin-left:3px;" onclick="exportWPResultshtmlEvidenceTestRunJob('+data.Records[i].testRunJobId+');"/></span><br/>';
			}
			
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
    	    	'<div style="float:right;">'+ isReport +  	    		
    	    '</div>'+
    	    '<div class="row" style="text-align:left;margin-top:75px;overflow:hidden;">'+
    	    '<span title="Job Id" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >'+ "Job Id : "+data.Records[i].testRunJobId+'<br /> </span>'+
    	     startDate+    	    
    	    '<span title="'+data.Records[i].testRunStatusName+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;" >Status : '+ data.Records[i].testRunStatusName +'<br /> </span>'+
    	    isResultBadge +
    	    '<span title="'+jobResult+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;word-wrap:break-word;display:block;" >Total Test Case : '+ data.Records[i].totalTestCaseCount +'<br /> </span>'+
			'<span id="passedTestCases" class="badge badge-default" style="background: #076;margin-top: 10px;margin-bottom: 10px;display: inline;margin-right: 5px;">'+data.Records[i].passedCount+' ['+passPercentage+'%]</span>'+
			'<span id="failedTestCases" class="badge badge-default" style="background: rgb(231, 80, 90);margin-top: 10px;margin-bottom: 10px;display: inline;">'+data.Records[i].failedCount+' ['+failedPercentage+'%]</span>'+
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

function workPackageHTMLReportFromGrid(workPackageId) {
	var vURL = "report.run.evidence.html?testRunNo=" + workPackageId
			+ "&testRunConfigurationChildId=-1&deviceId=&testRunJobId=-1"
			+ "&reportType=No&viewType=HTML";
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

function getPlanHandlerTestRunPlanTab(productId,productName,testRunPlanName,productBuildId,productBuildName){	
	var url='getISERecommended.Testcases.by.buildId?buildId='+productBuildId;
	var title = "Product Name:"+productName+" - Test Plan Name:"+testRunPlanName+" - Build Name:"+productBuildName+" - Recommended Test Cases";
	var jsonObj={"Title":title,		
			"listURL": url,					
			"componentUsageTitle":"recommendedTestPlan",
			"productId": productId,
			"buildId" : productBuildId,
			"componentUsageTitle": "ProductTestRunPlan"
	};
	RecommentedTestPlan.init(jsonObj);
	$("#recommendedTestPlanPdMgmContainer").modal();
}

function listTRPExecutionHistory(testRunPlanId){
	if(nodeType == "TestFactory"){
		callAlert("Please select Product or Product Version");
		return false;
	} else if(nodeType == "Product"){
		productVersionId = -1;
		url="workpackage.executiondetails.testrunplan.list?testRunPlanId="+testRunPlanId+"&jtStartIndex=-1&jtPageSize=-1";	
		assignWPDataTableValues(url, "parentTable", "", "");
	} else if(nodeType == "ProductVersion"){
		productId = -1;
		url="workpackage.executiondetails.testrunplan.list?testRunPlanId="+testRunPlanId+"&jtStartIndex=-1&jtPageSize=-1";	
		assignWPDataTableValues(url, "parentTable", "", "");
	}
	$("#showWorkpackageContainer").modal();
}
