package com.hcl.atf.taf.integration.data.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.ReadableByteChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ActivityTypeDAO;
import com.hcl.atf.taf.dao.CommonDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.TestCasePriorityDAO;
import com.hcl.atf.taf.dao.UserSkillsDAO;
import com.hcl.atf.taf.integration.data.TestDataIntegrator;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityMaster;
import com.hcl.atf.taf.model.ActivityResult;
import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityTaskType;
import com.hcl.atf.taf.model.ActivityType;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.DefectTypeMaster;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.EntityConfigurationProperties;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.SkillLevels;
import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestDataItemValues;
import com.hcl.atf.taf.model.TestDataItems;
import com.hcl.atf.taf.model.TestDataPlan;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestcaseTypeMaster;
import com.hcl.atf.taf.model.UIObjectItems;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserSkills;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkflowStatusCategory;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseList;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseStepList;
import com.hcl.atf.taf.model.dto.ActivitiesDTO;
import com.hcl.atf.taf.model.dto.ActivityTasksDTO;
import com.hcl.atf.taf.model.dto.ChangeRequestDTO;
import com.hcl.atf.taf.model.dto.ProductFeatureListDTO;
import com.hcl.atf.taf.model.dto.TestCaseListDTO;
import com.hcl.atf.taf.model.dto.TestStepDTO;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ActivityResultService;
import com.hcl.atf.taf.service.ActivitySecondaryStatusMasterService;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.ActivityStatusService;
import com.hcl.atf.taf.service.ActivityTaskService;
import com.hcl.atf.taf.service.ActivityTypeService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.ChangeRequestService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.DefectUploadService;
import com.hcl.atf.taf.service.DeviceListService;
import com.hcl.atf.taf.service.DimensionService;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.EntityConfigurationPropertiesService;
import com.hcl.atf.taf.service.EntityRelationshipService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.SkillService;
import com.hcl.atf.taf.service.StatusService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestExecutionBugsService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.TestFactoryManagementService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
import com.hcl.atf.taf.service.TestRunReportsDeviceCaseListService;
import com.hcl.atf.taf.service.TestRunReportsDeviceCaseStepListService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.atf.taf.util.ZipTool;
import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.WorkflowStatusActor;
import com.hcl.ilcm.workflow.model.WorkflowStatusPolicy;
import com.hcl.ilcm.workflow.service.WorkflowMasterService;
import com.hcl.ilcm.workflow.service.WorkflowStatusCategoryService;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;
import com.hcl.ilcm.workflow.service.WorkflowStatusService;
import com.hcl.jira.rest.JiraConnector;

public class ExcelTestDataIntegrator implements TestDataIntegrator {

	@Autowired
	public TestSuiteConfigurationService testSuiteConfigurationService;
	@Autowired
	public ProductListService productListService;
	@Autowired
	TestExecutionBugsService testExecutionBugsService;
	@Autowired
	TestExecutionService testExecutionService;
	@Autowired
	TestCaseService testCaseService;
	@Autowired
	DeviceListService deviceListService;
	@Autowired
	TestRunConfigurationService testRunConfigservice;
	@Autowired
	UserListService userListService;
	@Autowired 
	TestRunReportsDeviceCaseStepListService testRunReportsDeviceCaseStepListService;
	@Autowired
	EnvironmentService environmentService;
	@Autowired
	TestRunReportsDeviceCaseListService testRunReportsDeviceCaseListService;
	@Autowired
	private DefectUploadService defectUploadService;
	@Autowired
	private  EntityConfigurationPropertiesService entityConfigurationPropertiesService;
	@Autowired
	WorkPackageService workPackageService;
	@Autowired
	private TestFactoryManagementService testFactoryManagementService;
	
	ProductFeature productFeature;
	ProductMaster productMaster;
	ProductBuild productBuild;
	Activity taskActivity;
	
	@Autowired
	private ActivityWorkPackageService activityWorkPackageService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActivityTypeService activityTypeService;
	@Autowired
	private WorkflowStatusService workflowStatusService;
	@Autowired
	private WorkflowStatusPolicyService workflowStatusPolicyService;
	@Autowired
	private ChangeRequestService changeRequestService;
	@Autowired
	private DimensionService dimensionService;	
	
	@Autowired
	private MongoDBService mongoDBService;		
	@Autowired
	private ActivityTaskService activityTaskService;	
	@Autowired
	private ActivitySecondaryStatusMasterService activitySecondaryStatusMasterService;	
	@Autowired
	private ActivityResultService activityResultService;	
	@Autowired
	private ExecutionTypeMasterService executionTypeMasterService = null;	
	@Autowired
	private StatusService statusService;	
	@Autowired
	private EmailService emailService;	
	@Autowired
	private ActivityTypeDAO activityTypeDAO;
	
	@Autowired
	private ActivityStatusService activityStatusService; 
	
	@Autowired
	private EntityRelationshipService entityRelationshipService;
	
	@Autowired
	private WorkflowMasterService workFlowMasterService;
	
	@Autowired
	private WorkflowStatusCategoryService workflowStatusCategoryService;
	
	@Autowired
	private CommonDAO commonDao;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ProductMasterDAO productMasterDao;
	
	@Autowired
	private SkillService skillService;
	
	@Autowired
	private UserSkillsDAO userSkillsDAO;
	
	@Autowired
	WorkflowStatusPolicyService	workflowStatusPoliciesService;
	
	@Autowired
	EventsService eventsService;
	
	@Autowired
	TestCasePriorityDAO testCasePriorityDAO;
	
	@Value("#{ilcmProps['EMAIL_ALERT_NOTIFICATION']}")
    private String emailNotification;
	
	@Value("#{ilcmProps['TEST_STEP_BATCH_PROCESSING_COUNT']}")
    private String userSkillMaxBatchCount;
	
	@Value("#{ilcmProps['REPORT_FILE_PATH']}")
	private String reportFilePath;

	public static final int testCaseNameindex = 0;
	public static final int testCaseIDIndex = 1;
	public static final int testCaseDescIndex = 2;
	public static final int testCaseCreatedDateindex = 3;
	public static final int testCaseLastUpdatedDateIndex = 4;
	public static final int testCasePreconditionIndex = 5;
	public static final int testInputTestDataIndex = 6;
	public static final int testStepOrProcedureIndex = 7;
	public static final int testCaseExpectedOutputIndex = 7;
	public static final int testCasePriorityIndex = 8;
	public static final int testCaseTypeindex = 9;
	public static final int productFeatureCodeIndex = 10;
	public static final int executiontypeIndex = 11;
	public static final int testCaseSourceIndex = 12;
	public static final int testcaseScriptPkgNameIndex = 14;
	public static final int testcaseScriptFileName = 15;
	
	public static final int testStepIDIndex = 0;
	public static final int testStepCodeindex = 1;
	public static final int testStepNameindex = 2;
	public static final int testStepDescIndex = 3;
	public static final int testStepCreatedDateindex = 4;
	public static final int testStepLastUpdatedDateIndex = 5;
	public static final int testStepInputTestDataIndex = 6;
	public static final int testStepExpectedOutputIndex = 7;
	public static final int testStepSourceIndex = 8;
	public static final int testStepTestCaseIdIndex = 9;
	public static final int testStepTestCaseCodeIndex = 10;
	
	public static final int productFeatureIDIndex = 0;
	public static final int productFeatureCodeindex = 1;
	public static final int productFeatureNameindex = 2;
	public static final int productFeatureDescIndex = 3;
	public static final int productFeatureCreatedDateindex = 4;
	public static final int productFeatureLastUpdatedDateIndex = 5;
	public static final int parentFeatureIdIndex = 6;
	public static final int parentFeatureCodeIndex = 7;
	public static final int productFeaturePriority=8;
	

	public static final int defectIssueIdindex = 0;
	public static final int defectTypeindex = 1;
	public static final int defectCreatedDateindex = 2;
	public static final int defectReportedByindex = 3;
	public static final int defectModifiedDateindex = 4;
	public static final int defectPriorityindex = 5;
	public static final int defectTitleindex = 6;
	public static final int defectStatusindex = 7;
	public static final int defectAssigneeindex = 8;
	public static final int defectVerfiedByindex = 9;
	

	public String productFeatureName = "";
	public static final int testCaseStartRow = 0;
	public static final int testStepsStartRow = 0;
	public static final int productFeatureStartRow = 0;
	public static final int defectBugnizerStartRow = 0;
	public boolean status = false;
	public static final int maxBlankRowCount = 3;
	public static final int activitiesStartRow = 0;
	public static final int taskStartRow = 0;
	static final String DATA_ADD = "ADD";
	private static final String DATA_UPDATE = "UPDATE";
	private static final String DATA_ERROR = "ERROR";
	
	public static final int productNameIndex = 0;
	public static final int versionNameIndex = 1;
	public static final int buildNameIndex = 2;
	public static final int workpackageNameIndex = 3;
	public static final int activityNameindex = 4;
	public static final int srsSectionIndex = 5;
	public static final int activityMasterIndex = 6;
	public static final int activityTrackerIndex = 7;
	public static final int categoryIndex = 8;
	public static final int assigneeIndex = 9;
	public static final int reviewerIndex = 10;
	public static final int activityPriorityIndex = 11;
	public static final int activityComplexity = 12;
	public static final int remarksIndex = 13;
	public static final int plannedStartDateIndex = 14;
	public static final int plannedEndDateIndex = 15;
	public static final int activityPlanSize = 16;
	public static final int activityStatusIndex = 17;//Current status
	
	private Integer changeRequestIDIndex=0;
	private Integer changeRequestNameindex=1;
	private Integer changeRequestDescriptionIndex=2;
	private Integer changeRequestRaisedDateIndex=3;
	private Integer changeRequestPriorityIDIndex=4;
	private Integer changeRequestOwnerIndex=5;
	private Integer changeRequestPlanedValueIndex = 6;
	private Integer changeRequestStatusIndex=7;

	//Activity Task Import
	
	public static final int taskNameindex = 1;
	public static final int activityTaskTypeindex = 2;
	public static final int taskCategoryIndex = 3;
	public static final int environmentCombinationIndex = 4;
	public static final int taskAssigneeIndex = 5;
	public static final int taskReviewerIndex = 6;
	public static final int taskPrimaryStatusIndex = 7;
	public static final int taskSecondaryStatusIndex = 8;
	public static final int taskResultIndex =9;
	public static final int taskPlannedStartDateIndex= 10;
	public static final int taskPlannedEndDateIndex = 11;
	public static final int taskPlannedSizeIndex = 12;
	public static final int taskactualSizeIndex = 13;
	
	//Workflow Template Status
	
	public static final Integer workFlowTemplateNameIndex = 0;
	public static final Integer statusNameIndex = 1;
	public static final Integer statusDisplayNameIndex = 2;
	public static final Integer descriptionIndex = 3; 
	public static final Integer workflowStatusCategoryIndex = 4;
	public static final Integer WeightageIndex =5;
	public static final Integer slaDurationinHrsIndex =6;
	public static final Integer slaViolationActionIndex = 7;
	public static final Integer statusOrderIndex = 8;
	public static final Integer workflowStatusTypeIndex = 9;
	public static final Integer statusTransitionPolicyIndex = 10;
	public static final Integer actorIndex = 11;
	
	//Entity workflow mapping status policy
	
	public static final Integer entityTypeIndex = 0;
	public static final Integer entityIndex = 1;
	public static final Integer workFlowTemplateIndex = 2;
	public static final Integer entityStatusNameIndex = 3;
	public static final Integer UserIndex = 4;
	public static final Integer RolesIndex = 5;
	public static final Integer actorMappingTypeIndex = 6;
	public static final Integer actionRequirementIndex = 7;
	
	
	//Skill Profile
	
	public static final Integer skillUploadUserNameIndex = 0;
	public static final Integer skillUploadUserCodeIndex = 1;
	public static final Integer skillUploadLoginIDIndex = 2;
	public static final Integer skillUploadNameIndex = 3;
	public static final Integer skillUploadLevelIndex = 4;
	public static final Integer skillUploadIsPrimarySkillIndex = 5;
	public static final Integer skillUploadApproverIndex = 6;
	public static final Integer skillUploadFromDateIndex = 7;
	public static final Integer skillUploadToDateIndex = 8;

	
	// Naming convention for TestCases
	private static final String TC_NAMING_CONV = "TAF_ETC_";

	// Naming convention for Defects
	private static final String DEFECTS_NAMING_CONV = "TAF_DEFECTS_";

	// Naming convention for TestCases
	private static final String TC_NAMING_CONV_WORKPACKAGE = "TFWF_WPTCEP";
	
	//Added for TAF JNJ Features integration to iLCM
	//Naming convention for TestExecution Results
	private static final String TER_NAMING_CONV = "iLCM_TER_";
	//Changes for Localization Reports
	private static final String TER_NAMING_CONV_LOCALIZATION = "iLCM_TER_LOCALE_";
	//Changes for Features Reports
	private static final String TER_NAMING_CONV_FEATURES = "iLCM_TER_FEATURES_";
		
	
	@Value("#{ilcmProps['TEST_STEP_BATCH_PROCESSING_COUNT_FOR_READER']}")
    private String testStepReaderLimit;

	@Value("#{ilcmProps['DEFECT_BUGNIZER_DATA_IMPORT_SIZE_FOR_READER']}")
    private String defectBugnizerSize;
	
	@Value("#{ilcmProps['ACTIVITIES_BATCH_PROCESSING_COUNT_FOR_READER']}")
    private String activitiesReaderLimit;

	// Priorities
	private static final int PRIORITY_CRITICAL = 1;
	private static final int PRIORITY_HIGH = 2;
	private static final int PRIORITY_MEDIUM = 3;
	private static final int PRIORITY_TRIVIAL = 4;

	private static final int DIMENTIONTYPEID=2;

	private static final Log log = LogFactory
			.getLog(ExcelTestDataIntegrator.class);
	
	DataFormatter formatter = new DataFormatter();
	
	private static String specialChars = "[!@#$%^&*()+~:?;{}]";	

	@Override
	public boolean importTestCases(String excelDataFile, int productId,
			String testCaseSource) {

		productMaster = productListService.getProductById(productId);
		List<TestCaseList> testCases = readTestCasesFromExcelFile(
				excelDataFile, productMaster, testCaseSource);
		validateImportTestCases(testCases);
		return true;
	}

	private List<TestCaseList> readTestCasesFromExcelFile(String excelDataFile,
			ProductMaster productMaster, String testCaseSource) {

		int blankRowCount = 0;
		URL url = null;
		System.out
				.println("Import - Excel file from location:" + excelDataFile);
		List<TestCaseList> testCases = new ArrayList<TestCaseList>();
		List<Integer> errorRows = new ArrayList<Integer>();
		TestCaseList testCase = new TestCaseList();

		int numberOfSheets = 0;
		FileInputStream fis;
		String dataValidStatus = "";
		try { // 1

			// Get the Excel file
			fis = new FileInputStream(excelDataFile);
			Workbook workbook = null;
			if (excelDataFile.endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			}
			if (excelDataFile.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fis);
			}

			// Get the worksheet containing the testcases. It will be named
			// 'TestCases'.
			// If not available, get the first worksheet.
			numberOfSheets = workbook.getNumberOfSheets();

			Sheet sheet = null;
			for (int i = 0; i < numberOfSheets; i++) {
				if (workbook.getSheetName(i).equalsIgnoreCase("TestCases")) {
					sheet = workbook.getSheetAt(i);
					break;
				}
			}
			if (sheet == null) {
				sheet = workbook.getSheetAt(0);
			}

			// Check if the sheet contains rows of testcases
			int rowCount = sheet.getPhysicalNumberOfRows();
			
			int colCount = 0;
			int rowNum = 0;
			int colNum = 0;
			if (rowCount < 1) {
				log.info("No rows present in the test cases worksheet");
				return null;
			} else { // 2
				for (rowNum = 0; rowNum < rowCount; rowNum++) {
					Row row;
					blankRowCount = 0;
					
					row = sheet.getRow(rowNum);
					if (row == null) {
						
						rowNum++;
						continue;
					}
					if (row.getRowNum() <= testCaseStartRow) {
						
						colCount = row.getLastCellNum();
						rowNum++;
						continue;
					} else { // 4

						// Read the testcase info
						testCase = new TestCaseList();

						boolean testCaseNameIsMissing = false;
						boolean testCaseIDIsMissing = false;

						// TestCase name is mandatory. If not present skip row
						Cell cell1 = row.getCell(testCaseNameindex);
						if (isCellValid(cell1)) {
							testCase.setTestCaseName(cell1.getStringCellValue());
							System.out.println("Test Case Name: "
									+ cell1.getStringCellValue());
						} else {
							testCaseNameIsMissing = true;
							System.out.println("Test Case Name is empty or missing. So, testCaseNameIsMissing is set to TRUE"
									+ cell1.getStringCellValue());
						}

						Cell cell2 = row.getCell(testCaseIDIndex);
						if (isCellValid(cell2)) {
							testCase.setTestCaseCode(cell2.getStringCellValue());
							System.out.println("Test Case Code: "
									+ cell2.getStringCellValue());
						} else {
							testCaseIDIsMissing = true;
							System.out.println("Test Case Code is empty or missing. So, testCaseIDIsMissing is set to TRUE"
									+ cell2.getStringCellValue());
						}

						if (testCaseNameIsMissing || testCaseIDIsMissing) {
							blankRowCount++;
							log.info("Blank row encountered. Skipping to next row");
							rowNum++;
							continue;
						} 

						// Mandatory info is present. Get the rest of the
						// testcase details
						Cell cell3 = row.getCell(testCaseDescIndex);
						if (isCellValid(cell3)) {
							if (cell3.getStringCellValue() != null
									&& cell3.getStringCellValue().length() > 2000) {
								testCase.setTestCaseDescription((cell3
										.getStringCellValue()
										.substring(0, 2000)));
							} else {
								testCase.setTestCaseDescription(cell3
										.getStringCellValue());
							}
						}

						Cell cell4 = row.getCell(testCaseCreatedDateindex);
						if (isCellValid(cell4)) {
							String dateValue = cell4.getStringCellValue();
							System.out
									.println("Created dateValue:" + dateValue);
							try {
								Date sd = new SimpleDateFormat(
										"E MMM dd HH:mm:ss z yyyy",
										Locale.ENGLISH).parse(dateValue);
								testCase.setTestCaseCreatedDate(sd);
							} catch (ParseException e) {
								log.error("exception in parsing data value", e);
							}
						}

						Cell cell5 = row.getCell(testCaseLastUpdatedDateIndex);
						if (isCellValid(cell5)) {
							String updatedDate = cell5.getStringCellValue();
							
							try {
								Date date = new SimpleDateFormat(
										"E MMM dd HH:mm:ss z yyyy",
										Locale.ENGLISH).parse(updatedDate);
								testCase.setTestCaseCreatedDate(date);
							} catch (ParseException e) {
								log.error("Error in date parsing", e);
							}
						}

						Cell cell7 = row.getCell(testCasePriorityIndex);
						if (isCellValid(cell7)) {
							TestCasePriority tcp = new TestCasePriority();
							tcp.setPriorityName(cell7.getStringCellValue());
							testCase.setTestCasePriority(tcp);							
						}

						Cell cell8 = row.getCell(testCaseTypeindex);
						if (isCellValid(cell8)) {
							int testCaseType = Integer.parseInt((cell8
									.getStringCellValue()));
							if (testCaseType == 0) {
								testCase.setTestCaseType(TAFConstants.TESTCASE_MANUAL);
							} else if (testCaseType == 1) {
								testCase.setTestCaseType(TAFConstants.TESTCASE_AUTOMATED);
							} else {
								testCase.setTestCaseType(TAFConstants.TESTCASE_MIXED);
							}
						}

						Cell cell9 = row.getCell(productFeatureCodeIndex);
						if (isCellValid(cell9)) {
							productFeatureName = cell9.getStringCellValue();

							if (productListService
									.isProductFeatureExistingByName(productFeatureName)) {
								productFeature = productListService
										.getByProductFeatureName(productFeatureName);
							} else {
								productFeature = new ProductFeature();
								productFeature.setProductFeatureName(productFeatureName);
								productListService.addProductFeature(productFeature);
								/* add feature to mongoDB */
								if(productFeature!=null){
									if(productFeature.getProductFeatureId()!=null){
										mongoDBService.addProductFeature(productFeature.getProductFeatureId());
									}
									
								}
								
							}
							Set<ProductFeature> productFeatureset = new HashSet<ProductFeature>();
							productFeatureset.add(productFeature);
							testCase.setProductFeature(productFeatureset);
						}
						testCase.setTestCaseSource(testCaseSource);
						testCase.setProductMaster(productMaster);

						
						testCases.add(testCase);
					}

				}// forloop //While loop
			} // If-else
		} catch (IOException IOE) {
			log.error("Error reading testcases from file", IOE);
			return null;
		} catch (Exception e) {
			log.error("Error reading testcases from file", e);
			return null;
		}

		testCases.get(0).toString();
		return testCases;
	}

	private boolean validateImportTestCases(List<TestCaseList> testCases) {

		if (testCases == null || testCases.isEmpty()) {
			log.info("No testcases present from file");
			return false;
		}

		List<TestCaseList> newTestCases = new ArrayList<TestCaseList>();
		List<TestCaseList> updatedTestCases = new ArrayList<TestCaseList>();
		List<TestCaseList> invalidTestCases = new ArrayList<TestCaseList>();

		String dataValidStatus = null;
		for (TestCaseList testCase : testCases) {
			if (testCase == null) {
				
				continue;
			}
			System.out
					.println("testCase.getTestCaseName(), testCase.getTestCaseCode()"
							+ testCase.getTestCaseName()
							+ " , "
							+ testCase.getTestCaseCode());
			dataValidStatus = validateData(testCase.getTestCaseCode(),
					testCase.getTestCaseName(), testCase.getProductMaster()
							.getProductId());
			// Check if testcase already exists
			if (DATA_ADD.equals(dataValidStatus)) {
				newTestCases.add(testCase);
			} else if (DATA_UPDATE.equals(dataValidStatus)) {
				List<TestCaseList> existingTestCase = testSuiteConfigurationService
						.getTestCaseByCodeNameProduct(testCase
								.getTestCaseCode(), testCase.getTestCaseName(),
								testCase.getProductMaster().getProductId());
				testCase.setTestCaseId(existingTestCase.get(0).getTestCaseId());
				updatedTestCases.add(testCase);
			} else if (DATA_ERROR.equals(dataValidStatus)) {
				invalidTestCases.add(testCase);
			}
		}

		if (newTestCases != null && !newTestCases.isEmpty()) {
			testSuiteConfigurationService.addTestCases(newTestCases);
		}
		if (updatedTestCases != null && !updatedTestCases.isEmpty()) {
		}
		if (invalidTestCases != null && !invalidTestCases.isEmpty()) {
		}

		return true;
	}

	@Override
	public boolean exportTestCases(int productId, String exportLocation) {
		productMaster = productListService.getProductById(productId);
		List<TestCaseList> testCasesList = testSuiteConfigurationService
				.getTestCaseListByProductId(productId);
		generateandExportExcelData(testCasesList, productMaster, exportLocation);

		return false;
	}

	/**
	 * 
	 * @param testCasesList
	 *            - List of testcases to be exported
	 * @return boolean status of the export
	 * 
	 *         The method generates the excel with the test cases as rows and
	 *         export it
	 */
	private boolean generateandExportExcelData(
			List<TestCaseList> testCasesList, ProductMaster productMaster,
			String exportLocation) {

		String productName = "";

		try {

			if (productMaster != null) {
				productName = productMaster.getProductName();
			}

			// Export location is specified and fileoutput stream is created for
			// export
			String fileExportlocation = exportLocation + "TestCases";

			boolean isDirCreated = checkAndCreateTempDirectory(fileExportlocation);
			log.info("File directories created: " + isDirCreated);
			

			// Timestamp for exported testcases
			Date currentDate = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssZ",
					Locale.ENGLISH);
			String timeStamp = sdf.format(currentDate);
			String fileName = TC_NAMING_CONV + productName + ".xlsx";
			

			FileOutputStream fos = new FileOutputStream(fileExportlocation
					+ File.separator + fileName);

			
			int rowCount = 0;
			// From testCaseList number of test cases is obtained.
			if (testCasesList != null && !testCasesList.isEmpty()) {
				rowCount = testCasesList.size();
			}

			XSSFWorkbook workbook = new XSSFWorkbook();

			// Overview Sheet
			Sheet testCaseOverviewSheet = workbook.createSheet("Overview");
			// TestCases Sheet
			Sheet testCasesheet = workbook.createSheet("TestCases");
			// TestSteps sheet
			Sheet testStepsSheet = workbook.createSheet("TestSteps");

			Row overviewTitleRow = testCaseOverviewSheet.createRow(0);
			Row overviewProductRow = testCaseOverviewSheet.createRow(1);
			Row overviewTestCaseCountRow = testCaseOverviewSheet.createRow(2);
			Row overviewTestStepsCountRow = testCaseOverviewSheet.createRow(3);

			Cell cellR1 = overviewTitleRow.createCell(0);
			cellR1.setCellValue("TAF Test Cases Export Data Summary");

			Cell cellR2 = overviewTitleRow.createCell(1);
			cellR2.setCellValue(new Date(System.currentTimeMillis()).toString());

			Cell cellR3 = overviewProductRow.createCell(0);
			cellR3.setCellValue("Product");

			Cell cellR4 = overviewProductRow.createCell(1);
			cellR4.setCellValue(productName);

			Cell cellR5 = overviewTestCaseCountRow.createCell(0);
			cellR5.setCellValue("Total TestCases");

			Cell cellR6 = overviewTestCaseCountRow.createCell(1);

			Cell cellR7 = overviewTestStepsCountRow.createCell(0);
			cellR7.setCellValue("Total TestSteps");

			Cell cellR8 = overviewTestStepsCountRow.createCell(1);

			// TestCase and TestSteps titles processing
			int rowNum = 0;
			int testStepRowNum = 1;
			int colNum = 0;

			// Title Row for TestCase
			String[] testCaseheader = { "testCaseId", "testCaseCode",
					"testCaseName", "testCaseDescription",
					"testCaseFeatureName", "testCaseCreatedDate",
					"TestCasePriority", "TestCaseType",
					"TestCaseLastUpdatedDate" };

			System.out
					.println("Header row: " + testCaseheader[0]
							+ testCaseheader[1] + testCaseheader[2]
							+ testCaseheader[3] + testCaseheader[4]
							+ testCaseheader[5] + testCaseheader[6]);
			int testCaseColCount = testCaseheader.length;

			// Title Row for TestStep
			String[] testStepheader = { "testStepId", "testCaseCode",
					"testStepCode", "testStepName", "testStepDescription",
					"testStepInput", "testStepExpectedOutput",
					"testStepCreatedDate", "testStepLastUpdatedDate" };
			System.out
					.println("Header row: " + testStepheader[0]
							+ testStepheader[1] + testStepheader[2]
							+ testStepheader[3]);
			int testStepColCount = testStepheader.length;

			// Creating title rows
			Row testCaseTitlerow = testCasesheet.createRow(rowNum);
			Row testStepTitleRow = testStepsSheet.createRow(rowNum);

			// Setting title row values for TestCase

			Cell[] testCaseTitleArray = new Cell[testCaseColCount];
			for (int counter = 0; counter < testCaseColCount; counter++) {
				testCaseTitleArray[counter] = testCaseTitlerow
						.createCell(counter);
				testCaseTitleArray[counter]
						.setCellValue(testCaseheader[counter]);
			}

			// Setting title row values for TestCase

			Cell[] testStepTitleArray = new Cell[testStepColCount];
			for (int counter = 0; counter < testStepColCount; counter++) {
				testStepTitleArray[counter] = testStepTitleRow
						.createCell(counter);
				testStepTitleArray[counter]
						.setCellValue(testStepheader[counter]);
			}

			if (testCasesList != null && !testCasesList.isEmpty()) {
				// TestCase and TestSteps Processing
				for (TestCaseList testCase : testCasesList) {

					rowNum++;
					colNum = 0;
					// Creating Object array for holding test case data
					System.out
							.println("String array for holding test case data");

					Row testCaserow = testCasesheet.createRow(rowNum);

					String[] testCaseValueArray = new String[testCaseColCount];
					int[] testCaseColTypeArray = new int[testCaseColCount];

					testCaseValueArray[colNum] = testCase.getTestCaseId()
							.toString();
					testCaseColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					testCaseValueArray[colNum] = testCase.getTestCaseCode();
					testCaseColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					testCaseValueArray[colNum] = testCase.getTestCaseName();
					testCaseColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					testCaseValueArray[colNum] = testCase
							.getTestCaseDescription();
					testCaseColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					ProductFeature prod_feature = testCase.getProductFeature().iterator().next();
					
					if (prod_feature != null
							&& testCase.getProductFeature().iterator().next()
									.getProductFeatureName() != null) {

						testCaseValueArray[colNum] = prod_feature
								.getProductFeatureName();
					} else {
						testCaseValueArray[colNum] = "";
					}
					testCaseColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					if (testCase.getTestCaseCreatedDate() != null) {
						testCaseValueArray[colNum] = testCase
								.getTestCaseCreatedDate().toString();
					} else {
						testCaseValueArray[colNum] = "";
					}
					testCaseColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					if (testCase.getTestCasePriority() != null) {
				
						testCaseValueArray[colNum] = testCase.getTestCasePriority().getPriorityName();
						
					} else {
						testCaseValueArray[colNum] = "";
					}
					testCaseColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					if (testCase.getTestCaseType() != null) {
						testCaseValueArray[colNum] = testCase.getTestCaseType();
					} else {
						testCaseValueArray[colNum] = "";
					}
					testCaseColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					if (testCase.getTestCaseLastUpdatedDate() != null) {
						testCaseValueArray[colNum] = testCase
								.getTestCaseLastUpdatedDate().toString();
					} else {
						testCaseValueArray[colNum] = "";
					}
					testCaseColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					Cell[] testCaseCells = new Cell[testCaseColCount];
					for (int colCounter = 0; colCounter < testCaseColCount; colCounter++) {
						testCaseCells[colCounter] = testCaserow
								.createCell(colCounter);
						testCaseCells[colCounter]
								.setCellType(testCaseColTypeArray[colCounter]);

						if (testCaseCells[colCounter].getCellType() == Cell.CELL_TYPE_NUMERIC) {
							testCaseCells[colCounter].setCellValue(Integer
									.parseInt(testCaseValueArray[colCounter]));
						}

						else if (testCaseCells[colCounter].getCellType() == Cell.CELL_TYPE_STRING) {
							testCaseCells[colCounter]
									.setCellValue((testCaseValueArray[colCounter]));
						} else {
							testCaseCells[colCounter]
									.setCellValue((testCaseValueArray[colCounter]));
						}
					}

					// setting testStep Values

					Set<TestCaseStepsList> testStepList = testCase
							.getTestCaseStepsLists();
					if (testStepList != null && !testStepList.isEmpty()) {
						int stepSize = testStepList.size();

						

						if (stepSize > 0) {
							// Initialize rownum and colnum for testSteps
							// int testStepRowNum = 1;

							for (TestCaseStepsList testStep : testStepList) {
								// Create row for each test Step
								Row testSteprow = testStepsSheet
										.createRow(testStepRowNum);

								int testStepColNum = 0;
								String[] testStepValueArray = new String[testStepColCount];
								int[] testStepColTypeArray = new int[testStepColCount];

								testStepValueArray[testStepColNum] = testStep
										.getTestStepId().toString();
								testStepColTypeArray[testStepColNum++] = Cell.CELL_TYPE_STRING;

								testStepValueArray[testStepColNum] = testStep
										.getTestCaseList().getTestCaseCode();
								testStepColTypeArray[testStepColNum++] = Cell.CELL_TYPE_STRING;

								testStepValueArray[testStepColNum] = testStep
										.getTestStepCode();
								testStepColTypeArray[testStepColNum++] = Cell.CELL_TYPE_STRING;

								testStepValueArray[testStepColNum] = testStep
										.getTestStepName();
								testStepColTypeArray[testStepColNum++] = Cell.CELL_TYPE_STRING;

								testStepValueArray[testStepColNum] = testStep
										.getTestStepDescription();
								testStepColTypeArray[testStepColNum++] = Cell.CELL_TYPE_STRING;

								testStepValueArray[testStepColNum] = testStep
										.getTestStepInput();
								testStepColTypeArray[testStepColNum++] = Cell.CELL_TYPE_STRING;

								testStepValueArray[testStepColNum] = testStep
										.getTestStepExpectedOutput();
								testStepColTypeArray[testStepColNum++] = Cell.CELL_TYPE_STRING;

								Date testStepCreatedDate = testStep
										.getTestStepCreatedDate();
								if (testStepCreatedDate != null) {
									testStepValueArray[testStepColNum] = testStepCreatedDate
											.toString();
								} else
									testStepValueArray[testStepColNum] = "";
								testStepColTypeArray[testStepColNum++] = Cell.CELL_TYPE_STRING;

								Date testStepLastUpdatedDate = testStep
										.getTestStepLastUpdatedDate();
								if (testStepLastUpdatedDate != null) {
									testStepValueArray[testStepColNum] = testStepLastUpdatedDate
											.toString();
								} else
									testStepValueArray[testStepColNum] = "";
								testStepColTypeArray[testStepColNum++] = Cell.CELL_TYPE_STRING;

								// Setting Values in cells
								Cell[] testStepCells = new Cell[testStepColCount];

								for (int colCounter = 0; colCounter < testStepColCount; colCounter++) {
									testStepCells[colCounter] = testSteprow
											.createCell(colCounter);
									testStepCells[colCounter]
											.setCellType(testCaseColTypeArray[colCounter]);

									if (testStepCells[colCounter].getCellType() == Cell.CELL_TYPE_NUMERIC) {
										testStepCells[colCounter]
												.setCellValue(Integer
														.parseInt(testStepValueArray[colCounter]));
									}

									else if (testStepCells[colCounter]
											.getCellType() == Cell.CELL_TYPE_STRING) {
										testStepCells[colCounter]
												.setCellValue((testStepValueArray[colCounter]));
									}

								}
								testStepRowNum++;
							}

						}

					}
				}
			}
			cellR6.setCellValue(rowNum);
			cellR8.setCellValue(testStepRowNum - 1);

			workbook.write(fos);
			fos.close();
			status = true;

		} catch (FileNotFoundException e) {
			log.error("ERROR  ",e);
		} catch (IOException e) {
			log.error("ERROR  ",e);
		}
		return status;
	}

	public void evaluateCellValue(Cell cell, String cellType) {

	}

	@Override
	public boolean exportTestExecutionResults() {

		return false;
	}

	@Override
	public boolean importTestCases() {

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exportTestCases() {

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exportTestExecutionResults(int testSuiteId) {

		// TODO Auto-generated method stub
		return false;
	}

	// Data Validation for Import testCases
	public String validateData(String testCaseCode, String testCaseName,
			int productId) {

		List<TestCaseList> testCases = testSuiteConfigurationService
				.getTestCaseByCodeNameProduct(testCaseCode, testCaseName,
						productId);
		if (testCases == null || testCases.isEmpty()) {
			// Testcase with the code and/or name does not exist
			return DATA_ADD;
		}
		if (testCases.size() == 1) {
			// Only one test case exists for the code and name. Data needs
			// Update
			return DATA_UPDATE;
		}

		if (testCases.size() > 1) {
			// More than one test case results due to existing code as well
			// name. Error row.
			return DATA_ERROR;
		}
		return DATA_ERROR;
	}

	// Cell Validation for Import testCases
	private boolean isCellValid(Cell cell) {
		boolean validCell = false;
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				validCell = false;
				break;
			case Cell.CELL_TYPE_STRING:
				if ((cell.getStringCellValue() == null)
						|| (cell.getStringCellValue() != null
								&& !"".equals(cell.getStringCellValue()) && !" "
									.equals(cell.getStringCellValue()))) {
					validCell = true;
				}
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (!(Double.isNaN(cell.getNumericCellValue()))) {
					validCell = true;
				}
				break;
			}

		}
		return validCell;

	}

	@Override
	public String exportDefects(int testRunNo, int testRunConfigChild,
			String exportLocation) {

		String fileName = "";
		int deviceListId = -1;
		List<TestExecutionResultBugList> testExecutionResultBugs = testExecutionBugsService
				.listByTestRun(testRunNo, testRunConfigChild);
		fileName = generateAndExportDefects(testExecutionResultBugs, testRunNo,
				testRunConfigChild, deviceListId, exportLocation);

		return fileName;
	}

	@Override
	public String exportDeviceDefects(int testRunNo,
			int testRunConfigurationChildId, int deviceListId,
			String exportLocation) {

		String fileName = "";

		List<TestExecutionResultBugList> testExecutionResultBugs = testExecutionBugsService
				.list(testRunNo, testRunConfigurationChildId, deviceListId);
		fileName = generateAndExportDefects(testExecutionResultBugs, testRunNo,
				testRunConfigurationChildId, deviceListId, exportLocation);

		return fileName;
	}

	public String generateAndExportDefects(
			List<TestExecutionResultBugList> testExecutionResultBugs,
			int testRunNo, int testRunConfigChildId, int deviceListId,
			String exportLocation) {

		String fileName = "";
		TestExecutionResultBugList testExecutionResultBug = null;
		TestExecutionResult testExecutionResult = null;
		ProductMaster product;
		ProductVersionListMaster productVersionListMaster;
		TestSuiteList testSuiteList;
		TestCaseList testCaseList = null;
		TestCaseStepsList testStepsList = null;
		TestRunList testRunList = null;
		String productName = "";
		String productVersionName = "";
		String testSuiteName = "";
		DeviceList deviceList = null;
		TestRunConfigurationChild testRunConfigChild;
		int totalDefectsCount = 0;
		try {

			String fileExportlocation = exportLocation;
			boolean isDirCreated = checkAndCreateTempDirectory(fileExportlocation);

			log.info("File directories created: " + isDirCreated);
			

			// Timestamp for exported testcases
			Date currentDate = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMMdd-HHmmsss");
			String timeStamp = sdf.format(currentDate);

			TestRunConfigurationChild testRunChildConfig = testRunConfigservice
					.getByTestRunConfigurationChildId(testRunConfigChildId);
			if (testRunChildConfig != null) {
				productVersionListMaster = testRunChildConfig
						.getProductVersionListMaster();
				testSuiteList = testRunChildConfig.getTestSuiteList();

				if (productVersionListMaster != null) {
					productVersionName = productVersionListMaster
							.getProductVersionName();
					productMaster = productVersionListMaster.getProductMaster();
					if (productMaster != null) {
						productName = productMaster.getProductName();
					}
				}
				if (testSuiteList != null) {
					testSuiteName = testSuiteList.getTestSuiteName();
				}
			}

			fileName = DEFECTS_NAMING_CONV + productName + "_" + testRunNo
					+ "_" + timeStamp + ".xlsx";
			

			FileOutputStream fos = new FileOutputStream(fileExportlocation
					+ File.separator + fileName);

			XSSFWorkbook workbook = new XSSFWorkbook();

			// Overview Sheet
			Sheet defectsOverviewSheet = workbook.createSheet("Overview");

			// Defects Sheet
			Sheet defectSheet = workbook.createSheet("Defects");

			// Creating Overview Sheet Rows
			Row overViewSummaryRow = defectsOverviewSheet.createRow(0);
			Row overViewProductRow = defectsOverviewSheet.createRow(1);
			Row overViewProductVersionRow = defectsOverviewSheet.createRow(2);
			Row overViewTestSuiteRow = defectsOverviewSheet.createRow(3);
			Row overViewTestRunRow = defectsOverviewSheet.createRow(4);
			Row overViewTestRunConfigRow = defectsOverviewSheet.createRow(5);
			Row overViewDefectCountRow = defectsOverviewSheet.createRow(6);
			Row overViewTestEnvironmentRow = defectsOverviewSheet.createRow(7);

			// Creating Overview Sheet Cells
			Cell cellR1 = overViewSummaryRow.createCell(0);
			Cell cellR2 = overViewSummaryRow.createCell(1);
			Cell cellR3 = overViewProductRow.createCell(0);
			Cell cellR4 = overViewProductRow.createCell(1);
			Cell cellR5 = overViewProductVersionRow.createCell(0);
			Cell cellR6 = overViewProductVersionRow.createCell(1);
			Cell cellR7 = overViewTestSuiteRow.createCell(0);
			Cell cellR8 = overViewTestSuiteRow.createCell(1);

			Cell cellR9 = overViewTestRunRow.createCell(0);
			Cell cellR10 = overViewTestRunRow.createCell(1);
			Cell cellR11 = overViewTestRunConfigRow.createCell(0);
			Cell cellR12 = overViewTestRunConfigRow.createCell(1);
			Cell cellR13 = overViewDefectCountRow.createCell(0);
			Cell cellR14 = overViewDefectCountRow.createCell(1);

			Cell cellR15 = overViewTestEnvironmentRow.createCell(0);
			Cell cellR16 = overViewTestEnvironmentRow.createCell(1);

			// Overview Details
			if (testExecutionResultBugs != null
					&& !testExecutionResultBugs.isEmpty())
				testExecutionResultBug = testExecutionResultBugs.get(0);

			if (testExecutionResultBug != null)

			if (testExecutionResult != null) {

				testSuiteList = testExecutionResult.getTestSuiteList();

				if (testSuiteList != null) {
					testSuiteName = testSuiteList.getTestSuiteName();

					product = testSuiteList.getProductMaster();
					if (product != null) {
						productName = product.getProductName();
					}

					productVersionListMaster = testSuiteList
							.getProductVersionListMaster();
					if (productVersionListMaster != null) {
						productVersionName = productVersionListMaster
								.getProductVersionName();
					}
				}

			}

			testRunConfigChild = testRunConfigservice
					.getByTestRunConfigurationChildId(testRunConfigChildId);
			testRunConfigChild.getTestRunConfigurationName();

			// Setting Overview Sheet Cell Values

			cellR1.setCellValue("TAF Defect Summary");
			cellR2.setCellValue(new Date(System.currentTimeMillis()).toString());

			cellR3.setCellValue("Product");
			cellR4.setCellValue(productName);
			cellR5.setCellValue("Product Version");
			cellR6.setCellValue(productVersionName);
			cellR7.setCellValue("TestSuite");
			cellR8.setCellValue(testSuiteName);

			cellR9.setCellValue("Test Run No");
			cellR10.setCellValue(testRunNo);

			cellR11.setCellValue("Test Run Configuration");
			cellR12.setCellValue(testRunConfigChild
					.getTestRunConfigurationName());

			cellR13.setCellValue("Defect Count");
			cellR14.setCellValue(totalDefectsCount);

			cellR15.setCellValue("Test Environment");

			cellR16.setCellValue("");

			// Header
			String[] defectHeader = { "testExecutionResultBugId", "Product",
					"ProductVersion", "DefectType", "bugTitle",
					"bugDescription", "bugManagementSystemName",
					"bugManagementSystemBugId", "bugFilingStatus", "remarks",
					"deviceId", "deviceModel", "deviceMake", "device_plaform",
					"device_platform_version", "testCaseId", "testStepId",
					"testCaseCode", "testStepCode", "productFeature",
					"productFeatureCode", "Bug Creation Time" };

			int defectColCount = defectHeader.length;

			int defectRowNum = 0;

			Row defectHeaderRow = defectSheet.createRow(0);

			// Setting defect header values
			Cell[] defectTitleArray = new Cell[defectColCount];

			for (int counter = 0; counter < defectColCount; counter++) {
				defectTitleArray[counter] = defectHeaderRow.createCell(counter);
				defectTitleArray[counter].setCellValue(defectHeader[counter]);
			}

			// Processing defects list
			if (testExecutionResultBugs != null
					&& !testExecutionResultBugs.isEmpty()) {
				totalDefectsCount = testExecutionResultBugs.size();
				cellR14.setCellValue(totalDefectsCount);

				// Creating defects row for each defect
				for (TestExecutionResultBugList testExecutionResultBugList : testExecutionResultBugs) {

					defectRowNum++;
					int defectColNum = 0;
					if (testExecutionResultBugList != null) {
						Row defectRow = defectSheet.createRow(defectRowNum);

						// Forming String array for defect column values
						// Forming String array for defect column types

						String[] defectValueArray = new String[defectColCount];
						int[] defectColTypeArray = new int[defectColCount];

						if (testExecutionResultBugList
								.getTestExecutionResultBugId() != null) {
							defectValueArray[defectColNum] = testExecutionResultBugList
									.getTestExecutionResultBugId().toString();
						} else {
							defectValueArray[defectColNum] = "";
						}
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						defectValueArray[defectColNum] = productName;
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						defectValueArray[defectColNum] = productVersionName;
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						defectValueArray[defectColNum] = "bug";
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						defectValueArray[defectColNum] = testExecutionResultBugList
								.getBugTitle();
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						defectValueArray[defectColNum] = testExecutionResultBugList
								.getBugDescription();
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						defectValueArray[defectColNum] = testExecutionResultBugList
								.getBugManagementSystemName();
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						defectValueArray[defectColNum] = testExecutionResultBugList
								.getBugManagementSystemBugId();
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						defectValueArray[defectColNum] = testExecutionResultBugList
								.getRemarks();
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						// Device Details
						TestExecutionResult testExecResult =null; 
						if ((testExecResult != null)
								&& testExecResult.getTestRunList() != null) {
							deviceList = testExecResult.getTestRunList()
									.getDeviceList();
						}

						if (deviceList != null) {
							defectValueArray[defectColNum] = deviceList
									.getDeviceId();
							defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

							defectValueArray[defectColNum] = defectValueArray[defectColNum] = deviceList
									.getDeviceModelMaster().getDeviceModel();
							defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

							defectValueArray[defectColNum] = deviceList
									.getDeviceModelMaster()
									.getDeviceMakeMaster().getDeviceMake();
							defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

							defectValueArray[defectColNum] = deviceList
									.getDevicePlatformVersionListMaster()
									.getDevicePlatformMaster()
									.getDevicePlatformName();
							defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

							defectValueArray[defectColNum] = deviceList
									.getDevicePlatformVersionListMaster()
									.getDevicePlatformVersion();
							defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						} else {
							defectValueArray[defectColNum] = "";
							defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

							defectValueArray[defectColNum] = "";
							defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

							defectValueArray[defectColNum] = "";
							defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

							defectValueArray[defectColNum] = "";
							defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

							defectValueArray[defectColNum] = "";
							defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;
						}


						if (testCaseList != null
								&& testCaseList.getTestCaseCode() != null) {
							defectValueArray[defectColNum] = testCaseList
									.getTestCaseCode().toString();
						} else {
							defectValueArray[defectColNum] = "";
						}
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						if (testCaseList != null
								&& testCaseList.getTestCaseId() != null) {
							defectValueArray[defectColNum] = testCaseList
									.getTestCaseId().toString();
						} else {
							defectValueArray[defectColNum] = "";
						}
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						if (testStepsList != null) {
							defectValueArray[defectColNum] = testStepsList
									.getTestStepCode().toString();
						} else {
							defectValueArray[defectColNum] = "";
						}
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						if (testStepsList != null
								&& (testStepsList.getTestStepId()) != null) {
							defectValueArray[defectColNum] = testStepsList
									.getTestStepId().toString();
						} else {
							defectValueArray[defectColNum] = "";
						}
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						if (testCaseList != null
								&& testCaseList.getProductFeature() != null) {
							defectValueArray[defectColNum] = testCaseList
									.getProductFeature()
									.iterator().next().getProductFeatureName();
						} else {
							defectValueArray[defectColNum] = "";
						}
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						if (testCaseList != null
								&& testCaseList.getProductFeature() != null) {
							defectValueArray[defectColNum] = testCaseList
									.getProductFeature()
									.iterator().next().getProductFeatureCode();
						} else {
							defectValueArray[defectColNum] = "";
						}
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						if (testExecutionResultBugList.getBugCreationTime() != null) {
							Date bugCreationDate = testExecutionResultBugList
									.getBugCreationTime();
							SimpleDateFormat sdformat = new SimpleDateFormat(
									"E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
							String bugCreationTime = sdformat
									.format(bugCreationDate);
							defectValueArray[defectColNum] = bugCreationTime;
						} else {
							defectValueArray[defectColNum] = "";
						}
						defectColTypeArray[defectColNum++] = Cell.CELL_TYPE_STRING;

						// Setting Values in cells
						Cell[] defectCells = new Cell[defectColCount];

						for (int colCounter = 0; colCounter < defectColCount; colCounter++) {
							defectCells[colCounter] = defectRow
									.createCell(colCounter);
							defectCells[colCounter]
									.setCellType(defectColTypeArray[colCounter]);

							if (defectCells[colCounter].getCellType() == Cell.CELL_TYPE_NUMERIC) {
								defectCells[colCounter]
										.setCellValue(Integer
												.parseInt(defectValueArray[colCounter]));
							}

							else if (defectCells[colCounter].getCellType() == Cell.CELL_TYPE_STRING) {
								defectCells[colCounter]
										.setCellValue((defectValueArray[colCounter]));
							}

						}

					}

				}
			}

			workbook.write(fos);
			fos.close();

		} catch (FileNotFoundException e) {
			log.error("ERROR  ",e);
		} catch (IOException e) {
			log.error("ERROR  ",e);
		}
		status = true;
		return fileName;
	}
	
	
	public boolean checkAndCreateTempDirectory(String location) {

		
		File file = new File(location);
		boolean isDircreated = false; // file.mkdirs();
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
				isDircreated = file.mkdirs();
			}
		} else {
			isDircreated = file.mkdirs();
		}
		return isDircreated;
	}

	public static File getFile(String fileWithPath) {
		File file = null;
		URL url = null;
		ReadableByteChannel rbc = null;
		try {
			url = new URL(fileWithPath);
			URLConnection urlConn = url.openConnection();

			InputStream fis = (FileInputStream) urlConn.getInputStream();

			try {
				if (file.exists()) {
					file.delete();
					file.createNewFile();
				}
			} catch (Exception ee) {

			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (Exception e) {
			log.error("ERROR  ",e);
			file = null;
		}
		return file;
	}

	@Override
	public int exportDefectsToJira(int testRunNo,
			int testRunConfigurationChildId, JiraConnector jiraConnector) {

		int defectCounter = 0;
		int deviceListId = -1;
		List<TestExecutionResultBugList> testExecutionResultBugs = testExecutionBugsService
				.listByTestRun(testRunNo, testRunConfigurationChildId);
		defectCounter = jiraDefectsExport(testExecutionResultBugs,
				jiraConnector);

		return defectCounter;
	}

	@Override
	public int exportDeviceDefectsToJira(int testRunNo,
			int testRunConfigurationChildId, int deviceListId,
			JiraConnector jiraConnector) {

		int defectCounter = 0;

		List<TestExecutionResultBugList> testExecutionResultBugs = testExecutionBugsService
				.list(testRunNo, testRunConfigurationChildId, deviceListId);
		defectCounter = jiraDefectsExport(testExecutionResultBugs,
				jiraConnector);

		return defectCounter;
	}

	@Override
	public int exportDefectToJira(int testExecResultBugId,
			JiraConnector jiraConnector) {

		int defectCounter = 0;

		TestExecutionResultBugList testExecutionResultBug = testExecutionBugsService
				.getByBugId(testExecResultBugId);
		List<TestExecutionResultBugList> testExecutionResultBugs = new ArrayList<TestExecutionResultBugList>();
		testExecutionResultBugs.add(testExecutionResultBug);
		defectCounter = jiraDefectsExport(testExecutionResultBugs,
				jiraConnector);

		return defectCounter;
	}

	public int jiraDefectsExport(
			List<TestExecutionResultBugList> testExecutionResultBugsList,
			JiraConnector jiraConnector) {

		int defectCounter = 0;
		String productName = "";
		String IssueTypename = "";
		String summary = "";
		String priorityName = "";
		String environment = "";
		String description = "";
		String component = "";
		String[] productVersionName = new String[] { "" };
		String returnKey = "";
		DeviceList deviceList = null;
		TestCaseList testCaseList = null;
		TestCaseStepsList testStepList = null;
		TestSuiteList testSuiteList = null;
		ProductVersionListMaster prodVersion = null;
		ProductMaster prod = null;
		String deviceId = "";
		String deviceModel = "";
		String deviceMake = "";
		String devicePlatformName = "";
		String devicePlatformVersion = "";

		if (testExecutionResultBugsList != null
				&& !testExecutionResultBugsList.isEmpty()) {
			for (TestExecutionResultBugList testExecutionResultBugList : testExecutionResultBugsList) {

				if (testExecutionResultBugList == null
						|| (testExecutionResultBugList != null )) {
					
					return defectCounter;
				} else {
					summary = testExecutionResultBugList.getBugTitle();

					description = testExecutionResultBugList
							.getBugDescription();

					priorityName = "Minor";

					IssueTypename = "Bug";

					component = "";

					String failureReason = "";
					// Product Details

					TestExecutionResult testExecResult = null;
					if (testExecResult != null) {

						testSuiteList = testExecResult.getTestSuiteList();
						prodVersion = testSuiteList
								.getProductVersionListMaster();

						if (prodVersion != null) {
							prod = prodVersion.getProductMaster();
							productName = prod.getProductName();
							productVersionName[0] = prodVersion
									.getProductVersionName();
						}
						if ((testExecResult != null)
								&& testExecResult.getTestRunList() != null) {
							deviceList = testExecResult.getTestRunList()
									.getDeviceList();
						}

						if (deviceList != null) {
							deviceId = deviceList.getDeviceId();
							deviceModel = deviceList.getDeviceModelMaster()
									.getDeviceModel();
							deviceMake = deviceList.getDeviceModelMaster()
									.getDeviceMakeMaster().getDeviceMake();
							devicePlatformName = deviceList
									.getDevicePlatformVersionListMaster()
									.getDevicePlatformMaster()
									.getDevicePlatformName();
							devicePlatformVersion = deviceList
									.getDevicePlatformVersionListMaster()
									.getDevicePlatformVersion();

						} else {
							deviceId = "";
							deviceModel = "";
							deviceMake = "";
							devicePlatformName = "";
							devicePlatformVersion = "";
						}
						environment = deviceModel + "," + deviceMake + ","
								+ devicePlatformName + ","
								+ devicePlatformVersion;

						testCaseList = testExecResult.getTestCaseList();
						testStepList = testExecResult.getTestCaseStepsList();
						failureReason = testExecResult.getFailureReason();
						description = "TestCase :"
								+ testCaseList.getTestCaseName()
								+ " \n"
								+ "TestStep: "
								+ testStepList.getTestStepName()
								+ "\n"
								+ "FailureReason:"
								+ failureReason
								+ "\n"
								+ testExecutionResultBugList
										.getBugDescription();

						if (testCaseList != null && testCaseList.getTestCasePriority() != null) {
							priorityName = getPriority(testCaseList
									.getTestCasePriority().getPriorityName());
						}

						summary = testExecutionResultBugList.getBugTitle();
						if (testCaseList.getTestCaseCode() != null
								&& !testCaseList.getTestCaseCode().isEmpty())
							summary = summary + " (TC : "
									+ testCaseList.getTestCaseCode() + ") ";
						if (testStepList.getTestStepCode() != null
								&& !testStepList.getTestStepCode().isEmpty())
							summary = summary + " (TS : "
									+ testStepList.getTestStepCode() + ")";

					}

									log.info("JIRA defect insert for bug :" + summary);
									log.info("Values for insert defect: productName, IssueTypename, summary, priorityName, environment, description, null, productVersionName"
									+ productName
									+ "\n,"
									+ IssueTypename
									+ "\n,"
									+ summary
									+ "\n,"
									+ priorityName
									+ "\n,"
									+ environment
									+ "\n,"
									+ description
									+ "\n," + "\n," + productVersionName);
					try {
						returnKey = jiraConnector.insertDefect(productName,
								IssueTypename, summary, priorityName,
								environment, description, null,
								productVersionName);
					} catch (Exception e) {
						log.error("Unable to create defects ", e);
					}

					System.out.print("return value from insert defect: "
							+ returnKey);
					

					// Updating the defect filing status and Defect Management
					// ID in Database.
					if (returnKey != null && !returnKey.isEmpty()) {
						testExecutionResultBugList
								.setBugManagementSystemBugId(returnKey);
						testExecutionResultBugList
								.setBugManagementSystemName(TAFConstants.DEFECT_MANAGEMENT_SYSTEM_JIRA);
						testExecutionBugsService
								.update(testExecutionResultBugList);
						defectCounter++;
					}

				}
			}
		}
		return defectCounter;
	}

	public String getPriority(String testCasePriority) {

		int testPriority = 0;
		String priority = "";
		if (testCasePriority.equalsIgnoreCase("Critical")) {
			testPriority = PRIORITY_CRITICAL;
		} else if (testCasePriority.equalsIgnoreCase("High")) {
			testPriority = PRIORITY_HIGH;
		} else if (testCasePriority.equalsIgnoreCase("Medium")) {
			testPriority = PRIORITY_MEDIUM;
		} else if (testCasePriority.equalsIgnoreCase("Low")) {
			testPriority = PRIORITY_TRIVIAL;
		}

		switch (testPriority) {
		case 1:
			priority = "Critical";
		case 2:
			priority = "Major";
		case 3:
			priority = "Minor";
		case 4:
			priority = "Trivial";
		default:
			priority = "Minor";
		}
		return priority;
	}

	public int checkForExistingDefect(
			TestExecutionResultBugList testExecutionResultBugList) {

		int defectCounter = 0;
		if (testExecutionResultBugList != null) {
			TestExecutionResult testExecResult =null; 
			if (testExecResult != null) {
				testExecResult.getTestCaseList().getTestCaseId();
			}
		}

		return defectCounter;
	}

	@Override
	public boolean workPackageTestCaseExecutionPlanDataExport(
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList,
			WorkPackage workPackage, String exportLocation) {
		String workPackageName = "";
		String productName = "";
		String productVersionName="";
		String buildName="";
		String runName="";
		String plannedStartDate="";
		String reportGenerationDate="";
		String wpstatus="";
		boolean status=true;
		try {

			if (workPackage != null) {
				workPackageName = workPackage.getName();
				productName = workPackage.getProductBuild().getProductVersion()
						.getProductMaster().getProductName();
				productVersionName=workPackage.getProductBuild().getProductVersion().getProductVersionName();
				buildName=workPackage.getProductBuild().getBuildname();
				runName=workPackage.getName();
				plannedStartDate=DateUtility.sdfDateformatWithOutTime(workPackage.getPlannedStartDate());
				wpstatus=workPackage.getWorkFlowEvent().getWorkFlow().getStageName();
			}

			// Export location is specified and fileoutput stream is created for
			// export
			String fileExportlocation = exportLocation
					+ "WorkPackageTestCaseExecutionPlan";

			boolean isDirCreated = checkAndCreateTempDirectory(fileExportlocation);
			log.info("File directories created: " + isDirCreated);
			

			// Timestamp for exported testcases
			Date currentDate = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.ENGLISH);
			String timeStamp = sdf.format(currentDate);
			reportGenerationDate=timeStamp;
			String fileName = TC_NAMING_CONV_WORKPACKAGE+"-" + productName + "-"
					+ workPackageName + ".xlsx";
			

			FileOutputStream fos = new FileOutputStream(fileExportlocation
					+ File.separator + fileName);

			
			int rowCount = 0;
			// From testCaseList number of test cases is obtained.
			XSSFWorkbook workbook = new XSSFWorkbook();
			if (workPackageTestCaseExecutionPlanList != null
					&& !workPackageTestCaseExecutionPlanList.isEmpty()) {
				rowCount = workPackageTestCaseExecutionPlanList.size();
			}

			Sheet workPackageTestCaseExecutionPlanSheet = workbook
					.createSheet("WPTestCaseExecutionPlan");

			Row overviewProductNameRow = workPackageTestCaseExecutionPlanSheet
					.createRow(1 );
			Row overviewProductVersionNameRow = workPackageTestCaseExecutionPlanSheet
					.createRow(2);
			Row overviewBuildNameRow = workPackageTestCaseExecutionPlanSheet
					.createRow(3);
			Row overviewRunNameRow = workPackageTestCaseExecutionPlanSheet
					.createRow(4);
			Row overviewPlannedExecutionDateRow = workPackageTestCaseExecutionPlanSheet
					.createRow(5);
			Row overviewStatusRow = workPackageTestCaseExecutionPlanSheet
					.createRow(6);
			Row overviewReportGenerationDateRow = workPackageTestCaseExecutionPlanSheet
					.createRow(7);

			Cell cellR1 = overviewProductNameRow.createCell(1);
			cellR1.setCellValue("ProductName");

			Cell cellR2 = overviewProductNameRow.createCell(2);
			cellR2.setCellValue(productName); 

			Cell cellR3 = overviewProductVersionNameRow.createCell(1);
			cellR3.setCellValue("ProductVersionName");

			Cell cellR4 = overviewProductVersionNameRow.createCell(2);
			cellR4.setCellValue(productVersionName); 

			Cell cellR5 = overviewBuildNameRow.createCell(1);
			cellR5.setCellValue("BuildName");

		Cell cellR6 = overviewBuildNameRow.createCell(2);
			cellR6.setCellValue(buildName); 
			
			Cell cellR7 = overviewRunNameRow.createCell(1);
			cellR7.setCellValue("RunName");

			Cell cellR8 = overviewRunNameRow.createCell(2);
			cellR8.setCellValue(runName);
			
			Cell cellR9 = overviewPlannedExecutionDateRow.createCell(1);
			cellR9.setCellValue("PlannedStartDate");

			Cell cellR10 = overviewPlannedExecutionDateRow.createCell(2);
			cellR10.setCellValue(plannedStartDate); 
			

			Cell cellR11 = overviewStatusRow.createCell(1);
			cellR9.setCellValue("Status");

			Cell cellR12 = overviewStatusRow.createCell(2);
			cellR10.setCellValue(wpstatus); 

			
			Cell cellR13 = overviewReportGenerationDateRow.createCell(1);
			cellR11.setCellValue("ReportGenerationDate");

			Cell cellR14 = overviewReportGenerationDateRow.createCell(2);
			cellR12.setCellValue(reportGenerationDate);

			// TestCase and TestSteps titles processing
			int rowNum = 11;
			int colNum = 1;
			

			// Title Row for TestCase
			String[] workPackageTestCaseExecutionPlanHeader = { "TestCaseId",
					"TestCaseName", "Description","DecouplingCategory","EnvironmentName", "TestLead",
					"Tester", "PlannedExecutionDate",
					"PlannedShift", "ExecutionStatus","ExecutionPriority","IsExecuted" };

			System.out.println("Header row: "
					+ workPackageTestCaseExecutionPlanHeader[0]
					+ workPackageTestCaseExecutionPlanHeader[1]
					+ workPackageTestCaseExecutionPlanHeader[2]
					+ workPackageTestCaseExecutionPlanHeader[3]
					+ workPackageTestCaseExecutionPlanHeader[4]
					+ workPackageTestCaseExecutionPlanHeader[5]
					+ workPackageTestCaseExecutionPlanHeader[6]
					+ workPackageTestCaseExecutionPlanHeader[7]
					+ workPackageTestCaseExecutionPlanHeader[8]
							+ workPackageTestCaseExecutionPlanHeader[9]
							+ workPackageTestCaseExecutionPlanHeader[10]
					+ workPackageTestCaseExecutionPlanHeader[11]);
			int workPackageTestCaseExecutionPlanColCount = workPackageTestCaseExecutionPlanHeader.length;
			log.info("workPackageTestCaseExecutionPlan Column Count : "
							+ workPackageTestCaseExecutionPlanColCount);

			// Creating title rows
			Row workPackageTestCaseExecutionPlanTitlerow = workPackageTestCaseExecutionPlanSheet
					.createRow(rowNum++);

			// Setting title row values for TestCase

			Cell[] workPackageTestCaseExecutionPlanTitleArray = new Cell[workPackageTestCaseExecutionPlanColCount];
			for (int counter = 0; counter < workPackageTestCaseExecutionPlanColCount; counter++) {
				workPackageTestCaseExecutionPlanTitleArray[counter] = workPackageTestCaseExecutionPlanTitlerow
						.createCell(colNum++);
				workPackageTestCaseExecutionPlanTitleArray[counter]
						.setCellValue(workPackageTestCaseExecutionPlanHeader[counter]);
			}
			String executionStatus="";
			if (workPackageTestCaseExecutionPlanList != null
					&& !workPackageTestCaseExecutionPlanList.isEmpty()) {
				// TestCase and TestSteps Processing
				for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlanList) {

				//	rowNum++;
					colNum = 0;
					int celNum=1;
					// Creating Object array for holding test case data
					System.out
							.println("String array for holding test case data");

					Row workPackageTestCaseExecutionPlanrow = workPackageTestCaseExecutionPlanSheet
							.createRow(rowNum++);

					String[] workPackageTestCaseExecutionPlanValueArray = new String[workPackageTestCaseExecutionPlanColCount];
					int[] workPackageTestCaseExecutionPlanColTypeArray = new int[workPackageTestCaseExecutionPlanColCount];

					workPackageTestCaseExecutionPlanValueArray[colNum] = ""
							+ workPackageTestCaseExecutionPlan.getTestCase()
									.getTestCaseId();
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
					
					workPackageTestCaseExecutionPlanValueArray[colNum] = workPackageTestCaseExecutionPlan
							.getTestCase().getTestCaseName();
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
					
					workPackageTestCaseExecutionPlanValueArray[colNum] = workPackageTestCaseExecutionPlan
							.getTestCase().getTestCaseDescription();
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
					
					if(workPackageTestCaseExecutionPlan.getTestCase().getDecouplingCategory()!=null){	
						if (workPackageTestCaseExecutionPlan.getTestCase().getDecouplingCategory().size() != 0)
						{
							DecouplingCategory decouplingCategory = workPackageTestCaseExecutionPlan.getTestCase().getDecouplingCategory().iterator().next();
							if (decouplingCategory != null)
							{
								workPackageTestCaseExecutionPlanValueArray[colNum] = decouplingCategory.getDecouplingCategoryName();
							}
						}
					}
					else{
						workPackageTestCaseExecutionPlanValueArray[colNum] = "";
					}
					
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
					
					if(workPackageTestCaseExecutionPlan	.getRunConfiguration().getRunconfiguration()!=null)	
						workPackageTestCaseExecutionPlanValueArray[colNum] = workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getRunconfigName();
					else
						workPackageTestCaseExecutionPlanValueArray[colNum] = "";
					
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
					
					if(workPackageTestCaseExecutionPlan.getTestLead()!=null){
					workPackageTestCaseExecutionPlanValueArray[colNum] = workPackageTestCaseExecutionPlan
							.getTestLead().getLoginId();
					}else{
					workPackageTestCaseExecutionPlanValueArray[colNum] = "";
					}
					
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					if(workPackageTestCaseExecutionPlan.getTester()!=null){
					workPackageTestCaseExecutionPlanValueArray[colNum] = workPackageTestCaseExecutionPlan
							.getTester().getLoginId();
					}else{
						workPackageTestCaseExecutionPlanValueArray[colNum] ="";
					}
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					if(workPackageTestCaseExecutionPlan.getPlannedExecutionDate()!=null){
						workPackageTestCaseExecutionPlanValueArray[colNum] = workPackageTestCaseExecutionPlan.getPlannedExecutionDate().toString();
					}else{
						workPackageTestCaseExecutionPlanValueArray[colNum] ="";
					}
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					if(workPackageTestCaseExecutionPlan.getActualExecutionDate()!=null){
					workPackageTestCaseExecutionPlanValueArray[colNum] = workPackageTestCaseExecutionPlan
									.getActualExecutionDate().toString();
					}else{
						workPackageTestCaseExecutionPlanValueArray[colNum]="";
					}
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
					
					if(workPackageTestCaseExecutionPlan.getExecutionStatus()!=null){
						if(workPackageTestCaseExecutionPlan.getExecutionStatus() == 1){
							executionStatus="Assigned";
						}else if (workPackageTestCaseExecutionPlan.getExecutionStatus() == 2){
							executionStatus="Completed";
						}else if (workPackageTestCaseExecutionPlan.getExecutionStatus() == 3){
							executionStatus="NotStarted";
						}
					}else{
						executionStatus="NotStarted";
					}
					workPackageTestCaseExecutionPlanValueArray[colNum] =executionStatus;
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
					
					if(workPackageTestCaseExecutionPlan.getExecutionPriority()!=null){
						if(workPackageTestCaseExecutionPlan.getExecutionPriority().getExecutionPriorityName()!=null || workPackageTestCaseExecutionPlan.getExecutionPriority().getExecutionPriorityName().length()!=0)
							workPackageTestCaseExecutionPlanValueArray[colNum] =workPackageTestCaseExecutionPlan.getExecutionPriority().getExecutionPriorityName();
						else
							workPackageTestCaseExecutionPlanValueArray[colNum] ="NA";
					}else{
						workPackageTestCaseExecutionPlanValueArray[colNum]="";
					}
					
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
					
					if(workPackageTestCaseExecutionPlan.getIsExecuted()!=0){
						workPackageTestCaseExecutionPlanValueArray[colNum] ="No";
					}else{
						workPackageTestCaseExecutionPlanValueArray[colNum]="Yes";
					}
					
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
					

					Cell[] workPackageTestCaseExecutionPlanCells = new Cell[workPackageTestCaseExecutionPlanColCount];
					for (int colCounter = 0; colCounter < workPackageTestCaseExecutionPlanColCount; colCounter++) {
						workPackageTestCaseExecutionPlanCells[colCounter] = workPackageTestCaseExecutionPlanrow
								.createCell(celNum++);
						workPackageTestCaseExecutionPlanCells[colCounter]
								.setCellType(workPackageTestCaseExecutionPlanColTypeArray[colCounter]);

						if (workPackageTestCaseExecutionPlanCells[colCounter]
								.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							workPackageTestCaseExecutionPlanCells[colCounter]
									.setCellValue(Integer
											.parseInt(workPackageTestCaseExecutionPlanValueArray[colCounter]));
						}

						else if (workPackageTestCaseExecutionPlanCells[colCounter]
								.getCellType() == Cell.CELL_TYPE_STRING) {
							workPackageTestCaseExecutionPlanCells[colCounter]
									.setCellValue((workPackageTestCaseExecutionPlanValueArray[colCounter]));
						} else {
							workPackageTestCaseExecutionPlanCells[colCounter]
									.setCellValue((workPackageTestCaseExecutionPlanValueArray[colCounter]));
						}
					}

				}
			}

			workbook.write(fos);
			fos.close();
			status = true;

		} catch (FileNotFoundException e) {
			log.error("ERROR  ",e);
		} catch (IOException e) {
			log.error("ERROR  ",e);
		}

		return false;
	}
	
	@Override
	public String importTestCases(String excelDataFile, int productId, InputStream is) {

		// productId = 0;//For Temporary validation of code
		productMaster = productListService.getProductById(productId);		
		HashMap<String,Object> testCaseMap = readTestCasesFromExcelFile(excelDataFile, productMaster, is);
		String message =  (String) testCaseMap.get("message");
		List<TestCaseListDTO>	testCasesListDTO = (List<TestCaseListDTO>) testCaseMap.get("testcase");		
		
		if(testCasesListDTO != null && !testCasesListDTO.isEmpty()){
			HashMap<String,List<TestCaseListDTO>> tesCaseMap=verifyExistingTestCases(testCasesListDTO,productMaster);
			if (tesCaseMap.size() > 0) {
				Set<String> set = tesCaseMap.keySet();
				for (String action : set) {
					if(action.equalsIgnoreCase("Update")){
						List<TestCaseListDTO> listOfTestCaseDTOToUpdate = tesCaseMap.get(action);
						if(listOfTestCaseDTOToUpdate !=null && !listOfTestCaseDTOToUpdate.isEmpty()){
							testSuiteConfigurationService.updateTestCasesInImport(tesCaseMap.get(action), "Update");
						}
					}
					else if(action.equalsIgnoreCase("New")){
						List<TestCaseListDTO> listOfTestCaseDTOToAdd = tesCaseMap.get(action);
						if(listOfTestCaseDTOToAdd !=null && !listOfTestCaseDTOToAdd.isEmpty()){
							testSuiteConfigurationService.updateTestCasesInImport(tesCaseMap.get(action), "New");
						}
					}
				}
			}
		}else{
			log.info("No TestCase after reading from Excel");
		}
		return message;		
	}	

	public String importTestStepsFromExcel(String excelDataFile, int productId, InputStream is) {
		productMaster = productListService.getProductById(productId);
		log.info("Start Time: "+Calendar.getInstance().getTime());
		String addStatus = " ";
		
		addStatus = readTestStepsFromExcelFile(excelDataFile, productMaster, is);
		log.info("End Time: "+Calendar.getInstance().getTime());
		return addStatus;
	}
	
	
	public boolean batchProcessTestSteps(List<TestStepDTO> testStepDTOList,int productId) {
		boolean addStatus = true;
		int addedTestStepsCount = 0;
		HashMap<String,List<TestCaseStepsList>> testStepsMap=verifyExistingTestSteps(testStepDTOList,productMaster);
		if (testStepsMap.size() > 0) {
			Set<String> set = testStepsMap.keySet();
			for (String action : set) {
				if(action.equalsIgnoreCase("Update")){
					List<TestCaseStepsList> listOfTestStepsList = testStepsMap.get(action);					
					if(listOfTestStepsList !=null && !listOfTestStepsList.isEmpty()){
						testSuiteConfigurationService.updateTestCaseStepsListInImport(listOfTestStepsList,  "Update");
					}
				} else if(action.equalsIgnoreCase("New")){
					List<TestCaseStepsList> listOfTestStepsDTOToAdd = null;
					listOfTestStepsDTOToAdd = testStepsMap.get(action);
					log.info("Test Steps to Add newly: "+listOfTestStepsDTOToAdd.size());
					if(listOfTestStepsDTOToAdd !=null && !listOfTestStepsDTOToAdd.isEmpty()){
						addedTestStepsCount = testCaseService.addTestStepsBulk(listOfTestStepsDTOToAdd);
					
						/* Add teststep into mongoDB*/
						for(TestCaseStepsList testStep :listOfTestStepsDTOToAdd){
							if(testStep!=null){
								if(testStep.getTestStepId()!=null){
									mongoDBService.addTestStepsToMongoDB(testStep.getTestStepId());
								}
							}
							
						} 
						
						if (addedTestStepsCount != listOfTestStepsDTOToAdd.size()) {
							addStatus = false;	
						}
					}
				}
			}
		}
		return addStatus;
	}
	
	private HashMap<String,List<TestCaseListDTO>> verifyExistingTestCases(List<TestCaseListDTO> testCasesDTOExcel ,ProductMaster productMaster) {
		HashMap<String,List<TestCaseListDTO>> tesCaseMap= new HashMap<String,List<TestCaseListDTO>>();
		List<TestCaseListDTO> testCasesUpdate= new ArrayList<TestCaseListDTO>();
		List<TestCaseListDTO> testCasesNew= new ArrayList<TestCaseListDTO>();
		List<String> existingTestCaseCodes = null;
		existingTestCaseCodes = testCaseService.getExistingTestCaseCodes(productMaster);
		List<String> existingTestCaseNames = null;
		existingTestCaseNames = testCaseService.getExistingTestCaseNames(productMaster);
		for(TestCaseListDTO testCaseDTO: testCasesDTOExcel){
			if (testCaseDTO.getTestCaseList().getTestCaseCode() != null) {
				if (existingTestCaseCodes.contains(testCaseDTO.getTestCaseList().getTestCaseCode().trim())) {
					TestCaseList tcFromExcel = testCaseDTO.getTestCaseList();
					TestCaseList tcFromDB = testCaseService.getTestCaseByCode(testCaseDTO.getTestCaseList().getTestCaseCode().trim(), productMaster);
					tcFromExcel.setTestCaseId(tcFromDB.getTestCaseId());
					testCaseDTO.setTestCaseList(tcFromExcel);
					testCasesUpdate.add(testCaseDTO);
				} else if (existingTestCaseNames.contains(testCaseDTO.getTestCaseList().getTestCaseName().trim())) {
					testCasesUpdate.add(testCaseDTO);
				} else {
					testCasesNew.add(testCaseDTO);
				}
			}
		}
		tesCaseMap.put("Update", testCasesUpdate);
		tesCaseMap.put("New", testCasesNew);
		log.info("To Add: "+testCasesNew.size());
		log.info("To update: "+testCasesUpdate.size());
		return tesCaseMap;
	}
	

	private HashMap<String,List<TestCaseStepsList>> verifyExistingTestSteps(List<TestStepDTO> testStepsDTOExcel ,ProductMaster productMaster) {
		
		HashMap<String,List<TestCaseStepsList>> testStepsMap= new HashMap<String,List<TestCaseStepsList>>();
		List<TestCaseStepsList> testStepsUpdate= new ArrayList<TestCaseStepsList>();
		List<TestCaseStepsList> testStepsNew= new ArrayList<TestCaseStepsList>();
		
		
		for(TestStepDTO testStepDTO: testStepsDTOExcel){
			List<String> existingTestStepCodes = null;
			existingTestStepCodes = testCaseService.getExistingTestStepsCodes(testStepDTO.getTestCaseStepsList().getTestCaseList(),productMaster);
			List<String> existingTestStepNames = null;
			existingTestStepNames = testCaseService.getExistingTestStepsNames(testStepDTO.getTestCaseStepsList().getTestCaseList(),productMaster);
			
			if (testStepDTO.getTestCaseStepsList().getTestStepCode() != null) {
				if (existingTestStepCodes.contains(testStepDTO.getTestCaseStepsList().getTestStepCode().trim())) {
					TestCaseStepsList tStepFromExcel = testStepDTO.getTestCaseStepsList();
					TestCaseStepsList tStepFromDB = testCaseService.getTestCaseStepByCode(tStepFromExcel.getTestStepCode(),productMaster);
					tStepFromExcel.setTestStepId(tStepFromDB.getTestStepId());
					testStepsUpdate.add(tStepFromExcel);
				} else if (existingTestStepNames.contains(testStepDTO.getTestCaseStepsList().getTestStepName().trim())) {
					testStepsUpdate.add(testStepDTO.getTestCaseStepsList());
				} else {
					testStepsNew.add(testStepDTO.getTestCaseStepsList());
				}
			}
		}
		testStepsMap.put("Update", testStepsUpdate);
		testStepsMap.put("New", testStepsNew);
		log.info("testStepsUpdate map Size: "+testStepsUpdate.size());
		log.info("testStepsNew map Size: "+testStepsNew.size());
		return testStepsMap;
	}

	
	private HashMap<String,Object> readTestCasesFromExcelFile(String excelDataFile, ProductMaster productMaster, InputStream fis) {

		int blankRowCount = 0;
		URL url = null;		
		int validRecordCount = 0;
		int invalidRecordCount = 0;			    
		String message = " ";
		HashMap<String,Object> testCaseMap = new HashMap<String,Object>();		
		
		List<Integer> errorRows = new ArrayList<Integer>();
		TestCaseList testCase = null;
		TestCaseListDTO testCaseListDTO = null;
		List<TestCaseListDTO> listOfTestCaseDTO = new ArrayList<TestCaseListDTO>();
		int numberOfSheets = 0;
		try { // 1

			// Get the Excel file
			Workbook workbook = null;
			if (excelDataFile.endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			}
			if (excelDataFile.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fis);
			}

			// Get the worksheet containing the testcases. It will be named
			// 'TestCases'.
			// If not available, get the first worksheet.
			numberOfSheets = workbook.getNumberOfSheets();

			Sheet sheet = null;
			for (int i = 0; i < numberOfSheets; i++) {
				if (workbook.getSheetName(i).equalsIgnoreCase("TestCases")) {
					sheet = workbook.getSheetAt(i);
					break;
				}
			}
			if (sheet == null) {
				sheet = workbook.getSheetAt(0);
			}

			// Check if the sheet contains rows of testcases
			int rowCount = sheet.getPhysicalNumberOfRows();
			int colCount = 0;
			int rowNum = 0;
			int colNum = 0;
			int totalRowCount = sheet.getPhysicalNumberOfRows() - 1;
			if (rowCount < 1) {
				log.info("No rows present in the test cases worksheet");
				return null;
			} else { // 2
				for (rowNum = 0; rowNum < rowCount; rowNum++) {
					Row row;
					blankRowCount = 0;
					row = sheet.getRow(rowNum);
					if (row == null) {
						rowNum++;
						continue;
					}
					if (row.getRowNum() <= testCaseStartRow) {
						colCount = row.getLastCellNum();
						//rowNum= rowNum+1;
						continue;
					} else { // 4

						// Read the testcase info
						testCase = new TestCaseList();
						testCaseListDTO = new TestCaseListDTO();
						boolean testCaseNameIsMissing = false;
						boolean testCaseIDIsMissing = false;

						// TestCase name is mandatory. If not present skip row
						Cell cell1 = row.getCell(testCaseNameindex);
						if (isCellValid(cell1)) {
							cell1.setCellType(Cell.CELL_TYPE_STRING);
							testCase.setTestCaseName(cell1.getStringCellValue());
						} else {
							testCaseNameIsMissing = true;
						}

						Cell cell2 = row.getCell(testCaseIDIndex);
						if (isCellValid(cell2)) {
							if(cell2.getCellType() == cell2.CELL_TYPE_STRING){
								cell2.setCellType(Cell.CELL_TYPE_STRING);
								testCase.setTestCaseCode(cell2.getStringCellValue());
							}
							else if(cell2.getCellType() == cell2.CELL_TYPE_NUMERIC){
								cell2.setCellType(Cell.CELL_TYPE_NUMERIC);
								int cel2 = (int)cell2.getNumericCellValue();							
								String strCel2 = String.valueOf(cel2);
								testCase.setTestCaseCode(strCel2);
							}
							
						} else {
							testCaseIDIsMissing = true;
						}

						if (testCaseNameIsMissing || testCaseIDIsMissing) {
							blankRowCount++;
							//rowNum++;
							continue;
						}

						// Mandatory info is present. Get the rest of the
						// testcase details
						Cell cell3 = row.getCell(testCaseDescIndex);
						if (isCellValid(cell3)) {
							cell3.setCellType(Cell.CELL_TYPE_STRING);
							if (cell3.getStringCellValue() != null
									&& cell3.getStringCellValue().length() > 2000) {
								testCase.setTestCaseDescription((cell3
										.getStringCellValue()
										.substring(0, 2000)));
							} else {
								testCase.setTestCaseDescription(cell3
										.getStringCellValue());
							}
						}

						Cell cell4 = row.getCell(testCaseCreatedDateindex);
						if (isCellValid(cell4)) {
							cell4.setCellType(Cell.CELL_TYPE_STRING);
							String dateValue = cell4.getStringCellValue();
							try {
								testCase.setTestCaseCreatedDate(DateUtility.toFormatDate(dateValue));
							} catch (Exception e) {
								log.error("exception in parsing data value", e);
							}
						}

						Cell cell5 = row.getCell(testCaseLastUpdatedDateIndex);
						if (isCellValid(cell5)) {
							cell5.setCellType(Cell.CELL_TYPE_STRING);
							String updatedDate = cell5.getStringCellValue();
							try {
								
								testCase.setTestCaseLastUpdatedDate(DateUtility.toFormatDate(updatedDate));
							} catch (Exception e) {
								log.error("Error in date parsing", e);
							}
						}
						
						Cell cell6 = row.getCell(testCasePreconditionIndex);
						if (isCellValid(cell6)) {
							cell6.setCellType(Cell.CELL_TYPE_STRING);
							if (cell6.getStringCellValue() != null){
								testCase.setPreconditions(cell6.getStringCellValue());
							} 
						}
						
						Cell cell7 = row.getCell(testInputTestDataIndex);
						if (isCellValid(cell7)) {
							cell7.setCellType(Cell.CELL_TYPE_STRING);
							if (cell7.getStringCellValue() != null){
								testCase.setTestcaseinput(cell7.getStringCellValue());
							} 
						}
						
						Cell cell9 = row.getCell(testCaseExpectedOutputIndex);
						if (isCellValid(cell9)) {
							cell9.setCellType(Cell.CELL_TYPE_STRING);
							if (cell9.getStringCellValue() != null){
								testCase.setTestcaseexpectedoutput(cell9.getStringCellValue());
							} 
						}

						Cell cell10 = row.getCell(testCasePriorityIndex);
						TestCasePriority testCasePriority = null;
						if (isCellValid(cell10)) {
							cell10.setCellType(Cell.CELL_TYPE_STRING);
							String priorityName = cell10.getStringCellValue();	
							priorityName = priorityName.trim();
							if(priorityName.equals("P0")){
								priorityName = "Critical";
							}else if(priorityName.equals("P1")){
								priorityName = "High";
							}else if(priorityName.equals("P2")){
								priorityName = "Medium";
							}else if(priorityName.equals("P3")){
								priorityName = "Trivial";
							}else if(priorityName.equals("P4")){
								priorityName = "Trivial";
							} 
								
							testCasePriority = testCaseService.getPrioirtyByName(priorityName);
							if(testCasePriority != null){
								testCase.setTestCasePriority(testCasePriority);		
							}else{
								testCasePriority = new TestCasePriority();
								testCasePriority.setTestcasePriorityId(1);
								testCase.setTestCasePriority(testCasePriority);
							}
						}else{
							testCasePriority = new TestCasePriority();
							testCasePriority.setTestcasePriorityId(1);
							testCase.setTestCasePriority(testCasePriority);
						}

						Cell cell11 = row.getCell(testCaseTypeindex);
						TestcaseTypeMaster ttm = new TestcaseTypeMaster();	
						if (isCellValid(cell11)) {
							cell11.setCellType(Cell.CELL_TYPE_STRING);
							String tcType = (cell11
									.getStringCellValue());
							if(StringUtils.isNumeric(tcType)){
								int testCaseType = Integer.parseInt(tcType);													
								if (testCaseType == 0) {
									ttm.setTestcaseTypeId(1);
									testCase.setTestcaseTypeMaster(ttm);
								} else if (testCaseType == 1) {
									ttm.setTestcaseTypeId(2);
									testCase.setTestcaseTypeMaster(ttm);
								} else if (testCaseType == 2) {
									ttm.setTestcaseTypeId(3);
									testCase.setTestcaseTypeMaster(ttm);
								}
							}else {
								ttm = testCaseService.getTestcaseTypeMasterByName(tcType.trim());
								testCase.setTestcaseTypeMaster(ttm);
							}
						}else{
							ttm.setTestcaseTypeId(1);
							testCase.setTestcaseTypeMaster(ttm);
						}
						
						Cell cell12 = row.getCell(productFeatureCodeIndex);
						String productFeatureCode = null;
						if (isCellValid(cell12)) {
							productFeatureCode = cell12.getStringCellValue().trim();
							if(productFeatureCode!=null && productFeatureCode.length()!=0){
								if (productListService.isProductFeatureExistingByFeatureCode(productFeatureCode, productMaster)) {
									testCaseListDTO.setFeature(productFeatureCode);
								}else {
									//If Product Feature does not exists. Ignore the mapping
									log.info("Product Feature: "+productFeatureCode+" referred at row # "+row+" does not exists.");
								}
							}
						}
						Cell cell13 = row.getCell(executiontypeIndex);
						ExecutionTypeMaster etm = new ExecutionTypeMaster();
						String executionType = null;
						if (isCellValid(cell13)) {
							cell13.setCellType(Cell.CELL_TYPE_STRING);							
							 executionType = cell13.getStringCellValue().trim();
							if("Automated".equalsIgnoreCase(executionType)){
								etm.setExecutionTypeId(new Integer(1));
								testCase.setExecutionTypeMaster(etm);
							}else if("Manual".equalsIgnoreCase(executionType)){
								etm.setExecutionTypeId(new Integer(2));
								testCase.setExecutionTypeMaster(etm);
							}else{
								etm.setExecutionTypeId(new Integer(2));
								testCase.setExecutionTypeMaster(etm);
							}							
							
						}else{
							etm.setExecutionTypeId(new Integer(2));
							testCase.setExecutionTypeMaster(etm);
							
						}
						
						
						testCase.setProductMaster(productMaster);
						
						if(testCase.getTestCaseCreatedDate()!=null){
							testCase.setTestCaseCreatedDate(testCase.getTestCaseCreatedDate());
						}else{
							testCase.setTestCaseCreatedDate(DateUtility.getCurrentTime());
						}
						testCase.setTestCaseLastUpdatedDate(DateUtility.getCurrentTime());
						
						testCaseListDTO.setTestCaseList(testCase);
						listOfTestCaseDTO.add(testCaseListDTO);
					}
					
					Cell cell14 = row.getCell(testCaseSourceIndex);
					if (isCellValid(cell14)) {
						cell14.setCellType(Cell.CELL_TYPE_STRING);
						if (cell14.getStringCellValue() != null){
							testCase.setTestCaseSource(cell14.getStringCellValue());
						} 
					}
					
					Cell cell15 = row.getCell(testcaseScriptPkgNameIndex);
					if (isCellValid(cell15)) {
						cell15.setCellType(Cell.CELL_TYPE_STRING);
						if (cell15.getStringCellValue() != null){
							testCase.setTestCaseScriptQualifiedName(cell15.getStringCellValue());
						} 
					}
					
					Cell cell16 = row.getCell(testcaseScriptFileName);
					if (isCellValid(cell16)) {
						cell16.setCellType(Cell.CELL_TYPE_STRING);
						if (cell16.getStringCellValue() != null){
							testCase.setTestCaseScriptFileName(cell16.getStringCellValue());
						} 
					}
					
					
					testCase.setStatus(1);
					
					validRecordCount++;
				}// forloop //While loop
				invalidRecordCount =  totalRowCount - validRecordCount;
				message = "<br>Number of Records Inserted are  :"+Integer.toString(validRecordCount) +" <br> Number  Records not Inserted are :"+Integer.toString(invalidRecordCount);
				
				testCaseMap.put("message", message);
				testCaseMap.put("testcase", listOfTestCaseDTO);
				
			} // If-else
		} catch (IOException IOE) {
			log.error("Error reading testcases from file", IOE);
			IOE.printStackTrace();
			return null;
		} catch (Exception e) {
			log.error("Error reading testcases from file", e);
			return null;
		}

		listOfTestCaseDTO.get(0).toString();
		
		return testCaseMap;
	}

	private String readTestStepsFromExcelFile(String excelDataFile, ProductMaster productMaster, InputStream fis) {
					
		int blankRowCount = 0;
		URL url = null;
		log.info("Import - Excel file from location:" + excelDataFile);
		List<Integer> errorRows = new ArrayList<Integer>();
		
		List<TestStepDTO> listOfTestStepDTO = null;
		boolean batchStatus = false;
		String message = " ";
		
		int numberOfSheets = 0;
		try { // 1

			Workbook workbook = null;
			if (excelDataFile.endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			}
			if (excelDataFile.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fis);
			}

			// Get the worksheet containing the teststeps. It will be named 'TestSteps'.
			// If not available, get the first worksheet.
			numberOfSheets = workbook.getNumberOfSheets();
			if (numberOfSheets <= 0) {
				log.info("The Excel for Test Steps import does not conotain any worksheets");
			}
			Sheet sheet = null;
			for (int i = 0; i < numberOfSheets; i++) {
				if (workbook.getSheetName(i).equalsIgnoreCase("TestSteps")) {
					sheet = workbook.getSheetAt(i);
					break;
				}
			}
			if (sheet == null) {
				sheet = workbook.getSheetAt(0);
			}

			
			// Check if the sheet contains rows of teststeps
			int rowCount = sheet.getPhysicalNumberOfRows();
			int colCount = 0;
			int colNum = 0;
			int startIndex = 0;
			int endIndex = 0;
			int mod = 0;
			int numberOfCounters = 0;
			Integer maxRecordsToReadFromExcel = 0;			
			if (rowCount < 1) {
				log.info("No rows present in the test steps worksheet");
			//	return false;
			} else { // 2
				if(testStepReaderLimit == null || testStepReaderLimit.length() == 0){
					maxRecordsToReadFromExcel = 10;
				}else{
					maxRecordsToReadFromExcel = Integer.parseInt(testStepReaderLimit);
				}
				
				if(rowCount > maxRecordsToReadFromExcel){
					numberOfCounters = rowCount/maxRecordsToReadFromExcel;
					mod = rowCount%maxRecordsToReadFromExcel;
					if(mod > 0){
						numberOfCounters = numberOfCounters+1;
			    	}
					
					for(int batchCounter=1; batchCounter<=numberOfCounters; batchCounter++){
						
						if(batchCounter == 1){
			    			startIndex = 0;
			    			endIndex = batchCounter*maxRecordsToReadFromExcel;
			    		}else{
			    			startIndex = endIndex;
			    			if((rowCount-endIndex)> maxRecordsToReadFromExcel){
			    				endIndex = batchCounter*maxRecordsToReadFromExcel;
			    			}else{
			    				endIndex = rowCount;
			    				maxRecordsToReadFromExcel = endIndex-startIndex;
			    			}
			    		}
					HashMap<String,Object> testStepsMap = 	readTestSteps(sheet,startIndex,endIndex,productMaster);
					 message = (String) testStepsMap.get("message");
					listOfTestStepDTO =	(List<TestStepDTO>) testStepsMap.get("teststeps");
						batchStatus = batchProcessTestSteps(listOfTestStepDTO, productMaster.getProductId().intValue());
						if(batchStatus){
							log.info("Batch "+batchCounter+ " Start Row: "+startIndex+"  End Row: "+endIndex+" Completed successfully.");
						}else{
							log.info("Batch "+batchCounter+ " Start Row: "+startIndex+"  End Row: "+endIndex+" Failed !!!! ");
						}
					}
				}else{
					startIndex = 0;
		    		endIndex = rowCount;
		    		HashMap<String,Object> testStepsMap = 	readTestSteps(sheet,startIndex,endIndex,productMaster);
					message = (String) testStepsMap.get("message");
					listOfTestStepDTO =	(List<TestStepDTO>) testStepsMap.get("teststeps");
					
					batchStatus = batchProcessTestSteps(listOfTestStepDTO, productMaster.getProductId().intValue());
					if(batchStatus){
						log.info("Batch "+" Start Row: "+startIndex+"  End Row: "+endIndex+ " Completed successfully");
					}else{
						log.info("Batch "+" Start Row: "+startIndex+"  End Row: "+endIndex+ " Failed !!!!");
					}
					
				}
			} 
		} catch (IOException IOE) {
			log.error("Error reading testcases from file", IOE);
		} catch (Exception e) {
			log.error("Error reading testcases from file", e);
		}
		return message;
	}
	
	public HashMap<String,Object> readTestSteps(Sheet sheet,int startIndex, int endIndex, ProductMaster product){
		int validRecordCount = 0;
		int invalidRecordCount = 0;			    
		String message = " ";
		HashMap<String,Object> testStepMap = new HashMap<String,Object>();	
		
		TestCaseList testCase = null;
	    TestCaseStepsList testStep = null;
		TestStepDTO testStepDTO = null;
		List<TestStepDTO> listOfTestStepDTO = null;
		listOfTestStepDTO=new ArrayList<TestStepDTO>();		
		int totalRowCount = sheet.getPhysicalNumberOfRows() - 1;		
		Row row;
		int rowNum = 0;
		int blankRowCount = 0;
		int colCount = 0;
		for (rowNum = startIndex; rowNum < endIndex; rowNum++) {
			blankRowCount = 0;
			row = sheet.getRow(rowNum);
			if (row == null) {
				rowNum++;
				continue;
			}
			if (row.getRowNum() <= testStepsStartRow) {
				colCount = row.getLastCellNum();
				continue;
			} else { // 4

				// Read the teststeps info
				testStep = new TestCaseStepsList();
				testStepDTO = new TestStepDTO();
				boolean testStepCodeIsMissing = false;
				boolean testStepNameIsMissing = false;
				boolean testStepIDIsMissing = false;

				//Check if Testcase code ref is available. Either Test Case Code or Test case Id is mandatory.
				String testCaseCode = null;
				Cell cell11 = row.getCell(testStepTestCaseCodeIndex);
				if (isCellValid(cell11)) {
					cell11.setCellType(Cell.CELL_TYPE_STRING);
					if (cell11.getStringCellValue() != null){
						testCaseCode = cell11.getStringCellValue();
					} else {
						log.info("TestStep in row #" + rowNum + " does not have testcase Code");
						continue;
					}
				} 
				if(testCaseCode!=null){
				testCase = testCaseService.getTestCaseByCode(testCaseCode.trim(),product);
			}
				///Check if Testcase Id is available. Either Test Case Code or Test case Id is mandatory.
				String testCaseId = null;
				if (testCase == null) {
					log.info("TestStep in row #" + rowNum + " does not have a valid testcase Code : " + testCaseCode + ". Check if Test case Id is available.");
					Cell cell10 = row.getCell(testStepTestCaseIdIndex);
					if (isCellValid(cell10)) {
						cell10.setCellType(Cell.CELL_TYPE_STRING);
						if (cell10.getStringCellValue() != null){
							testCaseId = cell10.getStringCellValue();
							if(testCaseId.trim().contains(".")){
								testCaseId = testCaseId.trim().substring(0, testCaseId.indexOf("."));
							}
							log.info("testCaseId: "+testCaseId);
							testCase = testCaseService.getTestCaseById(Integer.parseInt(testCaseId));
						} else {
							log.info("TestStep in row #" + rowNum + " does not have testcase Id");
							continue;
						}
					} 
				} 
				
				if (testCase == null) {
					log.info("TestStep in row #" + rowNum + " does not even have a valid testcase Id : " + testCaseId + ". Ignoring row");
					continue;
				}
				
				
				// Test step code (Ref code from external system) or Test step Id (of iLCM) is mandatory. If any one is present not proceed or skip row
				Cell cell1 = row.getCell(testStepIDIndex);
				if (isCellValid(cell1)) {
					cell1.setCellType(Cell.CELL_TYPE_STRING);
					testStep.setTestStepId(Integer.parseInt(cell1.getStringCellValue().trim()));
				} else {
					testStepIDIsMissing = true;
				}

				// Test step code (Ref code from external system) or Test step Id (of iLCM) is mandatory. If any one is present not proceed or skip row
				Cell cell2 = row.getCell(testStepCodeindex);
				if (isCellValid(cell2)) {
					cell2.setCellType(Cell.CELL_TYPE_STRING);
					testStep.setTestStepCode(cell2.getStringCellValue().trim());
				} else {
					testStepCodeIsMissing = true;
				}
				
				// Teststep name is mandatory. If not present skip row
				Cell cell3 = row.getCell(testStepNameindex);
				if (isCellValid(cell3)) {
					cell3.setCellType(Cell.CELL_TYPE_STRING);
					testStep.setTestStepName(cell3.getStringCellValue().trim());
				} else {
					testStepNameIsMissing = true;
				}

				if (testStepNameIsMissing && testStepCodeIsMissing) {
					blankRowCount++;
					rowNum++;
					log.info("TestStep name or code is missing in row #" + rowNum + ". Ignoring row");
					continue;
				}

				// Mandatory info is present. Get the rest of the
				// testcase details
				Cell cell4 = row.getCell(testStepDescIndex);
				if (isCellValid(cell4)) {
					cell4.setCellType(Cell.CELL_TYPE_STRING);
					if (cell4.getStringCellValue() != null && cell4.getStringCellValue().length() > 2000) {
						testStep.setTestStepDescription(cell4.getStringCellValue().substring(0, 2000));
					} else {
						testStep.setTestStepDescription(cell4.getStringCellValue().trim());
					}
				}

				Cell cell5 = row.getCell(testStepCreatedDateindex);
				if (isCellValid(cell5)) {
					cell5.setCellType(Cell.CELL_TYPE_STRING);
					String dateValue = cell5.getStringCellValue();
					try {
						testStep.setTestStepCreatedDate(DateUtility.toFormatDate(dateValue));
					} catch (Exception e) {
						log.error("exception in parsing data value", e);
					}
				}

				Cell cell6 = row.getCell(testStepLastUpdatedDateIndex);
				if (isCellValid(cell6)) {
					cell6.setCellType(Cell.CELL_TYPE_STRING);
					String updatedDate = cell6.getStringCellValue();
					try {
						testStep.setTestStepLastUpdatedDate(DateUtility.toFormatDate(updatedDate));
					} catch (Exception e) {
						log.error("Error in date parsing", e);
					}
				}
				
				Cell cell7 = row.getCell(testStepInputTestDataIndex);
				if (isCellValid(cell7)) {
					cell7.setCellType(Cell.CELL_TYPE_STRING);
					if (cell7.getStringCellValue() != null){
						testStep.setTestStepInput(cell7.getStringCellValue());
					} 
				}
				
				Cell cell9 = row.getCell(testStepSourceIndex);
				if (isCellValid(cell9)) {
					cell9.setCellType(Cell.CELL_TYPE_STRING);
					if (cell9.getStringCellValue() != null){
						testStep.setTestStepSource(cell9.getStringCellValue());
					} else {
						testStep.setTestStepSource("Excel Import");
					}
				}
				
				Cell cell8 = row.getCell(testStepExpectedOutputIndex);
				if (isCellValid(cell8)) {
					cell8.setCellType(Cell.CELL_TYPE_STRING);
					if (cell8.getStringCellValue() != null){
						testStep.setTestStepExpectedOutput(cell8.getStringCellValue());
					} 
				}
				testStep.setTestCaseList(testCase);
				testStepDTO.setTestCaseStepsList(testStep);
				listOfTestStepDTO.add(testStepDTO);
				validRecordCount++;
			}
			
		}
		invalidRecordCount =  totalRowCount - validRecordCount;
		message = "<br>Number of Records Inserted are  :"+Integer.toString(validRecordCount) +"<br>  Number  Records not Inserted are :"+Integer.toString(invalidRecordCount);
		
		testStepMap.put("message", message);
		testStepMap.put("teststeps", listOfTestStepDTO);
		
		
		return testStepMap;
	}
	
	@Override
	public String importProductFeatures(String excelDataFile, int productId, InputStream is) {

		productMaster = productListService.getProductById(productId);
		
		HashMap<String,Object> productFeatureMap = readProductFeaturesFromExcelFile(excelDataFile, productMaster, is);
		String message =  (String) productFeatureMap.get("message");
		List<ProductFeatureListDTO> productFeatureListDTO = (List<ProductFeatureListDTO>) productFeatureMap.get("productFeatures");
		if(productFeatureListDTO != null && !productFeatureListDTO.isEmpty()){
			HashMap<String,List<ProductFeatureListDTO>> featuresMap = verifyExistingProductFeatures(productFeatureListDTO,productMaster);
			if (featuresMap.size() > 0) {
				Set<String> set = featuresMap.keySet();
				for (String action : set) {
					if(action.equalsIgnoreCase("Update")){
						List<ProductFeatureListDTO> listOfFeatureDTOToUpdate = featuresMap.get(action);
						if(listOfFeatureDTOToUpdate !=null && !listOfFeatureDTOToUpdate.isEmpty()){
						}
					}
					else if(action.equalsIgnoreCase("New")){
						List<ProductFeatureListDTO> listOfFeatureDTOToAdd = featuresMap.get(action);
						if(listOfFeatureDTOToAdd !=null && !listOfFeatureDTOToAdd.isEmpty()){
							productListService.productFeaturesbatchImport(listOfFeatureDTOToAdd,action);
						}
					}
				}
			}
		}else{
			log.info("No Features, after reading from Excel");
		}
		
		return message;
	}
	
	private HashMap<String,Object> readProductFeaturesFromExcelFile(String excelDataFile, ProductMaster productMaster, InputStream fis) {
		int blankRowCount = 0;
		log.info("Import - Excel file from location:" + excelDataFile);
		
		int validRecordCount = 0;
		int invalidRecordCount = 0;	
		String message = " ";
		HashMap<String,Object> productFeatureMap = new HashMap<String,Object>();		
		ProductFeature productFeature = null;
		ProductFeatureListDTO productFeatureDTO = null;
		List<ProductFeatureListDTO> listOfProductFeaturesDTO = new ArrayList<ProductFeatureListDTO>();

		int numberOfSheets = 0;
		try { // 1

			Workbook workbook = null;


		 if(! fis.markSupported()) {
			 fis = new PushbackInputStream(fis, 8);
         }

         if(POIFSFileSystem.hasPOIFSHeader(fis)) {
        	 workbook =    new HSSFWorkbook(fis);
         }
         if(POIXMLDocument.hasOOXMLHeader(fis)) {
        	 workbook =    new XSSFWorkbook(OPCPackage.open(fis));
         }
         
			// Get the worksheet containing the teststeps. It will be named 'TestSteps'.
			// If not available, get the first worksheet.
			numberOfSheets = workbook.getNumberOfSheets();
			if (numberOfSheets <= 0) {
				log.info("The Excel for Product Features import does not contain any worksheets");
				return null;
			}
			Sheet sheet = null;
			for (int i = 0; i < numberOfSheets; i++) {
				if (workbook.getSheetName(i).equalsIgnoreCase("Features")) {
					sheet = workbook.getSheetAt(i);
					break;
				}
			}
			if (sheet == null) {
				sheet = workbook.getSheetAt(0);
			}

			// Check if the sheet contains rows of teststeps
			int rowCount = sheet.getPhysicalNumberOfRows();
			int colCount = 0;
			int rowNum = 0;
			int totalRecordCount = 	sheet.getPhysicalNumberOfRows() - 1;		
			if (rowCount < 1) {
				log.info("No rows present in the Product Features import worksheet");
				return null;
			} else { // 2
				Row row;
				for (rowNum = 0; rowNum < rowCount; rowNum++) {
					//Row row;
					blankRowCount = 0;
					row = sheet.getRow(rowNum);
					if (row == null) {
						rowNum++;
						continue;
					}
					if (row.getRowNum() <= productFeatureStartRow) {
						colCount = row.getLastCellNum();
						continue;
					} else { // 4

						// Read the teststeps info
						productFeature = new ProductFeature();
						productFeatureDTO = new ProductFeatureListDTO();
						boolean featureCodeIsMissing = false;
						boolean featureNameIsMissing = false;
						boolean featureIDIsMissing = false;

						//Check if product Feature code ref is available. Either Product Feature Code or Product Feature Id is mandatory.
						String productFeatureCode = null;
						
						// Product Feature code (Ref code from external system) or Product Feature Id (of iLCM) is mandatory. If any one is present not proceed or skip row
						Cell cell1 = row.getCell(productFeatureIDIndex);
						if (isCellValid(cell1)) {
							cell1.setCellType(Cell.CELL_TYPE_STRING);
							String featureIdString = cell1.getStringCellValue();
							if(featureIdString.contains(".")){
								int index = featureIdString.indexOf('.') ;
								featureIdString=featureIdString.substring(0, index);
								
							}
							productFeature.setProductFeatureId(Integer.parseInt(featureIdString));
						} else {
							featureIDIsMissing = true;
						}

						
						
						// Product Feature code (Ref code from external system) or Product Feature Id (of iLCM) is mandatory. If any one is present not proceed or skip row
						Cell cell2 = row.getCell(productFeatureCodeindex);
						if (isCellValid(cell2)) {							
							String featureCode = formatter.formatCellValue(cell2);							
							productFeature.setProductFeatureCode(featureCode);
						} else {
							featureCodeIsMissing = true;
						}
						
						// Product Feature name is mandatory. If not present skip row
						Cell cell3 = row.getCell(productFeatureNameindex);
						if (isCellValid(cell3)) {
							String featureName = formatter.formatCellValue(cell3);	
							productFeature.setProductFeatureName(featureName);
						} else {
							featureNameIsMissing = true;
						}

						if (featureNameIsMissing && featureCodeIsMissing) {
							blankRowCount++;
							rowNum++;
							log.info("Product Feature name or code is missing in row #" + rowNum + ". Ignoring row");
							continue;
						}
						
						Cell cell4 = row.getCell(productFeatureDescIndex);
						if (isCellValid(cell4)) {
							String featureDescription = formatter.formatCellValue(cell4);	
							if (featureDescription != null
									&& featureDescription.length() > 2000) {
								productFeature.setProductFeatureDescription((featureDescription
										.substring(0, 2000)));
							} else {
								productFeature.setProductFeatureDescription(featureDescription);
							}
						}
						
						Cell cell5 = row.getCell(parentFeatureIdIndex);
						if (isCellValid(cell5)) {
							String cellValue = formatter.formatCellValue(cell5);
							if(cellValue != null && !cellValue.isEmpty()){
								int parentId = Integer.parseInt(cellValue);
								productFeatureDTO.setParentFeatureId(parentId);
							}
						} 
						
						Cell cell6 = row.getCell(parentFeatureCodeIndex);
						if (isCellValid(cell6)) {
							cell6.setCellType(Cell.CELL_TYPE_STRING);
							productFeatureDTO.setParentFeatureCode(cell6.getStringCellValue());
						}
						productFeature.setProductMaster(productMaster);
						productFeature.setSourceType(IDPAConstants.FEATURE_UPLOAD_SOURCE_TYPE);
						
						if(productFeature.getCreatedDate()==null){
							productFeature.setCreatedDate(DateUtility.getCurrentTime());
						}else{
							productFeature.setCreatedDate(productFeature.getCreatedDate());
						}
						
						
						Cell cell8 = row.getCell(productFeaturePriority);
						if (isCellValid(cell8)) {
							cell6.setCellType(Cell.CELL_TYPE_STRING);
							String executionPriorityName=cell6.getStringCellValue();
							TestCasePriority executionPriority=testCasePriorityDAO.getPrioirtyByName(executionPriorityName);
							if(executionPriority != null) {
								productFeature.setExecutionPriority(executionPriority);
							} else {
								
								executionPriority = new TestCasePriority();
								executionPriority.setTestcasePriorityId(3);
								productFeature.setExecutionPriority(executionPriority);
							}
						} else {
							TestCasePriority executionPriority = new TestCasePriority();
							executionPriority.setTestcasePriorityId(3);
							productFeature.setExecutionPriority(executionPriority);
						}
						
						
						
						productFeature.setModifiedDate(DateUtility.getCurrentTime());
						productFeatureDTO.setProductFeature(productFeature);
						listOfProductFeaturesDTO.add(productFeatureDTO);
						validRecordCount++;
					}
					
				}
				invalidRecordCount =	totalRecordCount - validRecordCount;			
			}
			message = "<br>Number of Records Inserted are  :"+Integer.toString(validRecordCount) +"<br> Number  Records not Inserted are :"+Integer.toString(invalidRecordCount);			
			productFeatureMap.put("message", message);
			productFeatureMap.put("productFeatures", listOfProductFeaturesDTO);			
		} catch (IOException IOE) {
			log.error("Error reading product features from file", IOE);
			return null;
		} catch (Exception e) {
			log.error("Error reading product features from file", e);
			return null;
		}
		return productFeatureMap;
	}
	
	private HashMap<String,List<ProductFeatureListDTO>> verifyExistingProductFeatures(List<ProductFeatureListDTO> productFeatureDTOExcel ,ProductMaster productMaster) {
		HashMap<String,List<ProductFeatureListDTO>> pfMap= new HashMap<String,List<ProductFeatureListDTO>>();
		List<ProductFeatureListDTO> pfUpdate= new ArrayList<ProductFeatureListDTO>();
		List<ProductFeatureListDTO> pfNew= new ArrayList<ProductFeatureListDTO>();
		
		for(ProductFeatureListDTO productFeatureDTO: productFeatureDTOExcel){
			ProductFeature productFeatureCode = productListService.getByProductFeatureCode(productFeatureDTO.getProductFeature().getProductFeatureCode(), productMaster);
			if(productFeatureCode == null){
				pfNew.add(productFeatureDTO);
			}else{
				pfUpdate.add(productFeatureDTO);
			}
		}
		pfMap.put("Update", pfUpdate);
		pfMap.put("New", pfNew);
		log.info("Add PF: "+pfNew.size());
		log.info("Update PF: "+pfUpdate.size());
		return pfMap;
	}

	
	
	//Modifications for integrating TAF JNJ Features to iLCM - Bug 717
	
		 //Changes for Localization Reports
		 /**
		  * This method is used for generating Localization string reports
		  * 
		  */
		 @Override
		 public Boolean exportLocaleReports(int testRunJobId, String exportLocation){
			Boolean status;			
			
			List<TestRunReportsDeviceCaseStepList> testStepReportResults   = testRunReportsDeviceCaseStepListService.getByJobId(testRunJobId);
				
			status = exportLocalizationReports(testStepReportResults, testRunJobId,exportLocation);		
				
			return status;
				
		 }
		 
		 /**
		  * This method generates Localization reports
		  * @param testExecutionResults
		  * @param testRunListId
		  * @param exportLocation
		  * @return export status
		  */
		 public Boolean exportLocalizationReports(List<TestRunReportsDeviceCaseStepList> testStepReportResults, int testRunJobId, String exportLocation){

			
			Boolean status = false;
			TestSuiteList testSuiteList;
			ProductMaster product;
			TestRunList testRunList;
			TestRunConfigurationChild testRunConfigChild;
			String productName="";
			String productVersionName= "";
			String testSuiteName="";
			int testRunNo = 0;		
			String testRunConfigName="";
			int totalTestExecResultsCount=0;
			String testRunStartTime = "";
			String testRunEndTime = "";
			String fileExportlocation = "";
			boolean isDirCreated;
			String locale = "";
			TestRunJob testRunJob;
			TestRunReportsDeviceCaseStepList testStepReportResult;
			Integer workpackageNo=0 ;
			String testRunPlanName="";
			String devicePlatformVersion="";
			String deviceModel	= "";
			String deviceMake ="";
			String deviceId="";
			String devicePlatform="";
			
			try{	
			
				testRunJob = environmentService.getTestRunJob(testRunJobId);
				if(testStepReportResults!= null & !testStepReportResults.isEmpty()){
					
					testStepReportResult = testStepReportResults.get(0);
					
					
					
					testSuiteName = testStepReportResult.getTestsuitename();					
					productName = testStepReportResult.getProductname();					
					productVersionName = testStepReportResult.getProductversionname();
					workpackageNo = testStepReportResult.getTestrunno();
					testRunPlanName = testStepReportResult.getTestrunconfigurationname();
							
					if(testRunJob!= null){
						//TestRun start and End Time
						SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy",Locale.ENGLISH);
						
						Date testRunStartDate = testRunJob.getTestRunStartTime();
						if(testRunStartDate!= null){
						testRunStartTime = sdf.format(testRunStartDate);					
						} 
						
						Date testRunEndDate = testRunJob.getTestRunEndTime();
						if(testRunEndDate!= null){
							testRunEndTime = sdf.format(testRunEndDate);					
						} 		
						List<EntityConfigurationProperties> entityConfigurationPropertiesList=entityConfigurationPropertiesService.getEntityConfigurePropertiesByEntityId(testRunJob.getRunConfiguration().getRunconfigId(),IDPAConstants.ENTITY_RUN_CONFIGURATION_ID, -1);
						if(entityConfigurationPropertiesList.size()!=0){
							EntityConfigurationProperties entityConfigurationProperties=entityConfigurationPropertiesList.get(0);
							if(entityConfigurationProperties!=null){
								locale = entityConfigurationProperties.getValue();
								log.info("Job Locale"+locale);
							}
						}else{
							List<EntityConfigurationProperties> entityConfigurationProperties=entityConfigurationPropertiesService.getEntityConfigurePropertiesByEntityId(testRunJob.getTestRunPlan().getTestRunPlanId(),IDPAConstants.ENTITY_TEST_RUN_PLAN_ID, -1);
							if(entityConfigurationProperties.size()!=0){
								EntityConfigurationProperties entityConfigurationProp=entityConfigurationProperties.get(0);
								if(entityConfigurationProp!=null){
									locale = entityConfigurationProp.getValue();
									log.info("Test Run Plan Locale"+locale);
								}
							}
							
						}
						
						
					}
					if(testRunJob.getGenericDevices()!=null){
						if(testRunJob.getGenericDevices().getPlatformType()!=null){	
							deviceId=testRunJob.getGenericDevices().getUDID();
							devicePlatformVersion = testRunJob.getGenericDevices().getPlatformType().getVersion();
							devicePlatform= testRunJob.getGenericDevices().getPlatformType().getName();
						} 
						
						if(testRunJob.getGenericDevices().getDeviceModelMaster()!=null){
							deviceModel	= testRunJob.getGenericDevices().getDeviceModelMaster().getDeviceModel();
							deviceMake = testRunJob.getGenericDevices().getDeviceModelMaster().getDeviceMakeMaster().getDeviceMake();
						}
					}
					
				}
				
				fileExportlocation = exportLocation;
				
						
				String fileName = TER_NAMING_CONV_LOCALIZATION + testRunJobId+".xlsx";
				log.info(fileName);
				
				FileOutputStream fos = new FileOutputStream(fileExportlocation+File.separator+fileName);
				
				String userName = SecurityContextHolder.getContext().getAuthentication().getName();
				
				log.info("Export Test Exec Results : Generate Excel data");
				
				XSSFWorkbook workbook = new XSSFWorkbook();
				
				//Overview Sheet
				Sheet testExecResultsOverviewSheet = workbook.createSheet("Overview");
				
				//Defects Sheet
				Sheet testExecResultsSheet = workbook.createSheet("TestExecutionResults");
				
				String[] overViewHeaders = {"Summary","Product","ProductVersion","TestSuite","TestRunNo","TestRunConfigChild","Build No", "Total Results","TestRun start time","TestRun End time","Locale"};
				
				Row[] overViewRows = new Row[overViewHeaders.length];
				
				for(int i=0;i<overViewHeaders.length; i++){
					overViewRows[i] =  testExecResultsOverviewSheet.createRow(i);
				}
				
						
				//Creating Overview Sheet Cells
				Cell cellR1 = overViewRows[0].createCell(0);	
				Cell cellR2 = overViewRows[0].createCell(1);
				
				Cell cellR3 = overViewRows[1].createCell(0);
				Cell cellR4 = overViewRows[1].createCell(1);
				
				Cell cellR5 = overViewRows[2].createCell(0);
				Cell cellR6 = overViewRows[2].createCell(1);
				
				Cell cellR7 = overViewRows[3].createCell(0);
				Cell cellR8 = overViewRows[3].createCell(1);
				
				Cell cellR9 = overViewRows[4].createCell(0);
				Cell cellR10 = overViewRows[4].createCell(1);
				
				Cell cellR11 = overViewRows[5].createCell(0);
				Cell cellR12 = overViewRows[5].createCell(1);	
				
				Cell cellR13 = overViewRows[6].createCell(0);			
				Cell cellR14 = overViewRows[6].createCell(1);
							
				Cell cellR15 = overViewRows[7].createCell(0);			
				Cell cellR16 = overViewRows[7].createCell(1);
				
				Cell cellR17 = overViewRows[8].createCell(0);			
				Cell cellR18 = overViewRows[8].createCell(1);
				
				Cell cellR19 = overViewRows[9].createCell(0);			
				Cell cellR20 = overViewRows[9].createCell(1);
				
				
				Cell cellR21 = overViewRows[9].createCell(0);			
				Cell cellR22 = overViewRows[9].createCell(1);
				//Setting Overview Sheet Cell Values
				//Summary Row
				cellR1.setCellValue("iLCM Execution Results Summary");
				cellR2.setCellValue(new Date(System.currentTimeMillis()).toString());			
				
				//Product Details
				cellR3.setCellValue("Product");			
				cellR4.setCellValue(productName);
				
				//Product Version
				cellR5.setCellValue("Product Version");			
				cellR6.setCellValue(productVersionName);
				
				//TestSuite
				cellR7.setCellValue("TestSuite");			
				cellR8.setCellValue(testSuiteName);
							
				//Test Run No
				cellR9.setCellValue("Test Run No");
				cellR10.setCellValue(workpackageNo);
				
				//Test Run Configuration
				cellR11.setCellValue("Test Run Configuration");
				cellR12.setCellValue(testRunConfigName);
				
				//Build No
				cellR13.setCellValue("Build No");
				cellR14.setCellValue("");
				
				
				//Total Records
				cellR15.setCellValue("Total Results");
				//cellR16.setCellValue();
				
				//Test Run Start Time
				cellR17.setCellValue("Test Run Start Time");
				cellR18.setCellValue(testRunStartTime);

				//Test Run End Time
				cellR19.setCellValue("Test Run End Time");
				cellR20.setCellValue(testRunStartTime);		
				
				cellR21.setCellValue("Locale");
				cellR22.setCellValue(locale);	
				
				//TestExec Results Sheet Locale Header
				
				String[] localeHeader = {"deviceId","deviceplatform","deviceplatformVer","Locale"};
				
				
				int localeHeaderCount = localeHeader.length;

				int testExecResultsRowNum=0;
				Row localeHeaderRow =  testExecResultsSheet.createRow(testExecResultsRowNum);
				
				Cell localeHeaderCell1 = localeHeaderRow.createCell(0);	
				localeHeaderCell1.setCellValue("deviceId:");
				Cell localeHeaderCell2 = localeHeaderRow.createCell(1);
				
				Cell localeHeaderCell3 = localeHeaderRow.createCell(2);
				localeHeaderCell3.setCellValue("deviceplatform:");
				Cell localeHeaderCell4 = localeHeaderRow.createCell(3);
				
				Cell localeHeaderCell5 = localeHeaderRow.createCell(4);
				localeHeaderCell5.setCellValue("deviceplatformVer:");
				Cell localeHeaderCell6 = localeHeaderRow.createCell(5);
				
				Cell localeHeaderCell7 = localeHeaderRow.createCell(6);
				localeHeaderCell7.setCellValue("Locale:");
				Cell localeHeaderCell8 = localeHeaderRow.createCell(7);
				
					if(deviceId!=null){
						localeHeaderCell2.setCellValue(deviceId);
					} else {
						localeHeaderCell2.setCellValue("");
					}
					
					if(devicePlatform!=null){
						localeHeaderCell4.setCellValue(devicePlatform);
					} else {
						localeHeaderCell4.setCellValue("");
					}
					
					if(devicePlatformVersion!=null){
						localeHeaderCell6.setCellValue(devicePlatformVersion);
					} else {
						localeHeaderCell6.setCellValue("");
					}
				//}
				
				if(locale!= null){
				localeHeaderCell8.setCellValue(locale);
				} else {
					localeHeaderCell8.setCellValue("");
				}
				
				
				
				//TestExec Results Sheet Header
				String[] testExecResultsHeader = {"runNo","testCaseID","testCaseName","testStepId","testStepName","testStepDescription","testStepInput","testStepExpectedOutput","testStepObservedOutput","testResultStatus",
						"DeviceId","DeviceModel","DeviceMake","DevicePlatformName","DevicePlatformVersion","screenShotPath","startTime","endTime","failureReason","executionRemarks","ExecutedBy"};
				
				int testExecResultsColCount = testExecResultsHeader.length;

				testExecResultsRowNum = testExecResultsRowNum+2;
				
				
				Row testExecResultsHeaderRow =  testExecResultsSheet.createRow(testExecResultsRowNum);
					
				//Setting defect header values
				Cell[] testExecResultsHeaderArray = new Cell[testExecResultsColCount];
				
				for(int counter = 0; counter<testExecResultsColCount;counter++){
					testExecResultsHeaderArray[counter] =  testExecResultsHeaderRow.createCell(counter);
					testExecResultsHeaderArray[counter].setCellValue(testExecResultsHeader[counter]);
				 }
				
				//Processing defects list
				if(testStepReportResults != null && !testStepReportResults.isEmpty()){
					totalTestExecResultsCount = testStepReportResults.size();	
					cellR16.setCellValue(totalTestExecResultsCount);
					
					
				//Creating defects row for each defect
				for(TestRunReportsDeviceCaseStepList testStepResults: testStepReportResults ){
					
					int testExecResultsColNum = 0;
					if(testStepResults!=null && (testStepResults.getTeststepinput() != null && testStepResults.getTeststepinput() !="")){
						testExecResultsRowNum = testExecResultsRowNum+1;
						
						Row testExecResultsRow = testExecResultsSheet.createRow(testExecResultsRowNum);	
						
						log.info("testExecResultsColNum:" + testExecResultsColNum);				
											
						//Forming  String array for ExecResults column types
						String[] testExecResultsValueArray = new String[testExecResultsColCount];	
						int[] testExecResultsColTypeArray = new int[testExecResultsColCount];
						//TestRunNo
						if(testStepResults.getTestrunno()!=null){
						testExecResultsValueArray[testExecResultsColNum] = testStepResults.getTestrunno().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
							
						//TestCaseId
						if(testStepResults.getTestcaseid()!= null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getTestcaseid().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//Added for Localization Reports - starts
						//Test Case Name
						if(testStepResults.getTestcasename() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getTestcasename();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//TestStepId
						if(testStepResults.getTeststepid()!= null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getTeststepid().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//Added for Localization Reports - Ends
						
						
						//TestStepName
						
						
						if(testStepResults.getTeststepname() !=null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getTeststepname();
						} else{
							testExecResultsValueArray[testExecResultsColNum] ="";
						}					
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;	
						
						//TestStepDescription
						
						if (testStepResults.getTeststepdescription() !=null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getTeststepdescription();
						} else{
							testExecResultsValueArray[testExecResultsColNum] = "";
						}					
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//TestStepInput
						
						if(testStepResults.getTeststepinput() != null){
							testExecResultsValueArray[testExecResultsColNum] =testStepResults.getTeststepinput();
						} else{
							testExecResultsValueArray[testExecResultsColNum] = "";
						}					
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//TestStepExpectedOutput
						if(testStepResults.getTeststepexpectedoutput() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getTeststepexpectedoutput();
						} else{
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//TestStepObservedOutput
						if(testStepResults.getTeststepobservedoutput() !=null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getTeststepobservedoutput().toString();
						} else{
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						//TestRun Status
						if(testStepResults.getTeststepresult() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getTeststepresult();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						 
						//DeviceId
						if(testStepResults.getDeviceid()!= null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getDeviceid();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//DeviceModel
						if(deviceModel!=""){
							testExecResultsValueArray[testExecResultsColNum] = deviceModel;
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//DeviceMake 
						if(deviceMake!= ""){
							testExecResultsValueArray[testExecResultsColNum] = deviceMake;
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						 
						//Device Platform
						if(testStepResults.getTestenvironmentname() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getTestenvironmentname();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
					
						//DevicePlatform Version
						if(devicePlatformVersion!= null && devicePlatformVersion!= ""){
							testExecResultsValueArray[testExecResultsColNum] = devicePlatformVersion;
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//Screenshot path
						if(testStepResults.getScreenshotpath() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getScreenshotpath();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_BLANK;
						 
						//TestExecStartTime
						if(testStepResults.getTeststepstarttime() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getTeststepstarttime().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						 
						//TestExecEndTime
						if(testStepResults.getTeststependtime() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getTeststependtime().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						//FailureReason
						if(testStepResults.getFailurereason()!= null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getFailurereason();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//Remarks
						if(testStepResults.getExecutionremarks() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResults.getExecutionremarks();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//ExecutedBy						
						testExecResultsValueArray[testExecResultsColNum] = userName;
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;				
					
					//ExecutedBy									
						
						//Setting Values in cells 
						Cell[] testExecResultCells = new Cell[testExecResultsColCount];
						
						for(int colCounter = 0; colCounter< testExecResultsColCount;colCounter++){
							testExecResultCells[colCounter] = testExecResultsRow.createCell(colCounter);
							testExecResultCells[colCounter].setCellType(testExecResultsColTypeArray[colCounter]);
							
							if(testExecResultCells[colCounter].getCellType() == Cell.CELL_TYPE_NUMERIC){
								testExecResultCells[colCounter].setCellValue(Integer.parseInt(testExecResultsValueArray[colCounter]));
							}
							
							else if(testExecResultCells[colCounter].getCellType() == Cell.CELL_TYPE_STRING){
								testExecResultCells[colCounter].setCellValue((testExecResultsValueArray[colCounter]));
							}													
							
						}
						
					} else if(testStepReportResults==null ){
						log.info("No Localization Result");
					}
						
				}
			}
			
					workbook.write(fos);
				
				ZipTool.dozip(fileExportlocation);
				fos.close();
				
			} catch (FileNotFoundException e) {
				log.error("error in genrating excel test exec results: File not found error", e);
				log.error(e.getMessage());
			} catch (IOException e) {
				log.error("error in genrating excel test exec results: File not found error", e);
				log.error(e.getMessage());
			} catch (Exception e){
				log.error("Exception in generating Excel reports for Test exec results", e); 
			}		
			log.info("Excel reports generated successfully");
			status = true;
			return status;
		}
	
		@Override
		public boolean exportTestResults(int testRunJobId, String exportLocation) {	
			
			boolean status;			
			
			List<TestRunReportsDeviceCaseStepList> testStepReportResults   = testRunReportsDeviceCaseStepListService.getByJobId(testRunJobId);
			status = generateAndExportTestExecResults(testStepReportResults, testRunJobId,exportLocation);		
			
			return status;
			
		}
		
		public Boolean generateAndExportTestExecResults(List<TestRunReportsDeviceCaseStepList> testStepReportResults, int testRunJobId, String exportLocation){

			
			Boolean status = false;
			ProductMaster product;
			TestRunJob testRunJob;
			TestRunReportsDeviceCaseStepList testStepReportResult;
			
			String productName="";
			String productVersionName= "";
			String testSuiteName="";
			int workpackageNo = 0;	
			String testRunPlanName="";
			int totalTestExecResultsCount=0;
			String testRunStartTime = "";
			String testRunEndTime = "";
			String fileExportlocation = "";
			boolean isDirCreated;
			String devicePlatformVersion="";
			String deviceModel;
			String deviceMake;
			
			try{	
				testRunJob = environmentService.getTestRunJob(testRunJobId);
				if(testStepReportResults!= null & !testStepReportResults.isEmpty()){
					
					testStepReportResult = testStepReportResults.get(0);
					
					
					
					testSuiteName = testStepReportResult.getTestsuitename();					
					productName = testStepReportResult.getProductname();					
					productVersionName = testStepReportResult.getProductversionname();
					workpackageNo = testStepReportResult.getTestrunno();
					testRunPlanName = testStepReportResult.getTestrunconfigurationname();
							
					if(testRunJob!= null){
						//TestRun start and End Time
						SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy",Locale.ENGLISH);
						
						Date testRunStartDate = testRunJob.getTestRunStartTime();
						if(testRunStartDate!= null){
						testRunStartTime = sdf.format(testRunStartDate);					
						} 
						
						Date testRunEndDate = testRunJob.getTestRunEndTime();
						if(testRunEndDate!= null){
							testRunEndTime = sdf.format(testRunEndDate);					
						} 		
						
					}
					if(testRunJob.getGenericDevices()!=null){
						if(testRunJob.getGenericDevices().getPlatformType()!=null){					
							devicePlatformVersion = testRunJob.getGenericDevices().getPlatformType().getVersion();
						} 
						
						if(testRunJob.getGenericDevices().getDeviceModelMaster()!=null){
							deviceModel	= testRunJob.getGenericDevices().getDeviceModelMaster().getDeviceModel();
							deviceMake = testRunJob.getGenericDevices().getDeviceModelMaster().getDeviceMakeMaster().getDeviceMake();
						}
					}
					
				}
				fileExportlocation = exportLocation;
				isDirCreated = checkAndCreateTempDirectory(fileExportlocation);
				log.info("is Directory Created:"+isDirCreated);
				
				File srcFolder = null;
				
				if(System.getProperty("os.name").contains("Linux")){
					srcFolder = new File( CommonUtility.getCatalinaPath() + IDPAConstants.EVIDENCE_PATH_LINUX+"/"+testRunJobId+"/"
							+IDPAConstants.EVIDENCE_SCREENSHOT);
				} else {
					srcFolder = new File( CommonUtility.getCatalinaPath() + IDPAConstants.EVIDENCE_PATH+File.separator+testRunJobId+File.separator
							+IDPAConstants.EVIDENCE_SCREENSHOT);
				}
				
				File destFolder = new File(fileExportlocation+File.separator+TAFConstants.EVIDENCE_SCREENSHOT);
				
				if(!srcFolder.exists()){
					
			          log.info("Directory does not exist.");
		        }
		    	else{ 
		            try{		            	
		         	copyFolder(srcFolder,destFolder);
		            }catch(IOException e){
		            	log.error("ERROR  ",e);
		                 System.exit(0);
		            }
		         }
						
				String fileName = TER_NAMING_CONV + testRunJobId+".xlsx";
				log.debug(exportLocation);
				
				FileOutputStream fos = new FileOutputStream(fileExportlocation+File.separator+fileName);
				
				String userName = SecurityContextHolder.getContext().getAuthentication().getName();
				
				log.info("Export Test Exec Results : Generate Excel data");
				
				XSSFWorkbook workbook = new XSSFWorkbook();
				
				//Overview Sheet
				Sheet testExecResultsOverviewSheet = workbook.createSheet("Overview");
				
				//Defects Sheet
				Sheet testExecResultsSheet = workbook.createSheet("TestExecutionResults");
				
				String[] overViewHeaders = {"Summary","Product","ProductVersion","TestSuite","WorkPackage","TestRunPlan","Build No","Total Results","TestRun start time","TestRun End time"};
				
				Row[] overViewRows = new Row[overViewHeaders.length];
				
				for(int i=0;i<overViewHeaders.length; i++){
					overViewRows[i] =  testExecResultsOverviewSheet.createRow(i);
				}
				
						
				//Creating Overview Sheet Cells
				Cell cellR1 = overViewRows[0].createCell(0);	
				Cell cellR2 = overViewRows[0].createCell(1);
				
				Cell cellR3 = overViewRows[1].createCell(0);
				Cell cellR4 = overViewRows[1].createCell(1);
				
				Cell cellR5 = overViewRows[2].createCell(0);
				Cell cellR6 = overViewRows[2].createCell(1);
				
				Cell cellR7 = overViewRows[3].createCell(0);
				Cell cellR8 = overViewRows[3].createCell(1);
				
				Cell cellR9 = overViewRows[4].createCell(0);
				Cell cellR10 = overViewRows[4].createCell(1);
				
				Cell cellR11 = overViewRows[5].createCell(0);
				Cell cellR12 = overViewRows[5].createCell(1);	
				
				Cell cellR13 = overViewRows[6].createCell(0);			
				Cell cellR14 = overViewRows[6].createCell(1);
							
				Cell cellR15 = overViewRows[7].createCell(0);			
				Cell cellR16 = overViewRows[7].createCell(1);
				
				Cell cellR17 = overViewRows[8].createCell(0);			
				Cell cellR18 = overViewRows[8].createCell(1);
				
				Cell cellR19 = overViewRows[9].createCell(0);			
				Cell cellR20 = overViewRows[9].createCell(1);
				
				
				//Setting Overview Sheet Cell Values
				//Summary Row
				cellR1.setCellValue("iLCM Execution Results Summary");
				cellR2.setCellValue(new Date(System.currentTimeMillis()).toString());			
				
				//Product Details
				cellR3.setCellValue("Product");			
				cellR4.setCellValue(productName);
				
				//Product Version
				cellR5.setCellValue("Product Version");			
				cellR6.setCellValue(productVersionName);
				
				//TestSuite
				cellR7.setCellValue("TestSuite");			
				cellR8.setCellValue(testSuiteName);
							
				//Test Run No
				cellR9.setCellValue("WorkPackage");
				cellR10.setCellValue(workpackageNo);
				
				//Test Run Configuration
				cellR11.setCellValue("Test Run Plan");
				cellR12.setCellValue(testRunPlanName);
				
				//Build No
				cellR13.setCellValue("Build No");
				cellR14.setCellValue("");
				
				
				//Total Records
				cellR15.setCellValue("Total Results");
				//cellR16.setCellValue();
				
				//Test Run Start Time
				cellR17.setCellValue("Test Run Start Time");
				cellR18.setCellValue(testRunStartTime);

				//Test Run End Time
				cellR19.setCellValue("Test Run End Time");
				cellR20.setCellValue(testRunStartTime);		
				
				//TestExec Results Sheet Header
				String[] testExecResultsHeader = {"testRunJobId","testCaseId","testCaseName","testStepId","testStepName","testStepDescription","testStepInput","testStepExpectedOutput","testStepObservedOutput","testResultStatus",
						"DeviceId","DevicePlatformName","DevicePlatformVersion","screenShotPath","startTime","endTime","failureReason","executionRemarks","ExecutedBy"};
				
				int testExecResultsColCount = testExecResultsHeader.length;

				int testExecResultsRowNum=0;
				
				
				Row testExecResultsHeaderRow =  testExecResultsSheet.createRow(0);
					
				//Setting defect header values
				Cell[] testExecResultsHeaderArray = new Cell[testExecResultsColCount];
				
				for(int counter = 0; counter<testExecResultsColCount;counter++){
					testExecResultsHeaderArray[counter] =  testExecResultsHeaderRow.createCell(counter);
					testExecResultsHeaderArray[counter].setCellValue(testExecResultsHeader[counter]);
				 }
				
				//Processing defects list
				if(testStepReportResults != null && !testStepReportResults.isEmpty()){
					totalTestExecResultsCount = testStepReportResults.size();	
					cellR16.setCellValue(totalTestExecResultsCount);
					
					
				//Creating defects row for each defect
				for(TestRunReportsDeviceCaseStepList testStepResult: testStepReportResults ){
					
					testExecResultsRowNum = testExecResultsRowNum+4;
					int testExecResultsColNum = 0;
					if(testStepResult!=null ){
						Row testExecResultsRow = testExecResultsSheet.createRow(testExecResultsRowNum);	
						
						log.info("testExecResultsColNum:" + testExecResultsColNum);				
											
						//Forming String array for ExecResults column values
						//Forming  String array for ExecResults column types
						String[] testExecResultsValueArray = new String[testExecResultsColCount];	
						int[] testExecResultsColTypeArray = new int[testExecResultsColCount];
						
						//TestRunJob Id
						if(testStepResult.getTestrunlistid()!=null){
						testExecResultsValueArray[testExecResultsColNum] = testStepResult.getTestrunlistid().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
							
						//TestCase Id
						if(testStepResult.getTestcaseid() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResult.getTestcaseid().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						
						//TestCase Name
						if(testStepResult.getTestcasename() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResult.getTestcasename();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//TestStep Id
						if(testStepResult.getTeststepid() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResult.getTeststepid().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//TestStep Name
						if(testStepResult.getTeststepname()!= null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResult.getTeststepname();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//TestStep Description
						if(testStepResult.getTeststepdescription() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResult.getTeststepdescription();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//TestStep Input
						if(testStepResult.getTeststepinput() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResult.getTeststepinput();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//TestStep ExpectedOutput
						if(testStepResult.getTeststepexpectedoutput() != null){
							testExecResultsValueArray[testExecResultsColNum] =testStepResult.getTeststepexpectedoutput();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						
						//TestStep ObservedOutput
						if(testStepResult.getTeststepobservedoutput()!= null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResult.getTeststepobservedoutput();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//TestRun Status
						if(testStepResult.getTeststepresult()!= null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResult.getTeststepresult();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						 
						//DeviceId
						if(testStepResult.getDeviceid()!= null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResult.getDeviceid();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						 
						//Device Platform
						if(testStepResult.getTestenvironmentname()!= null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResult.getTestenvironmentname();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
					
						//DevicePlatform Version
						if(devicePlatformVersion!= null && devicePlatformVersion!= ""){
							testExecResultsValueArray[testExecResultsColNum] = devicePlatformVersion;
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//Screenshot path
						if(testStepResult.getScreenshotpath() != null){
							testExecResultsValueArray[testExecResultsColNum] = CommonUtility.getCatalinaPath()+File.separator+testStepResult.getScreenshotpath();
							
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_BLANK;
						 
						//TestExecStartTime
						if(testStepResult.getTeststepstarttime() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResult.getTeststepstarttime().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						 
						//TestExecEndTime
						if(testStepResult.getTeststependtime() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResult.getTeststependtime().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						//FailureReason
						if(testStepResult.getFailurereason() != null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResult.getFailurereason();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						//Remarks
						if(testStepResult.getExecutionremarks()!= null){
							testExecResultsValueArray[testExecResultsColNum] = testStepResult.getExecutionremarks();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
						//ExecutedBy						
						testExecResultsValueArray[testExecResultsColNum] = userName;
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;				
					
						
						//Setting Values in cells 
						Cell[] testExecResultCells = new Cell[testExecResultsColCount];
						
						for(int colCounter = 0; colCounter< testExecResultsColCount;colCounter++){
							testExecResultCells[colCounter] = testExecResultsRow.createCell(colCounter);
							testExecResultCells[colCounter].setCellType(testExecResultsColTypeArray[colCounter]);
							
							if(testExecResultCells[colCounter].getCellType() == Cell.CELL_TYPE_NUMERIC){
								testExecResultCells[colCounter].setCellValue(Integer.parseInt(testExecResultsValueArray[colCounter]));
							}
							
							else if(testExecResultCells[colCounter].getCellType() == Cell.CELL_TYPE_STRING){
								testExecResultCells[colCounter].setCellValue((testExecResultsValueArray[colCounter]));
							}	else if(testExecResultCells[colCounter].getCellType() == Cell.CELL_TYPE_BLANK){
								
								int index =  testExecResultsValueArray[colCounter].lastIndexOf(File.separator);
								String screenshotValue = testExecResultsValueArray[colCounter].substring(index+1);
								 
								 
								if(screenshotValue != null && !screenshotValue.equalsIgnoreCase("null") ){
									 
									try{
									CellStyle hlink_style = workbook.createCellStyle();
								    Font hlink_font = workbook.createFont();
								    
								    CreationHelper helper = workbook.getCreationHelper();
								    hlink_font.setUnderline(Font.U_SINGLE);
								    hlink_font.setColor(IndexedColors.AUTOMATIC.getIndex());
								    hlink_style.setFont(hlink_font);
								    Hyperlink hp = helper.createHyperlink(Hyperlink.LINK_FILE);
								    log.info("Value is:"+screenshotValue);
								 
								    String fileAddress = "./SCREENSHOT"+"//"+screenshotValue;						    
								    log.info("screenshot path:" + fileAddress);
								    
								    
								    hp.setAddress(fileAddress);
								    hp.setLabel("Image");
								    
								    testExecResultCells[colCounter].setCellStyle(hlink_style);
									testExecResultCells[colCounter].setCellValue((testExecResultsValueArray[colCounter]));							   
									testExecResultCells[colCounter].setHyperlink(hp);							  
									File file = new File(srcFolder+"//"+screenshotValue);
								    InputStream inputStream = new FileInputStream(file);
								    
									   //Get the contents of an InputStream as a byte[].
									   byte[] bytes = IOUtils.toByteArray(inputStream);
									   //Adds a picture to the workbook
									   int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
									   //close the input stream
									   inputStream.close();
									 
									   //Returns an object that handles instantiating concrete classes
									   //CreationHelper helper = wb.getCreationHelper();
									 
									   //Creates the top-level drawing patriarch.
									   Drawing drawing = testExecResultsSheet.createDrawingPatriarch();
									 
									   //Create an anchor that is attached to the worksheet
									   ClientAnchor anchor = helper.createClientAnchor();
									   //set top-left corner for the image
									   anchor.setCol1(testExecResultsColNum++);
									   anchor.setRow1(testExecResultsRowNum);
									   anchor.setCol2(testExecResultsColNum);
									   anchor.setRow2(testExecResultsRowNum+3);
									   //Creates a picture
									   Picture pict = drawing.createPicture(anchor, pictureIdx);
									   pict.resize();
									} catch(Exception e){
										log.error("Error in setting screenshots in Excel reports : " + e.getMessage());
									}
								}else {
									testExecResultCells[colCounter].setCellValue("Screenshot-Not-Available");
									log.info("Screenshot path is not available");
								}
							}													
							
						}
						
					}
						
				}
			}
			
			workbook.write(fos);
			
			String serverFolderPath = null;
			if(System.getProperty("os.name").contains("Linux")){
				serverFolderPath= CommonUtility.getCatalinaPath() + IDPAConstants.TESTRUNJOBREPORTS_EXCELPATH_LINUX+"/"+testRunJobId;
			} else {
				serverFolderPath= CommonUtility.getCatalinaPath() + IDPAConstants.TESTRUNJOBREPORTS_EXCELPATH+File.separator+testRunJobId;
			}
								
			ZipTool.dozip(serverFolderPath);
			fos.close();
				
			} catch (FileNotFoundException e) {
				log.error("error in genrating excel test exec results: File not found error", e);
				log.error(e.getMessage());
			} catch (IOException e) {
				log.error("error in genrating excel test exec results: File not found error", e);
				log.error(e.getMessage());
			} catch (Exception e){
				log.error("Exception in generating Excel reports for Test exec results"+e); 
			}		
			log.info("Excel reports generated successfully");
			status = true;
			return status;
		}

		 //Changes for Requirements Reports
		 /**
		  * This method is used for generating Features(Requirements) reports
		  * 
		  */
		 @Override
		 public Boolean exportFeatureReports(int testRunJobId, String exportLocation){
			
			List<TestRunReportsDeviceCaseList> testCaseResults   = testRunReportsDeviceCaseListService.getTestRunReportsByTestRunJobId(testRunJobId);
			status = exportFeaturesReport(testCaseResults, testRunJobId,exportLocation);		
				
			return status;
				
		 }
		 
		
		 /**
		  * This method generates Features reports
		  * @param testExecutionResults
		  * @param testRunListId
		  * @param exportLocation
		  * @return export status
		  */
		
		public Boolean exportFeaturesReport(List<TestRunReportsDeviceCaseList> testCaseReportResults, int testRunJobId, String exportLocation){

			
			Boolean status = false;
			ProductMaster product;
			TestRunJob testRunJob;
			TestRunReportsDeviceCaseList testCaseReportResult;
			
			String productName="";
			String productVersionName= "";
			String testSuiteName="";
			int workpackageNo = 0;	
			String testRunPlanName="";
			int totalTestExecResultsCount=0;
			String testRunStartTime = "";
			String testRunEndTime = "";
			boolean isDirCreated;
			String devicePlatformVersion="";
			String deviceModel;
			String deviceMake;
			
			List<ProductFeature> prodFeatures = null;
			
			try{	
				testRunJob = environmentService.getTestRunJob(testRunJobId);
				if(testCaseReportResults!= null & !testCaseReportResults.isEmpty()){
					
					testCaseReportResult = testCaseReportResults.get(0);
					
					
					
					testSuiteName = testCaseReportResult.getTestSuiteName();					
					productName = testCaseReportResult.getProductName();					
					productVersionName = testCaseReportResult.getProductVersionName();
					workpackageNo = testCaseReportResult.getTestRunNo();
					testRunPlanName = testCaseReportResult.getTestRunconfigurationName();
							
					if(testRunJob!= null){
						//TestRun start and End Time
						SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy",Locale.ENGLISH);
						
						Date testRunStartDate = testRunJob.getTestRunStartTime();
						if(testRunStartDate!= null){
						testRunStartTime = sdf.format(testRunStartDate);					
						} 
						
						Date testRunEndDate = testRunJob.getTestRunEndTime();
						if(testRunEndDate!= null){
							testRunEndTime = sdf.format(testRunEndDate);					
						} 		
						
					}
					if(testRunJob.getGenericDevices()!=null){
						if(testRunJob.getGenericDevices().getPlatformType()!=null){					
							devicePlatformVersion = testRunJob.getGenericDevices().getPlatformType().getVersion();
						} 
						
						if(testRunJob.getGenericDevices().getDeviceModelMaster()!=null){
							deviceModel	= testRunJob.getGenericDevices().getDeviceModelMaster().getDeviceModel();
							deviceMake = testRunJob.getGenericDevices().getDeviceModelMaster().getDeviceMakeMaster().getDeviceMake();
						}
					}
					
					
					
				}
				
				log.info(exportLocation);
				
				FileOutputStream fos = new FileOutputStream(exportLocation);
				
				String userName = SecurityContextHolder.getContext().getAuthentication().getName();
				
				log.info("Export Test Exec Results : Generate Excel data");
				
				XSSFWorkbook workbook = new XSSFWorkbook();
				
				//Overview Sheet
				Sheet testExecResultsOverviewSheet = workbook.createSheet("Overview");
				
				//Defects Sheet
				Sheet testExecResultsSheet = workbook.createSheet("TestExecutionResults");
				
				String[] overViewHeaders = {"Summary","Product","ProductVersion","TestSuite","WorkPackage","TestRunPlan","Build No","Total Results","TestRun start time","TestRun End time"};
				
				Row[] overViewRows = new Row[overViewHeaders.length];
				
				for(int i=0;i<overViewHeaders.length; i++){
					overViewRows[i] =  testExecResultsOverviewSheet.createRow(i);
				}
				
						
				//Creating Overview Sheet Cells
				Cell cellR1 = overViewRows[0].createCell(0);	
				Cell cellR2 = overViewRows[0].createCell(1);
				
				Cell cellR3 = overViewRows[1].createCell(0);
				Cell cellR4 = overViewRows[1].createCell(1);
				
				Cell cellR5 = overViewRows[2].createCell(0);
				Cell cellR6 = overViewRows[2].createCell(1);
				
				Cell cellR7 = overViewRows[3].createCell(0);
				Cell cellR8 = overViewRows[3].createCell(1);
				
				Cell cellR9 = overViewRows[4].createCell(0);
				Cell cellR10 = overViewRows[4].createCell(1);
				
				Cell cellR11 = overViewRows[5].createCell(0);
				Cell cellR12 = overViewRows[5].createCell(1);	
				
				Cell cellR13 = overViewRows[6].createCell(0);			
				Cell cellR14 = overViewRows[6].createCell(1);
							
				Cell cellR15 = overViewRows[7].createCell(0);			
				Cell cellR16 = overViewRows[7].createCell(1);
				
				Cell cellR17 = overViewRows[8].createCell(0);			
				Cell cellR18 = overViewRows[8].createCell(1);
				
				Cell cellR19 = overViewRows[9].createCell(0);			
				Cell cellR20 = overViewRows[9].createCell(1);
				
				
				//Setting Overview Sheet Cell Values
				//Summary Row
				cellR1.setCellValue("iLCM Execution Results Summary");
				cellR2.setCellValue(new Date(System.currentTimeMillis()).toString());			
				
				//Product Details
				cellR3.setCellValue("Product");			
				cellR4.setCellValue(productName);
				
				//Product Version
				cellR5.setCellValue("Product Version");			
				cellR6.setCellValue(productVersionName);
				
				//TestSuite
				cellR7.setCellValue("TestSuite");			
				cellR8.setCellValue(testSuiteName);
							
				//Test Run No
				cellR9.setCellValue("WorkPackage");
				cellR10.setCellValue(workpackageNo);
				
				//Test Run Configuration
				cellR11.setCellValue("Test Run Plan");
				cellR12.setCellValue(testRunPlanName);
				
				//Build No
				cellR13.setCellValue("Build No");
				cellR14.setCellValue("");
				
				
				//Total Records
				cellR15.setCellValue("Total Results");
				
				//Test Run Start Time
				cellR17.setCellValue("Test Run Start Time");
				cellR18.setCellValue(testRunStartTime);

				//Test Run End Time
				cellR19.setCellValue("Test Run End Time");
				cellR20.setCellValue(testRunStartTime);		
				
				//-------Changes----------start
				
				String[] testExecResultsHeader = {"TestRunListId","testCaseId","testCaseName","testCaseDescription","testResultStatus","productFeatureCode-Name"};
				
				
				int testExecResultsColCount = testExecResultsHeader.length;

				int testExecResultsRowNum=0;
				
				
				Row testExecResultsHeaderRow =  testExecResultsSheet.createRow(0);
					
				//Setting defect header values
				Cell[] testExecResultsHeaderArray = new Cell[testExecResultsColCount];
				
				for(int counter = 0; counter<testExecResultsColCount;counter++){
					testExecResultsHeaderArray[counter] =  testExecResultsHeaderRow.createCell(counter);
					testExecResultsHeaderArray[counter].setCellValue(testExecResultsHeader[counter]);
				 }
				
				//Processing results list
				if(testCaseReportResults != null && !testCaseReportResults.isEmpty()){
					totalTestExecResultsCount = testCaseReportResults.size();	
					cellR16.setCellValue(totalTestExecResultsCount);
					
					
				//Creating row for each result
				for(TestRunReportsDeviceCaseList testCaseResult: testCaseReportResults ){
					
					int testExecResultsColNum = 0;
					TestCaseList testCase = null;
					Set<ProductFeature> productFeatures = null;
									
						if(testCaseResult!=null){
							
							testExecResultsRowNum = testExecResultsRowNum+1;
							
							Row testExecResultsRow = testExecResultsSheet.createRow(testExecResultsRowNum);	
							
							log.info("testExecResultsColNum:" + testExecResultsColNum);				
												
							//Forming String array for ExecResults column values
							//Forming  String array for ExecResults column types
							String[] testExecResultsValueArray = new String[testExecResultsColCount];	
							int[] testExecResultsColTypeArray = new int[testExecResultsColCount];
							
							//Setting TestRunListId
							testExecResultsValueArray[testExecResultsColNum] = String.valueOf(testRunJobId);
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
							
							//Setting TestCase Id
							
							testExecResultsValueArray[testExecResultsColNum] = testCaseResult.getTestCaseId().toString();
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						
							//Setting TestCase Name
							if(testCaseResult.getTestCaseName()!=null) {
								testExecResultsValueArray[testExecResultsColNum] = testCaseResult.getTestCaseName();
							} else {
								testExecResultsValueArray[testExecResultsColNum] = "";
							}						
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
							
							//Setting TestCase Description
							if(testCaseResult.getTestCaseDescription()!= null && testCaseResult.getTestCaseDescription() != null){
								testExecResultsValueArray[testExecResultsColNum] = testCaseResult.getTestCaseDescription();
							} else {
								testExecResultsValueArray[testExecResultsColNum] = "";
							}
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
								
							
							//Setting TestCase Result
							if(testCaseResult.getTestCaseResult()!=null){
								testExecResultsValueArray[testExecResultsColNum] =testCaseResult.getTestCaseResult().toString();								
							} else{
								testExecResultsValueArray[testExecResultsColNum] ="";
							}	
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;					
							
									
			
							prodFeatures = productListService.getFeaturesMappedToTestCase(testCaseResult.getTestCaseId());
							
							if(prodFeatures!=null && !prodFeatures.isEmpty()){
								testExecResultsValueArray[testExecResultsColNum]="";  
								for (ProductFeature productFeature: prodFeatures) {
									int featureColNum = testExecResultsColNum;
									//populating the Feature Code and Name							
									if(productFeature!= null && productFeature.getProductFeatureCode()!=null){					
										testExecResultsValueArray[testExecResultsColNum] = testExecResultsValueArray[testExecResultsColNum]+ productFeature.getProductFeatureCode();						
									} else{
										testExecResultsValueArray[testExecResultsColNum] = "";		
									}
									if(productFeature!= null && productFeature.getProductFeatureName()!=null){					
										testExecResultsValueArray[testExecResultsColNum] = testExecResultsValueArray[testExecResultsColNum] + "-" +productFeature.getProductFeatureName();						
									} else{
										testExecResultsValueArray[testExecResultsColNum] = testExecResultsValueArray[testExecResultsColNum];		
									}						
																	
										if(prodFeatures.size()>1){
											testExecResultsValueArray[testExecResultsColNum] = testExecResultsValueArray[testExecResultsColNum] + ",";
										}
									} 
								
								if(testExecResultsValueArray[testExecResultsColNum].endsWith(",")){
									testExecResultsValueArray[testExecResultsColNum] = testExecResultsValueArray[testExecResultsColNum].substring(0,(testExecResultsValueArray[testExecResultsColNum].length()-1));
								}	
								testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
							}
							
							
							//Setting Values in cells 
							Cell[] testExecResultCells = new Cell[testExecResultsColCount];
							
							for(int colCounter = 0; colCounter< testExecResultsColCount;colCounter++){
								
								testExecResultCells[colCounter] = testExecResultsRow.createCell(colCounter);
								testExecResultCells[colCounter].setCellType(testExecResultsColTypeArray[colCounter]);
								
								if(testExecResultCells[colCounter].getCellType() == Cell.CELL_TYPE_NUMERIC){
									testExecResultCells[colCounter].setCellValue(Integer.parseInt(testExecResultsValueArray[colCounter]));
								}
								
								else if(testExecResultCells[colCounter].getCellType() == Cell.CELL_TYPE_STRING){
									testExecResultCells[colCounter].setCellValue((testExecResultsValueArray[colCounter]));
								}													
								
							}
							
						} 
				}
			
				workbook.write(fos);			
				fos.close();
				
				} 
				
			} catch (FileNotFoundException e) {
				log.error("error in genrating excel test exec results Features Report: File not found error", e);
				log.error(e.getMessage());
			} catch (IOException e) {
				log.error("error in genrating excel test exec results Features Report: File not found error", e);
				log.error(e.getMessage());
			} catch (Exception e){
				log.error("Exception in generating Excel reports for Test exec results Features Report", e); 
			}		
			log.info("Excel - Features Report generated successfully");
			status = true;
			return status;
		}
		
		
		 public String getEvidencePath() {


				String propertyName = "EVIDENCE_FOLDER";
				InputStream fis;
				Properties appProperties = new Properties();
				String propertyValue = "";
				try {
					fis = getClass().getResourceAsStream("/TAFServer.properties");
					appProperties.load(fis);
					if (appProperties.get(propertyName) != null
							&& appProperties.get(propertyName) != "") {
						propertyValue = appProperties.get(propertyName).toString()
								.trim();
					}
				} catch (FileNotFoundException e) {
					log.error("Exception in getting TAFServer.properties file", e);
				} catch (IOException ioe) {
					log.error("Exception in loading hpqcProperties:", ioe);
				}
				return propertyValue;
			}
		 
		 public static void copyFolder(File src, File dest)
			    	throws IOException{
			 
			    	if(src.isDirectory()){
			 
			    		//if directory not exists, create it
			    		if(!dest.exists()){
			    		   dest.mkdir();
			    		   log.info("Directory copied from " 
			                              + src + "  to " + dest);
			    		}
			 
			    		//list all the directory contents
			    		String files[] = src.list();
			 
			    		for (String file : files) {
			    		   //construct the src and dest file structure
			    		   File srcFile = new File(src, file);
			    		   File destFile = new File(dest, file);
			    		   //recursive copy
			    		   copyFolder(srcFile,destFile);
			    		   log.info("original File name: " + destFile.getName());
			    		   
			    		   File modifiedFile = new File(destFile.getParent() + File.separator+destFile.getName().replaceAll(" ", "_")); 
			    		   destFile.renameTo(modifiedFile);
			    		   log.info("Modified File name: " + destFile.getName());
			    		}
			    	}
			    	else{
			    		//if file, then copy it
			    		//Use bytes stream to support all file types
			    		    FileInputStream in = new FileInputStream(src);
			    	        FileOutputStream out = new FileOutputStream(dest); 
			 
			    	        byte[] buffer = new byte[1024];
			 
			    	        int length;
			    	        //copy the file content in bytes 
			    	        while ((length = in.read(buffer)) > 0){
			    	    	   out.write(buffer, 0, length);
			    	        }
			 
			    	        in.close();
			    	        out.close();
			    	        log.info("File copied from " + src + " to " + dest);
			    	}
			}

			@Override
			public boolean importDefectfromBugnizer(String 	excelDataFile, InputStream is) {
				
				
				boolean addStatus = true;
				log.info("Start Time: "+Calendar.getInstance().getTime());
				addStatus = readBugnizerDefectsFromExcelFile(excelDataFile, is);
				log.info("End Time: "+Calendar.getInstance().getTime());
				return addStatus;
			}

			private boolean readBugnizerDefectsFromExcelFile(String excelDataFile,	InputStream fis) {
				int blankRowCount = 0;
				URL url = null;
				log.info("Import - Excel file from location:" + excelDataFile);
				List<Integer> errorRows = new ArrayList<Integer>();
				
				List<TestExecutionResultBugList> listOftestExecutionResult = null;
				boolean batchStatus = false;
				
				
				int numberOfSheets = 0;
				try { // 1

					Workbook workbook = null;
					if (excelDataFile.endsWith(".xls")) {
						workbook = new HSSFWorkbook(fis);
					}
					if (excelDataFile.endsWith(".xlsx")) {
						workbook = new XSSFWorkbook(fis);
					}

					// Get the worksheet containing the teststeps. It will be named 'TestSteps'.
					// If not available, get the first worksheet.
					numberOfSheets = workbook.getNumberOfSheets();
					if (numberOfSheets <= 0) {
						log.info("The Excel for Test Steps import does not conotain any worksheets");
						return false;
					}
					Sheet sheet = null;
					for (int i = 0; i < numberOfSheets; i++) {
						if (workbook.getSheetName(i).equalsIgnoreCase("DefectsBugnizer")) {
							sheet = workbook.getSheetAt(i);
							break;
						}
					}
					if (sheet == null) {
						sheet = workbook.getSheetAt(0);
					}

					
					// Check if the sheet contains rows of teststeps
					int rowCount = sheet.getPhysicalNumberOfRows();
					int colCount = 0;
					int colNum = 0;
					int startIndex = 0;
					int endIndex = 0;
					int mod = 0;
					int numberOfCounters = 0;
					Integer maxRecordsToReadFromExcel = 0;
					if (rowCount < 1) {
						log.info("No rows present in the defects worksheet");
						return false;
					} else { // 2
						if(defectBugnizerSize == null || defectBugnizerSize.length() == 0){
							maxRecordsToReadFromExcel = 10;
						}else{
							maxRecordsToReadFromExcel = Integer.parseInt(defectBugnizerSize);
						}
						
						if(rowCount > maxRecordsToReadFromExcel){
							numberOfCounters = rowCount/maxRecordsToReadFromExcel;
							mod = rowCount%maxRecordsToReadFromExcel;
							if(mod > 0){
								numberOfCounters = numberOfCounters+1;
					    	}
							
							for(int batchCounter=1; batchCounter<=numberOfCounters; batchCounter++){
								listOftestExecutionResult = new ArrayList<TestExecutionResultBugList>();
								if(batchCounter == 1){
					    			startIndex = 0;
					    			endIndex = batchCounter*maxRecordsToReadFromExcel;
					    		}else{
					    			startIndex = endIndex;
					    			if((rowCount-endIndex)> maxRecordsToReadFromExcel){
					    				endIndex = batchCounter*maxRecordsToReadFromExcel;
					    			}else{
					    				endIndex = rowCount;
					    				maxRecordsToReadFromExcel = endIndex-startIndex;
					    			}
					    		}
								// Logic to read data from file
								listOftestExecutionResult = readDefectBugnizer(sheet,startIndex,endIndex);
								batchStatus = batchProcessDefectBugnizer(listOftestExecutionResult);
								if(batchStatus){
									log.info("Batch "+batchCounter+ " Start Row: "+startIndex+"  End Row: "+endIndex+" Completed successfully.");
								}else{
									log.info("Batch "+batchCounter+ " Start Row: "+startIndex+"  End Row: "+endIndex+" Failed !!!! ");
								}
								listOftestExecutionResult = null;
							}
						}else{
							listOftestExecutionResult = new ArrayList<TestExecutionResultBugList>();
							startIndex = 0;
				    		endIndex = rowCount;
				    		// Logic to read data from file
				    		listOftestExecutionResult = readDefectBugnizer(sheet,startIndex,endIndex);
							batchStatus = batchProcessDefectBugnizer(listOftestExecutionResult);
							if(batchStatus){
								log.info("Batch "+" Start Row: "+startIndex+"  End Row: "+endIndex+ " Completed successfully");
							}else{
								log.info("Batch "+" Start Row: "+startIndex+"  End Row: "+endIndex+ " Failed !!!!");
							}
							listOftestExecutionResult = null;
						}
					} 
				} catch (IOException IOE) {
					log.error("Error reading defects from file", IOE);
					return false;
				} catch (Exception e) {
					log.error("Error reading defects from file", e);
					return false;
				}
				return batchStatus;
			}

			private List<TestExecutionResultBugList> readDefectBugnizer(Sheet sheet, int startIndex, int endIndex) {
				List<TestExecutionResultBugList> testExecutionResultBugList = null;
				TestExecutionResultBugList testExecutionResultBug = null;
				Row row;
				int rowNum = 0;
				int blankRowCount = 0;
				int colCount = 0;
				testExecutionResultBugList = new ArrayList<TestExecutionResultBugList>();
				for (rowNum = startIndex; rowNum < endIndex; rowNum++) {
					//Row row;
					blankRowCount = 0;
					row = sheet.getRow(rowNum);
					if (row == null) {
						rowNum++;
						continue;
					}
					if (row.getRowNum() <= defectBugnizerStartRow) {
						colCount = row.getLastCellNum();
						continue;
					} else { // 4

					
						testExecutionResultBug = new TestExecutionResultBugList();
						
						Cell cell0 = row.getCell(defectIssueIdindex);
						if (isCellValid(cell0)) {
							cell0.setCellType(Cell.CELL_TYPE_NUMERIC);
							if (cell0.getNumericCellValue() != 0){
								BigDecimal bugmangemntId = new BigDecimal(cell0.getNumericCellValue());
								testExecutionResultBug.setBugManagementSystemBugId(bugmangemntId.toString());
							} 
						}
						
						Cell cell1 = row.getCell(defectTypeindex);
						DefectTypeMaster defectType = null;
						if (isCellValid(cell1)) {
							cell1.setCellType(Cell.CELL_TYPE_STRING);
							String defectTypeName = cell1.getStringCellValue();							
							defectType = defectUploadService.getDefectType(defectTypeName.trim());
							if(defectType != null){
								testExecutionResultBug.setDefectType(defectType);		
							}else{
								defectType = new DefectTypeMaster();
								defectType.setDefectTypeId(3);
								testExecutionResultBug.setDefectType(defectType);
							}
						}else{
							defectType = new DefectTypeMaster();
							defectType.setDefectTypeId(3);
							testExecutionResultBug.setDefectType(defectType);
						}
						
						Cell cell2 = row.getCell(defectCreatedDateindex);
						if (isCellValid(cell2)) {
							if (cell2.getDateCellValue() != null){
								testExecutionResultBug.setBugCreationTime(cell2.getDateCellValue());
							} 
						}
						
						Cell cell3 = row.getCell(defectReportedByindex);
						UserList userList = new UserList();
						if (isCellValid(cell3)) {
							cell3.setCellType(Cell.CELL_TYPE_STRING);
							String assigneeName = cell3.getStringCellValue();							
							userList = defectUploadService.getUserList(assigneeName.trim());
							if(userList != null){
								testExecutionResultBug.setReportedBy(userList.getUserId());
							}else{
								testExecutionResultBug.setReportedBy(null);
							}
						}else{
							testExecutionResultBug.setReportedBy(null);	
						}
						
						
						
						Cell cell4 = row.getCell(defectModifiedDateindex);
						if (isCellValid(cell4)) {
							if (cell4.getDateCellValue() != null){
								testExecutionResultBug.setBugModifiedTime(cell4.getDateCellValue());
							} 
						}
						
					
						Cell cell5 = row.getCell(defectPriorityindex);
						ExecutionPriority executionPriority = null;
						if (isCellValid(cell5)) {
							cell5.setCellType(Cell.CELL_TYPE_STRING);
							String priorityName = cell5.getStringCellValue();							
							executionPriority = defectUploadService.getExecutionPriority(priorityName.trim());
							if(executionPriority != null){
								testExecutionResultBug.setTestersPriority(executionPriority);		
							}else{
								executionPriority = new ExecutionPriority();
								executionPriority.setExecutionPriorityId(1);
								testExecutionResultBug.setTestersPriority(executionPriority);
							}
						}else{
							executionPriority = new ExecutionPriority();
							executionPriority.setExecutionPriorityId(1);
							testExecutionResultBug.setTestersPriority(executionPriority);
						}
						
						Cell cell6 = row.getCell(defectTitleindex);
						if (isCellValid(cell6)) {
							cell6.setCellType(Cell.CELL_TYPE_STRING);
							if (cell6.getStringCellValue() != null){
								testExecutionResultBug.setBugTitle(cell6.getStringCellValue());
								testExecutionResultBug.setBugDescription(cell6.getStringCellValue());
							} 
						}
						
						Cell cell7 = row.getCell(defectStatusindex);
						WorkFlow workflow = null;
						if (isCellValid(cell7)) {
							cell7.setCellType(Cell.CELL_TYPE_STRING);
							String stageName = cell7.getStringCellValue();							
							workflow = defectUploadService.getWorkFlow(stageName.trim());
							if(workflow != null){
								testExecutionResultBug.setBugFilingStatus(workflow);	
							}
						}
						
						Cell cell9 = row.getCell(defectVerfiedByindex);
						UserList user = new UserList();
						if (isCellValid(cell9)) {
							cell9.setCellType(Cell.CELL_TYPE_STRING);
							String userName = cell9.getStringCellValue();	
							user = defectUploadService.getUserList(userName.trim());
							if(user != null){
								testExecutionResultBug.setApprovedBy(user);	
							}
						}
						
						testExecutionResultBug.setTestCaseExecutionResult(new TestCaseExecutionResult());
						testExecutionResultBug.getTestCaseExecutionResult().setTestCaseExecutionResultId(-100);
		 				testExecutionResultBug.setBugManagementSystemName(IDPAConstants.BUGNIZER_NAME);
		 				testExecutionResultBug.setUploadFlag(0);
						
						testExecutionResultBugList.add(testExecutionResultBug);
					}
					
				}
				return testExecutionResultBugList;
			}
			
			private boolean batchProcessDefectBugnizer(List<TestExecutionResultBugList> listOftestExecutionResult) {
				
				boolean addStatus = true;
				int addedDefectsCount = 0;
				HashMap<String, List<TestExecutionResultBugList>> defectsMap=verifyExistingDefects(listOftestExecutionResult);
				if (defectsMap.size() > 0) {
					Set<String> set = defectsMap.keySet();
					for (String action : set) {
						if(action.equalsIgnoreCase("Update")){
							List<TestExecutionResultBugList> listOfDefectsToUpdate = defectsMap.get(action);
							if(listOfDefectsToUpdate !=null && !listOfDefectsToUpdate.isEmpty()){
								addedDefectsCount = defectUploadService.addDefectsBulk(listOfDefectsToUpdate,"Update");
								if (addedDefectsCount != listOfDefectsToUpdate.size()) {
									addStatus = false;	
								}
							}
						} else if(action.equalsIgnoreCase("New")){
							List<TestExecutionResultBugList> listOfDefectsToAdd = defectsMap.get(action);
							log.info("Defects to Add newly: "+listOfDefectsToAdd.size());
							if(listOfDefectsToAdd !=null && !listOfDefectsToAdd.isEmpty()){
								addedDefectsCount = defectUploadService.addDefectsBulk(listOfDefectsToAdd,"New");
								if (addedDefectsCount != listOfDefectsToAdd.size()) {
									addStatus = false;	
								}
							}
						}
					}
				}
				return addStatus;
			}
			
				
			
			private HashMap<String,List<TestExecutionResultBugList>> verifyExistingDefects(List<TestExecutionResultBugList> listOftestExecutionResult) {
				
				HashMap<String,List<TestExecutionResultBugList>> defectsMap= new HashMap<String,List<TestExecutionResultBugList>>();
				List<TestExecutionResultBugList> defectsUpdate= new ArrayList<TestExecutionResultBugList>();
				List<TestExecutionResultBugList> defectsNew= new ArrayList<TestExecutionResultBugList>();
				
				for (TestExecutionResultBugList defectData : listOftestExecutionResult) {
					List<String> existingIssueId = null;
					existingIssueId = defectUploadService.getExistingIssueId(defectData.getBugManagementSystemBugId());
					if (existingIssueId != null) {
						if (existingIssueId.contains(defectData.getBugManagementSystemBugId())) {
							Integer defectDataId = defectUploadService.getDefectDataId(defectData.getBugManagementSystemBugId());
							defectData.setTestExecutionResultBugId(defectDataId);
							defectsUpdate.add(defectData);
						} else {
							defectsNew.add(defectData);
						}
					}
				}
				
				defectsMap.put("Update", defectsUpdate);
				defectsMap.put("New", defectsNew);
				log.info("defectBugsUpdate map Size: "+defectsMap.size());
				log.info("defectBugsNew map Size: "+defectsMap.size());
				return defectsMap;
			}
			public String exportWordTestResults(int testRunNo, String deviceId, String exportLocation) {	
				
				String fileLoc="";		
				
				List testResultsFromListAndDetailsView   = workPackageService.getTestRunResultFromListAndDetailViews(testRunNo, deviceId);		
				List testRunReportListFromView   = workPackageService.getTestRunResultFromViews(testRunNo);	
				fileLoc = generateAndExportWordTestExecResultsFromView(testResultsFromListAndDetailsView,testRunReportListFromView, testRunNo,exportLocation);
				
				return fileLoc;
				
			}
		 		
			public String generateAndExportWordTestExecResultsFromView(List testResultsFromListAndDetailsView, List testRunReportViewResults, int testRunNo, String exportLocation){
				
				String strCurrLoc=getReportDir();
				
				String fileLoc = "";
			
				String productName="";
				String productVersionName= "";
				String testSuiteName="";				
				String testRunConfigName="";
				String testRunCategoryName=null;
				String testRunStartTime = "";
				String testRunEndTime = "";
				String fileExportlocation = "";
				boolean isDirCreated;
				String finalContent="";
				String platFormVersion="";
				String deviceModel="";
				String deviceId="";
				Integer deviceListId=0;
				String serialNum="";
				String iosVer="";
				List<Integer> testCaseIdList=new ArrayList<Integer>();
				HashMap<Integer, String> testCaseIdMap = new HashMap<Integer, String>();
				try{	
					if(testResultsFromListAndDetailsView!= null & !testResultsFromListAndDetailsView.isEmpty()){
						
						Object[] obj1 = (Object[])testResultsFromListAndDetailsView.get(0);
						productName=String.valueOf(obj1[2]);
						
						productVersionName=String.valueOf(obj1[3]);
						testSuiteName=String.valueOf(obj1[10]);
						deviceId=String.valueOf(obj1[4]);
						platFormVersion=String.valueOf(obj1[6]);
						deviceModel=String.valueOf(obj1[5]);
						serialNum=String.valueOf(obj1[4]);
						iosVer=String.valueOf(obj1[7]);
						testRunConfigName=String.valueOf(obj1[8]);
						testRunStartTime =String.valueOf(obj1[11]);
						testRunEndTime =String.valueOf(obj1[12]);
					}
					fileExportlocation = exportLocation+"TestExecResults\\"+TER_NAMING_CONV+testRunNo;
					log.info("fileExportlocation>>"+fileExportlocation);
					isDirCreated = checkAndCreateTempDirectory(fileExportlocation);
					log.info("is Directory Created:"+isDirCreated);
					
					String stepExpecAttachmentPath="";
					stepExpecAttachmentPath=getValueFromPropFile("TESTSTEP_EXPECTED_OUTPUT_FOLDER");
					if(testRunCategoryName!=null && !testRunCategoryName.equals("REGRESSION")){
						File srcExpFolder = new File(stepExpecAttachmentPath+"\\"+productName);
						File destExpFolder = new File(fileExportlocation+"\\"+productName);
						log.info("srcExpFolder path>>>"+srcExpFolder.getAbsolutePath());
						log.info("destExpFolder path>>>"+destExpFolder.getAbsolutePath());
						if(!srcExpFolder.exists()){
					          log.info("srcExpFolder Directory does not exist.");
				        }
				    	else{ 
				            try{		            	
				         	copyFolder(srcExpFolder,destExpFolder);
				         	Thread.sleep(2000);
				            }catch(IOException e){
				            	log.error("ERROR  ",e);
				                 System.exit(0);
				            }
				         }
					}
					
					if(testRunCategoryName!=null && !testRunCategoryName.equals("REGRESSION")){
						File srcFolder = new File(getEvidencePath()+File.separator+
								TAFConstants.EVIDENCE_UNZIPPED+File.separator+testRunNo+File.separator+TAFConstants.EVIDENCE_SCREENSHOT);
						File destFolder = new File(fileExportlocation+"\\"+productName+"\\"+TAFConstants.EVIDENCE_SCREENSHOT);
						
						if(!srcFolder.exists()){
					          log.info("Directory does not exist.");
				        }
				    	else{ 
				            try{		            	
				         	copyFolder(srcFolder,destFolder);
				         	Thread.sleep(2000);
				            }catch(IOException e){
				            	log.error("ERROR  ",e);         	
				                 System.exit(0);
				            }
				    	}
					}
					
					String fileName = TER_NAMING_CONV + testRunNo+".doc";
					String screenShotLocation=productName+File.separator+TAFConstants.EVIDENCE_SCREENSHOT;
					log.info("screenShotLocation>>"+screenShotLocation);
					log.info(fileName);
					
					String userName = SecurityContextHolder.getContext().getAuthentication().getName();
					
					log.info("Export Test Exec Results : Generate Excel data");
					String fileContent="";String startContent="";String endContent="";
					File f = new File(getReportDir()+testSuiteName+".html");
					if(f.exists() && !f.isDirectory()) { 
						startContent=readFileAsString(getReportDir()+"start.html");
						endContent=readFileAsString(getReportDir()+"end.html");
						
						fileContent=readFileAsString(getReportDir()+testSuiteName+".html");
					}
					
					
					if(testRunReportViewResults != null && !testRunReportViewResults.isEmpty()){
						String testCaseHtmlStartTags="<table border=\"0\" cellpadding=\"2\" cellspacing=\"0\" style=\"width:100%;\"><tbody>";
						String testCaseHtmlContentTags="";
						String testCaseHtmlEndTags="</tbody></table>";
						String testCaseStudyHtmlTags="";
						String testCaseAndStepContentTags="";
						
						for(int i=0;i<testRunReportViewResults.size();i++){
							Object[] obj = (Object[]) testRunReportViewResults.get(i);
							
							
							if(String.valueOf(obj[0])!=null && String.valueOf(obj[0])!="" && !testCaseIdList.contains(new Integer(String.valueOf(obj[0])))){
								testCaseIdList.add(new Integer(String.valueOf(obj[0])));		
								testCaseIdMap.put(new Integer(String.valueOf(obj[0])), String.valueOf(obj[2]));//0-TestcaseId , 1-TestcaseDescription, 2-TestCaseName
								testCaseHtmlContentTags+="<tr><td><span style=\"padding-left:2em;\">"+String.valueOf(obj[2])+"</span></td><td>:</td><td>"+String.valueOf(obj[1])+"</td></tr>";
								testCaseAndStepContentTags+="<tr><td valign='top'>"+String.valueOf(obj[2])+"</td><td colspan=\"5\"  valign='top'>"+String.valueOf(obj[1])+"</td></tr>";
							}
							
												
							String obsImgSrcPath="";
							if(testRunCategoryName!=null && !testRunCategoryName.equals("REGRESSION")){
								String imgSrcPath=screenShotLocation+File.separator+String.valueOf(obj[9])+"";// 3-TestStepName
									imgSrcPath=imgSrcPath.replaceAll("\\\\", "\\\\\\\\");
									obsImgSrcPath="<img src=\""+imgSrcPath+"\" width=\"200\" height=\"200\" />";	
									log.info("obsImgSrcPath fullpath>>"+obsImgSrcPath);
							}
							else{
								obsImgSrcPath="";
							}
							log.info("obsImgSrcPath after>>"+obsImgSrcPath);
							testCaseAndStepContentTags+="<tr>";
							if(String.valueOf(obj[3])!=null)//testStepName
								testCaseAndStepContentTags+="<td valign='top'>"+String.valueOf(obj[3])+"</td>";
							else
								testCaseAndStepContentTags+="<td>&nbsp;</td>";
							if(String.valueOf(obj[4])!=null)//testStepDescription
								testCaseAndStepContentTags+="<td valign='top'>"+String.valueOf(obj[4])+"</td>";
							else
								testCaseAndStepContentTags+="<td>&nbsp;</td>";
							if(String.valueOf(obj[5])!=null){//testStepExpectedOutput
								String expOut=String.valueOf(obj[5]);
								if(testRunCategoryName!=null && testRunCategoryName.equals("REGRESSION")){
									testCaseAndStepContentTags+="<td valign='top'>"+expOut+"</td>";
								}else{
									expOut=StringEscapeUtils.unescapeHtml(expOut);
									String newExportLoc=productName;
									expOut=myreplace(expOut,"src=\"", "width=\"200\" height=\"200\" src=\""+newExportLoc+"");
									log.info("expOut>>UI>>"+expOut);
									testCaseAndStepContentTags+="<td valign='top'>"+expOut+"</td>";
								}
							}
							else
								testCaseAndStepContentTags+="<td>&nbsp;</td>";
							String obsOutput="";
							if(testRunCategoryName!=null && testRunCategoryName.equals("REGRESSION")){
								if(String.valueOf(obj[6])!=null){//testStepObservedOutput
									obsOutput=String.valueOf(obj[6]);
									obsOutput=obsOutput.replaceAll("(?i)<(?!img|/img|input|br).*?>", "");
									testCaseAndStepContentTags+="<td  valign='top'>"+obsOutput+"</td>";
								}else{
									testCaseAndStepContentTags+="<td>&nbsp;</td>";
								}
							}else{
								log.info("Inside else>>>");
								if(String.valueOf(obj[6])!=null){//testStepObservedOutput
									
									obsOutput=String.valueOf(obj[6]);
									obsOutput=obsOutput.replaceAll("(?i)<(?!img|/img|input|br).*?>", "");
									log.info("obsOutput>>"+obsOutput);
									testCaseAndStepContentTags+="<td  valign='top'>"+obsOutput+"<br>"+obsImgSrcPath+"</td>";
								}
								else
									testCaseAndStepContentTags+="<td>&nbsp;</td>";
							}
							if(String.valueOf(obj[7])!=null)//executionRemarks
								testCaseAndStepContentTags+="<td  valign='top'>"+String.valueOf(obj[7])+"</td>";
							else
								testCaseAndStepContentTags+="<td>&nbsp;</td>";
							if(String.valueOf(obj[8])!=null)//testStepResult
								testCaseAndStepContentTags+="<td valign='top'>"+String.valueOf(obj[8])+"</td></tr>";
							else
								testCaseAndStepContentTags+="<td>&nbsp;</td></tr>";
								
						}
						if(testCaseIdList!=null){
							testCaseAndStepContentTags=testCaseAndStepContentTags+testCaseHtmlEndTags;
						}
						testCaseStudyHtmlTags=testCaseHtmlStartTags+testCaseHtmlContentTags+testCaseHtmlEndTags;
						fileContent=fileContent.replaceAll("#TestCaseObj#",testCaseStudyHtmlTags);
						fileContent=fileContent.replaceAll("#TestCaseStepsDefs#", testCaseAndStepContentTags);
						fileContent=fileContent.replaceAll("#TEC#",testRunConfigName);
						fileContent=fileContent.replaceAll("#PRODNAME#",productName);
						fileContent=fileContent.replaceAll("#PRODVER#",productVersionName);
						fileContent=fileContent.replaceAll("#MDP#", platFormVersion);
						fileContent=fileContent.replaceAll("#Model#", deviceModel);
						
						fileContent=fileContent.replaceAll("#Serial#", serialNum);
						fileContent=fileContent.replaceAll("#IosVersion#", iosVer);
						fileContent=fileContent.replaceAll("#SDate#", testRunStartTime);
						fileContent=fileContent.replaceAll("#EDate#", testRunEndTime);
						
					}
					
					fileContent=fileContent.replaceAll("#TPNS#", "");			
					fileContent=fileContent.replaceAll("#DHFVersion#", "");
					fileContent=fileContent.replaceAll("#PIFUVersion#", "");
					fileContent=fileContent.replaceAll("#SafetyInfoVersion#", "");
					fileContent=fileContent.replaceAll("#InjectionPIVersion#", "");
					finalContent=startContent+fileContent+endContent;
					FileOutputStream out = new FileOutputStream(fileExportlocation+"\\"+fileName);
					fileLoc=fileExportlocation+"\\"+fileName;
					
					byte[] contentInBytes = finalContent.getBytes();
					out.write(contentInBytes);
					log.info("Generated successfuly");
					ZipTool.dozip(fileExportlocation);
					out.close();
					log.info("Word Document reports generated successfully");
				} catch (Exception e){
					log.error("Exception in generating word doc reports for Test exec results", e); 
				}	
				
				return fileLoc;
			}
			public String myreplace(String string, String pattern, String replacement) {
				  int index = string.indexOf(pattern);
				  if(index < 0) return string;
				  int endIndex = index+pattern.length();
				  return string.substring(0, index) + replacement +
						  myreplace(string.substring(endIndex), pattern, replacement);
			}
			public String getValueFromPropFile(String propertyName) {

				InputStream fis;
				Properties appProperties = new Properties();
				String propertyValue = "";
				try {
					fis = getClass().getResourceAsStream("/TAFServer.properties");
					appProperties.load(fis);
					if (appProperties.get(propertyName) != null
							&& appProperties.get(propertyName) != "") {
						propertyValue = appProperties.get(propertyName).toString()
								.trim();
					}
				} catch (FileNotFoundException e) {
					log.error("Exception in getting TAFServer.properties file", e);
				} catch (IOException ioe) {
					log.error("Exception in loading hpqcProperties:", ioe);
				}
				return propertyValue;
			}
		private String getReportDir(){
			String strReturnVal=null;
			try{			
				String strCurrLoc=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
				strCurrLoc = strCurrLoc.replace("%20", " ");
				if (strCurrLoc.indexOf("WEB-INF")>0){
					strCurrLoc=strCurrLoc.substring(1,strCurrLoc.indexOf("WEB-INF"));
				}
				strReturnVal=strCurrLoc+strReturnVal;
				strReturnVal=strReturnVal.substring(0,strReturnVal.lastIndexOf('/')+1);
				strReturnVal=strReturnVal+"html/protocol/";
			}catch(Exception exception){
				log.error("getReportDir:"+exception);
			}
			 return strReturnVal;
		}
		public String readFileAsString(String filePath)
		    throws java.io.IOException{
		        StringBuffer fileData = new StringBuffer(1000);
		        BufferedReader reader = new BufferedReader(
		                new FileReader(filePath));
		        char[] buf = new char[1024];
		        int numRead=0;
		        while((numRead=reader.read(buf)) != -1){
		            String readData = String.valueOf(buf, 0, numRead);
		            fileData.append(readData);
		            buf = new char[1024];
		        }
		        reader.close();
		        return fileData.toString();
		 }
		
		@Override
		public String importActivities(HttpServletRequest request,String excelDataFile, int testFactoryId, int productId, InputStream is) {
			TestFactory testFactory = testFactoryManagementService.getTestFactoryById(testFactoryId);
			ProductMaster productMaster = productListService.getProductById(productId);
			log.info("Start Time: "+Calendar.getInstance().getTime());
		String	addStatus = readActvitiesFromExcelFile(request,excelDataFile,testFactory, productMaster, is);
			log.info("End Time: "+Calendar.getInstance().getTime());
			return addStatus;
			
		}
		
		
		@Override
		public String importActivityTasks(HttpServletRequest request,String excelDataFile, int activitywpId, InputStream is) {
			String addStatus = " ";
		ActivityWorkPackage	activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(activitywpId, 0);
			log.info("Start Time: "+Calendar.getInstance().getTime());
			addStatus = readActvityTasksFromExcelFile(request,excelDataFile,activityWorkPackage, is);
			log.info("End Time: "+Calendar.getInstance().getTime());
			return addStatus;
			
		}
		

		private String readActvitiesFromExcelFile(HttpServletRequest request, String excelDataFile, TestFactory testFactory, ProductMaster prodMaster, InputStream fis) {

			String message =" ";
			int blankRowCount = 0;
			URL url = null;
			log.info("Import - Excel file from location:" + excelDataFile);
			List<Integer> errorRows = new ArrayList<Integer>();
			
			List<ActivitiesDTO> listOfActivitiesDTO = null;
			List<Activity> listOfActivitiesFromExcel = null;
			boolean batchStatus = false;
			

			
			int numberOfSheets = 0;
			try { // 1

				Workbook workbook = null;
				if (excelDataFile.endsWith(".xls")) {
					workbook = new HSSFWorkbook(fis);
				}
				if (excelDataFile.endsWith(".xlsx")) {
					workbook = new XSSFWorkbook(fis);
				}

				// Get the worksheet containing the teststeps. It will be named 'TestSteps'.
				// If not available, get the first worksheet.
				numberOfSheets = workbook.getNumberOfSheets();
				if (numberOfSheets <= 0) {
					log.info("The Excel for Activities import does not contain any worksheets");
				//	return false;
				}
				Sheet sheet = null;
				for (int i = 0; i < numberOfSheets; i++) {
					if (workbook.getSheetName(i).equalsIgnoreCase("Activities") || i==0) {
						sheet = workbook.getSheetAt(i);
						
						break;
					}
				}
				if (sheet == null) {
					sheet = workbook.getSheetAt(0);
				}

				
				// Check if the sheet contains rows of teststeps
				int rowCount = sheet.getPhysicalNumberOfRows();
				int colCount = 0;
				int colNum = 0;
				int startIndex = 0;
				int endIndex = 0;
				int mod = 0;
				int numberOfCounters = 0;
				Integer maxRecordsToReadFromExcel = 0;
				if (rowCount < 1) {
					log.info("No rows present in the activities worksheet");
				} else { // 2
					
					HashMap<String, Object> activityMap	= readActivities(request,sheet,rowCount,startIndex,endIndex,testFactory,workbook);
					 message = (String) activityMap.get("message");
					listOfActivitiesFromExcel = (List<Activity>) activityMap.get("activities");
					String filePathName = (String)activityMap.get("filePathAndName");
					if(null != filePathName && "" != filePathName){
						message += ";"+filePathName;
					}
					batchStatus = batchProcessActivities(request, listOfActivitiesFromExcel, testFactory, prodMaster);
					if(batchStatus){
						log.info("Batch "+0+ " Start Row: "+startIndex+"  End Row: "+endIndex+" Completed successfully.");
					}else{
						log.info("Batch "+0+ " Start Row: "+startIndex+"  End Row: "+endIndex+" Failed !!!! ");
					}
				} 
			} catch (IOException IOE) {
				log.error("Error reading Activities from file", IOE);
				return "Error reading Activities from file";
			} catch (Exception e) {
				log.error("Error reading Activities from file", e);
				return "Error reading Activities from file";
			}
			return message;
		}
		
		public HashMap<String, Object> readActivities(HttpServletRequest request,Sheet sheet,int rowCount,int startIndex, int endIndex, TestFactory testFactory, Workbook workbook){
		    Activity activity = null;
			int validRecordCount = 0;
			int invalidRecordCount = 0;			    
			String message = " ";
			HashMap<String,Object> activityMap = new HashMap<String,Object>();
			List<Activity> listOfActivities = new ArrayList<Activity>();
			  
			Row row = null;
			int rowNum = 0;
			int blankRowCount = 0;
			int colCount = 0;
			int totalRowCount = sheet.getPhysicalNumberOfRows()-1;
			List<ActivityMaster> activityMasterList=null;
			Map<Integer,List<ActivityMaster>> activityMapByProductId = new HashMap<Integer,List<ActivityMaster>>();
			List<ExecutionTypeMaster> executionTypeMasterList=null;
			Map<String,ProductFeature> productFeatureMap=new HashMap<String,ProductFeature>();
			Map<String,UserList> userListMap=new HashMap<String,UserList>();
			Set<Activity> existingActivitiesName=null;
			ProductMaster proMaster = null;
			ActivityWorkPackage actWorkPackage = null;
			List<String> activityNames = new ArrayList<String>();
			Map<String,WorkflowStatus> statusListMap=new HashMap<String,WorkflowStatus>();
			int invalidRowCount = 0;
			List<String> errorMessages = new ArrayList<String>();
			Map<String,ChangeRequest> changeRequestMap = new HashMap<String,ChangeRequest>();
			int isValidRowNum = 0;
			String rowStatus = "";
			String statusRemarks = "";
			String currentDate = DateUtility.dateToStringInSecond(new Date());
			currentDate = currentDate.replaceAll(":", "_");
			String activityUploadReportFile = "ActivityUploadReport_"+currentDate+".xlsx";
			
			//Store invalid row data into a map for data parsing
			Map<Integer,String> invalidRowMap = new HashMap<Integer,String>();
			
			//Header row - Row Number is 1
			Row headerRow = sheet.getRow(0);
	        //Applying CellStyle to the Header Row
	        CellStyle css = workbook.createCellStyle();
	        Font arialBoldFont = workbook.createFont();
	        
	        arialBoldFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	        arialBoldFont.setFontName("Arial");
	        arialBoldFont.setFontHeightInPoints((short) 10);	        
	        
	        css.setAlignment(CellStyle.ALIGN_CENTER);
			css.setFont(arialBoldFont);
			int totalHeaderCells = headerRow.getPhysicalNumberOfCells()-1;				
			for(int i=totalHeaderCells;i<totalHeaderCells+2;i++){
    			Cell cell = headerRow.createCell(i+1);
    			cell.setCellStyle(css);
    			if(i==totalHeaderCells){    				
    				cell.setCellValue("Status");    				
    			} else if(i==totalHeaderCells+1){   				
    				cell.setCellValue("Remarks");    				
    			}
    		}
			
			for (rowNum = 1; rowNum <= rowCount; rowNum++) {
				try {
					log.info("Inside readActivities()");
				//Row row;
				blankRowCount = 0;
				row = sheet.getRow(rowNum);
				if (row == null) {
					rowNum++;
					continue;
				}
				if (row.getRowNum() < activitiesStartRow) {
					colCount = row.getLastCellNum();
					continue;
				}else if(row.getRowNum() == activitiesStartRow){
					colCount = row.getLastCellNum();
					continue;
				} else { // 4

					// Read the Activity info
					activity = new Activity();
					boolean activityNameIsMissing = false;
					boolean activityIDIsMissing = false;
					
				
					///Check if Activity Work Package Id is available. Either Activity Work Package Name or Activity Work Package Id is mandatory.
					String activityWorkPackageId = null;
					
					
					// Teststep name is mandatory. If not present skip row
					List<ActivityWorkPackage> activityWpList = null;
					Cell cell = row.getCell(productNameIndex);
					String productName = "";
					if (isCellValid(cell)) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						productName = cell.getStringCellValue().trim();						
					}else{
						log.info("Product name is empty or Invalid in row #" + rowNum + ". Ignoring row");
						isValidRowNum = rowNum;
						rowStatus = "NotUpdated";
						statusRemarks = "Product name is missing";						
						invalidRowMap.put(isValidRowNum, rowStatus+";"+ statusRemarks);
						continue;
					}
					
					Cell cell0 = row.getCell(versionNameIndex);
					String versionName = "";
					if (isCellValid(cell0)) {
						cell0.setCellType(Cell.CELL_TYPE_STRING);
						versionName = cell0.getStringCellValue().trim();
						
					}else{
						log.info("Version name is empty or Invalid in row #" + rowNum + ". Ignoring row");
						isValidRowNum = rowNum;
						rowStatus = "NotUpdated";
						statusRemarks = "Version name is missing";						
						invalidRowMap.put(isValidRowNum, rowStatus+";"+ statusRemarks);
						continue;
					}
					
					Cell cell1 = row.getCell(buildNameIndex);
					String buildName = "";
					if (isCellValid(cell1)) {
						cell1.setCellType(Cell.CELL_TYPE_STRING);
						buildName = cell1.getStringCellValue().trim();
						
					}else{
						log.info("Build name is empty or Invalid in row #" + rowNum + ". Ignoring row");
						isValidRowNum = rowNum;
						rowStatus = "NotUpdated";
						statusRemarks = "Build name is missing";						
						invalidRowMap.put(isValidRowNum, rowStatus+";"+ statusRemarks);
						continue;
					}
					
					Cell cell2 = row.getCell(workpackageNameIndex);
					String activityWPName = "";
					if (isCellValid(cell2)) {
						cell2.setCellType(Cell.CELL_TYPE_STRING);
						activityWPName = cell2.getStringCellValue().trim();
						activityWpList = activityWorkPackageService.getActivityWorkPackagesByName(activityWPName);
					}else{
						log.info("ActivityWorkpackage name is empty or Invalid in row #" + rowNum + ". Ignoring row");
						isValidRowNum = rowNum;
						rowStatus = "NotUpdated";
						statusRemarks = "ActivityWorkPackageName is missing";						
						invalidRowMap.put(isValidRowNum, rowStatus+";"+ statusRemarks);
						continue;							
					} 					
					
					boolean isInvalid = false;				
					if(activityWpList != null && !activityWpList.isEmpty()){
						for(ActivityWorkPackage actWP : activityWpList){
							if(actWP.getProductBuild().getBuildname().equalsIgnoreCase(buildName)){
								if(actWP.getProductBuild().getProductVersion().getProductVersionName().equalsIgnoreCase(versionName)){
									if(actWP.getProductBuild().getProductVersion().getProductMaster().getProductName().equalsIgnoreCase(productName)){
										activity.setActivityWorkPackage(actWP);
										existingActivitiesName = actWP.getActivity();										
										proMaster = actWP.getProductBuild().getProductVersion().getProductMaster();
										actWorkPackage = actWP;
										isInvalid = false;
										break;
									}else{
										log.info("Product Name is Invalid for this combination in row #" + rowNum + ". Ignoring row");
										isValidRowNum = rowNum;
										rowStatus = "NotUpdated";
										statusRemarks = "Product Name is Invalid for this combination";						
										isInvalid = true;
									}
								}else{
									log.info("Version Name is Invalid for this combination in row #" + rowNum + ". Ignoring row");
									isValidRowNum = rowNum;
									rowStatus = "NotUpdated";
									statusRemarks = "Version Name is Invalid for this combination";						
									isInvalid = true;
								}
							}else{
								log.info("Build Name is Invalid for this combination in row #" + rowNum + ". Ignoring row");
								isValidRowNum = rowNum;
								rowStatus = "NotUpdated";
								statusRemarks = "Build Name is Invalid for this combination";						
								isInvalid = true;
							}
						}
					}else{
						log.info("ActivityWorkpackage Name is Invalid for this combination in row #" + rowNum + ". Ignoring row");
						isValidRowNum = rowNum;
						rowStatus = "NotUpdated";
						statusRemarks = "ActivityWorkpackage Name is Invalid for this combination";						
						isInvalid = true;
					}
					
					
					if(isInvalid){
						invalidRowMap.put(isValidRowNum, rowStatus+";"+ statusRemarks);
						continue;
					}
					
					String testFactName = actWorkPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryName();
					if(!testFactory.getTestFactoryName().equals(testFactName)){
						log.info("Product~ActivityWorkpackage details are invalid for the TestFactory: "+testFactory.getTestFactoryName()+" in row #" + rowNum + ". Ignoring row");
						isValidRowNum = rowNum;
						rowStatus = "NotUpdated";
						statusRemarks = "Product~ActivityWorkpackage details are invalid for the TestFactory: "+testFactory.getTestFactoryName();						
						invalidRowMap.put(isValidRowNum, rowStatus+";"+ statusRemarks);
						continue;
					}
					
					Cell cell3 = row.getCell(activityNameindex);
					if (isCellValid(cell3)) {
						cell3.setCellType(Cell.CELL_TYPE_STRING);
						String activityName = cell3.getStringCellValue().trim();
						if(activityName.matches(".*"+specialChars+".*")){
							invalidRowCount = invalidRowCount+1;
							log.info("Data of Row : "+invalidRowCount+ " with ActivityName : "+activityName+" is failed to upload");
							errorMessages.add("Data of Row : "+invalidRowCount+ " with ActivityName : "+activityName+" is failed to upload");
							log.info("Activity name is having special characters in row #" + rowNum + ". Ignoring row");
							isValidRowNum = rowNum;
							rowStatus = "NotUpdated";
							statusRemarks = "Activity name is having special characters";						
							invalidRowMap.put(isValidRowNum, rowStatus+";"+ statusRemarks);
							continue;
						}else{
						activity.setActivityName(cell3.getStringCellValue().trim());
						if(!activityNames.contains(activity.getActivityName())){
							activityNameIsMissing=verifyExistingActivitiesByName(activity, existingActivitiesName);
							if(!activityNameIsMissing){
								activityNames.add(activity.getActivityName());
							}
						}else{
							activityNameIsMissing = true;
						}
					  }
					} else {
						
						activityNameIsMissing = true;
					}

					if (activityNameIsMissing) {
						blankRowCount++;
						log.info("Activity name is missing or duplicated in row #" + rowNum + ". Ignoring row");
						isValidRowNum = rowNum;
						rowStatus = "NotUpdated";
						statusRemarks = "Activity name is missing or duplicated";						
						invalidRowMap.put(isValidRowNum, rowStatus+";"+ statusRemarks);
						continue;
					}


					
					Cell cell4 = row.getCell(srsSectionIndex);
					ProductFeature srsSection = null;
					ProductFeature productFeature = null;
					if (isCellValid(cell4)) {
						String featureName = formatter.formatCellValue(cell4);
						if(productFeatureMap.containsKey(featureName)){
							srsSection=productFeatureMap.get(featureName);
						}else{							
						srsSection = productListService.getByProductFeatureName(featureName, proMaster);
							if(srsSection == null){
								productFeature = new ProductFeature();
								productFeature.setProductFeatureName(featureName);
								productFeature.setProductFeatureCode(featureName);
								productFeature.setDisplayName(featureName);
								productFeature.setProductMaster(proMaster);
								productFeature.setCreatedDate(new Date());
								productFeature.setModifiedDate(new Date());
								productFeature.setSourceType("Import");
								productFeature=productListService.addFeature(productFeature);
							}
						
						}
						if(srsSection != null){
							activity.setProductFeature(srsSection);	
							productFeatureMap.put(featureName,srsSection);
						}else if(productFeature!=null){
							activity.setProductFeature(productFeature);	
							productFeatureMap.put(featureName,productFeature);
							
						}else {
							activity.setProductFeature(null);
						}
					}else{
						activity.setProductFeature(null);
					}
					
					
					Cell cell5 = row.getCell(activityMasterIndex);
					ActivityMaster activityMaster = null;
					ActivityMaster uploadActivityMaster = null;
					
						if(!activityMapByProductId.containsKey(proMaster.getProductId())){							
							activityMasterList = activityTypeDAO.listActivityTypes(testFactory.getTestFactoryId(), proMaster.getProductId(), null, null, 0, true);
							activityMapByProductId.put(proMaster.getProductId(), activityMasterList);
						}						
						activityMasterList = activityMapByProductId.get(proMaster.getProductId());
					
					if (isCellValid(cell5)) {
						cell5.setCellType(Cell.CELL_TYPE_STRING);
						String activityMasterName = cell5.getStringCellValue().replaceAll("\\\\s+","").trim();	
						activityMasterName =	activityMasterName.replaceAll("\u00A0", "");						
						log.info("activityMasterName:"+activityMasterName+"---");
							if(activityMasterList != null && activityMasterList.size()>0){
								activityMaster=getActivityMasterByActivityMasterName(activityMasterList,activityMasterName);
							}
							if(activityMaster == null){
						    	 uploadActivityMaster = new ActivityMaster();
						    	 ActivityType activityType= new ActivityType();
						    	 uploadActivityMaster.setActivityMasterName(activityMasterName.trim());
						    	 uploadActivityMaster.setWeightage(0F);
						    	 uploadActivityMaster.setProductMaster(proMaster);
						    	 uploadActivityMaster.setTestFactory(testFactory);
								activityType.setActivityTypeId(1);
								uploadActivityMaster.setActivityType(activityType);
								uploadActivityMaster=activityTypeDAO.uploadActivityMaster(uploadActivityMaster);
								activityMasterList.add(uploadActivityMaster);
								activityMapByProductId.put(proMaster.getProductId(), activityMasterList);
							}						 
						     
					     if(activityMaster != null){
								activity.setActivityMaster(activityMaster);	
							}else if(uploadActivityMaster!=null){
								activity.setActivityMaster(uploadActivityMaster);									
							}else {
								activity.setActivityMaster(null);
							}						     						     
					}else{
						activity.setActivityMaster(null);
						log.info("Activity Type is missing in row #" + rowNum + ". Ignoring row");
						isValidRowNum = rowNum;
						rowStatus = "NotUpdated";
						statusRemarks = "ActivityType is missing";
						invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);
						
						continue;
					}
					
					Cell cell7 = row.getCell(activityTrackerIndex);
					String activityTrackerNumber = "";
					if (isCellValid(cell7)) {
						cell7.setCellType(Cell.CELL_TYPE_STRING);
						if (cell7.getStringCellValue() != null){
							activityTrackerNumber = cell7.getStringCellValue();
							if(activityTrackerNumber.trim().contains(".")){
								activityTrackerNumber = activityTrackerNumber.trim().substring(0, activityTrackerNumber.indexOf("."));
							}
							log.info("activityTrackerNumber: "+activityTrackerNumber);
							activity.setActivityTrackerNumber(activityTrackerNumber);
						} 
					}
					
					Cell cell8 = row.getCell(categoryIndex);
					ExecutionTypeMaster category = null;
					if(rowNum==1){
						executionTypeMasterList=executionTypeMasterService.listbyEntityMasterId(1);
					}
					
					if (isCellValid(cell8)) {
						cell8.setCellType(Cell.CELL_TYPE_STRING);
						String categoryName = cell8.getStringCellValue();							
						if(executionTypeMasterList!=null && executionTypeMasterList.size() >0){
							for(ExecutionTypeMaster executionTypeMaster:executionTypeMasterList){
								if(executionTypeMaster.getName().equalsIgnoreCase(categoryName.trim())){
									category=executionTypeMaster;
									break;
								}
							}
						}
						if(category != null){
							activity.setCategory(category);		
						}else{
							activity.setCategory(null);	
						}
					}else{						
						activity.setCategory(null);	
					}
				
					
					Cell cell9 = row.getCell(assigneeIndex);
					UserList assignee = null;
					UserList defultUser=new UserList();
					defultUser=(UserList)request.getSession().getAttribute("USER");
					if (isCellValid(cell9)) {
						cell9.setCellType(Cell.CELL_TYPE_STRING);
						String assigneeLoginId = cell9.getStringCellValue().trim();		
						if(userListMap.containsKey(assigneeLoginId)){
							assignee =userListMap.get(assigneeLoginId);
						}else{
						assignee = userListService.getProductTeamResourcesByUserName(proMaster.getProductId(),assigneeLoginId,actWorkPackage.getPlannedStartDate(),actWorkPackage.getPlannedEndDate());
						
						
						}
						if(assignee != null){
							activity.setAssignee(assignee);	
							userListMap.put(assigneeLoginId, assignee);
						}else if(actWorkPackage.getOwner()!=null){
							activity.setAssignee(actWorkPackage.getOwner());
						}else {
							activity.setAssignee(defultUser);
						}
					}else{

						if(actWorkPackage.getOwner()!=null){
							activity.setAssignee(actWorkPackage.getOwner());
						}else {
							activity.setAssignee(defultUser);
						}
					}
					
					
					Cell cell10 = row.getCell(reviewerIndex);
					UserList reviewer = null;
					if (isCellValid(cell10)) {
						cell10.setCellType(Cell.CELL_TYPE_STRING);
						String reviewerLoginId = cell10.getStringCellValue().trim();	
						if(userListMap.containsKey(reviewerLoginId)){
							reviewer =userListMap.get(reviewerLoginId);
						}else{
							reviewer = userListService.getProductTeamResourcesByUserName(proMaster.getProductId(),reviewerLoginId,actWorkPackage.getPlannedStartDate(),actWorkPackage.getPlannedEndDate());
							
						}
						
						if(reviewer != null){
							
							activity.setReviewer(reviewer);	
							userListMap.put(reviewerLoginId, reviewer);
						}else if(actWorkPackage.getOwner()!=null){
							activity.setReviewer(actWorkPackage.getOwner());
						}else{
							activity.setReviewer(defultUser);
						}
					}else{
						if(actWorkPackage.getOwner()!=null){
							activity.setReviewer(actWorkPackage.getOwner());
						}else{
							activity.setReviewer(defultUser);
						}
							
					}
					
					
					Cell cell11 = row.getCell(activityStatusIndex);
					WorkflowStatus workflowStatus = null;
					if (isCellValid(cell11)) {
						cell11.setCellType(Cell.CELL_TYPE_STRING);
						String currentStatus = cell11.getStringCellValue();
						Integer activityTypeId = 0;
						if(activity.getActivityMaster() != null && activity.getActivityMaster().getActivityMasterId() != null){
							activityTypeId = activity.getActivityMaster().getActivityMasterId() ;
							workflowStatus =workflowStatusService.getWorkflowStatusForInstanceByEntityType(proMaster.getProductId(), IDPAConstants.ENTITY_ACTIVITY_TYPE, activityTypeId,currentStatus);
							activity.setWorkflowStatus(workflowStatus);
						} else {
							if(workflowStatus == null || workflowStatus.getWorkflowStatusId() == null || workflowStatus.getWorkflowStatusId() == 0){
								workflowStatus = workflowStatusService.getWorkflowStatusById(-1);
								activity.setWorkflowStatus(workflowStatus);
							}
						}
					} else {
					
						Integer activityTypeId = 0;
						if(activity.getActivityMaster() != null && activity.getActivityMaster().getActivityMasterId() != null){
							activityTypeId = activity.getActivityMaster().getActivityMasterId() ;
						}
						if(statusListMap.containsKey(activityTypeId+"")){
							workflowStatus = statusListMap.get(activityTypeId+"");
						}else{
							workflowStatus = workflowStatusService.getInitialStatusForInstanceByEntityType(proMaster.getProductId(), IDPAConstants.ENTITY_ACTIVITY_TYPE, activityTypeId);
						}
						if(workflowStatus == null || workflowStatus.getWorkflowStatusId() == null || workflowStatus.getWorkflowStatusId() == 0){
							workflowStatus = workflowStatusService.getWorkflowStatusById(-1);
						}
						statusListMap.put(activityTypeId+"", workflowStatus);
						activity.setWorkflowStatus(workflowStatus);		
						if(workflowStatus != null && workflowStatus.getWorkflowStatusId() == -1){
							activity.setActualStartDate(new Date());
							activity.setActualEndDate(new Date());
						}
						if(actWorkPackage.getWorkflowStatus() != null && actWorkPackage.getWorkflowStatus().getWorkflowStatusId() > 0){
							activity.setLifeCycleStage(actWorkPackage.getWorkflowStatus());
						}
					}
					
					Cell cell12 = row.getCell(activityPriorityIndex);
					ExecutionPriority priority = null;
					if (isCellValid(cell12)) {
						cell12.setCellType(Cell.CELL_TYPE_STRING);
						String priorityName = cell12.getStringCellValue();							
						priority=getExecutionPriorityByexecutionPriorityName(priorityName.trim());
						if(priority == null){
							priority=getExecutionPriorityByexecutionPriorityName("P0");
						}
					}else{
						priority=getExecutionPriorityByexecutionPriorityName("P0");	
					}
					activity.setPriority(priority);
					
					
					Cell cell13 = row.getCell(activityComplexity);
					String  complexity = null;
					if (isCellValid(cell13)) {
						cell13.setCellType(Cell.CELL_TYPE_STRING);
						if(cell13.getStringCellValue() != null){
							activity.setComplexity(complexity = cell13.getStringCellValue().trim());		
						}					
					}else{					
							activity.setComplexity("LOW");					
					}
									
					Cell cell14 = row.getCell(remarksIndex);
					if (isCellValid(cell14)) {
						cell14.setCellType(Cell.CELL_TYPE_STRING);
						if (cell14.getStringCellValue() != null){
							activity.setRemark(cell14.getStringCellValue());
						} 
					}
					
					Date plannedStartDate=null;
					Cell cell15 = row.getCell(plannedStartDateIndex);
					if (isCellValid(cell15)) {
						if(cell15.CELL_TYPE_NUMERIC == cell15.getCellType()){
						plannedStartDate = cell15.getDateCellValue();
						}else{
							plannedStartDate = DateUtility.dateformatWithOutTime(cell15.getStringCellValue());
						}
						
							activity.setPlannedStartDate(plannedStartDate);
					}else{
						try {
							if(actWorkPackage.getPlannedStartDate()!=null){
								activity.setPlannedStartDate(actWorkPackage.getPlannedStartDate());
							}else{
								activity.setPlannedStartDate(new Date());
							}
						} catch (Exception e) {
							log.error("exception in parsing data value", e);
						}
					}
					
					Date plannedEndDate=null;
					Cell cell16 = row.getCell(plannedEndDateIndex);
					if (isCellValid(cell16)) {
						if(cell16.CELL_TYPE_NUMERIC == cell16.getCellType()){
						plannedEndDate = cell16.getDateCellValue();
						}else{
							plannedEndDate = DateUtility.dateformatWithOutTime(cell16.getStringCellValue());
						}
						activity.setPlannedEndDate(plannedEndDate);
					}else{
						try {
							if(actWorkPackage.getPlannedEndDate()!=null){
								activity.setPlannedEndDate(actWorkPackage.getPlannedEndDate());
							}else{
								activity.setPlannedEndDate(new Date());
							}
						} catch (Exception e) {
							log.error("exception in parsing data value", e);
						}
					}
					
					if(plannedStartDate != null && plannedEndDate != null) {
						Integer dateresult = DateUtility.ActivityDateValidation(plannedStartDate, plannedEndDate,
								actWorkPackage.getPlannedStartDate(), actWorkPackage.getPlannedEndDate());
						if (dateresult == 1 || dateresult == 2) {
							activity.setPlannedStartDate(actWorkPackage.getPlannedStartDate());
							activity.setPlannedEndDate(actWorkPackage.getPlannedEndDate());
						} 
						
						Integer calculatedPlannedEffort= DateUtility.getWorkingHoursBetweenTwoDates(activity.getPlannedStartDate(),DateUtility.formatedDateWithMaxDateWithTime(activity.getPlannedEndDate()) , 9);
						activity.setPlannedEffort(calculatedPlannedEffort);
					}
					
					Cell cell17 = row.getCell(activityPlanSize);
					if (isCellValid(cell17)) {
						cell17.setCellType(Cell.CELL_TYPE_NUMERIC);
						Integer plandSize = (int) cell17.getNumericCellValue();
						activity.setPlannedActivitySize(plandSize);
						
					}else{
						activity.setPlannedActivitySize(0);
					}
					activity.setBaselineActivitySize(activity.getPlannedActivitySize());
					if(plannedStartDate!=null){					
						if(actWorkPackage.getPlannedStartDate().compareTo(plannedStartDate) <=0 && actWorkPackage.getPlannedEndDate().compareTo(plannedStartDate)>=0){
							activity.setPlannedStartDate(plannedStartDate);
						}else{
							activity.setPlannedStartDate(actWorkPackage.getPlannedStartDate());
						}
					}
					if(plannedEndDate!=null){					
						if(actWorkPackage.getPlannedStartDate().compareTo(plannedEndDate) <=0 && actWorkPackage.getPlannedEndDate().compareTo(plannedEndDate)>=0){
							activity.setPlannedEndDate(plannedEndDate);
						}else{
							activity.setPlannedEndDate(actWorkPackage.getPlannedEndDate());
						}
					}
					
					
					activity.setBaselineStartDate(activity.getPlannedStartDate());
					activity.setBaselineEndDate(activity.getPlannedEndDate());
					
					if(activity.getActivityWorkPackage() == null){
						activity.setActivityWorkPackage(actWorkPackage);
					}
					activity.setCreatedDate(new Date());
					activity.setModifiedDate(new Date());
					activity.setCreatedBy(defultUser);
					activity.setModifiedBy(defultUser);				
					listOfActivities.add(activity);
					validRecordCount++;
					}
					if(rowNum > 0){
						isValidRowNum = rowNum;
						rowStatus = "Updated";
						statusRemarks = "Row uploaded successfully";
						invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);
					}
				} catch (Exception e) {
					log.error("Unable to read data of row number - "+(rowNum+1)+" due to "+e.getMessage()+"\n", e);
				}
			}
										     
			for(Entry<Integer, String> parentMap : invalidRowMap.entrySet()){			        	
	        	int invalidRowNumber = parentMap.getKey();
	        	String values = parentMap.getValue();	        	
        		String status = values.split(";")[0];
        		String remarks = values.split(";")[1];	        		
        		for(int i=totalHeaderCells;i<totalHeaderCells+2;i++){
        			row = sheet.getRow(invalidRowNumber);
        			Cell cell = row.createCell(i+1);
        			if(i==totalHeaderCells)
        				cell.setCellValue(status);
        			else if(i==totalHeaderCells+1)
        				cell.setCellValue(remarks);	        			
        		}        		        	
	        }
			
			try{
				File filePath = new File(reportFilePath);
				if(filePath == null || !filePath.exists() || !filePath.isDirectory()){
					filePath.mkdirs();
				}
				filePath = new File(reportFilePath+activityUploadReportFile);
				FileOutputStream fos = new FileOutputStream(filePath);
				workbook.write(fos);
			}catch(Exception ex){
				log.error("Exception occured in writeUploadReportFile - ", ex);
			}	         	   									
			
			invalidRecordCount =  totalRowCount - validRecordCount;
			message = "<br>Number of Records Inserted are  :"+Integer.toString(validRecordCount) +"<br>Number  Records not Inserted are :"+Integer.toString(invalidRecordCount);
			
			activityMap.put("message", message);
			activityMap.put("activities", listOfActivities);
			activityMap.put("filePathAndName", reportFilePath+activityUploadReportFile);
			
			if(errorMessages != null && !errorMessages.isEmpty()){
				String mailContent = "Hi\n\t\tUpload failed for some rows due to Invalid special charactes\n\n";
				for(String errorMessage : errorMessages){
					mailContent += "\t"+errorMessage+"\n";
				}
				mailContent += "\n\nRegards,\niLCM Team.";
				UserList user = (UserList)request.getSession().getAttribute("USER");
				if(emailNotification.equalsIgnoreCase("YES")){
					emailService.sendEmail("Upload failed due to Invalid special chars", mailContent, user.getEmailId(), null);
				}
			}
			return activityMap;
		}
		
		public boolean batchProcessActivities(HttpServletRequest request, List<Activity> listOfActivitiesToAdd, TestFactory testFactory, ProductMaster prodMaster) {
			boolean addStatus = true;
			int addedActivitiesCount = 0;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			if(listOfActivitiesToAdd !=null && !listOfActivitiesToAdd.isEmpty()){
		
				addedActivitiesCount = activityService.addActivitiesBulk(listOfActivitiesToAdd);
				if (addedActivitiesCount != listOfActivitiesToAdd.size()) {
					addStatus = false;	
				}
				for(Activity activity : listOfActivitiesToAdd){
					if(activity!=null && activity.getActivityId()!=null){
						Integer activityTypeId=activity.getActivityMaster() != null ?activity.getActivityMaster().getActivityMasterId():0;
						workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByEntityType(prodMaster.getProductId(), IDPAConstants.ENTITY_ACTIVITY_TYPE, activityTypeId, activity.getActivityId(), activity.getWorkflowStatus().getWorkflowStatusId(), activity.getCreatedBy(), activity.getPlannedStartDate(), activity);
						eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ACTIVITY, activity.getActivityId(), activity.getActivityName(), user);
					}
				}
			}
			
			if(listOfActivitiesToAdd != null && listOfActivitiesToAdd.size() >0) {
				for(Activity activity : listOfActivitiesToAdd){
					if(activity != null){
						List<Integer> listOfChangeRequestIds = entityRelationshipService.listEntityInstance2ByEntityInstance1(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID,IDPAConstants.CHANGE_REQUEST_ENTITY_MASTER_ID,activity.getActivityId());
						for(int changeReqId : listOfChangeRequestIds){							
								activityService.updateChangeRequestToActivityOneToMany(changeReqId, activity.getActivityId(), "map");							
						}
					}					
				}
			}
			return addStatus;
		}
		private boolean verifyExistingActivitiesByName(Activity activityFromExcel,Set<Activity> existingActivitiesName) {
			boolean duplicateAvtivate=false;
			for(Activity activitiesFromDB: existingActivitiesName){
				if(activityFromExcel.getActivityName().equalsIgnoreCase(activitiesFromDB.getActivityName())){
					duplicateAvtivate=true;
					break;
				}
			}
			return duplicateAvtivate;
		}
		
		private HashMap<String,List<Activity>> verifyExistingActivities(List<ActivitiesDTO> activitiesDTOExcel,ActivityWorkPackage activityWorkPackage) {
			HashMap<String,List<Activity>> activitiesMap= new HashMap<String,List<Activity>>();
			List<Activity> activitiesUpdate = new ArrayList<Activity>();
			List<Activity> activitiesNew = new ArrayList<Activity>();
			Set<Activity> existingActivitiesName = activityWorkPackage.getActivity();
			int existingActivitiesNamesize=existingActivitiesName.size();
				for(ActivitiesDTO activitiesDTO: activitiesDTOExcel){
					if (activitiesDTO.getActivity().getActivityName().trim() != null) {
						if(existingActivitiesNamesize>0){
							if (existingActivitiesName.contains(activitiesDTO.getActivity().getActivityName())) {
							} else {
								activitiesNew.add(activitiesDTO.getActivity());
							}
						}else {
							activitiesNew.add(activitiesDTO.getActivity());
						}
						
					}
				}
			
			
			activitiesMap.put("Update", activitiesUpdate);
			activitiesMap.put("New", activitiesNew);
			log.info("activitiesUpdate map Size: "+activitiesUpdate.size());
			log.info("activitiesNew map Size: "+activitiesNew.size());
			return activitiesMap;
		}
		
		
		@Override
		public String importChangeRequests(HttpServletRequest request, String excelDataFile, int productId, InputStream is, Integer entityType) {
			ProductMaster product = productListService.getProductById(productId);
			log.info("Start Time: "+Calendar.getInstance().getTime());
			String addStatus =	 readChangeRequestsFromExcelFile(request,excelDataFile, product, is, entityType);
			log.info("End Time: "+Calendar.getInstance().getTime());
			return addStatus;
			
		}
		
		
		private String readChangeRequestsFromExcelFile(HttpServletRequest request,String excelDataFile, ProductMaster product, InputStream fis, Integer entityType) {

			int blankRowCount = 0;
			URL url = null;
			log.info("Import - Excel file from location:" + excelDataFile);
			List<Integer> errorRows = new ArrayList<Integer>();
			List<ChangeRequestDTO> listOfChangeRequestDTO = null;
			boolean batchStatus = false;
			String message = " ";
			
			int numberOfSheets = 0;
			try { // 1

				Workbook workbook = null;
				if (excelDataFile.endsWith(".xls")) {
					workbook = new HSSFWorkbook(fis);
				}
				if (excelDataFile.endsWith(".xlsx")) {
					workbook = new XSSFWorkbook(fis);
				}

				// Get the worksheet containing the teststeps. It will be named 'TestSteps'.
				// If not available, get the first worksheet.
				numberOfSheets = workbook.getNumberOfSheets();
				if (numberOfSheets <= 0) {
					log.info("The Excel for Change Requests import does not conotain any worksheets");
				}
				Sheet sheet = null;
				for (int i = 0; i < numberOfSheets; i++) {
					if (workbook.getSheetName(i).equalsIgnoreCase("CHANGEREQUESTS")) {
						sheet = workbook.getSheetAt(i);
						break;
					}
				}
				if (sheet == null) {
					sheet = workbook.getSheetAt(0);
				}

				
				// Check if the sheet contains rows of teststeps
				int rowCount = sheet.getPhysicalNumberOfRows();
				int colCount = 0;
				int colNum = 0;
				int startIndex = 0;
				int endIndex = 0;
				int mod = 0;
				int numberOfCounters = 0;
				Integer maxRecordsToReadFromExcel = 0;
				if (rowCount < 1) {
					log.info("No rows present in the ChangeRequests worksheet");
				//	return false;
				} else { // 2
					if(activitiesReaderLimit == null || activitiesReaderLimit.length() == 0){
						maxRecordsToReadFromExcel = 10;
					}else{
						maxRecordsToReadFromExcel = Integer.parseInt(activitiesReaderLimit);
					}
					
					if(rowCount > maxRecordsToReadFromExcel){
						numberOfCounters = rowCount/maxRecordsToReadFromExcel;
						mod = rowCount%maxRecordsToReadFromExcel;
						if(mod > 0){
							numberOfCounters = numberOfCounters+1;
				    	}
						
						for(int batchCounter=1; batchCounter<=numberOfCounters; batchCounter++){
							
							if(batchCounter == 1){
				    			startIndex = 0;
				    			endIndex = batchCounter*maxRecordsToReadFromExcel;
				    		}else{
				    			startIndex = endIndex;
				    			if((rowCount-endIndex)> maxRecordsToReadFromExcel){
				    				endIndex = batchCounter*maxRecordsToReadFromExcel;
				    			}else{
				    				endIndex = rowCount;
				    				maxRecordsToReadFromExcel = endIndex-startIndex;
				    			}
				    		}
							// Logic to read data from file
							
							HashMap<String, Object> changeRequestMap =readChangeRequests(request, sheet,startIndex,endIndex,product,entityType);
							listOfChangeRequestDTO = (List<ChangeRequestDTO>) changeRequestMap.get("changeRequest");
							 message = (String) changeRequestMap.get("message");							
							
							batchStatus = batchProcessChangeRequests(listOfChangeRequestDTO, product.getProductId().intValue());
							if(batchStatus){
								log.info("Batch "+batchCounter+ " Start Row: "+startIndex+"  End Row: "+endIndex+" Completed successfully.");
							}else{
								log.info("Batch "+batchCounter+ " Start Row: "+startIndex+"  End Row: "+endIndex+" Failed !!!! ");
							}
						}
					}else{
						startIndex = 0;
			    		endIndex = rowCount;
			    		// Logic to read data from file			    		
			    		
			    		HashMap<String, Object> changeRequestMap =readChangeRequests(request, sheet,startIndex,endIndex,product,entityType);
						listOfChangeRequestDTO = (List<ChangeRequestDTO>) changeRequestMap.get("changeRequest");
					    message = (String) changeRequestMap.get("message");				    	
						batchStatus = batchProcessChangeRequests(listOfChangeRequestDTO, product.getProductId().intValue());
						if(batchStatus){
							log.info("Batch "+" Start Row: "+startIndex+"  End Row: "+endIndex+ " Completed successfully");
						}else{
							log.info("Batch "+" Start Row: "+startIndex+"  End Row: "+endIndex+ " Failed !!!!");
						}
						
					}
				} 
			} catch (IOException IOE) {
				log.error("Error reading change request from file", IOE);
				return "Error reading change request from file";
			} catch (Exception e) {
				log.error("Error reading change request from file", e);
				return "Error reading change request from file";
			}
			return message;
		}
		
		
		public HashMap<String,Object> readChangeRequests(HttpServletRequest request,Sheet sheet,int startIndex, int endIndex, ProductMaster product,Integer entityType){
		
			int validRecordCount = 0;
			int invalidRecordCount = 0;
			String message = " ";
			HashMap<String,Object> changeRequestMap = new HashMap<String,Object>();
			
			
			ChangeRequest changeRequest = null;
		    ChangeRequestDTO changeRequestDTO = null;
			List<ChangeRequestDTO> listOfChangeRequestDTO = null;
			listOfChangeRequestDTO = new ArrayList<ChangeRequestDTO>();
			Row row;
			int rowNum = 0;
			int blankRowCount = 0;
			int colCount = 0;
			int rowCount = sheet.getPhysicalNumberOfRows()-1;
			for (rowNum = startIndex; rowNum < endIndex; rowNum++) {
				//Row row;
				blankRowCount = 0;
				row = sheet.getRow(rowNum);
				if (row == null) {
					rowNum++;
					continue;
				}
				if (row.getRowNum() <= activitiesStartRow) {
					colCount = row.getLastCellNum();
					continue;
				} else { // 4

					// Read the Activity info
					changeRequest = new ChangeRequest();
					changeRequestDTO = new ChangeRequestDTO();
					boolean changeRequestNameIsMissing = false;
					boolean changeRequestIDIsMissing = false;

				
					// Change Request Name (Ref code from external system) or Activity Id (of iLCM) is mandatory. If any one is present not proceed or skip row
					Cell cell0 = row.getCell(changeRequestIDIndex);
					if (isCellValid(cell0)) {
						cell0.setCellType(Cell.CELL_TYPE_STRING);
						changeRequest.setChangeRequestId(Integer.parseInt(cell0.getStringCellValue().trim()));
					} else {
						changeRequestIDIsMissing = true;
					}
					
					// Change Request name is mandatory. If not present skip row
					Cell cell1 = row.getCell(changeRequestNameindex);
					if (isCellValid(cell1)) {
						cell1.setCellType(Cell.CELL_TYPE_STRING);
						changeRequest.setChangeRequestName(cell1.getStringCellValue().trim());
					} else {
						changeRequestNameIsMissing = true;
					}

					if (changeRequestIDIsMissing && changeRequestNameIsMissing) {
						blankRowCount++;
						rowNum++;
						log.info("Change Request name or id is missing in row #" + rowNum + ". Ignoring row");
						continue;
					}

					Cell cell2 = row.getCell(changeRequestDescriptionIndex);
					if (isCellValid(cell2)) {
						cell2.setCellType(Cell.CELL_TYPE_STRING);
						if (cell2.getStringCellValue() != null){
							changeRequest.setDescription(cell2.getStringCellValue());
						} 
					}
					
					Cell cell3 = row.getCell(changeRequestRaisedDateIndex);
					if (isCellValid(cell3)) {
						Date raisedDate = cell3.getDateCellValue();
						try {
							changeRequest.setRaisedDate(raisedDate);
						} catch (Exception e) {
							log.error("exception in parsing data value", e);
						}
					}else{
						try {
							changeRequest.setRaisedDate(new Date());
						} catch (Exception e) {
							log.error("exception in parsing data value", e);
						}
					}
					
					Cell cell4 = row.getCell(changeRequestPriorityIDIndex);
					ExecutionPriority priority = null;
					if (isCellValid(cell4)) {
						cell4.setCellType(Cell.CELL_TYPE_STRING);
						String priorityName = cell4.getStringCellValue();							
						priority = defectUploadService.getExecutionPriority(priorityName.trim());
						if(priority != null){
							changeRequest.setPriority(priority);
						}else{
							priority = new ExecutionPriority();
							priority.setExecutionPriorityId(1);
							changeRequest.setPriority(priority);
						}
					}else{
						priority = new ExecutionPriority();
						priority.setExecutionPriorityId(1);
						changeRequest.setPriority(priority);	
					}
					
													
					Cell cell5 = row.getCell(changeRequestOwnerIndex);
					UserList owner = null;
					if (isCellValid(cell5)) {
						cell5.setCellType(Cell.CELL_TYPE_STRING);
						String ownerLoginId = cell5.getStringCellValue();							
						owner = userListService.getUserByLoginId(ownerLoginId.trim());
						if(owner != null){
							changeRequest.setOwner(owner);	
						}else{
							owner = (UserList)request.getSession().getAttribute("USER");
							changeRequest.setOwner(owner);	
						}
					}else{
						owner = (UserList)request.getSession().getAttribute("USER");
						changeRequest.setOwner(owner);
					}
					
					
					Cell cell6 = row.getCell(changeRequestPlanedValueIndex);					
					if (isCellValid(cell6)) {
						cell6.setCellType(Cell.CELL_TYPE_NUMERIC);
						Integer plannedValue = (int) cell6.getNumericCellValue();	
						changeRequest.setPlanExpectedValue(plannedValue);						
					}else{
						changeRequest.setPlanExpectedValue(1);
					}								
					
					Cell cell7 = row.getCell(changeRequestStatusIndex);
					StatusCategory statusCategory = null;
					if (isCellValid(cell7)) {
						cell7.setCellType(Cell.CELL_TYPE_STRING);
						String statusCategoryName = cell7.getStringCellValue();
						statusCategory = statusService.getStatusCategoryByName(statusCategoryName);
					}
					if(statusCategory == null){
						statusCategory = new StatusCategory();
						statusCategory.setStatusCategoryId(1);
					}
					changeRequest.setStatusCategory(statusCategory);
					EntityMaster entityMaster = new EntityMaster();
					entityMaster.setEntitymasterid(entityType);
					changeRequest.setEntityType(entityMaster);
					changeRequest.setEntityInstanceId(product.getProductId());
					changeRequest.setProduct(product);					
					changeRequestDTO.setChangeRequest(changeRequest);
					listOfChangeRequestDTO.add(changeRequestDTO);
					validRecordCount++;
					
				}				
			
			}
			invalidRecordCount =	rowCount-validRecordCount;
			message = "<br> Number of Records Inserted are  :"+Integer.toString(validRecordCount) +"<br> Number  Records not Inserted are :"+Integer.toString(invalidRecordCount);
			changeRequestMap.put("message", message);
			changeRequestMap.put("changeRequest", listOfChangeRequestDTO);
			
			return changeRequestMap;
		}
		
		public boolean batchProcessChangeRequests(List<ChangeRequestDTO> changeRequestDTOList,int productId) {
			boolean addStatus = true;
			int addedChangeRequestCount = 0;
			ProductMaster product = productListService.getProductById(productId);
			HashMap<String,List<ChangeRequest>> changeRequestMap = verifyChangeRequests(changeRequestDTOList,product);
			if (changeRequestMap.size() > 0) {
				Set<String> set = changeRequestMap.keySet();
				for (String action : set) {
					if(action.equalsIgnoreCase("Update")){
					} else if(action.equalsIgnoreCase("New")){
						List<ChangeRequest> listOfChangeRequestToAdd = null;
						listOfChangeRequestToAdd = changeRequestMap.get(action);
						log.info("Activities to Add newly: "+listOfChangeRequestToAdd.size());
						if(listOfChangeRequestToAdd !=null && !listOfChangeRequestToAdd.isEmpty()){
							addedChangeRequestCount = changeRequestService.addChangeRequestBulk(listOfChangeRequestToAdd);
							if (addedChangeRequestCount != listOfChangeRequestToAdd.size()) {
								addStatus = false;	
							}
						}
					}
				}
			}
			return addStatus;
		}
		
		private HashMap<String,List<ChangeRequest>> verifyChangeRequests(List<ChangeRequestDTO> changeRequestDTOList,ProductMaster product) {
			HashMap<String,List<ChangeRequest>> changeRequestsMap = new HashMap<String,List<ChangeRequest>>();
			List<ChangeRequest> changeRequestUpdate = new ArrayList<ChangeRequest>();
			List<ChangeRequest> changeRequestNew = new ArrayList<ChangeRequest>();
			for(ChangeRequestDTO changeRequestDTO: changeRequestDTOList){
				List<String> existingChangeRequestName = null;
				existingChangeRequestName = changeRequestService.getExistingChangeRequestNames(product);
				if (changeRequestDTO.getChangeRequest().getChangeRequestName().trim() != null) {
					if (existingChangeRequestName.contains(changeRequestDTO.getChangeRequest().getChangeRequestName().trim())) {
						changeRequestUpdate.add(changeRequestDTO.getChangeRequest());
					} else {
						changeRequestNew.add(changeRequestDTO.getChangeRequest());
					}
				}
			}
			changeRequestsMap.put("Update", changeRequestUpdate);
			changeRequestsMap.put("New", changeRequestNew);
			log.info("changeRequestUpdate map Size: "+changeRequestUpdate.size());
			log.info("changeRequestNew map Size: "+changeRequestNew.size());
			return changeRequestsMap;
		}
private ExecutionPriority getExecutionPriorityByexecutionPriorityName(String executionPriorityName){
	ExecutionPriority executionPriority=new ExecutionPriority();
	if(executionPriorityName.trim().equalsIgnoreCase(IDPAConstants.EXECUTION_PRIORITY_CRITICAL)){
		executionPriority.setExecutionPriorityId(1);
		executionPriority.setExecutionPriorityValue(1);
		executionPriority.setDisplayName("*****");
		executionPriority.setExecutionPriorityName(IDPAConstants.EXECUTION_PRIORITY_CRITICAL);
		executionPriority.setExecutionPriority(IDPAConstants.EXECUTION_PRIORITY_CRITICAL);
		
	}if(executionPriorityName.trim().equalsIgnoreCase(IDPAConstants.EXECUTION_PRIORITY_HIGH)){
		executionPriority.setExecutionPriorityId(2);
		executionPriority.setExecutionPriorityValue(2);
		executionPriority.setDisplayName("****");
		executionPriority.setExecutionPriorityName(IDPAConstants.EXECUTION_PRIORITY_HIGH);
		executionPriority.setExecutionPriority(IDPAConstants.EXECUTION_PRIORITY_HIGH);
		
	}if(executionPriorityName.trim().equalsIgnoreCase(IDPAConstants.EXECUTION_PRIORITY_MAJOR)){
		executionPriority.setExecutionPriorityId(3);
		executionPriority.setExecutionPriorityValue(3);
		executionPriority.setDisplayName("***");
		executionPriority.setExecutionPriorityName(IDPAConstants.EXECUTION_PRIORITY_MAJOR);
		executionPriority.setExecutionPriority(IDPAConstants.EXECUTION_PRIORITY_MAJOR);
		
	}if(executionPriorityName.trim().equalsIgnoreCase(IDPAConstants.EXECUTION_PRIORITY_MEDIUM)){
		executionPriority.setExecutionPriorityId(4);
		executionPriority.setExecutionPriorityValue(4);
		executionPriority.setDisplayName("**");
		executionPriority.setExecutionPriorityName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
		executionPriority.setExecutionPriority(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
		
	}if(executionPriorityName.trim().equalsIgnoreCase(IDPAConstants.EXECUTION_PRIORITY_TRIVIAL)){
		executionPriority.setExecutionPriorityId(5);
		executionPriority.setExecutionPriorityValue(5);
		executionPriority.setDisplayName("*");
		executionPriority.setExecutionPriorityName(IDPAConstants.EXECUTION_PRIORITY_TRIVIAL);
		executionPriority.setExecutionPriority(IDPAConstants.EXECUTION_PRIORITY_TRIVIAL);
		
	}
	return executionPriority;
	
}
private WorkflowStatus getActivityStatusBystatusName(List<WorkflowStatus> activityStatuslist,String activityStatusName){
	
	WorkflowStatus activityStatus=null;
	if(activityStatuslist != null && activityStatuslist.size() >0){
		for(WorkflowStatus activitySta:activityStatuslist){
			if(activitySta.getWorkflowStatusName().equalsIgnoreCase(activityStatusName)){
				activityStatus=activitySta;
				break;
			}
		}
	}
	return activityStatus;
}

private ActivitySecondaryStatusMaster getActivitySecondaryStatusBystatusName(List<ActivitySecondaryStatusMaster> activitySecondaryStatusMasterList,String secondaryStatusName){
	
	ActivitySecondaryStatusMaster activitySecondaryStatusMaster=null;
	if(activitySecondaryStatusMasterList !=null && activitySecondaryStatusMasterList.size() >0) {
		for(ActivitySecondaryStatusMaster secondaryStatus:activitySecondaryStatusMasterList){
			if(secondaryStatus.getActivitySecondaryStatusName().equalsIgnoreCase(secondaryStatusName)){
				activitySecondaryStatusMaster=secondaryStatus;
				break;
			}
		}
	}
	return activitySecondaryStatusMaster;
}

private ActivityResult getActivityResultbyName(List<ActivityResult> activityResultList,String resultName){
	
	ActivityResult activityResult=null;
	if(activityResultList!=null && activityResultList.size() >0) {
		for(ActivityResult result:activityResultList){
			if(result.getActivityResultName().equalsIgnoreCase(resultName)){
				activityResult=result;
				break;
			}
		}
	}
	return activityResult;
}

public ActivityMaster getActivityMasterByActivityMasterName(List<ActivityMaster> activityMasterList,String activityMasterName){
	ActivityMaster activityMaster=null;
	if(activityMasterList != null && activityMasterList.size()>0) {
		for(ActivityMaster activityMastr:activityMasterList){
			if(activityMastr.getActivityMasterName().trim().equalsIgnoreCase(activityMasterName.trim()) ||activityMasterName.equals(activityMastr.getActivityMasterName())  ){
				activityMaster=activityMastr;
				break;
			}
		}
	}
	return activityMaster;
}




	private String readActvityTasksFromExcelFile(HttpServletRequest request, String excelDataFile, ActivityWorkPackage  activityWorkPackage, InputStream fis) {
		String  message = " ";
		int blankRowCount = 0;
		URL url = null;
		log.info("Import - Excel file from location:" + excelDataFile);
		List<Integer> errorRows = new ArrayList<Integer>();
		
		List<ActivityTasksDTO> listOfActivityTaskDTO = null;
		List<ActivityTask> listOfActivityTaskFromExcel = null;
		boolean batchStatus = false;
		
		
		int numberOfSheets = 0;
		try { // 1
			
			Workbook workbook = null;
			if (excelDataFile.endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			}
			if (excelDataFile.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fis);
			}
	
			// Get the worksheet containing the teststeps. It will be named 'TestSteps'.
			// If not available, get the first worksheet.
			numberOfSheets = workbook.getNumberOfSheets();
			if (numberOfSheets <= 0) {
				log.info("The Excel for Activity task import does not contain any worksheets");
			}
			Sheet sheet = null;
			for (int i = 0; i < numberOfSheets; i++) {
				if (workbook.getSheetName(i).equalsIgnoreCase("ActivityTasks")) {
					sheet = workbook.getSheetAt(i);
					
					break;
				}
			}
			if (sheet == null) {
				sheet = workbook.getSheetAt(0);
			}
	
			
			// Check if the sheet contains rows of teststeps
			int rowCount = sheet.getPhysicalNumberOfRows();
			int colCount = 0;
			int colNum = 0;
			int startIndex = 0;
			int endIndex = 0;
			int mod = 0;
			int numberOfCounters = 0;
			Integer maxRecordsToReadFromExcel = 0;
			if (rowCount < 1) {
				log.info("No rows present in the task worksheet");
			} else { // 2
				
				HashMap<String, Object> activityTaskMap = readActivityTasks(request,sheet,rowCount,startIndex,endIndex,activityWorkPackage);
				  message =(String) activityTaskMap.get("message");
				listOfActivityTaskFromExcel = (List<ActivityTask>) activityTaskMap.get("activityTask");
			
				ProductMaster product=activityWorkPackage.getProductBuild().getProductVersion().getProductMaster();//Need for Workflow status policy table 
				batchStatus = batchProcessActivityTask(listOfActivityTaskFromExcel,product.getProductId());
				if(batchStatus){
					log.info("Batch "+0+ " Start Row: "+startIndex+"  End Row: "+endIndex+" Completed successfully.");
				}else{
					log.info("Batch "+0+ " Start Row: "+startIndex+"  End Row: "+endIndex+" Failed !!!! ");
				}
			
			} 
		} catch (IOException IOE) {
			log.error("Error reading activity task from file", IOE);
			return "Error reading activity task from file";
		} catch (Exception e) {
			log.error("Error reading activity task from file", e);
			return "Error reading activity task from file";
		}
		return message;
	}
	
	public boolean batchProcessActivityTask(List<ActivityTask> listOfActivityTasksToAdd,Integer productId) {
		boolean addStatus = true;
		int addedActivityTasksCount = 0;
		if(listOfActivityTasksToAdd !=null && !listOfActivityTasksToAdd.isEmpty()){
			addedActivityTasksCount = activityTaskService.addActivityTasksBulk(listOfActivityTasksToAdd,productId);
			if (addedActivityTasksCount != listOfActivityTasksToAdd.size()) {
				addStatus = false;	
			}
			for(ActivityTask activityTask : listOfActivityTasksToAdd){
				Integer entityId = 0;
				if(activityTask != null && activityTask.getActivityTaskId() != null && activityTask.getActivityTaskType() != null){
					entityId = activityTask.getActivityTaskType().getActivityTaskTypeId();
				}
				workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByEntityType(productId, IDPAConstants.ENTITY_TASK_TYPE, entityId, activityTask.getActivityTaskId(), activityTask.getStatus().getWorkflowStatusId(), activityTask.getCreatedBy(), activityTask.getPlannedStartDate(), activityTask);
			}
		}
		
		return addStatus;
	}
	
	public HashMap<String,Object> readActivityTasks(HttpServletRequest request,Sheet sheet,int rowCount,int startIndex, int endIndex, ActivityWorkPackage activityWorkPackage){
		   
		int validRecordCount = 0;
		int invalidRecordCount = 0;
		String message = " ";
		HashMap<String,Object> activityTaskMap = new HashMap<String,Object>();

		
			ActivityTask activityTask = null;
			List<ActivityTask> listOfActivityTask = new ArrayList<ActivityTask>();
			
			Row row;
			int rowNum = 0;
			int blankRowCount = 0;
			int taskRowCountDuplicate=0;
			int colCount = 0;
			List<WorkflowStatus> activityStatuslist=null;
			List<ActivitySecondaryStatusMaster> activitySecondaryStatusList=null;
			List<ActivityResult> activityResulList=null;
			Set<ActivityMaster> activityMasterList=null;
			List<ActivityTaskType> activityTaskTypes=null;
			List<ExecutionTypeMaster> executionTypeMasterList=null;
			Map<String,ProductFeature> productFeatureMap=new HashMap<String,ProductFeature>();
			Map<String,Activity> activityMap=new HashMap<String,Activity>();
			Map<String,EnvironmentCombination> environmentMap=new HashMap<String,EnvironmentCombination>();
			Map<String,UserList> userListMap=new HashMap<String,UserList>();
			Map<String,WorkflowStatus> statusListMap=new HashMap<String,WorkflowStatus>();
			Map<String,ActivityTaskType> activityTaskTypeMap=new HashMap<String,ActivityTaskType>();
			ProductMaster product=activityWorkPackage.getProductBuild().getProductVersion().getProductMaster();
			TestFactory testFactory = product.getTestFactory();
			Activity activity = null;
			List<String> activityTaskNames = null;
			int invalidRowCount = 0;
			List<String> errorMessages = new ArrayList<String>();
			for (rowNum = 0; rowNum <= rowCount; rowNum++) {
				//Row row;
				blankRowCount = 0;
				row = sheet.getRow(rowNum);
				if (row == null) {
					rowNum++;
					continue;
				}
				if (row.getRowNum() <= taskStartRow) {
					colCount = row.getLastCellNum();
					continue;
				} else { // 4

					// Read the Activity task info
					activityTask = new ActivityTask();
					boolean activityNameIsMissing = false;
					boolean taskNameIsMissing = false;
					boolean taskNameIsDuplicate=false;

				
					///Check if Activity Work Package Id is available. Either Activity Work Package Name or Activity Work Package Id is mandatory.
					String activityWorkPackageId = null;
					
					if(rowNum==1){
						activityTaskTypes=activityTaskService.listActivityTaskTypes(testFactory.getTestFactoryId(), product.getProductId(), 1, null, null, true);
					}
					
					if(rowNum==1){
						executionTypeMasterList=executionTypeMasterService.listbyEntityMasterId(1);
					}
					
					if(rowNum==1){
					}
					
					if(rowNum==1){
						List<Integer>competencyList=dimensionService.getDimensionIdByProductId(product.getProductId(),DIMENTIONTYPEID );
						if(competencyList!=null && competencyList.size() >0){
							activitySecondaryStatusList=activitySecondaryStatusMasterService.getSecondaryStatusbyDimentionId(competencyList.get(0));
						}
					}
					
					if(rowNum==1){
						activityResulList=activityResultService.listActivityResult();
					}
					
					// Teststep name is mandatory. If not present skip row
					Cell cell1 = row.getCell(activityNameindex);
					if (isCellValid(cell1)) {
						cell1.setCellType(Cell.CELL_TYPE_STRING);
						String activityName=cell1.getStringCellValue().trim();
						if(activityMap.containsKey(activityName)){
							activityTask.setActivity(activityMap.get(activityName));
						}else {
							activity = new Activity();
							log.info("Row number:"+rowNum+"WorkPackage ID:"+activityWorkPackage.getActivityWorkPackageId());
							log.info("Row number:"+rowNum+"Activity Name:"+activityName);
							activity=activityService.getActivityByWorkpackageIdAndActivityName(activityWorkPackage.getActivityWorkPackageId(),activityName);
							if(activity !=null){
								activityTask.setActivity(activity);
								activityMap.put(activityName,activity);
							}else {
								
								activityNameIsMissing = true;
							}
							
						}
					} else {
						
						activityNameIsMissing = true;
					}

					if (activityNameIsMissing) {
						blankRowCount++;
						log.info("Activity name or id is missing in row #" + rowNum + ". Ignoring row");
						continue;
					}
					
					
					Cell cell2 = row.getCell(taskNameindex);
					if (isCellValid(cell2)) {
						cell2.setCellType(Cell.CELL_TYPE_STRING);
						String taskName = cell2.getStringCellValue().trim();
						if(taskName.matches(".*"+specialChars+".*")){
							invalidRowCount = invalidRowCount+1;
							errorMessages.add("Data of Row : "+invalidRowCount+" with TaskName : "+taskName+" is failed to upload");
							continue;
						}else{
							if(activityTask != null) {
							activityTask.setActivityTaskName(cell2.getStringCellValue().trim());
								Set<ActivityTask> activityTasksFromDB = activity.getActivityTask();
								activityTaskNames = new ArrayList<String>();
	 							for(ActivityTask actTask : activityTasksFromDB){
									if(!activityTaskNames.contains(actTask.getActivityTaskName())){
										taskNameIsDuplicate = verifyExistingActivityTasksByName(activityTask, activityTasksFromDB);
										if(!taskNameIsMissing){
											activityTaskNames.add(actTask.getActivityTaskName());
										}
									}
									else{
										taskNameIsDuplicate = true;
									}
	 							}
							}
	 							
						}
					} else {
						
						taskNameIsMissing = true;
					}

					if (taskNameIsMissing) {
						blankRowCount++;
						log.info("Task Name or id is missing in row #" + rowNum + ". Ignoring row");
						continue;
					}

					if(taskNameIsDuplicate){
						taskRowCountDuplicate++;
						log.info("Task Name is duplicate in row #" + rowNum + ". Ignoring row");
						continue;
					}
			
					
					
					Cell cell3 = row.getCell(activityTaskTypeindex);
					ActivityTaskType activityTaskType = null;
					ActivityTaskType uploadActivityTaskType = null;
					if (isCellValid(cell3)) {
						cell3.setCellType(Cell.CELL_TYPE_STRING);
						String taskType = cell3.getStringCellValue();
						if(activityTaskTypeMap.containsKey(taskType)) {
							activityTaskType=activityTaskTypeMap.get(taskType);
						} else {
							if(activityTaskTypes != null && activityTaskTypes.size()>0){
								activityTaskType=getActivityTaskTypeName(activityTaskTypes,taskType.trim());
							} 
								
							if(activityTaskType == null) {
								uploadActivityTaskType = new ActivityTaskType();
								uploadActivityTaskType.setActivityTaskTypeName(taskType);
								uploadActivityTaskType.setActivityTaskTypeDescription(taskType);
								uploadActivityTaskType.setProduct(product);
								uploadActivityTaskType.setTestFactory(testFactory);
								activityTaskService.addActivityTaskType(uploadActivityTaskType);
							}
							 		
						}
						if(activityTaskType != null){
						   	activityTask.setActivityTaskType(activityTaskType);
						   	activityTaskTypeMap.put(taskType, activityTaskType);
						} else if(uploadActivityTaskType != null) {
							activityTask.setActivityTaskType(uploadActivityTaskType);
							activityTaskTypeMap.put(taskType, uploadActivityTaskType);
						}else {
						  	activityTask.setActivityTaskType(null);
						}
					}else{
						activityTask.setActivityTaskType(null);	
					}
					
					
					Cell cell4 = row.getCell(taskCategoryIndex);
					ExecutionTypeMaster category = null;
					
					if (isCellValid(cell4)) {
						cell4.setCellType(Cell.CELL_TYPE_STRING);
						String categoryName = cell4.getStringCellValue();
						for(ExecutionTypeMaster executionTypeMaster:executionTypeMasterList){
							if(executionTypeMaster.getName().equalsIgnoreCase(categoryName.trim())){
								category=executionTypeMaster;
								break;
							}
						}
						if(category != null){
							activityTask.setCategory(category);		
						}else{
							activityTask.setCategory(null);	
						}
					}else{						
						activityTask.setCategory(null);	
					}
					
					Cell cell5 = row.getCell(environmentCombinationIndex);
					String enviromentCombinationName = "";
					EnvironmentCombination environmentCombination=null;
					if (isCellValid(cell5)) {
						cell5.setCellType(Cell.CELL_TYPE_STRING);
						if (cell5.getStringCellValue() != null){
							environmentCombination=new EnvironmentCombination();
							enviromentCombinationName = cell5.getStringCellValue().trim();
							if(environmentMap.containsKey(enviromentCombinationName)){
								activityTask.setEnvironmentCombination(environmentMap.get(enviromentCombinationName));
							}else {
								environmentCombination=environmentService.listEnvironmentCombinationByNameNoIntialize(product.getProductId(), enviromentCombinationName);
								if(environmentCombination!=null){
									activityTask.setEnvironmentCombination(environmentCombination);
									environmentMap.put(enviromentCombinationName, environmentCombination);
									activityService.updateEnvironmentCombiToActivityOneToMany(environmentCombination.getEnvironment_combination_id(), activity.getActivityId(), "map");
								}else{
									activityTask.setEnvironmentCombination(null);
								}
							}
							
						} 
					}else {
						activityTask.setEnvironmentCombination(null);
					}
					
					
				
					
					Cell cell6 = row.getCell(taskAssigneeIndex);
					UserList assignee = null;
					UserList defultUser=new UserList();
					defultUser=(UserList)request.getSession().getAttribute("USER");
					if (isCellValid(cell6)) {
						cell6.setCellType(Cell.CELL_TYPE_STRING);
						String assigneeLoginId = cell6.getStringCellValue().trim();		
						if(userListMap.containsKey(assigneeLoginId)){
							assignee =userListMap.get(assigneeLoginId);
						}else{
							assignee = userListService.getProductTeamResourcesByUserName(product.getProductId(),assigneeLoginId,activityWorkPackage.getPlannedStartDate(),activityWorkPackage.getPlannedEndDate());
						}
						if(assignee != null){
							activityTask.setAssignee(assignee);	
							userListMap.put(assigneeLoginId, assignee);
						}else if(activityWorkPackage.getOwner()!=null){
							activityTask.setAssignee(activityWorkPackage.getOwner());
						}else {
							activityTask.setAssignee(defultUser);
						}
					}else{

						if(activityWorkPackage.getOwner()!=null){
							activityTask.setAssignee(activityWorkPackage.getOwner());
						}else {
							activityTask.setAssignee(defultUser);
						}
					}
					
					
					Cell cell7 = row.getCell(taskReviewerIndex);
					UserList reviewer = null;
					if (isCellValid(cell7)) {
						cell7.setCellType(Cell.CELL_TYPE_STRING);
						String reviewerLoginId = cell7.getStringCellValue().trim();	
						if(userListMap.containsKey(reviewerLoginId)){
							reviewer =userListMap.get(reviewerLoginId);
						}else{
							reviewer = userListService.getProductTeamResourcesByUserName(product.getProductId(),reviewerLoginId,activityWorkPackage.getPlannedStartDate(),activityWorkPackage.getPlannedEndDate());
						}
						
						if(reviewer != null){
							activityTask.setReviewer(reviewer);	
							userListMap.put(reviewerLoginId, reviewer);
						}else if(activityWorkPackage.getOwner()!=null){
							activityTask.setReviewer(activityWorkPackage.getOwner());
						}else{
							activityTask.setReviewer(defultUser);
						}
					}else{
						if(activityWorkPackage.getOwner()!=null){
							activityTask.setReviewer(activityWorkPackage.getOwner());
						}else{
							activityTask.setReviewer(defultUser);
						}
					}
					
					WorkflowStatus workflowStatus = null;
					Integer activityTaskTypeId = 0;
					if(activityTask.getActivityTaskType() != null && activityTask.getActivityTaskType().getActivityTaskTypeId() != null){
						activityTaskTypeId = activityTask.getActivityTaskType().getActivityTaskTypeId();
					}
					if(statusListMap.containsKey(activityTaskTypeId+"")){
						workflowStatus = statusListMap.get(activityTaskTypeId+"");
					}else{
						workflowStatus = workflowStatusService.getInitialStatusForInstanceByEntityType(product.getProductId(), IDPAConstants.ENTITY_TASK_TYPE, activityTaskTypeId);
					}
					if(workflowStatus == null || workflowStatus.getWorkflowStatusId() == null || workflowStatus.getWorkflowStatusId() == 0){
						workflowStatus = workflowStatusService.getWorkflowStatusById(-1);
					}
					statusListMap.put(activityTaskTypeId+"", workflowStatus);
					activityTask.setStatus(workflowStatus);					
					if(workflowStatus.getWorkflowStatusId() == -1){
						activityTask.setActualStartDate(new Date());
						activityTask.setActualEndDate(new Date());
					}
					if(activity.getLifeCycleStage() != null){
						activityTask.setLifeCycleStage(activity.getLifeCycleStage());
					}else if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflowStatusId() > 0){
						activityTask.setLifeCycleStage(activityWorkPackage.getWorkflowStatus());
					}
					
					Cell cell10 = row.getCell(taskResultIndex);
					ActivityResult activityResult = null;
					if (isCellValid(cell10)) {
						cell10.setCellType(Cell.CELL_TYPE_STRING);
						String resultName = cell10.getStringCellValue();
						activityResult =getActivityResultbyName(activityResulList,resultName.trim());
						if(activityResult != null){
							activityTask.setResult(activityResult);
						}else{
							if(activityResulList!=null && activityResulList.size()>0) {
								activityResult = activityResulList.get(0);
								activityTask.setResult(activityResult);
							}
						}
					}else{
						if(activityResulList!=null && activityResulList.size()>0) {
							activityResult = activityResulList.get(0);
							activityTask.setResult(activityResult);
						}
					}
					
					
					
					Cell cell11 = row.getCell(taskPlannedStartDateIndex);
					Date plannedStartDate = null;
					if (isCellValid(cell11)) {
						plannedStartDate = cell11.getDateCellValue();
						try {
							activityTask.setPlannedStartDate(plannedStartDate);
						} catch (Exception e) {
							log.error("exception in parsing data value", e);
						}
					}else{
						try {
							if(activity.getPlannedStartDate()!=null){
								activityTask.setPlannedStartDate(activity.getPlannedStartDate());
							}else{
								activityTask.setPlannedStartDate(new Date());
							}
						} catch (Exception e) {
							log.error("exception in parsing data value", e);
						}
					}
					
					Cell cell12 = row.getCell(taskPlannedEndDateIndex);
					Date plannedEndDate = null;
					if (isCellValid(cell12)) {
						plannedEndDate = cell12.getDateCellValue();
						try {
							activityTask.setPlannedEndDate(plannedEndDate);
						} catch (Exception e) {
							log.error("exception in parsing data value ", e);
						}
					}else{
						try {
							if(activity.getPlannedEndDate()!=null){
								activityTask.setPlannedEndDate(activity.getPlannedEndDate());
							}else{
								activityTask.setPlannedEndDate(new Date());
							}
						} catch (Exception e) {
							log.error("exception in parsing data value", e);
						}
					}
					
					if(plannedStartDate!=null){					
						if(activity.getPlannedStartDate().compareTo(plannedStartDate) <=0 && activity.getPlannedEndDate().compareTo(plannedStartDate)>=0){
							activityTask.setPlannedStartDate(plannedStartDate);
						}else{
							activityTask.setPlannedStartDate(activity.getPlannedStartDate());
						}
					}
					if(plannedEndDate!=null){					
						if(activity.getPlannedStartDate().compareTo(plannedEndDate) <=0 && activity.getPlannedEndDate().compareTo(plannedEndDate)>=0){
							activityTask.setPlannedEndDate(plannedEndDate);
						}else{
							activityTask.setPlannedEndDate(activity.getPlannedEndDate());
						}
					}
					
					Cell cell13 = row.getCell(taskPlannedSizeIndex);
					if (isCellValid(cell13)) {
						cell13.setCellType(Cell.CELL_TYPE_NUMERIC);
						Integer plandSize = (int) cell13.getNumericCellValue();
						activityTask.setPlannedTaskSize(plandSize);	
					}else{
						activityTask.setPlannedTaskSize(0);
					}
					activityTask.setBaselineTaskSize(activityTask.getPlannedTaskSize());
					
					Cell cell14 = row.getCell(taskactualSizeIndex);
					if (isCellValid(cell14)) {
						cell14.setCellType(Cell.CELL_TYPE_NUMERIC);
						Integer actualsize = (int) cell14.getNumericCellValue();
						activityTask.setActualTaskSize(actualsize);
						
					}else{
						activityTask.setActualTaskSize(0);
					}
					
					activityTask.setBaselineStartDate(activityTask.getPlannedStartDate());
					activityTask.setBaselineEndDate(activityTask.getPlannedEndDate());
					
					activityTask.setCreatedBy(defultUser);
					activityTask.setModifiedBy(defultUser);					
					listOfActivityTask.add(activityTask);
					
					validRecordCount++;
					
				}
			}
			
			invalidRecordCount =  rowCount - validRecordCount;
			message = "<br>Number of Records Inserted are  :"+Integer.toString(validRecordCount) +"<br> Number  Records not Inserted are :"+Integer.toString(invalidRecordCount);
			
			activityTaskMap.put("message",message);
			activityTaskMap.put("activityTask", listOfActivityTask);
			
			if(errorMessages != null && !errorMessages.isEmpty()){
				String mailContent = "Hi,\n\t\tUpload failed for some rows due to Invalid special characters\n\n";
				for(String errorMessage : errorMessages){
					mailContent += "\t"+errorMessage+"\n";
				}
				mailContent+="\n\nRegards,\nILCM Team.";
				UserList user = (UserList)request.getSession().getAttribute("USER");
				
				if(emailNotification.equalsIgnoreCase("YES")){
					emailService.sendEmail("Upload failed due to Invalid special chars", mailContent, user.getEmailId(), null);
				}
			}
			return activityTaskMap;
		}
	
	
	public ActivityTaskType getActivityTaskTypeName(List<ActivityTaskType> activityTaskTypeList,String taskTypeName){
		ActivityTaskType activityTaskType=null;
		for(ActivityTaskType taskType:activityTaskTypeList){
			if(taskType.getActivityTaskTypeName().equalsIgnoreCase(taskTypeName)){
				activityTaskType=taskType;
				break;
			}
		}
		return activityTaskType;
	}
		
	private boolean verifyExistingActivityTasksByName(ActivityTask activityTaskFromExcel,Set<ActivityTask> existingActivityTasksName) {
		boolean duplicateActivityTask=false;
		for(ActivityTask activityTasksFromDB: existingActivityTasksName){
			if(activityTaskFromExcel.getActivityTaskName().equalsIgnoreCase(activityTasksFromDB.getActivityTaskName())){
				duplicateActivityTask=true;
				
				break;
			}
		}
		return duplicateActivityTask;
	}
	
	@Override
	public String importWorkflowTemplateStatus(HttpServletRequest request,String excelDataFile, InputStream is) {
		String addStatus =" ";
				log.info("Start Time: "+Calendar.getInstance().getTime());
				HashMap<String,Object> workFLowTemplate =readWorkflowTemplateStatusFromExcelFile(request,excelDataFile, is);			
			addStatus =	(String) workFLowTemplate.get("message");				
				log.info("End Time: "+Calendar.getInstance().getTime());
				return addStatus;		
	}
	public HashMap<String,Object> readWorkflowTemplateStatusFromExcelFile(HttpServletRequest request, String excelDataFile, InputStream fis) {
		
		int blankRowCount = 0;
		URL url = null;

		int validRecordCount = 0;
		int invalidRecordCount = 0;
		String message = " ";
		HashMap<String,Object> workFLowTemplatsMap = new HashMap<String,Object>();
			
		log.info("Import - Excel file from location:" + excelDataFile);		
		
		int numberOfSheets = 0;
		try { // 1
	
			Workbook workbook = null;
			if (excelDataFile.endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			}
			if (excelDataFile.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fis);
			}
	
			// Get the worksheet containing the teststeps. It will be named 'TestSteps'.
			// If not available, get the first worksheet.
			numberOfSheets = workbook.getNumberOfSheets();
			if (numberOfSheets <= 0) {
				log.info("The Excel for WorkflowTemplateStatus  import does not contain any worksheets");
			}
			Sheet sheet = null;
			for (int i = 0; i < numberOfSheets; i++) {
				if (workbook.getSheetName(i).equalsIgnoreCase("WorkFlow Template Staus")) {
					sheet = workbook.getSheetAt(i);					
					break;
				}
			}
			if (sheet == null) {
			}
		
			Row row;
			int rowCount = sheet.getPhysicalNumberOfRows();
			int colCount = 0;
			int colNum = 0;
			int startIndex = 0;
			int endIndex = 0;
			int mod = 0;
			int numberOfCounters = 0;
			Integer maxRecordsToReadFromExcel = 0;
			int totalRecordsCount = sheet.getPhysicalNumberOfRows() - 1;
			if (rowCount < 1) {
				log.info("No rows present in the WorkflowTemplateStatus worksheet");
			} else { // 2
					
				WorkflowStatusPolicy	workFlowStatusPolicy = null;
				WorkflowStatus workFlowStatus = null;
				StatusCategory statusCategory = null;
				WorkflowMaster workFlowMaster = null;
				List<WorkflowStatusPolicy> workFlowStatuPolicyList = null;	
				HashMap<String, WorkflowMaster> workFlowMasterMap = new HashMap<String, WorkflowMaster>();				
				HashMap<Integer,List<String>> workFlowStatusNameMap = new HashMap<Integer,List<String>>();
				List<WorkflowStatusCategory> workFlowStatusCategoryList = null;
				HashMap<Integer,HashMap<String, WorkflowStatusCategory>> workFLowStatusCategoryMap = new HashMap<Integer,HashMap<String, WorkflowStatusCategory>>();
				
				int rowNum;
				for (rowNum = 1; rowNum <= rowCount; rowNum++) {
					//Row row;
					blankRowCount = 0;
					row = sheet.getRow(rowNum);
					if (row == null) {
						rowNum++;
						continue;
					}else{						
						workFlowStatusPolicy = new WorkflowStatusPolicy();
						workFlowStatus = new WorkflowStatus();
						WorkflowStatusCategory workFlowStatusCategory = null;
						workFlowMaster = null;
						List<WorkflowMaster> workFlowMasterList = null;	
						List<String> workFlowStatusNameList = null;
						
						String workFlowTemplateName = null;
						Integer workflowId = null;
						Cell cell0 = row.getCell(workFlowTemplateNameIndex);
						if (isCellValid(cell0)) {
							cell0.setCellType(Cell.CELL_TYPE_STRING);
							if (cell0.getStringCellValue() != null) {
								workFlowTemplateName = cell0.getStringCellValue().trim();								
								if(workFlowMasterMap.containsKey(workFlowTemplateName)){
									workFlowMaster = workFlowMasterMap.get(workFlowTemplateName);
								}else{
									workFlowMasterList =	workFlowMasterService.getWorkFlowMasterListByWorkflowName(workFlowTemplateName);
									if(workFlowMasterList != null && workFlowMasterList.size() > 0){
										workFlowMaster = workFlowMasterList.get(0);
										workFlowMasterMap.put(workFlowTemplateName, workFlowMaster);
									}
								}
								
							} else {
								log.info("workFlowTemplateName  in row #" + rowNum+ " does not have statusName Name");
							}
						} 
						if(workFlowMaster == null){
							continue;
						}else{
							workflowId = workFlowMaster.getWorkflowId();
						}
						String statusName = null;
						Cell cell1 = row.getCell(statusNameIndex);
						if (isCellValid(cell1)) {
							cell1.setCellType(Cell.CELL_TYPE_STRING);
							if (cell1.getStringCellValue() != null){
								statusName = cell1.getStringCellValue().trim();								
								if(workFlowStatusNameMap.containsKey(workflowId) && workFlowStatusNameMap.get(workflowId).contains(statusName)){
									continue;
								}else if((workFlowStatusNameMap.containsKey(workflowId) && !workFlowStatusNameMap.get(workflowId).contains(statusName)) || !workFlowStatusNameMap.containsKey(workflowId)){
									if(!workFlowStatusNameMap.containsKey(workflowId)){
										workFlowStatusNameList = workflowStatusPolicyService.getWorkFlowStatuNamebyWorkFlowId(workflowId);
										workFlowStatusNameMap.put(workflowId, workFlowStatusNameList);
									}else{
										workFlowStatusNameMap.get(workflowId).add(statusName);
									}
									workFlowStatus.setWorkflowStatusName(statusName);
								}
							} else {
								log.info("statusName  in row #" + rowNum + " does not have statusName Name");
							}
						} 
						if(workFlowStatus.getWorkflowStatusName() == null || workFlowStatus.getWorkflowStatusName().trim().isEmpty()){
							continue;
						}
						String statusDisplayName = null;
						Cell cell2 = row.getCell(statusDisplayNameIndex);
						if(isCellValid(cell2)){
							
							cell2.setCellType(Cell.CELL_TYPE_STRING);
							if(cell2.getStringCellValue() != null){
								
								statusDisplayName = cell2.getStringCellValue().trim();
								workFlowStatus.setWorkflowStatusDisplayName(statusDisplayName);
							}else{
								log.info("statusDisplayName  in row #" + rowNum + " does not have statusDisplayName");
								continue;
								
							}
						}
						
						
						String description = null;
						Cell cell3 = row.getCell(descriptionIndex);
						if(isCellValid(cell3)){
							
							cell3.setCellType(Cell.CELL_TYPE_STRING);
							if(cell3.getStringCellValue() != null){
								
								description = cell3.getStringCellValue().trim();								
								workFlowStatus.setWorkflowStatusDescription(description);
							}else{
								log.info("description  in row #" + rowNum + " does not have description");
								continue;
								
							}
						}						
						
						String workflowStatusCategoryName  = null;
						Cell cell4 = row.getCell(workflowStatusCategoryIndex);
						if(isCellValid(cell4)){							
							cell4.setCellType(Cell.CELL_TYPE_STRING);
							if(cell4.getStringCellValue() != null){
								workflowStatusCategoryName =  cell4.getStringCellValue().trim();
								if(workFLowStatusCategoryMap.containsKey(workflowId) && workFLowStatusCategoryMap.get(workflowId).containsKey(workflowStatusCategoryName)){
									workFlowStatusCategory = workFLowStatusCategoryMap.get(workflowId).get(workflowStatusCategoryName);
								}else if(workFLowStatusCategoryMap.containsKey(workflowId) && !workFLowStatusCategoryMap.get(workflowId).containsKey(workflowStatusCategoryName)){
									workFlowStatusCategory = null;
								}else if(!workFLowStatusCategoryMap.containsKey(workflowId)){
									workFlowStatusCategoryList = workflowStatusCategoryService.getWorkflowStatusCategoryByWorkflowId(workflowId,1);
									HashMap<String, WorkflowStatusCategory> workflowStatusCategoryNameMap = new HashMap<String, WorkflowStatusCategory>();
									for(WorkflowStatusCategory workFlowStatusCategoryFromList : workFlowStatusCategoryList){
										workflowStatusCategoryNameMap.put(workFlowStatusCategoryFromList.getWorkflowStatusCategoryName(), workFlowStatusCategoryFromList);
										if(workFlowStatusCategoryFromList.getWorkflowStatusCategoryName().equalsIgnoreCase(workflowStatusCategoryName)){
											workFlowStatusCategory = workFlowStatusCategoryFromList;
										}
									}
									workFLowStatusCategoryMap.put(workflowId, workflowStatusCategoryNameMap);
								}
							}else{
								
								log.info("workflowStatusCategoryName  in row #" + rowNum + " does not have description");
								continue;
							}
							workFlowStatus.setWorkflowStatusCategory(workFlowStatusCategory);
						}
											
						Integer weightage  = null;
						Cell cell5 = row.getCell(WeightageIndex);					
													
							if (isCellValid(cell5)) {
								cell5.setCellType(Cell.CELL_TYPE_NUMERIC);
								if (cell5.getNumericCellValue() != 0){
									 
									weightage = (int) cell5.getNumericCellValue();
									workFlowStatusPolicy.setWeightage(weightage);
								} else{
								log.info("weightage  in row #" + rowNum + " does not have description");
								continue;
								
							}
						}	
									
						Integer slaDurationHrs  = null;
						Cell cell6 = row.getCell(slaDurationinHrsIndex);			
													
							if (isCellValid(cell6)) {
								cell6.setCellType(Cell.CELL_TYPE_NUMERIC);
								if (cell6.getNumericCellValue() != 0){									 
									slaDurationHrs =  (int) cell6.getNumericCellValue();
									workFlowStatusPolicy.setSlaDuration(slaDurationHrs);
								} else{
								log.info("slaDurationHrs  in row #" + rowNum + " does not have description");
								continue;
								
							}
						}	
							String slaViolationAction  = null;
							Cell cell7 = row.getCell(slaViolationActionIndex);
							if(isCellValid(cell7)){
								
								cell7.setCellType(Cell.CELL_TYPE_STRING);
								if(cell7.getStringCellValue() != null){
									
									slaViolationAction = cell7.getStringCellValue();
									workFlowStatusPolicy.setSlaViolationAction(slaViolationAction);
								}else{
									log.info("slaViolationAction  in row #" + rowNum + " does not have description");
									continue;
									
								}
							}
				
							Integer statusOrder = null;														
							Cell cell8 = row.getCell(statusOrderIndex);														
								if (isCellValid(cell8)) {
									cell8.setCellType(Cell.CELL_TYPE_NUMERIC);
									if (cell8.getNumericCellValue() != 0){
										statusOrder = (int) cell8.getNumericCellValue();
										workFlowStatus.setStatusOrder(statusOrder);
									} else{
									log.info("statusOrder  in row #" + rowNum + " does not have description");
									continue;
									
								}
							}
							
						 String workflowStatusType = null;							
						 Cell cell9 = row.getCell(workflowStatusTypeIndex);
							if(isCellValid(cell9)){
								
								cell9.setCellType(Cell.CELL_TYPE_STRING);
								if(cell9.getStringCellValue() != null){
									
									workflowStatusType = cell9.getStringCellValue();
									workFlowStatus.setWorkflowStatusType(workflowStatusType);
									}else{
									log.info("workflowStatusType  in row #" + rowNum + " does not have description");
									continue;
									
								}
							}
							String statusTransitionPolicy = null;
							 Cell cell10 = row.getCell(statusTransitionPolicyIndex);
								if(isCellValid(cell10)){									
									cell10.setCellType(Cell.CELL_TYPE_STRING);
									if(cell10.getStringCellValue() != null){										
										statusTransitionPolicy = cell10.getStringCellValue().trim();
										workFlowStatusPolicy.setStautsTransitionPolicy(statusTransitionPolicy);
										
									}else{
										log.info("statusTransitionPolicy  in row #" + rowNum + " does not have description");
										continue;
										
									}
								}
							String actor = null;
							Cell cell11 = row.getCell(actorIndex);
							if(isCellValid(cell11)){									
								cell11.setCellType(Cell.CELL_TYPE_STRING);
								if(cell11.getStringCellValue() != null){										
									actor = cell11.getStringCellValue().trim();
									workFlowStatusPolicy.setActionScope(actor);
									
								}else{
									log.info("actor  in row #" + rowNum + " does not have description");
									continue;
									
								}
							}	
							workFlowStatusPolicy.setActiveStatus(1);
							workFlowStatusPolicy.setStatusPolicyType("Workflow");
							statusCategory = new StatusCategory();
							statusCategory.setStatusCategoryId(1);
							workFlowStatus.setStatusCategory(statusCategory);
							workFlowStatusPolicy.setWorkflowStatus(workFlowStatus);			
							boolean isExistWorkflowStatus=workflowStatusService.isExistWorkflowStausByWorkflowIdAndWorkflowStatusName(workflowId, statusName);
							if(isExistWorkflowStatus) {
								message =  "Workflow status already exists!" ;
								 workFLowTemplatsMap.put("message", message);
								 return workFLowTemplatsMap;
								
							}					
							
							workflowStatusPolicyService.addWorkflowStatusPolicy(workFlowStatusPolicy, workflowId);
							validRecordCount ++;
									
					} 
		} 
				invalidRecordCount =  totalRecordsCount - validRecordCount;
				
			}			
			message = "<br> Number of Records Inserted are  :"+Integer.toString(validRecordCount) +"<br> Number  Records not Inserted are :"+Integer.toString(invalidRecordCount);
			workFLowTemplatsMap.put("message", message);
			
			
		}catch (IOException IOE) {
			log.error("Error reading WorkflowTemplateStatus from file", IOE);
		} catch (Exception e) {
			log.error("Error reading WorkflowTemplateStatus from file", e);
		}
		return workFLowTemplatsMap;
		
	}

	@Override
	public String importEntityWorkflowMappingStatusPolicyForUserandRole(HttpServletRequest request, String excelDataFile, InputStream is,Integer productId) {
		String addStatus = " ";
		log.info("Start Time: "+Calendar.getInstance().getTime());
		HashMap<String,Object> statusPolicyUserRole = readEntityWorkflowMappingStatusPolicyForUserandRoleFromExcelFile(request,excelDataFile, is,productId);
		addStatus =	(String) statusPolicyUserRole.get("message");
		log.info("End Time: "+Calendar.getInstance().getTime());
		return addStatus;		
		
	}
		
public HashMap<String,Object> readEntityWorkflowMappingStatusPolicyForUserandRoleFromExcelFile(HttpServletRequest request, String excelDataFile, InputStream fis,Integer productId) {
		
		int blankRowCount = 0;
		URL url = null;
		int validRecordCount = 0;
		int invalidRecordCount = 0;	
		String message = " ";
		HashMap<String,Object> statusPolicyUserRoleMap = new HashMap<String,Object>();
		log.info("Import - Excel file from location:" + excelDataFile);		
		int numberOfSheets = 0;
		try { // 1
	
			Workbook workbook = null;
			if (excelDataFile.endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			}
			if (excelDataFile.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fis);
			}
	
			// Get the worksheet containing the teststeps. It will be named 'TestSteps'.
			// If not available, get the first worksheet.
			numberOfSheets = workbook.getNumberOfSheets();
			if (numberOfSheets <= 0) {
				log.info("The Excel for EntityWorkflowMappingStatusPolicyForUserandRole import does not contain any worksheets");
			}
			Sheet sheet = null;
			for (int i = 0; i < numberOfSheets; i++) {
				if (workbook.getSheetName(i).equalsIgnoreCase("statusPolicy")) {
					sheet = workbook.getSheetAt(i);					
					break;
				}
			}
			if (sheet == null) {

			}
		
			Row row;
			int rowCount = sheet.getPhysicalNumberOfRows();
			int colCount = 0;
			int colNum = 0;
			int startIndex = 0;
			int endIndex = 0;
			int mod = 0;
			int numberOfCounters = 0;
			Integer maxRecordsToReadFromExcel = 0;	
			int totalRecordCount = sheet.getPhysicalNumberOfRows()-1;
			if (rowCount < 1) {
				log.info("No rows present in the EntityWorkflowMappingStatusPolicyForUserandRole worksheet");
			} else { // 2
				WorkflowStatusActor workflowStatusActor = new WorkflowStatusActor();
				List<EntityMaster> entityTypeMasters  =	null;
				Map<String,EntityMaster> entityTypeMap = null;
				EntityMaster entityMaster = null;
				String EntityType = null;
				HashMap<String, WorkflowMaster> workFlowMasterMap = new HashMap<String, WorkflowMaster>();
				HashMap<Integer,Map<String,WorkflowStatus>> workFlowStatusMap = new HashMap<Integer,Map<String,WorkflowStatus>>();
				HashMap<String,UserList> userMap = new HashMap<String,UserList>();

				Map<String, Integer> entityIdMap = null;
				WorkflowMaster workFlowMaster = null;
				Integer workFlowId = null;
				List<UserList> userLists = null;
				List<UserRoleMaster> userRoleMasterList = null;
				HashMap<String,UserRoleMaster> userRoleMasterMap = new HashMap<String,UserRoleMaster>();
				int rowNum;
				for (rowNum = 1; rowNum <= rowCount; rowNum++) {
					//Row row;
					blankRowCount = 0;
					row = sheet.getRow(rowNum);
					if (row == null) {
						rowNum++;
						continue;
					}else{
						workflowStatusActor = new WorkflowStatusActor();
						entityTypeMasters  =	null;
						entityTypeMap = null;
						entityMaster = null;
						EntityType = null;
						workFlowMaster = null;
						List<WorkflowMaster> workFlowMasterList = null;
						List<WorkflowStatus> workFlowStatusList = null;
						WorkflowStatus	workFlowStatus = null;
						UserList user = null;
						UserRoleMaster userRoleMaster = null;


						Cell cell0 = row.getCell(entityTypeIndex);
						if (isCellValid(cell0)) {
							cell0.setCellType(Cell.CELL_TYPE_STRING);
							if (cell0.getStringCellValue() != null) {
								EntityType = cell0.getStringCellValue().trim();
								if(entityTypeMap == null || !entityTypeMap.containsKey(EntityType)){
									if(entityTypeMap == null){
										entityTypeMap = new HashMap<String, EntityMaster>();
									}
									entityTypeMasters =	commonDao.getWorkflowCapableEntityTypeList();
									if(entityTypeMasters != null && entityTypeMasters.size() > 0){										
										for(EntityMaster entityMasterFromList : entityTypeMasters){
											entityTypeMap.put(entityMasterFromList.getEntitymastername(), entityMasterFromList);									
										}
									}
								}
								if(entityTypeMap.containsKey(EntityType)){
									entityMaster = entityTypeMap.get(EntityType);
								}
							} else {
								log.info("EntityType  in row #" + rowNum+ " does not have EntityType");
							}
						} 
						if(entityMaster == null){
							continue;
						}else{
							workflowStatusActor.setEntityType(entityMaster);
						}
						String Entity = null;
						Integer entityId = null;
						Cell cell1 = row.getCell(entityIndex);
						if (isCellValid(cell1)) {
							cell1.setCellType(Cell.CELL_TYPE_STRING);
							if (cell1.getStringCellValue() != null){
								Entity = cell1.getStringCellValue().trim();	
								Integer entityTypeId = entityMaster.getEntitymasterid();
								if(entityIdMap == null || !entityIdMap.containsKey(entityTypeId+"-"+Entity)){
									if(entityIdMap == null){
										entityIdMap = new HashMap<String, Integer>();
									}
									List<Object[]> entities = null; 
									HashMap<String, Object> contraints = new HashMap<String, Object>();
									List<String> projectionKeyList = new ArrayList<String>();
									if(IDPAConstants.ENTITY_TASK_TYPE == entityTypeId){
										projectionKeyList.add("activityTaskType.activityTaskTypeId");
										projectionKeyList.add("activityTaskType.activityTaskTypeName");
										contraints.put("activityTaskType.product.productId", productId);
										entities = commonDao.getEntityForTypeList(ActivityTaskType.class.getName(), "activityTaskType", contraints, projectionKeyList);
									}else if(IDPAConstants.ENTITY_ACTIVITY_TYPE == entityTypeId){
										projectionKeyList.add("activityMaster.activityMasterId");
										projectionKeyList.add("activityMaster.activityMasterName");
										contraints.put("activityMaster.productMaster.productId", productId);
										entities = commonDao.getEntityForTypeList(ActivityMaster.class.getName(), "activityMaster", contraints, projectionKeyList);
									}

									if(entities != null && entities.size() > 0){
										for(Object[] entity : entities){
											entityIdMap.put(entityTypeId+"-"+(String)entity[1], (Integer)entity[0]);
										}
									}
								}
								if(entityIdMap.containsKey(entityTypeId+"-"+Entity)){
									entityId = entityIdMap.get(entityTypeId+"-"+Entity);
								}

							} else {
								log.info("Entity  in row #" + rowNum + " does not have Entity Name");
							}
						} 

						String workFlowTemplateName = null;	
						Cell cell2 = row.getCell(workFlowTemplateIndex);
						if (isCellValid(cell2)) {
							cell2.setCellType(Cell.CELL_TYPE_STRING);
							if (cell2.getStringCellValue() != null) {
								workFlowTemplateName = cell2.getStringCellValue().trim();													
								if(workFlowMasterMap.containsKey(workFlowTemplateName)){
									workFlowMaster = workFlowMasterMap.get(workFlowTemplateName);
								}else{
									workFlowMasterList =	workFlowMasterService.getWorkFlowMasterListByWorkflowName(workFlowTemplateName);
									if(workFlowMasterList != null && workFlowMasterList.size() > 0){
										workFlowMaster = workFlowMasterList.get(0);
										workFlowMasterMap.put(workFlowTemplateName, workFlowMaster);
									}
								}
							}	
							else {
								log.info("workFlowTemplateName  in row #" + rowNum+ " does not have workFlowTemplateName Name");
							}
						} 
						if(workFlowMaster == null){
							continue;
						}else{
							workFlowId = workFlowMaster.getWorkflowId();
						}
												
						String statusName = null;
						Cell cell3 = row.getCell(entityStatusNameIndex);
						if (isCellValid(cell3)) {
							cell3.setCellType(Cell.CELL_TYPE_STRING);
							if (cell3.getStringCellValue() != null){
								statusName = cell3.getStringCellValue().trim();								
								if(workFlowStatusMap.containsKey(workFlowId) && workFlowStatusMap.get(workFlowId).containsKey(statusName)){
									workFlowStatus = workFlowStatusMap.get(workFlowId).get(statusName);
								}else if((workFlowStatusMap.containsKey(workFlowId) && !workFlowStatusMap.get(workFlowId).containsKey(statusName)) ){
									workFlowStatus = null;
									
								}else if(!workFlowStatusMap.containsKey(workFlowId)){
										workFlowStatusList = workflowStatusPolicyService.getWorkFlowStatus(workFlowId);
										HashMap<String,WorkflowStatus> workFlowStatusNameMap = new 	HashMap<String,WorkflowStatus>();
										
										for(WorkflowStatus workFlowStatusFromList : workFlowStatusList){
											workFlowStatusNameMap.put(workFlowStatusFromList.getWorkflowStatusName(), workFlowStatusFromList);
											if(workFlowStatusFromList.getWorkflowStatusName().equalsIgnoreCase(statusName)){
												
												workFlowStatus = workFlowStatusFromList;
											}									

										}										
										workFlowStatusMap.put(workFlowId, workFlowStatusNameMap);
								}
									
								}
							}    
						
						else {
								log.info("statusName  in row #" + rowNum + " does not have statusName Name");
							}
						 
						if(workFlowStatus == null){
							continue;
						}

						String userFromExcel = null;
						Cell cell4 = row.getCell(UserIndex);
						if (isCellValid(cell4)) {
							cell4.setCellType(Cell.CELL_TYPE_STRING);
							if (cell4.getStringCellValue() != null){
								userFromExcel = cell4.getStringCellValue().trim();
								if(!userMap.containsKey(userFromExcel)){
									userLists = userListService.getActivityUserListBasedRoleIdAndProductId(-10, productId);
									if(userLists != null && userLists.size() > 0){
										for(UserList userList : userLists){
											userMap.put(userList.getLoginId(), userList);
											}
									}
									UserList user1 = userListService.getUserListById(-1);
									UserList user2 = userListService.getUserListById(-2);
									userMap.put(user1.getLoginId(), user1);
									userMap.put(user2.getLoginId(), user2);
								}
								if(userMap.containsKey(userFromExcel)){
									user = userMap.get(userFromExcel);
								}
							}						
						} else {
							log.info("user  in row #" + rowNum+ " does not have user Name");
						}

						String roles = null;
						Cell cell5 = row.getCell(RolesIndex);
						if (isCellValid(cell5)) {
							cell5.setCellType(Cell.CELL_TYPE_STRING);
							if (cell5.getStringCellValue() != null) {
								roles = cell5.getStringCellValue().trim();								

								if(userRoleMasterMap.containsKey(roles)){
									userRoleMaster = userRoleMasterMap.get(roles);
								}else{

									userRoleMasterList = productListService.getProductUserRoles(1);
									if(userRoleMasterList !=null && userRoleMasterList.size() > 0){
										for(UserRoleMaster userRoleMasterFromList : userRoleMasterList ){											
											userRoleMasterMap.put(userRoleMasterFromList.getRoleName(), userRoleMasterFromList);
										}
									}
								}	
								if(userRoleMasterMap.containsKey(roles)){
									userRoleMaster = userRoleMasterMap.get(roles);
								}
							} else {
								log.info("roles  in row #" + rowNum+ " does not have roles Name");
							}
						} 		

						String actorMappingType = null;
						Cell cell6 = row.getCell(actorMappingTypeIndex);
						if (isCellValid(cell6)) {
							cell6.setCellType(Cell.CELL_TYPE_STRING);
							if (cell6.getStringCellValue() != null) {
								actorMappingType = cell6.getStringCellValue().trim();	
								
								workflowStatusActor.setActorMappingType(actorMappingType);
								
							} else {
								log.info("actorMappingType  in row #" + rowNum+ " does not have actorMappingType Name");
							}
						} 		
						
						String actionRequirement = null;
						Cell cell7 = row.getCell(actionRequirementIndex);
						if (isCellValid(cell7)) {
							cell7.setCellType(Cell.CELL_TYPE_STRING);
							if (cell7.getStringCellValue() != null) {
								actionRequirement = cell7.getStringCellValue().trim();	
								
								workflowStatusActor.setActionRequirement(actionRequirement);
								
							} else {
								log.info("actorMappingLevel  in row #" + rowNum+ " does not have actorMappingType Name");
							}
						} 
						if(workflowStatusActor.getActionRequirement() == null || workflowStatusActor.getActionRequirement().trim().isEmpty()){
							workflowStatusActor.setActionRequirement("Optional");
						}
						ProductMaster product = productMasterDao.getByProductId( productId);						
						workflowStatusActor.setProduct(product);
						workflowStatusActor.setEntityType(entityMaster);
						workflowStatusActor.setEntityId(entityId);
						workflowStatusActor.setWorkflowStatus(workFlowStatus);
						workflowStatusActor.setUser(user);
						workflowStatusActor.setRole(userRoleMaster);
						workflowStatusActor.setUserActionStatus("Not Complete");
						workflowStatusActor.setMappingLevel("Entity");
						
						boolean isMappingAlreadyExist = false;
						if(workflowStatusActor.getUser() == null){
							 isMappingAlreadyExist = workflowStatusPoliciesService.checkWorkflowStatusActorAlreadyExist(productId, workflowStatusActor.getWorkflowStatusActorId(), workflowStatusActor.getWorkflowStatus().getWorkflowStatusId(), workflowStatusActor.getEntityType().getEntitymasterid(), workflowStatusActor.getEntityId(), workflowStatusActor.getEntityInstanceId(), null, workflowStatusActor.getRole().getUserRoleId());
						}
						else if(workflowStatusActor.getRole() == null){
							 isMappingAlreadyExist = workflowStatusPoliciesService.checkWorkflowStatusActorAlreadyExist(productId, workflowStatusActor.getWorkflowStatusActorId(), workflowStatusActor.getWorkflowStatus().getWorkflowStatusId(), workflowStatusActor.getEntityType().getEntitymasterid(), workflowStatusActor.getEntityId(), workflowStatusActor.getEntityInstanceId(), workflowStatusActor.getUser().getUserId(), null);
						}else{
							
							 isMappingAlreadyExist = workflowStatusPoliciesService.checkWorkflowStatusActorAlreadyExist(productId, workflowStatusActor.getWorkflowStatusActorId(), workflowStatusActor.getWorkflowStatus().getWorkflowStatusId(), workflowStatusActor.getEntityType().getEntitymasterid(), workflowStatusActor.getEntityId(), workflowStatusActor.getEntityInstanceId(), workflowStatusActor.getUser().getUserId(), workflowStatusActor.getRole().getUserRoleId());
						}				
						
						if(isMappingAlreadyExist){							
							message = "Actor already associated with the policy";
							statusPolicyUserRoleMap.put("message", message);
							return statusPolicyUserRoleMap;							
						}
						
						
						workflowStatusPolicyService.addWorkflowStatusActor(workflowStatusActor);
						validRecordCount++;
					} 
				} 
				
				invalidRecordCount =	totalRecordCount - validRecordCount;
				message = "<br> Number of Records Inserted are  :"+Integer.toString(validRecordCount) +"<br>Number  Records not Inserted are :"+Integer.toString(invalidRecordCount);
				statusPolicyUserRoleMap.put("message", message);				
				
			}		
		}catch (IOException IOE) {
			log.error("Error reading EntityWorkflowMappingStatusPolicyForUserandRole from file", IOE);
		} catch (Exception e) {
			log.error("Error reading EntityWorkflowMappingStatusPolicyForUserandRole from file", e);
		}
		return statusPolicyUserRoleMap;
		}

@Override
public String importSkills(HttpServletRequest request, String excelDataFile,
		InputStream is) {
	
	String addStatus =" ";	
	log.info("Start Time: "+Calendar.getInstance().getTime());
	HashMap<String,Object> skillUploadMap = readSkillsExcel(request,excelDataFile, is);
	addStatus = (String) skillUploadMap.get("message");
	String filePathAndName = (String)skillUploadMap.get("filePathAndName");
	if(null != filePathAndName && "" != filePathAndName){
		addStatus += ";"+filePathAndName;
	}
	List<UserSkills> userSkillList = (List<UserSkills>) skillUploadMap.get("skillupload");	
	log.info("End Time: "+Calendar.getInstance().getTime());
	return addStatus;	
	
}

private HashMap<String,Object> readSkillsExcel(HttpServletRequest request,
	String excelDataFile, InputStream fis) {
	int validRecordCount = 0;
	int invalidRecordCount = 0;		
	String message = " ";
	HashMap<String,Object> skillUploadMap = new HashMap<String,Object>();
	int blankRowCount = 0;
	URL url = null;		
	log.info("Import - Excel file from location:" + excelDataFile);		
	
	int isValidRowNum = 0;
	String rowStatus = "";
	String statusRemarks = "";
	String currentDate = DateUtility.dateToStringInSecond(new Date());
	currentDate = currentDate.replaceAll(":", "_");
	String skillProfileUploadReportFile = "SkillProfileUploadReport_"+currentDate+".xlsx";
	
	//Store invalid row data into a map for data parsing
	Map<Integer,String> invalidRowMap = new HashMap<Integer,String>();
	
	int numberOfSheets = 0;
	try { // 1

		Workbook workbook = null;
		if (excelDataFile.endsWith(".xls")) {
			workbook = new HSSFWorkbook(fis);
		}
		if (excelDataFile.endsWith(".xlsx")) {
			workbook = new XSSFWorkbook(fis);
		}

		// Get the worksheet containing the teststeps. It will be named 'TestSteps'.
		// If not available, get the first worksheet.
		numberOfSheets = workbook.getNumberOfSheets();
		if (numberOfSheets <= 0) {
			log.info("The Excel for Skill import does not contain any worksheets");
		}
		Sheet sheet = null;
		List<UserSkills> userSkillList= new ArrayList<UserSkills>();
		for (int i = 0; i < numberOfSheets; i++) {
			if (workbook.getSheetName(i).equalsIgnoreCase("SKILLPROFILE")) {
				sheet = workbook.getSheetAt(i);					
				break;
			}
		}
		if (sheet == null) {

		}	
		Row row;
		int rowCount = sheet.getPhysicalNumberOfRows();
		int colCount = 0;
		int colNum = 0;
		int startIndex = 0;
		int endIndex = 0;
		int mod = 0;
		int numberOfCounters = 0;
		Integer maxRecordsToReadFromExcel = 0;
		Map<String,Skill> skillMap=new HashMap<String,Skill>();
		boolean existingSkillDetail=false;
		Map<Integer,SkillLevels> skillLevelMap = new HashMap<Integer,SkillLevels>();
		int totalRecords = sheet.getPhysicalNumberOfRows()-1;
		if (rowCount < 1) {
			log.info("No rows present in the Skill Upload worksheet");
		} else { // 2
			
			//Header row - Row Number is 1
			Row headerRow = sheet.getRow(0);
	        //Applying CellStyle to the Header Row
	        CellStyle css = workbook.createCellStyle();
	        Font arialBoldFont = workbook.createFont();
	        
	        arialBoldFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	        arialBoldFont.setFontName("Arial");
	        arialBoldFont.setFontHeightInPoints((short) 10);	        
	        
	        css.setAlignment(CellStyle.ALIGN_CENTER);
			css.setFont(arialBoldFont);
			int totalHeaderCells = headerRow.getPhysicalNumberOfCells()-1;				
			for(int i=totalHeaderCells;i<totalHeaderCells+2;i++){
    			Cell cell = headerRow.createCell(i+1);
    			cell.setCellStyle(css);
    			if(i==totalHeaderCells){    				
    				cell.setCellValue("Status");    				
    			} else if(i==totalHeaderCells+1){   				
    				cell.setCellValue("Remarks");    				
    			}
    		}
			
			HashMap<String,UserList> userMap = new HashMap<String,UserList>();			
			UserList userLists = null;
			Skill skill = null;
			SkillLevels skillLevles = null;
			HashMap<String,ArrayList<String>> skillNameMap = new HashMap<String,ArrayList<String>>();
			
			int rowNum;
			for (rowNum = 1; rowNum <= rowCount; rowNum++) {
				//Row row;
				blankRowCount = 0;
				row = sheet.getRow(rowNum);
				if (row == null) {
					rowNum++;
					continue;
				}else{			
					UserSkills userSkill = new UserSkills();
					String userName= null;
					Cell cell0 = row.getCell(skillUploadUserNameIndex);
					if (isCellValid(cell0)) {
						cell0.setCellType(Cell.CELL_TYPE_STRING);
						if (cell0.getStringCellValue() != null) {
							userName = cell0.getStringCellValue().trim();	
							userSkill.setUserName(userName);
						} else {
							log.info("userSkill  in row #" + rowNum+ " does not have userSkill");
						}
					} 
					
					String userCode = null;					
					Cell cell1 = row.getCell(skillUploadUserCodeIndex);
					if (isCellValid(cell1)) {
						cell1.setCellType(Cell.CELL_TYPE_STRING);
						if (cell1.getStringCellValue() != null){	
							userCode = cell1.getStringCellValue().trim();
							userSkill.setUserCode(userCode);					
						} else {
							log.info("Usercode  in row #" + rowNum + " does not have UserCode Name");
						}
					} 
	
				
					String loginId = null;
					Cell cell2 = row.getCell(skillUploadLoginIDIndex);
					if (isCellValid(cell2)) {
						cell2.setCellType(Cell.CELL_TYPE_STRING);
						if (cell2.getStringCellValue() != null){
							loginId = cell2.getStringCellValue().trim();							
								userLists = userListService.getUserByLoginId(loginId);						
							}							
						}						
					 else {
						log.info("loginId  in row #" + rowNum+ " does not have loginId");
					}		
					if(userLists == null){
						log.info("loginId  in row #" + rowNum+ "is not valid");
						isValidRowNum = rowNum;
						rowStatus = "Not Updated";
						statusRemarks = "In Row: "+rowNum+" LoginId is empty or Invalid";
						invalidRowMap.put(rowNum, rowStatus+";"+statusRemarks);
						continue;
					} 
					userSkill.setUser(userLists);	
					int userID = userLists.getUserId();			
					
					String skillName = null;
					Cell cell3 = row.getCell(skillUploadNameIndex);
					if (isCellValid(cell3)) {
						cell3.setCellType(Cell.CELL_TYPE_STRING);	
						if (cell3.getStringCellValue() != null){
							skillName = cell3.getStringCellValue().trim();
						} else {
							log.info("skillName  in row #" + rowNum+ " does not have Proper format");
						}
						existingSkillDetail = skillService.isUserSkillExistingBySkillName(skillName, userID);
						if(existingSkillDetail) {
							log.info("skillName  already exists for this user");
							isValidRowNum = rowNum;
							rowStatus = "Not Updated";
							statusRemarks = "In Row: "+rowNum+" SkillName is Invalid";
							invalidRowMap.put(rowNum, rowStatus+";"+statusRemarks);
							continue;
						}else {	
							if(!skillMap.containsKey(skillName)){
								skill = skillService.getSkillByName(skillName);
								if(skill != null){
									skillMap.put(skillName, skill);
								}
							}else{
								skill = skillMap.get(skillName);
							}
							if(skill == null){
								log.info("skillName  not available in database");
								isValidRowNum = rowNum;
								rowStatus = "Not Updated";
								statusRemarks = "In Row: "+rowNum+" SkillName is Invalid";
								invalidRowMap.put(rowNum, rowStatus+";"+statusRemarks);
								continue;
							}
							
							if(((skillNameMap.containsKey(userLists.getLoginId()) && !skillNameMap.get(userLists.getLoginId()).contains(skillName)) || !skillNameMap.containsKey(userLists.getLoginId()))){
								if(!skillNameMap.containsKey(userLists.getLoginId())){
									ArrayList<String> userSkills = new ArrayList<String>();
									userSkills.add(skillName);
									skillNameMap.put(userLists.getLoginId(), userSkills);
								}else{
									skillNameMap.get(userLists.getLoginId()).add(skillName);
								}
							}else{
								log.info("Duplicate record in excel for skill - "+skillName+", User - "+loginId);
								isValidRowNum = rowNum;
								rowStatus = "Not Updated";
								statusRemarks = "In Row: "+rowNum+" Duplicate record in excel for skill - "+skillName+", User - "+loginId;
								invalidRowMap.put(rowNum, rowStatus+";"+statusRemarks);
								continue;
							}
						}
					}else {
						log.info("skillName  in row #" + rowNum+ " does not have skillName");
					} 
					 
					if(skill == null){
						log.info("Skill is empty");
						isValidRowNum = rowNum;
						rowStatus = "Not Updated";
						statusRemarks = "In Row: "+rowNum+" Skill is empty";
						invalidRowMap.put(rowNum, rowStatus+";"+statusRemarks);
						continue;
					}
					userSkill.setSkill(skill);					
					
					
					Integer skillLevelID = null;
					Cell cell4 = row.getCell(skillUploadLevelIndex);
					if (isCellValid(cell4)) {
						cell4.setCellType(Cell.CELL_TYPE_NUMERIC);
						if (cell4.getNumericCellValue() != 0){							
							skillLevelID = (int) cell4.getNumericCellValue();
							
							if(skillLevelMap.containsKey(skillLevelID)){
								skillLevles =	skillLevelMap.get(skillLevelID);
							}else{
								skillLevles =	skillService.getByskillLevelId(skillLevelID);
								if(skillLevles != null){
								skillLevelMap.put(skillLevelID, skillLevles);	
								}
							}
							
						}						
					} else {
						log.info("skillLevelName  in row #" + rowNum+ " does not have skillLevelName");
					}
					
					userSkill.setSelfSkillLevel(skillLevles);

					String isPrimarySkill = null;
					Cell cell5 = row.getCell(skillUploadIsPrimarySkillIndex);
					Integer selfIsPrimary=0;
					if (isCellValid(cell5)) {
						cell5.setCellType(Cell.CELL_TYPE_STRING);
						isPrimarySkill=cell5.getStringCellValue().trim();
						if(isPrimarySkill.equalsIgnoreCase("Yes") || isPrimarySkill.equalsIgnoreCase("Y")){
							selfIsPrimary=1;
						} else {
							selfIsPrimary=0;
						}
						
					} else {
						log.info("isPrimarySkill  in row #" + rowNum+ " does not have isPrimarySkill");
					}		
					userSkill.setSelfIsPrimary(selfIsPrimary);

					String approver = null;
					Cell cell6 = row.getCell(skillUploadApproverIndex);
					UserList approvingManager=null;
					if (isCellValid(cell6)) {
						cell6.setCellType(Cell.CELL_TYPE_STRING);
						if (cell2.getStringCellValue() != null){
							approver = cell2.getStringCellValue().trim();							
							approvingManager = userListService.getUserByLoginId(approver);						
						}else {
							log.info("approver  in row #" + rowNum+ " does not have approver");
						}
					} 		
					userSkill.setApprovingManager(approvingManager);
					
					Date fromDate=null;
					Cell cell7 = row.getCell(skillUploadFromDateIndex);
					if (isCellValid(cell7)) {
						fromDate = cell7.getDateCellValue();
						userSkill.setFromDate(fromDate);
					}else {
						log.info("fromDate  in row #" + rowNum+ " does not have fromDate");
					}
					
					Date toDate=null;
					Cell cell8 = row.getCell(skillUploadToDateIndex);
					if (isCellValid(cell8)) {
						toDate = cell8.getDateCellValue();
						userSkill.setToDate(toDate);
					}else {
						log.info("toDate  in row #" + rowNum+ " does not have toDate");
					}
					
					userSkill.setStatus(1);				
					userSkillList.add(userSkill);
					validRecordCount++;
				}
				if(rowNum >1){
					isValidRowNum = rowNum;
					rowStatus = "Updated";
					statusRemarks = "Row uploaded successfully";
					invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);
				}
			}
			
			for(Entry<Integer, String> parentMap : invalidRowMap.entrySet()){
	        	int invalidRowNumber = parentMap.getKey();
	        	String values = parentMap.getValue();	        	
        		String status = values.split(";")[0];
        		String remarks = values.split(";")[1];	        		
        		for(int i=totalHeaderCells;i<totalHeaderCells+2;i++){
        			row = sheet.getRow(invalidRowNumber);
        			Cell cell = row.createCell(i+1);
        			if(i==totalHeaderCells)
        				cell.setCellValue(status);
        			else if(i==totalHeaderCells+1)
        				cell.setCellValue(remarks);	        			
        		}        		        	
	        }
			
			try{
				File filePath = new File(reportFilePath);
				if(filePath == null || !filePath.exists() || !filePath.isDirectory()){
					filePath.mkdirs();
				}
				filePath = new File(reportFilePath+skillProfileUploadReportFile);
				FileOutputStream fos = new FileOutputStream(filePath);
				workbook.write(fos);
			}catch(Exception ex){
				log.error("Exception occured in writeUploadReportFile - ", ex);
			}
			
			userSkillsDAO.userSkillBulk(userSkillList,Integer.parseInt(userSkillMaxBatchCount));
			
			
		}	
		invalidRecordCount = totalRecords-validRecordCount;
		
		message = "<br> Number of Records Inserted are  :"+Integer.toString(validRecordCount) +"<br> Number  Records not Inserted are :"+Integer.toString(invalidRecordCount);
		skillUploadMap.put("message", message);
		skillUploadMap.put("skillupload",userSkillList);
		skillUploadMap.put("filePathAndName", reportFilePath+skillProfileUploadReportFile);
		
	}catch (IOException IOE) {
		log.error("Error reading readSkillsExcel from file", IOE);
		// return false;
	} catch (Exception e) {
		log.error("Error reading readSkillsExcel from file", e);
	//	return false;
	}
	return skillUploadMap;
}

public boolean uiObjectsExport(List<UIObjectItems> uiObjectsItemsList,String exportLocation,String fileName) {			
	try {				
		String fileExportlocation = exportLocation;						

		boolean isDirCreated = checkAndCreateTempDirectory(fileExportlocation);
		log.info("File directories created: " + isDirCreated);
		FileOutputStream fos = new FileOutputStream(fileExportlocation+File.separator+fileName);

		log.info("Export UI Objects: Generate Excel data");
		int rowCount = 0;
		// From testCaseList number of test cases is obtained.
		XSSFWorkbook workbook = new XSSFWorkbook();
		if (uiObjectsItemsList != null && !uiObjectsItemsList.isEmpty()) {
			rowCount = uiObjectsItemsList.size();
		}

		Sheet uiObjectItemListSheet = workbook.createSheet("UIOBJECTS");
		CellStyle s = null;
		s = uiObjectItemListSheet.getWorkbook().createCellStyle();       

		// TestCase and TestSteps titles processing
		int rowNum = 0;
		int colNum = 0;				

		// Title Row for TestCase
		String[] uiObjectHeader = { "Web","SeeTest", "Appium","CodedUI","TestComplete"};
		String[] uiObjectSecondRowHeader = {"Xpath","CSS Locator" };
		String[] uiObjectThirdRowHeader = { "Element Name", "Description","ID","Label","Chrome","Firefox","IE", "Safari","FirefoxGecko","Edge","Chrome","Firefox","IE", "Safari","FirefoxGecko","Edge","Label","Zone","Index","Label","Page Name","Element Type","Group Name","Page URL","Application Type","Label","ObjectId"};
		int uiObjectItemsColCount = uiObjectThirdRowHeader.length;

		log.info("UIObject Row Count : " + rowCount);
		log.info("uiObjectItemsColCount Column Count : "+ uiObjectItemsColCount);

		// Creating title rows
		Row uiObjectsTitlerow = uiObjectItemListSheet
				.createRow(rowNum++);
		s.setAlignment(CellStyle.ALIGN_CENTER);
		Cell[] uiObjectsTitleArray = new Cell[uiObjectHeader.length];


		uiObjectItemListSheet.addMergedRegion(new CellRangeAddress(
				0, //first row (0-based)
				0, //last row  (0-based)
				3, //first column (0-based)
				15  //last column  (0-based)
				));
		uiObjectItemListSheet.addMergedRegion(new CellRangeAddress(
				0, //first row (0-based)
				0, //last row  (0-based)
				16, //first column (0-based)
				18  //last column  (0-based)
				));

		uiObjectItemListSheet.addMergedRegion(new CellRangeAddress(
				0, //first row (0-based)
				0, //last row  (0-based)
				19, //first column (0-based)
				19  //last column  (0-based)
				));
		uiObjectItemListSheet.addMergedRegion(new CellRangeAddress(
				0, //first row (0-based)
				0, //last row  (0-based)
				25, //first column (0-based)
				25  //last column  (0-based)
				));
		uiObjectItemListSheet.addMergedRegion(new CellRangeAddress(
				0, //first row (0-based)
				0, //last row  (0-based)
				26, //first column (0-based)
				26  //last column  (0-based)
				));
		uiObjectItemListSheet.addMergedRegion(new CellRangeAddress(
				1, //first row (0-based)
				1, //last row  (0-based)
				4, //first column (0-based)
				9  //last column  (0-based)
				));


		uiObjectItemListSheet.addMergedRegion(new CellRangeAddress(
				1, //first row (0-based)
				1, //last row  (0-based)
				10, //first column (0-based)
				15  //last column  (0-based)
				));



		uiObjectsTitleArray[0] = uiObjectsTitlerow.createCell(3);
		uiObjectsTitleArray[0].setCellStyle(s);
		uiObjectsTitleArray[0].setCellValue(uiObjectHeader[0]);

		uiObjectsTitleArray[1] = uiObjectsTitlerow.createCell(16);
		uiObjectsTitleArray[1].setCellStyle(s);
		uiObjectsTitleArray[1].setCellValue(uiObjectHeader[1]);

		uiObjectsTitleArray[2] = uiObjectsTitlerow.createCell(19);
		uiObjectsTitleArray[2].setCellStyle(s);
		uiObjectsTitleArray[2].setCellValue(uiObjectHeader[2]);				

		uiObjectsTitleArray[2] = uiObjectsTitlerow.createCell(25);
		uiObjectsTitleArray[2].setCellStyle(s);
		uiObjectsTitleArray[2].setCellValue(uiObjectHeader[3]);
		
		uiObjectsTitleArray[3] = uiObjectsTitlerow.createCell(26);
		uiObjectsTitleArray[3].setCellStyle(s);
		uiObjectsTitleArray[3].setCellValue(uiObjectHeader[4]);
		
		uiObjectsTitlerow = uiObjectItemListSheet.createRow(rowNum++);

		Cell[] uiObjectsSecondRowTitleArray = new Cell[uiObjectSecondRowHeader.length];

		uiObjectsSecondRowTitleArray[0] = uiObjectsTitlerow.createCell(4);
		uiObjectsSecondRowTitleArray[0].setCellStyle(s);
		uiObjectsSecondRowTitleArray[0].setCellValue(uiObjectSecondRowHeader[0]);

		uiObjectsSecondRowTitleArray[1] = uiObjectsTitlerow.createCell(10);
		uiObjectsSecondRowTitleArray[1].setCellStyle(s);
		uiObjectsSecondRowTitleArray[1].setCellValue(uiObjectSecondRowHeader[1]);

		uiObjectsTitlerow = uiObjectItemListSheet.createRow(rowNum++);

		Cell[] uiObjectsThirdTitleArray = new Cell[uiObjectItemsColCount];
		for (int counter = 0; counter < uiObjectItemsColCount; counter++) {
			uiObjectsThirdTitleArray[counter] = uiObjectsTitlerow.createCell(counter);
			uiObjectsThirdTitleArray[counter].setCellStyle(s);
			uiObjectsThirdTitleArray[counter].setCellValue(uiObjectThirdRowHeader[counter]);
		}
		if (uiObjectsItemsList != null
				&& !uiObjectsItemsList.isEmpty()) {
			// TestCase and TestSteps Processing
			for (UIObjectItems uiObj : uiObjectsItemsList) {

				//	rowNum++;
				colNum = 0;
				int celNum=0;
				// Creating Object array for holding test case data
				log.info("String array for holding uiobject data");

				Row uiObjectsrow = uiObjectItemListSheet.createRow(rowNum++);

				String[] uiObjValueArray = new String[uiObjectItemsColCount];
				int[] uiObjeColTypeArray = new int[uiObjectItemsColCount];										

				uiObjValueArray[colNum] = uiObj.getElementName();
				uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;					

				uiObjValueArray[colNum] = uiObj.getDescription();
				uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

				uiObjValueArray[colNum] = uiObj.getIdType();
				uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

				uiObjValueArray[colNum] = uiObj.getWebLabel();
				uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

				if(null != uiObj && null != uiObj.getIdType() && uiObj.getIdType().equalsIgnoreCase("XPATH")){
					uiObjValueArray[colNum] = uiObj.getChrome();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = uiObj.getFirefox();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = uiObj.getIe();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = uiObj.getSafari();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = uiObj.getFirefoxgecko();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = uiObj.getEdge();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}else{
					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}

				if(null != uiObj.getIdType() && (uiObj.getIdType().equalsIgnoreCase("CSSSelector") || uiObj.getIdType().equalsIgnoreCase("CSS"))){
					uiObjValueArray[colNum] = uiObj.getChrome();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = uiObj.getFirefox();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = uiObj.getIe();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = uiObj.getSafari();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
					
					uiObjValueArray[colNum] = uiObj.getFirefoxgecko();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = uiObj.getEdge();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}else{
					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
					
					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}
				if(null != uiObj && uiObj.getSeetestLabel() != null){
					uiObjValueArray[colNum] = uiObj.getSeetestLabel().toString();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}else{
					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}
				
				if(null != uiObj && uiObj.getSeeTestZoneIndex() != null){
					uiObjValueArray[colNum] = uiObj.getSeeTestZoneIndex();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}else{
					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}

				if(null != uiObj && uiObj.getSeeTestIndexIndex() != null){
					uiObjValueArray[colNum] = uiObj.getSeeTestIndexIndex().toString();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_NUMERIC;
				}else{
					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}
				if(null != uiObj && uiObj.getAppiumLabel() != null){
					uiObjValueArray[colNum] = uiObj.getAppiumLabel();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}else{
					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}

				uiObjValueArray[colNum] = uiObj.getPageName();
				uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;


				uiObjValueArray[colNum] =  uiObj.getElementType();
				uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

				uiObjValueArray[colNum] = uiObj.getGroupName();
				uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

				uiObjValueArray[colNum] = uiObj.getPageURL();
				uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

				uiObjValueArray[colNum] = uiObj.getTestEngineName();
				uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				
				if(null != uiObj && uiObj.getCodeduiLabel() != null){
					uiObjValueArray[colNum] = uiObj.getCodeduiLabel();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}else{
					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}
				
				if(null != uiObj && uiObj.getTestCompleteLabel() != null){
					uiObjValueArray[colNum] = uiObj.getTestCompleteLabel();
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}else{
					uiObjValueArray[colNum] = "";
					uiObjeColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}
				
				Cell[] uiObjectCells = new Cell[uiObjectItemsColCount];
				for (int colCounter = 0; colCounter < uiObjectItemsColCount; colCounter++) {
					uiObjectCells[colCounter] = uiObjectsrow
							.createCell(celNum++);
					uiObjectCells[colCounter]
							.setCellType(uiObjeColTypeArray[colCounter]);

					if (uiObjeColTypeArray[colCounter] == Cell.CELL_TYPE_NUMERIC) {
						uiObjectCells[colCounter].setCellValue(Integer
								.parseInt(uiObjValueArray[colCounter]));
					}

					else if (uiObjeColTypeArray[colCounter] == Cell.CELL_TYPE_STRING) {
						uiObjectCells[colCounter]
								.setCellValue((uiObjValueArray[colCounter]));
					} else {
						uiObjectCells[colCounter]
								.setCellValue((uiObjValueArray[colCounter]));
					}
				}
			}
		}
		workbook.write(fos);
		fos.close();
	} catch (FileNotFoundException e) {
		log.error("error: "+e);
		return false;
	} catch (IOException e) {
		log.error("error: "+e);
		return false;
	}
	return true;
}

public boolean testDataExport(List<TestDataItems> testDataItemsList,String exportLocation, String fileName, Integer testDataItemMaxValuesCount) {
	try {
		Set<Integer> testdataItemsUniqueSet = new HashSet<Integer>();
		List<Integer> testDataPlanId =new ArrayList<Integer>();
		String fileExportlocation = exportLocation;						

		boolean isDirCreated = checkAndCreateTempDirectory(fileExportlocation);
		log.info("File directories created: " + isDirCreated);

		FileOutputStream fos = new FileOutputStream(fileExportlocation+File.separator+fileName);

		log.info("Export Test Data: Generate Excel data");
		int rowCount = 0;
		// From testCaseList number of test cases is obtained.
		XSSFWorkbook workbook = new XSSFWorkbook();
		if (testDataItemsList != null && !testDataItemsList.isEmpty()) {
			rowCount = testDataItemsList.size();
		}

		Sheet testDataSheet = workbook.createSheet("Test-Data");
		CellStyle s = null;

		s = testDataSheet.getWorkbook().createCellStyle();


		// TestCase and TestSteps titles processing
		int rowNum = 0;
		int colNum = 0;


		// Title Row for TestCase
		String[] testDataHeader = new String[testDataItemMaxValuesCount+4];
		int index = 0;
		testDataHeader[index++] = "Data";
		testDataHeader[index++] = "Type";
		//testDataHeader[index++] = "Handle";
		testDataHeader[index++] = "Remarks";
		testDataHeader[index++] = "Test Data Plan";

		for(int i=1 ;i<testDataItemMaxValuesCount+1;i++){

			testDataHeader[index++] = "Value"+i;

		}

		int testDataHeaderColCount = testDataHeader.length;

		log.info("Test Data Item's Row Count : " + rowCount);
		log.info("testDataHeaderColCount Column Count : "+ testDataHeaderColCount);

		// Creating title rows
		Row testDataTitlerow = testDataSheet.createRow(rowNum++);
		// Setting title row values for TestCase

		Cell[] testDataTitleArray = new Cell[testDataHeaderColCount];
		for (int counter = 0; counter < testDataHeaderColCount; counter++) {
			testDataTitleArray[counter] = testDataTitlerow.createCell(counter);
			testDataTitleArray[counter].setCellValue(testDataHeader[counter]);
		}
		if (testDataItemsList != null && !testDataItemsList.isEmpty()) {

			String[] testDataValueArray = new String[testDataHeaderColCount];
			int[] testDataColTypeArray = new int[testDataHeaderColCount];
			int colCounter = 0;
			Cell[] testDataCells = new Cell[testDataHeaderColCount];
			int dataItemCount = 0;
			// TestCase and TestSteps Processing
			for (TestDataItems testDataItem : testDataItemsList) {
				int celNum=0;
				// Creating Object array for holding test case data
				log.info("String array for holding test data");

				Row testDatarow = null;


				//TestDataItems testDataItem = testDataItemValues.getTestDataItems();
				
				List testDataList = new ArrayList<TestDataItemValues>(testDataItem.getTestDataItemsValSet());
				java.util.Collections.sort(testDataList, new Comparator<TestDataItemValues>(){
					@Override
					public int compare(TestDataItemValues tcl1, TestDataItemValues tcl2) {
						return tcl1.getTestDataValueId().compareTo(tcl2.getTestDataValueId());
					}
				});
				List<TestDataItemValues> testItemValList = testDataList;
				for(TestDataItemValues testDataItemValue : testItemValList){
					TestDataPlan testDataPlan  = testDataItemValue.getTestDataPlan();
					
					log.info("Data Item Id :"+testDataItem.getTestDataItemId()+" Data :"+testDataItem.getDataName()+" type:"+testDataItem.getType()+" Handle:"+testDataItem.getGroupName()+" Remarks:"+testDataItem.getRemarks()+" Plan Id:"+testDataPlan.getTestDataPlanId()+" Plan Name:"+testDataPlan.getTestDataPlanName()+" Value :"+testDataItemValue.getValues());					
					if(testdataItemsUniqueSet.add(testDataItem.getTestDataItemId())){
						testDataValueArray = new String[testDataHeaderColCount];
						testDataColTypeArray = new int[testDataHeaderColCount];
						testDatarow = testDataSheet.createRow(rowNum++);
						colNum = 0;

						testDataValueArray[colNum] = testDataItem.getDataName();
						testDataColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

						testDataValueArray[colNum] = testDataItem.getType();
						testDataColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
						// handle name column
						/*testDataValueArray[colNum] = testDataItem.getGroupName();
						testDataColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;*/

						testDataValueArray[colNum] = testDataItem.getRemarks();
						testDataColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;						

						testDataValueArray[colNum] = testDataPlan.getTestDataPlanName();
						testDataColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;						
						testDataCells = new Cell[testDataHeaderColCount];
						for ( colCounter = 0; colCounter < testDataHeaderColCount; colCounter++) {
							testDataCells[colCounter] = testDatarow.createCell(celNum++);
							testDataCells[colCounter].setCellType(testDataColTypeArray[colCounter]);

							if (testDataCells[colCounter].getCellType() == Cell.CELL_TYPE_NUMERIC) {
								testDataCells[colCounter].setCellValue(Integer.parseInt(testDataValueArray[colCounter]));
							} else if (testDataCells[colCounter].getCellType() == Cell.CELL_TYPE_STRING) {
								testDataCells[colCounter].setCellValue((testDataValueArray[colCounter]));
							} else {
								testDataCells[colCounter].setCellValue((testDataValueArray[colCounter]));
							}
						}
						//testDataColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;	
						//testDataValueArray[colNum] = testDataItemValues.getValues();
						//log.info("testDataValueArray[colNum] === "+testDataValueArray[colNum]);

					}
					if(testDataItemValue.getValues() != null){
						if(testDataItem.getType().equalsIgnoreCase("Number")){ 
							testDataCells[colNum].setCellType(Cell.CELL_TYPE_NUMERIC);
							testDataCells[colNum].setCellValue(new Double(testDataItemValue.getValues()));
						}
						else {
							testDataCells[colNum].setCellType(Cell.CELL_TYPE_STRING);
							testDataCells[colNum].setCellValue(testDataItemValue.getValues());
						}
					}

					testDataColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
				}
			}
		}
		workbook.write(fos);
		fos.close();
	} catch (FileNotFoundException e) {
		log.error("eroor: "+e);
		return false;
	} catch (IOException e) {

		log.error("eroor: "+e);
		return false;
	}
	return true;
}
}