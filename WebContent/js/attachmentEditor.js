var CodeAttachmentEditorJsonObj="";
var AttachmentEditor = function() {
  
   var initialise = function(jsonObj){
	   CodeAttachmentEditorJsonObj = jsonObj;
	   displayAttachmentCode(jsonObj, jsonObj.subTitle, jsonObj.code);
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

var attachmentEditor="";
function displayAttachmentCode(jsonObj, subTitle, code){
	$("#codeMirrorTextAttachmentEditor h4").text("");
	$("#codeMirrorTextAttachmentEditor h4").text(jsonObj.Title);
	$("#codeMirrorTextAttachmentEditor h5").text("");
	$("#codeMirrorTextAttachmentEditor h5").text(subTitle);
	$("#codeMirrorTextAttachmentEditor").modal();
		
	 if(attachmentEditor!=""){	 
		attachmentEditor=null;
		$("#codeMirrorTextAttachmentEditorContainer").html('');									
		$("#codeMirrorTextAttachmentEditorContainer").append('<textarea id="attachment_codeMode"></textarea>');

		 attachmentEditor = CodeMirror.fromTextArea(document.getElementById("attachment_codeMode"), {
			  value: code,
		      lineNumbers: true,
		      lineWrapping: true,
		      mode: jsonObj.mode,
		      matchBrackets: true,
		      readOnly : !jsonObj.isEditableCommands,
		    });	
		attachmentEditor.getDoc().setValue(code);
		 
	 }else{
		 
		 attachmentEditor = CodeMirror.fromTextArea(document.getElementById("attachment_codeMode"), {
			  value: code,
		      lineNumbers: true,
		      lineWrapping: true,
		      mode: jsonObj.mode,
		      matchBrackets: true,
		      readOnly : !jsonObj.isEditableCommands,
		    });
		attachmentEditor.getDoc().setValue(code);	
	 }	
	 $("#attachmentScriptBadgeLineNumber").text((attachmentEditor.getDoc().cm.lastLine())+" Lines");
}

function closeCodeMirrorTextAttachmentEditor() {	
	$("#codeMirrorTextAttachmentEditor").modal("hide");
}

function saveAttachmentEditor(){
	var saveScript='';
	var entityTypeId = CodeAttachmentEditorJsonObj.data.entityMasterId;
	var entityInstanceId = CodeAttachmentEditorJsonObj.data.entityPrimaryId;
	var attachmentId = CodeAttachmentEditorJsonObj.data.attachmentId;
	var attachmentFilename = CodeAttachmentEditorJsonObj.data.attachmentName;
	var attachmentType = CodeAttachmentEditorJsonObj.data.attributeFileExtension.toString();
	saveScript = attachmentEditor.getValue(false);
	saveScript = saveScript.join(';').toString();
	
	var urlValue="update.by.attachment.type.for.entity.or.instance?entityTypeId="+entityTypeId+"&entityInstanceId="+entityInstanceId+"&attachmentId="+attachmentId+"&attachmentFilename="+attachmentFilename+"&attachmentType="+attachmentType;
	$.ajax({
	      type: "POST",
	        url : urlValue,		
	        dataType : 'json',
	        data: { 'RESULT': saveScript},
	        success : function(data) {
	           if(data.Result=="OK"){
	           		callAlert(data.Message);
	           		closeCodeMirrorTextAttachmentEditor();
	           }else if(data.Result == "ERROR"){
	        	   callAlert(data.Message);			        	   
	           }
	      },
	      error: function(data){
	    	  console.log("error in ajax call");
	      },
	      complete: function(data){
	    	  console.log("complete in ajax call");
	      }
		 });
	
}