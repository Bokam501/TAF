package com.hcl.atf.taf.constants;


public class IDPAConstants {

	//Entity Types
	public static final String  ENTITY_TEST_FACTORY_LAB = "TestFactoryLab";
	public static final String  ENTITY_TEST_FACTORY = "TestFactory";
	public static final String  ENTITY_RESOURCE_POOL = "ResourcePool";
	public static final String  ENTITY_USER_TYPE = "UserType";
	public static final String  ENTITY_USER = "User";
	public static final String  ENTITY_TEST_FACTORY_ENGAGEMENT = "Engagement";
	
	public static final String  ENTITY_PRODUCT = "Product";
	public static final String  ENTITY_PRODUCT_VERSION = "ProductVersion";
	public static final String  ENTITY_PRODUCT_BUILD = "ProductBuild";
	
	public static final String  ENTITY_WORK_PACKAGE_ICON = "WorkPackageIcon";
	public static final String  ENTITY_WORK_PACKAGE = "WorkPackage";
	public static final String  ENTITY_WORK_SHIFT = "Shift";
	public static final String  ENTITY_ENVIRONMENT = "Environment";
	public static final String  ENTITY_DECOUPLING_CATEGORY = "DecouplingCategory";
	public static final String  ENTITY_PRODUCT_FEATURE = "Feature";
	public static final String  ENTITY_PRODUCT_FEATURE_LABEL = "ProductFeature";
	public static final String  ENTITY_TEST_CASE = "TestCase";
	public static final String  ENTITY_TEST_CASE_SCRIPT = "TestCaseScript";
	public static final String  ENTITY_TEST_CASE_STEPS = "TestStep";
	public static final String  ENTITY_TEST_SUITE = "TestSuite";
	public static final String  ENTITY_ENVIRONMENT_GROUP = "EnvironmentGroup";
	public static final String  ENTITY_ENVIRONMENT_CATEGORY = "EnvironmentCategory";
	public static final String  ENTITY_VENDOR = "Vendor";
	public static final String ENTITY_COMPETENCY="Competency";
	
	public static final String  ENTITY_DEVICE_LAB = "DeviceLab";
	public static final String  ENTITY_TEST_RUN_PLAN = "TestRunPlan";
	public static final String  ENTITY_TEST_RUN_PLAN_GROUP = "TestRunPlanGroup";
	public static final String  ENTITY_TEST_MANAGEMENT_SYSTEM = "TestManagementSystem";
	public static final String  ENTITY_DEFECT_MANAGEMENT_SYSTEM = "DefectManagementSystem";
	public static final String  ENTITY_SCM_MANAGEMENT_SYSTEM = "SCMManagementSystem";
	public static final Integer ENTITY_TEST_RUN_PLAN_ID=9;
	public static final Integer ENTITY_RUN_CONFIGURATION_ID=27;
	public static final String SOURCE_COMPONENT_ILCM ="Event.Source.ILCM";
	public static final String EVENT_CREATE = "CREATE";
	public static final String EVENT_UPDATE = "UPDATE";
	public static final String EVENT_DELETE = "DELETE";
	public static final String  ENTITY_MONGO_COLLECTION = "MongoCollection";
	public static final String  ENTITY_PIVOT_REPORT_COLLECTION = "ReportNames";
	
	public static final int ROLE_ID_ADMIN = 1;
	public static final int ROLE_ID_TEST_FACTORY_MANAGER = 2;
	public static final int ROLE_ID_TEST_MANAGER = 3;
	public static final int ROLE_ID_TEST_LEAD = 4;
	public static final int ROLE_ID_TESTER = 5;
	public static final int ROLE_ID_PROGRAM_MANAGER = 6;
	public static final int ROLE_ID_RESOURCE_MANAGER = 7;
	public static final int ROLE_ID_PQA_REVIEWER = 8;
	public static final int ROLE_ID_ALL = 10;
	
	public static final String  PRIORITY_CRITICAL = "CRITICAL";
	public static final String  PRIORITY_URGENT = "URGENT";
	public static final String  PRIORITY_HIGH = "HIGH";
	public static final String  PRIORITY_MAJOR = "MAJOR";
	public static final String  PRIORITY_MEDIUM = "MEDIUM";
	public static final String  PRIORITY_TRIVIAL = "TRIVIAL";
	public static final String  PRIORITY_LOW = "LOW";
	
	public static final String  EXECUTION_PRIORITY_CRITICAL = "P0";
	public static final String  EXECUTION_PRIORITY_HIGH = "P1";
	public static final String  EXECUTION_PRIORITY_MAJOR = "P2";
	public static final String  EXECUTION_PRIORITY_MEDIUM = "P3";
	public static final String  EXECUTION_PRIORITY_TRIVIAL = "P4";
	public static final String  EXECUTION_PRIORITY_PARSE_TOAKEN = "#";
	public static final String  PRIORITY_PARSE_TOKEN = ":";
	
