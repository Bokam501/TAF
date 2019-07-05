function listTestRunPlanBytestRunId(url){
	try{
		if ($('#jTableContainerTestRunPlanById').length>0) {
			 $('#jTableContainerTestRunPlanById').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainerTestRunPlanById').jtable({
        
        title: 'Test Plan',
        selecting: true, //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
		 
		actions: {
            listAction: url
        },
        
        fields: {
        	runconfigId: {
                key: true,
                list: false,
                create: false
            },
            testRunPlanName: {
                title: 'Name',
                list:true
            },
            notifyByMail: {
                title: 'Mail',
                list:true
            },            
            description: {
                title: 'Description',
                list:true
            },
            testRunScheduledStartTime: {
                title: 'Start Date',
                list:true
            },
            testRunScheduledEndTime: {
                title: 'End Date',
                list:true
            },
        },

        //Validate form when it is being submitted
         formSubmitting: function (event, data) {
       	  data.form.find('input[name="deviceId"]').addClass('validate[required, custom[AlphaNumeric_loworup]]');
             data.form.find('input[name="hostId"]').addClass('validate[required]');
             data.form.validationEngine();
            return data.form.validationEngine('validate');
        }, 
         //Dispose validation logic when form is closed
         formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }  
		});
	 $('#jTableContainerTestRunPlanById').jtable('load');
 }	 

//TestSuite for TestRunPlan Starts
function testRunPlanTestSuiteList(url){
	try{
		if ($('#jTableContainerTestSuiteOfTestRunPlanTable').length>0) {
			 $('#jTableContainerTestSuiteOfTestRunPlanTable').jtable('destroy'); 
		}
		}catch(e){}
		$('#jTableContainerTestSuiteOfTestRunPlanTable').jtable({	         
	         title: 'TestSuite',
	         selecting: true, //Enable selecting 
	         paging: true, //Enable paging
	         pageSize: 10, //Set page size (default: 10)
	         editinline:{enable:false},
	         actions: {	       	
	        	listAction:url	            
	         },
	         fields: {	           
	        	 productId: { 
        			type: 'hidden',  
        			list:false,
        			defaultValue: productId
        		},         		
        		testSuiteId: { 
       				key: true,
       				type: 'hidden',
       				create: false, 
       				edit: false, 
       				list: false
	        	}, 
	        	testSuiteName: { 
	            	title: 'TestSuite Name',
	            	inputTitle: 'TestSuite Name <font color="#efd125" size="4px">*</font>',
	            	width: "10%",
	            	list:true
	        	},
	        	productVersionListId: { 
         			type: 'hidden',
         			title: 'Version ID',
         			list: true,
         			edit : false,         			 
         			defaultValue : productVersionId
         		},
         		productVersionListName:{
         			title :'Version',
         			list : false,
         			create : false,
         			edit : false,         			
         		},
	        	testScriptType: { 
	            	title: 'ScriptType' ,
	            	width: "10%",  
	            	list:true,
	            	create : true,
	            	edit : false,
	            	options : 'common.list.scripttype'
	            }, 
	            testScriptSource:{
	            	title: 'Script Source' ,
	            	width: "10%",  
	            	list:true,
	            	create : true,
	            	edit : false,            	
	            },
	            testSuiteScriptFileLocation:{
	            	title: 'Script File Location' ,
	            	width: "10%",  
	            	list:true,
	            	create : true,
	            	edit : false,            	
	            },
	            testSuiteCode: { 
	            	title: 'Suite Code',
	            	inputTitle: 'Suite Code <font color="#efd125" size="4px">*</font>',
	            	width: "10%",
	            	list:true
	        	},
	        	testCaseCount: { 
	            	title: 'TestCase Count',
	            	create : false,
	            	list : true,
	            	edit : false,
	            	inputTitle: 'TestCase Count <font color="#efd125" size="4px">*</font>',
	            	width: "7%"
	        	},		        
		        testStepsCount: { 
		            title: 'TestSteps Count',
		            inputTitle: 'TestSteps Count <font color="#efd125" size="4px">*</font>',
		            width: "10%",
		            create : false,
		            list:true,			            
		            edit : false
		        },
		         executionTypeId:{
	        		title : 'Execution Type',
	        		width : "15%",
	        		create : true,
	        		list : true,
	        		edit : true,
	      			options:function(data){
	      				if(data.source =='list'){	      				
	      					return 'common.list.executiontypemaster.byentityid?entitymasterid=7';	
	      				}else if(data.source == 'create'){	      				
	      					return 'common.list.executiontypemaster.byentityid?entitymasterid=7';
	      				}
	      			}
	        	},  
	       },			 
           //Moved Formcreate validation to Form Submitting
           //Validate form when it is being submitted
            formSubmitting: function (event, data) {        
           	 data.form.find('input[name="environmentName"]').addClass('validate[required, custom[Letters_loworup_noSpec]]');     
           	 data.form.find('input[name="environmentGroupId"]').addClass('validate[required]');   
           	 data.form.find('input[name="environmentCategoryId"]').addClass('validate[required]');   
             data.form.validationEngine();
             return data.form.validationEngine('validate');
           }, 
            //Dispose validation logic when form is closed
            formClosed: function (event, data) {
               data.form.validationEngine('hide');
               data.form.validationEngine('detach');
           }  
	   });
		 
	   $('#jTableContainerTestSuiteOfTestRunPlanTable').jtable('load');
		 
		var jscrolheight = $("#jTableContainerTestSuiteOfTestRunPlanTable").height();
		var jscrolwidth = $("#jTableContainerTestSuiteOfTestRunPlanTable").width();
		  
		$(".jScrollContainer").on("scroll", function() {
			 var lastScrollLeft=0;	
			 var documentScrollLeft = $(".jScrollContainer").scrollLeft();   
			 if (lastScrollLeft < documentScrollLeft) {
			    	$("#jTableContainerTestSuiteOfTestRunPlanTable").width($(".jtable").width()).height($(".jtable").height());			
			        lastScrollLeft = documentScrollLeft;
			  }else if(lastScrollLeft >= documentScrollLeft){			
			    	$("#jTableContainerTestSuiteOfTestRunPlanTable").width(jscrolwidth).height(jscrolheight);
			  }			
		});
}
//TestSuite for TestRunPlan ends

