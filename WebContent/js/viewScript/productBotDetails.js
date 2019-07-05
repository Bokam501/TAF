var flag=1;
var SOURCE;
var treeData;
var isEdited = false;
var productBotDetailURL='product.bot.detail.list.by.productId?productId=';

jQuery(document).ready(function() {	
		   
		$("#botId").val("");
		$("#productBotName").val("");
});
	
function getProductBotDetails(botProductId){
	openLoaderIcon();
	
	var response;	
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : productBotDetailURL+botProductId,
		dataType : 'json',
		success : function(data) {
			response = data.Records;
			listTilesBot(response);
		},
		error : function(data){
			closeLoaderIcon();
		},
		complete : function(data){
			closeLoaderIcon();
		}
	});
}

function listTilesBot(data){	
	/* if(data.length==0){
		
		if(flag==1){
			$("#clearDataMessage label").text("No mobile is available for this product.");
		}else{
			$("#clearDataMessage label").text("No server is available for this product.");
		}		
		$("#clearDataMessage").show();
		
	}else{
		$("#clearDataMessage").hide();
	} */
	
	$('#productBotTileContent').empty();
	 
	 var availability = "bg-white"; 
	 if(data.length!=0){	            
         for (var key in data) {
         	var botName = data[key].botName;
         	var imgSrc="";
         	if(botName == "Task Allocation Controller") {
         		imgSrc="css/images/Task Allocation Controller.png";
         	} else if(botName == "Check-in Guard") {
         		imgSrc="css/images/Check-in Guard.png";
         	} else if(botName == "Test Data Creator") {
         		imgSrc="css/images/Test Data Creator.png";
         	} else if(botName == "Testcase Writer") {
         		imgSrc="css/images/Testcase Writer.png";
         	}else if(botName == "Source Code Reviewer") {
         		imgSrc="css/images/Source Code Reviewer.png";
         	}else{
         		imgsrc="css/images/noimage.jpg";
         	}
         	
         	//<span id="botCountBadge" class="badge" style="background: #4db3a4 !important;width: 60px;padding-top: 3px;margin-top: -10px;margin-left: 10px;float:left;">8Bots</span>
         	
         	var badgeDetails = data[key].badges.split(" ~ ");
         	var transactionCountBadge = '<br/><span id="transactionCountBadge" class="badge" style="background: #4db3a4 !important;width: 50px;float:left; cursor:default;" title="'+badgeDetails[0]+' Transactions">'+badgeDetails[0]+' t</span><br/>';
         	var durationBadge = '<span id="durationBadge" class="badge" style="background: #4db3a4 !important;width: 50px;float:left; cursor:default;" title="'+badgeDetails[1]+' Minutes">'+badgeDetails[1]+' m</span><br/>';
         	var costBadge = '<span id="costBadge" class="badge" style="background: #4db3a4 !important;width: 50px;float:left; cursor:default;" title="'+badgeDetails[2]+' Dollars">$ '+badgeDetails[2]+'</span><br/>';
         	
         	var titleSchedule = "Schedule Bot for - "+data[key].botName;
         	var subTitleSchedule=data[key].botName+">>"+data[key].productBotName;
         	var schedulerId=data[key].productBotId;
         	var schedulerType = "Bot Execution";
         	var botId=data[key].productBotId;
         	var botName=data[key].productBotName;
         	var brick = '<div class="'+"tile "+ availability + '" style="border:#ddd solid 1px; height:200px;width: 170px !important;"><div class="tile-body" style="padding:3px 3px;">' +
    	    '<div class="row">'+
         		'<div style="position:inherit; float:left;border-right: solid 0px #ddd;"><img width="90px" height="110px" src="'+ imgSrc +'" onerror="this.src=\'css/images/noimage.jpg\'"></img></div>'+
    	    	'<div style="position:inherit; float:right;">'+transactionCountBadge+'&nbsp;'+durationBadge+'&nbsp;'+costBadge+'</div>'+
    	    '</div>'+
    	    '<div class="row" style="text-align:center;">';
         	if(typeof data[key].isManualExecutionAllowed != 'undefined' && !data[key].isManualExecutionAllowed){
         		brick = brick+'<span style="float: left;padding: 1px 5px;"><i class="fa fa-bolt" style="color:#ccc;cursor:default" title="Manual execution disabled for this bot"></i></span>';
         	}else{
         		brick = brick+'<span style="float: left;padding: 1px 5px;"><i class="fa fa-bolt" style="color:grey;cursor:pointer" title="Execute" onclick="executeBot(\''+ data[key].productBotId+'\')"></i></span>';
         	}
         	
         	brick = brick+'<span style="float: left;padding: 1px 5px;"><i class="fa fa-list-alt" style="color:grey;cursor:pointer" title="Configuration" onclick="getCustomFieldsAndValueOfBot(\''+data[key].botId+'\',\''+ data[key].productBotId+'\',\''+ data[key].productBotName+'\')"></i></span><span style="float: left;padding: 1px 5px;"><i class="fa fa-building" style="color:grey;cursor:pointer" title="Bot Builder" onClick="displayBotScriptEditor(\''+botId+'\',\''+botName+'\')"></i></span><span style="float: left;padding: 1px 5px;"><i class="fa fa-search-plus" style="color:grey;cursor:pointer" title="History" onclick="listBotExecutionAuditHistory(\''+ data[key].productBotId+'\',\''+ data[key].productBotName+'\')"></i></span>';
         	
         	if(typeof data[key].isScheduleExecutionAllowed != 'undefined' && !data[key].isScheduleExecutionAllowed){
    	    	brick = brick+'<span style="float: left;padding: 1px 5px;"><i class="fa fa-clock-o" style="color:#ccc;cursor:default" title="Schedule execution disabled for this bot"></i></span>';
    	    }else{
    	    	brick = brick+'<span style="float: left;padding: 1px 5px;"><i class="fa fa-clock-o" style="color:grey;cursor:pointer" title="Schedule" onClick="scheduleBotUsingCronGen(\''+titleSchedule+'\',\''+subTitleSchedule+'\',\''+schedulerId+'\',\''+schedulerType+'\')"></i></span>';
    	    }
         	
         	brick = brick+'</div>'+
         	'<div class="row" style="text-align:center;"><label class="botTileWrap" title="'+ data[key].productBotName+'" style="font-size: 13px;margin-bottom: 0px; color:#5b9bd1;font-weight:bold;">'+ data[key].productBotName+'</label></div>'+ 
    	    '<div class="row" style="text-align:center;"><span title="'+data[key].botName+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 10px;">' + data[key].botName +'<br /> </span></div>'+			
    	    '</div>'+
    	    '</div>';
    	    
	 		$('#productBotTileContent').append($(brick));
         }
	 }   
	 if(!($('#productBotTileContent').hasClass('tiles'))){
			$('#productBotTileContent').addClass('tiles').css("margin-top","10px");
	 } 
}

