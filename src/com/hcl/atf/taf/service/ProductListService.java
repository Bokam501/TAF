package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hcl.atf.taf.model.DeviceLab;
import com.hcl.atf.taf.model.DeviceType;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.MetricsMaster;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductFeatureProductBuildMapping;
import com.hcl.atf.taf.model.ProductLocale;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductMode;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.RunConfigurationTSHasTC;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestFactoryProductCoreResource;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunPlanGroup;
import com.hcl.atf.taf.model.TestRunPlanTSHasTC;
import com.hcl.atf.taf.model.TestRunPlangroupHasTestRunPlan;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserRoles;
import com.hcl.atf.taf.model.dto.DefectWeeklyReportDTO;
import com.hcl.atf.taf.model.dto.ProductFeatureListDTO;
import com.hcl.atf.taf.model.dto.ProductSummaryDTO;
import com.hcl.atf.taf.model.dto.VerificationResult;
import com.hcl.atf.taf.model.json.JsonResourceDailyPerformance;
import com.hcl.atf.taf.model.json.JsonResourceExperienceSummary;
import com.hcl.atf.taf.model.json.JsonRiskHazardTraceabilityMatrix;
import com.hcl.atf.taf.model.json.JsonTestCaseExecutionResult;
import com.hcl.atf.taf.model.json.JsonTestExecutionResultBugList;
import com.hcl.atf.taf.model.json.JsonUserRoles;
import com.hcl.atf.taf.model.json.JsonWorkPackage;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;

public interface ProductListService {

	/*For Product Build*/
	Integer addProductBuild (ProductBuild productBuild);
	void updateProductBuild (ProductBuild productBuild);
	void deleteProductBuild (int productBuildId);
	List<ProductBuild> listProductBuild();
	List<ProductBuild> listProductBuild(int productVersionListId);
	List<ProductBuild> listProductBuilds(int productVersionListId, int status);
	List<ProductBuild> listProductBuildPaginate(int startIndex, int pageSize);
	List<ProductBuild> listProductBuild(int productVersionListId,int startIndex, int pageSize);
	int getTotalRecordsOfProductBuild();
	ProductBuild getProductBuildById(int productBuildId, int listOrObjInitialize);
	/*For Product Build*/
	
	void addProductVersion (ProductVersionListMaster productVersionListMaster);
	void updateProductVersion (ProductVersionListMaster productVersionListMaster);
	void deleteProductVersion (int productVersionListId);
	List<ProductVersionListMaster> listProductVersion();
	List<ProductVersionListMaster> listProductVersion(int productId);
	List<ProductVersionListMaster> listProductVersions(int productId, int status);
	List<ProductVersionListMaster> listProductVersionPaginate(int startIndex, int pageSize);
	List<ProductVersionListMaster> listProductVersion(int productId,int startIndex, int pageSize);
	List<ProductMaster> productsList();
	int getTotalRecordsOfProduct();
	ProductVersionListMaster getProductVersionListMasterById(int productVersionMasterListId);
	
	Integer addProduct (ProductMaster productMaster);
	void updateProduct (ProductMaster productMaster);
	void deleteProduct (int productId);
	List<ProductMaster> listProduct();
	List<ProductMaster> listProduct(int startIndex, int pageSize);
	List<ProductMaster> listProductsbyCustomerID(int customerId, Integer startIndex, Integer pageSize);
	List<ProductVersionListMaster> productVersionsList(int productId);
	int getTotalRecordsOfProductVersion();

	boolean isProductExistsByName(String productName);
	boolean isProductExistingByName(String productName);
	boolean isProductVersionExistingByName(ProductVersionListMaster productVersionListMaster);
	boolean isProductVersionExistingByNameForUpdate(ProductVersionListMaster productVersionListMaster, int productVersionListId);
	boolean isProductBuildExistingByName(ProductBuild productBuild);
	void reactivateProductBuild(int productBuildId);
	void reactivateProductVersion(int productVersionListId);
	void reactivateProduct(int productId);
	
	List<ProductMaster> productsListByNames();
	ProductMaster getProductById(int productId);
	
