package com.hcl.atf.taf.integration.data.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.constants.ValidateFieldConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.WorkShiftMasterDAO;
import com.hcl.atf.taf.integration.data.UserDataIntegrator;
import com.hcl.atf.taf.model.AuthenticationType;
import com.hcl.atf.taf.model.CommonActiveStatusMaster;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.model.VendorMaster;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.dto.ImportFileValidationDTO;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.CustomerService;
import com.hcl.atf.taf.service.DeviceListService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.ResourceManagementService;
import com.hcl.atf.taf.service.SkillService;
import com.hcl.atf.taf.service.TestExecutionBugsService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.TestFactoryManagementService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.UserTypeMasterNewService;
import com.hcl.atf.taf.service.VendorListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.jira.rest.JiraConnector;
import com.hcl.atf.taf.model.Comments;

public class ExcelUserDataIntegrator implements UserDataIntegrator {
	
	
	
	@Value("#{ilcmProps['USER_ROLE']}")
    private String userRoleProperty;

	@Value("#{ilcmProps['USER_TYPE']}")
    private String userTypeProperty;
	
	@Autowired
	public TestSuiteConfigurationService testSuiteConfigurationService;

	@Autowired
	public ProductListService productListService;

	@Autowired
	TestExecutionBugsService testExecutionBugsService;

	@Autowired
	TestExecutionService testExecutionService;

	@Autowired
	DeviceListService deviceListService;

	@Autowired
	TestRunConfigurationService testRunConfigservice;

	@Autowired
	UserListService userListService;
	
	@Autowired
	private EventsService eventService;
	
	@Autowired
	UserTypeMasterNewService userTypeMasterNewService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	VendorListService vendorListService;
	
	@Autowired
	ResourceManagementService resourceManagementService;
	
	@Autowired
	WorkPackageService workPackageService;
	
	@Autowired
	TestFactoryManagementService testFactoryManagementService;
	
	@Autowired
	private MongoDBService mongoDBService;
	
	@Autowired
	private WorkShiftMasterDAO workShiftMasterDAO;
	
	
	@Autowired
	private SkillService skillService;
	
	@Autowired
	private CommonService commonService;
	
	@Value("#{ilcmProps['DEFAULT_PASSWORD']}")
    private String default_password;
	
	ProductFeature productFeature;

	ProductMaster productMaster;

	@Value("#{ilcmProps['REPORT_FILE_PATH']}")
	private String reportFilePath;
	
	TestfactoryResourcePool resourcePool;
	
	public static final int testCaseIDIndex = 1;
	public static final int userIDIndex = 1;
	public static final int testCaseNameindex = 0;
	
	//For User Import sheet Column index
	int userFirstNameIndex = 0;
	int userMiddleNameIndex = 1;
	int userLastNameIndex = 2;
	int userLoginIdIndex = 3;
	int userCodeIndex = 4;
	int userDisplayNameIndex = 5;
	int userEmailIdIndex = 6;
	int userContactNumberIndex = 7;
	int userTypeIndex = 8;
	int userCustomerIndex = 9;
	int userRoleIndex = 10;
	int userLanguageIndex = 11;
	int userResourcePoolIndex = 12;
	int userFromDate = 13;
	int userToDate = 14;	
	int userVendorIndex = 15;
	int userAuthenticationTypeIndex = 16;
	
	public static final int testCaseDescIndex = 2;
	public static final int userDescIndex = 2;
	public static final int productFeatureNameIndex = 7;
	public static final int testCaseCreatedDateindex = 3;
	public static final int testCasePriorityIndex = 5;
	public static final int testCaseTypeindex = 6;

	public static final int testCaseLastUpdatedDateIndex = 4;
	public static final int testcaseCodeIndex = 8;
	public static final int testSuiteIdIndex = 9;
	public static final int testCaseSourceIndex = 10;
	public static final int productIdIndex = 11;

	public String productFeatureName = "";
	public static final int testCaseStartRow = 0;
	public static final int usersStartRow = 0;
	public boolean status = false;
	public static final int maxBlankRowCount = 3;

	static final String DATA_ADD = "ADD";
	private static final String DATA_UPDATE = "UPDATE";
	private static final String DATA_ERROR = "ERROR";

	// Naming convention for TestCases
	private static final String TC_NAMING_CONV = "TAF_ETC_";

	// Naming convention for TestExecution Results
	private static final String TER_NAMING_CONV = "TAF_ETER_";

	// Naming convention for Defects
	private static final String DEFECTS_NAMING_CONV = "TAF_DEFECTS_";

	// Naming convention for TestCases
	private static final String TC_NAMING_CONV_WORKPACKAGE = "TFWF_WPTCEP";

	// Priorities
	private static final int PRIORITY_CRITICAL = 1;
	private static final int PRIORITY_HIGH = 2;
	private static final int PRIORITY_MEDIUM = 3;
	private static final int PRIORITY_TRIVIAL = 4;

	private static final Log log = LogFactory
			.getLog(ExcelUserDataIntegrator.class);

	@Override
	public boolean importTestCases(String excelDataFile, int productId,
			String testCaseSource) {

		// productId = 0;//For Temporary validation of code
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
						}

						Cell cell2 = row.getCell(testCaseIDIndex);
						if (isCellValid(cell2)) {
							testCase.setTestCaseCode(cell2.getStringCellValue());
							System.out.println("Test Case Code: "
									+ cell2.getStringCellValue());
						} else {
							testCaseIDIsMissing = true;
						}

