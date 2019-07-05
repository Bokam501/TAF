package com.hcl.atf.taf.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hcl.atf.taf.constants.AOTCConstants;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.ZipTool;
import com.hcl.atf.taf.dao.ExecutionModeDAO;
import com.hcl.atf.taf.dao.LanguagesDAO;
import com.hcl.atf.taf.dao.ProductTypeDAO;
import com.hcl.atf.taf.dao.TestCaseAutomationScriptDAO;
import com.hcl.atf.taf.dao.TestEngineLanguageModeDAO;
import com.hcl.atf.taf.dao.TestToolMasterDAO;
import com.hcl.atf.taf.integration.data.TestDataIntegrator;
import com.hcl.atf.taf.model.AtsgParameters;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.BDDKeywordsPhrases;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ExecutionMode;
import com.hcl.atf.taf.model.KeywordLibrary;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.SCMSystem;
import com.hcl.atf.taf.model.ScriptGenerationDetails;
import com.hcl.atf.taf.model.TestCaseAutomationStory;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseScript;
import com.hcl.atf.taf.model.TestCaseScriptHasTestCase;
import com.hcl.atf.taf.model.TestCaseScriptVersion;
import com.hcl.atf.taf.model.TestDataItemValues;
import com.hcl.atf.taf.model.TestDataItems;
import com.hcl.atf.taf.model.TestDataPlan;
import com.hcl.atf.taf.model.TestEngineLanguageMode;
import com.hcl.atf.taf.model.TestStoryGeneratedScripts;
import com.hcl.atf.taf.model.TestToolMaster;
import com.hcl.atf.taf.model.UIObjectItems;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.AttachmentDTO;
import com.hcl.atf.taf.model.json.JsonAtsg;
import com.hcl.atf.taf.model.json.JsonAttachment;
import com.hcl.atf.taf.model.json.JsonAutomationScriptsVersion;
import com.hcl.atf.taf.model.json.JsonBDDKeywordsPhrase;
import com.hcl.atf.taf.model.json.JsonKeywordLibrary;
import com.hcl.atf.taf.model.json.JsonProductType;
import com.hcl.atf.taf.model.json.JsonTestCaseAutomationScript;
import com.hcl.atf.taf.model.json.JsonTestCaseAutomationScripts;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.JsonTestCaseScript;
import com.hcl.atf.taf.model.json.JsonTestDataItems;
import com.hcl.atf.taf.model.json.JsonTestDataItemsValues;
import com.hcl.atf.taf.model.json.JsonTestDataPlan;
import com.hcl.atf.taf.model.json.JsonTestEngineLanguageMode;
import com.hcl.atf.taf.model.json.JsonTestStoryGeneratedScripts;
import com.hcl.atf.taf.model.json.JsonTestToolMaster;
import com.hcl.atf.taf.model.json.JsonUiObjectItems;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptionsModel;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.scriptgeneration.ScriptGeneratorUtilities;
import com.hcl.atf.taf.service.AttachmentService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.DefectManagementService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.PasswordEncryptionService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestCaseScriptGenerationService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestFactoryManagementService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.ers.atsg.TestScriptFileGenerator;
import com.hcl.taf.git.util.GitConnector;
@Controller
public class TestCaseAutomationScriptsController {

	private static final Log log = LogFactory.getLog(TestCaseAutomationScriptsController.class);


	@Autowired
	private TestCaseScriptGenerationService testCaseScriptGenerationService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private AttachmentService attachmentsService;
	@Autowired
	private CommonService commonService;	
	@Autowired
	private EventsService eventsService;
	@Autowired
	private UserListService userListService;
	@Autowired
	private TestCaseService testCaseService;

	@Autowired
	private TestCaseAutomationScriptDAO testCaseAutomationScriptDAO;

	@Autowired
	private MongoDBService mongoDBService;



	@Autowired
	private TestFactoryManagementService testFactoryManagementService;

	@Autowired
	private ProductTypeDAO productTypeDAO;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TestDataIntegrator testDataIntegrator;

	@Value(value="#{ilcmProps['database.driver']}")
	private String databaseDriver;
	@Value(value="#{ilcmProps['database.url']}")
	private String databaseUrl;
	@Value(value="#{ilcmProps['database.user']}")
	private String dbUserName;
	@Value(value="#{ilcmProps['database.password']}")
	private String dbPassword;

	@Value("#{ilcmProps['GENERATED_TEST_SCRIPTS_DESTINATION_FOLDER']}")
	private String testScriptsDestinationDirectory;
	@Value("#{ilcmProps['GENERATED_SCRIPTS_DESTINATION_FOLDER']}")
	private String generatedTestScripts;
	@Value("#{ilcmProps['GENERATED_SCRIPTS_LIB_DESTINATION_FOLDER']}")
	private String libFolderpath;
	@Value(value="#{ilcmProps['STORY_EDITING_ALLOW_CONCURRENT_EDITING']}")
	private boolean testStoryEditingAllowMultipleUsers;
	@Value(value="#{ilcmProps['AMDOCS']}")
	private String amdocsMode;
	@Autowired
	private TestToolMasterDAO testToolMasterDAO;
	@Autowired
	private LanguagesDAO languagesDAO;
	@Autowired
	private ExecutionModeDAO executionModeDAO;
	@Autowired
	private TestEngineLanguageModeDAO testEngineLanguageModeDAO;
	@Autowired
	DefectManagementService defectManagementService;


	private static final int BUFFER_SIZE = 4096;

	private static final String CODE_GENERATION_MODE_TAF = "TAF-MODE";
	private static final String CODE_GENERATION_MODE_GENERIC = "GENERIC-MODE";
	private static final String BROWSER_FIREFOX = "FIREFOX";
	private static final String BROWSER_CHROME = "CHROME";
	@Value(value="#{ilcmProps['EDAT_CONFIG_FILE_FOLDER']}")
	private String eDatConfigFolder;
	private String eDatConfigFileName="";

	@Value(value="#{ilcmProps['SVN_URI']}")
	private String svnurl;

	@Value(value="#{ilcmProps['SVN_URI_TEST_SCRIPTS_SRC']}")
	private String svnurlsrc;

	@Value(value="#{ilcmProps['SVN_URI_TEST_STORIES']}")
	private String svnurlstories;

	@Value(value="#{ilcmProps['SVN_USERNAME']}")
	private String svnusername;

	@Value(value="#{ilcmProps['SVN_PASSWORD']}")
	private String svnpassword;

	@Value(value="#{ilcmProps['SVN_URI_PACKAGE_PATH']}")
	private String packagePath;