	//Changes for Product Feature
	void addProductFeature (ProductFeature productFeature);
	void updateProductFeature (ProductFeature productFeature);
	void deleteProductFeature (ProductFeature productFeature);
	List<ProductFeature> list();
	List<ProductFeature> getFeatureListByProductId(Integer productId, Integer featureStatus, Integer jtStartIndex, Integer jtPageSize);
	List<ProductFeature> getFeaturesMappedToTestCase(Integer testCaseId);
	List<ProductFeature> getFeatureListExcludingChildByparentFeatureId(Integer productId, Integer parentFeatureId);
	ProductFeature getByProductFeatureName(String productFeatureName);
	ProductFeature getByProductFeatureId(int productFeatureId);
	boolean isProductFeatureExistingByName(String string);
	boolean isProductFeatureExistingByName(String productFeatureName, Integer productId, Integer productFeatureId);
	int getTotalRecordCount();
	TestCaseList updateProductFeatureTestCases(int testCaseId, int productFeatureId);
	
	ProductMaster listProductByName(String productName);
	ProductVersionListMaster getProductVersionListMasterByName(String productVersionName);
	ProductBuild getProductBuildByName(String productBuildName);
	ProductMaster getProductByName(String productName);
	
	/*For Product Environment*/	
	List<Environment> getEnvironmentListByProductId(Integer productId);
	List<Environment> getEnvironmentListByProductIdAndStatus(Integer productId,Integer status);
	
	boolean isProductEnvironmentExistingByName(Environment environment);
	void addProductEnvironment(Environment environment);
	void updateProductEnvironment(Environment environment);
	void deleteProductEnvironment(Environment environment);
	Environment getEnvironmentById(Integer environmentId);
	Environment getEnvironmentByName(String environmentName);
	void addProductUserRole(ProductUserRole productUserRole);
	List<ProductUserRole> listProductUserRole(int productId,int jtStartIndex , int jtPageSize);
	List<UserRoleMaster> getAllRoles();
	boolean isProductUserRoleExits(int productId,int userId,int roleId);
	boolean isUserPermissionByProductIdandUserId(int productId,int userId,int roleId);
	
	/* Product Locale*/
	
	List<ProductLocale> getProductLocaleListByProductId(int productMasterId);
	
	void addProductLocale(ProductLocale locale);
	
	
	void updateProductLocale(ProductLocale locale);
	
	
	ProductLocale getLocaleByName(String localeName);
	
	ProductLocale getLocaleById(Integer productLocaleId);
	
	ProductLocale getLocaleByNameByProduct(String localeName,String productId);

	Environment getEnvironmentByNameByProduct(String environmentName,String productId);
	List<ProductUserRole> listProductUserRole(int productId);
	
	ProductUserRole getProductUserRoleByUserId(int userId);
	/* ProductCoreResources  */
	public List<TestFactoryProductCoreResource> getProductCoreResourcesList(
			int productId, Integer jtStartIndex, Integer jtPageSize);
	public void addProductCoreResource(TestFactoryProductCoreResource coreResouce);
	public TestFactoryProductCoreResource getCoreResourceById(
			Integer testFactoryProductCoreResourceId);
	public void updateProductCoreResource(
			TestFactoryProductCoreResource coreResourceFromUI);	
	Map<Integer, Integer> getProductCoreResourcesCountByRole(int productId);
	List<ProductMaster> listProductsByTestFactoryId(int testFactoryId);
	List<ProductMaster> getProductsByWorkPackageForUserId(int userRoleId,int userId,int filter);
	List<ProductMaster> getProductsByProductUserRoleForUserId(int userRoleId,int userId,int filter);
	List<ProductMaster> getProductsByTestFactoryId(int testFactoryId);
	List<UserRoleMaster> getRolesBasedResource();
	UserRoleMaster getRolesByUserRoleId(int userRoleId);
	
	UserRoles mapUserWithRoles(JsonUserRoles jsonUserRoles);
	public void updateUserRoles(UserRoles userRoles);
	UserRoles getUserRolewithuserRoleIdUserId(int userId, int userRoleId);
	List<UserRoles> listUserRoles(int startIndex, int pageSize,Integer userId);
	
