var urlToGetTemplateReport = "";
var engagementId;
var productId;
var collectionId;
var selectedCollectionId;
var selectedEngagementId=0;
var productSelectedId;
var templateId;
var selectedTemplateId;
var testFactoryId;
var testFactoryName;
var ProductId;
var productName;
var activityId;
var activityName;

var isFirstLoad = true;
/* Task code Ends */
function showPivotTableAdvancedTreeData(){	
	var jsonObj={"Title":"",				
	    	 	"urlToGetTree": 'pivot.report.view.advanced.tree?type=1',
	    	 	"urlToGetChildData": 'pivot.report.view.advanced.tree?type=1',	    	 	
	 };	 
	 TreeLazyLoading.init(jsonObj);	 
}

jQuery(document).ready(function() {	
	
	QuickSidebar.init(); // init quick sidebar
	pivotTableActivityLevel(0);
	//setBreadCrumb(userRole);
	//createHiddenFieldsForTree();
	setPageTitle("Reports");
	$('#treeStructure-portlet-light .portlet-title .tools').css('margin-right','12px');
	//getTreeData('pivot.report.view.advanced.tree');
	showPivotTableAdvancedTreeData();
	
	$("#treeContainerDiv").on("loaded.jstree", function(evt, data){
	   var defaultNodeId = "j1_1";
	   $.jstree.reference('#treeContainerDiv').select_node(defaultNodeId);
   });
	
	$("#treeContainerDiv").on("select_node.jstree",
	     function(evt, data){
			
   			var entityIdAndType =  data.node.data;
   			var arry = entityIdAndType.split("~");
   			var key = arry[0];
   			var type = arry[1];
   			var name = arry[2];
   			var title = data.node.text;
			var date = new Date();
		    var timestamp = date.getTime();
		    var nodeType = type;
		    var parent = data.node.parents;
		    var prodObj=parent[0];
		    var factObj=parent[1];
		    var prodArry = prodObj.split("~");		    
		    var pid=prodArry[1];
		    if(factObj != undefined){
		    	var factArry=factObj.split("~");
		    	var factid=factArry[1];
		    }
		     
		    console.log("type>>"+type);
		    console.log("nodeType>>>"+nodeType);
		    testFactoryId = factid;	 
		    ProductId = pid;
		    activityId = key;	
	        /*if(type == "TestFactory"){
	        	testFactoryId = factid;	        	
	        } else if(type == "Product"){
	        	ProductId = key;				
			}else if(type == "MongoCollection"){
				activityId = key;				
			}*/
		    if(type == "MongoCollection"){
		    	pivotTableActivityLevel(key);
		    }
	        console.log("testFactoryId>>"+testFactoryId);
	        console.log(ProductId);
	        console.log("activityId>>>"+activityId);
	        if(type == "ReportNames"){
	        	//loadTemplateList(testFactoryId,ProductId,activityId);
	        	//alert(key.split("PARENT")[0]);
	        	loadTemplateDetailsById(key.split("PARENT")[0]);
	        }
		}
	);
	//$('#templateList_ul').change(function(){
			//loadTemplateDetailsById();
			
		//});
});
$('input:button[name=save]').click(function() {
    var name = $('iframe[name=select_frame]').contents().find('#cubeNameSelect').val();
    alert("name>>"+name);
});
function loadTemplateList(fid,pid,cid){
	   $('#templateList_ul').empty();	   
	   var i=0;
	   var urltoPost='pivot.rest.template.report.byparams?factoryId='+fid+'&productId='+pid+'&collectionId='+cid;
	   $.post(urltoPost,function(data) {	
	        var ary = (data.Options);
	        //$('#templateList_ul').append('<option id="0" ><a href="#">Choose</a></option>');
	        $.each(ary, function(index, element) {
	        	if(i==0){
	        		$('#templateList_ul').append('<option id="-1" ><a href="#">Choose</a></option>');
	        		$('#templateList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
	        	}else{
	        		$('#templateList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
	        	}
	  		 });
	   		$('#templateList_ul').select2();	
	   		 
		});
}
function loadTemplateDetailsById(tempId){
	  	//var tempId = $("#templateList_ul").select2().find('option:selected').attr('id');
	$("#pivotDataTable").hide();
	$("#pivotReportFrame").show();
		console.log("tempId>>>"+ tempId);
		
		if(tempId!=-1){
		var urlToGetTempDetails = 'pivot.rest.template.list.id?templateId='+tempId;
			 //openLoaderIcon(); 
			$.ajax(
			{
			    type: "POST",
			    url : urlToGetTempDetails,
			    cache:false,
			    success: function(data) {
			    		
						var data1=data;
						var datArray = data1.split("~");
						var productVal=datArray[0];
						var cubNameVal=datArray[1];
						var confVal=datArray[2];
						
						loadProductAndEngagementNames(productVal);
						$('#myIframe').attr('src', "pivotfinalreport.html?paramCubeNameValue="+cubNameVal+"&paramPivotConfigValue="+confVal); 
						
				 	}
		 		    
				}
			); 
		}else{
			callAlert("Invalid Report...");
		}
	}
function loadTemplateDetailsByIdOnPopUp(tempId){
  	//var tempId = $("#templateList_ul").select2().find('option:selected').attr('id');
	console.log("tempId>>>"+ tempId);
	
	if(tempId!=-1){
	var urlToGetTempDetails = 'pivot.rest.template.list.id?templateId='+tempId;
		 //openLoaderIcon(); 
		$.ajax(
		{
		    type: "POST",
		    url : urlToGetTempDetails,
		    cache:false,
		    success: function(data) {
		    		
					var data1=data;
					//console.log("data1>>>"+data1);
					var datArray = data1.split("~");
					var productVal=datArray[0];
					var cubNameVal=datArray[1];
					var confVal=datArray[2];
					//var confObj=JSON.parse(confVal);
					//console.log("productVal>>>"+productVal);
					//console.log("cubNameVal>>>"+cubNameVal);
					//console.log("confVal>>>>"+confVal);
					//var newTemp = confVal.replace(/"/g, "'");
					//console.log("newTemp>>>>"+newTemp);
					loadProductAndEngagementNames(productVal);
					$('#myPopUpIframe').attr('src', "pivotfinalreport.html?paramCubeNameValue="+cubNameVal+"&paramPivotConfigValue="+confVal); 
					
			 	}
	 		    
			}
		); 
	}else{
		callAlert("Invalid Report...");
	}
}

function pivotTableActivityLevel(mongoCollectionId){
	var url="";
	$("#pivotReportFrame").hide();
	$("#pivotDataTable").show();
	url='pivot.rest.report.list.byId?collectionId='+mongoCollectionId+'&jtStartIndex=0&jtPageSize=10000';
	//url='workpackage.executiondetails.productorbuildlevel.list?productBuildId='+productBuildId+'&productId='+prodId+'&jtStartIndex=0&jtPageSize=10000';
	assignPivotDataTableValues(url, "parentTable", "", "");
}

var pivotJsonData='';
var pivotDTFlag = '';



function assignPivotDataTableValues(url,tableValue, row, tr){
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
				pivotJsonData = data;
				if(!pivotDTFlag)
					pivot_Container(data, "240px");
				else				
					reloadDataTableHandler(data, pivot_oTable);
				
			}else if(tableValue == "childTable1"){
				//wpAuditHistory_Container(data, row, tr);
			}else if(tableValue == "childTable2"){
				//wpTestRunJobs_Container(data, row, tr);
			}else if(tableValue == "childTable3"){
				//wpTestRunCases_Container(data, row, tr);				
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

function pivot_Container(data, scrollYValue){
	try{
		if ($('#pivot_dataTable').length>0) {
			$('#pivot_dataTable').dataTable().fnDestroy(); 
		}
	} catch(e) {}
	
	pivot_oTable = $('#pivot_dataTable').dataTable( {
		"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
		paging: true,	    			      				
		destroy: true,
		searching: true,
		bJQueryUI: false,
		"bScrollCollapse": true,
		autoWidth: false,
		bAutoWidth:false,
		"sScrollX": "100%",
       "sScrollXInner": "100%",
       "scrollY":"100%",
       /*fixedColumns:   {
           leftColumns: 3,
           rightColumns: 1
       }, */
      /* "fnInitComplete": function(data) {
    	   var searchcolumnVisibleIndex = [2,18,19]; // search column TextBox Invisible Column position
    	   if(!workPackageDTFlag){
    		   $('#pivot_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
    	   workPackageDTFlag=true;
		   reInitializeDataTableWKP();
	   },  */
	   buttons: [
		         {
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend: 'excel',
	                    	title: 'PivotAdvancedReports',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'csv',
	                    	title: 'PivotAdvancedReports',
	                    	exportOptions: {
	                            columns: ':visible'
	                        }
	                    },
	                    {
	                    	extend: 'pdf',
	                    	title: 'PivotAdvancedReports',
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
         columnDefs: [
              /*{ targets: [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18], visible: true},
              { targets: '_all', visible: false }*/
          ],
                  
         aaData:data,                 
	    aoColumns: [	        
			{ mData: "templateId", className: 'disableEditInline' , sWidth: '5%',
				mRender: function(data, type, full){
					
					var tID = full.templateId;
					console.log("hiiiiii"+tID);
					
					return ('<a class="details-control"  onclick="openIframePopup('+tID+')"  style="color: #0000FF;">'+data+'</a>');
				}
			},	
			
			{ mData: "reportType", className: 'disableEditInline' , sWidth: '5%'},
			 
			
		        	
		      { mData: "templateName",className: 'editable',sWidth: '6%'},
				
				
			
			       
			{ mData: "description", className: 'disableEditInline' , sWidth: '6%'},	
			{ mData: "userName", className: 'disableEditInline' , sWidth: '5%'},	
			{ mData: "createdDate", className: 'disableEditInline' , sWidth: '5%'},		
			{ mData: null,sWidth: '3%',		 
            	bSortable: false,
            	mRender: function(data, type, full) {		
            		var tID = full.templateId;
					console.log("img temp id>>>>"+tID);
           		 var img = ('<div style="display: flex;">'+
     	       		'<button style="border: none; background-color: transparent; outline: none;margin-left: 5px;">'+
     	       				'<img src="css/images/delete.png" class="details-control img1" style="width: 24px;height: 24px;"></button>'+		
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
	 
	//-----------------
	 $(function(){ // this will be called when the DOM is ready 
	    
		 pivot_oTable.DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        } );
	    } );
	   
		/* Use the elements to store their own index */
	   $("#pivot_dataTable_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input").each( function (i) {
			this.visibleIndex = i;
		} );
		
	 	$("#pivot_dataTable_wrapper .DTFC_LeftWrapper .DTFC_LeftFootWrapper th input").keyup( function () {
			/* If there is no visible index then we are in the cloned node */
			var visIndex = typeof this.visibleIndex == 'undefined' ? 0 : this.visibleIndex;
			
			/* Filter on the column (the index) of this element */
			pivot_oTable.fnFilter( this.value, visIndex );
		} );
	    
	    // ----- test step child table -----
	    $('#pivot_dataTable tbody').on('click', 'td a .iframePopup', function () {
	    	var tr = $(this).closest('tr');
	    	var row = pivot_oTable.DataTable().row(tr);
	    	//openIframePopup(row, tr);
   		});
	    
	    // ----- export child table -----
	    $('#pivot_dataTable tbody').on('click', 'td button .wp-details-control2', function () {
	    	var tr = $(this).closest('tr');
	    	var row = pivot_oTable.DataTable().row(tr);
	    	wpTestRunJobHandler(row, tr);    		
	    	
   		});	  
	    
	    $("#pivot_dataTable_length").css('margin-top','8px');
		$("#pivot_dataTable_length").css('padding-left','35px');
		
		
		
		   
		   $('#pivot_dataTable tbody').on( 'click', 'td button .img1', function () {
			   
			   //
			   var deleteTableRow  = $('#pivot_dataTable').DataTable();
			   var trElem = $(this).closest("tr");// grabs the button's parent tr element
		        var firstTd = $(trElem).children("td")[0]; //takes the first td which would have your Id
		        //alert("id>>"+$(firstTd).text())
		       
	        	
	        	deleteReport($(firstTd).text(),$(this).parents('tr'),deleteTableRow); 
		        
		   } );
	} ); 
}
function deleteReport(tId,trval,objs){
	 var alertMsg = "Are you sure want to delete the report?";
	 var flag = bootbox.confirm(alertMsg, function(result) {		 
		 if(result){
			 objs.row(trval).remove().draw();
			 callConfirmSuccess('pivot.rest.template.delete.id?templateId='+tId);
		 }      
       return true;
	}) ;
	callConfirm("Deleted Successfully...");
	//window.location.reload(true);
}

/*function deleteReport(tId){
	var deleteTableRow  = $('#pivot_dataTable').DataTable();
	deleteTableRow.row( $(this).parents('tr') ).remove().draw();
	var urlToGetTempDetails = 'pivot.rest.template.delete.id?templateId='+tId;
	 openLoaderIcon(); 	 
		$.ajax(
		{
		    type: "POST",
		    url : urlToGetTempDetails,
		    cache:false,
		    success: function(data) {
		    		closeLoaderIcon();
		    		callAlert("Report deleted successfully...");
		    		window.location.reload(true);
			 }
			    
		}); 
}*/
function openIframePopup(tID){
	//$("#pivotReportContainer #pivotReportDTContainer").empty();
	$("#pivotReportContainer").modal();
	loadTemplateDetailsByIdOnPopUp(tID);
	$("#pivotReportContainer #pivotReportDTContainer").append($('#myPopUpIframe'));
	//$("#pivotReportDTContainer").append();
	
}

function createTable(){
	$("#openPivotReportContainer").modal();
}
