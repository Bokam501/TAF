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
<link href="css/customStyle.css" rel="stylesheet" type="text/css">

<link rel="stylesheet" href="css/jquery.ui.timepicker.css?v=0.3.2" type="text/css" />
<link rel="stylesheet" href="js/datatable/jquery.dataTables.css" type="text/css" media="all">
<link rel="stylesheet" href="js/datatable/dataTables.tableTools.css" type="text/css" media="all">
<link rel="stylesheet" href="css/customizeDataTable.css" type="text/css" media="all">
<link rel="stylesheet" href="css/jquery.alerts.css" type="text/css" media="all">
<link rel="stylesheet" href="css/iosOverlay.css" type="text/css" media="all">

<!-- END THEME STYLES -->
<link href="css/bootstrap-select.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet" type="text/css"></link>
<link href="css/customStyle.css" rel="stylesheet" type="text/css"></link>
<link rel="stylesheet" href="js/datatable/dataTables.jqueryui.min.css" type="text/javascript" media="all">
  <div><jsp:include page="dataTableHeader.jsp"></jsp:include></div> 
<style type="text/css">
th, td {
    white-space: nowrap;
}
.col-md-1,.col-md-4{
	padding-right: 5px !important;
	padding-left: 0px !important;
}
#activitiesStatusTable_wrapper >.DTTT_container{
	float: right;
}
</style>
</head>
<body>
<input type="hidden" id="hdnDateSet" value="no"/>
<!-- BEGIN HEADER -->
<div class="page-header">
	<!-- BEGIN HEADER TOP -->
	<div><%@include file="headerlayout.jsp" %></div>	
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
<div id="reportbox" style="display: none;"></div>
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
 								<span class="caption-subject theme-font bold uppercase">Activities Status Report</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
								<span class="caption-helper hide">weekly stats...</span>
							</div>
						</div>
						<div class="portlet-body">
							<div class="row list-separated" style="margin-top:0px;">	
							<!-- jtable started -->
								<!-- Main -->
								    <div id="main" style="float:left; padding-top:0px; width:100%;">
							<div class="col-md-1"> Product :</div>  
							
							<div id="productList_dd" class="col-md-4">									
								<select class="form-control input-small select2me" id="productList_ul">
								</select>										
							</div>
							<div class="col-md-1"> Date :</div>  
							<div class="col-md-4">
								<div class="input-group" id="planned_defaultrange">								
									<input type="text" class="form-control" id="planned_fromTodatepicker" readonly style="background-color:white"/>
									<span class="input-group-btn">
									<button class="btn default date-range-toggle" type="button" style="height:34px"><i class="fa fa-calendar"></i></button>
									</span>
								</div>
							</div>
							<button id="generateReport" class="btn green-haze" onclick="generateActiviesStatusReport()">Generate Report</button>
						
					<div id="hidden"></div>

					<!-- ContainerBox -->
				 	<div id="reporttable">
					 	<!-- <div id="jTableContainer"></div> -->  
						<table id="activitiesStatusTable"   class="display" cellspacing="0" width="100%">
							<thead>
							<tr></tr>
							</thead>
						</table>					
						<div class="cl">&nbsp;</div>		
					</div>					
						<!-- End Container Box -->					
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
<input type="hidden" id="hdnDateSet" value="no"/>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/jquery.vmap.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.sparkline.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="files/lib/metronic/theme/assets/admin/layout/scripts/quick-sidebar.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jstree/dist/jstree.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->

<script src="js/moment.min.js" type="text/javascript"></script>
<script src="js/daterangepicker.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>

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

<!-- Calender -->
<script type="text/javascript" src="js/jquery.ui.timepicker.js?v=0.3.2"></script>

<!-- datatable -->
<!-- <script type="text/javascript" src="js/datatable/jquery-1.11.1.min.js"></script> -->
<script type="text/javascript" src="js/datatable/jquery.dataTables.min.js"></script>
<script src="js/datatable/dataTables.jqueryui.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/datatable/jquery.dataTables.columnFilter.js"></script>
<script type="text/javascript" src="js/datatable/dataTables.fixedHeader.js"></script>
<script type="text/javascript" src="js/datatable/dataTables.tableTools.js"></script>

<script type="text/javascript">
 var productId;
 var productSelectedId;
