var headerTitleArr=[];
var mappingVersionToTestCase_oTable='';
var clearTimeoutDTmappingVersionToTestCase='';

function mappingVersionToTestCase(url){	
	/*var versionId=-1;
	var buildId=0;
	
	var url = 'get.testcase.version.mappedList?productId='+productId+'&versionId='+versionId+'&buildId='+buildId+'&jtStartIndex=0&jtPageSize=10000';*/
	openLoaderIcon();
	 $.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			if(data != undefined)
				versionToTestCase(data);			
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});		
}

function versionToTestCase(data){	
	$("#versionTestcaseMappingDT").html('');
	
	try{		
		if ($("#versionTestcaseMappingDT").children().length>0) {
			$("#versionTestcaseMappingDT").children().remove();
		}
		
	} catch(e) {}
	
	var titleName="";
	var atarget = [];
	var stitle = []; 
	var scrollValue=350;
	var evidenceStr=""; 
	var titleCrUc="Mapping_VersionToTestCase"
	var data1=eval(data);	
	var columnData=data1.DATA;
	var cols = data1.COLUMNS;	
  	 
  	 var content='<table id="versionReport_dataTable" class="display datatable" style="width:100%"><thead><tr>';  	 
  	 content+='<th>Testcases</th>';
  	 var mappingVersionToTestCaseFooter='<tfoot><tr><th></th>'  	
  	 atarget[0]=0;
  	headerTitleArr=[];
  	 $(cols).each(function(index, element){
  		 titleName='';
  	     atarget[index]=index+1;
  	     titleName=titleName+element.title;  	     
  	     content+='<th>'+titleName+'</th>';  	     
  	     mappingVersionToTestCaseFooter+='<th></th>';
  	     headerTitleArr[index]=titleName;
  	 });
     content+='</tr></thead>';	
     mappingVersionToTestCaseFooter+='</tr></tfoot>';
     content+=mappingVersionToTestCaseFooter; 
     content+='</table>';     
  	$("#versionTestcaseMappingDT").append(content);	
  	
  	mappingVersionToTestCase_oTable = $('#versionReport_dataTable').dataTable( {
			dom: "Bfrtilp",
			paging: true,
			destroy: true,
			searching: true,			
			"scrollX":true,
			"scrollY":"100%",
			"bSort": false,
			"fnInitComplete": function(data) {
				   reInitializeDTMappingVersionToTestCase();			   
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
                     "aTargets":0,
                 }, 
                 { 
            	 mRender: function (data, type, full, meta) {
            		 var titleStr = (headerTitleArr[meta.col-1]);
                	 if(data == null || data == ''){
                		 return '<input id="'+full[0]+'~'+titleStr+'" type="checkbox" class="editorNotification-active">';
                	 }else if ( type === 'display' ){
                            return '<input id="'+full[0]+'~'+titleStr+'" type="checkbox" class="editorNotification-active">';
                     }
                        return data;
                    },                 
                 "aTargets":atarget,								   		                    						    				                
                }        
             ],
             rowCallback: function ( row, data) {       
            	 var arr = $('input.editorNotification-active', row);
            	 for(var i=0;i<data.length-1;i++){
            		 $(arr[i]).prop( 'checked', data[i+1] == 1 );
             	}
             },
             "oLanguage": {
             	"sSearch": "",
             	"sSearchPlaceholder": "Search all columns"
             },
		});
		
		$(function(){ // this will be called when the DOM is ready
			 			 
			 $("#versionReport_dataTable_length").css('margin-top','8px');
			 $("#versionReport_dataTable_length").css('padding-left','35px');
			 
			 $('#versionReport_dataTable').on( 'change', 'input.editorNotification-active', function (event) {
				var arr = event.target.id.split('~');
				var versionId = arr[1].split(':')[1];
				var st = arr[0].indexOf('[');				
				var end = arr[0].indexOf(']');
				var firstIndexValue = arr[0];
				var testCaseId = firstIndexValue.substr(st+1, end-1);
				var value = 0;
				var url="";
				if(event.currentTarget.checked){
					value=1;
					url="mapping.testcase.and.productVersion";
				}else{
					value=0;
					url="unMapping.testcase.and.productVersion";
				}
								
				var formdata = new FormData();
		        formdata.append("versionId", versionId);
				formdata.append("testCaseId",testCaseId);
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

function reInitializeDTMappingVersionToTestCase(){
	clearTimeoutDTmappingVersionToTestCase = setTimeout(function(){				
		mappingVersionToTestCase_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTmappingVersionToTestCase);
	},200);
}