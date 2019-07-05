var recommendedTestPlanJsonObj="";
var recommendedTestPlan_oTable='';
var clearTimeoutRecommendedTestRunPlan='';
var getPlanDataLength = 1;
var RecommentedTestPlan = function() {
  
   var initialise = function(jsonObj){
	   recommendedTestPlanJsonObj = jsonObj;
	   
	   $("#recommendedTestPlanPdMgmContainer h4").text("");
	   $("#recommendedTestPlanPdMgmContainer h4").text(jsonObj.Title);
	
	   assignValuesToRecommendedTestRunPlaan(jsonObj.listURL);
   };
		return {        
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

function assignValuesToRecommendedTestRunPlaan(url){
	//openLoaderIcon();
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
			if(data.length == 0) {
				//callAlert("No data available!");
			}
			recommendedTestPlanContainer(data);
		},
		  error : function(data) {
			 //closeLoaderIcon();  
		 },
		 complete: function(data){
			//closeLoaderIcon();
		 }
	});	
}

function reInitializeDTTestRunPlan(){
	clearTimeoutRecommendedTestRunPlan = setTimeout(function(){				
		recommendedTestPlan_oTable.DataTable().columns.adjust().draw();
		clearTimeout(clearTimeoutRecommendedTestRunPlan);
	},100);
}

//--- Product Build started -----

function recommendedTestPlanHeader(){
	var childDivString = '<table id="recommendedTestRunPlan_dataTable" class="cell-border compact" cellspacing="0" width="100%">'+
	'<thead>'+
		'<tr>'+
			'<th class="dataTableChildHeaderTitleTH">Id</th>'+
			'<th class="dataTableChildHeaderTitleTH">Testcase Name</th>'+
			/*'<th class="dataTableChildHeaderTitleTH">Feature Name</th>'+*/
			/*'<th class="dataTableChildHeaderTitleTH">Category</th>'+*/			
			'<th class="dataTableChildHeaderTitleTH">ST</th>'+	
			'<th class="dataTableChildHeaderTitleTH">NT</th>'+	
			'<th class="dataTableChildHeaderTitleTH">GT</th>'+	
			'<th class="dataTableChildHeaderTitleTH">ET</th>'+	
			'<th class="dataTableChildHeaderTitleTH">CT</th>'+	
			'<th class="dataTableChildHeaderTitleTH">BT</th>'+				
			'<th class="dataTableChildHeaderTitleTH">HFT</th>'+	
			'<th class="dataTableChildHeaderTitleTH">UST</th>'+	
			'<th class="dataTableChildHeaderTitleTH">LFT</th>'+	
			'<th class="dataTableChildHeaderTitleTH">Test Bed</th>'+	
			'<th class="dataTableChildHeaderTitleTH">Probability</th>'+	
			/*'<th class="dataTableChildHeaderTitleTH">Plan Date</th>'+	*/
			

		'</tr>'+
	'</thead>'+
	'<tfoot><tr></tr></tfoot>'+
	'</table>';		
	
	return childDivString;	
}

