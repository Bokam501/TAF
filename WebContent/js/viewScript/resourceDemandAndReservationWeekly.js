function getDemandIndicator(demand,reservation){
	if(demand < reservation){
		return 'red';
	}else if(demand > 0 && reservation > 0 && demand == reservation){
		return 'green';
	}else if(demand > reservation){
		return '#ff6600';
	}else{
		return 'blue';
	}
}

var resourceDemandData = {};
var dataTableId = "";
function showResourceDemand(urlForlistDemandWeekly, containerId, scrollYValue,selectedTab){
	 var urlSplit = urlForlistDemandWeekly.split("&recursiveWeeks=");
	 urlForlistDemandWeekly = urlSplit[0]+"&recursiveWeeks="+utilizationWeekRange+"&weekYear="+operationYear+"&selectedTab="+selectedTab;
	openLoaderIcon();
	resourceDemandData = [];	
	 $.ajax({
		  type: "POST",
		  url : urlForlistDemandWeekly,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			
			if(data.Result=="ERROR"){
				callAlert(data.Message);
				data.Records = [];
				resourceDemandData = data.Records;
				getResourceDemandHeaderAndFooterColumns(containerId,selectedTab);
				getResourceDemand(containerId, scrollYValue, getResourceDemandColumnMappings(selectedTab), resourceDemandData, selectedTab,urlForlistDemandWeekly);
				
			}else{			
				resourceDemandData = data.Records;
				getResourceDemandHeaderAndFooterColumns(containerId,selectedTab);
				getResourceDemand(containerId, scrollYValue, getResourceDemandColumnMappings(selectedTab), resourceDemandData, selectedTab,urlForlistDemandWeekly);							
				getLatestDemandUploadHistory();				
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

var resourceDemandAndReservation_oTable = '';
var resourceDemandAndReservationColumsAvailableIndex = [];
function getResourceDemand(containerId, scrollYValue, resourceDemandColumns, resourceDemandData,selectedTab,urlForlistDemandWeekly){
	try{
		if ($('#'+dataTableId).length > 0) {
			$('#'+dataTableId).dataTable().fnDestroy(); 
		}
	} catch(e) {}
	
	resourceDemandAndReservation_oTable = $('#'+dataTableId).dataTable( {		
	    "dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',	
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,		
	   "bScrollCollapse": true,		
		"sScrollX": "100%",
       "sScrollXInner": "100%",
       "scrollY":"100%",       
       "fnInitComplete": function(data) {
    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position    	  
    	  var headerItems = $('#'+dataTableId+'_wrapper tfoot tr#'+dataTableId+'_filterRow th');    	  
    	  headerItems.each( function () {   
  	    	    var i=$(this).index();
  	    	    var flag=false;
  	    	    var singleItem = $(headerItems).eq(i).find('div'); 
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
	  
		   reInitializeDataTableResourceDemand();
	   },  
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'Resource Demand ',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Resource Demand ',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Resource Demand and Reservation',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        orientation: 'landscape',
	                        pageSize: 'A0',
	                        footer: true
	                    },	                    
	                ],	                
	            },
	            'colvis',
	            {					
					text: '<i class="fa fa-upload showHandCursor demandUpload" title="Upload Demand"></i>',
					action: function ( e, dt, node, config ) {
						displayDemandUploadNotification();						
					}
				}, {					
					text: '<span class="commentsView" title="Uploaded Info"></span>',
					action: function ( e, dt, node, config ) {					
						var testFactId=document.getElementById("treeHdnCurrentTestFactoryId").value;						
						var url="comments.for.entity.list?testFactoryId=0&productId=0&workPackageId=0&entityTypeId=73&fromDate=&toDate=";
						assignDemandDataTableValues(url);
					}
				},	            
         ], 
         columnDefs: [
              { targets: resourceDemandAndReservationColumsAvailableIndex, visible: true},
              { targets: '_all', visible: false }
          ],
                  
        aaData : resourceDemandData,                 
	    aoColumns : resourceDemandColumns,
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },
       
       "footerCallback" : function(row, data, start, end, display) {
    	   
    	   	var api = this.api(), data;
			$.each(resourceDemandAndReservationColumsAvailableIndex,
					function(index, value) {
						if (value < 7) {
							/*$(api.column(0).footer()).html('');
							$(api.column(1).footer()).html('');
							$(api.column(2).footer()).html('');
							$(api.column(3).footer()).html('');
							$(api.column(4).footer()).html('');*/

							return;
						}
						// Remove the formatting to get integer
						// data for summation
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
						$(api.column(value).footer()).html(setValuePresicion(overAllWeekAllocation));						
						//$('#'+dataTableId+'_sum_'+value).html(setValuePresicion(overAllWeekAllocation));
					});
		}    	   
	});
	
	$(function(){ // this will be called when the DOM is ready
		
		// selectedTab - 0 - Demand
		// selectedTab - 1 - Reservation
		// selectedTab - 2 - DemandAndReservation
		// selectedTab - 3 - TeamUtilization		
		
		new $.fn.dataTable.FixedColumns( resourceDemandAndReservation_oTable, {
		    leftColumns: 7,
			//rightColumns: 1,
		});
		
		/*resourceDemandAndReservation_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });*/
		
		$('#'+dataTableId+'_wrapper .dataTables_scrollFoot tfoot tr#'+dataTableId+'_filterRow th input').each( function (i) {
			this.visibleIndex = i;
		} );
		
		$('#'+dataTableId+'_wrapper .dataTables_scrollFoot tfoot tr#'+dataTableId+'_filterRow th input').keyup( function () {
			var visIndex = typeof this.visibleIndex == 'undefined' ? 0 : this.visibleIndex;				
			resourceDemandAndReservation_oTable.fnFilter( this.value, visIndex );
		});
			
		/* Use the elements to store their own index */
		$('#'+dataTableId+'_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input').each( function (i) {
			this.visibleIndex = i;
		} );
		
		$('#'+dataTableId+'_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input').keyup( function () {
			var visIndex = typeof this.visibleIndex == 'undefined' ? 0 : this.visibleIndex;				
			resourceDemandAndReservation_oTable.fnFilter( this.value, visIndex );
		});
		
		if(selectedTab == 2 || selectedTab == 0){
			$("#"+dataTableId+"_wrapper").find(".demandUpload").parent().parent().show();
		}else{
			$("#"+dataTableId+"_wrapper").find(".dt-button").eq(3).hide();
			$("#"+dataTableId+"_wrapper").find(".demandUpload").parent().parent().hide();	
		}
		
		$("#"+dataTableId+"_length").css('margin-top','8px');
		$("#"+dataTableId+"_length").css('padding-left','35px');		
		
		var importDemandUpload = '<input id="uploadDemand" type="file" name="uploadDemand" style="display:none;" onclick="{this.value = null;};" onchange="importDemand()">';
		$('#'+dataTableId+'_filter').append(importDemandUpload);		
		
		var currentYear = new Date().getFullYear();
		var yearFilter = '<span><label style="margin: 5px;">Year :</label><select id="'+dataTableId+'YearFilter" style="margin: 5px;">';
		for(var i = (currentYear - 10); i <= (currentYear + 10); i++){
			if(operationYear == i){
				yearFilter += '<option value="'+i+'" selected>'+i+'</option>';
			}else{
				yearFilter += '<option value="'+i+'">'+i+'</option>';
			}
			
		}
		yearFilter += '</select><span>';
		$('#'+dataTableId+'_filter').append(yearFilter);
		
		$(document).off('change','#'+dataTableId+'YearFilter');
		$(document).on('change','#'+dataTableId+'YearFilter', function() {
			operationYear =  $("#"+dataTableId+"YearFilter").val();
			getWeeksName();
			 var urlSplit = urlForlistDemandWeekly.split("&weekYear=");
			 urlForlistDemandWeekly = urlSplit[0]+"&weekYear="+operationYear+"&selectedTab="+selectedTab;
			showResourceDemand(urlForlistDemandWeekly, containerId, scrollYValue,selectedTab);
		});
		
		var utilizationRangeFilter = '<span id="'+dataTableId+'utilizationRangeFilterContainer"><label style="margin: 5px;">Type </label><select id="utilizationRangeFilter" style="margin: 5px;">';
		utilizationRangeFilter = utilizationRangeFilter+'<option value="2" >Overutilized</option>';
		utilizationRangeFilter = utilizationRangeFilter+'<option value="1" >Underutilized</option>';
		utilizationRangeFilter = utilizationRangeFilter+'<option value="0" >All</option>';
		utilizationRangeFilter += '</select><span>';
		$('#'+dataTableId+'_filter').append(utilizationRangeFilter);
		$('#utilizationRangeFilter').val(utilizationRange);
		
		/*$(document).off('change','#utilizationRangeFilter');
		$(document).on('change','#utilizationRangeFilter', function() {
			utilizationRange =  $("#utilizationRangeFilter").val();			
			
			$("#resourceDemandWeekRangeFilterContainer").show();
			getWeeksName();
			showResourceDemand(urlForlistDemandWeekly, containerId, scrollYValue,selectedTab);
		});*/
		
		var weekRangeDisplay = 'block';
			
		var weekRangeFilter = '<span id="resourceDemandWeekRangeFilterContainer" style="display : '+weekRangeDisplay+';float: right;"><label style="margin: 5px;">Weeks </label><input type="text" id="resourceDemandWeekRangeFilter" value="'+utilizationWeekRange+'" style="margin: 5px;" />';
		//$('#'+dataTableId+'_filter').append(weekRangeFilter);
		$('#weekContainer').html('');
		$('#weekContainer').append(weekRangeFilter);
		
		$(document).off('blur','#resourceDemandWeekRangeFilter');
		$(document).on('blur','#resourceDemandWeekRangeFilter', function() {
			utilizationWeekRange =  $("#resourceDemandWeekRangeFilter").val();
			
			$("#resourceDemandWeekRangeFilterContainer").show();
			getWeeksName();
			showResourceDemand(urlForlistDemandWeekly, containerId, scrollYValue,selectedTab);
		});
		$("#resourceDemandWeekRangeFilter").val(utilizationWeekRange);
		
		if(selectedTab == 1){
			$('#'+dataTableId+'utilizationRangeFilterContainer').show();
		}else{
			$('#'+dataTableId+'utilizationRangeFilterContainer').hide();
		}
		
	}); 
	
}
var demandWPId = "";
var demandShiftId = "";
var demandSkillId = "";
var demandRoleId = "";
var demandWK ="";
var demandTypeId = "";
var demandYear = "";


function getResourceDemandColumnMappings(selectedTab){
	var resourceDemandColumns = [];
	resourceDemandColumns.push({ mData: "testFactoryName",className: 'disableEditInline', sWidth: '5%', });
	
	resourceDemandColumns.push({ mData: "productName",className: 'disableEditInline', sWidth: '5%'});
	resourceDemandColumns.push({ mData: "shiftName",className: 'disableEditInline', sWidth: '5%'});
	resourceDemandColumns.push({ mData: "workPackageName",className: 'disableEditInline', sWidth: '5%'});
	resourceDemandColumns.push({ mData: "userRoleName",className: 'disableEditInline', sWidth: '5%'});
	resourceDemandColumns.push({ mData: "userTypeName",className: 'disableEditInline', sWidth: '5%'});
	resourceDemandColumns.push({ mData: "skillName",className: 'disableEditInline', sWidth: '5%'});
			
	resourceDemandAndReservationColumsAvailableIndex = [0, 1, 2, 3, 4, 5, 6];
	var resourceDemandColumsIndex = 6;
	
	$.each(weeksName, function(index, value){
		var weekToMap = value.split("(W ")[1].split(")")[0];
		resourceDemandColumsIndex++;
		resourceDemandAndReservationColumsAvailableIndex.push(resourceDemandColumsIndex);
		var tabStyle="padding-right:55px;padding-left:55px;";
		if(selectedTab == 0){
			resourceDemandColumsIndex++;
			resourceDemandAndReservationColumsAvailableIndex.push(resourceDemandColumsIndex);
			tabStyle="padding-right:22px;padding-left:22px";
		}
		
		if(selectedTab != 3){
			columnMapping =	{ 
					mData: 'week'+(weekToMap), 
					sWidth: '2%',
					"render" : function (resourceDemandData, resourceDemandType, resourceDemandFull) {
						var colorValue =  getDemandIndicator(resourceDemandFull['week'+(weekToMap)],resourceDemandFull['reservedResourceCountWk'+(weekToMap)]);
						demandWPId = resourceDemandFull['workPackageId'];
						demandShiftId = resourceDemandFull['shiftId'];
						demandSkillId = resourceDemandFull['skillId'];
						demandRoleId = resourceDemandFull['userRoleId'];
						demandTypeId = resourceDemandFull['userTypeId'];
						demandWK = weekToMap;
						document.getElementById("currentDemandShiftName").value = resourceDemandFull.shiftName;
						document.getElementById("cworkPackageName").value = resourceDemandFull.workPackageName;
						return ('<ul style="display: -webkit-inline-box;list-style: none;padding: 4px;margin: 0px;color: '+colorValue+';"><li><a style="color:'+colorValue+';'+tabStyle+';" href=javascript:demandAdd('+demandWPId+','+demandShiftId+','+demandWK+','+demandSkillId+','+demandRoleId+','+demandTypeId+')>'+setValuePresicion(resourceDemandFull['week'+(weekToMap)])+'</a></li></ul>');
		    			
					},
		        };
				resourceDemandColumns.push(columnMapping);
		}
		
		if(selectedTab != 2){
			columnMapping =	{ 
				mData: 'reservedResourceCountWk'+(weekToMap),
				sWidth: '2%',
				"render" : function (resourceDemandData, resourceDemandType, resourceDemandFull) {
					var colorValue =  getDemandIndicator(resourceDemandFull['week'+(weekToMap)],resourceDemandFull['reservedResourceCountWk'+(weekToMap)]);
	    			return ('<ul style="display: -webkit-inline-box;list-style: none;padding: 4px;margin: 0px;color: '+colorValue+';"><li><a style="color: '+colorValue+';'+tabStyle+'" href="javascript:javascript:reservationAdd('+resourceDemandFull.workPackageId+','+resourceDemandFull.shiftId+','+resourceDemandFull.skillId+','+resourceDemandFull.userRoleId+','+(weekToMap)+','+resourceDemandFull.workYear+','+resourceDemandFull['reservedResourceCountWk'+(weekToMap)]+','+resourceDemandFull['week'+(weekToMap)]+','+resourceDemandFull.groupDemandId+','+resourceDemandFull.userTypeId+',\''+resourceDemandFull.workPackageName+'\',\''+weeksName[index]+'\',\''+resourceDemandFull.userTypeName+'\',\''+resourceDemandFull.userRoleName+'\',\''+resourceDemandFull.skillName+'\');" class="showHandCursor">'+resourceDemandFull['reservedResourceCountWk'+(weekToMap)]+'</a></li></ul>');

				},
	        };
			resourceDemandColumns.push(columnMapping);
		}		
	});	
	return resourceDemandColumns;
}

function getResourceDemandHeaderAndFooterColumns(containerId,selectedTab){
	var resourceDemandContainer = $('#'+containerId);
	
	if(selectedTab == 0){
		dataTableId = "resourceDemandReservation_dataTable";
	}else if(selectedTab == 2){
		dataTableId = "resourceDemand_dataTable";
	}else if(selectedTab == 3){
		dataTableId = "resourceReservation_dataTable";
	}else if(selectedTab == 4){
		dataTableId = "detailedUtilizationOfResource_dataTable";
	}
	
	resourceDemandContainer.empty();
	
	 var resourceDemand_dtColumns = ['Engagement','Product','Shift', 'Workpackage', 'Role', 'Type','Skill'];
	 resourceDemand_dtColumns[0] = rdEngagement;
	 resourceDemand_dtColumns[1] = rdproduct;
	 resourceDemand_dtColumns[3] = rdplanWorkpakcage;

	 $.each(weeksName, function(index, value){
		resourceDemand_dtColumns.push(value);
	});
	
	var resourceDemandTable = '<table id="'+dataTableId+'"  class="cell-border compact row-border" cellspacing="0" width="100%">';
	var resourceDemand_thead = '<thead><tr>';
	var resourceDemand_child_header_row = '<tr>';
	var resourceDemand_filter_row = '<tfoot><tr>';
	var resourceDemandAndReservation_tfoot = '<tr id="'+dataTableId+'_filterRow">';
	
	if(selectedTab == 0){
		var columnIndex = 0;
		$.each(resourceDemand_dtColumns, function(index, value){
			if(index <= 6){
				resourceDemand_thead += '<th rowspan="2">'+value+'</th>';
				resourceDemand_filter_row += '<th></th>';
				resourceDemandAndReservation_tfoot += '<th id="'+dataTableId+'_sum_'+columnIndex+'"></th>';
				columnIndex++;
			}else{
				resourceDemand_thead += '<th style="text-align:center !important;" colspan="2">'+value+'</th>';
				var weekAndMonthArr = value.match(/[^-]+(\.[^-]+)?/g);
				var weekAndMonthVal = weekAndMonthArr[0]+"-"+weekAndMonthArr[1];
				resourceDemand_child_header_row += '<th style="text-align:center !important;">D('+weekAndMonthVal+')</th><th style="text-align:center !important;">R('+weekAndMonthVal+')</th>';
				resourceDemand_filter_row += '<th style="text-align:center !important;" ></th><th></th>';
				resourceDemandAndReservation_tfoot += '<th id="'+dataTableId+'_sum_'+columnIndex+'"></th>';
				columnIndex++;
				resourceDemandAndReservation_tfoot += '<th id="'+dataTableId+'_sum_'+columnIndex+'"></th>';
				columnIndex++;
			}
			
		});
		resourceDemandTable += resourceDemand_thead + '</tr>' + resourceDemand_child_header_row + '</tr></thead>' + resourceDemand_filter_row + '</tr>' + resourceDemandAndReservation_tfoot + '</tr></tfoot></table>';
	}else{
		$.each(resourceDemand_dtColumns, function(index, value){
			resourceDemand_thead += '<th style="text-align:center !important;">'+value+'</th>';
			resourceDemand_filter_row += '<th style="text-align:center !important;"></th>';
			resourceDemandAndReservation_tfoot += '<th id="'+dataTableId+'_sum_'+index+'"></th>';
		});
		resourceDemandTable += resourceDemand_thead + '</tr></thead>' + resourceDemand_filter_row + '</tr>' + resourceDemandAndReservation_tfoot + '</tr></tfoot></table>';
	}	
	
	resourceDemandContainer.append(resourceDemandTable);
}

function setValuePresicion(value){
	if(value != "undefined" && value != 0  ){
		value = value.toFixed(2);
	}
	return value;
}

var clearTimeoutResourceDT='';
function reInitializeDataTableResourceDemand(){
	clearTimeoutResourceDT = setTimeout(function(){				
		resourceDemandAndReservation_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutResourceDT);
	},250);
}

