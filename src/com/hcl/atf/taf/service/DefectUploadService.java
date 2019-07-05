package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hcl.atf.taf.model.DefectTypeMaster;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.dto.TestCaseDTO;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;


public interface DefectUploadService {
	
	String exportToGetAuthKey();
	Integer exportToGoogleDriveDefects(String code, Integer productId, Integer productVersionId, Integer productBuildId, Integer workPackageId, Integer approveStatus, Date startDate, Date endDate);
	boolean exportToGoogleDriveTestResults(Integer workPackageId, String code);
	int addDefectsBulk(List<TestExecutionResultBugList> listOfDefectsToUpdate, String string);
	List<String> getExistingIssueId(String string);
	Integer getDefectDataId(String string);
	DefectTypeMaster getDefectType(String defectTypeName);
	ExecutionPriority getExecutionPriority(String priorityName);
	WorkFlow getWorkFlow(String status);
	UserList getUserList(String string);
	List<TestExecutionResultBugList> listDefectsByTestcaseExecutionPlanIdAnalyse(Integer productId, Integer productVersionId, Integer productBuildId, Integer workPackageId, Integer approveStatus, Date startDate, Date endDate,
			Integer issueStatus, Integer analyseStatus, Integer jtStartIndex, Integer jtPageSize);
	List<TestExecutionResultBugList> listDefectsAnalyseFromData(String stageName,String featureName, Integer action, Integer jtStartIndex, Integer jtPageSize);
	boolean updateDefectAnalyseUpdate(Integer bugId, Integer action);
	TestExecutionResultBugList listDefectsBySpecificTestcaseExecutionBugId(Integer bugid);
	void updateDefectbyIssueId(TestExecutionResultBugList testExecutionResultBugListUI);
	HashMap<String, JsonWorkPackageTestCaseExecutionPlanForTester> getworkPackageEnvironmentSummary(Integer workPackageId);
	List<String> listRunConfigurationNameBywpId(Integer workPackageId);
	List<TestCaseDTO> listWorkPackageTestCasesExecutionPlanBywpId(Integer workPackageId);
	List<TestCaseDTO> listWorkPackageTimeSheetBywpId(Integer workPackageId);
	
}
