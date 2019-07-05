var testingBrick='';
function clickOnbrick(e){
      
};   
   
   var recordArr=[];
   function getTotalWPCount(fileLocation) {
	   $("#loadingIconTM").css('top',"45%");	   
	   $("#loadingIconTM").show();
	   	   
	   $.ajax({
	        type: "POST",
	        contentType: "application/json; charset=utf-8",
	        url: fileLocation,
	        dataType: 'json',
	        success: function(data) {
	        	$("#loadingIconTM").hide();
	    		return data;	    
	        },
	        error: function(data){
	        	$("#loadingIconTM").hide();
	        },
	       complete: function(data) {
	    	   $("#loadingIconTM").hide();
	    	   recordArr = data.responseJSON.Records;
	    	   if(recordArr.length>0){
				   $("#overallStatusReport").show();
				   $('#overallStatusReport').empty();
					//$('#tileBrick1').empty();
					populateTilesViewWorkPkg(0);
			   }
	       }
	   });	   
   }
   
   function populateTilesViewWorkPkg(index){
	   //$("#loadingIconTM").css('top',"20px");	   
	  $("#loadingIconTM").show();
	   var urlToGetResourcesOfWorkPackage = 'workpackage.testcase.execution.summary?workpackageId='+recordArr[index].id;
	   $.ajax({
	        type: "POST",
	        contentType: "application/json; charset=utf-8",
	        url: urlToGetResourcesOfWorkPackage,
	        dataType: 'json',			
	        success: function(jsonData) {
	        	$("#loadingIconTM").hide();
	            var data = jsonData.Records; 
				brick="";
	            if(jsonData.length!=0){	            
		            for (var key in data) {
		            	var userId = data[key].userId;
		            	var timeSheetHours = data[key].timeSheetHours;		            
		                if(data[key].imageURI=="" || data[key].imageURI=="/Profile/" || data[key].imageURI == null){
		                	data[key].imageURI="css/images/noimage.jpg";
		                }else{
		                	data[key].imageURI="/Profile/"+data[key].imageURI;
		                }
		                var responses = fileExists(data[key].imageURI);
		                if (responses == 404){
		                	data[key].imageURI = "css/images/noimage.jpg";
		                }
		                var brick ='';
		                var executedTC =0;
		                var plannedTC = 0;
		                var startenddiff = 0;
		                var executeperday =0;
		                var executeperhr =0;
		                
		                executedTC = data[key].totalExecutedTesCases;
		                startenddiff = data[key].wpStartEnddayDiff;
		             //   console.log("executedTC--"+executedTC+"-startenddiff--"+startenddiff);	
		                if(startenddiff !=0 ){
		                	 executeperday = executedTC/startenddiff;
		                	 executeperday= executeperday.toFixed(0);
		                	 executeperhr = executedTC/(startenddiff*8);
		                	 executeperhr = executeperhr.toFixed(2);
		                }
		                var executedlasthour = 0;
		                if(data[key].executedLastHour != null){
		                	executedlasthour = data[key].executedLastHour;
		                }
		                var tc_notrun = 0;
		                if(data[key].p2totalNoRun != null){
		                	tc_notrun = data[key].p2totalNoRun;
		                }
		                var tc_blocked = 0;		                
		                if(data[key].p2totalBlock != null){
		                	tc_blocked = data[key].p2totalBlock;
		                }
		                var wpstatus = 'NA';
		                if(data[key].wpStatus != null){
		                	wpstatus = data[key].wpStatus;
		                }
		                var pass = data[key].p2totalPass;
		                var passpercent = 0;
		                if(executedTC != 0){
		                	passpercent = (pass/executedTC)*100;	
		                }
		                plannedTC = data[key].testCaseCountOfRunconfig; //testCaseCountOfRunconfig is total tcs associate with wp
		                var exe_percent = 0;
		                
		                if(plannedTC != 0){
		                	exe_percent = (executedTC/plannedTC)*100;		                	
		                	exe_percent =exe_percent.toFixed(0);		                	
		                }
		                	//(int) Math.floor((Math.random() * 100) + 1)
		                var daydifffromStart = data[key].wpnthDayfromStrart;
		                var daydiffWP = data[key].wpStartEnddayDiff;
		                
		                daydifffromStart = Math.abs(daydifffromStart);
		                var daydiffContent = "";
		                var beforeafter = data[key].executionBeforeAfter;
		                var plannedBeforeAfter = data[key].plannedBeforeAfterCurrentDate;
		                if(beforeafter == 'after'){//Based on 
		                //	console.log(recordArr[index].name+"==daydiff--"+daydifffromStart+"--execution --"+beforeafter); 	
		                	daydiffContent = daydifffromStart +" days";
		                }else if(beforeafter == 'before'){
		                //	console.log(recordArr[index].name+"==daydiff--"+daydifffromStart+"--execution --"+beforeafter);
		                	daydiffContent = "Started before "+daydifffromStart+" days from PlannedStartDate";
		                }else{//No TC Executed
		                	if (plannedBeforeAfter == 'after'){
		                		daydiffContent = 'Not Started';
		                	}else if (plannedBeforeAfter == 'before'){
		                		daydiffContent = daydifffromStart +" days";	
		                	}else{
		                		daydiffContent = daydifffromStart +" days";	
		                	}
		                }
		            	//console.log(recordArr[index].name+"==daydiff:"+daydifffromStart+"--execution --"+beforeafter+"-wpstatus-"+wpstatus);
		                		
		                /*brick  ='<div  id="brick_'+data[key].id+'" onclick="javascript:clickOnbrick(event);">';
					    brick +='<div class="col-md-4 col-lg-4 col-sm-6 col-xs-12" style="padding: 10px;"><div class="dashboard-stat green-haze more">';					    
					    brick +='<div class="row"><div class="col-md-7" style="padding: 5px;"><a class="more" style="cursor: default">'+recordArr[index].name+'</a></div>';
					    brick +='<div class="col-md-5" style="padding: 5px;"><span style="color:black;margin-right: 5px;">STATUS :&nbsp;</span><span class="badge" style="background:rgba(137, 196, 244, 0.22);">'+wpstatus+'</span>';
					    brick +='<!-- <a href="javascripts:;"><i class="m-icon-swapright m-icon-white" style="margin-left: 5px;"></i></a> --></div></div>';					    
					    brick +='<div class="tile double">';
					    brick +='<div class="tile-body" style="padding:1px 1px;">';				    
					    brick +='<div class="row">',
					    brick +='<div class="col-md-4 divAlign"><label>Test Beds</label></div><div class="col-md-3"><span>: '+data[key].testBedCount+'</span></div></div>';				    
					    brick +='<div class="row"><div class="col-md-4 divAlign"><label>Execution Status</label></div><div class="col-md-2"><span>:'+data[key].totalExecutedTesCases+"/"+data[key].totalWPTestCase+'</span></div><div class="col-md-6"><span>['+exe_percent+'%]</span></div></div>'
					    brick +='<div class="row"><div class="col-md-4 divAlign"><label>Execution Result</label></div><div class="col-md-8"><span>:</span><span class="badge" style="background: #83B645;">'+data[key].p2totalPass +'&nbsp;P</span><span>&nbsp;</span><span class="badge" style="background:#e35b5a;">'+data[key].p2totalFail+'&nbsp;F</span><span>&nbsp;</span><span class="badge" style="background: #E3A95A;">'+tc_notrun+'&nbsp;NR</span><span>&nbsp;</span><span class="badge" style="background: #e35b5a;">'+tc_blocked+'&nbsp;B</span></div></div>'
					    brick +='<div class="row"><div class="col-md-4 divAlign"><label>Schedule</label></div><div class="col-md-8"><span>: '+daydiffContent+' / '+data[key].wpStartEnddayDiff+' total days</span></div></div>';					   
					    brick +='<div class="row"><div class="col-md-4 divAlign"><label>Velocity</label></div><div class="col-md-4"><span>: '+executeperday+'/ day</span></div>',
					    //brick +='<div class="col-md-4"><span>: '+executedlasthour+'/1 hr</span></div></div>';
					    brick +='<div class="col-md-4"></div></div>';
					    brick +='<div class="row"><div class="col-md-4 divAlign"><label>Bugs Reported</label></div><div class="col-md-8"><span>: '+data[key].defectsCount+'</span></div></div></div>';*/
		            	
		            	brick  ='<div  id="brick_'+data[key].id+'" onclick="javascript:clickOnbrick(event);">';
					    brick +='<div class="col-md-6 col-lg-6 col-sm-6 col-xs-12" style="padding: 10px;"><div class="dashboard-stat green-haze more">';					    
					    brick +='<div class="row"><div class="col-md-9" style="padding: 5px;"><a class="more" style="cursor: default">'+recordArr[index].name+'</a></div>';
					    brick +='<div class="col-md-3" style="padding: 5px;"><span style="color:black;margin-right: 5px;">STATUS :&nbsp;</span><span class="badge" style="background:rgba(137, 196, 244, 0.22);">'+wpstatus+'</span>';
					    brick +='<!-- <a href="javascripts:;"><i class="m-icon-swapright m-icon-white" style="margin-left: 5px;"></i></a> --></div></div>';
					    brick +='<div class="tile double">';
					    brick +='<div class="tile-body" style="padding:1px 1px;">';				    
					    brick +='<div class="row">';
					    brick +='<div class="col-md-6 divAlign"><label>Test Beds</label></div><div class="col-md-3"><span>: '+data[key].jobsCount+'</span></div></div>';				    
					    brick +='<div class="row"><div class="col-md-6 divAlign"><label>Execution Status</label></div><div class="col-md-3"><span>: '+data[key].totalExecutedTesCases+"/"+data[key].testCaseCountOfRunconfig+'</span></div><div class="col-md-3"><span>['+exe_percent+'%]</span></div></div>';
					    brick +='<div class="row"><div class="col-md-6 divAlign"><label>Execution Result</label></div><div class="col-md-6"><span>: </span><span class="badge" style="background: #83B645;">'+data[key].p2totalPass +'&nbsp;P</span><span>&nbsp;</span><span class="badge" style="background:#e35b5a;">'+data[key].p2totalFail+'&nbsp;F</span><span>&nbsp;</span><span class="badge" style="background: #E3A95A;">'+tc_notrun+'&nbsp;NR</span><span>&nbsp;</span><span class="badge" style="background: #e35b5a;">'+tc_blocked+'&nbsp;B</span></div></div>';
					    brick +='<div class="row"><div class="col-md-6 divAlign"><label>Schedule</label></div><div class="col-md-6"><span>: '+daydiffContent+' / '+data[key].wpStartEnddayDiff+' total days</span></div></div>';					   
					    brick +='<div class="row"><div class="col-md-6 divAlign"><label>Velocity</label></div><div class="col-md-6"><span>: '+executeperday+'/ day</span></div>';
					    //brick +='<div class="col-md-6"><span>: '+executedlasthour+'/1 hr</span></div></div>';
					    brick +='<div class="col-md-6"></div></div>';
					    brick +='<div class="row"><div class="col-md-6 divAlign"><label>Bugs Reported</label></div><div class="col-md-6"><span>: '+data[key].defectsCount+'</span></div></div></div>';
					    
					    brick +='</div>';					    						
						brick +='</div></div></div>';		
			
	                   if(!($('#overallStatusReport').hasClass('tiles'))){
	                   		$('#overallStatusReport').addClass('tiles');
	                   		//$('#tileBrick1').append($(brick));
	                   }
						$('#overallStatusReport').append($(brick));
						//$('#tileBrick').append($(brick));
			            }
		            }else{
		            	$("#overallStatusReport").hide();
		            }},
		        complete: function(jsonData) {
					$("#loadingIconTM").hide();
	    	   		if(index<recordArr.length-1){
	    	   			index++;
						if(loadWorkPackage){
							populateTilesViewWorkPkg(index);  	   			
						}
	    	   		}
	    	   	},
				error: function (data){
					$("#loadingIconTM").hide();
				}			   		
       		});
   }
   
   function fileExists(fileLocation) {
	    var response = $.ajax({
	        url: fileLocation,
	        type: 'HEAD',
	        async: false
	    }).status;
	    return response;
	}   

	
	var productIds=[];
   function getProductIds(productIdsUrl,view) {
	   $("#loadingIconTM").css('top',"45%");	   
	   $("#loadingIconTM").show();
	   
	   $.ajax({
	        type: "POST",
	        contentType: "application/json; charset=utf-8",
	        url: productIdsUrl,
	        dataType: 'json',
	        success: function(data) {
	        	$("#loadingIconTM").hide();
	    		return data;	    
	        },
	        error: function(data){
	        	$("#loadingIconTM").hide();
	        },
	       complete: function(data) {
	    	   $("#loadingIconTM").hide();
	    	   productIds = data.responseJSON.Records;
	    	   if(view == dashboardTileView){
	    		   if(productIds.length>0){
					   $("#overallStatusReport").show();
					   $('#overallStatusReport').empty();
						//$('#tileBrick1').empty();
						populateTilesViewProduct(0);
				   }
	    	   }else if(view == workflowSummaryView){

	    		   if(productIds.length>0){
					   $("#overallStatusReport").show();
					   $('#overallStatusReport').empty();
						//$('#tileBrick1').empty();
						populateWorkFlowSummaryViewProduct(0);
				   }	    		   
	    	   }else if(view == workPackageRAGView){

	    		   if(productIds.length>0){
					   $("#overallStatusReport").show();
					   $('#overallStatusReport').empty();
						//$('#tileBrick1').empty();
					   populateWorkPackageRAGViewSummaryProduct(0);
				   }	    		   
	    	   }
	    	   
	       }
	   });	   
   }
   
   function populateTilesViewProduct(index){
	   //$("#loadingIconTM").css('top',"20px");	   
	  $("#loadingIconTM").show();
	 
	   var urlToGetResourcesOfProduct = 'product.activity.processSummary?productId='+productIds[index];
	   $.ajax({
	        type: "POST",
	        contentType: "application/json; charset=utf-8",
	        url: urlToGetResourcesOfProduct,
	        dataType: 'json',			
	        success: function(jsonData) {
	        	$("#loadingIconTM").hide();
	            var data = jsonData.Records; 
				brick="";
				if(jsonData.length!=0){	            
		            for (var key in data) {
						brick  ='<div  id="brick_'+productIds[index]+'" onclick="javascript:clickOnbrick(event);">';
					    brick +='<div class="col-md-6 col-lg-6 col-sm-6 col-xs-12" style="padding: 10px;"><div class="dashboard-stat green-haze more">';					    
					    brick +='<div class="row"><div class="col-md-12" style="padding: 5px;"><a class="more" style="cursor: default">'+data[key].productName+'</a></div>';
					    brick +='<!--<div class="col-md-3" style="padding: 5px;"><span style="color:black;margin-right: 5px;">STATUS :&nbsp;</span><span class="badge" style="background:rgba(137, 196, 244, 0.22);">'+data[key].status+'</span>';
					    brick +='<a href="javascripts:;"><i class="m-icon-swapright m-icon-white" style="margin-left: 5px;"></i></a> </div>--></div>';
					    brick +='<div class="tile double" style="width: 550px !important;">';
					    brick +='<div class="tile-body" style="padding:1px 1px;cursor:default">';				    
					    brick +='<div class="row">';
					    brick +='<div class="col-md-6 divAlign"><label>Active Workpackages</label></div><div class="col-md-3"><span>: '+data[key].activeWorkPackageCount+'/'+data[key].totalWorkPackageCount+'</span></div></div>';				    
					    brick +='<div class="row"><div class="col-md-6 divAlign"><label>Execution Status</label></div><div class="col-md-6"><span>: </span><span class="badge" style="background: rgba(137, 196, 244, 0.65);">'+data[key].openActivityCount+'&nbsp;Open</span><span>&nbsp;</span>'+
					    '<span class="badge" style="background: rgba(227, 169, 90, 0.5);">'+data[key].onHoldActivityCount+'&nbsp;OnHold</span><span>&nbsp;</span><span class="badge" style="background: rgba(131, 182, 69, 0.43);">'+data[key].completedActivityCount+' Complete</span></div></div>';
					    brick +='<div class="row"><div class="col-md-6 divAlign"><label>Health Index</label></div><div class="col-md-6"><span>: </span><span class="badge" style="background: rgba(131, 182, 69, 0.43);">'+data[key].scheduleVariance+' S.V</span><span>&nbsp;</span>'+
					    '<span class="badge" style="background: rgba(131, 182, 69, 0.43);">'+data[key].productQuality+' PQ </span></div></div>';
						
					    brick +='<div class="row"><div class="col-md-6 divAlign"><label>Velocity</label></div><div class="col-md-6"><span>: </span><span class="badge" style="background: #FFF;border-color: #ACB5C3;border-style: solid;border-width: thin;padding-top: 1px;">'+data[key].requiredRunRate+' Expected</span><span>&nbsp;</span>'+
					    '<span class="badge" style="background: rgba(131, 182, 69, 0.43);">'+data[key].productivity+' Current</span></div></div>';						
						
						brick +='<div class="row"><div class="col-md-6 divAlign"><label>Resources</label></div><div class="col-md-6"><span>: </span>'+
						'<span class="badge" style="background: #FFF;border-color: #ACB5C3;border-style: solid;border-width: thin;padding-top: 1px;">'+data[key].totalMangersCount+' Managers</span><span>&nbsp;</span>'+
						'<span class="badge" style="background: #FFF;border-color: #ACB5C3;border-style: solid;border-width: thin;padding-top: 1px;">'+data[key].totalLeadsCount+' Leads</span><span>&nbsp;</span>'+
						'<span class="badge" style="background: #FFF;border-color: #ACB5C3;border-style: solid;border-width: thin;padding-top: 1px;">'+data[key].totalEngineersCount+' Engineers</span></div></div>';
					    
					    brick +='</div>';					    						
						brick +='</div></div></div>';		
			
	                   if(!($('#overallStatusReport').hasClass('tiles'))){
	                   		$('#overallStatusReport').addClass('tiles');
	                   		//$('#tileBrick1').append($(brick));
	                   }
						$('#overallStatusReport').append($(brick));
						//$('#tileBrick').append($(brick));
			            }
		            }else{
		            	$("#overallStatusReport").hide();
		            }
				
				
					},
		            complete: function(jsonData) {
					$("#loadingIconTM").hide();
	    	   		if(index<productIds.length-1){
	    	   			index++;
						if(loadProduct){
							populateTilesViewProduct(index);  	   			
						}
	    	   		}
	    	   		
		   		},
				error: function (data){
					$("#loadingIconTM").hide();
				}			   		
       		});
   }   
   
   function populateWorkFlowSummaryViewProduct(index){
	  $("#loadingIconTM").show();
	 
	   var urlToGetPopulateWorkFlowSummaryViewProduct = 'workflow.status.indicator.product.summary?engagementId=0&productId='+productIds[index]+'&entityTypeId=0';
	   productId = productIds[index];
	   $.ajax({
	        type: "POST",
	        contentType: "application/json; charset=utf-8",
	        url: urlToGetPopulateWorkFlowSummaryViewProduct,
	        dataType: 'json',			
	        success: function(jsonData) {	        	
	        	$("#loadingIconTM").hide();
	            var data = jsonData.Records; 
	            var workflowType="Status Workflow";
	            brick="";
				if(data.length>0 && typeof data[0].productName != 'undefined'){	            
					//<!-- BEGIN PAGE CONTENT INNER -->
					brick  ='<div id="brick_'+productIds[index]+'">'
					brick +='<div class="col-md-4 col-lg-4 col-sm-4 col-xs-12" style="padding: 10px;">'
            		//<!-- BEGIN SAMPLE TABLE PORTLET-->
            		brick +='<div class="portlet light"><div class="portlet-title">'
        			brick +='<div class="caption"><span class="caption-subject font-green-sharp bold uppercase">'+data[0].productName+'</span></div>'
    				/*brick +='<div class="tools"><a href="javascript:;" class="reload"></a></div></div>'*/
					brick +='<div class="portlet-body">'
					brick +='<div class="table-scrollable">'
					brick +='<table class="table table-hover"><thead>'
					brick +='<tr>'
					brick +='<th></th>'
					brick +='<th><i class="fa fa-circle" style="color: red;" title="SLA duration elapsed"></i></th>'
					brick +='<th><i class="fa fa-circle" style="color: orangered;" title="Needs immediate action"></th>'
					brick +='<th><i class="fa fa-circle" style="color: orange;" title="Needs immediate attention"></th>'
					brick +='<th><i class="fa fa-circle" style="color: green;" title="Availble for action"></th>'
					brick +='</tr></thead><tbody>'
		            for (var key in data) {						
						brick +='<tr>'		            	
						brick +='<td>'+data[key].entityType+'</td>'
						brick +='<td>'+'<a href="javascript:void(0)" onclick="listWorkflowSummarySLADetail('+productIds[index]+',\''+data[key].productName+'\','+data[key].entityTypeId+','+0+','+0+',\''+workflowType+'\', \'red\')">'+data[key].red+'</a>'+'</td>'
						brick +='<td>'+'<a href="javascript:void(0)" onclick="listWorkflowSummarySLADetail('+productIds[index]+',\''+data[key].productName+'\','+data[key].entityTypeId+','+0+','+0+',\''+workflowType+'\', \'orangered\')">'+data[key].orangered+'</a>'+'</td>'
						brick +='<td>'+'<a href="javascript:void(0)" onclick="listWorkflowSummarySLADetail('+productIds[index]+',\''+data[key].productName+'\','+data[key].entityTypeId+','+0+','+0+',\''+workflowType+'\', \'orange\')">'+data[key].orange+'</a>'+'</td>'
						brick +='<td>'+'<a href="javascript:void(0)" onclick="listWorkflowSummarySLADetail('+productIds[index]+',\''+data[key].productName+'\','+data[key].entityTypeId+','+0+','+0+',\''+workflowType+'\', \'green\')">'+data[key].green+'</a>'+'</td>'						
						brick +='</tr>'						
		            	//<!-- END SAMPLE TABLE PORTLET-->	
							
	            	}
					brick +='</tbody>'
					brick +='</table>'
					brick +='</div>'
					brick +='</div>'
					brick +='</div>'	
					brick +='</div>'
					brick +='</div>';	
	            	
	            	if(!($('#overallStatusReport').hasClass('tiles'))){
                   		$('#overallStatusReport').addClass('tiles');
                   }
					$('#overallStatusReport').append($(brick));

				}else{
	            	//$("#overallStatusReport").hide();
	            }
				//}				
			},
            complete: function(jsonData) {
			$("#loadingIconTM").hide();
			if(index<productIds.length-1){
	   			index++;
				if(loadProduct){
					populateWorkFlowSummaryViewProduct(index);  	   			
				}
	   		}  		
	    	   		
	   		},
			error: function (data){
				$("#loadingIconTM").hide();
			}			   		
   		});
   }
   
   function populateWorkPackageRAGViewSummaryProduct(index){
		  $("#loadingIconTM").show();
		 
		   var urlToGetWorkflowSummaryOfProduct = 'workpackageRAG.View.summary?productId='+productIds[index];
		   productId = productIds[index];
		   $.ajax({
		        type: "POST",
		        contentType: "application/json; charset=utf-8",
		        url: urlToGetWorkflowSummaryOfProduct,
		        dataType: 'json',			
		        success: function(jsonData) {	        	
		        	$("#loadingIconTM").hide();
		        	var workflowType="Life Cycle Workflow";
		            var recordData = jsonData.Records[0]; 
		            brick="";
					//if(data.length>0 && typeof data.productName != 'undefined'){
		            if(jsonData.Records.length>0){
						//<!-- BEGIN PAGE CONTENT INNER -->
		            	for (var keyItem in recordData) {
		            	data = recordData[keyItem];
		            	for (var key in data) {	
						brick  ='<div id="brick_'+productId+'">'
						brick +='<div class="col-md-4 col-lg-4 col-sm-4 col-xs-12" style="padding: 10px;">'
	            		//<!-- BEGIN SAMPLE TABLE PORTLET-->
	            		brick +='<div class="portlet light"><div class="portlet-title">'
	            				            			
	        			brick +='<div class="caption"><span class="caption-subject font-green-sharp bold uppercase">'+data[key].productName+' : '+data[key].lifeCycleEntityName+'  ['+data[key].lifeCycleEntityStatusName+']</span><br/><span class="caption-subject font-green-sharp bold uppercase">Workpackage LifeCycle : '+data[key].lifeCycleEntityWorkflowTemplate+'</span></div>'
	        			//brick +=
						brick +='<div class="portlet-body">'
						brick +='<div class="table-scrollable">'
						brick +='<table class="table table-hover"><thead>'
						brick +='<tr>'
						brick +='<th>Category</th>'
						brick +='<th>Stage</th>'
						brick +='<th><i class="fa fa-square" style="color: red;" title="SLA duration elapsed"></i></th>'
						brick +='<th><i class="fa fa-square" style="color: orangered;" title="Needs immediate action"></th>'
						brick +='<th><i class="fa fa-square" style="color: orange;" title="Needs immediate attention"></th>'
						brick +='<th><i class="fa fa-square" style="color: green;" title="Availble for action"></th>'
						brick +='</tr></thead><tbody>'
						var entityDetailsData = data[key].entityDetails; 	
						for(var entityKey in entityDetailsData){				
								brick +='<tr>'		            	
								brick +='<td>'+entityDetailsData[entityKey].workflowStatusCategoryName+'</td>'
								brick +='<td>'+entityDetailsData[entityKey].lifeCycleStatusName+'</td>'
								brick +='<td>'+'<a href="javascript:void(0)" onclick="listWorkflowSummarySLADetail('+productIds[index]+',\''+data[key].productName+'\','+entityDetailsData[entityKey].entityTypeId+','+data[key].lifeCycleEntityId+','+entityDetailsData[entityKey].lifeCycleStatusId+',\''+workflowType+'\', \'red\')">'+entityDetailsData[entityKey].redCount+'</a>'+'</td>'
								brick +='<td>'+'<a href="javascript:void(0)" onclick="listWorkflowSummarySLADetail('+productIds[index]+',\''+data[key].productName+'\','+entityDetailsData[entityKey].entityTypeId+','+data[key].lifeCycleEntityId+','+entityDetailsData[entityKey].lifeCycleStatusId+',\''+workflowType+'\', \'orangered\')">'+entityDetailsData[entityKey].orangeredCount+'</a>'+'</td>'
								brick +='<td>'+'<a href="javascript:void(0)" onclick="listWorkflowSummarySLADetail('+productIds[index]+',\''+data[key].productName+'\','+entityDetailsData[entityKey].entityTypeId+','+data[key].lifeCycleEntityId+','+entityDetailsData[entityKey].lifeCycleStatusId+',\''+workflowType+'\', \'orange\')">'+entityDetailsData[entityKey].orangeCount+'</a>'+'</td>'
								brick +='<td>'+'<a href="javascript:void(0)" onclick="listWorkflowSummarySLADetail('+productIds[index]+',\''+data[key].productName+'\','+entityDetailsData[entityKey].entityTypeId+','+data[key].lifeCycleEntityId+','+entityDetailsData[entityKey].lifeCycleStatusId+',\''+workflowType+'\', \'green\')">'+entityDetailsData[entityKey].greenCount+'</a>'+'</td>'						
								brick +='</tr>'						
				            	//<!-- END SAMPLE TABLE PORTLET-->						
						}
						brick +='</tbody>'
						brick +='</table>'
						brick +='</div>'
						brick +='</div>'
						brick +='</div>'	
						brick +='</div>'
						brick +='</div>';	
		            	
		            	if(!($('#overallStatusReport').hasClass('tiles'))){
	                   		$('#overallStatusReport').addClass('tiles');
	                   }
						$('#overallStatusReport').append($(brick));
	            	}
		            	}
					}else{
		            	//$("#overallStatusReport").hide();
		            }
					//}				
				},
	            complete: function(jsonData) {
				$("#loadingIconTM").hide();
				if(index<productIds.length-1){
		   			index++;
					if(loadProduct){
						populateWorkPackageRAGViewSummaryProduct(index);  	   			
					}
		   		}  		
		    	   		
		   		},
				error: function (data){
					$("#loadingIconTM").hide();
				}			   		
	   		});
	   }
   
   