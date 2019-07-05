<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.hcl.atf.taf.controller.HomePageController"%>
<%@page import="com.hcl.atf.taf.model.UserList"%>
<%@page import="java.util.List"%>
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
<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet"	type="text/css"></link>
<link href="css/customStyle.css" rel="stylesheet" type="text/css">
<!-- END THEME STYLES -->

<style type="text/css">
#filter > .col-md-4 >.select2me{
	height: 26px;
    padding-top: 1px;
    margin-top: 6px;
    width: 100px !important;
    font-size: 13px;
}
#filter > .col-md-4 >.select2me > a{
	margin-top: -1px;
	padding-top: 0px;
}

/* #wpStatus_dd .form-control .select2-choice{
	height:28px;
    margin-top: -4px;
} */
#status_dd > .form-control .select2-choice {
 	height: 28px; 
 }
</style>
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
			<!-- BEGIN MEGA MENU -->			
			 <div><%@include file="menu.jsp" %></div>			
 			<!-- END MEGA MENU -->
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->

<!-- BEGIN CONTAINER -->
<div class="page-container">
<div><%@include file="treeStructureSidebar.jsp" %></div>
<div><%@include file="postHeader.jsp" %></div>
<div><%@include file="dynamicJtableContainer.jsp"%></div>

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
								<i class="icon-bar-chart theme-font hide"></i>
 								<span class="caption-subject theme-font bold uppercase">ACTIVITY WORKPACKAGE</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span> 
								<span class="caption-helper hide">weekly stats...</span>
							</div>
						</div>
						<div class="portlet-body"  style="display:none">
						 <!-- <div class="col-lg-1" style="padding-right: 10px;padding-top: 8px;">
								<label>Upload : </label>
							</div>
							<div class="col-lg-1" style="padding-left: 0px;">													
           			  			<input type="button" class="btn blue" id="trigUploadActivities" value="Activities">
           			  			<span id="fileNameActivities"></span>
								<input id="uploadFileActivities" type="file" name="uploadFileActivities" style="display:none;" onchange="importActivityWorkpackage()">
							</div>  -->
							<div class="row list-separated" style="margin-top:0px;">	
							
							<!-- jtable started -->
									
								<!-- Main -->
								    <div id="main" style="float:left; padding-top:0px; width:100%;">
								    <div id="hdnDiv"> </div>
								    			
								      <!-- Box -->
								      <div class="box">								        
								         <div id="hidden"></div>
								       	<!-- ContainerBox -->
								        <div class="cl">&nbsp;</div>
								 <div id="wpSearchBox" class="toolbarFullScreen" style="display:none">
								<fieldset class="scheduler-border" style="display:none">
									<legend class="scheduler-border theme-font" style="width: 44px;">Search</legend>
									<div class="control-group">
									
							    <div id="filter" class="fliter-align" >
						  			<!-- Adding filter -->									
									<div class='col-md-12'>		
										<div class='col-md-2'>       											
											<label class="bulkAllocationTitle">Owner</label>
										</div>
										<div class='col-md-2'>       											
											<label class="bulkAllocationTitle">Status</label>
										</div>											
										<div class='col-md-2'>      											
											<label class="bulkAllocationTitle">Priority</label>										
										</div>									
												
								   </div>

							    <div class='col-md-12'>							  						
									<div class='col-md-2'>       
											<div id="wpAssignee_dd" class="">												
												<select class="form-control input-small  select2me" id="wpAssigneeSearch_ul">
								        			<option class="assigneeList_ids" value="-">-</option> 			   			
												</select>
											</div>
										</div>
											<div class='col-md-2'>       
											<div id="wpStatus_dd" class="">												
												<select class="form-control input-small  select2me" id="wpStatusSearch_ul">
								        			<option class="statusList_ids" value="-">-</option> 			   			
												</select>
											</div>
										</div>
								
										<div class='col-md-2'>     
											<div id="wpPriorityList_dd" class="">
												<select class="form-control input-small  select2me" id="wpPrioritySearch_ul">
								        			<option class="priorityList_ids" value="-">-</option>  			   			
												</select>
											</div>
										</div>
										<div class="col-md-2">        
									      <button type="button" id="search" class="btn green-haze" onclick="SearchDetail()" style="padding: 6px 7px;font-size: 12px;">Search</button>                                  		     
									     <button type="reset" id="reset" class="btn green-haze" onclick="ResetSearchDetail()" style="padding: 6px 7px;font-size: 12px;">Reset</button> 
										</div>						
									 </div>		
								   </div>
								</div>
								</fieldset>								   
								   	 </div> 
								       <div class="jScrollContainerResponsive jScrollContainerFullScreen" style="clear:both;padding-top:10px;position: relative;min-height: 360px;">  
								       		<!-- Filter -->
											<div id="filter" class="col-md-6 jScrollTitleContainer" style="position: absolute; z-index: 10;
											 		margin-left: 250px;padding-right: 0px;">
													<div id="status_dd" class="col-md-4">
													<select class="form-control input-small select2me" id="isActive_ul">
														<option value="2" ><a href="#">ALL</a></option>
														<option value="1" selected><a href="#">Active</a></option>
														<option value="0" ><a href="#">Inactive</a></option>
													</select>
												</div>
		    								</div>
											<!-- End Filter -->
								      		<div id="jTableContainer" class ="jTableContainerFullScreen"></div>
									   </div>
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

