package com.hcl.atf.taf.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.AttachmentDAO;
import com.hcl.atf.taf.dao.DecouplingCategoryDAO;
import com.hcl.atf.taf.dao.DefectManagementSystemDAO;
import com.hcl.atf.taf.dao.DefectTypeDAO;
import com.hcl.atf.taf.dao.EnvironmentDAO;
import com.hcl.atf.taf.dao.ISERecommendedTestCaseDAO;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.ProductFeatureDAO;
import com.hcl.atf.taf.dao.ProductLocaleDao;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ProductTeamResourcesDao;
import com.hcl.atf.taf.dao.ProductTestEnvironmentDAO;
import com.hcl.atf.taf.dao.ProductUserRoleDAO;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.dao.ResourcePerformanceDAO;
import com.hcl.atf.taf.dao.TestCaseAutomationScriptDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.TestCaseStepsListDAO;
import com.hcl.atf.taf.dao.TestExecutionResultBugDAO;
import com.hcl.atf.taf.dao.TestFactoryDao;
import com.hcl.atf.taf.dao.TestFactoryProductCoreResourcesDao;
import com.hcl.atf.taf.dao.TestRunConfigurationChildDAO;
import com.hcl.atf.taf.dao.TestSuiteListDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.DefectApprovalStatusMaster;
import com.hcl.atf.taf.model.DefectIdentificationStageMaster;
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
import com.hcl.atf.taf.model.ResourceDailyPerformance;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.RunConfigurationTSHasTC;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestFactoryProductCoreResource;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunPlanGroup;
import com.hcl.atf.taf.model.TestRunPlanTSHasTC;
import com.hcl.atf.taf.model.TestRunPlangroupHasTestRunPlan;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserRoles;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;
import com.hcl.atf.taf.model.dto.DefectWeeklyReportDTO;
import com.hcl.atf.taf.model.dto.ProductFeatureListDTO;
import com.hcl.atf.taf.model.dto.ProductSummaryDTO;
import com.hcl.atf.taf.model.dto.ResourceExperienceSummaryDTO;
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
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestCaseScriptGenerationService;
import com.hcl.atf.taf.service.UserListService;

@Service
public class ProductListServiceImpl implements ProductListService {
	private static final Log log = LogFactory.getLog(ProductListServiceImpl.class);
	
	@Autowired
    private ProductBuildDAO productBuildDAO;
	
	@Autowired
    private ProductVersionListMasterDAO productVersionListMasterDAO;
	
	@Autowired
    private ProductMasterDAO productMasterDAO;
	
	@Autowired
    private TestRunConfigurationChildDAO testRunConfigurationChildDAO;

	@Autowired
    private TestSuiteListDAO testSuiteListDAO;

	@Autowired
	private ProductFeatureDAO productFeatureDAO;
	
	@Autowired
	private ProductTestEnvironmentDAO environmentDAO;
	@Autowired
	private ProductLocaleDao  localeDao;
	
	@Autowired
    private TestFactoryProductCoreResourcesDao productCoreResDao;

	@Autowired
	private ProductUserRoleDAO  productUserDao;

	@Autowired
	private WorkPackageDAO  workPackageDAO;
	
	@Autowired
	private ResourcePerformanceDAO  resourcePerformanceDAO;
	@Autowired
	private EnvironmentDAO envDAO;
	
	@Autowired
    private ProductTeamResourcesDao productTeamResourcesDao;
	
	@Autowired
	private DecouplingCategoryDAO decouplingCategoryDAO;
	
	@Autowired
	private TestFactoryDao testFactoryDao;
	
	@Autowired
	private TestFactoryProductCoreResourcesDao testFactoryProductCoreResourcesDao;
	
	@Autowired
	private TestCaseListDAO testCaseListDAO;
	
	@Autowired
	private TestCaseStepsListDAO testCaseStepsListDAO;
	
	@Autowired
	private DefectTypeDAO defectTypeDAO;
	
	@Autowired
	private DefectManagementSystemDAO defectManagementSystemDAO;
	
	@Autowired
	private TestCaseScriptGenerationService testCaseScriptGenerationService;
	
	@Autowired
	private TestCaseAutomationScriptDAO testCaseAutomationScriptDAO;
	
	
	@Autowired
	private MongoDBService mongoDBService;	
	
	
	@Value("#{ilcmProps['PRODUCT_FEATURE_BATCH_PROCESSING_COUNT']}")
    private String productFeatureMaxBatchCount;
	
	@Autowired
	private TestExecutionResultBugDAO testExecutionResultBugDAO;
	
	@Autowired
	private ISERecommendedTestCaseDAO iseRecommendedTestCaseDAO;	
	
	@Autowired
	private AttachmentDAO attachmentDAO;
	
	@Autowired
	private UserListService userListService;

	Integer numOfTestCongigurations = 1;

	@Override
	@Transactional
	public Integer addProduct(ProductMaster productMaster) {
		Integer productId = productMasterDAO.add(productMaster);
		//After adding the product always map admin user to that product. Admin should always have access to all the products
		ProductTeamResources productTeamUser = new ProductTeamResources();
		productTeamUser.setProductTeamResourceId(null);
		productTeamUser.setProductMaster(productMaster);
		UserList adminUser =  userListService.getUserByLoginId("admin");
		productTeamUser.setUserList(adminUser);
		productTeamUser.setStatus(1);

		//	productTeamUser.setFromDate(plannedStartDate);
		//	productTeamUser.setToDate(plannedEndDate);
		productTeamResourcesDao.addProductTeamResource(productTeamUser);

		ProductUserRole productUserPermission = new ProductUserRole();
		productUserPermission.setProduct(productMaster);
		productUserPermission.setRole(adminUser.getUserRoleMaster());
		productUserPermission.setUser(adminUser);
		productUserPermission.setStatus(1);
		addProductUserRole(productUserPermission);
		return productId;
	}

	@Override
	@Transactional
	public void addProductVersion(ProductVersionListMaster productVersionListMaster) {
		productVersionListMasterDAO.add(productVersionListMaster);
		
	}

	@Override
	@Transactional
	public Integer addProductBuild(ProductBuild productBuild) {
		
		return addProductBuild(productBuild, true);
		
	}

	@Override
	@Transactional
	public Integer addProductBuild(ProductBuild productBuild, boolean mapAllFeatures) {
		
		//Create the build
		int buildId = productBuildDAO.add(productBuild);
		ProductBuild build = productBuildDAO.getByProductBuildId(buildId, 1);
		Set<ProductFeature> features = build.getProductMaster().getProductFeatures();
		//Map the features to the build
		if (mapAllFeatures) {
			mapFeaturesToBuild(build, features, "map");
		}
		return buildId;
	}
	
	@Override
	@Transactional
	public boolean mapFeaturesToBuild(ProductBuild productBuild, Set<ProductFeature> features, String action) {
		List<ProductFeatureProductBuildMapping> productFeatureProductBuildMappingList = new ArrayList<>();
		for(ProductFeature feature : features){
			ProductFeatureProductBuildMapping productFeatureProductBuildMapping = new ProductFeatureProductBuildMapping();
			if(action.equalsIgnoreCase("map")) 
				productFeatureProductBuildMapping.setIsMapped(1);
			else
				productFeatureProductBuildMapping.setIsMapped(0);
			productFeatureProductBuildMapping.setBuildId(productBuild.getProductBuildId());
			productFeatureProductBuildMapping.setFeatureId(feature.getProductFeatureId());
			productFeatureProductBuildMapping.setProduct(productBuild.getProductMaster());
			productFeatureProductBuildMapping.setCreatedDate(new Date());
			productFeatureProductBuildMapping.setModifiedDate(new Date());
			if(action.equalsIgnoreCase("map")) 
				mappingProductFeatureToProductBuild(productFeatureProductBuildMapping);
			else
				unMappingProductFeatureToProductBuild(productFeatureProductBuildMapping);
			log.info("Product Feature and Build mapping successfully");
		}
		return true;
	}
		
	@Override
	@Transactional
	public Integer addProductBuildFromExternalSource(String productBuildName, String productBuildNo, String productBuildDescription, Integer buildTypeId, String productBuildDate, ProductVersionListMaster productVersion, ProductMaster product) {
		
		ProductBuild productBuild = new ProductBuild();
		productBuild.setBuildNo(productBuildNo);
		productBuild.setBuildname(productBuildName);
		productBuild.setBuildDescription(productBuildDescription);
		if(buildTypeId != null){
			DefectIdentificationStageMaster buildType = new DefectIdentificationStageMaster();
			buildType.setStageId(buildTypeId);
			productBuild.setBuildType(buildType);
		}
		if(productBuildDate == null || productBuildDate.trim().isEmpty()) {
			productBuild.setBuildDate(DateUtility.getCurrentTime());
		} else {
			productBuild.setBuildDate(DateUtility.dateformatWithOutTime(productBuildDate));
		}
		productBuild.setCreatedDate(DateUtility.getCurrentTime());
		productBuild.setModifiedDate(DateUtility.getCurrentTime());
		productBuild.setStatus(1);	
		productBuild.setProductVersion(productVersion);
		productBuild.setProductMaster(product);
		
		return productBuildDAO.add(productBuild);
		
	}

	@Override
	@Transactional
	public void addProductEnvironment(Environment environment) {
		ProductMaster productMaster = productMasterDAO.getProductDetailsById(environment.getProductMaster().getProductId());		
		environment.setProductMaster(productMaster);
		 environmentDAO.add(environment);		
	}
	@Override
	@Transactional
	public void updateProductEnvironment(Environment environment){
		
		environmentDAO.update(environment);	
		
	}
	@Override
	@Transactional
	public void deleteProductEnvironment(Environment environment){
		Integer environmentId=environment.getEnvironmentId();
		log.info("Delete Env Id--->"+environmentId);
		
	
		environmentDAO.delete(environment); 
	}
	
	
	@Override
	@Transactional
	public void deleteProduct(int productId) {
		ProductMaster product = productMasterDAO.getProductDetailsById(productId);
		productMasterDAO.delete(product);
		Set<ProductVersionListMaster> productVersions = product.getProductVersionListMasters();
		for(ProductVersionListMaster productVersion : productVersions){
			productVersionListMasterDAO.delete(productVersion);
			Set<TestRunConfigurationChild> testRunConfigurationChilds = productVersion.getTestRunConfigurationChilds();
			testRunConfigurationChildDAO.delete(testRunConfigurationChilds);
			Set<TestSuiteList> testSuiteLists = productVersion.getTestSuiteLists();
			testSuiteListDAO.delete(testSuiteLists);
		}		
		
	}

	@Override
	@Transactional
	public void reactivateProduct(int productId) {
		ProductMaster product = productMasterDAO.getProductDetailsById(productId);
		productMasterDAO.reactivate(product);
		Set<ProductVersionListMaster> productVersions = product.getProductVersionListMasters();
		for(ProductVersionListMaster productVersion : productVersions){
			productVersionListMasterDAO.reactivate(productVersion.getProductVersionListId());
			Set<TestRunConfigurationChild> testRunConfigurationChilds = productVersion.getTestRunConfigurationChilds();
			testRunConfigurationChildDAO.reactivate(testRunConfigurationChilds);
			Set<TestSuiteList> testSuiteLists = productVersion.getTestSuiteLists();
			testSuiteListDAO.reactivate(testSuiteLists);
		}		
		
	}
	
	@Override
	@Transactional
	public void deleteProductVersion(int productVersionListId) {
		ProductVersionListMaster productVersion = productVersionListMasterDAO.getByProductListId(productVersionListId);
		productVersionListMasterDAO.delete(productVersion);
		Set<TestRunConfigurationChild> testRunConfigurationChilds = productVersion.getTestRunConfigurationChilds();
		testRunConfigurationChildDAO.delete(testRunConfigurationChilds);
		Set<TestSuiteList> testSuiteLists = productVersion.getTestSuiteLists();
		testSuiteListDAO.delete(testSuiteLists);
	}
	
	@Override
	@Transactional
	public void reactivateProductVersion(int productVersionListId) {
		ProductVersionListMaster productVersion = productVersionListMasterDAO.getByProductListId(productVersionListId);
		productVersionListMasterDAO.reactivate(productVersionListId);
		Set<TestRunConfigurationChild> testRunConfigurationChilds = productVersion.getTestRunConfigurationChilds();
		testRunConfigurationChildDAO.reactivate(testRunConfigurationChilds);
		Set<TestSuiteList> testSuiteLists = productVersion.getTestSuiteLists();
		testSuiteListDAO.reactivate(testSuiteLists);
	}
	
	@Override
	@Transactional
	public void deleteProductBuild(int productBuildId) {
		log.info("PLSImpl:"+productBuildId);
		ProductBuild productBuild = productBuildDAO.getByProductBuildId(productBuildId, 0);
		productBuildDAO.delete(productBuild);		
	}
	
	@Override
	@Transactional
	public void reactivateProductBuild(int productBuildId) {
		ProductBuild productBuild = productBuildDAO.getByProductBuildId(productBuildId, 0);
		productBuildDAO.reactivate(productBuildId);	
	}

	@Override
	@Transactional
	public int getTotalRecordCount() {
		return productMasterDAO.getTotalRecordCount();
	}
	@Override
	@Transactional
	public int getTotalRecordsOfProduct() {
		
		return productMasterDAO.getTotalRecords();
	}

	@Override
	@Transactional
	public int getTotalRecordsOfProductVersion() {
		
		return productVersionListMasterDAO.getTotalRecords();
	}

	@Override
	@Transactional
	public int getTotalRecordsOfProductBuild() {
		
		return productBuildDAO.getTotalRecords();
	}

	
	@Override
	@Transactional
	public List<ProductBuild> listProductBuild() {
		
		return productBuildDAO.list();
		
	}
	
	@Override
	@Transactional
	public List<ProductVersionListMaster> listProductVersion() {
		
		return productVersionListMasterDAO.list();
		
	}

	@Override
	@Transactional
	public List<ProductMaster> listProduct() {
	
		return productMasterDAO.list(false);
	}

	@Override
	@Transactional
	public List<ProductMaster> listProduct(int startIndex, int pageSize) {
		return productMasterDAO.list(startIndex, pageSize);
	}
	
	@Override
	@Transactional
	public List<ProductMaster> listProductsbyCustomerID(int customerId, Integer startIndex, Integer pageSize) {
		return productMasterDAO.listbyCustomerId(customerId, startIndex, pageSize);
	}

