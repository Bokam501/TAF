<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<link type="text/css" href="css/jquery.jscrollpane.css" rel="stylesheet"
	media="all" />
<link rel="stylesheet" href="css/style.css" type="text/css" media="all">
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css"
	type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link
	href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235"
	rel="stylesheet" type="text/css" />
<link href="css/customStyle.css" rel="stylesheet" type="text/css">
<link href="js/Scripts/validationEngine/validationEngine.jquery.css"
	rel="stylesheet" type="text/css" />
<!-- END THEME STYLES -->
<link href="css/bootstrap-select.min.css" rel="stylesheet"
	type="text/css"></link>
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet"
	type="text/css"></link>
<link href="css/customStyle.css" rel="stylesheet" type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet"
	type="text/css"></link>
<link rel="stylesheet" type="text/css"
	href="css/bootstrap-datetimepicker.min.css"></link>
<link rel="stylesheet" href="css/iosOverlay.css" type="text/css" media="all">
<link rel="shortcut icon" href="favicon.ico">

<link href="js/datatable/Editor-1.5.6/css/dataTables.editor.css" rel="stylesheet" type="text/css"></link>
<link href="js/datatable/Editor-1.5.6/css/editor.dataTables.css" rel="stylesheet" type="text/css"></link>
<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>

<style>

.ui-ios-overlay {
  top: 35%;
  left: 90%;
  width: 100px;
  height: 100px;
}
.ui-ios-overlay img {
	 height:50px;
	width:50px;
}

.ui-ios-overlay .title {
	font-size: 15px;
	bottom:10px;
}
</style>

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
		<div><%@include file="headerlayout.jsp"%></div>
		<!-- END HEADER TOP -->
		<!-- BEGIN HEADER MENU -->
		<div class="page-header-menu">
			<div class="container container-position">
				<!-- BEGIN MEGA MENU -->
				<!-- DOC: Apply "hor-menu-light" class after the "hor-menu" class below to have a horizontal menu with white background -->
				<!-- DOC: Remove data-hover="dropdown" and data-close-others="true" attributes below to disable the dropdown opening on mouse hover -->
				<div><%@include file="menu.jsp"%></div>
				<!-- END MEGA MENU -->
			</div>
		</div>
		<!-- END HEADER MENU -->
	</div>
	<!-- END HEADER -->

	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<div><%@include file="treeStructureSidebar.jsp"%></div>
		<div><%@include file="postHeader.jsp"%></div>
		<div><%@include file="singleDataTableContainer.jsp"%></div>
		<%-- <div><%@include file="singleJtableContainer.jsp" %></div> --%>
				
		<div id="reportbox" style="display: none;"></div>
		<!-- BEGIN PAGE CONTENT -->
		<div class="page-content">
			<div class="container-fluid">
				<!-- BEGIN PAGE CONTENT INNER -->
				<div class="row margin-top-10" id="toAnimate">
					<div class="col-md-12">
						<!-- BEGIN PORTLET-->
						<div class="portlet light ">
							<div class="portlet-title">
								<div class="caption caption-md">
									<span class="caption-subject theme-font bold uppercase">Products
									</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
								</div>
								<div class="actions" style="padding-top: 4px;padding-left: 5px;">
									<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" 
										onClick="fullScreenHandlerDTClarificationList()" data-original-title="" title="FullScreen"></a>
								</div>
							</div>
							<div class="portlet-body form">
								<div class="row ">
									<div class="col-md-12 toolbarFullScreen">
										<ul class="nav nav-tabs" id="tabslist">
											<li class="active"><a href="#clarificationsList" data-toggle="tab">Clarifications List</a></li>
											<li><a href="#changeRequestsList" data-toggle="tab">ChangeRequests List</a></li>
											<li><a href="#consolidatedAttachments" data-toggle="tab">Attachments</a></li>
										</ul>
									</div>
										<div class="tab-content" id="tbCntnt" >
											<!--  <div class="tab-content">  -->
											<div id="clarificationsList" class="tab-pane fade active in">
												<!-- ContainerBox -->
												<div class="hidden" style="clear:both;padding-top:10px;">
													<div id="jTableClarifications"></div>
												</div>
												<div id="dataTableContainerForClarificationList"></div>
												<!-- End Container Box -->
											</div>
										
											<div id="changeRequestsList" class="tab-pane fade active in">
												<!-- ContainerBox -->
												<div class="hidden" style="clear:both;padding-top:10px;">
													<div id="jTableChangeRequestsList"></div>
												</div>
												<div id="dataTableContainerForChangeRequestList"></div>
												<!-- End Container Box -->
											</div>
											
											<div id="consolidatedAttachments" class="tab-pane fade">
												<!-- ContainerBox -->
												<div class="hidden jScrollContainerResponsive" style="clear:both;padding-top:10px;">
													<div id="jTableConsolidatedAttachments"></div>
												</div>
												<div id="dataTableContainerForAttachments"></div>
												<!-- End Container Box -->
											</div>
											<!--  </div>    -->
										</div>
									
								</div>
								<div id="hidden">
								<input type='hidden' id='tcid'/></div>

 							</div>
							<!-- END PORTLET-->
						</div>
					</div>

					<!-- END PAGE CONTENT INNER -->
				</div>
			</div>
			<!-- END PAGE CONTENT -->
		</div>
	</div>
	<!-- END PAGE-CONTAINER -->

	<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp"%></div>
	<!-- END FOOTER -->

	<!-- Popup -->
	<div id="div_PopupMain" class="divPopUpMain" style="display: none;">
		<div title="Press Esc to close" onclick="popupClose()"
			class="ImgPopupClose"></div>
	</div>

	<div id="div_PopupBackground"></div>
	<!-- Popup Ends -->
	
	<!-- BEGIN: Convert DataTable ChildTablePopup-->
	<div id="changeRequestDT_Child_Container" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Change Request</h4>
					</div>
					<div class="modal-body">					
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id=dataTableContainerForChangeRequest></div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
	
	<div id="transactionDT_Child_Container" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
						<h4 class="modal-title theme-font">Transaction Clarification</h4>
					</div>
					<div class="modal-body">					
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id=dataTableContainerForTransaction></div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
	<!-- END: Convert DataTable ChildTablePopup-->
			
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script
		src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/jquery.vmap.js"
		type="text/javascript"></script>
	<script
		src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js"
		type="text/javascript"></script>	
	<script
		src="files/lib/metronic/theme/assets/global/plugins/jquery.sparkline.min.js"
		type="text/javascript"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<script type="text/javascript" src="js/jquery.bootstrap.wizard.min.js"></script>

	<!-- BEGIN PAGE LEVEL SCRIPTS -->	
	<script
		src="files/lib/metronic/theme/assets/admin/layout/scripts/quick-sidebar.js"
		type="text/javascript"></script>
	<script
		src="files/lib/metronic/theme/assets/global/plugins/jstree/dist/jstree.min.js"
		type="text/javascript"></script>
	<!-- END PAGE LEVEL SCRIPTS -->

	<script src="js/select2.min.js" type="text/javascript"></script>
	<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
	<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
	<script src="js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
	<script src="js/moment.min.js" type="text/javascript"></script>
	<script src="js/daterangepicker.js" type="text/javascript"></script>
	<script src="js/components-pickers.js" type="text/javascript"></script>

	<!-- the jScrollPane script -->
	<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

	<!-- Include jTable script file. -->
	<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>

	<!-- Plugs ins for jtable inline editing -->
	<script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
	<!-- <script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.modifedColumn.js"></script> -->
	<script type="text/javascript" src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>	

	<!-- <script type="text/javascript" src="js/Scripts/popup/jquery.jtable.js"></script> -->

	<!-- Validation engine script file and english localization -->
	<script type="text/javascript"
		src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
	<script type="text/javascript"
		src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>

	<script type="text/javascript" src="js/draganddrop/Sortable.js"></script>
	<script type="text/javascript" src="js/draganddrop/ng-sortable.js"></script>
	<script type="text/javascript" src="js/Scripts/popup/iosOverlay.js"></script>

	<script type="text/javascript">
var key ='';
var nodeType='';
var addorno="yes";
var userRoleId='-1';
var userId='-1';
var temp = $("#userdisplayname").text();
var defaultRaisedBy = temp.split('[')[0].trim();
var defaultuserId = '${userId}';
var testFactId=0;
var clarificationTypeId = 31;
var changeRequestTypeId = 42;
var referenceTestfactoryId = 0;
var referenceProductId = 0;
var referenceEntityTypeId = 0;
var referenceEntityInstanceId = 0;


