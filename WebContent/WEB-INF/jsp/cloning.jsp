<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>

<!-- <link href="css/bootstrap-select.min.css" rel="stylesheet" type="text/css"></link> -->
<link href="css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css"></link>
<link href="css/daterangepicker-bs3.css" rel="stylesheet" type="text/css"></link>
<!-- <link href="css/jquery.nestable.css" rel="stylesheet" type="text/css"></link> -->
</head>
<!-- END HEAD -->
<body>
<div id="cloningContainerID" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true" 
						onclick="closeCloningContainer()"></button>
					<h4 class="modal-title theme-font"></h4>
					
					<div class="row" style="display:none">
						<div class="col-md-9" style="padding-left: 5px;">
							<h5 class="modal-title" style="padding-top: 5px;"></h5>
						</div>
					</div>					
				</div>
				<div class="modal-body">				    
							<div class="row list-separated" >	
								<!-- Main -->
								<div class="col-md-6">
									  <div class="col-md-12">
											 <div class="form-group">
											    <label>Activity Name:</label>							  
												<input id="cloning_name" class="form-control input-medium" type="text" name="cloning_name" />
											</div>											
											<div class="form-group">
												<!-- <label >Planned Duration:</label> -->
												<!-- <div class="input-group" id="cloning_defaultrange">								
													<input type="text" class="form-control"/>
													<span class="input-group-btn">
														<button class="btn default date-range-toggle" type="button" style="height:34px"><i class="fa fa-calendar"></i></button>
													</span>
												</div> -->
												
												
												<div class="row" id="plannedDateDiv">												
													<div class="form-group col-lg-4">
														 <div class="">
														  <label  >Planned Start Date:</label>
														  <input id="cloningStartDate" NAME="cloningStartDate" class="form-control input-small  date-picker"  />
														</div>
													</div>
													<div class="form-group col-lg-6">
														<div class="">
														  <label  >Planned End Date:</label>
														  <input id="cloningEndDate" NAME="cloningEndDate" class="form-control input-small  date-picker"  />
														</div>
													</div>													 													
												</div>																								
											</div>											
											<div class="form-group">
											    <label>Description:</label>
											    <div>
											    	<textarea id="cloning_desc" class="form-control" name="cloning_desc" rows="3"></textarea> 
											    </div>
											</div>
										</div>
								</div>
								<div class="col-md-6">								
									<div class="col-md-12" style="max-height: 450px;">
										  <!-- Testing -->
										  
										  <div class="portlet box blue">
											<div class="portlet-title">
												<div class="caption">Select Build</div>												
											</div>
											<div class="portlet-body"  style="max-height: 400px;overflow-y: auto;">
											   <div id="cloneTree"></div>
										  </div>
										  </div>
										  <!-- Testing -->
									</div>
							    <!-- End Main -->
								</div>							
							</div>
					<div class="row">
						<div id="savebutton">
							<div class="col-md-12">
								<div class="form-group">
								<div class="col-md-6">
								   <button type="button" id="save" class="btn green-haze" onclick="saveWorkpackageDetail()">Submit</button>
								 </div>
								</div>
							</div>
						</div>
					</div>
			</div>
		</div>
	</div>
</div>
<div id="cloningLoaderIcon" style="display:none;z-index:100001;position:absolute;top:40%;left:45%">
	<img src="css/images/ajax-loader.gif"/>
</div>
<!-- END JAVASCRIPTS -->
<script src="js/select2.min.js" type="text/javascript"></script>
<script src="js/bootstrap-select.min.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="js/moment.min.js" type="text/javascript"></script>
<script src="js/daterangepicker.js" type="text/javascript"></script>
<script src="js/components-pickers.js" type="text/javascript"></script>
<!-- <script src="js/jquery.nestable.js" type="text/javascript"></script>
<script src="js/ui-nestable.js" type="text/javascript"></script> -->

<script src="files/lib/metronic/theme/assets/global/plugins/jstree/dist/jstree.min.js"
		type="text/javascript"></script>

<script src="js/viewScript/cloning.js" type="text/javascript"></script>

</body>
<!-- END BODY -->
</html>