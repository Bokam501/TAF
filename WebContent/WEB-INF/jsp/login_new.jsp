<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<!-- 
Template Name: Metronic - Responsive Admin Dashboard Template build with Twitter Bootstrap 3.3.4
Version: 3.3.0
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
<html lang="en">
<!--<![endif]-->

<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8"/>
<link rel="shortcut icon" href="css/images/logo_new.png"><title></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta content="" name="description"/>
<meta content="" name="author"/>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!-- select2 plugin (depends on jquery-ui sortable)-->
<link href="js/nreco/pivotrest/Scripts/select2/select2.css" rel="stylesheet" />
<link href="js/nreco/pivotrest/Scripts/select2/select2-bootstrap.css" rel="stylesheet" />
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="files/lib/metronic/theme/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<link href="files/lib/metronic/theme/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
<link href="files/lib/metronic/theme/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="files/lib/metronic/theme/assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN THEME STYLES -->
<link href="files/lib/metronic/theme/assets/admin/pages/css/login.css" rel="stylesheet" type="text/css"/>
<link href="files/lib/metronic/theme/assets/global/css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css"/>
<link href="css/customStyle.css" rel="stylesheet" type="text/css"/>
<!-- END THEME STYLES -->

<style type="text/css">
  .select2-drop-active {
	z-index:100060 !important;
  }
  </style>
</head>
<!-- END HEAD -->

<!-- BEGIN BODY -->
<body class="login" onload="setErrorMessage('${message}')">
<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
<div class="menu-toggler sidebar-toggler">
</div>
<!-- END SIDEBAR TOGGLER BUTTON -->
<!-- BEGIN LOGO -->
<!-- <div class="logo" style="width:120px;background-color:#eceef1;border-radius:5px;padding:5px">
	<a href="#">
		<img src="css/images/fulllogo.png" alt="" style="width:88px;height:54px"/>	
	</a>
</div> -->

<div class="logo headerLogo logoTitle" style="background-color:#eceef1;padding:0px;">
	<p></p>
</div>
<!-- END LOGO -->

<!-- BEGIN LOGIN -->
<div class="content">
	<!-- BEGIN LOGIN FORM -->
	<form class="login-form" action="<c:url value='j_spring_security_check'/>" method="post" name="frmLogin">
		<div class="form-title">
			<span class="form-title">Welcome.</span>
			<span class="form-subtitle">Please login.</span>
		</div>
		<div class="alert alert-danger display-hide">
			<button class="close" data-close="alert"></button>
			<span id="alertBtn">
			Enter your Username and Password</span>
		</div>
		<div class="form-group">  
			<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
			<label class="control-label visible-ie8 visible-ie9">Username</label>
			<input class="form-control form-control-solid placeholder-no-fix" type="text"  onfocus="j_usernameOnFocus()"		
				autocomplete="off" placeholder="Username" name="j_username" id="j_username"/>
		</div>
		<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9">Password</label>
			<input class="form-control form-control-solid placeholder-no-fix" type="password" autocomplete="off" placeholder="Password" name="j_password" id="j_password"/>
		</div>
		<div class="form-actions">
		<button type="submit" id ="ilcmLoginButton" class="btn btn-primary btn-block uppercase" onclick="validateAndSubmit()">Login</button>
		</div>
	
		<!-- <a class="pull-right" href="#" data-modal-id="popup" onclick="displayRequestAccess()"> Request Access </a> -->	

	
	<!-- User Onboard Request Access Start -->
	
	<div id="div_PopupUserRequestAccess" class="modal " tabindex="-1" aria-hidden="true">
	<div class="modal-dialog" style="width: 400px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title caption-subject theme-font">User Request Access</h4>
			</div>
			<div class="modal-body">
				
						<div class="portlet light">								
							<div class="portlet-body form">
							  <div class="skin skin-flat">
									<form role="form">
										<div class="form-body" style="padding-top: 0px;padding-bottom: 0px;">									
											<div class="form-group">
												<label class="control-label visible-ie8 visible-ie9">Password</label>
												<input class="form-control form-control-solid placeholder-no-fix valid" autocomplete="off" placeholder="Email ID" id="userEmailId" aria-required="true" aria-invalid="false" useremailid="">
											</div>										
											<div class="form-group">
												<label class="control-label visible-ie8 visible-ie9">TestFactory</label>
												<!-- <input class="form-control form-control-solid placeholder-no-fix valid" autocomplete="off" placeholder="Product Name" id="productName" aria-required="true" aria-invalid="false" productName=""> -->
												<select class="form-control input-large select2me" id="testFactoryList_URA">
						     					    <option value="0" selected="selected">ALL</option>
						 					   </select>
											</div>
											
											
											<div class="form-group">
												<label class="control-label visible-ie8 visible-ie9">Product Name</label>
												<!-- <input class="form-control form-control-solid placeholder-no-fix valid" autocomplete="off" placeholder="Product Name" id="productName" aria-required="true" aria-invalid="false" productName=""> -->
												<select class="form-control input-large select2me" id="productList_URA">
						     					    <option value="0" selected="selected">ALL</option>
						 					   </select>
											</div>
																						
											<div class="form-group">
												<label class="control-label visible-ie8 visible-ie9">Workpackage Name</label>
												<!-- <input class="form-control form-control-solid placeholder-no-fix valid" autocomplete="off" placeholder="Workpackage Name" id="workpackageName" aria-required="true" aria-invalid="false" workpackageName=""> -->
												<select class="form-control input-large select2me" id="workpackage_URA">
						        					 <option value="ALL" selected="selected">ALL</option>
						    					</select>
												
											</div>
										</div>
										<div class="form-actions" style="padding-top: 5px;padding-bottom: 5px;padding-left: 30px;">
										<button type="button" onclick="submitUserRequstAccessHandler();" class="btn green-haze" style="padding: 6px 6px;" >Submit</button>
											<button type="button" class="btn grey-cascade" onclick="popupCloseUserRequestAccessHandler()">Cancel</button>
										</div>
									</form>
								</div>  
							</div>
						</div>
						<!-- </div> -->
				  <!--   </div> -->				
				</div>
		</div>
	</div>
