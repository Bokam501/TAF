<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="com.hcl.atf.taf.controller.HomePageController"%>
<%@page import="com.hcl.atf.taf.model.UserList"%>
<%@page import="java.util.List"%>
<%@page import="com.hcl.atf.taf.model.TestRunList"%><html xmlns="http://www.w3.org/1999/xhtml">

<head>
<link rel="shortcut icon" href="css/images/logo_new.png">
<meta http-equiv="Content-type" content="text/html; charset=utf-8">

<link type="text/css" href="css/jquery.jscrollpane.css" rel="stylesheet" media="all" />
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">

<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/black/jtable.min.css" rel="stylesheet" type="text/css" />

<!--[if IE]>
<style type="text/css" media="screen">
.shell {background-image: none;filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='css/images/shell-bg.png', sizingMethod='scale');}
.box{background-image: none;filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='css/images/dot.png', sizingMethod='scale');}
.transparent-frame .frame{background-image: none;filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='css/images/transparent-frame.png', sizingMethod='image');}
.search .field{padding-bottom:9px}
</style>
<![endif]-->
<script src="js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="js/jquery-ui-1.10.0.min.js" type="text/javascript"></script>
<script src="js/jquery-func.js" type="text/javascript"></script>
<!-- the mousewheel plugin -->
<script type="text/javascript" src="js/jquery.mousewheel.js"></script>
<!-- the jScrollPane script -->
<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>
<!-- Include jTable script file. -->
<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>
<script type="text/javascript" src="js/Scripts/SessionTimeout/jquery.sessionTimeout.js"></script>
<script type="text/javascript" src="js/Scripts/popup/jquery.alerts.js"></script>

<link href="css/customStyle.css" rel="stylesheet" type="text/css"></link>
<!-- END THEME STYLES -->
</head>
<body>

  <!-- Header -->
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
  <!-- End Header -->
  
  <!-- Content -->
<div class="page-container">
	<div class="page-content" >  
		<div class="container-fluid" id="content">
			<div class="row margin-top-10" style="height:70%">
				<div class="col-md-12">
				</div>
			</div>
		</div>
	</div>
</div>
  <!-- End Content -->
	<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp" %></div>			
	<!-- END FOOTER -->

<div id="div_PopupMain" class="modal fade" tabindex="-1" aria-hidden="true" style="display:none;top:25%">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header" id="div_PALMBuildDetailsHeader">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="popupClose()"></button>
					<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:200px" data-always-visible="1" data-rail-visible1="1">
					<div class="row">
						<div class="col-md-12">
							<div id="div_PALMDescription"></div>
							<br>
							<div id="div_PALMProjectName"></div>
							<div id="div_PALMVersionNumber"></div>
							<div id="div_PALMBuildId"></div>
							<div id="div_PALMBuildDate"></div>
							<div id="div_PALMDBVersion"></div>
							<div id="div_PALMLicenseExpiryDate"></div>
							<div id="div_PALMcopyRight">© Copyright 2014 - 2019<a href="http://www.hcl.com"> HCL Technologies Ltd.</a></div>
							
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
	
	<script type="text/javascript">
		$(document).ready(function()
		{
			/* $.sessionTimeout({
			        warnAfter: 1800000,
			        redirAfter: 1810000
			  });*/
			 showPalmDetails();
			 modeChangeAboutUsHandler();
		});
					
		var installation_mode_description_ilcm = 'Integrated Life Cycle Management (iLCM) is a service delivery platform that supports workflow, resource management and reporting in different test engagement models. It integrates People, Process & Technology to address operational inefficiencies & gaps in planning.';
		var installation_mode_description_ATLAS = 'Augmented Test Lifecycle Autonomics Solutions(ATLAS) is a service delivery platform that supports workflow, resource management and reporting in different test engagement models. It integrates People, Process & Technology to address operational inefficiencies & gaps in planning.';
		var installation_mode_description_taf = 'Test Automation Framework (TAF) is a service to automate the process of testing mobile apps / web Execution through scripts.'; 
		var installation_mode_description_ers_practice_deployment = 'ERS Practice Deployment is a service delivery platform that supports workflow, resource management and reporting in different test engagement models. It integrates People, Process & Technology to address operational inefficiencies & gaps in planning.';
		function modeChangeAboutUsHandler(){
			$("#div_PALMProjectName").html(modeTitleHandler());
			$("#div_PALMBuildDetailsHeader >.modal-title").text(modeAboutUsHandler());
			
			if(modeSelection.toLowerCase() == title_ilcm.toLowerCase()){
				$("#div_PALMDescription").text(installation_mode_description_ilcm);				
			}else if(modeSelection.toLowerCase() == title_ATLAS.toLowerCase()){
				$("#div_PALMDescription").text(installation_mode_description_ATLAS);				
			}else if(modeSelection.toLowerCase() == title_taf.toLowerCase()){
				$("#div_PALMDescription").text(installation_mode_description_taf);				
			}else if(modeSelection.toLowerCase() == title_ers_practice_deployment.toLowerCase()){
				$("#div_PALMDescription").text(installation_mode_description_ers_practice_deployment);				
			}else{
				$("#div_PALMDescription").text(installation_mode_description_ilcm);
			}
		}
		
		function showPalmDetails(){
				$.ajax({
					type: "POST",
			        contentType: "application/json; charset=utf-8",
					url : 'show.palm.build.details',
					dataType : 'json',
					success : function(data) {
						loadPALMBuildDetails(data);
					}
				});
		}
		
		function loadPALMBuildDetails(data){
			if(data.length>0){
				var parsedData = JSON.parse(data);
				var projName = parsedData.ProjectName;
				var projectText = projName;
				var versionNumber = parsedData.VersionNumber;
				var versionNumberText = "Version: "+versionNumber;
				var buildNo = parsedData.BuildNumber;
				var buildText =  "Build : "+buildNo;
				var buildDate = parsedData.Date;
				var buildDateText =  "Build Date : "+buildDate;
				var dbVersion = parsedData.DatabaseVersion;
				var dbVersionText =  "Database Version : "+dbVersion;
				var licenseExpiryDate = parsedData.ExpiryDate;
				var licenseExpiryDateText =  "License Expiry Date : "+licenseExpiryDate;
				// $("#div_PALMProjectName").html(projectText); 
				$("#div_PALMVersionNumber").html(versionNumberText);
				$("#div_PALMBuildId").html(buildText);
				$("#div_PALMBuildDate").html(buildDateText);
				$("#div_PALMDBVersion").html(dbVersionText);
				$("#div_PALMLicenseExpiryDate").html(licenseExpiryDateText);				
			}
			//loadPopup("div_PopupMain");
			$("#div_PopupMain").modal();
		}
		
		function loadPopup(divId) {
			$("#" + divId).fadeIn(0500); // fadein popup div
			$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
																// IE7, IE8
			$("#div_PopupBackground").fadeIn(0001);
		}
			
		function popupClose() {
			$("#div_PopupMain").fadeOut("normal");
			$("#div_PopupBackground").fadeOut("normal");
		}
		</script>
	
	<script>
	$(document).ready(function() {
		$("#menuList li:first-child").eq(0).remove();   // to remove fancytree icon near home tab		
	});  
	</script>
	
	
	

</body>
</html>