function mapProductBots() {
	$("#productBotName").val("");
	$('#editProductBotDTContainer').empty();
	
	productBotDropDownAjax("#botConfiguration_ul",'bot.master.list?isActive=1');
	$("#editProductBotContainer").modal();	
}

function saveProductBot(botId){
	//var botId=$("#botConfiguration_ul").val();
	var productBotName = $("#productBotName"+botId).val();
	var fd = new FormData();
	fd.append("botId", botId);  
	fd.append("productBotName", productBotName);
	fd.append("productId", productId);
	
	openLoaderIcon();
	$.ajax({
		url : 'product.bot.details.save',
		data : fd,
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
				callAlert(data.Message);
			}
			closeLoaderIcon();
			$('#editProductBotContainer').modal('hide');
			getProductBotDetails(productId);
		},
		error : function(data) {
			closeLoaderIcon();  
		},
		complete: function(data){
			closeLoaderIcon();
		}
	});
}

function productBotDropDownAjax(IDValue, url){
	$("#addCommentsLoaderIcon").show();
	 $.ajax({
      type: "POST",
		contentType: "application/json; charset=utf-8",
      url : url,
      dataType : 'json',
      success : function(data) {
           if(data.Result=="OK" && typeof data.Records != 'undefined' && data.Records.length>0){        	   
        	   botDropDownHandler(IDValue, data.Records, "Records");
		   }else if(data.Result=="OK" && typeof data.Options != 'undefined' && data.Options.length>0){        	   
			   botDropDownHandler(IDValue, data.Options, "Options");
		   }	
           $("#addCommentsLoaderIcon").hide();
      },
      error: function(data){
		   $("#addCommentsLoaderIcon").hide();
   	   console.log("error in ajax call");
      },
      
      complete: function(data){
    	  	/*if(jsonObjMain.isFromMyActions){
  				refreshTabCountAndContent();
  			}*/
    	   $("#addCommentsLoaderIcon").hide();
      },
	 });
}


