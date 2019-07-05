
var operationYear = new Date().getFullYear();
var utilizationRange = 0;
var utilizationWeekRange = '';
var weeksName = new Array();
var weekEndName = new Array();
var weeksNameAll = new Array();
var weekEndNameAll = new Array();
var weeksCompleteNames = new Array();
function getWeeksName() {
	 var weeksNameList = new Array();
	 var completeWeeksName = new Array();
     $.ajax({
	            url : 'common.list.weeksName?year='+operationYear+'&weekRange='+utilizationWeekRange,
	 			dataType : 'json',
	 			error: function() {
	 			      callAlert("Unable to get the dates for the week");
	 			},
	            success : function(jsonData) {
	        	   	for (var i = 0; i < jsonData.Records.length; i++) {
		             	var dateName = jsonData.Records[i];		        
		             	
		             	var dateMonthArray = dateName.option.split(" ");
		             	var date = dateMonthArray[2];
		             	var month = dateMonthArray[1];
		             	var twoDigitYear = dateMonthArray[5].substr(-2);
		             	var weekNo = dateName.option.split("~")[1];		             	
		             	var weekNameLable = date+"-"+month+"-"+twoDigitYear+" (W "+weekNo+")";		             	
		             	weeksNameList.push(weekNameLable.replace("00:00","").replace(":",""));
		             	completeWeeksName.push(dateName.option.substr(0,3));
		             	
		             	dateMonthArray = dateName.value.split(" ");
		             	date = dateMonthArray[2];
		             	month = dateMonthArray[1];
		             	twoDigitYear = dateMonthArray[5].substr(-2);
		             	weekNo = dateName.value.split("~")[1];		             	
		             	weekNameLable = date+"-"+month+"-"+twoDigitYear+" (W "+weekNo+")";		             	
		             	weekEndName.push(weekNameLable.replace("00:00","").replace(":",""));
	        	   }
	               setCompleteAllWeeksName(completeWeeksName);	               	               
	               setWeeksName(weeksNameList, weekEndName);
	          }
	    });
};

function setWeeksName(dateNames, endDateNames){
	weeksName = dateNames;
	weekEndName = endDateNames;
	
	if(utilizationWeekRange == ''){
		weeksNameAll = dateNames;
		weekEndNameAll = endDateNames;
	}
	
}

function setCompleteAllWeeksName(completeWeeksName){
	weeksCompleteNames = completeWeeksName;
}

function setValuePresicion(value){
	if(value != "undefined" && value != 0  ){
		value = value.toFixed(2);
	}
	return value;
}

function getTeamUtilizationIndicator(allocationPercentage){
	if(allocationPercentage > 1){
		return 'red';
	}else if(allocationPercentage == 1){
		return 'green';
	}else if(allocationPercentage>0){
		return '#ff6600';
	}else{
		return 'blue';
	}
}

var teamUtilizationData = {};
function showTeamUtilization(containerId, labId, resourcePoolId, testFactoryId, productId, workpackageId, showResourcePool, scrollYValue,userId){
	var weekRangeSelection = $("#teamUtilizationWeekRangeFilter").val();
	if(weekRangeSelection == undefined){
		weekRangeSelection="";
	}
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url : 'resource.pool.resource.allocation.weekly?labId='+labId+'&resourcePoolId='+resourcePoolId+'&testFactoryId='+testFactoryId+'&productId='+productId+'&workpackageId='+workpackageId+'&allocationYear='+operationYear+'&userId='+userId+'&utilizationRange='+utilizationRange+'&recursiveWeeks='+weekRangeSelection,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			
			if(data.Result=="ERROR"){
     		    data = [];						
			}else{
				teamUtilizationData = data.Records;
				getTeamUtilizationHeaderAndFooterColumns(containerId);
				var columnValueMapping = getTeamUtilizationColumnMappings(containerId, labId, resourcePoolId, testFactoryId, productId, workpackageId);
				getTeamUtilization(containerId, labId, resourcePoolId, testFactoryId, productId, workpackageId, showResourcePool, scrollYValue, columnValueMapping, teamUtilizationData, userId);
			}
			closeLoaderIcon();
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	 });
	
}

