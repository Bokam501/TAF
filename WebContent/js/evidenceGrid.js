var EvidenceGrid = function() {
	
   var initialise = function(jsonObj){	    
	    evidenceReport(jsonObj);	   
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

function evidenceReport(jsonObj){	
	$("#evidencesubTitle h4").text("");
	$("#evidencesubTitle h4").text(jsonObj.Title);
	
	$("#evidenceContainer").modal();	
	
	var url="evidence.grid.list?testRunNo="+jsonObj.evidenceWorkpackageID+"&testRunConfigurationChildId=-1";
	 $.ajax({
		  type: "POST",
			url:url,
			success : function(data) {					
					console.log("success :");
					evidenceData(data);
			},
			error : function(data) {
				console.log("error");
			}
		});
}

function closeEvidenceReport() {	
	$("#evidenceContainer").fadeOut("normal");	
}

function evidenceData(data){
	var scrollValue=180;
	var evidenceStr=""; 
	
	var data1=eval(data);	
	var columnData=data1.DATA;
	var cols = data1.COLUMNS;
	var headerValue=data1.HEADER;
	
	/*subTitle*/
	$("#evidencesubTitle h5").text("");		
	for(var i=0;i<headerValue.length;i++){
		evidenceStr += '   '+headerValue[i].header;
	}		
	$("#evidencesubTitle h5").text(evidenceStr);
		
	var content = "<table id='evidenceReport_dataTable' class='display datatable' style='width:100%;'><thead>";
	content +='<tr>';	
	
	try{
		if ($('#evidenceReport_dataTable').length>0) {
			$('#evidenceReport_dataTable').dataTable().fnDestroy(); 
		}
		$("#evidenceReport").empty();
	} catch(e) {}

  	 var atarget = [];
  	 var stitle = []; 
  	content += '<th > Test Case ~ Test Step ~ Screen Shot</th>';
  	atarget[0] =0;
  	 $(cols).each(function(index, element){
  		 var titleName="";
  	     atarget[index] =index+1;
  	   titleName=titleName+element.title;
  	  
  	var arr= titleName.split("#");
  	
  	 stitle[index] =arr[0];							  	 
  	  //content += '<th >'+arr[0]+'<br><a href='+arr[1]+' style="color: blue;">Device Log  , </a><a href='+arr[2]+' style="color: blue;">Job Log</a></th>';
  	 	content += '<th >'+arr[0]+'<br><a href='+arr[1]+' style="color: blue;">Job Log</a></th>';
  	 });
   content +=' </tr></thead>';	
  	  content += "</table>";
  	$("#evidenceReport").append(content);
		$('#evidenceReport_dataTable').dataTable( {
			paging: true,
			destroy: true,
			searching: true,
			//bJQueryUI: false,
			"scrollX":true,
			"scrollY":"100%",
			"bSort": false,
			 "aaData": columnData ,
			 "aoColumnDefs":[ 
			                 {
			                     "aTargets":0,
			                 }, 
			                 {
			                		
   		                     "mRender": function ( data, type, row ) {	
   		                    	
   		                    	if(data==null || data==''){
   		                    		return '<p>No ScreenShot Available</p>';
   		                    	}else{
   		                    	  
   		                    	 return '<img width="200px" BORDER="0" height="200px" src="'+data+'" alt="The system cannot find the path specified" />';
   		                    	}
   		                 

   		                  },
   		               "aTargets":atarget,								   		                    						    				                
			                 	}
   		            
   		                 ]
		});  
}

