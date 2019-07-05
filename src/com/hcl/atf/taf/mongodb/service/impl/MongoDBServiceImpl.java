package com.hcl.atf.taf.mongodb.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.Client;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.ActivityDAO;
import com.hcl.atf.taf.dao.CustomFieldDAO;
import com.hcl.atf.taf.dao.DashBoardDAO;
import com.hcl.atf.taf.dao.DashboardSLADAO;
import com.hcl.atf.taf.dao.DefectCollectionDao;
import com.hcl.atf.taf.dao.EnvironmentDAO;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.ProductFeatureDAO;
import com.hcl.atf.taf.dao.ProductUserRoleDAO;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.dao.TestCaseAutomationScriptDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.TestCaseToVersionMappingDAO;
import com.hcl.atf.taf.dao.TestExecutionResultBugDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.CustomFieldValues;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.DefectCollection;
import com.hcl.atf.taf.model.EntityRelationship;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductFeatureHasTestCase;
import com.hcl.atf.taf.model.ProductFeatureProductBuildMapping;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseProductVersionMapping;
import com.hcl.atf.taf.model.TestCaseScript;
import com.hcl.atf.taf.model.TestCaseScriptHasTestCase;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryLab;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.ActivityMongoDAO;
import com.hcl.atf.taf.mongodb.dao.ActivityTaskMongoDAO;
import com.hcl.atf.taf.mongodb.dao.ActivityWorkPackageMongoDAO;
import com.hcl.atf.taf.mongodb.dao.BuildMongoDAO;
import com.hcl.atf.taf.mongodb.dao.ChangeRequestMongoDAO;
import com.hcl.atf.taf.mongodb.dao.ClarificationTrackerMongoDAO;
import com.hcl.atf.taf.mongodb.dao.ElasticSearchDAO;
import com.hcl.atf.taf.mongodb.dao.FeaturesMongoDAO;
import com.hcl.atf.taf.mongodb.dao.MongoDbDAO;
import com.hcl.atf.taf.mongodb.dao.ProductMasterMongoDAO;
import com.hcl.atf.taf.mongodb.dao.ProductTeamResourceMongoDAO;
import com.hcl.atf.taf.mongodb.dao.ProductUserRoleMongoDAO;
import com.hcl.atf.taf.mongodb.dao.ProductVersionMasterMongoDAO;
import com.hcl.atf.taf.mongodb.dao.TestCaseDefectsMongoDAO;
import com.hcl.atf.taf.mongodb.dao.TestCaseExecutionResultMongoDAO;
import com.hcl.atf.taf.mongodb.dao.TestCaseScriptMongoDAO;
import com.hcl.atf.taf.mongodb.dao.TestCaseStepsListMongoDAO;
import com.hcl.atf.taf.mongodb.dao.TestCasesMongoDAO;
import com.hcl.atf.taf.mongodb.dao.TestFactoryMongoDAO;
import com.hcl.atf.taf.mongodb.dao.TestRunJobMongoDAO;
import com.hcl.atf.taf.mongodb.dao.TestSuiteListMongoDAO;
import com.hcl.atf.taf.mongodb.dao.TrendCollectionMongoDAO;
import com.hcl.atf.taf.mongodb.dao.UserListMongoDAO;
import com.hcl.atf.taf.mongodb.dao.WorkPackageMongoDAO;
import com.hcl.atf.taf.mongodb.dao.WorkflowEventMongoDAO;
import com.hcl.atf.taf.mongodb.model.AcitivityWorkPackageMongo;
import com.hcl.atf.taf.mongodb.model.ActivityCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ActivityMongo;
import com.hcl.atf.taf.mongodb.model.ActivityTaskMongo;
import com.hcl.atf.taf.mongodb.model.ChangeRequestMappingMongo;
import com.hcl.atf.taf.mongodb.model.ChangeRequestMongo;
import com.hcl.atf.taf.mongodb.model.ClarificationTrackerMongo;
import com.hcl.atf.taf.mongodb.model.DPAWorkbookCollectionMongo;
import com.hcl.atf.taf.mongodb.model.DefectCollectionMongo;
import com.hcl.atf.taf.mongodb.model.FeatureMasterMongo;
import com.hcl.atf.taf.mongodb.model.ISEDefectCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ISEFeatureMappingMongo;
import com.hcl.atf.taf.mongodb.model.ISEProductBuildMongo;
import com.hcl.atf.taf.mongodb.model.ISETestCaseCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ISETestExecutionCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ProductBuildMongo;
import com.hcl.atf.taf.mongodb.model.ProductFeatureProductBuildMappingCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ProductMasterMongo;
import com.hcl.atf.taf.mongodb.model.ProductTeamResourcesMongo;
import com.hcl.atf.taf.mongodb.model.ProductUserRoleMongo;
import com.hcl.atf.taf.mongodb.model.ProductVersionMasterMongo;
import com.hcl.atf.taf.mongodb.model.ReviewRecordCollectionMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseDefectsMasterMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseExecutionResultMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseMasterMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseProductVersionMappingCollectionMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseScriptMappingMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseScriptMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseStepsListMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseToProductFeatureMappingMongo;
import com.hcl.atf.taf.mongodb.model.TestFactoryMongo;
import com.hcl.atf.taf.mongodb.model.TestFactoryReservedResourcesMongo;
import com.hcl.atf.taf.mongodb.model.TestRunJobMongo;
import com.hcl.atf.taf.mongodb.model.TestSuiteListMongo;
import com.hcl.atf.taf.mongodb.model.TrendCollectionMongo;
import com.hcl.atf.taf.mongodb.model.UserListMongo;
import com.hcl.atf.taf.mongodb.model.UtilizationCollectionMongo;
import com.hcl.atf.taf.mongodb.model.WorkPackageMongo;
import com.hcl.atf.taf.mongodb.model.WorkflowEventMongo;
import com.hcl.atf.taf.mongodb.model.WorkpackageDemandProjectionMongo;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ActivityEffortTrackerService;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.ActivityTaskService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.ChangeRequestService;
import com.hcl.atf.taf.service.ClarificationTrackerService;
import com.hcl.atf.taf.service.CustomFieldService;
import com.hcl.atf.taf.service.CustomerService;
import com.hcl.atf.taf.service.EntityRelationshipService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.ResourceManagementService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestExecutionBugsService;
import com.hcl.atf.taf.service.TestFactoryManagementService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageExecutionService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.ilcm.workflow.model.WorkflowEvent;
import com.hcl.ilcm.workflow.service.WorkflowEventService;


@Service
public class MongoDBServiceImpl implements MongoDBService {

	private static final Log log = LogFactory.getLog(MongoDBServiceImpl.class);

	@Autowired
	private ElasticSearchDAO elasticSearchDAO;
	@Autowired
	ActivityDAO activityDAO;
	@Autowired
	MongoDbDAO mongoDbDAO;
	

	@Autowired
	ActivityWorkPackageService activityWorkPackageService;
	
	@Autowired
	private WorkPackageService workPackageService;
	
	@Autowired
	private ResourceManagementService resourceManagementService;
	
	
	@Autowired
	private WorkPackageExecutionService workPackageExecutionService;
	@Autowired
	private TestCaseExecutionResultMongoDAO testCaseExecutionResultMongoDAO;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private ClarificationTrackerService clarificationTrackerService;
	@Autowired
	private FeaturesMongoDAO featuresMongoDAO;
	@Autowired
	private ActivityWorkPackageMongoDAO activityWorkPackageMongoDAO;
	@Autowired
	private TestCaseDefectsMongoDAO testCaseDefectsMongoDAO;
	@Autowired
	private WorkPackageDAO workPackageDAO;
	@Autowired
	private WorkPackageMongoDAO workPackageMongoDAO;
	@Autowired
	private TestExecutionResultBugDAO testExecutionResultBugDAO;
	@Autowired
	private ProductMasterMongoDAO productMasterMongoDAO;
	@Autowired
	private ClarificationTrackerMongoDAO clarificationTrackerMongoDAO;
	@Autowired
	private ActivityMongoDAO activityMongoDAO;
	@Autowired
	private WorkflowEventMongoDAO workflowEventMongoDAO;
	@Autowired
	private TestSuiteListMongoDAO testSuiteListMongoDAO;
	@Autowired
	private TrendCollectionMongoDAO trendCollectionMongoDAO;
	@Autowired
	private ProductVersionListMasterDAO productVersionListMasterDAO;
	@Autowired
	private ProductBuildDAO productBuildDAO;
	
	@Autowired
	private ProductVersionMasterMongoDAO productVersionMasterMongoDAO;
	@Autowired
	private BuildMongoDAO buildMongoDAO;
	@Autowired
	private TestRunJobMongoDAO testRunJobMongoDAO;
	@Autowired
	private TestFactoryMongoDAO testFactoryMongoDAO;
	@Autowired
	private TestExecutionBugsService testExecutionBugsService;
	@Autowired
	private ActivityTaskService activityTaskService;
	@Autowired
	private UserListService userListService;
	
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActivityTaskMongoDAO activityTaskMongoDAO;
	@Autowired
	private EnvironmentDAO environmentDAO;
	@Autowired
	private TestCaseService testCaseService;
	@Autowired
	private TestCasesMongoDAO testCasesMongoDAO;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private TestFactoryManagementService testFactoryManagementService;
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService;
	@Autowired
	private TestCaseStepsListMongoDAO testCaseStepsListMongoDAO;
	@Autowired
	private DashboardSLADAO dashboardSLADAO;
	@Autowired
	private DashBoardDAO dashBoardDAO;
	@Autowired
	private ProductUserRoleMongoDAO productUserRoleMongoDAO;
	@Autowired
	private ProductUserRoleDAO productUserDao;
	@Autowired
	private ProductTeamResourceMongoDAO productTeamResourceMongoDAO;
	
	@Autowired
	private UserListMongoDAO userListMongoDAO;
	
	@Autowired
	private ProductFeatureDAO productFeatureDAO;
	
	
	@Autowired
	private ActivityEffortTrackerService activityEffortTrackerService;
	@Autowired
	private WorkflowEventService workflowEventService;
	@Autowired
	private ChangeRequestService changeRequestService;
	@Autowired
	private ChangeRequestMongoDAO changeRequestMongoDAO;

	@Autowired
	private CustomFieldService customFieldService;
	
	@Autowired
	private CustomFieldDAO customFieldDAO;
	
	@Autowired
	private EntityRelationshipService entityRelationshipService;
	
	@Autowired
	private TestCaseToVersionMappingDAO testCaseToVersionMappingDAO;
	
	@Autowired
	private TestCaseScriptMongoDAO testCaseScriptMongoDAO;
	
	@Autowired
	private TestCaseAutomationScriptDAO testCaseAutomationScriptDAO;
	
	@Autowired
	private DefectCollectionDao defectCollectionDao;
	
	@Autowired
	private TestCaseListDAO testCaseListDAO;
	
	private Client client;
	private String collectionName;
	private String tableName;
	private String rivertype;
	private String mongoCollection;
	private String indexName;
	private String indexRequestSource;
	
	@Value("#{ilcmProps['MONGODB_AVAILABLE']}")
    private String MONGODB_AVAILABLE;

	@Override
	@Transactional
	public boolean addTestCaseExecutionResult(Integer testCaseResultId) {

		TestCaseExecutionResult testCaseResult = workPackageService.getTestCaseExecutionResultByID(testCaseResultId);
		if (testCaseResult == null) {
			log.info("Could not find TestcaseexecutionResult with ID : " + testCaseResultId);
			return false;
		} else  {
			return addTestCaseExecutionResult(testCaseResult);
		}
	}

