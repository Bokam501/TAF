package com.hcl.atf.taf.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.CommonDAO;
import com.hcl.atf.taf.integration.data.excel.ExcelTestDataIntegrator;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityMaster;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.DefectIdentificationStageMaster;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TaskEffortTemplate;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.json.JsonTaskEffortTemplate;
import com.hcl.atf.taf.model.json.JsonTimeSheetStatistics;
import com.hcl.atf.taf.model.json.JsonWorkPackageResultsStatistics;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.report.Report;
import com.hcl.atf.taf.service.ActivityTaskService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.ReportService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.atf.taf.util.BackupAndRestoreDbUtil;

@Controller
public class ReportController {

	private static final Log log = LogFactory
			.getLog(ReportController.class);

	@Autowired
	private Report report;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private ExcelTestDataIntegrator excelTestDataIntegrator;
	@Autowired
	private EnvironmentService environmentService;
	@Autowired
	private ActivityTaskService activityTaskService;
	@Autowired
	private MongoDBService mongoDbService;
	@Autowired
	private CommonDAO commonDAO;
	@Autowired
	private CommonService commonService;
	

	@RequestMapping(value="workpackage.testcaseplan.report", method=RequestMethod.POST ,produces="application/json")
	public  @ResponseBody JTableResponse workPackageTestCaseExecutionPlanDataExport(@RequestParam Integer workPackageId, ModelAndView modelAndView,HttpServletRequest request,HttpServletResponse response) {
		log.debug("test.export");
		JTableResponse jTableResponse;
		try {
			
			
			String exportLocation = request.getServletContext().getRealPath("/")+"report\\"+"export\\";
			WorkPackage workPackage = workPackageService.getWorkPackageById(new Integer(workPackageId).intValue());
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList=workPackageService.listWorkPackageTestCasesExecutionPlanByWorkPackageId(workPackageId);
			
			boolean isexportComplete = excelTestDataIntegrator.workPackageTestCaseExecutionPlanDataExport(workPackageTestCaseExecutionPlanList, workPackage,exportLocation);
			if(isexportComplete){
				
				jTableResponse = new JTableResponse("Ok","Export testCases Completed.",exportLocation);
			} else{
				
				jTableResponse = new JTableResponse("Ok","Export completed", exportLocation);
			}
			
		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("ERROR","Unable to Export TestCases");
		}
		return jTableResponse;
	}