function botDropDownHandler(itemID, item, type){		
	$(itemID).empty();	
	if(type == "Options" || type == "Records"){
		var botModel='';		
		
		for(var i=0; i<item.length; i++){
			//console.log(item[i].Value);
			var botName=item[i].botName;
			var imgSrc="";
         	if(botName == "Task Allocation Controller") {
         		imgSrc="css/images/Task Allocation Controller.png";
         	} else if(botName == "Check-in Guard") {
         		imgSrc="css/images/Check-in Guard.png";
         	} else if(botName == "Test Data Creator") {
         		imgSrc="css/images/Test Data Creator.png";
         	} else if(botName == "Testcase Writer") {
         		imgSrc="css/images/Testcase Writer.png";
         	}else if(botName == "Source Code Reviewer") {
         		imgSrc="css/images/Source Code Reviewer.png";
         	}
         	
         	var nameBot = item[i].botName;
         	var botStatus=item[i].botStatus;
         	if(nameBot.length > 30){         		
         		nameBot = (item[i].botName).toString().substring(0,25)+'...';         		
         	}else{
         		nameBot = item[i].botName;
         	}  
         				
					botModel= ' <div class="tile" style="border:#ddd solid 1px;height:210px;width: 220px !important;"><div class="tile-body" style="padding:3px 3px;">'+
					'<div class="row"><div style="position:inherit;float:left;padding-left: 28%;"><img width="90px" height="90px" src="'+ imgSrc +'" onerror="this.src=\'css/images/noimage.jpg\'"></img></div>'+
					'<div style="float:right;"><span style="float: right;padding: 0px 0px;"><i class="fa fa-building" style="color:grey;cursor:pointer" title="Bot Plan" onclick="displayBotMasterScriptEditor(\''+item[i].id+'\',\''+ item[i].botName+'\',\''+ item[i].botPlan+'\')" ></i></span><br/><span style="float: right;padding: 0px 2px;"><i class="fa fa-question-circle" style="color:grey;cursor:pointer" title="Help" onclick="getBotHelp(\''+item[i].description+'\',\''+ item[i].createdByName+'\',\''+ item[i].createdOn+'\')"></i></span><br/></div>'+
					'</div>'+
					'<div class="row" style="text-align:center;"><span title="'+item[i].botName+'" class="botTileWrap" style="color:rgba(53, 65, 66, 0.78);font-size: 14px;">'+nameBot+'<br/> </span><span title="'+botStatus+'" class="botTileWrap" style="color:#3c763d;font-size: 10px;">'+botStatus+'<br/> </span></div><br/>'+
					'<div class="row" style="text-align:center;padding:0px 0px;"><span id="botCountBadge" class="badge" style="background: #4db3a4 !important;width: 60px;padding-top: 3px;margin-left: 10px;float:left;">'+item[i].botCount+'Bots</span>'+
					//Rating Control
					'<div style="background:white"><span class="star-rating-control"><div class="rating-cancel" style="display: block;"><a title="Cancel Rating"></a></div><div role="text" aria-label="P4" class="star-rating rater-0 auto-submit-star showHandCursor star-rating-applied star-rating-live star-rating-on"><a title="P4">5~10</a></div>'+
					'<div role="text" aria-label="P3" class="star-rating rater-0 auto-submit-star showHandCursor star-rating-applied star-rating-live star-rating-on"><a title="P3">4~10</a></div><div role="text" aria-label="P2" class="star-rating rater-0 auto-submit-star showHandCursor star-rating-applied star-rating-live star-rating-on"><a title="P2">3~10</a></div>'+
					'<div role="text" aria-label="P1" class="star-rating rater-0 auto-submit-star showHandCursor star-rating-applied star-rating-live star-rating-on"><a title="P1">2~10</a></div><div role="text" aria-label="P0" class="star-rating rater-0 auto-submit-star showHandCursor star-rating-applied star-rating-live"><a title="P0">1~10</a></div>'+
					'</span><input name="star10" type="radio" class="auto-submit-star showHandCursor star-rating-applied" value="5~10" title="P4" style="display: none;"><input name="star10" type="radio" class="auto-submit-star showHandCursor star-rating-applied" value="4~10" title="P3" checked="checked" style="display: none;">'+
					'<input name="star10" type="radio" class="auto-submit-star showHandCursor star-rating-applied" value="3~10" title="P2" style="display: none;"><input name="star10" type="radio" class="auto-submit-star showHandCursor star-rating-applied" value="2~10" title="P1" style="display: none;">'+
					'<input name="star10" type="radio" class="auto-submit-star showHandCursor star-rating-applied" value="1~10" title="P0" style="display: none;"></div>'+
					'</div>'+
					
					'<div class="row" style="margin-top: 5px;"><div class="col-md-9" style="padding-right: 0px;padding-left: 0px;"><input id="productBotName'+item[i].id+'" class="form-control input-medium" type="text" name="productBotName'+item[i].id+'" style="width: 160px !important;height: 24px;"></div>'+
					'<div class="col-md-3" style="padding-right: 5px;padding-left: 5px;"><button type="button" onclick="saveProductBot('+item[i].id+');" style="background-color: #666; height: 24px;font-size: 12px;padding: 0px 12px;margin-top: -1px;">Get</button></div></div></div></div>';			
		
		$('#editProductBotDTContainer').append($(botModel));
	 	}
		
		/* if(!($('#editProductBotDTContainer').hasClass('tiles'))){
			$('#editProductBotDTContainer').addClass('tiles').css("margin-top","10px");
		 } */
	}
	
	/*  $(itemID).select2();
	 if(itemID == "#botConfiguration_ul"){
			dispalyDefaultValue(itemID,"Select");
	} */
}

