var mode='edit';
var getAttachmentTabJSONObject;
var descriptionValue='';
var attachmentValue = 0;
var createURL='';
var attachmentIds = [];
var isViewAttachment = false;
var attachmentsTaskTypeOptionsArr= []; 
											
var AttachmentsTab = function() {
  
   var initialise = function(jsonObj){	
	   getAttachmentTabJSONObject="";	   
	   attachmentTabInitialize(jsonObj);	   
   };
		return {        
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

function attachmentTabInitialize(jsonObj){		
	getAttachmentTabJSONObject = jsonObj;
		
	attachmentsOptionsAWPComp(getAttachmentTabJSONObject.attachmentTypeURL);
	listAttachmentsOfSelAWPComponentDT(getAttachmentTabJSONObject);
	loadAttachmentTypesAWPComp();	
	
	$("jsonObj.attachmentTab_containerID h4").text("");
	$("jsonObj.attachmentTab_containerID h4").text(getAttachmentTabJSONObject.Title);
	$("jsonObj.attachmentTab_containerID h5").text("");	
	$("jsonObj.attachmentTab_containerID h5").text(getAttachmentTabJSONObject.SubTitle);
}

function attachmentsOptionsAWPComp(urlValue){
	$.ajax({
		type: "POST",
	    contentType: "application/json; charset=utf-8",
		url : urlValue,
		dataType : 'json',
		success : function(data) {
			attachmentsTaskTypeOptionsArr = data.Options;
		},
		error: function(data){
			console.log("error in delete attachment");
			callAlert(data.Message);
		}
	});
}

var attachmentURLs = {};
var attachmentTab_containerID = "";

function listAttachmentsOfSelAWPComponentDT(jsonObj){	 	
	openLoaderIcon();
	var urlValue = jsonObj.listURL+'&jtStartIndex=0&jtPageSize=10000';
	$.ajax({
	  type: "POST",
	  url:urlValue,
	  contentType: "application/json; charset=utf-8",
	  dataType : 'json',
	  success : function(data) {		
		closeLoaderIcon();		
		if(data == null || data.Result=="ERROR"){
			data = [];						
		}else{
			data = data.Records;
		}			
		attachmentTab_Container(data, jsonObj);			
		
	  },
	  error : function(data) {
		 closeLoaderIcon();  
	 },
	 complete: function(data){
		closeLoaderIcon();			
	 }
	});	
}

var clearTimeoutDTAttachmentTab='';
var attachmentTab_oTable='';
var attachmentTab_editor='';
function reInitializeDTArrangementTab(){
	clearTimeoutDTAttachmentTab = setTimeout(function(){				
		attachmentTab_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTAttachmentTab);
	},200);
}

function attachmentTab_editorResultsEditor(){	
    
	attachmentTab_editor = new $.fn.dataTable.Editor( {
			"table": '#attachmentTab_'+attachmentTab_containerID+'dataTable',
    		ajax: getAttachmentTabJSONObject.creatURL,
    		ajaxUrl: getAttachmentTabJSONObject.updateURL,
    		idSrc:  "attachmentId",
    		i18n: {
    	        create: {
    	            title:  "Create a new attachment",
    	            submit: "Create",
    	        }
    	    },
    		fields: [{								
				label:"attachmentId",
				name:"attachmentId",					
				type: 'hidden',								
			},{								
				label:"attachmentName",
				name:"attachmentName",						
			},{
                label:"entityMasterName",
				name:"entityMasterName",					
            },{
                label:"entityName",
				name:"entityName",									
            },{
                label: "attributeFileName",
                name: "attributeFileName", 				
            },{
                label: "description",
                name: "description", 				
            },{
                label: "attachmentType",
                name: "attachmentType", 
				options: attachmentsTaskTypeOptionsArr,
                "type"  : "select",	
            },{
                label: "attributeFileExtension",
                name: "attributeFileExtension", 				
            },{
                label: "attributeFileSize",
                name: "attributeFileSize",                
            },{
                label: "createrName",
                name: "createrName", 				
            },{
                label: "uploadedDate",
                name: "uploadedDate",   				
            }
        ]
    	});	
}

function attachmentTab_Container(data, jsonObj){
	attachmentTab_containerID = jsonObj.jtableDivId;
	
	try{
		if ($("#"+attachmentTab_containerID).children().length>0) {
			$("#"+attachmentTab_containerID).children().remove();
		}
	} 
	catch(e) {}
	
	  var emptytr = emptyTableRowAppending(11);  // total coulmn count				  
	  var childDivString = '<table id="attachmentTab_'+attachmentTab_containerID+'dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
		'<thead></thead><tfoot><tr></tr></tfoot></table>';					  
	  $("#"+attachmentTab_containerID).append(childDivString); 						  
	  
	  $('#attachmentTab_'+attachmentTab_containerID+'dataTable thead').html('');
	  $('#attachmentTab_'+attachmentTab_containerID+'dataTable thead').append(attachmentTabHeader());
	  
	  $('#attachmentTab_'+attachmentTab_containerID+'dataTable tfoot tr').html('');     			  
	  $('#attachmentTab_'+attachmentTab_containerID+'dataTable tfoot tr').append(emptytr);
	  
	  attachmentTab_editorResultsEditor();
					
	attachmentTab_oTable = $('#attachmentTab_'+attachmentTab_containerID+'dataTable').dataTable( {
		 	dom: "Bfrtilp",
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bSort": false,
	       "bScrollCollapse": true,	       
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex = [10]; // search column TextBox Invisible Column position
     		  $('#attachmentTab_'+attachmentTab_containerID+'dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
    	    	    var i=$(this).index();
    	    	    
    	    	    var flag=false;
    	    	    for(var j=0;j<searchcolumnVisibleIndex.length;j++){
    	    	    	if(i == searchcolumnVisibleIndex[j]){
    	    	    		flag=true;
    	    	    		break;
    	    	    	}
    	    	    }
    	    	    
    	    	    if(!flag){
    	    	    	$(this).html('');
    	    	    	$(this).append( '<input type="text" name="'+data.aoColumns[i].mData+'" value="" style="width:100%" class="search_init" />');
    	    	    }
	       	   });			   
			   reInitializeDTArrangementTab();			   
		   },  
		   select: true,
		   buttons: [	
					{ extend: "create", editor: attachmentTab_editor },
					{					
					text: '<span title="Add Attachments">New</span>',
						action: function ( e, dt, node, config ) {
							displayCreateTestDataAWPComp();    	
						}
					},	
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: "Attachment Details",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: "Attachment Details",
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: "Attachment Details",
		                    	exportOptions: {
		                            columns: ':visible'
		                        },
		                        orientation: 'landscape',
		                        pageSize: 'LEGAL'
		                    },	                    
		                ],	                
		            },
		            'colvis',
				], 	         
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "attachmentName",className: 'editable', sWidth: '5%' },		
           { mData: "entityMasterName",className: 'disableEditInline', sWidth: '15%' },
           { mData: "entityName",className: 'disableEditInline', sWidth: '15%' },           
		   { mData: "attributeFileName", className: 'disableEditInline', sWidth: '10%', 
				"mRender": function (data,type,full) {	        			 		
					attachmentURLs[full.attachmentId] = full.attributeFileURI;					
					return ('<a onclick="loadAttachmentsPopupEvidenceAWPComp('+full.attachmentId+')>'+full.attributeFileName+'</a>');
		    	},
		   },
           { mData: "description",className: 'editable', sWidth: '15%' },						
           //{ mData: "attachmentType",className: 'editable', sWidth: '15%' },
		   { mData: "attachmentType", className: 'editable', sWidth: '10%',
				mRender: function (data, type, full) {
					 if (full.action == "create" || full.action == "edit"){
						data = optionsValueHandler(acitivityChangeRequest_editor, 'attachmentType', full.attachmentType);
					 }
					 else if(type == "display"){
						data = full.attachmentType;
					 }	           	 
					 return data;
				 },
			},	
           { mData: "attributeFileExtension",className: 'disableEditInline', sWidth: '15%' },
		   { mData: "attributeFileSize",className: 'disableEditInline', sWidth: '5%' },		
           { mData: "createrName",className: 'disableEditInline', sWidth: '15%' },           					  
		   { mData: "uploadedDate",className: 'disableEditInline', sWidth: '15%' },           					  
			{ mData: null,				 
            	bSortable: false,
				sWidth: '2%',
				className: 'disableEditInline',
            	mRender: function(data, type, full) {			
           		 var img = ('<div style="display: flex;">'+					
     	       		'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
   						'<i class="fa fa-comments attachmentTabImg1" title="Comments"></i></button>'+	
					'<button style="border: none; background-color: transparent; outline: none;">'+
						'<i class="fa fa-trash-o details-control" title="Delete Activity Task" onClick="deleteAttachmentTabItem('+data.attachmentId+')" style="margin-left: 5px;"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	},
            },
       ], 			   
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	
	$(function(){ // this will be called when the DOM is ready  
		 
		 $('#attachmentTab_'+attachmentTab_containerID+'dataTable_length').css('margin-top','8px');
		 $('#attachmentTab_'+attachmentTab_containerID+'dataTable_length').css('padding-left','35px');
		 
		attachmentTab_oTable.DataTable().columns().every( function () {
			var that = this;
			$('input', this.footer() ).on( 'keyup change', function () {
				if ( that.search() !== this.value ) {
					that
						.search( this.value, true, false )
						.draw();
				}
			});
		});
		
		// ----- focus area -----	
			
		$( 'input', attachmentTab_editor.node()).on( 'focus', function () {
			this.select();
		});
		
		 $('#attachmentTab_'+attachmentTab_containerID+'dataTable_wrapper').find(".buttons-create").hide();
		
		// Activate an inline edit on click of a table cell
		  $('#attachmentTab_'+attachmentTab_containerID+'dataTable').on( 'click', 'tbody td.editable', function (e) {
        	attachmentTab_editor.inline( this, {
                submitOnBlur: true
            } );
        } );

		// ----- Comments  -----
		 
		 $('#attachmentTab_'+attachmentTab_containerID+'dataTable tbody').on('click', 'td button .attachmentTabImg1', function () {
			var tr = $(this).closest('tr');
			var row = attachmentTab_oTable.DataTable().row(tr);
							
			var entityTypeIdComments = 37;
			var entityNameComments = "Attachment";
			listComments(entityTypeIdComments, entityNameComments, row.data().attachmentId, row.data().attachmentName, "attachmentComments"); 				    
		 });	 
	});
}

