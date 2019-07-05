<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@page import="com.hcl.atf.taf.controller.HomePageController"%>
<%@page import="com.hcl.atf.taf.model.UserList"%>
<%@page import="java.util.List"%>
<%@page import="com.hcl.atf.taf.model.TestRunList"%>

<!DOCTYPE html>
<!-- 
Template Name: Metronic - Responsive Admin Dashboard Template build with Twitter Bootstrap 3.3.4
Version: 3.9.0
Author: KeenThemes
Website: http://www.keenthemes.com/
Contact: support@keenthemes.com
Follow: www.twitter.com/keenthemes
Like: www.facebook.com/keenthemes
Purchase: http://themeforest.net/item/metronic-responsive-admin-dashboard-template/4021469?ref=keenthemes
License: You must have a valid license purchased only from themeforest(the above link) in order to legally use the theme for your project.
-->
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8">
<link rel="shortcut icon" href="css/images/logo_new.png">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<link type="text/css" href="css/jquery.jscrollpane.css" rel="stylesheet" media="all" />
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link href="js/Scripts/validationEngine/validationEngine.jquery.css" rel="stylesheet" type="text/css" />
<!-- <link href="css/customStyle.css" rel="stylesheet" type="text/css"> -->

<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>
<!-- END THEME STYLES -->

<style type="text/css">
#filter > .col-md-4 >.select2me{
	height: 26px;
    padding-top: 2px;
    margin-top: 7px;
    width: 101px !important;
    font-size: 13px;
}
.ui-dialog-buttonset{
	width: 215px;
}
</style>

</head>
<!-- END HEAD -->
<body>
<!-- BEGIN HEADER -->
<div class="page-header">
	<!-- BEGIN HEADER TOP -->
	<div><%@include file="headerlayout.jsp" %></div>			
	<!-- END HEADER TOP -->
	<!-- BEGIN HEADER MENU -->
	<div class="page-header-menu">
		<div class="container container-position">		
			<div><%@include file="menu.jsp" %></div>			
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->

<!-- BEGIN CONTAINER -->
<div class="page-container">
<div><%@include file="postHeader.jsp" %></div>

<!-- BEGIN PAGE CONTENT -->
<div class="page-content">
		<div class="container-fluid">			
			<!-- BEGIN PAGE CONTENT INNER -->
			<div class="row margin-top-10" id="toAnimate">
				<div class="col-md-12">
					<!-- BEGIN PORTLET-->
					<div class="portlet light ">
						<div class="portlet-title  toolbarFullScreen" style="margin-bottom:0px;">
							<div class="caption caption-md">
								<i class="icon-bar-chart theme-font hide"></i>
								<span class="caption-subject theme-font bold uppercase">Dimension Management</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
								<span class="caption-helper hide">weekly stats...</span>
							</div>
							<div class="actions" style="padding-top: 4px;padding-left: 5px;">
								<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" 
									onClick="fullScreenHandlerDTDimensionManagement()" data-original-title="" title="FullScreen"></a>
							</div>
						</div>
						<div class="portlet-body">
							<div class="row list-separated" style="margin-top:0px;">	
							
							<!-- jtable started -->
								<!-- Main -->
								    <div id="main" style="float:left; padding-top:0px; width:100%;">
								    
								  
								    <div id="hdnDiv"> </div>
								    			
								    <div id="hidden"></div>
								        <!-- ContainerBox -->
								       <!-- <div class="jScrollContainer"> -->
								      <div style="clear:both;position: relative"> 
										<!-- Filter -->
										 <div id="filter" class="jScrollTitleContainer" style="float:right;position: absolute;z-index:10;right:140px;top:2px;">
											<div id="status_dd" class="col-md-4">
												<select class="form-control input-small select2me" id="status_ul">
													<option value="2" >ALL</option>
													<option value="1" selected>Active</option>
													<option value="0" >Inactive</option>
												</select>
											</div>
	    								</div>
										<!-- End Filter -->
								      <div id="jTableContainer" class ="jTableContainerFullScreen"></div>
									 </div>
									 <div id="dataTableContainerForDimensionManagement"></div>
									<!-- End Container Box -->
								 
									<div class="cl">&nbsp;</div>
								    </div>
								    <!-- End Main -->
							<!-- jtable ended -->											
							</div>							
						</div>
					</div>
					<!-- END PORTLET-->
				</div>				
			</div>			
			
			<!-- END PAGE CONTENT INNER -->
		</div>
	</div>
<!-- END PAGE CONTENT -->
</div>
<!-- END PAGE-CONTAINER -->

<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp" %></div>			
<!-- END FOOTER -->

<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/jquery.vmap.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.sparkline.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->

<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="files/lib/metronic/theme/assets/admin/layout/scripts/quick-sidebar.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jstree/dist/jstree.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->

<!-- the jScrollPane script -->
<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

<!-- Include jTable script file. -->
<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>

<!-- Plugs ins for jtable inline editing -->
<script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
<script type="text/javascript" src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>

