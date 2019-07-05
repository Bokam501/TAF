<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

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
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet" type="text/css"></link>

<link rel="stylesheet" href="js/datatable/jquery.dataTables.css" type="text/css" media="all">
<link rel="stylesheet" href="js/datatable/dataTables.tableTools.css" type="text/css" media="all">
<link rel="stylesheet" href="css/customizeDataTable.css" type="text/css" media="all">
<link rel="stylesheet" href="https://code.jquery.com/ui/1.11.3/themes/smoothness/jquery-ui.css" type="text/css" media="all"> 
	
<link rel="stylesheet" href="js/Scripts/star-rating/jquery.rating.css" type="text/css">
<link rel="stylesheet" href="css/iosOverlay.css" type="text/css" media="all">
<link href="css/customStyle.css" rel="stylesheet" type="text/css"> 
<link rel="stylesheet" href="js/datatable/jquery-ui.css" type="text/javascript" media="all">

<!-- <link href="files/lib/metronic/theme/assets/admin/layout3/css/layout.css" rel="stylesheet" type="text/css"> -->
<link rel="stylesheet" href="js/datatable/dataTables.jqueryui.min.css" type="text/javascript" media="all">

<link href="css/customStyle.css" rel="stylesheet" type="text/css">
<!-- END THEME STYLES -->
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body>
<!-- BEGIN HEADER -->
<div id="reportbox" style="display: none;"></div>
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
					<div class="portlet light " style="height:64%">
						<div class="portlet-title" style="padding:0px;margin-bottom:5px;min-height:40px;margin-top:-5px">
							<div class="caption caption-md">
								<span class="caption-subject theme-font bold uppercase">Environment Combination Reports</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
							</div>
						</div>
						<div class="portlet-body form"  >
						<div class="row " >
							<div class="col-md-12">
					        <ul class="nav nav-tabs" id="tabslist">
								<li class="active"><a href="#TestCase" data-toggle="tab">TestCase</a></li>
								<li><a href="#Tester" data-toggle="tab">Tester</a></li>
								<li><a href="#TimeSheet" data-toggle="tab">TimeSheet</a></li>
							</ul>
							</div>																
						</div>
						<div id="hidden"></div>
						<div class="tab-content">
							<!-- TestCase Tab -->
							<div id="TestCase" class="TestCase tab-pane fade active in" style="overflow: hidden; overflow-y: scroll; height: 73%;">
								<!-- ContainerBox -->
								<div id="environmentContent_Testcase" class="jScrollContainerfilter">														
								</div>
								<!-- End Container Box -->
								</div>
								<!-- TestCase Tab Ends -->
								<!-- Tester  -->
								<div id="Tester" class="tab-pane fade" >
										<!-- ContainerBox -->
									<div id="environmentContent_Tester" style="display: none" class="jScrollContainerfilter">														
									</div>
								<!-- End Container Box -->
								</div>
								<!-- Tester End -->
								<!-- TimeSheet  -->
								<div id="TimeSheet" class="tab-pane fade" >
										<!-- ContainerBox -->
											<div id="environmentContent_TimeSheet" style="display: none"	class="jScrollContainerfilter">
											</div>
								<!-- End Container Box -->
								</div>
								<!-- TimeSheet End -->
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
<!-- Popup -->

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

<script src="js/moment.min.js" type="text/javascript"></script>
<script src="js/daterangepicker.js" type="text/javascript"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>
<script src="js/select2.min.js" type="text/javascript"></script>

<script src="js/datatable/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="js/datatable/dataTables.jqueryui.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/datatable/dataTables.tableTools.js"></script>

<script type="text/javascript">
var urlToGetAnalyseDefects;
var productId;
var productVersionId;
var productBuildId;
var workpackageId;
bugIdVal=0;

function showWorkPackageTreeData(){	
	var jsonObj={"Title":"",				
	    	 	"urlToGetTree": 'administration.workpackage.plan.tree?type=1',
	    	 	"urlToGetChildData": 'administration.workpackage.plan.tree?type=1',	    	 	
	 };	 
	 TreeLazyLoading.init(jsonObj);	 
}


