<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link href="files/lib/metronic/theme/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/jqvmap.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/global/plugins/bootstrap-tabdrop/css/tabdrop.css" rel="stylesheet" type="text/css">
<link href="css/select2.css" rel="stylesheet" type="text/css"></link>

<!-- BEGIN THEME STYLES -->
<link href="files/lib/metronic/theme/assets/global/css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css"/>
<link href="files/lib/metronic/theme/assets/global/css/plugins.css" rel="stylesheet" type="text/css"/>
<link href="files/lib/metronic/theme/assets/admin/layout3/css/layout.css" rel="stylesheet" type="text/css">
<%-- <link href="files/lib/metronic/theme/assets/admin/layout/css/themes/${userTheme}.css" rel="stylesheet" type="text/css" id="style_color"> --%>
<link href="files/lib/metronic/theme/assets/admin/layout/css/themes/darkblue.css" rel="stylesheet" type="text/css" id="style_color">
<link href="files/lib/metronic/theme/assets/admin/layout3/css/custom.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/admin/pages/css/tasks.css" rel="stylesheet" type="text/css"/>
<link href="files/lib/metronic/theme/assets/global/plugins/jstree/dist/themes/default/style.min.css" rel="stylesheet" type="text/css">
<link href="css/customStyle.css" rel="stylesheet" type="text/css"/>
<!-- END THEME STYLES -->

<spring:eval var="toenableLogoChange" expression="@ilcmProps.getProperty('INSTALLATION_MODE')" />

<style>
	body{
		background-color:#fff;
		 min-width:1055px; 
	}
</style>

</head>
<body>
<div id="loadingIcon" style="display:none;z-index:100001;position:absolute;top:40%;left:50%"><img src="css/images/ajax-loader.gif"/></div>
<div class="page-header-top">
	<div class="container container-position">
		<!-- <div class="page-logo page-logo-position" style="margin:0px;margin-top:10px;">
			<a href="javascript:void(0);">
				<img src="css/images/fulllogo.png" style="width:30px; height: 30px;margin:0px" alt="logo" class="logo-default" title="Automate | Accelerate | Analyze">
				Life Cycle | Analytics | Automation">
			</a>
		</div> -->
		
		<div class="page-logo page-logo-position logo headerLogo" style="margin:0px;cursor: default;width: 90px;float:right;" title="">
			<img src="css/images/hclLogo4.png"  style="width: 85px;height:40px;margin-top:6px;" alt="hclLogo"></img>			
		</div>
		<div class="page-logo page-logo-position logo headerLogo" id="ilcmHeaderLogo" style="margin:0px;cursor: default;width:30%; title="">
			<p class="logoTitle" style="margin:0px 0px -7px 0px;color:#55616f !important"></p>
			<p class="logoSubTitle" style="margin:0px;"></p>
		</div>
		<a href="javascript:;" class="menu-toggler"></a>
		<div class="top-menu" style="margin:0px;">
			<ul class="nav navbar-nav pull-right">
				<li class="dropdown dropdown-user dropdown-dark" >
					<a style="text-align:center;padding-bottom:0px;" href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">						
						<img id="header-image-preview" alt="" class="img-circle" style="float:none;height:20px; width:20px">
					</a>					
					<ul class="dropdown-menu dropdown-menu-default">
						<li><a href="profile.management.plan"><i class="icon-user"></i> My Profile </a></li>
						<!-- <li><a href="javascript:;"><i class="icon-calendar"></i> My Calendar </a></li> -->
						<li class="divider"></li>
						<li><a href="j_spring_security_logout"><i class="icon-key"></i> Log Out </a></li>
					</ul>
					<div id='userdisplayname' style="clear: both;">${userDisplayName}
					</div>
				</li>
				<!-- <li style="cursor: pointer;">
				  	<i class="fa fa-users" style="font-size:23px;margin-top:15px;background-color: white;color:#55616f" title="Change MyRole" onclick="changeUserRole()"></i>
				</li> -->
				<li><a href="j_spring_security_logout" style="padding-top:12px;padding-bottom:12px"><img class="logoutLogo" src="css/images/logout.png" alt="LogOut"></img></a></li>
			</ul>
		</div>
	</div>
</div>