	public static final Integer  WORKPACKAGE_ENTITY_ID = 2;
	public static final Integer  WORKFLOW_STAGE_ID_NEW = 1;
	public static final Integer  WORKFLOW_STAGE_ID_PLANNING = 2;
	public static final Integer  WORKFLOW_STAGE_ID_EXECUTION = 3;
	public static final Integer  WORKFLOW_STAGE_ID_COMPLETED = 4;
	public static final Integer  WORKFLOW_STAGE_ID_CLOSED = 5;
	public static final Integer  WORKFLOW_STAGE_ID_ABORT = 6;
	public static final Integer  WORKFLOW_STAGE_ID_ABORTED = 7;
	public static final Integer  WORKFLOW_STAGE_ID_WORKPACKAGE_PLANNING = 8;
	public static final Integer  WORKFLOW_STAGE_ID_WORKPACKAGE_RESTART = 9;
	
	public static final String  ADMIN_LOGIN_ID = "admin";
	public static final String  FILE_NAME_SEPARATOR = "-";
	public static final String  EVIDENCE_PATH = "\\ROOT\\Evidence\\";
	public static final String  LOG_PATH = "\\webapps\\ROOT\\";
	public static final String  EVIDENCE_PATH_LINUX = "//bin//Evidence//";
	public static final String  TESTRUNJOBREPORTS_PATH = "\\bin\\TestRunJobReports\\";
	public static final String  TESTRUNJOBREPORTS_PATH_LINUX = "//bin//TestRunJobReports";
	public static final String  JASPERREPORTS_PATH = "\\bin\\JasperReports\\";
	public static final String   WORKPACKAGE_REPORTS_PATH = "\\bin\\WPReport\\";
	public static final String  JASPERREPORTS_PATH_LINUX = "//bin//JasperReports";
	public static final String  WORKPACKAGE_REPORTS_PATH_LINUX = "//bin//WPReport";
	public static final String  TESTRUNJOBREPORTS_EXCELPATH = "\\bin\\TestRunJobReports\\Excel\\";
	public static final String  TESTRUNJOBREPORTS_EXCELPATH_LINUX = "//bin//TestRunJobReports//Excel//";//Changes for TAF-iLCM integration - Excel Report - Bug 717
	public static final String  LOCALE_TESTRUNJOBREPORTS_EXCELPATH = "\\bin\\TestRunJobReports\\Locale\\";//Changes for TAF-iLCM integration - Excel Report - Bug 717
	public static final String  LOCALE_TESTRUNJOBREPORTS_EXCELPATH_LINUX = "//bin//TestRunJobReports//Locale//";
	public static final String  TESTRUNJOBREPORTS_FEATURES_PATH = "\\bin\\TestRunJobReports\\FEATURES\\";
	public static final String  TESTRUNJOBREPORTS_FEATURES_PATH_LINUX = "//bin//TestRunJobReports//FEATURES//";
	public static final String TER_NAMING_CONV = "iLCM_TER_";
	public static final String LOCALE_NAMING_CONV = "iLCM_LOCALE_";
	public static final String FEATURE_NAMING_CONV = "iLCM_FEATURES_";
	public static final String EVIDENCE_SCREENSHOT = "SCREENSHOT";
	public static final String EVIDENCE_LOG = "LOG";
	public static final String EVIDENCE_VIDEO = "VIDEO";
	public static final String EVIDENCE_REPORT = "REPORT";
	public static final String EVIDENCE_OTHER = "OTHER";
	
	public static final String  EXECUTION_TYPE_AUTO = "AUTOMATIC";
	public static final String  EXECUTION_TYPE_MANUAL = "MANUAL";
	public static final Integer  EXECUTION_TYPE_AUTO_CODE = 3;
	public static final Integer  EXECUTION_TYPE_MANUAL_CODE = 4;
	
	public static final Integer  ENGAGEMENT_TYPE_TEST_FACTORY = 1;
	public static final Integer  ENGAGEMENT_TYPE_TEST_ENGAGEMENT = 2;
	
	public static final Integer  TEST_FACTORY_MODE_PRODUCT = 1;
	public static final Integer  PROJECT_MODE_PRODUCT = 2;
	
	public static final String EMAIL_RESRVATION_RESERVED_USER = "reservedUser";
	public static final String EMAIL_RESRVATION_RESERVEDBY_USER = "reservedByUser";
	public static final String EMAIL_RESRVATION_RESERVED_WORKPACKAGE = "reservedWorkPackage";
	public static final String EMAIL_RESRVATION_RESERVED_TESTFACROTY = "reservedTestFactory";
	public static final String EMAIL_RESRVATION_RESERVED_DATE = "reserveDate";
	public static final String EMAIL_RESRVATION_RESERVED_SHIFT = "reservedShift";
	
	public static final int USER_ATUHENTICATION_TYPE_LOCAL = 1;
	public static final int USER_ATUHENTICATION_TYPE_ENTERPRISE = 2;
	
