<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<div id="div_ReportIssuePopup"   class="modal " tabindex="-1" aria-hidden="true">

	    <div class="modal-dialog" style="width: 500px;height:400px;" >
		<div class="modal-content" style="background-color:#eee;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" title="Press Esc to close" aria-hidden="true"></button>
			<h4 class="modal-title caption-subject theme-font"><strong><FONT color=#9eacb4>Report Issue</FONT></strong></h4> 
          </div>
			<div class="modal-body" >
			<div class="information">
		<p><FONT color=#DC143C><B>Note: </B></FONT> This is a restricted access site and intended for employees of HCL Technologies Ltd. (HCL).
		  Report will be  emailed to <A tabIndex=16  href="mailto:ERSDeploymentTool@hcl.com"><FONT color=#000080><B><U>ERSDeploymentTool@hcl.com</U></B></FONT></A> <p> 
		</div>
		
			<div class="portlet light" style="background-color:#eee;">								
							<div class="portlet-body form">
							  	<div class="skin skin-flat">
							  		<form role="form">										
										<div class="input-group">
									 		<label align ='center' style="padding-right: 25px;">Report Issue Type: </label>
											<select id="reportIssueType" name="reportIssueType" style="padding-right: 40px;">
												<option id="INFO">INFO</option>
												<option id="SERVICE REQUEST">SERVICE REQUEST</option>
												<option id="INCIDENT">INCIDENT</option>
												<option id="PROBLEM">PROBLEM</option>
												<option id="SUGGESSION">SUGGESSION</option>
												<option id="OTHERS">OTHERS</option>
											</select> 
										</div>
								</div>
							</div>													
								<div class="form-body" style="padding-top: 7px;padding-bottom: 0px;">																	
									<div class="form-group">													
										<div class="input-group">
											<textarea id="reportIssueInput" style="width: 400px;height:100px;background-color:#ffffff;"></textarea>	 
										</div>
									</div>									
								</div>										
								<div class="form-actions" style="padding-top: 10px;padding-bottom: 10px;">
									<div class="row">
				                       <div id="button">
					                      <div class="col-md-12">
						                    <div class="form-group">
						                       <div class="col-md-6">
						                         <button type="button" id="save" class="btn green-haze" onclick="sendReportIssueMail()">Submit</button>
						                          <button type="button"  class="btn grey-cascade"onclick="cancel()">Cancel</button>
						                         </div>
						                     </div>
					                        </div>
				                         </div>
				                       </div>
                                 </div>
							</form>
						</div>  
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>

<!-- <link rel="stylesheet" href="css/mainmenu.css" type="text/css" media="all"> -->
<script src="files/lib/metronic/theme/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript">
function menuLoader(roleNametoShow){
   	var JsonData;
   	if(roleNametoShow == "Engagement Manager")
	{
		JsonData = engagementManager_JsonData();
		getMenu(JsonData);
   	}
	else if(roleNametoShow == "TestManager")
	{
		JsonData = testManager_JsonData();
		getMenu(JsonData);		
	}
	else if(roleNametoShow == "ResourceManager")
	{	   
		JsonData = resourceManager_JsonData();
		getMenu(JsonData);		
   	}
	else if(roleNametoShow == "Administrator" )
	{
		JsonData = administrator_JsonData();
		getMenu(JsonData);			
  	}
	else if (roleNametoShow == "TestLead")
	{		   
		JsonData = testLead_JsonData();
		getMenu(JsonData);		
    }
   else if (roleNametoShow == "Tester")
	{   		
	    JsonData = tester_JsonData();
	    getMenu(JsonData);	   
	}
	else if (roleNametoShow == "ProgramManager")
	{		
		JsonData = programManager_JsonData();
	    getMenu(JsonData);	 
   }
   else if (roleNametoShow == "PQAReviewer")
	{		
		JsonData = pqaReview_JsonData();
	    getMenu(JsonData);	 
   }
   else if (roleNametoShow == "Guest"){		
		JsonData = guest_JsonData();
	    getMenu(JsonData);	 
  }else {			
 	   var contextpath = (window.location.pathname).split("/", 2);
 		var root = window.location.protocol	+ "//"+ window.location.host+ "/"+ contextpath[1]
 		window.location.assign(root);
		
    }
	
		var loc = window.location.href;
		var loc2 = loc.split("/");
		loc2 = loc2[loc2.length - 1];
		var indx = loc2.indexOf("?");
		if (indx > 0)
			loc2 = loc2.substring(0, indx);
			
		var activeLink;
		var breadCrumbData= [];
		/* $.each(JsonData.menu, function (ind,elem) {
			if(elem.sub.length > 0){
				$.each(elem.sub, function (subind,subelem) {
					if(loc2 == subelem.link){
						activeLink = elem.link;
						breadCrumbData.push(elem.name);
						breadCrumbData.push(subelem.name);
						break;
					}
			    });
			}else{
				activeLink = -1;
			}
	    }); */
	    var cookie_menu_value=getCookie('selectedmenu');
	    $.each(JsonData.menu, function (ind,elem) {
			
			 if(elem.sub.length > 0){				
				$.each(elem.sub, function (subind,subelem) {					
					if(subelem.sub && subelem.sub.length > 0){
						$.each(subelem.sub, function (subsubind,subsubelem) {
							if(cookie_menu_value!=''){
									if(cookie_menu_value==elem.name && loc2 == subsubelem.link){
										activeLink = elem.link;
										breadCrumbData.push(elem.name);
										breadCrumbData.push(subsubelem.name);
									}
								}else if(loc2 == subsubelem.link){
								activeLink = elem.link;
								breadCrumbData.push(elem.name);
								breadCrumbData.push(subsubelem.name);								
							}
					    });
					}else{
						if(cookie_menu_value!=''){
									if(cookie_menu_value==elem.name && loc2 == subelem.link){
										activeLink = elem.link;
										breadCrumbData.push(elem.name);
										breadCrumbData.push(subelem.name);
									}
								}else if(loc2 == subelem.link){
									activeLink = elem.link;
									breadCrumbData.push(elem.name);
									breadCrumbData.push(subelem.name);							
								}						
						}					
			    });		
								
			}else{
				activeLink = -1;
			}
	    });
		if(breadCrumbData.length>0){
			if(breadCrumbData[0] != "Help"){
				setBreadCrumbMenu(breadCrumbData[0], breadCrumbData[1]);
			}
		}
		if(typeof activeLink == "undefined")
			activeLink = 2;
		else
			activeLink = parseInt(activeLink) + 2;
		$("#menuList>li:nth-child("+ activeLink +")").addClass("active");
		$("#menuList>li:nth-child("+ activeLink +")").siblings().removeClass("active");
}

function showGraphScreen(dashBoardObj){    

	if(dashBoardObj.TotalRecordCount >0){
		 var myTestCase = "administration.dashboard.metricBoard";			
			if('${roleName}' != "Tester"){
				var pathArr = window.location.pathname.split('/');
				if(pathArr.length>1){
					if(pathArr[2] == "home"){				
						location.href = myTestCase;
					}
				}	
			}
	 }	 
}

function dashboardScreen(url){
	 $.ajax({
			type: "POST",
			url: url,
			success : function(data) {
				closeLoaderIcon();
				if(data.TotalRecordCount >0){				
		    		showGraphScreen(data);
		    	}else{
		    		return false;
		    	}
			},
			error : function(data){
				closeLoaderIcon();
			},
			complete : function(data){
				closeLoaderIcon();
			}
		});
}

