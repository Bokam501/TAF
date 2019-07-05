<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.hcl.atf.taf.controller.HomePageController"%>
<%@page import="com.hcl.atf.taf.model.UserList"%>
<%@page import="java.util.List"%>
<%@page import="com.hcl.atf.taf.model.TestRunList"%>
<%@ page import = "java.util.ResourceBundle" %>
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
<!--<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link href="js/Scripts/validationEngine/validationEngine.jquery.css" rel="stylesheet"	type="text/css"></link>-->
<link href="css/customStyle.css" rel="stylesheet" type="text/css">

<link href="js/datatable/Editor-1.5.6/css/dataTables.editor.css" rel="stylesheet" type="text/css"></link>
<link href="js/datatable/Editor-1.5.6/css/editor.dataTables.css" rel="stylesheet" type="text/css"></link>
<jsp:include page="dataTableHeader.jsp"></jsp:include>
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet"	type="text/css"></link>
<!-- END THEME STYLES -->

<style>
	.ui-dialog .ui-widget-content, .ui-dialog .ui-dialog-content{
		max-height:500px !important;
		overflow-x:hidden !important;
	}

 	.row > #status_dd > .select2me{
		 height: 28px !important; 
	     margin-top: 3px; 
	     width: 90px !important ;
	     font-size: 13px;
	}
	.row > #status_dd > .select2me > a{
	 	height: 28px !important; 
	     padding-top: 0px; 
	     margin-top: 0px; 
	 }
  	.select2-drop-active {
		z-index:100060 !important;
 	 }  
</style>
</head>
<!-- END HEAD -->

<!-- BEGIN BODY -->
<body>

