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

<link href="js/datatable/Editor-1.5.6/css/dataTables.editor.css" rel="stylesheet" type="text/css"></link>
<link href="js/datatable/Editor-1.5.6/css/editor.dataTables.css" rel="stylesheet" type="text/css"></link>
<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>


<!-- END THEME STYLES -->
<style type="text/css">
.logo{
	   margin-left: 45px;
}
#filterStatus > .col-md-4 >.select2me{
	height: 26px;
    padding-top: 2px;
    margin-top: 7px;
    width: 101px !important;
    font-size: 13px;
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
									<span class="caption-subject theme-font bold uppercase">MANAGE DASHBOARD TABS</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
									<span class="caption-helper hide">weekly stats...</span>
								</div>
								<div class="actions" style="padding-top: 4px;padding-left: 5px;">
									<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" 
										onClick="fullScreenHandlerDTVisualization()" data-original-title="" title="FullScreen"></a>
								</div>
							</div>
							<div class="portlet-body">
								<div class="row">
									<div class="col-md-12 toolbarFullScreen">
								        <ul class="nav nav-tabs" id="tabslist">
											<li class="active"><a href="#dashboardVisualization" data-toggle="tab">Visualization Url</a></li>
											<li ><a href="#dashboardTab" id="dashboardTabContent" data-toggle="tab">Dashboard Tab</a></li>
										</ul>
									</div>																
								</div>
								<div id="hidden"></div>
								<div class="tab-content">
									<!-- dashboard Tab -->
									<div class="tab-pane fade" id="dashboardTab" style="max-height:73%;overflow:hidden;overflow-y:auto;padding-left: 25px;">
								        <!-- jtable started -->
								        
								        <div class="jScrollContainerResponsive jScrollContainerFullScreen" style="clear:both;padding-top:10px;position: relative;"> 
								        	
								        	<div id="filterStatus" style="float:right;position: absolute;Z-index: 10;right: 105px;">
										    	<div id="status_dd_status" class="col-md-4">
													<select class="form-control input-small select2me" id="status_ul" style="height: 30px;margin-top: 4px;margin-right: 8px;">
														<option value="2" ><a href="#">ALL</a></option>
														<option value="1" selected><a href="#">Active</a></option>
														<option value="0" ><a href="#">Inactive</a></option>
													</select>
												</div>
			  								</div>
			  								
								      		<div id="jTableContainer" class ="jTableContainerFullScreen hidden"></div>
								      		<div id="dataTableContainerForDashboard"></div>
										</div>
										<div class="cl">&nbsp;</div>
										<!-- jtable ended -->	
									</div>
									<!-- dashboardTab Tab Ends -->
									<!-- dashboardVisualization Tab -->
									<div id="dashboardVisualization" class="tab-pane fade  active in" style="max-height:73%;overflow:hidden;overflow-y:auto;padding-left: 25px;">
										<!-- jtable started -->
									    <div class="jScrollContainerResponsive jScrollContainerFullScreen hidden" style="clear:both;padding-top:10px"> 
									    
									      	<div id="jTableUrlContainer" class ="jTableContainerFullScreen"></div>
										</div>
										<div id="dataTableContainerForVisualization"></div>
										<div class="cl">&nbsp;</div>
										<!-- jtable ended -->	
									</div>
									<!-- dashboardVisualization Tab Ends-->
									</div>
									<div class="cl">&nbsp;</div>
						  	  </div>
						</div>							
				</div>
			</div>
			<!-- END PORTLET-->
		</div>				
	</div>			
	<!-- END PAGE CONTENT INNER -->
</div>

<!-- BEGIN: Convert DataTable ChildTablePopup-->
<div id="dashboardDT_Child_Container" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
					<h4 class="modal-title theme-font">Role Based Tabs</h4>
				</div>
				<div class="modal-body">					
					 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
		 				<div id=dataTableContainerForRoleBasedTabs></div>
		 			</div>					 
				</div>
			</div>
	</div>
</div>
<!-- END: Convert DataTable ChildTablePopup-->

<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp" %></div>			
<!-- END FOOTER -->
<!-- POPUP -->

