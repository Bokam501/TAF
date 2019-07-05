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
<link rel="stylesheet" href="css/style.css" type="text/css" media="all">
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="css/iosOverlay.css" type="text/css" media="all">
<link href="css/customStyle.css" rel="stylesheet" type="text/css">
<!-- END THEME STYLES -->

<style type="text/css">
#filter > ul > li > #status_dd >.select2me , #filter > ul > li > #primary_dd >.select2me{
	width: 100px !important;
}
#filter > ul > li > #status_dd >.select2me a , #filter > ul > li > #primary_dd >.select2me a{
	height: 28px;
    padding-top: 0px;
    font-size: 12px;
    width: 100px;
}
</style>

</head>
<!-- END HEAD -->
<body>
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
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->

<!-- BEGIN CONTAINER -->
<div class="page-container">
<div><%@include file="postHeader.jsp" %></div> 		
		
	<div class="page-content">
		<div class="container-fluid">			
			<!-- BEGIN PAGE CONTENT INNER -->
			<div class="row margin-top-10" id="toAnimate">
				<div class="col-md-12">
					<!-- BEGIN PORTLET-->
					<div class="portlet light ">
						<div class="portlet-title toolbarFullScreen">
							<div class="caption caption-md">
								<i class="icon-bar-chart theme-font hide"></i>
								<span class="caption-subject theme-font bold uppercase">APPROVE SKILLS</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
						    </div>
						</div>
						<div class="portlet-body">
							<div class="row list-separated" style="margin-top:0px;">	
							
							<!-- jtable started -->
								<!-- Main -->
								    <div id="main" style="float:left; padding-top:0px; width:100%;">
								         <div id="hidden"></div>
									     <!-- Filer -->
<!-- 									    <div id="filter" style="float:right;margin-right:60px "> -->
<!-- 									      <ul>        -->
<!-- 									         <li> -->
<!-- 									         <div id="status_dd" class="col-md-4"> -->
<!-- 												<select class="form-control input-small select2me" id="status_ul"> -->
<!-- 													<option value="2" ><a href="#">ALL</a></option> -->
<!-- 													<option value="1" ><a href="#">Approved</a></option> -->
<!-- 													<option value="0" ><a href="#">UnApproved</a></option> -->
<!-- 												</select> -->
<!-- 											</div> -->
<!-- 									        </li> -->
<!-- 									        <li> -->
<!-- 											<div id="primary_dd" class="col-md-4"> -->
<!-- 												<select class="form-control input-small select2me" id="primary_ul"> -->
<!-- 													<option value="2" ><a href="#">ALL</a></option> -->
<!-- 													<option value="1" ><a href="#">Primary</a></option> -->
<!-- 													<option value="0" ><a href="#">Non-Primary</a></option> -->
<!-- 												</select> -->
<!-- 											</div> -->
<!-- 									        </li> -->
<!-- 									      </ul> -->
<!-- 									    </div> -->
									    <!-- End Filter -->
								        <!-- ContainerBox -->
								        <div class="cl">&nbsp;</div>
								       <div class="jScrollContainerResponsive jScrollContainerFullScreen" style="clear:both;padding-top:10px;position: relative">
								       		 <!-- Filer -->
											    <div id="filter" class="jScrollTitleContainer" style="float:right;right:0px;position: absolute;Z-index: 10;margin-top: 5px;">
											      <ul>       
											         <li>
											         <div id="status_dd" class="col-md-4">
														<select class="form-control input-small select2me" id="status_ul">
															<option value="2" ><a href="#">ALL</a></option>
															<option value="1" ><a href="#">Approved</a></option>
															<option value="0" ><a href="#">UnApproved</a></option>
														</select>
													</div>
											        </li>
											        <li>
													<div id="primary_dd" class="col-md-4">
														<select class="form-control input-small select2me" id="primary_ul">
															<option value="2" ><a href="#">ALL</a></option>
															<option value="1" ><a href="#">Primary</a></option>
															<option value="0" ><a href="#">Non-Primary</a></option>
														</select>
													</div>
											        </li>
											      </ul>
											    </div>
									    <!-- End Filter -->
								      <div id="jTableContainer" class ="jTableContainerFullScreen"></div>
									 </div>
									<!-- End Container Box -->
								  
								    </div>
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
<!-- <script type="text/javascript" src="js/Scripts/popup/jquery.jtable.editinline.modifedColumn.js"></script> -->
<script type="text/javascript" src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>

<!-- Validation engine script file and english localization -->
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>
<script src="js/select2.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/Scripts/popup/iosOverlay.js"></script>		
<script type="text/javascript">

