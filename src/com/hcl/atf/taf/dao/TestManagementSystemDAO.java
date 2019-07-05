package com.hcl.atf.taf.dao;

import java.util.List;
import java.util.Set;

import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestManagementSystemMapping;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPSummaryDTO;

public interface TestManagementSystemDAO  {
	
	//Methods for retrieve / update test management system details
	void add (TestManagementSystem testManagementSystem);
	void update (TestManagementSystem testManagementSystem);
	void delete (TestManagementSystem testManagementSystem);
	TestManagementSystem getByTestManagementSystemId(int testManagementSystemId);
	List<TestManagementSystem> listTestManagementSystem(int productId);
	int getTestManagementSystemId(int productId, String testManagementSystemName, String testManagementSystemVersion);
	List<WorkPackageTCEPSummaryDTO> exportSystemNameCodeListDAO(Integer testCaseExecutionResultId);
	
	//Methods for retrieve / update test management system mapping details	
	void addTestManagementSystemMapping (TestManagementSystemMapping testManagementSystemMapping);
	void updateTestManagementSystemMapping (TestManagementSystemMapping testManagementSystemMapping);
	void deleteTestManagementSystemMapping (TestManagementSystemMapping testManagementSystemMapping);
	List<TestManagementSystemMapping> listTestManagementSystemMapping(int testManagementSystemId);
	TestManagementSystemMapping getByTestManagementSystemMappingID(int testManagementSystemMappingId);
	String getTestSystemMappingValue(int testManagementSystemId, int productId, String mappingType);
	String getTestSystemMappingProductName(int testManagementSystemId, int productId);
	String getTestSystemMappingProductVersion(int testManagementSystemId, int productId);
	TestManagementSystem getByProductId(int productId);
	TestManagementSystemMapping getByTestManagementSystemMappingId(int testManagementSystemMappingId);
	int getTotalRecordsCount(int productId);
	TestManagementSystem getTMSByProduct(int tmsId,int productId);
	TestManagementSystem getPrimaryTMSByProductId(int productId);
	boolean checkIfTMSExists(String testManagementSystemName, int productId);
}
