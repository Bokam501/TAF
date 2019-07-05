var objectRepository='ObjectRepository';
var testData='TestData';
var headerSpan;
var ATSGHelpComponent = function(){	
	var initialise = function(jsonObj){
		initialiseHelpValue(jsonObj);
	};	
		return {
			init : function(jsonObj){
				initialise(jsonObj);
			}
		};
}();

var jsonObjMain='';
function initialiseHelpValue(jsonObj){
	jsonObjMain =jsonObj;
	
	
	var url='bddkeywordsphrases.list?productType='+productTypeName+'&testTool='+testToolName+'&status=2&jtStartIndex=0&jtPageSize=1000';
	var jsonObj={"Title":"BDD Keywords:",
			"url": url,
			 "componentUsageTitle":"BDDkeywords",
	};
	SingleDataTableContainer.init(jsonObj);
	//bddkeywordsphraseslist(jsonObj);
	/*displayATSGHelp(jsonObj);*/
}

function displayATSGHelp(jsonObj){
	$("#atsgHelpContainer h4").text("");
	$("#atsgHelpContainer h4").text(jsonObj.Title);
	$("#atsgHelpContainer h5").text("");
	$("#atsgHelpContainer h5").text(jsonObj.subTitle);	
	
	/*if($("#codeEditorRadiosBDD label").hasClass('active')){
		$("#codeEditorRadiosBDD label").removeClass('active');
	}	*/
	$("#atsgHelpContainer").modal();
}

function listBDDKeywords(){
	var url='bddkeywordsphrases.list?productType='+productTypeName+'&testTool='+testToolName+'&status=2&jtStartIndex=0&jtPageSize=1000';
	var jsonObj={"Title":"BDD Keywords:",
			"url": url,
			 "componentUsageTitle":"BDDkeywords",
	};
	SingleDataTableContainer.init(jsonObj);
}


function testScriptAttachmnetFileContent(){
	
	if(testDataId == -1){
		alert("Please select Test Data File !");
		return false;
	}
	var url='list.testData.and.objectRepository.FileContent?attachmentId='+testDataId;
	var jsonObj={"Title":"Test Data",
			"url": url,
			 "componentUsageTitle":"TestData",
	};
	SingleJtableContainer.init(jsonObj);
}

/*function bddScriptGenerationHandler(){
	closeATSGContainer();
	displayDownloadTestScriptsFromTestCases(jsonObjMain.testCaseId, scriptTestCaseName, scriptDownloadName);
}*/

function closeATSGContainer(){
	$("#divPopUpDownloadTestScriptsFromTestCases").modal("hide");
	$("#divPopUpBDDTestScripts").modal("hide");	
}

function objectRepositoryContent(){	
	if(objectRepositoryId == -1){
		callAlert("Please select Object Repository File!");
		return false;
	}
	var url='list.testData.and.objectRepository.FileContent?attachmentId='+objectRepositoryId;
	var jsonObj= { "Title":"Object Repository",
			"url": url,
			 "componentUsageTitle":"ObjectRepository",
	};
	SingleJtableContainer.init(jsonObj);
}

/*function testDataFileContent(jsonObj){
	clearSingleJTableDatas();
		$('#JtableSingleContainer').jtable({
	         title: 'Test Data ',	        
	         editinline:{enable:false},	        
	         actions: {	             
	           listAction:jsonObj.url,
	         },
	         fields: {	        			        	
	        	testData: { 
	            	title: 'Data' ,
	            	
	            	list:true,
	            	edit:false
	            },
	            testDataValue: { 
	            	title: 'Value' ,	            	
	            	list:true,
	            	edit:false
	            },
	            testDataType: { 
	            	title: 'Type',
	            	   	list:true,
	            	edit:false
	        	},
	        	testDataRemarks: { 
	            	title: 'Remarks',
	                  	list:true,
	            	edit:false
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
	       },
	    });
		 
	   $('#JtableSingleContainer').jtable('load');	
}*/