function dispalyDefaultValue(itemID, value){
    var itemArray = $(itemID).find('option');
	for(var i=0; i< itemArray.length; i++){ 
	  if(value == itemArray[i].id){			  
		  $(itemID).select2("val",$(itemID).find('option')[i].value);
		  break;
	  }
	} 
}

function getCustomFieldsAndValueOfBot(botTypeId, productBotId, productBotName){
	
	var jsonObj={"Title":"Configuration For Bot : [ "+productBotId+" ] "+productBotName,
		"subTitle": "",
		"url": "data/data.json",
		"columnGrouping":2,
		"containerSize": "medium",
		"componentUsageTitle":"customFields",
		"entityId":"74",
		"entityTypeId":botTypeId,
		"entityInstanceId":productBotId,
		"parentEntityId":"0",
		"parentEntityInstanceId":"0",
		"engagementId":"0",
		"productId":"0",
		"singleFrequency":"customFieldSingleFrequencyContainer",
		"multiFrequency":"customFieldMultiFrequencyContainer",
	};
	CustomFieldGropings.init(jsonObj);
}

function getCustomFieldsAndValueOfBotCommand(commandMasterId, commandId, commandName){
	
	var jsonObj={"Title":"Configuration For Bot Command : [ "+commandId+" ] "+commandName,
		"subTitle": "",
		"url": "data/data.json",
		"columnGrouping":2,
		"containerSize": "medium",
		"componentUsageTitle":"customFields",
		"entityId":"75",
		"entityTypeId":commandMasterId,
		"entityInstanceId":commandId,
		"parentEntityId":"0",
		"parentEntityInstanceId":"0",
		"engagementId":"0",
		"productId":"0",
		"singleFrequency":"customFieldSingleFrequencyContainer",
		"multiFrequency":"customFieldMultiFrequencyContainer",
	};
	CustomFieldGropings.init(jsonObj);
}

function executeBot(productBotId){
	openLoaderIcon();
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : 'bot.execution?productBotId='+productBotId,
		dataType : 'json',
		success : function(data) {
			//response = data.Records;
			callAlert("Execution completed");
		},
		error : function(data){
			closeLoaderIcon();
		},
		complete : function(data){
			closeLoaderIcon();
		}
	});
}


function scheduleBotUsingCronGen(title, subTitle, botId, rowType){	
	var	jsonBotsTabObj={"Title": title,	
							"SubTitle": subTitle,
							"rowID": botId,
							"rowType": rowType,
		};
	
	 Scheduling.init(jsonBotsTabObj);	
}

