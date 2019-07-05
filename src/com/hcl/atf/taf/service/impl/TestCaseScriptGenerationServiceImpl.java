package com.hcl.atf.taf.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.AOTCConstants;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.ZipTool;
import com.hcl.atf.taf.dao.AttachmentDAO;
import com.hcl.atf.taf.dao.LanguagesDAO;
import com.hcl.atf.taf.dao.ScriptTypeMasterDAO;
import com.hcl.atf.taf.dao.TestCaseAutomationScriptDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.TestCaseStoryDAO;
import com.hcl.atf.taf.dao.TestCategoryMasterDAO;
import com.hcl.atf.taf.integration.data.TestDataIntegrator;
import com.hcl.atf.taf.model.AmdocsPageMethods;
import com.hcl.atf.taf.model.AmdocsPageObjects;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.BDDKeywordsPhrases;
import com.hcl.atf.taf.model.KeywordLibrary;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.SCMSystem;
import com.hcl.atf.taf.model.ScriptGenerationDetails;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCaseAutomationStory;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseScript;
import com.hcl.atf.taf.model.TestCaseScriptHasTestCase;
import com.hcl.atf.taf.model.TestCaseScriptVersion;
//import com.hcl.atf.etb.model.ProjectCode;
import com.hcl.atf.taf.model.TestDataItemValues;
import com.hcl.atf.taf.model.TestDataItems;
import com.hcl.atf.taf.model.TestDataPlan;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestStoryGeneratedScripts;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestToolMaster;
//import com.hcl.atf.etb.model.ProjectCode;
import com.hcl.atf.taf.model.UIObjectItems;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonAutomationScriptsVersion;
import com.hcl.atf.taf.model.json.JsonTestCaseAutomationScript;
import com.hcl.atf.taf.model.json.JsonTestCaseAutomationScriptFileContent;
import com.hcl.atf.taf.model.json.JsonTestCaseAutomationScripts;
import com.hcl.atf.taf.model.json.JsonTestCaseAutomationStory;
import com.hcl.atf.taf.model.json.JsonTestCaseScript;
import com.hcl.atf.taf.model.json.JsonTestStoryGeneratedScripts;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.objectrepository.Element;
import com.hcl.atf.taf.objectrepository.ObjectRepositoryManagerFactory;
import com.hcl.atf.taf.objectrepository.TAFObjectRepositoryManager;
import com.hcl.atf.taf.scriptgeneration.ScriptGeneratorUtilities;
import com.hcl.atf.taf.scriptgeneration.TestCaseRefScriptGenerator;
import com.hcl.atf.taf.scriptgeneration.TestCaseSkeletonScriptGenerator;
import com.hcl.atf.taf.service.DefectManagementService;
import com.hcl.atf.taf.service.PasswordEncryptionService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestCaseScriptGenerationService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.testdata.TAFTestDataManager;
import com.hcl.atf.taf.testdata.TestDataItem;
import com.hcl.atf.taf.testdata.TestDataManagerFactory;
import com.hcl.connector.ConnectorCredentials;
import com.hcl.taf.git.util.GitConnector;

@Service
public class TestCaseScriptGenerationServiceImpl implements TestCaseScriptGenerationService {	 

	private static final Log log = LogFactory.getLog(TestCaseScriptGenerationServiceImpl.class);

	@Autowired
	private ProductListService productService;
	@Autowired
	private TestSuiteConfigurationService testSuiteService;
	@Autowired
	private TestCaseAutomationScriptDAO testCaseAutomationScriptDAO;
	@Autowired
	private TestCaseRefScriptGenerator testCaseRefScriptGenerator;
	@Autowired
	private TestCaseSkeletonScriptGenerator testCaseSkeletonScriptGenerator;
	@Autowired
	private ScriptTypeMasterDAO scriptTypeMasterDAO;
	@Autowired
	private TestCategoryMasterDAO testCategoryMasterDAO;
	@Autowired
	private TestCaseListDAO testCaseListDAO;

	@Autowired
	private AttachmentDAO attachmentDAO;

	@Autowired
	private MongoDBService mongoDBService;

	@Autowired
	private LanguagesDAO languagesDAO;

	@Autowired
	private TestCaseStoryDAO testCaseStoryDAO;

	@Autowired
	private TestCaseScriptGenerationService testCaseScriptGenerationService;
	
	@Autowired
	private TestCaseService testCaseService;
	
	@Autowired
	DefectManagementService defectManagementService;
	
	@Autowired
	PasswordEncryptionService passwordEncryptionService;

	@Value("#{ilcmProps['GENERATED_TEST_SCRIPTS_DESTINATION_FOLDER']}")
	private String testScriptsDestinationDirectory;

	@Value("#{ilcmProps['PRODUCT_OBJECT_REPOSITORY_TEST_DATA']}")
	private String objectRepositoryFolder;

	@Autowired
	private HttpServletRequest request;	
	@Autowired
	private TestDataIntegrator testDataIntegrator;

	// ObjectRepository
	public static final int startRow = 0;
	public static final int elementName = 0;
	public static final int description = 1;
	public static final int id = 2;
	public static final int label = 3;
	public static final int chromeXpath = 4;
	public static final int firefoxXpath = 5;
	public static final int ieXpath = 6;
	public static final int safariXpath = 7;
	public static final int chromeCssLocator = 8;
	public static final int firefoxCssLocator = 9;
	public static final int ieCssLocator = 10;
	public static final int safariCssLocator = 11;
	public static final int zone = 12;
	public static final int index = 13;
	static final String DATA_ADD = "ADD";
	private static final String DATA_UPDATE = "UPDATE";
	private static final String DATA_ERROR = "ERROR";

	// Test Data
	public static final int tesData = 0;
	public static final int testDataValue = 1;
	public static final int testDataType = 2;
	public static final int testDataCounter= 3;
	public static final int testDataRemarks = 4;

	@Override
	@Transactional
	public String generateProductTestCaseScripts(Integer id, String scriptsFor, String packageName, String className, String destinationDirectory, int nameSource, String scriptType, String executionEngine, boolean generateTestCaseRef, boolean generateSkeletonScripts, String testCaseOption) {

		try {
			if (id == null) {
				log.info("Test Case Ref Source Code Generation : Unable to generate source code as id is null");
				return "Failed : Test Case Ref Source Code Generation : Unable to generate source code as id is null";
			}

			ProductMaster product = null;
			TestSuiteList testSuite = null;
			TestCaseList testCase = null;
			if (scriptsFor.equalsIgnoreCase("Product")) {
				testCase = testSuiteService.getByTestCaseIdBare(id);
				product = productService.getProductById(id);
			} else if (scriptsFor.equalsIgnoreCase("TestSuite")) {
				testSuite = testSuiteService.getByTestSuiteId(id);
				product = testSuite.getProductMaster();
			} else if (scriptsFor.equalsIgnoreCase("TestCase")) {
				testCase = testSuiteService.getByTestCaseIdBare(id);
				product = testCase.getProductMaster();
			}

			String mainPackageName = "com.hcl.atf.taf";
			String testCasePackageName = null;
			if (packageName == null) {
				testCasePackageName = "com.hcl.atf.taf.testcase";
			} else {
				testCasePackageName = packageName;
			}
			if (className == null)
				className = "TestCasesReference";
			if (destinationDirectory == null) {
				if(testCase!=null){
					destinationDirectory = testScriptsDestinationDirectory+File.separator+"P" + testCase.getTestCaseId() + "-" + System.currentTimeMillis();
				} else if(product !=null){
					destinationDirectory = testScriptsDestinationDirectory+File.separator+"P" + product.getProductId() + "-" + System.currentTimeMillis();
				}
			}

			String message = "";
			log.info("Destination Directory : " + destinationDirectory);
			//Get all the test cases specified. If a test suite is specified, get only those test cases.
			List<TestCaseList> allTestCases = new ArrayList<TestCaseList>();
			Map<Integer, TestCaseList> testCaseOrder = new TreeMap<Integer, TestCaseList>();
			if (scriptsFor.equalsIgnoreCase("Product")) {
				allTestCases.addAll(product.getTestCaseLists());						
				for(int i=0;i<allTestCases.size();i++){
					TestCaseList testCaseList = allTestCases.get(i);
					testCaseOrder.put(testCaseList.getTestCaseId(), testCaseList);
				}
				if(testCaseOrder.size()>0){
					allTestCases.clear();
					allTestCases = new ArrayList<TestCaseList>(testCaseOrder.values());		
				}
			} else if (scriptsFor.equalsIgnoreCase("TestSuite")) {
				allTestCases.addAll(testSuite.getTestCaseLists());
				for(int i=0;i<allTestCases.size();i++){
					TestCaseList testCaseList = allTestCases.get(i);
					testCaseOrder.put(testCaseList.getTestCaseId(), testCaseList);
				}
				if(testCaseOrder.size()>0){
					allTestCases.clear();
					allTestCases = new ArrayList<TestCaseList>(testCaseOrder.values());		
				}
			} else if (scriptsFor.equalsIgnoreCase("TestCase")) {
				allTestCases.add(testCase);
			}
			log.info("Total test Cases for the " + scriptsFor +  " : id " + id + " : " + allTestCases.size());
			if (allTestCases.isEmpty()) {
				return "Failed : No Testcases are available for " + scriptsFor;
			}

			if (generateTestCaseRef) {
				message = message + testCaseRefScriptGenerator.generateTestCaseRefSourceCode(allTestCases, product, testCasePackageName, className, destinationDirectory, nameSource, scriptType, executionEngine);
				if (message.startsWith("Failed")) 
					return message;	
			}
			if (generateSkeletonScripts) {
				message =  testCaseSkeletonScriptGenerator.generateTestCasesSkeletonCode(allTestCases, product, testCasePackageName, className, destinationDirectory, nameSource, scriptType, executionEngine, testCaseOption);
				if (message.startsWith("Failed")) 
					return message;	
			}
			boolean generateMainClass = true;
			if (generateMainClass) {
				message =  testCaseSkeletonScriptGenerator.generateMainClass(allTestCases, product, mainPackageName, className, destinationDirectory, nameSource, scriptType, executionEngine);
				if (message.startsWith("Failed")) 
					return message;	

			}

			String zipFilePath = ZipTool.dozip(destinationDirectory);
			if (ZipTool.isValidZipArchive(zipFilePath)) {
				log.info("Zip file created" + zipFilePath);
			} else {
				return "Failed : Unable to create a Zip File for Automation Scripts";
			}

			return zipFilePath;
		} catch (Exception e) {
			log.error("Unable to generate test automation scripts for some reason", e);
			return "Failed : Unable to generate test scripts due to some reason";
		}
	}


	@Override
	@Transactional
	public JsonTestCaseScript getTestCaseScriptForViewing(Integer testCaseId, String scriptsFor, String packageName, String className, String destinationDirectory, int nameSource, String scriptType, String executionEngine, boolean generateTestCaseRef, boolean generateSkeletonScripts, String testCaseOption) {

		try {
			if (testCaseId == null) {
				log.info("Test Case Ref Source Code Generation : Unable to generate source code as testcase id is null");
				return null;
			}

			ProductMaster product = null;
			TestCaseList testCase = null;

			testCase = testSuiteService.getByTestCaseIdBare(testCaseId);
			product = testCase.getProductMaster();

			String mainPackageName = "com.hcl.atf.taf";
			String testCasePackageName = null;
			if (!(testCase.getTestCaseScriptQualifiedName() == null || testCase.getTestCaseScriptQualifiedName().trim().isEmpty())) {
				testCasePackageName = testCase.getTestCaseScriptQualifiedName();
			} else if (packageName == null) {
				testCasePackageName = "com.hcl.atf.taf.testcase";
			} else {
				testCasePackageName = packageName;
			}
			if (className == null)
				className = "TestCasesReference";
			if (destinationDirectory == null) {
				destinationDirectory = testScriptsDestinationDirectory+File.separator+"P" + product.getProductId() + "-" + System.currentTimeMillis();
			}

			String message = "";
			String testCaseScriptPath = "";
			String mainClassScriptPath = "";
			log.info("Destination Directory : " + destinationDirectory);
			//Get all the test cases specified. If a test suite is specified, get only those test cases.
			List<TestCaseList> allTestCases = new ArrayList<TestCaseList>();
			allTestCases.add(testCase);
			if (allTestCases.isEmpty()) {
				return null;
			}

			if (generateSkeletonScripts) {
				testCaseScriptPath =  testCaseSkeletonScriptGenerator.generateTestCasesSkeletonCode(allTestCases, product, testCasePackageName, className, destinationDirectory, nameSource, scriptType, executionEngine, testCaseOption);
				if (testCaseScriptPath.startsWith("Failed")) 
					return null;
				mainClassScriptPath = testCaseSkeletonScriptGenerator.generateMainClass(allTestCases, product, mainPackageName, className, destinationDirectory, nameSource, scriptType, executionEngine);
				if (mainClassScriptPath.startsWith("Failed")) 
					return null;
			}

			String scriptFilePath = message;
			log.info("Testcase Script File Path : " + testCaseScriptPath);
			log.info("Main class Script File Path : " + mainClassScriptPath);

			JsonTestCaseScript testScript = new JsonTestCaseScript(testCase);
			testScript.setScriptType(scriptType);
			testScript.setTestEngine(executionEngine);
			if (!testCaseScriptPath.startsWith("Failed")) { 

				File scriptFile = new File(testCaseScriptPath);
				String script = ScriptGeneratorUtilities.readFile(testCaseScriptPath, null);
				testScript.setTestCaseScript(script.toString());
				testScript.setTestCaseClassName(ScriptGeneratorUtilities.getTestCaseClassName(testCase.getTestCaseName(),  testCase.getTestCaseId(), testCase.getTestCaseCode(), nameSource) + ".java");
			} else {
				testScript.setTestCaseScript("Unable to generate Testcase script template");
			}

			if (!mainClassScriptPath.startsWith("Failed")) { 

				File scriptFile = new File(mainClassScriptPath);
				String script = ScriptGeneratorUtilities.readFile(mainClassScriptPath, null);
				testScript.setMainClassScript(script.toString());
				testScript.setMainClassName("Main.java");
			} else {
				testScript.setTestCaseScript("Unable to generate Main class script template");
			}
			return testScript;
		} catch (Exception e) {
			log.error("Unable to generate test automation script for viewing for some reason", e);
			return null;
		}
	}

	@Override
	@Transactional
	public JsonTestCaseScript getTestSuiteRefScriptForViewing(Integer id, String scriptsFor, String packageName, String className, String destinationDirectory, int nameSource, String scriptType, String executionEngine, boolean generateTestCaseRef, boolean generateSkeletonScripts, String testCaseOption) {

		try {
			if (id == null) {
				log.info("Test Case Ref Source Code Generation : Unable to generate source code as id is null");
				return null;
			}
			String catalinaHome = System.getProperty("catalina.home");
			ProductMaster product = null;
			TestSuiteList testSuite = null;
			TestCaseList testCase = null;
			if (scriptsFor.equalsIgnoreCase("Product")) {
				product = productService.getProductById(id);
			} else if (scriptsFor.equalsIgnoreCase("TestSuite")) {
				testSuite = testSuiteService.getByTestSuiteId(id);
				product = testSuite.getProductMaster();
			} else if (scriptsFor.equalsIgnoreCase("TestCase")) {
				testCase = testSuiteService.getByTestCaseIdBare(id);
				product = testCase.getProductMaster();
			}

			String mainPackageName = "com.hcl.atf.taf";
			String testCasePackageName = null;
			if (packageName == null) {
				testCasePackageName = "com.hcl.atf.taf.testcase";
			} else {
				testCasePackageName = packageName;
			}
			String testCasesRefClassName = "TestCasesReference";
			String mainClassName = "Main";
			if (destinationDirectory == null) {
				destinationDirectory =catalinaHome+File.separator+testScriptsDestinationDirectory+File.separator+"BoilerPlateCode"+File.separator+"P" + product.getProductId() + "-" + System.currentTimeMillis();
			}

			String message = "";
			log.info("Destination Directory : " + destinationDirectory);
			//Get all the test cases specified. If a test suite is specified, get only those test cases.
			List<TestCaseList> allTestCases = new ArrayList<TestCaseList>();
			if (scriptsFor.equalsIgnoreCase("Product")) {
				allTestCases.addAll(product.getTestCaseLists());	
			} else if (scriptsFor.equalsIgnoreCase("TestSuite")) {
				allTestCases.addAll(testSuite.getTestCaseLists());
			} else if (scriptsFor.equalsIgnoreCase("TestCase")) {
				allTestCases.add(testCase);
			}
			log.info("Total test Cases for the " + scriptsFor +  " : id " + id + " : " + allTestCases.size());
			if (allTestCases.isEmpty()) {
				log.info("No Testcases are available for " + scriptsFor);
				return null;
			}

			JsonTestCaseScript testScript = new JsonTestCaseScript();
			testScript.setScriptType(scriptType);
			testScript.setTestEngine(executionEngine);
			testScript.setDownloadPath(destinationDirectory);
			String testCaseRefScriptPath = "";
			String mainClassScriptPath = "";

			//Generate TestCaseRef class
			if (true) {
				testCaseRefScriptPath = testCaseRefScriptGenerator.generateTestCaseRefSourceCode(allTestCases, product, testCasePackageName, testCasesRefClassName, destinationDirectory, nameSource, scriptType, executionEngine);
				testScript.setTestCaseRefClassName(testCasesRefClassName);
				if (testCaseRefScriptPath.startsWith("Failed")) 
					log.info("Unable to generate TestCaseRef script : ");
				else {
					String script = ScriptGeneratorUtilities.readFile(testCaseRefScriptPath, null);
					log.info("Testcase ref Script : " + script);
					testScript.setTestCaseRefClassScript(script);
					testScript.setTestCaseRefClassName(testCasesRefClassName);
				}
			}

			//Generate main class
			boolean generateMainClass = true;
			if (generateMainClass) {
				message =  testCaseSkeletonScriptGenerator.generateMainClass(allTestCases, product, mainPackageName, mainClassName, destinationDirectory, nameSource, scriptType, executionEngine);
				if (message.startsWith("Failed")) {
					testScript.setMainClassScript("Unable to generate Main class script template");
					testScript.setMainClassName("Main.java");
					testScript.setMainClassScript("Unable to generate Main class script template");
				} else {

					String script = ScriptGeneratorUtilities.readFile(message, null);
					testScript.setMainClassScript(script.toString());
					testScript.setMainClassName("Main.java");
				}
			}
			String zipFilePath = ZipTool.dozip(destinationDirectory);
			if (ZipTool.isValidZipArchive(zipFilePath)) {
				log.info("Zip file created" + zipFilePath);
			}
			return testScript;
		} catch (Exception e) {
			log.error("Unable to generate test automation script for viewing for some reason", e);
			return null;
		}
	}

	@Override
	@Transactional
	public String addTestCaseAutomationScript(JsonTestCaseAutomationScript jsonTestCaseAutomationScript) {
		Integer maxVersionId = 1;
		if (jsonTestCaseAutomationScript == null) {
			return null;
		}

		TestCaseAutomationStory testCaseAutomationScriptFromUI = jsonTestCaseAutomationScript.getTestCaseAutomationStory();
		//Check if this already exists for the same language and same test tool
		maxVersionId = testCaseAutomationScriptDAO.getMaxVersionIdByTestCaseId(jsonTestCaseAutomationScript.getTestCaseId());
		TestCaseAutomationStory testCaseAutomationScriptInDB = testCaseAutomationScriptDAO.getTestAutomationScript(jsonTestCaseAutomationScript.getTestCaseId(), jsonTestCaseAutomationScript.getScriptType(), jsonTestCaseAutomationScript.getTestTool(), maxVersionId);

		if (testCaseAutomationScriptInDB == null) {

			testCaseAutomationScriptDAO.addTestCaseAutomationScript(testCaseAutomationScriptFromUI);
			return "SUCCESS";
		} else {

			return "FAILURE : Automation Script for the Language and Test Engine already exists";
		}
	}

