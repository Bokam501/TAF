package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.hcl.atf.taf.model.DeviceLab;
import com.hcl.atf.taf.model.DeviceType;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.MetricsMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductMode;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.RunConfigurationTSHasTC;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunPlanGroup;
import com.hcl.atf.taf.model.TestRunPlanTSHasTC;
import com.hcl.atf.taf.model.TestRunPlangroupHasTestRunPlan;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserRoles;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.dto.ProductSummaryDTO;
import com.hcl.atf.taf.model.dto.ResourceExperienceSummaryDTO;
import com.hcl.atf.taf.model.json.JsonUserRoles;

public interface ProductMasterDAO  {	 
	Integer add (ProductMaster productMaster);
	void update (ProductMaster productMaster);
	void delete (ProductMaster productMaster);
	List<ProductMaster> list(boolean initialize);
	List<ProductMaster> list(int startIndex, int pageSize);
	List<ProductMaster> listbyCustomerId(int customerId, Integer startIndex, Integer pageSize);
	List<ProductMaster> list(String[] parameters);
	
	ProductMaster getByProductId(int productId);
	int getTotalRecords();
	
	boolean isProductExistingByName(String productName);
	void reactivate(ProductMaster productMaster);
	List<ProductMaster> listByNames();
	int getTotalRecordCount();
	boolean isProductExistingByName(ProductMaster productMaster);
	ProductMaster getProductByName(String productName);
	void addProductUserRole(ProductUserRole productUserRole);
	List<ProductUserRole> listProductUserRole(int productId,int jtStartIndex , int jtPageSize);
	List<UserRoleMaster> getAllRoles();
	boolean isProductUserRoleExits(int productId,int userId,int roleId);
	ProductUserRole getProductUserRole(int productId, int userId);
	Environment getEnvironmentByName(String environmentName);
	Environment getEnvironmentByNameByProduct(String environmentName,String productId);
	List<ProductUserRole> listProductUserRole(int productId);
	List<ProductMaster> listProductsByTestFactoryId(int testFactoryId);
	List<ProductMaster> getProductsByWorkPackageForUserId(int userRoleId,int userId,int filter);
	List<ProductMaster> getProductsByProductUserRoleForUserId(int userRoleId,int userId,int filter);
	List<ProductMaster> getProductsByTestFactoryId(int testFactoryId);
	List<UserRoleMaster> getRolesBasedResource();
	UserRoleMaster getRolesByUserRoleId(int userRoleId);
	List<ProductMaster> listProductsByTestFactoryIdAndProductMode(int testFactoryId, int productModeId);
	UserRoles mapUserWithRoles(JsonUserRoles jsonUserRoles);
	public void updateUserRoles(UserRoles userRoles);
	UserRoles getUserRolewithuserRoleIdUserId(int userId, int userRoleId);
	List<UserRoles> listUserRoles(int startIndex, int pageSize,Integer userId);
	
	List<ProductMaster> listbyCustomerIdTestFactoryId(int testFactoryId, int customerId, int jtStartIndex, int jtPageSize);
	List<ProductMaster> getCustmersbyProductId(Integer testFactoryId, Integer productId);
	List<ProductMaster> getCustmersbyProductIdStatus(int testFactoryId, int productId, int status, int jtStartIndex, int jtPageSize);
	List<ResourceExperienceSummaryDTO> listResourceExperienceOfSelectedProduct(Integer productId, Integer productVersionId, Integer userId);
	List<WorkPackageTestCaseExecutionPlan> listWpTCExecutionOfUserforSelectedProduct(Integer productId, Integer productVersionId, Integer userId);
	List<ProductType> listProductTyper();
	ProductType getProductTypeById(Integer productTypeId);
	List<DeviceType> getDeviceType();
	List<DeviceLab> listDeviceLab();
	Integer addTestRunplan(TestRunPlan testRunPlan);
	List<TestRunPlan> listTestRunPlanByProductVersionId(Integer productVersionId);
	TestRunPlan getTestRunPlanById(Integer testRunPlanId);
	void mapTestRunPlanWithTestRunconfiguration(Integer testRunPlanId,Integer runConfigurationId, String action);
	void mapTestRunPlanWithTestSuite(Integer testRunPlanId,Integer testSuiteId, String action);
	TestRunPlan getTestRunPlanBytestRunPlanId(Integer testRunPlanId);
	ProductMaster productShowHideTab(Integer testFactoryId, Integer productId);
	boolean validateUserRole(UserRoles ur,JsonUserRoles jsonUserRoles);
	HostList getHostListById(Integer hostListId);
	Set<RunConfiguration> getRunConfigurationList(Integer testRunPlanId,Integer type,Integer environmentCombinationId);
	Set<RunConfiguration> getRunConfigurationList(Integer testRunPlanId,Integer type);

