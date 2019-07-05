<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@page import="com.hcl.atf.taf.controller.HomePageController"%>
<%@page import="com.hcl.atf.taf.model.UserList"%>
<%@page import="java.util.List"%>

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
<link href="css/customStyle.css" rel="stylesheet" type="text/css">
<link href="js/Scripts/validationEngine/validationEngine.jquery.css" rel="stylesheet" type="text/css" />
<!-- END THEME STYLES -->
<link href="css/bootstrap-select.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet" type="text/css"></link>
<link rel="stylesheet" type="text/css" href="css/bootstrap-datetimepicker.min.css"></link>
<link href="css/customStyle.css" rel="stylesheet" type="text/css"></link>

<link href="js/datatable/Editor-1.5.6/css/dataTables.editor.css" rel="stylesheet" type="text/css"></link>
<link href="js/datatable/Editor-1.5.6/css/editor.dataTables.css" rel="stylesheet" type="text/css"></link>
<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>

</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
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
 			<!-- END MEGA MENU -->
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->

<!-- BEGIN CONTAINER -->
<div class="page-container">
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
								<span id ="manageTestCenterLabel" class="caption-subject theme-font bold uppercase">MANAGE HELP CONTENT</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
								<span class="caption-helper hide"></span>
							</div>
							<!-- <div class="actions" style="padding-top: 4px;padding-left: 5px;">
								<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" 
									onClick="fullScreenHandlerDTTestCenters()" data-original-title="" title="FullScreen"></a>
							</div> -->
						</div>
						<div class="portlet-body">
							<div class="row list-separated" style="margin-top:0px;">	
								<!-- Main -->
								    <div id="main" style="float:left; padding-top:0px; width:100%;">
								      <!-- Box -->
								      <div class="box">								        
								         <div id="hidden"></div>
								        <!-- ContainerBox -->
								        <div class="hidden jScrollContainerResponsive jScrollContainerFullScreen" style="clear:both;padding-top:10px"> 
								     		 <div id="jTableContainer" class ="jTableContainerFullScreen"></div>
									 	</div>
									 	<div id="dataTableContainerForHelpCenters"></div>
									<!-- End Container Box -->
								 
									<div class="cl">&nbsp;</div>
								      </div>
								      <!-- End Box -->     
								    </div>
								    <!-- End Main -->
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
<script type="text/javascript">

jQuery(document).ready(function() {	
   setBreadCrumb("Admin");
   helpCentersDataTable("ParentTable");
   $("#menuList li:first-child").eq(0).remove();
   
});

//BEGIN: ConvertDataTable - TestCenters
var helpCentersDT_oTable='';
var editorHelpCenters='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;

function helpCentersDataTable(tableType){
	optionsItemCounter=0;
	optionsResultArr=[];
	if(tableType == "ParentTable"){
		helpCentersDataTableInit(tableType);	
	}
}

function helpCentersDataTableInit(tableType){
	var url,jsonObj={};
	if(tableType=="ParentTable"){
		 url= 'help.list?jtStartIndex=0&jtPageSize=10';
		 jsonObj={"Title":"Help Centers",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Test Centers",
		};
		assignHelpCentersDataTableValues(jsonObj);
	}
}

function assignHelpCentersDataTableValues(jsonObj){
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
			helpCentersDT_Container(jsonObj);
			
		 },
		 error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function helpCentersDataTableHeader(){
	var childDivString ='<table id="helpCenters_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>Name</th>'+
			'<th>Description</th>'+
			'<th>Title</th>'+
			/* '<th>Content</th>'+ */
			'<th>contentType</th>'+
			'<th>HelpFileURI</th>'+
			'<th></th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			/* '<th></th>'+ */ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function helpCentersDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForHelpCenters").children().length>0) {
			$("#dataTableContainerForHelpCenters").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = helpCentersDataTableHeader(); 			 
	$("#dataTableContainerForHelpCenters").append(childDivString);
	
	editorHelpCenters = new $.fn.dataTable.Editor( {
	    "table": "#helpCenters_dataTable",
		ajax: "help.add",
		ajaxUrl: "help.update",
		idSrc:  "helpItemId",
		i18n: {
	        create: {
	            title:  "Create a Help Content",
	            submit: "Create",
	        }
	    },
		fields: [
		{
             label: "HelpItemId",
             name: "helpItemId",
             "type":"hidden"
          },{
            label: "Name *",
            name: "name",
        },{
            label: "Description",
            name: "description", 
        },{
            label: "Title",
            name: "title",
        },{
            label: "Content",
            name: "content",
            type:  'hidden',
        },{
            label: "Content Type",
            name: "contentType", 
        },{
            label: "Help FileURI",
            name: "helpFileURI", 
        }       
    ]
	});
	
	helpCentersDT_oTable = $("#helpCenters_dataTable").dataTable( {				 	
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
	     		  $('#helpCenters_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeHelpCentersDT();
			   },  
		   buttons: [
						{ 
							extend: "create",
							editor: editorHelpCenters
						},{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'List of Help Contents',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'List of Help Contents',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		'colvis',
	         ], 
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           { mData: "helpItemId", className: 'disableEditInline', sWidth: '5%' },	
           { mData: "name", className: 'editable', sWidth: '20%' },		
           { mData: "description", className: 'editable', sWidth: '25%' },	
           { mData: "title", className: 'editable', sWidth: '10%',},
          /*  { mData: "content", className: 'disableEditInline', sWidth: '30%' }, */
           { mData: "contentType", className: 'disableEditInline', sWidth: '10%'},
           { mData: "helpFileURI", className: 'disableEditInline', sWidth: '10%'},          	
            { mData: null,				 
            	bSortable: false,
            	sWidth: '5%',
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
       				'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
	       					'<i class="fa fa-edit helpContentImg" title="Help Content"></i></button>'+		
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<i class="fa fa-trash-o deleteHelpContent" onClick="deleteHelpItem('+data.helpItemId+')" title="Delete Help"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
       ],       
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	// Activate an inline edit on click of a table cell
	$('#helpCenters_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorHelpCenters.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#helpCenters_dataTable tbody').on('click', 'td .helpContentImg', function () {
		var tr = $(this).closest('tr');
    	var row = helpCentersDT_oTable.DataTable().row(tr);
    		editorRHTMLHandler(row.data());
	});
	
	$("#helpCenters_dataTable_length").css('margin-top','8px');
	$("#helpCenters_dataTable_length").css('padding-left','35px');		
	
	helpCentersDT_oTable.DataTable().columns().every( function () {
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

//----- Deletion Help Items Started -----

function deleteHelpItem(helpItemId){
	var fd = new FormData();
	fd.append("helpItemId", helpItemId);
	
	openLoaderIcon();
	$.ajax({
		url : 'help.delete',
		data : fd,
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
				helpCentersDataTable("ParentTable");
				callAlert(data.Message);
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

var clearTimeoutTestCentersDT='';
function reInitializeHelpCentersDT(){
	clearTimeoutTestCentersDT = setTimeout(function(){				
		helpCentersDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTestCentersDT);
	},200);
}

</script>						
<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>   
<!-- END JAVASCRIPTS -->
<div><%@include file="editorHelp.jsp"%></div>
<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
</body>
<!-- END BODY -->
</html>