	private JsonTestCaseScript loadKeywords(JsonTestCaseScript jsonTestScript, Attachment objectRepAttach, Attachment testDataAttach, String productTypeName, String testEngine) {
		String fileName ="";
		try
		{

			if (jsonTestScript == null)
				return jsonTestScript;

			List<String> keywords = new ArrayList<String>();
			keywords= testCaseAutomationScriptDAO.getKeywordPhrases("",testEngine); // product type , test tool 
			if(keywords == null ){
				keywords = new ArrayList<String>();
			}
			String objectRespositoryUrl = null;
			String testDataUrl = null;
			if(objectRepAttach != null){
				objectRespositoryUrl = objectRepAttach.getAttributeFileURI();
			}
			if(testDataAttach != null){
				testDataUrl = testDataAttach.getAttributeFileURI();
			}

			//Add Product UI Objects

			log.info("loadKeywords function. Object repository url received: " + objectRespositoryUrl);


			if (objectRespositoryUrl != null && (new File(objectRespositoryUrl)).exists()) { 
				fileName= objectRepAttach.getAttachmentPrefixName();
				log.info("loadKeywords function. Object repository exists");
				Map <String, String> objectSourceProperties = new HashMap<String, String>();
				objectSourceProperties.put(ObjectRepositoryManagerFactory.PROPERTIES_FILENAME_KEY, objectRespositoryUrl);
				objectSourceProperties.put(ObjectRepositoryManagerFactory.PROPERTIES_WORKSHEET_KEY, "Object-Repository");
				objectSourceProperties.put(ObjectRepositoryManagerFactory.PROPERTIES_APPLICATION_TYPE_KEY, "Web");
				objectSourceProperties.put(ObjectRepositoryManagerFactory.PROPERTIES_BROWSER_KEY, "Chrome");

				TAFObjectRepositoryManager tafObjectRespositoryManager = ObjectRepositoryManagerFactory.getObjectRepositoryManager(ObjectRepositoryManagerFactory.DATA_SOURCE_EXCEL, objectSourceProperties);
				log.info("Call returned from getObjectRepositoryManager");

				if(tafObjectRespositoryManager != null)
				{
					Set<String> elements = tafObjectRespositoryManager.getElementNames(fileName);
					if (elements != null) {
						log.info("Elements size: " + elements.size());
						for (String element : elements) {
							log.info("Element : " + element);
							keywords.add(fileName+"." + element);
						}
					}
					else
					{
						log.info("loadKeywords function. Object repository dataItems don't exist");
					}
				}
				else
				{
					log.info("TAFObjectRepositoryManager is null");
				}
			}
			else
			{
				log.info("loadKeywords function. Object repository does not exist");
			}

			log.info("loadKeywords function. Test Data url received: " + testDataUrl);

			//Add Product test data elements
			if (testDataUrl != null && (new File(testDataUrl)).exists()) 
			{ 
				fileName=testDataAttach.getAttachmentPrefixName();
				log.info("loadKeywords function. Test Data exists");

				Map <String, String> dataSourceProperties = new HashMap<String, String>();
				dataSourceProperties.put(TestDataManagerFactory.DATA_PROPERTIES_FILENAME_KEY, testDataUrl);
				dataSourceProperties.put(TestDataManagerFactory.DATA_PROPERTIES_WORKSHEET_KEY, "Test-Data");

				TAFTestDataManager tafTestDataManager = TestDataManagerFactory.getTestDataManager(TestDataManagerFactory.DATA_SOURCE_EXCEL, dataSourceProperties);

				if(tafTestDataManager != null)
				{
					Set<String> dataItems = tafTestDataManager.getDataItemNames(fileName);
					if (dataItems != null) {
						log.info("dataItems : " + dataItems.size());
						for (String dataItem : dataItems) {
							log.info("dataItem : " + dataItem);
							keywords.add(fileName+"." + dataItem);
						}
					}
					else
					{
						log.info("loadKeywords function. TestData dataItems don't exist");
					}
				}
				else
				{
					log.info("TAFTestDataManager is null");
				}
			}
			else
			{
				log.info("loadKeywords function. Test Data does not exist");
			}


			jsonTestScript.setKeywords(keywords);

		}
		catch(Exception ex)
		{
			log.error("loadKeywords function. Exception occured. " + ex.getMessage());
		}
		return jsonTestScript;
	}

	@Override
	@Transactional
	public List<JsonTestCaseAutomationScript> listTestCaseAutomationScripts(Integer testCaseId) {

		if (testCaseId == null) {
			log.info("Testcase Id is not specified. Scripts cannot be sent");
			return null;
		}
		List<JsonTestCaseAutomationScript> jsonScripts = new ArrayList<JsonTestCaseAutomationScript>();
		try {
			List<TestCaseAutomationStory> storys = testCaseAutomationScriptDAO.listTestAutomationScripts(testCaseId);
			if (storys != null) {
				JsonTestCaseAutomationScript jsonScript = null;
				for (TestCaseAutomationStory story : storys) {
					jsonScript = new JsonTestCaseAutomationScript(story);
					jsonScripts.add(jsonScript);
				}
				return jsonScripts;
			} else {
				log.info("Unable to get Automation Scripts List for Testcase : " + testCaseId);
				return null;
			}
		} catch (Exception e) {
			log.error("Unable to get Automation Scripts List for Testcase : " + testCaseId, e);
			return null;
		}
	}

	@Override
	@Transactional
	public String generateBDDTestCaseAutomationScriptFiles(Integer id, String scriptsFor, String scriptType, String testEngine, String destinationDirectory) {

		try {

			if (!scriptType.equals("GHERKIN")) {
				log.info("Only BDD Stories download supported currently");
				return "Failed : Only BDD Stories download supported currently";
			}

			if (id == null) {
				log.info("BDD Stories : Unable to download stories as id is null");
				return "Failed : BDD Stories : Unable to download stories as id is null";
			}

			ProductMaster product = null;
			TestSuiteList testSuite = null;
			TestCaseList testCase = null;
			if (scriptsFor.equalsIgnoreCase("Product")) {
				product = productService.getProductById(id);
			} else if (scriptsFor.equalsIgnoreCase("TestSuite")) {
				testSuite = testSuiteService.getByTestSuiteId(id);
				product = testSuite.getProductMaster();
			} else if (scriptsFor.equalsIgnoreCase("TestCase")) {
				testCase = testSuiteService.getByTestCaseIdBare(id);
				product = testCase.getProductMaster();
			}

			String message = "";

			//Get all the test cases specified. If a test suite is specified, get only those test cases.
			List<TestCaseList> allTestCases = new ArrayList<TestCaseList>();
			if (scriptsFor.equalsIgnoreCase("Product")) {
				allTestCases.addAll(product.getTestCaseLists());	
			} else if (scriptsFor.equalsIgnoreCase("TestSuite")) {
				allTestCases.addAll(testSuite.getTestCaseLists());
			} else if (scriptsFor.equalsIgnoreCase("TestCase")) {
				allTestCases.add(testCase);
			}
			log.info("Total test Cases for the " + scriptsFor +  " : id " + id + " : " + allTestCases.size());
			if (allTestCases.isEmpty()) {
				return "Failed : No Testcases are available for " + scriptsFor;
			}

			Set<TestCaseList> allTestCasesSet = new HashSet<TestCaseList>(allTestCases);
			return generateBDDTestCaseAutomationScriptFiles(allTestCasesSet, scriptType, testEngine, destinationDirectory);

		} catch (Exception e) {
			log.error("Unable to generate test automation scripts for some reason", e);
			return "Failed : Unable to generate test scripts due to some reason";
		}
	}

	@Override
	@Transactional
	public String generateBDDTestCaseAutomationScriptFiles(Set<TestCaseList> testCases, String scriptType, String testEngine, String destinationDirectory) {

		return generateBDDTestCaseAutomationScriptFiles(testCases, scriptType, testEngine, destinationDirectory, null);
	}