	@Override
	@Transactional
	public List<ProductBuild> listProductBuildPaginate(int startIndex, int pageSize) {
		return productBuildDAO.list(startIndex, pageSize);
	}
	
	@Override
	@Transactional
	public List<ProductVersionListMaster> listProductVersion(int productId) {
		return productVersionListMasterDAO.list(productId);
	}	
	
	@Override
	@Transactional
	public List<ProductBuild> listProductBuild(int productVersionListId) {
		return productBuildDAO.list(productVersionListId);
	}
	
	@Override
	@Transactional
	public List<ProductVersionListMaster> listProductVersions(int productId, int status) {
		return productVersionListMasterDAO.listProductVersions(productId,status);
	}
	
	@Override
	@Transactional
	public List<ProductBuild> listProductBuilds(int productVersionListId, int status) {
		return productBuildDAO.listProductBuilds(productVersionListId,status);
	}	
	
	@Override
	@Transactional
	public List<ProductVersionListMaster> listProductVersionPaginate(int startIndex,
			int pageSize) {
		return productVersionListMasterDAO.list(startIndex, pageSize);
	}

	@Override
	@Transactional
	public List<ProductVersionListMaster> listProductVersion(int productId,
			int startIndex, int pageSize) {
		return productVersionListMasterDAO.list(productId, startIndex, pageSize);
	}	
	
	@Override
	@Transactional
	public List<ProductBuild> listProductBuild(int productVersionListId,
			int startIndex, int pageSize) {
		return productBuildDAO.list(productVersionListId, startIndex, pageSize);
	}

	@Override
	@Transactional
	public void updateProductBuild(
			ProductBuild productBuild) {
			productBuildDAO.update(productBuild);
	}
	
	@Override
	@Transactional
	public void updateProductVersion(
			ProductVersionListMaster productVersionListMaster) {
			productVersionListMasterDAO.update(productVersionListMaster);
	}

	@Override
	@Transactional
	public void updateProduct(ProductMaster productMaster) {
		productMasterDAO.update(productMaster);
		
	}

	@Override
	@Transactional
	public List<ProductMaster> productsList() {
		
		return productMasterDAO.list(new String[]{"productId","productName"});
	}	
	
	@Override
	@Transactional
	public List<ProductMaster> productsListByNames() {
		
		return productMasterDAO.listByNames();
	}
	
	
	@Override
	@Transactional
	public List<ProductVersionListMaster> productVersionsList(int productId) {
		
		return productVersionListMasterDAO.list(productId,new String[]{"productVersionListId","productVersionName"});
	}

	@Override
	@Transactional
	public ProductVersionListMaster getProductVersionListMasterById(
			int productVersionMasterListId) {		
		return productVersionListMasterDAO.getByProductListId(productVersionMasterListId);
	}

	@Override
	@Transactional
	public int getProductTypeByVersionId(int productVersionListId, int productId, int workPackageId) {		
		return productVersionListMasterDAO.getProductTypeByVersionId(productVersionListId, productId, workPackageId);	
	}
	@Override
	@Transactional
	public ProductBuild getProductBuildById(
			int productBuildId, int listOrObjInitialize) {
		
		return productBuildDAO.getByProductBuildId(productBuildId, 1);
	}
	
	@Override
	@Transactional
	public boolean isProductExistsByName(String productName) {
	
		return productMasterDAO.isProductExistingByName(productName);
	}

	@Override
	@Transactional
	public boolean isProductVersionExistingByName(ProductVersionListMaster productVersionListMaster) {
	
		return productVersionListMasterDAO.isProductVersionExistingByName(productVersionListMaster);
	}
	
	@Override
	@Transactional
	public boolean isProductVersionExistingByNameForUpdate(ProductVersionListMaster productVersionListMaster, int productVersionListId) {	
		return productVersionListMasterDAO.isProductVersionExistingByNameForUpdate(productVersionListMaster, productVersionListId);
	}
	
	@Override
	@Transactional
	public boolean isProductBuildExistingByName(ProductBuild productBuild) {
		return productBuildDAO.isProductBuildExistingByName(productBuild);		
	}
	@Override
	@Transactional
	public boolean isProductExistingByName(String productName) {
		return productMasterDAO.isProductExistingByName(productName);
	}	
	
	@Override
	@Transactional
	public ProductMaster getProductById(int productId) {
	
		return productMasterDAO.getByProductId(productId);
	}
	
	//Changes for Product feature
	@Override
	@Transactional
	public void addProductFeature(ProductFeature productFeature) {	
		productFeatureDAO.add(productFeature);
		
		if(productFeature.getParentFeature() != null){
			ProductFeature parentFeature = productFeatureDAO.getByProductFeatureId(productFeature.getParentFeature().getProductFeatureId());
			productFeature.setParentFeature(parentFeature);
			productFeature = buildDisplayName(productFeature);
			if (productFeature.getProductFeatureCode() == null || productFeature.getProductFeatureCode().trim().isEmpty()) 
				productFeature = buildFeatureCode(productFeature);
		}else{
			productFeature.setParentFeature(null);
			productFeature.setDisplayName(productFeature.getProductFeatureName());
			productFeature.setProductFeatureCode(productFeature.getProductFeatureId() + "");
		}
		
		productFeatureDAO.update(productFeature);
	}
	
	
	private ProductFeature buildFeatureCode(ProductFeature productFeature) {
		
		
		if(productFeature.getParentFeature() != null){
			ProductFeature parentFeature = productFeatureDAO.getByProductFeatureId(productFeature.getParentFeature().getProductFeatureId());
			productFeature.setParentFeature(parentFeature);
		
			//Create display name from parent 
			StringBuffer featureCode = new StringBuffer();
			if(productFeature.getProductFeatureId() != null ) {
				featureCode.append(productFeature.getAbbr() + "-" + productFeature.getProductFeatureId());
			} else {
				featureCode.append(productFeature.getAbbr());
			}
			
			boolean hasParent = true;
			while (hasParent) {
				if (parentFeature.getParentFeature() != null) {

					if (productFeature.getAbbr() == null || productFeature.getAbbr().trim().isEmpty()) {
					} else {
						if(parentFeature.getAbbr() != null) {
							featureCode.insert(0, parentFeature.getAbbr() +  "-");
						}
					}
				}
				parentFeature = parentFeature.getParentFeature();
				if (parentFeature == null)
					hasParent = false;			
			}
			productFeature.setProductFeatureCode(featureCode.toString());
		}else{
			productFeature.setDisplayName(productFeature.getProductFeatureName());	
			if (productFeature.getAbbr() == null || productFeature.getAbbr().trim().isEmpty()) {
				productFeature.setProductFeatureCode(productFeature.getProductFeatureId() == null?"":productFeature.getProductFeatureId()+"");
			} else {
				
				if(productFeature.getProductFeatureId() != null ) {
					productFeature.setProductFeatureCode(productFeature.getAbbr() + "-" + productFeature.getProductFeatureId());
				} else {
					productFeature.setProductFeatureCode(productFeature.getAbbr());
				}
			}
		}
		return productFeature;
	}
	
	private ProductFeature buildDisplayName(ProductFeature productFeature) {
		
		
		if(productFeature.getParentFeature() != null){
			ProductFeature parentFeature = productFeatureDAO.getByProductFeatureId(productFeature.getParentFeature().getProductFeatureId());
			productFeature.setParentFeature(parentFeature);
		
			//Create display name from parent 
			StringBuffer displayName = new StringBuffer();
			displayName.append(productFeature.getProductFeatureName());
			
			boolean hasParent = true;
			while (hasParent) {
				if (parentFeature.getParentFeature() != null) {
					displayName.insert(0, parentFeature.getProductFeatureName() + " | ");

				}
				parentFeature = parentFeature.getParentFeature();
				if (parentFeature == null)
					hasParent = false;			
			}
		
			productFeature.setDisplayName(displayName.toString());
		}else{
			productFeature.setDisplayName(productFeature.getProductFeatureName());	
		}
		return productFeature;
	}


	@Override
	@Transactional
	public void updateProductFeature(ProductFeature productFeature) {	
		
		//Reconstruct the display name and code name
		ProductFeature feature = buildDisplayName(productFeature);
		if (productFeature.getProductFeatureCode() == null || productFeature.getProductFeatureCode().trim().isEmpty()){ 
			feature = buildFeatureCode(productFeature);
		}

		productFeatureDAO.update(feature);
	}

	@Override
	@Transactional
	public void updateProductFeatureParent(ProductFeature feature, int oldParentFeatureId, int newParentFeatureId) {	
		
		if (oldParentFeatureId == newParentFeatureId) {
			//The parent feature has not changed. No action
			return;
		}
		
		//Update the parent feature in the hierarchy
		ProductFeature oldParentFeature = productFeatureDAO.getByProductFeatureId(oldParentFeatureId);
		ProductFeature newParentFeature = productFeatureDAO.getByProductFeatureId(newParentFeatureId);
		log.info("Update Parent Feature for " +  feature.getProductFeatureName() + " from " + oldParentFeature.getProductFeatureName() + " : to : " + newParentFeature.getProductFeatureName());
		productFeatureDAO.updateFeatureParent(feature, oldParentFeature, newParentFeature);
		//Reload updated feature
		feature = productFeatureDAO.getByProductFeatureId(feature.getProductFeatureId());
		
		//Reconstruct the display name and code name
		feature = buildDisplayName(feature);
		if (feature.getProductFeatureCode() == null || feature.getProductFeatureCode().trim().isEmpty()) 
			feature = buildFeatureCode(feature);
		productFeatureDAO.update(feature);

		//Create display name from parent 
		//feature.setDisplayName(createDisplayNameForFeature(feature));
	}

/*	private String createDisplayNameForFeature(ProductFeature feature) {
		
		//Create display name from parent 
		ProductFeature parentFeature = feature.getParentFeature();
		StringBuffer displayName = new StringBuffer();
		if (parentFeature.getProductFeatureId() != 0) {
			displayName.append(feature.getProductFeatureName());
			boolean hasParent = true;
			while (hasParent) {
					
				if (parentFeature != null)
					displayName.insert(0, parentFeature.getProductFeatureName() + " | ");
					
				if (parentFeature == null || parentFeature.getParentFeature() == null){
					hasParent = false;
				} else {
					parentFeature = productFeatureDAO.getProductFeatureParentById(parentFeature.getParentFeature().getProductFeatureId());
				}
					
			}
		} else {
			displayName.append(feature.getProductFeatureName());
		} 
		return displayName.toString();
	}*/
	
	@Override
	@Transactional
	public void deleteProductFeature(ProductFeature productFeature) {
		productFeatureDAO.delete(productFeature);
	}

	@Override
	@Transactional
	public List<ProductFeature> list() {
		return productFeatureDAO.list();
	}

	@Override
	@Transactional
	public List<ProductFeature> list(Integer startIndex, Integer pageSize, Date startDate,Date endDate) {
		return productFeatureDAO.list(startIndex, pageSize, startDate,endDate);
	}
	
	@Override
	@Transactional
	public Integer countProductFeatures(Date startDate,Date endDate) {
		
		return productFeatureDAO.countProductFeatures(startDate,endDate);
	}

	@Override
	@Transactional
	public ProductFeature getByProductFeatureName(String productFeatureName) {
		return productFeatureDAO.getByProductFeatureName(productFeatureName);
	}

	@Override
	@Transactional
	public ProductFeature getByProductFeatureId(int productFeatureId) {
		return productFeatureDAO.getByProductFeatureId(productFeatureId);
	}

	@Override
	@Transactional
	public Integer getUnMappedTestCaseListCountOfFeatureByProductFeatureId(
			int productId, int productFeatureId) {
		return productFeatureDAO.getUnMappedTestCaseListCountOfFeatureByProductFeatureId(productId, productFeatureId);
	}

	@Override
	@Transactional
	public List<Object[]> getUnMappedTestCaseListByProductFeatureId(int productId, int productFeatureId, Integer jtStartIndex, Integer jtPageSize) {
		return productFeatureDAO.getUnMappedTestCaseListByProductFeatureId(productId, productFeatureId, jtStartIndex, jtPageSize);
	}	
	
	@Override
	@Transactional
	public boolean isProductFeatureExistingByName(String productFeatureName) {
		return productFeatureDAO.isProductFeatureExistingByName(productFeatureName);
	}

	@Override
	@Transactional
	public boolean isProductFeatureExistingByName(String productFeatureName,Integer productId, Integer productFeatureId) {
		return productFeatureDAO.isProductFeatureExistingByName(productFeatureName, productId, productFeatureId);
	}
		
	@Override
	@Transactional
	public TestCaseList updateProductFeatureTestCases(int testCaseId,int productFeatureId) {
		return productFeatureDAO.updateProductFeatureTestCases(testCaseId, productFeatureId);
	}
	
	@Override
	@Transactional
	public List<ProductFeature> getFeaturesMappedToTestCase(Integer testCaseId) {
		return productFeatureDAO.getFeaturesMappedToTestCase(testCaseId);
	}

	@Override
	@Transactional
	public TestCaseList updateProductFeatureTestCasesOneToMany(
			Integer testCaseId, Integer productFeatureId, String maporunmap) {
		return productFeatureDAO.updateProductFeatureTestCasesOneToMany(testCaseId, productFeatureId, maporunmap);
	}

	@Override
	@Transactional
	public ProductMaster listProductByName(String productName) {
		return null;
	}

	@Override
	@Transactional
	public ProductVersionListMaster getProductVersionListMasterByName(String productVersionName) {
		return productVersionListMasterDAO.productVersionListByName(productVersionName);
	}
	
	@Override
	@Transactional
	public ProductVersionListMaster getLatestProductVersionListMaster(Integer productId) {
		return productVersionListMasterDAO.getLatestProductVersionListMaster(productId);
	}


	@Override
	@Transactional
	public ProductMaster getProductByName(String productName) {
		return productMasterDAO.getProductByName(productName);
	}

	@Override
	@Transactional
	public ProductBuild getProductBuildByName(String productBuildName) {
		return productBuildDAO.productBuildByName(productBuildName);
	}

	@Override
	@Transactional
	public List<Environment> getEnvironmentListByProductId(Integer productId) {
		return environmentDAO.getEnvironmentListByProductId(productId);
	}
	
