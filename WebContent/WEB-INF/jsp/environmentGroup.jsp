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
<!-- <link rel="stylesheet" href="css/style.css" type="text/css" media="all"> -->
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link href="js/Scripts/validationEngine/validationEngine.jquery.css" rel="stylesheet" type="text/css" />
<link href="css/customStyle.css" rel="stylesheet" type="text/css">

<link href="js/datatable/Editor-1.5.6/css/dataTables.editor.css" rel="stylesheet" type="text/css"></link>
<link href="js/datatable/Editor-1.5.6/css/editor.dataTables.css" rel="stylesheet" type="text/css"></link>
<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>

<!-- END THEME STYLES -->
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-header-menu-fixed" class to set the mega menu fixed  -->
<!-- DOC: Apply "page-header-top-fixed" class to set the top menu fixed  -->
<body>

<!-- BEGIN HEADER -->
<div class="page-header">
	<!-- BEGIN HEADER TOP -->
	<div><%@include file="headerlayout.jsp" %></div>			
	<!-- END HEADER TOP -->
	<!-- BEGIN HEADER MENU -->
	<div class="page-header-menu">
		<div class="container container-position">			
			<!-- BEGIN MEGA MENU -->
			<!-- DOC: Apply "hor-menu-light" class after the "hor-menu" class below to have a horizontal menu with white background -->
			<!-- DOC: Remove data-hover="dropdown" and data-close-others="true" attributes below to disable the dropdown opening on mouse hover -->
			 <div><%@include file="menu.jsp" %></div>			
 			<!-- END MEGA MENU -->
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->

<!-- BEGIN: Convert DataTable ChildTablePopup-->
<div id="environmentCategoriesDT_Child_Container" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
					<h4 class="modal-title theme-font">Environment Category</h4>
				</div>
				<div class="modal-body">					
					 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
		 				<div id=dataTableContainerForEnvironmentCategory></div>
		 			</div>					 
				</div>
			</div>
	</div>
</div>
<!-- END: Convert DataTable ChildTablePopup-->

<!-- BEGIN CONTAINER -->
<div class="page-container">
<%-- <div><%@include file="treeStructureSidebar.jsp" %></div> --%>
<div><%@include file="postHeader.jsp" %></div>
<div><%@include file="singleDataTableContainer.jsp"%></div>
<div><%@include file="singleJtableContainer.jsp" %></div>

<!-- BEGIN PAGE CONTENT -->
<div class="page-content">
		<div class="container-fluid">			
			<!-- BEGIN PAGE CONTENT INNER -->
			<div class="row margin-top-10" id="toAnimate">
				<div class="col-md-12">
					<!-- BEGIN PORTLET-->
					<div class="portlet light ">
						<div class="portlet-title toolbarFullScreen" style="margin-bottom:0px;">
							<div class="caption caption-md">
								<i class="icon-bar-chart theme-font hide"></i>
								<span class="caption-subject theme-font bold uppercase">ENVIRONMENT GROUPS</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
								<span class="caption-helper hide">weekly stats...</span>
							</div>
							<div class="actions" style="padding-top: 4px;padding-left: 5px;">
								<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" 
									onClick="fullScreenHandlerDTEnvironmentGroups()" data-original-title="" title="FullScreen"></a>
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
								        <div class="cl">&nbsp;</div>
								       <div class="hidden jScrollContainerResponsive jScrollContainerFullScreen" style="clear:both;padding-top:10px"> 
								      		<div id="jTableContainer" class ="jTableContainerFullScreen"></div>
									 	</div>
									 	<div id="dataTableContainerForEnvironmentGroups"></div>
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
<div><%@include file="treePopupLayout.jsp"%></div>
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

