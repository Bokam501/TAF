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
	<link href="js/jtable/themes/metro/darkgray/jtable.min.css?4884491531235" rel="stylesheet" type="text/css" />
	<link href="js/Scripts/validationEngine/validationEngine.jquery.css" rel="stylesheet" type="text/css" />
	<link href="css/customStyle.css" rel="stylesheet" type="text/css">
	<div><jsp:include page="dataTableHeader.jsp"></jsp:include></div>
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
			 <div><%@include file="menu.jsp" %></div>							
 			<!-- END MEGA MENU -->
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->

<!-- BEGIN CONTAINER -->
<div class="page-container">
<div><%@include file="treeStructureSidebar.jsp" %></div>
<div><%@include file="postHeader.jsp" %></div>
<div><%@include file="singleDataTableContainer.jsp" %></div>

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
								<span class="caption-subject theme-font bold uppercase">Devices</span>&nbsp;<span id="headerTitle" class="caption-subject theme-font"></span>
							</div>
						</div>
						<div class="portlet-body form">
							<div id="hidden"></div>
							<div class="row" style="padding-bottom:15px">
								<div class="col-md-2">
									<button type="button" id="save" class="btn green-haze" onclick="addGenericDevicesPopUp()">Add New Devices</button> </td>									
								</div>
								<div class="col-md-4">
									<div class="col-md-3" style="padding-right:0px;margin-top:5px;"><label>Status :</label></div>
									<div id="devstatus" class="radio-toolbar col-md-9" style="padding-left:0px">	
										<div class=" btn-group" data-toggle="buttons" style="width:100%">
											<label class="btn darkblue active"  data-status="-1" onclick="filterAllDeviceTiles(this)">
											<input type="radio" class="toggle"  >All</label>
											<label class="btn darkblue" data-status="1" onclick="filterAllDeviceTiles(this)">
											<input type="radio" class="toggle"  >Active</label>
											<label class="btn darkblue"  data-status="0" onclick="filterAllDeviceTiles(this)">
											<input type="radio" class="toggle"  >Inactive</label>
										</div>
									</div>
								</div>
								<div class="col-md-6" >
									<div class="col-md-3" style="padding-right:0px;margin-top:5px;"><label>Type :</label></div>
									<div id="devtype" class="radio-toolbar col-md-9" style="padding-left:0px">	
										<div class=" btn-group" data-toggle="buttons" style="width:100%">
											<label class="btn darkblue active" data-type="1" onclick="filterAllDeviceTiles(this);">
											<input type="radio" class="toggle"  >Mobile</label>
											<label class="btn darkblue" data-type="2" onclick="filterAllDeviceTiles(this);">
											<input type="radio" class="toggle"  >Server</label>
											<label class="btn darkblue" data-type="3" onclick="filterAllDeviceTiles(this);">
											<input type="radio" class="toggle"  >Storage Drive</label>
										</div>
									</div>
								</div>															
							</div>
							<div class="deviceListResponsive">
								<div class="form-group col-lg-12">									
									<div id="tileContent" style="margin:10px; border-style: double; padding: 20px;"></div>
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
<!-- END PAGE CONTENT -->
</div>
<!-- END PAGE-CONTAINER -->

<!-- BEGIN FOOTER -->
<div><%@include file="footerlayout.jsp" %></div>			
<!-- END FOOTER -->

<div id="div_PopupMain" class="modal " tabindex="-1" aria-hidden="true" style="z-index:999">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="popupClose()" aria-hidden="true"></button>
				<h4 class="modal-title">Add Target Host / Device</h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:450px;overflow-y:scroll;" data-always-visible="1" data-rail-visible1="1">
					<form  class="form-horizontal" name="frmProfile" enctype="multipart/form-data" >
					<div class="form-body">
					<div class="form-group">
						<div class="col-md-6">
							<label for="input" class="control-label ">Target Type <font color="#efd125" size="4px"> * </font>:</label>
						</div>
						<div class="col-md-6">
							<div id="deviceType_dd" class="" " class="">
								<select class="form-control input-small  select2me" id="deviceType_ul" >
   									<option id="-1" value="-1" selected><a href="#">--Select--</a></option>	
   									<option id="4" value="4" ><a href="#">Server</a></option>
   									<option id="5" value="5"><a href="#">Mobile</a></option>
   									<option id="6" value="6"><a href="#">StorageDrive</a></option>
								</select>
							</div>
						</div>
					</div>			
					<div class="form-group">
						<div class="col-md-6">
							<label for="input" class="control-label ">Available Status <font color="#efd125" size="4px"> * </font>:</label>
						</div>
						<div class="col-md-6">
							<div id="availableStatus_dd" >
								<select class="form-control input-small select2me" id="availableStatus_ul">
									<option id=-1 value="-1" selected><a href="#">--Select--</a></option>
									<option id=1 value="1" ><a href="#">Yes</a></option>
									<option id=0 value="0" ><a href="#">No</a></option>
								</select>
							</div>
						</div>
					</div>	<!-- 				
					<div class="form-group">
						<div class="col-md-6">
							<label for="input" class="control-label ">Platform Type <font color="#efd125" size="4px"> * </font> :</label>
						</div>
						<div class="col-md-6">
							<div id="platformType_dd" class="" " class="">
								<select class="form-control input-small select2me" name="platformType_ul" id="platformType_ul" data-placeholder="Select...">
								</select>
							</div>
						</div>
					</div>
					<div class="form-group" id="platformTypeVersionheader">
						<div class="col-md-6">
							<label for="input" class="control-label ">Version :</label>
						</div>
						<div class="col-md-6">
							<input type="text" class="form-control" size="30" name="input"  id="platformTypeVersion"/>		
						</div>
					</div> -->
					
					<!-- 
					<div class="form-group">
						<div class="col-md-6">
							<label for="input" class="control-label ">Device Model Master <font color="#efd125" size="4px"> * </font>:</label>
						</div>
						<div class="col-md-6">
							<div id="deviceModelMaster_dd" class="" " class="">
								<select class="form-control input-small select2me" id="deviceModelMaster_ul" data-placeholder="Select...">
								</select>
							</div>
						</div>
					</div> -->		
					<!--  Host Div for Edit - for Mobile Device-->			
					<div class="form-group" id="terminalHostDiv" >
						<div class="col-md-6">
							<label for="input" class="control-label ">Terminal Host Id <font color="#efd125" size="4px"> * </font>:</label>
						</div>
						<div class="col-md-6">
							<div id="terminalhost_dd" class="" " class="">
								<select class="form-control input-small select2me" id="terminalhost_ul" data-placeholder="Select..."></select>
							</div>
						</div>											
					</div>					
					<div class="form-group" id="deviceNameNeeded">
							<div class="col-md-6">
								<label for="input" class=" control-label ">Device Name <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="devName"/>		
							</div>
						</div>
					<div class="row" id="mobileTypeDiv" style="display:none;padding-left:20px;">
						<h4>Fill Mobile Device Details :</h4><hr />						
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Description :</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="devDesc"/>		
							</div>
						</div>		
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Kernel Number :</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="kernelNumber"/>		
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Build Number :</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="buildNumber"/>		
							</div>
						</div>
						<div class="form-group">					
							<div class="col-md-6">
								<label for="input" class="control-label ">Device Make Master <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<div id="deviceModelMaster_dd" class="" " class="">
									<select class="form-control input-small select2me" id="deviceMakeMaster_ul" data-placeholder="Select...">
									</select>
								</div>
							</div>
						</div>	
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Device Model Master <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<div id="mobiledeviceModelMaster_dd" class="" " class="">
									<select class="form-control input-small select2me" id="mobiledeviceModelMaster_ul" data-placeholder="Select...">
									</select>
								</div>
							</div>
						</div>				
												
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Platform Type <font color="#efd125" size="4px"> * </font> :</label>
							</div>
							<div class="col-md-6">
								<div id="mobileplatformType_dd" class="" " class="">
									<select class="form-control input-small select2me" name="mobileplatformType_ul" id="mobileplatformType_ul" data-placeholder="Select...">
									</select>
								</div>
							</div>
						</div>
						<div class="form-group" id="mobileplatformTypeVersionheader">
							<div class="col-md-6">
								<label for="input" class="control-label ">Version <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input" onkeypress="floatvalidation(event)" id="mobileplatformTypeVersion"/>		
							</div>
						</div> 
						<div class="form-group" id="mobileTypeDiv">
							<div class="col-md-6">
								<label for="input" class="control-label ">UDID <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="startRemarks"/>		
							</div>
						</div>
						<div class="form-group" id="screenResolutionX">
							<div class="col-md-6">
								<label for="input" class="control-label ">Screen Resolution X <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="screenResolutionXVal"/>		
							</div>
						</div>
						<div class="form-group" id="screenResolutionY">
							<div class="col-md-6">
								<label for="input" class="control-label ">Screen Resolution Y <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="screenResolutionYVal"/>		
							</div>
						</div>
						<div id="mobileplatformTypeAudit">
							<div class="form-group" ">
								<div class="col-md-6">
									<label for="input" class="control-label ">Audit History <font color="#efd125" size="4px"></font>:</label>
								</div>
								<div class="col-md-6">
									<i class="fa fa-search-plus showHandCursor" title="Audit History" onclick="listGenericAuditHistory(''+genericDevId+'','Device','deviceAudit');"></i>
								</div>																										
							</div>
						</div>
					</div>
					<div class="row" id="serverTypeDiv" style="display:none;padding-left:20px;">
						<h4>Fill Target Host Details :</h4><hr />						
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Host Name :</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="servHostName"/>		
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Description :</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="devDesc"/>		
							</div>
						</div>	
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">IP Address :</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input" id="servIP"/>		
							</div>
						</div>
						<!-- <div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">System Name <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="servSysName"/>		
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">System Type <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<div id="servSysType_dd" class="" " class="">
									<select class="form-control input-small select2me" id="servSysType_ul" data-placeholder="Select...">
									</select>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Processor <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<div id="servProcessor_dd" class="" " class="">
									<select class="form-control input-small select2me" id="servProcessor_ul" data-placeholder="Select...">
									</select>
								</div>
							</div>
						</div> -->		<!--	//For Server Make is not needed.
						<div class="form-group">					
							<div class="col-md-6">
								<label for="input" class="control-label ">Device Make Master <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<div id="deviceModelMaster_dd" class="" " class="">
									<select class="form-control input-small select2me" id="deviceMakeMaster_ul" data-placeholder="Select...">
									</select>
								</div>
							</div>
						</div> -->
						<!-- <div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Device Model Master <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<div id="deviceModelMaster_dd" class="" " class="">
									<select class="form-control input-small select2me" id="deviceModelMaster_ul" data-placeholder="Select...">
									</select>
								</div>
							</div>
						</div>	 -->								
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Host Platform<font color="#efd125" size="4px"> * </font> :</label>
							</div>
							<div class="col-md-6">
								<div id="platformType_dd" class="" " class="">
									<select class="form-control input-small select2me" name="platformType_ul" id="platformType_ul" data-placeholder="Select...">
									</select>
								</div>
							</div>
						</div>
						<!-- <div class="form-group" id="platformTypeVersionheader">
							<div class="col-md-6">
								<label for="input" class="control-label ">Version :</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="platformTypeVersion"/>		
							</div>
						</div>  -->
						<div id="serverTypeAudit">
							<div class="form-group" ">
								<div class="col-md-6">
									<label for="input" class="control-label ">Audit History <font color="#efd125" size="4px"></font>:</label>
								</div>
								<div class="col-md-6">
									<i class="fa fa-search-plus showHandCursor" title="Audit History" onclick="listGenericAuditHistory(''+genericDevId+'','Device','serverAudit');"></i>
								</div>																										
							</div>
							
						</div>
					</div>
					
					<div class="row" id="storageDriveTypeDiv" style="display:none;padding-left:20px;">
						<h4>Fill Storage Drive Details :</h4><hr />		
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Size :</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="storageSize"/>		
							</div>
						</div>								
						<!-- <div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Unit :</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="storageSizeUnit"/>		
							</div>
						</div>	-->	<!--	 yes				-->
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Storage Size Unit <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<div id="storageSizeUnit_dd" class="" " class="">
									<select class="form-control input-small  select2me" id="storageSizeUnit_ul" >
										<option id="-1" value="-1" selected><a href="#">--Select--</a></option>	
										<option id="2" value="4" ><a href="#">MB</a></option>
										<option id="3" value="5"><a href="#">GB</a></option>
										<option id="4" value="6"><a href="#">TB</a></option>
									</select>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Firmware :</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="firmware"/>		
							</div>
						</div>			
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">BootLoader :</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="bootLoader"/>		
							</div>
						</div>		
						<!-- <div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">SerialNumber :</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="serialNumber"/>		
							</div>
						</div> -->		
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Drive Version :</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="driveVersion"/>		
							</div>
						</div>					
						<div class="form-group">					
							<div class="col-md-6">
								<label for="input" class="control-label ">Device Make Master <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<div id="sdDeviceModelMaster_dd" class="" " class="">
									<select class="form-control input-small select2me" id="sdDeviceMakeMaster_ul" data-placeholder="Select...">
									</select>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Device Model Master <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<div id="storagedeviceModelMaster_dd" class="" " class="">
									<select class="form-control input-small select2me" id="storagedeviceModelMaster_ul" data-placeholder="Select...">
									</select>
								</div>
							</div>
						</div>										
						<div class="form-group">
							<div class="col-md-6">
								<label for="input" class="control-label ">Platform Type <font color="#efd125" size="4px"> * </font> :</label>
							</div>
							<div class="col-md-6">
								<div id="storageplatformType_dd" class="" " class="">
									<select class="form-control input-small select2me" name="storageplatformType_ul" id="storageplatformType_ul" data-placeholder="Select...">
									</select>
								</div>
							</div>
						</div>
						<div class="form-group" id="storageplatformTypeVersionheader">
							<div class="col-md-6">
								<label for="input" class="control-label ">Version :</label>
							</div>
							<div class="col-md-6">
								<input type="text" class="form-control" size="30" name="input"  id="storageplatformTypeVersion"/>		
							</div>
						</div> 			
						<!--  Host Div for Add and Edit - for StorageDrive-->
						<div class="form-group">					
							<div class="col-md-6">
								<label for="input" class="control-label ">Host <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<div id="sdHost_dd" class="" " class="">
									<select class="form-control input-small select2me" id="sdHost_ul" data-placeholder="Select...">
									</select>
								</div>
							</div>
						</div>				
						<div class="form-group">					
							<div class="col-md-6">
								<label for="input" class="control-label ">Processor <font color="#efd125" size="4px"> * </font>:</label>
							</div>
							<div class="col-md-6">
								<div id="sdHostProcessor_dd" class="" " class="">
									<select class="form-control input-small select2me" id="sdProcessor_ul" data-placeholder="Select...">
									</select>
								</div>
							</div>
						</div>
						<div id="storageplatformTypeAudit">
							<div class="form-group" ">
								<div class="col-md-6">
									<label for="input" class="control-label ">Audit History <font color="#efd125" size="4px"></font>:</label>
								</div>
								<div class="col-md-6">
									<i class="fa fa-search-plus showHandCursor" title="Audit History" onclick="listGenericAuditHistory(''+genericDevId+'','Device','storageAudit');"></i>
								</div>																										
							</div>
							
						</div>
					</div>
				</div>
				</form>
				<div class="row">
					<div class="col-md-10"></div>
					<div class="col-md-2">							
						 <button type="button" id="startSet" class="btn green-haze" onclick="createDevice();">Create</button>
					</div>
				</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="div_PopupBackground"></div>
