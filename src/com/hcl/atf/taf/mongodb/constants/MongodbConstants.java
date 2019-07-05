package com.hcl.atf.taf.mongodb.constants;


public class MongodbConstants {
	public static final String APP_NAME = "iLCM";
	
	//TESTFACTORIES
	public static final String 	TESTFACTORY_LAB="testLab";
	public static final String TEST_FACTORY_NAME="testFactoryName";
	public static final String DISPLAY_NAME="displayName";
	public static final String DESC="description";
	public static final String TYPE="testFactory";
	public static final String CITY="city";
	public static final String STATE="state";
	public static final String 	COUNTRY="country";

	//PRODUCTS
	public static final String TESTFACTORY_NAME = "testFactoryName";
	public static final String PRODUCT_NAME = "productName";
	public static final String 	PRODUCT_DESC="description";
	public static final String PROJECT_CODE="projectCode";
	public static final String PROJECT_NAME="projectName";
	public static final String STATUS="status";
	public static final String TYPE_DEVICE="type";
	public static final String STATUS_CHANGE="statusChange";
	public static final String MODE="mode";
	public static final String CUSTOMER="customerName";
	
	//VERSIONS
	public static final String VERSIONS = "releases";
	public static final String RELEASEID = "release";
	
	//Builds
	public static String PRODUCT_BUILD_ID="build";
	public static String PRODUCT_BUILD_VERSION_ID="release";
	public static String BUILDTYPE="build_type";
	public static String WT_DISTRIBUTION="wt_distribution";
	public static String BUILD_DATE="created_date";
	public static String LAST_UPDATED_DATE="last_updated_date";
	public static String UPDATED_BY="updated_by";
	
	//Features
	public static final String FEATURES_MAPPING="feature-mapping";
	
	public static final String PRODUCT_FEATURES_NAME="feature";
	public static final String PARENT_FEATURES_NAME="parent feature";
	public static final String FEATURE_CREATEDDATE="createddate";
	public static final String FEATURE_KEYWORDS="keywords";
	public static final String FEATURES_MODIFIEDDATE="last_updated_date ";
	public static final String FEATURES_UPDATEBY="updated_by";


	//WorkPackage
	public static final String WORKPACKAGE_ID="_id";
	public static final String WORKPACKAGE_NAME="name";
	public static final String WORKPACKAGE_DESCRIPTION="description";
	public static final String WP_PRODUCT_BUILD_ID="productBuildId";
	public static final String WORKPACKAGE_TYPE="workPackageType";
	public static final String WORKPACKAGE_STATUS="status";
	public static final String WORKPACKAGE_CODE="runCode";
	public static final String WORKPACKAGE_ISACTIVE="isActive";
	public static final String WORKFLOWEVENT_ID="workfloweventid";
	public static final String TESTRUNPLAN_ID="testRunPlanId";
	public static final String PLANNEDSTART_DATE="plannedStartDate";
	public static final String PLANNEDEND_DATE="plannedEndDate";
	public static final String CREATED_DATE="createDate";
	public static final String MODIFIED_DATE="modifiedDate";
	
	//TestStep
	public static final String TESTSTEP ="mcafee_testcase_steps";
	public static final String TESTSTEP_ID ="step_id";
	public static final String TESTSTEP_NAME="testStepName";
	public static final String TESTSTEP_TESTCASE_ID="tc_id";
	public static final String TESTSTEP_TESTCASE_DESCRIPTION="tc_desc";
	public static final String TESTSTEP_SEQUENCE="stepseq";	
	public static final String TESTSTEP_DESCRIPTION="step_desc";
	public static final String TESTSTEP_FEATURE_ID="features";
	public static final String TESTSTEP_MODIFIED_DATE="last_updated_date";
	public static final String TESTSTEP_UPDATEDBY_USER_ID="updated_by";

