package com.hcl.atf.taf.controller;

import java.io.BufferedInputStream;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.EnvironmentDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityEntityMaster;
import com.hcl.atf.taf.model.ActivityGroup;
import com.hcl.atf.taf.model.ActivityResult;
import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;
import com.hcl.atf.taf.model.ActivityStatus;
import com.hcl.atf.taf.model.ActivityTaskType;
import com.hcl.atf.taf.model.ActivityType;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.AuthenticationMode;
import com.hcl.atf.taf.model.AuthenticationType;
import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.Comments;
import com.hcl.atf.taf.model.CommentsTypeMaster;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.DefectApprovalStatusMaster;
import com.hcl.atf.taf.model.DefectIdentificationStageMaster;
import com.hcl.atf.taf.model.DefectSeverity;
import com.hcl.atf.taf.model.DefectTypeMaster;
import com.hcl.atf.taf.model.DeviceLab;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.DeviceMakeMaster;
import com.hcl.atf.taf.model.DeviceModelMaster;
import com.hcl.atf.taf.model.DevicePlatformMaster;
import com.hcl.atf.taf.model.DevicePlatformVersionListMaster;
import com.hcl.atf.taf.model.DeviceType;
import com.hcl.atf.taf.model.EngagementTypeMaster;
import com.hcl.atf.taf.model.EntityConfigurationPropertiesMaster;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCategory;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.EnvironmentGroup;
import com.hcl.atf.taf.model.Evidence;
import com.hcl.atf.taf.model.EvidenceType;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.PerformanceLevel;
import com.hcl.atf.taf.model.PlatformType;
import com.hcl.atf.taf.model.Processor;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductLocale;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductMode;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.ResourceShiftCheckIn;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.SCMSystem;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.ShiftTypeMaster;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.SkillLevels;
import com.hcl.atf.taf.model.SystemType;
import com.hcl.atf.taf.model.TestCaseEntityGroup;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestCategoryMaster;
import com.hcl.atf.taf.model.TestDataPlan;
import com.hcl.atf.taf.model.TestEnviromentMaster;
import com.hcl.atf.taf.model.TestEnvironmentDevices;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryLab;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestToolMaster;
import com.hcl.atf.taf.model.TestcaseTypeMaster;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.TimeSheetActivityType;
import com.hcl.atf.taf.model.TrccExecutionPlan;
import com.hcl.atf.taf.model.UserGroup;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.model.VendorMaster;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.dto.VerificationResult;
import com.hcl.atf.taf.model.json.JsonActivity;
import com.hcl.atf.taf.model.json.JsonActivityEntityMaster;
import com.hcl.atf.taf.model.json.JsonActivityGroup;
import com.hcl.atf.taf.model.json.JsonActivityMaster;
import com.hcl.atf.taf.model.json.JsonActivityResult;
import com.hcl.atf.taf.model.json.JsonActivitySecondaryStatusMaster;
import com.hcl.atf.taf.model.json.JsonActivityStatus;
import com.hcl.atf.taf.model.json.JsonActivityTask;
import com.hcl.atf.taf.model.json.JsonActivityTaskType;
import com.hcl.atf.taf.model.json.JsonActivityType;
import com.hcl.atf.taf.model.json.JsonActualShift;
import com.hcl.atf.taf.model.json.JsonAttachment;
import com.hcl.atf.taf.model.json.JsonAttachmentType;
import com.hcl.atf.taf.model.json.JsonAuthenticationMode;
import com.hcl.atf.taf.model.json.JsonAuthenticationType;
import com.hcl.atf.taf.model.json.JsonChangeRequest;
import com.hcl.atf.taf.model.json.JsonClarificationTracker;
import com.hcl.atf.taf.model.json.JsonComments;
import com.hcl.atf.taf.model.json.JsonCommentsTypeMaster;
import com.hcl.atf.taf.model.json.JsonCustomer;
import com.hcl.atf.taf.model.json.JsonDashBoardTabsRoleBasedURL;
import com.hcl.atf.taf.model.json.JsonDecouplingCategory;
import com.hcl.atf.taf.model.json.JsonDefectApprovalStatus;
import com.hcl.atf.taf.model.json.JsonDefectIdentificationStage;
import com.hcl.atf.taf.model.json.JsonDefectSeverity;
import com.hcl.atf.taf.model.json.JsonDefectType;
import com.hcl.atf.taf.model.json.JsonDeviceLab;
import com.hcl.atf.taf.model.json.JsonDeviceList;
import com.hcl.atf.taf.model.json.JsonDeviceMakeMaster;
import com.hcl.atf.taf.model.json.JsonDeviceModelMaster;
import com.hcl.atf.taf.model.json.JsonDevicePlatformMaster;
import com.hcl.atf.taf.model.json.JsonDevicePlatformVersionListMaster;
import com.hcl.atf.taf.model.json.JsonDeviceType;
import com.hcl.atf.taf.model.json.JsonEngagementTypeMaster;
import com.hcl.atf.taf.model.json.JsonEntityConfigurationPropertiesMaster;
import com.hcl.atf.taf.model.json.JsonEntityMaster;
import com.hcl.atf.taf.model.json.JsonEntityRelationship;
import com.hcl.atf.taf.model.json.JsonEnvironment;
import com.hcl.atf.taf.model.json.JsonEnvironmentCategory;
import com.hcl.atf.taf.model.json.JsonEnvironmentCombination;
import com.hcl.atf.taf.model.json.JsonEnvironmentGroup;
import com.hcl.atf.taf.model.json.JsonEvidenceType;
import com.hcl.atf.taf.model.json.JsonExecutionPriority;
import com.hcl.atf.taf.model.json.JsonExecutionTypeMaster;
import com.hcl.atf.taf.model.json.JsonGenericDevice;
import com.hcl.atf.taf.model.json.JsonHostList;
import com.hcl.atf.taf.model.json.JsonLanguage;
import com.hcl.atf.taf.model.json.JsonPerformanceLevel;
import com.hcl.atf.taf.model.json.JsonPlatformType;
import com.hcl.atf.taf.model.json.JsonProcessor;
import com.hcl.atf.taf.model.json.JsonProductBuild;
import com.hcl.atf.taf.model.json.JsonProductFeature;
import com.hcl.atf.taf.model.json.JsonProductLocale;
import com.hcl.atf.taf.model.json.JsonProductMaster;
import com.hcl.atf.taf.model.json.JsonProductMode;
import com.hcl.atf.taf.model.json.JsonProductType;
import com.hcl.atf.taf.model.json.JsonProductVersionListMaster;
import com.hcl.atf.taf.model.json.JsonResourcePool;
import com.hcl.atf.taf.model.json.JsonRunConfiguration;
import com.hcl.atf.taf.model.json.JsonSCMManagementSystem;
import com.hcl.atf.taf.model.json.JsonScriptTypeMaster;
import com.hcl.atf.taf.model.json.JsonShiftTypeMaster;
import com.hcl.atf.taf.model.json.JsonSimpleOption;
import com.hcl.atf.taf.model.json.JsonSkill;
import com.hcl.atf.taf.model.json.JsonSkillLevels;
import com.hcl.atf.taf.model.json.JsonStartWeekEndWeekOption;
import com.hcl.atf.taf.model.json.JsonSystemType;
import com.hcl.atf.taf.model.json.JsonTestCaseEntityGroup;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.JsonTestCasePriority;
import com.hcl.atf.taf.model.json.JsonTestCategoryMaster;
import com.hcl.atf.taf.model.json.JsonTestDataPlan;
import com.hcl.atf.taf.model.json.JsonTestEnvironmentDevices;
import com.hcl.atf.taf.model.json.JsonTestEnvironmentMaster;
import com.hcl.atf.taf.model.json.JsonTestExecutionResultBugList;
import com.hcl.atf.taf.model.json.JsonTestFactory;
import com.hcl.atf.taf.model.json.JsonTestFactoryLab;
import com.hcl.atf.taf.model.json.JsonTestManagementSystem;
import com.hcl.atf.taf.model.json.JsonTestRunList_runno;
import com.hcl.atf.taf.model.json.JsonTestRunPlan;
import com.hcl.atf.taf.model.json.JsonTestSuiteList;
import com.hcl.atf.taf.model.json.JsonTestToolMaster;
import com.hcl.atf.taf.model.json.JsonTestcaseTypeMaster;
import com.hcl.atf.taf.model.json.JsonTimeSheetActivityType;
import com.hcl.atf.taf.model.json.JsonTrccExecutionPlan;
import com.hcl.atf.taf.model.json.JsonUserGroup;
import com.hcl.atf.taf.model.json.JsonUserList;
import com.hcl.atf.taf.model.json.JsonUserRoleMaster;
import com.hcl.atf.taf.model.json.JsonUserTypeMasterNew;
import com.hcl.atf.taf.model.json.JsonVendorMaster;
import com.hcl.atf.taf.model.json.JsonWorkFlow;
import com.hcl.atf.taf.model.json.JsonWorkPackage;
import com.hcl.atf.taf.model.json.JsonWorkShiftMaster;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.ActivityEntityMasterService;
import com.hcl.atf.taf.service.ActivityGroupService;
import com.hcl.atf.taf.service.ActivityResultService;
import com.hcl.atf.taf.service.ActivitySecondaryStatusMasterService;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.ActivityStatusService;
import com.hcl.atf.taf.service.ActivityTaskService;
import com.hcl.atf.taf.service.ActivityTypeService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.AttachmentService;
import com.hcl.atf.taf.service.ChangeRequestService;
import com.hcl.atf.taf.service.ClarificationTrackerService;
import com.hcl.atf.taf.service.CommentsTypeMasterService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.CustomerService;
import com.hcl.atf.taf.service.DataTreeService;
import com.hcl.atf.taf.service.DecouplingCategoryService;
import com.hcl.atf.taf.service.DefectManagementService;
import com.hcl.atf.taf.service.DeviceListService;
import com.hcl.atf.taf.service.DimensionService;
import com.hcl.atf.taf.service.EntityConfigurationPropertiesService;
import com.hcl.atf.taf.service.EntityRelationshipService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.HostListService;
import com.hcl.atf.taf.service.LicenseCheckService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.ResourceManagementService;
import com.hcl.atf.taf.service.SkillService;
import com.hcl.atf.taf.service.TestCaseEntityGroupService;
import com.hcl.atf.taf.service.TestCaseScriptGenerationService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestEnvironmentDeviceService;
import com.hcl.atf.taf.service.TestExecutionBugsService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.TestFactoryManagementService;
import com.hcl.atf.taf.service.TestManagementService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.TimeSheetManagementService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.UserTypeMasterNewService;
import com.hcl.atf.taf.service.VendorListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.ilcm.workflow.model.json.JsonCommonOption;
@Controller
public class CommonListController {
	private static final Log log = LogFactory.getLog(CommonListController.class);
	
	@Autowired
	private ProductListService productListService;	
	@Autowired
	private DeviceListService deviceListService;
	@Autowired
	private TestRunConfigurationService testRunConfigurationService;
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService;
	@Autowired
	private TestEnvironmentDeviceService testEnviornmnetService;
	@Autowired
	private TestExecutionService testExecutionService;
	@Autowired
	private DecouplingCategoryService decouplingCategoryService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private UserTypeMasterNewService userTypeMasterNewService;
	@Autowired
	private UserListService userListService;
	@Autowired
	private VendorListService vendorListService;
	@Autowired
	private ResourceManagementService resourceManagementService;
	@Autowired
	private TestFactoryManagementService testFactoryManagementService;
	@Autowired
	private TimeSheetManagementService timeSheetManagementService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private SkillService skillService;
	@Autowired
	private EnvironmentService environmentService;
	@Autowired
	private DataTreeService dataTreeService;
	@Autowired
	private TestExecutionBugsService testExecutionBugsService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ExecutionTypeMasterService executionTypeMasterService;
	@Autowired
	private TestCaseService testCaseService;
	@Autowired
	private TestRunConfigurationService testRunConfigSer;
	@Autowired
	private EntityConfigurationPropertiesService entityConfigurationPropertiesService;
	@Autowired
	private ActivityStatusService activityStatusService;		
	@Autowired
	private ActivityService activityService;	
	@Autowired
	private ActivityTypeService activityTypeService;
	@Autowired
	private ActivityTaskService activityTaskService;
	@Autowired
	private ChangeRequestService changeRequestService;		
	@Autowired
	private ClarificationTrackerService clarificationTrackerService;	
	@Autowired
	private ActivityResultService activityResultService;
	@Autowired
	private ActivityWorkPackageService activityWorkPackageService;
	@Autowired
	private ActivityGroupService activityGroupService;

	@Autowired
	private ActivityEntityMasterService activityEntityMasterService;
	@Autowired
	private CommentsTypeMasterService commentsTypeMasterService;
	@Autowired
	private ActivitySecondaryStatusMasterService activitySecondaryStatusMasterService;
	@Autowired
	private DimensionService dimensionService;	
	@Autowired
	private AttachmentService attachmentService;	

	@Autowired
	TestManagementService testManagementService;
	
	@Autowired
	EntityRelationshipService entityRelationshipService;
	@Autowired
	ProductMasterDAO productMasterDAO;
	
	@Autowired
	private TestCaseEntityGroupService testCaseEntityGroupService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HostListService hostService;
	@Autowired
	private DefectManagementService defectManagementService;
	@Autowired
	EnvironmentDAO environmentDAO;
    @Autowired
    private TestCaseScriptGenerationService testCaseScriptGenerationService;
	
	@Value("#{ilcmProps['ATTACHMENT_BASE_LOCATION']}")
	private String attachmentBaseLocation;
	@Autowired
	private LicenseCheckService licenseCheckService;
	