function keywordslist(){
	testToolName = $("#testTollMaster_bdd_ul").find('option:selected').text();
	var url= "bddkeywordsphrases.list?productType="+productType+"&testTool="+testToolName+"&status=2&jtStartIndex=0&jtPageSize=1000";
	headerSpan = $("#keywordsDragItemsHeader").find("h5 span");
	$("#keywordsDragItemsHeader").find("h5").text("Keywords");
	$("#keywordsDragItemsHeader").find("h5").append("<span class='badge badge-default' id='#keywordsDragItemsTotalCount' style='float:right;background:#a294bb'>"+totalRepository+"</span>");
    $('#keywordssearchLeftDragItems').val('');

	$('#keywordssearchLeftDragItems').keyup(function() {
		var txt=$('#keywordssearchLeftDragItems').val();	
		if($('#keywordssearchLeftDragItems').value==''){				
			$("#keywordsleftDragItemsTotalCount").text(totalUnMappedTCList);
		}
		var resArr = [];
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();		
			$('#keywordDragItemsContainer li').show().filter(function() {	    	
	    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	        if(!~text.indexOf(val) == false) resArr.push("item");	        
	    	  return !~text.indexOf(val);	    
	    }).hide();		
			
		$("#keywordsDragItemsTotalCount").text(+resArr.length+" / "+totalRepository);		
	});
	
	var json={"ItemContainer":"keywordDragItemsContainer",
			"dragDropListItemLoaderIcon": "dragDropListItemLoaderIcon",
			 url:url,
			"badgeUnMappedTestCases":"badgeUnMappedKeywords",
			"selectAllUnmappedTestcasesRowCount":"selectAllUnmappedTestcasesRowCount",
			"DragItemsTotalCount":"keywordsDragItemsTotalCount",
			'DragItemsHeader':"keywordsDragItemsHeader",
			'titleName':"Keywords"		
	};
	loadAttachmentsKeywords(json);
}

// ----- For Embedded ----

function keywordslistForEmbedded(){
	var url= "bddkeywordsphrases.list?productType=&testTool="+testToolName+"&status=2&jtStartIndex=0&jtPageSize=1000";
	headerSpan = $("#keywordsDragItemsHeaderEmbedded").find("h5 span");
	$("#keywordsDragItemsHeaderEmbedded").find("h5").text("Keywords");
    $('#keywordssearchLeftDragItemsEmbedded').val('');

	$('#keywordssearchLeftDragItemsEmbedded').keyup(function() {
		var txt=$('#keywordssearchLeftDragItemsEmbedded').val();	
		if($('#keywordssearchLeftDragItemsEmbedded').value==''){				
			$("#keywordsleftDragItemsTotalCount").text(totalUnMappedTCList);
		}
		var resArr = [];
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();		
			$('#keywordDragItemsContainerEmbedded li').show().filter(function() {	    	
	    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	        if(!~text.indexOf(val) == false) resArr.push("item");	        
	    	  return !~text.indexOf(val);	    
	    }).hide();		
			
		$("#keywordsDragItemsTotalCountEmbedded").text(+resArr.length+" / "+totalRepository);			
	});
	
	var json={"ItemContainer":"keywordDragItemsContainerEmbedded",
			"dragDropListItemLoaderIcon": "dragDropListItemLoaderIcon",
			 url:url,
			"badgeUnMappedTestCases":"badgeUnMappedKeywords",
			"selectAllUnmappedTestcasesRowCount":"selectAllUnmappedTestcasesRowCount",
			"DragItemsTotalCount":"keywordsDragItemsTotalCountEmbedded",
			'DragItemsHeader':"keywordsDragItemsHeaderEmbedded",
			'titleName':"Keywords"		
	};
	loadAttachmentsKeywords(json);
}

