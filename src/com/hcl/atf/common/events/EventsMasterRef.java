package com.hcl.atf.common.events;

public class EventsMasterRef {

	//EventSourceomponent constants
	public static final String SOURCE_COMPONENT_TAF = "Event.Source.TAF"; 
	public static final String SOURCE_COMPONENT_RAF = "Event.Source.RAF";
	public enum EventSource {
		TAF(SOURCE_COMPONENT_TAF), RAF(SOURCE_COMPONENT_RAF);
		
		private String value;
		private EventSource(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}
	
	//Event Category Constants
	public static final String CATEGORY_TEST_STRATEGY = "Event.Category.Strategy";
	public static final String CATEGORY_TEST_PLANNING = "Event.Category.Planning";
	public static final String CATEGORY_TEST_DESIGN = "Event.Category.Design";
	public static final String CATEGORY_TEST_EXECUTION = "Event.Category.Execution";
	public static final String CATEGORY_TEST_REPORTING = "Event.Category.Reporting";
	public static final String CATEGORY_TEST_INFRASTRUCTURE = "Event.Category.Infrastructure";
	public enum EventCategory {
		TEST_STRATEGY(CATEGORY_TEST_STRATEGY),
		TEST_PLANNING(CATEGORY_TEST_PLANNING),
		TEST_DESIGN(CATEGORY_TEST_DESIGN),
		TEST_EXECUTION(CATEGORY_TEST_EXECUTION),
		TEST_REPORTING(CATEGORY_TEST_REPORTING),
		TEST_INFRASTRUCTURE_MANAGEMENT(CATEGORY_TEST_INFRASTRUCTURE);
		
		private String value;
		private EventCategory(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}

	//Event Type Constants
	public static final String TYPE_DEVICE = "Event.Type.Device";
	public static final String TYPE_TERMINAL = "Event.Type.Terminal";
	public static final String TYPE_JOB_EXECUTION = "Event.Type.Job.Execution";
	public static final String TYPE_REPORT_PDF_REPORT = "Event.Type.Report.PDF";
	public static final String TYPE_REPORT_EXCEL_REPORT = "Event.Type.Report.Excel";
	public static final String TYPE_REPORT_HTML_REPORT = "Event.Type.Report.Html";
	public static final String TYPE_REPORT_SCREENSHOT_REPORT = "Event.Type.Report.Screenshot";
	public static final String TYPE_INTEGRATION_TESTMANAGEMENTSYSTEM = "Event.Type.TestManagementSystem";
	public static final String TYPE_INTEGRATION_DEFECTMANAGEMENTSYSTEM = "Event.Type.DefectManagementSystem";
	public enum EventType {
		DEVICE(TYPE_DEVICE),
		TERMINAL(TYPE_TERMINAL),
		JOB_EXECUTION(TYPE_JOB_EXECUTION),
		REPORT_PDF_REPORT(TYPE_REPORT_PDF_REPORT),
		REPORT_EXCEL_REPORT(TYPE_REPORT_EXCEL_REPORT),
		REPORT_HTML_REPORT(TYPE_REPORT_HTML_REPORT),
		REPORT_SCREENSHOT_REPORT(TYPE_REPORT_SCREENSHOT_REPORT),
		INTEGRATION_TESTMANAGEMENTSYSTEM(TYPE_INTEGRATION_TESTMANAGEMENTSYSTEM),
		INTEGRATION_DEFECTMANAGEMENTSYSTEM(TYPE_INTEGRATION_DEFECTMANAGEMENTSYSTEM);
		
		private String value;
		private EventType(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}