<!-- <div id="header"></div> -->
<form action="" method="post" class="button" name="importForm" id="importForm" enctype="multipart/form-data">
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
						<div class="portlet-title toolbarFullScreen">
							<div class="caption caption-md">
								<i class="icon-bar-chart theme-font hide"></i>
								<span class="caption-subject theme-font bold uppercase">Resources</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
								<span class="caption-helper hide">weekly stats...</span>
							</div>
							<div class="actions">
								<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" 
									onClick="fullScreenHandlerDTRegularUser()" data-original-title="" title="FullScreen"></a>
							</div>
						</div>												
						<div class="portlet-body form"  >
						<div id="hidden"></div>
						
						<div class="row " >
							<div class="col-md-12 toolbarFullScreen">
						        <ul class="nav nav-tabs" id="tabslist">
									<li class="active"><a href="#RegularUsers" data-toggle="tab">Regular Users</a></li>
									<li><a href="#CustomerUsers" data-toggle="tab">Customer Users</a></li>
								</ul>
																			
						<div class="tab-content">
							<!-- Regular Users Tab -->
							<div id="RegularUsers" class="tab-pane fade active in">								
					            <input type="hidden" id="hdnProductId" name="hdnProductId">
					            <input type="hidden" id="hdnResPoolId" name="hdnResPoolId">
						        <div class="jScrollContainerFullScreen" style="clear:both;padding-top:10px;position: relative">						        
							         <div class="row jScrollTitleContainer" style="position: absolute;z-index: 10;width: 950px;margin-left: 205px;margin-top: 5px;" >
								    	<div class="col-md-5" style ="top: 5px;display:none">
						             		<label class="control-label" style="color: #FFFFFF;">Upload :</label> 
											<input type="button"  class="btn blue" id="trigUploadUserList" value="Users List" style=" margin-top: -2px;
														   font-size: .9em;height: 26px;background-color: #55616f;padding-top: 4px" />
											<span id="uploadUsersfileName"></span>
											<input id="uploadUserListFile" type="file" name="uploadUserListFile" style="display:none;"
														   onclick="{this.value = null;};" onchange="importUserList('FTE');" />
						             		
						             		<img src="css/images/dt_download_features.png" title="Download Template - Users List" id ="downloadTemplateUserIcon" class="icon_imgSize showHandCursor" 
												style = "margin-left: 5px;" alt="Download" onclick="downloadTemplateUserList();">					           				
					           			</div>										
									</div>					      		
		      		
						      		<div id="userDT">						      			
										 <div id="status_dd" class="col-md-4" style="position:absolute;z-index:10;right:118px;margin-top:3px;">
											<select class="form-control input-small select2me" id="status_ul" style="float:right;">
												<option value="2" selected><a href="#">ALL</a></option>
												<option value="1" ><a href="#">Active</a></option>
												<option value="0" ><a href="#">Inactive</a></option>
												<option value="-1" ><a href="#">Deleted</a></option>
											</select>
										</div>										 
										 <table id="regularUsersContainerDT"  class="cell-border compact row-border" cellspacing="0" width="100%">
											<thead>
							            		<tr>							            			
							            			<th>First Name</th>
									                <th>Middle Name</th>
									                <th>Last Name</th>	
									                <th>Login ID</th>
									                <th>Display Name</th>
									                <th>User Code</th>
									                <th>Email ID</th>
									                <th>Contact Number</th>
									                <th>User Type</th>
									                <th>User Role</th>
									                <th>Languages</th>									          
									                <th>Status</th>	
									                <th>Skill Name</th>
									                <th>Authentication Type</th>
									                <th>Authentication Mode</th>
									                <th></th>
							            		</tr>
							       			 </thead>
							       			 <tfoot>
							            		<tr>						            			
									                <th></th>
									                <th></th>
									                <th></th>
									                <th></th>
									                <th></th>
									                <th></th>
									                <th></th>
							            			<th></th>
							            			<th></th>	
							            			<th></th>	
							            			<th></th>	
							            			<th></th>	
							            			<th></th>	
							            			<th></th>	
							            			<th></th>
							            			<th></th>	
							            		</tr>
							       			 </tfoot>
										</table>									
									</div>						      		
								</div>							
								<div class="cl">&nbsp;</div>								
							</div>
						
							<!-- Regular Users Tab Ends -->
							
							<!--<div id="CustomerUsers" class="tab-pane fade" >
							    <div id="filter" class="col-md-12 toolbarFullScreen" >
							    	<div class="col-md-4">
					             		<label class="control-label">Upload UserList :</label> 
				           				<input id="uploadFile" type="file" name="uploadFile" 
				           				onclick="{this.value = null;};" onchange="importUsersList('Customer');"/>
				           			</div>									
								</div>
								
						     	<div id="customer_status_dd" class="col-md-4" style="position:absolute;z-index:10;right:118px;margin-top:3px;">
									<select class="form-control input-small select2me" id="customer_status_ul" style="float:right;">
										<option value="2" selected><a href="#">ALL</a></option>
										<option value="1" ><a href="#">Active</a></option>
										<option value="0" ><a href="#">Inactive</a></option>
									</select>
								</div> 
							     <div class="jScrollContainerResponsive jScrollContainerFullScreen" style="clear:both;padding-top:10px">
							      	<div id="jTableCustomerUsersContainer" class ="jTableContainerFullScreen"></div>							      	
								</div>
									
								<div class="cl">&nbsp;</div>							
							</div>-->
							
							<div id="CustomerUsers" class="tab-pane fade">
								<!-- <div id="filter" class="col-md-12 toolbarFullScreen" >
							    	<div class="col-md-4">
					             		<label class="control-label">Upload UserList :</label> 
				           				<input id="uploadFile" type="file" name="uploadFile" 
				           				onclick="{this.value = null;};" onchange="importUsersList('Customer');"/>
				           			</div>									
								</div> -->
								<div class="row jScrollTitleContainer" style="position: absolute;z-index: 10;width: 950px;margin-left: 205px;margin-top: 5px;" >
								    	<div class="col-md-5" style ="top: 5px;display:none">
						             		<label class="control-label" style="color: #FFFFFF;">Upload :</label> 
											<input type="button"  class="btn blue" id="trigUploadCustomerList" value="Customer List" style=" margin-top: -2px;
														   font-size: .9em;height: 26px;background-color: #55616f;padding-top: 4px" />
											<span id="uploadCustomerUsersfileName"></span>
											<input id="uploadCustomerFile" type="file" name="uploadCustomerFile" style="display:none;"
														   onclick="{this.value = null;};" onchange="importCustomerList('Customer');" />						             		
						             							           				
					           			</div>										
									</div>
								
								 <div id="customer_status_dd" class="col-md-4" style="position:absolute;z-index:10;right:140px;margin-top:3px;">
									<select class="form-control input-small select2me" id="customer_status_ul" style="float:right;">
										<option value="2" selected><a href="#">ALL</a></option>
										<option value="1" ><a href="#">Active</a></option>
										<option value="0" ><a href="#">Inactive</a></option>	
										<option value="-1" ><a href="#">Deleted</a></option>										
									</select>
								</div>										 
								 <table id="customerUsersContainerDT"  class="cell-border compact row-border" cellspacing="0" width="100%">
									<thead>
										<tr>							            			
											<th>First Name</th>
											<th>Middle Name</th>
											<th>Last Name</th>	
											<th>Login ID</th>
											<th>Display Name</th>
											<th>User Code</th>
											<th>Email ID</th>
											<th>Contact Number</th>
											<th>User Type</th>
											<th>User Role</th>
											<th>Customer</th>
											<th>Languages</th>									          
											<th>Status</th>	
											<th>Skill Name</th>
											<th>Authentication Type</th>
											<th>Authentication Mode</th>
											<th></th>
										</tr>
									 </thead>
									 <tfoot>
										<tr>						            			
											<th></th>
											<th></th>
											<th></th>
											<th></th>
											<th></th>
											<th></th>
											<th></th>
											<th></th>
											<th></th>
											<th></th>											
											<th></th>	
											<th></th>	
											<th></th>	
											<th></th>	
											<th></th>	
											<th></th>
											<th></th>	
										</tr>
									 </tfoot>
								</table>									
							</div>
							
							<!-- Customer Users Tab Ends-->
							</div>
						</div>
						</div>	
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