<div id="div_PopupMainrolechange" class="modal " tabindex="-1" aria-hidden="true">
	<div class="modal-dialog" style="width: 300px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupCloserolechange()" aria-hidden="true"></button>
				<h4 class="modal-title">Title</h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:320px" data-always-visible="1" data-rail-visible1="1">
				    <div class="col-md-12">
						<div class="portlet light">								
							<div class="portlet-body form">
							  <div><%@include file="changeRole.jsp" %></div>  
							</div>
						</div>
						</div>
				    </div>				
				</div>
		</div>
	</div>
</div>

<script src="js/datatable/dataTableCommon.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
 <script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>
 
<script type="text/javascript">
function loadRolesHdr(userid,roleId){
	$('#role_types1').empty();
		$.post('common.listuser.roles?userid='+userid,function(data) {	
			var ary = (data.Options);
	        $.each(ary, function(index, element) {
	        	if(roleId== element.Value){
	    			$('#role_types1').append('<label><input type="radio" name="radio1" checked class="icheck" id="' + element.Value + '" data-radio="iradio_flat-grey">'+element.DisplayText+'</label>');
	        	}else{
	        		$('#role_types1').append('<label><input type="radio" name="radio1" class="icheck" id="' + element.Value + '" data-radio="iradio_flat-grey">'+element.DisplayText+'</label>');
	        	}
			});
	        
	});
	
		$.post('administration.user.listUserRole',function(data) {	
			var ary = (data.Options);
	        $.each(ary, function(index, element) {
	        	if(roleId== element.Number){
	        		$('#role_types1').append('<label><input type="radio" name="radio1" checked class="icheck" id="' + element.Value + '" data-radio="iradio_flat-grey">'+element.DisplayText+'</label>');
	        	}
	        	if(roleId==null){
	        		$("#role_types1").append("<div style='font-size:small;' >No Roles</div>");
	        	}
	        });
	        
	});
}
var userid ='';
var globalanytimehandler = {
		isChanged :false		
};
var datepickerCurrentValue ;
if (!Object.prototype.watch) {
    Object.defineProperty(Object.prototype, "watch", {
          enumerable: false
        , configurable: true
        , writable: false
        , value: function (prop, handler) {
            var
              oldval = this[prop]
            , newval = oldval
            , getter = function () {
                return newval;
            }
            , setter = function (val) {
                oldval = newval;
                return newval = handler.call(this, prop, oldval, val);
            }
            ;

            if (delete this[prop]) { // can't watch constants
                Object.defineProperty(this, prop, {
                      get: getter
                    , set: setter
                    , enumerable: true
                    , configurable: true
                });
            }
        }
    });
}

// object.unwatch
if (!Object.prototype.unwatch) {
    Object.defineProperty(Object.prototype, "unwatch", {
          enumerable: false
        , configurable: true
        , writable: false
        , value: function (prop) {
            var val = this[prop];
            delete this[prop]; // remove accessors
            this[prop] = val;
        }
    });
}

$(document).ready(function(){
	userid ='<%=session.getAttribute("ssnHdnUserId")%>';
	roleId='<%=session.getAttribute("roleId")%>';
	if(userid!='' && userid!=null){
	 	profileImages();	 
	}
	
	// ----- mode change iLCM / TAF -----
	changeMode();
});