	@RequestMapping(value="common.list.product",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listProducts() {
		log.debug("inside common.list.product");
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			
			List<ProductMaster> productMaster=productListService.productsList();
			List<JsonProductMaster> jsonProductMaster=new ArrayList<JsonProductMaster>();
			for(ProductMaster dvm: productMaster){
				jsonProductMaster.add(new JsonProductMaster(dvm));
				//productMaster.remove(pm);
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductMaster);
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.usertypes.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listUsersOfType() {
		log.info("inside common.list.usertypes.list");
		JTableResponseOptions jTableResponseOptions;
	 
	try {		
		List<UserTypeMasterNew> userTypeMasterNewlist=userTypeMasterNewService.list();
		List<JsonUserTypeMasterNew> jsonUserTypeMasterNew =  new ArrayList<JsonUserTypeMasterNew>();
		
		for(UserTypeMasterNew utm: userTypeMasterNewlist){
			jsonUserTypeMasterNew.add(new JsonUserTypeMasterNew(utm));
		}	
		jTableResponseOptions = new JTableResponseOptions("OK", jsonUserTypeMasterNew);         
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }	
	
    return jTableResponseOptions;
    }

	@RequestMapping(value="common.list.product.environments",method=RequestMethod.GET ,produces="application/json")
    public @ResponseBody JTableResponseOptions listProductEnvironments(@RequestParam int workPackageId) {
		log.info("inside common.list.product.environments"+workPackageId);
		JTableResponseOptions jTableResponseOptions = null;
		 
		try {
			WorkPackage workPackage = workPackageService.getWorkPackageById(workPackageId);
			List<Environment> environments=productListService.getEnvironmentListByProductId(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId());
			List<JsonEnvironment> jsonEnvironments=new ArrayList<JsonEnvironment>();
			for(Environment environment: environments){
				
				jsonEnvironments.add(new JsonEnvironment(environment));
				log.info("Env Name : " + environment.getEnvironmentName());
			}
			int environmentCount = jsonEnvironments.size();
			JsonEnvironment dummyEnvironment = null;
			for (int i = environmentCount; i < 10; i++) {
				dummyEnvironment = new JsonEnvironment();
				dummyEnvironment.setEnvironmentId(0-i);
				dummyEnvironment.setEnvironmentName("NA");
				dummyEnvironment.setProductMasterId(1);
				
				jsonEnvironments.add(dummyEnvironment);
			}
			log.info("Total env : " + jsonEnvironments.size());
			jTableResponseOptions = new JTableResponseOptions("OK", jsonEnvironments);
	    } catch (Exception e) {
	        log.error("JSON ERROR", e);
	    }
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="common.trcc.execution.plan.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTrccExecutionPlan(@RequestParam int testRunConfigurationChildId) {
		log.debug("inside common.trcc.execution.plan.list");
		JTableResponseOptions jTableResponseOptions;
			try {
				List<TrccExecutionPlan> trccExecutionPlans=testExecutionService.listTrccExecutionPlan(testRunConfigurationChildId);
				List<JsonTrccExecutionPlan> jsonTrccExecutionPlan=new ArrayList<JsonTrccExecutionPlan>();
				for(TrccExecutionPlan trccExecutionPlan: trccExecutionPlans){
					jsonTrccExecutionPlan.add(new JsonTrccExecutionPlan(trccExecutionPlan));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonTrccExecutionPlan);
	            trccExecutionPlans = null;   
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching TrccExecutionPlan!");
	            log.error("JSON ERROR fetching TrccExecutionPlan", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="productName")
    public String listProductsByNames(ModelMap model) {
		log.debug("inside listProductNames:");
		//
		try {
			List<ProductMaster> productMaster=productListService.productsListByNames();
		//	
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
					.currentRequestAttributes();
			HttpSession session = attributes.getRequest().getSession(true);
			session.setAttribute("productMaster", productMaster);
				model.addAttribute("productMaster",productMaster);
	        } 
		catch (Exception e) {
	            log.error("JSON ERROR", e);
	        }
	        
        return "dropdown";
    }
	@RequestMapping(value="common.list.productversion",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listProductVersions(@RequestParam int productId) {
		log.debug("inside common.list.productversion");
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			
			List<ProductVersionListMaster> productVersionListMaster=productListService.productVersionsList(productId);
			List<JsonProductVersionListMaster> jsonProductVersionListMaster=new ArrayList<JsonProductVersionListMaster>();
			for(ProductVersionListMaster pvlm: productVersionListMaster){
				jsonProductVersionListMaster.add(new JsonProductVersionListMaster(pvlm));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductVersionListMaster);
			productVersionListMaster = null;   
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);	           
        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.runno",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listRunNo() {
		log.debug("common.list.runno");
		JTableResponse jTableResponse;
		
		try {
			//Temp formated class
			class Max implements java.io.Serializable{
				@JsonProperty
				Integer maxRunNo;
				public Max(){
					
				}
				public Max(Integer maxRunNo){
					this.maxRunNo = maxRunNo;
				}
				@JsonProperty
				public Integer getMaxRunNo() {
					return maxRunNo;
				}

				public void setMaxRunNo(Integer maxRunNo) {
					this.maxRunNo = maxRunNo;
				}
				
			}			
			List<Max> maxList= new ArrayList<Max>();			
			maxList.add(new Max(testRunConfigurationService.getMaxRunNo()));			
			jTableResponse = new JTableResponse("OK", maxList,1);
			
	        } catch (Exception e) {
	        	jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }	        
        return jTableResponse;
    }
	
	@RequestMapping(value="common.list.platformsrunno",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listRunNoforPlatform(@RequestParam String pltfm_id, @RequestParam int productId) {
		log.debug("common.list.platformsrunno");
		JTableResponse jTableResponse;
		JTableResponseOptions jTableResponseOptions = null;		
		if((productId!=-1) && (pltfm_id.equalsIgnoreCase("ALL"))){
			try {				
				List<TestRunList> testRunList=testRunConfigurationService.getRunNo(productId, pltfm_id);				
				if(testRunList !=null){					
					List<JsonTestRunList_runno> jsonTestRunList_runno=new ArrayList<JsonTestRunList_runno>();
				
					for(TestRunList tr: testRunList){
							jsonTestRunList_runno.add(new JsonTestRunList_runno(tr));						
					}	
					jTableResponseOptions = new JTableResponseOptions("OK", jsonTestRunList_runno);
				}				
				} catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        } 
		}else if((productId!=-1) && (!pltfm_id.equalsIgnoreCase("ALL"))){
			try {				
				List<TestRunList> testRunList=testRunConfigurationService.getRunNo(productId, pltfm_id);
				List<JsonTestRunList_runno> jsonTestRunList_runno=new ArrayList<JsonTestRunList_runno>();				
				for(TestRunList tr: testRunList){
					jsonTestRunList_runno.add(new JsonTestRunList_runno(tr));				
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonTestRunList_runno);
				} catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        } 
		}else{
			try {
				class Max implements java.io.Serializable{
					@JsonProperty
					Integer maxRunNo;
					public Max(){
						
					}
					public Max(Integer maxRunNo){
						this.maxRunNo = maxRunNo;
					}
					@JsonProperty
					public Integer getMaxRunNo() {
						return maxRunNo;
					}

					public void setMaxRunNo(Integer maxRunNo) {
						this.maxRunNo = maxRunNo;
					}					
				}			
				List<Max> maxList= new ArrayList<Max>();			
				maxList.add(new Max(testRunConfigurationService.getMaxRunNo()));			
				jTableResponse = new JTableResponse("OK", maxList,1);
				
				jTableResponseOptions = new JTableResponseOptions("OK", testRunConfigurationService.getMaxRunNo());
				
		        } catch (Exception e) {
		        	jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
		}
		return jTableResponseOptions;     
    }
		
	@RequestMapping(value="common.list.testenvironment",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestEnvironmentsUsingProductVersionListId(@RequestParam int productVersionListId) {
		log.debug("inside common.list.testenvironment");
		JTableResponseOptions jTableResponseOptions;
		try {
			
			List<TestEnviromentMaster> testEnviromentMaster=testRunConfigurationService.testEnviromentsList(productVersionListId);
			List<JsonTestEnvironmentMaster> jsonTestEnvironmentMaster=new ArrayList<JsonTestEnvironmentMaster>();
			for(TestEnviromentMaster tem: testEnviromentMaster){
				jsonTestEnvironmentMaster.add(new JsonTestEnvironmentMaster(tem));
				//productMaster.remove(pm);
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestEnvironmentMaster);
			testEnviromentMaster = null;   
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching Testenvironment!");
	            log.error("JSON ERROR fetching Testenvironment", e);
	        }
	        
        return jTableResponseOptions;
    }
	@RequestMapping(value="common.list.testsuite",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestSuitesUsingProductVersionListId(@RequestParam int productVersionListId) {
		log.debug("inside common.list.testsuite");
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			List<TestSuiteList> testSuiteList=testSuiteConfigurationService.testSuitesList(productVersionListId);
			List<JsonTestSuiteList> jsonTestSuiteList=new ArrayList<JsonTestSuiteList>();
			for(TestSuiteList tsl: testSuiteList){
				jsonTestSuiteList.add(new JsonTestSuiteList(tsl));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestSuiteList);
			testSuiteList = null;   
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching TestSuites!");
	            log.error("JSON ERROR fetching TestSuitesUsingProductVersionListId", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.testsuite.byproductId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestSuitesByProductId(@RequestParam int productId) {
		log.debug("inside common.list.testsuite");
		JTableResponseOptions jTableResponseOptions;
		try {
			List<TestSuiteList> testSuiteListFromDB=testSuiteConfigurationService.getByProductId(productId);
			List<JsonTestSuiteList> jsonTestSuiteListFromUI=new ArrayList<JsonTestSuiteList>();
			for(TestSuiteList testsuiteobj : testSuiteListFromDB){
				jsonTestSuiteListFromUI.add(new JsonTestSuiteList(testsuiteobj));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestSuiteListFromUI);
			
			testSuiteListFromDB = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching TestSuites!");
	            log.error("JSON ERROR fetching TestSuites", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.testcategory",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestCategories() {
		log.debug("inside common.list.testcategory");
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			
			List<TestCategoryMaster> testCategoryMaster=testRunConfigurationService.testCategoriesList();
			List<JsonTestCategoryMaster> jsonTestCategoryMaster=new ArrayList<JsonTestCategoryMaster>();
			for(TestCategoryMaster tcm: testCategoryMaster){
				jsonTestCategoryMaster.add(new JsonTestCategoryMaster(tcm));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestCategoryMaster);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching TestCategories!");
	            log.error("JSON ERROR fetching TestCategories", e);
	        }
	        
        return jTableResponseOptions;
    }
	@RequestMapping(value="common.list.scripttype",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestScriptTypes() {
		log.debug("inside common.list.scripttype");
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			
			List<ScriptTypeMaster> scriptTypeMaster=testSuiteConfigurationService.testScriptTypeList();
			List<JsonScriptTypeMaster> jsonScriptTypeMaster=new ArrayList<JsonScriptTypeMaster>();
			for(ScriptTypeMaster stm: scriptTypeMaster){
				jsonScriptTypeMaster.add(new JsonScriptTypeMaster(stm));
				
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonScriptTypeMaster);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching TestScriptTypes!");
	            log.error("JSON ERROR fetching TestScriptTypes", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.platform",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDevicePlatforms() {
		log.debug("inside common.list.platform");		
		JTableResponseOptions jTableResponseOptions;
		try {
			List<DevicePlatformMaster> devicePlatformMaster=deviceListService.platformsList();
			List<JsonDevicePlatformMaster> jsonDevicePlatformMaster=new ArrayList<JsonDevicePlatformMaster>();
			
			for(DevicePlatformMaster dvm: devicePlatformMaster){
				jsonDevicePlatformMaster.add(new JsonDevicePlatformMaster(dvm));			
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDevicePlatformMaster);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.productsplatform",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDevicePlatformsforProduct(@RequestParam int productId) {
		log.debug("inside common.list.productsplatform");		
		JTableResponseOptions jTableResponseOptions;
		try {
			List<DevicePlatformMaster> devicePlatformMaster=deviceListService.platformsList(productId);
			List<JsonDevicePlatformMaster> jsonDevicePlatformMaster=new ArrayList<JsonDevicePlatformMaster>();
			
			for(DevicePlatformMaster dvm: devicePlatformMaster){
				jsonDevicePlatformMaster.add(new JsonDevicePlatformMaster(dvm));			
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDevicePlatformMaster);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.TestEnviornmentName",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestEnviornmnets() {
		log.debug("inside common.list.TestEnviornmentName");
		JTableResponseOptions jTableResponseOptions;
		
		try {
			List<TestEnvironmentDevices> testEnviornmentDevices = testEnviornmnetService.listTestEnvironmentDevicesByName();
			List<JsonTestEnvironmentDevices> jsonTestEnviornmentdevices=new ArrayList<JsonTestEnvironmentDevices>();
			for(TestEnvironmentDevices testEnviornmentDevice: testEnviornmentDevices){
				jsonTestEnviornmentdevices.add(new JsonTestEnvironmentDevices(testEnviornmentDevice));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestEnviornmentdevices);
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	@RequestMapping(value="common.list.testenviornmentDescription",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listtestenviornmentDescription(@RequestParam int testEnvironmentDevicesId) {
		log.debug("common.list.testenviornmentDescription");
		
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			
			List<TestEnvironmentDevices> testenviornmentdevices=testEnviornmnetService.listTestEnvironmentDevicesBydescription(testEnvironmentDevicesId);
			
			List<JsonTestEnvironmentDevices> jsonTestEnviornmentdevices=new ArrayList<JsonTestEnvironmentDevices>();
			for(TestEnvironmentDevices ted: testenviornmentdevices){
				jsonTestEnviornmentdevices.add(new JsonTestEnvironmentDevices(ted));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestEnviornmentdevices);
			testenviornmentdevices = null;   
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	           
	        }
	        
        return jTableResponseOptions;
    }
	@RequestMapping(value="common.list.attacheddevice",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listAttachedDevices(@RequestParam int devicePlatformVersionListId) {
		log.debug("inside common.list.attacheddevice");
		
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			
			List<DeviceList> deviceList= testRunConfigurationService.listAttachedDevices(devicePlatformVersionListId);
			List<JsonDeviceList> jsonDeviceList=new ArrayList<JsonDeviceList>();
			for(DeviceList dl: deviceList){
				jsonDeviceList.add(new JsonDeviceList(dl));
				//productMaster.remove(pm);
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDeviceList);
			deviceList = null;   
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.availabledevices",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listAvailableDevices(@RequestParam String devicePlatformVersion) {
		log.debug("inside common.list.listAvailableDevices");
		JTableResponseOptions jTableResponseOptions;
		
		 
		try {
			
			List<DeviceList> deviceList= testRunConfigurationService.listAvailableDevices(devicePlatformVersion);
			
			List<JsonDeviceList> jsonDeviceList=new ArrayList<JsonDeviceList>();
			for(DeviceList dl: deviceList){
				jsonDeviceList.add(new JsonDeviceList(dl));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDeviceList);
	    } catch (Exception e) {
	      	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	        log.error("JSON ERROR", e);
	    }
	        
        return jTableResponseOptions;
    }

	@RequestMapping(value="common.list.platformversion",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDevicePlatformVersions(@RequestParam String devicePlatform) {
		log.debug("inside common.list.platformversion");
		
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			
			List<DevicePlatformVersionListMaster> devicePlatformVersionListMaster=deviceListService.platformVersionsList(devicePlatform);
			List<JsonDevicePlatformVersionListMaster> jsonDevicePlatformVersionListMaster=new ArrayList<JsonDevicePlatformVersionListMaster>();
			for(DevicePlatformVersionListMaster dvm: devicePlatformVersionListMaster){
				jsonDevicePlatformVersionListMaster.add(new JsonDevicePlatformVersionListMaster(dvm));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDevicePlatformVersionListMaster);
			
			
			devicePlatformVersionListMaster = null;   
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }

	@RequestMapping(value="common.list.entities",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listEntities(@RequestParam String entityType, @RequestParam String productId) {
		log.info("inside common.list.entities");
		JTableResponseOptions jTableResponseOptions;
		List<JsonSimpleOption> jsonOptionList=new ArrayList<JsonSimpleOption>();
		 
		try {
			
            log.info("Getting entities list for : " + productId + " : entity type : " + entityType);

            ProductMaster product = productListService.getProductDetailsById(Integer.parseInt(productId));
            if (product == null) {
				
	            log.info("Unable to Find product : " + productId + " : entyity type : " + entityType);
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to find the chosen entity!");
	            return jTableResponseOptions;
			}
			
			JsonSimpleOption jsonSimpleOption = null;
			if (entityType.equals(TAFConstants.ENTITY_PRODUCT)) {
				
				jsonSimpleOption = new JsonSimpleOption(product.getProductName());
	            log.info("Entity Name : " + product.getProductName());
				jsonOptionList.add(jsonSimpleOption);
			} else if (entityType.equals(TAFConstants.ENTITY_PRODUCT_VERSION)) { 
				
				Set<ProductVersionListMaster> productVersions = product.getProductVersionListMasters();
				if(productVersions==null){
					log.info("No Product Versions found");
				} else {
					for (ProductVersionListMaster productVersion : productVersions) {
						jsonSimpleOption = new JsonSimpleOption(productVersion.getProductVersionName());
						log.info("Entity Name : " + productVersion.getProductVersionName());
						jsonOptionList.add(jsonSimpleOption);
					}
				}
			} else if(entityType.equals(TAFConstants.ENTITY_TEST_SUITE)) {
				Set<TestSuiteList> testSuites=product.getTestSuiteLists();
				if(testSuites==null){
					log.info("No TestSuites found");
				} else {
					for(TestSuiteList testSuite:testSuites){
						jsonSimpleOption = new JsonSimpleOption(testSuite.getTestSuiteName());
			            log.info("Entity Name : " + testSuite.getTestSuiteName());
						jsonOptionList.add(jsonSimpleOption);
					}
				}
			}
            log.info("Entity options size : " + jsonOptionList.size());
			jTableResponseOptions = new JTableResponseOptions("OK", jsonOptionList);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }

	@RequestMapping(value="test.suite.case.options.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listTestCasesOptions(@RequestParam String testSuiteId,@RequestParam int productId) {
		log.debug("test.suite.case.options.list");
		int intTmpTestSuiteId=Integer.parseInt(testSuiteId);
		JTableResponseOptions jTableResponseOptions;
			try {
				List<TestCaseList> testCasesListsByTestSuiteId=testSuiteConfigurationService.listTestCase(intTmpTestSuiteId);
				List<TestCaseList> allTestCaseListsByProductId=testSuiteConfigurationService.listAllTestCases(productId);
				List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
				for(TestCaseList testCaseListsByTestSuiteId:testCasesListsByTestSuiteId)
				{
					if(allTestCaseListsByProductId.contains(testCaseListsByTestSuiteId))
					{
						allTestCaseListsByProductId.remove(testCaseListsByTestSuiteId);
					}
				}
				for(TestCaseList listAllTestCaseByProductId:allTestCaseListsByProductId)
				{
					jsonTestCaseList.add(new JsonTestCaseList(listAllTestCaseByProductId));
				}
				jTableResponseOptions = new JTableResponseOptions("OK",jsonTestCaseList);       
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@ExceptionHandler(Exception.class)
	public void handleException(Throwable ex, HttpServletResponse response)
			throws Exception {

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				ex.getMessage());
		ex.printStackTrace();
	}
	
	@RequestMapping(value="product.decouplingcategory.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDecouplingCategories(@RequestParam Integer productId) {
		log.info("inside product.decouplingcategory.list");
		JTableResponseOptions jTableResponseOptions = new JTableResponseOptions();
		try {			
			List<DecouplingCategory> decouplingCategoryList=decouplingCategoryService.getDecouplingCategoryListByProductId(productId);
			List<JsonDecouplingCategory> jsonDecouplingCategoryList=new ArrayList<JsonDecouplingCategory>();			 
			for(DecouplingCategory dc: decouplingCategoryList){
				jsonDecouplingCategoryList.add(new JsonDecouplingCategory(dc));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDecouplingCategoryList,true);
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching Decoupling records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="product.feature.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listFeatures(@RequestParam Integer productId) {
		log.debug("inside product.feature.list");
		JTableResponseOptions jTableResponseOptions;
		try {		
			List<ProductFeature> productFeatureFromDB = productListService.getFeatureListByProductId(productId, null, null, null);
			List<JsonProductFeature> jsonProductFeature = new ArrayList<JsonProductFeature>();
			
			for(ProductFeature pf: productFeatureFromDB){
				jsonProductFeature.add(new JsonProductFeature(pf));
			}
			if(productFeatureFromDB == null || productFeatureFromDB.isEmpty()){
				JsonProductFeature jsonpf = new JsonProductFeature();
				jsonpf.setProductFeatureId(0);
				jsonpf.setProductFeatureName("No Data");
				//jsonpf.setProductFeatureName("Null");
				jsonProductFeature.add(jsonpf);
				jTableResponseOptions = new JTableResponseOptions("OK", jsonProductFeature);
			}else{
				jTableResponseOptions = new JTableResponseOptions("OK", jsonProductFeature);	
			}
						
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching Features!");
	            log.error("JSON ERROR fetching Features!", e);
	        }	        
        return jTableResponseOptions;
    }

	@RequestMapping(value="product.scmsystem.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listSCMSystems(@RequestParam Integer productId) {
		log.debug("inside product.scmsystem.list");
		JTableResponseOptions jTableResponseOptions;
		try {	
			
			List<SCMSystem> scmSystems = defectManagementService.listSCMManagementSystem(productId);
			List<JsonSCMManagementSystem> jsonSCMSystems = new ArrayList<JsonSCMManagementSystem>();
			if (scmSystems == null || scmSystems.isEmpty()) {
				//Send an empty list
			} else {
				for(SCMSystem scmSystem: scmSystems){
					jsonSCMSystems.add(new JsonSCMManagementSystem(scmSystem));
				}
			}
			JsonSCMManagementSystem jsonSCSystem= new JsonSCMManagementSystem();
			jsonSCSystem.setId(null);
			jsonSCSystem.setTitle("--");
			jsonSCMSystems.add(jsonSCSystem);
			jTableResponseOptions = new JTableResponseOptions("OK", jsonSCMSystems);	
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching SCM Systems for product!");
            log.error("JSON ERROR fetching SCM systems for product : " + productId, e);
        }	        
        return jTableResponseOptions;
    }

	
	@RequestMapping(value="common.list.parentfeature.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listProductFeaturesForParent(@RequestParam Integer productID, @RequestParam Integer productFeatureId, @RequestParam String actionType) {

		JTableResponseOptions jTableResponseOptions;
		final boolean flag = false;
		try {	
			List<ProductFeature> productFeatureListFromDB = new ArrayList<ProductFeature>();
			List<JsonProductFeature> jsonProductFeatureList = new ArrayList<JsonProductFeature>();
			log.info(" Updating Parent Feature : " + actionType);
			if(actionType.equalsIgnoreCase("list") || actionType.equalsIgnoreCase("edit")){
				
				productFeatureListFromDB = productListService.getFeatureListExcludingChildByparentFeatureId(productID, productFeatureId);
				log.info("Excluding children");
				
			}else if(actionType.equalsIgnoreCase("create") || actionType.equalsIgnoreCase("default")){
				log.info("Excluding children----- Create  ");
				 productFeatureListFromDB = productListService.getFeatureListByProductId(productID, null, null, false);	
			}else{
				 productFeatureListFromDB = productListService.getFeatureListByProductId(productID, null, null, false);	
				 log.info("Excluding children----- Anything  ");
			}
			
			for(ProductFeature pf : productFeatureListFromDB){				
					jsonProductFeatureList.add(new JsonProductFeature(pf));	
			}
			JsonProductFeature jpf = new JsonProductFeature();
			jpf.setProductFeatureId(1);
			jpf.setProductFeatureName("--");
			jsonProductFeatureList.add( jpf);
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductFeatureList, flag);
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching ProductFeaturesForParent!");
	            log.error("JSON ERROR  fetching ProductFeaturesForParent!", e);
	        }	        
        return jTableResponseOptions;
    }	
	
	@RequestMapping(value="common.list.weekDateNames",method=RequestMethod.GET ,produces="application/json")
    public @ResponseBody JTableResponse getWeekDateNames(@RequestParam int weekNo) {
		log.debug("common.list.weekDateNames");
		JTableResponse jTableResponse;
		try {
			List<JsonSimpleOption> dateNames= new ArrayList<JsonSimpleOption>();			
			List<String> dateNamesString = DateUtility.getDateNamesOfWeek(weekNo);
			if(dateNamesString!=null && dateNamesString.size()>0){
				for (int i = 0; i<7; i++) {
					log.info(" Date sT " + dateNamesString.get(i));
					dateNames.add(new JsonSimpleOption(dateNamesString.get(i)));			
				}
				jTableResponse = new JTableResponse("OK", dateNames, dateNames.size());

			}else{
		       	jTableResponse = new JTableResponse("ERROR","No Records for week!");

			}
			
	    } catch (Exception e) {
	       	jTableResponse = new JTableResponse("ERROR","Error fetching dates for week!");
	        log.error("JSON ERROR", e);	            
	    }	        
        return jTableResponse;
    }
	
	@RequestMapping(value="common.list.weeksName",method=RequestMethod.GET ,produces="application/json")
    public @ResponseBody JTableResponse getWeeksNames(@RequestParam int year, @RequestParam String weekRange) {
		log.debug("common.list.weeksName");
		JTableResponse jTableResponse;
		try {
			List<JsonStartWeekEndWeekOption> dateNames= new ArrayList<JsonStartWeekEndWeekOption>();	
			Set<Integer> weekRangeSet = new HashSet<Integer>();
			weekRangeSet = resourceManagementService.getRecursiveWeeks(weekRange, weekRangeSet);
			
			Calendar calendar = Calendar.getInstance();
			Calendar endWeekcalendar = Calendar.getInstance();
			
			calendar.setMinimalDaysInFirstWeek(7);
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, Calendar.DECEMBER);
			calendar.set(Calendar.DAY_OF_MONTH, 31);
			int totalMonths = calendar.get(Calendar.WEEK_OF_YEAR);
			
			endWeekcalendar.setMinimalDaysInFirstWeek(7);
			endWeekcalendar.set(Calendar.YEAR, year);
			endWeekcalendar.set(Calendar.MONTH, Calendar.DECEMBER);
			endWeekcalendar.set(Calendar.DAY_OF_MONTH, 31);
			

			calendar = Calendar.getInstance();
			calendar.clear();
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			
			endWeekcalendar = Calendar.getInstance();
			endWeekcalendar.clear();
			endWeekcalendar.set(Calendar.YEAR, year);
			if(year == 2018) {
				endWeekcalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			} else {
				endWeekcalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			}
			
			int week = 1;
			int weekRangeSize = weekRangeSet.size();
			List<Date> calendarWeekDates = new ArrayList<Date>();
			List<Date> calendarEndWeekDates = new ArrayList<Date>();
			for (int i = 1; i <= totalMonths; i++) {
				calendar.set(Calendar.WEEK_OF_YEAR, i);
				calendar.set(Calendar.YEAR, year);
				Date date = calendar.getTime();
				calendarWeekDates.add(date);
				
				endWeekcalendar.set(Calendar.WEEK_OF_YEAR, i);
				endWeekcalendar.set(Calendar.YEAR, year);
				Date endWeekdate = endWeekcalendar.getTime();
				if(year == 2018) {
					Calendar formattedEndWeek = Calendar.getInstance();
					formattedEndWeek.setTime( endWeekdate );
					formattedEndWeek.add(Calendar.DAY_OF_MONTH, 1);			
					calendarEndWeekDates.add(formattedEndWeek.getTime());
				} else {
					calendarEndWeekDates.add(endWeekdate);
				}
				
				if(i == 1 && calendar.get(Calendar.MONTH) != Calendar.JANUARY){
					continue;
				}
				
				if(weekRangeSet.contains(week) || weekRangeSize == 0){					
					dateNames.add(new JsonStartWeekEndWeekOption(calendarWeekDates.get(i - 1)+" ~"+week,calendarEndWeekDates.get(i - 1)+""));	
				}
				week++;
			}
			jTableResponse = new JTableResponse("OK", dateNames, dateNames.size());

		} catch (Exception e) {
	       	jTableResponse = new JTableResponse("ERROR","Error fetching dates for week!");
	        log.error("JSON ERROR", e);	            
	    }	        
        return jTableResponse;
    }
	
	
	
	
	
	
	@RequestMapping(value="common.list.parentdecouplingcategory.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDecouplingCategoriesForParent(@RequestParam Integer productID, @RequestParam Integer decouplingCategoryID) {
		log.debug("inside common.list.parentdecouplingcategory.list=== Prd id=="+productID+", decouplingcategoryID=="+decouplingCategoryID);
		JTableResponseOptions jTableResponseOptions;
		 final boolean flag = true;
		try {
			
			List<DecouplingCategory> decouplingCategoryList=decouplingCategoryService.getDecouplingCategoryListByProductId(productID);
			List<JsonDecouplingCategory> jsonDecouplingCategoryList=new ArrayList<JsonDecouplingCategory>();		
				for(DecouplingCategory dc: decouplingCategoryList){	
					
						if(decouplingCategoryID != 0 && dc.getDecouplingCategoryId() != decouplingCategoryID){
							jsonDecouplingCategoryList.add(new JsonDecouplingCategory(dc));	//If DC already exist, then remove the self						
						}else if(decouplingCategoryID ==0){
							jsonDecouplingCategoryList.add(new JsonDecouplingCategory(dc));	//If 1st DC added, all into Parent DC list						
						}		
				}			
				
				JsonDecouplingCategory jdc = new JsonDecouplingCategory();
				jdc.setDecouplingCategoryId(0);
				jdc.setDecouplingCategoryName("Null");
				jsonDecouplingCategoryList.add(jdc);
				
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDecouplingCategoryList, flag);
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }

	
	@RequestMapping(value="administration.user.userType",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listAllUser() {
		log.debug("inside administration.user.userType");
		JTableResponseOptions jTableResponseOptions = null;
			try {
			
				List<UserTypeMasterNew> userTypeMasterNew = userTypeMasterNewService.list();
				List<JsonUserTypeMasterNew> jsonUserTypeMasterNew = new ArrayList<JsonUserTypeMasterNew>();
				
				for(UserTypeMasterNew userTypeMaster: userTypeMasterNew){
					jsonUserTypeMasterNew.add(new JsonUserTypeMasterNew(userTypeMaster));
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonUserTypeMasterNew, true);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.product.user.userType",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listProductUserType(@RequestParam Integer typeFilter) {
		log.debug("inside administration.product.user.userType");
		JTableResponseOptions jTableResponseOptions = null;
			try {
			
				List<UserTypeMasterNew> userTypeMasterNew = userTypeMasterNewService.listProductUser(typeFilter);
				List<JsonUserTypeMasterNew> jsonUserTypeMasterNew = new ArrayList<JsonUserTypeMasterNew>();
				
				for(UserTypeMasterNew userTypeMaster: userTypeMasterNew){
					jsonUserTypeMasterNew.add(new JsonUserTypeMasterNew(userTypeMaster));
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonUserTypeMasterNew, true);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponseOptions;
    }	
	
	@RequestMapping(value="administration.user.listUserRole",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listUserRole() {
		log.debug("inside administration.user.listUserRole");
		JTableResponseOptions jTableResponseOptions = null;
			try {
				
				List<UserRoleMaster> userRoleMaster = productListService.getAllRoles();
				
			     List<JsonUserRoleMaster> jsonUserRoleMaster = new ArrayList<JsonUserRoleMaster>();
				
				for(UserRoleMaster userRole: userRoleMaster){
					jsonUserRoleMaster.add(new JsonUserRoleMaster(userRole));
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonUserRoleMaster);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponseOptions;
    } 

	
	@RequestMapping(value="administration.product.user.listUserRole",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listProductUserRole(@RequestParam Integer typeFilter) {
		log.debug("inside administration.product.user.listUserRole");
		JTableResponseOptions jTableResponseOptions = null;
			try {
				
				List<UserRoleMaster> userRoleMaster = productListService.getProductUserRoles(typeFilter);
				
			     List<JsonUserRoleMaster> jsonUserRoleMaster = new ArrayList<JsonUserRoleMaster>();
				
				for(UserRoleMaster userRole: userRoleMaster){
					jsonUserRoleMaster.add(new JsonUserRoleMaster(userRole));
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonUserRoleMaster);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.user.role.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getUserRoleList(@RequestParam Integer typeFilter) {
		log.debug("inside administration.user.role.list");
		JTableResponseOptions jTableResponseOptions = null;
			try {
				
				List<UserRoleMaster> userRoleMaster = productListService.getProductUserRoles(typeFilter);
				
			     List<JsonUserRoleMaster> jsonUserRoleMaster = new ArrayList<JsonUserRoleMaster>();
				
				for(UserRoleMaster userRole: userRoleMaster){
					jsonUserRoleMaster.add(new JsonUserRoleMaster(userRole));
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonUserRoleMaster, true);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.workshift.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWorkShifts() {
		log.info("common.list.workshift.list=====");
		JTableResponseOptions jTableResponseOptions;
		try {
		
			List<WorkShiftMaster> workShiftMasterList = workPackageService.listAllWorkShift();
			List<JsonWorkShiftMaster> jsonWorkShiftMaster= new ArrayList<JsonWorkShiftMaster>();
			
			for(WorkShiftMaster workShiftMaster: workShiftMasterList){
				jsonWorkShiftMaster.add(new JsonWorkShiftMaster(workShiftMaster));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkShiftMaster);     
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.user.workpackages.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWorkpackgesbyUserId(HttpServletRequest request,@RequestParam int userId,@RequestParam String plannedExecutionDate) {
		log.info("common.list.user.workpackages.list=====");
		JTableResponseOptions jTableResponseOptions;
		List<WorkPackage> workPackagesAssociatedWithUser = new ArrayList<WorkPackage>();
			try {
				if(userId==0){
					UserList user = (UserList)request.getSession().getAttribute("USER");
					userId=user.getUserId();
				}
				UserList user = userListService.getUserListById(userId);
				String roleName = user.getUserRoleMaster().getRoleName();
				if(roleName.equalsIgnoreCase("Administrator") || roleName.equalsIgnoreCase("EngagementManager")){
					workPackagesAssociatedWithUser = workPackageService.list();
				}else if(roleName.equalsIgnoreCase("TestLead") || roleName.equalsIgnoreCase("Tester")){
					if(plannedExecutionDate!=null){
						List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList=workPackageService.listWorkPackageTestCasesExecutionPlan(0,user,plannedExecutionDate,null,null);
						log.info("workPackageTestCaseExecutionPlanList size"+workPackageTestCaseExecutionPlanList.size());
						for(WorkPackageTestCaseExecutionPlan workPTestCasePlan:workPackageTestCaseExecutionPlanList){
						WorkPackage workPackage=	workPTestCasePlan.getWorkPackage();
						if(!workPackagesAssociatedWithUser.contains(workPackage)){
							workPackagesAssociatedWithUser.add(workPackage);
						}
						}
					}else{
						workPackagesAssociatedWithUser = workPackageService.listWorkPackagesByUserId(userId);
					}
					
					log.info("workPackageService.size====="+workPackagesAssociatedWithUser.size());
				}
				List<JsonWorkPackage> jsonWorkPackage = new ArrayList<JsonWorkPackage>();
				
				for(WorkPackage workPackage: workPackagesAssociatedWithUser){
						jsonWorkPackage.add(new JsonWorkPackage(workPackage));
				}			
				
				if(jsonWorkPackage.size() == 0){
					jTableResponseOptions = new JTableResponseOptions("ERROR","No Test Cases!");
					return jTableResponseOptions;
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkPackage, true);  
				jsonWorkPackage=null;
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
	        return jTableResponseOptions;
    }
	

	@RequestMapping(value="common.list.product.locale",method=RequestMethod.GET ,produces="application/json")
    public @ResponseBody JTableResponseOptions listProductlocales(@RequestParam int workPackageId) {
		log.info("inside common.list.product.locale");
		JTableResponseOptions jTableResponseOptions = null;
		 
		try {
			WorkPackage workPackage = workPackageService.getWorkPackageById(workPackageId);
			
			List<ProductLocale> locales=productListService.getProductLocaleListByProductId(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId());
			List<JsonProductLocale> jsonLocales=new ArrayList<JsonProductLocale>();
			for(ProductLocale locale: locales){
				jsonLocales.add(new JsonProductLocale(locale));
				log.info("Locale Name : " + locale.getLocaleName());
			}
			int localeCount = jsonLocales.size();
			JsonProductLocale dummyLocale = null;
			for (int i = localeCount; i < 10; i++) {
				dummyLocale = new JsonProductLocale();
				dummyLocale.setProductLocaleId(0-i);
				dummyLocale.setLocaleName("NA");
				dummyLocale.setProductMasterId(1);
				
				jsonLocales.add(dummyLocale);
			}
			log.info("Total Locale : " + jsonLocales.size());
			jTableResponseOptions = new JTableResponseOptions("OK", jsonLocales);
	    } catch (Exception e) {
	        log.error("JSON ERROR", e);
	    }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.vendor.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listVendors() {			
		JTableResponseOptions jTableResponseOptions;		 
		try {
			List<VendorMaster> vendorList=vendorListService.getVendorMasterList();
			List<JsonVendorMaster> jsonVendorList=new ArrayList<JsonVendorMaster>();
			for(VendorMaster vendorMaster: vendorList){
				jsonVendorList.add(new JsonVendorMaster(vendorMaster));	
				}				
			jTableResponseOptions = new JTableResponseOptions("OK", jsonVendorList,true);     
			vendorList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show Vendor List!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }
	@RequestMapping(value="common.list.resourcepool.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listResourcePools(@RequestParam int resourcePoolId) {			
		JTableResponseOptions jTableResponseOptions;		 
		try {
			List<TestfactoryResourcePool> resourcePoolList=resourceManagementService.getTestfactoryResourcePoolList();
			List<JsonResourcePool> jsonResourcePool=new ArrayList<JsonResourcePool>();
			for(TestfactoryResourcePool rp: resourcePoolList){
				if(rp.getResourcePoolId() == resourcePoolId){
					jsonResourcePool.add(0, new JsonResourcePool(rp));
					}else{
					jsonResourcePool.add(new JsonResourcePool(rp));
				}
			}
						
			jTableResponseOptions = new JTableResponseOptions("OK", jsonResourcePool,true);     
			resourcePoolList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show Resource Pool List!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }
	
	
	
	
	@RequestMapping(value="common.list.testfactorylab.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestFactoryLab() {			
		JTableResponseOptions jTableResponseOptions;			 
		try {
			List<TestFactoryLab> testFactoryLabFromDB = testFactoryManagementService.getTestFactoryLabsList();
			List<JsonTestFactoryLab> jsonTestFactoryLab = new ArrayList<JsonTestFactoryLab>();
			
			for(TestFactoryLab tfl: testFactoryLabFromDB){
				jsonTestFactoryLab.add(new JsonTestFactoryLab(tfl));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestFactoryLab, true);  
			testFactoryLabFromDB=null;			
		
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show TestfactoryLab List!");
	            log.error("JSON ERROR listing TestfactoryLab", e);
	        }		        
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="common.list.testfactorylab.bytestfactorylabid.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestFactoryLab(HttpServletRequest request) {			
		JTableResponseOptions jTableResponseOptions;			 
		try {
			log.info("common.list.testfactorylab.bytestfactorylabid.list");
			
			UserList user = (UserList)request.getSession().getAttribute("USER");
			log.info("User=="+user);
		
			
			Integer testFactoryLabId=user.getResourcePool().getTestFactoryLab().getTestFactoryLabId();
			
			log.info("testFactoryLabId==>"+testFactoryLabId);
			
			 List<TestFactoryLab> testFactoryLabFromDB = testFactoryManagementService.getTestFactoryLabsList(testFactoryLabId);
		
			
			
			List<JsonTestFactoryLab> jsonTestFactoryLab = new ArrayList<JsonTestFactoryLab>();
			
			for(TestFactoryLab tfl: testFactoryLabFromDB){
				jsonTestFactoryLab.add(new JsonTestFactoryLab(tfl));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestFactoryLab, true);  
			testFactoryLabFromDB=null;			
		
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show TestfactoryLab List!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }
	

	@RequestMapping(value="common.administration.user.userList",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listUser() {
		log.debug("inside common.administration.user.userList");
		JTableResponseOptions jTableResponseOptions = null;
			try {
			
				List<UserList> testLeadList = workPackageService.userListByRole(TAFConstants.ROLE_ALL);
				List<JsonUserList> jsonUserList = new ArrayList<JsonUserList>();
				
				for(UserList testLead: testLeadList){
					jsonUserList.add(new JsonUserList(testLead));
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList,true);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.administration.user.userListByResourcePoolId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listUserByResourcePoolId(@RequestParam int resourcePoolId, int productId ) {
		log.debug("inside administration.user.userListByResourcePoolId");
		JTableResponseOptions jTableResponseOptions = null;
		List<JsonUserList> jsonUserList = new ArrayList<JsonUserList>();
			try {
				List<UserList> userList=null;
			log.info("resourcePoolId-->"+resourcePoolId);
			
			if(productId!=-1){
				log.info("inside Testing team user list");
				ProductMaster customer = productListService.getCustomerByProductId(productId);
				Integer customerId=customer.getCustomer().getCustomerId();
//				log.info("product id==>"+productId);
//				log.info("customer Id==>"+customerId);
			List<UserList> customerUserList = userListService.getCustomerUserList(resourcePoolId, customerId);
			log.info("inside for user====>"+customerUserList);
			if(customerUserList != null && customerUserList.size()>0){
			
				for(UserList user: customerUserList){
					jsonUserList.add(new JsonUserList(user));
					
				}
			}
		}
			userList = workPackageService.userListByRole(TAFConstants.ROLE_ALL);
			if(resourcePoolId==0){
			userList=	userListService.getUserListBasedRoleId(1);
			}else{
				userList=	userListService.getUserListBasedRoleId(resourcePoolId);
			}
			if(userList==null || userList.size()==0){
				jTableResponseOptions = new JTableResponseOptions("ERROR","No Resource(s) available in the mapped Resource Pool(s)!");
				 return jTableResponseOptions;
			}
				for(UserList user: userList){
					jsonUserList.add(new JsonUserList(user));
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList,true);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show Users!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponseOptions;
    }
	

	@RequestMapping(value="administration.customer.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listCustomers() {
		log.debug("inside administration.customer.option.list");
		JTableResponseOptions jTableResponseOptions;
		 
		try {			
			List<Customer> customerList=customerService.listCustomer(TAFConstants.ENTITY_STATUS_ACTIVE, null, null);
			
			List<JsonCustomer> jsonCustomerList=new ArrayList<JsonCustomer>();
			if(customerList==null || customerList.size()==0){
				jTableResponseOptions = new JTableResponseOptions("ERROR","Please create new customer!");
				 return jTableResponseOptions;
			}else{
			for(Customer cust: customerList){
				
				jsonCustomerList.add(new JsonCustomer(cust));
			}	
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonCustomerList,true);  
            customerList = null;
			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show Customers!");
	            log.error("JSON ERROR showing Customers!", e);
	        }
        return jTableResponseOptions;		
    }

	@RequestMapping(value="administration.workpackage.environment.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listEnvironmentByWorkpackage(@RequestParam int productMasterId) {			
		JTableResponseOptions jTableResponseOptions =null;		
		
		try {
			List<Environment> environments=productListService.getEnvironmentListByProductId(productMasterId);
			List<JsonEnvironment> jsonEnvironments=new ArrayList<JsonEnvironment>();
			for(Environment environ: environments){
				jsonEnvironments.add(new JsonEnvironment(environ));					
			}				
			jTableResponseOptions = new JTableResponseOptions("OK", jsonEnvironments);     
			environments = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show Environments!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.workpackage.locale.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listLocaleByWorkpackage(@RequestParam int productMasterId) {			
		JTableResponseOptions jTableResponseOptions =null;	
		
		try {
			List<ProductLocale> localeList=productListService.getProductLocaleListByProductId(productMasterId);
			List<JsonProductLocale> jsonLocaleList=new ArrayList<JsonProductLocale>();
			for(ProductLocale locale: localeList){
				jsonLocaleList.add(new JsonProductLocale(locale));	
			}				
			jTableResponseOptions = new JTableResponseOptions("OK", jsonLocaleList);     
			localeList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Err!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }
	

	@RequestMapping(value="administration.user.dcList",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDCByWorkpackage(@RequestParam int workPackageId) {			
		JTableResponseOptions jTableResponseOptions =null;	
		
		try {
			List<DecouplingCategory> decouplingCategoryList=decouplingCategoryService.getDCByWorkpackage(workPackageId);
			List<JsonDecouplingCategory> jsonDecouplingCategory=new ArrayList<JsonDecouplingCategory>();
			if(decouplingCategoryList!=null && !decouplingCategoryList.isEmpty()){
				for(DecouplingCategory decouplingCategory: decouplingCategoryList){
					jsonDecouplingCategory.add(new JsonDecouplingCategory(decouplingCategory));	
				}				
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDecouplingCategory);     
			jsonDecouplingCategory = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }

	@RequestMapping(value="common.testFactory.list.optionByLabId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestFactoryByLabId(@RequestParam int testFactoryLabId) {			
		JTableResponseOptions jTableResponseOptions;			 
		try {
			log.info("calling--->");
			log.info("TestFactoryLab Id"+testFactoryLabId);
			
			List<TestFactory> tfList=testFactoryManagementService.getTestFactoryList(testFactoryLabId,TAFConstants.ENTITY_STATUS_ACTIVE,1);
			List<JsonTestFactory> jsontfList=new ArrayList<JsonTestFactory>();
			for(TestFactory tfactory: tfList){
				jsontfList.add(new JsonTestFactory(tfactory));	
				}				
			jTableResponseOptions = new JTableResponseOptions("OK", jsontfList,true);     
			tfList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }

	
	@RequestMapping(value="timesheet.activityTypes.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTimeSheetActivityTypes(@RequestParam int workPackageId) {			
		JTableResponseOptions jTableResponseOptions =null;	
		
		try {
			List<TimeSheetActivityType> timeSheetActivityTypes = new ArrayList<TimeSheetActivityType>();
			
			List<TimeSheetActivityType> productSpecificActivityTypes = null;
			if(workPackageId != 0){
				WorkPackage workPackage = workPackageService.getWorkPackageById(workPackageId);
				productSpecificActivityTypes = timeSheetManagementService.listProductSpecificTimeSheetActivityTypes(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId());
			}
			
			List<TimeSheetActivityType> genericTimeSheetActivityTypes = timeSheetManagementService.listGenericTimeSheetActivityTypes();
			if(genericTimeSheetActivityTypes != null && genericTimeSheetActivityTypes.size() > 0){
				if(timeSheetActivityTypes.size() == 0){
					timeSheetActivityTypes = genericTimeSheetActivityTypes;
				}else{
					timeSheetActivityTypes.addAll(genericTimeSheetActivityTypes);
				}
			}
			
			if(productSpecificActivityTypes != null && productSpecificActivityTypes.size() > 0){
				if(timeSheetActivityTypes.size() == 0){
					timeSheetActivityTypes = productSpecificActivityTypes;
				}else{
					timeSheetActivityTypes.addAll(productSpecificActivityTypes);
				}
			}
			
			log.info("timeSheetActivityTypes size :::   "+timeSheetActivityTypes.size());
			List<JsonTimeSheetActivityType> jsonTimeSheetActivityType = new ArrayList<JsonTimeSheetActivityType>();
			if(timeSheetActivityTypes != null && !timeSheetActivityTypes.isEmpty()){
				for(TimeSheetActivityType timeSheetActivityType: timeSheetActivityTypes){
					jsonTimeSheetActivityType.add(new JsonTimeSheetActivityType(timeSheetActivityType));	
				}				
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTimeSheetActivityType, true);     
			jsonTimeSheetActivityType = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show Time sheet Activity Types!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }

	@RequestMapping(value="administration.workFlow.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions workFlowListByEntity(@RequestParam int entityType) {			
		JTableResponseOptions jTableResponseOptions =null;	
		
		try {
			List<WorkFlow> workFlowList=workPackageService.getworkFlowListByEntity(entityType);
			List<JsonWorkFlow> jsonWorkFlowList=new ArrayList<JsonWorkFlow>();
			for(WorkFlow workFlow: workFlowList){
				jsonWorkFlowList.add(new JsonWorkFlow(workFlow));	
			}				
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkFlowList,true);     
			workFlowList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show WorkFlows!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }

	@RequestMapping(value="administration.workpackage.List",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getWorkpackageByUser(HttpServletRequest request) {			
		JTableResponseOptions jTableResponseOptions =null;	
		
		try {
			List<WorkPackage> listOfUserAssociatedWorkPackages = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			
			listOfUserAssociatedWorkPackages = workPackageService.getUserAssociatedWorkPackages(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
			
			List<JsonWorkPackage> jsonWorkPackage=new ArrayList<JsonWorkPackage>();
			for(WorkPackage workPackage: listOfUserAssociatedWorkPackages){
				jsonWorkPackage.add(new JsonWorkPackage(workPackage));	
			}				
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkPackage);     
			listOfUserAssociatedWorkPackages = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show workpackage!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.parentskill.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listSkillsForParent(@RequestParam Integer skillIdfromUI) {
		JTableResponseOptions jTableResponseOptions;
		 final boolean flag = true;
		try {
			
			List<Skill> skillListFromDB = skillService.listSkill(1);
			List<JsonSkill> jsonSkillList = new ArrayList<JsonSkill>();
			for(Skill sk : skillListFromDB){
				if(skillIdfromUI != 0){
					if(sk.getSkillId() != skillIdfromUI){
						jsonSkillList.add(new JsonSkill(sk));//If DC already exist, then remove the self
					}
					
				}else{
					jsonSkillList.add(new JsonSkill(sk));//If 1st DC added, all into Parent DC list
				}
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonSkillList, flag);
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.skill.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listSkills(@RequestParam Integer skillIdfromUI) {		
		JTableResponseOptions jTableResponseOptions;
		 final boolean flag = true;
		try {
			
			List<Skill> skillListFromDB = skillService.listnoRoot(1);
			List<JsonSkill> jsonSkillList = new ArrayList<JsonSkill>();
			for(Skill sk : skillListFromDB){
				
				if(skillIdfromUI != 0){
					if(sk.getSkillId() != skillIdfromUI){
						if(sk.getSkillName().equalsIgnoreCase(IDPAConstants.SKILL_NA)){
							jsonSkillList.add(0,new JsonSkill(sk));
						}else{
							jsonSkillList.add(new JsonSkill(sk));//If DC already exist, then remove the self
						}
					}
				}else{
					jsonSkillList.add(new JsonSkill(sk));//If 1st DC added, all into Parent DC list
				}
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonSkillList, flag);
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }

	@RequestMapping(value="administration.environment.category.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getParentEnvironmentCategoryList(HttpServletRequest request,@RequestParam Integer environmentGroupId,@RequestParam Integer environmentCategoryId, @RequestParam Integer currentEnvironmentCategoryId) {			
		JTableResponseOptions jTableResponseOptions =null;	
		
		try {
			List<EnvironmentCategory> environmentCategoryList = new ArrayList<EnvironmentCategory>();
			List<JsonEnvironmentCategory> jsonEnvironmentCategoryList=new ArrayList<JsonEnvironmentCategory>();
			EnvironmentCategory evnCat = new EnvironmentCategory();
			if(currentEnvironmentCategoryId != 0){//Parent Drop down : At listing, if parent row, list only Null at Parent Drop down
				 evnCat = environmentService.getEnvironmentCategoryById(currentEnvironmentCategoryId);
				 if(evnCat != null && evnCat.getParentEnvironmentCategory() != null){//Parent Drop down : At listing, if child row, list all at Parent Drop down excluding itself
						environmentCategoryList=environmentService.getParentEnvironmentCategoryList(environmentGroupId,environmentCategoryId);
						for(EnvironmentCategory environmentCategory: environmentCategoryList){
							jsonEnvironmentCategoryList.add(new JsonEnvironmentCategory(environmentCategory));	
						}
					}else{
						
					}
			}else{//Parent Drop down : At Create, list all Environment Category at Parent Drop down
				environmentCategoryList=environmentService.getParentEnvironmentCategoryList(environmentGroupId,environmentCategoryId);
				for(EnvironmentCategory environmentCategory: environmentCategoryList){
					jsonEnvironmentCategoryList.add(new JsonEnvironmentCategory(environmentCategory));	
				}
			}					
			JsonEnvironmentCategory jec = new JsonEnvironmentCategory();
			jec.setEnvironmentCategoryId(0);
			jec.setEnvironmentCategoryName("Null");
			jsonEnvironmentCategoryList.add(jec);
			
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonEnvironmentCategoryList,true);     
			environmentCategoryList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to List Environment Category!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="common.list.workshift.list.testfactory",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWorkShiftsByTestFactoryId(int testFactoryId) {
		log.info("common.list.workshift.list.testfactory=====");
		JTableResponseOptions jTableResponseOptions;
		try {
		
			List<WorkShiftMaster> workShiftMasterList = testFactoryManagementService.listWorkShiftsByTestFactoryId(testFactoryId);
			List<JsonWorkShiftMaster> jsonWorkShiftMaster= new ArrayList<JsonWorkShiftMaster>();
			
			for(WorkShiftMaster workShiftMaster: workShiftMasterList){
				jsonWorkShiftMaster.add(new JsonWorkShiftMaster(workShiftMaster));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkShiftMaster);     
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.executionPriorityList",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getExecutionPriroityList(HttpServletRequest request) {			
		JTableResponseOptions jTableResponseOptions =null;	
		
		try {
			List<ExecutionPriority> executionPriorityList=workPackageService.getExecutionPriority();
			List<JsonExecutionPriority> jsonExecutionPriority=new ArrayList<JsonExecutionPriority>();
			for(ExecutionPriority executionPriority: executionPriorityList){
				jsonExecutionPriority.add(new JsonExecutionPriority(executionPriority));	
			}				
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonExecutionPriority);     
			jsonExecutionPriority = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show executionPriorityList!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }


	@RequestMapping(value="common.user.list.testmanagerorallusers",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listByUserRole(HttpServletRequest request, @RequestParam String approverList) {
		log.debug("administration.userskills.testManagerOrAllusers");
		JTableResponseOptions jTableResponseOptions = null;
			try {
				UserList sessionUser = (UserList)request.getSession().getAttribute("USER");
				Integer userRoleID = sessionUser.getUserRoleMaster().getUserRoleId();
				Integer userID = sessionUser.getUserId();
				List<UserList> userList=null;			
				
				if(approverList.equalsIgnoreCase("yes")){
					userList = workPackageService.userListByRole(String.valueOf(IDPAConstants.ROLE_ID_TEST_MANAGER));
				}else if (approverList.equalsIgnoreCase("ok")){
						userList = workPackageService.userListByRole("ResourceandTestManager");
				}else{
					userList = workPackageService.userListByRole(TAFConstants.ROLE_ALL);
				}
						
				List<JsonUserList> jsonUserList = new ArrayList<JsonUserList>();				
				for(UserList user: userList){
					if(userID != user.getUserId()){
						jsonUserList.add(new JsonUserList(user));
					}
					
				}				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList,true);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show Users!");
		            log.error("JSON ERROR listing Users", e);
		        }		        
	        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.user.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listUsers() {
		log.debug("inside common.user.list");
		JTableResponseOptions jTableResponseOptions = null;		 
		try {			
			List<UserList> userList=userListService.list();
			List<JsonUserList> jsonUserList=new ArrayList<JsonUserList>();
			for(UserList ul: userList){
				jsonUserList.add(new JsonUserList(ul));
				
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList,true);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.userskills.skillLevelsList",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listSkillLevelsByRole(HttpServletRequest request, @RequestParam String isApprover) {
		log.debug("administration.userskills.skillLevelsList");
		JTableResponseOptions jTableResponseOptions = null;
			try {

				UserList sessionUser = (UserList)request.getSession().getAttribute("USER");
				Integer userRoleID = sessionUser.getUserRoleMaster().getUserRoleId();
				
				List<SkillLevels> skillLevelsList= new ArrayList<SkillLevels>(); 
				if(isApprover.equals("yes")){
					skillLevelsList = skillService.listSkillLevels(2);
				}else{
					skillLevelsList = skillService.listSkillLevelsforUsers(2, userRoleID, isApprover);
				}	
				List<JsonSkillLevels> jsonSkillLevelsList = new ArrayList<JsonSkillLevels>();
				
				for(SkillLevels skillLevels: skillLevelsList){					
					jsonSkillLevelsList.add(new JsonSkillLevels(skillLevels));
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonSkillLevelsList,false);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show SkillLevels By Role!");
		            log.error("JSON ERROR listing SkillLevels By Role", e);
		        }		        
	        return jTableResponseOptions;
    }
	@RequestMapping(value="common.list.workshift.list.workpackage",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWorkShiftsUsingWorkpackageId(@RequestParam Integer workpackageId) {
		log.debug("inside common.list.workshift.list.workpackage");
		JTableResponseOptions jTableResponseOptions;
		List<JsonWorkShiftMaster> jsonWorkShiftMaster= new ArrayList<JsonWorkShiftMaster>();
		try {
			if(workpackageId!=null && workpackageId!=0){
				Integer	testFactoryId = workPackageService.getTestFactoryIdOfWorkPackage(workpackageId);			
				jsonWorkShiftMaster = testFactoryManagementService.listJsonWorkShiftsByTestFactoryId(testFactoryId);
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkShiftMaster); 
			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }

	
	@RequestMapping(value="common.list.workshift.list.workpackage.weekly.demand",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWeeklyWorkShiftsUsingWorkpackageId(@RequestParam Integer workpackageId) {
		log.debug("inside common.list.workshift.list.workpackage");
		JTableResponseOptions jTableResponseOptions;
		List<JsonWorkShiftMaster> jsonWorkShiftMaster= new ArrayList<JsonWorkShiftMaster>();
		try {
			if(workpackageId!=null && workpackageId!=0){
				Integer	testFactoryId = workPackageService.getTestFactoryIdOfWorkPackage(workpackageId);			
				jsonWorkShiftMaster = testFactoryManagementService.listJsonWorkShiftsByTestFactoryId(testFactoryId);
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkShiftMaster,true); 
			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.actualShift",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listActualShiftsUsingWorkpackageId(@RequestParam Integer workpackageId,@RequestParam String actualExecutionDate) {
		log.info("common.list.actualShift");
		JTableResponseOptions jTableResponseOptions;
		try {
			WorkPackage workpackage=workPackageService.getWorkPackageById(workpackageId);
			Integer testFactoryId=workpackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
			
			List<WorkShiftMaster> actualShiftList = workPackageService.listActualShiftByPlannedDateAndTestFactory(testFactoryId,actualExecutionDate);
			List<JsonWorkShiftMaster> jsonWorkShiftMaster= new ArrayList<JsonWorkShiftMaster>();
			
			for(WorkShiftMaster actualShift: actualShiftList){
				jsonWorkShiftMaster.add(new JsonWorkShiftMaster(actualShift));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkShiftMaster);     
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.workpackage.list.by.product",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getWorkpackageByProductId(@RequestParam int productId) {			
		JTableResponseOptions jTableResponseOptions =null;	
		log.info("administration.workpackage.list.by.product");
		try {
			List<WorkPackage> listOfWorkPackagesOfProduct = null;
			listOfWorkPackagesOfProduct = workPackageService.getWorkPackagesByProductId(productId);
			List<JsonWorkPackage> jsonWorkPackage = new ArrayList<JsonWorkPackage>();
			for(WorkPackage workPackage: listOfWorkPackagesOfProduct){
				jsonWorkPackage.add(new JsonWorkPackage(workPackage));	
			}				
			log.info("jsonWorkPackage--->"+jsonWorkPackage.size());
			jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkPackage);     
			listOfWorkPackagesOfProduct = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show workpackage!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }

	@RequestMapping(value="common.list.shifttypemaster",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listShiftTypeMaster() {
		log.info("common.list.shifttypemaster");
		JTableResponseOptions jTableResponseOptions;
		try {
			
			List<ShiftTypeMaster> shiftTypeMasterList = resourceManagementService.listShiftTypeMaster();
			List<JsonShiftTypeMaster> jsonShiftTypeMaster= new ArrayList<JsonShiftTypeMaster>();
			
			for(ShiftTypeMaster shiftTypeMaster: shiftTypeMasterList){
				jsonShiftTypeMaster.add(new JsonShiftTypeMaster(shiftTypeMaster));
			}
			log.info("jsonShiftTypeMaster--->"+jsonShiftTypeMaster.size());
			jTableResponseOptions = new JTableResponseOptions("OK", jsonShiftTypeMaster);     
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.workshift.user",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWorkShiftsByUser(HttpServletRequest req) {
		log.info("common.list.workshift.list.testfactory=====");
		JTableResponseOptions jTableResponseOptions;
		try {
			UserList user=(UserList)req.getSession().getAttribute("USER");

			Set<TestFactory> testFactoryList= user.getResourcePool().getTestFactoryList();
			List<WorkShiftMaster> workShiftMasterList =null;
			List<WorkShiftMaster> workShiftMasterListFull =new ArrayList<WorkShiftMaster>();

			for(TestFactory testFactory:testFactoryList){
				workShiftMasterList=testFactoryManagementService.listWorkShiftsByTestFactoryId(testFactory.getTestFactoryId());
				for(WorkShiftMaster workShiftMaster:workShiftMasterList){
					workShiftMasterListFull.add(workShiftMaster);	
				}
				
			}
			
			
			List<JsonWorkShiftMaster> jsonWorkShiftMaster= new ArrayList<JsonWorkShiftMaster>();
			
			for(WorkShiftMaster workShiftMaster: workShiftMasterListFull){
				jsonWorkShiftMaster.add(new JsonWorkShiftMaster(workShiftMaster));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkShiftMaster);     
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.environment.group.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getEnvironmentGroupList(HttpServletRequest request) {			
		JTableResponseOptions jTableResponseOptions =null;	
		
		try {
			List<EnvironmentGroup> environmentGroupList=environmentService.getEnvironmentGroupList();
			List<JsonEnvironmentGroup> jsonEnvironmentGroupList=new ArrayList<JsonEnvironmentGroup>();
			for(EnvironmentGroup environmentGroup: environmentGroupList){
				jsonEnvironmentGroupList.add(new JsonEnvironmentGroup(environmentGroup));	
			}				
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonEnvironmentGroupList,true);     
			environmentGroupList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show WorkFlows!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.environment.group.category.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getEnvironmentCategoryListByGroup(HttpServletRequest request,@RequestParam Integer environmentGroupId) {			
		JTableResponseOptions jTableResponseOptions =null;	
		
		try {
			List<EnvironmentCategory> environmentCategoryList=environmentService.getEnvironmentCategoryListByGroup(environmentGroupId);
			List<JsonEnvironmentCategory> jsonEnvironmentCategoryList=new ArrayList<JsonEnvironmentCategory>();
			for(EnvironmentCategory environmentCategory: environmentCategoryList){
				jsonEnvironmentCategoryList.add(new JsonEnvironmentCategory(environmentCategory));	
			}				
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonEnvironmentCategoryList,true);     
			environmentCategoryList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show WorkFlows!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="common.list.product.workpackages.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWorkpackgesbyProductId(@RequestParam int productId) {
		log.info("common.list.product.workpackages.list=====");
		JTableResponseOptions jTableResponseOptions;
		List<WorkPackage> workPackagesOfProduct = null;
			try {	
				workPackagesOfProduct = workPackageService.getWorkPackagesByProductId(productId);
				List<JsonWorkPackage> jsonWorkPackage = new ArrayList<JsonWorkPackage>();
				
				for(WorkPackage workPackage: workPackagesOfProduct){
					jsonWorkPackage.add(new JsonWorkPackage(workPackage));
				}			
				if(jsonWorkPackage == null || jsonWorkPackage.size() == 0){
					JsonWorkPackage jsonWorkPackageDefaultValue = new JsonWorkPackage();
					jsonWorkPackageDefaultValue.setId(0);
					jsonWorkPackageDefaultValue.setName(" - ");
					jsonWorkPackage.add( jsonWorkPackageDefaultValue);
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkPackage, true);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
	        return jTableResponseOptions;
    }
	@RequestMapping(value="common.list.resourcecheckin.product.activeworkpackages",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWorkpackgesbyResourceCheckInProductId(@RequestParam int resourceCheckInId) {
		log.info("common.list.product.workpackages.list=====");
		JTableResponseOptions jTableResponseOptions;
		List<WorkPackage> workPackagesOfProduct = null;
		
		List<ProductMaster> resourceCheckInProducts = null;
			try {	
				resourceCheckInProducts = timeSheetManagementService.getProductByResourceShiftCheckInId(resourceCheckInId);	
				ResourceShiftCheckIn resourceShiftCheck = timeSheetManagementService.getByresourceShiftCheckInId(resourceCheckInId);
				int userId = resourceShiftCheck.getUserList().getUserId();
				int shiftId = resourceShiftCheck.getActualShift().getShift().getShiftId();
				
				List<WorkPackage> listOfWorkPackagesOfProduct = new ArrayList<WorkPackage>();
				listOfWorkPackagesOfProduct = resourceManagementService.getWorkPackageFromTestFactoryResourceReservationByserId(userId, shiftId, resourceShiftCheck.getCreatedDate());
				if(listOfWorkPackagesOfProduct != null && listOfWorkPackagesOfProduct.size()>0){
					log.info("Workpackage for which user reserved : "+listOfWorkPackagesOfProduct);	
				}				
				List<ProductMaster> productMasterList = null;
				List<ProductMaster> listOfUniqueProducts= new ArrayList<ProductMaster>();
				List<WorkPackage> listOfUniqueWorkPackages = new ArrayList<WorkPackage>();
				WorkFlow workFlow=null;
								if(listOfWorkPackagesOfProduct != null && listOfWorkPackagesOfProduct.size()>0){
									for(WorkPackage workPackage:listOfWorkPackagesOfProduct){
										if(workPackage.getIsActive().equals(1)){
											if(workPackage.getWorkFlowEvent()!=null){
											workFlow=workPackage.getWorkFlowEvent().getWorkFlow();
											if(workFlow!=null){
												if(workFlow.getEntityMaster()!=null){
												if(workFlow.getEntityMaster().getEntitymasterid()==IDPAConstants.WORKPACKAGE_ENTITY_ID){
													int stageId=workFlow.getStageId();
													if((stageId==IDPAConstants.WORKFLOW_STAGE_ID_NEW) ||(stageId==IDPAConstants.WORKFLOW_STAGE_ID_PLANNING) ||(stageId==IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION) ){
														if(!listOfUniqueWorkPackages.contains(workPackage)){
															listOfUniqueWorkPackages.add(workPackage);
														}
														if(!listOfUniqueProducts.contains(workPackage.getProductBuild().getProductVersion().getProductMaster())){
															listOfUniqueProducts.add(workPackage.getProductBuild().getProductVersion().getProductMaster());
														}
													}
												}
											}
											}
										}
										}
									}
								}
								listOfWorkPackagesOfProduct=null;
				List<JsonWorkPackage> jsonWorkPackage = new ArrayList<JsonWorkPackage>();
				
				for(WorkPackage workPackage: listOfUniqueWorkPackages){
					jsonWorkPackage.add(new JsonWorkPackage(workPackage));
				}			
				if(jsonWorkPackage == null || jsonWorkPackage.size() == 0){
					JsonWorkPackage jsonWorkPackageDefaultValue = new JsonWorkPackage();
					jsonWorkPackageDefaultValue.setId(0);
					jsonWorkPackageDefaultValue.setName(" - ");
					jsonWorkPackage.add( jsonWorkPackageDefaultValue);
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkPackage, true);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching listWorkpackges!");
		            log.error("JSON ERROR listing Workpackges by ResourceCheck In ProductId", e);
		        }
	        return jTableResponseOptions;
    }
	@RequestMapping(value="common.workpackage.list.by.testFactory",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWorkpackagebyProductIdandTestFactoryId(@RequestParam int testFactoryId) {
		log.debug("common.workpackage.list.by.testFactory");
		JTableResponseOptions jTableResponseOptions=null;
		 
		try {
			List<JsonWorkPackage> jsonWorkPackage = new ArrayList<JsonWorkPackage>();			
			List<WorkPackage> listOfWorkPackagesOfProduct = null;
			List<ProductMaster> productMasterList = null;
			productMasterList=productListService.listProductsByTestFactoryId(testFactoryId);
			if(productMasterList != null && productMasterList.size()>0){
				for(ProductMaster pm: productMasterList){
					log.info("pm.getProductId()--->"+pm.getProductId());
					listOfWorkPackagesOfProduct = workPackageService.getWorkPackagesByProductId(pm.getProductId());
					if(listOfWorkPackagesOfProduct != null && listOfWorkPackagesOfProduct.size()>0){
						for(WorkPackage workPackage:listOfWorkPackagesOfProduct){
							jsonWorkPackage.add(new JsonWorkPackage(workPackage));
						}
					}
					listOfWorkPackagesOfProduct=null;
				}
			}
			log.info("jsonWorkPackage--->"+jsonWorkPackage.size());
			jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkPackage);     
			listOfWorkPackagesOfProduct = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show workpackage!");
	            log.error("JSON ERROR", e);
	        }		        
			return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.workpackage.environment.mappedlist",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listMappedEnvironments(@RequestParam Integer workPackageId) {
		log.debug("inside administration.workpackage.environment.mappedlist");
		JTableResponseOptions jTableResponseOptions = null;
			try {
				Set<Environment> environmentList=null;//workPackageService.getEnvironmentMappedToWorkpackage(workPackageId);
				log.info("environmentList"+environmentList.size());
				
			     List<JsonEnvironment> jsonEnvironment = new ArrayList<JsonEnvironment>();
			     Environment envFromDB=null;
				for(Environment environment: environmentList){
					
					log.info("environment.getEnvironmentId()"+environment.getEnvironmentId());
					jsonEnvironment.add(new JsonEnvironment(environment));
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonEnvironment);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponseOptions;
    } 
	
	@RequestMapping(value="list.roles.for.demand.projection",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listRolesforDemandProjection() {
		log.debug("inside administration.user.roleList");
		JTableResponseOptions jTableResponseOptions = null;
		final boolean flag = true;
			try {
				List<UserRoleMaster> userRoleMaster = workPackageService.getUserRolesForDemandProjection();
				List<JsonUserRoleMaster> jsonProductUserRole = new ArrayList<JsonUserRoleMaster>();
				for(UserRoleMaster userRole: userRoleMaster){
					jsonProductUserRole.add(new JsonUserRoleMaster(userRole));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonProductUserRole,true);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
	        return jTableResponseOptions;
    }
	@RequestMapping(value="common.administration.user.testManagerUserRoleList",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestMangerUserRole(@RequestParam int rescheckinId,@RequestParam int userId) {
		log.debug("inside common.administration.user.testManagerUserRoleList");
		JTableResponseOptions jTableResponseOptions = null;
		List<ProductMaster> productList =null;
		int productId =0;
		final boolean flag = true;
			try {
				productList = timeSheetManagementService.getProductByResourceShiftCheckInId(rescheckinId);
				if(!productList.isEmpty()){
					productId = productList.get(0).getProductId();	
				}
				
				log.info("productId-->"+productId+"+userId-->"+userId);
		ProductUserRole productUserRole=	productListService.getProductUserRoleByUserIdAndProductId(productId,userId);
		
				List<UserRoleMaster> userRoleMaster = productListService.getRolesBasedResource();
				
				List<JsonUserRoleMaster> jsonUserRole = new ArrayList<JsonUserRoleMaster>();
				if(productUserRole!=null){
					for(UserRoleMaster userRole: userRoleMaster){
						
					if(productUserRole.getRole().getUserRoleId()==userRole.getUserRoleId()){
						jsonUserRole.add(0,new JsonUserRoleMaster(userRole));
					
					}else{
						jsonUserRole.add(new JsonUserRoleMaster(userRole));
					}
						
					}
				}else{
					for(UserRoleMaster userRole: userRoleMaster){
						jsonUserRole.add(new JsonUserRoleMaster(userRole));
					}
					}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonUserRole,true);
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.performance.rating",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listPerformanceLevelRating() {
		log.info("inside common.list.performance.rating");
	
		JTableResponseOptions jTableResponseOptions = null;
			try {
				List<PerformanceLevel> performanceLevel = workPackageService.getPerformanceRating();
				List<JsonPerformanceLevel> jsonPerformanceLevel = new ArrayList<JsonPerformanceLevel>();
				for(PerformanceLevel performlevel: performanceLevel){
					jsonPerformanceLevel.add(new JsonPerformanceLevel(performlevel));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonPerformanceLevel);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
	        return jTableResponseOptions;
	}
	@RequestMapping(value="common.list.workshift.list.testfactoryWithShift",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWorkShiftsWithShiftByTestFactoryId(HttpServletRequest request,@RequestParam Date  workDate,@RequestParam Integer userId) {
		log.info("common.list.workshift.list.testfactory====="+workDate+"userId"+userId);
		JTableResponseOptions jTableResponseOptions=null;
		List<JsonActualShift>  jsonActualShiftList=new ArrayList<JsonActualShift>();
		try {
			List<ActualShift> actualShiftList=	testFactoryManagementService.listActualShift(-1, workDate);
			if(actualShiftList.size()>0){
				for(ActualShift actualShift:actualShiftList){
					Integer shiftId=actualShift.getShift().getShiftId();
					
					List<TestFactoryResourceReservation> testFactoryResReservationList=resourceManagementService.getTestFactoryResourceReservationByDateShiftAndUser(userId,shiftId,workDate);
					if(testFactoryResReservationList.size()>0){
						jsonActualShiftList.add(new JsonActualShift(actualShift));
					}
					
				}
			if(jsonActualShiftList==null || jsonActualShiftList.size()==0){
				jTableResponseOptions = new JTableResponseOptions("ERROR","There are no reserved shifts running for the selected date."
						+ " . Please contact Test Manager! ");
					return jTableResponseOptions;
			}else{	
			jTableResponseOptions = new JTableResponseOptions("OK", jsonActualShiftList,true);   
			}
			}else{
			
				jTableResponseOptions = new JTableResponseOptions("ERROR","There are no shifts running for the selected date."
						+ " . Please contact Test Manager! ");
					return jTableResponseOptions;
			}
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.products.list.byTestFactory",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listProductsWithActiveWpByTestFactoryId(HttpServletRequest request,int actualShiftId,Date date) {
		log.info("common.products.list.byTestFactory=====");
		JTableResponseOptions jTableResponseOptions=null;
		int testFactoryId;
		int shiftId;
		Integer shiftTypeId;
		List<JsonProductMaster> jsonProductMasterList=new ArrayList<JsonProductMaster>();
		UserList user = (UserList) request.getSession().getAttribute("USER");
		try {
			List<WorkPackage> listOfWorkPackagesOfProduct = null;
			List<ProductMaster> productMasterList = null;
			List<ProductMaster> listOfUniqueProducts= new ArrayList<ProductMaster>();
			List<WorkPackage> listOfUniqueWorkPackages = new ArrayList<WorkPackage>();
			List<WorkPackage> listOfUniqueWorkPackagesFrom = new ArrayList<WorkPackage>();
			if(actualShiftId!=-1){
			ActualShift actualShift=timeSheetManagementService.listActualShiftbyActualShiftId(actualShiftId);
			testFactoryId=actualShift.getShift().getTestFactory().getTestFactoryId();
			shiftId=actualShift.getShift().getShiftId();
			WorkFlow workFlow=null;
			List<TestFactoryResourceReservation> testFactoryResReservationList=resourceManagementService.getTestFactoryResourceReservationByDateShiftAndUser(user.getUserId(),shiftId,date);
			productMasterList=productListService.listProductsByTestFactoryId(testFactoryId);
			if(productMasterList != null && productMasterList.size()>0){
				for(ProductMaster pm: productMasterList){
					listOfWorkPackagesOfProduct = workPackageService.getWorkPackagesByProductId(pm.getProductId());
					if(listOfWorkPackagesOfProduct != null && listOfWorkPackagesOfProduct.size()>0){
						for(WorkPackage workPackage:listOfWorkPackagesOfProduct){
							if(workPackage.getIsActive()==1){
								if(workPackage.getWorkFlowEvent()!=null){
								workFlow=workPackage.getWorkFlowEvent().getWorkFlow();
								if(workFlow!=null){
									if(workFlow.getEntityMaster()!=null){
									if(workFlow.getEntityMaster().getEntitymasterid()==IDPAConstants.WORKPACKAGE_ENTITY_ID){
										int stageId=workFlow.getStageId();
										if((stageId==IDPAConstants.WORKFLOW_STAGE_ID_NEW) ||(stageId==IDPAConstants.WORKFLOW_STAGE_ID_PLANNING) ||(stageId==IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION) ){
											if(!listOfUniqueWorkPackages.contains(workPackage)){
												listOfUniqueWorkPackages.add(workPackage);
											}
										}
									}
								}
								}
							}
							}
						}
					}
					listOfWorkPackagesOfProduct=null;
				}
			}
			
				if(testFactoryResReservationList!=null && testFactoryResReservationList.size()>0){
					for(TestFactoryResourceReservation testFactResReservation :testFactoryResReservationList){
		if(listOfUniqueWorkPackages!=null && listOfUniqueWorkPackages.size()>0){
			for(WorkPackage workPackage:listOfUniqueWorkPackages){
				if(workPackage.getWorkPackageId().equals(testFactResReservation.getWorkPackage().getWorkPackageId())){
					if(!listOfUniqueWorkPackagesFrom.contains(workPackage)){
						listOfUniqueWorkPackagesFrom.add(workPackage);
						ProductMaster productMaster=workPackageService.getProductMasterByWorkpackageId(workPackage.getWorkPackageId());
						if(!listOfUniqueProducts.contains(productMaster)){
							listOfUniqueProducts.add(productMaster);
							jsonProductMasterList.add(new JsonProductMaster(productMaster));
						}
					}
					
				}
			}
			}
		}
		}
		listOfUniqueWorkPackagesFrom=null;
			if(jsonProductMasterList!=null && jsonProductMasterList.size()>0){
				jTableResponseOptions = new JTableResponseOptions("OK", jsonProductMasterList);
			}else{
				jTableResponseOptions = new JTableResponseOptions("ERROR","No product execution is planned for this shift. Please contact Test Manager! ");
				return jTableResponseOptions;
			}
		
			jsonProductMasterList=null;
			listOfUniqueProducts=null;
			}
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.user.associated.products",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions loadProductsBasedOnUserId(HttpServletRequest request) {
		log.debug("inside common.list.user.associated.products");
		JTableResponseOptions jTableResponseOptions=null;
		 
		try {
			UserList user = (UserList)request.getSession().getAttribute("USER");
			List<ProductMaster> productMaster = dataTreeService.getUserAssociatedProducts(user.getUserRoleMaster().getUserRoleId(),user.getUserId(),1);
			List<JsonProductMaster> jsonProductMaster=new ArrayList<JsonProductMaster>();
			if (productMaster != null){
				for(ProductMaster dvm: productMaster){
					jsonProductMaster.add(new JsonProductMaster(dvm));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonProductMaster);	
			}
			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.user.associated.product.versions",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions loadProductVersionsBasedOnUserId(HttpServletRequest request, @RequestParam int productId) {
		log.debug("inside common.list.user.associated.product.versions");
		JTableResponseOptions jTableResponseOptions=null;
		try {
				UserList user = (UserList)request.getSession().getAttribute("USER");
				List<ProductVersionListMaster> productVersionListMaster = new ArrayList<ProductVersionListMaster>();
				if(productId == -1){
					List<ProductMaster> productMaster = dataTreeService.getUserAssociatedProducts(user.getUserRoleMaster().getUserRoleId(),user.getUserId(),1);
					for (ProductMaster productMaster2 : productMaster) {
						List<ProductVersionListMaster> subProductVersionListMaster = productListService.productVersionsList(productMaster2.getProductId());
						if(productVersionListMaster.size() == 0){
							productVersionListMaster = subProductVersionListMaster;
						}else{
							productVersionListMaster.addAll(subProductVersionListMaster);
						}
					}
				}else{
					productVersionListMaster = productListService.productVersionsList(productId);
				}
				List<JsonProductVersionListMaster> jsonProductVersionListMaster = new ArrayList<JsonProductVersionListMaster>();
				if(productVersionListMaster != null && productVersionListMaster.size()>0){
					for(ProductVersionListMaster pvlm : productVersionListMaster){
						jsonProductVersionListMaster.add(new JsonProductVersionListMaster(pvlm));
					}
					jTableResponseOptions = new JTableResponseOptions("OK", jsonProductVersionListMaster);
				}
				productVersionListMaster = null;  
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.languages",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listLanguages(HttpServletRequest request) {
		log.debug("inside common.list.languages");
		JTableResponseOptions jTableResponseOptions=null;
		 
		try {			
			List<Languages> languagesFromDB = userListService.listLanguages(1);
			List<JsonLanguage>  jsonLanguage = new ArrayList<JsonLanguage>();
			if(languagesFromDB != null){
				for(Languages lang: languagesFromDB){
					jsonLanguage.add(new JsonLanguage(lang));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonLanguage);
			}		
			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching Languages!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.defectSeverity",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDefectSeverity(HttpServletRequest request) {
		log.debug("inside common.list.defectSeverity");
		JTableResponseOptions jTableResponseOptions=null;		 
		try {			
			List<DefectSeverity> defectSeverityFromDB = testExecutionBugsService.listDefectSeverity();
			List<JsonDefectSeverity> jsonDefectSeverityList = new ArrayList<JsonDefectSeverity>();
			if(defectSeverityFromDB != null){
				for (DefectSeverity defectSeverity : defectSeverityFromDB) {
					jsonDefectSeverityList.add(new JsonDefectSeverity(defectSeverity));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonDefectSeverityList);
			}				
			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching DefectSeverity!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="show.palm.build.details",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody String getPalmBuildDetails(HttpServletRequest request) {
		log.debug("inside show.palm.build.details");
		JSONObject jsonObj= new JSONObject();	
		try {			
			List<String> results = commonService.getPalmBuildDetails(request);
			for (String values : results) {
				String[] splitValue = values.split("=");
				String key = splitValue[0];
				String value = splitValue[1];
				jsonObj.put(key, value);	
			}
	        } catch (Exception e) {
	            log.error("JSON ERROR", e);
	        }
        return jsonObj.toString();
    }

	@RequestMapping(value="common.list.executiontypemaster",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listExecutionType(HttpServletRequest request) {
		log.debug("common.list.executiontypemaster");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<ExecutionTypeMaster> executionTypeMasterFromDB = executionTypeMasterService.listExecutionType();
			List<JsonExecutionTypeMaster> jsonExecutionTypeMaster = new ArrayList<JsonExecutionTypeMaster>();
			
			for(ExecutionTypeMaster etm :executionTypeMasterFromDB){
				jsonExecutionTypeMaster.add(new JsonExecutionTypeMaster(etm));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonExecutionTypeMaster);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error Execution Type records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.executiontypemaster.byentityid",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listExecutionTypeByEntityId(HttpServletRequest request, @RequestParam int entitymasterid) {
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<ExecutionTypeMaster> executionTypeMasterFromDB = executionTypeMasterService.listbyEntityMasterId(entitymasterid);
			List<JsonExecutionTypeMaster> jsonExecutionTypeMaster = new ArrayList<JsonExecutionTypeMaster>();
			
			for(ExecutionTypeMaster etm :executionTypeMasterFromDB){
				jsonExecutionTypeMaster.add(new JsonExecutionTypeMaster(etm));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonExecutionTypeMaster, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error Execution Type records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.testcasepriority",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestcasepriority(HttpServletRequest request) {
//		log.info("common.list.testcasepriority");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<TestCasePriority> testCasePriorityFromDB = testCaseService.listTestCasePriority();
		List<JsonTestCasePriority> jsonTestCasePriority = new ArrayList<JsonTestCasePriority>();
		for(TestCasePriority tcp: testCasePriorityFromDB){		
			jsonTestCasePriority.add(new JsonTestCasePriority(tcp));
		}			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestCasePriority, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining TestCasePriority records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.testcasetype",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestcasetype(HttpServletRequest request) {
//		log.info("common.list.testcasepriority");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<TestcaseTypeMaster> testcaseTypeMasterFromDB = testCaseService.listTestcaseTypeMaster();
			List<JsonTestcaseTypeMaster> jsonTestcaseTypeMaster = new ArrayList<JsonTestcaseTypeMaster>();
		
			for(TestcaseTypeMaster ttm : testcaseTypeMasterFromDB){
				jsonTestcaseTypeMaster.add(new JsonTestcaseTypeMaster(ttm));
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestcaseTypeMaster, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining TestcaseType  records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.resourcePool.list.by.testFactoryLabId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listResourcePoolsByTestFactoryByLabId(@RequestParam int testFactoryLabId) {			
		JTableResponseOptions jTableResponseOptions;			 
		try {
			List<TestfactoryResourcePool> tfResPoolList= testFactoryManagementService.listResourcePoolByTestFactoryLabId(testFactoryLabId);
			List<JsonResourcePool> jsontfResPoolList = new ArrayList<JsonResourcePool>();
			for(TestfactoryResourcePool tfResourcePool: tfResPoolList){
				jsontfResPoolList.add(new JsonResourcePool(tfResourcePool));	
			}	
			if(jsontfResPoolList.size() > 1){
				TestfactoryResourcePool tfAllResourcePool = new TestfactoryResourcePool();
				tfAllResourcePool.setResourcePoolId(-1);
				tfAllResourcePool.setResourcePoolName("All");
				jsontfResPoolList.add(new JsonResourcePool(tfAllResourcePool));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsontfResPoolList,true);     
			jsontfResPoolList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }

	@RequestMapping(value="common.list.productType",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listProductType(HttpServletRequest request) {
		log.info("common.list.productType");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
List<ProductType> productType = productListService.listProductTyper();
			List<JsonProductType> jsonProductType = new ArrayList<JsonProductType>();
		
			for(ProductType pt : productType){
				jsonProductType.add(new JsonProductType(pt));
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductType, false);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining ProductType  records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.deviceTypes",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDeviceType(HttpServletRequest request) {
		log.info("common.list.deviceTypes");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<DeviceType> deviceTypeList = productListService.listDeviceType();
			List<JsonDeviceType> jsonDeviceTypeList = new ArrayList<JsonDeviceType>();
		
			for(DeviceType deviceType : deviceTypeList){
				jsonDeviceTypeList.add(new JsonDeviceType(deviceType));
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDeviceTypeList, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining ProductType  records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	@RequestMapping(value="common.deviceLabId.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDeviceLabs(HttpServletRequest request) {
		log.info("common.list.deviceTypes");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<DeviceLab> deviceLabList = productListService.listDeviceLab();
			List<JsonDeviceLab> jsonDeviceLabList = new ArrayList<JsonDeviceLab>();
		
			for(DeviceLab deviceLab : deviceLabList){
				jsonDeviceLabList.add(new JsonDeviceLab(deviceLab));
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDeviceLabList, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining ProductType  records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	@RequestMapping(value="common.deviceModelMaster.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDeviceModelMaster(HttpServletRequest request) {
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<DeviceModelMaster> deviceModelMasterList = deviceListService.modelList();
			List<JsonDeviceModelMaster> jsonDeviceModelMasterList = new ArrayList<JsonDeviceModelMaster>();
		
			for(DeviceModelMaster deviceModelMaster : deviceModelMasterList){
				jsonDeviceModelMasterList.add(new JsonDeviceModelMaster(deviceModelMaster));
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDeviceModelMasterList, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining ProductType  records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
		
	@RequestMapping(value="common.host.list.deviceLab",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listHostByDeviceLabId(@RequestParam int deviceLabId,@RequestParam int filter) {			
		log.debug("Listing Host By DeviceLabId");
		JTableResponseOptions jTableResponseOptions;
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
			jTableResponseOptions = new JTableResponseOptions("OK", jsonHostLists, true);
			
			jsonHostList = null;
		        return jTableResponseOptions;
							
			} catch (Exception e) {
	            jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining HostList by DeviceLabId!");
	            log.error("JSON ERROR obtaining HostList", e);
	        }		        
        return jTableResponseOptions;
    }
	@RequestMapping(value="common.platformtype.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listPlatFormType(HttpServletRequest request) {
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<PlatformType> platformTypeList = deviceListService.listPlatformType();
			List<JsonPlatformType> jsonPlatformTypeList = new ArrayList<JsonPlatformType>();
		
			for(PlatformType platformType : platformTypeList){
				jsonPlatformTypeList.add(new JsonPlatformType(platformType));
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", jsonPlatformTypeList, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining PlatformType  records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }

	@RequestMapping(value="common.platformtype.listwithversion",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listPlatFormTypeWithVersion(HttpServletRequest request) {
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<PlatformType> platformTypeList = deviceListService.listPlatformType();
			List<JsonPlatformType> jsonPlatformTypeList = new ArrayList<JsonPlatformType>();
		
			for(PlatformType platformType : platformTypeList){
				jsonPlatformTypeList.add(new JsonPlatformType(platformType));
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", jsonPlatformTypeList, false);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining PlatformType  records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.deviceMakeMaster.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDeviceMakeMaster(HttpServletRequest request) {
		log.info("common.deviceMakeMaster.list");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<DeviceMakeMaster> deviceMakeMasterList = deviceListService.makeList();			
			List<JsonDeviceMakeMaster> jsonDeviceMakeMasterList = new ArrayList<JsonDeviceMakeMaster>();			
				
			for (DeviceMakeMaster deviceMakeMaster : deviceMakeMasterList) {
				jsonDeviceMakeMasterList.add(new JsonDeviceMakeMaster(deviceMakeMaster));
			}

			jTableResponseOptions = new JTableResponseOptions("OK", jsonDeviceMakeMasterList, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining DeviceMake records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.systemType.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listSystemType(HttpServletRequest request) {
		log.debug("Listing SystemType");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<SystemType> systemTypeList = deviceListService.listSystemType();
			List<JsonSystemType> jsonSystemType = new ArrayList<JsonSystemType>();
			for (SystemType systemType : systemTypeList) {
				jsonSystemType.add(new JsonSystemType(systemType));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonSystemType, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error Listing SystemType records!");
	            log.error("JSON ERROR Listing SystemType", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.systemprocessor.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listSystemProcessor(HttpServletRequest request) {
		log.debug("Listing SystemProcessor");
		JTableResponseOptions jTableResponseOptions;		 
		try {			
			List<Processor> processorList = deviceListService.listProcessor();			
			List<JsonProcessor> jsonProcessorList = new ArrayList<JsonProcessor>();
			if(processorList != null && processorList.size() >0){
				for (Processor processor : processorList) {
					jsonProcessorList.add(new JsonProcessor(processor));
				}
			}						
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProcessorList, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error Listing Processor records!");
	            log.error("JSON ERROR Listing Processor", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.listProductBuild.byProductVersionid.option",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listProductBuildByProductversionId(HttpServletRequest request,@RequestParam Integer productVersionListId ) {
		log.info("Listing ProductBuild ByProductversionId");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<ProductBuild> productBuildList = productListService.listProductBuild(productVersionListId);
			List<JsonProductBuild> jsonProductBuildList = new ArrayList<JsonProductBuild>();
			for(ProductBuild productBuild : productBuildList){
				jsonProductBuildList.add(new JsonProductBuild(productBuild));
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductBuildList, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining ProductBuild  records!");
	            log.error("JSON ERROR obtaining ProductBuild", e);
	        }	        
        return jTableResponseOptions;
    }

	@RequestMapping(value="common.listuser.roles",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listRolesByUser(HttpServletRequest request,@RequestParam Integer userid ) {
		log.info("common.listuser.roles");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<UserRoleMaster> userRoleMasters = userListService.getRolesByUser(userid);
			
			
			List<JsonUserRoleMaster> jsonUserRoleMasters = new ArrayList<JsonUserRoleMaster>();
			for(UserRoleMaster userRoleMaster : userRoleMasters){
				jsonUserRoleMasters.add(new JsonUserRoleMaster(userRoleMaster));
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", jsonUserRoleMasters, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining User Roles  records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="common.list.engagementtypes",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listEngagementTypes(HttpServletRequest request, int type) {
		log.debug("common.list.engagementtypes");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<EngagementTypeMaster> engagementTypes = testFactoryManagementService.listEngagementTypes();
			List<JsonEngagementTypeMaster> jsonEngagementTypeMaster = new ArrayList<JsonEngagementTypeMaster>();
		if(type==1){
				
					jsonEngagementTypeMaster.add(new JsonEngagementTypeMaster(engagementTypes.get(0)));
					jTableResponseOptions = new JTableResponseOptions("OK", jsonEngagementTypeMaster,true);
					 return jTableResponseOptions;
				}
		else if(type==2){
				jsonEngagementTypeMaster.add(new JsonEngagementTypeMaster(engagementTypes.get(1)));
				jTableResponseOptions = new JTableResponseOptions("OK", jsonEngagementTypeMaster,true);
				 return jTableResponseOptions;}
		else{
			for(EngagementTypeMaster engmtType :engagementTypes){
				jsonEngagementTypeMaster.add(new JsonEngagementTypeMaster(engmtType));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonEngagementTypeMaster,true);	
	        }
		}catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error listing Engagement Type records!");
	            log.error("JSON ERROR listing Engagement Type records", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.testToolMaster.option",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestToolMaster(HttpServletRequest request) {
		log.debug("common.list.testToolMaster.option");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<TestToolMaster> testToolMasterList = testRunConfigSer.listTestToolMaster();
			List<JsonTestToolMaster> jsonTestToolMasterList = new ArrayList<JsonTestToolMaster>();
			
			for(TestToolMaster testToolMaster :testToolMasterList){
				jsonTestToolMasterList.add(new JsonTestToolMaster(testToolMaster));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestToolMasterList,true);	
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error in loading or listing Engagement Type records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	

	
	@RequestMapping(value="administration.mode.list.option",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listModes(@RequestParam int testFactoryId) {			
			
		log.debug("inside administration.mode.list.option");
		JTableResponseOptions jTableResponseOptions = null;
		int engagementTypeId=testFactoryManagementService.getEngagementTypeIdBytestfactoryId(testFactoryId);
		try {
			List<ProductMode> productModeList = testFactoryManagementService.getmodelist();
			List<JsonProductMode> jsonProductModeList = new ArrayList<JsonProductMode>();
				if(engagementTypeId==1)
				{
				jsonProductModeList.add(new JsonProductMode(productModeList.get(0)));
				
				}else{
				
				jsonProductModeList.add(0,new JsonProductMode(productModeList.get(1)));
				
				}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductModeList,true);     
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        	return jTableResponseOptions;
			}
	
	@RequestMapping(value="common.list.user.authentication.types",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listUserAuthenticationType(HttpServletRequest request) {
		log.debug("Listing UserAuthenticationType");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<AuthenticationType> authenticationTypes = userListService.listAuthenticationTypes();
			List<JsonAuthenticationType> jsonAuthenticationTypes = new ArrayList<JsonAuthenticationType>();
			for(AuthenticationType authenticationType : authenticationTypes){
				jsonAuthenticationTypes.add(new JsonAuthenticationType(authenticationType));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonAuthenticationTypes, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining User Authentication Types!");
	            log.error("JSON ERROR obtaining User Authentication Types", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.user.authentication.modes",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listUserAuthenticationModes(HttpServletRequest request) {
		log.debug("Listing UserAuthentication Modes");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<AuthenticationMode> authenticationModes = userListService.listAuthenticationModes();
			List<JsonAuthenticationMode> jsonAuthenticationModes = new ArrayList<JsonAuthenticationMode>();
			for(AuthenticationMode authenticationMode : authenticationModes){
				jsonAuthenticationModes.add(new JsonAuthenticationMode(authenticationMode));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonAuthenticationModes, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining User Authentication Modes!");
	            log.error("JSON ERROR obtaining User Authentication Modes", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.defect.types.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDefectTypesList(HttpServletRequest request) {
		log.debug("Listing DefectTypes List");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<DefectTypeMaster> defectTypes = testExecutionBugsService.listDefectTypes();
			List<JsonDefectType> jsonDefectTypes = new ArrayList<JsonDefectType>();
			for(DefectTypeMaster defectType : defectTypes){
				jsonDefectTypes.add(new JsonDefectType(defectType));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDefectTypes, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining Defect Types!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }

	
	@RequestMapping(value="common.list.defect.identification.stages.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDefectIdentificationStagesList(HttpServletRequest request) {
		log.debug("Listing DefectIdentificationStages List");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<DefectIdentificationStageMaster> defectIdentificationStages = testExecutionBugsService.listDefectIdentificationStages();
			List<JsonDefectIdentificationStage> jsonDefectIdentificationStages = new ArrayList<JsonDefectIdentificationStage>();
			for(DefectIdentificationStageMaster authenticationType : defectIdentificationStages){
				jsonDefectIdentificationStages.add(new JsonDefectIdentificationStage(authenticationType));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDefectIdentificationStages, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining Defect Identification Stages!");
	            log.error("JSON ERROR Listing DefectIdentificationStages List", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.defect.approval.status.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDefectApprovalStatuses(HttpServletRequest request) {
		log.info("Listing DefectApproval Statuses");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<DefectApprovalStatusMaster> defectDefectApprovalStatuses = testExecutionBugsService.listDefectApprovalStatus();
			List<JsonDefectApprovalStatus> jsonDefectApprovalStatuses = new ArrayList<JsonDefectApprovalStatus>();
			for(DefectApprovalStatusMaster defectApprovalStatus : defectDefectApprovalStatuses){
				jsonDefectApprovalStatuses.add(new JsonDefectApprovalStatus(defectApprovalStatus));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDefectApprovalStatuses, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining Defect Approval Status!");
	            log.error("JSON ERROR Listing DefectApproval Statuses", e);
	        }	        
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="testcase.defectID.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getWorkpackageTestcaseExecDefectsId(@RequestParam int tcerId, @RequestParam int defectId) {
		log.debug("Getting WorkpackageTestcaseExec Defects");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<TestExecutionResultBugList> defectList=workPackageService.listDefectsByTestcaseExecutionPlanId(tcerId,0, 10);
			List<JsonTestExecutionResultBugList> jsonTestExecutionResultBugList = new ArrayList<JsonTestExecutionResultBugList>();
		
			
			for(TestExecutionResultBugList testExecutionbug : defectList){
				jsonTestExecutionResultBugList.add(new JsonTestExecutionResultBugList(testExecutionbug));
			}
			
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestExecutionResultBugList, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining Bug List!");
	            log.error("JSON ERROR Getting WorkpackageTestcaseExec Defects", e);
	        }	        
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="common.list.tms",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTMS(HttpServletRequest request,@RequestParam int productId) {
		log.debug("Listing TMS");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			
			List<TestManagementSystem> testManagementSystems = testManagementService.listTestManagementSystem(productId);
			List<JsonTestManagementSystem> jsonTestManagementSystems = new ArrayList<JsonTestManagementSystem>();
			for(TestManagementSystem tms : testManagementSystems){
				jsonTestManagementSystems.add(new JsonTestManagementSystem(tms));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestManagementSystems, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining TMS!");
	            log.error("JSON ERROR obtaining TMS", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.evidenceType",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listEvidenceType(HttpServletRequest request) {
		log.info("common.list.tms");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			
			List<EvidenceType> evidenceTypes = workPackageService.getEvidenceTypes();
			List<JsonEvidenceType> jsonEvidenceTypes = new ArrayList<JsonEvidenceType>();
			for(EvidenceType et : evidenceTypes){
				jsonEvidenceTypes.add(new JsonEvidenceType(et));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonEvidenceTypes, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining Evidence Type!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	@RequestMapping(value="common.listEntityConfigPropsMasterByentityConfigPropsMasterId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listEntityConfigurationPropertiesMasterByentityConfigPropertiesMasterId(HttpServletRequest request,@RequestParam Integer entityConfigPropertiesMasterId,@RequestParam Integer entityMasterId) {
		log.info("common.listEntityConfigPropsMasterByentityConfigPropsMasterId");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			String option=null;
			String arrOption[];
			List<EntityConfigurationPropertiesMaster> entityConfigurationPropertiesMasterList=entityConfigurationPropertiesService.listEntityConfigurationPropertiesMasterByEntityConfigPropertiesMasterId(entityConfigPropertiesMasterId,entityMasterId);
			List<JsonEntityConfigurationPropertiesMaster> jsonEntityConfigurationPropertiesMasterList = new ArrayList<JsonEntityConfigurationPropertiesMaster>();
			
			if(entityConfigPropertiesMasterId==1 || entityConfigPropertiesMasterId==3){ // 1 is locale
				EntityConfigurationPropertiesMaster entityConfigPropsMaster=entityConfigurationPropertiesMasterList.get(0);
				option=entityConfigPropsMaster.getOptions();
				arrOption=option.split(";");
				for(String locale:arrOption){
					EntityConfigurationPropertiesMaster entityConfigurationPropertiesMaster=new EntityConfigurationPropertiesMaster();
					entityConfigurationPropertiesMaster.setEntityConfigPropertiesMasterId(1);
					entityConfigurationPropertiesMaster.setOptions(locale);
					jsonEntityConfigurationPropertiesMasterList.add(new JsonEntityConfigurationPropertiesMaster(entityConfigurationPropertiesMaster));
				}
				
			}else{
				for(EntityConfigurationPropertiesMaster entityConfigurationPropertiesMaster : entityConfigurationPropertiesMasterList){
					jsonEntityConfigurationPropertiesMasterList.add(new JsonEntityConfigurationPropertiesMaster(entityConfigurationPropertiesMaster));
				}
				
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonEntityConfigurationPropertiesMasterList, true);		
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining Property Values!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	@RequestMapping(value="common.list.entityConfigPropsMaster",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listLocale(HttpServletRequest request,@RequestParam Integer entityMasterId) {
		log.info("common.list.entityConfigPropsMaster");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<EntityConfigurationPropertiesMaster> entityConfigurationPropertiesMasterList=entityConfigurationPropertiesService.listEntityConfigurationPropertiesMasterByEntityConfigPropertiesMasterId(-1,entityMasterId);
			List<JsonEntityConfigurationPropertiesMaster> jsonEntityConfigurationPropertiesMasterList = new ArrayList<JsonEntityConfigurationPropertiesMaster>();
			for(EntityConfigurationPropertiesMaster entityConfigurationPropertiesMaster : entityConfigurationPropertiesMasterList){
				jsonEntityConfigurationPropertiesMasterList.add(new JsonEntityConfigurationPropertiesMaster(entityConfigurationPropertiesMaster));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonEntityConfigurationPropertiesMasterList, false);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining Properties!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="process.list.builds.by.product",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listBuildsByProductId(HttpServletRequest request,@RequestParam Integer productId) {
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<ProductBuild> listOfProductBuild = productListService.listBuildsByProductId(productId);
			List<JsonProductBuild> listOfJsonProductBuild = new ArrayList<JsonProductBuild>();
		
			for(ProductBuild pbuild : listOfProductBuild){
				listOfJsonProductBuild.add(new JsonProductBuild(pbuild));
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", listOfJsonProductBuild, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining builds records!");
	            log.error("JSON ERROR", e);
	        }	
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="process.list.process.statuses",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listprocessStatuses(HttpServletRequest request) {
		log.debug("process.list.process.statuses");
		JTableResponseOptions jTableResponseOptions;		 
		try {		
			
			List<ActivityStatus> listOfActivityStatus = activityStatusService.listActivityStatus();
			List<JsonActivityStatus> listOfJsonActivityStatus = new ArrayList<JsonActivityStatus>();
		
			for(ActivityStatus status : listOfActivityStatus){
				listOfJsonActivityStatus.add(new JsonActivityStatus(status));
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", listOfJsonActivityStatus, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining process statuses records!");
	            log.error("JSON ERROR", e);
	        }	 
		log.info("jTableResponseOptions success");
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="common.list.activity.tasktype",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listAllActivityTaskType(@RequestParam Integer productId) {
		log.debug("common.list.activity.tasktype");
		JTableResponseOptions jTableResponseOptions;		 
		try {				
			List<JsonActivityTaskType> listOfJsonActivityTaskType = new ArrayList<JsonActivityTaskType>();
			ProductMaster productMaster = productListService.getProductById(productId);
			if(productMaster != null){
				List<ActivityTaskType> listOfActivityTaskType = activityTaskService.listActivityTaskTypes(productMaster.getTestFactory().getTestFactoryId(), productMaster.getProductId(), 1, null, null, true);
				for(ActivityTaskType taskType : listOfActivityTaskType){
					listOfJsonActivityTaskType.add(new JsonActivityTaskType(taskType));
				}
			}
			jTableResponseOptions = new JTableResponseOptions("OK", listOfJsonActivityTaskType, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining  tasktype records!");
	            log.error("JSON ERROR", e);
	        }	 
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="process.list.activity.by.activityworkpackage",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listActivitiessByAWPId(HttpServletRequest request,@RequestParam Integer activityWorkPackageId,Integer isActive) {
		log.info("process.list.activity.by.activityworkpackage");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<JsonActivity> listOfJsonActivities = activityService.listActivitiesByActivityWorkPackageId(activityWorkPackageId,0,isActive);
			jTableResponseOptions = new JTableResponseOptions("OK", listOfJsonActivities, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining builds records!");
	            log.error("JSON ERROR", e);
	        }	 
		log.info("jTableResponseOptions success");
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="process.list.activity.type",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listActivityTypes(HttpServletRequest request,Integer activityWorkPackageId) {
		JTableResponseOptions jTableResponseOptions;
		ActivityWorkPackage workRequestFromUI = null;
        ProductBuild productBuild = null;
        ProductVersionListMaster productVersion = null;
        ProductMaster productMaster = null;
        Customer customer=null;
        TestFactory testFactory = null;
        Integer jtStartIndex=0;
        Integer jtPageSize=50;
        int initializationLevel=1;
        List<JsonActivityMaster> listOfActivityTypes =new ArrayList<JsonActivityMaster>();
		try {	
			
			workRequestFromUI=activityWorkPackageService.getActivityWorkPackageById(activityWorkPackageId, 1);
			if(workRequestFromUI != null){
		 	productBuild = workRequestFromUI.getProductBuild();
            productVersion = productBuild.getProductVersion();
            productMaster = productVersion.getProductMaster();
            testFactory = productMaster.getTestFactory();
            listOfActivityTypes = activityTypeService.listActivityTypes(testFactory.getTestFactoryId(), productMaster.getProductId(), jtStartIndex, jtPageSize, initializationLevel, true);
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", listOfActivityTypes, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining activity type records!");
	            log.error("JSON ERROR", e);
	        }			
        return jTableResponseOptions;
    }
	
	
	
	@RequestMapping(value="list.features.by.activity",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listProductFeaturesByActivity(HttpServletRequest request, @RequestParam Integer activityId, @RequestParam Integer activityWorkPackageId) {
		JTableResponseOptions jTableResponseOptions = null;
		ProductMaster product = null;
		try {
			if(activityId != -1){
				Activity activity = activityService.getActivityById(activityId,1);
				product = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster();
			
			}else{
				ActivityWorkPackage activityWp = activityWorkPackageService.getActivityWorkPackageById(activityWorkPackageId,1);
				product = activityWp.getProductBuild().getProductVersion().getProductMaster();
			}
			List<ProductFeature> listOfProductFeature = productListService.getFeatureListByProductId(product.getProductId(),null,null,false);
			List<JsonProductFeature> jsonProductFeatures = new ArrayList<JsonProductFeature>();
			for(ProductFeature pf: listOfProductFeature){
				jsonProductFeatures.add(new JsonProductFeature(pf));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductFeatures);
	    } catch (Exception e) {
	        log.error("JSON ERROR", e);
	    }
        return jTableResponseOptions;
    }
	
	

	@RequestMapping(value="process.list.changerequest",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listChangeRequests(HttpServletRequest request) {
		log.info("process.list.changerequest");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<ChangeRequest> listChangeRequests = changeRequestService.listChangeRequests();
			List<JsonChangeRequest> listOfJsonChangeRequests = new ArrayList<JsonChangeRequest>();
		
			for(ChangeRequest changeRequest : listChangeRequests){
				listOfJsonChangeRequests.add(new JsonChangeRequest(changeRequest));
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", listOfJsonChangeRequests, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining activity type records!");
	            log.error("JSON ERROR", e);
	        }	 
		log.info("jTableResponseOptions success");
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="process.list.activity.result",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listActivityResults(HttpServletRequest request) {
		log.debug("process.list.activity.results");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<ActivityResult> listOfActivityResults = activityResultService.listActivityResult();
			List<JsonActivityResult> listOfJsonActivityResults = new ArrayList<JsonActivityResult>();
		
			for(ActivityResult result : listOfActivityResults){
				listOfJsonActivityResults.add(new JsonActivityResult(result));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", listOfJsonActivityResults, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining process Result Records!");
	            log.error("JSON ERROR", e);
	        }	 
		log.info("jTableResponseOptions success");
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="activity.group.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listActivityGroups() {
		log.debug("inside activity.group.option.list");
		JTableResponseOptions jTableResponseOptions;
		try {			
			List<ActivityGroup> activityGroupList = activityGroupService.listActivityGroups(TAFConstants.ENTITY_STATUS_ACTIVE, null, null);
			List<JsonActivityGroup> jsonActivityGroupList =new ArrayList<JsonActivityGroup>();
			if(activityGroupList==null || activityGroupList.size()==0){
				jTableResponseOptions = new JTableResponseOptions("ERROR","Please add activity group!");
				 return jTableResponseOptions;
			}else{
				for(ActivityGroup activityGroup: activityGroupList){
					jsonActivityGroupList.add(new JsonActivityGroup(activityGroup));
				}	
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonActivityGroupList,true);  
			activityGroupList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show Customers!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	@RequestMapping(value="activity.type.master.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listActivityTypeMaster(@RequestParam Integer activityGroupId) {
		log.info("Fetching activity.type.master.option.list "+activityGroupId);
		JTableResponseOptions jTableResponseOptions;
		try {			
			List<ActivityType> activityTypeMasterList = activityTypeService.listActivityTypes(TAFConstants.ENTITY_STATUS_ACTIVE, null, null,1);
			List<JsonActivityType> jsonActivityTypeMasterList = new ArrayList<JsonActivityType>();
			if(activityTypeMasterList==null || activityTypeMasterList.size()==0){
				jTableResponseOptions = new JTableResponseOptions("ERROR","Please add activity type master!");
				 return jTableResponseOptions;
			}else{
				for(ActivityType activityType: activityTypeMasterList){
					
					if(activityType.getActivityGroup().getActivityGroupId()==activityGroupId){
						jsonActivityTypeMasterList.add(0, new JsonActivityType(activityType));
					}else{
						jsonActivityTypeMasterList.add(new JsonActivityType(activityType));
					}
					
				}	
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonActivityTypeMasterList,true);  
			activityTypeMasterList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show activity type master!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="environment.combinations.options.by.activity",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listEnvironmentConbinationMappedToActivity(HttpServletRequest request,Integer activityId,Integer activityWorkPkgId) {
		log.debug("environment.combinations.options.by.activity");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<EnvironmentCombination> listOfEnvironmentCombinations = environmentService.getEnvironmentsCombinationsMappedToActivity(activityId,activityWorkPkgId);
			List<JsonEnvironmentCombination> listOfJsonEnvironmentCombinations = new ArrayList<JsonEnvironmentCombination>();
		
			for(EnvironmentCombination result : listOfEnvironmentCombinations){
				listOfJsonEnvironmentCombinations.add(new JsonEnvironmentCombination(result));
			}
			if(listOfEnvironmentCombinations == null || listOfEnvironmentCombinations.isEmpty()){
				EnvironmentCombination envComb = new EnvironmentCombination();
				envComb.setEnvironment_combination_id(0);
				envComb.setEnvironmentCombinationName("No Data");
				listOfJsonEnvironmentCombinations.add(new JsonEnvironmentCombination(envComb));
				jTableResponseOptions = new JTableResponseOptions("OK", listOfJsonEnvironmentCombinations, true);
			}
			else{
				jTableResponseOptions = new JTableResponseOptions("OK", listOfJsonEnvironmentCombinations, true);		
			}
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining Environment Combination records!");
	            log.error("JSON ERROR", e);
	        }	 
		log.info("jTableResponseOptions success");
        return jTableResponseOptions;
    } 
  
 @RequestMapping(value = "available.dr.List", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse listAllavailableDr() {
		log.info("inside process.activity.list");
		JTableResponse jTableResponse;
		try {
			List<JsonClarificationTracker> jsonClarificationTrackersList = null;
			jsonClarificationTrackersList = clarificationTrackerService.listJsonClarificationTrackers();
			jTableResponse = new JTableResponse("OK", jsonClarificationTrackersList);
			log.info("inside process fetching jsonClarificationTrackersList records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
 
	@RequestMapping(value="common.user.list.by.resourcepool.id",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listUsersbyResourcePoolId() {
		log.debug("inside common.user.list");
		JTableResponseOptions jTableResponseOptions = null;		 
		try {			
			List<UserList> userList=userListService.getActivityUserListBasedRoleId(-10);
			List<JsonUserList> jsonUserList=new ArrayList<JsonUserList>();
			for(UserList ul: userList){
				jsonUserList.add(new JsonUserList(ul));
				
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList,true);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }

	@RequestMapping(value="common.user.list.by.resourcepool.id.productId",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listUsersbyResourcePoolIdAndProductId(@RequestParam Integer productId) {
		log.debug("inside common.user.list");
		JTableResponseOptions jTableResponseOptions = null;	
		try {			
			if(productId == null) {
				productId=0;
			}
			 
			List<UserList> userList=userListService.getActivityUserListBasedRoleIdAndProductId(-10,productId);
			List<JsonUserList> jsonUserList=new ArrayList<JsonUserList>();
			for(UserList ul: userList){
				jsonUserList.add(new JsonUserList(ul));
				
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList,true);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.user.list.by.resourcepool.id.activityId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listUsersbyResourcePoolIdByActivityId(@RequestParam Integer activityId) {
		log.debug("inside common.user.list");
		JTableResponseOptions jTableResponseOptions = null;	
		ProductMaster product = null;
		List<JsonUserList> jsonUserList=new ArrayList<JsonUserList>();
		try {			
			
			if(activityId != null && activityId != -1){
				Activity activity = activityService.getActivityById(activityId,1);
				product = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster();
				List<UserList> userList=userListService.getActivityUserListBasedRoleIdAndProductId(-10,product.getProductId());
				for(UserList ul: userList){
					jsonUserList.add(new JsonUserList(ul));					
				}
				}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList,true);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="process.list.activity.entity.master",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listActivityEntityMaster(HttpServletRequest request) {
		log.debug("process.list.activity.entity.master");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<ActivityEntityMaster> listOfActivityEntities = activityEntityMasterService.listActivityEntityMaster();
			List<JsonActivityEntityMaster> listOfJsonActivityEntities = new ArrayList<JsonActivityEntityMaster>();
		
			for(ActivityEntityMaster activityEntities : listOfActivityEntities){
				listOfJsonActivityEntities.add(new JsonActivityEntityMaster(activityEntities));
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", listOfJsonActivityEntities, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining process listOfJsonActivityEntities records!");
	            log.error("JSON ERROR", e);
	        }	 
		log.info("jTableResponseOptions success");
        return jTableResponseOptions;
    }
	@RequestMapping(value="process.list.comment.type.master",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listActivityCommentsTypeMaster(HttpServletRequest request) {
		log.debug("process.list.activity.entity.master");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<CommentsTypeMaster> listOfCommentsTypes = commentsTypeMasterService.listCommentsTypeMaster();
			List<JsonCommentsTypeMaster> listOfJsonCommentTypes = new ArrayList<JsonCommentsTypeMaster>();
		
			for(CommentsTypeMaster commentsTypeMaster : listOfCommentsTypes){
				listOfJsonCommentTypes.add(new JsonCommentsTypeMaster(commentsTypeMaster));
			}
					
			jTableResponseOptions = new JTableResponseOptions("OK", listOfJsonCommentTypes, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining process listOfJsonCommentTypes records!");
	            log.error("JSON ERROR", e);
	        }	 
		log.info("jTableResponseOptions success");
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="activity.secondary.status.master.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listActivitySecondaryStatusMaster(@RequestParam Integer statusId) {
		log.info("inside activity.secondary.status.master.option.list "+statusId);
		JTableResponseOptions jTableResponseOptions;
		try {			
			
			List<ActivitySecondaryStatusMaster> activitySecondaryStatusMasterList = activitySecondaryStatusMasterService.listActivitySecondaryStatusMaster(statusId, null, null,1);
			List<JsonActivitySecondaryStatusMaster> jsonActivitySecondaryStatusMasterList = new ArrayList<JsonActivitySecondaryStatusMaster>();
			if(activitySecondaryStatusMasterList==null || activitySecondaryStatusMasterList.size()==0){
				jTableResponseOptions = new JTableResponseOptions("ERROR","Please add activity Secondary Status master!");
				 return jTableResponseOptions;
			}else{	
				for(ActivitySecondaryStatusMaster activitySecondaryStatusMaster: activitySecondaryStatusMasterList){
				jsonActivitySecondaryStatusMasterList.add(new JsonActivitySecondaryStatusMaster(activitySecondaryStatusMaster));
					}
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonActivitySecondaryStatusMasterList,true);  
			activitySecondaryStatusMasterList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Secondary Status Not Mapped!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }	

	@RequestMapping(value="activity.primary.status.master.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listActivityStatusMasterByProductId(@RequestParam Integer productId) {
		log.debug("process.list.process.statuses");
		JTableResponseOptions jTableResponseOptions;		 
		List<ActivityStatus> listOfActivityStatus =null;
		ProductMaster product=null;
		try {	
			List<Integer>competencyList=dimensionService.getDimensionIdByProductId(productId, 2);
			if(competencyList !=null && competencyList.size()>0){
			listOfActivityStatus = activityStatusService.listActivityStatusByDimensionId(competencyList.get(0));
			List<JsonActivityStatus> listOfJsonActivityStatus = new ArrayList<JsonActivityStatus>();
			if(listOfActivityStatus!=null && listOfActivityStatus.size() >0){
				for(ActivityStatus status : listOfActivityStatus){
					listOfJsonActivityStatus.add(new JsonActivityStatus(status));
				}
			}
			jTableResponseOptions = new JTableResponseOptions("OK", listOfJsonActivityStatus, true);
			return jTableResponseOptions;
			}
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Please map status to the product.");
            log.error("JSON ERROR", e);
        }	
		jTableResponseOptions = new JTableResponseOptions("ERROR","Please map status to the product.");
		return jTableResponseOptions; 
    }
	@RequestMapping(value="activity.primary.status.master.option.list.by.activityId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listActivityStatusMasterByActivityId(@RequestParam Integer activityId) {
		log.debug("process.list.process.statuses");
				JTableResponseOptions jTableResponseOptions;		 
				List<ActivityStatus> listOfActivityStatus =null;
				ProductMaster product=null;
				Integer productId = 0;
		try {	   ActivityWorkPackage activityWorkPackage = null;
			        ProductBuild productBuild = null;
			        ProductVersionListMaster productVersion = null;
			        ProductMaster productMaster = null;
			        if(activityId != null && activityId != -1  ){
				Activity activity = activityService.getActivityById(activityId,1);
				activityWorkPackage = activityService.getActivityWorkPackageByActivityId(activityId,1);
				    productBuild = activityWorkPackage.getProductBuild();
		            productVersion = productBuild.getProductVersion();
		            productMaster = productVersion.getProductMaster();         
		            productId = productMaster.getProductId();
			
			        }
			List<Integer>competencyList=dimensionService.getDimensionIdByProductId(productId, 2);
			log.info("competencyList.get(0)"+competencyList);
			if(competencyList !=null && competencyList.size()>0){
			listOfActivityStatus = activityStatusService.listActivityStatusByDimensionId(competencyList.get(0));
			List<JsonActivityStatus> listOfJsonActivityStatus = new ArrayList<JsonActivityStatus>();
			if(listOfActivityStatus!=null && listOfActivityStatus.size() >0){
				for(ActivityStatus status : listOfActivityStatus){
					listOfJsonActivityStatus.add(new JsonActivityStatus(status));
				}
			}
			jTableResponseOptions = new JTableResponseOptions("OK", listOfJsonActivityStatus, true);
			return jTableResponseOptions;
			}
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Please map status to the product.");
            log.error("JSON ERROR", e);
        }	
		jTableResponseOptions = new JTableResponseOptions("ERROR","Please map status to the product.");
		log.info("jTableResponseOptions success");
		return jTableResponseOptions; 
    }
	
	/*
	 * This method returns all the TestRunPlans that the test case is part of through it's test suites
	 */
	@RequestMapping(value="common.list.testcase.testrunplans",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions getTestCaseTestRunPlans(@RequestParam Integer testCaseId) {
		
		log.debug("common.list.testcase.testrunplans");
		JTableResponseOptions jTableResponseOptions;		 
		try {
			List<JsonTestRunPlan> jsonTestRunPlans = new ArrayList<JsonTestRunPlan>();
			jsonTestRunPlans = testCaseService.getTestCaseTestRunPlans(testCaseId);
			if (jsonTestRunPlans == null || jsonTestRunPlans.isEmpty()) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","There are no test run plans for the testcase : " + testCaseId);
	        	log.info("There are no test run plans for the testcase : " + testCaseId);
			} else {
				jTableResponseOptions = new JTableResponseOptions("OK", jsonTestRunPlans, true);
			}
		} catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining test run plans for the testcase : " + testCaseId);
			log.error("Unable to get TestRunPlans for Testcase : " + testCaseId);
		}
		return jTableResponseOptions;
	}
	

	@RequestMapping(value="entity.type.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions getEntityTypeList() {
		
		log.debug("Inside entity.type.list");
		JTableResponseOptions jTableResponseOptions;		 
		try {
			List<JsonEntityMaster> jsonEntityMasters = new ArrayList<JsonEntityMaster>();
			jsonEntityMasters = commonService.getEntityTypeList();
			if (jsonEntityMasters == null || jsonEntityMasters.isEmpty()) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","There are no entity type list available");
	        	log.info("There are no entity list available");
			} else {
				jTableResponseOptions = new JTableResponseOptions("OK", jsonEntityMasters, true);
			}
		} catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining entity type list");
			log.error("Unable to get entity type list", e);
		}
		return jTableResponseOptions;
	}
	
	@RequestMapping(value="entity.for.type.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions getEntityForTypeList(@RequestParam Integer engagementId, @RequestParam Integer productId, @RequestParam Integer entityTypeId) {
		log.debug("Inside entity.for.type.list");
		JTableResponseOptions jTableResponseOptions;		 
		try {
			jTableResponseOptions = commonService.getEntityForTypeList(engagementId, productId, entityTypeId);
		} catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining entity list for type - "+entityTypeId);
			log.error("Unable to get entity list for entity type - "+entityTypeId, e);
		}
		return jTableResponseOptions;
	}
	@RequestMapping(value="worflow.capable.entity.type.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions getWorkflowCapableEntityTypeList() {
		
		log.debug("Inside entity.type.list");
		JTableResponseOptions jTableResponseOptions;		 
		try {
			List<JsonEntityMaster> jsonEntityMasters = new ArrayList<JsonEntityMaster>();
			jsonEntityMasters = commonService.getWorkflowCapableEntityTypeList();
			if (jsonEntityMasters == null || jsonEntityMasters.isEmpty()) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","There are no workflow capable entity type list available");
	        	log.info("There are no entity list available");
			} else {
				jTableResponseOptions = new JTableResponseOptions("OK", jsonEntityMasters, true);
			}
		} catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining entity type list");
			log.error("Unable to get entity type list", e);
		}
		return jTableResponseOptions;
	}

	@RequestMapping(value="upload.attachment.for.entity.or.instance",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse uploadAttachmentForEntityOrInstance(HttpServletRequest req, @RequestParam Integer productId, @RequestParam Integer entityTypeId, @RequestParam Integer entityInstanceId, @RequestParam String description, @RequestParam String attachmentType,@RequestParam Integer isEditable){
		JTableSingleResponse jTableSingleResponse = null;
		try { 
			String catalinaHome = System.getProperty("catalina.home");
			UserList user = (UserList)req.getSession().getAttribute("USER");
			String folderPath = catalinaHome + File.separator + "webapps" + File.separator + "Attachments" + File.separator + entityTypeId+"\\"+entityInstanceId;
			String message = "Attachment uploaded successfully";
			
			if(entityInstanceId == 0){
				folderPath = catalinaHome + File.separator + "webapps" + File.separator + "Attachments" + File.separator + entityTypeId +"\\user\\"+user.getUserId();
			}
			
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
			CommonsMultipartFile multipartFile = null;
			String fileName = "";
			String fileExtension = "";
			String exisistingFileNames = "";
			
			List<JsonAttachment> attachments = new ArrayList<JsonAttachment>();
			
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				Attachment attachment = new Attachment();
				if(productId != null && productId > 0){
					ProductMaster product = new ProductMaster();
					product.setProductId(productId);
					attachment.setProduct(product);
				}
				EntityMaster entityMaster = new EntityMaster();
				entityMaster.setEntitymasterid(entityTypeId);
				attachment.setEntityMaster(entityMaster);						
				attachment.setEntityId(entityInstanceId);
				attachment.setAttachmentType(attachmentType);							
				attachment.setDescription(description);
				attachment.setIsEditable(isEditable);
				attachment.setCreatedBy(user);
				attachment.setModifiedBy(user);

				attachment.setLastModifiedDate(DateUtility.getCurrentTime());
				attachment.setModifiedDate(DateUtility.getCurrentTime());
				attachment.setUploadedDate(DateUtility.getCurrentTime());
				
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileName = multipartFile.getOriginalFilename();

				List<Attachment> existingAttachments = commonService.getAttachmentOfEntityOrInstance(entityTypeId, entityInstanceId, fileName.substring(0, fileName.lastIndexOf(".")), user.getUserId(), null, null);
				if(existingAttachments != null && existingAttachments.size() > 0){
					if(exisistingFileNames == null || exisistingFileNames.isEmpty()){
						exisistingFileNames = fileName;
					}else{
						exisistingFileNames += ", "+fileName;
					}
					continue;
				}
				
				Long size = multipartFile.getSize();
				String fileSize = "0 MB";
				if(size > 0){
					fileSize = String.format("%.2f", ((size.floatValue() / 1024) / 1024))+" MB";
				}
				attachment.setAttributeFileSize(fileSize);
				
				fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
				
				if(fileName != null){
					attachment.setAttributeFileName(fileName.substring(0, fileName.lastIndexOf(".")));
					attachment.setAttachmentName(fileName.substring(0, fileName.lastIndexOf(".")));
				}
				if(fileExtension != null){
					attachment.setAttributeFileExtension(fileExtension);
				}
				InputStream content = multipartFile.getInputStream();
				File filePath = new File(folderPath);
				File file = new File(folderPath + "\\"+ fileName);
				if (!filePath.isDirectory()) {
					try{
						FileUtils.forceMkdir(filePath);
					}catch(Exception ex){
						log.error("Unable to create directory, please check the base location given for attachment ", ex);
						message = "Unable to create directory, please check the base location given for attachment";
						jTableSingleResponse = new JTableSingleResponse("ERROR", message);
						return jTableSingleResponse;
					}
				}
				boolean isFileCreated = CommonUtility.copyInputStreamToFile(content, file);
				attachment.setAttributeFileURI(file.getAbsolutePath());
				if(isFileCreated){
					commonService.addAttachment(attachment);
					JsonAttachment jsonAttachment = new JsonAttachment(attachment);
					attachments.add(jsonAttachment);
				}
			}
			
			if(exisistingFileNames != null && !exisistingFileNames.trim().isEmpty()){
				message = exisistingFileNames+" already available for the same entity";
			}
			jTableSingleResponse = new JTableSingleResponse("OK", attachments, message);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error while uploading attachment");
			log.error("JSON ERROR", e);
		}			        
		return jTableSingleResponse;
	}
	
	@RequestMapping(value="delete.attachment.for.entity.or.instance",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse deleteAttachmentForEntityOrInstance(HttpServletRequest req, @RequestParam Integer attachmentId){
		JTableSingleResponse jTableSingleResponse = null;
		try { 
			boolean isFileDeleted = false;
			String message = "Unable to delete attachment";
			Attachment attachment = commonService.getAttachmentById(attachmentId);
			if(attachment != null){
				File file = new File(attachment.getAttributeFileURI());
				if(file.exists() && !file.isDirectory()){
					isFileDeleted = file.delete();
				}else{
					isFileDeleted = true;
				}
				if(isFileDeleted){
					commonService.deleteAttachment(attachment);
					message = "Attachment deleted successfully";
				}
			}
			
			jTableSingleResponse = new JTableSingleResponse("OK", message);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error while uploading attachment");
			log.error("JSON ERROR", e);
		}			        
		return jTableSingleResponse;
	}
	
	@RequestMapping(value="delete.attachment.for.multiple.entity.or.instance",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse deleteAttachmentForMultipleEntityOrInstance(HttpServletRequest req, @RequestParam String attachmentIds){
		JTableSingleResponse jTableSingleResponse = null;
		try { 
			boolean isFileDeleted = false;
			String message = "Unable to delete attachment";
			String[] attachmentIdSplits = attachmentIds.split(",");
			for(String attachmentId : attachmentIdSplits){
				if(!NumberUtils.isNumber(attachmentId)){
					continue;
				}
				Attachment attachment = commonService.getAttachmentById(Integer.parseInt(attachmentId));
				if(attachment != null){
					File file = new File(attachment.getAttributeFileURI());
					if(file.exists() && !file.isDirectory()){
						isFileDeleted = file.delete();
					}else{
						isFileDeleted = true;
					}
					if(isFileDeleted){
						commonService.deleteAttachment(attachment);
						message = "Attachment deleted successfully";
					}
				}
			}
			jTableSingleResponse = new JTableSingleResponse("OK", message);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error while uploading attachment");
			log.error("JSON ERROR", e);
		}			        
		return jTableSingleResponse;
	}
	
	@RequestMapping(value="attachment.type.for.entity.list.option",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions getAttachmentTypeForEntityListOption(@RequestParam Integer entityTypeId) {
		
		log.debug("Inside attachment.type.for.entity.list.option");
		JTableResponseOptions jTableResponseOptions;		 
		try {
			List<JsonAttachmentType> jsonAttachmentTypes = new ArrayList<JsonAttachmentType>();
			jsonAttachmentTypes = commonService.getAttachmentTypeForEntity(entityTypeId);
			if (jsonAttachmentTypes == null || jsonAttachmentTypes.isEmpty()) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","There is no attachment type list available for this entity");
	        	log.info("There are no entity list available");
			} else {
				jTableResponseOptions = new JTableResponseOptions("OK", jsonAttachmentTypes, true);
			}
		} catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining entity type list");
			log.error("Unable to get entity type list", e);
		}
		return jTableResponseOptions;
	}
	
	@RequestMapping(value="attachment.for.entity.or.instance.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getAttachmentForEntityOrInstanceList(HttpServletRequest req, @RequestParam Integer productId, @RequestParam Integer entityTypeId, @RequestParam Integer entityInstanceId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize){
		JTableResponse jTableResponse = null;
		try { 
			if(entityTypeId != null && entityTypeId == 0){
				entityTypeId = null;
			}
			if(entityInstanceId != null && entityInstanceId == 0){
				entityInstanceId = null;
			}
			List<Attachment> attachments = null;
			Integer attachmentsPagination = 0;
			
			List<Integer> entityInstanceIds = null;
			
			if(IDPAConstants.ENTITY_PRODUCT_VERSION_ID == entityTypeId && entityInstanceId == null){
				entityInstanceIds = commonService.getEntityInstanceIdsOfEntityByProductId(productId, entityTypeId);
			}
			
			if(entityInstanceIds != null){
				if(entityInstanceIds.size() > 0){
					attachments = commonService.getAttachmentOfEntityOrInstance(entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
					attachmentsPagination = commonService.getAttachmentOfEntityOrInstancePagination(entityTypeId, entityInstanceIds);
				}
			}else{
				UserList user = (UserList)req.getSession().getAttribute("USER");
				attachments = commonService.getAttachmentOfEntityOrInstance(entityTypeId, entityInstanceId, null, user.getUserId(), jtStartIndex, jtPageSize);
				attachmentsPagination = commonService.getAttachmentOfEntityOrInstancePagination(entityTypeId, entityInstanceId, null, user.getUserId());
			}
			List<JsonAttachment> jsonAttachments = new ArrayList<JsonAttachment>();
			
			if(attachments != null && attachments.size() > 0){
				for(Attachment attachment : attachments){
					JsonAttachment jsonAttachment = new JsonAttachment(attachment);

					if(attachment.getEntityMaster() != null){
						if(attachment.getEntityMaster().getEntitymasterid() == IDPAConstants.TESTFACTORY_ENTITY_MASTER_ID){
							TestFactory testFactory = testFactoryManagementService.getTestFactoryById(attachment.getEntityId());									
							jsonAttachment.setEntityName(testFactory.getTestFactoryName());
						}else if(attachment.getEntityMaster().getEntitymasterid() == IDPAConstants.PRODUCT_ENTITY_MASTER_ID){
							ProductMaster productMaster = productListService.getProductById(attachment.getEntityId());
							jsonAttachment.setEntityName(productMaster.getProductName());
						}else if(attachment.getEntityMaster().getEntitymasterid() == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID){
							Activity activity = activityService.getByActivityId(attachment.getEntityId());
							jsonAttachment.setEntityName(activity.getActivityName());
						}
					}
					jsonAttachments.add(jsonAttachment);
				}
			}
			
			jTableResponse = new JTableResponse("OK", jsonAttachments, attachmentsPagination);
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error while retriving attachment list");
			log.error("JSON ERROR", e);
		}			        
		return jTableResponse;
	}	

	@RequestMapping(value="comments.for.entity.or.instance.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getCommentsForEntityOrInstanceList(HttpServletRequest req, @RequestParam Integer productId,
			@RequestParam Integer entityTypeId, @RequestParam Integer entityInstanceId, 
			@RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize){
		JTableResponse jTableResponse = null;
		 
		try {				
			List<Comments> commentsList = commonService.getCommentsOfEntityIDorEntityIDList(entityTypeId, entityInstanceId, null, null, null, jtStartIndex, jtPageSize);
					
			int totalCommentsCountOfEntity = commonService.getCommentsCountOfEntityOrInstancePagination(entityTypeId, entityInstanceId, null, null);
			 
			List<JsonComments> jsonCommentsList = new ArrayList<JsonComments>();
			if(commentsList != null && !commentsList.isEmpty()){
				for (Comments commentsObj : commentsList) {
					jsonCommentsList.add(new JsonComments(commentsObj));
				}
			}									
			jTableResponse = new JTableResponse("OK", jsonCommentsList,totalCommentsCountOfEntity);			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Comments!");
	            log.error("JSON ERROR showing Comments : ", e);
	        }	
		return jTableResponse;
	}	

	@RequestMapping(value="comments.for.entity.or.instance.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody Integer addCommentsForEntityOrInstance(HttpServletRequest req,@RequestParam Map<String, String>  mapData) {
		log.info("Adding Comments ");
		JTableSingleResponse jTableSingleResponse = null;
		int commentId = 0;
		try {			
			UserList user = (UserList)req.getSession().getAttribute("USER");
			int userId = user.getUserId();
			user = userListService.getUserListById(userId);
			Comments commentsObj = new Comments();
			commentsObj.setComments(mapData.get("commentsText"));
			EntityMaster entityMaster=workPackageService.getEntityMasterById(Integer.parseInt(mapData.get("entityTypeId")));	
			ProductMaster product=new ProductMaster();
			Integer productId=Integer.parseInt((mapData.get("productId")!=null && !mapData.get("productId").isEmpty()) ?mapData.get("productId"):"0");
			if(productId != null && productId >0) {
				product.setProductId(productId);
				commentsObj.setProduct(product);
			}
			commentsObj.setEntityMaster(entityMaster);
			commentsObj.setEntityId(Integer.parseInt(mapData.get("entityInstanceId")));						
			commentsObj.setCreatedBy(user);
			commentsObj.setCreatedDate(DateUtility.getCurrentTime());				
			commonService.addComments(commentsObj);
			commentId = commentsObj.getCommentId();
		} catch (Exception e) {
			log.error("JSON ERROR", e);
		}			        
		return commentId;
	}
	
	@RequestMapping(value="attachments.consolidated.for.product.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getAttachmentsConsolidatedForProductList(HttpServletRequest req, @RequestParam Integer productId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize){
		JTableResponse jTableResponse = null;
		try { 
			List<Attachment> attachments = null;
			Integer attachmentsPagination = 0;
			
			attachments = commonService.getAttachmentsConsolidatedForProduct(productId, jtStartIndex, jtPageSize);
			attachmentsPagination = commonService.getAttachmentsConsolidatedForProductPagination(productId);
			List<JsonAttachment> jsonAttachments = new ArrayList<JsonAttachment>();
			if(attachments != null && attachments.size() > 0){
				for(Attachment attachment : attachments){
					JsonAttachment jsonAttachment = new JsonAttachment(attachment);
					jsonAttachments.add(jsonAttachment);
				}
			}
			
			jTableResponse = new JTableResponse("OK", jsonAttachments, attachmentsPagination);
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error while retriving consolidated attachments list for product");
			log.error("JSON ERROR", e);
		}			        
		return jTableResponse;
	}
	
	@RequestMapping(value="common.allusers.list.by.resourcepool.id",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listAllUsersbyResourcePoolId() {
		log.debug("inside common.user.list");
		JTableResponseOptions jTableResponseOptions = null;		 
		try {			
			List<UserList> userList=userListService.getAllUserListBasedRoleId(-10);
			List<JsonUserList> jsonUserList=new ArrayList<JsonUserList>();
			for(UserList ul: userList){
				jsonUserList.add(new JsonUserList(ul));
				
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList,true);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value = "update.attachment.for.entity.or.instance", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse updateWorkRequest(HttpServletRequest request, @ModelAttribute JsonAttachment jsonAttachment,BindingResult result) {
		log.debug("update.attachment.for.entity.or.instance");
		JTableResponse jTableResponse = null;
		Attachment attachment = null;
		      
		if (result.hasErrors()) {
			jTableResponse = new JTableResponse("ERROR", "Invalid Form!");
		}
		try {
         attachment = jsonAttachment.getAttachments();
         commonService.updateAttachment(attachment);         
         
         List<JsonAttachment> jsonAttachmentList=new ArrayList<JsonAttachment>();
		  jsonAttachmentList.add(new JsonAttachment(attachment));
         
		 jTableResponse = new JTableResponse("OK", jsonAttachmentList, jsonAttachmentList.size());	
			
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Unable to update the Attachment!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}	
	
	@RequestMapping(value = "list.source.entity.by.type.and.instance", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getSourceEntityByEntityTypeAndInstanceIds(@RequestParam Integer entityType, @RequestParam Integer entityInstance) {
		log.info("list.source.entity.by.type.and.instance execution started");
		JTableResponse jTableResponse = null;
		try{
			List<JsonEntityRelationship> jsonEntityRelationshipList = entityRelationshipService.listSourceEntitiesByTypeAndInstanceIds(entityType, entityInstance);
			jTableResponse = new JTableResponse("OK", jsonEntityRelationshipList);
		}catch(Exception e){
			jTableResponse = new JTableResponse("ERROR", "list.source.entity.by.Type.and.Instance failed");
		}
		return jTableResponse;
	}
	@RequestMapping(value="common.testFactory.list.byLabId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestFactoryByTestLabId(@RequestParam int testFactoryLabId, @RequestParam int engagementTypeId) {			
		JTableResponseOptions jTableResponseOptions;			 
		try {
			log.info("listing TestFactory By TestLabId");
			List<TestFactory> tfList=testFactoryManagementService.getTestFactoryList(testFactoryLabId,TAFConstants.ENTITY_STATUS_ACTIVE,engagementTypeId);
			List<JsonTestFactory> jsontfList=new ArrayList<JsonTestFactory>();
			for(TestFactory tfactory: tfList){
				jsontfList.add(new JsonTestFactory(tfactory));	
				}				
			jTableResponseOptions = new JTableResponseOptions("OK", jsontfList,true);     
			tfList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching TestFactory!");
	            log.error("JSON ERROR fetching TestFactory!", e);
	        }		        
        return jTableResponseOptions;
    }
	@RequestMapping(value="common.list.product.byTestFactoryId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listOfProductsByTestFactoryId(@RequestParam int testFactoryId) {
		log.debug("inside common.list.product.byTestFactoryId");
		log.info("common.list.product.byTestFactoryId--->"+testFactoryId);
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			
			List<ProductMaster> productMaster =productListService.getCustmersbyProductId(testFactoryId,0);  
			List<JsonProductMaster> jsonProductMaster=new ArrayList<JsonProductMaster>();
			for(ProductMaster dvm: productMaster){
				jsonProductMaster.add(new JsonProductMaster(dvm));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductMaster);
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;		
		
    }
	
	@RequestMapping(value="common.list.complexity",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listOfComplexityValues() {
		log.debug("Fetching complexity");
		JTableResponseOptions jTableResponseOptions;
		 List<JsonActivityTask> jsonActivityTasks = new ArrayList<JsonActivityTask>();
		try {
			JsonActivityTask jsonActivityTask = new JsonActivityTask();
			jsonActivityTask.setComplexity(IDPAConstants.PRIORITY_LOW);
			jsonActivityTasks.add(jsonActivityTask);
			jsonActivityTask = new JsonActivityTask();
			jsonActivityTask.setComplexity(IDPAConstants.PRIORITY_MEDIUM);
			jsonActivityTasks.add(jsonActivityTask);
			jsonActivityTask = new JsonActivityTask();
			jsonActivityTask.setComplexity(IDPAConstants.PRIORITY_HIGH);
			jsonActivityTasks.add(jsonActivityTask);
			jTableResponseOptions = new JTableResponseOptions("OK", jsonActivityTasks);
			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
	}
	
	@RequestMapping(value="administration.products.by.test.factory.id.and.user",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getProductsByTestFactoryIdAndUser(@RequestParam Integer testFactoryId, HttpServletRequest request) {
		log.info("inside administration.products.by.test.factory.id.and.user");
		JTableResponse jTableResponse;
		 List<JsonDashBoardTabsRoleBasedURL> jsonDashBoardTabsRoleBasedURLs = new ArrayList<JsonDashBoardTabsRoleBasedURL>();
		try {
			UserList user = (UserList)request.getSession().getAttribute("USER");
			List<ProductMaster> productMasters = productListService.getProductByUserCustomerAndEngagement(user.getUserId(), user.getUserRoleMaster().getUserRoleId(), null, testFactoryId, TAFConstants.ENTITY_STATUS_ACTIVE);
			JsonDashBoardTabsRoleBasedURL jsonDashBoardTabsRoleBased = new JsonDashBoardTabsRoleBasedURL();
			if(productMasters != null){
				String productDetails = "";
				for(ProductMaster productMaster : productMasters){
					if(productDetails == null || productDetails.trim().isEmpty()){
						productDetails = productMaster.getProductId()+"~~~"+productMaster.getProductName();
					}else{
						productDetails += ","+productMaster.getProductId()+"~~~"+productMaster.getProductName();
					}
				}
				jsonDashBoardTabsRoleBased.setProductDetails(productDetails);
			}
			jsonDashBoardTabsRoleBasedURLs.add(jsonDashBoardTabsRoleBased);
			jTableResponse = new JTableResponse("OK", jsonDashBoardTabsRoleBasedURLs);
			
	    } catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
	        
        return jTableResponse;
	}
	
	@RequestMapping(value="common.list.column.series.by.frequency",method=RequestMethod.GET ,produces="application/json")
    public @ResponseBody JTableResponse getColumnsByFrequency(@RequestParam String frequencyType, @RequestParam Integer frequencyYear, @RequestParam Integer frequencyMonth) {
		log.debug("common.list.column.series.by.frequency");
		JTableResponse jTableResponse;
		try {
			List<JsonSimpleOption> columnNames = new ArrayList<JsonSimpleOption>();	
			columnNames = commonService.getColumnByFrequency(frequencyType, frequencyMonth, frequencyYear);
			jTableResponse = new JTableResponse("OK", columnNames, columnNames.size());
	    } catch (Exception e) {
	       	jTableResponse = new JTableResponse("ERROR","Error fetching Column names!");
	        log.error("JSON ERROR", e);	            
	    }	        
        return jTableResponse;
    }
	@RequestMapping(value="common.list.product.byTestFactoryIds",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listOfProductsByTestFactoryIds(@RequestParam String testFactoryId) {
		log.debug("inside common.list.product.byTestFactoryIds");
		log.info("common.list.product.byTestFactoryIds--->"+testFactoryId);
		JTableResponseOptions jTableResponseOptions;
		 
		List<Integer> testFactoryIdList=new ArrayList<Integer>(); 
		try {
			String[] namesList = testFactoryId.split(",");
			for(String name : namesList){
				testFactoryIdList.add(Integer.parseInt(name));
			}
			System.out.println("testFactoryIdList>>>>"+testFactoryIdList.size());
			List<ProductMaster> productMaster =productListService.getProductsByEngagementId(testFactoryIdList);   
			List<JsonProductMaster> jsonProductMaster=new ArrayList<JsonProductMaster>();
			for(ProductMaster dvm: productMaster){
				jsonProductMaster.add(new JsonProductMaster(dvm));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductMaster);
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
	        
        return jTableResponseOptions;		
		
    }
	@RequestMapping(value="common.list.testdata.attachmentbyProductId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestDataAttachmentByProductId(@RequestParam int productId,@RequestParam int versionId,  @RequestParam String attachmentType) {
		log.debug("inside common.list.testdata.attachment");
		JTableResponseOptions jTableResponseOptions;		 
		try {			
			
			if(attachmentType.equalsIgnoreCase("1")){
				attachmentType = IDPAConstants.TDTYPE_TESTDATA;
			}else if(attachmentType.equalsIgnoreCase("2")){
				attachmentType = IDPAConstants.TDTYPE_OBJ_REPOSITORY;
			}
			List<Attachment> attachmentList=attachmentService.listTestDataAttachment(productId, -1, attachmentType);
			
			List<JsonAttachment> jsonAttachmentList=new ArrayList<JsonAttachment>();
			if(attachmentList != null && !attachmentList.isEmpty()){
				for(Attachment attach: attachmentList){
					jsonAttachmentList.add(new JsonAttachment(attach));
				}	
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonAttachmentList);
			attachmentList = null;   
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);	           
        }	        
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="list.features.by.enagment",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listProductFeaturesByActivity(HttpServletRequest request, @RequestParam Integer engagementId,@RequestParam Integer productId,@RequestParam Integer activityId, @RequestParam Integer activityWorkPackageId) {
		JTableResponseOptions jTableResponseOptions = null;
		ProductMaster product = null;
		List<ProductFeature> listOfProductFeature =null;
		try {
			if(activityId != -1){
				Activity activity = activityService.getActivityById(activityId,1);
				product = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster();
			
			}else if(activityWorkPackageId != null && activityWorkPackageId >0){
				ActivityWorkPackage activityWp = activityWorkPackageService.getActivityWorkPackageById(activityWorkPackageId,1);
				product = activityWp.getProductBuild().getProductVersion().getProductMaster();
				listOfProductFeature = productListService.getFeatureListByProductId(product.getProductId(),null,null,false);
			} else if(productId != null && productId >0) {
				listOfProductFeature = productListService.getFeatureListByProductId(productId,null,null,false);
			} else if(engagementId != null && engagementId >0) {
				listOfProductFeature = productListService.getFeatureListByEnagementId(engagementId,null,null,false);
			}
			List<JsonProductFeature> jsonProductFeatures = new ArrayList<JsonProductFeature>();
			if(listOfProductFeature != null && listOfProductFeature.size() >0 ) {
				for(ProductFeature pf: listOfProductFeature){
					jsonProductFeatures.add(new JsonProductFeature(pf));
				}
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductFeatures);
	    } catch (Exception e) {
	        log.error("JSON ERROR", e);
	    }
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="process.list.activity.type.engagement",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listActivityTypes(HttpServletRequest request,Integer engagementId,Integer productId) {
		JTableResponseOptions jTableResponseOptions;
		List<JsonActivityMaster> listOfActivityTypes =new ArrayList<JsonActivityMaster>();
		try {	
			
				listOfActivityTypes = activityTypeService.listActivityTypesByEnagementIdAndProductId(engagementId, productId, true);
				jTableResponseOptions = new JTableResponseOptions("OK", listOfActivityTypes, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining activity type records!");
	            log.error("JSON ERROR", e);
	        }			
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="common.user.list.by.resourcepool.id.engagement",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listUsersbyResourcePoolIdAndEngement(@RequestParam Integer engagementId,@RequestParam Integer productId) {
		log.debug("inside common.user.list.by.resourcepool.id.engagement");
		JTableResponseOptions jTableResponseOptions = null;
		List<UserList> userList=new ArrayList<UserList>();
		List<ProductMaster> subListOfProducts=null;
		try {
			if(productId != null && productId<=0 ){
				subListOfProducts = productMasterDAO.getProductsByTestFactoryId(engagementId);
				if(subListOfProducts != null && subListOfProducts.size() >0){
					for(ProductMaster product:subListOfProducts) {
						userList.addAll(userListService.getActivityUserListBasedRoleIdAndProductId(-10,product.getProductId()));
					}
				} else {
					userList=userListService.getActivityUserListBasedRoleIdAndProductId(-10,productId);
				}
			}	
			List<JsonUserList> jsonUserList=new ArrayList<JsonUserList>();
			for(UserList ul: userList){
				jsonUserList.add(new JsonUserList(ul));
				
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList,true);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.auto.allocate.resources.for.instance",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse autoAllocateResourcesForInstance(HttpServletRequest request, @RequestParam Integer entityId, @RequestParam Integer entityInstanceId, @RequestParam Integer parentEntityId, @RequestParam Integer parentEntityInstanceId) {
		JTableResponse jTableResponse = null;
		try{
			UserList user = (UserList)request.getSession().getAttribute("USER");
			String message = commonService.getInstanceAndAutoAllocationOfResource(entityId, entityInstanceId, parentEntityId, parentEntityInstanceId, user);
			jTableResponse = new JTableResponse("INFO", message);
		}catch(Exception ex){
			log.error("Error occured in autoAllocateResourcesForInstance - ", ex);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="user.group.list.options",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listOfUserGroupsOption(@RequestParam Integer testFactoryId, Integer productId, Boolean isConsolidated) {
		JTableResponseOptions jTableResponseOptions;
		try {
			List<UserGroup> userGroups = userListService.getUserGroups(testFactoryId, productId, isConsolidated);  
			List<JsonUserGroup> jsonUserGroups = new ArrayList<JsonUserGroup>();
			for(UserGroup userGroup : userGroups){
				jsonUserGroups.add(new JsonUserGroup(userGroup));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonUserGroups);
		} catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
		}

		return jTableResponseOptions;		

	}
	
	
	@RequestMapping(value="comments.for.entity.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getCommentsList(Integer testFactoryId,Integer productId,Integer workPackageId,Integer entityTypeId,String fromDate,String toDate){
		JTableResponse jTableResponse = null;
		 
		try {	
			log.info("Inside comments.for.entity.list ");
			Date startDate = null;
			Date endDate = null;
			if(fromDate != ""){
				startDate = DateUtility.dateFormatWithOutSeconds(fromDate);
			}
			
			if(toDate != ""){
				endDate = DateUtility.dateFormatWithOutSeconds(toDate);
			}
			
			
			List<Comments> commentsList = commonService.getCommentsListBasedOnDateFillter(testFactoryId,productId,workPackageId,entityTypeId,startDate,endDate);
			List<Integer>ids = new ArrayList<Integer>();
			List<JsonComments> jsonCommentsList = new ArrayList<JsonComments>();
			HashMap<Integer, String>map  = new HashMap<Integer, String>();
					if(entityTypeId.equals(IDPAConstants.RESOURCEDEMAND_ENTITY_MASTER_ID)){
						
						for(Comments comment :commentsList){
							jsonCommentsList.add(new JsonComments(comment));
						}
					}else{
						if(commentsList != null && commentsList.size() != 0){
							for(Comments comments :commentsList){
									ids.add(comments.getEntityId());
						 
							}	
						}
						List<Activity>activities = activityService.getActivitiesForSetOfActivityIds(ids);
						
						if(entityTypeId.equals(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID)){
							if(activities != null && activities.size() != 0){
								for(Activity activity :activities){
									if(!map.containsKey(activity.getActivityId())){
										map.put(activity.getActivityId(), activity.getActivityName());
									}
								}
							}
						}
						
						
				
				if(commentsList != null && !commentsList.isEmpty()){
					for (Comments commentsObj : commentsList) {
						jsonCommentsList.add(new JsonComments(commentsObj));
					}
				}	
			
				if(jsonCommentsList != null && jsonCommentsList.size() != 0){
					for(JsonComments jsonComments :jsonCommentsList){
						if(map.containsKey(jsonComments.getEntityPrimaryId())){
							jsonComments.setEntityName(map.get(jsonComments.getEntityPrimaryId()));
						}
					}
				}
					}
					
			jTableResponse = new JTableResponse("OK", jsonCommentsList,commentsList.size());			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Comments!");
	            log.error("JSON ERROR showing Comments : ", e);
	        }	
			        
		return jTableResponse;
	}	
	
	@RequestMapping(value="comments.for.entity.latest",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getCommentsByEntityLatest(Integer entityTypeId){
		JTableResponse jTableResponse = null;
		 
		try {	
			log.info("Inside comments.for.entity.latest ");
			
			Comments comments = commonService.getLatestCommentOfByEntityType(entityTypeId);
			List<JsonComments> jsonCommentsList = new ArrayList<JsonComments>();
					if(comments != null){						
							jsonCommentsList.add(new JsonComments(comments));
					}
					
			jTableResponse = new JTableResponse ("OK", jsonCommentsList);			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Comments!");
	            log.error("JSON ERROR showing Comments : ", e);
	        }	
			        
		return jTableResponse;
	}
	
	
	@RequestMapping(value="update.by.attachment.type.for.entity.or.instance",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse updateAttachmentForEntityOrInstance(HttpServletRequest req,@RequestParam Integer entityTypeId,@RequestParam Integer entityInstanceId,@RequestParam Integer attachmentId,@RequestParam String attachmentFilename,@RequestParam String attachmentType){
		JTableSingleResponse jTableSingleResponse = null;
		try { 
			String message = "Attachment file edited successfully";
			String fileContents=(String)req.getParameter("RESULT");
			
			InputStream content =null;
			if(fileContents !="" && !fileContents.isEmpty()){
				fileContents= fileContents.replaceAll(";", "\r\n");
				content = new ByteArrayInputStream(fileContents.toString().getBytes(StandardCharsets.UTF_8));
			} else {
				message = "Unable to create file, please check the file content";
				jTableSingleResponse = new JTableSingleResponse("ERROR", message);
				return jTableSingleResponse;
			}
			
			boolean isFileDeleted = false;
			Attachment attachment = commonService.getAttachmentById(attachmentId);
			if(attachment != null){
				File file = new File(attachment.getAttributeFileURI());
				if(file.exists() && !file.isDirectory()){
					isFileDeleted = file.delete();
				}else{
					isFileDeleted = true;
				}
				
			}
			
			UserList user = (UserList)req.getSession().getAttribute("USER");
			
			String folderPath = attachmentBaseLocation+entityTypeId+"\\"+entityInstanceId;
			
			
			if(entityInstanceId == 0){
				folderPath = attachmentBaseLocation+entityTypeId+"\\user\\"+user.getUserId();
			}
			
			String fileName = attachmentFilename;
			String fileExtension = "";
			String exisistingFileNames = "";
			
			List<JsonAttachment> attachments = new ArrayList<JsonAttachment>();
			
				EntityMaster entityMaster = new EntityMaster();
				entityMaster.setEntitymasterid(entityTypeId);
				attachment.setEntityMaster(entityMaster);						
				attachment.setEntityId(entityInstanceId);
				attachment.setAttachmentType(attachmentType);							
				
				attachment.setCreatedBy(user);
				attachment.setModifiedBy(user);

				attachment.setLastModifiedDate(DateUtility.getCurrentTime());
				attachment.setModifiedDate(DateUtility.getCurrentTime());
				attachment.setUploadedDate(DateUtility.getCurrentTime());
				
				fileName += attachmentType;

				List<Attachment> existingAttachments = commonService.getAttachmentOfEntityOrInstance(entityTypeId, entityInstanceId, fileName.substring(0, fileName.lastIndexOf(".")), user.getUserId(), null, null);
				if(existingAttachments != null && existingAttachments.size() > 0){
					if(exisistingFileNames == null || exisistingFileNames.isEmpty()){
						exisistingFileNames = fileName;
					}else{
						exisistingFileNames += ", "+fileName;
					}
				}
				
			
				
				fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
				
				if(fileName != null){
					attachment.setAttributeFileName(fileName.substring(0, fileName.lastIndexOf(".")));
					attachment.setAttachmentName(fileName.substring(0, fileName.lastIndexOf(".")));
				}
				if(fileExtension != null){
					attachment.setAttributeFileExtension(fileExtension);
				}
				
				
				File filePath = new File(folderPath);
				File file = new File(folderPath + "\\"+ fileName);
				
				if (!filePath.isDirectory()) {
					try{
						FileUtils.forceMkdir(filePath);
					}catch(Exception ex){
						log.error("Unable to create directory, please check the base location given for attachment ", ex);
						message = "Unable to create directory, please check the base location given for attachment";
						jTableSingleResponse = new JTableSingleResponse("ERROR", message);
						return jTableSingleResponse;
					}
				}
				
				Long size = file.getUsableSpace();
				String fileSize = "0 MB";
				if(size > 0){
					fileSize = String.format("%.2f", ((size.floatValue() / 1024) / 1024))+" MB";
				}
				attachment.setAttributeFileSize(fileSize);
				
				boolean isFileCreated = CommonUtility.copyInputStreamToFile(content, file);
				attachment.setAttributeFileURI(file.getAbsolutePath());
				if(isFileCreated){
					commonService.updateAttachment(attachment);
					JsonAttachment jsonAttachment = new JsonAttachment(attachment);
					attachments.add(jsonAttachment);
				}
			
			
			jTableSingleResponse = new JTableSingleResponse("OK", attachments, message);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error while uploading attachment");
			log.error("JSON ERROR", e);
		}			        
		return jTableSingleResponse;
	}
	
	
	
	@RequestMapping(value="get.attachment.type.for.entity.instance.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getAttachmentForEntityOrInstance(HttpServletRequest req, @RequestParam Integer attachmentId){
		JTableResponse jTableResponse = null;
		String fileContent="";
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		
		try { 
			
			
			Attachment attachment = commonService.getAttachmentById(attachmentId);
			File file = new File(attachment.getAttributeFileURI());
			
			fis = new FileInputStream(file);

			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			while (dis.available() != 0) {
				fileContent+=dis.readLine()+";";
			}
			
			fileContent=fileContent.replaceAll(";","\r\n");
			if(fileContent.endsWith("\r\n")) {
				fileContent = fileContent.substring(0, fileContent.length()-2);
			}
			
			jTableResponse = new JTableResponse("OK", fileContent);
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error while getAttachmentForEntityOrInstance");
			log.error("JSON ERROR", e);
		}finally{
			 try {
				 fis.close();
					bis.close();
					dis.close();
			} catch (IOException ioe) {
				log.error("BR",ioe);
			}
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value="common.multiple.testScriptsLevel.options",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestScriptsLevelOptions() {
		log.info("inside common.multiple.testScriptsLevel.options ");
		JTableResponseOptions jTableResponseOptions;
		try {			
			
			List<JsonCommonOption> jsonTestScriptsLevelOptions = new ArrayList<JsonCommonOption>();
			
			JsonCommonOption typeOption=new JsonCommonOption();
			typeOption.setValue(IDPAConstants.MULTIPLE_TEST_RUN_PLAN_LEVEL_SCRIPT_PACK);
			jsonTestScriptsLevelOptions.add(typeOption);
			
			typeOption=new JsonCommonOption();
			typeOption.setValue(IDPAConstants.MULTIPLE_TEST_SUITE_LEVEL_SCRIPT_PACKS);
			jsonTestScriptsLevelOptions.add(typeOption);
			
			typeOption=new JsonCommonOption();
			typeOption.setValue(IDPAConstants.MULTIPLE_RUNCONFIG_LEVEL_SCRIPT_PACK);
			jsonTestScriptsLevelOptions.add(typeOption);
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestScriptsLevelOptions, false);
			
			return jTableResponseOptions;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error in listing testScriptsLevel options");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.multiple.testSuites.options",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listMultipleTestSuitesOptions() {
		log.info("inside common.multiple.testSuites.options ");
		JTableResponseOptions jTableResponseOptions;
		try {			
			
			List<JsonCommonOption> jsonMultipleTestSuitesOptions= new ArrayList<JsonCommonOption>();
			
			JsonCommonOption typeOption=new JsonCommonOption();
			
		
			typeOption.setId(0);
			typeOption.setValue("No");
			jsonMultipleTestSuitesOptions.add(typeOption);
			
			typeOption=new JsonCommonOption();
			typeOption.setId(1);
			typeOption.setValue("Yes");
			jsonMultipleTestSuitesOptions.add(typeOption);
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonMultipleTestSuitesOptions, true);
			
			return jTableResponseOptions;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error in listing testSuites options");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="common.list.parentTestCaseEntitytGroup.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestCaseEntityGroupForParent(@RequestParam Integer productId,@RequestParam Integer testCaseEntityGroupId, @RequestParam String actionType) {

		JTableResponseOptions jTableResponseOptions;
		final boolean flag = false;
		try {	
			List<TestCaseEntityGroup> testCaseEntityGroupFromDB = new ArrayList<TestCaseEntityGroup>();
			List<JsonTestCaseEntityGroup> jsonTestCaseEntityGroupList = new ArrayList<JsonTestCaseEntityGroup>();
			log.info(" Updating Parent Test Case Entity Group : " + actionType);
			if(actionType.equalsIgnoreCase("list") || actionType.equalsIgnoreCase("edit")){
				testCaseEntityGroupFromDB = testCaseEntityGroupService.getTestCaseEntityGroupListExcludingChildByparentTestCaseEntityGroupId(productId,testCaseEntityGroupId);
				log.info("Excluding children");
				
			}else if(actionType.equalsIgnoreCase("create") || actionType.equalsIgnoreCase("default")){
				log.info("Excluding children----- Create  ");
				testCaseEntityGroupFromDB = testCaseEntityGroupService.getAllTestCaseEntityGroups(productId);
			}else{
				testCaseEntityGroupFromDB =testCaseEntityGroupService.getAllTestCaseEntityGroups(productId);
				 log.info("Excluding children----- Anything  ");
			}
			
			for(TestCaseEntityGroup testCaseEntityGp : testCaseEntityGroupFromDB){				
				jsonTestCaseEntityGroupList.add(new JsonTestCaseEntityGroup(testCaseEntityGp));	
			}
			JsonTestCaseEntityGroup jTestCaseEntity = new JsonTestCaseEntityGroup();
			jTestCaseEntity.setTestCaseEntityGroupId(0);
			jTestCaseEntity.setTestCaseEntityGroupName("---");
			jsonTestCaseEntityGroupList.add(jTestCaseEntity);
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestCaseEntityGroupList, flag);
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching TestCaseEntityGroupForParent!");
	            log.error("JSON ERROR  fetching TestCaseEntityGroupForParent!", e);
	        }	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="getEvidenceId.filename",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getEvidenceIdFileName(@RequestParam String evidenceId) {
		JTableResponse jTableResponse = null;
		String evidenceIdFileName = "";
		try{
			Evidence evidence = workPackageService.getEvidenceById(Integer.valueOf(evidenceId));
			String originalEvidenceFileName = "";
			String catalinaPath = System.getProperty("catalina.home");
			String evidencePath = null;
			
			if(evidence != null) { 
				originalEvidenceFileName = evidence.getFileuri();
				if(! originalEvidenceFileName.isEmpty()) {
					evidenceIdFileName = "http://" +request.getServerName() +":" + request.getServerPort() + File.separator + originalEvidenceFileName.replace("bin", "").replace("Evidence", "").replace("webapps", "").replace("ROOT", "");
					evidencePath = catalinaPath + File.separator + originalEvidenceFileName;
				}
			}
			
			log.info("evidence Path :"+evidencePath);
			if(new File(evidencePath).exists()){
				jTableResponse = new JTableResponse("OK","Got Evidence Id File Name", evidenceIdFileName);	            
			}else {
				jTableResponse = new JTableResponse("ERROR","File is not available", "");
			}
			
		} catch(Exception e){
			jTableResponse = new JTableResponse("ERROR","Got Evidence Id File Name Failed", "");
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="common.list.genericdevice",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getGenericDeviceList() {
		JTableResponseOptions jTableResponse = null;
		try{			
			List<GenericDevices> genericDevicesList = environmentService.listGenericDevicesByProductId(0);
			List<JsonGenericDevice> jsonGenericDevicesList=new LinkedList<JsonGenericDevice>();			
			for(GenericDevices genericDevices: genericDevicesList){
				JsonGenericDevice jsonGenericDevices=	new JsonGenericDevice(genericDevices);
				jsonGenericDevicesList.add(jsonGenericDevices);											
			}
			jTableResponse = new JTableResponseOptions("OK", jsonGenericDevicesList); 			
		} catch(Exception e){
			jTableResponse = new JTableResponseOptions("ERROR","Got Generic Device List Failed");
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value="common.list.host",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getHostList() {
		JTableResponseOptions jTableResponse = null;
		List<JsonHostList> jsonHostList = new LinkedList<>();
		try{			
			List<HostList> hostLists = hostService.list();
			if(hostLists != null && hostLists.size() >0) {
				for(HostList hostList: hostLists){				
					JsonHostList jsonHostLists = new JsonHostList(hostList);
					jsonHostList.add(jsonHostLists);
				}			
			}
			jTableResponse = new JTableResponseOptions("OK", jsonHostList); 			
		} catch(Exception e){
			jTableResponse = new JTableResponseOptions("ERROR","Got Host List Failed");
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="common.list.runconfiguration",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getRunConfigList(@RequestParam Integer productId) {
		JTableResponse jTableResponse = null;
		try{
			List<RunConfiguration> runConfigList = productListService.listRunConfiguration(productId);
			List<JsonRunConfiguration> jsonRunConfigList = new ArrayList<JsonRunConfiguration>();			
			for(RunConfiguration runConfig : runConfigList){
				JsonRunConfiguration jsonRunConfiguraiton =	new JsonRunConfiguration(runConfig);
				jsonRunConfigList.add(jsonRunConfiguraiton);											
			}
			jTableResponse = new JTableResponse("OK", jsonRunConfigList,jsonRunConfigList.size()); 			
		} catch(Exception e){
			jTableResponse = new JTableResponse("ERROR","Got Run Configuration List Failed", "");
		}
		return jTableResponse;
	}
	@RequestMapping(value="common.environment.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listEnvironment(@RequestParam int productMasterId, @RequestParam Integer envstatus) {
		log.debug("Listing Environment");
		JTableResponseOptions jTableResponseOptions;			 
		try {
			List<EnvironmentCombination> environmentCombinationsList = environmentService.listEnvironmentCombinationByStatus(productMasterId,envstatus);
			
			List<JsonEnvironmentCombination> jsonEnvironmentCombination=new ArrayList<JsonEnvironmentCombination>();
			for(EnvironmentCombination environmentCombination: environmentCombinationsList){
				jsonEnvironmentCombination.add(new JsonEnvironmentCombination(environmentCombination));					
			}				
			jTableResponseOptions = new JTableResponseOptions("OK", jsonEnvironmentCombination);     
			environmentCombinationsList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to List Environment Combination!");
	            log.error("JSON ERROR listing Environment Combination", e);
	        }		        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="testcase.execution.result.analysis.outcome.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listAnalysisOutcomes() {
		log.info("Fetching testcase.execution.result.analysis.outcome.option.list ");
		JTableResponseOptions jTableResponseOptions;
		try {			
			
			List<JsonCommonOption> jsonAnalysisOutComeOptions= new ArrayList<JsonCommonOption>();
			
			JsonCommonOption analysisRemarkOption=new JsonCommonOption();
			analysisRemarkOption.setValue(IDPAConstants.TESTCASE_EXECUTION_RESULT_ANALYSIS_OUTCOME_TOBECONFIRMED);
			jsonAnalysisOutComeOptions.add(analysisRemarkOption);
			
			analysisRemarkOption=new JsonCommonOption();
			analysisRemarkOption.setValue(IDPAConstants.TESTCASE_EXECUTION_RESULT_ANALYSIS_OUTCOME_FALSE_RESULT);
			jsonAnalysisOutComeOptions.add(analysisRemarkOption);
		
			jTableResponseOptions = new JTableResponseOptions("OK", jsonAnalysisOutComeOptions, false);
			
			return jTableResponseOptions;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error in Analysis Remark Options");
	            log.error("JSON ERROR Fetching Analysis Outcome Options", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.productbuild",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listProductBuild(@RequestParam int productId, @RequestParam int productVersionListId) {
		log.debug("inside common.list.productbuild");
		JTableResponseOptions jTableResponseOptions;
		try {				
			List<ProductBuild> listProductBuild = new LinkedList<ProductBuild>();
			if(productVersionListId != -1)
				listProductBuild = productListService.listProductBuild(productVersionListId);
			else
				listProductBuild = productListService.listBuildsByProductId(productId);
			List<JsonProductBuild> jsonProductBuild=new ArrayList<JsonProductBuild>();
			for(ProductBuild pvlm: listProductBuild){
				jsonProductBuild.add(new JsonProductBuild(pvlm));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductBuild);
			listProductBuild = null;   
		} catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);                      
		}

		return jTableResponseOptions;
	}
	
	@RequestMapping(value="administration.feature.execution.priority.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listFeatureExecutionPriority() {
		log.debug("inside administration.feature.execution.priority.option.list");
		JTableResponseOptions jTableResponseOptions;
		 
		try {			
			List<TestCasePriority> priorityList=productListService.listFeatureExecutionPriority();
			
			List<JsonTestCasePriority> jsonTestCasePriorityList=new ArrayList<JsonTestCasePriority>();
			if(priorityList==null || priorityList.size()==0){
				jTableResponseOptions = new JTableResponseOptions("ERROR","Please create new Execution Priority!");
				 return jTableResponseOptions;
			}else{
			for(TestCasePriority priority: priorityList){
				
				jsonTestCasePriorityList.add(new JsonTestCasePriority(priority));
			}	
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestCasePriorityList,true);  
			jsonTestCasePriorityList = null;
			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show Feature Execution Priority!");
	            log.error("JSON ERROR showing Feature Execution Priority!", e);
	        }
        return jTableResponseOptions;		
    }
	@RequestMapping(value="common.automationmode.options",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listautomationModeOptions() {
		log.info("inside common.automationmode.options ");
		JTableResponseOptions jTableResponseOptions;
		try {			
			
			List<JsonCommonOption> jsonautomationModeOptions = new ArrayList<JsonCommonOption>();
			
			JsonCommonOption typeOption=new JsonCommonOption();
			typeOption.setValue(IDPAConstants.AUTOMATION_MODE_ATTENDED);
			jsonautomationModeOptions.add(typeOption);
			
			typeOption=new JsonCommonOption();
			typeOption.setValue(IDPAConstants.AUTOMATION_MODE_UNATTENDED);
			jsonautomationModeOptions.add(typeOption);
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonautomationModeOptions, false);
			
			return jTableResponseOptions;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error listing automation mode options");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.testplan.runconfiguration",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getTestPlanRunConfigList(@RequestParam Integer testPlanId) {
		JTableResponseOptions jTableResponse = null;
		try{
			Set<RunConfiguration> runConfigList = productListService.getRunConfigurationList(testPlanId,-1);
			List<JsonRunConfiguration> jsonRunConfigList = new ArrayList<JsonRunConfiguration>();			
			for(RunConfiguration runConfig : runConfigList){
				JsonRunConfiguration jsonRunConfiguraiton =	new JsonRunConfiguration(runConfig);
				jsonRunConfigList.add(jsonRunConfiguraiton);											
			}
			jTableResponse = new JTableResponseOptions("OK", jsonRunConfigList); 			
		} catch(Exception e){
			jTableResponse = new JTableResponseOptions("ERROR","Got Run Configuration List Failed");
		}
		return jTableResponse;
	}
	
	@RequestMapping(value={"common.list.testdata.attachment"}, method={RequestMethod.POST}, produces={"application/json"})
    @ResponseBody
    public JTableResponseOptions listTestDataAttachment(HttpServletRequest request, @RequestParam int productId, @RequestParam String attachmentType) {
        JTableResponseOptions jTableResponseOptions = null;
        log.debug((Object)"inside common.list.testdata.attachment");
        try {
        	Attachment attach = null;
            UserList user = (UserList)request.getSession().getAttribute("USER");
            ArrayList<JsonAttachment> jsonAttachmentList = new ArrayList<JsonAttachment>();
            if (attachmentType.equalsIgnoreCase("1")) {
                attachmentType = "TestData";
                 attach = new Attachment();
            	attach.setAttributeFileName("Text");
            	jsonAttachmentList.add(new JsonAttachment(attach));
            	 attach = new Attachment();
            	attach.setAttributeFileName("Number");
            	jsonAttachmentList.add(new JsonAttachment(attach));
            	 attach = new Attachment();
            	attach.setAttributeFileName("Date");
            	jsonAttachmentList.add(new JsonAttachment(attach));
            } else if (attachmentType.equalsIgnoreCase("2")) {
                attachmentType = "ObjectRepository";
                List<String> uiObjectItemList = this.attachmentService.listUIObjectItemPageNamewithUIObjectItemIdByProductId(Integer.valueOf(productId));
                
                
                for(String pageName : uiObjectItemList){
                	 attach = new Attachment();
                	 if(pageName != null && pageName != ""){
                		 attach.setAttributeFileName(pageName);
                     	jsonAttachmentList.add(new JsonAttachment(attach));
                	 }
                	
                }
                
            }
            
          
           
            jTableResponseOptions = new JTableResponseOptions("OK", jsonAttachmentList);
        }
        catch (Exception e) {
            jTableResponseOptions = new JTableResponseOptions("ERROR", "Error fetching records!");
            log.error((Object)"JSON ERROR", (Throwable)e);
        }
        return jTableResponseOptions;
    }
	@RequestMapping(value={"testDataPlan.list.option"}, method={RequestMethod.POST}, produces={"application/json"})
	   @ResponseBody
	   public JTableResponseOptions listTestDataPlanOption(HttpServletRequest request, @RequestParam Integer productId) {
		JTableResponseOptions jTableResponseOptions = null;
	       log.debug((Object)"testDataPlan.list");
	       UserList user = (UserList)request.getSession().getAttribute("USER");
	       try {
	       	List<TestDataPlan> testDataPlanList = new ArrayList<TestDataPlan>();
	       	List<JsonTestDataPlan> jsonTestDataPlanList = new ArrayList<JsonTestDataPlan>();
	       	testDataPlanList = testCaseScriptGenerationService.listTestDataPlan(productId);
	       	
	      		if(testDataPlanList != null && !testDataPlanList.isEmpty()){
	      			for(TestDataPlan tdPlan : testDataPlanList){
	      				jsonTestDataPlanList.add(new JsonTestDataPlan(tdPlan));
	      			}
	      			
	      			jTableResponseOptions = new JTableResponseOptions("OK",jsonTestDataPlanList);
	      		}else{
	      			jTableResponseOptions = new JTableResponseOptions("ERROR", "No Test Data Plans");
	      		}
	      		
	      		
	       }catch (Exception e) {
	    	   jTableResponseOptions = new JTableResponseOptions("ERROR", "Unable to show tags!");
	           log.error((Object)"JSON ERROR", (Throwable)e);
	       }
	       return jTableResponseOptions;
	}
	
	@RequestMapping(value={"testplan.readiness.check"}, method={RequestMethod.POST}, produces={"application/json"})
    @ResponseBody
    public JTableResponse testPlanReadinessCheck(HttpServletRequest request, @RequestParam Integer testPlanId) {
		JTableResponse jTableResponse = new JTableResponse();
		try{
			String filePath = request.getServletContext().getRealPath(File.separator)+File.separator+"data"+File.separator+"license.properties";
			String response = licenseCheckService.licenseAgrementValidation(filePath);
			log.info("Response from licenseAgrementValidation : "+response);
			if(!response.contains("success"))
				return new JTableResponse("ERROR","License expired. Please contact the TAF admin.");
			
			VerificationResult testPlanVerificationResult = productListService.testPlanReadinessCheck(testPlanId);
			
			jTableResponse = new JTableResponse("OK",testPlanVerificationResult.getIsReady(), testPlanVerificationResult.getVerificationMessage());
		} catch(Exception e){
			jTableResponse = new JTableResponse("ERROR", "Error in checking test plan readiness");
		}
		return jTableResponse;
	}
	@RequestMapping(value="common.list.product.mapped.host",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listMappedHostListForProduct(@RequestParam Integer productId) {
		JTableResponseOptions jTableResponse = null;
		List<JsonHostList> jsonHostList = new LinkedList<>();
		List<HostList> hostLists = new LinkedList<>();
		try{			
			ProductMaster product = productListService.getProductById(productId);
			//Create an empty device
			JsonHostList jsonHostListEmpty = new JsonHostList();
			jsonHostListEmpty.setHostId(-1);
			jsonHostListEmpty.setHostName(" - ");
			
			jsonHostList.add(jsonHostListEmpty);
			
			for(HostList hl : product.getHostLists()){
				hostLists.add(hl);
			}
			if(hostLists != null){
				for(HostList hostList: hostLists){				
					JsonHostList jsonHostLists = new JsonHostList(hostList);
					jsonHostList.add(jsonHostLists);
				}	
			}
			jTableResponse = new JTableResponseOptions("OK", jsonHostList); 			
		} catch(Exception e){
			jTableResponse = new JTableResponseOptions("ERROR","Getting Host List Failed");
		}
		return jTableResponse;
	}
	
	/**
	 * 
	 * @param productId
	 * @return
	 * 
	 * Thi method returns the list of all devices mapped to a product, along with an empty option so that devices can be de-selected.
	 */
	@RequestMapping(value="common.list.product.mapped.genericdevice",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listMappedDevicesForProductWithEmptyOption(@RequestParam Integer productId) {
		JTableResponseOptions jTableResponse = null;
		List<GenericDevices> genericDevicesList = new LinkedList<>();
		try{			
			List<JsonGenericDevice> jsonGenericDevicesList=new LinkedList<JsonGenericDevice>();	
			//Create an empty device
			JsonGenericDevice jsonGenericDevicesEmpty = new JsonGenericDevice();
			jsonGenericDevicesEmpty.setGenericsDevicesId(-1);
			jsonGenericDevicesEmpty.setUDID(" - ");
			
			jsonGenericDevicesList.add(jsonGenericDevicesEmpty);
			
			ProductMaster product = productListService.getProductById(productId);
			for(GenericDevices gd : product.getGenericeDevices()){
				genericDevicesList.add(gd);
			}			
			if(genericDevicesList != null){
				for(GenericDevices genericDevices: genericDevicesList){
					JsonGenericDevice jsonGenericDevices=	new JsonGenericDevice(genericDevices);
					jsonGenericDevicesList.add(jsonGenericDevices);											
				}
			}			
			jTableResponse = new JTableResponseOptions("OK", jsonGenericDevicesList); 			
		} catch(Exception e){
			jTableResponse = new JTableResponseOptions("ERROR","Getting Generic Device List Failed");
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="common.copy.testsuite.from.testplan.options",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listcopyTestsuiteFromTestPlanOptions() {
		log.info("inside common.multiple.testSuites.options ");
		JTableResponseOptions jTableResponseOptions;
		try {			
			
			List<JsonCommonOption> jsonMultipleTestSuitesOptions= new ArrayList<JsonCommonOption>();
			
			JsonCommonOption typeOption=new JsonCommonOption();
			
			typeOption.setId(1);
			typeOption.setValue("Yes");
			jsonMultipleTestSuitesOptions.add(typeOption);
			
			typeOption=new JsonCommonOption();
			typeOption.setId(0);
			typeOption.setValue("No");
			jsonMultipleTestSuitesOptions.add(typeOption);
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonMultipleTestSuitesOptions, true);
			
			return jTableResponseOptions;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error in listing testSuites options");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="common.list.testToolMaster.option.supportmatrix",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listTestToolMasterForSupportMatrix(HttpServletRequest request) {
		log.debug("common.list.testToolMaster.option");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<TestToolMaster> testToolMasterList = testRunConfigSer.listTestToolMaster();
			List<JsonTestToolMaster> jsonTestToolMasterList = new ArrayList<JsonTestToolMaster>();

			for(TestToolMaster testToolMaster :testToolMasterList){
				if(!testToolMaster.getTestToolName().equalsIgnoreCase("APPIUM+SELENIUM") && ! testToolMaster.getTestToolName().equalsIgnoreCase("SEETEST+SELENIUM") && !testToolMaster.getTestToolName().equalsIgnoreCase("SELENDROID") && !testToolMaster.getTestToolName().equalsIgnoreCase("PYBOT") && !testToolMaster.getTestToolName().equalsIgnoreCase("SQUISH")&& !testToolMaster.getTestToolName().equalsIgnoreCase("HYBRID_SELENIUM")&& !testToolMaster.getTestToolName().equalsIgnoreCase("SEETEST+CUCUMBER+RUBY")&& !testToolMaster.getTestToolName().equalsIgnoreCase("WEBSERVICE") && !testToolMaster.getTestToolName().equalsIgnoreCase("GENERIC")&& !testToolMaster.getTestToolName().equalsIgnoreCase("BLOCKLY")&& !testToolMaster.getTestToolName().equalsIgnoreCase("SAHI")&& !testToolMaster.getTestToolName().equalsIgnoreCase("HFT")&& !testToolMaster.getTestToolName().equalsIgnoreCase("HWT")&& !testToolMaster.getTestToolName().equalsIgnoreCase("AUTOIT")){
					jsonTestToolMasterList.add(new JsonTestToolMaster(testToolMaster));
				}
			} 
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestToolMasterList,true);
		}catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR","Error in loading or listing Engagement Type records!");
			log.error("JSON ERROR", e);
		}	        
		return jTableResponseOptions;
	}
	@RequestMapping(value="common.environment.group.category.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getEnvironmentCategoryList(HttpServletRequest request) {			
		JTableResponseOptions jTableResponseOptions =null;	
		
		try {
			List<EnvironmentCategory> environmentCategoryList=environmentService.getEnvironmentCategoryListByGroup();
			List<JsonEnvironmentCategory> jsonEnvironmentCategoryList=new ArrayList<JsonEnvironmentCategory>();
			for(EnvironmentCategory environmentCategory: environmentCategoryList){
				jsonEnvironmentCategoryList.add(new JsonEnvironmentCategory(environmentCategory));	
			}				
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonEnvironmentCategoryList,true);     
			environmentCategoryList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show WorkFlows!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.user.dashboard.listUserRole",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDashboardUserRole() {
		log.debug("inside administration.user.dashboard.listUserRole");
		JTableResponseOptions jTableResponseOptions = null;
			try {
				
				List<UserRoleMaster> userRoleMaster = productListService.getAllRoles();
				
			     List<JsonUserRoleMaster> jsonUserRoleMaster = new ArrayList<JsonUserRoleMaster>();
				
				for(UserRoleMaster userRole: userRoleMaster){
					jsonUserRoleMaster.add(new JsonUserRoleMaster(userRole));
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonUserRoleMaster,true);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching listDashboardUserRole!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponseOptions;
    } 
}