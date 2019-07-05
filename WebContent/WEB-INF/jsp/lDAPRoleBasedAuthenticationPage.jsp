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

<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="files/lib/metronic/theme/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">
<link href="files/lib/metronic/theme/assets/global/css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/global/css/plugins.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/admin/layout3/css/layout.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/admin/layout3/css/themes/blue-steel.css" rel="stylesheet" type="text/css" id="style_color">
<link href="files/lib/metronic/theme/assets/admin/layout3/css/custom.css" rel="stylesheet" type="text/css">
<link href="css/customStyle.css" rel="stylesheet" type="text/css">

<script src="files/lib/metronic/theme/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>

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
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->
<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp" %></div>			
<!-- END FOOTER -->


<div id="div_PopupRoleSelect" class="modal fade" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog" style="width:300px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupCloserolechange();window.location.href='j_spring_security_logout'" aria-hidden="true"></button>
				<h4 class="modal-title">Select Your Role</h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:300px" data-always-visible="1" data-rail-visible1="1">
				    <div class="col-md-12">
						<div class="portlet light">								
							<div class="portlet-body form">			  					  
								<div id="userRoleTypes"></div>						  
							</div>
							</div>
							<div style="float:right;">								
								<button type="button" class="btn green-haze"  onclick="submitRoleChangeHandler()">Submit</button>
								<button type="button" class="btn grey-cascade" onclick="popupCloserolechange();"><a href="j_spring_security_logout" style="color: white;">Cancel</a></button>
							</div>						  
						</div>
						</div>
				    </div>				
				</div>
		</div>
	</div>
</div>

<script>

jQuery(document).ready(function() {
   $("#menuList li:first-child").eq(0).remove();   // to remove fancytree icon near home tab
   $("#userdisplayname").text("User Role");
   
  var urlToGetActiveWP = getUserRoles("administration.user.listUserRole");
   var recordArr=[];
   function getUserRoles(fileLocation) {
	   $.ajax({
	        type: "POST",
	        contentType: "application/json; charset=utf-8",
	        url: fileLocation,
	        dataType: 'json',
	        success: function(data) {        	
	    		return data;	    
	        },
	       complete: function(data) {
	    	   recordArr = data.responseJSON.Options;
	    	   if(recordArr.length>0){
	    		   populateUserRoles(recordArr);
				   $("#div_PopupRoleSelect").modal();	    		
			   }
	       }
	   });	   
   }
         
   function populateUserRoles(data){
	   $('#userRoleTypes').empty();
	   	   
	   var temp = '<form action>';
	   temp += '<label for="'+data[0].DisplayText+'"> <input type="radio" checked="checked" name="title" value="'+data[0].DisplayText+'" id="'+0+'" /> '+data[0].DisplayText+'</label><br/>';
       for(var i=1; i<data.length; i++){
    	   temp += '<label for="'+data[i].DisplayText+'"><input type="radio" name="title" value="'+data[i].DisplayText+'" id="'+i+'" /> '+data[i].DisplayText+'</label><br/>';
       };
       temp +='</form>';
       $('#userRoleTypes').append(temp);
   }  
    
   function popupCloserolechange(){
		$("#div_PopupRoleSelect").fadeOut("normal");
   }  
      
});

function submitRoleChangeHandler(){
	   console.log("save :"+$('input[name=title]:checked', '#userRoleTypes ').val()," - ",$('input[name=title]:checked', '#userRoleTypes ').attr('id'));
	  }

</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>