var teamUtilization_oTable = '';
var teamUtilizationColumsAvailableIndex = [];
function getTeamUtilization(containerId, labId, resourcePoolId, testFactoryId, productId, workpackageId, showResourcePool, scrollYValue, teamUtilizationColumns, teamUtilizationData, userId){
	try{
		if ($('#teamUtilization_dataTable').length > 0) {
			$('#teamUtilization_dataTable').dataTable().fnDestroy(); 
		}
	} catch(e) {}
	
	teamUtilization_oTable = $('#teamUtilization_dataTable').dataTable( {
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
       "fnInitComplete": function(data) {    	   
    	   var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position     	  
     	  //var footerItems = $('#teamUtilization_dataTable_wrapper tfoot tr#teamUtilization_dataTable_filterRow th');
     	 var footerItems = $('#teamUtilization_dataTable_wrapper tfoot tr#teamUtilization_dataTable_filterRow th');
     	  
     	  footerItems.each( function () {   
   	    	    var i=$(this).index();
   	    	    var flag=false;
   	    	    var singleItem = $(footerItems).eq(i).find('div'); 
   	    	    for(var j=0; j < searchcolumnVisibleIndex.length; j++){
   	    	    	if(i == searchcolumnVisibleIndex[j]){
   	    	    		flag=true;
   	    	    		$(singleItem).css('height','0px');
    	    	    		$(singleItem).css('color','#4E5C69');
    	    	    		$(singleItem).css('line-height','12px');
   	    	    		break;
   	    	    	}else{
   	    	    		$(singleItem).css('height','0px');
    	    	    		$(singleItem).css('color','#4E5C69');
    	    	    		$(singleItem).css('line-height','12px');
   	    	    	}
   	    	    }
   	    	    
   	    	    if(searchcolumnVisibleIndex.length==0){
   	    	    	$(singleItem).css('height','0px');
     	    		$(singleItem).css('color','#4E5C69');
     	    		$(singleItem).css('line-height','12px');
   	    	    }
   	    	    
   	    	    if(!flag){
   	    	    	$(this).append( '<input type="text" name="'+data.aoColumns[i].mData+'" value="" style="width:100%" class="search_init" />');
   	    	    }
     	   });   	       	  
		   reInitializeDataTableTeamUtilization();
	   },  
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'Team Utilization Summay',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Team Utilization Summay',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Team Utilization Summay',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        orientation: 'landscape',
	                        pageSize: 'A0',
	                        footer: true
	                    },	                    
	                ],	                
	            },
	            'colvis'
         ], 
         columnDefs: [
          	  //{ "orderable": false, "targets": 0 },
              { targets: teamUtilizationColumsAvailableIndex, visible: true},
              { targets: '_all', visible: false }
          ],
                  
        aaData : teamUtilizationData,                 
	    aoColumns : teamUtilizationColumns,
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },
       "footerCallback" : function ( row, data, start, end, display ) {
    	    var api = this.api(), data;
    	    
    	    $.each(teamUtilizationColumsAvailableIndex, function(index, value){
    	    	if(value < 5){
    	    		 /*$( api.column( 4 ).footer() ).html(
     	         	    	'Total'
     	         	 );
    	    		 $( api.column( 5 ).footer() ).html(
    	    				api.column( 5 ).data().count()
      	         	 );*/
    	    		return;
    	    	}
    	    	// Remove the formatting to get integer data for summation
    	    	var intVal = function(i) {
					return typeof i === 'string' ? i
							.replace(/[\$,]/g, '') * 1
							: typeof i === 'number' ? i
									: 0;
				};
				// Total over all pages
				overAllWeekAllocation = api.column(
						value, {search:'applied'}).data().reduce(
						function(a, b) {
							return intVal(a) + intVal(b);
						}, 0);

				// Total over this page
				currentPageWeekAllocation = api.column(
						value, {search:'applied'}, {
							page : 'current'
						}).data().reduce(
						function(a, b) {
							return intVal(a)
									+ intVal(b);
						}, 0);
    	    	// Update footer
        	    $( api.column( value ).footer() ).html(setValuePresicion(overAllWeekAllocation));        	            	    
        	   // $('#teamUtilization_dataTable_sum_'+value).html(setValuePresicion(overAllWeekAllocation));
    	    });
    	    
    	}
    	   
	});
	
	$(function(){ // this will be called when the DOM is ready 
		
		new $.fn.dataTable.FixedColumns( teamUtilization_oTable, {
			leftColumns:5,
		});	
	    
		/*teamUtilization_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });*/
		
		$('#teamUtilization_dataTable_wrapper .dataTables_scrollFoot tfoot tr#teamUtilization_dataTable_filterRow th input').each( function (i) {
			this.visibleIndex = i;
		} );
		
		$('#teamUtilization_dataTable_wrapper .dataTables_scrollFoot tfoot tr#teamUtilization_dataTable_filterRow th input').keyup( function () {
			var visIndex = typeof this.visibleIndex == 'undefined' ? 0 : this.visibleIndex;				
			teamUtilization_oTable.fnFilter( this.value, visIndex );
		});
					
		/* Use the elements to store their own index */
		$('#teamUtilization_dataTable_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input').each( function (i) {
			this.visibleIndex = i;
		} );
		
		$('#teamUtilization_dataTable_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input').keyup( function () {
			var visIndex = typeof this.visibleIndex == 'undefined' ? 0 : this.visibleIndex;				
			teamUtilization_oTable.fnFilter( this.value, visIndex );
		});
		
		$("#teamUtilization_dataTable_length").css('margin-top','8px');
		$("#teamUtilization_dataTable_length").css('padding-left','35px');
	    
		var currentYear = new Date().getFullYear();
		var yearFilter = '<span><label style="margin: 5px;">Year :</label><select id="teamUtilizationYearFilter" style="margin: 5px;">';
		for(var i = (currentYear - 10); i <= (currentYear + 10); i++){
			if(operationYear == i){
				yearFilter += '<option value="'+i+'" selected>'+i+'</option>';
			}else{
				yearFilter += '<option value="'+i+'">'+i+'</option>';
			}
		}
		yearFilter += '</select><span>';
		$('#teamUtilization_dataTable_filter').append(yearFilter);
		
		
		$(document).off('change','#teamUtilizationYearFilter');
		$(document).on('change','#teamUtilizationYearFilter', function() {
			operationYear =  $("#teamUtilizationYearFilter").val();
			getWeeksName();
			showTeamUtilization(containerId, labId, resourcePoolId, testFactoryId, productId, workpackageId, showResourcePool, scrollYValue,userId);
		});
		
		
		var utilaizationRangeFilter = '<span><label style="margin: 5px;">Type </label><select id="utilaizationRangeFilter" style="margin: 5px;">';
		utilaizationRangeFilter = utilaizationRangeFilter+'<option value="2" >Overutilized</option>';
		utilaizationRangeFilter = utilaizationRangeFilter+'<option value="1" >Underutilized</option>';
		utilaizationRangeFilter = utilaizationRangeFilter+'<option value="0" >All</option>';
		utilaizationRangeFilter += '</select><span>';
		$('#teamUtilization_dataTable_filter').append(utilaizationRangeFilter);
		$('#utilaizationRangeFilter').val(utilizationRange);
		
		$(document).off('change','#utilaizationRangeFilter');
		$(document).on('change','#utilaizationRangeFilter', function() {
			utilizationRange =  $("#utilaizationRangeFilter").val();			
			$("#teamUtilizationWeekRangeFilterContainer").show();
			getWeeksName();
			showTeamUtilization(containerId, labId, resourcePoolId, testFactoryId, productId, workpackageId, showResourcePool, scrollYValue,userId);
		});
		
		var weekRangeDisplay = 'block';		
		var weekRangeFilter = '<span id="teamUtilizationWeekRangeFilterContainer" style="display : '+weekRangeDisplay+';float:right;"><label style="margin: 5px;">Weeks </label><input type="text" id="teamUtilizationWeekRangeFilter" value="'+utilizationWeekRange+'" style="margin: 5px;" />';
		//$('#teamUtilization_dataTable_filter').append(weekRangeFilter);
		$('#weekContainer').html('');
		$('#weekContainer').append(weekRangeFilter);
		
		$(document).off('blur','#teamUtilizationWeekRangeFilter');
		$(document).on('blur','#teamUtilizationWeekRangeFilter', function() {
			utilizationWeekRange =  $("#teamUtilizationWeekRangeFilter").val();
			getWeeksName();
			
			if($("#tabslistResource li.active").eq(0).text() =="Utilization"){
				getSelectedTabRecords(4);				
			}else{
				showTeamUtilization(containerId, labId, resourcePoolId, testFactoryId, productId, workpackageId, showResourcePool, scrollYValue,userId);
			}
		});	
		$("#teamUtilizationWeekRangeFilter").val(utilizationWeekRange);
	}); 
}
function getTeamUtilizationColumnMappings(containerId, labId, resourcePoolId, testFactoryId, productId, workpackageId){
	var resourceUtilizationColumns = [];
	
	resourceUtilizationColumns.push({ mData: "userCode",className: 'disableEditInline', sWidth: '5%'});
	resourceUtilizationColumns.push({ mData: "userName", sWidth: '5%',
		"render" : function (teamUtilizationData, teamUtilizationType, teamUtilizationFull) {
			return ('<span style="color : blue; cursor : pointer;" onclick="showUserProfile('+teamUtilizationFull['userId']+','+teamUtilizationFull['userRoleId']+')">'+teamUtilizationFull['userName']+'</span>');
		},
	});
	resourceUtilizationColumns.push({ mData: "userRoleName",className: 'disableEditInline', sWidth: '5%'});
	resourceUtilizationColumns.push({ mData: "userTypeName",className: 'disableEditInline', sWidth: '5%'});
	resourceUtilizationColumns.push({ mData: "resourcePoolName",className: 'disableEditInline', sWidth: '5%'});
		
	var columnMapping =	{ 
		mData: 'allWeeks',
		sWidth: '5%',
		"render" : function (teamUtilizationData, teamUtilizationType, teamUtilizationFull) {
			return ('<span style="padding-right:55px;padding-left:55px;">'+setValuePresicion(teamUtilizationFull['allWeeks'])+'</span>');
    	},
    };
	resourceUtilizationColumns.push(columnMapping);
	
	teamUtilizationColumsAvailableIndex = [0, 1, 2, 3, 4, 5];
	var teamUtilizationColumsIndex = 5;
	var fontColor = '';
	$.each(weeksName, function(index, value){
		var weekToMap = value.split("(W ")[1].split(")")[0];
		teamUtilizationColumsIndex++;
		teamUtilizationColumsAvailableIndex.push(teamUtilizationColumsIndex);
		columnMapping =	{ 
			mData: 'week'+(weekToMap),
			sWidth: '2%',
			"render" : function (teamUtilizationData, teamUtilizationType, teamUtilizationFull) {
				var indexOfWeekAvailable = $.inArray(Number(weekToMap), teamUtilizationFull['reservedWeeks']);
				if(indexOfWeekAvailable >= 0){
					if(teamUtilizationFull['userName'] == "Total"){
						fontColor = 'blue';
					}else{
						fontColor = getTeamUtilizationIndicator(teamUtilizationFull['week'+(weekToMap)]);
					}
					var reservedResourcePool = teamUtilizationFull['resourcePoolId'];
					var isFullView = false;
					var isCreatePopup = true;
					return ('<span style="padding-right:55px;padding-left:55px;color : '+fontColor+'; cursor : pointer;" onclick="showDetailedUtilizationOfResource(\''+containerId+'\','+labId+','+reservedResourcePool+','+ testFactoryId+','+ productId+','+ workpackageId+','+teamUtilizationFull['userId']+','+(weekToMap)+','+operationYear+',\''+teamUtilizationFull['userName']+'\',\''+teamUtilizationFull['userRoleName']+'\',\''+teamUtilizationFull['userTypeName']+'\',\''+isFullView+'\',\''+isCreatePopup+'\',\'detailedUtilizationOfResource_dataTable\',false)">'+setValuePresicion(teamUtilizationFull['week'+(weekToMap)])+'</span>');
				}else{
					return ('<span style="padding-right:55px;padding-left:55px;color : lightgray;" >'+setValuePresicion(teamUtilizationFull['week'+(weekToMap)])+'</span>');
				}
				
        	},
        };
		resourceUtilizationColumns.push(columnMapping);
	});
	
	return resourceUtilizationColumns;
}

