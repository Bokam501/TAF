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
<!-- END THEME STYLES -->
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
<div><%@include file="treeStructureSidebar.jsp" %></div>
<div><%@include file="postHeader.jsp" %></div>

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
								<span class="caption-subject theme-font bold uppercase">Activity Types</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
								<span class="caption-helper hide">weekly stats...</span>
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
								       <div class="jScrollContainerResponsive jScrollContainerFullScreen" style="clear:both;padding-top:10px"> 
								      		<div id="jTableContainer" class ="jTableContainerFullScreen"></div>
									 	</div>
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

var isFirstLoad = true;
jQuery(document).ready(function() {	
   QuickSidebar.init(); // init quick sidebar
   //ComponentsPickers.init();
   setBreadCrumb("Admin");
   //createHiddenFieldsForTree();
   $("#menuList li:first-child").eq(0).remove();
	setPageTitle("Customer");
	getTreeData('customers.tree');
	
	//setDefaultnode();
	
 	$("#treeContainerDiv").on("select_node.jstree",
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
			    if(nodeType == "Customer"){
		    	  customerId = key;
		    	  urlToGetActivityTypesOfSpecifiedCustomerId = 'customer.specific.activity.type.listing?customerId='+customerId;
		    	  listActivityTypesOfCustomer();
			    }
		     }
		);
 	 
   
});
	
var SOURCE;
var SOURCE;
var treeData;
var customerId = "";
var urlToGetActivityTypesOfSpecifiedCustomerId = "";


function listActivityTypesOfCustomer(){
	try{
		if ($('#jTableContainer').length>0) {
			 $('#jTableContainer').jtable('destroy'); 
		}
		}catch(e){}
		 $('#jTableContainer').jtable({
	         title: 'Activity Types',
	         selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: 10, //Set page size (default: 10)
	         recordsLoaded: function(event, data) {
	 			$('.portlet > .portlet-title > .tools > .reload').trigger('click');
	         },
	         editinline:{enable:true},	 
	         actions: {
	             listAction:urlToGetActivityTypesOfSpecifiedCustomerId,
	             createAction: 'customer.specific.activity.add',
	             editinlineAction: 'customer.specific.activity.update',
	         },
	         fields: {
	        	 activityMasterId: { 
	   				key: true,
	   				type: 'hidden',
	   				create: false, 
	   				edit: false, 
	   				list: false,
	   			 	/* display: function (data) { return data.record.environmentGroupId;}, */
	   			},
				customerId:{
		  			title: 'Customer',
	     	  		inputTitle: 'Customer <font color="#efd125" size="4px">*</font>',
	     	  		width: "20%",
	     	  		list:true,
	     	  		create:true,
	     	  		edit:false,
	     	  		defaultValue: customerId,
	     	  	  	options: 'administration.customer.option.list'
		  		},
	   		 	activityMasterName: { 
	   		 		title:'Activity Type',
	   		 	    inputTitle: 'Activity Type <font color="#efd125" size="4px">*</font>',
	   				create: true, 
	   				edit: true, 
	   				list: true,
	   			},
	   			description: { 
	       			title: 'Description',
	       		    type: 'textarea',   
	     		  	width: "35%",  
	     	  		list:false,
		     	  	create:true
	    	   }, 
	   			activityGroupId:{
		  			title: 'Activity Group',
	     	  		inputTitle: 'Activity Group <font color="#efd125" size="4px">*</font>',
	     	  		width: "20%",
	     	  		list:true,
	     	  		create:true,
	     	  		edit:false,
	     	  	 	options: 'activity.group.option.list'
		  		},
		  		activityTypeId:{
		  			title: 'Activity Category',
	     	  		inputTitle: 'Activity Category <font color="#efd125" size="4px">*</font>',
	     	  		width: "20%",
	     	  		list:true, 
	     	  		create:true,
	     	  		edit:false,
					dependsOn:'activityGroupId',
					options:function(data){
						if(data.dependedValues.activityGroupId != null){
							return 'activity.type.master.option.list?activityGroupId='+data.dependedValues.activityGroupId;
						}
		     		}
		  		},
		  		weightage :{		  			
		  			title: 'Weightage',
		  			defaultValue: 1
		  			
		  		}
	  		  
	    	
			   /* status: {	      
			       	title: 'Status' ,
		        	width: "5%",  
		        	list:true,
		        	edit:true,
		        	create:false,
		        	type : 'checkbox',
		        	values: {'0' : 'No','1' : 'Yes'},
			   		defaultValue: '1'
	         	},	 */ 
            		          
	         },
	         formSubmitting: function (event, data) {        
	            	/* data.form.find('input[name="environmentGroupName"]').addClass('validate[required, custom[minSize[3]]'); 
	            	data.form.find('input[name="displayName"]').addClass('validate[required, custom[minSize[3]]');
	            //data.form.find('input[name="status"]').addClass('validate[required]');
	              data.form.validationEngine();
	              return data.form.validationEngine('validate'); */
	            }, 
	             //Dispose validation logic when form is closed
	          /*    formClosed: function (event, data) {
	                data.form.validationEngine('hide');
	                data.form.validationEngine('detach');
	            }, */
	     });		 
		 $('#jTableContainer').jtable('load');
		 }
		 
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
					
					return false;
				}
			});	
			//setDefaultnode();
		});
	} else {
		
		/* defaultNodeId = $.jstree.reference('#treeContainerDiv').get_node(defaultNodeId).children[0];
		nodeFlag = validateNodeLength($.jstree.reference('#treeContainerDiv').get_node(defaultNodeId))
		if(nodeFlag){
			setDefaultnode();					
		}else{			
			console.log(defaultNodeId)
			$.jstree.reference('#treeContainerDiv').deselect_all();
			$.jstree.reference('#treeContainerDiv').close_all();
			$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
			$.jstree.reference('#treeContainerDiv').trigger("select_node");
		} */
	}
}


</script>

<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>