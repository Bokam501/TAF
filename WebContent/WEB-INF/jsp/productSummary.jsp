<script type="text/javascript">

function getProductBuildDetailsByProductId() {
	openLoaderIcon();			
	var productManagementDTJsonDataTemp={};
	productManagementDTJsonDataTemp.data = function productManagementDTJsonDataTest (){return productManagementDTJsonData[0];}
	productSelectedRow = productManagementDTJsonDataTemp;
	productVersionResultHandler(productManagementDTJsonData[0].productId, productManagementDTJsonDataTemp, '');
	$("#productVersionPdMgmContainer").modal();

	/*var url ='get.product.build.by.productId?productId='+productId+'&jtStartIndex=0&jtPageSize=1000';
	 $.ajax({
		  type: "POST",
		  url: url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}					
			//jsonObj.data = data;
			productSummaryBuildContainer(data);
			$('#productBuildPdSummaryContainer_div_modal').modal();
			
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	*/
	
}
function getProductVersionDetailsByProductId(){
	openLoaderIcon();
	var productManagementDTJsonDataTemp={};
	productManagementDTJsonDataTemp.data = function productManagementDTJsonDataTest (){return productManagementDTJsonData[0];}
	productSelectedRow = productManagementDTJsonDataTemp;
	productVersionResultHandler(productManagementDTJsonData[0].productId, productManagementDTJsonDataTemp, '');
	$("#productVersionPdMgmContainer").modal();
	/*var url ='administration.product.version.list?productId='+productId+'&jtStartIndex=0&jtPageSize=1000';
	 $.ajax({
		  type: "POST",
		  url: url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			closeLoaderIcon();
			if(data.Result=="ERROR"){
      		    data = [];						
			}else{
				data = data.Records;
			}					
			//jsonObj.data = data;
			productSummaryProductVersion_Container(data);
			$('#productVersionPdSummaryContainer_div_modal').modal();
			
		  },
		  error : function(data) {
			 closeLoaderIcon();  
		 },
		 complete: function(data){
			closeLoaderIcon();
		 }
	});	*/
}


function productSummaryBuildTable(){
	var childDivString = '<table id="productSummaryBuild_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th>Build No</th>'+
			'<th>Name</th>'+
			'<th>Description</th>'+
			'<th>Build Date</th>'+
			'<th>Build Type</th>'+
			'<th>Cloned Build</th>'+			
			'<th>Status</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}


var clearTimeoutDTRelatedProductBuild='';
var productSummaryProductBuild_oTable='';
function reInitializeDTRelatedProductBuild(){
	clearTimeoutDTRelatedProductBuild = setTimeout(function(){				
		productSummaryProductBuild_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTRelatedProductBuild);
	},200);
}


