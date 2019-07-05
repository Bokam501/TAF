function loadDefectInfo(workPackageId, nodeType){	
	 if(nodeType !='WorkPackage'){
	 	setWorkPackageNode();
	 }
	$('#Defects').children().show();
	
	document.getElementById("ResultsGridView").style.display = "none";
	
	setTimeout(function(){loadDefectInfoWorkpackageDataTable(workPackageId);},1000);
	
	/*try{
		if ($('#jTableContainerDefects').length>0) {
	
			 $('#jTableContainerDefects').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainerDefects').jtable({
       title: 'Defect Details',
        editinline:{enable:true},
        selecting: true,  //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, 
        toolbar : {
      		items : [ {
      					//icon : '/css/images/excel_img1.png',
      					text : 'Export Defects',
      					click : function() 
      					{    exportDefectsPopUp();				
//       						exportDefectsBulk(workPackageId);    				
      					}
      				 }
      			 ]    			
      		},
          actions: {
       	 	listAction: 'testcase.defect.list.byWorkPackageId?workPackageId='+workPackageId,
        	// createAction: 'testcase.defect.add?tcerId='+tcerId,	
       	 	//updateAction: 'testcase.defect.update?tcerId='+tcerId 
       	 	editinlineAction: 'defects.for.analyse.update',
        },  
        fields : {
        	testExecutionResultBugId: { 
        		title: 'Bugid',
        		width: "5%",         
                list: true	,
                create:false,
                edit:false
            },
        	bugTitle: { 
        		title: 'Bug Title',  
        		width: "20%",                          
                create: true,
                list:true,
                edit:false 
        	},
        	testCaseExecutionResultId: { 
	        		title: 'id',
	        		width: "7%",         
	                list: false	,
	                create:false,
	                edit:false
	            },
        	bugDescription:{
        		title: 'Description',  
        		width: "30%",                          
	                list:true,
	                edit:false 
        	},
        	
        	testcaseId:{
        		title: 'Testcase Id',  
        		width: "7%",                          
	                list:true,
	                edit:false 
        	},
        	testcaseName:{
        		title: 'Testcase Name',  
        		width: "15%",                          
	                list:true,
	                edit:false 
        	},
//             bugFilingStatusId: { 
//        		//title: 'Filing Status',
//        		
//        		width: "7%",         
//        		 create: false,
//	                list:false,
//	                edit:false,
//	                //options : 'administration.workFlow.list?entityType=1'
//            }, 
            bugFilingStatusId: { 
        		title: 'Filing Status',
        		width: "5%",         
        		 create: true,
	                list:true,
	                edit:false ,
	                options : 'administration.workFlow.list?entityType=1'
            },
            severityId:{		        		 
        		title: 'Severity',
        		width: "7%",         
        		 create: true,
	                list:true,
	                edit:false,
	                options : 'common.list.defectSeverity'            
        	},
            remarks: { 
        		title: 'Remarks',
        		width: "7%",         
        		 create: true,
	                list:true,
	                edit:false 						                
            },
            userId:{
            	title: 'Raised By', 
        		width: "10%",                         
        		 create: true,
	                list:true,
	                edit:false
          
            },
           bugCreationTime: { 
        		title: 'Creation Time',
        		width: "7%",         
        		 create: false,
	                list:true,
	                edit:false							                
            } ,
            bugManagementSystemName: { 
        		title: 'System Name', 
        		width: "20%",                         
        		 create: false,
	                list:false,
	                edit:false 
           },
           bugManagementSystemBugId: { 
        		title: 'System Bug Id',  
        		width: "25%",                        
        		 create: false,
	                list:false,
	                edit:false 
            },
            fileBugInBugManagementSystem: { 
        		title: 'File Bug',  
        		width: "7%",         
        		 create: false,
	                list:false,
	                edit:false 
            },

            exportDefects: { 
              	title: 'ExportDefect',
              	width: "5%",
              	edit: false,
              	list:true,
              	display:function (data) { 
	        		
	           		//Create an image that will be used to open child table 
           			var $img = $('<img src="css/images/dt_export.png" title="ExportDefect" style="width:16px;height:16px;" />'); 
           			//Open child table when user clicks the image 
           			$img.click(function () {
           				exportOneDefectsPopUp(data.record.testExecutionResultBugId);
           				//exportOneDefect(data.record.testExecutionResultBugId);
           		  });
           			return $img;
	        	}
              },
              exportDefects1: { 
              	title: '',
              	width: "15%",
              	edit: false,
              	list:true,
              	display:function (defData) { 
           			var $img = $('<img src="css/images/list_metro.png" title="DefectSystem" class="showHandCursor" style="width:16px;height:16px;" />'); 
           			//Open child table when user clicks the image 
           			$img.click(function (data) { 
           				
           				// ----- Closing child table on the same icon click -----
        				closeChildTableFlag = closeJtableTableChildContainer($(this), $("#jTableContainerDefects"));
        				if(closeChildTableFlag){
        					return;
        				}
           				
			        	$('#jTableContainerDefects').jtable('openChildTable', 
					        	$img.closest('tr'), 
					        	{ 
					        	title: 'Defect System Details',
					        	paging: true, //Enable paging
					            pageSize: 10, //Set page size (default: 10)
					            selecting: true, //Enable selecting 
					            editinline:{enable:false},					        	  	
					        	actions: {
					        		listAction: 'defect.system.name.code.list?testExecutionResultsBugId='+defData.record.testExecutionResultBugId,
									recordUpdated:function(event, data){
					        	        	$('#jTableContainerDefects').find('.jtable-main-container').jtable('reload');
					        	        },
					        	     recordAdded: function (event, data){
					        	         	$('#jTableContainerDefects').find('.jtable-main-container').jtable('reload');
					        	        },
					            },
					        	fields: {		      
						   	            defectSystemName: {
							                 title: 'Defect System Name ',
							                 inputTitle: 'Defect System Name <font color="#efd125" size="4px">*</font>',
							                 width: "12%",
							                 create : true,
							                 edit :true ,
							                 //display: function (data) { 
							     			 //return data.record.defectSystemName;
							                 //}, 
							         	},
							         	defectSystemCode: {
							                 title: 'Defect System Code ',
							                 inputTitle: 'Defect System Code <font color="#efd125" size="4px">*</font>',
							                 width: "12%",
							                 create : true,
							                 edit :true ,
							                 //display: function (data) { 
							     			 //return data.record.defectSystemCode;
							                 //}, 
								             display: function (data) { 
								            	 if(data.record.defectSystemURI != null){
								            		 return $('<a style="color: #0000FF;" href='+data.record.defectSystemURI+' target="_blank">'+data.record.defectSystemCode+'</a>');
								            	 }else{
								            		 return data.record.defectSystemCode;
								            	 }											 	
								             }
							         	},	

//							            testRunPlanName: {
//							                title: 'Name',
//							                list:true,
//							                display: function (data) { 
//												hdnProductId = data.record.productId;
//												hdnProductName = data.record.productName;
//												hdnProductVersionId = data.record.productVersionId;
//							     				return $('<a style="color: #0000FF;" href=javascript:callConfirm('+data.record.testRunPlanId+','+data.record.executionTypeId+');>'+data.record.testRunPlanName+'</a>');
//							               }
//							            },
							         	defectExportDate: {
							                 title: 'Defect Export Date ',
							                 inputTitle: 'Defect Export Date <font color="#efd125" size="4px">*</font>',
							                 width: "12%",
							                 create : true,
							                 edit :true ,
							                 //type : 'date'
						                	 //display: function (data) { 
							     			 //return data.record.defectSystemCode;
							                 //}, 
							         	},
								    	},
						    	 // This is for closing $img.click(function (data) { 							
							}, 
		        			function (data) { //opened handler 
				        	data.childTable.jtable('load'); 
				        	}); 
			        	});  
           			return  $img;			
	        	}
            
              },
              
            defectTypeId: { 
        		title: 'Defect Type',  
        		width: "7%",  
        		edit:false,
	            options:'common.list.defect.types.list'
            },
            defectIdentifiedInStageId: { 
        		title: 'Found In',  
        		width: "7%",  
        		edit:false,
	            options:'common.list.defect.identification.stages.list'
            },
            reportedInBuildId: { 
        		title: 'Reported Build',  
        		width: "15%",  
        		edit:false,
        		list:true,
        		options:'process.list.builds.by.product?productId='+prodId
            },
            
            fixedInBuildId: { 
        		title: 'Fixed Build',  
        		width: "15%",  
        		edit:true,
        		list:true,
        		  options:'process.list.builds.by.product?productId='+prodId
            },
            sourceFilesModifiedForFixing: { 
        		title: 'Source File Modified',  
        		width: "10%",  
        		edit:true,
        		list:true,
            },
            
            
            

        },  // This is for closing $img.click(function (data) {  
	      
		});
	 $('#jTableContainerDefects').jtable('load');*/
}