function deleteAttachmentTabItem(attachmentId){
	var fd = new FormData();
	fd.append("attachmentId", attachmentId);
	
	$("#activityTabLoaderIcon").show();
	$.ajax({
		url : getAttachmentTabJSONObject.deleteURL,
		data : fd,
		contentType: false,
		processData: false,
		type: "POST",
		success : function(data) {		
			if(data.Message != 'undefined' && data.Message != null && data.Message != ''){
				callAlert(data.Message);
				listAttachmentsOfSelAWPComponentDT(getAttachmentTabJSONObject);
			}
			$("#activityTabLoaderIcon").hide();
		},
		error : function(data) {
			$("#activityTabLoaderIcon").hide();  
		},
		complete: function(data){
			$("#activityTabLoaderIcon").hide();
		}
	});
}

function attachmentTabHeader(){		
	var tr = '<tr>'+			
		'<th>Attachement Name</th>'+
		'<th>Source</th>'+
		'<th>Source Name</th>'+
		'<th>File Name</th>'+
		'<th>Description</th>'+
		'<th>Attachment Type</th>'+
		'<th>File Extension</th>'+
		'<th>File Size</th>'+	
		'<th>Uploaded By</th>'+				
		'<th>Uploaded Date</th>'+
		'<th></th>'+		
	'</tr>';
	return tr;
}

