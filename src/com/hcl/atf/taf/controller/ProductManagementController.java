package com.hcl.atf.taf.controller;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.util.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.common.io.Files;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.CronValidate;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.controller.utilities.ZipTool;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.ProductFeatureDAO;
import com.hcl.atf.taf.dao.ProductTestEnvironmentDAO;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.exceptions.ATFException;
import com.hcl.atf.taf.integration.data.excel.ExcelTestDataIntegrator;
import com.hcl.atf.taf.model.ActivityMaster;
import com.hcl.atf.taf.model.ActivityTaskType;
import com.hcl.atf.taf.model.ActivityType;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.CommonActiveStatusMaster;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.DefectIdentificationStageMaster;
import com.hcl.atf.taf.model.DeviceLab;
import com.hcl.atf.taf.model.DeviceMakeMaster;
import com.hcl.atf.taf.model.DeviceModelMaster;
import com.hcl.atf.taf.model.DeviceType;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.EntityUserGroup;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCategory;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.EnvironmentGroup;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.HostPlatformMaster;
import com.hcl.atf.taf.model.HostTypeMaster;
import com.hcl.atf.taf.model.MobileType;
import com.hcl.atf.taf.model.PlatformType;
import com.hcl.atf.taf.model.Processor;
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
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.ServerType;
import com.hcl.atf.taf.model.StorageDrive;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseProductVersionMapping;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryManager;
import com.hcl.atf.taf.model.TestFactoryProductCoreResource;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunPlanGroup;
import com.hcl.atf.taf.model.TestRunPlangroupHasTestRunPlan;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestToolMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageFeature;
import com.hcl.atf.taf.model.WorkPackageFeatureExecutionPlan;
import com.hcl.atf.taf.model.WorkPackageTestCase;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkPackageTestSuite;
import com.hcl.atf.taf.model.WorkPackageTestSuiteExecutionPlan;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;
import com.hcl.atf.taf.model.dto.DefectWeeklyReportDTO;
import com.hcl.atf.taf.model.dto.ISERecommandedTestcases;
import com.hcl.atf.taf.model.dto.ProductSummaryDTO;
import com.hcl.atf.taf.model.json.JsonDecouplingCategory;
import com.hcl.atf.taf.model.json.JsonDefectsWeeklyReport;
import com.hcl.atf.taf.model.json.JsonEnvironment;
import com.hcl.atf.taf.model.json.JsonEnvironmentCategory;
import com.hcl.atf.taf.model.json.JsonEnvironmentCombination;
import com.hcl.atf.taf.model.json.JsonEnvironmentGroup;
import com.hcl.atf.taf.model.json.JsonFeatureTestCase;
import com.hcl.atf.taf.model.json.JsonGenericDevice;
import com.hcl.atf.taf.model.json.JsonHostList;
import com.hcl.atf.taf.model.json.JsonISERecommendationTestcases;
import com.hcl.atf.taf.model.json.JsonProductBuild;
import com.hcl.atf.taf.model.json.JsonProductFeature;
import com.hcl.atf.taf.model.json.JsonProductFeatureProductBuildMapping;
import com.hcl.atf.taf.model.json.JsonProductLocale;
import com.hcl.atf.taf.model.json.JsonProductMaster;
import com.hcl.atf.taf.model.json.JsonProductSummary;
import com.hcl.atf.taf.model.json.JsonProductTeamResources;
import com.hcl.atf.taf.model.json.JsonProductUserRole;
import com.hcl.atf.taf.model.json.JsonProductVersionListMaster;
import com.hcl.atf.taf.model.json.JsonResourceDailyPerformance;
import com.hcl.atf.taf.model.json.JsonResourceExperienceSummary;
import com.hcl.atf.taf.model.json.JsonRiskHazardTraceabilityMatrix;
import com.hcl.atf.taf.model.json.JsonRunConfiguration;
import com.hcl.atf.taf.model.json.JsonTestCaseExecutionResult;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.JsonTestCaseProductVersionMapping;
import com.hcl.atf.taf.model.json.JsonTestExecutionResultBugList;
import com.hcl.atf.taf.model.json.JsonTestFactoryProductCoreResource;
import com.hcl.atf.taf.model.json.JsonTestRunPlan;
import com.hcl.atf.taf.model.json.JsonTestRunPlanGroup;
import com.hcl.atf.taf.model.json.JsonTestRunPlanGroupHasTestRunPlan;
import com.hcl.atf.taf.model.json.JsonTestSuiteList;
import com.hcl.atf.taf.model.json.JsonUserRoleMaster;
import com.hcl.atf.taf.model.json.JsonWorkPackage;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.BuildMongoDAO;
import com.hcl.atf.taf.mongodb.dao.FeaturesMongoDAO;
import com.hcl.atf.taf.mongodb.dao.ProductMasterMongoDAO;
import com.hcl.atf.taf.mongodb.dao.ProductVersionMasterMongoDAO;
import com.hcl.atf.taf.mongodb.dao.TestBedsMongoDAO;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.scheduler.TAFScheduleManager;
import com.hcl.atf.taf.scheduler.TestConfigScheduleEntity;
import com.hcl.atf.taf.service.ActivityTaskService;
import com.hcl.atf.taf.service.ActivityTypeService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.AttachmentService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.CustomerService;
import com.hcl.atf.taf.service.DataTreeService;
import com.hcl.atf.taf.service.DecouplingCategoryService;
import com.hcl.atf.taf.service.DeviceListService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.HierarchicalEntitiesService;
import com.hcl.atf.taf.service.HostListService;
import com.hcl.atf.taf.service.NotificationService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestCaseToVersionMappingService;
import com.hcl.atf.taf.service.TestExecutionBugsService;
import com.hcl.atf.taf.service.TestFactoryManagementService;
import com.hcl.atf.taf.service.TestManagementService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.service.ConfigurationWorkFlowService;
import com.hcl.ilcm.workflow.service.WorkflowEventService;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;
import com.hcl.ilcm.workflow.service.WorkflowStatusService;
@Controller
public class ProductManagementController {
	private static final Log log = LogFactory.getLog(ProductManagementController.class);
	
	@Autowired
	private ProductListService productListService; 
	@Autowired
	private CommonService commonService;
	@Autowired
	private DeviceListService deviceListService;
	@Autowired
	private DataTreeService dataTreeService;
	@Autowired
	private UserListService userListService;
	@Autowired
	private TestCaseService testCaseService;
	@Autowired
	private TestFactoryManagementService testFactoryManagementService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private EnvironmentService environmentService;
	@Autowired
	private ProductTestEnvironmentDAO productTestEnvironmentDAO;
	@Autowired
	private WorkPackageService workPackageService; 
	@Autowired
	private ExecutionTypeMasterService executionTypeMasterService; 
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService; 
	@Autowired
	private HostListService hostListService; 
	@Autowired
	private TestRunConfigurationService testRunConfigSer;
	@Autowired
	private ProductMasterMongoDAO productMasterMongoDAO;
	@Autowired
	private ProductVersionMasterMongoDAO productVersionMasterMongoDAO;
	@Autowired
	private BuildMongoDAO buildMongoDAO;
	@Autowired
	private FeaturesMongoDAO featuresMongoDAO;
	@Autowired
	private DecouplingCategoryService decouplingCategoryService;
	@Autowired
	private TestExecutionBugsService testExecutionBugsService;
	@Autowired
	private ExcelTestDataIntegrator excelTestDataIntegrator;
	@Autowired
	private MongoDBService mongoDBService;	
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private ActivityTaskService activityTaskService;
	@Autowired
	private ActivityWorkPackageService activityWorkPackageService;
	@Autowired
	private ActivityTypeService activityTypeService;
	@Autowired
	private HierarchicalEntitiesService hierarchicalEntitiesService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TestManagementService testManagementService; 
	
	@Value("#{ilcmProps['MONGODB_AVAILABLE']}")
    private String MONGODB_AVAILABLE;

	@Autowired
	private TestBedsMongoDAO testBedsMongoDAO;
	
	@Autowired
	private ProductBuildDAO productBuildDAO;
	
	@Autowired
	private ProductVersionListMasterDAO productVersionListMasterDAO;
	
	@Autowired
	private TestCaseToVersionMappingService testCaseToVersionMappingService;
	
	@Autowired
	private ProductFeatureDAO productFeatureDAO;
	
	@Autowired
	private ProductBuildDAO productBuildDao;
	
	@Autowired
	private WorkflowStatusService workflowStatusService;
	
	@Autowired
	private WorkflowStatusPolicyService workflowStatusPolicyService;
	
	@Autowired
	private ConfigurationWorkFlowService configurationWorkFlowService;
	
	@Autowired
	private WorkflowEventService workflowEventService;
	
	private String trimmingSuite;
	
	@Value("#{ilcmProps['GENERATED_TEST_SCRIPTS_DESTINATION_FOLDER']}")
    private String testScriptsDestinationDirectory;
	
