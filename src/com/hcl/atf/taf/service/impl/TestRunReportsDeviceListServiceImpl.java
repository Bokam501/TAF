package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestRunReportsDeviceResultDAO;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceList;
import com.hcl.atf.taf.service.TestRunReportsDeviceListService;
@Service
public class TestRunReportsDeviceListServiceImpl implements TestRunReportsDeviceListService {	 
	
	@Autowired
	private TestRunReportsDeviceResultDAO testRunReportsDeviceResultDAO;
	
	
	@Override
	@Transactional
	public TestRunReportsDeviceList getByTestRunReportsDeviceListId(int testRunListId) {
		
		return testRunReportsDeviceResultDAO.getByTestRunReportsDeviceListId(testRunListId);
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestRunReportsDeviceListResult() {
		
		return testRunReportsDeviceResultDAO.getTotalRecords();
	}


	@Override
	@Transactional
	public List<TestRunReportsDeviceList> listTestRunReportsDeviceResult(int testRunNo) {
		
		return testRunReportsDeviceResultDAO.list(testRunNo);
	}
	
	@Override
	@Transactional
	public List<TestRunReportsDeviceList> listTestRunReportsDeviceResult(int testRunNo, int startIndex,
			int pageSize) {
		
		return testRunReportsDeviceResultDAO.list(testRunNo, startIndex, pageSize);
	}
	

	@Override
	@Transactional
	public List<TestRunReportsDeviceList> listAllTestRunReportsDeviceResult() {
		
		return testRunReportsDeviceResultDAO.listAll();
	}

	@Override
	@Transactional
	public List<TestRunReportsDeviceList> listAllTestRunReportsDeviceResult(int startIndex, int pageSize) {
		
		return testRunReportsDeviceResultDAO.listAll(startIndex, pageSize);
	}
	public List<TestRunReportsDeviceList> listAllTestRunReportsDeviceResult(int startIndex, int pageSize,Integer testRunNo,String productName,String productVersionName){
		
		return testRunReportsDeviceResultDAO.listAll(startIndex, pageSize);
	}

	@Override
	@Transactional
	public List<TestRunReportsDeviceList> listAllTestRunReportsDeviceResult(Integer testRunNo,String productName,String productVersionName,String testRunTriggeredTime){
		
		return testRunReportsDeviceResultDAO.list(testRunNo,productName,productVersionName,testRunTriggeredTime);
	}
	@Override
	@Transactional
	public List<TestRunReportsDeviceList> listAllTestRunReportsDeviceResult(Integer testRunNo,String productName,String productVersionName){
		
		return testRunReportsDeviceResultDAO.list(testRunNo,productName,productVersionName);
	}
	@Override
	@Transactional
	public List<TestRunReportsDeviceList> listAllTestRunReportsDeviceResult(Integer testRunNo){
		
		return testRunReportsDeviceResultDAO.list(testRunNo);
	}
	@Override
	@Transactional
	public List<TestRunReportsDeviceList> listAllTestRunReportsDeviceResult(Integer testRunNo, Integer testRunConfigurationChildId){
		return testRunReportsDeviceResultDAO.list(testRunNo, testRunConfigurationChildId);
	}
	@Override
	@Transactional
	public int getTotalRecordsOfTestRunReportsDeviceListResult(Integer testRunNo,Integer testRunConfigurationChildId){
		return testRunReportsDeviceResultDAO.getTotalRecordsFilteredTestRunReportsDeviceList(testRunNo, testRunConfigurationChildId);
	}
	@Override
	@Transactional
	public int getTotalRecordsOfTestRunReportsDeviceListResult(Integer testRunNo,String productName,String productVersionName){
		
		return testRunReportsDeviceResultDAO.getTotalRecordsFilteredTestRunReportsDeviceList(testRunNo, productName, productVersionName);
	}
	@Override
	@Transactional
	public int getTotalRecordsOfTestRunReportsDeviceListResult(Integer testRunNo,String productName,String productVersionName,String testRunTriggeredTime){
		
		return testRunReportsDeviceResultDAO.getTotalRecordsFilteredTestRunReportsDeviceList(testRunNo, productName, productVersionName,testRunTriggeredTime);
	}

	
	
}
