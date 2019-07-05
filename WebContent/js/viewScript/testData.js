var parentTreeData;
var selectedNodeDetails;
var nodeDropped = false;
var getTestTestDataJsonObj='';
var testDataFilterId = 0;
var uiObjectItemId;
var objRepoId=0;
var tdType = "";
var selectedTab = "";

var TestDatOfJobsPagination = function() {	
	var initialise = function(jsonObj){
		getTestTestDataJsonObj=jsonObj;
		showTestDataInit(jsonObj);		   
	};
	return {				
		init: function(jsonObj) {        	
			initialise(jsonObj);
		}		
	};	
}();

function showTestDataInit(jsonObj){
	//selectedTab = $("#testsRadioGrp ").find("label.active").index();
	if(tdType != undefined && tdType != null && tdType == "1" && pageName == "MYTESTDATA" ){
		listTestDataItemsDataTable(productId);
	}else if (tdType != undefined && tdType != null && tdType == "2" && pageName == "OBJECTREPOSITORY"){
		objectRepositoryListDataTable(productId);
	}else if (pageName == "TESTDATAATTACHMENTS"){
		displayTestDataRepository(jsonObj.urlToGetTree);
	}else if (pageName == "EDAT"){
		eDataDataTable();
	}
}

function displayTestDataRepository(url){	 	
	openLoaderIcon();

	$.ajax({
		type: "POST",
		url:url,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		success : function(data) {		
			closeLoaderIcon();		
			if(data == null || data.Result=="ERROR"){
				data = [];						
			}else{
				data = data.Records;
			}			
			productTestTestData_Container(data);			

		},
		error : function(data) {
			closeLoaderIcon();  
		},
		complete: function(data){
			closeLoaderIcon();			
		}
	});	
}

var clearTimeoutDTProductTestTestData='';
var productTestTestData_oTable='';
function reInitializeDTProductTestTestData(){
	clearTimeoutDTProductTestTestData = setTimeout(function(){				
		productTestTestData_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTProductTestTestData);
	},200);
}

function productTestTestData_Container(data){
	pageName = "TESTDATAATTACHMENTS";
	try{
		if ($("#dataTableContainerForTDAttachment").children().length>0) {
			$("#dataTableContainerForTDAttachment").children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(10);  // total coulmn count				  
	  var childDivString = '<table id="testTestData_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#dataTableContainerForTDAttachment").append(childDivString); 						  
	  
	  $("#testTestData_dataTable thead").html('');
	  $("#testTestData_dataTable thead").append(productTestTestDataHeader());
	  
	  $("#testTestData_dataTable tfoot tr").html('');     			  
	  $("#testTestData_dataTable tfoot tr").append(emptytr);
					
	productTestTestData_oTable = $("#testTestData_dataTable").dataTable( {
		 	dom: "Bfrtilp",
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bSort": false,
	       "bScrollCollapse": true,	       
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex = [9]; // search column TextBox Invisible Column position
     		  $("#testTestData_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
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
			   reInitializeDTProductTestTestData();			   
		   },  
		   select: true,
		   buttons: [	             	
					{					
					text: '<span title="Add Test Data Repository">New</span>',
						action: function ( e, dt, node, config ) {
							displayCreateTestDataTRP(); 
						}
					},	
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "Test Data",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "Test Data",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "Test Data",
		                    	exportOptions: {
		                            columns: ':visible'
		                        },
		                        orientation: 'landscape',
		                        pageSize: 'LEGAL'
		                    },	                    
		                ],	                
		            },
		            'colvis',					
					/*{					
					text: '<i class="fa fa-tree" title="Testsuite Testcasestree"></i>',
					action: function ( e, dt, node, config ) {					
							document.getElementById("hdnProductName").value = productId;
							showTestSuiteTestCaseTree();
						}
					}, */	
				], 	         
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "entityMasterName",className: 'disableEditInline', sWidth: '5%' },		
           { mData: "entityName",className: 'disableEditInline', sWidth: '15%' },
           { mData: "attachmentType",className: 'disableEditInline', sWidth: '15%' },
           { mData: "attributeFileExtension",className: 'disableEditInline', sWidth: '15%' },
           { mData: "attributeFileName",className: 'disableEditInline', sWidth: '15%' },						
           { mData: "attributeFileURI",className: 'disableEditInline', sWidth: '15%' },
           { mData: "description",className: 'disableEditInline', sWidth: '15%' },
		   { mData: "createrName",className: 'disableEditInline', sWidth: '5%' },		
           { mData: "modifierName",className: 'disableEditInline', sWidth: '15%' },           					  
			{ mData: null,				 
            	bSortable: false,
				sWidth: '2%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+					
     	       		'<button style="border: none; background-color: transparent; outline: none;">'+
						'<i class="fa fa-search-plus details-control productTestTestDataImg1" title="Audit History" style="padding-left: 0px;"></i></button>'+
					'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						'<i class="fa fa-comments productTestTestDataImg2" title="Comments"></i></button>'+	
     	       		'</div>');	      		
           		 return img;
            	},
            },
       ], 			   
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready  
		 
		 $("#testTestData_dataTable_length").css('margin-top','8px');
		 $("#testTestData_dataTable_length").css('padding-left','35px');
		 		
		productTestTestData_oTable.DataTable().columns().every( function () {
			var that = this;
			$('input', this.footer() ).on( 'keyup change', function () {
				if ( that.search() !== this.value ) {
					that
						.search( this.value, true, false )
						.draw();
				}
			});
		});	

		 // ----- Audit History -----
		 
		 $('#testTestData_dataTable tbody').on('click', 'td button .productTestTestDataImg1', function () {
			var tr = $(this).closest('tr');
			var row = productTestTestData_oTable.DataTable().row(tr);
							
			listTestDataAuditHistory(row.data().attachmentId); 				    
		 });
		 
		 // ----- Comments  -----
		 
		 $('#testTestData_dataTable tbody').on('click', 'td button .productTestTestDataImg2', function () {
			var tr = $(this).closest('tr');
			var row = productTestTestData_oTable.DataTable().row(tr);
							
			var entityTypeIdComments = 37;
			var entityNameComments = "Attachment";
			listComments(entityTypeIdComments, entityNameComments, row.data().attachmentId, row.data().attributeFileName, "attachComments"); 				    
		 });
	});
}

function productTestTestDataHeader(){		
	var tr = '<tr>'+			
	'<th>Entity</th>'+
	'<th>Entity Name</th>'+
	'<th>Attachment Type</th>'+
	'<th>File Extension</th>'+
	'<th>File</th>'+
	'<th>URL</th>'+
	'<th>Description</th>'+
	'<th>Created By</th>'+	
	'<th>Modified By</th>'+				
	'<th></th>'+	
	'</tr>';
	return tr;
}