<!-- END PAGE-CONTAINER -->

<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp" %></div>			
<!-- END FOOTER -->
<!-- POPUP -->
<div id="div_PopupMain" class="modal " tabindex="-1" aria-hidden="true" style="z-index:999">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupClose()" aria-hidden="true"></button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:320px" data-always-visible="1" data-rail-visible1="1">
					<div id="validateResultDiv"></div>     
					<div id="resPoolSummary"></div>     
				</div>
			</div>
		</div>
	</div>
</div>

<div id="div_PopupBackground"></div>
<!-- POPUP ENDS -->

<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/jquery.vmap.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.sparkline.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="files/lib/metronic/theme/assets/admin/layout/scripts/quick-sidebar.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jstree/dist/jstree.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->

<!-- the jScrollPane script -->
<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

<!-- Include jTable script file. -->
<!--<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>-->

<!-- Plugs ins for jtable inline editing -->
<!--<script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.js"></script>-->
<script type="text/javascript" src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>
 
<script src="js/viewScript/UserListDT.js" type="text/javascript"></script>
 <script src="js/viewScript/userCustomerListDT.js" type="text/javascript"></script>
 <script src="js/importData.js" type="text/javascript"></script>  
 <script src="js/pageSpecific/download.js" type="text/javascript"></script> 

<!-- Validation engine script file and english localization -->
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>
<script src="js/select2.min.js" type="text/javascript"></script>

<% ResourceBundle resourceBundleForXeroxDownLoadTemplate = ResourceBundle.getBundle("TAFServer");
    String downloadUserTemplate=resourceBundleForXeroxDownLoadTemplate.getString("DOWNTEMPLATEUSERLIST_XEROX");  	
%>

<script type="text/javascript">
var newnodetype="";
var rpId = -10;
var tflId = 0;
var usertypeId = 0;
var date = new Date();
var timestamp = date.getTime();
var urlToListUserDetails;
var selectedTab;
var id;
var customerStatusId;
var userType = "FTE";