jQuery(document).ready(function() {	
   QuickSidebar.init(); // init quick sidebar
   ComponentsPickers.init();
   setBreadCrumb("Clarification");
   createHiddenFieldsForTree();
   setPageTitle("Products");
   getTreeData("administration.product.tree");
   setDefaultnode();
   
   userRoleId='<%=session.getAttribute("roleId")%>';
   userId='<%=session.getAttribute("ssnHdnUserId")%>'; 
   $("#treeContainerDiv").on("select_node.jstree",
		     function(evt, data){
	   			var entityIdAndType =  data.node.data;
	   			var arry = entityIdAndType.split("~");
	   			 key = arry[0];
	   			var type = arry[1];
			    nodeType = type;
			    var loMainSelected = data;
		        uiGetParents(loMainSelected);
		        
		         if(nodeType == "TestFactory"){
				    	testFactoryId = key;
				    	document.getElementById("treeHdnCurrentTestFactoryId").value=testFactoryId;
				    	productId=0;
				    	
				    	referenceTestfactoryId = key;
				    	referenceProductId = 0;
				    	referenceEntityTypeId = 14;
				    	referenceEntityInstanceId = key;
			    }
			    if(nodeType == "Product"){
			    		productId = key;
			    		testFactoryId = document.getElementById("treeHdnCurrentTestFactoryId").value;
			    		
			    		referenceTestfactoryId = testFactoryId;
				    	referenceProductId = key;
				    	referenceEntityTypeId = 18;
				    	referenceEntityInstanceId = key;
				    	
			    		if(productId==null && productId==undefined){
			    			productId=0;
			    	}
			   	}
			    if(nodeType == "activityworkpackage"){
		    		productId = key;
		    		testFactoryId = document.getElementById("treeHdnCurrentTestFactoryId").value;
		    		
		    		referenceTestfactoryId = testFactoryId;
			    	referenceProductId = key;
			    	referenceEntityTypeId = 34;
			    	referenceEntityInstanceId = key;
			    	
		    		if(productId==null && productId==undefined){
		    			productId=0;
		    	}
		   	}
			    $('#tabslist li').first().find("a").trigger("click");
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

//The following three methods are required for drop down listing (static/dynamic)
//function to set select item in the dropdown
var productId=-1;
var productVersionId=-1;
function setTitle(dd,id,text){	
	dv =$(dd).children('div');
	dv.text(text);	
	dv.attr('id',id);	
}

document.onkeydown = function(evt) {
	evt = evt || window.event;
	if (evt.keyCode == 27) {
		if (document.getElementById("div_PopupMain").style.display == 'block') {
			popupClose();
		}
	}
};

//init custom dropdown menu handler
function DropDown(el) {
			this.dd = el;
			this.initEvents();
		}
		DropDown.prototype = {
			initEvents : function() {
				var obj = this;
				obj.dd.on('click', function(event){
					$(this).toggleClass('active');
					event.stopPropagation();
				});	
			}
		},
		//init custom dropdown menu
		$(function() {
 		});
		
 
$(document).on('click', '#tabslist>li', function(){
	var selectedTab = $(this).index();
	tabSelection(selectedTab);
});

function tabSelection(selectedTab){
	if (selectedTab == 0){
		//callJtable(referenceTestfactoryId, referenceProductId, referenceEntityTypeId, referenceEntityInstanceId);
		clarificationListDataTable(referenceTestfactoryId, referenceProductId, referenceEntityTypeId, referenceEntityInstanceId, "ParentTable1Tab1");
	}else if (selectedTab == 1){
		//listChangeRequestsBasedOnTestFactIdOrProductId(referenceTestfactoryId, referenceProductId, referenceEntityTypeId, referenceEntityInstanceId);
		clarificationListDataTable(referenceTestfactoryId, referenceProductId, referenceEntityTypeId, referenceEntityInstanceId, "ParentTable2Tab2");
	}
	else if (selectedTab == 2){
		//displayConsolidatedAttachments();
		clarificationListDataTable(referenceTestfactoryId, referenceProductId, referenceEntityTypeId, referenceEntityInstanceId, "ParentTable3Tab3");
	}
}

//BEGIN: ConvertDataTable - ClarificationList - Tab1
var clarificationListDT_oTable='';
var editorClarificationList='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;
var tableType;
var clarfnTrackerId;

function clarificationListDataTable(testfactoryId, productId, entityTypeId, entityInstanceId, tableType){
	optionsItemCounter=0;
	optionsResultArr=[];
	if(tableType == "ParentTable1Tab1"){
		optionsArr = [{id:"clarificationTypeList", url:'list.clarification.type.option'},
		              {id:"executionPriorityList", url:'administration.executionPriorityList'},
		              {id:"statusOptionList", url:'clarification.status.option.list?entityTypeId=31'},
		              {id:"parentStatusList", url:'clarification.resolution.option.list?parentStatusId=15'},
		              {id:"allUsersList", url:'common.allusers.list.by.resourcepool.id'},
		              ];
		
	}else if(tableType == "ParentTable2Tab2"){
		optionsArr = [{id:"productList", url:'common.user.list.by.resourcepool.id.productId?productId='+productId},
		              {id:"executionPriorityList", url:'administration.executionPriorityList'},
		              {id:"statusCategoryList", url:'status.category.option.list'},
		              ];
		
	}else if(tableType == "ParentTable3Tab3"){
		optionsArr = [{id:"attachmentType", url:'attachment.type.for.entity.list.option?entityTypeId=0'},
		              ];
		
	}else if(tableType == "ChildTable1"){
		optionsArr = [{id:"productList", url:'administration.product.list.options.by.ids?testFactoryId='+testFactoryId+'&productId='+productId},
		              {id:"changeRequestType", url:'changerequest.type.option'},
		              {id:"executionPriorityList", url:'administration.executionPriorityList'},
		              {id:"statusCategoryList", url:'status.category.option.list'},
		              {id:"commonUserList", url:'common.user.list.by.resourcepool.id.productId?productId='+productId},
		              ];
		
	}else if(tableType == "ChildTable2"){
		optionsArr = [{id:"clarificationOptionsList", url:'transaction.clarification.option.list?clarificationTrackerId='+clarfnTrackerId}];
	}
	clarificationListOptions_Container(optionsArr, tableType);
}
		
function clarificationListOptions_Container(urlArr, tableType){
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
				 clarificationListOptions_Container(optionsArr,tableType);
			 }else{
				clarificationListDataTableInit(tableType);	
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

function clarificationListDataTableInit(tableType){
	var url,jsonObj={};
	if(tableType=="ParentTable1Tab1"){
		 url= 'list.clarificationtracker.by.engagementAndProductId?testFactoryId='+testFactoryId+'&productId='+productId+'&jtStartIndex=0&jtPageSize=10';
		 jsonObj={"Title":" Clarification",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Clarification",
		};
	}else if(tableType=="ParentTable2Tab2"){
		 url= 'list.changeRequest.by.engagementAndProductId?testFactoryId='+testFactoryId+'&productId='+productId+'&jtStartIndex=0&jtPageSize=10';
		 jsonObj={"Title":"Change Request List",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Change Request List",
		};
	}else if(tableType=="ParentTable3Tab3"){
		 url= 'attachments.consolidated.for.product.list?productId='+referenceProductId+'&jtStartIndex=0&jtPageSize=10';
		 jsonObj={"Title":"Attachments",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Attachments",
		};
	}else if(tableType=="ChildTable1"){
		url = 'list.changerequests.by.id?entityType1='+clarificationTypeId+'&entityType2='+changeRequestTypeId+'&entityInstanceId1='+clarfnTrackerId+'&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":" Change Request",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Change Request",
		};
	}else if(tableType=="ChildTable2"){
		url = 'transaction.clarification.list?clarificationTrackerId='+clarfnTrackerId+'&jtStartIndex=0&jtPageSize=10';
		jsonObj={"Title":"Transaction Clarification",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Transaction Clarification",
		};
	}
	clarificationListDataTableContainer.init(jsonObj);
}

var clarificationListDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		if(jsonObj.componentUsageTitle == "Clarification"){
 			assignClarificationListDataTableValues(jsonObj, "ParentTable1Tab1");
 			
 		}else if(jsonObj.componentUsageTitle == "Change Request List"){
 			assignClarificationListDataTableValues(jsonObj, "ParentTable2Tab2");
 			
 		}else if(jsonObj.componentUsageTitle == "Attachments"){
 			assignClarificationListDataTableValues(jsonObj, "ParentTable3Tab3");
 			
 		}else if(jsonObj.componentUsageTitle == "Change Request"){
 			assignClarificationListDataTableValues(jsonObj, "ChildTable1");
 			
 		}else if(jsonObj.componentUsageTitle == "Transaction Clarification"){
 			assignClarificationListDataTableValues(jsonObj, "ChildTable2");
 		}
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignClarificationListDataTableValues(jsonObj, tableType){
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
			if(tableType == "ParentTable1Tab1"){
				clarificationListDT_Container(jsonObj);
				
			}else if(tableType == "ParentTable2Tab2"){
				changeRequestListDT_Container(jsonObj);
				
			}else if(tableType == "ParentTable3Tab3"){
				attachmentsDT_Container(jsonObj);
				
			}else if(tableType == "ChildTable1"){
				changeRequestDT_Container(jsonObj);
				
			}else if(tableType == "ChildTable2"){
				transactionDT_Container(jsonObj);
				
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

function clarificationListDataTableHeader(){
	var childDivString ='<table id="clarificationList_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>Source Entity</th>'+
			'<th>Source Entity Name</th>'+
			'<th>Title</th>'+
			'<th>Type</th>'+
			'<th>Description</th>'+
			'<th>Priority</th>'+
			'<th>Planned Value</th>'+
			'<th>Status</th>'+
			'<th>Resolution</th>'+ 
			'<th>Owner</th>'+
			'<th>Raised By</th>'+
			'<th>Raised Date</th>'+
			'<th>Expected End Date</th>'+
			'<th>Change Request</th>'+
			'<th>Transaction</th>'+
			'<th>Audit History</th>'+
			'<th>Attachments</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function clarificationListDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForClarificationList").children().length>0) {
			$("#dataTableContainerForClarificationList").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = clarificationListDataTableHeader(); 			 
	$("#dataTableContainerForClarificationList").append(childDivString);
	
	editorClarificationList = new $.fn.dataTable.Editor( {
	    "table": "#clarificationList_dataTable",
		ajax: "add.clarificationtracker.by.activity",
		ajaxUrl: "update.clarificationtracker.by.activity",
		idSrc:  "entityInstanceId",
		i18n: {
	        create: {
	            title:  "Create a new Clarification",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "ID",
            name: "clarificationTrackerId",
            "type": "hidden",
         },{
             label: "Entity Type",
             name: "entityType",
             "type": "hidden",
          },{
              label: "Entity Instance Name",
              name: "entityInstanceName",
              "type": "hidden",
           },{
             label: "Clarification Title",
             name: "clarificationTitle",
         },{
             label: "Clarification Type",
             name: "clarificationTypeId",
             options: optionsResultArr[0],
             "type":"select"
         },{
             label: "priority",
             name: "priorityId",
             options: optionsResultArr[1],
             "type":"select"
          },{
            label: "Description",
            name: "clarificationDescription",
        },{
            label: "Planned Value",
            name: "planExpectedValue",
        },{
            label: "Status",
            name: "workflowStatusId",
            options: optionsResultArr[2],
            "type":"select"
        },{
            label: "Resolution",
            name: "resolution",
            options: optionsResultArr[3],
            "type":"select"
        },{
            label: "Owner",
            name: "ownerId",
            options: optionsResultArr[4],
            "type":"select"
        },{
            label: "Raised By",
            name: "raisedById",
            options: optionsResultArr[5],
            "type":"select"
        },{
            label: "Raised Date",
            name: "raisedDate", 
        },{
            label: "Expected End Date",
            name: "plannedEndDate", 
        }        
    ]
	});
	
	clarificationListDT_oTable = $("#clarificationList_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [4,6,8,9,10,11,14,15,16,17]; // search column TextBox Invisible Column position
	     		  $('#clarificationList_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeClarificationListDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorClarificationList },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Clarification',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Clarification',
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
           { mData: "clarificationTrackerId", className: 'disableEditInline', sWidth: '15%' },	
           { mData: "entityType", className: 'disableEditInline', sWidth: '15%' },	
           { mData: "entityInstanceName",  sWidth: '12%' },
           { mData: "clarificationTitle", className: 'editable', sWidth: '12%' },	
           { mData: "clarificationTypeId", className: 'editable', sWidth: '10%', editField: "clarificationTypeId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorClarificationList, 'clarificationTypeId', full.clarificationTypeId); 		           	 
		             return data;
	            },
			}, 
           { mData: "clarificationDescription", className: 'editable', sWidth: '12%' },		
           { mData: "priorityId", className: 'editable', sWidth: '10%', editField: "priorityId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorClarificationList, 'priorityId', full.priorityId); 		           	 
		             return data;
	            },
			},
			{ mData: "planExpectedValue", className: 'editable', sWidth: '12%' },
			{ mData: "workflowStatusId", className: 'editable', sWidth: '10%', editField: "workflowStatusId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorClarificationList, 'workflowStatusId', full.workflowStatusId); 		           	 
		             return data;
	            },
			},
			{ mData: "resolution", className: 'editable', sWidth: '10%', editField: "resolution",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorClarificationList, 'resolution', full.resolution); 		           	 
		             return data;
	            },
			},
			{ mData: "ownerId", className: 'editable', sWidth: '10%', editField: "ownerId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorClarificationList, 'ownerId', full.ownerId); 		           	 
		             return data;
	            },
			},
			{ mData: "raisedById", className: 'editable', sWidth: '10%', editField: "raisedById",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorClarificationList, 'raisedById', full.raisedById); 		           	 
		             return data;
	            },
			},
			{ mData: "raisedDate", className: 'editable', sWidth: '12%' },
			{ mData: "plannedEndDate", className: 'editable', sWidth: '12%' },
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
 	       				'<img src="css/images/list_metro.png" class="changeRequestImg" title="Add/Edit Change Request" />'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       				'<img src="css/images/list_metro.png" class="transactionImg" title="Add/Edit Transaction" />'+
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
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       			'<div><img src="css/images/attachment.png" class="attachmentImg" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+full.attachmentCount+']</span></div>'+
           		'</div>');	
           		 return img;
            	}
            },	
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorClarificationList-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#clarificationList_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorClarificationList.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#clarificationList_dataTable tbody').on('click', 'td .changeRequestImg', function () {
		var tr = $(this).closest('tr');
    	var row = clarificationListDT_oTable.DataTable().row(tr);
    	clarfnTrackerId = row.data().clarificationTrackerId;
    	if(referenceProductId == 0){
			callAlert("Please select a product");
			return;
		}
		$('#changeRequestDT_Child_Container').modal();
		$(document).off('focusin.modal');
    	clarificationListDataTable(referenceTestfactoryId, referenceProductId, referenceEntityTypeId, referenceEntityInstanceId, "ChildTable1");
	});
	
	$('#clarificationList_dataTable tbody').on('click', 'td .transactionImg', function () {
		var tr = $(this).closest('tr');
    	var row = clarificationListDT_oTable.DataTable().row(tr);
    	clarfnTrackerId = row.data().clarificationTrackerId;
		$('#transactionDT_Child_Container').modal();
		$(document).off('focusin.modal');
    	clarificationListDataTable(referenceTestfactoryId, referenceProductId, referenceEntityTypeId, referenceEntityInstanceId, "ChildTable2");
	});
	
	$('#clarificationList_dataTable tbody').on('click', 'td .attachmentImg', function () {
		var tr = $(this).closest('tr');
    	var row = clarificationListDT_oTable.DataTable().row(tr);
		if(referenceProductId == 0){
			callAlert("Please select a product");
			return;
		}
		isViewAttachment = false;
		var jsonObj={"Title":"Attachments for Clarification",			          
			"SubTitle": 'Clarification : ['+row.data().clarificationTrackerId+'] '+row.data().clarificationTitle,
			"listURL": 'attachment.for.entity.or.instance.list?productId='+referenceProductId+'&entityTypeId=31&entityInstanceId='+row.data().clarificationTrackerId,
			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+referenceProductId+'&entityTypeId=31&entityInstanceId='+row.data().clarificationTrackerId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
			"updateURL": 'update.attachment.for.entity.or.instance',
			"deleteURL": 'delete.attachment.for.entity.or.instance',
			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=31',
			"multipleUpload":true,
		};	 
		Attachments.init(jsonObj);
	});
	
	$('#clarificationList_dataTable tbody').on('click', 'td .auditHistoryImg', function () {
		var tr = $(this).closest('tr');
    	var row = clarificationListDT_oTable.DataTable().row(tr);
    	listGenericAuditHistory(row.data().clarificationTrackerId,"ClarificationTracker","clarificationTrackerAudit");
	});
	
	$('#clarificationList_dataTable').on( 'change', 'input.editorClarificationList-active', function () {
		editorClarificationList
            .edit( $(this).closest('tr'), false )
            .set( 'readyState', $(this).prop( 'checked' ) ? 1 : 0 )
            .submit();
	});
	
	$("#clarificationList_dataTable_length").css('margin-top','8px');
	$("#clarificationList_dataTable_length").css('padding-left','35px');		
	
	clarificationListDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutClarificationListDT='';
