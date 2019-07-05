package com.hcl.atf.taf.constants;

public class AOTCConstants {

	// Entity Types
	public static final String ENTITY_USER_TYPE = "UserType";

	public static final String ENTITY_TEST_CASE = "TestCase";
	public static final String ENTITY_TEST_SUITE = "TestSuite";

	public static final String DOMAINGROUP_TYPE = "GROUP";

	public static final String DOMAINCATEGORY_TYPE = "CATEGORY";

	public static final String ENTITY_TEST_MANAGEMENT_SYSTEM = "TestManagementSystem";
	public static final String DEFAULTDOMAIN = "Common";

	public static final Integer JOBTYPE_UPLOAD = 1;
	public static final Integer JOBTYPE_DOWNLOAD = 3;

	public static final Integer JOBSTATUS_PASSED = 1;
	public static final Integer JOBSTATUS_FAILED = 2;

	public static final int ROLE_ID_ADMIN = 1;
	public static final int ROLE_ID_TEST_LEAD = 4;
	public static final int ROLE_ID_TESTER = 5;
	public static final int ROLE_ID_PROGRAM_MANAGER = 6;
	public static final int ROLE_ID_ALL = 10;

	public static final String PRIORITY_CRITICAL = "CRITICAL";
	public static final String PRIORITY_URGENT = "URGENT";
	public static final String PRIORITY_HIGH = "HIGH";
	public static final String PRIORITY_MAJOR = "MAJOR";
	public static final String PRIORITY_MEDIUM = "MEDIUM";
	public static final String PRIORITY_TRIVIAL = "TRIVIAL";

	public static final String EXECUTION_PRIORITY_CRITICAL = "P0";
	public static final String EXECUTION_PRIORITY_HIGH = "P1";
	public static final String EXECUTION_PRIORITY_MAJOR = "P2";
	public static final String EXECUTION_PRIORITY_MEDIUM = "P3";
	public static final String EXECUTION_PRIORITY_TRIVIAL = "P4";
	public static final String EXECUTION_PRIORITY_PARSE_TOAKEN = "#";
	public static final String PRIORITY_PARSE_TOKEN = ":";

	public static final String ADMIN_LOGIN_ID = "hcl";
	public static final String FILE_NAME_SEPARATOR = "-";

	public static final Integer ENTITY_TEST_CASE_EVIDENCE_ID = 4;
	public static final Integer ENTITY_TEST_STEP_EVIDENCE_ID = 5;
	public static final String EXECUTION_TYPE_AUTO = "AUTOMATIC";
	public static final String EXECUTION_TYPE_MANUAL = "MANUAL";

	public static final String EMAIL_RESRVATION_RESERVED_USER = "reservedUser";
	public static final int USER_ATUHENTICATION_TYPE_ENTERPRISE = 2;

	public static final int USER_ATUHENTICATION_MODE_VENDOR = 1;
	public static final int USER_ATUHENTICATION_MODE_CUSTOMER = 2;

	public static final int ENTITY_STATUS_ACTIVE = 1;

	public static final String DEVICE_ACTIVE = "ACTIVE";
	public static final String DEVICE_INACTIVE = "INACTIVE";

	public static final String TESTCASE_DEFAULT_PRIORITY = "P1";

	public static final Integer EXECUTION_STATUS = 2;
	public static final Integer ISEXECUTED = 1;
	public static final Integer TESTCASE_PRIORITY = 3;
	public static final Integer TESTCASE_EXECUTION = 1;

	public static final Integer TESTCASE_DEFAULT_PRIORITY_ID = 2;
	public static final Integer TESTCASE_EXECUTION_AUTOMATION = 1;
	public static final Integer TESTCASE_TYPE_FUNCTIONAL = 1;
	public static final Integer TESTSUITE_EXECUTION_AUTOMATION = 3;

	public static final String HPQC_TOOL = "HPQC";
	public static final int ENTITY_STATUS_ALL = -1;
	public static final String TESTCASESTEP_DUPLICATE_MESSAGE = "TestCaseStep with this given name, code, already exists in TestCase. Please use another unique combination";
	public static final String TESTCASE_DUPLICATE_MESSAGE = "TestCase with this given name, code, already exists in Product. Please use another unique combination";
	public static final int USER_ATUHENTICATION_TYPE_LOCAL = 1;