	List<ProductMaster> listProductsbyCustomerIdTestFactoryId(int testFactoryId, int customerId, int jtStartIndex, int jtPageSize);
	ProductUserRole getProductUserRoleByUserIdAndProductId(int productId, int userId);
	List<JsonResourceExperienceSummary> listResourceExperienceOfSelectedProduct(Integer productId,Integer productVersionId, Integer userId,int jtStartIndex, int jtPageSize);
	List<JsonWorkPackage> listUserExperienceWorkPackagesDetails(Integer productId, Integer productVersionId, Integer userId,int jtStartIndex, int jtPageSize);
	List<JsonWorkPackageTestCaseExecutionPlan> listUserExperienceExecutedTCDetails(Integer productId, Integer productVersionId, Integer userId,int jtStartIndex, int jtPageSize);
	List<JsonTestExecutionResultBugList> listUserReportedDefectsDetails(Integer productId, Integer productVersionId, Integer userId, int jtStartIndex, int jtPageSize);
	List<JsonTestCaseExecutionResult> listApprovedDefectsDetails(Integer productId, Integer productVersionId, Integer userId,int jtStartIndex, int jtPageSize);
	List<JsonResourceDailyPerformance> getResourceAveragePerformanceDetails(List<JsonWorkPackage> listOfJsonWorkPackage,Integer userId,int jtStartIndex, int jtPageSize);
	List<ProductMaster> getCustmersbyProductId(int testFactoryId, int productId);
	List<ProductMaster> getCustmersbyProductIdStatus(int testFactoryId, int productId, int status, int jtStartIndex, int jtPageSize);
	void deleteEnvironmentCombination(EnvironmentCombination environmentCombination);
	List<ProductType> listProductTyper();
	ProductType getProductTypeById(Integer productTypeId);
	List<DeviceType> listDeviceType();
	List<DeviceLab> listDeviceLab();	
	RunConfiguration getRunConfigurationById(Integer runconfigId);
	Integer addTestRunplan(TestRunPlan testRunPlan);
	List<TestRunPlan> listTestRunPlanByProductVersionId(Integer productVersionId);
	TestRunPlan getTestRunPlanById(Integer testRunPlanId);
	TestRunPlan getTestRunPlanBytestRunPlanId(Integer testRunPlanId);
	void mapTestRunPlanWithTestRunconfiguration(Integer testRunPlanId,Integer runConfigurationId,String action);
	void mapTestRunPlanWithTestSuite(Integer testRunPlanId,Integer testSuiteId,String action);
	Boolean isUserAlreadyCoreResource(Integer productId, Integer userId, String fromDate, String toDate,TestFactoryProductCoreResource coreResourceFromDB);
	public ProductMaster getProductShowHideTab(Integer testFactoryId, Integer productId);
	boolean validateUserRole(UserRoles ur,JsonUserRoles jsonUserRoles);
	Set<RunConfiguration> getRunConfigurationList(Integer testRunPlanId,Integer type,Integer environmentCombinationId);
	Set<RunConfiguration> getRunConfigurationList(Integer testRunPlanId,Integer type);
	Set<RunConfiguration> getRunConfigurationListByWP(Integer workpackageId,Integer type,Integer environmentCombinationId);
	List<ProductTeamResources> getProductTeamResourcesList(int productId,Integer jtStartIndex, Integer jtPageSize);
	void addProductTeamResource(ProductTeamResources pTeamResource);
	Boolean isUserAlreadyProductTeamResource(Integer productId, Integer userId, String fromDate, String toDate, ProductTeamResources productTeamResourcesFromDB);
	ProductTeamResources getProductTeamResourceById(Integer productTeamResourceId);
	void updateProductTeamResource(ProductTeamResources productTeamResourcesFromUI);
	Map<Integer, Integer> getProductTeamResourcesCountByRole(int productId); 
	List<UserRoleMaster> getProductUserRoles(Integer typeFilter);
	TestCaseList updateProductFeatureTestCasesOneToMany(Integer testCaseId, Integer productFeatureId, String maporunmap);
	ProductMode getProductModeById(Integer productModeId);
	void removeProductTeamResourceMapping(ProductTeamResources productTeamResource);
	void mapMobileWithProduct(Integer productId, Integer deviceId,String type);
	void mapServerWithProduct(Integer productId, Integer hostId,String type);
	ProductMaster getCustomerByProductId(Integer productId);
	void mapTestRunPlanWithTestCase(Integer testRunPlanId,Integer testCaseId,String action);
	void mapTestRunPlanWithFeature(Integer testRunPlanId,Integer featureId,String action);
	ProductSummaryDTO getProductSummary(int productId);
	Integer getFeatureListSize(Integer productId);
	void addtestrunpalngroup(TestRunPlanGroup testRunPlanGroup);
	List<TestRunPlanGroup> listTestRunPlanGroup(int productVersionId, int productId);
	void update(TestRunPlanGroup testRunPlanGroupFromUI);
	List<TestRunPlangroupHasTestRunPlan> listTestRunPlanGroupMap(Integer testRunPlanGroupId);
	TestRunPlan addtestrunpalngroupMapping(int testRunPlanId, int testRunPlanGroupId,
			String maporunmap);
	TestRunPlanGroup getTestRunPlanGroupById(int testRunPlanGroupId);
	Integer getUnMappedTestCaseListCountOfFeatureByProductFeatureId(int productId, int productFeatureId);
	Integer getUnMappedTestCaseListCountOfFeatureByProductId(int productId);
	List<Object[]> getUnMappedTestCaseListByProductFeatureId(int productId, int productFeatureId, Integer jtStartIndex, Integer jtPageSize);	
	List<DefectWeeklyReportDTO> listDefectsWeeklyReport(int weekNo,int productId,int productVersionId,int productBuildId,int workPackageId);
	List<TestRunPlan> listTestRunPlanByProductVersionId(int productversionId,int testRunPlanGroupId);
	TestRunPlan getTestRunPlanByName(String name);

