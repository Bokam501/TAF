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

<!-- pivottable.js requires jQuery -->
		 <script type="text/javascript" src="js/nreco/jquery-2.1.3.min.js"></script> 
		
		<!-- jQuery UI sortable plugin for drag and drop - required only for pivotUI  -->
		<script src="js/nreco/webpivot/Scripts/jquery-ui-1.9.2.custom.min.js"></script>
		<!-- OPTIONAL: support touch events (pivot table usage from mobile and tablet devices) -->
		<script src="js/nreco/webpivot/Scripts/jquery.ui.touch-punch.min.js"></script>	
		

		<!-- PivotTable.js plugin with renderers -->
		 <script src="js/nreco/webpivot/Scripts/pivottable/pivot.min.js"></script>
		
		<!-- note: renderers are OPTIONAL (useful are: google charts, c3 charts) -->
	    <script src="js/nreco/webpivot/Scripts/pivottable/export_renderers.min.js"></script>
		
		<!-- NReco extensions -->
	    <script src="js/nreco/webpivot/Scripts/pivottable/nrecopivottableext.js"></script> s
		
		<!-- basic pivottable css -->
		<link href="js/nreco/webpivot/Scripts/pivottable/pivot.css" rel="stylesheet" />	
		<!-- NReco extensions css -->
		<link href="js/nreco/webpivot/Scripts/pivottable/nrecopivottableext.css" rel="stylesheet" />	
		