	@Override
	@Transactional
	public boolean addTestCaseExecutionResult(TestCaseExecutionResult testCaseResult) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push TestcaseResult to MongoDB. MongoDB is not setup");
			return false;
		}
		if (testCaseResult == null) {
			log.info("Could not push TestcaseResult to MongoDB. Test Case Result is null");
			return false;
		}
		TestCaseExecutionResultMongo testCaseResultMongo = new TestCaseExecutionResultMongo(testCaseResult);
		ISETestExecutionCollectionMongo iseTestCaseExecutionResult= new ISETestExecutionCollectionMongo(testCaseResult);
		testCaseExecutionResultMongoDAO.saveISE(iseTestCaseExecutionResult);
		testCaseExecutionResultMongoDAO.save(testCaseResultMongo);
		return true;
	}

	@Override
	@Transactional
	public boolean addTestCaseExecutionResult(List<TestCaseExecutionResult> testCaseResults, Date lastSyncDate) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push TestcaseResult to MongoDB. MongoDB is not setup");
			return false;
		}
		if (testCaseResults == null || testCaseResults.isEmpty()) {
			log.info("Could not push TestcaseResult to MongoDB. Test Case Result is null");
			return false;
		}
		
		int count = 0;
		List<TestCaseExecutionResultMongo> testCaseResultsMongo = new ArrayList<TestCaseExecutionResultMongo>();
		List<ISETestExecutionCollectionMongo> iseTestCaseResultsMongoList = new ArrayList<ISETestExecutionCollectionMongo>();
		for (TestCaseExecutionResult testCaseResult : testCaseResults) {
			TestCaseExecutionResultMongo testCaseResultMongo = new TestCaseExecutionResultMongo(testCaseResult);
			ISETestExecutionCollectionMongo iseTestExecutionCollectionMongo = new ISETestExecutionCollectionMongo(testCaseResult);
			testCaseResultsMongo.add(testCaseResultMongo);
			iseTestCaseResultsMongoList.add(iseTestExecutionCollectionMongo);
			count++;
		}
		testCaseExecutionResultMongoDAO.save(testCaseResultsMongo);
		testCaseExecutionResultMongoDAO.saveISE(iseTestCaseResultsMongoList);
		log.info("Pushed Testcaseexecutionresults to MongoDB : " + count);
		return true;
	}
	
	@Override
	@Transactional
	public boolean addAllTestCaseExecutionResult(Date startDate, Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push TestcaseResult to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = workPackageService.countAllTestCaseExecutionResult(startDate, endDate);
			if (size < 0) {
				log.info("No TCERs to be pushed to MongoDB");
				return false;
			}
			
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<TestCaseExecutionResult> testCaseResults = workPackageService.listAllTestCaseExecutionResult(i*pageSize, pageSize, startDate, endDate);
				if (testCaseResults != null) {
					for(TestCaseExecutionResult testCaseExecutionResult : testCaseResults){
						
						if(testCaseExecutionResult!=null){
							addTestCaseExecutionResult(testCaseExecutionResult);
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the TCERs to MongoDB",e);
			return false;
		}
	}
	
	/* Product adding to Mongo Start*/
	@Override
	@Transactional
	public boolean addProductToMongoDB(Integer productId) {

		ProductMaster product = productListService.getProductDetailsById(productId);
		if (product == null) {
			log.info("Could not find ProductResult with ID : " + productId);
			return false;
		} else  {
			return addProductToMongoDB(product);
		}
	}
	
	@Override
	@Transactional
	public boolean addProductToMongoDB(ProductMaster product) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add Product to MongoDB. MongoDB is not setup");
			return false;
		}
		if (product == null) {
			log.info("Could not add Product to MongoDB. Product Result is null");
			return false;
		}
		ProductMasterMongo productMongo=new ProductMasterMongo(product);
		productMasterMongoDAO.save(productMongo);
		
		return true;
	}
	@Override
	@Transactional
	public boolean addProductToMongoDB(List<ProductMaster> products, Date lastSyncDate) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push Products to MongoDB. MongoDB is not setup");
			return false;
		}
		if (products == null || products.isEmpty()) {
			log.info("Could not push Products to MongoDB. Product is null");
			return false;
		}
		
		int count = 0;
		List<ProductMasterMongo> productsMasterMongo = new ArrayList<ProductMasterMongo>();
		for (ProductMaster product : products) {
			ProductMasterMongo productMasterMongo = new ProductMasterMongo(product);
			productsMasterMongo.add(productMasterMongo);
			count++;
		}
		productMasterMongoDAO.save(productsMasterMongo);
		log.info("Pushed Products to MongoDB : " + count);
		return true;
	}
	
	@Override
	@Transactional
	public boolean addAllProduct(Date startDate,Date endDate) {
	
		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push Product to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = productListService.countAllProduct(startDate,endDate);
			if (size < 0) {
				log.info("No Products to be pushed to MongoDB");
				return false;
			}
			log.info("Product size "+size);

			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<ProductMaster> products = productListService.listAllProductsByLastSyncDate(i*pageSize, pageSize, startDate,endDate);
				if (products != null) {
					for(ProductMaster product:products){
						addProductToMongoDB(product);
					}
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the Products to MongoDB", e);
			return false;
		}
	}
	
	
	
	/* Product adding to Mongo End*/
	
	
	
	/* Version adding to MongoDb start */
	@Override
	@Transactional
	public boolean addProductVersionToMongoDB(Integer versionId) {
		
		ProductVersionListMaster productVersion = productListService.getProductVersionListMasterById(versionId);
		if (productVersion == null) {
			log.info("Could not find ProductVersionResult with ID : " + versionId);
			return false;
		} else  {
			return addProductVersionToMongoDB(productVersion);
		}
	}

	@Override
	@Transactional
	public boolean addProductVersionToMongoDB(ProductVersionListMaster productVersion) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add ProductVersion to MongoDB. MongoDB is not setup");
			return false;
		}
		if (productVersion == null) {
			log.info("Could not add ProductVersion to MongoDB. ProductVersion Result is null");
			return false;
		}
		ProductVersionMasterMongo versionMongo=new ProductVersionMasterMongo(productVersion);
		productVersionMasterMongoDAO.save(versionMongo);
		return true;
	}
	
	@Override
	@Transactional
	public boolean addProductVersionToMongoDB(List<ProductVersionListMaster> productVersions, Date lastSyncDate) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push ProductVersions to MongoDB. MongoDB is not setup");
			return false;
		}
		if (productVersions == null || productVersions.isEmpty()) {
			log.info("Could not push ProductVersions to MongoDB. productVersions is null");
			return false;
		}
		
		int count = 0;
		List<ProductVersionMasterMongo> productVersionsMasterMongo = new ArrayList<ProductVersionMasterMongo>();
		for (ProductVersionListMaster productVersion : productVersions) {
			ProductVersionMasterMongo productVersionMasterMongo = new ProductVersionMasterMongo(productVersion);
			productVersionsMasterMongo.add(productVersionMasterMongo);
			count++;
		}
		productVersionMasterMongoDAO.save(productVersionsMasterMongo);
		log.info("Pushed ProductVersions to MongoDB : " + count);
		return true;
	}
	
	
	@Override
	@Transactional
	public boolean addAllProductVersionMongoDB(Date startDate,Date endDate) {
		
		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push ProductVersions to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = productListService.countAllProductVersions(startDate,endDate);
			if (size < 0) {
				log.info("No ProductVersions to be pushed to MongoDB");
				return false;
			}
			log.info("ProductVersions size "+size);
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<ProductVersionListMaster> productVersions = productListService.listAllProductVersionByLastSyncDate(i*pageSize, pageSize, startDate,endDate);
				if (productVersions != null) {
					
					for(ProductVersionListMaster productVersion: productVersions)
					addProductVersionToMongoDB(productVersion);
					
					
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the ProductVersions to MongoDB ", e);
			return false;
		}
	}
	/* Version adding to MongoDb End */
	
	
	/* Build adding to MongoDB*/
	
	@Override
	@Transactional
	public boolean addProductBuildToMongoDB(Integer buildId) {
		
		ProductBuild productBuild = productListService.getProductBuildById(buildId, 0);
		if (productBuild == null) {
			log.info("Could not find ProductBuild Result with ID : " + buildId);
			return false;
		} else  {
			return addProductBuildToMongoDB(productBuild);
		}
	}
	
	@Override
	@Transactional
	public boolean addProductBuildToMongoDB(ProductBuild productBuild) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add ProductBuild to MongoDB. MongoDB is not setup");
			return false;
		}
		if (productBuild == null) {
			log.info("Could not add ProductBuild to MongoDB. ProductVersion Result is null");
			return false;
		}
		ProductBuildMongo productBuildMongo=new ProductBuildMongo(productBuild);
		buildMongoDAO.save(productBuildMongo);
		ISEProductBuildMongo iseProductBuildMongo=new ISEProductBuildMongo(productBuild);
		buildMongoDAO.saveISE(iseProductBuildMongo);
		return true;
	}
	
	@Override
	@Transactional
	public boolean addProductBuildToMongoDB(List<ProductBuild> ProductBuilds, Date lastSyncDate) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push ProductBuilds to MongoDB. MongoDB is not setup");
			return false;
		}
		if (ProductBuilds == null || ProductBuilds.isEmpty()) {
			log.info("Could not push ProductBuilds to MongoDB. ProductBuilds is null");
			return false;
		}
		
		int count = 0;
		List<ProductBuildMongo> productBuildsMongo = new ArrayList<ProductBuildMongo>();
		for (ProductBuild productBuild : ProductBuilds) {
			ProductBuildMongo buildMongo = new ProductBuildMongo(productBuild);
			productBuildsMongo.add(buildMongo);
			count++;
		}
		buildMongoDAO.save(productBuildsMongo);
		log.info("Pushed ProductBuilds to MongoDB : " + count);
		return true;
	}
	
	@Override
	@Transactional
	public boolean addAllProductBuildToMongoDB(Date startDate,Date endDate) {
		
		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push ProductBuilds to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = productListService.countAllProductBuilds(startDate,endDate);
			if (size < 0) {
				log.info("No ProductBuilds to be pushed to MongoDB");
				return false;
			}
			
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<ProductBuild> productBuilds = productListService.listAllProductBuildByLastSyncDate(i*pageSize, pageSize, startDate,endDate);
				
				if (productBuilds != null) {
					for(ProductBuild   build : productBuilds){
						addProductBuildToMongoDB(build);
					}
				}
				
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the ProductBuilds to MongoDB", e);
			return false;
		}
	}
	/* Build adding to MongoDb End */
	
	/*Test run Job adding start*/
	
	@Override
	@Transactional
	public boolean addTestRunJobToMongoDB(Integer testRunJobId) {
		
//		TestRunJob testRunJob = workPackageService.getTestRunJobById(testRunJobId);
		TestRunJob testRunJob =	environmentDAO.getTestRunJobById(testRunJobId);
		if (testRunJob == null) {
			log.info("Could not find TestRunJob Result with ID : " + testRunJobId);
			return false;
		} else  {
			return addTestRunJobToMongoDB(testRunJob);
		}
	}
	
	@Override
	@Transactional
	public boolean addTestRunJobToMongoDB(TestRunJob testRunJob) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add TestRunJob to MongoDB. MongoDB is not setup");
			return false;
		}
		if (testRunJob == null) {
			log.info("Could not add TestRunJob to MongoDB. TestRunJob Result is null");
			return false;
		}
		TestRunJobMongo testRunJobMongo=new TestRunJobMongo(testRunJob);
		testRunJobMongo.setResult(workPackageExecutionService.getTestRunJobResultStatus(testRunJob.getTestRunJobId()));

		/*Because of session error only we are setting TF Name*/
		TestFactory testfc=	testFactoryManagementService.getTestFactoryById(testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().
				getTestFactory().getTestFactoryId());
		log.debug("TestFactoryName*** "+testfc.getTestFactoryName());
		testRunJobMongo.setTestfactoryName(testfc.getTestFactoryName());
		
		testRunJobMongoDAO.save(testRunJobMongo);
		return true;
	}
	
	@Override
	@Transactional
	public boolean addTestRunJobToMongoDB(List<TestRunJob> testRunJobs, Date lastSyncDate) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push TestRunJob to MongoDB. MongoDB is not setup");
			return false;
		}
		if (testRunJobs == null || testRunJobs.isEmpty()) {
			log.info("Could not push TestRunJob to MongoDB. Test Case Result is null");
			return false;
		}
		
		int count = 0;
		List<TestRunJobMongo> testRunJobsMongo = new ArrayList<TestRunJobMongo>();
		for (TestRunJob testRunJob : testRunJobs) {
			TestRunJobMongo testRunJobMongo = new TestRunJobMongo(testRunJob);
			testRunJobMongo.setResult(workPackageExecutionService.getTestRunJobResultStatus(testRunJob.getTestRunJobId()));
		/*Because of session error only we are setting TF Name*/
			TestFactory testfc=	testFactoryManagementService.getTestFactoryById(testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().
					getTestFactory().getTestFactoryId());
			log.info("TestFactoryName*** "+testfc.getTestFactoryName());
			testRunJobMongo.setTestfactoryName(testfc.getTestFactoryName());
			
			testRunJobsMongo.add(testRunJobMongo);
			count++;
		}
		testRunJobMongoDAO.save(testRunJobsMongo);
		log.info("Pushed TestRunJobMongo to MongoDB : " + count);
		return true;
	}
	
	@Override
	@Transactional
	public boolean addAllTestRunJobToMongoDB(Date startDate,Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push TestRunJob to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = workPackageService.countAllTestRunJob(startDate,endDate);
			if (size < 0) {
				log.info("No TestRunJob to be pushed to MongoDB");
				return false;
			}
			log.info("size of testRunJob "+size);
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<TestRunJob> testRunJobs = workPackageService.listAllTestRunJob(i*pageSize, pageSize, startDate,endDate);
				if (testRunJobs != null) {
					for(TestRunJob   testRunJob : testRunJobs){
						addTestRunJobToMongoDB(testRunJob);
					}
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the TestRunJobs to MongoDB", e);
			return false;
		}
	}
	
	/* Test run Job adding End */
	
	
	/* TestFactory Add Start */
	
	@Override
	@Transactional
	public boolean addTestFactoryToMongo(Integer testFactoryId) {
		
		TestFactory testFactory=testFactoryManagementService.getTestFactoryById(testFactoryId);
		if (testFactory == null) {
			log.info("Could not find TestFactory Result with ID : " + testFactoryId);
			return false;
		} else  {
			return addTestFactoryToMongo(testFactory);
		}
	}
	
	@Override
	@Transactional
	public boolean addTestFactoryToMongo(TestFactory testFactory) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add TestFactory to MongoDB. MongoDB is not setup");
			return false;
		}
		if (testFactory == null) {
			log.info("Could not add TestFactory to MongoDB. TestRunJob Result is null");
			return false;
		}
		TestFactoryMongo testFactoryMongo=new TestFactoryMongo(testFactory);
		testFactoryMongoDAO.save(testFactoryMongo);
		return true;
	}
	
	
	@Override
	@Transactional
	public boolean addTestFactoryToMongo(List<TestFactory> testFactories, Date lastSyncDate) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push ProductBuilds to MongoDB. MongoDB is not setup");
			return false;
		}
		if (testFactories == null || testFactories.isEmpty()) {
			log.info("Could not push TestFactorys to MongoDB. TestFactorys is null");
			return false;
		}
		
		int count = 0;
		List<TestFactoryMongo> testFactoriesMongo = new ArrayList<TestFactoryMongo>();
		for (TestFactory testfac : testFactories) {
			TestFactoryMongo testFacMongo = new TestFactoryMongo(testfac);
			testFactoriesMongo.add(testFacMongo);
			count++;
		}
		testFactoryMongoDAO.save(testFactoriesMongo);
		log.info("Pushed TestFactories to MongoDB : " + count);
		return true;
	}
	
	@Override
	@Transactional
	public boolean addAllTestFactoryToMongo(Date startDate,Date endDate) {
		
		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push TestFactories to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = testFactoryManagementService.countAllTestFactory(startDate,endDate);
			
			if (size < 0) {
				log.info("No TestFactories to be pushed to MongoDB");
				return false;
			}
			
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				List<TestFactory> testFactories = testFactoryManagementService.listAllTestFactoryByLastSyncDate(i*pageSize, pageSize, startDate,endDate);
				log.info("testFactories size  "+testFactories.size());
				if (testFactories != null) {
					for(TestFactory testFactory:testFactories)
						addTestFactoryToMongo(testFactory);
				}
				
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the TestFactories to MongoDB", e);
			return false;
		}
	}
	
	
	
	/* Test Case add start*/
	
	@Override
	@Transactional
	public boolean addProductTestCaseToMongoDB(Integer testCaseId) {
		
		TestCaseList testCase = testCaseService.getTestCaseById(testCaseId);
		if (testCase == null) {
			log.info("Could not find testCase Result with ID : " + testCaseId);
			return false;
		} else  {
			return addProductTestCaseToMongoDB(testCase);
		}
	}
	
	@Override
	@Transactional
	public boolean addProductTestCaseToMongoDB(TestCaseList testCase) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add ProductTestCase to MongoDB. MongoDB is not setup");
			return false;
		}
		if (testCase == null) {
			log.info("Could not add ProductTestCase to MongoDB. ProductVersion Result is null");
			return false;
		}
		TestCaseMasterMongo testCaseMasterMongo=new TestCaseMasterMongo(testCase);
		ISETestCaseCollectionMongo iseTestCaseMasterMongo=new ISETestCaseCollectionMongo(testCase);
		Customer custom=customerService.getCustomerId(testCase.getProductMaster().getCustomer().getCustomerId());
		log.info("Name***********"+custom.getCustomerName());
		testCaseMasterMongo.setCustomerName(custom.getCustomerName());
		testCasesMongoDAO.save(testCaseMasterMongo);
		testCasesMongoDAO.saveISE(iseTestCaseMasterMongo);
		return true;
	}


	
	@Override
	@Transactional
	public void pushAlliLCMDataToMongoDB(Date startDate,Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push Data to MongoDB. MongoDB is not setup");
				return;
			}
			
			addAllProduct(startDate,endDate);
			addAllProductVersionMongoDB(startDate,endDate);
			addAllProductTestCaseToMongoDB(startDate,endDate);
			addAllTestCaseExecutionResult(startDate,endDate);
			addAllWorkPackages(startDate,endDate);
			addAllBugs(startDate,endDate);
			addAllProductUserRole(startDate,endDate);
			addAllTestRunJobToMongoDB(startDate,endDate);
			addAllProductBuildToMongoDB(startDate, endDate);
			addAllTestFactoryToMongo(startDate, endDate);
			addAllTestStepsToMongoDB(startDate, endDate);
			addAllActivitytoMongoD(startDate, endDate);
			addAllActivityWorkPackagetoMongoDB(startDate, endDate);
			addAllProductTeamResources(startDate, endDate);
			addAllWorkflowEventsToMongoDB(startDate, endDate);
			addAllUserList(startDate, endDate);
			addAllClarificationToMongoDB(startDate, endDate);
			addAllProductFeatures(startDate, endDate);
			addAllTestSuiteToMongoDB(startDate, endDate);
			addAllChangeRequestsToMongoDB(startDate, endDate);
			addAllResourceDemandToMongoDB(startDate, endDate);
			addAllReseveredResourceToMongoDB(startDate,endDate);
			addAllChangeRequestMappingToMongoDB(startDate, endDate);
	
		} catch (Exception e) {
			log.error("Unable to push all to MongoDB", e);
			return;
		}
	}

	@Override
	@Transactional
	public boolean addProductFeature(Integer featureId) {

		ProductFeature feature = productListService.getByProductFeatureId(featureId);
		if (feature == null) {
			log.info("Could not find Product Feature with ID : " + featureId);
			return false;
		} else  {
			return addProductFeature(feature);
		}
	}

	@Override
	@Transactional
	public boolean addProductFeature(ProductFeature feature) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push Product Feature to MongoDB. MongoDB is not setup");
			return false;
		}
		if (feature == null) {
			log.info("Could not push Product Feature to MongoDB. Test Case Result is null");
			return false; 
		}
		FeatureMasterMongo featureMongo = new FeatureMasterMongo(feature);
		featuresMongoDAO.save(featureMongo);
		ISEFeatureMappingMongo iseFeatureMappingMongo = new ISEFeatureMappingMongo(feature);
		featuresMongoDAO.saveISEFeatureMapping(iseFeatureMappingMongo);
		return true;
	}

	@Override
	@Transactional
	public boolean addProductFeatures(List<ProductFeature> features, Date lastSyncDate) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push TestcaseResult to MongoDB. MongoDB is not setup");
			return false;
		}
		if (features == null || features.isEmpty()) {
			log.info("Could not push Features to MongoDB. Features are null");
			return false;
		}
		
		int count = 0;
		List<FeatureMasterMongo> featuresMongo = new ArrayList<FeatureMasterMongo>();
		for (ProductFeature feature : features) {
			FeatureMasterMongo featureMongo = new FeatureMasterMongo(feature);
			featuresMongo.add(featureMongo);
			count++;
		}
		featuresMongoDAO.save(featuresMongo);
		log.info("Pushed features to MongoDB : " + count);
		return true;
	}
	
	@Override
	@Transactional
	public boolean addAllProductFeatures(Date startDate, Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push Features to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = productListService.countProductFeatures(startDate,endDate);
			if (size < 0) {
				log.info("No Features to be pushed to MongoDB");
				return false;
			}
			
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<ProductFeature> features = productListService.list(i*pageSize, pageSize, startDate,endDate);
				if (features != null) {
					for(ProductFeature feature:features){
						addProductFeature(feature);
					}
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the productfeatures to MongoDB",e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean addBug(Integer bugId) {

		TestExecutionResultBugList bug = null;//workPackageService.get.getByProductFeatureId(featureId);
		bug =testExecutionBugsService.getByBugId(bugId);
		if (bug == null) {
			log.info("Could not find bug with ID : " + bugId);
			return false;
		} else  {
			return addBug(bug);
		}
	}

	@Override
	@Transactional
	public boolean addBug(TestExecutionResultBugList bug) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push bug to MongoDB. MongoDB is not setup");
			return false;
		}
		if (bug == null) {
			log.info("Could not push bug to MongoDB. bug is null");
			return false;
		}
		TestCaseDefectsMasterMongo bugMongo = new TestCaseDefectsMasterMongo(bug);
		
		testCaseDefectsMongoDAO.save(bugMongo);
		return true;
	}

	@Override
	@Transactional
	public boolean addBugs(List<TestExecutionResultBugList> bugs, Date lastSyncDate) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push bugs to MongoDB. MongoDB is not setup");
			return false;
		}
		if (bugs == null || bugs.isEmpty()) {
			log.info("Could not push bugs to MongoDB. Bugs are null");
			return false;
		}
		
		int count = 0;
		List<TestCaseDefectsMasterMongo> bugsMongo = new ArrayList<TestCaseDefectsMasterMongo>();
		for (TestExecutionResultBugList bug : bugs) {
			TestCaseDefectsMasterMongo bugMongo = new TestCaseDefectsMasterMongo(bug);
			bugsMongo.add(bugMongo);
			count++;
		}
		testCaseDefectsMongoDAO.save(bugsMongo);
		log.info("Pushed bugs to MongoDB : " + count);
		return true;
	}
	
	@Override
	@Transactional
	public boolean addAllBugs(Date startDate,Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push Features to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = productListService.countAllBugs(startDate,endDate);
			log.info("bug size "+size);
			if (size < 0) {
				log.info("No Features to be pushed to MongoDB");
				return false;
			}
			
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<TestExecutionResultBugList> bugList = testExecutionResultBugDAO.listAllPaginate(i*pageSize, pageSize, startDate,endDate);
				
				log.info("bugList size  "+bugList.size());
				if (bugList != null) {
					for(TestExecutionResultBugList bug:bugList)
						addBug(bug);
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the bugs to MongoDB ", e);
			return false;
		}
	}


	@Override
	@Transactional
	public boolean addWorkPackage(Integer workPackageId) {

		WorkPackage workPackage = workPackageService.getWorkPackageById(workPackageId);
		if (workPackage == null) {
			log.info("Could not find workpackage with ID : " + workPackageId);
			return false;
		} else  {
			return addWorkPackage(workPackage);
		}
	}

	@Override
	@Transactional
	public boolean addWorkPackage(WorkPackage workPackage) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push WP to MongoDB. MongoDB is not setup");
			return false;
		}
		if (workPackage == null) {
			log.info("Could not push WP to MongoDB. Wp is null");
			return false;
		}
		log.info("WorkpackageId to Mongo "+workPackage.getWorkPackageId());
		WorkPackageMongo workPackageMongo = new WorkPackageMongo(workPackage);
		workPackageMongo.setResult(workPackageExecutionService.getWorkpackageResultStatus(workPackage.getWorkPackageId()));
		workPackageMongoDAO.save(workPackageMongo);
		return true;
	}

	@Override
	@Transactional
	public boolean addWorkPackages(List<WorkPackage> workPackages, Date lastSyncDate) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push WPs to MongoDB. MongoDB is not setup");
			return false;
		}
		if (workPackages == null || workPackages.isEmpty()) {
			log.info("Could not push WPs to MongoDB. WPs are null");
			return false;
		}
		
		int count = 0;
		List<WorkPackageMongo> workPackagesMongo = new ArrayList<WorkPackageMongo>();
		for (WorkPackage workPackage : workPackages) {
			WorkPackageMongo workPackageMongo = new WorkPackageMongo(workPackage);
			workPackageMongo.setResult(workPackageExecutionService.getWorkpackageResultStatus(workPackage.getWorkPackageId()));
			workPackagesMongo.add(workPackageMongo);
			count++;
		}
		workPackageMongoDAO.save(workPackagesMongo);
		log.info("Pushed WPs to MongoDB : " + count);
		return true;
	}
	
	@Override
	@Transactional
	public boolean addAllWorkPackages(Date startDate, Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push WPs to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = workPackageExecutionService.countAllWorkpackages(startDate, endDate);
			if (size < 0) {
				log.info("No WPs to be pushed to MongoDB");
				return false;
			}
			log.info(" workpackage size" +size);
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<WorkPackage> workPackages = workPackageDAO.list(i*pageSize, pageSize, startDate, endDate);
				if (workPackages != null) {
					for(WorkPackage workpackage:workPackages){
						addWorkPackage(workpackage);
					}
					
				}
			}
			return true;
		} catch (Exception e) {
			log.info(e);
			log.error("Unable to push all the WPs to MongoDB",e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean addActivityTaskToMongoDB(Integer activityTaskId) {
		ActivityTask activityTask = activityTaskService.getActivityTaskById(activityTaskId, 1);
		if (activityTask == null) {
			log.info("Could not find activityTask with ID : " + activityTask);
			return false;
		} else  {
			return addActivityTaskToMongoDB(activityTask);
		}
	}
	
	@Override
	@Transactional
	public boolean addActivityTaskToMongoDB(ActivityTask activityTask) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add activityTask to MongoDB. MongoDB is not setup");
			return false;
		}
		if (activityTask == null) {
			log.info("Could not add activityTask to MongoDB. Product Result is null");
			return false;
		}
		ActivityTaskMongo activityTaskMongo=new ActivityTaskMongo(activityTask);
		activityTaskMongoDAO.save(activityTaskMongo);	
		
		return true;
	}

	@Override
	@Transactional
	public void deleteActivityTaskFromMongoDb(Integer activityTaskId) {
		activityTaskMongoDAO.deleteActivityTaskFromMongoDb(activityTaskId);
	}
	
	@Override
	@Transactional
	public boolean addActivityTaskToMongoDB(ActivityTask activityTask,
			Integer productId,String productName,
			Integer productBuildVersionId, String productBuildVersionName,
			Integer productBuildId, String productBuildName,
			Integer activityWorkPackageId, String activityWorkPackageName, Integer testFactoryId, String testFactoryName, Integer customerId, String customerName) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add Product to MongoDB. MongoDB is not setup");
			return false;
		}
		if (activityTask == null) {
			log.info("Could not add activitytask to MongoDB. Product Result is null");
			return false;
		}
		ActivityTaskMongo activityTaskMongo=new ActivityTaskMongo(activityTask,productId,productName,productBuildVersionId,productBuildVersionName, productBuildId,productBuildName, activityWorkPackageId,activityWorkPackageName,testFactoryId, testFactoryName, customerId,customerName);
		activityTaskMongoDAO.save(activityTaskMongo);		
		return true;
	}

	@Override
	@Transactional
	public Float getScheduleVarienceData(List<Integer> productIdList) {
		
		Float schVar = 0.0F;
		List<ActivityTaskMongo> activityTaskMongoList = dashboardSLADAO.getScheduleVarienceData(productIdList);
		try{
			Date plannedStartDate = null;
			Date plannedEndDate = null;
			Date actualEndDate = null;
			int counter = 0;
			for (ActivityTaskMongo activityTaskMongo : activityTaskMongoList) {
				if(counter == 0){
					plannedStartDate = activityTaskMongo.getPlannedStartDate();
				}else if(counter == 1){
					plannedEndDate = activityTaskMongo.getPlannedEndDate();
				}else if(counter == 2){
					actualEndDate = activityTaskMongo.getActualEndDate();
				}
				counter++;
			}

			if(plannedStartDate != null && plannedEndDate != null && actualEndDate != null){
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				plannedStartDate = dateFormat.parse(dateFormat.format(plannedStartDate));
				plannedEndDate = dateFormat.parse(dateFormat.format(plannedEndDate));
				actualEndDate = dateFormat.parse(dateFormat.format(actualEndDate));
				
				long actEndPlannedEnd = actualEndDate.getTime() - plannedEndDate.getTime();
				int actEndPlannedEndDays = (int) TimeUnit.DAYS.convert(actEndPlannedEnd, TimeUnit.MILLISECONDS);

				long plannedEndPlannedStart = plannedEndDate.getTime() - plannedStartDate.getTime();
				int plannedEndPlannedStartDays = (int) TimeUnit.DAYS.convert(plannedEndPlannedStart, TimeUnit.MILLISECONDS);

				schVar = ((float)actEndPlannedEndDays / (float)plannedEndPlannedStartDays);

			}
		}catch(Exception ex){
			log.error("ERROR getting ScheduleVarienceData : ",ex);
		}
		return schVar;
	}

	@Override
	@Transactional
	public Float getProductQualityIndexData(List<Integer> productIdList, List<String> statusList) {
		Float productQualityIndex = dashboardSLADAO.getProductQualityIndexData(productIdList, statusList);
		return productQualityIndex;
	}

	@Override
	@Transactional
	public boolean addProductTestCaseToMongoDB(List<TestCaseList> testCaseList,Date lastSyncDate) {
		log.info("SQL DATA SIZE  "+testCaseList.size());
		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push ProductTestCases to MongoDB. MongoDB is not setup");
			return false;
		}
		if (testCaseList == null || testCaseList.isEmpty()) {
			log.info("Could not push ProductTestCases to MongoDB. testCaseList is null");
			return false;
		}
		
		int count = 0;
		List<TestCaseMasterMongo> testCasesMasterMongo = new ArrayList<TestCaseMasterMongo>();
		List<ISETestCaseCollectionMongo> iseTestCasesMasterMongoList = new ArrayList<ISETestCaseCollectionMongo>();
		for (TestCaseList testcase : testCaseList) {
			TestCaseMasterMongo testCaseMasterMongo = new TestCaseMasterMongo(testcase);
			
			ISETestCaseCollectionMongo iseTestCaseMasterMongo = new ISETestCaseCollectionMongo(testcase);
			
			/*Because of session error we are setting customer Name here */
			Customer custom=customerService.getCustomerId(testcase.getProductMaster().getCustomer().getCustomerId());
			log.info("Name***********"+custom.getCustomerName());
			testCaseMasterMongo.setCustomerName(custom.getCustomerName());
			
		//	testCasesMasterMongo.add(testCaseMasterMongo);
//			iseTestCasesMasterMongoList.add(iseTestCaseMasterMongo);
			testCasesMongoDAO.save(testCaseMasterMongo);
			count++;
		}
		log.info("Pushed testCaseList to MongoDB : " + count);
		return true;
	}

	@Override
	@Transactional
	public boolean addAllProductTestCaseToMongoDB(Date startDate,Date endDate) {
		
		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push ProductTestCase to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = productListService.countAllProductTestCases(startDate,endDate);
			if (size < 0) {
				log.info("No ProductTestCase to be pushed to MongoDB");
				return false;
			}
			
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<TestCaseList> testCaseList = productListService.listAllProductTestCasesByLastSyncDate(i*pageSize, pageSize,startDate,endDate);
				if (testCaseList != null) {
					/*for(TestCaseList testCase:testCaseList){
						addProductTestCaseToMongoDB(testCase);
					}*/
					addProductTestCaseToMongoDB(testCaseList, null);
					
					
					
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the ProductBuilds to MongoDB ", e);
			return false;
		}
	}

	@Override
	@Transactional
	public List<ReviewRecordCollectionMongo> getReivewRecordMongoCollectionList(Date endDate, String testFactoryName, String productName,Set<ProductUserRole> userProducts) {
		return dashboardSLADAO.getReivewRecordMongoCollectionList(endDate,testFactoryName, productName, userProducts);
	}

	@Override
	@Transactional
	public List<DefectCollectionMongo> getDefectMongoCollectionList(Date endDate,String collectionName,String testFactoryName, String productName,Set<ProductUserRole> userProducts) {
		return dashboardSLADAO.getDefectMongoCollectionList(endDate,collectionName,testFactoryName,productName, userProducts);
	}

	@Override
	@Transactional
	public List<ActivityCollectionMongo> getActivityMongoCollectionCountByDateFilter(Date startDate, Date currentDate,String collectionName,String testFactoryName, String productName,Set<ProductUserRole> userProducts) {
		return dashboardSLADAO.getActivityMongoCollectionCountByDateFilter(startDate, currentDate,collectionName,testFactoryName,productName, userProducts);
	}

	@Override
	@Transactional
	public List<ActivityMongo> getActivityMongoByDateFilter(Date startDate,Date currentDate, String collectionName, String testFactoryName, String productName,
			Set<ProductUserRole> userProducts) {
		return dashboardSLADAO.getActivityMongoByDateFilter(startDate, currentDate,collectionName,testFactoryName,productName, userProducts);
	}

	
	@Override
	@Transactional
	public float getActivityMongoCollectionListForScheduleVariance(Date startDate, Date currentDate,String productName,Set<ProductUserRole>userProducts) {
		
	Float schVar = 0.0F;
	List<ActivityCollectionMongo> activityMongoCollections = dashboardSLADAO.getActivityMongoCollectionListForScheduleVariance(startDate,currentDate,productName,userProducts);
	log.info("Schedule Variance Mongo list size "+activityMongoCollections.size());
	try{
		Date plannedStartDate = null;
		Date plannedEndDate = null;
		Date actualEndDate = null;
		int counter = 0;
		for (ActivityCollectionMongo activityCollection : activityMongoCollections) {
			if(counter == 0){
				plannedStartDate = (Date) activityCollection.getPlannedActivityStartDate();
			}else if(counter == 1){
				plannedEndDate = (Date) activityCollection.getPlannedActivityEndDate();
			}else if(counter == 2){
				actualEndDate = (Date) activityCollection.getActualActivityEndDate();
			}
			counter++;
		}
		if(plannedStartDate != null && plannedEndDate != null && actualEndDate != null){
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			plannedStartDate = dateFormat.parse(dateFormat.format(plannedStartDate));
			plannedEndDate = dateFormat.parse(dateFormat.format(plannedEndDate));
			actualEndDate = dateFormat.parse(dateFormat.format(actualEndDate));
			
			long actEndPlannedEnd = actualEndDate.getTime() - plannedEndDate.getTime();
			int actEndPlannedEndDays = (int) TimeUnit.DAYS.convert(actEndPlannedEnd, TimeUnit.MILLISECONDS);

			long plannedEndPlannedStart = plannedEndDate.getTime() - plannedStartDate.getTime();
			int plannedEndPlannedStartDays = (int) TimeUnit.DAYS.convert(plannedEndPlannedStart, TimeUnit.MILLISECONDS);

			schVar = (((float)actEndPlannedEndDays / (float)plannedEndPlannedStartDays));

		}
	}catch(Exception ex){
		log.error("ERROR getting ActivityMongoCollectionList For ScheduleVariance ",ex);
	}
	if(schVar == Float.NEGATIVE_INFINITY || schVar == Float.POSITIVE_INFINITY || schVar == Float.NaN || Float.isNaN(schVar) || Float.isInfinite(schVar)){
		schVar = 0.0F;
	}
	return schVar;
}

	
	@Override
	@Transactional
	public float getActivityMongoCollectionListForScheduleVarianceAvg(Date startDate, Date currentDate,String testFactoryName,String productName,Set<ProductUserRole>userProducts) {
		
	Float schVar = 0.0F;
	float  schVarTotal=0.0f;
	List<ActivityCollectionMongo> activityMongoCollections = dashboardSLADAO.getActivityMongoCollectionListForScheduleVarianceAvg(startDate,currentDate,testFactoryName,productName,userProducts);
	try{
		
		for(ActivityCollectionMongo activity:activityMongoCollections){
			schVarTotal=schVarTotal+activity.getsVa();
		}
		if(schVarTotal!=0){
			schVar=schVarTotal/activityMongoCollections.size();
		}
		
	}catch(Exception ex){
		log.error("ERROR getting ActivityMongoCollectionList For ScheduleVarianceAvg ",ex);
	}
	if(schVar == Float.NEGATIVE_INFINITY || schVar == Float.POSITIVE_INFINITY || schVar == Float.NaN || Float.isNaN(schVar) || Float.isInfinite(schVar)){
		schVar = 0.0F;
	}
	return schVar;
}

	@Override
	@Transactional
	public boolean addTestStepsToMongoDB(Integer testStepId) {

		TestCaseStepsList testCaseStep = testSuiteConfigurationService.getByTestStepId(testStepId);
		if (testCaseStep == null) {
			log.info("Could not find TestCaseStep with ID : " + testStepId);
			return false;
		} else  {
			return addTestStepsToMongoDB(testCaseStep);
		}
	}

	@Override
	@Transactional
	public boolean addTestStepsToMongoDB(TestCaseStepsList testCaseStep) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push TestCaseSteps to MongoDB. MongoDB is not setup");
			return false;
		}
		if (testCaseStep == null) {
			log.info("Could not push TestCaseSteps to MongoDB. Test Case Result is null");
			return false;
		}
		TestCaseStepsListMongo testCaseStepsListMongo = new TestCaseStepsListMongo(testCaseStep);
		testCaseStepsListMongoDAO.save(testCaseStepsListMongo);
		return true;
	}

	@Override
	@Transactional
	public boolean addTestStepsToMongoDB(List<TestCaseStepsList> testStepList,Date lastSyncDate) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push TestCaseStepsList to MongoDB. MongoDB is not setup");
			return false;
		}
		if (testStepList == null || testStepList.isEmpty()) {
			log.info("Could not push TestCaseStepsList to MongoDB. testCaseList is null");
			return false;
		}
		
		int count = 0;
		List<TestCaseStepsListMongo> testCaseStepsListMongo = new ArrayList<TestCaseStepsListMongo>();
		for (TestCaseStepsList testStep : testStepList) {
			TestCaseStepsListMongo testCaseStepListMongo = new TestCaseStepsListMongo(testStep);
			testCaseStepsListMongo.add(testCaseStepListMongo);
			count++;
		}
		testCaseStepsListMongoDAO.save(testCaseStepsListMongo);
		log.info("Pushed TestCaseStepsListMongo to MongoDB : " + count);
		return true;
	}

	@Override
	@Transactional
	public boolean addAllTestStepsToMongoDB(Date startDate,Date endDate) {
		
		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push TestCaseStepsListMongo to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = productListService.countAllProductTestCaseSteps(startDate,endDate);
			if (size < 0) {
				log.info("No TestCaseStepsListMongo to be pushed to MongoDB");
				return false;
			}
			
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<TestCaseStepsList> testCaseStepsList = productListService.listAllProductTestCaseStepsByLastSyncDate(i*pageSize, pageSize, startDate,endDate);
				
				log.info("producttestCaseStepsList size  "+testCaseStepsList.size());
				if (testCaseStepsList != null) {
					for(TestCaseStepsList testStep:testCaseStepsList)
						addTestStepsToMongoDB(testStep);
				}
				
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the TestCaseStepsListMongo to MongoDB ", e);
			return false;
		}
	}

	@Override
	@Transactional
	public List<TestCaseExecutionResultMongo> getTestCaseExecutionMongoCountByDateFilter(Date startDate, Date currentDate,String testFactoryName, String productName, Set<ProductUserRole> productSet) {
		return dashboardSLADAO.getTestCaseExecutionMongoCountByDateFilter(startDate, currentDate,testFactoryName, productName, productSet);
	}

	@Override
	@Transactional
	public List<TestCaseDefectsMasterMongo> getTestCaseDefectsMasterMongoList(String testFactoryName, String productName,Set<ProductUserRole> userProducts) {
		return dashboardSLADAO.getTestCaseDefectsMasterMongoList(testFactoryName, productName,userProducts);
	}

	
	
	
	@Override
	@Transactional
	public boolean addProductUserRole(Integer productUserRoleId) {

		ProductUserRole productUserRole = productListService.getProductUserRoleByUserId(productUserRoleId);
		if (productUserRole == null) {
			log.info("Could not find productUserRole with ID : " + productUserRoleId);
			return false;
		} else  {
			return addProductUserRole(productUserRole);
		}
	}

	@Override
	@Transactional
	public boolean addProductUserRole(ProductUserRole productUserRole) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add ProductUserRole to MongoDB. MongoDB is not setup");
			return false;
		}
		if (productUserRole == null) {
			log.info("Could not add ProductUserRole to MongoDB. productUserRole Result is null");
			return false;
		}
		ProductUserRoleMongo productUserRoleMongo=new ProductUserRoleMongo(productUserRole);
		productUserRoleMongoDAO.save(productUserRoleMongo);
		
		return true;
	}

	@Override
	@Transactional
	public boolean addProductUserRole(List<ProductUserRole> productUserRoleList, Date lastSyncDate) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push ProductUserRole to MongoDB. MongoDB is not setup");
			return false;
		}
		if (productUserRoleList == null || productUserRoleList.isEmpty()) {
			log.info("Could not push productUserRoleList to MongoDB. ProductUserRole Result is null");
			return false;
		}
		
		int count = 0;
		List<ProductUserRoleMongo> productUserRoleMongoList = new ArrayList<ProductUserRoleMongo>();
		for (ProductUserRole prodUserRole : productUserRoleList) {
			ProductUserRoleMongo prodUserRoleMongo = new ProductUserRoleMongo(prodUserRole);
			productUserRoleMongoList.add(prodUserRoleMongo);
			count++;
		}
		productUserRoleMongoDAO.save(productUserRoleMongoList);
		log.info("Pushed productUserRoleMongoList to MongoDB : " + count);
		return true;
	}

	@Override
	@Transactional
	public boolean addAllProductUserRole(Date startDate, Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push ProductUserRole to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = productListService.countAllProductUserRole(startDate,endDate);
			log.info("bug size "+size);
			if (size < 0) {
				log.info("No ProductUserRole to be pushed to MongoDB");
				return false;
			}
			
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<ProductUserRole> productUserRoleList = productListService.listAllPaginate(i*pageSize, pageSize, startDate,endDate);
				
				log.info("productUserRoleList size  "+productUserRoleList.size());
				if (productUserRoleList != null) {
					for(ProductUserRole produser:productUserRoleList)
						addProductUserRole(produser);
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the ProductUserRole to MongoDB ", e);
			return false;
		}
	}

	@Override
	@Transactional
	public List<UtilizationCollectionMongo> getUtilizationMongoCollectionList(Date startDate, Date currentDate, String testFactoryName) {
		return dashboardSLADAO.getUtilizationMongoCollectionList(startDate,currentDate, testFactoryName);
		}

	@Override
	@Transactional
	public boolean pushToMongoDB(Integer collectionType, Date startDate,Date endDate) {
	
		try{
			
			if(collectionType==0){
				pushAlliLCMDataToMongoDB(startDate,endDate);
			}else if(collectionType==1){
				addAllProduct(startDate,endDate);
			}else if(collectionType==2){
				addAllProductVersionMongoDB(startDate,endDate);
			}else if(collectionType==3){
				addAllProductTestCaseToMongoDB(startDate,endDate);
			}else if(collectionType==4){
				addAllTestCaseExecutionResult(startDate,endDate);
			}else if(collectionType==5){
				addAllWorkPackages(startDate,endDate);
			}else if(collectionType==6){
				addAllBugs(startDate,endDate);
			}else if(collectionType==7){
				addAllProductUserRole(startDate,endDate);
			}else if(collectionType==8){
				addAllTestRunJobToMongoDB(startDate,endDate);
			}else if(collectionType==9){
				addAllProductBuildToMongoDB(startDate, endDate);
			}else if(collectionType==10){
				addAllTestFactoryToMongo(startDate, endDate);
			}else if(collectionType==11){
				addAllTestStepsToMongoDB(startDate, endDate);
			}else if(collectionType==12){
				addAllActivityTaskToMongoDB(startDate, endDate);
			}else if(collectionType==13){
				addAllActivitytoMongoD(startDate, endDate);
			}else if(collectionType==14){
				addAllActivityWorkPackagetoMongoDB(startDate, endDate);
			}else if(collectionType==15){
				addAllProductTeamResources(startDate, endDate);
			}else if(collectionType==16){
				addAllWorkflowEventsToMongoDB(startDate, endDate);
			}else if(collectionType==17){
				addAllUserList(startDate, endDate);
			}else if(collectionType==18){
				addAllClarificationToMongoDB(startDate, endDate);
			}else if(collectionType==19){
				addAllProductFeatures(startDate,endDate);
			}else if(collectionType==20){
				addAllTestSuiteToMongoDB(startDate, endDate);
			}else if(collectionType==21){
				addAllChangeRequestsToMongoDB(startDate, endDate);
			}else if(collectionType==22){
				addAllResourceDemandToMongoDB(startDate, endDate);
			}else if(collectionType==23){
				addAllReseveredResourceToMongoDB(startDate, endDate);
			}else if(collectionType==24){
				addAllChangeRequestMappingToMongoDB(startDate, endDate);
			}else if(collectionType==25){
				addAllDefectCollectionsToMongoDB(startDate, endDate);
			}else if(collectionType==26){
				addAllFeatureTestCaseMappingsToMongoDB(startDate, endDate);
			}
			return true;
		}catch (Exception e){
			log.error("Unable to push data to MongoDB ", e);
			return false;
		}
		
	}

	@Override
	@Transactional
	public Date getStartDate(Integer summaryValue) {
		Date startDate=null;
		try{
			Calendar call = Calendar.getInstance();
			call.set(Calendar.HOUR_OF_DAY, 0);
			call.set(Calendar.MINUTE, 0);
			call.set(Calendar.SECOND, 0);
			call.set(Calendar.MILLISECOND, 0);
			if(summaryValue==0){
				log.info("daily calculation ");
				call.add(Calendar.DAY_OF_MONTH,-1);
			}else if(summaryValue==1){
				log.info("weekly calculation ");
				call.add(Calendar.DAY_OF_MONTH,-7);
			}else if(summaryValue==2){
				log.info("monthly calculation ");
				call.add(Calendar.DAY_OF_MONTH,-30);
			}else if(summaryValue==3){
				log.info("Quaterly calculation ");
				call.add(Calendar.DAY_OF_MONTH,-90);
			}else if(summaryValue==4){
				log.info("Year calculation ");
				call.add(Calendar.YEAR, -1);
			}
			startDate = call.getTime();
			log.info("startDate "+startDate);
			
		}catch(Exception e){
			log.error("Unable to get StartDate ", e);
		}
		return startDate;
	}

	@Override
	@Transactional
	public List<JSONObject> getSingleValueMetricsList( Integer summaryValue, String testFactoryName) {
		Date startDate=null;
		Date currentDate=new Date();
				if((summaryValue==0)||(summaryValue==1)||(summaryValue==2)){
					log.info("monthly calculation ");
					/*Calendar call = Calendar.getInstance();
					call.add(Calendar.DAY_OF_MONTH,-30);
					startDate = call.getTime();*/
					startDate=getStartDate(2);
					log.info(" pci startDate "+startDate);
				}else{
					startDate=getStartDate(summaryValue);
				}
	return dashboardSLADAO.getSingleValueMetricsList(startDate,currentDate, testFactoryName);
	}

	@Override
	@Transactional
	public List<JSONObject> getSingleValueMetricsList(Date endDate, String testFactoryName, String metricsName) {
		return dashboardSLADAO.getSingleValueMetricsList(endDate, testFactoryName, metricsName);
	}
	
	@Override
	@Transactional
	public Boolean checkAvailabiltyOfMongoDB() {
		return dashboardSLADAO.checkAvailabiltyOfMongoDB();
	}

	@Override
	@Transactional
	public boolean addAllActivityTaskToMongoDB(Date startDate, Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push ActivityTask to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = activityTaskService.countAllactivityTaskService(startDate,endDate);
			log.info("ActivityTask "+size);
			if (size < 0) {
				log.info("No ActivityTask to be pushed to MongoDB");
				return false;
			}
			
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<ActivityTask> activityTaskList = activityTaskService.listAllactivityTask(i*pageSize, pageSize, startDate,endDate);
				
				log.info("ActivityTaskList size  "+activityTaskList.size());
				if (activityTaskList != null) {
					for(ActivityTask activityTask:activityTaskList)
						addActivityTaskToMongoDB(activityTask.getActivityTaskId());
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the ActivityTask to MongoDB ",e);
			return false;
		}
	}

	@Override
	public boolean addActivitytoMongoDB(Integer activityId) {
	try{
			
		Activity activity = activityService.getActivityById(activityId,1);
		if (activity == null) {
			log.info("Could not find activity with ID : " + activityId);
			return false;
		} else  {
			return addActivitytoMongoDB(activity);
		}
	
		}
		 catch (Exception e) {
			log.error("Unable to push all the ActivityTask to MongoDB ",e);
			return false;
		}
	}
	
	
	public boolean addActivitytoMongoDB(Activity activity) {
		
		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add activity to MongoDB. MongoDB is not setup");
			return false;
		}
		if (activity == null) {
			log.info("Could not add activity to MongoDB. Product Result is null");
			return false;
		}
		ActivityMongo activityMongo=new ActivityMongo(activity);
		activityMongoDAO.save(activityMongo);
		List<Integer> activityIds = new ArrayList<Integer>();
		activityIds.add(activity.getActivityId());
		addCustomFieldsOnBulkUpdate(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID, activityIds, MongodbConstants.ACTIVITY);
		
		return true;
	}

	@Override
	public boolean addAllActivitytoMongoD(Date startDate, Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push Activity to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = activityService.countAllActivity(startDate, endDate);
			if (size < 0) {
				log.info("No BotMaster to be pushed to MongoDB");
				return false;
			}
			log.info(" BotMaster" +size);
			List<Integer> activityIds = new ArrayList<Integer>();
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				List<Activity> activityList = activityDAO.list(i*pageSize, pageSize, startDate, endDate);
				if (activityList != null) {
					for(Activity activity:activityList){
						addActivitytoMongoDB(activity);
						activityIds.add(activity.getActivityId());
					}
					
				}
			}
			if(activityIds != null && activityIds.size() > 0){
				addCustomFieldsOnBulkUpdate(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID, activityIds, MongodbConstants.ACTIVITY);
			}
			return true;
		} catch (Exception e) {
			log.info(e);
			log.error("Unable to push all the Activity to MongoDB",e);
			return false;
		}
	}
	
	
	
	@Override
	@Transactional
	public void deleteActivityFromMongodb(Integer activityId) {
		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not push Activity to MongoDB. MongoDB is not setup");
			return;
		} else {
		 activityMongoDAO.deleteActivityFromMongodb(activityId);
		}
	}
	

	@Override
	public boolean addActivityWorkPackagetoMongoDB(Integer activityWorkPackageId) {
		try{
			
			ActivityWorkPackage activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(activityWorkPackageId,1);
			if (activityWorkPackage == null) {
				log.info("Could not find activity with ID : " + activityWorkPackageId);
				return false;
			} else  {
				return addActivityWorkPackagetoMongoDB(activityWorkPackage);
			}
		
			}
			 catch (Exception e) {
				log.error("Unable to push all the activityWorkPackage to MongoDB ",e);
				return false;
			}
		
	}

	@Override
	@Transactional
	public boolean addActivityWorkPackagetoMongoDB(ActivityWorkPackage activityWorkPackage) {
		
		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add activity to MongoDB. MongoDB is not setup");
			return false;
		}
		if (activityWorkPackage == null) {
			log.info("Could not add activity to MongoDB. Product Result is null");
			return false;
		}
		AcitivityWorkPackageMongo activityWorkPackageMongo=new AcitivityWorkPackageMongo(activityWorkPackage);
		activityWorkPackageMongoDAO.save(activityWorkPackageMongo);
		
		return true;
	}

	@Override
	@Transactional
	public boolean addAllActivityWorkPackagetoMongoDB(Date startDate,Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push Activity WorkPackage to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = activityService.countAllActivityWorkpackage(startDate, endDate);
			if (size < 0) {
				log.info("No Activity WorkPackage to be pushed to MongoDB");
				return false;
			}
			log.info(" activityWorkPackage" +size);
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				List<ActivityWorkPackage> activityWorkpackageList = activityDAO.listActivityWorkpackage(i*pageSize, pageSize, startDate, endDate);
				if (activityWorkpackageList != null) {
					for(ActivityWorkPackage activitywp:activityWorkpackageList){
						addActivityWorkPackagetoMongoDB(activitywp);
					}
					
				}
			}
			return true;
		} catch (Exception e) {
			log.info(e);
			log.error("Unable to push all the Activity wpk to MongoDB",e);
			return false;
		}
	}
	
	@Override
	@Transactional
	public void deleteActivityWorkPackageFromMongoDb(Integer activityWorkPackageId) {
		activityWorkPackageMongoDAO.deleteActivityWorkPackageFromMongoDb(activityWorkPackageId);
	}
	

	@Override
	@Transactional
	public Integer getProjectTeamMembersCount(Date startDate,Date currentDate,String testFactoryName, String productName, Set<ProductUserRole> userProducts) {
		return dashboardSLADAO.getProjectTeamMembersCount(startDate,currentDate,testFactoryName,productName,userProducts);
	}

	

	@Override
	@Transactional
	public boolean addProductTeamResources(Integer productTeamResourcesId) {

		ProductTeamResources productTeamResources = productListService.getProductTeamResourceById(productTeamResourcesId);
		if (productTeamResources == null) {
			log.info("Could not find ProductTeamResources with ID : " + productTeamResourcesId);
			return false;
		} else  {
			return addProductTeamResources(productTeamResources);
		}
	}
	

	@Override
	@Transactional
	public boolean addProductTeamResources(ProductTeamResources productTeamResource) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add productTeamResources to MongoDB. MongoDB is not setup");
			return false;
		}
		if (productTeamResource == null) {
			log.info("Could not add productTeamResources to MongoDB. productTeamResources Result is null");
			return false;
		}
		ProductTeamResourcesMongo productTeamResourcesMongo=new ProductTeamResourcesMongo(productTeamResource);
		productTeamResourceMongoDAO.save(productTeamResourcesMongo);
		
		return true;
	}

	@Override
	public boolean addAllProductTeamResources(Date startDate, Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push addAllProductTeamResources to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = productListService.countAlladdAllProductTeamResources(startDate,endDate);
			log.info("bug size "+size);
			if (size < 0) {
				log.info("No addAllProductTeamResources to be pushed to MongoDB");
				return false;
			}
			
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<ProductTeamResources> productTeamResources = productListService.listAllProductTeam(i*pageSize, pageSize, startDate,endDate);
				
				log.info("addAllProductTeamResources size  "+productTeamResources.size());
				if (productTeamResources != null) {
					for(ProductTeamResources produser:productTeamResources)
						addProductTeamResources(produser);
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the addAllProductTeamResources to MongoDB ", e);
			return false;
		}
	}
	
	
	@Override
	@Transactional
	public boolean addWorkflowEventToMongoDB(Integer workflowEventId) {

		WorkflowEvent workflowEvent =  workflowEventService.getWorkflowEventById(workflowEventId);
		if (workflowEvent == null) {
			log.info("Could not find workflowEventId with ID : " + workflowEventId);
			return false;
		} else  {
			return addWorkflowEventToMongoDB(workflowEvent);
		}
	}
	
	@Override
	@Transactional
	public boolean addWorkflowEventToMongoDB(WorkflowEvent workflowEvent) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add WorkflowEvent to MongoDB. MongoDB is not setup");
			return false;
		}
		if (workflowEvent == null) {
			log.info("Could not add WorkflowEvent to MongoDB. WorkflowEvent Result is null");
			return false;
		}
		WorkflowEventMongo workflowEventMongo = new WorkflowEventMongo(workflowEvent);
		workflowEventMongoDAO.save(workflowEventMongo);
		
		return true;
	}

	@Override
	@Transactional
	public boolean addAllWorkflowEventsToMongoDB(Date startDate, Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push addAllWorkflowEventsToMongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = workflowEventService.countAllWorkflowEvents(startDate,endDate);
			log.info("EffortTracker "+size);
			if (size < 0) {
				log.info("No workflow event to be pushed to MongoDB");
				return false;
			}
			
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<WorkflowEvent> workflowEventsList = workflowEventService.listAllWorkflowEvents(i*pageSize, pageSize, startDate,endDate);
				
				log.info("workflowEventsList size  "+workflowEventsList.size());
				if (workflowEventsList != null) {
					for(WorkflowEvent workflowEvent : workflowEventsList)
						addWorkflowEventToMongoDB(workflowEvent);
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the workflow events to MongoDB ", e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean addTrendCollectionMongo(TrendCollectionMongo trendValue) {
		
		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add Trend Values to MongoDB. MongoDB is not setup");
			return false;
		}
		if (trendValue == null) {
			log.info("Could not add Trend Values to MongoDB.  Result is null");
			return false;
		}
		trendCollectionMongoDAO.save(trendValue);
		
		return true;
	}

	
	/*  Add UserList to Mongo  */
	@Override
	@Transactional
	public boolean addUserListToMongoDB(Integer userId) {

		UserList  user = userListService.getUserListById(userId);
		if (user == null) {
			log.info("Could not find user with ID : " + userId);
			return false;
		} else  {
			return addUserListToMongoDB(user);
		}
	}

	@Override
	@Transactional
	public boolean addUserListToMongoDB(UserList user) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add user to MongoDB. MongoDB is not setup");
			return false;
		}
		if (user == null) {
			log.info("Could not add user to MongoDB. user Result is null");
			return false;
		}
		UserListMongo userMongo=new UserListMongo(user);
		userListMongoDAO.save(userMongo);
		
		return true;
	}

	@Override
	@Transactional
	public boolean addAllUserList(Date startDate, Date endDate) {
	
		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push All UserList to MongoDB. MongoDB is not setup");
				return false;
			}
	
			
			int size = userListService.countAllUserList(startDate,endDate);
			
			if (size < 0) {
				log.info("No UserList to be pushed to MongoDB");
				return false;
			}
			log.info("UserList size "+size);

			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				
				List<UserList> users = userListService.listAllUsers(i*pageSize, pageSize, startDate,endDate);
				
				if (users != null) {
					for(UserList user:users){
						addUserListToMongoDB(user);
					}
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the Users to MongoDB" , e);
			return false;
		}
	}

	@Override
	@Transactional
	public List<JSONObject> getSingleValueListPCIScroreByDate(Date endDate, String testFactoryName) {
		return dashboardSLADAO.getSingleValueListPCIScroreByDate(endDate, testFactoryName);
	}

	@Override
	@Transactional
	public boolean addClarificationToMongoDB(Integer clarificationId) {

		ClarificationTracker clarificationTracker = clarificationTrackerService.getClarificationTrackersById(clarificationId, 1);
		if (clarificationTracker == null) {
			log.info("Could not find clarificationTracker Result with ID : " + clarificationId);
			return false;
		} else  {
			return addClarificationToMongoDB(clarificationTracker);
		}
	}

	
	
	@Override
	@Transactional
	public boolean addClarificationToMongoDB(ClarificationTracker clarificationTracker) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add ClarificationTracker to MongoDB. MongoDB is not setup");
			return false;
		}
		if (clarificationTracker == null) {
			log.info("Could not add ClarificationTracker to MongoDB. Product Result is null");
			return false;
		}
		ClarificationTrackerMongo clarificationTrackerMongo=new ClarificationTrackerMongo(clarificationTracker);
		
		Integer entityId=clarificationTrackerMongo.getEntityTypeId();
		setValue(clarificationTrackerMongo,entityId);
		
		clarificationTrackerMongoDAO.save(clarificationTrackerMongo);

		return true;
	}

	
	
	public void setValue(Object instanceObject, Integer entityId ){
	String entityInstanceName = null;
	ActivityTask activityTask = null;
	Integer activityTaskId = null;
	String activityTaskName = null;
	Activity activity = null;
	Integer activityId = null;
	String activityName = null;
	ActivityWorkPackage activityWorkPackage = null;
	Integer activityWorkpackageId = null;
	String activityWorkpackageName = null;
	ProductBuild productBuild = null;
	Integer productBuildId = null;
	String productBuildName = null;
	ProductVersionListMaster productVersionListMaster = null;
	Integer versionId = null;
	String versionName = null;
	ProductMaster productMaster = null;
	Integer productId = null;
	String productName = null;
	TestFactory testFactory = null;
	Integer testFactoryId = null;
	String testFactoryName = null;
	TestFactoryLab testFactoryLab = null;
	Integer labId = null;
	String labName = null;
	ClarificationTracker clarificationTracker = null;
	Integer clarificationId = null;
	String clarificationName = null;
	
	ClarificationTrackerMongo clarificationTrackerMongo = null;
	ChangeRequestMongo changeRequestMongo = null;
	
	Integer entityInstanceId = 0;
	if(instanceObject instanceof ClarificationTrackerMongo){
		clarificationTrackerMongo = (ClarificationTrackerMongo) instanceObject;
		entityInstanceId = clarificationTrackerMongo.getEntityInstanceId();
	}else if(instanceObject instanceof ChangeRequestMongo){
		changeRequestMongo = (ChangeRequestMongo) instanceObject;
		entityInstanceId = changeRequestMongo.getEntityInstanceId();
	}
	
	if(entityInstanceId == null || entityInstanceId == 0){
		return;
	}
	
	if(entityId != null && entityId == IDPAConstants.ENTITY_TASK){
		activityTask = activityTaskService.getActivityTaskById(entityInstanceId, 0);
		entityInstanceName = activityTask.getActivityTaskName();
	}else if(entityId != null && entityId == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID){
		activity = activityDAO.getActivityById(entityInstanceId, 1);
		entityInstanceName = activity.getActivityName();
	}else if(entityId != null && entityId == IDPAConstants.PRODUCT_ENTITY_MASTER_ID){
		productMaster = productListService.getProductById(entityInstanceId);
		entityInstanceName = productMaster.getProductName();
	}else if(entityId != null && entityId == IDPAConstants.TESTFACTORY_ENTITY_MASTER_ID){
		testFactory = testFactoryManagementService.getTestFactoryById(entityInstanceId);
		entityInstanceName = testFactory.getTestFactoryName();
	}else if(entityId != null && entityId == IDPAConstants.CLARIFICATION_ENTITY_MASTER_ID){
		clarificationTracker = clarificationTrackerService.getClarificationTrackersById(entityInstanceId, 1);
		entityInstanceName = clarificationTracker.getClarificationTitle();
	}
	
	if(activityTask != null){
		activityTaskId = activityTask.getActivityTaskId();
		activityTaskName = activityTask.getActivityTaskName();
		activity = activityTask.getActivity();
	}
	if(activity != null){
		activityId = activity.getActivityId();
		activityName = activity.getActivityName();
		activityWorkPackage = activity.getActivityWorkPackage();
	}
	if(activityWorkPackage != null){
		activityWorkpackageId = activityWorkPackage.getActivityWorkPackageId();
		activityWorkpackageName = activityWorkPackage.getActivityWorkPackageName();
		productBuild = activityWorkPackage.getProductBuild();
	}
	if(clarificationTracker != null){
		clarificationId = clarificationTracker.getClarificationTrackerId();
		clarificationName = clarificationTracker.getClarificationTitle();
		productMaster = clarificationTracker.getProduct();
		testFactory = clarificationTracker.getTestFactory();
	}
	if(productBuild != null){
		productBuildId = productBuild.getProductBuildId();
		productBuildName = productBuild.getBuildname();
		productVersionListMaster = productBuild.getProductVersion();
	}
	if(productVersionListMaster != null){
		versionId = productVersionListMaster.getProductVersionListId();
		versionName = productVersionListMaster.getProductVersionName();
		productMaster = productVersionListMaster.getProductMaster();
	}
	if(productMaster != null){
		productId = productMaster.getProductId();
		productName = productMaster.getProductName();
		testFactory = productMaster.getTestFactory();
	}
	if(testFactory != null){
		testFactoryId = testFactory.getTestFactoryId();
		testFactoryName = testFactory.getTestFactoryName();
		testFactoryLab = testFactory.getTestFactoryLab();
	}
	if(testFactoryLab != null){
		labId = testFactoryLab.getTestFactoryLabId();
		labName = testFactoryLab.getTestFactoryLabName();
		testFactoryLab = testFactory.getTestFactoryLab();
	}
	
	
	if(clarificationTrackerMongo != null){
		clarificationTrackerMongo.setEntityInstanceName(entityInstanceName);
		clarificationTrackerMongo.setActivityId(activityId);
		clarificationTrackerMongo.setActivityName(activityName);		
		clarificationTrackerMongo.setWorkPackageId(activityWorkpackageId);
		clarificationTrackerMongo.setWorkPackageName(activityWorkpackageName);
		clarificationTrackerMongo.setBuildId(productBuildId);
		clarificationTrackerMongo.setBuildName(productBuildName);
		clarificationTrackerMongo.setVersionId(versionId);
		clarificationTrackerMongo.setVersionName(versionName);
		clarificationTrackerMongo.setProductId(productId);
		clarificationTrackerMongo.setProductName(productName);
		clarificationTrackerMongo.setTestFactoryId(testFactoryId);
		clarificationTrackerMongo.setTestFactoryName(testFactoryName);
		clarificationTrackerMongo.setTestCenterId(labId);
		clarificationTrackerMongo.setTestCenterName(labName);
	}else if(changeRequestMongo != null){
		changeRequestMongo.setEntityInstanceName(entityInstanceName);
		changeRequestMongo.setActivityId(activityId);
		changeRequestMongo.setActivityName(activityName);
		changeRequestMongo.setWorkPackageId(activityWorkpackageId);
		changeRequestMongo.setWorkPackageName(activityWorkpackageName);
		changeRequestMongo.setBuildId(productBuildId);
		changeRequestMongo.setBuildName(productBuildName);
		changeRequestMongo.setVersionId(versionId);
		changeRequestMongo.setVersionName(versionName);
		changeRequestMongo.setProductId(productId);
		changeRequestMongo.setProductName(productName);
		changeRequestMongo.setTestFactoryId(testFactoryId);
		changeRequestMongo.setTestFactoryName(testFactoryName);
		changeRequestMongo.setTestCenterId(labId);
		changeRequestMongo.setTestCenterName(labName);	
		}
    }
	
	
	@Override
	@Transactional
	public boolean addAllClarificationToMongoDB(Date startDate, Date endDate) {
	
		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push Clarification to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = clarificationTrackerService.countAllClarification(startDate,endDate);
			if (size < 0) {
				log.info("No Clarification to be pushed to MongoDB");
				return false;
			}
			log.info("Clarification size "+size);

			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<ClarificationTracker> clarificationList = clarificationTrackerService.listAllClarificationByLastSyncDate(i*pageSize, pageSize, startDate,endDate);
				if (clarificationList != null) {
					for(ClarificationTracker clarification:clarificationList){
						addClarificationToMongoDB(clarification);
					}
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the Clarification to MongoDB ", e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean addTestSuiteToMongoDB(Integer testSuiteId) {
		TestSuiteList testSuite =  testSuiteConfigurationService.getByTestSuiteId(testSuiteId);
		if (testSuite == null) {
			log.info("Could not find testSuitetestSuite with ID : " + testSuiteId);
			return false;
		} else  {
			return addTestSuiteToMongoDB(testSuite);
		}
	}

	@Override
	@Transactional
	public boolean addTestSuiteToMongoDB(TestSuiteList testSuite) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add TestSuiteList to MongoDB. MongoDB is not setup");
			return false;
		}
		if (testSuite == null) {
			log.info("Could not add TestSuiteList to MongoDB. TestSuiteList Result is null");
			return false;
		}
		TestSuiteListMongo testSuiteListMongo=new TestSuiteListMongo(testSuite);
		
		testSuiteListMongoDAO.save(testSuiteListMongo);
		
		return true;
	}

	@Override
	@Transactional
	public boolean addAllTestSuiteToMongoDB(Date startDate, Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push addAllTestSuiteToMongoDB to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = testSuiteConfigurationService.countAllTestSuites(startDate,endDate);
			log.info("bug size "+size);
			if (size < 0) {
				log.info("No EffortTracker to be pushed to MongoDB");
				return false;
			}
			
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<TestSuiteList> testSuiteList = testSuiteConfigurationService.listAllTestSuites(i*pageSize, pageSize, startDate,endDate);
				
				log.info("testSuiteList size  "+testSuiteList.size());
				if (testSuiteList != null) {
					for(TestSuiteList testSuite:testSuiteList)
						addTestSuiteToMongoDB(testSuite);
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the TestSuite to MongoDB ", e);
			return false;
		}
	}
	
	
	
	
	@Override
	@Transactional
	public boolean addReseveredResourceToMongoDB(Integer reservationId) {
		
			TestFactoryResourceReservation testFactoryResourceReservation =  workPackageService.getTestFactoryResourceReservationById(reservationId);
			
			if (reservationId == null) {
				log.info("Could not find ReseveredResource with ID : " + reservationId);
				return false;
			} else  {
				return addReseveredResourceToMongoDB(testFactoryResourceReservation);
			}
		
	}
	
	
	@Override
	@Transactional
	public boolean addReseveredResourceToMongoDB(TestFactoryResourceReservation testFactoryResourceReservation) {
		try{
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not add ReseveredResource to MongoDB. MongoDB is not setup");
				return false;
			}
			if (testFactoryResourceReservation == null) {
				log.info("Could not add ReseveredResource to MongoDB. ReseveredResource Result is null");
				return false;
			}
			TestFactoryReservedResourcesMongo testFactoryReservedResourcesMongo=new TestFactoryReservedResourcesMongo(testFactoryResourceReservation);
			
			mongoDbDAO.save(testFactoryReservedResourcesMongo);
		}catch(Exception ex){
			log.error("Unable to push ReseveredResource ", ex);
			
		}
		
		return true;
	}
	

	@Override
	@Transactional
	public boolean addAllReseveredResourceToMongoDB(Date startDate, Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push addAllReseveredResourceToMongoDB to MongoDB. MongoDB is not setup");
				return false;
			}
			
			int size = workPackageService.countAllReservedResources(startDate,endDate);
			
			log.info("ReservedResources size "+size);
			if (size < 0) {
				log.info("No ReservedResources to be pushed to MongoDB");
				return false;
			}
			
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<TestFactoryResourceReservation> testFactoryResourceReservations = workPackageService.listAllReservedResources(i*pageSize, pageSize, startDate,endDate);
				
				log.info("ReservedResources size  "+testFactoryResourceReservations.size());
				if (testFactoryResourceReservations != null) {
					for(TestFactoryResourceReservation reservation:testFactoryResourceReservations)
						addReseveredResourceToMongoDB(reservation);
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the ReservedResources to MongoDB ", e);
			return false;
		}
	}
	
	
	
	
	
	
	@Override
	@Transactional
	public boolean addResourceDemandToMongoDB(Integer demandId) {
		
			WorkPackageDemandProjection workPackageDemandProjection =  workPackageService.getWorkPackageDemandProjectionById(demandId);
			if (demandId == null) {
				log.info("Could not find workPackageDemandProjection with ID : " + demandId);
				return false;
			} else  {
				return addResourceDemandToMongoDB(workPackageDemandProjection);
			}
		
	}
	
	
	@Override
	@Transactional
	public boolean addResourceDemandToMongoDB(WorkPackageDemandProjection workPackageDemandProjection) {
		try{
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not add WorkPackageDemandProjection to MongoDB. MongoDB is not setup");
				return false;
			}
			if (workPackageDemandProjection == null) {
				log.info("Could not add WorkPackageDemandProjection to MongoDB. WorkPackageDemandProjection Result is null");
				return false;
			}
			WorkpackageDemandProjectionMongo workpackageDemandProjectionMongo=new WorkpackageDemandProjectionMongo(workPackageDemandProjection);
			
			mongoDbDAO.save(workpackageDemandProjectionMongo);
		}catch(Exception ex){
			log.error("Unable to push demand ", ex);
			
		}
		
		return true;
	}
	
	
	@Override
	@Transactional
	public boolean addAllResourceDemandToMongoDB(Date startDate, Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push addAllResourceDemandToMongoDB to MongoDB. MongoDB is not setup");
				return false;
			}
			
			int size = workPackageService.countAllResourceDemands(startDate,endDate);
			
			log.info("Demand size "+size);
			if (size < 0) {
				log.info("No Demands to be pushed to MongoDB");
				return false;
			}
			
			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<WorkPackageDemandProjection> wDemandProjections = workPackageService.listAllResourceDemands(i*pageSize, pageSize, startDate,endDate);
				log.info("Demands size  "+wDemandProjections.size());
				if (wDemandProjections != null) {
					for(WorkPackageDemandProjection demand:wDemandProjections)
						addResourceDemandToMongoDB(demand);
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the ResourceDemandToMongoDB to MongoDB ", e);
			return false;
		}
	}
	
	
	
	
	@Override
	@Transactional
	public boolean addChangeRequestToMongoDB(Integer changeRequestId) {
		
		ChangeRequest changeRequest = changeRequestService.getChangeRequestById(changeRequestId);
		if (changeRequest == null) {
			log.info("Could not find ChangeRequest Result with ID : " + changeRequestId);
			return false;
		} else  {
			return addChangeRequestToMongoDB(changeRequest);
		}
	}

	
	
	@Override
	@Transactional
	public boolean addChangeRequestToMongoDB(ChangeRequest changeRequest) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add ChangeRequest to MongoDB. MongoDB is not setup");
			return false;
		}
		if (changeRequest == null) {
			log.info("Could not add ChangeRequest to MongoDB. Product Result is null");
			return false;
		}
		ChangeRequestMongo changeRequestMongo = new ChangeRequestMongo(changeRequest);
		Integer entityId=changeRequestMongo.getEntityTypeId();
		setValue(changeRequestMongo,entityId);
		
		changeRequestMongoDAO.save(changeRequestMongo);

		return true;
	}
	
	@Override
	@Transactional
	public boolean addAllChangeRequestsToMongoDB(Date startDate,Date endDate) {
	
		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push changeRequest to MongoDB. MongoDB is not setup");
				return false;
			}
	
			int size = changeRequestService.countAllChangeRequest(startDate, endDate);
			if (size < 0) {
				log.info("No ChangeRequest to be pushed to MongoDB");
				return false;
			}
			log.info("ChangeRequest size "+size);

			int pageSize = 100;
			int numberOfPages = (int) Math.ceil(size/100.0);
			for (int i = 0; i < numberOfPages; i++) {
				
				List<ChangeRequest> changeRequestList = changeRequestService.listAllChangeRequestByLastSyncDate(i*pageSize, pageSize, startDate,endDate);
				if (changeRequestList != null) {
					for(ChangeRequest changeRequest : changeRequestList){
						addChangeRequestToMongoDB(changeRequest);
					}
				}
			}
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the ChangeRequest to MongoDB ", e);
			return false;
		}
	}
	@Override
	@Transactional
	public List<JSONObject> getCollectionForPivotReportBasedOnCollectionName(String collectionName, int testFactoryId, int productId){
		return dashboardSLADAO.getCollectionForPivotReportBasedOnCollectionName(collectionName, testFactoryId, productId);		
	}
	public List<String> getKeyNameForPivotReportBasedOnCollectionName(String collectionName){
		return dashboardSLADAO.getKeyNameForPivotReportBasedOnCollectionName(collectionName);
	}
	@Override
	@Transactional
	public void updateParentStatusInChildColletions(Integer entityMasterId,Integer instanceId,Integer status) {
		try{
			
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push TestcaseResult to MongoDB. MongoDB is not setup");
				return;
			}
			
			List<String>collectionNames = new ArrayList<String>();

			TestFactoryLab testFactoryLab = null;
			TestFactory testFactory = null;
			ProductMaster productMaster = null;
			ProductVersionListMaster productVersionListMaster = null;
			ProductBuild productBuild = null;
			ActivityWorkPackage activityWorkPackage = null; 
			Activity activitydata = null;
			Integer parentStatus = 1;
			
			String fieldName="";
			if(entityMasterId == IDPAConstants.PRODUCT_ENTITY_MASTER_ID){
				if(status == 0){
					collectionNames.add(MongodbConstants.TESTCASES);
					collectionNames.add(MongodbConstants.BUILD);
					collectionNames.add(MongodbConstants.MONGO_VERSIONS);
					collectionNames.add(MongodbConstants.FEATURES_COLLECTION_NAME);
					collectionNames.add(MongodbConstants.PRODUCTS_USER_ROLE);
					collectionNames.add(MongodbConstants.PRODUCT_TEAM_RESOURCES);
					collectionNames.add(MongodbConstants.WORKPACKAGE);
					collectionNames.add(MongodbConstants.CLARIFICATION_MONGO);
					collectionNames.add(MongodbConstants.CHANGE_REQUEST_MONGO);
					collectionNames.add(MongodbConstants.TEST_RUN_JOB);
					collectionNames.add(MongodbConstants.ACTIVITYWORKPACKAGE);
					collectionNames.add(MongodbConstants.ACTIVITY);
					collectionNames.add(MongodbConstants.ACTIVITYTASK);
					fieldName = "productId";
					dashboardSLADAO.updateParentStatusInChildColletions(collectionNames,entityMasterId,instanceId,status,fieldName);
				}else{
					
					 productMaster = productListService.getProductById(instanceId);
					 if(productMaster!= null){
						 testFactory = productMaster.getTestFactory();
							boolean isAllParentActive = true;
							
							if(isAllParentActive && (testFactory == null || testFactory.getStatus() == 0)){
								isAllParentActive = false;
								
							}
							if(testFactory != null){
								testFactoryLab = testFactory.getTestFactoryLab();
							}
							if(!isAllParentActive){
								parentStatus = 0;
							}
							
							dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive("versions",parentStatus,status,instanceId,"productId");
							List<ProductVersionListMaster>	versionList=productVersionListMasterDAO.list(instanceId);
							for(ProductVersionListMaster version:versionList){
								dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive("builds",parentStatus,version.getStatus(),version.getProductVersionListId(),"versionId");
								List<ProductBuild> builds = productBuildDAO.list(version.getProductVersionListId());
								for(ProductBuild build:builds){
									dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive(MongodbConstants.ACTIVITYWORKPACKAGE,parentStatus,build.getStatus(),build.getProductBuildId(),"buildId");
									List<ActivityWorkPackage> activityWp = activityWorkPackageService.getActivityWorkPackageByBuildId(build.getProductBuildId());
									for(ActivityWorkPackage awp:activityWp){
										dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive(MongodbConstants.ACTIVITY,parentStatus,awp.getIsActive(),awp.getActivityWorkPackageId(),"activityWorkPackageId");
										List<Activity> activities = activityService.getActivityByActivtyWorkpackageId(awp.getActivityWorkPackageId());
										for(Activity activity:activities)
										dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive(MongodbConstants.ACTIVITYTASK,parentStatus,activity.getIsActive(),activity.getActivityId(),"activityId");
									}
									
								}
							
						}
					 }
					
				
				}
				
			}if(entityMasterId == IDPAConstants.PRODUCT_VERSION_ENTITY_MASTER_ID){
				if(status == 0){
					collectionNames.add(MongodbConstants.BUILD);
					collectionNames.add(MongodbConstants.WORKPACKAGE);
					collectionNames.add(MongodbConstants.CLARIFICATION_MONGO);
					collectionNames.add(MongodbConstants.CHANGE_REQUEST_MONGO);
					collectionNames.add(MongodbConstants.TEST_RUN_JOB);
					collectionNames.add(MongodbConstants.ACTIVITYWORKPACKAGE);
					collectionNames.add(MongodbConstants.ACTIVITY);
					collectionNames.add(MongodbConstants.ACTIVITYTASK);
					fieldName = "versionId";
					dashboardSLADAO.updateParentStatusInChildColletions(collectionNames,entityMasterId,instanceId,status,fieldName);
				}else{
					
					 productVersionListMaster = productListService.getProductVersionListMasterById(instanceId);
					productMaster = productVersionListMaster.getProductMaster();
					boolean isAllParentActive = true;
					
					if(isAllParentActive && (productMaster == null || productMaster.getStatus() == 0)){
						isAllParentActive = false;
					}
					if(productMaster != null){
						testFactory = productMaster.getTestFactory();
					}
					if(isAllParentActive && (testFactory == null || testFactory.getStatus() == 0)){
						isAllParentActive = false;
					}
					if(testFactory != null){
						testFactoryLab = testFactory.getTestFactoryLab();
					}
					if(!isAllParentActive){
						parentStatus = 0;
					}
					
					dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive("builds",parentStatus,status,instanceId,"versionId");
					List<ProductBuild> builds = productBuildDAO.list(instanceId);
					for(ProductBuild build:builds){
						dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive(MongodbConstants.ACTIVITYWORKPACKAGE,parentStatus,build.getStatus(),build.getProductBuildId(),"buildId");
						List<ActivityWorkPackage> activityWp = activityWorkPackageService.getActivityWorkPackageByBuildId(build.getProductBuildId());
						for(ActivityWorkPackage awp:activityWp){
							dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive(MongodbConstants.ACTIVITY,parentStatus,awp.getIsActive(),awp.getActivityWorkPackageId(),"activityWorkPackageId");
							List<Activity> activities = activityService.getActivityByActivtyWorkpackageId(awp.getActivityWorkPackageId());
							for(Activity activity:activities)
							dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive(MongodbConstants.ACTIVITYTASK,parentStatus,activity.getIsActive(),activity.getActivityId(),"activityId");
						}
				}
				
				}
				
			}if(entityMasterId == IDPAConstants.PRODUCT_BUILD_ENTITY_MASTER_ID){
				
				if(status == 0){
					collectionNames.add(MongodbConstants.WORKPACKAGE);
					collectionNames.add(MongodbConstants.ACTIVITYWORKPACKAGE);
					collectionNames.add(MongodbConstants.ACTIVITY);
					collectionNames.add(MongodbConstants.ACTIVITYTASK);
					collectionNames.add(MongodbConstants.CLARIFICATION_MONGO);
					collectionNames.add(MongodbConstants.CHANGE_REQUEST_MONGO);
					collectionNames.add(MongodbConstants.TEST_RUN_JOB);
					fieldName = "buildId";
					dashboardSLADAO.updateParentStatusInChildColletions(collectionNames,entityMasterId,instanceId,status,fieldName);
				}else{
					productBuild = productListService.getProductBuildById(instanceId, 0);
					productVersionListMaster = productBuild.getProductVersion();
					boolean isAllParentActive = true;
					
					
					if(isAllParentActive && (productVersionListMaster == null || productVersionListMaster.getStatus() == 0)){
						isAllParentActive = false;
					}
					if(productVersionListMaster != null){
						productMaster = productVersionListMaster.getProductMaster();
					}
					
					if(isAllParentActive && (productMaster == null || productMaster.getStatus() == 0)){
						isAllParentActive = false;
					}
					if(productMaster != null){
						testFactory = productMaster.getTestFactory();
					}
					
					if(isAllParentActive && (testFactory == null || testFactory.getStatus() == 0)){
						isAllParentActive = false;
					}
					if(testFactory != null){
						testFactoryLab = testFactory.getTestFactoryLab();
					}
					
					if(!isAllParentActive){
						parentStatus = 0;
					}
					
					dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive(MongodbConstants.ACTIVITYWORKPACKAGE,parentStatus,status,instanceId,"buildId");
					List<ActivityWorkPackage> activityWp = activityWorkPackageService.getActivityWorkPackageByBuildId(instanceId);
					for(ActivityWorkPackage awp:activityWp){
						dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive(MongodbConstants.ACTIVITY,parentStatus,awp.getIsActive(),awp.getActivityWorkPackageId(),"activityWorkPackageId");
						List<Activity> activities = activityService.getActivityByActivtyWorkpackageId(awp.getActivityWorkPackageId());
						for(Activity activity:activities)
						dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive(MongodbConstants.ACTIVITYTASK,parentStatus,activity.getIsActive(),activity.getActivityId(),"activityId");
					}
				}
				
			}if(entityMasterId == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
				if(status == 0){
					collectionNames.add(MongodbConstants.CLARIFICATION_MONGO);
					collectionNames.add(MongodbConstants.CHANGE_REQUEST_MONGO);
					collectionNames.add(MongodbConstants.ACTIVITY);
					collectionNames.add(MongodbConstants.ACTIVITYTASK);
					fieldName = "activityWorkPackageId";
					dashboardSLADAO.updateParentStatusInChildColletions(collectionNames,entityMasterId,instanceId,status,fieldName);
				}else{
					
					boolean isAllParentActive = true;
					activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(instanceId, 1);
					productBuild = activityWorkPackage.getProductBuild();
					
					if(isAllParentActive && (productBuild == null || productBuild.getStatus() == 0)){
						isAllParentActive = false;
					}
					if(productBuild != null){
						productVersionListMaster = productBuild.getProductVersion();
					}
					if(isAllParentActive && (productVersionListMaster == null || productVersionListMaster.getStatus() == 0)){
						isAllParentActive = false;
					}
					if(productVersionListMaster != null){
						productMaster = productVersionListMaster.getProductMaster();
					}
					if(isAllParentActive && (productMaster == null || productMaster.getStatus() == 0)){
						isAllParentActive = false;
					}
					if(productMaster != null){
						testFactory = productMaster.getTestFactory();
					}
					if(isAllParentActive && (testFactory == null || testFactory.getStatus() == 0)){
						isAllParentActive = false;
					}
					if(testFactory != null){
						testFactoryLab = testFactory.getTestFactoryLab();
					}
					if(!isAllParentActive){
						parentStatus = 0;
					}
					
					dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive(MongodbConstants.ACTIVITY,parentStatus,status,instanceId,"activityWorkPackageId");
					List<Activity>  activities = activityDAO.listActivitiesByActivityWorkPackageId(instanceId,1,2);
					for(Activity activity:activities){
						dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive(MongodbConstants.ACTIVITYTASK,parentStatus,activity.getIsActive(),activity.getActivityId(),"activityId");
					}
				}
				
			}if(entityMasterId == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID){
				if(status == 0){
					collectionNames.add(MongodbConstants.CLARIFICATION_MONGO);
					collectionNames.add(MongodbConstants.CHANGE_REQUEST_MONGO);
					collectionNames.add(MongodbConstants.ACTIVITYTASK);
					fieldName = "activityId";
					dashboardSLADAO.updateParentStatusInChildColletions(collectionNames,entityMasterId,instanceId,status,fieldName);
				}else{
					boolean isAllParentActive = true;
					activitydata = activityService.getActivityById(instanceId, 1);
					activityWorkPackage = activitydata.getActivityWorkPackage();
					
					if(isAllParentActive && (activityWorkPackage == null || activityWorkPackage.getIsActive() == 0) ){
						isAllParentActive = false;
					}
					if(activityWorkPackage != null){
						productBuild = activityWorkPackage.getProductBuild();
					}
					if(isAllParentActive && (productBuild == null || productBuild.getStatus() == 0)){
						isAllParentActive = false;
					}
					if(productBuild != null){
						productVersionListMaster = productBuild.getProductVersion();
					}
					if(isAllParentActive && (productVersionListMaster == null || productVersionListMaster.getStatus() == 0)){
						isAllParentActive = false;
					}
					if(productVersionListMaster != null){
						productMaster = productVersionListMaster.getProductMaster();
					}
					if(isAllParentActive && (productMaster == null || productMaster.getStatus() == 0)){
						isAllParentActive = false;
					}
					if(productMaster != null){
						testFactory = productMaster.getTestFactory();
					}
					if(isAllParentActive && (testFactory == null || testFactory.getStatus() == 0)){
						isAllParentActive = false;
					}
					if(testFactory != null){
						testFactoryLab = testFactory.getTestFactoryLab();
					}
					if(!isAllParentActive){
						parentStatus = 0;
					}
					dashboardSLADAO.updateParentStatusInChildColletionsWhileUpdatingActive(MongodbConstants.ACTIVITYTASK,parentStatus,status,instanceId,"activityId");
				}
			}
		
	}catch(Exception e){
		log.error("Unable to Update parent status  to MongoDB ", e);
	}
}

	@Override
	@Transactional
	public List<DPAWorkbookCollectionMongo> getDPAWorkbookMongoCollectionList(Date endDate, String testFactoryName, String productName, Set<ProductUserRole> userProducts) {
		return dashboardSLADAO.getDPAWorkbookMongoCollectionList(endDate,testFactoryName, productName, userProducts);
	}

	@Override
	@Transactional
	public Map<String, Object> getDPAWorkbookMongoCollectionCounts(Date startDate, Date endDate, String testFactoryName, String productName, Set<ProductUserRole> userProducts) {
		return dashboardSLADAO.getDPAWorkbookMongoCollectionCounts(startDate, endDate, testFactoryName, productName, userProducts);
	}

	@Override
	@Transactional
	public void deleteResourceDemandFromMongoDb(Integer demandId) {
		mongoDbDAO.deleteResourceDemandFromMongoDb(demandId);
	}

	@Override
	@Transactional
	public void deleteReseveredResourceFromMongoDb(Integer reservedId) {
		if (MONGODB_AVAILABLE.equals("YES")) {
			mongoDbDAO.deleteReseveredResourceFromMongoDb(reservedId);
		}
	}

	@Override
	@Transactional
	public void addCustomFieldsOnBulkUpdate(Integer entityTypeId, List<Integer> instanceIds, String collectionName){
		try{
			List<CustomFieldValues> customFieldValuesList = customFieldDAO.getCustomFieldValuesExistForAllInstanceOfEntity(entityTypeId, instanceIds, "Single", 1);
			HashMap<Integer, HashMap<String, Object>> allInstanceCustomFields = new HashMap<Integer, HashMap<String, Object>>();
			if(customFieldValuesList != null && customFieldValuesList.size() > 0){
				for(CustomFieldValues customFieldValues : customFieldValuesList){
					//addOrUpdateCustomField(customFieldValues.getEntityInstanceId(), customFieldValues.getCustomFieldId().getFieldName(), customFieldValues.getFieldValue(), collectionName);
					if(!allInstanceCustomFields.containsKey(customFieldValues.getEntityInstanceId())){
						HashMap<String, Object> customField = new HashMap<String, Object>();
						allInstanceCustomFields.put(customFieldValues.getEntityInstanceId(), customField);
					}
					allInstanceCustomFields.get(customFieldValues.getEntityInstanceId()).put(customFieldValues.getCustomFieldId().getFieldName(), customFieldService.convertCustomFieldDataType(customFieldValues.getFieldValue(), customFieldValues.getCustomFieldId().getDataType()));
				}
			}
			
			if(!allInstanceCustomFields.isEmpty() && allInstanceCustomFields.size() > 0){
				for(Map.Entry<Integer, HashMap<String, Object>> customFields : allInstanceCustomFields.entrySet()){
					addOrUpdateCustomField(customFields.getKey(), customFields.getValue(), collectionName);
				}
			}
		}catch(Exception ex){
			log.error("Error in addCustomFieldsOnBulkUpdate - ", ex);
		}
	}
	
	@Override
	@Transactional
	public void addOrUpdateCustomField(Integer entityInstanceId, HashMap<String, Object> customFields, String collectionName) {
		if(collectionName != null && !collectionName.trim().isEmpty()){
			mongoDbDAO.addOrUpdateCustomField(entityInstanceId, customFields, collectionName);
		}
	}

	@Override
	@Transactional
	public void addOrUpdateCustomField(Integer entityInstanceId, String fieldName, Object fieldValue, String collectionName) {
		if(collectionName != null && !collectionName.trim().isEmpty()){
			mongoDbDAO.addOrUpdateCustomField(entityInstanceId, fieldName, fieldValue, collectionName);
		}
	}

	@Override
	@Transactional
	public List<JSONObject> getSingleValueMetricsARIDList(Date endDate, String testFactoryName) {
		return dashboardSLADAO.getSingleValueMetricsARIDList(endDate, testFactoryName);
	}

	@Override
	@Transactional
	public List<JSONObject> getMongoDefectsPmoByPmtId(Float pmtId) {
		return dashboardSLADAO.getMongoDefectsPmoByPmtId(pmtId);
	}

	@Override
	@Transactional
	public JSONObject getMongoPmoByPmtId(Float pmtId) {
		return dashboardSLADAO.getMongoPmoByPmtId(pmtId);
	}

	@Override
	@Transactional
	public boolean addChangeRequestMappingToMongoDB(EntityRelationship entityRelationship) {
		Activity activity = null;
		ChangeRequest changeRequest = null;
		ActivityWorkPackage activityWorkPackage = null;
		try {			
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not add ChangeRequestMapping to MongoDB. MongoDB is not setup");
				return false;
			}
			if (entityRelationship == null) {
				log.info("Could not add ChangeRequestMapping to MongoDB. Result is null");
				return false;
			}
			activity = activityService.getActivityById(entityRelationship.getEntityInstanceId1(), 1);
			if(activity != null&& activity.getActivityWorkPackage() != null) {
				activityWorkPackage = activity.getActivityWorkPackage();
			}
			changeRequest = changeRequestService.getChangeRequestById(entityRelationship.getEntityInstanceId2());
			if(activity != null && activityWorkPackage != null && changeRequest != null){				
				ChangeRequestMappingMongo changeRequestMongo = new ChangeRequestMappingMongo(entityRelationship,activity.getActivityName(),changeRequest.getChangeRequestName(),activityWorkPackage.getActivityWorkPackageId(),activityWorkPackage.getActivityWorkPackageName(),activity);
				changeRequestMongoDAO.saveChangeRequestMapping(changeRequestMongo);
			}
		} catch (Exception e) {
			log.error("addChangeRequestMappingToMongoDB failed", e);
		}
		return true;
	}

	@Override
	@Transactional
	public boolean removeChangeRequestMappingFromMongoDB(EntityRelationship entityRelationship) {
		Integer entityRelationshipId = entityRelationship.getId();
		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not delete ChangeRequestMapping from MongoDB. MongoDB is not setup");
			return false;
		}
		if (entityRelationship == null) {
			log.info("Could not delete ChangeRequestMapping from MongoDB. Result is null");
			return false;
		}
		changeRequestMongoDAO.removeChangeRequestMapping(entityRelationshipId);
		return true;
	}

	@Override
	@Transactional
	public boolean addAllChangeRequestMappingToMongoDB(Date startDate, Date endDate) {
		List<EntityRelationship> entityRelationships = null;
		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push changeRequest to MongoDB. MongoDB is not setup");
				return false;
			}
			entityRelationships = entityRelationshipService.getEntityRelationShipByTypes(IDPAConstants.ENTITY_ACTIVITY_ID , IDPAConstants.CHANGE_REQUEST_ENTITY_MASTER_ID);
			for(EntityRelationship er : entityRelationships){				
				addChangeRequestMappingToMongoDB(er);
			}
			log.debug("addAllChangeRequestMappingToMongoDB successful");
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
	@Override
	@Transactional
	public boolean addProductFeatueBuildMappingtoMongoDB(Integer mappingId) {

		List<ProductFeatureProductBuildMapping> produtFeatureProductBuildMappingList = productListService.getProductFeatureAndProductBuildMappingId(mappingId);
		if (produtFeatureProductBuildMappingList == null) {
			log.info("Could not find Produt Feature build mapping with ID : " + mappingId);
			return false;
		} else  {
			if(produtFeatureProductBuildMappingList != null && produtFeatureProductBuildMappingList.size() >0) {
				String buildName="";
				String featureName="";
				Integer versionId=0;
				String versionName="";
				for(ProductFeatureProductBuildMapping mapping :produtFeatureProductBuildMappingList){
					ProductBuild build= productListService.getProductBuildById(mapping.getBuildId(), 0);
					ProductFeature feature = productFeatureDAO.getByProductFeatureId(mapping.getFeatureId());
					
					if(build != null ) {
						buildName = build.getBuildname();
						versionId = build.getProductVersion().getProductVersionListId();
						versionName = build.getProductVersion().getProductVersionName();
					}
					if(feature != null) {
						featureName = feature.getProductFeatureName();
					}
					addProductFeatueBuildMappingtoMongoDB(mapping,featureName,buildName,versionId,versionName);
					
				}
			}
			
		}
		return true;
	}
	
	@Override
	@Transactional
	public boolean addProductFeatueBuildMappingtoMongoDB(ProductFeatureProductBuildMapping productFeatureProductBuildMapping,String featureName,String buildName,Integer versionId,String versionName) {

		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add BotMaster to MongoDB. MongoDB is not setup");
			return false;
		}
		if (productFeatureProductBuildMapping == null) {
			log.info("Could not add productFeatureProductBuildMapping to MongoDB. BotMaster Result is null");
			return false;
		}
		
		ProductFeatureProductBuildMappingCollectionMongo productBuildMappingCollectionMongo = new ProductFeatureProductBuildMappingCollectionMongo(productFeatureProductBuildMapping,featureName,buildName,versionId,versionName);
		mongoDbDAO.saveProductFeatureBuildMapping(productBuildMappingCollectionMongo);
		return true;
	}
	
	
	@Override
	@Transactional
	public boolean addAllProductFeatueBuildMappingtoMongoDB(Date startDate,Date endDate) {

		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push ProductFeatueBuildMappingtoMongoDB to MongoDB. MongoDB is not setup");
				return false;
			}
	
			List<ProductFeatureProductBuildMapping> produtFeatureProductBuildMappingList = productListService.getProductFeatureAndProductBuildMappingList();
			if (produtFeatureProductBuildMappingList == null) {
				log.info("Could not find Produt Feature build mapping ");
				return false;
			} else  {
				if(produtFeatureProductBuildMappingList != null && produtFeatureProductBuildMappingList.size() >0) {
					String buildName="";
					String featureName="";
					Integer versionId=0;
					String versionName="";
					for(ProductFeatureProductBuildMapping mapping :produtFeatureProductBuildMappingList){
						ProductBuild build= productListService.getProductBuildById(mapping.getBuildId(), 0);
						ProductFeature feature = productFeatureDAO.getByProductFeatureId(mapping.getFeatureId());
						
						if(build != null ) {
							buildName = build.getBuildname();
							versionId = build.getProductVersion().getProductVersionListId();
							versionName = build.getProductVersion().getProductVersionName();
						}
						if(feature != null) {
							featureName = feature.getProductFeatureName();
						}
						addProductFeatueBuildMappingtoMongoDB(mapping,featureName,buildName,versionId,versionName);
						
					}
				}
				
			}
			
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the ProductFeatueBuildMappingtoMongoDB to MongoDB ",e);
			return false;
		}
	}

	@Override
	public boolean addTestCaseProductVersionMappingtoMongoDB(Integer mappingId) {
		try {
		List<TestCaseProductVersionMapping> testCaseVersionMappingList=testCaseToVersionMappingDAO.getTestcaseToProductVersionMappingId(mappingId);
		String testCaseName="";
		String versionName="";
		if(testCaseVersionMappingList != null && testCaseVersionMappingList.size() >0) {
			for(TestCaseProductVersionMapping testCaseProductVersionMapping : testCaseVersionMappingList) {
				
				
				TestCaseList testCase=testCaseService.getTestCaseById(testCaseProductVersionMapping.getTestCaseId());
				ProductVersionListMaster version=productVersionListMasterDAO.getByProductListId(testCaseProductVersionMapping.getVersionId());
				if(testCase != null) {
					testCaseName=testCase.getTestCaseName();
				}
				
				if(version != null) {
					versionName=version.getProductVersionName();
				}
				addTestCaseProductVersionMappingtoMongoDB(testCaseProductVersionMapping,testCaseName,versionName);
			}
		}
		return true;
		}catch(Exception e) {
			
		}
		return false;
	}

	@Override
	public boolean addTestCaseProductVersionMappingtoMongoDB(TestCaseProductVersionMapping testCaseProductVersionMapping,String testCaseName,String versionName) {
		try {
		
			TestCaseProductVersionMappingCollectionMongo testCaseProductVersionMappingCollectionMongo = new TestCaseProductVersionMappingCollectionMongo(testCaseProductVersionMapping,testCaseName,versionName);
			mongoDbDAO.saveTestCaseProductVersionMapping(testCaseProductVersionMappingCollectionMongo);
		 return true;
		}catch(Exception e) {
			
		}

		return false;
	}

	@Override
	public boolean addTestCaseProductVersionMappingtoMongoDB(Date startDate,Date endDate) {
			
		try {
			List<TestCaseProductVersionMapping> testCaseVersionMappingList=testCaseToVersionMappingDAO.getTestcaseToProductVersionMappingList();
			String testCaseName="";
			String versionName="";
			if(testCaseVersionMappingList != null && testCaseVersionMappingList.size() >0) {
				for(TestCaseProductVersionMapping testCaseProductVersionMapping : testCaseVersionMappingList) {
					
					
					TestCaseList testCase=testCaseService.getTestCaseById(testCaseProductVersionMapping.getTestCaseId());
					ProductVersionListMaster version=productVersionListMasterDAO.getByProductListId(testCaseProductVersionMapping.getVersionId());
					if(testCase != null) {
						testCaseName=testCase.getTestCaseName();
					}
					
					if(version != null) {
						versionName=version.getProductVersionName();
					}
					addTestCaseProductVersionMappingtoMongoDB(testCaseProductVersionMapping,testCaseName,versionName);
				}
			}
			return true;
			}catch(Exception e) {
				
			}
		
		return false;
	}
	
	@Override
	@Transactional
	public boolean addTestCaseScriptToMongoDB(TestCaseScript testCaseScript) {
		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add TestCaseScript to MongoDB. MongoDB is not setup");
			return false;
		}
		if (testCaseScript == null) {
			log.info("Could not add TestCaseScript to MongoDB. Product Result is null");
			return false;
		}
		TestCaseScriptMongo testCaseScriptMongo = new TestCaseScriptMongo(testCaseScript);	
		testCaseScriptMongoDAO.save(testCaseScriptMongo);
		return true;
	}

	@Override
	@Transactional
	public boolean addTestCaseScriptMappingToMongoDB(TestCaseScriptHasTestCase testCaseScriptHasTestCase) {
		Activity activity = null;
		ChangeRequest changeRequest = null;
		ActivityWorkPackage activityWorkPackage = null;
		try {			
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not add TestCaseScriptMapping to MongoDB. MongoDB is not setup");
				return false;
			}
			if (testCaseScriptHasTestCase == null) {
				log.info("Could not add TestCaseScriptMapping to MongoDB. Result is null");
				return false;
			}
				TestCaseScriptMappingMongo testCaseScriptMappingMongo = new TestCaseScriptMappingMongo(testCaseScriptHasTestCase);
				testCaseScriptMongoDAO.saveTestCaseScriptMapping(testCaseScriptMappingMongo);
		} catch (Exception e) {
			log.error("addChangeRequestMappingToMongoDB failed", e);
		}
		return true;
	}

	@Override
	@Transactional
	public boolean removeTestCaseScriptMappingFromMongoDB(Integer testCaseScriptHasMappingId) {
		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not delete TestCaseScriptMapping from MongoDB. MongoDB is not setup");
			return false;
		}
		if (testCaseScriptHasMappingId == null) {
			log.info("Could not delete TestCaseScriptMapping from MongoDB. Result is null");
			return false;
		}
		testCaseScriptMongoDAO.removeTestCaseScriptMapping(testCaseScriptHasMappingId);
		return true;
	}

	@Override
	@Transactional
	public boolean addAllTestCaseScriptToMongoDB(Date startDate, Date endDate) {
		List<TestCaseScript> testCaseScriptList = null;
		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push TestCaseScript to MongoDB. MongoDB is not setup");
				return false;
			}
	
			testCaseScriptList = testCaseAutomationScriptDAO.getTestcaseScriptsList();
			if(testCaseScriptList != null){
				for(TestCaseScript testCaseScript : testCaseScriptList) {
					addTestCaseScriptToMongoDB(testCaseScript);
				}
			}
			log.debug("addAllTestCaseScriptToMongoDB successful");
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the TestCaseScript to MongoDB ", e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean addAllTestCaseScriptMappingToMongoDB(Date startDate, Date endDate) {
		List<TestCaseScriptHasTestCase> testCaseScriptHasTestCaseList = null;
		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push TestCaseScriptMapping to MongoDB. MongoDB is not setup");
				return false;
			}
	
			testCaseScriptHasTestCaseList = testCaseAutomationScriptDAO.getTestCaseScriptAssociationList();
			if(testCaseScriptHasTestCaseList != null){
				for(TestCaseScriptHasTestCase testCaseScriptHasTestCase : testCaseScriptHasTestCaseList) {
					addTestCaseScriptMappingToMongoDB(testCaseScriptHasTestCase);
				}
			}
			log.debug("addAllTestCaseScriptMappingToMongoDB successful");
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the TestCaseScriptMapping to MongoDB ", e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean removeTestCaseScriptFromMongoDB(Integer testCaseScriptId) {
		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not delete TestCaseScript from MongoDB. MongoDB is not setup");
			return false;
		}
		if (testCaseScriptId == null) {
			log.info("Could not delete TestCaseScript from MongoDB. Result is null");
			return false;
		}
		testCaseScriptMongoDAO.removeTestCaseScript(testCaseScriptId);
		return true;
	}
	
	
	public boolean addAllDefectCollectionsToMongoDB(Date startDate, Date endDate) {
		List<DefectCollection> defectCollectionList = null;
		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push DefectCollection to MongoDB. MongoDB is not setup");
				return false;
			}
	
			defectCollectionList = defectCollectionDao.getDefectCollectionList();
			if(defectCollectionList != null){
				for(DefectCollection defectCollection : defectCollectionList) {
					addDefectCollectionToMongoDB(defectCollection);
				}
			}
			
			log.debug("addAllDefectCollectionsToMongoDB successful");
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the DefectCollection to MongoDB ", e);
			return false;
		}
	}
	
	@Override
	@Transactional
	public boolean addDefectCollectionToMongoDB(DefectCollection defectCollection) {
		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add DefectCollection to MongoDB. MongoDB is not setup");
			return false;
		}
		if (defectCollection == null) {
			log.info("Could not add DefectCollection to MongoDB. Product Result is null");
			return false;
		}
		ISEDefectCollectionMongo iseDefectCollectionMongo = new ISEDefectCollectionMongo(defectCollection);	
		mongoDbDAO.saveISEDefectCollection(iseDefectCollectionMongo);
		return true;
	}

	@Override
	@Transactional
	public boolean addFeatureTestCaseMappingToMongoDB(ProductFeature productFeature, TestCaseList testCaseList) {
		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not add FeatureTestCaseMapping to MongoDB. MongoDB is not setup");
			return false;
		}
		if (productFeature == null || testCaseList == null) {
			log.info("Could not add FeatureTestCaseMapping to MongoDB");
			return false;
		}
		TestCaseToProductFeatureMappingMongo mappingMongo = new TestCaseToProductFeatureMappingMongo(productFeature,testCaseList);	
		mongoDbDAO.saveFeatureTestCaseMapping(mappingMongo);
		return true;
	}

	
	@Override
	@Transactional
	public boolean addAllFeatureTestCaseMappingsToMongoDB(Date startDate, Date endDate) {
		List<ProductFeatureHasTestCase> productFeatureHasTestCaseList = null;
		ProductFeature productFeature = null;
		TestCaseList testCaseList = null;
		try {
			if (MONGODB_AVAILABLE.equals("NO")) {
				log.info("Could not push FeatureTestCaseMappings to MongoDB. MongoDB is not setup");
				return false;
			}
	
			productFeatureHasTestCaseList = productFeatureDAO.getProductFeatureHasTestCaseList();
			if(productFeatureHasTestCaseList != null) {
				for(ProductFeatureHasTestCase featureHasTestCase : productFeatureHasTestCaseList) {
					productFeature = productFeatureDAO.getByProductFeatureId(featureHasTestCase.getProductFeatureId());
					testCaseList = testCaseListDAO.getByTestCaseId(featureHasTestCase.getTestCaseId());
					addFeatureTestCaseMappingToMongoDB(productFeature,testCaseList);
				}
			}
			log.debug("addAllFeatureTestCaseMappingsToMongoDB successful");
			return true;
		} catch (Exception e) {
			log.error("Unable to push all the FeatureTestCaseMappings to MongoDB ", e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean removeFeatureTestCaseMappingFromMongoDB(Integer productFeatureId, Integer testCaseId) {
		if (MONGODB_AVAILABLE.equals("NO")) {
			log.info("Could not delete FeatureTestCaseMapping from MongoDB. MongoDB is not setup");
			return false;
		}
		if (productFeatureId == null || testCaseId == null) {
			log.info("Could not delete FeatureTestCaseMapping from MongoDB. Result is null");
			return false;
		}
		mongoDbDAO.removeFeatureTestCaseMapping(productFeatureId, testCaseId);
		return true;
	}
	
}