	boolean mapAndUnmapDCTestCases(Integer sourceDCId, Integer destinationDCId,	Integer tcId);
	void update(TestRunPlangroupHasTestRunPlan testRunPlanGroupHasFromUI);
	boolean isProductExitsInsameTestFactory(Integer testFactoryId,
			String productName);
	ProductFeature getByProductFeatureCode(int productFeatureId);
	ProductFeature getByProductFeatureCode(String productFeatureCode);
	ProductBuild getProductBuildIdWithCompleteInitialization(int productBuildId);
	List<ProductFeature> getFeatureListByProductId(Integer productId, Integer jtStartIndex, Integer jtPageSize, boolean initialize);
	List<ProductFeature> getFeatureListByEnagementId(Integer engagementId, Integer jtStartIndex, Integer jtPageSize, boolean initialize);
	
	boolean isProductFeatureExistingByFeatureCode(String productFeatureCode, ProductMaster product);
	void productFeaturesbatchImport(List<ProductFeatureListDTO> listOfFeatureDTOToUpdate,String tcAddOrUpdateAction);
	ProductFeature getByProductFeatureCode(String productFeatureCode, ProductMaster product);
	ProductFeature getByProductFeatureName(String productFeatureName, ProductMaster product);
	void updateTestRunPlan(TestRunPlan testRunPlan);
	List<ProductMaster> listProductByEngagementType(int engagementTypeId);
	public List<Object[]> getMappedTestCaseListByProductFeatureId(int productId, int productFeatureId, Integer jtStartIndex, Integer jtPageSize);
	   void mapTestSuiteTestCasesTestRunPlan(int testRunPlanId,int testSuiteId,int testCaseId,String type);
		List<TestCaseList> getTestSuiteTestCaseMapped(int testRunPlanId,int testSuiteId);
		List<RunConfiguration> listRunConfigurationByTestRunPlanId(Integer testRunPlanId,Integer jtStartIndex,Integer jtPageSize);

		List<MetricsMaster> listMetrics();

		Set<RunConfiguration> getRunConfigurationListByStatus(Integer testRunPlanId, Integer type, Integer status);
		Set<RunConfiguration> getRunConfigurationListOfWPrcStatus(Integer workpackageId, Integer runConfigStatus);
		Set<RunConfiguration> getRunConfigurationListOfTestRunPlanrcStatus(Integer testRunPlanId, Integer runConfigStatus);
		List<ProductBuild> listBuildsByProductId(Integer productId);		
		Set<RunConfiguration> getRunConfigurationListOfTestRunPlanrcStatusDeviceorHost(Integer testRunPlanId, Integer runConfigStatus, Integer prodType);
		int getProductTypeByVersionId(int productVersionListId, int productId, int workPackageId);
		List<ProductFeature> list(Integer startIndex, Integer pageSize, Date startDtae,Date endDate);
		Integer countProductFeatures(Date startDate, Date endDate);
		Integer countAllProduct(Date startDate, Date endDate);
		List<ProductMaster> listAllProductsByLastSyncDate(int startIndex, int pageSize, Date startDate,Date endDate);
		Integer countAllProductVersions(Date startDate,Date endDate);
		List<ProductVersionListMaster> listAllProductVersionByLastSyncDate(int startIndex, int pageSize, Date startDate, Date endDate);
		Integer countAllProductBuilds(Date startDate, Date endDate);
		Integer countAllProductTestCases(Date startDate,Date endDate);
		List<ProductBuild> listAllProductBuildByLastSyncDate(int startIndex, int pageSize, Date startDate, Date endDate);
		List<TestRunPlan> listTestRunPlanBytestFactorProductorVersion(Integer productVersionId, Integer productId, Integer testFactoryId);
		List<TestCaseList> listAllProductTestCasesByLastSyncDate(int startIndex,int pageSize, Date startDate,Date endDate);
		Integer countAllProductTestCaseSteps(Date startDate, Date endDate);
		List<TestCaseStepsList> listAllProductTestCaseStepsByLastSyncDate(int startIndex,int pageSize, Date startDate, Date endDate);
		Integer countAllBugs(Date startDate, Date endDate);
		Integer countAllProductUserRole(Date startDate, Date endDate);
		List<ProductUserRole> listAllPaginate(int i, int pageSize,Date startDate, Date endDate);
		