<div id="div_PopupBackground"></div>
<!-- END POPUP -->
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
custIdVal="";
$(document).on('click', '#trigUploadLogo', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadLogo").trigger("click");
	}
});
jQuery(document).ready(function() {	
	urlToListTabBasedOnStatus = 'administration.dashboardTab.list?status=1';
	//loadTabList(urlToListTabBasedOnStatus);
	
	urlToListUrlBasedOnStatus = 'administration.visualizationUrl.list?status=1';
	//loadUrlList(urlToListUrlBasedOnStatus);
	visualizationDataTableInit("ParentTableTab1");   
	
	$("#menuList li:first-child").eq(0).remove();
   	setBreadCrumb("Manage Tabs");   
   
	$(document).on('change','#status_ul', function() {
		var id = $("#status_ul").find('option:selected').val();				
	    urlToListTabBasedOnStatus = 'administration.dashboardTab.list?status='+id;		
		//loadTabList(urlToListTabBasedOnStatus);
		visualizationDataTable("ParentTableTab2");
	});
	
	$("#dashboardTabContent").click(function(){
	    visualizationDataTable("ParentTableTab2");
	});
   
});

//Delete - JTable
/*function loadTabList(urlToListTabBasedOnStatus){	
	try{
		if($('#jTableContainer').length > 0){
			$('#jTableContainer').jtable("destroy");
		}
	}catch(e){		
	}
	$('#jTableContainer').jtable({
      	title: 'Add/Edit Tabs',
      	selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},
		recordsLoaded: function(event, data) {
        	 $(".jtable-edit-command-button").prop("disabled", true);
         },
		 actions: {
	             listAction:urlToListTabBasedOnStatus,
	             createAction: 'administration.dashboardTab.add',
	             editinlineAction: 'administration.dashboardTab.update'// ,
	         },	 				
		fields: {
		       	 tabId: { 
						key: true,
						type: 'hidden',
						create: false, 
						edit: false, 
						list: false,
						}, 
				tabName: { 
			  	  	title: 'Tab Name',
			  	  	inputTitle: 'Tab Name <font color="#efd125" size="4px">*</font>',
			  	  	width: "20%",
			  	  	list:true,
			  	  	create:true
					},				
				engagementId: { 
		     	  		title: 'Engagement',
		     	  		inputTitle: 'Engagement <font color="#efd125" size="4px">*</font>',
		     	  		list:true,
		     	  		create:true,
		     	  		edit:true,
		     	  		options:function(data){	                	
		     	  			if(data.source =='list'){	     	  			
		                		 return 'administration.testFactory.list.option?testFactoryLabId=0&userId=0&testFactoryId=0&status=1&userRoleId=0';
			                }else if(data.source =='create'){	
			                	data.clearCache();
		                			return 'administration.testFactory.list.option?testFactoryLabId=0&userId=0&testFactoryId=0&status=1&userRoleId=0';	              			
		                		
		                	}	
		                 },
		  			},			  			 	
	  			deployment: { 
  		     	  	title: 'Deployment',
  		     	  	inputTitle: 'Deployment <font color="#efd125" size="4px"></font>',
  		     	  	width: "20%",
  		     	  	list:true,
  		     	  	create:true
  		  		},   		  			 	
		  		orderNo: { 
 		     	  	title: 'Order No',
 		     	  	inputTitle: 'Order No <font color="#efd125" size="4px">*</font>',
 		     	  	width: "20%",
 		     	  	list:true,
 		     	  	create:true
 		  		}, 
				status: {
   						 title: 'Status' ,
   						 inputTitle: 'Status <font color="#efd125" size="4px">*</font>',
   						 width: "10%",  
   						 list:true,
   						 edit:true,
   						 create:true,
   						 type : 'checkbox',
   						 values: {'0' : 'No','1' : 'Yes'},
   				    	 defaultValue: '1'
   		    		},  	
 		  		dashboardTabsRole:{
                	title: '',
                	width: "5%",
                	edit: true,
                	create: false,
           	 	display: function (dashBoardTabs) { 
           		var $img = $('<img src="css/images/list_metro.png" title="Role Based Tabs" />');
           			$img.click(function () { 
           				
           			// ----- Closing child table on the same icon click -----
           			    closeChildTableFlag = closeJtableTableChildContainer($(this), $('#jTableContainer'));
           				if(closeChildTableFlag){
           					return;
           				}            				
           				$('#jTableContainer').jtable('openChildTable', 
           				$img.closest('tr'), 
           					{ 
           					title: 'Add/Edit Role Based Tabs', 
           					 editinline:{enable:true},
           					actions: {
           						listAction:'administration.dashboardTabRoleBased.list?tabId=' + dashBoardTabs.record.tabId , 
           						editinlineAction: 'administration.dashboardTabRoleBased.update',  
           			            createAction: 'administration.dashboardTabRoleBased.add',
								deleteAction: 'administration.dashboardTabRoleBased.user.delete',
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
          						dashBoardTabsId: { 
              			           	type: 'hidden', 
              			           	defaultValue: dashBoardTabs.record.tabId
              			           	},									
								roleBasedId: { 
									key: true,
									type: 'hidden',
									create: false, 
									edit: false, 
									list: false,
									},           			  			 	
              			          productSpecificUserRoleName : {
           							title : 'User Role',
           							list:true,
           							edit:false,
           							create:true,
           							options : 'administration.user.listUserRole',
           							width : "10%",
           						},           						
        						urlId: { 
	        			     	  	title: 'URL',
	        			     	  	inputTitle: 'URL<font color="#efd125" size="4px">*</font>',
	        			     	  	width: "20%",
	        			     	  	list:true,
	        			     	  	create:true,
	        			     	  	options : 'administration.visualizationUrl.list.option',
        			  			},
           			  		  						
	          					 status: {
	          						 title: 'Status' ,
	          						 inputTitle: 'Status <font color="#efd125" size="4px">*</font>',
	          						 width: "10%",  
	          						 list:false,
	          						 edit:true,
	          						 create:true,
	          						 type : 'checkbox',
	          						 values: {'0' : 'No','1' : 'Yes'},
	          				    	 defaultValue: '1'
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
										$img.click(function () {						
											listDashboardTabRoleBasedAuditHistory(data.record.roleBasedId);
										});	
										return $img;
									}
								},  	
           				}, 
      								formSubmitting: function (event, data) { 
										data.form.find('input[name="url"]').addClass('validate[required]');
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
						$img.click(function () {						
							listDashboardTabURLAuditHistory(data.record.tabId);
						});
						return $img;
					}
				}, 		  		
			},

		formSubmitting: function (event, data) {
		data.form.find('input[name="tabName"]').addClass('validate[required], custom[minSize[3]], custom[maxSize[25]]');
		data.form.find('input[name="orderNo"]').addClass('validate[required]');
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

function listDashboardTabURLAuditHistory(tabId){
	clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=DashboardUrls&sourceEntityId='+tabId+'&jtStartIndex=0&jtPageSize=1000';
	//var url='administration.event.list?sourceEntityType=DashboardUrls&sourceEntityId='+tabId;
	var jsonObj={"Title":"DashboardTabURL Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":1000,	    
			"componentUsageTitle":"dashboardTabURLAudit",
	};
	SingleDataTableContainer.init(jsonObj);
	//SingleJtableContainer.init(jsonObj);
}

function listDashboardTabRoleBasedAuditHistory(roleBasedId){
	clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=DashboardTabsRoleBased&sourceEntityId='+roleBasedId+'&jtStartIndex=0&jtPageSize=1000';
	//var url='administration.event.list?sourceEntityType=DashboardTabsRoleBased&sourceEntityId='+roleBasedId;
	var jsonObj={"Title":"DashboardTabRoleBased Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":1000,	    
			"componentUsageTitle":"dashboardTabRoleBasedAudit",
	};
	SingleDataTableContainer.init(jsonObj);
	//SingleJtableContainer.init(jsonObj);
}

//BEGIN: ConvertDataTable - Visualization
var visualizationDT_oTable='';
var editorVisualization='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;
var tableType;
var tabId;

function visualizationDataTable(tableType){
	optionsItemCounter=0;
	optionsResultArr=[];
	if(tableType == "ParentTableTab2"){
		optionsArr = [{id:"engagementType", url:'administration.testFactory.list.option?testFactoryLabId=0&userId=0&testFactoryId=0&status=1&userRoleId=0'}];
	}else if(tableType == "ChildTable"){
		optionsArr = [{id:"userList", url:'administration.user.dashboard.listUserRole'},
		              {id:"urlList", url:'administration.visualizationUrl.list.option'}
		              ];
	}
	visualizationOptions_Container(optionsArr, tableType);
}
		
function visualizationOptions_Container(urlArr, tableType){
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
				 visualizationOptions_Container(optionsArr,tableType);
			 }else{
				visualizationDataTableInit(tableType);	
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

function visualizationDataTableInit(tableType){
	var url,jsonObj={};
	if(tableType=="ParentTableTab1"){
		 url= urlToListUrlBasedOnStatus +'&jtStartIndex=0&jtPageSize=10';
		 jsonObj={"Title":"Visualization",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Visualization",
		};
	}else if(tableType=="ParentTableTab2"){
		 url= urlToListTabBasedOnStatus +'&jtStartIndex=0&jtPageSize=10';
		 jsonObj={"Title":"Dashboard",
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Dashboard",
		};
	}else if(tableType=="ChildTable"){
		 url= 'administration.dashboardTabRoleBased.list?tabId=' + tabId +'&jtStartIndex=0&jtPageSize=10';
		 jsonObj={"Title":"Role Based Tabs",
				 "tabId":tabId,
				"url": url,	
				"jtStartIndex":0,
				"jtPageSize":10000,				
				"componentUsageTitle":"Role Based Tabs",
		};
	}	
	visualizationDataTableContainer.init(jsonObj);
}

var visualizationDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		if(jsonObj.componentUsageTitle == "Visualization"){
 			assignVisualizationDataTableValues(jsonObj, "ParentTableTab1");
 		}else if(jsonObj.componentUsageTitle == "Dashboard"){
 			assignVisualizationDataTableValues(jsonObj, "ParentTableTab2");
 		}else if(jsonObj.componentUsageTitle == "Role Based Tabs"){
 			assignVisualizationDataTableValues(jsonObj, "ChildTable");
 		}
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignVisualizationDataTableValues(jsonObj, tableType){
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
			if(tableType == "ParentTableTab1"){
				visualizationDT_Container(jsonObj);
			}else if(tableType == "ParentTableTab2"){
				dashboardDT_Container(jsonObj);
			}else if(tableType == "ChildTable"){
				roleBasedTabsDT_Container(jsonObj);
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

function visualizationDataTableHeader(){
	var childDivString ='<table id="visualization_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>URL Name</th>'+
			'<th>URL</th>'+
			'<th>Description</th>'+
			'<th>Audit History</th>'+
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
function visualizationDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForVisualization").children().length>0) {
			$("#dataTableContainerForVisualization").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = visualizationDataTableHeader(); 			 
	$("#dataTableContainerForVisualization").append(childDivString);
	
	editorVisualization = new $.fn.dataTable.Editor( {
	    "table": "#visualization_dataTable",
		ajax: "administration.visualizationUrl.add",
		ajaxUrl: "administration.visualizationUrl.update",
		idSrc:  "visualizationId",
		i18n: {
	        create: {
	            title:  "Create a new Visualization URL",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "visualizationId",
            name: "visualizationId",
            "type": "hidden"
        },{
             label: "URL Name",
             name: "urlName",
        },{
             label: "URL",
             name: "url",
        },{
            label: "Description",
            name: "description",
        },{
            label: "Status",
            name: "status", 
            "type": "hidden"
        }        
    ]
	});
	
	visualizationDT_oTable = $("#visualization_dataTable").dataTable( {				 	
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
	     		  $('#visualization_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeVisualizationDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorVisualization },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Visualization',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Visualization',
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
           { mData: "urlName", className: 'editable', sWidth: '30%' },	
           { mData: "url", className: 'editable', sWidth: '30%' },	
           { mData: "description", className: 'editable', sWidth: '30%' },		
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
    	   $('input.editorVisualization-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#visualization_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorVisualization.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#visualization_dataTable tbody').on('click', 'td .auditHistoryImg', function () {
		var tr = $(this).closest('tr');
    	var row = visualizationDT_oTable.DataTable().row(tr);
    	listVisualizationURLAuditHistory(row.data().visualizationId);
	});
	
	$('#visualization_dataTable').on( 'change', 'input.editorVisualization-active', function () {
		editorVisualization
            .edit( $(this).closest('tr'), false )
            .set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
            .submit();
	});
	
	$("#visualization_dataTable_length").css('margin-top','8px');
	$("#visualization_dataTable_length").css('padding-left','35px');		
	
	visualizationDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutVisualizationDT='';
function reInitializeVisualizationDT(){
	clearTimeoutVisualizationDT = setTimeout(function(){				
		visualizationDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutVisualizationDT);
	},200);
}

function fullScreenHandlerDTVisualization(){
	
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
		$('#testFacMode').css('max-height',displaytestFaceModeResponsive(window.innerWidth));
		
		visualizationDTFullScreenHandler(true);
		reInitializeVisualizationDT();
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
		$('#testFacMode').css('max-height','');
		
		reInitializeVisualizationDT();				
		visualizationDTFullScreenHandler(false);			
	}
}

function visualizationDTFullScreenHandler(flag){
	if(flag){
		reInitializeVisualizationDT();
		$("#visualization_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
	}else{
		reInitializeVisualizationDT();
		$("#visualization_dataTable_wrapper .dataTables_scrollBody").css('max-height','450px');
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
//END: ConvertDataTable - Visualization

//BEGIN: ConvertDataTable - Dashboard - Tab2
var dashboardDT_oTable='';
var editorDashboard='';
function dashboardDataTableHeader(){
	var childDivString ='<table id="dashboard_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Tab Name</th>'+
			'<th>Engagement</th>'+
			'<th>Deployment</th>'+
			'<th>Order No</th>'+
			'<th>Status</th>'+
			'<th>Role Based Tabs</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function dashboardDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForDashboard").children().length>0) {
			$("#dataTableContainerForDashboard").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = dashboardDataTableHeader(); 			 
	$("#dataTableContainerForDashboard").append(childDivString);
	
	editorDashboard = new $.fn.dataTable.Editor( {
	    "table": "#dashboard_dataTable",
		ajax: "administration.dashboardTab.add",
		ajaxUrl: "administration.dashboardTab.update",
		idSrc:  "tabId",
		i18n: {
	        create: {
	            title:  "Create a new Dashboard",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "tabId",
            name: "tabId",
            "type": "hidden",
         },{
             label: "Tab Name",
             name: "tabName",
         },{
             label: "Engagement",
             name: "engagementId",
             options: optionsResultArr[0],
             "type": "select",
         },{
            label: "Deployment",
            name: "deployment",
         },{
            label: "Order No",
            name: "orderNo",
         },{
            label: "Status",
            name: "status",
            "type": "hidden",
         }        
    ]
	});
	
	dashboardDT_oTable = $("#dashboard_dataTable").dataTable( {				 	
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
	     		  $('#dashboard_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeDashboardDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorDashboard },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Dashboard',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Dashboard',
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
           { mData: "tabName", className: 'editable', sWidth: '20%' },	
           { mData: "engagementId", className: 'editable', sWidth: '20%', editField: "engagementId",
	       		mRender: function (data, type, full) {
	       			data = optionsValueHandler(editorDashboard, 'engagementId', full.engagementId);
		            return data;
	            },
			},	
           { mData: "deployment",  className: 'editable', sWidth: '20%' },
           { mData: "orderNo",  className: 'editable', sWidth: '10%' },
           { mData: "status",
               mRender: function (data, type, full) {
             	  if ( type === 'display' ) {
                         return '<input type="checkbox" class="editorDashboard-active">';
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
		       				'<img src="css/images/list_metro.png" class="roleBasedTabImg" title="Role Based Tabs" />'+
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
    	   $('input.editorDashboard-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	// Activate an inline edit on click of a table cell
	$('#dashboard_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorDashboard.inline( this, {
            submitOnBlur: true
        } );
	});
	
	 $('#dashboard_dataTable').on( 'change', 'input.editorDashboard-active', function () {
		 editorDashboard
	            .edit( $(this).closest('tr'), false )
	            .set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
	            .submit();			
    });
	 
	$("#dashboard_dataTable_length").css('margin-top','8px');
	$("#dashboard_dataTable_length").css('padding-left','35px');		
	
	$('#dashboard_dataTable tbody').on('click', 'td .roleBasedTabImg', function () {
		var tr = $(this).closest('tr');
    	var row = dashboardDT_oTable.DataTable().row(tr);
		tabId = row.data().tabId;
		$('#dashboardDT_Child_Container').modal();
		$(document).off('focusin.modal');
		visualizationDataTable("ChildTable");
	});
	
	$('#dashboard_dataTable tbody').on('click', 'td .auditHistoryImg', function () {
		var tr = $(this).closest('tr');
    	var row = dashboardDT_oTable.DataTable().row(tr);
    	listDashboardTabURLAuditHistory(row.data().tabId);
	});
	
	dashboardDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutDashboardDT='';
function reInitializeDashboardDT(){
	clearTimeoutDashboardDT = setTimeout(function(){				
		dashboardDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDashboardDT);
	},200);
}
//END: ConvertDataTable - Dashboard - Tab2

//BEGIN: ConvertDataTable - RoleBasedTabs - ChildTable
var roleBasedTabsDT_oTable='';
var editorRoleBasedTabs='';
function roleBasedTabsDataTableHeader(){
	var childDivString ='<table id="roleBasedTabs_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>User Role</th>'+
			'<th>URL</th>'+
			'<th>Audit History</th>'+
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
function roleBasedTabsDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForRoleBasedTabs").children().length>0) {
			$("#dataTableContainerForRoleBasedTabs").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = roleBasedTabsDataTableHeader(); 			 
	$("#dataTableContainerForRoleBasedTabs").append(childDivString);
	
	editorRoleBasedTabs = new $.fn.dataTable.Editor( {
	    "table": "#roleBasedTabs_dataTable",
		ajax: 'administration.dashboardTabRoleBased.add',
		ajaxUrl: 'administration.dashboardTabRoleBased.update',
		idSrc:  "roleBasedId",
		i18n: {
	        create: {
	            title:  "Create a new Role based tab",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "dashBoardTabsId",
            name: "dashBoardTabsId",
			"type": "hidden",
			"default":jsonObj.tabId,
			
        },{
            label: "User Role",
            name: "productSpecificUserRoleId",
            options: optionsResultArr[0],
            "type":"select",
        },{
            label: "URL",
            name: "urlId",
            options: optionsResultArr[1],
            "type":"select",
        },{
            label: "status",
            name: "status",
			"type": "hidden",
			"def":1,
        }       
    ]
	});
	
	roleBasedTabsDT_oTable = $("#roleBasedTabs_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [2]; // search column TextBox Invisible Column position
	     		  $('#roleBasedTabs_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeRoleBasedTabsDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorRoleBasedTabs },	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Role Based Tabs',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Role Based Tabs',
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
           /* { mData: "productSpecificUserRoleId", className: 'disableEditInline', sWidth: '45%', editField: "productSpecificUserRoleId",
        	   mRender: function (data, type, full) {
	       			data = optionsValueHandler(editorRoleBasedTabs, 'productSpecificUserRoleId', full.productSpecificUserRoleId);
		            return data;
	            },
			}, */	
			{ mData: "productSpecificUserRoleId", className: 'editable', sWidth: '10%', editField: "productSpecificUserRoleId",
            	mRender: function (data, type, full) {
		           	 if (full.action == "create" || full.action == "edit"){
		           		data = optionsValueHandler(editorRoleBasedTabs, 'productSpecificUserRoleId', full.productSpecificUserRoleId);
		           	 }else if(type == "display"){
		           		data = full.productSpecificUserRoleName;
		           	 }	           	 
		             return data;
	             },               	
            },
           { mData: "urlId", className: 'editable', sWidth: '45%', editField: "urlId",
	       		mRender: function (data, type, full) {
	       			data = optionsValueHandler(editorRoleBasedTabs, 'urlId', full.urlId);
		            return data;
	            },
			},
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border:none; background-color:transparent; outline:none;margin-left:5px;">'+
 	       				'<i class="fa fa-search-plus auditHistoryImg" title="Audit History"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
       ],       
       rowCallback: function ( row, data ) {
    	   $('input.editorRoleBasedTabs-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	// Activate an inline edit on click of a table cell
	$('#roleBasedTabs_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorRoleBasedTabs.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#roleBasedTabs_dataTable tbody').on('click', 'td .auditHistoryImg', function () {
		var tr = $(this).closest('tr');
    	var row = roleBasedTabsDT_oTable.DataTable().row(tr);
    	$('#div_SingleDataTableSummary').css({'z-index':'10062','top':'2%'});
    	listDashboardTabRoleBasedAuditHistory(row.data().roleBasedId);
	});
	
	$("#roleBasedTabs_dataTable_length").css('margin-top','8px');
	$("#roleBasedTabs_dataTable_length").css('padding-left','35px');		
	
	roleBasedTabsDT_oTable.DataTable().columns().every( function () {
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

var clearTimeoutRoleBasedTabsDT='';
function reInitializeRoleBasedTabsDT(){
	clearTimeoutRoleBasedTabsDT = setTimeout(function(){				
		roleBasedTabsDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutRoleBasedTabsDT);
	},200);
}
//END: ConvertDataTable - RoleBasedTabs

//Delete - JTable
/*function loadUrlList(urlToListUrlBasedOnStatus){	
	try{
		if($('#jTableUrlContainer').length > 0){
			$('#jTableUrlContainer').jtable("destroy");
		}
	}catch(e){
		
	}
	$('#jTableUrlContainer').jtable({

      	title: 'Add/Edit VisualizationURL',
      	selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},
		recordsLoaded: function(event, data) {
        	 $(".jtable-edit-command-button").prop("disabled", true);
         },
		actions: { 
			listAction: urlToListUrlBasedOnStatus,
			createAction: 'administration.visualizationUrl.add',
			editinlineAction: 'administration.visualizationUrl.update',
				}, 	 				
		fields: { 
			visualizationId: { 
				type: 'hidden', 
				key: true, 
				create: false, 
               edit: false, 
				list: false
			},			
			urlName: { 
				title: 'URL Name',
				inputTitle: 'URL Name <font color="#efd125" size="4px">*</font>',
				list: true,
				width: "20%"
			},
			url: { 
				title: 'URL' ,
				inputTitle: 'URL <font color="#efd125" size="4px">*</font>',
				list: true,
				width: "35%"          	
						},  
			description: { 
    				title: 'Description' , 
    				inputTitle: 'Description<font color="#efd125" size="4px"></font>',
    				edit: true, 
					list: true,
    				width: "35%",    				
    		},
    		status: {
					 title: 'Status' ,
					 inputTitle: 'Status <font color="#efd125" size="4px">*</font>',
					 width: "10%",  
					 list:false,
					 edit:true,
					 create:true,
					 type : 'checkbox',
					 values: {'0' : 'No','1' : 'Yes'},
			    	 defaultValue: '1'
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
					$img.click(function () {						
						listVisualizationURLAuditHistory(data.record.visualizationId);
					});
					return $img;
				}
			},	
		},
		formSubmitting: function (event, data) {
		data.form.find('input[name="urlName"]').addClass('validate[required]');
		data.form.find('input[name="url"]').addClass('validate[required]'); 
		data.form.validationEngine();
		return data.form.validationEngine('validate');
		}, 
		//Dispose validation logic when form is closed
		formClosed: function (event, data) {
		data.form.validationEngine('hide');
		data.form.validationEngine('detach');
		}	    		
     });	 
	 $('#jTableUrlContainer').jtable('load');	
}*/

function listVisualizationURLAuditHistory(visualizationId){
	clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=DashboardVisualizationUrls&sourceEntityId='+visualizationId+'&jtStartIndex=0&jtPageSize=1000';
	//var url='administration.event.list?sourceEntityType=DashboardVisualizationUrls&sourceEntityId='+visualizationId;
	var jsonObj={"Title":"VisualizationURL Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":1000,	    
			"componentUsageTitle":"visualizationURLAudit",
	};
	
	SingleDataTableContainer.init(jsonObj);
	//SingleJtableContainer.init(jsonObj);
}

</script>						
<!-- END JAVASCRIPTS -->
<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
</body>
<!-- END BODY -->
</html>