function objectRepositoryFileContentEmbedded(){	
	$("#searchRightDragItemsTestdata").val('');
	$("#pageObjectSearchRightDragItems").val('');	
	totalRepository  = 0;
	
	var url= "list.testData.and.objectRepository.FileContent?productId="+productId+"&attachmentId=-1&type=ObjectRepository&filter="+objectRepositoryId+"&toolName="+testToolName;
	headerSpan = $("#leftDragItemsHeader").find("h5 span");
	$("#leftDragItemsHeaderEmbedded").find("h5").text("Tools");
	$('#searchLeftDragItemsEmbedded').val('');
	
	$('#searchLeftDragItemsEmbedded').keyup(function() {
		var txt=$('  #searchLeftDragItemsEmbedded').val();	
		if($('#searchLeftDragItemsEmbedded').value==''){				
			$("#leftDragItemsTotalCount").text(totalUnMappedTCList);
		}	
		var resArr = [];
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();		
			$('#leftDragItemsContainerEmbedded li').show().filter(function() {	    	
	    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	        if(!~text.indexOf(val) == false) resArr.push("item");	        
	    	  return !~text.indexOf(val);	    
	    }).hide();		
		
		$("#leftDragItemsTotalCount").text(+resArr.length+" / "+totalRepository);			
	});	
	
	var json={"ItemContainer":"leftDragItemsContainerEmbedded",
			"dragDropListItemLoaderIcon": "dragDropListItemLoaderIcon",
			 url:url,
			"badgeUnMappedTestCases":"badgeUnMappedTestCases",
			"selectAllUnmappedTestcasesRowCount":"selectAllUnmappedTestcasesRowCount",
			"DragItemsTotalCount":"leftDragItemsTotalCount",
			'DragItemsHeader':"leftDragItemsHeaderEmbedded",
			'titleName':"Object Repository"
			
		};
	loadAttachmentsKeywords(json);
}

// ----- ended 

function listingPageObjects(){
	$("#searchLeftDragItemsUIObject").val('');
	$("#searchRightDragItemsTestdata").val('');
	var url= "amdocs.pageobject.list?productId="+productId+"&testCaseId="+testCaseId;
	
	$('#pageObjectSearchRightDragItems').keyup(function() {
		var txt=$('#pageObjectSearchRightDragItems').val();	
		if($('#pageObjectSearchRightDragItems').value==''){				
			$("#pageObjectrightDragItemsTotalCount").text(totalUnMappedTCList);
		}	
		var resArr = [];
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();		
			$('#pageObjectrightDragItemsContainer li').show().filter(function() {	    	
	    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	        if(!~text.indexOf(val) == false) resArr.push("item");	        
	    	  return !~text.indexOf(val);	    
	    }).hide();		
			
		$("#pageObjectrightDragItemsTotalCount").text(+resArr.length+" / "+totalRepository);			
	});
	
	var json={"ItemContainer":"pageObjectrightDragItemsContainer",
			"dragDropListItemLoaderIcon": "dragDropListItemLoaderIcon",
			 url:url,
			"badgeUnMappedTestCases":"badgeUnMappedKeywords",
			"selectAllUnmappedTestcasesRowCount":"selectAllUnmappedTestcasesRowCount",
			"DragItemsTotalCount":"pageObjectrightDragItemsTotalCount",
			'DragItemsHeader':"pageObjectDragItemsHeader",
			'titleName':"Page Objects"		
		};
	loadAttachmentsKeywords(json);
}