function getTeamUtilizationHeaderAndFooterColumns(containerId){
	var teamUtilizationContainer = $('#'+containerId);
	teamUtilizationContainer.empty();
//	var teamUtilization_dtColumns = ['Resource&nbsp;Pool', 'User&nbsp;Code', 'User&nbsp;Name',  'User&nbsp;Role', 'User&nbsp;Type', 'All Weeks'];
	
	var teamUtilization_dtColumns = ['User&nbsp;Code','User&nbsp;Name','User&nbsp;Role', 'User&nbsp;Type','Resource&nbsp;Pool',  'All Weeks'];
	
	
	$.each(weeksName, function(index, value){
		teamUtilization_dtColumns.push(value);
	});
	
	var teamUtilizationTable = '<table id="teamUtilization_dataTable"  class="cell-border compact row-border" cellspacing="0" width="100%">';
	var teamUtilization_thead = '<thead><tr>';
	var teamUtilization_tfoot = '<tfoot><tr>';
	var teamUtilization_tfoot_sum = '<tr id="teamUtilization_dataTable_filterRow">';
	var columnIndex = 0;
	$.each(teamUtilization_dtColumns, function(index, value){		
		teamUtilization_thead += '<th class="sorting" style="text-align:center !important;">'+value+'</th>';
		teamUtilization_tfoot += '<th style="text-align:center !important;"></th>';
		teamUtilization_tfoot_sum += '<th id="'+'teamUtilization_dataTable_sum_'+columnIndex+'"></th>'; 
		columnIndex++;
	});
	teamUtilizationTable += teamUtilization_thead + '</tr></thead>'+ teamUtilization_tfoot + '</tr>'+ teamUtilization_tfoot_sum+ '</tr></tfoot></table>';
	teamUtilizationContainer.append(teamUtilizationTable);
}