//BEGIN: ConvertDataTable - DefectsInWorkpackage
function loadDefectInfoWorkpackageDataTable(workPackageId){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [{id:"entityTypeList", url:'administration.workFlow.list?entityType=1'},
	              {id:"defectSeverityList", url:'common.list.defectSeverity'},
	              {id:"stagesList", url:'common.list.defect.identification.stages.list'},
	              {id:"buildList", url:'process.list.builds.by.product?productId='+prodId}];
	defectInfoWorkpackageOptions_Container(optionsArr);
}

function defectInfoWorkpackageOptions_Container(urlArr){
	$.ajax( {
 	   "type": "POST",
        "url":  urlArr[optionsItemCounter].url,
        "dataType": "json",
         success: function (json) {		        	 
	         if(json.Result == "OK"){						 
				 if(json.Options.length>0){     		   
					   for(var i=0;i<json.Options.length;i++){
						   json.Options[i].label=json.Options[i].DisplayText;
						   json.Options[i].value=json.Options[i].Value;
					   }			   
				   }else{
					  json.Options=[];
				   }     	   
				   optionsResultArr.push(json.Options);						 
	         }
	         optionsItemCounter++;	
			 if(optionsItemCounter<optionsArr.length){
				 defectInfoWorkpackageOptions_Container(optionsArr);
			 }else{
				 defectInfoWorkpackageDataTableInit();
			 }					 
         },
         error: function (data) {
			optionsItemCounter++;
         },
         complete: function(data){
         	//console.log('Completed');
         },	            
   	});
}