function objectRepositoryFileContent(){	
	$("#searchRightDragItemsTestdata").val('');
	$("#pageObjectSearchRightDragItems").val('');
	$("#codeBDDContainerTest #leftDragItemsHeader").show();
	$("#codeBDDContainerTest #rightDragItemsHeader").hide();
	totalRepository  = 0;
	
	var url= "list.testData.and.objectRepository.FileContent?productId="+productId+"&attachmentId=-1&type=ObjectRepository&filter="+objectRepositoryId+"&toolName="+testToolName;
	headerSpan = $("#leftDragItemsHeader").find("h5 span");
	$("#codeBDDContainerTest #leftDragItemsHeader").find("h5").text("Object Repository");
	$('#searchLeftDragItemsUIObject').val('');
	
	$('#searchLeftDragItemsUIObject').keyup(function() {
		var txt=$('  #searchLeftDragItemsUIObject').val();	
		if($('#searchLeftDragItemsUIObject').value==''){				
			$("#leftDragItemsTotalCount").text(totalUnMappedTCList);
		}	
		var resArr = [];
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();		
			$('#leftDragItemsContainerCodeEditor li').show().filter(function() {	    	
	    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	        if(!~text.indexOf(val) == false) resArr.push("item");	        
	    	  return !~text.indexOf(val);	    
	    }).hide();		
		
		$("#leftDragItemsTotalCount").text(+resArr.length+" / "+totalRepository);			
	});	
	
	var json={"ItemContainer":"leftDragItemsContainerCodeEditor",
			"dragDropListItemLoaderIcon": "dragDropListItemLoaderIcon",
			 url:url,
			"badgeUnMappedTestCases":"badgeUnMappedTestCases",
			"selectAllUnmappedTestcasesRowCount":"selectAllUnmappedTestcasesRowCount",
			"DragItemsTotalCount":"leftDragItemsTotalCount",
			'DragItemsHeader':"leftDragItemsHeader",
			'titleName':"Object Repository"
			
		};
	loadAttachmentsKeywords(json);
}

function testDataFileContent(){
	//loadAttachments_Object(projectCodeId, 'testObjectRepository_bdd_dd',1);
	
	
	$("#searchLeftDragItemsUIObject").val('');
	$("#pageObjectSearchRightDragItems").val('');
	$("#codeBDDContainerTest #leftDragItemsHeader").hide();
	$("#codeBDDContainerTest #rightDragItemsHeader").show();
	totalTestData = 0;
	var url= "list.testData.and.objectRepository.FileContent?productId="+productId+"&attachmentId=-1&type=TestData&filter="+testDataId+"&toolName="+testToolName;
	headerSpan = $("#rightDragItemsHeader").find("h5 span");
	$("#rightDragItemsHeader").find("h5").text("Test Data");
		
	$('#searchRightDragItemsTestdata').keyup(function() {
		var txt=$('#searchRightDragItemsTestdata').val();
		if($(' #searchRightDragItemsTestdata').value==''){			
			$("#rightDragItemsTotalCount").text($('#rightDragItemsContainerCodeEditor li').length);
		}	
		var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
	    var resArr = [];
		$('#rightDragItemsContainerCodeEditor li').show().filter(function() {	    	
	    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
	    	 if(!~text.indexOf(val) == false) resArr.push("item");
	        return !~text.indexOf(val);	        
	    }).hide();		
		$(" #rightDragItemsTotalCount").text(+resArr.length+" / "+$(' #rightDragItemsContainerCodeEditor li').length);		
		if(txt==""){
			$("#rightDragItemsTotalCount").text($('#rightDragItemsContainerCodeEditor li').length);		
		}
	});
	
	var json={"ItemContainer":"rightDragItemsContainerCodeEditor",
			"dragDropListItemLoaderIcon": "dragDropListItemLoaderIcon",
			 url:url,
			"badgeUnMappedTestCases":"badgeUnMappedTestCases",
			"selectAllUnmappedTestcasesRowCount":"selectAllUnmappedTestcasesRowCount",
			"DragItemsTotalCount":"rightDragItemsTotalCount",
			'DragItemsHeader':"rightDragItemsHeader",
				'titleName':"Test Data"
			
	};
	loadAttachmentsKeywords(json);
}
var totalRepositoryData;
var totalTestDataData;
var totalTestData;
function loadAttachmentsKeywords(json){
	$("#"+json.ItemContainer).empty();			
	$("#"+json.dragDropListItemLoaderIcon).show();
	$("#"+json.ItemContainer).addClass("ptrNone");
	
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : json.url,
		dataType : 'json',
		complete : function(data){
			$("#"+json.dragDropListItemLoaderIcon).hide();
			if(data != undefined){			
				$("#"+json.ItemContainer).removeClass("ptrNone");	
		    		//allUnmappedTestcasesFun(startIndex,jsonObj);
			}
		},
		success : function(data) {
			$("#"+json.dragDropListItemLoaderIcon).hide();
			//if(testToolName == "SEETEST"){
				if(data.Records == null){
					data.Records = [];
				}
			//}
			if(data.Records.length == 0){
				if($("#"+json.ItemContainer).length>1){				
					$("#"+json.ItemContainer).emtpy();
				}
				
				$("#"+json.DragItemsHeader).find("h5");			
				$("#"+json.DragItemsHeader).find("h5").text(json.titleName).append("<span class='badge badge-default' id='"+json.DragItemsTotalCount+"' style='float:right;background:#a294bb'>0</span>");
				$("#"+json.ItemContainer).append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>No Items to show</b></span>");
				$("#"+json.DragItemsTotalCount).text("0");
				$("#badgeUnMappedTestCases").hide();
				$("#"+json.selectAllUnmappedTestcasesRowCount).hide();
			}else{
				if(json.titleName == 'Object Repository'){
					totalRepository = data.Records.length;
					totalRepositoryData = data.Records;
				}else if(json.titleName == 'Test Data'){
					totalTestData = data.Records.length;
					totalTestDataData = data.Records;
				}else if(json.titleName == 'Page Objects'){
					totalTestData = data.length;
				}
				
				$("#"+json.DragItemsHeader).find("h5");					
				$("#"+json.DragItemsHeader).find("h5").text(json.titleName).append("<span class='badge badge-default' id='"+json.DragItemsTotalCount+"' style='float:right;background:#a294bb'>"+data.Records.length+"</span>");				
				displayRecordsOfFile(data,json);	
			}				
		},
		error: function (data){
			$("#"+json.dragDropListItemLoaderIcon).hide();
		}
	});	
	
	/*if(bool){
		url=jsonObj.leftDragItemsDefaultLoadingUrl;
	}*/	
}	

