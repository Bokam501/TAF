package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestRunReportsDeviceCaseStepListDAO;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseStepList;
import com.hcl.atf.taf.service.TestRunReportsDeviceCaseStepListService;
@Service
public class TestRunReportsDeviceCaseStepListServiceImpl implements TestRunReportsDeviceCaseStepListService {	 
	
	@Autowired
	private TestRunReportsDeviceCaseStepListDAO testRunReportsDeviceCaseStepListDAO;
	
	
	@Override
	@Transactional
	public TestRunReportsDeviceCaseStepList getByTestRunReportsDeviceCaseStepListId(int testRunListId) {
		
		return testRunReportsDeviceCaseStepListDAO.getByTestRunReportsDeviceCaseStepListId(testRunListId);
	}

	
	@Override
	@Transactional
	public List<TestRunReportsDeviceCaseStepList> listAllTestRunReportsDeviceCaseStepResult() {
		
		return testRunReportsDeviceCaseStepListDAO.listAll();
	}

	@Override
	@Transactional
	public List<TestRunReportsDeviceCaseStepList> listAllTestRunReportsDeviceCaseStepResult(int startIndex, int pageSize) {
		
		return testRunReportsDeviceCaseStepListDAO.listAll(startIndex, pageSize);
	}
	
	@Override
	@Transactional
	public List<TestRunReportsDeviceCaseStepList> listAllTestRunReportsDeviceCaseStepResult(int startIndex, int pageSize,Integer testRunListId,Integer testRunConfigurationChildId,Integer testCaseId){
		
		return testRunReportsDeviceCaseStepListDAO.list(startIndex, pageSize,testRunListId, testRunConfigurationChildId,testCaseId);
	}
	
	@Override
	@Transactional
	public List<TestRunReportsDeviceCaseStepList> listAllTestRunReportsDeviceCaseStepResult(Integer testRunListId,Integer testRunConfigurationChildId,Integer testCaseId){
		
		return testRunReportsDeviceCaseStepListDAO.list(testRunListId, testRunConfigurationChildId,testCaseId);
	}
	
	@Override
	@Transactional
	public int getTotalRecordsOfTestRunReportsDeviceCaseStepListResult() {
		
		return testRunReportsDeviceCaseStepListDAO.getTotalRecords();
	}
	
	@Override
	@Transactional
	public int getTotalRecordsOfTestRunReportsDeviceCaseStepListResult(Integer testRunListId,Integer testRunConfigurationChildId,Integer testCaseId){
		
		return testRunReportsDeviceCaseStepListDAO.getTotalRecordsFilteredTestRunReportsDeviceCaseStepList(testRunListId, testRunConfigurationChildId,testCaseId);
	}

	@Override
	@Transactional
	public List<TestRunReportsDeviceCaseStepList> listAllTestRunReportsDeviceCaseStepResult(Integer testRunNo,Integer testRunConfigurationChildId) {
		
		return testRunReportsDeviceCaseStepListDAO.listForEvidenceGrid(testRunNo, testRunConfigurationChildId);
	}
	
	@Override
	@Transactional
	public int getTotalRecordsOfEvidence(Integer testRunNo, Integer testRunConfigurationChildId) {
		return testRunReportsDeviceCaseStepListDAO.getTotalRecordsOfEvidence(testRunNo, testRunConfigurationChildId);
	}

	//Added for iLCM TAF integration - Generating Excel reports  - Bugzilla Id 717
	@Override
	public List<TestRunReportsDeviceCaseStepList> getByJobId(int testRunJobId) {
		return testRunReportsDeviceCaseStepListDAO.getByJobId(testRunJobId);
	}
}