jQuery(document).ready(function() {	
   QuickSidebar.init(); // init quick sidebar
   setBreadCrumb("Admin");
   //createHiddenFieldsForTree();
   $("#menuList li:first-child").eq(0).remove();
	//setPageTitle("Environments");
	urlToGetWorkPackagesOfSpecifiedBuildId = 'environment.group.list';
	//listEnvironmentGroup();
	environmentGroupsDataTable("ParentTable");
	//getTreeData('envicategory.envigroup.tree');
	
/* 	$("#treeContainerDiv").on("select_node.jstree",
		     function(evt, data){
	   			var entityIdAndType =  data.node.data;
	   			var arry = entityIdAndType.split("~");
	   			var key = arry[0];
	   			var type = arry[1];
	   			var title = data.node.text;
				var date = new Date();
			    var timestamp = date.getTime();
			    var nodeType = type;
			    var loMainSelected = data;
		        uiGetParents(loMainSelected);
			    if(nodeType == "EnvironmentGroup"){
		    	  productBuildId = key;
		    	  urlToGetWorkPackagesOfSpecifiedBuildId = 'environment.group.list';
		    	  listEnvironmentGroup();
			    }
			    if(nodeType == "EnvironmentCategory"){
			  	  productBuildId = key;
			  	  urlToGetWorkPackagesOfSpecifiedBuildId = 'environment.group.list';
			  	  listEnvironmentGroup();
			  }
			    urlToGetWorkPackagesOfSpecifiedBuildId = 'environment.group.list';
			  	listEnvironmentGroup();
		     }
		); */
   
});

//BEGIN: ConvertDataTable - ListOfEnvironmentGroups
var environmentGroupsDT_oTable='';
var editorEnvironmentGroups='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;
var envGroupId;
var tableType;

function environmentGroupsDataTable(tableType){
	optionsItemCounter=0;
	optionsResultArr=[];
	if(tableType == "ParentTable"){
		environmentGroupsDataTableInit(urlToGetWorkPackagesOfSpecifiedBuildId,tableType);
	}else if(tableType == "ChildTable1"){
		optionsArr = [{id:"environmentCategory", url:'administration.environment.category.list?environmentGroupId='+envGroupId+'&environmentCategoryId=0&currentEnvironmentCategoryId='+0}];
		environmentGroupsOptions_Container(optionsArr, tableType);
	}
}
		