var modeSelection ="${toenableLogoChange}";
var title_ilcm = 'iLCM';
var title_bot = 'Project Optimus';
var title_taf = 'TAF';
var title_ers_practice_deployment="ERS Practice Deployments";
var title_iTAX = 'iTAX';
var title_ATLAS = 'ATLAS';
var subTitle_ilcm = 'Automate | Accelerate | Analyse';
var subTitle_taf = 'Test Automation Framework';
var subTitle_bot = 'Bot Management System';
var subTitle_ers_practice_deployment="Integrated Life Cycle Management";
var subTitle_iTAX = 'for Intelligent Test Automation & Execution';
var subTitle_ATLAS = 'for Augmented Test Lifecycle Autonomics Solutions';
var tooltip_ilcm = 'Integrated Life Cycle Management';
var tooltip_ers_practice_deployment="ERS Practice deployments";
var tooltip_taf = subTitle_taf;
var tooltip_bot = subTitle_bot;
var tooltip_iTAX = 'for Intelligent Test Automation & Execution';
var tooltip_ATLAS = 'for Augmented Test Lifecycle Autonomics Solutions';
var about_ilcm = 'About iLCM';
var about_taf = 'About TAF';
var about_bot = 'About BOT';
var about_ers_practice_deployment="About ERS Practice deployments";
var about_iTAX = 'About iTAX';
var about_ATLAS = 'About ATLAS';

	function modeTitleHandler(){
		var modeTitle='';
		if(modeSelection.toLowerCase() == title_ilcm.toLowerCase()){
			modeTitle = title_ilcm;			
		}else if(modeSelection.toLowerCase() == title_taf.toLowerCase()){
			modeTitle = title_taf;			
		}else if(modeSelection.toLowerCase() == title_bot.toLowerCase()){
			modeTitle = title_bot;			
		}else if(modeSelection.toLowerCase() == title_ers_practice_deployment.toLowerCase()){
			modeTitle = title_ers_practice_deployment;			
		}else if(modeSelection.toLowerCase() == title_iTAX.toLowerCase()){
			modeTitle = title_iTAX;			
		}else if(modeSelection.toLowerCase() == title_ATLAS.toLowerCase()){
			modeTitle = title_ATLAS;			
		}else{
			modeTitle = title_ilcm;
		}		
		return modeTitle;
	}
	
	function modeAboutUsHandler(){
		var aboutUs='';
		if(modeSelection.toLowerCase() == title_ilcm.toLowerCase()){
			aboutUs = about_ilcm;
		}else if(modeSelection.toLowerCase() == title_taf.toLowerCase()){
			aboutUs = about_taf;
		}else if(modeSelection.toLowerCase() == title_bot.toLowerCase()){
			aboutUs = about_bot;
		}else if(modeSelection.toLowerCase() == title_ers_practice_deployment.toLowerCase()){
			aboutUs = about_ers_practice_deployment;
		}else if(modeSelection.toLowerCase() == title_iTAX.toLowerCase()){
			aboutUs = about_iTAX;
		}else if(modeSelection.toLowerCase() == title_ATLAS.toLowerCase()){
			aboutUs = about_ATLAS;
		}else{
			aboutUs = about_ilcm;
		}		
		return aboutUs;
	}
	
	function changeMode(){
		if(modeSelection.toLowerCase() == title_ilcm.toLowerCase()){
			$('.page-header-top > .container >.logo >.logoTitle').text(title_ilcm);
			$('.page-header-top > .container >.logo >.logoSubTitle').text(subTitle_ilcm);			
			$('.page-header-top > .container >.headerLogo').eq(1).attr('title',tooltip_ilcm);
			document.title = title_ilcm;
			
		}else if(modeSelection.toLowerCase() == title_taf.toLowerCase()){			
			// ---- loadign TAF logo image file in the header part -----
			/* var temp = '<img src="css/images/taf_logo.png" style="height:40px;margin-top:6px;" alt="hclLogo" title="Test Automation Framework">;'
			$('.page-header-top > .container >.headerLogo').eq(1).empty();
			$('.page-header-top > .container >.headerLogo').eq(1).append(temp); */
			
			// ---- loadign TAF logo text font in the header part -----
			$('.page-header-top > .container >.logo >.logoTitle').text(title_taf);
			$('.page-header-top > .container >.logo >.logoSubTitle').text(subTitle_taf);
			$('.page-header-top > .container >.headerLogo').eq(1).attr('title',subTitle_taf);
			document.title = title_taf;
			
		}else if(modeSelection.toLowerCase() == title_bot.toLowerCase()){			
			
			$('.page-header-top > .container >.logo >.logoTitle').text(title_bot);
			$('.page-header-top > .container >.logo >.logoSubTitle').text(subTitle_bot);
			$('.page-header-top > .container >.headerLogo').eq(1).attr('title',subTitle_bot);
			document.title = title_bot;
			
		}else if(modeSelection.toLowerCase() == title_ers_practice_deployment.toLowerCase()){			
			
			$('.page-header-top > .container >.logo >.logoTitle').text(title_ers_practice_deployment);
			$('.page-header-top > .container >.logo >.logoSubTitle').text(subTitle_ers_practice_deployment);
			$('.page-header-top > .container >.headerLogo').eq(1).attr('title',subTitle_ers_practice_deployment);
			document.title = title_ers_practice_deployment;
			
		}else if(modeSelection.toLowerCase() == title_iTAX.toLowerCase()){			
			
			$('.page-header-top > .container >.logo >.logoTitle').text(title_iTAX);
			$('.page-header-top > .container >.logo >.logoSubTitle').text(subTitle_iTAX);
			$('.page-header-top > .container >.headerLogo').eq(1).attr('title',subTitle_iTAX);
			document.title = title_iTAX;

		}else if(modeSelection.toLowerCase() == title_ATLAS.toLowerCase()){			
			
			$('.page-header-top > .container >.logo >.logoTitle').text(title_ATLAS);
			$('.page-header-top > .container >.logo >.logoSubTitle').text(subTitle_ATLAS);
			$('.page-header-top > .container >.headerLogo').eq(1).attr('title',subTitle_ATLAS);
			document.title = title_ATLAS;

		}else{
			$('.page-header-top > .container >.logo >.logoTitle').text(title_ilcm);
			$('.page-header-top > .container >.logo >.logoSubTitle').text(subTitle_ilcm);			
			$('.page-header-top > .container >.headerLogo').eq(1).attr('title',tooltip_ilcm);
			document.title = title_ilcm;
		}
	}
	  
   function changeUserProfile() {
		userid ='<%=session.getAttribute("ssnHdnUserId")%>';
	   var id = $("input[name=radio1]:checked").attr("id");
		var date = new Date();
	    var timestamp = date.getTime();
	 /*    alert("window.location.pathname==>"+window.location.pathname);
	    var contextpath = (window.location.pathname)
		.split("/", 2); */
	var root = window.location.protocol
		+ "//"
		+ window.location.host+window.location.pathname+"?filter=1";
	/* alert("contextpath==>"+contextpath);
	alert("root==>"+root); */
	    if(confirm("Are you sure you want to change the Role?")){
		    $.ajax({
			    type: "POST",
			    url: "home.changerole?roleId="+id+"&userId="+userid,//+id,
			    success: function(data) {
			    	role=data.substring(data.indexOf("[")+1,data.length-1);
			    	menuLoader(role);
			    	document.getElementById('userdisplayname').innerHTML=data;
			    },
			    complete:function(data){
			    	//window.location.href = "http://localhost:8080/tfwp/home?filter=1";
			    	window.location.href = root;
			    }
			});
	    }
   }
   
  
   
 function profileImages(){
	 $.ajax({
	    url: 'profile.based.user.list?userId='+userid,
	    method: 'POST',
	    contentType: "application/json; charset=utf-8",
	    dataType : 'json',
		success : function(data) {
			var result=data.Records;
			if(result == null){
				//callAlert(data.Message);
			}else{
				var root = "/Profile/";
				$.each(result, function(i,item){
					var imagePath=root+item.imageURI;
					 $.ajax({
						  url: root+item.imageURI, //or your url
						  success: function(data){
							$("#header-image-preview").attr('src',imagePath);
						  },
						  error: function(data){
							$("#header-image-preview").attr('src','css/images/noimage.jpg');
						  }
						});
					});
				}
			}
		});
	}
 
 function getUserRole(){	
	var data=$("#userdisplayname").text();
	var role=data.substring(data.indexOf("[")+1,data.indexOf("]"));
	//console.log("visible :"+role);
	return role;
 }
 
   /* function changeUserRole(){
	   //alert("change user role");	   
		if(userid!='' && userid!=null){
		 	loadRolesHdr(userid,roleId);	 
		}
	   loadPopup("div_PopupMainrolechange");
   } */
   
   /* Load Poup function */
   function loadPopup(divId) {	
   	$("#" + divId).fadeIn(0500); 					 // fadein popup div
   	$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
   	$("#div_PopupBackground").fadeIn(0001);
   }
   
   function popupCloserolechange() {	
		$("#div_PopupMainrolechange").fadeOut("normal");
		$("#div_PopupBackground").fadeOut("normal");
	}
   
   function validateSpecialCharactersExceptDot(field, rules, i, options){
		var pattern = new RegExp(options.allrules.Letters_loworup_noSpec_dotAdded.regex);
		var pattern2 = new RegExp(options.allrules.Letters_loworup_noSpec_dotAdded_numbersOnly.regex)
		if ( !pattern.test(field.val()) && !pattern2.test(field.val()) ){
		     return options.allrules.Letters_loworup_noSpec_dotAdded.alertText;
	  	}
	}
</script>

<script language='javascript'>window._wfx_settings={"ent_id":"309589c0-0229-11e6-9560-448a5b5dd1ba"};</script>

<!-- UNCOMMENT AND INCLUDE BELOW SCRIPT IN ALL PAGES OF YOUR SITE (HEADER/FOOTER). YOU CAN MODIFY ITS PATH ACCORDING TO EXPORTED LOCATION -->

<!--  <script language='javascript' async='true' type='text/javascript' src='//whatfix.com/embed/embed.nocache.js'> -->
</script>

</body>
</html>