function reInitializeClarificationListDT(){
	clearTimeoutClarificationListDT = setTimeout(function(){				
		clarificationListDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutClarificationListDT);
	},200);
}

function fullScreenHandlerDTClarificationList(){
	
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
		$('#testFacMode').css('max-height',displaytestFaceModeResponsive(window.innerWidth));
		
		clarificationListDTFullScreenHandler(true);
		reInitializeClarificationListDT();
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
		$('#testFacMode').css('max-height','');
		
		reInitializeClarificationListDT();				
		clarificationListDTFullScreenHandler(false);			
	}
}

function clarificationListDTFullScreenHandler(flag){
	if(flag){
		reInitializeClarificationListDT();
		$("#clarificationList_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
	}else{
		reInitializeClarificationListDT();
		$("#clarificationList_dataTable_wrapper .dataTables_scrollBody").css('max-height','450px');
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
//END: ConvertDataTable - ClarificationList - Tab1

//BEGIN: ConvertDataTable - ChangeRequest - ChildTable1
function changeRequestDataTableHeader(){
	var childDivString ='<table id="changeRequest_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>Name</th>'+
			'<th>Description</th>'+
			'<th>Type</th>'+
			'<th>Priority</th>'+
			'<th>Planned Value</th>'+
			'<th>Status</th>'+
			'<th>Owner</th>'+
			'<th>Raised Date</th>'+
			'<th>Attachments</th>'+
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
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function changeRequestDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForChangeRequest").children().length>0) {
			$("#dataTableContainerForChangeRequest").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = changeRequestDataTableHeader(); 			 
	$("#dataTableContainerForChangeRequest").append(childDivString);
	
	editorChangeRequest = new $.fn.dataTable.Editor( {
	    "table": "#changeRequest_dataTable",
		ajax: "changerequests.add",
		ajaxUrl: "changerequests.update",
		idSrc:  "changeRequestId",
		i18n: {
	        create: {
	            title:  "Create a new Change Request",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "ID",
            name: "changeRequestId",
            "type": "hidden",
         },{
             label: "Change Request Name",
             name: "changeRequestName",
             "type": "hidden",
          },{
              label: "Description",
              name: "description",
              "type": "hidden",
         },{
             label: "Change Request Type",
             name: "changeRequestType",
             options: optionsResultArr[0],
             "type":"select"
         },{
             label: "Priority",
             name: "priorityId",
             options: optionsResultArr[1],
             "type":"select"
        },{
            label: "Planned Value",
            name: "planExpectedValue",
        },{
            label: "Status",
            name: "statusCategoryId",
            options: optionsResultArr[2],
            "type":"select"
        },{
            label: "Owner",
            name: "ownerId",
            options: optionsResultArr[3],
            "type":"select"
        },{
            label: "Raised Date",
            name: "raisedDate", 
        },{
            label: "Expected End Date",
            name: "plannedEndDate", 
        }        
    ]
	});
	
	changeRequestDT_oTable = $("#changeRequest_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "90%",
	       "sScrollXInner": "100%",
	       "scrollY":"280px",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [3,4,6,7,8,9,10]; // search column TextBox Invisible Column position
	     		  $('#changeRequest_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeChangeRequestDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorChangeRequest },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Change Request',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Change Request',
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
           { mData: "changeRequestId", className: 'disableEditInline', sWidth: '15%' },	
           { mData: "changeRequestName", className: 'disableEditInline', sWidth: '15%' },	
           { mData: "description", className: 'disableEditInline', sWidth: '12%' },
           { mData: "changeRequestType", className: 'editable', sWidth: '10%', editField: "changeRequestType",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorChangeRequest, 'changeRequestType', full.changeRequestType); 		           	 
		             return data;
	            },
			}, 
           { mData: "priorityId", className: 'editable', sWidth: '10%', editField: "priorityId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorChangeRequest, 'priorityId', full.priorityId); 		           	 
		             return data;
	            },
			},
			{ mData: "planExpectedValue", className: 'editable', sWidth: '12%' },
			{ mData: "statusCategoryId", className: 'editable', sWidth: '10%', editField: "statusCategoryId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorChangeRequest, 'statusCategoryId', full.statusCategoryId); 		           	 
		             return data;
	            },
			},
			{ mData: "ownerId", className: 'editable', sWidth: '10%', editField: "ownerId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorChangeRequest, 'ownerId', full.ownerId); 		           	 
		             return data;
	            },
			},
			{ mData: "raisedDate", className: 'disableEditInline', sWidth: '12%' },
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       			'<div><img src="css/images/attachment.png" class="attachmentImg" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+full.attachmentCount+']</span></div>'+
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
    	   //$('input.editorChangeRequest-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#changeRequest_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorChangeRequest.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#changeRequest_dataTable tbody').on('click', 'td .attachmentImg', function () {
		var tr = $(this).closest('tr');
    	var row = changeRequestDT_oTable.DataTable().row(tr);
		isViewAttachment = false;
		var jsonObj={"Title":"Attachments for ChangeRequest",			          
			"SubTitle": 'ChangeRequest : ['+row.data().changeRequestId+'] '+row.data().changeRequestName,
   			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=42&entityInstanceId='+row.data().changeRequestId,
   			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=42&entityInstanceId='+row.data().changeRequestId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
   			"updateURL": 'update.attachment.for.entity.or.instance',		
   			"deleteURL": 'delete.attachment.for.entity.or.instance',
   			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=42',
   			"multipleUpload":true,
   		};	 
   		Attachments.init(jsonObj);
	});
	
	$('#changeRequest_dataTable tbody').on('click', 'td .auditHistoryImg', function () {
		var tr = $(this).closest('tr');
    	var row = changeRequestDT_oTable.DataTable().row(tr);
    	$('#div_SingleDataTableSummary').css({'z-index':'10062','top':'2%'});
    	listGenericAuditHistory(row.data().changeRequestId,"ChangeRequest","changeRequestAudit");
	});
	
	$('#changeRequest_dataTable').on( 'change', 'input.editorChangeRequest-active', function () {
		editorChangeRequest
            .edit( $(this).closest('tr'), false )
            .set( 'readyState', $(this).prop( 'checked' ) ? 1 : 0 )
            .submit();
	});
	
	$("#changeRequest_dataTable_length").css('margin-top','8px');
	$("#changeRequest_dataTable_length").css('padding-left','35px');		
	
	changeRequestDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutChangeRequestDT='';