	//Entity Type Constants
	public static final String ENTITY_PRODUCT = "Product";
	public static final String ENTITY_PRODUCT_VERSION = "Product.Version";
	public static final String ENTITY_DEVICE = "Device";
	public static final String ENTITY_TESTRUN = "TestRun";
	public static final String ENTITY_TESTRUN_JOB = "TestRun.Job";
	public static final String ENTITY_TERMINAL = "Terminal";
	public static final String ENTITY_TESTSUITE = "TestSuite";
	public static final String ENTITY_TESTMANAGEMENTSYSTEM = "TestManagementSystem";
	public static final String ENTITY_DEFECTMANAGEMENTSYSTEM = "DefectManagementSystem";
	public enum EntityType {
		PRODUCT(ENTITY_PRODUCT),
		PRODUCT_VERSION(ENTITY_PRODUCT_VERSION),
		DEVICE(ENTITY_DEVICE),
		TESTRUN(ENTITY_TESTRUN),
		TESTRUN_JOB(ENTITY_TESTRUN_JOB),
		TERMINAL(ENTITY_TERMINAL),
		TESTSUITE(ENTITY_TESTSUITE),
		TESTMANAGEMENTSYSTEM(ENTITY_TESTMANAGEMENTSYSTEM),
		DEFECTMANAGEMENTSYSTEM(ENTITY_DEFECTMANAGEMENTSYSTEM);
		
		private String value;
		private EntityType(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}

	//Test Infrastructure Management Events : Device Events
	public static final String DEVICE_NEW_REGISTERED = "Device.New.Registered";
	public static final String DEVICE_CONNECTED = "Device.Connected";
	public static final String DEVICE_DISCONNECTED = "Device.Disconnected";
	public static final String DEVICE_DEREGISTERED = "Device.Deregistered";
	public static final String DEVICE_TERMINAL_CHANGED = "Device.Terminal.Changed";
	public static final String DEVICE_USED = "Device.Used"; //Used for testing by a Job
	public enum DeviceEvents {
		NEW_REGISTERED(DEVICE_NEW_REGISTERED),
		CONNECTED(DEVICE_CONNECTED),
		DISCONNECTED(DEVICE_DISCONNECTED),
		DEREGISTERED(DEVICE_DEREGISTERED),
		TERMINAL_CHANGED(DEVICE_TERMINAL_CHANGED),
		USED(DEVICE_USED);
		
		private String value;
		private DeviceEvents(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}
	//Test Infrastructure Management Events : Terminal Events
	public static final String TERMINAL_NEW_REGISTERED = "Terminal.New.Registered";
	public static final String TERMINAL_CONNECTED = "Terminal.Connected";
	public static final String TERMINAL_DISCONNECTED = "Terminal.Disconnected";
	public static final String TERMINAL_DEREGISTERED = "Terminal.Deregistered";
	public enum TerminalEvents {
		NEW_REGISTERED(TERMINAL_NEW_REGISTERED),
		CONNECTED(TERMINAL_CONNECTED),
		DISCONNECTED(TERMINAL_DISCONNECTED),
		DEREGISTERED(TERMINAL_DEREGISTERED);
		
		private String value;
		private TerminalEvents(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}

	//Test Planning Events
	public static final String PRODUCT_ADDED = "Product.Added";
	public static final String PRODUCT_VERSION_ADDED = "Product.Version.Added";
	public static final String PRODUCT_BUILD_ADDED = "Product.Build.Added";
	public static final String TESTSUITE_ADDED = "TestSuite.Added";
	public static final String DEFECT_SYSTEM_ADDED = "Defect.System.Added";
	public static final String DEFECTS_IDENTIFIED = "Defects.Identified";
	public static final String TESTMANAGEMENT_SYSTEM_ADDED = "TestManagement.System.Added";
	public static final String TESTMANAGEMENT_TESTCASES_IMPORTED = "TestManagementSystem.TestCases.Imported";
	public static final String TESTMANAGEMENT_TESTSUITES_IMPORTED = "TestManagementSystem.TestSuites.Imported";
	public static final String TEST_ENVIRONMENT_ADDED = "Test.Environment.Added";
	public static final String TEST_ENVIRONMENT_MODIFIED = "Test.Environment.Modified";
	public static final String TEST_ENVIRONMENT_DELETED = "Test.Environment.Deleted";
	public enum PlanningEvents {
		PRODUCT_ADDED_EVENT(PRODUCT_ADDED),
		PRODUCT_VERSION_ADDED_EVENT(PRODUCT_VERSION_ADDED),
		PRODUCT_BUILD_ADDED_EVENT(PRODUCT_BUILD_ADDED),
		TESTSUITE_ADDED_EVENT(TESTSUITE_ADDED),
		DEFECT_SYSTEM_ADDED_EVENT(DEFECT_SYSTEM_ADDED),
		DEFECTS_IDENTIFIED_EVENT(DEFECTS_IDENTIFIED),
		TESTMANAGEMENT_SYSTEM_ADDED_EVENT(TESTMANAGEMENT_SYSTEM_ADDED),
		TESTMANAGEMENT_TESTCASES_IMPORTED_EVENT(TESTMANAGEMENT_TESTCASES_IMPORTED),
		TESTMANAGEMENT_TESTSUITES_IMPORTED_EVENT(TESTMANAGEMENT_TESTSUITES_IMPORTED),
		TEST_ENVIRONMENT_ADDED_EVENT(TEST_ENVIRONMENT_ADDED),
		TEST_ENVIRONMENT_MODIFIED_EVENT(TEST_ENVIRONMENT_MODIFIED),
		TEST_ENVIRONMENT_DELETED_EVENT(TEST_ENVIRONMENT_DELETED);
		
		
		private String value;
		private PlanningEvents(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}