	@Override
	@Transactional
	public List<Environment> getEnvironmentListByProductIdAndStatus(Integer productId,Integer status) {
		
		return environmentDAO.getEnvironmentListByProductIdAndStatus(productId,status);
	}
	
	
	@Override
	@Transactional
	public boolean isProductEnvironmentExistingByName(Environment environment) {
		return environmentDAO.isProductEnvironmentExistingByName(environment);
	}
	@Override
	@Transactional
	public Environment getEnvironmentById(Integer environmentId) {
		return environmentDAO.getByEnvironmentId(environmentId);
	}
	@Override
	@Transactional
	public void addProductUserRole(ProductUserRole productUserRole) {
			productMasterDAO.addProductUserRole(productUserRole);
	}

	@Override
	@Transactional
	public List<ProductUserRole> listProductUserRole(int productId,
			int jtStartIndex, int jtPageSize) {
		return productMasterDAO.listProductUserRole(productId, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public List<UserRoleMaster> getAllRoles() {
		return productMasterDAO.getAllRoles();
	}

	@Override
	@Transactional
	public boolean isProductUserRoleExits(int productId, int userId, int roleId) {
		return productMasterDAO.isProductUserRoleExits(productId, userId, roleId);
	}
	
	@Override
	@Transactional
	public boolean isUserPermissionByProductIdandUserId(int productId, int userId, int roleId) {
		return productMasterDAO.isUserPermissionByProductIdandUserId(productId, userId, roleId);
	}
	
	
	
	/* product Locale*/	
	
	@Override
	@Transactional
	public List<ProductLocale> getProductLocaleListByProductId(
			int productMasterId) {
		return localeDao.getProductLocaleListByProductId(productMasterId);
	}

	@Override
	@Transactional
	public void addProductLocale(ProductLocale locale) {
		ProductMaster productMaster = productMasterDAO.getProductDetailsById(locale.getProductMaster().getProductId());		
		locale.setProductMaster(productMaster);
		localeDao.addLocale(locale);
	}


	@Override
	@Transactional
	public void updateProductLocale(ProductLocale locale) {
		localeDao.updateProductLocale(locale);
	}

	@Override
	@Transactional
	public List<ProductFeature> getFeatureListByProductId(Integer productId, Integer featureStatus, Integer jtStartIndex, Integer jtPageSize) {
		return productFeatureDAO.getFeatureListByProductId(productId, featureStatus, jtStartIndex, jtPageSize);
	}
	
	@Override
	@Transactional
	public List<ProductFeature> getFeatureListExcludingChildByparentFeatureId(Integer productId,
			Integer parentFeatureId) {
		return productFeatureDAO.getFeatureListExcludingChildByparentFeatureId(productId,parentFeatureId);
	}

	@Override
	@Transactional
	public Environment getEnvironmentByName(String environmentName) {
		return productMasterDAO.getEnvironmentByName(environmentName);
	}	
	
	@Override
	@Transactional
	public ProductLocale getLocaleByName(String localeName) {
		return localeDao.getLocaleByName(localeName);
	}

	

	@Override
	@Transactional
	public ProductLocale getLocaleById(Integer productLocaleId) {
		return localeDao.getProductLocaleById(productLocaleId);
	}
		
	@Override
	@Transactional
	public ProductLocale getLocaleByNameByProduct(String localeName,String productId) {
		return localeDao.getLocaleByNameByProduct(localeName,productId);
	}
	
	@Override
	@Transactional
	public Environment getEnvironmentByNameByProduct(String environmentName,String productId) {
		return productMasterDAO.getEnvironmentByNameByProduct(environmentName,productId);
	}	

	@Override
	@Transactional
	public List<ProductUserRole> listProductUserRole(int productId) {
		return productMasterDAO.listProductUserRole(productId);
	}

	@Override
	@Transactional
	public ProductUserRole getProductUserRoleByUserId(int userId) {
		return productUserDao.getProductUserRoleByUserId(userId);
	}

	@Override
	@Transactional
	public List<TestFactoryProductCoreResource> getProductCoreResourcesList(
			int productId, Integer jtStartIndex, Integer jtPageSize) {
		return productCoreResDao.getProductCoreResourcesList(productId,jtStartIndex,jtPageSize);
	}

	@Override
	@Transactional
	public void addProductCoreResource(
			TestFactoryProductCoreResource coreResouce) {
		productCoreResDao.add(coreResouce);
		
	}

	@Override
	@Transactional
	public TestFactoryProductCoreResource getCoreResourceById(
			Integer testFactoryProductCoreResourceId) {
		return productCoreResDao.getCoreResourceById(testFactoryProductCoreResourceId);
	}

	@Override
	@Transactional
	public void updateProductCoreResource(
			TestFactoryProductCoreResource coreResourceFromUI) {
		productCoreResDao.upadte(coreResourceFromUI);
		
	}

	@Override
	@Transactional
	public List<ProductMaster> listProductsByTestFactoryId(int testFactoryId) {
		return productMasterDAO.listProductsByTestFactoryId(testFactoryId);
	}

	@Override
	@Transactional
	public List<ProductMaster> getProductsByWorkPackageForUserId(
			int userRoleId, int userId, int filter) {
		return productMasterDAO.getProductsByWorkPackageForUserId(userRoleId, userId, filter);
	}

	@Override
	@Transactional
	/**
	 * Get products associated with user 
	 * @param userRoleId
	 * @param userId
	 * @param filter - 0 - both
	 * 				   1- Test Factory
	 * 				   2- Test Engagement
	 * @return List<ProductMaster>
	 */
	public List<ProductMaster> getProductsByProductUserRoleForUserId(
			int userRoleId, int userId,int filter) {
		return productMasterDAO.getProductsByProductUserRoleForUserId(userRoleId, userId,filter);
	}

	@Override
	@Transactional
	public List<ProductMaster> getProductsByTestFactoryId(int testFactoryId) {
		return productMasterDAO.getProductsByTestFactoryId(testFactoryId);
	}

	
	@Override
	@Transactional
	public List<UserRoleMaster> getRolesBasedResource() {
		return productMasterDAO.getRolesBasedResource();

	}

	@Override
	@Transactional
	public List<ProductMaster> listProductsbyCustomerIdTestFactoryId(int testFactoryId, int customerId, int jtStartIndex, int jtPageSize) {
			return productMasterDAO.listbyCustomerIdTestFactoryId(testFactoryId, customerId, jtStartIndex, jtPageSize);
	}
	@Override
	@Transactional
	public ProductUserRole getProductUserRoleByUserIdAndProductId(
			int productId, int userId) {
		return productUserDao.getProductUserRoleByUserIdAndProductId(userId,productId);
	}

	@Override
	@Transactional
	public List<JsonResourceExperienceSummary> listResourceExperienceOfSelectedProduct(Integer productId, Integer productVersionId, Integer userId,int jtStartIndex, int jtPageSize) {
		List<JsonResourceExperienceSummary> listOfJsonResourceExperienceSummaryDTO = new ArrayList<JsonResourceExperienceSummary>();
		List<ResourceExperienceSummaryDTO> listOfResourceExperienceSummaryDTO = productMasterDAO.listResourceExperienceOfSelectedProduct(productId, productVersionId,userId);
		List<WorkPackageTestCaseExecutionPlan> listOfWorkPackageTestCaseExecutionPlan = productMasterDAO.listWpTCExecutionOfUserforSelectedProduct(productId, productVersionId,userId);
		List<ResourceExperienceSummaryDTO> listOfResourceAveragePerfRating = null;
		if(listOfResourceExperienceSummaryDTO != null && listOfResourceExperienceSummaryDTO.size()>0){
			int counter = 0;
			for (ResourceExperienceSummaryDTO resourceExperienceSummaryDTO : listOfResourceExperienceSummaryDTO) {
				JsonResourceExperienceSummary jsonResourceExperienceSummary = new JsonResourceExperienceSummary();
				jsonResourceExperienceSummary.setExperienceSummaryId(counter++);
				jsonResourceExperienceSummary.setUserId(resourceExperienceSummaryDTO.getUserId());
				jsonResourceExperienceSummary.setUserLoginId(resourceExperienceSummaryDTO.getUserLoginId());
				jsonResourceExperienceSummary.setProductId(resourceExperienceSummaryDTO.getProductId());
				jsonResourceExperienceSummary.setProductName(resourceExperienceSummaryDTO.getProductName());
				jsonResourceExperienceSummary.setProductVersionId(resourceExperienceSummaryDTO.getProductVersionId());
				jsonResourceExperienceSummary.setProductVersionName(resourceExperienceSummaryDTO.getProductVersionName());
				jsonResourceExperienceSummary.setWpCount(resourceExperienceSummaryDTO.getWpCount());
				jsonResourceExperienceSummary.setExecutedTestCaseCount(resourceExperienceSummaryDTO.getExecutedTestCaseCount());
				jsonResourceExperienceSummary.setReportedDefectsCount(0);
				jsonResourceExperienceSummary.setApprovedDefectsCount(0);
				listOfJsonResourceExperienceSummaryDTO.add(jsonResourceExperienceSummary);
			}
		}
		
		if(listOfJsonResourceExperienceSummaryDTO != null &&  listOfJsonResourceExperienceSummaryDTO.size()>0){
			for (JsonResourceExperienceSummary jsonResourceExperience : listOfJsonResourceExperienceSummaryDTO) {
				if(listOfWorkPackageTestCaseExecutionPlan != null && listOfWorkPackageTestCaseExecutionPlan.size()>0){
					List<TestExecutionResultBugList> listOfDefectsRaisedByUser = null;
					List<TestCaseExecutionResult> listOfApprovedDefectsOfUser = null;
					int totalReportedDefectCount = 0;
					int totalApprovedDefectCount = 0;
					for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : listOfWorkPackageTestCaseExecutionPlan) {
						int wpTcExecDefectCount = 0;
						int wpTcExecApprovedDefectCount = 0;
						int wpId = workPackageTestCaseExecutionPlan.getWorkPackage().getWorkPackageId();
						int wpProductId = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
						int wpProductVersionId = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
						if(workPackageTestCaseExecutionPlan.getTester().getUserId() == jsonResourceExperience.getUserId() || workPackageTestCaseExecutionPlan.getTestLead().getUserId() == jsonResourceExperience.getUserId()){
							if(wpProductId == jsonResourceExperience.getProductId()){
								if(wpProductVersionId ==  jsonResourceExperience.getProductVersionId()){
									if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult() != null){
										listOfDefectsRaisedByUser = workPackageDAO.listDefectsByTestcaseExecutionPlanId(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestCaseExecutionResultId(),jtStartIndex, jtPageSize);
										listOfApprovedDefectsOfUser = workPackageDAO.listApprovedDefectsByTestcaseExecutionPlanId(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestCaseExecutionResultId(),jtStartIndex, jtPageSize);
										if(listOfDefectsRaisedByUser != null && listOfDefectsRaisedByUser.size()>0){
											wpTcExecDefectCount = listOfDefectsRaisedByUser.size();
											totalReportedDefectCount = totalReportedDefectCount+wpTcExecDefectCount;
											jsonResourceExperience.setReportedDefectsCount(totalReportedDefectCount);
										}
										if(listOfApprovedDefectsOfUser != null && listOfApprovedDefectsOfUser.size()>0){
											wpTcExecApprovedDefectCount = listOfApprovedDefectsOfUser.size();
											totalApprovedDefectCount = totalApprovedDefectCount+wpTcExecApprovedDefectCount;
											jsonResourceExperience.setApprovedDefectsCount(totalApprovedDefectCount);
										}
									}else{
										jsonResourceExperience.setReportedDefectsCount(0);
									}
									if(wpId > 0){
										listOfResourceAveragePerfRating = resourcePerformanceDAO.getResourceAveragePerformance(jsonResourceExperience.getUserId(),wpId);
										if(listOfResourceAveragePerfRating != null && listOfResourceAveragePerfRating.size()>0){
											for (ResourceExperienceSummaryDTO resourceExpDTO : listOfResourceAveragePerfRating) {
												jsonResourceExperience.setUserAveragePerformanceRating(resourceExpDTO.getUserAveragePerformanceRating());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return listOfJsonResourceExperienceSummaryDTO;
	}

	@Override
	@Transactional
	public List<JsonWorkPackage> listUserExperienceWorkPackagesDetails(Integer productId, Integer productVersionId, Integer userId,int startIndex, int pageSize) {
		List<JsonWorkPackage> listOfJsonWps = new ArrayList<JsonWorkPackage>();
		List<WorkPackage> listOfWorkPackages = null;
		List<WorkPackage> newListOfWorkPackages = new ArrayList<WorkPackage>();
		listOfWorkPackages = workPackageDAO.listWpOfUserforSelectedProduct(productId, productVersionId,userId,startIndex,pageSize);
		if(listOfWorkPackages != null && listOfWorkPackages.size()>0){
			for (WorkPackage workPackage : listOfWorkPackages) {
				if(workPackage != null){
					if(!newListOfWorkPackages.contains(workPackage)){
						newListOfWorkPackages.add(workPackage);
					}
				}
			}
			if(newListOfWorkPackages != null && newListOfWorkPackages.size() >0 ){
				for (WorkPackage workPackage2 : newListOfWorkPackages) {
					String plannedStartDate = DateUtility.dateToStringWithoutSeconds(workPackage2.getPlannedStartDate());
					String plannedEndDate = DateUtility.dateToStringWithoutSeconds(workPackage2.getPlannedEndDate());
					JsonWorkPackage  jsonWorkPackage = new JsonWorkPackage(workPackage2);
					jsonWorkPackage.setPlannedStartDate(plannedStartDate);
					jsonWorkPackage.setPlannedEndDate(plannedEndDate);
					listOfJsonWps.add(jsonWorkPackage);
				}
			}
		}
		return listOfJsonWps;
	}
	
	@Override
	@Transactional
	public List<JsonWorkPackageTestCaseExecutionPlan> listUserExperienceExecutedTCDetails(Integer productId, Integer productVersionId, Integer userId, int jtStartIndex, int jtPageSize) {
		List<JsonWorkPackageTestCaseExecutionPlan> listOfJsonWpTcExecPlan = new ArrayList<JsonWorkPackageTestCaseExecutionPlan>();
		List<WorkPackageTestCaseExecutionPlan> listOfWorkPackageTestCaseExecutionPlan = null;
		listOfWorkPackageTestCaseExecutionPlan = productMasterDAO.listWpTCExecutionOfUserforSelectedProduct(productId, productVersionId,userId);
		if(listOfWorkPackageTestCaseExecutionPlan != null && listOfWorkPackageTestCaseExecutionPlan.size()>0){
		for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : listOfWorkPackageTestCaseExecutionPlan) {
				JsonWorkPackageTestCaseExecutionPlan  jsonWorkPackageTestCaseExecutionPlan = new JsonWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
				WorkpackageRunConfiguration wprc = workPackageDAO.getWorkpackageRunConfigurationByWPTCEP(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackageRunConfigurationId());
				if(wprc != null){
					if(wprc.getRunconfiguration().getEnvironmentcombination() != null){
						jsonWorkPackageTestCaseExecutionPlan.setEnvironmentCombinationId(wprc.getRunconfiguration().getEnvironmentcombination().getEnvironment_combination_id());
						jsonWorkPackageTestCaseExecutionPlan.setEnvironmentCombinationName(wprc.getRunconfiguration().getEnvironmentcombination().getEnvironmentCombinationName());
					}
				}
				listOfJsonWpTcExecPlan.add(jsonWorkPackageTestCaseExecutionPlan);
			}
		}
		return listOfJsonWpTcExecPlan;
	}

	@Override
	@Transactional
	public List<JsonTestExecutionResultBugList> listUserReportedDefectsDetails(Integer productId, Integer productVersionId, Integer userId, int jtStartIndex, int jtPageSize) {
		List<WorkPackageTestCaseExecutionPlan> listOfWorkPackageTestCaseExecutionPlan = null;
		List<TestExecutionResultBugList> listOfDefectsRaisedByUser = new ArrayList<TestExecutionResultBugList>();
		List<TestExecutionResultBugList> subListOfDefectsRaisedByUser = null;
		List<JsonTestExecutionResultBugList> listOfTestExecutionResultBugList = new ArrayList<JsonTestExecutionResultBugList>();
		listOfWorkPackageTestCaseExecutionPlan = productMasterDAO.listWpTCExecutionOfUserforSelectedProduct(productId, productVersionId,userId);
		if(listOfWorkPackageTestCaseExecutionPlan != null && listOfWorkPackageTestCaseExecutionPlan.size()>0){
			for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : listOfWorkPackageTestCaseExecutionPlan) {
				if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult() != null){
					subListOfDefectsRaisedByUser = workPackageDAO.listDefectsByTestcaseExecutionPlanId(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestCaseExecutionResultId(),jtStartIndex,jtPageSize);
					if(listOfDefectsRaisedByUser.size() == 0){
						listOfDefectsRaisedByUser = subListOfDefectsRaisedByUser;
					}else{
						listOfDefectsRaisedByUser.addAll(subListOfDefectsRaisedByUser);
					}
				}
			}
			log.info("listOfDefectsRaisedByUser size innnn listUserReportedDefectsDetails: "+listOfDefectsRaisedByUser);
			if(listOfDefectsRaisedByUser != null && listOfDefectsRaisedByUser.size()>0){
				for (TestExecutionResultBugList testExecutionResultBugList : listOfDefectsRaisedByUser) {
					listOfTestExecutionResultBugList.add(new JsonTestExecutionResultBugList(testExecutionResultBugList));
				}
			}
		}
		return listOfTestExecutionResultBugList;
	}
	
	@Override
	@Transactional
	public List<JsonTestCaseExecutionResult> listApprovedDefectsDetails(Integer productId, Integer productVersionId, Integer userId,int jtStartIndex, int jtPageSize) {
		List<WorkPackageTestCaseExecutionPlan> listOfWorkPackageTestCaseExecutionPlan = null;
		List<TestCaseExecutionResult> listOfAppovedDefectsOfUser = new ArrayList<TestCaseExecutionResult>();
		List<TestCaseExecutionResult> subListOfAppovedDefectsOfUser = null;
		List<JsonTestCaseExecutionResult> listOfTestCaseExecutionResult = new ArrayList<JsonTestCaseExecutionResult>();
		listOfWorkPackageTestCaseExecutionPlan = productMasterDAO.listWpTCExecutionOfUserforSelectedProduct(productId, productVersionId,userId);
		if(listOfWorkPackageTestCaseExecutionPlan != null && listOfWorkPackageTestCaseExecutionPlan.size()>0){
			for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : listOfWorkPackageTestCaseExecutionPlan) {
				if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult() != null){
					subListOfAppovedDefectsOfUser = workPackageDAO.listApprovedDefectsByTestcaseExecutionPlanId(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestCaseExecutionResultId(),jtStartIndex, jtPageSize);
					if(listOfAppovedDefectsOfUser.size() == 0){
						listOfAppovedDefectsOfUser = subListOfAppovedDefectsOfUser;
					}else{
						listOfAppovedDefectsOfUser.addAll(subListOfAppovedDefectsOfUser);
					}
				}
			}
			log.info("listOfAppovedDefectsOfUser size innnn listUserReportedDefectsDetails: "+listOfAppovedDefectsOfUser);
			if(listOfAppovedDefectsOfUser != null && listOfAppovedDefectsOfUser.size()>0){
				for (TestCaseExecutionResult testCaseExecutionResult : listOfAppovedDefectsOfUser) {
					listOfTestCaseExecutionResult.add(new JsonTestCaseExecutionResult(testCaseExecutionResult));
				}
			}
		}
		return listOfTestCaseExecutionResult;
	}

	@Override
	@Transactional
	public List<JsonResourceDailyPerformance> getResourceAveragePerformanceDetails(List<JsonWorkPackage> listOfJsonWorkPackage, Integer userId,int jtStartIndex, int jtPageSize) {
		if(listOfJsonWorkPackage == null){
			return null;
		}
		List<ResourceDailyPerformance> listOfUserPerfRating = new ArrayList<ResourceDailyPerformance>();
		List<ResourceDailyPerformance> subListOfUserPerfRating = null;
		List<JsonResourceDailyPerformance> listOfJsonResourceDailyPerformance = new ArrayList<JsonResourceDailyPerformance>();
		if(listOfJsonWorkPackage != null && listOfJsonWorkPackage.size()>0){
			for (JsonWorkPackage jsonWorkPackage : listOfJsonWorkPackage) {
				if(jsonWorkPackage != null){
					subListOfUserPerfRating = resourcePerformanceDAO.getResourceAveragePerformanceDetails(userId, jsonWorkPackage.getId());
					if(listOfUserPerfRating.size() == 0){
						listOfUserPerfRating = subListOfUserPerfRating;
					}else{
						listOfUserPerfRating.addAll(subListOfUserPerfRating);
					}
				}
		}
		log.info("listOfJsonResourceDailyPerformance size innnn getResourceAveragePerformanceDetails: "+listOfUserPerfRating);
		if(listOfUserPerfRating != null && listOfUserPerfRating.size()>0){
			for (ResourceDailyPerformance resourceDailyPerformance : listOfUserPerfRating) {
				String workDate = DateUtility.dateToStringWithoutSeconds(resourceDailyPerformance.getWorkDate());
				JsonResourceDailyPerformance jsonResDailyPerformance = new JsonResourceDailyPerformance(resourceDailyPerformance);
				jsonResDailyPerformance.setWorkDate(workDate);
				listOfJsonResourceDailyPerformance.add(jsonResDailyPerformance);
			}
		}
		}
		return listOfJsonResourceDailyPerformance;
	}

	@Override
	@Transactional
	public List<ProductMaster> getCustmersbyProductId(int testFactoryId, int productId) {
		return productMasterDAO.getCustmersbyProductId(testFactoryId, productId);
	}

	@Override
	@Transactional
	public void deleteEnvironmentCombination(EnvironmentCombination environmentCombination) {
		envDAO.deleteEnvironmentCombination(environmentCombination);
		
	}

	@Override
	@Transactional
	public List<ProductType> listProductTyper() {
		return productMasterDAO.listProductTyper();
	}

	@Override
	@Transactional
	public ProductType getProductTypeById(Integer productTypeId) {
		return productMasterDAO.getProductTypeById(productTypeId);
	}

	@Override
	@Transactional
	public List<DeviceType> listDeviceType() {
		return productMasterDAO.getDeviceType();
	}

	@Override
	@Transactional
	public List<DeviceLab> listDeviceLab() {
		return productMasterDAO.listDeviceLab();
	}

	@Override
	@Transactional
	public RunConfiguration getRunConfigurationById(Integer runconfigId) {
		return productBuildDAO.getRunConfigurationById(runconfigId);
	}

	@Override
	@Transactional
	public Integer addTestRunplan(TestRunPlan testRunPlan) {

		return productMasterDAO.addTestRunplan(testRunPlan);
	}

	@Override
	@Transactional
	public List<TestRunPlan> listTestRunPlanByProductVersionId(Integer productVersionId) {
		return productMasterDAO.listTestRunPlanByProductVersionId(productVersionId);
	}
	@Override
	@Transactional
	public List<TestRunPlan> listTestRunPlanBytestFactorProductorVersion(Integer productVersionId, Integer productId,Integer testFactoryId) {
		return productMasterDAO.listTestRunPlanBytestFactorProductorVersion(productVersionId, productId, testFactoryId);
	}
	@Override
	@Transactional
	public TestRunPlan getTestRunPlanById(Integer testRunPlanId) {
		return productMasterDAO.getTestRunPlanById(testRunPlanId);
	}

	@Override
	@Transactional
	public TestRunPlan getTestRunPlanBytestRunPlanId(Integer testRunPlanId) {
		return productMasterDAO.getTestRunPlanBytestRunPlanId(testRunPlanId);
	}
	
	@Override
	@Transactional
	public void mapTestRunPlanWithTestRunconfiguration(Integer testRunPlanId,Integer runConfigurationId, String action) {
		productMasterDAO.mapTestRunPlanWithTestRunconfiguration(testRunPlanId,runConfigurationId,action);		
	}

	@Override
	@Transactional
	public void mapTestRunPlanWithTestSuite(Integer testRunPlanId,
			Integer testSuiteId, String action) {
		 productMasterDAO.mapTestRunPlanWithTestSuite(testRunPlanId,testSuiteId,action);
		
	}

	@Override
	@Transactional
	public List<ProductMaster> getCustmersbyProductIdStatus(int testFactoryId,
			int productId, int status, int jtStartIndex, int jtPageSize) {		
		return productMasterDAO.getCustmersbyProductIdStatus(testFactoryId, productId, status, jtStartIndex, jtPageSize);
	}


	@Override
	@Transactional
	public List<ProductMaster> getUserRoleBasedProductByTestFactoryId(int testFactoryId, int productId, int userId, int status,
			int jtStartIndex, int jtPageSize){
		return productMasterDAO.getUserRoleBasedProductByTestFactoryId(testFactoryId, productId, userId, status,
				jtStartIndex, jtPageSize);
	}
	

	@Override
	@Transactional
	public Integer getUsersProductCountByTestFactoryId(int testFactoryId,
			int productId, int userId, int status, int jtStartIndex,
			int jtPageSize, int userRoleId){
		return productMasterDAO.getUsersProductCountByTestFactoryId(testFactoryId, productId, userId, status, jtStartIndex,
				 jtPageSize, userRoleId);
	}
	
	@Override
	@Transactional
	public UserRoleMaster getRolesByUserRoleId(int userRoleId) {		
		return productMasterDAO.getRolesByUserRoleId(userRoleId);
	}

	@Override
	@Transactional
	public UserRoles mapUserWithRoles(JsonUserRoles jsonUserRoles) {
		return productMasterDAO.mapUserWithRoles(jsonUserRoles);
	}

	@Override
	@Transactional
	public void updateUserRoles(UserRoles userRoles) {
		productMasterDAO.updateUserRoles(userRoles);		
	}

	@Override
	@Transactional
	public UserRoles getUserRolewithuserRoleIdUserId(int userId, int userRoleId) {
		return productMasterDAO.getUserRolewithuserRoleIdUserId(userId, userRoleId);
	}

	@Override
	@Transactional
	public List<UserRoles> listUserRoles(int startIndex, int pageSize,Integer userId) {
		return productMasterDAO.listUserRoles(startIndex, pageSize,userId);
	}

	@Override
	@Transactional
	public Boolean isUserAlreadyCoreResource(Integer productId, Integer userId,
			String fromDate, String toDate,TestFactoryProductCoreResource coreResourceFromDB) {
		return productCoreResDao.isUserAlreadyCoreResource(productId,userId,DateUtility.dateformatWithOutTime(fromDate),DateUtility.dateformatWithOutTime(toDate),coreResourceFromDB);
	}

	@Override
	@Transactional
	public ProductMaster getProductShowHideTab(Integer testFactoryId, Integer productId) {
		return productMasterDAO.productShowHideTab(testFactoryId, productId);
	}

	@Override
	@Transactional
	public boolean validateUserRole(UserRoles ur, JsonUserRoles jsonUserRoles) {
		return productMasterDAO.validateUserRole(ur, jsonUserRoles);
	}

	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationList(Integer testRunPlanId,Integer type,Integer environmentCombinationId) {
		return productMasterDAO.getRunConfigurationList(testRunPlanId,type,environmentCombinationId);
	}

	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationList(Integer testRunPlanId,Integer type) {
		return productMasterDAO.getRunConfigurationList(testRunPlanId,type);
	}

	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationListByStatus(Integer testRunPlanId,Integer type, Integer status) {
		return productMasterDAO.getRunConfigurationListByStatus(testRunPlanId,type, status);
	}
	
	
	
	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationListByWP(
			Integer workpackageId, Integer type,Integer environmentCombinationId) {
		return productMasterDAO.getRunConfigurationListByWP(workpackageId, type,environmentCombinationId);
	}

	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationListOfWPrcStatus(Integer workpackageId, Integer runConfigStatus) {
		return productMasterDAO.getRunConfigurationListOfWPrcStatus(workpackageId, runConfigStatus);
	}
	@Override
	@Transactional
	public List<ProductTeamResources> getProductTeamResourcesList(
			int productId, Integer jtStartIndex, Integer jtPageSize) {
		return productTeamResourcesDao.getProductTeamResourcesList(productId,jtStartIndex,jtPageSize);
	}

	@Override
	@Transactional
	public void addProductTeamResource(ProductTeamResources pTeamResource) {
		productTeamResourcesDao.addProductTeamResource(pTeamResource);
	}

	@Override
	@Transactional
	public Boolean isUserAlreadyProductTeamResource(Integer productId,
			Integer userId, String fromDate, String toDate,ProductTeamResources productTeamResourcesFromDB) {
		return productTeamResourcesDao.isUserAlreadyProductTeamResource(productId,userId,DateUtility.dateformatWithOutTime(fromDate),DateUtility.dateformatWithOutTime(toDate), productTeamResourcesFromDB);
	}
	
	@Override
	@Transactional
	public List<UserRoleMaster> getProductUserRoles(Integer typeFilter) {
		return productMasterDAO.getProductUserRoles(typeFilter);
	}

	@Override
	@Transactional
	public ProductTeamResources getProductTeamResourceById(
			Integer productTeamResourceId) {
		return productTeamResourcesDao.getProductTeamResourceById(productTeamResourceId);
	}

	@Override
	@Transactional
	public void updateProductTeamResource(
			ProductTeamResources productTeamResourcesFromUI) {
		productTeamResourcesDao.update(productTeamResourcesFromUI);
	}

	@Override
	@Transactional
	public ProductMode getProductModeById(Integer productModeId) {
		return productMasterDAO.getProductModeById(productModeId);
	}

	@Override
	@Transactional
	public void removeProductTeamResourceMapping(
			ProductTeamResources productTeamResource) {
		productTeamResourcesDao.delete(productTeamResource);
	}

	@Override
	@Transactional
	public void mapMobileWithProduct(Integer productId, Integer deviceId,
			String type) {
		productMasterDAO.mapMobileWithProduct(productId, deviceId, type);
	}

	@Override
	@Transactional
	public void mapServerWithProduct(Integer productId, Integer hostId,
			String type) {
		productMasterDAO.mapServerWithProduct(productId, hostId, type);
	}
	
	@Override
	@Transactional
	public ProductMaster getCustomerByProductId(Integer productId) {
		return productMasterDAO.getCustomerByProductId(productId);
	}

	@Override
	@Transactional
	public void mapTestRunPlanWithTestCase(Integer testRunPlanId,
			Integer testCaseId, String action) {
		productMasterDAO.mapTestRunPlanWithTestCase(testRunPlanId, testCaseId, action);
	}

	@Override
	@Transactional
	public void mapTestRunPlanWithFeature(Integer testRunPlanId,
			Integer featureId, String action) {
		productMasterDAO.mapTestRunPlanWithFeature(testRunPlanId, featureId, action);
	}
	
	@Override
	@Transactional
	public ProductSummaryDTO getProductSummary(int productId) {
		ProductSummaryDTO productSummaryDTO = null;
		productSummaryDTO= productMasterDAO.getProductSummaryByProductId(productId);
		if(productSummaryDTO != null){
			productSummaryDTO.setActiveWpCount(productSummaryDTO.getWpCount());
			Integer unMappedTestCaseCount = 0;
			Integer unmappingPercentage = 0;
			Integer totalTestCaseCount = 0;
			unMappedTestCaseCount = getUnMappedTestCaseListCountOfFeatureByProductId(productId);
			productSummaryDTO.setUnMappedTCFeatureCount(unMappedTestCaseCount);
			try {
				unmappingPercentage = (unMappedTestCaseCount*100)/productSummaryDTO.getTestCaseCount();
				productSummaryDTO.setUnMappingPercentage(unmappingPercentage);
			} catch (Exception e) {
				log.info("Exception: "+e.getMessage());
			}
			Integer prodModeId=productSummaryDTO.getProductModeId();
			Integer pTTestManagersCount =  0;
			Integer pTTestLeadsCount =  0;
			Integer pTTestersCount =  0;
			if(prodModeId==1){
				Integer listOfResourcePoolsSize=testFactoryProductCoreResourcesDao.getTestFactoryResourcePoolHasTestFactorySizebyTestFactoryId(productSummaryDTO.getEngagementId());
				productSummaryDTO.setMappedResourcePoolCount(listOfResourcePoolsSize);
				Integer pCTTestManagersCount =  0;
				Integer pCTTestLeadsCount =  0;
				Integer pCTTestersCount =  0;
				
				Map<Integer, Integer> pctCountByRole = getProductCoreResourcesCountByRole(productId);
				if(pctCountByRole != null && pctCountByRole.size() >0){
					for(Map.Entry<Integer, Integer> map: pctCountByRole.entrySet()){
						Integer pcrRoleId = map.getKey();
						Integer pcrRoleCount = map.getValue();
						if(pcrRoleId == 3){
							pCTTestManagersCount = pcrRoleCount;
						}else if(pcrRoleId == 4){
							pCTTestLeadsCount = pcrRoleCount;
						}else if(pcrRoleId == 5){
							pCTTestersCount = pcrRoleCount;
						}
					} 
				}
				productSummaryDTO.setPcTestManagersCount(pCTTestManagersCount);
				productSummaryDTO.setPcTestLeadsCount(pCTTestLeadsCount);
				productSummaryDTO.setPcTestersCount(pCTTestersCount);
			}else{
				Map<Integer, Integer> prtCountByRole = getProductTeamResourcesCountByRole(productId);
				
				if(prtCountByRole != null && prtCountByRole.size() >0){
					for(Map.Entry<Integer, Integer> entry : prtCountByRole.entrySet()){
						Integer roleId = entry.getKey();
						Integer roleCount = entry.getValue();
						if(roleId != null) {
							if(roleId == 3){
								pTTestManagersCount = roleCount;
							}else if(roleId == 4){
								pTTestLeadsCount = roleCount;
							}else if(roleId == 5){
								pTTestersCount = roleCount;
							}
						
						}
					}
				}
				
				productSummaryDTO.setTestManagersCount(pTTestManagersCount);
				productSummaryDTO.setTestLeadsCount(pTTestLeadsCount);
				productSummaryDTO.setTestersCount(pTTestersCount);
			}
			
			Integer testScriptCount = testCaseScriptGenerationService.getTestCaseScriptCount(productId);
			productSummaryDTO.setTestScriptCount(testScriptCount);
			
			Integer featureCount= getFeatureListSize(productId);
			Integer mappedFeatureCount= productFeatureDAO.getMappedFeatureCountOfTestCasesByProductId(productId);
			Double featureMappedPercentage = 0.0;
			if(featureCount != null && featureCount>0 && mappedFeatureCount != null && mappedFeatureCount >0) {
				featureMappedPercentage = (Double.valueOf(mappedFeatureCount)/Double.valueOf(featureCount))*100;
				productSummaryDTO.setFeatureTestCoverage(mappedFeatureCount.toString()+"/"+featureCount.toString()+" ["+String.valueOf(Math.round(featureMappedPercentage))+"%]");
			} else {
				productSummaryDTO.setFeatureTestCoverage("0/0[0%]");
			}
			
			Integer mappedTestcaseWithScript=testCaseAutomationScriptDAO.getMappedTestCasesCountByProductId(productId);
			Double tescriptPercentage=0.0;
			if(testScriptCount != null && testScriptCount>0 && mappedTestcaseWithScript != null && mappedTestcaseWithScript >0) {
				tescriptPercentage= (Double.valueOf(mappedTestcaseWithScript.toString())/Double.valueOf(testScriptCount.toString()))*100;
				productSummaryDTO.setTestCaseAutomationCoverage(mappedTestcaseWithScript.toString()+"/"+testScriptCount.toString()+"["+String.valueOf(Math.round(tescriptPercentage))+"%]");
			} else {
				productSummaryDTO.setTestCaseAutomationCoverage("0/0[0%]");
			}
			
			Double orphanTestCasePercentage=0.0;
			if(productSummaryDTO.getTestCaseCount() !=null && productSummaryDTO.getTestCaseCount() >0 && unMappedTestCaseCount != null && unMappedTestCaseCount >0) {
				orphanTestCasePercentage=(Double.valueOf(unMappedTestCaseCount)/Double.valueOf(productSummaryDTO.getTestCaseCount()))*100;
				productSummaryDTO.setOrphanTestCases(unMappedTestCaseCount.toString()+"/"+productSummaryDTO.getTestCaseCount().toString()+"["+String.valueOf(Math.round(orphanTestCasePercentage))+"%]");
			} else {
				productSummaryDTO.setOrphanTestCases("0/0[0%]");
			}
			Integer defectsCount = testExecutionResultBugDAO.getDefectsCount(productId);
			productSummaryDTO.setDefectCount(defectsCount);
			
		}
		return productSummaryDTO;
	}

	@Override
	@Transactional
	public Map<Integer, Integer> getProductTeamResourcesCountByRole(int productId) {		
		return productTeamResourcesDao.getProductTeamResourcesCountByRole(productId);
	}

	@Override
	@Transactional
	public Integer getUnMappedTestCaseListCountOfFeatureByProductId(int productId) {		
		return productFeatureDAO.getUnMappedTestCaseListCountOfFeatureByProductId(productId);
	}

	@Override
	@Transactional
	public Integer getFeatureListSize(Integer productId) {
		return productFeatureDAO.getFeatureListSize(productId);
	}
	
	@Override
	@Transactional
	public void addtestrunpalngroup(TestRunPlanGroup testRunPlanGroup) {
		productMasterDAO.addtestRunPlanGroup(testRunPlanGroup);
		
	}
	
	
	
	@Override
	@Transactional
	public List<TestRunPlanGroup> listTestRunPlanGroup(int productVersionId, int productId) {
		
		return productMasterDAO.listTestRunPlanGroup(productVersionId, productId);
	}

	@Override
	@Transactional
	public void update(TestRunPlanGroup testRunPlanGroupFromUI) {
		productMasterDAO.update(testRunPlanGroupFromUI);
		
	}

	@Override
	@Transactional
	public List<TestRunPlangroupHasTestRunPlan> listTestRunPlanGroupMap( Integer testRunPlanGroupId) {
		return productMasterDAO.listTestRunPlanGroupMap(testRunPlanGroupId);
	}

	@Override
	@Transactional
	public TestRunPlan addtestrunpalngroupMapping(int testRunPlanId, int testRunPlanGroupId,
			String maporunmap){
			return  productMasterDAO.addtestrunpalngroupMapping(testRunPlanId,testRunPlanGroupId,maporunmap);
	}

	@Override
	@Transactional
	public List<DefectWeeklyReportDTO> listDefectsWeeklyReport(int weekNo,
			int productId,int productVersionId,int productBuildId,int workPackageId) {
    	 
		Date startDate = DateUtility.getDateForDayOfWeek(weekNo,0);
		Date endDate = DateUtility.getDateForDayOfWeek(weekNo,6);
		List<DefectWeeklyReportDTO> defectsWeeklyReportList = new ArrayList<DefectWeeklyReportDTO>();

		Map<DefectApprovalStatusMaster, Integer[][]> defectsWeeklyReportbyApprovalStatusMap = new HashMap<DefectApprovalStatusMaster, Integer[][]>();
		List<DefectApprovalStatusMaster> defectApprovalStatusList =  defectTypeDAO.listDefectApprovalStatus();
		List<DefectWeeklyReportDTO> defectsDTOList =  defectTypeDAO.listDefects(startDate, endDate,productId,productVersionId,productBuildId, workPackageId);
		
		for(DefectApprovalStatusMaster defectApprovalStatus : defectApprovalStatusList){
			Integer dateWiseDefectsCountArr[][] = {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}};
			 int count=0;
			 DefectWeeklyReportDTO defectWeeklyReportDTO=new DefectWeeklyReportDTO();
		 for (DefectWeeklyReportDTO defectWeeklyReportDTOFromDB : defectsDTOList) {
			 if(defectApprovalStatus.getApprovalStatusId().equals(defectWeeklyReportDTOFromDB.getDefectsApprovalStatusId())){
				 count++;
				 int dayOfWeek = DateUtility.getDayOfWeek(defectWeeklyReportDTOFromDB.getBugCreatedDate());
				 
				 dateWiseDefectsCountArr = loadDefectsByDate(dateWiseDefectsCountArr,dayOfWeek,defectWeeklyReportDTOFromDB);
				 defectWeeklyReportDTO.setDefectApprovalStatus(defectApprovalStatus);
				 defectWeeklyReportDTO.setDayNumberOfWeek(dayOfWeek);
				 defectWeeklyReportDTO.setWeekNo(weekNo);
				 defectWeeklyReportDTO.setDateWiseDefectDetailsArry(dateWiseDefectsCountArr);
				
			 }
		 }
		 if(count==0){
			 defectWeeklyReportDTO.setDefectApprovalStatus(defectApprovalStatus);
			 defectWeeklyReportDTO.setWeekNo(weekNo);
			 defectWeeklyReportDTO.setDateWiseDefectDetailsArry(dateWiseDefectsCountArr);
			 defectsWeeklyReportList.add(defectWeeklyReportDTO);
		 }else{
			 defectsWeeklyReportList.add(defectWeeklyReportDTO);
		 }
		}
		defectsWeeklyReportbyApprovalStatusMap = null;
		return defectsWeeklyReportList;
		
	}

	@Override
	@Transactional
	public TestRunPlanGroup getTestRunPlanGroupById(int testRunPlanGroupId) {
		return productMasterDAO.getTestRunPlanGroupById(testRunPlanGroupId);
	}

	@Override
	@Transactional
	public void update(TestRunPlangroupHasTestRunPlan testRunPlanGroupHasFromUI) {
		productMasterDAO.update(testRunPlanGroupHasFromUI);
		
	}
	
	@Override
	@Transactional
	public boolean mapAndUnmapDCTestCases(Integer sourceDCId,
			Integer destinationDCId, Integer tcId) {
		if(sourceDCId != null && sourceDCId > 0 && destinationDCId != null && destinationDCId > 0 && tcId != null && tcId>0){
			decouplingCategoryDAO.updateDecouplingCategoriesTestCasesOneToMany(tcId, sourceDCId, "unmap");
			decouplingCategoryDAO.updateDecouplingCategoriesTestCasesOneToMany(tcId, destinationDCId, "map");
			return true;
		}else{
			return false;
		}
	}
	
	private Integer[][]  loadDefectsByDate(Integer[][] defectCountByDate, int dayOfWeek, DefectWeeklyReportDTO defectDTO) {
		int foundInStageId = defectDTO.getFoundInStageId();
		switch(dayOfWeek) {
			// Please dont change this ordering of case numbers
			case 1: // Sunday
				if(foundInStageId == 1){
					defectCountByDate[6][1] = defectDTO.getBugsCount();
					break;
				}else{
					defectCountByDate[6][0] = defectDTO.getBugsCount();
					break;
				}
				
			case 2:  // Monday
				if(foundInStageId == 1){
					if(defectDTO.getBugsCount() != null){
						defectCountByDate[0][1] = defectDTO.getBugsCount();
					}else{
						defectCountByDate[0][1] = 0;
					}
					break;
				}else{
					if(defectDTO.getBugsCount() != null){
						defectCountByDate[0][0] = defectDTO.getBugsCount();
					}else{
						defectCountByDate[0][0] =0;
					}
					break;
				}
			case 3:
				if(foundInStageId == 1){
					defectCountByDate[1][1] = defectDTO.getBugsCount();
					break;
				}else{
					defectCountByDate[1][0] = defectDTO.getBugsCount();
					break;
				}
			case 4:
				if(foundInStageId == 1){
					defectCountByDate[2][1] = defectDTO.getBugsCount();
					break;
				}else{
					defectCountByDate[2][0] = defectDTO.getBugsCount();
					break;
				}
			case 5:
				if(foundInStageId == 1){
					defectCountByDate[3][1] = defectDTO.getBugsCount();
					break;
				}else{
					defectCountByDate[3][0] = defectDTO.getBugsCount();
					break;
				}
			case 6:
				if(foundInStageId == 1){
					defectCountByDate[4][1] = defectDTO.getBugsCount();
					break;
				}else{
					defectCountByDate[4][0] = defectDTO.getBugsCount();
					break;
				}
			case 7:
				if(foundInStageId == 1){
					defectCountByDate[5][1] = defectDTO.getBugsCount();
					break;
				}else{
					defectCountByDate[5][0] = defectDTO.getBugsCount();
					break;
				}
		}
		return defectCountByDate;
	}

	@Override
	@Transactional
	public List<TestRunPlan> listTestRunPlanByProductVersionId(
			int productversionId, int testRunPlanGroupId) {
		return productMasterDAO.listTestRunPlanByProductVersionId(productversionId, testRunPlanGroupId);
	}

	@Override
	@Transactional
	public TestRunPlan getTestRunPlanByName(String name) {
		return productMasterDAO.getTestRunPlanByName(name);
	}

	@Override
	@Transactional
	public boolean isProductExitsInsameTestFactory(Integer testFactoryId,
			String productName) {
		return productMasterDAO.isProductExitsInsameTestFactory(testFactoryId,productName);
	}

	@Override
	@Transactional
	public ProductFeature getByProductFeatureCode(int productFeatureCode) {
		return productFeatureDAO.getByProductFeatureCode(productFeatureCode);
	}

	@Override
	@Transactional
	public ProductBuild getProductBuildIdWithCompleteInitialization(
			int productBuildId) {
		return productBuildDAO.getProductBuildIdWithCompleteInitialization(productBuildId);
	}

	@Override
	@Transactional
	public List<ProductFeature> getFeatureListByProductId(Integer productId,
			Integer jtStartIndex, Integer jtPageSize, boolean initialize) {
		return productFeatureDAO.getFeatureListByProductId(productId, null, jtStartIndex, jtPageSize, initialize);
	}
	
	@Override
	@Transactional
	public List<ProductFeature> getFeatureListByEnagementId(Integer engagementId,
			Integer jtStartIndex, Integer jtPageSize, boolean initialize) {
		return productFeatureDAO.getFeatureListByEnagementId(engagementId, null, jtStartIndex, jtPageSize, initialize);
	}
	
	
	@Override
	@Transactional
	public boolean isProductFeatureExistingByFeatureCode(String productFeatureCode, ProductMaster product) {
		return productFeatureDAO.isProductFeatureExistingByFeatureCode(productFeatureCode, product);
	}

	@Override
	@Transactional
	public ProductFeature getByProductFeatureCode(String productFeatureCode) {
		return productFeatureDAO.getByProductFeatureCode(productFeatureCode);
	}
	
	@Override
	@Transactional
	public ProductFeature getByProductFeatureCode(String productFeatureCode, ProductMaster product){
		return productFeatureDAO.getByProductFeatureCode(productFeatureCode, product);
	}
	
	@Override
	@Transactional
	public ProductFeature getByProductFeatureName(String productFeatureName, ProductMaster product){
		return productFeatureDAO.getByProductFeatureName(productFeatureName, product);
	}

	@Override
	@Transactional
	public void productFeaturesbatchImport(List<ProductFeatureListDTO> listOfFeatureDTOToUpdate, String tcAddOrUpdateAction) { 
		int maxBatchProcessingLimit = 0;
		if(productFeatureMaxBatchCount == null || productFeatureMaxBatchCount.length() == 0){
			maxBatchProcessingLimit = 50;
		}
		maxBatchProcessingLimit = Integer.parseInt(productFeatureMaxBatchCount);
		int count=0;
		for(ProductFeatureListDTO productFeatureDTO : listOfFeatureDTOToUpdate){
			count++;
			if(tcAddOrUpdateAction.equalsIgnoreCase("Update")){
				ProductFeature productFeatureFromExcelData = productFeatureDTO.getProductFeature();
				productFeatureDAO.bulkUpdateOfProductFeature(productFeatureFromExcelData,count,maxBatchProcessingLimit);
				continue;
			}else if(tcAddOrUpdateAction.equalsIgnoreCase("New")){
				ProductFeature productFeatureFromExcelData = productFeatureDTO.getProductFeature();
				if(productFeatureDTO.getParentFeatureCode() != null && productFeatureDTO.getParentFeatureCode().length() != 0){
					ProductFeature parentFeature = productFeatureDAO.getByProductFeatureCode(productFeatureDTO.getParentFeatureCode(), productFeatureDTO.getProductFeature().getProductMaster());
					productFeatureFromExcelData.setParentFeature(parentFeature);
				} 
				else if(productFeatureDTO.getParentFeatureId() != null && productFeatureDTO.getParentFeatureId() != 0){
					ProductFeature parentFeature = productFeatureDAO.getByProductFeatureId(productFeatureDTO.getParentFeatureId());
					productFeatureFromExcelData.setParentFeature(parentFeature);
				} 
				productFeatureDAO.bulkUpdateOfProductFeature(productFeatureFromExcelData,count,maxBatchProcessingLimit);
				if(count==maxBatchProcessingLimit){
					count=0;
				}
			}
		}
	}

	@Override
	@Transactional
	public void updateTestRunPlan(TestRunPlan testRunPlan) {
		
		productMasterDAO.updateTestRunplan(testRunPlan);

	}

	@Override
	@Transactional
	public List<ProductMaster> listProductByEngagementType(int engagementTypeId) {
		return productMasterDAO.listProductByEngagementType(engagementTypeId);
	}
	
	@Override
	@Transactional
	public List<Object[]> getMappedTestCaseListByProductFeatureId(int productId, int productFeatureId, Integer jtStartIndex, Integer jtPageSize) {
	
		return productFeatureDAO.getMappedTestCaseListByProductFeatureId(productId, productFeatureId, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public void mapTestSuiteTestCasesTestRunPlan(int testRunPlanId,
			int testSuiteId, int testCaseId, String type) {
		 productMasterDAO.mapTestSuiteTestCasesTestRunPlan(testRunPlanId, testSuiteId, testCaseId, type);
	}

	@Override
	@Transactional
	public List<TestCaseList> getTestSuiteTestCaseMapped(int testRunPlanId,
			int testSuiteId) {
		return productMasterDAO.getTestSuiteTestCaseMapped(testRunPlanId, testSuiteId);
	}	
	
	@Override
	@Transactional
	public List<RunConfiguration> listRunConfigurationByTestRunPlanId(Integer testRunPlanId,Integer jtStartIndex,Integer jtPageSize) {
	
		return productMasterDAO.listRunConfigurationByTestRunPlanId(testRunPlanId,jtStartIndex,jtPageSize);
	}

	@Override
	@Transactional
	public Map<Integer, Integer> getProductCoreResourcesCountByRole(int productId) {		
		return productCoreResDao.getProductCoreResourcesCountByRole(productId);
	}

	@Override
	@Transactional
	public List<MetricsMaster> listMetrics() {
		return productMasterDAO.listMetrics();
	}

	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationListOfTestRunPlanrcStatus(
			Integer testRunPlanId, Integer runConfigStatus) {
		return productMasterDAO.getRunConfigurationListOfTestRunPlanrcStatus(testRunPlanId, runConfigStatus);
	}

	@Override
	@Transactional
	public List<ProductBuild> listBuildsByProductId(Integer productId) {
		return productBuildDAO.listBuildsByProductId(productId);
	}

	@Override
	@Transactional
	public Integer countAllProduct(Date startDate,Date endDate) {
		return productMasterDAO.countAllProduct(startDate,endDate);
	}
	
	@Override
	@Transactional
	public List<ProductMaster> listAllProductsByLastSyncDate(int startIndex, int pageSize, Date startDate,Date endDate) {
		return productMasterDAO.listAllProductsByLastSyncDate(startIndex, pageSize,startDate, endDate);
	}
	
	@Override
	@Transactional
	public Integer countAllProductVersions(Date startDate,Date endDate) {
		return productVersionListMasterDAO.countAllProductVersions(startDate,endDate);
	}
	
	@Override
	@Transactional
	public List<ProductVersionListMaster> listAllProductVersionByLastSyncDate(int startIndex, int pageSize, Date startDate,Date endDate) {
		return productVersionListMasterDAO.listAllProductVersionByLastSyncDate(startIndex, pageSize,startDate,endDate);
	}
	
	
	@Override
	@Transactional
	public List<TestCaseList> listAllProductTestCasesByLastSyncDate(int startIndex, int pageSize, Date startDate,Date endDate) {
		return testCaseListDAO.listAllProductTestCasesByLastSyncDate(startIndex, pageSize,startDate,endDate);
	}
	
	
	
	@Override
	@Transactional
	public Integer countAllProductBuilds(Date startDate,Date endDate) {
		return productBuildDAO.countAllProductBuilds(startDate,endDate);
	}
	
	@Override
	@Transactional
	public Integer countAllProductTestCases(Date startDate,Date endDate) {
		return productBuildDAO.countAllProductTestCases(startDate,endDate);
	}
	
	
	
	@Override
	@Transactional
	public List<ProductBuild> listAllProductBuildByLastSyncDate(int startIndex, int pageSize, Date startDate,Date endDate) {
		return productBuildDAO.listAllProductBuildByLastSyncDate(startIndex, pageSize,startDate,endDate);
	}
	
	
	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationListOfTestRunPlanrcStatusDeviceorHost(Integer testRunPlanId, Integer runConfigStatus, Integer prodType) {
		return productMasterDAO.getRunConfigurationListOfTestRunPlanrcStatusDeviceorHost(testRunPlanId, runConfigStatus, prodType);
	}

	@Override
	@Transactional
	public Integer countAllProductTestCaseSteps(Date startDate,Date endDate) {
		return testSuiteListDAO.countAllProductTestCaseSteps(startDate,endDate);
	}

	@Override
	@Transactional
	public List<TestCaseStepsList> listAllProductTestCaseStepsByLastSyncDate(int startIndex, int pageSize, Date startDate,Date endDate) {
		return testSuiteListDAO.listAllProductTestCaseStepsByLastSyncDate(startIndex, pageSize,startDate,endDate);
    }

	@Override
	@Transactional
	public Integer countAllBugs(Date startDate, Date endDate) {
		return defectManagementSystemDAO.countAllBugs(startDate,endDate);
	}

	@Override
	@Transactional
	public Integer countAllProductUserRole(Date startDate, Date endDate) {
		return productUserDao.countAllProductUserRole(startDate,endDate);
		
	}

	@Override
	@Transactional
	public List<ProductUserRole> listAllPaginate(int startIndex, int pageSize,Date startDate, Date endDate) {
		return productUserDao.listAllPaginate(startIndex, pageSize, startDate,endDate);
		
	}

	@Override
	@Transactional
	public List<Integer> listTestSuites(int testRunPlanId) {
		return productMasterDAO.listTestSuites(testRunPlanId);
	}
	
	@Override
	@Transactional
	public List<JsonRiskHazardTraceabilityMatrix> fixFailReportService(Integer productId) {
		List<JsonRiskHazardTraceabilityMatrix> jsonriskTraceMatrixList = new ArrayList<JsonRiskHazardTraceabilityMatrix>();
		JsonRiskHazardTraceabilityMatrix jsonriskTH = null;
		
		List<Object[]> fixObjList = productMasterDAO.fixFailReportDAO(productId);
			for (Object[] objects : fixObjList) {
				jsonriskTH = new JsonRiskHazardTraceabilityMatrix();
				String severityName = "";
				if(objects[0] != null){
					Integer defectId= (Integer)objects[0];
					jsonriskTH.setBugid(defectId);	
					severityName = productMasterDAO.getDefectSeverityName(defectId);
					jsonriskTH.setSeverityName(severityName);
			 	}
				if(objects[1] != null){
					String date= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)objects[1]);
					jsonriskTH.setBugCreationTime(date);
			 	}
				if(objects[2] != null){
					jsonriskTH.setResult((String)objects[2]);
			 	}
				if(objects[3] != null){
					jsonriskTH.setTestCaseId((Integer)objects[3]);
			 	}
				if(objects[4] != null){
					jsonriskTH.setIterationNumber((Integer)objects[4]);
			 	}
				if(objects[5] != null){
					jsonriskTH.setLifecyclephase((String)objects[5]);
			 	}
				if(objects[6] != null){
					String date= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)objects[6]);
					jsonriskTH.setDate(date);
			 	}	
				if(objects[7] != null){
					jsonriskTH.setTestcaseexeid((Integer)objects[7]);
			 	}
				jsonriskTraceMatrixList.add(jsonriskTH);
			}	
		return jsonriskTraceMatrixList;		
	}

	@Override
	@Transactional
	public List<JsonRiskHazardTraceabilityMatrix> testFixFailReportService(Integer productId) {
		List<JsonRiskHazardTraceabilityMatrix> jsonriskTestMatrixList = new ArrayList<JsonRiskHazardTraceabilityMatrix>();
		JsonRiskHazardTraceabilityMatrix jsonriskTest = null;
		List<Object[]> fixObjList = productMasterDAO.testFixFailReportDAO(productId);
		for (Object[] objects : fixObjList) {
			jsonriskTest = new JsonRiskHazardTraceabilityMatrix();
					
			if(objects[0] != null){
				jsonriskTest.setResult((String)objects[0]);
		 	}
			if(objects[1] != null){
				jsonriskTest.setTestCaseId((Integer)objects[1]);
		 	}
			if(objects[2] != null){
				jsonriskTest.setTestcasename((String)objects[2]);
		 	}
			if(objects[3] != null){
				jsonriskTest.setIterationNumber((Integer)objects[3]);
		 	}
			if(objects[4] != null){
				jsonriskTest.setLifecyclephase((String)objects[4]);
		 	}
			if(objects[5] != null){
				String date= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)objects[5]);
				jsonriskTest.setDate(date);
		 	}
			if(objects[6] != null){
				jsonriskTest.setFeatureId((Integer)objects[6]);
		 	}
			if(objects[7] != null){
				int tcerid = 0;
				tcerid = (Integer)objects[7];
				jsonriskTest.setTestcaseexeid(tcerid);
				/*Fetching Bug Id and Creation Time*/
				HashMap<String, String> bugmap = productMasterDAO.getBugDetails(tcerid);
				if(bugmap != null & bugmap.size() >0){
					jsonriskTest.setBugid(Integer.parseInt(bugmap.get("bugid")));
					jsonriskTest.setBugCreationTime(bugmap.get("creationTime"));
					jsonriskTest.setStageName(bugmap.get("stagename"));					
				}
				
				jsonriskTestMatrixList.add(jsonriskTest);
		 	}
		}
		return jsonriskTestMatrixList;
	}