$(document).ready(function() {	
	menuLoader('${roleName}');	
	loadjscssfile("js/viewScript/menuList.js", "js"); //dynamically load and add this .js file
		
	// ----- dashboard url should be called only for the home screen menu item ---
	var pathArr = window.location.pathname.split('/');
	if(pathArr.length>1 && '${roleName}' != "Tester"){
		if(pathArr[2] == "home"){
			dashboardScreen('administration.dashboardTabRoleBased.list?tabId=-1&jtStartIndex=0&jtPageSize=10');
		}
	}
	
 	//var myTestCase = $("#MyTestCases").find('a').attr("href");
 	 var workFlow = $("#MyActions_tab > ul > li > a").eq(1).attr("href");
	 if('${roleName}' == "Tester"){				
			var pathArr = window.location.pathname.split('/');
			if(pathArr.length>1){
				if(pathArr[2] == "home"){				
					location.href = workFlow;
				}
			}	
		}	
		 
});
		// ----- ENGAGEMENT-MANAGER MENU LIST STARTED -----
		
		function engagementManager_JsonData(){
			var JsonData = {
					menu: [{
			            name: 'Home',
			            link: 'home?filter=1',
			            sub: null
			        },{
			            name: 'Plan',
			            link: '1',
			            sub: [{
			                name: 'Product Management',
			                link: 'product.management.plan',
			                sub: null
			            }]
			        },{
						name: 'Test',
						link: '2',
						sub: [{
							 name: 'Create New Work Package',
							 link: 'administration.workpackage',
							 sub: null
						},
						{
							name: 'Work Packages',
							link: 'administration.workpackage.plan',
							sub: null
						},{
							name: 'Test Plans',
							link: 'testrunplan.view',
							sub: null
						},{
							name: 'Test Reports',
							link: 'javascript:;',
							sub: [{
								name: 'Resource Planning ',
								link: 'show.overall.demand.and.reservation',
								sub: null			               
							}]			               
						}]
					},{
						name: 'Activities',
						link: '3',
						sub: [{
								name: 'Activity Management ',
								link: 'process.activityGrouping',
								sub: null			               
							},{
								name: 'Activity Report',
								link: 'javascript:;',
								sub: [{
										name: 'Activities Status Report',
										link: 'activities.status.report',
										sub: null
								}, {
									 name: 'Activity Effort Report',
									 link: 'activitytask.effort.report',
									 sub: null
								},{
									 name: 'Generic Report',
									 link: 'generate.report',
									 sub: null
								}]
							}]
						},{
							name: 'Resources',
							link: '4',
							sub: [{
								name: 'Resource Planning ',
								link: 'show.overall.demand.and.reservation',
								sub: null			               
							}/* ,{
								name: 'Resource Report',
								link: 'javascript:;',
								sub: [{
									 name: 'Resource Effort Report',
									 link: 'resource.effort.report',
									 sub: null
								}]
							} */]
						},/*{
			        	name: 'Resource',
			        	link: '2',
			        	sub: [{
			        		name: 'Factory',
			        		link: 'javascript:;',
			        		sub: [{
			        			name: 'Resource Demand/Availability',
			        			link: 'workpackage.resource.availability',
			        			sub: null
			        		}]
			        	}]
			        },{
			        	name: 'Resource Management',
			        	link: '4',
			        	sub: [{
			        		name: 'Factory',
			        		link: 'javascript:;',
			        		sub: [{
			        			name: 'Resource Fulfilment',
			        			link: 'resource.management.plan',
			        			sub: null
			        		},{
			        			name: 'Resource Tracking',
			        			link: 'resource.tracking.plan',
			        			sub: null
			        		},{
			        			name: 'Resource Reservations',
			        			link: 'workpackage.resource.reservation',
			        			sub: null
			        		}]
			        	}]
		        },*/{
						name: 'My Actions',
						link: '5',
						sub: [{
							name: 'Workflow',
							link: 'workflow.my.actions',
							sub: null
						},{
			                name: 'My Activity',
			                link: 'my.activities',
			                sub: null
			            },{
			        		name: 'References',
			        		link: 'engagement.product.clarifications',
			        		sub: null
			        	},{
			        		name: 'Audit History',
			        		link: 'audit.history.byUser',
			        		sub: null
			        	}]
                    },{
			            name: 'DashBoard',
			            link: '6',
			        	sub: [{
			            	 name: 'Pivot Reports',
				             link: 'javascript:;',
				             sub: [{
	        			 		name: 'Pivot Reports - Basic',
			             		link: 'pivot.nreco.report',
			             		sub: null
	        				},{
			        			name: 'Pivot Reports - Advanced',
			        			link: 'pivot.nreco.advance.report',
			        			sub: null							        		
		        			}]
			        	}]
                    },/*{
			        	name: 'Reports',
			        	link: '5',
			        	sub: [{
			        		name: 'Factory',
			        		link: 'javascript:;',
			        		sub: [{
			        			name: 'Timesheet Statistics',
			        			link: 'report.timesheet.statistics',
			        			sub: null
			        		}, {
			        			name: 'Resource Reliability',
			        			link: 'resource.reliablity.report',
			        			sub: null
			        		}]
			        	},{
			        		name: 'Work Package',
			        		link: 'javascript:;',
			        		sub: [{
			        			name: 'EOD - Work Package TestCase Results',
			        			link: 'report.workpackage.testcase.plan.results.eod',
			        			sub: null
			        		}, {
			        			name: 'EOD - Work Package Statistics',
			        			link: 'report.workpackage.statistics.eod',
			        			sub: null
			        		}]
			        	},{
			                name: 'Defects Weekly Report',
			                link: 'defects.weekly.report',
			                sub: null
			            },{
			                name: 'Detailed Effort Report',
			                link: 'environment.combination.report',
			                sub: null
			            },{
			            	name: 'Activity Report',
			        		link: 'javascript:;',
			        		sub: [{
					                name: 'Activities Status Report',
					                link: 'activities.status.report',
					                sub: null
			        		}, {
			        			 name: 'Activity Effort Report',
					             link: 'activitytask.effort.report',
					             sub: null
			        		},{
				            	 name: 'Generic Report',
					             link: 'generate.report',
					             sub: null
				            }]
			            },{
			            	name: 'Resource Report',
			        		link: 'javascript:;',
			        		sub: [ {
			        			 name: 'Resource Effort Report',
					             link: 'resource.effort.report',
					             sub: null
			        		}]
			            },{
			            	 name: 'Pivot Reports',
				             link: 'javascript:;',
				             sub: [ 
				                    {
			        			 		name: 'Pivot Reports - Basic',
					             		link: 'pivot.nreco.report',
					             		sub: null
			        				},{
					        			name: 'Pivot Reports - Advanced',
					        			link: 'pivot.nreco.advance.report',
					        			sub: null
							        		
					        		}
				             	]
			            },]
			        },*/{
			            name: 'Setup',
			            link: '7',
			            sub: [{
			                name: 'Customer Management',
			                link: 'customer.management.plan',
			                sub: null
			            },{
			                name: 'Engagement Management',
			                link: 'testFactory.testFactories',
			                sub: null
			            }]
			        },{
			            name: 'Help',
			            link: '8',
			            sub: [{
			                name: 'Help Files',
			                link: 'WebHelp/index.htm',
			                sub: null
			            }, {
			                name: 'FAQs',
			                link: 'WebHelp/index.htm#faq.htm',
			                sub: null
			            }, {
			                name: modeAboutUsHandler(),
			                link: 'about.palm',
			                sub: null
			            }]
			        }]
			    };
			
			return JsonData;    	
		}
		
		// ----- ENGAGEMENT-MANAGER MENU LIST ENDED-----
		
		// ----- TEST-MANAGER MENU LIST STARTED-----
		
		function testManager_JsonData(){
			var JsonData = {
					menu: [{
			            name: 'Home',
			            link: 'home?filter=1',
			            sub: null
			        },{
						name: 'Plan',
						link: '1',
						sub: [{
							name: 'Product Management',
							link: 'product.management.plan',
							sub: null
						},{
							name: 'Product Version TestCase Mapping ',
							link: 'product.version.testcase.plan',
							sub: null
						},{
							name: 'Devices',
							link: 'administration.host',
							sub: null
							
						},{
							name: 'Entity Types',
							link: 'entity.type.management.plan',
							sub: null
						   
						}/*,{
							name: 'Workflow Summary',
							link: 'workflow.summary',
							sub: null
						   
						}*/]
					},{
						name: 'Test',
						link: '2',
						sub: [{
							 name: 'Create New Work Package',
							 link: 'administration.workpackage',
							 sub: null
							},
							{
								name: 'Work Packages',
								link: 'administration.workpackage.plan',
								sub: null
							},/*{
								name: 'Monitor Work Package',
								link: 'workPackage.status.summary.view',
								sub: null
							},*/{
								name: 'Test Plans',
								link: 'testrunplan.view',
								sub: null
							},{
								name: 'Test Reports',
								link: 'javascript:;',
								sub: [{
									name: 'Resource Planning ',
									link: 'show.overall.demand.and.reservation',
									sub: null			               
								}]			               
							}]
					},{
						name: 'Activities',
						link: '3',
						sub: [{
								name: 'Activity Management ',
								link: 'process.activityGrouping',
								sub: null			               
							},{
								name: 'Activity Report',
								link: 'javascript:;',
								sub: [{
									name: 'Activities Status Report',
									link: 'activities.status.report',
									sub: null
							}, {
								 name: 'Activity Effort Report',
								 link: 'activitytask.effort.report',
								 sub: null
							},{
								 name: 'Generic Report',
								 link: 'generate.report',
								 sub: null
							}]
						}]
					},/*{
			        	name: 'Resource',
			        	link: '4',
			        	sub: [{
			        		name: 'Factory',
			        		link: 'javascript:;',
			        		sub: [{
			        			name: 'Resource Reservation',
			        			link: 'workpackage.resource.reservation',
			        			sub: null
			        		},{
			        			name: 'Resource Planning',
			        			link: 'workpackage.resource.planning',
			        			sub: null
			        		},{
			        			name: 'Resource Demand/Availability',
			        			link: 'workpackage.resource.availability',
			        			sub: null
			        		},{
			        			name: 'Work Package - Demand Projection',
			        			link: 'workpackage.demand.projection',
			        			sub: null
			        		}]
			        	},{
			        		 name: 'View Resource Experience',
				             link: 'view.resources.experience',
		        			sub: null
			        	},
			        	{
			        		name: 'Productivity and Quality Reports',
			        		link: 'resources.productivity.quality.reports',
			        		sub: null
			        	}]
			        },*/					
					 {
			        	name: 'Resources',
			        	link: '4',
			        	sub: [ {
								name: 'Resource Planning ',
								link: 'show.overall.demand.and.reservation',
								sub: null			               
							}/* ,{
								name: 'Resource Reports',
								link: 'javascript:;',
								sub: [{
									name: 'Resource Effort Report',
									link: 'resource.effort.report',
									sub: null
								}]
							} */],
			        	},					
					{
			        	name: 'My Actions',
			        	link: '5',
			        	sub: [
						{
							name: 'My Test Cases',
							link: 'workpackage.testcase.execution.view',
							sub: null
						},{
			                name: 'My Activity',
			                link: 'my.activities',
			                sub: null
			            },
			           /*{
			        		name: 'Factory',
			        		link: 'javascript:;',
			        		sub: [{
			        			name: 'Time Management',
			        			link: 'user.time.management',
			        			sub: null
			        		},{
			        			name: 'My Attendance',
			        			link: 'resource.attendance.availability.user',
			        			sub: null
			        		},{
			        			name: 'Schedule Actual Shift',
			        			link: 'shift.entries.manage',
			        			sub: null
			        		}]
			        	},{
			        		name: 'Pending Approvals',
			        		link: 'javascript:;',
			        		sub: [{
			        			name: 'Approve Timesheets',
			        			link: 'timesheet.entries.approve',
			        			sub: null
			        		},{
			        			name: 'Shift Approvals',
			        			link: 'myApprovals.view',
			        			sub: null
			        		},{
			        			name: 'Approve Resource Performance',
			        			link: 'resource.dailyperformance.approve',
			        			sub: null
			        		},{
			        			name: 'Approve Skills',
			        			link: 'administration.userskills.approve',
			        			sub: null
			        		}]
			        	},{
			        		name: 'Review',
			        		link: 'javascript:;',
			        		sub: [{
			        			name: 'Review Test Case Executions',
			        			link: 'workpackage.testcase.plan.reviewTestLeadTestCases',
			        			sub: null
			        		},{
						    	 name: 'Review Defects',
					             link: 'workpackage.testcase.plan.reviewDefects',
					             sub: null
						    },{
						    	 name: 'Analyze Defects',
					             link: 'workpackage.analyse.defects',
					             sub: null
						    }, {
			        			 name: 'Review Activities',
			        			 link: 'process.review.activity',
			        			 sub: null
			        		}]
			        	},*/{
			        		name: 'References',
			        		link: 'engagement.product.clarifications',
			        		sub: null
			        	},{
			        		name: 'Audit History',
			        		link: 'audit.history.byUser',
			        		sub: null
			        	}]
                    },/*{
			        	name: 'Reports',
			        	link: '5',
			        	sub: [{
			        		name: 'Factory',
			        		link: 'javascript:;',
			        		sub: [{
			        			name: 'Timesheet Statistics',
			        			link: 'report.timesheet.statistics',
			        			sub: null
			        		}, {
			        			name: 'Resource Reliability',
			        			link: 'resource.reliablity.report',
			        			sub: null
			        		}]
			        	},{
			        		name: 'Work Package',
			        		link: 'javascript:;',
			        		sub: [{
			        			name: 'EOD - Work Package TestCase Results',
			        			link: 'report.workpackage.testcase.plan.results.eod',
			        			sub: null
			        		}, {
			        			name: 'EOD - Work Package Statistics',
			        			link: 'report.workpackage.statistics.eod',
			        			sub: null
			        		}]
			        	},{
			                name: 'Defects Weekly Report',
			                link: 'defects.weekly.report',
			                sub: null
			            },{
			                name: 'Detailed Effort Report',
			                link: 'environment.combination.report',
			                sub: null
			            },{
			            	name: 'Activity Report',
			        		link: 'javascript:;',
			        		sub: [{
					                name: 'Activities Status Report',
					                link: 'activities.status.report',
					                sub: null
			        		}, {
			        			 name: 'Activity Effort Report',
					             link: 'activitytask.effort.report',
					             sub: null
			        		},{
				            	 name: 'Generic Report',
					             link: 'generate.report',
					             sub: null
				            }]
			            },{
			            	name: 'Resource Report',
			        		link: 'javascript:;',
			        		sub: [ {
			        			 name: 'Resource Effort Report',
					             link: 'resource.effort.report',
					             sub: null
			        		}]
			            },{
			            	 name: 'Pivot Reports',
				             link: 'javascript:;',
				             sub: [ 
				                    {
			        			 		name: 'Pivot Reports - Basic',
					             		link: 'pivot.nreco.report',
					             		sub: null
			        				},{
					        			name: 'Pivot Reports - Advanced',
					        			link: 'pivot.nreco.advance.report',
					        			sub: null
							        		
					        		}
				             	]
			            },{
			                name: 'Demand And Reservation ',
			                link: 'show.overall.demand.and.reservation',
			                sub: null			               
			            } ]
			        },*/
		            {
			            name: 'DashBoard',
			            link: '6',
			            sub: [{
			                name: 'Workpackage Status',
			                link: 'administration.dashboard.status',
			                sub: null
			            },{
			            	 name: 'Pivot Reports',
				             link: 'javascript:;',
				             sub: [ 
				                    {
			        			 		name: 'Pivot Reports - Basic',
					             		link: 'pivot.nreco.report',
					             		sub: null
			        				},{
					        			name: 'Pivot Reports - Advanced',
					        			link: 'pivot.nreco.advance.report',
					        			sub: null
							        		
					        		}
				             	]
			            }]
		            },{
			            name: 'Setup',
			            link: '7',
			            sub: [{
			                name: 'User Management',
			                link: 'javascript:;',
			                sub: [{
						        name: 'Manage Flex Users',
						        link: 'administration.user',
						        sub: null
						    }]
					   },{
			                name: 'Competency Management',
			                link: 'competency.management.plan',
			                sub: null
			               
			            },{
			                name: 'Workflow Management',
			                link: 'javascript:;',
			                sub: [{
			                	name : 'Entity Worflow Mapping',
			                	link : 'workflow.product.entity.mapping',
			                	sub : null
			                }]
						}],										            
			        },{
			        	name: 'Admin',
			        	link: '8',
			        	sub: [{
			                name: 'Data Extractor',
			                link: 'data.source.extractor',
			                sub: null			               
						}]			        	
			        },{
		            	name: 'Help',
			            link: '9',
			            sub: [{
			                name: 'Help Files',
			                link: 'WebHelp/index.htm',
			                sub: null
			            }, {
			                name: 'FAQs',
			                link: 'WebHelp/index.htm#faq.htm',
			                sub: null
			            }, {
			                name: modeAboutUsHandler(),
			                link: 'about.palm',
			                sub: null
			            }]
			        }],
			    };
			
			return JsonData;    	
		}
		
		// ----- TEST-MANAGER MENU LIST ENDED-----
		
		// ----- RESOURCE-MANAGER MENU LIST STARTED-----
		
		function resourceManager_JsonData(){
			var JsonData = {
					menu: [{
			            name: 'Home',
			            link: 'home?filter=1',
			            sub: null
			        },{
						name: 'My Actions',
						link: '1',
						sub: [{
							name: 'Workflow',
							link: 'workflow.my.actions',
							sub: null
						},{
			                name: 'My Activity',
			                link: 'my.activities',
			                sub: null
			            },						
						{
			        		name: 'References',
			        		link: 'engagement.product.clarifications',
			        		sub: null
			        	},{
			        		name: 'Audit History',
			        		link: 'audit.history.byUser',
			        		sub: null
			        	}]
			        },{
			        	name: 'Resource Management',
			        	link: '2',
			        	sub: [{
			        		name: 'Factory',
			        		link: 'javascript:;',
			        		sub: [{
			        			name: 'Resource Fulfilment',
			        			link: 'resource.management.plan',
			        			sub: null
			        		},{
			        			name: 'Resource Tracking',
			        			link: 'resource.tracking.plan',
			        			sub: null
			        		},{
			        			name: 'Resource Reservations',
			        			link: 'workpackage.resource.reservation',
			        			sub: null
			        		}]
			        	}]
			        },{
			            name: 'Resource Pool Management',
			            link: '3',
			            sub: [{
			                name: 'Manage Resource Pool',
			                link: 'resource.pool.management',
			                sub: null
			            },{
			                name: 'Manage Resources',
			                link: 'administration.user',
			                sub: null
			            },{
			                name: 'Approve Skills',
			                link: 'administration.userskills.approve',
			                sub: null
			            }]
			        },{
			            name: 'Dashboard',
			            link: '4',
			            sub: [{
			                name: 'My Dashboard',
			                link: 'resource.management.dashboard',
			                sub: null
			            }]
			        },
			        {
			        	name: 'Help',
			            link: '5',
			            sub: [{
			                name: 'Help Files',
			                link: 'WebHelp/index.htm',
			                sub: null
			            }, {
			                name: 'FAQs',
			                link: 'WebHelp/index.htm#faq.htm',
			                sub: null
			            }, {
			                name: modeAboutUsHandler(),
			                link: 'about.palm',
			                sub: null
			            }]
			        }]
			    };
			
			return JsonData;    	
		}
		
		// ----- RESOURCE-MANAGER MENU LIST ENDED-----
					
		// ----- ADMINISTRATOR MENU LIST STARTED-----
		
		function administrator_JsonData(){
			var JsonData = {
					menu: [{
			            name: 'Home',
			            link: 'home?filter=1',
			            sub: null,
			        },{
			            name: 'Plan',
			            link: '1',
			            sub: [{
				                name: 'Product Management',
				                link: 'product.management.plan',
				                sub: null
						},							
						{
							name: 'Product Version TestCase Mapping ',
							link: 'product.version.testcase.plan',
							sub: null
						},{
							name: 'Devices',
							link: 'administration.host',
							sub: null
						},{
							name: 'Entity Types',
							link: 'entity.type.management.plan',
							sub: null
						   
						},{
							name: 'Custom Fields',
							link: 'custom.field.configuration',
							sub: null
						   
						},{
							name: 'Notifications Management',
							link: 'notification.product.management',
							sub: null
						},{
							name: 'Tool Integration Master',
							link: 'tool.intagration.master',
							sub: null
						}]
			        },{
			        	name: 'Test',
			        	link: '2',
			        	sub: [{
							 name: 'Create New Work Package',
							 link: 'administration.workpackage',
							 sub: null
						},
						{
							name: 'Work Packages',
							link: 'administration.workpackage.plan',
							sub: null
						},/*{
							name: 'Monitor Work Package',
							link: 'workPackage.status.summary.view',
							sub: null
						},*/{
							name: 'Test Plans',
							link: 'testrunplan.view',
							sub: null
						},{
							name: 'Test Reports',
							link: 'javascript:;',
							sub: [{
								name: 'Resource Planning ',
								link: 'show.overall.demand.and.reservation',
								sub: null			               
							}]			               
						},{
							name: 'BDD Keywords Phrases',
							link: 'administration.BDDkeywordsphrases',
							sub: null
						}]
			        },{
			        		name: 'Activities',
			        		link: '3',
			        		sub: [/*{
			        			 name: 'Activity WorkPackage',
			        			 link: 'process.activityWorkPackage',
			        			 sub: null
			        		},{
			        			 name: 'Activity',
			        			 link: 'process.activity',
			        			 sub: null
			        		},{
			        			 name: 'Activity Tasks',
			        			 link: 'process.activityTask',
			        			 sub: null
			        		},*/{
								name: 'Activity Management ',
								link: 'process.activityGrouping',
								sub: null			               
							},{
								name: 'Activity Reports',
								link: 'javascript:;',
								sub: [{
					                name: 'Activities Status Report',
					                link: 'activities.status.report',
					                sub: null
								},{
									 name: 'Activity Effort Report',
									 link: 'activitytask.effort.report',
									 sub: null
								},{
									 name: 'Generic Report',
									 link: 'generate.report',
									 sub: null
								}]			               
							}/* {
			        			 name: 'Assigned Activities',
			        			 link: 'process.assigned.activity',
			        			 sub: null
			        		}, {
			        			 name: 'Review Activities',
			        			 link: 'process.review.activity',
			        			 sub: null
			        		}*/]
			        	},{
			        	name: 'Resources',
			        	link: '4',
			        	sub: [ {
								name: 'Resource Planning ',
								link: 'show.overall.demand.and.reservation',
								sub: null			               
							},/*{
			        		name: 'Factory',
			        		link: 'javascript:;',
			        		sub: [{
									name: 'Resource Fulfilment',
									link: 'resource.management.plan',
									sub: null
								},{
									name: 'Resource Tracking',
									link: 'resource.tracking.plan',
									sub: null
								},{
									name: 'Resource Reservation',
									link: 'workpackage.resource.reservation',
									sub: null
								},{
									name: 'Resource Planning',
									link: 'workpackage.resource.planning',
									sub: null
								},{
									name: 'Resource Demand/Availability',
									link: 'workpackage.resource.availability',
									sub: null
								},{
									name: 'Work Package - Demand Projection',
									link: 'workpackage.demand.projection',
									sub: null
								},{
									name: 'Resource Demand/Availability Calendar View',
									link: 'resource.calendar.monthly.view',
									sub: null
								},{
									name: 'Resource Planning ',
									link: 'show.overall.demand.and.reservation',
									sub: null			               
								}]
							},*/{
								name: 'Resource Reports',
								link: 'javascript:;',
								sub: [/* {
									name: 'Timesheet Statistics',
									link: 'report.timesheet.statistics',
									sub: null
								}, {
									name: 'Resource Reliability',
									link: 'resource.reliablity.report',
									sub: null
								}, */{
									name: 'EOD - Work Package TestCase Results',
									link: 'report.workpackage.testcase.plan.results.eod',
									sub: null
								}, {
									name: 'EOD - Work Package Statistics',
									link: 'report.workpackage.statistics.eod',
									sub: null
								},{
									name: 'Defects Weekly Report',
									link: 'defects.weekly.report',
									sub: null
								}/* ,{
									name: 'Detailed Effort Report',
									link: 'environment.combination.report',
									sub: null
								},{
									name: 'Resource Effort Report',
									link: 'resource.effort.report',
									sub: null
								} */]
							}],
			        	},{
						name: 'My Actions',
						link: '5',
						sub: [{
							name: 'Workflow',
							link: 'workflow.my.actions',
							sub: null
						},{
			                name: 'My Activity',
			                link: 'my.activities',
			                sub: null
			            },{
			        		name: 'References',
			        		link: 'engagement.product.clarifications',
			        		sub: null
			        	},{
			        		name: 'Audit History',
			        		link: 'audit.history.byUser',
			        		sub: null
			        	}/*,{
							name: 'My Tasks',
							link: '6',
							sub: [{
								name: 'Factory',
								link: 'javascript:;',
								sub: [{
									 name: 'Resources Attendance',
									 link: 'resource.attendance.availability',
									 sub: null
								}]
							},{
								name: 'Pending Approvals',
								link: 'javascript:;',
								sub: [{
									 name: 'Approve Timesheets',
									 link: 'timesheet.entries.approve',
									 sub: null
								},{
									 name: 'Approve Resource Performance', 
									 link: 'resource.dailyperformance.approve',
									 sub: null
								}]
							}]
						}*/]
			        },{
			            name: 'DashBoard',
			            link: '6',
			            sub: [{
			                name: 'Dashboard Tab SetUp',
			                link: 'adding.tabs.dasboard',
			                sub: null
			            },
			            
			            {
			                name: 'Data Sync',
			                link: 'bulkPush.from.SqlToMongoDB',
			                sub: null
			            },
			                  {
			                name: 'DashBoard',
			                link: 'kibana.elastic.search',
			              // link:'dashboard/index.html',
			                sub: null
			            },{
			        	name: 'Reports',
			        	link: '7',
			        	sub: [{
			            	 name: 'Pivot Reports',
				             link: 'javascript:;',
				             sub: [ 
				                    {
			        			 		name: 'Pivot Reports - Basic',
					             		link: 'pivot.nreco.report',
					             		sub: null
			        				},{
					        			name: 'Pivot Reports - Advanced',
					        			link: 'pivot.nreco.advance.report',
					        			sub: null
							        		
					        		}
				             	]
			            },/* {
			                name: 'Demand And Reservation ',
			                link: 'show.overall.demand.and.reservation',
			                sub: null			               
			            } */]
						}]
		            },{
			            name: 'Setup',
			            link: '7',
			            sub: [{
			                name: 'User Management',
			                link: 'javascript:;',
			                sub: [{
						        name: 'Manage Regular Users',
						        link: 'administration.product.team.users',
						        sub: null
						    },{
						        name: 'Manage Flex Users',
						        link: 'administration.user',
						        sub: null
						    },{
						    	 name: 'Manage Skill',
							     link: 'administration.skill',
							     sub: null
						    },{
						    	 name: 'Onboard User Approval',
							     link: 'administration.onboard.user.approval',
							     sub: null
						    },{
						    	name: 'Report Issue',
						    	link: 'administration.report.issue',
						    	sub: null
						    }]
			            },{
			                name: 'Customer Management',
			                link: 'customer.management.plan',
			                sub: null
			            },{
			                name: 'Dimension Management',
			                link: 'dimension.management.plan',
			                sub: null
			               
			            },{
			                name: 'Competency Management',
			                link: 'competency.management.plan',
			                sub: null
			               
			            },{
			                name: 'Status Management',
			                link: 'status.management.plan',
			                sub: null
			               
			            },{
			                name: 'Workflow Management',
			                link: 'javascript:;',
			                sub: [{
				                name: 'Add Workflow Templates',
				                link: 'workflows.management.plan',
				                sub: null
				            },{
			                	name : 'Entity Worflow Mapping',
			                	link : 'workflow.product.entity.mapping',
			                	sub : null
			                }]
			               
			            },{
			                name: 'Environment Group',
			                link: 'administration.environmentGroup',
			                sub: null
			            }]
			        },
		            {
			            name: 'Admin',
			            link: '8',
			            sub: [{
			                name: 'Manage Test Centers',
			                link: 'administration.testFactoryLabs',
			                sub: null
			            },{
			                name: 'Manage Vendors',
			                link: 'administration.vendor',
			                sub: null
			            },{
			            	name: 'Index Elastic Search',
						     link: 'administration.indicies',
			                sub: null
			            },{
			                name: 'Data Extractor',
			                link: 'data.source.extractor',
			                sub: null
			               
			            },{
							name : 'Create Help',
							link : 'administration.help',
							sub : null
						}
			            
			            /* ,{
                            name: 'Bot Configuration',
                            link: 'bot.details.configuration',
                   			sub: null
                   		  } */]
		            },
		            {
		            	name: 'Help',
			            link: '9',
			            sub: [{
			                name: 'Help Files',
			                link: 'WebHelp/index.htm',
			                sub: null
			            }, {
			                name: 'FAQs',
			                link: 'WebHelp/index.htm#faq.htm',
			                sub: null
			            }, {
			                name: modeAboutUsHandler(),
			                link: 'about.palm',
			                sub: null
			            }]
			        }]
			    };
			
			return JsonData;    	
		}
		
		// ----- ADMINISTRATOR MENU LIST ENDED-----
		
		// ----- TestLead MENU LIST STARTED-----
		
		function testLead_JsonData(){
			var JsonData = {
					menu: [{
			            name: 'Home',
			            link: 'home?filter=1',
			            sub: null
			        },
			        {
			            name: 'Plan',
			            link: '1',
			            sub: [{
			                name: 'Product Management',
			                link: 'product.management.plan',
			                sub: null
			            },{
			                name: 'Entity Types',
			                link: 'entity.type.management.plan',
			                sub: null
			               
			            }]
			        },{
						name: 'Test',
						link: '2',
						sub: [{
							 name: 'Create A New Work Package',
							 link: 'administration.workpackage',
							 sub: null
						},
						{
							name: 'Work Packages',
							link: 'administration.workpackage.plan',
							sub: null
						},/*{
							name: 'Monitor Work Package',
							link: 'workPackage.status.summary.view',
							sub: null
						},*/{
							name: 'Test Plans',
							link: 'testrunplan.view',
							sub: null
						}]
					},			        
			        {
			        	name: 'Activities',
			        	link: '3',
			        	sub: [{
			                name: 'Activity Management ',
			                link: 'process.activityGrouping',
			                sub: null			               
			            }]				         			        	
			        },
			        {
			            name: 'My Actions',
			            link: '4',
			            sub: [{
							name: 'My Test Cases',
							link: 'workpackage.testcase.execution.view',
							sub: null
						},{
			                name: 'My Activity',
			                link: 'my.activities',
			                sub: null
			            },/*{
			                name: 'Factory Activities',
			                link: 'javascript:;',
			                sub: [{
						        name: 'Time Management',
						        link: 'user.time.management',
						        sub: null
						    },{
						        name: 'My Attendance',
						        link: 'resource.attendance.availability.user',
						        sub: null
						    },
						    {
						    	name: 'My Status',
				                link: 'workPackage.status.summary.view',
						        sub: null
						    },
						    {
				                name: 'Schedule Actual Shift',
				                link: 'shift.entries.manage',
				                sub: null
				            },{
				                name: 'Resources Daily Performance',
				                link: 'report.workpackage.statistics.eod',
				                sub: null
				            }]
			            },{
			                name: 'Approvals',
			                link: 'javascript:;',
			                sub: [{
			                	  name: 'Shift Approvals',
					              link: 'myApprovals.view',
						          sub: null
						    }]
			            },{
			        		name: 'Review',
			        		link: 'javascript:;',
			        		sub: [{
			        			name: 'Review Test Case Executions',
			        			link: 'workpackage.testcase.plan.reviewTestLeadTestCases',
			        			sub: null
			        		},{
						    	 name: 'Review Defects',
					             link: 'workpackage.testcase.plan.reviewDefects',
					             sub: null
						    },{
						    	 name: 'Analyze Defects',
					             link: 'workpackage.analyse.defects',
					             sub: null
						    }]
			        	},*/{
			        		name: 'References',
			        		link: 'engagement.product.clarifications',
			        		sub: null
			        	},{
			        		name: 'Audit History',
			        		link: 'audit.history.byUser',
			        		sub: null
			        	}]
			        },
			        /*{
			        	name: 'Reports',
			        	link: '4',
			        	sub: [{
			        		name: 'Factory',
			        		link: 'javascript:;',
			        		sub: [{
			        			name: 'Timesheet Statistics',
			        			link: 'report.timesheet.statistics',
			        			sub: null
			        		}, {
			        			name: 'Resource Reliability',
			        			link: 'resource.reliablity.report',
			        			sub: null
			        		}]
			        	},{
			        		name: 'Work Package',
			        		link: 'javascript:;',
			        		sub: [{
			        			name: 'EOD - Work Package TestCase Results',
			        			link: 'report.workpackage.testcase.plan.results.eod',
			        			sub: null
			        		}, {
			        			name: 'EOD - Work Package Statistics',
			        			link: 'report.workpackage.statistics.eod',
			        			sub: null
			        		}]
			        	},{
			                name: 'Defects Weekly Report',
			                link: 'defects.weekly.report',
			                sub: null
			            },{
			                name: 'Detailed Effort Report',
			                link: 'environment.combination.report',
			                sub: null
			            }]
			        }, */
					{
			            name: 'Setup',
			            link: '5',
			            sub: [{
			                name: 'Workflow Management',
			                link: 'javascript:;',
			                sub: [{
			                	name : 'Entity Worflow Mapping',
			                	link : 'workflow.product.entity.mapping',
			                	sub : null
			                }]
			               
			            }]
			        },{
			        	name: 'Help',
			            link: '6',
			            sub: [{
			                name: 'Help Files',
			                link: 'WebHelp/index.htm',
			                sub: null
			            }, {
			                name: 'FAQs',
			                link: 'WebHelp/index.htm#faq.htm',
			                sub: null
			            }, {
			                name: modeAboutUsHandler(),
			                link: 'about.palm',
			                sub: null
			            }]
			        }]
			    };
			
			return JsonData;    	
		}
		
		// ----- TestLead MENU LIST ENDED-----
		
		// ----- Tester MENU LIST STARTED-----
		
		function tester_JsonData(){
			var JsonData = {
					menu: [{
			            name: 'Home',
			            link: 'home?filter=1',
			            sub: null
			        },/* {
			        	name: 'Plan',
			        	link: '1',
			        	sub: [{
			        		name: 'Activity Management',
			        		link: 'javascript:;',
			        		sub: [{
			        			 name: 'Activity WorkPackage',
			        			 link: 'process.activityWorkPackage',
			        			 sub: null
			        		},{
			        			 name: 'Activity',
			        			 link: 'process.activity',
			        			 sub: null
			        		},{
			        			 name: 'Activity Task',
			        			 link: 'process.activityTask',
			        			 sub: null
			        		}]
			        	}]}, */{
			            name: 'My Actions',
			            link: '1',
			            sub: [{
								name: 'My Test Cases',
								link: 'workpackage.testcase.execution.view',
								sub: null
							},{
								name: 'My Activity',
								link: 'my.activities',
								sub: null
							},/*{
			                name: 'Factory Activities',
			                link: 'javascript:;',
			                sub: [{
						        name: 'Time Management',
						        link: 'user.time.management',
						        sub: null
						    },{
						        name: 'My Attendance',
						        link: 'resource.attendance.availability.user',
						        sub: null
						    },{
						    	name: 'My Status',
				                link: 'workPackage.status.summary.view',
						        sub: null
						    }]
			            },{
			        		name: 'Review',
			        		link: 'javascript:;',
			        		sub: [{
						    	 name: 'Review Defects',
					             link: 'workpackage.testcase.plan.reviewDefects',
					             sub: null
						    } ,{
			        			 name: 'Review Activities',
			        			 link: 'process.review.activity',
			        			 sub: null
			        		}]
			        	},*/{
			        		name: 'References',
			        		link: 'engagement.product.clarifications',
			        		sub: null
			        	},{
			        		name: 'Audit History',
			        		link: 'audit.history.byUser',
			        		sub: null
			        	}]
			        },{
			        	name: 'Help',
			            link: '2',
			            sub: [{
			                name: 'Help Files',
			                link: 'WebHelp/index.htm',
			                sub: null
			            }, {
			                name: 'FAQs',
			                link: 'WebHelp/index.htm#faq.htm',
			                sub: null
			            }, {
			                name: modeAboutUsHandler(),
			                link: 'about.palm',
			                sub: null
			            }]
			        }]
			    };
			
			return JsonData;    	
		}
		
		// ----- Tester MENU LIST ENDED-----
		
		// ----- PROGRAM MANAGER MENU LIST STARTED-----
		
		function programManager_JsonData(){
			var JsonData = {
					menu: [{
			            name: 'Home',
			            link: 'home?filter=1',
			            sub: null
			        },{
			            name: 'Resources',
			            link: '1',
			            sub: [{
			                name: 'Manage Users',
			                link: 'administration.user',
			                sub: null
			            },{
			                name: 'Manage Vendors',
			                link: 'administration.vendor',
			                sub: null
			            }]
			        },{
			            name: 'Plan',
			            link: '2',
			            sub: [/* {
			                name: 'Fill My Availability',
			                link: 'resource.availability.view',
			                sub: null
			            }, */{
			                name: 'View Resource Availability',
			                link: '',
			                sub: null
			            }]
			        },{
						name: 'My Actions',
						link: '3',
						sub: [{
							name: 'Workflow',
							link: 'workflow.my.actions',
							sub: null
						},{
			                name: 'My Activity',
			                link: 'my.activities',
			                sub: null
			            }]
			        },{
			        	name: 'Help',
			            link: '4',
			            sub: [{
			                name: 'Help Files',
			                link: 'WebHelp/index.htm',
			                sub: null
			            }, {
			                name: 'FAQs',
			                link: 'WebHelp/index.htm#faq.htm',
			                sub: null
			            }, {
			                name: modeAboutUsHandler(),
			                link: 'about.palm',
			                sub: null
			            }]
			        }]
			    };
			
			return JsonData;    	
		}
		
		// ----- PROGRAM MANAGER MENU LIST ENDED-----
		
		// ----- PQA Reviewer MENU LIST STARTED-----
		
		function pqaReview_JsonData(){
			var JsonData = {
					menu: [{
			            name: 'Home',
			            link: 'home?filter=1',
			            sub: null
			        },{
			            name: 'My Actions',
			            link: '1',
			            sub: [
// 			            {
// 			                name: 'Review',
// 			                link: 'javascript:;',
// 			                sub: [{
// 						        name: 'PQA Review',
// 						        link: 'pqa.review',
// 						        sub: null
// 						    }]
// 			            }
						{
			                name: 'Activities',
			                link: 'javascript:;',
			                sub: [{
	 		        			 name: 'My Activities',
	 		        			 link: 'process.assigned.activity',
	 		        			 sub: null
	 		        		}]
			            },{
			                name: 'Workflow',
			                link: 'workflow.my.actions',
			                sub: null
			            },{
			                name: 'My Activity',
			                link: 'my.activities',
			                sub: null
			            }
			            ]

			        },{
			        	name: 'Help',
			            link: '2',
			            sub: [{
			                name: 'Help Files',
			                link: 'WebHelp/index.htm',
			                sub: null
			            }, {
			                name: 'FAQs',
			                link: 'WebHelp/index.htm#faq.htm',
			                sub: null
			            }, {
			                name: modeAboutUsHandler(),
			                link: 'about.palm',
			                sub: null
			            }]
			        }]
			    };
			
			return JsonData;    	
		}
		
		
		function guest_JsonData(){
			var JsonData = {
					menu: [{
			            name: 'Home',
			            link: 'home?filter=1',
			            sub: null
			        },{
			        	name: 'Help',
			            link: '2',
			            sub: [{
			                name: 'Help Files',
			                link: 'WebHelp/index.htm',
			                sub: null
			            }, {
			                name: 'FAQs',
			                link: 'WebHelp/index.htm#faq.htm',
			                sub: null
			            }, {
			                name: modeAboutUsHandler(),
			                link: 'about.palm',
			                sub: null
			            }]
			        }]
			    };
			
			return JsonData;    	
		}
		
		
				
		var getSubMenuItem = function (itemData) {	
			var liHtml = "<li id ="+ myfunTrim(itemData.name)+"_SubMenuTab" +">";
			if (itemData.sub) 
				liHtml = "<li class='dropdown-submenu' id ="+ myfunTrim(itemData.name) +"_SubMenuTab" +" >";
			if((itemData.name == "Help Files") || (itemData.name == "FAQs")) {	
			    var item = $(liHtml)
		        .append($("<a target='_blank' href="+itemData.link+">"+itemData.name+"</a>"));
			}else {
			    var item = $(liHtml)
			        .append($("<a href="+itemData.link+">"+itemData.name+"</a>"));
			}
		    		    
		    if (itemData.sub) {		    	
		        var subList = $("<ul  class='dropdown-menu'>");
		        $.each(itemData.sub, function () {
		        	subList.append(getSubSubMenuItem(this));
		        });
		        item.append(subList);
		    }	   
		    return item;
		};
		
		var getSubSubMenuItem = function (itemData) {		    
		     var item = $('<li id="' +myfunTrim(itemData.name)+ '">')
		        .append($("<a href="+itemData.link+">"+itemData.name+"</a>"));
		    
		    if (itemData.sub) {
		        console.log("No No sub-sub-sub menu items available");		        
		    }		   
		    return item;
		};
		
		function myfunTrim(x) {
		    return x.replace(/ /g,'');
		}
    
		var getMenuItem = function (itemData) {
			 var item = $('<li id="' +myfunTrim(itemData.name)+ "_tab" + '" class="menu-dropdown classic-menu-dropdown">')		    
		        .append($("<a data-hover='megamenu-dropdown' data-close-others='true' data-toggle='dropdown href='#'"+itemData.link+">"+itemData.name+		        	
		          "<i class='fa fa-angle-down'>"));
		    
		    if (itemData.sub) {
		        var subList = $("<ul  class='dropdown-menu pull-left'>");
		        $.each(itemData.sub, function () {
		            subList.append(getSubMenuItem(this));
		        });
		        item.append(subList);		        
		    }
		   
		    return item;
		};
		
		  function getMenu(JsonData){   
		        var $menu = $("#menu");
		        $("#menu").empty();
		        $menu.append($("<div class='hor-menu'><ul id='menuList' class='nav navbar-nav'><li class='dropdown dropdown-quick-sidebar-toggler'>"+
				"<a href='javascript:;' class='dropdown-toggle'><i class='icon-logout'></i></a></li><li id="+ JsonData.menu[0].name + "_tab" +"><a href="+JsonData.menu[0].link+">"+JsonData.menu[0].name+"</a>"));
  		    	
				JsonData.menu.shift(); 					// removes the first element in the array collection.
				$.each(JsonData.menu, function () {
 		         	$('#menuList').append(
 		             	getMenuItem(this)
 		         );
 		    });
	         $('#menu').append('<div style="float:right;padding-top:12px;padding-right:12px;"><i class="fa fa-question-circle" style="font-size:23px;color:whitesmoke;cursor:help;" title="Help" onclick=showHelpPage()></i></div>');		        
			$('#menu').append('<div style="float:right;padding-top:12px;padding-right:20px;"><i class="fa fa-envelope" style="font-size:23px;color:whitesmoke;cursor:pointer;" title="Report Issue" onclick=displayReportIssue()></i></div>');	

		}
	  function showHelpPage(){
		  var activeUrl = window.location.href;
		  var activeJsp = activeUrl.split('/');
		  activeJsp = activeJsp[activeJsp.length - 1];
		  var mappingUrl = activeUrl.replace(activeJsp,'');
		  var mappingPage = activeJsp.replace(/\./g,'_').toLowerCase();
		  var completeURL = mappingUrl + "WebHelp/index.htm#" + mappingPage + ".htm";
		  		 
		 $.ajax({
			  
			    url:mappingUrl + "WebHelp/" + mappingPage + ".htm",
    			type:'HEAD',
    			error: function() {
					 $("#processFlow").show();
					 $("#processFlow").attr('href', mappingUrl + "WebHelp/index.htm");
					
					 callAlertHelp("The page you are referring for is not available right now. Please refer to the ");
					 
    			},
			    success: function() {
			    	window.open(completeURL,'_blank');//, "height=600,width=500");
			    }
			 
			});

	}	
	function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length,c.length);
        }
    }
    return "";
}


	
	 //---- drop down listing down on click event for automation testing requirment.	
	  $(document).on('click', "#menuList>li",function(){
			var menulistvalue=$(this).children().html();
			var indexvalue=menulistvalue.indexOf('<');
			if(indexvalue!=-1){
				var selectedmenu=menulistvalue.substring(0,indexvalue)
			}else{
				var selectedmenu=menulistvalue;
			}
			 var d = new Date();
			d.setTime(d.getTime() + (1000*60*60*24));
			var expires = "expires="+ d.toUTCString();
			document.cookie = "selectedmenu=" + selectedmenu + "; " + expires;	
			  $(this).addClass('open');
		  });
	  
	  $(document).on('mouseleave', "#menuList>li",function(){
			  $(this).removeClass('open');
		  });
		  
		// ----- PREPARING THE MENU LIST ON ROLE BASED ENDED -----	  