		List<Integer> listTestSuites(int testRunPlanId);
		List<JsonRiskHazardTraceabilityMatrix>  fixFailReportService(Integer productId);
		List<JsonRiskHazardTraceabilityMatrix> testFixFailReportService(Integer productId);
		Integer countAlladdAllProductTeamResources(Date startDate, Date endDate);
		List<ProductTeamResources> listAllProductTeam(int startIndex, int pageSize,Date startDate, Date endDate);
		
		public ProductFeature addFeature(ProductFeature productFeature);
		Boolean getTeamResourceByUserIdandProductIdandDate(Integer productId, Integer userId, Date aWpPsDate, Date aWpPeDate);
		List<ProductMaster> getUserRoleBasedProductByTestFactoryId(int testFactoryId, int productId, int userId, int status,
				int jtStartIndex, int jtPageSize);
		Integer getUsersProductCountByTestFactoryId(int testFactoryId, int productId, int userId, int status, int jtStartIndex,
				int jtPageSize, int userRoleId);
		ProductMaster getProductDetailsById(Integer productId);
		ProductMaster getProductDetailsByIdWithDevicesHostList(Integer productId);
		ProductMaster getProductDetailsByIdWithDevicesList(Integer productId);
		ProductMaster getProductDetailsByIdWithHostList(Integer productId);
		List<TestRunPlan> listTestRunPlanBytestCaseId(int testCaseId);
		List<ProductMaster> getProductByUserCustomerAndEngagement(Integer userId,	Integer userRoleId, Integer customerId, Integer engagementId, Integer activeStatus);
		List<ProductMaster> getProductsByEngagementId(List<Integer> testFactoryId);
		void updateProductFeatureParent(ProductFeature feature, int oldParentFeatureId, int newParentFeatureId);
		List<ProductFeature> listChildNodesInHierarchyinLayers(ProductFeature feature);
		void updateHierarchyIndexForDelete(Integer leftIndex, Integer rightIndex);
		void updateHierarchyIndexForNew(Integer parentRightIndex);
		ProductMaster getProductByNameAndTestfactoryId(String product,Integer testFactoryId);
		ProductFeature getRootFeature();		
		List<ProductFeatureProductBuildMapping> getProductFeatureAndProductBuildMappingList(Integer productId);		
		void mappingProductFeatureToProductBuild(ProductFeatureProductBuildMapping productFeatureProductBuildMapping);
		void mapTestSuiteWithRunConfiguration(Integer runConfigId,Integer testSuiteId,String action);	
		void mapTestSuiteTestCasesRunConfiguration(int runConfigId,int testSuiteId,int testCaseId,String type);		
		List<TestRunPlan> listTestRunPlanByProductId(Integer productId);
		List<RunConfiguration> listRunConfiguration(Integer productId);
		List<TestCaseList> getRunConfigTestSuiteTestCaseMapped(int runConfigId,int testSuiteId);
		Integer addProductBuildFromExternalSource(String productBuildName, String productBuildNo,
				String productBuildDescription, Integer buildTypeId, String productBuildDate,
				ProductVersionListMaster productVersion, ProductMaster product);
		ProductVersionListMaster getLatestProductVersionListMaster(Integer productId);
		void unMappingProductFeatureToProductBuild(ProductFeatureProductBuildMapping productFeatureProductBuildMapping);
		List<ProductFeature> getFeatureListByProductIdAndVersionIdAndBuild(Integer productMasterId, Integer versionId, Integer buildId,
				Integer featureStatus, Integer jtStartIndex, Integer jtPageSize);
		List<ProductFeatureProductBuildMapping> getProductFeatureAndProductBuildMappingByVersionIdOrBuildId(Integer productId, Integer versionId, Integer buildId);
		List<ProductFeatureProductBuildMapping> getProductFeatureAndProductBuildMappingId(Integer mappingId);
		List<ProductFeatureProductBuildMapping> getProductFeatureAndProductBuildMappingList();
		List<Integer> getFeatureTotestCaseMappingByFeatureId(Integer productFeatureId);
		Integer getTotalTestCaseCountForATestRunPlan(Integer testRunPlanId);
		boolean isTestCaseAlreadyMapped(Integer testRunPlanId, Integer testSuiteId,Integer testCaseId);
		
