package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.DefectTypeMaster;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.dto.TestCaseDTO;

public interface DefectUploadDAO {

	void updateDefectUPloadFlag(TestExecutionResultBugList defect);
	public int addBulk(List<TestExecutionResultBugList> listOfDefectData, int batchSize, String action);
	List<String> getExistingIssueId(String issueId);
	Integer getDefectDataId(String issueId);
	DefectTypeMaster listDefectType(String defectTypeName);
	ExecutionPriority listExecutionPriority(String priorityName);
	WorkFlow listWorkflow(String status);
	UserList listUserList(String userName);
	List<Object> listDefectsByTestcaseExecutionPlanIdAnalyse(Integer productId,	Integer productVersionId, Integer productBuildId, Integer workPackageId, Integer approveStatus, Date startDate,
			Date endDate, Integer issueStatus, Integer analyseStatus, Integer jtStartIndex, Integer jtPageSize);
	TestExecutionResultBugList getByBugWithCompleteInitialization(Integer bugId);
	List<TestExecutionResultBugList> listDefectsAnalyseFromData(String stageName, String featureName, Integer action, Integer jtStartIndex, Integer jtPageSize);
	void update(TestExecutionResultBugList defects);
	List<TestCaseDTO> listWorkPackageEnvironmentSummary(Integer workPackageId);
	List<String> listRunConfigurationNameBywpId(Integer workPackageId);
	List<TestCaseDTO> listWorkPackageTestCasesExecutionPlanBywpId(Integer workPackageId);
	List<TestCaseDTO> listWorkPackageTimeSheetBywpId(Integer workPackageId);
	
}
