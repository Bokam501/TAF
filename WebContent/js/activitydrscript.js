function availableDrList(){		
	var urlToGetAvailableDrList = "available.dr.List"; 	
	$('#listAllDr').jtable({
		title: 'Available Clarification',
        selecting: true,  //Enable selecting 
        paging: false, //Enable paging
        pageSize: 10, 
          actions: {
       	 	listAction: urlToGetAvailableDrList,
        },  
   fields: { 
				                   					 activityId: { 
				                   						type: 'hidden'
				                   						
				                   						},  
				                   						clarificationTrackerId: { 
				                       						key: true,
	                                                        list: false,
	                                                        create: false
				                       					},
				                       					clarificationTitle : {
				    										title : 'Title',		    										
															list : true,	    										
				    										width : "20%"
				    										
				    									},
				    									clarificationDescription:{
				    							             title: 'Description',
				    							             list:false,	
															 width:"20%"
				    							             },
				    										
				                       					priorityId : {
				    										title : 'Priority',				    										
				    										list : true,				    												    										
				    										width : "20%",	
				    										options : 'administration.executionPriorityList'
				    									},
				    								/*	statusId : {
				    										title : 'Status',			    										
				    										list : true,
				    									    width : "20%",	
				    									    options : 'status.category.option.list'
				    										
				    									},*/
				    									statusCategoryId : {
				    										title : 'Status',
				    										create:false,
				    										list : true,
				    										edit : true,
				    										defaultValue : 1,
				    										options:function(data){
				    										 	return 'status.category.option.list';
				    							     		}
				    									},
				    									ownerId : {
				    										title : 'Owner',				    										
				    										list : true,
				    										width : "20%",		 
				    										options : function(data) {
				    											return 'common.user.list';
				    										}
				    										
				    									},
				    									raisedDate : {
				    										title : 'Raised Date',				    										
				    										list : true,				    									
				    										width : "20%"
				    									},   
				    									raisedById : {
				    										title : 'Raised By',				    										
				    										list : true,				    										
				    										width : "20%",
				    										options : function(data) {
				    											return 'common.user.list';
				    										}
				    										
				    									},
				    								},  
		});
	$('#listAllDr').jtable('load');
	$('#popUpAvailableDr').modal();	
	$("#popUpAvailableDr h4").text("Clarifications");
}