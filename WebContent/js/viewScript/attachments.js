var mode='edit';
var getJSONObject;
var descriptionValue='';
var attachmentValue = 0;
var createURL='';
var attachmentIds = [];
var isViewAttachment = false;
var attachmentsTaskTypeOptionsArr= [];
var loadAttachmentArray=[];
var isEditableValue=1;

var Attachments = function() {
  
   var initialise = function(jsonObj){	
	   getJSONObject="";	   
	   attachmentInitialize(jsonObj);	   
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();


$('#attachmentPopup_dataType_ul').on('change', function() {
	var url=updateCreateURLAttachments();
	//$("#fileUploader_Container .ajax-upload-dragdrop .ajax-file-upload").html("");	
	//var local =  $("#fileUploader_Container .ajax-upload-dragdrop").html(local);	
	$("#fileUploader_Container .ajax-upload-dragdrop .ajax-file-upload form").attr("action", url);
	
});

function attachmentInitialize(jsonObj){	
	getJSONObject = jsonObj;
		
	attachmentsOptions(getJSONObject.attachmentTypeURL);
	//displayAttachments(jsonObj);
	displayAttachmentsDataTable(getJSONObject);
	loadAttachmentTypes();
	
	$("#attachmentsID .modal-header h4").text("");
	$("#attachmentsID .modal-header h4").text(jsonObj.Title);
	$("#attachmentsID .modal-header h5").text("");	
	$("#attachmentsID .modal-header h5").text(jsonObj.SubTitle);
	$("#attachmentsID").find(".input-group").remove();	
	$("#attachmentsID").modal();
	
}

function updateCreateURLAttachments(){
	descriptionValue = $("#attachmentPopup_desc").val();
	
	if($("#attachmentPopup_dataType_ul").find('option').length>0){
		attachmentValue = $("#attachmentPopup_dataType_ul").find('option:selected').val();
		isEditableValue = $("#isEditable").val();
	}
	
	createURL = getJSONObject.creatURL.replace("[description]", descriptionValue).replace("[attachmentType]", attachmentValue).replace("[isEditable]", isEditableValue);
	
	return createURL; 
}

var attachmentURLs = {};
var updateURL ;
//BEGIN: ConvertDataTable - Attachment
function displayAttachmentsDataTable(jsonObj){
	var listURL = jsonObj.listURL;
	updateURL = jsonObj.updateURL;
	var url = listURL +'&jtStartIndex=0&jtPageSize=1000';
	jsonObj={"Title":"Attachments","url": url,"jtStartIndex":0,"jtPageSize":10000,"componentUsageTitle":"Attachments"};
	attachmentDataTableContainer.init(jsonObj);
}

var attachmentDataTableContainer = function() {
 	var initialise = function(jsonObj){
 		assignAttachmentDataTableValues(jsonObj);
	};
		return {
	       //main function to initiate the module
	       init: function(jsonObj) {        	
	       	initialise(jsonObj);
	    }		
	};	
}();

function assignAttachmentDataTableValues(jsonObj){
	openLoaderIcon();			
	 $.ajax({
		  type: "POST",
		  url: jsonObj.url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}					
			jsonObj.data = data;
			
			attachmentURLs=[];
			for(var i=0;i<data.length;i++){
				attachmentURLs[data[i]['attachmentId']]=data[i]['attributeFileURI'];
			}
			
			attachmentDT_Container(jsonObj);
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}
function attachmentDataTableHeader(){
	var childDivString ='<table id="attachment_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Attachment Name</th>'+
			'<th>File Name</th>'+
			'<th>Description</th>'+
			'<th>Attachment Type</th>'+
			'<th>File Extension</th>'+	
			'<th>File Size</th>'+
			'<th>Uploaded By</th>'+
			'<th>Uploaded Date</th>'+
			'<th>Comments</th>'+
			'<th>Editable</th>'+
			'<th>Edit Attachment</th>'+
		'</tr>'+		
	'</thead>'+
	'<tfoot>'+
		'<tr>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+ 
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
			'<th></th>'+
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}
function attachmentDT_Container(jsonObj){
	
	try{
		if ($("#dataTableContainerForAttachment").children().length>0) {
			$("#dataTableContainerForAttachment").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = attachmentDataTableHeader(); 			 
	$("#dataTableContainerForAttachment").append(childDivString);
	
	editorAttachment = new $.fn.dataTable.Editor( {
		"table": "#attachment_dataTable",
		//ajax: "risk.severity.add",
		ajaxUrl: updateURL,
		idSrc:  "attachmentId",
		i18n: {
	        create: {
	            title:  "Create a new Attachment",
	            submit: "Create",
	        }
	    },
		fields: [{								
			label:"attachmentName",
			name:"attachmentName",					
		},{								
			label:"description",
			name:"description",		
		},{								
			label:"attachmentType",
			name:"attachmentType",
			options: attachmentsTaskTypeOptionsArr[0],
			"type":"select",
		}
		]
	});	
	
	attachmentDT_oTable = $("#attachment_dataTable").dataTable( {				 	
		 	"dom":'Bfrtilp',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		   // "sScrollX": "100%",
	       //"sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	 
	       //"aaSorting": [[4,'desc']],
	       "fnInitComplete": function(data) {
		    	  var searchcolumnVisibleIndex = [8,9,10]; // search column TextBox Invisible Column position
	     		  $('#attachment_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
		     	  reInitializeAttachmentDT();
			   },  
		   buttons: [
		             	{ extend: "create", editor: editorAttachment,
							action: function ( e, dt, node, config ) {
								displayCreateTestData();
							}
						},	
						{
						  extend: 'collection',
						  text: 'Export',
						  buttons: [
						        {
						        	extend: 'excel',
						        	title: 'Attachment',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						        {
						        	extend: 'csv',
						        	title: 'Attachment',
						        	exportOptions: {
						                columns: ':visible'
						            }
						        },
						    ],	                
						},
						'colvis',
	         ], 
	         columnDefs: [
	         ],
        aaData:jsonObj.data,		    				 
	    aoColumns: [	        	        
			{ mData: "attachmentName",className: 'editable', sWidth: '15%'},
			{ mData: "attributeFileName",className: 'disableEditInline', sWidth: '15%',
				mRender: function (data, type, full) {
					data = ('<a style="color:#0000FF;" onclick="loadAttachmentsPopupEvidence('+full.attachmentId+');">'+data+'</a>');
					return data;
            	},
			},
			{ mData: "description",className: 'editable', sWidth: '18%' },			
			{ mData: "attachmentType",className: 'editable', sWidth: '10%'},	
			{ mData: "attributeFileExtension",className: 'disableEditInline', sWidth: '10%' },
			{ mData: "attributeFileSize",className: 'disableEditInline', sWidth: '10%' },
			{ mData: "createrName",className: 'disableEditInline', sWidth: '12%' },
			{ mData: "uploadedDate",className: 'disableEditInline', sWidth: '18%' },
            { mData: null, className: 'disableEditInline',				 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<i class="fa fa-comments attachmentCommentsImg" title="Comments"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
	        { mData: "isEditable",
                mRender: function (data, type, full) {
              	  if ( type === 'display' ) {
                          return '<input type="checkbox" class="editorAttachment-active">';
                      }
                      return data;
                  },
                  className: "dt-body-center"
	        },
            { mData: null, className: 'disableEditInline',					 
            	bSortable: false,
            	mRender: function(data, type, full) {				            	
           		 var img = ('<div style="display: flex;">'+
 	       			'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
 	       					'<i class="fa fa-pencil-square-o attachmentEdit" title="Edit Attachment"></i></button>'+
     	       		'</div>');	      		
           		 return img;
            	}
            },	
       ],       
       rowCallback: function ( row, data ) {
    	   $('input.editorAttachment-active', row).prop( 'checked', data.isEditable == 1 );
       },
       "oLanguage": {
            "sSearch": "",
            "sSearchPlaceholder": "Search all columns"
        },   
	}); 	
	
	// Activate an inline edit on click of a table cell
	$('#attachment_dataTable').on( 'click', 'tbody td.editable', function (e) {
		editorAttachment.inline( this, {
            submitOnBlur: true
        } );
	});
	
	$('#attachment_dataTable tbody').on('click', 'td .attachmentEdit', function () {
		var tr = $(this).closest('tr');
    	var row = attachmentDT_oTable.DataTable().row(tr);
		editAttachmentsHandler(row.data().attachmentId, row.data().attachmentName, row.data().attributeFileExtension, row.data());
	});

	$('#attachment_dataTable tbody').on('click', 'td .attachmentCommentsImg', function () {
		var tr = $(this).closest('tr');
    	var row = attachmentDT_oTable.DataTable().row(tr);
		var entityTypeIdComments = 37;
		var entityNameComments = "Attachment";
		listComments(entityTypeIdComments, entityNameComments, row.data().attachmentId, row.data().attachmentName, "attachmentComments");
	});

	$("#attachment_dataTable_length").css('margin-top','8px');
	$("#attachment_dataTable_length").css('padding-left','35px');
	
	$("#attachment_dataTable_filter").css('margin-right','6px');
	
	$('#attachment_dataTable').on( 'change', 'input.editorAttachment-active', function () {
		editorAttachment
            .edit( $(this).closest('tr'), false )
            .set( 'isEditable', $(this).prop( 'checked' ) ? 1 : 0 )
            .submit();
	});
	
	attachmentDT_oTable.DataTable().columns().every( function () {
	    var that = this;
	    $('input', this.footer() ).on( 'keyup change', function () {
	        if ( that.search() !== this.value ) {
	            that
	            	.search( this.value, true, false )
	                .draw();
	        }
	    } );
	} );

}

var clearTimeoutAttachmentDT='';
function reInitializeAttachmentDT(){
	clearTimeoutAttachmentDT = setTimeout(function(){				
		attachmentDT_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutAttachmentDT);
	},200);
}
//END: ConvertDataTable - Attachment
/* Delete - JTable
function displayAttachments(jsonObj){	
	attachmentIds = [];
	var url='';
	try{
		if ($("#attachmentsContainer").length>0) {
			 $('#attachmentsContainer').jtable('destroy');
		}
	} catch(e) {};
	
    $('#attachmentsContainer').jtable({
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
    				displayCreateTestData();    				
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
				list: false,
			},	
			attachmentName: {
				title: 'Attachment Name',
				create : false,
				edit: true,
				list: true, 
				display:function (data) {
					loadAttachmentArray.push(data.record.attachmentId);
					attachmentIds.push(data.record.attachmentId);
					return data.record.attachmentName;
				}
			},	
			attributeFileName: {
				title: 'File Name',
				edit: false,
				list: true, 
				display:function (data) {
					attachmentURLs[data.record.attachmentId] = data.record.attributeFileURI;
					return $("<a style='color: #0000FF'; href=javascript:loadAttachmentsPopupEvidence('"+data.record.attachmentId+"');>"+data.record.attributeFileName+"</a>");
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
						var entityTypeIdComments = 37;
						var entityNameComments = "Attachment";
						listComments(entityTypeIdComments, entityNameComments, data.record.attachmentId, data.record.attachmentName, "attachmentComments");
					});
					return $img;				 
				}
			},
			isEditable: { 
				title: 'isEditable', 
				list: true,
				create : true,
				edit: true,
				type: 'checkbox',
                values: { 'false': 0, 'true': 1 },
                defaultValue: 'true'
			},
			attributeFileURI:{
				title : '',
				list : true,
				create : false,
				edit : false,
				width: "5%",
				display:function (data) { 
					//Create an image for test script popup 
					var $img = $('<i class="fa fa-pencil-square-o attachmentEdit" aria-hidden="true" title="Edit Attachment" data-toggle="modal" /></i>');
					$img.click(function (event) {						
						var entityTypeIdComments = 37;
						var entityNameComments = "Attachment";
						//listComments(entityTypeIdComments, entityNameComments, data.record.attachmentId, data.record.attachmentName, "attachmentComments");
						editAttachmentsHandler(data.record.attachmentId, data.record.attachmentName, data.record.attributeFileExtension, data);						
					});
					return $img;			 
				}
			},						
		}
    });
    $('#attachmentsContainer').jtable('load');		
}Delete - JTable */

function editAttachmentsHandler(id, name, filetype, parentData){
	// ----- testing -----
	//var url= 'product.testcase.script.view?id=7&scriptsFor=TestCase&scriptType=JUNIT&testEngine=SEETEST&testStepOptions=SEPARATE_METHOD';
	var url="get.attachment.type.for.entity.instance.list?attachmentId="+id;
    $.ajax({
		    type: "POST",
		    url: url,
		    dataType: "json", // expected return value type		    
		    success: function(data) {		    	
		     	//alert("success");
		    },
		    error: function(data) {
		    	//alert("error");
		    },
		    complete: function(data){		    		
		    	var setmode="text/html";
		    	var editableFlag=false;
		    	$("#attachmentEditorSave").hide();
		    	$("#attachmentScriptIsEditable").text("Read Only");
		    	
		    	if(filetype == ".xml"){
		    		setmode ="text/html";
		    		setAttachmentEditable();
		    		editableFlag=true;
		    		
		    	}else if(filetype == ".java"){
		    		setmode = "text/x-java";
		    		setAttachmentEditable();
		    		editableFlag=true;
		    		
    			}else if(filetype == ".props"){
    				setmode="text/html";
    				setAttachmentEditable();
    				editableFlag=true;
    			}    	
		    	
                if(data.responseJSON.Result=="ERROR"){
   	 				code="";
                }else{                	
                	var jsonObj={"Title":"Edit Attachment: ",
				         "subTitle": "Attachment ID : "+id+" - FileName: "+name+filetype, 
		    			 "data":parentData.record,		    			 
		    			 "code": data.responseJSON.Message,		    			 
		    			 "mode": setmode,
		    			 "isEditableCommands":editableFlag,
		    			};
		    
                	AttachmentEditor.init(jsonObj);		    	
                }
		    }
		});  
}

function setAttachmentEditable(){
	$("#attachmentEditorSave").show();
	$("#attachmentScriptIsEditable").text("Editable");
}

function updateDropUrl(){
	return updateCreateURLAttachments(); 
}

function loadAttachmentsComponent(){
	//$("#fileuploader_Attachment").html('');
	$("#evidenceUpload_Attachment").html('');
	
	 if(mode!="view"){
		 createURL = updateCreateURLAttachments();
		 
		 $("#fileuploader_Attachment").uploadFile({
				url:createURL,
				fileName:"myfile",
				multiple: getJSONObject.multipleUpload,
				dynamicFormData: function()
				{					
					/*var id="";
					if(document.getElementById('tcerIdhidden')!=null){
						id=document.getElementById('tcerIdhidden').value;
					}
					var data ={ 'tcerId':id};
					return data;*/
					
					var url=updateCreateURLAttachments();
					var len=$("#fileUploader_Container .ajax-upload-dragdrop .ajax-file-upload form").length;
					$("#fileUploader_Container .ajax-upload-dragdrop .ajax-file-upload form").eq(len-1).attr('action',url)
					
				},
				onSelect:function(files){
					console.log(files);
				},
				onSubmit:function(files)
				{					
					var fileNameText = files[0];
					var allowedFile = [];
					allowedFile.push($('#attachmentPopup_dataType_ul').find('option:selected').text());
					if($.inArray(fileNameText.split('.')[1], allowedFile) == -1){
						callAlert("Please attach "+allowedFile[0]+" file");
						return false;
					}
				},
			 afterUploadAll:function(data)
			 {
				 if(data.responses.length>0){					
					 if(data.responses[data.responses.length-1].Result == 'ERROR' || data.responses[data.responses.length-1].Result == 'Error'){
						 callAlert(data.responses[data.responses.length-1].Message);
					 }else if(data.responses[data.responses.length-1].Record.length > 0){
						var record = data.responses[data.responses.length-1].Record[0];
						loadAttachmentArray.push(record);
						attachmentIds = [];
						
						$("#evidenceUpload_Attachment").html('');
						
						$.each(loadAttachmentArray, function(i,item){
							loadAttachmentsEvidenceDetails(item);
							attachmentIds.push(item.attachmentId);
						});
						//displayAttachments(getJSONObject);
						displayAttachmentsDataTable(getJSONObject);
					}else{
						callAlert(data.responses[data.responses.length-1].Message);
					}					
				}
				 $(".ajax-file-upload-statusbar .ajax-file-upload-green").trigger('click');
			 }
		});
	 }
	 
}

function reloadAttachmentList(attachmentId){		
	$("#evidenceUpload_Attachment").html('');
	
	var resultArr=[];
	attachmentIds = [];
	$.each(loadAttachmentArray, function(i,item){
		if(item.attachmentId != attachmentId){
			resultArr.push(item);
			attachmentIds.push(item.attachmentId);
			loadAttachmentsEvidenceDetails(item);
		}
	});
	//displayAttachments(getJSONObject);
	displayAttachmentsDataTable(getJSONObject);
	loadAttachmentArray = resultArr;
}

function loadAttachmentsEvidenceDetails(result){	
	var record = result;
	/*$("#evidenceUpload_Attachment").append("<div style=display:none; id=attachmentUpload"+record.attachmentId+"></div>");
	document.getElementById("attachmentUpload"+record.attachmentId).innerHTML = record.attributeFileURI;*/
	
	attachmentURLs[record.attachmentId] = record.attributeFileURI;
	
	$("#evidenceUpload_Attachment").append("<div style=font-size:small;><a href=javascript:loadAttachmentsPopupEvidence('"+record.attachmentId+"');>"+
			record.attributeFileName+"</a><span onclick='javascript:deleteAttachment("+record.attachmentId+")'; style='padding-left: 5px;padding-top: 5px;'>"+
					"<i class='fa fa-close showHandCursor' title='Delete Attachment'></i></span></div>");
	
}

function loadAttachmentsPopupEvidence(attachmentId){
    var url = attachmentURLs[attachmentId];
	var urlfinal="rest/download/evidence?fileName="+url;
	if(url != undefined ) {
		parent.window.location.href=urlfinal;
	} else {
		callAlert("File not available!");
	}
  	
}




function attachmentsOptions(urlValue){
	$.ajax({
		type: "POST",
	    contentType: "application/json; charset=utf-8",
		url : urlValue,
		dataType : 'json',
		success : function(data) {
			for(var i=0;i<data.Options.length;i++){
				data.Options[i].label=data.Options[i].DisplayText;
				data.Options[i].value=data.Options[i].Value;
			}
			attachmentsTaskTypeOptionsArr.push(data.Options);
		},
		error: function(data){
			console.log("error in delete attachment");
			callAlert(data.Message);
		}
	});

}

function deleteAttachment(attachmentId){
	$.ajax({
		type: "POST",
	    contentType: "application/json; charset=utf-8",
		url : getJSONObject.deleteURL+'?attachmentId='+attachmentId,
		dataType : 'json',
		success : function(data) {
			callAlert(data.Message);
			reloadAttachmentList(attachmentId);			
		},
		error: function(data){
			console.log("error in delete attachment");
			callAlert(data.Message);
		}
	});

}

// ------------------- add popup screen -----------------


/*function loadVersionsForTestData(){
		$('#productVersionTestData_ul').empty();

		$.post('common.list.productversion?productId='+productId,function(data) {	
			var ary = (data.Options);
			$.each(ary, function(index, element) {
				$('#productVersionTestData_ul').append('<option id="' + element.Value + '" value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
			});
			$('#productVersionTestData_ul').select2();
		});
}*/

function loadAttachmentTypes(){
	$('#attachmentPopup_dataType_ul').empty();
	$.post(getJSONObject.attachmentTypeURL,function(data) {	
		var ary = (data.Options);
		$.each(ary, function(index, element) {
			$('#attachmentPopup_dataType_ul').append('<option id="' + element.Value + '" value="' + element.DisplayText + '" ><a href="#">' + element.DisplayText + '</a></option>'); 
		});
		$('#attachmentPopup_dataType_ul').select2();
	});
}

function initializeTestData(){
	$('#attachmentPopup_name').val('');
	$('#attachmentPopup_desc').val('');
	loadAttachmentArray=[];
	$("#evidenceUpload_Attachment").html('');
	
	/*$('select[id="productVersionTestData_ul"] option[value="-1"]').attr("selected","selected");
	$('#productVersionTestData_ul').select2();
	$('#productVersionTestData_ul').prop('disabled', false);*/
	
	$('select[id="attachmentPopup_dataType_ul"] option[value="-1"]').attr("selected","selected");
	$('#attachmentPopup_dataType_ul').select2();
	$('#attachmentPopup_dataType_ul').prop('disabled', false);	
}

function displayCreateTestData(){
	$("#attachmentPopup").modal();
	
	$(".ajax-upload-dragdrop").empty();
	var dragDropLen = $(".ajax-upload-dragdrop").parent().find('.ajax-upload-dragdrop');
	for(var i=0;i<dragDropLen.length;i++){
		$(dragDropLen).eq(i).remove();
	}
	
	loadAttachmentsComponent();	
	
	$('#attachmentPopup').find('.modal-title').replaceWith('<h4 class="modal-title">Add Attachment</h4>');
	initializeTestData();	
}

function closeAttachmentPopup() {	
	$("#attachmentsID").modal('hide');
	initializeTestData();
}

function closeAttachmentAddPopup(){
	$("#attachmentPopup").modal('hide');
}