<div><%@include file="singleJtableContainer.jsp" %></div> 
</div>
<!-- END PAGE-CONTAINER -->

<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp" %></div>			
<!-- END FOOTER -->
<!-- Popup -->
<div id="div_PopupMain" class="modal " tabindex="-1" aria-hidden="true" >
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupClose()" aria-hidden="true"></button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:320px" data-always-visible="1" data-rail-visible1="1">
					<div class="jScrollContainerResponsive" style="clear:both;padding-top:10px">
						<div id="jtableSelectEnv"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>	
<!--  <div id="div_PopupMain" class="divPopUpMain" style="display:none;"> -->
<!--         <div title="Press Esc to close" onclick="popupClose()" class="ImgPopupClose"> -->
<!--         </div> -->
<!-- 	</div> -->
<div id="div_PopupBackground"></div>
<!-- Popup Ends -->	

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
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datetimepicker.min.js" type="text/javascript" ></script>

<script src="js/select2.min.js" type="text/javascript"></script>

<script src="js/viewScript/instanceWorkflowConfiguration.js" type="text/javascript"></script>
<script src="js/viewScript/instanceWorkflowSummary.js" type="text/javascript"></script>

<div><%@include file="addComments.jsp"%></div>

<% ResourceBundle resourceChangeUsecaseAwp = ResourceBundle.getBundle("TAFServer");
    String changeReqeustTitle = resourceChangeUsecaseAwp.getString("CHANGEREQUEST_TABLE_TITLE");    
    
%>


	
<script type="text/javascript">

var changeRequestToUsecase = "<%=changeReqeustTitle%>";

var isFirstLoad = true;
var productId=0;
var productName="";
var defaultActivityWorkpackagePlannedStartDate = new Date();
var defaultActivityWorkpackagePlannedEndDate = new Date();

