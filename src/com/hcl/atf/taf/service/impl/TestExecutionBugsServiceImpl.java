package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.DefectSeverityDAO;
import com.hcl.atf.taf.dao.DefectTypeDAO;
import com.hcl.atf.taf.dao.TestExecutionResultBugDAO;
import com.hcl.atf.taf.dao.TestRunListDAO;
import com.hcl.atf.taf.model.DefectApprovalStatusMaster;
import com.hcl.atf.taf.model.DefectExportData;
import com.hcl.atf.taf.model.DefectIdentificationStageMaster;
import com.hcl.atf.taf.model.DefectSeverity;
import com.hcl.atf.taf.model.DefectTypeMaster;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.service.TestExecutionBugsService;

@Service
public class TestExecutionBugsServiceImpl implements TestExecutionBugsService {

	@Autowired
	private TestExecutionResultBugDAO testExecutionResultBugDAO;
	@Autowired
	private TestRunListDAO testRunListDAO;
	@Autowired
	private DefectSeverityDAO defectSeverityDAO;
	@Autowired
	private DefectTypeDAO defectTypeDAO;

	@Override
	@Transactional
	public void add(TestExecutionResultBugList bug) {
		testExecutionResultBugDAO.add(bug);

	}

	@Override
	@Transactional
	public void update(TestExecutionResultBugList bug) {
		testExecutionResultBugDAO.update(bug);

	}

	@Override
	@Transactional
	public void delete(TestExecutionResultBugList bug) {
		testExecutionResultBugDAO.delete(bug);
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listAll() {
		return testExecutionResultBugDAO.listAll();
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listByTestRun(int runNo,
			int testRunConfigurationChildId) {
		return testExecutionResultBugDAO.listByTestRun(runNo,
				testRunConfigurationChildId);
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> list(int runNo,
			int testRunConfigurationChildId, int deviceListId) {
		return testExecutionResultBugDAO.list(runNo,
				testRunConfigurationChildId, deviceListId);
	}

	@Override
	@Transactional
	public TestExecutionResultBugList getByBugId(int bugId) {
		return testExecutionResultBugDAO.getByBugId(bugId);
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listAllPaginate(int startIndex,
			int pageSize) {
		return testExecutionResultBugDAO.listAllPaginate(startIndex, pageSize);
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listTestExecutionResultBugsPaginate(
			int runNo, int testRunConfigurationChildId, int startIndex,
			int pageSize) {
		return testExecutionResultBugDAO.listTestExecutionResultBugsPaginate(
				runNo, testRunConfigurationChildId, startIndex, pageSize);
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listTestExecutionResultBugsPaginate(
			int runNo, int testRunConfigurationChildId, int deviceListId,
			int startIndex, int pageSize) {
		return testExecutionResultBugDAO.listTestExecutionResultBugsPaginate(
				runNo, testRunConfigurationChildId, deviceListId, startIndex,
				pageSize);
	}

	@Override
	@Transactional
	public int getTotalBugsCount() {
		return testExecutionResultBugDAO.getTotalRecords();
	}

	@Override
	@Transactional
	public int getTotalBugsCount(int runNo, int testRunConfigurationChildId) {
		return testExecutionResultBugDAO.getTotalBugsForTestRun(runNo,
				testRunConfigurationChildId);
	}

	@Override
	@Transactional
	public int getTotalBugsCount(int runNo, int testRunConfigurationChildId,
			int deviceListId) {
		return testExecutionResultBugDAO.getTotalRecords(runNo,
				testRunConfigurationChildId, deviceListId);
	}

	// Changes for updating defect traceability information in defect export
	// data table
	@Override
	@Transactional
	public void addDefectExportData(DefectExportData defectsExportData) {
		testExecutionResultBugDAO.addDefectExportData(defectsExportData);
	}

	@Override
	public List<TestExecutionResult> listExceutionResults(int runListId,
			int testCaseListId) {

		return testExecutionResultBugDAO.listExceutionResults(runListId,
				testCaseListId);
	}

	@Override
	@Transactional
	public List<DefectSeverity> listDefectSeverity() {
		return defectSeverityDAO.list();
	}

	@Override
	public DefectSeverity getDefectSeverityByseverityId(int severityId) {
		return defectSeverityDAO.getDefectSeverityByseverityId(severityId);
	}
	
	

	@Override
	@Transactional
	public List<DefectTypeMaster> listDefectTypes() {
		return defectTypeDAO.listDefectTypes();
	}

	@Override
	public DefectTypeMaster getDefectTypeById(int defectTypeId) {
		return defectTypeDAO.getDefectTypeById(defectTypeId);
	}

	@Override
	@Transactional
	public List<DefectIdentificationStageMaster> listDefectIdentificationStages() {
		return defectTypeDAO.listDefectIdentificationStages();
	}

	@Override
	public DefectIdentificationStageMaster getDefectIdentificationStageMasterById(
			int defectTypeId) {
		return defectTypeDAO.getDefectIdentificationStageMasterById(defectTypeId);
	}
	
	@Override
	@Transactional
	public List<DefectApprovalStatusMaster> listDefectApprovalStatus() {
		return defectTypeDAO.listDefectApprovalStatus();
	}

	@Override
	@Transactional
	public DefectApprovalStatusMaster getDefectApprovalStatusById(int defectApprovalStatusId){
		return defectTypeDAO.getDefectApprovalStatusById(defectApprovalStatusId);
	}

	@Override
	@Transactional
	public void reviewAndApproveDefect(TestExecutionResultBugList defectFromUI,UserList approver, int isApproved){
		if(isApproved == 1){
			defectFromUI.setApprovedBy(approver);
		}else{
			defectFromUI.setApprovedBy(null);
		}
		defectFromUI.setIsApproved(isApproved);
		defectFromUI.setApprovedOn(DateUtility.getCurrentTime());
		defectTypeDAO.reviewAndApproveDefect(defectFromUI);
		
	}
	
}
