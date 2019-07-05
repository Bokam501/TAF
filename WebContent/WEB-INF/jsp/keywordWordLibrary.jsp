<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

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
<meta charset="utf-8">
<link rel="shortcut icon" href="css/images/logo_new.png">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">

<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link href="js/Scripts/validationEngine/validationEngine.jquery.css" rel="stylesheet" type="text/css" />

<!-- END THEME STYLES -->
<link href="css/bootstrap-select.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet"	type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet" type="text/css"></link>
<link rel="stylesheet" type="text/css" href="css/bootstrap-datetimepicker.min.css"></link>
<link rel="stylesheet" href="css/iosOverlay.css" type="text/css" media="all">
<!-- For DataTable -->
<!-- <link rel="stylesheet" href="js/datatable/jquery.dataTables.css" type="text/css" media="all"> -->
<!-- <link rel="stylesheet" href="js/datatable/dataTables.tableTools.css" type="text/css" media="all"> -->
<!-- <link rel="stylesheet" href="css/customizeDataTable.css" type="text/css" media="all"> -->
<!-- <link rel="stylesheet" href="https://code.jquery.com/ui/1.11.3/themes/smoothness/jquery-ui.css" type="text/css" media="all"> -->

<link rel="stylesheet" href="js/Scripts/star-rating/jquery.rating.css" type="text/css">
<link rel="stylesheet" href="js/datatable/jquery-ui.css" type="text/javascript" media="all">
<link href="css/customStyle.css" rel="stylesheet" type="text/css"></link>
<link rel="stylesheet" href="js/datatable/dataTables.jqueryui.min.css" type="text/javascript" media="all">
<style type="text/css">
.expanded-group{
				background: url("media/images/minus.jpg") no-repeat scroll left center transparent;
				padding-left: 15px !important
			}

			.collapsed-group{
				background: url("media/images/plus.jpg") no-repeat scroll left center transparent;
				padding-left: 15px !important
			}
			.item-download{
				float:right;
				height:45px;
				width:45px;
			}
			.nav-pills>li.active>a, .nav-pills>li.active>a:focus, .nav-pills>li.active>a:hover {
				background-color:#205ab6 !important;
			}
			.nav-pills>li>a, .nav-pills>li>a:focus, .nav-pills>li>a:hover{
				color:#205ab6;
				border:1px solid #205ab6;
			}
			.nav>li>a:focus, .nav>li>a:hover {
			    background-color: #CED5F3 !important;
			}
			.nav-tabs, .nav-pills{
				margin:0px 10px 10px 10px;
			}
			.nav-action>li>a{
				padding:5px 8px !important;
			}
			.approve_btn,.reject_btn,.review_btn{
				float:left;
				padding:5px;
				border:1px solid blue;
				font-size:10px;
				
			}
			.review_btn{
				border-width:1px 0;
				background-color:yellow;
			}
			.approve_btn{
				background-color:orange;
			}
			.reject_btn{
				background-color:#70B645;
			}
			
			.command_btn{
				float:left;
				text-align:center;
				width:100%;
				margin:10px;
			}
			
			.btn-default:active, .btn-default.active {
				background-color:#205ab6 !important;
				color:#fff !important;
			}
			.btn-default:hover, .btn-default:focus, .btn-default:active, .btn-default.active {
				color: #fff !important;
				background-color: #205ab6 !important;
				border-color: #205ab6 !important;
			}
			.btn-default-custom-active,.btn-default-custom-active:hover, .btn-default-custom-active:focus, .btn-default-custom-active:active{
				color: #fff !important;
				background-color: #205ab6 !important;
				border-color:#205ab6 !important;
				
			}
			
			.btn-default-custom-inactive,.btn-default-custom-inactive:hover, .btn-default-custom-inactive:focus, .btn-default-custom-inactive:active{
				color: #333 !important;
				background-color: #fff !important;
				border-color:#205ab6 !important;
			}
			
			.btn-default-custom:hover, .btn-default-custom:focus, .btn-default-custom:active {
				/*color: #333 !important;
				background-color: #fff !important;
				border-color:#205ab6 !important;*/
				cursor:default;
			}
			
			.btn{
				/* border:1px solid #205ab6 !important; */
				margin:3px;	
			}
			.btn-default{
				font-size:10px !important;
			}
			.btn-group{
				margin:1px;
			}
			
			.template_download{
				background: url("css/images/template-download.jpg") no-repeat;
				margin:7px 2px 0 2px;
			}
			.guidelines_download{
				background: url("css/images/file-download.png") no-repeat;
				margin:7px 2px 0 2px;
			}
			.attachment{
				float:left;
			   background: url("css/images/attachment.png") no-repeat;
			   background-position:center;	
			   width:100%;
			   height:100%;
			}
			.attach_file{
				
				text-align:center;
				width:100%;
				height:100%;
				float:left;
			}
			.attach_file img{
				cursor:pointer;
				
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
<%-- 		<div><%@include file="treeStructureSidebar.jsp"%></div> --%>
		 <div><%@include file="postHeader.jsp"%></div> 
		<div id="reportbox" style="display: none;"></div>
		<!-- BEGIN PAGE CONTENT -->
		<div class="page-content">
			<div class="container-fluid">
				<!-- BEGIN PAGE CONTENT INNER -->
				<div class="row margin-top-10" id="toAnimate">
					<div class="col-md-12">
						<!-- BEGIN PORTLET-->
						<div class="portlet light ">
							<div class="portlet-title" style="margin-bottom:0px;">
							 
								<!-- <div class="caption caption-md">
									<span id="header" class="caption-subject theme-font bold uppercase">My Test Cases
									</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
										
								</div> -->
								
							<!-- </div> -->
							 <div class="portlet-body form">								
							 <div class="jScrollContainerResponsive" style="clear:both;padding-top: 5px;">
															<div id="jTableContainerForMyKeywords"></div>
														</div>
                                                    
								<!--<div id="hidden"></div>
									Testcases 
									<div id="Testcases" >
									
											 <div class="row">
											   
												
												<a href="javascript:download('Template');" class="template_download item-download" title="Download Template"></a>
												<a href="javascript:download('Guidelines');" class="guidelines_download item-download" title="Download Guidelines"></a>
												
												<img  class="item-download" src='css/images/template-download.jpg' style="padding-top:5px;" title="Download Template" onclick="download('Template');"/>
										<img  class="item-download" src='css/images/file-download.png' style="padding-top:8px;" title="Download Guidelines" onclick="download('Guidelines');" />
											  <ul class="nav nav-pills nav-action" style="float:right;padding-top:10px;">
                                                    <li  class="active">
                                                        <a href="#tab11" data-toggle="tab" aria-expanded="true" onclick="myKeywords();">My Keywords</a>
                                                    </li>
													<li class="">
                                                        <a href="#tab15" data-toggle="tab" aria-expanded="false" onclick="myTestCaseList();">My TestCases</a>
                                                    </li>
                                                    <li class="">
                                                        <a href="#tab12" data-toggle="tab" aria-expanded="false" onclick="approvedTestCasesList();">My Pending Action</a>
                                                    </li>
                                                     <li class="">
                                                        <a href="#tab13" data-toggle="tab" aria-expanded="false" onclick="referBackList();">My Refer Back Items</a>
                                                    </li>
                                                    <li class="">
                                                        <a href="#tab14" data-toggle="tab" aria-expanded="false" onclick="listOfJobs();">My Contribution</a>
                                                    </li> 
                                                    
                                                </ul> 													 
											</div>
                                           								
											<div class="tabbable tabbable-tabdrop">

                                                <div class="tab-content" style="clear:both;">
                                                    <div class="tab-pane active" id="tab15">
														<div class="jScrollContainerResponsive" style="clear:both;padding-top: 5px;">
															<div id="jTableContainerForStatus"></div>
														</div>
                                                    </div>
                                                     <div class="tab-pane" id="tab12">
                                                       <div class="jScrollContainerResponsive" style="clear:both;padding-top: 5px;">
															<div id="jTableContainertest"></div>
														</div>
                                                    </div>
                                                     <div class="tab-pane" id="tab13">
                                                       <div class="jScrollContainerResponsive" style="clear:both;padding-top: 5px;">
															<div id="jTableContainerreferback"></div>
														</div>
                                                    </div>
                                                  
													<div class="tab-pane active" id="tab11">
                                                       <div class="jScrollContainerResponsive" style="clear:both;padding-top: 5px;">
															<div id="jTableContainerForMyKeywords"></div>
														</div>
                                                    </div>
                                                    
                                                  
                                                </div>
                                            </div>
											
											End Container Box
											
										</form>
									</div> -->
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
	
	



<!-- four drop downs -->

	
	
		
<%-- <div><%@include file="productManagePlan.jsp"%></div>	 --%>
<div><%@include file="treePopupLayout.jsp"%></div>



<div><%@include file="singleJtableContainer.jsp" %></div>
 
<%-- <div><%@include file="testCaseScriptlessExecution.jsp"%></div> --%>


<%-- <div><%@include file="cloneBuild.jsp"%></div> --%>

<%-- <div><%@include file="addComments.jsp"%></div>
<div><%@include file="testCaseOverAllView.jsp"%></div> --%>
<%-- <div><%@include file="attachments.jsp"%></div> --%>
	<div id="div_PopupBackground"></div>
	<!-- Popup Ends -->
			
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
	<script src="js/bootstrap-datetimepicker.min.js" type="text/javascript" ></script>
	<script src="js/moment.min.js" type="text/javascript"></script>
	<script src="js/daterangepicker.js" type="text/javascript"></script>
	<script src="js/components-pickers.js" type="text/javascript"></script>

	<!-- the jScrollPane script -->
	<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

	<!-- Include jTable script file. -->
	<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>

	<!-- Plugs ins for jtable inline editing -->
	<script type="text/javascript"
		src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
	<script type="text/javascript"
		src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>
	<!-- <script type="text/javascript" src="js/Scripts/popup/jquery.jtable.js"></script> -->

	<!-- Validation engine script file and english localization -->
	<script type="text/javascript"
		src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
	<script type="text/javascript"
		src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>

	<script type="text/javascript" src="js/draganddrop/Sortable.js"></script>
	<script type="text/javascript" src="js/draganddrop/ng-sortable.js"></script>
	<!-- For DataTable -->


	<script src="js/datatable/jquery.dataTables.min.js" type="text/javascript"></script>
	<script src="js/datatable/dataTables.jqueryui.min.js" type="text/javascript"></script>
	<script src="js/Scripts/popup/jquery.jtable.toolbarsearch.js" type="text/javascript"></script>
	
	<script type="text/javascript">	
	
	
var key ='';
var nodeType='';
var addorno="yes";
var userRoleId='-1';
var userId='-1';
var title='';
var date = new Date();
var timestamp = date.getTime();
var filterVal;
var projectCodeId = -1;
var domainCategoryId=-1;
var urlToGetTestCasesOfSpecifiedProductId;

//ATSG
var idUnique;
var scriptsForUnique;
var scriptTypeUnique = "GHERKIN";
var testEngineUnique;
var testStepOption;
var scriptExecutionType;
var testrunPlanIdForTestCaseExecution ='';
var runConfigCheckBoxArrVal = [];
var scriptTestCaseName = "TestCase";
var scriptTestSuiteName = "TestSuite";
var scriptViewName = "View";
var scriptDownloadName = "Download";
var testCaseId;
var testCaseName;
var productTypeName = "WEB" ;
var scriptClosePopupFlag = true ; // for automatic save script
var editStoryFalg = false; // 
var objectRepositoryId = -1;
var testDataId = -1;
var atsgId = -1;
var totalRepository = 0;
var totalTestData = 0;
var projectName;
var date = new Date();
var timestamp = date.getTime();
var filterVal;
var domainGroupId=-1;
var domainCategoryId=-1;
var urlToGetTestCasesOfSpecifiedProductId;
var referedBackItems="ReferedBackItems";
var myPendingaction="MyPendingaction";

jQuery(document).ready(function() {
	/* alert(${propertyConfigurer['my.string.from.prop.file']}); */
	/* alert(${ilcmProps['seleniumProjectDirectory']}); */
   QuickSidebar.init(); // init quick sidebar
   ComponentsPickers.init();
   setBreadCrumb("Admin");
  /*  setBreadCrumb("");   */
  /* 
  var modeSelection ="${seleniumProjectDirectory}";
  alert(modeSelection); */
   userRoleId='<%=session.getAttribute("roleId")%>';
   userId='<%=session.getAttribute("ssnHdnUserId")%>';
 
   myKeywords();
    
   // projectCodeId = $("#projectCodes_ul").find('option:selected').val();
	/* var productVersionOptionName = $("#projectCodes_ul").find('option:selected').text(); */
   
   
   
   
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

function popupClose() {	
	$("#div_PopupMain").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}
function popupTestcaseClose() {	
	$("#div_PopupTestcase").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}

/* Load Poup function */
function loadPopup(divId) {
	$("#" + divId).fadeIn(0500); // fadein popup div
	$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
														// IE7, IE8
	$("#div_PopupBackground").fadeIn(0001);
}

document.onkeydown = function(evt) {
	evt = evt || window.event;
	if (evt.keyCode == 27) {
		if (document.getElementById("div_PopupMain").style.display == 'block') {
			popupClose();
		}
	}
}





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
		}		









function  downlaodTCAttachmentByName(jsonObj){
	//alert(attachmentsArr[index]);
	alert(jsonObj.attachment);
	
}

		function approvedTestCasesList(){
			var referedBackItems="ReferedBackItems";
			var myPendingaction="MyPendingaction";
		try{
				//alert("productId----"+productId);
				if ($('#jTableContainertest').length>0) {
					 $('#jTableContainertest').jtable('destroy'); 
				}
				}catch(e){}
				//init jTable
				 $('#jTableContainertest').jtable({
				title: 'My Pending Action',
			        	paging: true, //Enable paging
			            pageSize: 10, //Set page size (default: 10)
			            editinline:{enable:true},
						recordsLoaded: function(event, data) {
							setCheckBoxValues();
						},
						actions: { 
			        		// listAction:'userAction.list?userId='+userId,	 
			        				listAction:'mytestcase.list?filter=MyPendingTestCases',
			        		 editinlineAction:'testcase.tags.update?tsetCaseId=null',
			        		
			        	     recordUpdated:function(event, data){
			        	        	$('#jTableContainertest').find('.jtable-main-container').jtable('reload');
			        	        },
			        	     recordAdded: function (event, data) {
			        	         	$('#jTableContainertest').find('.jtable-main-container').jtable('reload');
			        	        } 
			            }, 
						fields: { 
							
								/* otherDomainCategories:{
									title: 'Domains', 
									list:true,
									edit:false,
									width: "10%"
									},
								  domainMapping:{ 
							          	title: '',
							          	width: "5%",
							          	edit: true,
							          	create: false,
							          
							     	 	display: function (data) { 
							     		//Create an image that will be used to open child table 
							     			var $img = $('<img src="css/images/mapping.png" title="Add/Edit Domain(S)" data-toggle="modal" data-target="#dragListItemsContainer" />');
							     			//Open child table when user clicks the image 
							     			$img.click(function () {
							     				$img = manageCompetenciesMapping($img, data.record.testCaseId,data.record.testCaseName);
							     			});
							     			return $img; 
							         	} 
							 	  	}, 
							testCaseId:{
								title: 'Id', 
								list:true,
								edit:false,
								width: "10%"
								},
								testCaseName:{
								title: 'Test Case', list:true,edit:false,width:"10%",display: function (data) { 
					    			return '<p ><a href="javascript:getTestCaseDetails('+data.record.testCaseId+',1)">'+data.record.testCaseName+'</a></p>';
				                }, 
								},*/
								keywordLibId:{
									title: 'Id', 
									list:false,
									edit:false,
									},
									keywordPhrase :{
										title: 'Keyword', 
										list:true,
										edit:false,
										},
								 type: { 
			                    		title : 'Type',
			          				edit: false,
			          				list:true,
			          				create:true,
			          				options:{
			 	                    	  'ScriptGeneration':'Script Generation',
			 	                    	  'Scriptexecution' : 'Script Execution',
			 	                      }
			          			},
			          			testToolId : {
			 				 		title : 'Test Engine',
			 						list : true,
			 						create : true,
			 						edit : false,
			 						options : function(data) {
			 							return 'common.list.testToolMaster.option';
			 						}
			 					},
			 					languagID : {
			 				 		title : 'Language',
			 						list : true,
			 						create : false,
			 						edit : false,
			 						options : function(data) {
			 							return 'common.list.languages';
			 						}
			 					},
			 					className: {
			  	                     title: 'Class Name',
			  	                     list: true,
			  	                     edit : false,
			  	                     create: false,
			      			   },
			      			   	   
			      			  
			      			 binary: {
			                       title: 'Binary',
			                       create : true,
			                       edit : false,
			                       input: function(data) {
			                        var   html = '<input type ="file" id="input-image" name="userfile"  />';
			                           return html;
			                       }
			          	  	   },
								isApprove:{
								title: 'Actions', list:true,edit:false, type : 'checkbox',width:"26%",
									 values: {'1' : 'Yes','0' : 'No'},
									 display:function(data)
						    		 {
						        	   if(data.record){ //3 is 
		     								 var display_checkbox_value='<div class="btn-group" data-toggle="buttons">';
									      var flag=false;
						        		  var approveStatus = data.record.isApprove;
						        		  var reviewStatus = data.record.isReferBack;
						        		  var rejectStatus = data.record.isReject;
						        		  if(approveStatus==1|| reviewStatus==1 || rejectStatus==1){
						        			  flag=true;
						        		  }
						        		  if(flag){
						        			  if(approveStatus == 0){
							        		 		display_checkbox_value+='<label class="btn btn-default" ><input type="checkbox" class="toggle switch_checkbox" title="click to check" id="approve" val='+data.record.isApprove+' >  Approve </label>';  
											}else if( approveStatus == 1){
							        	  			 display_checkbox_value+='<label class="btn btn-default" ><input type="checkbox" class="toggle switch_checkbox" checked title="click to check"  id="approve" val='+data.record.isApprove+' > Approve </label>';	
											}

											 						
											if(reviewStatus == 0){ // 4 is review
							        		 		 display_checkbox_value+='<label class="btn btn-default"  ><input class="toggle switch_checkbox" type="checkbox"  title="click to check" id="review" val='+data.record.isReview+' > Refer Back </label>';  
											}else if( reviewStatus == 1){
							        	  			  display_checkbox_value+='<label class="btn btn-default" ><input class="toggle switch_checkbox" type="checkbox"  checked title="click to check" id="review" val='+data.record.isReview+' > Refer Back </label>';	
											}

											  						
											if(rejectStatus == 0){    // 5 is reject
							        		 		display_checkbox_value+='<label class="btn btn-default"  ><input class="toggle switch_checkbox" type="checkbox"  title="click to check" id="reject" val='+data.record.isReject+' > Reject </label>';  
											}else if( rejectStatus == 1){
							        	  			 display_checkbox_value+='<label class="btn btn-default" ><input class="toggle switch_checkbox" type="checkbox"  checked title="click to check" id="reject" val='+data.record.isReject+' > Reject </label>';	
											}
											
						        		  }else{
						        			  if(approveStatus == 0){
							        		 		display_checkbox_value+='<label class="btn btn-default" onclick="updateDomainSMEAction(1,'+data.record.keywordLibId+',3,\''+myPendingaction+'\')"><input type="checkbox" class="toggle switch_checkbox" title="click to check" id="approve" val='+data.record.isApprove+' >  Approve </label>';  
											}else if( approveStatus == 1){
							        	  			 display_checkbox_value+='<label class="btn btn-default" onclick="updateDomainSMEAction(0,'+data.record.keywordLibId+',3,\''+myPendingaction+'\')"><input type="checkbox" class="toggle switch_checkbox" checked title="click to check"  id="approve" val='+data.record.isApprove+' > Approve </label>';	
											}

											 						
											if(reviewStatus == 0){ // 4 is review
							        		 		 display_checkbox_value+='<label class="btn btn-default" onclick="updateDomainSMEAction(1,'+data.record.keywordLibId+',4,\''+myPendingaction+'\')" ><input class="toggle switch_checkbox" type="checkbox"  title="click to check" id="review" val='+data.record.isReview+' > Refer Back </label>';  
											}else if( reviewStatus == 1){
							        	  			  display_checkbox_value+='<label class="btn btn-default" onclick="updateDomainSMEAction(0,'+data.record.keywordLibId+',4,\''+myPendingaction+'\')"><input class="toggle switch_checkbox" type="checkbox"  checked title="click to check" id="review" val='+data.record.isReview+' > Refer Back </label>';	
											}

											  						
											if(rejectStatus == 0){    // 5 is reject
							        		 		display_checkbox_value+='<label class="btn btn-default"  onclick="updateDomainSMEAction(1,'+data.record.keywordLibId+',5,\''+myPendingaction+'\')"><input class="toggle switch_checkbox" type="checkbox"  title="click to check" id="reject" val='+data.record.isReject+' > Reject </label>';  
											}else if( rejectStatus == 1){
							        	  			 display_checkbox_value+='<label class="btn btn-default"  onclick="updateDomainSMEAction(0,'+data.record.keywordLibId+',5,\''+myPendingaction+'\')"><input class="toggle switch_checkbox" type="checkbox"  checked title="click to check" id="reject" val='+data.record.isReject+' > Reject </label>';	
											}
											
						        		  }
										
										display_checkbox_value+="</div>"
										
										return display_checkbox_value;
						        	   }
						           	}
								},
								attachments:{
									title: 'Attachments',
									list:true,
									width:"24%",
									display:function (data) {	
									 if(data.record.binary == null){
										return '<div>No Attachments</div>';
									}else{
										var $img = $('<img src="css/images/rsz_download.png" title="Download"  />');
						     			//Open child table when user clicks the image 
						     			$img.click(function () {
						     				//$img = manageCompetenciesMapping($img, data.record.testCaseId);
						     				var urlfinal="rest/download/testcase?fileName="+data.record.binary;
						     			  	parent.window.location.href=urlfinal;
						     			});
						     			return $img; 
									} 
										
									}
									}
								/* comments:{
								title: 'Comments',width:"24%",
								display:function (data) {	
										record_comments=(data.record.comments!='' && data.record.comments!=null)?data.record.comments:'';
											return '<div style="width:100%;height:100%;" onclick="changeTextarea(this,'+data.record.testCaseId+',\''+myPendingaction+'\')">'+record_comments+'</div>';
									}
								}, */
								/* tagName:{
									title: 'Tag', list:true,edit:false,width:"24%",
									display:function (data) {	
										tag=(data.record.tagName!='' && data.record.tagName!=null)?data.record.tagName:'';
											//return '<div style="width:100%;height:100%;" onclick="changeTagTextarea(this,'+data.record.testCaseId+',\''+myPendingaction+'\')">'+tag+'</div>';
										return '<div style="width:100%;height:100%;" >'+tag+'</div>';
									}
								},
								attachments:{
									title: 'Attachments',
									list:true,
									width:"24%",
									display:function (data) {	
									
									var attachment_file=(data.record.attachments!=null)?data.record.attachments:'';
									
									return '<div class="attach_file"><img src="css/images/attachment.png"  onclick="upload_files('+data.record.testCaseId+');"   /><br/><span id="fileName_'+data.record.testCaseId+'">'+attachment_file+'</span></div><input id="upload_files_'+data.record.testCaseId+'" type="file" name="upload_files_'+data.record.testCaseId+'" style="display:none;" onchange="get_upload_file_name('+data.record.testCaseId+',\''+myPendingaction+'\')">';

									
									if(data.record.attachments!=null){
										return '<a href="javascript:upload_files('+data.record.testCaseId+');" class="attachment" title="Attachments"/><div style="float:left;">'+data.record.attachments+'</div><span id="fileName_'+data.record.testCaseId+'"></span><input id="upload_files_'+data.record.testCaseId+'" type="file" name="upload_files_'+data.record.testCaseId+'" style="display:none;" onchange="get_upload_file_name('+data.record.testCaseId+')">';
									}else{
										return '</div><a href="javascript:upload_files('+data.record.testCaseId+');" class="attachment" title="Attachments"/></span><input id="upload_files_'+data.record.testCaseId+'" type="file" name="upload_files_'+data.record.testCaseId+'" style="display:none;" onchange="get_upload_file_name('+data.record.testCaseId+')">';
									}
									}
									},	 */
						
						},
						 
			       	formSubmitting: function (event, data) {
			        	  //data.form.find('input[name="testCaseName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[minSize[3]], custom[maxSize[25]]');
			             
			              data.form.validationEngine();
			             return data.form.validationEngine('validate');
			         }, 
			          //Dispose validation logic when form is closed
			        formClosed: function (event, data) {
			             data.form.validationEngine('hide');
			             data.form.validationEngine('detach');
			         }
			     });
			   $('#jTableContainertest').jtable('load');  	   
						
		}
		
		function updateDomainSMEAction(value,keywrdLibId,fieldId,filter){

			//alert("value==>"+value+"==userId==>"+userId+"==fieldId=="+fieldId+"==testCaseId=="+testCaseId);
			var url='userAction.update?userId='+userId+'&keywrdLibId='+keywrdLibId+'&value='+value+'&fieldId='+fieldId;
			 $.ajax({
		           type: "POST",
		     contentType: "application/json; charset=utf-8",
		           url : url,
		           dataType : 'json',
		           success : function(data) {
		                  if(data.Result=="ERROR"){
		                        callAlert(data.Message);
		                  return false;
		           }else{
		            	   //userDomainSMEactiolListUe=rl='userAction.list?userId='+userId;
		        	   if(filter==referedBackItems){
				    		referBackList();
				    	}else{
				    		approvedTestCasesList();
				    	}
		           	      // approvedTestCasesList();
		           	    //referBackList();
		           	}
		                 
		           }
		    }); 
			
		}
//Testcases Scope Starts


function upload_files(id){
	
	$("#upload_files_"+id).trigger("click");
	
}

function get_upload_file_name(id,filter){
	//var referedBackItems="ReferedBackItems";
	//var myPendingaction="MyPendingaction";
		
	var uploadFileID = "upload_files_"+id;
    var uploadFileName = "#fileName_"+id;
	
    
    var url='testcase.attachements?testCaseId='+id;
     var fileContent = document.getElementById(uploadFileID).files[0];
	 var formdata = new FormData();
	 formdata.append(uploadFileID, fileContent);
	 $.ajax({
		    url: url,
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
		    	if(filter==referedBackItems){
		    		referBackList();
		    	}else{
		    		approvedTestCasesList();
		    	}
		    	//location.reload(); 
		    },
		});	 
		
		 var x = document.getElementById(uploadFileID);

    if ('files' in x) {
        if (x.files.length > 0) {
            for (var i = 0; i < x.files.length; i++) {
                var file = x.files[i];
                if ('name' in file) {
                	$(uploadFileName).text(file.name);
                }
            }
        }
    } 
		
	/* importFeaturesInitialStep(uploadFileID, uploadFileName);
	importFeatureFinalStep(url, "TestCase", uploadFileID, uploadFileName); */
	
	
	
}
function setCheckBoxValues(){
	$('.btn-group > .btn-default').each(function(){
		if($(this).children('.switch_checkbox').is(':checked')){
			$(this).addClass("active");
		}else{
			$(this).removeClass("active");
		}
		
	});
}


function referBackList(){
	try{
		//alert("productId----"+productId);
		if ($('#jTableContainerreferback').length>0) {
			 $('#jTableContainerreferback').jtable('destroy'); 
		}
		}catch(e){}
		//init jTable
		 $('#jTableContainerreferback').jtable({
		title: 'My ReferBack Action',
	        	paging: true, //Enable paging
	            pageSize: 10, //Set page size (default: 10)
	            editinline:{enable:true},
				actions: { 
	        	//	 listAction:'testcase.referback.list',	 
	        		listAction:'mytestcase.list?filter=ReferBackTestCases',
	        	     recordUpdated:function(event, data){
	        	        	$('#jTableContainertest').find('.jtable-main-container').jtable('reload');
	        	        },
	        	     recordAdded: function (event, data) {
	        	         	$('#jTableContainertest').find('.jtable-main-container').jtable('reload');
	        	        } 
	            }, 
				fields: { 
					/* otherDomainCategories:{
						title: 'Domain', 
						list:true,
						edit:false,
						width: "10%"
						},
						  domainMapping:{ 
					          	title: '',
					          	width: "5%",
					          	edit: true,
					          	create: false,
					          
					     	 	display: function (data) { 
					     		//Create an image that will be used to open child table 
					     			var $img = $('<img src="css/images/mapping.png" title="Add/Edit Domain(S)" data-toggle="modal" data-target="#dragListItemsContainer" />');
					     			//Open child table when user clicks the image 
					     			$img.click(function () {
					     				$img = manageCompetenciesMapping($img, data.record.testCaseId,data.record.testCaseName);
					     				//referBackList();
					     			});
					     			
					     			return $img; 
					         	} 
					 	  	}, */
					keywordLibId:{
						title: 'Id', 
						list:false,
						edit:false,
						},
					 type: { 
                    		title : 'Type',
          				edit: false,
          				list:true,
          				create:true,
          				options:{
 	                    	  'ScriptGeneration':'Script Generation',
 	                    	  'Scriptexecution' : 'Scriptless Execution',
 	                      }
          			},
          			testToolId : {
 				 		title : 'Test Engine',
 						list : true,
 						create : true,
 						edit : false,
 						options : function(data) {
 							return 'common.list.testToolMaster.option';
 						}
 					},
 					languagID : {
 				 		title : 'Language',
 						list : true,
 						create : false,
 						edit : false,
 						options : function(data) {
 							return 'common.list.languages';
 						}
 					},
 					className: {
  	                     title: 'Class Name',
  	                     list: true,
  	                     edit : false,
  	                     create: false,
      			   },
      			   	   
      			  
      			 binary: {
                       title: 'Binary',
                       create : true,
                       edit : false,
                       input: function(data) {
                        var   html = '<input type ="file" id="input-image" name="userfile"  />';
                           return html;
                       }
          	  	   },
          	  	isApprove:{
					title: 'Actions', list:true,edit:false, type : 'checkbox',width:"26%",
						 values: {'1' : 'Yes','0' : 'No'},
						 display:function(data)
			    		 {
			        	   if(data.record){ //3 is 
 								 var display_checkbox_value='<div class="btn-group" data-toggle="buttons">';
						      var flag=false;
			        		  var reviewStatus = data.record.isReferBack;
			        		 // var rejectStatus=data.record.isReject
			        		      // 5 is reject
			        		 		display_checkbox_value+='<label class="btn btn-default"  onclick="updateDomainSMEAction(1,'+data.record.keywordLibId+',5,\''+referedBackItems+'\')"><input class="toggle switch_checkbox" type="checkbox"  title="click to check" id="reject" val='+data.record.isReject+' > Reject </label>';  
							        display_checkbox_value+='<label class="btn btn-default" onclick="updateDomainSMEAction(1,'+data.record.keywordLibId+',7,\''+referedBackItems+'\')" ><input class="toggle switch_checkbox" type="checkbox"  title="click to check" id="review" val='+data.record.isReview+' > Re-Submit </label>';  
	        	  			 // display_checkbox_value+='<label class="btn btn-default" onclick="updateDomainSMEAction(0,'+data.record.testCaseId+',5)"><input class="toggle switch_checkbox" type="checkbox"  title="click to check" id="review" val='+data.record.isReview+' > Reject </label>';	

							display_checkbox_value+="</div>"
							
							return display_checkbox_value;
			        	   }
			           	}
					},
				
				},
				 
	       	formSubmitting: function (event, data) {
	        	  //data.form.find('input[name="testCaseName"]').addClass('validate[required, custom[Letters_loworup_noSpec]],custom[minSize[3]], custom[maxSize[25]]');
	             
	              data.form.validationEngine();
	             return data.form.validationEngine('validate');
	         }, 
	          //Dispose validation logic when form is closed
	        formClosed: function (event, data) {
	             data.form.validationEngine('hide');
	             data.form.validationEngine('detach');
	         }
	     });
	   $('#jTableContainerreferback').jtable('load');  
	   
	  setTimeout(function(){
		  
		  setCheckBoxValues();
	  
		  $("#jTableContainerreferback #jtable_custom_Reload").click(function(){
			  setTimeout(function(){setCheckBoxValues(); },500);
		  });
	  
	  },500);}


function importProductFeatures() {   
    var uploadProductID = "uploadFileOfProductFeatures";
    var uploadFileName = "productFeaturefileName";
    alert("testing");
	importFeaturesInitialStep(uploadProductID, uploadFileName);
	importFeatureFinalStep("product.features.import?productId="+productId, importProductFeatureStr, uploadProductID, uploadFileName);	
}
 function  fileUpload(testCaseId){
	var value=document.getElementById("uploadImageTS").files[0];
	//alert("value="+value);
	 var url="";
$.ajax({
    url:url,
    method: 'POST',
    contentType: false,
    data: formdata,
    dataType:'json',
    processData: false,
    success: function (data) {
    	var json = data;
    	var obj = json;
    	obj.Record = obj.Records;
    	delete obj.Records;
    	json = obj;
	    $dfd.resolve(json);
        $('#jTableContainerTS').jtable('load');
    },
    error: function () {
        $dfd.reject();
    }
});
 }
 
 



function trim(str) {
    return str.replace(/^\s+|\s+$/g,"");
}

function download(str) {
	var urlfinal="rest/download/documents?type="+str;
  	parent.window.location.href=urlfinal;
	
}


function myKeywords(){
	try{
		if ($('#jTableContainerForMyKeywords').length>0) {
			 $('#jTableContainerForMyKeywords').jtable('destroy'); 
		}
		}catch(e){}
		
		$('#jTableContainerForMyKeywords').jtable({
	         title: 'BDD Keywords',
	       /*   selecting: true, //Enable selecting  */
	         paging: true, //Enable paging
	         pageSize: 10, //Set page size (default: 10)
	         editinline:{enable:false},
	         //toolbarsearch:true,
	         selecting: false, //Enable selecting
	           /*  multiselect: true, //Allow multiple selecting
	            selectingCheckboxes: true, //Show checkboxes on first column */
	         //sorting: true, //Enable sortin
	         /* saveUserPreferences: false, */
	         actions: {
	             listAction: 'mykeywords.list',
	             createAction: 'bddkeywordsphrases.save',
	           //  editinlineAction: 'bddkeywordsphrases.update'

	            // deleteAction: 'administration.product.environment.delete'
	         },
	         fields: {
	        	 
	        	 id: { 
	        		 		key: true,
	        				type: 'hidden',
	        				create: false, 
	        				edit: false, 
	        				list: false,
	        				
	        	}, 
	        	
	        	keywordPhrase: { 
	            	title: 'Keyword Phrase' ,
	            	inputTitle: 'Keyword Phrase <font color="#efd125" size="4px">*</font>',
	            	list:true,
	            	edit:false,
	            	create : true,
	            	 width: "25%"
	            },
	            description: { 
	            	title: 'Description' ,
	            	inputTitle: 'Description <font color="#efd125" size="4px">*</font>',
	            	list:true,
	            	edit:false,
	            	create : true,
	            	 width: "25%"
	            },
	            tags: { 
	            	title: 'Tags' ,
	                list:true,
	            	edit:false,
	            	create:true,
	            	 width: "20%"
	    		},
	             objects: { 
	            	title: 'Objects',
	            	   	list:true,
	            	edit:false,
	            	create:true
	        	},
	        	parameters: { 
	            	title: 'Parameters',
	                  	list:true,
	            	edit:false,
	            	create:true
	            }, 
	            KeywordLibrary:{
		             title: '',
	                 width: "5%",
	                 edit: false,
	                 create: false,
	              	 display: function (parentData) { 
	              	//Create an image that will be used to open child table 
	              	var $img = $('<img src="css/images/list_metro.png" title="Add KeywordLibrary" />'); 
	              	//Open child table when user clicks the image 
	              	$img.click(function (data) { 
					
					// ----- Closing child table on the same icon click -----
					/* closeChildTableFlag = closeJtableTableChildContainer($(this), $("#jTableContainer"));
					if(closeChildTableFlag){
						return;
					} */
					
					$('#jTableContainerForMyKeywords').jtable('openChildTable', 
	              	$img.closest('tr'), 
	              	{ 
	              	title: 'Add KeywordLibrary',
	              	paging: true, //Enable paging
		            pageSize: 10, 
	              	editinline:{enable:false},
					actions: { 
	              		 listAction: 'keywordLibrary.list?keywordId='+parentData.record.id,
	                       createAction:  function (postData) {
	                           var formdata = getVars(postData);
	                           var fileContent = $('#input-image').get(0).files[0];
	                           
	                          
	                          // var formdata = new FormData();
	                           
	                          // var url = 'keywordLibrary.save?'+postData;
	                           var url = 'keywordLibrary.save';
	                           //url ='project.testdata.add';
	                           if($('#input-image').val() != ""){
	                        	   formdata.append("binary",fileContent);
	                           }

	                           return $.Deferred(function ($dfd) {
	                               $.ajax({
	                            	   type: "POST",
	                       		    url: url,
	                       		    method: 'POST',
	                       	    contentType: false,
	                       	    data: formdata,
	                       	    dataType:'json',
	                       	    processData: false,
	                                  // contentType: false, // Set content type to false as jQuery will tell the server its a query string request
	                                   success: function (data) {
	                                       $dfd.resolve(data);
	                                       $('#jTableContainerForMyKeywords').jtable('load');
	                                   },
	                                   error: function () {
	                                       $dfd.reject();
	                                   }
	                               });
	                           });
	                       },
	                       
	              	}, 
	              	 recordUpdated:function(event, data){
	                     $('#jTableContainerForMyKeywords').find('.jtable-child-table-container').jtable('reload');
	                 },
	                 recordAdded: function (event, data) {
	                 	//$('#jTableContainerForMyKeywords').find('.jtable-child-table-container').jtable('reload');
	                 },
	                
	              	fields: {
	              		keywordLibId: { 
	                      	type: 'hidden', 
	                      	edit: false,
	                      	list:false,
	                      	create:false
	                      }, 
	                      id: { 
		                      	type: 'hidden', 
		                      	edit: false,
		                      	list:false,
		                      	create:true,
		                      	defaultValue: parentData.record.id 
		                      }, 
	                      
	                      type: { 
	                    		title : 'Type',
	          				edit: false,
	          				list:true,
	          				create:true,
	          				options:{
	 	                    	  'ScriptGeneration':'Script Generation',
	 	                    	   'Scriptlessexecution' : 'Scriptless Execution', 
	 	                      }
	          			},
	          			testToolName : {
	 				 		title : 'Test Engine',
	 						list : true,
	 						create : true,
	 						edit : true,
	 						width: "20%",
	 						options:{
	 	                    	  'SELENIUM':'SELENIUM',
	 	                    	   'APPIUM' : 'APPIUM',
	 	                    	  'SEETEST' : 'SEETEST',
	 	                      }
	 					},
	 					languagID : {
	 				 		title : 'Language',
	 						list : true,
	 						create : true,
	 						edit : true,
	 						width: "20%",
	 						options:{
	 	                    	  '1':'JAVA',
	 	                    	   /* 'PYTHON' : 'PYTHON', */
	 	                      }	 					},
	 					className: {
	  	                     title: 'Class Name',
	  	                   inputTitle: 'Class Name <font color="#efd125" size="4px">*</font>',
	  	                     width: "15%",
	  	                     list: true,
	  	                     edit : false,
	  	                     create: true,
	      			   },
	      			   	   
	      			  
	      			 binary: {
	                       title: 'Binary',
	                       inputTitle: 'Binary <font color="#efd125" size="4px">*</font>',
	                       create : true,
	                       edit : false,
	                       input: function(data) {
	                        var   html = '<input type ="file" id="input-image" name="userfile"  />';
	                           return html;
	                       }
	          	  	   },
	          	  	status: {
 	                     title: 'Status',
 	                     width: "15%",
 	                     list: true,
 	                     edit : false,
 	                     create: false,
     			   },

	      				
	      				
           		},
           		 formSubmitting: function (event, data) {
                   	 //data.form.find('input[name="mappingValue"]').addClass('validate[required]');
                	 data.form.find('input[name="className"]').addClass('validate[required]');
                	 data.form.find('input[name="userfile"]').addClass('validate[required]');
                	 if($('#input-image').get(0).files[0] !=  undefined){
                	 if(!$('#input-image').get(0).files[0].name.endsWith(".jar") ){
                  	   alert("Please select jar ");
                  	   return false;
                     } 
           		 }
                	
	                       data.form.validationEngine();
                       return data.form.validationEngine('validate');
                   }, 
                    formClosed: function (event, data) {
                       data.form.validationEngine('hide');
                       data.form.validationEngine('detach');
                   }
         	},
           function (data) { //opened handler 
           	data.childTable.jtable('load'); 
           	}); 
           	}); 
           	//Return image 
           	return $img; 
           	}, 	                 
            },
        		
		    		
		    		
	       },			 
           formSubmitting: function (event, data) {        
        	   
        	   data.form.find('input[name="keywordPhrase"]').addClass('validate[required]');
        	   data.form.find('input[name="description"]').addClass('validate[required]');
        	   
			data.form.validationEngine();
          	return data.form.validationEngine('validate');
           }, 
            //Dispose validation logic when form is closed
            formClosed: function (event, data) {
               data.form.validationEngine('hide');
               data.form.validationEngine('detach');
           }, 
          
   	 
   	 
   	 });  
  
        
		 
	   $('#jTableContainerForMyKeywords').jtable('load');	  
}


//Read a page's GET URL variables and return them as an associative array.
function getVars(url)
{
    var formData = new FormData();
    var split;
    $.each(url.split("&"), function(key, value) {
        split = value.split("=");
        formData.append(split[0], decodeURIComponent(split[1].replace(/\+/g, " ")));
    });

    return formData;
}

// Variable to store your files
var files;

$( document ).delegate('#input-image','change', prepareUpload);

// Grab the files and set them to our variable
function prepareUpload(event)
{
    files = event.target.files;
}

</script>

 <link href="css/scott.css" rel="stylesheet" type="text/css">
  <style>

  .jstree-default .jstree-wholerow-clicked, .jstree-wholerow .jstree-wholerow-clicked,.jstree-hovered,.jstree-wholerow-hovered,.jstree-anchor:hover{
	 background:none !important;
 }
 a.jstree-clicked{
	 border:0px !important;
 }
 
 
 </style>

 <div><%@include file="dragDropListItems.jsp"%></div>
 <script type="text/javascript" src="js/datatable/jquery.dataTables.columnFilter.js"></script>
<script type="text/javascript" src="js/datatable/dataTables.fixedHeader.js"></script>
<script type="text/javascript" src="js/datatable/dataTables.tableTools.js"></script>
<script type="text/javascript" src="js/datatable/jquery.dataTables.rowGrouping.js"></script>

 <script src="js/pagination/jquery.paging.min.js" type="text/javascript"></script>
 <script src="js/importData.js" type="text/javascript"></script> 
 	<!-- <script src="js/pageSpecific/testcaseExecutionView.js" type="text/javascript"></script> -->
 
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>