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
<link rel="stylesheet" href="css/jquery/themes/vader/jquery-ui.css" type="text/css" media="all">
<!-- Include one of jTable styles. -->
<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
<link href="js/Scripts/validationEngine/validationEngine.jquery.css" rel="stylesheet" type="text/css" />
<!-- <link href="css/customStyle.css" rel="stylesheet" type="text/css"> -->
<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>
<!-- END THEME STYLES -->

<style type="text/css">
#filter > .col-md-4 >.select2me{
	height: 26px;
    padding-top: 2px;
    margin-top: 7px;
    width: 101px !important;
    font-size: 13px;
}
.ui-dialog-buttonset{
	width: 215px;
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
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->

<!-- BEGIN CONTAINER -->
<div class="page-container">
<div><%@include file="postHeader.jsp" %></div>

<!-- BEGIN PAGE CONTENT -->
<div class="page-content">
		<div class="container-fluid">			
			<!-- BEGIN PAGE CONTENT INNER -->
			<div class="row margin-top-10" id="toAnimate">
				<div class="col-md-12">
					<!-- BEGIN PORTLET-->
					<div class="portlet light ">
						<div class="portlet-title toolbarFullScreen" style="margin-bottom:0px;">
							<div class="caption caption-md">
								<i class="icon-bar-chart theme-font hide"></i>
								<span class="caption-subject theme-font bold uppercase">MANAGE DATA Extractor</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
								<span class="caption-helper hide">weekly stats...</span>
							</div>
							<div class="actions" style="padding-top: 4px;padding-left: 5px;">
								<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" 
									onClick="fullScreenHandlerDTDataExtractor()" data-original-title="" title="FullScreen"></a>
							</div>
						</div>
						<div class="portlet-body">
							<div class="row list-separated" style="margin-top:0px;">	
							
							<!-- jtable started -->
								<!-- Main -->
								    <div id="main" style="float:left; padding-top:0px; width:100%;">
								    
								  
								    <div id="hdnDiv"> </div>
								    			<!-- <input type="hidden" id="currentWorkPackage" value="" ></input>
												<input type="hidden" id="currentReservationShift" value="" ></input>
												<input type="hidden" id="currentReservationDate" value="" ></input>
												<input type="hidden" id="currentResourceDemandCount" value="" ></input>
												<input type="hidden" id="currentAvailabilityType" value="" ></input>
												<input type="hidden" id="currentViewType" value="" ></input> -->
								    <div id="hidden"></div>
								        <!--  <div id="selectedUserDetailshidden"></div>			 -->				       
								       
								        <!-- ContainerBox -->
								       <!-- <div class="jScrollContainer"> -->
								      <div style="clear:both;position:relative"> 
								      	<!-- Filter -->
										 <!-- <div id="filter" style="float:right;position: absolute;Z-index: 10;right: 56px;">
												<div id="status_dd" class="col-md-4">
												<select class="form-control input-small select2me" id="status_ul">
													<option value="2" ><a href="#">ALL</a></option>
													<option value="1" selected><a href="#">Active</a></option>
													<option value="0" ><a href="#">Inactive</a></option>
												</select>
											</div>
	    								</div> -->
									<!-- End Filter -->
								      <div id="jTableContainer" class ="jTableContainerFullScreen"></div>
									 </div>
									 <div id="dataTableContainerForDataExtractor"></div>
									<!-- End Container Box -->
								 
									<div class="cl">&nbsp;</div>
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
	<div><%@include file="comments.jsp"%></div>
	<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
	<script type="text/javascript" src="js/datatable/Editor-1.5.6/js/dataTables.editor.js"></script>			
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
<script type="text/javascript" src="js/Scripts/popup/jquery.noty.packaged.min.js"></script>

<!-- Validation engine script file and english localization -->
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/Scripts/validationEngine/jquery.validationEngine-en.js"></script>
<div><%@include file="schedule.jsp"%></div>
<div><%@include file="attachments.jsp" %></div>
<script type="text/javascript">
//var customerId = ${customerId};
var urlToListDataExtractorBasedOnStatus ='';
jQuery(document).ready(function() {	
   urlToListDataExtractorBasedOnStatus = 'data.source.extractor.list?status=2';
   //loadDataExtractorList(urlToListDataExtractorBasedOnStatus);
   dataExtractorDataTable(urlToListDataExtractorBasedOnStatus);
   $("#menuList li:first-child").eq(0).remove();
   setBreadCrumb("Manage Data Extractors");
   /* $(document).on('change','#status_ul', function() {
		var id = $("#status_ul").find('option:selected').val();				
	    urlToListDataExtractorBasedOnStatus = 'data.source.extractor.list?status='+id;		
	    loadDataExtractorList(urlToListDataExtractorBasedOnStatus);	    
	 }); */
});


/*var customerId = 0;
function loadDataExtractorList(urlToListDataExtractorBasedOnStatus){	
	try{
		if($('#jTableContainer').length > 0){
			$('#jTableContainer').jtable("destroy");
		}
	}catch(e){
		
	}
	$('#jTableContainer').jtable({

      	title: 'Add/Edit Data Extractor(s)',
      	selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},
		recordsLoaded: function(event, data) {
        	 $(".jtable-edit-command-button").prop("disabled", true);
         },
		actions: { 
			listAction: urlToListDataExtractorBasedOnStatus,
			createAction: 'data.source.extractor.add',
			editinlineAction: 'data.source.extractor.update',
			//deleteAction: 'data.source.extractor.delete', 
		
		}, 	 				
		fields: { 
			id: { 
				type: 'hidden', 
				key: true, 
				create: false, 
               	edit: false, 
				list: false
			},
			jobName: { 
   				title: 'Job Name' , 
   				edit : false,
   				list : false,
   				create : false
    		},
			extractorName: { 
				title: 'Name' , 
				inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
				width: "10%",
				edit : false
			},
			customerId: { 
				title: 'Customer',
				inputTitle: 'Customer <font color="#efd125" size="4px">*</font>',
				width: "10%",
				edit : false,
				create : true,
				list : false,
				options:function(data){
		        	return 'data.source.extractor.customer.by.user.option.list';
		        }
			},
			customerName: { 
				title: 'Customer' ,
				inputTitle: 'Customer',
				width: "10%",
				edit : false,
				create : false,
			},
			engagementId: { 
				title: 'Engagement',
				inputTitle: 'Engagement <font color="#efd125" size="4px">*</font>',
				width: "10%",
				edit : false,
				create : true,
				list : false,
				dependsOn: 'customerId', 
				defaultValue : 0,
				options:function(data){
					customerId = data.dependedValues.customerId;
					return 'data.source.extractor.engagement.by.user.customer.option.list?customerId='+data.dependedValues.customerId;
		        }
			},
			engagementName: { 
				title: 'Engagement' ,
				inputTitle: 'Engagement',
				width: "10%",
				edit : false,
				create : false,
			},
			productId: { 
   				title: 'Product' , 
   				inputTitle: 'Product',
   				width: "10%",
   				edit : false,
   				list : false,
   				dependsOn: 'engagementId', 
   				options:function(data){
		        	return 'data.source.extractor.product.by.user.customer.engagement.option.list?customerId='+customerId+'&engagementId='+data.dependedValues.engagementId;
		        },
    		},
    		productName: { 
   				title: 'Product' , 
   				inputTitle: 'Product <font color="#efd125" size="4px">*</font>',
   				width: "10%",
   				edit : false,
   				create : false,
    		},
			competencyId: { 
				title: 'Competency' ,
				inputTitle: 'Competency',
				width: "10%",
				edit : false,
				list : false,
				options:function(data){
		        	return 'dimension.list.options?dimensionTypeId=1';
		        },
			}, 
			competencyName: { 
				title: 'Competency' ,
				inputTitle: 'Competency',
				width: "10%",
				edit : false,
				create : false,
			},
    		extractorTypeId: { 
				title: 'Type' , 
				inputTitle: 'Type <font color="#efd125" size="4px">*</font>',
				width: "10%",
				edit : false,
				list : false,
   				options:function(data){
		        	return 'extractor.type.list.options';
		        },
			},
			extractorTypeName: { 
				title: 'Type' , 
				inputTitle: 'Type <font color="#efd125" size="4px">*</font>',
				width: "10%",
				edit : false,
				create : false,
			},
			fileLocation: { 
				title: 'Location' , 
				create : false,
				width: "10%",    				
			},
			description: { 
				title: 'Description' , 
				width: "10%",    				
			},
			cronExpression: { 
   				title: 'Cron Expression' , 
   				edit : false,
   				list : false,
   				create : false
    		},
    		startDate: { 
				title: 'Start Date' , 
				create : false,
				width: "10%", 
				edit : false
			},
			endDate: { 
				title: 'End Date' , 
				create : false,
				width: "10%", 
				edit : false
			},
			lastExecuted: { 
				title: 'Last Executed' , 
				create : false,
				width: "10%", 
				edit : false
			},
			nextExecution: { 
				title: 'Next Execution' , 
				create : false,
				width: "10%", 
				edit : false
			},
			createdDate: { 
   				title: 'Created Date' , 
   				edit : false,
   				list : false,
   				create : false
    		},
    		updatedDate: { 
   				title: 'Updated Date' , 
   				edit : false,
   				list : false,
   				create : false
    		},
    		scheduleMapped: { 
              	title: 'Schedule',
              	width: "5%",
              	edit: true,
              	create: false,
              
         	 	display: function (data) { 
         		//Create an image that will be used to open child table 
         			var $img = $('<i class="fa fa-clock-o" title="Schedule Data Extractor"></i>');
         			//Open child table when user clicks the image 
         			var dataExtractorId = data.record.id;
         			var dataExtractorType = "Data Extractor";
         			var titleSchedule = "Schedule Data Extractor for - "+data.record.extractorType;
                	var subTitleSchedule = data.record.customerName+" >> "+data.record.competencyName+" >> "+data.record.productName;
         			$img.click(function () {
         				scheduleUsingCronGen(titleSchedule, subTitleSchedule, dataExtractorId, dataExtractorType);
         			});
         			return $img; 
             	} 
     	  	},
     	  	runJobNow: { 
              	title: 'Execute',
              	width: "5%",
              	edit: true,
              	create: false,
              
         	 	display: function (data) { 
         			var dataExtractorId = data.record.id;
         			var dataExtractorType = "Data Extractor";
         			var $img = $('<img src="css/images/execute_metro.png" title="Execute Data Extractor" class="exe">');
         			$img.click(function () {
         				runDataExtractor(dataExtractorId, dataExtractorType);
         			});
         			return $img; 
             	} 
     	  	},
     	  	attachment: { 
				title: '', 
				list: true,
				edit: false,
				create: false,
				width: "10%",
				display:function (data) {	        		
	           		//Create an image that will be used to open child table 
					var $img = $('<div><img src="css/images/attachment.png" title="Attachment" style="width: 18px;height: 18px;position: absolute;" /><span style="margin-left: 15px;">['+data.record.attachmentCount+']</span></div>'); 
	       			$img.click(function () {          
	       				isViewAttachment = false;
	       				var jsonObj={"Title":"Attachments for DataExtractor",			          
	       					"SubTitle": 'DataExtractor : ['+data.record.id+'] '+data.record.jobName,
	    	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+data.record.productId+'&entityTypeId=54&entityInstanceId='+data.record.id,
	    	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+data.record.productId+'&entityTypeId=54&entityInstanceId='+data.record.id+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
	    	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
	    	    			"updateURL": 'update.attachment.for.entity.or.instance',
	    	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=54',
	    	    			"multipleUpload":true,
	    	    			
	    	    		};	 
		        		Attachments.init(jsonObj);
	       		  });
	       			return $img;
	        	}				
			}, 
			commentsExtractor:{
				title : '',
				list : true,
				create : false,
				edit : false,
				width: "5%",
				display:function (data) { 
					//Create an image for test script popup 
					var $img = $('<i class="fa fa-comments" title="Comments"></i>');
						$img.click(function () {
							var entityTypeIdComments = 65;
							var entityNameComments = "DataExtractorSchedule";
							listComments(entityTypeIdComments, entityNameComments, data.record.id, data.record.extractorName, "extractorComments");
						});
					return $img;
					}		
			},
		},

		formSubmitting: function (event, data) {
			data.form.find('input[name="extractorName"]').addClass('validate[required], custom[minSize[3]], custom[maxSize[50]]');
			data.form.find('select[name="customerId"]').addClass('validate[required]');
			data.form.find('select[name="engagementId"]').addClass('validate[required]');
			//data.form.find('select[name="productId"]').addClass('validate[required]');
			data.form.find('select[name="extractorTypeId"]').addClass('validate[required]');
			data.form.validationEngine();
			
			var selectedCustomer = data.form.find('select[name="customerId"]').val();
			if(typeof selectedCustomer == 'undefined' || selectedCustomer == null || selectedCustomer == 0){
				$("#basicAlert").css("z-index", "100001");
				callAlert('Please select valid customer');
				return false;
			}
			
			var selectedEngagement = data.form.find('select[name="engagementId"]').val();
			if(typeof selectedEngagement == 'undefined' || selectedEngagement == null || selectedEngagement == 0){
				$("#basicAlert").css("z-index", "100001");
				callAlert('Please select valid engagement');
				return false;
			}
			
			return data.form.validationEngine('validate');
		}, 
		//Dispose validation logic when form is closed
		formClosed: function (event, data) {
		data.form.validationEngine('hide');
		data.form.validationEngine('detach');
		}	    		
     });	 
	 $('#jTableContainer').jtable('load');	
}*/

//BEGIN: ConvertDataTable - DataExtractor
var customerId = 1;
var engagementId = 1;
var dataExtractorDT_oTable='';
var editorDataExtractor='';
var optionsArr=[];
var optionsResultArr=[];
var optionsItemCounter=0;

function dataExtractorDataTable(){
	optionsItemCounter=0;
	optionsResultArr=[];
	optionsArr = [{id:"dataExtractorList", url:'data.source.extractor.customer.by.user.option.list'},
	              {id:"dataExtractorCustomer", url:'data.source.extractor.engagement.by.user.customer.option.list?customerId='+customerId},
	              {id:"dataExtractorEngagement", url:'data.source.extractor.product.by.user.customer.engagement.option.list?customerId='+customerId+'&engagementId='+engagementId},
	              {id:"dataExtractorDimension", url:'dimension.list.options?dimensionTypeId=1'},
	              {id:"dataExtractorTypeOptions", url:'extractor.type.list.options'},
	              ];
	dataExtractorTypeOptions_Container(optionsArr);
}
		
function dataExtractorTypeOptions_Container(urlArr){
	$.ajax( {
 	   "type": "POST",
        "url":  urlArr[optionsItemCounter].url,
        "dataType": "json",
         success: function (json) {		        	 
	         if(json.Result == "OK"){						 
				 if(json.Options.length>0){     		   
					   for(var i=0;i<json.Options.length;i++){
						   json.Options[i].label=json.Options[i].DisplayText;
						   json.Options[i].value=json.Options[i].Value;
					   }			   
				   }else{
					  json.Options=[];
				   }     	   
				   optionsResultArr.push(json.Options);						 
	         }
	         optionsItemCounter++;	
			 if(optionsItemCounter<optionsArr.length){
				 dataExtractorTypeOptions_Container(optionsArr);
			 }else{
				dataExtractorDataTableInit(urlToListDataExtractorBasedOnStatus);	
			 }					 
         },
         error: function (data) {
         	console.log('error in ajax call : '+url);
			 optionsItemCounter++;
         },
         complete: function(data){
         	//console.log('Completed');
         },	            
   	});
}

function dataExtractorDataTableInit(urlToListDataExtractorBasedOnStatus){
	var url= urlToListDataExtractorBasedOnStatus +'&jtStartIndex=0&jtPageSize=10';
	 var jsonObj={"Title":" Dimension Management",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"componentUsageTitle",
	};
	dataExtractorDataTableContainer.init(jsonObj);
}

var dataExtractorDataTableContainer = function() {
 	var initialise = function(jsonObj){
		assignDataExtractorDataTableValues(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignDataExtractorDataTableValues(jsonObj){
	openLoaderIcon();			
	 $.ajax({
		  type: "POST",
		  url: jsonObj.url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}					
			jsonObj.data = data;
			dataExtractorDT_Container(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function dataExtractorDataTableHeader(){
	var childDivString ='<table id="dataExtractor_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Name</th>'+
			'<th>Customer</th>'+
			'<th>Engagement</th>'+
			'<th>Product</th>'+
			'<th>Competency</th>'+
			'<th>Type</th>'+
			'<th>Location</th>'+
			'<th>Description</th>'+
			'<th>Start Date</th>'+
			'<th>End Date</th>'+
			'<th>Last Executed</th>'+
			'<th>Next Execution</th>'+
			'<th>Schedule</th>'+
			'<th>Execute</th>'+
			'<th>Attachments</th>'+
			'<th>Comments</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function dataExtractorDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForDataExtractor").children().length>0) {
			$("#dataTableContainerForDataExtractor").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = dataExtractorDataTableHeader(); 			 
	$("#dataTableContainerForDataExtractor").append(childDivString);
	
	editorDataExtractor = new $.fn.dataTable.Editor( {
	    "table": "#dataExtractor_dataTable",
		ajax: "data.source.extractor.add",
		ajaxUrl: "data.source.extractor.update",
		idSrc:  "customerId",
		i18n: {
	        create: {
	            title:  "Create a new Dimension Management",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "Name",
            name: "extractorName",
         },{
            label: "Customer Name",
            name: "customerId",
            options: optionsResultArr[0],
            "type":"select"
         },{
             label: "Engagement Name",
             name: "engagementId",
             options: optionsResultArr[1],
             "type":"select"
         },{
             label: "Product Name",
             name: "productId",
             options: optionsResultArr[2],
             "type":"select"
         },{
             label: "Competency Name",
             name: "competencyId",
             options: optionsResultArr[3],
             "type":"select"
        },{
            label: "Type",
            name: "extractorTypeId",
            options: optionsResultArr[4],
            "type":"select"
        },{
            label: "Location",
            name: "fileLocation",
            "type":"hidden"
        },{
            label: "Description",
            name: "description",
        },{
            label: "Start Date",
            name: "startDate",
            "type":"hidden"
        },{
            label: "End Date",
            name: "endDate",
            "type":"hidden"
        },{
            label: "Last Executed",
            name: "lastExecuted",
            "type":"hidden"
        },{
            label: "Next Execution",
            name: "nextExecution",
            "type":"hidden"
        }        
    ]
	});
	
	dataExtractorDT_oTable = $("#dataExtractor_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "90%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [12,13,14,15]; // search column TextBox Invisible Column position
	     		  $('#dataExtractor_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
	    	    	    var i=$(this).index();
	    	    	    var flag=false;
	    	    	    for(var j=0;j<searchcolumnVisibleIndex.length;j++){
	    	    	    	if(i == searchcolumnVisibleIndex[j]){
	    	    	    		flag=true;
	    	    	    		break;
	    	    	    	}
	    	    	    }
	    	    	    if(!flag){
	    	    	    	$(this).html('');
	    	    	    	$(this).append( '<input type="text" name="'+data.aoColumns[i].mData+'" value="" style="width:100%" class="search_init" />');
	    	    	    }
		       	   });	     		
		     	  reInitializeDataExtractorDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorDataExtractor },								 
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Dimension Management',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Dimension Management',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
			    		//    'colvis'
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
           	{ mData: "extractorName", className: 'editable', sWidth: '15%' },	
           	{ mData: "customerId",className: 'disableEditInline',
              	mRender: function (data, type, full) {
	 				data = optionsValueHandler(editorDataExtractor, 'customerId', full.customerId);
                    return data;
                },
        	},
           	{ mData: "engagementId",className: 'disableEditInline',
               	mRender: function (data, type, full) {
	 				data = optionsValueHandler(editorDataExtractor, 'engagementId', full.engagementId);
                    return data;
                },
           	},
           	{ mData: "productId",className: 'disableEditInline',
             	mRender: function (data, type, full) {
	 				data = optionsValueHandler(editorDataExtractor, 'productId', full.productId);
                   return data;
               },
       		},
          	{ mData: "competencyId",className: 'disableEditInline',
              	mRender: function (data, type, full) {
	 				data = optionsValueHandler(editorDataExtractor, 'competencyId', full.competencyId);
                   return data;
               },
          	},
          	{ mData: "extractorTypeId",className: 'disableEditInline',
            	mRender: function (data, type, full) {
	 				data = optionsValueHandler(editorDataExtractor, 'extractorTypeId', full.extractorTypeId);
                  return data;
            	},
      		},
            { mData: "fileLocation", className: 'disableEditInline', sWidth: '15%' },
            { mData: "description", className: 'disableEditInline', sWidth: '15%' },
            { mData: "startDate", className: 'disableEditInline', sWidth: '15%' },
            { mData: "endDate", className: 'disableEditInline', sWidth: '15%' },
            { mData: "lastExecuted", className: 'disableEditInline', sWidth: '15%' },
            { mData: "nextExecution", className: 'disableEditInline', sWidth: '15%' },
            { mData: null,className: 'disableEditInline',			 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<i class="fa fa-clock-o scheduleImg" title="Schedule Data Extractor"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
            { mData: null,className: 'disableEditInline',			 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<img class="executeImg" src="css/images/execute_metro.png" title="Execute Data Extractor"></img></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
            { mData: null,className: 'disableEditInline',				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<img src="css/images/attachment.png" class="attachmentImg" title="Attachment"></img></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
            { mData: null,className: 'disableEditInline',				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<i class="fa fa-comments commentsImg" title="Comments"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
       ],       
       rowCallback: function ( row, data ) {
    	  // $('input.editorDataExtractor-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#dataExtractor_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorDataExtractor.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#dataExtractor_dataTable tbody').on('click', 'td .scheduleImg', function () {
		var tr = $(this).closest('tr');
    	var row = dataExtractorDT_oTable.DataTable().row(tr);
		var dataExtractorId = row.data().id;
		var dataExtractorType = "Data Extractor";
		var titleSchedule = "Schedule Data Extractor for - "+row.data().extractorType;
    	var subTitleSchedule = row.data().customerName+" >> "+row.data().competencyName+" >> "+row.data().productName;
		scheduleUsingCronGen(titleSchedule, subTitleSchedule, dataExtractorId, dataExtractorType);
	});
	
	$('#dataExtractor_dataTable tbody').on('click', 'td .executeImg', function () {
		var tr = $(this).closest('tr');
    	var row = dataExtractorDT_oTable.DataTable().row(tr);
		var dataExtractorId = row.data().id;
		var dataExtractorType = "Data Extractor";
		runDataExtractor(dataExtractorId, dataExtractorType);
	});
	
	$('#dataExtractor_dataTable tbody').on('click', 'td .attachmentImg', function () {
		var tr = $(this).closest('tr');
    	var row = dataExtractorDT_oTable.DataTable().row(tr);
		isViewAttachment = false;
		var jsonObj={"Title":"Attachments for DataExtractor",			          
				"SubTitle": 'DataExtractor : ['+row.data().id+'] '+row.data().jobName,
			"listURL": 'attachment.for.entity.or.instance.list?productId='+row.data().productId+'&entityTypeId=54&entityInstanceId='+row.data().id,
			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+row.data().productId+'&entityTypeId=54&entityInstanceId='+row.data().id+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
			"deleteURL": 'delete.attachment.for.entity.or.instance',
			"updateURL": 'update.attachment.for.entity.or.instance',
			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=54',
			"multipleUpload":true,
			
		};	 
		Attachments.init(jsonObj);
		
	});
	
	$('#dataExtractor_dataTable tbody').on('click', 'td .commentsImg', function () {
		var tr = $(this).closest('tr');
    	var row = dataExtractorDT_oTable.DataTable().row(tr);
		var entityTypeIdComments = 65;
		var entityNameComments = "DataExtractorSchedule";
		listComments(entityTypeIdComments, entityNameComments, row.data().id, row.data().extractorName, "extractorComments");
	});
	
	$("#dataExtractor_dataTable_length").css('margin-top','8px');
	$("#dataExtractor_dataTable_length").css('padding-left','35px');		

	dataExtractorDT_oTable.DataTable().columns().every( function () {
	    var that = this;
	    $('input', this.footer() ).on( 'keyup change', function () {
	        if ( that.search() !== this.value ) {
	            that
	            	.search( this.value, true, false )
	                .draw();
	        }
	    } );
	} );

}

var clearTimeoutDataExtractorDT='';
function reInitializeDataExtractorDT(){
	clearTimeoutDataExtractorDT = setTimeout(function(){				
		dataExtractorDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDataExtractorDT);
	},200);
}

function fullScreenHandlerDTDataExtractor(){
	
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
		$('#testFacMode').css('max-height',displaytestFaceModeResponsive(window.innerWidth));
		
		dataExtractorDTFullScreenHandler(true);
		reInitializeDataExtractorDT();
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
		$('#testFacMode').css('max-height','');
		
		reInitializeDataExtractorDT();				
		dataExtractorDTFullScreenHandler(false);			
	}
}

function dataExtractorDTFullScreenHandler(flag){
	if(flag){
		reInitializeDataExtractorDT();
		$("#dataExtractor_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
	}else{
		reInitializeDataExtractorDT();
		$("#dataExtractor_dataTable_wrapper .dataTables_scrollBody").css('max-height','450px');
	}
}

function displaytestFaceModeResponsive(widthValue){
	var resultWidth="";
	if(widthValue<768){
		resultWidth = 200;			
	}else if(widthValue<992){
		resultWidth = 300;
	}else if(widthValue<1200){
		resultWidth = 400;
	}else if(widthValue<1500){
		resultWidth = 500;			
	}else if(widthValue<1600){
		resultWidth = 600;
	}else if(widthValue<1800){
		resultWidth = 700;
	}else if(widthValue<2050){
		resultWidth = 750;
	}else if(widthValue<2400){
		resultWidth = 850;
	}else if(widthValue<3000){
		resultWidth = 1100;
	}else if(widthValue<4000){
		resultWidth = 1300;
	}else if(widthValue<5000){
		resultWidth = 1500;
	}
	
	return resultWidth+'px';
}
//END: ConvertDataTable - DataExtractor

function listComments(entityTypeId, entityName, instanceId, instanceName, componentUsageTitle){
	var url='comments.for.entity.or.instance.list?productId=0&entityTypeId='+entityTypeId+'&entityInstanceId='+instanceId+'&jtStartIndex=0&jtPageSize=10000';
	var jsonObj={"Title":"Comments on "+entityName+ ": " +instanceName,
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,
			"componentUsageTitle":componentUsageTitle,			
			"entityTypeId":entityTypeId,
			"entityInstanceId":instanceId,
	};
	CommentsMetronicsUI.init(jsonObj);
}

function scheduleUsingCronGen(title, subTitle, testRunPlanID, rowType){	
	var	jsonTestCaseTabObj={"Title": title,	
							"SubTitle": subTitle,
							"rowID": testRunPlanID,
							"rowType": rowType,
		};
	
	 Scheduling.init(jsonTestCaseTabObj);	
}

function runDataExtractor(dataExtractorId, dataExtractorType){
	openLoaderIcon();
	$.ajax({
		type:"POST",
		contentType: "application/json; charset=utf-8",
		url:"entity.schedule.run.now?entityId="+dataExtractorId+'&entityType='+dataExtractorType,
		dataType : 'json',
	    sucess:function(data){
	    	closeLoaderIcon();
	    },error:function(data){
	    	closeLoaderIcon();
	    	console.log("error in scheduling:"+data);
	    	callAlert("Error in scheduling - "+data);
	    },complete:function(data){
	    	closeLoaderIcon();
	    	if(data.responseJSON.Result == 'OK' &&  typeof data.responseJSON.Record != 'undefined' && typeof data.responseJSON.Record.message != 'undefined' && data.responseJSON.Record.message != ''){
	    		callAlert(data.responseJSON.Record.message);
	    	}else if(data.responseJSON.Result == 'ERROR' || data.responseJSON.Result == 'Error'){
	    		callAlert(data.responseJSON.Message);
	    	}	
	    }		
	});
}
</script>						
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>