	@Override
	@Transactional
	public Integer countAlladdAllProductTeamResources(Date startDate,Date endDate) {
		return productTeamResourcesDao.countAlladdAllProductTeamResources(startDate,endDate);
		
	}

	@Override
	@Transactional
	public List<ProductTeamResources> listAllProductTeam(int startIndex,int pageSize, Date startDate, Date endDate) {
		return productTeamResourcesDao.listAllProductTeam(startIndex,pageSize,startDate,endDate);
		
	}
	
	@Override
	@Transactional
	public ProductFeature addFeature(ProductFeature productFeature){
		return productFeatureDAO.addProductFeature(productFeature);
	}

	@Override
	@Transactional
	public Boolean getTeamResourceByUserIdandProductIdandDate(Integer productId, Integer userId, Date aWpPsDate, Date aWpPeDate) {
		return productTeamResourcesDao.getTeamResourceByUserIdandProductIdandDate(productId,userId,aWpPsDate,aWpPeDate);
		
	}

	@Override
	@Transactional
	public ProductMaster getProductDetailsById(Integer productId) {
		return productMasterDAO.getProductDetailsById(productId);
	}

	@Override
	@Transactional
	public ProductMaster getProductDetailsByIdWithDevicesHostList(Integer productId) {
		return productMasterDAO.getProductDetailsByIdWithDevicesList(productId);
	}
	