var demandWeekFlag;
function demandAdd(wpIdDemand,shiftIdDemand,wkNoDemand,skillIdDemand,roleIdDemand,demandTypeId){
	demandWeekFlag=true;
	raiseResourceDemandForWeekly(wpIdDemand,shiftIdDemand,wkNoDemand,skillIdDemand,roleIdDemand,demandTypeId);
}

function reservationAdd(demandWPId,demandShiftId,demandSkillId,demandRoleId,demandWK,demandYear,reservedCount,demandCount,groupDemandId,demandTypeId,wpName,weeksName,demandUserTypeName,demandUserRoleName,demandSkillName) {	
	getResourceForBlockingGridViewForWeekly(demandWPId,demandShiftId,demandSkillId,demandRoleId,demandWK,demandYear,reservedCount,demandCount,groupDemandId,demandTypeId,wpName,weeksName,demandUserTypeName,demandUserRoleName,demandSkillName);
}

function displayDemandUploadNotification(){
	$("#div_DemandUploadNotifications").modal();
	
	var demandUploadTitle="Demand Upload Notification";
	/*if(activityName.length > 25){         		
		activityNameTitle = (activityName).toString().substring(0,20)+'...';         		
 	} else {
 		activityNameTitle =activityName;
 	}*/
	
	$("#div_DemandUploadNotifications .modal-header h4").text(demandUploadTitle);
	//$("#div_DemandUploadNotifications .modal-header h4").attr('title',activityName);
	$("#div_DemandUploadNotifications .green-haze").text("Demand");
	
	$("#notifyDemandUpload").empty();
	/*$("#demandInput").val('');
	$("#demandInput").hide();*/
	//$("#demandID").text(activityId);
	
	$('#notifyDemandUpload').append('<label><input type="radio" name="radio1" checked class="icheck" id="fullUpload" value="FullUpload" data-radio="iradio_flat-grey">Full Upload</label>'+
			'<label><input type="radio" name="radio1" class="icheck" id="partialUpload" value="PartialUpload" data-radio="iradio_flat-grey">Upload From Current Week</label>');
	$('#notifyDemandUpload').show();
	
}