<!-- Popup Ends -->
	
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
<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>

<script type="text/javascript">
var flag=1;
var statusId="";
var typeId="";
var treedeviceLabId="";

var mobileplatformTypeVersion_oldValue='';
var mobileAvailableStatus_oldValue='';
var mobileTerminalHost_oldValue='';
jQuery(document).ready(function() {	
   QuickSidebar.init(); // init quick sidebar
   setBreadCrumb("Devices");
   createHiddenFieldsForTree();
	setPageTitle("Devices");
	getTreeData('administration.device.lab');
	
	$("#treeContainerDiv").on("loaded.jstree",function(evt, data) {
		$.each($('#treeContainerDiv li'), function(ind, ele){
			if($.jstree.reference('#treeContainerDiv').is_parent($(ele).attr("id"))){
				var defaultNodeId = $.jstree.reference('#treeContainerDiv').get_node($(ele).attr("id")).children[0];
				$.jstree.reference('#treeContainerDiv').deselect_all();
				$.jstree.reference('#treeContainerDiv').close_all();
				$.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
				$.jstree.reference('#treeContainerDiv').trigger("select_node");
				return false;
			}
		});	
	});	
	
	$("#treeContainerDiv").on("select_node.jstree",
		     function(evt, data){
				if(data.node != undefined){
	   			var entityIdAndType =  data.node.data;
	   			var arry = entityIdAndType.split("~");
	   			var key = arry[0];
	   			var type = arry[1];
	   			var title = data.node.text;
				var date = new Date();
			    var timestamp = date.getTime();
			    var nodeType = type;
			    var loMainSelected = data;
		        uiGetParents(loMainSelected);
		        treedeviceLabId = data.node.data.split("~")[0];
		        statusId= $("#devstatus").find('label.active').attr("data-status");
			  	typeId= $("#devtype").find('label.active').attr("data-type");		    	
		    	showAllDeviceTiles();
				}
			}
	);

	$(document).on('change','#deviceType_ul', function() {
			var devType = $("#deviceType_ul").find('option:selected').val();
			showDeviceTypeDetails(devType);
	 });
	var dev_Type_for_platform = "";
	//Server Type	
	$(document).on('change','#platformType_ul', function() {			
			var platformTypeId = $("#platformType_ul").find('option:selected').attr('id');
			var platformTypeName = $("#platformType_ul").find('option:selected').val();
			dev_Type_for_platform = "server";
			showPlatFormVersions(platformTypeId, platformTypeName, dev_Type_for_platform);
			
		 });
	//Mobile Type
	$(document).on('change','#mobileplatformType_ul', function() {			
		var platformTypeId = $("#mobileplatformType_ul").find('option:selected').attr('id');
		var platformTypeName = $("#mobileplatformType_ul").find('option:selected').val();
		dev_Type_for_platform = "mobile";
		showPlatFormVersions(platformTypeId, platformTypeName, dev_Type_for_platform);
		
	 });
	//Storage Type
	$(document).on('change','#storageplatformType_ul', function() {			
		var platformTypeId = $("#storageplatformType_ul").find('option:selected').attr('id');
		var platformTypeName = $("#storageplatformType_ul").find('option:selected').val();
		dev_Type_for_platform = "storageDrive";
		showPlatFormVersions(platformTypeId, platformTypeName, dev_Type_for_platform);		
	 });
	
});

function popupClose() {	
	$("#div_PopupMain").fadeOut("normal");	
	$("#div_PopupBackground").fadeOut("normal");
}	

/* Load Poup function */
function loadPopup(divId) {
	$("#" + divId).fadeIn(0500); // fadein popup div
	$("#div_PopupBackground").css("opacity", "0.7"); // css opacity, supports
	$("#div_PopupBackground").fadeIn(0001);
}

