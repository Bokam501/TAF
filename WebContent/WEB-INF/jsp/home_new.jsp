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
<link href="files/lib/metronic/theme/assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css">
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL PLUGIN STYLES -->
<link href="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/jqvmap.css" rel="stylesheet" type="text/css">
<!-- END PAGE LEVEL PLUGIN STYLES -->
<!-- BEGIN PAGE STYLES -->
<link href="files/lib/metronic/theme/assets/admin/pages/css/tasks.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">
<!-- END PAGE STYLES -->
<!-- BEGIN THEME STYLES -->
<!-- DOC: To use 'rounded corners' style just load 'components-rounded.css' stylesheet instead of 'components.css' in the below style tag -->
<link href="files/lib/metronic/theme/assets/global/css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/global/css/plugins.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/admin/layout3/css/layout.css" rel="stylesheet" type="text/css">
<link href="files/lib/metronic/theme/assets/admin/layout3/css/themes/blue-steel.css" rel="stylesheet" type="text/css" id="style_color">
<link href="files/lib/metronic/theme/assets/admin/layout3/css/custom.css" rel="stylesheet" type="text/css">
<link href="css/metrojs.min.css" rel="stylesheet" type="text/css">
<link href="css/customStyle.css" rel="stylesheet" type="text/css">
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

<div class="dashboardContainerResponsive">
	<div class="form-group col-lg-12">
	    <div id="loadingIconTM" style="display:none;z-index:100001;position:absolute;top:20px;left:50%">			<img src="css/images/ajax-loader.gif"/>
		</div>
		<div id="overallStatusReport" style="display:none; margin:10px; background-color: #eff3f8; border-style: ridge; padding: 20px;"></div>
    </div>    
</div>

<!-- BEGIN FOOTER -->
	<div><%@include file="footerlayout.jsp" %></div>			
<!-- END FOOTER -->

<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/jquery.vmap.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js" type="text/javascript"></script>
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.sparkline.min.js" type="text/javascript"></script>

<script src="js\flip\metrojs.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- <div class="row">
	<div class="form-group col-lg-12">
		<div id="overallStatusReport" style="margin:10px; background-color: #eff3f8; border-style: ridge; padding: 20px;">
		<label>Previous</label><input style="align:right;"type = radio name="viewbydate" id=-1 value="previous">&nbsp;&nbsp;&nbsp;&nbsp;
		<label>Current</label><input type = radio name="viewbydate" id=0 value="current" checked>&nbsp;&nbsp;&nbsp;&nbsp;
		<label>Next</label><input type = radio name="viewbydate" id=1 value="next">	
		</div>			
    </div>    
</div> -->
<script>
var testingBrick='';
function clickOnbrick(e){
  	// testing 
     /** var targetID = e.currentTarget;
     $(targetID).liveTile();
     $(targetID).liveTile("play",{delay : 1000});
	 	 
	 if($(targetID)[0].id == "brick_18"){
		 $(testingBrick).liveTile("stop");	 
		 $(targetID).liveTile("stop");
	 }
     testingBrick = targetID; */     
};