function setFormattedDate(date){	
	if(date == "" || date == null || typeof date == "undefined") return "";
	
	var dateArr = date.split('-');
	var dateNew = "";
	if(dateArr.length > 0)
		 dateNew = dateArr[1] + "/" + dateArr[2] + "/" + dateArr[0];	
	return dateNew;
}
function setJtableFormattedDate(date){	
	if(date == "" || date == null || typeof date == "undefined") return "";
	var dateArr = date.split('-');
	if(dateArr[0].length == 4){
		var setFormatted = setFormattedDate(date);
		return setFormatted;
	}
	var dateNew = "";
	if(dateArr.length > 0)
		 dateNew = dateArr[1] + "/" + dateArr[0] + "/" + dateArr[2];	
	return dateNew;
}
function callAlertHelp(alrtMsg){
	$("#alertModalHelp").text(alrtMsg);
	//$("#alertModal").text("");
	$("#alertModal").html("");
	$("#basicAlert").modal();
}		
function callAlert(alrtMsg){
	$("#processFlow").hide();
	//$("#alertModal").text(alrtMsg);
	if(typeof alertWidthFlag != 'undefined' && alertWidthFlag){
		$('#basicAlert > div').removeClass('modal-sm')
		alertWidthFlag = false;
	}else{
		$('#basicAlert > div').addClass('modal-sm');
	}
	$("#alertModal").html(alrtMsg);
	$("#alertModalHelp").text("");
	$("#basicAlert").modal();
}