function defectInfoWorkpackageDataTableInit(){
	var url,jsonObj={};
	url= 'testcase.defect.list.byWorkPackageId?workPackageId='+workPackageId+'&jtStartIndex=0&jtPageSize=10000';
	jsonObj={"Title":"Defect Info",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"Defect Info",
	};
	defectInfoWorkpackageDataTableContainer.init(jsonObj);
}

var defectInfoWorkpackageDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		assignDefectInfoWorkpackageDataTableValues(jsonObj,"ParentTable");
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignDefectInfoWorkpackageDataTableValues(jsonObj,tableType){
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
			if(tableType == "ParentTable"){
				defectInfoWorkpackageDT_Container(jsonObj);
			}else if(tableType == "ChildTable"){
				defectSystemWorkpackageDT_Container(jsonObj);
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

function defectInfoWorkpackageDataTableHeader(){
	var childDivString ='<table id="defectInfoWorkpackage_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Bug Id</th>'+
			'<th>Bug Title</th>'+
			'<th>Description</th>'+
			'<th>Testcase Id</th>'+
			'<th>Testcase Name</th>'+
			'<th>Environment Combination</th>'+
			'<th>Filing Status</th>'+
			'<th>Severity</th>'+
			'<th>Remarks</th>'+
			//'<th>Raised By</th>'+
			'<th>Creation Time</th>'+
			/*'<th>Defect Type</th>'+
			'<th>Found In</th>'+
			'<th>Reported Build</th>'+
			'<th>Fixed Build</th>'+ 
			'<th>Source File Modified</th>'+*/
			'<th>Export Defect</th>'+
			'<th>Defect System</th>'+
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
			/*'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+*/
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
var editorDefectInfoWorkpackage='';
function defectInfoWorkpackageDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForDefectDetailsInWorkpackage").children().length>0) {
			$("#dataTableContainerForDefectDetailsInWorkpackage").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = defectInfoWorkpackageDataTableHeader(); 			 
	$("#dataTableContainerForDefectDetailsInWorkpackage").append(childDivString);
	
	editorDefectInfoWorkpackage = new $.fn.dataTable.Editor( {
	    "table": "#defectInfoWorkpackage_dataTable",
		ajax: "",
		ajaxUrl: "defects.for.analyse.update",
		idSrc:  "testExecutionResultBugId",
		i18n: {
	        create: {
	            title:  "Create a new Defects",
	            submit: "Create",
	        }
	    },
		fields: [
			{
			    label: "bugFilingStatusId",
			    name: "bugFilingStatusId",
			    options: optionsResultArr[0],
			    "type":"select"
			},{
			    label: "severityId",
			    name: "severityId",
			    options: optionsResultArr[1],
			    "type":"select"
			},{
			    label: "defectIdentifiedInStageId",
			    name: "defectIdentifiedInStageId",
			    options: optionsResultArr[2],
			    "type":"select"
			},{
			    label: "reportedInBuildId",
			    name: "reportedInBuildId",
			    options: optionsResultArr[3],
			    "type":"select"
			},{
			    label: "fixedInBuildId",
			    name: "fixedInBuildId",
			    options: optionsResultArr[3],
			    "type":"select"
			},
		]
	});
	
	defectInfoDT_oTable = $("#defectInfoWorkpackage_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "90%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [10,11]; // search column TextBox Invisible Column position
	     		  $('#defectInfoWorkpackage_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeDefectInfoDT();
			   },  
		   buttons: [
						//{ extend: "create", editor: editorDefectInfoWorkpackage },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Defects',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Defects',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		'colvis',
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "testExecutionResultBugId", className: 'disableEditInline', sWidth: '7%' },	
           { mData: "bugTitle", className: 'disableEditInline', sWidth: '8%' },	
           { mData: "bugDescription", className: 'disableEditInline', sWidth: '20%' },
           { mData: "testcaseId", className: 'disableEditInline', sWidth: '20%' },
           { mData: "testcaseName", className: 'disableEditInline', sWidth: '20%' },
           { mData: "runConfiguration", className: 'disableEditInline', sWidth: '20%' },
           { mData: "bugFilingStatusId", className: 'disableEditInline', sWidth: '10%', editField: "bugFilingStatusId",
		       	mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorDefectInfoWorkpackage, 'bugFilingStatusId', full.bugFilingStatusId); 		           	 
		             return data;
	            },
           },
           { mData: "severityId", className: 'disableEditInline', sWidth: '10%', editField: "severityId",
		       	mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorDefectInfoWorkpackage, 'severityId', full.severityId); 		           	 
		             return data;
	            },
           },
           { mData: "remarks", className: 'disableEditInline', sWidth: '7%' },
           //{ mData: "userId", className: 'disableEditInline', sWidth: '7%' },
           { mData: "bugCreationTime", className: 'disableEditInline', sWidth: '7%' },
          /* { mData: "defectTypeId", className: 'disableEditInline', sWidth: '15%' },
           { mData: "defectIdentifiedInStageId", className: 'disableEditInline', sWidth: '10%', editField: "defectIdentifiedInStageId",
		       	mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorDefectInfoWorkpackage, 'defectIdentifiedInStageId', full.defectIdentifiedInStageId); 		           	 
		             return data;
		       	},
           },
           { mData: "reportedInBuildId", className: 'disableEditInline', sWidth: '10%', editField: "reportedInBuildId",
		       	mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorDefectInfoWorkpackage, 'reportedInBuildId', full.reportedInBuildId); 		           	 
		             return data;
	            },
           },
           { mData: "fixedInBuildId", className: 'editable', sWidth: '10%', editField: "fixedInBuildId",
		       	mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorDefectInfoWorkpackage, 'fixedInBuildId', full.fixedInBuildId); 		           	 
		             return data;
	            },
           },
           { mData: "sourceFilesModifiedForFixing", className: 'editable', sWidth: '7%' },*/
           { mData: null,				 
        	   bSortable: false,
           		mRender: function(data, type, full) {				            	
           			var img = ('<div style="display: flex;">'+
	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
	       				'<img src="css/images/dt_export.png" class="exportDefectImg" title="Export Defect" style="width:16px;height:16px;" />'+
    	       		'</div>');	      		
           			return img;
           		}
           },
           { mData: null,				 
        	   bSortable: false,
           		mRender: function(data, type, full) {				            	
           			var img = ('<div style="display: flex;">'+
	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
	       				'<img src="css/images/list_metro.png" class="defectSystemImg" title="Defect System" />'+
    	       		'</div>');	      		
           			return img;
           		}
           },	
       ],       
       rowCallback: function ( row, data ) {
    	  // $('input.editorDefectInfoWorkpackage-active', row).prop( 'checked', data.readyState == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#defectInfoWorkpackage_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorDefectInfoWorkpackage.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#defectInfoWorkpackage_dataTable tbody').on('click', 'td .exportDefectImg', function () {
		var tr = $(this).closest('tr');
		var row = defectInfoDT_oTable.DataTable().row(tr);
		exportOneDefectsPopUp(row.data().testExecutionResultBugId);
	});

	$('#defectInfoWorkpackage_dataTable tbody').on('click', 'td .defectSystemImg', function () {
		var tr = $(this).closest('tr');
		var row = defectInfoDT_oTable.DataTable().row(tr);
		var jsonObj={"Title":"Defect System",
				"url": 'defect.system.name.code.list?testExecutionResultsBugId='+row.data().testExecutionResultBugId+'&jtStartIndex=0&jtPageSize=10000',	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Defect System",
		};
		assignDefectInfoWorkpackageDataTableValues(jsonObj,"ChildTable");
		$('#wpDefectSystemContainer').modal();
	});

	$("#defectInfoWorkpackage_dataTable_length").css('margin-top','8px');
	$("#defectInfoWorkpackage_dataTable_length").css('padding-left','35px');		
	
	defectInfoDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutDefectInfoDT='';