jQuery(document).ready(function() {	
   QuickSidebar.init(); // init quick sidebar
   setBreadCrumb("Activity Management");
   createHiddenFieldsForTree();
	setPageTitle("Products");
	getTreeData('administration.product.hierarchy.tree');
	setDefaultnode();
	var nodeType=0;	
	
    var productVersionId=0;
	var productBuildId=0;							

	$("#treeContainerDiv").on("select_node.jstree",
		     function(evt, data){
	   			var entityIdAndType =  data.node.data;
	   			var arry = entityIdAndType.split("~");
	   			var key = arry[0];
	   			var type = arry[1];
	   			var title = data.node.text;
				var date = new Date();
			    var timestamp = date.getTime();
			    nodeType = type;
			    var loMainSelected = data;
			    var enableAddOrNot = "no";
				
			    defaultActivityPlannedStartDate = new Date();
				defaultActivityPlannedEndDate = new Date();
			    
		        uiGetParents(loMainSelected);
		        if(nodeType == "Product"){
			 		productId=key;
			 		productName = title;
			 		productVersionId=0;
			 		productBuildId=0;
			 		enableAddOrNot = "no";
			    	 var isActive = $("#isActive_ul").find('option:selected').val();
			    	  urlToGetWorkRequestsOfSpecifiedBuildId = 'process.workRequest.list?productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&isActive='+isActive;
			    	  listWorkRequestsOfSelectedProductBuild(enableAddOrNot);
			    	  $("#filter").show();
			 	}
				if(nodeType == "ProductVersion"){
					 productId=document.getElementById("treeHdnCurrentProductId").value;
					 productName=document.getElementById("treeHdnCurrentProductName").value;
					 productVersionId = key;
					productBuildId=0;
			 		enableAddOrNot = "no";
			    	 var isActive = $("#isActive_ul").find('option:selected').val();
			    	  urlToGetWorkRequestsOfSpecifiedBuildId = 'process.workRequest.list?productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&isActive='+isActive;
		    	  	  listWorkRequestsOfSelectedProductBuild(enableAddOrNot);
			    	  $("#filter").show();
			 	}
			    if(nodeType == "ProductBuild"){
			    
			    	  productId=document.getElementById("treeHdnCurrentProductId").value;
			    	  productName=document.getElementById("treeHdnCurrentProductName").value;
					  productVersionId=document.getElementById("treeHdnCurrentProductVersionId").value;
			    	  $(".portlet-body").show();
			    	  productBuildId = key;
					  if(typeof data.node.original.plannedStartDate != "undefined" && data.node.original.plannedStartDate != null && data.node.original.plannedStartDate != ""){
						  defaultActivityWorkpackagePlannedStartDate = data.node.original.plannedStartDate;
					  }
					  if(typeof data.node.original.plannedEndDate != "undefined" && data.node.original.plannedEndDate != null && data.node.original.plannedEndDate != ""){
						  defaultActivityWorkpackagePlannedEndDate = data.node.original.plannedEndDate;
					  }
			    	  document.getElementById("treeHdnCurrentProductBuildId").value=productBuildId;
			    	  enableAddOrNot = "yes";
			    		ActivitySearchInitloadList(); 
			    	  var isActive = $("#isActive_ul").find('option:selected').val();
			    	  urlToGetWorkRequestsOfSpecifiedBuildId = 'process.workRequest.list?productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&isActive='+isActive;
			    	  listWorkRequestsOfSelectedProductBuild(enableAddOrNot);
			    	  $("#filter").show();
			    	  //$("#wpSearchBox").show();
			    	  
			    }
			});
	 $(document).on('change','#isActive_ul', function() {
		 var isActive = $("#isActive_ul").find('option:selected').val();	
		 productId=document.getElementById("treeHdnCurrentProductId").value;
		 productName=document.getElementById("treeHdnCurrentProductName").value;
		 productVersionId=document.getElementById("treeHdnCurrentProductVersionId").value;
		 productBuildId=document.getElementById("treeHdnCurrentProductBuildId").value;		
		 urlToGetWorkRequestsOfSpecifiedBuildId = 'process.workRequest.list?productId='+productId+'&productVersionId='+productVersionId+'&productBuildId='+productBuildId+'&isActive='+isActive;
		 if(nodeType == "ProductBuild"){
				enableAddOrNot = "yes";
			}
		 listWorkRequestsOfSelectedProductBuild(enableAddOrNot);
		 }); 
});

