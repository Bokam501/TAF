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
									<span class="caption-subject theme-font bold uppercase">Indices
									</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
								</div>
							</div>
							<div class="portlet-body form">
								<div class="row ">
									<div class="col-md-12">
										<ul class="nav nav-tabs " id="tabslist">
											<li class="active"><a href="#ProductsVersionTestcase" data-toggle="tab">Indices For Dashboard</a>
											</li>

										</ul>
									</div>
								</div>
								<div id="hidden">
								<input type='hidden' id='tcid'/></div>

								<div class="tab-content">
								<!-- Version TestSuites Ends -->
									<div id="VersionTestCaseMapping" class="tab-pane fade active in">
									<div class="row">
												
												<div id="status_dd" class="col-md-4">
												<label >Select Collection For Indices :</label>
													<select class="form-control input-small select2me" id="status_ul">
														<option value="0" ><a href="#">ALL</a></option>
														<option value="3" ><a href="#">PRODUCT</a></option>
														<option value="4" ><a href="#">PRODUCTVERSION</a></option>
														<option value="5" ><a href="#">PRODUCTBUILD</a></option>
														<option value="1" ><a href="#">TESTCASE</a></option>
														<option value="6" ><a href="#">FEATURE</a></option>
														<option value="7" ><a href="#">TESTBED</a></option>
														<option value="8" ><a href="#">WORKPACKAGE</a></option>
														<option value="9" ><a href="#">RESULTS</a></option>
														<option value="2" ><a href="#">DEFECTS</a></option>
														<option value="10" ><a href="#">iLCM DEFECTS</a></option>
													</select>
												</div>
												<div class="col-md-6">
										   			<button type="button" id="save" class="btn green-haze" onclick="createIndicesForTestcase()">Create</button> </td>
										 		</div>
	    								</div>
									
										<!-- ContainerBox -->
<!-- 										<div class="jScrollContainerResponsive" style="clear:both;padding-top:10px"> -->
<!-- 											<div id="jTableContainerversiontestcase"></div> -->
<!-- 										</div> -->
										<!-- End Container Box -->
									</div>
									

								</div>
								<!-- 						</div></div></form> -->
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
	<script type="text/javascript" src="js/bootstrap-datetimepicker.min.js"></script>
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
jQuery(document).ready(function() {	
   QuickSidebar.init(); // init quick sidebar
   ComponentsPickers.init();
   setBreadCrumb("Manage Products");
   createHiddenFieldsForTree();
   setPageTitle("Products");
   getTreeData("administration.productWithTF.tree");
   userRoleId='<%=session.getAttribute("roleId")%>';
   userId='<%=session.getAttribute("ssnHdnUserId")%>'; 
   $("#treeContainerDiv").on("select_node.jstree",
		     function(evt, data){
	   			var entityIdAndType =  data.node.data;
	   			var arry = entityIdAndType.split("~");
	   			 key = arry[0];
	   			var type = arry[1];
	   			var title = data.node.text;
				var date = new Date();
			    var timestamp = date.getTime();
			    nodeType = type;
			    var loMainSelected = data;
		        uiGetParents(loMainSelected);		        
		        if(nodeType == "TestFactory"){
				    	testFactoryId = key;
				    	document.getElementById("treeHdnCurrentTestFactoryId").value=testFactoryId;				    	 
				    	productId=0;
				    	callAlert("Please select a product");
				    	hideAllTabs();
			    }
			    if(nodeType == "Product"){
			    		productId = key;
			    		testFactoryId = document.getElementById("treeHdnCurrentTestFactoryId").value;
			    		if(productId==null && productId==undefined){
			    			productId=0;
			    		}
			    }
			  
			    selectedTab=$("#tabslist>li.active").index();
					  if(selectedTab==0){						  
						 var firstTab = $("VersionTestCaseMapping");
						if(!(firstTab.hasClass("active in"))){
							firstTab.addClass("active in");
							firstTab.siblings(".tab-pane").removeClass("active in");
						}
						$("#VersionTestCaseMapping").addClass("active in").siblings(".tab-pane").removeClass("active in");

	     			}
   });
   
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

function createIndicesForTestcase(){
	if(nodeType == "Product"){
		productId = key;
		if(productId == null || productId <= 0 || productId == 'null' || productId == ''){
			callAlert("Please select the Product");
			return false;
		}
	}
	var indiciesVal = $("#status_ul").find('option:selected').val();				

	 $.ajax({
         type: "POST",
   		contentType: "application/json; charset=utf-8",
         url : 'elastic.search.testcase?productId='+productId+'&type='+indiciesVal,
         dataType : 'json',
         success : function(data) {
                if(data.Result=="ERROR"){
                      callAlert(data.Message);
                return false;
		         }else{
		        	 
		        	 if(data.Result=="SUCCESS"){
		        		 callAlert("Successfully collection Index created");
	        	 		testcases();
		        	 }
	         	}
               
         }
  });
}

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

//Modified
$(document).on('click', '#tabslist>li', function(){
		selectedTab=$(this).index();
		document.getElementById("form_wizard_1").style.display="none";
		  if(selectedTab==0){				
			  //products();
			  $("#VersionTestCaseMapping").addClass("active in").siblings(".tab-pane").removeClass("active in");
			  }
});
		


//Load the dynamic versions columns for Jtable

function hideAllTabs(){	
	$('#VersionTestCaseMapping').children().hide();	
}


</script>

	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>