</div>

<!-- User Onboard Request Access End -->


<div class="modal fade" id="basicAlert" tabindex="-1" role="basic" aria-hidden="true" style="z-index:100080;">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
				<h4 class="modal-title">Alert</h4>
			</div>
			<div class="modal-body" >
				 <span id="alertModal"></span>
				 <span id="alertModalHelp"></span>
				 <a href="" id="processFlow" style="display:none" target="_blank">process workflow.</a>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn green-haze" data-dismiss="modal">Ok</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>



</form>
	
<%-- 		<form class="cust-login-form" action="${pageContext.request.contextPath}/customerlogin" method="post" name="custfrmLogin">
		<div class="form-title">
			<span class="form-title">Welcome.</span>
			<span class="form-subtitle">Please login.</span>
		</div>
		<div class="alert alert-danger display-hide">
			<button class="close" data-close="alert"></button>
			<span id="alertBtn">
			Enter your Customer Username and Password</span>
		</div>
		<div class="form-group">  
			<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
			<label class="control-label visible-ie8 visible-ie9">Username</label>
			<input class="form-control form-control-solid placeholder-no-fix" type="text"  onfocus="j_custUsernameOnFocus()"		
				autocomplete="off" placeholder="Customer/Vendor Username" name="j_custUsername" id="j_custUsername"/>
		</div>
		<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9">Password</label>
			<input class="form-control form-control-solid placeholder-no-fix" type="password" autocomplete="off" placeholder="Password" name="j_custUserpassword" id="j_custUserpassword"/>
		</div>
	<!-- 	<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9">Domain</label>
			<input class="form-control form-control-solid placeholder-no-fix" type="text" autocomplete="off" placeholder="Domain" name="j_domain" id="j_domain"/>
		</div> -->
		<div class="form-actions">
			<button type="submit" class="btn btn-primary btn-block uppercase" onclick="validateAndSubmit(2)">Login</button>
		</div>
		<div class="login-options">			
		</div>
	</form>  --%>
	
	<!-- END LOGIN FORM -->
	<!-- BEGIN FORGOT PASSWORD FORM -->
	<!-- <form class="forget-form" action="index.html" method="post">
		<div class="form-title">
			<span class="form-title">Forget Password ?</span>
			<span class="form-subtitle">Enter your e-mail to reset it.</span>
		</div>
		<div class="form-group">
			<input class="form-control placeholder-no-fix" type="text" autocomplete="off" placeholder="Email" name="email"/>
		</div>
		<div class="form-actions">
			<button type="button" id="back-btn" class="btn btn-default">Back</button>
			<button type="submit" class="btn btn-primary uppercase pull-right">Submit</button>
		</div>
	</form> -->
	<!-- END FORGOT PASSWORD FORM -->	