function setDefaultnode() {			
	var nodeFlag = false;			
	if(isFirstLoad) {
		$("#treeContainerDiv").on("loaded.jstree",function(evt, data) {
			$.each($('#treeContainerDiv li'), function(ind, ele){
				if($.jstree.reference('#treeContainerDiv').is_parent($(ele).attr("id"))){
					defaultNodeId = $(ele).attr("id");							
					isFirstLoad = false;
					return false;
				}
			});	
			setDefaultnode();
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

function validateNodeLength(nodeArray){
	var flag = false;			
	if(nodeArray.children && nodeArray.children.length>0){
		flag=true;
	}
	return flag;
}
	

/* Pop Up close function */
function popupClose() {
	//document.getElementById("lbl_PopupTitle").innerHTML = "";
	//document.getElementById("lbl_RightTitle").innerHTML = "";
	//$("#tbl_PopupData").empty();
	$("#div_PopupMain").fadeOut("normal");
	$("#div_PopupBackground").fadeOut("normal");
}

/* Load Poup function */
function loadPopup(divId) {
	$("#" + divId).fadeIn(0500); // fadein popup div
	$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
														// IE7, IE8
	$("#div_PopupBackground").fadeIn(0001);
}

var prodBuildId="";
var actWorkPackageId="";
function listWorkRequestsOfSelectedProductBuild(enableAddOrNot){
	//var productId= document.getElementById("treeHdnCurrentProductId").value ;
	//alert("productId"+productId);

	try{
		if ($('#jTableContainer').length>0) {
			 $('#jTableContainer').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainer').jtable({
        title: 'Activity WorkPackage List',
        editinline:{enable:true},
        selecting: true,  //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10,
		recordAdded: function (event, data) {
             if(data.serverResponse.Message!='undefined' && data.serverResponse.Message!=""){
					callAlert("Warning: "+data.serverResponse.Message)
			}          				         
             				},
        recordsLoaded: function(event, data) {
			$('.portlet > .portlet-title > .tools > .reload').trigger('click');
			if(enableAddOrNot == "no"){
				$('#jTableContainer .jtable-toolbar-item-add-record').hide();
			 }
        },
        /* toolbar : {
            items : [                     
                     {
						icon : '',
						tooltip: 'Workflow Summary',
						cssClass: 'fa fa-th showHandCursor',
						click : function() {
							showWorkflowEntityInstanceStatusSummary(productId, productName, 34, "Activity Workpackage", 0, 0);
						}
					} ,{
						icon : '',
						tooltip: 'Workflow RAG Summary',
						cssClass: 'fa fa-th showHandCursor',
						click : function() {
							showWorkflowEntityInstanceRAGSummary(productId, productName, 34, "Activity Workpackage", 0, 0);
						} 
					}
				]
	   	 }, */
          actions: {
       	 	listAction: urlToGetWorkRequestsOfSpecifiedBuildId,
       	 	createAction:'process.workrequest.add',
       	   editinlineAction: 'process.workrequest.update',
       	   deleteAction: 'process.workrequest.delete',
        },  
        fields : {
        	activityWorkPackageId: {
        		//title: 'Work Package Name',        	
	                 key: true,
	                 list: false,
	                 create: false
	             },	
	             activityWorkPackageName:{
	                 title: 'Work Package Name',
	                 inputTitle: 'Activity Package Name <font color="#efd125" size="4px">*</font>',
	            	 list:true,
	            	 edit:true,
	                 width:"20%"
	             },
				plannedStartDate : {
					title : 'Planned Start Date',
					inputTitle : 'Planned Start Date <font color="#efd125" size="4px">*</font>',
					edit : true,
					create : true,
					list : true,
					type : 'date',
					defaultValue : defaultActivityWorkpackagePlannedStartDate,
					width : "20%"
				},
				plannedEndDate : {
					title : 'Planned End Date',
					inputTitle : 'Planned End Date <font color="#efd125" size="4px">*</font>',
					edit : true,
					list : true,
					create : true,
					type : 'date',
					defaultValue : defaultActivityWorkpackagePlannedEndDate,
					width : "20%"
				},
				workflowRAG : {
					create : false,
					edit : true,
					list : true,
				},
				statusDisplayName : {
					title : 'Current Status',
					create : false,
					list : true,
					edit : false,
					width : "20%",
					display: function(data){
						return data.record.statusDisplayName+"&nbsp;"+data.record.workflowStatusCategoryName;
					}
				},	
				actors : {
					title : 'Status Pending With',
					create : false,
					list : true,
					edit : false,
					width : "20%"
				},
				visibleEventComment : {
					title : 'visibleComment',
					create : false,
					list : false,
					edit : false,
					width : "20%"
				},
				completedBy : {
					title : 'Status Complete By',
					create : false,
					list : true,
					edit : false,
					width : "20%"
				},
				remainingHrsMins : {
					title : 'Status Time Left',
					create : false,
					list : true,
					edit : false,
					width : "20%"
				},
	             description:{
	                 title: 'Description',
	            	 list:false,
	            	 type: 'textarea',    
	            	 create:true,
	            	 edit:true,
	                 width:"20%"
	             },
	             ownerId : {
					title : 'Owner',
					inputTitle: 'Owner <font color="#efd125" size="4px">*</font>',
					list : true,
					create:true,
					edit:true,
					options:function(data){
				     	return 'common.user.list.by.resourcepool.id.productId?productId='+productId;
				    }
				},
				productBuildId: {
	                 title: 'Build',
	             	inputTitle: 'Build <font color="#efd125" size="4px">*</font>',
	                 list:true,
		             edit:false,
		             create:true,
		             options: 'process.list.builds.by.product?productId='+productId
	                 
	             },
			    priorityId : {
					title : 'Priority',					
					list : true,
					create:true,
					edit:true,
					options: 'administration.executionPriorityList'
				},
				
				/* statusId : {
					title : 'Status',
					create:false,
					list : false,
					edit : false,
					options:function(data){
					 	return 'activity.primary.status.master.option.list?productId='+productId;
		     		}
				}, */
				/* 
				// ----- modified -- not needed ---
				statusCategoryId : {
					title : 'Status',
					create:false,
					list : true,
					edit : true,
					defaultValue : 1,
					options:function(data){
					 	return 'status.category.option.list';
		     		}
				}, */
				competencyId : {
					title: 'Competency',
		           	options:function(data){
				        return 'dimension.list.options?dimensionTypeId=1';
				    },
				},
        
				 remark:{
	            	 title: 'Remarks',
	            	 type: 'textarea',  
	            	 list:true,
	                 width:"10%",
	                 list:false
	             },
	            
				actualStartDate : {
					title : 'Actual Start Date',
					create : false,
					edit : true,
					list : true,
					type : 'date',
					width : "20%"
				},
	             actualEndDate:{
	                 title: 'Actual End Date',
	            	 create:false,
	       			 edit: true, 
					 list: false,
	       			 type: 'date',
	       			 width: "20%"
	                 
	             },
			      totalEffort : {
		        	title : 'Total Effort',
		        	create : false,
		        	edit : false,
		        	list : true,
		        	width : "20%",
		        	
		         },
				percentageCompletion : {
					title : '% Completion',
					create : false,
					list : true,
					edit : true,
					width : "4%"
				},
				workflowIndicator : {
					title : '',
					create : false,
					list : false,
					edit : false,
					width : "20%"
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
	             Cloning:{
						title : '',
						list : true,
						create : false,
						width: "5%",
						display:function (data) { 
						 var $img = $('<img src="css/images/cloning.jpg" style="width: 24px;height: 24px;" title="Cloning or Moving Activity Workpackage" data-toggle="modal" />'); 
						$img.click(function () {
							$('#plannedDateDiv').show();
							var jsonActivityWorkPackageCloningObj = {
									"title": "Cloning Activity Workpackage",
									"packageName":"Activity Name",
									"startDate" : dateFormat(data.record.plannedStartDate),
									"endDate" : dateFormat(data.record.plannedEndDate),
									"selectionTerm" : "Select Build",
									"sourceParentID": data.record.productBuildId,
									"sourceParentName": data.record.productBuildName,
									"sourceID": data.record.activityWorkPackageId,
									"sourceName": data.record.activityWorkPackageName,
									"componentUsageTitle":"activityWorkpackageClone",
							}
							
							// -- for cloning workpackage ---
							openLoaderIcon();
							$.ajax({
								type: "POST",
								contentType: "application/json; charset=utf-8",
								url: 'administration.product.hierarchy.tree',
								dataType : 'json',
								complete : function(data){
									console.log('complete');
									closeLoaderIcon();
									},
								success : function(data) {
									treeData = data;			
									Cloning.init(jsonActivityWorkPackageCloningObj);							
									},
								error: function (data){
									console.log('error');
									closeLoaderIcon();
								}
							});
							
						});
						return $img; 
					}
	             },Moving:{
						title : '',
						list : true,
						create : false,
						width: "5%",
						display:function (data) { 
						 var $img = $('<i class="fa fa-scissors" style="width: 24px;height: 24px;" title="Moving Activity Workpackage" data-toggle="modal" />'); 
						$img.click(function () {
							$('#plannedDateDiv').hide();						
								var jsonActivityWorkPackageCloningObj = {
									"title": "Moving Activity Workpackage",
									"packageName":"Activity Name",
									"startDate" : dateFormat(data.record.plannedStartDate),
									"endDate" : dateFormat(data.record.plannedEndDate),
									"selectionTerm" : "Select Build",
									"sourceParentID": data.record.productBuildId,
									"sourceParentName": data.record.productBuildName,
									"sourceID": data.record.activityWorkPackageId,
									"sourceName": data.record.activityWorkPackageName,
									"componentUsageTitle":"activityWorkpackageMove",
							}
							
							// -- for cloning workpackage ---
							openLoaderIcon();
							$.ajax({
								type: "POST",
								contentType: "application/json; charset=utf-8",
								  url: 'administration.product.hierarchy.tree.by.productId?productId='+data.record.productId,
								dataType : 'json',
								complete : function(data){
									console.log('complete');
									closeLoaderIcon();
									},
								success : function(data) {
									treeData = data;			
									Cloning.init(jsonActivityWorkPackageCloningObj);							
									},
								error: function (data){
									console.log('error');
									closeLoaderIcon();
								}
							});
							
						});
						return $img; 
					}
	             },
	             RCN:{
					title : '',
					list : true,
					create : false,
					edit : false,
					width: "10%",
					display:function (data) { 
						//Create an image that will be used to open child table 
						
						if(changeRequestToUsecase == "YES"){
							var $img = $('<img src="css/images/mapping.png" title="Activity Creation from Use Case" data-toggle="modal" />');
						}else{
							var $img = $('<img src="css/images/mapping.png" title="Activity Creation from ChangeRequest" data-toggle="modal" />');
						}
						
						 
						//Open child table when user clicks the image 
						$img.click(function () {
							prodBuildId=data.record.productBuildId;
							actWorkPackageId = data.record.activityWorkPackageId;
							loadChangeRequestsByProductId(productId,prodBuildId,actWorkPackageId);	           				
					    });
					    return $img;
				    }
			     },
			     activityEffortTrackerId:{
		        	title : '',
		        	list : true,
		        	create : false,
		        	edit : false,
		        	width: "5%",
		        	display:function (data) { 
		           		//Create an image for test script popup 
					//	var $img = $('<i class="fa fa-clock-o" title="Edit status and Enter Effort spent for task"></i>');
		           		var $img = $('<i class="fa fa-history showHandCursor" title="Event History"></i>');  
	           			//Open Testscript popup  
	           			$img.click(function () {
	           				addActivityWorkpackageComments(data.record);
	           		  });
	           			return $img;
		        	}
		        },
		         statusAndPolicies:{
		        	title : '',
		        	list : true,
		        	create : false,
		        	edit : false,
		        	width: "5%",
		        	display:function (data) { 
		           		//Create an image for test script popup 
		        		var $img = $('<img src="css/images/workflow.png" title="Configure Workflow" />'); 
	           			$img.click(function () {
	           				workflowId = 0;
	           				entityTypeId = 34;
	           				entityId = 0;
	           				statusPolicies(productId, workflowId, entityTypeId, entityId, data.record.activityWorkPackageId, data.record.activityWorkPackageName, "Activity Workpackage", data.record.statusId);
	           		  });
	           			return $img;
		        	}
		        },
		        workflowId:{
		        	title : 'WorkFlow Template',
		        	list:false, 
	     	  		create:true,
	     	  		edit:false,
					options:function(data){							
						var entityId = 0;
						return 'workflow.master.mapped.to.entity.list.options?productId='+productId+'&entityTypeId=34&entityId='+entityId;
		     		}
		        },
		        attachment: { 
					title: '', 
					list: true,
					edit: false,
					create:false,
					width: "25%",
					display:function (data) {	        		
		           		//Create an image that will be used to open child table 
						var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>'); 
		       			$img.click(function () {
		       				isViewAttachment = false;
		       				var jsonObj={"Title":"Attachments for Activity Workpackage",			          
		       					"SubTitle": 'Activity Workpackage : ['+data.record.activityWorkPackageId+'] '+data.record.activityWorkPackageName,
		    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=34&entityInstanceId='+data.record.activityWorkPackageId,
		    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=34&entityInstanceId='+data.record.activityWorkPackageId+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
		    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
		    	    			"updateURL": 'update.attachment.for.entity.or.instance',
		    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=34',
		    	    			"multipleUpload":true,
		    	    		};	 
			        		Attachments.init(jsonObj);
		       		  });
		       			return $img;
		        	}				
				},
				 auditionHistory:{
					title : '',
					list : true,
					create : false,
					edit : false,
					width: "5%",
					display:function (data) { 
						//Create an image for test script popup 
						var $img = $('<i class="fa fa-search-plus" title="Audit History"></i>');
						$img.click(function () {						
							listActivityWorkPackageAuditHistory(data.record.activityWorkPackageId);
						});
					return $img;
					}
				 },   
        },  // This is for closing $img.click(function (data) { 
        formSubmitting: function (event, data) {
      	  //data.form.find('input[name="activityWorkPackageName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[maxSize[100]]');
      	  	data.form.find('input[name="activityWorkPackageName"]').addClass('class="validate[required,funcCall[validateSpecialCharactersExceptDot]]", custom[maxSize[100]]');
            data.form.find('select[name="ownerId"]').addClass('validate[required]');
            data.form.find('input[name="plannedStartDate"]').addClass('validate[required]');
            data.form.find('input[name="plannedEndDate"]').addClass('validate[required]');
            data.form.validationEngine();
           return data.form.validationEngine('validate');
          }, 
		});
	 $('#jTableContainer').jtable('load');	
	 
	 
	 $("button[role='button']:contains('Close')").click(function () {
	        $('.ui-dialog').filter(function () {
	            return $(this).css("display") === "block";
	        }).find('#ui-error-dialog').dialog('close');
	    });
}

function listActivityWorkPackageAuditHistory(activityWorkPackageId){
	clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=ActivityWorkpackage&sourceEntityId='+activityWorkPackageId;
	var jsonObj={"Title":"ActivityWorkPackage Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10,	    
			"componentUsageTitle":"activityWorkPackageAudit",
	};
	SingleJtableContainer.init(jsonObj);
}

document.onkeydown = function(evt) {
	evt = evt || window.event;
	if (evt.keyCode == 27) {
		if (document.getElementById("div_PopupMain").style.display == 'block') {
			popupClose();
		}
	}
};



/* ----will be replaced in future --------- */ 
function loadChangeRequestsByProductId(productId){	
	loadPopup("div_PopupMainrolechange");
	if(changeRequestToUsecase == "YES"){
		$("#div_PopupMainrolechange .modal-title").text("Create Activity from Use Case");
	}else{
		$("#div_PopupMainrolechange .modal-title").text("Create Activity from CR");
	}
	$("#div_PopupMainrolechange .green-haze").text("Create Activity");
	
	$('#role_types1').empty();
		$.post('unmapped.changerequest.list.by.productId?productId='+productId+'&jtStartIndex=0&jtPageSize=10',function(data) {	
			var ary = (data.Records);
	        $.each(ary, function(index, element) {
	        	$('#role_types1').append('<label><input type="checkbox" name="radio1" class="icheck" id="' + element.itemId + '" data-radio="iradio_flat-grey">'+element.itemName+'</label>');
			});        
	});
}
/*- ----will be replaced in future ---------  */
function submitRadioBtnHandler(){
		var changeRequestIdFromUI = []; 
	   $("input:checkbox[name=radio1]:checked").each(function(){
		   changeRequestIdFromUI.push($(this).attr('id'));
		});
	    if(confirm("Are you sure to create activity based on changeRequest")){
		    $.ajax({
			    type: "POST",
			    data: {changeRequestIdFromUI:changeRequestIdFromUI},
			    url: "add.activity.by.changerequest?changeRequestIdFromUI="+changeRequestIdFromUI+"&prodBuildId="+prodBuildId+"&actWorkPackageId="+actWorkPackageId+"&productId="+productId,
			    success: function(data) {
			    	$.unblockUI();
			    	if(data.Result=="ERROR"){
	 		    		callAlert("Activity Names: "+data.Message+" are already exists");
	 		    	}else{
	 		    		callAlert("Activities saved successfully");			    	
	 		    	}
			    },
			    error: function(data){
			    	callAlert(data.Message);
			    },
			    complete:function(data){
			    }
			});
		  }
	    }


function addActivityWorkpackageComments(record){	

	var entityInstanceName = record.activityWorkPackageName;	
	var entityInstanceId = record.activityWorkPackageId;
	var modifiedById = record.ownerId;
	var currentStatusId = record.statusId;
	var currentStatusName = record.statusName;
	var entityTypeId = 34;//Activity type
	var entityId = 0;
	var actionTypeValue = 0;

	var visibleEventComment=record.visibleEventComment;
	$("#addCommentsMainDiv").modal();		
	if(!visibleEventComment) {
		$('#addComments').hide();//Display only histroy of task Effort
	}
	
	var jsonObj = {
			Title : "Activity Workflow History: ["+entityInstanceId+"] "+entityInstanceName,	
			entityTypeName : 'Activity',		
			entityTypeId : entityTypeId,
			entityInstanceName : entityInstanceName,
			entityInstanceId : entityInstanceId,
			modifiedByUrl : 'common.user.list.by.resourcepool.id',		
			modifiedById : modifiedById,
			raisedDate : new Date(),
			comments : "",
			productId : productId,
			primaryStatusUrl : 'workflow.status.master.option.list?productId='+productId+'&entityTypeId='+entityTypeId+'&entityId='+entityId+'&currentStatusId='+currentStatusId,
			secondaryStatusUrl : 'workflow.entity.secondary.status.master.option.list?productId='+productId+'&entityTypeId='+entityTypeId+'&statusId='+currentStatusId,
			currentStatusId : currentStatusId,
			currentStatusName : currentStatusName,
			secondaryStatusId : secondaryStatusId,
			effortListUrl : 'workflow.event.tracker.list?entityTypeId='+entityTypeId+'&entityInstanceId='+entityInstanceId,
			actionTypeValue : actionTypeValue,
			commentsName : commentsReviewActivity,
			urlToSave : 'workflow.event.tracker.add?productId='+productId+'&entityId='+entityId+'&entityTypeId='+entityTypeId+'&primaryStatusId=[primaryStatusId]&secondaryStatusId=[secondaryStatusId]&effort=[effort]&comments=[comments]&sourceStatusId='+currentStatusId+'&approveAllEntityInstanceIds=[approveAllEntityIds]&entityInstanceId='+entityInstanceId+'&attachmentIds=[attachmentIds]&actionDate=[actionDate]',
			// commentsStatus: "['started','InProgress','Completed']",		
	};
	AddComments.init(jsonObj);
}
</script>
<div id="popUpAvailableDr" class="modal " tabindex="-1" aria-hidden="true" >
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:320px" data-always-visible="1" data-rail-visible1="1">
					<div id="listAllDr"></div>					
					<div class="row" style="margin-top:5px;">
						<div class="radio-toolbar col-lg-6">								
						</div>
					</div> 					
				</div>
			</div>
		</div>
	</div>
</div>
<div><%@include file="cloning.jsp"%></div>
<div><%@include file="dragDropListItems.jsp"%></div>
<script src="js/activityWPSearch.js" type="text/javascript"></script>
<script src="js/activitydrscript.js" type="text/javascript"></script>
<script src="js/importData.js" type="text/javascript"></script> 
<script src="js/viewScript/WorkflowIndicatorDetailView.js" type="text/javascript"></script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>