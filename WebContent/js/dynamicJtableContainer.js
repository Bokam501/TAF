var DynamicJtableContainer = function() {
  
   var initialise = function(jsonObj){
	   dynamicJTableContainerSummary(jsonObj);
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

function dynamicJTableContainerSummary(jsonObj){	
	$("#div_DynamicJTableSummary").find("h4").text(jsonObj.Title);
	$("#div_DynamicJTableSummary").modal();	
}

function clearDynamicJTableDatas(){
	try{
		if ($(".dynamicJTable").length>0) {
			 $('.dynamicJTable').jtable('destroy');
		}
		$("#div_DynamicJTableSummary").find(".jScrollContainerResponsiveTop").empty();
	} catch(e) {}
}