			//TestCases
			public static final String TESTCASE_ID = "_id";
			public static final String TESTCASES_NAME = "title";
			public static final String 	TESTCASES_DESC="description";
			public static final String TESTCASES_ENVIRONMENT="environment";
			public static final String TESTCASES_FEATURE_NAME="featurename";
			public static final String TESTCASES_CREATED_DATE="created date";
			public static final String TESTCASES_priority="priority";
			public static final String TESTCASES_AUTOMATED="automated";
			public static final String  TESTCASES_TYPE="testtype";
			public static final String TESTCASES_LAST_UPDATED_DATE="last_updated_date";
			public static final String TESTCASES_UPDTAEDBY="updated_by";
			public static final String TESTCASES_PRIMARY_FEATURE="primary_feature";
			public static final String TESTCASES_PRIMARY_FEATURE_PARENT="primary_feature_parent";
			public static final String TESTCASES_SECONDARY_FEATURE="secondary_feature";
			public static final String TESTCASES_MAPPED="mapped";

			//Defects
			public static final String DEFECTS_ID = "_id";
			public static final String  DEFECTS_NAME = "title";
			public static final String 	 DEFECTS_DESC="description";
			public static final String DEFECTS_RELEASE="release";
			public static final String DEFECTS_BUILD="build";
			public static final String DEFECTS_REQ_TYPE="req_type";
			public static final String  DEFECTS_PRIORITY="priority";
			public static final String  DEFECTS_SEVERITY="severity";
			public static final String  DEFECTS_DETECTION="detection";
			public static final String  DEFECTS_INJECTION="injection";
			public static final String DEFECTS_INTERNAL_DEFECT="internal defect";
			public static final String DEFECTS__FEATURE="feature";
			public static final String DEFECTS__DATE="date";
			public static final String DEFECTS__KBID="kBid";
			public static final String  DEFECTS__STATUS="status";
			public static final String DEFECTS__UPDATEDBY="updatedBy";
			public static final String DEFECTS__LAST_UPDATED_DATE="lastUpdatedDate";
			public static final String DEFECTS__PRIAMRY_FEATURE="primaryFeature";
			public static final String DEFECTS__PRIMARY_FEATURE_PARENT="primaryFeatureParent";
			public static final String  DEFECTS_SECONDARY_FEATURE="secondary feature";
			public static final String  DEFECTS_MAPPED="mapped";
			
	//Test Executions
	public static final String TESTCASEEXECUTION_ID="_id";	
	public static final String WP_VERSION_ID="release";	
	public static final String WP_BUILD_ID="build";	
	public static final String WP_TC_ID="testcaseid";
	public static final String ENVIRONMENT_COMBINATION_ID="environment";
	public static final String TEST_RESULTS="status";
	public static final String EXECUTION_DATE="date";
	public static final String TCEX_PLANNEDSTART_DATE="planneddate";
	public static final String TESTCASEEXECUTIONRESULT_ID="executionid";
	public static final String WP_MODIFIED_DATE="last_updated_date";
	public static final String TESTER_ID="updated_by";	

	//TestBeds/RunConfiguration
	public static final String TESTBEDS ="testbeds";
	public static final String TB_ID ="_id";
	public static final String TB_ENVIRONMENT_NAME ="env_name";
	public static final String TB_DESCRIPTION="description";	
	public static final String TB_DISTRIBUTION="distribution";	
	public static final String TB_ENVIRONMENT_TYPE="env_type";	
	public static final String TB_CREATEDDATE="created_date";	

	//Testcase collection
	public static final String INDEXING_TESTCASENAME="testcase_collection";	
	public static final String RIVER_TESTCASENAME="tc";	
	public static final String TESTCASES_TABLE = "testcases";

	//product collection
	public static final String INDEXING_PRODUCT="product_collection";	
	public static final String RIVER_PRODUCTNAME="prod";	
	public static final String  PRODUCT_TABLE = "productMaster";

	//productversion collection
	public static final String INDEXING_PRODUCTVERSION="productversion_collection";	
	public static final String RIVER_PRODUCTVERSION="pv";	
	public static final String  PRODUCTVERSION_TABLE = "versions";

	//productbuild collection
	public static final String INDEXING_PROUCTBUILD="productbuild_collection";	
	public static final String RIVER_PRODUCTBUILD="pb";	
	public static final String  PRODUCTBUILD_TABLE = "builds";