jQuery(document).ready(function() {	
QuickSidebar.init(); // init quick sidebar
setBreadCrumb("Reports");
createHiddenFieldsForTree();
ComponentsPickers.init();
setPageTitle("Products");
showWorkPackageTreeData();
//getTreeData('administration.workpackage.plan.tree?type=1');
 $("#treeContainerDiv").on("select_node.jstree",
	     function(evt, data){				 	
	 		if(data.node.icon=='fa fa-close' || data.node.icon=='fa fa-lock'){
	 			editableFlag=false;
	 		}else{
	 			editableFlag=true;
	 		}
	 		var entityIdAndType =  data.node.data;			 		
   			var arry = entityIdAndType.split("~");
   			key = arry[0];
   			var type = arry[1];
   			title = data.node.text;
   		 	var loMainSelected = data;
	        uiGetParents(loMainSelected);
		    nodeType = type;
		    if(nodeType=='WorkPackage')
		   	{
		    	workPackageId = key;				    	
		    	document.getElementById("treeHdnCurrentWorkPackageId").value = workPackageId;
	    	 	selectedTab = $("#tabslist>li.active").index();
	    	 	tabSelection(selectedTab);	    	 	
		   	}
	 }
);
	
});

$(document).on('click', '#tabslist>li', function(){
	selectedTab=$(this).index();
	tabSelection(selectedTab);
});

function tabSelection(selectedTab){	
	if(selectedTab == 0){
		urlTestCase='environment.combination.based.report.testcase?workPackageId='+workPackageId;
    	listEnvCombOfTestCase(urlTestCase);
	}else if(selectedTab == 1){
		urlTester='environment.combination.based.report.tester?workPackageId='+workPackageId;
		listEnvCombOfTester(urlTester);
	}else if(selectedTab == 2){
		urlTimeSheet='environment.combination.based.report.timesheet?workPackageId='+workPackageId;
		listEnvCombOfTimesheet(urlTimeSheet);
	}	
}