var userDownLoadTemplateXerox = "<%=downloadUserTemplate%>";

jQuery(document).ready(function() {	
   $("#menuList li:first-child").eq(0).remove();	
   QuickSidebar.init(); 
   setBreadCrumb("Admin");
   
   id = $("#status_ul").find('option:selected').val();
   customerStatusId = $("#customer_status_ul").find('option:selected').val();
   selectedTab = $("#tabslist>li.active").index();
   
   if(selectedTab == 0){
	   usertypeId = 0;
	   $("#status_dd").css("display","block");
	   $("#customer_status_dd").css("display","none");
	   urlToListUserDetails = 'administration.user.list.by.options.status?userTypeId='+usertypeId+"&resourcePoolId="+rpId+"&testFactoryLabId="+tflId+"&timeStamp="+timestamp+"&statusID="+id;	  
	   updateUserList(urlToListUserDetails+'&jtStartIndex=0&jtPageSize=10000', "parentTable", "", "");	     
	   
	}else if(selectedTab == 1){
		usertypeId = 4;
		$("#status_dd").css("display","none");
		$("#customer_status_dd").css("display","block");
		urlToListUserDetails = 'administration.user.list.by.options.status?userTypeId='+usertypeId+"&resourcePoolId="+rpId+"&testFactoryLabId="+tflId+"&timeStamp="+timestamp+"&statusID="+customerStatusId;
		updateCustomerUserList(urlToListUserDetails+'&jtStartIndex=0&jtPageSize=10000', "parentTable", "", "");
	}
}); 

$(document).on('click', '#tabslist>li', function(){
	selectedTab=$(this).index();
	id = $("#status_ul").find('option:selected').val();
	customerStatusId = $("#customer_status_ul").find('option:selected').val();
	   if(selectedTab == 0){
		   usertypeId = 0;
		   $("#status_dd").css("display","block");
		   $("#customer_status_dd").css("display","none");
		   urlToListUserDetails = 'administration.user.list.by.options.status?userTypeId='+usertypeId+"&resourcePoolId="+rpId+"&testFactoryLabId="+tflId+"&timeStamp="+timestamp+"&statusID="+id;		  
		   updateUserList(urlToListUserDetails+'&jtStartIndex=0&jtPageSize=10000', "parentTable", "", "");
		   
		}else if(selectedTab == 1){
			usertypeId = 4;
			$("#status_dd").css("display","none");
			$("#customer_status_dd").css("display","block");
			urlToListUserDetails = 'administration.user.list.by.options.status?userTypeId='+usertypeId+"&resourcePoolId="+rpId+"&testFactoryLabId="+tflId+"&timeStamp="+timestamp+"&statusID="+customerStatusId;
			updateCustomerUserList(urlToListUserDetails+'&jtStartIndex=0&jtPageSize=10000', "parentTable", "", "");
		}
});
	
$(document).on('change','#status_ul', function() {
	var id = $("#status_ul").find('option:selected').val();
	var date = new Date();
    var timestamp = date.getTime();
    selectedTab = $("#tabslist>li.active").index();
    if(selectedTab == 0){ 	   
 	  usertypeId = 0;
	   urlToListUserDetails = 'administration.user.list.by.options.status?userTypeId='+usertypeId+"&resourcePoolId="+rpId+"&testFactoryLabId="+tflId+"&timeStamp="+timestamp+"&statusID="+id;	  
	   updateUserList(urlToListUserDetails+'&jtStartIndex=0&jtPageSize=10000', "parentTable", "", "");	     

 	}
 });
 
 
