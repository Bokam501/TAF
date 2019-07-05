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
<link href="files/lib/metronic/theme/assets/global/plugins/icheck/skins/all.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet"	type="text/css"></link>
<link href="css/customStyle.css" rel="stylesheet" type="text/css">


<link href="js/datatable/Editor-1.5.6/css/dataTables.editor.css" rel="stylesheet" type="text/css"></link>
<link href="js/datatable/Editor-1.5.6/css/editor.dataTables.css" rel="stylesheet" type="text/css"></link>
<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>
<!-- END THEME STYLES -->
<!-- bootstrap (optional) -->

     <link rel="stylesheet" type="text/css" href="js/Scripts/multiselect/bootstrap.min.css"> 
     <script type="text/javascript" src="js/Scripts/multiselect/bootstrap.min.js"></script>  

    <!-- <link href="js/nreco/pivotrest/Scripts/bootstrap/bootstrap.min.css" rel="stylesheet" /> -->
    <script type="text/javascript" src="js/nreco/pivotrest/Scripts/jquery-2.1.4.min.js"></script>
    <!-- <script type="text/javascript" src="js/nreco/pivotrest/Scripts/bootstrap/bootstrap.js"></script> -->


    <!-- select2 plugin (depends on jquery-ui sortable)-->
    <link href="js/nreco/pivotrest/Scripts/select2/select2.css" rel="stylesheet" />
    <link href="js/nreco/pivotrest/Scripts/select2/select2-bootstrap.css" rel="stylesheet" />
    <script type="text/javascript" src="js/nreco/pivotrest/Scripts/jquery-ui-1.9.2.custom.min.js"></script>
    
           
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-header-menu-fixed" class to set the mega menu fixed  -->
<!-- DOC: Apply "page-header-top-fixed" class to set the top menu fixed  -->
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
<%-- <div><%@include file="treeStructureSidebar.jsp" %></div> --%>
<div><%@include file="treeStructureSidebarLazyLoading.jsp" %></div>
<div><%@include file="postHeader.jsp" %></div>

<!-- BEGIN PAGE CONTENT -->
<div class="page-content">
		<div class="container-fluid">			
			<!-- BEGIN PAGE CONTENT INNER -->
			<div class="row margin-top-10" id="toAnimate">
				<div class="col-md-12">
					<!-- BEGIN PORTLET-->
					<div class="portlet light ">
						<!--  <div class="portlet-title toolbarFullScreen">
							<div class="caption caption-md">
								<i class="icon-bar-chart theme-font hide"></i>
								<span class="caption-subject theme-font bold uppercase">Pivot View Report</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font">: Advanced</span>
								<span class="caption-helper hide"></span>
							</div>
						</div>  -->
						<!-- <div class="portlet-body">
							<div>
								<label class="col-sm-4 control-label">Template:</label>
		                        <div class="col-sm-8" id="row">
		                            <select class="form-control input-small select2me" id="templateList_ul">
									</select>	
		                        </div>
								
							</div>
						</div> -->
						<div class="portlet-body" style="height:800px;">
							<div id="pivotDataTable">
								<img src="css/images/create.jpg" style="width: 24px;height: 24px;float:right;cursor: pointer;" title="Create New Report" onclick="createTable()"></img>
								<!-- <iframe width=100% height=100%  src="http://localhost:5000/report.html" frameborder="0" allowfullscreen scrolling="auto"></iframe> -->
								<%-- <%@include file="pivotnrecoadvancereportframe.jsp" %> --%>
								<table id="pivot_dataTable"  class="cell-border compact row-border" cellspacing="0" width="100%">
									<thead>
							           		<tr>
								                <th>ID</th>
								                <th>Report Type</th>
								                <th>Name</th>
								                <th>Description</th>
								                <th>Created By</th>
								                <th>Created Date</th>
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
							           		</tr>
							      	  </tfoot>
								</table>
				
							</div>
							<div id="pivotReportFrame">
								<iframe id="myIframe" src=""  width="100%" height="650" scrolling="yes" frameborder="0"></iframe>
								<input id="engagementNames" type="hidden" value="" />
								<input id="productNames" type="hidden" value="" />

							</div>
								
						</div>
							
						<!-- END PORTLET-->
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

