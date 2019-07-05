<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@page import="com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseStepEvidenceGridList"%>
<%@page import="com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseStepList"%>
<%@page import="com.hcl.atf.taf.model.custom.TestRunReportsDeviceList"%>
<%@page import="java.util.*"%>
<html xmlns="http://www.w3.org/1999/xhtml"><head>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<!-- End : Include FancyTree Plugins : Rajesh -->
<link type="text/css" href="css/jquery.jscrollpane.css" rel="stylesheet" media="all" />
<!-- <link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all"> -->
<link href="css/bootstrap-select.min.css" rel="stylesheet" type="text/css"></link>

<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link href="js/Scripts/validationEngine/validationEngine.jquery.css" rel="stylesheet" type="text/css" />
<link href="css/codepen.css" rel="stylesheet" type="text/css">
<link href="css/bootstrap-select.min.css" rel="stylesheet" type="text/css"></link>
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

<script src="js/jquery-1.9.1.min.js" type="text/javascript"></script>
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
<!-- <script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script> -->

<!-- Plugs ins for jtable inline editing -->
<!-- <script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.js"></script> -->
<script type="text/javascript" src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>

<!-- Validation engine script file and english localization -->
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>

<script src="js/grid/freewall.js" type="text/javascript"></script>
<!-- Datetimepicker -->
<script type="text/javascript" 	src="js/Scripts/DateTimePicker/anytime.js"></script>
<script type="text/javascript" 	src="js/Scripts/DateTimePicker/jquery.themeswitcher.js"></script>
<link rel="stylesheet" href="js/Scripts/DateTimePicker/anytime.css" />
<!-- End Datetimepicker -->
<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="js/moment.min.js" type="text/javascript"></script>
<script src="js/daterangepicker.js" type="text/javascript"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>

<script type="text/javascript" src="js/Scripts/jqmeter/jqmeter.min.js"></script>
<script type="text/javascript" src="js/Scripts/jqmeter/jquery_simple_progressbar.js"></script>
<script type="text/javascript" 	src="js/Scripts/validationEngine/jquery.blockUI.js"></script>

<!-- <script type="text/javascript" src="js/Scripts/heatColor/jquery.heatcolor.0.0.1.js"></script>
<script type="text/javascript" src="js/Scripts/heatColor/jquery.tablesorter.pack.js"></script> -->

<script type="text/javascript" src="js/Scripts/star-rating/jquery.rating.js"></script>
<script type="text/javascript" src="js/Scripts/popup/iosOverlay.js"></script>

<script type="text/javascript" 	src="js/draganddrop/Sortable.js"></script> 
<script type="text/javascript" 	src="js/draganddrop/ng-sortable.js"></script>
<!-- Newly added Scripts End -->
<script type="text/javascript" 	src="js/jquery.hottie.js"></script>

<script src="js/datatable/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="js/datatable/dataTables.jqueryui.min.js" type="text/javascript"></script>
<!-- <script src="js/jtable/jquery.jtable.toolbarsearch.js" type="text/javascript"></script> -->
<!-- <script src="js/Scripts/popup/jquery.jtable.toolbarsearch.js" type="text/javascript"></script> -->


</head>
<body>

<table style="padding-top: 8px;">
	<div class="cl">&nbsp;</div>
	<tbody>
	
		<% 
		// The first section in the web page - This displays basic information on RunNo level..
		TestRunReportsDeviceCaseStepList testRunReportsDeviceCaseStepList =  (TestRunReportsDeviceCaseStepList)request.getAttribute("testRunReportsList");
		 %>
		<tr id="font-color3"  style="line-height: 24px;">
			<td class="gap" style="width: 1506px;">	Product: <%=testRunReportsDeviceCaseStepList.getProductname()%>
			      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Product&nbsp;&nbsp;Version: <%=testRunReportsDeviceCaseStepList.getProductversionname()%>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Configuration Name:&nbsp;&nbsp;<%=testRunReportsDeviceCaseStepList.getTestrunconfigurationname()%>	
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Run&nbsp;&nbsp;No: <%=testRunReportsDeviceCaseStepList.getTestrunno()%>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Start&nbsp;&nbsp;Time:&nbsp;&nbsp;<%=testRunReportsDeviceCaseStepList.getTeststepstarttime()%></td>
		</tr>
	</tbody>	
	</table>	
 <div id="featureByTestbeds"  >
									<div class="caption caption-md" style="margin:5px 0px -5px 0px;">
										<span class="caption-subject theme-font bold uppercase">Report:</span>
									</div>	
									<!--<table id="featureByTestbeds_dataTable"  style="font-size: 12px;" class="display" cellspacing="0" width="100%">
										 <thead>
											<tr></tr>										
										</thead>						
							        </table> -->
 								</div>
 			<!--   <button onclick="goBack()">Go Back</button> -->
	<a href="report" style="font-size: 18px; padding-left: 570px">Go Back</a>
  	<div class="cl">&nbsp;</div>
  	  
 		
 		<script type="text/javascript">
 		
 var data ='<%=request.getAttribute("DataTable")%>' ;
