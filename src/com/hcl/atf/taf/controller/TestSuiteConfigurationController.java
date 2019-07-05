package com.hcl.atf.taf.controller;

import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.integration.data.excel.ExcelTestDataIntegrator;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.RunConfigurationTSHasTC;
import com.hcl.atf.taf.model.SCMSystem;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkFlowEvent;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.dto.ISERecommandedTestcases;
import com.hcl.atf.taf.model.dto.ScriptLessExecutionDTO;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.JsonTestCaseStepsList;
import com.hcl.atf.taf.model.json.JsonTestSuiteList;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.atf.taf.util.ZipTool;


@Controller
public class TestSuiteConfigurationController {

	private static final Log log = LogFactory.getLog(TestSuiteConfigurationController.class);
	
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService;
	@Autowired
	private ExcelTestDataIntegrator excelTestDataIntegrator;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private TestCaseService testCaseService;
	@Autowired
	private ExecutionTypeMasterService executionTypeMasterService;
	@Autowired
	private UserListService userListService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private MongoDBService mongoDBService;
	@Autowired
	private EnvironmentService environmentService;
	
	private String trimmingSuite;
	private boolean fileDownloaded;
	
	@Value("#{ilcmProps['TESTSUITE_SCRIPTPACK_BASEFOLDER']}")
	private String testSuiteScriptPackBaseFolder;
	
	@Value("#{ilcmProps['GIT_DOWNLOADER_JAR_LOCATION']}")
	private String gitDownloaderJarLocation;

