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

<!-- END THEME STYLES -->
<!-- bootstrap (optional) -->
    <link href="js/nreco/pivotrest/Scripts/bootstrap/bootstrap.min.css" rel="stylesheet" />
    <script type="text/javascript" src="js/nreco/pivotrest/Scripts/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="js/nreco/pivotrest/Scripts/bootstrap/bootstrap.js"></script>


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

<!-- END HEADER -->

<!-- BEGIN CONTAINER -->
<div class="page-container">

<!-- BEGIN PAGE CONTENT -->
<div class="page-content">
		<div class="container-fluid">			
			<!-- BEGIN PAGE CONTENT INNER -->
			<div class="row margin-top-10" id="toAnimate">
				<div class="col-md-12">
					<!-- BEGIN PORTLET-->
					<div class="portlet light ">
						<!-- <div class="portlet-title toolbarFullScreen">
							<div class="caption caption-md">
								<i class="icon-bar-chart theme-font hide"></i>
								<span class="caption-subject theme-font bold uppercase">Pivot Create Report</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font">: Advanced</span>
								<span class="caption-helper hide"></span>
							</div>
						</div> -->
						<div class="portlet-body">
							<div class ="row ">		
								<div class="container1">
								
							
							<div class="row" style="margin-top: 3px;">
								<%@include file="pivotnrecoadvancereportcreateinner.jsp" %>
							</div>
							<div class="clearfix"></div>
						    </div>
						      
						      <!-- Nreco code ends -->
								
							</div>
							
						<!-- END PORTLET-->
				        
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
</div>
<!-- END PAGE-CONTAINER -->

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

</form>
<!-- END JAVASCRIPTS -->
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
 	        var i=0;
 	        $.each(ary, function(index, element) {
 	        	if(i==0){
 	        		$('#factoryList_ul').append('<option id="-1" ><a href="#">Choose</a></option>');
 	    			$('#factoryList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
 	        	}else{
 	        		$('#factoryList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
 	        	}
 	        	i++;
 	  		 });
 	   		$('#factoryList_ul').select2();	
 	   		 
 		});
 	}
   function loadProductsBasedOnTestFactoryId(){	
	   	   	   
	    engagementId = $("#factoryList_ul").select2().find('option:selected').attr('id');
	    console.log("engId>>"+engagementId)
   		selectedEngagementId= parseInt(engagementId);
	    //selectedEngagementId = isNaN(parseInt(engagementId)) ? -1 : parseInt(engagementId);
   		console.log("selectedEngagementId new>>>"+ selectedEngagementId);
  		$('#productList_ul').empty();	
  		if(selectedEngagementId!=-1){
  		
      		$.post('common.list.product.byTestFactoryId?testFactoryId='+selectedEngagementId+'',function(data) {	
      	        var ary = (data.Options);
      	        var j=0;
      	        $.each(ary, function(index, element) {
      	        	if(j==0){
      	        		$('#productList_ul').append('<option id="-1" ><a href="#">ALL</a></option>');
      	        		$('#productList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
      	        	}else{
      	    			$('#productList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
      	        	}
      	        	j++;
      	        });
      	   		$('#productList_ul').select2();	
      	   		      
      		});
  		}else{
  			callAlert("Please choose engagement... ");
  		}
  		
  	}
   
</script>
</body>
<!-- END BODY -->
</html>