document.onkeydown = function(evt) {
	evt = evt || window.event;
	if (evt.keyCode == 27) {
		if (document.getElementById("div_PopupMain").style.display == 'block') {
			popupClose();
		}
	}
};

function setTitle(dd,id,text){	
	$(this).css('height','40px')
		dv =$(dd).children('div');
		dv.text(text);	
		dv.attr('id',id);
		dv1 =$(dd).children('div');
		dv1.text(text);	
		dv1.attr('id1',id);
		dv2 =$(dd).children('div');
		dv2.text(text);	
		dv2.attr('id2',id);
	 	
		var workShifts = $("#shift_dd").children();
		workShiftIds= workShifts.attr('id'); 
		var products= $("#product_dd").children();
		productIds= products.attr('id1');
		
		var devicelabs= $("#devicelab_dd").children();
		dd_devicelabId = devicelabs.attr('id1');	
		var platformType= $("#platformType_dd").children();
		platformTypeId = platformType.attr('id1');
		var deviceModelMaster= $("#deviceModelMaster_dd").children();
		deviceModelMasterId = deviceModelMaster.attr('id1');
		var sdDeviceModelMaster= $("#sdDeviceMakeMaster_dd").children();
		sdDeviceModelMasterId = sdDeviceModelMaster.attr('id1');
		var sdHost= $("#deviceModelMaster_dd").children();
		sdHostId = sdHost.attr('id1');
		
		var terminalhost= $("#terminalhost_dd").children();
		terminalhostId = terminalhost.attr('id1');
}

//init custom dropdown menu handler
function DropDown(el) {
			this.dd = el;
			this.initEvents();
		}
		DropDown.prototype = {
			initEvents : function() {
				var obj = this;
				obj.dd.on('click', function(event){
					$(this).toggleClass('active');
					event.stopPropagation();
				});	
			}
		},
		//init custom dropdown menu
		$(function() {
			var shift_dd = new DropDown( $('#shift_dd') ); 
			var devicelab_dd = new DropDown( $('#devicelab_dd') );
			var platformType_dd = new DropDown( $('#platformType_dd') );
			$(document).click(function() {
				// all dropdowns
				$('.wrapper-dropdown-2').removeClass('active');					
			});   
 		});
		
		function loadScrollbarForDropDown(){
			$('.dropdown').jScrollPane({showArrows: false,
			        					scrollbarWidth: 5,
			        					arrowSize: 5});
		}
		
var SOURCE;
var SOURCE;
var treeData;
var dd_devicelabId;
var platformTypeId;
var deviceModelMasterId;
var sdDeviceModelMasterId;
var sdHostId;
var terminalhostId;
//Modified

function devicesNew(url){
	var response;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			response = data.Records;
			listTilesHost(response);
		}       
	});
}

function hostNew(url){
	var response;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			response = data.Records;
			listTilesHost(response);
		}       
	});
}
function listTilesHost(data) {
	$('#tileContent').empty();
	var brick='';
	var imgSrc='';
	var devType= $("#devtype").find('label.active').attr("data-type");
	
	if(devType == 1){
		imgSrc = "css/images/mobile.png";
	}else if(devType == 2){
		imgSrc = "css/images/server.png";
	}else{
		imgSrc = "css/images/storageDrives.PNG";
	}
		
	$.each(data,function(ind, ele){
		var availability = "bg-green"; 
		var udid = (ele.UDID == null)?"":ele.UDID;
		var pfName = "";
		if(flag==1){
			if(ele.availableStatus==0){
				availability = "bg-red-sunglo";
			};
			pfName = (ele.platformTypeName == null)?"":ele.platformTypeName;			
			
		    brick = '<div class="'+"tile "+ availability + '" onclick="updateGenericDevicesPopUp('+ele.genericsDevicesId+')"><div class="tile-body" style="padding:3px 3px;">' +
		    '<div class="row"><img width="30%" height="30px" src="'+ imgSrc +'" ></img></div>'+
		    '<div class="row"><label class="deviceTileWrap" style="font-size: 12px;margin-bottom: 0px;">'+ ele.deviceModelMasterNmae+'</label><span style="font-size:11px; class="deviceTileWrap">'+pfName+'</span><BR/><span class="deviceTileWrap">' + udid +'</span></div></div>'+
		    '<div class="tile-object"><br/><span class="deviceTileWrap">Make : ' + ele.deviceMake+'</span><br/><span class="deviceTileWrap">Res : ' + ele.screenResolutionX +" X "+ele.screenResolutionY+'</span></div>'+
		  //  '<div class="tile-object"><span class="deviceTileWrap">' + ele.screenResolutionX +"x"+ele.screenResolutionY+'</span></div>'+
		    '</div>';
		
		}else if(flag ==2){
			if(ele.hostStatus=='INACTIVE'){
				availability = "bg-red-sunglo";
			};
			
		    brick = '<div class="'+"tile "+ availability + '"><div class="tile-body" style="padding:3px 3px;">' +
		    '<div class="row"><img width="30%" height="30px" src="'+ imgSrc +'"></img></div>'+
		    '<div class="row"><label class="deviceTileWrap" style="font-size: 12px;margin-bottom: 0px;">'+ ele.hostName+'</label><span style="font-size:11px;" class="deviceTileWrap">'+ele.hostPlatform+'</span></div></div>'+
		    '<div class="tile-object" ><span class="deviceTileWrap">' + ele.hostIpAddress +'</span></div>'+
		    '</div>';
		}else if(flag ==3){
			if(ele.availableStatus==0){
				availability = "bg-red-sunglo";
			};
			
		    brick = '<div class="'+"tile "+ availability + '" onclick="updateGenericDevicesPopUp('+ele.genericsDevicesId+')"><div class="tile-body" style="padding:3px 3px;">' +
		    '<div class="row"><img width="30%" height="30px" src="'+ imgSrc +'" ></img></div>'+
		    '<div class="row"><label class="deviceTileWrap" style="font-size: 12px;margin-bottom: 0px;">'+ ele.name+'</label><span style="font-size:11px; class="deviceTileWrap">'+ele.platformTypeName+'</span></div></div>'+
		    '<div class="tile-object"><span class="deviceTileWrap">' + udid +'</span></div>'+
		    '</div>';
		
		}
		$('#tileContent').append($(brick));
	});
	if(!($('#tileContent').hasClass('tiles'))){
		$('#tileContent').addClass('tiles').css("margin-top","10px");
	}
}

function addGenericDevicesPopUp()
{	
     var devLabId = treedeviceLabId;
	 if((devLabId==null)|| (devLabId==undefined) || (devLabId==-1)){
		 callAlert("Please select DeviceLab");
		 return false;
	 }
	$('#div_PopupMain').find('.modal-title').replaceWith('<h4 class="modal-title">Add Target Host / Device</h4>');
	document.getElementById("terminalHostDiv").style.display = "none";
	$("#startSet").html('Create');
		$("#startSet").attr("onclick","createDevice()");
	//Empty textboxes	
	$('#devName').val('');
		$('#devName').prop('disabled', false);
	$('#devDesc').val('');
		$('#devDesc').prop('disabled', false);
	$('select[id="availableStatus_ul"] option[value="-1"]').attr("selected","selected");
	   $('#availableStatus_ul').select2();
	   $('#availableStatus_ul').prop('disabled', false);
	 $('#startRemarks').val('');
		$('#startRemarks').prop('disabled', false);	
	$('select[id="deviceType_ul"] option[value="-1"]').attr("selected","selected");
	   $('#deviceType_ul').select2();
	   $('#deviceType_ul').prop('disabled', false);	

	//For Mobile Type
	$('#kernelNumber').val('');
		 $('#kernelNumber').prop('disabled', false);
	$('#buildNumber').val('');
		 $('#buildNumber').prop('disabled', false);
	$('select[id="deviceMakeMaster_ul"] option[value="-1"]').attr("selected","selected");
	   $('#deviceMakeMaster_ul').select2();
	   $('#deviceMakeMaster_ul').prop('disabled', false); 			 
	$('select[id="mobiledeviceModelMaster_ul"] option[value="-1"]').attr("selected","selected");
	   $('#mobiledeviceModelMaster_ul').select2();
	   $('#mobiledeviceModelMaster_ul').prop('disabled', false);
	$('select[id="mobileplatformType_ul"] option[value="-1"]').attr("selected","selected");
	   $('#mobileplatformType_ul').select2();
	   $('#mobileplatformType_ul').prop('disabled', false);	
	$('#mobileplatformTypeVersionheader').show();
		$('#mobileplatformTypeVersion').val('');
		$('#mobileplatformTypeVersion').prop('disabled', false);
	$('#mobileplatformTypeAudit').hide();
	$('#screenResolutionXVal').val('');
	$('#screenResolutionYVal').val('');
		
	//For Server Type		 
	$('#servHostName').val('');
		 $('#servHostName').prop('disabled', false);
	$('#servIP').val('');
		 $('#servIP').prop('disabled', false);
	$('#servSysName').val('');
		 $('#servSysName').prop('disabled', false);
	$('select[id="servSysType_ul"] option[value="-1"]').attr("selected","selected");
	   $('#servSysType_ul').select2();
	   $('#servSysType_ul').prop('disabled', false);
	   
	$('select[id="servProcessor_ul"] option[value="-1"]').attr("selected","selected");
	   $('#servProcessor_ul').select2();
	   $('#servProcessor_ul').prop('disabled', false);
	   //Make not needed for server
	$('select[id="deviceModelMaster_ul"] option[value="-1"]').attr("selected","selected");
	   $('#deviceModelMaster_ul').select2();
	   $('#deviceModelMaster_ul').prop('disabled', false);

	$('select[id="platformType_ul"] option[value="-1"]').attr("selected","selected");
		$('#platformType_ul').select2();
		$('#platformType_ul').prop('disabled', false);	
	$('#platformTypeVersionheader').show();
		$('#platformTypeVersion').val('');
		$('#platformTypeVersion').prop('disabled', false);
	$('#serverTypeAudit').hide();
		 
	//For Storage Drive			
	$('#storageSize').val('');
	$('#storageSize').prop('disabled', false);
	
	$('#storageplatformTypeAudit').hide();
	/* $('#storageSizeUnit').val('');
	$('#storageSizeUnit').prop('disabled', false); */
	$('select[id="storageSizeUnit_ul"] option[value="-1"]').attr("selected","selected");
	   $('#storageSizeUnit_ul').select2();
	   $('#storageSizeUnit_ul').prop('disabled', false);	
		
	$('#firmware').val('');
	$('#firmware').prop('disabled', false);
			
	$('#bootLoader').val('');
	$('#bootLoader').prop('disabled', false);

	/* $('#serialNumber').val('');
	$('#serialNumber').prop('disabled', false); */
		
	$('#driveVersion').val('');
	$('#driveVersion').prop('disabled', false);

	$('select[id="sdDeviceMakeMaster_ul"] option[value="-1"]').attr("selected","selected");
		$('#sdDeviceMakeMaster_ul').select2();
		$('#sdDeviceMakeMaster_ul').prop('disabled', false);
	
	$('select[id="storagedeviceModelMaster_ul"] option[value="-1"]').attr("selected","selected");
	   $('#storagedeviceModelMaster_ul').select2();
	   $('#storagedeviceModelMaster_ul').prop('disabled', false); 

	$('select[id="storageplatformType_ul"] option[value="-1"]').attr("selected","selected");
	   $('#storageplatformType_ul').select2();
	   $('#storageplatformType_ul').prop('disabled', false);
	   
	$('#storageplatformTypeVersionheader').show();
		$('#storageplatformTypeVersion').val('');
		$('#storageplatformTypeVersion').prop('disabled', false);		 
	
	$('select[id="sdHost_ul"] option[value="-1"]').attr("selected","selected");
		$('#sdHost_ul').select2();
		$('#sdHost_ul').prop('disabled', false);

	$('select[id="sdProcessor_ul"] option[value="-1"]').attr("selected","selected");
		$('#sdProcessor_ul').select2();
		$('#sdProcessor_ul').prop('disabled', false);	
	 
	 document.getElementById("serverTypeDiv").style.display = "none";
	 document.getElementById("mobileTypeDiv").style.display = "none";	
	 document.getElementById("storageDriveTypeDiv").style.display = "none";	 
	 
	 loadPopup("div_PopupMain"); 
	 
     loadPlatformType("add", "");//Server add
     loadMobilePlatformType("add", "")//Mobile
     loadSDPlatformType("add", "")//StorageDrive
     
     //Make not needed for Server
     loadDeviceMakeMaster("add", "");//Mobile
     loadSDDeviceMakeMaster("add", "");//StorageDrive
     
     loaddeviceModelMaster("add", "");//Server
     loadMobiledeviceModelMaster("add", "");//Mobile
     loadStoragedeviceModelMaster("add", "");     //StorageDrive
     
     loadSDHost("add", "");
 	 loadSystemType("add", "");
 	 loadSystemProcessor("add", "");
 	 loadDriveSystemProcessor("add", "");
}

