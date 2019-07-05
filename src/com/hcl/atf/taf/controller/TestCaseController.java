package com.hcl.atf.taf.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.ISEServerAccesUtility;
import com.hcl.atf.taf.controller.utilities.UploadForm;
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.integration.data.excel.ExcelTestDataIntegrator;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.SimilartoTestcaseMapping;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestcaseTypeMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonDecouplingCategory;
import com.hcl.atf.taf.model.json.JsonFeatureTestCaseDefect;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.JsonTestCaseStepsList;
import com.hcl.atf.taf.model.json.JsonTestExecutionResultBugList;
import com.hcl.atf.taf.model.json.JsonTestSuiteList;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionResultSummary;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.TestCaseStepsListMongoDAO;
import com.hcl.atf.taf.mongodb.dao.TestCasesMongoDAO;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.DataTreeService;
import com.hcl.atf.taf.service.DecouplingCategoryService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.SimilartoTestcaseMappingService;
import com.hcl.atf.taf.service.TestCaseScriptGenerationService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.atf.taf.util.Configuration;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.service.WorkflowEventService;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;
import com.hcl.ilcm.workflow.service.WorkflowStatusService;
@Controller
public class TestCaseController {

	private static final Log log = LogFactory.getLog(TestCaseController.class);
	
	@Autowired
	private TestCaseService testCaseService;
	
	@Autowired
	private DataTreeService dataTreeService;

	@Autowired
	private UserListService userListService;
	
	@Autowired
	private ExcelTestDataIntegrator excelTestDataIntegrator;
	
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService;
	
	@Autowired
	private ProductListService productListService;
	
	@Autowired
	private ExecutionTypeMasterService executionTypeMasterService;
	
	@Autowired
	private WorkPackageService workPackageService;

	@Autowired
	private DecouplingCategoryService decouplingCategoryService;
	
	@Autowired
	private TestCasesMongoDAO testCasesMongoDAO;
	
	@Autowired
	private MongoDBService mongoDBService;	
	
	@Autowired
	private TestCaseStepsListMongoDAO testCaseStepsListMongoDAO;
	
	@Autowired
	private WorkflowStatusService workflowStatusService;
	
	@Autowired
	private WorkflowStatusPolicyService workflowStatusPolicyService;
	
	@Autowired
	private WorkflowEventService workflowEventService;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private ProductMasterDAO productMasterDAO;
	
	@Autowired
	private ProductVersionListMasterDAO productVersionListMasterDAO;
	
	@Autowired
	private ProductBuildDAO productBuildDAO;
	
	@Autowired
	private EnvironmentService environmentService;
	
	@Value("#{ilcmProps['MONGODB_AVAILABLE']}")
    private String MONGODB_AVAILABLE;
	
	@Value("#{ilcmProps['ISE_SERVER_URL']}")
    private String iseServerURL;
	
	@Value("#{ilcmProps['ISE_SIMILAR_TESTCASE_SEARCH_API']}")
	private String similarToTestcaseServiceAPI;
	
	@Autowired
	private TestCaseScriptGenerationService testCaseScriptGenerationService;
	
	@Autowired
	private SimilartoTestcaseMappingService similartoTestcaseMappingService;
	
	private static final int BUFFER_SIZE = 4096;
	
	private String trimmingSuite;