function listBotExecutionAuditHistory(botId, botName){
	var url='administration.event.list?sourceEntityType=Bot&sourceEntityId='+botId+'&jtStartIndex=0&jtPageSize=10000';
	var jsonObj={"Title":"Bot Execution History : [ "+botId+" ] "+botName,
			"botId": botId,
			"botName": botName,
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,
			"componentUsageTitle":"botExecutionAudit",
	};
	SingleDataTableContainer.init(jsonObj);
}
var botBuilderCommandsFlag=false;
var botCommandMasterIds = [];
function displayBotMasterScriptEditor(botMasterId,botName){
	botCommandMasterIds = [];
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : 'load.botPlan.by.BotMasterId?botMasterId='+botMasterId,
		dataType : 'json',
		success : function(data) {
			var botPlan = '';
			if(typeof data.Message != 'undefined'){
				var botPlanDetails = data.Message.split('~~~');
				botPlan = botPlanDetails[0];
				if(botPlanDetails.length > 1){
					var botCmdMasterIds = botPlanDetails[1].split(',');
					if(typeof botCmdMasterIds != 'undefined'){
						$.each(botCmdMasterIds, function(index, value){
							botCommandMasterIds.push(value);
						});
					}
				}
			}
			
			var jsonObj={"Title":"Bot Plan Builder : "+botName,
				 "subTitle": "BotId : "+botMasterId, 
				 "botId":botMasterId,
				 "botName":botName,
				 "BotCommandscript": botPlan,
				 "isBotMaster":true,
				 "isEditableCommands":false,
				 "keywords":"",
				 "mode":"gherkin",
				 gutters: ["CodeMirror-lint-markers", "CodeMirror-foldgutter"],
				 lint: true,
				 foldGutter: true
			};
			CodeEditorForBotCommandScript.init(jsonObj);
				
		},
		error : function(data){
			closeLoaderIcon();
		},
		complete : function(data){
			closeLoaderIcon();
		}
	});
	
}

var botBuilderCommandIds = [];
function displayBotScriptEditor(botId,botName){
	botBuilderCommandIds = [];
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : 'load.botPlan.by.BotId?botId='+botId,
		dataType : 'json',
		success : function(data) {
			var botCommands = '';
			if(typeof data.Message != 'undefined'){
				var botCommandDetails = data.Message.split('~~~');
				botCommands = botCommandDetails[0];
				if(botCommandDetails.length > 1){
					botBuilderCommandsFlag=true;
					var productBotBuilderCommandIds = botCommandDetails[1].split(',');
					if(typeof productBotBuilderCommandIds != 'undefined'){
						$.each(productBotBuilderCommandIds, function(index, value){
							botBuilderCommandIds.push(value);
						});
					}
				}
			}
			
			//console.log("Commands:"+botBuilderCommandIds);
			
			var jsonObj={"Title":"Bot Plan Builder : "+botName,
					 "subTitle": "BotId : "+botId, 
					 "botId":botId,
					 "botName":botName,
					 "BotCommandscript": botCommands,
					 "isBotMaster":false,
					 "isEditableCommands":false,
					 "keywords":"",
					 "mode":"gherkin",
					 gutters: ["CodeMirror-lint-markers", "CodeMirror-foldgutter"],
					 lint: true,
					 foldGutter: true
				};
				CodeEditorForBotCommandScript.init(jsonObj);
				
		},
		error : function(data){
			closeLoaderIcon();
		},
		complete : function(data){
			closeLoaderIcon();
		}
	});
	
	
}


function getBotHelp(description, createdName, createdOn){
	$('#editProductBotContainer').append($('.botHelpPopup'));
	var xPos, yPos;
	var popupWidth = 490;
	xPos = event.pageX;		
	yPos = event.pageY - 145;
	if(event.pageX > 900){
		xPos = event.pageX - popupWidth;	
	}else if (event.pageY > 350){
		yPos = 200;
	}
	$('.botHelpPopup').css({'left':xPos,'top':yPos});
	$('.botHelpPopup, .botHelpPopupCotainer').removeClass('hidden');	
	
	$(".botHelpPopup .descriptionlabel").empty().html("Description");
	$(".botHelpPopup .descriptionvalue").empty().html(description);
	$(".botHelpPopup .creatednamelabel").empty().html("Created By");
	$(".botHelpPopup .creatednamevalue").empty().html(createdName);
	$(".botHelpPopup .createdonlabel").empty().html("Created On");
	$(".botHelpPopup .createdonvalue").empty().html(createdOn);
}

var isBotCommandMasterPopupCreated = false;
var customFieldConfigForBotCommandMasterPopup = "";
function getCustomFieldConfigForBotCommandMasterPopup(){
	customFieldConfigForBotCommandMasterPopup = '<div id="div_customFieldConfigForBotCommandMasterPopup" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">'
				+'<div class="modal-full">'
				+'<div class="modal-content">'
				+'<div class="modal-header" style="padding-bottom: 5px;">'
				+'<button type="button" class="close" title="Press Esc to close" aria-hidden="true" onclick="closeCustomFieldConfigForBotCommandMasterPopup(\'div_customFieldConfigForBotCommandMasterPopup\')"></button>'
				+'<h4 class="modal-title theme-font"></h4>'
				+'</div>'
				+'<div class="modal-body">'					
				+'<div class="scroller" style="height: 500px; overflow: auto;" data-always-visible="1" data-rail-visible1="1">'
				+'<div id="customFieldsListContainerForBotCommandMaster"></div>'
				+'</div>'					 
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>';

	isBotCommandMasterPopupCreated = true;
}