$(document).on('change','#customer_status_ul', function() {
	var custStatusid = $("#customer_status_ul").find('option:selected').val();
	var date = new Date();
    var timestamp = date.getTime();
    selectedTab = $("#tabslist>li.active").index();
    if(selectedTab == 1){
 		usertypeId = 4;
 		urlToListUserDetails = 'administration.user.list.by.options.status?userTypeId='+usertypeId+"&resourcePoolId="+rpId+"&testFactoryLabId="+tflId+"&timeStamp="+timestamp+"&statusID="+custStatusid;
 		updateCustomerUserList(urlToListUserDetails+'&jtStartIndex=0&jtPageSize=10000', "parentTable", "", "");
 	}
 });
 
/* Pop Up close function */
function popupClose() {	
	$("#div_PopupMain").fadeOut("normal");
	$("#div_PopupBackground").fadeOut("normal");
}

/* Load Poup function */
function loadPopup(divId) {
	$("#" + divId).fadeIn(0500); // fadein popup div
	$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
														// IE7, IE8
	if($("#div_PopupMain").children().length == 1)
	$("#div_PopupMain").prepend('<div id="div_popupclose" title="Press Esc to close" onclick="popupClose()" class="ImgPopupClose"  style="display:block;"> </div>');
	$("#div_PopupBackground").fadeIn(0001);
}

/* function importUsersList(userTypeUpload) {
	userType = userTypeUpload;
	var fileContent = document.getElementById("uploadFile").files[0];
	var formdata = new FormData();
	var ftype = "userList";	
	formdata.append("filetype", ftype);	
	formdata.append("uploadFile", fileContent);

	 $.ajax({
		   // url: "testcase.import?productId="+productId,
			url: "userList.validation.for.import?userType="+userType,
		    method: 'POST',
		    contentType: false,
		    data: formdata,
		    dataType:'json',
		    processData: false,
		    success : function(data) {
		    	if(data.Result=="ERROR"){
		    		callAlert(data.Message);
		    		return false;
		    	}

		   	document.getElementById("validateResultDiv").innerHTML=data.Message;	   
		    	showValidationOutputDialog(data.Message, data.exportFileName);
		    },
		});
} 

function showValidationOutputDialog(msg, reportfilepath) {	
		$('#validateResultDiv').dialog({
			 width : 400,
			 top : 200,		
			title : "Validation Details", 
		
			buttons : {
				"Generate Validation Report" : function() {					
					downloadFileFun(reportfilepath);
				},
				"Import" : function() {
					validImport();					
					$(this).dialog('close');
				},
				"Cancel" : function() {					
					$(this).dialog('close');					
					//put code here for form submission
				}
			}
		});
		
	} */
	
	function downloadFileFun(reportfilepath){		
			var urlfinal="rest/download/evidence?fileName="+reportfilepath;
		  	parent.window.location.href=urlfinal;	
	}
	
	 function loadDialog(id)
	   {	
		   	$('#'+id).dialog({
		   		width: 400,
		   		heigth: 200,
		   	    modal: true,  
		   	    position: {my: "center top", at:"center top", of: window}
		   	    
		   	});
		   	$(".ui-dialog-titlebar").hide();
	
	   }
	 	function disableDialog(id) {
			$('#'+id).dialog('close');
		}
	 	
	 	function disablingButton(){
	 		var uploadBtnId=document.getElementById("uploadFile");
	 		uploadBtnId.disabled =true; 		
	 	}
	 	
	function validImport(){
	
	var fileContent = document.getElementById("uploadFile").files[0];
	var formdata = new FormData();
	var ftype = "userList";
	 formdata.append("uploadFile", fileContent);
	 $.ajax({
		   // url: "testcase.import?productId="+productId,
			url: 'userList.import?userType='+userType,
		    method: 'POST',
		    contentType: false,
		    data: formdata,
		    dataType:'json',
		    processData: false,
		    success : function(data) {
		    	if(data.Result=="ERROR"){
		    		callAlert(data.Message);
		    		return false;
		    	}
		    //	listTestCasesOfSelectedProduct();
		    	listUserbySelectedState();
		    },
		});	
}