var genericDevId = '';
function updateGenericDevicesPopUp(genericsDevicesId){	
	var url = 'admin.genericDevices.list.bygenericsDevicesId?genericsDevicesId='+genericsDevicesId;
	var response;
	//Replace Header and Button
	$('#div_PopupMain').find('.modal-title').replaceWith('<h4 class="modal-title">Edit Device</h4>');
	
	$("#startSet").html('Update');
	$("#startSet").attr("onclick","updateDevice()");	
	 $.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : url,
		dataType : 'json',
		success : function(data) {
			response = data.Record;
			$('#devName').val(response.name);
			$('#devName').prop('disabled', true);
			
			$('#devDesc').val(response.description);
			//$('#devDesc').prop('disabled', true);
			
			genericDevId = response.genericsDevicesId;//Globally used
			
			var avail_status = response.availableStatus;
			$('select[id="availableStatus_ul"] option[value="'+avail_status+'"]').attr("selected","selected");
			   $('#availableStatus_ul').select2();
			   
		   //var platform_typename = response.platformTypeName;
			var platform_typeId = response.platformTypeId;		
			var platform_version= response.platformTypeversion;
			$('#startRemarks').val(response.UDID);	
				$('#startRemarks').prop('disabled', true);
			
			//var dev_modelmasterName = response.deviceModelMasterNmae;
			var dev_modelmasterId = response.deviceModelMasterId;	
			/* loaddeviceModelMaster("update", dev_modelmasterId);
			$('#deviceModelMaster_ul').prop('disabled', true);	 */
			
			var dev_type = response.devType;
			$('select[id="deviceType_ul"] option[value="'+dev_type+'"]').attr("selected","selected");
				$('#deviceType_ul').select2();
			//$('#deviceType_ul').prop('disabled', true);
			
			$('#screenResolutionXVal').val(response.screenResolutionX);
			$('#screenResolutionYVal').val(response.screenResolutionY);
			
			showDeviceTypeDetails(dev_type);
			if(dev_type == 6){//Storage Drive
			   $('#availableStatus_ul').prop('disabled', false); //Enabling Status - Editable
			   document.getElementById("terminalHostDiv").style.display = "none"; //Hiding Host - Not Needed.		   
			   var sdSize = response.storageSize;
			   $('#storageSize').val(sdSize);	
			//   $('#storageSize').prop('disabled', true);
			   
			   /* var sdSizeUnit = response.storageSizeUnit;
			   $('#storageSizeUnit').val(sdSizeUnit);	
			   $('#storageSizeUnit').prop('disabled', true); */
			   var sdSizeUnit = response.storageSizeUnit;
			   if(sdSizeUnit ==  "MB"){
				   sdSizeUnit = 4;
			   }else if(sdSizeUnit ==  "GB"){
				   sdSizeUnit = 5;
			   }else if(sdSizeUnit ==  "TB"){
				   sdSizeUnit = 6;
			   }
				$('select[id="storageSizeUnit_ul"] option[value="'+sdSizeUnit+'"]').attr("selected","selected");
				$('#storageSizeUnit_ul').select2();
				//$('#storageSizeUnit_ul').prop('disabled', true);
			   
			   var firmware = response.firmware;
			   $('#firmware').val(firmware);	
			 //  $('#firmware').prop('disabled', true);
			   
			   var bootLoader = response.bootLoader;
			   $('#bootLoader').val(bootLoader);	
			 //  $('#bootLoader').prop('disabled', true);
			   
			  /*  var serialNumber = response.serialNumber;
			   $('#serialNumber').val(serialNumber);
			   $('#serialNumber').prop('disabled', true); */
			   
			   var driveVersion = response.driveVersion;
			   $('#driveVersion').val(driveVersion);
			  // $('#driveVersion').prop('disabled', true);
			   
			   var deviceMakeMaterId = response.deviceMakeId;
			   loadSDDeviceMakeMaster("update", deviceMakeMaterId);
			//   $('#sdDeviceMakeMaster_ul').prop('disabled', true);
			   
			 //For StorageDrive
			  loadStoragedeviceModelMaster("update", dev_modelmasterId);
			//  $('#storagedeviceModelMaster_ul').prop('disabled', true);	
			//StorageDrive
			  loadSDPlatformType("update", platform_typeId);
			 // $('#storageplatformType_ul').prop('disabled', true);	
				
			  $('#storageplatformTypeVersionheader').show();
			  $('#storageplatformTypeVersion').val(platform_version);
			//  $('#storageplatformTypeVersion').prop('disabled', true);
				
			   var sdHostId = response.hostId;			
			   loadSDHost("update", sdHostId);
			  // $('#sdHost_ul').prop('disabled', true);
			   
			   var serv_ProcessorId = response.server_processorId;			   
			//   var serv_ProcessorName = response.server_processorName;
			   loadDriveSystemProcessor("update", serv_ProcessorId);
			 //  $('#sdProcessor_ul').prop('disabled', true);
			}
			else if(dev_type == 5){//Mobile
				$('#availableStatus_ul').prop('disabled', false); //Enabling Status - Editable
				document.getElementById("terminalHostDiv").style.display = "block";//Visualizagin Host - Not Needed.
				var terminal_hostid = response.hostId;
				//var terminal_hostname = response.hostName;
				loadterminalhost("update", terminal_hostid);//
				
				var kernerlno = response.kernelNumber;
			   	$('#kernelNumber').val(kernerlno);	
			   //	$('#kernelNumber').prop('disabled', true);
			   	var buildno = response.buildNumber;
			   	$('#buildNumber').val(buildno);
			 //  	$('#buildNumber').prop('disabled', true);
			   
				var deviceMakeMaterId = response.deviceMakeId;
				//   var deviceMakeMaterName = response.deviceMake;
				loadDeviceMakeMaster("update", deviceMakeMaterId);
			//	$('#deviceMakeMaster_ul').prop('disabled', true);
			   
				//For Mobile
				loadMobiledeviceModelMaster("update", dev_modelmasterId);
				//$('#mobiledeviceModelMaster_ul').prop('disabled', true);	
			  
				//Mobile
				loadMobilePlatformType("update", platform_typeId);
				//$('#mobileplatformType_ul').prop('disabled', true);	
			
				$('#mobileplatformTypeVersionheader').show();
				$('#mobileplatformTypeVersion').val(platform_version);
			//	$('#mobileplatformTypeVersion').prop('disabled', true);	
			}
			else if(dev_type == 4){//Server
				$('#availableStatus_ul').prop('disabled', true);//Disabling Status - Non-editable
				document.getElementById("terminalHostDiv").style.display = "none"; //Hiding Host - Not Needed.				
				/* var terminal_hostid = response.hostId;
				//var terminal_hostname = response.hostName;
				loadterminalhost("update", terminal_hostid); */
				
				var serv_hostname = response.server_hostName;
				$('#servHostName').val(serv_hostname);
				//$('#servHostName').prop('disabled', true);
				
				var serv_Ip = response.server_ip;
				$('#servIP').val(serv_Ip);
				//$('#servIP').prop('disabled', true);
				
				var serv_SysName = response.server_systemName;
				$('#servSysName').val(serv_SysName);
				//$('#servSysName').prop('disabled', true);
			   
				var serv_SysTypeId = response.server_systemTypeId;
			//	$('#servSysType_ul').prop('disabled', true);
				loadSystemType("update", serv_SysTypeId);

				var serv_ProcessorId = response.server_processorId;			   
				var serv_ProcessorName = response.server_processorName;
				loadSystemProcessor("update", serv_ProcessorId);
			//	$('#servProcessor_ul').prop('disabled', true);			   
				//Make not needed for Server
				//For Server
				loaddeviceModelMaster("update", dev_modelmasterId);
				//$('#deviceModelMaster_ul').prop('disabled', true);	
				//Server
				loadPlatformType("update", platform_typeId);
				//$('#platformType_ul').prop('disabled', true);	
				
				$('#platformTypeVersionheader').show();
				$('#platformTypeVersion').val(platform_version);	
				//$('#platformTypeVersion').prop('disabled', true);
			}

			mobileplatformTypeVersion_oldValue = document.getElementById("mobileplatformTypeVersion").value;
			mobileAvailableStatus_oldValue = document.getElementById("availableStatus_ul").value;
			
		}
	}); 
	 loadPopup("div_PopupMain"); 
	 
}
var frmdataforDeviceType="";
function createDevice()
{	
	var devTypeId = $("#deviceType_ul").find('option:selected').attr('id');
	if((devTypeId==null)|| (devTypeId==undefined) || (devTypeId==-1)){
		callAlert("Please select Device Type");
		 return false;
	}
	
	var devDesc = document.getElementById("devDesc").value;
	if(devTypeId != 4){
		var devName = document.getElementById("devName").value;
		if(devName ==""){
			callAlert("Please Enter Device Name");
			 return false;	
		}	
		
		var devLabId = treedeviceLabId;
		if((devLabId==null)|| (devLabId==undefined)|| (devLabId==-1)){
			 callAlert("Please select DeviceLab");
			 return false;
		}
	}
	var devAvailStatus = $("#availableStatus_ul").find('option:selected').attr('id');
	if((devAvailStatus==null)|| (devAvailStatus==undefined)|| (devAvailStatus==-1)){
		 callAlert("Please select Status");
		 return false;
	 }
	
	var uDID = document.getElementById("startRemarks").value;	
	/* var devTypeId = $("#deviceType_ul").find('option:selected').attr('id'); */
	
	
	if($("#servIP").val() !=''){
		 if(!ipValidation()){
			 callAlert("Please enter a valid ip address");
			 return false;
		 }
	}
	
	if(devTypeId == 4){		
		var hostName = document.getElementById("servHostName").value;
		var hostIp = document.getElementById("servIP").value;
		//var sysName = document.getElementById("servSysName").value;	
		//var sysTypeId = $("#servSysType_ul").find('option:selected').attr('id');
 		/* if((sysTypeId==null)|| (sysTypeId==undefined)|| (sysTypeId==-1)){
			 callAlert("Please select System Type");
 			 return false;
 		 }	
		var sysProcessorId = $("#servProcessor_ul").find('option:selected').attr('id');
 		if((sysProcessorId==null)|| (sysProcessorId==undefined)|| (sysProcessorId==-1)){
 			 callAlert("Please select System Processor");
 			 return false;
 		 }
 		var deviceModelId = $("#deviceModelMaster_ul").find('option:selected').attr('id');
 		if((deviceModelId==null)|| (deviceModelId==undefined)|| (deviceModelId==-1)){
 			 callAlert("Please select DeviceModelMaster");
 			 return false;
 		 } */
 		var platTypeId = $("#platformType_ul").find('option:selected').attr('id');
 		if((platTypeId==null)|| (platTypeId==undefined)|| (platTypeId==-1)){
 			 callAlert("Please select platformType");
 			 return false;
 		 }		
 			
	}
	else if(devTypeId == 5){
		var kerNo = document.getElementById("kernelNumber").value;
		var buildNo = document.getElementById("buildNumber").value;	
		var screenResolutionXVal = document.getElementById("screenResolutionXVal").value;
		var screenResolutionYVal = document.getElementById("screenResolutionYVal").value;
		var devMakeId = $("#deviceMakeMaster_ul").find('option:selected').attr('id');
 		if((devMakeId==null)|| (devMakeId==undefined)|| (devMakeId==-1)){
			callAlert("Please select Device Make");
			return false;
 		}

 		var deviceModelId = $("#mobiledeviceModelMaster_ul").find('option:selected').attr('id');
 		if((deviceModelId==null)|| (deviceModelId==undefined)|| (deviceModelId==-1)){
 			callAlert("Please select DeviceModelMaster");
 			return false;
		}
 		var platTypeId = $("#mobileplatformType_ul").find('option:selected').attr('id');
 		if((platTypeId==null)|| (platTypeId==undefined)|| (platTypeId==-1)){
 			callAlert("Please select platformType");
 			return false;
 		}
 		if((uDID==null)|| (uDID=="")|| (uDID==undefined)|| (uDID==-1)){
 			callAlert("Please enter UDID");
 			return false;
 		}
 		if((screenResolutionXVal==null)|| (screenResolutionXVal=="")||(screenResolutionXVal==undefined)|| (screenResolutionXVal==-1)){
			callAlert("Please enter Screen Resolution X");
			return false;
		}
 		if((screenResolutionYVal==null)|| (screenResolutionYVal==undefined)|| (screenResolutionYVal=="")|| (screenResolutionYVal==-1)){
 			callAlert("Please enter Screen Resolution Y");
 			return false;
 		}
 		
	}
	else if(devTypeId == 6){
		var sdSize = document.getElementById("storageSize").value;
		var value = Number(sdSize);
		if (Math.floor(value) == value) {
		  // value is an integer, do something based on that
		} else {
		  // value is not an integer, show some validation error
			callAlert("Please enter Size in Numeric");
			return false;
		}
	//	var sdstorageSizeUnit = document.getElementById("storageSizeUnit").value;	
		var sdstorageSizeUnit = $("#storageSizeUnit_ul").find('option:selected').attr('id');
		if((sdstorageSizeUnit==null)|| (sdstorageSizeUnit==undefined) || (sdstorageSizeUnit==-1)){
			callAlert("Please select Size Unit");
			 return false;
		 }
		var sdFirmware = document.getElementById("firmware").value;
		var sdbootLoader = document.getElementById("bootLoader").value;
		//var sdserialNumber = document.getElementById("serialNumber").value;
		var sddriveVersion = document.getElementById("driveVersion").value;
		
		var devMakeId = $("#sdDeviceMakeMaster_ul").find('option:selected').attr('id');
 		if((devMakeId==null)|| (devMakeId==undefined)|| (devMakeId==-1)){
 			 callAlert("Please select Device Make");
			 return false;
 		 }

 		var deviceModelId = $("#storagedeviceModelMaster_ul").find('option:selected').attr('id');
 		if((deviceModelId==null)|| (deviceModelId==undefined)|| (deviceModelId==-1)){
 			 callAlert("Please select DeviceModelMaster");
 			 return false;
 		 }
 		var platTypeId = $("#storageplatformType_ul").find('option:selected').attr('id');
 		if((platTypeId==null)|| (platTypeId==undefined)|| (platTypeId==-1)){
 			 callAlert("Please select platformType");
 			 return false;
 		 }
 		var terminalHostId = $("#sdHost_ul").find('option:selected').attr('id');
		if((terminalHostId==null)|| (terminalHostId==undefined) || (terminalHostId==-1)){
			callAlert("Please select Host");
			return false;
		}	
		var sysProcessorId  = $("#sdProcessor_ul").find('option:selected').attr('id');
 		if((sysProcessorId==null)|| (sysProcessorId==undefined)|| (sysProcessorId==-1)){
 			 callAlert("Please select System Processor");
 			 return false;
 		}
	}	
	//alert("devName--"+devName+"--devDesc--"+devDesc+"--devLabId--"+devLabId+"--devAvailStatus--"+devAvailStatus+"--platTypeId-"+platTypeId+"--deviceModelId--"+deviceModelId);
	//alert("--devTypeId--"+devTypeId+"--kerNo--"+kerNo+"--buildNo--"+buildNo+"--devMakeId--"+devMakeId+"--hostName--"+hostName+"--hostIp--"+hostIp+"--sysName--"+sysName+"--sysTypeId--"+sysTypeId+"--sysProcessorId--"+sysProcessorId);
	//'admin.genericDevices.add?productId='+productId
	openLoaderIcon();
	var url='admin.genericDevices.array.add?devTypeId='+devTypeId;
	if(devTypeId == 4){
		//frmdataforDeviceType ={'devName': devName, 'devDesc': devDesc, 'devLabId': devLabId,'devAvailStatus': devAvailStatus,'platTypeId':platTypeId,'udid':uDID,'deviceModelId':deviceModelId,'hostName':hostName,'hostIp':hostIp,'sysName':sysName,'sysTypeId':sysTypeId,'sysProcessorId':sysProcessorId  };
		frmdataforDeviceType ={'devDesc': devDesc,'devAvailStatus': devAvailStatus,'platTypeId':platTypeId,'hostName':hostName,'hostIp':hostIp};
	}else if(devTypeId == 5){
		frmdataforDeviceType ={'devName': devName, 'devDesc': devDesc, 'devLabId': devLabId,'devAvailStatus': devAvailStatus,'platTypeId':platTypeId,'udid':uDID,'deviceModelId':deviceModelId,'kerNo':kerNo,'buildNo':buildNo,'devMakeId':devMakeId,'screenResolutionX':screenResolutionXVal,'screenResolutionY':screenResolutionYVal };
	}else if(devTypeId == 6){
		frmdataforDeviceType ={'devName': devName, 'devDesc': devDesc, 'devLabId': devLabId,'devAvailStatus': devAvailStatus,'platTypeId':platTypeId,'udid':uDID,'sdSize': sdSize, 'sdstorageSizeUnit': sdstorageSizeUnit, 'sdFirmware': sdFirmware,'sdbootLoader': sdbootLoader,'sddriveVersion':sddriveVersion,'deviceModelId':deviceModelId,'devMakeId':devMakeId,'terminalHostId':terminalHostId,'sysProcessorId':sysProcessorId };
	}	
 		$.ajax({
 		    type: "POST",
 		    url: url,
 		    //data: { 'devName': devName, 'devDesc': devDesc, 'devLabId': devLabId,'devAvailStatus': devAvailStatus,'platTypeId':platTypeId,'deviceModelId':deviceModelId},
 		     data: frmdataforDeviceType,
 		   success : function(data) {
				closeLoaderIcon();
 				if(data.Result=="ERROR"){
 					callAlert(data.Message);
 		    		return false;
 		    	}else{
 		    		popupClose();
 		    		showAllDeviceTiles();
 		    	}
 			},
 		    dataType: "json", // expected return value type
 		});
}
		
	function floatvalidation(event){
		var keyCode = (event.which) ? event.which : (window.event.keyCode) ? window.event.keyCode : -1;
	     if ((keyCode >= 48 && keyCode <= 57)) {
	         return true;
	     }else if (keyCode == 46) {
	         if ((event.value) && (event.value.indexOf('.') >= 0)){
	        	 event.preventDefault();
	             return false;
	         }
	         else
	             return true;
	     }else{
	    	 event.preventDefault();
	    	 return false;
	     }
	     return false;
    }
		
	function ipValidation(){
		var ipformat = /^((([01]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))[.]){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))$/;
		var val = $("#servIP").val();
		 if(val.match(ipformat)){		 	
		 	return true;
		 }else{
			 return false;
		 }
		 return false;
		//"regex": /^((([01]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))[.]){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))$/,
    }	
		
	