var clearTimeoutDTTeamUtilization='';
function reInitializeDataTableTeamUtilization(){
	setTimeout(function(){				
		clearTimeoutDTTeamUtilization = teamUtilization_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTTeamUtilization);
	},500);
}

var teamUtilizationDetailedData = {};
function showDetailedUtilizationOfResource(containerId, labId, resourcePoolId, testFactoryId, productId, workpackageId, userId, workWeek, workYear, userName, uesrRoleName, userTypeName, isFullView, isCreatePopup, dataTableId, showResourceName){
	workWeek = $("#teamUtilizationWeekRangeFilter").val();
	if(workWeek == undefined){
		workWeek="";
	}
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url : 'resource.pool.resource.allocation.detailed.weekly?labId='+labId+'&resourcePoolId='+resourcePoolId+'&testFactoryId='+testFactoryId+'&productId='+productId+'&workpackageId='+workpackageId+'&userId='+userId+'&recursiveWeeks='+workWeek+'&workYear='+operationYear,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			
			if(data.Result=="ERROR"){
     		    data = [];						
			}else{
				teamUtilizationDetailedData = data.Records;
				getDetailedUtilizationOfResourceDataTable(containerId, userName, uesrRoleName, userTypeName, isCreatePopup, dataTableId, showResourceName);
				var columnValueMapping = getDetailedUtilizationOfResourceColumnMappings(isFullView, showResourceName, containerId, labId, resourcePoolId, testFactoryId, productId, workpackageId, userId, workWeek, workYear, userName, uesrRoleName, userTypeName, isFullView, isCreatePopup, dataTableId, showResourceName);
				getDetailedUtilizationOfResource('150', columnValueMapping, teamUtilizationDetailedData, dataTableId);
			}
			closeLoaderIcon();
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	 });
}