/*
function listAttachmentsOfSelAWPComponent(jsonObj){	
	jtableDIVId = jsonObj.jtableDivId;	
	try{
		if ($(jtableDIVId).length>0) {
			 $(jtableDIVId).jtable('destroy');
		}
	} catch(e) {};
	
    $(jtableDIVId).jtable({
		title: 'Attachments', 
		paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        editinline:{enable:true},	
		actions: { 
			listAction: jsonObj.listURL,
			editinlineAction :jsonObj.updateURL,
			deleteAction: jsonObj.deleteURL,		
		},		
		toolbar : {
    		items : [
    		  {
    			text : 'Add',
    			click : function() {
    				displayCreateTestDataAWPComp();    				
    			}
    		}]    			
		},
		recordsLoaded:function(){
			if(typeof isViewAttachment != undefined && isViewAttachment){
				$('.jtable-toolbar-item').hide();
				$('.jtable-command-column').hide();
			}else{
				$('.jtable-toolbar-item').show();
				$('.jtable-command-column').show();
			}
		},
		fields: {			 
			attachmentId: { 
				key: true, 
				create: false, 
            	edit: false, 
				list: false
			},	
			attachmentName: {
				title: 'Attachement Name',
				create : false,
				edit: true,
				list: true, 
			},
			entityMasterName : {
				title : 'Source',
				list : true,				    										
				create : false,
				edit : false,
				width : "5%"
			},
			entityName : {
				title : 'Source Name',
				list : true,				    										
				create : false,
				edit : false,
				width : "10%"
			},
			attributeFileName: {
				title: 'File Name',
				edit: false,
				list: true, 
				display:function (data) {
					attachmentURLs[data.record.attachmentId] = data.record.attributeFileURI;
					return $("<a style='color: #0000FF'; href=javascript:loadAttachmentsPopupEvidenceAWPComp('"+data.record.attachmentId+"');>"+data.record.attributeFileName+"</a>");
				}
			},		
			description: {
				title: 'Description',
				edit:true,
				list: true, 
			},
			attachmentType: {
				title: 'Attachment Type',
				edit:true,
				list: true, 
				//options: getJSONObject.attachmentTypeURL				
				options:function () {
            		return  attachmentsTaskTypeOptionsArr; 
            	}				
			},
			attributeFileExtension: {
				title: 'File Extension',
				list: true, 
				edit: false,
			},			
			attributeFileSize: {
				title: 'File Size',
				list: true, 
				edit: false,
			},			
			createrName: { 
				title: 'Uploaded By', 
				list: true,
				edit: false,
			},
			uploadedDate: { 
				title: 'Uploaded Date', 
				list: true,
				edit: false,
			},
			commentsAWP:{
				title : '',
				list : true,
				create : false,
				edit : false,
				width: "5%",
				display:function (data) { 
					//Create an image for test script popup 
					var $img = $('<i class="fa fa-comments" title="Comments"></i>');
					$img.click(function () {						
						//listActivityWorkPackageAuditHistory(data.record.activityWorkPackageId);
						var entityTypeIdComments = 37;
						var entityNameComments = "Attachment";
						listComments(entityTypeIdComments, entityNameComments, data.record.attachmentId, data.record.attachmentName, "attachmentComments");
					});
				return $img;
				}
			},					
		}
    });
    $(jtableDIVId).jtable('load');			
}
*/