function callConfirm(flag){	
	if(flag){
		$(".bootbox-confirm button").eq(1).removeClass("btn-default");
		$(".bootbox-confirm button").eq(1).addClass("grey-cascade");		
	
		$(".bootbox-confirm button").eq(2).removeClass("btn-primary");
		$(".bootbox-confirm button").eq(2).addClass("green-haze");
	}
}

function callConfirmSuccess(url){
	openLoaderIcon();
	 $.ajax({
		type: "POST",
		url: url,
		success : function(data) {
			closeLoaderIcon();
			if(data.Result=="ERROR"){
				callAlert(data.Message);
	    		return false;
	    	}else{
	    		callAlert(data.Message);
	    	}
		},
		error : function(data){
			closeLoaderIcon();
		},
		complete : function(data){
			closeLoaderIcon();
		}
	});
}

// ----- START  close child table if already opened (toggle functionality) -----
 function closeJtableTableChildContainer(imgContainer, parentTable){
	var flag=false;
	
	//var tr = $(this).parents("tr");
	//var parentTable = $('#jTableContainer');
    var tr = imgContainer.parents("tr");
    var isChildRowOpen = parentTable.jtable("isChildRowOpen", tr );

    if( isChildRowOpen ){
        $( parentTable.jtable("getChildRow", tr ) ).slideUp();
         flag = true;
    }	
	return flag;
} 
  // ----- END  close child table if already opened (toggle functionality) -----
  
  
  
  //Added For Report Issue Pop up