<div id="openPivotReportContainer" class="modal " tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
					<!-- <h4 class="modal-title theme-font">Create Table</h4> -->
				</div>
				<div class="modal-body">					
					 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">					 
		 				<div><%@include file="pivotnrecoadvancereportcreateinner.jsp" %></div> 
		 			</div>					 
				</div>
			</div>
	</div>
</div>


<div id="pivotReportContainer" class="modal " tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
					<h4 class="modal-title theme-font">Reports</h4>
				</div>
				<div class="modal-body">					
					 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
		 				<div id="pivotReportDTContainer"><iframe id="myPopUpIframe" src=""  width="100%" height="650" scrolling="yes" frameborder="0"></iframe></div>
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
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>

<!-- END PAGE LEVEL SCRIPTS -->

<!-- the jScrollPane script -->
<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

<!-- Include jTable script file. -->
<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>

<!-- Plugs ins for jtable inline editing -->
<script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
<!-- <script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.modifedColumn.js"></script> -->
<script type="text/javascript" src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>

<!-- Validation engine script file and english localization -->
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>
<script src="js/select2.min.js" type="text/javascript"></script>

<script src="js/viewScript/pivotnrecoadvancereport.js" type="text/javascript"></script>

		<!-- basic pivot table styles -->
       <link href="js/nreco/pivotrest/Scripts/nrecopivottable.css" rel="stylesheet" />

       <!-- interactive nreco pivot table jQuery plugin (change sorting, fixed headers) -->
       <script type="text/javascript" src="js/nreco/pivotrest/Scripts/jquery.nrecopivottable.js"></script>

       <!-- chartist.js for charts (optional)  -->
       <link rel="stylesheet" type="text/css" href="js/nreco/pivotrest/Scripts/chartist/chartist.min.css">
       <script type="text/javascript" src="js/nreco/pivotrest/Scripts/chartist/chartist.min.js"></script>

       <!-- chartist plugins for charts usability (tooltip, axis labels) -->
       <script type="text/javascript" src="js/nreco/pivotrest/Scripts/chartist/chartist-plugin-tooltip.min.js"></script>
       <script type="text/javascript" src="js/nreco/pivotrest/Scripts/chartist/chartist-plugin-axistitle.min.js"></script>

       <!-- nreco pivot chart wrapper for chartist.js -->
       <script type="text/javascript" src="js/nreco/pivotrest/Scripts/jquery.nrecopivotchart.js"></script>

       <!-- autocomplete plugin for filter (optional) -->
       <script type="text/javascript" src="js/nreco/pivotrest/Scripts/awesomplete/awesomplete.min.js"></script>
       <link rel="stylesheet" type="text/css" href="js/nreco/pivotrest/Scripts/awesomplete/awesomplete.min.css">
       
      <!-- multiselect dropdown -->
     <link rel="stylesheet" type="text/css" href="js/Scripts/multiselect/bootstrap-multiselect.css"> 
 	<script type="text/javascript" src="js/Scripts/multiselect/bootstrap-multiselect.js"></script> 
     <!-- <script type="text/javascript" src="js/Scripts/multiselect/components-bootstrap-multiselect.min.js"></script> -->
     <script type="text/javascript" src="js/Scripts/multiselect/components-bootstrap-multiselect.js"></script> 
     <link rel="stylesheet" type="text/css" href="js/Scripts/multiselect/components.min.css"> 

</form>
<script>
var urlToGetActivityStatusReport = "";
var engagementId;
var productId;
var collectionId;
var selectedCollectionId;
var selectedEngagementId=0;
	var productSelectedId;
