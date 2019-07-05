function mappingFeatureToBuild(){	
	var buildId=0;
	var url = 'get.productFeature.build.mappedList?productId='+productId+'&versionId='+productVersionId+'&buildId='+buildId+'&jtStartIndex=0&jtPageSize=10000';
	$("#featureToBuildLoaderIcon").show();

	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			  $("#featureToBuildLoaderIcon").hide();
			
			if(data != undefined){
				evidenceReportData(data);
			}      		    
		  },
		  error : function(data) {
			  $("#featureToBuildLoaderIcon").hide(); 
		 },
		 complete: function(data){
			 $("#featureToBuildLoaderIcon").hide();
		 }
	});		
}

var mappingFeatureToBuild_oTable='';
var clearTimeoutDTmappingFeatureToBuild='';
function reInitializeDTMappingFeatureToBuild(){
	clearTimeoutDTmappingFeatureToBuild = setTimeout(function(){				
		mappingFeatureToBuild_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTmappingFeatureToBuild);
	},200);
}

var headerTitleArr=[];
function evidenceReportData(data){	
	$("#featureToBuildMappingContent").html('');
	
	var scrollValue=350;
	var evidenceStr=""; 
	var titleCrUc="Mapping_FeatureToBuild"
	var data1=eval(data);	
	var columnData=data1.DATA;
	var cols = data1.COLUMNS;		
	var content = "<table id='evidenceReport_dataTable' class='display datatable' style='width:100%;'><thead>";
	content +='<tr>';	
	
	try{		
		if ($("#featureToBuildMappingContent").children().length>0) {
			$("#featureToBuildMappingContent").children().remove();
		}
		
	} catch(e) {}

	var titleName="";
	var displayTitleName="";
  	 var atarget = [];
  	 var stitle = []; 
  	 var mappingFeatureToBuildFooter='<tfoot><tr><th></th><th></th>'
  	content += '<th>ID</th><th>Name</th>';
  	 atarget[0]=0;
  	headerTitleArr=[];
  	 $(cols).each(function(index, element){
  		 titleName='';
  		 displayTitleName='';
  	     atarget[index] =index+2;
  	     var resArr = element.title.split('~');
  	     displayTitleName = resArr[1];
  	     titleName=titleName+element.title;  	
  	     content += '<th >'+displayTitleName+'</th>';
  	     mappingFeatureToBuildFooter +='<th></th>';
  	     headerTitleArr[index]=titleName;
  	 });
     content +=' </tr></thead>';	
     mappingFeatureToBuildFooter += '</tr></tfoot>';
     content += mappingFeatureToBuildFooter; 
     content += "</table>";  	  
  	$("#featureToBuildMappingContent").append(content);	
  	
  	mappingFeatureToBuild_oTable = $('#evidenceReport_dataTable').dataTable( {
			dom: "Bfrtilp",
			paging: true,
			destroy: true,
			searching: true,			
			"scrollX":true,
			"scrollY":"100%",
			"bSort": false,
			"fnInitComplete": function(data) {
					var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
				  $("#evidenceReport_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th").each( function () {
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
				   reInitializeDTMappingFeatureToBuild();			   
			   },
			buttons: [		             		  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: titleCrUc,
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: titleCrUc,
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: titleCrUc,
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
			 "aaData": columnData ,
			 "aoColumnDefs":[ 
                 {
                     "aTargets":[0,1],
                     sWidth: '10%',
                 },                
                 { 
            	 mRender: function (data, type, full, meta) {
					 
					 if ( type === 'display' ){						
						var resultType='<span><select id="'+full[0]+'~'+headerTitleArr[meta.col-2]+'" class="editorNotification-active">';
											
						resultType += '<option '+full[0]+' value=Yes>Yes</option>'; 
						resultType += '<option '+full[0]+' value=No>No</option>';
						resultType += '</select><span>';					 
						data = resultType;
					 }
					 
                	/* if(data == null || data == ''){
                		 return '<input id="'+full[0]+'~'+headerTitleArr[meta.col-1]+'" type="checkbox" class="editorNotification-active">';
                	 }else if ( type === 'display' ){
                            return '<input id="'+full[0]+'~'+headerTitleArr[meta.col-1]+'" type="checkbox" class="editorNotification-active">';
                     } */
                      return data;
                    },                 
                 "aTargets":atarget,								   		                    						    				                
                }        
             ],
             rowCallback: function ( row, data) {       
            	 var arr = $('select.editorNotification-active', row);
            	 for(var i=0;i<data.length-1;i++){
            		 //$(arr[i]).prop( 'selected', data[i+1] == 1 );
					  $(arr[i]).find('option[value="'+data[i+2]+'"]').prop('selected', true);
             	}
             },
             "oLanguage": {
             	"sSearch": "",
             	"sSearchPlaceholder": "Search all columns"
             },
		}); 
		
		$(function(){ // this will be called when the DOM is ready
			 			 
			 $("#evidenceReport_dataTable_length").css('margin-top','8px');
			 $("#evidenceReport_dataTable_length").css('padding-left','35px');
			 
			mappingFeatureToBuild_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
			} );
			 
			 $('#evidenceReport_dataTable').on( 'change', 'select.editorNotification-active', function (event) {
				var arr = event.target.id.split('~');
				var buildId = arr[1].split(':')[1];
				var featureId = arr[0];
				var value = 0;
				var url="";
				if(event.target.value == "Yes"){
					value=1;
					url="mapping.productFeature.and.productBuild";
				}else{
					value=0;
					url="unmapping.productFeature.and.productBuild";
				}
								
				var formdata = new FormData();
		        formdata.append("featureId", featureId);
				formdata.append("buildId",buildId);
				formdata.append("productId", productId);
				formdata.append("isMapped", value);
											
				
				 $.ajax({
					   url:url,
						method: 'POST',
						contentType: false,
						data: formdata,
						dataType:'json',
						processData: false,
						success : function(data) {					
							console.log("success :");							
						},
						error : function(data) {
							console.log("error");
						}
					});			
				
			  });
		});
}