// TestCase Scope Starts 

	function listEnvCombOfTestCase(urlTestCases){				
		scrollValue = clearDataTableValues('#environment_dataTable_Testcase', '#environmentContent_Testcase');
		
		var htmlTable = '<table id="environment_dataTable_Testcase" style="font-size: 12px;" class="display" cellspacing="0" width="100%"><thead><tr><th></th><th></th><th colspan="3">Execution Test Cases</th>'+
				'</tr><tr><th>Tester Name</th><th>Environment Combination</th><th>Total Test Cases</th><th>Executed Test Cases</th><th>Not Executed Test Cases</th>'+
				'</tr></thead><tfoot><tr><th>Total</th><th></th><th></th><th></th><th></th></tr></tfoot></table>';	
		$('#environmentContent_Testcase').append(htmlTable);

		openLoaderIcon();
		$.ajax({
		    type: "POST",
		    dataType : 'json',
		    url : urlTestCases,
		    cache:false,
		    success: function(data) {
				closeLoaderIcon();
		    	if(data.Result=="ERROR"){					
					$("#noDataTable").show().find("label").text(data.Message);
					 return false;
				 }else{
		 	    	if(data.Result=='OK'){
			    		//if(data.Records.length>0){			    			
			    			var data=data.Records;
			    			var table=	$('#environment_dataTable_Testcase').dataTable( {
			    				paging: true,
			    				destroy: true,
			    				searching: false,
			    				bJQueryUI: true,			    				

			    			"scrollX":true,
			    			"scrollY":"100%",
			    			dom: 'T<"clear">lfrtip',
		    				 tableTools: {
		    			            "sSwfPath": "//cdn.datatables.net/tabletools/2.2.2/swf/copy_csv_xls_pdf.swf"
		    			        },
		    				 oTableTools: {
		    			            "aButtons": [
		    			                "copy",
		    			                "print",
		    			                {
		    			                    "sExtends":    "collection",
		    			                    "sButtonText": "Save",
		    			                    "aButtons":    [ "csv", "xls", "pdf" ]
		    			                }
		    			            ]
		    			        },
			    				aaData:data,
			    				 "columnDefs": [ {
			    			            "visible": false,
			    			           // "targets": 0
			    			        } ],
			    			        bAutoWidth : true,
				    				bSort : false,
				    			aoColumns: [

				    				        { mData: "testerName" },
			    				            { mData: "environmentCombinationName" },
			    				            { mData: "totalExecutionTCs" },
	                                        { mData: "totalExecutedTesCases" },
				    				        { mData: "notExecuted" },

			    				        ],

			    				        "footerCallback": function ( row, data, start, end, display ) {
					    					var api = this.api(), data;
					    				    // Remove the formatting to get integer data for summation
					    				    var intVal = function ( i ) {
					    				 		return typeof i === 'string' ?
					    				        i.replace(/[\$,]/g, '')*1 :
					    				        typeof i === 'number' ?
					    				        i : 0;
					    				     };
					    				     // Total over all pages
				    				            //Open Bugs
				    				            shifttotal = api
				    				                .column(2)
				    				                .data()
				    				                .reduce( function (a, b) {
				    				                	 Number.prototype.padDigit = function () {
				    				                	        return (this < 10) ? '0' + this : this;
				    				                	    };
				    				                	 if(a==0 || a==null){
					    				                		a='0';
					    				                	}
				    				                	 if(b==0 || b==null){
					    				                		b='0';
					    				                	}

				    				                     var op1= Number(a);
				    				                     var op2 = Number(b);
				    				                     //console.log(op1  + op2);
				    				                    return op1+op2;
				    				                } );
				    				            // Total over this page

				    				            // Update footer
				    				            $( api.column(2).footer() ).html(
				    				                shifttotal
				    				            );
					    				  // Total over all pages
				    				            //Open Bugs
				    				            shifttotal = api
				    				                .column(3)
				    				                .data()
				    				                .reduce( function (a, b) {
				    				                	 Number.prototype.padDigit = function () {
				    				                	        return (this < 10) ? '0' + this : this;
				    				                	    };
				    				                	 if(a==0 || a==null){
					    				                		a='0';
					    				                	}
				    				                	 if(b==0 || b==null){
					    				                		b='0';
					    				                	}

				    				                     var op1= Number(a);
				    				                     var op2 = Number(b);
				    				                    // console.log(op1  + op2);
				    				                    return op1+op2;
				    				                } );
				    				            // Total over this page

				    				            // Update footer
				    				            $( api.column(3).footer() ).html(
				    				                shifttotal
				    				            ); 
					    				  // Total over all pages
				    				            //ReferredBack
				    				            shifttotal = api
				    				                .column(4)
				    				                .data()
				    				                .reduce( function (a, b) {
				    				                	 Number.prototype.padDigit = function () {
				    				                	        return (this < 10) ? '0' + this : this;
				    				                	    };
				    				                	 if(a==0 || a==null){
					    				                		a='0';
					    				                	}
				    				                	 if(b==0 || b==null){
					    				                		b='0';
					    				                	}

				    				                     var ref1= Number(a);
				    				                     var ref2 = Number(b);
				    				                    return ref1+ref2;
				    				                } );
				    				            // Total over this page

				    				            // Update footer
				    				            $( api.column(4).footer() ).html(
				    				                shifttotal
				    				            );

			    				        }			    				        
			    			});
			    			
			    		//}
		 	    	}
				 }
		    },
			error : function(data){
				closeLoaderIcon();
			},
			complete : function(data){
				closeLoaderIcon();				
				setTimeout( function(){ $(environment_dataTable_Testcase).dataTable().fnAdjustColumnSizing();},100);
			}
		});			 
	}

	//TestCase Scope Ends

	//Tester Scope Starts

	function listEnvCombOfTester(urlTesters) {		 
		 scrollValue = clearDataTableValues('#environment_dataTable_Tester', '#environmentContent_Tester');		 
				
				var htmlTable ='<table id="environment_dataTable_Tester" style="font-size: 12px;" class="display" cellspacing="0" width="100%"><thead><tr></tr></thead></table>';;	
				$('#environmentContent_Tester').append(htmlTable);	
				
				openLoaderIcon();		
				$.ajax(
				{
			    type: "POST",
			    url : urlTesters,
			    cache:false,
			    success: function(data) {				
					closeLoaderIcon();
			    		$("#environmentContent_Tester").show();	
						var data1 = eval(data);
						var cols = data1[0].COLUMNS;
						var columnData = data1[0].DATA;
						$('#environment_dataTable_Tester').dataTable({
											paging : true,
											destroy : true,
											bJQueryUI : false,
											"scrollX" : true,
											"scrollY":"100%",
											dom : 'T<"clear">lfrtip',
											tableTools : {
												"sSwfPath" : "//cdn.datatables.net/tabletools/2.2.2/swf/copy_csv_xls_pdf.swf"
											},
											oTableTools : {
												"aButtons" : [
														"copy",
														"print",
														{
															"sExtends" : "collection",
															"sButtonText" : "Save",
															"aButtons" : ["csv","xls","pdf" ]
														} ]
											},
											"bDeferRender" : true,
											"bInfo" : false,
											"bSort" : false,
											"bDestroy" : true,
											"bFilter" : false,
											"fnRowCallback" : function(nRow,
											aData, iDisplayIndex) {
												$.each(aData, function(i, item) {
											});
											},
											"bPagination" : false,
											"aaData" : columnData,
											"aoColumns" : cols
										});
						
					},
					error : function(data){
						closeLoaderIcon();
					},
					complete : function(data){
						closeLoaderIcon();
						setTimeout( function(){ $(environment_dataTable_Tester).dataTable().fnAdjustColumnSizing();},100);
					}
				});

	}

	//Tester Scope Ends

	//TimeSheet Scope Starts

	function listEnvCombOfTimesheet(urlTimeSheets) {
		 scrollValue = clearDataTableValues('#environment_dataTable_TimeSheet', '#environmentContent_TimeSheet');		 
			
			var htmlTable ='<table id="environment_dataTable_TimeSheet" style="font-size: 12px;" class="display" cellspacing="0" width="100%"><thead><tr></tr></thead><tfoot><tr><th colspan="5">Total</th><th></th></tr></tfoot></table>';	
			$('#environmentContent_TimeSheet').append(htmlTable);	
			
			openLoaderIcon();		
			$.ajax(
			{
		    type: "POST",
		    url : urlTimeSheets,
		    cache:false,
		    success: function(data) {				
				closeLoaderIcon();
		    		$("#environmentContent_TimeSheet").show();	
					var data1 = eval(data);
					var cols = data1[0].COLUMNS;
					var columnData = data1[0].DATA;
					var total = data1[0].TOTAL;
					$('#environment_dataTable_TimeSheet').dataTable({
										paging : true,
										destroy : true,
										bJQueryUI : true,
										"scrollX" : true,
										"scrollY":"100%",
										dom : 'T<"clear">lfrtip',
										tableTools : {
											"sSwfPath" : "//cdn.datatables.net/tabletools/2.2.2/swf/copy_csv_xls_pdf.swf"
										},
										oTableTools : {
											"aButtons" : [
													"copy",
													"print",
													{
														"sExtends" : "collection",
														"sButtonText" : "Save",
														"aButtons" : ["csv","xls","pdf" ]
													} ]
										},
										"bDeferRender" : true,
										"bInfo" : false,
										"bSort" : false,
										"bDestroy" : true,
										"bFilter" : false,
										"fnRowCallback" : function(nRow,
										aData, iDisplayIndex) {
											$.each(aData, function(i, item) {
										});
										},
										"bPagination" : false,
										"aaData" : columnData,
										"aoColumns" : cols,
										"footerCallback": function ( row, data, start, end, display ) {
												var api = this.api(), data;
												// Update footer
												$(api.column(5).footer()).html(total);

											}

										});
					},
					error : function(data) {
						closeLoaderIcon();
					},
					complete : function(data) {
						closeLoaderIcon();
						setTimeout( function(){ $(environment_dataTable_TimeSheet).dataTable().fnAdjustColumnSizing();},100);
					}
				});
	}
	//TimeSheet Scope Ends
	
	function stopPropagation(evt) {
		if (evt.stopPropagation !== undefined) {
			evt.stopPropagation();
		} else {
			evt.cancelBubble = true;
		}
	}

	function clearDataTableValues(idValue, idValueParent) {
		try {
			if ($(idValue).length > 0) {
				$(idValueParent).children().remove();
				$(idValue).remove();
			}

		} catch (e) {
		}

		if (window.innerWidth < 1600) {
			scrollValue = "250px";
		}
		if (window.innerWidth > 1600) {
			scrollValue = "500px";
		}

		return scrollValue;
	}
</script>

<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>