function updateCreateURLAttachmentsAWPComp(){
	descriptionValue = $("#attachmentPopup_desc").val();
	
	if($("#attachmentPopup_dataType_ul").find('option').length>0){
		attachmentValue = $("#attachmentPopup_dataType_ul").find('option:selected').val();
		isEditableValue = $("#isEditable").val();
	}
	
	createURL = getAttachmentTabJSONObject.creatURL.replace("[description]", descriptionValue).replace("[attachmentType]", attachmentValue).replace("[isEditable]", isEditableValue);
	
	return createURL; 
}

function updateDropUrlAWPComp(){
	return updateCreateURLAttachmentsAWPComp(); 
}

var loadAttachmentArrayAWPComp=[];
function loadAttachmentsComponentAWPComp(){
	
	 if(mode!="view"){
		 updateCreateURLAttachmentsAWPComp();
		 
		 $("#fileuploader_Attachment").uploadFile({
				url:createURL,
				fileName:"myfile",
				multiple: getAttachmentTabJSONObject.multipleUpload,
				dynamicFormData: function()
				{					
					updateCreateURLAttachmentsAWPComp();
				},
			 afterUploadAll:function(data)
			 {
				 if(data.responses.length>0){					
					 if(data.responses[data.responses.length-1].Result == 'ERROR' || data.responses[data.responses.length-1].Result == 'Error'){
						 callAlert(data.responses[data.responses.length-1].Message);
					 }else if(data.responses[data.responses.length-1].Record.length > 0){
						var record = data.responses[data.responses.length-1].Record[0];
						loadAttachmentArrayAWPComp.push(record);
						attachmentIds = [];
						
						$("#evidenceUpload_Attachment").empty();
						$.each(loadAttachmentArrayAWPComp, function(i,item){
							loadAttachmentsEvidenceDetailsAWPComp(item);
							attachmentIds.push(item.attachmentId);
						});
						listAttachmentsOfSelAWPComponentDT(getAttachmentTabJSONObject);
					}else{
						callAlert(data.responses[data.responses.length-1].Message);
					}					
				}
				 $(".ajax-file-upload-statusbar .ajax-file-upload-green").trigger('click');
			 }
		});
	 }
}