<!-- END THEME STYLES -->

	<style>
  			.page-footer{
				z-index: 95!important;
			}
  			/* body {
				font-family:Arial;
			} */
			.container1 {
				/* margin-left:auto;
				margin-right:auto; */
				/* max-width:1200px; */
				/* height: auto; */
			}
			.infoBlock {
				background-color:#F0F0F0;
				border-radius:6px;
				padding:20px;
				padding-bottom:10px;
				margin-bottom:20px;
			}
			/* .row {
				margin-left:-15px;
				margin-right:-15px;
				box-sizing: border-box;
			} */
			.col50 {
				/* width:50%; */
				float:left;
				padding-left:15px;
				padding-right:15px;
				box-sizing: border-box;
			}
			.clearfix {
				clear:both;
			}
			ul {
				margin-top:0px;
				margin-bottom:10px;
			}
			hr {
				border-bottom:0px;
			}
			pre {
				font-size:11px;
			}
			
			/* styles for responsive pivot UI */
			table.pvtUi {
				/* table-layout:fixed; */
				width:100%;
			}
			table.pvtUi>tbody>tr:first-child>td:first-child {
				width:200px;
			}
			.pvtTableRendererHolder {
				/* max-height:300px; */
				width: 600px;
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
								<span class="caption-subject theme-font bold uppercase">Pivot Report</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
							</div>
						</div>
						<div class="portlet-body">
							<div class ="row ">		
								<div class="container1">
								<div class="row" style="border: 1px solid grey;padding: 5px 5px 5px 5px">
								<div class="col-md-3">
									<div class="col-md-5" style="margin-top: 4px;"> Collections :</div>  
						      		<div id="collectionList_dd">	
							      		<select class="form-control input-small select2me" id="collectionList_ul">
										</select>	
									</div>
								</div>
								<div class="col-md-3">
									<div class="col-md-5" style="margin-top: 4px;"> Engagement :</div>  
						      		<div id="factoryList_dd">	
							      		<select class="form-control input-small select2me" id="factoryList_ul">
										</select>	
									</div>
								</div>
								<div class="col-md-3">
						      		<div class="col-md-5" style="margin-top: 4px;"> Product :</div>  
						      		<div id="productList_dd">	
							      		<select class="form-control input-small select2me" id="productList_ul">
										</select>	
									</div>
									
								</div>
								<div class="col-md-3">
						      		<div class="col-md-5" style="margin-top: 4px;"> &nbsp;</div>  
						      		<div id="productList_dd">	
							      		<button class="btn green-haze" onclick="generateActivityTaskEffortReport()">Load Data</button>
									</div>
									
								</div>
								</div>
							
							<div class="row" style="margin-top: 10px;">
								
								<div class="col50">
									
									
								</div>
								<!--  <a href="javascript:saveTemplate();" class="btn btn-default exportBtn">Save</a> -->
								<div class="col50" style="margin-bottom: 20px;margin-top: 20px;">
									<div id="samplePivotTable2" ></div>
								</div>	
								</div>
								<div class="clearfix"></div>
						      </div>
						      
						      <!-- Nreco code ends -->
								
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



<!-- Include jTable script file. -->
<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>

<!-- Plugs ins for jtable inline editing -->
<script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
<script type="text/javascript" src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>

<!-- Validation engine script file and english localization -->
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>




<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet" type="text/css"></link>

<!-- <script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script> -->
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="js/moment.min.js" type="text/javascript"></script>
<script src="js/daterangepicker.js" type="text/javascript"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>

<script type="text/javascript">


	jQuery(document).ready(function() {	
		   
		   ComponentsPickers.init();		   
		   
	});
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
       function loadCollectionList(){
    	   $('collectionList_ul').empty();
    	   $.post('mongo.collections.list',function(data) {	
    	        var ary = (data.Options);
    	        $.each(ary, function(index, element) {
    	    		$('#collectionList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
    	  		 });
    	   		$('#collectionList_ul').select2();	
    	   		 
    		});
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
       function test(){
     		alert("calling...");
   	    	collectionId = $("#collectionList_ul").select2().find('option:selected').attr('id');
	   		//selectedCollectionId= parseInt(collectionId);
	   		console.log("collectionId>>>"+ collectionId);
	   		
	   		engagementId = $("#factoryList_ul").select2().find('option:selected').attr('id');
	   		selectedEngagementId= parseInt(engagementId);
	   		console.log("selectedEngagementId>>>"+ selectedEngagementId);
	   		
     	 	productId = $("#productList_ul").find('option:selected').attr('id');
     		//productId = (typeof productId == 'undefined') ? -1 : productId;
     		productSelectedId=productId;
     		urlToGetActivityStatusReport = 'mongo.collection.pivot.report?collectionName='+collectionId+'&testFactoryId='+selectedEngagementId+'&productId='+productSelectedId;
     		console.log("productSelectedId>>>"+ productSelectedId);
     			//openLoaderIcon(); 
     			$.ajax(
     			{
     			    type: "POST",
     			    url : urlToGetActivityStatusReport,
     			    cache:false,
     			    success: function(data) {
     			    				
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
       function generateActivityTaskEffortReport(){
    	   loadPivotNRecoTable("[]");
    	   collectionId = $("#collectionList_ul").select2().find('option:selected').attr('id');
	   		//selectedCollectionId= parseInt(collectionId);
	   		console.log("collectionId>>>"+ collectionId);
	   		
	   		engagementId = $("#factoryList_ul").select2().find('option:selected').attr('id');
	   		selectedEngagementId= parseInt(engagementId);
	   		console.log("selectedEngagementId>>>"+ selectedEngagementId);
	   		
    	 	productId = $("#productList_ul").find('option:selected').attr('id');
    		//productId = (typeof productId == 'undefined') ? -1 : productId;
    		productSelectedId=productId;
    		if(selectedEngagementId!=-1){
    		urlToGetActivityStatusReport = 'mongo.collection.pivot.report?collectionName='+collectionId+'&testFactoryId='+selectedEngagementId+'&productId='+productSelectedId;
    		console.log("productSelectedId>>>"+ productSelectedId);
      			 //openLoaderIcon(); 
      			$.ajax(
      			{
      			    type: "POST",
      			    url : urlToGetActivityStatusReport,
      			    cache:false,
      			    success: function(data) {
      			    	//closeLoaderIcon();
      			    	
      					//$("#reportTable").show();			
      					var data1=eval(data);
      					//console.log("data1>>>"+data1);
      			  		//var cols = data1[0].COLUMNS;
      			  		var columnData=data1[0].DATA;
      			  		//console.log("columnData>>>"+columnData);
      			  		colData=data1[0].DATA;
      			  		dynamicValues="";
      			  		if(columnData == undefined){
      			  			columnData = [];
      			  			colData= [];
      			  		}
      			  		console.log("columnData.length>>>"+columnData.length);
      			  		dynamicValues="[";
      			  		 for(var i = 0; i < columnData.length; ++i){
      			  		   //do something with obj[i]
      			  		   for(var ind in columnData[i]) {
      			  		        //console.log("{");
      			  		      dynamicValues=dynamicValues+"{";
      			  		        for(var vals in columnData[i][ind]){
      			  		        	var str="";var kys="";
      			  		        	str=columnData[i][ind][vals]+"";
      			  		        	if(vals!='id')
      			  		        		str = '"' + str.replace(/^"*|"*$/, '') + '"';
      			  		        	kys= '"' + vals.replace(/^"*|"*$/, '') + '"';
      			  		            //console.log("kys value>>>"+kys, str);
      			  		        	dynamicValues=dynamicValues+kys+":"+str+",";
      			  		        }
      			  		        //console.log("}");
      			  		      dynamicValues=dynamicValues.replace(/,(?=[^,]*$)/, '');
      			  		      dynamicValues=dynamicValues+"}";
      			  		   }
      			  		   if(i!=(columnData.length-1)){
      			  		   		//console.log(",");
      			  		   		dynamicValues=dynamicValues+",";
      			  		   }
      			  		} 
      			  		dynamicValues=dynamicValues+"]";
      			  		//console.log("dynamicValues>>>"+dynamicValues);
      			  		//alert("cols>>>"+cols);
      			  		//alert("colData>>>"+colData);	  	
      			  		//loadPivotTable(dynamicValues); 		
      			  		loadPivotNRecoTable(dynamicValues);				
      				 	},
      		 		    error : function(data){					
      		 				//closeLoaderIcon();
      		 			},
      		 			complete : function(data){				
      		 				//closeLoaderIcon();
      		 			}
      				}
      			); 
    		}else{
    			callAlert("Please Choose Engagement...");
    		}
      	}
       
       function loadPivotNRecoTable(dynFieldValues){
      	 //alert("hi...");
	        	 var testString = '' + dynFieldValues +'';
	     		/* console.log("testString>>>"+testString);
				var json = $.parseJSON(testString);
				var json = JSON.stringify(eval("(" + testString + ")")); */
				var obj = "";
				obj=JSON.parse(testString);
				console.log("obj>>>>"+obj);
				//var sampleData = dynamicValues; // loaded from sampledata.js
				
				var nrecoPivotExt = "";
				nrecoPivotExt=new NRecoPivotTableExtensions({
					wrapWith: '<div class="pvtTableRendererHolder"></div>',  // special div is needed by fixed headers when used with pivotUI
					fixedHeaders : true
				});
				
				var stdRendererNames = ""; 
				var wrappedRenderers = "";
				
				stdRendererNames = ["Table","Table Barchart","Heatmap","Row Heatmap","Col Heatmap"];
				wrappedRenderers = $.extend( {}, $.pivotUtilities.renderers);
				$.each(stdRendererNames, function() {
					var rName = "";
					rName=this;
					wrappedRenderers[rName] = nrecoPivotExt.wrapTableRenderer(wrappedRenderers[rName]);
				});

				$('#samplePivotTable2').pivotUI(obj, {
					renderers: wrappedRenderers,
					rendererOptions: { sort: { direction : "desc", column_key : [ ]} },
					vals: [],
					rows: [],
					cols: [],
					aggregatorName : "Sum",
					onRefresh: function (pivotUIOptions) {
						// this is correct way to apply fixed headers with pivotUI
						nrecoPivotExt.initFixedHeaders($('#samplePivotTable2 table.pvtTable'));
					}
				});
       }
       
       function saveTemplate() {
    	   var reportData = $('#samplePivotTable2 .pivotExportData').data('getPivotExportData')();
    	    alert("reportData>>"+reportData);
    	    console.log("reportData>>>>"+reportData);
    	}
</script>						

<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>

<!-- the jScrollPane script -->
<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

<script src="files/lib/metronic/theme/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script> 
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>