function productSummaryBuildContainer(data){
	
	try{
		if ($("#productBuildPdSummaryContainer").children().length>0) {
			$("#productBuildPdSummaryContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = productSummaryBuildTable(); 			 
	$("#productBuildPdSummaryContainer").append(childDivString);		
	productSummaryProductBuild_oTable = $("#productSummaryBuild_dataTable").dataTable( {
		 	"dom": '<"top"Bf<"clear">>rt<"bottom"ip<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,
	       /*fixedColumns:   {
	           leftColumns: 1,
	       }, */
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex = [6]; // search column TextBox Invisible Column position
			  $('#productSummaryBuild_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
	    	   reInitializeDTRelatedProductBuild();
	    	   
		   },  
		   buttons: [
		             
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Product Version Build',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Product Version Build',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Product Version Build',
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
	              
	          ],
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "productBuildNo",className: 'disableEditInline', sWidth: '15%' },		
           { mData: "productBuildName",className: 'disableEditInline' , sWidth: '15%' },	
           { mData: "productBuildDescription",className: 'disableEditInline' , sWidth: '15%' },
           { mData: "productBuildDate", className: 'disableEditInline', sWidth: '15%',
          		mRender: function (data, type, full) {
		           	 if(type == "display"){
		           		data = full.productBuildDate;
		           	 }	           	 
		             return data;
	             }
           },
           { mData: "buildTypeName",className: 'disableEditInline' , sWidth: '5%', editField: "buildTypeId",
	    		 mRender: function (data, type, full) {		           	 
		           	 if(type == "display"){
		           		data = full.buildTypeName;
		           	 }	           	 
		             return data;
	             },		   
	       },
           { mData: "clonedProductBuildName",className: 'disableEditInline' , sWidth: '10%' },
           { mData: null,
        	 sWidth: '2%',
             mRender: function (data, type, full) {
            	  if ( type === 'display' ) {
                        return '<input type="checkbox" class="editorProductBuild-active">';
                    }
                    return data;
                },
                className: "dt-body-center"
            },
            
       ],
       rowCallback: function ( row, data ) {
           $('input.editorProductBuild-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	});
	
	$(function(){ // this will be called when the DOM is ready 
		productSummaryProductBuild_oTable.DataTable().columns().every( function () {
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


var clearTimeoutDTRelatedProductVersion='';
var productSummaryProductVersion_oTable='';
function reInitializeDTRelatedProductVersion(){
	clearTimeoutDTRelatedProductVersion = setTimeout(function(){				
		productSummaryProductVersion_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutDTRelatedProductVersion);
	},200);
}

function productSummaryProductVersionDetailTable(){
	var childDivString = '<table id="productSummary_ProductVersion_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th id="productVersion">Product Version</th>'+
			'<th>Description</th>'+
			'<th>Web Application URL</th>'+
			'<th>Source Location</th>'+
			'<th>Binary Location</th>'+
			'<th>Release Date</th>'+
			'<th>Status</th>'+
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
		'</tr>'+
	'</tfoot>'+	
	'</table>';		
	
	return childDivString;	
}

function productSummaryProductVersion_Container(data){
	
	
	try{
		if ($("#productVersionPdSummaryContainer").children().length>0) {
			$("#productVersionPdSummaryContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = productSummaryProductVersionDetailTable(); 			 
	$("#productVersionPdSummaryContainer").append(childDivString);
		
	// --- editor for the product Version started -----
	
	
	productSummaryProductVersion_oTable = $("#productSummary_ProductVersion_dataTable").dataTable( {
		 	"dom": '<"top"Bf<"clear">>rt<"bottom"ip<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,
	       /*fixedColumns:   {
	           leftColumns: 1,
	       }, */
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex = [6]; // search column TextBox Invisible Column position
			  $('#productSummary_ProductVersion_dataTable_wrapper .dataTables_scroll .dataTables_scrollFootInner th').each( function () {
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
	    	   reInitializeDTRelatedProductVersion();	
		   },  
		   select: true,
		   buttons: [
	             	//{ extend: "create", editor: editorProductVersion },	  
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Product Version',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Product Version',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Product Version',
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
	              
	          ],
        aaData:data,		    				 
	    aoColumns: [	        	        
           { mData: "productVersionName",className: 'disableEditInline', sWidth: '15%' },		
           { mData: "productVersionDescription",className: 'disableEditInline', sWidth: '20%' },		
           { mData: "webAppURL",className: 'disableEditInline', sWidth: '20%' },		
           { mData: "targetSourceLocation",className: 'disableEditInline', sWidth: '12%' },		
           { mData: "targetBinaryLocation",className: 'disableEditInline', sWidth: '12%' },	
           { mData: "releaseDate", className: 'disableEditInline', sWidth: '15%',
           		mRender: function (data, type, full) {
		           	 if (full.action == "create"){
		           		data = convertDTDateFormatAdd(data, ["releaseDate"]);
		           	 }else if(type == "display"){
		           		data = full.releaseDate;
		           	 }	           	 
		             return data;
	             }
           },
           { mData: null,
              mRender: function (data, type, full) {
            	  if ( type === 'display' ){
                        return '<input type="checkbox" class="editorProductVersion-active">';
                    }
                    return data;
                },
                className: "dt-body-center"
            },
           
       ],
       rowCallback: function ( row, data ) {
           $('input.editorProductVersion-active', row).prop( 'checked', data.status == 1 );
       },
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	}); 
	// ------
	$(function(){ // this will be called when the DOM is ready 
		productSummaryProductVersion_oTable.DataTable().columns().every( function () {
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


</script>
<script type="text/javascript">


function showProductSummaryDetails(productId){
	openLoaderIcon();
	
	var urlToGetProductSummaryOfSelectedProductId = 'product.summary?productId='+productId;
	$.ajax({
		type:"POST",
	 	contentType: "application/json; charset=utf-8",
		url : urlToGetProductSummaryOfSelectedProductId,
		dataType : 'json',
		cache:false,
		success : function(data) {
			closeLoaderIcon();
			var result=data.Records;
			var prodName;
			var description;
			var customerName;
			var engagementName;
			var productMode;
			var pVersionCount;
			var pBuildCount;
			var testCaseCount;
			var testCaseStepsCount;
			var testSuiteCount;
			var featuresCount;
			var dcCount;
			var wpCount;
			var activeWpCount;
			var unMappedTCFeatureCount;
			var unMappingPercentage;
			var mappedTCFeatureCount;
			var activeWpCount;
			var ptTestManagersCount;
			var ptTestLeadsCount;
			var ptTestersCount;
			var pcTestManagersCount;
			var pcTestLeadsCount;
			var pcTestersCount;
			var mappedResourcePoolsCount;
			var coreResourcesCount;
			var productEnvironmentsCount;
			var productEnviCombinationCount;
			var testRunPlanCount;
			var testScriptCount;
			var featureTestCoverage;
			var testCaseAutomationCoverage;
			var orphanTestCases;
			var defectCount;
			
			if(data.Records != null && data.Records.length === 0) {
				 $("#spProductName").text("");
			} else {
				$.each(result, function(i,item){ 
					prodName = item.productName;
					description = item.description;
					customerName = item.customerName;
					engagementName = item.engagementName;
					productMode= item.productMode;
					if(productMode == "Project") $(".optional").hide(); else $(".optional").show();
					pVersionCount = (item.pVersionCount == null) ? 0 : item.pVersionCount;
					pBuildCount = item.pBuildCount;
					featuresCount = item.featuresCount;
					testCaseCount = item.testCaseCount;
					testCaseStepsCount=item.testCaseStepsCount;
					testSuiteCount = item.testSuiteCount;
					dcCount = item.dcCount;
					wpCount = item.wpCount;
					activeWpCount = item.activeWpCount;
					unMappedTCFeatureCount = item.unMappedTCFeatureCount;
					unMappingPercentage = item.unMappingPercentage;
					mappedTCFeatureCount= item.mappedTCFeatureCount;
					ptTestManagersCount = item.testManagersCount;
					ptTestLeadsCount = item.testLeadsCount;
					ptTestersCount = item.testersCount;
					pcTestManagersCount = item.pcTestManagersCount;
					pcTestLeadsCount = item.pcTestLeadsCount;
					pcTestersCount = item.pcTestersCount;
					mappedResourcePoolsCount = item.mappedResourcePoolCount;
					//coreResourcesCount = item.productCoreResourceCount;
					productEnvironmentsCount = item.productEnvironmentsCount;
					productEnviCombinationCount = item.productEnviCombinationCount;
					testRunPlanCount = item.testRunPlanCount;
					testScriptCount = item.testScriptCount;
					featureTestCoverage = item.featureTestCoverage;
					testCaseAutomationCoverage=item.testCaseAutomationCoverage;
					orphanTestCases=item.orphanTestCases;
					defectCount=item.defectCount;			
					
					/*if(item.productType != null){
						document.getElementById("hdnproductType").value = item.productType;
					}*/
				});
			} 
			$("#spProductName").text(prodName);
			$("#spDescription").text(description);
			$("#spCustomer").text(customerName);
			$("#spEngagement").text(engagementName);
			$("#spProductMode").text(productMode);
			$("#sppVersionCount").text(pVersionCount);
			$("#sppBuildCount").text(pBuildCount);
			$("#spFeaturesCount").text(featuresCount);
			$("#spTcCount").text(testCaseCount);
			$("#spTstepsCount").text(testCaseStepsCount);
			$("#spTsCount").text(testSuiteCount);
			$("#spDCCount").text(dcCount);
			$("#spWPCount").text(wpCount);
			$('#spActiveWPCount').text(activeWpCount);
			$('#spUnMappedTCFeatureCount').text(unMappedTCFeatureCount+" ("+unMappingPercentage+"%)");
		//	$('#spUnMappingPercentage').text(unMappingPercentage+"%");
			$('#spMappedTCFeatureCount').text(mappedTCFeatureCount);
			$('#spPtTestManagersCount').text(ptTestManagersCount);
			$('#spPtTestLeadsCount').text(ptTestLeadsCount);
			$('#spPtTestersCount').text(ptTestersCount);
			$('#spPcTestManagersCount').text(pcTestManagersCount);
			$('#spPcTestLeadsCount').text(pcTestLeadsCount);
			$('#spPcTestersCount').text(pcTestersCount);
			$('#spMappedResourcePoolsCount').text(mappedResourcePoolsCount);
			//$('#spCoreResourcesCount').text(coreResourcesCount);
			$('#spProductEnvironmentsCount').text(productEnvironmentsCount);
			$('#spProductEnviCombinationCount').text(productEnviCombinationCount);
			$('#spTestRunPlanCount').text(testRunPlanCount);
			$('#testcaseScriptCount').text(testScriptCount);
			
			$('#featureTestCoverage').text(featureTestCoverage);
			$('#testCaseAutoMationCoverage').text(testCaseAutomationCoverage);
			$('#orphanTestCases').text(orphanTestCases);
			$('#defectCount').text(defectCount);
		},
		error:function(data){
			closeLoaderIcon();
			
		},
		complete:function(data) {
			closeLoaderIcon();
			
			$.each($("table.table-striped tbody tr td:nth-child(2) span"), function(ind, elem) {
				var value = parseInt($(elem).text());
				
				if(!isNaN(value)) {
					if (value == 0) $(elem).addClass("label-danger").removeClass("label-proper");
					else $(elem).addClass("label-proper").removeClass("label-danger");
				}
			});
			var res = data.responseJSON.Records;
			if(res.length !== 0) {
				$.each(res, function(i,item){
					if (parseInt(item.unMappingPercentage) == 0) $("#spUnMappedTCFeatureCount").addClass("label-proper").removeClass("label-danger");
					else $("#spUnMappedTCFeatureCount").addClass("label-danger").removeClass("label-proper");
				});
			}
		}
	});
}
</script>

<div class="row"  id="pdtSummaryView" style="max-height:100%;">
	<div class="col-md-6">
		<div class="table-scrollable">
			<table class="table table-striped table-hover" id="dfnTable">
				<thead>
				<tr height="30">
					<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Product Details</th>
					<th>
					</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td>Name :
					</td>
					<td><span id="spProductName"></span>
					</td>
				</tr>
				<tr>
					<td>Description  : 
					</td>
					<td><span id="spDescription"></span></td>
				</tr>
				<tr>
					<td>Engagement :
					</td>
					<td><span id="spEngagement" ></span>
					</td>
				</tr>
				<tr>
					<td>Customer :
					</td>
					<td><span id="spCustomer"></span>
					</td>
				</tr>
					<tr>
					<td>Mode :
					</td>
					<td><span id="spProductMode"></span>
					</td>
				</tr>
				</tbody>
			</table>
		</div>

		<div class="table-scrollable">
			<table class="table table-striped table-hover" id="opnTable">
				<thead>
				<tr height="30">
					<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Assets</th>
					<th>
					</th>
				</tr>
				</thead>
				<tbody>
			<tr>
				<td><a href="javascript:getProductVersionDetailsByProductId()">Versions</a> : 
				</td>
				<td>
				
					<span class="label label-sm label-proper" id="sppVersionCount"></span>
				
				</td>
					</tr>
					<tr>
						<td><a href="javascript:getProductBuildDetailsByProductId()">Builds</a> : 
						</td>
						<td>
						
						<span class="label label-sm label-proper" id="sppBuildCount"></span>
						
						</td>
					</tr>					
					<tr>
						<td>Features : 
						</td>
						<td><span class="label label-sm label-proper" id="spFeaturesCount"></span></td>
					</tr>										
					<tr>
						<td>Environment Combinations : 
						</td>
						<td><span class="label label-sm label-proper" id="spProductEnviCombinationCount"></span></td>
					</tr>										
				</tbody>
			</table>
		</div>
		<!--
		<div class="table-scrollable">
			<table class="table table-striped table-hover" id="opnTable">
				<thead>
				<tr height="30">
					<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Activity Management Operations</th>
					<th>
					</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td>Workpackages : 
					</td>
					<td><span class="label label-sm label-proper" id="sppVersionCount"></span></td>
				</tr>
					<tr>
						<td>Activities : 
						</td>
						<td><span class="label label-sm label-proper" id="sppBuildCount"></span></td>
					</tr>					
					<tr>
						<td>Activity Types : 
						</td>
						<td><span class="label label-sm label-proper" id="spFeaturesCount"></span></td>
					</tr>
					<tr>
						<td>Clarifications : 
						</td>
						<td><span class="label label-sm label-proper" id="spProductEnviCombinationCount"></span></td>
					</tr>
					<tr>
						<td>UseCases : 
						</td>
						<td><span class="label label-sm label-proper" id="spProductEnviCombinationCount"></span></td>
					</tr>	
					<tr>
						<td>Workflows : 
						</td>
						<td><span class="label label-sm label-proper" id="spProductEnviCombinationCount"></span></td>
					</tr>
						
				</tbody>
			</table>
		</div>	
		-->
	</div>
	
	<div class="col-md-6">
		<div class="table-scrollable">
			<table class="table table-striped table-hover" id="opnTable">
				<thead>
				<tr height="30">
					<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Test Assets</th>
					<th>
					</th>
				</tr>
				</thead>
				<tbody>
					<tr>
						<td>Testcases : 
						</td>
						<td><span class="label label-sm label-proper" id="spTcCount"></span></td>
					</tr>
					<tr>
						<td>Teststeps : 
						</td>
						<td><span class="label label-sm label-proper" id="spTstepsCount"></span></td>
					</tr>
					<tr>
						<td>Testcase Scripts : 
						</td>
						<td><span class="label label-sm label-proper" id="testcaseScriptCount"></span></td>
					</tr>
					<tr>
						<td>Testsuites : 
						</td>
						<td><span class="label label-sm label-proper" id="spTsCount"></span></td>
					</tr>
					<tr>
						<td>Test Plans : 
						</td>
						<td><span class="label label-sm label-proper" id="spTestRunPlanCount"></span></td>
					</tr>					
				</tbody>
			</table>
		</div>
		
		<div class="table-scrollable">
			<table class="table table-striped table-hover" id="opnTable">
				<thead>
				<tr height="30">
					<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Traceability</th>
					<th>
					</th>
				</tr>
				</thead>
				<tbody>
					<tr>
						<td>Features - Testcases Coverage :
						</td>
						<td><span class="label label-sm label-proper" id="featureTestCoverage"></span></td>
					</tr>
					<tr>
						<td>Orphan Testcases :
						</td>
						<td><span class="label label-sm label-proper" id="orphanTestCases"></span>						
						</td>
					</tr>										
					<tr>
						<td>Testcases Automation Coverage :
						</td>
						<td><span class="label label-sm label-proper" id="testCaseAutoMationCoverage"></span>						
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="table-scrollable">
			<table class="table table-striped table-hover" id="opnTable">
				<thead>
				<tr height="30">
					<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Test Operations</th>
					<th>
					</th>
				</tr>
				</thead>
				<tbody>
					<tr>
						<td>Workpackages :
						</td>
						<td><span class="label label-sm label-proper" id="spWPCount"></span></td>
					</tr>
					<!-- <tr>
						<td>Testcase Executions :
						</td>
						<td><span class="label label-sm label-proper" id="spUnMappedTCFeatureCount"></span>						
						</td>
					</tr> -->
					<tr>
						<td>Defects : 
						</td>
						<td><span class="label label-sm label-proper" id="defectCount"></span></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="table-scrollable">
			<table class="table table-striped table-hover" id="resTable">
				<thead>
				<tr height="30">
					<th style="color:black;font-size:16px;padding-top:3px;padding-bottom:3px">Resource 	</th>
					<th>
					</th>
				</tr>
				</thead>
				<tbody>
					<tr>
						<td>Test Manager(s) :
						</td>
						<td><span class="label label-sm label-proper" id="spPtTestManagersCount"></span></td>
					</tr>
					<tr>
						<td>Test Lead(s) :
						</td>
						<td><span class="label label-sm label-proper" id="spPtTestLeadsCount"></span></td>
					</tr>
					<tr>
						<td>Tester(s) :
						</td>
						<td><span class="label label-sm label-proper" id="spPtTestersCount"></span></td>
					
						<tr class="optional">
						<td>Product Core Test Manager(s) :
						</td>
						<td><span class="label label-sm label-proper" id="spPcTestManagersCount"></span></td>
					</tr>
						<tr class="optional">
						<td>Product Core Test Lead(s) :
						</td>
						<td><span class="label label-sm label-proper" id="spPcTestLeadsCount"></span></td>
					</tr>
					<tr class="optional">
						<td>Product Core Tester(s) :
						</td>
						<td><span class="label label-sm label-proper" id="spPcTestersCount"></span></td>
					</tr>
					<tr class="optional">
						<td>Mapped Resource Pool(s) :
						</td>
						<td><span class="label label-sm label-proper" id="spMappedResourcePoolsCount"></span></td>
					</tr>
				<!-- 	<tr class="optional">
						<td>Core Resources :
						</td>
						<td><span class="label label-sm label-proper" id="spCoreResourcesCount"></span></td>
					</tr> -->
				</tbody>
			</table>
		</div>
	</div>
</div>
	
<style>
#dfnTable td, #opnTable td, #resTable td {
	border-top:0px;
} 
#dfnTable th, #opnTable th, #resTable th {
	border-bottom:1px solid #ddd;
} 
#dfnTable tbody tr td:nth-child(1) {
	width:200px !important;
}
#opnTable tbody tr td:nth-child(1), #resTable tbody tr td:nth-child(1) {
	width:300px !important;
}

/* Medium devices (desktops, 992px and up) */
@media (min-width: 992px) {
 #pdtSummaryView {max-height:285px;overflow:auto;}
}
/* Large devices (large desktops, 1200px and up) */
@media (min-width: 1200px) { 
 #pdtSummaryView {max-height:285px;overflow:auto;}
}
/* Xtra Large devices (xtra large desktops, 1600px and up) */
@media (min-width: 1500px) { 
 #pdtSummaryView {max-height:380px;overflow:auto;}
}
@media (min-width: 1600px) { 
 #pdtSummaryView {max-height:500px;overflow:auto;}
}
@media (min-width: 1800px) { 
 #pdtSummaryView {max-height:800px;overflow:auto;}
}
</style>