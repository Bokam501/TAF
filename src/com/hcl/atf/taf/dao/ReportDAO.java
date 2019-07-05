package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.MongoCollection;
import com.hcl.atf.taf.model.PivotRestTemplate;
import com.hcl.atf.taf.model.ReportIssue;
import com.hcl.atf.taf.model.TaskEffortTemplate;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.dto.PivotRestTemplateDTO;
import com.hcl.atf.taf.model.dto.TimeSheetStatisticsDTO;
import com.hcl.atf.taf.model.dto.WorkPackageResultsStatisticsDTO;

public interface ReportDAO  {	 
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanResultsEod(Integer workPackageId, Integer shiftId, Date executionDate, int jtStartIndex,int jtPageSize);
	List<WorkPackageResultsStatisticsDTO> listWorkPackageStatisticsEOD(Integer workPackageId, Integer shiftId, Date executionDate);
	List<TimeSheetStatisticsDTO> listCompleteTimeSheetStatistics(Date executionDateFrom, Date executionDateTo);
	List<Activity> listActivitiesByDate(Date dataFromDate, Date dataToDate,Integer productId);
	List<Activity> getActivitiesByDate(Date dataFromDate, Date dataToDate);
	List<Object[]> getActivityTaskEffortReport(Integer productId);
	void addTaskEffortTemplate(TaskEffortTemplate taskEffortTemplate) ;
	List<TaskEffortTemplate> getTaskEffortTemplateList();
	List<Object[]> getActivityTaskEffortReportBasedOnFilter(String productId, String statusVal, Date fromDate, Date toDate, String resourceVal);
	TaskEffortTemplate getTaskEffortTemplateById(Integer templateId);
	List<Object[]> getResourceEffortReport(Integer productId);
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
