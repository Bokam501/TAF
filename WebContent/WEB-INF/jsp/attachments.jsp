<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>

<link href="js/Scripts/upload/uploadfile.css" rel="stylesheet" type="text/css" />
<link href="css/dropzone.css" rel="stylesheet" type="text/css"/>
</head>
<body>

<!--  Testing attachments -->
<%
	String mode="";

	if(session.getAttribute("tcermode")!=null){
		//mode=session.getAttribute("tcermode").toString();
		mode = "edit";
	}	
%>

<!-- started popup -->
<div id="attachmentsID" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%;">
	<div class="modal-full">
		<div class="modal-content" style="min-height: 600px;">
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" data-dismiss="modal" class="close" onclick="closeAttachmentPopup()"  title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title theme-font"></h4>
				<h5 class="modal-title" style="padding-top: 5px;"></h5>
			</div>
			<div class="modal-body" style="padding-left:0px;padding-right:0px;">					
    				<div class="scroller" style="height: 100%; padding: 0px;" data-always-visible="1" data-rail-visible1="1">						
						<div class="container" id="test" style="height: 100%; padding: 0px;margin-top: -20px !important;">								
							<div class="col-lg-12 col-md-12"> 								
								<div style = "margin-top :20px;min-height: 400px;">
									<!-- <div id="attachmentsContainer"></div> -->	
									<div id="dataTableContainerForAttachment"></div>								
								</div>
							</div>							
						<div>																		
					</div>						
				</div>
			</div>
		</div>
	</div>
</div>
</div>
<!-- ended popup -->

<!----- added popup ---->

<div id="attachmentPopup" class="modal" tabindex="-1" aria-hidden="true" style="z-index: 100051;">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" data-dismiss="modal" class="close" title="Press Esc to close" onclick="closeAttachmentAddPopup()" aria-hidden="true"></button>
				<h4 class="modal-title">Add Attachment</h4>
			</div>
			<div class="modal-body">
				<div class="scroller" style="height:325px;overflow-y:scroll;" data-always-visible="1" data-rail-visible1="1">
					<form  class="form-horizontal" name="frmProfile" enctype="multipart/form-data" >
					<div class="form-body">					
					<div class="form-group">
						<div class="col-md-6">
							<label for="input" class="control-label ">Attachment Type <font color="#efd125" size="4px"> * </font>:</label>
						</div>
						<div class="col-md-6">
							<div class="">
								<select class="form-control input-small select2me" id="attachmentPopup_dataType_ul" >									
								</select>
							</div>
						</div>
					</div>					
					<div class="form-group">
						<div class="col-md-6">
							<label for="input" class="control-label ">Description :</label>
						</div>
						<div class="col-md-6">
							<textarea id="attachmentPopup_desc" class="form-control" rows="2" name="input"> </textarea>		
						</div>
					</div>	
					
					<div class="form-group">
						<div class="col-md-6">
							<label for="input" class="control-label ">isEditable :</label>
						</div>
						<div class="col-md-6">
							<input type="checkbox" id="isEditable" class="form-control" value=1 checked="checked"/>
						</div>
					</div>	
									
   					 <div class="form">
						<div class="form-group">
							<div class="col-md-4">
								<label class=" control-label">Upload : <font color="#F5A9A9" size="4px">*</font></label>
							</div>
							<div class="col-md-8" id="fileUploader_Container">
								<div id="fileuploader_Attachment">Upload</div>										
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-4">
							<!--  	<label class=" control-label">Evidence: <font color="#F5A9A9" size="4px">*</font></label> -->
							</div>
							<div class="col-md-8" id="evidenceUpload_Container">
								<div id="evidenceUpload_Attachment" name="evidenceUpload_Attachment"></div>
							</div>										
						</div>								
					</div>
														
				</div>
				</form>
				 <br><br><br>
					<div class="row" style="float:right;width:100%">						
						<div class="col-md-10"></div>
						<div class="col-md-2">
							 <button type="button" class="btn grey-cascade" onclick="closeAttachmentAddPopup();">Close</button>
						</div>
					</div>
				</div> 
			</div>
		</div>
	</div>
</div>

<!--- End of addpopup ---->

<script src="files/lib/metronic/theme/assets/global/plugins/dropzone/dropzone.js"></script>
<script src="js/Scripts/upload/jquery.uploadfile.js" type="text/javascript"></script>
<script src="js/viewScript/attachments.js" type="text/javascript"></script>
<div><%@include file="attachmentEditor.jsp"%></div>

</body>
</html>