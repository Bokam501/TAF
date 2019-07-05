var totalUnMappedTCList=0;
var unMapListCountAsWhenModified = 0;
var getJSONObject;
var idName;
	
var listItems = function() {
	
   var initialise = function(jsonObj){	    
	    totalUnMappedTCList=0;
	    unMapListCountAsWhenModified=0;
	    getJSONObject="";		
		$('#selectAllUnmappedTestcasesRowCount').find('option[value="50"]').attr("selected", true);
		if(recentBuildFlag!="Workpackage"){
			testCaseMappingtoFeatureTotal(jsonObj);	   
		}else{
			workpackageFeatureTotal(jsonObj);
		}
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

	function closePopupDragListItems() {	
		$("#buildListItemsContainer").fadeOut("normal");	
		$("#wpkgeListItemsContainer").fadeOut("normal");	
		$("#div_PopupBackground").fadeOut("normal");
	}
	
	function setDropDownName(value){
		var starting = 0;
		var ending = 0;
		var currentPageNo=0;
		var pageNo = Number($(paginationCase).find('span.current').text());
		if ((value*pageNo) > totalUnMappedTCList) {
			currentPageNo = 0;		
		}else{
			currentPageNo = ((value*pageNo)-value);			
		}
		getJSONObject.leftDragItemsPageSize = value;		
		initMappingtotestCase(starting,currentPageNo,getJSONObject,false);
	}
	
	function updateBadgeUnMappedTestCasesCount(value, totalcount){
		var tempValue = $(badgeUnMappedTestCases).text();			
		var firstHalf = tempValue.split(' of ')[0];
		var firstSubHalf = firstHalf.replace(firstHalf.split('/')[1],value);
		tempValue = tempValue.replace(tempValue.split(' of ')[0],firstSubHalf);		
		tempValue = tempValue.replace(tempValue.split(' of ')[1],totalcount);						
		$(badgeUnMappedTestCases).text(tempValue);
	}
	
	// bool is used for initial loading for url concatation
	function initMappingtotestCase(startIndex,startCount,jsonObj,bool){	
		$("#leftDragItemsContainer").empty();	
		
		$("#dragDropListItemLoaderIcon").show();
		$('#leftDragItemsContainer').addClass("ptrNone");	

		var url=jsonObj.leftItemPaginationUrl+"&jtStartIndex="+startCount+"&jtPageSize="+jsonObj
		.leftDragItemsPageSize;
		
		if(bool){
			url=jsonObj.leftDragItemsDefaultLoadingUrl;
		}

		$.ajax({
			type: "POST",
	        contentType: "application/json; charset=utf-8",
			url : url,
			dataType : 'json',
			complete : function(data){
				if(data != undefined){			
					$("#dragDropListItemLoaderIcon").hide();
					$('#leftDragItemsContainer').removeClass("ptrNone");	

			    		allUnmappedTestcasesFun(startIndex,jsonObj);
				}
			},
			success : function(data) {
				displayRecords(data,jsonObj);		
			},
			error: function (data){
				$("#dragDropListItemLoaderIcon").hide();
			}
		});	
	}	

	// bool is used for initial loading for url concatation
	function initMappingtoworkpackage(startIndex,startCount,jsonObj,bool){	
		$("#wpkgeItemsContainer").empty();	
		
		$("#dragDropListItemLoaderIconWorkpackage").show();
		$('#wpkgeItemsContainer').addClass("ptrNone");	

		var url=jsonObj.leftItemPaginationUrl+"&jtStartIndex="+startCount+"&jtPageSize="+jsonObj
		.leftDragItemsPageSize;
		
		if(bool){
			url=jsonObj.leftDragItemsDefaultLoadingUrl;
		}

		$.ajax({
			type: "POST",
	        contentType: "application/json; charset=utf-8",
			url : url,
			dataType : 'json',
			complete : function(data){
				if(data != undefined){			
					$("#dragDropListItemLoaderIconWorkpackage").hide();
					$('#wpkgeItemsContainer').removeClass("ptrNone");	

			    		allUnmappedTestcasesFun(startIndex,jsonObj);
				}
			},
			success : function(data) {
				displayRecordsWorkpackage(data,jsonObj);		
			},
			error: function (data){
				$("#dragDropListItemLoaderIconWorkpackage").hide();
			}
		});	
	}	
	
	function testCaseMappingtoFeatureTotal(jsonObj)	
	{			
		var leftSpan = $("#leftItemsHeaderBuild").find("h5 span");
		$("#leftItemsHeaderBuild").find("h5 span").text(jsonObj.leftDragItemsHeaderName);
		$("#leftItemsHeaderBuild").find("h5").append(leftSpan);
		
		var rightSpan = $("#rightDragItemsHeader").find("h5 span");
		$("#rightDragItemsHeader").find("h5").text(jsonObj.rightDragItemsHeaderName);
		$("#rightDragItemsHeader").find("h5").append(rightSpan);
		
		$.ajax({
			type: "POST",
	        contentType: "application/json; charset=utf-8",
			url : jsonObj.leftDragItemsTotalUrl,
			dataType : 'json',		
			success : function(data) {				
				//totalUnMappedTCList = data.Record.unMappedTCCount;
				
				$("#leftDragItemsTotalCount").text('');
				$("#leftDragItemsTotalCount").text(totalUnMappedTCList);
				unMapListCountAsWhenModified = jsonObj.leftDragItemsPageSize;
		
				testCaseMappingtoFeature(jsonObj);
			}
		});	
	}

	function workpackageFeatureTotal(jsonObj)	
	{			
		var leftSpan = $("#leftItemsHeaderWpkge").find("h5 span");
		$("#leftItemsHeaderWpkge").find("h5 span").text(jsonObj.leftDragItemsHeaderName);
		$("#leftItemsHeaderWpkge").find("h5").append(leftSpan);
		
		var rightSpan = $("#rightDragItemsHeader").find("h5 span");
		$("#rightDragItemsHeader").find("h5").text(jsonObj.rightDragItemsHeaderName);
		$("#rightDragItemsHeader").find("h5").append(rightSpan);
		
		$.ajax({
			type: "POST",
	        contentType: "application/json; charset=utf-8",
			url : jsonObj.leftDragItemsTotalUrl,
			dataType : 'json',		
			success : function(data) {				
				//totalUnMappedTCList = data.Record.unMappedTCCount;
				
				$("#leftDragItemsTotalCount").text('');
				$("#leftDragItemsTotalCount").text(totalUnMappedTCList);
				unMapListCountAsWhenModified = jsonObj.leftDragItemsPageSize;
		
				workpackagetoFeature(jsonObj);
			}
		});	
	}

	 //TestCase Mapping to Feature Starts
	 function testCaseMappingtoFeature(jsonObj){
		getJSONObject = jsonObj;
		$('#searchLeftDragItems').val('');
		$('#searchRightDragItems').val('');
		
		$('#searchLeftDragItems').keyup(function() {
			var txt=$('  #searchLeftDragItems').val();	
			if($('#searchLeftDragItems').value==''){				
				$("#leftDragItemsTotalCount").text(totalUnMappedTCList);
			}	
			var resArr = [];
			var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();		
				$('#leftDragItemsContainer li').show().filter(function() {	    	
		    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
		        if(!~text.indexOf(val) == false) resArr.push("item");	        
		    	  return !~text.indexOf(val);	    
		    }).hide();							
				$("#leftDragItemsTotalCount").text(+resArr.length+" / "+unMapListCountAsWhenModified);	
				if(txt==""){							
					$("#leftDragItemsTotalCount").text(unMapListCountAsWhenModified);			
				}
		});	
		
		$(".tilebody").empty();
		$(".tilebody").remove();
		document.getElementById("buildListItemsContainer").style.display = "inline-block";
		$("#buildListItemsContainer").find("h4").text(jsonObj.Title);
		
		var startIndex=0;
	    var startCount=0;	
		var rowCount = $("#selectAllUnmappedTestcasesRowCount").find('option:selected').val();		
		initMappingtotestCase(startIndex,startCount,jsonObj,true);
		$("#leftDragItemsContainer").empty();	
		
	}
	

	function workpackagetoFeature(jsonObj){
		getJSONObject = jsonObj;
		$('#searchLeftDragItemsWorkpackage').val('');
		$('#searchRightDragItemsWorkpackage').val('');
		
		$('#searchLeftDragItemsWorkpackage').keyup(function() {
			var txt=$('  #searchLeftDragItemsWorkpackage').val();	
			if($('#searchLeftDragItemsWorkpackage').value==''){				
				$("#leftDragItemsTotalCount").text(totalUnMappedTCList);
			}	
			var resArr = [];
			var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();		
				$('#wpkgeItemsContainer li').show().filter(function() {	    	
		    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
		        if(!~text.indexOf(val) == false) resArr.push("item");	        
		    	  return !~text.indexOf(val);	    
		    }).hide();							
				$("#leftDragItemsTotalCount").text(+resArr.length+" / "+unMapListCountAsWhenModified);	
				if(txt==""){							
					$("#leftDragItemsTotalCount").text(unMapListCountAsWhenModified);			
				}
		});	
		
		$(".tilebody").empty();
		$(".tilebody").remove();
		document.getElementById("wpkgeListItemsContainer").style.display = "inline-block";
		$("#wpkgeListItemsContainer").find("h4").text(jsonObj.Title);		

		var startIndex=0;
	    var startCount=0;	
		var rowCount = $("#selectAllUnmappedTestcasesRowCount").find('option:selected').val();		
		//initMappingtotestCase(startIndex,startCount,jsonObj,true);
		initMappingtoworkpackage(startIndex,startCount,jsonObj,true);
		$("#wpkgeItemsContainer").empty();			
	}


	var Prev = {start: 0,stop: 0}, 
	TabPage, Paging = [], CONT;
	var prevUnMappedRequest='';

	function allUnmappedTestcasesFun(index,jsonObj){
		var rowCount = jsonObj.leftDragItemsPageSize;
		
		var totalCurrentCount=0;
		var currentPageNo=0;
		var page = Number($(paginationCase).find('span.current').text());			
		currentPageNo=page;
		$("#paginationCase").show();
		if(totalUnMappedTCList<rowCount){
			$("#paginationCase").hide();
		}			
		if(page==0){
			currentPageNo=1;
		}			

		//Paging[0].setPage(1); // we dropped "page" support and need to run it by hand
	};
	
	function displayRecords(data,jsonObj)
	{
		var result = data.Records;
		$("#leftDragItemsContainer").empty();

		$("#badgeUnMappedTestCases").show();
		$("#selectAllUnmappedTestcasesRowCount").show();		
		
		if(result.length==0){
			$("#leftDragItemsContainer").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>"+jsonObj.noItems+"</b></span>");
				
			$("#leftDragItemsTotalCount").text(totalUnMappedTCList);
			$("#badgeUnMappedTestCases").hide();
			$("#selectAllUnmappedTestcasesRowCount").hide();
		}else{
		    var resultValue="";
			$.each(result, function(i,item){				
				resultValue = leftItemDislayListItemRecentBuilds(item, jsonObj);
				$("#leftDragItemsContainer").append(resultValue);
				
		 	});
			
			$("#leftDragItemsTotalCount").text(totalUnMappedTCList);
		}
	 }

	function displayRecordsWorkpackage(data,jsonObj)
	{
		var result = data.Records;
		$("#wpkgeItemsContainer	").empty();

		$("#badgeUnMappedTestCases").show();
		$("#selectAllUnmappedTestcasesRowCount").show();		
		
		if(result.length==0){
			$("#wpkgeItemsContainer").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>"+jsonObj.noItems+"</b></span>");
				
			$("#leftDragItemsTotalCount").text(totalUnMappedTCList);
			$("#badgeUnMappedTestCases").hide();
			$("#selectAllUnmappedTestcasesRowCount").hide();
		}else{
		    var resultValue="";
			$.each(result, function(i,item){				
				resultValue = leftItemDislayListItemWorkpackage(item, jsonObj);
				$("#wpkgeItemsContainer").append(resultValue);
				
		 	});
			
			//$("#leftDragItemsTotalCount").text(totalUnMappedTCList);
		}
	 }