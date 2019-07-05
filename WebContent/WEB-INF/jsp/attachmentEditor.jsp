<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>

<link rel=stylesheet href="files/lib/CodeMirror-master/lib/codemirror.css">

<script src="files/lib/CodeMirror-master/lib/codemirror.js"></script>
<script src="files/lib/CodeMirror-master/mode/xml/xml.js"></script>
<script src="files/lib/CodeMirror-master/mode/javascript/javascript.js"></script>
<script src="files/lib/CodeMirror-master/mode/css/css.js"></script>
<script src="files/lib/CodeMirror-master/mode/htmlmixed/htmlmixed.js"></script>
<script src="files/lib/CodeMirror-master/mode/clike/clike.js"></script>
<script src="files/lib/CodeMirror-master/addon/edit/matchbrackets.js"></script>
<script type="text/javascript" src="js/attachmentEditor.js"></script>

<style>
  .CodeMirror { height: auto; border: 1px solid #ddd; }
  .CodeMirror pre { padding-left: 7px; line-height: 1.25; }
</style>
</head>

<body>	

<!-- started popup -->
<div id="codeMirrorTextAttachmentEditor" class="modal " tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%;">
		<div class="modal-full">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom: 5px;">
					<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true" onclick="closeCodeMirrorTextAttachmentEditor()"></button>
					<h4 class="modal-title theme-font"></h4>
					
					<div class="row">
						<div class="col-md-9" style="padding-left: 5px;">
							<h5 class="modal-title" style="padding-top: 5px;float: left;"></h5>
							<h6><span id="attachmentScriptBadgeLineNumber" class="badge" style="background: #4db3a4 !important;width: 100px;padding-top: 3px;margin-top: -10px;margin-left: 95px;">2000</span>
							<span id="attachmentScriptIsEditable" class="badge" style="background: #4db3a4 !important;width: 100px;padding-top: 3px;margin-top: -10px;margin-left: 95px;"></span>
							</h6>
						</div>
						<div class="col-md-3">
						</div>								
					</div>					
				</div>
				<div class="modal-body">					
					<div id="codeMirrorTextAttachmentEditorContainer" class="scroller" style="height: 425px" data-always-visible="1" data-rail-visible1="1">	
						<textarea id="attachment_codeMode"></textarea>
					</div>
					
					 <div class="row" style="padding-top: 15px; padding-right: 10px;">
						<div style="float:right;">
							 <input id="attachmentEditorSave" type="button" nwsaveas class="btn green" value="Save" onclick="saveAttachmentEditor()">
							 <input type="button" data-dismiss="modal" class="btn grey-cascade" value="Cancel" onclick="closeAttachmentHandler()">
						</div>						
					</div>	 					
				</div>
			</div>
	</div>
</div>
<!-- ended popup -->	
</body>
</html>