	@Override
	@Transactional
	public ProductMaster getProductDetailsByIdWithDevicesList(Integer productId) {
		return productMasterDAO.getProductDetailsByIdWithDevicesList(productId);
	}

	@Override
	@Transactional
	public ProductMaster getProductDetailsByIdWithHostList(Integer productId) {
		return productMasterDAO.getProductDetailsByIdWithHostList(productId);
	}

	@Override
	@Transactional
	public List<TestRunPlan> listTestRunPlanBytestCaseId(int testCaseId) {
		return productMasterDAO.listTestRunPlanBytestCaseId(testCaseId);
	}

	@Override
	@Transactional
	public List<ProductMaster> getProductByUserCustomerAndEngagement(Integer userId, Integer userRoleId, Integer customerId, Integer engagementId, Integer activeStatus) {
		return productMasterDAO.getProductByUserCustomerAndEngagement(userId, userRoleId, customerId, engagementId, activeStatus);
	}
	@Override
	@Transactional
	public List<ProductMaster> getProductsByEngagementId(List<Integer> testFactoryId) {
		return productMasterDAO.getProductsByEngagementId(testFactoryId);
	}

	@Override
	@Transactional
	public List<ProductFeature> listChildNodesInHierarchyinLayers(ProductFeature feature) {
		return productFeatureDAO.listChildNodesInHierarchyinLayers(feature);
	}