function reInitializeDefectInfoDT(){
	clearTimeoutDefectInfoDT = setTimeout(function(){				
		defectInfoDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDefectInfoDT);
	},200);
}
//END: ConvertDataTable - DefectsInWorkpackage

/*
function loadDefectInfo(workPackageId, nodeType){	
	 if(nodeType !='WorkPackage'){
	 	setWorkPackageNode();
	 }
	$('#Defects').children().show();
	
	document.getElementById("ResultsGridView").style.display = "none";
	
	var url ='testcase.defect.list.byWorkPackageId?workPackageId='+workPackageId+'&jtStartIndex=0&jtPageSize=10';
	assignDefectsDataTableValues(url, "parentTable", "", "");

}

function assignDefectsDataTableValues(url,tableValue, row, tr){
	
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {					
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			
			if(tableValue == "parentTable"){
				workPackageDefects_Container(data);
			}else if(tableValue == "childTable1"){
				//tsResults_Container(data, row, tr);
			}else{
				console.log("no child");
			}
			
		  },
		  error : function(data) {
			console.log("error");
		 }
	});	
}

var defectsResults_oTable='';
//var tsResults_oTable='';
var defects_FieldsArr = [];

function workPackageDefects_Container(data){
	defects_FieldsArr = [];
	try{
		if ($('#defects_dataTable').length>0) {
			$('#defects_dataTable').dataTable().fnDestroy(); 
		}
	} catch(e) {}
	
	 defectsResults_oTable = $('#defects_dataTable').dataTable( {
		dom: "Bfrtip",
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"sScrollX": "100%",
       //"sScrollXInner": "100%",       
       "scrollY":"100%",
       "bScrollCollapse": true,
		select: {
           style:    'os',
           selector: 'td:first-child'
       },
       //fixedColumns: {
       //    leftColumns: 1,
       //},
		buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    'excel',
	                    'csv',
	                    'pdf',
	                ]
	            }
         ],				        
         aaData:data,		    				 
	    aoColumns: [	        	        	        
           { mData: "testExecutionResultBugId" },		    				            
           { mData: "bugTitle" },		
           { mData: "bugDescription" },		
           { mData: "testcaseId" },		
		   { mData: "testcaseName" },		    				            
           { mData: "bugFilingStatusId" },		
           { mData: "severityId" },		
           { mData: "remarks" },
           { mData: "userId" },		
           { mData: "bugCreationTime" },	
           { 
					mData:"",
					"className":      'details-control',
					"orderable":      false,
					"data":           data,
					"defaultContent": ''	
		   },
           {       	
	        	
	        	     mData:"",
					"className":      'details-control',
					"orderable":      false,
					"data":           data,
					"defaultContent": ''				      	
	        	
	        }, 
           { mData: "defectTypeId" },
           { mData: "defectIdentifiedInStageId" },                               	    				           
       ],
       "oLanguage": {
           "sSearch": "Search all columns:"
       },
	});
	 
	//-----------------
	
	 $(function(){ // this will be called when the DOM is ready 
	    $("#Defects tfoot input").each( function (i) {
	        defects_FieldsArr[i] = this.name;
	    } );  
	    
	    $("#Defects tfoot input").keyup( function (event) {
	    	tcResults_oTable.fnFilter( this.value, returnDefects_IndexElement(this.name));
	    });
	    
	    $("#Defects tfoot input").focus( function (event) {
	        if ( this.className == "search_init" )
	        {
	            this.className = "";
	            this.value = "";
	        }
	    } );
	     
	    $("#Defects tfoot input").blur( function (i) {
	        if ( this.value == "" )
	        {
	            this.className = "search_init";
	            //this.value = defects_FieldsArr[returnIndexElement(this.name)];
	            this.value = "";
	        }
		} );
	    
	    // ------ child table -----
	    
	    $('#defects_dataTable tbody').on('click', 'td.details-control', function () {
	    	// -----	 
	    	var tr = $(this).closest('tr');
	    	var row = tcResults_oTable.DataTable().row(tr);

	    	if ( row.child.isShown() ) {
	    		// This row is already open - close it
	    		row.child.hide();
	    		tr.removeClass('shown');
	    	}
	    	else {
	    		//tcResultHandler(row, tr);
	    		
	    	}
	    	//-----
   		});
	    
	} ); 
}

function returnDefects_IndexElement(elementName){
	var elementLength = $("#Defects tfoot input").length;
	var indexValue=0;
	
	for(var i=0;i<elementLength;i++){
		if($("#Defects tfoot input").eq(i).attr('name') == elementName){
			indexValue = i;
			break;
		}			
	}
	return indexValue;
	
}


function tcResultHandler(row, tr){
	//1var url ='teststepsexecution.of.testcaseexecutionId?wptcepId=5&jtStartIndex=0&jtPageSize=25';
	var url ='teststepsexecution.of.testcaseexecutionId?wptcepId='+row.data().testCaseExecutionResultId+'&jtStartIndex=0&jtPageSize=10';
	assignDataTableValues(url, "childTable1", row, tr);
}

// ------ test step dataTable -----

function tsChild1Table(){
		var childDivString = '<table id="tsResults_dataTable"  class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead>'+
			'<tr>'+
				'<th>Test Step Id</th>'+
				'<th>TestStep Exection Id</th>'+
				'<th>Test Step Name</th>'+
				'<th>TestCase Code</th>'+
				'<th>Description</th>'+
				'<th>Expected Output</th>'+
				'<th>Observed Output</th>'+
				'<th>Status</th>'+
				'<th>Start Time</th>'+		
				'<th>End Time</th>'+
				'<th>Remarks</th>'+	      	
			'</tr>'+
		'</thead>'+
		'</table>';		
		
		return childDivString;	
}

function tsResults_Container(data, row, tr){
	var childDivString = tsChild1Table();
	var childDiv = $(childDivString);
	row.child(childDiv).show();
	       
	try{
		if ($(childDiv).length>1) {
			$(childDiv).dataTable().fnDestroy(); 
		}
	} catch(e) {}
	
	 tsResults_oTable = $(childDiv).dataTable( {
		dom: "Bfrtip",
		paging: true,	    			      				
		destroy: true,
		searching: false,
		bJQueryUI: false,
		"sScrollX": "90%",
        "sScrollXInner": "100%",
        //"scrollY":"230px",
        "scrollY":"100%",
        "bScrollCollapse": true,
        buttons: [],       				        
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "testStepId" },		
           { mData: "teststepexecutionresultId" },		
           { mData: "testStepName" },		
           { mData: "testStepCode" },		
           { mData: "testStepDescription" },	
           { mData: "testStepExpectedOutput" },
           { mData: "testStepObservedOutput" },
           { mData: "testResultStatus" },
           { mData: "startTime" },
           { mData: "endTime" },
           { mData: "executionRemarks" },
       ],      
	});	
	 
	// Open this row	    		
	//row.child($(tsResults_oTable)).show();	    		
	//tr.addClass('shown');	 
}
*/