	public static final int USER_ATUHENTICATION_MODE_VENDOR = 1;
	public static final int USER_ATUHENTICATION_MODE_CUSTOMER = 2;
	
	//Entity Status
		
	public static final int ENTITY_STATUS_ACTIVE = 1;
	
	public static final Integer  WORKSHIFT_TYPE_ID_MORNING = 1;
	public static final Integer  WORKSHIFT_TYPE_ID_NIGHT = 2;
	public static final Integer  WORKSHIFT_TYPE_ID_GRAVEYARD = 3;
	
	public static final String  SKILL_NA = "NA";
	
	public static final Integer  GRAVE_YARD_SHIFT_TYPE_ID = 3;
	
	public static final String  DEVICE_ACTIVE = "ACTIVE";
	public static final String  DEVICE_INACTIVE = "INACTIVE";
	public static final String  DEVICE_BUSY = "BUSY";
	public static final Integer DEVICE_STATUS_INACTIVE = 0;
	public static final Integer DEVICE_STATUS_ACTIVE = 1;
	public static final Integer DEVICE_STATUS_BUSY = 2;
	
	
	public static final Integer JOB_NEW = 1;//TODO : New status. Add support
	public static final String  JOB_STATUS_NEW ="NEW";
	public static final Integer JOB_QUEUED = 4;
	public static final String  JOB_STATUS_QUEUED ="QUEUED";
	public static final Integer JOB_EXECUTING = 3;
	public static final String  JOB_STATUS_EXECUTING = "EXECUTING";
	public static final Integer JOB_COMPLETED = 5;
	public static final String  JOB_STATUS_COMPLETED = "COMPLETED";
	public static final Integer JOB_ABORT = 6;
	public static final String  JOB_STATUS_ABORT = "ABORT";
	public static final Integer JOB_ABORTED = 7;
	public static final String  JOB_STATUS_ABORTED = "ABORTED";
	public static final Integer JOB_RESTART = 8;
	public static final String  JOB_STATUS_RESTART = "RESTART";

	public static final String JOB_STATUS_PASSED = "PASSED";
	public static final String JOB_STATUS_FAILED = "FAILED";

	public static final String TEST_SCRIPT_SOURCE_ILCM = "TAF Server";
	public static final String TEST_SCRIPT_SOURCE_HUDSON = "HUDSON";
	public static final String TESTCASE_DEFAULT_PRIORITY ="P1";
	
	public static final Integer EXECUTION_STATUS = 2;
	public static final Integer ISEXECUTED =1;
	public static final Integer TESTCASE_PRIORITY =3;
	public static final Integer TESTCASE_EXECUTION=1;
	public static final String ITEM_ID = "itemId";
	public static final String ITEM_NAME = "itemName";
	public static final String ITEM_CODE = "itemCode";

	public static final String  EXECUTION_RESULT_PASSED = "PASSED";
	public static final String  EXECUTION_RESULT_FAILED = "FAILED";
	public static final String  EXECUTION_RESULT_RESTART = "RESTART";
	public static final String  EXECUTION_RESULT_NORUN = "NORUN";
	public static final String  EXECUTION_RESULT_BLOCKED = "BLOCKED";

	public static final Integer  TESTCASE_DEFAULT_PRIORITY_ID =2;
	public static final Integer  TESTCASE_EXECUTION_AUTOMATION =1;
	public static final Integer  TESTCASE_TYPE_FUNCTIONAL =1;
	public static final Integer  TESTSUITE_EXECUTION_AUTOMATION =3;

	public static final String  HPQC_TOOL = "HPQC";
	public static final String  JIRA_TOOL = "JIRA";
	public static final String  TFS_TOOL = "TFS";
	public static final String  SVN_TOOL = "SVN";
	public static final String  TESTLINK_TOOL = "TESTLINK";
	
	public static final Integer  JOBEVIDENCE_ENTITY =8;
	public static final String  ENTITY_TESTCASE_AUTOMATION_SCRIPT ="TestCaseAutomationScript";
	
	public static final String APP_MOBILE="MOBILE";
	public static final String APP_WEB="WEB";
	public static final String APP_DEVICE="DEVICE"; 
	
	public static final String BUGNIZER_NAME="BUGNIZER";
	public static final String STAGE_NAME_LIVE="Live";
	public static final String STAGE_NAME_WEB="Web";
	
	public static final String MONTHLY_VIEW_TYPE = "Monthly";

	//Folders for processing HPQC Checkouts
	public static final String CHECKOUT_FOLDER = "CheckoutFolder";
	public static final String PROCESSINGFOLDER = "processingFolder";
	public static final String OUTPUTFOLDER = "OutputFolder";
	public static final String FINALSCRIPTSLOCATION = "finalScriptslocation";

	public static final String JOB_LOG_PATH="ILCM-Terminal-Job";
	
	
	public static final Integer  TESTCASE_EXECUTION_STATUS_NOT_STARTED = 3;
	public static final Integer  TESTCASE_EXECUTION_STATUS_COMPLETED = 2;
	public static final Integer  TESTCASE_EXECUTION_STATUS_ASSIGNED = 1;
	
