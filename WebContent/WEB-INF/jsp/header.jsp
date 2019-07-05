<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link type="text/css" href="css/jquery.jscrollpane.css" rel="stylesheet" media="all" />

<link rel="stylesheet" href="css/style.css" type="text/css" media="all">
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css??4884491531235" rel="stylesheet" type="text/css" />
<!-- Validation engine style file -->
<link href="js/Scripts/validationEngine/validationEngine.jquery.css")" 
      rel="stylesheet" type="text/css" />
 
<script src="js/grid/freewall.js" type="text/javascript"></script>
<script src="js/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script src="js/jquery-ui-1.10.0.min.js" type="text/javascript"></script>
<script src="js/jquery-func.js" type="text/javascript"></script>
<!-- the mousewheel plugin -->
		<script type="text/javascript" src="js/jquery.mousewheel.js"></script>
		<!-- the jScrollPane script -->
		<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>
<!-- Include jTable script file. -->
<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>
<!-- Validation engine script file and english localization -->
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>
<!-- Block UI when AJAX call -->
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.blockUI.js"></script>
<script type="text/javascript" src="js/Scripts/SessionTimeout/jquery.sessionTimeout.js"></script>
<!-- Plugs ins for jtable inline editing -->
<script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
<script type="text/javascript" src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>

<link rel="stylesheet" href="css/jquery.alerts.css" type="text/css" media="all">
<link rel="stylesheet" href="css/iosOverlay.css" type="text/css" media="all">


<!-- Begin : Include FancyTree Plugins : Rajesh -->
<link href="js/fancytree/skins/skin-win8/ui.fancytree.css" rel="stylesheet" type="text/css" />
<script src="js/fancytree/jquery.fancytree.js" type="text/javascript"></script>
<script src="js/fancytree/jquery.fancytree.childcounter.js" type="text/javascript"></script>
<script src="js/fancytree/jquery.fancytree.persist.js" type="text/javascript"></script>
<script src="js/fancytree/jquery.cookie.js" type="text/javascript"></script>
<!-- End : Include FancyTree Plugins : Rajesh -->
<script type="text/javascript" src="js/Scripts/jquery.popupwindow.js"></script>
<script type="text/javascript" src="js/Scripts/popup/jquery.alerts.js"></script>
<script type="text/javascript" src="js/Scripts/popup/iosOverlay.js"></script>

</head>
<body>

<!-- BEGIN HEADER -->
<div class="page-header">
	<!-- BEGIN HEADER TOP -->
	<div class="page-header-top">
	    <!-- BEGIN USER LOGOUT -->
	    <div align="right" class="page-header-top-logoout">
			<a href="j_spring_security_logout">LogOut</a>
		</div>
		<!-- END USER LOGOUT -->
		<div class="container">
			<!-- BEGIN LOGO -->
			<div class="page-logo">
				<a href="index.html"><img src="files/lib/metronic/theme/assets/admin/layout/img/logo-big-white.jpg" style="width:175px; height: 40px; alt="logo" class="logo-default"></a>
			</div>
			<!-- END LOGO -->
			<!-- BEGIN RESPONSIVE MENU TOGGLER -->
			<a href="javascript:;" class="menu-toggler"></a>
			<!-- END RESPONSIVE MENU TOGGLER -->
			<!-- BEGIN TOP NAVIGATION MENU -->
			<div class="top-menu">
				<ul class="nav navbar-nav pull-right">					
					<!-- BEGIN USER LOGIN DROPDOWN -->
					<li class="dropdown dropdown-user dropdown-dark" style="height: 40px;">
						<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
						<img alt="" class="img-circle" src="files/lib/metronic/theme/assets/admin/layout3/img/user.png">						
						<span class="username username-hide-mobile">UserName</span>
						</a>
						<ul class="dropdown-menu dropdown-menu-default">
							<li>
								<a href="extra_profile.html">
								<i class="icon-user"></i> My Profile </a>
							</li>
							<li>
								<a href="page_calendar.html">
								<i class="icon-calendar"></i> My Calendar </a>
							</li>
							<li>
								<a href="inbox.html">
								<i class="icon-envelope-open"></i> My Inbox <span class="badge badge-danger">
								3 </span>
								</a>
							</li>
							<li>
								<a href="javascript:;">
								<i class="icon-rocket"></i> My Tasks <span class="badge badge-success">
								7 </span>
								</a>
							</li>							
						</ul>
					</li>
					<!-- END USER LOGIN DROPDOWN -->
					<li style="clear: both;float: right;"><b><span>${user}</span></b></li> 
					<!-- BEGIN USER LOGOUT -->
<!-- 					<li> -->
<!-- 					<div align="right" style="float:right; margin-right: 20px"><a href="j_spring_security_logout">LogOut</a></div> -->
<!-- 					</li> -->
					<!-- END USER LOGOUT -->
				</ul>
				
<%-- 				<div align="left" style="float:left; margin-left:20px">${user}</div> --%>				
			</div>
			<!-- END TOP NAVIGATION MENU -->
		</div>
	</div>
	<!-- END HEADER TOP -->
	
	<!-- BEGIN HEADER MENU -->
	<div class="page-header-menu">
		<div class="container">			
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

</body>
</html>