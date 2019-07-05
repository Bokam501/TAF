<html lang="en" class="no-js">
<head>	
	<style type="text/css">
		#reviewFilter > .col-md-4 > .select2me{
			width: 150px !important;
		    margin-top: 5px;
		}
		#reviewFilter > .col-md-4 > .select2me > a{
			height: 28px;
		    font-size: 12px;
		    width: 150px;
		    padding-top: 0px;
		}
	</style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
	<body>
		<!-- BEGIN CONTAINER -->											
												
				<!-- Tabs -->
				 <div id="activitesSummaryPage">
					<ul class="nav nav-tabs toolbarFullScreen" id="tablistActivities">
						<li class="active"><a href="#ActivitySummary" data-toggle="tab">Activity Summary</a></li>
						<li><a href="#ActivityTasks" data-toggle="tab">Tasks</a></li>
						<!-- <li><a href="#activitySeries" data-toggle="tab">Series</a></li>	 -->							
						<li><a href="#ChangeRequests" data-toggle="tab">Change Requests</a></li>
						<li><a href="#Clarifications" data-toggle="tab">Clarifications</a></li>
						<li><a href="#attachmentsOfAct" data-toggle="tab">Attachments</a></li>
						<li><a href="#StatusPlan" data-toggle="tab">Workflow</a></li>
						<li><a href="#auditHistoryOfAct" data-toggle="tab">Audit History</a></li>
					</ul>
					
					<div class="tab-content" id="tbCntnt">
						<!-- Summary Tab -->
						<div id="ActivitySummary" class="tab-pane fade active in" style="overflow: hidden; height: 73%;">						
						</div>
						<!-- Summary Tab Ends -->
						
						<!-- Products -->
						<div id="ActivityTasks" class="tab-pane fade">		
							<div style="clear: both; padding-top: 10px;position: relative;">
								<div id="bulkselectionnew" style="display:none">
									<fieldset class="scheduler-border">
											<legend class="scheduler-border theme-font" style="width: 44px;">Search</legend>
											<div class="control-group">
											
									    <div id="filter" class="fliter-align" >
									 			<!-- Adding filter -->									
											<div class='col-md-12'>		
												<div class='col-md-2'>       											
													<label class="bulkAllocationTitle">Assignee</label>
												</div>
												<div class='col-md-2'>       											
													<label class="bulkAllocationTitle">Status</label>
												</div>											
												<div class='col-md-2'>      											
													<label class="bulkAllocationTitle">Priority</label>										
												</div>									
														
										   </div>
									
									    <div class='col-md-12'>							  						
											<div class='col-md-2'>       
													<div id="assignee_dd" class="">												
														<select class="form-control input-small  select2me" id="assigneeSearch_ul">
										        			<option class="assigneeList_ids" value="-">-</option> 			   			
														</select>
													</div>
												</div>
													<div class='col-md-2'>       
													<div id="status_dd" class="">												
														<select class="form-control input-small  select2me" id="statusSearch_ul">
										        			<option class="statusList_ids" value="-">-</option> 			   			
														</select>
													</div>
												</div>
										
												<div class='col-md-2'>     
													<div id="priorityList_dd" class="">
														<select class="form-control input-small  select2me" id="prioritySearch_ul">
										        			<option class="priorityList_ids" value="-">-</option>  			   			
														</select>
													</div>
												</div>
												<div class="col-md-2">        
											      <button type="button" id="search" class="btn green-haze" onclick="SearchDetail()" style="padding: 6px 7px;font-size: 12px;">Search</button>                                  		     
											     <button type="reset" id="reset" class="btn green-haze" onclick="ResetSearchDetail()" style="padding: 6px 7px;font-size: 12px;">Reset</button> 
												</div>						
											 </div>		
										   </div>
										</div>
									</fieldset>								   
		   						 </div>
	   						<div style="clear: both; padding-top: 10px;position:relative;">	 
	   						 <div class="col-md-8" style="position: absolute; z-index: 10;
						 		margin-left: 180px;padding-right: 0px;">
							 	<div class="col-md-8" id="divUploadActivityTaskContainer">
								 	<div class="col-md-2" style="padding-right: 10px;padding-top: 8px;margin-top: 2px;margin-right: 20px;width: 95px;display:none">
										<label style="color:  #FFF;font-family: sans-serif;font-size: 1.1em;">Upload : </label>
									</div>
								 	<div class="col-md-5" style="padding-left: 0px;margin-top: 0px;margin-left: -17px; display:none">													
	          			  					<input type="button" class="btn blue" id="trigUploadActivityTask" value="Activity Task" style="background-color: #55616f; font-size: 0.96em;font-family: sans-serif;margin-top: 8px;height: 26px;padding-top: 5px;">
	          			  					<span id="fileNameActivityTask"></span>
										<input id="uploadFileActivityTask" type="file" name="uploadFileActivityTask" style="display:none;" 
										onclick="{this.value = null;};" onchange="importActivityTask()">
										
										<img src="css/images/dt_download_features.png" title="Download Template - Activity Task" class="icon_imgSize showHandCursor" 
										alt="Download" onclick="downloadTemplateActivityTask();" style="margin-top: 11px;margin-left: 5px;">
									</div>
								</div>		
									
								<div class="col-md-2" id="filterIsActiveTask" style="margin-top: 0px;float:right;display:none">		
								  <div id="status_dd">
									<select class="form-control input-small select2me" id="isActiveTask_ul">
										<option value="2" selected ><a href="#">ALL</a></option>
										<option value="1" ><a href="#">Active</a></option>
										<option value="0" ><a href="#">Inactive</a></option>
									</select>
						          </div>
						         </div>					         					       
			            		</div>		   						  
								<div id="jTableContainerActivityTasks"></div>
							</div>
						</div>
						</div>
						<!-- Products Ends -->												
						
						<!-- Change Request Tab -->
						<div id="ChangeRequests" class="tab-pane fade">		
							<div style="clear: both; padding-top: 10px;position: relative;">
								<div id="jTableContainerforChangeRequests"></div>
							</div>
						</div>	
						<!-- Change Request Tab Ends -->
						
						<!-- Clarification Tab -->
						<div id="Clarifications" class="tab-pane fade">		
							<div style="clear: both; padding-top: 10px;position: relative;">
								<div id="jTableContainerforClarification"></div>
							</div>
						</div>
						<!-- Clarification Tab Ends -->
						<!-- Attachments Tab -->
						<div id="attachmentsOfAct" class="tab-pane fade">		
							<div style="clear: both; padding-top: 10px;position: relative;">
								<div id="jTableContainerforACTAttachments"></div>
							</div>
						</div>
						<!-- Attachments Tab Ends -->
						<!-- Workflow Tab -->
						<div id="StatusPlan" class="tab-pane fade">		
							<div style="clear: both; padding-top: 10px;position: relative;">
								<div id="jTableContainerforWorkflowPlanActivityOrTask"></div>
							</div>
						</div>
						<!-- Workflow Tab Ends -->
						<!-- Audit History Tab -->
						<div id="auditHistoryOfAct" class="tab-pane fade">		
							<div style="clear: both; padding-top: 10px;position: relative;">
								<div id="jTableContainerforACTAuditHistory"></div>
							</div>
						</div>
						<!-- Audit History Tab Ends -->
						<!-- Activity MultiSeries Tab -->
						<!-- <div id="activitySeries" class="tab-pane fade">
							<div id="activityMultiSeriesContainer">
							</div>							
						</div> -->
						<!-- Activity MultiSeries Tab Ends -->			
						
					</div>
				<!-- Tabs ended -->										
		</div>
										
		<!-- Popup -->
		<div id="div_PopupMain" class="modal " tabindex="-1" aria-hidden="true">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							title="Press Esc to close" onclick="popupClose()"
							aria-hidden="true"></button>
						<h4 class="modal-title"></h4>
					</div>
					<div class="modal-body">
						<div class="scroller" style="height: 320px" data-always-visible="1"
							data-rail-visible1="1">
							<div class="jScrollContainerResponsive"
								style="clear: both; padding-top: 10px">
								<div id="jtableSelectEnv"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div id="div_PopupBackground"></div>
		<!-- Popup Ends -->
			
<script type="text/javascript">
	var defaultuserId = '${userId}';
	var allowPlanDateEndDate = true;
	if('${roleName}' == "Tester"){	
		allowPlanDateEndDate = false;		
	}
</script>

<script type="text/javascript">
	var raisedby='';
	jQuery(document).ready(function() 
	{
		raisedby = $("#userdisplayname").text().split('[')[0].trim();
		$('#activitySummaryView').append($('#activitesSummaryPage'));		
		
		document.onkeydown = function(evt) {
			evt = evt || window.event;
			if (evt.keyCode == 27) {
				if (document.getElementById("div_PopupMain").style.display == 'block') {
					popupClose();
				}
			}
		};
	});
		
</script>
	
<script type="text/javascript" src="js/assignedActivityGrouping.js"></script>
<script type="text/javascript" src="js/Scripts/popup/iosOverlay.js"></script>

	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>