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

<link href="js/datatable/Editor-1.5.6/css/dataTables.editor.css" rel="stylesheet" type="text/css"></link>
<link href="js/datatable/Editor-1.5.6/css/editor.dataTables.css" rel="stylesheet" type="text/css"></link>
<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>

<!-- END THEME STYLES -->
<style>
.logo{
   margin-left: 45px;
}
#filter > .col-md-4 >.select2me{
	 height: 28px !important; 
     margin-top: 3px; 
     width: 101px !important ;
     font-size: 13px;
     padding-top: 3px;
}
#filter > .col-md-4 >.select2me a{
	 height: 28px !important; 
     padding-top: 0px; 
     margin-top: 0px; 
}
.ui-dialog-buttonset{
	width: 230px;
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
<div><%@include file="singleDataTableContainer.jsp"%></div>
<div><%@include file="singleJtableContainer.jsp" %></div>

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
								<span id="manageCustomerLabel"  class="caption-subject theme-font bold uppercase">MANAGE CUSTOMERS</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
								<span class="caption-helper hide">weekly stats...</span>
							</div>
							<div class="actions" style="padding-top: 4px;padding-left: 5px;">
								<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" 
									onClick="fullScreenHandlerDTCustomerManagement()" data-original-title="" title="FullScreen"></a>
							</div>
						</div>
						<div class="portlet-body">
							<div class="row list-separated" style="margin-top:0px;">
								    <div id="main" style="float:left; padding-top:0px; width:100%;">								  
								    <div id="hdnDiv"> </div>
								    			<input type="hidden" id="currentWorkPackage" value="" ></input>
												<input type="hidden" id="currentReservationShift" value="" ></input>
												<input type="hidden" id="currentReservationDate" value="" ></input>
												<input type="hidden" id="currentResourceDemandCount" value="" ></input>
												<input type="hidden" id="currentAvailabilityType" value="" ></input>
												<input type="hidden" id="currentViewType" value="" ></input>
								    <div id="hidden"></div>
						         	<div id="selectedUserDetailshidden"></div>							       
								       
								        <!-- ContainerBox -->
								       <!-- <div class="jScrollContainer"> -->
								      <div style="clear:both;position: relative"> 
								      	<div id="filter" class="jScrollTitleContainer" style="float:right;position: absolute;Z-index: 10;right:140px;top:4px">
											<div id="status_dd" class="col-md-4">
												<select class="form-control input-small select2me" id="status_ul">
													<option value="2" ><a href="#">ALL</a></option>
													<option value="1" selected><a href="#">Active</a></option>
													<option value="0" ><a href="#">Inactive</a></option>
												</select>
											</div>
    									</div>
								      	<div id="jTableContainer" class ="jTableContainerFullScreen"></div>
									  </div>
									  <div id="dataTableContainerForCustomerManagement"></div>
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
<!-- END FOOTER -->
<!-- POPUP -->
<div id="div_PopupLogo" class="modal " tabindex="-1" aria-hidden="true" >
	<div class="modal-dialog modal-md"  style="width:20%">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close"  aria-hidden="true"></button>
				<h4 class="modal-title">Customer Logo</h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:300px" data-always-visible="1" data-rail-visible1="1">
				    <div class="row">
							<div class="col-lg-4">
								<div class="form-group margin-top-10">
									<div id="image-preview"></div>
									<br />
									<input type="button"  class="btn blue logo" id="trigUploadLogo" value="Upload Logo" />
									<input id="uploadLogo" type="file" name="uploadLogo" style="visibility:hidden" onchange="submitLogo();" />
								</div>
							</div>
						</div>				
				    			
				</div>
			</div>
		</div>
	</div>
</div>	
<div id="div_PopupBackground"></div>
<!-- END POPUP -->
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

<script type="text/javascript">
var urlToListCustomerBasedOnStatus ='';
custIdVal="";
$(document).on('click', '#trigUploadLogo', function(e){
	if($(e.target).attr('id') == this.id){
	  $("#uploadLogo").trigger("click");
	}
});
jQuery(document).ready(function() {	
   urlToListCustomerBasedOnStatus = 'administration.customer.list.bystatus?status=1';
   //loadCustomerList(urlToListCustomerBasedOnStatus); //DT - Customer
   customerManagementDataTable(urlToListCustomerBasedOnStatus); //DT - Customer
   $("#menuList li:first-child").eq(0).remove();
   setBreadCrumb("Manage Customers");
   $(document).on('change','#status_ul', function() {
		var id = $("#status_ul").find('option:selected').val();				
	    urlToListCustomerBasedOnStatus = 'administration.customer.list.bystatus?status='+id;		
	    //loadCustomerList(urlToListCustomerBasedOnStatus);	 //DT - Customer  
	    customerManagementDataTable(urlToListCustomerBasedOnStatus); //DT - Customer
	 });
});

/*function loadCustomerList(urlToListCustomerBasedOnStatus){	
	try{
		if($('#jTableContainer').length > 0){
			$('#jTableContainer').jtable("destroy");
		}
	}catch(e){
		
	}
	$('#jTableContainer').jtable({

      	title: 'Add/Edit Customer(s)',
      	selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},
		recordsLoaded: function(event, data) {
        	 $(".jtable-edit-command-button").prop("disabled", true);
         },
		actions: { 
			//listAction: 'administration.customer.list',
			listAction: urlToListCustomerBasedOnStatus,
			createAction: 'administration.customer.add',
			//updateAction: 'administration.customer.update',
			editinlineAction: 'administration.customer.update',
			//deleteAction: 'administration.product.build.delete', 
		
				}, 	 				
		fields: { 
			customerId: { 
				type: 'hidden', 
				key: true, 
				create: false, 
               edit: false, 
				list: false
			},
			
			customerName: { 
				title: 'Name',
				inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
				width: "20%"
						},
			description: { 
				title: 'Description' ,
				inputTitle: 'Description <font color="#efd125" size="4px"></font>',
				width: "35%"          	
						},  
			customerRefId: { 
    				title: 'Reference ID' , 
    				inputTitle: 'Reference ID <font color="#efd125" size="4px">*</font>',
    				edit: true, 
					list: true,
    				width: "35%",    				
    		},
    		customerLogo: { 
				title: 'Logo' , 
				inputTitle: 'Logo <font color="#efd125" size="4px">*</font>',
				edit: false, 
				create: false, 
				list: true,
				width: "35%",
				display: function (data) {
					return $('<a style="color: #0000FF;" href=javascript:togetLogo('+data.record.customerId+');>'+data.record.customerName+'</a>');
               	}
			},
			 status: {
				 title: 'Status' ,
				 inputTitle: 'Status <font color="#efd125" size="4px">*</font>',
				 width: "10%",  
				 list:true,
				 edit:true,
				 create:false,
				 type : 'checkbox',
				 values: {'0' : 'No','1' : 'Yes'},
		    		defaultValue: '1'
		    },
			auditionHistory:{
				title : 'Audit History',
				list : true,
				create : false,
				edit : false,
				width: "10%",
				display:function (data) { 
					//Create an image for test script popup 
					var $img = $('<i class="fa fa-search-plus" title="Audit History"></i>');
					//Open Testscript popup  
					$img.click(function () {
						listCustomerAuditHistory(data.record.customerId);
					});
					return $img;
				}
			}, 
			commentsCustomer:{
				title : '',
				list : true,
				create : false,
				edit : false,
				width: "5%",
				display:function (data) { 
					//Create an image for test script popup 
					var $img = $('<i class="fa fa-comments" title="Comments"></i>');
						$img.click(function () {
							var entityTypeIdComments = 16;
							var entityNameComments = "Customer";
							listComments(entityTypeIdComments, entityNameComments, data.record.customerId, data.record.customerName, "customerComments");
						});
						return $img;
					}		
			},
	
		},

		formSubmitting: function (event, data) {
		data.form.find('input[name="customerName"]').addClass('validate[required], custom[minSize[3]], custom[maxSize[25]]');
		data.form.find('input[name="customerRefId"]').addClass('validate[required], custom[minSize[3]], custom[maxSize[15]]'); 
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
}*/

//BEGIN: ConvertDataTable - CustomerManagement
var customerManagementDT_oTable='';
var editorCustomerManagement='';
function customerManagementDataTable(urlToListCustomerBasedOnStatus){
	var url= urlToListCustomerBasedOnStatus +'&jtStartIndex=0&jtPageSize=10';
	 var jsonObj={"Title":" Customer Management",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,				
			"componentUsageTitle":"componentUsageTitle",
	};
	customerManagementDataTableContainer.init(jsonObj);
}

var customerManagementDataTableContainer = function() {
 	var initialise = function(jsonObj){
		assignCustomerManagementDataTableValues(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignCustomerManagementDataTableValues(jsonObj){
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
			customerManagementDT_Container(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}

function customerManagementDataTableHeader(){
	var childDivString ='<table id="customerManagement_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Customer Name</th>'+
			'<th>Description</th>'+
			'<th>Customer Reference ID</th>'+
			'<th>Logo</th>'+
			'<th>Status</th>'+
			'<th>Audit History</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function customerManagementDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForCustomerManagement").children().length>0) {
			$("#dataTableContainerForCustomerManagement").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = customerManagementDataTableHeader(); 			 
	$("#dataTableContainerForCustomerManagement").append(childDivString);
	
	editorCustomerManagement = new $.fn.dataTable.Editor( {
	    "table": "#customerManagement_dataTable",
		ajax: "administration.customer.add",
		ajaxUrl: "administration.customer.update",
		idSrc:  "customerId",
		i18n: {
	        create: {
	            title:  "Create a new Customer Management",
	            submit: "Create",
	        }
	    },
		fields: [
		{
            label: "Customer Name*",
            name: "customerName",
         },{
            label: "Description",
            name: "description",
        },{
            label: "Reference ID*",
            name: "customerRefId",
        },{
            label: "customerId",
            name: "customerId",
        },{
            label: "Status",
            name: "status", 
            "type": "hidden",
        }        
    ]
	});
	
	customerManagementDT_oTable = $("#customerManagement_dataTable").dataTable( {				 	
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
		    	  var searchcolumnVisibleIndex = [4,5,6]; // search column TextBox Invisible Column position
	     		  $('#customerManagement_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeCustomerManagementDT();
			   },  
		   buttons: [
						{ extend: "create", editor: editorCustomerManagement },								 
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Customer Management',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Customer Management',
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
           { mData: "customerName", className: 'editable', sWidth: '15%' },	
           { mData: "description", className: 'editable', sWidth: '12%' },		
           { mData: "customerRefId", className: 'editable', sWidth: '15%' },
           { mData: "customerId",
               mRender: function (data, type, full) {
            	 	data =	'<a style="color: #0000FF;" href=javascript:togetLogo('+full.customerId+');>'+full.customerName+'</a>';
                    return data;
                 },
           },
           { mData: null,
              mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editorCustomerManagement-active">';
                    }
                    return data;
                },
                className: "dt-body-center"
            },
            { mData: null,				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<i class="fa fa-search-plus auditHistoryImg" title="Audit History"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
            { mData: null,				 
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
    	   $('input.editorCustomerManagement-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 
	
	
	// Activate an inline edit on click of a table cell
	$('#customerManagement_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorCustomerManagement.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#customerManagement_dataTable tbody').on('click', 'td .auditHistoryImg', function () {
		var tr = $(this).closest('tr');
    	var row = customerManagementDT_oTable.DataTable().row(tr);
		listCustomerAuditHistory(row.data().customerId);
	});
	
	$('#customerManagement_dataTable tbody').on('click', 'td .commentsImg', function () {
		var entityTypeIdComments = 16;
		var entityNameComments = "Customer";
		var tr = $(this).closest('tr');
    	var row = customerManagementDT_oTable.DataTable().row(tr);
		listComments(entityTypeIdComments, entityNameComments, row.data().customerId, row.data().customerName, "customerComments");
	});
	
	$('#customerManagement_dataTable').on( 'change', 'input.editorCustomerManagement-active', function () {
		editorCustomerManagement
            .edit( $(this).closest('tr'), false )
            .set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
            .submit();
	});
	
	$("#customerManagement_dataTable_length").css('margin-top','8px');
	$("#customerManagement_dataTable_length").css('padding-left','35px');		

	customerManagementDT_oTable.DataTable().columns().every( function () {
	    var that = this;
	    $('input', this.footer() ).on( 'keyup change', function () {
	        if ( that.search() !== this.value ) {
	            that
	            	.search( this.value, true, false )
	                .draw();
	        }
	    } );
	} );
	
	editorCustomerManagement.on( 'preSubmit', function ( e, o, action ) {
        if ( action !== 'remove' ) {
        	var validationArr = ['customerName','customerRefId'];
        	var str;
        	for(var i=0;i<validationArr.length;i++){
	            str = this.field(validationArr[i]);
	            if ( ! str.isMultiValue() ) {
	                if ( str.val() ) {
	                	if(validationArr[i] == "customerName" && str.val().length<3){
	                		str.error("Please enter minimum 3 letters");
	                	}
                	}else{
	                	if(validationArr[i] == "customerName")
	                		str.error("Please enter Customer Name");
	                	if(validationArr[i] == "customerRefId")
	                		str.error("Please enter Reference ID");	                	
                	}
	            }
        	}

            // If any error was reported, cancel the submission so it can be corrected
            if ( this.inError() ) {
                return false;
            }
        }
    } );

}

var clearTimeoutCustomerManagementDT='';
function reInitializeCustomerManagementDT(){
	clearTimeoutCustomerManagementDT = setTimeout(function(){				
		customerManagementDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutCustomerManagementDT);
	},200);
}

function fullScreenHandlerDTCustomerManagement(){
	
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);	
		$('#testFacMode').css('max-height',displaytestFaceModeResponsive(window.innerWidth));
		
		customerManagementDTFullScreenHandler(true);
		reInitializeCustomerManagementDT();
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');
		$('#testFacMode').css('max-height','');
		
		reInitializeCustomerManagementDT();				
		customerManagementDTFullScreenHandler(false);			
	}
}

function customerManagementDTFullScreenHandler(flag){
	if(flag){
		reInitializeCustomerManagementDT();
		$("#customerManagement_dataTable_wrapper .dataTables_scrollBody").css('max-height','240px');
	}else{
		reInitializeCustomerManagementDT();
		$("#customerManagement_dataTable_wrapper .dataTables_scrollBody").css('max-height','450px');
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
//END: ConvertDataTable - CustomerManagement
		
function listCustomerAuditHistory(customerId){
	//clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=Customer&sourceEntityId='+customerId+'&jtStartIndex=0&jtPageSize=1000';
	//var url='administration.event.list?sourceEntityType=Customer&sourceEntityId='+customerId;
	var jsonObj={"Title":"Customer Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":1000,
			"componentUsageTitle":"customerAudit",
	};
	SingleDataTableContainer.init(jsonObj);
	//SingleJtableContainer.init(jsonObj);
}
function togetLogo(custId) {
	custIdVal = custId;
	$("#image-preview").empty();
	var urlMapping = 'customer.list.by.customerid?customerId='+custId;
	 $.ajax({
        type: "POST",
   		contentType: "application/json; charset=utf-8",
         url : urlMapping,
         dataType : 'json',
         success : function(data) {
			 var result=data.Records;
			 var root = "/Logo/";
        	 $.each(result, function(i,item){
        		var imagePath=root+item.imageURI;
        		$.ajax({
					  url: root+item.imageURI, //or your url
					  success: function(data){
						$("#image-preview").append("<img id='uploadPreview1' src='"+imagePath+"' width='200px' height='200px'/>");
					  },
					  error: function(data){
						$("#image-preview").append("<img id='uploadPreview1' src='css/images/noimage.jpg' width='200px' height='200px'/>");
					  },
				});
        		$("#div_PopupLogo").modal();
        	 });
         },
  	}); 
}

function submitLogo(){
	 var fileContent = document.getElementById("uploadLogo").files[0];
	 var formdata = new FormData();
	 formdata.append("uploadLogo", fileContent);
	 $.ajax({
		    url: 'customer.logo.update?customerId='+custIdVal,
		    method: 'POST',
		    contentType: false,
		    data: formdata,
		    dataType:'json',
		    processData: false,
		    success : function(data) {
		    	if(data.Result=="ERROR"){
		    		callAlert(data.Message);
		    		return false;
		    	}
		    	location.reload(); 
		    },
		});	  
}

function listComments(entityTypeId, entityName, instanceId, instanceName, componentUsageTitle){

	var url='comments.for.entity.or.instance.list?productId=0&entityTypeId='+entityTypeId+'&entityInstanceId='+instanceId+'&jtStartIndex=0&jtPageSize=10000';
	//var instanceId = row.data().productId;
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
</script>
<div><%@include file="comments.jsp"%></div>					
<!-- END JAVASCRIPTS -->
<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
</body>
<!-- END BODY -->
</html>