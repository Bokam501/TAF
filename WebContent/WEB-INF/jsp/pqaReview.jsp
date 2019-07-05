<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="com.hcl.atf.taf.controller.HomePageController"%>
<%@page import="com.hcl.atf.taf.model.UserList"%>
<%@page import="java.util.List"%>
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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<link type="text/css" href="css/jquery.jscrollpane.css" rel="stylesheet"
	media="all" />
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css"
	type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link
	href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235"
	rel="stylesheet" type="text/css" />
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet"
	type="text/css"></link>
	<link rel="stylesheet" href="css/iosOverlay.css" type="text/css" media="all">
<link href="css/customStyle.css" rel="stylesheet" type="text/css">
<!-- END THEME STYLES -->
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body>
	<!-- BEGIN HEADER -->
	<div class="page-header">
		<!-- BEGIN HEADER TOP -->
		<div><%@include file="headerlayout.jsp"%></div>
		<!-- END HEADER TOP -->
		<!-- BEGIN HEADER MENU -->
		<div class="page-header-menu">
			<div class="container container-position">
				<!-- BEGIN MEGA MENU -->
				<div><%@include file="menu.jsp"%></div>
				<!-- END MEGA MENU -->
			</div>
		</div>
		<!-- END HEADER MENU -->
	</div>
	<!-- END HEADER -->

	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<div><%@include file="treeStructureSidebar.jsp"%></div>
		<div><%@include file="postHeader.jsp"%></div>

		<!-- BEGIN PAGE CONTENT -->
		<div class="page-content">
			<div class="container-fluid">
				<!-- BEGIN PAGE CONTENT INNER -->
				<div class="row margin-top-10" id="toAnimate">
					<div class="col-md-12">
						<!-- BEGIN PORTLET-->
						<div class="portlet light ">
							<div class="portlet-title">
								<div class="caption caption-md">
									<i class="icon-bar-chart theme-font hide"></i> <span
										class="caption-subject theme-font bold uppercase">ACTIVITY TASK</span>&nbsp;<span
										id="headerTitle" class="caption-subject theme-font"></span> <span
										class="caption-helper hide">weekly stats...</span>
								</div>
							</div>
							<div class="portlet-body">
								<div class="row list-separated" style="margin-top: 0px;">
								<div id="reviewFilter" style="float:right;display:none">	                                               
											<div id="reviewFilter_dd" class="col-md-4">
											<select class="form-control input-small select2me" id="reviewFilter_ul">
												<option value="-1" ><a href="#">All</a></option>
												<option value="1" ><a href="#">Review Completed</a></option>
												<option value="0" selected><a href="#">Review Pending</a></option>
											</select>
										</div>
    								</div>
									<!-- jtable started -->
									<!-- Main -->
									<div id="main"
										style="float: left; padding-top: 0px; width: 100%;">
										<div id="hdnDiv"></div>

										<!-- Box -->
										<div class="box">
											<div id="hidden"></div>
											<!-- ContainerBox -->
											<div class="cl">&nbsp;</div>
											<div class="jScrollContainerResponsive"
												style="clear: both; padding-top: 10px">
												<div id="jTableContainer"></div>
											</div>
											<!-- End Container Box -->

											<div class="cl">&nbsp;</div>
										</div>
										<!-- End Box -->
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
	<div><%@include file="footerlayout.jsp"%></div>
	<!-- END FOOTER -->
	<!-- Popup -->
	<div id="div_PopupMain" class="modal " tabindex="-1" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						title="Press Esc to close" onclick="popupClose()"
						aria-hidden="true"></button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body">
					<div class="scroller" style="height: 320px" data-always-visible="1"
						data-rail-visible1="1">
						<div class="jScrollContainerResponsive"
							style="clear: both; padding-top: 10px">
							<div id="jtableSelectEnv"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--  <div id="div_PopupMain" class="divPopUpMain" style="display:none;"> -->
	<!--         <div title="Press Esc to close" onclick="popupClose()" class="ImgPopupClose"> -->
	<!--         </div> -->
	<!-- 	</div> -->
	<div id="div_PopupBackground"></div>
	<!-- Popup Ends -->

	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script
		src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/jquery.vmap.js"
		type="text/javascript"></script>
	<script
		src="files/lib/metronic/theme/assets/global/plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js"
		type="text/javascript"></script>
	<script
		src="files/lib/metronic/theme/assets/global/plugins/jquery.sparkline.min.js"
		type="text/javascript"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script
		src="files/lib/metronic/theme/assets/admin/layout/scripts/quick-sidebar.js"
		type="text/javascript"></script>
	<script
		src="files/lib/metronic/theme/assets/global/plugins/jstree/dist/jstree.min.js"
		type="text/javascript"></script>
	<!-- END PAGE LEVEL SCRIPTS -->

	<!-- the jScrollPane script -->
	<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

	<!-- Include jTable script file. -->
	<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>

	<!-- Plugs ins for jtable inline editing -->
	<script type="text/javascript"
		src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
	<script type="text/javascript"
		src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>

	<!-- Validation engine script file and english localization -->
	<script src="js/select2.min.js" type="text/javascript"></script>
	<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
	<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
	<script src="js/bootstrap-datetimepicker.min.js" type="text/javascript" ></script>
	<script src="js/moment.min.js" type="text/javascript"></script>
	<script src="js/daterangepicker.js" type="text/javascript"></script>
	<script src="js/components-pickers.js" type="text/javascript"></script>

	<!-- the jScrollPane script -->
	<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>

	<!-- Include jTable script file. -->
	<script src="js/Scripts/popup/jquery.jtable.js" type="text/javascript"></script>

	<!-- Plugs ins for jtable inline editing -->
	<script type="text/javascript"
		src="js/Scripts/popup/jquery.jtable.editinline.js"></script>
	<script type="text/javascript"
		src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>
	<!-- <script type="text/javascript" src="js/Scripts/popup/jquery.jtable.js"></script> -->

	<!-- Validation engine script file and english localization -->
	<script type="text/javascript"
		src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
	<script type="text/javascript"
		src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>