	@SuppressWarnings("unused")
	@Override
	@Transactional
	public String generateBDDTestCaseAutomationScriptFiles(Set<TestCaseList> testCases, String scriptType, String testEngine, String destinationDirectory, TestRunPlan testRunPlan) {
		try {
			Integer maxVersionId = 1;
			Integer productId = null;
			String fileName = null;
			StringBuffer sbOutputPackageAndClass = new StringBuffer();
			List<UIObjectItems> uiObjectItemList= new ArrayList<UIObjectItems>();
			String exportLocation = System.getProperty("catalina.home")+File.separator;
			List<TestDataItemValues> testDataItemValuesList = new ArrayList<TestDataItemValues>();
			List<TestDataItems> testDataItemsList = new ArrayList<TestDataItems>();
			UserList user = null;
			try{
				user = (UserList)request.getSession().getAttribute("USER");
			}catch(Exception e){
				log.error("Error in getting user from request ");
			}
			
			Integer languageId = null;
			Path FROM;
			
			String templatesFolderPath = System.getProperty("catalina.home") + File.separator + "webapps" + File.separator + "TestScripts"; 
			if (testCases == null|| testCases.isEmpty()) {
				return "Failed : No Testcases specified";
			}
			if (destinationDirectory == null || destinationDirectory.trim().isEmpty()) {
				log.info("Destination Directory : " + destinationDirectory);
			}
			//Write all Scripts to a temp folder for downloading
			for (TestCaseList testCaseObject : testCases) {								
				//Get the latest version of BDD Automation Story from DB
				maxVersionId = testCaseAutomationScriptDAO.getMaxVersionIdByTestCaseId(testCaseObject.getTestCaseId());
				if(testCaseObject.getProductMaster().getProductId() != null)
				productId = testCaseObject.getProductMaster().getProductId();
				if(maxVersionId != null){
					TestCaseAutomationStory story = testCaseAutomationScriptDAO.getTestAutomationScript(testCaseObject.getTestCaseId(), scriptType, testEngine,maxVersionId);
					testEngine = story.getTestTool().getTestToolName();
					//Write the Automation script content to a file
					if (story != null) {
						//Enhancement to attach generated scripts to the story bundle at server side only if the script is generated
						
						File destinationDir = new File(destinationDirectory);						
						if (!destinationDir.exists()) {
							destinationDir.mkdirs();		
						}

						JsonTestStoryGeneratedScripts testStoryGeneratedScript = testCaseScriptGenerationService.getGeneratedScriptById(story.getTestCaseAutomationStoryId());
						if(testStoryGeneratedScript == null){

						}
						if(testStoryGeneratedScript != null && testStoryGeneratedScript.getTestScript() != null && testStoryGeneratedScript.getOutputPackage() != null){
							languageId = testStoryGeneratedScript.getLanguageId();
							String generatedScriptFileName = generateScriptFile(testStoryGeneratedScript, destinationDirectory, story.getName());
							if(testStoryGeneratedScript.getOutputPackage() != null){
								sbOutputPackageAndClass.append(testStoryGeneratedScript.getOutputPackage()+"."+generatedScriptFileName);
								sbOutputPackageAndClass.append(System.lineSeparator());
							}
							log.info("Wrote Generated Script to destination : " + generatedScriptFileName);
						}
					} else {
						log.info("No Generated script for test case : " + testCaseObject.getTestCaseId());
					}
				}else{
					log.info("No Story is available for test case : " + testCaseObject.getTestCaseId());
				}
			}

			if(testRunPlan != null){
				List<Attachment> attachmentsForTestRunPlan = attachmentDAO.listDataRepositoryAttachments(testRunPlan.getTestRunPlanId());
				if(attachmentsForTestRunPlan!=null && attachmentsForTestRunPlan.size() > 0){
					for(Attachment attachment : attachmentsForTestRunPlan){
						log.info("Repository File Type : "+attachment.getAttachmentType()+" & Repository File name : "+attachment.getAttributeFileName()+" & Repository File URI : "+attachment.getAttributeFileURI());
						if(attachment != null && !attachment.getAttachmentType().equalsIgnoreCase("ObjectRepository") && !attachment.getAttachmentType().equalsIgnoreCase("TestData") && !attachment.getAttributeFileExtension().equalsIgnoreCase(".xlsx")){
							String attachmentFileUriName = attachment.getAttributeFileURI();
							if(attachmentFileUriName != null && attachmentFileUriName != ""){
								File repositoryFile = new File(attachmentFileUriName);
								if (repositoryFile.exists()) {								
									FROM = Paths.get(attachmentFileUriName);
									Path TO = Paths.get(destinationDirectory);			
									Files.copy(FROM, TO.resolve(FROM.getFileName()), StandardCopyOption.REPLACE_EXISTING);	
									if(attachment.getAttachmentType().equalsIgnoreCase(AOTCConstants.TDTYPE_SEETEST_REPOSITORY)){
										log.debug("Copied Seetest Repository File to bundle");
									}
								} else {
									if(attachment.getAttachmentType().equalsIgnoreCase(AOTCConstants.TDTYPE_SEETEST_REPOSITORY)){
										log.debug(" Seetest Repository File does not exist");
									}								
								}
							}
						}
					}
				}
				if(productId != null){
					Integer userId = 1;
					if(user != null && user.getUserId() != null)
						userId = user.getUserId();
					String loginId = "admin";
					if(user != null && user.getLoginId() != null)
						loginId = user.getLoginId();
					
					fileName = "uiObject.xlsx";
					Path TO = Paths.get(destinationDirectory, new String[0]);
					String  objectRepositoryFile = exportLocation+"webapps"+File.separator+"EXPORT"+File.separator+ "UIOBJECTS"+File.separator+loginId+File.separator+productId;					
					uiObjectItemList = testCaseScriptGenerationService.listUIObjectItemsByProductId(productId,0,userId,null,null);
					if(uiObjectItemList != null && !uiObjectItemList.isEmpty() && uiObjectItemList.size() > 0){
						Boolean isexportComplete = testDataIntegrator.uiObjectsExport(uiObjectItemList, objectRepositoryFile,fileName);
						objectRepositoryFile = objectRepositoryFile + File.separator +fileName; 
						if(isexportComplete){
							FROM = Paths.get(objectRepositoryFile, new String[0]);
							Files.copy(FROM, TO.resolve(FROM.getFileName()), StandardCopyOption.REPLACE_EXISTING);
							log.info((Object)"Copied Object Repository File to bundle");

						}
					}
					fileName = "testData.xlsx";
					int testDataItemHeaderCount  =  testCaseScriptGenerationService.listTestDataItemValuesCountByProductIdAndUserId(productId,userId);
					String testDataFolderDirectory = exportLocation+"webapps"+File.separator+"EXPORT"+File.separator+ "TESTDATA"+File.separator+loginId+File.separator+productId;
					testDataItemsList = testCaseScriptGenerationService.listTestDataItemValuesByProductAndTestDataPlanAndHandle(productId,null,-1, userId);
					if(testDataItemsList != null && !testDataItemsList.isEmpty() && testDataItemsList.size() > 0){
						Boolean isTestDataexportComplete =  testDataIntegrator.testDataExport(testDataItemsList, testDataFolderDirectory,fileName, testDataItemHeaderCount);
						testDataFolderDirectory = testDataFolderDirectory + File.separator + fileName;
						if(isTestDataexportComplete){
							FROM = Paths.get(testDataFolderDirectory, new String[0]);
							Files.copy(FROM, TO.resolve(FROM.getFileName()), StandardCopyOption.REPLACE_EXISTING);
							log.info((Object)"Copied Test Data File to bundle");
						}
					}
				}
			}

			if(sbOutputPackageAndClass.toString() != null && !sbOutputPackageAndClass.toString().isEmpty())
				writepackageWithClassNamesToTextFile(destinationDirectory,sbOutputPackageAndClass.toString());
			String seleniumTemplateFolderPath = templatesFolderPath + File.separator + "SeleniumTemplate";
			String seetestTemplateFolderPath = templatesFolderPath +  File.separator + "SeeTestTemplate";
			String appiumTemplateFolderPath = templatesFolderPath + File.separator + "AppiumTemplate";
			String edatTemplateFolderPath = templatesFolderPath + File.separator + "EdatTemplate";
			String seleniumFolderPathForBDDExecution = destinationDirectory + File.separator + "SeleniumTemplate" ;
			String seetestFolderPathForBDDExecution = destinationDirectory + File.separator + "SeeTestTemplate" ;
			String appiumFolderPathForBDDExecution = destinationDirectory + File.separator + "AppiumTemplate" ;
			String eDATFolderPathForBDDExecution = destinationDirectory + File.separator + "EdatTemplate" ;
			if ((testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_SELENIUM) || testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_RESTASSURED))&& !new File(seleniumFolderPathForBDDExecution).exists()) {
				new File(seleniumFolderPathForBDDExecution).mkdirs();
			}
			else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_SEETEST) && !new File(seetestFolderPathForBDDExecution).exists()){
				new File(seetestFolderPathForBDDExecution).mkdirs();
			}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_APPIUM) && !new File(appiumTemplateFolderPath).exists()){
				new File(appiumFolderPathForBDDExecution).mkdirs();
			}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_EDAT) && !new File(edatTemplateFolderPath).exists()){
				new File(eDATFolderPathForBDDExecution).mkdirs();
			} else{

			}

			//copying testengine based template folder for bdd scriptless execution
			try {
				if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_SEETEST) && new File(seetestTemplateFolderPath).exists()){
					FileUtils.copyDirectory(new File(seetestTemplateFolderPath), new File(seetestFolderPathForBDDExecution));
				}
				else if((testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_SELENIUM) || testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_RESTASSURED)) && new File(seleniumTemplateFolderPath).exists()){
					FileUtils.copyDirectory(new File(seleniumTemplateFolderPath), new File(seleniumFolderPathForBDDExecution));
				}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_APPIUM) && new File(appiumTemplateFolderPath).exists()){
					FileUtils.copyDirectory(new File(appiumTemplateFolderPath), new File(appiumFolderPathForBDDExecution));
				}else if(testEngine.equalsIgnoreCase(AOTCConstants.TEST_TOOL_EDAT) && new File(edatTemplateFolderPath).exists()) {
					FileUtils.copyDirectory(new File(edatTemplateFolderPath), new File(eDATFolderPathForBDDExecution));
				} else {

				}
			} catch (IOException e) {
				log.error("Exception while copying test engine based template folder to " + destinationDirectory,e);
			}
			String zipFilePath = ZipTool.dozip(destinationDirectory);
			if (ZipTool.isValidZipArchive(zipFilePath)) {
				log.info("Zip file created" + zipFilePath);
			} else {
				return "Failed : Unable to create a Zip File for Automation Scripts";
			}
			return zipFilePath;

		} catch (Exception e) {
			log.error("Unable to generate test automation scripts for some reason", e);
			return "Failed : Unable to generate test scripts due to some reason";
		}
	}


	private String writeAutomationScriptToFile(TestCaseAutomationStory story, String destinationDirectory) {

		String scriptFileName = "";
		if (story.getScriptFileName() == null || story.getScriptFileName().trim().isEmpty()) {
			scriptFileName = ScriptGeneratorUtilities.getTestCaseClassName((String)story.getTestCase().getTestCaseName(), (int)0, (String)"", (int)0) + ".story";
		} else {
			scriptFileName = story.getScriptFileName();
		}
		String fullFileName = destinationDirectory + File.separator+ scriptFileName;
		try {

			File destinationDir = new File(destinationDirectory);						
			if (!destinationDir.exists()) {
				destinationDir.mkdirs();		
			}

			File scriptFile = new File(fullFileName);						
			if (!scriptFile.exists()) {				
				scriptFile.createNewFile();
			}

			FileWriter fw = new FileWriter(scriptFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			String userBDDScriptStory = story.getScript();
			String testCaseName = ScriptGeneratorUtilities.getTestCaseClassName(story.getTestCase().getTestCaseName(), (int)0, (String)"", (int)0); 

			String[] lines = userBDDScriptStory.split("\\\\n\\\\r|\\\\n|\\\\r");
			StringBuilder finalStringBuilder = new StringBuilder();
			for(String s:lines){
				if(!s.equals("")){
					if(s.trim().startsWith("Feature")){
						s = "";
						s = "Feature: " + testCaseName + " story" + System.lineSeparator() 
								+  "Meta: " + System.lineSeparator()
								+ "@storyname " + testCaseName + System.lineSeparator(); 
						if(story.getTestCase().getTestCaseDescription() != null ){
							s = s	+ "@storydesc " + story.getTestCase().getTestCaseDescription() + System.lineSeparator() ;	
						}else{
							s = s	+ "@storydesc " + "" + System.lineSeparator() ;
						}


					}

					if(s.trim().startsWith("Meta") || s.trim().startsWith("@storyname") || s.trim().startsWith("@storydesc") || s.trim().startsWith("@storytype")  ){
						s = "";
					}
					if(s != null && s != ""){
						finalStringBuilder.append(s.trim()).append(System.getProperty("line.separator"));
					}
				}
			}
			userBDDScriptStory = "";
			userBDDScriptStory = finalStringBuilder.toString();

			userBDDScriptStory = userBDDScriptStory.replaceAll("(\\\\n\\\\r|\\\\n|\\\\r)", System.lineSeparator());
			bw.write(userBDDScriptStory);			
			bw.close();
			return fullFileName;
		} catch (Exception e) {
			log.error("Unable to write Script to File : " + fullFileName, e );
			return null;
		}
	}


	@Override
	@Transactional
	public void addTestcaseautoscripts(JsonTestCaseAutomationScripts jsonTestAutomationScripts) {
		TestCaseScript testCaseautoscriptsFromUI = jsonTestAutomationScripts.getTestCaseScript();
		testCaseAutomationScriptDAO.addTestCaseAutomationScript(testCaseautoscriptsFromUI);
	}


	@Override
	@Transactional
	public List<TestCaseScript> getTestcaseAutoScripts(Integer testcaseId,Integer jtStartIndex,Integer jtPageSize) {
		return testCaseAutomationScriptDAO.getTestCaseAutoscripts(testcaseId,jtStartIndex,jtPageSize);
	}


	@Override
	@Transactional
	public List<TestCaseScriptVersion> addTestCaseAutoScriptsVersion(JsonAutomationScriptsVersion jsontcautomationscriptsversion, Integer testCaseId) {
		TestCaseScriptVersion testcasescriptversionFromUI = jsontcautomationscriptsversion.getTestCaseScriptVersion();
		List<TestCaseScriptVersion> tcscriptversionIssel = null;

		tcscriptversionIssel = testCaseAutomationScriptDAO.getTestCaseScriptVersionIsSelected(testCaseId);
		if(tcscriptversionIssel != null && tcscriptversionIssel.size()>0){
			TestCaseScriptVersion tcsel = tcscriptversionIssel.get(0);
			tcsel.setIsSelected(0);
			testCaseAutomationScriptDAO.addTestCaseAutomationScriptVersionDAO(tcsel);
		}
		testcasescriptversionFromUI.setStatus(1);
		testcasescriptversionFromUI.setIsSelected(1);		
		testCaseAutomationScriptDAO.addTestCaseAutomationScriptVersionDAO(testcasescriptversionFromUI);

		return tcscriptversionIssel;		
	}


	@Override
	@Transactional
	public List<TestCaseScriptVersion> getTestCaseAutoScriptsVersionList(Integer scriptId, Integer jtStartIndex, Integer jtPageSize) {
		return testCaseAutomationScriptDAO.getTestAutomationScriptversion(scriptId,jtStartIndex,jtPageSize);
	}

	@Override
	@Transactional
	public List<BDDKeywordsPhrases> getBDDKeywordsPhrasesList(String productType,String testTool,Integer jtStartIndex,Integer jtPageSize){

		return testCaseAutomationScriptDAO.getBDDKeywordsPhrases(productType,testTool,jtStartIndex,jtPageSize);

	}


	@Override
	@Transactional
	public void updateBDDKeyWordsPhrase(
			BDDKeywordsPhrases bddKeyWordsPhrasesFromUI) {
		testCaseAutomationScriptDAO.updateBDDKeyWordsPhrase(bddKeyWordsPhrasesFromUI);
	}


	@Override
	@Transactional
	public void addTestCaseAutoScriptsVersionStatus(
			JsonAutomationScriptsVersion jsontcautomationscriptsversion,
			Integer scriptVersionId, Integer status) {
		TestCaseScriptVersion testcasescriptversionFromUI = jsontcautomationscriptsversion.getTestCaseScriptVersion();
		List<TestCaseScriptVersion> tcscriptversionStatus = null;

		tcscriptversionStatus = testCaseAutomationScriptDAO.getTestCaseScriptVersionStatus(scriptVersionId, status);		
		if(tcscriptversionStatus!=null && tcscriptversionStatus.size()>0){
			TestCaseScriptVersion tcstatus = tcscriptversionStatus.get(0);
			tcstatus.setStatus(0);
			testCaseAutomationScriptDAO.addTestCaseAutomationScriptVersionDAO(tcstatus);
		}
		testcasescriptversionFromUI.setStatus(1);
		testCaseAutomationScriptDAO.addTestCaseAutomationScriptVersionDAO(testcasescriptversionFromUI);		
	}


	@Override
	@Transactional
	public Integer getBDDKeywordsPhrasesListSize(String productType,
			String testTool) {
		return testCaseAutomationScriptDAO.getBDDKeywordsPhrasesListSize(productType,testTool);
	}



	@Override
	@Transactional
	public List<JsonTestCaseAutomationScriptFileContent> readContentFromObjectRepositoryFile(String filePath) {
		int blankRowCount = 0;
		URL url = null;
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(filePath);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		JsonTestCaseAutomationScriptFileContent jsonTestCaseAutomationScriptFileContent = null;
		List<JsonTestCaseAutomationScriptFileContent> jsonTestCaseAutomationScriptFileContentList = new ArrayList<JsonTestCaseAutomationScriptFileContent>();
		int numberOfSheets = 0;
		try { // 1

			Workbook workbook = null;
			if (filePath.endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			}
			if (filePath.endsWith(".xlsx")) {
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
			if (rowCount < 3) {
				log.info("No rows present in the test cases worksheet");
				return null;
			} else { // 2
				for (rowNum = 3; rowNum < rowCount; rowNum++) {
					Row row;
					blankRowCount = 0;
					row = sheet.getRow(rowNum);
					if (row == null) {
						rowNum++;
						continue;
					}
					if (row.getRowNum() <= startRow) {
						colCount = row.getLastCellNum();
						continue;
					} else { // 4

						// Read the testcase info
						jsonTestCaseAutomationScriptFileContent = new JsonTestCaseAutomationScriptFileContent();


						// TestCase name is mandatory. If not present skip row
						Cell cell1 = row.getCell(elementName);
						if (isCellValid(cell1)) {
							cell1.setCellType(Cell.CELL_TYPE_STRING);
							jsonTestCaseAutomationScriptFileContent.setElementName(cell1.getStringCellValue());
						} 

						Cell cell2 = row.getCell(description);
						if (isCellValid(cell2)) {
							if(cell2.getCellType() == cell2.CELL_TYPE_STRING){
								cell2.setCellType(Cell.CELL_TYPE_STRING);
								jsonTestCaseAutomationScriptFileContent.setDescription(cell2.getStringCellValue());
							}
							else if(cell2.getCellType() == cell2.CELL_TYPE_NUMERIC){
								cell2.setCellType(Cell.CELL_TYPE_NUMERIC);
								int cel2 = (int)cell2.getNumericCellValue();							
								String strCel2 = String.valueOf(cel2);
								jsonTestCaseAutomationScriptFileContent.setDescription(strCel2);
							}

						} 

						// Mandatory info is present. Get the rest of the
						// testcase details
						Cell cell3 = row.getCell(id);
						if (isCellValid(cell3)) {
							cell3.setCellType(Cell.CELL_TYPE_STRING);
							if (cell3.getStringCellValue() != null
									&& cell3.getStringCellValue().length() > 2000) {
								jsonTestCaseAutomationScriptFileContent.setId((cell3
										.getStringCellValue()
										.substring(0, 2000)));
							} else {
								jsonTestCaseAutomationScriptFileContent.setId(cell3
										.getStringCellValue());
							}
						}
						jsonTestCaseAutomationScriptFileContentList.add(jsonTestCaseAutomationScriptFileContent);
					
					}

				}// forloop //While loop
			} // If-else
		} catch (IOException IOE) {
			log.error("Error reading testcases from file", IOE);
			IOE.printStackTrace();
			return null;
		} catch (Exception e) {
			log.error("Error reading testcases from file", e);
			e.printStackTrace();
			return null;
		}

		jsonTestCaseAutomationScriptFileContentList.get(0).toString();

		return jsonTestCaseAutomationScriptFileContentList;
	}



	@Override
	@Transactional
	public List<JsonTestCaseAutomationScriptFileContent> readContentFromTestDataFile(String filePath) {
		int blankRowCount = 0;
		URL url = null;
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(filePath);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		JsonTestCaseAutomationScriptFileContent jsonTestCaseAutomationScriptFileContent = null;
		List<JsonTestCaseAutomationScriptFileContent> jsonTestCaseAutomationScriptFileContentList = new ArrayList<JsonTestCaseAutomationScriptFileContent>();
		int numberOfSheets = 0;
		//			FileInputStream fis;
		try { // 1

			// Get the Excel file
			Workbook workbook = null;
			if (filePath.endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			}
			if (filePath.endsWith(".xlsx")) {
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
				for (rowNum = 1; rowNum < rowCount; rowNum++) {
					Row row;
					blankRowCount = 0;
					row = sheet.getRow(rowNum);
					if (row == null) {
						rowNum++;
						continue;
					}
					if (row.getRowNum() <= startRow) {
						colCount = row.getLastCellNum();
						continue;
					} else { // 4

						// Read the testcase info
						jsonTestCaseAutomationScriptFileContent = new JsonTestCaseAutomationScriptFileContent();
						// TestCase name is mandatory. If not present skip row
						Cell cell1 = row.getCell(tesData);
						if (isCellValid(cell1)) {
							cell1.setCellType(Cell.CELL_TYPE_STRING);
							jsonTestCaseAutomationScriptFileContent.setTestData(cell1.getStringCellValue());
						} 

						Cell cell2 = row.getCell(testDataValue);
						if (isCellValid(cell2)) {
							if(cell2.getCellType() == cell2.CELL_TYPE_STRING){
								cell2.setCellType(Cell.CELL_TYPE_STRING);
								jsonTestCaseAutomationScriptFileContent.setTestDataValue(cell2.getStringCellValue());
							}

						} 

						// Mandatory info is present. Get the rest of the
						// testcase details
						Cell cell3 = row.getCell(testDataType);
						if (isCellValid(cell3)) {
							cell3.setCellType(Cell.CELL_TYPE_STRING);
							jsonTestCaseAutomationScriptFileContent.setTestDataType(cell3.getStringCellValue());

						}

						Cell cell4 = row.getCell(testDataCounter);
						if (isCellValid(cell4)) {
							cell4.setCellType(Cell.CELL_TYPE_STRING);
							jsonTestCaseAutomationScriptFileContent.setTestDataCounter(cell4.getStringCellValue());

						}
						Cell cell5 = row.getCell(testDataRemarks);
						if (isCellValid(cell4)) {
							cell4.setCellType(Cell.CELL_TYPE_STRING);
							jsonTestCaseAutomationScriptFileContent.setTestDataRemarks(cell5.getStringCellValue());

						}
						jsonTestCaseAutomationScriptFileContentList.add(jsonTestCaseAutomationScriptFileContent);
					}

				}// forloop //While loop
			} // If-else
		} catch (IOException IOE) {
			log.error("Error reading testcases from file", IOE);
			IOE.printStackTrace();
			return null;
		} catch (Exception e) {
			log.error("Error reading testcases from file", e);
			e.printStackTrace();
			return null;
		}

		jsonTestCaseAutomationScriptFileContentList.get(0).toString();

		return jsonTestCaseAutomationScriptFileContentList;
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
	public List<JsonTestCaseAutomationScriptFileContent> loadAttachmentKeywords(Integer attachmentId) {
		List<JsonTestCaseAutomationScriptFileContent> jsonTestCaseScriptAttachmentFileContentList = new ArrayList<JsonTestCaseAutomationScriptFileContent>();
		try
		{
			String objectRespositoryUrl =null;
			String testDataUrl = null;
			String fileName ="";
			Attachment attach = attachmentDAO.getAttachmentById(attachmentId);
			if(attach != null){
				fileName = attach.getAttributeFileName().replace(".", "@").split("@")[0];
				if (attach.getAttachmentType().equalsIgnoreCase("ObjectRepository")) {
					objectRespositoryUrl = attach.getAttributeFileURI();

				}
				else if (attach.getAttachmentType().equalsIgnoreCase("TestData")) {
					testDataUrl = attach.getAttributeFileURI();
				}else{

				}

			}
			//Add Product UI Objects



			if (objectRespositoryUrl != null && (new File(objectRespositoryUrl)).exists()) { 

				log.info("loadKeywords function. Object repository exists");
				Map <String, String> objectSourceProperties = new HashMap<String, String>();
				objectSourceProperties.put(ObjectRepositoryManagerFactory.PROPERTIES_FILENAME_KEY, objectRespositoryUrl);
				objectSourceProperties.put(ObjectRepositoryManagerFactory.PROPERTIES_WORKSHEET_KEY, "Object-Repository");
				objectSourceProperties.put(ObjectRepositoryManagerFactory.PROPERTIES_APPLICATION_TYPE_KEY, "Web");
				objectSourceProperties.put(ObjectRepositoryManagerFactory.PROPERTIES_BROWSER_KEY, "Chrome");

				TAFObjectRepositoryManager tafObjectRespositoryManager = ObjectRepositoryManagerFactory.getObjectRepositoryManager(ObjectRepositoryManagerFactory.DATA_SOURCE_EXCEL, objectSourceProperties);

				log.info("Call returned from getObjectRepositoryManager");

				if(tafObjectRespositoryManager != null) {
					Set<String> elements = tafObjectRespositoryManager.getElementNames(fileName);
					if (elements != null) {
						log.info("Elements : " + elements.size());
						for (String element : elements) {
							log.info("Element : " + element);
							JsonTestCaseAutomationScriptFileContent jsonAttachmentFileContent = new JsonTestCaseAutomationScriptFileContent();
							jsonAttachmentFileContent.setElementName(element);
							jsonTestCaseScriptAttachmentFileContentList.add(jsonAttachmentFileContent);
						}
					}
					else
					{
						log.info("loadKeywords function. Object repository dataItems don't exist");
					}
				}
				else
				{
					log.info("TAFObjectRepositoryManager is null");
				}
			}
			else
			{
				log.info("loadKeywords function. Object repository does not exist");
			}

			log.info("loadKeywords function. Test Data url received: " + testDataUrl);

			//Add Product test data elements
			if (testDataUrl != null && (new File(testDataUrl)).exists()) 
			{ 

				log.info("loadKeywords function. Test Data exists");
				Map <String, String> dataSourceProperties = new HashMap<String, String>();
				dataSourceProperties.put(TestDataManagerFactory.DATA_PROPERTIES_FILENAME_KEY, testDataUrl);
				dataSourceProperties.put(TestDataManagerFactory.DATA_PROPERTIES_WORKSHEET_KEY, "Test-Data");

				TAFTestDataManager tafTestDataManager = TestDataManagerFactory.getTestDataManager(TestDataManagerFactory.DATA_SOURCE_EXCEL, dataSourceProperties);

				if(tafTestDataManager != null) {
					Set<String> dataItems = tafTestDataManager.getDataItemNames(fileName);
					if (dataItems != null) {
						log.info("dataItems : " + dataItems.size());
						for (String dataItem : dataItems) {
							log.info("dataItem : " + dataItem);
							JsonTestCaseAutomationScriptFileContent jsonAttachmentFileContent = new JsonTestCaseAutomationScriptFileContent();
							jsonAttachmentFileContent.setElementName(dataItem);
							jsonTestCaseScriptAttachmentFileContentList.add(jsonAttachmentFileContent);
						}
					}
					else
					{
						log.info("loadKeywords function. TestData dataItems don't exist");
					}
				}
				else
				{
					log.info("TAFTestDataManager is null");
				}
			}
			else
			{
				log.info("loadKeywords function. Test Data does not exist");
			}

		}
		catch(Exception ex)
		{
			log.error("loadKeywords function. Exception occured. " + ex.getMessage());
		}
		return jsonTestCaseScriptAttachmentFileContentList;
	}
	@Override
	public List<KeywordLibrary> listKeywordLibraryByKeywordId(
			Integer keywordId) {
		return  testCaseAutomationScriptDAO.listKeywordLibraryByKeywordId(keywordId);
	}


	@Override
	public Integer saveKeywordLibrary(KeywordLibrary keywordLibrary) {
		return  testCaseAutomationScriptDAO.saveKeywordLibrary(keywordLibrary);
	}


	@Override
	public void updateKeywordLibrary(KeywordLibrary keywordLibrary) {
		testCaseAutomationScriptDAO.updateKeywordLibrary(keywordLibrary);

	}


	@Override
	public List<String> getKeywordLibrariesbyName(String testEngine) {
		return testCaseAutomationScriptDAO.getKeywordLibrariesbyName(testEngine);
	}
	@Override
	public List<KeywordLibrary> getkeywordLibById(Integer keywrdLibId) {
		return testCaseAutomationScriptDAO.getkeywordLibById(keywrdLibId);
	}


	@Override
	public List<BDDKeywordsPhrases> getMyKeywords(Integer userId,
			Integer jtStartIndex, Integer jtPageSize) {
		return testCaseAutomationScriptDAO.getMyKeywords(userId,jtStartIndex,jtPageSize);
	}


	@Override
	public Integer getMyKeywordsSize(Integer userId,
			Integer jtStartIndex, Integer jtPageSize) {
		return testCaseAutomationScriptDAO.getMyKeywordsSize(userId);
	}


	@Override
	public KeywordLibrary getkeywordLibBTestoolAndLanguageAndTypeAndKeywordId(
			Integer testToolId, Integer languagID, String type,Integer id) {
		return testCaseAutomationScriptDAO.getkeywordLibBTestoolAndLanguageAndTypeAndKeywordId(testToolId,languagID,type,id);
	}


	@Override
	public Integer totalKeywordLibraryByKeywordId(Integer keywordId) {
		return testCaseAutomationScriptDAO.totalKeywordLibraryByKeywordId(keywordId);
	}


	@Override
	public BDDKeywordsPhrases getBDDKeyWordsPhraseByKeywordPharse(
			String keywordPhrase) {
		return testCaseAutomationScriptDAO.getBDDKeyWordsPhraseByKeywordPharse(keywordPhrase);
	}
	@Override
	@Transactional
	public Integer saveBDDKeyWordsPhrase(
			BDDKeywordsPhrases bddKeyWordsPhrasesFromUI) {
		return  testCaseAutomationScriptDAO.saveBDDKeyWordsPhrase(bddKeyWordsPhrasesFromUI);
	}


	@Override
	public List<TestCaseScript> getTestcaseScripts(Integer productId,Integer jtStartIndex, Integer jtPageSize) {					
		return testCaseAutomationScriptDAO.getTestCaseScripts(productId,jtStartIndex, jtPageSize);										
	}


	@Override
	@Transactional
	public void addTestcaseScript(TestCaseScript testAutomationScript) {
		try {
			testCaseAutomationScriptDAO.addTestcaseScript(testAutomationScript);
		}catch(Exception e) {

		}

	}


	@Override
	@Transactional
	public void updateTestcaseScript(TestCaseScript testCaseScript) {
		testCaseAutomationScriptDAO.updateTestcaseScript(testCaseScript);
	}


	@Override
	@Transactional
	public void deleteTestcaseScript(TestCaseScript testCaseScript) {
		testCaseAutomationScriptDAO.deleteTestcaseScript(testCaseScript);
	}


	@Override
	@Transactional
	public List<Object[]> getUnMappedTestCasesByProductId(int productId, int scriptId, int jtStartIndex,int jtPageSize) {
		return testCaseAutomationScriptDAO.getUnMappedTestCasesByProductId(productId, scriptId, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public List<Object[]> getMappedTestCasesByScriptId(int scriptId) {
		return testCaseAutomationScriptDAO.getMappedTestCasesByScriptId(scriptId);
	}

	@Override
	@Transactional
	public int getUnMappedTestCasesCountByProductId(int productId, int scriptId) {
		return testCaseAutomationScriptDAO.getUnMappedTestCasesCountByProductId(productId,scriptId);
	}


	@Override
	@Transactional
	public void updateTestCaseToTestCaseScript(Integer scriptId, Integer testCaseId, UserList userList, String mapOrUnmap) {
		TestCaseScriptHasTestCase testCaseScriptHasTestCase = null;
		TestCaseScript testCaseScript = null;
		TestCaseList testCaseList = null;
		ProductMaster productMaster = null;
		TestFactory testFactory = null;
		String testCaseScriptMappingId = "";
		if (scriptId!= null && testCaseId!= null){
			if(mapOrUnmap.equalsIgnoreCase("map")){										
				testCaseScriptHasTestCase = new TestCaseScriptHasTestCase();
				testCaseScriptHasTestCase.setScriptId(scriptId);
				testCaseScriptHasTestCase.setTestCaseId(testCaseId);
				testCaseScriptHasTestCase.setCreatedBy(userList);
				testCaseScriptHasTestCase.setModifiedBy(userList);
				testCaseScriptHasTestCase.setCreatedDate(new Date());
				testCaseScriptHasTestCase.setModifiedDate(new Date());

				testCaseScript = testCaseAutomationScriptDAO.getTestCaseScriptById(scriptId);
				testCaseList = testCaseListDAO.getByTestCaseId(testCaseId);
				productMaster = testCaseScript.getProduct();
				testFactory = productMaster.getTestFactory();

				if(testCaseScript != null){								
					testCaseScriptHasTestCase.setScriptName(testCaseScript.getScriptName());
				}
				if(testCaseList != null){								
					testCaseScriptHasTestCase.setTestCaseName(testCaseList.getTestCaseName());
				}
				if(productMaster != null){								
					testCaseScriptHasTestCase.setProductName(productMaster.getProductName());
				}
				if(testFactory != null){								
					testCaseScriptHasTestCase.setTestFactoryName(testFactory.getTestFactoryName());
				}
				testCaseAutomationScriptDAO.addTestCaseScriptAssociation(testCaseScriptHasTestCase);
				mongoDBService.addTestCaseScriptMappingToMongoDB(testCaseScriptHasTestCase);
			}else if(mapOrUnmap.equalsIgnoreCase("unmap")){	
				testCaseScriptHasTestCase = getTestCaseScriptAssociationByIds(scriptId, testCaseId);
				testCaseAutomationScriptDAO.deleteTestCaseScriptAssociation(testCaseScriptHasTestCase);
				mongoDBService.removeTestCaseScriptMappingFromMongoDB(testCaseScriptHasTestCase.getId());
			}
		}
	}


	@Override
	@Transactional
	public TestCaseScript getTestCaseScriptById(int testCaseScriptId) {
		return testCaseAutomationScriptDAO.getTestCaseScriptById(testCaseScriptId);
	}


	@Override
	@Transactional
	public TestCaseScript getTestCaseScriptByName(String testCaseScriptName) {
		return testCaseAutomationScriptDAO.getTestCaseScriptByName(testCaseScriptName);
	}


	@Override
	public void addTestCaseScriptAssociation(TestCaseScriptHasTestCase testCaseScriptHasTestCase) {
		try {
			testCaseAutomationScriptDAO.addTestCaseScriptAssociation(testCaseScriptHasTestCase);

		}catch(Exception e) {
			log.error(" Error in addTestCaseScriptAssociation",e);
		}
	}


	@Override
	public void deleteTestCaseScriptAssociation(TestCaseScriptHasTestCase testCaseScriptHasTestCase) {
		testCaseAutomationScriptDAO.deleteTestCaseScriptAssociation(testCaseScriptHasTestCase);
	}


	@Override
	public TestCaseScriptHasTestCase getTestCaseScriptAssociationByIds(Integer scriptId, Integer testCaseId) {
		return testCaseAutomationScriptDAO.getTestCaseScriptAssociationByIds(scriptId,testCaseId);
	}


	@Override
	public List<TestCaseScriptHasTestCase> getTestcaseAndScriptByScriptId(Integer testScriptId) {
		try {
			return testCaseAutomationScriptDAO.getTestcaseAndScriptByScriptId(testScriptId);
		}catch(Exception e) {
			log.error("Error in Service getTestcaseAndScriptByScriptId",e);
		}
		return null;
	}


	@Override
	@Transactional
	public List<TestCaseScript> getTestcaseScriptsList() {
		return testCaseAutomationScriptDAO.getTestcaseScriptsList();
	}


	@Override
	@Transactional
	public Integer getTestCaseScriptCount(Integer productId) {
		try {
			return testCaseAutomationScriptDAO.getTestCaseScriptCount(productId);
		}catch(Exception e) {

		}
		return null;
	}


	@Override
	@Transactional
	public List<TestCaseScript> getTestcaseScriptByScriptId(Integer scriptId) {
		try {
			return testCaseAutomationScriptDAO.getTestcaseScriptByScriptId(scriptId);
		}catch(Exception e) {

		}
		return null;
	}


	@Override
	@Transactional
	public List<TestCaseScript> getTestCaseScripsByTestcaseId(Integer testcaseId) {
		try {
			return testCaseAutomationScriptDAO.getTestCaseScripsByTestcaseId(testcaseId);
		}catch(Exception e) {
			log.error("Error in getTestCaseScripsByTestcaseId",e);
		}
		return null;
	}				
	@Override
	@Transactional
	public void addTestDataItems(Attachment testDataAttach) {

		List<TestDataItems> testDataItemsList = getTestDataItemsFromTestDataExcel(testDataAttach);
		log.info("testDataItemsList size==>"+testDataItemsList);
		int productId = testDataAttach.getProduct().getProductId();
		for(TestDataItems testDataItems : testDataItemsList){
			TestDataItems testDataItem = testCaseAutomationScriptDAO.getTestDataItemByItemName(testDataItems.getDataName(), productId, null);
			if(testDataItem == null){
				Set<TestDataItemValues> testDataItemValuesSet = new HashSet<TestDataItemValues>();
				testDataItemValuesSet = testDataItems.getTestDataItemsValSet();
				testDataItems.setTestDataItemsValSet(null);
				testDataItems.setIsShare(1);
				int testDataItemId = testCaseAutomationScriptDAO.addTestDataItems(testDataItems);
				testDataItems.setTestDataItemId(testDataItemId);
				if(testDataItemValuesSet != null && !testDataItemValuesSet.isEmpty()){
					for(TestDataItemValues testDataItemVal : testDataItemValuesSet){
						TestDataPlan testDataPlan = testDataItemVal.getTestDataPlan();
						if(testDataPlan.getTestDataPlanId() != null){
							testDataItemVal.setTestDataPlan(testDataPlan);
						}else{
							testDataItemVal.setTestDataPlan(null);
						}
						testDataItemVal.setTestDataItems(testDataItems);
						testCaseAutomationScriptDAO.addTestDataItemValues(testDataItemVal);
					}
				}
			}else if(testDataItem.getUserlist().getUserId() == testDataAttach.getCreatedBy().getUserId()){
				testDataItems.setTestDataItemId(testDataItem.getTestDataItemId());
				testDataItems.setCreatedDate(new Date());
				Set<TestDataItemValues> testDataItemValuesSet = new HashSet<TestDataItemValues>();
				if(testDataItems.getTestDataItemsValSet() != null && !testDataItems.getTestDataItemsValSet().isEmpty()){
					for(TestDataItemValues testDataItemVal : testDataItems.getTestDataItemsValSet()){
						TestDataItemValues testDataItemValues =	testCaseAutomationScriptDAO.getTestDataItemValuesByProductAndTestItemValName(productId, null, testDataItemVal.getValues(), testDataItem.getTestDataItemId(),testDataItemVal.getTestDataPlan().getTestDataPlanId());
						if(testDataItemValues == null){
						}else{
							testDataItemVal.setTestDataValueId(testDataItemValues.getTestDataValueId());
						}
						testDataItemVal.setTestDataItems(testDataItems);
						testDataItemValuesSet.add(testDataItemVal);
					}
				}
				testDataItems.setTestDataItemsValSet(testDataItemValuesSet);
				testDataItems.setIsShare(1);
				testCaseAutomationScriptDAO.updateTestDataItems(testDataItems);

			}

		}

	}
	public List<TestDataItems> getTestDataItemsFromTestDataExcel(Attachment testDataAttach){
		List<TestDataItems> listTestDataItems = new ArrayList<TestDataItems>();
		String fileName = "";
		String testDataUrl = null;
		Set<TestDataItemValues> testDataItemsValSet = null;
		Integer productId = null;
		TestDataPlan testDataPlan = new TestDataPlan();
		ProductMaster productMaster = new ProductMaster();
		String testdatItemvalue = "";
		DateFormat originalFormat = null;
		DateFormat targetFormat = null;
		Date formattedDate = null;
		try{
			if(testDataAttach.getProduct().getProductId() != null)
				productId = testDataAttach.getProduct().getProductId();
			productMaster.setProductId(productId);
			if(testDataAttach != null){
				testDataUrl = testDataAttach.getAttributeFileURI();
			}
			if (testDataUrl != null && (new File(testDataUrl)).exists()) { 
				fileName=testDataAttach.getAttachmentPrefixName();
				log.info("loadKeywords function. Test Data exists");

				Map <String, String> dataSourceProperties = new HashMap<String, String>();
				dataSourceProperties.put(TestDataManagerFactory.DATA_PROPERTIES_FILENAME_KEY, testDataUrl);
				dataSourceProperties.put(TestDataManagerFactory.DATA_PROPERTIES_WORKSHEET_KEY, "Test-Data");

				TAFTestDataManager tafTestDataManager = TestDataManagerFactory.getTestDataManager(TestDataManagerFactory.DATA_SOURCE_EXCEL, dataSourceProperties);
				if(tafTestDataManager != null)
				{
					List<TestDataItem> listTestDataElements = tafTestDataManager.listTestDataItems();
					if (listTestDataElements != null) {
						log.info("listTestDataElements : " + listTestDataElements.size());
						for(TestDataItem testDataItemFromExcel : listTestDataElements){
							TestDataItems testDataItems = new TestDataItems(); 
							testDataItems.setCreatedDate(new Date());
							testDataItems.setDataName(testDataItemFromExcel.testData);
							testDataItems.setIsShare(0);
							testDataItems.setProductMaster(productMaster);
							testDataItems.setRemarks(testDataItemFromExcel.remarks);
							testDataItems.setUserlist(testDataAttach.getCreatedBy());
							testDataItems.setType(testDataItemFromExcel.type.trim());
							testDataItems.setGroupName(testDataAttach.getAttachmentPrefixName());

							testDataPlan = new TestDataPlan();
							if(testDataItemFromExcel.testPlan == null || testDataItemFromExcel.testPlan == ""){
								TestDataPlan testDataPlanFromDB = testCaseAutomationScriptDAO.getTestDataPlanByTestDataPlanName("SampleTDP",testDataAttach.getProduct().getProductId());
								if(testDataPlanFromDB == null ){
									testDataPlan.setTestDataPlanName("SampleTDP");
									testDataPlan.setTestDataPlanDescription("SampleTDP");
									testDataPlan.setProductMaster(productMaster);
									testDataPlan.setUserlist(testDataAttach.getCreatedBy());
									testDataPlan.setCreatedOn(new Date());
									Integer testDataPlanId = testCaseAutomationScriptDAO.addTestDataPlan(testDataPlan);
									testDataPlan.setTestDataPlanId(testDataPlanId);
								}else{
									testDataPlan = testDataPlanFromDB;
									testDataPlan.setCreatedOn(new Date());
								}

							}else{

								TestDataPlan testDataPlanFromDB = testCaseAutomationScriptDAO.getTestDataPlanByTestDataPlanName(testDataItemFromExcel.testPlan,testDataAttach.getProduct().getProductId());
								if(testDataPlanFromDB == null ){
									testDataPlan.setTestDataPlanName(testDataItemFromExcel.testPlan);
									testDataPlan.setCreatedOn(new Date());
									testDataPlan.setProductMaster(productMaster);
									testDataPlan.setUserlist(testDataAttach.getCreatedBy());
									Integer testDataPlanId =  	testCaseAutomationScriptDAO.addTestDataPlan(testDataPlan);
									testDataPlan.setTestDataPlanId(testDataPlanId);
								}else{
									testDataPlan = testDataPlanFromDB;
									testDataPlan.setCreatedOn(new Date());
								}
							}
							//Set<String> uniqueValSet = new HashSet<String>();
							List<String> valListfromExcel = new ArrayList<String>();
							List<Object> testDataItemsValues = testDataItemFromExcel.additionalValues;
							testDataItemsValSet = new LinkedHashSet<TestDataItemValues>();
							if(testDataItemsValues != null && !testDataItemsValues.isEmpty()){
								for(Object testDataObj :testDataItemsValues){
									if(valListfromExcel.add(testDataObj.toString().trim())){
										TestDataItemValues testDataItemVal = new TestDataItemValues();
										testDataItemVal.setTestDataPlan(testDataPlan);
										testDataItemVal.setCreatedDate(new Date());
										if(testDataObj.toString().trim() != null && testDataObj.toString().trim().contains("IST") && testDataObj.toString().trim().contains("00:00:00")){
											testdatItemvalue = testDataObj.toString().trim().replace("IST", "");
											originalFormat = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy");
											targetFormat = new SimpleDateFormat("dd/MM/yyyy");
											formattedDate = originalFormat.parse(testdatItemvalue);
											testdatItemvalue = targetFormat.format(formattedDate);
											testDataItemVal.setValues(testdatItemvalue);
										}else {
											if(testDataItemFromExcel.type.trim().equalsIgnoreCase("Number")) {
												if(CommonUtility.numericValidation(testDataObj.toString())) {
													log.info("Test data value type is not valid number:"+testDataObj.toString());
													continue;
												}
												
											} else if(testDataItemFromExcel.type.trim().equalsIgnoreCase("Text")) {
												//Dont Do any validation - ADI support
												/*if(!CommonUtility.numericValidation(testDataObj.toString())) {
													log.info("Test data value type is not valid text:"+testDataObj.toString());
													continue;
												}*/
											}
											testDataItemVal.setValues(testDataObj.toString());
										}
										testDataItemVal.setTestDataItems(testDataItems);
										testDataItemsValSet.add(testDataItemVal);
									}

								}

								testDataItems.setTestDataItemsValSet(testDataItemsValSet);
							}

							listTestDataItems.add(testDataItems);
						}

					}
					else
					{
						log.info("loadKeywords function. TestData dataItems don't exist");
					}
				}
				else
				{
					log.info("TAFTestDataManager is null");
				}
			}
		}catch(Exception e){
			log.error("Error while reading test data excel file :", e);
			e.printStackTrace();

		}

		return listTestDataItems;
	}
	@Override
	public void addUIObjects(Attachment objectRepAttach) {

		List<UIObjectItems> listUIObjects = getUIObjectContentFromUIObjectExcel(objectRepAttach);
		log.info("listUIObjects size==>" + listUIObjects.size());
		int productId = objectRepAttach.getProduct().getProductId();
		ProductMaster productMaster = new ProductMaster();
		productMaster.setProductId(productId);
		for (UIObjectItems uiObjects : listUIObjects) {

			uiObjects.setProductMaster(productMaster);
			uiObjects.setUserlist(objectRepAttach.getCreatedBy());
			uiObjects.setCreatedDate(new Date());
			uiObjects.setModifiedDate(new Date());
			uiObjects.setHandle(objectRepAttach.getAttachmentPrefixName());
			uiObjects.setIsShare(1);
			UIObjectItems uiObjFromDB = testCaseAutomationScriptDAO.getUIObjectItemByElementName(uiObjects.getElementName(),productId,null);
			if(uiObjFromDB != null){
				if(uiObjFromDB.getUserlist().getUserId().equals(objectRepAttach.getCreatedBy().getUserId())){
					log.info("uiObjFromDB.getUserlist().getUserId() is :"+uiObjFromDB.getUserlist().getUserId()+" objectRepAttach.getCreatedBy().getUserId() is: "+objectRepAttach.getCreatedBy().getUserId());
					uiObjects.setUiObjectItemId(uiObjFromDB.getUiObjectItemId());
					testCaseAutomationScriptDAO.updateUIObjects(uiObjects);
				}

			}else{
				testCaseAutomationScriptDAO.addUIObjects(uiObjects);
			}

		}

	}
	public List<UIObjectItems> getUIObjectContentFromUIObjectExcel(Attachment objectRepAttach){
		List<UIObjectItems> listUIObjects = new ArrayList<>();
		String fileName = "";
		String objectRespositoryUrl = null;
		if(objectRepAttach != null){
			objectRespositoryUrl = objectRepAttach.getAttributeFileURI();
		}
		if (objectRespositoryUrl != null && (new File(objectRespositoryUrl)).exists()) { 
			fileName= objectRepAttach.getAttachmentPrefixName();
			log.info("getUIObjectContentFromUIObjectExcel function. Object repository exists");
			Map <String, String> objectSourceProperties = new HashMap<String, String>();
			objectSourceProperties.put(ObjectRepositoryManagerFactory.PROPERTIES_FILENAME_KEY, objectRespositoryUrl);
			objectSourceProperties.put(ObjectRepositoryManagerFactory.PROPERTIES_WORKSHEET_KEY, "Object-Repository");
			objectSourceProperties.put(ObjectRepositoryManagerFactory.PROPERTIES_APPLICATION_TYPE_KEY, "Web");
			objectSourceProperties.put(ObjectRepositoryManagerFactory.PROPERTIES_BROWSER_KEY, "Chrome");

			TAFObjectRepositoryManager tafObjectRespositoryManager = ObjectRepositoryManagerFactory.getObjectRepositoryManager(ObjectRepositoryManagerFactory.DATA_SOURCE_EXCEL, objectSourceProperties);
			if(tafObjectRespositoryManager != null) {
				List<Element> listElements = tafObjectRespositoryManager.listElements();

				if (listElements != null) {
					log.info("Elements size: " + listElements.size());
					for (Element element : listElements) {					
						if(null != element){
							UIObjectItems uiObjects = new UIObjectItems(); 
							uiObjects.setIsShare(0); 	
							uiObjects.setElementName(element.name);
							uiObjects.setDescription(element.description);
							uiObjects.setTestEngineName(element.applicationType);
							if (null != element.applicationType && element.applicationType.equalsIgnoreCase("Web")) {
								uiObjects.setPageName(element.pageName);
								uiObjects.setElementType(element.elementType);
								uiObjects.setPageURL(element.pageURL);
								uiObjects.setGroupName(element.groupName);
								uiObjects.setIdType(element.webElement.idType);
								uiObjects.setWebLabel(element.webElement.id);
								Map<String, String> xpaths = element.webElement.xpaths;
								if (!xpaths.isEmpty()) {

									for(Map.Entry<String, String> entry : xpaths.entrySet()){

										if(entry.getKey().equalsIgnoreCase(AOTCConstants.BROWSER_CHROME)){
											uiObjects.setChrome(entry.getValue());
										}else if(entry.getKey().equalsIgnoreCase(AOTCConstants.BROWSER_FIREFOX)){
											uiObjects.setFirefox(entry.getValue());
										}
										else if(entry.getKey().equalsIgnoreCase(AOTCConstants.BROWSER_IE)){
											uiObjects.setIe(entry.getValue());
										}
										else if(entry.getKey().equalsIgnoreCase(AOTCConstants.BROWSER_SAFARI)){
											uiObjects.setSafari(entry.getValue());
										}else if(entry.getKey().equalsIgnoreCase(AOTCConstants.BROWSER_FIREFOXGECKO)){
											uiObjects.setFirefoxgecko(entry.getValue());
										}else if(entry.getKey().equalsIgnoreCase(AOTCConstants.BROWSER_EDGE)){
											uiObjects.setEdge(entry.getValue());
										}else{}
									}

								} 
								Map<String, String> cssLocators =	element.webElement.cssLocators;

								if (!cssLocators.isEmpty()) {
									for(Map.Entry<String, String> entry : cssLocators.entrySet()){
										if(entry.getKey().equalsIgnoreCase(AOTCConstants.BROWSER_CHROME)){
											uiObjects.setChrome(entry.getValue());
										}else if(entry.getKey().equalsIgnoreCase(AOTCConstants.BROWSER_FIREFOX)){
											uiObjects.setFirefox(entry.getValue());
										}
										else if(entry.getKey().equalsIgnoreCase(AOTCConstants.BROWSER_IE)){
											uiObjects.setIe(entry.getValue());
										}
										else if(entry.getKey().equalsIgnoreCase(AOTCConstants.BROWSER_SAFARI)){
											uiObjects.setSafari(entry.getValue());
										}else if(entry.getKey().equalsIgnoreCase(AOTCConstants.BROWSER_FIREFOXGECKO)){
											uiObjects.setFirefoxgecko(entry.getValue());
										}else if(entry.getKey().equalsIgnoreCase(AOTCConstants.BROWSER_EDGE)){
											uiObjects.setEdge(entry.getValue());
										}else{}
									}
								}


							}  
							if (null != element.applicationType && element.applicationType.equalsIgnoreCase("MOBILE")){
								uiObjects.setPageName(element.pageName);
								uiObjects.setElementType(element.elementType);
								uiObjects.setPageURL(element.pageURL);
								uiObjects.setGroupName(element.groupName);

								uiObjects.setSeeTestIndexIndex(element.seetestMobileElement.index);
								uiObjects.setSeeTestZoneIndex(element.seetestMobileElement.zone);
								uiObjects.setSeetestLabel(element.seetestMobileElement.id);

								uiObjects.setIdType(element.appiumMobileElement.idType);
								uiObjects.setAppiumLabel(element.appiumMobileElement.id);
							}

							if (null != element.applicationType && element.applicationType.equalsIgnoreCase("Desktop")){
								uiObjects.setPageName(element.pageName);
								uiObjects.setElementType(element.elementType);
								uiObjects.setPageURL(element.pageURL);
								uiObjects.setGroupName(element.groupName);

								uiObjects.setIdType(element.codedUIElement.idType);
								uiObjects.setCodeduiLabel(element.codedUIElement.id);

								uiObjects.setTestCompleteLabel(element.testCompleteElement.id);
							}

							listUIObjects.add(uiObjects);
						} else {
							log.info("TAFObjectRepositoryManager Element is null");
						}
					}

				} else {
					log.info("getUIObjectContentFromUIObjectExcel function. Object repository dataItems don't exist");
				}
			} else {
				log.info("TAFObjectRepositoryManager is null");
			}
		}
		log.info("Call returned from getObjectRepositoryManager");
		return listUIObjects;
	}

	@Override
	@Transactional
	public List<JsonTestCaseScript> getTestAutomationStoryWithVersions(Integer testCaseId, String scriptType, String testEngine, Integer productId,
			HttpSession session) {


		if (testCaseId == null) {
			log.info("Testcase Id is not specified. Scripts cannot be sent");
			return null;
		}
		List<JsonTestCaseScript> jsonTestCaseScriptList = new ArrayList<JsonTestCaseScript>(); 
		List<JsonTestCaseAutomationStory> jsonTestCaseAutomationScriptList = new ArrayList<JsonTestCaseAutomationStory>();
		try {
			List<TestCaseAutomationStory> storys = testCaseStoryDAO.getTestCaseStoryWithVersions(testCaseId);
			if (storys != null) {
				JsonTestCaseAutomationStory jsonTestCaseAutomationScript = new JsonTestCaseAutomationStory();
				JsonTestCaseScript jsonTestCaseScript;
				for (TestCaseAutomationStory story : storys) {
					jsonTestCaseScript = new JsonTestCaseScript();
					jsonTestCaseScript.setTestCaseAutomationScriptId(story.getTestCaseAutomationStoryId());
					String newstory = story.getScript();
					newstory = newstory.replaceAll("(\\\\n\\\\r|\\\\n|\\\\r)", System.lineSeparator());
					jsonTestCaseScript.setTestCaseScript(newstory);
					jsonTestCaseScript.setTestCaseId(story.getTestCase().getTestCaseId());
					jsonTestCaseScript.setTestToolName(story.getTestTool().getTestToolName());
					jsonTestCaseScript.setScriptType(story.getScriptType().getScriptType());
					jsonTestCaseScript.setTestEngineConfigFile(story.getTestEngineConfigFile());
					jsonTestCaseScript.setVersionId(story.getVersionId());
					jsonTestCaseScript.setTestCaseName(story.getTestCase().getTestCaseName());

					jsonTestCaseScript = this.loadKeywords(jsonTestCaseScript, "-1", "-1", "-1", scriptType, "", productId, session );

					jsonTestCaseScriptList.add(jsonTestCaseScript);
				}

				return  jsonTestCaseScriptList;
			} else {
				log.info("Unable to get Automation Scripts List for Testcase : " + testCaseId);
				return null;
			}
		} catch (Exception e) {
			log.error("Unable to get Automation Scripts List for Testcase : " + testCaseId, e);
			return null;
		}

	}
	private JsonTestCaseScript loadKeywords(JsonTestCaseScript jsonTestScript, String objectRepAttach, String testDataAttach, String productTypeName, String testEngine,String amdocsMode,Integer productId, HttpSession session) {
		String fileName ="";
		try {

			if (jsonTestScript == null)
				return jsonTestScript;

			Integer userId=0;
			UserList user = (UserList)session.getAttribute("USER");
			if(null != user && null != user.getUserId()){
				userId = user.getUserId();
			}

			List<String> keywords = new ArrayList<String>();
			keywords= testCaseAutomationScriptDAO.getKeywordPhrases("",testEngine); // product type , test tool 
			List<String> keywordRegularExpressions = new ArrayList<String>();
			//Get the Keyword phrase regular expressions. These are needed to validate syntax in the ATSG editor
			keywordRegularExpressions= testCaseAutomationScriptDAO.getKeywordPhraseRegularExpressions("",testEngine); // product type , test tool 
			jsonTestScript.setKeywordRegularExpressions(keywordRegularExpressions);

			if(keywords == null ) {
				keywords = new ArrayList<String>();
			}

			List<String> elements = testCaseAutomationScriptDAO.listUIObjectItemElementNamesByProjectId(productId, userId, objectRepAttach);
			if (elements != null) {
				for (String element : elements) {
					if(element.contains("~")){
						if(element.split("~").length == 2){
							keywords.add(element.split("~")[1]);
						}
					}

				}
			} else {
				log.info("loadKeywords function. Object repository dataItems don't exist");
			}

			List<String> dataItems = testCaseAutomationScriptDAO.listTestDataNamesByProductId(productId, userId,testDataAttach);
			if (dataItems != null) {
				for (String dataItem : dataItems) {
					if(dataItem.contains("~")){
						if(dataItem.split("~").length == 2){
							keywords.add(dataItem.split("~")[1]);
						}
					}

				}
			}
			else
			{
				log.info("loadKeywords function. TestData dataItems don't exist");
			}


			//Add the page objects for ATT/Amdocs mode into the keywords
			if (amdocsMode != null && amdocsMode.equalsIgnoreCase("Yes")) { 

				List<AmdocsPageObjects> amdocsPageObjects = testCaseAutomationScriptDAO.listAmdocsPageObjectsByProjectIdAndTestCaseId(productId, null,jsonTestScript.getTestCaseId());
				List<AmdocsPageMethods> amdocsPageMethods = testCaseAutomationScriptDAO.listAmdocsPageMethodsByProjectId(productId, null);

				if(amdocsPageObjects != null) {
					for(AmdocsPageObjects amdocsPageObject : amdocsPageObjects) {

						if (amdocsPageObject.getPackageName() == null || amdocsPageObject.getPackageName().isEmpty()) {
							keywords.add(amdocsPageObject.getName());
						} else {
							keywords.add(amdocsPageObject.getPackageName() + "." + amdocsPageObject.getName());
						}
					}
				}
				if(amdocsPageMethods != null) {
					for(AmdocsPageMethods pageMethod : amdocsPageMethods) {

						String displayName = pageMethod.getAmdocsPageObjects().getName() + "." + pageMethod.getMethodName();
						if (pageMethod.getParams() == null || pageMethod.getParams().isEmpty()) {
							displayName = displayName + "()";
						} else {
							displayName = displayName + "(..)";
						}
						keywords.add(displayName);
					}
				}

			} else {
				log.info("loadKeywords function. Page objects don't exist");
			}
			jsonTestScript.setKeywords(keywords);

		} catch(Exception ex) {
			log.error("loadKeywords function. Exception occured. " + ex);
		}
		return jsonTestScript;
	}
	@Override
	@Transactional
	public List<BDDKeywordsPhrases> getBDDKeywordsPhrasesList(String productType,String testTool,Integer status,Integer jtStartIndex,Integer jtPageSize,Map<String, String> searchStrings){

		return testCaseAutomationScriptDAO.getBDDKeywordsPhrases(productType,testTool,status,jtStartIndex,jtPageSize,searchStrings);

	}

	@Override
	@Transactional
	public String getTestCaseStoryEditingUser(Integer testCaseId) {

		return testCaseAutomationScriptDAO.getTestCaseStoryEditingUser(testCaseId);
	}

	@Override
	@Transactional
	public void updateTestStoryEditingStatus(Integer testCaseId, String userName, String editingStatus,Integer userId) {
		testCaseAutomationScriptDAO.updateTestStoryEditingStatus(testCaseId, userName, new Date(System.currentTimeMillis()), editingStatus,userId);
	}
	@Override
	@Transactional
	public List<String> loadAttachmentKeywords(HttpSession session, Integer productId,Integer attachmentId,String type,String filter) {
		List<String> keywordList = new ArrayList<String>();
		try
		{
			Integer userId = 0;
			if(null != session){
				UserList user = (UserList)session.getAttribute("USER");
				if(null != user && null != user.getUserId()){
					userId = user.getUserId();
				}				
			}
			if (type.equalsIgnoreCase("ObjectRepository")) {
				List<String> elements = testCaseAutomationScriptDAO.listUIObjectItemElementNamesByProjectId(productId, userId,filter);
				if (elements != null) {
					for (String element : elements) {
						keywordList.add(element);

					}
				} else {
					log.info("loadKeywords function. Object repository dataItems don't exist");
				}

			}
			else if (type.equalsIgnoreCase("TestData")) {
				List<String> dataItems = testCaseAutomationScriptDAO.listTestDataNamesByProductId(productId, userId,filter);
				if (dataItems != null) {
					for (String dataItem : dataItems) {
						keywordList.add(dataItem);
					}
				}
				else
				{
					log.info("loadKeywords function. TestData dataItems don't exist");
				}
			}else{

			}

		}
		catch(Exception ex)
		{
			log.error("loadKeywords function. Exception occured. " + ex.getMessage());
		}
		return keywordList;
	}
	@Override
	public List<Attachment> listDeviceObjectsEDAT(Integer productId) {
		List<Attachment> deviceObjectsList = new ArrayList<Attachment>();
		try
		{
			deviceObjectsList = testCaseAutomationScriptDAO.listDeviceObjectsNamesByProductId(productId);
		}
		catch(Exception ex)
		{
			log.error("loadKeywords function. Exception occured. " + ex.getMessage());
		}
		return deviceObjectsList;
	}
	@Override
	@Transactional
	public String saveTestCaseAutomationStory( Integer testCaseId,
			String testEngine, String automationScript,
			String selectedConfigFile) {
		try {
			log.info("Automation Script : " + automationScript);
			int versionId = 0;
			Integer versionMaxIdfromDB = testCaseAutomationScriptDAO.getMaxVersionIdByTestCaseId(testCaseId);
			TestCaseAutomationStory testCaseAutomationStory = new TestCaseAutomationStory();
			TestCaseList testCase = new TestCaseList();
			if (automationScript == null)
				automationScript = "";

			if(versionMaxIdfromDB != null){
				versionId = versionMaxIdfromDB+1;
			}
			if (testEngine != null) {
				TestToolMaster testToolObject = testCategoryMasterDAO.getTestToolByName(testEngine);
				if (testToolObject != null)
					testCaseAutomationStory.setTestTool(testToolObject);
			}
			testCase = testCaseListDAO.getByTestCaseId(testCaseId);
			if(testCase.getTestCaseName() != null){
				testCaseAutomationStory.setName(testCase.getTestCaseName());
			}
			else{
				testCaseAutomationStory.setName("New_Automation_Story");
			}
			testCaseAutomationStory.setDescription("Auto Created Test Automation Script entry");
			testCaseAutomationStory.setScriptSource("iLCM");
			testCaseAutomationStory.setScriptFileName("");
			testCaseAutomationStory.setScriptURI("");
			ScriptTypeMaster scriptTypeMaster = new ScriptTypeMaster("GHERKIN");
			testCaseAutomationStory.setScriptType(scriptTypeMaster);
			testCaseAutomationStory.setScript(automationScript);
			testCaseAutomationStory.setVersionId(versionId);
			testCase.setTestCaseId(testCaseId);
			testCaseAutomationStory.setTestCase(testCase);
			testCaseAutomationStory.setCreatedDate(new Date(System.currentTimeMillis()));
			testCaseAutomationStory.setModifiedDate(new Date(System.currentTimeMillis()));
			testCaseAutomationStory.setTestEngineConfigFile(selectedConfigFile);
			if(testCase.getProductMaster().getProductId() != null)
				testCaseAutomationStory.setProductId(testCase.getProductMaster().getProductId());
			testCaseAutomationScriptDAO.updateTestCaseAutomationScript(testCaseAutomationStory);
			return "SUCCESS";
		} catch (Exception e) {
			log.error("Unable to Save Automation Script", e);
			return "FAILED : " + e.getMessage();
		}
	}
	@Override
	@Transactional
	public TestStoryGeneratedScripts saveGeneratedTestScript(Integer testCaseId,
			Integer testCaseStoryId, String script, String testEngine,
			String languageName ,Integer userId, String downloadPath,String codeGenerationMode) {

		try {
			String[] outputPackage;
			String outputPackageName = null;
			TestStoryGeneratedScripts storyGeneratedScripts = new TestStoryGeneratedScripts();
			TestCaseAutomationStory testCaseAutomationStory =  new TestCaseAutomationStory();
			Languages languages = null;
			UserList userList = new UserList();
			userList.setUserId(userId);
			TestToolMaster testToolMaster = null;
			TestCaseList testCaseList = null;
			String productType = null;
			if (languageName != null) {
				languages = languagesDAO.getLanguageByName(languageName);
				if (languages != null)
					storyGeneratedScripts.setLanguages(languages);
			}
			if (testEngine != null) {
				testToolMaster = testCategoryMasterDAO.getTestToolByName(testEngine);
				if (testToolMaster != null)
					storyGeneratedScripts.setTestTool(testToolMaster);
			}
			if(testCaseId != null){
				testCaseList = testCaseService.getTestCaseById(testCaseId);
				if(testCaseList != null)
					productType = testCaseList.getProductType().getTypeName();
			}
			if(productType != null && productType.equalsIgnoreCase("Web") && testEngine.equalsIgnoreCase("TESTCOMPLETE")){
				outputPackageName = "com.hcl.ers.atsg.script.output";
			}
			else if(testEngine.equalsIgnoreCase("CODEDUI")){
				outputPackageName = "com.hcl.taf";
			}
			else if(!testEngine.equalsIgnoreCase("EDAT") && script != null){
				outputPackage = script.split(";");
				outputPackageName = outputPackage[0];
				if(outputPackageName != null && !outputPackageName.isEmpty()){
					outputPackageName = outputPackageName.trim();
					outputPackageName = outputPackageName.substring(8, outputPackageName.length());
					log.info("output package is :"+outputPackageName);
				}
			//seeting output package name with taf mode as default for edat
			}else if(testEngine.equalsIgnoreCase("EDAT")){
				outputPackageName = "com.hcl.taf";
			}else{

			}

			TestStoryGeneratedScripts testStoryGeneratedScript = testCaseStoryDAO.getGeneratedScripts(testCaseStoryId,languages.getId(),testToolMaster.getTestToolId());
			if(testStoryGeneratedScript != null){
				testCaseAutomationStory.setTestCaseAutomationStoryId(testCaseStoryId);
				testStoryGeneratedScript.setTestCaseStory(testCaseAutomationStory);
				if(codeGenerationMode != null && codeGenerationMode.equalsIgnoreCase("GENERIC-MODE") && outputPackageName != null && downloadPath != null){
					testStoryGeneratedScript.setTestScriptForGeneric(script);
					testStoryGeneratedScript.setOutputpackageForGeneric(outputPackageName);
					testStoryGeneratedScript.setDownloadPathForGeneric(downloadPath);
				}
				else{
					testStoryGeneratedScript.setTestScript(script);
					testStoryGeneratedScript.setOutputPackage(outputPackageName);
					testStoryGeneratedScript.setDownloadPath(downloadPath);
				}
				testStoryGeneratedScript.setLanguages(languages);
				testStoryGeneratedScript.setTestTool(testToolMaster);
				testStoryGeneratedScript.setUserList(userList);


				testStoryGeneratedScript.setCodeGenerationMode(codeGenerationMode);
				return testCaseStoryDAO.saveGeneratedScripts(testStoryGeneratedScript );
			}else{
				testCaseAutomationStory.setTestCaseAutomationStoryId(testCaseStoryId);
				storyGeneratedScripts.setTestCaseStory(testCaseAutomationStory);
				if(codeGenerationMode != null && codeGenerationMode.equalsIgnoreCase("GENERIC-MODE") && outputPackageName != null && downloadPath != null){
					storyGeneratedScripts.setTestScriptForGeneric(script);
					storyGeneratedScripts.setOutputpackageForGeneric(outputPackageName);
					storyGeneratedScripts.setDownloadPathForGeneric(downloadPath);
				}
				else{
					storyGeneratedScripts.setTestScript(script);
					storyGeneratedScripts.setOutputPackage(outputPackageName);
					storyGeneratedScripts.setDownloadPath(downloadPath);
				}
				storyGeneratedScripts.setLanguages(languages);
				storyGeneratedScripts.setTestTool(testToolMaster);
				storyGeneratedScripts.setUserList(userList);
				storyGeneratedScripts.setCodeGenerationMode(codeGenerationMode);
				return testCaseStoryDAO.saveGeneratedScripts(storyGeneratedScripts );
			}

		} catch (Exception e) {
			log.error("Unable to Save generated Script", e);
			return null;
		}

	}
	@Override
	@Transactional
	public JsonTestStoryGeneratedScripts getAutomationScript(
			Integer testCaseStoryId, String languageName, String testEngine ) {
		JsonTestStoryGeneratedScripts jsonTestStoryGeneratedScripts = null;
		if (testCaseStoryId == null) {
			log.info("Testcase Story ID has not been specified. Scripts cannot be sent");
			return null;
		}

		try {
			TestToolMaster testToolMaster = null;
			if (testEngine != null) {
				testToolMaster = testCategoryMasterDAO.getTestToolByName(testEngine);
			}
			Languages lang = languagesDAO.getLanguageByName(languageName);
			TestStoryGeneratedScripts testStoryGeneratedScript = testCaseStoryDAO.getGeneratedScripts(testCaseStoryId,lang.getId(),testToolMaster.getTestToolId());
			if (testStoryGeneratedScript != null) {
				String newGeneartedScript = testStoryGeneratedScript.getTestScript();
				testStoryGeneratedScript.setTestScript(newGeneartedScript);
				jsonTestStoryGeneratedScripts = new JsonTestStoryGeneratedScripts(testStoryGeneratedScript);		
			}



		} catch (Exception e) {
			log.error("Unable to get generated Scripts List for Teststory : " + testCaseStoryId, e);
			return null;
		}
		return  jsonTestStoryGeneratedScripts;
	}

	@Override
	@Transactional
	public JsonTestStoryGeneratedScripts updatedGeneartedScript(
			Integer generatedScriptId, String updatedScript ,String languageName,String modifiedBy, String testToolName,String codeGenerationMode) {

		Languages lang = languagesDAO.getLanguageByName(languageName);
		String[] outputPackage;
		String outputPackageName = null;
		TestStoryGeneratedScripts testStoryGeneratedScript = testCaseStoryDAO.getGeneratedScript(generatedScriptId,lang.getId());
		JsonTestStoryGeneratedScripts jsonTestStoryGeneratedScripts = null;

		if(testStoryGeneratedScript != null){
			testStoryGeneratedScript.setTestScript(updatedScript);
			String message =testCaseStoryDAO.updateGeneratedScript(testStoryGeneratedScript);
			if(message.equalsIgnoreCase("success")){
				TestToolMaster testToolMaster = null;
				if (testToolName != null) {
					testToolMaster = testCategoryMasterDAO.getTestToolByName(testToolName);
				}
				testStoryGeneratedScript = testCaseStoryDAO.getGeneratedScripts(generatedScriptId,lang.getId(),testToolMaster.getTestToolId(),codeGenerationMode);
				if(!testToolMaster.getTestToolName().equalsIgnoreCase("EDAT") && testStoryGeneratedScript.getTestScript() != null){
					outputPackage = testStoryGeneratedScript.getTestScript().split(";");
					outputPackageName = outputPackage[0];
					if(outputPackageName != null && !outputPackageName.isEmpty()){
						outputPackageName = outputPackageName.substring(8, outputPackageName.length());
						log.info("output package is :"+outputPackageName);
					}
				}else if(testToolMaster.getTestToolName().equalsIgnoreCase("EDAT")){
					outputPackageName = "com.hcl.taf";
				}
				if (testStoryGeneratedScript != null) {
					String newGeneartedScript = testStoryGeneratedScript.getTestScript();
					newGeneartedScript = newGeneartedScript.replaceAll("(\\\\n\\\\r|\\\\n|\\\\r)", System.lineSeparator());
					testStoryGeneratedScript.setTestScript(newGeneartedScript);
					testStoryGeneratedScript.setModifiedBy(modifiedBy);
					testStoryGeneratedScript.setModifiedDate(new Date());
					testStoryGeneratedScript.setOutputPackage(outputPackageName);
					jsonTestStoryGeneratedScripts = new JsonTestStoryGeneratedScripts(testStoryGeneratedScript);		
				}

			}
		}

		return jsonTestStoryGeneratedScripts;
	}
	@Override
	public List<TestDataItems> listTestDataItemsByProductId(Integer productId,Integer testDataItemFilterId,Integer userId, Integer jtStartIndex, Integer jtPageSize) {
		return testCaseAutomationScriptDAO.listTestDataItemsByProductId(productId,testDataItemFilterId,userId,jtStartIndex,jtPageSize);
	}
	@Override
	public int totalTestDataItemsByProductId(Integer productId,Integer testDataFilterId, Integer userId) {
		return testCaseAutomationScriptDAO.totalTestDataItemsByProductId(productId,testDataFilterId,userId);
	}
	@Override
	@Transactional
	public List<TestDataPlan> listTestDataPlan(Integer productId) {
		List<TestDataPlan> testDataPlanList = new ArrayList<TestDataPlan>();
		TestDataPlan testDataPlan = new TestDataPlan();
		ProductMaster productMaster = new ProductMaster();
		productMaster.setProductId(productId);
		testDataPlanList = testCaseAutomationScriptDAO.listTestDataPlan(productId);
		if(testDataPlanList.size() == 0 || testDataPlanList == null || testDataPlanList.isEmpty()){
			UserList user = (UserList)request.getSession().getAttribute("USER");
			testDataPlan.setTestDataPlanName("SampleTDP");
			testDataPlan.setTestDataPlanDescription("SampleTDP");
			testDataPlan.setProductMaster(productMaster);
			testDataPlan.setCreatedOn(new Date());
			testDataPlan.setUserlist(user);
			Integer testDataPlanId = testCaseAutomationScriptDAO.addTestDataPlan(testDataPlan);
			if(testDataPlanId != null)
				testDataPlanList = testCaseAutomationScriptDAO.listTestDataPlan(productId);
		}

		return testDataPlanList;
	}
	@Override
	public int addTestDataItems(TestDataItems testDataItems) {
		return testCaseAutomationScriptDAO.addTestDataItems(testDataItems);
	}
	@Override
	public void updateTestDataItems(TestDataItems testDataItems) {
		testCaseAutomationScriptDAO.updateTestDataItems(testDataItems);

	}
	@Override
	public TestDataItems getTestDataItemByItemName(String testDataItemName,
			Integer productId, Integer userId) {
		return testCaseAutomationScriptDAO.getTestDataItemByItemName(testDataItemName,productId,userId);
	}
	@Override
	@Transactional
	public List<TestDataItemValues> listTestDataItemValuesByTestDataItemId(Integer testDataItemId, Integer jtStartIndex, Integer jtPageSize) {
		return testCaseAutomationScriptDAO.listTestDataItemValuesByTestDataItemId(testDataItemId,jtStartIndex,jtPageSize);
	}

	@Override
	@Transactional
	public int totalTestDataItemValuesByTestDataItemId(Integer testDataItemId) {
		return testCaseAutomationScriptDAO.totalTestDataItemValuesByTestDataItemId(testDataItemId);
	}
	@Override
	@Transactional
	public int addTestDataItemValues(TestDataItemValues testDataItemVal) {
		return testCaseAutomationScriptDAO.addTestDataItemValues(testDataItemVal);
	}

	@Override
	@Transactional
	public void updateTestDataItemValues(TestDataItemValues testDataItemVal) {
		testCaseAutomationScriptDAO.updateTestDataItemValues(testDataItemVal);
	}
	@Override
	@Transactional
	public TestDataItemValues getTestDataItemValuesByProductAndTestItemValName(Integer projectId, Integer userId, String testDataItemValue,Integer testDataItemId, Integer testDataPlanId) {
		return testCaseAutomationScriptDAO.getTestDataItemValuesByProductAndTestItemValName(projectId,userId,testDataItemValue,testDataItemId,testDataPlanId);
	}

	@Override
	@Transactional
	public int totalUIObjectItemsByProductId(Integer productId,Integer objRepoFilterId, Integer userId) {
		return testCaseAutomationScriptDAO.totalUIObjectItemsByProductId(productId,objRepoFilterId,userId);
	}
	@Override
	@Transactional
	public int addUIObjects(UIObjectItems uiObj) {
		return testCaseAutomationScriptDAO.addUIObjects(uiObj);
	}
	@Override
	@Transactional
	public void updateUIObjects(UIObjectItems uIObjectItems) {
		testCaseAutomationScriptDAO.updateUIObjects(uIObjectItems);
	}
	@Override
	@Transactional
	public UIObjectItems getUIObjectItemByElementName(String elementName,Integer productId, Integer userId) {
		return testCaseAutomationScriptDAO.getUIObjectItemByElementName(elementName,productId,null);
	}
	@Override
	@Transactional
	public String saveTestCaseAutomationScript(Integer testCaseId,Integer versionId, String automationScript, String selectedConfigFile) {

		try {
			log.info("Automation Script : " + automationScript);
			TestCaseAutomationStory testCaseAutomationStory = testCaseAutomationScriptDAO.getByTestCaseAutomationScriptId(testCaseId,versionId);
			TestCaseList testCase = new TestCaseList();

			if (automationScript == null)
				automationScript = "";
			testCase = testCaseListDAO.getByTestCaseId(testCaseId);
			if(testCase.getTestCaseName() != null)
				testCaseAutomationStory.setName(testCase.getTestCaseName());
			testCaseAutomationStory.setScript(automationScript);
			testCaseAutomationStory.setVersionId(versionId);
			testCase.setTestCaseId(testCaseId);
			testCaseAutomationStory.setTestCase(testCase);
			testCaseAutomationStory.setTestEngineConfigFile(selectedConfigFile);
			testCaseAutomationScriptDAO.updateTestCaseAutomationScript(testCaseAutomationStory);
			return "SUCCESS";
		} catch (Exception e) {
			log.error("Unable to Save Automation Story", e);
			return "FAILED : " + e.getMessage();
		}
	}
	@Override
	@Transactional
	public JsonTestCaseScript getTestCaseAutomationScript(Integer testCaseId, String scriptType, String testEngine, String productTypeName,String objectRepAttach, String testDataAttach,String amdocsMode,Integer projectId, HttpSession session) {
		if (testCaseId == null) {
			log.info("Testcase Id is not specified. Script cannot be sent");
			return null;
		}
		if (scriptType == null) {
			log.info("Script Type is not specified. Script cannot be sent");
			return null;
		}
		TestCaseList testCase = null;
		Integer maxVersionId;
		try {

			maxVersionId = testCaseAutomationScriptDAO.getMaxVersionIdByTestCaseId(testCaseId);
			testEngine = testEngine.toUpperCase();
			TestCaseAutomationStory story = null;
			if (maxVersionId != null) {
				story = testCaseAutomationScriptDAO.getTestAutomationScript(
						testCaseId, scriptType, maxVersionId, null);
			}
			if (story != null) {
				testCase = story.getTestCase();

				JsonTestCaseScript jsonTestScript = new JsonTestCaseScript(story, story.getTestCase());
				if (story.getScriptType().getScriptType().equalsIgnoreCase("GHERKIN")) {

					if (story.getScript() == null || story.getScript().trim().isEmpty()) {
						String defaultBDDScriptForNewStory = "Feature: " + testCase.getTestCaseName() + " story" + System.lineSeparator() +
								System.lineSeparator();
						defaultBDDScriptForNewStory = defaultBDDScriptForNewStory +"Scenario: " + testCase.getTestCaseName() + System.lineSeparator() + 
								System.lineSeparator() ;
						if(amdocsMode.equalsIgnoreCase("YES")){
							if(testCase.getTestCaseCode().equalsIgnoreCase("0")){
								defaultBDDScriptForNewStory = defaultBDDScriptForNewStory +	"Given ATT start new page "+testCase.getTestCaseName() + System.lineSeparator() ;
							}else if(testCase.getTestCaseCode().equalsIgnoreCase("1")){
								defaultBDDScriptForNewStory = defaultBDDScriptForNewStory +	"Given ATT start new test "+testCase.getTestCaseName() + System.lineSeparator() ;
							}else{
								defaultBDDScriptForNewStory = defaultBDDScriptForNewStory + "Given " + System.lineSeparator() ;
							}
						}else{
							defaultBDDScriptForNewStory = defaultBDDScriptForNewStory + "Given " + System.lineSeparator() ;
						}
						defaultBDDScriptForNewStory = defaultBDDScriptForNewStory +	"When " + System.lineSeparator() +
								"Then " + System.lineSeparator();
						jsonTestScript.setTestCaseScript(defaultBDDScriptForNewStory);
					} else {
						String defaultBDDScriptForNewStory = story.getScript();
						defaultBDDScriptForNewStory = defaultBDDScriptForNewStory.replaceAll("(\\\\n\\\\r|\\\\n|\\\\r)", System.lineSeparator());

						defaultBDDScriptForNewStory = defaultBDDScriptForNewStory.replaceAll("\\\\","");

						jsonTestScript.setTestCaseScript(defaultBDDScriptForNewStory);
					}
					jsonTestScript = this.loadKeywords(jsonTestScript, objectRepAttach, testDataAttach, productTypeName, testEngine,amdocsMode, projectId, session);
				}
				return jsonTestScript;
			} else {

				//Testcase Automation Story Entity does not exist for this. Create a new one
				TestCaseAutomationStory newStory = new TestCaseAutomationStory();
				testCase = testSuiteService.getByTestCaseId(testCaseId);
				if(testCase != null && testCase.getTestCaseName() != null){
					newStory.setName(testCase.getTestCaseName());
				}else{
					newStory.setName("New_Automation_Story");
				}
				newStory.setDescription("Auto Created Test Automation Story entry");
				newStory.setScriptSource("iLCM");
				newStory.setScriptFileName("");
				newStory.setScriptURI("");
				String defaultBDDScriptForNewStory = "Feature: " + testCase.getTestCaseName() + " story" + System.lineSeparator() +
						System.lineSeparator() ;
				defaultBDDScriptForNewStory = defaultBDDScriptForNewStory +	"Scenario: " + testCase.getTestCaseName() + System.lineSeparator() ;
				if(amdocsMode.equalsIgnoreCase("YES")){
					if(testCase.getTestCaseCode().equalsIgnoreCase("0")){
						defaultBDDScriptForNewStory = defaultBDDScriptForNewStory +	"Given ATT start new page "+testCase.getTestCaseName() + System.lineSeparator() ;
					}else if(testCase.getTestCaseCode().equalsIgnoreCase("1")){
						defaultBDDScriptForNewStory = defaultBDDScriptForNewStory +	"Given ATT start new test "+testCase.getTestCaseName() + System.lineSeparator() ;
					}else{
						defaultBDDScriptForNewStory = defaultBDDScriptForNewStory + "Given " + System.lineSeparator() ;
					}
				}else{
					defaultBDDScriptForNewStory = defaultBDDScriptForNewStory + "Given " + System.lineSeparator() ;
				}		
				defaultBDDScriptForNewStory = defaultBDDScriptForNewStory +	"When " + System.lineSeparator() +
						"Then " + System.lineSeparator();
				newStory.setScript(defaultBDDScriptForNewStory);


				testCase = testCaseListDAO.getByTestCaseId(testCaseId);
				if (testCase != null) {
					newStory.setTestCase(testCase);
				} else {
					log.info("Unable to get Automation Script for Testcase : " + testCaseId);
					return null;
				}

				ScriptTypeMaster scriptTypeObject = null;

				if (scriptType != null && !scriptType.equalsIgnoreCase("BDD")) {
					scriptTypeObject = scriptTypeMasterDAO.getScriptTypeMasterByscriptType(scriptType);
				} else {
					scriptTypeObject = scriptTypeMasterDAO.getScriptTypeMasterByscriptType("GHERKIN");

				}
				if (scriptTypeObject != null)
					newStory.setScriptType(scriptTypeObject);


				if (testEngine != null) {
					TestToolMaster testToolObject = testCategoryMasterDAO.getTestToolByName(testEngine);
					if (testToolObject != null)
						newStory.setTestTool(testToolObject);
				}
				//TODO : Add User name

				newStory.setCreatedDate(new Date(System.currentTimeMillis()));
				newStory.setModifiedDate(new Date(System.currentTimeMillis()));
				Integer versionId = 1;
				newStory.setVersionId(versionId);
				//setting productid to story
				if(testCase.getProductMaster().getProductId() != null)
					newStory.setProductId(testCase.getProductMaster().getProductId());
				Integer testCaseIdFromDB =  testCaseAutomationScriptDAO.addTestCaseAutomationScript(newStory);
				if(testCaseIdFromDB != null)
					newStory = testCaseAutomationScriptDAO.getByTestCaseAutomationScriptId(testCaseIdFromDB,versionId);

				JsonTestCaseScript jsonTestScript = new JsonTestCaseScript(newStory, newStory.getTestCase());
				if (newStory.getScriptType().getScriptType().equalsIgnoreCase("GHERKIN")) {
					jsonTestScript = loadKeywords(jsonTestScript, objectRepAttach, testDataAttach, productTypeName,testEngine, amdocsMode,projectId, session);
				}
				return jsonTestScript;
			}
		} catch (Exception e) {
			log.error("Unable to get Automation Script for Testcase : " + testCaseId, e);
			return null;
		}
	}
	@Override
	@Transactional
	public String generateBDDTestCaseAutomationScriptFiles(Integer id, String scriptsFor, String scriptType, Integer versionId, String testEngine, String destinationDirectory) {

		try {
			if (!scriptType.equals("GHERKIN")) {
				log.info("Only GHERKIN Stories download supported currently");
				return "Failed : Only GHERKIN Stories download supported currently";
			}

			if (id == null) {
				log.info("GHERKIN Stories : Unable to download stories as id is null");
				return "Failed : GHERKIN Stories : Unable to download stories as id is null";
			}

			ProductMaster product = null;
			TestSuiteList testSuite = null;
			TestCaseList testCase = null;
			if (scriptsFor.equalsIgnoreCase("Product")) {
			} else if (scriptsFor.equalsIgnoreCase("TestSuite")) {
				testSuite = testSuiteService.getByTestSuiteId(id);
			} else if (scriptsFor.equalsIgnoreCase("TestCase")) {
				testCase = testSuiteService.getByTestCaseIdBare(id);
			}

			String message = "";

			//Get all the test cases specified. If a test suite is specified, get only those test cases.
			List<TestCaseList> allTestCases = new ArrayList<TestCaseList>();
			if (scriptsFor.equalsIgnoreCase("Product")) {
			} else if (scriptsFor.equalsIgnoreCase("TestSuite")) {
			} else if (scriptsFor.equalsIgnoreCase("TestCase")) {
				allTestCases.add(testCase);
			}
			log.info("Total test Cases for the " + scriptsFor +  " : id " + id + " : " + allTestCases.size());
			if (allTestCases.isEmpty()) {
				return "Failed : No Testcases are available for " + scriptsFor;
			}
			Set<TestCaseList> allTestCasesSet = new HashSet<TestCaseList>(allTestCases);
			if(versionId.equals(-1))
				versionId = testCaseAutomationScriptDAO.getMaxVersionIdByTestCaseId(id);
			return generateBDDTestCaseAutomationScriptFiles(allTestCasesSet, scriptType,versionId, testEngine, destinationDirectory);

		} catch (Exception e) {
			log.error("Unable to generate test automation scripts for some reason", e);
			return "Failed : Unable to generate test scripts due to some reason";
		}
	}
	@Override
	@Transactional
	public String generateBDDTestCaseAutomationScriptFiles(Set<TestCaseList> testCases, String scriptType, Integer versionId, String testEngine, String destinationDirectory) {

		return generateBDDTestCaseAutomationScriptFiles(testCases, scriptType,versionId, testEngine, destinationDirectory, null);
	}
	@Override
	@Transactional
	public String generateBDDTestCaseAutomationScriptFiles(Set<TestCaseList> testCases, String scriptType, Integer versionId, String testEngine, String destinationDirectory, TestRunPlan testRunPlan) {

		try {

			if (testCases == null|| testCases.isEmpty()) {
				return "Failed : No Testcases specified";
			}
			if (destinationDirectory == null || destinationDirectory.trim().isEmpty()) {
				log.info("Destination Directory : " + destinationDirectory);
			}

			//Write all Scripts to a temp folder for downloading
			for (TestCaseList testCaseObject : testCases) {

				//Get the BDD Automation Script from DB
				TestCaseAutomationStory script = testCaseAutomationScriptDAO.getTestAutomationScript(testCaseObject.getTestCaseId(), scriptType, versionId, testEngine);
				//Write the Automation script content to a file
				if (script != null) {
					String fileName = "";
					if(null != testEngine && !testEngine.isEmpty()/* && !testEngine.equalsIgnoreCase("EDAT")*/){
						fileName = writeAutomationScriptToFile(script, destinationDirectory);
					} 
					log.info("Wrote Test Script to file : " + fileName);
				} else {
					log.info("No BDD script for test case : " + testCaseObject.getTestCaseId());
				}
			}

			if(null != testEngine && !testEngine.isEmpty() && !testEngine.equalsIgnoreCase("EDAT")){
				if(testRunPlan!=null){
					Attachment objectRepositoryAtt = testRunPlan.getObjectRepository();
					if(objectRepositoryAtt!=null){
						String objectRepositoryFile = objectRepositoryAtt.getAttributeFileURI();
						log.info("Object Repository Path: " + objectRepositoryFile);		    	

						if (objectRepositoryFile != null) {
							File repositoryFile = new File(objectRepositoryFile);
							if (repositoryFile.exists()) {

								Path FROM = Paths.get(objectRepositoryFile);
								Path TO = Paths.get(destinationDirectory);			
								Files.copy(FROM, TO.resolve(FROM.getFileName()), StandardCopyOption.REPLACE_EXISTING);			
								log.debug("Copied Object Repository File to bundle");
							} else {
								log.debug("Object Repository File does not exist : ");
							}
						}
					}


					Attachment testDataAtt = testRunPlan.getTestData();
					if(testDataAtt!=null){
						String testDataFile = testDataAtt.getAttributeFileURI();
						log.info("Object Repository Path: " + testDataFile);		    	

						if (testDataFile != null) {
							File repositoryFile = new File(testDataFile);
							if (repositoryFile.exists()) {

								Path FROM = Paths.get(testDataFile);
								Path TO = Paths.get(destinationDirectory);			
								Files.copy(FROM, TO.resolve(FROM.getFileName()), StandardCopyOption.REPLACE_EXISTING);			
								log.debug("Copied Testdata File to bundle");
							} else {
								log.debug("Testdata File does not exist : ");
							}
						}
					}				
				}
			}

			String zipFilePath = ZipTool.dozip(destinationDirectory);
			if (ZipTool.isValidZipArchive(zipFilePath)) {
				log.info("Zip file created" + zipFilePath);
			} else {
				return "Failed : Unable to create a Zip File for Automation Scripts";
			}
			return zipFilePath;

		} catch (Exception e) {
			log.error("Unable to generate test automation scripts for some reason", e);
			return "Failed : Unable to generate test scripts due to some reason";
		}

	}
	private String writeEDatAutomationScriptToFile(TestCaseAutomationStory script, String destinationDirectory) {
		String scriptFileName = "";
		if (script.getScriptFileName() == null || script.getScriptFileName().trim().isEmpty()) {
			scriptFileName = ScriptGeneratorUtilities.getTestCaseClassName((String)script.getTestCase().getTestCaseName(), (int)0, (String)"", (int)0) + ".txt";
		} else {
			scriptFileName = script.getScriptFileName();
		}
		String fullFileName = destinationDirectory + File.separator+ scriptFileName;
		try {
			File destinationDir = new File(destinationDirectory);						
			if (!destinationDir.exists()) {
				destinationDir.mkdirs();		
			}

			File scriptFile = new File(fullFileName);						
			if (!scriptFile.exists()) {				
				scriptFile.createNewFile();
			}

			FileWriter fw = new FileWriter(scriptFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			String userEDatScriptStory = script.getScript();

			//Arun for edat
			userEDatScriptStory = userEDatScriptStory.replaceAll("(\\\\n\\\\r|\\\\n|\\\\r)", System.lineSeparator());

			String testCaseName = ScriptGeneratorUtilities.getTestCaseClassName(script.getTestCase().getTestCaseName(), (int)0, (String)"", (int)0);			
			bw.write(userEDatScriptStory);			
			bw.close();
			return fullFileName;
		} catch (Exception e) {
			log.error("Unable to write Script to File : " + fullFileName, e );
			return null;
		}
	}


	@Override
	@Transactional
	public String generateBDDTestCaseAutomationScriptFiles(Integer id, String scriptsFor, String scriptType, String testEngine) {

		ProductMaster product = null;
		TestSuiteList testSuite = null;
		TestCaseList testCase = null;
		String catalinaHome = System.getProperty("catalina.home");
		String destinationDirectory = "";		
		if (scriptsFor.equalsIgnoreCase("Product")) {
		} else if (scriptsFor.equalsIgnoreCase("TestSuite")) {
			destinationDirectory = catalinaHome+File.separator+testScriptsDestinationDirectory + File.separator  +AOTCConstants.ENTITY_TEST_SUITE+File.separator+ id;
			testSuite = testSuiteService.getByTestSuiteId(id);
		} else if (scriptsFor.equalsIgnoreCase("TestCase")) {
			destinationDirectory = catalinaHome+File.separator+testScriptsDestinationDirectory + File.separator  +AOTCConstants.ENTITY_TEST_CASE+File.separator+ id;	
			testCase = testSuiteService.getByTestCaseIdBare(id);

		}
		log.info("Destination Directory : " + destinationDirectory);

		return generateBDDTestCaseAutomationScriptFiles(id, scriptsFor, scriptType, -1, testEngine, destinationDirectory);
	}

	@Override
	@Transactional
	public TestCaseAutomationStory getTestEngineConfigFile(Integer testCaseId, String scriptType, Integer versionId, String testEngine){
		versionId = testCaseAutomationScriptDAO.getMaxVersionIdByTestCaseId(testCaseId);
		return testCaseAutomationScriptDAO.getTestAutomationScript(testCaseId, scriptType, versionId, null);
	}
	@Override
	@Transactional
	public void addScriptGenerationDetails(ScriptGenerationDetails scriptDetails) {
		this.testCaseAutomationScriptDAO.addScriptGenerationDetails(scriptDetails);
	}
	@Override
	@Transactional
	public List<String> listUIObjectItemHandleNamesByProductId(Integer productId, Integer userId) {
		return testCaseAutomationScriptDAO.listUIObjectItemHandleNamesByProductId(productId,userId);
	}

	@Override
	@Transactional
	public List<UIObjectItems> listUIObjectItemsByHandleName(Integer productId,Integer userId, String handle) {
		return testCaseAutomationScriptDAO.listUIObjectItemsByHandleName(productId,userId,handle);
	}

	@Override
	@Transactional
	public List<UIObjectItems> listUIObjectItemsByProductId(Integer productId,Integer objRepoFilterId,Integer userId,Integer jtStartIndex, Integer jtPageSize) {
		return testCaseAutomationScriptDAO.listUIObjectItemsByProductId(productId,objRepoFilterId,userId,jtStartIndex,jtPageSize);
	}
	@Override
	@Transactional
	public List<String> listTesDataItemHandleNamesByProductId(Integer productId, Integer userId) {
		return testCaseAutomationScriptDAO.listTesDataItemHandleNamesByProductId(productId,userId);
	}
	@Override
	@Transactional
	public Integer listTestDataItemValuesCountByProductIdAndUserId(Integer productId, Integer userId) {

		return testCaseAutomationScriptDAO.getTestDataItemValuesCountByProductIdAndUserId(productId,userId);
	}
	@Override
	@Transactional
	public List<TestDataItems> listTestDataItemValuesByProductAndTestDataPlanAndHandle(Integer productId, String handle, Integer testDataPlanId,Integer userId) {
		return testCaseAutomationScriptDAO.listTestDataItemValuesByProductAndTestDataPlanAndHandle(productId,handle,testDataPlanId,userId);
	}

	private String generateScriptFile(JsonTestStoryGeneratedScripts generatedScripts, String destinationDirectory, String testStoryName){
		String fileName = null;
		String scriptFileName = null;
		String scriptFolderPath = null;
		String scriptFileInFolderPath = null;
		String languageName = null;
		File scriptFilePath ;
		log.info("Story name : "+ testStoryName +" : dest place : "+destinationDirectory);
		try{
			String outputPackage = generatedScripts.getOutputPackage();
			if(outputPackage != null)
				outputPackage = outputPackage.replace(".",File.separator);
			scriptFolderPath = destinationDirectory + File.separator + "TestScripts" + File.separator + outputPackage;
			scriptFilePath = new File(scriptFolderPath);
			if (!scriptFilePath.exists()) {
				scriptFilePath.mkdirs();		
			}
			Integer languageId = generatedScripts.getLanguageId();
			languageName = ".java";
			switch(languageId){
			case 1:
				break;
			case 2:
				break;
			case 3:
				languageName = ".js";
				break;
			case 4:
				languageName = ".py";
				break;				
			case 5:
				languageName = ".ps1";
				break;
			case 6:
				languageName = ".cs";					
				break;
			case 7:
				languageName = ".c";
				break;
			case 8:
				languageName = ".js";
				break;				
			}

			PrintWriter pw = null;
			//5077 Fix
			testStoryName =  testStoryName.replace(" ", "_");
			fileName = destinationDirectory+ File.separator + testStoryName+languageName;
			scriptFileInFolderPath = scriptFolderPath + File.separator + testStoryName+languageName;
			scriptFileName = testStoryName + languageName;
			log.info("Generated Script File Name : "+ fileName);
			try {
				pw = new PrintWriter(new File(fileName));
				pw.println(generatedScripts.getTestScript());
			} catch (FileNotFoundException e) {
				log.error("Could not find file for generated Testcase import statements : "+ fileName, e);
			} finally {
				if (pw != null) {
					pw.close();
				}
			}
		} catch(Exception e){
			log.error("Error while generating script file ", e);
		}
		if(new File(fileName).exists()){
			try {
				FileUtils.copyFile(new File(fileName),new File(scriptFileInFolderPath));
			} catch (IOException e) {
				log.error("Error while copying script file"+ scriptFileInFolderPath, e);
			}
		}
		return scriptFileName;
	}
	private String writeAutomationStoryToFile(TestCaseAutomationStory story, String destinationDirectory) {

		String storyName = "";
		if (story.getScriptFileName() == null || story.getScriptFileName().trim().isEmpty()) {
			storyName = ScriptGeneratorUtilities.getTestCaseClassName((String)story.getTestCase().getTestCaseName(), (int)0, (String)"", (int)0) + ".story";
		} else {
			storyName = story.getName();
		}

		String storiesFolderPath = destinationDirectory + File.separator + "TestStories";
		String fullFileName = storiesFolderPath + File.separator+ storyName;

		try {

			File destinationStoriesDirectory = new File(storiesFolderPath);
			if (!destinationStoriesDirectory.exists()) {
				destinationStoriesDirectory.mkdirs();		
			}

			File scriptFile = new File(fullFileName);						
			if (!scriptFile.exists()) {				
				scriptFile.createNewFile();
			}

			FileWriter fw = new FileWriter(scriptFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			String userBDDScriptStory = story.getScript();
			String testCaseName = ScriptGeneratorUtilities.getTestCaseClassName(story.getTestCase().getTestCaseName(), (int)0, (String)"", (int)0); 

			String[] lines = userBDDScriptStory.split("\\\\n\\\\r|\\\\n|\\\\r");
			StringBuilder finalStringBuilder = new StringBuilder();
			for(String s:lines){
				if(!s.equals("")){
					if(s.trim().startsWith("Feature")){
						s = "";
						s = "Feature: " + testCaseName + " story" + System.lineSeparator() 
								+  "Meta: " + System.lineSeparator()
								+ "@storyname " + testCaseName + System.lineSeparator(); 
						if(story.getTestCase().getTestCaseDescription() != null ){
							s = s	+ "@storydesc " + story.getTestCase().getTestCaseDescription() + System.lineSeparator() ;	
						}else{
							s = s	+ "@storydesc " + "" + System.lineSeparator() ;
						}


					}

					if(s.trim().startsWith("Meta") || s.trim().startsWith("@storyname") || s.trim().startsWith("@storydesc") || s.trim().startsWith("@storytype")  ){
						s = "";
					}
					if(s != null && s != ""){
						finalStringBuilder.append(s.trim()).append(System.getProperty("line.separator"));
					}
				}
			}
			userBDDScriptStory = "";
			userBDDScriptStory = finalStringBuilder.toString();

			userBDDScriptStory = userBDDScriptStory.replaceAll("(\\\\n\\\\r|\\\\n|\\\\r)", System.lineSeparator());
			bw.write(userBDDScriptStory);			
			bw.close();
			return fullFileName;
		} catch (Exception e) {
			log.error("Unable to write Story to File : " + fullFileName, e );
			return null;
		}
	}

	private String generateScriptFileForDownload(JsonTestStoryGeneratedScripts generatedScripts, String destinationDirectory, String testStoryName){
		String fileName = null;
		String scriptFileName = null;
		String scriptFolderPath = null;
		boolean isGenericSciptAvailable = false;
		log.info("inside generateScriptFileForDownload method ....!");
		 Map<String, List<String>> scriptForModesMap = new HashMap<String, List<String>>();
		 List<String> genericModeScriptList = new ArrayList<String>();
		 List<String> tafModeScriptList = new ArrayList<String>();
		 if(generatedScripts.getOutputPackage() != null && generatedScripts.getTestScript() != null){
			 tafModeScriptList.add((generatedScripts.getOutputPackage()));
			 tafModeScriptList.add((generatedScripts.getTestScript()));
		 }
		  if(generatedScripts.getOutputpackageForGeneric() != null && generatedScripts.getTestScriptForGeneric() != null){
			 genericModeScriptList.add((generatedScripts.getOutputpackageForGeneric()));
			 genericModeScriptList.add((generatedScripts.getTestScriptForGeneric()));
			 isGenericSciptAvailable = true;
		 }
		  scriptForModesMap.put("TAF",tafModeScriptList );
		  if(isGenericSciptAvailable)
		  scriptForModesMap.put("GENERIC",genericModeScriptList );
		  
		  for (Map.Entry<String, List<String>> entry : scriptForModesMap.entrySet()) {
	            String mode = entry.getKey();
	            List<String> outputPackageAndScriptList = entry.getValue();
	    		try{
	    			String outputPackage = outputPackageAndScriptList.get(0);
	    			if(outputPackage != null)
	    				outputPackage = outputPackage.replace(".",File.separator);
	    			scriptFolderPath = destinationDirectory + File.separator +"TestScripts"+File.separator+outputPackage;
	    			File scriptFilePath = new File(scriptFolderPath);
	    			if (!scriptFilePath.exists()) {
	    				scriptFilePath.mkdirs();		
	    			}

	    			Integer languageId = generatedScripts.getLanguageId();
	    			String languageName = ".java";
	    			switch(languageId){
	    			case 1:
	    				break;
	    			case 2:
	    				break;
	    			case 3:
	    				languageName = ".js";
	    				break;
	    			case 4:
	    				languageName = ".py";
	    				break;				
	    			case 5:
	    				languageName = ".ps1";
	    				break;
	    			case 6:
	    				languageName = ".cs";					
	    				break;
	    			case 7:
	    				languageName = ".c";
	    				break;
	    			case 8:
	    				languageName = ".js";
	    				break;				
	    			}

	    			PrintWriter pw = null;
	    			fileName = scriptFolderPath + File.separator + testStoryName + languageName;
	    			scriptFileName = testStoryName + languageName;
	    			log.info("Generated Script File Name : "+ fileName);
	    			try {
	    				pw = new PrintWriter(new File(fileName));
	    				pw.println(outputPackageAndScriptList.get(1));
	    			} catch (FileNotFoundException e) {
	    				log.error("Could not find file for generated Testcase import statements : "+ fileName, e);
	    			} finally {
	    				if (pw != null) {
	    					pw.close();
	    				}
	    			}
	    		} catch(Exception e){
	    			log.error("Error while generating script file ", e);
	    		}
	        }

		return fileName;
	}
	@Override
	@Transactional
	public String downloadStoryScriptBundle(Map<String, String> queryMap) {
		log.info("Downloading Story and Script Bundle");
		Integer productId = null , testSuiteId = null;		
		String bundleFileName = null;
		List<TestCaseList> testCases = new ArrayList<TestCaseList>();
		List<TestCaseAutomationStory> stories = new ArrayList<TestCaseAutomationStory>();
		String catalinaHome = System.getProperty("catalina.home");
		String destinationDirectory = catalinaHome + File.separator + "webapps" +request.getContextPath()+ File.separator + "TestScripts" + File.separator + "StoryScriptBundles";
		File destinationDir = new File(destinationDirectory);						
		try{
			if (!destinationDir.exists()) {
				destinationDir.mkdirs();		
			}
			FileUtils.cleanDirectory(destinationDir);
			if( queryMap.get("productId") != null )
				productId = Integer.valueOf(queryMap.get("productId"));
			if( queryMap.get("suiteId") != null )
				testSuiteId = Integer.valueOf(queryMap.get("suiteId"));

			if( productId != null && productId != -1) {
				stories = testCaseAutomationScriptDAO.listTestAutomationStories(productId);
			}			

			if( testSuiteId != null && testSuiteId != -1)
				testCases =  testCaseListDAO.listTestCases(testSuiteId);

			bundleFileName = generateStoryScriptBundles(productId,stories, testCases , "GHERKIN", null, destinationDirectory);
			File bundleFile = new File(bundleFileName);
			String fileName = bundleFile.getName();
			bundleFileName = "http://" + request.getServerName() + ":" + request.getServerPort() +request.getContextPath() +"/TestScripts/StoryScriptBundles/" + fileName;  
		}catch(IOException ie){
			log.error("Unable to clean the directory StoryScriptDownload...!"+ie.getMessage());
		}catch(Exception e){
			log.error("Failed to download the bundle due to "+e.getMessage());
		}

		return bundleFileName;
	}
	//New method
	@Override
	@Transactional
	public String generateStoryScriptBundles(Integer productId,List<TestCaseAutomationStory> stories , List<TestCaseList> testCases, String scriptType, String testEngine, String destinationDirectory) {
		try {
			log.info("inside generatestoryscript bundles method ....!");
			TestCaseList testCaseList = new TestCaseList();
			Integer maxVersionId = 1;
			if ((stories == null || stories.isEmpty()) && (testCases == null|| testCases.isEmpty())) {
				return null;
			}
			if (destinationDirectory == null || destinationDirectory.trim().isEmpty()) {
				log.info("Destination Directory is not available");
			} else {
					log.info("Destination Directory : " + destinationDirectory);
			}

			destinationDirectory = destinationDirectory + File.separator + productId ;
			if(stories != null && !stories.isEmpty()) {
				for(TestCaseAutomationStory story : stories) {
					testCaseList = testCaseListDAO.getByTestCaseId(story.getTestCase().getTestCaseId());
					if(!testCaseList.getStatus().equals(2)){
						productId = story.getProductId();
						log.info("TestCaseId :"+testCaseList.getTestCaseId()+ " productid :"+testCaseList.getProductMaster().getProductId());
						prepareStoryAndScript(story, destinationDirectory);
					}
				}
			} else if(testCases != null && !testCases.isEmpty()) {
				//Write all Scripts to a temp folder for downloading
				for (TestCaseList testCaseObject : testCases) {
					productId = testCaseObject.getProductMaster().getProductId();
					//Get the latest version of BDD Automation Story from DB
					maxVersionId = testCaseAutomationScriptDAO.getMaxVersionIdByTestCaseId(testCaseObject.getTestCaseId());
					TestCaseAutomationStory story = testCaseAutomationScriptDAO.getTestAutomationScript(testCaseObject.getTestCaseId(), scriptType, testEngine,maxVersionId);
					prepareStoryAndScript(story, destinationDirectory);
				}
			}
			String zipFilePath = ZipTool.dozip(destinationDirectory);
			if (ZipTool.isValidZipArchive(zipFilePath)) {
				log.info("Zip file created" + zipFilePath);
			} else {
				return "Failed : Unable to create a Zip File for Automation Scripts";
			}
			return zipFilePath;
		} catch (Exception e) {
			log.error("Unable to generate test automation scripts for some reason", e);
			return "Failed : Unable to generate test scripts due to some reason";
		}
	}

	private void prepareStoryAndScript(TestCaseAutomationStory story, String destinationDirectory){
		try {
			//Write the Automation script content to a file
			if (story != null) {				
				//commenting writing story to file location
				String fileName = writeAutomationStoryToFile(story, destinationDirectory);
				log.info("inside prepareStoryAndScript method ....!");
				log.info("Wrote Test Script to file : " + fileName);				
				//Enhancement to attach generated scripts to the story bundle at server side only if the script is generated
				File destinationDir = new File(destinationDirectory);						
				if (!destinationDir.exists()) {
					destinationDir.mkdirs();		
				}

				JsonTestStoryGeneratedScripts testStoryGeneratedScript = testCaseScriptGenerationService.getGeneratedScriptById(story.getTestCaseAutomationStoryId());
				if(testStoryGeneratedScript == null){
					return;
				}
				if(testStoryGeneratedScript != null){
					String generatedScriptFileName = generateScriptFileForDownload(testStoryGeneratedScript, destinationDirectory, story.getName());					
					log.info("Wrote Generated Script to destination : " + generatedScriptFileName);
				}
			}
		} catch(Exception e) {
			log.error("Unable to prepare story and script bundle"+e);
		}
	}
	@Override
	@Transactional
	public JsonTestStoryGeneratedScripts getGeneratedScriptById(
			Integer testCaseStoryId) {
		JsonTestStoryGeneratedScripts testStoryGeneratedScripts = null;
		if (testCaseStoryId == null) {
			log.info("Testcase Story ID has not been specified. Scripts cannot be sent");
			return null;
		}

		try {

			TestStoryGeneratedScripts testStoryGeneratedScript = testCaseStoryDAO.getGeneratedScripts(testCaseStoryId);
			if (testStoryGeneratedScript != null) {
				String newGeneartedScript = testStoryGeneratedScript.getTestScript();
				testStoryGeneratedScript.setTestScript(newGeneartedScript);
				testStoryGeneratedScripts = new JsonTestStoryGeneratedScripts(testStoryGeneratedScript);		
			}



		} catch (Exception e) {
			log.error("Unable to get generated Scripts List for Teststory : " + testCaseStoryId, e);
			return null;
		}
		return  testStoryGeneratedScripts;
	}
	private void writepackageWithClassNamesToTextFile(String destinationDirectory,String outputpackagesWithClassnames) {
		PrintWriter pw = null;
		String fileName = null;
		fileName = destinationDirectory+ File.separator + "TestScriptDetails"+".txt";
		log.info("Destination directory is :"+destinationDirectory+" outputpackages :"+outputpackagesWithClassnames);
		try {
			pw = new PrintWriter(new File(fileName));
			pw.println(outputpackagesWithClassnames);
		} catch (FileNotFoundException e) {
			log.error("Could not find file for generated Testcase import statements : "+ fileName, e);
		} finally {
			if (pw != null) {
				pw.close();
			}
		}

	}
	@Override
	@Transactional
	public String saveTestCaseAutomationStory(Integer testCaseId,Integer versionId, String automationScript, String selectedConfigFile, String testEngine) {

		try {
			log.info("Automation Script : " + automationScript);
			TestCaseAutomationStory testCaseAutomationStory = testCaseAutomationScriptDAO.getByTestCaseAutomationScriptId(testCaseId,versionId);
			TestCaseList testCase = new TestCaseList();
			if (automationScript == null)
				automationScript = "";
			testCase = testCaseListDAO.getByTestCaseId(testCaseId);
			if(testCase.getTestCaseName() != null)
				testCaseAutomationStory.setName(testCase.getTestCaseName());
			testCaseAutomationStory.setScript(automationScript);
			testCaseAutomationStory.setVersionId(versionId);
			testCase.setTestCaseId(testCaseId);
			testCaseAutomationStory.setTestCase(testCase);
			testCaseAutomationStory.setTestEngineConfigFile(selectedConfigFile);
			testCaseAutomationStory.setTestTool(testCategoryMasterDAO.getTestToolByName(testEngine));
			testCaseAutomationScriptDAO.updateTestCaseAutomationScript(testCaseAutomationStory);
			return "SUCCESS";
		} catch (Exception e) {
			log.error("Unable to Save Automation Story", e);
			return "FAILED : " + e.getMessage();
		}
	}


	@Override
	@Transactional
	public JsonTestStoryGeneratedScripts getAutomationScript(
			Integer testCaseStoryId, String languageName, String testEngine,
			String codeGenerationMode) {


		JsonTestStoryGeneratedScripts jsonTestStoryGeneratedScripts = null;
		if (testCaseStoryId == null) {
			log.info("Testcase Story ID has not been specified. Scripts cannot be sent");
			return null;
		}

		try {
			TestToolMaster testToolMaster = null;
			if (testEngine != null) {
				testToolMaster = testCategoryMasterDAO.getTestToolByName(testEngine);
			}
			Languages lang = languagesDAO.getLanguageByName(languageName);
			TestStoryGeneratedScripts testStoryGeneratedScript = testCaseStoryDAO.getGeneratedScripts(testCaseStoryId,lang.getId(),testToolMaster.getTestToolId(),codeGenerationMode);
			if (testStoryGeneratedScript != null) {
				String newGeneartedScript = testStoryGeneratedScript.getTestScript();
				testStoryGeneratedScript.setTestScript(newGeneartedScript);
				jsonTestStoryGeneratedScripts = new JsonTestStoryGeneratedScripts(testStoryGeneratedScript);		
			}



		} catch (Exception e) {
			log.error("Unable to get generated Scripts List for Teststory : " + testCaseStoryId, e);
			return null;
		}
		return  jsonTestStoryGeneratedScripts;

	}

	@Override
	@Transactional
	public JsonTestStoryGeneratedScripts getGeneratedScriptByIdAndTAFMode(
			Integer testCaseStoryId,String codeGenerationMode) {
		JsonTestStoryGeneratedScripts testStoryGeneratedScripts = null;
		if (testCaseStoryId == null) {
			log.info("Testcase Story ID has not been specified. Scripts cannot be sent");
			return null;
		}

		try {

			TestStoryGeneratedScripts testStoryGeneratedScript = testCaseStoryDAO.getGeneratedScripts(testCaseStoryId);
			if (testStoryGeneratedScript != null) {
				String newGeneartedScript = testStoryGeneratedScript.getTestScript();
				testStoryGeneratedScript.setTestScript(newGeneartedScript);
				testStoryGeneratedScripts = new JsonTestStoryGeneratedScripts(testStoryGeneratedScript);		
			}



		} catch (Exception e) {
			log.error("Unable to get generated Scripts List for Teststory : " + testCaseStoryId, e);
			return null;
		}
		return  testStoryGeneratedScripts;
	}


	@SuppressWarnings("unused")
	@Override
	@Transactional
	public List<String> addTestStepFilesByModeAndTestEngine(
			String codeGenerationMode, String testEngine,String scriptLanguage) {
		List<String> stepFiles = new ArrayList<String>();
		if(codeGenerationMode .equalsIgnoreCase("ATT-MODE")){
			if(stepFiles == null ){
				stepFiles = new ArrayList<String>();
			}
			stepFiles.add(0,"com.hcl.ers.atsg.script.utils.ATT_ATSG_Keywords_TestNG_Selenium");  
		}else{
			if(testEngine.equalsIgnoreCase("selenium")){
				if(stepFiles == null ){
					stepFiles = new ArrayList<String>();
				}
				stepFiles.add(0,"com.hcl.ers.atsg.script.utils.ATSG_Functions_TestNG_Selenium");
			}else if(testEngine.equalsIgnoreCase("appium")){
				if(stepFiles == null ){
					stepFiles = new ArrayList<String>();
				}
				stepFiles.add(0,"com.hcl.ers.atsg.script.utils.ATSG_Functions_TestNG_Appium");
			} else if(testEngine.equalsIgnoreCase("protractor")){
				if(stepFiles == null ){
					stepFiles = new ArrayList<String>();
				}
				stepFiles.add(0,"com.hcl.ers.atsg.script.utils.ATSG_Functions_JavaScript_Protractor");
			}else if(testEngine.equalsIgnoreCase("seetest")){
				if(stepFiles == null ){
					stepFiles = new ArrayList<String>();
				}
				stepFiles.add(0,"com.hcl.ers.atsg.script.utils.ATSG_Functions_TestNG_SeeTest");
			}else if(testEngine.equalsIgnoreCase("codedui")){
				if(stepFiles == null ){
					stepFiles = new ArrayList<String>();
				}
				stepFiles.add(0,"com.hcl.ers.atsg.script.utils.ATSG_Functions_CSharp_CodedUI");
			}else if(testEngine.equalsIgnoreCase("testcomplete")){
				if(stepFiles == null ){
					stepFiles = new ArrayList<String>();
				}
				stepFiles.add(0,"com.hcl.ers.atsg.script.utils.ATSG_Keywords_JScript_TestComplete");
			} else if(testEngine.equalsIgnoreCase("restassured")){
				if(stepFiles == null ){
					stepFiles = new ArrayList<String>();
				}
				stepFiles.add(0,"com.hcl.ers.atsg.script.utils.ATSG_Keywords_TestNG_RestService_RestAssured");
			} else if(testEngine.equalsIgnoreCase("edat")){
				if(stepFiles == null ){
					stepFiles = new ArrayList<String>();
				}
				if(scriptLanguage != null && scriptLanguage.equalsIgnoreCase("powershell")){
					stepFiles.add(0,"com.hcl.ers.atsg.script.utils.ATSG_Keywords_Powershell_eDAT");
				}else if(scriptLanguage != null && scriptLanguage.equalsIgnoreCase("python")){
					stepFiles.add(0,"com.hcl.ers.atsg.script.utils.ATSG_Keywords_Python_eDAT");
				}
				
			} else if(testEngine.equalsIgnoreCase("custom_cisco")){
				if(stepFiles == null ){
					stepFiles = new ArrayList<String>();
				}
				stepFiles.add(0,"com.hcl.ers.atsg.script.utils.ATSG_Keywords_Python_CustomCisco");
			}
		}
		return stepFiles;
	}


	@Override
	@Transactional
	public String checkInStoryToSVN(Integer id, String tcName, String data, String commitComment) {
		String SVNResponse = null;
		String response = "";
		ConnectorCredentials connectorCredentials = new ConnectorCredentials();
		try{		
			//Committing the story file to SVN
			String scriptTOSVN = new String(data);
			TestCaseList tc = testCaseService.getTestCaseById(id);
			if(tc != null){
				if(tc.getProductMaster() != null){
					int productId = tc.getProductMaster().getProductId();
					List<SCMSystem> scmManagementSystems=defectManagementService.listSCMManagementSystem(productId);
					for(SCMSystem scmSystem : scmManagementSystems){
						if(scmSystem.getIsPrimary() != null && scmSystem.getIsPrimary() == 1){
							String pwd  = passwordEncryptionService.decrypt(scmSystem.getConnectionPassword().trim());
							if(scmSystem.getToolIntagration() != null && scmSystem.getToolIntagration().getId() != null 
									&& scmSystem.getToolIntagration().getId() == 7) {
								SVNResponse = connectorCredentials.commitNewFileToSVN(scmSystem.getConnectionProperty1(), scmSystem.getConnectionUserName(), pwd, tcName, scriptTOSVN.replaceAll("(\\\\n\\\\r|\\\\n|\\\\r)", System.lineSeparator()), null, commitComment);
								SVNResponse = scmSystem.getTitle()+" : " + SVNResponse;
								response = response + SVNResponse + "<br>";
							} else if (scmSystem.getToolIntagration() != null && scmSystem.getToolIntagration().getId() != null 
									&& scmSystem.getToolIntagration().getId() == 8){
								
								String authorName = "";
								String authorMail = "";
								
								if(scmSystem.getConnectionUri() == null || scmSystem.getConnectionUri().isEmpty()){
									response = "Invalid Connection URI, so failed to connect and commit";
									return response;
								}
								
								if(scmSystem.getConnectionProperty1() == null || scmSystem.getConnectionProperty1().isEmpty()) {
									response = "Invalid Local Repo Path, so failed to connect and commit";
									return response;
								}
								
								if(scmSystem.getConnectionUserName() == null || scmSystem.getConnectionUserName().isEmpty()) {
									response = "Invalid Username, so failed to connect and commit";
									return response;
								}
								
								if(scmSystem.getConnectionPassword() == null || scmSystem.getConnectionPassword().isEmpty()) {
									response = "Invalid Password, so failed to connect and commit";
									return response;
								}
								
								if(scmSystem.getConnectionProperty2() == null || scmSystem.getConnectionProperty2().isEmpty())
									authorName = "admin";
								
								if(scmSystem.getConnectionProperty3() == null || scmSystem.getConnectionProperty3().isEmpty())
									authorMail = "admin";
								
								String remoteRepoPath = scmSystem.getConnectionUri();
								//Conection Property 1 is used for Local GIT Repository
								String localRepositoryPath = scmSystem.getConnectionProperty1();
								String username = scmSystem.getConnectionUserName();
								String password = passwordEncryptionService.decrypt(scmSystem.getConnectionPassword());
								authorName = scmSystem.getConnectionProperty2();
								authorMail = scmSystem.getConnectionProperty3();								
								
								//Creating and establishing GIT Connector
								GitConnector gitConnector = new GitConnector();
								
								if(!new File(localRepositoryPath).exists()){
									gitConnector.cloneGitRepository(remoteRepoPath, localRepositoryPath, username, password);
								} 
								
								File localRepoTestFile = new File(localRepositoryPath + File.separator + tcName);
								FileWriter fileWriter = new FileWriter(localRepoTestFile);
								fileWriter.write(scriptTOSVN);
								fileWriter.flush();
								fileWriter.close();
								
								boolean isSuccess = gitConnector.commitFile(localRepositoryPath, username, password, 
										localRepoTestFile , commitComment,authorName,authorMail);
								
								if(isSuccess)
									response = "Success: Successfully committed into GIT." + "<br>";								
								else
									response = "Failed to commit into GIT." + "<br>";
								
								log.info("Committed file to GIT Remote Repo");								
							}
						}
					}
				}
			}
		} catch(Exception e) {
			log.error("Error in checking in to SCM repository",e);
		}
		return response;
	}
	
	@Override
	@Transactional
	public String checkInScriptToSVN(Integer storyId, String commitComment) {
		String SVNResponse = null;
		String response = "";
		ConnectorCredentials connectorCredentials = new ConnectorCredentials();
		try{
			TestCaseAutomationStory story = testCaseAutomationScriptDAO.getByTestCaseStoryByAutomationStoryId(storyId);
			String tcName = story.getTestCase().getTestCaseName()+".java";
			String packagePath = "";
			TestStoryGeneratedScripts script = 	testCaseStoryDAO.getGeneratedScripts(storyId);
			
			if(script.getOutputPackage() != null)
				packagePath = script.getOutputPackage();
			String scriptToSVN = script.getTestScript();

			if(story.getTestCase() != null && story.getTestCase().getProductMaster() != null){
				int productId =story.getTestCase().getProductMaster().getProductId();
				List<SCMSystem> scmManagementSystems=defectManagementService.listSCMManagementSystem(productId);
				for(SCMSystem scmSystem : scmManagementSystems){
					if(scmSystem.getIsPrimary() != null && scmSystem.getIsPrimary() == 1){
						String pwd  = passwordEncryptionService.decrypt(scmSystem.getConnectionPassword().trim());
						if(scmSystem.getToolIntagration() != null && scmSystem.getToolIntagration().getId() != null 
								&& scmSystem.getToolIntagration().getId() == 7) {
							SVNResponse = connectorCredentials.commitNewFileToSVN(scmSystem.getConnectionProperty2()+"/", scmSystem.getConnectionUserName(), pwd, tcName, scriptToSVN.replaceAll("(\\\\n\\\\r|\\\\n|\\\\r)", System.lineSeparator()), packagePath.replace(".","/")+"/", commitComment);
							SVNResponse = scmSystem.getTitle()+" : " + SVNResponse;
							response = response + SVNResponse + "<br>";
						} else if (scmSystem.getToolIntagration() != null && scmSystem.getToolIntagration().getId() != null 
								&& scmSystem.getToolIntagration().getId() == 8){
							String authorName = "";
							String authorMail = "";
							
							if(scmSystem.getConnectionUri() == null || scmSystem.getConnectionUri().isEmpty()){
								response = "Invalid Connection URI, so failed to connect and commit";
								return response;
							}
							
							if(scmSystem.getConnectionProperty1() == null || scmSystem.getConnectionProperty1().isEmpty()) {
								response = "Invalid Local Repo Path, so failed to connect and commit";
								return response;
							}
							
							if(scmSystem.getConnectionUserName() == null || scmSystem.getConnectionUserName().isEmpty()) {
								response = "Invalid Username, so failed to connect and commit";
								return response;
							}
							
							if(scmSystem.getConnectionPassword() == null || scmSystem.getConnectionPassword().isEmpty()) {
								response = "Invalid Password, so failed to connect and commit";
								return response;
							}
							
							if(scmSystem.getConnectionProperty2() == null || scmSystem.getConnectionProperty2().isEmpty())
								authorName = "admin";
							
							if(scmSystem.getConnectionProperty3() == null || scmSystem.getConnectionProperty3().isEmpty())
								authorMail = "admin";
							
							String remoteRepoPath = scmSystem.getConnectionUri();
							//Conection Property 1 is used for Local GIT Repository
							String localRepositoryPath = scmSystem.getConnectionProperty1();
							String username = scmSystem.getConnectionUserName();
							String password = passwordEncryptionService.decrypt(scmSystem.getConnectionPassword());
							authorName = scmSystem.getConnectionProperty2();
							authorMail = scmSystem.getConnectionProperty3();								
							
							//Creating and establishing GIT Connector
							GitConnector gitConnector = new GitConnector();
							
							if(!new File(localRepositoryPath).exists()){
								gitConnector.cloneGitRepository(remoteRepoPath, localRepositoryPath, username, password);
							} 
							
							File localRepoTestFile = new File(localRepositoryPath + File.separator + tcName);
							FileWriter fileWriter = new FileWriter(localRepoTestFile);
							fileWriter.write(scriptToSVN);
							fileWriter.flush();
							fileWriter.close();
							
							boolean isSuccess = gitConnector.commitFile(localRepositoryPath, username, password, 
									localRepoTestFile , commitComment,authorName,authorMail);
							
							if(isSuccess)
								response = "Success: Successfully committed into GIT." + "<br>";								
							else
								response = "Failed to commit into GIT." + "<br>";
							
							log.info("Committed file to GIT Remote Repo");		
						}						
					}
				}
			} 
		} catch(Exception e){
			log.error("Unable to commit script into SVN / GIT", e);
		}		
		return response;
	}


	@Override
	@Transactional
	public String generateScriptTdandObjForDownload(
			JsonTestStoryGeneratedScripts jsonTestStoryGeneratedScripts) {
		String destinationDirectory = null;
		TestCaseAutomationStory story = null;
		if(jsonTestStoryGeneratedScripts.getTestCaseStoryId() != null){
			story = testCaseAutomationScriptDAO.getByTestCaseStoryByAutomationStoryId(jsonTestStoryGeneratedScripts.getTestCaseStoryId());
		}
		destinationDirectory = System.getProperty("catalina.home") + File.separator + "webapps" +request.getContextPath()+ File.separator + "TestScripts" + File.separator + "DownloadStoryScriptBundles"+File.separator+story.getTestCase().getTestCaseId();
		prepareStoryAndScript(story, destinationDirectory);
		generateTestDataAndObjectRepoForProduct(story.getTestCase().getProductMaster().getProductId(),destinationDirectory);
		String zipFilePath = ZipTool.dozip(destinationDirectory);
		if (ZipTool.isValidZipArchive(zipFilePath)) {
			log.info("Zip file created" + zipFilePath);
		} else {
			return "Failed : Unable to create a Zip File for Automation Scripts";
		}
		return zipFilePath;
	}
	
	public String generateTestDataAndObjectRepoForProduct(Integer productId,String destinationDirectory){
		try{
		List<UIObjectItems> uiObjectItemList= new ArrayList<UIObjectItems>();
		List<TestDataItems> testDataItemsList = new ArrayList<TestDataItems>();
		String exportLocation = System.getProperty("catalina.home")+File.separator;
		List<TestDataItemValues> testDataItemValuesList = new ArrayList<TestDataItemValues>();
		UserList user = (UserList)request.getSession().getAttribute("USER");
		Path FROM;
		String fileName = null;
		fileName = "uiObject.xlsx";
		Path TO = Paths.get(destinationDirectory, new String[0]);
		String  objectRepositoryFile = exportLocation+"webapps"+File.separator+"EXPORT"+File.separator+ "UIOBJECTS"+File.separator+user.getLoginId()+File.separator+productId;
		uiObjectItemList = testCaseScriptGenerationService.listUIObjectItemsByProductId(productId,0,user.getUserId(),null,null);
		if(uiObjectItemList != null && !uiObjectItemList.isEmpty() && uiObjectItemList.size() > 0){
			Boolean isexportComplete = testDataIntegrator.uiObjectsExport(uiObjectItemList, objectRepositoryFile,fileName);
			objectRepositoryFile = objectRepositoryFile + File.separator +fileName; 
			if(isexportComplete){
				FROM = Paths.get(objectRepositoryFile, new String[0]);
				Files.copy(FROM, TO.resolve(FROM.getFileName()), StandardCopyOption.REPLACE_EXISTING);
				log.info((Object)"Copied Object Repository File to bundle");

			}
		}
		fileName = "testData.xlsx";
		int testDataItemHeaderCount  =  testCaseScriptGenerationService.listTestDataItemValuesCountByProductIdAndUserId(productId,user.getUserId());
		String  testDataFolderDirectory = exportLocation+"webapps"+File.separator+"EXPORT"+File.separator+ "TESTDATA"+File.separator+user.getLoginId()+File.separator+productId;
		testDataItemsList = testCaseScriptGenerationService.listTestDataItemValuesByProductAndTestDataPlanAndHandle(productId,null,-1, user.getUserId());
		if(testDataItemValuesList != null && !testDataItemValuesList.isEmpty() && testDataItemValuesList.size() > 0){
			Boolean isTestDataexportComplete =  testDataIntegrator.testDataExport(testDataItemsList, testDataFolderDirectory,fileName, testDataItemHeaderCount);
			testDataFolderDirectory = testDataFolderDirectory + File.separator + fileName;
			if(isTestDataexportComplete){
				FROM = Paths.get(testDataFolderDirectory, new String[0]);
				Files.copy(FROM, TO.resolve(FROM.getFileName()), StandardCopyOption.REPLACE_EXISTING);
				log.info((Object)"Copied Test Data File to bundle");
			}
		}
		}catch(Exception e){
			log.error("Exception whle exporting testdata and object repository ... !", e);
			
		}

		return objectRepositoryFolder;

	}
	@Override
	@Transactional
	public Integer addTestDataPlan(TestDataPlan testDataPlan) {
		return testCaseAutomationScriptDAO.addTestDataPlan(testDataPlan);
	}
	@Override
	@Transactional
	public void updateTestDataPlan(TestDataPlan testDataPlan) {
		testCaseAutomationScriptDAO.updateTestDataPlan(testDataPlan);
	}
	@Override
	@Transactional
	public TestDataPlan getTestDataPlanByTestDataPlanName(String testDataPlanName, Integer productId) {
		return testCaseAutomationScriptDAO.getTestDataPlanByTestDataPlanName(testDataPlanName,productId);
	}


	@Override
	@Transactional
	public TestDataItems getTestDataItemByItemId(Integer testDataItemId) {
		
		return testCaseAutomationScriptDAO.getTestDataItemById(testDataItemId);
	}


	@Override
	@Transactional
	public String deleteTestData(TestDataItems tesDataItem) {
		
		return testCaseAutomationScriptDAO.deleteTestdata(tesDataItem);
	}


	@Override
	@Transactional
	public UIObjectItems getUIObjectItemById(Integer uiObjectItemId) {
		
		return testCaseAutomationScriptDAO.getUiObjectItemById(uiObjectItemId);
	}


	@Override
	@Transactional
	public String deleteObjectRepo(UIObjectItems uiObjectItem) {
		return testCaseAutomationScriptDAO.deleteUiObjectRepository(uiObjectItem);
	}


	@Override
	@Transactional
	public boolean deleteKeywordLibrary(Integer keywordLibId) {
		try {
			return testCaseAutomationScriptDAO.deleteKeywordLibrary(keywordLibId);
		}catch(Exception e) {
			
		}
		return false;
	}

	@Override
	@Transactional
	public KeywordLibrary getKeywordLibraryByClassNameAndBinary(String className, String binaryLoaderName) {
		return testCaseAutomationScriptDAO.getKeywordLibraryByClassNameAndBinary(className,binaryLoaderName);
	}

	@Override
	@Transactional
	public String checkInRepositoryToSVN(Integer storyId, String commitComment,boolean isObjectRepo, boolean isTestData) {

		String SVNResponse = null;
		String response = "";
		ConnectorCredentials connectorCredentials = new ConnectorCredentials();
		try{
			//get the excel file for Object Repo
			String objectRepFileName ="";
			String testDataFileName ="";
			Boolean isTestDataExportComplete = false;
			Boolean isObjectRepositoryExportComplete = false;
			String objectRepositoryFile = "";
			String testDataFile = "";
			List<UIObjectItems> uiObjectItemList= new ArrayList<UIObjectItems>();
			String exportLocation = System.getProperty("catalina.home")+File.separator;
			List<TestDataItemValues> testDataItemValuesList = new ArrayList<TestDataItemValues>();
			List<TestDataItems> testDataItemsList = new ArrayList<TestDataItems>();
			UserList user = null;
			try{
				user = (UserList)request.getSession().getAttribute("USER");
			}catch(Exception e){
				log.error("Error in getting user from request ");
			}
			Integer userId = 1;
			String loginId = "admin";
			if(user != null && user.getUserId() != null){
				userId = user.getUserId();
				loginId = user.getLoginId();
			}
			TestCaseAutomationStory story = testCaseAutomationScriptDAO.getByTestCaseStoryByAutomationStoryId(storyId);
			Integer productId = story.getProductId();
			
			if(isObjectRepo){
				objectRepFileName = "uiObject.xlsx";
				String  objectRepositoryFolder = exportLocation+"webapps"+File.separator+"EXPORT"+File.separator+ "UIOBJECTS"+File.separator+loginId+File.separator+productId;					
				uiObjectItemList = testCaseScriptGenerationService.listUIObjectItemsByProductId(story.getProductId(),0,-1,null,null);
				if(uiObjectItemList != null && !uiObjectItemList.isEmpty() && uiObjectItemList.size() > 0){
					isObjectRepositoryExportComplete = testDataIntegrator.uiObjectsExport(uiObjectItemList, objectRepositoryFolder,objectRepFileName);
					objectRepositoryFile = objectRepositoryFolder + File.separator +objectRepFileName; 
				}
			}
			if(isTestData){
				testDataFileName = "testData.xlsx";
				int testDataItemHeaderCount  =  testCaseScriptGenerationService.listTestDataItemValuesCountByProductIdAndUserId(productId,userId);
				String testDataFolderDirectory = exportLocation+"webapps"+File.separator+"EXPORT"+File.separator+ "TESTDATA"+File.separator+loginId+File.separator+productId;
				testDataItemsList = testCaseScriptGenerationService.listTestDataItemValuesByProductAndTestDataPlanAndHandle(productId,null,-1, userId);
				if(testDataItemsList != null && !testDataItemsList.isEmpty() && testDataItemsList.size() > 0){
					isTestDataExportComplete =  testDataIntegrator.testDataExport(testDataItemsList, testDataFolderDirectory,testDataFileName, testDataItemHeaderCount);
					testDataFile = testDataFolderDirectory + File.separator + testDataFileName;
				}
			}
			if(story.getTestCase() != null && story.getTestCase().getProductMaster() != null){
				List<SCMSystem> scmManagementSystems=defectManagementService.listSCMManagementSystem(productId);
				for(SCMSystem scmSystem : scmManagementSystems){
					if(scmSystem.getIsPrimary() != null && scmSystem.getIsPrimary() == 1){
						String pwd  = passwordEncryptionService.decrypt(scmSystem.getConnectionPassword().trim());
						/*if(scmSystem.getToolIntagration() != null && scmSystem.getToolIntagration().getId() != null 
								&& scmSystem.getToolIntagration().getId() == 7) {
							if(isObjectRepositoryExportComplete)
								SVNResponse = connectorCredentials.commitFileToSVN(scmSystem.getConnectionProperty2()+"/", scmSystem.getConnectionUserName(), pwd, objectRepositoryFolder, objectRepFileName, commitComment) + "<br>";
							else
								SVNResponse = "Object Repository is not committed due to some error" + "<br>";
							
							if(isTestDataExportComplete)
								SVNResponse = connectorCredentials.commitFileToSVN(scmSystem.getConnectionProperty2()+"/", scmSystem.getConnectionUserName(), pwd, testDataFolderDirectory, testDataFileName, commitComment)+ "<br>";
							else
								SVNResponse = "Object Repository is not committed due to some error"+ "<br>";
							
							SVNResponse = scmSystem.getTitle()+" : " + SVNResponse;
							response = response + SVNResponse + "<br>";
						}	*/				
						if(scmSystem.getToolIntagration() != null && scmSystem.getToolIntagration().getId() != null 
								&& scmSystem.getToolIntagration().getId() == 7) {
							/*SVNResponse = connectorCredentials.commitNewFileToSVN(scmSystem.getConnectionProperty1(), scmSystem.getConnectionUserName(), pwd, tcName, scriptTOSVN.replaceAll("(\\\\n\\\\r|\\\\n|\\\\r)", System.lineSeparator()), null, commitComment);
							SVNResponse = scmSystem.getTitle()+" : " + SVNResponse;
							response = response + SVNResponse + "<br>";*/
						} else if (scmSystem.getToolIntagration() != null && scmSystem.getToolIntagration().getId() != null 
								&& scmSystem.getToolIntagration().getId() == 8){
							
							String authorName = "";
							String authorMail = "";
							
							if(scmSystem.getConnectionUri() == null || scmSystem.getConnectionUri().isEmpty()){
								response = "Invalid Connection URI, so failed to connect and commit";
								return response;
							}
							
							if(scmSystem.getConnectionProperty1() == null || scmSystem.getConnectionProperty1().isEmpty()) {
								response = "Invalid Local Repo Path, so failed to connect and commit";
								return response;
							}
							
							if(scmSystem.getConnectionUserName() == null || scmSystem.getConnectionUserName().isEmpty()) {
								response = "Invalid Username, so failed to connect and commit";
								return response;
							}
							
							if(scmSystem.getConnectionPassword() == null || scmSystem.getConnectionPassword().isEmpty()) {
								response = "Invalid Password, so failed to connect and commit";
								return response;
							}
							
							if(scmSystem.getConnectionProperty2() == null || scmSystem.getConnectionProperty2().isEmpty())
								authorName = "admin";
							
							if(scmSystem.getConnectionProperty3() == null || scmSystem.getConnectionProperty3().isEmpty())
								authorMail = "admin";
							
							String remoteRepoPath = scmSystem.getConnectionUri();
							//Conection Property 1 is used for Local GIT Repository
							String localRepositoryPath = scmSystem.getConnectionProperty1();
							String username = scmSystem.getConnectionUserName();
							String password = passwordEncryptionService.decrypt(scmSystem.getConnectionPassword());
							authorName = scmSystem.getConnectionProperty2();
							authorMail = scmSystem.getConnectionProperty3();								
							
							//Creating and establishing GIT Connector
							GitConnector gitConnector = new GitConnector();
							
							if(!new File(localRepositoryPath).exists()){
								gitConnector.cloneGitRepository(remoteRepoPath, localRepositoryPath, username, password);
							} 

							//To copy Test Data
							Path FROM;
							Path TO;
							if(isTestData){
								FROM = Paths.get(testDataFile);
								TO = Paths.get(localRepositoryPath);			
								Files.copy(FROM, TO.resolve(FROM.getFileName()), StandardCopyOption.REPLACE_EXISTING);
							}
							//To Copy Object repository
							if(isObjectRepo){
								FROM = Paths.get(objectRepositoryFile);
								TO = Paths.get(localRepositoryPath);			
								Files.copy(FROM, TO.resolve(FROM.getFileName()), StandardCopyOption.REPLACE_EXISTING);
							}
							//Commit the copied files from local Repo to GIT
							boolean isSuccess = gitConnector.commitFile(localRepositoryPath, username, password, 
									null , commitComment,authorName,authorMail);
							
							if(isSuccess)
								response = "Success: Successfully committed into GIT." + "<br>";								
							else
								response = "Failed to commit into GIT." + "<br>";
							
							log.info("Committed file to GIT Remote Repo");								
						}
					}
				}
			} 
		} catch(Exception e){
			log.error("Unable to commit script into SVN / GIT", e);
		}		
		return response;
	}
}