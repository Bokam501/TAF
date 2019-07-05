<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title></title>
</head>

<body>
<!-- started popup -->
<div id="atsgContainer" class="modal " tabindex="-1" aria-hidden="true" style="width: 50%;left: 25%;top: 2%; padding: 0px;z-index:10054;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true" onclick="closeATSGContainer()"></button>
					<h4 class="modal-title theme-font"></h4>					
						<div class="row">
						<div class="col-md-9" style="padding-left: 5px;">
							<h5 class="modal-title" style="padding-top: 5px;"></h5>
						</div>						
					</div>					
				</div>
				<div class="modal-body" style="min-height: 430px;">
					<div id="atsgLoaderIcon" style="display:none;z-index:100001;position:absolute;top:40%;left:45%">
						<img src="css/images/ajax-loader.gif"/>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="radio-toolbar style="float:left; padding-right:5px;">								
								<div class="btn-group" data-toggle="buttons" style="width:100%" id="codeEditorRadiosBDD">
									<label class="btn darkblue hidden" data-name="testCases" onclick="boilerPlateCodeHandler();">
									<input type="radio" class="toggle" >Boiler Plate Code</label>
									<label class="btn darkblue" data-name="testSteps" onclick='bddScriptHandler("ADD");'>
									<input type="radio" class="toggle" >BDD Test Case Story</label>
									<!-- <label class="btn darkblue" data-name="testScript" onclick="bddScriptGenerationHandler();">
									<input type="radio" class="toggle" >Script Generation</label> -->
								</div>
							</div>
						</div>
					</div>					
			    <!--<div>
					 <div class="row">
						<div class="col-md-8"></div>
						<div class="col-md-4" style="padding-top: 5px;">
						 <button type="button" id="codeEditorBDDSave" class="btn green-haze" onclick="codeEditorBDDSaveHandler();">Save</button>
						 <button type="button"  class="btn green-haze" onclick="codeEditorBDDExecuteHandler();">Execute</button> 
						 <input id="codeEditorBDDCancel" type="button" data-dismiss="modal" class="btn grey-cascade" value="Cancel" onclick="codeEditorBDDCancelHandler()">
						 <input type="button" data-dismiss="modal" class="btn grey-cascade" value="Close" onclick="codeEditorBDDCancelHandler()"></div>
					</div> -->
					<!-- <div style="float:right;">
						 <button type="button" id="startSet" class="btn green-haze" onclick="addCommentsHandler();">Save</button>
						 <input id="addCommentsCancel" type="button" data-dismiss="modal" class="btn grey-cascade" value="Cancel" onclick="addCommentsMainDivClose()">
					</div> 
					</div>	-->
					
				</div>
			</div>
	</div>
</div>
<!-- ended popup -->

<script src="js/viewScript/atsg.js" type="text/javascript"></script>
<script src="js/viewScript/atsgEditorCommon.js" type="text/javascript"></script>
</body>
</html>