jQuery(document).ready(function() {	
   //QuickSidebar.init(); // init quick sidebar
  // $("#menuList li:first-child").eq(0).remove();
   ComponentsPickers.init();
   setBreadCrumb("Reports");
   loadProductsBasedOnUserId();
 	//statusReportDate();
 	
 	
	  plannedStartDate = DaterangePicker.fromDate();
	  plannedEndDate = DaterangePicker.toDate();
 	  cb(moment().subtract('days', 30), moment()); 
	//$("#planned_fromTodatepicker").daterangepicker();
	
	$('#productList_ul').change(function(){
		$("#hdnDateSet").val("yes");
		generateActiviesStatusReport();
	}); 

   /* $(function() {
		$( "#datepickerFrom" ).datepicker();
		$( "#datepickerTo" ).datepicker();
		
		$( "#datepickerFrom" ).change(function() {
		});
		
		
		$( "#datepickerTo" ).change(function() {
		});
		
	}); */
   
});
$(document).on("click",".daterangepicker .applyBtn", function(e) {
	$("#hdnDateSet").val("yes");
});
var SOURCE;
var treeData;
var workPackageId = "";
var shiftId = "";
var urlToGetActivityStatusReport = "";

var ratings = {'1': '1','2': '2', '3': '3','4': '4', '5': '5'};

var DaterangePicker = {
		fromDate : function(){
			//$("#fromTodatepicker").daterangepicker();
			var datepickerStartDate = $(".daterangepicker_start_input>input").val();
			datepickerStartDate = ((typeof datepickerStartDate == "undefined") || (datepickerStartDate == ''))? new Date() : new Date(datepickerStartDate);
			datepickerStartDate = (datepickerStartDate.getMonth() + 1) + "/" + datepickerStartDate.getDate() + "/" + datepickerStartDate.getFullYear();
			return datepickerStartDate;
		},
		toDate : function(){
			//$("#fromTodatepicker").daterangepicker();
			var datepickerEndDate = $(".daterangepicker_end_input>input").val();
			datepickerEndDate = ((typeof datepickerEndDate == "undefined") || (datepickerEndDate == '')) ? new Date() : new Date(datepickerEndDate);
			datepickerEndDate = (datepickerEndDate.getMonth() + 1) + "/" + datepickerEndDate.getDate() + "/" + datepickerEndDate.getFullYear();		
			return datepickerEndDate;
		}
	}

function exportToExcel() {
	var workPackageId = 1;
	var workPackageName = "SampleWorkpackage";
	var productName = "msnonline";
	var url = "workpackage.testcaseplan.report?workPackageId="+ workPackageId +'&reportMode=XLS';
	var thediv = document.getElementById('reportbox');
	if (thediv.style.display == "none") {
		$
				.blockUI({
					theme : true,
					title : 'Please Wait',
					message : '<h4><img src="css/images/ajax-loader.gif" />Processing..</h4>'
				});
		$
				.post(
						url,
						function(data) {
							var contextpath = (window.location.pathname)
									.split("/", 2);
							var root = window.location.protocol
									+ "//"
									+ window.location.host
									+ "/"
									+ contextpath[1]
									+ "/report/export/WorkPackageTestCaseExecutionPlan/TFWF_WPTCEP-"
									+ productName + "-" + workPackageName
									+ ".xlsx";
							thediv.style.display = "";
							thediv.innerHTML = "<table width='100%' height='100%'><tr><td align='center' valign='middle' width='100%' height='100%'><br><br><a href="+root+" target = '_blank'><h5><font color = 'yellow'>REPORTS GENERATED. CLICK HERE TO DOWNLOAD.</font></h5></a><br><br><br><br><a href='#' onclick='return exportToExcel();'>CLOSE WINDOW</a></td></tr></table>";
							$.unblockUI();
						});
	} else {
		thediv.style.display = "none";
		thediv.innerHTML = '';
	}
	return false;
}
	
$("#activitiesStatusTable tfoot input" ).on( 'keyup change', function () {
    that
    	.search( this.value, true, false )
        .draw();
} );