jQuery(document).ready(function() {	
	   QuickSidebar.init(); // init quick sidebar
	   $("#menuList li:first-child").eq(0).remove();
	   setBreadCrumb("Resource Pool Management");
	    //createHiddenFieldsForTree();
		//setPageTitle("Work Packages");
		var userId='<%=session.getAttribute("ssnHdnUserId")%>';
		var userRoleId='<%=session.getAttribute("roleId")%>';
	//	getTreeData('administration.workpackage.tree?userRoleId='+userRoleId+"&userId="+userId);
	/* 	$("#treeContainerDiv").on("select_node.jstree",
			     function(evt, data){
		   			var entityIdAndType =  data.node.data;
		   			var arry = entityIdAndType.split("~");
		   			var key = arry[0];
		   			var type = arry[1];
		   			var title = data.node.text;
					var date = new Date();
				    var timestamp = date.getTime();
				    var nodeType = type;
				    var loMainSelected = data;
				    var approverTab ='yes';
			        uiGetParents(loMainSelected);
			        if(nodeType == "WorkPackage"){
			        	workPackageId = key;
			        //	urlToGetTimeEntriesOfWorkPackage = 'workpackage.timesheet.entries.approve.list?workPackageId='+workPackageId+"&timeStamp="+timestamp;    
			        	urlApprovingUserSkillsList= 'administration.userskills.list?toApprove=yes&selfIsPrimary='+2+'&managerIsPrimary='+2+'&isApproved='+0; 
			        	listUserSkillResultsforApproval(userRoleId,approverTab);
			        }
				    
			   }
		); */
		

	});
loadUserSkillsBasedonStatus($("#status_ul").find('option:selected').val(), "#status_dd");
$(document).on('change','#status_ul', function() {  
	var id = $("#status_ul").find('option:selected').val();
  	var filterType = "#status_dd";  	
  	loadUserSkillsBasedonStatus(id, filterType);
 });

$(document).on('change','#primary_ul', function() {	
	var id = $("#primary_ul").find('option:selected').val();
  	var filterType = "#primary_dd";
	 loadUserSkillsBasedonStatus(id, filterType);
 });
var SOURCE;
var urlToGetTimeEntriesOfWorkPackage = "";
var weekDateNames = new Array();

var workhours = {'0': '0','1': '1','2': '2', '3': '3',	'4': '4', '5': '5', '6': '6', '7': '7', '8': '8', '9': '9',
	'10': '10','11': '11','12': '12'};
	
var workmins = {'0': '0','1': '1','2': '2', '3': '3',	'4': '4', '5': '5', '6': '6', '7': '7', '8': '8', '9': '9',
	'10': '10','11': '11','12': '12', '13': '13','14': '14', '15': '15', '16': '16', '17': '17', '18': '18', '19': '19',
	'20': '20','21': '21','22': '22', '23': '23','24': '24', '25': '25', '26': '26', '27': '27', '28': '28', '29': '29',
	'30': '30','31': '31','32': '32', '33': '33','34': '34', '35': '35', '36': '36', '37': '37', '38': '38', '39': '39',
	'40': '40','41': '41','42': '42', '43': '43','44': '44', '45': '45', '46': '46', '47': '47', '48': '48', '49': '49',
	'50': '50','51': '51','52': '52', '53': '53','54': '54', '55': '55', '56': '56', '57': '57', '58': '58', '59': '59',
	'60': '60'};


