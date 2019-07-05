<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<link type="text/css" href="css/jquery.jscrollpane.css" rel="stylesheet" media="all" />
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css"	type="text/css" media="all">

<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />

<!-- Validation engine style file -->
<link href="js/Scripts/validationEngine/validationEngine.jquery.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="css/jquery.alerts.css" type="text/css" media="all">
<link rel="stylesheet" href="css/iosOverlay.css" type="text/css" media="all">
<script src="js/jquery-func.js" type="text/javascript"></script>

<!-- the mousewheel plugin -->
<script type="text/javascript" src="js/jquery.mousewheel.js"></script>

<!-- the jScrollPane script -->
<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>
<script src="js/Scripts/popup/jquery.session.js" type="text/javascript"></script>

<!-- Validation engine script file and english localization -->
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>
<script type="text/javascript" src="js/Scripts/SessionTimeout/jquery.sessionTimeout.js"></script>

 <!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all" rel="stylesheet" type="text/css">
<link href="css/bootstrap-select.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/customStyle.css" rel="stylesheet" type="text/css"></link>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:eval var="dashboardDateFilterVisible" expression="@ilcmProps.getProperty('DASHBOARD_DATE_FILTER_VISIBLE')" />
</head>
<body>
	<!-- pop-up report Div -->
	<div id="reportbox" style="display: none;"></div>
	<input type="hidden" value="" id="hdnAutoSearchResources" />
  <!-- Header -->
	<!-- BEGIN HEADER -->
	<div class="page-header">
		<!-- BEGIN HEADER TOP -->
			<div><%@include file="headerlayout.jsp" %></div>			
		<!-- END HEADER TOP -->
		<!-- BEGIN HEADER MENU -->
		<div class="page-header-menu">
			<div class="container container-position">			
				 <div><%@include file="menu.jsp" %></div>			
	 			<!-- END MEGA MENU -->
			</div>
		</div>
	</div>
	<!-- END HEADER -->
 
<!-- Content -->
<!-- BEGIN CONTAINER -->
<div class="page-container">
<!-- BEGIN PAGE CONTENT -->
	<div><%@include file="postHeader.jsp" %></div>

	<div id="content">
		<div class="container-fluid">			
			<!-- BEGIN PAGE CONTENT INNER -->
			<div class="row margin-top-10" id="toAnimate">
				<div class="col-md-12">
					<!-- BEGIN PORTLET-->
					<div class="portlet light ">
						<div class="portlet-title">
							<div class="caption caption-md">
								<i class="icon-bar-chart theme-font hide"></i>
								<span class="caption-subject theme-font bold uppercase">									
									PROJECT STATUS DASHBOARD 
								</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
							</div>
						</div>
						<div class="portlet-body" style="height: 100%;overflow: auto;">
							<div class="dashboardContainerResponsive">
								<div class="form-group col-lg-12">
									<div id="loadingIconTM" style="display:none;z-index:100001;position:absolute;top:20px;left:50%">			
										<img src="css/images/ajax-loader.gif"></img>
									</div>
									<div id="overallStatusReport" style="display:none; margin:10px; background-color: #eff3f8; border-style: ridge; padding: 20px;"></div>
								</div>    
							</div>					      								
				         </div>								
																												
						</div>
					</div>
					<!-- END PORTLET-->
				</div>				
			</div>			
			
			<!-- END PAGE CONTENT INNER -->
		</div>
	</div>
	<!-- End Content -->
			
	<!-- BEGIN FOOTER -->
		<div><%@include file="footerlayout.jsp" %></div>			
	<!-- END FOOTER -->
	
	<div id="div_PopupMainUserProfile" class="divPopupMainUserProfile" style="display:none;border: white;display: block;background: none;width: 635px;max-height: 600px;">
		<div id="div_SelectedUserProfile"></div>
	</div>	

<script>

/*
var testingBrick='';
function clickOnbrick(e){
      
};

 jQuery(document).ready(function() {
   $("#menuList li:first-child").eq(0).remove();   // to remove fancytree icon near home tab   
   getTotalWPCount('workpackage.for.testmanager');
   
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
					    brick +='<div class="col-md-4 col-lg-4 col-sm-6 col-xs-12" style="padding: 10px;"><div class="dashboard-stat green-haze more">';					    
					    brick +='<div class="row"><div class="col-md-7" style="padding: 5px;"><a class="more" href="javascript:;">'+recordArr[index].name+'</a></div>';
					    brick +='<div class="col-md-5" style="padding: 5px;"><span style="color:white;margin-right: 5px;">STATUS :&nbsp;</span><span class="badge" style="background:#e35b5a;">'+wpstatus+'</span>';
					    brick +='<!-- <a href="javascripts:;"><i class="m-icon-swapright m-icon-white" style="margin-left: 5px;"></i></a> --></div></div>';					    
					    brick +='<div class="tile double">';
					    brick +='<div class="tile-body" style="padding:1px 1px;">';				    
					    brick +='<div class="row">',
					    brick +='<div class="col-md-4 divAlign"><label>Test Beds</label></div><div class="col-md-3"><span>: '+data[key].testBedCount+'</span></div></div>';				    
					    brick +='<div class="row"><div class="col-md-4 divAlign"><label>Execution Status</label></div><div class="col-md-2"><span>:'+data[key].totalExecutedTesCases+"/"+data[key].totalWPTestCase+'</span></div><div class="col-md-6"><span>['+exe_percent+'%]&nbsp;&nbsp;&nbsp;&nbsp;['+data[key].p2totalPass + " P/" + data[key].p2totalFail+ " F/" + tc_notrun+" NR/" + tc_blocked+" B]" +'</span></div></div>'
					    brick +='<div class="row"><div class="col-md-4 divAlign"><label>Schedule</label></div><div class="col-md-8"><span>: '+daydiffContent+' / '+data[key].wpStartEnddayDiff+' total days</span></div></div>';					   
					    brick +='<div class="row"><div class="col-md-4 divAlign"><label>Velocity</label></div><div class="col-md-4"><span>: '+executeperday+'/ day</span></div>',
					    brick +='<div class="col-md-4"><span>: '+executedlasthour+'/1 hr</span></div></div>';					    
					    brick +='<div class="row"><div class="col-md-4 divAlign"><label>Bugs Reported</label></div><div class="col-md-8"><span>: '+data[key].defectsCount+'</span></div></div></div>';
					    
					    brick +='</div>';					    						
						brick +='</div></div></div>';		
			
	                   if(!($('#overallStatusReport').hasClass('tiles'))){
	                   		$('#overallStatusReport').addClass('tiles');
	                   		$('#tileBrick1').append($(brick));
	                   }
						$('#overallStatusReport').append($(brick));
						$('#tileBrick').append($(brick));
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
}); */
</script>

<!-- END PAGE LEVEL SCRIPTS -->
<script src="js/viewScript/dashboardStatus.js" type="text/javascript"></script>
<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>

<script>
jQuery(document).ready(function() {
   $("#menuList li:first-child").eq(0).remove();   // to remove fancytree icon near home tab
   getTotalWPCount('workpackage.for.testmanager');
   var dashboardDateFilterVisible="${dashboardDateFilterVisible}";
   
	if(dashboardDateFilterVisible == 'yes') {
    	$('#planned_defaultrange').show();
    	$('#planned_fromTodatepicker').show();
    } else {
    	$('#planned_defaultrange').hide();
    	$('#planned_fromTodatepicker').hide();
    }
	  	
});

</script>
	
</body>
</html>