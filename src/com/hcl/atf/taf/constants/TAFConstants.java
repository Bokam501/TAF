package com.hcl.atf.taf.constants;

public class TAFConstants {

	public static final String PRODUCT_DUPLICATE_MESSAGE = "product with this given name already exists. Please use another unique name";
	public static final String PRODUCT_VERSION_DUPLICATE_MESSAGE = "Product Version already Exists.";
	public static final String PRODUCT_BUILD_DUPLICATE_MESSAGE = "Product Build already Exists.";
	public static final String WORKPACKAGE_DUPLICATE_MESSAGE = "WorkPackage already Exists.";
	public static final String PRODUCT_ENVIRONMENT_DUPLICATE_MESSAGE = "Product Environment already Exists.";
	public static final String PRODUCT_DECOUPLINGCATEGORY_DUPLICATE_MESSAGE = "Product Decoupling Category already Exists for the product.";
	public static final String PRODUCT_FEATURE_DUPLICATE_MESSAGE = "Product Feature already Exists for the product.";
	public static final String TESTSUITE_DUPLICATE_MESSAGE = "TestSuite already Exists for the Product Version.";
	public static final String TESTCONFIGURATIONCHILD_DUPLICATE_MESSAGE = "-Test Run Configuration Child with this given name already exists. Please use another unique name.";
	public static final String CUSTOMER_DUPLICATE_MESSAGE = "Customer with this given name already exists. Please use another unique name";
	public static final String SKILL_DUPLICATE_MESSAGE = "Skill with this given name already exists. Please use another unique name";
	public static final String VENDOR_DUPLICATE_MESSAGE = "Vendor with this given name already exists. Please use another unique name";
	public static final String USER_SKILL_DUPLICATE_MESSAGE = "User Skill with this given name already exists. Please use another unique name";
	public static final String TESTCASE_DUPLICATE_MESSAGE = "TestCase with this given name, code, already exists in Product. Please use another unique combination";
	public static final String TESTCASESTEP_DUPLICATE_MESSAGE = "TestCaseStep with this given name, code, already exists in TestCase. Please use another unique combination";
	public static final String TESTFACTORY_DUPLICATE_MESSAGE = " Engagement with this given name already Exists. Please use another unique combination";
	public static final String DIMENSION_DUPLICATE_MESSAGE = "Dimension with this given name and type already exists. Please use another unique name or different type";
	public static final String COMPETENCY_MEMBER_DUPLICATE_MESSAGE = "User is already mapped with competency. Please select another user";
	public static final String DIMENSION_PRODUCT_DUPLICATE_MESSAGE = "Product is already mapped with dimension. Please select another product";
	public static final String DATA_EXTRACTOR_DUPLICATE_MESSAGE = "Data extractor already configured for the given details";
	public static final String PRIMARY_STATUS_DUPLICATE_MESSAGE = "Primary status is already available for this status group. Please use another unique primary status.";
	public static final String SECONDARY_STATUS_DUPLICATE_MESSAGE = "Secondary status is already available for this status group. Please use another unique seocndary status.";
	public static final String SECONDARY_STATUS_ALREADY_MAPPED_MESSAGE = "Secondary status is already mapped for this primary status. Please use another seocndary status.";
	
	//Entity Status
	public static final int ENTITY_STATUS_ACTIVE = 1;
	public static final int ENTITY_STATUS_INACTIVE = 0;
	public static final int ENTITY_STATUS_ALL = -1;
	//Entity Types
	public static final String  ENTITY_PRODUCT = "Product";
	public static final String  ENTITY_PRODUCT_VERSION = "Product Version";
	public static final String  ENTITY_TEST_SUITE = "Test Suite";
	public static final String  ENTITY_TEST_RUN_CONFIGURATION_CHILD = "Test Run Configuration";
	public static final String  ENTITY_TEST_ENVIRONMENT = "Test Environment";
	
	//Testcase Types
	public static final String TESTCASE_AUTOMATED = "Automated Testcase";
	public static final String TESTCASE_MANUAL = "Manual Testcase";
	public static final String TESTCASE_MIXED = "Mixed Testcase";
	public static final String[] TESTCASE_TYPES = {TESTCASE_AUTOMATED, TESTCASE_MANUAL, TESTCASE_MIXED};
	
	public static final String TESTCASE_FUNCTIONAL = "Functional Testing";
	public static final String TESTCASE_REGRESSION = "Regression Testing";
	public static final String TESTCASE_CHANGEBASED = "Change Based Testing";
	
	//Testcase Source
	public static final String TESTCASE_SOURCE_TAF = "TAF Terminal";
	public static final String TESTCASE_SOURCE_HPQC = "HPQC";
	public static final String TESTCASE_SOURCE_QTP = "QTP";
	public static final String TESTCASE__SOURCE_TFS = "TFS";
	public static final String TESTCASE__SOURCE_DIRECTUI = "UI";
	public static final String[] TESTCASE_SOURCES = {TESTCASE_SOURCE_TAF, TESTCASE_SOURCE_HPQC, TESTCASE_SOURCE_QTP, TESTCASE__SOURCE_TFS, TESTCASE__SOURCE_DIRECTUI};

