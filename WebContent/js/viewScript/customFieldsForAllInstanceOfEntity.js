var entityIdOfAllInstance = 0;
var entityNameOfAllInstance = "";
var entityTypeIdOfAllInstance = 0;
var entityParentInstanceIdOfAllInstance = 0;
var engagementIdOfAllInstance = 0;
var productIdOfAllInstance = 0;
var isReadyForDataTable = true;

$(document).ready(function() {
	$('#entityTypeList_ul').off('change');
	$('#entityTypeList_ul').change(function(){
		//entityTypeIdOfAllInstance = $("#entityTypeList_ul").select2().find('option:selected').attr('id');
		entityTypeIdOfAllInstance = $("#entityTypeList_ul").find('option:selected').val();
		getCustomFieldsOfAllInstance();
	});
});

function loadEntityTypeList(entityIdForType){
	$('#entityTypeList_ul').empty();
	//$('#entityTypeList_ul').append('<option id="-1" ><a href="#">Choose</a></option>');
	$.post('entity.for.type.list?engagementId='+engagementIdOfAllInstance+'&productId='+productIdOfAllInstance+'&entityTypeId='+entityIdForType,function(data) {	
		var ary = (data.Options);
		$.each(ary, function(index, element) {
			$('#entityTypeList_ul').append('<option id="' + element.Value + '" value="' + element.Value + '"><a href="#">' + element.DisplayText + '</a></option>');
		});
		$('#entityTypeList_ul').select2();	
		entityTypeIdOfAllInstance = $("#entityTypeList_ul").select2().find('option:selected').attr('id');
		isReadyForDataTable = true;
	});
}

function getAllInstanceCustomFieldsOfEntity(entityId, entityTypeId, entityParentInstanceId, engagementId, productId, entityName){
	entityIdOfAllInstance = entityId;
	entityNameOfAllInstance = entityName;
	entityTypeIdOfAllInstance = entityTypeId;
	entityParentInstanceIdOfAllInstance = entityParentInstanceId;
	engagementIdOfAllInstance = engagementId;
	productIdOfAllInstance = productId;
	if(entityId == 28){
		isReadyForDataTable = false;
		loadEntityTypeList(33);
		$("#entityType").show();
	}else{
		$("#entityType").hide();
	}
	
	var isReadyForDataTableChecker = setInterval(function () {
		if(isReadyForDataTable){
			clearInterval(isReadyForDataTableChecker);
			getCustomFieldsOfAllInstance();
		}
	}, 1000);
}

