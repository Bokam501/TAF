package com.hcl.atf.taf.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DeviceListDAO;
import com.hcl.atf.taf.dao.HostHeartbeatDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.TestEnvironmentDAO;
import com.hcl.atf.taf.dao.TestExecutionResultDAO;
import com.hcl.atf.taf.dao.TestRunConfigurationChildDAO;
import com.hcl.atf.taf.dao.TestRunConfigurationParentDAO;
import com.hcl.atf.taf.dao.TestRunListDAO;
import com.hcl.atf.taf.dao.TestRunSelectedDeviceListDAO;
import com.hcl.atf.taf.dao.TrccExecutionPlansCommonDAO;
import com.hcl.atf.taf.integration.testManagementSystem.TAFTestManagementSystemIntegrator;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.json.JsonTestExecutionResult;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.TestExecutionResultAutomatedService;
import com.hcl.atf.taf.service.TestReportService;
@Service
public class TestExecutionResultAutomatedServiceImpl implements TestExecutionResultAutomatedService {	 
	@Autowired
	private TestExecutionResultDAO testExecutionResultDAO;
	@Autowired
	private TestRunListDAO testRunListDAO;
	@Autowired
	private TestRunSelectedDeviceListDAO testRunSelectedDeviceListDAO;
	@Autowired
	private TestRunConfigurationChildDAO testRunConfigurationChildDAO;
	@Autowired
	private TestRunConfigurationParentDAO testRunConfigurationParentDAO;
	@Autowired
	private DeviceListDAO deviceListDAO;
	@Autowired
	private HostHeartbeatDAO hostHeartbeatDAO;
	@Autowired
	private TestReportService testReportService;
	@Autowired
	private EmailService eMailService;
	@Autowired
	private TestEnvironmentDAO testEnvironmentDAO;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private TAFTestManagementSystemIntegrator tafTestManagementIntegrator;	
	@Autowired
	private TestCaseListDAO testCaseListDAO;
	@Autowired
	TrccExecutionPlansCommonDAO trccExecutionPlansCommonDAO;
	
	

	@Override
	@Transactional
	public TestExecutionResult addTestExecutionResult(TestExecutionResult testExecutionResult) {
		
		return testExecutionResultDAO.add(testExecutionResult);
	}
	
	@Override
	@Transactional
	public TestExecutionResult processTestExecutionResult(TestExecutionResult testExecutionResult, JsonTestExecutionResult jsonTestExecutionResult) {
		
		return testExecutionResultDAO.processTestExecutionResult(testExecutionResult, jsonTestExecutionResult);
	}

}
