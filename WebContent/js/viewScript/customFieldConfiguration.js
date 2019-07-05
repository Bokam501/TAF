var customFieldsListContainerId = 'customFieldsListContainer';
var scrollYValue = '150';

var entityId = 0;
var entityTypeId = 0;
var engagementId = 0;
var productId = 0;

var parentEntityId = 0;
var parentEntityInstanceId = 0;

$(document).ready(function() {
	if(typeof isCustomFieldConfiguration != 'undefined' && isCustomFieldConfiguration){
		loadEntityList();
		loadEntityTypeList();
		loadEngagementsList();
		loadProductsBasedOnEngagementId();
	}
	getCustomFieldsDropDownOptions(0);
	
	$('#engagementList_ul').change(function(){
		engagementId = $("#engagementList_ul").select2().find('option:selected').attr('id');
		if(engagementId != -1){
			parentEntityId = 13;
			parentEntityInstanceId = engagementId;	
		}else{
			parentEntityId = 0;
			parentEntityInstanceId = 0;
		}
		productId = 0;
		loadProductsBasedOnEngagementId();
		getCustomFieldsOfEntity();
	});
	
	$('#productList_ul').change(function(){
		productId = $("#productList_ul").select2().find('option:selected').attr('id');
		if(productId != -1){
			parentEntityId = 18;
			parentEntityInstanceId = productId;
		}else{
			parentEntityId = 13;
			parentEntityInstanceId = engagementId;
		}
		loadEntityTypeList();
		getCustomFieldsOfEntity();
	});
	
	$('#entityList_ul').change(function(){
		entityId = $("#entityList_ul").select2().find('option:selected').attr('id');
		entityTypeId = 0;
		loadEntityTypeList();
		getCustomFieldsOfEntity();
	});
	
	$('#entityTypeList_ul').change(function(){
		entityTypeId = $("#entityTypeList_ul").select2().find('option:selected').attr('id');
		getCustomFieldsOfEntity();
	});
	
});

