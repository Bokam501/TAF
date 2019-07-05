package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestManagementSystemDAO;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestManagementSystemMapping;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPSummaryDTO;
import com.hcl.atf.taf.service.TestManagementService;
@Service
public class TestManagementServiceImpl  implements TestManagementService{	 
	@Autowired
	private TestManagementSystemDAO testManagementSystemDAO;
	
	
	
	@Override
	@Transactional
	public void addTestManagementSystem(TestManagementSystem testManagementSystem) {
		testManagementSystemDAO.add(testManagementSystem);
	}
	
	@Override
	@Transactional
	public void updateTestManagementSystem(TestManagementSystem testManagementSystem) {
		testManagementSystemDAO.update(testManagementSystem);
		
	}
	
	@Override
	@Transactional
	public void deleteTestManagementSystem(int testManagementSystemId) {
		testManagementSystemDAO.delete(getByTestManagementSystemId(testManagementSystemId));
		
	}
	
	@Override
	@Transactional
	public List<TestManagementSystem> listTestManagementSystem(int productId) {
		return testManagementSystemDAO.listTestManagementSystem(productId);
		
	}
	
	@Override
	@Transactional
	public int getTotalRecordsCount(int productId) {
		return testManagementSystemDAO.getTotalRecordsCount(productId);
	}
	
	@Override
	@Transactional
	public TestManagementSystem getByTestManagementSystemId(int testManagementSystemMappingId) {
		return testManagementSystemDAO.getByTestManagementSystemId(testManagementSystemMappingId);
	}
	
	@Override
	@Transactional
	public int getTestManagementSystemId(int productId, String testManagementSystemName, String testManagementSystemVersion) {
		return testManagementSystemDAO.getTestManagementSystemId(productId, testManagementSystemName, testManagementSystemVersion);
	}
	
	@Override
	@Transactional
	public void addTestManagementSystemMapping(TestManagementSystemMapping testManagementSystemMapping) {
		testManagementSystemDAO.addTestManagementSystemMapping(testManagementSystemMapping);
		
	}
	
	@Override
	@Transactional
	public void updateTestManagementSystemMapping(TestManagementSystemMapping testManagementSystemMapping) {
		testManagementSystemDAO.updateTestManagementSystemMapping(testManagementSystemMapping);
	}
	
	@Override
	@Transactional
	public void deleteTestManagementSystemMapping(int testManagementSystemMappingId) {
		testManagementSystemDAO.deleteTestManagementSystemMapping(getByTestManagementSystemMappingId(testManagementSystemMappingId));
		
	}
	
	@Override
	@Transactional
	public List<TestManagementSystemMapping> listTestManagementSystemMapping(int testManagementSystemId) {
		return testManagementSystemDAO.listTestManagementSystemMapping(testManagementSystemId);
	}
	
	@Override
	@Transactional
	public TestManagementSystemMapping getByTestManagementSystemMappingId(int testManagementSystemMappingId) {
		return testManagementSystemDAO.getByTestManagementSystemMappingId(testManagementSystemMappingId);
	}
	
	@Override
	@Transactional
	public String getTestSystemMappingValue(int testManagementSystemId, int productId, String mappingType) {
		return getTestSystemMappingValue(testManagementSystemId, productId, mappingType);
	}
	
	@Override
	@Transactional
	public String getTestSystemMappingProductName(int testManagementSystemId, int productId) {
		return testManagementSystemDAO.getTestSystemMappingProductName(testManagementSystemId, productId);
	}
	
	@Override
	@Transactional
	public String getTestSystemMappingProductVersion(int testManagementSystemId, int productId) {
		return testManagementSystemDAO.getTestSystemMappingProductVersion(testManagementSystemId, productId);
	}

	@Override
	@Transactional
	public TestManagementSystem getTMSByProduct(int tmsId, int productId) {
		return testManagementSystemDAO.getTMSByProduct(tmsId, productId);
	}

	@Override
	@Transactional
	public TestManagementSystem getPrimaryTMSByProductId(int productId) {
		return testManagementSystemDAO.getPrimaryTMSByProductId(productId);
	}
	
	@Override
	@Transactional
	public boolean checkIfTMSExists(String testManagementSystemName, int productId) {
		
		return testManagementSystemDAO.checkIfTMSExists(testManagementSystemName, productId);
	}

	@Override
	@Transactional
	public List<WorkPackageTCEPSummaryDTO> listExportSystemNameAndCode(Integer testCaseExecutionResultId) {
		return testManagementSystemDAO.exportSystemNameCodeListDAO(testCaseExecutionResultId);
	}

}
