var FeatureOverallViewDetails = function() {
  
   var initialise = function(jsonObj){
	   listFeatureDetailsOverall(jsonObj);
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

var isLoad = false;			
var exceptedOutput='';
var featureOverallJsonObj='';
function listFeatureDetailsOverall(jsonObj){	
	featureOverallJsonObj=jsonObj;
	$("#featureDetailedViewsModal").find("h4:first").text(jsonObj.Title);
	if(jsonObj.featureName == null || jsonObj.featureName == "null")jsonObj.featureName='';
	if(jsonObj.featureId == null || jsonObj.featureId == "null")jsonObj.featureId='';
	$("#featureDetailedViewsModal").find("h4:first").text("Feature Details :- "+ jsonObj.featureId + "- "+jsonObj.featureName );
	
	//$("#tablistRelatedFeatureDetails li").removeClass('active');
	//$("#tablistRelatedFeatureDetails li").eq(0).addClass('active');
	
	getFeatureDetailsForResultOverall();	
	getRelatedFeatureTestcases();
	
	//$("#relatedFeatureDetails li").removeClass('active');
	//$("#relatedFeatureDetails li").eq(0).addClass('active');	
	
	//hideRelatedFeatureTabs();
	//$("#relatedFeatureDetails").show();
	//$("#relatedTestCases").removeClass("active in");
	//$("#relatedFeatureDetails").addClass("active in");
	
	$("#featureDetailedViewsModal").modal();		
}

/*function hideRelatedFeatureTabs(){
	$("#relatedFeatureDetails").hide();
	$("#relatedTestCases").hide();
}*/

function getRelatedFeatureTestcases(){
	//hideRelatedFeatureTabs();	
	//$("#relatedTestCases").show();
	
	var jsonObj=featureOverallJsonObj;	
	var containerID = "relatedTestcasesChild1";
	var urlToGetFeaturesOfSpecifiedProductId = 'product.feature.testcase.mappedlist?productFeatureId='+jsonObj.featureId+'&jtStartIndex=0&jtPageSize=10000';		
	listFeaturesOfSelectedProduct(urlToGetFeaturesOfSpecifiedProductId,"240px" ,"childTable1" ,containerID ,0);
	$("#relatedTestcaseDetails").html('');
	//$("#relatedTestcaseDetails").html('<div id="productFeatureChild1"></div>');
	$("#relatedTestcaseDetails").html('<div id="'+containerID+'"></div>');
	
	/*var url = 'product.feature.testcase.mappedlist?productFeatureId='+jsonObj.featureId+'&jtStartIndex=0&jtPageSize=10000';		
	openLoaderIcon();
	
	$.ajax({
	    type: "POST",
	    url : url,
	    cache:false,
	    success: function(data) {
			if(data != null){
				data = data.Records;
				//relatedFeatureTestCases(data);
			}
	    	closeLoaderIcon();
	    	
	    },error : function(data){
			closeLoaderIcon();
		}
	}); */  
}

function getFeatureDetailsForResultOverall(){
	//$("#relatedFeatureDetails").show();
	var jsonObj=featureOverallJsonObj;
	$.ajax({
		type: "POST",
        contentType: "application/json; charset=utf-8",
		url : jsonObj.featureOverAllDetailsUrl,
		dataType : 'json',
		success : function(data) {
			var result=data.Records;
			var executionName="";
			var executionTime="";
			var executionStatus="";
			var isapproved = "";
			$("#featureDetailedViewsModal #currCaseName, #currCaseId").text("");
			$("#featureDetailedViewsModal #featureId").empty();
			$("#featureDetailedViewsModal #featureName").empty();
			$("#featureDetailedViewsModal #featureCode").empty();
			$("#featureDetailedViewsModal #parentFeatureName").empty();
			$("#featureDetailedViewsModal #featurePriority").empty();
			
			$("#featureDetailedViewsModal #mappedTestcaseCount").empty();
			
			//////////////////////// - modified started -
			$("#featureDetailedViewsModal #productId").text('--');
			$("#featureDetailedViewsModal #productName").text('--');
			$("#featureDetailedViewsModal #productType").text('--');
			
			$("#featureDetailedViewsModal #featureId").text('--');
			$("#featureDetailedViewsModal #featureName").text('--');
			$("#featureDetailedViewsModal #featureCode").text('--');
			$("#featureDetailedViewsModal #parentFeatureName").text('--');
			$("#featureDetailedViewsModal #featurePriority").text('--');

			$("#featureDetailedViewsModal #mappedTestcaseCount").text('--');
			
			//////////////////////// - modified ended -
			
			$.each(result, function(i,item){				
				if(item.productFeatureId != null){
					$("#featureDetailedViewsModal #featureId").text(item.productFeatureId);		
				}
				if(item.productFeatureName != null){
					$("#featureDetailedViewsModal #featureName").text(item.productFeatureName);	
				}
				
				if(item.productFeatureCode != null){
					$("#featureDetailedViewsModal #featureCode").text(item.productFeatureCode);	
				}
				
				if(item.parentFeatureName != null){
					$("#featureDetailedViewsModal #parentFeatureName").append("<div style='font-size:small;'  >"+item.parentFeatureName+"</div>");
				}
				if(item.executionPriorityName != null){
					$("#featureDetailedViewsModal #featurePriority").append("<div style='font-size:small;'  >"+item.executionPriorityName+"</div>");
				}
				
				
				////////////////////////// - modified started -
				if(item.productId != null){
					$("#featureDetailedViewsModal #productId").text(item.productId);
				}
				if(item.productName != null){
					$("#featureDetailedViewsModal #productName").text(item.productName);
				}
				if(item.productType != null){
					$("#featureDetailedViewsModal #productType").text(item.productType);
				}
				if(item.productVersionName != null){
					$("#featureDetailedViewsModal #productVersionName").text(item.productVersionName);
				}
				if(item.productType != null){
					$("#featureDetailedViewsModal #productType").text(item.productType);	
				}
				if(item.featureId != null){
					$("#featureDetailedViewsModal #featureId").text(item.productFeatureId);
				}
								
				
				if(item.productFeatureName != null){
					$("#featureDetailedViewsModal #featureName").text(item.productFeatureName);
				}
				if(item.parentFeatureName != null){
					$("#featureDetailedViewsModal #parentFeatureName").text(item.parentFeatureName);
				}
				if(item.executionPriorityName != null){
					$("#featureDetailedViewsModal #featurePriority").text(item.executionPriorityName);
				}				
	
				
				if(item.createdDate != null){
					$("#featureDetailedViewsModal #createdDate").text(item.createdDate);	
				}
				if(item.modifiedDate != null){
					$("#featureDetailedViewsModal #modifiedDate").text(item.modifiedDate);	
				}
				
				
			
				if(item.mappedTestcaseCount != null){
					$("#featureDetailedViewsModal #mappedTestcaseCount").text(item.mappedTestcaseCount);		
				}
				
				
								
			});
			
			$('.hover-star').rating({ 
			 }); 					
		
			if(document.getElementById("paginationButton")!=null)
			document.getElementById("paginationButton").style.display = "block";	
			$("#featureDetailedViewsModal").modal();			
		}
	});	
}