<!-- Validation engine script file and english localization -->
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>
<script type="text/javascript">
//var customerId = ${customerId};
var urlToListDimensionBasedOnStatus ='';
jQuery(document).ready(function() {	
   urlToListDimensionBasedOnStatus = 'dimension.list.by.status?status=1&dimensionTypeId=0';
   //loadDimensionList(urlToListDimensionBasedOnStatus);
   dimensionManagementDataTable(urlToListDimensionBasedOnStatus);
   $("#menuList li:first-child").eq(0).remove();
   setBreadCrumb("Manage Dimensions");
   $(document).on('change','#status_ul', function() {
		var id = $("#status_ul").find('option:selected').val();				
	    urlToListDimensionBasedOnStatus = 'dimension.list.by.status?status='+id+'&dimensionTypeId=0';		
	    //loadDimensionList(urlToListDimensionBasedOnStatus);
	    dimensionManagementDataTable(urlToListDimensionBasedOnStatus);
	 });
});

/*function loadDimensionList(urlToListDimensionBasedOnStatus){	
	try{
		if($('#jTableContainer').length > 0){
			$('#jTableContainer').jtable("destroy");
		}
	}catch(e){
		
	}
	$('#jTableContainer').jtable({
		title: 'Add/Edit Dimension(s)',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	         
        //sorting: true, //Enable sortin
        recordsLoaded: function(event, data) {
       	 $(".jtable-edit-command-button").prop("disabled", true);
        },
        actions: {
            listAction: urlToListDimensionBasedOnStatus,
    		 createAction: 'dimension.add',
            editinlineAction: 'dimension.update'
            //deleteAction: 'dimension.delete'
        },
        fields: {
        				   		
  			dimensionId: { 
  				key: true,
  				type: 'hidden',
  				create: false, 
  				edit: false, 
  				list: false,
  				display: function (dataDimension) {return dataDimension.record.dimensionId;},	   			 
  			}, 
  			name: { 
    	  		title: 'Name',
    	  		width: '20%',
    	  		edit: true,
    	  		inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
    	  		list:true
 			},
 		  	description: { 
      			title: 'Description' ,
      			width: '30%',
      			edit: true,
    		  	list:true
   	  		},
	   	  	typeId:{
	           	title: 'Type',
	           	width: '25%',
	           	list:true,
	           	edit: false,
	           	create:true,
	           	inputTitle: 'Type <font color="#efd125" size="4px">*</font>',
	           	options:function(data){
			        return 'dimension.type.list.options';
			    },		        	 	
	       	},
   	  		parentId:{
	           	title: 'Parent',
	           	width: '25%',
	           	list:true,
	           	edit: false,
	           	create:true,
	           	dependsOn: 'typeId', 
	           	options:function(data){
			        return 'dimension.list.options?dimensionTypeId='+data.dependedValues.typeId;
			    },		        	 	
           	},  
			status: {	      
		       	title: 'Status' ,
		       	width: '15%',
	        	list:true,
	        	edit:true,
	        	create:false,
	        	type : 'checkbox',
	        	values: {'0' : 'No','1' : 'Yes'},
		   		defaultValue: '1'
		    },
		    ownerName: { 
      			title: 'Owner' ,
      			width: '30%',
      			edit: false,
    		  	create: false,
    		  	list:true
   	  		}, 
			commentsDimension:{
				title : '',
				list : true,
				create : false,
				edit : false,
				width: "5%",
				display:function (data) { 
					//Create an image for test script popup 
					var $img = $('<i class="fa fa-comments" title="Comments"></i>');
					$img.click(function () {
						var entityTypeIdComments = 62;
						var entityNameComments = "Dimension";
						listComments(entityTypeIdComments, entityNameComments, data.record.dimensionId, data.record.name, "dimensionComments");
					});
					return $img;
				}		
			}, 
   	  	
        },
        
        //Moved Formcreate validation to Form Submitting
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
        	data.form.find('input[name="name"]').addClass('validate[required], custom[minSize[3]], custom[maxSize[50]]');
			data.form.find('select[name="typeId"]').addClass('validate[required]');
			data.form.validationEngine();
            return data.form.validationEngine('validate');
        }, 
        
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }	    		
     });	 
	 $('#jTableContainer').jtable('load');	
}*/

//BEGIN: ConvertDataTable - DimensionManagement
var dimensionManagementDT_oTable='';
var editorDimensionManagement='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;

function dimensionManagementDataTable(){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [{id:"dimensionTypeList", url:'dimension.type.list.options'},
	              {id:"dimensionTypeParent", url:'dimension.list.options?dimensionTypeId=1'},
	              ];
	dimesionTypeOptions_Container(optionsArr);
}
		