<div><%@include file="addComments.jsp"%></div>
<script type="text/javascript">
var prodId=0;
prodId=document.getElementById("treeHdnCurrentProductId").value;
function addComments(record){	
	
	
	var activityTaskUI = [];
	var taskName= record.activityTaskName;
		var modifiedByName=record.reviewerName;
	var modifiedById=record.reviewerId;
	var primaryStatusId=record.statusId;
	var sourceStatus=record.statusName;
	var taskId=record.activityTaskId;
	var effortList='';
	var actionTypeValue=2;	
	var productId=document.getElementById("treeHdnCurrentProductId").value;
	
 $("#addCommentsMainDiv").modal();			

 var jsonObj = {
		Title : "Edit Status",	
		entityType: 'process.list.activity.entity.master',		
		entityTypeDefault: '1',
		entityNameDefault:taskName,
	    modifiedBy: 'common.user.list.by.resourcepool.id',		
	    modifiedByDefault: modifiedById,
	    raisedDate: new Date(),
	    comments:"",	   
		activityStatus:'activity.primary.status.master.option.list?productId='+productId,
		activityStatusDefault:primaryStatusId,
		secondaryStatus:'activity.secondary.status.master.option.list?statusId='+primaryStatusId,
		taskId:taskId,
		effortList:'activitytask.effort.tracker.list?taskId='+taskId,
		sourceStatus:sourceStatus,
		actionTypeValue:actionTypeValue,
		commentsName:commentsReviewActivity
	  // commentsStatus: "['started','InProgress','Completed']",		
	};
	AddComments.init(jsonObj);
	}

