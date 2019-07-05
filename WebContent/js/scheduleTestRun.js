

	function closePopupscheduleTestRun() {	
		$("#scheduleTestRunContainer").fadeOut("normal");	
	}
	
	
	function initialiseTestRunItems(title,url){
		
		$('#scheduleTestRunContainer').show();
		$("#scheduleTestRunHead").html(title);
	
		try{
			if ($('#jtablescheduleTestRunContainer').length>0) {
				 $('#jtablescheduleTestRunContainer').jtable('destroy'); 
			}
		} catch(e) {}
		$('#jtablescheduleTestRunContainer').jtable({
	        
	        title: 'Test Plan',
	        selecting: true, //Enable selecting 
	        paging: true, //Enable paging
	        pageSize: 10, //Set page size (default: 10)
	        //sorting: true, //Enable sortin
	        /*  saveUserPreferences: false, */ 
	        
			 toolbar : {
				items : [{
						text : "Add New Test Plan",
						click : function() {
							//showTestRunplanForm("true");
							filterVal='add';
							showTestRunplanFormNew();
						}
					}]
			}, 
			recordsLoaded: function(event, data) {
				$("#hdnTrpLength").val(data.records.length);
				
			},
			actions: {
	            listAction: url
	            //createAction: funcall//'administration.device.add'
	           // updateAction: 'administration.device.update',
	            //deleteAction: 'administration.device.delete'
	        },
	        fields: {
	        	testRunPlanId: {
	                key: true,
	                title: 'Test Plan Id',
	                list: true,
	                create: false
	            },
	            testRunPlanName: {
	                title: 'Name',
	                list:true,
	                display: function (data) { 
	     				return $('<a style="color: #0000FF;" href=javascript:callConfirm('+data.record.testRunPlanId+','+data.record.executionTypeId+');>'+data.record.testRunPlanName+'</a>');
	               }
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
	            notifyByMail: {
	                title: 'Mail',
	                list:true
	            },
	            executionTypeName: {
	                title: 'Execution Type',
	                list:true
	            },
	            executeImg:{
	       		 display: function (data) {    	            	 
	   	            	var $exe = $('<img src="css/images/execute_metro.png" title="Execute Test Plan" class="exe"/>');
	   	            	$exe.click(function () {
	   	            		var ans=confirm("Do you want to execute the Test Run now?");
	   	            		if(ans){
	   	            			var productName= document.getElementById("treeHdnCurrentProductName").value ;
								var productVersionName=title ;
							    var timestamp = date.getFullYear()+"-"+date.getMonth()+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	   	            			var workpackageName=productName+'-'+productVersionName+'-'+data.record.testRunPlanName+'-'+timestamp;
	   	            			var description=workpackageName + " is created";

	   	            			document.getElementById("hdnTestRunPlanId").value=data.record.testRunPlanId;
	   	            			document.getElementById("wpkg_name").value=workpackageName;
	   	            			document.getElementById("wpkg_desc").value=description;
	   	            			
	   		            		//execution code comes here
	   		            		saveWorkpackageDetail(-1);
	   		            		//$.post('test.run.parent.execute',{testRunConfigurationParentId:data.record.testRunConfigurationParentId});
	   		            	}
	   	            	  });
	   	            	return $exe;
	       			  },
	       			  create:false,
	       			  edit:false,
	       			  
	       		}, 
	       	
	            workPackage:{
	             	title: '',
	             	width: "5%",
	             	edit: true,
	             	create: false,
	        	 	display: function (testRunPlan) { 
	        		//Create an image that will be used to open child table 
	        			var $img = $('<img src="css/images/list_metro.png" title=" WorkPackage" />'); 
	        			//Open child table when user clicks the image 
	        			$img.click(function () { 
	        				
	        				// ----- Closing child table on the same icon click -----
	        				closeChildTableFlag = closeJtableTableChildContainer($(this), $("#jTableContainertestrun"));
	        				if(closeChildTableFlag){
	        					return;
	        				}
	        				
	        				$('#jTableContainertestrun').jtable('openChildTable', 
	        				$img.closest('tr'), 
	        					{ 
	        					title: 'WorkPackage', 
	        					// editinline:{enable:true},
	        					 recordsLoaded: function(event, data) {
	        			        	 $(".jtable-edit-command-button").prop("disabled", true);
	        			         },
	        					actions: { 
	        						listAction: 'administration.workPackage.list.bytestRunPlanId?testRunPlanId='+ testRunPlan.record.testRunPlanId , 
	        						//deleteAction: 'administration.product.build.delete', 
	        					//	editinlineAction: 'administration.product.build.update',  
	        						//createAction: 'administration.product.build.add'
	        							}, 
	        	 				fields: { 
	        	 					testRunPlanId: { 
	        							type: 'hidden', 
	        							defaultValue: testRunPlan.record.testRunPlanId 
	        						},  
	        						id: { 
	            						key: true, 
	            						create: false, 
					                	edit: false, 
	            						list: false
	            					},
	            					name: { 
	            						title: 'Name',
	            						inputTitle: 'Name <font color="#efd125" size="4px">*</font>',
	            						edit: false
	        									},
	        					description: { 
	            						title: 'Description' ,
	            						inputTitle: 'Description <font color="#efd125" size="4px">*</font>',
	            						edit: false
	            								},  
	            				plannedStartDate: { 
	                        				title: 'Planned Start Date' , 
	                        				inputTitle: 'Start Date <font color="#efd125" size="4px">*</font>',
	                						list: true,
	                        				type: 'date',
	                        				edit: false
	                        				
	                        		},
	                        	plannedEndDate: { 
	                    				title: 'Planned End Date' , 
	                    				inputTitle: 'End Date <font color="#efd125" size="4px">*</font>', 
	            						list: true,
	                    				type: 'date',
	                    				edit: false
	                    				
	                    		},
	        				},
	        	
	              formSubmitting: function (event, data) {
	            	 /*  data.form.find('input[name="productBuildNo"]').addClass('validate[required]');
	            	  data.form.find('input[name="productBuildName"]').addClass('validate[required]');                                           
	                  data.form.find('input[name="productBuildDate"]').addClass('validate[required, custom[dateFormat]]'); */
	                  data.form.validationEngine();
	                 return data.form.validationEngine('validate');
	             }, 
	              //Dispose validation logic when form is closed
	              formClosed: function (event, data) {
	                 data.form.validationEngine('hide');
	                 data.form.validationEngine('detach');
	             }
	        	}, function (data) { //opened handler 
	        	data.childTable.jtable('load'); 
	        	}); 
	        	}); 
	        	//Return image 
	        	return $img; 
	        	} 
	         },
	        clock:{
	          	title: '',
	          	width: "5%",
	          	edit: true,
	          	create: false,
	     	 	display: function (data) { 
	     		//Create an image that will be used to open child table 
	     			var $img = $('<img src="css/images/clock.png" title=" schedules" />'); 
	     			$img.click(function () { 
	     				initialiseTestRunItems('XYZ'); 				
	     			});
	     			return $img;
	     			
	     	 	}
	         },
	         
	        },
	      //Initialize validation logic when a form is created
	       /*  formCreated: function (event, data) {        	 
	           data.form.find('input[name="deviceId"]').addClass('validate[required, custom[AlphaNumeric_loworup]]');
	           data.form.find('input[name="hostId"]').addClass('validate[required]'); */
	          /*  data.form.find('input[name="deviceModelListId"]').addClass('validate[required]');
	           data.form.find('input[name="deviceModel"]').addClass('validate[required]'); */
	           /* data.form.validationEngine();
	        },  */

	        //Validate form when it is being submitted
	         formSubmitting: function (event, data) {
	       	 /*  data.form.find('input[name="deviceId"]').addClass('validate[required, custom[AlphaNumeric_loworup]]');
	             data.form.find('input[name="hostId"]').addClass('validate[required]'); */
	            /*  data.form.find('input[name="deviceModelListId"]').addClass('validate[required]');
	             data.form.find('input[name="deviceModel"]').addClass('validate[required]'); */
	             data.form.validationEngine();
	            return data.form.validationEngine('validate');
	        }, 
	         //Dispose validation logic when form is closed
	         formClosed: function (event, data) {
	            data.form.validationEngine('hide');
	            data.form.validationEngine('detach');
	        }  
			});
		 $('#jtablescheduleTestRunContainer').jtable('load');

		
		
		
		
		
	}
	