var colName="";
var colValues="";
var dynamicValues="";
   $(document).ready(function() {
	   
  	// loadPivotTable(colName, colValues);
  		loadCollectionList();
  		loadEngagementsList();
  		//loadProductsBasedOnTestFactoryId();
  		//loadPivotNRecoTable("[]");
 		
  		$('#factoryList_ul').change(function(){
  			loadProductsBasedOnTestFactoryId();
 			
 		});
  		/* $('#productList_ul').change(function(){
 			generateActivityTaskEffortReport();
 			//test();
 			
 		}); */
  		
   });
   var collectionArray=[];
   function loadCollectionList(){
	   $('collectionList_ul').empty();
	   $.post('pivot.mongo.collections.list',function(data) {	
	        var ary = (data.Options);
	        collectionArray=(data.Options);
	        $.each(ary, function(index, element) {
	    		$('#collectionList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
	  		 });
	   		$('#collectionList_ul').select2();	
	   		 
		});
   }
   
   function asssignCollectionId(cubeid){
	   var finalVal='';
	   for (var i = 0; i < collectionArray.length; i++) {
   			if(collectionArray[i].DisplayText==cubeid){
   				finalVal=collectionArray[i].Value;   			
   				break;
	   		}
 		 }
	   return finalVal;
   }
   function loadEngagementsList(){		
 		$('#factoryList_ul').empty();				
 		$.post('common.testFactory.list.byLabId?testFactoryLabId=1&engagementTypeId=0',function(data) {	
 	        var ary = (data.Options);
 	       /*  var i=0;
 	        $.each(ary, function(index, element) {
 	        	if(i==0){
 	        		$('#factoryList_ul').append('<option id="-1" ><a href="#">Choose</a></option>');
 	    			$('#factoryList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
 	        	}else{
 	        		$('#factoryList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
 	        	}
 	        	i++;
 	  		 });
 	   		$('#factoryList_ul').select2();	 */
 	       var pivotTableEngagementList=[];
	   	      var temp='';
	   	        $.each(ary, function(index, element) {
		        		temp = { id:element.Value, label: element.DisplayText, title: element.DisplayText, value: element.Value};
		        		pivotTableEngagementList.push(temp);
	   	        });   
	   	      
	   	    $('#factoryList_ul').multiselect();
	   	    $('#factoryList_ul').multiselect('dataprovider', pivotTableEngagementList); 
 	   		 
 		});
 	}
   
   function loadProductsBasedOnTestFactoryId(){	
	   	   	   
	   engagementId = $("#factoryList_ul").find('option:selected').attr('id');
  		//selectedEngagementId= parseInt(engagementId);
  		//console.log("selectedEngagementId>>>"+ selectedEngagementId);
			var engagementIdArr=[];
     		var engagementIdArrLen = $("#factoryList_ul").find('option:selected').length;
     		var temps='';
     		for(var i=0;i<engagementIdArrLen;i++){
     			temps = $("#factoryList_ul").find('option:selected').eq(i)[0].value;       	       			
     			engagementIdArr.push(Number(temps));       	       			
     		}     
	    //selectedEngagementId = isNaN(parseInt(engagementId)) ? -1 : parseInt(engagementId);
   		console.log("selectedEngagementId arr new>>>"+ engagementIdArr);
  		$('#productList_ul').empty();	
  		if(engagementIdArr!=0){
  		
      		$.post('common.list.product.byTestFactoryIds?testFactoryId='+engagementIdArr+'',function(data) {	
      	        var ary = (data.Options);
      	       /*  var j=0;
      	        $.each(ary, function(index, element) {
      	        	if(j==0){
      	        		$('#productList_ul').append('<option id="-1" ><a href="#">ALL</a></option>');
      	        		$('#productList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
      	        	}else{
      	    			$('#productList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
      	        	}
      	        	j++;
      	        }); */
      	   	//$('#productList_ul').select2();
      	      
      	      var pivotTableProductList=[];
      	      var temp='';
      	        $.each(ary, function(index, element) {
   	        		temp = { id:element.Value, label: element.DisplayText, title: element.DisplayText, value: element.Value};
   	        		pivotTableProductList.push(temp);
      	        });   
      	      
      	    $('#productList_ul').multiselect();
      	    $('#productList_ul').multiselect('dataprovider', pivotTableProductList);      	   		      
      		});
  		}else{
  			callAlert("Please choose engagement... ");
  		}
  		
  	}
   function loadProductAndEngagementNames(productSelectedId){		
	  // alert("calling productSelectedId...."+productSelectedId);
		var urls = 'pivot.load.product.names.id?&productIds='+productSelectedId;
		$.ajax(
		{
		    type: "POST",
		    url : urls,
		    cache:false,
		    success: function(data) {
		    			
				var data1=data;
				var datArray = data1.split("~");
				var engagementNames=datArray[0];
				var productNames=datArray[1];
				document.getElementById("engagementNames").value=engagementNames;
				document.getElementById("productNames").value=productNames;
							
			 	},
	 		    error : function(data){					
	 				//closeLoaderIcon();
	 			},
	 			complete : function(data){				
	 				//closeLoaderIcon();
	 			}
			}
		); 		
  	}
</script>
<!-- END JAVASCRIPTS -->
<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>

</body>
<!-- END BODY -->
</html>