	//Test Design Events
	public static final String TESTCASE_SCRIPTS_GENERATED = "Testcase.Scripts.Generated";
	public enum TestDesignEvents {
		TESTCASE_SCRIPTS_GENERATED_EVENT(TESTCASE_SCRIPTS_GENERATED);
		
		private String value;
		private TestDesignEvents(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}

	//Test Execution Events
	public static final String TESTRUN_CONFIGURATION_EXECUTED = "TestRun.Configuration.Executed";
	public static final String TESTRUN_CONFIGURATION_ABORTED = "TestRun.Configuration.Aborted";
	public static final String TESTRUN_JOB_EXECUTED = "TestRun.Configuration.Job.Executed";
	public static final String TESTRUN_JOB_ABORTED = "TestRun.Configuration.Job.Aborted";
	public enum TestExecutionEvents {
		TESTRUN_CONFIGURATION_EXECUTED_EVENT(TESTRUN_CONFIGURATION_EXECUTED),
		TESTRUN_CONFIGURATION_ABORTED_EVENT(TESTRUN_CONFIGURATION_ABORTED),
		TESTRUN_JOB_EXECUTED_EVENT(TESTRUN_JOB_EXECUTED),
		TESTRUN_JOB_ABORTED_EVENT(TESTRUN_JOB_ABORTED);
		
		private String value;
		private TestExecutionEvents(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}

	//Test Reporting & Analysis Events
	public static final String TESTRUN_REPORT_GENERATED = "TestRun.Report.Generated";
	public static final String TESTRUN_JOB_REPORT_GENERATED = "TestRun.Job.Report.Generated";
	public static final String TESTRUN_TESTRESULTS_EXPORTED = "TestManagementSystem.TestResults.Exported";
	public static final String REPORT_EMAILED = "TestRun.Report.Emailed";
	public static final String TESTRUN_DEFECTS_REPORTED = "TestRun.Defects.Reported";
	public enum TestReportingEvents {
		TESTRUN_REPORT_GENERATED_EVENT(TESTRUN_REPORT_GENERATED),
		TESTRUN_JOB_REPORT_GENERATED_EVENT(TESTRUN_JOB_REPORT_GENERATED),
		REPORT_EMAILED_EVENT(REPORT_EMAILED),
		TESTRUN_DEFECTS_REPORTED_EVENT(TESTRUN_DEFECTS_REPORTED),
		TESTRUN_TESTRESULTS_EXPORTED_EVENT(TESTRUN_TESTRESULTS_EXPORTED);
		
		private String value;
		private TestReportingEvents(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}


	//Benefits
	public static final String BENEFIT_TEST_SCRIPT_GENERATION = "Benefit.Testscript.Generation";
	public enum Benefits {
		TEST_SCRIPT_GENERATION(BENEFIT_TEST_SCRIPT_GENERATION);
		
		private String value;
		private Benefits(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}

}
