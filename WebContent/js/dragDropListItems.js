var totalUnMappedTCList=0;
var unMapListCountAsWhenModified = 0;
var getJSONObject;
	
var DragDropListItems = function() {
	
   var initialise = function(jsonObj){	    
	    totalUnMappedTCList=0;
	    unMapListCountAsWhenModified=0;
	    getJSONObject="";		
		$('#selectAllUnmappedTestcasesRowCount').find('option[value="50"]').attr("selected", true);
	    testCaseMappingtoFeatureTotal(jsonObj);	   
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

	function closePopupDragListItems() {	
		$("#dragListItemsContainer").fadeOut("normal");	
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
	
	function testCaseMappingtoFeatureTotal(jsonObj)	
	{			
		var leftSpan = $("#leftDragItemsHeader").find("h5 span");
		$("#leftDragItemsHeader").find("h5").text(jsonObj.leftDragItemsHeaderName);
		$("#leftDragItemsHeader").find("h5").append(leftSpan);
		
		var rightSpan = $("#rightDragItemsHeader").find("h5 span");
		$("#rightDragItemsHeader").find("h5").text(jsonObj.rightDragItemsHeaderName);
		$("#rightDragItemsHeader").find("h5").append(rightSpan);
		
		$.ajax({
			type: "POST",
	        contentType: "application/json; charset=utf-8",
			url : jsonObj.leftDragItemsTotalUrl,
			dataType : 'json',		
			success : function(data) {				
				totalUnMappedTCList = data.Record.unMappedTCCount;
				
				$("#leftDragItemsTotalCount").text('');
				$("#leftDragItemsTotalCount").text(totalUnMappedTCList);
				unMapListCountAsWhenModified = jsonObj.leftDragItemsPageSize;
		
				testCaseMappingtoFeature(jsonObj);
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
		
		$('#searchRightDragItems').keyup(function() {
			var txt=$('#searchRightDragItems').val();
			if($(' #searchRightDragItems').value==''){			
				$("#rightDragItemsTotalCount").text($('#rightDragItemsContainer li').length);
			}	
			var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
		    var resArr = [];
			$('#rightDragItemsContainer li').show().filter(function() {	    	
		    	var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
		    	 if(!~text.indexOf(val) == false) resArr.push("item");
		        return !~text.indexOf(val);	        
		    }).hide();		
			$(" #rightDragItemsTotalCount").text(+resArr.length+" / "+$(' #rightDragItemsContainer li').length);		
			if(txt==""){
				$("#rightDragItemsTotalCount").text($('#rightDragItemsContainer li').length);		
			}
		});
		
		$(".tilebody").empty();
		$(".tilebody").remove();
		document.getElementById("dragListItemsContainer").style.display = "block";
		$("#dragListItemsContainer").find("h4").text(jsonObj.Title);
		
		var startIndex=0;
	    var startCount=0;	
		var rowCount = $("#selectAllUnmappedTestcasesRowCount").find('option:selected').val();		
		initMappingtotestCase(startIndex,startCount,jsonObj,true);
		$("#leftDragItemsContainer").empty();	
			
		url=jsonObj.rightDragItemsDefaultLoadingUrl;
		 $("#rightDragItemsContainer").empty();		 
		$.ajax({
			type: "POST",
	        contentType: "application/json; charset=utf-8",
			url : url,
			dataType : 'json',
			success : function(data) {						
				var listOfData = data.Records;
				$("#rightDragItemsContainer").empty();
				if(listOfData.length==0){			
				 $("#rightDragItemsContainer").append("<span style='color: black;' id='emptyListAll' ><b style='margin-left: 101px;'>"+jsonObj.noItems+"</b></span>");
					$("#rightDragItemsTotalCount").text(listOfData.length);					
					
				}else{
					$.each(listOfData, function(i,item){
						resultValue = rightItemDislayListItem(item, jsonObj);
						$("#rightDragItemsContainer").append(resultValue);										 
				 	});
					$("#rightDragItemsTotalCount").text(listOfData.length);
				}
			}
		});
	
		Sortable.create(document.getElementById("leftDragItemsContainer"), {			    
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
				var urlContact = leftDraggedItemURLChanges(draggeditem,jsonObj.componentUsageTitle)														
				var urlUnMapping = jsonObj.rightDragItemUrl+''+urlContact;				
				if($("#emptyListAll").length) $("#emptyListAll").remove();
								
				totalUnMappedTCList = totalUnMappedTCList+1;
				$("#leftDragItemsTotalCount").text(totalUnMappedTCList);
				
				var updatedCount = 	unMapListCountAsWhenModified+1;
				unMapListCountAsWhenModified =updatedCount;
				updateBadgeUnMappedTestCasesCount(unMapListCountAsWhenModified, totalUnMappedTCList);
				
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
				if($("#leftDragItemsContainer").children().length == 0) {
			 		$("#leftDragItemsContainer").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 101px;'>"+getJSONObject.noItems+"</b></span>");
			 		$("#leftDragItemsTotalCount").text(0);
			 		updateBadgeUnMappedTestCasesCount(unMapListCountAsWhenModified-1, unMapListCountAsWhenModified-1);			 		
			 	}else{					
					var updatedCount = 	unMapListCountAsWhenModified-1;
					unMapListCountAsWhenModified =updatedCount;					
					updateBadgeUnMappedTestCasesCount(unMapListCountAsWhenModified, totalUnMappedTCList);									
			 	}	
			
			},
			
			onStart:function(evt){},
			onSort:function(evt){ },
			onEnd: function(evt){ }
		});
		
		 Sortable.create(document.getElementById("rightDragItemsContainer"), {
				group: "words",
				animation: 150,
				onAdd: function (evt){
					totalUnMappedTCList = totalUnMappedTCList-1;
					$("#leftDragItemsTotalCount").text(totalUnMappedTCList);
					
					var draggeditem = trim(evt.item.innerText);					
					var urlContact = rightDraggedItemURLChanges(draggeditem,jsonObj.componentUsageTitle);														
					var urlMapping = jsonObj.leftDragItemUrl+''+urlContact;
				 	
				 	if($("#emptyListAll").length) $("#emptyListAll").remove();
					$("#rightDragItemsTotalCount").text($("#rightDragItemsContainer").children().length);
				 			
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
					if($("#rightDragItemsContainer").children().length == 0) {
				 		$("#rightDragItemsContainer").append("<span style='color: black;' id='emptyListAll' disabled><b style='margin-left: 101px;'>"+getJSONObject.noItems+"</b></span>");
				 		$("#rightDragItemsTotalCount").text(0);
				 	}else{
						$("#rightDragItemsTotalCount").text($("#rightDragItemsContainer").children().length);
				 	}
				
				},
				
				onStart:function(evt){ },
				onEnd: function(evt){}
			});
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
			
			Paging[0] = $(".a .toppagination").paging(totalUnMappedTCList, {								                    
			onSelect: function(page) {					
			    currentPageNo=page;
				if(totalUnMappedTCList < (rowCount*currentPageNo)){
					totalCurrentCount = totalUnMappedTCList;
				}else{
					totalCurrentCount = (rowCount*page);
				}
				unMapListCountAsWhenModified = totalCurrentCount;
			    var count = (((rowCount*page)-rowCount+1)+'/'+(totalCurrentCount));
				$(badgeUnMappedTestCases).text(count+" of "+totalUnMappedTCList);				
				if(index!=0)
				{					
					$("#dragDropListItemLoaderIcon").show();
					$('#leftDragItemsContainer').addClass("ptrNone");	
					var url=jsonObj.leftItemPaginationUrl+"&jtStartIndex="+((rowCount*page)-rowCount)+"&jtPageSize="+rowCount;
					console.log("--"+((rowCount*page)-rowCount));
					prevUnMappedRequest = $.ajax({
						type: "POST",
				        contentType: "application/json; charset=utf-8",
						url : url,
						dataType : 'json',
			            beforeSend : function()    {           
			            	if(prevUnMappedRequest!=''){ 
								prevUnMappedRequest.abort();
					        }
			            },					
						complete: function(data){						
							$("#dragDropListItemLoaderIcon").hide();
							$('#leftDragItemsContainer').removeClass("ptrNone");	
						},
						success: function(data) {						 						
							displayRecords(data,jsonObj);						
						}
					});
				}
				console.log('2 incrementing');
				index++;
			},
					
			onFormat: function (type) {		
				switch (type) {			
					case 'block':				
						if (!this.active)
						return '<span class="disabled">' + this.value + '<\/span>';
						else if (this.value != this.page)
						return '<em><a href="#' + this.value + '">' + this.value + '<\/a><\/em>';
						return '<span class="current">' + this.value + '<\/span>';
					
					case 'right':
					case 'left':				
						if (!this.active) {
						return '';
						}
						return '<a href="#' + this.value + '">' + this.value + '<\/a>';
						
					case 'next':				
						if (this.active) {
						return '<a href="#' + this.value + '" class="next">&raquo;<\/a>';
						}
						return '<span class="disabled">&raquo;<\/span>';
					
					case 'prev':					
						if (this.active) {
						return '<a href="#' + this.value + '" class="prev">&laquo;<\/a>';
						}
						return '<span class="disabled">&laquo;<\/span>';
					
					case 'first':				
						if (this.active) {
						return '<a href="#' + this.value + '" class="first">|&lt;<\/a>';
						}
						return '<span class="disabled">|&lt;<\/span>';
					
					case 'last':				
						if (this.active) {
						return '<a href="#' + this.value + '" class="prev">&gt;|<\/a>';
						}
						return '<span class="disabled">&gt;|<\/span>';
					
					case 'fill':
						if (this.active) {
							return "...";
						}
				}
				return ""; // return nothing for missing branches
			},
			format: '[< ncnnn! >]',
			perpage: rowCount,
			lapping: 0,
			page: null // we await hashchange() event
		});

		Paging[0].setPage(1); // we dropped "page" support and need to run it by hand
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
				resultValue = leftItemDislayListItem(item, jsonObj)
				$("#leftDragItemsContainer").append(resultValue);				
		 	});
			
			$("#leftDragItemsTotalCount").text(totalUnMappedTCList);
		}
	 }