	//Used for reports
	@RequestMapping(value="report.workpackage.testcase.plan.results.eod.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listWorkpackageTestCaseExecutionPlanResultsEod(@RequestParam Integer workPackageId,@RequestParam Integer shiftId,
    	@RequestParam String executionDateFilter, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {

		log.info("inside workpackage.testcase.plan.results.eod.list");
		log.info("executionDateFilter="+executionDateFilter);
		
		Date executionDate = null;
		
		if(executionDateFilter != null && !executionDateFilter.trim().isEmpty()) {
			executionDate = DateUtility.toConvertDate(executionDateFilter);
			log.info("executionDate="+executionDate);
		}
		
		JTableResponse jTableResponse = null;
		try {
			List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlanForTesterList=
					new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
			
			
			if(workPackageId == null || shiftId == null){					
				jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlanForTesterList, 0);
				return jTableResponse;
			}
			
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList=
					reportService.listWorkPackageTestCasesExecutionPlanResultsEod(workPackageId,
					shiftId, executionDate, jtStartIndex, jtPageSize);
			if (workPackageTestCaseExecutionPlanList == null || workPackageTestCaseExecutionPlanList.isEmpty()) {
				log.info("No workpackage testcase execution plan results available for the requested filters: ");
				log.info("workPackageId="+workPackageId+" shiftId="+shiftId+"executionDate="+executionDate);
				jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlanForTesterList, 0);
			} else {
				for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan: workPackageTestCaseExecutionPlanList){
					String filter;
					jsonWorkPackageTestCaseExecutionPlanForTesterList.add(new JsonWorkPackageTestCaseExecutionPlanForTester(workPackageTestCaseExecutionPlan, "EOD"));
				}
		        
				jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlanForTesterList, 
						jsonWorkPackageTestCaseExecutionPlanForTesterList.size());
				workPackageTestCaseExecutionPlanList = null;
			}
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);	            
        }
        return jTableResponse;
    }	

	//Used for reports
	@RequestMapping(value="report.workpackage.statistics.eod.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listWorkPackageStatisticsEOD(@RequestParam Integer workPackageId,@RequestParam Integer shiftId,
    	@RequestParam String executionDateFilter) {
		//) {

		log.info("inside workpackage.statistics.eod.list");
		
		Date executionDate = null;
		
		if(executionDateFilter != null && !executionDateFilter.trim().isEmpty()) {
			executionDate = DateUtility.toConvertDate(executionDateFilter);
			log.info("executionDate="+executionDate);
		}
		
		JTableResponse jTableResponse = null;
		try {
			List<JsonWorkPackageResultsStatistics> jsonWorkPackageResultsStatisticsList =  new ArrayList<JsonWorkPackageResultsStatistics>();
			
			if(workPackageId == null || shiftId == null){					
				jTableResponse = new JTableResponse("OK", jsonWorkPackageResultsStatisticsList, 0);
				return jTableResponse;
			}
			jsonWorkPackageResultsStatisticsList = reportService.listWorkPackageStatisticsEOD(workPackageId,
					shiftId, executionDate);
			
			if (jsonWorkPackageResultsStatisticsList == null || jsonWorkPackageResultsStatisticsList.isEmpty()) {
				log.info("No statistics present for requested filters: ");
				log.info("workPackageId="+workPackageId+" shiftId="+shiftId+"executionDate="+executionDate);
				jTableResponse = new JTableResponse("OK", jsonWorkPackageResultsStatisticsList, 0);
			} else {
		        
				jTableResponse = new JTableResponse("OK", jsonWorkPackageResultsStatisticsList, jsonWorkPackageResultsStatisticsList.size());
			}
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);	            
        }
        return jTableResponse;
    }	
	
	//Used for reports
	@RequestMapping(value="report.timesheet.statistics.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTimeSheetStatistics(@RequestParam String executionDateFilterFrom, @RequestParam String executionDateFilterTo) {

		log.info("inside report.timesheet.statistics.list");
		log.info("executionDateFilter="+executionDateFilterFrom);
		log.info("executionDateFilter="+executionDateFilterTo);
		
		JTableResponse jTableResponse = null;
		List<JsonTimeSheetStatistics> jsonTimeSheetStatisticsList =  new ArrayList<JsonTimeSheetStatistics>();
		
		Date executionDateFrom = null;
		
		if(executionDateFilterFrom != null && !executionDateFilterFrom.trim().isEmpty()) {
			executionDateFrom = DateUtility.dateformatWithSlash(executionDateFilterFrom);
			log.info("executionDate="+executionDateFrom);
		}
		
		Date executionDateTo = null;
		if(executionDateFilterTo != null && !executionDateFilterTo.trim().isEmpty()) {
			executionDateTo = DateUtility.dateformatWithSlash(executionDateFilterTo);
			log.info("executionDateTo="+executionDateTo);
		}else {
			executionDateTo = executionDateFrom;	
		}	
		
		try {
			
			jsonTimeSheetStatisticsList = reportService.listTimeSheetStatistics(executionDateFrom, executionDateTo);
			
			
			if (jsonTimeSheetStatisticsList == null || jsonTimeSheetStatisticsList.isEmpty()) {
				log.info("No statistics present for requested filters: ");
				log.info("executionDateFrom="+executionDateFrom+" executionDateTo="+executionDateTo);
				jTableResponse = new JTableResponse("OK", jsonTimeSheetStatisticsList, 0);
			} else {
				log.info("Success report generation");
				jTableResponse = new JTableResponse("OK", jsonTimeSheetStatisticsList, jsonTimeSheetStatisticsList.size());
			}
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);	            
        }
        return jTableResponse;
    }

	
	@RequestMapping(value="report.activity.status.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String getActivityStatus(@RequestParam Integer productId,@RequestParam String fromDate, @RequestParam String toDate) {			
		String finalResult="";
		try {
			log.info("productId*****"+productId);
			List<Activity> activityReportList =  new ArrayList<Activity>();
			Date dataFromDate = null;
			
			if(fromDate != null && !fromDate.trim().isEmpty()) {
				dataFromDate = DateUtility.toConvertDate(fromDate);
				log.info("dataFromDate="+dataFromDate);
			}
			
			Date dataToDate = null;
			if(toDate != null && !toDate.trim().isEmpty()) {
				dataToDate = DateUtility.toConvertDate(toDate);
				log.info("dataToDate="+dataToDate);
			}else {
				dataToDate = dataFromDate;	
			}
			JSONObject finalObj = new JSONObject();
			JSONObject productTitle = new JSONObject();
			JSONObject srsTitle = new JSONObject();
			JSONObject activityMasterTypeTitle = new JSONObject();
			JSONObject buildTitle = new JSONObject();
			JSONObject buildTypeTitle = new JSONObject();
			JSONObject productVersionTitle = new JSONObject();
			JSONObject drTitle = new JSONObject();
			JSONObject rcnTitle = new JSONObject();
			JSONObject tprcTitle = new JSONObject();
	
			JSONObject folderDeliveryStatusTitle = new JSONObject();
			JSONObject remarksTitle = new JSONObject();
			JSONObject clarificationTitle = new JSONObject();
			JSONObject statusTrackTitle = new JSONObject();
			JSONObject statusTitle = new JSONObject();
			
			
			
			 String productName = "";
			 String productFeatureName = "";
			 String productVersionName = "";
			 String productBuildName = "";
			 String buildTypeName = "";
			 String activityMasterName = "";
			 String drName = "NA";
			 String tprcNumber = "";
			 String changeRequestName = "NA";
			
			 String priorityName = "";	
			 String baselineStartDate = "";
			 String baselineEndDate = "";
			 String plannedStartDate = "";
			 String plannedEndDate = "";
			 String actualStartDate = "";
			 String actualEndDate = "";
			 String folderStatus="";
			 String remarks="";
			 String clarificationRef="";
			 String forStatusTrack="";
			 String status="";
						 
			
			JSONObject ecTitle = null;
			JSONArray list = new JSONArray();
			JSONArray columnData = new JSONArray();
			JSONArray columnData1 = new JSONArray();
		
			
		     	productTitle.put("title", "Product");
				list.add(productTitle);
				srsTitle.put("title", "Requirement");
				list.add(srsTitle);
				activityMasterTypeTitle.put("title", "Type");
				list.add(activityMasterTypeTitle);
				buildTypeTitle.put("title", "Build Type");
				list.add(buildTypeTitle);
				productVersionTitle.put("title", "Interim Build");
				list.add(productVersionTitle);
				buildTitle.put("title", "PY Build");
				list.add(buildTitle);
				statusTitle.put("title", "Status w.r.t?");
				list.add(statusTitle);
				
				drTitle.put("title", "Clarification");
				list.add(drTitle);
				rcnTitle.put("title", "Change Request");
				list.add(rcnTitle);
				tprcTitle.put("title", "Tracker Number");
				list.add(tprcTitle);
								
				List<EnvironmentCombination> listofEnvironmentCombinations = environmentService.listEnvironmentCombination(productId);
				
				for(EnvironmentCombination ec : listofEnvironmentCombinations){
						ecTitle = new JSONObject();
						ecTitle.put("title", ec.getEnvironmentCombinationName());
						list.add(ecTitle);
				}
				
				
				folderDeliveryStatusTitle.put("title", "Folder Delivery Status");
				list.add(folderDeliveryStatusTitle);
				
				remarksTitle.put("title", "Remarks");
				list.add(remarksTitle);
				
				clarificationTitle.put("title", "Clarification Reference If Any");
				list.add(clarificationTitle);
				
				statusTrackTitle.put("title", "For Status Tracking");
				list.add(statusTrackTitle);
				

				finalObj.put("COLUMNS", list);
				
				activityReportList = reportService.getActivitiesByDate(dataFromDate, dataToDate,productId);
				if(activityReportList.size()!=0){
					for(Activity activity : activityReportList){
						columnData = new JSONArray();
						ActivityMaster activityMaster = activity.getActivityMaster();

						tprcNumber = String.valueOf(activity.getActivityTrackerNumber() != null? activity.getActivityTrackerNumber():0);
						ClarificationTracker dr = activity.getClarificationTracker();
						if(dr != null){
							drName = dr.getClarificationTitle();
						}
						ProductFeature productFeature = activity.getProductFeature();
						ActivityWorkPackage activityWorkPackage = activity.getActivityWorkPackage();
						ProductBuild productBuild = activityWorkPackage.getProductBuild();
						DefectIdentificationStageMaster buildType = productBuild.getBuildType();
						ProductVersionListMaster pVersion = productBuild.getProductVersion();
						ProductMaster productMaster = pVersion.getProductMaster();
						productName = productMaster.getProductName();
						if(productFeature!=null)
						productFeatureName = productFeature.getProductFeatureName();
						if(activityMaster != null) {
							activityMasterName = activityMaster.getActivityMasterName();
						}
						productBuildName = productBuild.getBuildname();
						buildTypeName = buildType.getStageName();
						productVersionName = pVersion.getProductVersionName();
						if(activity.getPlannedStartDate() != null){
							plannedStartDate = DateUtility.dateToStringInSecond(activity.getPlannedStartDate());
						}
						
						if(activity.getPlannedEndDate() != null){
							plannedEndDate = DateUtility.dateToStringInSecond(activity.getPlannedEndDate());
						}
						
						if(activity.getBaselineStartDate() != null){
							baselineStartDate = DateUtility.dateToStringInSecond(activity.getBaselineStartDate());
						}
						
						if(activity.getBaselineEndDate() != null){
							baselineEndDate = DateUtility.dateToStringInSecond(activity.getBaselineEndDate());
						}
						
						if(activity.getActualStartDate() != null){
							actualStartDate = DateUtility.dateToStringInSecond(activity.getActualStartDate());
						}
						
						if(activity.getActualEndDate() != null){
							actualEndDate = DateUtility.dateToStringInSecond(activity.getActualEndDate());
						}
						
						if(activity.getPriority()!=null)
						priorityName = activity.getPriority().getDisplayName();
						columnData.add(productName);
						columnData.add(productFeatureName);
						columnData.add(activityMasterName);
						columnData.add(buildTypeName);
						columnData.add(productVersionName);
						columnData.add(productBuildName);
						columnData.add(status);
						columnData.add(drName);
						columnData.add(tprcNumber);
						
						List<ActivityTask> activityTaskslist = activityTaskService.getActivityTaskById(activity.getActivityId());
						
						if(activityTaskslist != null && activityTaskslist.size() >0){
							for(int i=0;i<activityTaskslist.size();i++){
								ActivityTask activityTask=activityTaskslist.get(i);
								if((listofEnvironmentCombinations!=null && listofEnvironmentCombinations.size() >0) && (activityTask !=null && activityTask.getEnvironmentCombination() != null)){
									int combinationCount=listofEnvironmentCombinations.size();
									if(i<combinationCount){
										if(listofEnvironmentCombinations.get(i).getEnvironment_combination_id() == activityTask.getEnvironmentCombination().getEnvironment_combination_id()){
											columnData.add(activityTask.getStatus().getWorkflowStatusDisplayName());
										}else{
											columnData.add("");
										}
									}else{
										columnData.add("");
									}
								}else{
									columnData.add("");
								}
							}
						 }else{
							 columnData.add("");
						 }
						
						columnData.add("");
						columnData.add("");
						columnData.add("");
						columnData.add("");
						columnData1.add(columnData);
					}
				}
				finalObj.put("DATA", columnData1);				
				finalResult=finalObj.toString();
				log.info("finalResult : "+finalResult);
			} catch (Exception e) {
	            log.error("JSON ERROR", e);
	        }		        
	    return "["+finalResult+"]";
	}
	
	
	@RequestMapping(value="report.activity.status",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String getActivityStatusList(@RequestParam Integer productId,@RequestParam String fromDate, @RequestParam String toDate) {			
		String finalResult="";
		try {
			log.info("Fetching ActivityStatus List");
			List<Activity> activityReportList =  new ArrayList<Activity>();
			Date dataFromDate = null;
			
			if(fromDate != null && !fromDate.trim().isEmpty()) {
				dataFromDate = DateUtility.dateformatWithOutTime(fromDate);
				log.debug("dataFromDate="+dataFromDate);
			}
			
			Date dataToDate = null;
			if(toDate != null && !toDate.trim().isEmpty()) {
				dataToDate = DateUtility.dateformatWithOutTime(toDate);
				log.debug("dataToDate="+dataToDate);
			}else {
				dataToDate = dataFromDate;	
			}
			JSONObject finalObj = new JSONObject();
			JSONObject productTitle = new JSONObject();
			JSONObject activityWorkpackageNameTitle = new JSONObject();
			JSONObject activityNameTitle = new JSONObject();
			JSONObject activityTypeTitle = new JSONObject();
			JSONObject currentStatusTitle = new JSONObject();
			JSONObject plannedStartDateTitle = new JSONObject();
			JSONObject plannedEndDateTitle = new JSONObject();
			JSONObject actualStartDateTitle = new JSONObject();
			JSONObject actualEndDateTitle = new JSONObject();
			JSONObject totalEffortTitle = new JSONObject();
			JSONObject plannedSizeTitle = new JSONObject();
			JSONObject actualSizeTitle = new JSONObject();
			JSONObject activityWeightageTitle = new JSONObject();
			JSONObject activityAssigneeTitle = new JSONObject();
			JSONObject activityRivewerTitle = new JSONObject();
			JSONObject percentageOfCompleteTitle = new JSONObject();
			JSONObject plannedUnitTitle = new JSONObject();
			JSONObject actualUnitTitle = new JSONObject();
			
			JSONArray list = new JSONArray();
			JSONArray columnData = new JSONArray();
			JSONArray columnData1 = new JSONArray();
			
		     	productTitle.put("title", "Product");
				list.add(productTitle);
				activityWorkpackageNameTitle.put("title", "Workpackage");
				list.add(activityWorkpackageNameTitle);
				
				activityNameTitle.put("title", "Activity");
				list.add(activityNameTitle);
				
				
				activityTypeTitle.put("title", "Activity Type");
				list.add(activityTypeTitle);
				
				currentStatusTitle.put("title", "Current Status");
				list.add(currentStatusTitle);
				
				plannedStartDateTitle.put("title", "Planned Start Date");
				list.add(plannedStartDateTitle);
				
				plannedEndDateTitle.put("title", "Planned End Date");
				list.add(plannedEndDateTitle);
				
				actualStartDateTitle.put("title", "Actual Start Date");
				list.add(actualStartDateTitle);
				
				actualEndDateTitle.put("title", "Actual End Date");
				list.add(actualEndDateTitle);
				
				totalEffortTitle.put("title", "Total Effort");
				list.add(totalEffortTitle);
				
				percentageOfCompleteTitle.put("title", "% Completed");
				list.add(percentageOfCompleteTitle);
				
				plannedSizeTitle.put("title", "Planned Size");
				list.add(plannedSizeTitle);
				
				actualSizeTitle.put("title", "Actual Size");
				list.add(actualSizeTitle);
				
				activityWeightageTitle.put("title", "Weightage");
				list.add(activityWeightageTitle);
				
				activityAssigneeTitle.put("title", "Assignee");
				list.add(activityAssigneeTitle);
				
				activityRivewerTitle.put("title", "Reivewer");
				list.add(activityRivewerTitle);
				
				plannedUnitTitle.put("title", "Planned Unit");
				list.add(plannedUnitTitle);
				
				actualUnitTitle.put("title", "Actual Unit");
				list.add(actualUnitTitle);

				finalObj.put("COLUMNS", list);
				
				activityReportList = reportService.getActivitiesByDate(dataFromDate, dataToDate,productId);
				
				
				if(activityReportList.size()!=0){
					for(Activity activity : activityReportList){
						
						 String productName = "";
						 String activityWorkpackageName = "";
						 String activityName = "";
						 String activityType = "";
						 String currentStatus = "";
						 String baselineStartDate = "";
						 String baselineEndDate = "";
						 String plannedStartDate = "";
						 String plannedEndDate = "";
						 String actualStartDate = "";
						 String actualEndDate = "";
						 Integer totalEffort = 0;
						 Float percentageOfComplete = 0.0F;
						 Integer baselineSize = 0;
						 Integer plannedSize = 0;
						 Integer actualSize = 0;
						 float activityWeightage = 0;
						 String activityAssignee = "";
						 String activityReviewer = "";
						 float plannedUnit = 0;
						 float actualUnit = 0;
						 
						columnData = new JSONArray();
						if(activity!=null){
							activityName = activity.getActivityName();
							totalEffort = activity.getTotalEffort();
							plannedSize = activity.getPlannedActivitySize();
							baselineSize = activity.getBaselineActivitySize();
							actualSize = activity.getActualActivitySize();
							percentageOfComplete=activity.getPercentageCompletion();
							
							if(activity.getPlannedStartDate() != null){
								plannedStartDate = DateUtility.dateToStringInSecond(activity.getPlannedStartDate());
							}					
							if(activity.getPlannedEndDate() != null){
								plannedEndDate = DateUtility.dateToStringInSecond(activity.getPlannedEndDate());
							}	
							if(activity.getBaselineStartDate() != null){
								baselineStartDate = DateUtility.dateToStringInSecond(activity.getBaselineStartDate());
							}					
							if(activity.getBaselineEndDate() != null){
								baselineEndDate = DateUtility.dateToStringInSecond(activity.getBaselineEndDate());
							}	
							if(activity.getActualStartDate() != null){
								actualStartDate = DateUtility.dateToStringInSecond(activity.getActualStartDate());
							}							
							if(activity.getActualEndDate() != null){
								actualEndDate = DateUtility.dateToStringInSecond(activity.getActualEndDate());
							}							
							if(activity.getAssignee()!=null){
								activityAssignee = activity.getAssignee().getLoginId();
							}if(activity.getReviewer()!=null){
								activityReviewer = activity.getReviewer().getLoginId();
							}							
							if(activity.getActivityMaster()!=null){
								activityType = activity.getActivityMaster().getActivityMasterName();
								if(activity.getActivityMaster().getWeightage()!=null){
									activityWeightage = activity.getActivityMaster().getWeightage();
								}
							}							
							if(activity.getWorkflowStatus()!=null){
								currentStatus = activity.getWorkflowStatus().getWorkflowStatusName();
							}							
							if(activity.getActivityWorkPackage()!=null){
								activityWorkpackageName = activity.getActivityWorkPackage().getActivityWorkPackageName();
								if(activity.getActivityWorkPackage().getProductBuild()!=null){
									if(activity.getActivityWorkPackage().getProductBuild().getProductVersion()!=null){
										if(activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
											productName = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
										}
									}
								}
							}
						}
						
						if(plannedSize!=null ){
							plannedUnit = activityWeightage*plannedSize;
						}
						if(percentageOfComplete!= null){
							actualUnit = plannedUnit*percentageOfComplete;
						}
						
						columnData.add(productName);
						columnData.add(activityWorkpackageName);
						columnData.add(activityName);
						columnData.add(activityType);
						columnData.add(currentStatus);
						columnData.add(plannedStartDate);
						columnData.add(plannedEndDate);
						columnData.add(actualStartDate);
						columnData.add(actualEndDate);
						columnData.add(totalEffort);
						columnData.add(percentageOfComplete);
						columnData.add(plannedSize);
						columnData.add(actualSize);
						columnData.add(activityWeightage);
						columnData.add(activityAssignee);
						columnData.add(activityReviewer);
						columnData.add(plannedUnit);
						columnData.add(actualUnit);
						
						columnData1.add(columnData);
					}
				}
				finalObj.put("DATA", columnData1);				
				finalResult=finalObj.toString();
			} catch (Exception e) {
	            log.error("JSON ERROR", e);
	        }		        
	    return "["+finalResult+"]";
	}
	
	
	@RequestMapping(value="report.activity.task.effort",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String getActivityTaskEffortReport(@RequestParam Integer productId) {			
		String finalResult="";
		try {
			log.info("report.activity.task.effort productId*****"+productId);
			List<Object[]> activityTaskEffortReportList =  new ArrayList<Object[]>();
			JSONArray list = new JSONArray();
			JSONArray columnData = new JSONArray();
			JSONArray columnData1 = new JSONArray();
			JSONObject finalObj = new JSONObject();
			JSONObject productTitle = new JSONObject();
			JSONObject activityWorkpackageNameTitle = new JSONObject();
			JSONObject activityNameTitle = new JSONObject();
			JSONObject assigneeTitle = new JSONObject();
			JSONObject activityTaskNameTitle = new JSONObject();
			JSONObject actualEffortTitle = new JSONObject();		
			JSONObject taskTypeTitle = new JSONObject();
			JSONObject priorityTitle = new JSONObject();
			JSONObject plannedStartDateTitle = new JSONObject();
			JSONObject plannedEndDateTitle = new JSONObject();
			JSONObject remarksTitle = new JSONObject();
			JSONObject createdDateTitle = new JSONObject();
			
			productTitle.put("title", "Product");
			list.add(productTitle);
			activityWorkpackageNameTitle.put("title", "Workpackage");
			list.add(activityWorkpackageNameTitle);
			activityNameTitle.put("title", "Activity");
			list.add(activityNameTitle);			
			activityTaskNameTitle.put("title", "Task");
			list.add(activityTaskNameTitle);
			taskTypeTitle.put("title", "Task Type");
			list.add(taskTypeTitle);
			assigneeTitle.put("title", "Resource");
			list.add(assigneeTitle);
			actualEffortTitle.put("title", "Actual Efforts Spent");
			list.add(actualEffortTitle);
									
			priorityTitle.put("title", "Priority");
			list.add(priorityTitle);
			plannedStartDateTitle.put("title", "Planned Start Date");
			list.add(plannedStartDateTitle);			
			plannedEndDateTitle.put("title", "Planned End Date");
			list.add(plannedEndDateTitle);			
			createdDateTitle.put("title", "Created Date");
			list.add(createdDateTitle);		
			remarksTitle.put("title", "Remarks");
			list.add(remarksTitle);
			
			finalObj.put("COLUMNS", list);
			
			activityTaskEffortReportList = reportService.getActivityTaskEffortReport(productId);
			if(activityTaskEffortReportList.size()!=0){
				for (Object[] objects : activityTaskEffortReportList) {
					columnData = new JSONArray();
					
					columnData.add((String)objects[0]);//Product Name - 0
					columnData.add((String)objects[1]);//activityWorkPackageName - 1 
					columnData.add((String)objects[2]);//activityName - 2
					columnData.add((String)objects[4]);//activityTaskName - 4
					columnData.add((String)objects[5]);//activityTaskTypeName - 5
					columnData.add((String)objects[7]); // loginId - 7
					if(objects[6]!=null)
						columnData.add(objects[6].toString()); //actualEffort - 6
					
					columnData.add((String)objects[8]);//executionPriority - 8
					columnData.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objects[9])); //plannedStartDate - 9
					columnData.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objects[10]));//plannedEndDate - 10
					columnData.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objects[11]));//createdDate - 11
					columnData.add((String)objects[12]);//remark -12
					
					columnData1.add(columnData);
				}
			}
			finalObj.put("DATA", columnData1);				
			finalResult=finalObj.toString();
		} catch (Exception e) {
            log.error("JSON ERROR", e);
        }		        
	    return "["+finalResult+"]";
	}
	
	
	@RequestMapping(value="report.resource.effort",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String getResourceEffortReport(@RequestParam Integer productId) {			
		String finalResult="";
		try {
			log.info("report.resource.effort productId*****"+productId);
			List<Object[]> activityTaskEffortReportList =  new ArrayList<Object[]>();
			JSONArray list = new JSONArray();
			JSONArray columnData = new JSONArray();
			JSONArray columnData1 = new JSONArray();
			JSONObject finalObj = new JSONObject();
			JSONObject resourceTitle = new JSONObject();
			JSONObject productTitle = new JSONObject();
			JSONObject activityWorkpackageNameTitle = new JSONObject();
			JSONObject activityNameTitle = new JSONObject();
			JSONObject activityTaskNameTitle = new JSONObject();
			JSONObject taskTypeTitle = new JSONObject();
			JSONObject currentStatusTitle = new JSONObject();
			JSONObject actualEffortTitle = new JSONObject();		
			JSONObject priorityTitle = new JSONObject();
			JSONObject executionDateTitle = new JSONObject();
			JSONObject plannedStartDateTitle = new JSONObject();
			JSONObject plannedEndDateTitle = new JSONObject();
			JSONObject remarksTitle = new JSONObject();
			JSONObject slaDurationPlannedTitle = new JSONObject();
			JSONObject slaDurationActutalTitle = new JSONObject();
			
			resourceTitle.put("title", "Resource");
			list.add(resourceTitle);
			productTitle.put("title", "Product");
			list.add(productTitle);
			activityWorkpackageNameTitle.put("title", "Workpackage");
			list.add(activityWorkpackageNameTitle);
			activityNameTitle.put("title", "Activity");
			list.add(activityNameTitle);			
			activityTaskNameTitle.put("title", "Task");
			list.add(activityTaskNameTitle);
			taskTypeTitle.put("title", "Task Type");
			list.add(taskTypeTitle);
			
			currentStatusTitle.put("title", "Current Status");
			list.add(currentStatusTitle);
			
			actualEffortTitle.put("title", "Actual Efforts Spent");
			list.add(actualEffortTitle);
									
			priorityTitle.put("title", "Priority");
			list.add(priorityTitle);
			executionDateTitle.put("title", "Execution Date");
			list.add(executionDateTitle);
			
			plannedStartDateTitle.put("title", "Planned Start Date");
			list.add(plannedStartDateTitle);			
			plannedEndDateTitle.put("title", "Planned End Date");
			list.add(plannedEndDateTitle);			
			remarksTitle.put("title", "Remarks");
			list.add(remarksTitle);
			slaDurationPlannedTitle.put("title", "SLA Duration Planned");
			list.add(slaDurationPlannedTitle);
			slaDurationActutalTitle.put("title", "SLA Duration Actual");
			list.add(slaDurationActutalTitle);
			finalObj.put("COLUMNS", list);
			activityTaskEffortReportList = reportService.getResourceEffortReport(productId);
			if(activityTaskEffortReportList.size()!=0){
				for (Object[] objects : activityTaskEffortReportList) {
					columnData = new JSONArray();
					
					columnData.add((String)objects[0]); // User - 0
					columnData.add((String)objects[1]);//Product Name - 1
					columnData.add((String)objects[2]);//activityWorkPackageName - 2 
					columnData.add((String)objects[3]);//activityName - 3
					columnData.add((String)objects[4]);//activityTaskName - 4
					columnData.add((String)objects[5]);//activityTaskTypeName - 5
					columnData.add((String)objects[6]);//workflowStatus - 6
					if(objects[7]!=null)
						columnData.add(objects[7].toString()); //actualEffort - 7
					
					columnData.add((String)objects[8]);//executionPriority - 8
					columnData.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objects[9])); //Execution Date - 9
					columnData.add(new SimpleDateFormat("yyyy-MM-dd").format(objects[10])); //plannedStartDate - 10
					columnData.add(new SimpleDateFormat("yyyy-MM-dd").format(objects[11]));//plannedEndDate - 11
					columnData.add((String)objects[12]);//remark -12
					columnData.add((Integer)objects[13]);//slaDurationPlanned -13
					columnData.add((Integer)objects[14]);//slaDurationActual -14
					columnData.add((Integer)objects[14]);//entityInstanceId -14
					
					columnData1.add(columnData);
				}
			}
			finalObj.put("DATA", columnData1);				
			finalResult=finalObj.toString();
		} catch (Exception e) {
            log.error("JSON ERROR", e);
        }		        
	    return "["+finalResult+"]";
	}
	@RequestMapping(value="report.activity.task.effort.filters",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String getActivityTaskEffortReportWithFilter(@RequestParam Integer templateId) {			
		String finalResult="";
		String productId="";
		String templateName="";
		String mappedFields="";
		Date fromDate;
		Date toDate;
		String statusValue="";
		String resourceValue="";
		try {
			log.info("report.activity.task.effort productId*****"+productId);
			TaskEffortTemplate taskEffortTemplate=null;
			List<Object[]> activityTaskEffortReportList =  new ArrayList<Object[]>();
			JSONArray list = new JSONArray();
			JSONArray columnData = new JSONArray();
			JSONArray columnData1 = new JSONArray();
			JSONObject finalObj = new JSONObject();
			
			JSONObject productTitle = new JSONObject();
			JSONObject activityWorkpackageNameTitle = new JSONObject();
			JSONObject activityNameTitle = new JSONObject();
			JSONObject activityTaskNameTitle = new JSONObject();
			JSONObject resourceTitle = new JSONObject();
			JSONObject statusTitle = new JSONObject();			
			JSONObject actualEffortTitle = new JSONObject();		
			JSONObject actualStartDateTitle = new JSONObject();
			JSONObject actualEndDateTitle = new JSONObject();
			JSONObject plannedStartDateTitle = new JSONObject();
			JSONObject plannedEndDateTitle = new JSONObject();
			JSONObject commentsTitle = new JSONObject();
			
			try{
				
				taskEffortTemplate=reportService.getTaskEffortTemplateById(templateId);
				if(taskEffortTemplate!=null)
				{
					productId=taskEffortTemplate.getProductValue();
					templateName=taskEffortTemplate.getTemplateName();					
					mappedFields=taskEffortTemplate.getMappedFields();
					fromDate=taskEffortTemplate.getFromDateValue();
					toDate=taskEffortTemplate.getToDateValue();
					statusValue=taskEffortTemplate.getStatusValue();
					resourceValue=taskEffortTemplate.getResourceValue();
					StringTokenizer mappedFieldsStr=new StringTokenizer(mappedFields,",");
					String val="";
					List<String> mappedFieldList=new ArrayList<String>();
					while(mappedFieldsStr.hasMoreTokens()){
						val="";
						val=mappedFieldsStr.nextToken();
						mappedFieldList.add(val);
						if(val!=null && val.equals("Product")){			
							productTitle.put("title", "Product");
							list.add(productTitle);
						}else if(val!=null && val.equals("Workpackage")){		
							activityWorkpackageNameTitle.put("title", "Workpackage");
							list.add(activityWorkpackageNameTitle);
						}else if(val!=null && val.equals("Activity")){	
							activityNameTitle.put("title", "Activity");
							list.add(activityNameTitle);		
						}else if(val!=null && val.equals("Task")){						
							activityTaskNameTitle.put("title", "Task");
							list.add(activityTaskNameTitle);
						}else if(val!=null && val.equals("Resource")){					
							resourceTitle.put("title", "Resource");
							list.add(resourceTitle);
						}else if(val!=null && val.equals("Status")){	
							statusTitle.put("title", "Status");
							list.add(statusTitle);	
						}else if(val!=null && val.equals("Effort")){	
							actualEffortTitle.put("title", "Effort in Hr(s)");
							list.add(actualEffortTitle);
						}else if(val!=null && val.equals("Actual Start Date")){	
							actualStartDateTitle.put("title", "Actual Start Date");
							list.add(actualStartDateTitle);		
						}else if(val!=null && val.equals("Actual End Date")){	
							actualEndDateTitle.put("title", "Actual End Date");
							list.add(actualEndDateTitle);
						}else if(val!=null && val.equals("Planned Start Date")){	
							plannedStartDateTitle.put("title", "Planned Start Date");
							list.add(plannedStartDateTitle);		
						}else if(val!=null && val.equals("Planned End Date")){	
							plannedEndDateTitle.put("title", "Planned End Date");
							list.add(plannedEndDateTitle);		
						}else if(val!=null && val.equals("Comments")){	
							commentsTitle.put("title", "Comments");
							list.add(commentsTitle);
						}
					}
					finalObj.put("COLUMNS", list);
					activityTaskEffortReportList = reportService.getActivityTaskEffortReportBasedOnFilter(productId,statusValue, fromDate, toDate,resourceValue);
					if(activityTaskEffortReportList.size()!=0){
						String tmpVar="";
						for (Object[] objects : activityTaskEffortReportList) {
							columnData = new JSONArray();
							
							if(mappedFieldList!=null && mappedFieldList.contains("Product"))
								columnData.add((String)objects[0]);//Product Name - 0
							if(mappedFieldList!=null && mappedFieldList.contains("Workpackage"))
							columnData.add((String)objects[1]);//activityWorkPackageName - 1
							if(mappedFieldList!=null && mappedFieldList.contains("Activity"))
							columnData.add((String)objects[2]);//activityName - 2
							if(mappedFieldList!=null && mappedFieldList.contains("Task"))
							columnData.add((String)objects[3]);//activityTaskName - 3
							if(mappedFieldList!=null && mappedFieldList.contains("Resource"))
							columnData.add((String)objects[4]);//Resource name - 4
							if(mappedFieldList!=null && mappedFieldList.contains("Status"))
							columnData.add((String)objects[5]); // status - 5
							if(mappedFieldList!=null && mappedFieldList.contains("Effort")){
								if(objects[6]!=null)
									columnData.add(objects[6].toString()); //actualEffort - 6	
								else
									columnData.add(0);
							}
							if(mappedFieldList!=null && mappedFieldList.contains("Actual Start Date"))
							columnData.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objects[7]));//actualStartDate - 7
							if(mappedFieldList!=null && mappedFieldList.contains("Actual End Date"))
							columnData.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objects[8]));//actualEndDate - 8
							if(mappedFieldList!=null && mappedFieldList.contains("Planned Start Date"))
							columnData.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objects[9])); //plannedStartDate - 9
							if(mappedFieldList!=null && mappedFieldList.contains("Planned End Date"))
							columnData.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objects[10]));//plannedEndDate - 10
							if(mappedFieldList!=null && mappedFieldList.contains("Comments")){
								tmpVar="";
								tmpVar=objects[11].toString();//comments -11
								if(tmpVar!=null && !tmpVar.equals("")){
									if(tmpVar.length()>15)
										columnData.add(tmpVar.substring(0, 15)+" ...");
									else
										columnData.add(tmpVar);
										
								}else{
									columnData.add("");
								}
							}
							
							columnData1.add(columnData);
						}
					}
					finalObj.put("DATA", columnData1);				
					finalResult=finalObj.toString();
				}
				else{
						
					productTitle.put("title", "Product");
					list.add(productTitle);				
					activityWorkpackageNameTitle.put("title", "Workpackage");
					list.add(activityWorkpackageNameTitle);				
					activityNameTitle.put("title", "Activity");
					list.add(activityNameTitle);								
					activityTaskNameTitle.put("title", "Task");
					list.add(activityTaskNameTitle);							
					resourceTitle.put("title", "Resource");
					list.add(resourceTitle);				
					statusTitle.put("title", "Status");
					list.add(statusTitle);				
					actualEffortTitle.put("title", "Effort in Hr(s)");
					list.add(actualEffortTitle);				
					actualStartDateTitle.put("title", "Actual Start Date");
					list.add(actualStartDateTitle);				
					actualEndDateTitle.put("title", "Actual End Date");
					list.add(actualEndDateTitle);				
					plannedStartDateTitle.put("title", "Planned Start Date");
					list.add(plannedStartDateTitle);				
					plannedEndDateTitle.put("title", "Planned End Date");
					list.add(plannedEndDateTitle);				
					commentsTitle.put("title", "Comments");
					list.add(commentsTitle);			
			
					finalObj.put("COLUMNS", list);
					finalObj.put("DATA", columnData1);				
					finalResult=finalObj.toString();
				}
			}catch(Exception e){
				log.error("Error",e);
			}
		} catch (Exception e) {
            log.error("JSON ERROR", e);
        }		        
	    return "["+finalResult+"]";
	}
	@RequestMapping(value = "process.taskefforttemplate.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String addTaskEffortTemplate( HttpServletRequest request,@RequestParam String templateName, @RequestParam String mappedFields,@RequestParam String fromDate, @RequestParam String toDate, @RequestParam String statusValue, @RequestParam String productValue, @RequestParam String resourceValue) {	
	String response=null;
		try {
			
			log.info("templateName "+templateName);
			log.info("mappedFields "+mappedFields);
			log.info(" statusValue "+statusValue);
			Date dateRangeStart=null;
		    Date dateRangeEnd=null;
		    UserList user=(UserList)request.getSession().getAttribute("USER");
			if(fromDate!=null &&!fromDate.isEmpty() && toDate!=null && !toDate.isEmpty() ){
				dateRangeStart=DateUtility.dateformatWithOutTime(fromDate);
				dateRangeEnd=DateUtility.dateformatWithOutTime(toDate);
			}
			TaskEffortTemplate taskEffortTemplate=new TaskEffortTemplate();
			taskEffortTemplate.setTemplateName(templateName);
			taskEffortTemplate.setMappedFields(mappedFields);
			taskEffortTemplate.setFromDateValue(dateRangeStart);
			taskEffortTemplate.setToDateValue(dateRangeEnd);			
			taskEffortTemplate.setProductValue(productValue);
			taskEffortTemplate.setStatusValue(statusValue);
			taskEffortTemplate.setResourceValue(resourceValue);
			taskEffortTemplate.setCreatedBy(user.getUserId());
			taskEffortTemplate.setCreatedOn(new Date());
			taskEffortTemplate.setIsActive(1);
			log.info("taskEffortTemplate    --- "+ taskEffortTemplate);
			reportService.addTaskEffortTemplate(taskEffortTemplate);
			
		} catch (Exception e) {
			response="Error adding task effort template record!";
			log.error("ERROR", e);
		}
		return response;
	}
	@RequestMapping(value="task.effort.templates.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions loadTaskEffortTemplates(HttpServletRequest request) {
		log.debug("inside common.list.task.effort.templates");
		JTableResponseOptions jTableResponseOptions=null;
		 
		try {
			List<TaskEffortTemplate> taskEffortTemplate = reportService.getTaskEffortTemplateList();
			List<JsonTaskEffortTemplate> jsonTaskEffortTemplate=new ArrayList<JsonTaskEffortTemplate>();
			if (taskEffortTemplate != null){
				for(TaskEffortTemplate dvm: taskEffortTemplate){
					jsonTaskEffortTemplate.add(new JsonTaskEffortTemplate(dvm));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonTaskEffortTemplate);	
			}
			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	@RequestMapping(value="pivot.report.activity.task.effort",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String getActivityTaskEffortPivotReport(@RequestParam Integer productId) {			
		String finalResult="";
		try {
			log.info("pivot.report.activity.task.effort productId*****"+productId);
			List<Object[]> activityTaskEffortReportList =  new ArrayList<Object[]>();
			JSONArray list = new JSONArray();
			JSONArray columnData = new JSONArray();
			JSONArray columnData1 = new JSONArray();
			JSONObject finalObj = new JSONObject();
			JSONObject productTitle = new JSONObject();
			JSONObject activityWorkpackageNameTitle = new JSONObject();
			JSONObject activityNameTitle = new JSONObject();
			JSONObject assigneeTitle = new JSONObject();
			JSONObject activityTaskNameTitle = new JSONObject();
			JSONObject actualEffortTitle = new JSONObject();		
			JSONObject taskTypeTitle = new JSONObject();
			JSONObject priorityTitle = new JSONObject();
			JSONObject plannedStartDateTitle = new JSONObject();
			JSONObject plannedEndDateTitle = new JSONObject();
			JSONObject remarksTitle = new JSONObject();
			JSONObject createdDateTitle = new JSONObject();
			
			productTitle.put("title", "Product");
			list.add(productTitle);
			activityWorkpackageNameTitle.put("title", "Workpackage");
			list.add(activityWorkpackageNameTitle);
			activityNameTitle.put("title", "Activity");
			list.add(activityNameTitle);			
			activityTaskNameTitle.put("title", "Task");
			list.add(activityTaskNameTitle);
			taskTypeTitle.put("title", "Task Type");
			list.add(taskTypeTitle);
			assigneeTitle.put("title", "Resource");
			list.add(assigneeTitle);
			actualEffortTitle.put("title", "Actual Efforts Spent");
			list.add(actualEffortTitle);
									
			priorityTitle.put("title", "Priority");
			list.add(priorityTitle);
			plannedStartDateTitle.put("title", "Planned Start Date");
			list.add(plannedStartDateTitle);			
			plannedEndDateTitle.put("title", "Planned End Date");
			list.add(plannedEndDateTitle);			
			createdDateTitle.put("title", "Created Date");
			list.add(createdDateTitle);		
			remarksTitle.put("title", "Remarks");
			list.add(remarksTitle);
			
			finalObj.put("COLUMNS", list);
			
			activityTaskEffortReportList = reportService.getActivityTaskEffortReportBasedOnFilter(productId+"","", null, null,"");
			if(activityTaskEffortReportList.size()!=0){
				JSONObject datObj = null;
				int i=1;
				
				for (Object[] objects : activityTaskEffortReportList) {
					columnData = new JSONArray();
					datObj = new JSONObject();
					datObj.put("id", i);
					datObj.put("product", ""+(String)objects[0]+"");
					datObj.put("workpackage", ""+(String)objects[1]+"");
					datObj.put("activity", ""+(String)objects[2]+"");
					datObj.put("task", ""+(String)objects[3]+"");
					datObj.put("resource", ""+(String)objects[4]+"");
					datObj.put("status", ""+(String)objects[5]+"");
					datObj.put("effort", objects[6].toString());
					datObj.put("actualStDate", ""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objects[7])+"");
					datObj.put("actualEndDate", ""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objects[8])+"");
					datObj.put("plannedStDate", ""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objects[9])+"");
					datObj.put("plannedEndDate", ""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objects[10])+"");
					datObj.put("remarks", ""+(String)objects[11]+"");

					columnData.add(datObj);
					columnData1.add(columnData);
					i++;
				}
			}
			finalObj.put("DATA", columnData1);				
			finalResult=finalObj.toString();
		} catch (Exception e) {
            log.error("JSON ERROR", e);
        }		        
	    return "["+finalResult+"]";
	}
	@RequestMapping(value = "sync.db.backup.restore", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String syncMysqlDbBackupAndRestoreProcess( HttpServletRequest request) {	
	String response=null;
		try {			
						
			BackupAndRestoreDbUtil dbUtil=new BackupAndRestoreDbUtil();
			String mysqlInstallationDriveLoc=getValueFromPropFile("mysql.installation.drive.location");// C:
			String mysqlPath="\""+getValueFromPropFile("mysql.installation.mysql.exe.location")+"\"";
			String dbServerHostname=getValueFromPropFile("db.server.hostname");// localhost or 10.108.66.173
			String dbServerPort=getValueFromPropFile("db.server.port");// 3306
			String dbServerUserName=getValueFromPropFile("db.server.username");// 3306
			String dbServerPassword=getValueFromPropFile("db.server.password");// 3306
			
			String dbMirrorHostname=getValueFromPropFile("db.mirror.hostname");// localhost or 10.108.66.173
			String dbMirrorPort=getValueFromPropFile("db.mirror.port");// 3306
			String dbMirrorUserName=getValueFromPropFile("db.mirror.username");// 3306
			String dbMirrorPassword=getValueFromPropFile("db.mirror.password");// 3306
			String dbName=getValueFromPropFile("db.sync.dbname");// ilcm_workflow
			String dbBackupBatFile=getValueFromPropFile("db.backup.bat.filename");
			String dbBckupMirrorBatFile=getValueFromPropFile("db.backup.mirror.filename");
			String dbBackupFolderLoc=getValueFromPropFile("db.backup.folder.location");
			String mysqlFilePath=getValueFromPropFile("db.backup.folder.location")+"backup_" + dbName+".sql";
			String restoreSqlFilePath=getValueFromPropFile("db.backup.folder.location")+"backup_restore_" + dbName+".sql";
			String dbMirrorRestoreDbBatFilePath=getValueFromPropFile("db.backup.restore.filename.withfullpath");// 3306
			String mysqlBinLoc=getValueFromPropFile("mysql.installation.bin.folder.location");
			String syncMongoDbStatus=getValueFromPropFile("sync.mongodbdata");
			String dbMirrorBackupRequired=getValueFromPropFile("db.mirror.backup.required");
			log.info("mysqlPath>>>>"+mysqlPath);
			try{
				File f1 = new File(dbBackupFolderLoc);
		        f1.mkdir();
				dbUtil.getData(mysqlInstallationDriveLoc,mysqlPath, dbServerHostname, dbServerPort,dbServerUserName, dbServerPassword, dbName, mysqlFilePath,dbBackupBatFile,mysqlBinLoc);
				if(dbMirrorBackupRequired.equals("true"))
					dbUtil.getData(mysqlInstallationDriveLoc,mysqlPath, dbServerHostname, dbServerPort,dbServerUserName, dbServerPassword, dbName, restoreSqlFilePath,dbBckupMirrorBatFile,mysqlBinLoc);
				
				log.info("Restore Process Initiated...");
				dbUtil.restoreDatabase(mysqlInstallationDriveLoc,mysqlPath, dbMirrorHostname, dbMirrorPort,dbMirrorUserName, dbMirrorPassword, dbName, mysqlFilePath,dbMirrorRestoreDbBatFilePath,mysqlBinLoc);
				log.info("Restore Process Completed...");
				if(syncMongoDbStatus.equals("true")){
					log.info("Sync Mongo db process initiated...");
					mongoDbService.pushToMongoDB(0, null, null);// sync all mongo collections
					log.info("Sync Mongo db process completed...");
				}
			}catch(Exception e){
				log.error("Error in db sync up...",e);
			}
		} catch (Exception e) {
			response="Error sync mysql db!";
			log.error("ERROR", e);
		}
		return response;
	}
	public String getValueFromPropFile(String propertyName) {

		InputStream fis;
		Properties appProperties = new Properties();
		String propertyValue = "";
		try {
			fis = getClass().getResourceAsStream("/TAFServer.properties");
			appProperties.load(fis);
			if (appProperties.get(propertyName) != null
					&& appProperties.get(propertyName) != "") {
				propertyValue = appProperties.get(propertyName).toString()
						.trim();
			}
		} catch (FileNotFoundException e) {
			log.error("Exception in getting TAFServer.properties file", e);
		} catch (IOException ioe) {
			log.error("Exception in loading hpqcProperties:", ioe);
		}
		return propertyValue;
	}
	
	
}