function reInitializeChangeRequestDT(){
	clearTimeoutChangeRequestDT = setTimeout(function(){				
		changeRequestDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutChangeRequestDT);
	},200);
}
//END: ConvertDataTable - ChangeRequest - ChildTable1

//BEGIN: ConvertDataTable - Transaction - ChildTable2
function transactionDataTableHeader(){
	var childDivString ='<table id="transaction_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Transaction Name</th>'+
			'<th>Reply To</th>'+
			'<th>Comment</th>'+
			'<th>Commented D</th>'+
			'<th>Comment Weightage</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
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
function transactionDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForTransaction").children().length>0) {
			$("#dataTableContainerForTransaction").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = transactionDataTableHeader(); 			 
	$("#dataTableContainerForTransaction").append(childDivString);
	
	editorTransaction = new $.fn.dataTable.Editor( {
	    "table": "#transaction_dataTable",
		ajax: "transaction.clarification.add?clarificationTrackerId="+clarfnTrackerId,
		ajaxUrl: "transaction.clarification.update",
		idSrc:  "transactionId",
		i18n: {
	        create: {
	            title:  "Create a new Transaction Clarification",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "ID",
            name: "transactionId",
            "type": "hidden",
         },{
             label: "Transaction Name*",
             name: "transactionName",
         },{
             label: "Reply To",
             name: "replyTo",
             options: optionsResultArr[0],
             "type":"select"
         },{
            label: "Comment",
            name: "comment",
        },{
            label: "Commented Date",
            name: "commentedDate", 
        },{
            label: "Comment Weightage",
            name: "commentWeightage", 
        }        
    ]
	});
	
	transactionDT_oTable = $("#transaction_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "90%",
	       "sScrollXInner": "100%",
	       "scrollY":"280px",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [1]; // search column TextBox Invisible Column position
	     		  $('#transaction_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeTransactionDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorTransaction },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Transaction Clarification',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Change Request',
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
           { mData: "transactionName", className: 'disableEditInline', sWidth: '25%' },	
           { mData: "replyTo", className: 'editable', sWidth: '20%', editField: "replyTo",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorTransaction, 'replyTo', full.replyTo); 		           	 
		             return data;
	            },
			}, 
			{ mData: "comment", className: 'editable', sWidth: '12%' },
			{ mData: "commentedDate", className: 'disableEditInline', sWidth: '12%' },
			{ mData: "commentWeightage", className: 'disableEditInline', sWidth: '12%' },
       ],       
       rowCallback: function ( row, data ) {
    	   //$('input.editorTransaction-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#transaction_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorTransaction.inline( this, {
            submitOnBlur: true
        } );
	});
	
	editorEnvironmentGroups.on( 'preSubmit', function ( e, o, action ) {
        if ( action !== 'remove' ) {
            var transactionName = this.field( 'transactionName' );
            if ( ! transactionName.isMultiValue() ) {
                if ( transactionName.val() ) {
                	var str = transactionName.val();
                	if(/^[a-zA-Z0-9-_]*$/.test(str) == false) {
                		transactionName.error( 'Please enter Valid Transaction name' );
                	}
                }else{
                	transactionName.error( 'Please enter Transaction name' );
            	}
            }
            // If any error was reported, cancel the submission so it can be corrected
            if ( this.inError() ) {
                return false;
            }
        }
    } );
	
	$("#transaction_dataTable_length").css('margin-top','8px');
	$("#transaction_dataTable_length").css('padding-left','35px');		
	
	transactionDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutTransactionDT='';
function reInitializeTransactionDT(){
	clearTimeoutTransactionDT = setTimeout(function(){				
		transactionDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutTransactionDT);
	},200);
}
//END: ConvertDataTable - Transaction - ChildTable2

