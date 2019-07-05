var reportIssueFlag = false;
function getReportIssueList(url,tableValue,row,tr){
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			if(data.Message=="Success"){
				callAlert("Report Issues Successfully");
			}
			else if(data.Result=="ERROR"){
      		    data = [];						
			}
			
			if(tableValue == 'parentTable'){
				if(!reportIssueFlag){
					reportIssue_Container(data,"185px");
				}else{				
					reloadDataTableHandler(data, reportissueDT_oTable);
				}
				
			}
			
					
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	
}


function reportIssue_Container(data, scrollYValue){	
	//returnOptionsUserItem(optionsRegularUserArr[0].url, scrollYValue, data, "");
	 listreportIssueDT(data.Records, scrollYValue);
}


function listreportIssueDT(data, scrollYValue){	
	reportIssueListEditor();	
		
	reportissueDT_oTable = $('#reportIssueContainerDT').dataTable({	 
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
        /*fixedColumns: {
            leftColumns: 1,
            rightColumns: 1,
        },*/
        "fnInitComplete": function(data) {
     	   var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
   		  $('#reportIssueContainerDT_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
     	  reInitializeDTReportIssue();
 	   }, 
		buttons: [
		        /* { extend: "create", editor: reportissuelist_editor },*/
		         {
		          extend: "collection",	 
		          text: 'Export',
	              buttons: [
		          {
                    	extend: 'excel',
                    	title: 'Report Issues',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'csv',
                    	title: 'Report Issues',
                    	exportOptions: {
                            columns: ':visible'
                        }
                    },
                    {
                    	extend: 'pdf',
                    	title: 'Report Issues',
                    	exportOptions: {
                            columns: ':visible',	                            
                        },
                        orientation: 'landscape',
                        pageSize: 'A2',
                        footer: true
                    
                    },	                    
                   ],
		         },
		         'colvis',
		         /*{					
					text: '<i class="fa fa-upload showHandCursor trigUploadUserList" title="Upload User List"></i>',
					action: function ( e, dt, node, config ) {						
						//importUsersList('FTE');
						triggerUserListUpload();
					}
				},
				{					
					text: '<i class="fa fa-download showHandCursor downloadUserListTemplate" title="Download User Template"></i>',
					action: function ( e, dt, node, config ) {					
						downloadTemplateUserList();
					}
				},*/
               ],			        
        aaData:data,		    				 
	    aoColumns: [
	          /* { mData: "reportissueid",className: 'disableEditInline', sWidth: '25%' }, */  
	          	{mData:"reportIssueLoginId",className: 'disableEditInline',sWidth:'20%'},
	            { mData: "reportIssuename", className: 'disableEditInline', sWidth: '20%'}, 
	            { mData: "reportIssuetype", className: 'disableEditInline', sWidth: '20%'},
	            { mData: "reportIssueDate", className: 'disableEditInline', sWidth: '20%'},
	            { mData: "reportIssueStatus", className: 'disableEditInline', sWidth: '20%'},
	            { mData: "reportIssuecomment", className: 'disableEditInline', sWidth: '20%'},
	            
                { mData: null,				 
	            	bSortable: false,
	            	mRender: function(data, type, full) {				            	
	           		 var img = ('<div style="display: flex;">'+
         	       		'<button style="border: none; background-color: transparent; outline: none;">'+
         	       				'<img src="css/images/attachment.png" class="reportIssueImg1" title="Attachment" style="width: 18px;height: 18px;">&nbsp;['+data.attachmentCount+']&nbsp;</img></button>'+
         	       			'<button style="border: none; background-color: transparent; outline: none;padding-left: 0px;">'+
         	       					'<i class="fa fa-comments reportIssueImg2" title="User Comments"></i></button>'+
     	       			'</div>');	      		
               		 return img;
	            	}
	            },                         
              ],              
         "oLanguage": {
        	"sSearch": "",
        	"sSearchPlaceholder": "Search all columns"
         },
	});
	 	
	 $(function(){ // this will be called when the DOM is ready
		 	// ------ Attachments -----
		 var productId = 0;
		 $("#reportIssueContainerDT tbody").on('click', 'td button .reportIssueImg1', function () {
				var tr = $(this).closest('tr');
			    var row = reportissueDT_oTable.DataTable().row(tr);
			    isViewAttachment = false;
   				var jsonObj={"Title":"Attachments for ReportIssue",			          
	    			"SubTitle": 'ReporIssue : ['+row.data().reportIssueid+'] '+row.data().reportIssuename,
	    			"listURL": 'attachment.for.entity.or.instance.list?productId='+productId+'&entityTypeId=78&entityInstanceId='+row.data().reportIssueid,
	    			"creatURL": 'upload.attachment.for.entity.or.instance?productId='+productId+'&entityTypeId=78&entityInstanceId='+row.data().reportIssueid+'&description=[description]&attachmentType=[attachmentType]&isEditable=[isEditable]',
	    			"deleteURL": 'delete.attachment.for.entity.or.instance',
	    			"updateURL": 'update.attachment.for.entity.or.instance',
	    			"attachmentTypeURL": 'attachment.type.for.entity.list.option?entityTypeId=78',
	    			"multipleUpload":true,
	    		};	 
        		Attachments.init(jsonObj);
		 });
		 			// ----- Comments  -----
		 
		 $("#reportIssueContainerDT tbody").on('click', 'td button .reportIssueImg2', function () {
				var tr = $(this).closest('tr');
			    var row = reportissueDT_oTable.DataTable().row(tr);
			    var entityTypeIdComments = 78;
				var entityNameComments = "ReportIssueComments";
				listComments(entityTypeIdComments, entityNameComments, row.data().reportIssueid, row.data().reportIssuename, "reportIssueComments");			    
		 });
		 
		 $("#reportIssueContainerDT_length").css('margin-top','8px');
		 $("#reportIssueContainerDT_length").css('padding-left','35px');
		 
		 reportissueDT_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );
		
	 });
        	
	 $('#reportIssueContainerDT tbody').on('click', 'td button .img1', function () {
    	var tr = $(this).closest('tr');
    	var row = reportissueDT_oTable.DataTable().row(tr);    	
    	remove(row.data().userId);
	});	 
}

