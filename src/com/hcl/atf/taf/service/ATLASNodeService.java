package com.hcl.atf.taf.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionSummary;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;

public interface ATLASNodeService {
	
	public JTableResponse postWorkpackageDefects(Integer workpackageId,  String defectIds, HttpServletRequest request);
	
	public JTableResponse postWorkpackageTestResults(Integer workpackageId, Integer tmsId, HttpServletRequest request);
	
	public List<JsonTestCaseList> getISERecommendedTestcases(TestRunPlan testPlan);
	
	public String listTestBeds(Integer testPlanId);

	public List<JsonWorkPackageTestCaseExecutionSummary> listTestJobsWorkpackageSummary(Integer workPackageId, Integer productBuildId);

	public org.json.JSONObject executeSelectiveTestCasesTestPlan(String nodeRedTafJSONQuery);

	public List<JsonTestCaseList> getISERecommendedTestcases(TestRunPlan testPlan, Integer productBuildId);

	public org.json.JSONObject executeTestPlanGroup(String nodeRedTafJSONQuery);
}