</div>

<!-- <div class="copyright hide">
	 2014 © HCL Technologies Ltd.
</div> -->
<!-- END LOGIN -->

<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.cokie.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="files/lib/metronic/theme/assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->

<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="files/lib/metronic/theme/assets/global/scripts/metronic.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/admin/layout/scripts/layout.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/admin/layout/scripts/demo.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/admin/pages/scripts/login.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->

<script type="text/javascript" src="js/nreco/pivotrest/Scripts/jquery-ui-1.9.2.custom.min.js"></script>
<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>

<spring:eval var="toenableLogoChange" expression="@ilcmProps.getProperty('INSTALLATION_MODE')" />


<script>
var modeSelection ='<c:out value="${toenableLogoChange}"/>';
var title_ilcm = 'iLCM';
var title_taf = 'TAF';
var title_ers_practice_deployment = "ERS Practice Deployments";
var title_bot = 'Project Optimus';
var title_iTAX = 'iTAX';
var title_ATLAS = 'ATLAS';
var engagementId = "";
jQuery(document).ready(function() {     
	Metronic.init(); // init metronic core components
	Layout.init(); // init current layout
	Login.init();
	Demo.init();
	
	$('#j_username').focus();
		
	changeMode();
});

	$('.login').click(function(e){
	   e.stopPropagation();
	}); 

function changeMode(){	
	if(modeSelection.toLowerCase() == title_ilcm.toLowerCase()){
		$('.logoTitle p').text(title_ilcm);
		document.title = title_ilcm;
	}else if(modeSelection.toLowerCase() == title_taf.toLowerCase()){
		$('.logoTitle p').text(title_taf);
		document.title = title_taf;
	}else if(modeSelection.toLowerCase() == title_bot.toLowerCase()){
		$('.logoTitle p').text(title_bot);
		document.title = title_bot;
	}else if(modeSelection.toLowerCase() == title_ers_practice_deployment.toLowerCase()){
		$('.logoTitle p').text(title_ers_practice_deployment);
		document.title = title_ers_practice_deployment;
	}else if(modeSelection.toLowerCase() == title_iTAX.toLowerCase()){
		$('.logoTitle p').text(title_iTAX);
		document.title = title_iTAX;
	}else if(modeSelection.toLowerCase() == title_ATLAS.toLowerCase()){
		$('.logoTitle p').text(title_ATLAS);
		document.title = title_ATLAS;
	}else{
		$('.logoTitle p').text(title_ilcm);
		document.title = title_ilcm;
	}
}


function displayRequestAccess()
{
	$("#div_PopupUserRequestAccess").modal();
	
	$("#div_PopupUserRequestAccess .modal-header h20").text('Onboard User Request');
	$('#userEmailId').val('');
	$('#workpackage_URA').val('');
	$('#productList_URA').val('');	
	
    displayTestFactoryRequestAccess();
}

function displayTestFactoryRequestAccess(){	
	$('#testFactoryList_URA').empty();				
		$.post('common.testFactory.list.byLabId?testFactoryLabId=1&engagementTypeId=0',function(data) {	
	        var ary = (data.Options);
	        var i=0;
	        $.each(ary, function(index, element) {
	        	if(i==0){
	        		$('#testFactoryList_URA').append('<option id="-1" ><a href="#">Choose</a></option>');
	    			$('#testFactoryList_URA').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
	        	}else{
	        		$('#testFactoryList_URA').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
	        	}
	        	i++;
	  		 });
	   		$('#testFactoryList_URA').select2();	
	   		 
		});	
}
$('#testFactoryList_URA').change(function(){
	loadProductsBasedOnTestFactoryIdRequestAccess();
		
});