function getCustomFieldConfigurationForBotCommandMaster(botCommandName, botCommandId){
	entityId = 75;
	entityTypeId = botCommandId;
	customFieldsListContainerId = 'customFieldsListContainerForBotCommandMaster';
	if(!isBotCommandMasterPopupCreated){
		getCustomFieldConfigForBotCommandMasterPopup();
	}
	var customFieldConfigForBotContainer = $('#codeBotCommandContainerTest');
	customFieldConfigForBotContainer.append(customFieldConfigForBotCommandMasterPopup);
	customFieldConfigForBotContainer.find(".modal-title").text('Manage Configuration Properties for Bot Command Master - '+' [ '+botCommandId+' ] '+botCommandName);
	getCustomFieldsOfEntity();
	$("#div_customFieldConfigForBotCommandMasterPopup").modal();
}




/* END Product Bots */
/*

//----- Making ajax request for the dataTable ----- 

function assignDataTableValuesForBot(url,tableValue, row, tr){
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
				data = data.Records;
			}
			
			if(tableValue == "parentTable"){
				productManagementDTJsonData = data;
				if(!productManagementFlag){
					productVersionResults_Container(data, "240px", row);
				}else{				
					reloadDataTableHandler(data, productMgmProduct_oTable);
				}
				
			}else if(tableValue == "childTable1"){
				botCommandChild1_Container(data, row, tr);
				
			}else if(tableValue == "childTable2"){
				//productUserPermissionResults_Container(data, row, tr);
				
			}else if(tableValue == "childTable3"){
				//data = convertDTDateFormat(data, ["productBuildDate"]);
				//productVersionBuildResults_Container(data, row, tr);
				
			}else{
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

//----- Product Version Started -----

function botCommandChild1Table(){
	var childDivString = '<table id="botCommandChild1_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th class="dataTableChildHeaderTitleTH">Command ID</th>'+
			'<th class="dataTableChildHeaderTitleTH">Command Name</th>'+			
			'<th class="dataTableChildHeaderTitleTH"></th>'+
		'</tr>'+
	'</thead>'+
	'</table>';		
	
	return childDivString;	
}

function botCommandChild1_Container(data, row, tr){
	
	try{
		if ($("#dataTableSingleContainer_botCommand").children().length>0) {
			$("#dataTableSingleContainer_botCommand").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = botCommandChild1Table(); 			 
	$("#dataTableSingleContainer_botCommand").append(childDivString);
		
	productMgmProductVersion_oTable = $("#botCommandChild1_dataTable").dataTable( {
		 	"dom": '<"top"Bf<"clear">>rt<"bottom"ip<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	       
	       "fnInitComplete": function(data) {
		   },  
		   select: true,
		   buttons: [
	             	//{ extend: "create"},	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Bot Command',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Bot Command',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Bot Command',
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
           { mData: "botCommandId",className: 'editable', sWidth: '5%' },		
           { mData: "botCommandName",className: 'editable', sWidth: '15%' },          
            { mData: null,				 
            	bSortable: false,
            	sWidth: '4%',
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
  	       		'<button style="border: none; background-color: transparent; outline: none;">'+
  	       				'<img src="css/images/list_metro.png" class="details-control imgVersion1" title="Bot Command Event Result" style="margin-left: 5px;"></button>'+  	       		
  	       		'</div>');	      		
        		 return img;
            	}
            },
       ],       
       "oLanguage": {
           "sSearch": "Search all columns:"
       },     
	}); 
	// ------
	 $(function(){ // this will be called when the DOM is ready
		 
			 
	 // ----- product UserPermission child table -----
	  $('#dataTableSingleContainer_botCommand').on('click', 'td button .imgVersion1', function () {
		  var tr = $(this).closest('tr');
		  var row = productMgmProductVersion_oTable.DataTable().row(tr);
		  
		  productVersionSelectedTr = tr;		  
		  productVersionSelectedRow = row;
		  
		  productVersionBuildResultHandler(row.data().productVersionListId, row, tr);
		  $("#productVersionBuildPdMgmContainer").modal();
	}); 
    
	 });
}
*/