//Device for TestRunPlan Starts
function testRunPlanDeviceList(url){
	try{
		if ($('#jTableContainerDeviceOfTestRunPlanTable').length>0) {
			 $('#jTableContainerDeviceOfTestRunPlanTable').jtable('destroy'); 
		}
	} catch(e) {}	
	$('#jTableContainerDeviceOfTestRunPlanTable').jtable({        
        title: 'Target Device',
        selecting: true,  //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)
        actions: {
            listAction: url,
        },
        fields: {
					runconfigId: {
				    key: true,
	                list: false,
	                create: false
	            },	            
	            runConfigurationName: {
	                title: 'Test Configuration',
	                inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
	                list:false
	            }, 
	         environmentcombinationName: {
	                title: 'Environment Combination',
	                inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
	            }, 
	         deviceName: {
	                title: 'Device Name',
	                inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
	            },
	            hostName:{
	            	  title: 'Host Name',
		                inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
	            }
	         },
		                	
        //Validate form when it is being submitted
         formSubmitting: function (event, data) {
       	  data.form.find('input[name="deviceId"]').addClass('validate[required, custom[AlphaNumeric_loworup]]');
             data.form.find('input[name="hostId"]').addClass('validate[required]');
             data.form.validationEngine();
            return data.form.validationEngine('validate');
        }, 
         //Dispose validation logic when form is closed
         formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }  
		});
	 $('#jTableContainerDeviceOfTestRunPlanTable').jtable('load');
 }
 
//Device for TestRunPlan Ends

function listTestRunSelectedProduct(url){	
	try{
		if ($('#jTableContainertestrun').length>0) {
			 $('#jTableContainertestrun').jtable('destroy'); 
		}
	} catch(e) {}
	$('#jTableContainertestrun').jtable({
        
        title: 'Add/Edit Test Run',
        selecting: true,  //Enable selecting 
        paging: true, //Enable paging
        pageSize: 10, //Set page size (default: 10)        
		toolbar : {
			items : [{
					text : "Show",
					click : function() {						
						showTestRunplanFormNew();
					}
				}]
		},
		actions: {
            listAction: url
        },
        fields: {
        	runconfigId: {
                key: true,
                list: false,
                create: false
            },
            runConfigurationName: {
                title: 'Test Configuration',
                inputTitle: 'Test Configuration <font color="#efd125" size="4px">*</font>',
            },
         
        },

        //Validate form when it is being submitted
         formSubmitting: function (event, data) {
       	  data.form.find('input[name="deviceId"]').addClass('validate[required, custom[AlphaNumeric_loworup]]');
             data.form.find('input[name="hostId"]').addClass('validate[required]');
             data.form.validationEngine();
            return data.form.validationEngine('validate');
        }, 
         //Dispose validation logic when form is closed
         formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }  
		});
	 $('#jTableContainertestrun').jtable('load');	
}

function showTestRunplanFormNew() {
	reInitializeFormWizard();
	
	var jsonObj={};	
	jsonObj.mode ="edit",
	jsonObj.Title = "TestRunPlan";
	jsonObj.productId=productId;
	jsonObj.productVersionListId=productVersionListId;		
	TestRunPlan.init(jsonObj);	
}
