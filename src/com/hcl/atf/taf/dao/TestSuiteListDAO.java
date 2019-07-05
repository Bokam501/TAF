package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestSuiteList;

public interface TestSuiteListDAO  {	 
	int add (TestSuiteList testSuiteList);
	int addTestSuiteMapping (int testSuiteId, int testSuiteIdtobeMapped);	
	void update (TestSuiteList testSuiteList);
	void delete (TestSuiteList testSuiteList);
	List<TestSuiteList> list();
	List<TestSuiteList> list(int status);
	TestSuiteList getByTestSuiteId(int testSuitId);
	List<TestSuiteList> list(int startIndex, int pageSize);  
	Set<TestSuiteList> getTestSuiteByProductId(int productId);
	int getTotalRecords();
	
    public List<TestSuiteList> list(int productVersionListId,String[] parameters);
	void delete(Set<TestSuiteList> testSuiteLists);
	List<TestSuiteList> list(int startIndex, int pageSize, int status);
	void reactivate(Set<TestSuiteList> testSuiteLists);
	void reactivate(TestSuiteList testSuiteList);	
	//Changes for Test Management tools integration
	TestSuiteList getByTestSuiteCode(String testSuiteCode);
	TestSuiteList getByProductTestSuiteCode(int productId, String testSuiteCode);
	void updateTestSuite(TestSuiteList testSuiteList);
	List<TestSuiteList> filterTestSuites(int jtStartIndex, int jtPageSize, Integer productId);
	public List<TestSuiteList> getByProductId(Integer startIndex, Integer pageSize, int productId);
	public List<TestSuiteList> getByProductId(int productId);
	public List<TestSuiteList> getByProductVersionId(int startIndex, int pageSize, int productVersionListId);
	List<TestSuiteList> listMappedTestSuite(int testSuiteId);	
	List<TestSuiteList> getTestSuiteByProductId(Integer productId,Integer testSuiteExecutionId);
	List<TestSuiteList> getByProductVersionId(Integer jtStartIndex,Integer jtPageSize,Integer versionId,Integer testRunPlanId);
	boolean isVersionTestSuiteExistingByName(String testSuiteName, Integer versionId);
	ProductVersionListMaster getLatestProductVersion(ProductMaster productMaster);
	List<TestCaseList> listTestCaseByTestSuite(int testSuiteId, int jtStartIndex, int jtPageSize);
	Integer countAllProductTestCaseSteps(Date startDate, Date endDate);
	List<TestCaseStepsList> listAllProductTestCaseStepsByLastSyncDate(int startIndex, int pageSize, Date startDate, Date endDate);
	List<TestSuiteList> returnVersionTestSuiteId(String testSuiteName,Integer versionId);
	Integer countAllTestSuites(Date startDate, Date endDate);
	List<TestSuiteList> listAllTestSuites(int startIndex, int pageSize,Date startDate, Date endDate);
	List<TestSuiteList> getTestSuitesMappedToTestCaseByTestCaseId(int testCaseId, Integer startIndex, Integer pageSize);
	TestSuiteList getByTestSuiteName(String testSuiteName);
	TestSuiteList getByProductIdAndTestSuiteNandAndTestSuiteCode(int productId,String testSuiteName,String testSuiteCode);
	TestSuiteList getTestSuiteByProductIdOrTestSuiteNandOrTestSuiteCode(int productId,String testSuiteName,String testSuiteCode);
}