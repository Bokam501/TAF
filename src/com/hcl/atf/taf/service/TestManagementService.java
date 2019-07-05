package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestManagementSystemMapping;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPSummaryDTO;

public interface TestManagementService {
	
	//Methods for retrieve / update test management system details
	void addTestManagementSystem (TestManagementSystem testManagementSystem);
	void updateTestManagementSystem (TestManagementSystem testManagementSystem);
	void deleteTestManagementSystem (int testManagementSystemId);
	List<TestManagementSystem> listTestManagementSystem(int productId);
	TestManagementSystem getByTestManagementSystemId(int testManagementSystemId);
	int getTestManagementSystemId(int productId, String testManagementSystemName, String testManagementSystemVersion);
	int getTotalRecordsCount(int productId);
	List<WorkPackageTCEPSummaryDTO> listExportSystemNameAndCode(Integer testCaseExecutionResultId);

	//Methods for retrieve / update test management system mapping details	
	void addTestManagementSystemMapping (TestManagementSystemMapping testManagementSystemMapping);
	void updateTestManagementSystemMapping (TestManagementSystemMapping testManagementSystemMapping);
	String getTestSystemMappingValue(int testManagementSystemId, int productId, String mappingType);
	String getTestSystemMappingProductName(int testManagementSystemId, int productId);
	String getTestSystemMappingProductVersion(int testManagementSystemId, int productId);
	void deleteTestManagementSystemMapping (int testManagementSystemMappingId);
	List<TestManagementSystemMapping> listTestManagementSystemMapping(int testManagementSystemId);
	TestManagementSystemMapping getByTestManagementSystemMappingId(int testManagementSystemMappingId);
	TestManagementSystem getTMSByProduct(int tmsId,int productId);
	TestManagementSystem getPrimaryTMSByProductId(int productId);
	boolean checkIfTMSExists(String testManagementSystemName, int productId);

}
