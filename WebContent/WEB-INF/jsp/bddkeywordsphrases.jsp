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

<!-- <div id="header"></div> -->

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
						<div class="portlet-title toolbarFullScreen" style="margin-bottom:0px;">
							<div class="caption caption-md">
								<i class="icon-bar-chart theme-font hide"></i>
								<span class="caption-subject theme-font bold uppercase">BDD Keywords Phrases</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
								<!-- <span class="caption-helper hide">weekly stats...</span> -->
							</div>
							<div class="actions" style="padding-top: 4px;padding-left: 5px;">
								<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" 
									onClick="fullScreenHandlerDTBDDKeywordPhrases()" data-original-title="" title="FullScreen"></a>
							</div>
						</div>
						<div class="portlet-body">
							<div class="row list-separated" style="margin-top:0px;">	
							
							<!-- jtable started -->
								<!-- Main -->
								    <div id="main" style="float:left; padding-top:0px; width:100%;">
								      <!-- Box -->
								      <div class="box">								        
								         <div id="hidden"></div>
								        <!-- ContainerBox -->
								       <div class="hidden jScrollContainerResponsive jScrollContainerFullScreen" style="clear:both;padding-top:10px"> 
								      		<div id="jTableContainer" class ="jTableContainerFullScreen"></div>
									 	</div>
									 	<div style="position: absolute;z-index: 10;right: 165px;margin-top: 3px;display: block;">
											<div id="bddKeywordStatus_dd" class="col-md-4">
												<select class="form-control input-small select2me" id="bddKeywordStatus_ul">
													<option value="2" selected><a href="#">ALL</a></option>
													<option value="1" ><a href="#">Active</a></option>
													<option value="0"><a href="#">Inactive</a></option>
												</select>
											</div>
	    								</div>
									 	<div id="dataTableContainerForBDDKeywordPhrases"></div>
									<!-- End Container Box -->
								 
									<div class="cl">&nbsp;</div>
								      </div>
								      <!-- End Box -->     
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

<div id="bddKeywordPhrases_Child_Container" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;xz-index:10052;">
	<div class="modal-full">
		<div class="modal-content">
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title theme-font">Add Keyword Library</h4>
			</div>
			<div class="modal-body">					
				 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
	 				<div id="dataTableContainerForAddKeywordLibrary"></div>
	 			</div>					 
			</div>
		</div>
	</div>
</div>

<div id="addKeywordLibraryNewModal" class="modal " tabindex="-1" aria-hidden="true" style="z-index:10051">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupCloseAddKeywordLibraryFormNew()" aria-hidden="true"></button>
				<h4 class="modal-title">Add Keyword Library</h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:375px;overflow-y:scroll;" data-always-visible="1" data-rail-visible1="1">
					<form  class="form-horizontal" name="frmProfile" enctype="multipart/form-data" >
					<div class="form-body">
					
					<div class="form-group">
						<div class="col-md-6">
							<label for="input" class="control-label ">Product Type :</label>
						</div>
						<div class="col-md-6">
							<div id="productType_akl" >
								<select class="form-control select2me" id="productType_akl_ul" data-placeholder="Select...">											
								</select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6">
							<label for="input" class="control-label ">Test Engine :</label>
						</div>
						<div class="col-md-6">
							<div id="testEngine_akl" >
								<select class="form-control select2me" id="testEngine_akl_ul" data-placeholder="Select...">	
								</select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6">
							<label for="input" class="control-label ">Language :</label>
						</div>
						<div class="col-md-6">
							<div id="language_akl" >
								<select class="form-control select2me" id="language_akl_ul" data-placeholder="Select...">	
								</select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6">
							<label for="input" class=" control-label ">Class Name <font color="#efd125" size="4px"> * </font>:</label>
						</div>
						<div class="col-md-6">
							<input type="text" class="form-control" size="30" name="input"  id="className_akl"/>		
						</div>
					</div>					
					<div class="form-group">
						<div class="col-md-4">
							<label for="input" class="control-label ">Binary <font color="#efd125" size="4px"> * </font>:</label>
						</div>
						<div class="col-md-5">
							<input type="text" class="form-control" size="30" name="input"  id="keywordJARAttachmentID"/>		
						</div>
						<div class="col-md-3">
							<input type="button"  class="btn blue" id="keywordJARUpload" value="Attachment" style=" margin-top: -2px;
							   font-size: .9em;height: 26px;background-color: #55616f;padding-top: 4px" />
							<span id="testDataFileName"></span>
							<input id="uploadKeywordJAR" type="file" name="uploadKeywordJAR" style="display:none;"
							   onclick="{this.value = null;};" onchange="importKeywordJAR(event);" />
					   </div>
   					</div>					
				</div>
				</form>
				<div class="row" style="padding-top: 20px; float: right; ">
					 <button type="button" id="addKeywordLibraryFormNewSubmit" class="btn green-haze" onclick="addKeywordLibraryFormNewSubmitHandler();">Submit</button>							
					 <button type="button" class="btn grey-cascade" onclick="popupCloseAddKeywordLibraryFormNew();">Cancel</button>					
				</div>
				</div>
			</div>
		</div>
	</div>