function loadProductsBasedOnTestFactoryIdRequestAccess(){	
   	   
    engagementId = $("#testFactoryList_URA").select2().find('option:selected').attr('id');
    console.log("engId>>"+engagementId)
		selectedEngagementId= parseInt(engagementId);
    //selectedEngagementId = isNaN(parseInt(engagementId)) ? -1 : parseInt(engagementId);
		console.log("selectedEngagementId new>>>"+ selectedEngagementId);
		$('#productList_URA').empty();	
		if(selectedEngagementId!=-1){
		
  		$.post('common.list.product.byTestFactoryId?testFactoryId='+selectedEngagementId+'',function(data) {	
  	        var ary = (data.Options);
  	        var j=0;
  	        $.each(ary, function(index, element) {
  	        	if(j==0){
  	        		$('#productList_URA').append('<option id="-1" ><a href="#">ALL</a></option>');
  	        		$('#productList_URA').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
  	        	}else{
  	    			$('#productList_URA').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
  	        	}
  	        	j++;
  	        });
  	   		$('#productList_URA').select2();	
  	   		      
  		});
		}else{
			callAlert("Please choose engagement... ");
		}
		
	}
$('#productList_URA').change(function(){
	loadWorkPackageBasedOnProductIdRequestAccess();
		
});
	
function loadWorkPackageBasedOnProductIdRequestAccess(){
	
	 productId = $("#productList_URA").select2().find('option:selected').attr('id');
	    console.log("engId>>"+productId)
			selectedProductId= parseInt(productId);
	    //selectedEngagementId = isNaN(parseInt(engagementId)) ? -1 : parseInt(engagementId);
			console.log("selectedEngagementId new>>>"+ selectedEngagementId);
			$('#workpackage_URA').empty();	
			if(selectedProductId!=-1){
			
	  		$.post('workpackage.type.option.byProductId?productId='+selectedProductId+'',function(data) {	
	  	        var ary = (data.Options);
	  	        var j=0;
	  	        $.each(ary, function(index, element) {
	  	        	if(j==0){
	  	        		$('#workpackage_URA').append('<option id="-1" ><a href="#">ALL</a></option>');
	  	        		$('#workpackage_URA').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
	  	        	}else{
	  	    			$('#workpackage_URA').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
	  	        	}
	  	        	j++;
	  	        });
	  	   		$('#workpackage_URA').select2();	
	  	   		      
	  		});
			}else{
				callAlert("Please choose engagement... ");
			}
}	
	
function popupCloseUserRequestAccessHandler(){
    $("#div_PopupUserRequestAccess").modal('hide');	

}


function submitUserRequstAccessHandler(){
	var emailId=$("#userEmailId").val();
	var workpackageName=$('#workpackage_URA').val();
	var productName=$('#productList_URA').val();
	
	$.get('rest/restAPIService/createOnboardUser?emailId='+emailId+'&workpackageName='+workpackageName+'&productName='+productName,function(data) {	
		console.log("success "+data);
		callAlert(data);
	});

    $("#div_PopupUserRequestAccess").modal('hide');	

}


function callAlert(alrtMsg){
	$("#processFlow").hide();
	//$("#alertModal").text(alrtMsg);
	$("#alertModal").html(alrtMsg);
	$("#alertModalHelp").text("");
	$("#basicAlert").modal();
}



</script>

<script type="text/javascript" src="js/jquery-func.js" type="text/javascript"></script>
<script type="text/javascript" src="js/Scripts/SessionTimeout/jquery.sessionTimeout.js"></script>

<script type="text/javascript">
var loginErrorMsgDisplayVar = "Enter your Username and Password";
//var invalidCredentialsMsgVar = "The Username or Password entered is not valid. Please try again.";

	function setErrorMessage(msg){
		if (msg == null || msg == '') {
			$('.alert-danger', $('.login-form')).innerHTML = '<br>';
			$('.alert-danger', $('.login-form')).hide();
		} else {
			document.getElementById("alertBtn").innerText = msg;	
			$('.alert-danger', $('.login-form')).show();
		}		
	}
	
	function validateAndSubmit() {	
		var name = document.forms["frmLogin"]["j_username"].value;
	 	var pwd = document.forms["frmLogin"]["j_password"].value;
 		if (name == null || name == "" || pwd == null || pwd == "") {
 			setErrorMessage(loginErrorMsgDisplayVar);
 		} else {
 			document.forms["frmLogin"].submit(); 			
 		}
 		return true;
	}
	
	function j_usernameOnFocus(){
		setErrorMessage(null);
	}
	
	
</script>

<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>