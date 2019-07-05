<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<link href="css/dropzone.css" rel="stylesheet" type="text/css"/>
<link href="files/lib/metronic/theme/assets/global/css/components.min.css" rel="stylesheet" type="text/css"/>

<script type="text/javascript">
function showAddCommentsMetronicsUI(event){
	$("#add_CommentsUI").modal();	
	$('#add_CommentsUI #addCommentsDiv textarea').val("");
	$('#addCommentsDiv textarea').focus();	
}
function closeAddCommentsMetronicsUI(){
	$("#add_CommentsUI").modal('hide');
}

function closeCommentsMetronicsUI(){
	closeAddCommentsMetronicsUI();
}
</script>

</head>
<body>

<div id="div_CommentsUI" class="modal" tabindex="-1" aria-hidden="true" style="width: 52%;left: 35%;top: 2%; padding: 0px;z-index:10054;">
			<div class="modal-full">
				<div class="modal-content">
					<div class="modal-header" style="padding-bottom: 5px;">
						<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true" onclick="closeCommentsMetronicsUI(event)"></button>
						<h4 class="modal-title theme-font">Comments</h4>
					</div>
					<div class="modal-body">	
					<div style="width: 18%; margin-left:auto; margin-top: 2px;">
	 				     <button class="btn btn-circle green-haze btn-sm" type="button"  data-close-others="true" onclick="showAddCommentsMetronicsUI(event)"> Post a Comment
                         </button>
					</div>				
						 <div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
			 				<div id="metronicsCommentsUI">
			 				 
			 				 <div class="timeline"></div> 			 					
			 				</div>
			 			</div>					 
					</div>
				</div>
		</div>
	</div>
	
	
<div id="add_CommentsUI" class="modal" tabindex="-1" aria-hidden="true" style="width: 30%;left: 46%;top: 11%; z-index:10055; display: none;">
	<div class="modal-full">
		<div class="modal-content">
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title theme-font">Add Comments</h4>
			</div>
			<div class="modal-body">					
				 <div class="scroller" style="height: 150px" data-always-visible="1" data-rail-visible1="1">
	 				<div id="addCommentsDiv">
	 					<textarea style="width: 100%;height: 138px;"></textarea>
	 				</div>
	 			</div>					 
				<div class="row">
					<button type="button" data-dismiss="modal" title="Press Esc to close" onclick="closeAddCommentsMetronicsUI();" class="btn green-haze" style="float: right;" >Cancel</button>
					<button type="button" onclick="addCommentsOfCommenter();" class="btn green-haze" style="float: right;position: relative;margin-right: 10px;" >Add</button>
				</div>
			</div>
		</div>
	</div>
</div>

<div id="div_allCommentsPopup" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%;left: 2%;top: 2%; padding: 0px;">
	<div class="modal-full">
		<div class="modal-content">
			<div class="modal-header" style="padding-bottom: 5px;">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
				<h4 class="modal-title theme-font">Comments</h4>
			</div>
			<div class="modal-body">				
				<div class="clearfix"></div>
				<div class="scroller" style="height: 500px" data-always-visible="1" data-rail-visible1="1">
					<div id="allComments">
					
					</div>
				</div>				 
			</div>
		</div>
	</div>
</div>

<!--- End of addpopup ----> 

<script src="files/lib/metronic/theme/assets/global/plugins/dropzone/dropzone.js"></script>
<script src="js/viewScript/comments.js" type="text/javascript"></script>
</body>
</html>