	/* Bug filing status */
	public static final Integer  TESTCASE_DEFECTS_STATUS_NEW = 1;
	public static final Integer  TESTCASE_DEFECTS_STATUS_REFERBACK = 2;
	public static final Integer  TESTCASE_DEFECTS_STATUS_REVIEWED = 3;
	public static final Integer  TESTCASE_DEFECTS_STATUS_APPROVED = 4;
	public static final Integer  TESTCASE_DEFECTS_STATUS_CLOSED = 5;
	public static final Integer  TESTCASE_DEFECTS_STATUS_DUPLICATE = 17;
	public static final Integer  TESTCASE_DEFECTS_STATUS_FIXED = 18;
	public static final Integer  TESTCASE_DEFECTS_STATUS_VIRIFIED = 20;
	public static final Integer  TESTCASE_DEFECTS_STATUS_INTENDED_BEHAVIOUR = 21;
	public static final Integer  TESTCASE_DEFECTS_STATUS_NOT_REPRODUCIBLE = 22;
	
	public static final Integer  TESTCASE_DEFECTS_SEVERITYID_BLOCKER =1;
	public static final Integer  TESTCASE_DEFECTS_SEVERITYID_NORMAL =2;
	
	public static final String ENTITY_ATTACHMENT = "Attachment";
	
	/* TestCase Result */
	public static final String TESTCASE_PASSED="PASSED";
	public static final String TESTCASE_FAILED="FAILED";
	public static final String TESTCASE_BLOCKED="BLOCKED";
	public static final String TESTCASE_NORUN="NORUN"; 
	
	public static final String  STATUS_OPEN = "Open";
	public static final String ENTITY_ACTIVITY_WORKPACKAGE= "ActivityWorkpackage";
	public static final String ENTITY_ACTIVITY_WORKPACKAGE_FOLDER_ICON= "ActivityWorkPackageIcon";
	public static final String ENTITY_ACTIVITY= "Activity";
	public static final String ENTITY_ACTIVITY_FOLDER_ICON= "ActivityIcon";
	public static final String ENTITY_ACTIVITY_TASK= "ActivityTask";
	public static final String ENTITY_ACTIVITY_TASK_FOLDER_ICON= "ActivityTaskIcon";
	public static final String ENTITY_CHANGE_REQUEST= "ChangeRequest";
	public static final String ENTITY_CLARIFICATION_TRACKER= "ClarificationTracker";
	public static final String ENTITY_CUSTOMER= "Customer";
	
	public static final String  ACTIVITY_ASSISGNEE = "Assignee";
	public static final String  ACTIVITY_REVIEWVER = "Reviewer";
	public static final String  ACTIVITY_WORK_PACKAGE_OWNER = "Owner";
	public static final String ACTIVITY_PQAREVIEWVER = "PQAREVIEWER";
	
	
	/*Feature adding source Type*/
	public static final String FEATURE_ADD_SOURCE_TYPE= "iLCM";
	public static final String FEATURE_UPLOAD_SOURCE_TYPE= "Import";
	
	
	/* Competency (Defect Collection Constant*/
	public static final String INVALID_TESTCASE= "Invalid Test Case";
	public static final String TESTER_ERROR = "Tester Error";
	public static final String TEST_ENVIRONMENT_ERROR= "Test Environment Error";
	
	public static final String DEFECT_CATEGORY_MANDATORY_INFORMATION= "Against Submitter seeking missing mandatory information";
	public static final String DEFECT_CATEGORY_DIFFERENT_BUILD_OR_PRODUCT= "Against Submitter seeking reproduction on different build/product";
	public static final String DEFECT_CATEGORY_ADDITIONAL_INFORMATION= "Against Submitter seeking additional information";
	
	public static final String DEFECT_DETECTION_GMC_GM= "GMC - GM";
	public static final String DEFECT_DETECTION_Pre_GMC_GM= "Pre GMC � GM";
	public static final String DEFECT_DETECTION_Pre_GMC_GMC= "Pre GMC - GMC";
	
	
	public static final String DATA_EXTRACTOR = "Data Extractor";
	public static final String  STATUS_SELFREVIEW = "SelfReview";
	public static final String  STATUS_SELFREVIEW_ID = "8";
	public static final String  STATUS_PEERREVIEW = "Peer_Review";
	public static final String  STATUS_PQAREVIEW = "PQA_Review";
	public static final String  STATUS_COMPLETED = "Completed";
	public static final String  STATUS_DELIVERED = "Delivered";
	public static final String  STATUS_FORDELIVERY = "For_Delivery";


	public static final String USER_FOR_TESTRUN_PLAN= "System";
	
