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
<div id="atsgHelpContainer" class="modal " tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 5%; padding: 0px;">
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
				<div class="modal-body" style="min-height: 450px;">
					<div class="row">
						<div class="col-md-6">
							<div class="radio-toolbar style="float:left; padding-right:5px;">								
								<div class=" btn-group" data-toggle="buttons" style="width:100%" id="codeEditorRadiosBDD">
									<label class="btn darkblue" data-name="testCases" onclick="listBDDKeywords();">
									<input type="radio" class="toggle" >BDD Keyword Phrases</label>
									<label class="btn darkblue" data-name="testSteps" onclick="objectRepositoryContent();">
									<input type="radio" class="toggle" >Object Repository</label>
									<label class="btn darkblue" data-name="testScript" onclick="testScriptAttachmnetFileContent();">
									<input type="radio" class="toggle" >Test Data</label>
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
					
				
				<!-- <div class="jScrollContainerResponsive jScrollContainerFullScreen" >
										ContainerBox
										<div class="jScrollContainerResponsive" style="clear:both;padding-top:10px">
											<div id="JtableScriptFileContainer"></div>
											<div id="JtableTestDataFileFileContainer"></div>
										</div>		
			</div> -->
			<!-- <div class="jScrollContainerResponsive jScrollContainerFullScreen" >
										ContainerBox
										<div class="jScrollContainerResponsive" style="clear:both;padding-top:10px">
											<div id="JtableTestDataFileFileContainer"></div>
										</div>		
			</div> -->
			</div>
	</div>
</div>
<!-- ended popup -->
<script src="js/singleJtableContainer.js" type="text/javascript"></script>
<script src="js/viewScript/atsgHelp.js" type="text/javascript"></script>
</body>
</html>