	Set<RunConfiguration> getRunConfigurationListByWP(Integer workpackageId,Integer type,Integer environmentCombinationId);
	List<UserRoleMaster> getProductUserRoles(Integer typeFilter);
	ProductMode getProductModeById(Integer productModeId);
	  void mapMobileWithProduct(Integer productId, Integer deviceId,String type);
	  void mapServerWithProduct(Integer productId, Integer hostId,String type);
	ProductMaster getCustomerByProductId(int productId);
	
	void mapTestRunPlanWithTestCase(Integer testRunPlanId,Integer testCaseId,String action);
	void mapTestRunPlanWithFeature(Integer testRunPlanId,Integer featureId,String action);
	ProductSummaryDTO getProductSummary(Integer productId);
	List<TestRunPlan> listTestRunPlanByProductId(Integer productId);
	void addtestRunPlanGroup(TestRunPlanGroup testRunPlanGroup);
	List<TestRunPlanGroup> listTestRunPlanGroup(int productVersionId, int productId);
	void update(TestRunPlanGroup testRunPlanGroupFromUI);
	List<TestRunPlangroupHasTestRunPlan> listTestRunPlanGroupMap(Integer testRunPlanGroupId);
	TestRunPlan addtestrunpalngroupMapping(int testRunPlanId, int testRunPlanGroupId,
			String maporunmap);
	TestRunPlanGroup getTestRunPlanGroupById(int testRunPlanGroupId);
	List<ProductMaster> getProductsByAllocation(int userId);
	List<TestRunPlan> listTestRunPlanByProductVersionId(int productversionId,int testRunPlanGroupId);

	TestRunPlan getTestRunPlanByName(String name);
	
	void update(TestRunPlangroupHasTestRunPlan testRunPlanGroupHasFromUI);
	boolean isProductExitsInsameTestFactory(Integer testFactoryId, String productName);
	void updateTestRunplan(TestRunPlan testRunPlan);
	List<ProductMaster> listProductByEngagementType(int engagementTypeId);
	   void mapTestSuiteTestCasesTestRunPlan(int testRunPlanId,int testSuiteId,int testCaseId,String type);
		List<TestCaseList> getTestSuiteTestCaseMapped(int testRunPlanId,int testSuiteId);
		List<RunConfiguration> listRunConfigurationByTestRunPlanId(Integer testRunPlanId,Integer jtStartIndex,Integer jtPageSize);

		List<MetricsMaster> listMetrics();
		Set<RunConfiguration> getRunConfigurationListByStatus(Integer testRunPlanId, Integer type, Integer status);
		Set<RunConfiguration> getRunConfigurationListOfWPrcStatus(Integer workpackageId, Integer runConfigStatus);
		Set<RunConfiguration> getRunConfigurationListOfTestRunPlanrcStatus(Integer testRunPlanId, Integer runConfigStatus);
		List<ProductMaster> getProductsByAssigneeOrReviwerForUserId(int userRoleId, int userId, int filter);
		Set<RunConfiguration> getRunConfigurationListOfTestRunPlanrcStatusDeviceorHost(Integer testRunPlanId, Integer runConfigStatus, Integer prodType);
		Integer countAllProduct(Date startDate,Date endDate);
		List<ProductMaster> listAllProductsByLastSyncDate(int startIndex,int pageSize, Date startDate, Date endDate);
		List<TestRunPlan> listTestRunPlanBytestFactorProductorVersion(Integer productVersionId, Integer productId, Integer testFactoryId);
		ProductSummaryDTO getProductSummaryByProductId(Integer productId);
		List<Integer> listTestSuites(int testRunPlanId);
		