		List<Object[]> getISERecommendatedTestCaseListByBuildId(Integer buildId);
		List<TestCasePriority> listFeatureExecutionPriority();
		List<Object[]> getISERecommendatedTestCaseCategoryCountByBuildId(Integer buildId);
		
		List<Object[]> getMappedTestScriptListByTestcaseId(int productId,Integer testcaseId, Integer jtStartIndex, Integer jtPageSize);
		void unMapRunConfigurationTestRunPlan(int testrunplanid, int runconfigid);
		Integer getMappedTestcasecountByFeatureId(int featureId);
		
		Integer getISERecommendatedTestCaseCountByBuildId(Integer buildId);
		boolean isTestConfigurationTestCaseAlreadyMapped(Integer testConfigurationId, Integer testSuiteId, Integer testCaseId);
		void mapTestConfigurationTestSuiteTestCase(Integer runconfigId,  Integer testSuiteId, List<TestCaseList> testCaseLists, String string);
		VerificationResult testPlanReadinessCheck(Integer testPlanId);
		JTableResponse verificationTestPlanReadinessResult(Integer testPlanId);
		Set<RunConfiguration> getRunConfigurationList(Integer testRunPlanId,Integer type, Integer environmentCombinationId,Integer hostId, Integer deviceId);
		boolean hostHasTestConfigurations(int productId, int hostId);
		boolean deviceHasTestConfigurations(int productId, int deviceId);
		VerificationResult testConfigurationReadinessCheck(RunConfiguration testConfiguration, TestRunPlan testPlan, StringBuffer sb);
		List<TestRunPlan> getTestPlansByProductBuildIds(String productBuildIds);
		ProductType getProductTypeByName(String productTypeName);
		Integer addProductBuild(ProductBuild productBuild, boolean mapAllFeatures);
		boolean mapFeaturesToBuild(ProductBuild productBuild,Set<ProductFeature> feature, String action);
		String updateProductFeatureTestCase(Integer testCaseId, Integer productFeatureId, String maporunmap);
		int getProductVersionIdBybuildId(int productBuildId);
		boolean isFeatureExistingByFeatureNameAndFeatureCode(String featureName,String productFeatureCode, ProductMaster product);
		List<RunConfiguration> getRunConfigurationListByEnvironmentCombination(Integer productMasterId);
		ProductMaster getProductExitsInsameTestFactory(Integer testFactoryId,String productName);
		List<TestCaseList> getRunConfigTestCasesByTestSuite(Integer testcaseId);
		List<RunConfigurationTSHasTC> getRunConfigTestCaseObjectByTestSuite(Integer testcaseId);
		RunConfiguration getRunConfigurationByIdWithoutInitialization(Integer runconfigId);
		ProductVersionListMaster getProductVersionListByProductIdAndVersionName(Integer productId,String productVersionName);
		ProductBuild getproductBuildByProductIdAndBuildName(Integer productVersionId,String productBuildName);
		TestRunPlan getTestRunPlanBytestRunPlanNameAndProductBuild(String testRunPlanName, Integer productBuildId);
		TestRunPlan getFirstTestRunPlanByTestPlanGroupId(Integer testPlanGroupId);
		TestRunPlangroupHasTestRunPlan getTestRunPlanGroupHasTestPlanByTestPlanId(Integer testRunPlanId);
		TestRunPlanTSHasTC getTestPlanTestSuiteTestCase(Integer testRunPlanId,Integer testSuiteId, Integer testCaseId);
}