function displayRecordsOfFile(data,json)
{
	var listObj;
	var result = data.Records;
	$("#"+json.ItemContainer).empty();
	$("#"+json.badgeUnMappedTestCases).show();
	$("#"+json.selectAllUnmappedTestcasesRowCount).show();		
	
    var resultValue="";
    $("#"+json.ItemContainer).html('');
    
	$.each(result, function(i,item){
		if(json.titleName == 'Page Objects'){
			listObj = item.pageObjectsList;
			$.each(listObj, function(i,item){	
				resultValue = leftItemDislayListItemOFile(item,json);			
				$("#"+json.ItemContainer).append(resultValue);			
			});
		}else{
			resultValue = leftItemDislayListItemOFile(item,json);			
			$("#"+json.ItemContainer).append(resultValue);		
		}	
 	});		
 }

function leftItemDislayListItemOFile(item,json){
	var resultList="";
	var entity_name="";
	var entity_dispname = '';
	var tempTargetName='';
		
	//var entity_id = 0;
	if(json.titleName == 'Object Repository' || json.titleName == 'Test Data'  ){
		tempTargetName = item;
		if(testToolName != "EDAT"){
			entity_name = item.split('~')[1];
		} else {
			entity_name = item;
		}
	}else if(json.titleName == 'Keywords'){
		entity_name = item.keywordPhrase;
	}else if(json.titleName == 'Page Objects'){
		entity_name = item;	
	}
		
	entity_dispname = " "+entity_name+"";		
	if(json.titleName == "Page Objects") {
		tempTargetName = entity_dispname; 
		entity_dispname = entity_dispname.split('~')[2];
		entity_name = item.split('~')[3]+"."+entity_dispname;
	}
	entity_dispname=trim(entity_dispname);	
	/*if(json.titleName == 'Object Repository' || json.titleName == 'Test Data'  ){
		resultList = "<li style='width:92%;display:inline-block;border-right:0px;' name="+json.titleName+" pagetargetname="+tempTargetName+" title='"+entity_name+"' ondblclick='codeEditorListClickHandler(event)' oncontextmenu='contextmenuPopup(event);' class='codeMirrorListItem'>"+entity_dispname+"</li><i class='fa fa-pencil-square-o' aria-hidden='true' style='float:right;margin-top:-23px;padding-left:4px;padding-top:3px;display:inline-block;border-right:1px solid #ccc;border-top:1px solid #ccc;border-bottom:1px solid #ccc;width:8%;height:21px;background:#fafafa;'></i>";
	}else{*/
		resultList = "<li class='codeMirrorListItem' name="+json.titleName+" pagetargetname="+tempTargetName+" title="+entity_name+" ondblclick='codeEditorListClickHandler(event)' oncontextmenu='contextmenuPopup(event);'>"+entity_dispname+"</li>";
	//}
		
	return resultList;	
}

