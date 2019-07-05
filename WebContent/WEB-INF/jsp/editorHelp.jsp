<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title></title>	
		<!-- BEGIN PAGE LEVEL PLUGINS -->
        <link href="files/lib/metronic/theme/assets/global/plugins/bootstrap-wysihtml5/bootstrap-wysihtml5.css" rel="stylesheet" type="text/css" />
        <link href="files/lib/metronic/theme/assets/global/plugins/bootstrap-summernote/summernote.css" rel="stylesheet" type="text/css" />
        <!-- END PAGE LEVEL PLUGINS -->       
        
        <link href="files/lib/metronic/theme/assets/global/plugins/bootstrap-wysihtml5/bootstrap-toggle.css" rel="stylesheet">		
	</head>	
<body>
	<script>
	  function initializeHelpContainer(){		
		if('${roleName}' != "Administrator"){
			$("#enableEditorSaveID_"+editorElementId).hide();
			$("#saveEditorID_"+editorElementId).hide();
			$("#editorHelpHTMLContainer_"+editorElementId+" .fa-edit").hide();
		}else{
			$("#enableEditorSaveID_"+editorElementId).show();
			$("#saveEditorID_"+editorElementId).show();
			$("#editorHelpHTMLContainer_"+editorElementId+" .fa-edit").show();
		}	
		$("#editorHelpContainer_"+editorElementId+" .note-icon-video").parent().hide();
		$("#editorHelpContainer_"+editorElementId+" .note-icon-arrows-alt").parent().hide();
		
   	  }
	  
	  function initializeHTMLHelpContainer(){
		if('${roleName}' != "Administrator"){
			$("#enableEditorSaveID_"+editorElementId).hide();
			$("#saveEditorID_"+editorElementId).hide();
			$("#editorHelpHTMLContainer_"+editorElementId+" .fa-edit").hide();
		}else{
			$("#enableEditorSaveID_"+editorElementId).show();
			$("#saveEditorID_"+editorElementId).show();
			$("#editorHelpHTMLContainer_"+editorElementId+" .fa-edit").show();
		}	
		$("#editorHelpHTMLContainer_"+editorElementId+" .note-icon-video").parent().hide();
		$("#editorHelpHTMLContainer_"+editorElementId+" .note-icon-arrows-alt").parent().hide();
	  }
	</script>
	
	<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap-wysihtml5/wysihtml5-0.3.0.js" type="text/javascript"></script>
  	<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap-wysihtml5/bootstrap-wysihtml5.js" type="text/javascript"></script>
  	<script src="files/lib/metronic/theme/assets/global/plugins/bootstrap-summernote/summernote.min.js" type="text/javascript"></script> 
	<script type="text/javascript" src="js/editorHelp.js"></script>		
</body>
</html>