function defectSystemWorkpackageDataTableHeader(){
	var childDivString ='<table id="defectSystemWorkpackage_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Defect System Code</th>'+
			'<th>Defect System Name</th>'+
			'<th>Defect Export Date</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
var editorDefectSystemWorkpackage='';
function defectSystemWorkpackageDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForDefectSystemInWorkpackage").children().length>0) {
			$("#dataTableContainerForDefectSystemInWorkpackage").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = defectSystemWorkpackageDataTableHeader(); 			 
	$("#dataTableContainerForDefectSystemInWorkpackage").append(childDivString);
	
	editorDefectSystemWorkpackage = new $.fn.dataTable.Editor( {
	    "table": "#defectSystemWorkpackage_dataTable",
		ajax: "",
		ajaxUrl: "",
		idSrc:  "defectSystemCode",
		i18n: {
	        create: {
	            title:  "Create a new Defects",
	            submit: "Create",
	        }
	    },
		fields: []
	});
	
	defectSystemDT_oTable = $("#defectSystemWorkpackage_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "90%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
	     		  $('#defectSystemWorkpackage_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeDefectSystemDT();
			   },  
		   buttons: [
						//{ extend: "create", editor: editorDefectSystemWorkpackage },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Defect System',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Defect System',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		'colvis',
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
          /* { mData: "defectSystemCode", className: 'disableEditInline', sWidth: '30%' },	*/
           { mData: "defectSystemCode", sWidth: '10%', "render": function (tcData,type,full) {	        
		 		if(full.defectSystemCode != null && full.defectSystemCode != "") {
					return ('<a href="'+full.defectSystemURI+'" target="_blank">'+full.defectSystemCode+'</a>');		        
				} else {
					return "";
				}					
		 		
	    	},
	    },
	    { mData: "defectSystemName", className: 'disableEditInline', sWidth: '40%' },	
           { mData: "defectExportDate", className: 'disableEditInline', sWidth: '30%' },
       ],       
       rowCallback: function ( row, data ) {
    	  // $('input.editorDefectSystemWorkpackage-active', row).prop( 'checked', data.readyState == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#defectSystemWorkpackage_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorDefectSystemWorkpackage.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#defectSystemWorkpackage_dataTable_length").css('margin-top','8px');
	$("#defectSystemWorkpackage_dataTable_length").css('padding-left','35px');		
	
	defectSystemDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutDefectSystemDT='';
function reInitializeDefectSystemDT(){
	clearTimeoutDefectSystemDT = setTimeout(function(){				
		defectSystemDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDefectSystemDT);
	},200);
}
//END: ConvertDataTable - DefectsInSystem