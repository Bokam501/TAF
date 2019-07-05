package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.MongoCollection;
import com.hcl.atf.taf.model.PivotRestTemplate;
import com.hcl.atf.taf.model.ReportIssue;
import com.hcl.atf.taf.model.TaskEffortTemplate;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.dto.PivotRestTemplateDTO;
import com.hcl.atf.taf.model.json.JsonActivityReport;
import com.hcl.atf.taf.model.json.JsonTimeSheetStatistics;
import com.hcl.atf.taf.model.json.JsonWorkPackageResultsStatistics;

public interface ReportService {
	
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanResultsEod(Integer workPackageId, Integer shiftId, Date executionDate, int jtStartIndex,int jtPageSize);
	List<JsonWorkPackageResultsStatistics> listWorkPackageStatisticsEOD(Integer workPackageId, Integer shiftId, Date executionDate);
	List<JsonTimeSheetStatistics> listTimeSheetStatistics(Date executionDateFrom, Date executionDateTo);
	List<JsonActivityReport> listActivitiesByDate(Date dataFromDate, Date dataToDate,Integer productId);
	List<Activity> getActivitiesByDate(Date dataFromDate, Date dataToDate,Integer productId);
	List<Object[]> getActivityTaskEffortReport(Integer productId);
	List<Object[]> getResourceEffortReport(Integer productId);
	void addTaskEffortTemplate(TaskEffortTemplate taskEffortTemplate) ;
	List<TaskEffortTemplate> getTaskEffortTemplateList();
	List<Object[]> getActivityTaskEffortReportBasedOnFilter(String productId, String statusVal, Date fromDate, Date toDate, String resourceVal);
	TaskEffortTemplate getTaskEffortTemplateById(Integer templateId);	
	List<MongoCollection> getMongoCollectionList();
	void addPivotRestTemplate(PivotRestTemplate pivotRestTemplate);
	List<Object[]> getPivotRestTemplateList(Integer templateId);
	List<PivotRestTemplate> getPivotRestTemplateReportByParams(Integer factoryId, Integer productId, Integer collectionId);
	List<PivotRestTemplate> getPivotRestTemplateList();
	List<PivotRestTemplateDTO> getPivotRestTemplateList(Integer collectionId,Integer jtStartIndex,Integer jtPageSize);
	String deletePivotRestReportById(int templateId);	
	List<Object[]> getProductAndEngagementNameList(String productIds);
	List<ReportIssue> getReportIssueList();
	void updateReportIssue(ReportIssue reportIssue);
	void addReportIssue(ReportIssue reportIssue);
	
	
}