	//Defect Management System
	public static final String DEFECT_MANAGEMENT_SYSTEM_JIRA = "JIRA";
	public static final String DEFECT_MANAGEMENT_SYSTEM_JIRA_VERSION_6 = "6.2.x";
	
	//Test Management System
	public static final String TEST_MANAGEMENT_SYSTEM_HPQC = "HPQC";
	public static final String TEST_MANAGEMENT_SYSTEM_TFS = "TFS";
	public static final String TEST_MANAGEMENT_SYSTEM_HPQC_VERSION_11 = "11";
	
	//Testscript Types
	public static final String TESTSCRIPT_TYPE_JUNIT = "JUNIT";
	public static final String TESTSCRIPT_TYPE_TESTNG = "TESTNG";
	public static final int TESTSCRIPT_TYPE_JUNIT_NUMBER = 1;
	public static final int TESTSCRIPT_TYPE_TESTNG_NUMBER = 2;
	public static final int TESTSCRIPT_TYPE_PYTHON_NUMBER = 3;
	
	//Test Execution Engines
	public static final String TESTENGINE_SEETEST = "SEETEST";
	public static final String TESTENGINE_SELENIUM = "SELENIUM";
	public static final String TESTENGINE_SELENDROID = "SELENDROID";
	public static final String TESTENGINE_APPIUM = "APPIUM";
	public static final String TESTENGINE_ROBOTIUM = "ROBOTIUM";
	public static final String TESTENGINE_UIAUTOMATION = "UIAUTOMATION";

	//Evidence Module constants
	public static final String EVIDENCE_UNZIPPED = "EVIDENCE_UNZIPPED";
	public static final String EVIDENCE_SCREENSHOT = "SCREENSHOT";
	public static final String EVIDENCE_LOG = "LOG";
	public static final String EVIDENCE_VIDEO = "VIDEO";
	public static final String BUG_IMAGES_TO_TMS = "BUG_IMAGES_TO_TMS";
	
	//Evidence Module - ServiceConnector 'Evidence Pack' Push Outcome from Server
	public static final String EVIDENCE_PACK_SERVER_PUSH_SUCCESSFUL="EVIDENCE_PACK_SERVER_PUSH_SUCCESSFUL"; 
	public static final String EVIDENCE_PACK_NOT_A_VALID_ARCHIVE="EVIDENCE_PACK_NOT_A_VALID_ARCHIVE";
	public static final String EVIDENCE_PACK_PROBLEM_WHILE_SAVING_AT_SERVER="EVIDENCE_PACK_PROBLEM_WHILE_SAVING_AT_SERVER";
	public static final String EVIDENCE_PACK_SENDREQUEST_HAS_ISSUES="EVIDENCE_PACK_SENDREQUEST_HAS_ISSUES";
	
	//Evidence Module - Server receive status - 'DB update processing constants'
	public static final String EVIDENCE_STATUS_UPDATED="EVIDENCE_STATUS_UPDATED";
	public static final String EVIDENCE_STATUS_NOT_UPDATED="EVIDENCE_STATUS_NOT_UPDATED";

	//Evidence Module - 'Server receive status'
	public static final String EVIDENCE_PACK_RECEIVED_AT_SERVER="EVIDENCE_PACK_RECEIVED_AT_SERVER";
	public static final String EVIDENCE_PACK_NOT_RECEIVED_AT_SERVER="EVIDENCE_PACK_NOT_RECEIVED_AT_SERVER";  // Default value
	//public static final String EVIDENCE_PACK_FOLDER_NOT_AVAILABLE_AT_TERMINAL="EVIDENCE_PACK_FOLDER_NOT_AVAILABLE_AT_TERMINAL";
	
	//TAF Script Sources
	//Added changes for Test script source as TestResources. 
	public static final String TEST_SCRIPT_SOURCE_HPQC = "HPQC";
	public static final String TEST_SCRIPT_SOURCE_HPQC_TEST_PLAN = "HPQC_TEST_PLAN";
	public static final String TEST_SCRIPT_SOURCE_HPQC_TEST_RESOURCES = "HPQC_TEST_RESOURCES";
	public static final String TEST_SCRIPT_SOURCE_TAF = "TAF";
	public static final String TEST_SCRIPT_SOURCE_JENKINS = "JENKINS";
	public static final String TEST_SCRIPT_SOURCE_HUDSON = "HUDSON";
	
	//TAF constansts for TestManagement system inputs for Test case data
	public static final String TESTCASE_QUALIFIED_SCRIPT_NAME = "TAF_TC_Qualified_Name";
	public static final String TESTCASE_SCRIPT_FILE_NAME =  "TC_FileName";
	public static final String TESTCASE_PRIORITY =  "TC_Priority";
	public static final String TAF_DATA = "#TAF#";
	public static final String TAF_PARSE_TOKEN = "#";
	
	public static final String ROLE_TESTER = "5";
	public static final String ROLE_TESTLEAD = "4";
	public static final String ROLE_TESTMANAGER = "3";
	public static final String ROLE_ALL = "All";
	
	public static final String ADD = "Add";
	public static final String REMOVE = "Remove";
	
}