function reloadAttachmentListAWPComp(attachmentId){		
	$("#evidenceUpload_Attachment").empty();
	
	var resultArr=[];
	attachmentIds = [];
	$.each(loadAttachmentArrayAWPComp, function(i,item){
		if(item.attachmentId != attachmentId){
			resultArr.push(item);
			attachmentIds.push(item.attachmentId);
			loadAttachmentsEvidenceDetailsAWPComp(item);
		}
	});
	listAttachmentsOfSelAWPComponentDT(getAttachmentTabJSONObject);
	loadAttachmentArrayAWPComp = resultArr;
}

function loadAttachmentsEvidenceDetailsAWPComp(result){	
	var record = result;	
	attachmentURLs[record.attachmentId] = record.attributeFileURI;
	
	$("#evidenceUpload_Attachment").append("<div style=font-size:small;><a href=javascript:loadAttachmentsPopupEvidenceAWPComp('"+record.attachmentId+"');>"+
			record.attributeFileName+"</a><span onclick='javascript:deleteAttachmentAWPComp("+record.attachmentId+")'; style='padding-left: 5px;padding-top: 5px;'>"+
					"<i class='fa fa-close showHandCursor' title='Delete Attachment'></i></span></div>");	
}

function loadAttachmentsPopupEvidenceAWPComp(attachmentId){
    var url = attachmentURLs[attachmentId];
	var urlfinal="rest/download/evidence?fileName="+url;
  	parent.window.location.href=urlfinal;
}

function deleteAttachmentAWPComp(attachmentId){
	$.ajax({
		type: "POST",
	    contentType: "application/json; charset=utf-8",
		url : getAttachmentTabJSONObject.deleteURL+'?attachmentId='+attachmentId,
		dataType : 'json',
		success : function(data) {
			callAlert(data.Message);
			reloadAttachmentListAWPComp(attachmentId);			
		},
		error: function(data){
			console.log("error in delete attachment");
			callAlert(data.Message);
		}
	});
}

function loadAttachmentTypesAWPComp(){
	$('#attachmentPopup_dataType_ul').empty();
	$.post(getAttachmentTabJSONObject.attachmentTypeURL,function(data) {	
		var ary = (data.Options);
		$.each(ary, function(index, element) {
			$('#attachmentPopup_dataType_ul').append('<option id="' + element.Value + '" value="' + element.DisplayText + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
		$('#attachmentPopup_dataType_ul').select2();
	});
}

function initializeTestDataAWPComp(){
	$('#attachmentPopup_name').val('');
	$('#attachmentPopup_desc').val('');
	loadAttachmentArrayAWPComp=[];
	$("#evidenceUpload_Attachment").empty();
		
	$('select[id="attachmentPopup_dataType_ul"] option[value="-1"]').attr("selected","selected");
	$('#attachmentPopup_dataType_ul').select2();
	$('#attachmentPopup_dataType_ul').prop('disabled', false);	
}

function displayCreateTestDataAWPComp(){
	$("#attachmentPopup").modal();
	
	$(".ajax-upload-dragdrop").empty();
	var dragDropLen = $(".ajax-upload-dragdrop").parent().find('.ajax-upload-dragdrop');
	for(var i=0;i<dragDropLen.length;i++){
		$(dragDropLen).eq(i).remove();
	}
	
	loadAttachmentsComponentAWPComp();	
	
	$('#attachmentPopup').find('.modal-title').replaceWith('<h4 class="modal-title">Add Attachment</h4>');
	initializeTestDataAWPComp();	
}

function closeAttachmentPopup() {	
	$("#attachmentsID").modal('hide');
	initializeTestDataAWPComp();
}

function closeAttachmentAddPopup(){
	$("#attachmentPopup").modal('hide');
}