package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestRunReportsResultDAO;
import com.hcl.atf.taf.model.custom.TestRunReportsList;
import com.hcl.atf.taf.service.TestRunReportsListService;
@Service
public class TestRunReportsListServiceImpl implements TestRunReportsListService {	 
	
	@Autowired
	private TestRunReportsResultDAO testRunReportsResultDAO;
	//@Autowired
	
	

	@Override
	@Transactional
	public TestRunReportsList getByTestRunReportsResultId(int testRunNo) {
		
		return testRunReportsResultDAO.getByTestRunReportListId(testRunNo);
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestRunReportsResult() {
		
		return testRunReportsResultDAO.getTotalRecords();
	}

	@Override
	@Transactional
	public List<TestRunReportsList> listTestRunReportsResult(int testRunNo) {
		
		return testRunReportsResultDAO.list(testRunNo);
	}

	@Override
	@Transactional
	public List<TestRunReportsList> listTestRunReportsResult(int testRunNo, int startIndex,
			int pageSize) {
		
		return testRunReportsResultDAO.list(testRunNo, startIndex, pageSize);
	}
	
	@Override
	@Transactional
	public List<TestRunReportsList> listAllTestRunReportsResult() {
		
		return testRunReportsResultDAO.listAll();
	}

	@Override
	@Transactional
	public List<TestRunReportsList> listAllTestRunReportsResult(int startIndex, int pageSize) {
		
		return testRunReportsResultDAO.listAll(startIndex, pageSize);
	}
	public List<TestRunReportsList> listAllTestRunReportsResult(int startIndex, int pageSize,Integer testRunNo,String productName,String productVersionName){
		
		return testRunReportsResultDAO.listAll(startIndex, pageSize);
	}


	
}