function loadVersionsForTestData(){
	$('#productVersionTestData_ul').empty();

	$.post('common.list.productversion?productId='+productId,function(data) {	
		var ary = (data.Options);
		$('#productVersionTestData_ul').append('<option id="-1" value="-1" ><a href="#">--Select--</a></option>');
		$.each(ary, function(index, element) {
			$('#productVersionTestData_ul').append('<option id="' + element.Value + '" value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
		$('#productVersionTestData_ul').select2();
	});
}

function initializeTestDataTRP(){
	$('#name_TestData').val('');
	$('#desc_TestData').val('');
	$('#prefixname_TestData').val('');	
	$('#testDataAttachmentID').val('');

	loadVersionsForTestData();	
	$('#productVersionTestData_ul').prop('disabled', false);

	$('select[id="testDataType_ul"] option[value="-1"]').attr("selected","selected");
	$('#testDataType_ul').select2();
	$('#testDataType_ul').prop('disabled', false);	
}

function createTestDataTRP(){
	var tdAttachName = document.getElementById("testDataAttachmentID").value;	
	var tdName = document.getElementById("name_TestData").value;
	if((tdName==null)|| (tdName==undefined)|| ("" == tdName)){
		callAlert("Please provide valid Name");
		return false;
	}

	var tdPrefixName = document.getElementById("prefixname_TestData").value;
	if((tdPrefixName==null)|| (tdPrefixName==undefined)|| ("" == tdPrefixName)){
		callAlert("Please provide valid handle name");
		return false;
	}

	if(("" != tdPrefixName) && tdPrefixName.length > 10){
		callAlert("Please provide max 10 characters to handle name");
		return false;
	}

	/*var pattern = /^([a-zA-Z0-9-]+)$/;

	if(!tdPrefixName.match(pattern)){
		callAlert("Please enter alphanumeric values");
		return false;
	}*/

	var tdVersion = $("#productVersionTestData_ul").find('option:selected').attr('id');
	if((tdVersion==null)|| (tdVersion==undefined)|| (tdVersion==-1)){
		callAlert("Please choose Version");
		return false;
	}

	var tdDesc = document.getElementById("desc_TestData").value;	
	tdType = $("#testDataType_ul").find('option:selected').attr('id');
	if(pageName != "EDAT" && ((tdType==null)|| (tdType==undefined)|| (tdType==-1))){
		callAlert("Please choose Data Type");
		return false;
	}
	if(pageName == "EDAT") tdType='3';
	
	if((tdAttachName==null)|| (tdAttachName==undefined)|| ("" == tdAttachName)){
		callAlert("Please provide attachment");
		return false;
	}else{
		if(pageName=="EDAT"){
			var allowedFile = ['xml'];
			if($.inArray(tdAttachName.split('.')[1], allowedFile) == -1){
				callAlert("Please attach xml file");
				closeLoaderIcon();
				return false;
			}
		}else{
			var allowedFile;
			(tdType=='4') ? allowedFile=['zip'] : allowedFile=['xlsx'];
			if($.inArray(tdAttachName.split('.')[1], allowedFile) == -1){
				callAlert("Please attach "+ allowedFile[0] + " file");
				closeLoaderIcon();
				return false;
			}
		}
	}
	
	var fileLocation = tdAttachName;
	var file = fileLocation.split('\\');
	var fileName = file[file.length-1];
	//submitTestData();
	var uploadTestDataID = "uploadFileOftestData";
	var uploadFileName = "testDataFileName";	
	console.log("-- "+tdName," -- ",tdDesc," -- ",tdAttachName);
//	console.log("--- "+document.getElementById("treeHdnCurrentProductId").value," -- ",productId);

	var fileContent = document.getElementById("uploadFileOftestData").files[0];
	var formdata = new FormData();
	formdata.append("uploadTestData", fileContent);
	var url='update.testdata.attachment?fileName='+tdPrefixName+"&productVersion="+tdVersion;
	$.ajax({
		type: "POST",
		url: url,
		dataType:'json',
		//   data: {'name': tdName, 'description': tdDesc,'fileLocation':fileLocation,'fileName':fileName, 'uploadTestData': formdata},
		success: function(data) {
			$.unblockUI();
			if(data != null){
				if(data.Result=="OK"){
					//console.log('TestData error');
					//alert(data.Message);
					//var ans=confirm("This file is already existed.Would you want to override the file?");
					//var ans=confirm("Do you want to execute the Test Plan Group now?");
					if(data.Message == "Attachment already Exists."){
						var ans=confirm("This file already exists.Would you want to override the file?");
						if(ans){
							openLoaderIcon();
							url='product.version.testdata.add?productVersionId='+tdVersion+'&name='+tdName+'&description='+tdDesc+'&tdType='+tdType+'&fileLocation='+fileLocation+'&fileName='+fileName+'&prefixName='+tdPrefixName;	
							addAttachmentTRP(url,formdata);
						}
					}
					else{
						openLoaderIcon();
						url='product.version.testdata.add?productVersionId='+tdVersion+'&name='+tdName+'&description='+tdDesc+'&tdType='+tdType+'&fileLocation='+fileLocation+'&fileName='+fileName+'&prefixName='+tdPrefixName;	
						addAttachmentTRP(url,formdata);
					}
					/*if(ans){
	            			url='product.version.testdata.add?productVersionId='+tdVersion+'&name='+tdName+'&description='+tdDesc+'&tdType='+tdType+'&fileLocation='+fileLocation+'&fileName='+fileName;	
	            			addAttachmentTRP(url,formdata);
		            	}*/
					return false;
				}else{
					closeLoaderIcon();
					console.log('TestData Success');	 		    	
				}
			}		    	
		}		   
	});	 
//	url='product.version.testdata.add?productVersionId='+tdVersion+'&name='+tdName+'&description='+tdDesc+'&tdType='+tdType+'&fileLocation='+fileLocation+'&fileName='+fileName;	
//	addAttachment(url,formdata);	

}

function displayCreateTestDataTRP(){
	loadPopupTestData('div_testDataMain');
	$("#dataTypeHandlerID").show();
	if(pageName == "EDAT"){
		$('#div_testDataMain').find('.modal-title').replaceWith('<h4 class="modal-title">Upload EDAT Device Configuration</h4>');
		$('#div_testDataMain .attachLabel').html('File Attachment <font color="#efd125" size="4px"> * </font>:')
		$('#testData_submit').text('Upload');
		$("#dataTypeHandlerID").hide();
	}else {
		if(pageName == "OBJECTREPOSITORY"){
			$('#div_testDataMain').find('.modal-title').replaceWith('<h4 class="modal-title">Add Object Repository</h4>');
		}else if(pageName == "MYTESTDATA"){
			$('#div_testDataMain').find('.modal-title').replaceWith('<h4 class="modal-title">Add Test Data</h4>');
		}else if(pageName == "TESTDATAATTACHMENTS"){
			$('#div_testDataMain').find('.modal-title').replaceWith('<h4 class="modal-title">Add Test Data Attachments</h4>');
		}
	}
	initializeTestDataTRP();
}

function popupCloseTestData() {	
	$("#div_testDataMain").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");

	initializeTestDataTRP();
}

/* Load Poup function */
function loadPopupTestData(divId) {

	$("#" + divId).fadeIn(0500); // fadein popup div
	$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
	$("#div_PopupBackground").fadeIn(0001);
}

function addAttachmentTRP(url,formdata){
	$.ajax({
		type: "POST",
		url: url,
		method: 'POST',
		contentType: false,
		data: formdata,
		dataType:'json',
		processData: false,
		//   data: {'name': tdName, 'description': tdDesc,'fileLocation':fileLocation,'fileName':fileName, 'uploadTestData': formdata},
		success: function(data) {
			$.unblockUI();
			closeLoaderIcon();
			if(data.Result=="ERROR"){
				console.log('TestData error');
				alert(data.Message);
				return false;
			}else{
				console.log('TestData Success');	 		    	
			}
			popupCloseTestData();
			showTestDataInit(getTestTestDataJsonObj);
		},
		error: function(data){
			popupCloseTestData();
			console.log("error");
		},
		complete: function(data){
			popupCloseTestData();
			console.log("complete");		    	
		}
	}); 
}

//Test data item data table starts
function listTestDataItemsDataTable(productId){
	var url='testDataItems.list?productId='+ productId+ '&testDataFilterId='+ testDataFilterId+ '&jtStartIndex=0&jtPageSize=25000';
	var jsonObj={"Title":" Object Repository",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"componentUsageTitle",
	};
	listTestDataItemsDataTableContainer.init(jsonObj);	
}

var listTestDataItemsDataTableContainer = function() {
	var initialise = function(jsonObj){
		assignTestdataItemsDataTableValues(jsonObj, "ParentTable");
	};
	return {
		//main function to initiate the module
		init: function(jsonObj) {        	
			initialise(jsonObj);
		}		
	};	
}();

function assignTestdataItemsDataTableValues(jsonObj, tableValue, row, tr){
	openLoaderIcon();			
	$.ajax({
		type: "POST",
		url: jsonObj.url,
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
			options_Container(jsonObj, tableValue, row, tr);
		},
		error : function(data) {
			closeLoaderIcon();  
		},
		complete: function(data){
			closeLoaderIcon();
		}
	});	
}
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;
var optionsType="ItemType";
var isShareObj={};
var isShareArr=[];

function options_Container(data, tableValue, row, tr){
	optionsItemCounter=0;
	optionsResultArr=[];
	if(tableValue == "ParentTable"){
		optionsArr = [{url:'common.list.testdata.attachment?productId='+productId+'&attachmentType=1'}];
	}else if(tableValue == "ChildTableTestdataVaues"){
		optionsArr = [{url:'common.list.testdata.attachment?productId=13&attachmentType=1'}];
	}else{
		optionsArr = [{url:'testDataPlan.list.option?productId='+productId}];
	}
	returnOptionsItemForTestData(optionsArr[0].url, data, tableValue, row, tr);
}

function returnOptionsItemForTestData(url, data, tableValue, row, tr){
	$.ajax( {
		"type": "POST",
		"url":  url,
		"dataType": "json",
		success: function (json) {
			if(json.Result == "Error" || json.Options == null){
				callAlert(json.Message);
				json.Options=[];
				optionsResultArr.push(json.Options);         	
			}else{
				if(json.Options.length>0){     		   
					for(var i=0;i<json.Options.length;i++){
						json.Options[i].label=json.Options[i].DisplayText;
						json.Options[i].value=json.Options[i].Value;
					}			   
				}else{
					json.Options=[];
				}     	   
				optionsResultArr.push(json.Options);
				if(optionsItemCounter<optionsArr.length-1){
					returnOptionsItemForTestData(optionsArr[optionsResultArr.length].url,data);     		  
				}else{
					if(tableValue == "ParentTable"){
						isShareObj = {"Options":[{"value":"1","label":"YES","DisplayText":"YES"},
						                         {"value":"0","label":"NO","DisplayText":"NO"}
						]		
						};						
						isShareArr.push(isShareObj.Options);

						listTestdataDT_Container(data);   
					}else if(tableValue == "ChildTableTestdataVaues"){
						isShareObj = {"Options":[{"value":"1","label":"YES","DisplayText":"YES"},
						                         {"value":"0","label":"NO","DisplayText":"NO"}
						]		
						};						
						isShareArr.push(isShareObj.Options);
						listTestdataValuesDT_Container(data);   
					}else{
						listTestdataDT_Child_Container(data, row, tr);
					}
				}
				optionsItemCounter++;     	   
			}
		},
		error: function (data) {
			optionsItemCounter++;

		},
		complete: function(data){
			console.log('Completed');

		},	            
	});	
}

function testDataItemDataTable(){
	var childDivString ='<table id="testdata_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
	'<tr>'+
	'<th>Name</th>'+
	'<th>Type</th>'+
	'<th>Handle</th>'+
	'<th>Remarks</th>'+
	'<th>Date</th>'+
	'<th>Created By</th>'+
	/*'<th>Share</th>'+*/
	'<th></th>'+
	'</tr>'+
	'</thead>'+
	'<tfoot>'+
	'<tr>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	/*'<th></th>'+*/
	'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}

function listTestdataDT_Container(jsonObj){
	pageName="MYTESTDATA";
	try{
		if ($("#dataTableContainerForTestdata").children().length>0) {
			$("#dataTableContainerForTestdata").children().remove();
		}
	} 
	catch(e) {}
	var dt = new Date();
	var currDate = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();

	var childDivString = testDataItemDataTable(); 			 
	$("#dataTableContainerForTestdata").append(childDivString);

	editorTestdata = new $.fn.dataTable.Editor( {
		"table": "#testdata_dataTable",
		ajax: "testDataItems.save.and.update",
		ajaxUrl: "testDataItems.save.and.update",
		idSrc:  "dataName",
		i18n: {
			create: {
				title:  "Create a new Test Data",
				submit: "Create",
			}
		},
		fields: [{
			label: "Data Name *",
			name: "dataName",
		},{
			label: "ProductId",
			name: "productId",
			"type": "hidden",
			"def" : productId,
		},{
			label: "Type:",
			name: "type",
			options: optionsResultArr[0],
			"type"  : "select",
		},{
			label: "Handle:",
			name: "handle",
		}, {
			label: "Remarks:",
			name: "remarks",
		},{
			label: "Created Date",
			name: "createdDate",
			"type": "hidden",
			"def": currDate,
		},{
			label: "Created By",
			name: "createdBy",
			"type": "hidden"
		},{
			label: "testDataItemId",
			name: "testDataItemId",
			"type": "hidden"
		},{
			label: "Share",
			name: "isShare",
			options: isShareArr[0],
			"type": "hidden",
			"def":'1'
		},
		]
	});

	editorTestdata.on( 'preSubmit', function ( e, o, action ) {
		if(action == 'create'){
			for(var i=0;i<testdata_oTable.DataTable().data().length;i++){
				if(o.data[0].dataName == testdata_oTable.DataTable().data()[i].dataName 
						&& o.data[0].testDataPlanId == testdata_oTable.DataTable().data()[i].testDataPlanId){
					editorTestdata.error("The record already exists...");
					return false;
				}
			}
		}    		
		if ( action !== 'remove' ) {
			var dataName = this.field( 'dataName' );
			if ( ! dataName.isMultiValue() ) {
				if ( dataName.val() ) {
					var str = dataName.val();
					if(/^[a-zA-Z0-9-_. ]*$/.test(str) == false) {
						dataName.error( 'Please enter Valid Data name' );
						return false;
					}else if(/^[a-zA-Z0-9-_.]*$/.test(str) == false) {
						dataName.error( 'Please enter Data name without space.');
						return false;
					}
				}else{
					dataName.error( 'Please enter Data name' );
					return false;
				}
			}
		}
	} );

	testdata_oTable = $('#testdata_dataTable').dataTable( {
		dom: "Bfrtilp",
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "90%",
		"sScrollXInner": "100%",
		"scrollY":"300px",
		//"scrollY":"100%",
		"bScrollCollapse": true,
		"type": "POST",
		//"bPaginate": false,   
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = [6]; // search column TextBox Invisible Column position
			$('#testdata_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeTestdataDT();
		},
		buttons: [
		          { extend: "create", editor: editorTestdata },
		          {
		        	  text: 'Export File',
		        	  action: function ( e, dt, node, config ) {
		        		  uiObjectPopup();
		        	  }
		          },
		          {
		        	  text: 'Import File',
		        	  action: function ( e, dt, node, config ) {
		        		  displayCreateTestDataTRP();
		        	  }
		          },
		          {
		        	  text: 'Download Templates',
		        	  action: function ( e, dt, node, config ) {
		        		  downloadATSGTestdata('TESTDATAITEM');
		        	  }
		          },
		          {
		        	  extend: "collection",	 
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Test Data',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Test Data',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],
		          },
		          {
		        	  text: '<i title="Test Data Values" class="fa fa-table"></i>',
		        	  action: function ( e, dt, node ) {
		        		  var url='test.data.items.table.list?productId='+productId+'&jtStartIndex=0&jtPageSize=10000';
		        		  var jsonObj={"Title":" ChildTableTestdataVaues",
		        				"url": url,	
		        				"jtStartIndex":0,
			  					"jtPageSize":10000,				
			  					"componentUsageTitle":"ChildTableTestdataVaues",
		        		  };
		        		  assignTestdataItemsDataTableValues(jsonObj, "ChildTableTestdataVaues");
		        		  $("#ChildTableTestdataVaues_Child_Container h4").text("Test Data Values");
		        		  $("#ChildTableTestdataVaues_Child_Container").modal();
		        		  $(document).off('focusin.modal');
		        	  }
		          },
		          // 'colvis',
		          ],				        
		          aaData:jsonObj.data,		    				 
		          aoColumns: [	    			           
		                      { mData: "dataName", className: 'editable', sWidth: '18%'},			
		                      { mData: "type", className: 'disableEditInline', sWidth: '18%'},	
		                      { mData: "handle", className: 'disableEditInline', sWidth: '18%'},
		                      { mData: "remarks", className: 'editable', sWidth: '18%'},
		                      { mData: "createdDate", className: 'disableEditInline', sWidth: '18%'},
		                      { mData: "createdBy", className: 'disableEditInline', sWidth: '18%'},
		                      /*{ mData: "isShare", className: 'editable', sWidth: '12%', editField: "isShare",
		                    	  mRender: function (data, type, full) {
		                    		  data = optionsValueHandler(editorTestdata, 'isShare', full.isShare);
		                    		  return data;
		                    	  },
		                      },*/
		                      { mData: null,				 
		                    	  bSortable: false,
		                    	  mRender: function(data, type, full) {				            	
		                    		  var img = ('<div style="display: flex;">'+
		                    				  '<button style="border: none; background-color: transparent; outline: none;">'+
		                    				  '<img src="css/images/list_metro.png" class="details-control img1" title="Mapped Test Data Values" style="margin-left: 5px;"></button>'+
		                    				  '<button style="border: none; background-color: transparent; outline: none;">'+
		             							'<i class="fa fa-trash-o details-control deleteTestData" onClick="deleteTestData('+data.testDataItemId+')" title="Delete Testdata" style="padding-left: 0px;"></i></button>'+
		                    		  '</div>');	      		
		                    		  return img;
		                    	  }
		                      },				            
		                      ],
		                      rowCallback: function ( row, data ) {
		                    	  // $('input.editor-active', row).prop( 'checked', data.status == 1 );
		                      },
		                      "oLanguage": {
		                    	  "sSearch": "Search all columns:"
		                      },
	});	    		

	// ------
	$(function(){ // this will be called when the DOM is ready 

		// Activate an inline edit on click of a table cell
		$('#testdata_dataTable').on( 'click', 'tbody td.editable', function (e) {
			editorTestdata.inline( this, {
				submitOnBlur: true
			} );
		} );

		$("#testdata_dataTable_length").css('margin-top','8px');
		$("#testdata_dataTable_length").css('padding-left','35px');		

		$('#testdata_dataTable').on( 'change', 'input.editor-active', function () {
			editorTestdata
			.edit( $(this).closest('tr'), false )
			.set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
			.submit();
		});

		testdata_oTable.DataTable().columns().every( function () {
			var that = this;
			$('input', this.footer() ).on( 'keyup change', function () {
				if ( that.search() !== this.value ) {
					that
					.search( this.value, true, false )
					.draw();
				}
			} );
		} );

		$('#testdata_dataTable tbody').on('click', 'td button .img1', function () {
			var tr = $(this).closest('tr');
			var row = testdata_oTable.DataTable().row(tr);			
			setTimeout(function(){
				$( ".ui-tooltip" ).remove();
				var url='testDataItemValues.list?testDataItemId='+row.data().testDataItemId+'&jtStartIndex=0&jtPageSize=10000';
				var jsonObj={"Title":" Values",
						"url": url,	
						"jtStartIndex":0,
						"jtPageSize":10000,				
						"componentUsageTitle":"componentUsageTitle",
				};
				assignTestdataItemsDataTableValues(jsonObj, "ChildTable", row, tr);
				$("#listTestdataDT_Child_Container h4").text("Testdata Name: "+row.data().dataName+" ["+row.data().type+"]");
				$("#listTestdataDT_Child_Container").modal();
			},100);
		});

		var loginUser =	$('#userdisplayname').text().split('[')[0].trim();
		$(document).on('change', 'td.editable #DTE_Field_isShare', function() {	
			var tr = $(this).closest('tr');
			var row = testdata_oTable.DataTable().row(tr);
			if(row.data().createdBy.toUpperCase() != loginUser.toUpperCase()){
				callAlert("You do not have privilege to change the status.");
				return;
			}
		});
	});
}
var clearTimeoutTestdataDT='';
function reInitializeTestdataDT(){
	clearTimeoutTestcaseDT = setTimeout(function(){				
		testdata_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTestdataDT);
	},200);
}

function deleteTestData(testDataItemId){
	var fd = new FormData();
	fd.append("testDataItemId", testDataItemId);
	
	openLoaderIcon();
	$.ajax({
		url : 'product.testdata.delete',
		data : fd,
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
				callAlert(data.Message);
				listTestDataItemsDataTable(productId);	
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

//Child Table
function testDataItemChildDataTable(){
	var childDivString ='<table id="testdata_child_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
	'<tr>'+
	'<th>Test Data Plan</th>'+
	'<th>Test Data Values</th>'+
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


function listTestdataDT_Child_Container(jsonObj, row, tr){	
	try{
		if ($("#dataTableContainerForTestdataChildTable").children().length>0) {
			$("#dataTableContainerForTestdataChildTable").children().remove();
		}
	} 
	catch(e) {}

	var childDivString = testDataItemChildDataTable(); 			 
	$("#dataTableContainerForTestdataChildTable").append(childDivString);

	editorChildTestdata = new $.fn.dataTable.Editor( {
		"table": "#testdata_child_dataTable",
		ajax: "testDataItemValues.save.and.update",
		ajaxUrl: "testDataItemValues.save.and.update",
		idSrc:  "testDataItemValueId",
		i18n: {
			create: {
				title:  "Add Values",
				submit: "Create",
			}
		},
		fields: [{
			label: "Test Data Plan:",
			name: "testDataPlanId",
			options: optionsResultArr[0],
			"type"  : "select",
		}, {
			label: "Test Data Values:",
			name: "values",
		},{
			label: "ProductId",
			name: "productId",
			"type": "hidden",
			"def" : productId,
		},{
			label: "TestDataItemId",
			name: "testDataItemId",
			"type": "hidden",
			"def" : row.data().testDataItemId,
		},{
			label: "TestDataItemValueId",
			name: "testDataItemValueId",
			"type": "hidden",
			// "def" : row.data().testDataItemValueId,
		}
		]
	});

	$( 'input', editorChildTestdata.node()).on( 'focus', function () {
		this.select();
	});

	editorChildTestdata.on( 'preSubmit', function ( e, o, action ) {
		if ( action !== 'remove' ) {
			var elementName = editorChildTestdata.field('values');
			if ( ! elementName.isMultiValue() ) {
				if ( ! elementName.val() ) {
					elementName.error( 'Please enter value');
					return false;
				}
			}
			var str = elementName.val();
			if(row.data().type == 'Number') {
				if(/^[0-9-_.]*$/.test(str) == false) {
					elementName.error( 'Please enter Valid number format value' );
					return false;
				}
			} //else if(row.data().type == 'Text') {
				//Accepts Text/Alpha numeric 
				//if(/^[a-zA-Z].*/.test(str) == false) {
					/*elementName.error( 'Please enter Valid alpha numeric');
					return false;
				}*/
				//Accepts Except only numbers
				/*if(/^[0-9-_. ]*$/.test(str) == true) {
					elementName.error( 'Please enter Valid Test data Value' );
					return false;
				}*/
			//}
		}
		/*if(action == 'create'){
			for(var i=0;i<testdata_child_oTable.DataTable().data().length;i++){
				if(o.data[0].values == testdata_child_oTable.DataTable().data()[i].values &&
						o.data[0].testDataPlanId == testdata_child_oTable.DataTable().data()[i].testDataPlanId){
					this.error("The record already exists...");
					return false;
				}
			}
		}*/
	} );

	testdata_child_oTable = $('#testdata_child_dataTable').dataTable( {
		dom: "Bfrtilp",
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "90%",
		"sScrollXInner": "100%",
//		"scrollY": scrollYValue,
		//"scrollY":"100%",
		"bScrollCollapse": true,
		//"bPaginate": false,				        
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
			$('#testdata_child_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeTestdataChildDT();
		},
		buttons: [
		          { extend: "create", editor: editorChildTestdata },
		          {
		        	  extend: "collection",	 
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Product',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Product',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],
		          },
		          // 'colvis',
		          ],				        
		          aaData:jsonObj.data,		    				 
		          aoColumns: [	    			           
		                      { mData: "testDataPlanId", className: 'editable', sWidth: '20%', editField: "testDataPlanId",
		                    	  mRender: function (data, type, full) {
		                    		  data = optionsValueHandler(editorChildTestdata, 'testDataPlanId', full.testDataPlanId);
		                    		  return data;
		                    	  },
		                      },
		                      { mData: "values", className: 'editable', sWidth: '20%' },
		                      ],
		                      rowCallback: function ( row, data ) {
		                    	  // $('input.editor-active', row).prop( 'checked', data.status == 1 );
		                      },
		                      "oLanguage": {
		                    	  "sSearch": "Search all columns:"
		                      },
	});	    		

	// ------
	$(function(){ // this will be called when the DOM is ready 

		// Activate an inline edit on click of a table cell
		$('#testdata_child_dataTable').on( 'click', 'tbody td.editable', function (e) {
			editorChildTestdata.inline( this, {
				submitOnBlur: true
			} );
		} );

		$("#testdata_child_dataTable_length").css('margin-top','8px');
		$("#testdata_child_dataTable_length").css('padding-left','35px');		

		$('#testdata_child_dataTable').on( 'change', 'input.editorChildTestdata-active', function () {
			editorChildTestdata
			.edit( $(this).closest('tr'), false )
			.set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
			.submit();
		});

		testdata_child_oTable.DataTable().columns().every( function () {
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

var clearTimeoutTestdataChildDT='';
function reInitializeTestdataChildDT(){
	clearTimeoutTestdataChildDT = setTimeout(function(){				
		testdata_child_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTestdataChildDT);
	},200);
}
//END: ConvertDataTable - TestData

//BEGIN: ChildTableTestdataVaues
function testDataItemValuesDataTable(){
	var childDivString ='<table id="testdataValues_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
	'<tr>'+
	'<th>Name</th>'+
	'<th>Type</th>'+
	'<th>Handle</th>'+
	'<th>Val1</th>'+
	'<th>Val2</th>'+
	'<th>Val3</th>'+
	'<th>Val4</th>'+
	'<th>Val5</th>'+
	'<th>Val6</th>'+
	'<th>Val7</th>'+
	'<th>Val8</th>'+
	'<th>Val9</th>'+
	'<th>Val10</th>'+
	'<th>Val11</th>'+
	'<th>Val12</th>'+
	'<th>Val13</th>'+
	'<th>Val14</th>'+
	'<th>Val15</th>'+
	'<th>Val16</th>'+
	'<th>Val17</th>'+
	'<th>Val18</th>'+
	'<th>Val19</th>'+
	'<th>Val20</th>'+
	'<th>Val21</th>'+
	'<th>Val22</th>'+
	'<th>Val23</th>'+
	'<th>Val24</th>'+
	'<th>Val25</th>'+
	'<th>Val26</th>'+
	'<th>Val27</th>'+
	'<th>Val28</th>'+
	'<th>Val29</th>'+
	'<th>Val30</th>'+
	/*'<th></th>'+*/
	'</tr>'+
	'</thead>'+
	'<tfoot>'+
	'<tr>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	'<th></th>'+
	/*'<th></th>'+	*/
	'</tr>'+
	'</tfoot>'+	
	'</table>';		

	return childDivString;	
}

function listTestdataValuesDT_Container(jsonObj){
	try{
		if ($("#dataTableContainerForTestdataValues").children().length>0) {
			$("#dataTableContainerForTestdataValues").children().remove();
		}
	} 
	catch(e) {}
	var dt = new Date();
	var currDate = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();

	var childDivString = testDataItemValuesDataTable(); 			 
	$("#dataTableContainerForTestdataValues").append(childDivString);

	editorTestdataValues = new $.fn.dataTable.Editor( {
		"table": "#testdataValues_dataTable",
		ajax: "testDataItems.values.consolidated.save",
		ajaxUrl: "testDataItemValues.consolidatedView.save.and.update",
		idSrc:  "dataName",
		i18n: {
			create: {
				title:  "Create a new Test Data Value",
				submit: "Create",
			}
		},
		fields: [{
			label: "Name *",
			name: "dataName",
		},{
			label: "ProductId",
			name: "productId",
			"type": "hidden",
			"def" : productId,
		},{
			label: "Type:",
			name: "type",
			options: optionsResultArr[0],
			"type"  : "select",
		},{
			label: "Handle:",
			name: "handle",
		},{
			label: "Created Date",
			name: "createdDate",
			"type": "hidden",
			"def": currDate,
		},{
			label: "Created By",
			name: "createdBy",
			"type": "hidden"
		},{
			label: "testDataItemId",
			name: "testDataItemId",
			"type": "hidden"
		},{
			label: "value1",
			name: "value1",
		},{
			label: "value2",
			name: "value2",
		},{
			label: "value3",
			name: "value3",
		},{
			label: "value4",
			name: "value4",
		},{
			label: "value5",
			name: "value5",
		},{
			label: "value6",
			name: "value6",
		},{
			label: "value7",
			name: "value7",
		},{
			label: "value8",
			name: "value8",
		},{
			label: "value9",
			name: "value9",
		},{
			label: "value10",
			name: "value10",
		},{
			label: "value11",
			name: "value11",
		},{
			label: "value12",
			name: "value12",
		},{
			label: "value13",
			name: "value13",
		},{
			label: "value14",
			name: "value14",
		},{
			label: "value15",
			name: "value15",
		},{
			label: "value16",
			name: "value16",
		},{
			label: "value17",
			name: "value17",
		},{
			label: "value18",
			name: "value18",
		},{
			label: "value19",
			name: "value19",
		},{
			label: "value20",
			name: "value20",
		},{
			label: "value21",
			name: "value21",
		},{
			label: "value22",
			name: "value22",
		},{
			label: "value23",
			name: "value23",
		},{
			label: "value24",
			name: "value24",
		},{
			label: "value25",
			name: "value25",
		},{
			label: "value26",
			name: "value26",
		},{
			label: "value27",
			name: "value27",
		},{
			label: "value28",
			name: "value28",
		},{
			label: "value29",
			name: "value29",
		},{
			label: "value30",
			name: "value30",
		}
		]
	});

	testdataValues_oTable = $('#testdataValues_dataTable').dataTable( {
		dom: "Bfrtilp",
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"ordering": false,
		"sScrollX": "90%",
		"sScrollXInner": "100%",
		"scrollY":"300px",
		"bScrollCollapse": true,
		"type": "POST",
		"fnInitComplete": function(data) {
			var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
			$('#testdataValues_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
			reInitializeTestdataValuesDT();
		},
		buttons: [
		          { extend: "create", editor: editorTestdataValues },
		          {
		        	  extend: "collection",	 
		        	  text: 'Export',
		        	  buttons: [
		        	            {
		        	            	extend: 'excel',
		        	            	title: 'Test Data Values',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            {
		        	            	extend: 'csv',
		        	            	title: 'Test Data Values',
		        	            	exportOptions: {
		        	            		columns: ':visible'
		        	            	}
		        	            },
		        	            ],
		          }
		          // 'colvis',
		          ],				        
		          aaData:jsonObj.data,		    				 
		          aoColumns: [	    			           
		                      { mData: "dataName", className: 'editable', sWidth: '8%'},			
		                      { mData: "type", className: 'disableEditInline', sWidth: '5%'},	
		                      { mData: "handle", className: 'disableEditInline', sWidth: '7%'},
		                      { mData: "value1", className: 'editable', sWidth: '4%'},
		                      { mData: "value2", className: 'editable', sWidth: '4%'},
		                      { mData: "value3", className: 'editable', sWidth: '4%'},
		                      { mData: "value4", className: 'editable', sWidth: '4%'},
		                      { mData: "value5", className: 'editable', sWidth: '4%'},
		                      { mData: "value6", className: 'editable', sWidth: '4%'},
		                      { mData: "value7", className: 'editable', sWidth: '4%'},
		                      { mData: "value8", className: 'editable', sWidth: '4%'},
		                      { mData: "value9", className: 'editable', sWidth: '4%'},
		                      { mData: "value10", className: 'editable', sWidth: '4%'},
		                      { mData: "value11", className: 'editable', sWidth: '4%'},
		                      { mData: "value12", className: 'editable', sWidth: '4%'},
		                      { mData: "value13", className: 'editable', sWidth: '4%'},
		                      { mData: "value14", className: 'editable', sWidth: '4%'},
		                      { mData: "value15", className: 'editable', sWidth: '4%'},
		                      { mData: "value16", className: 'editable', sWidth: '4%'},
		                      { mData: "value17", className: 'editable', sWidth: '4%'},
		                      { mData: "value18", className: 'editable', sWidth: '4%'},
		                      { mData: "value19", className: 'editable', sWidth: '4%'},
		                      { mData: "value20", className: 'editable', sWidth: '4%'},
		                      { mData: "value21", className: 'editable', sWidth: '4%'},
		                      { mData: "value22", className: 'editable', sWidth: '4%'},
		                      { mData: "value23", className: 'editable', sWidth: '4%'},
		                      { mData: "value24", className: 'editable', sWidth: '4%'},
		                      { mData: "value25", className: 'editable', sWidth: '4%'},
		                      { mData: "value26", className: 'editable', sWidth: '4%'},
		                      { mData: "value27", className: 'editable', sWidth: '4%'},
		                      { mData: "value28", className: 'editable', sWidth: '4%'},
		                      { mData: "value29", className: 'editable', sWidth: '4%'},
		                      { mData: "value30", className: 'editable', sWidth: '4%'},
		                      /*{ mData: null,				 
		                    	  bSortable: false,
		                    	  mRender: function(data, type, full) {				            	
		                    		  var img = ('<div style="display: flex;">'+
		                    				  '<button style="border: none; background-color: transparent; outline: none;">'+
		             							'<i class="fa fa-trash-o details-control deleteTestData" onClick="deleteTestData('+data.testDataItemId+')" title="Delete Testdata" style="padding-left: 0px;"></i></button>'+
		                    		  '</div>');	      		
		                    		  return img;
		                    	  }
		                      },*/				            
		                      ],
		                      rowCallback: function ( row, data ) {
		                    	  // $('input.editor-active', row).prop( 'checked', data.status == 1 );
		                      },
		                      "oLanguage": {
		                    	  "sSearch": "Search all columns:"
		                      },
	});	    		

	// ------
	$(function(){ // this will be called when the DOM is ready 

		// Activate an inline edit on click of a table cell
		$('#testdataValues_dataTable').on( 'click', 'tbody td.editable', function (e) {
			/*for(var i=1;i<=20;i++){
				editorTestdataValues.field('value'+i).show();
			}*/
			editorTestdataValues.field('value1').show();
			editorTestdataValues.field('value2').show();
			editorTestdataValues.field('value3').show();
			editorTestdataValues.field('value4').show();
			editorTestdataValues.field('value5').show();
			editorTestdataValues.field('value6').show();
			editorTestdataValues.field('value7').show();
			editorTestdataValues.field('value8').show();
			editorTestdataValues.field('value9').show();
			editorTestdataValues.field('value10').show();
			editorTestdataValues.field('value11').show();
			editorTestdataValues.field('value12').show();
			editorTestdataValues.field('value13').show();
			editorTestdataValues.field('value14').show();
			editorTestdataValues.field('value15').show();
			editorTestdataValues.field('value16').show();
			editorTestdataValues.field('value17').show();
			editorTestdataValues.field('value18').show();
			editorTestdataValues.field('value19').show();
			editorTestdataValues.field('value20').show();
			editorTestdataValues.field('value21').show();
			editorTestdataValues.field('value22').show();
			editorTestdataValues.field('value23').show();
			editorTestdataValues.field('value24').show();
			editorTestdataValues.field('value25').show();
			editorTestdataValues.field('value26').show();
			editorTestdataValues.field('value27').show();
			editorTestdataValues.field('value28').show();
			editorTestdataValues.field('value29').show();
			editorTestdataValues.field('value30').show();

			editorTestdataValues.inline( this, {
				submitOnBlur: true
			} );
		} );
		
		$('.buttons-create').on('click', function(){
			/*for(var i=1;i<=20;i++){
				editorTestdataValues.field('value'+i).hide();
			}*/
			editorTestdataValues.field('value1').hide();
			editorTestdataValues.field('value2').hide();
			editorTestdataValues.field('value3').hide();
			editorTestdataValues.field('value4').hide();
			editorTestdataValues.field('value5').hide();
			editorTestdataValues.field('value6').hide();
			editorTestdataValues.field('value7').hide();
			editorTestdataValues.field('value8').hide();
			editorTestdataValues.field('value9').hide();
			editorTestdataValues.field('value10').hide();
			editorTestdataValues.field('value11').hide();
			editorTestdataValues.field('value12').hide();
			editorTestdataValues.field('value13').hide();
			editorTestdataValues.field('value14').hide();
			editorTestdataValues.field('value15').hide();
			editorTestdataValues.field('value16').hide();
			editorTestdataValues.field('value17').hide();
			editorTestdataValues.field('value18').hide();
			editorTestdataValues.field('value19').hide();
			editorTestdataValues.field('value20').hide();
			editorTestdataValues.field('value21').hide();
			editorTestdataValues.field('value22').hide();
			editorTestdataValues.field('value23').hide();
			editorTestdataValues.field('value24').hide();
			editorTestdataValues.field('value25').hide();
			editorTestdataValues.field('value26').hide();
			editorTestdataValues.field('value27').hide();
			editorTestdataValues.field('value28').hide();
			editorTestdataValues.field('value29').hide();
			editorTestdataValues.field('value30').hide();
		});

		$("#testdataValues_dataTable_length").css('margin-top','8px');
		$("#testdataValues_dataTable_length").css('padding-left','35px');		
		
		$('#testdataValues_dataTable').on( 'change', 'input.editor-active', function () {
			editorTestdataValues
			.edit( $(this).closest('tr'), false )
			.set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
			.submit();
		});

		testdataValues_oTable.DataTable().columns().every( function () {
			var that = this;
			$('input', this.footer() ).on( 'keyup change', function () {
				if ( that.search() !== this.value ) {
					that
					.search( this.value, true, false )
					.draw();
				}
			} );
		} );		
		
		editorTestdataValues.on( 'preSubmit', function ( e, o, action ) {			   		
			if ( action !== 'remove' ) {
				var dataName = this.field( 'dataName' );
				if ( ! dataName.isMultiValue() ) {
					if ( dataName.val() ) {
						var str = dataName.val();
						if(/^[a-zA-Z0-9-_. ]*$/.test(str) == false) {
							dataName.error( 'Please enter Valid name' );
							return false;
						}else if(/^[a-zA-Z0-9-_.]*$/.test(str) == false) {
							dataName.error( 'Please enter name without space.');
							return false;
						}
					}else{
						dataName.error( 'Please enter name' );
						return false;
					}
				}
			}
		} );
		
	});
}
var clearTimeoutTestdataValuesDT='';
function reInitializeTestdataValuesDT(){
	clearTimeoutTestcaseValuesDT = setTimeout(function(){				
		testdataValues_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTestdataValuesDT);
	},200);
}

//END: ChildTableTestdataVaues

//BEGIN: ConverDataTable - ObjectRepository
var elementTypeArr=[],idTypeArr=[],applicationTypeArr=[];
var isShareObj={};
var isShareArr=[];
function objectRepositoryListDataTable(productId){
	elementTypeArr = ['Text','Password','Checkbox','Scrollable Checkbox','Checkbox List','Dropdown','Radio Button','List','Text Area','Option','File Select box','Select','List Item','Submit','Reset','Combo box','iFrame','Anchor Link','div'];
	idTypeArr = ['id','xpath','cssSelector','className','tagName','linkText','name','partialLinkText','cssContainingText','buttonText','partialButtonText','binding','repeater','model','src'];
	applicationTypeArr = ['Web','Mobile','Desktop'];
	isShareObj = {"Options":[{"value":"1","label":"YES","DisplayText":"YES"},
	                         {"value":"0","label":"NO","DisplayText":"NO"}
							]		
				};						
	isShareArr.push(isShareObj.Options);
	var url='uiObjects.list?productId='+ productId+ '&objRepoFilterId='+ objRepoId+ '&jtStartIndex=0&jtPageSize=25000';
	 var jsonObj={"Title":" Object Repository",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"componentUsageTitle",
	};
	objectRepositoryDataTableContainer.init(jsonObj);
}

var objectRepositoryDataTableContainer = function() {
 	var initialise = function(jsonObj){
		assignObjectRepositoryDataTableValues(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignObjectRepositoryDataTableValues(jsonObj){
	openLoaderIcon();			
	 $.ajax({
		  type: "POST",
		  url: jsonObj.url,
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
			objectRepositoryDT_Container(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function objectRepositoryDataTable(){
	var childDivString ='<table id="objectRepository_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th rowspan="3">Name</th>'+
			'<th rowspan="3">Description</th>'+
			'<th rowspan="3">Group</th>'+
			'<th rowspan="3">Handle</th>'+
			'<th rowspan="3">Created Date</th>'+
			'<th rowspan="3">Created By</th>'+
			/*'<th rowspan="3">Share</th>'+*/
			'<th rowspan="3">Element Type</th>'+
			'<th rowspan="3">Page Name</th>'+
			'<th rowspan="3">Page URL</th>'+
			'<th rowspan="3">Id Type</th>'+
			'<th colspan="7">Web</th>'+
			'<th colspan="3">Mobile</th>'+
			'<th rowspan="3">Appium</th>'+	
			'<th rowspan="3">CodedUI</th>'+	
			'<th rowspan="3">TestComplete</th>'+
			'<th rowspan="3">Application Type</th>'+
			'<th rowspan="3"></th>'+
		'</tr>'+
		'<tr>'+
			'<th rowspan="2">Label</th>'+
			'<th rowspan="2">Chrome</th>'+
			'<th rowspan="2">FireFox</th>'+
			'<th rowspan="2">IE</th>'+
			'<th rowspan="2">Safari</th>'+
			'<th rowspan="2">FireFoxGecko</th>'+
			'<th rowspan="2">Edge</th>'+
			'<th colspan="3">SeeTest</th>'+
		'</tr>'+
		'<tr>'+
			'<th>Zone</th>'+
			'<th>Index</th>'+
			'<th>Label</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function objectRepositoryDT_Container(jsonObj){
	pageName="OBJECTREPOSITORY";
	try{
		if ($("#dataTableContainerForObjectRepository").children().length>0) {
			$("#dataTableContainerForObjectRepository").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = objectRepositoryDataTable(); 			 
	$("#dataTableContainerForObjectRepository").append(childDivString);
	
	editorObjectRepository = new $.fn.dataTable.Editor( {
	    "table": "#objectRepository_dataTable",
		ajax: "uiObjects.save.and.update",
		ajaxUrl: "uiObjects.save.and.update",
		idSrc:  "elementName",
		i18n: {
	        create: {
	            title:  "Create a new UI Object Element",
	            submit: "Create",
	        }
	    },
		fields: [
		{
			label: "Product Id",
            name: "productId",
            "type": "hidden",
            "def" : productId,
            
        },{
			label: "uiObject ItemId",
            name: "uiObjectItemId",
            "type": "hidden",
            //"def" : uiObjectItemId,
        },{
            label: "Name*",
            name: "elementName",
        },{
            label: "Description",
            name: "description",
        },{
            label: "Group",
            name: "groupName",
        },{
            label: "Handle",
            name: "handle",
        },{
            label: "Created Date",
            name: "createdDate",
            "type":"hidden",
        },{
            label: "Created By",
            name: "createdBy",
            "type":"hidden",
        },{
            label: "Share",
            name: "isShare",
            options: isShareArr[0],
            "type": "hidden",
            "def":'1'
        },{
            label: "Element Type*",
            name: "elementType",
            options: elementTypeArr,
            "type":	"select"
        },{
            label: "Page Name",
            name: "pageName",
        },{
            label: "Page URL",
            name: "pageURL",
        },{
            label: "Id Type*",
            name: "idType",
            options: idTypeArr,
            "type":	"select"
        },{
            label: "Label",
            name: "weblabel",
        },{
            label: "Chrome",
            name: "chrome"
        },{
            label: "FireFox",
            name: "firefox",	                
        },
        {
            label: "IE",
            name: "ie",
        },
        {
            label: "Safari",
            name: "safari",
        },
        {
            label: "FireFoxGecko",
            name: "firefoxgecko",
        },{
            label: "Edge",
            name: "edge",
        },
        {
            label: "SeeTest Zone",
            name: "seeTestZoneIndex",
        },
        {
            label: "SeeTest Index",
            name: "seeTestIndex",
        },
        {
            label: "SeeTest Label",
            name: "seetestLabel",
        },
        {
            label: "Appium",
            name: "appiumLabel",
        },
        {
            label: "CodedUI",
            name: "codeduiLabel",
        },
        {
            label: "TestComplete",
            name: "testCompleteLabel",
        },
        {
            label: "Application Type",
            name: "testEngineName",
            options: applicationTypeArr,
            "type":	"select"
        }        
    ]
	});
	
	editorObjectRepository.on( 'preSubmit', function ( e, o, action ) {
        if ( action !== 'remove' ) {
        	var elementName = editorObjectRepository.field('elementName');
            if ( ! elementName.isMultiValue() ) {
                if ( elementName.val() ) {
                	var str = elementName.val();
                	if(/^[a-zA-Z0-9-_. ]*$/.test(str) == false) {
                		elementName.error( 'Please enter Valid name');return false;
                	}else if(/^[a-zA-Z0-9-_.]*$/.test(str) == false) {
                		elementName.error( 'Please enter name without space.');return false;
                	}
                }else{
                	elementName.error( 'Please enter name' );return false;
            	}
            }
        }
        if(action == 'create'){
			for(var i=0;i<objectRepositoryDT_oTable.DataTable().data().length;i++){
    			if(o.data[0].elementName == objectRepositoryDT_oTable.DataTable().data()[i].elementName){
    				this.error("The record already exists...");
    				return false;
    			}
			}
		}
    } );
	
	objectRepositoryDT_oTable = $("#objectRepository_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "90%",
	       "sScrollXInner": "100%",
	       "scrollY":"280px",
	       "bScrollCollapse": true,	 
	       "aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [24]; // search column TextBox Invisible Column position
	     		  $('#objectRepository_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeObjectRepositoryDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorObjectRepository },								 
						{
						    text: 'Export File',
						    action: function ( e, dt, node, config ) {
						    	uiObjectPopup();
						    }
						},
						{
						    text: 'Import File',
						    action: function ( e, dt, node, config ) {
						    	displayCreateTestDataTRP();
						    }
						},
						{
						    text: 'Download Templates',
						    action: function ( e, dt, node, config ) {
						    	downloadATSGTestdata('UIOBJECTREPOSITORY');
						    }
						 },
						 {
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'UI Object Repository',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'UI Object Repository',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		//    'colvis'
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "elementName", className: 'editable', sWidth: '15%' },	
           { mData: "description", className: 'editable', sWidth: '12%' },		
           { mData: "groupName", className: 'editable', sWidth: '15%' },	
           { mData: "handle", className: 'editable', sWidth: '12%' },		
           { mData: "createdDate", className: 'disableEditInline', sWidth: '15%' },	
           { mData: "createdBy", className: 'disableEditInline', sWidth: '12%' },
           /*{ mData: "isShare", className: 'editable', sWidth: '12%', editField: "isShare",
           	mRender: function (data, type, full) {
	           		data = optionsValueHandler(editorObjectRepository, 'isShare', full.isShare);
		           	return data;
	             },
			},*/
           { mData: "elementType", className: 'editable', sWidth: '17%', editField: "elementType",
            	mRender: function (data, type, full) {
		             if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(editorObjectRepository, 'elementType', full.elementType);
		           	 }
		           	 else if(type == "display"){
		           		data = full.elementType;
		           	 }	           	 
		             return data;
	             },
            },
            { mData: "pageName", className: 'editable', sWidth: '15%' },	
            { mData: "pageURL", className: 'editable', sWidth: '12%' },		
            { mData: "idType", className: 'editable', sWidth: '12%', editField: "idType",
            	mRender: function (data, type, full) {
		             if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(editorObjectRepository, 'idType', full.idType);
		           	 }
		           	 else if(type == "display"){
		           		data = full.idType;
		           	 }	           	 
		             return data;
	             },
			}, 		
           { mData: "weblabel", className: 'editable', sWidth: '12%' },	
           { mData: "chrome", className: 'editable', sWidth: '15%' },	
           { mData: "firefox", className: 'editable', sWidth: '12%' },		
           { mData: "ie", className: 'editable', sWidth: '15%' },	
           { mData: "safari", className: 'editable', sWidth: '12%' },
           { mData: "firefoxgecko", className: 'editable', sWidth: '12%' },	
           { mData: "edge", className: 'editable', sWidth: '12%' },	
           { mData: "seeTestZoneIndex", className: 'editable', sWidth: '15%' },	
           { mData: "seeTestIndex", className: 'editable', sWidth: '12%' },
           { mData: "seetestLabel", className: 'editable', sWidth: '12%' },
           { mData: "appiumLabel", className: 'editable', sWidth: '15%' },
           { mData: "codeduiLabel", className: 'editable', sWidth: '5%' },
           { mData: "testCompleteLabel", className: 'editable', sWidth: '5%' },
           { mData: "testEngineName", className: 'editable', sWidth: '15%',
            	mRender: function (data, type, full) {
		             if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(editorObjectRepository, 'testEngineName', full.testEngineName);
		           	 }
		           	 else if(type == "display"){
		           		data = full.testEngineName;
		           	 }	           	 
		             return data;
	            },
           },
           { mData: null,				 
         	  bSortable: false,
         	  mRender: function(data, type, full) {				            	
         		  var img = ('<div style="display: flex;">'+
         				  '<button style="border: none; background-color: transparent; outline: none;">'+
  							'<i class="fa fa-trash-o details-control" onClick="deleteObjectRepository('+data.uiObjectItemId+')" title="Delete Objectrepository" style="padding-left: 0px;"></i></button>'+
         		  '</div>');	      		
         		  return img;
         	  }
           },
       ],       
       rowCallback: function ( row, data ) {
       },
       "oLanguage": {
            "sSearch": "Search all columns:"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#objectRepository_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorObjectRepository.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$(document).on('change', '#DTE_Field_idType', editorObjectRepository , function() {
		var idVal = editorObjectRepository.field('idType').val();
		if(idVal == 'xpath' || idVal == 'cssSelector'){
			editorObjectRepository.field('chrome').show();
			editorObjectRepository.field('firefox').show();
			editorObjectRepository.field('safari').show();
			editorObjectRepository.field('ie').show();
			editorObjectRepository.field('firefoxgecko').show();
			editorObjectRepository.field('edge').show();
		}else{
			editorObjectRepository.field('chrome').hide();
			editorObjectRepository.field('firefox').hide();
			editorObjectRepository.field('safari').hide();
			editorObjectRepository.field('ie').hide();
			editorObjectRepository.field('firefoxgecko').hide();
			editorObjectRepository.field('edge').hide();
		}
	});
	
	$("#objectRepository_dataTable_length").css('margin-top','8px');
	$("#objectRepository_dataTable_length").css('padding-left','35px');		

	objectRepositoryDT_oTable.DataTable().columns().every( function () {
    var that = this;
    $('input', this.footer() ).on( 'keyup change', function () {
        if ( that.search() !== this.value ) {
            that
            	.search( this.value, true, false )
                .draw();
        }
    } );
	} );

	$('#objectRepository_dataTable tbody').on('click', 'td .img1', function () {
		var tr = $(this).closest('tr');
    	var row = objectRepositoryDT_oTable.DataTable().row(tr);	    	

    	var result = (row.data().testCaseId
				+ '~'
				+ row.data().testCaseName
				+ '~' + 'web').toString();
    	scriptsFor = "TestCase";
		displayATSGPopupHandler(result);
	});
	
	var loginUser =	$('#userdisplayname').text().split('[')[0].trim();
	$(document).on('change', 'td.editable #DTE_Field_isShare', function() {	
		var tr = $(this).closest('tr');
		var row = objectRepositoryDT_oTable.DataTable().row(tr);
		if(row.data().createdBy.toUpperCase() != loginUser.toUpperCase()){
			callAlert("You do not have privilege to change the status.");
			return;
		}
	});
}

var objectRepositoryDT_oTable='';
var clearTimeoutObjectRepositoryDT='';
function reInitializeObjectRepositoryDT(){
	clearTimeoutObjectRepositoryDT = setTimeout(function(){				
		objectRepositoryDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutObjectRepositoryDT);
	},200);
}

function deleteObjectRepository(uiObjectItemId){
	var fd = new FormData();
	fd.append("uiObjectItemId", uiObjectItemId);
	
	openLoaderIcon();
	$.ajax({
		url : 'product.objectrepository.delete',
		data : fd,
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
				callAlert(data.Message);
				objectRepositoryListDataTable(productId);
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
//END: ConverDataTable - ObjectRepository


$(document).on('change','#objRepoFilter_ul',function() {
	objRepoId = $("#objRepoFilter_ul").find('option:selected').val();
	if (objRepoId == 1) {
		statusListingFlag = false;
	} else {
		statusListingFlag = true;
	}
	objectRepositoryListDataTable(productId);
});

function trim(str) {
    return str.replace(/^\s+|\s+$/g,"");
}

function download(str,path) {
	var urlfinal="rest/download/documents?type="+str+'&path='+path;
  	parent.window.location.href=urlfinal;
	
}
function downloadATSGTestdata(str) {
	var urlfinal="rest/download/documents?type="+str;
  	parent.window.location.href=urlfinal;
	
}

function uiObjectPopup(){
	$("#div_uiobjectExportPopup").modal();
	$("#div_uiobjectExportPopup h4").text('UI Object Repository');
	var testDataName = $("#testdata_dataTable").is(':visible'); 
	if(testDataName){
		$("#div_uiobjectExportPopup h4").text('Test Data');
		$('.fileHandler_dd').removeClass('hidden');
		fileHandlerDropdown();
	}else{
		$('.fileHandler_dd').addClass('hidden');
	}
}

function fileHandlerDropdown() {
	$('#fileHandler_ul').empty();
	var exportDropDown="";
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : 'testDataPlan.list.option?productId='+productId,
		dataType : "json",
		success : function(data) {
			if (data.Result == "OK") {
				var ary = (data.Options);
				//exportDropDown = "<option id='-1'>ALL</option>";
				$.each(ary, function(index, element) {
					exportDropDown += '<option id="' + element.Value +  '" ><a href="#">'
						+ element.DisplayText
						+ '</a></option>';
					});
				$("#fileHandler_ul").append(exportDropDown);
				$('#fileHandler_ul').select2();
			} else if (data.Result == "ERROR") {
				callAlert(data.Message);
			}
		},
		error : function(data) {
			console.log("error in ajax call");
		},
		complete : function(data) {
			console.log("complete in ajax call");
		}
	});
}
function uiObjectExport(selectedRadioValue,testdataPlanSelectedValue){
	selectedTab = $("#testsRadioGrp ").find("label.active").index();
	var selectedTabTitle = $("#testsRadioGrp ").find("label.active")[0].textContent;
	if(selectedTab != undefined  && selectedTabTitle == 'Test Data'){
		url = 'testData.export?productId='+productId+'&type='+selectedRadioValue+'&testDataPlanId='+testdataPlanSelectedValue;
		testData = '';
	}
	else if(selectedTab != undefined  && selectedTabTitle == 'Object Repository'){
		var url = 'uiobject.export?productId='+productId+'&type='+selectedRadioValue;
	}
	/*var url = 'uiobject.export?projectId='+productId+'&type= all';*/ //  irrespective of handle
	 $.ajax({
		    type: "POST",
		    url: url,
	   	  dataType:'json',
		    success: function(data) {
		    	$.unblockUI();
		    	if(data != null){
		    		if(data.Result=="OK"){
		    			var urlfinal="rest/download/excelReport?fileName="+data.exportFileName;
		    		  	parent.window.location.href=urlfinal;			    		
			    	}else{
			    		console.log(' Success');	 		 
			    		callAlert(data.Message);
			    	}
		    	}
		    	
		    	
		    }
	 
		   
		}); 
	
}

// started eDAT

//BEGIN: ConvertDataTable - eData
function eDataDataTable(){
	tdType='3';
	var url = 'productId.testobjectdata.attachments.list?productId='+productId+'&jtStartIndex=0&jtPageSize=10000';
	var jsonObj = 	{
						"Title":"EData",
						"url":url,
						"jtStartIndex":0,
						"jtPageSize":1000,
						"componentUsageTitile":"Test Data Plan"
					};
	assignEDataDataTableValues(jsonObj);
};

function assignEDataDataTableValues(jsonObj){
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
			eDataDataTableContainer(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function eDataDataTableHeader(){
	var childDivString = '<table id="eData_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
				'<th>Attachment</th>'+
				'<th>Description</th>'+
				'<th>Attachment Type</th>'+
				'<th>File</th>'+
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
var eDataDT_oTable='';
var pageName='';
function eDataDataTableContainer(jsonObj){
	pageName = "EDAT";
	
	try{
		if ($("#dataTableContainerForEData").children().length>0) {
			$("#dataTableContainerForEData").children().remove();
		}
	} 
	catch(e) {}	
	
	var childDivString = eDataDataTableHeader(); 			 
	$("#dataTableContainerForEData").append(childDivString);
	
	editorEData = new $.fn.dataTable.Editor( {
	    "table": "#eData_dataTable",
		//ajax: "testDataPlan.save.and.update",
		//ajaxUrl: "testDataPlan.save.and.update",
		//idSrc:  "testDataPlanId",
		i18n: {
	        create: {
	            title:  "Create a new EDAT",
	            submit: "Create",
	        }
	    },
		fields: [
		{
				label: "Attachment",
				name: "attachmentName",
				//"type": "hidden",
	     },{
                label: "productId",
                name: "productId",
                "type": "hidden",
                "def" : productId,
	     },{
                label: "Description",
                name: "description",
	     },{
                label: "Attachment Type",
                name: "attachmentType",
	     },{
                label: "File",
                name: "attributeFileName",
        }
    ]
	});
	
	eDataDT_oTable = $("#eData_dataTable").dataTable( {
		"dom":'Bfrtilp',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
	    "sScrollX": "90%",
       "sScrollXInner": "100%",
       "scrollY":"300px",
       "bScrollCollapse": true,	 
       "fnInitComplete": function(data) {
    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
 		  $('#eData_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
     	  reInitializeEDataDT();
	   },  
	   buttons: [
				 { 
					extend: "create",
					editor: editorEData,
					action: function ( e, dt, node, config ) {						
						displayCreateTestDataTRP();
	                }
				 },
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'My Edat',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'My Edat',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                ],	                
	            }
	            //'colvis'
	           ], 
          aaData:jsonObj.data,		    				 
          aoColumns: [	        	        
		       { mData: "attachmentName", className: 'disableEditInline', sWidth: '20%' },		
		       { mData: "description", className: 'disableEditInline', sWidth: '20%' },		
			   { mData: "attachmentType", className: 'disableEditInline', sWidth: '20%' },		
		       { mData: "attributeFileName", className: 'disableEditInline', sWidth: '20%',
		    	   "render": function(data, type, row, meta){
		               if(type === 'display'){
		                   data = '<a href="' + data + '">' + data +'.xml'+ '</a>';
		               }
		               return data;
		            }   
		       },		
		   ],
		   "oLanguage": {
		       "sSearch": "Search all columns:"
		   },     
	}); 
	// Activate an inline edit on click of a table cell
	$('#eData_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorEdata.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#eData_dataTable').on( 'click', 'tbody td > a', function (e) {
		var tr = $(this).closest('tr');
    	var row = eDataDT_oTable.DataTable().row(tr);
    	var path = row.data().attributeFileURI;	 
    	download("EDAT",path);
		return false;
	});
	
	$("#eData_dataTable_length").css('margin-top','8px');
	$("#eData_dataTable_length").css('padding-left','35px');		

	eDataDT_oTable.DataTable().columns().every( function () {
    var that = this;
    $('input', this.footer() ).on( 'keyup change', function () {
        if ( that.search() !== this.value ) {
            that
            	.search( this.value, true, false )
                .draw();
        }
    } );
	} );

}
var clearTimeoutEDataDT='';
function reInitializeEDataDT(){
	clearTimeoutEDataDT = setTimeout(function(){				
		eDataDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutEDataDT);
	},200);
}

//END: ConvertDataTable - EData