	//These constants are used for indicating the trigger for a workpackage
	public static final String WORKPACKAGE_SOURCE_TESTRUNPLAN = "Test Run Plan";
	public static final String WORKPACKAGE_SOURCE_NORMAL = "Normal";
	public static final String WORKPACKAGE_SOURCE_CLONED = "Build Cloned";
	public static final String WORKPACKAGE_SOURCE_TESTRUNPLAN_CI_REST = "External or CI Triggered";
	public static final String WORKPACKAGE_SOURCE_TESTRUNPLAN_SCHEDULER = "Scheduler Triggered";
	public static final String WORKPACKAGE_SOURCE_TESTCASE = "Test Case";
	public static final String WORKPACKAGE_SOURCE_TESTRUNPLANGROUP = "Test Run Plan Group";
	
	public static final String  WORKPACKAGE_STATUS_NEW = "NEW";
	public static final String  WORKPACKAGE_STATUS_PLANNING = "PLANNING";
	public static final String  WORKPACKAGE_STATUS_EXECUTING = "EXECUTING";
	public static final String  WORKPACKAGE_STATUS_COMPLETED = "COMPLETED";
	public static final String  WORKPACKAGE_STATUS_CLOSED = "CLOSED";
	public static final String  WORKPACKAGE_STATUS_ABORTED = "ABORTED";
	
	public static final String  EXECUTION_HISTORY_PRODUCT_LEVEL = "productLevel";
	public static final String  EXECUTION_HISTORY_WORKPACKAGE_LEVEL = "workPackageLevel";
	public static final String ENTITY_ACTIVITIES= "Activities";
	
	public static final Integer STATUS_OPEN_CODE=1;
	public static final Integer STATUS_INPROG_CODE=4;
	public static final Integer STATUS_COMPLETED_CODE=3;
	public static final Integer STATUS_HOLD_CODE=2;
	
	public static final String DEVICETYPE_MOBILE = "MOBILE";
	public static final String DEVICETYPE_SDD = "STORAGEDRIVE";
		
	//Product Types. These IDs should match the IDs in the database
	public static final Integer PRODUCT_TYPE_DEVICE=1;
	public static final Integer PRODUCT_TYPE_WEB=2;
	public static final Integer PRODUCT_TYPE_EMBEDDED=3;
	public static final Integer PRODUCT_TYPE_DESKTOP=4;
	public static final Integer PRODUCT_TYPE_MOBILE=5;
	public static final Integer PRODUCT_TYPE_COMPOSITE=6;
	
	//SLA Metrics
	public static final String SCHEDULE_VARIANCE="ScheduleVariance";
	public static final String DEFECT_VALIDITY="DefectValidity";
	public static final String DEFECT_QUALITY="DefectQuality";
	public static final String TESTCASE_JOB_EXECUTION="TestCaseJobExecution";
	public static final String COST_PER_UNIT="CostPerUnit";
	public static final String DEFECT_FIND_RATE="DefectFindRate";
	public static final String INTERNAL_DRE="InternalDre";
	public static final String PRODUCT_QUALITY="ProductQuality";
	public static final String PRODUCTIVITY="Productivity";
	public static final String UTILIZATION_INDEX="UtilizationIndex";
	public static final String PCISCORE="PCISCORE";
	public static final String CSAT="CSAT";
	public static final String ARID="ARID";
	public static final String HEALTH_INDEX="HealthIndex";
	public static final String PRODUCT_CONFIDENCE="ProductConfidence";
	
	public static final String RISK_REMOVAL_INDEX="RiskRemovalIndex";
	
	public static final String TDTYPE_TESTDATA = "TestData";
	public static final String TDTYPE_OBJ_REPOSITORY = "ObjectRepository";
	public static final String TDTYPE_TESTDATA_OBJ_REPOSITORY = "TestDataAndObjectRepository";
	
	public static final Integer ENTITY_ACTIVITY_ID=28;
	
	public static final String  PRODUCT_WEB = "Web";
	public static final String  PRODUCT_MOBILE = "Mobile";
	public static final String  PRODUCT_EMBEDDED = "Embedded";
	public static final String  PRODUCT_DESKTOP = "Desktop";
	public static final String  PRODUCT_DEVICE = "Device";
	
	public static final String  TEST_TOOL_APPIUM = "APPIUM";
	public static final String  TEST_TOOL_SEETEST = "SEETEST";
	public static final String  TEST_TOOL_AUTOIT = "AUTOIT";
	public static final String  TEST_TOOL_SELENIUM = "SELENIUM";
	public static final String  TEST_TOOL_ROBOTIUM = "ROBOTIUM";
	public static final String  TEST_TOOL_TESTCOMPLETE = "TESTCOMPLETE";
	public static final String  TEST_TOOL_PROTRACTOR = "PROTRACTOR";
	public static final String  TEST_TOOL_EDAT = "EDAT";
	public static final String  TEST_TOOL_RESTASSURED = "RESTASSURED";
	public static final String  TEST_TOOL_CUSTOMCISCO = "CUSTOM_CISCO";
	
