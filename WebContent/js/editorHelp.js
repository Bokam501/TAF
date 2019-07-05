var getHelpItemObj={};
var editorElementId = "";
function editorRHTMLHandler(event){
	if(event.target != undefined){
		 editorElementId = event.target.parentNode.id;
	}else{
		 editorElementId = event.name;
	}
	console.log("name : "+editorElementId);
	
	  var modalEditorHelpString = '<div id="editorHelpContainer_'+editorElementId+'" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%; left: 2%; top: 2%; padding: 0px">'+
		'<div class="modal-full">'+
			'<div class="modal-content">'+				
				'<div class="modal-body">'+
					'<div class="row">'+
						'<button type="button" class="close" title="Press Esc to close" data-dismiss="modal" aria-hidden="true" onclick="closeEditorHelpContainer()"></button>'+
						'<div class="col-md-12">'+
							'<div class="portlet light form-fit ">'+
								'<div class="caption">'+
									'<span class="caption-subject font-green bold uppercase">Help : Title</span>'+
								'</div>'+
								'<div class="portlet-body">'+
									'<div class="col-md-12">'+
										'<div id="editorHelpID_'+editorElementId+'" name="summernote"></div>'+
									'</div>'+																		
									'<div class="row">'+
										'<div style="float:right">'+
											'<button id="saveEditorID_'+editorElementId+'" type="button" class="btn green" onclick="saveEditorHelp(event)">Save</button>'+
											'<button type="button" data-dismiss="modal" class="btn default grey-cascade" value="Cancel">Close</button>'+
										'</div>'+
									'</div>'+									
								'</div>'+
							'</div>'+
						'</div>'+
					'</div>'+
				'</div>'+
			'</div>'+
		'</div>'+
	'</div>';
	
	$("#editorHelpContainer_"+editorElementId).remove();
	$("body").append($(modalEditorHelpString));
	$("#editorHelpContainer_"+editorElementId).modal();
	
	var url="help.view?name="+editorElementId;
	 $.ajax({
		 url:url,
		 method: 'POST',
		 contentType: false,			
		 dataType:'json',
		 success : function(data) {					
			getHelpItemObj = data.Record;				
			if(data.Result == "ERROR"){
				getHelpItemObj=returnEmtpyObjectHelp();
			}				
			$("#editorHelpContainer_"+editorElementId+" .caption .caption-subject").text("");
			$("#editorHelpContainer_"+editorElementId+" .caption .caption-subject").text("HELP : "+getHelpItemObj.title);
			
			/*var url = "files/lib/metronic/theme/assets/global/plugins/bootstrap/js/bootstrap.min.js";
			$.getScript(url, function(data, textStatus, jqxhr) {
				console.log(jqxhr.status); //200
				console.log('Load was performed.');				
				var markupStr = getHelpItemObj.content;
				$("#editorHelpContainer_"+editorElementId+" #editorHelpID_"+editorElementId).summernote('code', markupStr);
				initializeHelpContainer();					
			});*/	
			
			var markupStr = getHelpItemObj.content;
			$("#editorHelpContainer_"+editorElementId+" #editorHelpID_"+editorElementId).summernote('code', markupStr);
			initializeHelpContainer();
			
			$('.link-dialog, .note-editor .modal').on('click', '[data-dismiss="modal"]', function(e) { 
				e.stopPropagation(); 
			});
		},
		error : function(data) {
			console.log("error");
		}
	});
}

function editorMainContent(){
	var str = '<div class="col-md-12">'+
		'<div id="editorHelpID_'+editorElementId+'" name="summernote"></div>'+
		  '</div>'+																		
		  '<div class="row">'+
			'<div style="float:right">'+
				'<button id="saveEditorID_'+editorElementId+'" type="button" class="btn green" onclick="saveEditorHelp(event)">Save</button>'+
				'<button type="button" data-dismiss="modal" class="btn default grey-cascade" value="Cancel">Close</button>'+
			'</div>'+
		 '</div>';	
	return str;	
}

function enableEditorHandler(event){
	$("#editorHelpHTMLContainer_"+editorElementId+" .portlet-body").html('');
	$("#editorHelpHTMLContainer_"+editorElementId+" .portlet-body").html($(editorMainContent()));
	
	if(returnCurrentURL() == "administration.help"){
		$(".note-toolbar").show();
	}else{
		$(".note-toolbar").hide();
	}
	
	var url="help.view?name="+editorElementId;
	 $.ajax({
	   url:url,
	   method: 'POST',
	   contentType: false,			
	   dataType:'json',
	   success : function(data) {					
			getHelpItemObj = data.Record;
			if(data.Result == "ERROR"){
				getHelpItemObj=returnEmtpyObjectHelp();
			}
			$("#editorHelpHTMLContainer_"+editorElementId+" .caption .caption-subject").text("");
			$("#editorHelpHTMLContainer_"+editorElementId+" .caption .caption-subject").text("HELP : "+getHelpItemObj.title);
		
			var markupStr = getHelpItemObj.content;
			$("#editorHelpHTMLContainer_"+editorElementId+" #editorHelpID_"+editorElementId).summernote({'focus':true});
			$("#editorHelpHTMLContainer_"+editorElementId+" #editorHelpID_"+editorElementId).summernote('code', markupStr);
			initializeHTMLHelpContainer();
			
			$('.link-dialog, .note-editor .modal').on('click', '[data-dismiss="modal"]', function(e) { 
				e.stopPropagation(); 
			});
		},
		error : function(data) {
			console.log("error");
		}
	});
}