var customFiledsAllInstanceColumns = [];
function getMultiFrequencyCustomFieldsOfAllInstanceColumns(){
	customFiledsAllInstanceColumns = [];
	openLoaderIcon();
	$.ajax({
		type: "POST",
		//url : 'custom.field.columns.exist.for.all.instance.of.entity?entityId='+entityId+'&parentEntityId='+parentEntityId+'&parentEntityInstanceId='+parentEntityInstanceId,
		url : 'custom.field.columns.exist.for.all.instance.of.entity?entityId='+entityIdOfAllInstance+'&entityTypeId='+entityTypeIdOfAllInstance+'&engagementId='+engagementIdOfAllInstance+'&productId='+productIdOfAllInstance,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {		
			if(data.Result=="ERROR"){
				data = [];						
			}else{
				if(typeof data.Records != 'undefined'){
					for (var i = 0; i < data.Records.length; i++) {
						customFiledsAllInstanceColumns.push(data.Records[i].option);
					}
				}
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

function getCustomFieldsOfAllInstance(){
	getMultiFrequencyCustomFieldsOfAllInstanceColumns();
	openLoaderIcon();
	$.ajax({
		type: "POST",
		//url : 'custom.field.exist.for.all.instance.of.entity?entityId='+entityId+'&entityParentInstanceId='+entityParentInstanceId+'&parentEntityId='+parentEntityId+'&parentEntityInstanceId='+parentEntityInstanceId,
		url : 'custom.field.exist.for.all.instance.of.entity?entityId='+entityIdOfAllInstance+'&entityTypeId='+entityTypeIdOfAllInstance+'&entityParentInstanceId='+entityParentInstanceIdOfAllInstance+'&engagementId='+engagementIdOfAllInstance+'&productId='+productIdOfAllInstance,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {		
			var customFiledsAllInstanceData = {};
			if(data.Result=="ERROR"){
				customFiledsAllInstanceData = {};						
			}else{
				customFiledsAllInstanceData = data.Records;
			}
			var allInstanceColumnChecker = setInterval(function () {
				if(typeof customFiledsAllInstanceColumns != 'undefined' && customFiledsAllInstanceColumns.length > 0){
					clearInterval(allInstanceColumnChecker);
					getCustomFiledsOfAllInstanceDataTable();
					var columnValueMapping = getCustomFiledsOfAllInstanceColumnMappings();
					showCustomFieldsOfAllInstance('150', columnValueMapping, customFiledsAllInstanceData);
				}
			}, 1000);
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

var allInstanceCustomField_oTable = '';
var allInstanceCustomFieldColumsAvailableIndex = [];
function showCustomFieldsOfAllInstance(scrollYValue, allInstanceCustomFieldColumns, allInstanceCustomFieldData){
	try{
		if ($('#allInstanceCustomField_dataTable').length > 0) {
			$('#allInstanceCustomField_dataTable').dataTable().fnDestroy(); 
		}
	} catch(e) {}
	
	allInstanceCustomField_oTable = $('#allInstanceCustomField_dataTable').dataTable( {
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
       /*fixedColumns:   {
           leftColumns: 4,
           //rightColumns: 1
       },*/ 
       "fnInitComplete": function(data) {
    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
    	  
    	  var headerItems = $('#allInstanceCustomField_dataTable_wrapper thead tr#allInstanceCustomField_dataTable_filterRow th');
    	  
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
  	    	    	$(this).prepend( '<input type="text" name="'+data.aoColumns[i].mData+'" value="" style="width:100%" class="search_init" />');
  	    	    }
    	   });   	
	  
    	  reInitializeDataTableAllInstanceCustomField();
	   },  
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'Instance Custom Fields',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Instance Custom Fields',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Instance Custom Fileds',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        orientation: 'landscape',
	                        pageSize: 'LEGAL',
	                        footer: true
	                    },	                   
	                ],	                
	            },
	            'colvis'
         ], 
         columnDefs: [
              { targets: allInstanceCustomFieldColumsAvailableIndex, visible: true},
              { targets: '_all', visible: false }
          ],
        aaData : allInstanceCustomFieldData,                 
	    aoColumns : allInstanceCustomFieldColumns,
	    rowCallback: function ( row, data ) {
	    	if(data['isModified']) {
				$(row).find('td:eq(0)').css('backgroundColor', 'orange');
			}
	   },
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },
    	   
	});
	
	$(function(){ // this will be called when the DOM is ready 
		
		// Activate an inline edit on click of a table cell
        $('#allInstanceCustomField_dataTable tbody').on( 'click', 'td', function (e) {
        	editorInStanceAllCell = allInstanceCustomField_oTable.DataTable().cell(this);
        } );
	    
		allInstanceCustomField_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.header() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                    .search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });
		
		$("#allInstanceCustomField_dataTable_length").css('margin-top','8px');
		$("#allInstanceCustomField_dataTable_length").css('padding-left','35px');
	}); 
}

function reInitializeDataTableAllInstanceCustomField(){
	setTimeout(function(){				
		allInstanceCustomField_oTable.DataTable().columns.adjust().draw();		
	},200);
}