//BEGIN: ConvertDataTable - ChangeRequestList - Tab2
function changeRequestListDataTableHeader(){
	var childDivString ='<table id="changeRequestList_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>ID</th>'+
			'<th>Source Entity</th>'+
			'<th>Source Entity Name</th>'+
			'<th>Change Request</th>'+
			'<th>Description</th>'+
			'<th>Owner</th>'+
			'<th>Priority</th>'+
			'<th>Planned Value</th>'+
			'<th>Status</th>'+
			'<th>Active</th>'+
			'<th>Attachments</th>'+
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
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function changeRequestListDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForChangeRequestList").children().length>0) {
			$("#dataTableContainerForChangeRequestList").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = changeRequestListDataTableHeader(); 			 
	$("#dataTableContainerForChangeRequestList").append(childDivString);
	
	editorChangeRequestList = new $.fn.dataTable.Editor( {
	    "table": "#changeRequestList_dataTable",
		ajax: "changerequests.add",
		ajaxUrl: "changerequests.update",
		idSrc:  "changeRequestId",
		i18n: {
	        create: {
	            title:  "Create a new Change Request List",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "ID",
            name: "changeRequestId",
            "type": "hidden",
         },{
             label: "Source Entity",
             name: "entityType",
             "type": "hidden",
          },{
              label: "Source Entity Name",
              name: "entityInstanceName",
              "type": "hidden",
         },{
             label: "Change Request",
             name: "changeRequestName",
         },{
             label: "Description",
             name: "description",
         },{
             label: "Owner",
             name: "ownerId",
             options: optionsResultArr[0],
             "type":"select"
         },{
             label: "Priority",
             name: "priorityId",
             options: optionsResultArr[1],
             "type":"select"
        },{
            label: "Planned Value",
            name: "planExpectedValue",
        },{
            label: "Status",
            name: "statusCategoryId",
            options: optionsResultArr[2],
            "type":"select",
            "type": "hidden",
        }        
    ]
	});
	
	changeRequestListDT_oTable = $("#changeRequestList_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "90%",
	       "sScrollXInner": "100%",
	       "scrollY":"280px",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [5,6,8,9,10,11]; // search column TextBox Invisible Column position
	     		  $('#changeRequestList_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeChangeRequestListDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorChangeRequestList },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Change Request List',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Change Request List',
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
           { mData: "changeRequestId", className: 'disableEditInline', sWidth: '15%' },	
           { mData: "entityType", className: 'disableEditInline', sWidth: '15%' },	
           { mData: "entityInstanceName", className: 'disableEditInline', sWidth: '15%' },	
           { mData: "changeRequestName", className: 'disableEditInline', sWidth: '15%' },	
           { mData: "description", className: 'disableEditInline', sWidth: '12%' },
			{ mData: "ownerId", className: 'editable', sWidth: '10%', editField: "ownerId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorChangeRequestList, 'ownerId', full.ownerId); 		           	 
		             return data;
	            },
			},
           { mData: "priorityId", className: 'editable', sWidth: '10%', editField: "priorityId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorChangeRequestList, 'priorityId', full.priorityId); 		           	 
		             return data;
	            },
			},
			{ mData: "planExpectedValue", className: 'editable', sWidth: '12%' },
			{ mData: "statusCategoryId", className: 'editable', sWidth: '10%', editField: "statusCategoryId",
	       		mRender: function (data, type, full) {
		           	 data = optionsValueHandler(editorChangeRequestList, 'statusCategoryId', full.statusCategoryId); 		           	 
		             return data;
	            },
			},
            { mData: null,
              mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editorChangeRequestList-active">';
                    }
                    return data;
                },
                className: "dt-body-center"
            },
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       			'<div><img src="css/images/attachment.png" class="attachmentImg" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+full.attachmentCount+']</span></div>'+
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
    	   $('input.editorChangeRequestList-active', row).prop( 'checked', data.isActive == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#changeRequestList_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorChangeRequestList.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#changeRequestList_dataTable tbody').on('click', 'td .attachmentImg', function () {
		var tr = $(this).closest('tr');
    	var row = changeRequestListDT_oTable.DataTable().row(tr);
		isViewAttachment = false;
		var jsonObj={"Title":"Attachments for ChangeRequestList",			          
			"SubTitle": 'ChangeRequestList : ['+row.data().changeRequestId+'] '+row.data().changeRequestName,
   			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=42&entityInstanceId='+row.data().changeRequestId,
   			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=42&entityInstanceId='+row.data().changeRequestId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
   			"updateURL": 'update.attachment.for.entity.or.instance',		
   			"deleteURL": 'delete.attachment.for.entity.or.instance',
   			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=42',
   			"multipleUpload":true,
   		};	 
   		Attachments.init(jsonObj);
	});
	
	$('#changeRequestList_dataTable tbody').on('click', 'td .auditHistoryImg', function () {
		var tr = $(this).closest('tr');
    	var row = changeRequestListDT_oTable.DataTable().row(tr);
    	$('#div_SingleDataTableSummary').css({'z-index':'10062','top':'2%'});
    	listGenericAuditHistory(row.data().changeRequestId,"ChangeRequest","changeRequestAudit");
	});
	
	$('#changeRequestList_dataTable').on( 'change', 'input.editorChangeRequestList-active', function () {
		editorChangeRequestList
            .edit( $(this).closest('tr'), false )
            .set( 'isActive', $(this).prop( 'checked' ) ? 1 : 0 )
            .submit();
	});
	
	$("#changeRequestList_dataTable_length").css('margin-top','8px');
	$("#changeRequestList_dataTable_length").css('padding-left','35px');		
	
	changeRequestListDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutChangeRequestListDT='';
function reInitializeChangeRequestListDT(){
	clearTimeoutChangeRequestListDT = setTimeout(function(){				
		changeRequestListDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutChangeRequestListDT);
	},200);
}
//END: ConvertDataTable - ChangeRequestList - Tab2