function loadUserSkillsBasedonStatus(id, filterType){
var date = new Date();
var roleid='<%=session.getAttribute("roleId")%>';
if(filterType == "#status_dd"){	
	urlApprovingUserSkillsList= 'administration.userskills.list?toApprove=yes&selfIsPrimary='+2+'&managerIsPrimary='+2+'&isApproved='+id;  
 }else if (filterType == "#primary_dd"){	
	 urlApprovingUserSkillsList= 'administration.userskills.list?toApprove=yes&selfIsPrimary='+id+'&managerIsPrimary='+2+'&isApproved='+2;		  
 }  
 	listUserSkillResultsforApproval(roleid);
}	
//callJtableUpdate("'+data.record.id+'")
function callJtableUpdate(isPrimaryIdbyManager, userSkillId){
	
	//$( "#"+isPrimaryIdbyManager ).focus();
	var imgid = isPrimaryIdbyManager+userSkillId;
	//alert("--isPrimaryIdbyManager--"+isPrimaryIdbyManager+"--userSkillId--"+userSkillId+"--imgid--"+imgid);
	if(isPrimaryIdbyManager == 0 || isPrimaryIdbyManager == 2){
		isPrimaryIdbyManager=1;
	}else if(isPrimaryIdbyManager ==1 ){
		isPrimaryIdbyManager=0;
	}
	
	
	$.ajax({ 
			type: "POST",
			url:'administration.userskills.approve.update.inline?userSkillId='+userSkillId+'&modifiedField=managerIsPrimary&modifiedFieldValue='+isPrimaryIdbyManager,
			success : function(data) {
				//alert("data.Result--"+data.Result);
				if(data.Result=="ERROR"){
		    		callAlert(data.Message);
		    		return false;
		    	}
				   if(data.Result=="OK"){
					
					  $("#"+imgid).attr("src", 'css/images/right.jpg');
		    		return true;
		    	}   
			}
		 });	
	/*  urlToGetWorkPackageTestcasesOfSpecifiedWorkPackageId = 'workpackage.testcase.plan?workPackageId='+workPackageId+"&timeStamp="+timestamp+"&testLeadId=0&testerId=0&envId=0&localeId=0&plannedExecutionDate=&dcId=0&executionPriority=0";
	  listWorkPackageTestcasesOfSelectedWorkPackage(); */
	
	
}
function listUserSkillResultsforApproval(roleid){	
try{
	if ($('#jTableContainer').length>0) {
		 $('#jTableContainer').jtable('destroy'); 
	}
} catch(e) {}
	showUserSkillsTable();
}