function recommendedTestPlanContainer(data){
	
	try{
		if ($("#recommendedTestPlanDTContainer").children().length>0) {
			$("#recommendedTestPlanDTContainer").children().remove();
		}
	} 
	catch(e) {}
	
	var childDivString = recommendedTestPlanHeader(); 			 
	$("#recommendedTestPlanDTContainer").append(childDivString);
	
	var planTime='';
	if(data.length>0){
		planTime = data[0].planUptainTime;
		if(planTime == null)
			planTime='Build analysis recommendations are not available. Please initiate analysis.';
	}else{
		planTime='Build analysis recommendations are not available. Please initiate analysis.';
	}
	$("#recommendedTestPlanPdMgmContainer .modal-header h5").text("Analysis date : " + planTime);
	
	
	var emptytr = emptyTableRowAppending(13);  // total coulmn count	
	 $("#recommendedTestRunPlan_dataTable tfoot tr").html('');     			  
	 $("#recommendedTestRunPlan_dataTable tfoot tr").append(emptytr);

	recommendedTestPlan_oTable = $("#recommendedTestRunPlan_dataTable").dataTable( {
		 	"dom": '<"top"Bf<"clear">>rt<"bottom"ilp<"clear">>',
			paging: true,	    			      				
			destroy: true,
			searching: true,
			bJQueryUI: false,
		    "sScrollX": "100%",
	       "sScrollXInner": "100%",
	       "scrollY":"100%",
	       "bScrollCollapse": true,	       
	       "fnInitComplete": function(data) {
			  var searchcolumnVisibleIndex = []; // search column TextBox Invisible Column position
     		  var headerItems = $('#recommendedTestRunPlan_dataTable_wrapper tfoot tr th');
			   headerItems.each( function () { 
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
				reInitializeDTTestRunPlan();
		   },  
		   buttons: [		             
			         {
		                extend: 'collection',
		                text: 'Export',
		                buttons: [
		                    {
		                    	extend: 'excel',
		                    	title: 'Intelligent TestPlan',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'csv',
		                    	title: 'Intelligent TestPlan',
		                    	exportOptions: {
		                            columns: ':visible'
		                        }
		                    },
		                    {
		                    	extend: 'pdf',
		                    	title: 'Intelligent TestPlan',
		                    	exportOptions: {
		                            columns: ':visible'
		                        },
		                        orientation: 'landscape',
		                        pageSize: 'LEGAL'
		                    },	                    
		                ],	                
		            },
		            'colvis',					
					{					
					text: '<img src="css/images/analytics.jpg" class="getPlanHandler" style="width:15px; height:15px; cursor:pointer;" title="Get Plan" />',
						action: function ( e, dt, node, config ) {					
							getPlanHandler();
						}
					},			
	         ], 	         
        aaData:data,		    				 
	    aoColumns: [		
           { mData: "id",className: 'disableEditInline', sWidth: '3%' },		
           { mData: "title",className: 'disableEditInline' , sWidth: '5%' },	
		   /*{ mData: "feature",className: 'disableEditInline' , sWidth: '15%' },		*/	   
           /*{ mData: "recommendationCategory",className: 'disableEditInline' , sWidth: '5%' },*/  
		   { mData: "ST",className: 'disableEditInline', sWidth: '3%' },
		   { mData: "NT",className: 'disableEditInline', sWidth: '3%' },
		   { mData: "GT",className: 'disableEditInline', sWidth: '3%' },
		   { mData: "ET",className: 'disableEditInline', sWidth: '3%' },
		   { mData: "CT",className: 'disableEditInline', sWidth: '3%' },
		   { mData: "BT",className: 'disableEditInline', sWidth: '3%' },
		   { mData: "HFT",className: 'disableEditInline', sWidth: '3%' },
		   { mData: "UST",className: 'disableEditInline', sWidth: '3%' },
		   { mData: "LFT",className: 'disableEditInline', sWidth: '3%' },
		   { mData: "testbed",className: 'disableEditInline', sWidth: '7%' },
		   { mData: "probability",className: 'disableEditInline', sWidth: '3%' }, 
		   /*{ mData: "planUptainTime",className: 'disableEditInline', sWidth: '10%' },*/
		   
       ],      
       "oLanguage": {
    	   "sSearch": "",
    	   "sSearchPlaceholder": "Search all columns"
       },     
	});
	
	// ------
	 $(function(){ // this will be called when the DOM is ready
		 
		 //var resultText="Count :"
		 var url = 'get.ise.recommendation.testcase.category.count?buildId='+recommendedTestPlanJsonObj.buildId;
		 
		  $.ajax({
			type: "POST",
			url:url,
			contentType: "application/json; charset=utf-8",
			dataType : 'json',
			success : function(data) {		
				//closeLoaderIcon();			
				//resultText = JSON.stringify(data.Record);
				var arrList = data.Record;
				var resultString='';
				var tempStr='';
				//if(arrList.length>0){
					for(key in arrList){
						tempStr = '<span class="badge badge-default" style="background:#076">'+key+':'+arrList[key]+'</span>'+' : ';
						resultString += tempStr;
					}
					if(resultString.length >0) {
						resultString=(resultString).toString().substring(0,resultString.length-1);
					}
				//}
					$("#recommendedTestRunPlan_dataTable_wrapper .dt-buttons p").html('');
				$("#recommendedTestRunPlan_dataTable_wrapper .dt-buttons").append('<p style="margin: 5px 0px 0px 5px;float: right;">Category summary : '+resultString+'</p>');	
			
			},
			  error : function(data) {
				 //closeLoaderIcon();  
			 },
			 complete: function(data){
				//closeLoaderIcon();
			 } 
		  });
	 
		$("#recommendedTestRunPlan_dataTable_length").css('margin-top','8px');
		$("#recommendedTestRunPlan_dataTable_length").css('padding-left','35px');
		
		$('#recommendedTestRunPlan_dataTable').DataTable().columns().every( function () {
	        var that = this;
	        $('input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                	.search( this.value, true, false )
	                    .draw();
	            }
	        });
	    });
		 
	// Activate an inline edit on click of a table cell
   /* $('#recommendedTestRunPlanPdMgmDTContainer').on( 'click', 'tbody td.editable', function (e) {
    	editorProductBuild.inline( this, {
            submitOnBlur: true
        } );
    });	
    
    $('#recommendedTestRunPlanPdMgmDTContainer').on( 'change', 'input.editorProductBuild-active', function () {
    	editorProductBuild
            .edit( $(this).closest('tr'), false )
            .set( 'status', $(this).prop( 'checked' ) ? 1 : 0 )            
            .submit();
    }); 
	*/
	 });
	 
	 
	 
	// ------
	 $(function(){ // this will be called when the DOM is ready
		 
		 //var resultText="Count :"
		 var url = 'get.ise.recommendation.testcase.summary.Details?productId='+recommendedTestPlanJsonObj.productId+'&buildId='+recommendedTestPlanJsonObj.buildId;
		 
		  $.ajax({
			type: "POST",
			url:url,
			contentType: "application/json; charset=utf-8",
			dataType : 'json',
			success : function(data) {		
					$('#recommendedTestcaseSummary p').html('');
					if(getPlanDataLength == 0) data.Record.recommendetedTestCases=0;
					$('#recommendedTestcaseSummary').html('<p>Summary : '+data.Record.recommendetedTestCases+' TestCases recommended for execution out of a total of '+data.Record.totalTestCases+' testcases </p>');
					getPlanDataLength = 1;
			},
			  error : function(data) {
				 //closeLoaderIcon();  
			 },
			 complete: function(data){
				//closeLoaderIcon();
			 } 
		  });
	 });
}

function getPlanHandler(){
	$("#recommendedLoaderIcon").show();
	var url="process.ise.recommendation.Testcases.add?productId="+recommendedTestPlanJsonObj.productId+"&buildId="+recommendedTestPlanJsonObj.buildId;
	$.ajax({
		  type: "POST",
		  url:url,
		  contentType: "application/json; charset=utf-8",
		  dataType : 'json',
		  success : function(data) {		
			  $("#recommendedLoaderIcon").hide();
			
			if(data.Result=="ERROR"){
    		    data = [];						
			}else{
				data = data.Records;
			}
			if(data.length == 0) {
				getPlanDataLength = 0;
				//callAlert("No data available!");
			}
			recommendedTestPlanContainer(data);
		},
		  error : function(data) {
			  $("#recommendedLoaderIcon").hide();  
		 },
		 complete: function(data){
			 $("#recommendedLoaderIcon").hide();
		 }
	});
	
}