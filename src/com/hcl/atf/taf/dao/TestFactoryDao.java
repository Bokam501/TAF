package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.EngagementTypeMaster;
import com.hcl.atf.taf.model.ProductMode;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestfactoryResourcePool;

public interface TestFactoryDao {

	public List<TestFactory> getTestFactoryList(int testFactoryLabId, int status,int filter);
	public List<TestFactory> getTestFactoryListByLabAndUser(int testFactoryLabId, int status, int userId,int testFactoryId,int filterstatus);
	public List<TestFactory> list(int testFactoryLabId);
	public List<TestFactory> listByTestFactoryId(int testFactoryId);
	public List<TestFactory> listByTestFactoryIdAndLabId(int testFactoryId,	int testFactoryLabId);
	public TestFactory getTestFactoryById(Integer testFactoryId);
	public List<TestFactory> getTestFactoryList();
	public List<TestfactoryResourcePool> getResourcePoolListbyTestFactoryId(int testFactoryId);
	public List<TestFactory> getTestFactoriesByTestFactoryManagerId(int testFactoryManagerId);
	public List<TestFactory> getTestFactoriesByProductId(int productId);
	public TestFactory getTestFactoryByWorkPackageId(int workPackageId);
	
	public TestfactoryResourcePool mapRespoolTestfactory(Integer testFactoryId,
			Integer resourcePoolId, String action);
	
	int add(TestFactory testFactory);
	void update(TestFactory testFactory);
	
	boolean isTestFactoryExistingByName(TestFactory testFactory);
	boolean isTestFactoryExistingByNameForUpdate(TestFactory testFactory, int testFactoryId);
	TestFactory getTestFactoryByName(String testFactoryName);
	public List<EngagementTypeMaster> listEngagementTypes();
	public EngagementTypeMaster getEngagementTypeById(int engagementTypeId);
	int getEngagementTypeIdBytestfactoryId(int testfactoryId);
	List<ProductMode> getmodelist();
	public TestFactory getResourcePoolShowHideTab(Integer testFactoryId);
	public Integer countAllTestFactory(Date startDate, Date endDate);
	public List<TestFactory> listAllTestFactoryByLastSyncDate(int startIndex,int pageSize, Date startDate, Date endDate);
	public List<TestFactory> getEngagementByUserAndCustomerProduct(Integer userId, Integer userRoleId, Integer customerId, Integer activeStatus);
	public List<TestFactory> getEngagementListByUserId(Integer userId);
	public int addEngagement(EngagementTypeMaster engagementTypeMaster);
	
}
