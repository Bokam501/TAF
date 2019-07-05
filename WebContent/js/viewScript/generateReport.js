var productId="";
var resourceId="";
var mappedFields=null;
var productFields=null;
var resourceFields=null;

function generateNewReport(){
	$("#generateReportFormID").show();
	//$("#saveReportID").show();
	
	initReportHanlder();
}

function initReportHanlder(){
	/*loadSearchStatusList('groupByList_ul', 'common.list.user.associated.products');*/
	loadSearchStatusCheckboxList('groupByList_ul', 'common.list.user.associated.products','productList');
	loadSearchStatusCheckboxList('resource_ul', 'common.user.list','resourceList');
}

function cancelReport(){
	$("#generateReportFormID").hide();
	//$("#saveReportID").hide();
}

/*function generateTaskEffortReport(){
	try{
			if ($('#taskEffortReportTable').length>0) {
				$("#taskEffortReportDiv").children(":last").remove();
				$('#taskEffortReportTable').remove();
			}
				//$("#generateReportFormID").children(":last").remove();
				$('#generateReportFormID').hide();
				$('#saveReportID').hide();
				var htmlTable = '<table id="taskEffortReportTable"><thead><tr></tr></thead></table>';
				$("#taskEffortReportDiv").append(htmlTable);
				$('#taskEffortReportDiv').show();
			//}
		} catch(e) {}
		templateId = $("#reportList_ul").find('option:selected').attr('id');
		templateId = (typeof templateId == 'undefined') ? -1 : templateId;
		var templateSelectedId=templateId;
		//alert("templateSelectedId>>>"+templateSelectedId);
		var urlToGetTaskTemplateReport = 'report.activity.task.effort.filters?templateId='+templateSelectedId;
		 
		 var scrollValue = "";
		 if(window.innerWidth < 1600){
			 scrollValue = "250px";
		 }
		 if(window.innerWidth > 1600){
			 scrollValue = "500px";
		 }
			openLoaderIcon(); 
			$.ajax(
			{
			    type: "POST",
			    url : urlToGetTaskTemplateReport,
			    cache:false,
			    success: function(data) {
			    	closeLoaderIcon();
			    	
					//$("#generateReportFormID").show();			
					var data1=eval(data);
			  		var cols = data1[0].COLUMNS;
			  		var columnData=data1[0].DATA;
			  		if(columnData == undefined){
			  			columnData = [];
			  		}
			  			  	
				   	 $('#taskEffortReportTable').dataTable({			   		
				   		paging: true,			
						destroy: true,
						bJQueryUI: false,
						"scrollX":true,
						"scrollY":"300px",
						dom: 'T<"clear">lfrtip',
						 tableTools: {
					            //"sSwfPath": "/tfwp/js/swf/copy_csv_xls_pdf.swf"
					            "sSwfPath": "//cdn.datatables.net/tabletools/2.2.2/swf/copy_csv_xls_pdf.swf"
					        },
						 oTableTools: {
					            "aButtons": [
					                "copy",
					                "print",
					                {
					                    "sExtends":    "collection",
					                    "sButtonText": "Save",
					                    "aButtons":    [ "csv", "xls", "pdf" ]
					                }
					            ]
					        },
				   		        "bDeferRender": true,
				   		        "bInfo" : false,
				   		        "bSort" : false,
				   		        "bDestroy" : true,
				   		        "bFilter" : false,
				                  "fnRowCallback": function( nRow, aData, iDisplayIndex) {
				                	$.each(aData, function(i,item){
				                		
				                	});	                
				                },  
				   		        "bPagination" : false,
				   		        "aaData": columnData,
				   		        "aoColumns": cols
				   		    });	   			
			  						
					 	},
			 		    error : function(data){					
			 				closeLoaderIcon();
			 			},
			 			complete : function(data){				
			 				closeLoaderIcon();
			 			}
					}
				);
	}
*/