function loadEngagementsList(){		
	$('#engagementList_ul').empty();				
	$('#engagementList_ul').append('<option id="-1" ><a href="#">Choose</a></option>');
	$.post('custom.field.configuration.engagement.list',function(data) {	
		var ary = (data.Options);
		$.each(ary, function(index, element) {
			$('#engagementList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
		});
		$('#engagementList_ul').select2();	
	});
}

function loadProductsBasedOnEngagementId(){	
	$('#productList_ul').empty();	
	$('#productList_ul').append('<option id="-1" ><a href="#">Choose</a></option>');
	if(engagementId > 0){
		$.post('custom.field.configuration.product.list?engagementId='+engagementId,function(data) {	
			var ary = (data.Options);
			$.each(ary, function(index, element) {
				$('#productList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
			});
		});
	}
	$('#productList_ul').select2();	
	/*else{
		callAlert("Please choose engagement... ");
	}*/
}

function loadEntityList(){
	$('#entityList_ul').empty();
	$('#entityList_ul').append('<option id="-1" ><a href="#">Choose</a></option>');
	$.post('custom.field.configuration.eligible.entity.list',function(data) {	
		var ary = (data.Options);
		$.each(ary, function(index, element) {
			$('#entityList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
		});
		$('#entityList_ul').select2();	
	});
}

function loadEntityTypeList(){
	$('#entityTypeList_ul').empty();
	$('#entityTypeList_ul').append('<option id="-1" ><a href="#">Choose</a></option>');
	$.post('entity.for.type.list?engagementId='+engagementId+'&productId='+productId+'&entityTypeId='+entityId,function(data) {	
		var ary = (data.Options);
		$.each(ary, function(index, element) {
			$('#entityTypeList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
		});
		$('#entityTypeList_ul').select2();	
	});
}

var customFieldDropDowns = ['dataType', 'controlType', 'mandatory', 'fieldGroup', 'frequencyType', 'frequency'];
var customFieldDropDownCount = 0;
var customFieldDropDownOptions = {};
function getCustomFieldsDropDownOptions(position){
	if(typeof position == 'undefined' || typeof customFieldDropDowns[position] == 'undefined'){
		return;
	}
	$.ajax({
		  type: "POST",
		  url : 'custom.field.configuration.drop.down.options?fieldName='+customFieldDropDowns[position],
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			customFieldDropDownCount++;
			customFieldDropDownOptions[position] = data.Records;
			if(customFieldDropDownCount >= customFieldDropDowns.length){
				getCustomFieldsOfEntity();
			}else{
				getCustomFieldsDropDownOptions(customFieldDropDownCount);
			}
		  },
		  error : function(data) {
			  
		 },
		 complete: function(data){
		 }
	 });
}

var customFieldsData = {};
function getCustomFieldsOfEntity(){
	openLoaderIcon();
	$.ajax({
		  type: "POST",
		  url : 'custom.field.exist.for.entity?entityId='+entityId+'&entityTypeId='+entityTypeId+'&parentEntityId='+parentEntityId+'&parentEntityInstanceId='+parentEntityInstanceId,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			
			if(data.Result=="ERROR"){
				customFieldsData = {};		
     		   callAlert(data.Message);
			}else{
				customFieldsData = data.Records;
			}
			getCustomFieldsHeaderAndFooterColumns();
			var columnValueMapping = getCustomFieldsColumnMappings();
			showCustomFieldsOfEntity(scrollYValue, columnValueMapping, customFieldsData);
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

var customFields_oTable = '';
var customFieldsColumsAvailableIndex = [];
function showCustomFieldsOfEntity(scrollYValue, columnValueMapping, customFieldsData){
	try{
		if ($('#customFields_dataTable').length > 0) {
			$('#customFields_dataTable').dataTable().fnDestroy(); 
		}
	} catch(e) {}
	
	customFieldCreator = {};
	customFieldCreator = new $.fn.dataTable.Editor( {
	    "table": "#customFields_dataTable",
		//ajax: "custom.field.save.or.update.for.entity?entityId="+entityId+"&entityTypeId="+entityTypeId+"&parentEntityId="+parentEntityId+"&parentEntityInstanceId="+parentEntityInstanceId,
	    ajax: "custom.field.save.or.update.for.entity",
		ajaxUrl: "custom.field.save.or.update.for.entity",
		idSrc:  "id",
		i18n: {
	        create: {
	            title:  "Create Custom Field",
	            submit: "Create",
	        }
	    },
		fields: [
			 {
	            label: "parentEntityId",
	            name: "parentEntityId",
	            "type": "hidden",
	            "default": parentEntityId,
	        },{
	            label: "parentEntityInstanceId",
	            name: "parentEntityInstanceId",
	            "type": "hidden",
	            "default": parentEntityInstanceId,
	        },{
	            label: "Entity Id",
	            name: "entityId",
	            "type": "hidden",
	            "default": entityId,
	        },{
	            label: "Entity Type Id",
	            name: "entityTypeId",
	            "type": "hidden",
	            "default": entityTypeId,
	        },{
	            label: "Id",
	            name: "id",	
	            type: "hidden",
	            "default": 0,
	        },{
	            label: "Name *",
	            name: "fieldName",	
	        },{
	            label: "Description",
	            name: "description",
	            type: "textarea"
	        },{
	            label: "Data Type *",
	            name: "dataType",
	            //options: [{"DisplayText":"Text", "Number":"", "Value":"Text", "label":"Text", "value":"Text"}, {"DisplayText":"Integer", "Number":"", "Value":"Integer", "label":"Integer", "value":"Integer"}, {"DisplayText":"Decimal", "Number":"", "Value":"Decimal", "label":"Decimal", "value":"Decimal"}, {"DisplayText":"Boolean", "Number":"", "Value":"Boolean", "label":"Boolean", "value":"Boolean"}, {"DisplayText":"Date", "Number":"", "Value":"Date", "label":"Date", "value":"Date"}],//optionsResultArr[2],
	            options: customFieldDropDownOptions['0'],
	            type: "select",			                
	        },{
	            label: "Control Type *",
	            name: "controlType",
	            //options: ["Text Box", "Select", "Radio Button", "Check Box", "Text Area", "Date Picker"],//optionsResultArr[3],
	            options: customFieldDropDownOptions['1'],
	            type : "select",
	        },{
	            label: "Mandatory *",
	            name: "isMandatory",
	            //options: ["Text Box", "Select", "Radio Button", "Check Box", "Text Area", "Date Picker"],//optionsResultArr[3],
	            options: customFieldDropDownOptions['2'],
	            type : "select",
	        },{
	            label: "Default Value",
	            name: "defaultValue",	
	        },{
	            label: "Upper Limit",
	            name: "upperLimit",	
	        },{
	            label: "Lower Limit",
	            name: "lowerLimit",	
	        },{
	            label: "Field Options",
	            name: "fieldOptions",
	            type: "textarea"
	        },{
	            label: "Group *",
	            name: "fieldGroupId",
	            //options: [{"DisplayText":"Project Details", "Number":"", "Value":"1", "label":"Project Details", "value":"1"}, {"DisplayText":"Rev Impact", "Number":"", "Value":"2", "label":"Rev Impact", "value":"2"}, {"DisplayText":"Risks", "Number":"", "Value":"3", "label":"Risks", "value":"3"}, {"DisplayText":"Planning", "Number":"", "Value":"4", "label":"Planning", "value":"4"}, {"DisplayText":"Resource Planning", "Number":"", "Value":"5", "label":"Resource Planning", "value":"5"}],//optionsResultArr[3],
	            options: customFieldDropDownOptions['3'],
	            type : "select",
	        },{
	            label: "Frequency Type *",
	            name: "frequencyType",
	            //options: ["Single", "Series"],//optionsResultArr[3],
	            options: customFieldDropDownOptions['4'],
	            type : "select",
	        },{
	            label: "Frequency *",
	            name: "frequency",
	            //options: ["--", "Daily", "Weekly", "Monthly", "Quaterly", "Half-yearly", "Annual"],//optionsResultArr[3],
	            options: customFieldDropDownOptions['5'],
	            type : "select",
	        },{
	            label: "Order",
	            name: "displayOrder",
	        },{
	            label: "Active State",
	            name: "isActive",
	            type: "hidden",
	            "default": 1,
	        },{
	            label: "Created By",
	            name: "createdById",
	            type: "hidden",
	            "default": 0,
	        },{
	            label: "Created On",
	            name: "createdOn",
	            type: "hidden",
	            "default": 'dd-mm-yy',
	        }
        ]
	});
	
	$( 'input', customFieldCreator.node()).on( 'focus', function () {
		this.select();
	});
	
	$( 'textarea', customFieldCreator.node()).on( 'focus', function () {
		this.select();
	});
	
	customFields_oTable = $('#customFields_dataTable').dataTable( {
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
    	  
    	  var headerItems = $('#customFields_dataTable_wrapper tfoot tr#customFields_dataTable_filterRow th');
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
	  
		   reInitializeDataTableCustomFields();
	   },  
	   buttons: [
	             { extend: "create", editor: customFieldCreator },
	             {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'Custom Fileds',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'Custom Fields',
	                    	exportOptions: {
	                            columns: ':visible',
	                        },
	                        footer: true
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'Custom Fields',
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
              { targets: customFieldsColumsAvailableIndex, visible: true},
              { targets: '_all', visible: false }
          ],
                  
        aaData : customFieldsData,                 
	    aoColumns : columnValueMapping,
	    rowCallback: function ( row, data ) {
            $('input.editor-active', row).prop( 'checked', data.isActive == 1 );
        },
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },
    	   
	});
	
	$(function(){ // this will be called when the DOM is ready 
	    
		customFields_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });
		$('#customFields_dataTable').on( 'change', 'input.editor-active', function () {
			customFieldCreator
			.edit( $(this).closest('tr'), false )
			.set( 'isActive', $(this).prop( 'checked' ) ? 1 : 0 )
			.submit();
		});
		  // ----- Comments -----
	    $('#customFields_dataTable tbody').on('click', 'td button .img2', function () {
	    	var tr = $(this).closest('tr');
	    	var row = customFields_oTable.DataTable().row(tr);
	    	//listComments1(row, tr);
	    	var entityTypeIdComments = 68;
	    	var entityNameComments = "CustomFieldConfig";
	    	listComments(entityTypeIdComments, entityNameComments, row.data().id, row.data().fieldName, "customFieldsComments");
	    });
		// ----- Delete Custom field -----
	    /*$('#customFields_dataTable tbody').on('click', 'td button .img1', function () {
	    	deleteCustomField(customFieldId);
   		});*/
	    
		// Activate an inline edit on click of a table cell
        $('#customFields_dataTable').on( 'click', 'tbody td.editable', function (e) {
        	customFieldCreator.inline( this, {
                submitOnBlur: true
            } );
        } );
        
		$("#customFields_dataTable_length").css('margin-top','8px');
		$("#customFields_dataTable_length").css('padding-left','35px');
	    
	}); 
}

function getCustomFieldsColumnMappings(){
	var customFieldsColumns = [];
	//customFieldsColumns.push({ mData: "fieldName", className: 'disableEditInline', sWidth: '5%'});
	customFieldsColumns.push({ mData: "fieldName", className: 'editable', sWidth: '5%'});
	customFieldsColumns.push({ mData: "description", className: 'editable', sWidth: '5%'});
	customFieldsColumns.push({ mData: "dataType", className: 'editable', sWidth: '5%'});
	customFieldsColumns.push({ mData: "controlType", className: 'editable', sWidth: '5%'});
	customFieldsColumns.push({ mData: "isMandatory", className: 'editable', sWidth: '5%'});
	customFieldsColumns.push({ mData: "defaultValue", className: 'editable', sWidth: '5%'});
	customFieldsColumns.push({ mData: "upperLimit", className: 'editable', sWidth: '5%'});
	customFieldsColumns.push({ mData: "lowerLimit", className: 'editable', sWidth: '5%'});
	customFieldsColumns.push({ mData: "fieldOptions", className: 'editable', sWidth: '5%'});
	customFieldsColumns.push({ mData: "fieldGroupName", className: 'editable', sWidth: '5%', editField: "fieldGroupId",
		mRender: function (data, type, full) {
          	 if (full.action == "create" || full.action == "edit"){
          		data = optionsValueHandler(customFieldCreator, 'fieldGroupId', full.fieldGroupId);
          	 }
          	 else if(type == "display"){
          		data = full['fieldGroupName'];
          	 }	           	 
            return data;
        },	
	});
	customFieldsColumns.push({ mData: "displayOrder", className: 'editable', sWidth: '5%'});
	customFieldsColumns.push({ mData: "frequencyType", className: 'editable', sWidth: '5%'});
	customFieldsColumns.push({ mData: "frequency", className: 'editable', sWidth: '5%'});
	customFieldsColumns.push({ mData: null,
	    mRender: function (data, type, full) {
	  	  if ( type === 'display' ) {
	              return '<input type="checkbox" class="editor-active">';
	          }
	          return data;
	      },
	      className: "dt-body-center",
	      sWidth: '5%'
	});
	customFieldsColumns.push({ mData: null,				 
		bSortable: false, mRender: function(data, type, full) {				            	
			var img = ('<div style="display: flex;">'+
					'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
					'<i class="fa fa-comments img2" title="Comments"></i></button>'+
					'<button style="border: none; background-color: transparent; outline: none;">'+
       				'<i class="fa fa-trash-o details-control img1" title="Delete Custom Field" style="margin-left: 5px;" onClick="deleteCustomField(\''+full['customFieldId']+'\')"></i></button>'+
			'</div>');	      		
	return img;
		}
	});
		
	customFieldsColumsAvailableIndex = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14];
	
	return customFieldsColumns;
}

function getCustomFieldsHeaderAndFooterColumns(){
	var customFieldsContainer = $('#'+customFieldsListContainerId);
	customFieldsContainer.empty();
	var customFields_dtColumns = ['Field&nbsp;Name', 'Description', 'Data&nbsp;Type',  'Control&nbsp;Type', 'Mandatory', 'Default&nbsp;Value', 'Upper&nbsp;Limit', 'Lower&nbsp;Limit', 'Options', 'Group', 'Order', 'Frequency Type', 'Frequency', 'Active State'];
	
	$('#customFields_dataTable').remove();
	var customFieldsTable = '<table id="customFields_dataTable"  class="cell-border compact row-border" cellspacing="0" width="100%">';
	var customFields_thead = '<thead><tr>';
	var customFields_tfoot = '<tfoot><tr id="customFields_dataTable_filterRow">';
	$.each(customFields_dtColumns, function(index, value){
		customFields_thead += '<th>'+value+'</th>';
		customFields_tfoot += '<th></th>';
	});
	customFields_thead += '<th></th>';
	customFields_tfoot += '<th></th>';
	customFieldsTable += customFields_thead + '</tr></thead>'+ customFields_tfoot + '</tr></tfoot></table>';
	customFieldsContainer.append(customFieldsTable);
}

function reInitializeDataTableCustomFields(){

	setTimeout(function(){				
		customFields_oTable.DataTable().columns.adjust().draw();		
	},200);
}

function deleteCustomField(customFieldId){
	openLoaderIcon();
	$.ajax({
		type: "POST",
		url : 'custom.field.delete?customFieldId='+customFieldId,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
				callAlert(data.Message);
			}
			closeLoaderIcon();
			getCustomFieldsOfEntity();
		},
		error : function(data) {
			closeLoaderIcon();  
		},
		complete: function(data){
			closeLoaderIcon();
		}
	});
}

function listComments(entityTypeId, entityName, instanceId, instanceName, componentUsageTitle){

	var url='comments.for.entity.or.instance.list?productId=0&entityTypeId='+entityTypeId+'&entityInstanceId='+instanceId+'&jtStartIndex=0&jtPageSize=10000';
	//var instanceId = row.data().productId;
	var jsonObj={"Title":"Comments on "+entityName+ ": " +instanceName,
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,
			"componentUsageTitle":componentUsageTitle,			
			"entityTypeId":entityTypeId,
			"entityInstanceId":instanceId,
	};
	CommentsMetronicsUI.init(jsonObj);
}

function closeCustomFieldConfigForBotCommandMasterPopup(popupId){
	$("#"+popupId).modal('hide');
}