var detailedUtilizationOfResource_oTable = '';
var detailedUtilizationOfResourceColumsAvailableIndex = [];
function getDetailedUtilizationOfResource(scrollYValue, teamUtilizationDetailedColumns, teamUtilizationDetailedData, dataTableId){
	try{
		if ($('#'+dataTableId).length > 0) {
			$('#'+dataTableId).dataTable().fnDestroy(); 
		}
	} catch(e) {}
	
	detailedUtilizationOfResource_oTable = $('#'+dataTableId).dataTable( {
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
       "fnInitComplete": function(data) {
    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
    	  
    	  //var footerItems = $('#detailedUtilizationOfResource_dataTable_wrapper thead tr#detailedUtilizationOfResource_dataTable_filterRow th');
    	  var footerItems = $('#'+dataTableId+'_wrapper tfoot tr#'+dataTableId+'_filterRow th');
    	  
    	  footerItems.each( function () {   
  	    	    var i=$(this).index();
  	    	    var flag=false;
  	    	    var singleItem = $(footerItems).eq(i).find('div'); 
  	    	    for(var j=0; j < searchcolumnVisibleIndex.length; j++){
  	    	    	if(i == searchcolumnVisibleIndex[j]){
  	    	    		flag=true;
  	    	    		$(singleItem).css('height','0px');
   	    	    		$(singleItem).css('color','#4E5C69');
   	    	    		$(singleItem).css('line-height','12px');
  	    	    		break;
  	    	    	}else{
  	    	    		$(singleItem).css('height','0px');
   	    	    		$(singleItem).css('color','#4E5C69');
   	    	    		$(singleItem).css('line-height','12px');
  	    	    	}
  	    	    }
  	    	    
  	    	    if(searchcolumnVisibleIndex.length==0){
  	    	    	$(singleItem).css('height','0px');
    	    		$(singleItem).css('color','#4E5C69');
    	    		$(singleItem).css('line-height','12px');
  	    	    }
  	    	    
  	    	    if(!flag){
  	    	    	$(this).prepend( '<input type="text" name="'+data.aoColumns[i].mData+'" value="" style="width:100%" class="search_init" />');
  	    	    }
    	   });   	
	  
    	  reInitializeDataTableDetailedUtilizationOfResource();
	   },  
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'Resource Utilization Details',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Resource Utilization Details',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Resource Utilization Details',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        orientation: 'landscape',
	                        pageSize: 'A0',
	                        footer: true
	                    },	                   
	                ],	                
	            },
	            'colvis'
         ], 
         columnDefs: [
          	  //{ "orderable": false, "targets": 0 },
              { targets: detailedUtilizationOfResourceColumsAvailableIndex, visible: true},
              { targets: '_all', visible: false }
          ],
                  
        aaData : teamUtilizationDetailedData,                 
	    aoColumns : teamUtilizationDetailedColumns,
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },
       "footerCallback" : function ( row, data, start, end, display ) {
   	    var api = this.api(), data;
	    
	    $.each(detailedUtilizationOfResourceColumsAvailableIndex, function(index, value){
	    	if(value < 8){
	    		$( api.column( 7 ).footer() ).html(
 	         	    	'Total'
 	         	 );
	    		 $( api.column( 8 ).footer() ).html(
	    				api.column( 8 ).data().count()
  	         	 );
	    		return;
	    	}
	    	// Remove the formatting to get integer data for summation
    	    var intVal = function ( i ) {
    	        return typeof i === 'string' ?
    	            i.replace(/[\$,]/g, '')*1 :
    	            typeof i === 'number' ?
    	                i : 0;
    	    };
    	    // Total over all pages
    	    overAllWeekAllocation = api.column(
					value, {search:'applied'}).data().reduce(
					function(a, b) {
						return intVal(a) + intVal(b);
					}, 0);

    	    // Total over this page
    	    currentPageWeekAllocation = api.column(
					value, {search:'applied'}, {
						page : 'current'
					}).data().reduce(
					function(a, b) {
						return intVal(a)
								+ intVal(b);
					}, 0);

    	    // Update footer
    	    /*$( api.column( value ).footer() ).html(
    	    	overAllWeekAllocation
    	    );*/
    	    
    	    $(api.column(value).footer()).html(setValuePresicion(overAllWeekAllocation));
	    });
	    
	}
    	   
	});
	
	$(function(){ // this will be called when the DOM is ready		
		
		new $.fn.dataTable.FixedColumns( detailedUtilizationOfResource_oTable, {
		    leftColumns: 8,
		});
		
		$('#'+dataTableId+'_wrapper .dataTables_scrollFoot tfoot tr#'+dataTableId+'_filterRow th input').each( function (i) {
			this.visibleIndex = i;
		} );
		
		$('#'+dataTableId+'_wrapper .dataTables_scrollFoot tfoot tr#'+dataTableId+'_filterRow th input').keyup( function () {
			var visIndex = typeof this.visibleIndex == 'undefined' ? 0 : this.visibleIndex;				
			detailedUtilizationOfResource_oTable.fnFilter( this.value, visIndex );
		});
		
		/* Use the elements to store their own index */
		$('#'+dataTableId+'_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input').each( function (i) {
			this.visibleIndex = i;
		} );
		
		$('#'+dataTableId+'_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input').keyup( function () {
			var visIndex = typeof this.visibleIndex == 'undefined' ? 0 : this.visibleIndex;				
			detailedUtilizationOfResource_oTable.fnFilter( this.value, visIndex );
		});
		
		$("#"+dataTableId+"_length").css('margin-top','8px');
		$("#"+dataTableId+"_length").css('padding-left','35px');
		
		var currentYear = new Date().getFullYear();
		var yearFilter = '<span><label style="margin: 5px;">Year :</label><select id="teamUtilizationYearFilter" style="margin: 5px;">';
		for(var i = (currentYear - 10); i <= (currentYear + 10); i++){
			if(operationYear == i){
				yearFilter += '<option value="'+i+'" selected>'+i+'</option>';
			}else{
				yearFilter += '<option value="'+i+'">'+i+'</option>';
			}
		}
		yearFilter += '</select><span>';
		$('#teamUtilization_dataTable_filter').append(yearFilter);
		
		
		$(document).off('change','#teamUtilizationYearFilter');
		$(document).on('change','#teamUtilizationYearFilter', function() {
			operationYear =  $("#teamUtilizationYearFilter").val();
			getWeeksName();
			showTeamUtilization(containerId, labId, resourcePoolId, testFactoryId, productId, workpackageId, showResourcePool, scrollYValue,userId);
		});
		
		
		var utilaizationRangeFilter = '<span><label style="margin: 5px;">Type </label><select id="utilaizationRangeFilter" style="margin: 5px;">';
		utilaizationRangeFilter = utilaizationRangeFilter+'<option value="2" >Overutilized</option>';
		utilaizationRangeFilter = utilaizationRangeFilter+'<option value="1" >Underutilized</option>';
		utilaizationRangeFilter = utilaizationRangeFilter+'<option value="0" >All</option>';
		utilaizationRangeFilter += '</select><span>';
		$('#teamUtilization_dataTable_filter').append(utilaizationRangeFilter);
		$('#utilaizationRangeFilter').val(utilizationRange);
		
		$(document).off('change','#utilaizationRangeFilter');
		$(document).on('change','#utilaizationRangeFilter', function() {
			utilizationRange =  $("#utilaizationRangeFilter").val();			
			$("#teamUtilizationWeekRangeFilterContainer").show();
			getWeeksName();
			getSelectedTabRecords(4);
			//showTeamUtilization(containerId, labId, resourcePoolId, testFactoryId, productId, workpackageId, showResourcePool, scrollYValue,userId);
		});
		
		var weekRangeDisplay = 'block';		
		var weekRangeFilter = '<span id="teamUtilizationWeekRangeFilterContainer" style="display : '+weekRangeDisplay+';float:right;"><label style="margin: 5px;">Weeks </label><input type="text" id="teamUtilizationWeekRangeFilter" value="'+utilizationWeekRange+'" style="margin: 5px;" />';
		$('#weekContainer').html('');
		$('#weekContainer').append(weekRangeFilter);
		
		$(document).off('blur','#teamUtilizationWeekRangeFilter');
		$(document).on('blur','#teamUtilizationWeekRangeFilter', function() {
			utilizationWeekRange =  $("#teamUtilizationWeekRangeFilter").val();
			getWeeksName();
			
			if($("#tabslistResource li.active").eq(0).text() =="Utilization"){
				getSelectedTabRecords(4);				
			}else{
				showTeamUtilization(containerId, labId, resourcePoolId, testFactoryId, productId, workpackageId, showResourcePool, scrollYValue,userId);
			}
		});	
		$("#teamUtilizationWeekRangeFilter").val(utilizationWeekRange);
		
	}); 
}