	@Autowired
	PasswordEncryptionService passwordEncryptionService;
	/*
	 * Boiler Plate Code : Download the scripts for a Testcase/Test Suite/Product 
	 */
	@RequestMapping(value="product.testscripts.download",method=RequestMethod.GET)// ,produces="application/json")
	public @ResponseBody JTableResponse downloadBoilerPlateTestScripts(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id, @RequestParam String scriptsFor, @RequestParam String scriptType, @RequestParam String testEngine, @RequestParam String testStepOptions) {
		log.info("product.testcase.script.download");
		JTableResponse jTableResponse = null;
		try {
			log.info("Id : " + id);
			//if (productId <= 0) {
			if (id == null || ("null").equals(id)) {
			}

			if (testStepOptions == null || testStepOptions.isEmpty()) {
				testStepOptions = "SINGLE_METHOD";
			}
			String fileName = testCaseScriptGenerationService.generateProductTestCaseScripts(id, scriptsFor, null, null, null, 1, scriptType, testEngine, true, true, testStepOptions);
			if (fileName.startsWith("Failed")) {
				log.info("Unable to generate test scripts : " + fileName);
				jTableResponse = new JTableResponse("SUCCESS",fileName);
				return jTableResponse;
			} else {

				log.info("Initiating download of the script files : " + fileName);
				// get absolute path of the application
				ServletContext context = request.getServletContext();
				String appPath = context.getRealPath("");


				// construct the complete absolute path of the file
				//String fullPath = appPath + filePath;      
				File downloadFile = new File(fileName);
				FileInputStream inputStream = new FileInputStream(downloadFile);

				// get MIME type of the file
				String mimeType = context.getMimeType(fileName);
				if (mimeType == null) {
					// set to binary type if MIME mapping not found
					mimeType = "application/octet-stream";
				}
				log.info("MIME type: " + mimeType);

				// set content attributes for the response
				response.setContentType(mimeType);
				response.setContentLength((int) downloadFile.length());

				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
				response.setHeader(headerKey, headerValue);

				// get output stream of the response
				OutputStream outStream = response.getOutputStream();
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;

				// write bytes read from the input stream into the output stream
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}

				response.flushBuffer();
				inputStream.close();
				outStream.close();
				jTableResponse = new JTableResponse("OK", "success");
				log.info("Has the download started ?" );
			}
		} catch (Exception e) {
			log.error("Test Scripts Download Error", e);
		}
		return jTableResponse;
	}


	/*
	 * Boiler Plate Code : Show the Test case, Main 
	 */
	@RequestMapping(value="product.testcase.script.view",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse viewBoilerPlateTestCaseScript(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id, @RequestParam String scriptsFor, @RequestParam String scriptType, @RequestParam String testEngine, @RequestParam String testStepOptions) {
		log.info("product.testcase.script.view");
		JTableResponse jTableResponse = null;
		try {
			log.info("Id : " + id);
			if (id == null || ("null").equals(id)) {

				log.info("Unable to show test scripts for Testcase : " + id);
				jTableResponse = new JTableResponse("ERROR","Testcase ID is not specified!");
				return jTableResponse;
			}

			if (testStepOptions == null || testStepOptions.isEmpty()) {
				testStepOptions = "SINGLE_METHOD";
			}
			JsonTestCaseScript jsonTestCaseScript = testCaseScriptGenerationService.getTestCaseScriptForViewing(id, scriptsFor, null, null, null, 1, scriptType, testEngine, true, true, testStepOptions);
			if (jsonTestCaseScript == null) {
				log.info("Unable to generate test scripts for viewing: " + id);
				jTableResponse = new JTableResponse("ERROR","Unable to generate test scripts for viewing: " + id);
				return jTableResponse;
			} else {

				List<JsonTestCaseScript> jsonTestCaseScriptList = new ArrayList<JsonTestCaseScript>();
				jsonTestCaseScriptList.add(jsonTestCaseScript);
				jTableResponse = new JTableResponse("OK", jsonTestCaseScriptList, 1);
			}
		} catch (Exception e) {
			log.error("Test Scripts View Error", e);
		}
		return jTableResponse;
	}

	/*
	 * Boiler Plate Code : Show the Main and Ref Script for a test suite 
	 */
	@RequestMapping(value="product.testsuite.script.ref.view",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse viewBoilerPlateTestSuiteRefScript(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id, @RequestParam String scriptsFor, @RequestParam String scriptType, @RequestParam String testEngine, @RequestParam String testStepOptions) {
		log.info("product.testcase.script.ref.view");
		JTableResponse jTableResponse = null;
		try {
			log.info("Id : " + id);
			if (id == null || ("null").equals(id)) {

				log.info("Unable to show Ref scripts for TestSuite : " + id);
				jTableResponse = new JTableResponse("ERROR","Testsuite ID is not specified!");
				return jTableResponse;
			}

			if (testStepOptions == null || testStepOptions.isEmpty()) {
				testStepOptions = "SINGLE_METHOD";
			}
			JsonTestCaseScript jsonTestCaseScript = testCaseScriptGenerationService.getTestSuiteRefScriptForViewing(id, scriptsFor, null, null, null, 1, scriptType, testEngine, true, true, testStepOptions);
			if (jsonTestCaseScript == null) {
				log.info("Unable to generate ref scripts for viewing for TestSuite : " + id);
				jTableResponse = new JTableResponse("ERROR","Unable to generate test scripts for viewing for TestSuite : " + id);
				return jTableResponse;
			} else {

				List<JsonTestCaseScript> jsonTestCaseScriptList = new ArrayList<JsonTestCaseScript>();
				jsonTestCaseScriptList.add(jsonTestCaseScript);
				jTableResponse = new JTableResponse("OK", jsonTestCaseScriptList, 1);
			}
		} catch (Exception e) {
			log.error("Test suite ref scripts View Error", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value="product.testcase.automationscript.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addTestCaseAutomationScriptForTestcase( HttpServletRequest request, @ModelAttribute JsonTestCaseAutomationScript jsonTestCaseAutomationScript, BindingResult result) {

		JTableSingleResponse jTableSingleResponse;
		log.debug("product.testcase.automationscript.add");
		if(result.hasErrors()){			
			jTableSingleResponse =jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {
			String errorMessage = testCaseScriptGenerationService.addTestCaseAutomationScript(jsonTestCaseAutomationScript);

			if(errorMessage == null ||  errorMessage.startsWith("SUCCESS")) {
				UserList user=(UserList)request.getSession().getAttribute("USER");
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_TESTCASE_AUTOMATION_SCRIPT, jsonTestCaseAutomationScript.getTestCaseAutomationStoryId(), jsonTestCaseAutomationScript.getName(), user);
				jTableSingleResponse = new JTableSingleResponse("OK",jsonTestCaseAutomationScript);
			} else {				
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
			}

		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Test Case Automation Script!");
			log.error("JSON ERROR", e);
		}	        
		return jTableSingleResponse;		
	}

	@RequestMapping(value="product.testcase.automationscripts.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestCaseAutomationScriptsForTestcase(@RequestParam Integer testCaseId) {
		JTableResponse jTableResponse;
		try {
			if (testCaseId == null) {
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			List<JsonTestCaseAutomationScript> automationScripts = testCaseScriptGenerationService.listTestCaseAutomationScripts(testCaseId);
			if (automationScripts == null) {	
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			jTableResponse = new JTableResponse("OK", automationScripts, automationScripts.size());
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching automation scripts list!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}


	@RequestMapping(value="product.testcase.automationscripts.download",method=RequestMethod.GET)// ,produces="application/json")
	public @ResponseBody JTableResponse downloadBDDTestCaseAutomationScriptContent(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id, @RequestParam String scriptsFor, @RequestParam String scriptType, @RequestParam String testEngine) {

		JTableResponse jTableResponse;
		try {
			if (id == null) {
				jTableResponse = new JTableResponse("ERROR", "Unable to download GHERKIN scripts as Id is not available");
				return jTableResponse;
			}

			if (!scriptType.equals("GHERKIN")) {

				jTableResponse = new JTableResponse("ERROR", "Only GHERKIN Stories are supported for download currently");
				return jTableResponse;
			}

			String fileName = testCaseScriptGenerationService.generateBDDTestCaseAutomationScriptFiles(id, scriptsFor, scriptType, testEngine);
			if (fileName.startsWith("Failed")) {
				log.info("Unable to generate test scripts : " + fileName);
				jTableResponse = new JTableResponse("SUCCESS",fileName);
				return jTableResponse;
			} else {

				log.info("Initiating download of the GHERKIN script files : " + fileName);
				// get absolute path of the application
				ServletContext context = request.getServletContext();
				String appPath = context.getRealPath("");


				// construct the complete absolute path of the file
				//String fullPath = appPath + filePath;      
				File downloadFile = new File(fileName);
				FileInputStream inputStream = new FileInputStream(downloadFile);

				// get MIME type of the file
				String mimeType = context.getMimeType(fileName);
				if (mimeType == null) {
					// set to binary type if MIME mapping not found
					mimeType = "application/octet-stream";
				}
				log.info("MIME type: " + mimeType);

				// set content attributes for the response
				response.setContentType(mimeType);
				response.setContentLength((int) downloadFile.length());

				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
				response.setHeader(headerKey, headerValue);

				// get output stream of the response
				OutputStream outStream = response.getOutputStream();
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;

				// write bytes read from the input stream into the output stream
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}

				response.flushBuffer();
				inputStream.close();
				outStream.close();
				jTableResponse = new JTableResponse("OK", "success");
				log.info("Has the download started ?" );
			}

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Saving Script!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}


	//Automation Scripts
	@RequestMapping(value="product.automation.script.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addAutomatedScript( HttpServletRequest request, @ModelAttribute JsonTestCaseAutomationScripts jsonTestAutomationScripts , BindingResult result) {

		JTableSingleResponse jTableSingleResponse;
		log.debug("product.automation.script.add");
		if(result.hasErrors()){			
			jTableSingleResponse =jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {	
			testCaseScriptGenerationService.addTestcaseautoscripts(jsonTestAutomationScripts);
			jTableSingleResponse = new JTableSingleResponse("OK",jsonTestAutomationScripts);

		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Test Case Automation Script!");
			log.error("JSON ERROR", e);
		}	        
		return jTableSingleResponse;		
	}

	@RequestMapping(value="product.automation.script.get",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse viewAutomatedScript(@RequestParam Integer testCaseId,@RequestParam Integer jtStartIndex,@RequestParam Integer jtPageSize) {

		log.info("product.automation.script.get - inside");
		JTableResponse jTableResponse=null;
		try {
			if (testCaseId == null) {
				jTableResponse = new JTableResponse("ERROR", "Unable to get script as Id is not available");
				return jTableResponse;
			}
			//List<TestCaseScript> tcScript = new ArrayList<TestCaseScript>();					
			List<TestCaseScript> tcScript = 	testCaseScriptGenerationService.getTestcaseAutoScripts(testCaseId,jtStartIndex,jtPageSize);

			List<JsonTestCaseAutomationScripts> jsonTCScripts = new ArrayList<JsonTestCaseAutomationScripts>();
			if (tcScript == null || tcScript.size() ==0 ) {

				jTableResponse = new JTableResponse("OK", jsonTCScripts, 0);
			}else{

				for (TestCaseScript tcscripts : tcScript) {
					jsonTCScripts.add(new JsonTestCaseAutomationScripts(tcscripts));
				}				
				jTableResponse = new JTableResponse("OK", jsonTCScripts,jsonTCScripts.size() );
				tcScript = null;
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Saving Script!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	//Automation Scripts Version
	@RequestMapping(value="product.automation.script.version.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addAutomatedScriptVersion( HttpServletRequest request, @ModelAttribute JsonAutomationScriptsVersion jsontcautomationscriptsversion , BindingResult result) {

		List<TestCaseScriptVersion> listTcScriptVerIsSel = new ArrayList<TestCaseScriptVersion>();

		JTableSingleResponse jTableSingleResponse;
		log.debug("product.automation.script.version.add");
		if(result.hasErrors()){			
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {	
			testCaseScriptGenerationService.addTestCaseAutoScriptsVersion(jsontcautomationscriptsversion,jsontcautomationscriptsversion.getTestCaseId());

			jTableSingleResponse = new JTableSingleResponse("OK",jsontcautomationscriptsversion);

		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Test Case Automation Script Version!");
			log.error("JSON ERROR", e);
		}	        
		return jTableSingleResponse;		
	}

	@RequestMapping(value="product.automation.script.version.get",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse viewAutomatedScriptVersion(@RequestParam Integer scriptId,@RequestParam Integer jtStartIndex,@RequestParam Integer jtPageSize) {

		JTableResponse jTableResponse;
		try {
			if (scriptId == null) {
				jTableResponse = new JTableResponse("ERROR", "Unable to get script as Id is not available");
				return jTableResponse;
			}
			List<TestCaseScriptVersion> tcScriptVersion = new ArrayList<TestCaseScriptVersion>();					
			tcScriptVersion = 	testCaseScriptGenerationService.getTestCaseAutoScriptsVersionList(scriptId,jtStartIndex,jtPageSize);


			List<JsonAutomationScriptsVersion> jsonTCScriptsVersion = new ArrayList<JsonAutomationScriptsVersion>();
			if (tcScriptVersion == null || tcScriptVersion.size() ==0 ) {

				jTableResponse = new JTableResponse("OK", jsonTCScriptsVersion, 0);
			}else{

				for (TestCaseScriptVersion tcscriptsversion : tcScriptVersion) {
					jsonTCScriptsVersion.add(new JsonAutomationScriptsVersion(tcscriptsversion));
				}				
				jTableResponse = new JTableResponse("OK", jsonTCScriptsVersion,jsonTCScriptsVersion.size() );
				tcScriptVersion = null;
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Saving Script Version!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	
	@RequestMapping(value="bddkeywordsphrases.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateBDDKeyWordSphrases(@ModelAttribute JsonBDDKeywordsPhrase jsonBDDKeywordsPhrase, BindingResult result) {
		JTableResponse jTableResponse = null;
		BDDKeywordsPhrases bddKeyWordsPhrasesFromUI = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}

		try {

			bddKeyWordsPhrasesFromUI = jsonBDDKeywordsPhrase.getUpdateJsonBDDKeywordsPhrase();

			if(bddKeyWordsPhrasesFromUI.getIsSelenium() != null && bddKeyWordsPhrasesFromUI.getIsSelenium() == 1){
				bddKeyWordsPhrasesFromUI.setIsSeleniumScripGeneration(1);
				bddKeyWordsPhrasesFromUI.setIsSelenium(1);
			}
			if(bddKeyWordsPhrasesFromUI.getIsAppium() != null && bddKeyWordsPhrasesFromUI.getIsAppium() == 1){
				bddKeyWordsPhrasesFromUI.setIsAppiumScripGeneration(1);
				bddKeyWordsPhrasesFromUI.setIsAppium(1);
			}
			if(bddKeyWordsPhrasesFromUI.getIsSeeTest() != null && bddKeyWordsPhrasesFromUI.getIsSeeTest() == 1){
				bddKeyWordsPhrasesFromUI.setIsSeetestScripGeneration(1);
				bddKeyWordsPhrasesFromUI.setIsSeeTest(1);
			}
			if(bddKeyWordsPhrasesFromUI.getIsProtractor() != null && bddKeyWordsPhrasesFromUI.getIsProtractor() == 1){
				bddKeyWordsPhrasesFromUI.setIsProtractorScripGeneration(1);
				bddKeyWordsPhrasesFromUI.setIsProtractor(1);
			}
			if(bddKeyWordsPhrasesFromUI.getIsTestComplete()!= null && bddKeyWordsPhrasesFromUI.getIsTestComplete() == 1){
				bddKeyWordsPhrasesFromUI.setIsTestComplete(1);
				if(bddKeyWordsPhrasesFromUI.getIsDesktop() != null && bddKeyWordsPhrasesFromUI.getIsDesktop() == 1)
					bddKeyWordsPhrasesFromUI.setIsTestCompleteScripGeneration(1);
				else
					bddKeyWordsPhrasesFromUI.setIsTestCompleteWebScripGeneration(1);
			}
			if(bddKeyWordsPhrasesFromUI.getIsRestAssured() != null && bddKeyWordsPhrasesFromUI.getIsRestAssured() == 1){
				bddKeyWordsPhrasesFromUI.setIsRestAssuredScripGeneration(1);
				bddKeyWordsPhrasesFromUI.setIsRestAssured(1);
			}
			if(bddKeyWordsPhrasesFromUI.getIsEdat() != null && bddKeyWordsPhrasesFromUI.getIsEdat() == 1){
				bddKeyWordsPhrasesFromUI.setIsEDATScripGeneration(1);
				bddKeyWordsPhrasesFromUI.setIsEdat(1);
			}
		
			testCaseScriptGenerationService.updateBDDKeyWordsPhrase(bddKeyWordsPhrasesFromUI);
			List<JsonBDDKeywordsPhrase> jsonBddKeywords = new ArrayList<JsonBDDKeywordsPhrase>();
			jsonBddKeywords.add(new JsonBDDKeywordsPhrase(bddKeyWordsPhrasesFromUI));
			jTableResponse = new JTableResponse("Ok",jsonBddKeywords);	
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Unable to update the Product!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value="product.version.testobjectdata.attachments.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listVersionTestObjectDataAttachments(@RequestParam int productId, @RequestParam int versionId) {
		log.debug("inside product.version.testobjectdata.attachments.list");
		JTableResponse jTableResponse;			 
		try {
			List<AttachmentDTO> attachmentDTOList = attachmentsService.listTestDataAttachmentDTO(productId, versionId, "-1");				
			List<JsonAttachment> jsonAttachmentList = new ArrayList<JsonAttachment>();
			for (AttachmentDTO attachmentDTO : attachmentDTOList) {
				jsonAttachmentList.add(new JsonAttachment(attachmentDTO));
			}			
			jTableResponse = new JTableResponse("OK", jsonAttachmentList,jsonAttachmentList.size());     
			attachmentDTOList = null;
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Unable to show Attachment!");
			log.error("JSON ERROR", e);
		}		        
		return jTableResponse;
	}


	@RequestMapping(value="product.automation.script.version.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse updateAutomatedScriptVersion( HttpServletRequest request, @ModelAttribute JsonAutomationScriptsVersion jsontcautomationscriptsversion , BindingResult result) {

		List<TestCaseScriptVersion> listTcScriptVerIsSel = new ArrayList<TestCaseScriptVersion>();

		JTableSingleResponse jTableSingleResponse;
		log.debug("product.automation.script.version.update");
		if(result.hasErrors()){			
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {	
			testCaseScriptGenerationService.addTestCaseAutoScriptsVersion(jsontcautomationscriptsversion,jsontcautomationscriptsversion.getTestCaseId());
			testCaseScriptGenerationService.addTestCaseAutoScriptsVersionStatus(jsontcautomationscriptsversion,jsontcautomationscriptsversion.getScriptVersionId(),jsontcautomationscriptsversion.getStatus());

			jTableSingleResponse = new JTableSingleResponse("OK",jsontcautomationscriptsversion);

		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Test Case Automation Script Version!");
			log.error("JSON ERROR", e);
		}	        
		return jTableSingleResponse;		
	}
	//mamtha
	@RequestMapping(value="update.testdata.attachment",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse  updateTestDataAttachments(@RequestParam String fileName,@RequestParam Integer productVersion) {

		List<TestCaseScriptVersion> listTcScriptVerIsSel = new ArrayList<TestCaseScriptVersion>();

		JTableSingleResponse jTableSingleResponse=null;
		log.debug("update.testdata.attachment");

		try {	

			String filter="entityId="+productVersion;
			String errorMessage = commonService.duplicateName(fileName, "Attachment", "attachmentPrefixName", "Attachment",filter);
			if (errorMessage != null) {

				jTableSingleResponse = new JTableSingleResponse("OK",errorMessage);
				return jTableSingleResponse;
			}
			jTableSingleResponse = new JTableSingleResponse("OK",fileName);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error verifying test data attachment!");
			log.error("JSON ERROR", e);
		}	        
		return jTableSingleResponse;		
	}
	
	@RequestMapping(value="get.atsg.parameters", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public JTableResponse getAtsgScriptParametersByTestCaseId(@RequestParam int testCaseId) {
		JTableResponse jTableResponse;
		try {
			AtsgParameters atsg = this.attachmentsService.getAtsgScriptParametersByTestCaseId(testCaseId);
			ArrayList<JsonAtsg> jsonAtsgList = new ArrayList<JsonAtsg>();
			if (atsg != null) {
				JsonAtsg jsonatsg = new JsonAtsg(atsg);
				jsonAtsgList.add(jsonatsg);
			}
			jTableResponse = new JTableResponse("OK", jsonAtsgList, jsonAtsgList.size());
		}
		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Unable to show atsg parametere!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}


	@RequestMapping(value={"keywordLibrary.save"},method=RequestMethod.POST, produces={"application/json"})
	@ResponseBody	
	public JTableSingleResponse saveKeywordlibrary(HttpServletRequest request,@ModelAttribute JsonKeywordLibrary jsonKeywordLibrary, BindingResult result) {
		JTableSingleResponse jTableResponse = null;
		KeywordLibrary keywordLibrary = null;
		List<KeywordLibrary>  keywordLibList = null;
		KeywordLibrary keywordLib = null;
		BDDKeywordsPhrases keywords = null;
		Integer keywordLibId = null;
		BDDKeywordsPhrases keywordsFromDB = new BDDKeywordsPhrases();
		UserList user = (UserList)request.getSession().getAttribute("USER");
		try {
			/*JsonKeywordLibrary jsonKeywordLibrary = new JsonKeywordLibrary();*/
			String filter = "" ;
			/*if(jsonKeywordLibrary.getType().equalsIgnoreCase(IDPAConstants.SCRIPT_GENERATION) || 
					jsonKeywordLibrary.getType().equalsIgnoreCase(IDPAConstants.SCRIPT_GENERATION_SPACE)){
				if(jsonKeywordLibrary.getTestToolName() .equalsIgnoreCase(IDPAConstants.TEST_TOOL_SELENIUM)){
					filter = filter + " isSeleniumScripGeneration=  1";	
				}else if(jsonKeywordLibrary.getTestToolName().equalsIgnoreCase(IDPAConstants.TEST_TOOL_APPIUM)){
					filter = filter + " isAppiumScripGeneration =  1";	
				}else if(jsonKeywordLibrary.getTestToolName().equalsIgnoreCase(IDPAConstants.TEST_TOOL_SEETEST)){
					filter = filter + " isSeetestScripGeneration =  1";	
				}else{}				
			} else if(jsonKeywordLibrary.getType().equalsIgnoreCase(IDPAConstants.SCRIPTLESS_EXECUTION)){
				if(jsonKeywordLibrary.getTestToolName() .equalsIgnoreCase(IDPAConstants.TEST_TOOL_SELENIUM)){
					filter = filter + " isSeleniumScriptless =  1";	
				}else if(jsonKeywordLibrary.getTestToolName().equalsIgnoreCase(IDPAConstants.TEST_TOOL_APPIUM)){
					filter = filter + " isAppiumScriptless =  1";	
				}else if(jsonKeywordLibrary.getTestToolName().equalsIgnoreCase(IDPAConstants.TEST_TOOL_SEETEST)){
					filter = filter + " isSeetestScriptless =  1";	
				}else{}
			}*/
			
			//Test Tool Id : Selenium=6, Appium=1, SeeTest=3, Protractor=24, eDAT=17, CodedUI=20, TestComplete=25, RestAssured =26; 
			if(jsonKeywordLibrary.getTestToolId() == IDPAConstants.TEST_TOOL_ID_SELENIUM){
				filter = filter + "isSelenium = 1";	
			} else if(jsonKeywordLibrary.getTestToolId() == IDPAConstants.TEST_TOOL_ID_APPIUM){
				filter = filter + "isAppium = 1";	
			} else if(jsonKeywordLibrary.getTestToolId() == IDPAConstants.TEST_TOOL_ID_SEETEST){
				filter = filter + "isSeeTest = 1";	
			} else if(jsonKeywordLibrary.getTestToolId() == IDPAConstants.TEST_TOOL_ID_PROTRACTOR){
				filter = filter + "isProtractor = 1";	
			} else if(jsonKeywordLibrary.getTestToolId() == IDPAConstants.TEST_TOOL_ID_EDAT){
				filter = filter + "isEDAT = 1";	
			} else if(jsonKeywordLibrary.getTestToolId() == IDPAConstants.TEST_TOOL_ID_CODEDUI){
				filter = filter + "isCodedui = 1";	
			} else if(jsonKeywordLibrary.getTestToolId() == IDPAConstants.TEST_TOOL_ID_TESTCOMPLETE){
				filter = filter + "isTestComplete = 1";	
			} else if(jsonKeywordLibrary.getTestToolId() == IDPAConstants.TEST_TOOL_ID_RESTASSURED){
				filter = filter + "isRestAssured = 1";	
			}else if(jsonKeywordLibrary.getTestToolId() == IDPAConstants.TEST_TOOL_ID_CUSTOM_CISCO){
				filter = filter + "isCustomCisco = 1";	
			}

			/*String errorMessage = this.commonService.duplicateName(""+jsonKeywordLibrary.getId(), "bdd_keyword_phrases", "id", "Keyword", filter);
			if (errorMessage != null) {
				jTableResponse = new JTableSingleResponse("ERROR", errorMessage);
				return jTableResponse;
			}*/
			
			keywordLibrary =  testCaseScriptGenerationService.getkeywordLibBTestoolAndLanguageAndTypeAndKeywordId(jsonKeywordLibrary.getTestToolId(),jsonKeywordLibrary.getLanguagID(),jsonKeywordLibrary.getType(),jsonKeywordLibrary.getId());
			
			jsonKeywordLibrary.setType("ScriptGeneration");
			//Setting up 'Status' of Keyword Library as if we are trying to save new Hot Keyword Jar
			jsonKeywordLibrary.setStatus(AOTCConstants.STATUS_COMPLETED);
			
			if(keywordLibrary == null){
				keywordLibrary = jsonKeywordLibrary.getKeywordLibrary();
			} else {
				keywordLibrary.setClassName(jsonKeywordLibrary.getClassName());
			}
				
			keywordLibrary.setUser(user);			
			String serverFolderPath = libFolderpath+"\\"+"lib"+"\\";	
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile multipartFile = null;
			String fileNamestr = "";
			String fileExtn = "";
			String handleNameToFileNameStr = "";
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileNamestr = multipartFile.getOriginalFilename();

				//long size=multipartFile.getSize();
				String Path = multipartFile.getName();
				fileExtn = fileNamestr.substring(fileNamestr.lastIndexOf(".")).toLowerCase();

				Long size = multipartFile.getSize();
				String fileSize = "0 MB";
				if(size > 0){
					fileSize = String.format("%.2f", ((size.floatValue() / 1024) / 1024))+" MB";
				}

				InputStream content = multipartFile.getInputStream();
				File filePath = new File(serverFolderPath);
				File tdFile = new File(serverFolderPath + "\\"+ fileNamestr);

				if (!filePath.isDirectory()) {
					FileUtils.forceMkdir(filePath);
				}			
				
				CommonUtility.copyInputStreamToFile(content, tdFile);

				keywordLibrary.setBinary(tdFile.getAbsolutePath());	
				content.close();

			}
			//Arun hot keywords
			String hotKeyWordFileFullPath = serverFolderPath+File.separator+fileNamestr;
			String hotKeyWordFileURL =  hotKeyWordFileFullPath.replace(File.separator, "//");

			/*//Standard URLS
			String atsgCommonFileFullPath = serverFolderPath+File.separator+"ATSG_Common.jar";
			String atsgCommonFileURL =  "file:///"+atsgCommonFileFullPath.replace(File.separator, "//");
			URL atsgCommonURL = new java.net.URL(atsgCommonFileURL);

			String atsgLogFileFullPath = serverFolderPath+File.separator+"commons-logging-1.1.jar";
			String atsgLogFileURL =  "file:///"+atsgLogFileFullPath.replace(File.separator, "//");
			URL atsgLogURL = new java.net.URL(atsgLogFileURL);
			
			String log4jFileFullPath = serverFolderPath+File.separator+"log4j-1.2.12.jar";
			String log4jFileFullURL =  "file:///"+log4jFileFullPath.replace(File.separator, "//");
			URL log4jLogURL = new java.net.URL(log4jFileFullURL);		
			try
            {
                  URLClassLoader loader = new java.net.URLClassLoader (new java.net.URL[] {atsgLogURL,log4jLogURL,atsgCommonURL,hotKeywordURL});
                  String fullyQualifiedClassName = jsonKeywordLibrary.getClassName();
                  Class<?> cl = Class.forName (fullyQualifiedClassName, true, loader);
                  loader.close ();
            }
            catch(ClassNotFoundException e){
                  jTableResponse = new JTableSingleResponse("ERROR", "Class name is not valid. Please provide a valid fully qualifed class name!");
                  log.error((Object)"JSON ERROR", (Throwable)e);
                  return jTableResponse;
            }
            catch(Exception e){
                  jTableResponse = new JTableSingleResponse("ERROR", "Class name is not valid. Please provide a valid fully qualifed class name!");
                  log.error((Object)"JSON ERROR", (Throwable)e);
                  return jTableResponse;
            }*/
			boolean isClassExist = false;
			try{
				ZipInputStream zip = new ZipInputStream(new FileInputStream(hotKeyWordFileURL));
				if(zip != null){
					ZipEntry entry = zip.getNextEntry();
					while(entry != null && !isClassExist){
						if(!entry.isDirectory() && entry.getName().endsWith(".class")){
							String className = entry.getName().replace("/", ".").replace(".class", "").trim();
							if(className.equalsIgnoreCase(jsonKeywordLibrary.getClassName())){
								isClassExist = true;
								break;
							}
						}						
						entry = zip.getNextEntry();
					}
					zip.close();
				}		
			} catch(Exception e){
				jTableResponse = new JTableSingleResponse("ERROR", "Class name is not valid. Please provide a valid fully qualifed class name!");
                log.error((Object)"JSON ERROR", (Throwable)e);
                return jTableResponse;
			}
			
			if(!isClassExist) {
				jTableResponse = new JTableSingleResponse("ERROR", "Class does not exist. Please provide a valid fully qualifed class name or upload valid jar!");
                log.error("Class is not present inside the uploaded hot keyword jar.");
                return jTableResponse;
			}
			
			keywordLib = keywordLibrary;			
			
			if(keywordLibrary.getId() != null){
				keywordLibId = keywordLibrary.getId(); 
				testCaseScriptGenerationService.updateKeywordLibrary(keywordLibrary);
			}else{
				keywordLibId = testCaseScriptGenerationService.saveKeywordLibrary( keywordLibrary);
				if(keywordLibId != null){
					keywordLib.setId(keywordLibId);
				}
			}
			keywordLib = new KeywordLibrary();
			keywordLibList = testCaseScriptGenerationService.getkeywordLibById(keywordLibId);

			if(keywordLibList != null && keywordLibList.size()>0){
				keywordLib = keywordLibList.get(0);
				if(keywordLib .getKeywords() != null){
					keywords =  keywordLib .getKeywords();
					keywordsFromDB = keywords;
				}
			}

			if(jsonKeywordLibrary.getType().equalsIgnoreCase(IDPAConstants.SCRIPT_GENERATION)){
				if(keywordLib.getTestToolMaster().getTestToolName() .equalsIgnoreCase(IDPAConstants.TEST_TOOL_SELENIUM)){
					keywordsFromDB.setIsSeleniumScripGeneration(1);
					keywordsFromDB.setIsSelenium(1);
				}
				if(keywordLib.getTestToolMaster().getTestToolName() .equalsIgnoreCase(IDPAConstants.TEST_TOOL_APPIUM)){
					keywordsFromDB.setIsAppiumScripGeneration(1);
					keywordsFromDB.setIsAppium(1);
				}
				if(keywordLib.getTestToolMaster().getTestToolName() .equalsIgnoreCase(IDPAConstants.TEST_TOOL_SEETEST)){
					keywordsFromDB.setIsSeetestScripGeneration(1);
					keywordsFromDB.setIsSeeTest(1);
				}
				if(keywordLib.getTestToolMaster().getTestToolName() .equalsIgnoreCase(IDPAConstants.TEST_TOOL_PROTRACTOR)){
					keywordsFromDB.setIsProtractorScripGeneration(1);
					keywordsFromDB.setIsProtractor(1);
				}
				if(keywordLib.getTestToolMaster().getTestToolName() .equalsIgnoreCase(IDPAConstants.TEST_TOOL_TESTCOMPLETE)){
					keywordsFromDB.setIsTestComplete(1);
					if(keywordsFromDB.getIsDesktop() != null && keywordsFromDB.getIsDesktop() == 1)
						keywordsFromDB.setIsTestCompleteScripGeneration(1);
					else
						keywordsFromDB.setIsTestCompleteWebScripGeneration(1);
				}
				if(keywordLib.getTestToolMaster().getTestToolName() .equalsIgnoreCase(IDPAConstants.TEST_TOOL_RESTASSURED)){
					keywordsFromDB.setIsRestAssuredScripGeneration(1);
					keywordsFromDB.setIsRestAssured(1);
				}
				if(keywordLib.getTestToolMaster().getTestToolName() .equalsIgnoreCase(IDPAConstants.TEST_TOOL_EDAT)){
					keywordsFromDB.setIsEDATScripGeneration(1);
					keywordsFromDB.setIsEdat(1);
				}
				if(keywordLib.getTestToolMaster().getTestToolName().equalsIgnoreCase(IDPAConstants.TEST_TOOL_CUSTOMCISCO)){
					keywordsFromDB.setIsCustomCiscoScripGeneration(1);
					keywordsFromDB.setIsCustomCisco(1);
				}
			}else if(jsonKeywordLibrary.getType().equalsIgnoreCase(IDPAConstants.SCRIPTLESS_EXECUTION)){
				if(keywordLib.getTestToolMaster().getTestToolName() .equalsIgnoreCase(IDPAConstants.TEST_TOOL_SELENIUM)){
					keywordsFromDB.setIsSeleniumScriptless(1);
				}
				if(keywordLib.getTestToolMaster().getTestToolName() .equalsIgnoreCase(IDPAConstants.TEST_TOOL_APPIUM)){
					keywordsFromDB.setIsAppiumScriptless(1);
				}
				if(keywordLib.getTestToolMaster().getTestToolName() .equalsIgnoreCase(IDPAConstants.TEST_TOOL_SEETEST)){
					keywordsFromDB.setIsSeetestScriptless(1);
				}
			}
			keywordsFromDB.setStatus(1);
			ProductType pm = productListService.getProductTypeById(jsonKeywordLibrary.getProductTypeId());
			if(pm != null){
				if(pm.getTypeName().equalsIgnoreCase("Device"))
					keywordsFromDB.setIsDevice(1);
				else if(pm.getTypeName().equalsIgnoreCase("Web"))
					keywordsFromDB.setIsWeb(1);
				else if(pm.getTypeName().equalsIgnoreCase("Embedded"))
					keywordsFromDB.setIsEmbedded(1);
				else if(pm.getTypeName().equalsIgnoreCase("Desktop"))
					keywordsFromDB.setIsDesktop(1);
				else if(pm.getTypeName().equalsIgnoreCase("Mobile"))
					keywordsFromDB.setIsMobile(1);
			}
			testCaseScriptGenerationService.updateBDDKeyWordsPhrase(keywordsFromDB);

			JsonKeywordLibrary jsonKeywrdLib = new JsonKeywordLibrary(keywordLib);

			jTableResponse = new JTableSingleResponse("OK","Successfully updated or added keyword library "+jsonKeywrdLib);
		}
		catch (Exception e) {
			jTableResponse = new JTableSingleResponse("ERROR", "Unable to save the keyword!"+ e);
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}

	@RequestMapping(value={"keywordLibrary.list"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse listKeywordLibraryByKeywordId(@RequestParam Integer keywordId) {

		List<JsonKeywordLibrary> jsonKeywordLibrary = null;
		KeywordLibrary keywordLibrary = null;
		JTableResponse jTableResponse = null;
		try {
			List<KeywordLibrary> keywrdLibraryList = testCaseScriptGenerationService.listKeywordLibraryByKeywordId(keywordId);
			ArrayList<JsonKeywordLibrary> jsonKeywordsLib = new ArrayList<JsonKeywordLibrary>();
			if(keywrdLibraryList != null){
				for(KeywordLibrary keywordLib : keywrdLibraryList){
					jsonKeywordsLib.add(new JsonKeywordLibrary(keywordLib));
				}
			}

			jTableResponse = new JTableResponse("OK", jsonKeywordsLib, testCaseScriptGenerationService.totalKeywordLibraryByKeywordId(keywordId));

		}
		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "unable to list keyword libraries !");
			log.error((Object)"JSON ERROR", (Throwable)e);
			return jTableResponse;
		}
		return jTableResponse;
	}

	@RequestMapping(value={"mykeywords.list"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse getMyKeywords(HttpServletRequest req, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		JTableResponse jTableResponse;
		try {
			Integer userId;
			List<BDDKeywordsPhrases> bddKeywordsPhrasesList = new ArrayList<BDDKeywordsPhrases>();
			UserList user = (UserList)req.getSession().getAttribute("USER");
			if(user.getUserRoleMaster().getUserRoleId() == 1){
				userId = -1 ; 
			}else{
				userId = user.getUserId();
			}
			bddKeywordsPhrasesList = this.testCaseScriptGenerationService.getMyKeywords(userId, jtStartIndex, jtPageSize);
			ArrayList<JsonBDDKeywordsPhrase> jsonBddKeywords = new ArrayList<JsonBDDKeywordsPhrase>();
			if (bddKeywordsPhrasesList == null || bddKeywordsPhrasesList.size() == 0) {
				jTableResponse = new JTableResponse("OK", jsonBddKeywords, 0);
			} else {
				for (BDDKeywordsPhrases bddkeywordsphrase : bddKeywordsPhrasesList) {
					jsonBddKeywords.add(new JsonBDDKeywordsPhrase(bddkeywordsphrase));
				}
				jTableResponse = new JTableResponse("OK", jsonBddKeywords, testCaseScriptGenerationService.getMyKeywordsSize(userId, jtStartIndex, jtPageSize));
				bddKeywordsPhrasesList = null;
			}
		}
		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Error Listing keyword!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}

	@RequestMapping(value={"bddkeywordsphrases.save"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableSingleResponse saveBDDKeyWordSphrases(HttpServletRequest req,@ModelAttribute JsonBDDKeywordsPhrase jsonBDDKeywordsPhrase, BindingResult result) {
		JTableSingleResponse jTableResponse = null;
		BDDKeywordsPhrases bddKeyWordsPhrasesFromUI = null;
		if (result.hasErrors()) {
			jTableResponse = new JTableSingleResponse("ERROR", "Invalid Form!");
		}
		try {
			bddKeyWordsPhrasesFromUI = testCaseScriptGenerationService.getBDDKeyWordsPhraseByKeywordPharse(jsonBDDKeywordsPhrase.getKeywordPhrase());
			if(bddKeyWordsPhrasesFromUI != null){
				jTableResponse = new JTableSingleResponse("ERROR", "Keyword Phrase already exists!");
				return jTableResponse;
			}
			if(bddKeyWordsPhrasesFromUI == null){
				bddKeyWordsPhrasesFromUI = jsonBDDKeywordsPhrase.getJsonBDDKeywordsPhrase();
			}
			UserList user = (UserList)req.getSession().getAttribute("USER");
			bddKeyWordsPhrasesFromUI.setUser(user);
			BDDKeywordsPhrases  bddKeyWordsPhrases = new BDDKeywordsPhrases();
			bddKeyWordsPhrases = bddKeyWordsPhrasesFromUI;
			if(bddKeyWordsPhrasesFromUI.getId() != null){
				testCaseScriptGenerationService.updateBDDKeyWordsPhrase(bddKeyWordsPhrasesFromUI);
			}
			else{
				Integer keywordId = testCaseScriptGenerationService.saveBDDKeyWordsPhrase(bddKeyWordsPhrasesFromUI);

				if(keywordId != null){
					bddKeyWordsPhrases.setId(keywordId);
				}
			}
			JsonBDDKeywordsPhrase jsonBDDKeywords =  new JsonBDDKeywordsPhrase(bddKeyWordsPhrases);

			jTableResponse = new JTableSingleResponse("OK", jsonBDDKeywords);
		}
		catch (Exception e) {
			jTableResponse = new JTableSingleResponse("ERROR", "Unable to update the keyword!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}


	@RequestMapping(value="get.testcase.script.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getTestCaseScriptList(@RequestParam Integer productId, @RequestParam Integer jtStartIndex,@RequestParam Integer jtPageSize) {

		log.info("get.testcase.script.list - inside");
		JTableResponse jTableResponse=null;
		try {

			List<TestCaseScript> tcScript = testCaseScriptGenerationService.getTestcaseScripts(productId,jtStartIndex,jtPageSize);

			List<JsonTestCaseAutomationScripts> jsonTCScripts = new ArrayList<JsonTestCaseAutomationScripts>();
			if (tcScript == null || tcScript.size() ==0 ) {

				jTableResponse = new JTableResponse("OK", jsonTCScripts, 0);
			}else{

				for (TestCaseScript tcscripts : tcScript) {
					jsonTCScripts.add(new JsonTestCaseAutomationScripts(tcscripts));
				}				
				jTableResponse = new JTableResponse("OK", jsonTCScripts,jsonTCScripts.size() );
				tcScript = null;
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error listing Testcase Script!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value="testcase.script.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addTestCaseScript( HttpServletRequest request, @ModelAttribute JsonTestCaseAutomationScripts jsonTestAutomationScripts , BindingResult result) {

		log.debug("Inside testcase.script.add");		
		JTableSingleResponse jTableSingleResponse;
		TestCaseScript testCaseScriptUI = null;
		TestCaseScript testCaseScriptDB = null;
		ProductMaster productMaster = null;
		String testCaseScriptName = "";
		try {	
			testCaseScriptUI = jsonTestAutomationScripts.getTestCaseScript();
			productMaster = productListService.getProductById(testCaseScriptUI.getProduct().getProductId());
			testCaseScriptName = testCaseScriptUI.getScriptName().trim();
			if(testCaseScriptName != "" && testCaseScriptName != null){			
				String filter="product.productId="+testCaseScriptUI.getProduct().getProductId();
				String errorMessage=commonService.duplicateName(testCaseScriptName, "TestCaseScript", "scriptName", "Test Script",filter);
				if(errorMessage != null){
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION", "Test script name already exists!");
				}
			}
			if(productMaster != null){
				testCaseScriptUI.setProduct(productMaster);
			}
			
			testCaseScriptGenerationService.addTestcaseScript(testCaseScriptUI);
			mongoDBService.addTestCaseScriptToMongoDB(testCaseScriptUI);
			jTableSingleResponse = new JTableSingleResponse("OK",jsonTestAutomationScripts);

		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Test Case Script!");
			log.error("JSON ERROR", e);
		}	        
		return jTableSingleResponse;		
	}

	@RequestMapping(value="testcase.script.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse updateTestCaseScript( HttpServletRequest request, @ModelAttribute JsonTestCaseAutomationScripts jsonTestAutomationScripts , BindingResult result) {

		log.debug("Inside testcase.script.update");		
		JTableSingleResponse jTableSingleResponse;
		TestCaseScript testCaseScriptUI = null;
		TestCaseScript testCaseScriptDB = null;
		String testCaseScriptName = "";
		List<TestCaseScriptHasTestCase> mappedTeseCaseAndTestScriptList= new ArrayList<TestCaseScriptHasTestCase>();
		try {	
			testCaseScriptUI = jsonTestAutomationScripts.getTestCaseScript();


			if(jsonTestAutomationScripts.getModifiedFieldTitle().equalsIgnoreCase("Test Case Script Name")){
				testCaseScriptName = testCaseScriptUI.getScriptName().trim();
				if(testCaseScriptName != "" && testCaseScriptName != null){						
					testCaseScriptDB = testCaseScriptGenerationService.getTestCaseScriptByName(testCaseScriptName);
					if(testCaseScriptDB != null){
						return jTableSingleResponse = new JTableSingleResponse("INFORMATION", "Test script name already exists!");
					}
				}
			}
			if(testCaseScriptUI != null) {
				List<TestCaseScriptHasTestCase> dbMappedTeseCaseAndTestScriptList=testCaseScriptGenerationService.getTestcaseAndScriptByScriptId(testCaseScriptUI.getScriptId());
				if(dbMappedTeseCaseAndTestScriptList != null && dbMappedTeseCaseAndTestScriptList.size() >0) {
					for(TestCaseScriptHasTestCase mappedTestCase: dbMappedTeseCaseAndTestScriptList) {
						mappedTeseCaseAndTestScriptList.add(mappedTestCase);
					}
				}
			}

			testCaseScriptGenerationService.updateTestcaseScript(testCaseScriptUI);


			if(mappedTeseCaseAndTestScriptList != null && mappedTeseCaseAndTestScriptList.size() >0) {
				for(TestCaseScriptHasTestCase mappedTestCase: mappedTeseCaseAndTestScriptList) {
					testCaseAutomationScriptDAO.addTestCaseScriptAssociation(mappedTestCase);
				}
			}


			jTableSingleResponse = new JTableSingleResponse("OK",jsonTestAutomationScripts);

		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating Test Case Script!");
			log.error("JSON ERROR", e);
		}	        
		return jTableSingleResponse;		
	}

	@RequestMapping(value="testcase.script.delete",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse deleteTestCaseScript( HttpServletRequest request, @RequestParam int scriptId) {

		JTableSingleResponse jTableSingleResponse = null;
		TestCaseScript testCaseScript = null;
		log.debug("Inside testcase.script.delete");
		try {	
			testCaseScript = testCaseScriptGenerationService.getTestCaseScriptById(scriptId);
			if(testCaseScript != null){
				testCaseScriptGenerationService.deleteTestcaseScript(testCaseScript);
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestCaseAutomationScripts(testCaseScript));
			}else{
				jTableSingleResponse = new JTableSingleResponse("ERROR","Error deleting Test Case Script!");
			}

		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error deleting Test Case Script!");
			log.error("JSON ERROR", e);
		}	        
		return jTableSingleResponse;		
	}

	@RequestMapping(value="unmapped.testcases.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listUnMappedTestCasesByProductId(@RequestParam int productId,@RequestParam int scriptId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("unmapped.testcases.list");
		JTableResponse jTableResponse=null;
		try {	
			List<Object[]> unMappedTestcaseListObj = testCaseScriptGenerationService.getUnMappedTestCasesByProductId(productId, scriptId, jtStartIndex, jtPageSize);

			JSONArray unMappedJsonArray = new JSONArray();
			for (Object[] row : unMappedTestcaseListObj) {
				JSONObject jsonObj =new JSONObject();
				jsonObj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
				jsonObj.put(IDPAConstants.ITEM_NAME, (String)row[1]);	
				unMappedJsonArray.add(jsonObj);					
			}				
			jTableResponse = new JTableResponse("OK", unMappedJsonArray,unMappedJsonArray.size());
			unMappedTestcaseListObj = null;					 

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching UnMappedTestCaseList for TestCaseScript!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value="testCases.mapped.to.testScript.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listMappedTestCasesByScriptId(@RequestParam int scriptId) {
		log.debug("testCases.mapped.to.testScript.list");
		JTableResponse jTableResponse;		
		try {	
			List<Object[]> mappedTestCasesObj = testCaseScriptGenerationService.getMappedTestCasesByScriptId(scriptId);
			JSONArray mappedJsonArray = new JSONArray();
			for (Object[] row : mappedTestCasesObj) {
				JSONObject jsonObj =new JSONObject();
				jsonObj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
				jsonObj.put(IDPAConstants.ITEM_NAME, (String)row[1]);		
				mappedJsonArray.add(jsonObj);					
			}				
			jTableResponse = new JTableResponse("OK", mappedJsonArray,mappedJsonArray.size());
			mappedTestCasesObj = null;
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching mapped Testcases for TestCaseScript!");
			log.error("JSON ERROR", e);
		}	        
		return jTableResponse;
	}

	@RequestMapping(value="unmapped.testcases.count",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse getUnMappedTestCasesCountByProductId(@RequestParam int productId, @RequestParam int scriptId) {
		JTableSingleResponse jTableSingleResponse;
		int unMappedTestCasesCount = 0;
		JSONObject unMappedTestCasesCountObj =new JSONObject();
		try {	
			unMappedTestCasesCount = testCaseScriptGenerationService.getUnMappedTestCasesCountByProductId(productId,scriptId);		    	
			unMappedTestCasesCountObj.put("unMappedTCCount", unMappedTestCasesCount);						
			jTableSingleResponse = new JTableSingleResponse("OK",unMappedTestCasesCountObj);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error fetching UnMappedTestCasesCount!");
			log.error("JSON ERROR fetching UnMappedTestCasesCount", e);	 
		}

		return jTableSingleResponse;
	}

	@RequestMapping(value="testCaseScript.map.or.unmap.to.testCase",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse mapOrUnmapTestCases(HttpServletRequest request, @RequestParam Integer scriptId,@RequestParam Integer testCaseId, @RequestParam String maporunmap) {
		JTableSingleResponse jTableSingleResponse;
		TestCaseList testCaseList = null;
		try {
			UserList userList = (UserList)request.getSession().getAttribute("USER");
			testCaseScriptGenerationService.updateTestCaseToTestCaseScript(scriptId, testCaseId, userList, maporunmap);

			List<JsonTestCaseList> jsonTestCaseList = new ArrayList<JsonTestCaseList>();
			if(testCaseList != null){
				jsonTestCaseList.add(new JsonTestCaseList(testCaseList));
			}				
			jTableSingleResponse = new JTableSingleResponse("OK",jsonTestCaseList);	

		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the TestCaseScript  & TestCase association!");
			log.error("JSON ERROR", e);	 
		}

		return jTableSingleResponse;
	}


	@RequestMapping(value="unmapped.testScripts.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listUnMappedTestScriptsByProductId(@RequestParam int productId,@RequestParam int testCaseId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("unmapped.testScripts.list");
		JTableResponse jTableResponse=null;
		try {	
			List<Object[]> unMappedTestScriptListObj = testCaseService.getUnMappedTestScriptsByProductId(productId, testCaseId, jtStartIndex, jtPageSize);

			JSONArray unMappedJsonArray = new JSONArray();
			for (Object[] row : unMappedTestScriptListObj) {
				JSONObject jsonObj =new JSONObject();
				jsonObj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
				jsonObj.put(IDPAConstants.ITEM_NAME, (String)row[1]);	
				unMappedJsonArray.add(jsonObj);					
			}				
			jTableResponse = new JTableResponse("OK", unMappedJsonArray,unMappedJsonArray.size());
			unMappedTestScriptListObj = null;					 

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching UnMappedTestScriptList for TestCase!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value="testScripts.mapped.to.testCase.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listMappedTestScriptsByTestCaseId(@RequestParam int testCaseId) {
		log.debug("testScripts.mapped.to.testCase.list");
		JTableResponse jTableResponse;		
		try {	
			List<Object[]> mappedTestScriptsObj = testCaseService.getMappedTestScriptsByTestCaseId(testCaseId);
			JSONArray mappedJsonArray = new JSONArray();
			for (Object[] row : mappedTestScriptsObj) {
				JSONObject jsonObj =new JSONObject();
				jsonObj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
				jsonObj.put(IDPAConstants.ITEM_NAME, (String)row[1]);		
				mappedJsonArray.add(jsonObj);					
			}				
			jTableResponse = new JTableResponse("OK", mappedJsonArray,mappedJsonArray.size());
			mappedTestScriptsObj = null;
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching mapped TestScripts for TestCase!");
			log.error("JSON ERROR", e);
		}	        
		return jTableResponse;
	}

	@RequestMapping(value="unmapped.testScripts.count",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse getUnMappedTestScriptsCountByProductId(@RequestParam int productId, @RequestParam int testCaseId) {
		JTableSingleResponse jTableSingleResponse;
		int unMappedTestScriptsCount = 0;
		JSONObject unMappedTestScriptsCountObj =new JSONObject();
		try {	
			unMappedTestScriptsCount = testCaseService.getUnMappedTestScriptsCountByProductId(productId,testCaseId);		    	
			unMappedTestScriptsCountObj.put("unMappedTCCount", unMappedTestScriptsCount);						
			jTableSingleResponse = new JTableSingleResponse("OK",unMappedTestScriptsCountObj);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error fetching unMappedTestScriptsCount!");
			log.error("JSON ERROR", e);	 
		}

		return jTableSingleResponse;
	}

	@RequestMapping(value="testCase.map.or.unmap.to.testCaseScript",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse mapOrUnmapTestScripts(HttpServletRequest request, @RequestParam Integer testCaseId,@RequestParam Integer scriptId, @RequestParam String maporunmap) {
		JTableSingleResponse jTableSingleResponse;
		TestCaseList testCaseList = null;
		try {

			UserList userList = (UserList)request.getSession().getAttribute("USER");
			testCaseScriptGenerationService.updateTestCaseToTestCaseScript(scriptId, testCaseId, userList, maporunmap);

			List<JsonTestCaseList> jsonTestCaseList = new ArrayList<JsonTestCaseList>();
			if(testCaseList != null){
				jsonTestCaseList.add(new JsonTestCaseList(testCaseList));
			}				
			jTableSingleResponse = new JTableSingleResponse("OK",jsonTestCaseList);	

		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the testcase  & testscript association!");
			log.error("JSON ERROR", e);	 
		}

		return jTableSingleResponse;
	}

	@RequestMapping(value="get.testscript.details",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getTestCaseScriptList(@RequestParam Integer scriptId) {

		log.info("get.testcase.script.list - inside");
		JTableResponse jTableResponse=null;
		try {

			List<TestCaseScript> tcScript = testCaseScriptGenerationService.getTestcaseScriptByScriptId(scriptId);

			List<JsonTestCaseAutomationScripts> jsonTCScripts = new ArrayList<JsonTestCaseAutomationScripts>();
			if (tcScript == null || tcScript.size() ==0 ) {

				jTableResponse = new JTableResponse("OK", jsonTCScripts, 0);
			}else{

				for (TestCaseScript tcscripts : tcScript) {
					jsonTCScripts.add(new JsonTestCaseAutomationScripts(tcscripts));
				}				
				jTableResponse = new JTableResponse("OK", jsonTCScripts,jsonTCScripts.size() );
				tcScript = null;
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error listing Testcase Script!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}


	@RequestMapping(value="get.mapped.testscripts.by.testcaseId",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getTestCaseScripsByTestcaseId(@RequestParam Integer testcaseId) {

		log.info("get.testcase.script.list - inside");
		JTableResponse jTableResponse=null;
		try {

			List<TestCaseScript> tcScript = testCaseScriptGenerationService.getTestCaseScripsByTestcaseId(testcaseId);

			List<JsonTestCaseAutomationScripts> jsonTCScripts = new ArrayList<JsonTestCaseAutomationScripts>();
			if (tcScript == null || tcScript.size() ==0 ) {

				jTableResponse = new JTableResponse("OK", jsonTCScripts, 0);
			}else{

				for (TestCaseScript tcscripts : tcScript) {
					jsonTCScripts.add(new JsonTestCaseAutomationScripts(tcscripts));
				}				
				jTableResponse = new JTableResponse("OK", jsonTCScripts,jsonTCScripts.size() );
				tcScript = null;
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error listing Testcase Script!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	//aotc - ilcm integration 

	@RequestMapping(value="product.version.testdata.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addTestData(HttpServletRequest req, @RequestParam Integer productVersionId, @RequestParam String name, @RequestParam String description, @RequestParam String tdType, @RequestParam String fileLocation, @RequestParam String fileName, @RequestParam String prefixName){
		JTableSingleResponse jTableSingleResponse = null;
		try { 
			UserList user = (UserList)req.getSession().getAttribute("USER");
			Attachment attachment = new Attachment();
			ProductMaster productMaster = new ProductMaster();
			Integer productId = -1 ;

			String productName = "";
			String versionName = "";
			if(productVersionId != 0 ){						
				ProductVersionListMaster pversion= productListService.getProductVersionListMasterById(productVersionId);
				productName = pversion.getProductMaster().getProductName();
				versionName = pversion.getProductVersionName();
				EntityMaster entityMaster = workPackageService.getEntityMasterByName(IDPAConstants.ENTITY_PRODUCT_VERSION);
				attachment.setEntityMaster(entityMaster);						
				attachment.setEntityId(productVersionId);
				attachment.setProduct(pversion.getProductMaster());
				productId = pversion.getProductMaster().getProductId();
			}

			if(tdType != null && tdType.equalsIgnoreCase("1")){
				tdType = AOTCConstants.TDTYPE_TESTDATA;
				attachment.setAttachmentType(tdType);							
			} else if(tdType != null && tdType.equalsIgnoreCase("2")){
				tdType = AOTCConstants.TDTYPE_OBJ_REPOSITORY;
				attachment.setAttachmentType(tdType);
			} else if(tdType != null && tdType.equalsIgnoreCase("3")){
				tdType = "EDAT";
				attachment.setAttachmentType("EDAT");
			} else if(tdType != null && tdType.equalsIgnoreCase("4")){
				tdType = AOTCConstants.TDTYPE_SEETEST_REPOSITORY;
				attachment.setAttachmentType(tdType);
			}
			else {
				tdType = "EDAT";
				attachment.setAttachmentType("EDAT");
			}
			Attachment attachmentFromDB = attachmentsService.getAttachmentByFileName(productId, prefixName,user.getUserId(),tdType);
			if(attachmentFromDB != null){
				attachment=attachmentFromDB;

			}



			if(description != null){
				attachment.setDescription(description);
			}
			if(prefixName!=null && !prefixName.equalsIgnoreCase("")){
				attachment.setAttachmentPrefixName(prefixName);
			}


			attachment.setCreatedBy(user);
			attachment.setModifiedBy(user);

			attachment.setLastModifiedDate(DateUtility.getCurrentTime());
			attachment.setModifiedDate(DateUtility.getCurrentTime());
			attachment.setUploadedDate(DateUtility.getCurrentTime());				

			String catalinaHome = System.getProperty("catalina.home");
			String serverFolderPath = catalinaHome + "\\webapps\\Attachments\\"+productName+"\\"+versionName+"\\";	
			if(tdType != null && tdType.equalsIgnoreCase("EDAT")){
				serverFolderPath = catalinaHome + "\\webapps"+File.separator + "TestScripts"+File.separator+"EDAT"+File.separator+user.getLoginId()+"\\"+productId+"\\";
			}


			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
			CommonsMultipartFile multipartFile = null;
			String fileNamestr = "";
			String fileExtn = "";
			String handleNameToFileNameStr = "";
			handleNameToFileNameStr = attachment.getAttachmentPrefixName().trim();
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileNamestr = multipartFile.getOriginalFilename();

				String Path = multipartFile.getName();
				fileExtn = fileNamestr.substring(fileNamestr.lastIndexOf(".")).toLowerCase();
				
				Long size = multipartFile.getSize();
				String fileSize = "0 MB";
				if(size > 0){
					fileSize = String.format("%.2f", ((size.floatValue() / 1024) / 1024))+" MB";
				}
				attachment.setAttributeFileSize(fileSize);
				if(fileNamestr != null){
					attachment.setAttributeFileName(fileNamestr);
				}
				if(fileExtn != null){
					attachment.setAttributeFileExtension(fileExtn);
				}
				InputStream content = multipartFile.getInputStream();
				File filePath = new File(serverFolderPath);
				File tdFile = new File(serverFolderPath + "\\"+ fileNamestr);
				File newRenamedFile = new File(serverFolderPath + "\\"+ handleNameToFileNameStr+fileExtn);

				if (!filePath.isDirectory()) {
					FileUtils.forceMkdir(filePath);
				}						
				CommonUtility.copyInputStreamToFile(content, tdFile);
				attachment.setAttributeFileURI(tdFile.getAbsolutePath());	
				content.close();
				if(tdFile.exists()){
					if(!fileNamestr.equals(handleNameToFileNameStr+fileExtn)){
						FileUtils.copyFile(tdFile, newRenamedFile);  							
						if(newRenamedFile.exists()){

							attachment.setAttributeFileName(handleNameToFileNameStr);
							attachment.setAttributeFileURI(newRenamedFile.getAbsolutePath());
							new File(serverFolderPath + "\\"+ fileNamestr).delete();
						}
					}
				}



			}
			String extensionName = "";
			if(attachment.getAttributeFileName() != null)
				extensionName = FilenameUtils.getExtension(attachment.getAttributeFileName());
			attachment.setAttachmentName(name);
			productMaster.setProductId(productId);
			attachment.setProduct(productMaster);
			attachmentsService.addTestDataAttachment(attachment);

			if(tdType != null && tdType.equalsIgnoreCase(AOTCConstants.TDTYPE_TESTDATA)){
				testCaseScriptGenerationService.addTestDataItems(attachment);					
			} else if(tdType != null && tdType.equalsIgnoreCase(AOTCConstants.TDTYPE_OBJ_REPOSITORY)){
				testCaseScriptGenerationService.addUIObjects(attachment);
			}
			
			jTableSingleResponse = new JTableSingleResponse("OK", "TestData had been added");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
			log.error("JSON ERROR", e);
		}			        
		return jTableSingleResponse;
	}
	@RequestMapping(value={"getproducttype.list"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponseOptions listProductTypeAndEngine() {
		JTableResponseOptions jTableResponseOptions = null;
		List<ProductType> productTypeList = new ArrayList<ProductType>();
		List<JsonProductType> jsonProductTypeList = new ArrayList<JsonProductType>();
		try {
			productTypeList = productTypeDAO.getProductList();
			for(ProductType pt : productTypeList){
				jsonProductTypeList.add(new JsonProductType(pt));
			}
			jTableResponseOptions = new JTableResponseOptions("OK",jsonProductTypeList, true);
		}catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR", "Unable to show tags!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponseOptions;
	}

	@RequestMapping(value={"getlanguage.list"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponseOptions listLanguage() {
		JTableResponseOptions jTableResponseOptions = null;
		List<TestToolMaster> testToolMasterList = new ArrayList<TestToolMaster>();
		List<JsonTestToolMaster> jsonTestToolMasterList = new ArrayList<JsonTestToolMaster>();
		try {
			testToolMasterList = testToolMasterDAO.list();
			for(TestToolMaster t : testToolMasterList){
				jsonTestToolMasterList.add(new JsonTestToolMaster(t));
			}
			jTableResponseOptions = new JTableResponseOptions("OK",jsonTestToolMasterList, true);
		}catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR", "Unable to show tags!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponseOptions;
	}	

	@RequestMapping(value={"testcase.versions.list"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse getTestCaseStoryVersionsList(HttpSession session,HttpServletRequest request, @RequestParam Integer testCaseId ,@RequestParam String scriptType, @RequestParam String testEngine, @RequestParam Integer productId) {
		JTableResponse jTableResponse = null;
		List<JsonTestCaseScript> jsonTestCaseScriptList = null;
		try {

			if(testCaseId != null){
				jsonTestCaseScriptList = this.testCaseScriptGenerationService.getTestAutomationStoryWithVersions(testCaseId,scriptType,testEngine,productId,session);
			}
			if(jsonTestCaseScriptList != null){
				jTableResponse = new JTableResponse("OK", jsonTestCaseScriptList, jsonTestCaseScriptList.size());
			}
		}
		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Error Saving Script!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}

	@RequestMapping(value={"bddkeywordsphrases.list"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse getBDDKeywordsPhrasesList(HttpServletRequest req,@RequestParam String productType, @RequestParam String testTool, @RequestParam Integer status, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		JTableResponse jTableResponse;
		try {
			List<BDDKeywordsPhrases> bddKeywordsPhrasesList = new ArrayList<BDDKeywordsPhrases>();
			Map<String, String> searchStrings = new HashMap<String, String>();

			String searchKeywordsPhrase = req.getParameter("searchKeywordsPhrase");
			String searchTags = req.getParameter("searchTags");
			String searchKeyDescription = req.getParameter("searchKeyDescription");
			String searchIsSelenium = req.getParameter("searchIsSelenium");
			if(searchIsSelenium != null && searchIsSelenium != "" && !searchIsSelenium.trim().isEmpty() ){
				if(searchIsSelenium .equalsIgnoreCase("y") || searchIsSelenium .equalsIgnoreCase("e") || searchIsSelenium .equalsIgnoreCase("s") || searchIsSelenium .equalsIgnoreCase("ye") || searchIsSelenium .equalsIgnoreCase("es") || searchIsSelenium .equalsIgnoreCase("yes")){
					searchIsSelenium = "1"; 
				}else if(searchIsSelenium .equalsIgnoreCase("n") || searchIsSelenium .equalsIgnoreCase("o") || searchIsSelenium .equalsIgnoreCase("no") ){
					searchIsSelenium = "0";
				}
			}
			String searchIsAppium = req.getParameter("searchIsAppium");
			if(searchIsAppium != null && searchIsAppium !="" && !searchIsAppium.trim().isEmpty() ){
				if(searchIsAppium .equalsIgnoreCase("y") || searchIsAppium .equalsIgnoreCase("e") || searchIsAppium .equalsIgnoreCase("s") || searchIsAppium .equalsIgnoreCase("ye") || searchIsAppium .equalsIgnoreCase("es") || searchIsAppium .equalsIgnoreCase("yes")){
					searchIsAppium = "1"; 
				}else if(searchIsAppium .equalsIgnoreCase("n") || searchIsAppium .equalsIgnoreCase("o") || searchIsAppium .equalsIgnoreCase("no") ){
					searchIsAppium = "0";
				}
			}
			String searchIsSeeTest = req.getParameter("searchIsSeeTest");
			if(searchIsSeeTest != null && searchIsSeeTest !="" &&  !searchIsSeeTest.trim().isEmpty() ){
				if(searchIsSeeTest .equalsIgnoreCase("y") || searchIsSeeTest .equalsIgnoreCase("e") || searchIsSeeTest .equalsIgnoreCase("s") || searchIsSeeTest .equalsIgnoreCase("ye") || searchIsSeeTest .equalsIgnoreCase("es") || searchIsSeeTest .equalsIgnoreCase("yes")){
					searchIsSeeTest = "1"; 
				}else if(searchIsSeeTest .equalsIgnoreCase("n") || searchIsSeeTest .equalsIgnoreCase("o") || searchIsSeeTest .equalsIgnoreCase("no") ){
					searchIsSeeTest = "0";
				}
			}

			String searchIsCodedUI = req.getParameter("searchIsCodedUI");
			if(searchIsCodedUI != null && searchIsCodedUI !="" &&  !searchIsCodedUI.trim().isEmpty() ){
				if(searchIsCodedUI .equalsIgnoreCase("y") || searchIsCodedUI .equalsIgnoreCase("e") || searchIsCodedUI .equalsIgnoreCase("s") || searchIsCodedUI .equalsIgnoreCase("ye") || searchIsCodedUI .equalsIgnoreCase("es") || searchIsCodedUI .equalsIgnoreCase("yes")){
					searchIsCodedUI = "1"; 
				}else if(searchIsCodedUI .equalsIgnoreCase("n") || searchIsCodedUI .equalsIgnoreCase("o") || searchIsCodedUI .equalsIgnoreCase("no") ){
					searchIsCodedUI = "0";
				}
			}

			String searchIsTestComplete = req.getParameter("searchIsTestComplete");
			if(searchIsTestComplete != null && searchIsTestComplete !="" &&  !searchIsTestComplete.trim().isEmpty() ){
				if(searchIsTestComplete .equalsIgnoreCase("y") || searchIsTestComplete .equalsIgnoreCase("e") || searchIsTestComplete .equalsIgnoreCase("s") || searchIsTestComplete .equalsIgnoreCase("ye") || searchIsTestComplete .equalsIgnoreCase("es") || searchIsTestComplete .equalsIgnoreCase("yes")){
					searchIsTestComplete = "1"; 
				}else if(searchIsTestComplete .equalsIgnoreCase("n") || searchIsTestComplete .equalsIgnoreCase("o") || searchIsTestComplete .equalsIgnoreCase("no") ){
					searchIsTestComplete = "0";
				}
			}

			searchStrings.put("searchKeywordsPhrase", searchKeywordsPhrase);
			searchStrings.put("searchTags", searchTags);
			searchStrings.put("searchDescription", searchKeyDescription);
			searchStrings.put("searchIsSelenium", searchIsSelenium);
			searchStrings.put("searchIsAppium", searchIsAppium);
			searchStrings.put("searchIsSeeTest", searchIsSeeTest);
			searchStrings.put("searchIsCodedUI", searchIsCodedUI);
			searchStrings.put("searchIsTestComplete", searchIsTestComplete);

			bddKeywordsPhrasesList = this.testCaseScriptGenerationService.getBDDKeywordsPhrasesList(productType, testTool,status, jtStartIndex, jtPageSize,searchStrings);
			ArrayList<JsonBDDKeywordsPhrase> jsonBddKeywords = new ArrayList<JsonBDDKeywordsPhrase>();
			if (bddKeywordsPhrasesList == null || bddKeywordsPhrasesList.size() == 0) {
				jTableResponse = new JTableResponse("OK", jsonBddKeywords, 0);
			} else {
				for (BDDKeywordsPhrases bddkeywordsphrase : bddKeywordsPhrasesList) {
					jsonBddKeywords.add(new JsonBDDKeywordsPhrase(bddkeywordsphrase));
				}
				jTableResponse = new JTableResponse("OK", jsonBddKeywords,bddKeywordsPhrasesList.size());
				bddKeywordsPhrasesList = null;
			}
		}
		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Error Listing keyword!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	@RequestMapping(value={"product.testcase.automationscript.get"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse viewTestCaseAutomationScriptContent(HttpSession session, @RequestParam Integer testCaseId, @RequestParam String scriptType, @RequestParam String testEngine, @RequestParam String productTypeName, @RequestParam String objectRepOption, @RequestParam String testDataOption,@RequestParam Integer productId) {
		JTableResponse jTableResponse;
		log.info((Object)("testEngine=" + testEngine + "TestCaseId=" + testCaseId + "Script Type :"+scriptType+" ,productTypeName=" + productTypeName + " ,objectRepOption=" + objectRepOption + " ,testDataOption= " + testDataOption));
		Attachment objectRepAttach = null;
		Attachment testDataAttach = null;
		String testDataFileUrl = null;
		String objectRepositoryUrl = null;
		try {
			if (testCaseId == null) {
				jTableResponse = new JTableResponse("ERROR", "Unable to get script as Id is not available");
				return jTableResponse;
			}
			if (testDataOption == null || testDataOption  == "") {
				testDataOption = "-1";
			}

			if (objectRepOption == null || objectRepOption == "") {
				objectRepOption = "-1";
			}
			JsonTestCaseScript jsonScript = this.testCaseScriptGenerationService.getTestCaseAutomationScript(testCaseId, scriptType, testEngine, "", objectRepOption, testDataOption,amdocsMode,productId, session);
			//Set the editing status for Test script
			log.info("Allow concurrent editing setting : " + testStoryEditingAllowMultipleUsers);
			if (!testStoryEditingAllowMultipleUsers) {
				String editingUser = testCaseScriptGenerationService.getTestCaseStoryEditingUser(testCaseId);
				if (!(editingUser == null || editingUser.isEmpty())) {
					jsonScript.setEditingUser(editingUser);
					jsonScript.setBeingEdited(true);
				} else {
					jsonScript.setBeingEdited(false);
					jsonScript.setEditingUser("");
					Date startTime = new Date(System.currentTimeMillis());		
					UserList user=(UserList)session.getAttribute("USER");
					if (user != null) { 
						String userName = user.getLoginId();
						testCaseScriptGenerationService.updateTestStoryEditingStatus(testCaseId, user.getLoginId() + " [" + startTime.toString() + "]", AOTCConstants.ATSG_STARTED_EDITING,user.getUserId());
					}
				}
			} else {
				jsonScript.setBeingEdited(false);
				jsonScript.setEditingUser("");
			}
			List<SCMSystem> scmList = defectManagementService.listSCMManagementSystem(productId);
			String isSCMSystemAvailable = "No";
			for(SCMSystem scm : scmList){
				if(scm.getIsPrimary() != null && scm.getIsPrimary() == 1 && scm.getStatus() != null && scm.getStatus() == 1){
					isSCMSystemAvailable = "Yes";
				}
			}
			jsonScript.setIsSCMSystemAvaialble(isSCMSystemAvailable);
			ArrayList<JsonTestCaseScript> jsonScripts = new ArrayList<JsonTestCaseScript>();
			if (jsonScript != null) {
				jsonScripts.add(jsonScript);
				jTableResponse = new JTableResponse("OK", jsonScripts, 1);
			} else {
				jTableResponse = new JTableResponse("ERROR", "Unable to get script");
			}
		}
		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Error Saving Script!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	@RequestMapping(value={"list.testData.and.objectRepository.FileContent"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse listTestDataAndObjectRepositoryFileContent(HttpSession session, @RequestParam Integer productId, @RequestParam Integer attachmentId,@RequestParam String type,@RequestParam String filter,@RequestParam String toolName) {

		List<String> jsonTestCaseScriptAttachmentFileContent = null;
		JTableResponseOptions options = new JTableResponseOptions();
		try {
			if (attachmentId == null) {
				JTableResponse jTableResponse = new JTableResponse("ERROR", "File is not available !");
				return jTableResponse;
			}
			if(filter == null || filter == ""){
				filter = "-1";
			}

			if(null != toolName && !toolName.isEmpty() && toolName.equalsIgnoreCase("EDAT") && type.equalsIgnoreCase("ObjectRepository")){			 			 

				if(!eDatConfigFileName.isEmpty()|| !eDatConfigFileName.equalsIgnoreCase("") || !eDatConfigFileName.equalsIgnoreCase(null)){
					jsonTestCaseScriptAttachmentFileContent = getEDATObjectItems(session, productId,eDatConfigFileName.replace(".xml", ""));
				}else{
					options = listEdatConfigFiles(request,productId,0);
					JTableResponseOptionsModel model = (JTableResponseOptionsModel) options.getOptions().get(0);
					eDatConfigFileName = model.getDisplayText().replace(".xml", "");
					jsonTestCaseScriptAttachmentFileContent = getEDATObjectItems(session, productId,model.getDisplayText().replace(".xml", ""));
				}


			} else {
				jsonTestCaseScriptAttachmentFileContent = testCaseScriptGenerationService.loadAttachmentKeywords(session, productId,attachmentId,type,filter);
			}

			if (jsonTestCaseScriptAttachmentFileContent == null) {
				JTableResponse jTableResponse = new JTableResponse("SUCCESS", "File is not available ! ");
				return jTableResponse;
			}
			JTableResponse jTableResponse = new JTableResponse("OK", jsonTestCaseScriptAttachmentFileContent, jsonTestCaseScriptAttachmentFileContent.size());
			return jTableResponse;
		}
		catch (Exception e) {
			JTableResponse jTableResponse = new JTableResponse("ERROR", "File is not available !");
			log.error((Object)"JSON ERROR", (Throwable)e);
			return jTableResponse;
		}
	}
	private List<String> getEDATObjectItems(HttpSession session, Integer productId,String xmlFileName){
		List<String> uiObjectItems = new ArrayList<String>();
		try{
			UserList user = (UserList)session.getAttribute("USER");
			String xmlConfigFileString = System.getProperty("catalina.home")+eDatConfigFolder+File.separator+"EDAT"+File.separator+user.getLoginId()+File.separator+productId+File.separator+xmlFileName+".xml";
			File xmlConfigFile = new File(xmlConfigFileString);			
			if(xmlConfigFile.exists()){
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlConfigFile);				
				doc.getDocumentElement().normalize();
				Element docEle = doc.getDocumentElement();
				NodeList nList = docEle.getChildNodes();
				Set<String> parentNodes = retrieveParentNodes(nList);
				for(String parentNode : parentNodes){
					iterateChildNodes(docEle.getElementsByTagName(parentNode),uiObjectItems,parentNode);
				}
			}									
		}catch(Exception e){
			log.error("JSON ERROR", e);	   
		}
		return uiObjectItems;
	}
	private Set<String> retrieveParentNodes(NodeList childNodeList){
		Set<String> parentNodes = new HashSet<String>();
		try{			
			for(int temp = 0; temp < childNodeList.getLength(); temp++) {
				Node nNode = childNodeList.item(temp);
				nNode.getParentNode();
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					parentNodes.add(nNode.getNodeName());
				}
			}		
		}catch(Exception e){
			log.error("XML Parent Node Parse Error : "+e);
		}
		return parentNodes;
	}

	private List<String> iterateChildNodes(NodeList childNodeList,List<String> uiObjectItems,String parentNodeName){
		try{
			for(int temp = 0; temp < childNodeList.getLength(); temp++) {
				Node nNode = childNodeList.item(temp);				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					if (nNode.hasAttributes()) {
						NamedNodeMap nodeMap = nNode.getAttributes();
						for (int i = 0; i < nodeMap.getLength(); i++) {
							Node node = nodeMap.item(i);
							if(null != node.getNodeName() && node.getNodeName().equalsIgnoreCase("Label")){								
								uiObjectItems.add(parentNodeName.concat(".").concat(node.getNodeValue()));								
							}
						}
					}					
					if (nNode.hasChildNodes()) {
						iterateChildNodes(nNode.getChildNodes(), uiObjectItems,parentNodeName);
					}
				}
			}
		}catch(Exception e){
			log.error("XML Children Node Parse Error : "+e);
		}
		return uiObjectItems;
	}

	@RequestMapping(value={"update.orsave.atsg.parameteres"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableSingleResponse upateorsavAtsgParameters(HttpServletRequest request, @RequestParam Map<String, String> mapData) {
		JTableSingleResponse jTableSingleResponse;
		try {
			Integer testCaseId = Integer.parseInt(mapData.get("testCaseId"));
			String testToolName = mapData.get("testToolName");
			Integer atsgParameterId = Integer.parseInt(mapData.get("atsgParameterId"));
			AtsgParameters atsg = new AtsgParameters();
			if (atsgParameterId != -1) {
				atsg.setAtsgId(atsgParameterId);
			} else {
				AtsgParameters atsgFromDb = this.attachmentsService.getAtsgScriptParametersByTestCaseId(testCaseId.intValue());
				if (atsgFromDb != null) {
					atsg.setAtsgId(atsgFromDb.getAtsgId());
				}
			}
			atsg.setTestEngine(testToolName);
			atsg.setTestCaseId(testCaseId);
			this.attachmentsService.upateorsavAtsgParameters(atsg);
			JsonAtsg jsonatsg = new JsonAtsg(atsg);
			jTableSingleResponse = new JTableSingleResponse("OK", (Object)jsonatsg);
		}
		catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error adding atsg parameters!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableSingleResponse;
	}
	@RequestMapping(value={"product.testcase.automationscript.close"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public void closeTestCaseAutomationScriptContent(HttpServletRequest req, @RequestParam Integer testCaseId) {

		log.info("Inside closeTestCaseAutomationScriptContent : This will mark TS as not being edited" );

		if (testStoryEditingAllowMultipleUsers) 
			return;

		if (testCaseId == null)
			return;

		UserList user=(UserList)req.getSession().getAttribute("USER");
		if (user != null) {
			String userName = user.getLoginId();
			testCaseScriptGenerationService.updateTestStoryEditingStatus(testCaseId, userName, AOTCConstants.ATSG_FINISHED_EDITING,user.getUserId());
		}
	}
	@RequestMapping(value={"teststory.newversion.save"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableSingleResponse saveTestCaseStoryAsNewVersion(HttpServletRequest request, @RequestParam Map<String, String> mapData) {
		JTableSingleResponse jTableSingleResponse = null;
		try {
			Integer testCaseId = Integer.parseInt(mapData.get("testCaseId"));
			String testEngine = mapData.get("testEngine");
			String edatSelectedCongileFile = mapData.get("selectedConfigFile");
			if (testCaseId == null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to save story as Id is not available");
				return jTableSingleResponse;
			}
			String automationScript = mapData.get("script");
			String message = this.testCaseScriptGenerationService.saveTestCaseAutomationStory(testCaseId,testEngine, automationScript, edatSelectedCongileFile);
			
			jTableSingleResponse = message.startsWith("SUCCESS") ? new JTableSingleResponse("OK", "Story saved as new version") : new JTableSingleResponse("ERROR", message);
			/*String SVNResponse = null;
			 if(message != null && !message.isEmpty()){	
				SVNResponse = this.testCaseScriptGenerationService.checkInToSVN(testCaseId,automationScript, "Story");
			}			
			
			if(SVNResponse == null){
				jTableSingleResponse = message.startsWith("SUCCESS") ? new JTableSingleResponse("OK", "Story saved as new version") : new JTableSingleResponse("ERROR", message);
				return jTableSingleResponse;
			}else{
				if(message.startsWith("SUCCESS") && SVNResponse != null && !SVNResponse.equalsIgnoreCase("Success"))
					return new JTableSingleResponse("OK", SVNResponse+"!. But Story Saved Successfully");
				else if(message.startsWith("SUCCESS") && SVNResponse != null && SVNResponse.equalsIgnoreCase("Success"))
					return new JTableSingleResponse("OK", "Story Saved Successfully");
				else if(message.startsWith("SUCCESS") && SVNResponse != null && !SVNResponse.equalsIgnoreCase("Success"))
					return new JTableSingleResponse("ERROR", message);
			}*/
			
		}
		catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error Saving Story!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableSingleResponse;
	}

	@RequestMapping(value={"generatedscript.save"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableSingleResponse saveGeneratedScript(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> mapData ) {
		JTableSingleResponse jTableSingleResponse = null;
		try {
			TestStoryGeneratedScripts generatedScript;
			Integer testCaseStoryId = Integer.parseInt(mapData.get("testCaseStoryId"));
			String script = mapData.get("script");
			String testEngine = mapData.get("testEngine");
			String languageName = mapData.get("languageName");
			String downloadPath = mapData.get("downloadPath");
			String codeGenerationMode = mapData.get("codeGenerationMode");
			UserList user=(UserList)request.getSession().getAttribute("USER");
			if (testCaseStoryId == null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to save script as Id is not available");
				return jTableSingleResponse;
			}else{
				generatedScript = this.testCaseScriptGenerationService.saveGeneratedTestScript(null,testCaseStoryId , script, testEngine, languageName, user.getUserId(), downloadPath , codeGenerationMode);
			}
			if(generatedScript != null){
				jTableSingleResponse = new JTableSingleResponse("OK", "Script Saved");
			}
		}
		catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error Saving Script!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableSingleResponse;
	}

	@RequestMapping(value={"generatedscript.list"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse getgeneratedscript(HttpServletRequest request, @RequestParam Integer testCaseStoryId, @RequestParam String languageName, @RequestParam String testEngine , @RequestParam String codeGenerationMode) {
		JTableResponse jTableResponse = null;
		JsonTestStoryGeneratedScripts jsonTestStoryGeneratedScripts = null;
		try {

			if(testCaseStoryId != null){
				jsonTestStoryGeneratedScripts = this.testCaseScriptGenerationService.getAutomationScript(testCaseStoryId,languageName,testEngine,codeGenerationMode);
			}
			
			if(jsonTestStoryGeneratedScripts != null){
				jTableResponse = new JTableResponse("OK", jsonTestStoryGeneratedScripts);
			}else{
				jTableResponse = new JTableResponse("ERROR", "Script is not available for this story");
			}
		}
		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Error Listing generated script!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	@RequestMapping(value={"generatedscript.update"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse updateGeneratedScript(HttpServletRequest request,@RequestParam Map<String, String> mapData) {
		JTableResponse jTableResponse = null;
		JsonTestStoryGeneratedScripts jsonTestStoryGeneratedScripts = null;
		String SVNResponse = null;
		try {
			Integer generatedScriptId = Integer.parseInt(mapData.get("generatedScriptId"));
			String updatedScript = mapData.get("updatedScript");
			String languageName = mapData.get("languageName");
			String testToolName = mapData.get("testToolName");
			String codeGenerationMode = mapData.get("codeGenerationMode");
			UserList user=(UserList)request.getSession().getAttribute("USER");
			if(generatedScriptId != null && updatedScript !=null & user != null){
				jsonTestStoryGeneratedScripts = this.testCaseScriptGenerationService.updatedGeneartedScript(generatedScriptId, updatedScript, languageName, user.getLoginId(), testToolName, codeGenerationMode );
			}
			if(jsonTestStoryGeneratedScripts != null){
				jTableResponse = new JTableResponse("OK", jsonTestStoryGeneratedScripts);
			}
			/*if(jsonTestStoryGeneratedScripts != null){
				//jTableSingleResponse = new JTableSingleResponse("OK", "Script Saved");
				byte[] data = updatedScript.getBytes();
				ConnectorCredentials connectorCredentials = new ConnectorCredentials();
				TestCaseAutomationStory story = testCaseAutomationScriptDAO.getByTestCaseStoryByAutomationStoryId(generatedScriptId);
				String tcName = story.getTestCase().getTestCaseName()+".java";
				String packagePath = "";
				if(jsonTestStoryGeneratedScripts.getOutputPackage() != null)
					packagePath = jsonTestStoryGeneratedScripts.getOutputPackage();
				String scriptToSVN = updatedScript;

				if(story.getTestCase() != null && story.getTestCase().getProductMaster() != null){
					int productId =story.getTestCase().getProductMaster().getProductId();
					List<SCMSystem> scmManagementSystems=defectManagementService.listSCMManagementSystem(productId);
					if(scmManagementSystems != null && !scmManagementSystems.isEmpty()){
						SCMSystem scmSystem = scmManagementSystems.get(0);
						String pwd  = passwordEncryptionService.decrypt(scmSystem.getConnectionPassword().trim());
						SVNResponse = connectorCredentials.commitNewFileToSVN(scmSystem.getConnectionProperty2(), scmSystem.getConnectionUserName(), pwd, tcName, scriptToSVN.replaceAll("(\\\\n\\\\r|\\\\n|\\\\r)", System.lineSeparator()), packagePath.replace(".","/")+"/");
					}
				}
				if(SVNResponse != null && !SVNResponse.equalsIgnoreCase("Success"))
					jTableResponse = new JTableResponse("OK", jsonTestStoryGeneratedScripts,SVNResponse);
				else 
					jTableResponse = new JTableResponse("OK", jsonTestStoryGeneratedScripts);
			}*/
		}
		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Error Updating generated script!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	@RequestMapping(value={"testDataItems.list"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse listTestDataItemsByProjectId(HttpServletRequest request, @RequestParam Integer productId, @RequestParam Integer testDataFilterId,@RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		JTableResponse jTableResponse = null;
		log.debug((Object)"inside amdocs.pageobject.list");
		UserList user = (UserList)request.getSession().getAttribute("USER");
		try {
			List<TestDataItems> testDataItemsList = new ArrayList<TestDataItems>();
			testDataItemsList = testCaseScriptGenerationService.listTestDataItemsByProductId(productId,testDataFilterId,user.getUserId(),jtStartIndex,jtPageSize);

			ArrayList<JsonTestDataItems> jsonTestDataItemsList = new ArrayList<JsonTestDataItems>();
			for (TestDataItems testDataItems : testDataItemsList) {
				jsonTestDataItemsList.add(new JsonTestDataItems(testDataItems));
			}
			jTableResponse = new JTableResponse("OK", jsonTestDataItemsList,testCaseScriptGenerationService.totalTestDataItemsByProductId(productId,testDataFilterId, user.getUserId()));
		}

		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Unable to show testDataitems!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	@RequestMapping(value={"testDataItems.save.and.update"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableSingleResponse addTestDataItems(HttpServletRequest req,@ModelAttribute JsonTestDataItems jsonTestDataItems, BindingResult result) {
		JTableSingleResponse jTableResponse = null;
		TestDataItems testDataItemsFromUI = null;
		Integer testDataItemId = null;
		if (result.hasErrors()) {
			jTableResponse = new JTableSingleResponse("ERROR", "Invalid Form!");
		}
		try {

			UserList user = (UserList)req.getSession().getAttribute("USER");

			testDataItemsFromUI = jsonTestDataItems.getTestDataItems();

			if(testDataItemsFromUI.getTestDataItemId() == null){
				testDataItemsFromUI.setUserlist(user);
				TestDataItems testDataItemsFromDB	= testCaseScriptGenerationService.getTestDataItemByItemName(testDataItemsFromUI.getDataName(),jsonTestDataItems.getProductId(),null);
				if(testDataItemsFromDB == null){
					testDataItemId = testCaseScriptGenerationService.addTestDataItems(testDataItemsFromUI);
				}else if(testDataItemsFromDB.getUserlist().getUserId() == user.getUserId()){
					//if(){}
					testDataItemsFromUI.setModifiedDate(new Date());
					testDataItemsFromUI.setTestDataItemId(testDataItemsFromDB.getTestDataItemId());
					testCaseScriptGenerationService.updateTestDataItems(testDataItemsFromUI);
				}else{
					jTableResponse = new JTableSingleResponse("ERROR", testDataItemsFromDB.getDataName()+" already had been created by "+testDataItemsFromDB.getUserlist().getUserId());
					return jTableResponse;
				}

			}else{
				testDataItemsFromUI.setUserlist(user);
				testCaseScriptGenerationService.updateTestDataItems(testDataItemsFromUI);
			}


			TestDataItems  testDataItems = new TestDataItems();
			testDataItems = testDataItemsFromUI;

			if(testDataItemId != null){
				testDataItems.setTestDataItemId(testDataItemId);
			}

			JsonTestDataItems jsonTestDataItem =  new JsonTestDataItems(testDataItems);

			jTableResponse = new JTableSingleResponse("OK", jsonTestDataItem);
		}
		catch (Exception e) {
			jTableResponse = new JTableSingleResponse("ERROR", "Unable to update the testDataitems!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	@RequestMapping(value={"testDataItemValues.list"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse listTestDataItemValuesBytestDataItemId(HttpServletRequest request, @RequestParam Integer testDataItemId,@RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		JTableResponse jTableResponse = null;
		log.debug((Object)"inside testDataItemValues.list");
		UserList user = (UserList)request.getSession().getAttribute("USER");
		try {
			List<TestDataItemValues> testDataItemValuesList = new ArrayList<TestDataItemValues>();
			testDataItemValuesList = testCaseScriptGenerationService.listTestDataItemValuesByTestDataItemId(testDataItemId,jtStartIndex,jtPageSize);

			ArrayList<JsonTestDataItems> jsonTestDataItemsList = new ArrayList<JsonTestDataItems>();
			for (TestDataItemValues testDataItemValues : testDataItemValuesList) {
				jsonTestDataItemsList.add(new JsonTestDataItems(testDataItemValues));
			}
			jTableResponse = new JTableResponse("OK", jsonTestDataItemsList,testCaseScriptGenerationService.totalTestDataItemValuesByTestDataItemId(testDataItemId));
		}

		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Unable to show testDataitemvalues!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	@RequestMapping(value={"testDataItemValues.save.and.update"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableSingleResponse addTestDataItemValues(HttpServletRequest req,@ModelAttribute JsonTestDataItems jsonTestDataItems, BindingResult result) {
		JTableSingleResponse jTableResponse = null;
		TestDataItemValues testDataItemvaluesFromUI = null;
		Integer testDataItemValuesId = null;
		if (result.hasErrors()) {
			jTableResponse = new JTableSingleResponse("ERROR", "Invalid Form!");
		}
		try {

			UserList user = (UserList)req.getSession().getAttribute("USER");

			testDataItemvaluesFromUI = jsonTestDataItems.getTestDataItemValues();

			if(testDataItemvaluesFromUI.getTestDataValueId() == null){
				TestDataItemValues testDataItemvaluesFromDB = testCaseScriptGenerationService. getTestDataItemValuesByProductAndTestItemValName(jsonTestDataItems.getProductId(),null,testDataItemvaluesFromUI.getValues(),testDataItemvaluesFromUI.getTestDataItems().getTestDataItemId(),testDataItemvaluesFromUI.getTestDataPlan().getTestDataPlanId());
				if(testDataItemvaluesFromDB == null){
					testDataItemValuesId = testCaseScriptGenerationService.addTestDataItemValues(testDataItemvaluesFromUI);
				}else{
					testDataItemvaluesFromUI.setTestDataValueId(testDataItemvaluesFromDB.getTestDataValueId());
					testCaseScriptGenerationService.updateTestDataItemValues(testDataItemvaluesFromUI);
				}

			}else{
				testCaseScriptGenerationService.updateTestDataItemValues(testDataItemvaluesFromUI);
			}


			TestDataItemValues  testDataItemValues = new TestDataItemValues();
			testDataItemValues = testDataItemvaluesFromUI;

			if(testDataItemValuesId != null){
				testDataItemValues.setTestDataValueId(testDataItemValuesId);
			}

			JsonTestDataItems jsonTestDataItem =  new JsonTestDataItems(testDataItemValues);

			jTableResponse = new JTableSingleResponse("OK", jsonTestDataItem);
		}
		catch (Exception e) {
			jTableResponse = new JTableSingleResponse("ERROR", "Unable to update the testDataitem values!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	@RequestMapping(value={"uiObjects.list"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse listUIObjectsByProductId(HttpServletRequest request, @RequestParam Integer productId,@RequestParam Integer objRepoFilterId,@RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		JTableResponse jTableResponse = null;
		log.debug((Object)"inside amdocs.pageobject.list");
		UserList user = (UserList)request.getSession().getAttribute("USER");
		try {
			List<UIObjectItems> uIObjectItemsList = new ArrayList<UIObjectItems>();
			uIObjectItemsList = testCaseScriptGenerationService.listUIObjectItemsByProductId(productId,objRepoFilterId,user.getUserId(),jtStartIndex,jtPageSize);
			int count =  testCaseScriptGenerationService.totalUIObjectItemsByProductId(productId,objRepoFilterId,user.getUserId());
			ArrayList<JsonUiObjectItems> jsonUIObjectItemsList = new ArrayList<JsonUiObjectItems>();
			for (UIObjectItems uIObjectItems : uIObjectItemsList) {
				jsonUIObjectItemsList.add(new JsonUiObjectItems(uIObjectItems));
			}
			jTableResponse = new JTableResponse("OK", jsonUIObjectItemsList,count);
		}

		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Unable to show Attachment!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	@RequestMapping(value={"uiObjects.save.and.update"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableSingleResponse addUIObjects(HttpServletRequest req,@ModelAttribute JsonUiObjectItems jsonUiObjectItems, BindingResult result) {
		JTableSingleResponse jTableResponse = null;
		UIObjectItems uIObjectItemsFromUI = null;
		Integer uiObjectId = null;
		if (result.hasErrors()) {
			jTableResponse = new JTableSingleResponse("ERROR", "Invalid Form!");
		}
		try {

			UserList user = (UserList)req.getSession().getAttribute("USER");

			uIObjectItemsFromUI = jsonUiObjectItems.getUIObjects();

			if(uIObjectItemsFromUI.getUiObjectItemId() == null){
				UIObjectItems uiObjFromDB = testCaseScriptGenerationService.getUIObjectItemByElementName(uIObjectItemsFromUI.getElementName(),jsonUiObjectItems.getProductId(),null);
				uIObjectItemsFromUI.setCreatedDate(new Date());
				uIObjectItemsFromUI.setUserlist(user);
				if(uiObjFromDB == null){
					uiObjectId = testCaseScriptGenerationService.addUIObjects(uIObjectItemsFromUI);
				}else if(uiObjFromDB.getUserlist().getUserId() == user.getUserId()){
					uIObjectItemsFromUI.setModifiedDate(new Date());
					uIObjectItemsFromUI.setUserlist(user);
					testCaseScriptGenerationService.updateUIObjects(uIObjectItemsFromUI);
				}else{
					jTableResponse = new JTableSingleResponse("ERROR", uIObjectItemsFromUI.getElementName()+" already had been created by "+uiObjFromDB.getUserlist().getUserId());
					return jTableResponse;
				}

			}else{
				uIObjectItemsFromUI.setModifiedDate(new Date());
				uIObjectItemsFromUI.setUserlist(user);
				testCaseScriptGenerationService.updateUIObjects(uIObjectItemsFromUI);
			}


			UIObjectItems  uIObjectItems = new UIObjectItems();
			uIObjectItems = uIObjectItemsFromUI;

			if(uiObjectId != null){
				uIObjectItems.setUiObjectItemId(uiObjectId);
			}

			JsonUiObjectItems jsonUIObjectItems =  new JsonUiObjectItems(uIObjectItems);

			jTableResponse = new JTableSingleResponse("OK", jsonUIObjectItems);
		}
		catch (Exception e) {
			jTableResponse = new JTableSingleResponse("ERROR", "Unable to update the Ui Object!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	@RequestMapping(value={"product.testcase.automationscript.save"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableSingleResponse saveTestCaseAutomationScriptContent(HttpServletRequest request, @RequestParam Map<String, String> mapData) {
		JTableSingleResponse jTableSingleResponse = null;
		try {
			Integer testCaseAutomationScriptId = Integer.parseInt(mapData.get("testCaseAutomationScriptId"));
			Integer testCaseAutomationStoryVersionId = null;

			log.info(" Version Id :"+mapData.get("versionId") +" Test Case Id : " +mapData.get("testCaseId"));
			if(mapData.get("versionId") != null && !mapData.get("versionId").isEmpty()){
				testCaseAutomationStoryVersionId = Integer.parseInt(mapData.get("versionId"));
			}else{
				log.error("Version id has not been set to header ");
			}
			Integer testCaseId = Integer.parseInt(mapData.get("testCaseId"));
			String tcName = null;
			if(testCaseId !=  null) 
				tcName = testCaseService.getTestCaseById(testCaseId).getTestCaseName()+".story";

			String selectedConfigFile = mapData.get("selectedConfigFile");

			if (testCaseAutomationScriptId == null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to save script as Id is not available");
				return jTableSingleResponse;
			}
			String automationScript = mapData.get("script");
			String scriptType = mapData.get("scriptType");
			String testEngine = mapData.get("testEngine");
			byte[] data = automationScript.getBytes();
			String message = this.testCaseScriptGenerationService.saveTestCaseAutomationStory(testCaseId,testCaseAutomationStoryVersionId, automationScript, selectedConfigFile,testEngine);
			/*//Comitting the story file to SVN
			String SVNResponse = null;
			if(message != null && !message.isEmpty()){				
				String scriptTOSVN = new String(automationScript);
				ConnectorCredentials connectorCredentials = new ConnectorCredentials();
				TestCaseList tc = testCaseService.getTestCaseById(testCaseId);
				if(tc != null){
					if(tc.getProductMaster() != null){
						int productId = tc.getProductMaster().getProductId();
						List<SCMSystem> scmManagementSystems=defectManagementService.listSCMManagementSystem(productId);
						if(scmManagementSystems != null && !scmManagementSystems.isEmpty()){
							SCMSystem scmSystem = scmManagementSystems.get(0);
							String pwd  = passwordEncryptionService.decrypt(scmSystem.getConnectionPassword().trim());
							SVNResponse = connectorCredentials.commitNewFileToSVN(scmSystem.getConnectionProperty1(), scmSystem.getConnectionUserName(), pwd, tcName, scriptTOSVN.replaceAll("(\\\\n\\\\r|\\\\n|\\\\r)", System.lineSeparator()), null);
						}
					}
				}
			}*/
			jTableSingleResponse = message.startsWith("SUCCESS") ? new JTableSingleResponse("OK", "Story Saved Successfully") : new JTableSingleResponse("ERROR", message);
			return jTableSingleResponse;
			/*if(SVNResponse == null){
				jTableSingleResponse = message.startsWith("SUCCESS") ? new JTableSingleResponse("OK", "Story Saved Successfully") : new JTableSingleResponse("ERROR", message);
				return jTableSingleResponse;
			}else{
				if(message.startsWith("SUCCESS") && SVNResponse != null && !SVNResponse.equalsIgnoreCase("Success"))
					return new JTableSingleResponse("OK", SVNResponse+"!. But Story Saved Successfully");
				else if(message.startsWith("SUCCESS") && SVNResponse != null && SVNResponse.equalsIgnoreCase("Success"))
					return new JTableSingleResponse("OK", "Story Saved Successfully");
				else if(message.startsWith("SUCCESS") && SVNResponse != null && !SVNResponse.equalsIgnoreCase("Success"))
					return new JTableSingleResponse("ERROR", message);
			}*/


		}
		catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error Saving Story!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableSingleResponse;
	}
	@RequestMapping(value={"product.testcase.scriptsource"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse generateScriptSource(HttpServletRequest request, HttpServletResponse response, @RequestParam String ids, @RequestParam String type, @RequestParam Integer versionId,  @RequestParam Map<String, String> mapData) {
		JTableResponse jTableResponse = null;
		String storyData = null;
		String scriptData = null;
		String destinationDirectory = "";
		String fileName = "";
		String path = "";
		String generateScriptPath = "";
		String mainFilePath = "";
		String identityFilePath = "";
		long timestamp = DateUtility.dateToLong((Date)new Date());
		FileInputStream inputStream = null;
		String testCaseName = "";
		String scriptsFor = mapData.get("scriptsFor");
		String temp[] = ids.split(",");
		Integer [] idList = new Integer[temp.length];
		for(int i = 0; i<temp.length; i++){
			idList[i] = Integer.parseInt(temp[i]);
		}
		int length = idList.length;
		// String scriptsFor = mapData.get("scriptsFor");
		String scriptType = mapData.get("scriptType");
		String testEngine = mapData.get("testEngine");
		//testEngine = "SELENIUM";
		String scriptLanguage = mapData.get("scriptLanguage");
		String codeGenerationMode = mapData.get("codeGenerationMode");
		String name = mapData.get("name");
		String TEST_METHOD_SINGLE = mapData.get("testMethodSingle");
		String TEST_CLASS_SINGLE = mapData.get("testClasssingle");
		Integer productId = Integer.parseInt(mapData.get("productId"));
		String packageName = mapData.get("packageName");
		ArrayList<JsonTestCaseScript> jsonScripts = new ArrayList<JsonTestCaseScript>();
		boolean flag = false;
		JsonTestCaseScript jsonTestCaseScript = new JsonTestCaseScript();
		UserList user = (UserList)request.getSession().getAttribute("USER");
		List<AttachmentDTO> attachmentDTOList = null;
		String objectRepPath ="";
		String testdataPath = "";
		List<TestDataItemValues> testDataItemValuesList = new ArrayList<TestDataItemValues>();
		List<UIObjectItems> uiObjectItemList= new ArrayList<UIObjectItems>();
		String exportLocation = System.getProperty("catalina.home")+File.separator+"";
		Integer testStoryId = Integer.valueOf(mapData.get("storyId"));
		Integer testCaseId = null;
		try {
			String zipFilePath;
			Attachment objcetRepositoryAttachment;
			Path FROM;
			Attachment testDataAttachmnts;
			if (idList == null || idList.length == 0) {
				jTableResponse = new JTableResponse("ERROR", "Unable to download GHERKIN scripts as Id is not available");
				return jTableResponse;
			}
			if (!scriptType.equals("GHERKIN")) {
				jTableResponse = new JTableResponse("ERROR", "Only GHERKIN Stories are supported for download currently");
				return jTableResponse;
			}
			String catalinaHome = System.getProperty("catalina.home");
			if(!testEngine.equalsIgnoreCase("EDAT") && !new File(libFolderpath).exists()){
				return new JTableResponse("ERROR", "Please check ATSG Lib Path is mapped correctly in properties file..!");        	  
			}

			if (scriptsFor.equalsIgnoreCase("TestCase")) {

				generateScriptPath = String.valueOf(catalinaHome) + File.separator + this.generatedTestScripts + File.separator + "TestCase" + File.separator + timestamp;
				if(length > 1){
					destinationDirectory = String.valueOf(generateScriptPath) + File.separator + idList[0];
				}else{

					destinationDirectory = String.valueOf(generateScriptPath) + File.separator + idList[0];
				}

			} 
			log.info((Object)("Destination directory>>>>" + destinationDirectory));
			for(int i=0; i<idList.length; i++){
				fileName = this.testCaseScriptGenerationService.generateBDDTestCaseAutomationScriptFiles(idList[i], scriptsFor, scriptType, versionId, testEngine, destinationDirectory);
			}
			if (fileName.startsWith("Failed")) {
				log.info((Object)("Unable to generate test scripts : " + fileName));
				jTableResponse = new JTableResponse("SUCCESS", fileName);
				return jTableResponse;
			} 
			try {
				List<String> stepFiles = new ArrayList<String>();
				List<String> hotKeywordStepFiles = new ArrayList<String>();
				List<String> stepFilesForTestEngines = new ArrayList<String>();
				stepFilesForTestEngines = testCaseScriptGenerationService.addTestStepFilesByModeAndTestEngine(codeGenerationMode,testEngine,scriptLanguage);
				hotKeywordStepFiles = testCaseScriptGenerationService.getKeywordLibrariesbyName(testEngine);
				stepFiles.addAll(stepFilesForTestEngines);
				stepFiles.addAll(hotKeywordStepFiles);
				log.info("Class defined for the Script type : "+ scriptType + " ; Test Engine : "+testEngine+ " is "+stepFilesForTestEngines.get(0));
				int count = 0;
				/*  attachmentDTOList = attachmentsService.listTestDataAttachmentDTO(projectId, user.getUserId(), "-1",-1,-1);*/
				attachmentDTOList = attachmentsService.listTestDataAttachmentDTO(productId, -1, "-1",-1,-1);
				if(attachmentDTOList.size()>0){
					for(AttachmentDTO attachments : attachmentDTOList){
						if(attachments.getAttachmentType().equalsIgnoreCase(AOTCConstants.TDTYPE_OBJ_REPOSITORY)){
							if(attachments.getAttributeFileURI() != null && attachments.getAttributeFileURI() != ""){
								if(count > 0){
									objectRepPath = objectRepPath + ",";
								}
								objectRepPath = objectRepPath + attachments.getAttributeFileURI().replace("\\", "\\\\");
								count++;
							}
						}
					}
				}
				
				log.info("Language :"+scriptLanguage + " testEngine: "+testEngine);
				if (length == 1 ) {
					TestScriptFileGenerator testScriptFileGenerator = new TestScriptFileGenerator(
							codeGenerationMode, "CHROME", libFolderpath,
							stepFiles, TEST_METHOD_SINGLE, TEST_CLASS_SINGLE,
							objectRepPath, packageName, user.getLoginId(),
							user.getUserId(), productId, dbUserName,
							dbPassword, databaseUrl, databaseDriver, idList[0]);

					testScriptFileGenerator.readAllBDDStories(
							generateScriptPath, idList[0], generateScriptPath,
							scriptLanguage, testEngine);
				}else{
					TestScriptFileGenerator testScriptFileGenerator = new TestScriptFileGenerator(
							codeGenerationMode, "CHROME", libFolderpath,
							stepFiles, TEST_METHOD_SINGLE, TEST_CLASS_SINGLE,
							objectRepPath, packageName, user.getLoginId(),
							user.getUserId(), productId, dbUserName,
							dbPassword, databaseUrl, databaseDriver, idList[0]);

					testScriptFileGenerator.readAllBDDStories(
							generateScriptPath, idList[0], generateScriptPath,
							scriptLanguage, testEngine);
				}
			}
			catch (Exception e) {
				log.error("script file error "+e);
			} finally{
			}
			if (scriptsFor.equalsIgnoreCase("TestCase")) {

				if("SingleTestCase".equals(type)){ 
					jsonTestCaseScript.setTestScriptType("TestCase");
					testCaseId  =	idList[0];
					TestCaseList tc = testCaseService.getTestCaseById(testCaseId);
					name = tc.getTestCaseName();
					testCaseName = ScriptGeneratorUtilities.getTestCaseClassName((String)name, (int)0, (String)"", (int)0);
					if(!packageName.isEmpty()){
						packageName = packageName.replaceAll("\\.","\\\\");
					}
					log.info("testEngine : "+testEngine);
					if(testEngine.equalsIgnoreCase("EDAT")){
						if(scriptLanguage.equalsIgnoreCase("PYTHON")){
							path = String.valueOf(destinationDirectory) + File.separator + "src" + File.separator + testCaseName + ".py";
							log.info("Generated File extension : " + path);
						} else if(scriptLanguage.equalsIgnoreCase("POWERSHELL")){
							path = String.valueOf(destinationDirectory) + File.separator + "src" + File.separator + testCaseName + ".ps1";
							log.info("Generated File extension : " + path);
						}	    							  
					} else if(testEngine.equalsIgnoreCase("protractor")){
						path = String.valueOf(destinationDirectory) + File.separator + "src" + File.separator + testCaseName + ".js";                           
					}else if(testEngine.equalsIgnoreCase("codedui")){
						path = String.valueOf(destinationDirectory) + File.separator + "src" + File.separator + testCaseName + ".cs";                           
					}else if(testEngine.equalsIgnoreCase("testcomplete")){
						path = String.valueOf(destinationDirectory) + File.separator + "src" + File.separator + testCaseName + ".sj";                           
					}
					else if(testEngine.equalsIgnoreCase("custom_cisco")){
						path = String.valueOf(destinationDirectory) + File.separator + "src" + File.separator + testCaseName + ".py";
						log.info("Generated File extension for custom CISCO : " + path);                           
					}
					else {
						path = String.valueOf(destinationDirectory) + File.separator + "src" + File.separator + packageName +File.separator+ testCaseName + ".java";
						mainFilePath = String.valueOf(destinationDirectory) + File.separator + "src" + File.separator + "com" + File.separator + "hcl" + File.separator + "ers" + File.separator + "atsg" + File.separator + "script" + File.separator + "output" + File.separator + "Main" + ".java";
					}
					FileReader scriptFile = null;
					int lines = 0;
					BufferedReader br = null;
					log.info("path : "+path);
					try {
						scriptFile = new FileReader(path);
						ScriptGenerationDetails scriptDetails = new ScriptGenerationDetails();
						TestCaseList tclist = new TestCaseList();
						tclist.setTestCaseId(testCaseId);
						ProductMaster productMaster = new ProductMaster();
						productMaster.setProductId(productId);
						scriptDetails.setProductMaster(productMaster);
						scriptDetails.setUser(user);
						scriptDetails.setTestCase(tclist);
						scriptDetails.setScriptGeneratedDate(new Date());
						testCaseScriptGenerationService.addScriptGenerationDetails(scriptDetails);
						inputStream = new FileInputStream(path);
						scriptData = IOUtils.toString((InputStream)inputStream);

					} finally {
						if (br != null) {
							br.close();
						}
						if(scriptFile != null){
							scriptFile.close();     
						}

						if(inputStream != null){
							inputStream.close();    
						}

					}
					jsonTestCaseScript.setScriptFileCode(scriptData);
					//saving the generated script 
					if(scriptData != null && !scriptData.isEmpty()){

						JTableSingleResponse jTableSingleResponse = null;
						
							TestStoryGeneratedScripts generatedScript;
							
							generatedScript = this.testCaseScriptGenerationService.saveGeneratedTestScript(testCaseId,testStoryId , scriptData, testEngine, scriptLanguage, user.getUserId(), destinationDirectory , codeGenerationMode);
							if(generatedScript != null){
								log.info("Script saved successfully...!");
							}
					}
					storyData = "";
					jsonTestCaseScript.setMainFileCode(storyData);
					storyData = "";
					jsonTestCaseScript.setIdentityFileCode(storyData);
				}

			}
			File storyZipFile = new File(String.valueOf(destinationDirectory) + ".zip");
			FileUtils.forceDelete((File)storyZipFile);
			if(!testEngine.equalsIgnoreCase("EDAT")){

			} else {
				if (length == 1 && testEngine.equalsIgnoreCase("EDAT")){
					Path TO = Paths.get(destinationDirectory, new String[0]);
					String  eDATConfigFile = exportLocation+"webapps"+File.separator+"TestScripts"+File.separator+ "EDAT"+File.separator+user.getLoginId()+File.separator+productId;
					eDATConfigFile = eDATConfigFile + File.separator + eDatConfigFileName+".xml";   
					if(new File(eDATConfigFile).exists()){
						FROM = Paths.get(eDATConfigFile, new String[0]);
						Files.copy(FROM, TO.resolve(FROM.getFileName()), StandardCopyOption.REPLACE_EXISTING);
						log.debug((Object)"Copied eDAT Configuration File to bundle");
					} else {
						log.debug("eDAT Configuration File not exists to copy");
					}
				}
			}

			if (ZipTool.isValidZipArchive((String)(zipFilePath = ZipTool.dozip((String)destinationDirectory)))) {
				log.info((Object)("Zip file created" + zipFilePath));
			}
			jsonTestCaseScript.setDownloadPath(destinationDirectory);
			jsonScripts.add(jsonTestCaseScript);
			jTableResponse = new JTableResponse("OK", jsonScripts, 1);
			return jTableResponse;                            
		}
		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Please check story is valid!");
			log.error((Object)"JSON ERROR", (Throwable)e);
			return jTableResponse;
		}
	}
	@RequestMapping(value="uiobject.export", method=RequestMethod.POST ,produces="application/json")
	public  @ResponseBody JTableResponse uiOjectItemsExport(@RequestParam Integer productId,@RequestParam String type,HttpServletRequest request,HttpServletResponse response) {
		log.debug("test.export");
		JTableResponse jTableResponse;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
			Date currentDate = new Date(System.currentTimeMillis());
			String timeStamp = sdf.format(currentDate);
			//String exportLocation = request.getServletContext().getRealPath("/");
			String exportLocation=System.getProperty("catalina.home")+File.separator+"";
			/*String exportLocation="D:"+File.separator;*/
			UserList user = (UserList)request.getSession().getAttribute("USER");
			List<UIObjectItems> uiObjectItemList= new ArrayList<UIObjectItems>();
			String zipFilePath;
			String fileExportlocation = exportLocation+"webapps"+File.separator+"EXPORT"+File.separator+ "UIOBJECTS"+File.separator+user.getLoginId()+File.separator+productId;
			boolean isexportComplete = false;
			if(type.trim().equalsIgnoreCase("handle")){
				long timestampLong = DateUtility.dateToLong((Date)new Date());
				fileExportlocation =  fileExportlocation+File.separator+timestampLong+File.separator+"UiObject"+"_"+timeStamp;
				List<String> handleList = testCaseScriptGenerationService.listUIObjectItemHandleNamesByProductId(productId, user.getUserId());
				for(String handle : handleList){
					String fileName = handle +".xlsx" ;
					uiObjectItemList =	testCaseScriptGenerationService.listUIObjectItemsByHandleName(productId, user.getUserId(),handle);
					isexportComplete = testDataIntegrator.uiObjectsExport(uiObjectItemList, fileExportlocation,fileName);
					//fileExportlocation  = fileExportlocation	+".zip";

				}
				if (ZipTool.isValidZipArchive((String)(zipFilePath = ZipTool.dozip((String)fileExportlocation)))) {
					log.info((Object)("Zip file created" + zipFilePath));
					fileExportlocation = zipFilePath;
				}
			}else{
				String fileName ="UiObject"+"_"+timeStamp+".xlsx";
				log.info("fileName:"+fileName);
				uiObjectItemList = testCaseScriptGenerationService.listUIObjectItemsByProductId(productId,0,user.getUserId(),null,null);
				isexportComplete = testDataIntegrator.uiObjectsExport(uiObjectItemList, fileExportlocation,fileName);
				fileExportlocation =  fileExportlocation+File.separator+fileName;
			}		


			if(isexportComplete){
				log.info("Export uiobjects Completed.");
				jTableResponse = new JTableResponse("OK","Export uiobjects Completed.",fileExportlocation);
			} else{

				jTableResponse = new JTableResponse("ERROR","Export Not completed");
			}

		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("ERROR","Export completed");
		}
		return jTableResponse;
	}

	@RequestMapping(value="testData.export", method=RequestMethod.POST ,produces="application/json")
	public  @ResponseBody JTableResponse  testDataItemsExport(@RequestParam Integer productId,@RequestParam String type,@RequestParam Integer testDataPlanId,HttpServletRequest request,HttpServletResponse response) {
		log.debug("test.export");
		JTableResponse jTableResponse;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
			Date currentDate = new Date(System.currentTimeMillis());
			String timeStamp = sdf.format(currentDate);
			//String exportLocation = request.getServletContext().getRealPath("/");
			String exportLocation=System.getProperty("catalina.home")+File.separator+"";
			/*String exportLocation="D:"+File.separator;*/
			UserList user = (UserList)request.getSession().getAttribute("USER");
			int testDataItemHeaderCount;
			List<TestDataItemValues> testDataItemValuesList = new ArrayList<TestDataItemValues>();
			List<TestDataItems> testDataItemList = new ArrayList<TestDataItems>();
			String zipFilePath;
			String fileExportlocation = exportLocation+"webapps"+File.separator+"EXPORT"+File.separator+ "TESTDATA"+File.separator+user.getLoginId()+File.separator+productId;
			boolean isexportComplete = false;
			if(type.trim().equalsIgnoreCase("handle")){
				long timestampLong = DateUtility.dateToLong((Date)new Date());
				fileExportlocation =  fileExportlocation+File.separator+timestampLong+File.separator+"TestData"+"_"+timeStamp;
				List<String> handleList =  testCaseScriptGenerationService.listTesDataItemHandleNamesByProductId(productId, user.getUserId());
				testDataItemHeaderCount  =  testCaseScriptGenerationService.listTestDataItemValuesCountByProductIdAndUserId(productId,user.getUserId());
				for(String handle : handleList){
					String fileName = handle +".xlsx" ;
					testDataItemList =	testCaseScriptGenerationService.listTestDataItemValuesByProductAndTestDataPlanAndHandle(productId,handle,testDataPlanId, user.getUserId());
					isexportComplete = testDataIntegrator.testDataExport(testDataItemList, fileExportlocation,fileName,testDataItemHeaderCount);
					//fileExportlocation  = fileExportlocation	+".zip";

				}
				if (ZipTool.isValidZipArchive((String)(zipFilePath = ZipTool.dozip((String)fileExportlocation)))) {
					log.info((Object)("Zip file created" + zipFilePath));
					fileExportlocation = zipFilePath;
				}
			}else{
				String fileName = "TestData"+"_"+timeStamp+".xlsx";
				log.info("fileName:"+fileName);
				testDataItemHeaderCount  =  testCaseScriptGenerationService.listTestDataItemValuesCountByProductIdAndUserId(productId,user.getUserId());
				testDataItemList =	testCaseScriptGenerationService.listTestDataItemValuesByProductAndTestDataPlanAndHandle(productId,null,testDataPlanId, user.getUserId());
				isexportComplete = testDataIntegrator.testDataExport(testDataItemList, fileExportlocation,fileName,testDataItemHeaderCount);
				fileExportlocation =  fileExportlocation+File.separator+fileName;
			}		

			if(isexportComplete){
				log.info("Export Completed.");
				jTableResponse = new JTableResponse("OK","Export Completed.",fileExportlocation);
			} else{

				jTableResponse = new JTableResponse("ERROR","Export Not completed");
			}

		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("ERROR","Export completed");
		}
		return jTableResponse;
	}
	@RequestMapping(value={"gettestengines.list"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse listTestEngines() {
		JTableResponse jTableResponseOptions = null;
		List<Languages> languagesList = new ArrayList<Languages>();
		List<JsonTestEngineLanguageMode> jsonLanguagesList = new ArrayList<JsonTestEngineLanguageMode>();
		try {
			languagesList = languagesDAO.listLanguages(1);
			Set<Languages> lang = new HashSet<>(languagesList);
			for(Languages language : lang){
				JsonTestEngineLanguageMode jsonTestEngineLanguageMode = new JsonTestEngineLanguageMode();
				jsonTestEngineLanguageMode = jsonTestEngineLanguageMode.getJsonTestEngineLanguageMode(language);
				jsonLanguagesList.add(jsonTestEngineLanguageMode);
			}		   
			jTableResponseOptions = new JTableResponse("OK",jsonLanguagesList,jsonLanguagesList.size());
		}catch (Exception e) {
			jTableResponseOptions = new JTableResponse("ERROR", "Unable to show tags!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponseOptions;
	}	
	@RequestMapping(value={"language.testengine.update"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableSingleResponse updateTestEngineLanguageMode(HttpServletRequest request, @ModelAttribute JsonTestEngineLanguageMode jsonTestEngineLanguageMode, BindingResult result) {
		JTableSingleResponse jTableResponse = null;
		if (result.hasErrors()) {
			jTableResponse = new JTableSingleResponse("ERROR", "Invalid Form!");
		}		
		try {
			Integer modeId ;
			String modifiedField =  jsonTestEngineLanguageMode.getModifiedFieldTitle();
			Integer modifiedFieldVal = Integer.parseInt(jsonTestEngineLanguageMode.getModifiedFieldValue());

			Languages lang = new Languages();
			String temp[] =  modifiedField.split("~");

			String mode = temp[0].toUpperCase();
			String testEngine=temp[1].toUpperCase();

			if(temp[0].equalsIgnoreCase("ScriptGeneration")){
				mode = "Generic";
			}else if(temp[0].equalsIgnoreCase("ScriptGenTAF")){
				mode = "TAF-MODE";
			}else if(temp[0].equalsIgnoreCase("TestExecution")){
				mode = "Test Execution";
			}else if(temp[0].equalsIgnoreCase("Scriptless")){
				mode = "SCRIPTLESS";
			}
			TestEngineLanguageMode testEngineLanguageMode = new TestEngineLanguageMode();
			Languages language =   languagesDAO.getLanguageByName(jsonTestEngineLanguageMode.getLanguageName());
			TestToolMaster testToolMaster = testToolMasterDAO.getTestToolIdByName(testEngine);
			ExecutionMode executionMode = executionModeDAO.getModeIdByName(mode);
			//If Modified field value is 1 then add a new record
			if(modifiedFieldVal == 1){
				testEngineLanguageMode.setTestTool(testToolMaster);
				testEngineLanguageMode.setLanguage(language);
				testEngineLanguageMode.setExecutionMode(executionMode);
				languagesDAO.updateTestEngineLanguageMode(testEngineLanguageMode);
			}else{
				//else delete that record
				testEngineLanguageMode = testEngineLanguageModeDAO.getTestEngineLanguageModeId(testToolMaster.getTestToolId(),language.getId(),executionMode.getModeId());
				testEngineLanguageModeDAO.delete(testEngineLanguageMode);
			}
			language =   languagesDAO.getLanguageByName(jsonTestEngineLanguageMode.getLanguageName());
			JsonTestEngineLanguageMode jsonTestEngineLanguage = new JsonTestEngineLanguageMode();
			jsonTestEngineLanguage = jsonTestEngineLanguage.getJsonTestEngineLanguageMode(language);
			jTableResponse = new JTableSingleResponse("OK",jsonTestEngineLanguage);
		}catch (Exception e) {
			jTableResponse = new JTableSingleResponse("ERROR", "Invalid Form!");
			log.error((Object)"TestEngineLanguageMode Update failed", (Throwable)e);
		}
		return jTableResponse;
	}

	@RequestMapping(value={"productId.testobjectdata.attachments.list"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse listVersionTestObjectDataAttachments(HttpServletRequest request, @RequestParam Integer productId,@RequestParam int jtStartIndex,@RequestParam int jtPageSize) {
		JTableResponse jTableResponse;
		log.debug((Object)"inside product.version.testobjectdata.attachments.list");
		try {
			UserList user = (UserList)request.getSession().getAttribute("USER");
			int userId = user.getUserId();
			List<AttachmentDTO> attachmentDTOList = attachmentsService.listTestDataAttachmentDTO(productId, userId, "EDAT",jtStartIndex,jtPageSize);
			Integer total =   attachmentsService. totalTestDataAttachmentByProductIdAndUserId( productId,  userId,  "EDAT");
			ArrayList<JsonAttachment> jsonAttachmentList = new ArrayList<JsonAttachment>();
			for (AttachmentDTO attachmentDTO : attachmentDTOList) {
				jsonAttachmentList.add(new JsonAttachment(attachmentDTO));
			}
			jTableResponse = new JTableResponse("OK", jsonAttachmentList, total);
			attachmentDTOList = null;
		}
		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Unable to show Attachment!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	@RequestMapping(value={"edatconfig.list.option"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponseOptions listEdatConfigFiles(HttpServletRequest request, @RequestParam Integer productId, @RequestParam Integer testCaseId) {
		JTableResponseOptions jTableResponseOptions = null;
		UserList user = (UserList)request.getSession().getAttribute("USER");
		try {
			List<JsonAttachment> jsonAttachmentList = new ArrayList<JsonAttachment>();
			List<Attachment> edatConfigFileNamesList = new ArrayList<Attachment>();
			edatConfigFileNamesList = testCaseScriptGenerationService.listDeviceObjectsEDAT(productId);
			if(edatConfigFileNamesList != null && !edatConfigFileNamesList.isEmpty()&& edatConfigFileNamesList.size()>0){
				for(Attachment attach : edatConfigFileNamesList){
					jsonAttachmentList.add(new JsonAttachment(attach));
				}
				jTableResponseOptions = new JTableResponseOptions("OK",jsonAttachmentList, true);    		   
			}else{
				jTableResponseOptions = new JTableResponseOptions("ERROR", "No Data");
			}

		}catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR", "Unable to show tags!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponseOptions;
	}
	@RequestMapping(value={"load.configfile.option"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse loadEdatConfigFileContent(HttpSession session,@RequestParam Integer testCaseId, @RequestParam Integer productId ,@RequestParam String xmlFileName) {

		List<String> jsonTestCaseScriptAttachmentFileContent = null;
		eDatConfigFileName="";
		try {
			if (xmlFileName == null) {
				JTableResponse jTableResponse = new JTableResponse("ERROR", "File is not available !");
				return jTableResponse;
			}
			eDatConfigFileName = xmlFileName;
			jsonTestCaseScriptAttachmentFileContent = getEDATObjectItems(session, productId,xmlFileName);		
			if (jsonTestCaseScriptAttachmentFileContent == null) {
				JTableResponse jTableResponse = new JTableResponse("SUCCESS", "File is not available ! ");
				return jTableResponse;
			}
			JTableResponse jTableResponse = new JTableResponse("OK", jsonTestCaseScriptAttachmentFileContent, jsonTestCaseScriptAttachmentFileContent.size());
			return jTableResponse;
		}
		catch (Exception e) {
			JTableResponse jTableResponse = new JTableResponse("ERROR", "File is not available !");
			log.error((Object)"JSON ERROR", (Throwable)e);
			return jTableResponse;
		}

	}
	
	@RequestMapping(value={"product.testcase.automationscript.svn.checkin"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableSingleResponse checkInStoryToSVN(HttpServletRequest request, @RequestParam Map<String, String> mapData) {
		JTableSingleResponse jTableSingleResponse = null;
		String messageFromRepo = "";
		boolean isTestData = false;
		boolean isObjectRepo = false;
		try {
			
			if (mapData.get("testCaseAutomationScriptId") == null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to save story as story id is not available");
				return jTableSingleResponse;
			}
			if (mapData.get("testCaseId") == null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to save story as testcase id is not available");
				return jTableSingleResponse;
			}
			Integer testCaseAutomationScriptId = Integer.parseInt(mapData.get("testCaseAutomationScriptId"));
			Integer testCaseId = Integer.parseInt(mapData.get("testCaseId"));
			String commitComment = mapData.get("comments");
			if(mapData.get("isObjectRepository") != null)
				isObjectRepo = Boolean.valueOf(mapData.get("isObjectRepository"));
			if(mapData.get("isTestdata") != null)
				isTestData = Boolean.valueOf(mapData.get("isTestdata"));
			log.info("Inside checkInStoryToSVNd Test Case Id : "+ testCaseId+" Story Id : "+testCaseAutomationScriptId);
			TestCaseAutomationStory story = testCaseAutomationScriptDAO.getByTestCaseStoryByAutomationStoryId(testCaseAutomationScriptId);
			String automationScript = story.getScript();
			String tcName = null;
			if(testCaseId !=  null) 
				tcName = testCaseService.getTestCaseById(testCaseId).getTestCaseName()+".story";

			String message = this.testCaseScriptGenerationService.checkInStoryToSVN(testCaseId,tcName, automationScript,commitComment);
			if(isObjectRepo || isTestData){
				messageFromRepo = this.testCaseScriptGenerationService.checkInRepositoryToSVN(story.getTestCaseAutomationStoryId(),commitComment,isObjectRepo,isTestData);
				log.info(messageFromRepo);
				if(message.contains("Success") && messageFromRepo.contains("Success"))
					message = "Success : Story,Testdata and ObjectRepository successfully committed into GIT.";
				else
					message = message + "<br>" + messageFromRepo;
			}
			
			jTableSingleResponse = message.startsWith("Success") ? new JTableSingleResponse("OK", message) : new JTableSingleResponse("ERROR", message);
			return jTableSingleResponse;
		}
		catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error Checking in Story to SVN!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value={"generatedscript.svn.checkin"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableSingleResponse checkInScriptToSVN(HttpServletRequest request,@RequestParam Map<String, String> mapData) {
		JTableSingleResponse jTableSingleResponse = null;
		String SVNResponse = null;
		String messageForRepo = "";
		boolean isTestData = false;
		boolean isObjectRepo = false;
		try {
			if (mapData.get("generatedScriptId") == null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to save script as story id is not available");
				return jTableSingleResponse;
			}
			Integer storyId = Integer.parseInt(mapData.get("generatedScriptId"));
			String commitComment = mapData.get("comments");
			if(mapData.get("isObjectRepository") != null)
				isObjectRepo = Boolean.valueOf(mapData.get("isObjectRepository"));
			if(mapData.get("isTestdata") != null)
				isTestData = Boolean.valueOf(mapData.get("isTestdata"));
			
			if(storyId != null ){
				SVNResponse = this.testCaseScriptGenerationService.checkInScriptToSVN(storyId,commitComment);
				if(isObjectRepo || isTestData){
					messageForRepo = this.testCaseScriptGenerationService.checkInRepositoryToSVN(storyId,commitComment,isObjectRepo,isTestData);
					log.info(messageForRepo);
					if(messageForRepo.contains("Success") && SVNResponse.contains("Success"))
						messageForRepo = "Success : Story,Testdata and ObjectRepository successfully committed into GIT.";
					else
						SVNResponse = SVNResponse + "<br>" +messageForRepo;
				}
			}
			jTableSingleResponse = SVNResponse.startsWith("Success") ? new JTableSingleResponse("OK", SVNResponse) : new JTableSingleResponse("ERROR", SVNResponse);
			return jTableSingleResponse;

		}
		catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error checking in generated script to SVN!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableSingleResponse;
		
	}
	@RequestMapping(value={"generatedscript.download"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse downloadGeneratedScript(HttpServletRequest request, @RequestParam Integer testCaseStoryId, @RequestParam String languageName, @RequestParam String testEngine , @RequestParam String codeGenerationMode) {
		JTableResponse jTableResponse = null;
		JsonTestStoryGeneratedScripts jsonTestStoryGeneratedScripts = null;
		String downloadLocationPath = "";
		try {

			if(testCaseStoryId != null){
				jsonTestStoryGeneratedScripts = this.testCaseScriptGenerationService.getAutomationScript(testCaseStoryId,languageName,testEngine);
			}
			
			if(jsonTestStoryGeneratedScripts != null){
				downloadLocationPath = testCaseScriptGenerationService.generateScriptTdandObjForDownload(jsonTestStoryGeneratedScripts);
				jsonTestStoryGeneratedScripts.setDownloadPath(downloadLocationPath);
				jTableResponse = new JTableResponse("OK", jsonTestStoryGeneratedScripts);
			}else{
				jTableResponse = new JTableResponse("ERROR", "Script is not available for this story");
			}
		}
		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Error Listing generated script!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	@RequestMapping(value={"testDataPlan.list"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableResponse listTestDataPlan(HttpServletRequest request, @RequestParam Integer projectId,@RequestParam Integer jtStartIndex,@RequestParam Integer jtPageSize) {

		JTableResponse jTableResponse = null;
		log.debug((Object)"testDataPlan.list");
		UserList user = (UserList)request.getSession().getAttribute("USER");
		try {
			List<TestDataPlan> testDataPlanList = new ArrayList<TestDataPlan>();
			List<JsonTestDataPlan> jsonTestDataPlanList = new ArrayList<JsonTestDataPlan>();
			testDataPlanList = testCaseScriptGenerationService.listTestDataPlan(projectId);

			if(testDataPlanList != null && !testDataPlanList.isEmpty()){
				for(TestDataPlan tdPlan : testDataPlanList){
					jsonTestDataPlanList.add(new JsonTestDataPlan(tdPlan));
				}

				jTableResponse = new JTableResponse("OK",jsonTestDataPlanList,jsonTestDataPlanList.size());
			}else{
				jTableResponse = new JTableResponse("ERROR", "No data available!");
			}


		}catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Unable to show test data plans!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	@RequestMapping(value={"testDataPlan.save.and.update"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableSingleResponse addTestDataPlan(HttpServletRequest req,@ModelAttribute JsonTestDataPlan jsonTestDataPlan, BindingResult result) {
		JTableSingleResponse jTableResponse = null;
		TestDataPlan testDataPlanFromUI = null;
		Integer testDataPlanId = null;
		if (result.hasErrors()) {
			jTableResponse = new JTableSingleResponse("ERROR", "Invalid Form!");
		}
		try {

			UserList user = (UserList)req.getSession().getAttribute("USER");

			testDataPlanFromUI = jsonTestDataPlan.getTestDataPlan();

			if(testDataPlanFromUI.getTestDataPlanId() == null){
				testDataPlanFromUI.setUserlist(user);
				TestDataPlan testDataPlanFromDB = testCaseScriptGenerationService.getTestDataPlanByTestDataPlanName(testDataPlanFromUI.getTestDataPlanName(),testDataPlanFromUI.getProductMaster().getProductId());
				if(testDataPlanFromDB == null){
					testDataPlanId = testCaseScriptGenerationService.addTestDataPlan(testDataPlanFromUI);
				}else{
					testDataPlanFromUI.setTestDataPlanId(testDataPlanFromDB.getTestDataPlanId());
					testCaseScriptGenerationService.updateTestDataPlan(testDataPlanFromUI);
				}

			}else{
				/*testDataItemvaluesFromUI.setUserlist(user);*/
				testCaseScriptGenerationService.updateTestDataPlan(testDataPlanFromUI);
			}


			TestDataPlan  testDataplanObj = new TestDataPlan();
			testDataplanObj = testDataPlanFromUI;

			if(testDataPlanId != null){
				testDataplanObj.setTestDataPlanId(testDataPlanId);
			}

			JsonTestDataPlan jsonTestDataplanObj =  new JsonTestDataPlan(testDataplanObj);

			jTableResponse = new JTableSingleResponse("OK", jsonTestDataplanObj);
		}
		catch (Exception e) {
			jTableResponse = new JTableSingleResponse("ERROR", "Unable to update the test data plan!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="product.testdata.delete",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse deleteTestData(@RequestParam Integer testDataItemId) {
		log.info("Deleting test data item :"+testDataItemId);
		JTableResponse jTableResponse;
		String status = null;
		try {		
			if (testDataItemId == null || ("null").equals(testDataItemId)) {					
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			TestDataItems tesDataItem = testCaseScriptGenerationService.getTestDataItemByItemId(testDataItemId);
			if (tesDataItem != null) {				
				status = testCaseScriptGenerationService.deleteTestData(tesDataItem);
			} else {
				log.info("TestData is not available for the Id :"+testDataItemId);
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			if(status.equalsIgnoreCase("SUCCESS")){		
				jTableResponse = new JTableResponse("OK","Test data Deleted Successfully" );
			}else{
				jTableResponse = new JTableResponse("OK", null,0);
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	@RequestMapping(value="product.objectrepository.delete",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse deleteObjectRepo(@RequestParam Integer uiObjectItemId) {
		log.info("Deleting Object Repository :"+uiObjectItemId);
		JTableResponse jTableResponse;
		String status = null;
		try {		
			if (uiObjectItemId == null || ("null").equals(uiObjectItemId)) {					
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			UIObjectItems uiObjectItem = testCaseScriptGenerationService.getUIObjectItemById(uiObjectItemId);
			if (uiObjectItem != null) {				
				status = testCaseScriptGenerationService.deleteObjectRepo(uiObjectItem);
			} else {
				log.info("Object Repo is not available for the Id :"+uiObjectItemId);
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			if(status.equalsIgnoreCase("SUCCESS")){		
				jTableResponse = new JTableResponse("OK","Object Repository Deleted Successfully" );
			}else{
				jTableResponse = new JTableResponse("OK", null,0);
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	
	
	@RequestMapping(value="keyword.library.delete",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse deleteKeywordLibrary(@RequestParam Integer keywordLibId) {
		log.info("Deleting Object Repository :"+keywordLibId);
		JTableResponse jTableResponse;
		String status = null;
		try {		
			if (keywordLibId == null || ("null").equals(keywordLibId)) {					
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			boolean deleteStatus= testCaseScriptGenerationService.deleteKeywordLibrary(keywordLibId);
			if(deleteStatus){		
				jTableResponse = new JTableResponse("OK","Keyword Library Deleted Successfully" );
			}else{
				jTableResponse = new JTableResponse("OK", null,0);
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error delete keyword library!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	@RequestMapping(value="test.data.items.table.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse ListTestDataItemsAndValues(@RequestParam Integer productId, HttpServletRequest request,HttpServletResponse response) {
		log.debug("test.data.items.list");
		JTableResponse jTableResponse = null;
		try {
			UserList user = (UserList)request.getSession().getAttribute("USER");
			List<TestDataItems> testDataItemList = new ArrayList<TestDataItems>();
			List<JsonTestDataItemsValues> jsonTestDataItemsValuesList = new ArrayList<JsonTestDataItemsValues>();
			testDataItemList =	testCaseScriptGenerationService.listTestDataItemValuesByProductAndTestDataPlanAndHandle(productId,null,-1, user.getUserId());
			if(testDataItemList != null && !testDataItemList.isEmpty()){
				for(TestDataItems testDataItem : testDataItemList){
					JsonTestDataItemsValues jsonTestDataItemsValues =new JsonTestDataItemsValues();
					jsonTestDataItemsValues = jsonTestDataItemsValues.getestDataItemsValues(testDataItem, true);
					jsonTestDataItemsValuesList.add(jsonTestDataItemsValues);
				}
				jTableResponse = new JTableResponse("OK",jsonTestDataItemsValuesList,jsonTestDataItemsValuesList.size());
			}else{
				jTableResponse = new JTableResponse("ERROR", "No data available!");
			}
		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("ERROR","Error in getting test data items");
		}
		return jTableResponse;
	}
	
	@RequestMapping(value={"testDataItemValues.consolidatedView.save.and.update"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableSingleResponse addTestDataItemValuesTableView(HttpServletRequest req,@ModelAttribute JsonTestDataItemsValues jsonTestDataItemsValues, BindingResult result) {
		JTableSingleResponse jTableResponse = null;
		TestDataItemValues testDataItemvaluesFromUI = null;
		TestDataItemValues testDataItemvaluesFromDB = null;
		Integer testDataItemValuesId = null;
		List<TestDataItemValues> testDataItemValuesList = new ArrayList<TestDataItemValues>();
		List<JsonTestDataItemsValues> jsonTestDataItemsValuesList = new ArrayList<JsonTestDataItemsValues>();
		if (result.hasErrors()) {
			jTableResponse = new JTableSingleResponse("ERROR", "Invalid Form!");
		}
		try {

			UserList user = (UserList)req.getSession().getAttribute("USER");
			String modifiedFieldTitle = jsonTestDataItemsValues.getModifiedFieldTitle();
			Integer index = new Integer(modifiedFieldTitle.substring(5, modifiedFieldTitle.length()));
			testDataItemvaluesFromUI = jsonTestDataItemsValues.getTestDataItemValues();
			TestDataPlan testDataPlanFromDB = testCaseAutomationScriptDAO.getTestDataPlanByTestDataPlanName("SampleTDP",jsonTestDataItemsValues.getProductId());
			testDataItemvaluesFromUI.setTestDataPlan(testDataPlanFromDB);
			testDataItemValuesList = testCaseScriptGenerationService.listTestDataItemValuesByTestDataItemId(jsonTestDataItemsValues.getTestDataItemId(),0,30);
			if(testDataItemValuesList != null && testDataItemValuesList.size() > 0 && !testDataItemValuesList.isEmpty()){
				int valListSize = testDataItemValuesList.size();
				if(index > valListSize){
					//code to add a new as the index is greater than the size
					testDataItemvaluesFromUI.setValues(jsonTestDataItemsValues.getModifiedFieldValue());
					testDataItemValuesId = testCaseScriptGenerationService.addTestDataItemValues(testDataItemvaluesFromUI);
				}else{
					//code to update 1.Get the testdataval obj with the old field value 2. Update the same object with modifiedFiledValue 3.Update the test data val table
					//testDataItemvaluesFromDB = testCaseScriptGenerationService. getTestDataItemValuesByProductAndTestItemValName(jsonTestDataItemsValues.getProductId(),null,jsonTestDataItemsValues.getOldFieldValue(),testDataItemvaluesFromUI.getTestDataItems().getTestDataItemId(),testDataItemvaluesFromUI.getTestDataPlan().getTestDataPlanId());
					testDataItemvaluesFromDB = testDataItemValuesList.get(index-1);
					testDataItemvaluesFromDB.setValues(jsonTestDataItemsValues.getModifiedFieldValue());
					testCaseScriptGenerationService.updateTestDataItemValues(testDataItemvaluesFromDB);
				}
			}else{
				//add a new value as there is no existing test data value
				testDataItemvaluesFromUI.setValues(jsonTestDataItemsValues.getModifiedFieldValue());
				testDataItemValuesId = testCaseScriptGenerationService.addTestDataItemValues(testDataItemvaluesFromUI);
			}
			TestDataItems testDataItems = testCaseScriptGenerationService.getTestDataItemByItemId(jsonTestDataItemsValues.getTestDataItemId());
			jsonTestDataItemsValues = jsonTestDataItemsValues.getestDataItemsValues(testDataItems, true);
			jTableResponse = new JTableSingleResponse("OK",jsonTestDataItemsValues);
		}
		catch (Exception e) {
			jTableResponse = new JTableSingleResponse("ERROR", "Unable to update the testData Item values!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value={"testDataItems.values.consolidated.save"}, method={RequestMethod.POST}, produces={"application/json"})
	@ResponseBody
	public JTableSingleResponse addTestDataItemsValues(HttpServletRequest req,@ModelAttribute JsonTestDataItemsValues jsonTestDataItemsValues, BindingResult result) {
		JTableSingleResponse jTableResponse = null;
		TestDataItems testDataItemsFromUI = null;
		Integer testDataItemId = null;
		if (result.hasErrors()) {
			jTableResponse = new JTableSingleResponse("ERROR", "Invalid Form!");
		}
		try {
			UserList user = (UserList)req.getSession().getAttribute("USER");
			testDataItemsFromUI = jsonTestDataItemsValues.getTestDataItems();

			if(testDataItemsFromUI.getTestDataItemId() == null){
				testDataItemsFromUI.setUserlist(user);
				TestDataItems testDataItemsFromDB	= testCaseScriptGenerationService.getTestDataItemByItemName(testDataItemsFromUI.getDataName(),jsonTestDataItemsValues.getProductId(),null);
				if(testDataItemsFromDB == null){
					testDataItemId = testCaseScriptGenerationService.addTestDataItems(testDataItemsFromUI);
				}else if(testDataItemsFromDB.getUserlist().getUserId() == user.getUserId()){
					//if(){}
					testDataItemsFromUI.setModifiedDate(new Date());
					testDataItemsFromUI.setTestDataItemId(testDataItemsFromDB.getTestDataItemId());
					testCaseScriptGenerationService.updateTestDataItems(testDataItemsFromUI);
				}else{
					jTableResponse = new JTableSingleResponse("ERROR", testDataItemsFromDB.getDataName()+" already had been created by "+testDataItemsFromDB.getUserlist().getUserId());
					return jTableResponse;
				}

			}else{
				testDataItemsFromUI.setUserlist(user);
				testCaseScriptGenerationService.updateTestDataItems(testDataItemsFromUI);
			}
			TestDataItems  testDataItems = new TestDataItems();
			testDataItems = testDataItemsFromUI;
			if(testDataItemId != null){
				testDataItems.setTestDataItemId(testDataItemId);
			}
			jsonTestDataItemsValues = jsonTestDataItemsValues.getestDataItemsValues(testDataItems, true);
			jTableResponse = new JTableSingleResponse("OK",jsonTestDataItemsValues);
		}
		catch (Exception e) {
			jTableResponse = new JTableSingleResponse("ERROR", "Unable to update the testDataitems!");
			log.error((Object)"JSON ERROR", (Throwable)e);
		}
		return jTableResponse;
	}
	@RequestMapping(value="keep.atsg.alive",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions keepATSGAlive() {
		log.debug("keep.atsg.alive");
		JTableResponseOptions jTableResponseOptions = null;
	        return jTableResponseOptions= new JTableResponseOptions("OK","Server Hit Success.");
    } 
}