	@RequestMapping(value="test.suite.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestSuites(@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("inside test.suite.list");
		JTableResponse jTableResponse;
			try {
				
			List<TestSuiteList> testSuiteList=testSuiteConfigurationService.listTestSuite(jtStartIndex, jtPageSize, TAFConstants.ENTITY_STATUS_ACTIVE);
			List<JsonTestSuiteList> jsonTestSuiteList=new ArrayList<JsonTestSuiteList>();
			for(TestSuiteList tsl: testSuiteList){
				jsonTestSuiteList.add(new JsonTestSuiteList(tsl));
			}
            jTableResponse = new JTableResponse("OK", jsonTestSuiteList,testSuiteConfigurationService.getTotalRecordsOfTestSuite());
            testSuiteList = null;
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="test.suite.byProduct.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestSuitesofProductId(@RequestParam int productMasterId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.info("inside test.suite.byProduct.list");
		JTableResponse jTableResponse;
			try {
				
			List<TestSuiteList> testSuiteList=testSuiteConfigurationService.getByProductId(jtStartIndex, jtPageSize, productMasterId);
			List<TestSuiteList> testSuiteListforPagination=testSuiteConfigurationService.getByProductId(null, null, productMasterId);
			List<JsonTestSuiteList> jsonTestSuiteList=new ArrayList<JsonTestSuiteList>();
			for(TestSuiteList tsl: testSuiteList){
				jsonTestSuiteList.add(new JsonTestSuiteList(tsl));
			}
            jTableResponse = new JTableResponse("OK", jsonTestSuiteList, testSuiteListforPagination.size());
            testSuiteList = null;
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
   @RequestMapping(value="test.suite.byProductVersion.list",method=RequestMethod.POST ,produces="application/json")
   public @ResponseBody JTableResponse listTestSuitesOfProductVersionId(@RequestParam int versionId, @RequestParam Integer testRunPlanId, @RequestParam Integer runConfigId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("test.suite.byProductVersion.list");
		JTableResponse jTableResponse;
		try {				
		List<TestSuiteList> testSuiteList=testSuiteConfigurationService.getByProductVersionId(jtStartIndex, jtPageSize, versionId,testRunPlanId);
		List<TestSuiteList> testSuiteListTotal=testSuiteConfigurationService.getByProductVersionId(-1, -1, versionId,testRunPlanId);
		JsonTestSuiteList jsonTestSuite= new JsonTestSuiteList();
		List<JsonTestSuiteList> jsonTestSuiteList=new ArrayList<JsonTestSuiteList>();
		for(TestSuiteList tsl: testSuiteList){
			jsonTestSuite=new JsonTestSuiteList(tsl);
			if(testRunPlanId!=-1){
				TestRunPlan testRunPlan=productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
				Set<TestSuiteList> testSuiteLists=testRunPlan.getTestSuiteLists();
				if(testSuiteLists!=null && !testSuiteLists.isEmpty()){
					if(testSuiteLists.contains(tsl)){
						jsonTestSuite.setIsSelected(1);
					}else{
						jsonTestSuite.setIsSelected(0);
					}
				}else{
					jsonTestSuite.setIsSelected(0);
				}
				
			} else {
				RunConfiguration runconfig = environmentService.getRunConfiguration(runConfigId);
				if(runconfig != null && runconfig.getTestSuiteLists() != null){
					Set<TestSuiteList> testSuiteLists = runconfig.getTestSuiteLists();
					if(testSuiteLists!=null && !testSuiteLists.isEmpty()){
						if(testSuiteLists.contains(tsl)){
							jsonTestSuite.setIsSelected(1);
						}else{
							jsonTestSuite.setIsSelected(0);
						}
					}else{
						jsonTestSuite.setIsSelected(0);
					}
				}
			}
			jsonTestSuiteList.add(jsonTestSuite);
		}
        jTableResponse = new JTableResponse("OK", jsonTestSuiteList,testSuiteListTotal.size());
        testSuiteList = null;
		} catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
        return jTableResponse;
    }
	

	@RequestMapping(value="test.suite.byTestCase.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestSuitesMappedToTestCaseId(@RequestParam int testCaseId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.info("inside test.suite.byTestCase.list");
		JTableResponse jTableResponse;
			try {
				
			List<TestSuiteList> testSuiteList=testSuiteConfigurationService.getTestSuitesMappedToTestCaseByTestCaseId(testCaseId, jtStartIndex, jtPageSize);
			List<TestSuiteList> testSuiteListforPagination=testSuiteConfigurationService.getTestSuitesMappedToTestCaseByTestCaseId(testCaseId, null, null);
			List<JsonTestSuiteList> jsonTestSuiteList=new ArrayList<JsonTestSuiteList>();
			for(TestSuiteList tsl: testSuiteList){
				jsonTestSuiteList.add(new JsonTestSuiteList(tsl));
			}
            jTableResponse = new JTableResponse("OK", jsonTestSuiteList, testSuiteListforPagination.size());
            testSuiteList = null;
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="test.case.byProductVersion.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestCaseOfProductVersionId(@RequestParam int versionId, @RequestParam int testSuiteId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("test.case.byProductVersion.list");
		JTableResponse jTableResponse;
			try {	
				
				TestSuiteList testSuite = testSuiteConfigurationService.getByTestSuiteId(testSuiteId);
				Set<TestCaseList> listTestCase = testSuite.getTestCaseLists();
				List<Integer> mappedtestcaseIdList = new ArrayList<Integer>();				
					for(TestCaseList testCaseList: listTestCase){
						mappedtestcaseIdList.add(testCaseList.getTestCaseId());
					}
			if(versionId == 0){
				versionId= testSuite.getProductVersionListMaster().getProductVersionListId();
			}
			List<TestCaseList> testCaseList = testCaseService.getTestCaseByProductVersionId(jtStartIndex, jtPageSize, versionId);
			List<JsonTestCaseList> jsonTestCaseList = new ArrayList<JsonTestCaseList>();
			for (TestCaseList  tclist: testCaseList) {	
				if(mappedtestcaseIdList != null){
					if(!mappedtestcaseIdList.contains(tclist.getTestCaseId())){
						jsonTestCaseList.add(new JsonTestCaseList(tclist));
					}else{
						//Do nothing
					}
				}else{
					jsonTestCaseList.add(new JsonTestCaseList(tclist));
				}				
			}
			jTableResponse = new JTableResponse("OK", jsonTestCaseList,jsonTestCaseList.size());
			testCaseList = null;			 
			
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching TestcaseList for Product Version!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="testsuite.byProductVersion.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestSuiteOfProductVersionId(@RequestParam int versionId, @RequestParam int testSuiteId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("testsuite.byProductVersion.list");
		JTableResponse jTableResponse;
			try {	
				TestSuiteList testSuite = testSuiteConfigurationService.getByTestSuiteId(testSuiteId);
				if(versionId == 0){					
					versionId = testSuite.getProductVersionListMaster().getProductVersionListId();					
				}
				List<TestSuiteList> testSuiteListmapped = testSuiteConfigurationService.listMappedTestSuite(testSuiteId);				
				List<TestSuiteList> testSuiteList = testSuiteConfigurationService.getByProductVersionId(jtStartIndex, jtPageSize, versionId);
				
				List<Integer> mappedTestSuiteIdList = new ArrayList<Integer>();
				mappedTestSuiteIdList.add(testSuiteId);//Adding itself to be excluded in the list
				for (TestSuiteList testSuiteList2 : testSuiteListmapped) {
					mappedTestSuiteIdList.add(testSuiteList2.getTestSuiteId());
				}
				
				List<JsonTestSuiteList> jsonTestSuiteList = new ArrayList<JsonTestSuiteList>();
				for (TestSuiteList testSuiteList2 : testSuiteList) {
					if(mappedTestSuiteIdList != null){
						if(!mappedTestSuiteIdList.contains(testSuiteList2.getTestSuiteId())){
							jsonTestSuiteList.add(new JsonTestSuiteList(testSuiteList2));
						}
					}
					
				}
							
			jTableResponse = new JTableResponse("OK", jsonTestSuiteList,jsonTestSuiteList.size());
			testSuiteListmapped=null;	 
			
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching TestsuiteList of Testsuite!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="test.suite.list.filter",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestSuites(@RequestParam int jtStartIndex, @RequestParam int jtPageSize,@RequestParam Integer productId) {
		log.debug("inside test.suite.list");
		
		JTableResponse jTableResponse;
			try {
			
			List<TestSuiteList> testSuiteList=testSuiteConfigurationService.filterTestSuites(jtStartIndex, jtPageSize,productId);
			List<JsonTestSuiteList> jsonTestSuiteList=new ArrayList<JsonTestSuiteList>();
			for(TestSuiteList tsl: testSuiteList){
				jsonTestSuiteList.add(new JsonTestSuiteList(tsl));
			}
            jTableResponse = new JTableResponse("OK", jsonTestSuiteList,testSuiteConfigurationService.getTotalRecordsOfTestSuite());
            testSuiteList = null;
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	 
	
	@RequestMapping(value="test.suite.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestSuite(HttpServletRequest request, @ModelAttribute JsonTestSuiteList jsonTestSuiteList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse= new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		
		try {
			
				TestSuiteList testSuiteListFromUI=jsonTestSuiteList.getTestSuiteList();
				
				if(testSuiteListFromUI.getTestSuiteScriptFileLocation() != null){
					trimmingSuite = testSuiteListFromUI.getTestSuiteScriptFileLocation().trim();
				}
								
				if(jsonTestSuiteList.getProductId() != 0){
					ProductMaster productFromUI = productListService.getProductById(jsonTestSuiteList.getProductId());	
					testSuiteListFromUI.setProductMaster(productFromUI);
				}
				if(jsonTestSuiteList.getProductVersionListId() != null && jsonTestSuiteList.getProductVersionListId() != 0){
					ProductVersionListMaster productVersionFromUI = productListService.getProductVersionListMasterById(jsonTestSuiteList.getProductVersionListId());
					testSuiteListFromUI.setProductVersionListMaster(productVersionFromUI);					
				}
				if(jsonTestSuiteList.getExecutionTypeId() != null){
					ExecutionTypeMaster executionTypeMasterFromUI = executionTypeMasterService.getExecutionTypeByExecutionTypeId(jsonTestSuiteList.getExecutionTypeId());
					testSuiteListFromUI.setExecutionTypeMaster(executionTypeMasterFromUI);
				}
				
				
				if(jsonTestSuiteList.getTestScriptType() != null){
					ScriptTypeMaster scriptTypeMaster = new ScriptTypeMaster();
					scriptTypeMaster.setScriptType(jsonTestSuiteList.getTestScriptType());
					testSuiteListFromUI.setScriptTypeMaster(scriptTypeMaster);
				}else{
					testSuiteListFromUI.setScriptTypeMaster(null);
				}	
				
				if(null != trimmingSuite && !trimmingSuite.isEmpty()){
					if(null != testSuiteListFromUI.getScriptPlatformLocation() && testSuiteListFromUI.getScriptPlatformLocation().equalsIgnoreCase("TERMINAL")){
						 if(!trimmingSuite.startsWith("file:///") && trimmingSuite.endsWith(".zip")){
							 if(trimmingSuite.contains("http://")){
								 String httpToBeReplaced = "http://" + request.getServerName()+":" + request.getServerPort()+request.getContextPath()+"/TestScripts/";
								 trimmingSuite = trimmingSuite.replace(httpToBeReplaced, "{Drive}/{Folders}/");
							 }
							 trimmingSuite = "file:///".concat(trimmingSuite).trim();
						 } else {
							 if(trimmingSuite.contains("http://")){
								 String httpToBeReplaced = "http://" + request.getServerName()+":" + request.getServerPort()+request.getContextPath()+"/TestScripts/";
								 trimmingSuite = trimmingSuite.replace(httpToBeReplaced, "{Drive}/{Folders}/");
							 }
						 }
					} else if(null != testSuiteListFromUI.getScriptPlatformLocation() && testSuiteListFromUI.getScriptPlatformLocation().equalsIgnoreCase("SERVER")){
						if(trimmingSuite.startsWith("file:///")){
							 trimmingSuite.replace("file:///", "");
						}						
						trimmingSuite = trimmingSuite.substring(trimmingSuite.lastIndexOf("/")+1,trimmingSuite.length());
						trimmingSuite = trimmingSuite.substring(trimmingSuite.lastIndexOf("\\")+1,trimmingSuite.length());
						trimmingSuite = "http://" + request.getServerName()+":" + request.getServerPort()+request.getContextPath()+"/TestScripts/"+trimmingSuite;
					}
				}
				testSuiteListFromUI.setTestSuiteScriptFileLocation(trimmingSuite);			
				
				String errorMessage = ValidationUtility.validateForNewVersionTestSuiteAddition(testSuiteListFromUI, testSuiteConfigurationService);
				if (errorMessage != null) {
					
					jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
					return jTableSingleResponse;
				}
				testSuiteConfigurationService.addTestSuite(testSuiteListFromUI);
				if(testSuiteListFromUI!=null && testSuiteListFromUI.getTestSuiteId()!=null){
					mongoDBService.addTestSuiteToMongoDB(testSuiteListFromUI.getTestSuiteId());
				}
				
				if(testSuiteListFromUI != null && testSuiteListFromUI.getTestSuiteId() != null){
					UserList user = (UserList)request.getSession().getAttribute("USER");
					int userId=user.getUserId();
					UserList userObj = userListService.getUserListById(userId);
					//Entity Audition History //Addition
					EntityMaster entityMaster = workPackageService.getEntityMasterByName(IDPAConstants.ENTITY_TEST_SUITE);
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_TEST_SUITE, testSuiteListFromUI.getTestSuiteId(), testSuiteListFromUI.getTestSuiteName(), userObj);	
				}
	            jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestSuiteList(testSuiteListFromUI));
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);
	            e.printStackTrace();
	        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="test.suite.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteTestSuite(@RequestParam int testSuiteId) {
		JTableResponse jTableResponse;
		
		try {
				testSuiteConfigurationService.deleteTestSuite(testSuiteId);
				
				jTableResponse = new JTableResponse("OK");
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
            log.error("JSON ERROR", e);
        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="test.suite.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse updateTestSuite(HttpServletRequest request, @ModelAttribute JsonTestSuiteList jsonTestSuiteList, BindingResult result) {
		JTableResponse jTableResponse;
		ProductMaster productMaster = null;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse  = new JTableResponse("ERROR","Invalid form!"); 
		}
		
		try {
				TestSuiteList testSuiteFromDB = testSuiteConfigurationService.getByTestSuiteId(jsonTestSuiteList.getTestSuiteId());
				TestSuiteList testSuiteList=jsonTestSuiteList.getTestSuiteList();
				productMaster = testSuiteFromDB.getProductMaster();
				 
				if(testSuiteList.getTestSuiteScriptFileLocation() != null){
					trimmingSuite = testSuiteList.getTestSuiteScriptFileLocation().trim();
				}

				testSuiteList.setStatus(1);
				testSuiteList.setStatusChangeDate(new Date(System.currentTimeMillis()));
				if(jsonTestSuiteList.getExecutionTypeId() != null){
					ExecutionTypeMaster executionTypeMasterFromUI = executionTypeMasterService.getExecutionTypeByExecutionTypeId(jsonTestSuiteList.getExecutionTypeId());
					testSuiteList.setExecutionTypeMaster(executionTypeMasterFromUI);
				}
				if(jsonTestSuiteList.getTestScriptType() != null || !jsonTestSuiteList.getTestScriptType().equalsIgnoreCase(" ")){					
					ScriptTypeMaster stm = testSuiteConfigurationService.getScriptTypeMasterByscriptType(jsonTestSuiteList.getTestScriptType());
					testSuiteList.setScriptTypeMaster(stm);
				}else {
					testSuiteList.setScriptTypeMaster(null);
				}	
				if(jsonTestSuiteList.getProductVersionListId() != null){
					ProductVersionListMaster versionFromUI = productListService.getProductVersionListMasterById(jsonTestSuiteList.getProductVersionListId());
					testSuiteList.setProductVersionListMaster(versionFromUI);					
				}
			
				if(null != trimmingSuite && !trimmingSuite.isEmpty()){
					if(null != testSuiteList.getScriptPlatformLocation() && testSuiteList.getScriptPlatformLocation().equalsIgnoreCase("TERMINAL")){
						 if(!trimmingSuite.startsWith("file:///") && trimmingSuite.endsWith(".zip")){
							 if(trimmingSuite.contains("http://")){
								 String httpToBeReplaced = "http://" + request.getServerName()+":" + request.getServerPort()+request.getContextPath()+"/TestScripts/";
								 trimmingSuite = trimmingSuite.replace(httpToBeReplaced, "{Drive}/{Folders}/");
							 }
							 trimmingSuite = "file:///".concat(trimmingSuite).trim();
						 } else {
							 if(trimmingSuite.contains("http://")){
								 String httpToBeReplaced = "http://" + request.getServerName()+":" + request.getServerPort()+request.getContextPath()+"/TestScripts/";
								 trimmingSuite = trimmingSuite.replace(httpToBeReplaced, "{Drive}/{Folders}/");
							 }
						 }
					} else if(null != testSuiteList.getScriptPlatformLocation() && testSuiteList.getScriptPlatformLocation().equalsIgnoreCase("SERVER")){
						if(trimmingSuite.startsWith("file:///")){
							 trimmingSuite.replace("file:///", "");
						}						
					}
				}
				testSuiteList.setTestSuiteScriptFileLocation(trimmingSuite);
				
				int testCaseId = 0;
				
				testSuiteList.setTestCaseLists(testSuiteFromDB.getTestCaseLists());
				testSuiteConfigurationService.updateTestSuite(testSuiteList);
				//Entity Audition History //Update
				UserList user = (UserList)request.getSession().getAttribute("USER");
				remarks = "Product :"+productMaster.getProductName()+", TestSuite :"+testSuiteFromDB.getTestSuiteName();
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_TEST_SUITE, testSuiteList.getTestSuiteId(), testSuiteList.getTestSuiteName(),
				jsonTestSuiteList.getModifiedField(), jsonTestSuiteList.getModifiedFieldTitle(),
				jsonTestSuiteList.getOldFieldValue(), jsonTestSuiteList.getModifiedFieldValue(), user, remarks);
				List<JsonTestSuiteList> tmpList =new ArrayList<JsonTestSuiteList>();
				tmpList.add(new JsonTestSuiteList(testSuiteList));
	            jTableResponse = new JTableResponse("OK",tmpList ,1);
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating record!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
   	
	@RequestMapping(value="test.suite.case.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestSuiteCases(@RequestParam String testSuiteId) {
		log.debug("test.suite.cases.list");
		int intTmpTestSuiteId=Integer.parseInt(testSuiteId);
		JTableResponse jTableResponse;
			try {
			
				TestSuiteList testSuite = testSuiteConfigurationService.getByTestSuiteId(intTmpTestSuiteId);
				Set<TestCaseList> listTestCase = testSuite.getTestCaseLists();
				List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
				for(TestCaseList testCaseList: listTestCase){
					jsonTestCaseList.add(new JsonTestCaseList(testCaseList));
				}
			    jTableResponse = new JTableResponse("OK", jsonTestCaseList,testSuiteConfigurationService.getTotalRecordsOfTestSuite());
		           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="test.suite.of.testsuite.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestSuitesTestSuite(@RequestParam String testSuiteId) {
		log.debug("test.suite.of.testsuite.list");
		int intTmpTestSuiteId=Integer.parseInt(testSuiteId);
		JTableResponse jTableResponse;
			try {
			
				TestSuiteList testSuite = testSuiteConfigurationService.getByTestSuiteId(intTmpTestSuiteId);
				Set<TestSuiteList> listTestSuites = testSuite.getTestSuiteList();			

				
				List<JsonTestSuiteList> jsonTestSuiteList=new ArrayList<JsonTestSuiteList>();
					for(TestSuiteList testSuiteObj: listTestSuites){
						jsonTestSuiteList.add(new JsonTestSuiteList(testSuiteObj));
						
					}
			    jTableResponse = new JTableResponse("OK", jsonTestSuiteList,jsonTestSuiteList.size());
		           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Mapped TestSuites records!");
	            log.error("JSON ERROR", e);
	            e.printStackTrace();
	        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="test.suite.case.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestCases(@ModelAttribute JsonTestSuiteList jsonTestSuiteList,@ModelAttribute JsonTestCaseList jsonTestCaseList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		TestCaseList testCaseList=null;
		TestSuiteList testSuiteList = null;
		log.info("inside the test.suite.case.add");
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {		
				testCaseList=jsonTestCaseList.getTestCaseList();
				int testCaseId=Integer.parseInt(jsonTestCaseList.getTestCaseNameOptions());
				testCaseList.setTestCaseId(testCaseId);
				testSuiteList = jsonTestSuiteList.getTestSuiteList();
				
				testSuiteConfigurationService.addTestCase(testCaseList.getTestCaseId(),jsonTestSuiteList.getTestSuiteId());
				List<JsonTestCaseList> jsntestCaseList=new ArrayList<JsonTestCaseList>();
				jsntestCaseList.add(jsonTestCaseList);
				jTableSingleResponse = new JTableSingleResponse("OK",jsntestCaseList);
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);	 
	            e.printStackTrace();
	        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="test.suite.case.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse updateTestCases(@ModelAttribute JsonTestSuiteList jsonTestSuiteList,@ModelAttribute JsonTestCaseList jsonTestCaseList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse = null;
		TestCaseList testCaseList=null;
		TestSuiteList testSuiteList = null;
		log.info("inside the test.suite.case.update");
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {	
			
			testCaseList=jsonTestCaseList.getTestCaseList();
			testSuiteConfigurationService.updateTestCaseManually(testCaseList);
			List<JsonTestCaseList> jsntestCaseList=new ArrayList<JsonTestCaseList>();
			jsntestCaseList.add(jsonTestCaseList);
			jTableSingleResponse = new JTableSingleResponse("OK",jsntestCaseList);
		}
		catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
            log.error("JSON ERROR", e);	 
            e.printStackTrace();
        }
		 return jTableSingleResponse;
	}
	
	@RequestMapping(value="test.suite.case.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteTestCase(@RequestParam int testCaseId) {
		JTableResponse jTableResponse;
		log.info("inside the administration.defect.management.system.delete");
		try {
				testSuiteConfigurationService.deleteTestCase(testCaseId);
				jTableResponse = new JTableResponse("OK");
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
            log.error("JSON ERROR", e);
        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="test.suite.step.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestSuiteCasesSteps(@RequestParam String testCaseId) {
		log.debug("test.suite.step.list");
		
		int intTmpTestCaseId=Integer.parseInt(testCaseId);
		JTableResponse jTableResponse;
			try {
			
				List<TestCaseStepsList> listTestCaseSteps=testSuiteConfigurationService.listTestCaseSteps(intTmpTestCaseId);
				
				List<JsonTestCaseStepsList> jsonTestCaseStepsList=new ArrayList<JsonTestCaseStepsList>();
				
				for(TestCaseStepsList testCaseStepsList: listTestCaseSteps){
					jsonTestCaseStepsList.add(new JsonTestCaseStepsList(testCaseStepsList));
					
				}
				jTableResponse = new JTableResponse("OK", jsonTestCaseStepsList,testSuiteConfigurationService.getTotalRecordsOfTestCaseSteps());
		           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	
	
	@RequestMapping(value="test.suite.details",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestSuiteDetails(@RequestParam String testSuiteId) {
		log.debug("test.suite.cases.list");
		
		Integer intTmpTestSuiteId=Integer.parseInt(testSuiteId);
		JTableResponse jTableResponse;
			try {
			
			TestSuiteList testSuiteList=testSuiteConfigurationService.getByTestSuiteId(intTmpTestSuiteId);
			
			List<JsonTestSuiteList> jsonTestSuiteList=new ArrayList<JsonTestSuiteList>();
				jsonTestSuiteList.add(new JsonTestSuiteList(testSuiteList));
				
			    jTableResponse = new JTableResponse("OK", jsonTestSuiteList,testSuiteConfigurationService.getTotalRecordsOfTestSuite());
	           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	

	@RequestMapping(value="test.suite.refsourcecode.generate",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse generateTestSuiteRefSource(@RequestParam int productId, @RequestParam int testSuiteId) {
		log.debug("inside test.suite.list");
		JTableResponse jTableResponse;
		try {
            jTableResponse = new JTableResponse("OK", "");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Unable to generate Reference class for test cases!");
	        log.error("Unable to generate Reference class for test cases", e);
	    }
        return jTableResponse;
    }

	
	@ExceptionHandler(Exception.class)
	public void handleException(Throwable ex, HttpServletResponse response)
			throws Exception {

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				ex.getMessage());
		ex.printStackTrace();
	}

	
	@RequestMapping(value="test.suite.case.add.bulk",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestCasesToTestSuite(@RequestParam Integer testcaseId,@RequestParam Integer testSuiteId) {
		JTableSingleResponse jTableSingleResponse;
		log.info("inside the test.suite.case.add");
		
		try {		
				TestCaseList testCaseList = testSuiteConfigurationService.getByTestCaseId(testcaseId);
				TestSuiteList testSuiteList=testSuiteConfigurationService.getByTestSuiteId(testSuiteId);

				if(testSuiteList.getTestCaseLists() !=null){
					for(TestCaseList tl : testSuiteList.getTestCaseLists()){
						if(tl.getTestCaseId().equals(testCaseList.getTestCaseId())){
							return new JTableSingleResponse("ERROR","Test Case Already added!");
						}
					}
				}
				
				testSuiteConfigurationService.addTestCase(testCaseList.getTestCaseId(),testSuiteList.getTestSuiteId());
				List<JsonTestCaseList> jsntestCaseList=new ArrayList<JsonTestCaseList>();
				jTableSingleResponse = new JTableSingleResponse("OK",jsntestCaseList);
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);	 
	            e.printStackTrace();
	        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="test.suite.to.testsuite.add.bulk",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestSuiteToTestSuite(@RequestParam Integer testSuiteId,@RequestParam Integer testSuiteIdtobeMapped) {
		JTableSingleResponse jTableSingleResponse;
		log.info("inside test.suite.to.testsuite.add.bulk");		
		try {
			testSuiteConfigurationService.addTestSuiteMapping(testSuiteId, testSuiteIdtobeMapped);
			List<JsonTestSuiteList> jsonTestSuiteList=new ArrayList<JsonTestSuiteList>();				
				jTableSingleResponse = new JTableSingleResponse("OK",jsonTestSuiteList);
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error Mapping TestSuite to TestSuite!");
	            log.error("JSON ERROR", e);	 
	            e.printStackTrace();
	        }	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="test.suite.case.delete.bulk",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteTestCaseFromTestSuite(@RequestParam Integer testcaseId,@RequestParam Integer testSuiteId) {
		JTableResponse jTableResponse;
		log.info("inside the test.suite.case.delete.bulk"+testcaseId+">>>>>>>>>>>>"+testSuiteId);
		try {
			TestCaseList testCaseList = testSuiteConfigurationService.getByTestCaseId(testcaseId);
			TestSuiteList testSuiteList=testSuiteConfigurationService.getByTestSuiteId(testSuiteId);
			Set<TestCaseList> testSuiteListSet=testSuiteList.getTestCaseLists();
			Set<TestCaseList> testSuiteListSetNew=new HashSet<TestCaseList>();
			List<RunConfigurationTSHasTC> rctc = productListService.getRunConfigTestCaseObjectByTestSuite(testcaseId);
			String res = "";
			for(RunConfigurationTSHasTC rct : rctc){
				RunConfiguration rc = productListService.getRunConfigurationById(rct.getRunConfiguration().getRunconfigId());
				res = res + "Test Plan ID : "+rc.getTestRunPlan().getTestRunPlanId() + " Test Configuration ID : " + rc.getRunconfigId() + " Test Suite ID : "
							+rct.getTestSuiteList().getTestSuiteId() + ",<br>";
			}
			if(rctc != null && !rctc.isEmpty()){
				res = res.substring(0, res.length() -1);
				log.info("This Testcase is already selected for execution in one or more Test Configurations. Hence it cannot be unmapped from this Testsuite.");
				return new JTableResponse("ERROR","This Testcase is already selected for execution in 1 or more Test Configurations. Hence it cannot be unmapped from this Testsuite."+res.trim());
			}
				
			for(TestCaseList tc:testSuiteListSet){
				if(!tc.getTestCaseId().equals(testCaseList.getTestCaseId())){
					testSuiteListSetNew.add(tc);
				}
			}
			
			testSuiteList.setTestCaseLists(testSuiteListSetNew);
			
			testSuiteConfigurationService.updateTestSuiteList(testSuiteList);
			log.info("Removed testcase from testsuite successfully");
				
			jTableResponse = new JTableResponse("OK");
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
            log.error("JSON ERROR", e);
            e.printStackTrace();
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="test.suite.delete.testsuite.bulk",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteTestSuiteFromTestSuite(@RequestParam Integer testSuiteId,@RequestParam Integer testSuiteIdtobeUnMapped) {
		JTableResponse jTableResponse;
		log.info("inside the test.suite.delete.testsuite.bulk"+testSuiteId+">>>>>>>>>>>>"+testSuiteIdtobeUnMapped);
		try {
			TestSuiteList testSuiteList=testSuiteConfigurationService.getByTestSuiteId(testSuiteId);			
			TestSuiteList testSuitetobeUnMapped=testSuiteConfigurationService.getByTestSuiteId(testSuiteIdtobeUnMapped);
			
			Set<TestSuiteList> mappedTestSuites = testSuiteList.getTestSuiteList();
			Set<TestSuiteList> newMappedTestSuites = new HashSet<TestSuiteList>();
			for (TestSuiteList testSuiteList2 : mappedTestSuites) {
				if(!testSuiteList2.getTestSuiteId().equals(testSuitetobeUnMapped.getTestSuiteId())){
					newMappedTestSuites.add(testSuiteList2);
				}
			}
			
			testSuiteList.setTestSuiteList(newMappedTestSuites);
			testSuiteConfigurationService.updateTestSuiteList(testSuiteList);
			log.debug("Removed TestSuite from testsuite successfully");
				
			jTableResponse = new JTableResponse("OK");
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error deleting TestSuite from TestSuite record!");
            log.error("JSON ERROR", e);
        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="test.suite.case.testrunplan.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestSuiteTestCaseOfTestRunPlan(@RequestParam Integer testRunPlanId,@RequestParam Integer testSuiteId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("test.suite.byProductVersion.list");
		JTableResponse jTableResponse;
		List<TestCaseList> testCaseList = null;
		List<TestCaseList> testCaseListTotal = null;
		List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
		List<ISERecommandedTestcases> recommendedTestCases = new LinkedList<ISERecommandedTestcases>();
		Set<ISERecommandedTestcases> recommendedTestCasesSet = new HashSet<ISERecommandedTestcases>();
		Map<String, Integer> iseRecommendedTC = new LinkedHashMap<String, Integer>();
		Integer totalTestRunPlanTestCasesCount = 0;
		Integer recommendedTestCasesCount = 0;			
		Double probCount = 0.00;
		//int maxCountThrottleForDemo;
		try {
			TestRunPlan trplan = productListService.getTestRunPlanById(testRunPlanId);
			
			if(trplan == null)
				return new JTableResponse("OK", "Invalid Test Plan");
			
			if(trplan != null && trplan.getTestScriptsLevel().equalsIgnoreCase(IDPAConstants.MULTIPLE_RUNCONFIG_LEVEL_SCRIPT_PACK)){
				 return listTestSuiteTestCaseOfTestConfiguration(testRunPlanId, testSuiteId, jtStartIndex, jtPageSize);
			} else if(trplan != null && !trplan.getTestScriptsLevel().equalsIgnoreCase(IDPAConstants.MULTIPLE_RUNCONFIG_LEVEL_SCRIPT_PACK)){
				//get the total number of test run plan test cases count 
				totalTestRunPlanTestCasesCount = productListService.getTotalTestCaseCountForATestRunPlan(testRunPlanId);
				log.info("Is TRP for Recommended Process : "+trplan.getUseIntelligentTestPlan());
				//For ISE recommended Test case obtain from ISE API
				if (trplan.getUseIntelligentTestPlan() != null && trplan.getUseIntelligentTestPlan().equalsIgnoreCase("Yes")) {
					log.info("Getting Test Plan for ISE");							
					//Make a call to ISE API to get the recommended test plan for this build
					try {
						recommendedTestCases = workPackageService.getIntelligentTestPlanFromISE(trplan);
					} catch (Exception recommendedException) {
						log.error("Problem while invoking ISE ", recommendedException);
					}
										
					if(recommendedTestCases != null && !recommendedTestCases.isEmpty()){
						recommendedTestCasesSet.addAll(recommendedTestCases);
						recommendedTestCases = null;
						recommendedTestCases = new LinkedList<ISERecommandedTestcases>();
						recommendedTestCases.addAll(recommendedTestCasesSet);
						for(int i=0;i< recommendedTestCases.size();i++){
							iseRecommendedTC.put(recommendedTestCases.get(i).getTitle(), i);
						}
					}
				}
				
				if(testSuiteId == -1){					
					Set<TestSuiteList> testSuitSet = trplan.getTestSuiteLists();								
					if(testSuitSet .size() > 0){
						for(TestSuiteList testSuite : testSuitSet){
							testSuiteId = testSuite.getTestSuiteId();
							//String testSuiteName = testSuiteConfigurationService.getTestSuiteNameByTestSuiteId(testSuiteId);
							testCaseList = testSuiteConfigurationService.listTestCaseByTestSuite(testSuiteId, jtStartIndex, jtPageSize);						
							testCaseListTotal = testSuiteConfigurationService.listTestCaseByTestSuite(testSuiteId,-1, -1);
							JsonTestCaseList jsonTestCase= new JsonTestCaseList();
							List<TestCaseList> testCaseListsTRP = null;
							testCaseListsTRP = productListService.getTestSuiteTestCaseMapped(testRunPlanId,testSuiteId);
							log.info("Test Suite ID : "+testSuiteId+" TCTPlan size:"+testCaseListsTRP.size());
							for(TestCaseList tcl: testCaseList){
								String category = "";
								String probability = "";									
								jsonTestCase=new JsonTestCaseList(tcl);
								jsonTestCase.setTestSuiteId(testSuiteId);
								jsonTestCase.setTestSuiteName(testSuite.getTestSuiteName());								
								if(testRunPlanId!=-1){																		
									if(testCaseListsTRP!=null && !testCaseListsTRP.isEmpty()){
										if(testCaseListsTRP.contains(tcl)){
											jsonTestCase.setIsSelected(1);																							
										}else{
											jsonTestCase.setIsSelected(0);
										}
									}else{
										jsonTestCase.setIsSelected(0);
									}										
								}
								
								if(iseRecommendedTC != null && iseRecommendedTC.size() > 0){
									
									if(!iseRecommendedTC.containsKey(tcl.getTestCaseName())){
										productListService.mapTestSuiteTestCasesTestRunPlan(testRunPlanId, testSuiteId,tcl.getTestCaseId(),"Remove");
										log.info("Unmapped Test Case : " +tcl.getTestCaseName()+" to Test Suite ");
										jsonTestCase.setIsSelected(0);
										jsonTestCase.setIseRecommended("NO");
									} else {
										boolean flag = productListService.isTestCaseAlreadyMapped(testRunPlanId, testSuiteId,tcl.getTestCaseId());
										if(!flag){
											productListService.mapTestSuiteTestCasesTestRunPlan(testRunPlanId, testSuiteId,tcl.getTestCaseId(),"Add");
											log.info("mapped Test Case : " +tcl.getTestCaseName()+" to Test Suite ");
										}
										jsonTestCase.setIsSelected(1);		
									}
									
									for(Map.Entry<String, Integer> iseMap : iseRecommendedTC.entrySet()){
										String iseTCName = iseMap.getKey().trim();
										ISERecommandedTestcases result = recommendedTestCases.get(iseMap.getValue());												
										if(iseTCName.equalsIgnoreCase(tcl.getTestCaseName())){
											jsonTestCase.setIseRecommended("YES");
											recommendedTestCasesCount = recommendedTestCasesCount + 1;
											jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
											log.info("Recommended TC Count : " +recommendedTestCasesCount);
											log.info("ISE Recommended Test Case Name : "+iseTCName+" : is matched : "+iseTCName.equalsIgnoreCase(tcl.getTestCaseName())+" : "+jsonTestCase.getIseRecommended());
											if(result.getBT() != null && result.getBT().equalsIgnoreCase("true"))
												category += "BT"+",";
											if(result.getCT() != null && result.getCT().equalsIgnoreCase("true"))
												category += "CT"+",";
											if(result.getET() != null && result.getET().equalsIgnoreCase("true"))
												category += "ET"+",";
											if(result.getGT() != null && result.getGT().equalsIgnoreCase("true"))
												category += "GT"+",";
											if(result.getHFT() != null && result.getHFT().equalsIgnoreCase("true"))
												category += "HFT"+",";
											if(result.getLFT() != null && result.getLFT().equalsIgnoreCase("true"))
												category += "LFT"+",";
											if(result.getNT() != null && result.getNT().equalsIgnoreCase("true"))
												category += "NT"+",";
											
											if(result.getProbability() != null){
												probability = result.getProbability();
												if(probability.equalsIgnoreCase("1")){
													probCount = (Double)(Math.random()*(1 - 0.5)) + 0.5;
												} else if(probability.equalsIgnoreCase("2")){
													probCount = (Double)(Math.random()*(2 - 1.5)) + 1.5;
												} else if(probability.equalsIgnoreCase("3")){
													probCount = (Double)(Math.random()*(3 - 2.5)) + 2.5;
												} else if(probability.equalsIgnoreCase("4")){
													probCount = (Double)(Math.random()*(4 - 3.5)) + 3.5;
												}
												DecimalFormat df = new DecimalFormat("####0.00");
												jsonTestCase.setProbability(df.format(probCount));
											}
											
											if(category != null && !category.isEmpty()){
												category = category.substring(0, category.length()-1);
												jsonTestCase.setRecommendedCategory(category);
											}
										}
									}
								} else {
									jsonTestCase.setIseRecommended("NO");
									jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
								}
								jsonTestCase.setTotalTestCaseCount(totalTestRunPlanTestCasesCount);								
								if(jsonTestCase.getIseRecommended() == null){
									jsonTestCase.setIseRecommended("NO");
									jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
								}
								jsonTestCaseList.add(jsonTestCase);								
							}							
						}
					} else{
						 jTableResponse = new JTableResponse("OK", "Test run plan is not associated with test suite ");
						 return jTableResponse;
					}
				} else {
					testCaseList = testSuiteConfigurationService.listTestCaseByTestSuite(testSuiteId, jtStartIndex, jtPageSize);						
					testCaseListTotal = testSuiteConfigurationService.listTestCaseByTestSuite(testSuiteId,-1, -1);
					JsonTestCaseList jsonTestCase= new JsonTestCaseList();	
					for(TestCaseList tcl: testCaseList){
						String category = "";
						String probability = "";	
						jsonTestCase=new JsonTestCaseList(tcl);
						jsonTestCase.setTestSuiteId(testSuiteId);
						List<TestCaseList> testCaseListsTRP = null;
						if(testRunPlanId!=-1){													
							testCaseListsTRP = productListService.getTestSuiteTestCaseMapped(testRunPlanId,testSuiteId);
							if(testCaseListsTRP!=null && !testCaseListsTRP.isEmpty()){
								log.info("testCaseListsTRP size:"+testCaseListsTRP.size());
								if(testCaseListsTRP.contains(tcl)){
									jsonTestCase.setIsSelected(1);									
								}else{
									jsonTestCase.setIsSelected(0);
								}
							}else{
								jsonTestCase.setIsSelected(0);
							}							
						}
						
						if(iseRecommendedTC != null && iseRecommendedTC.size() > 0){
							
							if(!iseRecommendedTC.containsKey(tcl.getTestCaseName())){
								productListService.mapTestSuiteTestCasesTestRunPlan(testRunPlanId, testSuiteId,tcl.getTestCaseId(),"Remove");
								log.info("Unmapped Test Case : " +tcl.getTestCaseName()+" to Test Suite ");
								jsonTestCase.setIsSelected(0);
								jsonTestCase.setIseRecommended("NO");
							} else {
								boolean flag = productListService.isTestCaseAlreadyMapped(testRunPlanId, testSuiteId,tcl.getTestCaseId());
								if(!flag){
									productListService.mapTestSuiteTestCasesTestRunPlan(testRunPlanId, testSuiteId,tcl.getTestCaseId(),"Add");
									log.info("mapped Test Case : " +tcl.getTestCaseName()+" to Test Suite ");
								}
								jsonTestCase.setIsSelected(1);		
							}
							
							for(Map.Entry<String, Integer> iseMap : iseRecommendedTC.entrySet()){
								String iseTCName = iseMap.getKey().trim();
								ISERecommandedTestcases result = recommendedTestCases.get(iseMap.getValue());								
								if(iseTCName.equalsIgnoreCase(tcl.getTestCaseName())){
									jsonTestCase.setIseRecommended("YES");
									recommendedTestCasesCount = recommendedTestCasesCount + 1;
									jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
									log.info("Recommended TC Count : " +recommendedTestCasesCount);
									log.info("ISE Recommended Test Case Name : "+iseTCName+" : is matched : "+iseTCName.equalsIgnoreCase(tcl.getTestCaseName())+" : "+jsonTestCase.getIseRecommended());
									if(result.getBT() != null && result.getBT().equalsIgnoreCase("true"))
										category += "BT"+",";
									if(result.getCT() != null && result.getCT().equalsIgnoreCase("true"))
										category += "CT"+",";
									if(result.getET() != null && result.getET().equalsIgnoreCase("true"))
										category += "ET"+",";
									if(result.getGT() != null && result.getGT().equalsIgnoreCase("true"))
										category += "GT"+",";
									if(result.getHFT() != null && result.getHFT().equalsIgnoreCase("true"))
										category += "HFT"+",";
									if(result.getLFT() != null && result.getLFT().equalsIgnoreCase("true"))
										category += "LFT"+",";
									if(result.getNT() != null && result.getNT().equalsIgnoreCase("true"))
										category += "NT"+",";
									
									if(result.getProbability() != null){
										if(probability.equalsIgnoreCase("1")){
											probCount = (Double)(Math.random()*(1 - 0.5)) + 0.5;
										} else if(probability.equalsIgnoreCase("2")){
											probCount = (Double)(Math.random()*(2 - 1.5)) + 1.5;
										} else if(probability.equalsIgnoreCase("3")){
											probCount = (Double)(Math.random()*(3 - 2.5)) + 2.5;
										} else if(probability.equalsIgnoreCase("4")){
											probCount = (Double)(Math.random()*(4 - 3.5)) + 3.5;
										}
										DecimalFormat df = new DecimalFormat("####0.00");
										jsonTestCase.setProbability(df.format(probCount));
									}
									
									if(category != null && !category.isEmpty()){
										category = category.substring(0, category.length()-1);
										jsonTestCase.setRecommendedCategory(category);
									}
								}
							}
						} else {
							jsonTestCase.setIseRecommended("NO");
							jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
						}
						jsonTestCase.setTotalTestCaseCount(totalTestRunPlanTestCasesCount);
						if(jsonTestCase.getIseRecommended() == null){
							jsonTestCase.setIseRecommended("NO");
							jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
						}
						jsonTestCaseList.add(jsonTestCase);
					}					
				}	
			}						 
            jTableResponse = new JTableResponse("OK", jsonTestCaseList,testCaseListTotal.size());
            testCaseList = null;
		} catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
	    return jTableResponse;
	}
	
	@RequestMapping(value="test.suite.of.rc.byProductVersion.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestSuitesOfRCByProductVersionId(@RequestParam int versionId, @RequestParam Integer testRunPlanId, @RequestParam Integer runConfigId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
			log.info("test.suite.byProductVersion.list");
		JTableResponse jTableResponse;
			try {				
			List<TestSuiteList> testSuiteList=testSuiteConfigurationService.getByProductVersionId(jtStartIndex, jtPageSize, versionId,testRunPlanId);
			List<TestSuiteList> testSuiteListTotal=testSuiteConfigurationService.getByProductVersionId(-1, -1, versionId,testRunPlanId);
			JsonTestSuiteList jsonTestSuite= new JsonTestSuiteList();
			List<JsonTestSuiteList> jsonTestSuiteList=new ArrayList<JsonTestSuiteList>();
			for(TestSuiteList tsl: testSuiteList){
				jsonTestSuite=new JsonTestSuiteList(tsl);
				if(testRunPlanId!=-1){
					TestRunPlan testRunPlan=productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
					Set<TestSuiteList> testSuiteLists=testRunPlan.getTestSuiteLists();
					if(testSuiteLists!=null && !testSuiteLists.isEmpty()){
						if(testSuiteLists.contains(tsl)){
							jsonTestSuite.setIsSelected(1);
						}else{
							jsonTestSuite.setIsSelected(0);
						}
					}else{
						jsonTestSuite.setIsSelected(0);
					}					
				}
				
				if(runConfigId != -1){
					RunConfiguration runConfig = productListService.getRunConfigurationById(runConfigId);
					Set<TestSuiteList> testSuiteLists = runConfig.getTestSuiteLists();
					if(testSuiteLists!=null && !testSuiteLists.isEmpty()){
						if(testSuiteLists.contains(tsl)){
							jsonTestSuite.setIsSelected(1);
						}else{
							jsonTestSuite.setIsSelected(0);
						}
					}else{
						jsonTestSuite.setIsSelected(0);
					}
				}
				jsonTestSuiteList.add(jsonTestSuite);
			}
            jTableResponse = new JTableResponse("OK", jsonTestSuiteList,testSuiteListTotal.size());
            testSuiteList = null;
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="git.download.script",method=RequestMethod.POST ,produces="application/json")
	   public @ResponseBody JTableResponse downloadGitScript(@RequestParam Integer testSuiteId,@RequestParam Integer productId,@RequestParam Integer scmSystemId) {
				log.info("test.suite.byProductVersion.list");
			JTableResponse jTableResponse=null;
			String filePath = "";
			String cmd;
			Process p;
			File scriptFile;
			String gitDirectoryParam="";
			String gitAPIURLParam="";
				try {
					 SCMSystem scmSys = environmentService.getSourceManagementSystem(productId,scmSystemId) ;
					 if(scmSys !=null){
						 gitAPIURLParam =scmSys.getConnectionUri();
					 }
					 filePath = testSuiteScriptPackBaseFolder+File.separator+productId+File.separator+testSuiteId;
					 gitDirectoryParam=gitDownloaderJarLocation;
					 if((gitAPIURLParam =="" || gitAPIURLParam ==null) || (testSuiteScriptPackBaseFolder=="" || testSuiteScriptPackBaseFolder==null) || (gitDownloaderJarLocation==""|| gitDownloaderJarLocation==null)) {
						 throw new NullPointerException("null");
					 }
					
					try {
						File file = new File(filePath);
						if (!file.exists()) {
							file.mkdirs();
						}else{
							FileUtils.cleanDirectory(file);
						}
						File batchFile = new File(filePath+File.separator+"gitdownloader.bat");
						PrintWriter out = new PrintWriter(batchFile);
						out.println("cd /d %1");
						out.println("java -jar %2 %3 %4");
						//out.println("exit");
					    out.flush();			       
					    out.close();
					    scriptFile = batchFile;
					    cmd =scriptFile.getAbsolutePath()+" \""+gitDirectoryParam+"\" "+gitAPIURLParam+" \""+filePath+"\"";
					    p = Runtime.getRuntime().exec(cmd);			
						p.waitFor();
						batchFile.delete();
						ZipTool.dozip(filePath);
						p.destroy();
					} catch (Exception e1) {
						 log.error("JSON ERROR", e1);
					}
					fileDownloaded = isFileDownloaded(filePath);
					if(!fileDownloaded){
						throw new NullPointerException("null");
					}
					jTableResponse = new JTableResponse("OK", "Downloaded successfully");
				} catch (NullPointerException e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
	        return jTableResponse;
	    }
	
	private boolean isFileDownloaded(String dirPath) {
		boolean flag=false;
	    File dir = new File(dirPath);
	    File[] files = dir.listFiles();
	    if (files == null || files.length == 0) {
	    	flag = false;
	    }else{
	    	flag = true;
	    }
	    return flag;
	}
	
	@RequestMapping(value="test.suite.case.runconfig.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestSuiteTestCaseOfRunConfig(@RequestParam Integer runConfigId,@RequestParam Integer testSuiteId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("test.suite.byProductVersion.list");
		JTableResponse jTableResponse;
		try {	
			if(testSuiteId == -1){
				RunConfiguration runconfig = environmentService.getRunConfiguration(runConfigId) ;
				Set<TestSuiteList> testSuitSet = runconfig.getTestSuiteLists();
				if(testSuitSet .size() > 0){
					for(TestSuiteList testSuite : testSuitSet){
						testSuiteId = testSuite.getTestSuiteId();
					}
				}else{
					 jTableResponse = new JTableResponse("OK", "Run Configuration is not associated with test suite ");
					 return jTableResponse;
				}
			}
			List<TestCaseList> testCaseList=testSuiteConfigurationService.listTestCaseByTestSuite(testSuiteId, jtStartIndex, jtPageSize);
			
			List<TestCaseList> testCaseListTotal=testSuiteConfigurationService.listTestCaseByTestSuite(testSuiteId,-1, -1);
			JsonTestCaseList jsonTestCase= new JsonTestCaseList();
			List<JsonTestCaseList> jsonTestCaseList=new LinkedList<JsonTestCaseList>();
			for(TestCaseList tcl: testCaseList){
				jsonTestCase=new JsonTestCaseList(tcl);
				jsonTestCase.setTestSuiteId(testSuiteId);
				if(runConfigId !=-1){											
					List<TestCaseList> testCaseListsTRP = productListService.getRunConfigTestSuiteTestCaseMapped(runConfigId, testSuiteId);
					if(testCaseListsTRP!=null && !testCaseListsTRP.isEmpty()){
						log.info("testCaseListsTRP size:"+testCaseListsTRP.size());
						if(testCaseListsTRP.contains(tcl)){
							jsonTestCase.setIsSelected(1);
						}else{
							jsonTestCase.setIsSelected(0);
						}
					}else{
						jsonTestCase.setIsSelected(0);
					}
					
				}
				jsonTestCaseList.add(jsonTestCase);
			}
            jTableResponse = new JTableResponse("OK", jsonTestCaseList,testCaseListTotal.size());
            testCaseList = null;
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	public @ResponseBody JTableResponse listUnAttendedTestSuiteTestCaseOfTestRunPlan(@RequestParam Integer testRunPlanId,@RequestParam Integer testSuiteId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize,  @RequestParam Map<String, String> mapData, HttpServletRequest req) {
		log.info("test.suite.byProductVersion.list");
		JTableResponse jTableResponse;
		List<TestCaseList> testCaseList = null;
		List<TestCaseList> testCaseListTotal = null;
		List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
		List<ISERecommandedTestcases> recommendedTestCases = new LinkedList<ISERecommandedTestcases>();
		Set<ISERecommandedTestcases> recommendedTestCasesSet = new HashSet<ISERecommandedTestcases>();
		Map<String, Integer> iseRecommendedTC = new LinkedHashMap<String, Integer>();
		Integer totalTestRunPlanTestCasesCount = 0;
		Integer recommendedTestCasesCount = 0;			
		Double probCount = 0.00;
		try {
			WorkPackage wp = workPackageService.getWorkPackageById(Integer.parseInt(mapData.get("wpId")));
			TestRunPlan trplan = productListService.getTestRunPlanById(testRunPlanId);
			//get the total number of test run plan test cases count 
			totalTestRunPlanTestCasesCount = productListService.getTotalTestCaseCountForATestRunPlan(testRunPlanId);
			log.info("Is TRP for Recommended Process : "+trplan.getUseIntelligentTestPlan());
			//For ISE recommended Test case obtain from ISE API
			if (trplan.getUseIntelligentTestPlan() != null && trplan.getUseIntelligentTestPlan().equalsIgnoreCase("Yes")) {
				log.info("Getting Test Plan for ISE");				
				//Make a call to ISE API to get the recommended test plan for this build					
				try {
					recommendedTestCases = workPackageService.getIntelligentTestPlanFromISE(trplan);
				} catch (Exception recommendedException) {
					log.error("Problem while invoking ISE ", recommendedException);
				}
				
				if(recommendedTestCases != null && !recommendedTestCases.isEmpty()){
					recommendedTestCasesSet.addAll(recommendedTestCases);
					recommendedTestCases = null;
					recommendedTestCases = new LinkedList<ISERecommandedTestcases>();
					recommendedTestCases.addAll(recommendedTestCasesSet);
					for(int i=0;i< recommendedTestCases.size();i++){
						iseRecommendedTC.put(recommendedTestCases.get(i).getTitle(), i);
					}
				}
			}
			
			if(testSuiteId == -1){					
				Set<TestSuiteList> testSuitSet = trplan.getTestSuiteLists();								
				if(testSuitSet .size() > 0) {
					for(TestSuiteList testSuite : testSuitSet){
						testSuiteId = testSuite.getTestSuiteId();
						//String testSuiteName = testSuiteConfigurationService.getTestSuiteNameByTestSuiteId(testSuiteId);
						testCaseList = testSuiteConfigurationService.listTestCaseByTestSuite(testSuiteId, jtStartIndex, jtPageSize);						
						testCaseListTotal = testSuiteConfigurationService.listTestCaseByTestSuite(testSuiteId,-1, -1);
						JsonTestCaseList jsonTestCase= new JsonTestCaseList();
						List<TestCaseList> testCaseListsTRP = null;
						testCaseListsTRP = productListService.getTestSuiteTestCaseMapped(testRunPlanId,testSuiteId);
						log.info("Test Suite ID : "+testSuiteId+" TCTPlan size:"+testCaseListsTRP.size());
						for(TestCaseList tcl: testCaseList){
							String category = "";
							String probability = "";									
							jsonTestCase=new JsonTestCaseList(tcl);
							jsonTestCase.setTestSuiteId(testSuiteId);
							jsonTestCase.setTestSuiteName(testSuite.getTestSuiteName());								
							if(testRunPlanId!=-1){																		
								if(testCaseListsTRP!=null && !testCaseListsTRP.isEmpty()){
									if(testCaseListsTRP.contains(tcl)){
										jsonTestCase.setIsSelected(1);																							
									}else{
										jsonTestCase.setIsSelected(0);
									}
								}else{
									jsonTestCase.setIsSelected(0);
								}										
							}
													
							if(iseRecommendedTC != null && iseRecommendedTC.size() > 0){								
								if(!iseRecommendedTC.containsKey(tcl.getTestCaseName())){
									productListService.mapTestSuiteTestCasesTestRunPlan(testRunPlanId, testSuiteId,tcl.getTestCaseId(),"Remove");
									log.info("Unmapped Test Case : " +tcl.getTestCaseName()+" to Test Suite ");
									jsonTestCase.setIsSelected(0);
									jsonTestCase.setIseRecommended("NO");
								} else {
									boolean flag = productListService.isTestCaseAlreadyMapped(testRunPlanId, testSuiteId,tcl.getTestCaseId());
									if(!flag){
										productListService.mapTestSuiteTestCasesTestRunPlan(testRunPlanId, testSuiteId,tcl.getTestCaseId(),"Add");
										log.info("mapped Test Case : " +tcl.getTestCaseName()+" to Test Suite ");
									}
									jsonTestCase.setIsSelected(1);		
								}
								
								for(Map.Entry<String, Integer> iseMap : iseRecommendedTC.entrySet()){
									String iseTCName = iseMap.getKey().trim();
									ISERecommandedTestcases result = recommendedTestCases.get(iseMap.getValue());										
									if(iseTCName.equalsIgnoreCase(tcl.getTestCaseName())){
										jsonTestCase.setIseRecommended("YES");
										recommendedTestCasesCount++;
										jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
										log.info("Recommended TC Count : " +recommendedTestCasesCount);
										log.info("ISE Recommended Test Case Name : "+iseTCName+" : is matched : "+iseTCName.equalsIgnoreCase(tcl.getTestCaseName())+" : "+jsonTestCase.getIseRecommended());
										if(result.getBT() != null && result.getBT().equalsIgnoreCase("true"))
											category += "BT"+",";
										if(result.getCT() != null && result.getCT().equalsIgnoreCase("true"))
											category += "CT"+",";
										if(result.getET() != null && result.getET().equalsIgnoreCase("true"))
											category += "ET"+",";
										if(result.getGT() != null && result.getGT().equalsIgnoreCase("true"))
											category += "GT"+",";
										if(result.getHFT() != null && result.getHFT().equalsIgnoreCase("true"))
											category += "HFT"+",";
										if(result.getLFT() != null && result.getLFT().equalsIgnoreCase("true"))
											category += "LFT"+",";
										if(result.getNT() != null && result.getNT().equalsIgnoreCase("true"))
											category += "NT"+",";
										
										if(result.getProbability() != null){
											probability = result.getProbability();
											if(probability.equalsIgnoreCase("1")){
												probCount = (Double)(Math.random()*(1 - 0.5)) + 0.5;
											} else if(probability.equalsIgnoreCase("2")){
												probCount = (Double)(Math.random()*(2 - 1.5)) + 1.5;
											} else if(probability.equalsIgnoreCase("3")){
												probCount = (Double)(Math.random()*(3 - 2.5)) + 2.5;
											} else if(probability.equalsIgnoreCase("4")){
												probCount = (Double)(Math.random()*(4 - 3.5)) + 3.5;
											}
											DecimalFormat df = new DecimalFormat("####0.00");
											jsonTestCase.setProbability(df.format(probCount));
										}
										
										if(category != null && !category.isEmpty()){
											category = category.substring(0, category.length()-1);
											jsonTestCase.setRecommendedCategory(category);
										}
									}
								}
							} else {
								jsonTestCase.setIseRecommended("NO");
								jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
							}
							jsonTestCase.setTotalTestCaseCount(totalTestRunPlanTestCasesCount);								
							if(jsonTestCase.getIseRecommended() == null){
								jsonTestCase.setIseRecommended("NO");
								jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
							}
							jsonTestCaseList.add(jsonTestCase);								
						}
					}
					
					//Update Test Plan after Recommended Test Cases by Analytics
					try{
						WorkFlowEvent workFlowEvent = new WorkFlowEvent();
						workFlowEvent.setEventDate(DateUtility.getCurrentTime());
						if(trplan.getUseIntelligentTestPlan().equalsIgnoreCase("Yes")){
							if (recommendedTestCasesCount > 0) {
								workFlowEvent.setRemarks("Performed analysis of build for test planning. " + System.lineSeparator() + recommendedTestCasesCount + " test cases recommended for execution for testing the build." + System.lineSeparator() +  "Updated Test Plan.");
							} else {
								workFlowEvent.setRemarks("Performed analysis of build for test planning. " + System.lineSeparator() + "Insufficient historical data for meaningful analysis.");
							}
						} else {
							workFlowEvent.setRemarks("Skipping Test Plan analysis.");
						}
						workFlowEvent.setUser(null);
						workFlowEvent.setWorkFlow(workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.ENTITY_TEST_RUN_PLAN_ID,IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED));
						workFlowEvent.setEntityTypeRefId(wp.getWorkPackageId());
						workPackageService.addWorkFlowEvent(workFlowEvent);
						wp.setWorkFlowEvent(workFlowEvent);
						workPackageService.updateWorkPackage(wp);
						log.info("Updated Workpackage : " +wp.getWorkPackageId());
					} catch(Exception e){
						log.error("Error while updating workflopw to Workpackage ID : "+wp.getWorkPackageId());
					}
				} else{
					 jTableResponse = new JTableResponse("OK", "Test run plan is not associated with test suite ");
					 return jTableResponse;
				}
			} else {
				testCaseList = testSuiteConfigurationService.listTestCaseByTestSuite(testSuiteId, jtStartIndex, jtPageSize);						
				testCaseListTotal = testSuiteConfigurationService.listTestCaseByTestSuite(testSuiteId,-1, -1);
				JsonTestCaseList jsonTestCase= new JsonTestCaseList();	
				for(TestCaseList tcl: testCaseList){
					String category = "";
					String probability = "";	
					jsonTestCase=new JsonTestCaseList(tcl);
					jsonTestCase.setTestSuiteId(testSuiteId);
					List<TestCaseList> testCaseListsTRP = null;
					if(testRunPlanId!=-1){													
						testCaseListsTRP = productListService.getTestSuiteTestCaseMapped(testRunPlanId,testSuiteId);
						if(testCaseListsTRP!=null && !testCaseListsTRP.isEmpty()){
							log.info("testCaseListsTRP size:"+testCaseListsTRP.size());
							if(testCaseListsTRP.contains(tcl)){
								jsonTestCase.setIsSelected(1);									
							}else{
								jsonTestCase.setIsSelected(0);
							}
						}else{
							jsonTestCase.setIsSelected(0);
						}							
					}
									
					if(iseRecommendedTC != null && iseRecommendedTC.size() > 0){
						//TODO : DO NOT UPDATE TEST PLAN & TEST CONFIGURATION MAPPINGS
						if(!iseRecommendedTC.containsKey(tcl.getTestCaseName())){
							productListService.mapTestSuiteTestCasesTestRunPlan(testRunPlanId, testSuiteId,tcl.getTestCaseId(),"Remove");
							log.info("Unmapped Test Case : " +tcl.getTestCaseName()+" to Test Suite ");
							jsonTestCase.setIsSelected(0);
							jsonTestCase.setIseRecommended("NO");
						} else {
							productListService.mapTestSuiteTestCasesTestRunPlan(testRunPlanId, testSuiteId,tcl.getTestCaseId(),"Add");
							log.info("mapped Test Case : " +tcl.getTestCaseName()+" to Test Suite ");
							jsonTestCase.setIsSelected(1);
						}
						//TODO : END
						
						for(Map.Entry<String, Integer> iseMap : iseRecommendedTC.entrySet()){
							String iseTCName = iseMap.getKey().trim();
							ISERecommandedTestcases result = recommendedTestCases.get(iseMap.getValue());								
							if(iseTCName.equalsIgnoreCase(tcl.getTestCaseName())){
								jsonTestCase.setIseRecommended("YES");
								recommendedTestCasesCount ++;
								jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
								log.info("Recommended TC Count : " +recommendedTestCasesCount);
								log.info("ISE Recommended Test Case Name : "+iseTCName+" : is matched : "+iseTCName.equalsIgnoreCase(tcl.getTestCaseName())+" : "+jsonTestCase.getIseRecommended());
								if(result.getBT() != null && result.getBT().equalsIgnoreCase("true"))
									category += "BT"+",";
								if(result.getCT() != null && result.getCT().equalsIgnoreCase("true"))
									category += "CT"+",";
								if(result.getET() != null && result.getET().equalsIgnoreCase("true"))
									category += "ET"+",";
								if(result.getGT() != null && result.getGT().equalsIgnoreCase("true"))
									category += "GT"+",";
								if(result.getHFT() != null && result.getHFT().equalsIgnoreCase("true"))
									category += "HFT"+",";
								if(result.getLFT() != null && result.getLFT().equalsIgnoreCase("true"))
									category += "LFT"+",";
								if(result.getNT() != null && result.getNT().equalsIgnoreCase("true"))
									category += "NT"+",";
								
								//TODO : This is for the demo and needs to be removed. Probablity should be as given by the ISE platform
								if(result.getProbability() != null){
									if(probability.equalsIgnoreCase("1")){
										probCount = (Double)(Math.random()*(1 - 0.5)) + 0.5;
									} else if(probability.equalsIgnoreCase("2")){
										probCount = (Double)(Math.random()*(2 - 1.5)) + 1.5;
									} else if(probability.equalsIgnoreCase("3")){
										probCount = (Double)(Math.random()*(3 - 2.5)) + 2.5;
									} else if(probability.equalsIgnoreCase("4")){
										probCount = (Double)(Math.random()*(4 - 3.5)) + 3.5;
									}
									DecimalFormat df = new DecimalFormat("####0.00");
									jsonTestCase.setProbability(df.format(probCount));
								}
								//TODO : END
								
								if(category != null && !category.isEmpty()){
									category = category.substring(0, category.length()-1);
									jsonTestCase.setRecommendedCategory(category);
								}
							}
						}
					} else {
						jsonTestCase.setIseRecommended("NO");
						jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
					}
					jsonTestCase.setTotalTestCaseCount(totalTestRunPlanTestCasesCount);
					if(jsonTestCase.getIseRecommended() == null){
						jsonTestCase.setIseRecommended("NO");
						jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
					}
					jsonTestCaseList.add(jsonTestCase);
				}
				//Update Test Plan after Recommended Test Cases by Analytics
				try{
					WorkFlowEvent workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					if(trplan.getUseIntelligentTestPlan().equalsIgnoreCase("Yes")){
						if (recommendedTestCasesCount > 0) {
							workFlowEvent.setRemarks("Performed analysis of build for test planning. " + System.lineSeparator() + recommendedTestCasesCount + " test cases recommended for execution for testing the build." + System.lineSeparator() +  "Updated Test Plan.");
						} else {
							workFlowEvent.setRemarks("Performed analysis of build for test planning. " + System.lineSeparator() + "Insufficient historical data for meaningful analysis.");
						}
					} else {
						workFlowEvent.setRemarks("Skipping Test Plan analysis.");
					}
					workFlowEvent.setUser(null);
					workFlowEvent.setWorkFlow(workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.ENTITY_TEST_RUN_PLAN_ID,IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED));
					workFlowEvent.setEntityTypeRefId(wp.getWorkPackageId());
					workPackageService.addWorkFlowEvent(workFlowEvent);
					wp.setWorkFlowEvent(workFlowEvent);
					workPackageService.updateWorkPackage(wp);
					log.info("Updated Workpackage : " +wp.getWorkPackageId());
				} catch(Exception e){
					log.error("Error while updating workflopw to Workpackage ID : "+wp.getWorkPackageId());
				}
			}			 
			jTableResponse = new JTableResponse("OK", jsonTestCaseList,testCaseListTotal.size());
			testCaseList = null;
		} catch (Exception e) {
         jTableResponse = new JTableResponse("ERROR","Error fetching records!");
         log.error("JSON ERROR", e);
     }
	    return jTableResponse;
	}
		

	
	
	
	//Added new implementation for retrieving Test Configuration Test Suite / Test Cases
	@RequestMapping(value="test.suite.case.testconfiguration.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestSuiteTestCaseOfTestConfiguration(@RequestParam Integer testRunPlanId, @RequestParam Integer testSuiteId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("test.suite.case.testconfiguration.list");
		JTableResponse jTableResponse;
		List<TestCaseList> testCaseListTotal = null;
		List<JsonTestCaseList> jsonTestCaseList = new ArrayList<JsonTestCaseList>();
		List<ISERecommandedTestcases> recommendedTestCases = new LinkedList<ISERecommandedTestcases>();
		Map<String, Integer> iseRecommendedTC = new LinkedHashMap<String, Integer>();
		Integer totalTestConfigurationTestCasesCount = 0;
		Integer recommendedTestCasesCount = 0;			
		Double probCount = 0.00;
		Set<RunConfiguration> testConfigurationSet = new LinkedHashSet<RunConfiguration>();
		List<TestSuiteList> testConfigurationTestSuiteSet = new LinkedList<TestSuiteList>();
		Set<TestCaseList> testConfigurationTestSuiteTestCaseSet = new HashSet<TestCaseList>();
		
		try {
			TestRunPlan trplan = productListService.getTestRunPlanById(testRunPlanId);
		
			if(trplan == null)
				return new JTableResponse("OK", "Invalid Test Plan Analysis requested");
		
			//Obtain list of Test Configuration mapped for the Test Plan
			if(trplan.getRunConfigurationList() != null && !trplan.getRunConfigurationList().isEmpty()){
				testConfigurationSet = trplan.getRunConfigurationList(); 
			}
			
			if(testConfigurationSet == null || testConfigurationSet.isEmpty())
				return new JTableResponse("OK", "Test Plan is not associated with any Test Configuration");
			
			//Iterate each Test Configuration to obtain list of test suites associated
			for(RunConfiguration testConfig : testConfigurationSet){
				if(testConfig != null && testConfig.getTestSuiteLists() != null && ! testConfig.getTestSuiteLists().isEmpty()){
					testConfigurationTestSuiteSet.addAll(testConfig.getTestSuiteLists());
					for(TestSuiteList testConfigTestSuite : testConfig.getTestSuiteLists()) {
						if(testConfigTestSuite != null 
								&& testConfigTestSuite.getTestCaseLists() != null 
								&& !testConfigTestSuite.getTestCaseLists().isEmpty()){
							testConfigurationTestSuiteTestCaseSet.addAll(testConfigTestSuite.getTestCaseLists());							
						}						
					}	
				}				
			}
			
			if(testConfigurationTestSuiteSet == null || testConfigurationTestSuiteSet.isEmpty())
				return new JTableResponse("OK", "Any Test Configuration is not associated with any Test Suite");
			
			//Get total number of test configuration test cases count
			if(testConfigurationTestSuiteTestCaseSet != null && !testConfigurationTestSuiteTestCaseSet.isEmpty())
				totalTestConfigurationTestCasesCount = testConfigurationTestSuiteTestCaseSet.size();
			
			log.info("Is Test Configuration for Recommended Process : "+trplan.getUseIntelligentTestPlan());
			
			//For ISE recommended Test case obtain from ISE API
			if (trplan.getUseIntelligentTestPlan() != null && trplan.getUseIntelligentTestPlan().equalsIgnoreCase("Yes")) {
				log.info("Getting Test Plan for ISE");							
				//Make a call to ISE API to get the recommended test plan for this build
				try {
					recommendedTestCases = workPackageService.getIntelligentTestPlanFromISE(trplan);
				} catch (Exception recommendedException) {
					log.error("Problem while invoking ISE ", recommendedException);
				}
				
				if(recommendedTestCases != null && !recommendedTestCases.isEmpty()){
					for(int i=0;i< recommendedTestCases.size();i++){
						iseRecommendedTC.put(recommendedTestCases.get(i).getTitle(), i);
					}
				}
			}
			
			testConfigurationTestSuiteSet = null;
			testConfigurationTestSuiteSet = new LinkedList<TestSuiteList>();
			
			for(RunConfiguration testConfig : testConfigurationSet){
				testConfigurationTestSuiteSet.addAll(testConfig.getTestSuiteLists());
				for(TestSuiteList testSuite : testConfigurationTestSuiteSet){
					testSuiteId = testSuite.getTestSuiteId();
					testCaseListTotal = testSuiteConfigurationService.listTestCaseByTestSuite(testSuiteId,-1, -1);					
					List<TestCaseList> testCaseListsTConfig = productListService.getRunConfigTestSuiteTestCaseMapped(testConfig.getRunconfigId(), testSuiteId);
					log.info("Test Suite ID : "+testSuiteId+" TCTConfiguration size:"+testCaseListsTConfig.size());
					for(TestCaseList tcl: testSuite.getTestCaseLists()){
						String category = "";
						String probability = "";									
						JsonTestCaseList jsonTestCase = new JsonTestCaseList(tcl);
						jsonTestCase.setTestSuiteId(testSuiteId);
						jsonTestCase.setTestSuiteName(testSuite.getTestSuiteName());								
					
						if(testConfig.getRunconfigId() != -1){																		
							if(testCaseListsTConfig !=null && !testCaseListsTConfig.isEmpty()){
								if(testCaseListsTConfig.contains(tcl)){
									jsonTestCase.setIsSelected(1);																							
								}else{
									jsonTestCase.setIsSelected(0);
								}
							}else{
								jsonTestCase.setIsSelected(0);
							}										
						}
						if(iseRecommendedTC != null && iseRecommendedTC.size() > 0){
							for(Map.Entry<String, Integer> iseMap : iseRecommendedTC.entrySet()){
								String iseTCName = iseMap.getKey().trim();
								ISERecommandedTestcases result = recommendedTestCases.get(iseMap.getValue());										
								if(iseTCName.equalsIgnoreCase(tcl.getTestCaseName()) && result.getTestbed().equalsIgnoreCase(testConfig.getEnvironmentcombination().getEnvironmentCombinationName())){
									jsonTestCase.setIseRecommended("YES");
									recommendedTestCasesCount = recommendedTestCasesCount + 1;
									jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
									log.info("Recommended TC Count : " +recommendedTestCasesCount);
									log.info("ISE Recommended Test Case Name : "+iseTCName+" : is matched : "+iseTCName.equalsIgnoreCase(tcl.getTestCaseName())+" : "+jsonTestCase.getIseRecommended());
									if(result.getBT() != null && result.getBT().equalsIgnoreCase("true"))
										category += "BT"+",";
									if(result.getCT() != null && result.getCT().equalsIgnoreCase("true"))
										category += "CT"+",";
									if(result.getET() != null && result.getET().equalsIgnoreCase("true"))
										category += "ET"+",";
									if(result.getGT() != null && result.getGT().equalsIgnoreCase("true"))
										category += "GT"+",";
									if(result.getHFT() != null && result.getHFT().equalsIgnoreCase("true"))
										category += "HFT"+",";
									if(result.getLFT() != null && result.getLFT().equalsIgnoreCase("true"))
										category += "LFT"+",";
									if(result.getNT() != null && result.getNT().equalsIgnoreCase("true"))
										category += "NT"+",";
									
									if(result.getProbability() != null){
										probability = result.getProbability();
										if(probability.equalsIgnoreCase("1")){
											probCount = (Double)(Math.random()*(1 - 0.5)) + 0.5;
										} else if(probability.equalsIgnoreCase("2")){
											probCount = (Double)(Math.random()*(2 - 1.5)) + 1.5;
										} else if(probability.equalsIgnoreCase("3")){
											probCount = (Double)(Math.random()*(3 - 2.5)) + 2.5;
										} else if(probability.equalsIgnoreCase("4")){
											probCount = (Double)(Math.random()*(4 - 3.5)) + 3.5;
										}
										DecimalFormat df = new DecimalFormat("####0.00");
										jsonTestCase.setProbability(df.format(probCount));
									}
									
									if(category != null && !category.isEmpty()){
										category = category.substring(0, category.length()-1);
										jsonTestCase.setRecommendedCategory(category);
									}
									//If the recommended test case from Analytics is not mapped map it manually
									try{
										boolean flag = productListService.isTestConfigurationTestCaseAlreadyMapped(testConfig.getRunconfigId(), testSuiteId,tcl.getTestCaseId());
										if(!flag){
											productListService.mapTestSuiteTestCasesRunConfiguration(testConfig.getRunconfigId(), testSuiteId,tcl.getTestCaseId(),"Add");
											log.info("Mapped Test Case : " +tcl.getTestCaseName()+" to Test Suite ");
										}										
										jsonTestCase.setIsSelected(1);	
									} catch(Exception e){
										log.error("Unable to map Test Case : " +tcl.getTestCaseName()+" to Test Suite ");
									}	
								}
							}
						} else {
							jsonTestCase.setIseRecommended("NO");
							jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
						}
						jsonTestCase.setTotalTestCaseCount(totalTestConfigurationTestCasesCount);								
						if(jsonTestCase.getIseRecommended() == null){
							jsonTestCase.setIseRecommended("NO");
							jsonTestCase.setRecommendedTestCaseCount(recommendedTestCasesCount);
						}
						jsonTestCaseList.add(jsonTestCase);								
					}							
				}
				
				//Reset Test Configuration Test Suite List to new stage
				testConfigurationTestSuiteSet = null;
				testConfigurationTestSuiteSet = new LinkedList<TestSuiteList>();
			}								 
            jTableResponse = new JTableResponse("OK", jsonTestCaseList,testCaseListTotal.size());
		} catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error retrieving Test Configuration Test Suite Test Case Data!");
            log.error("JSON ERROR", e);
        }
	    return jTableResponse;
	}
	@RequestMapping(value="test.suite.case.testplan.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestSuiteTestCaseOfTestPlan(@RequestParam Integer testRunPlanId,@RequestParam Integer testSuiteId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("test.suite.byProductVersion.list");
		JTableResponse jTableResponse;
		try {	
			
			List<TestCaseList> testCaseList=testSuiteConfigurationService.listTestCaseByTestSuite(testSuiteId, jtStartIndex, jtPageSize);
			
			List<TestCaseList> testCaseListTotal=testSuiteConfigurationService.listTestCaseByTestSuite(testSuiteId,-1, -1);
			JsonTestCaseList jsonTestCase= new JsonTestCaseList();
			List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
			for(TestCaseList tcl: testCaseList){
				jsonTestCase=new JsonTestCaseList(tcl);
				jsonTestCase.setTestSuiteId(testSuiteId);
				if(testRunPlanId !=-1){											
					List<TestCaseList> testCaseListsTRP = productListService.getTestSuiteTestCaseMapped(testRunPlanId, testSuiteId);
					if(testCaseListsTRP!=null && !testCaseListsTRP.isEmpty()){
						log.info("testCaseListsTRP size:"+testCaseListsTRP.size());
						if(testCaseListsTRP.contains(tcl)){
							jsonTestCase.setIsSelected(1);
						}else{
							jsonTestCase.setIsSelected(0);
						}
					}else{
						jsonTestCase.setIsSelected(0);
					}
					
				}
				jsonTestCaseList.add(jsonTestCase);
			}
            jTableResponse = new JTableResponse("OK", jsonTestCaseList,testCaseListTotal.size());
            testCaseList = null;
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="test.suite.case.testrunplan.unattended",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse executeWorkpackageWithAnalyticsRecommendedTestcases(@RequestParam Integer testRunPlanId,@RequestParam Integer testSuiteId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize,  @RequestParam Map<String, String> mapData, HttpServletRequest req) {
		JTableSingleResponse jTableResponse;
		String recommendedTestcaseNames;
		Set<TestSuiteList> testSuiteList = new HashSet<TestSuiteList>();
		Set<TestCaseList> totalTestCaseList =new HashSet<TestCaseList>();
		List<TestCaseList> testCaseList=new ArrayList<TestCaseList>();
		int executionTCCount = 0;
		int tcCount = 0;
		try {
			WorkPackage wp = workPackageService.getWorkPackageById(Integer.parseInt(mapData.get("wpId")));
			TestRunPlan trplan = productListService.getTestRunPlanById(testRunPlanId);
			UserList user=(UserList)req.getSession().getAttribute("USER");
			recommendedTestcaseNames = workPackageService.getAnalyticsRecommendedTestcasesUnattended(testRunPlanId, testSuiteId, mapData);
						
			if (recommendedTestcaseNames == null || recommendedTestcaseNames.trim().isEmpty()) {				
				workPackageService.workpackageExxecutionPlan(wp, trplan, null, null, null);
			} else {
				workPackageService.workpackageExxecutionPlan(wp, trplan, null, null, recommendedTestcaseNames);
			}
						
			Set<RunConfiguration> rcList = trplan.getRunConfigurationList();
			for(RunConfiguration rc : rcList){
				testSuiteList = rc.getTestSuiteLists();
				if (testSuiteList.size() > 0) {
					for (TestSuiteList ts : testSuiteList) {
						testCaseList = productListService.getRunConfigTestSuiteTestCaseMapped(rc.getRunconfigId(), ts.getTestSuiteId());
						executionTCCount = executionTCCount + testCaseList.size();
						for(TestCaseList tl : testCaseList)
							totalTestCaseList.add(tl);
					}
				}
				tcCount = 	totalTestCaseList.size();
			}
			
			String eventRemarks = null;
			String jobId = "not created.";
			Integer jobCount = 0;
			if(ScriptLessExecutionDTO.getJobIDs() != null && !ScriptLessExecutionDTO.getJobIDs().isEmpty()){
				jobId = ScriptLessExecutionDTO.getJobIDs().substring(0, ScriptLessExecutionDTO.getJobIDs().lastIndexOf(","))+".";
				jobCount = ScriptLessExecutionDTO.getJobIDs().split(",").length;
			}
			//This plan is for Workpackage planning
			if(trplan.getUseIntelligentTestPlan() != null && trplan.getUseIntelligentTestPlan().equalsIgnoreCase("yes")){
				if(ScriptLessExecutionDTO.getTestCasesExecutionCount() != null && ScriptLessExecutionDTO.getTestCasesExecutionCount() > 0){
					tcCount = ScriptLessExecutionDTO.getTestCasesExecutionCount();
					eventRemarks =  "A total of  "+ tcCount +" testcases will be executed across " + jobCount + " test jobs."
							+ System.lineSeparator() + "Test Job IDs are " +  jobId;
				} else {
					eventRemarks =  "A total of  "+ tcCount +" testcases will be executed "+ executionTCCount +" times across " + jobCount + " test jobs."
							+ System.lineSeparator() + "Test Job IDs are " +  jobId;
				}
			} else {
				eventRemarks =  "A total of  "+ tcCount +" testcases will be executed "+ executionTCCount +" times across " + jobCount + " test jobs."
						+ System.lineSeparator() + "Test Job IDs are " +  jobId;
			}
			
			WorkFlowEvent workFlowEvent = new WorkFlowEvent();
			workFlowEvent.setEventDate(DateUtility.getCurrentTime());
			if(ScriptLessExecutionDTO.getJobIDs() != null && !ScriptLessExecutionDTO.getJobIDs().isEmpty())
				workFlowEvent.setRemarks(ScriptLessExecutionDTO.getJobsCount() + " Jobs created with IDs " + ScriptLessExecutionDTO.getJobIDs().substring(0, ScriptLessExecutionDTO.getJobIDs().length()-1) + System.lineSeparator() + "."); // TODO : + "The total number of testcase execution that will " + ScriptLessExecutionDTO.getTotalTestcaseExecutionsCount() + ".");
			else 
				workFlowEvent.setRemarks("No Jobs created.");			
			workFlowEvent.setRemarks(eventRemarks);
			workFlowEvent.setUser(user);
			workFlowEvent.setWorkFlow(workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_WORKPACKAGE_PLANNING));
			workFlowEvent.setEntityTypeRefId(wp.getWorkPackageId());
			workPackageService.addWorkFlowEvent(workFlowEvent);
			wp.setWorkFlowEvent(workFlowEvent);
			workPackageService.updateWorkPackage(wp);
			log.info("Updated Workpackage : " +wp.getWorkPackageId());

			String jobIds = ScriptLessExecutionDTO.getJobIDs();
			if (jobIds != null && !jobIds.trim().equalsIgnoreCase("")) {
				jTableResponse = new JTableSingleResponse("OK","Test Run Plan execution initiated. Workpackage "+ wp.getWorkPackageId() + "["
								+ wp.getName() + "]"
								+ " created. Jobs are "
								+ jobIds.substring(0, jobIds.length() - 1));				
			} else {
				jTableResponse = new JTableSingleResponse("OK",
						"Test Run Plan execution initiated. Workpackage "
								+ wp.getWorkPackageId() + "["
								+ wp.getName() + "]" + " created.");
			}
			
		} catch (Exception e) {
			jTableResponse = new JTableSingleResponse("ERROR","Error while executing Test Plan!");
			log.error("JSON ERROR", e);
		}
	    return jTableResponse;
	}
	   @RequestMapping(value="test.suite.byProduct.with.mappings.list",method=RequestMethod.POST ,produces="application/json")
	   public @ResponseBody JTableResponse listTestSuitesOfProductWithMapping(@RequestParam int productMasterId, @RequestParam Integer testRunPlanId, @RequestParam Integer runConfigId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
			log.info("test.suite.byProductVersion.list");
			JTableResponse jTableResponse;
			try {		
				
			List<TestSuiteList> testSuiteList=testSuiteConfigurationService.getByProductId(jtStartIndex, jtPageSize, productMasterId);
			List<TestSuiteList> testSuiteListTotal=testSuiteConfigurationService.getByProductId(null, null, productMasterId);
				
			JsonTestSuiteList jsonTestSuite= new JsonTestSuiteList();
			List<JsonTestSuiteList> jsonTestSuiteList=new ArrayList<JsonTestSuiteList>();
			for(TestSuiteList tsl: testSuiteList){
				jsonTestSuite=new JsonTestSuiteList(tsl);
				if(testRunPlanId!=-1){
					TestRunPlan testRunPlan=productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
					Set<TestSuiteList> testSuiteLists=testRunPlan.getTestSuiteLists();
					if(testSuiteLists!=null && !testSuiteLists.isEmpty()){
						if(testSuiteLists.contains(tsl)){
							jsonTestSuite.setIsSelected(1);
						}else{
							jsonTestSuite.setIsSelected(0);
						}
					}else{
						jsonTestSuite.setIsSelected(0);
					}
					
				} else {
					RunConfiguration runconfig = environmentService.getRunConfiguration(runConfigId);
					if(runconfig != null && runconfig.getTestSuiteLists() != null){
						Set<TestSuiteList> testSuiteLists = runconfig.getTestSuiteLists();
						if(testSuiteLists!=null && !testSuiteLists.isEmpty()){
							if(testSuiteLists.contains(tsl)){
								jsonTestSuite.setIsSelected(1);
							}else{
								jsonTestSuite.setIsSelected(0);
							}
						}else{
							jsonTestSuite.setIsSelected(0);
						}
					}
				}
				jsonTestSuiteList.add(jsonTestSuite);
			}
	        jTableResponse = new JTableResponse("OK", jsonTestSuiteList,testSuiteListTotal.size());
	        testSuiteList = null;
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        return jTableResponse;
	    }	
}