	@Override
	@Transactional
	public void updateHierarchyIndexForDelete(Integer leftIndex, Integer rightIndex) {
		
		productFeatureDAO.updateHierarchyIndexForDelete(leftIndex, rightIndex);
	}

	@Override
	@Transactional
	public void updateHierarchyIndexForNew(Integer parentRightIndex) {
		
		productFeatureDAO.updateHierarchyIndexForNew(parentRightIndex);
	}

	@Override
	@Transactional
	public ProductMaster getProductByNameAndTestfactoryId(String product,Integer testFactoryId) {
		return productMasterDAO.getProductByNameAndTestfactoryId(product,testFactoryId);
	}

	@Override
	public ProductFeature getRootFeature() {
		return productFeatureDAO.getRootFeature();
	}
	
	@Override
	@Transactional
	public List<ProductFeatureProductBuildMapping> getProductFeatureAndProductBuildMappingList(Integer productId) {
		try {
			return productFeatureDAO.getProductFeatureAndProductBuildMappingList(productId);
		}catch(Exception e) {
		log.error("Error in getProductFeatureAndProductBuildMappingList",e);	
		}
		return null;
		
	}
	
	@Override
	@Transactional
	public void mappingProductFeatureToProductBuild(ProductFeatureProductBuildMapping productFeatureProductBuildMapping) {
		try {
			productFeatureDAO.mappingProductFeatureToProductBuild(productFeatureProductBuildMapping);
		}catch(Exception e) {
			log.error("Error in mappingProductFeatureToProductBuild",e);
		}
	}