function reportIssueListEditor(){
	
	reportissuelist_editor = new $.fn.dataTable.Editor( {
		"table": "#reportIssueContainerDT",
    	ajax: "administration.report.issue.add",
    	ajaxUrl: "administration.report.issue.update",
    	idSrc:  "reportIssueid",
    	i18n: {
    	        create: {
    	            title:  "Create a new ReportIssue",
    	            submit: "Create",
    	        }
    	    },
    		fields: [{
			    label: "Report Issue  ID",
			    name: "reportIssueid",
			    "type":"hidden",
			},{
                label: "Report IssueUser",
                name: "reportIssueLoginId",
            },{
                label: "Report Issue  Name",
                name: "reportIssuename",
            },{
                label: "Report Issue Type",
                name: "reportIssuetype",                
            },
            {
                label: "Report Issue Date",
                name: "reportIssueDate",
            },
            {
                label: "Report Issue Status",
                name: "reportIssueStatus",
            },
            {
                label: "Response",
                name: "reportIssuecomment",
            },
        ]
    	});
}
function reInitializeDTReportIssue(){
	clearTimeoutDTuserListRegular = setTimeout(function(){				
		reportissueDT_oTable.DataTable().columns.adjust().draw();
	clearTimeout(clearTimeoutDTuserListRegular);
	},500);
}


function listComments(entityTypeId, entityName, instanceId, instanceName, componentUsageTitle){

	var url='comments.for.entity.or.instance.list?productId=0&entityTypeId='+entityTypeId+'&entityInstanceId='+instanceId+'&jtStartIndex=0&jtPageSize=10000';
	//var instanceId = row.data().productId;
	var jsonObj={"Title":"Response on "+entityName+ ": " +instanceName,
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10000,
			"componentUsageTitle":componentUsageTitle,			
			"entityTypeId":entityTypeId,
			"entityInstanceId":instanceId,
	};
	CommentsMetronicsUI.init(jsonObj);
}