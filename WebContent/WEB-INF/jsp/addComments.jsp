<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>

<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet"	type="text/css"></link>

<style>
.datetimepicker-minutes{
	height: 200px;
	overflow-y: scroll;
}
</style>
</head>
<body>

<div id="addCommentsMainDiv" class="modal " tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%;">
	<div class="modal-full">
		<div class="modal-content" style="min-height: 600px;">
			<div id="addCommentsLoaderIcon" style="display:none;z-index:100001;position:absolute;top:43.5%;left:45%">
				<img src="css/images/ajax-loader.gif"/>
			</div>
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="closeAddCommentsPopup()" aria-hidden="true"></button>
				<h4 class="modal-title">Workflow </h4>
			</div>
			<div class="modal-body">							
				<div>				
				<div id="addComments" style="width:100%;">
				   <div class="scroller" style="height:95%;overflow-y:scroll;" data-always-visible="1" data-rail-visible1="1">				    
					<form  class="form-horizontal" name="frmProfile" enctype="multipart/form-data" >
					<div class="form-body">
						<div style="width:50%; float:left;">
							<div class="form-group">
								<div class="col-md-4">
									<label for="input" class="control-label">Current Status:</label>
								</div>
								<div class="col-md-6">												
									<span id="effortCurrentStatus"></span><span id="currentStatusCategory" class="statusCategory"></span>		
								</div>
							</div>								
							<div class="form-group">
								<div class="col-md-4">
									<label for="input" class="control-label">Change to Status:</label>
								</div>
								<div class="col-md-6">												
									<select class="form-control input-small select2me" name="activityStatus_ul" id="activityStatus_ul" data-placeholder="Select..." style="display: inline-block;"></select>
									<span id="targetStatusCategory" class="statusCategory"></span>							
								</div>
							</div>	
							  
							<div class="form-group" style="display: none;">
								<div class="col-md-4">
									<label for="input" class="control-label ">Secondary Status:</label>
								</div>
								<div class="col-md-6">												
									<select class="form-control select2me" name="secondaryStatus_ul" id="secondaryStatus_ul" data-placeholder="Select..."  style="width:250px !important;"></select>							
								</div>
							</div>
							
							<div class="form-group">
								<div class="col-md-4">
									<label class="control-label">Add Attachment: </label><span style="cursor: pointer;"><img src="css/images/attachment.png" title="Attachment" onclick="addAttachment()" style="width: 24px;height: 24px;"></span>
								</div>
								<div class="col-md-6">												
																
								</div>
							</div>
						</div>
						
						<div style="width:50%; float:right;">
							 <div class="form-group" id="addCommentsDateTimePickerBox" style="display:none">		
							  <div class="col-md-4"> Date :</div> 
    						   <div class="col-md-4">        						  
            						  <div class="input-group" id="planned_defaultrange">	                						 
	            						  <input type="text" class="form-control" id="planned_fromTodatepicker1" readonly style="background-color:white"/>
            						  </div>        						  
    						   </div> 
						  	 </div>
							<div class="form-group">
								<div class="col-md-4">
									<label for="input" class="control-label ">Effort(in hr):</label>
								</div>
								<div class="col-md-6">							
									<input type='text' id="effort" class="form-control" rows="1" style="resize:none;" ></input>																		
								</div>
								<div class="col-md-2">
									<button type="button" id="startSet" class="btn green-haze" onclick="updateEffortHandler();">update</button>
								</div>
							</div>	
							
							<div class="form-group" id="eventEffortActualSizeDiv">
								<div class="col-md-4">
									<label for="input" class="control-label ">Actual size:</label>
								</div>
								<div class="col-md-6">							
									<input type='text' id="actualSize" class="form-control" rows="1" style="resize:none;" ></input>									
								</div>
							</div>	
							
							<div class="form-group">
								<div class="col-md-4">
									<label for="input" class="control-label ">Comments :</label>
								</div>
								<div class="col-md-6">							
									<textarea id="comments" class="form-control" rows="3" style="resize:none;" placeholder="Comments..."></textarea>									
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-8">									
								</div>
								<div class="col-md-4">							
									<button type="button" id="startSet" class="btn green-haze" onclick="addCommentsHandler();">Save</button>
									<input id="addCommentsCancel" type="button" data-dismiss="modal" class="btn grey-cascade" value="Cancel" onclick="addCommentsMainDivClose()">									
								</div>
							</div>
						</div>						
				</div>				
			 </form>				
				</div>
			</div>	
			<div id="viewComments" style="width:100%;">
  				<div id="jTableContainerComments"></div>									
			</div>
				</div>
				
			</div>
		</div>
	</div>
</div>

<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>
<script src="js/viewScript/addComments.js" type="text/javascript"></script>
<script src="js/viewScript/activityListingGroupingDT.js" type="text/javascript"></script>
<script src="js/viewScript/productManagementFeature.js" type="text/javascript"></script>

<div><%@include file="attachments.jsp" %></div>

</body>
</html>