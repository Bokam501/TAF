
var optionsToolIntagrationArr=[];
var optionsToolIntagrationResultArr=[];
var optionsToolIntagrationItemCounter=0;
var optionType_toolType="toolTypeList";
var modifiedField;
var modifiedFieldTitle;
var oldFieldValue;
var newFieldValue;
var urlToListToolIntagrationDetails;
var toolIntagrationlist_editor='';

function toolIntagration_Container(data, scrollYValue){
	optionsToolIntagrationItemCounter=0;
	optionsToolIntagrationResultArr=[];
	optionsToolIntagrationArr = [
          {id:"toolTypeId", type:optionType_toolType, url:'administration.tool.type.master.option.list?status=1'},
	              
	 ];
	returnOptionsToolIntagrationItem(optionsToolIntagrationArr[0].url, scrollYValue, data, "");
}

function returnOptionsToolIntagrationItem(url, scrollYValue, data, tr){
	$.ajax( {
 	   "type": "POST",
        "url":  url,
        "dataType": "json",
         success: function (json) {
	         if(json.Result == "Error" || json.Options == null){
	         	callAlert(json.Message);
	         	json.Options=[];
	         	optionsToolIntagrationResultArr.push(json.Options);         	
	         	if(optionsToolIntagrationArr[0].type == optionType_toolType){	     			   
     			  listToolIntagrationDT(data, scrollYValue);
     		    }	         	
	         }else{
	     	   if(json.Options.length>0){     		   
				   for(var i=0;i<json.Options.length;i++){
					   json.Options[i].label=json.Options[i].DisplayText;
					   json.Options[i].value=json.Options[i].Value;
				   }			   
	     	   }else{
	     		  json.Options=[];
	     	   }     	   
	     	   optionsToolIntagrationResultArr.push(json.Options);
	     	   
	     	   if(optionsToolIntagrationItemCounter<optionsToolIntagrationArr.length-1){
	     		  if(optionsToolIntagrationArr[0].type == optionType_toolType){
	     			 optionsToolIntagrationArr[optionsToolIntagrationResultArr.length].url = optionsToolIntagrationArr[optionsToolIntagrationResultArr.length].url;
	     		  
	     		  }
	     		 returnOptionsToolIntagrationItem(optionsToolIntagrationArr[optionsToolIntagrationResultArr.length].url, scrollYValue, data, tr);     		  
	     	   }else{
	     		   if(optionsToolIntagrationArr[0].type == optionType_toolType){	     			   
	     			  listToolIntagrationDT(data, scrollYValue);
	     		   } 
	     	   }
	     	   optionsToolIntagrationItemCounter++;     	   
	         }
         },error: function (data) {
        	 optionsToolIntagrationItemCounter++;
        	 
         },complete: function(data){
         },	            
   	});	
}

var toolIntagration_oTable='';
var toolIntagrationChildTable_oTable='';
var clearTimeoutDTToolIntagration=0;
var clearTimeoutDTToolIntagrationCustomer=0;
var toolIntagration_editor='';
var toolIntagration_editor='';
var toolIntagrationListFlag=false;

function reInitializeDTToolIntagration(){
	
}

function reInitializeToolIntagrationList(){
	clearTimeoutDTToolIntagration = setTimeout(function(){				
		toolIntagrationChildTable_oTable.DataTable().columns.adjust().draw();
	clearTimeout(clearTimeoutDTToolIntagration);
	},500);
}

