<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>

<link rel=stylesheet href="files/lib/CodeMirror-master/lib/codemirror.css">

<script type="text/javascript" src="files/lib/CodeMirror-master/lib/codemirror.js"></script>
<script type="text/javascript" src="files/lib/CodeMirror-master/mode/xml/xml.js"></script>
<script type="text/javascript" src="files/lib/CodeMirror-master/mode/javascript/javascript.js"></script>
<script type="text/javascript" src="files/lib/CodeMirror-master/mode/css/css.js"></script>
<script type="text/javascript" src="files/lib/CodeMirror-master/mode/htmlmixed/htmlmixed.js"></script>
<script type="text/javascript" src="files/lib/CodeMirror-master/mode/clike/clike.js"></script>
<script type="text/javascript" src="files/lib/CodeMirror-master/addon/edit/matchbrackets.js"></script>
<script src="files/lib/CodeMirror-master/mode/gherkin/gherkin.min.js" type="text/javascript"></script>
<script src="files/lib/CodeMirror-master/mode/gherkin/gherkin.js"  charset="UTF-8" type="text/javascript"></script>
<script type="text/javascript" src="files/lib/CodeMirror-master/lib/util/formatting.js"></script>
<script type="text/javascript" src="js/codeEditor.js"></script>

<style>
  .CodeMirror { height: auto; border: 1px solid #ddd; }
  /* .CodeMirror-scroll { max-height: 380px; } */
  .CodeMirror pre { padding-left: 7px; line-height: 1.25; }
</style>
</head>

<body>	

<!-- started popup -->
<div id="codeMirrorTextEditor" class="modal " tabindex="-1" aria-hidden="true" style="width: 92%;left: 3%;top: 5%;padding: 0px;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true" onclick="closeCodeMirrorTextEditor()"></button>
					<h4 class="modal-title theme-font"></h4>
					
					<div class="row">
						<div class="col-md-9" style="padding-left: 5px;">
							<h5 class="modal-title" style="padding-top: 5px;float: left;"></h5>
							<h6><span id="scriptBadgeLineNumber" class="badge" style="background: #4db3a4 !important;width: 100px;padding-top: 3px;margin-top: -10px;margin-left: 95px;">2000</span></h6>
						</div>
						<div class="col-md-3">
							<div class="radio-toolbar style="float:left; padding-right:5px;">								
								<div class=" btn-group" data-toggle="buttons" style="width:100%" id="codeEditorRadios">
									<label class="btn darkblue" data-name="testCases" onclick="codeRadioBtn1();" >
									<!-- <input type="radio" class="toggle"  >Test Case --></label>
									<label class="btn darkblue" data-name="testSteps" onclick="codeRadioBtn2();">
									<!-- <input type="radio" class="toggle" >Main --></label>
									
								</div>
								
							</div>
							
						</div>
								
					</div>					
				</div>
				<div class="modal-body">					
					<!--  <div class="scroller" id="testing" style="height: 410px" data-always-visible="1" data-rail-visible1="1"> -->	 
					 <div  id="testing" style="height: 410px" data-always-visible="1" data-rail-visible1="1">	 
<!-- 						<textarea id="java-code"></textarea> -->
					</div>
					
					 <div id="codeEditorBottomConatiner" class="row" style="padding-top: 15px; padding-right: 10px; display:none;">
						<div style="float:right;">
							 <input type="button" nwsaveas class="btn blue" value="Download" onclick="downloadScriptHandler()">
							 <input type="button" data-dismiss="modal" class="btn grey-cascade" value="Cancel" onclick="closeDownloadScriptHandler()">
						</div>						
					</div>	 					
				</div>
			</div>
	</div>
</div>
<!-- ended popup -->	
</body>
</html>