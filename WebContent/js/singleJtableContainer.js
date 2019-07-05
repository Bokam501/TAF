var SingleJtableContainer = function() {
  
   var initialise = function(jsonObj){
	   singleJTableContainerSummary(jsonObj);
   };
		return {
        //main function to initiate the module
        init: function(jsonObj) {        	
        	initialise(jsonObj);
        }		
	};	
}();

function singleJTableContainerSummary(jsonObj){	
	$("#div_SingleJTableSummary").find("h4").text(jsonObj.Title);
	
	if(jsonObj.componentUsageTitle == "DefectSummary"){
		listDefectSummaryTable(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "BDDkeywords"){		
		bddkeywordsphraseslist(jsonObj);		
	}
	else if(jsonObj.componentUsageTitle == "ObjectRepository"){		
		objectRepositoryFileContent(jsonObj);		
	}
	else if(jsonObj.componentUsageTitle == "TestData"){		
		testDataFileContent(jsonObj);		
	}
	else if(jsonObj.componentUsageTitle == "productAudit"){		
		auditFileConent(jsonObj, true);
	}
	else if(jsonObj.componentUsageTitle == "productVersionAudit"){		
		auditFileConent(jsonObj, true);
	}
	else if(jsonObj.componentUsageTitle == "productBuildAudit"){		
		auditFileConent(jsonObj, true);
	}
	else if(jsonObj.componentUsageTitle == "productFeatureAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "environmentAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "testRunPlanAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "attachmentAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "workPackageAudit"){		
		workPackageAuditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "activityWorkPackageAudit"){		
		auditFileConent(jsonObj, true);
	}
	else if(jsonObj.componentUsageTitle == "activityAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "activityTaskAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "changeRequestAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "clarificationTrackerAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "testCaseAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "testCaseStepsAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "testSuiteAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "regularUserAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "testFactoryAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "vendorAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "customerAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "environmentGroupAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "environmentCategoryAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "riskAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "riskMitigationAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "riskSeverityAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "riskLikehoodAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "riskRatingAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "riskRatingAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "workFlowAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "workFlowStatusAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "workFlowEventsAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "visualizationURLAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "dashboardTabURLAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "dashboardTabRoleBasedAudit"){		
		auditFileConent(jsonObj);
	}
	else if(jsonObj.componentUsageTitle == "sourceEntityAudit"){		
		sourceEntityConent(jsonObj);
	}
	else{
		console.log("add custom Jtable");
	}
				
	$("#div_SingleJTableSummary").modal();	
}

function clearSingleJTableDatas(){
	try{
		if ($("#JtableSingleContainer").length>0) {
			 $('#JtableSingleContainer').jtable('destroy');
		}
	} catch(e) {}
}

function listDefectSummaryTable(jsonObj){	
	clearSingleJTableDatas();
    $('#JtableSingleContainer').jtable({
		title: jsonObj.singleJTableTitle,
        selecting: true,  //Enable selecting 
        editinline : {enable : true},
        actions: {
       	 	listAction: jsonObj.url
        },  
        fields : {
             productVersionName: {
                 title: 'Product Version',
                 create:false,
                 edit:false,
                 list:true,
                 width: "8%"
 			 },
 			openDefects: {
                key: true,
                title: 'Open',
                create:false,
                edit:false,
                list:true,
                width: "8%"
             },
             reviewedDefects: {
                 title: 'Reviewed',
                 create:false,
                 edit:false,
                 list:true,
                 width: "8%"
 			},
 			referBackDefects: {
                 title: 'Refer Back',
                 create:false,
                 edit:false,
                 list:false
 			},
 			approvedDefects: {
                 title: 'Approved',
                 create:false,
                 edit:false,
                 list:true,
                 width: "8%"
 			},
 			closedDefects: {
                 title: 'Closed',
                 create:false,
                 edit:false,
                 list:true,
                 width: "8%"
 			},
        	}
		});
	$('#JtableSingleContainer').jtable('load'); 
}

//Change Requet Genric Method
/*function listChangeRequestAuditHistory(changeRequestId){
	clearSingleJTableDatas();
	var url='administration.event.list?sourceEntityType=ChangeRequest&sourceEntityId='+changeRequestId;
	var jsonObj={"Title":"Change Request Audit History:",
			"url": url,	
			"jtStartIndex":0,
			"jtPageSize":10,
			"componentUsageTitle":"changeRequestAudit",
	};
	SingleJtableContainer.init(jsonObj);
}*/
function auditFileConent(jsonObj){		
		$('#JtableSingleContainer').jtable({
	         title: jsonObj.Title,
	        // selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: jsonObj.jtPageSize, //Set page size (default: 10)
	         editinline:{enable:false},	        
	         actions: {
	           listAction:jsonObj.url,
	         },
	         fields: {        	
	        	eventId: { 
	            	title: 'EventId' ,	            	
	            	list:true,
	            	edit:false
	            },
//	            eventSourceComponent: { 
//	            	title: 'Source Component' ,
//	            	list:true,
//	            	edit:false
//	            },
	            eventName: { 
	            	title: 'Event Name',
	            	list:true,
	            	edit:false
	        	},
	            eventDescription: { 
	            	title: 'Event Description',
	            	list:true,
	            	edit:false
	        	},
	        	sourceEntityType: { 
	            	title: 'EntityType',
	                list:true,
	            	edit:false
	            },
	        	sourceEntityName: { 
	            	title: 'Remarks',
	                list:true,
	            	edit:false
	            },
	            userName: { 
	            	title: 'User',
	                list:true,
	            	edit:false
	            },/*
	            startTime: { 
	            	title: 'Start Time',
	                list:true,
	            	edit:false
	            },*/
	            endTime: { 
	            	title: 'Time',
	                list:true,
	            	edit:false
	            },		
	       },			 
	        formSubmitting: function (event, data) {    
			data.form.validationEngine();
	      	return data.form.validationEngine('validate');
	       }, 
	        //Dispose validation logic when form is closed
	        formClosed: function (event, data) {
	           data.form.validationEngine('hide');
	           data.form.validationEngine('detach');
	       },
	    });
		 
	   $('#JtableSingleContainer').jtable('load');	
}

function auditFileConent(jsonObj, dispModified){
	if(dispModified == true){
		$('#JtableSingleContainer').jtable({
	         title: jsonObj.Title,
	        // selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: jsonObj.jtPageSize, //Set page size (default: 10)
	         editinline:{enable:false},	        
	         actions: {
	           listAction:jsonObj.url,
	         },
	         fields: {        	
	        	eventId: { 
	            	title: 'EventId' ,	            	
	            	list:false,
	            	edit:false
	            },
//	            eventSourceComponent: { 
//	            	title: 'Source Component' ,
//	            	list:true,
//	            	edit:false
//	            },
	            eventName: { 
	            	title: 'Event',
	            	list:true,
	            	edit:false
	        	},
	            eventDescription: { 
	            	title: 'Description',
	            	list:true,
	            	edit:false,
	            	width:"15%",
	        	},
	        	sourceEntityType: { 
	            	title: 'EntityType',
	                list:false,
	            	edit:false
	            },
	        	sourceEntityName: { 
	            	title: 'Remarks',
	                list:true,
	            	edit:false
	            },
	            userName: { 
	            	title: 'User',
	                list:true,
	            	edit:false
	            },
	            fieldName: { 
	            	title: 'ModifiedField',
	                list:false,
	            	edit:false
	            },
	            fieldValue: { 
	            	title: 'Old Value',
	                list:false,
	            	edit:false
	            },
	            newFieldValue: { 
	            	title: 'New Value',
	                list:false,
	            	edit:false
	            },/*
	            startTime: { 
	            	title: 'Start Time',
	                list:true,
	            	edit:false
	            },*/
	            endTime: { 
	            	title: 'Time',
	                list:true,
	            	edit:false
	            },		
	       },			 
	        formSubmitting: function (event, data) {    
			data.form.validationEngine();
	      	return data.form.validationEngine('validate');
	       }, 
	        //Dispose validation logic when form is closed
	        formClosed: function (event, data) {
	           data.form.validationEngine('hide');
	           data.form.validationEngine('detach');
	       },
	    });
	}else{
		$('#JtableSingleContainer').jtable({
	         title: jsonObj.Title,
	        // selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: jsonObj.jtPageSize, //Set page size (default: 10)
	         editinline:{enable:false},	        
	         actions: {
	           listAction:jsonObj.url,
	         },
	         fields: {        	
	        	eventId: { 
	            	title: 'EventId' ,	            	
	            	list:false,
	            	edit:false
	            },
//	            eventSourceComponent: { 
//	            	title: 'Source Component' ,
//	            	list:true,
//	            	edit:false
//	            },
	            eventName: { 
	            	title: 'Event',
	            	list:true,
	            	edit:false
	        	},
	            eventDescription: { 
	            	title: 'Description',
	            	list:true,
	            	edit:false
	        	},
	        	sourceEntityType: { 
	            	title: 'EntityType',
	                list:false,
	            	edit:false
	            },
	        	sourceEntityName: { 
	            	title: 'Remarks',
	                list:true,
	            	edit:false
	            },
	            userName: { 
	            	title: 'User',
	                list:true,
	            	edit:false
	            },/*
	            startTime: { 
	            	title: 'Start Time',
	                list:true,
	            	edit:false
	            },*/
	            endTime: { 
	            	title: 'Time',
	                list:true,
	            	edit:false
	            },		
	       },			 
	        formSubmitting: function (event, data) {    
			data.form.validationEngine();
	      	return data.form.validationEngine('validate');
	       }, 
	        //Dispose validation logic when form is closed
	        formClosed: function (event, data) {
	           data.form.validationEngine('hide');
	           data.form.validationEngine('detach');
	       },
	    });
	}
		
		 
	   $('#JtableSingleContainer').jtable('load');	
}

function sourceEntityConent(jsonObj){		
	$('#JtableSingleContainer').jtable({
         title: jsonObj.Title,
        // selecting: true, //Enable selecting 
         paging: true, //Enable paging
         pageSize: jsonObj.jtPageSize, //Set page size (default: 10)
         editinline:{enable:false},	        
         actions: {
           listAction:jsonObj.url,
         },
         fields: {  
        	 entityTypeName2: { 
            	title: 'Source EntityType' ,	            	
            	list:true,
            	edit:false
            },
            entityInstanceId2: { 
            	title: 'Source Entity Id',
            	list:true,
            	edit:false
        	},
        	entityInstanceName2: { 
            	title: 'Source Entity Name',
            	list:true,
            	edit:false
        	},  
        	entityStatus: { 
            	title: 'Entity Status',
            	list:true,
            	edit:false
        	},
       },			 
        formSubmitting: function (event, data) {    
		data.form.validationEngine();
      	return data.form.validationEngine('validate');
       }, 
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
           data.form.validationEngine('hide');
           data.form.validationEngine('detach');
       },
    });
	 
   $('#JtableSingleContainer').jtable('load');	
}
function bddkeywordsphraseslist(jsonObj){
	clearSingleJTableDatas();
	$('#JtableSingleContainer').jtable({
         title: 'BDD Keywords Phrases',
         toolbarsearch:true,
       //  selecting: true, //Enable selecting 
         paging: true, //Enable paging
         pageSize: 10, //Set page size (default: 10)
         editinline:{enable:false},
        // search:true,
        
        // sorting: true, //Enable sortin
        // multiselect : true, //Allow multiple selecting
 		//selectingCheckboxes : true, //Show checkboxes on first column
         /* saveUserPreferences: false, */
         actions: {
             listAction: 'bddkeywordsphrases.list?productType=&testTool='+testToolName,
            // createAction: 'administration.vendor.add',
        //     editinlineAction: 'bddkeywordsphrases.update'

            // deleteAction: 'administration.product.environment.delete'
         },
         fields: {
        	 
        	 id: { 
        		 title: 'Keyword Phrase' ,
        		 		key: true,
        		 		
        				type: 'hidden',
        				create: false, 
        				edit: false, 
        				list: false
        	}, 
        	
        	keywordPhrase: { 
            	title: 'Keyword Phrase' ,
            	
            	list:true,
            	edit:false,
            	width : "25%",
            	
        /*	display:function (data) {
				return $('<input   style="width:200px" title="'+data.record.description+'" value="' + data.record.keywordPhrase + '" />');
			}*/
            },
            description: { 
            	title: 'Description' ,
            	//inputTitle: 'Description <font color="#efd125" size="4px">*</font>',
            	list:true,
            	edit:true,
            	width : "25%",
            },
            tags: { 
            	title: 'Tags' ,
            	//inputTitle: 'Description <font color="#efd125" size="4px">*</font>',
            	list:true,
            	edit:false,
            	width : "20%",
            },
          /*  objects: { 
            	title: 'Objects',
            	   	list:true,
            	edit:true
        	},
        	parameters: { 
            	title: 'Parameters',
                  	list:true,
            	edit:true
            },*/
           /* isCommon: { 
            	title: 'Common',
            	list:true,
            	edit:true,
            	type : 'checkbox',

	    		values: {'0' : 'No','1' : 'Yes'},
	    		defaultValue: '1'
            },	*/        
            /*isMobile: { 
            	title: 'Mobile' ,
            	
            	list:true,
            	edit:true,
            	create:false,
            	type : 'checkbox',

	    		values: {'0' : 'No','1' : 'Yes'},
	    		defaultValue: '1'
	    		},
	    		isWeb: { 
	            	title: 'Web' ,
	            	list:true,
	            	edit:true,
	            	create:false,
	            	type : 'checkbox',
		    		values: {'0' : 'No','1' : 'Yes'},
		    		defaultValue: '1'
	    		},
	    		isDesktop: { 
	            	title: 'Desktop' ,
	            	            	list:true,
	            	edit:true,
	            	create:false,
	            	type : 'checkbox',
		    		values: {'0' : 'No','1' : 'Yes'},
		    		defaultValue: '1'
	    		},*/
	    		/*isSoftwareCommon: { 
	            	title: 'SoftwareCommon' ,
	            			            	list:true,
	            	edit:true,
	            	create:false,
	            	type : 'checkbox',
		    		values: {'0' : 'No','1' : 'Yes'},
		    		defaultValue: '1'
	    		},*/
	    		/*isEmbedded: { 
	            	title: 'Embedded' ,
	                        	list:true,
	            	edit:true,
	            	create:false,
	            	type : 'checkbox',
		    		values: {'0' : 'No','1' : 'Yes'},
		    		defaultValue: '1'
	    		},*/
            isSeleniumScripGeneration: { 
            	title: 'Selenium(Web)' ,
                    	list:true,
            	edit:true,
            	create:false,
            /*	type : 'checkbox',
	    		values: {'0' : 'No','1' : 'Yes'},
	    		defaultValue: '1',*/
	    		width : "10%",
    		},
	    		
    		isAppiumScripGeneration: { 
	            	title: 'Appium(Mobile)' ,
	                     	list:true,
	            	edit:true,
	            	create:false,
	            	/*type : 'checkbox',
		    		values: {'0' : 'No','1' : 'Yes'},
		    		defaultValue: '1',*/
		    		width : "10%",
	    		},
	    		/*isSeetestScripGeneration: { 
	            	title: 'SeeTest(Mobile)' ,
	            			            	list:true,
	            	edit:true,
	            	create:false,
	            	type : 'checkbox',
		    		values: {'0' : 'No','1' : 'Yes'},
		    		defaultValue: '1',
		    		width : "10%",
	    		},*/
	    		/*isRobotium: { 
	            	title: 'Robotium(Mobile)' ,
	                    	list:true,
	            	edit:true,
	            	create:false,
	            	type : 'checkbox',
		    		values: {'0' : 'No','1' : 'Yes'},
		    		defaultValue: '1'
	    		},*/
	    		
	    		/*isAutoIt: { 
	            	title: 'AutoIt(Desktop)' ,
	            	      	list:true,
	            	edit:true,
	            	create:false,
	            	type : 'checkbox',
		    		values: {'0' : 'No','1' : 'Yes'},
		    		defaultValue: '1'
	    		},*/
	    		
       },			 
        /*formSubmitting: function (event, data) {        
       	
		data.form.validationEngine();
      	return data.form.validationEngine('validate');
       }, 
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
           data.form.validationEngine('hide');
           data.form.validationEngine('detach');
       },*/
       
       recordsLoaded: function(event, data) {
			/*$('#jtable-toolbarsearch-description').prop("type", "hidden");  */				
			/*$('#jtable-toolbarsearch-isSeeTest').prop("type", "hidden");  */
/*			$('#jtable-toolbarsearch-tags').prop("type", "hidden"); */
			$('#jtable-toolbarsearch-isAppiumScripGeneration').prop("type", "hidden"); 			
     	/*	$('#jtable-toolbarsearch-isRobotium').prop("type", "hidden");*/
			$('#jtable-toolbarsearch-isSeleniumScripGeneration').prop("type", "hidden");
			/*$('#jtable-toolbarsearch-isAutoIt').prop("type", "hidden"); */ 				
		
			
	 },
	 
	 
	 });  
	$('#JtableSingleContainer').keyup(function(e){
        
   	   	$('#JtableSingleContainer').jtable(
   	   			'load',
   	   			{
   	   			searchKeywordsPhrase: $('#JtableSingleContainer #jtable-toolbarsearch-keywordPhrase').val(),
   	   		searchKeyDescription: $('#JtableSingleContainer #jtable-toolbarsearch-description').val(),
      	   	searchTags: $('#JtableSingleContainer #jtable-toolbarsearch-tags').val(),
      	  searchIsSelenium: '',
      	  searchIsAppium: '',
      	/* searchIsSeeTest:'',*/	
   	   				
   	   			});
   	   });
	   
	   	$('#JtableSingleContainer #jtable-toolbarsearch-keywordPhrase #jtable-toolbarsearch-description #jtable-toolbarsearch-tags').bind('change', function(e){
                
   	   	$('#JtableSingleContainer').jtable(
   	   			'load',
   	   			{
   	   			searchKeywordsPhrase: $('#JtableSingleContainer #jtable-toolbarsearch-keywordPhrase').val(),
   	   		searchKeyDescription: $('#JtableSingleContainer #jtable-toolbarsearch-description').val(),
      	   	searchTags: $('#JtableSingleContainer #jtable-toolbarsearch-tags').val(),
      	  searchIsSelenium: '',
      	  searchIsAppium: '',
      	 /*searchIsSeeTest:'',*/
	   			  			});
	   	   });
       
   
	 
   $('#JtableSingleContainer').jtable('load');	  
}