function showUserSkillsTable(){	
$('#jTableContainer').jtable({
    title: 'Approve Users skills',
    editinline:{enable:true},
    editInlineRowRequestMode : true,   
    selecting: true,  //Enable selecting 
    paging: true, //Enable paging
    pageSize: 10, 
      actions: {
   	 	listAction: urlApprovingUserSkillsList,
   	    editinlineAction : 'administration.userskills.approve.update',
    },  
    fields : {
		userSkillId: { 
				key: true,
				type: 'hidden',
				create: false, 
				edit: false, 
				list: false /* ,
				display: function (data) { 
				 return data.record.productFeatureId;
             }  */
				}, 
			/* productFeatureName: { 
 	  		title: 'Feature Name',
 	  		inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
 	  		width: "20%",
 	  		list:true
			 	}, */
			userId: {
			       title: 'Users Name',
			       list:true,
		    	   edit: false,
		    	   create : false,
			       	inputTitle: 'User Name <font color="#efd125" size="4px">*</font>',
			        width:"20%",				        
			        options: 'common.user.list.testmanagerorallusers?approverList=no',
				     },	
		skillId: {
	        title: 'Skill',
	        list:true,
	    	edit: false,
	    	create : true,
	       	inputTitle: 'Skill Name <font color="#efd125" size="4px">*</font>',
	        width:"20%",				        
	        options: 'common.list.skill.list?skillIdfromUI='+ 0,
	        },
			selfIsPrimary: {
					 title: 'Is Primary Skill' ,
					 inputTitle: 'User Primary Skill is <font color="#efd125" size="4px">*</font>',
					// width: "10%",  
					 list:true,
					 edit:false,
					 create:false,
					 type : 'checkbox',
					 values: {'0' : 'No','1' : 'Yes'},
			    	defaultValue: '1',
			    	display:function(data)
	    		 {
	        	   if(data.record){
	        		  var selfStatus = data.record.selfIsPrimary;						
					if(selfStatus == 0){
	        		 		return '<img title="click to check" val='+data.record.selfIsPrimary+' style="cursor:pointer;" src="css/images/crossmark.jpg">';  
					}else if( selfStatus == 1){
	        	  			 return '<img title="click to check" val='+data.record.selfIsPrimary+' style="cursor:pointer;" src="css/images/right.jpg">';	
					}else if( selfStatus == 2){
						
	        	  		//Do nothing, manager will take care
	        	  	}
	        	   }
	           	}
		   		},
		
		selfSkillLevelId: {                
			 title: 'Skill Level',
			 list:true,
			 edit: true,
			 create : true,
		     inputTitle: 'Skill <font color="#efd125" size="4px">*</font>',
		     width:"20%",				        
		        		options: 'common.userskills.skillLevelsList?isApprover=no'
		},			     
		approvingManagerId: {
	        title: 'Approver',
	        list:true,
	    	edit: false,
	    	create : false,
	       	inputTitle: 'Approver  <font color="#efd125" size="4px">*</font>',
	        width:"20%",				        
	       options: 'common.user.list.testmanagerorallusers?approverList=yes',
	        },
		managerSkillLevelId: {
		       title: 'Approved-Skill Level',
		       list:true,
	    	  // edit: true,
	    	   create : true,
	       		inputTitle: 'Approver Skill <font color="#efd125" size="4px">*</font>',
	       	 	width:"20%",				        
	       	 	options: 'common.userskills.skillLevelsList?isApprover=no'
		     },
		/* selfIsPrimary:{
					title : 'Self Is Primary',
					edit : false,
					width: "8%",
				}, */					
	
  	managerIsPrimary: {
				 title: 'Approved-Is Primary Skill' ,
				 inputTitle: 'Is Primary Skill <font color="#efd125" size="4px">*</font>',
				 width: "10%",  
				 list:false,
				 //edit:true,
				 create:false,
				 type : 'checkbox',
				 values: {'0' : 'No','1' : 'Yes'},
		    	defaultValue: '1',
		    		display:function(data)
		      		 {
		          	   if(data.record){
		          		  var managerStatus = data.record.managerIsPrimary;		
		          		  var misprimary = data.record.managerIsPrimary;
		          		  var userskillid = data.record.userSkillId;
		          		  var imgid = misprimary+userskillid;		          		 
		   				if(managerStatus == 0){
		          		 		return '<img id='+imgid+' title="click to check" val='+data.record.managerIsPrimary+' onclick=callJtableUpdate('+misprimary+'\,'+userskillid+'); style="cursor:pointer;" src="css/images/crossmark.jpg">';  
		   				}else if( managerStatus == 1){
		          	  			 return '<img id='+imgid+' title="click to check" val='+data.record.managerIsPrimary+' onclick=callJtableUpdate('+misprimary+'\,'+userskillid+'); style="cursor:pointer;" src="css/images/right.jpg">';	
		   				}else if( managerStatus == 2){
		   					return '<img id='+imgid+' title="click to check" val='+data.record.managerIsPrimary+' onclick=callJtableUpdate('+misprimary+'\,'+userskillid+'); style="cursor:pointer;" src="css/images/crossmark.jpg">';		   					
		          	  		//Do nothing, manager will take care
		          	  	}
		          	   }
		             	}
		  		},
		  		
    			/* {
				 title: 'Is Primary Skill' ,
				 inputTitle: 'User Primary Skill is <font color="#efd125" size="4px">*</font>',				
				 list:true,
				 edit:false,
				 create:false,
				 type : 'checkbox',
				 values: {'0' : 'No','1' : 'Yes'},
		    	defaultValue: '1',
		    	display:function(data)
   		 {
       	   if(data.record){
       		  var selfStatus = data.record.selfIsPrimary;						
				if(selfStatus == 0){
       		 		return '<img title="click to check" val='+data.record.selfIsPrimary+' style="cursor:pointer;" src="css/images/crossmark.jpg">';  
				}else if( selfStatus == 1){
       	  			 return '<img title="click to check" val='+data.record.selfIsPrimary+' style="cursor:pointer;" src="css/images/right.jpg">';	
				}else if( selfStatus == 2){
					
       	  		//Do nothing, manager will take care
       	  	}
       	   }
          	} */
	   		
		/* status: {
					 title: 'Status' ,
					 inputTitle: 'Status <font color="#efd125" size="4px">*</font>',
					 width: "10%",  
					 list:true,
					 edit:true,
					 create:false,
					 type : 'checkbox',
					 values: {'0' : 'No','1' : 'Yes'},
			    		defaultValue: '1'
		   		}, */	
		/* managerIsPrimary:{
					title : 'Manager Is Primary',
					edit : false,
					width: "8%",
				}, */
		isApproved: { 
	           	title: 'Is Approved' ,
	           	width: "10%",  
	           	list:true,
	           	create: false,
	           	edit:true,
	           	type : 'checkbox',
		   		values: {'1' : 'Yes','0' : 'No'}	            
	         },
		fromDate: { 
       				title: 'FromDate' , 
      				edit: false, 
					list: true,
       				type: 'date',
       				width: "30%"          	
               		},
  		toDate: { 
   				title: 'ToDate' , 
           		edit: false, 
   				list: true,
           		type: 'date',
           		width: "30%"          	
           		},	
		
	},  // This is for closing $img.click(function (data) {  
      formSubmitting: function (event, data) {
    	//  data.form.find('input[name="approvedDate"]').addClass('validate[required]');	           
          data.form.validationEngine();
         return data.form.validationEngine('validate');
     }, 
      //Dispose validation logic when form is closed
      formClosed: function (event, data) {
         data.form.validationEngine('hide');
         data.form.validationEngine('detach');
     }
	});
 $('#jTableContainer').jtable('load');

}


</script>
</body>
</html>