function saveEditorHelp(event){
	var formdata = new FormData();	
	formdata.append("name", getHelpItemObj.name);
	formdata.append("title", getHelpItemObj.title);
	formdata.append("description", getHelpItemObj.description);
	formdata.append("contentType", getHelpItemObj.contentType);
	formdata.append("helpFileURI", getHelpItemObj.helpFileURI);
	
	var markupStr = $('#editorHelpID_'+editorElementId).summernote('code');
	formdata.append("content", markupStr);
	formdata.append("helpItemId", getHelpItemObj.helpItemId);
	
	var url="help.update"
	 $.ajax({
		 url:url,
			method: 'POST',
			contentType: false,
			data: formdata,
			dataType:'json',
			processData: false,
			success : function(data) {					
				closeEditorHelpContainer();
				closeEditorHelpHTMLContainer()
				
				if(returnCurrentURL() == "administration.help")
					helpCentersDataTable("ParentTable");
				
				callAlert("Saved Successfully");
			},
			error : function(data) {
				closeEditorHelpContainer();
				closeEditorHelpHTMLContainer()
				
				callAlert("Error in Saving Records.");
				
			},complete: function(data){
				closeEditorHelpContainer();
				closeEditorHelpHTMLContainer()
			}
		});
}

// --- loading HTML content ---

function editorHTMLHandler(event){
	if(event.target != undefined){
		 editorElementId = event.target.parentNode.id;
	}else{
		 editorElementId = event.name;
	}
	console.log("name : "+editorElementId);
	
	var editorHelpTxt = '<div id="editorHelpHTMLContainer_'+editorElementId+'" class="modal" tabindex="-1" aria-hidden="true" style="width: 96%; left: 2%; top: 2%; padding: 0px">'+
		'<div class="modal-full">'+
			'<div class="modal-content">'+				
				'<div class="modal-body">'+
					'<div class="row">'+
						'<button type="button" class="close" title="Press Esc to close" aria-hidden="true" data-dismiss="modal" onclick="closeEditorHelpHTMLContainer()"></button>'+
						'<div class="col-md-12">'+
							'<div class="portlet light form-fit">'+
								'<div class="caption">'+
									'<div style="display: flex;"><span class="caption-subject font-green bold uppercase">Help : Title</span>'+
										'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;" onclick="enableEditorHandler(event)">'+
										 	'<i class="fa fa-edit helpContentImg" title="Help Content"></i>'+
										 '</button>'+
									'</div>'+
								'</div>'+
								'<div class="portlet-body">'+
									'<div class="col-md-12">'+
										/*'<iframe id="editorHelpIDTxt_'+editorElementId+'" style="width:100%;height:500px" allowfullscreen allowpaymentrequest frameborder="0"></iframe>'+*/
										'<div id="editorHelpIDTxt_'+editorElementId+'" name="summernote"></div>'+
									'</div>'+
								'</div>'+
							'</div>'+
						'</div>'+
					'</div>'+
				'</div>'+
			'</div>'+
		'</div>'+
	'</div>';
	
	$("#editorHelpHTMLContainer_"+editorElementId).remove();
	$("body").append($(editorHelpTxt));
	$("#editorHelpHTMLContainer_"+editorElementId).modal();
	
	var url="help.view?name="+editorElementId;
	 $.ajax({
		   url:url,
			method: 'POST',
			contentType: false,			
			dataType:'json',
			success : function(data) {					
				getHelpItemObj = data.Record;
				if(data.Result == "ERROR"){
					getHelpItemObj=returnEmtpyObjectHelp();
				}
				$("#editorHelpHTMLContainer_"+editorElementId+" .caption .caption-subject").text("");
				$("#editorHelpHTMLContainer_"+editorElementId+" .caption .caption-subject").text("HELP : "+getHelpItemObj.title);		        

				var markupStr = getHelpItemObj.content;
				$("#editorHelpHTMLContainer_"+editorElementId+" #editorHelpIDTxt_"+editorElementId).summernote({'focus':true});
				$("#editorHelpHTMLContainer_"+editorElementId+" #editorHelpIDTxt_"+editorElementId).summernote('disable');
				$("#editorHelpHTMLContainer_"+editorElementId+" #editorHelpIDTxt_"+editorElementId).summernote('code', markupStr);
				initializeHTMLHelpContainer();				
				$(".note-toolbar").hide();
				
				$('.link-dialog, .note-editor .modal').on('click', '[data-dismiss="modal"]', function(e) { 
					e.stopPropagation(); 
				});
		        //$("#editorHelpHTMLContainer_"+editorElementId+" #editorHelpIDTxt_"+editorElementId).attr('srcdoc',markupStr);		        
			},
			error : function(data) {
				console.log("error");
			}
		});
}

function returnCurrentURL(){
	var loc = window.location.href;
	var loc2 = loc.split("/");
	loc2 = loc2[loc2.length - 1];
	var indx = loc2.indexOf("?");
	if (indx > 0)
		loc2 = loc2.substring(0, indx);
	
	return loc2;
}

function returnEmtpyObjectHelp(){
	var getHelpObj={};
	getHelpObj.name = editorElementId;
	getHelpObj.description = editorElementId.replace('_', ' ');;
	getHelpObj.title = getHelpObj.description.toLocaleUpperCase();;
	getHelpObj.contentType = 'Text';
	getHelpObj.helpFileURI = '';
	getHelpObj.content="<p>No Help Content Available<p>";
	
	return getHelpObj;
}

function closeEditorHelpContainer(){
	$("#editorHelpContainer_"+editorElementId).modal("hide");
}

function closeEditorHelpHTMLContainer(){
	$("#editorHelpHTMLContainer_"+editorElementId).modal("hide");
}