	//Workflow directory location
	public static final String ENTITY_WORKFLOW_TEMPLATE ="WorkFlow";
	public static final String ENTITY_WORKFLOW_TEMPLATE_STATUS = "WorkFlowStatus";
	public static final String ENTITY_WORKFLOW_POLICY = "WorkFlowsStatusPolicy";
	public static final String ENTITY_WORKFLOW_EVENTS = "WorkFlowEvents";
	public static final String WORKFLOW_DIR_LOCATION="workflow/";
	
	public static final String WORKFLOW_STATUS_POLICY_TYPE_WORKFLOW = "Workflow";
	public static final String WORKFLOW_STATUS_POLICY_TYPE_ENTITY = "Entity";
	public static final String WORKFLOW_STATUS_POLICY_TYPE_INSTANCE = "Instance";
	public static final String WORKFLOW_STATUS_POLICY_ROLE_ACTION_SCOPE="Role";
	public static final String WORKFLOW_STATUS_POLICY_USER_ACTION_SCOPE="User";
	public static final String WORKFLOW_STATUS_POLICY_USERORROLE_ACTION_SCOPE="Role or User";
	public static final String WORKFLOW_STATUS_POLICY_BOTH_ACTION_SCOPE="Both";
	
	public static final String WORKFLOW_STATUS_POLICY_SLA_VIOLATION_NO_ACTION="No Action";
	public static final String WORKFLOW_STATUS_POLICY_SLA_VIOLATION_AUTO_APPROVE="Auto Approve";
	public static final String WORKFLOW_STATUS_POLICY_SLA_VIOLATION_AUTO_REJECT="Auto Reject";
	public static final String WORKFLOW_STATUS_POLICY_SLA_VIOLATION_AUTO_REFERBACK="Auto Referback";
	
	public static final String WORKFLOW_STATUS_USER_ACTION_NOT_COMPELTE="Not Complete";
	public static final String WORKFLOW_STATUS_USER_ACTION_COMPELTED="Completed";
	
	public static final String WORKFLOW_STATUS_USER_ACTION_MANDATORY="Mandatory";
	public static final String WORKFLOW_STATUS_USER_ACTION_OPTIONAL="Oprional";
	
	public static final String WORKFLOW_STATUS_TYPE_BEGIN="Begin";
	public static final String WORKFLOW_STATUS_TYPE_INTERMEDIATE="Intermediate";
	public static final String WORKFLOW_STATUS_TYPE_END="End";
	public static final String WORKFLOW_STATUS_TYPE_ABORT="Abort";

	public static final String WORKFLOW_STATUS_TRANSITION_POLICY_FIRST_ACTION="First Action";
	public static final String WORKFLOW_STATUS_TRANSITION_POLICY_MANDATORY_CHECK="Mandatory Check";
	public static final String WORKFLOW_STATUS_TRANSITION_POLICY_POLLING="Polling";
	public static final String WORKFLOW_STATUS_TRANSITION_POLICY_AUTO_APPROVE="Auto Approve";
	
	public static final String ENTITY_RISK = "Risk";
	public static final String ENTITY_RISK_MITIGATION = "RiskMitigation";
	public static final String ENTITY_RISK_SEVERITY = "RiskSeverityMaster";
	public static final String ENTITY_RISK_LIKEHOOD = "RiskLikehood";
	public static final String ENTITY_RISK_RATING = "RiskRating";
	public static final String ENTITY_RISK_MATRIX = "RiskMatrix";	
	
	public static final String ENTITY_DASHBOARD_VISUALIZATION_URLS ="DashboardVisualizationUrls";
	public static final String ENTITY_DASHBOARD_URLS ="DashboardUrls";
	public static final String ENTITY_DASHBOARD_TABS_ROLEBASED ="DashboardTabsRoleBased";
	
	public static final String WORKFLOW_STATUS_TYPE="Status Workflow";
	public static final String WORKFLOW_LIFE_CYCLE_TYPE="Life Cycle Workflow";
	
	
	/* Entity ID */
	