	@Override
	@Transactional
	public void mapTestSuiteWithRunConfiguration(Integer runConfigId,
			Integer testSuiteId, String action) {
		productMasterDAO.mapTestSuiteWithRunConfiguration(runConfigId, testSuiteId, action);		
	}

	@Override
	@Transactional
	public void mapTestSuiteTestCasesRunConfiguration(int runConfigId,int testSuiteId, int testCaseId, String type) {
		productMasterDAO.mapTestSuiteTestCasesRunConfiguration(runConfigId, testSuiteId, testCaseId, type);		
	}

	@Override
	@Transactional
	public List<TestRunPlan> listTestRunPlanByProductId(Integer productId) {
		return productMasterDAO.listTestRunPlanByProductId(productId);
	}

	@Override
	@Transactional
	public List<RunConfiguration> listRunConfiguration(Integer productId) {
		return productMasterDAO.listRunConfiguration(productId);
	}

	@Override
	@Transactional
	public List<TestCaseList> getRunConfigTestSuiteTestCaseMapped(int runConfigId, int testSuiteId) {
		return productMasterDAO.getRunConfigTestSuiteTestCaseMapped(runConfigId, testSuiteId);
	}
	
	@Override
	@Transactional
	public void unMappingProductFeatureToProductBuild(ProductFeatureProductBuildMapping productFeatureProductBuildMapping) {
		try {
			productFeatureDAO.unMappingProductFeatureToProductBuild(productFeatureProductBuildMapping);
		}catch(Exception e) {
			log.error("Error in mappingProductFeatureToProductBuild",e);
		}
	}

	@Override
	@Transactional
	public List<ProductFeature> getFeatureListByProductIdAndVersionIdAndBuild(
			Integer productId, Integer versionId, Integer buildId,
			Integer featureStatus, Integer jtStartIndex, Integer jtPageSize) {
		try {
			return productFeatureDAO.getFeatureListByProductIdAndVersionIdAndBuild(productId,versionId,buildId,featureStatus, jtStartIndex, jtPageSize);
		}catch(Exception e) {
			log.error("Error in getFeatureListByProductIdAndVersionIdAndBuild",e);
		}
		return null;
	}

	@Override
	@Transactional
	public List<ProductFeatureProductBuildMapping> getProductFeatureAndProductBuildMappingByVersionIdOrBuildId(
			Integer productId, Integer versionId, Integer buildId) {
		try {
			return productFeatureDAO.getFeatureListByProductIdAndVersionIdAndBuild(productId,versionId,buildId);
		}catch(Exception e) {
			log.error("Error in getProductFeatureAndProductBuildMappingByVersionIdOrBuildId",e);
		}
		return null;
	}
	

