var EngGageMentActivityWorkPackageViewDetails = function(){  
	var initialise = function(jsonObj){	    
	   listEngageMentAWPDetailsComponent(jsonObj);
	};
	return {
		//main function to initiate the module
        init: function(jsonObj) {        	
       	initialise(jsonObj);
        }		
	};	
	}();
	
	var engageMentActivityWPJsonObj='';
	function listEngageMentAWPDetailsComponent(jsonObj){
		engageMentActivityWPJsonObj=jsonObj;	
		
		//productActivityWPselectedTab=$("#tablistProductACTWP>li.active").index();
		//productActivityWPTabSelection(productActivityWPselectedTab);
		
		engageMentActivityWPVeiwComponent();
	}
	
	function engageMentActivityWPVeiwComponent(){
		$("#productActWorkPackageDiv #prodName").text('hello');
		$("#productActWorkPackageDiv #productVersionName").text('-hello-');
		$("#productActWorkPackageDiv #prodBuildName").text('-hello-');
		
		$.ajax({
			type: "POST",
			url : engageMentActivityWPJsonObj.EnageMentActivityWPURL,
			 contentType: "application/json; charset=utf-8",
			dataType : 'json',
			success : function(data) {
				var result=data.Record;			
				
				$("#engagementActWorkPackageDiv #engagementTestFactoryId").text('--');				
				$("#engagementActWorkPackageDiv #engagementCreatedOnPS").text('--');	
				$("#engagementActWorkPackageDiv #engagementModifiedOnPS").text('--');	
				$("#engagementActWorkPackageDiv #engagementDescPS").text('--');	
					
					if(result.testFactoryId  != null){
						$("#engagementActWorkPackageDiv #engagementTestFactoryId").text(result.testFactoryId);		
					}
					
					if(result.testFactoryName != null){
						
						$("#engagementActWorkPackageDiv #engagementNamePS").text(result.testFactoryName);							
					}		
					
					if(result.description != null){
						
						$("#engagementActWorkPackageDiv #engagementDescPS").text(result.description);							
					}	
					if(result.country != null){						
						$("#engagementActWorkPackageDiv #engagementCountryPS").text(result.country);							
					}
					if(result.state != null){						
						$("#engagementActWorkPackageDiv #engagementStatePS").text(result.state);							
					}					
					
					if(result.city != null){						
						$("#engagementActWorkPackageDiv #engagementCity").text(result.city);							
					}
					if(result.status != null){						
						$("#engagementActWorkPackageDiv #engagementStatusPS").text(result.status);							
					}
					
					
					if(result.createdDate  != null){
						$("#engagementActWorkPackageDiv #engagementCreatedOnPS").text(result.createdDate);		
					}
					
					if(result.modifiedDate  != null){
						$("#engagementActWorkPackageDiv #engagementModifiedOnPS").text(result.modifiedDate);		
					}
					
			}
		});	
	}
		
	var productVersionId=0;
	var productBuildId=0;
	var entityTypeId=33;
	var entityTypeName="Activities";
	
	function showEngageMentDashBoard(){
		//DashBoard
		$('#engagementDashboardRAGHeaderSubTitle').show();
		$('#engagementDashboardWorkflowHeaderSubTitle').hide();
		var engageMentRAGDashBoardDiv='engagementRAGSummary';
		var engageMentRAGSummaryHeaderTitle='engagementDashboardRAGHeaderSubTitle';
		activityWPId=0;
		showDashBoardEntityInstanceRAGSummary(engageMentRAGDashBoardDiv,engageMentRAGSummaryHeaderTitle,testFactId,prodId,productVersionId,productBuildId,activityWPId,productName, entityTypeId, "", 0, activityWPId);
	}	
	
	function showEngamentGroupDashboardSummaryTableTool(tabVisible){
		if(tabVisible == 1){
			$('#engagementWorkflowSummary').hide();
			$('#engagementDashboardWorkflowHeaderSubTitle').hide();
			$('#engagementDashboardRAGHeaderSubTitle').show();
			$('#engagementRAGSummary').show();
		} else {
			$('#engagementRAGSummary').hide();
			$('#engagementWorkflowSummary').show();
			$('#engagementDashboardWorkflowHeaderSubTitle').show();
			$('#engagementDashboardRAGHeaderSubTitle').hide();
			var engageMentWorkflowDashBoardDiv='engagementWorkflowSummary';
			var engageMentWorkflowSummaryHeaderTitle='engagementDashboardWorkflowHeaderSubTitle';
			activityWPId=0;
			showDashBoardWorkflowEntityInstanceStatusSummary(engageMentWorkflowDashBoardDiv,engageMentWorkflowSummaryHeaderTitle,testFactId,prodId,productVersionId,productBuildId,activityWPId,productName, entityTypeId, entityTypeName, 0,activityWPId);
		}
	}