	public static final Integer  ENTITY_TEST_CASE_EVIDENCE_ID = 4;
	public static final Integer  ENTITY_TEST_STEP_EVIDENCE_ID = 5;
	public static final Integer  ENTITY_PRODUCT_VERSION_ID = 19;
	public static final Integer  ENTITY_WORK_PACKAGE_ID = 2;
	public static final Integer  ENTITY_DEFECT_ID = 1;
	public static final Integer ENTITY_TEST_CASE_ID=3;
	public static final Integer TESTSUITE_ENTITY_MASTER_ID = 7;
	public static final Integer TESTRUNJOB_ENTITY_MASTER_ID = 10;
	public static final Integer ENGAGEMENT_ENTITY_MASTER_ID = 13;
	public static final Integer TESTFACTORY_ENTITY_MASTER_ID = 14;
	public static final Integer PRODUCT_ENTITY_MASTER_ID = 18;
	public static final Integer PRODUCT_VERSION_ENTITY_MASTER_ID = 19;
	public static final Integer PRODUCT_BUILD_ENTITY_MASTER_ID = 20;
	public static final Integer PRODUCT_FEATURE_ENTITY_MASTER_ID = 23;
	public static final Integer TESTSTEP_ENTITY_MASTER_ID = 25;
	public static final Integer ACTIVITY_ENTITY_MASTER_ID = 28;
	public static final Integer ENTITY_TASK=29;
	public static final Integer CLARIFICATION_ENTITY_MASTER_ID=31;
	public static final Integer ENTITY_ACTIVITY_WORKPACKAGE_ID=34;
	public static final Integer ENTITY_USER_ID=38;
	public static final Integer CHANGE_REQUEST_ENTITY_MASTER_ID=42;
	public static final Integer ENTITY_TASK_TYPE=30;
	public static final Integer RESOURCEDEMAND_ENTITY_MASTER_ID=73;
	public static final Integer REPORTISSUE_ENTITY_MASTER_ID=78;
	public static final Integer ENTITY_WORKFLOW_EVENT_ID = 32;
	public static final Integer ENTITY_ACTIVITY_TYPE=33;
	public static final Integer ENTITY_WORKFLOW_TEMPLATE_ID=45;
	public static final Integer ENTITY_DATA_EXTRACTOR_ID=54;
	public static final Integer ENTITY_TEST_CASE_SCRIPT_ID=78;
	public static final Integer ENTITY_TEST_CASE_EXECUTION_RESULT_ID=79;
	
	public static final String RAG_STATUS_VIEW_ABORT_INDICATOR="darkRed";
	public static final String RAG_STATUS_VIEW_COMPLETED_INDICATOR="darkgreen";
	public static final String RAG_STATUS_VIEW_TOTAL_INDICATOR="blue";
	
	/*Custom Fields Constants*/
	public static final String ENTITY_CUSTOM_FIELD_CONFIG ="CustomFieldConfig";
	public static final String ENTITY_CUSTOM_FIELD_VALUE ="CustomFieldValue";
	
	public static final String[] CUSTOM_FIELDS_DATA_TYPE = {"Text", "Integer", "Decimal", "Boolean", "Date"};
	public static final String[] CUSTOM_FIELDS_CONTROL_TYPE = {"Text Box", "Select", "Radio Button", "Check Box", "Text Area", "Date Picker", "Date Time Picker", "Password"};
	public static final String[] CUSTOM_FIELDS_MANDATORY = {"No", "Yes"};
	public static final String[] CUSTOM_FIELDS_FREQUENCY_TYPE = {"Single", "Series"};
	public static final String[] CUSTOM_FIELDS_FREQUENCY = {"--", "Daily", "Weekly", "Monthly", "Quaterly", "Half-yearly", "Annual"};

	public static final Integer UN_ALLOCATED_RESOURCEPOOL_ID = -100;
	public static final String UN_ALLOCATED_RESOURCEPOOL_NAME = "Un Allocate";
	
	public static final String[] defaultMailId = {"hareem@hcl.com"};
	public static final String defaultName = "Hari";
	
	public static final String NOTIFICATION_ACTIVITY_UPDATION="process.activity.update";
	public static final String NOTIFICATION_ACTIVITY_CREATION="process.activity.add";
	public static final String NOTIFICATION_ACTIVITY_DELETION="process.activity.delete";
	
	public static final String NOTIFICATION_ACTIVITY_WORKPACKAGE_CREATION="process.workrequest.add";
	public static final String NOTIFICATION_ACTIVITY_WORKPACKAGE_UPDATION="process.workrequest.update";
	public static final String NOTIFICATION_ACTIVITY_WORKPACKAGE_DELETION="process.workrequest.delete";
	
	public static final String NOTIFICATION_ACTIVITY_TASK_CREATION="process.activitytask.add";
	public static final String NOTIFICATION_ACTIVITY_TASK_UPDATION="process.activitytask.update";
	public static final String NOTIFICATION_ACTIVITY_TASK_DELETION="process.activitytask.delete";
	
	
	public static final String NOTIFICATION_CLARIFICATION_TRACKER_CREATION="add.clarificationtracker.by.activity";
	public static final String NOTIFICATION_CLARIFICATION_TRACKER_UPDATION="update.clarificationtracker.by.activity";
	public static final String NOTIFICATION_CHANGE_REQUEST_CREATION="changerequests.add.by.entityTypeAndInstanceId";
	public static final String NOTIFICATION_CHANGE_REQUEST_UPDATION="changerequests.update";
	
	
	
	
	public static final String  MOBILE_PLATFORM_VERSION = "MobilePlatformVersion";
	public static final String  MOBILE_AVAILABLE_STATUS = "MobileAvailableStatus";
	public static final String  MOBILE_HOSTLIST = "MobileHostList";	
	public static final String  ENTITY_USER_SKILLS = "UserSkills";
	public static final String  ENTITY_RESOURCE_DEMAND = "ResourceDemand";
	public static final String  ENTITY_WORKSHIFT = "WorkShift";
	public static final String CLONED = "Cloned";
	public static final String ENTITY_PRODUCT_TEAM_RESOURCES = "ProductTeamResources";
	