	@RequestMapping(value="administration.product.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listProducts(HttpServletRequest request, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("inside administration.product.list");
		JTableResponse jTableResponse;
		try {
			List<ProductMaster> productMaster = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			int  userRoleId = user.getUserRoleMaster().getUserRoleId();
			List<TestFactory> listOfTestFactories = null;
			List<ProductMaster> listOfProducts = null;
			if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER){
				productMaster = productListService.getProductsByProductUserRoleForUserId(userRoleId, user.getUserId(),0);
			}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
				listOfTestFactories = testFactoryManagementService.getTestFactoriesByTestFactoryManagerId(user.getUserId());
				 if(listOfTestFactories != null && listOfTestFactories.size()>0){
					 listOfProducts = new ArrayList<ProductMaster>();
					 for (TestFactory testFactory : listOfTestFactories) {
						List<ProductMaster> subListOfProducts = null;
						subListOfProducts = productListService.getProductsByTestFactoryId(testFactory.getTestFactoryId());
						if(listOfProducts.size() == 0){
							listOfProducts = subListOfProducts;
						}else{
							listOfProducts.addAll(subListOfProducts);
						}
					}
					productMaster = listOfProducts;
				 }
			}else{
				productMaster=productListService.listProduct(jtStartIndex,jtPageSize);
			}
			List<JsonProductMaster> jsonProductMaster=new ArrayList<JsonProductMaster>();
			if(productMaster != null && productMaster.size()>0){
				for(ProductMaster pm: productMaster){
					jsonProductMaster.add(new JsonProductMaster(pm));
				}
			}
            jTableResponse = new JTableResponse("OK", jsonProductMaster,productListService.getTotalRecordsOfProduct());     
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Products!");
	            log.error("JSON ERROR listing Products", e);
	        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.customer.product.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listProductsbyCustomerID(HttpServletRequest request, @RequestParam int customerId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("administration.customer.product.list");
		JTableResponse jTableResponse;
		
		try {
			List<ProductMaster> productMaster = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			int  userRoleId = user.getUserRoleMaster().getUserRoleId();
			List<TestFactory> listOfTestFactories = null;
			List<ProductMaster> listOfProducts = null;
			if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER){
				productMaster = productListService.getProductsByProductUserRoleForUserId(userRoleId, user.getUserId(),0);
			}else if(userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER){
				listOfTestFactories = testFactoryManagementService.getTestFactoriesByTestFactoryManagerId(user.getUserId());
				 if(listOfTestFactories != null && listOfTestFactories.size()>0){
					 listOfProducts = new ArrayList<ProductMaster>();
					 for (TestFactory testFactory : listOfTestFactories) {
						List<ProductMaster> subListOfProducts = null;
						subListOfProducts =	productListService.listProductsbyCustomerIdTestFactoryId(testFactory.getTestFactoryId(),customerId,jtStartIndex,jtPageSize);
						if(listOfProducts.size() == 0){
							listOfProducts = subListOfProducts;
						}else{
							listOfProducts.addAll(subListOfProducts);
						}
					}
					productMaster = listOfProducts;
				 }
			}else{
				productMaster=productListService.listProductsbyCustomerID(customerId,jtStartIndex,jtPageSize);
			}
			List<JsonProductMaster> jsonProductMaster=new ArrayList<JsonProductMaster>();
			if(productMaster != null && productMaster.size()>0){
				for(ProductMaster pm: productMaster){
					jsonProductMaster.add(new JsonProductMaster(pm));
				}
			}
            jTableResponse = new JTableResponse("OK", jsonProductMaster,productListService.getTotalRecordsOfProduct());     
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Products!");
	            log.error("JSON ERROR listing Products by CustomerID", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.customer.product.list.options",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listProductsbyCustomerIDOptions(HttpServletRequest request, @RequestParam int customerId) {
		log.debug("administration.customer.product.list");
		JTableResponseOptions jTableResponseOptions;
		
		try {
			List<ProductMaster> productMaster = null;
			productMaster=productListService.listProductsbyCustomerID(customerId,null, null);
			List<JsonProductMaster> jsonProductMaster=new ArrayList<JsonProductMaster>();
			if(productMaster != null && productMaster.size()>0){
				for(ProductMaster pm: productMaster){
					pm.setProductMode(null);
					pm.setProductType(null);
					jsonProductMaster.add(new JsonProductMaster(pm));
				}
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductMaster);     
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching Products!");
	            log.error("JSON ERROR listing Products by CustomerIDOptions ", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.product.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addProduct(HttpServletRequest request,@ModelAttribute JsonProductMaster jsonProductMaster, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		ProductUserRole productUserRole=new ProductUserRole();
		try {
			int userId;
			int roleId;
			int productId;
				ProductMaster productMaster= jsonProductMaster.getProductMaster();		
				String errorMessage = ValidationUtility.validateForNewProductAddition(productMaster.getProductName().trim(), productListService);
				if (errorMessage!= null) {
					Integer testFactoryId=	productMaster.getTestFactory().getTestFactoryId();
					String productName=productMaster.getProductName();
					boolean errorFlagTestFactory=productListService.isProductExitsInsameTestFactory(testFactoryId,productName);
					if(errorFlagTestFactory){
						jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
						return jTableSingleResponse;
					}
					
				}		
				TestFactory testFactory=testFactoryManagementService.getTestFactoryById(jsonProductMaster.getTestFactoryId());
				Customer custmer=customerService.getCustomerId(jsonProductMaster.getCustomerId());
				ProductType prodType=productListService.getProductTypeById(productMaster.getProductType().getProductTypeId());
				ProductMode prodMode=	productListService.getProductModeById(productMaster.getProductMode().getModeId());
				//end
				jsonProductMaster.setTestFactoryName(testFactory.getTestFactoryName());
				jsonProductMaster.setCustomerName(custmer.getCustomerName());
				jsonProductMaster.setTypeName(prodType.getTypeName());
				jsonProductMaster.setModeName(prodMode.getModeName());
				productMaster.setProductType(prodType);
				productMaster.setProductMode(prodMode);
				productListService.addProduct(productMaster);
				
				if(productMaster != null && productMaster.getProductId() != null){
					mongoDBService.addProductToMongoDB(productMaster.getProductId());
				}
				
				
				UserList user = (UserList)request.getSession().getAttribute("USER");
				userId=user.getUserId();
				UserList userObj = userListService.getUserListById(userId);
				roleId=userObj.getUserRoleMaster().getUserRoleId();
				String roleName = userObj.getUserRoleMaster().getRoleName();
				if(productMaster != null && productMaster.getProductId() != null ){						
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_PRODUCT, productMaster.getProductId(), productMaster.getProductName(), userObj);
					try{
						ActivityTaskType activityTaskType = new ActivityTaskType();
						activityTaskType.setActivityTaskTypeName(productMaster.getProductName()+"-TaskType");
						activityTaskType.setActivityTaskTypeDescription(productMaster.getProductName()+"-TaskType");
						activityTaskType.setActivityTaskTypeWeightage(1.0F);
						activityTaskType.setProduct(productMaster);
						activityTaskType.setTestFactory(testFactory);
						activityTaskService.addActivityTaskType(activityTaskType);
					}catch(Exception ex){
						log.error("Error while creating Task Type for product - ", ex);
					}
					try{
						ActivityMaster activityMaster = new ActivityMaster();
						activityMaster.setActivityMasterName(productMaster.getProductName()+"-ActivityType");
						activityMaster.setDescription(productMaster.getProductName()+"-ActivityType");
						activityMaster.setProductMaster(productMaster);
						activityMaster.setTestFactory(testFactory);
						ActivityType activityType = new ActivityType();
						activityType.setActivityTypeId(1);
						activityMaster.setActivityType(activityType);
						activityMaster.setWeightage(1.0F);
						activityTypeService.addActivityMaster(activityMaster);
					}catch(Exception ex){
						log.error("Error while creating Activity Type for product - ", ex);
					}
					
					try {
						notificationService.prepareNotificationMaster(user);
						notificationService.prepareNotificationPolicy(productMaster.getProductId());
					}catch(Exception e) {
						log.error("Error while creation notification intial data setup");
					}
					
				}
				
					if(roleId == IDPAConstants.ROLE_ID_TEST_MANAGER || roleId == IDPAConstants.ROLE_ID_TEST_LEAD){
					ProductMaster product=productListService.getProductByName(productMaster.getProductName());
						 productId=product.getProductId();
						boolean errorFlag=productListService.isProductUserRoleExits(productId,userId,roleId);
						log.debug(errorFlag);

						if (errorFlag) {
							String msg="User "+ userObj.getLoginId()+" already mapped to the product "+ product.getProductName() ;
							jTableSingleResponse = new JTableSingleResponse("ERROR",msg);
							return jTableSingleResponse;
						}
						productUserRole.setUser(userObj);
						productUserRole.setProduct(product);
						if(userObj.getUserRoleMaster()!=null){
						productUserRole.setRole(userObj.getUserRoleMaster());
						}
							productListService.addProductUserRole(productUserRole);
						
				}
					if(productMaster.getTestFactory()!=null){
						Integer testFactoryId=productMaster.getTestFactory().getTestFactoryId();
						List<TestFactoryManager> tfManagerList=testFactoryManagementService.listTestFactoryManager(testFactoryId,TAFConstants.ENTITY_STATUS_ACTIVE);
						if(tfManagerList !=null && tfManagerList.size() != 0){
							for(TestFactoryManager testManager:tfManagerList){
								if(testManager.getUserList()!=null){
									UserList manager = userListService.getUserListById(testManager.getUserList().getUserId());
									productUserRole.setUser(manager);
									productUserRole.setProduct(productMaster);
									if(manager.getUserRoleMaster()!=null){
									productUserRole.setRole(manager.getUserRoleMaster());
									}
									
									boolean errorFlag=productListService.isProductUserRoleExits(productMaster.getProductId(),manager.getUserId(),manager.getUserRoleMaster().getUserRoleId());
									if (!errorFlag) {
										productListService.addProductUserRole(productUserRole);
									}
								}
							}
							
						}
					}
					
					JsonProductMaster jsonProd = new JsonProductMaster(productMaster);
					jsonProd.setTestFactoryName(testFactory.getTestFactoryName());
					jsonProd.setCustomerName(custmer.getCustomerName());
					
				jTableSingleResponse = new JTableSingleResponse("OK",jsonProd);		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding Product!");
	            log.error("JSON ERROR adding Product", e);
	        }
	        
        return jTableSingleResponse;
    }

	@RequestMapping(value="administration.customer.product.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addProductbyCustomerID(HttpServletRequest request, @ModelAttribute JsonProductMaster jsonProductMaster,  BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		ProductUserRole productUserRole=new ProductUserRole();
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {
			int userId;
			int roleId;
			int productId;
				ProductMaster productMaster= jsonProductMaster.getProductMaster();		
				String errorMessage = ValidationUtility.validateForNewProductAddition(productMaster.getProductName().trim(), productListService);
				if (errorMessage!= null) {
					
					jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
					return jTableSingleResponse;
				}		
				productListService.addProduct(productMaster);
					UserList user = (UserList)request.getSession().getAttribute("USER");
					userId=user.getUserId();
					UserList userObj = userListService.getUserListById(userId);
					roleId=userObj.getUserRoleMaster().getUserRoleId();
					String roleName = userObj.getUserRoleMaster().getRoleName();
					if(roleName.equalsIgnoreCase("TestManager")){
					ProductMaster product=productListService.getProductByName(productMaster.getProductName());
						 productId=product.getProductId();
						boolean errorFlag=productListService.isProductUserRoleExits(productId,userId,roleId);
						log.error(errorFlag);

						if (errorFlag) {
							String msg="User "+ userObj.getLoginId()+" already mapped to the product  "+ product.getProductName() ;
							jTableSingleResponse = new JTableSingleResponse("ERROR",msg);
							return jTableSingleResponse;
						}
						productUserRole.setUser(userObj);
						productUserRole.setProduct(product);
						if(userObj.getUserRoleMaster()!=null){
						productUserRole.setRole(userObj.getUserRoleMaster());
						}
							productListService.addProductUserRole(productUserRole);
						
				}
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonProductMaster(productMaster));		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding Product!");
	            log.error("JSON ERROR add Product by CustomerID", e);
	        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="administration.product.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteProduct(@RequestParam int productId) {
		JTableResponse jTableResponse;
		try {
	            productListService.deleteProduct(productId);
	            jTableResponse = new JTableResponse("OK");
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Not able to delete the product. Check if any versions of the product exist and delete them first.");
	            log.error("JSON ERROR deleting Product", e);
	        }
  //      jTableResponse = new JTableResponse("ERROR","Deleting Product not supported currently.");
		return jTableResponse;
    }

	@RequestMapping(value="administration.product.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateProduct(HttpServletRequest request, @ModelAttribute JsonProductMaster jsonProductMaster, BindingResult result) {
		JTableResponse jTableResponse = null;
		ProductMaster productFromUI = null;
		ProductMaster productFromDB = null;
		TestFactory testFactory = null;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}		
		try {			
				productFromUI = jsonProductMaster.getProductMaster();				
				productFromDB = productListService.getProductDetailsByIdWithDevicesHostList(productFromUI.getProductId());
				testFactory = productFromDB.getTestFactory();
				
				if(productFromDB.getGenericeDevices() != null && !productFromDB.getGenericeDevices().isEmpty()){
					Set<GenericDevices> gdSet = productFromDB.getGenericeDevices();
					productFromUI.setGenericeDevices(gdSet);
				}
				if(productFromDB.getHostLists() != null && !productFromDB.getHostLists().isEmpty()){
					Set<HostList> hostSet = productFromDB.getHostLists();
					productFromUI.setHostLists(hostSet);
				}
				//following two for while updating if the productName is same and testfactory is different means need to save.
				Integer testFactoryId=	productFromUI.getTestFactory().getTestFactoryId();
				String productName=productFromUI.getProductName();
				boolean errorFlagTestFactory=productListService.isProductExitsInsameTestFactory(testFactoryId,productName);
				
				if(jsonProductMaster.getModifiedField().equalsIgnoreCase(MongodbConstants.MODIFIED_FIELD_STATUS)){
					mongoDBService.updateParentStatusInChildColletions(IDPAConstants.PRODUCT_ENTITY_MASTER_ID,jsonProductMaster.getProductId(),jsonProductMaster.getStatus());
				}
				UserList userObj = (UserList)request.getSession().getAttribute("USER");
				remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productFromDB.getProductName();
				List<JsonProductMaster> jsonprod = new ArrayList<JsonProductMaster>();
				if (productFromUI.getProductName() != null && productFromDB.getProductName() != null){
					if(productFromUI.getProductName().trim().equals(productFromDB.getProductName().trim())){
						List<ProductMaster> products = updateProduct(productFromUI);						
						eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_PRODUCT, productFromUI.getProductId(), productFromUI.getProductName(),
								jsonProductMaster.getModifiedField(), jsonProductMaster.getModifiedFieldTitle(),
								jsonProductMaster.getOldFieldValue(), jsonProductMaster.getModifiedFieldValue(), userObj, remarks);
						
						
						for (ProductMaster productMaster : products) {
							jsonprod.add(new JsonProductMaster(productMaster));
						}
						jTableResponse = new JTableResponse("OK",jsonprod,1);
					}
					else {
						if(errorFlagTestFactory){
							jTableResponse = new JTableResponse("INFORMATION","Product Name Already Exists For Same TestFactory!");
						}						
						else
						{
							List<ProductMaster> products = updateProduct(productFromUI);
							eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_PRODUCT, productFromUI.getProductId(), productFromUI.getProductName(),
									jsonProductMaster.getModifiedField(), jsonProductMaster.getModifiedFieldTitle(),
									jsonProductMaster.getOldFieldValue(), jsonProductMaster.getModifiedFieldValue(), userObj, remarks);
							jsonprod.add(jsonProductMaster);
							jTableResponse = new JTableResponse("OK",jsonprod,1);
						}
					}
					
					try {
						notificationService.prepareNotificationMaster(userObj);
						notificationService.prepareNotificationPolicy(productFromUI.getProductId());
					}catch(Exception e) {
						log.error("Error while creation notification intial data setup");
					}
				}
		    } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to update the Product!");
	            log.error("JSON ERROR updating Product", e);
	        }
        return jTableResponse;
    }
	
		
		
		@RequestMapping(value="administration.product.version.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listProductVersions(@RequestParam int productId) {
			log.debug("inside administration.product.version.list");
			JTableResponse jTableResponse;
			try {
				List<ProductVersionListMaster> productVersionListMaster=productListService.listProductVersions(productId, TAFConstants.ENTITY_STATUS_ACTIVE);
				
				List<JsonProductVersionListMaster> jsonProductVersionListMaster=new ArrayList<JsonProductVersionListMaster>();
				for(ProductVersionListMaster pvm: productVersionListMaster){
					
					jsonProductVersionListMaster.add(new JsonProductVersionListMaster(pvm));
				}
	            jTableResponse = new JTableResponse("OK", jsonProductVersionListMaster,jsonProductVersionListMaster.size());     
				productVersionListMaster = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Product Versions!");
		            log.error("JSON ERROR listing Product Versions", e);
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="administration.product.version.listByGet",method=RequestMethod.GET ,produces="application/json")
	    public @ResponseBody JTableResponse listProductVersionsByGet(@RequestParam int productId) {
			log.debug("inside administration.product.version.listByGet");
			JTableResponse jTableResponse;
			try {
				List<ProductVersionListMaster> productVersionListMaster=productListService.listProductVersions(productId, TAFConstants.ENTITY_STATUS_ACTIVE);
				List<JsonProductVersionListMaster> jsonProductVersionListMaster=new ArrayList<JsonProductVersionListMaster>();
				for(ProductVersionListMaster pvm: productVersionListMaster){
					
					jsonProductVersionListMaster.add(new JsonProductVersionListMaster(pvm));
				}
	            jTableResponse = new JTableResponse("OK", jsonProductVersionListMaster,jsonProductVersionListMaster.size());     
				productVersionListMaster = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Product Versions!");
		            log.error("JSON ERROR listing ProductVersions", e);
		        }
		        
	        return jTableResponse;
	    }		
		
		@RequestMapping(value="get.prodcutType.by.versionIdProdIdWorkPackageId",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getProductTypeByVersionIdProdIdWorkPackageId(@RequestParam int productVersionId, @RequestParam int productId, @RequestParam int workpackageId) {
			log.debug("Inside getProductTypeByVersionId ");
			String ffinalResult="";
			JSONArray jproductType = new JSONArray();
			int productType = 0;
			try {   
				if(workpackageId != -1){
					productType = productListService.getProductTypeByVersionId(productVersionId, productId, workpackageId);	
				}else{
					productType = productListService.getProductTypeByVersionId(productVersionId, productId, workpackageId);	
				}
								
				JSONObject jobj = new JSONObject();
				jobj.put("productType", productType);
				jproductType.add(jobj);				
						ffinalResult=jproductType.toString();						
						if (ffinalResult == null) {		
							log.info("No ProductType Id");							
							return "No Data";
						}
		        } catch (Exception e) {
		            log.error("JSON ERROR getting ProductType By VersionIdProdIdWorkPackageId", e);
		        }		        
	        return  ffinalResult;
	    }		
		
		@RequestMapping(value="administration.product.version.add",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addProductVersion(HttpServletRequest request, @ModelAttribute JsonProductVersionListMaster jsonProductVersionListMaster, BindingResult result) {
			JTableSingleResponse jTableSingleResponse;
			log.info("adding ProductVersion ");
			if(result.hasErrors()){				
				jTableSingleResponse =jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
			}			
			try {
					ProductVersionListMaster productVersionListMaster = jsonProductVersionListMaster.getProductVersionListMaster();
					
					String errorMessage=commonService.duplicateName(productVersionListMaster.getProductVersionName(), "product_version_list_master", "productVersionName", "Product Version", "productId="+jsonProductVersionListMaster.getProductId());
					if (errorMessage != null) {
						
						jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
						return jTableSingleResponse;
					}

					productListService.addProductVersion(productVersionListMaster);
					mongoDBService.addProductVersionToMongoDB(productVersionListMaster.getProductVersionListId());
					UserList userObj = (UserList)request.getSession().getAttribute("USER");
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_PRODUCT_VERSION, productVersionListMaster.getProductVersionListId(), productVersionListMaster.getProductVersionName(), userObj);
		            jTableSingleResponse = new JTableSingleResponse("OK",new JsonProductVersionListMaster(productVersionListMaster));
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Product Version!");
		            log.error("JSON ERROR adding ProductVersion", e);
		        }		        
	        return jTableSingleResponse;			
	    }
		
		@RequestMapping(value="administration.product.version.delete",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse deleteProductVersion(@RequestParam int productVersionListId) {
			log.debug("Deleting Product Version ");
			JTableResponse jTableResponse;
			try {
		            productListService.deleteProductVersion(productVersionListId);
		            jTableResponse = new JTableResponse("OK");
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error deleting Product Version!");
		            log.error("JSON ERROR deleting ProductVersion", e);
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="administration.product.version.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateProductVersion(HttpServletRequest request, @ModelAttribute JsonProductVersionListMaster jsonProductVersionListMaster, BindingResult result) {
			log.debug("Updating Version");
			JTableResponse jTableResponse;
			TestFactory testFactory = null;
			ProductMaster productMaster = null;
			String remarks = "";
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}			
			try {
				String date=jsonProductVersionListMaster.getReleaseDate();
				log.info("date"+date);				
					ProductVersionListMaster productVersionListMasterFromDB = productListService.getProductVersionListMasterById(jsonProductVersionListMaster.getProductVersionListId());
					ProductVersionListMaster productVersionListMasterFromUI = jsonProductVersionListMaster.getProductVersionListMaster();
					productMaster = productVersionListMasterFromDB.getProductMaster();
					testFactory = productMaster.getTestFactory();
					String errorMessage = ValidationUtility.validateForNewProductVersionAddition(productVersionListMasterFromUI, productListService, "update");
					if (errorMessage != null) {						
						jTableResponse = new JTableResponse("ERROR",errorMessage);
						return jTableResponse;
					}
					
					log.info("added date"+productVersionListMasterFromUI.getReleaseDate());
					productListService.updateProductVersion(productVersionListMasterFromUI);
					List<JsonProductVersionListMaster> tmpList =new ArrayList();
					tmpList.add(jsonProductVersionListMaster);
					
					if(jsonProductVersionListMaster.getModifiedField().equalsIgnoreCase(MongodbConstants.MODIFIED_FIELD_STATUS)){
						mongoDBService.updateParentStatusInChildColletions(IDPAConstants.PRODUCT_VERSION_ENTITY_MASTER_ID,jsonProductVersionListMaster.getProductVersionListId(),jsonProductVersionListMaster.getStatus());
					}
					mongoDBService.addProductVersionToMongoDB(productVersionListMasterFromUI.getProductVersionListId());
					
					UserList userObj = (UserList)request.getSession().getAttribute("USER");
					remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", ProductVersion :"+productVersionListMasterFromDB.getProductVersionName();
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_PRODUCT_VERSION,  productVersionListMasterFromDB.getProductVersionListId(), productVersionListMasterFromUI.getProductVersionName(),
							jsonProductVersionListMaster.getModifiedField(), jsonProductVersionListMaster.getModifiedFieldTitle(),
							jsonProductVersionListMaster.getOldFieldValue(), jsonProductVersionListMaster.getModifiedFieldValue(), userObj, remarks);
		            jTableResponse = new JTableResponse("OK",tmpList ,1);
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating Product Version!");
		            log.error("JSON ERROR updating ProductVersion", e);
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="administration.product.version.get",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse getProductVersionListMaster(@RequestParam int productVersionListId) {
			log.debug("Fetching Product Version List");
			JTableSingleResponse jTSingleResponse;			
			try {			
					ProductVersionListMaster productVersionListMaster = productListService.getProductVersionListMasterById(productVersionListId);
					jTSingleResponse = new JTableSingleResponse("OK",new JsonProductVersionListMaster(productVersionListMaster));				
					
		        } catch (Exception e) {
		        	jTSingleResponse = new JTableSingleResponse("ERROR","Unable to get Product Versions!");
		            log.error("JSON ERROR getting ProductVersionListMaster", e);
		        }		        
	        return jTSingleResponse;
	    }	
		
		@RequestMapping(value="administration.product.build.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listProductBuild(@RequestParam int productVersionListId) {	
			log.debug("Listing Product Build administration.product.build.list");
			JTableResponse jTableResponse;			 
			try {				 
				List<ProductBuild> productBuildbyStatus=productListService.listProductBuilds(productVersionListId, TAFConstants.ENTITY_STATUS_ACTIVE);				 
				List<JsonProductBuild> jsonProductBuild=new ArrayList<JsonProductBuild>();
//				
				for(ProductBuild pbuild: productBuildbyStatus){					
					jsonProductBuild.add(new JsonProductBuild(pbuild));					
				}				
				jTableResponse = new JTableResponse("OK", jsonProductBuild,jsonProductBuild.size());     
				productBuildbyStatus = null;
				
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Product Builds!");
		            log.error("JSON ERROR listing ProductBuild", e);
		        }		        
	        return jTableResponse;
	    }
	
		@RequestMapping(value="administration.product.build.add",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addProductBuild(HttpServletRequest req, @ModelAttribute JsonProductBuild jsonProductBuild, BindingResult result) {  
			JTableSingleResponse jTableSingleResponse;
			UserList user = new UserList();
			log.debug("Adding Build");
			if(result.hasErrors()){
				jTableSingleResponse =jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
			}			
			try {
			user = (UserList)req.getSession().getAttribute("USER");
			ProductBuild productBuild = jsonProductBuild.getProductBuild();
			ProductVersionListMaster productVersion = productListService.getProductVersionListMasterById(jsonProductBuild.getProductVersionListId());
			productBuild.setProductVersion(productVersion);
			DefectIdentificationStageMaster buildType = testExecutionBugsService.getDefectIdentificationStageMasterById(jsonProductBuild.getBuildTypeId());
			productBuild.setBuildType(buildType);
			ProductMaster productMaster = productVersion.getProductMaster();
			productBuild.setProductMaster(productMaster);
			
			String errorMessage=commonService.duplicateName(productBuild.getBuildname(), "product_build", "buildname", "Product Build", "productId="+productVersion.getProductMaster().getProductId());
				if (errorMessage != null) {
					
					jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
					return jTableSingleResponse;
				}
			productListService.addProductBuild(productBuild);	
			mongoDBService.addProductBuildToMongoDB(productBuild.getProductBuildId());			
			if(productBuild != null && productBuild.getProductBuildId() != null){
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_PRODUCT_BUILD, productBuild.getProductBuildId(), productBuild.getBuildname(), user);
			}	
		         jTableSingleResponse = new JTableSingleResponse("OK",new JsonProductBuild(productBuild));
		    } catch (Exception e) {
		         jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding Product Build!");
		         log.error("JSON ERROR adding ProductBuild", e);
		    }		        
	        return jTableSingleResponse;			
	    }
		
		@RequestMapping(value="administration.product.build.delete",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse deleteProductBuild(@RequestParam int productBuildId) {
			log.debug("Deleting product build:"+productBuildId);
			JTableResponse jTableResponse;
			try {
		            productListService.deleteProductBuild(productBuildId);
		            jTableResponse = new JTableResponse("OK");
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error deleting Product Build!");
		            log.error("JSON ERROR deleting ProductBuild", e);
		        }		        
	        return jTableResponse;
	    }
		
		@RequestMapping(value="administration.product.build.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateProductBuild(HttpServletRequest request, @ModelAttribute JsonProductBuild jsonProductBuild, BindingResult result) {
			log.debug("Updating Product Build");
			JTableResponse jTableResponse;
			TestFactory testFactory = null;
			ProductMaster productMaster = null;
			ProductVersionListMaster productVersion = null;
			String remarks = "";
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}			
			try {
				ProductBuild productBuildFromDB=productListService.getProductBuildById(jsonProductBuild.getProductBuildId(), 0);
				ProductBuild productBuildFromUI = jsonProductBuild.getProductBuild();	
				productVersion = productBuildFromDB.getProductVersion();
				productMaster = productVersion.getProductMaster();
				testFactory = productMaster.getTestFactory();
				
				log.info("ProductBuildFromDb"+productBuildFromDB.getProductVersion().getProductVersionListId());
				ProductVersionListMaster productVersionListMaster=new ProductVersionListMaster();
				productVersionListMaster.setProductVersionListId(productBuildFromDB.getProductVersion().getProductVersionListId());
				productBuildFromUI.setProductVersion(productVersionListMaster);
					
				DefectIdentificationStageMaster buildType = testExecutionBugsService.getDefectIdentificationStageMasterById(jsonProductBuild.getBuildTypeId());
				productBuildFromUI.setBuildType(buildType);
				if(productBuildFromUI.getBuildNo() != null){//Bug 2024 /To handle if Build No is too long.
					String buildNo = productBuildFromUI.getBuildNo();
					if(buildNo.length() >=45){
						jTableResponse = new JTableResponse("ERROR","Error updating Product Build. Build No is too long!");			            
					}
				}
							
					productListService.updateProductBuild(productBuildFromUI);
					if(jsonProductBuild.getModifiedField().equalsIgnoreCase(MongodbConstants.MODIFIED_FIELD_STATUS)){
						mongoDBService.updateParentStatusInChildColletions(IDPAConstants.PRODUCT_BUILD_ENTITY_MASTER_ID,jsonProductBuild.getProductBuildId(),jsonProductBuild.getStatus());
					}
					mongoDBService.addProductBuildToMongoDB(productBuildFromUI.getProductBuildId());
					
					//Entity Audition History //Update
					UserList userObj = (UserList)request.getSession().getAttribute("USER");
					remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", ProductVersion :"+productVersion.getProductVersionName()+", ProductBuild :"+productBuildFromDB.getBuildname();
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_PRODUCT_BUILD,  productBuildFromUI.getProductBuildId(), productBuildFromUI.getBuildname(),
							jsonProductBuild.getModifiedField(), jsonProductBuild.getModifiedFieldTitle(),
							jsonProductBuild.getOldFieldValue(), jsonProductBuild.getModifiedFieldValue(), userObj, remarks);
					
					List<JsonProductBuild> tmpList =new ArrayList();
					tmpList.add(jsonProductBuild);
		            jTableResponse = new JTableResponse("OK",tmpList ,1);            
				
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating Product Build!");
		            log.error("JSON ERROR updating ProductBuild", e);
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="administration.product.build.get",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse getProductBuild(@RequestParam int productBuildId) {
			JTableSingleResponse jTSingleResponse;
			log.info("Getting Product Build:"+productBuildId);
			try {						
				ProductBuild productBuild = productListService.getProductBuildById(productBuildId, 0);				
				jTSingleResponse = new JTableSingleResponse("OK",new JsonProductBuild(productBuild));	
				
		        } catch (Exception e) {
		        	jTSingleResponse = new JTableSingleResponse("ERROR");
		            log.error("JSON ERROR getting ProductBuild", e);	            
		        }		        
	        return jTSingleResponse;
	    }
		
		@RequestMapping(value="administration.product.environment.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listEnvironment(@RequestParam int productMasterId, @RequestParam Integer envstatus) {
			log.debug("Listing Environment");
			JTableResponse jTableResponse;			 
			try {
				List<Environment> environments=productListService.getEnvironmentListByProductIdAndStatus(productMasterId,envstatus);
				
				List<JsonEnvironment> jsonEnvironments=new ArrayList<JsonEnvironment>();
				for(Environment environ: environments){
					jsonEnvironments.add(new JsonEnvironment(environ));					
				}				
				jTableResponse = new JTableResponse("OK", jsonEnvironments,jsonEnvironments.size());     
				environments = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Product Test Environments!");
		            log.error("JSON ERROR listing Environment", e);
		        }		        
	        return jTableResponse;
	    }
	@RequestMapping(value="administration.product.environment.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addEnvironment(HttpServletRequest request, @ModelAttribute JsonEnvironment jsonEnvironment, BindingResult result) {
		log.debug("Adding Environment");
		JTableSingleResponse jTableSingleResponse=null;			
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {				
			Environment environment = jsonEnvironment.getEnvironment();			
			EnvironmentCategory environmentCategory =environmentService.getEnvironmentCategoryById(jsonEnvironment.getEnvironmentCategoryId());
			EnvironmentGroup environmentGroup = environmentService.getEnvironmentGroupById(jsonEnvironment.getEnvironmentGroupId());
			if(environmentGroup!=null && environmentCategory!= null){
				environmentCategory.setEnvironmentGroup(environmentGroup);
			}
			if(environmentCategory!=null){
				environment.setEnvironmentCategory(environmentCategory);
			}				
			String errorMessage=commonService.duplicateName(environment.getEnvironmentName(), "Environment", "environmentName", "Environment", "productMaster.productId="+environment.getProductMaster().getProductId());
			if (errorMessage != null) {
				
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			productListService.addProductEnvironment(environment);				

			UserList user = (UserList)request.getSession().getAttribute("USER");
			int userId=user.getUserId();
			UserList userObj = userListService.getUserListById(userId);
									
			EntityMaster entityMaster = workPackageService.getEntityMasterByName(IDPAConstants.ENTITY_ENVIRONMENT);
			eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ENVIRONMENT, environment.getEnvironmentId(), environment.getEnvironmentName(), userObj);	
			ProductMaster product = productListService.getProductById(jsonEnvironment.getProductMasterId());
			environmentService.createEnvironmentCombinationsForNewEnvironment(product, environment, jsonEnvironment.getEnvironmentGroupId());
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonEnvironment(environment));				
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Environment!");
	        log.error("JSON ERROR adding Environment", e);
		}		        
        return jTableSingleResponse;			
	}
	
	@ExceptionHandler(Exception.class)
	public void handleException(Throwable ex, HttpServletResponse response)
			throws Exception {

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				ex.getMessage());
		ex.printStackTrace();
	}
	public List<ProductMaster> updateProduct(ProductMaster productFromUI){
		log.debug("Updating Product");
		List<ProductMaster> products =new ArrayList<ProductMaster>();
		try {
			if (productFromUI.getStatus() == null)
				productFromUI.setStatus(TAFConstants.ENTITY_STATUS_ACTIVE);
				productFromUI.setStatusChangeDate(new Date(System.currentTimeMillis()));
				productFromUI.setProductType(productListService.getProductTypeById(productFromUI.getProductType().getProductTypeId()));
				productListService.updateProduct(productFromUI);
			
				if(productFromUI != null && productFromUI.getProductId() != null){
					mongoDBService.addProductToMongoDB(productFromUI.getProductId());
				}	
			products.add(productFromUI);
		} catch (Exception e) {
			log.error("ERROR updating Product", e);			
		}
		return products;		
	}
	
	public boolean isProductExists(String productName){
		boolean bResult=false;
		try{
			bResult = productListService.isProductExistsByName(productName);
		}catch(Exception exception){
			log.error("JSON ERROR", exception);
		}
		return bResult;
	}
	
	@RequestMapping(value="administration.product.addUserRole",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addProductUserRole(HttpServletRequest req,@ModelAttribute JsonProductUserRole jsonProductUserRole, BindingResult result) {
		log.debug("Mapping User to Product and Role");
		JTableSingleResponse jTableSingleResponse;
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {
			
				UserList loginUser = (UserList)req.getSession().getAttribute("USER");
				ProductUserRole productUserRole= jsonProductUserRole.getProductUserRoleList();
				ProductMaster product = new ProductMaster();
				product.setProductId(jsonProductUserRole.getProductId());
				product.setProductName(jsonProductUserRole.getProductName());
				productUserRole.setProduct(product);
				
				UserList user = userListService.getUserListById(jsonProductUserRole.getUserId());
				productUserRole.setUser(user);
				
				UserRoleMaster role = userListService.getRoleById(jsonProductUserRole.getRoleId());
				productUserRole.setRole(role);
				int productId=productUserRole.getProduct().getProductId();
				int userId=productUserRole.getUser().getUserId();
				int roleId=productUserRole.getRole().getUserRoleId();
				boolean errorMessage=productListService.isProductUserRoleExits(productId,userId,roleId);

				if (errorMessage) {
					
					String msg="User "+ productUserRole.getUser().getLoginId()+" already mapped to the product  "+ productUserRole.getProduct().getProductName() ;
					jTableSingleResponse = new JTableSingleResponse("ERROR",msg);
					return jTableSingleResponse;
				}
					productListService.addProductUserRole(productUserRole);
					
					List<ActivityWorkPackage>activityWorkPackages = activityWorkPackageService.getActivityWorkPackageListByProductId(productId);
					EntityMaster entityMaster =new EntityMaster();
					entityMaster.setEntitymasterid(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID);
					for(ActivityWorkPackage activityWorkPackage : activityWorkPackages){
						EntityUserGroup userGroup = new EntityUserGroup();
						userGroup.setEntityInstanceId(activityWorkPackage.getActivityWorkPackageId());
						userGroup.setMappedBy(loginUser);
						userGroup.setUser(user);
						userGroup.setMappedDate(new Date());
						userGroup.setEntityTypeId(entityMaster);
						userGroup.setProduct(product);
						userListService.mapOrUnmapEntityUserGroup(userGroup,"map");
					}
					
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonProductUserRole(productUserRole));		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding User to Product and Role!");
	            log.error("JSON ERROR adding ProductUserRole", e);
	        }	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="administration.product.listUserRole",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listUserRole(@RequestParam int productId){
		log.debug("inside administration.product.listUserRole");
		JTableResponse jTableResponse;
		int jtStartIndex=1;
		int jtPageSize=10;
		try {			
			
			List<ProductUserRole> productUserRole=productListService.listProductUserRole(productId,jtStartIndex,jtPageSize);
			List<JsonProductUserRole> jsonProductUserRole=new ArrayList<JsonProductUserRole>();
			for(ProductUserRole pm: productUserRole){
				jsonProductUserRole.add(new JsonProductUserRole(pm));
			}
			
            jTableResponse = new JTableResponse("OK", jsonProductUserRole,productListService.getTotalRecordsOfProduct());     
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching UserRoles!");
	            log.error("JSON ERROR listing UserRole", e);
	        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.product.updateUserRole",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateUserRole(@ModelAttribute JsonProductUserRole jsonProductUserRole, BindingResult result) {
		log.debug("Updating User Role");
		JTableResponse jTableResponse;		
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {	
				ProductUserRole productUserRoleFromDB = userListService.getProductUserRoleById(jsonProductUserRole.getProductUserRoleId());				
				ProductUserRole productUserRoleFromUI = jsonProductUserRole.getProductUserRoleList();			
				
				if(productUserRoleFromUI.getRole()!=null && productUserRoleFromUI.getRole().getUserRoleId()!=null && !productUserRoleFromUI.getRole().getUserRoleId().equals("0")){					
					productUserRoleFromUI.setRole(userListService.getRoleById(productUserRoleFromUI.getRole().getUserRoleId()));					
				}				
				
				if(productUserRoleFromUI.getUser()!=null && productUserRoleFromUI.getUser().getUserId()!=null && !productUserRoleFromUI.getUser().getUserId().equals("0")){					
					productUserRoleFromUI.setUser(userListService.getUserListById(productUserRoleFromUI.getUser().getUserId()));					
				}
				if(productUserRoleFromUI.getCreatedDate()!=null ){					
					productUserRoleFromUI.setCreatedDate(productUserRoleFromDB.getCreatedDate());
				}				
				userListService.updateProductUserRole(productUserRoleFromUI);
				if(productUserRoleFromUI.getStatus() != 1){
					userListService.deleteEntityAndUserMappingByProductIdandUserId(productUserRoleFromUI.getProduct().getProductId(),productUserRoleFromUI.getUser().getUserId());
				}
				
				List<JsonProductUserRole> tmpList = new ArrayList();
				tmpList.add(jsonProductUserRole);
				jTableResponse = new JTableResponse("OK",tmpList ,1);			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating Product User Role!");
	            log.error("JSON ERROR updating UserRole", e);
	        }
        return jTableResponse;
    }	
	
	@RequestMapping(value="administration.user.roleList",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listAllUser(int userId) {
		log.debug("inside administration.user.roleList");
		JTableResponseOptions jTableResponseOptions = null;
		final boolean flag = true;
			try {
				List<UserRoleMaster> userRoleMaster = productListService.getAllRoles();
				List<JsonUserRoleMaster> jsonProductUserRole = new ArrayList<JsonUserRoleMaster>();
				UserRoleMaster role = userListService.listUserRoleByUserId(userId);
				for(UserRoleMaster userRole: userRoleMaster){
					if(userRole.getUserRoleId()==role.getUserRoleId()){
						jsonProductUserRole.add(0,new JsonUserRoleMaster(userRole));
					}else{
						if(userRole.getUserRoleId() == 3 || userRole.getUserRoleId()==4 || userRole.getUserRoleId()==5 || userRole.getUserRoleId()==8){
							jsonProductUserRole.add(new JsonUserRoleMaster(userRole));
						}
					}
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonProductUserRole,true);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching UserRoleMasters!");
		            log.error("JSON ERROR listing All UserRoleMaster", e);
		        }
	        return jTableResponseOptions;
    }

	
	@RequestMapping(value="administration.product.environment.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateEnvironment(HttpServletRequest req, @ModelAttribute JsonEnvironment jsonEnvironment, BindingResult result) {
		log.debug("Updating Environment");
		JTableResponse jTableResponse =null;
		List<JsonEnvironment> jsonEnvironmentList = new LinkedList<JsonEnvironment>();
		TestFactory testFactory = null;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {			
			//Environment environmentFromUI = jsonEnvironment.getEnvironment();	
			Environment environmentFromUI=new Environment();
			Environment environmentFromDB = productListService.getEnvironmentById(jsonEnvironment.getEnvironmentId());	
			ProductMaster pm=productListService.getProductDetailsById(jsonEnvironment.getProductMasterId());
			environmentFromUI.setDescription(jsonEnvironment.getDescription());
			environmentFromUI.setEnvironmentId(jsonEnvironment.getEnvironmentId());
			environmentFromUI.setEnvironmentName(jsonEnvironment.getEnvironmentName());
			environmentFromUI.setModifiedDate(new Date(System.currentTimeMillis()));
			environmentFromUI.setCreatedDate(environmentFromDB.getCreatedDate());
			environmentFromUI.setStatus(jsonEnvironment.getStatus());
			environmentFromUI.setProductMaster(pm);
			EnvironmentCategory environmentCategory =environmentService.getEnvironmentCategoryById(jsonEnvironment.getEnvironmentCategoryId());
			EnvironmentGroup environmentGroup = environmentService.getEnvironmentGroupById(jsonEnvironment.getEnvironmentGroupId());
			if(environmentGroup!=null && environmentCategory!= null){
				environmentCategory.setEnvironmentGroup(environmentGroup);
			}
			if(environmentCategory!=null){
				environmentFromUI.setEnvironmentCategory(environmentCategory);
			}			
			
			if(environmentFromUI.getCreatedDate()!=null ){					
				environmentFromUI.setCreatedDate(environmentFromDB.getCreatedDate());
			}
			int count=0;
			List<Environment> environmentsList=productListService.getEnvironmentListByProductId(jsonEnvironment.getProductMasterId());
			Environment environment=	productListService.getEnvironmentById(jsonEnvironment.getEnvironmentId());
			testFactory = pm.getTestFactory();
			if(jsonEnvironment.getStatus()!=1){
				for(Environment envFromDB:environmentsList){
					if(envFromDB.getEnvironmentCategory().getEnvironmentGroup().getEnvironmentGroupId()==environment.getEnvironmentCategory().getEnvironmentGroup().getEnvironmentGroupId()){
						if(++count>1){
							Set<EnvironmentCombination> environmentCombinationSet=environment.getEnvironmentCombinationSet();	
							for(EnvironmentCombination envComb:environmentCombinationSet){
								//Check if the envcomb is associated to any testConfig, if yes then do not delete the environment combination and its association.return error to the user
								List<RunConfiguration> testConfig = productListService.getRunConfigurationListByEnvironmentCombination(envComb.getEnvironment_combination_id());
								if(testConfig != null && !testConfig.isEmpty()){
									jTableResponse = new JTableResponse("ERROR",envComb.getEnvironmentCombinationName()+" is associated to a Test Configuration. So Environment Delete is not allowed!");
									return jTableResponse;
								}
								environmentService.mapEnvironmentCombinationWithEnvironment(envComb.getEnvironment_combination_id(),environment.getEnvironmentId(),"Remove");
								environmentService.deleteEnvironmentCombination(envComb);
							}							
							productListService.updateProductEnvironment(environmentFromUI);
		
							//Entity Audition History //Update
							UserList user = (UserList)req.getSession().getAttribute("USER");
							int userId=user.getUserId();
							UserList userObj = userListService.getUserListById(userId);
							remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+pm.getProductName()+", Environment :"+environmentFromDB.getEnvironmentName();
										eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_ENVIRONMENT,  environmentFromUI.getEnvironmentId(), environmentFromUI.getEnvironmentName(),
												jsonEnvironment.getModifiedField(), jsonEnvironment.getModifiedFieldTitle(),
												jsonEnvironment.getOldFieldValue(), jsonEnvironment.getModifiedFieldValue(), userObj, remarks);
										break;
						}	
					}
				}
				if(count==1){
					jTableResponse = new JTableResponse("ERROR","Only one environment group is available.So Environment Delete is not allowed!");
					return jTableResponse;
				}
			}else{
				productListService.updateProductEnvironment(environmentFromUI);	
			}
			jsonEnvironmentList.add(new JsonEnvironment(environmentFromUI));
			jTableResponse = new JTableResponse("OK",jsonEnvironmentList,jsonEnvironmentList.size());            
		} catch (Exception e) {
	     jTableResponse = new JTableResponse("ERROR","Error updating Product Environment!");
	     log.error("JSON ERROR updating Product Environment", e);
		}	         
	    return jTableResponse;
	}
	
	@RequestMapping(value="administration.product.environment.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteEnvironment(@ModelAttribute JsonEnvironment jsonEnvironment,@RequestParam int environmentId) {
		log.debug("Deleting Environment");
		JTableResponse jTableResponse;
		try {
			Environment environment=	productListService.getEnvironmentById(environmentId);
	            productListService.deleteProductEnvironment(environment);
	            jTableResponse = new JTableResponse("OK");
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error deleting Product Environment!");
	            log.error("JSON ERROR deleting Product Environment", e);
	        }	        
        return jTableResponse;
    }
	/*Product Locale */
	@RequestMapping(value="administration.product.locale.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listLocale(@RequestParam int productMasterId) {			
		JTableResponse jTableResponse;			 
		try {			
			List<ProductLocale> localeList=productListService.getProductLocaleListByProductId(productMasterId);
			List<JsonProductLocale> jsonLocaleList=new ArrayList<JsonProductLocale>();
			for(ProductLocale locale: localeList){
				jsonLocaleList.add(new JsonProductLocale(locale));	
				}				
			jTableResponse = new JTableResponse("OK", jsonLocaleList,jsonLocaleList.size());     
			localeList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching LocaleList!");
	            log.error("JSON ERROR fetching LocaleList", e);
	        }		        
        return jTableResponse;
    }
	@RequestMapping(value="administration.product.locale.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addLocale(@ModelAttribute JsonProductLocale jsonLocale, BindingResult result) {
		log.debug("Adding Locale");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse =jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}			
		try {
			ProductLocale locale = jsonLocale.getProductLocale();
			productListService.addProductLocale(locale);			
            jTableSingleResponse = new JTableSingleResponse("OK",new JsonProductLocale(locale));				
	    }catch (Exception e) {
	    	jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Product Locale!");
	        log.error("JSON ERROR adding new Product Locale", e);
	    }		        
        return jTableSingleResponse;			
    }	
	@RequestMapping(value="administration.product.locale.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateLocale(@ModelAttribute JsonProductLocale jsonLocale, BindingResult result) {
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {	
				ProductLocale localeFromUI = jsonLocale.getProductLocale();	
				ProductLocale localeFromDB = productListService.getLocaleById(jsonLocale.getProductLocaleId());	
				if(localeFromUI.getCreatedDate()!=null ){					
					localeFromUI.setCreatedDate(localeFromDB.getCreatedDate());
				}
				productListService.updateProductLocale(localeFromUI);	
				 jTableResponse = new JTableResponse("OK");  
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating Product Locale!");
	            log.error("JSON ERROR updating Product Locale", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.product.feature.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listFeatures(@RequestParam int productMasterId,Integer featureStatus, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("Listing Features");
		JTableResponse jTableResponse;			 
		try {
			List<ProductFeature> featuresfromDB = productListService.getFeatureListByProductId(productMasterId, featureStatus, jtStartIndex, jtPageSize);
			Integer featuresfromDBforPagination = productListService.getFeatureListSize(productMasterId);
			List<JsonProductFeature> jsonProductFeature = new ArrayList<JsonProductFeature>();			
			for(ProductFeature pfeature: featuresfromDB){
				jsonProductFeature.add(new JsonProductFeature(pfeature));
			}				
			jTableResponse = new JTableResponse("OK", jsonProductFeature,featuresfromDBforPagination);     
			featuresfromDB = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Features!");
	            log.error("JSON ERROR fetching Features", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.product.feature.list.testrunplan",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listFeaturesByTestRunPlan(@RequestParam int productMasterId,@RequestParam int productVersionId,@RequestParam int testRunPlanId,@RequestParam int type) {
		log.debug("listFeaturesByTestRunPlan");
		JTableResponse jTableResponse;			 
		try {
			List<ProductFeature> featuresfromDB = productListService.getFeatureListByProductId(productMasterId,null, null, null);
			List<JsonProductFeature> jsonProductFeatureList = new ArrayList<JsonProductFeature>();
			JsonProductFeature jsonProductFeature = new JsonProductFeature();
			for(ProductFeature pfeature: featuresfromDB){
				jsonProductFeature=new JsonProductFeature(pfeature);
				if(testRunPlanId!=-1){
					TestRunPlan testRunPlan=productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
					Set<ProductFeature> productFeatureList=testRunPlan.getFeatureList();
					if(productFeatureList!=null && !productFeatureList.isEmpty()){
						if(productFeatureList.contains(pfeature)){
							jsonProductFeature.setIsSelected(1);
						}else{
							jsonProductFeature.setIsSelected(0);
						}
					}else{
						jsonProductFeature.setIsSelected(0);
					}					
				}
				jsonProductFeatureList.add(jsonProductFeature);			
			}				
			jTableResponse = new JTableResponse("OK", jsonProductFeatureList,jsonProductFeatureList.size());     
			featuresfromDB = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching FeaturesByTestRunPlan!");
	            log.error("JSON ERROR fetching FeaturesByTestRunPlan", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="product.testcase.list.testRunPlan",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestCasesByTestRunPlan(@RequestParam int productMasterId,@RequestParam int productVersionId,@RequestParam int testRunPlanId,@RequestParam int type) {
		log.debug("inside product.testcase.list");
		JTableResponse jTableResponse;
			try {
			List<TestCaseList> testCaseList=testCaseService.getTestCaseListByProductId(productMasterId, null, null);
			List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
			JsonTestCaseList jsonTestCase = new JsonTestCaseList();
			if (testCaseList == null)
			{	
				log.info("inside product.testcase.list testCaseList is null");
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			for(TestCaseList tcl: testCaseList){
				jsonTestCase=new JsonTestCaseList(tcl);
				if(testRunPlanId!=-1){
					TestRunPlan testRunPlan=productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
					Set<TestCaseList> testcaselist=testRunPlan.getTestCaseList();
					if(testcaselist!=null && !testcaselist.isEmpty()){
						if(testcaselist.contains(tcl)){
							jsonTestCase.setIsSelected(1);
						}else{
							jsonTestCase.setIsSelected(0);
						}
					}else{
						jsonTestCase.setIsSelected(0);
					}					
				}
				jsonTestCaseList.add(jsonTestCase);
			}
            jTableResponse = new JTableResponse("OK", jsonTestCaseList,jsonTestCaseList.size());
            testCaseList = null;
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching TestCases By TestRunPlan!");
	            log.error("JSON ERROR listing TestCasesByTestRunPlan", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.testcase.feature.mappedlist",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listFeaturesMappedToTestCase(@RequestParam int testCaseId) {	
		log.debug("list Features Mapped To TestCase");
		JTableResponse jTableResponse;			 
		try {
			List<ProductFeature> featuresfromDB = productListService.getFeaturesMappedToTestCase(testCaseId);
			List<JsonProductFeature> jsonProductFeature = new ArrayList<JsonProductFeature>();			
			for(ProductFeature pfeature: featuresfromDB){
				jsonProductFeature.add(new JsonProductFeature(pfeature));
			}				
			jTableResponse = new JTableResponse("OK", jsonProductFeature,jsonProductFeature.size());     
			featuresfromDB = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Features Mapped To TestCase!");
	            log.error("JSON ERROR fetching Features Mapped To TestCase!", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.product.feature.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addFeature(HttpServletRequest req, @ModelAttribute JsonProductFeature jsonProductFeature, BindingResult result) {  
		log.debug("Adding Feature");
		JTableSingleResponse jTableSingleResponse = null;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}			
		try {
			UserList user = (UserList) request.getSession().getAttribute("USER");
			ProductFeature featuresfromUI = jsonProductFeature.getProductFeature();
			ProductMaster productMasterFromUI = new ProductMaster();
			productMasterFromUI.setProductId(jsonProductFeature.getProductId());
						
			featuresfromUI.setProductMaster(productMasterFromUI);
			
			if(jsonProductFeature.getParentFeatureId()!= null && !jsonProductFeature.getParentFeatureId().equals("0")){
				featuresfromUI.setParentFeature(productListService.getByProductFeatureId(jsonProductFeature.getParentFeatureId()));
			}
			String errorMessage = ValidationUtility.validateForNewProductFeatureAddition(featuresfromUI, productListService);
			if (errorMessage != null) {				
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			//Workflow implementation logic start
			WorkflowStatus workflowStatus = null;
			if(jsonProductFeature.getStatusId() ==  null || jsonProductFeature.getStatusId() == 0){
				workflowStatus = workflowStatusService.getInitialStatusForInstanceByWorkflowId(featuresfromUI.getProductMaster().getProductId(), IDPAConstants.PRODUCT_FEATURE_ENTITY_MASTER_ID, 0, jsonProductFeature.getWorkflowId());
			}else{
				workflowStatus = workflowStatusService.getWorkflowStatusById(jsonProductFeature.getStatusId());
			}
			/*if(workflowStatus == null || workflowStatus.getWorkflowStatusId() == null || workflowStatus.getWorkflowStatusId() == 0){
				jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to find status for activity. Please check workflow mapped for activity type or mapped workflow has status");
				return jTableSingleResponse;
			}*/
			if(workflowStatus == null) {
				workflowStatus= new WorkflowStatus();
				workflowStatus.setWorkflowStatusId(-1);
			} 
			featuresfromUI.setWorkflowStatus(workflowStatus);
			//Workflow implementation logic End
			
			featuresfromUI.setSourceType(IDPAConstants.FEATURE_ADD_SOURCE_TYPE);
			productListService.addProductFeature(featuresfromUI);

			if(featuresfromUI!=null){
				if(featuresfromUI.getProductFeatureId()!=null){
					mongoDBService.addProductFeature(featuresfromUI.getProductFeatureId());
					int userId=user.getUserId();
					UserList userObj = userListService.getUserListById(userId);
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_PRODUCT_FEATURE_LABEL, featuresfromUI.getProductFeatureId(), featuresfromUI.getProductFeatureName(), userObj);
				}					
			}
			workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(featuresfromUI.getProductMaster().getProductId(), IDPAConstants.PRODUCT_FEATURE_ENTITY_MASTER_ID, 0, featuresfromUI.getProductFeatureId(), jsonProductFeature.getWorkflowId(), featuresfromUI.getWorkflowStatus().getWorkflowStatusId(), user, featuresfromUI.getCreatedDate(), featuresfromUI);	
	        jTableSingleResponse = new JTableSingleResponse("OK",new JsonProductFeature(featuresfromUI));
	       } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Product Feature!");
	            log.error("JSON ERROR Error adding new Product Feature", e);
	        }	        
        return jTableSingleResponse;
		
    }
	
	@RequestMapping(value="administration.product.feature.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateFeature(HttpServletRequest request, @ModelAttribute JsonProductFeature jsonProductFeature, BindingResult result) {

		log.debug("Updating Feature");
		JTableResponse jTableResponse;
		TestFactory testFactory = null;
		ProductMaster productMaster = null;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {		
				ProductFeature productFeatureFromUI = jsonProductFeature.getProductFeature();
				productMaster = productFeatureFromUI.getProductMaster();
				testFactory = productMaster.getTestFactory();
				ProductFeature featuresfromUI = jsonProductFeature.getProductFeature();
				String errorMessage = ValidationUtility.validateForNewProductFeatureAddition(featuresfromUI, productListService);
				if (errorMessage != null) {
					
					jTableResponse = new JTableResponse("ERROR",errorMessage);
					return jTableResponse;
				}
				
				List<Integer> featureTotestCaseMappingList = productListService.getFeatureTotestCaseMappingByFeatureId(productFeatureFromUI.getProductFeatureId());
				log.info("Modified Field : " + jsonProductFeature.getModifiedField());
				log.info("Modified Field Title : " + jsonProductFeature.getModifiedFieldTitle());
				log.info("Modified Field Id : " + jsonProductFeature.getModifiedFieldID());
				log.info("Modified Field Value : " + jsonProductFeature.getModifiedFieldValue());
				log.info("Old Field Id : " + jsonProductFeature.getOldFieldID());
				log.info("Old Field value : " + jsonProductFeature.getOldFieldValue());

				//Workflow changes start
				UserList assigneeUserList=null;
				UserList reviewerUserList=null;			
				if(productFeatureFromUI.getAssignee()!=null){
					 assigneeUserList=userListService.getUserListById(productFeatureFromUI.getAssignee().getUserId());
				}
				if(productFeatureFromUI.getReviewer()!=null){
					 reviewerUserList=userListService.getUserListById(productFeatureFromUI.getReviewer().getUserId());
				}
				
				if(jsonProductFeature.getModifiedFieldTitle().equalsIgnoreCase("Reviewer") ){ 
					configurationWorkFlowService.changeInstnaceActorMapping(productFeatureFromUI.getProductMaster().getProductId(),  IDPAConstants.PRODUCT_FEATURE_ENTITY_MASTER_ID, 0,  productFeatureFromUI.getProductFeatureId(), productFeatureFromUI.getWorkflowStatus().getWorkflow().getWorkflowId(), reviewerUserList);
				} else if(jsonProductFeature.getModifiedFieldTitle().equalsIgnoreCase("Assignee")) {
					configurationWorkFlowService.changeInstnaceActorMapping(productFeatureFromUI.getProductMaster().getProductId(),  IDPAConstants.PRODUCT_FEATURE_ENTITY_MASTER_ID, 0,  productFeatureFromUI.getProductFeatureId(), productFeatureFromUI.getWorkflowStatus().getWorkflow().getWorkflowId(), assigneeUserList);
				}
				//Workflow changes end				
				
				if (jsonProductFeature.getModifiedField().equalsIgnoreCase("parentFeatureId")) {
					if (jsonProductFeature.getOldFieldValue() == null || jsonProductFeature.getOldFieldValue().trim().isEmpty() || jsonProductFeature.getOldFieldValue().equals("-1") || jsonProductFeature.getOldFieldValue().equals("0")) {
						ProductFeature rootFeature = productListService.getRootFeature();
						jsonProductFeature.setOldFieldID(rootFeature.getProductFeatureId()+"");
						jsonProductFeature.setOldFieldValue(rootFeature.getProductFeatureName());
					}
					if (jsonProductFeature.getModifiedFieldID() == null || jsonProductFeature.getModifiedFieldID().equals("-1")) {
						ProductFeature rootFeature = productListService.getRootFeature();
						jsonProductFeature.setModifiedFieldID(rootFeature.getProductFeatureId()+"");
						jsonProductFeature.setModifiedFieldValue(rootFeature.getProductFeatureName());
					}
					updateFeatureParent(productFeatureFromUI, new Integer(jsonProductFeature.getOldFieldValue()), new Integer(jsonProductFeature.getModifiedFieldValue()));
				} else {
					productListService.updateProductFeature(productFeatureFromUI);
				}
				
				UserList user = (UserList)request.getSession().getAttribute("USER");
				int userId=user.getUserId();
				UserList userObj = userListService.getUserListById(userId);
				remarks = "Product :"+productMaster.getProductName()+", ProductFeature :"+productFeatureFromUI.getProductFeatureName();
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_PRODUCT_FEATURE_LABEL, productFeatureFromUI.getProductFeatureId(), productFeatureFromUI.getProductFeatureName(),
						jsonProductFeature.getModifiedField(), jsonProductFeature.getModifiedFieldTitle(),
						jsonProductFeature.getOldFieldValue(), jsonProductFeature.getModifiedFieldValue(), userObj, remarks);
				
				List<JsonProductFeature> tmpList = new ArrayList<JsonProductFeature>();
			//	Integer totalEfforts= workflowEventService.getTotalEffortsByEntityInstanceIdAndEntityType(jsonProductFeature.getProductFeatureId(),IDPAConstants.PRODUCT_FEATURE_ENTITY_MASTER_ID);
			//	jsonProductFeature.setTotalEffort(totalEfforts);
				tmpList.add(jsonProductFeature);
				jTableResponse = new JTableResponse("OK",tmpList ,1);    
				
				
				
				if(featureTotestCaseMappingList != null && featureTotestCaseMappingList.size() >0) {
					for(Integer testCaseId:featureTotestCaseMappingList) {
						
						productListService.updateProductFeatureTestCasesOneToMany(testCaseId,productFeatureFromUI.getProductFeatureId(), "map");
					}
				}
				
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating Product Feature!");
	            log.error("JSON ERROR updating Product Feature", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.product.feature.update.parent",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateFeatureParentInHierarchy(HttpServletRequest request,@RequestParam Integer featureId, @RequestParam Integer oldParentFeatureId, @RequestParam Integer newParentFeatureId) {

		log.debug("Updating Feature");
		JTableResponse jTableResponse;
		try {
			ProductFeature feature = productListService.getByProductFeatureId(featureId);
			updateFeatureParent(feature, oldParentFeatureId, newParentFeatureId);
			jTableResponse = new JTableResponse("OK", "Updated Product Feature parent");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error updating Product Feature parent!");
			log.error("JSON ERROR updating Product Feature parent", e);
		}
		return jTableResponse;
	}
	
	public void updateFeatureParent(ProductFeature feature, Integer oldParentFeatureId, Integer newParentFeatureId) {
		
		ProductFeature oldParentFeature = null;
		if (oldParentFeatureId == null || oldParentFeatureId < 0 ) {
			oldParentFeature = productListService.getRootFeature();
		} else {
			oldParentFeature = productListService.getByProductFeatureId(oldParentFeatureId);
		}
		ProductFeature newParentFeature = null;
		if (newParentFeatureId == null || newParentFeatureId < 0) {
			newParentFeature = productListService.getRootFeature();
		} else {
			newParentFeature = productListService.getByProductFeatureId(newParentFeatureId);
		}
		updateFeatureParent(feature, oldParentFeature, newParentFeature);
	}
	
	public void updateFeatureParent(ProductFeature feature, ProductFeature oldParentFeature, ProductFeature newParentFeature) {
		
		log.debug("updating Product Feature parent");
		String ENTITY_TABLE_NAME = "product_feature";

		try {
			
			log.info("Change Feature parent for " + feature.getProductFeatureName() + " from " + oldParentFeature.getProductFeatureName() + " to " + newParentFeature.getProductFeatureName());
			//Refresh the feature from DB so that all index related fileds are available
			feature = productListService.getByProductFeatureId(feature.getProductFeatureId());
			log.info("Updated Feature Indexes : " + feature.getLeftIndex() + " : " + feature.getRightIndex());
			//Get the child nodes hierarchy in a list. The list will be ordered top to bottom, left to right
			List<ProductFeature> childFeaturesHierarchy = productListService.listChildNodesInHierarchyinLayers(feature);
			log.info("Updated Node : " + feature.getProductFeatureName());
			log.info("Child Nodes Count : " + childFeaturesHierarchy.size());
			//childFeaturesHierarchy.forEach(childFeature -> log.info("Feature Hierarchy Layered : " + childFeature.getLeftIndex() + " : " + childFeature.getProductFeatureName() + " : " + childFeature.getRightIndex()));
			if (childFeaturesHierarchy == null || childFeaturesHierarchy.isEmpty()) {

				log.info("Updated Feature Indexes Before Delete : " + feature.getLeftIndex() + " : " + feature.getRightIndex());
				//The feature is a leaf node. Remove from old hierarchy and move to new hierarchy
				//Remove the updated node. With this, the hierarchy now has no indexes for the updated feature 
				log.info("Parent Feature Indexes Before Delete : " + newParentFeature.getLeftIndex() + " : " + newParentFeature.getRightIndex());
				hierarchicalEntitiesService.updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, feature.getLeftIndex(), feature.getRightIndex());
				
				//Refresh the Parent so that its indices are refreshed
				newParentFeature = productListService.getByProductFeatureId(newParentFeature.getProductFeatureId());
				log.info("Parent Feature Indexes After Delete : " + newParentFeature.getLeftIndex() + " : " + newParentFeature.getRightIndex());
				log.info("Updated Feature Indexes After Delete : " + feature.getLeftIndex() + " : " + feature.getRightIndex());
				//Add the updated feature to its new parent hierarchy
				hierarchicalEntitiesService.updateHierarchyIndexForNew(ENTITY_TABLE_NAME, newParentFeature.getRightIndex());
				newParentFeature = productListService.getByProductFeatureId(newParentFeature.getProductFeatureId());
				//newParentFeature = getByProductFeatureId(newParentFeature.getProductFeatureId());
				log.info("Parent Feature Indexes After Add : " + newParentFeature.getLeftIndex() + " : " + newParentFeature.getRightIndex());
				log.info("Updated Feature Indexes After Add : " + feature.getLeftIndex() + " : " + feature.getRightIndex());
				
				//Update the feature's new parent in database
				feature.setParentFeature(newParentFeature);
				feature.setLeftIndex(newParentFeature.getRightIndex() - 2);
				feature.setRightIndex(newParentFeature.getRightIndex() - 1);
				productListService.updateProductFeature(feature);
				feature = productListService.getByProductFeatureId(feature.getProductFeatureId());
				log.info("Updated Feature Indexes After Add : " + feature.getLeftIndex() + " : " + feature.getRightIndex());
				return;
			}
				
			for (ProductFeature feature1 : childFeaturesHierarchy) {
				log.info("Feature Hierarchy Layered sorted : " + feature1.getLeftIndex() + " : " + feature1.getProductFeatureName() + " : " + feature1.getRightIndex());
			}
			
			//Remove all the child nodes, starting from leaf nodes and then progress upwards
			int childNodesCount = childFeaturesHierarchy.size();
			for (int i = childNodesCount-1; i >= 0; i--) {
				
				ProductFeature childFeature = childFeaturesHierarchy.get(i);
				childFeature = productListService.getByProductFeatureId(childFeature.getProductFeatureId());
				oldParentFeature = productListService.getByProductFeatureId(oldParentFeature.getProductFeatureId());
				log.info("Old Parent before Removing child Node : " + oldParentFeature.getLeftIndex() + " : " + oldParentFeature.getProductFeatureName() + " : " + oldParentFeature.getRightIndex());
				hierarchicalEntitiesService.updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, childFeature.getLeftIndex(), childFeature.getRightIndex());
				log.info("Removed child Node : " + childFeature.getLeftIndex() + " : " + childFeature.getProductFeatureName() + " : " + childFeature.getRightIndex());
				oldParentFeature = productListService.getByProductFeatureId(oldParentFeature.getProductFeatureId());
				log.info("Old Parent after Removing child Node : " + oldParentFeature.getLeftIndex() + " : " + oldParentFeature.getProductFeatureName() + " : " + oldParentFeature.getRightIndex());
			}
			//Remove the updated node. With this, the hierarchy now has no indexes for the updated feature and its n level child nodes 
			feature = productListService.getByProductFeatureId(feature.getProductFeatureId());
			hierarchicalEntitiesService.updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, feature.getLeftIndex(), feature.getRightIndex());
			log.info("Removed updated Node : " + feature.getLeftIndex() + " : " + feature.getProductFeatureName() + " : " + feature.getRightIndex());
			oldParentFeature = productListService.getByProductFeatureId(oldParentFeature.getProductFeatureId());
			log.info("Old Parent after updated Node : " + oldParentFeature.getLeftIndex() + " : " + oldParentFeature.getProductFeatureName() + " : " + oldParentFeature.getRightIndex());
			log.info("Finished removing Hierarchy from old parent");
			
			newParentFeature = productListService.getByProductFeatureId(newParentFeature.getProductFeatureId());
			log.info("New Parent before adding updated Node : " + newParentFeature.getLeftIndex() + " : " + newParentFeature.getProductFeatureName() + " : " + newParentFeature.getRightIndex());
			hierarchicalEntitiesService.updateHierarchyIndexForNew(ENTITY_TABLE_NAME, newParentFeature.getRightIndex());
			newParentFeature = productListService.getByProductFeatureId(newParentFeature.getProductFeatureId());
			feature.setParentFeature(newParentFeature);
			feature.setLeftIndex(newParentFeature.getRightIndex() - 2);
			feature.setRightIndex(newParentFeature.getRightIndex() - 1);
			productListService.updateProductFeature(feature);
			feature = productListService.getByProductFeatureId(feature.getProductFeatureId());
			log.info("Updated Feature Indexes After Add : " + feature.getLeftIndex() + " : " + feature.getRightIndex());
			log.info("Added updated Node : " + feature.getLeftIndex() + " : " + feature.getProductFeatureName() + " : " + feature.getRightIndex());
			newParentFeature = productListService.getByProductFeatureId(newParentFeature.getProductFeatureId());
			log.info("New Parent after adding updated Node : " + newParentFeature.getLeftIndex() + " : " + newParentFeature.getProductFeatureName() + " : " + newParentFeature.getRightIndex());
			oldParentFeature = productListService.getByProductFeatureId(oldParentFeature.getProductFeatureId());
			log.info("Old Parent after adding updated Node : " + oldParentFeature.getLeftIndex() + " : " + oldParentFeature.getProductFeatureName() + " : " + oldParentFeature.getRightIndex());
			
			//Add the child nodes back to the hierarchy. Their parents have not changed
			for (int i = 0; i < childNodesCount; i++) {
				
				ProductFeature childFeature = childFeaturesHierarchy.get(i);
				//Reload parent from DB, so that it contains the updated indices
				ProductFeature tempParentFeature = productListService.getByProductFeatureId(childFeature.getParentFeature().getProductFeatureId());
				log.info("Child node parent : " + tempParentFeature.getLeftIndex() + " : " + tempParentFeature.getProductFeatureName() + " : " + tempParentFeature.getRightIndex());
				hierarchicalEntitiesService.updateHierarchyIndexForNew(ENTITY_TABLE_NAME, tempParentFeature.getRightIndex());
				tempParentFeature = productListService.getByProductFeatureId(tempParentFeature.getProductFeatureId());
				childFeature.setLeftIndex(tempParentFeature.getRightIndex() - 2);
				childFeature.setRightIndex(tempParentFeature.getRightIndex() - 1);
				productListService.updateProductFeature(childFeature);
				log.info("Added child Node : " + childFeature.getLeftIndex() + " : " + childFeature.getProductFeatureName() + " : " + childFeature.getRightIndex());
				newParentFeature = productListService.getByProductFeatureId(newParentFeature.getProductFeatureId());
				log.info("New Parent after adding child Node : " + newParentFeature.getLeftIndex() + " : " + newParentFeature.getProductFeatureName() + " : " + newParentFeature.getRightIndex());
				oldParentFeature = productListService.getByProductFeatureId(oldParentFeature.getProductFeatureId());
				log.info("Old Parent after adding child Node : " + oldParentFeature.getLeftIndex() + " : " + oldParentFeature.getProductFeatureName() + " : " + oldParentFeature.getRightIndex());
			}
			log.debug("Parent Feature Updated Successfully");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}

	@RequestMapping(value="product.feature.testcase.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listProductFeatureTestCase(@RequestParam Integer productId) {
		log.debug("inside product.feature.testcase.list");
		JTableResponse jTableResponse;
			try {				
				if (productId == null || ("null").equals(productId)) {
					jTableResponse = new JTableResponse("OK", null,0);
					return jTableResponse;
				}
			List<TestCaseList> testCaseList=testCaseService.getTestCaseListByProductId(productId, null, null);
			if (testCaseList == null)
			{	
				log.info("testCaseList is null");
				//jTableResponse = new JTableResponse("Message","No Data Available");
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			JsonFeatureTestCase jsonFeatureTestCase=null;
			List<JsonFeatureTestCase> jsonFeatureTestCaseList = new ArrayList<JsonFeatureTestCase>();
			
			for(TestCaseList testCase: testCaseList){
				jsonFeatureTestCase = new JsonFeatureTestCase(testCase);
				jsonFeatureTestCaseList.add(jsonFeatureTestCase);
			}
					
            jTableResponse = new JTableResponse("OK", jsonFeatureTestCaseList,testCaseService.getTotalRecordsOfTestCases());
            
            testCaseList = null;
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching feature.testcase.list!");
	            log.error("JSON ERROR fetching featureTestcase", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="product.feature.testcase.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateProductFeatureTestCase(@ModelAttribute JsonFeatureTestCase jsonFeatureTestCase, BindingResult result) {
		log.debug("inside product.feature.testcase.update");		
		JTableResponse jTableResponse = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		try {
			int testCaseId = jsonFeatureTestCase.getTestcaseId();
			int productFeatureId = jsonFeatureTestCase.getProductFeatureId();
			
			TestCaseList testCase = productListService.updateProductFeatureTestCases(testCaseId, productFeatureId);
			
			List<JsonTestCaseList> jsonTestCaseListToUI = new ArrayList<JsonTestCaseList>();
			JsonTestCaseList jsonTestCaseSingleRecord = new JsonTestCaseList(testCase);
			jsonTestCaseListToUI.add(jsonTestCaseSingleRecord);
			
			jTableResponse = new JTableResponse("OK",jsonTestCaseListToUI,1);		
	    } catch (Exception e) {
	        jTableResponse = new JTableResponse("ERROR","Unable to update the testcase Product Feature  & Testcase association!");
	        log.error("JSON ERROR updating ProductFeatureTestCase", e);
	    }	        
	    return jTableResponse;
	}
	
	/* Product core Resources */
	@RequestMapping(value="testfactory.product.coreResouces.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listCoreResources(@RequestParam int productId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {			
		JTableResponse jTableResponse;			 
		try {
			List<TestFactoryProductCoreResource> productCoreResList=productListService.getProductCoreResourcesList(productId,jtStartIndex,jtPageSize);
			List<TestFactoryProductCoreResource> productCoreResListforPagination=productListService.getProductCoreResourcesList(productId,null,null);
			List<JsonTestFactoryProductCoreResource> jsonProductCoreResList=new ArrayList<JsonTestFactoryProductCoreResource>();
			for(TestFactoryProductCoreResource productCoreRes: productCoreResList){
				jsonProductCoreResList.add(new JsonTestFactoryProductCoreResource(productCoreRes));	
				}				
			jTableResponse = new JTableResponse("OK", jsonProductCoreResList,productCoreResListforPagination.size());     
			productCoreResList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching CoreResources!");
	            log.error("JSON ERROR fetching CoreResources", e);
	        }		        
        return jTableResponse;
    }
	@RequestMapping(value="testfactory.product.coreResouces.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addProduct(@ModelAttribute JsonTestFactoryProductCoreResource jsonCoreResouce, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {   
			TestFactoryProductCoreResource coreResouce= jsonCoreResouce.getTestFactoryProductCoreResource();
			UserList user=userListService.getUserListById(jsonCoreResouce.getUserId());
			Boolean errorMessage=	productListService.isUserAlreadyCoreResource(jsonCoreResouce.getProductId(),jsonCoreResouce.getUserId(),jsonCoreResouce.getFromDate(),jsonCoreResouce.getToDate(),null);
			if (errorMessage) {
				String msg="User "+ user.getLoginId() +" is already a core resource for the specified period for this or other products";
				jTableSingleResponse = new JTableSingleResponse("ERROR",msg);
				return jTableSingleResponse;
			}
			productListService.addProductCoreResource(coreResouce);
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestFactoryProductCoreResource(coreResouce));		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding coreResouces to product!");
	            log.error("JSON ERROR adding coreResouces to product", e);	        }
        return jTableSingleResponse;
    }
	
	
	@RequestMapping(value="testfactory.product.coreResouces.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateCoreResource(@ModelAttribute JsonTestFactoryProductCoreResource jsonCoreResouce, BindingResult result) {
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {	
			Date updatetodate=DateUtility.dateformatWithOutTime(jsonCoreResouce.getToDate());
			Date updatefromdate=DateUtility.dateformatWithOutTime(jsonCoreResouce.getFromDate());
			if(updatefromdate.compareTo(updatetodate)>0){
				jTableResponse = new JTableResponse("ERROR","Warning: From date should be less than or equal to To date");
				return jTableResponse;
			}
		TestFactoryProductCoreResource coreResourceFromUI = jsonCoreResouce.getTestFactoryProductCoreResource();
		TestFactoryProductCoreResource coreResourceFromDB = productListService.getCoreResourceById(jsonCoreResouce.getTestFactoryProductCoreResourceId());	
				
				if((coreResourceFromUI.getCreatedDate()!=null) ){					
					coreResourceFromUI.setCreatedDate(coreResourceFromDB.getCreatedDate());
				}
			
				UserList user=userListService.getUserListById(jsonCoreResouce.getUserId());
				UserRoleMaster userRoleMaster = userListService.getRoleById(jsonCoreResouce.getUserRoleId());
				coreResourceFromUI.setUserRole(userRoleMaster);
				Boolean errorMessage=	productListService.isUserAlreadyCoreResource(jsonCoreResouce.getProductId(),jsonCoreResouce.getUserId(),jsonCoreResouce.getFromDate(),jsonCoreResouce.getToDate(),coreResourceFromDB);
				if (errorMessage) {
					String msg="User "+ user.getLoginId() +" is already a core resource for the specified period for this or other products";
					jTableResponse = new JTableResponse("ERROR",msg);
					return jTableResponse;
				}
				else{
					productListService.updateProductCoreResource(coreResourceFromUI);	
					 jTableResponse = new JTableResponse("OK");  
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating Product Core Resource!");
	            log.error("JSON ERROR updating Product Core Resource", e);
	        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="administration.testfactory.customer.product.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listProductsbyTestFactoryId(HttpServletRequest request,@RequestParam int testFactoryId,@RequestParam int productId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("administration.testfactory.customer.product.list");
		JTableResponse jTableResponse;
		List<ProductMaster> productMaster = new ArrayList<ProductMaster>();
		List<ProductMaster> productMasterForTestManager = null;
		try {
			int userId=0;
			int roleId;
			if(productId==0){
				UserList user = (UserList)request.getSession().getAttribute("USER");
				userId=user.getUserId();
				UserList userObj = userListService.getUserListById(userId);
				roleId=userObj.getUserRoleMaster().getUserRoleId();
				String roleName = userObj.getUserRoleMaster().getRoleName();
				if(roleId== IDPAConstants.ROLE_ID_TEST_MANAGER || roleId == IDPAConstants.ROLE_ID_TEST_LEAD){
					Set<ProductUserRole> productUserRoleSet=userObj.getProductUserRoleSet();
					if(productUserRoleSet!=null){
					for(ProductUserRole productUserRole:productUserRoleSet){
						productMasterForTestManager =productListService.getCustmersbyProductId(testFactoryId,productUserRole.getProduct().getProductId()); 
						if(productMasterForTestManager!=null){
						productMaster.addAll(productMasterForTestManager);
						}
						productMasterForTestManager=null;
					}
					}
			}else{
				productMaster =productListService.getCustmersbyProductId(testFactoryId,productId);  
			}
			}else{
				productMaster =productListService.getCustmersbyProductId(testFactoryId,productId);  
			}
			List<JsonProductMaster> jsonProductMaster=new ArrayList<JsonProductMaster>();
			if(productMaster != null && productMaster.size()>0){
				for(ProductMaster pm: productMaster){
					jsonProductMaster.add(new JsonProductMaster(pm));
				}
			}
			if (jsonProductMaster != null){
				Collections.sort(jsonProductMaster, JsonProductMaster.jsonProductMasterComparator);
			}
            jTableResponse = new JTableResponse("OK", jsonProductMaster,productListService.getTotalRecordsOfProduct());     
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Products by TestFactoryId!");
	            log.error("JSON ERROR fetching Products by TestFactoryId", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.testfactory.customer.product.list.bystatus",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listProductsbyTestFactoryIdByStatus(HttpServletRequest request,@RequestParam int testFactoryId,@RequestParam int productId,@RequestParam int status, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("administration.testfactory.customer.product.list.bystatus");
		JTableResponse jTableResponse;
		List<ProductMaster> productMasterList = new ArrayList<ProductMaster>();
		List<ProductMaster> productMasterForTestManagerList = null;
		try {
			int userId=0;
			int roleId;
			int totalRecords = 0;
			if(productId==0){
				UserList user = (UserList)request.getSession().getAttribute("USER");
				userId=user.getUserId();
				UserList userObj = userListService.getUserListById(userId);
				roleId=userObj.getUserRoleMaster().getUserRoleId();
				String roleName = userObj.getUserRoleMaster().getRoleName();
				if(roleId== IDPAConstants.ROLE_ID_TEST_MANAGER || roleId == IDPAConstants.ROLE_ID_TEST_LEAD){
					Set<ProductUserRole> productUserRoleSet=userObj.getProductUserRoleSet();
					if(productUserRoleSet!=null && !productUserRoleSet.isEmpty()){
						productMasterForTestManagerList = productListService.getUserRoleBasedProductByTestFactoryId(testFactoryId, productId, userId, status, jtStartIndex, jtPageSize);	
					}
					if(productMasterForTestManagerList!=null){
						productMasterList.addAll(productMasterForTestManagerList);
						}
					productMasterForTestManagerList=null;
					totalRecords = productListService.getUsersProductCountByTestFactoryId(testFactoryId, productId,  userId,  status,  -1, -1, roleId);		
				}else{
				productMasterList =productListService.getCustmersbyProductIdStatus(testFactoryId,productId,status, jtStartIndex, jtPageSize);  
				totalRecords = productListService.getUsersProductCountByTestFactoryId(testFactoryId, productId,  -1,  status,  -1, -1, roleId);
				}
			}else{
				productMasterList =productListService.getCustmersbyProductIdStatus(testFactoryId,productId,status, jtStartIndex, jtPageSize);  
				totalRecords = productListService.getUsersProductCountByTestFactoryId(testFactoryId, productId,  -1,  status,  -1, -1, -1);
			}
			List<JsonProductMaster> jsonProductMaster=new ArrayList<JsonProductMaster>();
			if(productMasterList != null && productMasterList.size()>0){
				for(ProductMaster pm: productMasterList){
					jsonProductMaster.add(new JsonProductMaster(pm));
				}
			}
			if (jsonProductMaster != null){
				Collections.sort(jsonProductMaster, JsonProductMaster.jsonProductMasterComparator);
			}
			 jTableResponse = new JTableResponse("OK", jsonProductMaster,totalRecords);
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Products byTestFactoryId ByStatus!");
	            log.error("JSON ERROR fetching Products byTestFactoryId ByStatus", e);
	        }	        
        return jTableResponse;
    }
	
	
		@RequestMapping(value="product.resource.experience",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listResourceExperienceOfSelectedProduct(HttpServletRequest request, @RequestParam Integer productId,@RequestParam Integer productVersionId, @RequestParam Integer filter, int jtStartIndex, int jtPageSize) {
			log.debug("inside product.resource.experience");
			Integer userId = null;
			if(filter == 0){
				if(request != null){
					UserList user = (UserList)request.getSession().getAttribute("USER");
					if(user != null){
						userId = user.getUserId();
					}
				}
			}else{
				userId = new Integer(0);
			}
			JTableResponse jTableResponse = null;
			try {
				List<JsonResourceExperienceSummary> jsonResourceExperienceSummary =  new ArrayList<JsonResourceExperienceSummary>();
				if(productId == null || productVersionId == null){					
					jTableResponse = new JTableResponse("OK", jsonResourceExperienceSummary, 0);
					return jTableResponse;
				}
				jsonResourceExperienceSummary = productListService.listResourceExperienceOfSelectedProduct(productId,productVersionId,userId,jtStartIndex, jtPageSize);
				if (jsonResourceExperienceSummary == null || jsonResourceExperienceSummary.isEmpty()) {
					log.info("No data available for productId="+productId+" productVersionId="+productVersionId);
					jTableResponse = new JTableResponse("OK", jsonResourceExperienceSummary, 0);
				} else {
					jTableResponse = new JTableResponse("OK", jsonResourceExperienceSummary, jsonResourceExperienceSummary.size());
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Resources Experience for Product!");
	            log.error("JSON ERROR fetching Resources Experience for Product", e);	            
	        }
	        return jTableResponse;
	    }
		
		
		@RequestMapping(value="resource.experience.details.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listUserExperienceExecutedTCDetails(@RequestParam Integer productId,@RequestParam Integer productVersionId, @RequestParam Integer userId, @RequestParam Integer filter,@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
			log.debug("inside resource.experience.details.list");
			JTableResponse jTableResponse = null;
			try {
				List<JsonWorkPackage> listOfJsonWorkPackages =  new ArrayList<JsonWorkPackage>();
				List<JsonWorkPackageTestCaseExecutionPlan> listOfJsonWorkPackageTestCaseExecutionPlan =  new ArrayList<JsonWorkPackageTestCaseExecutionPlan>();
				List<JsonTestExecutionResultBugList> listOfJsonTestExecutionResultBugList =  new ArrayList<JsonTestExecutionResultBugList>();
				List<JsonTestCaseExecutionResult> listOfJsonTestCaseExecutionResult =  new ArrayList<JsonTestCaseExecutionResult>();
				List<JsonResourceDailyPerformance> listOfJsonResourceDailyPerformance = new ArrayList<JsonResourceDailyPerformance>();
				if(productId == null || productVersionId == null){					
					jTableResponse = new JTableResponse("OK", listOfJsonWorkPackageTestCaseExecutionPlan, 0);
					return jTableResponse;
				}
				if(filter == 0){
					listOfJsonWorkPackages = productListService.listUserExperienceWorkPackagesDetails(productId,productVersionId,userId,jtStartIndex,jtPageSize);
					if (listOfJsonWorkPackages == null || listOfJsonWorkPackages.isEmpty()) {
						log.info("No data available for productId="+productId+" productVersionId="+productVersionId);
						jTableResponse = new JTableResponse("OK", listOfJsonWorkPackages, 0);
					} else {
						jTableResponse = new JTableResponse("OK", listOfJsonWorkPackages, listOfJsonWorkPackages.size());
					}
				}
				if(filter == 1){
					listOfJsonWorkPackageTestCaseExecutionPlan = productListService.listUserExperienceExecutedTCDetails(productId,productVersionId,userId,jtStartIndex, jtPageSize);
					if (listOfJsonWorkPackageTestCaseExecutionPlan == null || listOfJsonWorkPackageTestCaseExecutionPlan.isEmpty()) {
						log.info("No data available for productId="+productId+" productVersionId="+productVersionId);
						jTableResponse = new JTableResponse("OK", listOfJsonWorkPackageTestCaseExecutionPlan, 0);
					} else {
						jTableResponse = new JTableResponse("OK", listOfJsonWorkPackageTestCaseExecutionPlan, listOfJsonWorkPackageTestCaseExecutionPlan.size());
					}
				}
				if(filter == 2){
					listOfJsonTestExecutionResultBugList = productListService.listUserReportedDefectsDetails(productId,productVersionId,userId,jtStartIndex, jtPageSize);
					if (listOfJsonTestExecutionResultBugList == null || listOfJsonTestExecutionResultBugList.isEmpty()) {
						log.info("No data available for productId="+productId+" productVersionId="+productVersionId);
						jTableResponse = new JTableResponse("OK", listOfJsonTestExecutionResultBugList, 0);
					} else {
						jTableResponse = new JTableResponse("OK", listOfJsonTestExecutionResultBugList, listOfJsonTestExecutionResultBugList.size());
					}
				}
				if(filter == 3){
					listOfJsonTestCaseExecutionResult = productListService.listApprovedDefectsDetails(productId,productVersionId,userId, jtStartIndex, jtPageSize);
					if (listOfJsonTestCaseExecutionResult == null || listOfJsonTestCaseExecutionResult.isEmpty()) {
						log.info("No data available for productId="+productId+" productVersionId="+productVersionId);
						jTableResponse = new JTableResponse("OK", listOfJsonTestCaseExecutionResult, 0);
					} else {
						jTableResponse = new JTableResponse("OK", listOfJsonTestCaseExecutionResult, listOfJsonTestCaseExecutionResult.size());
					}
				}
				if(filter == 4){
					listOfJsonWorkPackages = productListService.listUserExperienceWorkPackagesDetails(productId,productVersionId,userId,jtStartIndex,jtPageSize);
					listOfJsonResourceDailyPerformance = productListService.getResourceAveragePerformanceDetails(listOfJsonWorkPackages,userId,jtStartIndex, jtPageSize);
					if (listOfJsonResourceDailyPerformance == null || listOfJsonResourceDailyPerformance.isEmpty()) {
						log.info("No data available for productId="+productId+" productVersionId="+productVersionId);
						jTableResponse = new JTableResponse("OK", listOfJsonResourceDailyPerformance, 0);
					} else {
						jTableResponse = new JTableResponse("OK", listOfJsonResourceDailyPerformance, listOfJsonResourceDailyPerformance.size());
					}
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Resources Experience for Product!");
	            log.error("JSON ERROR fetching Resources Experience for Product", e);	            
	        }
	        return jTableResponse;
	    }
		@RequestMapping(value="environment.combination.list.byProductId",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listEnvironmnetCombinationById(@RequestParam int productVersionId,@RequestParam int productId,@RequestParam int workpackageId,@RequestParam Integer testRunPlanId, @RequestParam int jtStartIndex, 
	    		@RequestParam int jtPageSize) {			
			JTableResponse jTableResponse=null;;			 
			try {
				if(productId==-1){
					if(productVersionId!=-1){
						ProductVersionListMaster productVersionMasterList=productListService.getProductVersionListMasterById(productVersionId);
						if(productVersionMasterList != null) {
							productId=productVersionMasterList.getProductMaster().getProductId();
						}
					}else{
						 productId = workPackageService.getProductIdByWorkpackageId(workpackageId);		
					}
				}
				List<JsonEnvironmentCombination> jsonEnvironmentCombinationList=new ArrayList<JsonEnvironmentCombination>();
				List<EnvironmentCombination> environmentCombinationsList;
				if(productId != -1){
					environmentCombinationsList = environmentService.listEnvironmentCombinationByStatus(productId, 1);	
					if(environmentCombinationsList != null && environmentCombinationsList.size() > 0){
						for(EnvironmentCombination environmentCombination: environmentCombinationsList){
							JsonEnvironmentCombination jsonEnvironmentCombination=	new JsonEnvironmentCombination(environmentCombination);
							jsonEnvironmentCombination.setStatus(0);
							jsonEnvironmentCombinationList.add(jsonEnvironmentCombination);				
						}
					}					
				}
				JsonEnvironmentCombination jsonEnvironmentCombination=	null;

				if(workpackageId!=-1){
					
					List<WorkpackageRunConfiguration> workpackageRunConfigurations =workPackageService.getWorkpackageRunConfigurationOfWPwithrcStatus(workpackageId, 1);
					
					for(WorkpackageRunConfiguration wprunConfig:workpackageRunConfigurations){
						RunConfiguration runConfig=wprunConfig.getRunconfiguration();
						jsonEnvironmentCombination=	new JsonEnvironmentCombination(runConfig.getEnvironmentcombination());
						for(JsonEnvironmentCombination jsonenv:jsonEnvironmentCombinationList){
							if(jsonenv.getEnvionmentCombinationId().equals(jsonEnvironmentCombination.getEnvionmentCombinationId())){
								int index=jsonEnvironmentCombinationList.indexOf(jsonenv);
								jsonenv.setStatus(1);
								jsonEnvironmentCombinationList.set(index,jsonenv);
							}
						}
													
					}
				}

				if(testRunPlanId!=-1){
					TestRunPlan testRunPlan=productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
					Set<RunConfiguration> runConfigurations=testRunPlan.getRunConfigurationList();
					for(RunConfiguration runConfig:runConfigurations){
						jsonEnvironmentCombination=	new JsonEnvironmentCombination(runConfig.getEnvironmentcombination());
						for(JsonEnvironmentCombination jsonenv:jsonEnvironmentCombinationList){
							if(jsonenv.getEnvionmentCombinationId().equals(jsonEnvironmentCombination.getEnvionmentCombinationId())){
								int index=jsonEnvironmentCombinationList.indexOf(jsonenv);
								jsonenv.setStatus(1);
								jsonEnvironmentCombinationList.set(index,jsonenv);
							}							
						}
					}
				}
				
				jTableResponse = new JTableResponse("OK", jsonEnvironmentCombinationList,jsonEnvironmentCombinationList.size());  
				
				environmentCombinationsList = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Product Test Environments Combination!");
		            log.error("JSON ERROR listing EnvironmnetCombination", e);
		        }		        
	        return jTableResponse;
	    }


		@RequestMapping(value="admin.genericDevices.list",method=RequestMethod.POST ,produces="application/json")
		    public @ResponseBody JTableResponse listGenricDevicesByProductId(@RequestParam int productId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize ,@RequestParam String filter,@RequestParam Integer productversionId,@RequestParam Integer  environmentCombinationId,@RequestParam Integer testRunPlanId,@RequestParam Integer workpackageId) {			
				JTableResponse jTableResponse=null;;			 
				try {
					WorkPackage workPackage=null;
					Set<RunConfiguration> runConfigurations =null;
					if(productId==-1){
						if(productversionId!=-1){
							ProductVersionListMaster productVersionListMaster=productListService.getProductVersionListMasterById(productversionId);
							productId=productVersionListMaster.getProductMaster().getProductId();
						}else if(workpackageId!=-1 && productversionId==-1){
							
								workPackage= workPackageService.getWorkPackageById(workpackageId);
								productversionId=workPackage.getProductBuild().getProductVersion().getProductVersionListId();
								
								productId=workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
						}
					}
					List<GenericDevices> genericDevicesList =environmentService.listGenericDevicesByProductId(productId);
					List<JsonGenericDevice> jsonGenericDevicesList=new ArrayList<JsonGenericDevice>();
				
					
					if(filter.equals("-1")){
						 TestRunPlan testRunPlan=productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
						 runConfigurations=productListService.getRunConfigurationList(testRunPlanId,1);
							List<GenericDevices> genericDevicesAlreadyToTestPlan =new ArrayList<GenericDevices>();
						 for(RunConfiguration rc:runConfigurations){
							 if(rc.getEnvironmentcombination().getEnvironment_combination_id().equals(environmentCombinationId))
								 genericDevicesAlreadyToTestPlan.add(rc.getGenericDevice());
						 }
						for(GenericDevices genericDevices: genericDevicesList){
							if(!genericDevicesAlreadyToTestPlan.contains(genericDevices)){
								JsonGenericDevice jsonGenericDevices=	new JsonGenericDevice(genericDevices);
								jsonGenericDevicesList.add(jsonGenericDevices);	
							}
											
						}
						jTableResponse = new JTableResponse("OK", jsonGenericDevicesList,jsonGenericDevicesList.size());  
						
						jsonGenericDevicesList = null;
				        return jTableResponse;
					}else if(filter.equals("1")){//1 for list from workpackage
						
						runConfigurations =productListService.getRunConfigurationListOfWPrcStatus(workpackageId, 1);
						
						List<GenericDevices> genericDevicesAlreadyAddedList =new ArrayList<GenericDevices>();
						
						for(RunConfiguration rc:runConfigurations){
							if(rc.getGenericDevice()!=null){
								if(rc.getEnvironmentcombination().getEnvironment_combination_id().equals(environmentCombinationId))
									genericDevicesAlreadyAddedList.add(rc.getGenericDevice());
							}
						}
						if(genericDevicesAlreadyAddedList==null || genericDevicesAlreadyAddedList.isEmpty() || genericDevicesAlreadyAddedList.size()==0){
							for(GenericDevices gd:genericDevicesList){
								JsonGenericDevice jsonGenericDevices=	new JsonGenericDevice(gd);
								jsonGenericDevicesList.add(jsonGenericDevices);	
							}
						}else{
							for(GenericDevices gd:genericDevicesList){
								if(!genericDevicesAlreadyAddedList.contains(gd)){
									JsonGenericDevice jsonGenericDevices=	new JsonGenericDevice(gd);
									jsonGenericDevicesList.add(jsonGenericDevices);					
								}
							}
						}
						jTableResponse = new JTableResponse("OK", jsonGenericDevicesList,jsonGenericDevicesList.size());  
						jsonGenericDevicesList = null;
				        return jTableResponse;
					
					}else if(filter.equals("-2")){
						for(GenericDevices gd:genericDevicesList){
								JsonGenericDevice jsonGenericDevices=	new JsonGenericDevice(gd);
								jsonGenericDevicesList.add(jsonGenericDevices);					
							}
						jTableResponse = new JTableResponse("OK", jsonGenericDevicesList,jsonGenericDevicesList.size());  
						jsonGenericDevicesList = null;
				        return jTableResponse;
					}					
					} catch (Exception e) {
			            jTableResponse = new JTableResponse("ERROR","Unable to show Generic Devices!");
			            log.error("JSON ERROR listing GenricDevices By ProductId", e);
			        }		        
		        return jTableResponse;
		    }


		@RequestMapping(value="admin.genericDevices.list.mapping",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listGenricDevicesMapping(@RequestParam int productId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize ,@RequestParam String filter,@RequestParam Integer productversionId,@RequestParam Integer  environmentCombinationId,@RequestParam Integer testRunPlanId,@RequestParam Integer workpackageId) {			
			JTableResponse jTableResponse=null;;			 
			try {
				WorkPackage workPackage=null;
				ProductMaster productMaster=null;
				if(productId==-1){
					if(productversionId!=-1){
						ProductVersionListMaster productVersionListMaster=productListService.getProductVersionListMasterById(productversionId);
						productId=productVersionListMaster.getProductMaster().getProductId();
						productMaster=productVersionListMaster.getProductMaster();
					}else if(workpackageId!=-1 && productversionId==-1){
						
							workPackage= workPackageService.getWorkPackageById(workpackageId);
							productversionId=workPackage.getProductBuild().getProductVersion().getProductVersionListId();
							productMaster=workPackage.getProductBuild().getProductVersion().getProductMaster();
							
							productId=workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
					}
				}
				
				//-1 for list from testrunplan 
				if(filter.equals("-1")){
			        List<JsonGenericDevice> unMappedGenDeviceOfTestRunPlan = deviceListService.getUnMappedGenericDeviceOfProductfromRunConfigurationTestRunPlanLevel(productId, environmentCombinationId, 1, testRunPlanId);
			        jTableResponse = new JTableResponse("OK", unMappedGenDeviceOfTestRunPlan,unMappedGenDeviceOfTestRunPlan.size());
				    log.info("Unmapped Device obtained from SQL query");
				    unMappedGenDeviceOfTestRunPlan = null;				       
				}else if(filter.equals("1")){//1 for list from workpackage				
			      //Fetch unmapped Device from GenericDevice which is not mapped in Runconfiguration table WP Level
					List<JsonGenericDevice> unMappedGenericDevicesOfWorkPackageLevel = deviceListService.getUnMappedGenericDeviceOfProductfromRunConfigurationWorkPackageLevel(productMaster.getProductId(), environmentCombinationId, 1, workpackageId);
					jTableResponse = new JTableResponse("OK", unMappedGenericDevicesOfWorkPackageLevel,unMappedGenericDevicesOfWorkPackageLevel.size());
					log.info("Unmapped GenDevice obtained from SQL query");
					unMappedGenericDevicesOfWorkPackageLevel = null;
					
			        return jTableResponse;
				}else if(filter.equals("-2")){
					Set<GenericDevices> genericDevicesList =productMaster.getGenericeDevices();//environmentService.listGenericDevicesByProductId(productId);
					List<JsonGenericDevice> jsonGenericDevicesList=new ArrayList<JsonGenericDevice>();
					for(GenericDevices gd:genericDevicesList){
							JsonGenericDevice jsonGenericDevices=	new JsonGenericDevice(gd);
							jsonGenericDevicesList.add(jsonGenericDevices);					
						}
					jTableResponse = new JTableResponse("OK", jsonGenericDevicesList,jsonGenericDevicesList.size());  
					jsonGenericDevicesList = null;
			        return jTableResponse;
				}
				
				} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Generic Devices Mapping!");
		            log.error("JSON ERROR listing GenricDevices Mapping", e);
		        }		        
	        return jTableResponse;
	    }

		@RequestMapping(value="administration.runConfiguration.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listRunConfigurationByProductId(@RequestParam int productId,@RequestParam int workpackageId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize,@RequestParam String type  ) {			
			JTableResponse jTableResponse=null;;			 
			try {
				List<RunConfiguration> runConfigurationsList =environmentService.listRunConfiguration(productId);
				List<JsonRunConfiguration> jsonRunConfigurationList=new ArrayList<JsonRunConfiguration>();
				for(RunConfiguration runConfiguration: runConfigurationsList){
					
					if(workpackageId!=-1){
						WorkPackage workPackage=workPackageService.getWorkPackageById(workpackageId);
						List<WorkpackageRunConfiguration> runConfigurationsSet= workPackageService.getWorkpackageRunConfigurationList(workpackageId, null, type);
						if(runConfigurationsSet!=null && runConfigurationsSet.size()!=0){
							for(WorkpackageRunConfiguration runConfigurationWP:runConfigurationsSet){
								if(runConfiguration.getRunconfigId() .equals(runConfigurationWP.getRunconfiguration().getRunconfigId())){
									JsonRunConfiguration jsonRunConfiguration=	new JsonRunConfiguration(runConfiguration);
									jsonRunConfiguration.setStatus(1);
									jsonRunConfigurationList.add(jsonRunConfiguration);
								}else{
									JsonRunConfiguration jsonRunConfiguration=	new JsonRunConfiguration(runConfiguration);
									jsonRunConfiguration.setStatus(0);
									jsonRunConfigurationList.add(jsonRunConfiguration);
								}
							}
						}
						else{
							JsonRunConfiguration jsonRunConfiguration=	new JsonRunConfiguration(runConfiguration);
							jsonRunConfiguration.setStatus(0);
							jsonRunConfigurationList.add(jsonRunConfiguration);
						}
					}else{
					
						JsonRunConfiguration jsonRunConfiguration=	new JsonRunConfiguration(runConfiguration);
						jsonRunConfiguration.setStatus(0);
						jsonRunConfigurationList.add(jsonRunConfiguration);
					}
				}
				
				jTableResponse = new JTableResponse("OK", jsonRunConfigurationList,jsonRunConfigurationList.size());  
				
				jsonRunConfigurationList = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show RunConfiguration By ProductId!");
		            log.error("JSON ERROR listing RunConfiguration By ProductId", e);
		        }		        
	        return jTableResponse;
	    }

		
		@RequestMapping(value="admin.genericDevices.add",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addGenericDevice(@RequestParam Integer productId,@ModelAttribute JsonGenericDevice  jsonGenericDevice, BindingResult result) {
			JTableSingleResponse jTableSingleResponse;
			if(result.hasErrors()){
				jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
			}			
			try {   
				ProductMaster product = new ProductMaster();
				product.setProductId(productId);
				DeviceLab deviceLab=deviceListService.getDeviceLabByDeviceLabId(jsonGenericDevice.getDeviceLabId());
				DeviceModelMaster deviceModelMaster=deviceListService.getDeviceModelMasterById(jsonGenericDevice.getDeviceModelMasterId());
				PlatformType platformType=deviceListService.getPlatFormType(jsonGenericDevice.getPlatformTypeId());
				GenericDevices genericDevice=jsonGenericDevice.getGenericDevices();
				genericDevice.setProductMaster(product);
				genericDevice.setPlatformType(platformType);
				genericDevice.setDeviceModelMaster(deviceModelMaster);
				genericDevice.setDeviceLab(deviceLab);
				genericDevice.setStatus(1);
				
				String errorMessage=commonService.duplicateName(genericDevice.getName(), "GenericDevices", "name", "Generic Device", "productMaster.productId="+product.getProductId());
				if (errorMessage != null) {
					
					jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
					return jTableSingleResponse;
				}
				environmentService.addGenericDevice(genericDevice);
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonGenericDevice(genericDevice));		
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding GenericDevice!");
		            log.error("JSON ERROR adding GenericDevice", e);
		        }
		        
	        return jTableSingleResponse;
	    }
		
		@RequestMapping(value="admin.genericDevices.array.add",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addGenericDevices(HttpServletRequest req,@RequestParam Map<String, String>  mapData, @RequestParam Integer devTypeId) {	
			JTableSingleResponse jTableSingleResponse = null;					
			try {
				ServerType serverType = null;
				MobileType mobileType = null;
				StorageDrive storageDrive = null;
				UserList userObj = (UserList)req.getSession().getAttribute("USER");
				
				if(devTypeId != 4){
					DeviceLab deviceLab=deviceListService.getDeviceLabByDeviceLabId(Integer.parseInt(mapData.get("devLabId")));
					DeviceModelMaster deviceModelMaster=deviceListService.getDeviceModelMasterById(Integer.parseInt(mapData.get("deviceModelId")));
					PlatformType platformType=deviceListService.getPlatFormType(Integer.parseInt(mapData.get("platTypeId")));
					DeviceType devType = deviceListService.getDeviceTypeByTypeId(devTypeId);
					GenericDevices genericDevice =new GenericDevices();					
					if(devTypeId == 5){
					 	mobileType = new MobileType();
						mobileType.setKernelNumber(mapData.get("kerNo"));
						mobileType.setBuildNumber(mapData.get("buildNo"));
						DeviceMakeMaster deviceMakeMaster = new DeviceMakeMaster();
						deviceMakeMaster.setDeviceMakeId(Integer.parseInt(mapData.get("devMakeId")));
						mobileType.setDeviceMakeMaster(deviceMakeMaster);
						
						mobileType.setName(mapData.get("devName"));
						mobileType.setDescription(mapData.get("devDesc"));
						mobileType.setPlatformType(platformType);
						mobileType.setDeviceModelMaster(deviceModelMaster);
						mobileType.setDeviceLab(deviceLab);
						mobileType.setStatus(1);
						mobileType.setAvailableStatus(Integer.parseInt(mapData.get("devAvailStatus")));
						mobileType.setUDID(mapData.get("udid"));
						mobileType.setScreenResolutionX(Integer.parseInt(mapData.get("screenResolutionX")));
						mobileType.setScreenResolutionY(Integer.parseInt(mapData.get("screenResolutionY")));
					}else if(devTypeId == 6){
						storageDrive = new StorageDrive();
						storageDrive.setName(mapData.get("devName"));
						storageDrive.setDescription(mapData.get("devDesc"));
						storageDrive.setPlatformType(platformType);
						if(mapData.get("sdSize")!=null && mapData.get("sdSize")!=""){
							if(NumberUtils.isNumber(mapData.get("sdSize"))  == true){
								storageDrive.setStorageSize(new Long(mapData.get("sdSize")));
							} else {
								storageDrive.setStorageSize(new Long(mapData.get("sdSize").replaceAll("[^0-9]", "")));
							}
					    } else {
					    	storageDrive.setStorageSize(new Long(0));
					    }
						storageDrive.setStorageSizeUnit(mapData.get("sdstorageSizeUnit"));
						storageDrive.setFirmware(mapData.get("sdFirmware"));
						storageDrive.setBootLoader(mapData.get("sdbootLoader"));
						//storageDrive.setSerialNumber(mapData.get("sdserialNumber"));
						storageDrive.setUDID(mapData.get("udid"));//serial number
						storageDrive.setAvailableStatus(Integer.parseInt(mapData.get("devAvailStatus")));
						storageDrive.setDriveVersion(mapData.get("sddriveVersion"));
						storageDrive.setStatus(1);
						
						DeviceMakeMaster deviceMakeMaster = new DeviceMakeMaster();
						deviceMakeMaster.setDeviceMakeId(Integer.parseInt(mapData.get("devMakeId")));					
						storageDrive.setDeviceMakeMaster(deviceMakeMaster);
						storageDrive.setDeviceModelMaster(deviceModelMaster);
						storageDrive.setDeviceLab(deviceLab);
						Processor processor = new Processor();
						processor = deviceListService.getProcessorByProcessorId(Integer.parseInt(mapData.get("sysProcessorId")));
						storageDrive.setProcessor(processor);
						
						HostList hostList= new HostList();
						hostList=hostListService.getHostById(Integer.parseInt(mapData.get("terminalHostId")));
						storageDrive.setHostList(hostList);					
					}
					
					String errorMessage=commonService.duplicateName(mapData.get("devName"), "GenericDevices", "name", "Generic Device", "deviceLab.device_lab_Id="+deviceLab.getDevice_lab_Id());
					if (errorMessage != null) {					
						jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
						return jTableSingleResponse;
					}
										
					if(devTypeId == 5){
						environmentService.addGenericDevice((GenericDevices)mobileType);
						eventsService.addNewEntityEvent(IDPAConstants.PRODUCT_DEVICE, mobileType.getGenericsDevicesId(), mobileType.getName(), userObj);
						jTableSingleResponse = new JTableSingleResponse("OK",new JsonGenericDevice((GenericDevices)mobileType));		
					}else if(devTypeId == 6){
						environmentService.addGenericDevice((GenericDevices)storageDrive);
						eventsService.addNewEntityEvent(IDPAConstants.PRODUCT_DEVICE, storageDrive.getGenericsDevicesId(), storageDrive.getName(), userObj);
						jTableSingleResponse = new JTableSingleResponse("OK",new JsonGenericDevice((GenericDevices)storageDrive));		
					}	
				}
				
				HostList host = null;
				if(devTypeId == 4){					
					//Host List can be added
					host = new HostList();
					host.setHostName(mapData.get("hostName"));
					host.setHostIpAddress(mapData.get("hostIp"));
											
					HostPlatformMaster hostPlatformMaster = new HostPlatformMaster();
					hostPlatformMaster.setHostPlatform(mapData.get("platTypeId"));
					host.setHostPlatformMaster(hostPlatformMaster);
					
					HostTypeMaster hostTypeMaster = new HostTypeMaster();
					hostTypeMaster.setHostType("TERMINAL");
					host.setHostTypeMaster(hostTypeMaster);
					
					CommonActiveStatusMaster hostStatusMaster = new CommonActiveStatusMaster();
					if(mapData.get("devAvailStatus") != null && mapData.get("devAvailStatus").equalsIgnoreCase("YES"))
						hostStatusMaster.setStatus("ACTIVE");
					else if(mapData.get("devAvailStatus") != null && mapData.get("devAvailStatus").equalsIgnoreCase("NO"))
						hostStatusMaster.setStatus("INACTIVE");
					else
						hostStatusMaster.setStatus("INACTIVE");
					host.setCommonActiveStatusMaster(hostStatusMaster);
					
					String errorMessage=commonService.duplicateName(mapData.get("hostName"), "HostList", "hostName", "Host", null);
					if (errorMessage != null) {					
						jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
						return jTableSingleResponse;
					}
					
					if(devTypeId == 4){
						hostListService.add(host);
						eventsService.addNewEntityEvent(IDPAConstants.TARGET_TYPE_HOST, host.getHostId(), host.getHostName(), userObj);
						jTableSingleResponse = new JTableSingleResponse("OK",new JsonHostList(host));		
					}
				}						
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding Target Host / Device!");
	            log.error("JSON ERROR add Generic Host / Device in Array", e);
	        }		        
	        return jTableSingleResponse;
	    }
	
		
		@RequestMapping(value="getPlatFormVersion.by.platformId",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getPlatformTypeVersionByplatformId(@RequestParam int platformId) {
			log.debug("Inside getProductTypeByVersionId ");
			String ffinalResult="";
			JSONArray jplatformVersionarray = new JSONArray();			
			try {   		
				String platformTypeVersion = deviceListService.getPlatformTypeVersionByplatformId(platformId);
				
				JSONObject jobj = new JSONObject();				
				jobj.put("platformVersion", platformTypeVersion);
				jplatformVersionarray.add(jobj);				
						ffinalResult=jplatformVersionarray.toString();
						if (ffinalResult == null) {		
							log.info("No platformVersion ");							
							return "No Data";
						}
		        } catch (Exception e) {
		            log.error("JSON ERROR getting PlatformTypeVersion ByplatformId", e);
		        }		        
	        return  ffinalResult;
	    }	
		
		@RequestMapping(value="admin.genericDevices.array.update",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse updateGenericDevices(HttpServletRequest req,@RequestParam Map<String, String>  mapData, @RequestParam Integer devTypeId) {	
			JTableSingleResponse jTableSingleResponse;	
			String remarks = "";
			try {
				
				ServerType serverType = null;
				MobileType mobileType = null;
				mobileType = new MobileType();
				
				HostList hostList ;
				GenericDevices genericDevice = environmentService.getGenericDevicesById(Integer.parseInt(mapData.get("genericsDevicesId")));
				
				String fieldNames = "";
				String oldFieldValues = "";
				String newFieldValues = "";
				boolean mobileVersionFlag = Boolean.parseBoolean(mapData.get("mobileplatformVersionFlag"));
				boolean mobileStatusFlag = Boolean.parseBoolean(mapData.get("mobileStatusFlag"));
				boolean mobileHostListFlag = Boolean.parseBoolean(mapData.get("mobilehostListFlag"));
				if(mobileVersionFlag){
					fieldNames = IDPAConstants.MOBILE_PLATFORM_VERSION;
					oldFieldValues = mapData.get("mobileplatformTypeVersion_oldValue");
					newFieldValues = mapData.get("mobileplatformTypeVersion_newValue");
				}
				if(mobileStatusFlag){
					if(fieldNames != ""){
						fieldNames += ","+IDPAConstants.MOBILE_AVAILABLE_STATUS;
						oldFieldValues += ","+mapData.get("mobileAvailableStatus_oldValue");
						newFieldValues += ","+mapData.get("mobileAvailableStatus_newValue");
					}else{
						fieldNames = IDPAConstants.MOBILE_AVAILABLE_STATUS;
						oldFieldValues = mapData.get("mobileAvailableStatus_oldValue");
						newFieldValues = mapData.get("mobileAvailableStatus_newValue");
					}
				}
				if(mobileHostListFlag){
					if(fieldNames != ""){
						fieldNames += ","+IDPAConstants.MOBILE_HOSTLIST;
						oldFieldValues += ","+mapData.get("mobileTerminalHost_oldValue");
						newFieldValues += ","+mapData.get("mobileTerminalHost_newValue");
					}else{
						fieldNames = IDPAConstants.MOBILE_HOSTLIST;
						oldFieldValues = mapData.get("mobileTerminalHost_oldValue");
						newFieldValues = mapData.get("mobileTerminalHost_newValue");
					}
				}
				if(fieldNames.endsWith(",")){
					fieldNames = fieldNames.substring(0, fieldNames.length()-1);
				}
				if(oldFieldValues.endsWith(",")){
					oldFieldValues = oldFieldValues.substring(0, oldFieldValues.length()-1);
				}
				if(newFieldValues.endsWith(",")){
					newFieldValues = newFieldValues.substring(0, newFieldValues.length()-1);
				}
				int availablestat = Integer.parseInt(mapData.get("devAvailStatus"));
				UserList userObj = (UserList)req.getSession().getAttribute("USER");
				if(devTypeId == 4){		
					((ServerType)genericDevice).setAvailableStatus(availablestat);
					if(mapData.get("terminalHostId") != null){
						hostList=hostListService.getHostById(Integer.parseInt(mapData.get("terminalHostId")));
						((ServerType)genericDevice).setHostList(hostList);
					}
					//genericDevice.setHostList(hostList);(Integer.parseInt(mapData.get("devAvailStatus")));					
					environmentService.updateGenericDevice((ServerType)genericDevice); 
					eventsService.addEntityChangedEvent(IDPAConstants.PRODUCT_DEVICE,  genericDevice.getGenericsDevicesId(), genericDevice.getName(),
							fieldNames, fieldNames, oldFieldValues, newFieldValues, userObj, remarks);	
				}else if(devTypeId == 5){
					((MobileType)genericDevice).setAvailableStatus(availablestat);
					if(mapData.get("terminalHostId") != null){
						hostList=hostListService.getHostById(Integer.parseInt(mapData.get("terminalHostId")));
						((MobileType)genericDevice).setHostList(hostList);
					}		
					PlatformType platformType = deviceListService.validatePlatFormOrCreate(mapData.get("platformName"), mapData.get("platformVersion"));
					((MobileType)genericDevice).setPlatformType(platformType);
					((MobileType)genericDevice).setScreenResolutionX(Integer.parseInt(mapData.get("screenResolutionX")));
					((MobileType)genericDevice).setScreenResolutionY(Integer.parseInt(mapData.get("screenResolutionY")));
					//((MobileType)genericDevice).setScreenResolutionX(1000);
					environmentService.updateGenericDevice((MobileType)genericDevice);
					eventsService.addEntityChangedEvent(IDPAConstants.PRODUCT_DEVICE,  genericDevice.getGenericsDevicesId(), genericDevice.getName(),
							fieldNames, fieldNames, oldFieldValues, newFieldValues, userObj, remarks);
				}
				else if(devTypeId == 6){
					((StorageDrive)genericDevice).setAvailableStatus(availablestat);
					//((StorageDrive)genericDevice).setHostList(hostList);
					PlatformType platformType = deviceListService.validatePlatFormOrCreate(mapData.get("platformName"), mapData.get("platformVersion"));
					((StorageDrive)genericDevice).setPlatformType(platformType);
					environmentService.updateGenericDevice((StorageDrive)genericDevice);
					eventsService.addEntityChangedEvent(IDPAConstants.PRODUCT_DEVICE,  genericDevice.getGenericsDevicesId(), genericDevice.getName(), 
							fieldNames, fieldNames, oldFieldValues, newFieldValues, userObj, remarks);
				
				}
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonGenericDevice(genericDevice));		
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating Device!");
		            log.error("JSON ERROR updating GenericDevices", e);
		        }
	        return jTableSingleResponse;
	    }
		@RequestMapping(value="admin.genericDevices.list.bygenericsDevicesId",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse getByGenericsDevicesId(@RequestParam int genericsDevicesId) {
			JTableSingleResponse jTSingleResponse = null;
			JTableResponse jTableResponse = null;
			log.debug("Getting genericDevices by genericsDevicesId:"+genericsDevicesId);
			try {	
				GenericDevices genericeDevice = environmentService.getGenericDevicesById(genericsDevicesId);		
				
				log.info("Device Type --"+genericeDevice.getDecriminatorValue());
				JSONObject deviceTypeInfojsobj =new JSONObject();
				if(genericeDevice instanceof ServerType){
					log.info("Server type");
					if(((ServerType) genericeDevice).getHostName() != null){
						deviceTypeInfojsobj.put("serv_hostname", ((ServerType) genericeDevice).getHostName());	
					}
					if(((ServerType) genericeDevice).getIp() != null){
						deviceTypeInfojsobj.put("serv_ip", ((ServerType) genericeDevice).getIp());	
					}
					if(((ServerType) genericeDevice).getSystemName() != null){
						deviceTypeInfojsobj.put("serv_sysname", ((ServerType) genericeDevice).getSystemName());	
					}					
					if(((ServerType) genericeDevice).getSystemType()  != null){
						deviceTypeInfojsobj.put("serv_systype", ((ServerType) genericeDevice).getSystemType());	
					}
					if(((ServerType) genericeDevice).getProcessor() != null){
						deviceTypeInfojsobj.put("serv_processorname", ((ServerType) genericeDevice).getProcessor().getProcessorName());
					}					
				}else if(genericeDevice instanceof MobileType){
					if(((MobileType) genericeDevice).getKernelNumber() != null){
						deviceTypeInfojsobj.put("mob_kernelno", ((MobileType) genericeDevice).getKernelNumber());	
					}
					if(((MobileType) genericeDevice).getBuildNumber() != null){
						deviceTypeInfojsobj.put("mob_buildno", ((MobileType) genericeDevice).getBuildNumber());	
					}					
					if(((MobileType) genericeDevice).getDeviceMakeMaster() != null){
						deviceTypeInfojsobj.put("mob_devmakmastger", ((MobileType) genericeDevice).getDeviceMakeMaster().getDeviceMake());
					}										
				}
				JSONArray deviceJsonArray = new JSONArray();
				deviceJsonArray.add(new JsonGenericDevice(genericeDevice));//Adding Generic Device
				deviceJsonArray.add(deviceTypeInfojsobj);//Adding Device Type info(server/mobile)
				
				jTableResponse = new JTableResponse("OK", deviceJsonArray,deviceJsonArray.size());
				jTSingleResponse = new JTableSingleResponse("OK",new JsonGenericDevice(genericeDevice));	
				
		        } catch (Exception e) {
		        	jTSingleResponse = new JTableSingleResponse("ERROR", "Error getting Device By DevicesId");
		            log.error("JSON ERROR getting GenericsDevices By Id", e);	            
		        }
		        
	        return jTSingleResponse;
	    }
	

		@RequestMapping(value="product.testRunPlan.add",method=RequestMethod.POST ,produces="application/json")
		 public @ResponseBody JTableSingleResponse addTestRunPlan(HttpServletRequest req,@RequestParam Integer testrunplanId,@RequestParam Map<String, String>  mapData){
			JTableSingleResponse jTableSingleResponse = null;
			Integer testRunPlanId=0;
			try {  
				String errorMessage=null;
				TestFactory testFactory = null;
				ProductMaster productMaster = null;				
				String remarks = "";
				TestRunPlan testRunPlan=new TestRunPlan();
				log.debug("product.testRunPlan.add"+"testrunplanId==>"+testrunplanId);
				ProductVersionListMaster productVersionListMaster= productListService.getProductVersionListMasterById(Integer.parseInt(mapData.get("productVersionId")));
				boolean update = false;
				if(testrunplanId!= null && testrunplanId!=0 ){
					 testRunPlan=productListService.getTestRunPlanBytestRunPlanId(testrunplanId);
					 productMaster = testRunPlan.getProductVersionListMaster().getProductMaster();
					 testFactory = productMaster.getTestFactory();
					 remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", TestRunPlan :"+testRunPlan.getTestRunPlanName();
					 if(testRunPlan != null && testRunPlan.getTestRunPlanId() != null){
						 update = true;
					 }
					 if(!testRunPlan.getTestRunPlanName().equals(mapData.get("name"))){
						  errorMessage=commonService.duplicateName(mapData.get("name"), "TestRunPlan", "testRunPlanName", "Test Run Plan", "productVersionListMaster.productVersionListId="+productVersionListMaster.getProductVersionListId());
					 }
				}
				
				if(testrunplanId == null || testrunplanId==0){
					 errorMessage=commonService.duplicateName(mapData.get("name"), "TestRunPlan", "testRunPlanName", "Test Run Plan", "productVersionListMaster.productVersionListId="+productVersionListMaster.getProductVersionListId());
					testRunPlan.setCreatedDate(DateUtility.getCurrentTime());
				}
				
				testRunPlan.setDescription(mapData.get("description"));
				if(mapData.get("executionType")!=""){
					ExecutionTypeMaster exeTypeMaster=executionTypeMasterService.getExecutionTypeByExecutionTypeId(Integer.parseInt(mapData.get("executionType")));
					testRunPlan.setExecutionType(exeTypeMaster);
				}
				
				testRunPlan.setNotifyByMail(mapData.get("notifyByMail"));
				testRunPlan.setProductVersionListMaster(productVersionListMaster);
				testRunPlan.setStatus(1);
				testRunPlan.setTestRunPlanName(mapData.get("name"));
				testRunPlan.setTestToolMaster(testRunConfigSer.getTestToolMaster(Integer.parseInt(mapData.get("testToolMasterId"))));
				Integer postbugs = Integer.parseInt(mapData.get("autoPostBugs"));
				testRunPlan.setAutoPostBugs(postbugs);
				Integer postresults = Integer.parseInt(mapData.get("autoPostResults"));
				testRunPlan.setAutoPostResults(postresults);
				String cornSchedule=mapData.get("cronSchedule");
				testRunPlan.setTestRunCronSchedule(cornSchedule);
				testRunPlan.setTestScriptSource(mapData.get("testScriptSource"));
				testRunPlan.setTestSuiteScriptFileLocation(mapData.get("testSuiteScriptFileLocation"));
				testRunPlan.setMultipleTestSuites(mapData.get("isMultipleTestSuite"));
				testRunPlan.setTestScriptsLevel(mapData.get("testScriptLevel"));
				String testRunschedule=mapData.get("testRunSchedule");
				
				ProductBuild productBuild = null;
				if(mapData.get("productBuildId") != null){
					//Set Product Build
					productBuild = productListService.getProductBuildById(Integer.parseInt(mapData.get("productBuildId")), 0);
				}
				testRunPlan.setProductBuild(productBuild);			
				
				//Set useiSETestPlan to Test Run Plan
				if(mapData.get("useISETestPlan") != null){
					testRunPlan.setUseIntelligentTestPlan(mapData.get("useISETestPlan"));
				} else {
					testRunPlan.setUseIntelligentTestPlan("NO");
				}
				
				//Set Scrpt Type Master to Test Run Plan for Multiple Test Suites implementation
				if(mapData.get("testScriptType")!=""){
					ScriptTypeMaster scriptTypeMaster=testSuiteConfigurationService.getScriptTypeMasterByscriptType(mapData.get("testScriptType"));
					testRunPlan.setScriptTypeMaster(scriptTypeMaster);
				}
				//log.info("testRunschedule:"+testRunschedule);
				if(testRunschedule!=null && testRunschedule!=""){
					String temp[]=testRunschedule.split("-");
					String startTime=temp[0];
					String endTime=temp[1];
					//	
					testRunPlan.setTestRunScheduledStartTime(DateUtility.dateformatWithOutTime(temp[0]));
					testRunPlan.setTestRunScheduledEndTime(DateUtility.dateformatWithOutTime(temp[1]));
				}else{
					testRunPlan.setTestRunScheduledStartTime(null);
					testRunPlan.setTestRunScheduledEndTime(null);
				}
				log.info("At addTestRunPlan errorMessage"+errorMessage);
				if (errorMessage != null) {					
					jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
					return jTableSingleResponse;
				}
				 UserList user=(UserList)req.getSession().getAttribute("USER");
				 testRunPlan.setUserList(user);
				 
				 //Add Automation mode to testrun plan
				if (mapData.get("automationMode") != null) {
					testRunPlan.setAutomationMode(mapData.get("automationMode"));
				} else {
					testRunPlan.setAutomationMode("ATTENDED");
				}
				
				//Set Results Reporting mode to Test Plan
				if(mapData.get("resultsReportingMode") != null){
					testRunPlan.setResultsReportingMode(mapData.get("resultsReportingMode"));
				} else {
					testRunPlan.setResultsReportingMode("DISCRETE JOBS");
				}
				
				if(mapData.get("tpRunConfig") != null && mapData.get("resultsReportingMode").equalsIgnoreCase("COMBINED JOB")){
					RunConfiguration combinedRunConfiguration = new RunConfiguration();
					combinedRunConfiguration.setRunconfigId(Integer.parseInt(mapData.get("tpRunConfig")));
					testRunPlan.setCombinedResultsRunConfiguration(combinedRunConfiguration);
				} else if(!mapData.get("resultsReportingMode").equalsIgnoreCase("COMBINED JOB")){					
					testRunPlan.setCombinedResultsRunConfiguration(null);
				}
				
				 testRunPlanId=productListService.addTestRunplan(testRunPlan);
				 if(update){
					 eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_TEST_RUN_PLAN, testRunPlanId, testRunPlan.getTestRunPlanName(),
							 null, null, null, null, user, remarks);
				 }else{//Add
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_TEST_RUN_PLAN, testRunPlanId, testRunPlan.getTestRunPlanName(), user);
				 }
				 
				 boolean isScheduled=false;
				 if(cornSchedule!=null && cornSchedule!=""){	
						String cronErrorMessage = CronValidate.validQuartzCron(cornSchedule);
						String error = null;
						
						if (cronErrorMessage != null)
							error =  cronErrorMessage;
						if (error != null) {
							jTableSingleResponse = new JTableSingleResponse("ERROR",error);
							return jTableSingleResponse;
						}else{
							isScheduled=true;
						}
					}

				 try {
					 log.info("At addTestRunPlan isScheduled>>>>>"+isScheduled);
						if(isScheduled){
							TAFScheduleManager tafScheduleManager = TAFScheduleManager.getTAFScheduleManager();
							log.info("inside the !isAdhoc if condition TestConfigchild id:"+testRunPlan.getTestRunPlanId());
							tafScheduleManager.createTestConfigSchedule(createTestConfigScheduleEntity(testRunPlan));
							jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestRunPlan(testRunPlan));	
						}else{
							// added for because of the isScheduled data null, its not working so we added here
							jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestRunPlan(testRunPlan));	
						}
			   		} catch (ATFException e) {
			            jTableSingleResponse = new JTableSingleResponse("ERROR","Test Configuration Created.Error in CRON Expression.Correct and Resave using EDIT option.");
			            log.error("Quartz Scheduler Error : Unable to create schedule for Test Configuration", e);	 
			  		} catch (Exception e) {
			  			
			            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to create schedule for Test Configuration!");
			            log.error("Quartz Scheduler Error : Unable to create schedule for Test Configuration", e);	 
			  		}
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding TestRunPlan!");
		            log.error("JSON ERROR add TestRunPlan", e);
		        }
	        return jTableSingleResponse;
	    }
		
		/*
		 * Create a ScheduleEntity object from the testconfigchild data object
		 */
		private TestConfigScheduleEntity createTestConfigScheduleEntity(TestRunPlan testRunPlan) {
			
			//Convert the object for creating a new schedule
			TestConfigScheduleEntity entity = new TestConfigScheduleEntity();
			entity.setProductId(testRunPlan.getProductVersionListMaster().getProductMaster().getProductId().toString());
			entity.setProductName(testRunPlan.getProductVersionListMaster().getProductMaster().getProductName());
			entity.setProductVersionId(testRunPlan.getProductVersionListMaster().getProductVersionListId().toString());
			entity.setProductVersionName(testRunPlan.getProductVersionListMaster().getProductVersionName());
			entity.setTestConfigName(testRunPlan.getTestRunPlanName());
			entity.setTestConfigDescription(testRunPlan.getDescription());
			entity.setTestConfigId(testRunPlan.getTestRunPlanId().toString());
			entity.setScheduleCronExpression(testRunPlan.getTestRunCronSchedule());
			entity.setJobClassName("com.hcl.atf.taf.scheduler.jobs.DefaultTestConfigCycleJob");
			entity.setStartDate(testRunPlan.getTestRunScheduledStartTime());
			entity.setEndDate(testRunPlan.getTestRunScheduledEndTime());
			return entity;
		}
		
		@RequestMapping(value="administration.runConfiguration.listbyproductversion",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listRunConfigurationByProductVersion(@RequestParam int productversionId,@RequestParam Integer productId ,@RequestParam Integer environmentCombinationId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize,@RequestParam Integer testRunPlanId,@RequestParam Integer workpackageId) {			
			JTableResponse jTableResponse=null;;			 
			try {
				WorkPackage workPackage=null;
				TestRunPlan testRunPlan=null;
				
				Set<RunConfiguration> runConfigurationsList=null;
				List<JsonRunConfiguration> jsonRunConfigurationListSQL = null;
				if(testRunPlanId!=-1){
					jsonRunConfigurationListSQL  = environmentService.getMappedHostListFromRunconfigurationTestRunPlanLevel(environmentCombinationId, testRunPlanId,1);
					jTableResponse = new JTableResponse("OK", jsonRunConfigurationListSQL,jsonRunConfigurationListSQL.size());
					jsonRunConfigurationListSQL = null;					
				
				}
				
				if(workpackageId!=-1){
					jsonRunConfigurationListSQL  = environmentService.getMappedHostListFromRunconfigurationWorkPackageLevel(environmentCombinationId,  workpackageId,1);
					jTableResponse = new JTableResponse("OK", jsonRunConfigurationListSQL,jsonRunConfigurationListSQL.size());
					jsonRunConfigurationListSQL = null;	
				}			
		
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show RunConfiguration!");
		            log.error("JSON ERROR listing RunConfiguration By ProductVersion", e);
		        }		        
	        return jTableResponse;
	    }
		@RequestMapping(value="administration.genericdevice.mappedList",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listRunConfigurationOfDeviceByProductVersion(@RequestParam int productversionId,@RequestParam Integer productId ,@RequestParam Integer environmentCombinationId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize,@RequestParam Integer testRunPlanId,@RequestParam Integer workpackageId) {			
			JTableResponse jTableResponse=null;;			 
			try {
				Set<RunConfiguration> runConfigurationsList=null;
				List<JsonRunConfiguration> jsonRunConfigurationListSQL = null;
				if(testRunPlanId!=-1){
					jsonRunConfigurationListSQL = deviceListService.getMappedGenericDeviceFromRunconfigurationTestRunPlanLevel(environmentCombinationId, testRunPlanId, 1);
					jTableResponse = new JTableResponse("OK", jsonRunConfigurationListSQL,jsonRunConfigurationListSQL.size());
					jsonRunConfigurationListSQL = null;
				}
				if(workpackageId!=-1){
					jsonRunConfigurationListSQL  = deviceListService.getMappedGenericDeviceFromRunconfigurationWorkPackageLevel(environmentCombinationId,  workpackageId,1);
					jTableResponse = new JTableResponse("OK", jsonRunConfigurationListSQL,jsonRunConfigurationListSQL.size());
					jsonRunConfigurationListSQL = null;	
				}			
		
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show RunConfiguration Of Device By ProductVersion!");
		            log.error("JSON ERROR listing RunConfiguration Of Device By ProductVersion", e);
		        }		        
	        return jTableResponse;
	    }
		
		@RequestMapping(value="administration.testsuite.maprunConfiguration",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse mapRunConfigurationTestSuites(@RequestParam int productversionId,@RequestParam Integer productId ,@RequestParam Integer testRunPlanId,@RequestParam Integer testSuiteId,@RequestParam String type) {			
			JTableResponse jTableResponse=null;;			 
			try {
	            	   productListService.mapTestRunPlanWithTestSuite(testRunPlanId, testSuiteId,type);
	            	   if(type.equals("Add")){
	            		   jTableResponse = new JTableResponse("SUCCESS","Test Suite successfully mapped to TestRunPlan");
	            	   }else{
	            		   jTableResponse = new JTableResponse("SUCCESS","Test Suite successfully unmapped from TestRunPlan");
	            	   }
			} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to map test suite!");
		            log.error("JSON ERROR mapping RunConfiguration TestSuites", e);
		        }		        
	        return jTableResponse;
	    }
		
		@RequestMapping(value="administration.testsuite.mapunmaprunConfiguration",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse mapUnmapRunConfigurationTestSuites(@RequestParam int productversionId,@RequestParam Integer productId ,@RequestParam Integer testRunPlanId,@RequestParam Integer testSuiteId,@RequestParam String type) {			
			JTableResponse jTableResponse=null;
			try {
					List<Integer> testSuites = productListService.listTestSuites(testRunPlanId);
            	   productListService.mapTestRunPlanWithTestSuite(testRunPlanId, testSuiteId,type);
            	   if(type.equals("Add")){
            		   jTableResponse = new JTableResponse("SUCCESS","Test Suite successfully mapped to Test Plan");
            	   }else{
            		   jTableResponse = new JTableResponse("SUCCESS","Test Suite successfully unmapped from Test Plan");
            	   }
			} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to map test suite!");
		            log.error("JSON ERROR map/Unmap RunConfigurationTestSuites", e);
		        }		        
	        return jTableResponse;
	    }

		
		@RequestMapping(value="administration.feature.mapTestRunPlan",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse mapTestRunPlanFeatures(@RequestParam int productversionId,@RequestParam Integer productId ,@RequestParam Integer testRunPlanId,@RequestParam Integer featureId,@RequestParam String type) {			
			JTableResponse jTableResponse=null;;			 
			try {
	            	   productListService.mapTestRunPlanWithFeature(testRunPlanId, featureId,type);
	            	   if(type.equals("Add")){
	            		   jTableResponse = new JTableResponse("SUCCESS","Feature successfully mapped to TestRunPlan");
	            	   }else{
	            		   jTableResponse = new JTableResponse("SUCCESS","Feature successfully unmapped from TestRunPlan");
	            	   }
			} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to map Feature to TestRunPlan!");
		            log.error("JSON ERROR : Unable to map Feature to TestRunPlan", e);
		        }		        
	        return jTableResponse;
	    }

		
		@RequestMapping(value="administration.testCase.mapTestRunPlan",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse mapTestRunPlanTestcase(@RequestParam int productversionId,@RequestParam Integer productId ,@RequestParam Integer testRunPlanId,@RequestParam Integer testCaseId,@RequestParam String type) {			
			JTableResponse jTableResponse=null;;			 
			try {
	            	   productListService.mapTestRunPlanWithTestCase(testRunPlanId, testCaseId,type);
	            	   if(type.equals("Add")){
	            		   jTableResponse = new JTableResponse("SUCCESS","Test case successfully mapped to TestRunPlan");
	            	   }else{
	            		   jTableResponse = new JTableResponse("SUCCESS","Test case successfully unmapped from TestRunPlan");
	            	   }
			} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to map testcase!");
		            log.error("JSON ERROR : Unable to map TestRunPlan Testcase", e);
		        }		        
	        return jTableResponse;
	    }

		@RequestMapping(value="administration.testsuite.testcase.maptestrunplan",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse mapTestSuiteTestCasesTestRunPlan(@RequestParam int testCaseId,@RequestParam Integer testRunPlanId,@RequestParam Integer testSuiteId,@RequestParam String type) {			
			JTableResponse jTableResponse=null;;			 
			try {
	            	   productListService.mapTestSuiteTestCasesTestRunPlan(testRunPlanId, testSuiteId,testCaseId,type);
	            	   if(type.equals("Add")){
	            		   jTableResponse = new JTableResponse("SUCCESS","Selected Test Case successfully mapped to Test Plan");
	            	   }else{
	            		   jTableResponse = new JTableResponse("SUCCESS","Selected Test Case successfully unmapped from Test Plan");
	            	   }
			} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to map test case!");
		            log.error("JSON ERROR : Unable to map TestSuite TestCases TestRunPlan", e);
		        }		        
	        return jTableResponse;
	    }
		
		@RequestMapping(value="administration.device.maprunConfiguration.workpackage",method=RequestMethod.POST ,produces="application/json")
        public @ResponseBody JTableResponse mapRunConfigurationDevices(@RequestParam Integer environmentCombinationId,@RequestParam Integer deviceId,@RequestParam String type,@RequestParam Integer workpackageId,@RequestParam Integer runConfigId) {                  
			
			JTableResponse jTableResponse=null;;                  
			try {
				WorkPackage workPackage= workPackageService.getWorkPackageById(workpackageId);
				ProductVersionListMaster productVersionListMaster = workPackage.getProductBuild().getProductVersion();
				ProductMaster productMaster=workPackage.getProductBuild().getProductVersion().getProductMaster();
				JsonRunConfiguration jsonrunConfig = new JsonRunConfiguration();
				List<JsonRunConfiguration> jsonrunConfigList = new ArrayList<JsonRunConfiguration>();
				if(type.equals("map")){                    
                	RunConfiguration runConfigurationOfGenDeviceExistingForWP = null;                    
                    //check for existing Runconfiguration(withGenDeviceOfWP) entry, if available, then modify                   
                	runConfigurationOfGenDeviceExistingForWP = deviceListService.isRunConfigurationOfGenDeviceOfWPExisting(environmentCombinationId, workpackageId, deviceId);
                	
                	if(runConfigurationOfGenDeviceExistingForWP != null && runConfigurationOfGenDeviceExistingForWP.getRunconfigId() != null && runConfigurationOfGenDeviceExistingForWP.getRunconfigId() != 0){//existing, hence modify or update Status to 1                    	
                		runConfigurationOfGenDeviceExistingForWP.setRunConfigStatus(1);//Set Status 1 for Mapping
                    	environmentService.updateRunConfiguration(runConfigurationOfGenDeviceExistingForWP);
                    	jsonrunConfig = new JsonRunConfiguration(runConfigurationOfGenDeviceExistingForWP);
                    }else{//Newly adding RunConf with GenDevice Of WP                    	
                    	 RunConfiguration runConfigurationObj=new RunConfiguration();
                         EnvironmentCombination environmentCombination= environmentService.getEnvironmentCombinationById(environmentCombinationId);
                         runConfigurationObj.setEnvironmentcombination(environmentCombination);
                         runConfigurationObj.setProduct(productMaster);
                         runConfigurationObj.setProductVersion(productVersionListMaster);                         
                         GenericDevices  genericDevices = environmentService.getGenericDevices(deviceId);
                         String deviceName=genericDevices.getName();
                         
                         runConfigurationObj.setGenericDevice(genericDevices);
                         String name=environmentCombination.getEnvironmentCombinationName()+":"+deviceName;
                         runConfigurationObj.setRunconfigName(name);
                         runConfigurationObj.setWorkPackage(workPackage);
                         runConfigurationObj.setRunConfigStatus(1);//Set Status 1 for Mapping
                         RunConfiguration runConfiguration=environmentService.addRunConfiguration(runConfigurationObj);
                         if(runConfiguration != null){
                       		 jsonrunConfig = new JsonRunConfiguration(runConfigurationObj);
                       	 	}                     
                    }                    
                 }else if(type.equals("unmap")){                	 
                	    RunConfiguration runConfiguration=productListService.getRunConfigurationById(runConfigId);
                        //environmentService.deleteRunConfiguration(runConfiguration);
                	    runConfiguration.setRunConfigStatus(0);
                        environmentService.updateRunConfiguration(runConfiguration);
                        jsonrunConfig = new JsonRunConfiguration(runConfiguration); 
                 }  
                //jTableResponse = new JTableResponse("OK");
                jsonrunConfigList.add(jsonrunConfig);    
                jTableResponse = new JTableResponse("OK", jsonrunConfigList);
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR","Unable to Map/UnMap RunConfiguration!");
				log.error("JSON ERROR : Unable to Map/UnMap RunConfiguration", e);
			}                   
            	return jTableResponse;
    	}


		@RequestMapping(value="administration.device.maprunConfiguration.testRunPlan",method=RequestMethod.POST ,produces="application/json")
        public @ResponseBody JTableResponse mapRunConfigurationDevicesTestRunPlan(@RequestParam Integer ecid,@RequestParam Integer deviceId,@RequestParam String type,@RequestParam Integer testRunPlanId,@RequestParam Integer runConfigId) {                  
			JTableResponse jTableResponse=null;                  
			try {
				TestRunPlan testRunPlan =productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
				
				ProductVersionListMaster productVersionListMaster = testRunPlan.getProductVersionListMaster();
				ProductMaster productMaster=testRunPlan.getProductVersionListMaster().getProductMaster();
				
				if(type.equals("map")){
                    RunConfiguration runConfigurationObj=new RunConfiguration();
                    EnvironmentCombination environmentCombination= environmentService.getEnvironmentCombinationById(ecid);
                    runConfigurationObj.setEnvironmentcombination(environmentCombination);
                    runConfigurationObj.setProduct(productMaster);
                    runConfigurationObj.setProductVersion(productVersionListMaster);
                    
                    GenericDevices  genericDevices = environmentService.getGenericDevices(deviceId);
                    String deviceName=genericDevices.getName();
                    
                    runConfigurationObj.setGenericDevice(genericDevices);
                    String name=environmentCombination.getEnvironmentCombinationName()+":"+deviceName;
                    runConfigurationObj.setRunconfigName(name);
                    runConfigurationObj.setTestRunPlan(testRunPlan);
                    runConfigurationObj.setRunConfigStatus(1);
                    
                    RunConfiguration runConfiguration=environmentService.addRunConfiguration(runConfigurationObj);
                    
                 }else if(type.equals("unmap")){                	 
                	    RunConfiguration runConfiguration=productListService.getRunConfigurationById(runConfigId);
                	    runConfiguration.setRunConfigStatus(0);
                        environmentService.deleteRunConfiguration(runConfiguration);
                 }                    
                jTableResponse = new JTableResponse("OK");
       
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR","Unable to show RunConfiguration!");
				log.error("JSON ERROR : Unable to map RunConfiguration Devices TestRunPlan", e);
			}                   
            	return jTableResponse;
    	}


		@RequestMapping(value="testing",method=RequestMethod.POST ,produces="application/json")
        public @ResponseBody JTableResponse testing(@RequestParam Integer ecid,@RequestParam Integer deviceId,@RequestParam String type,@RequestParam Integer testRunPlanId,@RequestParam Integer runConfigId) {                  
			JTableResponse jTableResponse=null;                  
			try {
				TestRunPlan testRunPlan =productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
				
				ProductVersionListMaster productVersionListMaster = testRunPlan.getProductVersionListMaster();
				ProductMaster productMaster=testRunPlan.getProductVersionListMaster().getProductMaster();
				JsonRunConfiguration jsonrunConfig = new JsonRunConfiguration();
				List<JsonRunConfiguration> jsonrunConfigList = new ArrayList<JsonRunConfiguration>();
				if(type.equals("map")){
                    
                    RunConfiguration runConfigurationExisting = null;                    
                    //check for existing Runconfiguration entry, if available, then modify                   
                    runConfigurationExisting = environmentService.isRunConfigurationOfDeviceOfTestRunExisting(ecid, testRunPlanId,deviceId, productVersionListMaster.getProductVersionListId());                    
                    
                    if(runConfigurationExisting != null && runConfigurationExisting.getRunconfigId() != null && runConfigurationExisting.getRunconfigId() != 0){//existing, hence modify or update Status to 1                    	
                    	runConfigurationExisting.setRunConfigStatus(1);
                    	environmentService.updateRunConfiguration(runConfigurationExisting);
                    	jsonrunConfig = new JsonRunConfiguration(runConfigurationExisting);
                    	
                    }else{//Newly adding
                    	RunConfiguration runConfigurationObj=new RunConfiguration();
                        EnvironmentCombination environmentCombination= environmentService.getEnvironmentCombinationById(ecid);
                        runConfigurationObj.setEnvironmentcombination(environmentCombination);
                        runConfigurationObj.setProduct(productMaster);
                        runConfigurationObj.setProductVersion(productVersionListMaster);
                        runConfigurationObj.setRunConfigStatus(1);
                        GenericDevices  genericDevices = environmentService.getGenericDevices(deviceId);
                        String deviceName=genericDevices.getName();
                        
                        runConfigurationObj.setGenericDevice(genericDevices);
                        String name=environmentCombination.getEnvironmentCombinationName()+":"+deviceName;
                        runConfigurationObj.setRunconfigName(name);
                        runConfigurationObj.setTestRunPlan(testRunPlan);
                        
                    	 runConfigurationExisting=environmentService.addRunConfiguration(runConfigurationObj);
                    	 if(runConfigurationExisting != null){
                    		 jsonrunConfig = new JsonRunConfiguration(runConfigurationObj);
                    	 }
                    }
                 }else if(type.equals("unmap")){
                	 
                	    RunConfiguration runConfiguration=productListService.getRunConfigurationById(runConfigId);
                	    runConfiguration.setRunConfigStatus(0);
                        environmentService.updateRunConfiguration(runConfiguration);
                        jsonrunConfig = new JsonRunConfiguration(runConfiguration);                        
                 }
				jsonrunConfigList.add(jsonrunConfig);    
                jTableResponse = new JTableResponse("OK", jsonrunConfigList);
       
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR","Unable to show RunConfiguration!");
				log.error("JSON ERROR : Unable to show RunConfiguration", e);
			}                   
            	return jTableResponse;
    	}

		@RequestMapping(value="administration.maprunConfiguration.workpackage",method=RequestMethod.POST ,produces="application/json")
        public @ResponseBody JTableResponse mapRunConfigurationWorkpackage(@RequestParam Integer environmentCombinationId,@RequestParam String type,@RequestParam Integer workpackageId) {                  
			JTableResponse jTableResponse=null;;                  
			try {
				WorkPackage workPackage= workPackageService.getWorkPackageById(workpackageId);
				ProductVersionListMaster productVersionListMaster = workPackage.getProductBuild().getProductVersion();
				ProductMaster productMaster=workPackage.getProductBuild().getProductVersion().getProductMaster();
				Set<RunConfiguration> runConfigurationForWP= new HashSet<RunConfiguration>();
				
				runConfigurationForWP=productListService.getRunConfigurationListByWP(workpackageId,productMaster.getProductType().getProductTypeId(),environmentCombinationId);

				if(runConfigurationForWP!=null && !runConfigurationForWP.isEmpty()){
					runConfigurationForWP=productListService.getRunConfigurationListOfWPrcStatus(workpackageId, 1);
				}else{
					RunConfiguration runConfigurationObj=new RunConfiguration();
                    EnvironmentCombination environmentCombination= environmentService.getEnvironmentCombinationById(environmentCombinationId);
                    runConfigurationObj.setEnvironmentcombination(environmentCombination);
                    runConfigurationObj.setProduct(productMaster);
                    runConfigurationObj.setProductVersion(productVersionListMaster);
                    String name=environmentCombination.getEnvironmentCombinationName();
                    runConfigurationObj.setRunconfigName(name);
                    runConfigurationObj.setWorkPackage(workPackage);
                    RunConfiguration runConfiguration=environmentService.addRunConfiguration(runConfigurationObj);
    				//Inserting in MongoDB for ISE
                    if(MONGODB_AVAILABLE.equalsIgnoreCase("YES"))
                    	testBedsMongoDAO.save(runConfiguration);
                    runConfigurationForWP.add(runConfiguration);
                    
				}
				Set<RunConfiguration> runConfigurationByEC=new HashSet<RunConfiguration>();
				for(RunConfiguration rc:runConfigurationForWP){
					if(rc.getEnvironmentcombination().getEnvironment_combination_id().equals(environmentCombinationId)){
						runConfigurationByEC.add(rc);
					}
				}
				if(type.equals("map")){
                    Set<WorkPackageTestSuite> wpts=workPackage.getWorkPackageTestSuites();
                    WorkpackageRunConfiguration wpRunConfiguration =null;
                    WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan =null;
                    List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate= new ArrayList<WorkPackageTestCaseExecutionPlan>();
                    TestSuiteList testSuiteList=null;
    				WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan= null;
    				Set<TestCaseList> testCaseListByTS=null;

    				for(RunConfiguration runConfiguration:runConfigurationByEC){
                    	workPackageService.addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"testsuite");
                        
                    	wpRunConfiguration=workPackageService.getWorkpackageRunConfiguration(workPackage.getWorkPackageId(), runConfiguration.getRunconfigId(), "testsuite");
	                
	                    for(WorkPackageTestSuite wpTestSuite:wpts){
	                    	
	                    	if(wpTestSuite.getIsSelected()==1){
	                    		testSuiteList=wpTestSuite.getTestSuite();
	                    		TestRunJob testRunJob=workPackageService.getTestRunJobByWP(workPackage, runConfiguration);
	                    		if(testRunJob!=null){
                    				workPackageService.mapTestRunJobTestSuite(testRunJob.getTestRunJobId(), testSuiteList.getTestSuiteId(), "Add");
                    			}else{
                    				workPackageService.addTestRunJob(runConfiguration, null, workPackage, null);
                    				testRunJob=workPackageService.getTestRunJobByWP(workPackage, runConfiguration);
                    				workPackageService.mapTestRunJobTestSuite(testRunJob.getTestRunJobId(), testSuiteList.getTestSuiteId(), "Add");
                    				if(testRunJob!=null){
    									if(testRunJob.getTestRunJobId()!=null)
    									mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
    								}

                    				
                    			}
	                    		
	                    		if(testSuiteList!=null){
	                    			workPackageTestSuiteExecutionPlan= new WorkPackageTestSuiteExecutionPlan();
	        						workPackageTestSuiteExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());
	        						workPackageTestSuiteExecutionPlan.setTestsuite(testSuiteList);
	        						workPackageTestSuiteExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
	        						workPackageTestSuiteExecutionPlan.setPlannedExecutionDate(workPackage.getPlannedStartDate());
	        						workPackageTestSuiteExecutionPlan.setRunConfiguration(wpRunConfiguration);
	        						workPackageTestSuiteExecutionPlan.setWorkPackage(workPackage);
	        						workPackageTestSuiteExecutionPlan.setStatus(1);
	        						workPackageTestSuiteExecutionPlan.setTestRunJob(testRunJob);
	        						ExecutionPriority executionPriority=null;
	        						if(wpTestSuite.getTestSuite().getExecutionPriority()!=null)
	        							executionPriority= workPackageService.getExecutionPriorityByName(CommonUtility.getExecutionPriority(wpTestSuite.getTestSuite().getExecutionPriority().getPriorityName()));
	        						else
	        							executionPriority= workPackageService.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
	        						workPackageTestSuiteExecutionPlan.setExecutionPriority(executionPriority);
	        						
	        						workPackageService.addWorkpackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlan);
	                    			testCaseListByTS=testSuiteList.getTestCaseLists();
	                    			
	                    			for(TestCaseList testCaseList :testCaseListByTS){
	                    				if (testCaseList != null){
	                    					testCaseList = testCaseService.getTestCaseById(testCaseList.getTestCaseId());
	                    					workPackageTestCaseExecutionPlan=workPackageService.addWorkpPackageTestCaseExecutionPlans(testCaseList, testSuiteList, workPackage, wpRunConfiguration,null,"TestSuite",testRunJob);
	                    					workPackageTestCaseExecutionPlanListForUpdate.add(workPackageTestCaseExecutionPlan);
	                    				}
	                    			}
	                    		}     
	                       }
	                    }
                    }
                    workPackageService.addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);

                    //Feature changes
                    workPackageTestCaseExecutionPlan =null;
                    workPackageTestCaseExecutionPlanListForUpdate.clear();
                    Set<TestCaseList> testCaseByFeature=null;
                    Set<TestCaseList> childTestCaseByFeature=null;
                    
                    Set<WorkPackageFeature> wpf=workPackage.getWorkPackageFeature();
                    WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan =null;
                    List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlanListForUpdate= new ArrayList<WorkPackageFeatureExecutionPlan>();
                    ProductFeature feature=null;
                    for(RunConfiguration runConfiguration:runConfigurationByEC){
                    	workPackageService.addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"feature");
                        
                    	wpRunConfiguration=workPackageService.getWorkpackageRunConfiguration(workPackage.getWorkPackageId(), runConfiguration.getRunconfigId(), "feature");
                    	
	                    for(WorkPackageFeature wpFeature:wpf){
	                    	
	                    	if(wpFeature.getIsSelected()==1){
	                    		feature=wpFeature.getFeature();
	                    		TestRunJob testRunJob=workPackageService.getTestRunJobByWP(workPackage, runConfiguration);
	                    		if(testRunJob!=null){
                    				workPackageService.mapTestRunJobFeature(testRunJob.getTestRunJobId(), feature.getProductFeatureId(), "Add");
                    			}else{
                    				workPackageService.addTestRunJob(runConfiguration, null, workPackage, null);
                    				testRunJob=workPackageService.getTestRunJobByWP(workPackage, runConfiguration);
                    				workPackageService.mapTestRunJobFeature(testRunJob.getTestRunJobId(), feature.getProductFeatureId(), "Add");
                    				if(testRunJob!=null){
    									if(testRunJob.getTestRunJobId()!=null)
    									mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
    								}
                    			}
	                    		
	                    		if(feature!=null){
	                    			workPackageFeatureExecutionPlan=new WorkPackageFeatureExecutionPlan();
                					workPackageFeatureExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());
    								workPackageFeatureExecutionPlan.setFeature(feature);
    								workPackageFeatureExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
    								workPackageFeatureExecutionPlan.setPlannedExecutionDate(workPackage.getPlannedStartDate());
    								workPackageFeatureExecutionPlan.setRunConfiguration(wpRunConfiguration);
    								workPackageFeatureExecutionPlan.setWorkPackage(workPackage);
    								workPackageFeatureExecutionPlan.setStatus(1);
    								workPackageFeatureExecutionPlan.setTestRunJob(testRunJob);
    								ExecutionPriority executionPriority=null;
    								if(wpFeature.getFeature().getExecutionPriority()!=null)
    									executionPriority= workPackageService.getExecutionPriorityByName(CommonUtility.getExecutionPriority(wpFeature.getFeature().getExecutionPriority().getPriorityName()));
    								else
    									executionPriority= workPackageService.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
    								workPackageFeatureExecutionPlan.setExecutionPriority(executionPriority);
    								workPackageService.addWorkpackageFeatureExecutionPlan(workPackageFeatureExecutionPlan);
    								
    								Set<TestCaseList> featureTC=feature.getTestCaseList();
    								for(TestCaseList testCaseList :featureTC){
	                    				if (testCaseList != null){
	                    					testCaseList = testCaseService.getTestCaseById(testCaseList.getTestCaseId());
	                    					workPackageTestCaseExecutionPlan=workPackageService.addWorkpPackageTestCaseExecutionPlans(testCaseList, null, workPackage, wpRunConfiguration,feature,"Feature",testRunJob);
	    	                    			workPackageTestCaseExecutionPlanListForUpdate.add(workPackageTestCaseExecutionPlan);
	                    				}
	                    			}

	                    		}     
	                       }
	                    }
                    }
        			workPackageService.addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);

                    //Feature changes ends
                    Set<WorkPackageTestCase> wptc=workPackage.getWorkPackageTestCases();
                    workPackageTestCaseExecutionPlan =null;
                    workPackageTestCaseExecutionPlanListForUpdate.clear();
                    TestCaseList testCaseList=null;
                    testSuiteList=null;

                    for(RunConfiguration runConfiguration:runConfigurationByEC){
                        workPackageService.addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"testcase");

                    	wpRunConfiguration=workPackageService.getWorkpackageRunConfiguration(workPackage.getWorkPackageId(), runConfiguration.getRunconfigId(), "testcase");
	                    for(WorkPackageTestCase wpTestcase:wptc){
	                    	if(wpTestcase.getIsSelected()==1){
	                    		testCaseList=wpTestcase.getTestCase();
	                    		if (testCaseList != null){
	                    			TestRunJob testRunJob=workPackageService.getTestRunJobByWP(workPackage, runConfiguration);
	                    			if(testRunJob!=null){
	                    				workPackageService.mapTestRunJobTestCase(testRunJob.getTestRunJobId(), testCaseList.getTestCaseId(), "Add");
	                    			}else{
	                    				workPackageService.addTestRunJob(runConfiguration, null, workPackage, null);
	                    				testRunJob=workPackageService.getTestRunJobByWP(workPackage, runConfiguration);
	                    				workPackageService.mapTestRunJobTestCase(testRunJob.getTestRunJobId(), testCaseList.getTestCaseId(), "Add");
	                    				if(testRunJob!=null){
	    									if(testRunJob.getTestRunJobId()!=null)
	    									mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
	    								}
	                    				

	                    			}
	                    			testCaseList = testCaseService.getTestCaseById(testCaseList.getTestCaseId());
	                    			workPackageTestCaseExecutionPlan=workPackageService.addWorkpPackageTestCaseExecutionPlans(testCaseList, testSuiteList, workPackage, wpRunConfiguration,null,"TestCase",testRunJob);
	                    			workPackageTestCaseExecutionPlanListForUpdate.add(workPackageTestCaseExecutionPlan);
	                    		}
	                    	}
	                    }     
                    }
                    workPackageService.addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
                }
				else if(type.equals("unmap")){
					Integer runConfigId=-1;
					Set<TestCaseList> testCaseListSet=null;
					List<WorkPackageTestCaseExecutionPlan> wptceplist=new ArrayList<WorkPackageTestCaseExecutionPlan>();
					for(RunConfiguration runConfiguration:runConfigurationByEC){
                        runConfigId=runConfiguration.getRunconfigId();
                        TestRunJob testRunJob = workPackageService.getTestRunJobByWP(workPackage, runConfiguration);
                    	Set<WorkPackageTestCase> workPackageTestCases=workPackage.getWorkPackageTestCases();
                    	Set<WorkPackageTestSuite> workPackageTestSuites=workPackage.getWorkPackageTestSuites();
                    	Set<WorkPackageFeature> workPackageFeatures=workPackage.getWorkPackageFeature();

                    	//delete testcase starts
                    	for(WorkPackageTestCase wptc:workPackageTestCases){
                    		if(wptc.getIsSelected()==1){
                    			wptceplist = workPackageService.getWorkPackageTestcaseExecutionPlanByWorkPackageId(workPackage.getWorkPackageId(),wptc.getTestCase().getTestCaseId(),-1,-1,"TestCase",runConfiguration.getRunconfigId());
								workPackageService.deleteWorkPackageTestcaseExecutionPlan(wptceplist);
								
								workPackage=workPackageService.deleteRunConfigurationWorkpackage(workpackageId,runConfigId,"testcase");
						        if(testRunJob.getRunConfiguration().getRunconfigId().equals(runConfiguration.getRunconfigId())){
						        	workPackageService.mapTestRunJobTestCase(testRunJob.getTestRunJobId(), wptc.getTestCase().getTestCaseId(), "Remove");
						        }
                    		}
                    	}

                    	for(WorkPackageTestSuite wpts:workPackageTestSuites){
                    		if(wpts.getIsSelected()==1){
                    			WorkPackageTestSuiteExecutionPlan wptsep= workPackageService.getWorkpackageTestSuiteExecutionPlan(workPackage.getWorkPackageId(),wpts.getTestSuite().getTestSuiteId(),runConfiguration.getRunconfigId());
								if(wptsep.getRunConfiguration().getRunconfiguration().getRunconfigId().equals(runConfiguration.getRunconfigId())){
	    							workPackageService.deleteWorkpackageTestSuiteExecutionPlan(wptsep);
	    							 wptceplist = workPackageService.getWorkPackageTestcaseExecutionPlanByWorkPackageId(workPackage.getWorkPackageId(),-1,wpts.getTestSuite().getTestSuiteId(),-1,"TestSuite",runConfiguration.getRunconfigId());
	 								workPackageService.deleteWorkPackageTestcaseExecutionPlan(wptceplist);
								}
								workPackage=workPackageService.deleteRunConfigurationWorkpackage(workpackageId,runConfigId,"testsuite");
						        
								if(testRunJob.getRunConfiguration().getRunconfigId().equals(runConfiguration.getRunconfigId())){
									workPackageService.mapTestRunJobTestSuite(testRunJob.getTestRunJobId(), wpts.getTestSuite().getTestSuiteId(), "Remove");
    							}
                    		}
                        }
                    	for(WorkPackageFeature wpf:workPackageFeatures){
                    		if(wpf.getIsSelected()==1){
                    			WorkPackageFeatureExecutionPlan wpfep= workPackageService.getWorkpackageFeatureExecutionPlan(workPackage.getWorkPackageId(),wpf.getFeature().getProductFeatureId(),runConfiguration.getRunconfigId());
	    							workPackageService.deleteWorkpackageFeatureExecutionPlan(wpfep);
	    							wptceplist = workPackageService.getWorkPackageTestcaseExecutionPlanByWorkPackageId(workPackage.getWorkPackageId(),-1,-1,wpf.getFeature().getProductFeatureId(),"Feature",runConfiguration.getRunconfigId());
	 								workPackageService.deleteWorkPackageTestcaseExecutionPlan(wptceplist);
							        workPackage=workPackageService.deleteRunConfigurationWorkpackage(workpackageId,runConfigId,"feature");
								if(testRunJob.getRunConfiguration().getRunconfigId().equals(runConfiguration.getRunconfigId())){
                        			workPackageService.mapTestRunJobFeature(testRunJob.getTestRunJobId(), wpf.getFeature().getProductFeatureId(), "Remove");
    							}
                    		}
                        }
                    	//delete feature ends
                    	
						if(testRunJob.getRunConfiguration().getRunconfigId().equals(runConfiguration.getRunconfigId())){
                        		workPackage=workPackageService.deleteRunConfigurationWorkpackage(workpackageId,runConfigId,"testcase");
								workPackage=workPackageService.deleteRunConfigurationWorkpackage(workpackageId,runConfigId,"testsuite");
						        workPackage=workPackageService.deleteRunConfigurationWorkpackage(workpackageId,runConfigId,"feature");
	                        	if(testRunJob.getTestCaseListSet().isEmpty() && testRunJob.getTestSuiteSet().isEmpty()&& testRunJob.getFeatureSet().isEmpty() ){

	                        		environmentService.deleteTestRunJob(workpackageId,runConfigId,null);
		                            environmentService.deleteRunConfiguration(runConfiguration);
	                        	}
						}
					}
				}
				jTableResponse = new JTableResponse("OK");
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR","Unable to UnMap Environment Combination!");
				log.error("JSON ERROR : Unable to map RunConfiguration Workpackage", e);
			}                   
	        	return jTableResponse;
		} 
		
		
    	@RequestMapping(value="administration.maprunConfiguration.testRunPlan",method=RequestMethod.POST ,produces="application/json")
        public @ResponseBody JTableResponse mapRunConfigurationTestRunPlan(@RequestParam Integer environmentCombinationId,@RequestParam String type,@RequestParam Integer testRunPlanId,
        		@RequestParam Integer hostId, @RequestParam Integer deviceId) {                  
			JTableResponse jTableResponse=null;;                  
			try {
				TestRunPlan testRunPlan = productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
				ProductVersionListMaster productVersionListMaster=testRunPlan.getProductVersionListMaster();
				ProductMaster productMaster=productVersionListMaster.getProductMaster();
				Set<RunConfiguration> runConfigurationForTRP= new HashSet<RunConfiguration>();
				runConfigurationForTRP=productListService.getRunConfigurationList(testRunPlanId,productMaster.getProductType().getProductTypeId(),environmentCombinationId,hostId,deviceId);
				if(runConfigurationForTRP == null)	{
					RunConfiguration runConfigurationObj=new RunConfiguration();
                    EnvironmentCombination environmentCombination= environmentService.getEnvironmentCombinationById(environmentCombinationId);
                    runConfigurationObj.setEnvironmentcombination(environmentCombination);
                    runConfigurationObj.setProduct(productMaster);
                    runConfigurationObj.setProductVersion(productVersionListMaster);
                    String name=environmentCombination.getEnvironmentCombinationName();
                    runConfigurationObj.setRunconfigName(name);
                    runConfigurationObj.setTestRunPlan(testRunPlan);
                    RunConfiguration runConfiguration=environmentService.addRunConfiguration(runConfigurationObj);
                    
                    runConfigurationForTRP.add(runConfiguration);
				}
				Set<RunConfiguration> runConfigurationByEC=new HashSet<RunConfiguration>();
				for(RunConfiguration rc:runConfigurationForTRP){
					if(rc.getEnvironmentcombination().getEnvironment_combination_id().equals(environmentCombinationId)){
						runConfigurationByEC.add(rc);
					}
				}
				if(type.equals("map")){
                    for(RunConfiguration runConfiguration:runConfigurationByEC){
                    	productListService.mapTestRunPlanWithTestRunconfiguration(testRunPlanId, runConfiguration.getRunconfigId(), "Add");
                    }
                    jTableResponse = new JTableResponse("OK", "Run Configuration to Test Plan mapped successfully.");
                }else if(type.equals("unmap")){
                    for(RunConfiguration runConfiguration:runConfigurationByEC){
                    	productListService.mapTestRunPlanWithTestRunconfiguration(testRunPlanId, runConfiguration.getRunconfigId(), "Remove");
                    }
                    jTableResponse = new JTableResponse("OK", "Run Configuration to Test Plan unmapped successfully.");
                 }
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR","Unable to map or unmap RunConfiguration TestRunPlan!");
				log.error("JSON ERROR : Unable to map or unmap RunConfiguration TestRunPlan", e);
			}                   
	        	return jTableResponse;
		} 

    	
		@RequestMapping(value="administration.testrunplan.listbyproductversion",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listTestRunPlanByProductVersionId(@RequestParam int productversionId,@RequestParam Integer productId ,@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {			
			JTableResponse jTableResponse=null;;			 
			try {
				List<TestRunPlan> testRunPlanList =productListService.listTestRunPlanByProductVersionId(productversionId);
				List<JsonTestRunPlan> jsonTestRunPlans=new ArrayList<JsonTestRunPlan>();
				for(TestRunPlan testRunPlan: testRunPlanList){
					JsonTestRunPlan jsonTestRunPlan=new JsonTestRunPlan(testRunPlan);
					//jsonTestRunPlan.setStatus(0);
					jsonTestRunPlans.add(jsonTestRunPlan);
				}
				jTableResponse = new JTableResponse("OK", jsonTestRunPlans,jsonTestRunPlans.size());  
				
				jsonTestRunPlans = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to list TestRunPlan By ProductVersionId!");
		            log.error("JSON ERROR : Unable to list TestRunPlan By ProductVersionId", e);
		        }		        
	        return jTableResponse;
	    }
		
		@RequestMapping(value="administration.testrunplan.listbytestFactorProductorVersion",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listTestRunPlanBytestFactorProductorVersion(@RequestParam int productversionId,@RequestParam Integer productId, 
	    		@RequestParam Integer testFactoryId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {			
			JTableResponse jTableResponse=null;;			 
			try {
				List<TestRunPlan> testRunPlanList =productListService.listTestRunPlanBytestFactorProductorVersion(productversionId, productId, testFactoryId);
				List<JsonTestRunPlan> jsonTestRunPlans=new ArrayList<JsonTestRunPlan>();
				String avgExecutionTime = null;
				for(TestRunPlan testRunPlan: testRunPlanList){
					JTableResponse response = productListService.verificationTestPlanReadinessResult(testRunPlan.getTestRunPlanId());
					
					JsonTestRunPlan jsonTestRunPlan=new JsonTestRunPlan(testRunPlan);					
					if(response != null) {
						if(response.getMessage() != null){
							jsonTestRunPlan.setIsReady(response.getMessage());
						}
						
						if(response.getExportFileName() != null){
							jsonTestRunPlan.setMessage(response.getExportFileName());
						}
					}
					int executionTCCount = 0;
					int tcCount = 0;
					String totalTestCases;
					Set<TestSuiteList> testSuiteList = new HashSet<TestSuiteList>();
					Set<TestCaseList> totalTestCaseList =new HashSet<TestCaseList>();
					List<TestCaseList> testCaseList=new ArrayList<TestCaseList>();
					if(testRunPlan.getRunConfigurationList() != null){
						Set<RunConfiguration> rcList = testRunPlan.getRunConfigurationList();
						for(RunConfiguration rc : rcList){
							testSuiteList = rc.getTestSuiteLists();
							if (testSuiteList.size() > 0) {
								for (TestSuiteList ts : testSuiteList) {
									testCaseList = productListService.getRunConfigTestSuiteTestCaseMapped(rc.getRunconfigId(), ts.getTestSuiteId());
									executionTCCount = executionTCCount + testCaseList.size();
									for(TestCaseList tl : testCaseList)
										totalTestCaseList.add(tl);
								}
							}
							tcCount = 	totalTestCaseList.size();
						}
					}
					
					totalTestCases =  executionTCCount+"["+tcCount+"]";
					
					jsonTestRunPlan.setTotalTestCases(totalTestCases);
					avgExecutionTime = workPackageService.getExecutionTimeForEachWPByTPId(testRunPlan.getTestRunPlanId());
					jsonTestRunPlan.setAvgExecutionTime(avgExecutionTime);
					jsonTestRunPlans.add(jsonTestRunPlan);
				}
				
				jTableResponse = new JTableResponse("OK", jsonTestRunPlans,jsonTestRunPlans.size());  
				
				jsonTestRunPlans = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show TestRunPlan!");
		            log.error("JSON ERROR : Unable to list TestRunPlan BytestFactor ProductorVersion", e);
		        }		        
	        return jTableResponse;
	    }
		/* Product core Resources Show Tab*/
		@RequestMapping(value="product.tab.show.mode",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse showHideTab(@RequestParam Integer testFactoryId,@RequestParam Integer productId) {			
			JTableResponse jTableResponse = null;			 
			try {
				ProductMaster productShowHideTab=productListService.getProductShowHideTab(testFactoryId, productId);
				
				if (productShowHideTab != null){
					jTableResponse = new JTableResponse("OK","The Tab can show ");
				}else{
					jTableResponse = new JTableResponse("INFORMATION","The Tab cannot show");
				}
				
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error Hiding Tab!");
		            log.error("JSON ERROR : Hiding Tab", e);
		        }		        
	        return jTableResponse;
	    }

		@RequestMapping(value="testrunplan.runconfig.testRunPlan.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listTestRunPlanRC(@RequestParam int testRunPlanId) {
			log.debug("inside testrunplan.runconfig.testRunPlan.list");
			JTableResponse jTableResponse;
			 
			try {
						
				TestRunPlan testRunPlan=productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
				Set<RunConfiguration> runConfigurations=testRunPlan.getRunConfigurationList();//runConfigStatus implementation
				List<JsonRunConfiguration> jsonRunConfigurations= new ArrayList<JsonRunConfiguration>();
				for(RunConfiguration rc: runConfigurations){
					
					jsonRunConfigurations.add(new JsonRunConfiguration(rc));
					
				}
	            jTableResponse = new JTableResponse("OK", jsonRunConfigurations,jsonRunConfigurations.size());     
	            runConfigurations = null;

		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Run Configurations!");
		            log.error("JSON ERROR : Unable to show RunConfigurations of TestRunPlan", e);
		        }		        
	        return jTableResponse;
	    }

		@RequestMapping(value="testrunplan.runconfig.testRunPlanOfDeviceorHost.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listTestRunPlanRCOfDeviceorHost(@RequestParam int testRunPlanId, @RequestParam int prodType) {
			log.debug("inside testrunplan.runconfig.testRunPlanOfDeviceorHost.list");
			JTableResponse jTableResponse;			 
			try {
				if(prodType == -1){
					prodType =environmentService.getProductTypeByTestRunPlanId(testRunPlanId);
				}
				Set<RunConfiguration> runConfigurations=productListService.getRunConfigurationListOfTestRunPlanrcStatusDeviceorHost(testRunPlanId, 1, prodType);				
				List<JsonRunConfiguration> jsonRunConfigurations= new ArrayList<JsonRunConfiguration>();
				for(RunConfiguration rc: runConfigurations){					
					jsonRunConfigurations.add(new JsonRunConfiguration(rc));
				}
	            jTableResponse = new JTableResponse("OK", jsonRunConfigurations,jsonRunConfigurations.size());     
	            runConfigurations = null;

		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Run Configurations!");
		            log.error("JSON ERROR", e);
		        }		        
	        return jTableResponse;
	    }
		@RequestMapping(value="administration.host.mappedList",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listHostMappedList(@RequestParam int testRunPlanId,@RequestParam int workpackageId,@RequestParam Integer ecId) {
			log.debug("inside administration.host.mappedList");
			JTableResponse jTableResponse = null;			 
			try {				
				TestRunPlan testRunPlan=null;
				Set<RunConfiguration> runConfigurationsList=null;
				List<JsonRunConfiguration> jsonRunConfigurationListSQL = null;
				if(workpackageId!=-1){
					jsonRunConfigurationListSQL  = environmentService.getMappedHostListFromRunconfigurationWorkPackageLevel(ecId,  workpackageId,1);
					jTableResponse = new JTableResponse("OK", jsonRunConfigurationListSQL,jsonRunConfigurationListSQL.size());
					jsonRunConfigurationListSQL = null;				 
				}
				if(testRunPlanId!=-1){
					jsonRunConfigurationListSQL  = environmentService.getMappedHostListFromRunconfigurationTestRunPlanLevel(ecId, testRunPlanId,1);
					jTableResponse = new JTableResponse("OK", jsonRunConfigurationListSQL,jsonRunConfigurationListSQL.size());
					jsonRunConfigurationListSQL = null;					
				}

		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Host Details!");
		            log.error("JSON ERROR : Error listing Hosts Mapped", e);
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="administration.host.mapTerminal",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse mapTerminalHost(@RequestParam Integer testRunPlanId,@RequestParam String type,@RequestParam Integer runconfigId,@RequestParam Integer hostId,@RequestParam Integer ecId) {			
			JTableResponse jTableResponse=null;		 
			try {
				TestRunPlan testRunPlan =productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);				
				ProductVersionListMaster productVersionListMaster = testRunPlan.getProductVersionListMaster();
				ProductMaster productMaster=testRunPlan.getProductVersionListMaster().getProductMaster();
				JsonRunConfiguration jsonrunConfig = new JsonRunConfiguration();
				List<JsonRunConfiguration> jsonrunConfigList = new ArrayList<JsonRunConfiguration>();				
				if(type.equals("map")){
					RunConfiguration runConfigurationOfHostExistingForTestRunPlan = null;                    
                    //check for existing Runconfiguration(withHostListOfTestRunPlan) entry, if available, then modify                   
                    runConfigurationOfHostExistingForTestRunPlan = hostListService.isRunConfigurationOfHostOfTestRunPlanExisting(ecId, testRunPlanId, hostId);                 
                    if(runConfigurationOfHostExistingForTestRunPlan != null && runConfigurationOfHostExistingForTestRunPlan.getRunconfigId() != null && runConfigurationOfHostExistingForTestRunPlan.getRunconfigId() != 0){//existing, hence modify or update Status to 1
                    	runConfigurationOfHostExistingForTestRunPlan.setRunConfigStatus(1);//Set Status 1 for Mapping
                    	HostList hostList=hostListService.getHostById(hostId);
                    	runConfigurationOfHostExistingForTestRunPlan.setHostList(hostList);
                    	environmentService.updateRunConfiguration(runConfigurationOfHostExistingForTestRunPlan);
                    	jsonrunConfig = new JsonRunConfiguration(runConfigurationOfHostExistingForTestRunPlan);
                    }else{//Newly adding RunConf with Host Of TestRunPlan
            			RunConfiguration runConfigurationObj=new RunConfiguration();
                        EnvironmentCombination environmentCombination= environmentService.getEnvironmentCombinationById(ecId);
                        runConfigurationObj.setEnvironmentcombination(environmentCombination);
                        runConfigurationObj.setProduct(productMaster);
                        runConfigurationObj.setProductVersion(productVersionListMaster);                        
                        HostList hostList=hostListService.getHostById(hostId);
                        String hostName=hostList.getHostName();                        
                        runConfigurationObj.setHostList(hostList);
                        String name=environmentCombination.getEnvironmentCombinationName()+":"+hostName;
                        runConfigurationObj.setRunconfigName(name);
                        runConfigurationObj.setTestRunPlan(testRunPlan);   
                        runConfigurationObj.setRunConfigStatus(1);//Set Status 1 for Mapping
                        RunConfiguration runConfiguration=environmentService.addRunConfiguration(runConfigurationObj);
                        if(runConfiguration != null){
                   		 jsonrunConfig = new JsonRunConfiguration(runConfigurationObj);
                   	 	}
                    }
                    jTableResponse = new JTableResponse("OK", "Host mapped to Run Configuration successfully");
                 }else if(type.equals("unmap")){//Update RunConfigStatus to 0 for UnMapping TestRunPlanLevel   
                	 
                	    RunConfiguration runConfiguration=productListService.getRunConfigurationById(runconfigId);
                	    runConfiguration.setRunConfigStatus(0);
                        environmentService.updateRunConfiguration(runConfiguration);
                        jsonrunConfig = new JsonRunConfiguration(runConfiguration); 
                        jTableResponse = new JTableResponse("OK", "Host unmapped to Run Configuration successfully");
                 }  
				 jsonrunConfigList.add(jsonrunConfig);    
       			
			} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to map terminal!");
		            log.error("JSON ERROR : Unable to map TerminalHost", e);
		        }		        
	        return jTableResponse;
	    }
	

		@RequestMapping(value="administration.host.mapTerminal.wp",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse mapTerminalHostWorkpackage(@RequestParam Integer workpackageId,@RequestParam String type,@RequestParam Integer runconfigId,@RequestParam Integer hostId,@RequestParam Integer ecId) {			
			JTableResponse jTableResponse=null;;			 
			try {
				WorkPackage workPackage=workPackageService.getWorkPackageById(workpackageId);
				ProductVersionListMaster productVersionListMaster = workPackage.getProductBuild().getProductVersion();
				ProductMaster productMaster=productVersionListMaster.getProductMaster();
				JsonRunConfiguration jsonrunConfig = new JsonRunConfiguration();
				List<JsonRunConfiguration> jsonrunConfigList = new ArrayList<JsonRunConfiguration>();
				if(type.equals("map")){                     
                    RunConfiguration runConfigurationExisting = null;                    
                    //check for existing Runconfiguration(withHostListOfWP) entry, if available, then modify                   
                    runConfigurationExisting = hostListService.isRunConfigurationOfHostOfWorkPackageExisting(ecId, workpackageId, hostId);                    
                    
                    if(runConfigurationExisting != null && runConfigurationExisting.getRunconfigId() != null && runConfigurationExisting.getRunconfigId() != 0){//existing, hence modify or update Status to 1                    	
                    	runConfigurationExisting.setRunConfigStatus(1);//Set Status 1 for Mapping
                    	environmentService.updateRunConfiguration(runConfigurationExisting);
                    	jsonrunConfig = new JsonRunConfiguration(runConfigurationExisting);
                    }else{//Newly adding RunConf with Host Of WP
                    	RunConfiguration runConfigurationObj=new RunConfiguration();
                        EnvironmentCombination environmentCombination= environmentService.getEnvironmentCombinationById(ecId);
                        runConfigurationObj.setEnvironmentcombination(environmentCombination);
                        runConfigurationObj.setProduct(productMaster);
                        runConfigurationObj.setProductVersion(productVersionListMaster);
                        
                        HostList hostList=hostListService.getHostById(hostId);
                        String hostName=hostList.getHostName();
                        
                        runConfigurationObj.setHostList(hostList);
                        String name=environmentCombination.getEnvironmentCombinationName()+":"+hostName;
                        runConfigurationObj.setRunconfigName(name);
                        runConfigurationObj.setWorkPackage(workPackage);
                        runConfigurationObj.setRunConfigStatus(1);//Set Status 1 for Mapping
                        RunConfiguration runConfiguration=environmentService.addRunConfiguration(runConfigurationObj);                    	 
                    	 if(runConfiguration != null){
                    		 jsonrunConfig = new JsonRunConfiguration(runConfigurationObj);
                    	 }
                    }                    
                 }else if(type.equals("unmap")){//Update RunConfigStatus to 0 for UnMapping                       
                        RunConfiguration runConfiguration=productListService.getRunConfigurationById(runconfigId);
                	    runConfiguration.setRunConfigStatus(0);
                        environmentService.updateRunConfiguration(runConfiguration);
                        jsonrunConfig = new JsonRunConfiguration(runConfiguration); 
                 }
                    
                jsonrunConfigList.add(jsonrunConfig);    
                jTableResponse = new JTableResponse("OK", jsonrunConfigList);
       			
			} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to map terminal to Workpackage!");
		            log.error("JSON ERROR : Unable to map TerminalHost to Workpackage", e);
		        }		        
	        return jTableResponse;
	    }
	
		
		/* Product Team Resources */
		@RequestMapping(value="product.team.resouces.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listProductTeamResources(@RequestParam int productId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {			
			JTableResponse jTableResponse;			 
			try {
				List<ProductTeamResources> productTeamResList = productListService.getProductTeamResourcesList(productId,jtStartIndex,jtPageSize);
				List<ProductTeamResources> productCoreResListforPagination=productListService.getProductTeamResourcesList(productId,null,null);
				List<JsonProductTeamResources> jsonProductTeamResList=new ArrayList<JsonProductTeamResources>();
				for(ProductTeamResources productCoreRes: productTeamResList){
					jsonProductTeamResList.add(new JsonProductTeamResources(productCoreRes));	
					}				
				jTableResponse = new JTableResponse("OK", jsonProductTeamResList,productCoreResListforPagination.size());     
				productTeamResList = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching ProductTeamResources!");
		            log.error("JSON ERROR : Unable to list ProductTeamResources", e);
		        }		        
	        return jTableResponse;
	    }
		
		@RequestMapping(value="product.team.user.add",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addProductTeamUser(@ModelAttribute JsonProductTeamResources jsonProductTeamResource, BindingResult result) {
			JTableSingleResponse jTableSingleResponse;
			if(result.hasErrors()){
				jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
			}
			try {   
				ProductTeamResources pTeamResource = jsonProductTeamResource.getProductTeamResources();
				UserList user=userListService.getUserListById(jsonProductTeamResource.getUserId());
				UserRoleMaster productSpecificRole = userListService.getRoleById(jsonProductTeamResource.getProductSpecificUserRoleId());
				if(productSpecificRole != null){
					pTeamResource.setProductSpecificUserRole(productSpecificRole);
				}
				Boolean errorMessage=	productListService.isUserAlreadyProductTeamResource(jsonProductTeamResource.getProductId(),jsonProductTeamResource.getUserId(),jsonProductTeamResource.getFromDate(),jsonProductTeamResource.getToDate(),null);
				if (errorMessage) {
					String msg="User "+ user.getLoginId() +" is already a Product Team resource for the specified period for this or other products";
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonProductTeamResources(pTeamResource),msg);
				}else{
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonProductTeamResources(pTeamResource));	
				}
				productListService.addProductTeamResource(pTeamResource);
				mongoDBService.addProductTeamResources(pTeamResource.getProductTeamResourceId());
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_PRODUCT_TEAM_RESOURCES, pTeamResource.getProductTeamResourceId(), user.getFirstName(), user);
				
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding aProductTeamUser!");
		            log.error("JSON ERROR : Unable to add ProductTeamUser", e);
		        }
		        
	        return jTableSingleResponse;
	    }
		
		@RequestMapping(value="product.team.user.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateProductTeamResourceMapping(@ModelAttribute JsonProductTeamResources jsonProductTeamResource, BindingResult result) {
			JTableResponse jTableResponse = null;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}			
			try {	
				TestFactory testFactory = null;
				ProductMaster productMaster = null;
				String remarks = "";
				Date updatetodate=DateUtility.dateformatWithOutTime(jsonProductTeamResource.getToDate());
				Date updatefromdate=DateUtility.dateformatWithOutTime(jsonProductTeamResource.getFromDate());
				if(updatefromdate.compareTo(updatetodate)>0){
					jTableResponse = new JTableResponse("ERROR","Warning: From date should be less than or equal to To date");
					return jTableResponse;
				}
			ProductTeamResources productTeamResourcesFromUI = jsonProductTeamResource.getProductTeamResources();
			ProductTeamResources productTeamResourcesFromDB = productListService.getProductTeamResourceById(jsonProductTeamResource.getProductTeamResourceId());
			productMaster = productTeamResourcesFromDB.getProductMaster();
			testFactory = productMaster.getTestFactory();
				//	log.info(" Cretaed date FromDB :"+productTeamResourcesFromDB.getCreatedDate());Bug 1636
					if(jsonProductTeamResource.getStatus() == 0){
						ProductTeamResources prdTeamResource = productListService.getProductTeamResourceById(jsonProductTeamResource.getProductTeamResourceId());
						 if(prdTeamResource != null){
							 productListService.removeProductTeamResourceMapping(prdTeamResource);
							 log.info("Product Team User association with product is removed.");
							 jTableResponse = new JTableResponse("OK");  
						 }
					}else{
						if((productTeamResourcesFromUI.getCreatedDate()!=null) ){		
							if(productTeamResourcesFromDB.getCreatedDate() != null){//Bug 1636
								productTeamResourcesFromUI.setCreatedDate(productTeamResourcesFromDB.getCreatedDate());	
							}							
						}
						UserList user=userListService.getUserListById(jsonProductTeamResource.getUserId());
						UserRoleMaster userRoleMaster = userListService.getRoleById(jsonProductTeamResource.getProductSpecificUserRoleId());
						productTeamResourcesFromUI.setProductSpecificUserRole(userRoleMaster);
						Boolean errorMessage = productListService.isUserAlreadyProductTeamResource(jsonProductTeamResource.getProductId(),jsonProductTeamResource.getUserId(),jsonProductTeamResource.getFromDate(),jsonProductTeamResource.getToDate(),productTeamResourcesFromDB);

						List<JsonProductTeamResources> jsonProductTeamResourceList =new ArrayList<JsonProductTeamResources>();
						jsonProductTeamResourceList.add(jsonProductTeamResource);
						
						if (errorMessage) {
							String msg="User "+ user.getLoginId() +" is already a Product Team resource for the specified period for this or other products and now added with this Product";
							jTableResponse = new JTableResponse("OK",jsonProductTeamResourceList ,1, msg);
						}else{								
							jTableResponse = new JTableResponse("OK",jsonProductTeamResourceList ,1); 
						}
						
						 if(jsonProductTeamResource.getPercentageofallocation() != null && (jsonProductTeamResource.getPercentageofallocation() <= 0 || jsonProductTeamResource.getPercentageofallocation() >100)){				 	
							 
								 	String msg=" The percentage of allocation should be in between 1 to 100 ";
									jTableResponse = new JTableResponse("ERROR",msg);
								    return jTableResponse; 
							 						 
						 }						
						productListService.updateProductTeamResource(productTeamResourcesFromUI);
						remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", ProductSpecificUserRole :"+productTeamResourcesFromDB.getProductSpecificUserRole().getRoleName();
						eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_PRODUCT_TEAM_RESOURCES, productTeamResourcesFromDB.getProductTeamResourceId(), user.getFirstName(), 
								jsonProductTeamResource.getModifiedField(), jsonProductTeamResource.getModifiedFieldTitle(), jsonProductTeamResource.getOldFieldValue(), jsonProductTeamResource.getModifiedFieldValue(), user, remarks);
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating Product Team Resource!");
		            log.error("JSON ERROR updating ProductTeamResource Mapping", e);
		        }
	        return jTableResponse;
	    }



		@RequestMapping(value="admin.genericDevices.list.deviceLab",method=RequestMethod.POST ,produces="application/json")
		    public @ResponseBody JTableResponse listGenricDevicesByDeviceLabId(@RequestParam int deviceLabId,@RequestParam int filter) {			
				JTableResponse jTableResponse=null;;			 
				try {
					List<GenericDevices> genericDevicesList=null;
						genericDevicesList =environmentService.listGenericDevices(deviceLabId,filter, -1);

					List<JsonGenericDevice> jsonGenericDevicesList=new ArrayList<JsonGenericDevice>();
					
					JsonGenericDevice jsonGenericDevices=	null;
					for(GenericDevices genericDevices: genericDevicesList){
						jsonGenericDevices=new JsonGenericDevice(genericDevices);
						jsonGenericDevicesList.add(jsonGenericDevices);	
					}
					jTableResponse = new JTableResponse("OK", jsonGenericDevicesList,jsonGenericDevicesList.size());  
						
						jsonGenericDevicesList = null;
				        return jTableResponse;
									
					} catch (Exception e) {
			            jTableResponse = new JTableResponse("ERROR","Unable to show Generic Devices!");
			            log.error("JSON ERROR listing GenricDevices By DeviceLabId", e);
			        }		        
		        return jTableResponse;
		    }

		@RequestMapping(value="admin.genericDevices.storageDrive.list.deviceLab",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listGenricDevicesStorageDriveByDeviceLabId(@RequestParam int deviceLabId,@RequestParam int deviceTypeId, @RequestParam int filter) {			
			JTableResponse jTableResponse=null;;			 
			try {
				List<GenericDevices> genericDevicesList=null;
					genericDevicesList =environmentService.listGenericDevices(deviceLabId,filter, deviceTypeId);
				List<JsonGenericDevice> jsonGenericDevicesList=new ArrayList<JsonGenericDevice>();
				JsonGenericDevice jsonGenericDevices=	null;
				for(GenericDevices genericDevices: genericDevicesList){
					jsonGenericDevices=new JsonGenericDevice(genericDevices);
					jsonGenericDevicesList.add(jsonGenericDevices);	
				}
				jTableResponse = new JTableResponse("OK", jsonGenericDevicesList,jsonGenericDevicesList.size());  
					
					jsonGenericDevicesList = null;
			        return jTableResponse;
								
				} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Generic Devices!");
		            log.error("JSON ERROR listing StorageDrive By DeviceLabId", e);
		        }		        
	        return jTableResponse;
	    }

		@RequestMapping(value="administration.host.list.deviceLab",method=RequestMethod.POST ,produces="application/json")
		    public @ResponseBody JTableResponse listHostByDeviceLabId(@RequestParam int deviceLabId,@RequestParam int filter) {			
				JTableResponse jTableResponse=null;;			 
				try {
					List<HostList> hostList=null;
					hostList =environmentService.listHost(deviceLabId,filter);

					List<JsonHostList> jsonHostLists=new ArrayList<JsonHostList>();
					
					JsonHostList jsonHostList=	null;
					if(hostList!=null && !hostList.isEmpty()){
					for(HostList hl: hostList){
						jsonHostList=new JsonHostList(hl);
						jsonHostLists.add(jsonHostList);	
					}
					}
					jTableResponse = new JTableResponse("OK", jsonHostLists,jsonHostLists.size());  
						
					jsonHostList = null;
				        return jTableResponse;
									
					} catch (Exception e) {
			            jTableResponse = new JTableResponse("ERROR","Unable to show Host List!");
			            log.error("JSON ERROR listing HostBy DeviceLabId", e);
			        }		        
		        return jTableResponse;
		    }

		
	@RequestMapping(value="administration.testrunplan.testbed",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody String listTestBeds(@RequestParam int testRunPlanId) {
		String finalResult="";
		try {
			TestRunPlan testRunPlan=null;
			testRunPlan = productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
			
			//Initialization parameters
			List<JsonTestRunPlan> jsonTestRunPlanList=new ArrayList<JsonTestRunPlan>();
			JSONArray list = new JSONArray();
			
			JSONObject finalObj = new JSONObject();
			JSONObject tsTitle = new JSONObject();
			JSONObject tcTitle = new JSONObject();
			JSONObject rcTitle = new JSONObject();
			
			JSONObject tsData = new JSONObject();
			JSONObject tcData = new JSONObject();
			JSONObject rcData = new JSONObject();
			
			JSONArray columnData = new JSONArray();
			JSONArray columnData1 = new JSONArray();
			JsonTestRunPlan jsonTestRunPlan = null;
			
			List<Integer> runConfigIds = new LinkedList<Integer>();
			
			//Logic started for Test Configuration / Test Plan - Test Suite levels			
			if(testRunPlan != null ){	
				
				String testSuiteName="";
				String testCaseName="";
				String runConfigName="";
				
				tsTitle.put("title", "TestSuite");					
				list.add(tsTitle);
				
				tcTitle.put("title", "TestCase");					
				list.add(tcTitle);
				
				Set<RunConfiguration> runConfigurations = testRunPlan.getRunConfigurationList();
				MultiMap testSuiteRunConfigurationMap = new MultiHashMap();
				for(RunConfiguration rc : runConfigurations){
					rcTitle= new JSONObject();
					rcTitle.put("title", rc.getRunconfigName());
					rcTitle.put("id", String.valueOf(rc.getRunconfigId()));
					list.add(rcTitle);
					runConfigIds.add(rc.getRunconfigId());
				}				
				
				finalObj.put("COLUMNS", list);
				
				Set<TestSuiteList> testSuiteLists = new HashSet<TestSuiteList>();
				Set<TestCaseList> testCaseLists = new HashSet<TestCaseList>();
				for(RunConfiguration rc : runConfigurations){
					testSuiteLists.addAll(rc.getTestSuiteLists());
					for(TestSuiteList tsl : testSuiteLists){
						for (TestCaseList tc : productListService.getRunConfigTestSuiteTestCaseMapped(rc.getRunconfigId(), tsl.getTestSuiteId())){
							testSuiteRunConfigurationMap.put(tc.getTestCaseId(),rc.getRunconfigId());
						}
					}
				}
				for(TestSuiteList tsl : testSuiteLists){
					testSuiteName = tsl.getTestSuiteName();
					for(RunConfiguration rc : testRunPlan.getRunConfigurationList()){
						if(testRunPlan.getUseIntelligentTestPlan() != null && !testRunPlan.getUseIntelligentTestPlan().equalsIgnoreCase("Yes"))
							testCaseLists.addAll(productListService.getRunConfigTestSuiteTestCaseMapped(rc.getRunconfigId(), tsl.getTestSuiteId()));
						else
							testCaseLists.addAll(tsl.getTestCaseLists());
					}
					for (TestCaseList testCaseList : testCaseLists) {							
						testCaseName = testCaseList.getTestCaseName();
						columnData = new JSONArray();
						columnData.add(testSuiteName+"["+tsl.getTestSuiteId()+"]");
						columnData.add(testCaseName+"["+testCaseList.getTestCaseId()+"]");
						for(RunConfiguration rc : runConfigurations){
							List<String> s = (List<String>) testSuiteRunConfigurationMap.get(testCaseList.getTestCaseId());				
							if(s != null && !s.isEmpty() && s.contains(rc.getRunconfigId())){
								runConfigName = rc.getRunconfigName()+"["+rc.getRunconfigId()+"]";	
							} else {
								runConfigName = "NA"+"["+rc.getRunconfigId()+"]";
							}
							columnData.add(runConfigName);
						}
						columnData1.add(columnData);
					}
					testCaseLists = null;
					testCaseLists = new HashSet<TestCaseList>();
				}
				finalObj.put("DATA", columnData1);
				jsonTestRunPlan=new JsonTestRunPlan(testRunPlan,"TestBed");
				jsonTestRunPlanList.add(jsonTestRunPlan);	
			}			
			finalResult=finalObj.toString();
			jsonTestRunPlan = null;
		    return "["+finalResult+"]";								
		} catch (Exception e) {	         
			log.error("JSON ERROR listing TestBeds", e);
	    }		        
	    return "["+finalResult+"]";
	}		
				
		@RequestMapping(value="admin.genericDevices.list.product",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listGenricDevicesByProductId(@RequestParam int productId,@RequestParam int filter) {			
			log.debug("admin.genericDevices.list.product");
			JTableResponse jTableResponse=null;;			 
			try {
				Set<GenericDevices> genericDevicesList = null;
				ProductMaster product = productListService.getProductDetailsByIdWithDevicesList(productId);
				
				genericDevicesList =product.getGenericeDevices();

				List<JsonGenericDevice> jsonGenericDevicesList = new ArrayList<JsonGenericDevice>();
				
				JsonGenericDevice jsonGenericDevices=	null;
				for(GenericDevices genericDevices: genericDevicesList){
					if(filter==1){
						if(genericDevices.getAvailableStatus()==1){
							jsonGenericDevices=new JsonGenericDevice(genericDevices);
							jsonGenericDevicesList.add(jsonGenericDevices);
						}
					}else if(filter==0){
						if(genericDevices.getAvailableStatus()==0){
							jsonGenericDevices=new JsonGenericDevice(genericDevices);
							jsonGenericDevicesList.add(jsonGenericDevices);
						}
					}else {
						jsonGenericDevices=new JsonGenericDevice(genericDevices);
						jsonGenericDevicesList.add(jsonGenericDevices);
					}
				}
				jTableResponse = new JTableResponse("OK", jsonGenericDevicesList,jsonGenericDevicesList.size());  
					
					jsonGenericDevicesList = null;
			        return jTableResponse;
								
				} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Generic Devices!");
		            log.error("JSON ERROR listing Devices By ProductId", e);
		        }		        
	        return jTableResponse;
	    }

		
		@RequestMapping(value="administration.host.list.product",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listHostByProduct(@RequestParam int productId,@RequestParam int filter) {			
			JTableResponse jTableResponse=null;;			 
			try {

				ProductMaster productMaster = productListService.getProductDetailsByIdWithHostList(productId);
				Set<HostList> hostList=null;
				hostList =productMaster.getHostLists();

				List<JsonHostList> jsonHostLists=new ArrayList<JsonHostList>();
				
				JsonHostList jsonHostList=	null;
				if(hostList!=null && !hostList.isEmpty()){
					for(HostList hl: hostList){
						if(filter==1){
							if(hl.getCommonActiveStatusMaster().getStatus().equals("ACTIVE")){
								jsonHostList=new JsonHostList(hl);
								jsonHostLists.add(jsonHostList);	

							}
						}else if(filter==0){
							if(hl.getCommonActiveStatusMaster().getStatus().equals("INACTIVE")){
								jsonHostList=new JsonHostList(hl);
								jsonHostLists.add(jsonHostList);	

							}
						}else {
							jsonHostList=new JsonHostList(hl);
							jsonHostLists.add(jsonHostList);	

						}
					}
				}
				jTableResponse = new JTableResponse("OK", jsonHostLists,jsonHostLists.size());  
					
				jsonHostList = null;
			        return jTableResponse;
								
				} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Host!");
		            log.error("JSON ERROR listing Host By Product", e);
		        }		        
	        return jTableResponse;
	    }

		@RequestMapping(value="admin.genericDevices.product",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listGenricDevicesByProduct(@RequestParam int deviceLabId,@RequestParam int productId) {			
			JTableResponse jTableResponse=null;;			 
			try {
				List<GenericDevices> genericDevicesList=null;
					genericDevicesList =environmentService.listGenericDevices(deviceLabId);

				List<JsonGenericDevice> jsonGenericDevicesList=new ArrayList<JsonGenericDevice>();
				
				ProductMaster productMaster = productListService.getProductDetailsByIdWithDevicesList(productId);

				Set<GenericDevices> genericDevicesInProduct=productMaster.getGenericeDevices();
				
				JsonGenericDevice jsonGenericDevices=	null;
				
				for(GenericDevices genericDevices: genericDevicesList){
				        boolean found=false;
				        for (GenericDevices genericDevicesProd : genericDevicesInProduct) {
				            if ((genericDevices.getGenericsDevicesId().equals(genericDevicesProd.getGenericsDevicesId())) ) {
				                found=true;
				                break;
				            }
				        }
				        if(!found){
				        	JsonGenericDevice jsonTestRunPlan=new JsonGenericDevice(genericDevices);
							jsonGenericDevicesList.add(jsonTestRunPlan);
				        }
				    }
				jTableResponse = new JTableResponse("OK", jsonGenericDevicesList,jsonGenericDevicesList.size());  
					jsonGenericDevicesList = null;
			        return jTableResponse;
								
				} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Generic Devices!");
		            log.error("JSON ERROR listing Devices By Product", e);
		        }		        
	        return jTableResponse;
	    }

		@RequestMapping(value="administration.product.mapMobile",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse mapMobileToProduct(@RequestParam int productId,@RequestParam Integer deviceId ,@RequestParam String type){	
			JTableResponse jTableResponse=null;;			 
			try {
				boolean hasTestConfigurations = productListService.deviceHasTestConfigurations(productId,deviceId);
				if(hasTestConfigurations && type.equals("unmap")){
					jTableResponse = new JTableResponse("ERROR","Device cannot be unmapped as it has Test Configurations associated");
					return jTableResponse;
				}else{
					productListService.mapMobileWithProduct(productId, deviceId,type);
					if(type.equals("map")){
					jTableResponse = new JTableResponse("SUCCESS","Device successfully mapped to Product");
					}else{
					jTableResponse = new JTableResponse("SUCCESS","Device successfully unmapped from Product");
					}
				}

			} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error in Device mapping");
		            log.error("JSON ERROR mapping Mobile To Product", e);
		        }		        
	        return jTableResponse;
	    }


		@RequestMapping(value="admin.host.product",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listServerByProduct(@RequestParam int deviceLabId,@RequestParam int productId) {			
			JTableResponse jTableResponse=null;;			 
			try {
				List<HostList> hostLists=null;
				hostLists =environmentService.listHost(deviceLabId,-1);
				List<JsonHostList> jsonHostLists=new ArrayList<JsonHostList>();
				ProductMaster productMaster = productListService.getProductDetailsByIdWithHostList(productId);
				Set<HostList> hostInProduct=productMaster.getHostLists();
				
				JsonHostList jsonHostList=	null;
				for(HostList hostList: hostLists){
					if(!hostInProduct.contains(hostList)){
						jsonHostList=new JsonHostList(hostList);
						jsonHostLists.add(jsonHostList);	
					}
				}
				jTableResponse = new JTableResponse("OK", jsonHostLists,jsonHostLists.size());  
					
				jsonHostLists = null;
			        return jTableResponse;
								
				} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Host list!");
		            log.error("JSON ERROR listing Server By Product", e);
		        }		        
	        return jTableResponse;
	    }
		
		
		@RequestMapping(value="administration.product.mapServer",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse mapServerToProduct(@RequestParam int productId,@RequestParam Integer hostId ,@RequestParam String type){	
			JTableResponse jTableResponse=null;;			 
			try {
				boolean hasTestConfigurations = productListService.hostHasTestConfigurations(productId,hostId);
				if(hasTestConfigurations && type.equals("unmap")){
					jTableResponse = new JTableResponse("ERROR","Host cannot be unmapped as it has Test Configurations associated");
					return jTableResponse;
				}
				else{
					productListService.mapServerWithProduct(productId, hostId,type);
					if(type.equals("map")){
						jTableResponse = new JTableResponse("SUCCESS","Server successfully mapped to Product");
					}else{
						jTableResponse = new JTableResponse("SUCCESS","Server successfully unmapped from Product");
					}
				}
			} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error in mapping server");
		            log.error("JSON ERROR mapping Server To Product", e);
		    }		        
	        return jTableResponse;
	    }
		
		@RequestMapping(value="product.summary",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getProductSummary(@RequestParam int productId){	
			log.debug("product.summary");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			JTableResponse jTableResponse = null;
				try {
					ProductSummaryDTO productSummaryDTO = productListService.getProductSummary(productId);
					List<JsonProductSummary> jsonProductSummaryList = new ArrayList<JsonProductSummary>();
					if (productSummaryDTO == null ) {
						jTableResponse = new JTableResponse("OK", jsonProductSummaryList, 0);
					} else {
						jsonProductSummaryList.add(new JsonProductSummary(productSummaryDTO));
						jTableResponse = new JTableResponse("OK", jsonProductSummaryList,jsonProductSummaryList.size() );
						productSummaryDTO = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching Product Summary!");
		            log.error("JSON ERROR getting Product Summary", e);	            
		        }
	        return jTableResponse;
	    }
		@RequestMapping(value="product.feature.details",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getProductFeatureDetails(@RequestParam int featureId){	
			JTableResponse jTableResponse = null;
			List<JsonProductFeature> jsonProductFeatureList = new ArrayList<JsonProductFeature>();
				try {
					if(featureId != 0){
						ProductFeature productFeature = productListService.getByProductFeatureId(featureId);
						if(productFeature != null){
								jsonProductFeatureList.add(new JsonProductFeature(productFeature));
								jTableResponse = new JTableResponse("OK", jsonProductFeatureList,jsonProductFeatureList.size() );
						}
					}else{
						jTableResponse = new JTableResponse("OK", jsonProductFeatureList, 0);
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching ProductFeature!");
		            log.error("JSON ERROR getting ProductFeature", e);	            
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="product.decoupling.category.details",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getProductDecouplingCategoryDetails(@RequestParam int dcId){	
			log.debug("product.decoupling.category.details");
			JTableResponse jTableResponse = null;
			List<JsonDecouplingCategory> jsonDecouplingCategoryList = new ArrayList<JsonDecouplingCategory>();
				try {
					if(dcId != 0){
						DecouplingCategory dc = decouplingCategoryService.getDecouplingCategoryById(dcId);
						if(dc != null){
							jsonDecouplingCategoryList.add(new JsonDecouplingCategory(dc));
								jTableResponse = new JTableResponse("OK", jsonDecouplingCategoryList,jsonDecouplingCategoryList.size() );
						}
					}else{
						jTableResponse = new JTableResponse("OK", jsonDecouplingCategoryList, 0);
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching DecouplingCategory!");
		            log.error("JSON ERROR getting DecouplingCategory", e);	            
		        }
	        return jTableResponse;
	    }

		@RequestMapping(value="environment.combination.list.of.selectedProduct",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableResponse listEnvironmnetCombinationOfSelectedProduct(@RequestParam int productVersionId,@RequestParam int productId,@RequestParam int workpackageId,@RequestParam int jtStartIndex, 
@RequestParam int jtPageSize,@RequestParam Integer testRunPlanId, @RequestParam Integer envComstatus) {			
			JTableResponse jTableResponse=null;;			 
			try {
				if(productId==-1){
					if(productVersionId!=-1){
						ProductVersionListMaster productVersionMasterList=productListService.getProductVersionListMasterById(productVersionId);
						productId=productVersionMasterList.getProductMaster().getProductId();
					}else{
						WorkPackage workPackage=workPackageService.getWorkPackageById(workpackageId);
						productId=workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
					}
				}
				List<EnvironmentCombination> environmentCombinationsList = environmentService.listEnvironmentCombinationByStatus(productId,envComstatus);
				List<JsonEnvironmentCombination> jsonEnvironmentCombinationList=new ArrayList<JsonEnvironmentCombination>();
				 
				for(EnvironmentCombination environmentCombination: environmentCombinationsList){
					JsonEnvironmentCombination jsonEnvironmentCombination=	new JsonEnvironmentCombination(environmentCombination);
					jsonEnvironmentCombination.setStatus(0);
					jsonEnvironmentCombinationList.add(jsonEnvironmentCombination);				
				}

				jTableResponse = new JTableResponse("OK", jsonEnvironmentCombinationList,jsonEnvironmentCombinationList.size());  
				environmentCombinationsList = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Product Test Environments Combination!");
		            log.error("JSON ERROR listing EnvironmnetCombination Of SelectedProduct", e);
		        }		        
	        return jTableResponse;
	    }
		
		/* testrunplangroup.list */
		@RequestMapping(value="testrunplangroup.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse testrunplangrouplist(@RequestParam int productVersionId, @RequestParam int productId) {			
			JTableResponse jTableResponse;			 
			try {				
				List<TestRunPlanGroup> testrunplangrouplist ;
				if(productVersionId == -1 || productVersionId == 0){
					testrunplangrouplist=productListService.listTestRunPlanGroup(0, productId);					
				}else{
					testrunplangrouplist=productListService.listTestRunPlanGroup(productVersionId, 0);	
				}
				
				List<JsonTestRunPlanGroup> jsonTestRunPlanGroup=new ArrayList<JsonTestRunPlanGroup>();			
				for(TestRunPlanGroup testrun: testrunplangrouplist){					
					jsonTestRunPlanGroup.add(new JsonTestRunPlanGroup(testrun));
				}
	            jTableResponse = new JTableResponse("OK", jsonTestRunPlanGroup,jsonTestRunPlanGroup.size());     
	            testrunplangrouplist = null;				
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show testrunplangroup!");
		            log.error("JSON ERROR getting Testrunplangroup", e);
		        }		        
	        return jTableResponse;				
	    }
		
		@RequestMapping(value="testrunplangroup.list.add",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addtestrunplangroup(HttpServletRequest request, @ModelAttribute JsonTestRunPlanGroup jsonTestRunPlanGroup, BindingResult result) {
			log.info("Inside testrunplangroup.list.add ");
			JTableSingleResponse jTableSingleResponse;
			ProductMaster product = null;
			if(result.hasErrors()){
				jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
			}		
			try {   		
				UserList user = (UserList)request.getSession().getAttribute("USER");
				UserList userObj = userListService.getUserListById(user.getUserId());
				TestRunPlanGroup testRunPlan= jsonTestRunPlanGroup.gettestRunPlanGroup();	
				product = productListService.getProductById(jsonTestRunPlanGroup.getProductId());
				//Check if ducplicate exists
				String errorMessage=commonService.duplicateName(testRunPlan.getName(), "TestRunPlanGroup", "name", "Test Plan Group", "productId="+product.getProductId());
				if (errorMessage != null) {
					jTableSingleResponse = new JTableSingleResponse("ERROR","Test Plan Group Already Exists");
					return jTableSingleResponse;
				}
				testRunPlan.setProduct(product);
				ProductVersionListMaster productVersionListMaster=productListService.getProductVersionListMasterById(jsonTestRunPlanGroup.getProductVersionId());
				testRunPlan.setProductVersionListMaster(productVersionListMaster);
					
				ExecutionTypeMaster exeTypeMaster=executionTypeMasterService.getExecutionTypeByExecutionTypeId(testRunPlan.getExecutionTypeMaster().getExecutionTypeId());
				testRunPlan.setExecutionTypeMaster(exeTypeMaster);
		
				productListService.addtestrunpalngroup(testRunPlan);
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_TEST_RUN_PLAN_GROUP, testRunPlan.getTestRunPlanGroupId(), testRunPlan.getName(), userObj);
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestRunPlanGroup(testRunPlan));	
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding testrunplangroup record!");
		            log.error("JSON ERROR adding testrunplangroup record", e);
		        }
	        return jTableSingleResponse;
	    }
		
		@RequestMapping(value="testrunplangroup.list.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateTestrunplangroup(HttpServletRequest request, @ModelAttribute JsonTestRunPlanGroup jsonTestRunPlanGroup, BindingResult result) {
			JTableResponse jTableResponse = null;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}	
			TestRunPlanGroup testRunPlanGroupFromUI = null;
			TestFactory testFactory = null;
			ProductMaster productMaster = null;
			String remarks = "";
			
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}
			
			try {
				UserList user = (UserList)request.getSession().getAttribute("USER");
				UserList userObj = userListService.getUserListById(user.getUserId());
				int updatename=jsonTestRunPlanGroup.getName().length();
				
				log.info("updatename length===>"+updatename);
				if(updatename<3)
				{
					jTableResponse = new JTableResponse("ERROR","Please enter minimum 3 letters!"); 
					return jTableResponse;
				}
				
				testRunPlanGroupFromUI= jsonTestRunPlanGroup.gettestRunPlanGroup();
				ProductVersionListMaster productVersionListMaster = null;
				if(jsonTestRunPlanGroup.getProductVersionId() != null){
					productVersionListMaster=productListService.getProductVersionListMasterById(jsonTestRunPlanGroup.getProductVersionId());
				testRunPlanGroupFromUI.setProductVersionListMaster(productVersionListMaster);				
				productListService.update(testRunPlanGroupFromUI);
				productMaster = productVersionListMaster.getProductMaster();
				testFactory = productMaster.getTestFactory();
					remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", TestRunPlanGroup :"+testRunPlanGroupFromUI.getName();
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_TEST_RUN_PLAN_GROUP, testRunPlanGroupFromUI.getTestRunPlanGroupId(), testRunPlanGroupFromUI.getName(), 
						jsonTestRunPlanGroup.getModifiedField(), jsonTestRunPlanGroup.getModifiedFieldTitle(), jsonTestRunPlanGroup.getOldFieldValue(), jsonTestRunPlanGroup.getModifiedFieldValue(), userObj, remarks);
				jTableResponse = new JTableResponse("OK",new JsonTestRunPlanGroup(testRunPlanGroupFromUI));	
				}
			    } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to update the testrunplangroup!");
		            log.error("JSON ERROR updating Testrunplangroup", e);
		        }	        
	        return jTableResponse;
	    }	
		
		@RequestMapping(value="testrunplangroup_has_testrunplan.mappedlist",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getTestrunplangroupMapping(@RequestParam Integer testRunPlanGroupId) {
			log.debug ("testrunplangroup_has_testrunplan.mappedlist");
			JTableResponse jTableResponse;
			try {		
				
				List<TestRunPlangroupHasTestRunPlan> testrunplangrouplist=productListService.listTestRunPlanGroupMap(testRunPlanGroupId);
				
				List<JsonTestRunPlanGroupHasTestRunPlan> jsonTestRunPlanGroupHasTestRunPlan=new ArrayList<JsonTestRunPlanGroupHasTestRunPlan>();
			
				for(TestRunPlangroupHasTestRunPlan testrun: testrunplangrouplist){
					
					jsonTestRunPlanGroupHasTestRunPlan.add(new JsonTestRunPlanGroupHasTestRunPlan(testrun));
					}
	            jTableResponse = new JTableResponse("OK", jsonTestRunPlanGroupHasTestRunPlan,jsonTestRunPlanGroupHasTestRunPlan.size());     
	            testrunplangrouplist = null;
			} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching Testrunplangroup!");
		            log.error("JSON ERROR getting Testrunplangroup Mapping", e);
		        }
	        return jTableResponse;
	    }
		

		@RequestMapping(value="testrunplangroup_has_testrunplan.add",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addTestrunplangroup_has_testrunplan(@RequestParam int testRunPlanId,@RequestParam int testRunPlanGroupId,@RequestParam String maporunmap) {
			log.info("Inside testrunplangroup_has_testrunplan.add ");
			JTableSingleResponse jTableSingleResponse=null;
			JTableResponse jTableResponse;
					
			try {   
				/*if(maporunmap.equalsIgnoreCase("unmap")){
					return new JTableSingleResponse("ERROR","UnMap is not allowed!");
				}*/
				TestRunPlan testRunPlan=productListService.addtestrunpalngroupMapping(testRunPlanId,testRunPlanGroupId,maporunmap);
				List<JsonTestRunPlan> jsonTestRunPlanListToUI=new ArrayList<JsonTestRunPlan>();
				if(testRunPlan != null){
					jsonTestRunPlanListToUI.add(new JsonTestRunPlan(testRunPlan));
				}				
				if(maporunmap.equalsIgnoreCase("map")){
					jTableSingleResponse = new JTableSingleResponse("OK","Test Plan group mapped successfully");
				}else{
					jTableSingleResponse = new JTableSingleResponse("OK","Test Plan group unmapped successfully");
				}
								
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error in mapping testrunplangroup!");
		            log.error("JSON ERROR adding Testrunplangroup to testrunplan", e);
		        }
	        return jTableSingleResponse;
	    }
		
		@RequestMapping(value="list.defects.for.review",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listDefectsForReview(@RequestParam int productId,@RequestParam int productVersionId,@RequestParam int productBuildId,
	    		@RequestParam int workPackageId,@RequestParam int approveStatus, @RequestParam  Date startDate, @RequestParam  Date endDate,@RequestParam  Integer raisedByUser, HttpServletRequest req, Integer jtStartIndex, Integer jtPageSize) {
				log.debug("list.defects.for.review");
				UserList user=(UserList)req.getSession().getAttribute("USER");
				List<TestExecutionResultBugList> defectList=null;
				
				List<WorkPackage> Wpackagelist= new ArrayList<WorkPackage>();

				JTableResponse jTableResponse = null;
				List<JsonTestExecutionResultBugList> jsonTestExecutionResultBugList=new ArrayList<JsonTestExecutionResultBugList>();
					try {
						defectList	= workPackageService.listDefectsByTestcaseExecutionPlanIdByApprovedStatus(productId, productVersionId,productBuildId,workPackageId,approveStatus,startDate, endDate,raisedByUser,jtStartIndex, jtPageSize);
						List<TestExecutionResultBugList> defectListforPagination=workPackageService.listDefectsByTestcaseExecutionPlanIdByApprovedStatus(productId, productVersionId,productBuildId,workPackageId,approveStatus,startDate, endDate,raisedByUser,null, null);
						for(TestExecutionResultBugList defect: defectList){
							jsonTestExecutionResultBugList.add(new JsonTestExecutionResultBugList(defect));
						}
					
						if(jsonTestExecutionResultBugList.size()!=0) {
							jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList,defectListforPagination.size() );
						}else{    
							jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList, 0);  
						}
			        } catch (Exception e) {
			            jTableResponse = new JTableResponse("ERROR","Error fetching DefectsForReview!");
			            log.error("JSON ERROR listing DefectsForReview", e);	            
			        }
		        return jTableResponse;
		    }
		
		@RequestMapping(value="report.defects.weekly.view",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listDefectsWeeklyReport(HttpServletRequest req, 
	    		@RequestParam int weekNo,@RequestParam int productId,@RequestParam int productVersionId,@RequestParam int productBuildId,@RequestParam int workPackageId) {
			log.debug("inside resource.availability.view.list");
			JTableResponse jTableResponse = null;
			log.debug("Week No from Request : " + weekNo);
			List<JsonDefectsWeeklyReport> jsonDefectsWeeklyReportList = new ArrayList<JsonDefectsWeeklyReport>();
			try {
					if (weekNo < 0) {
						weekNo = DateUtility.getWeekOfYear();
					}
					req.getSession().setAttribute("ssnHdnResourceAvailabilityWeekNo", weekNo);
					req.getSession().setAttribute("ssnHdnSelectedResourceId", productId);
					
					List<DefectWeeklyReportDTO> defectWeeklyReportDTOList = productListService.listDefectsWeeklyReport(weekNo,productId,productVersionId,productBuildId, workPackageId);
					for(DefectWeeklyReportDTO defectDTO : defectWeeklyReportDTOList){
						jsonDefectsWeeklyReportList.add(new JsonDefectsWeeklyReport(defectDTO));
					}
					
					log.info("jsonDefectsWeeklyReportList size="+jsonDefectsWeeklyReportList.size());
					jTableResponse = new JTableResponse("OK", jsonDefectsWeeklyReportList, jsonDefectsWeeklyReportList.size());
					jsonDefectsWeeklyReportList = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching DefectsWeeklyReport!");
		            log.error("JSON ERROR listing DefectsWeeklyReport", e);	            
		        }
	        return jTableResponse;
	    }

		@RequestMapping(value="administration.testrunplangroup.listbyproductversion",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listTestRunPlangroupByProductVersionId(@RequestParam int productversionId,@RequestParam Integer productId ,@RequestParam int jtStartIndex, @RequestParam int jtPageSize,@RequestParam int testRunPlanGroupId) {			
			JTableResponse jTableResponse=null;;			 
			try {
				List<TestRunPlan> testRunPlanList =productListService.listTestRunPlanByProductVersionId(productversionId,testRunPlanGroupId);
				List<JsonTestRunPlan> jsonTestRunPlans=new ArrayList<JsonTestRunPlan>();
				
				List<TestRunPlangroupHasTestRunPlan> testrunplangrouplist=productListService.listTestRunPlanGroupMap(testRunPlanGroupId);
				List<TestRunPlan> testRunPlanForGroupList=new ArrayList<TestRunPlan>();
				
				for(TestRunPlangroupHasTestRunPlan testRunPlanGrp:testrunplangrouplist)
				{
					testRunPlanForGroupList.add(testRunPlanGrp.getTestrunplan());
				}
				
				 for (TestRunPlan testRunPlan : testRunPlanList) {
				        boolean found=false;
				        for (TestRunPlan testRunPlanGrp : testRunPlanForGroupList) {
				            if ((testRunPlan.getTestRunPlanId().equals(testRunPlanGrp.getTestRunPlanId())) ) {
				                found=true;
				                break;
				            }
				        }
				        if(!found){
				        	JsonTestRunPlan jsonTestRunPlan=new JsonTestRunPlan(testRunPlan);
							jsonTestRunPlans.add(jsonTestRunPlan);
				        }
				    }
				jTableResponse = new JTableResponse("OK", jsonTestRunPlans,jsonTestRunPlans.size());  
				
				jsonTestRunPlans = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show TestRunPlangroup!");
		            log.error("JSON ERROR listing TestRunPlangroup By ProductVersionId", e);
		        }		        
	        return jTableResponse;
	    }
		
		
		@RequestMapping(value="product.decoupling.category.testcase.dnd",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse productDcTestcaseDnd(HttpServletRequest req, @RequestParam Integer sourceDCId, @RequestParam Integer destinationDCId, @RequestParam Integer tcId) {
			log.info("inside product.decoupling.category.testcase.dnd");
			JTableSingleResponse jTableSingleResponse;
			try {
					boolean isSuccessfullyUnmappedAndMapped = productListService.mapAndUnmapDCTestCases(sourceDCId, destinationDCId, tcId);
					if(isSuccessfullyUnmappedAndMapped){
						jTableSingleResponse = new JTableSingleResponse("OK","Success");	
					}else{
						jTableSingleResponse = new JTableSingleResponse("Error","Issue while drag and drop of test case to decoupling category");	
					}
		        } catch (Exception e) {
		        	jTableSingleResponse = new JTableSingleResponse("ERROR","Error while drag and drop of test case to decoupling category!");
		            log.error("JSON ERROR while drag and drop of test case to decoupling category", e);            
		        }
	        return jTableSingleResponse;
	    }
		
		@RequestMapping(value="testrunplangroup_has_testrunplan.mappedlist.Update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateTestrunplangroupHasTestRunPlan(@ModelAttribute JsonTestRunPlanGroupHasTestRunPlan jsonTestRunPlanGroupHasTestRunPlan, BindingResult result) {
			JTableResponse jTableResponse = null;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}	
			TestRunPlangroupHasTestRunPlan testRunPlanGroupHasFromUI = null;
			TestRunPlangroupHasTestRunPlan testRunPlanGroupHasFromDB = null;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}
			
			try {
				testRunPlanGroupHasFromUI= jsonTestRunPlanGroupHasTestRunPlan.gettestRunPlangroupHasTestRunPlan();
				TestRunPlan testRunPlan = productListService.getTestRunPlanById(testRunPlanGroupHasFromUI.getTestrunplan().getTestRunPlanId());
				if(testRunPlanGroupHasFromUI.getTestRunGroupId() == null){
					return new JTableResponse("ERROR","Please map the test plan and then update!");
				}
				testRunPlanGroupHasFromUI.setTestrunplan(testRunPlan);
				TestRunPlanGroup tesrRunPlanGroup =productListService.getTestRunPlanGroupById(testRunPlanGroupHasFromUI.getTestRunPlanGroup().getTestRunPlanGroupId());
				testRunPlanGroupHasFromUI.setTestRunPlanGroup(tesrRunPlanGroup);
				productListService.update(testRunPlanGroupHasFromUI);		
				jTableResponse = new JTableResponse("OK",new JsonTestRunPlanGroupHasTestRunPlan(testRunPlanGroupHasFromUI));	
			    } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to update the testrunplangroup!");
		            log.error("JSON ERROR updating TestrunplangroupHasTestRunPlan", e);
		        }	        
	        return jTableResponse;
		}
		
		@RequestMapping(value="product.clone.build",method=RequestMethod.POST ,produces="application/json")
	  public @ResponseBody JTableResponse cloneProductBuild(HttpServletRequest req,@RequestParam int sourceBuildId,@RequestParam String buildNo,@RequestParam String buildName,
	  @RequestParam String buildDes,@RequestParam int buildTypeId,@RequestParam String buildDate) {	
	  		JTableResponse jTableResponse = null;	
	  		try {
				UserList user = (UserList)req.getSession().getAttribute("USER");
				ProductBuild productBuild = productListService.getProductBuildIdWithCompleteInitialization(sourceBuildId);
				if(productBuild != null){
					String errorMessage=commonService.duplicateName(buildName, "ProductBuild", "buildname", "Product Build", "productVersion.productMaster.productId="+productBuild.getProductVersion().getProductMaster().getProductId());
					if (errorMessage != null) {
						jTableResponse = new JTableResponse("ERROR","Product Build Already Exists");
						return jTableResponse;
					}else{
						ProductBuild newProductBuild = new ProductBuild();
						newProductBuild.setBuildname(buildName);
						newProductBuild.setBuildDescription(buildDes);
						newProductBuild.setBuildNo(buildNo);
						DefectIdentificationStageMaster buildType = testExecutionBugsService.getDefectIdentificationStageMasterById(buildTypeId);
						newProductBuild.setBuildType(buildType);
						newProductBuild.setStatus(1);
						newProductBuild.setBuildDate(DateUtility.dateformatWithOutTime(buildDate));
						newProductBuild.setProductVersion(productBuild.getProductVersion());
						newProductBuild.setClonedBuild(productBuild);
						newProductBuild.setProductMaster(productBuild.getProductMaster());
						newProductBuild.setCreatedDate(new Date());
						newProductBuild.setModifiedDate(new Date());
						productListService.addProductBuild(newProductBuild);
						List<WorkPackage> setOfWorkPackages =workPackageService.getActiveWorkpackagesByProductBuildId(productBuild.getProductBuildId());

						if(setOfWorkPackages != null && setOfWorkPackages.size()>0){
							for (WorkPackage workPackage : setOfWorkPackages) {
								WorkPackage clonedWp = workPackageService.addWorkpackageToClonedProductBuild(newProductBuild, user, req, workPackage);
								
								if(clonedWp!=null && clonedWp.getWorkPackageId()!=null){
									log.info(" clonedWp workpackageId*******"+clonedWp.getWorkPackageId());
									mongoDBService.addWorkPackage(clonedWp.getWorkPackageId());
								}
							
								
								workPackageService.copyPlanningDetailsInClonedWorkPacakge(MONGODB_AVAILABLE, workPackage.getWorkPackageId(), clonedWp, user);
								log.info("Cloning workpackage success."+clonedWp.getName());
							}
							log.info("Build Cloning workpackage  success.");
							jTableResponse= new JTableResponse("OK","Product Build Cloned Successfully");
							return jTableResponse;
						}else{
							jTableResponse= new JTableResponse("ERROR","No Workpackage Available for this Build.");
							return jTableResponse;
						}
					}
				}else{
					jTableResponse= new JTableResponse("ERROR","No Product Build Available");
					return jTableResponse;
				}
			} catch (Exception e) {
				  log.error("JSON ERROR Cloning ProductBuild", e);	   
			}
	  		return jTableResponse;
	    }
		
		@RequestMapping(value="product.environment.group.details",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getEnvironmentGroupDetails(@RequestParam int eGroupId){	
			log.debug("product.decoupling.category.details");
			JTableResponse jTableResponse = null;
			List<JsonEnvironmentGroup> jsonEnvironmentGroupList = new ArrayList<JsonEnvironmentGroup>();
				try {
					if(eGroupId != 0){
						EnvironmentGroup egroup = environmentService.getEnvironmentGroupById(eGroupId);
						if(egroup != null){
							jsonEnvironmentGroupList.add(new JsonEnvironmentGroup(egroup));
								jTableResponse = new JTableResponse("OK", jsonEnvironmentGroupList,jsonEnvironmentGroupList.size() );
						}
					}else{
						jTableResponse = new JTableResponse("OK", jsonEnvironmentGroupList, 0);
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching EnvironmentGroup!");
		            log.error("JSON ERROR getting EnvironmentGroup", e);	            
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="product.environment.category.details",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getEnvironmentCategoryDetails(@RequestParam int eCategoryId){	
			log.debug("product.decoupling.category.details");
			JTableResponse jTableResponse = null;
			List<JsonEnvironmentCategory> jsonEnvironmentCategoryList = new ArrayList<JsonEnvironmentCategory>();
				try {
					if(eCategoryId != 0){
						EnvironmentCategory ec = environmentService.getEnvironmentCategoryById(eCategoryId);
						if(ec != null){
							jsonEnvironmentCategoryList.add(new JsonEnvironmentCategory(ec));
								jTableResponse = new JTableResponse("OK", jsonEnvironmentCategoryList,jsonEnvironmentCategoryList.size() );
						}
					}else{
						jTableResponse = new JTableResponse("OK", jsonEnvironmentCategoryList, 0);
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching EnvironmentCategory!");
		            log.error("JSON ERROR getting EnvironmentCategory", e);	            
		        }
	        return jTableResponse;
	    }
		
	@RequestMapping(value="product.features.import", method=RequestMethod.POST ,produces="text/plain" )
	public @ResponseBody JTableResponse productFeaturesDataimport(HttpServletRequest request,@RequestParam Integer productId) {
		log.debug("product.features.import");
		JTableResponse jTableResponse;
		try {
			
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile multipartFile = null;
			String fileName = "";
			InputStream is=null;
			Iterator<String> iterator = multipartRequest.getFileNames();
			File fileForProcess = null;
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileName = multipartFile.getOriginalFilename();
				is=multipartFile.getInputStream();
			}
			
			String isImportComplete = excelTestDataIntegrator.importProductFeatures(fileName, productId, is);			
			if(isImportComplete != null){
				
				jTableResponse = new JTableResponse("Ok","Import of Product Features Completed."+"  "+isImportComplete );
			} else{
				
				jTableResponse = new JTableResponse("Ok","Import completed"+"  "+isImportComplete);
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in of Product Features Import");
			log.error("JSON ERROR productFeaturesData import", e);
		}
		return jTableResponse;
	}
	@RequestMapping(value="runConfiguration.listbyTestRunPlan",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listRunConfigurationByProductVersion(@RequestParam int testRunPlanId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {			
		JTableResponse jTableResponse=null;			 
		try {
			
			List<RunConfiguration> runConfigurationsList=new LinkedList<RunConfiguration>();
			TestRunPlan testRunPLan = productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
			Set<RunConfiguration> testPlanRunConfigurationList = new HashSet<RunConfiguration>();
			testPlanRunConfigurationList = testRunPLan.getRunConfigurationList();
			
			runConfigurationsList=productListService.listRunConfigurationByTestRunPlanId(testRunPlanId,jtStartIndex,jtPageSize);
			
			Set<JsonRunConfiguration> jsonRunConfigurationList=new HashSet<JsonRunConfiguration>();
			if(runConfigurationsList.size()!=0){
				for(RunConfiguration runConfiguration: runConfigurationsList){
					if(runConfiguration.getGenericDevice() == null){
		            	GenericDevices gd = new GenericDevices();
		            	gd.setGenericsDevicesId(-1);
		            	gd.setUDID(" - ");
		            	runConfiguration.setGenericDevice(gd);
		            }
		            if(runConfiguration.getHostList() == null){
		            	HostList hl = new HostList();
		            	hl.setHostId(-1);
		            	hl.setHostName(" - ");
		            	runConfiguration.setHostList(hl);
		            }
					JsonRunConfiguration jsonRunConfiguration = new JsonRunConfiguration(runConfiguration);
					jsonRunConfigurationList.add(jsonRunConfiguration);					
				}			
			}
			if(testPlanRunConfigurationList != null && testPlanRunConfigurationList.size() > 0){
				for(RunConfiguration rc : testPlanRunConfigurationList){
					for(JsonRunConfiguration jsrc : jsonRunConfigurationList){
						if(jsrc.getRunconfigId().equals(rc.getRunconfigId())){
							jsrc.setIsSelected(1);
							jsonRunConfigurationList.add(jsrc);
						}
					}					
				}
			}
			jTableResponse = new JTableResponse("OK",new LinkedList(jsonRunConfigurationList),jsonRunConfigurationList.size());  
			
			jsonRunConfigurationList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show RunConfiguration!");
	            log.error("JSON ERROR listing RunConfiguration By ProductVersion", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="change.requests.import", method=RequestMethod.POST ,produces="text/plain" )
		public @ResponseBody JTableResponse activitiesChangeRequests(HttpServletRequest request,@RequestParam Integer productId,@RequestParam Integer entityType) {
			log.debug("change.requests.import");
			JTableResponse jTableResponse;
			try {
				log.info("Product Id input:" + productId);
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				CommonsMultipartFile multipartFile = null;
				String fileName = "";
				InputStream is=null;
				Iterator<String> iterator = multipartRequest.getFileNames();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
					fileName = multipartFile.getOriginalFilename();
					is=multipartFile.getInputStream();
				}
				
				String isImportComplete = " ";
				
				isImportComplete = excelTestDataIntegrator.importChangeRequests(request,fileName, productId, is, entityType);
				
				if(isImportComplete != null){
					log.info("Import Activities Completed.");
					jTableResponse = new JTableResponse("Ok","Import Change Requests Completed."+" "+isImportComplete);
				} else{
					log.info("Import completed");
					jTableResponse = new JTableResponse("Ok","Import completed"+" "+isImportComplete);
				}
			} catch (Exception e) {
				jTableResponse = new JTableResponse("Error in Import");
				log.error("JSON ERROR activitiesChangeRequests Import", e);
			}
			return jTableResponse;
		}
	
	@RequestMapping(value = "defect.report.fix.fail.report", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody	JTableResponse defectReportFixFailReport(@RequestParam Integer productId) {
		log.debug("inside defect.report.fix.fail.report");
		JTableResponse jTableResponse = null;
		
		try {
			List<JsonRiskHazardTraceabilityMatrix> jsonriskTraceMatrixList = new ArrayList<JsonRiskHazardTraceabilityMatrix>();
			
			jsonriskTraceMatrixList = productListService.fixFailReportService(productId);
			if (jsonriskTraceMatrixList == null || jsonriskTraceMatrixList.size() ==0 ) {

				jTableResponse = new JTableResponse("OK", jsonriskTraceMatrixList, 0);
			}else{
								
				jTableResponse = new JTableResponse("OK", jsonriskTraceMatrixList,jsonriskTraceMatrixList.size() );
				jsonriskTraceMatrixList = null;
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching RiskTraceMatrix!");
			log.error("JSON ERROR getting defectReportFixFailReport", e);	            
		}
		return jTableResponse;
	}
	

	@RequestMapping(value = "test.fix.fail.details.report", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody	JTableResponse testFixFailReportDetails(@RequestParam Integer productId) {
		log.debug("inside test.fix.fail.details.report");
		JTableResponse jTableResponse = null;
		
		try {
			List<JsonRiskHazardTraceabilityMatrix> jsonriskTraceMatrixList = new ArrayList<JsonRiskHazardTraceabilityMatrix>();
			jsonriskTraceMatrixList = productListService.testFixFailReportService(productId);
			if (jsonriskTraceMatrixList == null || jsonriskTraceMatrixList.size() ==0 ) {

				jTableResponse = new JTableResponse("OK", jsonriskTraceMatrixList, 0);
			}else{
				jTableResponse = new JTableResponse("OK", jsonriskTraceMatrixList,jsonriskTraceMatrixList.size() );
				jsonriskTraceMatrixList = null;
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching RiskTraceMatrix!");
			log.error("JSON ERROR getting testFixFailReportDetails", e);	            
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="product.testsuite.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestSuite(HttpServletRequest request, @ModelAttribute JsonTestSuiteList jsonTestSuiteList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		boolean flg = true;
		if(result.hasErrors()){
			jTableSingleResponse= new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {			
				TestSuiteList testSuiteListFromUI=jsonTestSuiteList.getTestSuiteList();
				if(testSuiteListFromUI.getTestSuiteScriptFileLocation() != null){
					trimmingSuite = testSuiteListFromUI.getTestSuiteScriptFileLocation().trim();
				}
								
				if(jsonTestSuiteList.getProductId() != 0){
					ProductMaster productFromUI = productListService.getProductById(jsonTestSuiteList.getProductId());	
					testSuiteListFromUI.setProductMaster(productFromUI);
					Set<ProductVersionListMaster> prodVersions  = productFromUI.getProductVersionListMasters();
					if(prodVersions!=null){
						while(flg){
							for(ProductVersionListMaster pv : prodVersions){
								jsonTestSuiteList.setProductVersionListId(pv.getProductVersionListId());
								flg = false;
							}
						}
					}					
				}
				
				if(jsonTestSuiteList.getProductVersionListId()!=null && jsonTestSuiteList.getProductVersionListId() != 0){
					ProductVersionListMaster productVersionFromUI = productListService.getProductVersionListMasterById(jsonTestSuiteList.getProductVersionListId());
					testSuiteListFromUI.setProductVersionListMaster(productVersionFromUI);					
				}
				if(jsonTestSuiteList.getExecutionTypeId() != null){
					ExecutionTypeMaster executionTypeMasterFromUI = executionTypeMasterService.getExecutionTypeByExecutionTypeId(jsonTestSuiteList.getExecutionTypeId());
					testSuiteListFromUI.setExecutionTypeMaster(executionTypeMasterFromUI);
				}
				
				
				if(jsonTestSuiteList.getTestScriptType() != null){
					ScriptTypeMaster scriptTypeMaster = new ScriptTypeMaster();
					scriptTypeMaster.setScriptType(jsonTestSuiteList.getTestScriptType());
					testSuiteListFromUI.setScriptTypeMaster(scriptTypeMaster);
				}else{
					testSuiteListFromUI.setScriptTypeMaster(null);
				}	
				testSuiteListFromUI.setTestSuiteScriptFileLocation(trimmingSuite);				
				
				String errorMessage = ValidationUtility.validateForNewVersionTestSuiteAddition(testSuiteListFromUI, testSuiteConfigurationService);
				
				if (errorMessage != null) {					
					int testSuiteId = ValidationUtility.returnVersionTestSuiteId(testSuiteListFromUI.getTestSuiteName(), jsonTestSuiteList.getProductVersionListId(),testSuiteConfigurationService);
					if(testSuiteId != 0){
						jTableSingleResponse = new JTableSingleResponse("ERROR",testSuiteId);
						return jTableSingleResponse;
					}
				}
				testSuiteConfigurationService.addTestSuite(testSuiteListFromUI);
				
	            jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestSuiteList(testSuiteListFromUI));
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding TestSuite!");
	            log.error("JSON ERROR adding TestSuite", e);
	        }
	        
        return jTableSingleResponse;
    }	
	
	@RequestMapping(value="testFactoryId.By.productId",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getTestFactoryIdByProductId(@RequestParam Integer productId) {
		JTableResponse jTableResponse = null;
		log.debug("inside testFactoryId.By.productId");
		try{
		ProductMaster productMaster = productListService.getProductById(productId);
		List<JsonProductMaster> jsonProductMasterList = new ArrayList<JsonProductMaster>();
		jsonProductMasterList.add(new JsonProductMaster(productMaster));
		jTableResponse = new JTableResponse("OK", jsonProductMasterList);
		}catch(Exception e){
			jTableResponse = new JTableResponse("ERROR","Error fetching Products by productId");
			log.error("JSON ERROR getting Products", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="administration.product.list.options.by.ids",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listProductsByTestFactoryAndProductIds(HttpServletRequest request, @RequestParam int testFactoryId,@RequestParam int productId) {
		log.debug("administration.product.list.options.by.ids");
		JTableResponseOptions jTableResponseOptions;
		try {
			List<ProductMaster> productMaster = null;
			productMaster=productListService.getCustmersbyProductId(testFactoryId, productId);
			List<JsonProductMaster> jsonProductMaster=new ArrayList<JsonProductMaster>();
			if(productMaster != null && productMaster.size()>0){
				for(ProductMaster pm: productMaster){
					pm.setProductMode(null);
					pm.setProductType(null);
					jsonProductMaster.add(new JsonProductMaster(pm));
				}
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductMaster);     
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching Products By TestFactory!");
	            log.error("JSON ERROR listing Products By TestFactoryAndProductIds", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	//Fetching oldValue and Labeling FieldName
	public HashMap<String, String> getModifiedColDetails(String entityTypeName, String jsonModifiedField, String jsonNewFieldVaue, Integer entityPrimaryId){
		String modifiedFieldOldValue ="";
		HashMap<String, String> modifiedDetails = new HashMap<String, String>();
		
		try {
			if(entityTypeName.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT)){
				modifiedFieldOldValue = commonService.getModifiedFieldOldValue(jsonModifiedField, IDPAConstants.ENTITY_PRODUCT, "product_master", "productId", ""+entityPrimaryId, null);
				
				if(jsonModifiedField.equalsIgnoreCase("productTypeId")){
					jsonModifiedField = "ProductType";
					if(modifiedFieldOldValue != null && !modifiedFieldOldValue.equalsIgnoreCase("")){
						ProductType oldProdType = productListService.getProductTypeById(Integer.parseInt(modifiedFieldOldValue.trim()));
						if(oldProdType != null)
							modifiedFieldOldValue = oldProdType.getTypeName();
						}
						if(jsonNewFieldVaue != null && !jsonNewFieldVaue.equalsIgnoreCase("")){
							ProductType newProdType = productListService.getProductTypeById(Integer.parseInt(jsonNewFieldVaue.trim()));
							if(newProdType != null)
								jsonNewFieldVaue = newProdType.getTypeName();	
						}														
				}					
			}
			else if(entityTypeName.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT_VERSION)){
				modifiedFieldOldValue = commonService.getModifiedFieldOldValue(jsonModifiedField, IDPAConstants.ENTITY_PRODUCT_VERSION, "product_version_list_master", "productVersionListId", ""+entityPrimaryId, null);			
			}
			else if(entityTypeName.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT_BUILD)){
					if(jsonModifiedField.equalsIgnoreCase("productBuildNo")){
						jsonModifiedField = "buildNo";
					}else if(jsonModifiedField.equalsIgnoreCase("productBuildName")){
						jsonModifiedField = "buildName";
					}else if(jsonModifiedField.equalsIgnoreCase("productBuildDescription")){
						jsonModifiedField = "buildDescription";
					}else if(jsonModifiedField.equalsIgnoreCase("productBuildDate")){
						jsonModifiedField = "buildDate";
					}
					modifiedFieldOldValue = commonService.getModifiedFieldOldValue(jsonModifiedField, IDPAConstants.ENTITY_PRODUCT_BUILD, "product_build", "productBuildId", ""+entityPrimaryId, null);
					
					if(jsonModifiedField.equalsIgnoreCase("buildTypeId")){
						if(!modifiedFieldOldValue.equalsIgnoreCase("")){
							DefectIdentificationStageMaster oldBuildType = testExecutionBugsService.getDefectIdentificationStageMasterById(Integer.parseInt(modifiedFieldOldValue.trim()));
							if(oldBuildType != null)
								modifiedFieldOldValue = oldBuildType.getStageName();
						}
						if(!jsonNewFieldVaue.equalsIgnoreCase("")){
							DefectIdentificationStageMaster newBuildType =  testExecutionBugsService.getDefectIdentificationStageMasterById(Integer.parseInt(jsonNewFieldVaue.trim()));								
							if(newBuildType != null)
								jsonNewFieldVaue = newBuildType.getStageName();
						}	
						jsonModifiedField = "BuildType";
					}												
			}
			else if(entityTypeName.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT_FEATURE_LABEL)){
				
			}
			else if(entityTypeName.equalsIgnoreCase(IDPAConstants.ENTITY_ENVIRONMENT)){
				
			}
			else if(entityTypeName.equalsIgnoreCase(IDPAConstants.PRODUCT_DEVICE)){
				
			}
			else if(entityTypeName.equalsIgnoreCase(IDPAConstants.ENTITY_TEST_RUN_PLAN)){
				
			}
			modifiedDetails.put("modifiedField", jsonModifiedField);
			modifiedDetails.put("modifiedFieldOldValue", modifiedFieldOldValue);
			modifiedDetails.put("newFieldValue", jsonNewFieldVaue);
		} catch (NumberFormatException e) {
			 log.error("ERROR getting ModifiedColDetails" , e);
		}	
		return modifiedDetails;
	}
	
	@RequestMapping(value="testrunplan.listbytestCase",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestRunPlanByTestCaseId(@RequestParam int testCaseId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {			
		JTableResponse jTableResponse=null;;			 
		try {
			List<TestRunPlan> testRunPlanList = productListService.listTestRunPlanBytestCaseId(testCaseId);
			Set<TestRunPlan> testPlanSet = new LinkedHashSet<TestRunPlan>();
			for(TestRunPlan testPlan : testRunPlanList){
				testPlanSet.add(testPlan);
			}
			
			if(testPlanSet == null || testPlanSet.size() == 0){
				 jTableResponse = new JTableResponse("ERROR","Test case is not mapped with test run plan");
				 return jTableResponse;
			}
			List<JsonTestRunPlan> jsonTestRunPlans=new ArrayList<JsonTestRunPlan>();
			for(TestRunPlan testRunPlan: testPlanSet){				
				JsonTestRunPlan jsonTestRunPlan=new JsonTestRunPlan(testRunPlan);
				//jsonTestRunPlan.setStatus(0);
				jsonTestRunPlans.add(jsonTestRunPlan);
			}
			
			jTableResponse = new JTableResponse("OK", jsonTestRunPlans,jsonTestRunPlans.size());		
			jsonTestRunPlans = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show TestRunPlan!");
	            log.error("JSON ERROR listing TestRunPlan By TestCaseId", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="testRunPlan_list_For_BDD",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getTestRunPlan(@RequestParam Integer testRunPlanId ) {
		JTableResponse jTableResponse = null;
		try {
			
			TestRunPlan testRunPlan = productListService.getTestRunPlanById(testRunPlanId);
			List<Attachment> testDataFiles = new ArrayList<Attachment>();
			List<Attachment> objectRepositoryFiles = new ArrayList<Attachment>();
			List<Attachment> attachmentsForTestRunPlan = new ArrayList<Attachment>();
			attachmentsForTestRunPlan = attachmentService.listDataRepositoryAttachments(testRunPlan.getTestRunPlanId());
			
			if(testRunPlan != null){
			Set<TestSuiteList>  testSteSet = testRunPlan.getTestSuiteLists();
				if(testSteSet.size() > 0){
					for(TestSuiteList testSuite : testSteSet){
						if(testSuite != null){
							if(testSuite.getScriptTypeMaster() != null && testSuite.getScriptTypeMaster().getScriptType().equalsIgnoreCase("GHERKIN") ){
								if(attachmentsForTestRunPlan!=null && attachmentsForTestRunPlan.size() > 0){
									for(Attachment attach : attachmentsForTestRunPlan){
										Attachment attachment = attach;
										if(attachment.getAttachmentType().equalsIgnoreCase(IDPAConstants.TDTYPE_OBJ_REPOSITORY)){
											objectRepositoryFiles.add(attachment);
										} else if(attachment.getAttachmentType().equalsIgnoreCase(IDPAConstants.TDTYPE_TESTDATA)){
											testDataFiles.add(attachment);
										}
									}
								}
								
								if(objectRepositoryFiles.size() == 0 ){
									return jTableResponse = new JTableResponse("ERROR","Please select Object Repository File");
								}
							}
							
							//Send attachments to the test run plan execution trigger
							if(testSuite.getScriptPlatformLocation() != null && testSuite.getScriptPlatformLocation().equalsIgnoreCase("SERVER")){
								if(testSuite.getTestSuiteScriptFileLocation() != null && testSuite.getTestSuiteScriptFileLocation().endsWith("zip")){
									//UnZip the file and extract the attachment and overwrite
									File ff = new File(testSuite.getTestSuiteScriptFileLocation());
									String sourceFileLocation = System.getProperty("catalina.home")+File.separator+"webapps"+File.separator+request.getContextPath()+File.separator+"TestScripts"+File.separator+ff.getName();
									String destFileLocation = System.getProperty("catalina.home")+File.separator+"webapps"+File.separator+request.getContextPath()+File.separator+"TestScripts"+File.separator;
									ZipTool.unzip(sourceFileLocation,destFileLocation);
									String folderName = sourceFileLocation.replace(".zip","");
									File file = new File(folderName);
									if(file.exists()){
										List<Attachment> testRunPlanAttachments = attachmentService.getTestRunPlanAttachments(9, testRunPlan.getTestRunPlanId());
										if(testRunPlanAttachments != null && testRunPlanAttachments.size() > 0){
											 File root = new File( file.getAbsolutePath());
										        File[] list = root.listFiles();
										        try{	
										        	for(Attachment attachment : testRunPlanAttachments){
										        		for ( File f : list ) {
												            if ( f.isDirectory() ) {
												            	continue;
												            } else {
												            	if(f.getName().equalsIgnoreCase(attachment.getAttributeFileName()+attachment.getAttributeFileExtension())){
												            		String fileName = f.getAbsolutePath();
												            		FileUtils.delete(f);
												            		Files.copy(new File(attachment.getAttributeFileURI()), new File(fileName));									            		
												            	} else {
												            		Files.copy(f,file);
												            	}
												            }
												        }
										        	}									        
										        }catch(Exception e){
										        	log.error("Unable to find the Test Complete Project Suite File Location"+e);
										        }
										}
										FileUtils.delete(new File(sourceFileLocation));
										ZipTool.dozip(folderName);
										FileUtils.delete(new File(folderName));
									}									
								}
							} else if(testSuite.getScriptPlatformLocation() != null && testSuite.getScriptPlatformLocation().equalsIgnoreCase("TERMINAL")){
								List<Attachment> testRunPlanAttachments = attachmentService.getTestRunPlanAttachments(9, testRunPlan.getTestRunPlanId());
								if(testRunPlanAttachments != null && testRunPlanAttachments.size() > 0){
									String sourceFileLocation = System.getProperty("catalina.home")+File.separator+"webapps"+File.separator+request.getContextPath()+File.separator+"TestScripts"+File.separator+testRunPlan.getTestRunPlanId();
									File file = new File(sourceFileLocation);
									if(!file.exists())
										file.mkdirs();
									for(Attachment attachment : testRunPlanAttachments){
										File sourceFile = new File(attachment.getAttributeFileURI());
										if(sourceFile.exists())
											Files.copy(sourceFile, new File(sourceFileLocation+File.separator+attachment.getAttributeFileName()+attachment.getAttributeFileExtension()));		        
						        	}		
									ZipTool.dozip(sourceFileLocation);
									FileUtils.delete(new File(sourceFileLocation));	
								}								
							}
						}
					}
				}
			}					
			 jTableResponse = new JTableResponse("OK");  
		    } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to update the TestRunPlan!");
	            log.error("JSON ERROR getting TestRunPlan", e);
	        }	        
        return jTableResponse;
	}
	
	@RequestMapping(value="attachment.list.for.product",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getAttachmentListForProduct(@RequestParam Integer productId, @RequestParam Integer productVersionId, @RequestParam int testRunPlanId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		JTableResponse jTableResponse = null;		
		try {			
				List<Object[]> unMappedTRPAttachmentsListObj = attachmentService.listAllAttachmentsForProduct(productId, productVersionId, testRunPlanId, jtStartIndex, jtPageSize);	
				JSONArray unMappedJsonArray = new JSONArray();
				for (Object[] row : unMappedTRPAttachmentsListObj) {
					JSONObject jsobj =new JSONObject();
					jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
					jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]+ " [ "+(String)row[2]+" ] ");	
					unMappedJsonArray.add(jsobj);					
				}				
				jTableResponse = new JTableResponse("OK", unMappedJsonArray,unMappedJsonArray.size());
				unMappedTRPAttachmentsListObj = null;
			
		    } catch (Exception e) {
	           log.error("JSON ERROR getting Attachment of Product", e);
	        }	        
        return jTableResponse;
	}
	
	@RequestMapping(value="attachment.count.for.product",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse getAttachmentCountForProduct(@RequestParam Integer productId, @RequestParam Integer productVersionId, @RequestParam int testRunPlanId) {
		JTableSingleResponse jTableSingleResponse;
		log.info("inside the attachment.count.for.product");	
		
		int unMappedFeatureofTC = 0;		
		JSONObject unMappedFeatureCountObj =new JSONObject();
		try {
			unMappedFeatureofTC = attachmentService.getUnMappedAttachmentCount(productId, productVersionId, testRunPlanId);
			unMappedFeatureCountObj.put("unMappedTCCount", unMappedFeatureofTC);						
			jTableSingleResponse = new JTableSingleResponse("OK",unMappedFeatureCountObj);
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to fetch unmappedFeature count to testcase!");
	            log.error("JSON ERROR getting AttachmentCount For Product", e);	 
	        }
	        
        return jTableSingleResponse;

	}
	
	@RequestMapping(value="attachment.mapped.for.testrunplan",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getAttachmentListMappedForTestRunPlan(@RequestParam Integer productId, @RequestParam Integer productVersionId, @RequestParam int testRunPlanId) {
		JTableResponse jTableResponse = null;		
		try {			
				List<Object[]> mappedTRPAttachmentsListObj = attachmentService.listMappedAttachmentsForTestRunPlan(productId, productVersionId, testRunPlanId);	
				JSONArray mappedJsonArray = new JSONArray();
				for (Object[] row : mappedTRPAttachmentsListObj) {
					JSONObject jsobj =new JSONObject();
					jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
					jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]+ " [ "+(String)row[2]+" ] ");	
					mappedJsonArray.add(jsobj);					
				}				
				jTableResponse = new JTableResponse("OK", mappedJsonArray,mappedJsonArray.size());
				mappedTRPAttachmentsListObj = null;
			
		    } catch (Exception e) {
	           log.error("JSON ERROR getting AttachmentList Mapped ForTestRunPlan", e);
	        }	        
        return jTableResponse;
	}
	
	@RequestMapping(value="testrunplan.attachment.mapping",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse addFeatureToTestCase(@RequestParam Integer attachmentId,@RequestParam Integer testRunPlanId, @RequestParam String maporunmap) {
		JTableResponse jTableResponse = null;
		log.info("inside the testrunplan.attachment.mapping");		
		try {			
				Attachment attachment = attachmentService.getAttachmentById(attachmentId);	
				TestRunPlan testRunPlan = productListService.getTestRunPlanById(testRunPlanId);			
				attachmentService.mapTestRunPlanWithAttachment(attachment, testRunPlan, maporunmap);				
				jTableResponse = new JTableResponse("SUCCESS","Attachments successfully mapped to TestRunPlan");
	        } catch (Exception e) {
	        	jTableResponse = new JTableResponse("ERROR","Unable to map the attachment to test run plan!");
	            log.error("JSON ERROR mapping the attachment to test run plan", e);	 
	        }	        
        return jTableResponse;
    }
	@RequestMapping(value="administration.hostAndDevice.mappedList",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listHostAndDevicetMappedList(@RequestParam int testRunPlanId,@RequestParam int workpackageId,@RequestParam Integer ecId) {
		log.debug("inside administration.hostAndDevice.mappedList");
		JTableResponse jTableResponse = null;			 
		try {				
			TestRunPlan testRunPlan=null;
			Set<RunConfiguration> runConfigurationsList=null;
			List<JsonRunConfiguration> jsonRunConfigurationListSQL = null;
	
			if(testRunPlanId!=-1){
				jsonRunConfigurationListSQL  = environmentService.getMappedHostAndDeviceListFromRunconfigurationTestRunPlanLevel(ecId, testRunPlanId,1);
				jTableResponse = new JTableResponse("OK", jsonRunConfigurationListSQL,jsonRunConfigurationListSQL.size());
				jsonRunConfigurationListSQL = null;					
			}

	       } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Host Details!");
	            log.error("JSON ERROR : Error listing Hosts Mapped", e);
	       }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="get.productFeature.build.mappedList",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JSONObject getProductFeatureAndBuildMappingList(@RequestParam Integer productId,@RequestParam Integer versionId,@RequestParam Integer buildId) {
		log.debug("inside get.productFeature.build.mappedList");
		List<ProductFeatureProductBuildMapping> productFeatureBuildMappedList=null;
		List<ProductBuild> listOfProductBuild =null;
		List<ProductFeature> featureList = null;
		JSONObject finalObj = new JSONObject();
		try {
			JSONArray buildTitleList = new JSONArray();
			JSONArray dataList = new JSONArray();
			

			if((productId != null && productId >0) && (versionId == null || versionId <=0 )&& (buildId == null || buildId <= 0)) {
				featureList = productListService.getFeatureListByProductId(productId, 1, 0, 10000);
				productFeatureBuildMappedList=productListService.getProductFeatureAndProductBuildMappingList(productId);
				listOfProductBuild = productListService.listBuildsByProductId(productId);
			} else if ((versionId != null && versionId >0) && (buildId == null || buildId == 0)) {
				listOfProductBuild=productBuildDAO.list(versionId);
				featureList = productListService.getFeatureListByProductIdAndVersionIdAndBuild(productId,versionId,buildId,1, 0, 10000);
				productFeatureBuildMappedList=productListService.getProductFeatureAndProductBuildMappingByVersionIdOrBuildId(productId,versionId,buildId);
			} else {
				ProductBuild build =productBuildDAO.getByProductBuildId(buildId, 0);
				if(build!= null) {
					listOfProductBuild.add(build);
				}
				featureList = productListService.getFeatureListByProductIdAndVersionIdAndBuild(productId,versionId,buildId,1, 0, 10000);
				productFeatureBuildMappedList=productListService.getProductFeatureAndProductBuildMappingByVersionIdOrBuildId(productId,versionId,buildId);
			}
			int featureIdTitle;
			String featureNameTitle ="";
			if(featureList != null && featureList.size() >0) {
				for(ProductFeature feature:featureList) {
					JSONArray featureDataList = new JSONArray();
					featureIdTitle = feature.getProductFeatureId();
					featureNameTitle = feature.getProductFeatureName();
					featureDataList.add(featureIdTitle);
					featureDataList.add(featureNameTitle);		
					if(listOfProductBuild != null && listOfProductBuild.size() >0) {
						Boolean isBuildMapped = false;
						for(ProductBuild build:listOfProductBuild) {
							log.info("Feature ID : "+ feature.getProductFeatureId() +" Build ID : "+build.getProductBuildId());
							isBuildMapped= false;
							if(productFeatureBuildMappedList != null && productFeatureBuildMappedList.size() >0) {
								for(ProductFeatureProductBuildMapping mappedFeatureAnBuild:productFeatureBuildMappedList ) {									
									if((mappedFeatureAnBuild.getFeatureId().equals(feature.getProductFeatureId())) && (build.getProductBuildId().equals(mappedFeatureAnBuild.getBuildId()))) {
										log.info("Mapped Feature : "+mappedFeatureAnBuild.getFeatureId() +" Mapped Build : "+mappedFeatureAnBuild.getBuildId());
										isBuildMapped = true;
										break;
									} 
								}
							}
												
							if(isBuildMapped){
								featureDataList.add("Yes");
							}else{
								featureDataList.add("No");
							}
						}
					}	
					
					dataList.add(featureDataList);
					featureDataList = new JSONArray();
				}
			}
			
			if(listOfProductBuild != null && listOfProductBuild.size() >0) {
				for(ProductBuild build:listOfProductBuild) {
					JSONObject buildTitle=new JSONObject();
					buildTitle.put("title", "BuildId:"+build.getProductBuildId()+ "~"+build.getBuildname());
					buildTitleList.add(buildTitle);
				}
			}
			
			finalObj.put("COLUMNS", buildTitleList);
			finalObj.put("DATA", dataList);
			
		   } catch (Exception e) {
	       }
       return finalObj;
	}
	
	
	@RequestMapping(value="administration.testsuite.testcase.maprunconfiguration",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse mapTestSuiteTestCasesRunConfiguration(@RequestParam int testCaseId,@RequestParam Integer runconfigId,@RequestParam Integer testSuiteId,@RequestParam String type) {			
		JTableResponse jTableResponse=null;		 
		try {
    	   productListService.mapTestSuiteTestCasesRunConfiguration(runconfigId, testSuiteId,testCaseId,type);
    	   if(type.equals("Add")){
    		   jTableResponse = new JTableResponse("SUCCESS","Selected Test Case successfully mapped to Run Configuration");
    	   }else{
    		   jTableResponse = new JTableResponse("SUCCESS","Selected Test Case successfully unmapped from Run Configuration");
    	   }
		} catch (Exception e) {
	       jTableResponse = new JTableResponse("ERROR","Unable to map test case!");
	        log.error("JSON ERROR : Unable to map TestSuite TestCases TestRunPlan", e);
	    }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.testsuite.mapunmaprunconfig",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse mapUnmapTSRunConfiguration(@RequestParam Integer runConfigId,@RequestParam Integer testSuiteId,@RequestParam String action) {			
		JTableResponse jTableResponse=null;
		try {
		   productListService.mapTestSuiteWithRunConfiguration(runConfigId, testSuiteId,action);
    	   if(action.equals("Add")){
    		   jTableResponse = new JTableResponse("SUCCESS","Test Suite successfully mapped to Run Configuration");
    	   } else if(action.equals("Remove")){
    		   jTableResponse = new JTableResponse("SUCCESS","Test Suite successfully unmapped to Run Configuration");
    	   }
		} catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Unable to map test suite!");
            log.error("JSON ERROR map/Unmap RunConfigurationTestSuites", e);
	    }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="mapping.productFeature.and.productBuild",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse mappingProductFeatureToProductBuild(HttpServletRequest request, @ModelAttribute JsonProductFeatureProductBuildMapping jsonProductFeatureProductBuildMapping,  BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		UserList user=(UserList)request.getSession().getAttribute("USER");
		try {
			ProductFeatureProductBuildMapping productFeatureProductBuildMapping = jsonProductFeatureProductBuildMapping.getProductFeatureProductBuildMapping();
			
			productFeatureProductBuildMapping.setCreatedBy(user);
			productFeatureProductBuildMapping.setModifiedBy(user);
			productFeatureProductBuildMapping.setCreatedDate(new Date());
			productFeatureProductBuildMapping.setModifiedDate(new Date());
			productListService.mappingProductFeatureToProductBuild(productFeatureProductBuildMapping);
			
			if(productFeatureProductBuildMapping != null && productFeatureProductBuildMapping.getId() != null) {
				mongoDBService.addProductFeatueBuildMappingtoMongoDB(productFeatureProductBuildMapping.getId());
			}
			
				jTableSingleResponse = new JTableSingleResponse("OK","Product Feature and Build mapping successfully");		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error mapping  Product Feature and Product Build!");
	            log.error("JSON ERROR mapping  Product Feature and Product Build", e);
	        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="administration.testsuite.testcase.maprunconfig",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse mapTestSuiteTestCasesRunConfig(@RequestParam int testCaseId,@RequestParam Integer runConfigId,@RequestParam Integer testSuiteId,@RequestParam String type) {			
		JTableResponse jTableResponse=null;;			 
		try {
			Boolean flag = productListService.isTestConfigurationTestCaseAlreadyMapped(runConfigId, testSuiteId, testCaseId);
			if(type.equals("Remove") && !flag){
				return jTableResponse = new JTableResponse("ERROR","Test Case is not mapped to this Test Configuration");
			}
			if(type.equals("Add"))
				productListService.mapTestSuiteWithRunConfiguration(runConfigId, testSuiteId, type);	
			productListService.mapTestSuiteTestCasesRunConfiguration(runConfigId, testSuiteId,testCaseId,type);
			if(type.equals("Add")){
				jTableResponse = new JTableResponse("SUCCESS","Selected Test Case successfully mapped to Run Configuration");
			}else{
				jTableResponse = new JTableResponse("SUCCESS","Selected Test Case successfully unmapped from Run Configuration");
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Unable to map test case!");
			log.error("JSON ERROR : Unable to map TestSuite TestCases Run Configuration", e);
		}		        
		return jTableResponse;
    }
	
	@RequestMapping(value="runconfiguration.testtool.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateRunConfiguration(@RequestParam Integer testRunplanId, @ModelAttribute JsonRunConfiguration jsonRunConfiguration) {
		JTableResponse jTableResponse;					
		try {
			Integer envCombId = jsonRunConfiguration.getEnvironmentcombinationId();
			Integer hostId = jsonRunConfiguration.getHostId();
			Integer deviceId = jsonRunConfiguration.getDeviceId();
			HostList host = null;
			GenericDevices genericDevices = null;
			ScriptTypeMaster scriptTypeMaster = null;
			TestToolMaster testToolMaster = null;
			ProductType productType = null;
			String genericDevicename = null;
			String hostname = null;
			List<JsonRunConfiguration> jsonRunConfigurationList = new LinkedList<JsonRunConfiguration>();
			if(deviceId == null || deviceId == -1)
            	deviceId = null;
            if(hostId == null || hostId == -1)
            	hostId = null;
            TestRunPlan testRunPlan = productListService.getTestRunPlanById(testRunplanId);
            Integer productTypeId = null;
            //Added for product type based Test Configuration validation to update or save
            if(jsonRunConfiguration.getProductType() != null)
            	productTypeId = jsonRunConfiguration.getProductType();
           
			RunConfiguration rcFromDB = null;
			//Added for product type based Test Configuration validation to update or save
			if(productTypeId != null && productTypeId > 0)
				rcFromDB = environmentService.isRunConfigurationAlreadyExist(envCombId, testRunplanId,hostId,deviceId,testRunPlan.getProductVersionListMaster().getProductVersionListId(), productTypeId);
			else
				rcFromDB = environmentService.isRunConfigurationAlreadyExist(envCombId, testRunplanId,hostId,deviceId,testRunPlan.getProductVersionListMaster().getProductVersionListId());
				
			RunConfiguration rcFromUI = environmentService.getRunConfiguration(jsonRunConfiguration.getRunconfigId());		
			
			if(rcFromDB != null){
				log.info("Env Comb Id : "+rcFromDB.getEnvironmentcombination().getEnvironment_combination_id());
				if(rcFromUI != null && rcFromDB.getRunconfigId().equals(rcFromUI.getRunconfigId())){
					if(jsonRunConfiguration.getTestToolId() != null){
						testToolMaster = testRunConfigSer.getTestToolMaster(jsonRunConfiguration.getTestToolId());
					}
					rcFromUI.setTestTool(testToolMaster);
					if(jsonRunConfiguration.getTestScriptType() != null){
						scriptTypeMaster = testSuiteConfigurationService.getScriptTypeMasterByscriptType(jsonRunConfiguration.getTestScriptType());
					}
					if(jsonRunConfiguration.getProductType() != null){
						productType = productListService.getProductTypeById(jsonRunConfiguration.getProductType());
					}
					if(jsonRunConfiguration.getHostId() != null){
						host = hostListService.getHostById(jsonRunConfiguration.getHostId());
					}
					rcFromUI.setHostList(host);
					if(jsonRunConfiguration.getDeviceId() != null){
						genericDevices = environmentService.getGenericDevices(jsonRunConfiguration.getDeviceId());
					}
					rcFromUI.setGenericDevice(genericDevices);
					rcFromUI.setProductType(productType);
					rcFromUI.setScriptTypeMaster(scriptTypeMaster);
					rcFromUI.setTestScriptFileLocation(jsonRunConfiguration.getTestScriptFileLocation());
					environmentService.updateRunConfiguration(rcFromUI);
					jsonRunConfigurationList.add(new JsonRunConfiguration(rcFromUI));
					return new JTableResponse("OK", jsonRunConfigurationList,jsonRunConfigurationList.size());
				} else if(rcFromUI != null && rcFromDB.getRunconfigId() != rcFromUI.getRunconfigId()){
					return new JTableResponse("ERROR","Run configuration already exists!");
				}
		    }			
			
            EnvironmentCombination environmentCombination = environmentService.getEnvironmentCombinationById(jsonRunConfiguration.getEnvironmentcombinationId()); 	               	
			rcFromUI.setRunConfigStatus(1);
			//Device List
            if(jsonRunConfiguration.getDeviceId() != null){
            	genericDevices = environmentService.getGenericDevices(jsonRunConfiguration.getDeviceId());
            	if(genericDevices != null){
            		String deviceName = genericDevices.getName();           	
            		genericDevicename = environmentCombination.getEnvironmentCombinationName()+":"+deviceName;
            	}  
            }
            //Host List
            if(jsonRunConfiguration.getHostId() != null){
	            host = hostListService.getHostById(jsonRunConfiguration.getHostId());
	            hostname = environmentCombination.getEnvironmentCombinationName()+":"+host.getHostName();	            
            }
			if (jsonRunConfiguration.getProductType() == IDPAConstants.PRODUCT_TYPE_DEVICE
					|| jsonRunConfiguration.getProductType() == IDPAConstants.PRODUCT_TYPE_MOBILE) {
				rcFromUI.setRunconfigName(genericDevicename);
			} else {
				rcFromUI.setRunconfigName(hostname);
			}
	        rcFromUI.setHostList(host);
			rcFromUI.setGenericDevice(genericDevices);
			rcFromUI.setEnvironmentcombination(environmentCombination);				
            rcFromUI.setProduct(testRunPlan.getProductVersionListMaster().getProductMaster());
			rcFromUI.setProductVersion(testRunPlan.getProductVersionListMaster());
			rcFromUI.setTestRunPlan(testRunPlan);
            rcFromUI.setTestTool(testRunConfigSer.getTestToolMaster(jsonRunConfiguration.getTestToolId()));	            
            rcFromUI.setScriptTypeMaster(testSuiteConfigurationService.getScriptTypeMasterByscriptType(jsonRunConfiguration.getTestScriptType()));
            //Added for product type based Test Configuration validation to update or save
            if(jsonRunConfiguration.getProductType() != null){
				productType = productListService.getProductTypeById(jsonRunConfiguration.getProductType());
			}
            rcFromUI.setProductType(productType);
        	environmentService.updateRunConfiguration(rcFromUI);
        	jsonRunConfigurationList.add(new JsonRunConfiguration(rcFromUI));
			jTableResponse = new JTableResponse("OK",jsonRunConfigurationList, jsonRunConfigurationList.size());
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error updating Run Configuration!");
            log.error("JSON ERROR updating Run Configuration", e);
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="unmapping.productFeature.and.productBuild",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse unMappingProductFeatureToProductBuild(HttpServletRequest request, @ModelAttribute JsonProductFeatureProductBuildMapping jsonProductFeatureProductBuildMapping,  BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {
			ProductFeatureProductBuildMapping productFeatureProductBuildMapping = jsonProductFeatureProductBuildMapping.getProductFeatureProductBuildMapping();
			productListService.unMappingProductFeatureToProductBuild(productFeatureProductBuildMapping);
				jTableSingleResponse = new JTableSingleResponse("OK","Product Feature and Build unmap successfully");		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error unmap  Product Feature and Product Build!");
	            log.error("JSON ERROR unmap  Product Feature and Product Build", e);
	        }
        return jTableSingleResponse;
    }
	
	
	@RequestMapping(value="administration.product.feature.by.versionId.or.buildId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listFeaturesByVersionAndBuild(@RequestParam Integer productMasterId,Integer versionId,Integer buildId,Integer featureStatus, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		JTableResponse jTableResponse;			 
		List<ProductFeature> featuresList =new ArrayList<ProductFeature>();
		try {
			if((productMasterId != null && productMasterId >0) && (versionId == null || versionId <=0 )&& (buildId == null || buildId <=0)) {
				featuresList = productListService.getFeatureListByProductId(productMasterId, featureStatus, jtStartIndex, jtPageSize);
				
			} else {
				featuresList = productListService.getFeatureListByProductIdAndVersionIdAndBuild(productMasterId,versionId,buildId,featureStatus, jtStartIndex, jtPageSize);
			}
			List<JsonProductFeature> jsonProductFeature = new ArrayList<JsonProductFeature>();
			if(featuresList != null && featuresList.size() >0) {
				for(ProductFeature pfeature: featuresList){
					jsonProductFeature.add(new JsonProductFeature(pfeature));
				}				
			} else{
				featuresList =new ArrayList<ProductFeature>();
			}
			
			jTableResponse = new JTableResponse("OK", jsonProductFeature,featuresList.size());     
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Features!");
	            log.error("JSON ERROR fetching Features", e);
	        }		        
        return jTableResponse;
    }
	
	
	@RequestMapping(value="get.testcase.version.mappedList",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JSONObject getTestCaseProductVersionMappingList(@RequestParam Integer productId,@RequestParam Integer versionId,@RequestParam Integer buildId) {
		log.debug("inside get.testcase.version.mappedListt");
		List<TestCaseProductVersionMapping> testCaseProductVersionMappingList=null;
		List<ProductVersionListMaster> productVersionList =new ArrayList<ProductVersionListMaster>();
		List<TestCaseList> testCaseList = null;
		JSONObject finalObj = new JSONObject();
		try {
			
			JSONArray versionTitleList = new JSONArray();
			JSONArray dataList = new JSONArray();
			

			if((productId != null && productId >0) && (versionId == null || versionId <=0 )&& (buildId == null || buildId <= 0)) {
				testCaseList = testCaseService.getTestCaseListByProductId(productId, 0, 10000);
				testCaseProductVersionMappingList=testCaseToVersionMappingService.getTestcaseToProductVersionMappingList(productId);
				productVersionList = productListService.listProductVersion(productId);
			} else if (versionId != null && versionId >0) {
				
				ProductVersionListMaster productVersion = productVersionListMasterDAO.getByProductListId(versionId);
				
				if(productVersion != null) {
					productVersionList.add(productVersion);
					testCaseList = testCaseToVersionMappingService.getTestCaseByProductIdAndVersionIdAndBuild(productId, versionId, 0, 10000);
					testCaseProductVersionMappingList=testCaseToVersionMappingService.getTestCaseListByProductIdAndVersionIdAndBuild(productId, versionId);
				}
			} 
			String testCaseTitle ="";
			if(testCaseList != null && testCaseList.size() >0) {
				for(TestCaseList testCase:testCaseList) {
					JSONArray testCaseDataList = new JSONArray();
					testCaseTitle="["+testCase.getTestCaseId()+"]"+testCase.getTestCaseName();
					testCaseDataList.add(testCaseTitle);
					if(productVersionList != null && productVersionList.size() >0) {
						Boolean isBuildMapped = false;
						for(ProductVersionListMaster version:productVersionList) {
							isBuildMapped= false;
							if(testCaseProductVersionMappingList != null && testCaseProductVersionMappingList.size() >0) {
								for(TestCaseProductVersionMapping mappedTestCaseVersion:testCaseProductVersionMappingList ) {
									if((mappedTestCaseVersion.getTestCaseId().equals(testCase.getTestCaseId())) && (version.getProductVersionListId().equals(mappedTestCaseVersion.getVersionId()))) {
										isBuildMapped = true;
										break;
									} 
								}
							}
							
							if(isBuildMapped){
								testCaseDataList.add(1);
							}else{
								testCaseDataList.add(0);
							}
						}
					}	
					
					dataList.add(testCaseDataList);
					testCaseDataList = new JSONArray();
				}
			}
			
			if(productVersionList != null && productVersionList.size() >0) {
				for(ProductVersionListMaster version:productVersionList) {
					JSONObject versionTitle=new JSONObject();
					versionTitle.put("title", "VersionId:"+version.getProductVersionListId()+ "~"+version.getProductVersionName());
					versionTitleList.add(versionTitle);
				}
			}
			
			finalObj.put("COLUMNS", versionTitleList);
			finalObj.put("DATA", dataList);
			
		   } catch (Exception e) {
	       }
       return finalObj;
	}
	
	
	@RequestMapping(value="mapping.testcase.and.productVersion",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse mappingTestCaseToProductVersion(HttpServletRequest request, @ModelAttribute JsonTestCaseProductVersionMapping jsonTestCaseProductVersionMapping,  BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {
			TestCaseProductVersionMapping testCaseProductVersionMapping = jsonTestCaseProductVersionMapping.getTestCaseProductVersionMapping();
			testCaseToVersionMappingService.mappingTestCaseToProductVersion(testCaseProductVersionMapping);
			
			if(testCaseProductVersionMapping != null && testCaseProductVersionMapping.getId() != null) {
				mongoDBService.addTestCaseProductVersionMappingtoMongoDB(testCaseProductVersionMapping.getId());
			}
				jTableSingleResponse = new JTableSingleResponse("OK","Testcase and Version mapping successfully");		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error mapping  TestCase and Product Version!");
	            log.error("JSON ERROR mapping Testcase and Product version", e);
	        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="unMapping.testcase.and.productVersion",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse unMappingTestCaseToProductVersion(HttpServletRequest request, @ModelAttribute JsonTestCaseProductVersionMapping jsonTestCaseProductVersionMapping,  BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {
			TestCaseProductVersionMapping testCaseProductVersionMapping = jsonTestCaseProductVersionMapping.getTestCaseProductVersionMapping();
			testCaseToVersionMappingService.unMappingTestCaseToProductVersion(testCaseProductVersionMapping);
				jTableSingleResponse = new JTableSingleResponse("OK","Testcase and Version un Map successfully");		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error un mapping  TestCase and Product Version!");
	            log.error("JSON ERROR mapping Testcase and Product version", e);
	        }
        return jTableSingleResponse;
    }
	
	
	@RequestMapping(value="administration.testCase.by.productId.versionId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestCaseByVersion(@RequestParam Integer productMasterId,Integer versionId,Integer buildId,@RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		JTableResponse jTableResponse;			 
		List<TestCaseList> testCaseList =new ArrayList<TestCaseList>();
		try {
			if((productMasterId != null && productMasterId >0) && (versionId == null || versionId <=0 )&& (buildId == null || buildId <=0)) {
				testCaseList = testCaseList = testCaseService.getTestCaseListByProductId(productMasterId, 0, 10000);
				
			} else {
				testCaseList = testCaseToVersionMappingService.getTestCaseByProductIdAndVersionIdAndBuild(productMasterId, versionId, jtStartIndex, jtPageSize);
			}
			List<JsonTestCaseList> jsonTestCaseList = new ArrayList<JsonTestCaseList>();
			if(testCaseList != null && testCaseList.size() >0) {
				for(TestCaseList testCase: testCaseList){
					jsonTestCaseList.add(new JsonTestCaseList(testCase));
				}				
			} else{
				jsonTestCaseList =new ArrayList<JsonTestCaseList>();
			}
			
			jTableResponse = new JTableResponse("OK", jsonTestCaseList,jsonTestCaseList.size());     
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching TestCases!");
	            log.error("JSON ERROR fetching TestCases", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.runconfig.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestConfiguration(@RequestParam Integer testRunplanId,@ModelAttribute JsonRunConfiguration  jsonRunConfiguration) {
		JTableSingleResponse jTableSingleResponse = null;	
		List<JsonRunConfiguration> jsonRnConfiguration = null;
		try {			
			Integer envCombId = jsonRunConfiguration.getEnvironmentcombinationId();
			Integer hostId = jsonRunConfiguration.getHostId();
			Integer deviceId = jsonRunConfiguration.getDeviceId();
			String name = "";
			HostList host = null;
			GenericDevices genericDevices = null;
			String hostname = null;
			String genericDevicename = null;
			RunConfiguration runConfigurationObj = jsonRunConfiguration.getRunConfiguration();
            TestRunPlan testRunPlan = productListService.getTestRunPlanById(testRunplanId);
            Set<TestSuiteList> testPlanTestSuiteSet = testRunPlan.getTestSuiteLists();  
            EnvironmentCombination environmentCombination = environmentService.getEnvironmentCombinationById(jsonRunConfiguration.getEnvironmentcombinationId());            
			Integer productTypeId = jsonRunConfiguration.getProductType();
			
            if(deviceId == null || deviceId == -1)
            	deviceId = null;
            if(hostId == null || hostId == -1)
            	hostId = null;
            RunConfiguration rc = environmentService.isRunConfigurationAlreadyExist(envCombId, testRunplanId,hostId,deviceId,testRunPlan.getProductVersionListMaster().getProductVersionListId(),productTypeId);
			if(rc != null){
				log.info("Env Comb Id : "+rc.getEnvironmentcombination().getEnvironment_combination_id());
				if(rc.getEnvironmentcombination().getEnvironment_combination_id().intValue() == envCombId.intValue()
						&& (rc.getProductType() != null && rc.getProductType().getProductTypeId() != null && 
						rc.getProductType().getProductTypeId().intValue() == productTypeId)){					
					return new JTableSingleResponse("ERROR","Run Configuration already exists!");
				}
			} 
			
			environmentCombination.setEnvionmentCombinationStatus(1);
			environmentService.updateEnvironmentCombination(environmentCombination);
            runConfigurationObj.setEnvironmentcombination(environmentCombination);
            //
            
            //Device List
            if(jsonRunConfiguration.getDeviceId() != null){
            	genericDevices = environmentService.getGenericDevices(jsonRunConfiguration.getDeviceId());
            	if(genericDevices != null){
            		String deviceName = genericDevices.getName();           	
            		genericDevicename = environmentCombination.getEnvironmentCombinationName()+":"+deviceName;
            	}  
            }
            //Host List
            if(jsonRunConfiguration.getHostId() != null){
	            host = hostListService.getHostById(jsonRunConfiguration.getHostId());
	            if(host != null){
		            hostname = environmentCombination.getEnvironmentCombinationName()+":"+host.getHostName();	  
	            }
            }
                  
           
            runConfigurationObj.setGenericDevice(genericDevices);
            runConfigurationObj.setProduct(testRunPlan.getProductVersionListMaster().getProductMaster());
            if(jsonRunConfiguration.getProductType() == IDPAConstants.PRODUCT_TYPE_DEVICE || jsonRunConfiguration.getProductType() == IDPAConstants.PRODUCT_TYPE_MOBILE){
            	runConfigurationObj.setRunconfigName(genericDevicename);
            }else{
            	runConfigurationObj.setRunconfigName(hostname);
            }    
            runConfigurationObj.setProductVersion(testRunPlan.getProductVersionListMaster());
          
            runConfigurationObj.setWorkPackage(null);
            runConfigurationObj.setHostList(host);
            runConfigurationObj.setRunConfigStatus(1);//Set Status 1 for Mapping
            //Set Test Tool
            runConfigurationObj.setTestTool(testRunConfigSer.getTestToolMaster(jsonRunConfiguration.getTestToolId()));
            //Set Script Type
            runConfigurationObj.setScriptTypeMaster(testSuiteConfigurationService.getScriptTypeMasterByscriptType(jsonRunConfiguration.getTestScriptType()));
            runConfigurationObj.setTestScriptFileLocation(jsonRunConfiguration.getTestScriptFileLocation());          
			runConfigurationObj.setTestRunPlan(testRunPlan); 
			runConfigurationObj.setCopyTestPlanTestSuite(jsonRunConfiguration.getCopyTestPlanTestSuite());
            RunConfiguration runConfiguration = environmentService.addRunConfiguration(runConfigurationObj); 	
            if(runConfiguration.getGenericDevice() == null){
            	GenericDevices gd = new GenericDevices();
            	gd.setGenericsDevicesId(-1);
            	gd.setUDID(" - ");
            	runConfiguration.setGenericDevice(gd);
            }
            if(runConfiguration.getHostList() == null){
            	HostList hl = new HostList();
            	hl.setHostId(-1);
            	hl.setHostName(" - ");
            	runConfiguration.setHostList(hl);
            }
            if(runConfiguration.getCopyTestPlanTestSuite().equalsIgnoreCase("1")){
            	if(testPlanTestSuiteSet != null && !testPlanTestSuiteSet.isEmpty()) { 
    		        //Update Test Configuration test Suite mapping
    				for(TestSuiteList testPlanTestSuite : testPlanTestSuiteSet){
    					productListService.mapTestSuiteWithRunConfiguration(runConfiguration.getRunconfigId(),testPlanTestSuite.getTestSuiteId(),"Add");	
    					List<TestCaseList> testCaseLists = productListService.getTestSuiteTestCaseMapped(testRunPlan.getTestRunPlanId(), testPlanTestSuite.getTestSuiteId());
    					productListService.mapTestConfigurationTestSuiteTestCase(runConfiguration.getRunconfigId(),testPlanTestSuite.getTestSuiteId(),testCaseLists, "Add");
    				}
    			}
            }
        	        	      
            if(runConfiguration != null){            	  
            	jsonRnConfiguration = new ArrayList<JsonRunConfiguration>();
            	jsonRnConfiguration.add(new JsonRunConfiguration(runConfiguration));
            	return new JTableSingleResponse("OK",jsonRnConfiguration);
			}            
        } catch (Exception e) {
        	jTableSingleResponse = new JTableSingleResponse("ERROR","Error Adding Run Configuration!");
            log.error("JSON ERROR Adding Run Configuration", e);
        }		        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="getISERecommended.Testcases.by.buildId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getISERecommendationTestcases(@RequestParam Integer buildId) {
		JTableResponse jTableResponse;		
		List<JsonISERecommendationTestcases> jsonIseRecommendationTestcases = null;
		try {	
			List<JsonISERecommendationTestcases> jsonISERecommendationTestcases = new ArrayList<JsonISERecommendationTestcases>();
			List<Object[]> iseRecommendationTestcases=productListService.getISERecommendatedTestCaseListByBuildId(buildId);
			
			if(iseRecommendationTestcases != null && iseRecommendationTestcases.size() >0) {
				JsonISERecommendationTestcases jsonISERecommendationTestcase = null;
				for(Object[] iseRecommendationTestCase:iseRecommendationTestcases) {
					
					jsonISERecommendationTestcase = new JsonISERecommendationTestcases();
					Integer id=(Integer)iseRecommendationTestCase[0];
					Integer recommenedBuildId=(Integer)iseRecommendationTestCase[1];
					String recommendedCategory=(String)iseRecommendationTestCase[3];
					String ST=(String)iseRecommendationTestCase[5];
					String NT=(String)iseRecommendationTestCase[6];
					String title=(String)iseRecommendationTestCase[8];
					String GT=(String)iseRecommendationTestCase[10];
					String ET=(String)iseRecommendationTestCase[11];
					String CT=(String)iseRecommendationTestCase[13];
					String BT=(String)iseRecommendationTestCase[14];
					String feature=(String)iseRecommendationTestCase[15];
					String probablity=(String)iseRecommendationTestCase[16];
					String picked=(String)iseRecommendationTestCase[17];
					String defaultProbablity=(String)iseRecommendationTestCase[18];
					String HFT=(String)iseRecommendationTestCase[19];
					String UST=(String)iseRecommendationTestCase[20];
					String LFT=(String)iseRecommendationTestCase[22];
					String ragValue=(String)iseRecommendationTestCase[23];
					String testRunPlanId=(String)iseRecommendationTestCase[24];
					Date planObtainDate=(Date)iseRecommendationTestCase[25];
					String testBeds=(String)iseRecommendationTestCase[26];
					
					if((BT != null && !BT.isEmpty()) && BT.equalsIgnoreCase("true")) {
						jsonISERecommendationTestcase.setBT("Yes");
					} else {
						jsonISERecommendationTestcase.setBT("No");
					}
					jsonISERecommendationTestcase.setBuildId(recommenedBuildId);
					if((CT != null && !CT.isEmpty()) && CT.equalsIgnoreCase("true")) {
						jsonISERecommendationTestcase.setCT("Yes");
					} else {
						jsonISERecommendationTestcase.setCT("No");
					}
					
					jsonISERecommendationTestcase.setDefect_prob(defaultProbablity);
					if((ET != null && !ET.isEmpty()) && ET.equalsIgnoreCase("true")) {
						jsonISERecommendationTestcase.setET("Yes");
					} else {
						jsonISERecommendationTestcase.setET("No");
					}
					jsonISERecommendationTestcase.setFeature(feature);
					if((GT != null && !GT.isEmpty()) && GT.equalsIgnoreCase("true")) {
						jsonISERecommendationTestcase.setGT("Yes");
					} else {
						jsonISERecommendationTestcase.setGT("No");
					}
					if((HFT != null && !HFT.isEmpty()) && HFT.equalsIgnoreCase("true")) {
						jsonISERecommendationTestcase.setHFT("Yes");
					} else {
						jsonISERecommendationTestcase.setHFT("No");
					}

					jsonISERecommendationTestcase.setId(id);
					
					if((LFT != null && !LFT.isEmpty()) && LFT.equalsIgnoreCase("true")) {
						jsonISERecommendationTestcase.setLFT("Yes");
					} else {
						jsonISERecommendationTestcase.setLFT("No");
					}
					if((NT != null && !NT.isEmpty()) && NT.equalsIgnoreCase("true")) {
						jsonISERecommendationTestcase.setNT("Yes");
					} else {
						jsonISERecommendationTestcase.setNT("No");
					}

					jsonISERecommendationTestcase.setPicked(picked);
					jsonISERecommendationTestcase.setProbability(probablity);
					jsonISERecommendationTestcase.setRag_value(ragValue);
					jsonISERecommendationTestcase.setRecommendationCategory(recommendedCategory);
					if((ST != null && !ST.isEmpty()) && ST.equalsIgnoreCase("true")) {
						jsonISERecommendationTestcase.setST("Yes");
					} else {
						jsonISERecommendationTestcase.setST("No");
					}

					jsonISERecommendationTestcase.setTestbed(testBeds);
					jsonISERecommendationTestcase.setTestCaseName(title);
					jsonISERecommendationTestcase.setTestRunPlanId(testRunPlanId);
					jsonISERecommendationTestcase.setTitle(title);
					if((UST != null && !UST.isEmpty()) && UST.equalsIgnoreCase("true")) {
						jsonISERecommendationTestcase.setUST("Yes");
					} else {
						jsonISERecommendationTestcase.setUST("No");
					}
					jsonISERecommendationTestcase.setPlanUptainTime( DateUtility.dateToString(planObtainDate));
					jsonISERecommendationTestcases.add(jsonISERecommendationTestcase);
				}
			}
			
			
			jTableResponse = new JTableResponse("OK", jsonISERecommendationTestcases,jsonISERecommendationTestcases.size());     
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Ise Recommendation Testcases!");
	            log.error("JSON ERROR fetching Ise Recommendation Testcases", e);
	        }		        
        return jTableResponse;
	}
	
	@RequestMapping(value="process.ise.recommendation.Testcases.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse addRecommendationTestcasesToTAF(@RequestParam Integer productId,@RequestParam Integer buildId) {
		JTableResponse jTableResponse;		
		List<JsonISERecommendationTestcases> jsonIseRecommendationTestcases = null;
		try {	
			List<JsonISERecommendationTestcases> jsonISERecommendationTestcases = new ArrayList<JsonISERecommendationTestcases>();
			List<ISERecommandedTestcases> iseRecommendationTestcases=workPackageService.addRecommendationTestcasePlan(productId, buildId);
			
			if(iseRecommendationTestcases != null && iseRecommendationTestcases.size() >0) {
				for(ISERecommandedTestcases iseRecommendationTestCase:iseRecommendationTestcases) {
					jsonISERecommendationTestcases.add(new JsonISERecommendationTestcases(iseRecommendationTestCase));
				}
			}
			
			
			jTableResponse = new JTableResponse("OK", jsonISERecommendationTestcases,jsonISERecommendationTestcases.size());     
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Ise Recommendation Testcases!");
	            log.error("JSON ERROR fetching Ise Recommendation Testcases", e);
	        }		        
        return jTableResponse;
	}
	
	@RequestMapping(value="administration.remove.runconfiguration",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteRunConfiguration(@RequestParam Integer runConfigId, @RequestParam Integer testRunPlanId) {                  
		JTableResponse jTableResponse=null;                  
		try {             	 
			productListService.unMapRunConfigurationTestRunPlan(testRunPlanId, runConfigId);
          	RunConfiguration runConfiguration=productListService.getRunConfigurationById(runConfigId);
          	environmentService.deleteRunConfiguration(runConfiguration);		                    
          	jTableResponse = new JTableResponse("OK", "Test Configuration Deleted!");   
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Unable to remove Test Configuration!");
			
			log.error("JSON ERROR : Unable to remove RunConfiguration for TestRunPlan", e);
		}                   
       	return jTableResponse;
	}
	

	@RequestMapping(value="product.feature.overall.details",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getProductFeatureOverAllDetails(@RequestParam int featureId, HttpServletRequest req) {
		log.debug("inside product.feature.overall.details");
		UserList user=(UserList)req.getSession().getAttribute("USER");
		JTableResponse jTableResponse = null;
			try {
				req.getSession().setAttribute("user", user);					
				ProductFeature feature = productFeatureDAO.getByProductFeatureId(featureId);			
				Integer mappedTestcaseCount=productListService.getMappedTestcasecountByFeatureId(featureId);
				List<JsonProductFeature> jsonProductFeatures=new ArrayList<JsonProductFeature>();
				if (feature == null ) {						
					jTableResponse = new JTableResponse("OK", jsonProductFeatures, 0);
				} else {					
					feature.setMappedTestcaseCount(mappedTestcaseCount);
					jsonProductFeatures.add(new JsonProductFeature(feature));
					
					jTableResponse = new JTableResponse("OK", jsonProductFeatures, jsonProductFeatures.size());
					feature = null;
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="get.ise.recommendation.testcase.category.count",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse getIseRecommenationTestcaseCategoryCount(@RequestParam Integer buildId) {  
		JTableSingleResponse jTableSingleResponse;
		
		List<HashMap<String, Integer>> testCaseRecommedationCategoryList =new ArrayList<HashMap<String, Integer>>();
		HashMap<String, Integer> testCaseRecommedationCategory =new HashMap<String, Integer>();
		
		try {
			String iseDefaultCategories= "ST,NT,GT,ET,CT,BT,HFT,UST,LFT";
			String [] categories=iseDefaultCategories.split(",");
			List<Object[]> recommendationTestcaseCategoriestCount=productListService.getISERecommendatedTestCaseCategoryCountByBuildId(buildId);
			if(recommendationTestcaseCategoriestCount != null && recommendationTestcaseCategoriestCount.size() >0) {
				for(Object [] category:recommendationTestcaseCategoriestCount) {
					String ctg=(String)category[0];
					BigInteger count=(BigInteger)category[1];
					testCaseRecommedationCategory.put(ctg,count.intValue());
				}
			}
			
			if(testCaseRecommedationCategory != null && testCaseRecommedationCategory.size() >0) {
				
				for(String category : categories){
					if(testCaseRecommedationCategory.get(category) != null) {
						
					} else {
						testCaseRecommedationCategory.put(category, 0);
					}
					
				}
			}else {
				
				for(String category : categories){
					testCaseRecommedationCategory.put(category, 0);
				}
				
			}
			jTableSingleResponse = new JTableSingleResponse("OK", testCaseRecommedationCategory);
        } catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to fetch ise recommendation testcase category  count!");
            log.error("Error in getIseRecommenationTestcaseCategoryCount - ", e);	 
        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="testrunplan.build.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse testRunPlanBuildUpdate(@ModelAttribute JsonTestRunPlan jsontestRunPlan, BindingResult result) {
		JTableResponse jTableResponse;
		List<JsonTestRunPlan> jsonTestRunPlans=new ArrayList<JsonTestRunPlan>();
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {	
			TestRunPlan trp = jsontestRunPlan.getTestRunPlan();
			trp.setStatus(1);
			UserList user= userListService.getUserListById(1);
			trp.setUserList(user);
			TestRunPlan tr = productListService.getTestRunPlanById(trp.getTestRunPlanId());
			trp.setTestSuiteLists(tr.getTestSuiteLists());
			trp.setRunConfigurationList(tr.getRunConfigurationList());
			if(trp.getUseIntelligentTestPlan().equalsIgnoreCase("1"))
				trp.setUseIntelligentTestPlan("YES");
			else
				trp.setUseIntelligentTestPlan("NO");
			productListService.updateTestRunPlan(trp);	
			jsonTestRunPlans.add(new JsonTestRunPlan(trp));
			jTableResponse = new JTableResponse("OK", jsonTestRunPlans,jsonTestRunPlans.size());
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error updating Product Build to Test Run Plan!");
            log.error("JSON ERROR updating Product Build to Test Run Plan!", e);
        }
        return jTableResponse;
	}
	
	@RequestMapping(value="get.ise.recommendation.testcase.summary.Details",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse getIseRecommenationTestcaseSummaryDetails(@RequestParam Integer productId,@RequestParam Integer buildId) {  
		JTableSingleResponse jTableSingleResponse;
		
		HashMap<String, String> testCaseRecommedationSummary =new HashMap<String, String>();
		
		try {
			Integer recommendationTestcaseCount=productListService.getISERecommendatedTestCaseCountByBuildId(buildId);
			Integer totalTestCaseCount=testCaseService.getTestCaseListSize(productId);
			testCaseRecommedationSummary.put("recommendetedTestCases", recommendationTestcaseCount.toString());
			testCaseRecommedationSummary.put("totalTestCases", totalTestCaseCount.toString());
			jTableSingleResponse = new JTableSingleResponse("OK", testCaseRecommedationSummary);
        } catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to fetch ise recommendation testcase summary  count!");
            log.error("Error in getIseRecommenationTestcaseSummaryDetails - ", e);	 
        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="get.product.build.by.productId", method=RequestMethod.POST, produces="application/json")
    public  @ResponseBody JTableResponse getProductBuildNodeTree(HttpServletRequest request,@RequestParam Integer productId) {
		log.debug("inside get.product.build.by.productId");
		JTableResponse jTableResponse = null;
		try {
			List<JsonProductBuild> jsonProductBuildList= new ArrayList<JsonProductBuild>();
			List<ProductBuild> productBuildList=productBuildDao.listBuildsByProductId(productId);
			if(productBuildList != null && productBuildList.size() >0) {
				for(ProductBuild build:productBuildList) {
					jsonProductBuildList.add(new JsonProductBuild(build));
				}
			}
			jTableResponse = new JTableResponse("OK", jsonProductBuildList,jsonProductBuildList.size());
		}catch(Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error in get product build details");
			log.error("JSON ERROR", e);
		}
        return jTableResponse;
	}

   	@RequestMapping(value="administration.map.testConfiguration.testRunPlan",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse mapTestConfigurationTestRunPlan(@RequestParam Integer testRunPlanId, @RequestParam String type, @RequestParam Integer runConfigId) {                  
		JTableResponse jTableResponse=null;;                  
		try {
			RunConfiguration runConfigurationFromDB = new RunConfiguration();
			if(runConfigId != null){
				runConfigurationFromDB =productListService.getRunConfigurationById(runConfigId);
			}
			else{
				jTableResponse = new JTableResponse("ERROR","Please select a valid Test Configuration to map");
			}
			if(runConfigurationFromDB != null){
				if(type.equals("map")){
	                productListService.mapTestRunPlanWithTestRunconfiguration(testRunPlanId, runConfigurationFromDB.getRunconfigId(), "Add");
	                jTableResponse = new JTableResponse("OK", "Run Configuration to Test Plan mapped successfully.");
	            }else if(type.equals("unmap")){
	                productListService.mapTestRunPlanWithTestRunconfiguration(testRunPlanId, runConfigurationFromDB.getRunconfigId(), "Remove");
	                jTableResponse = new JTableResponse("OK", "Run Configuration to Test Plan unmapped successfully.");
	             }
			}else{
				jTableResponse = new JTableResponse("ERROR","Please select a valid Test Configuration to map");
			}
			
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Unable to map or unmap Test Configuration");
			log.error("JSON ERROR : Unable to map or unmap RunConfiguration TestRunPlan", e);
		}                   
        	return jTableResponse;
	} 

	@RequestMapping(value="get.versionId.of.productBuild",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody String getProductVersionIdOfProductBuildBybuildId(@RequestParam int productBuildId) {
		log.info("Inside getProductVersionIdOfProductBuildBybuildId ");
		String ffinalResult="";
		JSONArray jproductVersionId = new JSONArray();
		int productVersionId = 0;
		try {   
			if(productBuildId != -1){
				productVersionId = productListService.getProductVersionIdBybuildId(productBuildId);	
			}else{
			}
							
			JSONObject jobj = new JSONObject();
			jobj.put("versionId", productVersionId);
			jproductVersionId.add(jobj);				
					ffinalResult=jproductVersionId.toString();						
					if (ffinalResult == null) {		
						log.info("No Version Id");							
						return "No Data";
					}
	        } catch (Exception e) {
	            log.error("JSON ERROR", e);
	            e.printStackTrace();
	        }		        
        return  ffinalResult;
    }
	
	@RequestMapping(value="testrunplangroup_has_testrunplan.mapped.unmapped.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getTotalTestPlanGroupMappingList(@RequestParam Integer testRunPlanGroupId) {
		log.info("testrunplangroup_has_testrunplan.mapped.unmapped.list");
		JTableResponse jTableResponse;
		try {		
			TestRunPlanGroup testRunPlanGroup =productListService.getTestRunPlanGroupById(testRunPlanGroupId);
			if(testRunPlanGroup == null){
				jTableResponse = new JTableResponse("ERROR","Invalid Test Plan Group ID!");
			}
			if(testRunPlanGroup.getProductVersionListMaster() == null){
				jTableResponse = new JTableResponse("ERROR","Corrupted Test Plan Group.Please create a new group");
			}
			List<TestRunPlan> totalTestPlanList =productListService.listTestRunPlanByProductVersionId(testRunPlanGroup.getProductVersionListMaster().getProductVersionListId(),testRunPlanGroup.getTestRunPlanGroupId());
			List<TestRunPlangroupHasTestRunPlan> mappedTestrunplangrouplist=productListService.listTestRunPlanGroupMap(testRunPlanGroupId);
			List<JsonTestRunPlanGroupHasTestRunPlan> jsonTestRunPlanGroupList = new ArrayList<JsonTestRunPlanGroupHasTestRunPlan>();
			for(TestRunPlan testPlan : totalTestPlanList){
				JsonTestRunPlanGroupHasTestRunPlan jsonTestPlanGroup =  new JsonTestRunPlanGroupHasTestRunPlan();
				jsonTestPlanGroup.setTestRunPlanGroupId(testRunPlanGroup.getTestRunPlanGroupId());
				jsonTestPlanGroup.setTestRunPlanId(testPlan.getTestRunPlanId());
				jsonTestPlanGroup.setTestRunPlanName(testPlan.getTestRunPlanName());
				jsonTestPlanGroup.setIsSelected(0);
				for(TestRunPlangroupHasTestRunPlan testrunplangroup : mappedTestrunplangrouplist){
					if(testrunplangroup.getTestrunplan()!= null && testPlan.getTestRunPlanId().equals(testrunplangroup.getTestrunplan().getTestRunPlanId())){
						jsonTestPlanGroup.setOrder(testrunplangroup.getExecutionOrder());
						jsonTestPlanGroup.setTestRunGroupId(testrunplangroup.getTestRunGroupId());
						jsonTestPlanGroup.setIsSelected(1);
					}
				}
				jsonTestRunPlanGroupList.add(jsonTestPlanGroup);
			}
            jTableResponse = new JTableResponse("OK", jsonTestRunPlanGroupList,jsonTestRunPlanGroupList.size());     
            mappedTestrunplangrouplist = null;
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Testrunplangroup!");
	            log.error("JSON ERROR getting Testrunplangroup Mapping", e);
		}
        return jTableResponse;
    }
}