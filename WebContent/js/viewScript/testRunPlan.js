var TestRunPlan = function(){	
	var initialise = function(jsonObj){
		initialiseTestRunPlan(jsonObj);
	};	
		return {
			init : function(jsonObj){
				initialise(jsonObj);
			}
		};
}();

var jsonObjTestRunPlan='';
function initialiseTestRunPlan(jsonObj){
	jsonObjTestRunPlan = jsonObj;
	
	$("#testRunPlanNameLinkContainer .modal-header h4").text("");
	$("#testRunPlanNameLinkContainer .modal-header h4").text(jsonObj.Title);
	
	if(jsonObjTestRunPlan.productId == null || jsonObjTestRunPlan.productId == undefined){
		jsonObjTestRunPlan.productId=0;
	}
	
	if(jsonObjTestRunPlan.productVersionId == null || jsonObjTestRunPlan.productVersionId == undefined){
		jsonObjTestRunPlan.productVersionId=0;
	}
	
	loadVersions();
	loadExecutionTypes();
	loadProductBuilds();
	loadTestToolMaster();
	//testrunplangroup();	
	loadMultipleTestSuites();
	loadTestScriptsLevel();
	loadScriptTypeMaster();
	loadAutomationMode();
	loadResultsReportingMode();
	loadRunConfiguration(jsonObj.testRunPlanId);
	
	$(".button-edit, .button-next").hide();
	initializeBootstrapWizard();
	document.getElementById("hdnProductName").value=jsonObj.productName;
	if(jsonObj.mode == "edit"){	
		var urlToGetTestRunPlanOfSpecifiedProductVersionId = 'testrunplan.list?testRunPlanId='+jsonObj.testRunPlanId;	
		testRunPlanSummary(urlToGetTestRunPlanOfSpecifiedProductVersionId);	
		listTestRunPlanBytestRunId(urlToGetTestRunPlanOfSpecifiedProductVersionId);	
		//testRunPlanTestBeds();		
		$('#form_wizard_1').bootstrapWizard('first');
		
		var firstTab = $("#form_wizard_1").find("#TestRunPlan");
		if(!(firstTab.hasClass("active in"))){
			firstTab.addClass("active in");
			firstTab.siblings(".tab-pane").removeClass("active in");
		}
		$('#form_wizard_1').find('.button-previous').hide();  
	}else{
		$('#form_wizard_1').bootstrapWizard('first');		
		//$(".button-submit").show();
	}    
    $(".button-previous").show();
	$('#form_wizard_1').find('.button-submit').show();
	
    $('#readyToExecuteId').text(isReady);
    $('#detailsId').html(isReadyMessage);
    if(isReady == "Yes"){
    	$('#testRunPlanNameLinkContainer .portlet-title').css('background-color','#3FABA4 !important');
    }else if(isReady == "No"){
    	$('#testRunPlanNameLinkContainer .portlet-title').css('background-color','#D05454 !important');
    }else{
    	$('#testRunPlanNameLinkContainer .portlet-title').css('background-color','#55616f !important');
    }
    $("#tpRunConfig").closest('tr').hide();
	$("#testRunPlanNameLinkContainer").modal();
	closeLoaderIcon();
}