						if (testCaseNameIsMissing && testCaseIDIsMissing) {
							blankRowCount++;
							System.out.println("No of Blank Rows : "
									+ blankRowCount);
							if (blankRowCount > maxBlankRowCount) {
								log.info("Max Blank rows encountered. Exiting the document");
								
								break;
							} else {
								log.info("Blank row encountered. Skipping to next row");
								rowNum++;
								continue;
							}
						} else {
							// Reset the blankrow counter
							blankRowCount = 0;
							if (testCaseNameIsMissing) {
								log.info("TestCase name is missing. Skipping the row");
								rowNum++;
								continue;
							}
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
								log.error("exception in parsing data value : ", e);
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
								log.error("Error in date parsing : ", e);
							}
						}

						Cell cell6 = row.getCell(testCasePriorityIndex);
						if (isCellValid(cell6)) {
							TestCasePriority tcp = new TestCasePriority();
							tcp.setPriorityName(cell6.getStringCellValue());
							testCase.setTestCasePriority(tcp);
						}

						Cell cell7 = row.getCell(testCaseTypeindex);
						if (isCellValid(cell7)) {
							int testCaseType = Integer.parseInt((cell7
									.getStringCellValue()));
							if (testCaseType == 0) {
								testCase.setTestCaseType(TAFConstants.TESTCASE_MANUAL);
							} else if (testCaseType == 1) {
								testCase.setTestCaseType(TAFConstants.TESTCASE_AUTOMATED);
							} else {
								testCase.setTestCaseType(TAFConstants.TESTCASE_MIXED);
							}
						}

						Cell cell9 = row.getCell(productFeatureNameIndex);
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
					+ "\\" + fileName);

			
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
					+ "\\" + fileName);


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

	@Override
	public boolean exportTestResults(int testRunListId, String exportLocation) {

		Boolean status;
		List<TestExecutionResult> testExecutionResults = testExecutionService
				.listTestExecutionResult(testRunListId);
		status = generateAndExportTestExecResults(testExecutionResults,
				testRunListId, exportLocation);

		return status;

	}

	public Boolean generateAndExportTestExecResults(
			List<TestExecutionResult> testExecutionResults, int testRunListId,
			String exportLocation) {

		Boolean status = false;
		TestSuiteList testSuiteList;
		ProductMaster product;
		TestRunList testRunList;
		TestRunConfigurationChild testRunConfigChild;
		String productName = "";
		String productVersionName = "";
		String testSuiteName = "";
		int testRunNo = 0;
		String testRunConfigName = "";
		int totalTestExecResultsCount = 0;
		String testRunStartTime = "";
		String testRunEndTime = "";

		try {

			String fileExportlocation = exportLocation + "TestExecResults";
			boolean isDirCreated = checkAndCreateTempDirectory(fileExportlocation);

			log.info("File Directories created:" + isDirCreated);
			

			

			if (testExecutionResults != null & !testExecutionResults.isEmpty()) {

				TestExecutionResult testExecResult = testExecutionResults
						.get(0);

				testSuiteList = testExecResult.getTestSuiteList();
				testRunList = testExecResult.getTestRunList();

				if (testSuiteList != null) {
					// Product Details
					product = testSuiteList.getProductMaster();
					if (product != null) {
						productName = product.getProductName();
					}

					ProductVersionListMaster productVersion = testSuiteList
							.getProductVersionListMaster();
					if (productVersion != null) {
						productVersionName = productVersion
								.getProductVersionName();
					}

					testSuiteName = testSuiteList.getTestSuiteName();
				}

				if (testRunList != null) {
					// TestRun start and End Time
					SimpleDateFormat sdf = new SimpleDateFormat(
							"E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

					Date testRunStartDate = testRunList.getTestRunStartTime();
					if (testRunStartDate != null) {
						testRunStartTime = sdf.format(testRunStartDate);
					}

					Date testRunEndDate = testRunList.getTestRunEndTime();
					if (testRunEndDate != null) {
						testRunEndTime = sdf.format(testRunEndDate);
					}

					// TestRun Configuration Details
					testRunNo = testExecResult.getTestRunList().getRunNo();

					testRunConfigChild = testExecResult.getTestRunList()
							.getTestRunConfigurationChild();

					if (testRunConfigChild != null) {
						testRunConfigName = testRunConfigChild
								.getTestRunConfigurationName();
					}
				}
			}

			String fileName = TER_NAMING_CONV + testRunNo + ".xlsx";
			

			FileOutputStream fos = new FileOutputStream(fileExportlocation
					+ "\\" + fileName);

			String userName = SecurityContextHolder.getContext()
					.getAuthentication().getName();

			System.out
					.println("Export Test Exec Results : Generate Excel data");

			XSSFWorkbook workbook = new XSSFWorkbook();

			// Overview Sheet
			Sheet testExecResultsOverviewSheet = workbook
					.createSheet("Overview");

			// Defects Sheet
			Sheet testExecResultsSheet = workbook
					.createSheet("TestExecutionResults");

			String[] overViewHeaders = { "Summary", "Product",
					"ProductVersion", "TestSuite", "TestRunNo",
					"TestRunConfigChild", "Build No", "Total Results",
					"TestRun start time", "TestRun End time" };

			Row[] overViewRows = new Row[overViewHeaders.length];

			for (int i = 0; i < overViewHeaders.length; i++) {
				overViewRows[i] = testExecResultsOverviewSheet.createRow(i);
			}

			// Creating Overview Sheet Cells
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

			// Setting Overview Sheet Cell Values
			// Summary Row
			cellR1.setCellValue("TAF Execution Results Summary");
			cellR2.setCellValue(new Date(System.currentTimeMillis()).toString());

			// Product Details
			cellR3.setCellValue("Product");
			cellR4.setCellValue(productName);

			// Product Version
			cellR5.setCellValue("Product Version");
			cellR6.setCellValue(productVersionName);

			// TestSuite
			cellR7.setCellValue("TestSuite");
			cellR8.setCellValue(testSuiteName);

			// Test Run No
			cellR9.setCellValue("Test Run No");
			cellR10.setCellValue(testRunNo);

			// Test Run Configuration
			cellR11.setCellValue("Test Run Configuration");
			cellR12.setCellValue(testRunConfigName);

			// Build No
			cellR13.setCellValue("Build No");
			cellR14.setCellValue("");

			// Total Records
			cellR15.setCellValue("Total Results");
			// cellR16.setCellValue();

			// Test Run Start Time
			cellR17.setCellValue("Test Run Start Time");
			cellR18.setCellValue(testRunStartTime);

			// Test Run End Time
			cellR19.setCellValue("Test Run End Time");
			cellR20.setCellValue(testRunStartTime);

			// TestExec Results Sheet Header
			String[] testExecResultsHeader = { "testExecutionResultId",
					"testCaseCode", "testStepCode", "testStepName",
					"testStepDescription", "testStepInput",
					"testStepExpectedOutput", "testStepObservedOutput",
					"testResultStatus", "productFeatureName", "DeviceId",
					"DeviceModel", "DeviceMake", "DevicePlatformName",
					"DevicePlatformVersion", "screenShotPath", "startTime",
					"endTime", "failureReason", "executionRemarks",
					"ExecutedBy" };

			int testExecResultsColCount = testExecResultsHeader.length;

			int testExecResultsRowNum = 0;

			Row testExecResultsHeaderRow = testExecResultsSheet.createRow(0);

			// Setting defect header values
			Cell[] testExecResultsHeaderArray = new Cell[testExecResultsColCount];

			for (int counter = 0; counter < testExecResultsColCount; counter++) {
				testExecResultsHeaderArray[counter] = testExecResultsHeaderRow
						.createCell(counter);
				testExecResultsHeaderArray[counter]
						.setCellValue(testExecResultsHeader[counter]);
			}

			// Processing defects list
			if (testExecutionResults != null && !testExecutionResults.isEmpty()) {
				totalTestExecResultsCount = testExecutionResults.size();
				cellR16.setCellValue(totalTestExecResultsCount);

				// Creating defects row for each defect
				for (TestExecutionResult testExecutionResult : testExecutionResults) {

					testExecResultsRowNum++;
					int testExecResultsColNum = 0;
					if (testExecutionResult != null) {
						Row testExecResultsRow = testExecResultsSheet
								.createRow(testExecResultsRowNum);

						System.out.println("testExecResultsColNum:"
								+ testExecResultsColNum);

						// Forming String array for ExecResults column values
						// Forming String array for ExecResults column types
						String[] testExecResultsValueArray = new String[testExecResultsColCount];
						int[] testExecResultsColTypeArray = new int[testExecResultsColCount];

						if (testExecutionResult.getTestExecutionResultId() != null) {
							testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
									.getTestExecutionResultId().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						if (testExecutionResult.getTestCaseList() != null
								&& testExecutionResult.getTestCaseList()
										.getTestCaseCode() != null) {
							testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
									.getTestCaseList().getTestCaseCode();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						if (testExecutionResult.getTestCaseStepsList() != null
								&& testExecutionResult.getTestCaseStepsList()
										.getTestStepCode() != null) {
							testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
									.getTestCaseStepsList().getTestStepCode();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						if (testExecutionResult.getTestStep() != null) {
							testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
									.getTestStep().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						if (testExecutionResult.getTestStepDescription() != null) {
							testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
									.getTestStepDescription().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						if (testExecutionResult.getTestStepInput() != null) {
							testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
									.getTestStepInput().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						if (testExecutionResult.getTestStepExpectedOutput() != null) {
							testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
									.getTestStepExpectedOutput().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						if (testExecutionResult.getTestStepObservedOutput() != null) {
							testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
									.getTestStepObservedOutput().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						if (testExecutionResult.getTestResultStatusMaster() != null) {
							testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
									.getTestResultStatusMaster()
									.getTestResultStatus();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						if (testExecutionResult.getTestCaseList() != null
								&& testExecutionResult.getTestCaseList()
										.getProductFeature() != null) {
							testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
									.getTestCaseList().getProductFeature()
									.iterator().next().getProductFeatureName();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						// Device Details
						DeviceList deviceList = testExecutionResult
								.getTestRunList().getDeviceList();
						if (deviceList != null) {
							testExecResultsValueArray[testExecResultsColNum] = deviceList
									.getDeviceId();
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

							testExecResultsValueArray[testExecResultsColNum] = testExecResultsValueArray[testExecResultsColNum] = deviceList
									.getDeviceModelMaster().getDeviceModel();
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

							testExecResultsValueArray[testExecResultsColNum] = deviceList
									.getDeviceModelMaster()
									.getDeviceMakeMaster().getDeviceMake();
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

							testExecResultsValueArray[testExecResultsColNum] = deviceList
									.getDevicePlatformVersionListMaster()
									.getDevicePlatformMaster()
									.getDevicePlatformName();
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

							testExecResultsValueArray[testExecResultsColNum] = deviceList
									.getDevicePlatformVersionListMaster()
									.getDevicePlatformVersion();
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

							testExecResultsValueArray[testExecResultsColNum] = "";
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

							testExecResultsValueArray[testExecResultsColNum] = "";
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

							testExecResultsValueArray[testExecResultsColNum] = "";
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

							testExecResultsValueArray[testExecResultsColNum] = "";
							testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;
						}

						testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
								.getScreenShotPath();
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						if (testExecutionResult.getStartTime() != null) {
							testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
									.getStartTime().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						if (testExecutionResult.getEndTime() != null) {
							testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
									.getEndTime().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						if (testExecutionResult.getFailureReason() != null) {
							testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
									.getFailureReason().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						if (testExecutionResult.getExecutionRemarks() != null) {
							testExecResultsValueArray[testExecResultsColNum] = testExecutionResult
									.getExecutionRemarks().toString();
						} else {
							testExecResultsValueArray[testExecResultsColNum] = "";
						}
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						testExecResultsValueArray[testExecResultsColNum] = userName;
						testExecResultsColTypeArray[testExecResultsColNum++] = Cell.CELL_TYPE_STRING;

						// Setting Values in cells
						Cell[] testExecResultCells = new Cell[testExecResultsColCount];

						for (int colCounter = 0; colCounter < testExecResultsColCount; colCounter++) {
							testExecResultCells[colCounter] = testExecResultsRow
									.createCell(colCounter);
							testExecResultCells[colCounter]
									.setCellType(testExecResultsColTypeArray[colCounter]);

							if (testExecResultCells[colCounter].getCellType() == Cell.CELL_TYPE_NUMERIC) {
								testExecResultCells[colCounter]
										.setCellValue(Integer
												.parseInt(testExecResultsValueArray[colCounter]));
							}

							else if (testExecResultCells[colCounter]
									.getCellType() == Cell.CELL_TYPE_STRING) {
								testExecResultCells[colCounter]
										.setCellValue((testExecResultsValueArray[colCounter]));
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
		return status;
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
					//testExecutionResultBugList.getTestExecutionResult();
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
						// Environment-Device Details

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

						if (testCaseList != null
								&& testCaseList.getTestCasePriority() != null) {
						/*if (testCaseList != null
								&& testCaseList.getExecutionPriority() != null) {	*/						
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
					System.out
							.println("Values for insert defect: productName, IssueTypename, summary, priorityName, environment, description, null, productVersionName"
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
						log.error("Unable to create defects", e);
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
		String plannedExecutionDate="";
		String reportGenerationDate="";
		try {

			if (workPackage != null) {
				workPackageName = workPackage.getName();
				productName = workPackage.getProductBuild().getProductVersion()
						.getProductMaster().getProductName();
				productVersionName=workPackage.getProductBuild().getProductVersion().getProductVersionName();
				buildName=workPackage.getProductBuild().getBuildname();
				runName=workPackage.getName();
			}

			// Export location is specified and fileoutput stream is created for
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
					+ "\\" + fileName);

			
			int rowCount = 0;
			// From testCaseList number of test cases is obtained.
			XSSFWorkbook workbook = new XSSFWorkbook();
			if (workPackageTestCaseExecutionPlanList != null
					&& !workPackageTestCaseExecutionPlanList.isEmpty()) {
				rowCount = workPackageTestCaseExecutionPlanList.size();
			}

			Sheet workPackageTestCaseExecutionPlanSheet = workbook
					.createSheet("WorkPackageTestCaseExecutionPlan");

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
			Row overviewReportGenerationDateRow = workPackageTestCaseExecutionPlanSheet
					.createRow(6);

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
			cellR9.setCellValue("PlannedExecutionDate");

			Cell cellR10 = overviewPlannedExecutionDateRow.createCell(2);
			cellR10.setCellValue("null"); 
			Cell cellR11 = overviewReportGenerationDateRow.createCell(1);
			cellR11.setCellValue("ReportGenerationDate");

			Cell cellR12 = overviewReportGenerationDateRow.createCell(2);
			cellR12.setCellValue(reportGenerationDate);

			// TestCase and TestSteps titles processing
			int rowNum = 8;
			int colNum = 1;
			

			// Title Row for TestCase
			String[] workPackageTestCaseExecutionPlanHeader = { "testCaseId",
					"testCaseName", "environmentName","deviceName", "testLeadName",
					"testerName", "plannedExcecutionDate",
					"actualExecutionDate", "executionStatus" };

			System.out.println("Header row: "
					+ workPackageTestCaseExecutionPlanHeader[0]
					+ workPackageTestCaseExecutionPlanHeader[1]
					+ workPackageTestCaseExecutionPlanHeader[2]
					+ workPackageTestCaseExecutionPlanHeader[3]
					+ workPackageTestCaseExecutionPlanHeader[4]
					+ workPackageTestCaseExecutionPlanHeader[5]
					+ workPackageTestCaseExecutionPlanHeader[6]
					+ workPackageTestCaseExecutionPlanHeader[7]
					+ workPackageTestCaseExecutionPlanHeader[8]);
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
					if(workPackageTestCaseExecutionPlan	.getRunConfiguration().getRunconfiguration()!=null)	
						workPackageTestCaseExecutionPlanValueArray[colNum] = workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getEnvironmentcombination().getEnvironmentCombinationName();
					else
						workPackageTestCaseExecutionPlanValueArray[colNum] = "";
					
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
					if(workPackageTestCaseExecutionPlan	.getRunConfiguration().getRunconfiguration()!=null)	
						if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
							workPackageTestCaseExecutionPlanValueArray[colNum] = workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice().getName();
						else if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getHostList()!=null)
							workPackageTestCaseExecutionPlanValueArray[colNum] = workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getHostList().getHostIpAddress();
					else
						workPackageTestCaseExecutionPlanValueArray[colNum] = "";
					
					
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;
					if(workPackageTestCaseExecutionPlan.getTestLead()!=null){
					workPackageTestCaseExecutionPlanValueArray[colNum] = workPackageTestCaseExecutionPlan
							.getTestLead().getFirstName();
					}else{
					workPackageTestCaseExecutionPlanValueArray[colNum] = "";
					}
					
					workPackageTestCaseExecutionPlanColTypeArray[colNum++] = Cell.CELL_TYPE_STRING;

					if(workPackageTestCaseExecutionPlan.getTester()!=null){
					workPackageTestCaseExecutionPlanValueArray[colNum] = workPackageTestCaseExecutionPlan
							.getTester().getFirstName();
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
					}}else{
						executionStatus="NotStarted";
					}
					workPackageTestCaseExecutionPlanValueArray[colNum] =executionStatus;
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
	public String importUserLists(HttpServletRequest request, String excelDataFile, InputStream is, String userType) {
		HashMap<String, Object> usersMap = readUserListFromExcelFile(excelDataFile, is, userType);
		List<UserList>	 users = (List<UserList>) usersMap.get("users");
		String message = (String) usersMap.get("message");
		String filePathAndName = (String)usersMap.get("filePathAndName");
		if(null != filePathAndName && "" != filePathAndName){
			message += ";"+filePathAndName;
		}
		
			userListService.updateUsers(users);
			return message;
	}
	
	private HashMap<String,List<TestCaseList>> verifyExistingTestCases(List<TestCaseList> testCasesExcel ,int productId) {
		HashMap<String,List<TestCaseList>> tesCaseMap= new HashMap<String,List<TestCaseList>>();
		List<TestCaseList> testCasesUpdate= new ArrayList<TestCaseList>();
		List<TestCaseList> testCasesNew= new ArrayList<TestCaseList>();
		List<TestCaseList> testCasesDB=testSuiteConfigurationService.getTestCaseListByProductId(productId);
		
		String testCaseCodeExcel=null;
		String testCaseCodeDB=null;
		String testCaseNameExcel=null;
		String testCaseNameDB=null;
		
		//Comparing the testcase code
		for(TestCaseList testCaseExcel: testCasesExcel){
			testCaseCodeExcel =testCaseExcel.getTestCaseCode();
			testCaseNameExcel =testCaseExcel.getTestCaseName();
			if(testCasesDB!=null && !testCasesDB.isEmpty()){
				for(TestCaseList testCaseDB : testCasesDB){
					testCaseCodeDB=testCaseDB.getTestCaseCode();
					testCaseNameDB=testCaseDB.getTestCaseName();
					if(testCaseCodeDB!=null && testCaseCodeExcel!=null && testCaseCodeExcel.equals(testCaseCodeDB)){
						
						testCaseExcel.setTestCaseId(testCaseDB.getTestCaseId());
						testCasesUpdate.add(testCaseExcel);
					}else if(testCaseNameDB!= null &&testCaseNameExcel!=null && testCaseNameExcel.equals(testCaseNameDB)){
						
						testCaseExcel.setTestCaseId(testCaseDB.getTestCaseId());
						testCasesUpdate.add(testCaseExcel);
					}else{
						testCasesNew.add(testCaseExcel);
					}
				}
			}else{
				testCasesNew.add(testCaseExcel);
			}
		}
		tesCaseMap.put("Update", testCasesUpdate);
		tesCaseMap.put("New", testCasesNew);
		return tesCaseMap;
	}
	
	private HashMap<String,List<UserList>> verifyExistingUsers(List<TestCaseList> testCasesExcel ,int resourcePoolId) {
		HashMap<String,List<TestCaseList>> tesCaseMap= new HashMap<String,List<TestCaseList>>();
		HashMap<String,List<UserList>> userMap= new HashMap<String,List<UserList>>();
		List<TestCaseList> testCasesUpdate= new ArrayList<TestCaseList>();
		List<TestCaseList> testCasesNew= new ArrayList<TestCaseList>();
		List<TestCaseList> testCasesDB=testSuiteConfigurationService.getTestCaseListByProductId(resourcePoolId);
		//Change the code later to UserList from TestcaseList
		String testCaseCodeExcel=null;
		String testCaseCodeDB=null;
		String testCaseNameExcel=null;
		String testCaseNameDB=null;
		
		//Comparing the testcase code
		for(TestCaseList testCaseExcel: testCasesExcel){
			testCaseCodeExcel =testCaseExcel.getTestCaseCode();
			testCaseNameExcel =testCaseExcel.getTestCaseName();
			if(testCasesDB!=null && !testCasesDB.isEmpty()){
				for(TestCaseList testCaseDB : testCasesDB){
					testCaseCodeDB=testCaseDB.getTestCaseCode();
					testCaseNameDB=testCaseDB.getTestCaseName();
					if(testCaseCodeDB!=null && testCaseCodeExcel!=null && testCaseCodeExcel.equals(testCaseCodeDB)){
						
						testCaseExcel.setTestCaseId(testCaseDB.getTestCaseId());
						testCasesUpdate.add(testCaseExcel);
					}else if(testCaseNameDB!= null &&testCaseNameExcel!=null && testCaseNameExcel.equals(testCaseNameDB)){
						
						testCaseExcel.setTestCaseId(testCaseDB.getTestCaseId());
						testCasesUpdate.add(testCaseExcel);
					}else{
						testCasesNew.add(testCaseExcel);
					}
				}
			}else{
				testCasesNew.add(testCaseExcel);
			}
		}
		tesCaseMap.put("Update", testCasesUpdate);
		tesCaseMap.put("New", testCasesNew);
		return userMap;
	}
	private List<TestCaseList> readTestCasesFromExcelFile(String excelDataFile,
			ProductMaster productMaster, InputStream fis) {

		int blankRowCount = 0;
		URL url = null;
		System.out
				.println("Import - Excel file from location:" + excelDataFile);
		List<TestCaseList> testCases = new ArrayList<TestCaseList>();
		List<Integer> errorRows = new ArrayList<Integer>();
		TestCaseList testCase = new TestCaseList();

		int numberOfSheets = 0;
		String dataValidStatus = "";
		try { // 1

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

						boolean testCaseNameIsMissing = false;
						boolean testCaseIDIsMissing = false;

						// TestCase name is mandatory. If not present skip row
						Cell cell1 = row.getCell(testCaseNameindex);
						if (isCellValid(cell1)) {
							cell1.setCellType(Cell.CELL_TYPE_STRING);
							testCase.setTestCaseName(cell1.getStringCellValue());
							System.out.println("Test Case Name: "
									+ cell1.getStringCellValue());
						} else {
							testCaseNameIsMissing = true;
						}

						Cell cell2 = row.getCell(testCaseIDIndex);
						if (isCellValid(cell2)) {
							cell2.setCellType(Cell.CELL_TYPE_STRING);
							testCase.setTestCaseCode(cell2.getStringCellValue());
							System.out.println("Test Case Code: "
									+ cell2.getStringCellValue());
						} else {
							testCaseIDIsMissing = true;
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
								log.error("exception in parsing data value : ", e);
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

						Cell cell6 = row.getCell(testCasePriorityIndex);
						if (isCellValid(cell6)) {
							cell6.setCellType(Cell.CELL_TYPE_STRING);
							TestCasePriority tcp = new TestCasePriority();
							tcp.setPriorityName(cell6.getStringCellValue());
							testCase.setTestCasePriority(tcp);
						}

						Cell cell7 = row.getCell(testCaseTypeindex);
						if (isCellValid(cell7)) {
							cell7.setCellType(Cell.CELL_TYPE_STRING);
							int testCaseType = Integer.parseInt((cell7
									.getStringCellValue()));
							if (testCaseType == 0) {
								testCase.setTestCaseType(TAFConstants.TESTCASE_MANUAL);
							} else if (testCaseType == 1) {
								testCase.setTestCaseType(TAFConstants.TESTCASE_AUTOMATED);
							} else {
								testCase.setTestCaseType(TAFConstants.TESTCASE_MANUAL);
							}
						}

						
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

	private HashMap<String, Object> readUserListFromExcelFile(String excelDataFile, InputStream fis, String userType) {

		
		int validRecordCount = 0;
		int invalidRecordCount = 0;	
		
		UserList user = new UserList();
		String message = " ";
		HashMap<String,Object> usersMap = new HashMap<String,Object>();
		
		ArrayList<UserList> users = new ArrayList<UserList>();
		DataFormatter formatter = new DataFormatter();
		
		String currentDate = DateUtility.dateToStringInSecond(new Date());
		currentDate = currentDate.replaceAll(":", "_");
		String regUserReportFileName = "RegularUserReport"+currentDate+".xlsx";
		int isValidRowNum = 0;
		String rowStatus = "";
		String statusRemarks = "";
		//Store invalid row data into a map for data parsing
		Map<Integer,String> invalidRowMap = new HashMap<Integer,String>();
		
		try {
			List<String> availableUserLoginIds = userListService.getUserLoginIds();
			if(availableUserLoginIds == null){
				availableUserLoginIds = new ArrayList<String>();
			}
			Workbook workbook = null;
			if (excelDataFile.endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			}
			if (excelDataFile.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fis);
			}
			Sheet sheet = workbook.getSheetAt(0);
			
			int rowCount = sheet.getPhysicalNumberOfRows();
			int rowNum = 0;
			
			Boolean isValidRecord = true;
			int totalRecordCount = 0;
			if (rowCount < 1) {
				log.info("No rows present in the test cases worksheet");
				return null;
			} else { 
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
	    			Cell cell = headerRow.createCell(i);
	    			cell.setCellStyle(css);
	    			if(i==totalHeaderCells){    				
	    				cell.setCellValue("Status");    				
	    			} else if(i==totalHeaderCells+1){   				
	    				cell.setCellValue("Remarks");    				
	    			}
	    		}
				
				for (rowNum = 0; rowNum < rowCount; rowNum++) {
					isValidRecord = true;
					Row row;
					
					row = sheet.getRow(rowNum);
					if (row == null) {
						
						rowNum++;
						continue;
					}
					if (row.getRowNum() <= usersStartRow) {
						continue;
					} else { 
						
						int columnCount = row.getLastCellNum();						
						for(int columnNum = 0; columnNum < columnCount; columnNum++){
							if(row.getCell(columnNum) != null && !row.getCell(columnNum).getStringCellValue().trim().isEmpty()){
								totalRecordCount++;
								break;
							}
						}
						
						
						
						user = new UserList();
						Cell cell = row.getCell(userFirstNameIndex);
						if (isCellValid(cell)) {
							cell.setCellType(Cell.CELL_TYPE_STRING);						
							user.setFirstName(cell.getStringCellValue());
							
						}
						if(user.getFirstName() == null || user.getFirstName().trim().isEmpty()){
							isValidRecord = false;
							isValidRowNum = rowNum;
							rowStatus = "Not Updated";
							statusRemarks = "FirstName is empty ~";
						}

						cell = row.getCell(userMiddleNameIndex);
						if (isCellValid(cell)) {
							cell.setCellType(Cell.CELL_TYPE_STRING);					
							user.setMiddleName(cell.getStringCellValue());
						}
						if(user.getMiddleName() == null){
							user.setMiddleName("");
						}

						cell = row.getCell(userLastNameIndex);
						if (isCellValid(cell)) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							user.setLastName(cell.getStringCellValue());
						}
						if(user.getLastName() == null || user.getLastName().trim().isEmpty()){
							isValidRecord = false;
							isValidRowNum = rowNum;
							rowStatus = "Not Updated";
							statusRemarks += "LastName is empty ~";
						}
							
						cell = row.getCell(userLoginIdIndex);
						String loginId = "";
						if (isCellValid(cell)) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							loginId = cell.getStringCellValue();
							if(loginId != null && !loginId.trim().isEmpty() && loginId.length() >= 3){
								if(!availableUserLoginIds.contains(loginId)){
									user.setLoginId(loginId);
									availableUserLoginIds.add(loginId);
								}else{
									isValidRecord = false;
									isValidRowNum = rowNum;
									rowStatus = "Not Updated";
									statusRemarks += "Invalid loginId ~";
								}
							} else {
								isValidRecord = false;
								isValidRowNum = rowNum;
								rowStatus = "Not Updated";
								statusRemarks += "LoginId should more then three charactors";
							}
						}else {
							isValidRecord = false;
							isValidRowNum = rowNum;
							rowStatus = "Not Updated";
							statusRemarks += "LoginId should not empty!";
						}
						log.info("user Code "+formatter.formatCellValue(row.getCell(userCodeIndex)));
						String userCode = "";
						if(formatter.formatCellValue(row.getCell(userCodeIndex)) != null){
							userCode = formatter.formatCellValue(row.getCell(userCodeIndex));
							user.setUserCode(userCode);
						}
						
						if(user.getUserCode() == null || user.getUserCode().trim().isEmpty()){
							isValidRecord = false;
							isValidRowNum = rowNum;
							rowStatus = "Not Updated";
							statusRemarks += "UserCode is empty ~";
						}					
						cell = row.getCell(userDisplayNameIndex);
						if (isCellValid(cell)) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							user.setUserDisplayName(cell.getStringCellValue());
						}
						if(user.getUserDisplayName() == null || user.getUserDisplayName().trim().isEmpty()){
							if(isValidRecord){
								user.setUserDisplayName(user.getFirstName()+" "+user.getLastName());
							}
						}
						
						cell = row.getCell(userEmailIdIndex);
						if (isCellValid(cell)) {
							cell.setCellType(Cell.CELL_TYPE_STRING);							
							user.setEmailId(cell.getStringCellValue());
						}
						if(user.getEmailId() == null || user.getEmailId().trim().isEmpty()){
							isValidRecord = false;
							isValidRowNum = rowNum;
							rowStatus = "Not Updated";
							statusRemarks += "EmailId is empty ~";
						}						
						
						
						log.info("Contact Number  "+formatter.formatCellValue(row.getCell(userContactNumberIndex)));
						String contactNumber = "";
						if(formatter.formatCellValue(row.getCell(userContactNumberIndex)) != null){
							contactNumber = formatter.formatCellValue(row.getCell(userContactNumberIndex));
							user.setContactNumber(contactNumber);
						}
						
						if(user.getContactNumber() == null || user.getContactNumber().trim().isEmpty()){
							user.setContactNumber("9999999999");
						}
						
						cell = row.getCell(userTypeIndex);
						UserTypeMasterNew userTypeMasterNew = null;
						if (isCellValid(cell)) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							if(userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_FLEX_USER)){
								userTypeMasterNew = userTypeMasterNewService.getUserTypeMasterNewByuserTypeLabel(cell.getStringCellValue());
							}
						}
						if(userTypeMasterNew == null){
							if(userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_FLEX_USER)){
								isValidRecord = false;
								isValidRowNum = rowNum;
								rowStatus = "Not Updated";
								statusRemarks += "UserType is empty ~";
							}else{
								String userTypeProcess = userType;
								if(userType.contains("-")){
									userTypeProcess = userType.split("-")[1].trim();
								}
								userTypeMasterNew = userTypeMasterNewService.getUserTypeMasterNewByuserTypeLabel(userTypeProcess);
								
							}
							
							if(userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_REGULAR_USER)){
								if (isCellValid(cell)) {
									cell.setCellType(Cell.CELL_TYPE_STRING);
									String regularUserType=cell.getStringCellValue();
									userTypeMasterNew = userTypeMasterNewService.getUserTypeMasterNewByuserTypeLabel(regularUserType);
									
								}
							}
							
							if(userTypeMasterNew == null){
								isValidRecord = false;
								isValidRowNum = rowNum;
								rowStatus = "Not Updated";
								statusRemarks += "UserType is empty ~";
							}
						}
						user.setUserTypeMasterNew(userTypeMasterNew);
						
						cell = row.getCell(userCustomerIndex);
						Customer customer = null;
						Set<Customer> customerSet = null;
						if (isCellValid(cell)) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							if(userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_CUSTOMER_USER)){
								customer = customerService.getCustomerByName(cell.getStringCellValue());
								customerSet = new HashSet<Customer>();
								customerSet.add(customer);
							}
						}
						if(customerSet == null || customerSet.size() <= 0){
							if(userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_CUSTOMER_USER)){
								isValidRecord = false;	
								isValidRowNum = rowNum;
								rowStatus = "Not Updated";
								statusRemarks += "Customer is empty ~";
							}
						}
						user.setCustomer(customerSet);
						
						cell = row.getCell(userRoleIndex);
						UserRoleMaster userRoleMaster = null;
						if (isCellValid(cell)) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							userRoleMaster = userListService.getRoleByLabel(cell.getStringCellValue());
						}
						if(userRoleMaster == null){
							isValidRecord = false;
							isValidRowNum = rowNum;
							rowStatus = "Not Updated";
							statusRemarks += "UserRole is empty ~";
						}
						user.setUserRoleMaster(userRoleMaster);
						
						cell = row.getCell(userLanguageIndex);
						Languages languages = null;
						if (isCellValid(cell)) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							languages = userListService.getLanguageByName(cell.getStringCellValue());
						}
						if(languages == null){
							languages = new Languages();
							languages.setId(1);
						}
						user.setLanguages(languages);
						
						cell = row.getCell(userResourcePoolIndex);
						TestfactoryResourcePool testfactoryResourcePool = null;
						if (isCellValid(cell)) {
							cell.setCellType(Cell.CELL_TYPE_STRING);	
							if(!userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_REGULAR_USER) && !userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_CUSTOMER_USER)){
								testfactoryResourcePool = resourceManagementService.getTestFactoryResourcePoolByName(cell.getStringCellValue());
							}else{
								testfactoryResourcePool = new TestfactoryResourcePool();
								testfactoryResourcePool.setResourcePoolId(-10);
							}
						}
						if(testfactoryResourcePool == null){
							if(!userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_REGULAR_USER) && !userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_CUSTOMER_USER)){
								isValidRecord = false;
								isValidRowNum = rowNum;
								rowStatus = "Not Updated";
								statusRemarks += "ResourcePool is empty or not exists ~";
							}else{
								testfactoryResourcePool = new TestfactoryResourcePool();
								testfactoryResourcePool.setResourcePoolId(-10);
							}
						}
						user.setResourcePool(testfactoryResourcePool);
						
						
						
						Date userFromDateValue=null;
						cell = row.getCell(userFromDate);					
						
						if (isCellValid(cell)) {
							if(cell.CELL_TYPE_NUMERIC == cell.getCellType()){
								userFromDateValue = cell.getDateCellValue();
							}else{
								userFromDateValue = DateUtility.dateFormatToDDMMYYYY(cell.getStringCellValue());
							}
							user.setResourceFromDate(userFromDateValue);
							
						} else {
							user.setResourceFromDate(new Date());
						}
						
						Date userToDateValue=null;
						cell = row.getCell(userToDate);							
						if (isCellValid(cell)) {
							if(cell.CELL_TYPE_NUMERIC == cell.getCellType()){
								userToDateValue = cell.getDateCellValue();
							}else{
								userToDateValue = DateUtility.dateFormatToDDMMYYYY(cell.getStringCellValue());
							}
							user.setResourceToDate(userToDateValue);
							
						}else {
							user.setResourceToDate(DateUtility.formatedDateWithMaxYear(new Date()));
						}					
						
						
						cell = row.getCell(userVendorIndex);
						VendorMaster vendor = null;
						if (isCellValid(cell)) {
							cell.setCellType(Cell.CELL_TYPE_STRING);		
							if(!userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_REGULAR_USER) && !userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_CUSTOMER_USER)){
								vendor = vendorListService.getVendorByregisteredCompanyName(cell.getStringCellValue());
							}else{
								vendor = new VendorMaster();
								vendor.setVendorId(-10);
							}
						}
						if(vendor == null){
							if(!userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_REGULAR_USER) && !userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_CUSTOMER_USER)){
								isValidRecord = false;
								isValidRowNum = rowNum;
								rowStatus = "Not Updated";
								statusRemarks += "Vendor is empty ~";
							}else{
								vendor = new VendorMaster();
								vendor.setVendorId(-10);
							}
						}
						user.setVendor(vendor);
						
						cell = row.getCell(userAuthenticationTypeIndex);
						AuthenticationType authenticationType = null;
						if (isCellValid(cell)) {
							cell.setCellType(Cell.CELL_TYPE_STRING);		
							authenticationType = userListService.getAuthenticationTypeByName(cell.getStringCellValue());
						}
						if(authenticationType == null){
							authenticationType = new AuthenticationType();
							authenticationType.setAuthenticationTypeId(1);
						}
						user.setAuthenticationType(authenticationType);
						CommonActiveStatusMaster commonActiveStatusMaster = new CommonActiveStatusMaster();
						commonActiveStatusMaster.setStatus("ACTIVE");
						user.setCommonActiveStatusMaster(commonActiveStatusMaster);
						
						user.setUserPassword(encrypt(default_password));
						user.setStatus(1);				
						if(isValidRecord){
							users.add(user);
							
							validRecordCount++;
							if(rowNum > 0){
								isValidRowNum = rowNum;
								rowStatus = "Updated";
								statusRemarks = "Row uploaded successfully";
								invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);
								log.info("Row no :"+isValidRowNum+" Status :"+rowStatus+" Remarks :"+statusRemarks);
								}
						}else{
							invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);	
							log.info("Row no :"+isValidRowNum+" Status :"+rowStatus+" Remarks :"+statusRemarks);
						}					
						rowStatus = "";
						statusRemarks = "";
					}
					invalidRecordCount =	totalRecordCount-validRecordCount;				
				}// forloop //While loop
				
				for(Entry<Integer, String> parentMap : invalidRowMap.entrySet()){
					Row row = null;
		        	int invalidRowNumber = parentMap.getKey();
		        	String values = parentMap.getValue();	        	
	        		String status = values.split(";")[0];
	        		String remarks = values.split(";")[1];	        		
	        		for(int i=totalHeaderCells;i<totalHeaderCells+2;i++){
	        			row = sheet.getRow(invalidRowNumber);
	        			Cell cell = row.createCell(i);
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
					filePath = new File(reportFilePath+regUserReportFileName);
					FileOutputStream fos = new FileOutputStream(filePath);
					workbook.write(fos);
				}catch(Exception ex){
					log.error("Exception occured in writeUploadReportFile - ", ex);
				}
			} // If-else
				
			message = "<br> Number of Records Inserted are  :"+Integer.toString(validRecordCount) +"<br> Number  Records not Inserted are :"+Integer.toString(invalidRecordCount);
			usersMap.put("message", message);
			usersMap.put("users", users);
			usersMap.put("filePathAndName", reportFilePath+regUserReportFileName);
			log.info("Records Inserted are :"+validRecordCount+" Records NotInserted are :"+invalidRecordCount);
			
		} catch (IOException IOE) {
			log.error("Error reading Users from file", IOE);
			return null;
		} catch (Exception e) {
			log.error("Error reading Users from file", e);
			return null;
		}

		return usersMap;
	}

	@Override
	public boolean importTestCases(String excelDataFile, int productId,
			InputStream is) {
		return false;
	}
	
	
	public boolean copyInputStreamToFile( InputStream in, File file ) {
		
		boolean res=false;
	    try {
	    	int cnt=0;
	    	OutputStream out = new FileOutputStream(file);
	    	byte[] buffer = new byte[1024];
	    	int len = in.read(buffer);
	    	while (len != -1) {
	    		cnt++;
	        	if(cnt==1)
	        		res=true;
	    	    out.write(buffer, 0, len);
	    	    len = in.read(buffer);
	    	    if (Thread.interrupted()) {
	    	        throw new InterruptedException();
	    	    }
	    	}
	    } catch (Exception e) {
	       log.error("ERROR  ",e);
	        res=false;
	    }
	    return res;
	}
	
	public ImportFileValidationDTO readExcelFile(String filePath, String fileName,String uploadType, String userType){
		ImportFileValidationDTO impValidDTO = new ImportFileValidationDTO();
		try{
			FileInputStream fis = new FileInputStream(new File(fileName));
			Workbook wb = WorkbookFactory.create(fis); 
			Sheet sheet = wb.getSheetAt(0);	
			
			int numberOfRows = sheet.getPhysicalNumberOfRows();
			int numberOfCols = sheet.getRow(0).getPhysicalNumberOfCells();
					
			Row row = null;			
			Cell cell =null; 
			
			int percentCount=0;
			int mandatoryExcelColCount=0;
			int mandatoryExcelCellPos=0;
			float occPercent=0;

			boolean isValidEntry=true;
			boolean isRowEmpty=false;

			ArrayList<Integer> mandatoryColumns = new ArrayList<Integer>();
			HashMap<Integer,Boolean> validOrInvalidData = new HashMap<Integer,Boolean>();
			HashMap<Integer,Float> percentDataOccurance = new HashMap<Integer,Float>();
			
			for(int i = 0; i < sheet.getLastRowNum(); i++){
		        if(sheet.getRow(i) == null){
		           // condition of blank row so do whatever you want to do in this case
		           // for example I have done nothing just removed that row.
		            sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
		            i--;
		            continue;
		        }
		        for(int j = 0; j < sheet.getRow(i).getLastCellNum(); j++){
		            if(sheet.getRow(i).getCell(j) == null || sheet.getRow(i).getCell(j).toString().trim().equals("")){
		                isRowEmpty=true;
		            }else {
		                isRowEmpty=false;
		                break;
		            }
		        }
		        if(isRowEmpty==true){
		         // condition of blank row so do whatever you want to do in this case
		         // for example I have done nothing just removed that row.
		            sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
		            i--;
		        }
		    }
			numberOfRows = sheet.getPhysicalNumberOfRows();

			for(int r = 0; r < numberOfRows; r++) {
				isValidEntry=true;
				percentCount=0;
		        row = sheet.getRow(r);
		        if(row != null) {
		            for(int c = 0; c < numberOfCols; c++) {
		            	if(r==0){ 
		            		cell = row.getCell((short)c);
			                if(cell != null) {
			                	mandatoryExcelColCount += checkMandatoryColumnFieldNames(cell.getStringCellValue(), uploadType, userType);
			                	mandatoryExcelCellPos = checkMandatoryColumnCellNum(cell.getStringCellValue(), c, uploadType, userType);
			                	if(mandatoryExcelCellPos != -1)
			                		if(!mandatoryColumns.contains(mandatoryExcelCellPos))
			                			mandatoryColumns.add(mandatoryExcelCellPos);
			                }
			                
		            	}
		            	if(r>0){
		            		 row = sheet.getRow(r);
			                cell = row.getCell((short)c);
			                if(cell != null && cell.getCellType()!=3) {
			                    // Your code here
			                	percentCount+=1;			                	
			                }else{
			                	if(mandatoryColumns != null && mandatoryColumns.size() > 0 && mandatoryColumns.indexOf(c) >= 0){
			                		isValidEntry=false;
			                	}
			                }
			                
			                if(c == (numberOfCols-1)){
			                	occPercent=0;
			                	validOrInvalidData.put(r, isValidEntry);		                	
			                	occPercent=(((float)percentCount/(float)numberOfCols)*100);
			                	percentDataOccurance.put(r, occPercent);
			                }
			                
		            	}
		            }
		        }
		    }
			row=null;
			row=sheet.getRow(0);
			CellStyle style = wb.createCellStyle();
			CellStyle validStyle = wb.createCellStyle();
	        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
	        Font arialBoldFont = wb.createFont();
	        Font validFont = wb.createFont();
	        arialBoldFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	        arialBoldFont.setFontName("Arial");
	        arialBoldFont.setFontHeightInPoints((short) 10);
	        style.setFont(arialBoldFont);       
	        Cell addCell =null;
	        addCell=row.getCell(numberOfCols);
			if (addCell == null)
				addCell = row.createCell(numberOfCols);
			addCell.setCellType(Cell.CELL_TYPE_STRING);
			addCell.setCellValue("Is Valid Data");
			addCell.setCellStyle(style);  
			
			addCell=row.getCell(numberOfCols+1);
			if (addCell == null)
				addCell = row.createCell(numberOfCols+1);
			addCell.setCellType(Cell.CELL_TYPE_STRING);
			addCell.setCellValue("Percentage");
			addCell.setCellStyle(style);  
			int ct=0;int vCount=0;int completenessCount=0;int ivCount=0;
			try{
				
				
				Set<Integer> keyset = validOrInvalidData.keySet();
			    for (Integer key : keyset) {//
			    	row=sheet.getRow(key);
			    	Boolean isvalid = (Boolean)validOrInvalidData.get(key);
			    	float percentVal=(Float)percentDataOccurance.get(key);					    	
			    	addCell=row.createCell(numberOfCols);
	                if(isvalid){	
	                	vCount++;
	                	validFont.setColor(HSSFColor.GREEN.index);
	                	addCell.setCellValue(isvalid);
	                	addCell.setCellStyle(validStyle);
		                	completenessCount++;
		                	addCell = row.createCell(numberOfCols+1);
		                	addCell.setCellValue(percentVal+"%");
	                }else{	                	
	                	validFont.setColor(HSSFColor.RED.index);
	                	addCell.setCellValue(isvalid);
	                	addCell.setCellStyle(validStyle);
	                }
	                ct++;
			    }
			}catch(Exception e){
				log.error("ERROR  ",e);
			}
			int finalTotalRows=0;			
 			float completnessPercentage=0;
 			float overallCompleteLevel=0;
 			finalTotalRows=numberOfRows-1;
 			completnessPercentage=((float)completenessCount/(float)finalTotalRows)*100;
 			String tmp=completnessPercentage+"";
 			if(tmp!=null && !tmp.equals("") && tmp.indexOf(".")!=-1)
 				tmp=tmp.substring(0,tmp.indexOf(".")+2);
 			ivCount=finalTotalRows-vCount;
			overallCompleteLevel=((float)vCount/(float)finalTotalRows)*100;
		    try{//For creating new sheet for summary
		    	sheet = wb.createSheet("Validation Summary");
		    	 
		        Row header =null;Cell summaryCell =null;
		        header=	sheet.createRow(0);
		        summaryCell=header.createCell(0);
		        summaryCell.setCellValue("Total Records");
		        summaryCell.setCellStyle(style);
		        summaryCell=header.createCell(1);
		        summaryCell.setCellValue(finalTotalRows);
		        
		        header = sheet.createRow(1);
		        summaryCell=header.createCell(0);
		        summaryCell.setCellValue("Valid Records");
		        summaryCell.setCellStyle(style);
		        summaryCell=header.createCell(1);
		        summaryCell.setCellValue(vCount);
		        
		        header = sheet.createRow(2);
		        summaryCell=header.createCell(0);
		        summaryCell.setCellValue("In Valid Records");
		        summaryCell.setCellStyle(style);
		        summaryCell=header.createCell(1);
		        summaryCell.setCellValue(ivCount);
		        
		        header = sheet.createRow(3);
		        summaryCell=header.createCell(0);
		        summaryCell.setCellValue("Overall Completeness Level(%)");
		        summaryCell.setCellStyle(style);
		        summaryCell=header.createCell(1);
		        summaryCell.setCellValue(precision(2,overallCompleteLevel));
		        sheet.autoSizeColumn(0);
		    	/*Total Records finalTotalRows
		        Valid Records vCount
		        In Valid Records ivCount*/
		    	
		    }catch(Exception e){
		    	log.error("ERROR  ",e);
		    }
		 // Write the output to a file
 			FileOutputStream fileOut = new FileOutputStream(fileName);
 			wb.write(fileOut);
 			fileOut.close();
 			fileOut.flush();			
 			int mandatoryColumnCount = 0;
 			if(ValidateFieldConstants.UPLOAD_REGULAR_USER.equalsIgnoreCase(userType)){
 				mandatoryColumnCount = ValidateFieldConstants.UPLOAD_REGULAR_USER_MAND_COL_SIZE;
 			}else if(ValidateFieldConstants.UPLOAD_CUSTOMER.equalsIgnoreCase(userType)){
 				mandatoryColumnCount = ValidateFieldConstants.UPLOAD_CUSTOMER_USER_MAND_COL_SIZE;
 			}else if(ValidateFieldConstants.UPLOAD_FLEX_USER.equalsIgnoreCase(userType)){
 				mandatoryColumnCount = ValidateFieldConstants.UPLOAD_FLEX_USER_MAND_COL_SIZE;
 			}else if(ValidateFieldConstants.UPLOAD_FLEX_REGULAR_USER.equalsIgnoreCase(userType)){
 				mandatoryColumnCount = ValidateFieldConstants.UPLOAD_FLEX_REGULAR_USER_MAND_COL_SIZE;
 			}else if(ValidateFieldConstants.UPLOAD_FLEX_CONTRACT_USER.equalsIgnoreCase(userType)){
 				mandatoryColumnCount = ValidateFieldConstants.UPLOAD_FLEX_CONTRACT_USER_MAND_COL_SIZE;
 			}else if(ValidateFieldConstants.UPLOAD_FLEX_FLEXI_USER.equalsIgnoreCase(userType)){
 				mandatoryColumnCount = ValidateFieldConstants.UPLOAD_FLEX_FLEXI_USER_MAND_COL_SIZE;
 			}else if(ValidateFieldConstants.UPLOAD_FLEX_CUSTOMER_USER.equalsIgnoreCase(userType)){
 				mandatoryColumnCount = ValidateFieldConstants.UPLOAD_FLEX_CUSTOMER_USER_MAND_COL_SIZE;
 			}
			
			impValidDTO.setFileType(uploadType);
			impValidDTO.setFinalTotalRows(finalTotalRows);
			impValidDTO.setValidCount(vCount);
			impValidDTO.setInvalidCount(ivCount);
			impValidDTO.setCompletnessPercentage(completnessPercentage);
			impValidDTO.setMandatoryExcelColCount(mandatoryExcelColCount);
			impValidDTO.setValidElementColSize(mandatoryColumnCount);
			
			
			
		}catch(Exception e){
			log.error("ERROR  ",e);
		}
		return impValidDTO;
	}
	public float precision(int decimalPlace, Float d) {

	    BigDecimal bd = new BigDecimal(Float.toString(d));
	    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
	    return bd.floatValue();
	  }
	
	public int getActualMandatoryFieldCount(String uploadType, String userType){
		int cnt=0;		
		switch (Integer.parseInt(uploadType)) {
		case 1:				
			if(ValidateFieldConstants.UPLOAD_REGULAR_USER.equalsIgnoreCase(userType)){
				cnt = ValidateFieldConstants.UPLOAD_REGULAR_USER_MAND_COL_SIZE;
 			}else if(ValidateFieldConstants.UPLOAD_CUSTOMER.equalsIgnoreCase(userType)){
 				cnt = ValidateFieldConstants.UPLOAD_CUSTOMER_USER_MAND_COL_SIZE;
 			}else if(ValidateFieldConstants.UPLOAD_FLEX_USER.equalsIgnoreCase(userType)){
 				cnt = ValidateFieldConstants.UPLOAD_FLEX_USER_MAND_COL_SIZE;
 			}else if(ValidateFieldConstants.UPLOAD_FLEX_REGULAR_USER.equalsIgnoreCase(userType)){
 				cnt = ValidateFieldConstants.UPLOAD_FLEX_REGULAR_USER_MAND_COL_SIZE;
 			}else if(ValidateFieldConstants.UPLOAD_FLEX_CONTRACT_USER.equalsIgnoreCase(userType)){
 				cnt = ValidateFieldConstants.UPLOAD_FLEX_CONTRACT_USER_MAND_COL_SIZE;
 			}else if(ValidateFieldConstants.UPLOAD_FLEX_FLEXI_USER.equalsIgnoreCase(userType)){
 				cnt = ValidateFieldConstants.UPLOAD_FLEX_FLEXI_USER_MAND_COL_SIZE;
 			}else if(ValidateFieldConstants.UPLOAD_FLEX_CUSTOMER_USER.equalsIgnoreCase(userType)){
 				cnt = ValidateFieldConstants.UPLOAD_FLEX_CUSTOMER_USER_MAND_COL_SIZE;
 			} //Code Components
			break;			
		default:
			cnt=0;
			break;
		}
		return cnt;
	}
	public int checkMandatoryColumnFieldNames(String excelColNames,String uploadType, String userType){
		int count=0;
		try{
			if(ValidateFieldConstants.UPLOAD_TYPE_USER_LIST.equals(uploadType)){//1
				if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_FIRST_NAME)) {
					count=1;
				} else if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_LAST_NAME)) {
					count=1;
				} else if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_EMAIL_ID)) {
					count=1;
				}else if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_USER_TYPE) && userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_FLEX_USER)) {
					count=1;
				}else if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_CUSTOMER) && userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_CUSTOMER_USER)) {
					count=1;
				}else if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_USER_ROLE)) {
					count=1;
				}else if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_RESOURCE_POOL) && !userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_REGULAR_USER) && !userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_CUSTOMER_USER)) {
					count=1;
				}else if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_VENDOR) && !userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_REGULAR_USER) && !userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_CUSTOMER_USER)) {
					count=1;
				}
			}			
		}catch(Exception e){
			log.error("ERROR  ",e);
		}
		return count;
	}
	
	public int checkMandatoryColumnCellNum(String excelColNames,int cellNo,String uploadType, String userType){
		int count=-1;
		try{
			if(ValidateFieldConstants.UPLOAD_TYPE_USER_LIST.equals(uploadType)){//1
				if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_FIRST_NAME)) {
					count=cellNo;
				}  else if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_LAST_NAME)) {
					count=cellNo;
				} else if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_EMAIL_ID)) {
					count=cellNo;
				}else if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_USER_TYPE) && userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_FLEX_USER)) {
					count=cellNo;
				}else if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_CUSTOMER) && userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_CUSTOMER_USER)) {
					count=cellNo;
				}else if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_USER_ROLE)) {
					count=cellNo;
				}else if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_RESOURCE_POOL) && !userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_REGULAR_USER) && !userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_CUSTOMER_USER)) {
					count=cellNo;
				}else if (excelColNames!=null && excelColNames.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_VENDOR) && !userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_REGULAR_USER) && !userType.equalsIgnoreCase(ValidateFieldConstants.UPLOAD_CUSTOMER_USER)) {
					count=cellNo;
				}
			}
		}catch(Exception e){
			log.error("ERROR  ",e);
		}
		return count;
	}
	
	private String encrypt(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
           log.error("ERROR  ",e);
        }
        
        return generatedPassword;
    }
	
	
	@Override
	public String importResourceDemandWeekly(HttpServletRequest request, String excelDataFile, InputStream is, Integer workPackageId, Integer shiftId,String demandUploadPermission) {
		
		String addStatus = " ";
		String filePathName = "";
		
		HashMap<String,Object> statusPolicyUserRole = readResourceDemandFromExcelFile(request,excelDataFile, is, workPackageId, shiftId,demandUploadPermission);
		addStatus =	(String) statusPolicyUserRole.get("message");
		filePathName = (String) statusPolicyUserRole.get("filePathAndName");
		if(null != filePathName && "" != filePathName){
			addStatus += ";"+filePathName;
		}
		log.info("Excel File Name "+filePathName);
		log.info("End Time: "+Calendar.getInstance().getTime());
		return addStatus;		
		
	}
	
	
	
	@SuppressWarnings("deprecation")
	private HashMap<String, Object> readResourceDemandFromExcelFile(HttpServletRequest request, String excelDataFile, InputStream is, Integer workPackageById, Integer shiftId,String demandUploadPermission) {
		
		int validRecordCount = 0;
		int invalidRecordCount = 0;	
		String message = " ";
		Boolean isValidRecord = true;
		
		HashMap<String, Object> resourceDemandResult = new HashMap<String, Object>();
		HashMap<String, List<WorkPackageDemandProjection>> resourceDemanded = new HashMap<String, List<WorkPackageDemandProjection>>();
		HashMap<String, Skill> skillDemand = new HashMap<String, Skill>();
		HashMap<String, WorkPackage> workpackageDemand = new HashMap<String, WorkPackage>();
		HashMap<String, UserRoleMaster> roleDemand = new HashMap<String, UserRoleMaster>();
		HashMap<String, UserTypeMasterNew> typeDemand = new HashMap<String, UserTypeMasterNew>();
		HashMap<String, TestFactory> tFactoryMap = new HashMap<String, TestFactory>();
		
		HashMap<Integer, WorkShiftMaster> wrkShiftMap = new HashMap<Integer, WorkShiftMaster>();
		
		WorkPackage workPackage = workPackageService.getWorkPackageById(workPackageById);
		WorkShiftMaster workShiftMaster = testFactoryManagementService.getWorkShiftsByshiftId(shiftId);
		UserList userList = (UserList)request.getSession().getAttribute("USER");
		
		TestFactory tFactory =	new TestFactory();
				
		
		log.info("Import - Excel file from location:" + excelDataFile);		
		int numberOfSheets = 0;
		List<String> headers = new ArrayList<String>();
		
		String currentDate = DateUtility.dateToStringInSecond(new Date());
		currentDate = currentDate.replaceAll(":", "_");
		String demandReportFileName = "DemandReport"+currentDate+".xlsx";
		int isValidRowNum = 0;
		String rowStatus = "";
		String statusRemarks = "";
		//Store invalid row data into a map for data parsing
		Map<Integer,String> invalidRowMap = new HashMap<Integer,String>();
		List<Map<String, String>> xlCombList = new ArrayList<Map<String, String>>();
		HashMap<String, String> xlCombMap = null;
		
		Set<Integer> unMapWorkpackageIds= new HashSet<Integer>();
		Set<Integer> mappedWorkpackageList= new HashSet<Integer>();
		
		try { // 1

			Workbook workbook = null;
			if (excelDataFile.endsWith(".xls")) {
				workbook = new HSSFWorkbook(is);
			}
			if (excelDataFile.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(is);
			}


			numberOfSheets = workbook.getNumberOfSheets();
			if (numberOfSheets <= 0) {
				log.info("The Excel for Demand import does not contain any worksheets");
				//return false;
			}
			Sheet sheet = null;
			for (int i = 0; i < numberOfSheets; i++) {
				if (workbook.getSheetName(i).equalsIgnoreCase("Current-Sheet")) {
					sheet = workbook.getSheetAt(i);					
					break;
				}
			}
			if (sheet == null) {
				resourceDemandResult.put("message", "Sheet Not Available");
				return  resourceDemandResult;

			}
			
			Row row;
			int rowCount = sheet.getLastRowNum();
			int colCount = 0;
			int colNum = 0;

			int totalRecordCount = 0;//sheet.getLastRowNum();

			if (rowCount < 1) {
				log.info("No rows present in the Demand worksheet");
			} else { // 2
				DataFormatter dataFormatter = new DataFormatter();
				SimpleDateFormat demandDateFormat = new SimpleDateFormat("dd-MMM-yy");
				String cellValue = "";
				String demandKey = "";
				Float demand = 0.0F;
				Date demandDate = null;
				Integer demandWeek = 0;
				List<WorkPackageDemandProjection> workPackageDemandProjections = new ArrayList<WorkPackageDemandProjection>();
				List<WorkPackageDemandProjection> workPackageDemandProjectionProcess = new ArrayList<WorkPackageDemandProjection>();
				int rowNum;
				boolean isDataAvailable = false;							

				userRoleProperty=userRoleProperty.toLowerCase();
				List<String> userRoleListProperty = Arrays.asList(userRoleProperty.split(","));
				HashMap<String, String> userRoleMap = new HashMap<String, String>();
				for(String userRole:userRoleListProperty){
					String splitType=userRole.split("~")[0];
					String splitRole = userRole.split("~")[1];
					userRoleMap.put(splitType, splitRole);
				}

				userTypeProperty=userTypeProperty.toLowerCase();
				List<String> userTypeListProperty = Arrays.asList(userTypeProperty.split(","));
				HashMap<String, String> userTypeMap = new HashMap<String, String>();
				for(String userType:userTypeListProperty){
					String splitType=userType.split("~")[0];
					String mappedType = userType.split("~")[1];
					userTypeMap.put(splitType, mappedType);
				}
				
				//Header row - Row Number is 1
				Row headerRow = sheet.getRow(1);
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
	    			Cell cell = headerRow.createCell(i);
	    			cell.setCellStyle(css);
	    			if(i==totalHeaderCells){    				
	    				cell.setCellValue("Status");    				
	    			} else if(i==totalHeaderCells+1){   				
	    				cell.setCellValue("Remarks");    				
	    			}
	    		}
				
				
				for (rowNum = 1; rowNum <= rowCount; rowNum++) {
					isDataAvailable = false;
					row = sheet.getRow(rowNum);
					if (row == null) {
						isValidRowNum = rowNum;
						rowStatus = "NotUpdated";
						statusRemarks = "Row is empty";
						invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);
						continue;
					}

					if(rowNum == 1){
						colCount = row.getPhysicalNumberOfCells();
						for(colNum = 0; colNum < colCount; colNum++){
							if(row.getCell(colNum) != null){
								String value = row.getCell(colNum).getStringCellValue();
								if(value != null && value.toLowerCase().startsWith("jan")){
									break;
								}else{
									headers.add(value);
								}
							}

						}
						isDataAvailable = true;
					}else{
						cellValue = "";
						demandKey = "";
						colCount = headers.size();

						Cell cell = row.getCell(3);
						String testFactoryName = "";
						String workPackageName = "";

						if(cell != null ){
							cellValue = cell.getStringCellValue();
							if(cellValue != null && !cellValue.trim().isEmpty()){
								cellValue = cellValue.trim();
								workPackageName = cellValue;
							}
						}
						if(workPackageName == null || workPackageName.trim().isEmpty()){
							log.info("WorkPackage name is not available in row - " + rowNum);
							isValidRecord = false;
							isValidRowNum = rowNum;
							rowStatus = "NotUpdated";
							statusRemarks = "In this row Program is empty";
							invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);							
							continue;
						}
						
						cell = row.getCell(4);
						if(cell != null ){
							cellValue = cell.getStringCellValue();
							if(cellValue != null && !cellValue.trim().isEmpty()){
								cellValue = cellValue.trim();
								testFactoryName = cellValue;
							}
						}
						if(testFactoryName == null || testFactoryName.trim().isEmpty()){
							log.info("Testfactory name is not available in row - " + rowNum);
							isValidRecord = false;
							isValidRowNum = rowNum;
							rowStatus = "NotUpdated";
							statusRemarks = "In this row Major Competency is empty";
							invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);
							continue;
						}
						
						demandKey = workPackageName+"-"+testFactoryName;
						
						
						
						cell = row.getCell(5);
						String product = null;
						UserRoleMaster userRoleMaster = null;
						UserTypeMasterNew userTypeMasterNew = null;
						if(cell != null ){
							cellValue = cell.getStringCellValue();
							if(cellValue != null && !cellValue.trim().isEmpty()){
								cellValue = cellValue.trim();
								demandKey += "-" + cellValue;
								product = cellValue;
								isDataAvailable = true;
							}
						}
						if(cellValue == null){
							log.info("Product name is not available in row - " + rowNum + " / Given skill ( "+cellValue+" ) is not available in database");
							isValidRecord = false;
							isValidRowNum = rowNum;
							rowStatus = "NotUpdated";
							statusRemarks = "In this row Sub Competency is empty";
							invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);
							continue;
						}
						
						if(workpackageDemand.containsKey(demandKey)){
							workPackage = workpackageDemand.get(demandKey);
						}else{
							workPackage = getWorkPackageByProductAndTestFactory(workPackageName+"~"+testFactoryName+"~"+product);
							if(workPackage != null){
								workpackageDemand.put(demandKey, workPackage);
							}
						}

						if(workPackage == null){
							log.info("Given product and testfactory ( "+product+" ~ "+testFactoryName+" )  is not available in database / No workpackage available for the same.");
							isValidRecord = false;
							isValidRowNum = rowNum;
							rowStatus = "NotUpdated";
							statusRemarks = "There is no such combination of Program, Major competency and Sub competency ( "+workPackageName+"~"+testFactoryName+"~"+product+" )";
							invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);
							continue;
						}						

						
						
						Skill skill = null;
						skill = skillService.getSkillByName("NA");
						
						if(tFactoryMap.containsKey(testFactoryName)){
							tFactory = tFactoryMap.get(testFactoryName);
						}else{
							tFactory = testFactoryManagementService.getTestFactoryByName(testFactoryName);
						}
						
						if(wrkShiftMap.containsKey(tFactory.getTestFactoryId())){
							workShiftMaster = wrkShiftMap.get(tFactory.getTestFactoryId());
						}else{
							List<WorkShiftMaster> workShiftMasters = workShiftMasterDAO.listWorkShiftsByTestFactoryId(tFactory.getTestFactoryId());
							if(workShiftMasters != null && workShiftMasters.size() > 0){
								workShiftMaster = workShiftMasters.get(0);
							}else{
								log.info("Tesfactory : "+testFactoryName +" has no Shifts  in row - " + rowNum);
								message = "   TestFactory :  ("+testFactoryName+ ") Shifts not Available";
								isValidRowNum = rowNum;
								rowStatus = "NotUpdated";
								statusRemarks = "In this row Major Competency : "+testFactoryName +" has no Shifts";
								invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);
								continue;
							}
						}
						
						
						
						String role = "";
						String type = "";
						cell = row.getCell(7);
						if(cell != null){
							cellValue = cell.getStringCellValue();
							if(cellValue != null && !cellValue.trim().isEmpty()){
								cellValue = cellValue.trim().replaceAll("[\n\r]", "");
								role = userRoleMap.get(cellValue.toLowerCase());
								type = userTypeMap.get(cellValue.toLowerCase());

								if(role != null && !role.trim().isEmpty()){
									demandKey += "-" + role;
									if(roleDemand.containsKey(role)){
										userRoleMaster = roleDemand.get(role);
									}else{
										userRoleMaster = userListService.getRoleByLabel(role);
										roleDemand.put(role, userRoleMaster);
									}
								}

								if(type != null && !type.trim().isEmpty()){
									demandKey += "-" + type;
									if(typeDemand.containsKey(type)){
										userTypeMasterNew = typeDemand.get(type);
									}else{
										userTypeMasterNew = userListService.getUserTypeNewByLabel(type);
										typeDemand.put(type, userTypeMasterNew);
									}
								}
								isDataAvailable = true;
							}

						}
						if(userRoleMaster == null){
							log.info("Role name is not available in row - " + rowNum + " / Given role ( "+role+" ) is not available in database");
							isValidRecord = false;
							isValidRowNum = rowNum;
							rowStatus = "NotUpdated";
							statusRemarks = "In this row Role or UserType is not available ("+cellValue+")";
							invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);
							continue;
						}

						if(userTypeMasterNew == null){
							log.info("User type name is not available in row - " + rowNum + " / Given user type ( "+type+" ) is not available in database");
							isValidRecord = false;
							isValidRowNum = rowNum;
							rowStatus = "NotUpdated";							
							statusRemarks = "In this row Role or UserType is not available ("+cellValue+")";
							invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);
							continue;
						}
						
						String temptDemandKey = demandKey;
						for(colNum = 12; colNum < colCount; colNum++){
							demandKey = "";
							demand = 0.0F;
							demandWeek = 0;
							demandDate = null;
							cell = row.getCell(colNum);
							if(cell != null){
								cellValue = dataFormatter.formatCellValue(cell);
								demandDate = demandDateFormat.parse(headers.get(colNum));
								if(demandDate != null){
									demandWeek = DateUtility.getWeekNumberOfDateOnAYear(demandDate);
								}
								Integer currentWeek = DateUtility.getWeekNumberOfDateOnAYear(new Date());
								if(demandWeek == null || demandWeek == 0){
									log.info("Could not able to identify week number for this date - "+headers.get(colNum));
									isValidRowNum = rowNum;
									rowStatus = "NotUpdated";
									statusRemarks = "In this row week number for this date is not available";
									invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);
									continue;
								}else if(demandUploadPermission.equalsIgnoreCase(IDPAConstants.DEMAND_UPLOAD_PERMISSION_PARTIALLY) && demandWeek < currentWeek ){
									log.info("Option to updating data for past weeks disabled");
									isValidRowNum = rowNum;
									rowStatus = "NotUpdated";
									statusRemarks = "In this row Demand week is older than Current week";
									invalidRowMap.put(isValidRowNum, rowStatus+";"+statusRemarks);
									continue;
								}else{
									demandKey = temptDemandKey+"-"+demandWeek;
								}
								if(cellValue != null && !cellValue.trim().isEmpty()){
									isDataAvailable = true;
									demand = Float.parseFloat(cellValue);
								}

								if(demand != 0){
									if(resourceDemanded.containsKey(demandKey)){
										workPackageDemandProjectionProcess = resourceDemanded.get(demandKey);
										if(workPackageDemandProjectionProcess != null && workPackageDemandProjectionProcess.size() > 0){
											for(WorkPackageDemandProjection workPackageDemandProjection : workPackageDemandProjectionProcess){
												workPackageDemandProjection.setResourceCount(workPackageDemandProjection.getResourceCount() + demand);
											}
										}
									}else{

										Calendar calendar = Calendar.getInstance();
										calendar.setTime(demandDate);
										
										//log.info(workPackage+" - "+workShiftMaster+" - "+skill+" - "+userRoleMaster+" - "+userTypeMasterNew+" - "+demandWeek+" - "+calendar.get(Calendar.YEAR)+" - "+demand+userList);
										workPackageDemandProjectionProcess = getWorkPackageResourceDemands(workPackage, workShiftMaster, skill, userRoleMaster, userTypeMasterNew, demandWeek, calendar.get(Calendar.YEAR), demand, userList);
										if(workPackageDemandProjectionProcess != null && workPackageDemandProjectionProcess.size() > 0){
											workPackageDemandProjections.addAll(workPackageDemandProjectionProcess);
											resourceDemanded.put(demandKey, workPackageDemandProjectionProcess);
										}
									}
								}
							}
						}
						
						xlCombMap = new HashMap<String, String>();
						xlCombMap.put("wpName", workPackageName);
						xlCombMap.put("prodName", product);
						xlCombMap.put("testFactName", testFactoryName);
						xlCombList.add(xlCombMap);
						validRecordCount++;

					}			
					
					if(!isDataAvailable){
						break;
					}else{
						totalRecordCount++;
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
	        			Cell cell = row.createCell(i);
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
					filePath = new File(reportFilePath+demandReportFileName);
					FileOutputStream fos = new FileOutputStream(filePath);
					workbook.write(fos);
				}catch(Exception ex){
					log.error("Exception occured in writeUploadReportFile - ", ex);
				}
				
				if(workPackageDemandProjections != null && workPackageDemandProjections.size() > 0){
					for(WorkPackageDemandProjection workPackageDemandProjection : workPackageDemandProjections){
						workPackageService.addWorkPackageDemandProjection(workPackageDemandProjection);	
						/* Mongo Db adding */
						if(workPackageDemandProjection != null){
							mongoDBService.addResourceDemandToMongoDB(workPackageDemandProjection);
						}
						
						
						
						/*  Audit History */
						String	remarks = "WP:"+workPackageDemandProjection.getWorkPackage().getName()+"  WK:"+workPackageDemandProjection.getWorkWeek()+"  Shift:"+workPackageDemandProjection.getWorkShiftMaster().getShiftName()
								+"  Role: "+workPackageDemandProjection.getUserRole().getRoleLabel()+" Skill:"+workPackageDemandProjection.getSkill().getSkillName()+"  DemandedCount: "+workPackageDemandProjection.getResourceCount();
						eventService.addNewEntityEvent(IDPAConstants.ENTITY_RESOURCE_DEMAND, workPackageDemandProjection.getWpDemandProjectionId(), remarks, userList);
					}
				}
				
				rowCount=rowCount - 1;
				invalidRecordCount = rowCount - validRecordCount;
				
				if(!(xlCombList == null && xlCombList.isEmpty())){
					List<Object[]> dbCombList = resourceManagementService.getWorkpackageAndProductAndTestFactoryCombinations();
					for(Iterator<Object[]> iterator = dbCombList.iterator(); iterator.hasNext();){
						Object[] value = iterator.next();
						for(Map<String, String> xlMap : xlCombList){
							if(!mapsAreEqual(value,xlMap)){
								unMapWorkpackageIds.add((Integer)value[3]);
							}else {
								mappedWorkpackageList.add((Integer)value[3]);
								break;
							}
						}
					}
				}
				Integer currentWeekNum = DateUtility.getWeekNumberOfDateOnAYear(new Date());
				
				if(unMapWorkpackageIds != null && unMapWorkpackageIds.size() >0){
					unMapWorkpackageIds.removeAll(mappedWorkpackageList);
					String wpIds="";
					for(Integer wpId:unMapWorkpackageIds) {
						wpIds+=wpId+",";
					}
					if(wpIds.endsWith(",")) {
						wpIds=wpIds.substring(0, wpIds.length()-1);
					}
					for(int i=currentWeekNum;i<54;i++){
						Integer updatedNonMatchedDemandCount = resourceManagementService.updateDemandProjectionByNonMatchedCombList(currentWeekNum,wpIds);						
					}
				}
				
				if(!workPackageDemandProjections.isEmpty()){
					Comments commentsObj = new Comments();
					commentsObj.setComments(excelDataFile);
					EntityMaster entityMaster=workPackageService.getEntityMasterById(IDPAConstants.RESOURCEDEMAND_ENTITY_MASTER_ID);	
					
					commentsObj.setEntityMaster(entityMaster);
					commentsObj.setCreatedBy(userList);
					commentsObj.setCreatedDate(DateUtility.getCurrentTime());
					commentsObj.setResult(Integer.toString(validRecordCount)+","+Integer.toString(invalidRecordCount));
					commonService.addComments(commentsObj);
					
				}
				
				/* removing header row  count */
				
				message = message + " Number of Records Inserted are  :"+Integer.toString(validRecordCount) +"   Number Records not Inserted are :"+Integer.toString(invalidRecordCount);
				resourceDemandResult.put("message", message);
				resourceDemandResult.put("filePathAndName", reportFilePath+demandReportFileName);
				log.info(message + " Number of Records Inserted are  :"+Integer.toString(validRecordCount) +"   Number Records not Inserted are :"+Integer.toString(invalidRecordCount));
				log.info("Non uploaded Records are updated to: "+demandReportFileName+" sheet in the location: "+reportFilePath);
			}		
		} catch (Exception e) {
			log.error("Error occured in readResourceDemandFromExcelFile - ", e);
		//	return false;
		}
		return resourceDemandResult;
	}
	
	private List<WorkPackageDemandProjection> getWorkPackageResourceDemands(WorkPackage workPackage, WorkShiftMaster shift, Skill skill, UserRoleMaster role, UserTypeMasterNew type, Integer demandWeek, Integer demandYear, Float demand, UserList user){
		List<WorkPackageDemandProjection> workPackageDemandProjections = resourceManagementService.getWorkpackageDemandProjection(workPackage.getWorkPackageId(), shift.getShiftId(), skill.getSkillId(), role.getUserRoleId(), demandWeek, demandYear, type.getUserTypeId());
		if(workPackageDemandProjections != null && workPackageDemandProjections.size() > 0){
			for(WorkPackageDemandProjection workPackageDemandProjection : workPackageDemandProjections){
				workPackageDemandProjection.setResourceCount(demand);
			}
		}else{
			workPackageDemandProjections = new ArrayList<WorkPackageDemandProjection>();
			Date startDate = DateUtility.getWeekStartDateFromJanMonday(demandWeek, demandYear);
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(startDate);
			
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(startDate);
			endCalendar.add(Calendar.DAY_OF_MONTH, 4);
			endCalendar.set(Calendar.HOUR_OF_DAY, 23);
			endCalendar.set(Calendar.MINUTE, 59);
			endCalendar.set(Calendar.SECOND, 59);
			
			Date endDate = endCalendar.getTime();
			
			while(startDate.before(endDate)){
				WorkPackageDemandProjection workPackageDemandProjection = new WorkPackageDemandProjection();
				workPackageDemandProjection.setSkill(skill);
				workPackageDemandProjection.setUserRole(role);
				workPackageDemandProjection.setUserTypeMasterNew(type);
				workPackageDemandProjection.setWorkWeek(demandWeek);
				workPackageDemandProjection.setWorkYear(demandYear);
				workPackageDemandProjection.setResourceCount(demand);
				workPackageDemandProjection.setWorkDate(startDate);
				workPackageDemandProjection.setDemandMode("Weekly");
				
				workPackageDemandProjection.setWorkPackage(workPackage);
				workPackageDemandProjection.setWorkShiftMaster(shift);
				workPackageDemandProjection.setDemandRaisedOn(new Date());
				workPackageDemandProjection.setDemandRaisedByUser(user);
				workPackageDemandProjection.setGroupDemandId(System.currentTimeMillis());
				
				workPackageDemandProjections.add(workPackageDemandProjection);
				
				startCalendar.add(Calendar.DAY_OF_MONTH, 1);
				startDate = startCalendar.getTime();
				
			}
		}
		
		return workPackageDemandProjections;
	}
	
	
	public WorkPackage getWorkPackageByProductAndTestFactory(String wpNameTestFactoryAndProductName){
			WorkPackage workPackage = null;
		try{
			log.info("wpNameTestFactoryAndProductName -----------  "+wpNameTestFactoryAndProductName);
			List<String>productTF = Arrays.asList(wpNameTestFactoryAndProductName.split("~"));
			String product = "";
			String testFactory = "";
			String workPackageName = "";
			
			workPackageName=productTF.get(0);
			testFactory = productTF.get(1);
			product = productTF.get(2);
				
		TestFactory tFactory =	testFactoryManagementService.getTestFactoryByName(testFactory);
		ProductMaster pMaster = null;
		if(tFactory != null){
			pMaster = productListService.getProductByNameAndTestfactoryId(product,tFactory.getTestFactoryId());
		}
		if(pMaster != null){
			workPackage = workPackageService.getLatestWorkPackageByproductId(pMaster.getProductId(),workPackageName);
		}
			
		}catch(Exception ex){
			log.error("Unable to Get WP ", ex);
		}
		
		return workPackage;
		
	}
	
	public boolean mapsAreEqual(Object[] objA, Map<String,String> mapB){
		
		if(objA!=null){
				
				String wpName=(String)objA[0];
				String productName=(String)objA[1];
				String testFactoryName=(String)objA[2];				
				if(wpName.equals(mapB.get("wpName")) && productName.equals(mapB.get("prodName")) && testFactoryName.equals(mapB.get("testFactName"))){
					return true;
				} else {
					return false;
				}				
		}
			return false;		
	 }
}