//Testcases Scope Ends 
/* function resourcePoolSummary_popup(){
	if(usertypeId == ""){
		usertypeId=0;
	}
	if(rpId == ""){
		rpId=0;
	}
	 if(tflId == ""){
		tflId=0;
	}
	urlToGetResourcePoolcount = 'testfactory.resourcepool.summary.list?userTypeId='+usertypeId+"&resourcePoolId="+rpId+"&testFactoryLabId="+tflId;
	
	try{
		if ($('#div_PopupMain #resPoolSummary').length>0) {
			 $('#div_PopupMain #resPoolSummary').jtable('destroy'); 
		}
	} catch(e) {}
	$('#div_PopupMain #resPoolSummary').jtable({
		title: 'ResourcePool Summary Status',
      selecting: true,  //Enable selecting 
      paging: false, //Enable paging
      pageSize: 10, 
        actions: {
     	 	listAction: urlToGetResourcePoolcount,
      },  
      fields : {
      	 	jsonResourcePoolSummaryId: {
	                 key: true,
	                 type: 'hidden',
	                 list: false,
	                 create: false
	             }, 
	             resourcePoolId: { 
		   				create: false, 
		   				edit: false, 
		   				list: false
		   				}, 
	  			resourcePoolName: { 
		     	  		title: 'Resource Pool Name',
		     	  		inputTitle: 'ResourcePool Name <font color="#efd125" size="4px">*</font>',
		     	  		width: "20%",
		     	  		list:true
		  			 	}, 
				userTypeId: { 
			   				create: false, 
			   				edit: false, 
			   				list: false
			   				}, 
				userTypeLabel: { 
			     	  		title: 'User Types',
			     	  		inputTitle: 'userTypeLabel Name <font color="#efd125" size="4px">*</font>',
			     	  		width: "10%",
			     	  		list:true
			  			 	},
	             engagementManager_Count:{
	                 title: 'Test Factory Managers',
	            	 list:true,
	                 width:"10%"
	             },
	             testManager_Count:{
	                 title: 'Test Managers',
	            	 list:true,
	                 width:"10%"
	             },
	             testLead_Count: {
	                 title: 'Test Leads',
	                 list:true,
	                 width:"10%"
	             },
	             tester_Count: {
	                 title: 'Testers',
	                 list:true,
	                 width:"10%"
	             },
	             programManager_Count: {
	                 title: 'Program Managers',
	                 list:true,
	                 width:"10%"
	             },
	             resourceManager_Count: {
	                 title: 'Resource Managers',
	                 list:true,
	                 width:"10%"
	             },
      	},  
		});
	$('#div_PopupMain #resPoolSummary').jtable('load');
    loadPopup("div_PopupMain");	
    $("#div_PopupMain h4").text("ResourcePool Summary Status");
} */

document.onkeydown = function(evt) {
	evt = evt || window.event;
	if (evt.keyCode == 27) {
		if (document.getElementById("div_PopupMain").style.display == 'block') {
			popupClose();
		}
	}
};

function clearLoginId(){
	var loginId=$('#loginId').val();
	if (loginId.match(" ")){
		$('#loginId').val("");
	}
}

function loginIdSuggestion(){
	var firstName=$('#firstName').val().substring(0, 1);
	var lastName=$('#lastName').val();
	var counter = Math.floor((Math.random() * 100) + 1); 
	var loginID = firstName+lastName+"_"+counter;
	$.ajax({
	    url: 'administration.user.list.loginId.exist?loginId='+loginID,
	    method: 'POST',
	    contentType: "application/json; charset=utf-8",
	    dataType : 'json',
		success : function(data) {
			if(data.Result=="OK"){
				if(firstName != "" && lastName != ""){
					$('#loginId').val(loginID);
				}
	    	}else{
	    		loginIdSuggestion();
	    	}
			
		},
	});
}

function resetUserList(userID){
    var alertMsg = "Are you sure you want to Reset the Password?";
	 var flag = bootbox.confirm(alertMsg, function(result) {		 
		 if(result){
			 callConfirmSuccess('administration.user.update.password?userId='+userID);
		 }      
       return true;
	}) ;
	callConfirm(flag);
}