function updateDevice()
{			//mamtha
	//	var platformVersion = document.getElementById("platformTypeVersion").value;
	//	var platformName = $( "#platformType_ul" ).val();
	var flag=false;
	var mobileplatformVersionFlag = false;
	var mobileStatusFlag = false;
	var mobilehostListFlag = false;
	var mobileplatformTypeVersion_newValue = document.getElementById("mobileplatformTypeVersion").value;
	var mobileAvailableStatus_newValue = document.getElementById("availableStatus_ul").value;
	var mobileTerminalHost_newValue = document.getElementById("terminalhost_ul").value;
	
	if(mobileplatformTypeVersion_oldValue != mobileplatformTypeVersion_newValue){
		flag = true;
		mobileplatformVersionFlag = true;
	}
	if(mobileAvailableStatus_oldValue != mobileAvailableStatus_newValue){
		flag = true;
		mobileStatusFlag = true;
		if(mobileAvailableStatus_oldValue == 1){
			mobileAvailableStatus_oldValue = "Connected";
		}else{
			mobileAvailableStatus_oldValue = "NotConnected";
		}
		if(mobileAvailableStatus_newValue == 1){
			mobileAvailableStatus_newValue = "Connected";
		}else{
			mobileAvailableStatus_newValue = "NotConnected";
		}
	}
	if(mobileTerminalHost_oldValue != mobileTerminalHost_newValue){
		flag = true;
		mobilehostListFlag = true;
	}
	
	//if(flag){
		var devTypeId = $("#deviceType_ul").find('option:selected').attr('id');
		if((devTypeId==null)|| (devTypeId==undefined) || (devTypeId==-1)){
			callAlert("Please select Device Type");
			 return false;
		}
	
		if(devTypeId == 4){
				var platformVersion = document.getElementById("platformTypeVersion").value;
				var platformName = $( "#platformType_ul" ).val();				
				
				/* var terminalHostId = $("#terminalhost_ul").find('option:selected').attr('id');
				if((terminalHostId==null)|| (terminalHostId==undefined) || (terminalHostId==-1)){
					callAlert("Please select Host");
					return false;
				} */
				//frmdataforDeviceType ={'devName': devName, 'devDesc': devDesc, 'devLabId': devLabId,'devAvailStatus': devAvailStatus,'platTypeId':platTypeId,'deviceModelId':deviceModelId,'hostName':hostName,'hostIp':hostIp,'sysName':sysName,'sysTypeId':sysTypeId,'sysProcessorId':sysProcessorId  };
		}else if(devTypeId == 5){
				var platformVersion = document.getElementById("mobileplatformTypeVersion").value;
				var platformName = $( "#mobileplatformType_ul" ).val();
				//var platTypeId = $("#mobileplatformType_ul").find('option:selected').attr('id');
		 		if((platformName==null)|| (platformName==undefined)|| (platformName==-1) || (platformName=='')){
		 			 callAlert("Please select platformType");
		 			 return false;
		 		 }
		 		var screenResolutionXVal = document.getElementById("screenResolutionXVal").value;
				var screenResolutionYVal = document.getElementById("screenResolutionYVal").value;
		 		
		 		if((screenResolutionXVal==null)|| (screenResolutionXVal=="")||(screenResolutionXVal==undefined)|| (screenResolutionXVal==-1)){
					callAlert("Please enter Screen Resolution X");
					return false;
				}
		 		if((screenResolutionYVal==null)|| (screenResolutionYVal==undefined)|| (screenResolutionYVal=="")|| (screenResolutionYVal==-1)){
		 			callAlert("Please enter Screen Resolution Y");
		 			return false;
		 		}
		 		if((platformVersion==null)|| (platformVersion==undefined)|| (platformVersion==-1) || (platformVersion=='')){
		 			 callAlert("Please enter Version");
		 			 return false;
		 		 }
		 		
				var terminalHostId = $("#terminalhost_ul").find('option:selected').attr('id');
				if((terminalHostId==null)|| (terminalHostId==undefined) || (terminalHostId==-1)){
					callAlert("Please select Host");
					return false;
				}
				//frmdataforDeviceType ={'devName': devName, 'devDesc': devDesc, 'devLabId': devLabId,'devAvailStatus': devAvailStatus,'platTypeId':platTypeId,'deviceModelId':deviceModelId,'kerNo':kerNo,'buildNo':buildNo,'devMakeId':devMakeId };
		}		    
					
		var devAvailStatus = $("#availableStatus_ul").find('option:selected').attr('id');
		if((devAvailStatus==null)|| (devAvailStatus==undefined)|| (devAvailStatus==-1)){
			callAlert("Please select Status");
			return false;
		}
				
		var devTypeId = $("#deviceType_ul").find('option:selected').attr('id');
		if((devTypeId==null)|| (devTypeId==undefined) || (devTypeId==-1)){
			callAlert("Please select Device Type");
			return false;
		}
		openLoaderIcon();
		var url='admin.genericDevices.array.update?devTypeId='+devTypeId;
		$.ajax({
 		    type: "POST",
 		    url: url,
 		    //data: { 'devName': devName, 'devDesc': devDesc, 'devLabId': devLabId,'devAvailStatus': devAvailStatus,'platTypeId':platTypeId,'deviceModelId':deviceModelId},
			data: { 'devAvailStatus': devAvailStatus,'terminalHostId':terminalHostId,'genericsDevicesId':genericDevId,'platformVersion':platformVersion,'platformName':platformName,
					'mobileplatformVersionFlag': mobileplatformVersionFlag,'mobileplatformTypeVersion_oldValue':mobileplatformTypeVersion_oldValue,'mobileplatformTypeVersion_newValue':mobileplatformTypeVersion_newValue,
					'mobileStatusFlag':mobileStatusFlag,'mobileAvailableStatus_oldValue':mobileAvailableStatus_oldValue,'mobileAvailableStatus_newValue':mobileAvailableStatus_newValue,
					'mobilehostListFlag':mobilehostListFlag,'mobileTerminalHost_oldValue':mobileTerminalHost_oldValue,'mobileTerminalHost_newValue':mobileTerminalHost_newValue,'screenResolutionX':screenResolutionXVal,
					'screenResolutionY':screenResolutionYVal},
 		    //data: frmdataforDeviceType,
 		   success : function(data) {
				closeLoaderIcon();
 				if(data.Result=="ERROR"){
 					callAlert(data.Message);
 		    		return false;
 		    	}else{
 		    		popupClose();
 		    		showAllDeviceTiles();	
 		    	}
 			},
 		    dataType: "json", // expected return value type
 		});	
	//}
	
}		