jQuery(document).ready(function() {
   $("#menuList li:first-child").eq(0).remove();   // to remove fancytree icon near home tab
   var data = $("#userdisplayname")[0].innerText;
   
   role=data.substring(data.indexOf("[")+1,data.length-1);
   /*if(role == "Test Manager" || role == "TestManager" || role == "TestLead" || role == "Test Lead"){  
	  var urlToGetActiveWP = getTotalWPCount('workpackage.for.testmanager');
   }else{
	   $("#overallStatusReport").empty();
	   $("#overallStatusReport").hide();
   }*/
   
   if(role == "TestLead" || role == "Test Lead"){  
		  var urlToGetActiveWP = getTotalWPCount('workpackage.for.testmanager');
	   }else{
		   $("#overallStatusReport").empty();
		   $("#overallStatusReport").hide();
	   }
   
   var recordArr=[];
   function getTotalWPCount(fileLocation) {
	   $.ajax({
	        type: "POST",
	        contentType: "application/json; charset=utf-8",
	        url: fileLocation,
	        dataType: 'json',
	        success: function(data) {        	
	    		return data;	    
	        },
	       complete: function(data) {
	    	   recordArr = data.responseJSON.Records;
	    	   if(recordArr.length>0){
				   $("#overallStatusReport").show();
					populateTilesView(0);
			   }
	       }
	   });	   
   }
   
   function populateTilesView(index){	   
	  $("#loadingIconTM").show();
	   var urlToGetResourcesOfWorkPackage = 'workpackage.testcase.execution.summary?workpackageId='+recordArr[index].id;
	   $.ajax({
	        type: "POST",
	        contentType: "application/json; charset=utf-8",
	        url: urlToGetResourcesOfWorkPackage,
	        dataType: 'json',			
	        success: function(jsonData) {
	            var data = jsonData.Records; 
	            if(jsonData.length!=0){	            
		            for (var key in data) {
		            	var userId = data[key].userId;
		            	var timeSheetHours = data[key].timeSheetHours;		            
		                if(data[key].imageURI=="" || data[key].imageURI=="/Profile/" || data[key].imageURI == null){
		                	data[key].imageURI="css/images/noimage.jpg";
		                }else{
		                	data[key].imageURI="/Profile/"+data[key].imageURI;
		                }
		                var responses = fileExists(data[key].imageURI);
		                if (responses == 404){
		                	data[key].imageURI = "css/images/noimage.jpg";
		                }
		                var brick ='';
		                var executedTC =0;
		                var plannedTC = 0;
		                var startenddiff = 0;
		                var executeperday =0;
		                var executeperhr =0;		                
		                executedTC = data[key].totalExecutedTesCases;
		                startenddiff = data[key].wpStartEnddayDiff;
		             //   console.log("executedTC--"+executedTC+"-startenddiff--"+startenddiff);	
		                if(startenddiff !=0 ){
		                	 executeperday = executedTC/startenddiff;
		                	 executeperday= executeperday.toFixed(0);
		                	 executeperhr = executedTC/(startenddiff*8);
		                	 executeperhr = executeperhr.toFixed(2);
		                }
		                var executedlasthour = 0;
		                if(data[key].executedLastHour != null){
		                	executedlasthour = data[key].executedLastHour;
		                }
		                var tc_notrun = 0;
		                if(data[key].p2totalNoRun != null){
		                	tc_notrun = data[key].p2totalNoRun;
		                }
		                var tc_blocked = 0;		                
		                if(data[key].p2totalBlock != null){
		                	tc_blocked = data[key].p2totalBlock;
		                }
		                var wpstatus = 'NA';
		                if(data[key].wpStatus != null){
		                	wpstatus = data[key].wpStatus;
		                }
		                var pass = data[key].p2totalPass;
		                var passpercent = 0;
		                if(executedTC != 0){
		                	passpercent = (pass/executedTC)*100;	
		                }
		                plannedTC = data[key].totalWPTestCase;
		                var exe_percent = 0;
		                
		                if(plannedTC != 0){
		                	exe_percent = (executedTC/plannedTC)*100;		                	
		                	exe_percent =exe_percent.toFixed(0);		                	
		                }
		                	//(int) Math.floor((Math.random() * 100) + 1)
		                var daydifffromStart = data[key].wpnthDayfromStrart;
		                var daydiffWP = data[key].wpStartEnddayDiff;
		                
		                daydifffromStart = Math.abs(daydifffromStart);
		                var daydiffContent = "";
		                var beforeafter = data[key].executionBeforeAfter;
		                var plannedBeforeAfter = data[key].plannedBeforeAfterCurrentDate;
		                if(beforeafter == 'after'){//Based on 
		                //	console.log(recordArr[index].name+"==daydiff--"+daydifffromStart+"--execution --"+beforeafter); 	
		                	daydiffContent = daydifffromStart +" days";
		                }else if(beforeafter == 'before'){
		                //	console.log(recordArr[index].name+"==daydiff--"+daydifffromStart+"--execution --"+beforeafter);
		                	daydiffContent = "Started before "+daydifffromStart+" days from PlannedStartDate";
		                }else{//No TC Executed
		                	if (plannedBeforeAfter == 'after'){
		                		daydiffContent = 'Not Started';
		                	}else if (plannedBeforeAfter == 'before'){
		                		daydiffContent = daydifffromStart +" days";	
		                	}else{
		                		daydiffContent = daydifffromStart +" days";	
		                	}
		                }
		            	console.log(recordArr[index].name+"==daydiff:"+daydifffromStart+"--execution --"+beforeafter+"-wpstatus-"+wpstatus);
		                		
		                brick  ='<div  id="brick_'+data[key].id+'" onclick="javascript:clickOnbrick(event);">';
					    brick +='<div class="col-md-4 col-md-4 col-sm-6 col-xs-12"><div class="dashboard-stat green-haze more">';					    
					    brick +='<div class="row"><div class="col-md-7" style="padding: 5px;"><a class="more" href="javascript:;">'+recordArr[index].name+'</a></div>';
					    brick +='<div class="col-md-5" style="padding: 5px;"><span style="color:white;margin-right: 5px;">STATUS :&nbsp;</span><span class="badge" style="background:#e35b5a;">'+wpstatus+'</span>';
					    brick +='<!-- <a href="javascripts:;"><i class="m-icon-swapright m-icon-white" style="margin-left: 5px;"></i></a> --></div></div>';					    
					    brick +='<div class="tile double">';
					    brick +='<div class="tile-body" style="padding:1px 1px;">';				    
					    brick +='<div class="row">',
					    brick +='<div class="col-md-4 divAlign"><label>Test Beds</label></div><div class="col-md-3"><span >'+data[key].testBedCount+'</span></div></div>';				    
					    brick +='<div class="row"><div class="col-md-4 divAlign"><label>Execution Status</label></div><div class="col-md-1"><span >'+data[key].totalExecutedTesCases+"/"+data[key].totalWPTestCase+'</span></div><div class="col-md-6"><span>['+exe_percent+'%]&nbsp;&nbsp;&nbsp;&nbsp;['+data[key].p2totalPass + " P/" + data[key].p2totalFail+ " F/" + tc_notrun+" NR/" + tc_blocked+" B]" +'</span></div></div>'
					    brick +='<div class="row"><div class="col-md-4 divAlign"><label>Schedule</label></div><div class="col-md-8"><span>: '+daydiffContent+' / '+data[key].wpStartEnddayDiff+' total days</span></div></div>';					   
					    brick +='<div class="row"><div class="col-md-4 divAlign"><label>Velocity</label></div><div class="col-md-4"><span>: '+executeperday+'/ day</span></div>',
					    brick +='<div class="col-md-4"><span>: '+executedlasthour+'/1 hr</span></div></div>';					    
					    brick +='<div class="row"><div class="col-md-4 divAlign"><label>Bugs Reported</label></div><div class="col-md-8"><span>: '+data[key].defectsCount+'</span></div></div></div>';
					    
					    brick +='</div>';					    						
						brick +='</div></div></div>';		
			
	                   if(!($('#overallStatusReport').hasClass('tiles'))){
	                   		$('#overallStatusReport').addClass('tiles');
	                   		//$('#tileBrick1').append($(brick));
	                   }
						$('#overallStatusReport').append($(brick));
						//$('#tileBrick').append($(brick));
			            }
		            }else{
		            	$("#overallStatusReport").hide();
		            }},
		            complete: function(jsonData) {
					$("#loadingIconTM").hide();
	    	   		if(index<recordArr.length-1){
	    	   			if(index ==0){
	    	   				console.log("Start--"+new Date());
	    	   			}
	    	   			index++;
	    	   			
	    	   			populateTilesView(index);  	   			
	    	   		}
	    	   		console.log("End--"+new Date());
		   		},
				error: function (data){
					$("#loadingIconTM").hide();
				}			   		
       		});
   }   
   function fileExists(fileLocation) {
	    var response = $.ajax({
	        url: fileLocation,
	        type: 'HEAD',
	        async: false
	    }).status;
	    return response;
	}   
   
});

</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>