function displayNameSuggestion(){
	var firstName=$('#firstName').val();
	var lastName=$('#lastName').val();
	
	var displayName = firstName+" "+lastName;
		if(firstName != "" && lastName != ""){
			$('#userDisplayName').val(displayName);
		}
}

</script>
</form>
<!-- END JAVASCRIPTS -->
<div id="regularUserCustomerContainer" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
	<div class="modal-full">
		<div class="modal-content">
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title theme-font">Add/Edit User Customer Account</h4>
			</div>
			<div class="modal-body">					
				 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
	 				<div id=regularUserCustomerDTContainer></div>
	 			</div>					 
			</div>
		</div>
	</div>
</div>


<div id="customerUserCustomerContainer" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
	<div class="modal-full">
		<div class="modal-content">
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title theme-font">Add/Edit User Customer Account</h4>
			</div>
			<div class="modal-body">					
				 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
	 				<div id=customerUserCustomerDTContainer></div>
	 			</div>					 
			</div>
		</div>
	</div>
</div>
	
<!-- Status Change notification start -->
<div id="div_StatusChangeNotifications" class="modal " tabindex="-1" aria-hidden="true">
	<div class="modal-dialog" style="width: 400px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title caption-subject theme-font">Status Changes</h4>
			</div>
			<div class="modal-body">				
					<div class="portlet light">								
						<div class="portlet-body form">
						  <div class="skin skin-flat">
								<form role="form">
									<div class="form-body" style="padding-top: 0px;padding-bottom: 0px;">									
										<div class="form-group">													
											<div class="input-group">
											<div id="notifyStatusChange" class="icheck-list">														
											  </div>	 
											</div>
										</div>											
									</div>
									<div class="form-group">													
											<div class="input-group">
											<!--  <input id="userStatusChangeID" type="hidden"></input>-->
											<textarea id="statusChangeInput" style="width: 300px;height:100px;"></textarea>	 
											</div>
									</div>
									<div class="form-actions" style="padding-top: 5px;padding-bottom: 5px;">
									<button type="button" onclick="submitStatusChangeNotificationHandler();" class="btn green-haze" style="padding: 6px 6px;" >Submit</button>
										<button type="button" class="btn grey-cascade" onclick="popupCloseStatusChangeNotificationHandler()">Cancel</button>
									</div>
								</form>
							</div>  
						</div>
					</div>
				</div>
		</div>
	</div>
</div>

<!-- Status Change notification end -->

<!-- User Delete notification start -->

<div id="div_UserDeleteNotifications" class="modal " tabindex="-1" aria-hidden="true">
	<div class="modal-dialog" style="width: 400px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title caption-subject theme-font">Notification</h4>
			</div>
			<div class="modal-body">				
					<div class="portlet light">								
						<div class="portlet-body form">
						  <div class="skin skin-flat">
								<form role="form">
									<div class="form-body" style="padding-top: 0px;padding-bottom: 0px;">									
										<div class="form-group">													
											<div class="input-group">
											<div id="notifyUserDelete" class="icheck-list">														
											  </div>	 
											</div>
										</div>											
									</div>
									<div class="form-group">													
											<div class="input-group">
											<!--  <input id="userStatusChangeID" type="hidden"></input>-->
											<textarea id="userDeleteInput" style="width: 300px;height:100px;"></textarea>	 
											</div>
									</div>
									<div class="form-actions" style="padding-top: 5px;padding-bottom: 5px;">
									<button type="button" onclick="submitUserDeleteNotificationHandler();" class="btn green-haze" style="padding: 6px 6px;" >Submit</button>
										<button type="button" class="btn grey-cascade" onclick="popupCloseUserDeleteNotificationHandler()">Cancel</button>
									</div>
								</form>
							</div>  
						</div>
					</div>								
				</div>
		</div>
	</div>
</div>

<!-- User Delete notification end -->

<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
</body>
<!-- END BODY -->
</html>