		List<ProductMaster> getProductsByProductUserRoleForTestingTeamUserId(
				int userRoleId, int userId,int filter);
		List<Object[]> fixFailReportDAO(Integer productId);
		String getDefectSeverityName(int bugId);
		List<Object[]> testFixFailReportDAO(Integer productId);
		HashMap<String, String> getBugDetails(int testCaseExecutionResultId);
		List<ProductMaster> getUserRoleBasedProductByTestFactoryId(int testFactoryId, int productId, int userId, int status,
				int jtStartIndex, int jtPageSize);
		Integer getUsersProductCountByTestFactoryId(int testFactoryId,
				int productId, int userId, int status, int jtStartIndex,
				int jtPageSize, int userRoleId);
		
		ProductMaster getProductDetailsById(Integer productId);
		ProductMaster getProductDetailsByIdWithDevicesHostList(Integer productId);
		ProductMaster getProductDetailsByIdWithDevicesList(Integer productId);
		ProductMaster getProductDetailsByIdWithHostList(Integer productId);
		List<ProductMaster> getProductsByProductUserRoleForUserIdNotByRole(int userRoleId, int userId, int filter);
		List<TestRunPlan> listTestRunPlanBytestCaseId(int testCaseId);
		List<ProductMaster> getProductsByProductUserRoleForUserIdOfTestFactory(
				int userRoleId, int userId, int testFactoryId, int filter);
		List<ProductMaster> getProductByUserCustomerAndEngagement(Integer userId,	Integer userRoleId, Integer customerId, Integer engagementId, Integer activeStatus);
		List<ProductMaster> getProductsOfEngagementByUserId(Integer engagementId, Integer userId);
		Integer getProductsCountByTestFactoryId(int testFactoryId, int jtStartIndex, int jtPageSize);
		List<ProductMaster> getProductsByEngagementId(List<Integer> testFactoryId);
		ProductMaster getProductByNameAndTestfactoryId(String product,Integer testFactoryId);
		void mapTestSuiteWithRunConfiguration(Integer runConfigId,Integer testSuiteId, String action);
		List<RunConfiguration> listRunConfiguration(Integer productId);
		void mapTestSuiteTestCasesRunConfiguration(int runConfigId,int testSuiteId, int testCaseId, String type);
		List<TestCaseList> getRunConfigTestSuiteTestCaseMapped(int runConfigId,int testSuiteId);
		Integer getTotalTestCaseCountForATestRunPlan(Integer testRunPlanId);
		boolean isTestCaseAlreadyMapped(Integer testRunPlanId,Integer testSuiteId, Integer testCaseId);
		void unMapRunConfigurationTestRunPlan(int testrunplanid,	int runconfigid);
		boolean isTestConfigurationTestCaseAlreadyMapped(Integer testConfigurationId, Integer testSuiteId, Integer testCaseId);
		void mapTestConfigurationTestSuiteTestCase(int testConfigId, int testSuiteId, List<TestCaseList> testCaseLists, String type);
		Set<RunConfiguration> getRunConfigurationList(Integer testRunPlanId,Integer type, Integer environmentCombinationId, Integer hostId,	Integer deviceId);
		boolean hostHasTestConfigurations(int productId, int hostId);
		boolean deviceHasTestConfigurations(int productId, int deviceId);
		List<TestRunPlan> getTestPlansByProductBuildIds(String productBuildIds);
		ProductType getProductTypeByName(String productTypeName);
		List<RunConfiguration> getRunConfigurationListByEnvironmentCombination(Integer environmentCombinationId);
		ProductMaster getProductExitsInsameTestFactory(Integer testFactoryId,String productName);
		List<ProductMaster> getProductDetailsByUserId(int userId);
		List<TestCaseList> getRunConfigTestCasesByTestSuite(Integer testSuiteId);
		List<RunConfigurationTSHasTC> getRunConfigTestCaseObjectByTestSuite(Integer testSuiteId);
		boolean isUserPermissionByProductIdandUserId(Integer productId, Integer userId,Integer roleId);
		TestRunPlan getTestRunPlanBytestRunPlanNameAndProductBuild(String testRunPlanName, Integer productBuildId);
		TestRunPlan getFirstTestRunPlanByTestPlanGroupId(Integer testPlanGroupId);
		TestRunPlangroupHasTestRunPlan getTestRunPlanGroupHasTestPlanByTestPlanId(Integer testRunPlanId);
		TestRunPlanTSHasTC getTestPlanTestSuiteTestCase(Integer testRunPlanId,Integer testSuiteId, Integer testCaseId);
}
