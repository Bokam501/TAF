<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<link href="files/lib/metronic/theme/assets/global/plugins/icheck/skins/all.css" rel="stylesheet" type="text/css" />

</head>
<body>
	
	<div id="div_generateScript" class="modal " data-backdrop="static" tabindex="-1" aria-hidden="true" style="z-index:10051;">
		<div class="modal-dialog" style="width:300px;">
			<div class="modal-content" style="height:530px;width:160%">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="closeGenerateScriptHandler()" aria-hidden="true"></button>
					<h4 class="modal-title">Title</h4>
				</div>
				<div class="modal-body">
					<div class="portlet light">								
						<div class="portlet-body">
							 <div id="generateScriptLoaderIcon" style="display:none;z-index:100001;position:absolute;top:33%;left:45%"> 
								<img src="css/images/ajax-loader.gif"/>
							</div>
							
						   <div class="skin skin-flat">
								<form role="form">
									<div class="form-body">	
									<div class="form-group hidden testTollMaster_bdd_multiple_testcase_container">
										<div style="display:inline-block;margin-right:20px;">Test Engine: </div>
										<div id="testTollMaster_bdd_multiple_dd" style="display:inline-block;">
											<select class="input-medium select2me" id="testTollMaster_bdd_multiple_ul" >							        				   			
												<option  value="SELENIUM" ><a href="#">SELENIUM</a></option>
												<option  value="APPIUM" ><a href="#">APPIUM</a></option>			   			
												<option  value="PROTRACTOR" ><a href="#">PROTRACTOR</a></option>			   			
												<option  value="SEETEST" ><a href="#">SEETEST</a></option>
												<option  value="EDAT" ><a href="#">EDAT</a></option>
												<option  value="CODEDUI" ><a href="#">CODED UI</a></option>
												<option  value="AUTOIT" ><a href="#">AUTO IT</a></option>
												<option  value="TESTCOMPLETE" ><a href="#">TEST COMPLETE</a></option>			   			
											</select>									
										</div>	
						 			</div>
									 <div class="form-group">
									 	<label style="float: left;margin-right:26px;">Language  :	 </label>													
											<div id="scriptlanguage_id">											
												<select class="form-control input-medium select2me" id="scriptlanguage_ul">																			
												</select>																				
											</div>	
										</div> 	
										<div class="form-group" id="packageDiv">
											<label style="float: left;margin-right: 20px;">Package<font color="#efd125" size="4px"> * </font>: </label>
											<input type="text" id ="package" class="form-control input-medium" value = "com.hcl.ers.atsg.script.output">							
										</div>
										<div class="form-group" >													
											<div class="input-group">
											<label>Mode  : </label>
												<div id="radioHandlerBtn" class="icheck-list">
													<label><input type="radio" name="radio1" id="ATT-MODE" data-radio="iradio_flat-grey" radio-val="ATT" style = "display:show(); onclick= "hideOptions(1)">AT&T</label>
													<label><input type="radio" name="radio1" checked id="GENERIC-MODE" data-radio="iradio_flat-grey" radio-val="Generic" onclick="displayingOptions(1)">GENERIC</label>
													<label><input type="radio" name="radio1" checked  id="TAF-MODE" data-radio="iradio_flat-grey" radio-val="TAF-MODE" onclick="displayingOptions(1)">TAF</label> 
													<label><input type="radio" name="radio1" id="SCRIPTLESS" data-radio="iradio_flat-grey" radio-val="SCRIPTLESS" style = "display:show(); onclick= "hideOptions(1)">Scriptless</label>
													<label><input type="radio" name="radio1" id="TE-MODE" data-radio="iradio_flat-grey" radio-val="Test Execution" style = "display:show(); onclick= "hideOptions(1)">Test Execution</label>
												</div>	 
											</div>
										</div>	
										<div class="form-group" >													
											<div class="input-group" id="mainDiv">
											<label>Script Structure  : </label>
												<div id="radioHandlerBtn1" class="icheck-list">
													<label><input type="radio" name="radio2" id="TEST_METHOD_PER_STEP" data-radio="iradio_flat-grey" onclick= "hideOptions(2)">Single Method Per Step</label>
													<label><input type="radio" name="radio2" checked id="TEST_METHOD_SINGLE_STORY" data-radio="iradio_flat-grey" onclick= "hideOptions(2)">Single Method Per Story</label>
													<!-- <label><input type="radio" name="radio2"  id="SCENARIO_LEVEL" data-radio="iradio_flat-grey" onclick="displayingOptions(2)">Scenario Level</label>
													<div id="radioHandlerBtn2" class="icheck-list" style = "display:none; margin-left: 22px;" >
													<label><input type="radio" name="radio3" checked id="NEW_METHOD" data-radio="iradio_flat-grey">New Method</label>
													<label><input type="radio" name="radio3"  id="NEW_CLASS" data-radio="iradio_flat-grey" >New Class</label> -->
												</div>	
												</div>	 
											</div>
										</div>			    								
										<div class="form-group text-center">
											<button type="button" class="btn grey-cascade" onclick="closeGenerateScriptHandler()">Cancel</button>
											<button type="button" id="submitButton" onclick="submitRadioBtnHandler();" class="btn green-haze">Submit</button>
										</div>
									</div>									
								</form>
							</div>  
						</div>
					</div>
					</div>
			</div>
		</div>
	</div>
	
	<div id="div_uiobjectExportPopup" class="modal " data-backdrop="static" tabindex="-1" aria-hidden="true">
		<div class="modal-dialog" style="width:300px;">
			<div class="modal-content" style="height:330px;width:160%">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" onclick="closeGenerateScriptHandler()" aria-hidden="true"></button>
					<h4 class="modal-title">Title</h4>
				</div>
				<div class="modal-body">
					<div class="portlet light">								
						<div class="portlet-body">
							 <div id="generateScriptLoaderIcon" style="display:none;z-index:100001;position:absolute;top:33%;left:45%"> 
								<img src="css/images/ajax-loader.gif"/>
							</div>
							
						   <div class="skin skin-flat">
								<form role="form">
									<div class="form-body">	
										<div class="form-group" >													
											<div class="input-group">
											<label>Mode  : </label>
												<div id="exportRadioHandlerBtn" class="icheck-list">
													 <!-- <label><input type="radio" name="radioExport" checked id="handle" value="handle" data-radio="iradio_flat-grey">Separate file for each handle</label> --> 
													<label><input type="radio" name="radioExport" checked id="withouthandle" value="withouthandle" data-radio="iradio_flat-grey">Single file</label>
												</div>	 
												<div class="fileHandler_dd hidden" style="margin-top:10px;">
													<div style="margin:7px 7px 0px 0px;float:left;"><label>Testdata Plan  : </label></div>
													<select class="form-control input-small select2me" id="fileHandler_ul">
														<option id = "-1"  selected="selected">ALL</option>
													</select>
												</div>												
											</div>
										</div>	
										<div class="form-group" style="padding-left: 55px;">
										<button type="button" class="btn grey-cascade" onclick="closeGenerateScriptHandler()">Cancel</button>
											<button type="button" onclick="exportSubmitBtnHandler();" class="btn green-haze">Submit</button>
											
										</div>
									</div>									
								</form>
							</div>  
						</div>
					</div>
					</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">	
	
	function closeGenerateScriptHandler(){		
		$("#div_generateScript, #div_uiobjectExportPopup").modal("hide");
		testCaseIdArr = [];
		testEngineArr = [];
		$('.multiTestcaseCheckbox input').prop('checked',false);
		//$("#testTollMaster_bdd_ul").empty();
	}	
	function displayingOptions(temp){		
		if(temp == 1){
			if("PROTRACTOR" != $("#testTollMaster_bdd_ul").find('option:selected').val()){				
				//$("#mainDiv").show();
			}
		}else if(temp == 2){
		 $("#radioHandlerBtn2").show();
		}else{}
		
		var currMode = event.target.getAttribute('id');
		if(currMode=="TAF-MODE"){
			$("#package").val('com.hcl.taf');
			$('#package').attr('readonly','true');
		}else {
			$("#package").val('com.hcl.ers.atsg.script.output');
			$('#package').attr('readonly',false);
		}
	 }
	
	function hideOptions(temp){
		if(temp == 1){
			$("#mainDiv").hide();
		}else if(temp == 2){
		 	$("#radioHandlerBtn2").hide();
		}else{}
	 }	

	//BEGIN: Export Popup
	$('#exportRadioHandlerBtn #handle').click(function(el){
		//console.log("handle=",$(el.target).attr('id'));
	});
	$('#exportRadioHandlerBtn #withouthandle').click(function(el){
		//console.log("withouthandle=",$(el.target).attr('id'));
	});
	function exportSubmitBtnHandler(){
		var selectedRadioValue = $('#exportRadioHandlerBtn input[name=radioExport]:checked').val();
		var testdataPlanSelectedValue = $("#fileHandler_ul").find('option:selected').attr('id');
		
		$("#div_uiobjectExportPopup").modal("hide");
		uiObjectExport(selectedRadioValue,testdataPlanSelectedValue);
	}
	
	
		
	//END: Export Popup
	
	</script>	
</body>
</html>