	@Override
	@Transactional
	public List<ProductFeatureProductBuildMapping> getProductFeatureAndProductBuildMappingId(Integer mappingId) {
		try {
			return productFeatureDAO.getProductFeatureAndProductBuildMappingId(mappingId);
		}catch(Exception e) {
			log.error("Error in getProductFeatureAndProductBuildMappingId",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<ProductFeatureProductBuildMapping> getProductFeatureAndProductBuildMappingList() {
		try {
			return productFeatureDAO.getProductFeatureAndProductBuildMappingList();
		}catch(Exception e) {
			log.error("Error in getProductFeatureAndProductBuildMappingId",e);
		}
		return null;
	}

	@Override
	@Transactional
	public List<Integer> getFeatureTotestCaseMappingByFeatureId(Integer productFeatureId) {
		try {
		return productFeatureDAO.getFeatureTotestCaseMappingByFeatureId(productFeatureId);
		}catch(Exception e) {
			
		}
		return null;
	}

	@Override
	@Transactional
	public Integer getTotalTestCaseCountForATestRunPlan(Integer testRunPlanId) {
		return productMasterDAO.getTotalTestCaseCountForATestRunPlan(testRunPlanId);
	}

	@Override
	@Transactional
	public boolean isTestCaseAlreadyMapped(Integer testRunPlanId, Integer testSuiteId,Integer testCaseId){
		return productMasterDAO.isTestCaseAlreadyMapped(testRunPlanId, testSuiteId, testCaseId);
	}

	@Override
	@Transactional
	public List<Object[]> getISERecommendatedTestCaseListByBuildId(Integer buildId) {
		try {
			return iseRecommendedTestCaseDAO.getISERecommendatedTestCaseListByBuildId(buildId);
		}catch(Exception e) {
			
		}
		return null;
	}

	@Override
	@Transactional
	public List<TestCasePriority> listFeatureExecutionPriority() {
		try {
			return productFeatureDAO.listFeatureExecutionPriority();
		}catch(Exception e) {
			
		}
		return null;
	}

	@Override
	@Transactional
	public List<Object[]> getMappedTestScriptListByTestcaseId(int productId,
			Integer testcaseId, Integer jtStartIndex, Integer jtPageSize) {
		try {
			return productFeatureDAO.getMappedTestScriptListByTestcaseId(productId, testcaseId, jtStartIndex, jtPageSize);
		}catch(Exception e) {
			log.error("Error while getMappedTestScriptListByTestcaseId",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public void unMapRunConfigurationTestRunPlan(int testrunplanid, int runconfigid){
		productMasterDAO.unMapRunConfigurationTestRunPlan(testrunplanid, runconfigid);
	}

	@Override
	@Transactional
	public Integer getMappedTestcasecountByFeatureId(int featureId) {
		try {
			return productFeatureDAO.getMappedTestcasecountByFeatureId(featureId);
		}catch(Exception e) {
			
		}
		return 0;
	}

	@Override
	@Transactional
	public List<Object[]> getISERecommendatedTestCaseCategoryCountByBuildId(
			Integer buildId) {
		try {
			return iseRecommendedTestCaseDAO.getISERecommendatedTestCaseCategoryCountByBuildId(buildId);
		}catch(Exception e) {
			log.error("Error while fetching getISERecommendatedTestCaseCategoryCountByBuildId",e);
		}
		return null;
	}

	@Override
	@Transactional
	public Integer getISERecommendatedTestCaseCountByBuildId(Integer buildId) {
		try {
			return iseRecommendedTestCaseDAO.getISERecommendatedTestCaseCountByBuildId(buildId);
		}catch(Exception e) {
			
		}
		
		return null;
	}

	@Override
	@Transactional
	public boolean isTestConfigurationTestCaseAlreadyMapped(Integer testConfigurationId, Integer testSuiteId, Integer testCaseId) {
		return productMasterDAO.isTestConfigurationTestCaseAlreadyMapped(testConfigurationId, testSuiteId, testCaseId);
	}

	@Override
	@Transactional
	public void mapTestConfigurationTestSuiteTestCase(Integer runconfigId,
			Integer testSuiteId, List<TestCaseList> testCaseLists, String string) {
		productMasterDAO.mapTestConfigurationTestSuiteTestCase(runconfigId,  testSuiteId, testCaseLists, string);		
	}

	@Override
	@Transactional
	public VerificationResult testPlanReadinessCheck(Integer testPlanId) {		
		VerificationResult testPlanVerificationResult = new VerificationResult();		
		testPlanVerificationResult.setIsReady("Yes");
		boolean isReady = true;
		StringBuffer sb = new StringBuffer();
		try{
			TestRunPlan testPlan = productMasterDAO.getTestRunPlanById(testPlanId);			
			//Is the test plan valid
			if (testPlan == null) {
				testPlanVerificationResult.setIsReady("No");
				sb.append("The Test Plan ID : " + testPlanId + " is not a valid ID");
				testPlanVerificationResult.addMessage(sb.toString());
				return testPlanVerificationResult;
			}
			//Check if the test plan has build assoaciated to it
			if (testPlan.getProductBuild() == null) {
				testPlanVerificationResult.setIsReady("No");
				sb.append("The Test Plan ID : " + testPlanId + " does not have build");
				testPlanVerificationResult.addMessage(sb.toString());
				return testPlanVerificationResult;
			}
			//Check is test configurations are defined for the test plan. There should be a minimum of 1
			Set<RunConfiguration> testConfigurations= testPlan.getRunConfigurationList();
			if (testConfigurations == null || testConfigurations.isEmpty()) {
				testPlanVerificationResult.setIsReady("No");
				sb.append("The Test Plan ID: " + testPlanId + " does not have any Test Configurations specified for execution<br>");
				testPlanVerificationResult.addMessage(sb.toString());
				return testPlanVerificationResult;
			} else {
				sb.append("The Test Plan ID : " + testPlanId + " has " + testConfigurations.size() + " Test Configurations defined.<br>");
			}
			
			//Test Data , Object Repository validation
			
			List<Attachment> attachmentsForTestRunPlan = attachmentDAO.listDataRepositoryAttachments(testPlan.getTestRunPlanId());
			List<Attachment> objectRepositoryFiles = new ArrayList<Attachment>();
			List<Attachment> testDataFiles = new ArrayList<Attachment>();
			
			//Check if UI object repository files and Test data files are specified for the Test COnfiguration
			//These are not mandatory, but is good information to validate
			if(attachmentsForTestRunPlan  != null && !attachmentsForTestRunPlan.isEmpty()){
				for(Attachment attachment : attachmentsForTestRunPlan){
					if(attachment.getAttachmentType().equalsIgnoreCase(IDPAConstants.TDTYPE_OBJ_REPOSITORY)){
						objectRepositoryFiles.add(attachment);
					} 
					
					if(attachment.getAttachmentType().equalsIgnoreCase(IDPAConstants.TDTYPE_TESTDATA)){
						testDataFiles.add(attachment);
					}
				}
			}
			sb.append(testPlan.getTestRunPlanName() + " has " + objectRepositoryFiles.size() + " UI Object Repository files and " + testDataFiles.size() + " Test Data files are specified.<br>" );
			
			//Verify at least one test configuration is ready for execution
			int validTestConfigurationsCounter = 0;
			for (RunConfiguration testConfiguration : testConfigurations) {
				VerificationResult testConfigurationReadinessCheck = testConfigurationReadinessCheck(testConfiguration, testPlan, sb);
				if (testConfigurationReadinessCheck.getIsReady().equalsIgnoreCase("Yes")) {
					validTestConfigurationsCounter++;
				} else {
					
				}
				sb.append(testConfigurationReadinessCheck.getVerificationMessage() + "<br>");
			}
			
			if (validTestConfigurationsCounter <= 0) {
				
				testPlanVerificationResult.setIsReady("No");
			}
				
			//Verification is complete
			//Update the outcome to the test plan readiness object
			testPlanVerificationResult.addMessage(sb.toString());
		} catch(Exception e){
			log.error("Error in checking test plan readiness : ", e);
		}
		return testPlanVerificationResult;
	}
	
	@Override
	@Transactional
	public VerificationResult testConfigurationReadinessCheck(RunConfiguration testConfiguration, TestRunPlan testPlan, StringBuffer sb) {
		
		VerificationResult testConfigurationResult = new VerificationResult();
		testConfigurationResult.setIsReady("Yes");
		try { 
			Set<TestSuiteList> testConfigTestSuiteList = testConfiguration.getTestSuiteLists();
			StringBuffer messageSB = new StringBuffer();
			String testScriptLevel = testPlan !=null?testPlan.getTestScriptsLevel():"";
			//Verify if the test configuration has test suites mapped to it. At least one test suite has to be mapped and the test suite should have atleast one test case selected
			if (testConfigTestSuiteList == null || testConfigTestSuiteList.isEmpty()) {
				testConfigurationResult.setIsReady("No");
				messageSB.append(" Test Configuration : " + testConfiguration.getRunconfigName() + " does not have any Test Suites specified for execution" + System.lineSeparator());
				testConfigurationResult.addMessage(messageSB.toString());
				return testConfigurationResult;
			} else {
				messageSB.append("Test Configuration : " + testConfiguration.getRunconfigName() + " has "+ testConfigTestSuiteList.size() + " Test Suite(s)" + System.lineSeparator());
				
				int validTestSuitesCounter = 0;
				for(TestSuiteList ts : testConfigTestSuiteList){
					List<TestCaseList> runConfigTestCaseList = getRunConfigTestSuiteTestCaseMapped(testConfiguration.getRunconfigId(), ts.getTestSuiteId());
					//Verify that the test suite has executable scripts pack specified. This applies only for non-ATSG test suites and require a script pack
					if(!ts.getScriptTypeMaster().getScriptType().equalsIgnoreCase("GHERKIN")){
						//Get the test script file location based on the test script level property
						String scriptFileLocation = null;
						if(testScriptLevel.equalsIgnoreCase(IDPAConstants.MULTIPLE_TEST_SUITE_LEVEL_SCRIPT_PACKS)){
							scriptFileLocation = ts.getTestSuiteScriptFileLocation();
						}else if(testScriptLevel.equalsIgnoreCase(IDPAConstants.MULTIPLE_TEST_RUN_PLAN_LEVEL_SCRIPT_PACK)){
							scriptFileLocation = testPlan.getTestSuiteScriptFileLocation();
						}else{
							scriptFileLocation = testConfiguration.getTestScriptFileLocation();
						}						
						if(scriptFileLocation != null && !scriptFileLocation.trim().isEmpty()){
							if (scriptFileLocation.endsWith("zip") || scriptFileLocation.endsWith("py") || scriptFileLocation.endsWith("ps1")|| scriptFileLocation.endsWith("java") 
									|| scriptFileLocation.endsWith("jar") || scriptFileLocation.endsWith("js") || scriptFileLocation.endsWith("bat") || 
									scriptFileLocation.endsWith("sh") || scriptFileLocation.endsWith("exe") || scriptFileLocation.endsWith("msi")) {
								validTestSuitesCounter++;
							} else {
								messageSB.append("Test Suite : " + ts.getTestSuiteName() + "[" + ts.getTestSuiteId() + "]" + " script pack is not a valid archive. It should be a zip file." + System.lineSeparator());
							}
						} else {
							messageSB.append("Test Suite : " + ts.getTestSuiteName() + "[" + ts.getTestSuiteId() + "]" + " does not have any script pack specified." + System.lineSeparator());
						}
					}else if(runConfigTestCaseList == null || runConfigTestCaseList.isEmpty()){
						//Verify that a test suite has atleast one test case mapped to it.
						messageSB.append("Test Suite : " + ts.getTestSuiteName() + "[" + ts.getTestSuiteId() + "]" + " does not have any Testcases in it." + System.lineSeparator());
					} else {
						validTestSuitesCounter++;
					}
				}
				
				if (validTestSuitesCounter <= 0) {				
					testConfigurationResult.setIsReady("No");
					messageSB.append(testConfiguration.getRunconfigName() + " does not have any valid test suites mapped to it." + System.lineSeparator());
					testConfigurationResult.addMessage(messageSB.toString());
					return testConfigurationResult;
				}
			}
			
			//Verify that the target device / host is active
			if(testConfiguration.getProductType() != null && (testConfiguration.getProductType().getTypeName().equalsIgnoreCase("MOBILE") || testConfiguration.getProductType().getTypeName().equalsIgnoreCase("DEVICE"))) {
				if(testConfiguration.getGenericDevice() == null && testConfiguration.getGenericDevice().getAvailableStatus() == null 
						&& testConfiguration.getGenericDevice().getAvailableStatus() == null){
					testConfigurationResult.setIsReady("No");
					messageSB.append("Device : "+ testConfiguration.getRunconfigName() +" is INACTIVE" + System.lineSeparator());				
				} else if(testConfiguration.getGenericDevice() != null && testConfiguration.getGenericDevice().getAvailableStatus() != null && testConfiguration.getGenericDevice().getAvailableStatus() == 0){
					testConfigurationResult.setIsReady("No");
					messageSB.append("Device : "+ testConfiguration.getGenericDevice().getName() +" is INACTIVE" + System.lineSeparator());
				} else {
					messageSB.append("Device : "+ testConfiguration.getGenericDevice().getName() +" is ACTIVE" + System.lineSeparator());
				}
			} else if(testConfiguration.getProductType() != null && ( testConfiguration.getProductType().getTypeName().equalsIgnoreCase("WEB") || testConfiguration.getProductType().getTypeName().equalsIgnoreCase("DESKTOP") || testConfiguration.getProductType().getTypeName().equalsIgnoreCase("EMBEDDED"))) {
				if(testConfiguration.getHostList() != null && testConfiguration.getHostList().getCommonActiveStatusMaster() != null 
						&& testConfiguration.getHostList().getCommonActiveStatusMaster().getStatus().equalsIgnoreCase("INACTIVE")){
					testConfigurationResult.setIsReady("No");
					messageSB.append("Target Host: "+testConfiguration.getRunconfigName()+" is INACTIVE" + System.lineSeparator());
				} else if(testConfiguration.getHostList() == null || testConfiguration.getHostList().getCommonActiveStatusMaster() == null ||
						testConfiguration.getHostList().getCommonActiveStatusMaster().getStatus() == null){
					testConfigurationResult.setIsReady("No");
					messageSB.append("Target Host: "+testConfiguration.getRunconfigName()+" is INACTIVE" + System.lineSeparator());					
				} else if(testConfiguration.getHostList() != null && testConfiguration.getHostList().getCommonActiveStatusMaster() != null 
						&& testConfiguration.getHostList().getCommonActiveStatusMaster().getStatus().equalsIgnoreCase("ACTIVE")){
					messageSB.append("Target Host: "+testConfiguration.getHostList().getHostName()+" is ACTIVE" + System.lineSeparator());
				}
			}
			testConfigurationResult.addMessage(messageSB.toString());
		} catch(Exception e) {
			log.error("Error in checking test configuration readiness check ", e);
		}
		return testConfigurationResult;
	}

	@Override
	@Transactional
	public JTableResponse verificationTestPlanReadinessResult(Integer testPlanId) {
		JTableResponse jTableResponse = new JTableResponse();
		try{
			VerificationResult testPlanVerificationResult = testPlanReadinessCheck(testPlanId);
			jTableResponse = new JTableResponse("OK",testPlanVerificationResult.getIsReady(), testPlanVerificationResult.getVerificationMessage());
		} catch(Exception e){
			jTableResponse = new JTableResponse("ERROR", "Error in checking test plan readiness");
		}
		return jTableResponse;
	}

	@Override
	@Transactional
	public Set<RunConfiguration> getRunConfigurationList(Integer testRunPlanId,Integer type,Integer environmentCombinationId, Integer hostId, Integer deviceId) {
		return productMasterDAO.getRunConfigurationList(testRunPlanId,type,environmentCombinationId,hostId,deviceId);
	}

	@Override
	@Transactional
	public boolean hostHasTestConfigurations(int productId, int hostId) {
		return productMasterDAO.hostHasTestConfigurations(productId, hostId);
	}

	@Override
	@Transactional
	public boolean deviceHasTestConfigurations(int productId, int deviceId) {
		return productMasterDAO.deviceHasTestConfigurations(productId, deviceId);
	}

	@Override
	@Transactional
	public List<TestRunPlan> getTestPlansByProductBuildIds(	String productBuildIds) {
		return productMasterDAO.getTestPlansByProductBuildIds(productBuildIds);
	}
	@Override
	@Transactional
	public ProductType getProductTypeByName(String productTypeName) {
		return productMasterDAO.getProductTypeByName(productTypeName);
	}
	
	@Override
	@Transactional
	public String updateProductFeatureTestCase(Integer testCaseId, Integer productFeatureId, String maporunmap) {
		return productFeatureDAO.updateProductFeatureTestCase(testCaseId, productFeatureId, maporunmap);
	}
	
	@Override
	@Transactional
	public int getProductVersionIdBybuildId(int productBuildId) {
		return productVersionListMasterDAO.getProductVersionIdBybuildId(productBuildId);
	}

	@Override
	@Transactional
	public boolean isFeatureExistingByFeatureNameAndFeatureCode(String featureName, String productFeatureCode, ProductMaster product) {
		try {
			return productFeatureDAO.isFeatureExistingByFeatureNameAndFeatureCode(featureName, productFeatureCode, product);
		}catch(Exception e) {
			log.error("Error in service isFeatureExistingByFeatureNameAndFeatureCode",e);
		}
		return false;
	}

	@Override
	@Transactional
	public List<RunConfiguration> getRunConfigurationListByEnvironmentCombination(Integer environmentCombinationId) {
		// TODO Auto-generated method stub
		return productMasterDAO.getRunConfigurationListByEnvironmentCombination(environmentCombinationId);
	}

	@Override
	@Transactional
	public ProductMaster getProductExitsInsameTestFactory(
			Integer testFactoryId, String productName) {
		try {
			return productMasterDAO.getProductExitsInsameTestFactory(testFactoryId,productName);
		} catch(Exception e) {
			
		}
		return null;
	}
	@Override
	@Transactional
	public List<TestCaseList> getRunConfigTestCasesByTestSuite(Integer testcaseId) {
		return productMasterDAO.getRunConfigTestCasesByTestSuite(testcaseId);
	}
	@Override
	@Transactional
	public List<RunConfigurationTSHasTC> getRunConfigTestCaseObjectByTestSuite(Integer testcaseId) {
		return productMasterDAO.getRunConfigTestCaseObjectByTestSuite(testcaseId);
	}

	@Override
	@Transactional
	public RunConfiguration getRunConfigurationByIdWithoutInitialization(Integer runconfigId) {
		return productBuildDAO.getRunConfigurationByIdWithoutInitialization(runconfigId);
	}
	
	@Override
	@Transactional
	public ProductVersionListMaster getProductVersionListByProductIdAndVersionName(Integer productId,String productVersionName) {
		try {
		return productVersionListMasterDAO.getProductVersionListByProductIdAndVersionName(productId,productVersionName);
		}catch(Exception e) {
			log.error("Error in getProductVersionListByProductIdAndVersionName",e);
		}
		return null;
	}

	@Override
	@Transactional
	public ProductBuild getproductBuildByProductIdAndBuildName(Integer productVersionId,String productBuildName) {
		try {
			return productBuildDAO.getproductBuildByProductIdAndBuildName(productVersionId, productBuildName);
		}catch(Exception e) {
			log.error("Error in getproductBuildByProductIdAndBuildName",e);
		}
		return null;
	}

	@Override
	@Transactional
	public TestRunPlan getTestRunPlanBytestRunPlanNameAndProductBuild(String testRunPlanName, Integer productBuildId) {
		return productMasterDAO.getTestRunPlanBytestRunPlanNameAndProductBuild(testRunPlanName, productBuildId);
	}

	@Override
	@Transactional
	public TestRunPlan getFirstTestRunPlanByTestPlanGroupId(Integer testPlanGroupId) {
		return productMasterDAO.getFirstTestRunPlanByTestPlanGroupId(testPlanGroupId);
	}

	@Override
	@Transactional
	public TestRunPlangroupHasTestRunPlan getTestRunPlanGroupHasTestPlanByTestPlanId(Integer testRunPlanId) {
		return productMasterDAO.getTestRunPlanGroupHasTestPlanByTestPlanId(testRunPlanId);
	}

	@Override
	@Transactional
	public TestRunPlanTSHasTC getTestPlanTestSuiteTestCase(Integer testRunPlanId, Integer testSuiteId, Integer testCaseId) {
		return productMasterDAO.getTestPlanTestSuiteTestCase(testRunPlanId, testSuiteId, testCaseId);
	}
}