//BEGIN: ConvertDataTable - Attachments - Tab3
function attachmentsDataTableHeader(){
	var childDivString ='<table id="attachments_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Name</th>'+
			'<th>Description</th>'+
			'<th>Attachment Type</th>'+
			'<th>File Extension</th>'+
			'<th>File Size</th>'+
			'<th>Uploaded By</th>'+
			'<th>Uploaded Date</th>'+
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
function attachmentsDT_Container(jsonObj){
	if(referenceProductId == 'undefined' || referenceProductId == 0){
		callAlert("Please select a product");
	}
	try{
		if ($("#dataTableContainerForAttachments").children().length>0) {
			$("#dataTableContainerForAttachments").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = attachmentsDataTableHeader(); 			 
	$("#dataTableContainerForAttachments").append(childDivString);
	
	editorAttachments = new $.fn.dataTable.Editor( {
	    "table": "#attachments_dataTable",
		ajax: "",
		ajaxUrl: "",
		idSrc:  "attachmentId",
		i18n: {
	        create: {
	            title:  "Create a new Attachment",
	            submit: "Create",
	        }
	    },
		fields: [
			{
	        	label: "attachmentType",
	            name: "attachmentType",
	            options: optionsResultArr[0],
	            "type":"select",
	        }
		]
	});
	
	attachmentsDT_oTable = $("#attachments_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "90%",
	       "sScrollXInner": "100%",
	       "scrollY":"280px",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [7]; // search column TextBox Invisible Column position
	     		  $('#attachments_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeAttachmentsDT();
			   },  
		   buttons: [
						//{ extend: "create", editor: editorAttachments },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Attachments',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Attachment',
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
            { mData: "attachmentName", className: 'disableEditInline', sWidth: '25%',
            	mRender: function (data, type, full) {
        		   	data = ("<a style='color: #0000FF'; href=javascript:loadAttachmentsPopupEvidence('"+full.attachmentId+"');>"+data+"</a>");
		            return data;
	            },
            },            
            { mData: "description", className: 'disableEditInline', sWidth: '25%' },	
            { mData: "attachmentType", className: 'disableEditInline', sWidth: '20%' }, 
			{ mData: "attributeFileExtension", className: 'disableEditInline', sWidth: '12%' },
			{ mData: "attributeFileSize", className: 'disableEditInline', sWidth: '12%' },
			{ mData: "createrName", className: 'disableEditInline', sWidth: '12%' },
			{ mData: "uploadedDate", className: 'disableEditInline', sWidth: '12%' },
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
    	   //$('input.editorAttachments-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#attachments_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorAttachments.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#attachments_dataTable tbody').on('click', 'td .auditHistoryImg', function () {
		var tr = $(this).closest('tr');
    	var row = attachmentsDT_oTable.DataTable().row(tr);
    	listGenericAuditHistory(row.data().attachmentId,"Attachment","attachmentAudit");
	});
	
	editorAttachments.on( 'preSubmit', function ( e, o, action ) {
        if ( action !== 'remove' ) {
            var attachmentsName = this.field( 'attachmentsName' );
            if ( ! attachmentsName.isMultiValue() ) {
                if ( attachmentsName.val() ) {
                	var str = attachmentsName.val();
                	if(/^[a-zA-Z0-9-_]*$/.test(str) == false) {
                		attachmentsName.error( 'Please enter Valid Attachments name' );
                	}
                }else{
                	attachmentsName.error( 'Please enter Attachments name' );
            	}
            }
            // If any error was reported, cancel the submission so it can be corrected
            if ( this.inError() ) {
                return false;
            }
        }
    } );
	
	$("#attachments_dataTable_length").css('margin-top','8px');
	$("#attachments_dataTable_length").css('padding-left','35px');		
	
	attachmentsDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutAttachmentsDT='';
function reInitializeAttachmentsDT(){
	clearTimeoutAttachmentsDT = setTimeout(function(){				
		attachmentsDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutAttachmentsDT);
	},200);
}
//END: ConvertDataTable - Attachments - Tab3

//Delete JTable
/*function callJtable(testFactoryId,productId,entityTypeId,entityInstanceId){
	var urlToGetClarificationsOfSpecifiedEngagementAndProduct = 'list.clarificationtracker.by.engagementAndProductId?testFactoryId='+testFactoryId+'&productId='+productId;
	try{
		if ($('#jTableClarifications').length>0) {
			 $('#jTableClarifications').jtable('destroy'); 
		}
	} catch(e) {}
	 $('#jTableClarifications').jtable({
         
			title: 'Add/Edit Clarification', 
		 	editinline:{enable:true},
		 	editInlineRowRequestModeDependsOn : true,
		 	paging : true, //Enable paging								
			pageSize : 10,
			actions: { 
				listAction: urlToGetClarificationsOfSpecifiedEngagementAndProduct,
				createAction : 'add.clarificationtracker.by.activity',	    									
				editinlineAction : 'update.clarificationtracker.by.activity',
				deleteAction : 'delete.clarificationtracker',
				}, 
				recordsLoaded: function(event, data) {
          		        		$(".jtable-edit-command-button").prop("disabled", true);
          		         	},
          		recordUpdated:function(event, data){
          			$('#jTableClarifications').find('.jtable-main-container .reload').trigger('click');
                },	fields: {    
           					 	productId: { 
        							type: 'hidden', 
        							create: true, 
        							edit:true,
        							defaultValue: productId
        						},
        						testFactoryId: {
        							type: 'hidden', 
        							create: true, 
        							edit:true,
        							defaultValue: testFactoryId
        						},
        						 entityInstanceId: {
        							type: 'hidden',
        							create: true, 
        							edit:true,
        							list:true,
           							defaultValue: entityInstanceId
        						}, 
        						entityTypeId: {
        							type: 'hidden', 
        							create: true, 
        							edit:true,
        							list:true,
        							defaultValue: entityTypeId
        						},
        						entityTypeId2: {
        							type: 'hidden',
    								create: true, 
    				                edit: false, 
            						list: false,
    								defaultValue: clarificationTypeId
        						},
           						clarificationTrackerId: {
           							key: true, 
           							title : 'ID',
               						create: false, 
    				                edit: false, 
               						list: true
               					},entityType : {
    								title : 'Source Entity',
    								list : true,				    										
    								create : false,
    								edit : false,
    								width : "20%"
    								
    							}, entityInstanceName : {
    								title : 'Source Entity Name',
    								list : true,				    										
    								create : false,
    								edit : false,
    								width : "20%"
    							},clarificationTitle : {
    								title : 'Title',
    								inputTitle : 'Clarification Title <font color="#efd125" size="4px">*</font>',
    								list : true,				    										
    								create : true,
    								edit : true,
    								width : "20%"
    							},
    							 clarificationTypeId : {
    								title : 'Type',	
    								inputTitle : 'Clarification Type <font color="#efd125" size="4px">*</font>',
    								list : true,
    								create : true,
    								edit : true,
    								width : "20%",
    								options : 'list.clarification.type.option'							
    							}, 
    							clarificationDescription:{
    					             title: 'Description',
    					             list:true,
    					             type: 'textarea',    
    					             create:true,
    					             edit:true,
    					             width:"20%"
    					             },
    								
               					 priorityId : {
    								title : 'Priority',				    										
    								list : true,
    								create : true,
    								edit : true,
    								width : "20%",
    								options : 'administration.executionPriorityList'
    							},
    							
    							planExpectedValue: {
    								title : 'Planned Value',										
    								inputTitle : 'Planned Value <font color="#efd125" size="4px"></font>',
    								edit : true,
    								list : true,
    								create : true,    							
    								width : "20%",
    							},
    							workflowStatusId : {
    								title : 'Status',
    								create : false,
    								list : true,
    								edit : true,
    								width : "20%",
    								options: function(data){
    									return 'clarification.status.option.list?entityTypeId=31';
    								}
    							},  
								 resolution : {
    								title : 'Resolution',
    								create : false,
    								list : true,
    								edit : true,
    								width : "20%",
    								 dependsOn:'workflowStatusId ',									
    									options:function(data){
    										return 'clarification.resolution.option.list?parentStatusId='+data.dependedValues.workflowStatusId;			
    					     		}	 				
    							}, 
								ownerId : {
    								title : 'Owner',				    									
    								list : true,
    								width : "20%",
    								create : true,
    								edit : true,
    								options : 'common.allusers.list.by.resourcepool.id'
    							},  
    							raisedById : {
    								title : 'Raised By',
    								inputTitle : 'Raised By <font color="#efd125" size="4px">*</font>',
    								list : true,
    								create : true,
    								width : "20%",
    								edit : true,
    								defaultValue:defaultRaisedBy,
    								options : 'common.allusers.list.by.resourcepool.id'
    							},
    							raisedDate : {
    								title : 'Raised Date',
    								inputTitle : 'Raised Date <font color="#efd125" size="4px">*</font>',
    								edit : true,
    								list : true,
    								create : true,
    								type : 'date',
    								width : "20%",
    								defaultValue : (new Date().getMonth()+1)+"/"+new Date().getDate()+"/"+new Date().getFullYear()
    							},   
    							plannedStartDate : {
    								title : 'Planned Start Date',										
    								inputTitle : 'Planned Start Date <font color="#efd125" size="4px"></font>',
    								edit : false,
    								create : false,
    								list : false,
    								type : 'date',
    								width : "20%"
    							},actualStartDate : {
    								title : 'Actual Start Date',
    								create : false,
    								edit : false,
    								list : false,
    								type : 'date',
    								width : "20%"
    							},
    							actualEndDate : {
    								title : 'Actual End Date',
    								create : false,
    								edit : false,
    								list : false,
    								type : 'date',
    								width : "20%"
    							},plannedEndDate : {
    								title : 'Expected End Date',										
    								inputTitle : 'Expected End Date <font color="#efd125" size="4px"></font>',
    								edit : true,
    								list : true,
    								create : true,
    								type : 'date',
    								width : "20%",
    								defaultValue : (new Date().getMonth()+1)+"/"+new Date().getDate()+"/"+new Date().getFullYear()
    							},			
    							
    							changeRequest:{
				                	title: 'Change Request',
				                	width: "5%",
				                	edit: true,
				                	create: false,
				                	display: function (activityData) { 
				                   		//Create an image that will be used to open child table 
				                   			var $img = $('<img src="css/images/list_metro.png" title="Add/Edit Change Request" />'); 											
				                   			//Open child table when user clicks the image 
				                   			$img.click(function () {
				                   				
				                   				if(referenceProductId == 0){
							       					callAlert("Please select a product");
							       					return;
							       				}               				
				                   				// ----- Closing child table on the same icon click -----
				                   			    closeChildTableFlag = closeJtableTableChildContainer($(this), $('#jTableClarifications'));
				                   				if(closeChildTableFlag){
				                   					return;
				                   				} 		                   						
				                   				
				                   				$('#jTableClarifications').jtable('openChildTable', 
				                   				$img.closest('tr'), 
				                   					{						                   	      	   
				                   						title: 'Add/Edit Change Request',							                   	      
				                   						editinline:{enable:true},
				                   						recordsLoaded: function(event, data) {
				                   		        		$(".jtable-edit-command-button").prop("disabled", true);
				                   		         	},
				                   					actions: { 
				                   						listAction: 'list.changerequests.by.id?entityType1='+clarificationTypeId+'&entityType2='+changeRequestTypeId+'&entityInstanceId1='+activityData.record.clarificationTrackerId,
				                   						createAction : 'changerequests.add',
				    									editinlineAction : 'changerequests.update',					    								
				                   						}, 
				                   						recordsLoaded: function(event, data) {
				                   						},
				                   					fields: { 													
				                   						changeRequestId: { 					                   							
				                   							title : 'ID',
				                   							key: true, 
				                       						create: false, 
				        					                edit: false, 
				                       						list: true,
				                       					},
														entityType1:{
															type: 'hidden',
															create: true, 
				        					                edit: true, 
				                       						list: true,
															defaultValue: clarificationTypeId
														},
														entityType2:{
															type: 'hidden',
															create: true, 
				        					                edit: false, 
				                       						list: false,
															defaultValue: changeRequestTypeId
														},
														entityInstance1:{
															type: 'hidden',
															create: true, 
				        					                edit: false, 
				                       						list: false,
															defaultValue: activityData.record.clarificationTrackerId,
														},		
														productId : {
				    										title : 'Product',					    										
				    										list : false,
				    										create : true,
				    										edit : false,
															options: 'administration.product.list.options.by.ids?testFactoryId='+testFactoryId+'&productId='+productId						    												
				    									},
				                       					changeRequestName: { 					                   							
				                   							title : 'Name',
				                   							inputTitle : 'Change Request Name <font color="#efd125" size="4px">*</font>',
				                   							key: true, 
				                       						create: true, 
				        					                edit: true, 
				                       						list: true,
															defaultValue: activityData.record.clarificationTitle,
				                       					},
				                       					description: {
					                       					 title: 'Description',
				    							             list:true,
				    							             type: 'textarea',    
				    							             create:true,
				    							             edit:false,
				    							             width:"20%",
				    							             defaultValue: activityData.record.clarificationDescription,
				    							             },
				    							             changeRequestType: {
						                       					 title: 'Type',
					    							             list:true,
					    							             create:true,
					    							             edit:false,
					    							             width:"20%",
					    							          	 options: 'changerequest.type.option'
					    							             }, 
				                       					priorityId : {
				    										title : 'Priority',					    										
				    										list : true,
				    										create : true,
				    										edit : true,
				    										width : "20%",
				    										options : 'administration.executionPriorityList'											
				    									},
														planExpectedValue: {
															title : 'Planned Value',										
															inputTitle : 'Planned Value <font color="#efd125" size="4px"></font>',
															edit : true,
															list : true,
															create : true,    							
															width : "20%",
															defaultValue: activityData.record.planExpectedValue,
														},
				    									statusCategoryId : {
				    										title : 'Status',
				    										create:true,
				    										list : true,
				    										edit :true,				    									
				    										options: 'status.category.option.list'
				    									},
				    									ownerId : {
				    										title : 'Owner',					    										
				    										list : true,
				    										create : true,
				    										edit : true,
				    										defaultValue:defaultuserId,
				    										options : function(data) {
				    											if(productId != null){
				    												return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
				    											}
				    										}															
				    									},   
				    									raisedDate : {
				    										title : 'Raised Date',
				    										inputTitle : 'Raised Date <font color="#efd125" size="4px">*</font>',
				    										edit : false,
				    										list : true,
				    										create : true,
				    										type : 'date',
				    										width : "20%",
															defaultValue : (new Date().getMonth()+1)+"/"+new Date().getDate()+"/"+new Date().getFullYear(),
				    									},attachment: { 
				    										title: '', 
				    										list: true,
				    										create: false,
				    										width: "5%",
				    										display:function (data) {	        		
				    							           		//Create an image that will be used to open child table 
				    											var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>'); 
				    							       			$img.click(function () {
				    							       				isViewAttachment = false;
				    							       				var jsonObj={"Title":"Attachments for ChangeRequest",			          
				    							       					"SubTitle": 'ChangeRequest : ['+data.record.changeRequestId+'] '+data.record.changeRequestName,
				    							    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=42&entityInstanceId='+data.record.changeRequestId,
				    							    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=42&entityInstanceId='+data.record.changeRequestId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
				    							    	    			"updateURL": 'update.attachment.for.entity.or.instance',		
				    							    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
				    							    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=42',
				    							    	    			"multipleUpload":true,
				    							    	    		};	 
				    								        		Attachments.init(jsonObj);
				    							       		  });
				    							       			return $img;
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
																	listGenericAuditHistory(data.record.changeRequestId,"ChangeRequest","changeRequestAudit");
																});
																return $img;
															}
														},				    									
				                   					},					                   	
				                   					 formSubmitting: function (event, data) {
				  							      	    data.form.find('input[name="changeRequestName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[maxSize[100]');
				  							      	    data.form.find('input[name="raisedDate"]').addClass('validate[required]');
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
				                	}
				                },       
				                
				                transaction:{
				                	title: 'Transaction',
				                	width: "5%",
				                	edit: true,
				                	create: false,
				                	display: function (activityData) { 
				                   		//Create an image that will be used to open child table 
				                   			var $img = $('<img src="css/images/list_metro.png" title="Add/Edit Transaction Clarification" />'); 
											
				                   			//Open child table when user clicks the image 
				                   			$img.click(function () {
				                   				// ----- Closing child table on the same icon click -----
				                   			    closeChildTableFlag = closeJtableTableChildContainer($(this), $('#jTableClarifications'));
				                   				if(closeChildTableFlag){
				                   					return;
				                   				} 		                   						
				                   				
				                   				$('#jTableClarifications').jtable('openChildTable', 
				                   				$img.closest('tr'), 
				                   					{						                   	      	   
				                   						title: 'Add/Edit Transaction Clarification',							                   	      
				                   						editinline:{enable:true},
				                   						recordsLoaded: function(event, data) {
				                   		        		$(".jtable-edit-command-button").prop("disabled", true);
				                   		         	},
				                   					actions: { 
				                   						listAction: 'transaction.clarification.list?clarificationTrackerId='+activityData.record.clarificationTrackerId,
				                   						createAction : 'transaction.clarification.add?clarificationTrackerId='+activityData.record.clarificationTrackerId,
				    									editinlineAction : 'transaction.clarification.update',					    								
				                   						}, 
				                   						recordsLoaded: function(event, data) {
				                   						},
				                   					fields: { 													
				                   						transactionId: { 					                   							
				                   							title : 'Transaction Id',
				                   							key: true, 
				                       						create: false, 
				        					                edit: false, 
				                       						list: false,
				                       					},
				                       					transactionName:{
				                       						title: 'Transaction Name',
															inputTitle : 'Transaction Name<font color="#efd125" size="4px">*</font>',
															create: true, 
				        					                edit: true, 
				                       						list: true,
														},
														replyTo: {
					                       					 title: 'Reply To',
				    							             list:true,
				    							             create:true,
				    							             edit:true,
				    							             options:'transaction.clarification.option.list?clarificationTrackerId='+activityData.record.clarificationTrackerId,
				    							             width:"20%",
				    							             }, 
														clarificationTrackerId:{
															type: 'ClarificationId',
															create: false, 
				        					                edit: true, 
				                       						list: false,
														},
														comment: { 					                   							
				                   							title : 'Comment',
				                   							inputTitle : 'Comment<font color="#efd125" size="4px">*</font>',
				                   							key: true, 
				                   						   type: 'textarea',    
				                       						create: true, 
				        					                edit: true, 
				                       						list: true,
				                       					},
				                   					  commentedDate: {
					                       					 title: 'CommentedDate',
				    							             list:true,
				    							             create:false,
				    							             edit:true,
				    							             width:"20%",
				    							          }, 
			    							             commentWeightage: {
					                       				    title: 'Comment Weightage',
					                       				    inputTitle : 'Comment Weightage<font color="#efd125" size="4px"></font>',
				    							             list:true,
				    							             create:true,
				    							             edit:true,
				    							             width:"20%",
				    							          }, 
				                   					},					                   	
				                   					 formSubmitting: function (event, data) {
				  							      	    data.form.find('input[name="comment"]').addClass('validate[required');
				  							      	    data.form.find('input[name="transactionName"]').addClass('validate[required]');
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
											listGenericAuditHistory(data.record.clarificationTrackerId,"ClarificationTracker","clarificationTrackerAudit");
										});
										return $img;
									}
								},attachment: { 
									title: '', 
									list: true,
									create :false,
									width: "5%",
									display:function (data) {	        		
						           		//Create an image that will be used to open child table 
										var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>'); 
						       			$img.click(function () {
						       				if(referenceProductId == 0){
						       					callAlert("Please select a product");
						       					return;
						       				}
						       				isViewAttachment = false;
						       				var jsonObj={"Title":"Attachments for Clarification",			          
						       					"SubTitle": 'Clarification : ['+data.record.clarificationTrackerId+'] '+data.record.clarificationTitle,
						    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+referenceProductId+'&entityTypeId=31&entityInstanceId='+data.record.clarificationTrackerId,
						    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+referenceProductId+'&entityTypeId=31&entityInstanceId='+data.record.clarificationTrackerId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
						    	    			"updateURL": 'update.attachment.for.entity.or.instance',
						    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
						    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=31',
						    	    			"multipleUpload":true,
						    	    		};	 
							        		Attachments.init(jsonObj);
						       		  });
						       			return $img;
						        	}				
								},
    						},
    						formSubmitting: function (event, data) {
					      	    data.form.find('input[name="clarificationTitle"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[maxSize[50]]');
					            data.form.find('input[name="raisedDate"]').addClass('validate[required]');					  							           
					            data.form.validationEngine();
					            return data.form.validationEngine('validate');
					          },   
                     //Dispose validation logic when form is closed
                     formClosed: function (event, data) {
                        data.form.validationEngine('hide');
                        data.form.validationEngine('detach');
                    },
		});
	 $('#jTableClarifications').jtable('load'); 	 
}

function listChangeRequestsBasedOnTestFactIdOrProductId(testFactoryId,productId,entityTypeId,entityInstanceId){
	var urlToGetChangeRequestsOfSpecifiedEngagementAndProduct = 'list.changeRequest.by.engagementAndProductId?testFactoryId='+testFactoryId+'&productId='+productId;
	try{
		if ($('#jTableChangeRequestsList').length>0) {
			 $('#jTableChangeRequestsList').jtable('destroy'); 
		}
		}catch(e){}
		//init jTable
		 $('#jTableChangeRequestsList').jtable({
	         title: 'Change Requests',
	         selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: 10, //Set page size (default: 10)
			 editinline:{enable:true},	
	        
	          actions: {
	             listAction: urlToGetChangeRequestsOfSpecifiedEngagementAndProduct,
	             createAction: 'changerequests.add',
	             editinlineAction : 'changerequests.update', 
	         }, 
	        fields: {
	        		productId : {
						title : 'Product',					    										
						list : false,
						create : false,
						edit : false,
						options: 'administration.product.list.options.by.ids?testFactoryId='+testFactoryId+'&productId='+productId						    												
					},
					changeRequestId: { 
		   				key: true,
		   				title: 'ID',
		   				create: false, 
		   				edit: false, 
		   				list: true ,
		   				},
					entityType: {
						title: 'Source Entity' ,
		       			type: 'textarea', 
		     		  	width: "20%",  
		     		  	create: false, 
		   				edit: false, 
		     	  		list:true
					},
					entityInstanceName: {
						title: 'Source Entity Name' ,
		       			type: 'textarea', 
		     		  	width: "20%",  
		     		  	create: false, 
		   				edit: false, 
		     	  		list:true
					},
					testFactoryId: {
						type: 'hidden', 
						create: true, 
						edit:true,
						defaultValue: testFactoryId
					},
				 	entityInstanceId: {
						type: 'hidden',
						create: true, 
						edit:true,
						list:true,
						defaultValue: entityInstanceId
					}, 
					entityTypeId: {
						type: 'hidden', 
						create: true, 
						edit:true,
						list:true,
						defaultValue: entityTypeId
					},
					entityTypeId2: {
						type: 'hidden',
						create: true, 
	              	  	edit: false, 
						list: false,
						defaultValue: changeRequestTypeId
					},		   		      
	   			     changeRequestName: { 
		     	  		title: 'Change Request',
		     	  		inputTitle: 'Change Request <font color="#efd125" size="4px">*</font>',
		     	  		create: true, 
		   				edit: true, 
		     	  		list:true,
		     	  		width: "20%",
		  			 	},
	  			     description: { 
		       			title: 'Description' ,
		       			type: 'textarea', 
		     		  	width: "20%",  
		     		  	create: true, 
		   				edit: true, 
		     	  		list:true
		    	      },	
				     ownerId : {
				 		title : 'Owner',
						list : true,
						create : true,
						edit : true,
						width: "20%",
						options : function(data) {
							return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
						}
					},
				  	priorityId : {
						title : 'Priority',
						inputTitle : 'Priority <font color="#efd125" size="4px">*</font>',
						list : true,
						create : true,
						edit : true,
						width: "20%",
						options : 'administration.executionPriorityList'
				  	},
				 	planExpectedValue: {
						title : 'Planned Value',										
						inputTitle : 'Planned Value <font color="#efd125" size="4px"></font>',
						edit : true,
						list : true,
						create : true,								
						width : "20%",					
				 	},				
					statusCategoryId : {
						title : 'Status',
						create:false,
						list : true,
						edit : true,
						width: "20%",
						defaultValue : 1,
						options:function(data){
					 		return 'status.category.option.list';
		     			}
					},
					isActive: { 
                   		title: 'Active',
                   		list:true,
       					edit:true,
    					create:false,
    					type : 'checkbox',
    					values: {'0' : 'No','1' : 'Yes'},
    		    		defaultValue: '1'
		    		},
		    		attachment: { 
						title: '', 
						list: true,
						create: false,
						width: "5%",
						display:function (data) {	        		
		           		//Create an image that will be used to open child table 
						var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>'); 
		       			$img.click(function () {
		       				isViewAttachment = false;
		       				var jsonObj={"Title":"Attachments for ChangeRequest",			          
		       					"SubTitle": 'ChangeRequest : ['+data.record.changeRequestId+'] '+data.record.changeRequestName,
		    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=42&entityInstanceId='+data.record.changeRequestId,
		    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=42&entityInstanceId='+data.record.changeRequestId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
		    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
		    	    			"updateURL": 'update.attachment.for.entity.or.instance',
		    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=42',
		    	    			"multipleUpload":true,
		    	    		};	 
			        		Attachments.init(jsonObj);
		       		  	});
		       			return $img;
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
								listGenericAuditHistory(data.record.changeRequestId,"ChangeRequest","changeRequestAudit");
							});
							return $img;
						}
					}, 					
				
	        	},	 	        	
	        	 //Validate form when it is being submitted
	         	 formSubmitting: function (event, data) {
	        	 	 data.form.find('input[name="changeRequestName"]').addClass('validate[required]');
	              	data.form.validationEngine();
	             	return data.form.validationEngine('validate');
	         	 }, 
	          	//Dispose validation logic when form is closed
	          	formClosed: function (event, data) {
	             	data.form.validationEngine('hide');
	             	data.form.validationEngine('detach');
	         	}
	     	});			 		 
						 
	$('#jTableChangeRequestsList').jtable('load');		 
}

function displayConsolidatedAttachments(){	
	try{
		if ($("#jTableConsolidatedAttachments").length>0) {
			 $('#jTableConsolidatedAttachments').jtable('destroy');
		}
	} catch(e) {};
	
	if(referenceProductId == 'undefined' || referenceProductId == 0){
		callAlert("Please select a product");
	}
	
    $('#jTableConsolidatedAttachments').jtable({
		title: 'Attachments', 
		paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:false},	
		actions: { 
			listAction: 'attachments.consolidated.for.product.list?productId='+referenceProductId,
			deleteAction: 'delete.attachment.for.entity.or.instance',		
		},		
		
		
		fields: {			 
			attachmentId: { 
				key: true, 
				create: false, 
            	edit: false, 
				list: false
			},			
			attributeFileName: {
				title: 'Name',
				edit: false,
				list: true, 
				display:function (data) {
					return $("<a style='color: #0000FF'; href=javascript:loadAttachmentsPopupEvidence('"+data.record.attachmentId+"');>"+data.record.attributeFileName+"</a>");
				}
			},
			description: {
				title: 'Description',
				edit:false,
				list: true, 
			},
			attachmentType: {
				title: 'Attachment Type',
				edit:true,
				list: true, 
				//options: getJSONObject.attachmentTypeURL				
				options:function () {
            		return  'attachment.type.for.entity.list.option?entityTypeId=0'; 
            	}				
			},
			attributeFileExtension: {
				title: 'File Extension',
				list: true, 
			},			
			attributeFileSize: {
				title: 'File Size',
				list: true, 
			},			
			createrName: { 
				title: 'Uploaded By', 
				list: true,
			},
			uploadedDate: { 
				title: 'Uploaded Date', 
				list: true,
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
							listGenericAuditHistory(data.record.attachmentId,"Attachment","attachmentAudit");
						});
					return $img;
				}
			},					
		}
    });
    $('#jTableConsolidatedAttachments').jtable('load');			
}
Delete - JTable
*/

var isFirstLoad=true;
function setDefaultnode() {			
	var nodeFlag = false;			
	if(isFirstLoad) {
		$("#treeContainerDiv").on("loaded.jstree",function(evt, data) {
			$.each($('#treeContainerDiv li'), function(ind, ele){
				if($.jstree.reference('#treeContainerDiv').is_parent($(ele).attr("id"))){
					defaultNodeId = $(ele).attr("id");							
					isFirstLoad = false;
					
					$.jstree.reference('#treeContainerDiv').deselect_all();
					$.jstree.reference('#treeContainerDiv').close_all();
					$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
					$.jstree.reference('#treeContainerDiv').trigger("select_node");							
					//return false;
				}
			});	
			//setDefaultnode();
		});
	} else {
		
		defaultNodeId = $.jstree.reference('#treeContainerDiv').get_node(defaultNodeId).children[0];
		nodeFlag = validateNodeLength($.jstree.reference('#treeContainerDiv').get_node(defaultNodeId))
		if(nodeFlag){
			setDefaultnode();					
		}else{			
			console.log(defaultNodeId)
			$.jstree.reference('#treeContainerDiv').deselect_all();
			$.jstree.reference('#treeContainerDiv').close_all();
			$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
			$.jstree.reference('#treeContainerDiv').trigger("select_node");
		}
	}
}

function listclarificationTrackerAuditHistory(clarificationTrackerId){
	//clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=ClarificationTracker&sourceEntityId='+clarificationTrackerId+'&jtStartIndex=0&jtPageSize=1000';
	//var url='administration.event.list?sourceEntityType=ClarificationTracker&sourceEntityId='+clarificationTrackerId;
	var jsonObj={"Title":"ClarificationTracker Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":1000,	    
			"componentUsageTitle":"clarificationTrackerAudit",
	};
	SingleDataTableContainer.init(jsonObj);
	//SingleJtableContainer.init(jsonObj);
}
</script>

<div><%@include file="attachments.jsp" %></div>
	<!-- END JAVASCRIPTS -->
<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
</body>
<!-- END BODY -->
</html>