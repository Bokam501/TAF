<!doctype html>
<html>
  <head>   
   <script src="files/lib/metronic/theme/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
   <script src="files/lib/metronic/theme/assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
    <script src="jbPivot-master/build/jbPivot.min.js"></script>
    <script src="jbPivot-master/build/jbPivot.min.js"></script>
     <script src="jbPivot-master/src/pivot.js"></script>
      <script src="jbPivot-master/src/group_distinct.js"></script>
      <script src="jbPivot-master/src/formatter_default.js"></script>
      <script src="jbPivot-master/src/agregate_count.js"></script>
      <script src="jbPivot-master/src/agregate_sum.js"></script>
      <script src="jbPivot-master/src/agregate_average.js"></script>
      <script src="jbPivot-master/src/agregate_distinct.js"></script>
    <link rel="stylesheet" href="jbPivot-master/css/jbPivot.css">
  <style>
         body {
            padding: 30px;
         }
      </style>

      <script type="text/javascript">
      var urlToGetActivityStatusReport = "";
      var productId;
   	  var productSelectedId;
      var colName="";
      var colValues="";
      var dynamicValues="";
         $(document).ready(function() {
        	// loadPivotTable(colName, colValues);
        	 loadProductsBasedOnUserId();
        		
       		$('#productList_ul').change(function(){
       			generateActivityTaskEffortReport();
       			
       		}); 
         });
         function loadProductsBasedOnUserId(){		
        		$('#productList_ul').empty();				
        		$.post('common.list.user.associated.products',function(data) {	
        	        var ary = (data.Options);
        	        $.each(ary, function(index, element) {
        	    		$('#productList_ul').append('<option id="' + element.Value + '" ><a href="#">' + element.DisplayText + '</a></option>');
        	  		 });
        	   		$('#productList_ul').select2();	
        	   		
        	   		productId = $("#productList_ul").select2().find('option:selected').attr('id');
        	   		generateActivityTaskEffortReport();
        	   		productSelectedId= parseInt(productId);
        	   		console.log( productSelectedId);
        	   		
        	   		urlToGetActivityStatusReport = 'pivot.report.activity.task.effort?productId='+productSelectedId;
        	   		console.log(urlToGetActivityStatusReport);
        	   				
        	   		//    	$('#productList_ul').select2().val();	       
        		});
        	}

         function generateActivityTaskEffortReport(){
        		
        	 	productId = $("#productList_ul").find('option:selected').attr('id');
        		productId = (typeof productId == 'undefined') ? -1 : productId;
        		productSelectedId=productId;
        		urlToGetActivityStatusReport = 'pivot.report.activity.task.effort?productId='+productSelectedId;
        		
        			//openLoaderIcon(); 
        			$.ajax(
        			{
        			    type: "POST",
        			    url : urlToGetActivityStatusReport,
        			    cache:false,
        			    success: function(data) {
        			    	//closeLoaderIcon();
        			    	
        					//$("#reportTable").show();			
        					var data1=eval(data);
        			  		var cols = data1[0].COLUMNS;
        			  		var columnData=data1[0].DATA;
        			  		colData=data1[0].DATA;
        			  		dynamicValues="";
        			  		if(columnData == undefined){
        			  			columnData = [];
        			  			colData= [];
        			  		}
        			  		console.log("columnData.length>>>"+columnData.length);
        			  		for(var i = 0; i < columnData.length; ++i){
        			  		   //do something with obj[i]
        			  		   for(var ind in columnData[i]) {
        			  		        //console.log("{");
        			  		      dynamicValues=dynamicValues+"{";
        			  		        for(var vals in columnData[i][ind]){
        			  		        	var str="";
        			  		        	str=columnData[i][ind][vals]+"";
        			  		        	if(vals!='id')
        			  		        		str = '"' + str.replace(/^"*|"*$/, '') + '"';
        			  		            //console.log(vals, str);
        			  		        	dynamicValues=dynamicValues+vals+":"+str+",";
        			  		        }
        			  		        //console.log("}");
        			  		      dynamicValues=dynamicValues.replace(/,(?=[^,]*$)/, '');
        			  		      dynamicValues=dynamicValues+"}";
        			  		   }
        			  		   if(i!=(columnData.length-1)){
        			  		   		//console.log(",");
        			  		   		dynamicValues=dynamicValues+",";
        			  		   }
        			  		}
        			  		//console.log("dynamicValues>>>"+dynamicValues);
        			  		//alert("cols>>>"+cols);
        			  		//alert("colData>>>"+colData);	  	
        			  		loadPivotTable(dynamicValues); 		
        			  						
        				 	},
        		 		    error : function(data){					
        		 				//closeLoaderIcon();
        		 			},
        		 			complete : function(data){				
        		 				//closeLoaderIcon();
        		 			}
        				}
        			);

        	}
         function loadPivotTable(dynFieldValues){
        	 //console.log("dynFieldValues>>>>"+dynFieldValues);
        		//Pivot table code starts
        		var testString = '[' + dynFieldValues +']';
        		//console.log("testString>>>"+testString);
				//var json = $.parseJSON(testString);
				var json = JSON.stringify(eval("(" + testString + ")"));
				var obj = JSON.parse(json);
				//console.log("json>>"+json);
				//console.log("obj>>"+obj);
        		var i = 0;
            $("#pivot1").jbPivot(
                    {
                       fields: {
                          id: {field: 'id', sort: "asc", agregateType: "none"},
                          product: {field: 'product', sort: "asc", showAll: true, agregateType: "distinct", label: "Product"},
                          workpackage: {field: 'workpackage', sort: "asc", showAll: false, agregateType: "distinct", label: "Workpackage"},
                          activity: {field: 'activity', sort: "asc", showAll: false, agregateType: "distinct", label: "Activity"},
                          task: {field: 'task', sort: "asc", showAll: false, agregateType: "distinct", label: "Task"},
                          resource: {field: 'resource', sort: "asc", showAll: false, agregateType: "distinct", label: "Resource"},
                          status: {field: 'status', sort: "asc", showAll: false, agregateType: "distinct", label: "Status"},
                          //effort: {field: 'effort', sort: "asc", showAll: false, agregateType: "distinct", label: "Effort"},
                          actualStDate: {field: 'actualStDate', sort: "asc", showAll: false, agregateType: "distinct", label: "Actual Start Date"},
                          actualEndDate: {field: 'actualEndDate', sort: "asc", showAll: false, agregateType: "distinct", label: "Actual End Date"},
                          plannedStDate: {field: 'plannedStDate', sort: "asc", showAll: false, agregateType: "distinct", label: "Planned Start Date"},
                          plannedEndDate: {field: 'plannedEndDate', sort: "asc", showAll: false, agregateType: "distinct", label: "Planned End Date"},
                          remarks: {field: 'remarks', sort: "asc", showAll: false, agregateType: "distinct", label: "Remarks"},
                          
                          count: {agregateType: "count", groupType: "none", label: "Count"},
                          sum: {field: 'effort', agregateType: "sum", groupType: "none", label: "Effort"},
                          average: {field: 'effort', agregateType: "average", groupType: "none", label: "Average Effort", formatter: function(V, f) {
                                var res = null;
                                if (typeof(V) === "number") {
                                   res = V.toFixed(2);
                                }
                                return res;
                             }}   
                       },
                       xfields: [],
                       yfields: [],
                       zfields: [],
                       data: obj,
                       
                       l_all: "All",
                       l_unused_fields: "Available fields"
                    }
            );
            
        		//Pivot table code ends
        	}
      </script>

   </head>
   <body>
   	  <div id="productList_dd">									
		<select class="form-control input-small select2me" id="productList_ul">
		</select>										
	  </div>
	  <div>&nbsp;</div>
      <div id="pivot1">
      </div>
      <a id="reload" href="#">reload</a>
      
   </body>



</html>