// data ='[{"DATA":[["evidence.image?imagePath=760&imageFileName=C://Program Files//Apache Software Foundation//Tomcat 7.0//bin//Evidence//403//2015-08-24//tsetwp1-5764-3162-170918.jpg","HomeNavigation-Launch HCL-ERS app-EvidenceEri-EvidenceEriVer-2015-11-15 13:58:29-37304-5051-175820.png"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","MenuNavigation-Open Main-Menu-Home-Main_Menu"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","MenuNavigation-Open Industries-Menu-Home-Industries_Menu"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","MenuNavigation-Open Aerospace-page-Home->Aerospace_&_Defence"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","MenuNavigation-Open Capabilities-Menu-Home-Capabilities_Menu"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","MenuNavigation-Open Embedded-Software-page-Home->Embedded_Software"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","HomeNavigation-Click About-Us-Home-About_Us_Tab"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","HomeNavigation-Click Accleration-Home-About_Us_Tab"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","HomeNavigation-Click Value-Home-About_Us_Tab"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","HomeNavigation-Click Technology-Home-About_Us_Tab"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","HomeNavigation-Click Collaterals-Home-Collaterals_Tab"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","HomeNavigation-Swipe to Videos-Home-Collaterals_Tab"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","HomeNavigation-Swipe to Analyst Reports-Home-Collaterals_Tab"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","HomeNavigation-Click Maps-Home-Maps_Tab"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","HomeNavigation-Click Video-760_HomeNavigation.png"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","MenuNavigation-Open Service-Lines-Menu-Home-Service_Lines_Menu"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","MenuNavigation-Open Application-Test-Factory-page-Home->Application_Test_Factory"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","MenuNavigation-Open Insights-Menu-Home-Insights_Menu"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","MenuNavigation-Open Analyst-Reports-page-Home->Analyst_Reports"],["evidence.image?imagePath=760&imageFileName=C:\Program Files\Apache Software Foundation\Tomcat 7.0\bin\Evidence\403\2015-08-24\tsetwp1-5764-3162-170918.jpg","MenuNavigation-Open Home-Home-Video_Tab"]],"COLUMNS":[{"title":"null-8-8-4d00600307ad3025#evidence.log?logPath=760&logFileName=760-4d00600307ad3025.log"}]}]';
 // data=data.replace("\\","//");
   data=data.replace("/\\/g","//");
  // data=data.replace("/\\/g","//");

var data1=eval(data);
   // console.log(data1);
	var columnData=data1[0].DATA;
	var cols = data1[0].COLUMNS;
	var root = "/Evidence/";
							  	      var content = "<table id='featureByTestbeds_dataTable' class='display datatable' style='width:100%;'><thead>";
content +='<tr>';

							  	 //  var c = columns.length;
							  	   var atarget = [];
							  	   var stitle = []; 
							  	 
							     content += '<th >Test Case - Test Step - ScreenShot Label  </th>';
							  	 $(cols).each(function(index, element){
							  		 var titleName="";
							  		//alert(element.title);
							  	     atarget[index] =index;
							  	     
							  	   titleName=titleName+element.title;
							  	var arr= titleName.split("#");
							  	
							  	 stitle[index] =arr[0];							  	 
							  	   content += '<th >'+arr[0]+'<br><a href='+arr[1]+'>DeviceLog</a></th>';
							  	 });
							   content +=' </tr></thead>';	
							  	  content += "</table>";
							  	$("#featureByTestbeds").append(content);
					    			$('#featureByTestbeds_dataTable').dataTable( {
					    				paging: true,
					    				destroy: true,
					    				searching: true,
					    				//bJQueryUI: false,
					    				"scrollX":true,
					    				"scrollY":"100%",					    				
					    				 "aaData": columnData ,
					    				 "aoColumnDefs":[       
							   		                    {
							   		                    	
							   		                     "mRender": function ( data, type, row ) {
							   		                    	 console.log(row[1])
							   		                   return '<img width="360" BORDER="0" height="600" src="'+data+'"/>';
							   		                 // return '<img width="360" BORDER="0" height="600" src="evidence.image?imagePath=760&imageFileName=C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\bin\\Evidence\\403\\2015-08-24\\tsetwp1-5764-3162-170918.jpg"/>';
							   		                  },
							   		                    	"aTargets":atarget
							   		                    	}
							   		                 ]
					    			});  
							   		                 
							   		        </script>					 

</body>
</html>