	public static final String NOTIFICATION_RECIPIENT_ASSIGNEE = "Assignee";
	public static final String NOTIFICATION_RECIPIENT_REVIEWER = "Reviewer";
	public static final String NOTIFICATION_RECIPIENT_OWNER = "Owner";
	public static final String NOTIFICATION_RECIPIENT_TESTMANAGER="TestManager";
	public static final String NOTIFICATION_RECIPIENT_TESTLEAD="TestLead";
	public static final String NOTIFICATION_RECIPIENT_ENGAGEMENTMANAGER="Engagement Manager";
	
	
	
	public static final String SCRIPT_GENERATION = "ScriptGeneration";
	public static final String SCRIPT_GENERATION_SPACE = "Script Generation";
	public static final String SCRIPTLESS_EXECUTION = "Scriptlessexecution";
	
	public static final boolean ENABLE_PENDING_WITH_COLUMN=true;
	public static final String DEMAND_UPLOAD_PERMISSION_FULLY = "FullUpload";
	public static final String DEMAND_UPLOAD_PERMISSION_PARTIALLY ="PartialUpload";
	
	public static final String  ENTITY_OnBoard_USER = "OnboardUser";
	
	public static final String WORKFLOW_STATUS_CATEGORY_COMPLETED="Completed";
	public static final String WORKFLOW_STATUS_CATEGORY_WORKINPROGRESS="WIP";
	public static final String WORKFLOW_STATUS_CATEGORY_PIPELINE="Pipeline";
	
	public static final String MULTIPLE_TEST_SUITE_LEVEL_SCRIPT_PACKS="Test Suite";
	public static final String MULTIPLE_TEST_RUN_PLAN_LEVEL_SCRIPT_PACK="Test Plan";
	public static final String MULTIPLE_RUNCONFIG_LEVEL_SCRIPT_PACK="Test Configuration";
	
	public static final String ENTITY_RELATIONSHIP_TYPE_PREDECESSOR = "Predecessor";
	public static final String ENTITY_RELATIONSHIP_SUBTYPE_FINISH_START = "F-to-S";
	public static final String ENTITY_RELATIONSHIP_SUBTYPE_FINISH_FINISH = "F-to-F";
	public static final String ENTITY_RELATIONSHIP_SUBTYPE_START_START = "S-to-S";
	public static final String ENTITY_RELATIONSHIP_SUBTYPE_START_FINISH = "S-to-F";
	public static final String ENTITY_RELATIONSHIP_TYPE_ASSOCIATION = "Association";

	public static final String ASSIGNEE = "@Assignee";
	public static final String REVIEWER = "@Reviewer";
	
	public static final String TESTCASE_EXECUTION_RESULT_ANALYSIS_OUTCOME_TOBECONFIRMED="To be Confirmed";
	public static final String TESTCASE_EXECUTION_RESULT_ANALYSIS_OUTCOME_FALSE_RESULT="False Result";
	
	public static final String AUTOMATION_MODE_ATTENDED="Attended";
	public static final String AUTOMATION_MODE_UNATTENDED="UnAttended";
	
	public static final String MULTIPLE_TESTSUITES_FOR_EXECUTION = "1";
	public static final String SINGLE_TESTSUITE_FOR_EXECUTION = "2";
	public static final String TARGET_TYPE_HOST = "HOST";
	public static final String TARGET_TYPE_DEVICE = "DEVICE";
	public static final String TARGET_TYPE_STORAGEDRIVE = "STORAGEDRIVE";
	public static final String TEST_SCRIPT_LANGUAGE_JAVA="JAVA_TESTNG";
	public static final String TEST_SCRIPT_LANGUAGE_JAVASCRIPT="JAVASCRIPT";
	public static final String TEST_SCRIPT_LANGUAGE_JSCRIPT="JSCRIPT";
	public static final String TEST_SCRIPT_LANGUAGE_CSHARP="CSHARP";
	public static final String TEST_SCRIPT_LANGUAGE_PYTHON ="PYTHON";
	
	public static final int TEST_TOOL_ID_SELENIUM= 6;	
	public static final int TEST_TOOL_ID_APPIUM = 1;
	public static final int TEST_TOOL_ID_SEETEST = 3;
	public static final int TEST_TOOL_ID_PROTRACTOR = 24;
	public static final int TEST_TOOL_ID_EDAT = 17;
	public static final int TEST_TOOL_ID_CODEDUI = 20;
	public static final int TEST_TOOL_ID_TESTCOMPLETE = 25;
	public static final int TEST_TOOL_ID_RESTASSURED= 26;
	public static final int TEST_TOOL_ID_CUSTOM_CISCO= 28;
	
	public static final String LOCALE="locale";
	public static final String TEST_DATA="testData";
}	

