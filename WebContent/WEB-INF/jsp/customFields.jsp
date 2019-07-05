<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:eval var="seriesCustomFieldDisplayName" expression="@ilcmProps.getProperty('SERIES_CUSTOM_FIELD_DISPLAY_NAME')" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
	<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet"	type="text/css"></link>
	<link rel="stylesheet" type="text/css" href="css/bootstrap-datetimepicker.min.css"></link>
</head>

<body>
<!-- started popup -->

<div id="customFieldGroupId" class="modal" tabindex="-1" aria-hidden="true" style="padding: 0px;z-index: 100051;">
	<div class="modal-full">
		<div class="modal-content">
			<div id="customFieldValuesLoaderIcon" style="display:none;z-index:100001;position:absolute;top:43.5%;left:45%">
				<img src="css/images/ajax-loader.gif"/>
			</div>
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title theme-font"></h4>					
					<div class="row">
						<div class="col-md-9" style="padding-left: 5px;">
							<h5 class="modal-title" style="padding-top: 5px;float: left;"> </h5>							
						</div>						 					
				</div>					
			</div>
			<div class="modal-body">
				<div id="customFieldLoaderIcon" style="display:none;z-index:100001;position:absolute;top:3%;left:48%">
					<img src="css/images/ajax-loader.gif"/>
				</div>
				<div class="row">
					<div class="col-md-12">
						<div class="radio-toolbar" style="float:left; padding-right:5px;">								
							<div class="btn-group" data-toggle="buttons" style="width:100%">
							<!-- 	<label class="btn darkblue active" data-name="SingleFrequency" onclick="customFieldSingleFrequancyHandler();">
								<input type="radio" class="toggle">Single</label> -->
								<label class="btn darkblue active" data-name="SeriesFrequency" onclick="customFieldSeriesFrequancyHandler();">
								<!-- <input type="radio" class="toggle">Series</label> -->
								<input type="radio" class="toggle">${seriesCustomFieldDisplayName}</label>								
								
							</div>
						</div>
					</div>
				</div>
				<div class="row" id="customFieldSingleFrequencyID"></div>
				<div class="row" id="customFieldMultiFrequencyID"></div>					
			</div>
		</div>
	</div>
</div>

<div id="div_allInstanceCustomFieldPopup" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
	<div class="modal-full">
		<div class="modal-content">
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title theme-font">Custom Fields</h4>
			</div>
			<div class="modal-body">					
				<div id="entityType" style="display: none;">
					<div class="col-md-4">
						<div class="col-md-4" style="margin-top: 8px; text-align: right;"> Entity Type </div>  
	      				<div id="entityTypeList_dd">	
		      				<select class="form-control input-small select2me" id="entityTypeList_ul">
							</select>	
						</div>
					</div>
				</div>
				<div class="clearfix"></div>
				<div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
					<div id="allInstanceCustomField"></div>
				</div>				 
			</div>
		</div>
	</div>
</div>

<div id="customFieldSingleGroupId" class="modal" tabindex="-1" aria-hidden="true" style="padding: 0px; z-index: 100052;">
	<div class="modal-full">
		<div class="modal-content">
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title" style="color: black; font-size: 16px; padding-top: 3px; padding-bottom: 3px;"></h4>					
					<div class="row">
						<div class="col-md-9" style="padding-left: 5px;">
							<h5 class="modal-title" style="padding-top: 5px;float: left;"> </h5>							
						</div>						 					
				</div>					
			</div>
			<div class="modal-body" style="padding:5px">
				<div id="customFieldLoaderIcon" style="display:none;z-index:100001;position:absolute;top:3%;left:48%">
					<img src="css/images/ajax-loader.gif"/>
				</div>
				<div class="row" id="customFieldSingleContainerID"></div>
			</div>
		</div>
	</div>
</div>

<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>

<script src="js/viewScript/customFields.js" type="text/javascript"></script>
<script src="js/viewScript/customFieldsForAllInstanceOfEntity.js" type="text/javascript"></script>
<script src="js/viewScript/customFieldSingle.js" type="text/javascript"></script>

</body>
<!-- END BODY -->
</html>