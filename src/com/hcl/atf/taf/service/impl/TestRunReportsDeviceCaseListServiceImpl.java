package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestRunReportsDeviceCaseListDAO;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseList;
import com.hcl.atf.taf.service.TestRunReportsDeviceCaseListService;
@Service
public class TestRunReportsDeviceCaseListServiceImpl implements TestRunReportsDeviceCaseListService {	 
	
	@Autowired
	private TestRunReportsDeviceCaseListDAO testRunReportsDeviceCaseListDAO;
	
	
	@Override
	@Transactional
	public TestRunReportsDeviceCaseList getByTestRunReportsDeviceCaseListId(int testRunListId) {
		
		return testRunReportsDeviceCaseListDAO.getByTestRunReportsDeviceCaseListId(testRunListId);
	}

	
	@Override
	@Transactional
	public List<TestRunReportsDeviceCaseList> listAllTestRunReportsDeviceCaseResult() {
		
		return testRunReportsDeviceCaseListDAO.listAll();
	}

	@Override
	@Transactional
	public List<TestRunReportsDeviceCaseList> listAllTestRunReportsDeviceCaseResult(int startIndex, int pageSize) {
		
		return testRunReportsDeviceCaseListDAO.listAll(startIndex, pageSize);
	}
	

	@Override
	@Transactional
	public List<TestRunReportsDeviceCaseList> listAllTestRunReportsDeviceCaseResult(int startIndex, int pageSize,Integer testRunListId,Integer testRunConfigurationChildId){
		
		return testRunReportsDeviceCaseListDAO.list(testRunListId, testRunConfigurationChildId, startIndex, pageSize);
	}
	
	@Override
	@Transactional
	public List<TestRunReportsDeviceCaseList> listAllTestRunReportsDeviceCaseResult(Integer testRunListId,Integer testRunConfigurationChildId){
		
		return testRunReportsDeviceCaseListDAO.list(testRunListId, testRunConfigurationChildId);
	}
	
	@Override
	@Transactional
	public int getTotalRecordsOfTestRunReportsDeviceCaseListResult() {
		
		return testRunReportsDeviceCaseListDAO.getTotalRecords();
	}
	
	@Override
	@Transactional
	public int getTotalRecordsOfTestRunReportsDeviceCaseListResult(Integer testRunListId,Integer testRunConfigurationChildId){
		
		return testRunReportsDeviceCaseListDAO.getTotalRecordsFilteredTestRunReportsDeviceCaseList(testRunListId, testRunConfigurationChildId);
	}


	//Added for iLCM TAF integration - Generating Excel reports  - Bugzilla Id 717
	@Override
	@Transactional
	public List<TestRunReportsDeviceCaseList> getTestRunReportsByTestRunJobId(int testRunJobId) {
		
		return testRunReportsDeviceCaseListDAO.getTestRunReportsByTestRunJobId(testRunJobId);
	}

	
	
	
}