function codeEditorListClickHandler(event){
	var titleName = event.target.getAttribute('name');
	var text = event.target.innerText;
	
	if($('#codeMirrorTextEditorBDD').is(':visible')){
		pasteSelectedKeywordItemInEditor(text, titleName);
 	}
 	else if($('#codeMirrorTextEditorBDDEmbedded').is(':visible')){
 		pasteSelectedKeywordItemInEditorEmbedded(text, titleName);
 	}
}

function contextmenuPopup(el){
	var url ='';
	$('.contextmenuPopup').removeClass('hidden');
	var titleName = $(el.target).attr('name');
	var targetName = $(el.target).text();
	var level;
	
	var toolName = $("#testTollMaster_bdd_ul").find('option:selected').text();
	
	if(toolName != "EDAT"){	
		$("#contextMenuBDDKeyword").css('display',"block");
		
		url = 'keyword.list?keywordName='+titleName;
		if(titleName == 'Object'){ // UI Objects
			targetName = $(el.target).attr('pagetargetname');
			$('.contextmenuPopupKeywords, .contextmenuPopupTestData, .contextmenuPopupPageObjects').addClass('hidden');
			$('.contextmenuPopupUIObjects').removeClass('hidden');
			url = 'objectRepositoryAndtestDataContent.list?name='+targetName+'&type='+titleName;
		}else if(titleName == 'Keywords'){ // Keywords
			$('.contextmenuPopupTestData, .contextmenuPopupUIObjects, .contextmenuPopupPageObjects').addClass('hidden');
			$('.contextmenuPopupKeywords').removeClass('hidden');
			url = 'keyword.list?keywordName='+targetName;
		}else if(titleName == 'Test'){ // Test Data
			targetName = $(el.target).attr('pagetargetname');
			$('.contextmenuPopupUIObjects, .contextmenuPopupKeywords, .contextmenuPopupPageObjects').addClass('hidden');
			$('.contextmenuPopupTestData').removeClass('hidden');
			url = 'objectRepositoryAndtestDataContent.list?name='+targetName+'&type='+titleName;
		}else if(titleName == 'Page'){ // Page Objects
			$('.contextmenuPopupUIObjects, .contextmenuPopupTestData, .contextmenuPopupKeywords').addClass('hidden');
			$('.contextmenuPopupPageObjects').removeClass('hidden');
			targetName = $(el.target).attr('pagetargetname');
			level = targetName.split('~')[1];
			if(level == "PageObjects") {
				$('.contextmenuPopupPageObjectsLevelTwo').addClass('hidden');
				$('.contextmenuPopupPageObjectsLevelOne').removeClass('hidden');
			}else {
				$('.contextmenuPopupPageObjectsLevelOne').addClass('hidden');
				$('.contextmenuPopupPageObjectsLevelTwo').removeClass('hidden');
			}
			url = 'amdocs.objects.and.methods.list.byName?name='+targetName;
		}else {
			$('.contextmenuPopup').addClass('hidden');
		}
		loadPopupdata(url, titleName, targetName);
		el.preventDefault();
	}else{
		$("#contextMenuBDDKeyword").css('display',"none");
	}
	return false;	
}