//Listing items in Drop down from Server
function loadPlatformType(addorupdate, selectedId){
	$('#platformType_ul').empty();
	if(addorupdate == 'add'){
		$('#platformType_ul').append('<option style="padding-Bottom:28px" id="-1" selected><a href="#">' + '--Select--' + '</a></li>');
		$('#platformType_ul').append('<option style="padding-Bottom:28px" id="WINDOWS GENERIC" selected><a href="#">WINDOWS GENERIC</a></li>');
		$('#platformType_ul').append('<option style="padding-Bottom:28px" id="LINUX GENERIC"><a href="#">LINUX GENERIC</a></li>');
		$('#platformType_ul').append('<option style="padding-Bottom:28px" id="MAC GENERIC"><a href="#">MAC GENERIC</a></li>');
		$('#platformType_ul').prop('disabled', false);	 
	}
	/* $.post('common.platformtype.listwithversion',function(data) {		
		if(data.Result=="ERROR"){
			callAlert(data.Message);
    		return false;
    		}else{
		        var ary = (data.Options);
				if(addorupdate == 'add'){
					$('#platformType_ul').append('<option style="padding-Bottom:28px" id="-1" selected><a href="#">' + '--Select--' + '</a></li>');
					$('#platformType_ul').append('<option style="padding-Bottom:28px" id="WINDOWS GENERIC" selected><a href="#">WINDOWS GENERIC</a></li>');
					$('#platformType_ul').append('<option style="padding-Bottom:28px" id="LINUX GENERIC" selected><a href="#">LINUX GENERIC</a></li>');
					$('#platformType_ul').append('<option style="padding-Bottom:28px" id="MAC GENERIC" selected><a href="#">MAC GENERIC</a></li>');
					$('#platformType_ul').prop('disabled', false);	 
				}			    
		        /* $.each(ary, function(index, element) {		        	
		        	if(addorupdate == 'update'){
						if(element.Value == ""+selectedId+""){
							$('#platformType_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#platformType_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')" selected><a href="#">' + element.DisplayText + '</a></li>');
						}else{
							$('#platformType_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#platformType_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
						}
					}else{
					$('#platformType_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#platformType_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
					}
		       });
		        $('#platformType_ul').select2();
    		}
	});
	*/
	$('#platformType_ul').select2();
}
//Listing items in Drop down from Server
function loaddeviceModelMaster(addorupdate, selectedId){
	$('#deviceModelMaster_ul').empty();
	$.post('common.deviceModelMaster.list',function(data) {		
		if(data.Result=="ERROR"){
			callAlert(data.Message);
    		return false;
    		}else{
		        var ary = (data.Options);
		        if(addorupdate =='add'){
					$('#deviceModelMaster_ul').append('<option style="padding-Bottom:28px" id="-1" selected><a href="#">' + '--Select--' + '</a></li>');
					$('#deviceModelMaster_ul').prop('disabled', false);	 
				}				
		        $.each(ary, function(index, element) {		        	
		        	if( addorupdate = 'update'){
						if(element.Value == ""+selectedId+""){
							$('#deviceModelMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#deviceModelMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')" selected><a href="#">' + element.DisplayText + '</a></li>');
						}else{
							$('#deviceModelMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#deviceModelMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
						}
					}else{
						$('#deviceModelMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#deviceModelMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
					}					
		       });
		        $('#deviceModelMaster_ul').select2();
    		}
	});
}

//Listing items in Drop down from Server
function loadterminalhost(addorupdate, selectedId){
	$('#terminalhost_ul').empty();
	$.post('common.host.list.deviceLab?deviceLabId='+treedeviceLabId+'&filter=-1',function(data) {		
		if(data.Result=="ERROR"){
			callAlert(data.Message);
    		return false;
    		}else{
		        var ary = (data.Options);
		        if(addorupdate =='add'){
					$('#terminalhost_ul').append('<option style="padding-Bottom:28px" id="-1" selected><a href="#">' + '--Select--' + '</a></li>');
				}				
		        $.each(ary, function(index, element) {		        	
		        	if(addorupdate =='update'){
						if(element.Value == ""+selectedId+""){
							$('#terminalhost_ul').append('<option style="padding-Bottom:28px; width: 200px !important;" id="' + element.Value + '" title="'+element.DisplayText+'" onclick="setTitle(\'#terminalhost_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')" selected><a href="#">' + element.DisplayText + '</a></li>');
						}else{
							$('#terminalhost_ul').append('<option style="padding-Bottom:28px; width: 200px !important;" id="' + element.Value + '" title="'+element.DisplayText+'" onclick="setTitle(\'#terminalhost_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
						}
					}else{
						$('#terminalhost_ul').append('<option style="padding-Bottom:28px; width: 200px !important;" id="' + element.Value + '" title="'+element.DisplayText+'" onclick="setTitle(\'#terminalhost_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
					}
					
				});
		        $('#terminalhost_ul').select2();
		        mobileTerminalHost_oldValue = document.getElementById("terminalhost_ul").value;
    		}
	});
}

//Listing items in Drop down from Server
function loadDeviceMakeMaster(addorupdate, selectedId){
	$('#deviceMakeMaster_ul').empty();
	$.post('common.deviceMakeMaster.list',function(data) {		
		if(data.Result=="ERROR"){
			callAlert(data.Message);
    		return false;
    		}else{
		        var ary = (data.Options);
				 if(addorupdate =='add'){
					$('#deviceMakeMaster_ul').append('<option style="padding-Bottom:28px" id="-1" selected><a href="#">' + '--Select--' + '</a></li>');
					$('#deviceMakeMaster_ul').prop('disabled', false);	 
				 }		        
		        $.each(ary, function(index, element) {	
				if(addorupdate =='update'){	        	
					if(element.Value == ""+selectedId+""){
						$('#deviceMakeMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#deviceMakeMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')" selected><a href="#">' + element.DisplayText + '</a></li>');
					}else{
						$('#deviceMakeMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#deviceMakeMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
					}
				}else{
					$('#deviceMakeMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#deviceMakeMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
				}
		       });
		       $('#deviceMakeMaster_ul').select2();
    		}
	});
}

//Listing Platformitems in Drop down for Mobile
function loadMobilePlatformType(addorupdate, selectedId){
	$('#mobileplatformType_ul').empty();
	$.post('common.platformtype.listwithversion',function(data) {		
		if(data.Result=="ERROR"){
			callAlert(data.Message);
  		return false;
  		}else{
		        var ary = (data.Options);
				if(addorupdate == 'add'){
				$('#mobileplatformType_ul').append('<option style="padding-Bottom:28px" id="-1" selected><a href="#">' + '--Select--' + '</a></li>');
				$('#mobileplatformType_ul').prop('disabled', false);	 
				}			    
		        $.each(ary, function(index, element) {		        	
		        	if(addorupdate == 'update'){
						if(element.Value == ""+selectedId+""){
							$('#mobileplatformType_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#mobileplatformType_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')" selected><a href="#">' + element.DisplayText + '</a></li>');
						}else{
							$('#mobileplatformType_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#mobileplatformType_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
						}
					}else{
					$('#mobileplatformType_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#mobileplatformType_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
					}
		       });
		        $('#mobileplatformType_ul').select2();
  		}
	});	
}


//Listing Modelitems in Drop down for Mobile
function loadMobiledeviceModelMaster(addorupdate, selectedId){
	$('#mobiledeviceModelMaster_ul').empty();
	$.post('common.deviceModelMaster.list',function(data) {		
		if(data.Result=="ERROR"){
			callAlert(data.Message);
  		return false;
  		}else{
		        var ary = (data.Options);
		        if(addorupdate =='add'){
					$('#mobiledeviceModelMaster_ul').append('<option style="padding-Bottom:28px" id="-1" selected><a href="#">' + '--Select--' + '</a></li>');
					$('#mobiledeviceModelMaster_ul').prop('disabled', false);	 
				}				
		        $.each(ary, function(index, element) {		        	
		        	if( addorupdate = 'update'){
						if(element.Value == ""+selectedId+""){
							$('#mobiledeviceModelMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#mobiledeviceModelMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')" selected><a href="#">' + element.DisplayText + '</a></li>');
						}else{
							$('#mobiledeviceModelMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#mobiledeviceModelMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
						}
					}else{
						$('#mobiledeviceModelMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#mobiledeviceModelMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
					}					
		       });
		        $('#mobiledeviceModelMaster_ul').select2();
  		}
	});
}
//Listing Platformitems in Drop down for StorageDrive
function loadSDPlatformType(addorupdate, selectedId){
	$('#storageplatformType_ul').empty();
	$.post('common.platformtype.listwithversion',function(data) {		
		if(data.Result=="ERROR"){
			callAlert(data.Message);
  		return false;
  		}else{
		        var ary = (data.Options);
				if(addorupdate == 'add'){
				$('#storageplatformType_ul').append('<option style="padding-Bottom:28px" id="-1" selected><a href="#">' + '--Select--' + '</a></li>');
				$('#storageplatformType_ul').prop('disabled', false);	 
				}			    
		        $.each(ary, function(index, element) {		        	
		        	if(addorupdate == 'update'){
						if(element.Value == ""+selectedId+""){
							$('#storageplatformType_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#storageplatformType_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')" selected><a href="#">' + element.DisplayText + '</a></li>');
						}else{
							$('#storageplatformType_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#storageplatformType_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
						}
					}else{
					$('#storageplatformType_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#storageplatformType_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
					}
		       });
		        $('#storageplatformType_ul').select2();
  		}
	});	
}
//Listing Makeitems in Drop down for StorageDrive
function loadSDDeviceMakeMaster(addorupdate, selectedId){
	$('#sdDeviceMakeMaster_ul').empty();
	$.post('common.deviceMakeMaster.list',function(data) {		
		if(data.Result=="ERROR"){
			callAlert(data.Message);
  		return false;
  		}else{
		        var ary = (data.Options);
				 if(addorupdate =='add'){
					$('#sdDeviceMakeMaster_ul').append('<option style="padding-Bottom:28px" id="-1" selected><a href="#">' + '--Select--' + '</a></li>');
					$('#sdDeviceMakeMaster_ul').prop('disabled', false);	 
				 }		        
		        $.each(ary, function(index, element) {	
				if(addorupdate =='update'){	        	
					if(element.Value == ""+selectedId+""){
						$('#sdDeviceMakeMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#sdDeviceMakeMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')" selected><a href="#">' + element.DisplayText + '</a></li>');
					}else{
						$('#sdDeviceMakeMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#sdDeviceMakeMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
					}
				}else{
					$('#sdDeviceMakeMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#sdDeviceMakeMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
				}
		       });
		       $('#sdDeviceMakeMaster_ul').select2();
  		}
	});
}

//Listing Modelitems in Drop down for StorageDrive
function loadStoragedeviceModelMaster(addorupdate, selectedId){
	$('#storagedeviceModelMaster_ul').empty();
	$.post('common.deviceModelMaster.list',function(data) {		
		if(data.Result=="ERROR"){
			callAlert(data.Message);
		return false;
		}else{
		        var ary = (data.Options);
		        if(addorupdate =='add'){
					$('#storagedeviceModelMaster_ul').append('<option style="padding-Bottom:28px" id="-1" selected><a href="#">' + '--Select--' + '</a></li>');
					$('#storagedeviceModelMaster_ul').prop('disabled', false);	 
				}				
		        $.each(ary, function(index, element) {		        	
		        	if( addorupdate = 'update'){
						if(element.Value == ""+selectedId+""){
							$('#storagedeviceModelMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#storagedeviceModelMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')" selected><a href="#">' + element.DisplayText + '</a></li>');
						}else{
							$('#storagedeviceModelMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#storagedeviceModelMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
						}
					}else{
						$('#storagedeviceModelMaster_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#storagedeviceModelMaster_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
					}					
		       });
		        $('#storagedeviceModelMaster_ul').select2();
		}
	});
}

//Listing items in Drop down from StorageDrive
function loadSDHost(addorupdate, selectedId){
	$('#sdHost_ul').empty();
	$.post('common.host.list.deviceLab?deviceLabId='+treedeviceLabId+'&filter=-1',function(data) {		
		if(data.Result=="ERROR"){
			callAlert(data.Message);
  		return false;
  		}else{
		        var ary = (data.Options);
		        if(addorupdate =='add'){
					$('#sdHost_ul').append('<option style="padding-Bottom:28px" id="-1" selected><a href="#">' + '--Select--' + '</a></li>');
				}				
		        $.each(ary, function(index, element) {		        	
		        	if(addorupdate =='update'){
						if(element.Value == ""+selectedId+""){
							$('#sdHost_ul').append('<option style="padding-Bottom:28px;  width: 200px !important;" id="' + element.Value + '" title="'+element.DisplayText+'" onclick="setTitle(\'#sdHost_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')" selected><a href="#">' + element.DisplayText + '</a></li>');
						}else{
							$('#sdHost_ul').append('<option style="padding-Bottom:28px;  width: 200px !important;" id="' + element.Value + '" title="'+element.DisplayText+'" onclick="setTitle(\'#sdHost_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
						}
					}else{
						$('#sdHost_ul').append('<option style="padding-Bottom:28px;  width: 200px !important;" id="' + element.Value + '" title="'+element.DisplayText+'" onclick="setTitle(\'#sdHost_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
					}
					
				});
		        $('#sdHost_ul').select2();
  		}
	});
}


//Listing items in Drop down from StorageDrive
function loadDriveSystemProcessor(addorupdate, selectedId){
	$('#sdProcessor_ul').empty();
	$.post('common.systemprocessor.list',function(data) {		
		if(data.Result=="ERROR"){
			callAlert(data.Message);
  		return false;
  		}else{
		        var ary = (data.Options);
				if(addorupdate =='add'){
					$('#sdProcessor_ul').append('<option style="padding-Bottom:28px" id="-1" selected><a href="#">' + '--Select--' + '</a></li>');
					$('#sdProcessor_ul').prop('disabled', false);	 
				}		        
		        $.each(ary, function(index, element) {	
					if(addorupdate =='update'){
						if(element.Value == ""+selectedId+""){						
							$('#sdProcessor_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#sdProcessor_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')" selected><a href="#">' + element.DisplayText + '</a></li>');
						}else{
							$('#sdProcessor_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#sdProcessor_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
						}
					}else{
						$('#sdProcessor_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#sdProcessor_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
					}
		       });
		       $('#sdProcessor_ul').select2();
  		}
	});
}

//Listing items in Drop down from Server
function loadSystemType(addorupdate, selectedId){
	$('#servSysType_ul').empty();
	$.post('common.systemType.list',function(data) {		
		if(data.Result=="ERROR"){
			callAlert(data.Message);
    		return false;
    		}else{
		        var ary = (data.Options);
				if(addorupdate =='add'){
					$('#servSysType_ul').append('<option style="padding-Bottom:28px" id="-1" selected><a href="#">' + '--Select--' + '</a></li>');
					$('#servSysType_ul').prop('disabled', false);	 
				}
		        
		        $.each(ary, function(index, element) {	
					if(addorupdate =='update'){
						if(element.Value == ""+selectedId+""){
							$('#servSysType_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#servSysType_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')" selected><a href="#">' + element.DisplayText + '</a></li>');
						}else{
							$('#servSysType_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#servSysType_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
						}
					}else{
						$('#servSysType_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#servSysType_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
					}
		       });
		       $('#servSysType_ul').select2();
    		}
	});
}

//Listing items in Drop down from Server
function loadSystemProcessor(addorupdate, selectedId){
	$('#servProcessor_ul').empty();
	$.post('common.systemprocessor.list',function(data) {		
		if(data.Result=="ERROR"){
			callAlert(data.Message);
    		return false;
    		}else{
		        var ary = (data.Options);
				if(addorupdate =='add'){
					$('#servProcessor_ul').append('<option style="padding-Bottom:28px" id="-1" selected><a href="#">' + '--Select--' + '</a></li>');
					$('#servProcessor_ul').prop('disabled', false);	 
				}		        
		        $.each(ary, function(index, element) {	
					if(addorupdate =='update'){
						if(element.Value == ""+selectedId+""){						
							$('#servProcessor_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#servProcessor_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')" selected><a href="#">' + element.DisplayText + '</a></li>');
						}else{
							$('#servProcessor_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#servProcessor_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
						}
					}else{
						$('#servProcessor_ul').append('<option style="padding-Bottom:28px" id="' + element.Value + '" onclick="setTitle(\'#servProcessor_dd\',\''+element.Value+'\',\''+element.DisplayText+'\')"><a href="#">' + element.DisplayText + '</a></li>');
					}
		       });
		       $('#servProcessor_ul').select2();
    		}
	});
}

//Hiding and UnHiding Div for Device Type
function showDeviceTypeDetails(devType){
	
	if(devType == 4){
		document.getElementById("serverTypeDiv").style.display = "block";
		document.getElementById("mobileTypeDiv").style.display = "none";
		document.getElementById("storageDriveTypeDiv").style.display = "none";		
	}
	if(devType == 5){
		document.getElementById("mobileTypeDiv").style.display = "block";
		document.getElementById("serverTypeDiv").style.display = "none";
		document.getElementById("storageDriveTypeDiv").style.display = "none";
	}
	if(devType == 6){
		document.getElementById("storageDriveTypeDiv").style.display = "block";
		document.getElementById("serverTypeDiv").style.display = "none";
		document.getElementById("mobileTypeDiv").style.display = "none";
	}
}
//Diplay PlatformVersion based on PlatFormType
function showPlatFormVersions(platformTypeId, platformTypeName, devType){
	//help
	var url = 'getPlatFormVersion.by.platformId?platformId='+platformTypeId;			
		$.ajax({
			 type: "POST",
			    dataType : 'json',
			    url : url,
			    success: function(data) {
			    	var platformTyeVersionData=eval(data);
					var platformVersion =  platformTyeVersionData[0].platformVersion;
					if(devType == "server"){
						$('#platformTypeVersion').val(platformVersion);
						$('#platformTypeVersion').prop('disabled', false);
					}else if(devType == "mobile"){
						$('#mobileplatformTypeVersion').val(platformVersion);
						$('#mobileplatformTypeVersion').prop('disabled', false);
					}else if(devType == "storageDrive"){
						$('#storageplatformTypeVersion').val(platformVersion);
						$('#storageplatformTypeVersion').prop('disabled', false);
					}
					//platformTypeVersion					
			    }
		});
}
/* END Devices */
function filterAllDeviceTiles(obj)
{
	statusId = $(obj).attr("data-status");
	typeId = $(obj).attr("data-type");	
	if (statusId == undefined)
	    statusId= $("#devstatus").find('label.active').attr("data-status");
	if (typeId == undefined)
	  	typeId= $("#devtype").find('label.active').attr("data-type");		    	
	showAllDeviceTiles();
}

 function showAllDeviceTiles(){	
	if(typeId==1){
		urlToGetDevicesOfSpecifiedProductId= 'admin.genericDevices.list.deviceLab?deviceLabId='+treedeviceLabId+'&filter='+statusId;
		flag=1;
		devicesNew(urlToGetDevicesOfSpecifiedProductId);	
	}else if(typeId==2){
		urlToGetHost='administration.host.list.deviceLab?deviceLabId='+treedeviceLabId+'&filter='+statusId;
		flag=2;
		hostNew(urlToGetHost);	
	}else if(typeId==3){
		urlToGetStorageDriveOfSpecifiedProductId='admin.genericDevices.storageDrive.list.deviceLab?deviceLabId='+treedeviceLabId+'&deviceTypeId=6'+'&filter='+statusId;
		flag=3;
		devicesNew(urlToGetStorageDriveOfSpecifiedProductId);	
	}	
}

</script>

<!-- END JAVASCRIPTS -->
<div><jsp:include page="dataTableFooter.jsp"></jsp:include></div>
</body>
<!-- END BODY -->
</html>