function getCustomFiledsOfAllInstanceDataTable(){
	var allInstanceCustomFieldContainer = $('#div_allInstanceCustomFieldPopup');
	allInstanceCustomFieldContainer.find(".modal-title").text('Custom Fields of '+entityNameOfAllInstance);
	var customFieldsContainer = allInstanceCustomFieldContainer.find('#allInstanceCustomField');
	if(customFieldsContainer != 'undefined' && customFieldsContainer != null){
		customFieldsContainer.empty();
	}
		
	var allInstanceCustomFieldTable = '<table id="allInstanceCustomField_dataTable"  class="cell-border compact row-border" cellspacing="0" width="100%">';
	var allInstanceCustomField_thead = '<thead><tr>';
	var allInstanceCustomField_filter_row = '<tr id="allInstanceCustomField_dataTable_filterRow">';
	var allInstanceCustomField_tfoot = '<tfoot><tr>';
	$.each(customFiledsAllInstanceColumns, function(index, value){
		allInstanceCustomField_thead += '<th class="sorting">'+value+'</th>';
		allInstanceCustomField_filter_row += '<th>'+value+'</th>';
		allInstanceCustomField_tfoot += '<td></td>';
	});
	
	allInstanceCustomField_thead += '<th class="sorting">Modified</th>';
	allInstanceCustomField_filter_row += '<th>Modified</th>';
	allInstanceCustomField_tfoot += '<td></td>';
	
	allInstanceCustomFieldTable += allInstanceCustomField_thead + '</tr>' + allInstanceCustomField_filter_row + '</tr></thead>'+ allInstanceCustomField_tfoot + '</tr></tfoot></table>';
	customFieldsContainer.append(allInstanceCustomFieldTable);
	$("#div_allInstanceCustomFieldPopup").modal();
}

function getCustomFiledsOfAllInstanceColumnMappings(){
	var allInstanceCustomFieldColumns = [];
	allInstanceCustomFieldColumsAvailableIndex = [];
	$.each(customFiledsAllInstanceColumns, function(index, value){
		/*if(value == 'Name' || value == 'Id'){
			allInstanceCustomFieldColumns.push({ mData: value, sWidth: '5%'});
		}else{
			
		}*/
		allInstanceCustomFieldColumns.push({ 
			mData: value, 
			sWidth: '5%',
			"render" : function (allInstanceCustomFieldData, allInstanceCustomFieldType, allInstanceCustomFieldFull) {
				var customFieldIdAndValueId = allInstanceCustomFieldFull['idsOf'+value];
				var isModified = allInstanceCustomFieldFull['isModifiedOf'+value];
				var labelColor = '#333';
				if(isModified){
					labelColor = 'orange';
				}
				if(typeof customFieldIdAndValueId != 'undefined' && customFieldIdAndValueId != null && customFieldIdAndValueId != ''){
					var customFieldId = customFieldIdAndValueId.split('-')[0];
					var customFieldValueId = customFieldIdAndValueId.split('-')[1];
					var entityInstanceId = allInstanceCustomFieldFull['Id'];
					return ('<span style="cursor : pointer; color : '+labelColor+'" onclick="getMultiFrequencyIndividualFieldDetailSingle(\''+customFieldValueId+'\', \''+customFieldId+'\', \'Single\', \'0\', \''+entityInstanceId+'\')">'+allInstanceCustomFieldData+'</span>');
				}else{
					return allInstanceCustomFieldData;
				}
				
        	}
		});
		allInstanceCustomFieldColumsAvailableIndex.push(index);
	});
	allInstanceCustomFieldColumns.push({ mData: 'isModified', sWidth: '5%', 
		"render" : function (allInstanceCustomFieldData, allInstanceCustomFieldType, allInstanceCustomFieldFull) {
			if(allInstanceCustomFieldData){
				return 'Yes';
			}else{
				return 'No';
			}
		}
	});
	allInstanceCustomFieldColumsAvailableIndex.push(customFiledsAllInstanceColumns.length);
	return allInstanceCustomFieldColumns;
}