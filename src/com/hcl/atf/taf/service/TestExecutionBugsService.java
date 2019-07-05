package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.DefectApprovalStatusMaster;
import com.hcl.atf.taf.model.DefectExportData;
import com.hcl.atf.taf.model.DefectIdentificationStageMaster;
import com.hcl.atf.taf.model.DefectSeverity;
import com.hcl.atf.taf.model.DefectTypeMaster;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.UserList;

public interface TestExecutionBugsService  {	 
	
	void add (TestExecutionResultBugList bug);
	void update (TestExecutionResultBugList bug);
	void delete (TestExecutionResultBugList bug);
	List<TestExecutionResultBugList> listAll();
	List<TestExecutionResultBugList> listByTestRun(int runNo, int testRunConfigurationChildId);
	List<TestExecutionResultBugList> list(int runNo, int testRunConfigurationChildId, int deviceListId);
	TestExecutionResultBugList getByBugId(int bugId);

	List<TestExecutionResultBugList> listAllPaginate(int startIndex, int pageSize);
	List<TestExecutionResultBugList> listTestExecutionResultBugsPaginate(int runNo, int testRunConfigurationChildId, int startIndex, int pageSize);
	List<TestExecutionResultBugList> listTestExecutionResultBugsPaginate(int runNo, int testRunConfigurationChildId, int deviceListId,int startIndex, int pageSize);
	int getTotalBugsCount();
	int getTotalBugsCount(int runNo, int testRunConfigurationChildId );
	int getTotalBugsCount(int runNo, int testRunConfigurationChildId, int deviceListId);
	void addDefectExportData(DefectExportData defectsExportData);//Changes for updating defect traceability information in defect export data table
	//TestExecutionResultBugList END
	List<TestExecutionResult> listExceutionResults(int runListId,int testCaseListId);
	
	
	List<DefectSeverity> listDefectSeverity();
	DefectSeverity getDefectSeverityByseverityId(int severityId);
	
	List<DefectTypeMaster> listDefectTypes();
	DefectTypeMaster getDefectTypeById(int defectTypeId);
	
	List<DefectIdentificationStageMaster> listDefectIdentificationStages();
	DefectIdentificationStageMaster getDefectIdentificationStageMasterById(int defectTypeId);
	List<DefectApprovalStatusMaster> listDefectApprovalStatus();
	DefectApprovalStatusMaster getDefectApprovalStatusById(int defectApprovalStatusId);
	void reviewAndApproveDefect(TestExecutionResultBugList defectFromDB,
			UserList approver, int isApprovedStautsFromUI);
}