function reInitializeDataTableDetailedUtilizationOfResource(){

	setTimeout(function(){				
		detailedUtilizationOfResource_oTable.DataTable().columns.adjust().draw();		
	},200);
}


var isPopupCreated = false;
var detailedUtilizationOfResourcePopup = "";
function getDetailedUtilizationOfResourcePopup(){
	detailedUtilizationOfResourcePopup = '<div id="div_detailedUtilizationOfResourcePopup" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">'
				+'<div class="modal-full">'
				+'<div class="modal-content">'
				+'<div class="modal-header" style="padding-bottom: 5px;">'
				+'<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>'
				+'<h4 class="modal-title theme-font"></h4>'
				+'</div>'
				+'<div class="modal-body">'					
				+'<div class="scroller" style="height: 500px; overflow: auto;" data-always-visible="1" data-rail-visible1="1">'
				+'<div id="detailedUtilizationOfResource"></div>'
				+'</div>'					 
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>';

	isPopupCreated = true;
}



function getDetailedUtilizationOfResourceDataTable(containerId, userName, userRoleName, userTypeName, isCreatePopup, dataTableId, showResourceName){
	var detailedUtilizationOfResourceContainer;
	if(isCreatePopup){
		if(!isPopupCreated){
			getDetailedUtilizationOfResourcePopup();
		}
		var teamUtilizationContainer = $('#'+containerId);
		teamUtilizationContainer.append(detailedUtilizationOfResourcePopup);
		teamUtilizationContainer.find(".modal-title").text('Utilization Details of Resource - '+userName+' [ '+userRoleName+' - '+userTypeName+' ]');
		detailedUtilizationOfResourceContainer = teamUtilizationContainer.find('#detailedUtilizationOfResource');
		
	}else{
		detailedUtilizationOfResourceContainer = $('#'+containerId);
	}
	if(detailedUtilizationOfResourceContainer != 'undefined' && detailedUtilizationOfResourceContainer != null){
		detailedUtilizationOfResourceContainer.empty();
	}
	$('#'+dataTableId).remove();
	
	var detailedUtilizationOfResource_dtColumns = [];
	if(showResourceName){
		detailedUtilizationOfResource_dtColumns = ['Resource Name', 'User Code', 'Engagement', 'Resource Pool', 'Product', 'Workpackage', 'Shift',  'Reserved for Role', 'Reserved for UserType', 'Reserved for Skill',  'Utilization'];
	}else{
		detailedUtilizationOfResource_dtColumns = ['Engagement', 'Resource Pool', 'Product', 'Workpackage', 'Shift',  'Reserved for Role', 'Reserved for UserType', 'Reserved for Skill',  'Utilization'];
	}
	
	
	$.each(weeksName, function(index, value){
		detailedUtilizationOfResource_dtColumns.push(value);
	});
	
	var detailedUtilizationOfResourceTable = '<table id="'+dataTableId+'"  class="cell-border compact row-border" cellspacing="0" width="100%">';
	var detailedUtilizationOfResource_thead = '<thead><tr>';
	var detailedUtilizationOfResource_tfoot = '<tfoot><tr>';
	var detailedUtilizationOfResource_filter_row = '<tr id="'+dataTableId+'_filterRow">';
	$.each(detailedUtilizationOfResource_dtColumns, function(index, value){
		detailedUtilizationOfResource_thead += '<th class="sorting">'+value+'</th>';
		detailedUtilizationOfResource_tfoot += '<th></th>';
		detailedUtilizationOfResource_filter_row += '<th></th>';
	});
	detailedUtilizationOfResourceTable += detailedUtilizationOfResource_thead + '</tr>' + '</thead>'+ detailedUtilizationOfResource_tfoot + '</tr>'+ detailedUtilizationOfResource_filter_row + '</tfoot></table>';
	detailedUtilizationOfResourceContainer.append(detailedUtilizationOfResourceTable);
	if(isCreatePopup){
		$("#div_detailedUtilizationOfResourcePopup").modal();
	}
}