function loadPopupdata(url, titleName, targetName){
	$.ajax({
        method: "POST",
        url: url,
        dataType: 'json',
	
	    success: function(data) {  
			if(data.Result == "ERROR"){
				//callAlert(data.Message);
			}else if(data.Result == "OK"){
				if(titleName == "Keywords"){
					$(".contextmenuPopup .namelabel").empty().html("Keyword");
					$(".contextmenuPopup .namevalue").empty().html(data.Records[0].keywordPhrase);
					$(".contextmenuPopup .descriptionvalue").empty().html(data.Records[0].description);
					$(".contextmenuPopup .uiobjectslabel").empty().html("UI Objects");
					$(".contextmenuPopup .uiobjectsvalue").empty().html(data.Records[0].objects);
					$(".contextmenuPopup .parameterslabel").empty().html("Parameters");
					$(".contextmenuPopup .parametersvalue").empty().html(data.Records[0].parameters);
					$(".contextmenuPopup .tagslabel").empty().html("Tags");
					$(".contextmenuPopup .tagsvalue").empty().html(data.Records[0].tags);
					$(".contextmenuPopup .supportlabel").empty().html("Support");
					$(".contextmenuPopup .sgseleniumvalue").empty().html(data.Records[0].isSeleniumScripGeneration);
					$(".contextmenuPopup .sgappvalue").empty().html(data.Records[0].isAppiumScripGeneration);
					$(".contextmenuPopup .sgseetestvalue").empty().html(data.Records[0].isSeetestScripGeneration);					
					$(".contextmenuPopup .sgprotractorvalue").empty().html(data.Records[0].isProtractorScripGeneration);
					$(".contextmenuPopup .sgedatvalue").empty().html(data.Records[0].isEDATScripGeneration);
					$(".contextmenuPopup .sgcodeduivalue").empty().html(data.Records[0].isCodeduiScripGeneration);
					$(".contextmenuPopup .sgtestcompletevalue").empty().html(data.Records[0].isTestCompleteScripGeneration);
					$(".contextmenuPopup .sgrestassuredvalue").empty().html(data.Records[0].isRestAssuredScripGeneration);					
					$(".contextmenuPopup .seseleniumvalue").empty().html(data.Records[0].isSeleniumScriptless);
					$(".contextmenuPopup .seappvalue").empty().html(data.Records[0].isAppiumScriptless);
					$(".contextmenuPopup .seseetestvalue").empty().html(data.Records[0].isSeetestScriptless);					
					$(".contextmenuPopup .seprotractorvalue").empty().html(data.Records[0].isProtractorScriptless);
					$(".contextmenuPopup .seedatvalue").empty().html(data.Records[0].isEDATScriptless);
					$(".contextmenuPopup .secodeduivalue").empty().html(data.Records[0].isCodeduiScriptless);
					$(".contextmenuPopup .setestcompletevalue").empty().html(data.Records[0].isTestCompleteScriptless);
					$(".contextmenuPopup .serestassuredvalue").empty().html(data.Records[0].isRestAssuredScriptless);
				}else if(titleName == "Page") {
					$(".contextmenuPopup .packagenamelabel").empty().html("Package");
					$(".contextmenuPopup .packagenamevalue").empty().html(data.Record.packageName);
					$(".contextmenuPopup .namelabel").empty().html("Name");
					$(".contextmenuPopup .namevalue").empty().html(data.Record.name);
					$(".contextmenuPopup .testcasenamelabel").empty().html("Test Case Name");
					$(".contextmenuPopup .testcasenamevalue").empty().html(data.Record.testCaseName);

					$(".contextmenuPopup .packagenamelabel").empty().html("Package");
					$(".contextmenuPopup .packagenamevalue").empty().html(data.Record.packageName);
					$(".contextmenuPopup .namelabel").empty().html("Name");
					$(".contextmenuPopup .namevalue").empty().html(data.Record.name);
					$(".contextmenuPopup .methodnamelabel").empty().html("Method Name");
					$(".contextmenuPopup .methodnamevalue").empty().html(data.Record.methodName);
					$(".contextmenuPopup .paramlabel").empty().html("Param");
					$(".contextmenuPopup .paramvalue").empty().html(data.Record.param);
					$(".contextmenuPopup .paramtypelabel").empty().html("Param Type");
					$(".contextmenuPopup .paramtypevalue").empty().html(data.Record.paramType);
					$(".contextmenuPopup .returntypelabel").empty().html("Return Type");
					$(".contextmenuPopup .returntypevalue").empty().html(data.Record.returnType);
				}
			else if(titleName == "Object"){
				//data = $.parseJSON(data);
				targetName = String(targetName).split('.')[1];
				$(".contextmenuPopup .elementlabel").empty().html("Element");
				$(".contextmenuPopup .elementvalue").empty().html(data.Record.elementName);
				$(".contextmenuPopup .descriptionvalue").empty().html(data.Record.description);
				$(".contextmenuPopup .idtypelabel").empty().html("ID Type");
				if(data.Record.id != null){
					$(".contextmenuPopup .idtypevalue").empty().html(data.Record.IdType+" [ "+data.Record.id +" ]");
				}else{
					$(".contextmenuPopup .idtypevalue").empty().html(data.Record.IdType);
				}
				if(data.Record.IdType == "xpath" || data.Record.IdType == "cssSelector"){
					$('.contextmenuPopupUIObjects .uiObjectBrowserTable').removeClass('hidden');
				}else{
					$('.contextmenuPopupUIObjects .uiObjectBrowserTable').addClass('hidden');
				}
				$(".contextmenuPopup .chromexpathvalue").empty().html(data.Record.chromeXpath);
				$(".contextmenuPopup .chromecssvalue").empty().html(data.Record.chromeCssSelector);
				$(".contextmenuPopup .chromelocalvalue").empty().html("locale value");
				$(".contextmenuPopup .firefoxxpathvalue").empty().html(data.Record.firefoxXpath);
				$(".contextmenuPopup .firefoxcssvalue").empty().html(data.Record.firefoxCssSelector);
				$(".contextmenuPopup .firefoxlocalvalue").empty().html("locale value");
				$(".contextmenuPopup .safarixpathvalue").empty().html(data.Record.safariXpath);
				$(".contextmenuPopup .safaricssvalue").empty().html(data.Record.safariCssSelector);
				$(".contextmenuPopup .safarilocalvalue").empty().html("locale value");
				$(".contextmenuPopup .iexpathvalue").empty().html(data.Record.ieXpath);
				$(".contextmenuPopup .iecssvalue").empty().html(data.Record.ieCssSelector);
				$(".contextmenuPopup .ielocalvalue").empty().html("locale value");
			}else if(titleName == "Test"){
				//data = $.parseJSON(data);
				targetName = String(targetName).split('.')[1];
				$(".contextmenuPopup .dataitemlabel").empty().html("Data Item");
				$(".contextmenuPopup .dataitemvalue").empty().html(data.Record.dataName);//data.DataItem
				$(".contextmenuPopup .descriptionvalue").empty().html(data.Record.description);
				$(".contextmenuPopup .typelabel").empty().html("Type");
				$(".contextmenuPopup .typevalue").empty().html(data.Record.type);
				$(".contextmenuPopup .valueslabel").empty().html("Value");
				$(".contextmenuPopup .valuesvalue").empty().html(data.Record.values);
			}
			}
	    },
	    error: function(data){
	       console.log("Error");		   
	        },
	        complete: function(data) {
	    	  
	       }
	   });	
}

$(window).click(function(e){
	if($(e.target).attr('name') == "contextmenuPopupCloseBtn" || $(e.target).parents('.contextmenuPopup').attr('name') != "contextmenuPopup"){
		$('.contextmenuPopup').addClass('hidden');
	}
});

function rightItemDislayListItem(item){
	var resultList="";
	var entity_id = item.domainCategoryId;
	var entity_name = item.domainCategoryName;	
	var entity_dispname = '';		
		entity_dispname = entity_id+" ("+entity_name+")";
		entity_dispname=trim(entity_dispname);
		resultList = "<li title='"+entity_name+"' class='codeMirrorListItem'>"+entity_dispname+"</li>";	
	
	return resultList;
}