function displayReportIssue()
{
	$("#div_ReportIssuePopup").modal();
	
	$("#div_ReportIssuePopup .modal-header h2").text('Report Issue');
	
	$("#reportIssueInput").val('');

	
}

function cancel()
{
    $("#div_ReportIssuePopup").modal('hide');	

}

function sendReportIssueMail() {

		var message=$("#reportIssueInput").val();;
		var reportIssueType=$('#reportIssueType').val();
		
		$.get('rest/reportIssue/reportIssueMailPreparation?message='+message+'&reportIssueType='+reportIssueType,function(data) {	
			console.log("success "+data);

		});
		
	
		$("#div_ReportIssuePopup").modal('hide');

}

//Ended For Report Issue Pop up



</script>
<title></title>
</head>
<body>

<div id="menu"> 
</div>

<div class="modal fade" id="basicAlert" tabindex="-1" role="basic" aria-hidden="true" style="z-index:100080;">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
				<h4 class="modal-title">Alert</h4>
			</div>
			<div class="modal-body" >
				 <span id="alertModal"></span>
				 <span id="alertModalHelp"></span>
				 <a href="" id="processFlow" style="display:none" target="_blank">process workflow.</a>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn green-haze" data-dismiss="modal">Ok</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<input type="hidden" id="hdnProcessFlow" value=""/>
<script src="js/viewScript/hideMenuItem.js" type="text/javascript"></script>
</body>
</html>