	@RequestMapping(value="product.testcase.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestCases(HttpServletRequest request,@RequestParam Integer productId,@RequestParam String status, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.info("inside product.testcase.list");
		JTableResponse jTableResponse;
		Integer engagementId=0;
			try {
				//if (productId <= 0) {
				if (productId == null || ("null").equals(productId)) {
					jTableResponse = new JTableResponse("OK", null,0);
					return jTableResponse;
				}
			UserList user = (UserList) request.getSession().getAttribute("USER");
			List<TestCaseList> testCaseList=testCaseService.getTestCaseListByProductId(productId, null, null);
			Integer testCaseListforPagination=testCaseService.getTestCaseListSize(productId);
			List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
			
			if (testCaseList == null)
			{	
				log.info("inside product.testcase.list testCaseList is null");
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			for(TestCaseList tcl: testCaseList){
				JsonTestCaseList jsonTestCase =new JsonTestCaseList(tcl);
				jsonTestCaseList.add(jsonTestCase);
			}
			workflowStatusPolicyService.setInstanceIndicators(engagementId,productId,null, IDPAConstants.ENTITY_TEST_CASE_ID, jsonTestCaseList, IDPAConstants.ENTITY_TEST_CASE_ID,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
			Collections.sort(jsonTestCaseList, new Comparator<JsonTestCaseList>() {
				@Override
				public int compare(JsonTestCaseList testCase, JsonTestCaseList testCase2) {
					if(testCase.getRemainingHours() != null && testCase2.getRemainingHours() != null){
						return testCase.getRemainingHours().compareTo(testCase2.getRemainingHours());
					}else {
						return 0;
					}
				}
			});
			
			
			
            jTableResponse = new JTableResponse("OK", workflowStatusPolicyService.getPaginationListFromFullList(jsonTestCaseList, jtStartIndex, jtPageSize), testCaseListforPagination);
            testCaseList = null;
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }

	

	@RequestMapping(value="product.testcase.version.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestCasesWithProductVersionMapping(@RequestParam Integer productId) {
		log.info("inside product.testcase.list");
		JTableResponse jTableResponse;
		try {
				log.info("product Id : " + productId);
				if (productId == null || ("null").equals(productId)) {
					jTableResponse = new JTableResponse("OK", null,0);
					return jTableResponse;
				}
				
			List<JsonTestCaseList> jsonTestCasesWithProductVersionMapping = testCaseService.getTestCaseListWithProductVersionMappingByProductId(productId);
			
					
			if (jsonTestCasesWithProductVersionMapping == null) {
				
				log.info("No mappings available for versions");
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			
            jTableResponse = new JTableResponse("OK", jsonTestCasesWithProductVersionMapping,jsonTestCasesWithProductVersionMapping.size());
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }

	@RequestMapping(value="product.feature.testcase.mappedlist",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getTestCasesMappedToFeatures(@RequestParam Integer productFeatureId) {
		log.info("product.feature.testcase.mappedlist");
		JTableResponse jTableResponse;
		try {		
			if (productFeatureId == null || ("null").equals(productFeatureId)) {					
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			List<TestCaseList> testCaseListFromDB = testCaseService.getTestCasesMappedToFeature(productFeatureId);	
			if (testCaseListFromDB == null) {				
				log.info("No mappings available for Feature");
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			List<JsonTestCaseList> jsonTestCaseList = new ArrayList<JsonTestCaseList>();
			for (TestCaseList testCaseList : testCaseListFromDB) {
				jsonTestCaseList.add(new JsonTestCaseList(testCaseList, productFeatureId));
			}		
			jTableResponse = new JTableResponse("OK", jsonTestCaseList,jsonTestCaseList.size());
	        testCaseListFromDB = null;  
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="product.feature.testcase.defects.mappedlist",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody String getTestCasesMappedToFeaturesWithDefect(@RequestParam Integer productId) {
		log.info("product.feature.testcase.defects.mappedlist");
		JTableResponse jTableResponse;
		String ffinalResult="";
		try {		
			if (productId == null || ("null").equals(productId)) {					
				jTableResponse = new JTableResponse("OK", null,0);
				return "No Product Id";
			}
			List<ProductFeature> featureList = productListService.getFeatureListByProductId(productId, null, null, null);		
			List<JsonFeatureTestCaseDefect> jftcdf = testCaseService.getFeatureTestCaseDefect(productId,  featureList);			
			if (jftcdf == null) {				
				log.info("No Bugs for FeatureTestCase");
				jTableResponse = new JTableResponse("OK", null,0);
				return "No Data";
			}
		
			JSONArray ftlist = new JSONArray();
			JSONObject ffinalObj = new JSONObject();
			JSONObject pfTitle= new JSONObject();
			JSONObject pfnTitle= new JSONObject();
			JSONObject pfcodeTitle= new JSONObject();
			JSONObject tcTitle= new JSONObject();
			JSONObject tcnTitle= new JSONObject();
			JSONObject terTitle= new JSONObject();
			JSONObject defTitle= new JSONObject();			
			
			 JSONArray fcolumnData = new JSONArray();
			 JSONArray fcolumnData1 = new JSONArray();
			if(jftcdf!=null ){
				
				pfnTitle.put("title", "Feature Name");
				ftlist.add(pfnTitle);
				pfTitle.put("title", "Feature Id");
				ftlist.add(pfTitle);
				pfcodeTitle.put("title", "Feature Code");
				ftlist.add(pfcodeTitle);
				tcTitle.put("title", "TestCase Id");
				ftlist.add(tcTitle);
				tcnTitle.put("title", "TestCase Name");
				ftlist.add(tcnTitle);
				terTitle.put("title", "Test Execution Results");
				ftlist.add(terTitle);
				defTitle.put("title", "Defects");
				ftlist.add(defTitle);				
				ffinalObj.put("COLUMNS", ftlist);
			
				for (JsonFeatureTestCaseDefect jsonFeatureTestCaseDefect : jftcdf) {
					fcolumnData = new JSONArray();
					
					fcolumnData.add(jsonFeatureTestCaseDefect.getProductFeatureName());
					fcolumnData.add(jsonFeatureTestCaseDefect.getProductFeatureId());
					fcolumnData.add(jsonFeatureTestCaseDefect.getProductFeatureCode());
					fcolumnData.add(jsonFeatureTestCaseDefect.getTestcaseId());
					fcolumnData.add(jsonFeatureTestCaseDefect.getTestcaseName());
					fcolumnData.add(jsonFeatureTestCaseDefect.getTestExecutionCount());
					fcolumnData.add(jsonFeatureTestCaseDefect.getTotalDefects());
					fcolumnData1.add(fcolumnData);
					ffinalObj.put("DATA", fcolumnData1);
				}		
			}
			jTableResponse = new JTableResponse("OK", jftcdf,jftcdf.size());

			
			ffinalResult=ffinalObj.toString();
			
			jftcdf = null;
		    return "["+ffinalResult+"]";
		 
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Feature TestCase Traceability View records!");
	            log.error("JSON ERROR", e);            
	        }		
		return "["+ffinalResult+"]";
    }
	
	@RequestMapping(value="product.feature.testcase.execution.defects",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody String getTestCasesMappedToFeaturesWithDefectList(@RequestParam Integer tcId) {
		log.info("product.feature.testcase.execution.defects");
		JTableResponse jTableResponse;
		String ffinalResult="";
		try {		
			if (tcId == null || ("null").equals(tcId)) {
				jTableResponse = new JTableResponse("OK", null,0);
				return "No Product Id";
			}
			List<JsonTestExecutionResultBugList> jterbugList = testCaseService.getFeatureTestCaseDefectList(tcId);
					
			log.info("jterbugList size "+jterbugList.size());
			if (jterbugList == null) {				
				log.info("No Bugs for FeatureTestCase");
				jTableResponse = new JTableResponse("OK", null,0);
				return "No Data";
			}
		
			JSONArray ftlist = new JSONArray();
			JSONObject ffinalObj = new JSONObject();
			JSONObject bugTitle= new JSONObject();
			JSONObject bugmanagenTitle= new JSONObject();
			JSONObject bugsysIdTitle= new JSONObject();
			JSONObject fillingstatus= new JSONObject();
			JSONObject remarks= new JSONObject();
			JSONObject bugseverity= new JSONObject();
			
			JSONObject bugData= new JSONObject();
			JSONObject bugmanagenData= new JSONObject();
			JSONObject bugsysIdData= new JSONObject();
			JSONObject fillingstatusData= new JSONObject();
			JSONObject remarksData= new JSONObject();
			JSONObject bugseverityData= new JSONObject();
			
			 JSONArray fcolumnData = new JSONArray();
			 JSONArray fcolumnData1 = new JSONArray();
			if(jterbugList!=null ){
				bugTitle.put("title", "Title");
				ftlist.add(bugTitle);
				bugmanagenTitle.put("title", "Management System");
				ftlist.add(bugmanagenTitle);
				bugsysIdTitle.put("title", "System Bug Id");
				ftlist.add(bugsysIdTitle);
				fillingstatus.put("title", "Status");
				ftlist.add(fillingstatus);
				remarks.put("title", "Remarks");
				ftlist.add(remarks);
				bugseverity.put("title", "Severity");
				ftlist.add(bugseverity);				
				ffinalObj.put("COLUMNS", ftlist);
				for (JsonTestExecutionResultBugList jsonTestExecutionResultBugList : jterbugList) {
					fcolumnData = new JSONArray();
					fcolumnData.add(jsonTestExecutionResultBugList.getBugTitle());
					fcolumnData.add(jsonTestExecutionResultBugList.getBugManagementSystemName());
					fcolumnData.add(jsonTestExecutionResultBugList.getBugManagementSystemBugId());
					fcolumnData.add(jsonTestExecutionResultBugList.getBugFilingStatusName());
					fcolumnData.add(jsonTestExecutionResultBugList.getRemarks());
					fcolumnData.add(jsonTestExecutionResultBugList.getSeverityName());
					fcolumnData1.add(fcolumnData);
					ffinalObj.put("DATA", fcolumnData1);
				}
					
			}
			jTableResponse = new JTableResponse("OK", jterbugList,jterbugList.size());

			
			ffinalResult=ffinalObj.toString();
			
			jterbugList = null;
		    return "["+ffinalResult+"]";
		 
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Feature TestCase Defect View records!");
	            log.error("JSON ERROR", e);            
	        }		
		return "["+ffinalResult+"]";
    }
	
	@RequestMapping(value="product.feature.testcase.execution.result",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody String getTestCasesMappedToFeaturesWithDefectList1(@RequestParam Integer tcId) {
		log.info("product.feature.testcase.execution.result");
		JTableResponse jTableResponse;
		String ffinalResult="";
		try {		
			if (tcId == null || ("null").equals(tcId)) {
				jTableResponse = new JTableResponse("OK", null,0);
				return "No Product Id";
			}
			List<JsonWorkPackageTestCaseExecutionPlanForTester>  jsonWPTCEPTObjList = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
			HashMap<Integer,JsonWorkPackageTestCaseExecutionPlanForTester> jWPTCEPList = testCaseService.getFeatureTestCaseExecutionResultList(tcId);			
			log.info("jWPTCEPList size "+jWPTCEPList.size());
			if (jWPTCEPList == null) {				
				log.info("No TC executed for FeatureTestCase");
				jTableResponse = new JTableResponse("OK", null,0);
				return "No Data";
			}
		
			JSONArray ftlist = new JSONArray();
			JSONObject ffinalObj = new JSONObject();
			JSONObject workPackageNameTitle= new JSONObject();
			JSONObject versionIdTitle= new JSONObject();
			JSONObject verNameTitle= new JSONObject();
			JSONObject passedTitle= new JSONObject();
			JSONObject failedTitle= new JSONObject();
			JSONObject blockedTitle= new JSONObject();
			JSONObject notRunTitle= new JSONObject();
			JSONObject totalExecutedTitle= new JSONObject();
			
			JSONObject workPackageNameData= new JSONObject();
			JSONObject versionIdData= new JSONObject();
			JSONObject verNameData= new JSONObject();
			JSONObject passedData= new JSONObject();
			JSONObject failedData= new JSONObject();
			JSONObject blockedData= new JSONObject();
			JSONObject notRunData= new JSONObject();
			JSONObject totalExecutedData= new JSONObject();
			
			 JSONArray fcolumnData = new JSONArray();
			 JSONArray fcolumnData1 = new JSONArray();
			if(jWPTCEPList!=null ){
				workPackageNameTitle.put("title", "WorkPackage");
				ftlist.add(workPackageNameTitle);
				versionIdTitle.put("title", "Version Id");
				ftlist.add(versionIdTitle);
				verNameTitle.put("title", "Version Name");
				ftlist.add(verNameTitle);
				passedTitle.put("title", "Passed");
				ftlist.add(passedTitle);
				failedTitle.put("title", "Failed");
				ftlist.add(failedTitle);
				blockedTitle.put("title", "Not Run");
				ftlist.add(blockedTitle);
				notRunTitle.put("title", "Blocked");
				ftlist.add(notRunTitle);
				totalExecutedTitle.put("title", "Totally Executed");
				ftlist.add(totalExecutedTitle);
				ffinalObj.put("COLUMNS", ftlist);
				
				JsonWorkPackageTestCaseExecutionPlanForTester  jsonWPTCEPTObj = null;
				for (Map.Entry<Integer, JsonWorkPackageTestCaseExecutionPlanForTester> entry : jWPTCEPList.entrySet()) {
					
					jsonWPTCEPTObj = entry.getValue();
					fcolumnData = new JSONArray();
					fcolumnData.add(jsonWPTCEPTObj.getWorkPackageName());
					fcolumnData.add(jsonWPTCEPTObj.getProductVersionId());
					fcolumnData.add(jsonWPTCEPTObj.getProductVersionName());
					fcolumnData.add(jsonWPTCEPTObj.getP2totalPass());
					fcolumnData.add(jsonWPTCEPTObj.getP2totalFail());
					fcolumnData.add(jsonWPTCEPTObj.getP2totalNoRun());
					fcolumnData.add(jsonWPTCEPTObj.getP2totalBlock());
					fcolumnData.add(jsonWPTCEPTObj.getTotalExecutedTesCases());
					jsonWPTCEPTObjList.add(jsonWPTCEPTObj);
					fcolumnData1.add(fcolumnData);
					ffinalObj.put("DATA", fcolumnData1);
				}
			}
			jTableResponse = new JTableResponse("OK", jsonWPTCEPTObjList,jsonWPTCEPTObjList.size());
			
			ffinalResult=ffinalObj.toString();
			
			jWPTCEPList = null;
		    return "["+ffinalResult+"]";		 
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Feature TestCase Result View records!");
	            log.error("JSON ERROR", e);            
	        }		
		return "["+ffinalResult+"]";
    }
	
	@RequestMapping(value="testcase.unmappedfeatures.byProduct.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listUnMappedFeaturesOfProductId(@RequestParam int productId, @RequestParam int testCaseId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("testcase.unmappedfeatures.byProduct.list");
		JTableResponse jTableResponse;
			try {
				List<Object[]> unMappedFeatureListObj = testCaseService.getUnMappedFeatureByTestCaseId(productId, testCaseId, jtStartIndex, jtPageSize);		
				JSONArray unMappedJsonArray = new JSONArray();
				for (Object[] row : unMappedFeatureListObj) {
					JSONObject jsobj =new JSONObject();
					//log.info((Integer)row[0] +"--"+(String)row[1]);
					jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
					jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);					
					unMappedJsonArray.add(jsobj);					
				}				
				jTableResponse = new JTableResponse("OK", unMappedJsonArray,unMappedJsonArray.size());
				unMappedFeatureListObj = null;				
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching UnMappedFeatureList for Product!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="test.case.unmappedto.feature.count",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse getUnMappedTestCaseListCountOfFeatureByProductFeatureId(@RequestParam int productId, @RequestParam int productFeatureId) {
		JTableSingleResponse jTableSingleResponse;
		log.info("inside the test.case.unmappedto.feature.list");	
		int unMappedTCOfFeature = 0;
		JSONObject unMappedTCCountObj =new JSONObject();
		try {	
			unMappedTCOfFeature = productListService.getUnMappedTestCaseListCountOfFeatureByProductFeatureId(productId, productFeatureId);			
			//	String myJson = " {unMappedTCCount:"+unMappedTCOfFeature+"}";
			unMappedTCCountObj.put("unMappedTCCount", unMappedTCOfFeature);						
			jTableSingleResponse = new JTableSingleResponse("OK",unMappedTCCountObj);
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the testcase Product Feature  & Testcase association!");
	            log.error("JSON ERROR updating the testcase Product Feature", e);	 
	        }
	        
        return jTableSingleResponse;
    }	
	
	@RequestMapping(value="features.unmappedtestcase.byProduct.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listUnMappedTestCaseWithCodeOfProductId(@RequestParam int productId, @RequestParam int productFeatureId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("features.unmappedtestcase.byProduct.list");
		JTableResponse jTableResponse;
			try {	
				List<Object[]> unMappedTCListObj = productListService.getUnMappedTestCaseListByProductFeatureId(productId, productFeatureId, jtStartIndex, jtPageSize);
			
				JSONArray unMappedJsonArray = new JSONArray();
				for (Object[] row : unMappedTCListObj) {
					JSONObject jsobj =new JSONObject();
					jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
					jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);	
					jsobj.put(IDPAConstants.ITEM_CODE, (String)row[2]);	
					unMappedJsonArray.add(jsobj);					
				}				
				jTableResponse = new JTableResponse("OK", unMappedJsonArray,unMappedJsonArray.size());
				unMappedTCListObj = null;					 
			
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching UnMappedTestCaseList for Product!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }

	@RequestMapping(value="testcase.unmappeddecoupling.byProduct.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listUnMappedDecouplingCategoriesOfProductId(@RequestParam int productId, @RequestParam int testCaseId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("testcase.unmappeddecoupling.byProduct.list");
		JTableResponse jTableResponse;
			try {	
			
				TestCaseList testCase = testCaseService.getTestCaseById(testCaseId);
				Set<DecouplingCategory> listdecoupling = testCase.getDecouplingCategory();
				List<Integer> mappedDecouplingCategoryIdList = new ArrayList<Integer>();				
				for (DecouplingCategory decoup : listdecoupling) {
					mappedDecouplingCategoryIdList.add(decoup.getDecouplingCategoryId());
				}
				
				List<DecouplingCategory> decouplingList = decouplingCategoryService.getDecouplingCategoryListByProductId(productId);
						
				List<JsonDecouplingCategory> jsonDecouplingCategoryList  = new ArrayList<JsonDecouplingCategory>();
				for (DecouplingCategory decoup : decouplingList) {
					if(mappedDecouplingCategoryIdList != null){
						if(!mappedDecouplingCategoryIdList.contains(decoup.getDecouplingCategoryId())){
							jsonDecouplingCategoryList.add(new JsonDecouplingCategory(decoup));
						}else{
							//Do Nothing
						}
					}else{
						jsonDecouplingCategoryList.add(new JsonDecouplingCategory(decoup));
					}
				}
				jTableResponse = new JTableResponse("OK", jsonDecouplingCategoryList,jsonDecouplingCategoryList.size());
				decouplingList = null;					 
			
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching DecouplingList for Product!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="decoupling.unmappedtestcase.byProduct.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listUnMappedTestCasesOfProductId(@RequestParam int productId, @RequestParam int decouplingCategoryId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("decoupling.unmappedtestcase.byProduct.list");
		JTableResponse jTableResponse;
			try {			
				Set<TestCaseList> listtestcase = new HashSet<TestCaseList>();
				
				List<DecouplingCategory> decoupLingList = decouplingCategoryService.listDecouplingCategories();
				for (DecouplingCategory decouplingCategory : decoupLingList) {
					if(decouplingCategory.getTestCaseList() != null &&  !decouplingCategory.getTestCaseList().isEmpty()){
						listtestcase.addAll(decouplingCategory.getTestCaseList());						
					}
					
				}
				List<Integer> mappedTestCaseIdList = new ArrayList<Integer>();				
				for (TestCaseList testCase : listtestcase) {
					mappedTestCaseIdList.add(testCase.getTestCaseId());
				}	
				
				List<TestCaseList> testCaseList = testCaseService.getTestCaseListByProductId(productId , null, null);						
				List<JsonTestCaseList> jsonTestCaseList  = new ArrayList<JsonTestCaseList>();
				for (TestCaseList tc : testCaseList) {
					if(mappedTestCaseIdList != null){
						if(!mappedTestCaseIdList.contains(tc.getTestCaseId())){
							jsonTestCaseList.add(new JsonTestCaseList(tc));
						}else{
							//Do Nothing
						}
					}else{
						jsonTestCaseList.add(new JsonTestCaseList(tc));
					}
				}
				jTableResponse = new JTableResponse("OK", jsonTestCaseList,jsonTestCaseList.size());
				testCaseList = null;					 
			
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching TestCaseList for Product!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	@RequestMapping(value="test.case.feature.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestCaseFeatures(@RequestParam String testCaseId, @RequestParam int productId) {
		log.debug("test.case.feature.list");
		int intTmpTestCaseId = Integer.parseInt(testCaseId);
		JTableResponse jTableResponse;		
			try {
				List<Object[]> mappedTCListObj = testCaseService.getMappedFeatureBytestCaseId(productId, intTmpTestCaseId, -1, -1);					
					JSONArray mappedJsonArray = new JSONArray();					
					for (Object[] row : mappedTCListObj) {
						JSONObject jsobj =new JSONObject();
						jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
						jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);					
						mappedJsonArray.add(jsobj);					
					}				
					jTableResponse = new JTableResponse("OK", mappedJsonArray,mappedJsonArray.size());
					mappedTCListObj = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Features mapped to TestCase!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponse;
    }
	
	@RequestMapping(value="feature.unmappedto.testcase.count",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse getUnMappedFeatureCountOfFeatureByTestCaseId(@RequestParam int productId, @RequestParam int testCaseId) {
		JTableSingleResponse jTableSingleResponse;
		log.info("inside the feature.unmappedto.testcase.count");	
		
		int unMappedFeatureofTC = 0;		
		JSONObject unMappedFeatureCountObj =new JSONObject();
		try {
			unMappedFeatureofTC = testCaseService.getUnMappedFeatureCountOfTestCaseByTestCaseId(productId, testCaseId);
			unMappedFeatureCountObj.put("unMappedTCCount", unMappedFeatureofTC);						
			jTableSingleResponse = new JTableSingleResponse("OK",unMappedFeatureCountObj);
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to fetch unmappedFeature count to testcase!");
	            log.error("JSON ERROR fetching unmappedFeature count to testcase", e);	 
	        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="test.case.decoupling.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestCaseDecouplingCategories(@RequestParam String testCaseId) {
		log.debug("test.case.decoupling.list");
		int intTmpTestCaseId = Integer.parseInt(testCaseId);
		JTableResponse jTableResponse;		
			try {
				TestCaseList testCase = testCaseService.getTestCaseById(intTmpTestCaseId);
				List<JsonDecouplingCategory> jsonDecouplingCategory = new ArrayList<JsonDecouplingCategory>();
				if(testCase.getDecouplingCategory() != null){
					Set<DecouplingCategory> listdecoupling = testCase.getDecouplingCategory();
					
				
					for (DecouplingCategory decoup : listdecoupling) {
						jsonDecouplingCategory.add(new JsonDecouplingCategory(decoup));
					}	
				}
				
				 jTableResponse = new JTableResponse("OK", jsonDecouplingCategory,jsonDecouplingCategory.size());
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Decouplings mapped to TestCase!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponse;
    }
	
	@RequestMapping(value="decoupling.test.case.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listDecouplingCategoryTestCase(@RequestParam String decouplingCategoryId) {
		log.debug("decoupling.test.case.list");
		int intTmpDecouplingId = Integer.parseInt(decouplingCategoryId);
		JTableResponse jTableResponse;		
			try {
				DecouplingCategory decoupling = decouplingCategoryService.getDecouplingCategoryById(intTmpDecouplingId);
				
				List<JsonTestCaseList> jsonTestCaseList = new ArrayList<JsonTestCaseList>();
				if(decoupling.getTestCaseList() != null){
					Set<TestCaseList> listtestCases = decoupling.getTestCaseList();
					
				
					for (TestCaseList tc : listtestCases) {
						jsonTestCaseList.add(new JsonTestCaseList(tc));
					}	
				}
				
				 jTableResponse = new JTableResponse("OK", jsonTestCaseList,jsonTestCaseList.size());
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching TestCases mapped to Decouplings!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponse;
    }
	
	
	@RequestMapping(value="feature.test.case.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listFeatureTestCaseswithcode(@RequestParam String productFeatureId, @RequestParam int productId) {
		log.debug("feature.test.case.list.withcode");
		int intTmpproductFeatureId = Integer.parseInt(productFeatureId);
		JTableResponse jTableResponse;		
			try {	
					List<Object[]> mappedTCListObj = testCaseService.getMappedTestCaseListByProductFeatureId(productId, intTmpproductFeatureId, -1, -1);
					JSONArray mappedJsonArray = new JSONArray();
					for (Object[] row : mappedTCListObj) {
						JSONObject jsobj =new JSONObject();
						jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
						jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);		
						jsobj.put(IDPAConstants.ITEM_CODE, (String)row[2]);	
						mappedJsonArray.add(jsobj);					
					}				
					jTableResponse = new JTableResponse("OK", mappedJsonArray,mappedJsonArray.size());
					mappedTCListObj = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching TestCases mapped to Feature!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponse;
    }
	
	@RequestMapping(value="test.case.feature.mapping",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addFeatureToTestCase(@RequestParam Integer productFeatureId,@RequestParam Integer testcaseId, @RequestParam String maporunmap) {
		JTableSingleResponse jTableSingleResponse;
		log.info("inside the test.case.feature.mapping");		
		try {
				ProductFeature feature = productListService.getByProductFeatureId(productFeatureId);				
				TestCaseList testCaseList = testSuiteConfigurationService.getByTestCaseId(testcaseId);
				TestCaseList testCase = productListService.updateProductFeatureTestCasesOneToMany(testCaseList.getTestCaseId(), feature.getProductFeatureId(), maporunmap);
				if(maporunmap.equalsIgnoreCase("map")){
					mongoDBService.addFeatureTestCaseMappingToMongoDB(feature, testCaseList);
				}else if(maporunmap.equalsIgnoreCase("unmap")){
					mongoDBService.removeFeatureTestCaseMappingFromMongoDB(feature.getProductFeatureId(), testCaseList.getTestCaseId());
				}
				List<JsonTestCaseList> jsonTestCaseListToUI=new ArrayList<JsonTestCaseList>();
				if(testCase != null){
					jsonTestCaseListToUI.add(new JsonTestCaseList(testCase));
				}				
				jTableSingleResponse = new JTableSingleResponse("OK",jsonTestCaseListToUI);
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the testcase Product Feature  & Testcase association!");
	            log.error("JSON ERROR updating testcase Product Feature", e);	 
	        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="test.case.decoupling.mapping",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addDecouplingToTestCase(@RequestParam Integer decouplingCategoryId,@RequestParam Integer testcaseId, @RequestParam String maporunmap) {
		JTableSingleResponse jTableSingleResponse;
		log.info("inside the test.case.decoupling.mapping");		
		try {
				DecouplingCategory decoupling = decouplingCategoryService.getDecouplingCategoryById(decouplingCategoryId);				
				TestCaseList testCaseList = testSuiteConfigurationService.getByTestCaseId(testcaseId);
			
				TestCaseList testCase = decouplingCategoryService.updateDecouplingCategoriesTestCasesOneToMany(testCaseList.getTestCaseId(), decoupling.getDecouplingCategoryId(), maporunmap);
				
				List<JsonTestCaseList> jsonTestCaseListToUI=new ArrayList<JsonTestCaseList>();
				if(testCase != null){
					jsonTestCaseListToUI.add(new JsonTestCaseList(testCase));
				}				
				jTableSingleResponse = new JTableSingleResponse("OK",jsonTestCaseListToUI);
				
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the testcase Product Feature  & Testcase association!");
	            log.error("JSON ERROR updating testcase Product Feature", e);	 
	        }
        return jTableSingleResponse;
    }
	
	
	@RequestMapping(value="product.testcase.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestCase( HttpServletRequest request, @ModelAttribute JsonTestCaseList jsonTestCaseList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		TestCaseList similarTestCaseFromDB = null;
		log.info("product.testcase.add");
		if(result.hasErrors()){			
			jTableSingleResponse =jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {		
			UserList user = (UserList)request.getSession().getAttribute("USER");
				TestCaseList testCaseListFromUI = jsonTestCaseList.getTestCaseList();				
				String errorMessage = ValidationUtility.validateForNewTestCaseAddition(testCaseListFromUI, testCaseService, testSuiteConfigurationService, testCaseListFromUI.getProductMaster().getProductId());				
				
				if (errorMessage != null) {					
					jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
					return jTableSingleResponse;
				}	
				//Check for special characters validation
				String tcName = testCaseListFromUI.getTestCaseName().replaceAll("[^A-Za-z0-9]", "");
				similarTestCaseFromDB = testCaseService.getSimilarTestCasesByName(tcName, testCaseListFromUI.getProductMaster().getProductId());
				if(similarTestCaseFromDB != null){
					jTableSingleResponse = new JTableSingleResponse("ERROR","Similar testcase name exists in the product.");
					return jTableSingleResponse;
				}
				if(jsonTestCaseList.getExecutionTypeId() != null){
					ExecutionTypeMaster executionTypeMasterFromUI = executionTypeMasterService.getExecutionTypeByExecutionTypeId(jsonTestCaseList.getExecutionTypeId());
					testCaseListFromUI.setExecutionTypeMaster(executionTypeMasterFromUI);
				}
				if(jsonTestCaseList.getTestcasePriorityId() != null){
					TestCasePriority TestCasePriorityFromUI = testCaseService.getTestCasePriorityBytestcasePriorityId(jsonTestCaseList.getTestcasePriorityId());
					testCaseListFromUI.setTestCasePriority(TestCasePriorityFromUI);
				}
				if(jsonTestCaseList.getTestcaseTypeId() != null){
					TestcaseTypeMaster testcaseTypeMasterFromUI = testCaseService.getTestcaseTypeMasterBytestcaseTypeId(jsonTestCaseList.getTestcaseTypeId());
					testCaseListFromUI.setTestcaseTypeMaster(testcaseTypeMasterFromUI);					
				}
				int productFeatureId = 0;
				if(jsonTestCaseList.getProductFeatureId() != null && jsonTestCaseList.getProductFeatureId() != 0){
				//Since TestCase is going to be added, it can contain only one Feature mapped. Hence adding directly
					productFeatureId = jsonTestCaseList.getProductFeatureId();
					ProductFeature productFeature = productListService.getByProductFeatureId(productFeatureId);
					Set<ProductFeature> featureSet = new HashSet<ProductFeature>();
					featureSet.add(productFeature);
					testCaseListFromUI.setProductFeature(featureSet);					
				}				
				int decouplingCategoryId = 0;
				if(jsonTestCaseList.getDecouplingCategoryId() != null && jsonTestCaseList.getDecouplingCategoryId() != 0){
					//Since TestCase is going to be added, it can contain only one Decoupling category mapped. Hence adding directly
					decouplingCategoryId = jsonTestCaseList.getDecouplingCategoryId();
					DecouplingCategory decoupling = decouplingCategoryService.getDecouplingCategoryById(decouplingCategoryId);
					Set<DecouplingCategory> decouplingSet = new HashSet<DecouplingCategory>();
					decouplingSet.add(decoupling);
					testCaseListFromUI.setDecouplingCategory(decouplingSet);
				}
				testCaseListFromUI.setStatus(1);
				
				WorkflowStatus workflowStatus = null;
				if(jsonTestCaseList.getWorkflowStatusId() ==  null || jsonTestCaseList.getWorkflowStatusId() == 0){
					workflowStatus = workflowStatusService.getInitialStatusForInstanceByWorkflowId(testCaseListFromUI.getProductMaster().getProductId(), IDPAConstants.ENTITY_TEST_CASE_ID, null, jsonTestCaseList.getWorkflowId());
				}else{
					workflowStatus = workflowStatusService.getWorkflowStatusById(jsonTestCaseList.getWorkflowStatusId());
				}/*
				
				if(workflowStatus == null || workflowStatus.getWorkflowStatusId() == null || workflowStatus.getWorkflowStatusId() == 0){
					jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to find status for test case. Please check workflow mapped or mapped workflow has status");
					return jTableSingleResponse;
				}*/

				if(workflowStatus == null) {
					workflowStatus= new WorkflowStatus();
					workflowStatus.setWorkflowStatusId(-1);
					testCaseListFromUI.setWorkflowStatus(workflowStatus);
				} 
				
				
				int testCaseId = testCaseService.addTestCase(testCaseListFromUI);
				log.info("Testcase Id - "+testCaseId);
				if(testCaseListFromUI.getTestCaseExecutionOrder() == null || testCaseListFromUI.getTestCaseExecutionOrder() == 0){
					testCaseListFromUI.setTestCaseExecutionOrder(testCaseId);
					testCaseService.update(testCaseListFromUI);
					log.info("Testcase Updated "+testCaseListFromUI.getTestCaseExecutionOrder());
				}
				if(testCaseId > 0){
					workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(testCaseListFromUI.getProductMaster().getProductId(), IDPAConstants.ENTITY_TEST_CASE_ID, null, testCaseId, jsonTestCaseList.getWorkflowId(), workflowStatus.getWorkflowStatusId(), user, null, testCaseListFromUI);
				}
				
				
				String comments="";
				Integer primaryStatusId=workflowStatus.getWorkflowStatusId();
				String approveAllEntityInstanceIds=String.valueOf(testCaseId);
				Integer productId=testCaseListFromUI.getProductMaster().getProductId();
				
				mongoDBService.addProductTestCaseToMongoDB(testCaseListFromUI.getTestCaseId());				

				int userId=user.getUserId();
				UserList userObj = userListService.getUserListById(userId);
				//Entity Audition History //Addition
				EntityMaster entityMaster = workPackageService.getEntityMasterByName(IDPAConstants.ENTITY_TEST_CASE);
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_TEST_CASE, testCaseListFromUI.getTestCaseId(), testCaseListFromUI.getTestCaseName(), userObj);
			
				if(jsonTestCaseList.getProductFeatureId()!= 0){
					productListService.updateProductFeatureTestCasesOneToMany(testCaseId,jsonTestCaseList.getProductFeatureId(), "map");
				}	
				if(decouplingCategoryId != 0){					
					decouplingCategoryService.updateDecouplingCategoriesTestCasesOneToMany(testCaseId, decouplingCategoryId, "map");
				}
				
	            jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestCaseList(testCaseListFromUI));
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new TestCase!");
	            log.error("JSON ERROR adding new TestCase", e);
	        }	        
        return jTableSingleResponse;		
    }
	

	@RequestMapping(value="product.testcase.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateTestCase(HttpServletRequest request, @ModelAttribute JsonTestCaseList jsonTestCaseList, BindingResult result) {
		JTableResponse jTableResponse = null;
		String remarks = "";
		ProductMaster productMaster = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		
		try {
	
		String deleteStatus = "";
		TestCaseList testCaseListFromDB = testCaseService.getTestCaseById(jsonTestCaseList.getTestCaseId());
		TestCaseList testCaseListFromUI = jsonTestCaseList.getTestCaseList();
		productMaster = testCaseListFromDB.getProductMaster();
		Set<TestCaseList> testCases= new HashSet<TestCaseList>();
		String tc_update = "yes";
		//Check Test case list from database is null? //Check if TC Status was disabled, by comparing Status from UI and Database.
		if(testCaseListFromDB != null && testCaseListFromDB.getStatus() != null && testCaseListFromDB.getStatus()  == 1 && jsonTestCaseList.getStatus()==0){
			
			//Check if TC was mapped to WorkPackage
			Integer wpPlanTCId = workPackageService.getWorkPackageTestCaseOfTCID(jsonTestCaseList.getTestCaseId());
			if(wpPlanTCId == null || wpPlanTCId == 0){
				
				testCaseService.deleteTCWithMappings(testCaseListFromDB);
				deleteStatus = "Deleted TC";
				tc_update = "no";
				
			}else{
				deleteStatus = "TC is mapped, so only disabled and not deleted.";		
			}
		}
		
		if(tc_update.equalsIgnoreCase("yes")){
			//Made script package editable
			if(jsonTestCaseList.getTestCaseScriptQualifiedName() == null || jsonTestCaseList.getTestCaseScriptQualifiedName().isEmpty()){
			} else {
				testCaseListFromUI.setTestCaseScriptQualifiedName(jsonTestCaseList.getTestCaseScriptQualifiedName().trim());				
			}
			
			if(jsonTestCaseList.getExecutionTypeId() != null){
				ExecutionTypeMaster executionTypeMasterFromUI = executionTypeMasterService.getExecutionTypeByExecutionTypeId(jsonTestCaseList.getExecutionTypeId());
				testCaseListFromUI.setExecutionTypeMaster(executionTypeMasterFromUI);
			}
			if(jsonTestCaseList.getTestcasePriorityId() != null){
				TestCasePriority TestCasePriorityFromUI = testCaseService.getTestCasePriorityBytestcasePriorityId(jsonTestCaseList.getTestcasePriorityId());
				testCaseListFromUI.setTestCasePriority(TestCasePriorityFromUI);
			}
			if(jsonTestCaseList.getTestcaseTypeId() != null){
				TestcaseTypeMaster testcaseTypeMasterFromUI = testCaseService.getTestcaseTypeMasterBytestcaseTypeId(jsonTestCaseList.getTestcaseTypeId());
				testCaseListFromUI.setTestcaseTypeMaster(testcaseTypeMasterFromUI);					
			}
			
			String errorMessage = "";
			if((!testCaseListFromUI.getTestCaseName().equalsIgnoreCase(testCaseListFromDB.getTestCaseName())) 
					|| (!testCaseListFromUI.getTestCaseCode().equalsIgnoreCase(testCaseListFromDB.getTestCaseCode())) )
			{
				
				errorMessage = ValidationUtility.validateForNewTestCaseUpdate(testCaseListFromUI, testCaseService, testSuiteConfigurationService, testCaseListFromUI.getProductMaster().getProductId());				
				
				if (errorMessage != null) {					
					jTableResponse = new JTableResponse("ERROR",errorMessage);
					return jTableResponse;
				}
		//		errorMessage = ValidationUtility.validateForNewTestCaseStepUpdate(testCaseStepsListFromUI, testCaseService, testSuiteConfigurationService, testCaseStepsListFromUI.getTestCaseList().getTestCaseId());					
			}	
			ProductFeature productFeature =null;
			int productFeatureId = 0;
			if(jsonTestCaseList.getProductFeatureId() != null){
				productFeatureId = jsonTestCaseList.getProductFeatureId();
				productFeature = productListService.getByProductFeatureId(jsonTestCaseList.getProductFeatureId());
				
				if(testCaseListFromUI.getProductFeature() != null){
					Set<ProductFeature> featureSet = testCaseListFromUI.getProductFeature();
					if(!featureSet.contains(productFeature)){
						featureSet.add(productFeature);
						testCaseListFromUI.setProductFeature(featureSet);
					}	
				}					
			}			
			
			int decouplingCategoryId = 0;
			if(jsonTestCaseList.getDecouplingCategoryId() != null){
				decouplingCategoryId = jsonTestCaseList.getDecouplingCategoryId();
				DecouplingCategory decoupling = decouplingCategoryService.getDecouplingCategoryById(jsonTestCaseList.getDecouplingCategoryId());
				if(testCaseListFromUI.getDecouplingCategory() != null){
					Set<DecouplingCategory> decouplingSet = testCaseListFromUI.getDecouplingCategory();
					if(!decouplingSet.contains(decoupling)){
						decouplingSet.add(decoupling);
						testCaseListFromUI.setDecouplingCategory(decouplingSet);
					}						
				}					
			}
			
			try{
				int x = Integer.parseInt(testCaseListFromUI.getTestCaseExecutionOrder().toString());
			} catch(NumberFormatException ex) {
				log.info("Error in updating test case details because Test Case Execution Order is provided with character combination");
				log.error("Error in updating test case details because Test Case Execution Order is provided with character combination", ex);
				jTableResponse = new JTableResponse("ERROR","Error updating TestCase!");
				return jTableResponse;
			}
			
			if(testCaseListFromUI.getTestCaseExecutionOrder() <= 0){
				log.info("Error in updating test case details because Test Case Execution Order is provided with negative number combination");
				log.error("Error in updating test case details because Test Case Execution Order is provided with negative number combination");
				jTableResponse = new JTableResponse("ERROR","Error updating TestCase!");
				return jTableResponse;
			}
			
			Set<TestSuiteList> testSuiteList = testCaseListFromUI.getTestSuiteLists();
			if(testCaseListFromUI != null && !testSuiteList.isEmpty()){
				for(TestSuiteList ts : testSuiteList){
					TestSuiteList tsl = ts;
					tsl = testSuiteConfigurationService.getByTestSuiteId(tsl.getTestSuiteId());
					testSuiteList.clear();
					testSuiteList.add(tsl);
					testCaseListFromUI.getTestSuiteLists().clear();
					testCaseListFromUI.setTestSuiteLists(testSuiteList);			
				}
			}
			if(productFeature != null){
				testCases.addAll(productFeature.getTestCaseList());	
			}
			
			testCaseService.update(testCaseListFromUI);
			//testCases.add(testCaseListFromUI);
			if(productFeature != null){
				productFeature.setTestCaseList(testCases);
				productListService.updateProductFeature(productFeature);
			}
			
			if(jsonTestCaseList.getModifiedField().equalsIgnoreCase(MongodbConstants.MODIFIED_FIELD_STATUS)){
				mongoDBService.updateParentStatusInChildColletions(IDPAConstants.ENTITY_TEST_CASE_ID,jsonTestCaseList.getTestCaseId(),jsonTestCaseList.getStatus());
			}
			
			/* Update the testcase in to MongoDb*/
			if(testCaseListFromUI!=null){
				if(testCaseListFromUI.getTestCaseId()!=null){
					log.info("Updating the testcase in to MongoDb");
					mongoDBService.addProductTestCaseToMongoDB(testCaseListFromUI.getTestCaseId());					
					//Entity Audition History //Update
					UserList user = (UserList)request.getSession().getAttribute("USER");
					remarks = "Product :"+productMaster.getProductName()+", TestCase :"+testCaseListFromUI.getTestCaseName();
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_TEST_CASE,  testCaseListFromUI.getTestCaseId(), testCaseListFromUI.getTestCaseName(),
							jsonTestCaseList.getModifiedField(), jsonTestCaseList.getModifiedFieldTitle(),
							jsonTestCaseList.getOldFieldValue(), jsonTestCaseList.getModifiedFieldValue(), user, remarks);
				}
			}
			
			/*if(productFeatureId != 0){
				productListService.updateProductFeatureTestCasesOneToMany(jsonTestCaseList.getTestCaseId(),productFeatureId, "map");
			}*/
			if(decouplingCategoryId != 0){					
				decouplingCategoryService.updateDecouplingCategoriesTestCasesOneToMany(jsonTestCaseList.getTestCaseId(), decouplingCategoryId, "map");
			}
			if (testCaseListFromUI.getTestCaePredecessors().matches("[0-9, ]+") || testCaseListFromUI.getTestCaePredecessors().isEmpty()) {
				testCaseService.updateTestCasePredecessors(testCaseListFromUI, testCaseListFromUI.getTestCaePredecessors());
			}else {
				jTableResponse = new JTableResponse("ERROR","Invalid Predeccesors Input"); 
				return jTableResponse;
			}
			
			List<JsonTestCaseList> jsonTestCaseListTemp =new ArrayList<JsonTestCaseList>();
			jsonTestCaseListTemp.add(jsonTestCaseList);
            jTableResponse = new JTableResponse("OK",jsonTestCaseListTemp ,1);		
            
		}else if(tc_update.equalsIgnoreCase("no")){
			List<JsonTestCaseList> jsonTestCaseListTemp =new ArrayList<JsonTestCaseList>();
			jsonTestCaseListTemp.add(jsonTestCaseList);
			jTableResponse = new JTableResponse("OK", deleteStatus);
		}
					
				
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating TestCase!");
	            log.error("JSON ERROR updating TestCase", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="testcase.teststep.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestSuiteCasesSteps(@RequestParam String testCaseId) {
		log.debug("inside testcase.teststep.list");
		
		int intTmpTestCaseId=Integer.parseInt(testCaseId);
		JTableResponse jTableResponse;
			try {
			
				List<TestCaseStepsList> listTestCaseSteps=testCaseService.listTestCaseSteps(intTmpTestCaseId);
				
				List<JsonTestCaseStepsList> jsonTestCaseStepsLists=new ArrayList<JsonTestCaseStepsList>();
				JsonTestCaseStepsList jsonTestCaseStepsList = new JsonTestCaseStepsList();
				for(TestCaseStepsList testCaseStepsList: listTestCaseSteps){
					jsonTestCaseStepsList = new JsonTestCaseStepsList(testCaseStepsList);
					
					if(("null").equals(jsonTestCaseStepsList.getTestStepInput()))
						jsonTestCaseStepsList.setTestStepInput("NA");
					jsonTestCaseStepsLists.add(jsonTestCaseStepsList);
					
				}
				jTableResponse = new JTableResponse("OK", jsonTestCaseStepsLists,testCaseService.getTotalRecordsOfTestCaseSteps());
		           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="testcase.teststep.list.by.status",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestStepsbyStatus(@RequestParam String testCaseId,@RequestParam Integer status) {
		log.debug("inside testcase.teststep.list");
		
		int intTmpTestCaseId=Integer.parseInt(testCaseId);
		JTableResponse jTableResponse;
			try {
			
				List<TestCaseStepsList> listTestCaseSteps=testCaseService.listTestCaseStepsByStatus(intTmpTestCaseId,status);
				
				List<JsonTestCaseStepsList> jsonTestCaseStepsLists=new ArrayList<JsonTestCaseStepsList>();
				JsonTestCaseStepsList jsonTestCaseStepsList = new JsonTestCaseStepsList();
				for(TestCaseStepsList testCaseStepsList: listTestCaseSteps){
					jsonTestCaseStepsList = new JsonTestCaseStepsList(testCaseStepsList);
					
					if(("null").equals(jsonTestCaseStepsList.getTestStepInput()))
						jsonTestCaseStepsList.setTestStepInput("NA");
					jsonTestCaseStepsLists.add(jsonTestCaseStepsList);
					
				}
				jTableResponse = new JTableResponse("OK", jsonTestCaseStepsLists,testCaseService.getTotalRecordsOfTestCaseStepsByTestCaseId(intTmpTestCaseId));
		           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="testcase.teststep.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestCaseSteps(HttpServletRequest request, @ModelAttribute JsonTestCaseStepsList jsonTestCaseStepsList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		log.info("product.testcase.add");
		UserList user = (UserList)request.getSession().getAttribute("USER");
		int  userId = user.getUserId();
		if(result.hasErrors()){			
			jTableSingleResponse =jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {	
				TestCaseStepsList testCaseStepsListFromUI = jsonTestCaseStepsList.getTestCaseList();
				String errorMessage = ValidationUtility.validateForNewTestCaseStepAddition(testCaseStepsListFromUI, testCaseService, testSuiteConfigurationService, testCaseStepsListFromUI.getTestCaseList().getTestCaseId());
				if (errorMessage != null) {					
					jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
					return jTableSingleResponse;
				}
				testSuiteConfigurationService.addTestCaseStep(testCaseStepsListFromUI);
				TestCaseList tc = testCaseService.getTestCaseById(jsonTestCaseStepsList.getTestCaseId());
				
				/* Add teststep into mongoDB*/
				if(testCaseStepsListFromUI!=null){
					if(testCaseStepsListFromUI.getTestStepId()!=null){
						mongoDBService.addTestStepsToMongoDB(testCaseStepsListFromUI.getTestStepId());						
						
						UserList userObj = userListService.getUserListById(userId);
						//Entity Audition History //Addition
						EntityMaster entityMaster = workPackageService.getEntityMasterByName(IDPAConstants.ENTITY_TEST_CASE_STEPS);
						eventsService.addNewEntityEvent(IDPAConstants.ENTITY_TEST_CASE_STEPS, testCaseStepsListFromUI.getTestStepId(), testCaseStepsListFromUI.getTestStepName(), userObj);
					}
				}
				
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestCaseStepsList(testCaseStepsListFromUI));			
				
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new TestCase Steps!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableSingleResponse;		
    }
	
	
	@RequestMapping(value="testcase.teststep.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateTestCaseSteps(HttpServletRequest request, @ModelAttribute JsonTestCaseStepsList jsonTestCaseStepsList, BindingResult result) {
		JTableResponse jTableResponse;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}		
		try {
				TestCaseStepsList testCaseStepsListFromDB = testSuiteConfigurationService.getByTestStepId(jsonTestCaseStepsList.getTestStepId());
				TestCaseStepsList testCaseStepsListFromUI = jsonTestCaseStepsList.getTestCaseList();
				String errorMessage = "";
				if((!testCaseStepsListFromUI.getTestStepName().equalsIgnoreCase(testCaseStepsListFromDB.getTestStepName())) 
						|| (!testCaseStepsListFromUI.getTestStepCode().equalsIgnoreCase(testCaseStepsListFromDB.getTestStepCode())) )
				{
					errorMessage = ValidationUtility.validateForNewTestCaseStepUpdate(testCaseStepsListFromUI, testCaseService, testSuiteConfigurationService, testCaseStepsListFromUI.getTestCaseList().getTestCaseId());
					if (errorMessage != null) {					
						jTableResponse = new JTableResponse("ERROR",errorMessage);
						return jTableResponse;
					}
				}				
				
				testSuiteConfigurationService.updateTestCaseSteps(testCaseStepsListFromUI);
				
				/* Update teststep into mongoDB*/
				if(testCaseStepsListFromUI!=null){
					if(testCaseStepsListFromUI.getTestStepId()!=null){
						mongoDBService.addTestStepsToMongoDB(testCaseStepsListFromUI.getTestStepId());						
						//Entity Audition History //Update
						UserList user = (UserList)request.getSession().getAttribute("USER");
						remarks = "TestCase :"+testCaseStepsListFromUI.getTestCaseList().getTestCaseName()+", TestStep :"+testCaseStepsListFromUI.getTestStepName();
						eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_TEST_CASE_STEPS,  testCaseStepsListFromUI.getTestStepId(), testCaseStepsListFromUI.getTestStepName(),
								jsonTestCaseStepsList.getModifiedField(), jsonTestCaseStepsList.getModifiedFieldTitle(),
								jsonTestCaseStepsList.getOldFieldValue(), jsonTestCaseStepsList.getModifiedFieldValue(), user, remarks);
					}
				}
				List<JsonTestCaseStepsList> jsonTestCaseStepsListTemp =new ArrayList<JsonTestCaseStepsList>();
				
				jsonTestCaseStepsListTemp.add(jsonTestCaseStepsList);
	            jTableResponse = new JTableResponse("OK",jsonTestCaseStepsListTemp ,1);
				
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating TestCase Steps!");
	            log.error("JSON ERROR updating TestCase Steps", e);
	        }	        
        return jTableResponse;
    }	
	
	@RequestMapping(value="testcase.import", method=RequestMethod.POST ,produces="text/plain" )
	public @ResponseBody JTableResponse testDataimport(HttpServletRequest request,@RequestParam Integer productId) {
		log.debug("testcase.import");
		JTableResponse jTableResponse;
		try {
			
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile multipartFile = null;
			String fileName = "";
			InputStream is=null;
			Iterator<String> iterator = multipartRequest.getFileNames();
			File fileForProcess = null;
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileName = multipartFile.getOriginalFilename();
				is=multipartFile.getInputStream();
			}
			
			String isImportComplete = excelTestDataIntegrator.importTestCases(fileName, productId, is);
			
			if(isImportComplete != null){
				
				jTableResponse = new JTableResponse("Ok","Import testCases Completed."+" \n "+isImportComplete);
			} else{
				
				jTableResponse = new JTableResponse("Ok","Import completed"+"\n"+isImportComplete);
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error Importing TestData");
			log.error("JSON ERROR Importing TestData", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="teststeps.import", method=RequestMethod.POST ,produces="text/plain" )
	public @ResponseBody JTableResponse testStepsExcelImport(HttpServletRequest request,@RequestParam Integer productId) {
		log.debug("teststeps.import");
		JTableResponse jTableResponse;
		try {
			log.info("Product Id for importing test steps :" + productId);
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile multipartFile = null;
			String fileName = "";
			InputStream is=null;
			Iterator<String> iterator = multipartRequest.getFileNames();
			File fileForProcess = null;
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileName = multipartFile.getOriginalFilename();
				is=multipartFile.getInputStream();
			}			
			String isImportComplete = " ";
			isImportComplete = excelTestDataIntegrator.importTestStepsFromExcel(fileName, productId, is);
			if(isImportComplete != null){
				log.info("Import test Steps Completed.");
				jTableResponse = new JTableResponse("Ok","Import testSteps Completed."+"\n "+isImportComplete);
			} else{
				log.info("Import completed");
				jTableResponse = new JTableResponse("Ok","Import completed"+"\n"+isImportComplete);
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in Test Steps Import");
			log.error("JSON ERROR Importing Test Steps", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="testcase.to.productversion.mapping.update.inline",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse updateTestCaseToProductVersionMapping(@RequestParam Integer testCaseId,@RequestParam String modifiedFieldValue, @RequestParam String modifiedField) {
		JTableSingleResponse jTableSingleResponse;
		log.info("inside the testcase.to.productversion.mapping.update.inline");		
		try {				
				testSuiteConfigurationService.updateTestCasetoProductVersionMapping(testCaseId, modifiedField, modifiedFieldValue);
				jTableSingleResponse = new JTableSingleResponse("OK","Mapping Updated for Product Version " + modifiedFieldValue);
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating Mapping for Product Version " + modifiedFieldValue);
	            log.error("JSON ERROR", e);	 
	        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="testcase.to.productversion.delete.bulk",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteTestCaseFromProductVersion(@RequestParam Integer testcaseId,@RequestParam Integer versionId) {
		JTableResponse jTableResponse;
		log.info("testcase.to.productversion.delete.bulk--"+testcaseId+">>>>>>>>>>>>"+versionId);
		try {
			TestCaseList testCaseList = testSuiteConfigurationService.getByTestCaseId(testcaseId);
			ProductVersionListMaster prodVersionList = productListService.getProductVersionListMasterById(versionId);			
					
			Set<TestCaseList> testCaseListSetofVersion=prodVersionList.getTestCaseLists();			
			Set<TestCaseList> testCaseListSetNew=new HashSet<TestCaseList>();
			
			for (TestCaseList tcObj : testCaseListSetofVersion) {
				if(!tcObj.getTestCaseId().equals(testCaseList.getTestCaseId())){
					testCaseListSetNew.add(tcObj);
				}
			}

			log.info("New Mapped TC list size--"+testCaseListSetNew);
			prodVersionList.setTestCaseLists(testCaseListSetNew);
			productListService.updateProductVersion(prodVersionList);			
			
			log.debug("Removed testcase from ProductVersion successfully");
				
			jTableResponse = new JTableResponse("OK");
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error Removing testcase from ProductVersion record!");
            log.error("JSON ERROR Removing testcase from ProductVersion", e);
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="testcase.version.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestCasestoProductVersion(@RequestParam String versionId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("testcase.version.list");
		int intTmpVersionId=Integer.parseInt(versionId);
		JTableResponse jTableResponse;
			try {
				
				List<TestCaseList> testCaseList = testCaseService.getTestCaseByProductVersionId(jtStartIndex, jtPageSize, intTmpVersionId);
							
				
				List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
					for(TestCaseList tc: testCaseList){
						jsonTestCaseList.add(new JsonTestCaseList(tc));
						
					}
			    jTableResponse = new JTableResponse("OK", jsonTestCaseList,jsonTestCaseList.size());
		           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching TestCase of Product Version records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="show.testcase.execution.details",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getTestCasesExecutionDetails(@RequestParam Integer tcId) {
		log.info("product.feature.testcase.execution.result");
		JTableResponse jTableResponse;
		try {		
			if (tcId == null || ("null").equals(tcId)) {
				return jTableResponse = new JTableResponse("OK", null,0);
			}
			List<JsonWorkPackageTestCaseExecutionPlanForTester>  jsonWPTCEPTObjList = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
			HashMap<Integer,JsonWorkPackageTestCaseExecutionPlanForTester> jWPTCEPList = testCaseService.getFeatureTestCaseExecutionResultList(tcId);			
					
			log.info("WorkPackage TC Executions - "+jWPTCEPList.size());
			if (jWPTCEPList == null) {			
				log.info("No TC executed for FeatureTestCase");
				return jTableResponse = new JTableResponse("OK", null,0);
			}
		
			if(jWPTCEPList!=null ){
				
				JsonWorkPackageTestCaseExecutionPlanForTester  jsonWPTCEPTObj = null;
				for (Map.Entry<Integer, JsonWorkPackageTestCaseExecutionPlanForTester> entry : jWPTCEPList.entrySet()) {
					jsonWPTCEPTObj = entry.getValue();
					jsonWPTCEPTObjList.add(jsonWPTCEPTObj);
				}
			}
			jTableResponse = new JTableResponse("OK", jsonWPTCEPTObjList,jsonWPTCEPTObjList.size());
			jWPTCEPList = null;
		    return jTableResponse;		 
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Feature TestCase Result View records!");
	            log.error("JSON ERROR", e);            
	    }		
		return jTableResponse;
    }
	
	@RequestMapping(value="result.testcase.execution.summary",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getTestCasesExecutionSummary(@RequestParam Integer tcId, @RequestParam Integer workPackageId, @RequestParam Integer productId, @RequestParam String dataLevel) {
		log.info("result.testcase.execution.summary");
		JTableResponse jTableResponse = null;
		try {		
			if (tcId == null || ("null").equals(tcId)) {
				return jTableResponse = new JTableResponse("OK", null,0);
			}	
				
			List<JsonWorkPackageTestCaseExecutionResultSummary>  jsonWPTCEPTObjList = testCaseService.getTestCaseExecutionResultSummary(tcId, workPackageId, productId, dataLevel);			
		
			if (jsonWPTCEPTObjList == null) {			
				log.info("No TC executed for WorkPackage");
				return jTableResponse = new JTableResponse("OK", null,0);
			}
		
			if(jsonWPTCEPTObjList!=null ){				
				jTableResponse = new JTableResponse("OK", jsonWPTCEPTObjList,jsonWPTCEPTObjList.size());
			}			
			jsonWPTCEPTObjList = null;
		    return jTableResponse;		 
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching TestCase Result View records!");
	            log.error("JSON ERROR", e);            
	    }		
		return jTableResponse;
    }
	
	@RequestMapping(value="show.testcase.defect.details",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getTestCasesSDefectsDetails(@RequestParam Integer tcId) {
		log.info("product.feature.testcase.execution.defects");
		JTableResponse jTableResponse;
		String ffinalResult="";
		try {		
			if (tcId == null || ("null").equals(tcId)) {
				return jTableResponse = new JTableResponse("OK", null,0);
			}
			List<JsonTestExecutionResultBugList> jterbugList = testCaseService.getFeatureTestCaseDefectList(tcId);
			
					
			log.info("jterbugList size "+jterbugList.size());
			if (jterbugList == null) {				
				log.info("No Bugs for FeatureTestCase");
				return jTableResponse = new JTableResponse("OK", null,0);
			}
		
			 JSONArray fcolumnData = new JSONArray();
			 JSONArray fcolumnData1 = new JSONArray();
			if(jterbugList!=null ){
				for (JsonTestExecutionResultBugList jsonTestExecutionResultBugList : jterbugList) {
					fcolumnData = new JSONArray();
					fcolumnData.add(jsonTestExecutionResultBugList.getProductVersionName());
					fcolumnData.add(jsonTestExecutionResultBugList.getBugTitle());
					fcolumnData.add(jsonTestExecutionResultBugList.getBugManagementSystemName());
					fcolumnData.add(jsonTestExecutionResultBugList.getBugManagementSystemBugId());
					fcolumnData.add(jsonTestExecutionResultBugList.getBugFilingStatusName());
					fcolumnData.add(jsonTestExecutionResultBugList.getRemarks());
					fcolumnData.add(jsonTestExecutionResultBugList.getSeverityName());
					fcolumnData1.add(fcolumnData);
				}
					
			}
			jTableResponse = new JTableResponse("OK", jterbugList,jterbugList.size());
			jterbugList = null;
			return jTableResponse;
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Feature TestCase Defect View records!");
	            log.error("JSON ERROR", e);            
	        }		
		return jTableResponse;
	}
	
	@RequestMapping(value="show.testcase.defect.count",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getTestCasesSDefectsCount(@RequestParam Integer tcId) {
		log.info("product.feature.testcase.execution.defects");
		JTableResponse jTableResponse;
		String ffinalResult="";
		try {		
			if (tcId == null || ("null").equals(tcId)) {
				return jTableResponse = new JTableResponse("OK", null,0);
			}
			List<JsonFeatureTestCaseDefect> jterbugList = testCaseService.getFeatureTestCaseDefectCount(tcId);
			log.info("Feature TestCase Defects - "+jterbugList.size());
			if (jterbugList == null) {				
				log.info("No Bugs for FeatureTestCase");
				return jTableResponse = new JTableResponse("OK", null,0);
			}
			jTableResponse = new JTableResponse("OK", jterbugList,jterbugList.size());
			jterbugList = null;
			return jTableResponse;
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Feature TestCase Defect View records!");
	            log.error("JSON ERROR", e);            
	        }		
		return jTableResponse;
	}
	
	@RequestMapping(value="show.mapped.testcase.of.feature",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse showMappedTestCases(@RequestParam Integer featureId, @RequestParam Integer jtStartIndex ,@RequestParam Integer jtPageSize) {
		log.info("show.mapped.testcase.of.feature");
		JTableResponse jTableResponse;
		int productId = 0;
			try {	
				List<Object[]> mappedTCListObj = productListService.getMappedTestCaseListByProductFeatureId(productId, featureId, jtStartIndex, jtPageSize);
				JSONArray mappedJsonArray = new JSONArray();
				for (Object[] row : mappedTCListObj) {
					JSONObject jsobj =new JSONObject();
					jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
					jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);	
					jsobj.put(IDPAConstants.ITEM_CODE, (String)row[2]);	
					mappedJsonArray.add(jsobj);					
				}				
				jTableResponse = new JTableResponse("OK", mappedJsonArray,mappedJsonArray.size());
				mappedTCListObj = null;					 
			
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching MappedTestCaseList for Product!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
	}
	
	@RequestMapping(value="show.mapped.testscript.of.testcase",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse showMappedTestscripts(@RequestParam Integer testcaseId, @RequestParam Integer jtStartIndex ,@RequestParam Integer jtPageSize) {
		log.info("show.mapped.testcase.of.feature");
		JTableResponse jTableResponse;
		int productId = 0;
			try {	
				List<Object[]> mappedTCListObj = productListService.getMappedTestScriptListByTestcaseId(productId, testcaseId, jtStartIndex, jtPageSize);
				JSONArray mappedJsonArray = new JSONArray();
				for (Object[] row : mappedTCListObj) {
					JSONObject jsobj =new JSONObject();
					jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
					jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);	
					jsobj.put(IDPAConstants.ITEM_CODE, (String)row[2]);	
					mappedJsonArray.add(jsobj);					
				}				
				jTableResponse = new JTableResponse("OK", mappedJsonArray,mappedJsonArray.size());
				mappedTCListObj = null;					 
			
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Mapped Testescript List for Product!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
	}
	
	
	
	@RequestMapping(value="testcase.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addRestTestCase( HttpServletRequest request, @ModelAttribute JsonTestCaseList jsonTestCaseList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		log.info("product.testcase.add");
		if(result.hasErrors()){			
			jTableSingleResponse =jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {		
			    UserList user = (UserList)request.getSession().getAttribute("USER");
				TestCaseList testCaseListFromUI = jsonTestCaseList.getTestCaseList();				
				String errorMessage = ValidationUtility.validateForNewTestCaseAddition(testCaseListFromUI, testCaseService, testSuiteConfigurationService, testCaseListFromUI.getProductMaster().getProductId());				
				
				if (errorMessage != null) {					
					jTableSingleResponse = new JTableSingleResponse("ERROR",new JsonTestCaseList(testCaseListFromUI));
					return jTableSingleResponse;
				}	
				
				if(jsonTestCaseList.getExecutionTypeId() != null){
					ExecutionTypeMaster executionTypeMasterFromUI = executionTypeMasterService.getExecutionTypeByExecutionTypeId(jsonTestCaseList.getExecutionTypeId());
					testCaseListFromUI.setExecutionTypeMaster(executionTypeMasterFromUI);
				}
				if(jsonTestCaseList.getTestcasePriorityId() != null){
					TestCasePriority TestCasePriorityFromUI = testCaseService.getTestCasePriorityBytestcasePriorityId(jsonTestCaseList.getTestcasePriorityId());
					testCaseListFromUI.setTestCasePriority(TestCasePriorityFromUI);
				}
				if(jsonTestCaseList.getTestcaseTypeId() != null){
					TestcaseTypeMaster testcaseTypeMasterFromUI = testCaseService.getTestcaseTypeMasterBytestcaseTypeId(jsonTestCaseList.getTestcaseTypeId());
					testCaseListFromUI.setTestcaseTypeMaster(testcaseTypeMasterFromUI);					
				}			
				
				testCaseListFromUI.setStatus(1);
				
				int testCaseId = testCaseService.addTestCase(testCaseListFromUI);	
				
				mongoDBService.addProductTestCaseToMongoDB(testCaseListFromUI.getTestCaseId());
				
	            jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestCaseList(testCaseListFromUI));
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new TestCase!");
	            log.error("JSON ERROR adding new TestCase", e);
	        }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="product.script.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody String addRestTestScripts(@RequestParam Integer productId, @ModelAttribute(value="FORM1") UploadForm form, BindingResult result){
        if(!result.hasErrors()){
            FileOutputStream outputStream = null;
            log.info("Product Id: "+productId);
            String path = CommonUtility.getCatalinaPath().concat("/webapps/iLCM/TestScript").trim() + File.separator + productId +File.separator;
            Configuration.checkAndCreateDirectory(path);
            String filePath = path+File.separator+form.getFile().getOriginalFilename();
            try {            	            	
            	File file = new File(filePath);            	
                outputStream = new FileOutputStream(file);
                outputStream.write(form.getFile().getFileItem().get());
                outputStream.close();
            } catch (Exception e) {
            	log.error("Error while saving file", e);               
                return "ERROR";
            }
            return "OK - Test Scripts uploaded to server location ="+filePath.trim();
        }else{
            return "ERROR";
        }
    }	
	
	@RequestMapping(value="product.testsuite.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse updateTestSuite(@ModelAttribute JsonTestSuiteList jsonTestSuiteList, BindingResult result) {
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableResponse  = new JTableResponse("ERROR","Invalid form!"); 
		}
		
		try {
				TestSuiteList testSuiteFromDB = testSuiteConfigurationService.getByTestSuiteId(jsonTestSuiteList.getTestSuiteId());
				TestSuiteList testSuiteList=jsonTestSuiteList.getTestSuiteList();
				 
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
				}else{
					TestSuiteList testSuiteList1 = testSuiteConfigurationService.getByTestSuiteId(jsonTestSuiteList.getTestSuiteId());
					jsonTestSuiteList.setProductVersionListId(testSuiteList1.getProductVersionListMaster().getProductVersionListId());
				}
			
				testSuiteList.setTestSuiteScriptFileLocation(trimmingSuite);
				
				int testCaseId = 0;				
				
				testSuiteList.setTestCaseLists(testSuiteFromDB.getTestCaseLists());
				testSuiteConfigurationService.updateTestSuite(testSuiteList);
				
				List<JsonTestSuiteList> tmpList =new ArrayList<JsonTestSuiteList>();
				tmpList.add(new JsonTestSuiteList(testSuiteList));
	            jTableResponse = new JTableResponse("OK",tmpList ,1);
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating record!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	
	@RequestMapping(value="get.predecessors.testcase.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getPredecessorsTestcaseList(@RequestParam Integer testCaseId) {
		log.info("get.predecessors.testcase.list");
		JTableResponse jTableResponse;
		try {		
			if (testCaseId == null || ("null").equals(testCaseId)) {					
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			List<TestCaseList> testCaseListFromDB = testCaseService.getTestCasePredecessorByTestCaseId(testCaseId);	
			if (testCaseListFromDB == null) {				
				log.info("No predecessors available for this testcase");
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			List<JsonTestCaseList> jsonTestCaseList = new ArrayList<JsonTestCaseList>();
			for (TestCaseList testCaseList : testCaseListFromDB) {
				jsonTestCaseList.add(new JsonTestCaseList(testCaseList, testCaseId));
			}		
			jTableResponse = new JTableResponse("OK", jsonTestCaseList,jsonTestCaseList.size());
	        testCaseListFromDB = null;  
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="get.similar.to.testcase.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getSimilarToTestcaseList(@RequestParam Integer testCaseId) {
		log.info("get.predecessors.testcase.list");
		JTableResponse jTableResponse;
		try {		
			if (testCaseId == null || ("null").equals(testCaseId)) {					
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			List<TestCaseList> testCaseListFromDB = testCaseService.getSimilarToTestCaseSByTestCaseId(testCaseId);	
			if (testCaseListFromDB == null) {				
				log.info("No predecessors available for this testcase");
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			List<JsonTestCaseList> jsonTestCaseList = new ArrayList<JsonTestCaseList>();
			for (TestCaseList testCaseList : testCaseListFromDB) {
				jsonTestCaseList.add(new JsonTestCaseList(testCaseList, testCaseId));
			}		
			jTableResponse = new JTableResponse("OK", jsonTestCaseList,jsonTestCaseList.size());
	        testCaseListFromDB = null;  
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	

	
	
	@RequestMapping(value="product.testcase.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteTestcase(@RequestParam Integer testCaseId) {
		log.info("product.testcase.delete");
		JTableResponse jTableResponse;
		try {		
			if (testCaseId == null || ("null").equals(testCaseId)) {					
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			TestCaseList testCaseListFromDB=testCaseService.getTestCaseById(testCaseId);
			if (testCaseListFromDB == null) {				
				log.info("No available for this testcase");
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			} else {
				testCaseListFromDB.setStatus(2);
				testCaseService.update(testCaseListFromDB);
				//To unmap the Test case from the test suite
				testSuiteConfigurationService.delAllTestCaseMappingsByTestCaseId(testCaseId);
			}
					
			jTableResponse = new JTableResponse("OK","TestCase Deleted Successfully" );
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="product.testsuite.unmapped.testcase.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listUnmappedTestCases(HttpServletRequest request,@RequestParam Integer productId,@RequestParam Integer testSuiteId,@RequestParam String status, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.info("inside product.testcase.list");
		JTableResponse jTableResponse;
		Integer engagementId=0;
		try {
			if (productId == null || ("null").equals(productId)) {
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			UserList user = (UserList) request.getSession().getAttribute("USER");

			List<TestCaseList> totaltestCaseList=testCaseService.getTestCaseListByProductId(productId, null, null);
			List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
			
			if (totaltestCaseList == null){	
				log.info("inside product.testcase.list testCaseList is null");
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			
			TestSuiteList testSuite = testSuiteConfigurationService.getByTestSuiteId(testSuiteId);
			Set<TestCaseList> listTestCase = testSuite.getTestCaseLists();
			List<Integer> mappedtestcaseIdList = new ArrayList<Integer>();		
			
			for(TestCaseList testCaseList: listTestCase){
				mappedtestcaseIdList.add(testCaseList.getTestCaseId());				
			}
			
			for (TestCaseList  tclist: totaltestCaseList) {	
				if(mappedtestcaseIdList != null){
					if(!mappedtestcaseIdList.contains(tclist.getTestCaseId())){
						jsonTestCaseList.add(new JsonTestCaseList(tclist));
					}
				}else{
					jsonTestCaseList.add(new JsonTestCaseList(tclist));
				}				
			}
			jTableResponse = new JTableResponse("OK", jsonTestCaseList,jsonTestCaseList.size());
			totaltestCaseList = null;			 
			
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching TestcaseList for Product Version!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="get.ise.similar.to.testcase.list",method=RequestMethod.POST ,produces="application/json")
	@ResponseBody JSONObject getISESimilartoTestcaseList(HttpServletRequest request,@RequestParam Integer productId,@RequestParam Integer testCaseId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
	log.info("inside get.ise.similar.to.testcase.list");
		JTableResponse jTableResponse;
		Integer engagementId=0;
		JSONObject finalObj = new JSONObject();
		try {
		ProductMaster product=productListService.getProductById(productId);
	
			JSONObject inputObject = new JSONObject();
			inputObject.put("username","admin@hcl.com");
			if(product !=null) {
				inputObject.put("projectName",product.getProductName());
			}
			inputObject.put("testcaseID",testCaseId);
			
			List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
			
			UserList user = (UserList) request.getSession().getAttribute("USER");
			
			String responseData= ISEServerAccesUtility.GetISERestServiceCall(iseServerURL,inputObject.toString(),similarToTestcaseServiceAPI);			
			log.info("Final Result"+responseData);
			
			
			JSONArray testCaseTitleList = new JSONArray();
			JSONArray dataList = new JSONArray();
			
			JSONParser parser = new JSONParser();
			JSONObject similartToTestcaseJsonObj=null;
			Map<Integer,String> iseSimilartToTestcases= new HashMap<Integer,String>();
				log.info("Parsing JSON response returned from ise similar to API: \n" + responseData+"\n");
				similartToTestcaseJsonObj=(JSONObject)parser.parse(responseData);
				if(similartToTestcaseJsonObj != null){ 
						if(similartToTestcaseJsonObj.get("result") != null && !similartToTestcaseJsonObj.get("result").equals("Data not available")){
								JSONArray responseCTArr = (JSONArray)similartToTestcaseJsonObj.get("result");	
								if(responseCTArr != null && responseCTArr.size() >0) {
									for (Object o : responseCTArr) {
										JSONObject jsonObj = (JSONObject) o;
										if(jsonObj.get("_id") != null && jsonObj.get("title") != null){
												iseSimilartToTestcases.put(Integer.parseInt(jsonObj.get("_id").toString()),jsonObj.get("title").toString());
												dataList.add(iseSimilartToTestcases);
										}
											
									}
								
							     }
						}
				}		
			List<SimilartoTestcaseMapping> mappedSimilarToTestCases=similartoTestcaseMappingService.getSimilarToTestcaseMappingListByTestcaseId(testCaseId);
			
			if(mappedSimilarToTestCases != null && mappedSimilarToTestCases.size() >0) {
				for(SimilartoTestcaseMapping similarToTestcaseObj:mappedSimilarToTestCases){
					if(similarToTestcaseObj.getId().equals(mappedSimilarToTestCases.get(similarToTestcaseObj.getId()))) {
						JSONObject testCaseTitle=new JSONObject();
						testCaseTitle.put("_id",similarToTestcaseObj.getId());
						testCaseTitleList.add(testCaseTitle);
					}
				}
				finalObj.put("COLUMNS", testCaseTitleList);
				
			}
			finalObj.put("DATA", dataList);
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching TestcaseList for Product Version!");
	            log.error("JSON ERROR", e);
	        }
        return finalObj;
    }
}