function generateActiviesStatusReport(){
	try{
		if ($('#activitiesStatusTable').length>0) {
			$("#reporttable").children(":last").remove();
			$('#activitiesStatusTable').remove();
			var htmlTable = '<table id="activitiesStatusTable"><thead><tr></tr></thead></table>';
			$("#reporttable").append(htmlTable);
		}
	} catch(e) {}
	
	if($("#hdnDateSet").val() == "no"){
		callAlert('Please pick \'From Date\' and \'To Date\'');
		return false;		
	}	
	if ((DaterangePicker.fromDate() == null)  || (DaterangePicker.toDate() == null) ){	
		callAlert('Please pick \'From Date\' and \'To Date\'');
		return false;
	}
 	productId = $("#productList_ul").find('option:selected').attr('id');
	productId = (typeof productId == 'undefined') ? -1 : productId;
	productSelectedId=productId;
	urlToGetActivityStatusReport = 'report.activity.status?productId='+productSelectedId+'&fromDate='+DaterangePicker.fromDate()+'&toDate='+DaterangePicker.toDate();
	 
	 var scrollValue = "";
	 if(window.innerWidth < 1600){
		 scrollValue = "250px";
	 }
	 if(window.innerWidth > 1600){
		 scrollValue = "500px";
	 }
		openLoaderIcon(); 
		$.ajax(
		{
		    type: "POST",
		    url : urlToGetActivityStatusReport,
		    cache:false,
		    success: function(data) {
		    	closeLoaderIcon();
		    	
				$("#reporttable").show();			
				var data1=eval(data);
		  		var cols = data1[0].COLUMNS;
		  		var columnData=data1[0].DATA;
		  		if(columnData == undefined){
		  			columnData = [];
		  		}
		  			  	
			   	 $('#activitiesStatusTable').dataTable({			   		
			   		paging: true,			
					destroy: true,
					bJQueryUI: false,
					"scrollX":true,
					"scrollY":"100%",
					dom: 'T<"clear">lfrtip',
					 tableTools: {
				            //"sSwfPath": "/tfwp/js/swf/copy_csv_xls_pdf.swf"
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
			   		        "bDeferRender": true,
			   		        "bInfo" : false,
			   		        "bSort" : false,
			   		        "bDestroy" : true,
			   		        "bFilter" : false,
			                 /* "fnRowCallback": function( nRow, aData, iDisplayIndex) {
			                	$.each(aData, function(i,item){
			                		
			                	});	                
			                }, */ 
			   		        "bPagination" : false,
			   		        "aaData": columnData,
			   		        "aoColumns": cols
			   		    });	   			
		  						
				 	},
		 		    error : function(data){					
		 				closeLoaderIcon();
		 			},
		 			complete : function(data){				
		 				closeLoaderIcon();
		 			}
				}
			);
			
	/*  } else {
		thediv.style.display = "none";
		thediv.innerHTML = '';
	}  */
	  
}

function stopPropagation(evt) {
	if (evt.stopPropagation !== undefined) {
		evt.stopPropagation();
	} else {
		evt.cancelBubble = true;
	}
}

function removeElement() {
	$.each($('#activitiesStatusTable > tfoot>tr>th'), function(index, item){
		if($(item).attr("colspan")>1) $(this).children().remove();
	});
}


function loadProductsBasedOnUserId(){		
	$('#productList_ul').empty();				
	$.post('common.list.user.associated.products',function(data) {	
        var ary = (data.Options);
        $.each(ary, function(index, element) {
    		$('#productList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
  		 });
   		$('#productList_ul').select2();	
   		
   		productId = $("#productList_ul").select2().find('option:selected').attr('id');
   		generateActiviesStatusReport();
   		productSelectedId= parseInt(productId);
   		console.log( productSelectedId);
   		
   		urlToGetActivityStatusReport = 'report.activity.status?productId='+productSelectedId+'&fromDate='+DaterangePicker.fromDate()+'&toDate='+DaterangePicker.toDate();
   		console.log(urlToGetActivityStatusReport);
   				
   		//    	$('#productList_ul').select2().val();	       
	});
}

/* function statusReportDate() {
    var month = new Array();
    var month = [ "January", "February", "March", "April", "May", "June",
                 "July", "August", "September", "October", "November", "December" ];
    var d = new Date();
    var currentmonth = month[d.getMonth()];
    var lastmonth = month[d.getMonth()-1];
    var currentdate = d.getDate();
    var year = d.getFullYear();
    document.getElementById("planned_fromTodatepicker").value = (lastmonth+' '+(currentdate-1)+','+' '+year+' '+'-'+' '+currentmonth+' '+currentdate+','+year);
    	$("#hdnDateSet").val("yes");
     generateActiviesStatusReport(); 
  }
 */

function cb(start, end) {
	 $("#hdnDateSet").val("yes");
	document.getElementById('planned_fromTodatepicker').value = (start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
} 

</script>						
<!-- END JAVASCRIPTS -->
<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
</body>
<!-- END BODY -->
</html>