	//feature collection
	public static final String INDEXING_FEATURENAME="feature_collection";	
	public static final String RIVER_FEATURENAME="ft";	
	public static final String  FEATURE_TABLE = "feature-mapping";

	//workpakage collection
	public static final String INDEXING_WORPACKAGE="workpackage";	
	public static final String RIVER_WORPACKAGE="wpkg";	
	public static final String  WORPACKAGE_TABLE = "workpackage";

		
		//TestBed collection
	public static final String INDEXING_TESTBEDS="testbed_collection";	
	public static final String RIVER_TESTBEDS="tb";	
	public static final String  TESTBEDS_TABLE = "testbeds";

		
		//results collection
	public static final String INDEXING_RESULTS="result_collection";	
	public static final String RIVER_RESULT="res";	
	public static final String  RESULT_TABLE = "test_executions";

	//defects collection
	public static final String INDEXING_DEFECTS="defects_collection";	
	public static final String RIVER_DEFECTS="def";	
	public static final String  DEFECTS_TABLE = "defects";

	public static final String DEFAULT_STATUS = "{\"status\":\"default\"}";
	public static final String ISE_DEFECTS="defect";
	public static final String ISE_FEATURES="feature";
	
	/* MongoDb Collection Name */
	public static final String TESTCASEEXECUTION ="testexecutions";
	public static final String PRODUCTS = "products";
	public static final String MONGO_VERSIONS = "versions";
	public static String BUILD="builds";
	public static String PRODCTBUILD="productbuilds";
	public static final String TESTCASES = "testcases";	
	public static String TEST_RUN_JOB="testrunjob";
	public static final String TESTFACTORIES = "testfactories";
	public static final String TEST_STEPS ="teststeps";
	public static final String FEATURES_COLLECTION_NAME="features";
	public static final String DEFECTS = "testcasedefects";
	public static final String PRODUCTS_USER_ROLE = "productuserspermission";
	public static final String PRODUCT_TEAM_RESOURCES = "productsteamresources";
	public static final String WORKPACKAGE ="testworkpackages";
	public static final String ACTIVITYTASK="activitytasks";
	public static final String WORK_FLOW_EVENTS = "workflowevents";
	public static final String TEST_SUITE = "testsuites";
	public static final String CLARIFICATION_MONGO ="activityclarificationtracker";
	public static final String ACTIVITY = "activities";
	public static final String ACTIVITYWORKPACKAGE = "activityworkpackages";
	public static final String USER_LIST_MONGO = "userlist";
	public static final String TREND_COLLECTION = "trendcollection";
	public static final String CHANGE_REQUEST_MONGO ="changerequest";
	public static final String WORKPACKAGAE_DEMAND = "workpackagedemandprojection";
	public static final String WORKPACKAGAE_RESOURCE_RESERVATION = "workpackageresourcereservation";
	public static final String CHANGE_REQUEST_MAPPING_MONGO = "changerequestmapping";
	public static final String ISETESTCASEEXECUTION ="test_executions";
	public static final String ISETESTCASES = "testcase_collection";
	public static final String ISE_FEATURES_COLLECTION_NAME="features_mapping";
	public static final String TESTCASE_TO_FEATURES_MAPPING_COLLECTION_NAME="productfeature_testcase_mapping";
	
	public static final String PRODUCT_FEATURE_BUILD_MAPPING_MONGO = "productFeature_productBuild_mapping";
	
	public static final String TESTCASE_PRODUCT_VERSION_MAPPING = "testCase_product_versionMapping";
	
	public static final String TEST_CASE_SCRIPT_MONGO = "testcasescript";
	public static final String TEST_CASE_SCRIPT_MAPPING_MONGO = "testcasescriptmapping";
	
	
	
	/* modified field status */
	public static final String MODIFIED_FIELD_STATUS="status";
	public static final String MODIFIED_FIELD_ISACTIVE="isActive";
	public static final String MODIFIED_FIELD_INACTIVE_STATUS="InActive";
	public static final String MODIFIED_FIELD_ACTIVE_STATUS="Active";
}