	public static final String ENTITY_DOMAIN_GROUP = "DomainGroup";
	public static final String ENTITY_DOMAIN_GROUPID = "domainGroupId";
	public static final String ENTITY_DOMAIN_CATEGORY = "DomainCategory";
	public static final String SEARCH_PARAMETER = "searchParameter";
	public static final String PROJECT_NAME = "etb";
	public static final String TESTCASENAME = "testCaseName";
	public static final String REJECT = "reject";
	public static final String TESTCASEDESCRIPTION = "testCaseDescription";
	public static final String TESTCASEIDETB = "testCaseIdETB";

	public static final String DOMAINCATEGORYNAME = "domainCategoryName";
	public static final String DOMAINCATEGORYDESCRIPTION = "description";
	public static final String DOMAINCATGEORYID = "domainCategoryId";

	public static final String JOB_ADD_TYPE = "Add";
	public static final String JOB_UPDATE_TYPE = "Update";
	public static final String DOCUMENTS_TEMPLATE = "bin//ATSGTemplates";
	public static final String DOMAIN_CATEGORY_TABLE = "domain_category";

	public static final String DC_UPDATED = "DC Updated";
	public static final String DC_HAS_CHILD = "DC has child";
	public static final String DC_HAS_NO_CHILD = "DC has no child";
	public static final String DC_REM_HIERARCHY = "DC removed from Hierarchy";
	public static final String TESTCASE_ATTACHMENTS = "ETB\\TestCasesAttachments";
	public static final String AOTC_INTRODUCTION_TEMPLATE = "bin//ATSG_Intro_Template//ATSG-on-the-Cloud-Intro-v2.pptx";

	public static final String APPLICATION_TYPE_WEB = "Web";
	public static final String APPLICATION_TYPE_MOBILE_SEETEST = "Mobile-SeeTest";
	public static final String APPLICATION_TYPE_MOBILE_APPIUM = "APPIUM";

	public static final String BROWSER_CHROME = "CHROME";
	public static final String BROWSER_FIREFOX = "FIREFOX";
	public static final String BROWSER_IE = "IE";
	public static final String BROWSER_SAFARI = "SAFARI";

	public static final String TDTYPE_TESTDATA = "TestData";
	public static final String TDTYPE_OBJ_REPOSITORY = "ObjectRepository";
	public static final String TDTYPE_SEETEST_REPOSITORY = "seetestRepository";

	public static final String TEST_TOOL_APPIUM = "APPIUM";
	public static final String TEST_TOOL_SEETEST = "SEETEST";
	public static final String TEST_TOOL_AUTOIT = "AUTOIT";
	public static final String TEST_TOOL_SELENIUM = "SELENIUM";
	public static final String TEST_TOOL_ROBOTIUM = "ROBOTIUM";
	public static final String TEST_TOOL_PROTRACTOR = "PROTRACTOR";
	public static final String TEST_TOOL_CODEDUI = "CODEDUI";
	public static final String TEST_TOOL_TESTCOMPLETE = "TESTCOMPLETE";
	public static final String TEST_TOOL_RESTASSURED = "RESTASSURED";	
	public static final String TEST_TOOL_EDAT = "EDAT";
	public static final String TEST_TOOL_CUSTOM_CISCO = "CUSTOM_CISCO";
	
	public static final String ENTITY_PROJECT = "Project";
	public static final Integer ENTITY_PROJECT_ID = 3;

	public static final String STATUS_COMPLETED = "Completed";
	public static final String PRODUCT_WEB = "Web";
	public static final String PRODUCT_MOBILE = "Mobile";
	public static final String PRODUCT_EMBEDDED = "Embedded";
	public static final String PRODUCT_DESKTOP = "Desktop";
	public static final String PRODUCT_DEVICE = "Device";
	
	public static final String ATSG_STORIES_BEING_EDITED = "StoriesBeingEditedCurrently";
	public static final String ATSG_STARTED_EDITING = "StartedEditing";
	public static final String ATSG_FINISHED_EDITING = "FinishedEditing";
	public static final String BROWSER_FIREFOXGECKO = "FIREFOXGECKO";
	public static final String BROWSER_EDGE = "EDGE";
}