function environmentGroupsOptions_Container(urlArr, tableType){
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
				 environmentGroupsOptions_Container(optionsArr,tableType);
			 }else{
				environmentGroupsDataTableInit(urlToGetWorkPackagesOfSpecifiedBuildId,tableType);	
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

function environmentGroupsDataTableInit(urlToGetWorkPackagesOfSpecifiedBuildId,tableType){
	var url,jsonObj={};
	if(tableType=="ParentTable"){
		 url= urlToGetWorkPackagesOfSpecifiedBuildId +'?jtStartIndex=0&jtPageSize=10';
		 jsonObj={"Title":"List of Environment Groups",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"List of Environment Groups",
		};
	}else if(tableType=="ChildTable1"){
		 url= 'environment.category.list?environmentGroupId='+envGroupId+'&jtStartIndex=0&jtPageSize=10';
		 jsonObj={"Title":"Environment Category",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Environment Category",
		};
	}
	environmentGroupsDataTableContainer.init(jsonObj);
}

var environmentGroupsDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		if(jsonObj.componentUsageTitle == "List of Environment Groups"){
 			assignEnvironmentGroupsDataTableValues(jsonObj, "ParentTable");
 		}else if(jsonObj.componentUsageTitle == "Environment Category"){
 			assignEnvironmentGroupsDataTableValues(jsonObj, "ChildTable1");
 		}
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignEnvironmentGroupsDataTableValues(jsonObj, tableType){
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
				environmentGroupsDT_Container(jsonObj);
			}else if(tableType == "ChildTable1"){
				environmentCategoryDT_Container(jsonObj);
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

function environmentGroupsDataTableHeader(){
	var childDivString ='<table id="environmentGroups_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Environment Group Name</th>'+
			'<th>Description</th>'+
			'<th>Display Name</th>'+
			'<th>Created Date</th>'+
			'<th>Modified Date</th>'+
			'<th>Status</th>'+
			'<th>Environment Category</th>'+
			'<th>Audit History</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function environmentGroupsDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForEnvironmentGroups").children().length>0) {
			$("#dataTableContainerForEnvironmentGroups").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = environmentGroupsDataTableHeader(); 			 
	$("#dataTableContainerForEnvironmentGroups").append(childDivString);
	
	editorEnvironmentGroups = new $.fn.dataTable.Editor( {
	    "table": "#environmentGroups_dataTable",
		ajax: "environment.group.add",
		ajaxUrl: "environment.group.update",
		idSrc:  "environmentGroupId",
		i18n: {
	        create: {
	            title:  "Create a new Environment Group",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "Environment Group ID",
            name: "environmentGroupId",
            "type":"hidden",
        },{
            label: "Environment Group Name*",
            name: "environmentGroupName",
        },{
            label: "Description",
            name: "description",
        },{
            label: "Display Name*",
            name: "displayName",
        },{
            label: "Created Date",
            name: "createdDate",
            "type": "hidden",
        },{
            label: "Modified Date",
            name: "modifiedDate",
            "type": "hidden",
        }        
    ]
	});
	
	environmentGroupsDT_oTable = $("#environmentGroups_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [5,6,7]; // search column TextBox Invisible Column position
	     		  $('#environmentGroups_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeEnvironmentGroupsDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorEnvironmentGroups },	
						{
						    text: '<i class="fa fa-tree" aria-hidden="true"></i>',
						    action: function ( e, dt, node, config ) {
						    	showEnvGroupEnvCategoryTree();
						    }
						},
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'List of Environment Groups',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'List of Environment Groups',
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
           { mData: "environmentGroupName", className: 'editable', sWidth: '20%' },	
           { mData: "description", className: 'editable', sWidth: '25%' },		
           { mData: "displayName", className: 'editable', sWidth: '15%' },	
           { mData: "createdDate", className: 'disableEditInline', sWidth: '12%',
        		/* mRender: function(data, type, full) {				            	
        	   		if (full.action == "create" || full.action == "edit"){
        	   			data = '08/05/2015';//data.replace('-','/');
		          	}
		           	else if(type == "display"){
		           		data = full.createdDate;
		           	} 	           	 
		            return data;	      		
              	} */
         	},
           { mData: "modifiedDate", className: 'disableEditInline', sWidth: '12%' },
           { mData: null,
              mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editorEnvironmentGroups-active">';
                    }
                    return data;
                },
                className: "dt-body-center"
            },
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
 	       				'<img src="css/images/list_metro.png" class="envCategoryImg" title="Environment Category" />'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<i class="fa fa-search-plus auditHistoryImg" title="Audit History"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
       ],       
       rowCallback: function ( row, data ) {
    	   $('input.editorEnvironmentGroups-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#environmentGroups_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorEnvironmentGroups.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#environmentGroups_dataTable tbody').on('click', 'td .envCategoryImg', function () {
		var tr = $(this).closest('tr');
    	var row = environmentGroupsDT_oTable.DataTable().row(tr);
    	envGroupId = row.data().environmentGroupId;
    	$('#environmentCategoriesDT_Child_Container').modal();
	   	$(document).off('focusin.modal');
	   	environmentGroupsDataTable("ChildTable1");
	});
	
	$('#environmentGroups_dataTable tbody').on('click', 'td .auditHistoryImg', function () {
		var tr = $(this).closest('tr');
    	var row = environmentGroupsDT_oTable.DataTable().row(tr);
    	$('#div_SingleDataTableSummary').css('top','2%');
    	listEnvironmentGroupAuditHistory(row.data().environmentGroupId);
	});
	
	$('#environmentGroups_dataTable').on( 'change', 'input.editorEnvironmentGroups-active', function () {
		editorEnvironmentGroups
            .edit( $(this).closest('tr'), false )
            .set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
            .submit();
	});
	
	$("#environmentGroups_dataTable_length").css('margin-top','8px');
	$("#environmentGroups_dataTable_length").css('padding-left','35px');		
	
	editorEnvironmentGroups.on( 'preSubmit', function ( e, o, action ) {
        if ( action !== 'remove' ) {
            var environmentGroupName = this.field( 'environmentGroupName' );
            if ( ! environmentGroupName.isMultiValue() ) {
                if ( environmentGroupName.val() ) {
                	var str = environmentGroupName.val();
                	if(/^[a-zA-Z0-9-_]*$/.test(str) == false) {
                		environmentGroupName.error( 'Please enter Valid name' );
                	}
                }else{
                	environmentGroupName.error( 'Please enter Environment Group name' );
            	}
            }
            var displayName = this.field( 'displayName' );
            if ( ! displayName.isMultiValue() ) {
                if ( displayName.val() ) {
                	var str = displayName.val();
                	if(/^[a-zA-Z0-9-_]*$/.test(str) == false) {
                		displayName.error( 'Please enter Valid Display Name' );
                	}
                }else{
                	displayName.error( 'Please enter Display Name' );
            	}
            }
            // If any error was reported, cancel the submission so it can be corrected
            if ( this.inError() ) {
                return false;
            }
        }
    } );
	
	environmentGroupsDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutEnvironmentGroupsDT='';
function reInitializeEnvironmentGroupsDT(){
	clearTimeoutEnvironmentGroupsDT = setTimeout(function(){				
		environmentGroupsDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutEnvironmentGroupsDT);
	},200);
}

function fullScreenHandlerDTEnvironmentGroups(){
	
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
		$('#testFacMode').css('max-height',displaytestFaceModeResponsive(window.innerWidth));
		
		environmentGroupsDTFullScreenHandler(true);
		reInitializeEnvironmentGroupsDT();
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
		$('#testFacMode').css('max-height','');
		
		reInitializeEnvironmentGroupsDT();				
		environmentGroupsDTFullScreenHandler(false);			
	}
}

function environmentGroupsDTFullScreenHandler(flag){
	if(flag){
		reInitializeEnvironmentGroupsDT();
		$("#environmentGroups_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
	}else{
		reInitializeEnvironmentGroupsDT();
		$("#environmentGroups_dataTable_wrapper .dataTables_scrollBody").css('max-height','450px');
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
//END: ConvertDataTable - ListOfEnvironmentGroups

//BEGIN: ConvertDataTable - EnvironmentCategory
var environmentCategoryDT_oTable='';
var editorEnvironmentCategory='';
function environmentCategoryDataTableHeader(){
	var childDivString ='<table id="environmentCategory_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Environment Category Name</th>'+
			'<th>Description</th>'+
			'<th>Display Name</th>'+
			'<th>Status</th>'+
			'<th>Parent Environment Category</th>'+
			'<th>Audit History</th>'+
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
function environmentCategoryDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForEnvironmentCategory").children().length>0) {
			$("#dataTableContainerForEnvironmentCategory").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = environmentCategoryDataTableHeader(); 			 
	$("#dataTableContainerForEnvironmentCategory").append(childDivString);
	
	editorEnvironmentCategory = new $.fn.dataTable.Editor( {
	    "table": "#environmentCategory_dataTable",
		ajax: "environment.category.add",
		ajaxUrl: "environment.category.update",
		idSrc:  "environmentGroupId",
		i18n: {
	        create: {
	            title:  "Create a new Environment Category",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "Environment Group ID",
            name: "environmentGroupId",
            "type":"hidden",
            "def": envGroupId,
        },{
            label: "Environment Category Name*",
            name: "environmentCategoryName",
        },{
            label: "Description",
            name: "description",
        },{
            label: "Display Name*",
            name: "displayName",
        },{
            label: "Parent Environment Category",
            name: "parentEnvironmentCategoryId",
            options: optionsResultArr[0],
            "type": "select",
        },{
            label: "Created Date",
            name: "createdDate",
            "type": "hidden",
        },{
            label: "Modified Date",
            name: "modifiedDate",
            "type": "hidden",
        }        
    ]
	});
	
	environmentCategoryDT_oTable = $("#environmentCategory_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [3,4,5]; // search column TextBox Invisible Column position
	     		  $('#environmentCategory_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeEnvironmentCategoryDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorEnvironmentCategory },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'List of Environment Groups',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'List of Environment Groups',
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
           { mData: "environmentCategoryName", className: 'editable', sWidth: '20%' },	
           { mData: "description", className: 'editable', sWidth: '25%' },		
           { mData: "displayName", className: 'editable', sWidth: '15%' },	
           { mData: null,
              mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editorEnvironmentCategory-active">';
                    }
                    return data;
                },
                className: "dt-body-center"
            },
            { mData: "parentEnvironmentCategoryId", className: 'editable', sWidth: '10%', editField: "parentEnvironmentCategoryId",
            	mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorEnvironmentCategory, 'parentEnvironmentCategoryId', full.parentEnvironmentCategoryId); 		           	 
		             return data;
	             },
			},	
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<i class="fa fa-search-plus auditHistoryImg" title="Audit History"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
       ],       
       rowCallback: function ( row, data ) {
    	   $('input.editorEnvironmentCategory-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#environmentCategory_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorEnvironmentCategory.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#environmentCategory_dataTable tbody').on('click', 'td .auditHistoryImg', function () {
		var tr = $(this).closest('tr');
    	var row = environmentCategoryDT_oTable.DataTable().row(tr);
    	listEnvironmentCategoryAuditHistory(row.data().environmentCategoryId);
	});
	
	$('#environmentCategory_dataTable').on( 'change', 'input.editorEnvironmentCategory-active', function () {
		editorEnvironmentCategory
            .edit( $(this).closest('tr'), false )
            .set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
            .submit();
	});
	
	$("#environmentCategory_dataTable_length").css('margin-top','8px');
	$("#environmentCategory_dataTable_length").css('padding-left','35px');		
	
	editorEnvironmentCategory.on( 'preSubmit', function ( e, o, action ) {
        if ( action !== 'remove' ) {
            var environmentCategoryName = this.field( 'environmentCategoryName' );
            if ( ! environmentCategoryName.isMultiValue() ) {
                if ( environmentCategoryName.val() ) {
                	var str = environmentCategoryName.val();
                	if(/^[a-zA-Z0-9-_]*$/.test(str) == false) {
                		environmentCategoryName.error( 'Please enter Valid name' );
                	}
                }else{
                	environmentCategoryName.error( 'Please enter Environment Category name' );
            	}
            }
            var displayName = this.field( 'displayName' );
            if ( ! displayName.isMultiValue() ) {
                if ( displayName.val() ) {
                	var str = displayName.val();
                	if(/^[a-zA-Z0-9-_]*$/.test(str) == false) {
                		displayName.error( 'Please enter Valid Display Name' );
                	}
                }else{
                	displayName.error( 'Please enter Display Name' );
            	}
            }
            // If any error was reported, cancel the submission so it can be corrected
            if ( this.inError() ) {
                return false;
            }
        }
    } );
	
	environmentCategoryDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutEnvironmentCategoryDT='';
function reInitializeEnvironmentCategoryDT(){
	clearTimeoutEnvironmentCategoryDT = setTimeout(function(){				
		environmentCategoryDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutEnvironmentCategoryDT);
	},200);
}
//END: ConvertDataTable - EnvironmentCategory

var SOURCE;
var SOURCE;
var treeData;
var productBuildId = "";
var urlToGetWorkPackagesOfSpecifiedBuildId = "";


function listEnvironmentGroup(){
	try{
		if ($('#jTableContainer').length>0) {
			 $('#jTableContainer').jtable('destroy'); 
		}
		}catch(e){}
		 $('#jTableContainer').jtable({
	         title: 'List Of Environment Groups ',
	         selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: 10, //Set page size (default: 10)
	         editinline:{enable:true},	 
	         toolbar : {
					items : [ 
						{
							icon : '',
							cssClass: 'fa fa-tree',
							click : function() {
								showEnvGroupEnvCategoryTree();
							}
						}
					]
			 },
	         actions: {
	             listAction:urlToGetWorkPackagesOfSpecifiedBuildId,
	             createAction: 'environment.group.add',
	             editinlineAction: 'environment.group.update'// ,
	         },
	         fields: {
	       
	        	 environmentGroupId: { 
	   				key: true,
	   				type: 'hidden',
	   				create: false, 
	   				edit: false, 
	   				list: false,
	   			 display: function (data) { return data.record.environmentGroupId;},
	   			}, 
	   			environmentGroupName: { 
	     	  	title: 'Environment Group Name',
	     	  	inputTitle: 'Environment Group Name <font color="#efd125" size="4px">*</font>',
	     	  	width: "20%",
	     	  	list:true,
	     	  	create:true
	  			 	},
	  		  	description: { 
	       			title: 'Description' ,
	       			
	     		  	width: "35%",  
	     	  		list:true,
		     	  	create:true
	    	   }, 
	    	   displayName: { 
	       			title: 'Display Name' ,
	       			inputTitle: 'Display Name <font color="#efd125" size="4px">*</font>',
	     	  		list:true,
		     	  	create:true
	    	   }, 
	    	   createdDate: { 
	       			title: 'Created Date' ,
	     	  		list:true,
	     	  		create:false,
	     	  		edit:false
	    	   }, 
	    	   modifiedDate: { 
	       			title: 'Modified Date' ,
	     	  		list:true,
	     	  		create:false,
	     	  		edit:false
	    	   }, 
		status: {	      
			       	title: 'Status' ,
		        	width: "5%",  
		        	list:true,
		        	edit:true,
		        	create:false,
		        	type : 'checkbox',
		        	values: {'0' : 'No','1' : 'Yes'},
			   		defaultValue: '1'
			      
	         },	 
	         
	         //Adding environment category
	          environmentCategory:{
                 	title: '',
                 	width: "5%",
                 	edit: true,
                 	create: false,
            	 	display: function (environmentGroup) { 
            		var $img = $('<img src="css/images/list_metro.png" title="Environment Category" />');
            			$img.click(function () { 
            				
            				// ----- Closing child table on the same icon click -----
	           			    closeChildTableFlag = closeJtableTableChildContainer($(this), $('#jTableContainer'));
	           				if(closeChildTableFlag){
	           					return;
	           				} 
            				
            				$('#jTableContainer').jtable('openChildTable', 
            				$img.closest('tr'), 
            					{ 
            					title: 'Add/Edit Environment Category', 
            					 editinline:{enable:true},
            					actions: { 
            						listAction: 'environment.category.list?environmentGroupId=' + environmentGroup.record.environmentGroupId , 
            						//deleteAction: 'administration.product.build.delete', 
            						editinlineAction: 'environment.category.update',  
            						createAction: 'environment.category.add'
            							}, 
            	 				recordUpdated:function(event, data){
                     				$('#jTableContainer').find('.jtable-child-table-container').jtable('reload');
                 				},
                 				recordAdded: function (event, data) {
                 					$('#jTableContainer').find('.jtable-child-table-container').jtable('reload');
                 				},
                 				recordDeleted: function (event, data) {
                 					$('#jTableContainer').find('.jtable-child-table-container').jtable('reload');
                 				},
            				
            					fields: { 
            						environmentGroupId: { 
            							type: 'hidden', 
            							defaultValue:  environmentGroup.record.environmentGroupId 
            						}, 
            						environmentCategoryId: {
            							type: 'hidden', 
                						key: true, 
                						create: false, 
					                	edit: false, 
                						list: true,
                						display: function (data) { return data.record.environmentCategoryId;}
                					},
                					environmentCategoryName: { 
             			     	  		title: 'Environment Category Name',
             			     	  		inputTitle: 'Environment Category Name <font color="#efd125" size="4px">*</font>',
             			     	  		width: "20%",
             			     	  		list:true,
             			     	  		create:true,
             			  			 	},
             			  			 description: { 
                			     	  		title: 'Description',
                			     	  		width: "20%",
                			     	  		list:true,
                			     	  		create:true,
                			  			 	},
             			  			 	displayName: { 
             				       			title: 'Display Name' ,
             				       			inputTitle: 'Display Name <font color="#efd125" size="4px">*</font>',
             				     		  	width: "35%",  
             				     	  		list:true
             					     	  	
             				    	   }, 
             			  				status: {	      
             						       	title: 'Status' ,
             					        	width: "35%",  
             					        	list:true,
             					        	edit:true,
             					        	create:false,
             					        	type : 'checkbox',
             					        	values: {'0' : 'No','1' : 'Yes'},
             						   		defaultValue: '1'
             						        },
 	
             						   parentEnvironmentCategoryId: {
             							   title: 'Parent Environment Category',
            						        width:"15%",
            						        list:true,
            						        create:true,
            						        dependsOn: 'environmentCategoryId',
	            						    options:function (data){
	            						    	if(data.source =='list')
	            			                	{	
	            						    		return 'administration.environment.category.list?environmentGroupId='+environmentGroup.record.environmentGroupId+'&environmentCategoryId='+data.dependedValues.environmentCategoryId+'&currentEnvironmentCategoryId='+data.dependedValues.environmentCategoryId;
	            			                	} else if(data.source =='create'){
	            			                		data.clearCache();
	            			                		var xx = data.dependedValues.environmentCategoryId;
	            			                		return 'administration.environment.category.list?environmentGroupId='+environmentGroup.record.environmentGroupId+'&environmentCategoryId=0&currentEnvironmentCategoryId='+0;
	            			                	}else{
            			                			return 'administration.environment.category.list?environmentGroupId='+environmentGroup.record.environmentGroupId+'&environmentCategoryId=0&currentEnvironmentCategoryId='+0;
	            			                	}
	            						    	
	            						    }
	            						   
           						      },
									auditionHistory:{
										title : 'Audit History',
										list : true,
										create : false,
										edit : false,
										width: "10%",
										display:function (data) { 
											//Create an image for test script popup 
											var $img = $('<i class="fa fa-search-plus" title="Audit History"></i>');
											//Open Testscript popup  
											$img.click(function () {
												listEnvironmentCategoryAuditHistory(data.record.environmentCategoryId);
											});
											return $img;
											}
										},
       								}, 
       								formSubmitting: function (event, data) { 
										data.form.find('input[name="environmentCategoryName"]').addClass('validate[required]');
										data.form.find('input[name="displayName"]').addClass('validate[required]');
										
       				             		data.form.validationEngine();
       				             		return data.form.validationEngine('validate');
       				           		}, 
       				            	//Dispose validation logic when form is closed
       				            	formClosed: function (event, data) {
       				               		data.form.validationEngine('hide');
       				               		data.form.validationEngine('detach');
       				           		}
            				
            			         	
            		               
            		         	}, function (data) { //opened handler 
            		         	data.childTable.jtable('load'); 
            		         	}); 
            		         	}); 
            		         	//Return image 
            		         	return $img; 
            		         	} ,
            		          },
							auditionHistory:{
								title : 'Audit History',
								list : true,
								create : false,
								edit : false,
								width: "10%",
								display:function (data) { 
									//Create an image for test script popup 
									var $img = $('<i class="fa fa-search-plus" title="Audit History"></i>');
									//Open Testscript popup  
									$img.click(function () {
										listEnvironmentGroupAuditHistory(data.record.environmentGroupId);
									});
									return $img;
								}
							},
            		          
	         },
	         formSubmitting: function (event, data) {        
	            	data.form.find('input[name="environmentGroupName"]').addClass('validate[required]'); 
	            	data.form.find('input[name="displayName"]').addClass('validate[required]');
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
		 $('#jTableContainer').jtable('load');
		 
}

function listEnvironmentGroupAuditHistory(environmentGroupId){
	//clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=EnvironmentGroup&sourceEntityId='+environmentGroupId+'&jtStartIndex=0&jtPageSize=1000';
	//var url='administration.event.list?sourceEntityType=EnvironmentGroup&sourceEntityId='+environmentGroupId;
	var jsonObj={"Title":"EnvironmentGroup Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":1000,
			"componentUsageTitle":"environmentGroupAudit",
	};
	SingleDataTableContainer.init(jsonObj);
	//SingleJtableContainer.init(jsonObj);
}

function listEnvironmentCategoryAuditHistory(environmentCategoryId){
	//clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=EnvironmentCategory&sourceEntityId='+environmentCategoryId+'&jtStartIndex=0&jtPageSize=1000';
	//var url='administration.event.list?sourceEntityType=EnvironmentCategory&sourceEntityId='+environmentCategoryId;
	var jsonObj={"Title":"EnvironmentCategory Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":1000,
			"componentUsageTitle":"environmentCategoryAudit",
	};
	SingleDataTableContainer.init(jsonObj);
	//SingleJtableContainer.init(jsonObj);
}

/////////////////////////////////Environment Group and Environment Category Tree - Begin //////////////////////////////////////////////////////


function showEnvGroupEnvCategoryTree(){
	 // --- start of testing TreePagination -------
	 
	 if ($('#jTableContainer table tbody tr').length==1 && 
			 $('#jTableContainer table tbody tr').text() == "No data available!") {			  
			callAlert("No Data Available");
		}else{
			var jsonObj={"Title":"Environment Groups and Categories",			          
	    			"urlToGetTree": "environment.group.category.tree",
	    			"urlToGetChildData": "environment.category.of.parentnode?parentId=",	    			
	    			};	 
	 	TreePagination.init(jsonObj);
	  }
	 // ---- end of testing TreePagination ------
}

/////////////////////////////////Environment Group and Environment Category Tree - End //////////////////////////////////////////////////////

</script>

<!-- END JAVASCRIPTS -->
<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
</body>
<!-- END BODY -->
</html>