</script>
	<script type="text/javascript">
		jQuery(document)
				.ready(
						function() {
							QuickSidebar.init(); // init quick sidebar
							setBreadCrumb("Activity Task Management");
							createHiddenFieldsForTree();
							setPageTitle("Products");									
							var actionType=2;
							//getTreeData('process.review.activity.tree?type='+actionType);
							getTreeData('administration.product.activity.tree?actionType='+actionType);
							$("#treeContainerDiv")
									.on(
											"select_node.jstree",
											function(evt, data) {
												var entityIdAndType = data.node.data;
												var arry = entityIdAndType.split("~");
												var key = arry[0];
												var type = arry[1];
												var title = data.node.text;
												var date = new Date();
												var timestamp = date.getTime();
												var nodeType = type;
												var loMainSelected = data;
												var activityId;
												var activityWorkPackageId;
												uiGetParents(loMainSelected);

											/* 	if (nodeType == "ActivityWorkPackage") {
													activityWorkPackageId = key;
													listActivitiesOfSelectedAWP(activityWorkPackageId);
												} */
												if (nodeType == "Activity") {
														activityId = key;
														document.getElementById("treeHdnCurrentActivityId").value = activityId;
														var reviewfilter = $("#reviewFilter_ul").find('option:selected').val();	
														listActivitiesOfSelectedActivity(activityId,actionType,reviewfilter);														
														$("#reviewFilter").show();
														
												}
												$(document).on('change','#reviewFilter_ul', function() {
													activityId = key;
													var reviewfilter = $("#reviewFilter_ul").find('option:selected').val();	
													listActivitiesOfSelectedActivity(activityId,actionType,reviewfilter);
													$("#reviewFilter").show();
													 });

											});

						});

		/* Pop Up close function */
		function popupClose() {
			//document.getElementById("lbl_PopupTitle").innerHTML = "";
			//document.getElementById("lbl_RightTitle").innerHTML = "";
			//$("#tbl_PopupData").empty();
			$("#div_PopupMain").fadeOut("normal");
			$("#div_PopupBackground").fadeOut("normal");
		}
			function reviewAllTask(){			
		    activityId = document.getElementById("treeHdnCurrentActivityId").value;			    
			var reviewItemsListsFromUI = [];
			var $selectedFeatureRows = '';			   
			//alert("activityId"+activityId);
			$selectedFeatureRows = $('#jTableContainer').jtable('selectedRows');
				if($selectedFeatureRows.length == 0){
					callAlert("Please select task(s) for bulk approval");
				}

			$selectedFeatureRows.each(function () {
				    var record = $(this).data('record');
				    var taskId = record.activityTaskId;
				   // alert(record.isPeerReviewed);
				    reviewItemsListsFromUI.push(taskId);
				    
				});
						
		
			if(reviewItemsListsFromUI.length == null || reviewItemsListsFromUI ==''){
					callAlert("Select atleast one Task","ok");
				}				
				else{			
					
				
				$.post('activitytask.pqa.reviewall.update?reviewItemsListsFromUI='+reviewItemsListsFromUI,function(data){
					if(data.Result=="OK"){
					iosOverlay({
						text: "Selected task(s) are approved", // String
						icon: "css/images/check.png", // String (file path)
						spinner: null,
						duration: 1500, // in ms
						});
					var reviewfilter = $("#reviewFilter_ul").find('option:selected').val();
					//activityId=document.getElementById("treeHdnCurrentActivityId").value;
					//alert(activityId);
					var actionType=2;
				    listActivitiesOfSelectedActivity(activityId,actionType,reviewfilter);
					}
					else{
						iosOverlay({
							text: "Task(s) not approved", // String
							icon: "css/images/cross.png", // String (file path)
							spinner: null,
							duration: 1500, // in ms
							});
					}
				
					});
				}
				
		}

		/* Load Poup function */
		function loadPopup(divId) {
			$("#" + divId).fadeIn(0500); // fadein popup div
			$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
			// IE7, IE8
			$("#div_PopupBackground").fadeIn(0001);
		}
			
		function listActivitiesOfSelectedActivity(activityId,actionType,reviewfilter) {
			var urlToGetActivitiesOfSpecifiedActivityId = 'activitytask.list.for.review?activityId='+activityId+'&actionType='+actionType+'&reviewFilter='+reviewfilter;

			try {
				if ($('#jTableContainer').length > 0) {
					$('#jTableContainer').jtable('destroy');
				}
			} catch (e) {
			}
			$('#jTableContainer')
					.jtable(
							{
								title : 'Activity Task for PQA Review',
								selecting : true, //Enable selecting
								multiselect : true, //Allow multiple selecting
								selectingCheckboxes : true, //Show checkboxes on first column
								editinline : {
									enable : true
								},								
								paging : true, //Enable paging								
								pageSize : 10,
								toolbar : {
	                                items : [{
												text : 'Approve All',
												click : function() {				
												reviewAllTask();
													}
											},]
					
							    },
								pageSize : 10,
								actions : {
									listAction : urlToGetActivitiesOfSpecifiedActivityId,
									editinlineAction : 'process.activitytask.update',
								},
								fields : {
									
								
									activityId : {
							 				type: 'hidden', 
							 				defaultValue: activityId 
									},
								
									activityTaskId : {
										key : true,
										list : false,
										create : false,
									},									
									activityTaskName:{
						                 title: 'Activity Task',
						                 inputTitle: 'Activity Task <font color="#efd125" size="4px">*</font>',
						            	 list:true,
						            	 edit:true,
						            	 create:true,
						                 width:"20%"
						             },

										activityTaskTypeId : {
												title : 'Activity TaskType Name',
												create : true,
												list : true,
												edit : true,
												width : "20%",
												options : 'common.list.activity.tasktype?productId='+prodId,
											},
						             activityMasterName : {
											title : 'Activity Type',
											list : true,
											edit : false,
											create : false,
										},						            
														
									categoryId : {
										title : 'Category',
										inputTitle : 'Category <font color="#efd125" size="4px">*</font>',
										list : true,
										create : true,
										edit : true,

										options : function(data) {
											return 'common.list.executiontypemaster.byentityid?entitymasterid=1'
										}
									},								 
									 enviromentCombinationName:{										
										title : 'Environment Combination',
										inputTitle : 'Category <font color="#efd125" size="4px">*</font>',
										list : true,
										create : true,
										edit : true,
										/* options : function(envdata) {
											return 'environment.combinations.options.by.activity?activityId='+envdata.record.activityId
										} */
								   }, 
									assigneeId : {
										title : 'Assignee',
										inputTitle : 'Assignee <font color="#efd125" size="4px">*</font>',
										list : true,
										create : true,
										edit : true,
										options : function(data) {
											return 'common.user.list'
										}
									},
									reviewerId : {
										title : 'Reviewer',
										list : true,
										create : true,
										edit : true,
										width : "20%",
										options : function(data) {
											return 'common.user.list'
										}
									},
									priorityId : {
										title : 'Priority',
										inputTitle : 'Priority <font color="#efd125" size="4px">*</font>',
										list : true,
										create : true,
										edit : true,
										width : "5%",
										options : 'administration.executionPriorityList'
									},
									statusId : {
										title : 'Status',
										create : false,
										list : true,
										edit : false,
										width : "20%",
										options : 'process.list.process.statuses'
									},
									secondaryStatusId:{
							  			title: 'Secondary Status',
						     	  		inputTitle: 'Secondary Status <font color="#efd125" size="4px">*</font>',
						     	  		width: "50%",
						     	  		list:true, 
						     	  		create:true,
						     	  		edit:false,
										dependsOn:'statusId',
										options:function(data){
											if(data.dependedValues.statusId != null){
												return 'activity.secondary.status.master.option.list?statusId='+data.dependedValues.statusId;
											}
							     		}
							  		}, 
									resultId : {
										title : 'Result',
										create : false,
										list : true,
										edit : true,
										width : "20%",
										options : 'process.list.activity.result'
									},
									remark : {
										title : 'Remarks',
										type: 'textarea',  
										list : true,
										width : "10%",
										width : "20%",
										list : false,
									},
									plannedStartDate : {
										title : 'Planned Start Date',
										inputTitle : 'Planned Start Date <font color="#efd125" size="4px">*</font>',
										edit : true,
										create : true,
										list : true,
										type : 'date',
										width : "20%",
									},
									plannedEndDate : {
										title : 'Planned End Date',
										inputTitle : 'Planned End Date <font color="#efd125" size="4px">*</font>',
										edit : true,
										list : true,
										create : true,
										type : 'date',
										width : "20%",
									},
									actualStartDate : {
										title : 'Actual Start Date',
										create : false,
										edit : true,
										list : false,
										type : 'date',
										width : "20%",
									},
									actualEndDate : {
										title : 'Actual End Date',
										create : false,
										edit : true,
										list : false,
										type : 'date',
										width : "20%",
									},
									activityEffortTrackerId:{
	        				        	title : 'Effort',
	        				        	list : true,
	        				        	create : false,
	        				        	edit : false,
	        				        	width: "10%",
	        				        	display:function (data) { 
	        				           		//Create an image for test script popup 
	        								var $img = $('<i class="fa fa-clock-o" title="Enter effort spent for task"></i>');
	        			           			//Open Testscript popup  
	        			           			$img.click(function () {
	        			           				//displayTestScriptsFromTestCases(data.record.testCaseId,"TestCase"); 			           				
	        			           				addComments(data.record);			           				
	        			           		  });
	        			           			return $img;
	        				        	}
	        				        },

								}, // This is for closing $img.click(function (data) {  
							});
			$('#jTableContainer').jtable('load');
		}
							
		document.onkeydown = function(evt) {
			evt = evt || window.event;
			if (evt.keyCode == 27) {
				if (document.getElementById("div_PopupMain").style.display == 'block') {
					popupClose();
				}
			}
		};
	</script>
<script type="text/javascript" src="js/Scripts/popup/iosOverlay.js"></script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>