function dimesionTypeOptions_Container(urlArr){
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
				 dimesionTypeOptions_Container(optionsArr);
			 }else{
				dimensionManagementDataTableInit(urlToListDimensionBasedOnStatus);	
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

function dimensionManagementDataTableInit(urlToListDimensionBasedOnStatus){
	var url= urlToListDimensionBasedOnStatus +'&jtStartIndex=0&jtPageSize=10';
	 var jsonObj={"Title":" Dimension Management",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"componentUsageTitle",
	};
	dimensionManagementDataTableContainer.init(jsonObj);
}

var dimensionManagementDataTableContainer = function() {
 	var initialise = function(jsonObj){
		assignDimensionManagementDataTableValues(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignDimensionManagementDataTableValues(jsonObj){
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
			dimensionManagementDT_Container(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function dimensionManagementDataTableHeader(){
	var childDivString ='<table id="dimensionManagement_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Name</th>'+
			'<th>Description</th>'+
			'<th>Type</th>'+
			'<th>Parent</th>'+
			'<th>Status</th>'+
			'<th>Owner</th>'+
			'<th>Comments</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function dimensionManagementDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForDimensionManagement").children().length>0) {
			$("#dataTableContainerForDimensionManagement").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = dimensionManagementDataTableHeader(); 			 
	$("#dataTableContainerForDimensionManagement").append(childDivString);
	
	editorDimensionManagement = new $.fn.dataTable.Editor( {
	    "table": "#dimensionManagement_dataTable",
		ajax: "dimension.add",
		ajaxUrl: "dimension.update",
		idSrc:  "typeId",
		i18n: {
	        create: {
	            title:  "Create a new Dimension Management",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "Name",
            name: "name",
         },{
            label: "Description",
            name: "description",
        },{
            label: "Type Id",
            name: "typeId",
            options: optionsResultArr[0],
            "type":"select"
        },{
            label: "Parent",
            name: "parentId",
            options: optionsResultArr[1],
            "type":"select"
        },{
            label: "Status",
            name: "status", 
            "type": "hidden",
        },{
            label: "Owner",
            name: "ownerName", 
            "type": "hidden",
        }        
    ]
	});
	
	dimensionManagementDT_oTable = $("#dimensionManagement_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [4,5,6]; // search column TextBox Invisible Column position
	     		  $('#dimensionManagement_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeDimensionManagementDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorDimensionManagement },								 
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Dimension Management',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Dimension Management',
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
           { mData: "name", className: 'editable', sWidth: '15%' },	
           { mData: "description", className: 'editable', sWidth: '12%' },		
           { mData: "typeId",
              	mRender: function (data, type, full) {
	 				data = optionsValueHandler(editorDimensionManagement, 'typeId', full.typeId);
                    return data;
                },
        	},
           { mData: "parentId",
               	mRender: function (data, type, full) {
	 				data = optionsValueHandler(editorDimensionManagement, 'parentId', full.parentId);
                    return data;
                },
           },
           { mData: null,
              mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editorDimensionManagement-active">';
                    }
                    return data;
                },
                className: "dt-body-center"
            },
            { mData: "ownerName", className: 'disableEditInline', sWidth: '15%' },	
            { mData: null,				 
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
    	   $('input.editorDimensionManagement-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#dimensionManagement_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorDimensionManagement.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#dimensionManagement_dataTable tbody').on('click', 'td .commentsImg', function () {
		var entityTypeIdComments = 62;
		var entityNameComments = "Dimension";
		var tr = $(this).closest('tr');
    	var row = dimensionManagementDT_oTable.DataTable().row(tr);
		listComments(entityTypeIdComments, entityNameComments, row.data().dimensionId, row.data().name, "dimensionComments");
	});
	
	$('#dimensionManagement_dataTable').on( 'change', 'input.editorDimensionManagement-active', function () {
		editorDimensionManagement
            .edit( $(this).closest('tr'), false )
            .set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
            .submit();
	});
	
	$("#dimensionManagement_dataTable_length").css('margin-top','8px');
	$("#dimensionManagement_dataTable_length").css('padding-left','35px');		

	dimensionManagementDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutDimensionManagementDT='';
function reInitializeDimensionManagementDT(){
	clearTimeoutDimensionManagementDT = setTimeout(function(){				
		dimensionManagementDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDimensionManagementDT);
	},200);
}

function fullScreenHandlerDTDimensionManagement(){
	
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
		$('#testFacMode').css('max-height',displaytestFaceModeResponsive(window.innerWidth));
		
		dimensionManagementDTFullScreenHandler(true);
		reInitializeDimensionManagementDT();
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
		$('#testFacMode').css('max-height','');
		
		reInitializeDimensionManagementDT();				
		dimensionManagementDTFullScreenHandler(false);			
	}
}

function dimensionManagementDTFullScreenHandler(flag){
	if(flag){
		reInitializeDimensionManagementDT();
		$("#dimensionManagement_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
	}else{
		reInitializeDimensionManagementDT();
		$("#dimensionManagement_dataTable_wrapper .dataTables_scrollBody").css('max-height','450px');
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
//END: ConvertDataTable - DimensionManagement

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
</script>
<div><%@include file="comments.jsp"%></div>
<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
<script type="text/javascript" src="js/datatable/Editor-1.5.6/js/dataTables.editor.js"></script>
					
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>