function saveReport(){
	/*var changeRequestIdFromUI = []; 
	$("input:checkbox[name=radio1]:checked").each(function(){
		changeRequestIdFromUI.push($(this).attr('id'));
	});
	
	console.log("save :"+changeRequestIdFromUI);
	
	var assignee=$("#groupReport_dd .select2-chosen").text();
	//var status=$("#statusReport_dd .select2-chosen").text();		
	var priority=  $("#resourceReport_dd .select2-chosen").text();		 
	var reportdate = document.getElementById('planned_fromTodatepicker').value;
	if((status!="-")&&(status!="")){
		statusId = $("#groupReport_dd").find('option:selected').attr('id');
	}else{
		statusId=-1;
	}	*/
	mappedFields=new Array();
	productFields=new Array();
	resourceFields=new Array();
	//productId=$('#groupByList_ul').val();
	//alert("ProdcutId>>>"+productId);
	//resourceId=$('#resource_ul').val();
	//alert("resourceId>>>"+resourceId);
	var templateName=$("#templateReport_name").val();
	$("input:checkbox[name=mapFields]:checked").each(function(){
	    mappedFields.push($(this).val());
	});
	$("input:checkbox[name=productList]:checked").each(function(){
		productFields.push($(this).val());
	});
	$("input:checkbox[name=resourceList]:checked").each(function(){
		resourceFields.push($(this).val());
	});
	var plannedStartDate = DaterangePicker.fromDate();
	var plannedEndDate = DaterangePicker.toDate();
	/*console.log("productFields>>>"+productFields);
	console.log("resourceFields>>>"+resourceFields);
	//console.log("reportdate>>>"+reportdate);
	console.log("templateName>>>"+templateName);
	console.log("mappedFields>>>"+mappedFields);
	console.log("plannedStartDate>>>"+plannedStartDate);
	console.log("plannedEndDate>>>"+plannedEndDate);
	console.log("len>>>"+mappedFields.length);*/
	if(mappedFields.length==0)
		callAlert("Choose atleast one field to map..");
	else{
		//templateName,mappedFields,fromDate,toDate,statusValue,productValue,resourceValue
		//var urlToSaveTaskEffortTemplate ="";
		var urlToSaveTaskEffortTemplate = 'process.taskefforttemplate.add?templateName='+templateName+'&mappedFields='+mappedFields+'&fromDate='+plannedStartDate+'&toDate='+plannedEndDate+'&statusValue=&productValue='+productFields+'&resourceValue='+resourceFields;
		//console.log("url>>>"+urlToSaveTaskEffortTemplate);
		openLoaderIcon(); 
		$.ajax(
		{
		    type: "POST",
		    url : urlToSaveTaskEffortTemplate,
		    cache:false,
		    success: function(data) {
		    	closeLoaderIcon();	    	
				var data1=eval(data);
		  		callAlert("Report Template Created Successfully");
		  		//$("#generateReportFormID").hide();
		  		//loadReportTemplates();
				//generateTaskEffortReport();
		  		//loadSearchStatusList('reportList_ul', 'task.effort.templates.list');
		  		//window.refresh();
		  		location.reload();
		  		},
	 		    error : function(data){					
	 				closeLoaderIcon();
	 			},
	 			complete : function(data){				
	 				closeLoaderIcon();
	 			}
			}
		);
	}
}


/*function loadSearchStatusList(idValue, url){
	
	//$('#'+idValue).empty();	
	$.post(url, function(data) {		
	    var ary = (data.Options);
	    //console.log("ary>>>"+ary);
	    $.each(ary, function(index, element) {
	    	
	    	$('#'+idValue).append('<option id="' + element.Value + '" value="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');	
		});
	});
}*/
function loadSearchStatusCheckboxList(idValue, url,name){
	//$('#'+idValue).empty();	
	$.post(url, function(data) {		
	    var ary = (data.Options);
	    //console.log("ary>>>"+ary);
	    $.each(ary, function(index, element) {
	    	
	    	$('#'+idValue).append('<div> <input  type=checkbox name="'+name+'" id="' + element.Value + '" value="' + element.Value + '" >' + element.DisplayText + '</input></div>');
		});
	});
}
