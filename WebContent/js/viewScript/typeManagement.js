var key ='';
var nodeType='';

var testFactoryId = 0;
var productId = 0;

jQuery(document).ready(function() {	
   QuickSidebar.init(); // init quick sidebar
   ComponentsPickers.init();
   setBreadCrumb("Type Management");
   createHiddenFieldsForTree();
   setPageTitle("Type Management");
   getTreeData("administration.productWithTF.tree");
   setDefaultnode("j1_1");
   
   $("#treeContainerDiv").on("select_node.jstree",
		     function(evt, data){
				 if(data.node != undefined){
					var entityIdAndType =  data.node.data;
					var arry = entityIdAndType.split("~");
					key = arry[0];
					var type = arry[1];
					nodeType = type;
					var loMainSelected = data;
					uiGetParents(loMainSelected);
					
					if(nodeType == "TestFactory"){
						testFactoryId = key;
						productId = 0;
						//setDefaultnode(data.node.id);
					}
					if(nodeType == "Product"){
						productId = key;
						testFactoryId = data.node.original.parent;
					}	 
					$('#tabslist li').first().find("a").trigger("click");
				}
	     	}
		);
  // loadExecutionTypes();
   
	   $( document ).tooltip({
			 track: true,
		      position: {
		        my: "center bottom-20",
		        at: "center top",
		        using: function( position, feedback ) {
		          $( this ).css( position );
		          $( "<div>" )
		            .addClass( "arrow" )
		            .addClass( feedback.vertical )
		            .addClass( feedback.horizontal )
		            .appendTo( this );
		        }
		      }
		    });
	    
}); 

$(document).on('click', '#tabslist>li', function(){
	var selectedTab = $(this).index();
	tabSelection(selectedTab);
});

function tabSelection(selectedTab){
	if (selectedTab == 0){
		//addOrEditActivityType();
		activityTypeDataTable();
	}else if (selectedTab == 1){
		//addOrEditTaskType();
		activityTaskDataTable();
	}else if (selectedTab == 2){
		//addOrEditUserGroups();
		activityUserGroupDataTable();
	}
}

/*function addOrEditActivityType(){
	
	try{
		if ($('#jTableActivityTypes').length>0) {
			 $('#jTableActivityTypes').jtable('destroy'); 
		}
	}catch(e){}
	 $('#jTableActivityTypes').jtable({
         title: 'Activity Types',
         selecting: true, //Enable selecting 
         paging: true, //Enable paging
         pageSize: 10, //Set page size (default: 10)
         recordsLoaded: function(event, data) {
 			
         },
         editinline:{enable:true},	 
         actions: {
             //listAction: 'product.specific.activity.type.listing?productId='+productId,
        	 listAction: 'activity.type.listing?testFactoryId='+testFactoryId+'&productId='+productId,
             createAction: 'activity.type.add',
             editinlineAction: 'activity.type.update',
         },
         fields: {
        	 activityMasterId: { 
   				key: true,
   				type: 'hidden',
   				create: false, 
   				edit: false, 
   				list: false,
   			 	// display: function (data) { return data.record.environmentGroupId;}, 
   			},
   		 	activityMasterName: { 
   		 		title:'Activity Type',
   		 	    inputTitle: 'Activity Type <font color="#efd125" size="4px">*</font>',
   		 		width: "20%",
   				create: true, 
   				edit: true, 
   				list: true,
   			},
   			testFactoryId:{
   				defaultValue : testFactoryId,
   				type: 'hidden',
   			},
   			productId:{
   				defaultValue : productId,
   				type: 'hidden',
   			},
   			description: { 
       			title: 'Description',
       		    type: 'textarea',   
     		  	width: "30%",  
     	  		list:true,
	     	  	create:true
    	   }, 
   			activityGroupId:{
	  			title: 'Activity Group',
     	  		inputTitle: 'Activity Group <font color="#efd125" size="4px">*</font>',
     	  		width: "20%",
     	  		list:true,
     	  		create:true,
     	  		edit:false,
     	  	 	options: 'activity.group.option.list'
	  		},
	  		activityTypeId:{
	  			title: 'Activity Category',
     	  		inputTitle: 'Activity Category <font color="#efd125" size="4px">*</font>',
     	  		width: "20%",
     	  		list:true, 
     	  		create:true,
     	  		edit:false,
				dependsOn:'activityGroupId',
				options:function(data){
					if(data.dependedValues.activityGroupId != null){
						return 'activity.type.master.option.list?activityGroupId='+data.dependedValues.activityGroupId;
					}
	     		}
	  		},
	  		weightage :{		  			
	  			title: 'Weightage',
	  			defaultValue: 1
	  			
	  		},
			commentsActTypes:{
				title : '',
				list : true,
				create : false,
				edit : false,
				width: "5%",
				display:function (data) { 
					//Create an image for test script popup 
					var $img = $('<i class="fa fa-comments" title="Comments"></i>');
						$img.click(function () {
							var entityTypeIdComments = 33;
							var entityNameComments = "ActivityType";
							listComments(entityTypeIdComments, entityNameComments, data.record.activityMasterId, data.record.activityMasterName, "actTypesComments");
						});
					return $img;
					}		
			},
  		  
    	
		   /* status: {	      
		       	title: 'Status' ,
	        	width: "5%",  
	        	list:true,
	        	edit:true,
	        	create:false,
	        	type : 'checkbox',
	        	values: {'0' : 'No','1' : 'Yes'},
		   		defaultValue: '1'
         	},	  
        		          
         },
         formSubmitting: function (event, data) {       
            	/* data.form.find('input[name="environmentGroupName"]').addClass('validate[required, custom[minSize[3]]'); 
            	data.form.find('input[name="displayName"]').addClass('validate[required, custom[minSize[3]]');
            //data.form.find('input[name="status"]').addClass('validate[required]');
              data.form.validationEngine();
              return data.form.validationEngine('validate'); 
            }, 
             //Dispose validation logic when form is closed
              formClosed: function (event, data) {
                data.form.validationEngine('hide');
                data.form.validationEngine('detach');
            }, 
     });		 
	 $('#jTableActivityTypes').jtable('load');
}*/

