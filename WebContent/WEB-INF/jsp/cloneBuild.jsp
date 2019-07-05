<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>

<style type="text/css">

.clone-build-error{
	display:none;
	text-align:right;
	color:red;
}

</style>



</head>

<body>	
<!-- started popup -->

<div id="divPopUpCloneBuild" class="modal " tabindex="-1" aria-hidden="true" style="width: 30%;left: 35%;top: 20%;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true" onclick="popUpCloneBuildclose()"></button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body">	
					<div class="row" style="padding-bottom: 5px;">
					<div class="form-group">
						<div class="col-md-6">
							<label class=" control-label">Build No. <font color="#F5A9A9" size="4px">*</font></label>
						</div>
						<div class="col-md-6">
							<div><input id="build_no"  class="form-control " type="text" NAME="build_no"/>																	
							</div>	
						</div>
						<div class="col-md-12 clone-build-error" id="build_no_err"><span class="col-md-6"></span><span class="col-md-6">Please Enter Build No.</span></div>
					</div>
					</div>
					<div class="row" style="padding-bottom: 5px;">
					<div class="form-group">
						<div class="col-md-6">
							<label class=" control-label">Build Name <font color="#F5A9A9" size="4px">*</font></label>
						</div>
						<div class="col-md-6">
							<div><input id="build_name"  class="form-control " type="text" NAME="build_name"/> 
																	
							</div>	
						</div>
						<div class="col-md-12 clone-build-error" id="build_name_err"><span class="col-md-6"></span><span class="col-md-6">Please Enter Build Name</span></div>
					</div>
					</div>
					<div class="row" style="padding-bottom: 5px;">
					<div class="form-group">
						<div class="col-md-6">
							<label class=" control-label">Description</label>
						</div>
						<div class="col-md-6">
							<div><input id="build_des"  class="form-control " type="text" NAME="build_des"/> 
																	
							</div>	
						</div>
					</div>
					</div>
					<div class="row" style="padding-top: 15px;">
						<div class="col-md-6">
							<label class=" control-label">Build Type</label>
						</div>
						<div class="col-md-6">
							<select name="build_clone_options" class="form-control input-small  select2me" id="buildType_ul">						        					   			
							</select>				
					</div>
					</div>
					<div class="row" style="padding-top: 15px;">
						<div class="col-md-6">
							<label class=" control-label">Build Date</label>
						</div>
						<div class="col-md-6">
							<input class="form-control input-small  date-picker" id="datePickerBuildDate" size="10" type="text" value="" />				
					</div>
					</div>					

					<div class="row" style="padding-top: 15px;">
						<div class="col-md-12">
							<div class="col-md-6"></div>
							<div class="col-md-3">
								<input type="button"  nwsaveas class="btn green-haze" onclick="saveCloneBuild()" value="Save">		
							</div>
							<div class="col-md-3">
								<input type="button" data-dismiss="modal" class="btn grey-cascade" value="Cancel" onclick="popUpCloneBuildclose()">		
							</div>	
						</div>	
					</div>
				</div>
			</div>
	</div>
</div>

<!-- ended popup -->
<script type="text/javascript" src="js/cloneBuild.js"></script>
</body>

</html>