function getDetailedUtilizationOfResourceColumnMappings(isFullView, showResourceName, containerId, labId, resourcePoolId, testFactoryId, productId, workpackageId, userId, workWeek, workYear, userName, uesrRoleName, userTypeName, isFullView, isCreatePopup, dataTableId, showResourceName){
	var detailedUtilizationOfResourceColumns = [];
	
	if(showResourceName){
		detailedUtilizationOfResourceColumns.push({ mData: "userName", sWidth: '5%'});
		detailedUtilizationOfResourceColumns.push({ mData: "userCode", sWidth: '5%'});
		detailedUtilizationOfResourceColumsAvailableIndex = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
	}else{
		detailedUtilizationOfResourceColumsAvailableIndex = [0, 1, 2, 3, 4, 5, 6, 7, 8];
	}
	
	detailedUtilizationOfResourceColumns.push({ mData: "testFactoryName", sWidth: '5%'});
	detailedUtilizationOfResourceColumns.push({ mData: "resourcePoolName", sWidth: '5%'});
	detailedUtilizationOfResourceColumns.push({ mData: "productName", sWidth: '5%'});
	detailedUtilizationOfResourceColumns.push({ mData: "workPackageName", sWidth: '5%'});
	detailedUtilizationOfResourceColumns.push({ mData: "shiftName", sWidth: '5%'});
	detailedUtilizationOfResourceColumns.push({ mData: "userRoleName", sWidth: '5%'});
	detailedUtilizationOfResourceColumns.push({ mData: "userTypeName", sWidth: '5%'});
	detailedUtilizationOfResourceColumns.push({ mData: "skillName", sWidth: '5%'});
	detailedUtilizationOfResourceColumns.push({ mData: "reservedResourceCount", sWidth: '5%'});
	
	var columnMapping =	{ 
		mData: 'allWeeks',
		sWidth: '5%',
		"render" : function (teamUtilizationDetailedData, teamUtilizationDetailedType, teamUtilizationDetailedFull) {
			return ('<span>'+setValuePresicion(teamUtilizationDetailedFull['allWeeks'])+'</span>');
    	},
    };
	
	
	if(isFullView){
		//var teamUtilizationDetailedColumsIndex = 7;
		var teamUtilizationDetailedColumsIndex = detailedUtilizationOfResourceColumns.length-1;
		
		
		var fontColor = '';
		$.each(weeksName, function(index, value){
			var weekToMap = value.split("(W ")[1].split(")")[0];
			teamUtilizationDetailedColumsIndex++;
			detailedUtilizationOfResourceColumsAvailableIndex.push(teamUtilizationDetailedColumsIndex);
			columnMapping =	{ 
				mData: 'week'+(weekToMap),
				sWidth: '2%',
				"render" : function (teamUtilizationDetailedFull, teamUtilizationDetailedType, teamUtilizationDetailedFull) {
					var indexOfWeekAvailable = $.inArray(Number(weekToMap), teamUtilizationDetailedFull['reservedWeeks']);
					if(indexOfWeekAvailable >= 0){
						if(teamUtilizationDetailedFull['userName'] == "Total"){
							fontColor = 'blue';
						}else{
							fontColor = getTeamUtilizationIndicator(teamUtilizationDetailedFull['week'+(weekToMap)]);
						}
						var reservedResourcePool = teamUtilizationDetailedFull['resourcePoolId'];
						var isFullView = false;
						var isCreatePopup = true;
						return ('<span style="padding-right:55px;padding-left:55px;color : '+fontColor+';">'+setValuePresicion(teamUtilizationDetailedFull['reservedResourceCountWk'+(weekToMap)])+'</span>');
					}else{
						return ('<span style="padding-right:55px;padding-left:55px;color : lightgray;" >'+setValuePresicion(teamUtilizationDetailedFull['reservedResourceCountWk'+(weekToMap)])+'</span>');
					}
					
	        	},
	        };
			detailedUtilizationOfResourceColumns.push(columnMapping);
		});
				
	}
	return detailedUtilizationOfResourceColumns;
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


/*function test(temp){
	temp = temp.replace(' IST','');
	var temp1 = temp.toString().split(' ');
	var temp2 = temp1[0]+' '+temp1[1]+' '+temp1[2]+' '+temp1[4]+' '+temp1[3];
	var d = new Date(temp2+' GMT-0800 (Pacific Standard Time)');
	var str = $.datepicker.formatDate('yy-mm-dd', d);
	//new Date("2015-03-25");
	return getWeekNumber(new Date(str));
}*/