//BEGIN: ConvertDataTable - ActivityType
var activityTypeDT_oTable='';
var editorActivityType='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;

function activityTypeDataTable(){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [{id:"activityGroupList", url:'activity.group.option.list'},
	              {id:"activityCategoryList", url:'activity.type.master.option.list?activityGroupId=1'},
	              ];
	activityTypeTypeOptions_Container(optionsArr);
}
		
function activityTypeTypeOptions_Container(urlArr){
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
				 activityTypeTypeOptions_Container(optionsArr);
			 }else{
				activityTypeDataTableInit();	
			 }					 
         },
         error: function (data) {
         	console.log('error in ajax call : '+url);
			 optionsItemCounter++;
         },
         complete: function(data){
         	//console.log('Completed');
         },	            
   	});
}

function activityTypeDataTableInit(){
	 var url= 'activity.type.listing?testFactoryId='+testFactoryId+'&productId='+productId+'&jtStartIndex=0&jtPageSize=10000';
	 var jsonObj={"Title":"Activity Type",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"activityType",
	};
	activityTypeDataTableContainer.init(jsonObj);
}

var activityTypeDataTableContainer = function() {
 	var initialise = function(jsonObj){
		assignActivityTypeDataTableValues(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignActivityTypeDataTableValues(jsonObj){
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
			activityTypeDT_Container(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function activityTypeDataTableHeader(){
	var childDivString ='<table id="activityType_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Activity Type</th>'+
			'<th>Description</th>'+
			'<th>Activity Group</th>'+
			'<th>Activity Category</th>'+
			'<th>Weightage</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function activityTypeDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForActivityType").children().length>0) {
			$("#dataTableContainerForActivityType").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = activityTypeDataTableHeader(); 			 
	$("#dataTableContainerForActivityType").append(childDivString);
	
	editorActivityType = new $.fn.dataTable.Editor( {
	    "table": "#activityType_dataTable",
		ajax: "activity.type.add",
		ajaxUrl: "activity.type.update",
		idSrc:  "activityMasterId",
		i18n: {
	        create: {
	            title:  "Create a Activity Type",
	            submit: "Create",
	        }
	    },
		fields: [
		 {
            label: "activityMasterId",
            name: "activityMasterId",
            "type": "hidden"
         },{
            label: "testFactoryId",
            name: "testFactoryId",
            "type": "hidden",
			"def": testFactoryId
         },{
            label: "productId",
            name: "productId",
            "type": "hidden",
			"def": productId
         },{
            label: "Activity Master Name",
            name: "activityMasterName",
         },{
             label: "Description",
             name: "description",
         },{
            label: "Activity Group Id",
            name: "activityGroupId",
            options: optionsResultArr[0],
            "type":"select"
         },{
             label: "Activity Type Id",
             name: "activityTypeId",
             options: optionsResultArr[1],
             "type":"select"
        },{
            label: "weightage",
            name: "weightage",            
			"def": 1
         }     
    ]
	});
	
	activityTypeDT_oTable = $("#activityType_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [5]; // search column TextBox Invisible Column position
	     		  $('#activityType_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeActivityTypeDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorActivityType },								 
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Activity Type',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Activity Type',
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
           	{ mData: "activityMasterName", className: 'editable', sWidth: '15%' },
           	{ mData: "description", className: 'editable', sWidth: '15%' },
           	{ mData: "activityGroupId",className: 'disableEditInline',
              	mRender: function (data, type, full) {
	 				data = optionsValueHandler(editorActivityType, 'activityGroupId', full.activityGroupId);
                    return data;
                },
        	},
           	{ mData: "activityTypeId",className: 'disableEditInline',
               	mRender: function (data, type, full) {
	 				data = optionsValueHandler(editorActivityType, 'activityTypeId', full.activityTypeId);
                    return data;
                },
           	},
            { mData: "weightage", className: 'editable', sWidth: '15%' },
            { mData: null,className: 'disableEditInline',				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<i class="fa fa-comments commentsImg" title="Comments"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
       ],       
       rowCallback: function ( row, data ) {
    	  // $('input.editorActivityType-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#activityType_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorActivityType.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#activityType_dataTable tbody').on('click', 'td .commentsImg', function () {
		var tr = $(this).closest('tr');
    	var row = activityTypeDT_oTable.DataTable().row(tr);
		var entityTypeIdComments = 65;
		var entityNameComments = "ActivityTypeSchedule";
		listComments(entityTypeIdComments, entityNameComments, row.data().activityMasterId, row.data().extractorName, "extractorComments");
	});
	
	$("#activityType_dataTable_length").css('margin-top','8px');
	$("#activityType_dataTable_length").css('padding-left','35px');		

	activityTypeDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutActivityTypeDT='';
function reInitializeActivityTypeDT(){
	clearTimeoutActivityTypeDT = setTimeout(function(){				
		activityTypeDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutActivityTypeDT);
	},200);
}

function fullScreenHandlerDTActivityType(){
	
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
		$('#testFacMode').css('max-height',displaytestFaceModeResponsive(window.innerWidth));
		
		activityTypeDTFullScreenHandler(true);
		if($($('#tabslist li')[0]).hasClass('active')){
			reInitializeActivityTypeDT();
		}else if($($('#tabslist li')[1]).hasClass('active')){
			reInitializeActivityTaskDT();
		}else if($($('#tabslist li')[2]).hasClass('active')){
			reInitializeActivityUserGroupDT();
		}
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
		$('#testFacMode').css('max-height','');
		
		reInitializeActivityTypeDT();				
		activityTypeDTFullScreenHandler(false);			
		if($($('#tabslist li')[0]).hasClass('active')){
			reInitializeActivityTypeDT();
		}else if($($('#tabslist li')[1]).hasClass('active')){
			reInitializeActivityTaskDT();
		}else if($($('#tabslist li')[2]).hasClass('active')){
			reInitializeActivityUserGroupDT();
		}
	}
}

function activityTypeDTFullScreenHandler(flag){
	if(flag){
		if($($('#tabslist li')[0]).hasClass('active')){
			reInitializeActivityTypeDT();
			$("#activityType_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
		}else if($($('#tabslist li')[1]).hasClass('active')){
			reInitializeActivityTaskDT();
			$("#activityTask_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
		}else if($($('#tabslist li')[2]).hasClass('active')){
			reInitializeActivityUserGroupDT();
			$("#activityUserGroup_dataTable .dataTables_scrollBody").css('max-height','240px');
		}
	}else{
		if($($('#tabslist li')[0]).hasClass('active')){
			reInitializeActivityTypeDT();
			$("#activityType_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
		}else if($($('#tabslist li')[1]).hasClass('active')){
			reInitializeActivityTaskDT();
			$("#activityTask_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
		}else if($($('#tabslist li')[2]).hasClass('active')){
			reInitializeActivityUserGroupDT();
			$("#activityUserGroup_dataTable .dataTables_scrollBody").css('max-height','240px');
		}
	}
}

function displaytestFaceModeResponsive(widthValue){
	var resultWidth="";
	if(widthValue<768){
		resultWidth = 200;			
	}else if(widthValue<992){
		resultWidth = 300;
	}else if(widthValue<1200){
		resultWidth = 400;
	}else if(widthValue<1500){
		resultWidth = 500;			
	}else if(widthValue<1600){
		resultWidth = 600;
	}else if(widthValue<1800){
		resultWidth = 700;
	}else if(widthValue<2050){
		resultWidth = 750;
	}else if(widthValue<2400){
		resultWidth = 850;
	}else if(widthValue<3000){
		resultWidth = 1100;
	}else if(widthValue<4000){
		resultWidth = 1300;
	}else if(widthValue<5000){
		resultWidth = 1500;
	}
	
	return resultWidth+'px';
}
//END: ConvertDataTable - ActivityType

/*function addOrEditTaskType(){
	try{
		if ($('#jTableTaskTypes').length>0) {
			 $('#jTableTaskTypes').jtable('destroy'); 
		}
	}catch(e){}
	$('#jTableTaskTypes').jtable({
		title: 'Task Types',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
        },
		actions: { 
			listAction: 'activity.task.type.list?testFactoryId='+testFactoryId+'&productId='+productId,
            createAction: 'activity.task.type.add?testFactoryId='+testFactoryId+'&productId='+productId,
            editinlineAction: 'activity.task.type.update',
		}, 
		
		fields: {
			activityTaskTypeId: { 
				type: 'hidden', 
				edit: false,
			},
			productId:{
				list:false,
				create:false,
				edit:false
			},
			testFactoryId:{
				list:false,
				create:false,
				edit:false
			},
			activityTaskTypeName: { 
				title: 'Task Type Name',
				inputTitle: 'Task Type Name <font color="#efd125" size="4px">*</font>',
				width: "15%",
				list: true,
				edit : true,
				create: true,
			},
			activityTaskTypeDescription: { 
				title: 'Description',
				width: "15%",
				list: true,
				edit : true,
				create: true,
			},
			activityTaskTypeWeightage: { 
				title: 'Weightage',
				width: "15%",
				list: true,
				edit : true,
				create: true,
				defaultValue: 1
			},
			commentsActTaskTypes:{
				title : '',
				list : true,
				create : false,
				edit : false,
				width: "5%",
				display:function (data) { 
					//Create an image for test script popup 
					var $img = $('<i class="fa fa-comments" title="Comments"></i>');
						$img.click(function () {
							var entityTypeIdComments = 67;
							var entityNameComments = "ActivityTaskType";
							listComments(entityTypeIdComments, entityNameComments, data.record.activityTaskTypeId, data.record.activityTaskTypeName, "actTaskTypesComments");
						});
					return $img;
					}		
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
        }
	}, function (data) { //opened handler 
		
	});
	$('#jTableTaskTypes').jtable('load');

}*/

//BEGIN: ConvertDataTable - ActivityTaskType
var activityTaskDT_oTable='';
var editorActivityTask='';

function activityTaskDataTable(){
	activityTaskDataTableInit();
}
		
function activityTaskDataTableInit(){
	 var url = 'activity.task.type.list?testFactoryId='+testFactoryId+'&productId='+productId+'&jtStartIndex=0&jtPageSize=10000';
	 var jsonObj={"Title":"Activity Task",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"activityTask",
	};
	activityTaskDataTableContainer.init(jsonObj);
}

var activityTaskDataTableContainer = function() {
 	var initialise = function(jsonObj){
		assignActivityTaskDataTableValues(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignActivityTaskDataTableValues(jsonObj){
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
			activityTaskDT_Container(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function activityTaskDataTableHeader(){
	var childDivString ='<table id="activityTask_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Task Type Name</th>'+
			'<th>Description</th>'+
			'<th>Weightage</th>'+
			'<th></th>'+
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
function activityTaskDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForActivityTask").children().length>0) {
			$("#dataTableContainerForActivityTask").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = activityTaskDataTableHeader(); 			 
	$("#dataTableContainerForActivityTask").append(childDivString);
	
	editorActivityTask = new $.fn.dataTable.Editor( {
	    "table": "#activityTask_dataTable",
		ajax: "activity.task.type.add?testFactoryId="+testFactoryId+"&productId="+productId,
		ajaxUrl: "activity.task.type.update",
		idSrc:  "activityTaskTypeId",
		i18n: {
	        create: {
	            title:  "Create a Task Type",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "activityTaskTypeId",
            name: "activityTaskTypeId",
			"type": "hidden",			
         },{
            label: "Task Type Name",
            name: "activityTaskTypeName",
         },{
             label: "Description",
             name: "activityTaskTypeDescription",
        },{
            label: "Weightage",
            name: "activityTaskTypeWeightage",
        }        
    ]
	});
	
	activityTaskDT_oTable = $("#activityTask_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [3]; // search column TextBox Invisible Column position
	     		  $('#activityTask_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeActivityTaskDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorActivityTask },								 
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Activity Type',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Activity Type',
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
           	{ mData: "activityTaskTypeName", className: 'editable', sWidth: '25%' },
           	{ mData: "activityTaskTypeDescription", className: 'editable', sWidth: '25%' },
            { mData: "activityTaskTypeWeightage", className: 'editable', sWidth: '25%' },
            { mData: null,className: 'disableEditInline',				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<i class="fa fa-comments commentsImg" title="Comments"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
       ],       
       rowCallback: function ( row, data ) {
    	  // $('input.editorActivityTask-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#activityTask_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorActivityTask.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#activityTask_dataTable tbody').on('click', 'td .commentsImg', function () {
		var tr = $(this).closest('tr');
    	var row = activityTaskDT_oTable.DataTable().row(tr);
		var entityTypeIdComments = 67;
		var entityNameComments = "ActivityTaskType";
		listComments(entityTypeIdComments, entityNameComments, row.data().activityTaskTypeId, row.data().activityTaskTypeName, "actTaskTypesComments");

	});
	
	$("#activityTask_dataTable_length").css('margin-top','8px');
	$("#activityTask_dataTable_length").css('padding-left','35px');		

	activityTaskDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutActivityTaskDT='';
function reInitializeActivityTaskDT(){
	clearTimeoutActivityTaskDT = setTimeout(function(){				
		activityTaskDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutActivityTaskDT);
	},200);
}
//END: ConvertDataTable - ActivityTaskType

/*function addOrEditUserGroups(){
	try{
		if ($('#jTableUserGroups').length>0) {
			 $('#jTableUserGroups').jtable('destroy'); 
		}
	}catch(e){}
	$('#jTableUserGroups').jtable({
		title: 'User Groups',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
        },
		actions: { 
			listAction: 'user.group.list?testFactoryId='+testFactoryId+'&productId='+productId+'&isConsolidated=false',
            createAction: 'user.group.add?testFactoryId='+testFactoryId+'&productId='+productId,
            editinlineAction: 'user.group.update',
		}, 
		
		fields: {
			userGroupId: { 
				type: 'hidden', 
				edit: false,
			},
			productId:{
				list:false,
				create:false,
				edit:false
			},
			testFactoryId:{
				list:false,
				create:false,
				edit:false
			},
			userGroupName: { 
				title: 'Name',
				inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
				width: "15%",
				list: true,
				edit : true,
				create: true,
			},
			description: { 
				title: 'Description',
				width: "15%",
				list: true,
				edit : true,
				create: true,
			},
			/*commentsActTaskTypes:{
				title : '',
				list : true,
				create : false,
				edit : false,
				width: "5%",
				display:function (data) { 
					//Create an image for test script popup 
					var $img = $('<i class="fa fa-comments" title="Comments"></i>');
						$img.click(function () {
							var entityTypeIdComments = 67;
							var entityNameComments = "ActivityTaskType";
							listComments(entityTypeIdComments, entityNameComments, data.record.activityTaskTypeId, data.record.activityTaskTypeName, "actTaskTypesComments");
						});
					return $img;
					}		
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
        }
	}, function (data) { //opened handler 
		
	});
	$('#jTableUserGroups').jtable('load');

}*/

//BEGIN: ConvertDataTable - ActivityUserGroup
var activityUserGroupDT_oTable='';
var editorActivityUserGroup='';

function activityUserGroupDataTable(){
	activityUserGroupDataTableInit();
}
		
function activityUserGroupDataTableInit(){
	 var url = 'user.group.list?testFactoryId='+testFactoryId+'&productId='+productId+'&isConsolidated=false&jtStartIndex=0&jtPageSize=10000';
	 var jsonObj={"Title":"Activity User Group",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"activityUserGroup",
	};
	activityUserGroupDataTableContainer.init(jsonObj);
}

var activityUserGroupDataTableContainer = function() {
 	var initialise = function(jsonObj){
		assignActivityUserGroupDataTableValues(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignActivityUserGroupDataTableValues(jsonObj){
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
			activityUserGroupDT_Container(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function activityUserGroupDataTableHeader(){
	var childDivString ='<table id="activityUserGroup_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Name</th>'+
			'<th>Description</th>'+
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
function activityUserGroupDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForUserGroup").children().length>0) {
			$("#dataTableContainerForUserGroup").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = activityUserGroupDataTableHeader(); 			 
	$("#dataTableContainerForUserGroup").append(childDivString);
	
	editorActivityUserGroup = new $.fn.dataTable.Editor( {
	    "table": "#activityUserGroup_dataTable",
		ajax: 'user.group.add?testFactoryId='+testFactoryId+'&productId='+productId,
		ajaxUrl: 'user.group.update',
		idSrc:  "userGroupId",
		i18n: {
	        create: {
	            title:  "Create a User Group",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "Name",
            name: "userGroupName",
         },{
             label: "Description",
             name: "description",
        }        
    ]
	});
	
	activityUserGroupDT_oTable = $("#activityUserGroup_dataTable").dataTable( {				 	
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
	     		  $('#activityUserGroup_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeActivityUserGroupDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorActivityUserGroup },								 
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'User Group',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'User Group',
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
           	{ mData: "userGroupName", className: 'editable', sWidth: '50%' },
           	{ mData: "description", className: 'editable', sWidth: '50%' },
       ],       
       rowCallback: function ( row, data ) {
    	  // $('input.editorActivityUserGroup-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#activityUserGroup_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorActivityUserGroup.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#activityUserGroup_dataTable_length").css('margin-top','8px');
	$("#activityUserGroup_dataTable_length").css('padding-left','35px');		

	activityUserGroupDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutActivityUserGroupDT='';
function reInitializeActivityUserGroupDT(){
	clearTimeoutActivityUserGroupDT = setTimeout(function(){				
		activityUserGroupDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutActivityUserGroupDT);
	},200);
}
//END: ConvertDataTable - ActivityUserGroup

var isFirstLoad=true;
function setDefaultnode(defaultNodeId) {			
	if(isFirstLoad) {
		$("#treeContainerDiv").on("loaded.jstree",function(evt, data) {
			$.each($('#treeContainerDiv li'), function(ind, ele){
				if($.jstree.reference('#treeContainerDiv').is_parent($(ele).attr("id"))){
					defaultNodeId = $(ele).attr("id");							
					isFirstLoad = false;
					return false;
				}
			});	
			setDefaultnode(defaultNodeId);
		});
	} else {
		defaultNodeId = $.jstree.reference('#treeContainerDiv').get_node(defaultNodeId).children[0];
		$.jstree.reference('#treeContainerDiv').deselect_all();
		$.jstree.reference('#treeContainerDiv').close_all();
		$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
		$.jstree.reference('#treeContainerDiv').trigger("select_node");
	}
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

