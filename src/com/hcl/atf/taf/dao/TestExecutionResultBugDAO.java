package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.DefectExportData;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultBugList;

public interface TestExecutionResultBugDAO  {	 
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
	int getTotalRecords();
	int getTotalBugsForTestRun(int runNo, int testRunConfigurationChildId );
	int getTotalRecords(int runNo, int testRunConfigurationChildId, int deviceListId);

	public List<TestExecutionResultBugList> listFilteredTestExecutionResult(
			int startIndex, int pageSize, int productId, String platformName,
			int testRunListId, Date timeFrom, Date timeTo); 
	int getTotalRecordsFiltered(int productId, String platformName, int testRunListId,
			Date timeFrom, Date timeTo);
	void addDefectExportData(DefectExportData defectsExportData);//Changes for updating defect traceability information in  defect export data table
	List<TestExecutionResult> listExceutionResults(int runListId,int testCaseListId);
	TestExecutionResultBugList getByBugWithCompleteInitialization(int bugId);
	List<TestExecutionResultBugList> listAllPaginate(int startIndex, int pageSize, Date startDate,Date endDate);
	
	Integer getDefectsCount(Integer productId);
}