function fullScreenHandlerDTToolIntagration(){
	if($('#toAnimate .portlet-title .fullscreen').hasClass('on')){
		
		var height = Metronic.getViewPort().height -
        $('#toAnimate .portlet-fullscreen .portlet-title').eq(0).outerHeight() -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-top')) -
        parseInt($('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('padding-bottom'));
		
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height',height);
		$("#tabslist").show();
		toolIntagrationDTFullScreenHandler(true);		
	}
	else{
		$('#toAnimate .portlet-fullscreen .portlet-body').eq(0).css('height','auto');						
		$("#tabslist").hide();
		toolIntagrationDTFullScreenHandler(false);
	}
}

function toolIntagrationDTFullScreenHandler(flag){
	if(flag){
		reInitializeDTToolIntagration();
		$("#toolIntagrationContainerDT_wrapper .dataTables_scrollBody").css('max-height','220px');
	}else{
		reInitializeDTToolIntagration();
		$("#toolIntagrationContainerDT_wrapper .dataTables_scrollBody").css('max-height','450px');
	}
}



function toolIntagrationEditor(){
		
	toolIntagrationlist_editor = new $.fn.dataTable.Editor( {
		"table": "#toolIntagrationContainerDT",
	 	ajax: "administration.tool.intagration.master.add",
		ajaxUrl: "administration.tool.intagration.master.update",
		idSrc:  "toolIntagrationId",
		i18n: {
        create: {
	            title:  "Create a new Tool Integration master",
	            submit: "Create",
	        }
	    },
		fields: [{								
			label:"ID",
			name:"toolIntagrationId",
			"type"  : "hidden",
			
		},{
            label: "Name",
            name: "name",
        },{
            label: "Description",
            name: "description",
        },{
            label: "Tool Type",
            name: "toolTypeId",
            options: optionsToolIntagrationResultArr[0],
            "type"  : "select",
           
        },{
            label: "Version",
            name: "version",
        },{
            label: "Status",
            name: "status",
            "type":"hidden",
        },
    ]
	});
	
	return toolIntagrationlist_editor;

}

function listToolIntagrationDT(data, scrollYValue){	
	toolIntagrationlist_editor = toolIntagrationEditor();	
	toolIntagration_oTable = $('#toolIntagrationContainerDT').dataTable({	 
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
     	   var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
 		   
     	   if(!toolIntagrationListFlag){
     		  $('#toolIntagrationContainerDT_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
     	   }
     	  toolIntagrationListFlag=true;
           reInitializeDTToolIntagration();
 	   }, 
 	   select: true,
	   buttons: [
            	{ extend: "create", editor: toolIntagrationlist_editor },	         
            	{
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: "ToolIntegration",
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: "ToolIntegration",
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: "ToolIntegration",
	                    	exportOptions: {
	                            columns: ':visible'
	                        },
	                        orientation: 'landscape',
	                        pageSize: 'LEGAL'
	                    },	                    
	                ],	                
	            },
	            'colvis'
        ],					        
        aaData:data,		    				 
	    aoColumns: [	    
	            
	            { mData: "name", className: 'editable', sWidth: '10%'},
                { mData: "description", className: 'editable', sWidth: '10%'},      
                { mData: "toolTypeId", className: 'editable', sWidth: '10%', editField: "toolTypeId",
                	mRender: function (data, type, full) {
			           	 if (full.action == "create" || full.action == "edit"){
			           		data = optionsValueHandler(toolIntagrationlist_editor, 'toolTypeId', full.toolTypeId);
			           	 }else if(type == "display"){
			           		data = full.toolTypeName;
			           	 }	           	 
			             return data;
		             },               	
                },
                { mData: "version", className: 'editable', sWidth: '10%'},      
               { mData: "status", className: 'editable', sWidth: '5%',
                    mRender: function (data, type, full) {
                  	  if ( type === 'display' ) {
                              return '<input type="checkbox" class="toolintegration-editor-active">';
                          }
                          return data;
      				},
                      className: "dt-body-center"
                  },
                     
              ], 
              rowCallback: function ( row, data ) {
  	            $('input.toolintegration-editor-active', row).prop( 'checked', data.status == 1 );
  	        },
         "oLanguage": {
        	"sSearch": "",
        	"sSearchPlaceholder": "Search all columns"
         },
	});
	
	 $(function(){ 
    	$("#toolIntagrationContainerDT_length").css('margin-top','8px');
		$("#toolIntagrationContainerDT_length").css('padding-left','35px');		
		toolIntagrationlist_editor.field("status").hide();
		
		 $("#toolIntagrationContainerDT").on( 'click', 'tbody td.editable', function (e) {
			 toolIntagrationlist_editor.inline( this, {
	                submitOnBlur: true
	            } );
	        } );
		 
		 $('#toolIntagrationContainerDT').on( 'change', 'input.toolintegration-editor-active', function () {
			 toolIntagrationlist_editor
		            .edit( $(this).closest('tr'), false )
		            .set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )
		            .submit();			
	    });
		
		toolIntagration_oTable.DataTable().columns().every( function () {
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
 }

function toolIntagrationMasterList(url,tableValue, row, tr){
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}
			
			if(tableValue == "parentTable"){
				if(!toolIntagrationListFlag){
					toolIntagration_Container(data, "220px");
				}else{				
					reloadDataTableHandler(data, toolIntagration_oTable);
				}			
				
			}
			else{
				console.log("no child");
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

function getToolIntegrationMasterByStatus()	{
	var status=$('#toolIntagrationStatus_ul').val();
	 var url = 'administration.tool.intagration.master.list?status='+status;	  
	toolIntagrationMasterList(url,"parentTable", "", "")
}