function submitRadioDemandUploadNotificationHandler(){
	
	var demandMessageFromUI = []; 
	   $("input:radio[name=radio1]:checked").each(function(){
		   demandMessageFromUI.push($(this).attr('id'));
		}); 
	   
	var message = document.querySelector('input[name = "radio1"]:checked').value;
	/*$.post('process.activity.poke.notification?activityId='+$("#pokeActivityID").text()+'&message='+message+'&messageType='+messageType ,function(data) {	
		console.log("success "+data.Message);
	});*/
	
	$("#div_DemandUploadNotifications").modal('hide');
	triggerDemandUpload(message);
	
}

function popupCloseDemandUploadNotificationHandler(){
	$("#div_DemandUploadNotifications").modal('hide');
}

function getLatestDemandUploadHistory(){
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url : 'comments.for.entity.latest?entityTypeId=73',
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			
			if(data.Result=="ERROR"){
     		    data = [];						
			}else{
				var firstValue = 'Resource Demand UploadInfo';
				if(data.Records.length>0){					
					firstValue = data.Records[0].commentsText;			
				}
				$("#resourceDemandReservation_dataTable_wrapper").find(".dt-button").eq(3).attr('title',"Uploaded Info");
				$("#resourceDemandReservation_dataTable_wrapper").find(".dt-button").eq(3).text(firstValue);
				
				$("#resourceDemand_dataTable_wrapper").find(".dt-button").eq(3).attr('title',"Uploaded Info");
				$("#resourceDemand_dataTable_wrapper").find(".dt-button").eq(3).text(firstValue);				
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