</div>
	
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

jQuery(document).ready(function() {	
  /*  QuickSidebar.init(); // init quick sidebar
   setBreadCrumb("Admin");
   listVendors();
   $("#menuList li:first-child").eq(0).remove(); */   
   //bddkeywordsphraseslist(); 
   bddKeywordPhrasesDataTable();
   loadProductType();
   loadTestEngine();
   loadLanguage();
});

//BEGIN: ConvertDataTable - BDDKeywordPhrases
$('#bddKeywordStatus_ul').on('change', function() {
	bddKeywordPhrasesDataTable();
});

var bddKeywordPhrasesDT_oTable='';
var editorBDDKeywordPhrases='';
function bddKeywordPhrasesDataTable(){
	var bddKeywordStatus = $("#bddKeywordStatus_ul").find('option:selected').val();
	var url= 'bddkeywordsphrases.list?productType=null&testTool=null&status='+bddKeywordStatus+'&jtStartIndex=0&jtPageSize=1000';
	 var jsonObj={"Title":"BDD Keyword Phrases",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"BDD Keyword Phrases",
	};
	bddKeywordPhrasesDataTableContainer.init(jsonObj);
}

var bddKeywordPhrasesDataTableContainer = function() {
 	var initialise = function(jsonObj){
		assignBDDKeywordPhrasesDataTableValues(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignBDDKeywordPhrasesDataTableValues(jsonObj){
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
			bddKeywordPhrasesDT_Container(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function bddKeywordPhrasesDataTableHeader(){
	var childDivString ='<table id="bddKeywordPhrases_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Keywords Phrase</th>'+
			'<th>Description</th>'+
			'<th>Objects</th>'+
			'<th>Parameters</th>'+
			'<th>Regular Expression</th>'+
			'<th>Web</th>'+
			'<th>Mobile</th>'+
			'<th>Desktop</th>'+
			'<th>Embedded</th>'+
			'<th>Selenium</th>'+
			'<th>Protractor</th>'+
			'<th>TestComplete</th>'+
			'<th>Appium</th>'+
			'<th>SeeTest</th>'+
			'<th>CodedUI</th>'+
			'<th>EDAT</th>'+
			'<th>RestAssured</th>'+
			'<th>Status</th>'+
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
function bddKeywordPhrasesDT_Container(jsonObj){

	var isSelectOptionsObj={};
	var isSelectOptionsArr=[];
	isSelectOptionsObj = {"Options":[{"value":"1","label":"Yes","DisplayText":"Yes"},{"value":"0","label":"No","DisplayText":"No"}]};						
	isSelectOptionsArr.push(isSelectOptionsObj.Options);

	try{
		if ($("#dataTableContainerForBDDKeywordPhrases").children().length>0) {
			$("#dataTableContainerForBDDKeywordPhrases").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = bddKeywordPhrasesDataTableHeader(); 			 
	$("#dataTableContainerForBDDKeywordPhrases").append(childDivString);
	
	editorBDDKeywordPhrases = new $.fn.dataTable.Editor( {
	    "table": "#bddKeywordPhrases_dataTable",
		ajax: "bddkeywordsphrases.save",
		ajaxUrl: "bddkeywordsphrases.update",
		idSrc:  "id",
		i18n: {
	        create: {
	            title:  "Create a new BDD Keyword Phrase",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "ID",
            name: "id",
            "type": "hidden",
         },{
            label: "Keyword Phrase *",
            name: "keywordPhrase",
        },{
            label: "Description",
            name: "description",
            type: "textarea"
        },{
            label: "Objects",
            name: "objects",
            type: "textarea"
        },{
            label: "Parameters",
            name: "parameters",
            type: "textarea"
        },{
            label: "Regular Expression",
            name: "keywordRegularExpression",
            type: "textarea"
        },{
            label: "Web",
            name: "isWeb",
            "type": "select",
            options: isSelectOptionsArr[0]
        },{
            label: "Mobile",
            name: "isMobile", 
            "type": "select",
            options: isSelectOptionsArr[0]
        },{
            label: "Desktop",
            name: "isDesktop", 
            "type": "select",
            options: isSelectOptionsArr[0]
        },{
            label: "Embedded",
            name: "isEmbedded", 
            "type": "select",
            options: isSelectOptionsArr[0]
        },{
            label: "Selenium",
            name: "isSelenium", 
            "type": "select",
            options: isSelectOptionsArr[0]
        },{
            label: "Protractor",
            name: "isProtractor", 
            "type": "select",
            options: isSelectOptionsArr[0]
        },{
            label: "TestComplete",
            name: "isTestComplete", 
            "type": "select",
            options: isSelectOptionsArr[0]
        },{
            label: "Appium",
            name: "isAppium", 
            "type": "select",
            options: isSelectOptionsArr[0]
        },{
            label: "SeeTest",
            name: "isSeeTest", 
            "type": "select",
            options: isSelectOptionsArr[0]
        },{
            label: "CodedUI",
            name: "isCodedui", 
            "type": "select",
            options: isSelectOptionsArr[0]
        },{
            label: "EDAT",
            name: "isEDAT", 
            "type": "select",
            options: isSelectOptionsArr[0]
        },{
            label: "RestAssured",
            name: "isRestAssured", 
            "type": "select",
            options: isSelectOptionsArr[0]
        },{
            label: "Status",
            name: "status", 
            "type": "select",
            options: isSelectOptionsArr[0]
        }
    ]
	});
	
	bddKeywordPhrasesDT_oTable = $("#bddKeywordPhrases_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [18]; // search column TextBox Invisible Column position
	     		  $('#bddKeywordPhrases_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeBDDKeywordPhrasesDT();
			   },  
		   buttons: [
						{ 
							extend: "create",
							editor: editorBDDKeywordPhrases
						},
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'BDD Keyword Phrases',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'BDD Keyword Phrases',
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
           { mData: "keywordPhrase", className: 'editable', sWidth: '10%'},	
           { mData: "description", className: 'editable', sWidth: '10%'},		
           { mData: "objects", className: 'editable', sWidth: '10%'},
           { mData: "parameters", className: 'editable', sWidth: '10%'},
           { mData: "keywordRegularExpression", className: 'editable', sWidth: '10%'},	
           { mData: "isWeb",className: 'editable', editField: "isWeb",
	              mRender: function (data, type, full) {
	          	   	data = optionsValueHandler(editorBDDKeywordPhrases, 'isWeb', full.isWeb);
	             		return data;
	             },
	       },
           { mData: "isMobile",className: 'editable', editField: "isMobile",
               mRender: function (data, type, full) {
           	   	data = optionsValueHandler(editorBDDKeywordPhrases, 'isMobile', full.isMobile);
              		return data;
              },
		   },
           { mData: "isDesktop",className: 'editable', editField: "isDesktop",
	             mRender: function (data, type, full) {
	         	   	data = optionsValueHandler(editorBDDKeywordPhrases, 'isDesktop', full.isDesktop);
	            		return data;
	            },
	       },
           { mData: "isEmbedded",className: 'editable', editField: "isEmbedded",
	             mRender: function (data, type, full) {
	         	   	data = optionsValueHandler(editorBDDKeywordPhrases, 'isEmbedded', full.isEmbedded);
	            		return data;
	            },
	       },
           { mData: "isSelenium",className: 'editable', editField: "isSelenium",
		        mRender: function (data, type, full) {
		    	   	data = optionsValueHandler(editorBDDKeywordPhrases, 'isSelenium', full.isSelenium);
		       		return data;
		       },
		   },
           { mData: "isProtractor",className: 'editable', editField: "isProtractor",
		        mRender: function (data, type, full) {
		    	   	data = optionsValueHandler(editorBDDKeywordPhrases, 'isProtractor', full.isProtractor);
		       		return data;
		       },
		   },
           { mData: "isTestComplete",className: 'editable', editField: "isTestComplete",
		        mRender: function (data, type, full) {
		    	   	data = optionsValueHandler(editorBDDKeywordPhrases, 'isTestComplete', full.isTestComplete);
		       		return data;
		       },
		   },
           { mData: "isAppium",className: 'editable', editField: "isAppium",
		         mRender: function (data, type, full) {
		     	   	data = optionsValueHandler(editorBDDKeywordPhrases, 'isAppium', full.isAppium);
		        		return data;
		        },
		   },
	       { mData: "isSeeTest",className: 'editable', editField: "isSeeTest",
		         mRender: function (data, type, full) {
		      	   	data = optionsValueHandler(editorBDDKeywordPhrases, 'isSeeTest', full.isSeeTest);
		         	return data;
		         },
		   },
           { mData: "isCodedui",className: 'editable', editField: "isCodedui",
	       	    mRender: function (data, type, full) {
	       		   	data = optionsValueHandler(editorBDDKeywordPhrases, 'isCodedui', full.isCodedui);
	          		return data;
	            },
	       },
           { mData: "isEDAT",className: 'editable', editField: "isEDAT",
		    	mRender: function (data, type, full) {
		   	   		data = optionsValueHandler(editorBDDKeywordPhrases, 'isEDAT', full.isEDAT);
		      		return data;
		   	    },
		   },
           { mData: "isRestAssured",className: 'editable', editField: "isRestAssured",
		    	mRender: function (data, type, full) {
		   	   		data = optionsValueHandler(editorBDDKeywordPhrases, 'isRestAssured', full.isRestAssured);
		      		return data;
		   	    },
		   },
           { mData: "status",className: 'editable', editField: "status",
			     mRender: function (data, type, full) {
			 	   	data = optionsValueHandler(editorBDDKeywordPhrases, 'status', full.status);
			    		return data;
			    },
			},
           { mData: null,				 
           		bSortable: false,
           		mRender: function(data, type, full) {				            	
          			 var img = ('<div style="display: flex;">'+
    	       			'<button style="border: none; background-color: transparent; outline: none;">'+
    	       			'<img src="css/images/list_metro.png" class="details-control addKeywordLibraryImg" title="Add keyword library" style="margin-left: 5px;"></button>'+
    	       			'</div>');	      		
          		 	return img;
           		}
           },
       ],       
       rowCallback: function ( row, data ) {
    	  // $('input.editorBDDKeywordPhrases-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#bddKeywordPhrases_dataTable').on( 'click', 'tbody td.editable', function (e) {
		if('${roleName}' != 'Administrator'){
			return false;
		}
		editorBDDKeywordPhrases.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#bddKeywordPhrases_dataTable tbody').on('click', 'td .addKeywordLibraryImg', function () {
		var tr = $(this).closest('tr');
    	var row = bddKeywordPhrasesDT_oTable.DataTable().row(tr);
    	keywordId=row.data().id;
    	$('#bddKeywordPhrases_Child_Container').modal();
		$(document).off('focusin.modal');
		addKeywordLibraryDataTable();
	});
	
	$("#bddKeywordPhrases_dataTable_length").css('margin-top','8px');
	$("#bddKeywordPhrases_dataTable_length").css('padding-left','35px');		

	editorBDDKeywordPhrases.on( 'preSubmit', function ( e, o, action ) {
        if ( action !== 'remove' ) {
            var keywordPhrase = this.field( 'keywordPhrase' );
            if ( ! keywordPhrase.isMultiValue() ) {
                if ( !keywordPhrase.val() ) {
                	keywordPhrase.error( 'Please enter Keywords Phrase' );
            	}
            }
            
            // If any error was reported, cancel the submission so it can be corrected
            if ( this.inError() ) {
                return false;
            }
        }
    } );
	
	editorBDDKeywordPhrases.on( 'create', function ( e, o, action ) {
		bddKeywordPhrasesDataTable();
    } );
	
	editorBDDKeywordPhrases.on( 'open', function ( e, json, data ) {
		if(data!="edit"){
		    editorBDDKeywordPhrases.field('isMobile').hide();
		    editorBDDKeywordPhrases.field('isWeb').hide();
		    editorBDDKeywordPhrases.field('isDesktop').hide();
		    editorBDDKeywordPhrases.field('isEmbedded').hide();
		    editorBDDKeywordPhrases.field('isSeeTest').hide();
		    editorBDDKeywordPhrases.field('isAppium').hide();
		    editorBDDKeywordPhrases.field('isSelenium').hide();
		    editorBDDKeywordPhrases.field('isCodedui').hide();
		    editorBDDKeywordPhrases.field('isEDAT').hide();
		    editorBDDKeywordPhrases.field('isProtractor').hide();
		    editorBDDKeywordPhrases.field('isTestComplete').hide();
		    editorBDDKeywordPhrases.field('isRestAssured').hide();
		    editorBDDKeywordPhrases.field('status').hide();
		}else{
			editorBDDKeywordPhrases.field('isMobile').show();
		    editorBDDKeywordPhrases.field('isWeb').show();
		    editorBDDKeywordPhrases.field('isDesktop').show();
		    editorBDDKeywordPhrases.field('isEmbedded').show();
		    editorBDDKeywordPhrases.field('isSeeTest').show();
		    editorBDDKeywordPhrases.field('isAppium').show();
		    editorBDDKeywordPhrases.field('isSelenium').show();
		    editorBDDKeywordPhrases.field('isCodedui').show();
		    editorBDDKeywordPhrases.field('isEDAT').show();
		    editorBDDKeywordPhrases.field('isProtractor').show();
		    editorBDDKeywordPhrases.field('isTestComplete').show();
		    editorBDDKeywordPhrases.field('isRestAssured').show();
		    editorBDDKeywordPhrases.field('status').show();
		}
	} );
	
	bddKeywordPhrasesDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutBDDKeywordPhrasesDT='';
function reInitializeBDDKeywordPhrasesDT(){
	clearTimeoutBDDKeywordPhrasesDT = setTimeout(function(){				
		bddKeywordPhrasesDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutBDDKeywordPhrasesDT);
	},200);
}

function fullScreenHandlerDTBDDKeywordPhrases(){
	
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
		$('#testFacMode').css('max-height',displaytestFaceModeResponsive(window.innerWidth));
		
		bddKeywordPhrasesDTFullScreenHandler(true);
		reInitializeBDDKeywordPhrasesDT();
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
		$('#testFacMode').css('max-height','');
		
		reInitializeBDDKeywordPhrasesDT();				
		bddKeywordPhrasesDTFullScreenHandler(false);			
	}
}

function bddKeywordPhrasesDTFullScreenHandler(flag){
	if(flag){
		reInitializeBDDKeywordPhrasesDT();
		$("#bddKeywordPhrases_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
	}else{
		reInitializeBDDKeywordPhrasesDT();
		$("#bddKeywordPhrases_dataTable_wrapper .dataTables_scrollBody").css('max-height','450px');
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
//END: ConvertDataTable - BDDKeywordPhrases

//BEGIN: ConvertDatatable - Add keyword Library
var addKeywordLibraryDT_oTable='';
var editorAddKeywordLibrary='';
var keywordId;
function addKeywordLibraryDataTable(){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [{id:"productType", url:'common.list.productType'},
		              {id:"testToolMaster", url:'common.list.testToolMaster.option'},
		              {id:"testLanguages", url:'common.list.languages'}];
	addKeywordLibraryOptions_Container(optionsArr);
}
		
function addKeywordLibraryOptions_Container(urlArr){
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
				 addKeywordLibraryOptions_Container(optionsArr);
			 }else{
				 addKeywordLibraryDataTableInit();
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

function addKeywordLibraryDataTableInit(){
	var url,jsonObj={};
		url= 'keywordLibrary.list?keywordId='+keywordId+'&jtStartIndex=0&jtPageSize=1000';
		jsonObj={"Title":"Add Keyword Library",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Add Keyword Library",
		};
	addKeywordLibraryDataTableContainer.init(jsonObj);
}

var addKeywordLibraryDataTableContainer = function() {
 	var initialise = function(jsonObj){
		assignAddKeywordLibraryDataTableValues(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       initialise(jsonObj);
	    }		
	};	
}();

function assignAddKeywordLibraryDataTableValues(jsonObj){
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
				addKeywordLibraryDT_Container(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function addKeywordLibraryDataTableHeader(){
	var childDivString ='<table id="addKeywordLibrary_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Product Type</th>'+
			'<th>Test Engine</th>'+
			'<th>Language</th>'+
			'<th>Class Name</th>'+
			'<th>Binary</th>'+
			'<th>Status</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}

function addKeywordLibraryDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForAddKeywordLibrary").children().length>0) {
			$("#dataTableContainerForAddKeywordLibrary").children().remove();
		}
	}
	catch(e) {}
	
	var childDivString = addKeywordLibraryDataTableHeader(); 			 
	$("#dataTableContainerForAddKeywordLibrary").append(childDivString);
	
	editorAddKeywordLibrary = new $.fn.dataTable.Editor( {
	    "table": "#addKeywordLibrary_dataTable",
		//ajax: "keywordLibrary.save?languageId=2",
		//ajaxUrl: "build.update",
		idSrc:  "keywordLibId",
		i18n: {
	        create: {
	            title:  "Create a new Keyword Library",
	            submit: "Create",
	        }
	    },
		fields: []
	});
	
	addKeywordLibraryDT_oTable = $("#addKeywordLibrary_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [6]; // search column TextBox Invisible Column position
	     		  $('#addKeywordLibrary_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeAddKeywordLibraryDT();
			   },  
		   buttons: [
						{ 
							extend: "create",
							editor: editorAddKeywordLibrary,
							action: function ( e, dt, node, config ) {
								$("#productType_akl").find('option').eq(0).attr('selected',true).change()
								$("#testEngine_akl_ul").find('option').eq(0).attr('selected',true).change()								
								$("#language_akl_ul").find('option').eq(0).attr('selected',true).change()
								$("#className_akl").val("");
								$("#keywordJARAttachmentID").val("");
								$('#addKeywordLibraryNewModal').modal();
			                }
						},	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Add Keyword Library',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Add Keyword Library',
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
           { mData: "productTypeName", className: 'disableEditInline', sWidth: '10%'},
           { mData: "testToolName", className: 'disableEditInline', sWidth: '10%'},
			{ mData: "languageName", className: 'disableEditInline', sWidth: '10%'},
           { mData: "className", className: 'disableEditInline', sWidth: '12%' },
           { mData: "binary", className: 'disableEditInline', sWidth: '12%' },
           { mData: "status", className: 'disableEditInline', sWidth: '12%' },
           { mData: null,				 
           	bSortable: false,
				sWidth: '5%',
           		mRender: function(data, type, full) {			
	          		 var img = ('<div style="display: flex;">'+
						'<button style="border: none; background-color: transparent; outline: none;">'+
	      							'<i class="fa fa-trash-o details-control" onClick="deleteKeywordLibrary('+data.keywordLibId+')" title="Delete Keyword Library" style="padding-left: 0px;"></i></button>'+		
	    	       		'</div>');	      		
	          		 return img;
           	}
           },           
       ],
       columnDefs: [],
       rowCallback: function ( row, data ) {
    	   //$('input.editorAddKeywordLibrary-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#addKeywordLibrary_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorAddKeywordLibrary.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$("#addKeywordLibrary_dataTable_length").css('margin-top','8px');
	$("#addKeywordLibrary_dataTable_length").css('padding-left','35px');		
	
	addKeywordLibraryDT_oTable.DataTable().columns().every( function () {
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

function deleteKeywordLibrary(keywordLibId){
	var fd = new FormData();
	fd.append("keywordLibId", keywordLibId);
	
	openLoaderIcon();
	$.ajax({
		url : 'keyword.library.delete',
		data : fd,
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
				callAlert(data.Message);
				addKeywordLibraryDataTableInit();
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

var clearTimeoutAddKeywordLibraryDT='';
function reInitializeAddKeywordLibraryDT(){
	clearTimeoutAddKeywordLibraryDT = setTimeout(function(){				
		addKeywordLibraryDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutAddKeywordLibraryDT);
	},200);
}
//END: ConvertDatatable - Add keyword Library

function bddkeywordsphraseslist(){
	try{
		if ($('#jTableContainer').length>0) {
			 $('#jTableContainer').jtable('destroy'); 
		}
		}catch(e){}
		
		$('#jTableContainer').jtable({
	         title: 'BDD Keywords Phrases',
	         selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: 10, //Set page size (default: 10)
	         editinline:{enable:true},
	         //sorting: true, //Enable sortin
	         /* saveUserPreferences: false, */
	         actions: {
	             listAction: 'bddkeywordsphrases.list?productType=null&testTool=null',
	            // createAction: 'administration.vendor.add',
	             editinlineAction: 'bddkeywordsphrases.update'

	            // deleteAction: 'administration.product.environment.delete'
	         },
	         fields: {
	        	 
	        	 id: { 
	        		 		key: true,
	        				type: 'hidden',
	        				create: false, 
	        				edit: false, 
	        				list: false
	        	}, 
	        	
	        	keywordPhrase: { 
	            	title: 'KeyWordPhrase' ,
	            	inputTitle: 'Company Name <font color="#efd125" size="4px">*</font>',
	            	list:true,
	            	edit:true
	            },
	            description: { 
	            	title: 'Description' ,
	            	//inputTitle: 'Description <font color="#efd125" size="4px">*</font>',
	            	list:true,
	            	edit:true
	            },
	            objects: { 
	            	title: 'Objects',
	            	   	list:true,
	            	edit:true
	        	},
	        	parameters: { 
	            	title: 'Parameters',
	                  	list:true,
	            	edit:true
	            },
	            isCommon: { 
	            	title: 'Common',
	            	list:true,
	            	edit:true,
	            	type : 'checkbox',

		    		values: {'0' : 'No','1' : 'Yes'},
		    		defaultValue: '1'
	            },	        
	            isMobile: { 
	            	title: 'Mobile' ,
	            	
	            	list:true,
	            	edit:true,
	            	create:false,
	            	type : 'checkbox',

		    		values: {'0' : 'No','1' : 'Yes'},
		    		defaultValue: '1'
		    		},
		    		isWeb: { 
		            	title: 'Web' ,
		            	list:true,
		            	edit:true,
		            	create:false,
		            	type : 'checkbox',
			    		values: {'0' : 'No','1' : 'Yes'},
			    		defaultValue: '1'
		    		},
		    		isDesktop: { 
		            	title: 'Desktop' ,
		            	            	list:true,
		            	edit:true,
		            	create:false,
		            	type : 'checkbox',
			    		values: {'0' : 'No','1' : 'Yes'},
			    		defaultValue: '1'
		    		},
		    		isSoftwareCommon: { 
		            	title: 'SoftwareCommon' ,
		            			            	list:true,
		            	edit:true,
		            	create:false,
		            	type : 'checkbox',
			    		values: {'0' : 'No','1' : 'Yes'},
			    		defaultValue: '1'
		    		},
		    		isEmbedded: { 
		            	title: 'Embedded' ,
		                        	list:true,
		            	edit:true,
		            	create:false,
		            	type : 'checkbox',
			    		values: {'0' : 'No','1' : 'Yes'},
			    		defaultValue: '1'
		    		},
		    		isSeeTest: { 
		            	title: 'SeeTest' ,
		            			            	list:true,
		            	edit:true,
		            	create:false,
		            	type : 'checkbox',
			    		values: {'0' : 'No','1' : 'Yes'},
			    		defaultValue: '1'
		    		},
		    		isAppium: { 
		            	title: 'Appium' ,
		                     	list:true,
		            	edit:true,
		            	create:false,
		            	type : 'checkbox',
			    		values: {'0' : 'No','1' : 'Yes'},
			    		defaultValue: '1'
		    		},
		    		isSelenium: { 
		            	title: 'Selenium' ,
		                    	list:true,
		            	edit:true,
		            	create:false,
		            	type : 'checkbox',
			    		values: {'0' : 'No','1' : 'Yes'},
			    		defaultValue: '1'
		    		},
		    		isAutoIt: { 
		            	title: 'AutoIt' ,
		            	      	list:true,
		            	edit:true,
		            	create:false,
		            	type : 'checkbox',
			    		values: {'0' : 'No','1' : 'Yes'},
			    		defaultValue: '1'
		    		},
		    		isRobotium: { 
		            	title: 'Robotium' ,
		                    	list:true,
		            	edit:true,
		            	create:false,
		            	type : 'checkbox',
			    		values: {'0' : 'No','1' : 'Yes'},
			    		defaultValue: '1'
		    		},
		    		
		    		
		    		/*  hostStatus: {
		                 title: 'Status',
		                 type : 'checkbox',
		             	values: {'0' : 'INACTIVE','1' : 'ACTIVE'},
		 	    		 display: function (data) 
		                 {
		                	  if(data.record.hostStatus=="ACTIVE")
		                		  return '<img val='+data.record.status+' style="cursor:pointer;" src="css/images/right.jpg">'; 
		                	else 
		                		 return '<img val='+data.record.status+' style="cursor:pointer;" src="css/images/crossmark.jpg">';	
		      			    },
		             	// options: {'ACTIVE':'ACTIVE','INACTIVE':'INACTIVE'},
		             	 width: "10%"
		             } */
		    		status: { 
		            	title: 'Status',
		            	 	list:true,
		            	 	edit:true
		            }   		
		    		
	       },			 
            formSubmitting: function (event, data) {        
           	data.form.find('input[name="registeredCompanyName"]').addClass('validate[required, custom[AlphaNumeric_loworup_numspac(opt)], custom[minSize[3]]'); 
           	data.form.find('input[name="spocName"]').addClass('validate[required, custom[minSize[3]]');
           	data.form.find('input[name="contactEmailId"]').addClass('validate[required,custom[email]]'); 
           	data.form.find('input[name="contactNumber"]').addClass('validate[required], custom[phone], custom[minSize[10]], custom[maxSize[15]'); 
           	
            /* data.form.find('input[name="firstName"]').addClass('validate[required]');
         	data.form.find('input[name="lastName"]').addClass('validate[required]');
            data.form.find('input[name="userPassword"]').addClass('validate[required, custom[AlphaNumeric_loworup], custom[minSize[6]], custom[maxSize[20]]]');
            data.form.find('input[name="userDisplayName"]').addClass('validate[required]'); 
            data.form.find('input[name="emailId"]').addClass('validate[required,custom[email]]');
            data.form.find('input[name="contactNumber"]').addClass('validate[required], custom[phone], custom[minSize[10]], custom[maxSize[15]');
            data.form.validationEngine();  */
            
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

function popupCloseAddKeywordLibraryFormNew(){
	$('#addKeywordLibraryNewModal').modal('hide');
}

function loadProductType(){
	$('#productType_akl_ul').empty();

	$.post('common.list.productType',function(data) {	
		var ary = (data.Options);
		$.each(ary, function(index, element) {
			$('#productType_akl_ul').append('<option id="' + element.Value + '" value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
		$('#productType_akl_ul').select2();
	});
}

function loadTestEngine(){
	$('#testEngine_akl_ul').empty();

	$.post('common.list.testToolMaster.option',function(data) {	
		var ary = (data.Options);
		$.each(ary, function(index, element) {
			$('#testEngine_akl_ul').append('<option id="' + element.DisplayText + '" value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
		$('#testEngine_akl_ul').select2();
	});
}

var langAry;
var langArr;
function loadLanguage(){
	$.post('common.list.testToolMaster.option',function(data) {
		langAry = (data.Options);
		langArr = [];
	});	
}

$('#testEngine_akl_ul').change(function(){
	var testToolName = $('#testEngine_akl_ul').find('option:selected').attr('id');
	$("#language_akl_ul").empty();
	$.each(langAry, function(index, element) {
		langArr = element.options3;
		if(testToolName == element.DisplayText){
			if(Object.keys(langArr).length == 0){
				$('#language_akl_ul').append('<option id="-" value="-" ><a href="#">---</a></option>');
			}else{
				$.each( langArr, function( key, value ) {
					$('#language_akl_ul').append('<option id="' + key + '" value="' + value + '" ><a href="#">' + value + '</a></option>'); 
				});
			}
			$("#language_akl_ul").select2();
		}
	});
});

function addKeywordLibraryFormNewSubmitHandler(){
	var productTypeId = $("#productType_akl_ul").find('option:selected').attr('id');
	var testToolId = $("#testEngine_akl_ul").find('option:selected').val();
	var languagID = $("#language_akl_ul").find('option:selected').attr('id');
	var className = $("#className_akl").val();
	var keywordJARAttachmentID = $("#keywordJARAttachmentID").val();
	if(className ==""){
		callAlert("Please provide Class Name");
		return false;
	}
	if(keywordJARAttachmentID ==""){
		callAlert("Please attach the JAR file");
		return false;
	}
	var allowedFile = ['jar'];
	if($.inArray(keywordJARAttachmentID.split('.')[1], allowedFile) == -1){
		callAlert("Please attach the JAR file");
		closeLoaderIcon();
		return false;
	}
	var fileContent = document.getElementById("uploadKeywordJAR").files[0];
	var formdata = new FormData();
	formdata.append("binary",fileContent);
	formdata.append("id",keywordId);
	formdata.append("productTypeId",productTypeId);
	formdata.append("testToolId",testToolId);
	formdata.append("languagID",languagID);
	formdata.append("className",className);
	
	var url='keywordLibrary.save?languageId='+languagID;
	$.ajax({
		type: "POST",
		url: url,
		data : formdata,
		dataType:'json',		
	    contentType: false,
	    processData: false,
	    
		success: function(data) {
			openLoaderIcon();
			if(data != null){
				if(data.Result=="OK"){
					closeLoaderIcon();
					$('#addKeywordLibraryNewModal').modal('hide');
					addKeywordLibraryDataTableInit();
				}else{
					closeLoaderIcon();
					callAlert(data.Message);
				}
			}		    	
		}		   
	});	
}

</script>						
<!-- END JAVASCRIPTS -->
<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
<script src="js/importData.js" type="text/javascript"></script>
</body>
<!-- END BODY -->
</html>