function loadVersions(){
	$('#version_ul').empty();
	$.post('common.list.productversion?productId='+jsonObjTestRunPlan.productId,function(data) {	
		var ary = (data.Options);
		$.each(ary, function(index, element) {
			$('#version_ul').append('<option id="' + element.Value + '" value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
		$('#version_ul').select2();
		loadPdtBuild();
	});
}

function loadExecutionTypes(){
	$('#executionType_ul').empty();	
	$.post('common.list.executiontypemaster.byentityid?entitymasterid=7',function(data) {	
		var ary = (data.Options);
	    $.each(ary, function(index, element) {
			$('#executionType_ul').append('<option id="' + element.Value + '" value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
    	});
	    $('#executionType_ul').select2();
	});
}

function loadProductBuilds(){
	$('#productBuild_ul').empty();	
	$.post('common.listProductBuild.byProductVersionid.option?productVersionListId='+jsonObjTestRunPlan.productVersionId,function(data) {	
		var ary = (data.Options);
	    $.each(ary, function(index, element) {
			$('#productBuild_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
	    $('#productBuild_ul').select2();
	});
}

function loadTestToolMaster(){
	$('#testTollMaster_ul').empty();
	$.post('common.list.testToolMaster.option',function(data) {	
		var ary = (data.Options);
		$.each(ary, function(index, element) {
			$('#testTollMaster_ul').append('<option id="' + element.Value + '"  value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
		$('#testTollMaster_ul').select2();
	});
}

function loadMultipleTestSuites(){
	$('#multipleTestSuites_ul').empty();
	$.post('common.multiple.testSuites.options',function(data) {	
		var ary = (data.Options);
		$.each(ary, function(index, element) {
			$('#multipleTestSuites_ul').append('<option id="' + element.Value + '"  value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
		$('#multipleTestSuites_ul').select2();
	});
}

function loadTestScriptsLevel(){
	$('#testScriptsLevel_ul').empty();
	$.post('common.multiple.testScriptsLevel.options',function(data) {	
		var ary = (data.Options);
	    $.each(ary, function(index, element) {
			$('#testScriptsLevel_ul').append('<option id="' + element.Value + '"  value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
	   $('#testScriptsLevel_ul').select2();
	});
}
function loadAutomationMode(){
	$('#automationMode_ul').empty();
	$.post('common.automationmode.options',function(data) {	
		var ary = (data.Options);
	    $.each(ary, function(index, element) {
			$('#automationMode_ul').append('<option id="' + element.Value + '"  value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
	   $('#automationMode_ul').select2();
	});
}


function loadPdtBuild(){
	$('#pdtBuild_ul').empty();
	var selectedVersion = $('#version_ul').find('option:selected').attr('id');
	
	$.post('common.list.productbuild?productId='+productId+'&productVersionListId='+selectedVersion,function(data) {	
		var ary = (data.Options);
		$.each(ary, function(index, element) {
			$('#pdtBuild_ul').append('<option id="' + element.Value + '" value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
		$('#pdtBuild_ul').select2();
	});
}

function loadRunConfiguration(testPlanId){
	$('#tpRunConfig').empty();
	$.post('common.list.testplan.runconfiguration?testPlanId='+testPlanId,function(data) {	
		var ary = (data.Options);
		$.each(ary, function(index, element) {
			$('#tpRunConfig').append('<option id="' + element.Value + '" value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
		$('#tpRunConfig').select2();
	});
}

var testRunPlanId;
var entityConfigPropertiesMasterId;
var entityMastrId;
var entityPropsVal;
var runConfigurationId;
function loadPropertyValues(entityConfigPropertiesMasterId){
	if(entityConfigPropertiesMasterId==undefined){
		callAlert("Please Select Property");
		return false;
	}
	if(rnCnfgFlag){
		entityMastrId = 27;
	}
	$('#propertyValue_ul').empty();
	$.post('common.listEntityConfigPropsMasterByentityConfigPropsMasterId?entityConfigPropertiesMasterId='+entityConfigPropertiesMasterId+'&entityMasterId='+entityMastrId,function(data) {	
		if(data.Result=="ERROR"){
			callAlert(data.Message);
    		return false;
    		}else{
		        var ary = (data.Options);
		        $.each(ary, function(index, element) {
		        	$('#propertyValue_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#propertyValue_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
		       });
		       $('#propertyValue_ul').select2();
		   		entityConfigPropertiesMasterId= $("#property_ul").find('option:selected').attr('id');
			   	 if(entityConfigPropertiesMasterId!=undefined && (entityConfigPropertiesMasterId==1 || entityConfigPropertiesMasterId==3)){
					document.getElementById("propertyValue_dd").style.display="block";
			    	document.getElementById("dataPropertyValueDiv").style.display="none";
			    	document.getElementById("trpProp").style.display="none";
			    	//loadPropertyValues(entityConfigPropertiesMasterId); 
				 }else if(entityConfigPropertiesMasterId!=undefined && (entityConfigPropertiesMasterId==2 || entityConfigPropertiesMasterId==4)){
			    	document.getElementById("propertyValue_dd").style.display="none";
			    	document.getElementById("dataPropertyValueDiv").style.display="block";
			    	document.getElementById("trpProp").style.display="none";
			    	$('#dataPropertyValue').val('');
			    	$('#propertyTrp').val('');
			    } else if(entityConfigPropertiesMasterId!=undefined && (entityConfigPropertiesMasterId==5 || entityConfigPropertiesMasterId==6)){
			    	document.getElementById("trpProp").style.display="block";
			    	$('#dataPropertyValue').val('');
			    	$('#propertyTrp').val('');
			    	document.getElementById("propertyValue_dd").style.display="none";
			    	document.getElementById("dataPropertyValueDiv").style.display="block";
			    }

    		}
	});
} 

function setTitle(dd,id,text){
	$(this).css('height','40px')
	
	dv1 =$(dd).children('div');
	dv1.text(text);	
	dv1.attr('id1',id); 
	dv2 =$(dd).children('div');
	dv2.text(text);	
	dv2.attr('id2',id); 
	var property = $("#property_dd").children();
	entityConfigPropertiesMasterId= property.attr('id'); 
	var propertyVal= $("#propertyValue_dd").children();
	propertyValId= propertyVal.attr('id1');	
}

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
}

//init custom dropdown menu
$(function() {
	var property_dd = new DropDown( $('#property_dd') ); 
	var propertyValue_dd = new DropDown( $('#propertyValue_dd') ); 
	$(document).click(function() {
		// all dropdowns
		$('.wrapper-dropdown-2').removeClass('active');					
	});
});	

function loadScrollbarForDropDown(){
	$('.dropdown').jScrollPane({showArrows: false,
	        					scrollbarWidth: 5,
	        					arrowSize: 5});
}

function entityConfigurationProps(entityMasterId,testRunPlan,runConfigId){
	if(runConfigId!=null){
		runConfigurationId=runConfigId;
	}
	
	testRunPlanId=testRunPlan;
	entityMastrId=entityMasterId;
	$("#divPopUpEntityConfigurationProps h4").text("");
	$("#divPopUpEntityConfigurationProps h4").text("Entity Configuration Properties");
	$("#divPopUpEntityConfigurationProps").modal();
	
	$('#property_ul').empty();
	$('#propertyValue_ul').empty();
	
	if(rnCnfgFlag){
		entityMasterId = 27;
	}
	
	$.post('common.list.entityConfigPropsMaster?entityMasterId='+entityMasterId,function(data) {
		if(data.Result=="ERROR"){
			callAlert(data.Message);
    		return false;
    	}
    var ary = (data.Options);
    $.each(ary, function(index, element) {  
    	$('#property_ul').append('<option style="padding-Bottom:34px" name="' + element.DisplayText + '" id="' + element.Value + '" onclick="setTitle(\'#property_dd\',\''+element.Number+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
    });
    
    $('#property_ul').select2();
    entityConfigPropertiesMasterId = $('#property_ul').find('option:selected').attr('id');	
	loadPropertyValues(entityConfigPropertiesMasterId);
	});
}

function popUpEntityConfiguratioPropsClose(){
	$("#divPopUpEntityConfigurationProps").fadeOut("normal");
}

function popUpEntityConfiguratioPropsClose() {	
	$("#divPopUpCloneBuild").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
} 

function saveEntityConfigurationProps(){	
	  var propertyValue;
	  entityConfigPropertiesMasterId = $('#property_ul').find('option:selected').attr('id');
	  console.log("entityConfigPropertiesMasterId==>"+entityConfigPropertiesMasterId);
	  var dataPropertyVal= document.getElementById("dataPropertyValue").value;	 
	  var optionPropertyValue =  $('#propertyValue_ul').find('option:selected').val();;
	  console.log("optionPropertyValue=>"+optionPropertyValue+"dataPropertyVal=>"+dataPropertyVal);
	  var propertyTrp;	
	  if(entityConfigPropertiesMasterId==2 || entityConfigPropertiesMasterId==4){
		  propertyValue=dataPropertyVal;
	  } else if(entityConfigPropertiesMasterId == 5 || entityConfigPropertiesMasterId == 6){		  
		  propertyTrp = document.getElementById("propertyTrp").value;
		  if(propertyTrp == ""){
				callAlert("Please Provide Key");				
				return false;
		  }
		  propertyValue=dataPropertyVal;
	  } else{		  
		  propertyValue=optionPropertyValue;
	  }	
	  var copyDataType = [];
	 
	  if(propertyValue== ""){
		callAlert("Please Select Value");		
		return false;
	  }
	  
	  var entityId;
	  if(rnCnfgFlag){
		  entityMastrId = 27;
		  entityId = rnCnfgId;
	  } else {
		  entityId = tpcId;
	  }
	   
	 var url='entityConfigureProperties.add?copyDataType='+copyDataType;
	 openLoaderIcon();
		$.ajax({
			type: "POST",
	    url: url,
	    data: {'entityMasterId': entityMastrId, 'testRunPlanId': testRunPlanId,'runConfigurationId': rnCnfgId,'entityConfigPropertiesMasterId':entityConfigPropertiesMasterId,'propertyValue':propertyValue,'propertyTrp':propertyTrp},
	   
	    complete : function(data){
			if(data != undefined){			
				closeLoaderIcon();
			}
			$('#jTableContainerproducts').find('.jtable-child-table-container').jtable('reload');
		},
		success : function(data) {
			closeLoaderIcon();
			if(data.Result == 'OK'){
				callAlert("Record is saved");
				var url = 'entityConfigureProperties.list?entityId='+ entityId +'&entityMasterId='+entityMastrId+'&jtStartIndex=0&jtPageSize=10';
		    	jsonObj={"Title":"Test Configuration Properties",
						"url": url,	
						"jtStartIndex":0,
						"jtPageSize":10000,				
						"componentUsageTitle":"TPConfigurationProperties",
				};
				assignTestPlanDataTableValues(jsonObj, "TPConfigurationPropertiesTable");
			}else if(data.Result == 'ERROR'){
				callAlert(data.Message);
			}
			else{
				callAlert("Failed !!!");	
			}
		},error : function(data){
			closeLoaderIcon();
		}		
	});
}

var entityId;
$(document).ready(function(){
	document.getElementById("trpProp").style.display="none";
	$('#property_ul').empty();
	$('#propertyValue_ul').empty();
	$("#property_ul").on( "select2-close", function(){
		$('#propertyValue_ul .select2-chosen').empty();
		var valueName = $("#property_dd .select2-chosen").text().trim(); 
		entityConfigPropertiesMasterId = $('#property_ul option[name='+valueName+']').attr('id');
		loadPropertyValues(entityConfigPropertiesMasterId); 
			  
		if(entityConfigPropertiesMasterId!=undefined && (entityConfigPropertiesMasterId==1 || entityConfigPropertiesMasterId==3)){
			document.getElementById("propertyValue_dd").style.display="block";
		    document.getElementById("dataPropertyValueDiv").style.display="none";
		    document.getElementById("trpProp").style.display="none";
		    loadPropertyValues(entityConfigPropertiesMasterId); 
		}else if(entityConfigPropertiesMasterId!=undefined && (entityConfigPropertiesMasterId==2 || entityConfigPropertiesMasterId==4)){
			document.getElementById("propertyValue_dd").style.display="none";
			document.getElementById("dataPropertyValueDiv").style.display="block";
			document.getElementById("trpProp").style.display="none";
	    } else if(entityConfigPropertiesMasterId!=undefined && (entityConfigPropertiesMasterId==5)){
	    	document.getElementById("trpProp").style.display="block";
	    	document.getElementById("propertyValue_dd").style.display="none";
	    	document.getElementById("dataPropertyValueDiv").style.display="block";
	    } else if(entityConfigPropertiesMasterId!=undefined && (entityConfigPropertiesMasterId==6)){
	    	document.getElementById("trpProp").style.display="block";
	    	document.getElementById("propertyValue_dd").style.display="none";
	    	document.getElementById("dataPropertyValueDiv").style.display="block";
	    }
	});
	
	$(document).on('change','#resultsReportingMode', function() {
		if($(this).val() == "DISCRETE JOBS"){
			$("#tpRunConfig").closest('tr').hide();
		}else{
			$("#tpRunConfig").closest('tr').show();
		}
	});
	$(document).on("change",".applyBtn", function(e) {
		var workdateFrom = DaterangePicker.fromDate();
		var workdateTo = DaterangePicker.toDate();
	});
	$(document).on('change','#version_ul', function() {
		loadPdtBuild();
	});
}); 
 
$(document).on('change','#property_ul', function() {
	entityConfigPropertiesMasterId = $('#property_ul').find('option:selected').attr('id');	
	loadPropertyValues(entityConfigPropertiesMasterId); 	  
	if(entityConfigPropertiesMasterId!=undefined && (entityConfigPropertiesMasterId==1 || entityConfigPropertiesMasterId==3)){
		document.getElementById("propertyValue_dd").style.display="block";
		document.getElementById("dataPropertyValueDiv").style.display="none";
		document.getElementById("trpProp").style.display="none";
		loadPropertyValues(entityConfigPropertiesMasterId); 
	}else if(entityConfigPropertiesMasterId!=undefined && (entityConfigPropertiesMasterId==2 || entityConfigPropertiesMasterId==4)){
		document.getElementById("propertyValue_dd").style.display="none";
		document.getElementById("dataPropertyValueDiv").style.display="block";
		document.getElementById("trpProp").style.display="none";
	} else if(entityConfigPropertiesMasterId!=undefined && (entityConfigPropertiesMasterId==5 || entityConfigPropertiesMasterId==6)){
		document.getElementById("trpProp").style.display="block";
		document.getElementById("propertyValue_dd").style.display="none";
		document.getElementById("dataPropertyValueDiv").style.display="block";
	}
});	
 
function testRunPlan(testRunPlanId,executionTypeName){
	var testRunPlanType='';
	productTypehidden = document.getElementById("hdnproductType").value;
	if(productTypehidden == "hdnproductType"){
		productTypehidden= -1;
	}
	if(nodeType == "Product"){
		if(pageType!="HOMEPAGE")productVersionId = -1;	
		key = productId;
	}
	if(pageType=="HOMEPAGE"){key=productVersionId;title='';};
	if(productVersionId == null || productVersionId == 'null' || productVersionId == ''){
		callAlert("Please select the Product Version");
		return false;
	}
	if(testRunPlanId==-1){
		testRunPlanId=document.getElementById("hdnTestRunPlanId").value;
		testRunPlanType=document.getElementById("hdnTestRunPlanType").value;
	}
	testRunPlanType=executionTypeName;
	if(testRunPlanType == 4 || testRunPlanType == "Manual") {
		$("#WorkpackageCreationDiv div.row:nth-child(2) div.form-group:nth-child(1)").show();
		$("#WorkpackageCreationDiv div.row:nth-child(2) div.form-group:nth-child(2)").show();
	}else{
		$("#WorkpackageCreationDiv div.row:nth-child(2) div.form-group:nth-child(1)").hide();
		$("#WorkpackageCreationDiv div.row:nth-child(2) div.form-group:nth-child(2)").hide();
		
	}
	document.getElementById("hdnTestRunPlanId").value=testRunPlanId;
	document.getElementById("hdnTestRunPlanType").value=testRunPlanType;
	var month=0;
	var date = new Date();
	if((date.getMonth()<=9)){
		month="0"+(date.getMonth()+1);
	}else{
		month=date.getMonth()+1;
	}
    var timestamp = date.getFullYear()+"-"+month+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	var productVersionName=title;
	var productName= document.getElementById("treeHdnCurrentProductName").value ;
	var workpackageName=productName+'-'+productVersionName+'-'+timestamp;
	var description=workpackageName + " is created";
	
	$("#wpkg_name").val(workpackageName);
	$("#wpkg_desc").val(description);
	
	filterVal='Edit';
}

function showTestRunplanForm(disp){	
	if(disp == "true"){		
		var jsonObj={};
		jsonObj.Title = "TestRunPlan";
		jsonObj.mode ="edit",
		jsonObj.productId=productId;
		jsonObj.productVersionId=productVersionId;		
		TestRunPlan.init(jsonObj);
		$("#testRunPlanNameLinkContainer").modal();
	}
}	 

function setModeEdit(){
	filterVal='Edit';	
	$(".button-next").attr('disabled', 'disabled');
}

function loadScriptTypeMaster(){
	$('#scriptTypeMaster_ul').empty();
	$.post('common.list.scripttype',function(data) {	
		var ary = (data.Options);
	    $.each(ary, function(index, element) {
			$('#scriptTypeMaster_ul').append('<option id="' + element.Value + '"  value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
	    $('#scriptTypeMaster_ul').select2();
	});
}

function testRunPlanSummary(urlToGetTestRunPlanOfSpecifiedProductVersionId) {		
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : urlToGetTestRunPlanOfSpecifiedProductVersionId,
		dataType : 'json',
		success : function(data) {
			var result=data.Records;
			var totalTCCount=0;
			var totalTCExecutedCount=0;
			var totalDefectsCount=0;
			var approvedDefectsCount=0;
			
			$("#name").empty();
			$("#description").empty();
			var executionType= $("#executionType_ul").find('option:selected').attr('id');
			$("#cronSchedule").empty();
	
			$("#notifyByMail").empty();
			var testToolMasterId=  $("#testTollMaster_ul").find('option:selected').attr('id');
			var testScriptType=  $("#scriptTypeMaster_ul").find('option:selected').attr('id');
								 
			 $.each(result, function(i,item){
				$("#name").val(item.testRunPlanName);
										
				if(item.description==null) {							
					$("#description").val('NA');
				
				}else{							
					$("#description").val(item.description);
				}
			 	//$("#version_ul").select2("val",item.productVersionId);
				$("#version_ul").val(item.productVersionId);				
				$("#testTollMaster_ul").select2("val", item.testToolMasterId);
				$("#scriptTypeMaster_ul").select2("val", item.testScriptType);
				//$("#testTollMaster_ul").val(item.testToolMasterId);
				$("#executionType_ul").select2("val",item.executionTypeId);
				//$("#executionType_ul").val(item.executionTypeId);
				
				$("#testRunScheduledIntervalInHour").val(item.testRunScheduledIntervalInHour);						
				$("#testRunRecurrenceLimit").val(item.testRunRecurrenceLimit);						
				$("#cronSchedule").val(item.testRunCronSchedule);
				$("#notifyByMail").val(item.notifyByMail);
				
				//$("#Objectrepository_ul").select2("val",item.objectRepoAttachmentId);
				$("#Objectrepository_ul").val(item.objectRepoAttachmentId);
				//$("#testDataRepository_ul").select2("val",item.testDataAttachmentId);
				$("#testDataRepository_ul").val(item.testDataAttachmentId);
				postBug = item.autoPostBugs;//mamtha
				
				// ---- 
				if(postBug == 1){							
					if($("#autoPostBugsID").has('span').length>0){
						$("#autoPostBugsID span").addClass('checked');
					}else{
						$("#autoPostBugsID input").attr("checked", "checked");	
					}							

				}else if(postBug == 0){							
					if($("#autoPostBugsID").has('span').length>0){
						$("#autoPostBugsID span").removeClass('checked');
					}else{
						$("#autoPostBugsID input").attr("checked", false);
					}							
				}			
				// ----				
				postTestResult = item.autoPostResults;
				
				if(postTestResult == 1){
					if($("#autoPostResultsID").has('span').length>0){
						$("#autoPostResultsID span").addClass('checked');
					}else{
						$("#autoPostResultsID input").attr("checked", "checked");	
					}
					
				}else if(postTestResult == 0){
					if($("#autoPostResultsID").has('span').length>0){
						$("#autoPostResultsID span").removeClass('checked');
					}else{
						$("#autoPostResultsID input").attr("checked", false);
					}							
				}
				$("#testScriptSource").val(item.testScriptSource);
				$("#testSuiteScriptFileLocation").val(item.testSuiteScriptFileLocation);
				//$("#multipleTestSuites_ul").val(item.multipleTestSuites);
				$("#multipleTestSuites_ul").select2("val",item.multipleTestSuites);
				//$("#testScriptsLevel_ul").val(item.testScriptsLevel);
				$("#testScriptsLevel_ul").select2("val",item.testScriptsLevel);
				$("#pdtBuild_ul").select2("val",item.productBuildId);
				$("#automationMode_ul").select2("val",item.automationMode);
				$("#resultsReportingMode").select2("val",item.resultsReportingMode);
				
				if(item.resultsReportingMode == "DISCRETE JOBS"){
					$("#tpRunConfig").closest('tr').hide();
				} else {
					$("#tpRunConfig").closest('tr').show();
				}
				
				if(item.useIntelligentTestPlan=="1"){
					$($("#useISETestPlan").find('input')[0]).attr('checked',true);
				}else{
					$($("#useISETestPlan").find('input')[1]).attr('checked',true);
				}
				
				if(item.resultsReportingMode == undefined || item.resultsReportingMode.toUpperCase() == "COMBINED_JOB"){
					$($("#resultsReportingMode").find('input')[0]).attr('checked',true);
				} else {
					$($("#resultsReportingMode").find('input')[0]).attr('checked',true);
				}
				
				$("#tpRunConfig").select2("val",item.combinedResultsRunConfigurationId);
			});					
		},
	    complete: function(data){
	    }        
	});
}

var clearTimeoutDTTestRunPlanTestBed='';
var testRunPlanTestBedRequest_oTable='';
function reInitializeDTTestRunPlanTestBed(){
	clearTimeoutDTTestRunPlanTestBed = setTimeout(function(){				
		testRunPlanTestBedRequest_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTTestRunPlanTestBed);
	},200);
}

var testBedColumnData;
var testBedCols;
function testRunPlanTestBeds(){
	var divId;
	var tpCreationMode = $('#testRunPlanNameLinkContainer').is(':visible'); 
	if(tpCreationMode){
		divId = "TargTestPlanBeds";
	}else {
		divId = "ConfigureTestBeds";
	}
	 try{
		 if ($('#testBed_dataTable_'+divId).length>0) {
			 $('#'+divId).empty();
		 }
	 } catch(e) {}
	
	var cols,columnData;
	var testRunPlanId=document.getElementById("hdnTestRunPlanId").value;
	var headerTitleArr=[];
	var content = '<table id="testBed_dataTable_'+divId+'" style="font-size: 12px;" class="display" cellspacing="0" width="100%">';
	content+= '<thead><tr><th>Testsuite</th><th>Testcase</th>';
	var mappingFooter='<tfoot><tr>';

	$.ajax({
	    type: "POST",
	    url : "administration.testrunplan.testbed?testRunPlanId="+testRunPlanId,
	    cache:false,
	    success: function(data) {
	 		var data1=eval(data);
	  		columnData=data1[0].DATA;
	  		cols = data1[0].COLUMNS; 
			
			for(var j=0;j<columnData.length;j++){
				for(var i=0;i<columnData[j].length;i++){
					item = columnData[j][i];
					if(i>1){
						if(item.startsWith("NA")){
							columnData[j][i]="NO";
						}else{
							columnData[j][i]="YES";
						}
					}	  					
				}
			}
				
	  		testBedColumnData = columnData;
	  		testBedCols = cols;
	  		var atarget = [];
	  	  	$(cols).each(function(index, element){
	  	  		if(!tpCreationMode){
		  	  		if(index > 1){
			  	  		element.title = '<input type="checkbox" class="icheck selectAllCheckboxes" id="' + element.id + '" data-radio="iradio_flat-grey" style="margin-left:-8px;margin-right:8px;top:2px;position:relative;"/>' + element.title;
		  	  			content += '<th>'+element.title+'</th>';
			  	  		atarget[index] = index;	
			  	  		headerTitleArr[index]=element.id;
		  	  		}
	  	  		}else{
		  	  		if(index > 1){
		  	  			content += '<th>'+element.title+'</th>';
			  	  		atarget[index] = index;
			  	  		headerTitleArr[index]=element.id;
		  	  		}
	  	  		}
	  	  		mappingFooter +='<th></th>';
	  	  	});
	  	  	content +=' </tr></thead>';
	  	    mappingFooter += '</tr></tfoot>';
	  	    content += mappingFooter; 
	  	    content += "</table>";

	  	    $('#'+divId).append(content);	 
	  		testRunPlanTestBedRequest_oTable = $('#testBed_dataTable_'+divId).dataTable({
	  			"dom":'Bfrtilp',
				paging: true,	    			      				
				destroy: true,
				searching: true,
				bJQueryUI: false,
				"bSort" : false,
			    "sScrollX": "100%",
		       "sScrollXInner": "100%",
		       "scrollY":"100%",
		       "bScrollCollapse": true, 
		       
	  			"fnInitComplete": function(data) {
	  				var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	  				$("#testBed_dataTable_"+divId+"_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
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
	  				
	  				reInitializeDTTestRunPlanTestBed();			   
	  			},
	  			 buttons: [
	  			         {
	  		                extend: 'collection',
	  		                text: 'Export',
	  		                buttons: [
	  		                    {
	  		                    	extend: 'excel',
	  		                    	title: 'Test Suite',
	  		                    	exportOptions: {
	  		                            columns: ':visible'
	  		                        }
	  		                    },
	  		                    {
	  		                    	extend: 'csv',
	  		                    	title: 'Test Suite',
	  		                    	exportOptions: {
	  		                            columns: ':visible'
	  		                        }
	  		                    },
	  		                ],	                
	  		            },
	  		            'colvis',
	  		     ], 
	  			"fnRowCallback": function( nRow, aData, iDisplayIndex) {
	  			},
	  			"oLanguage": {
	             	"sSearch": "",
	             	"sSearchPlaceholder": "Search all columns"
	            },
	  			//"bPagination" : false,
	  			"aaData": columnData,
	  			//"aoColumns": cols,
	  			"aoColumnDefs":[ 
	  			                 {
	  			                     "aTargets":[0,1],
	  			                     sWidth: '25%',
	  			                 },                
	  			                 { 
	  								mRender: function (data, type, full, meta) {
	  									if ( type === 'display' ){						
	  										var resultType='<span><select id="'+headerTitleArr[meta.col]+'" class="editorNotification-active">';
	  										resultType += '<option '+full[0]+' value=Yes>Yes</option>'; 
	  										resultType += '<option '+full[0]+' value=No>No</option>';
	  										resultType += '</select><span>';					 
	  										data = resultType;
	  									}
	  									return data;
	  								},                 
	  								"aTargets":atarget,								   		                    						    				                
	  			                } 
	  					]
	  		});	   			
	  		$("div.toolbar").html('<b>Test Beds Distribution</b>');  			

	  		$(function(){ // this will be called when the DOM is ready
	  			$("#testBed_dataTable_"+divId+"_length").css('margin-top','8px');
	  			$("#testBed_dataTable_"+divId+"_length").css('padding-left','35px');
	  			
				for(var j=0;j<columnData.length;j++){
	  				for(var i=0;i<columnData[j].length;i++){
	  					item = columnData[j][i];
	  					if(i>1){
	  						if(item.toLowerCase() == "yes"){
	  							$(testRunPlanTestBedRequest_oTable.fnGetNodes()).eq(j).find('td').eq(i).find('option').prop('selected', false);
	  						}else{
	  							$(testRunPlanTestBedRequest_oTable.fnGetNodes()).eq(j).find('td').eq(i).find('option').prop('selected', true);
	  						}
	  					}	  					
	  				}
	  			}
	  			
				$('#testBed_dataTable_'+divId+' tbody').on('change', 'td', function () {
					var selVal = $(this).find('select').val();
					var colIndex = $(this).index();
					var rowIndex = parseInt($(this).parent().index());
					testRunPlanTestBedRequest_oTable.DataTable().cell(this).data(selVal);
					$(this).find('select').val(selVal);
				});
				
	  			$('#testBed_dataTable_'+divId+' tbody').on('click', 'td input', function () {	
	  				var tr = $(this).closest('tr');
	  		    	var row = testRunPlanTestBedRequest_oTable.DataTable().row(tr);
	  		    	var tcId = row.data()[1].substring(row.data()[1].indexOf("[")+1, row.data()[1].indexOf("]"));
	  		    	var tsId = row.data()[0].substring(row.data()[0].indexOf("[")+1, row.data()[0].indexOf("]"));
	  		    	var rnId = $(this).attr('id');	
	  		    	var selValue = tScriptLevel;//$('#testScriptsLevel_ul').find('option:selected').val();
	  		    	if(!$(this).prop( 'checked' )){
	  			    	urlMapping = 'administration.testsuite.testcase.maprunconfig?testCaseId='+tcId+'&runConfigId='+rnId+'&testSuiteId='+tsId+'&type=Remove';
	  				}else{
	  		        	urlMapping = 'administration.testsuite.testcase.maprunconfig?testCaseId='+tcId+'&runConfigId='+rnId+'&testSuiteId='+tsId+'&type=Add';
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
	  			        		   callAlert(data.Message);
	  			        		   return true;
	  			        	   }
	  			         }
	  			    });	  		    	
	  			});
	  			
	  			testRunPlanTestBedRequest_oTable.DataTable().columns().every( function () {
	  				var that = this;
	  				$('input', this.footer() ).on( 'keyup change', function () {
	  		           if ( that.search() !== this.value ) {
	  		               that
	  		               	.search( this.value, true, false )
	  		                   .draw();
	  		           }
	  				} );
	  			} );
	  		});   	
		}   	
	});	 
}

var DaterangePicker = {
	fromDate : function(){
		$("#fromTodatepicker").daterangepicker();
		var datepickerStartDate = $(".daterangepicker_start_input>input").val();
		datepickerStartDate = ((typeof datepickerStartDate == "undefined") || (datepickerStartDate == ''))? new Date() : new Date(datepickerStartDate);
		datepickerStartDate = (datepickerStartDate.getMonth() + 1) + "/" + datepickerStartDate.getDate() + "/" + datepickerStartDate.getFullYear();
		return datepickerStartDate;
	},
	toDate : function(){
		$("#fromTodatepicker").daterangepicker();
		var datepickerEndDate = $(".daterangepicker_end_input>input").val();
		datepickerEndDate = ((typeof datepickerEndDate == "undefined") || (datepickerEndDate == '')) ? new Date() : new Date(datepickerEndDate);
		datepickerEndDate = (datepickerEndDate.getMonth() + 1) + "/" + datepickerEndDate.getDate() + "/" + datepickerEndDate.getFullYear();		
		return datepickerEndDate;
	}
};

function saveTestRunPlan(){
	var testrunplanId=0;
	if(filterVal=='Edit'){
		testrunplanId=document.getElementById("hdnTestRunPlanId").value;	
		$(".button-next").removeAttr('disabled');
	}
	
	productVersionId=document.getElementById("hdnProductVersionId").value;
	
	var name= document.getElementById("name").value;
	var description= document.getElementById("description").value;
	var verId = $("#version_ul").find('option:selected').attr('id');
	if(verId != ""){
		productVersionId = verId;
	}	
	var executionType= $("#executionType_ul").find('option:selected').attr('id');
	var cronSchedule= document.getElementById("cronSchedule").value;
	var notifyByMail= document.getElementById("notifyByMail").value;
	var testToolMasterId=  $("#testTollMaster_ul").find('option:selected').attr('id');
	var testScriptType=  $("#scriptTypeMaster_ul").find('option:selected').attr('id');
	var repositoryAttachmentNameId=  $("#Objectrepository_ul").find('option:selected').attr('id');
	var dataAttachmentNameId=  $("#testDataRepository_ul").find('option:selected').attr('id');
	var autoPostBugs = 0;
	var autoPostResults = 0;	
	var autoPostBugsValue = 0;
	
	if($("#autoPostResultsID").has('span').length>0){
       autoPostBugsValue = $('#autoPostBugsID span').hasClass('checked');
	} else {
		autoPostBugsValue = $('#autoPostBugsID input').is(":checked");
	}
	
	if(autoPostBugsValue){
		autoPostBugs = 1;
	}else{
		autoPostBugs = 0;
	}
	
	var autoPostResultsValue = 0;
	
	if($("#autoPostResultsID").has('span').length>0){
		autoPostResultsValue = $('#autoPostResultsID span').hasClass('checked');
	} else {
		autoPostResultsValue = $('#autoPostResultsID input').is(":checked");
	}
	
	if(autoPostResultsValue){
		autoPostResults = 1;
	}else{
		autoPostResults = 0;
	}
	
	var testScriptSource = document.getElementById("testScriptSource").value;
	var testSuiteScriptFileLocation = document.getElementById("testSuiteScriptFileLocation").value;
	var isMultipleTestSuite = $("#multipleTestSuites_ul").find('option:selected').attr('id');
	var testScriptLevel = $("#testScriptsLevel_ul").find('option:selected').attr('id');
	var productBuildId = $("#pdtBuild_ul").find('option:selected').attr('id');
	var useISETestPlan = $("#useISETestPlan").find('input:checked').val();
	var automationMode = $("#automationMode_ul").find('option:selected').attr('id');
	var resultsReportingMode = $("#resultsReportingMode").find('option:selected').attr('id');
	var tpRunConfig =  $("#tpRunConfig").find('option:selected').attr('id');
	var pdtBuildVal = $('#pdtBuild_ul').find('option:selected').text();
	var pdtVersionVal = $('#version_ul').find('option:selected').text();
	if(productVersionId== ""){
		callAlert("Please Select ProductVersion");
		return false;
	}
	if(name== ""){
		callAlert("Please Enter  Name");
		return false;
	}	
	if(description== ""){
		callAlert("Please Enter Description");
		return false;
	}
	if(typeof(pdtVersionVal)=='undefined' || pdtVersionVal == ""){
		callAlert("Please create Product Version");
		return false;
	}
	if(typeof(pdtBuildVal)=='undefined' || pdtBuildVal == ""){
		callAlert("Please create Product Build");
		return false;
	}
	if(executionType== ""){
		callAlert("Please Select Execution type");
		return false;
	}
	if(testToolMasterId== ""){
		callAlert("Please Select Test Tool Master");
		return false;
	}
	if(isMultipleTestSuite== ""){
		callAlert("Please Select Multiple Test Suites");
		return false;
	}
	var url='product.testRunPlan.add?testrunplanId='+testrunplanId;
	$.ajax({
		type: "POST",
		url: url,
		data: {'productVersionId': productVersionId, 'name': name,'description': description,'executionType':executionType,'cronSchedule':cronSchedule ,/*'testRunSchedule':testRunSchedule,*/
			'notifyByMail':notifyByMail,'testToolMasterId':testToolMasterId, 'obAttachmentId':repositoryAttachmentNameId, 'tdAttachmentId':dataAttachmentNameId, 'autoPostBugs':autoPostBugs, 'autoPostResults':autoPostResults,
			'testScriptSource' : testScriptSource, 'testSuiteScriptFileLocation' : testSuiteScriptFileLocation, 'isMultipleTestSuite' : isMultipleTestSuite, 'testScriptLevel' : testScriptLevel, 'testScriptType' : testScriptType,
			'productBuildId':productBuildId, 'useISETestPlan':useISETestPlan, 'automationMode':automationMode, 'resultsReportingMode' : resultsReportingMode, 'tpRunConfig':tpRunConfig},
		success: function(data) {
			$.unblockUI();
			if(data.Result=="ERROR"){
				alert(data.Message);
				return false;
			}else{
				if(atlasIframeFlag && pageType=="TESTRUNPLANVIEW"){
					var messageData = {};
					messageData["action-request"] = "close-model";
					messageData["element-id-for-action"] = "createTAFViewUI";
					parent.postMessage(messageData, '*');
				}else{
					$('#testRunPlanNameLinkContainer').modal('hide');
					document.getElementById("hdnTestRunPlanId").value=data.Record.testRunPlanId;
					document.getElementById("hdnTestRunPlanType").value=data.Record.executionTypeName;
					productMgmtTestRun();
				}
			}
		},    
		dataType: "json", // expected return value type
		complete: function(data){
		}
	});	
}

function loadResultsReportingMode(){
	$('#resultsReportingMode').empty();
	$('#resultsReportingMode').append('<option id="DISCRETE JOBS"  value="DISCRETE JOBS" ><a href="#">DISCRETE JOBS</a></option>');
	$('#resultsReportingMode').append('<option id="COMBINED JOB"  value="COMBINED JOB" ><a href="#">COMBINED JOB</a></option>'); 
	$('#resultsReportingMode').select2();
}
