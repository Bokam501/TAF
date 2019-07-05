/**
 * 
 */
package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestcaseTypeMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonProductFeature;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.terminal.JsonTestSuiteList;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.CommonTestManagementService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.ilcm.workflow.model.WorkflowStatus;

/**
 * @author silambarasur
 *
 */
@Service
public class CommonTestManagementServiceImpl implements CommonTestManagementService{

	private static final Log log = LogFactory.getLog(CommonTestManagementService.class);
	
	@Autowired
	private TestCaseService testCaseService;
	
	@Autowired
	private ProductListService productListService;
	
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService;
	
	@Autowired
	private MongoDBService mongoDBService;
	
	@Autowired
	private TestCaseListDAO testcaseListDao;
	
	@Autowired
	private UserListService userListService;
	
	@Autowired
	HttpServletRequest request;
	
	@Value("#{ilcmProps['USER_AUTHENTICATION_REQUIRED']}")
    private String atlasUserAuthenticationRequired;
	
	@Override
	@Transactional
	public JSONObject addTestcases(String testcasesJson) {
	//	JSONObject responseJson = new JSONObject();
		List<org.json.simple.JSONObject> testCases = new ArrayList<org.json.simple.JSONObject>();
		String testCaseName = null;
		ProductMaster product = null;
		JSONArray arr = new JSONArray();
		Integer productId = -1;
		Integer productTypeId = null;
		String productTypeName = null;
		ProductType productType = null;
		String failedTCs = "";
		String createdTCs = "";
		String updatedTCs = "";
		String errorMessage = null;
		boolean testCaseUpdateFlag=true;
		try {
			final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
			for(StackTraceElement trace : traces){
				if(trace.getClassName().equalsIgnoreCase("com.hcl.atf.taf.integration.CustomTestSystemConnectorsManager")) {
					testCaseUpdateFlag=false;
					break;
				}
			}
			
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(testcasesJson);
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testExecution/query/addNewTestcase");
				return prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			JSONObject authResponse = authenticateUser(userName, "/testExecution/query/addNewTestcase");
			if (authResponse != null)
				return authResponse;
			
			org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray) jsonFormatObject.get("testCases");
			for (int i = 0; i < jsonArray.size(); i++) {
				org.json.simple.JSONObject explrObject = (org.json.simple.JSONObject) jsonArray.get(i);
			    testCases.add(explrObject);
			}
			if(testCases != null && !testCases.isEmpty()){				
				for(org.json.simple.JSONObject obj : testCases){	
					testCaseName = (String) obj.get("testCaseName");
					if(testCaseName== null || testCaseName.isEmpty()){
						/*responseJson.put("result", "ERROR");
						responseJson.put("status", "400");	
						responseJson.put("data", "");
						responseJson.put("message", "Testcase name is required");
						responseJson.put("Failure_Details", "Testcase name is required");
						return responseJson;*/
						return prepareErrorResponseWithoutData("Testcase name is required", "Testcase name is required");
					}
					
					if((String) obj.get("productTypeName") != null){
						productTypeName = (String) obj.get("productTypeName");
						if(productTypeName != null)
							productType = productListService.getProductTypeByName(productTypeName);
						if(productType == null){
							return prepareErrorResponseWithoutData("Product Type is invalid", "Product Type is invalid");
						}
					}else if((String) obj.get("productTypeId") != null){
						try {
							productTypeId = Integer.valueOf((String) obj.get("productTypeId"));
						} catch (Exception er) {
							productTypeId = 2;
						}
						productType = productListService.getProductTypeById(productTypeId);
						if(productType == null){
							return prepareErrorResponseWithoutData("Product Type is invalid", "Product Type is invalid");
						}
					}else{
						productType = productListService.getProductTypeById(2);
					}
					
					TestCaseList testCaseList = new TestCaseList();
					
					if((String) obj.get("productId") != null){
						try {
							productId = Integer.valueOf((String) obj.get("productId"));
						} catch (Exception er) {
							productId = -1;
						}
					}
					product = productListService.getProductDetailsById(productId);		
					if (product == null) {
						/*responseJson.put("result", "ERROR");
						responseJson.put("status", "400");
						responseJson.put("message", "Product Id is missing or invalid");
						responseJson.put("Failure_Details", "Product Id is missing or invalid");
						return responseJson;*/
						return prepareErrorResponseWithoutData("Product Id is missing or invalid", "Product Id is missing or invalid");
					}
							
					//Check authorization for user
					authResponse = checkUserAuthorization(product.getProductId(), userName, "/testExecution/query/addNewTestcase");
					if (authResponse != null)
						return authResponse;
					String createdDateString = (String)obj.get("createdDate");
					String updatedDateString = (String)obj.get("updatedDate");
					Date createdDate = null;
					if (createdDateString != null && !createdDateString.trim().isEmpty()) {
						createdDate = DateUtility.dateWithTimeFormat(createdDateString);
					}
					Date updatedDate = null;
					if (updatedDateString != null && !updatedDateString.trim().isEmpty()) {
						updatedDate =DateUtility.dateWithTimeFormat(updatedDateString);
					}
					
					testCaseList=setUpTestCaseListValue(testCaseList, obj, testCaseName,product, productType);
					errorMessage = ValidationUtility.validateForNewTestCaseAddition(testCaseList, testCaseService, testSuiteConfigurationService, testCaseList.getProductMaster().getProductId());					
					if (errorMessage != null && testCaseUpdateFlag) {					
						failedTCs += testCaseList.getTestCaseName()+",";
					}else{
						TestCaseList existingTestcase =testCaseService.getTestCaseByNameAndCode(testCaseName,testCaseList.getTestCaseCode(), productId);
						if(existingTestcase == null) {
							int testCaseId = testCaseService.addTestCase(testCaseList);
							log.info("Created Test Case ID : "+testCaseId);
							createdTCs += testCaseList.getTestCaseName()+",";
							JsonTestCaseList josnTestcaseList=new JsonTestCaseList(testCaseList);
							arr.put(josnTestcaseList.getCleanJson());
						} else if(existingTestcase != null && !testCaseUpdateFlag) {
							if((updatedDate == null || existingTestcase.getTestCaseLastUpdatedDate() == null)||(DateUtility.compareDateTimeRange(updatedDate, existingTestcase.getTestCaseLastUpdatedDate()) > 0)) {
								existingTestcase=setUpTestCaseListValue(existingTestcase, obj, testCaseName,product, productType);
								testCaseService.update(existingTestcase);
								updatedTCs += testCaseList.getTestCaseName()+",";
								JsonTestCaseList josnTestcaseList=new JsonTestCaseList(existingTestcase);
								arr.put(josnTestcaseList.getCleanJson());
							}
						}
						
					}
				}
			}else{
				/*responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("data", "");
				responseJson.put("message", "Testcase input is missing");
				responseJson.put("Failure_Details", "Testcase input is missing");
				return responseJson;*/
				return prepareErrorResponseWithoutData("Testcase input is missing", "Testcase input is missing");
			}			
			/*responseJson.put("result", "OK");
			responseJson.put("status", "200");
			responseJson.put("data", arr);
			*/
			if(!createdTCs.isEmpty()) {
				//responseJson.put("message", "Added testcases are "+createdTCs.substring(0, createdTCs.length()-1)+".");
				return prepareSuccessResponseWithJSONArray("Added testcases are "+createdTCs.substring(0, createdTCs.length()-1)+".", arr);
			}else if(!updatedTCs.isEmpty()){
				//responseJson.put("message", "updated testcases are "+updatedTCs.substring(0, updatedTCs.length()-1)+".");
				return prepareSuccessResponseWithJSONArray("updated testcases are "+updatedTCs.substring(0, updatedTCs.length()-1)+".", arr);
			} else if(!failedTCs.isEmpty()) {
//				responseJson.put("Failure_Details", "Failed testcases :"+failedTCs.substring(0, failedTCs.length()-1)+".Reason : TestCase already exists with the same name(s)");
				return prepareErrorResponseWithoutData("Failed to add testcases", "Failed testcases :"+failedTCs.substring(0, failedTCs.length()-1)+".Reason : TestCase already exists with the same name(s)");
			} else {
				//responseJson.put("message", "Failed to add testcases");
				return prepareErrorResponseWithoutData("Failed to add testcases", "Failed to add testcases");
			}
		}catch(Exception e) {
			try {
				/*responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("data", "");
				responseJson.put("message", "Error in add new testcases ");
				responseJson.put("Failure_Details", "Error in add new testcases");*/
				return prepareErrorResponseWithoutData("Error in add new testcases ", "Error in add new testcases ");
			} catch (Exception e1) {
				
			}
			return null;	
		}
	}

	private TestCaseList setUpTestCaseListValue(TestCaseList testCaseList,org.json.simple.JSONObject obj,String testCaseName,ProductMaster product,ProductType productType){
		try {
			testCaseList.setTestCaseName(testCaseName);
			if((String) obj.get("testCaseDesc") != null)
				testCaseList.setTestCaseDescription((String) obj.get("testCaseDesc"));
			else
				testCaseList.setTestCaseDescription(testCaseName);
			String testCaseCode = (String) obj.get("testCaseCode");
			if(testCaseCode != null || testCaseCode != "" || testCaseCode.trim().isEmpty())
				testCaseList.setTestCaseCode((String) obj.get("testCaseCode"));
			else
				testCaseList.setTestCaseCode(testCaseName);
			testCaseList.setStatus(1);
			
			testCaseList.setProductMaster(product);					
			testCaseList.setTestCaseSource("TAF Terminal");
			testCaseList.setTestcaseExecutionType(3);
			ExecutionTypeMaster etm = new ExecutionTypeMaster();
			etm.setExecutionTypeId(1);
			testCaseList.setExecutionTypeMaster(etm);			
			TestCasePriority testcasePriority = new TestCasePriority();
			testcasePriority.setTestcasePriorityId(2);
			testCaseList.setTestCasePriority(testcasePriority);			
			TestcaseTypeMaster testcaseTypeMaster = new TestcaseTypeMaster();
			testcaseTypeMaster.setTestcaseTypeId(1);
			testCaseList.setTestcaseTypeMaster(testcaseTypeMaster);
			testCaseList.setTestCaseCreatedDate(DateUtility.getCurrentTime());			
			testCaseList.setTestCaseLastUpdatedDate(DateUtility.getCurrentTime());		
			testCaseList.setProductType(productType);
		}catch(Exception e) {
			
		}
		return testCaseList;
	}
	@Override
	@Transactional
	public JSONObject addFeatures(String featuresJson) {
		//JSONObject responseJson = new JSONObject();
		JSONArray arr = new JSONArray();
		List<ProductFeature> existingFeatureList= new ArrayList<ProductFeature>(); 
		List<ProductFeature> newFeatureLis= new ArrayList<ProductFeature>();
		Boolean featureUpdateFlag=true;
		StringBuffer responseMessage = new StringBuffer();
		try {
			
			final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
			for(StackTraceElement trace : traces){
				if(trace.getClassName().equalsIgnoreCase("com.hcl.atf.taf.integration.CustomTestSystemConnectorsManager")) {
					featureUpdateFlag=false;
					break;
				}
			}
		    
			JSONParser parser = new JSONParser();
			org.json.simple.JSONArray featureFormArray = (org.json.simple.JSONArray) parser.parse(featuresJson);
			if(featureFormArray == null) {
				log.info("Could not parse request : /addfeature");
				return prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			
			for(int i=0;i<featureFormArray.size();i++) {
				org.json.simple.JSONObject jsonFormatObject=(org.json.simple.JSONObject) featureFormArray.get(i);
				
				String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
				JSONObject authResponse = authenticateUser(userName, "/testExecution//addfeature");
				if (authResponse != null)
					return authResponse;
				
				String productFeatureName=(String)jsonFormatObject.get("productFeatureName");
				String productFeatureDescription=(String)jsonFormatObject.get("productFeatureDescription");
				String productFeatureCode=(String)jsonFormatObject.get("productFeatureCode");
				String parentFeatureId=(String)jsonFormatObject.get("parentFeatureId");
				String parentFeatureStatus=(String)jsonFormatObject.get("parentFeatureStatus");
				String productId=(String)jsonFormatObject.get("productId");
				String abbr=(String)jsonFormatObject.get("abbr");
				String executionPriorityId=(String)jsonFormatObject.get("executionPriorityId");
				//String synTime=(String)jsonFormatObject.get("synTime");
				String createdDateString = (String)jsonFormatObject.get("createdDate");
				String updatedDateString = (String)jsonFormatObject.get("updatedDate");
				Date createdDate = null;
				if (createdDateString != null && !createdDateString.trim().isEmpty()) {
					createdDate = DateUtility.dateWithTimeFormat(createdDateString);
				}
				Date updatedDate = null;
				if (updatedDateString != null && !updatedDateString.trim().isEmpty()) {
					updatedDate =DateUtility.dateWithTimeFormat(updatedDateString);
				}
				if(productFeatureName == null || productFeatureName.trim().isEmpty()) {
					
					responseMessage.append("Feature no " + i + " ignored as it does not have feature name" + System.lineSeparator());
					continue;
					//responseJson.put("result", "ERROR");
					//responseJson.put("status", "400");	
					//responseJson.put("data", "");
					//responseJson.put("message", "productFeatureName is required");
					//responseJson.put("Failure_Details", "productFeatureName is required");
					//return responseJson;
				}
				
				if(productId == null || productId.trim().isEmpty()) {
					/*responseJson.put("result", "ERROR");
					responseJson.put("status", "400");	
					responseJson.put("data", "");
					responseJson.put("message", "ProductId is required");
					responseJson.put("Failure_Details", "ProductId is required");
					return responseJson;*/
					return prepareErrorResponseWithoutData("ProductId is required", "ProductId is required");
				}
				
				ProductMaster productMaster = null;
				productMaster=productListService.getProductDetailsById(Integer.parseInt(productId));
				
				if(productMaster == null) {
					/*responseJson.put("result", "ERROR");
					responseJson.put("status", "400");	
					responseJson.put("data", "");
					responseJson.put("message", "Invalid ProductId");
					responseJson.put("Failure_Details", "Invalid ProductId");
					return responseJson;*/
					return prepareErrorResponseWithoutData("Invalid ProductId", "Invalid ProductId");
				}
				//Check authorization for user
				authResponse = checkUserAuthorization(productMaster.getProductId(), userName, "/testExecution/addfeature");
				if (authResponse != null)
					return authResponse;
				ProductFeature existingFeature=productListService.getByProductFeatureName(productFeatureName, productMaster);
				
				TestCasePriority testCasePriority = new TestCasePriority();
				ProductFeature parentFeature=null;
				if(existingFeature != null) {
					//Check if the feature has been updated after last sync
					if ((updatedDate == null || existingFeature.getModifiedDate() == null ) || (DateUtility.compareDateTimeRange(updatedDate, existingFeature.getModifiedDate()) > 0)) {
						//The feature has been updated in the test system after last sync
						//Needs to be updated in TAF
					
						if(productFeatureCode != null && !productFeatureCode.trim().isEmpty()) {
							existingFeature.setProductFeatureCode(productFeatureCode);
						} else {
							existingFeature.setProductFeatureCode(productFeatureName);
						}
						existingFeature.setDisplayName(productFeatureName);
						
						if(productFeatureDescription != null && !productFeatureDescription.trim().isEmpty()) {
							existingFeature.setProductFeatureDescription(productFeatureDescription);
						} else {
							existingFeature.setProductFeatureDescription(productFeatureName);
						}
						
						if(executionPriorityId != null && executionPriorityId.trim().isEmpty()) {
							testCasePriority.setTestcasePriorityId(Integer.parseInt(executionPriorityId));
						} else{
							testCasePriority.setTestcasePriorityId(1);
						}
						existingFeature.setExecutionPriority(testCasePriority);
						if(abbr != null && !abbr.trim().isEmpty()) {
							existingFeature.setAbbr(abbr);
						}
						existingFeature.setProductFeaturestatus(1);
						 existingFeature.setModifiedDate(new Date());
						if(parentFeatureId!= null && (!parentFeatureId.trim().isEmpty() && !parentFeatureId.equals("0"))){
							parentFeature=productListService.getByProductFeatureId(Integer.parseInt(parentFeatureId));
							if(parentFeature== null) {
	
								responseMessage.append("Parent feature id " + Integer.parseInt(parentFeatureId) + " for feature no " + i + " not found in system. Not adding parent feature" + System.lineSeparator());
	
	/*							responseJson.put("result", "ERROR");
								responseJson.put("status", "400");	
								responseJson.put("data", "");
								responseJson.put("message", "Invalid ParentFeatureId");
								responseJson.put("Failure_Details", "Invalid ParentFeatureId");
								return responseJson;
	*/						
							} else {
								existingFeature.setParentFeature(parentFeature.getParentFeature());
							}
						} 
						existingFeature.setSourceType(IDPAConstants.FEATURE_ADD_SOURCE_TYPE);
						existingFeatureList.add(existingFeature);
					} else {
						//Do nothing. This feature has not been updated in test system since last sync.
						//Hence no need to update it in TAF
					}
				} else {
					
					ProductFeature feature= new ProductFeature();
					feature.setProductMaster(productMaster);
					feature.setProductFeatureName(productFeatureName);
					if(productFeatureCode != null && !productFeatureCode.trim().isEmpty()) {
						feature.setProductFeatureCode(productFeatureCode);
					} else {
						feature.setProductFeatureCode(productFeatureName);
					}
					feature.setDisplayName(productFeatureName);
					
					if(productFeatureDescription != null && !productFeatureDescription.trim().isEmpty()) {
						feature.setProductFeatureDescription(productFeatureDescription);
					} else {
						feature.setProductFeatureDescription(productFeatureName);
					}
					
					
					if(executionPriorityId != null && executionPriorityId.trim().isEmpty()) {
						testCasePriority.setTestcasePriorityId(Integer.parseInt(executionPriorityId));
					} else{
						testCasePriority.setTestcasePriorityId(1);
					}
					feature.setExecutionPriority(testCasePriority);
					if(abbr != null && !abbr.trim().isEmpty()) {
						feature.setAbbr(abbr);
					}
					feature.setProductFeaturestatus(1);
					WorkflowStatus workflowStatus = new WorkflowStatus();
					workflowStatus.setWorkflowStatusId(-1);
					feature.setWorkflowStatus(workflowStatus);
					
					if(parentFeatureId!= null && (!parentFeatureId.trim().isEmpty() && !parentFeatureId.equals("0"))){
						parentFeature=productListService.getByProductFeatureId(Integer.parseInt(parentFeatureId));
						if(parentFeature== null) {
							
							responseMessage.append("Parent feature id " + Integer.parseInt(parentFeatureId) + " for feature no " + i + " not found in system. Not adding parent feature" + System.lineSeparator());
							//continue;

							//responseJson.put("result", "ERROR");
							//responseJson.put("status", "400");	
							//responseJson.put("data", "");
							//responseJson.put("message", "Invalid ParentFeatureId");
							//responseJson.put("Failure_Details", "Invalid ParentFeatureId");
							//return responseJson;
							parentFeature = new ProductFeature();
							parentFeature.setProductFeatureId(1);
						} else {
							feature.setParentFeature(parentFeature.getParentFeature());
						}
					} 
					feature.setSourceType(IDPAConstants.FEATURE_ADD_SOURCE_TYPE);
					feature.setCreatedDate(new Date());
					feature.setModifiedDate(new Date());
					newFeatureLis.add(feature);
				}
			}
			if(newFeatureLis != null && newFeatureLis.size() >0) {
				for(ProductFeature feature:newFeatureLis) {
					productListService.addProductFeature(feature);
					mongoDBService.addProductFeature(feature.getProductFeatureId());
					arr.put(new JsonProductFeature(feature).getCleanJson());
				}
			} 
		
			if(existingFeatureList != null && existingFeatureList.size() >0 && !featureUpdateFlag) {
				for(ProductFeature feature:existingFeatureList) {
					productListService.updateProductFeature(feature);
					arr.put(new JsonProductFeature(feature).getCleanJson());
				}
			}
				
			String message ="";
			if(featureUpdateFlag && (newFeatureLis != null && newFeatureLis.size() >0)) {
				responseMessage.append(newFeatureLis.size() + " Features created Successfully" + System.lineSeparator());
			} else if(!featureUpdateFlag && existingFeatureList != null && existingFeatureList.size() >0){
				responseMessage.append(existingFeatureList.size() + " Features updated Successfully" + System.lineSeparator());
			}
			if(existingFeatureList != null && existingFeatureList.size() >0 && featureUpdateFlag) {
				message = "";
				for(ProductFeature feature:existingFeatureList) {
					if(message == ""){
						message = feature.getProductFeatureName();
					}else{
						message += ", "+feature.getProductFeatureName();
					}
				}
				responseMessage.append(message + " - features are already available" + System.lineSeparator());
			} 
			/*responseJson.put("result", "OK");
			responseJson.put("status", "200");	
			responseJson.put("data", arr);
			responseJson.put("message", responseMessage.toString());*/
			return prepareSuccessResponseWithJSONArray(responseMessage.toString(), arr);
		}catch(Exception e) {
			try {
				/*responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("data", "");
				responseJson.put("message", "Error in add new features ");
				responseJson.put("Failure_Details", "Error in add new features");*/
				return prepareErrorResponseWithoutData( "Error in add new features ",  "Error in add new features ");
			} catch (Exception e1) {
				log.error(e1);
			}
			
			return null;	
		}
	}

	@Override
	@Transactional
	public JSONObject addTestSuites(String testSuitesJson) {
		String testSuiteName = null;
		String testSuiteCode = null;
		String testScriptType = null;
		ProductMaster product = null;
		String testSuiteScriptFileLocation = null;
		String scriptPlatformLocation = null;
		JSONArray arr = new JSONArray();
		Integer productId = null;
		Integer productTypeId = null;
		Integer productVersionId = null;
		String trimmingSuite = null;
		ProductVersionListMaster productVersion = null;
		ObjectMapper mapper = new ObjectMapper();
		JSONObject responseJson = new JSONObject();
		boolean testSuitesUpdateFlag= true;
		try {
			final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
			for(StackTraceElement trace : traces){
				if(trace.getClassName().equalsIgnoreCase("com.hcl.atf.taf.integration.CustomTestSystemConnectorsManager")) {
					testSuitesUpdateFlag=false;
					break;
				}
			}
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(testSuitesJson);
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testManagement/query/addTestSuite");
				return prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			JSONObject authResponse = authenticateUser(userName, "/testManagement/query/addTestSuite");
			if (authResponse != null)
				return authResponse;
			
			testSuiteName = (String) jsonFormatObject.get("testSuiteName");
			if(testSuiteName== null || testSuiteName.isEmpty()){
				return prepareErrorResponseWithoutData("Test Suite Name is required", "Test Suite Name is required");
			}
			testScriptType = (String) jsonFormatObject.get("testScriptType");
			if(testScriptType== null){
				testScriptType = "GHERKIN";
			}
			testSuiteScriptFileLocation = (String) jsonFormatObject.get("testScriptType");
			if((String) jsonFormatObject.get("testSuiteCode") != null)
				testSuiteCode = ((String) jsonFormatObject.get("testSuiteCode"));
			else
				testSuiteCode = testSuiteName;
			scriptPlatformLocation = ((String) jsonFormatObject.get("scriptPlatformLocation"));
			if(scriptPlatformLocation == null)
				scriptPlatformLocation = "Terminal";
			if(scriptPlatformLocation != null && !scriptPlatformLocation.equalsIgnoreCase("Server") && !scriptPlatformLocation.equalsIgnoreCase("Terminal")){
				return prepareErrorResponseWithoutData("scriptPlatformLocation is invalid.Valid values are Server/Terminal", "scriptPlatformLocation is invalid.Valid values are Server/Terminal");
			}
			if((String) jsonFormatObject.get("productId") != null){
				try {
					productId = Integer.valueOf((String) jsonFormatObject.get("productId"));
				} catch (Exception er) {
					productId = -1;
				}
			}
			product = productListService.getProductDetailsById(productId);		
			if (product == null) {
				return prepareErrorResponseWithoutData("Product Id is missing or invalid", "Product Id is missing or invalid");
			}
			//Check authorization for user
			authResponse = checkUserAuthorization(product.getProductId(), userName, "/testManagement/query/addTestSuite");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("productVersionId") != null){
				try {
					productVersionId = Integer.valueOf((String) jsonFormatObject.get("productVersionId"));
				} catch (Exception er) {
					productVersionId = -1;
				}
				productVersion = productListService.getProductVersionListMasterById(productVersionId);
				if (productVersion == null) {
					return prepareErrorResponseWithoutData("Product Version Id is missing or invalid", "Product Version Id is missing or invalid");
				}
			}
			String createdDateString = (String)jsonFormatObject.get("createdDate");
			String updatedDateString = (String)jsonFormatObject.get("updatedDate");
			Date createdDate = null;
			if (createdDateString != null && !createdDateString.trim().isEmpty()) {
				createdDate = DateUtility.dateWithTimeFormat(createdDateString);
			}
			Date updatedDate = null;
			if (updatedDateString != null && !updatedDateString.trim().isEmpty()) {
				updatedDate =DateUtility.dateWithTimeFormat(updatedDateString);
			}
			
			if(productVersion != null && product != null && productVersion.getProductMaster().getProductId() != product.getProductId()){
				log.info("Invalid product and product version assoaciation : /testExecution/query/productBuilds");
				return prepareErrorResponseWithoutData("Product Version : "+productVersionId +" is not associated to the Product : " +productId, "Product Version : "+productVersionId +" is not associated to the Product : " +productId);
			}
			if(productVersion == null){
				productVersion = productListService.getLatestProductVersionListMaster(product.getProductId());
				if(productVersion ==null){
					return prepareErrorResponseWithoutData("There are no Product Versions associated to the product", "There are no Product Versions associated to the product");
					
				}
			}
			TestSuiteList testSuiteList = new TestSuiteList();
			testSuiteList=setTestSuitesValue(testSuiteList, testSuiteName, testSuiteCode, product, productVersion, testSuiteScriptFileLocation, testScriptType, scriptPlatformLocation, trimmingSuite);			
			String errorMessage = ValidationUtility.validateForNewVersionTestSuiteAddition(testSuiteList, testSuiteConfigurationService);
			TestSuiteList existingTestSuiteList=testSuiteConfigurationService.getByProductIdAndTestSuiteNandAndTestSuiteCode(productId, testSuiteName, testSuiteCode);
			if (errorMessage != null && testSuitesUpdateFlag) {
				return prepareErrorResponseWithoutData(errorMessage, errorMessage);
			}
			
			if(existingTestSuiteList == null) {
				int testSuiteId = testSuiteConfigurationService.addTestSuite(testSuiteList);
				log.info("Created Test Suite ID : "+testSuiteId);
				JSONObject object = new JSONObject(mapper.writeValueAsString(new JsonTestSuiteList(testSuiteList)));
				arr.put(object);
				return prepareSuccessResponseWithJSONArray("Test Suite added", arr);
			}
			if(!testSuitesUpdateFlag && null != existingTestSuiteList) {
				 if((updatedDate == null || existingTestSuiteList.getStatusChangeDate() == null)||(DateUtility.compareDateTimeRange(updatedDate, existingTestSuiteList.getStatusChangeDate()) > 0)) {
					existingTestSuiteList=setTestSuitesValue(existingTestSuiteList, testSuiteName, testSuiteCode, product, productVersion, testSuiteScriptFileLocation, testScriptType, scriptPlatformLocation, trimmingSuite);
					testSuiteConfigurationService.updateTestSuiteList(existingTestSuiteList);
					JSONObject object = new JSONObject(mapper.writeValueAsString(new JsonTestSuiteList(existingTestSuiteList)));
					arr.put(object);
					return prepareSuccessResponseWithJSONArray("Test Suite Updated", arr);
				 }
			}else{
				return prepareErrorResponseWithoutData("Test Suite already exists", "Test Suite already exists");
			}
		}catch(Exception e) {
			try {
				return prepareErrorResponseWithoutData("Error adding TestSuite", "Error adding TestSuite");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return responseJson;
	}
	
	private TestSuiteList setTestSuitesValue(TestSuiteList testSuiteList,String testSuiteName,String testSuiteCode,ProductMaster product,ProductVersionListMaster productVersion,String testSuiteScriptFileLocation,String testScriptType,String scriptPlatformLocation,String trimmingSuite) {
		try {
		testSuiteList.setTestSuiteName(testSuiteName);
		testSuiteList.setTestSuiteCode(testSuiteCode);
		testSuiteList.setStatus(1);
		testSuiteList.setProductMaster(product);	
		testSuiteList.setProductVersionListMaster(productVersion);
		testSuiteList.setTestSuiteScriptFileLocation(testSuiteScriptFileLocation);
		ScriptTypeMaster scriptTypeMaster = new ScriptTypeMaster();
		scriptTypeMaster.setScriptType(testScriptType);
		testSuiteList.setScriptTypeMaster(scriptTypeMaster);
		testSuiteList.setTestScriptSource("TAF Server");
		testSuiteList.setScriptPlatformLocation(scriptPlatformLocation);
		ExecutionTypeMaster etm = new ExecutionTypeMaster();
		etm.setExecutionTypeId(3);
		testSuiteList.setExecutionTypeMaster(etm);
		testSuiteList.setStatusChangeDate(new Date());
		if(testSuiteScriptFileLocation != null)
			trimmingSuite = testSuiteScriptFileLocation.trim(); 
		if(null != trimmingSuite && !trimmingSuite.isEmpty()){
			if(null != scriptPlatformLocation && scriptPlatformLocation.equalsIgnoreCase("TERMINAL")){
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
			} else if(null != scriptPlatformLocation && scriptPlatformLocation.equalsIgnoreCase("SERVER")){
				if(trimmingSuite.startsWith("file:///")){
					 trimmingSuite.replace("file:///", "");
				}						
				trimmingSuite = trimmingSuite.substring(trimmingSuite.lastIndexOf("/")+1,trimmingSuite.length());
				trimmingSuite = trimmingSuite.substring(trimmingSuite.lastIndexOf("\\")+1,trimmingSuite.length());
				trimmingSuite = "http://" + request.getServerName()+":" + request.getServerPort()+request.getContextPath()+"/TestScripts/"+trimmingSuite;
			}
		}
		testSuiteList.setTestSuiteScriptFileLocation(trimmingSuite);
		}catch(Exception e) {
			
		}
		return testSuiteList;
	}

	@Override
	@Transactional
	public JSONObject mapTestcasesToTestsuite(String mapJsonObject) {
		//JSONObject responseJson = new JSONObject();
		Integer testSuiteId = null;
		List<Integer> testCaseIdList = new ArrayList<Integer>();
		TestSuiteList testSuiteList = null;
		List<TestCaseList> totalTestCaseList = null;
		Set<TestCaseList> tstcList = new HashSet<TestCaseList>();
		try {
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(mapJsonObject);
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testExecution/query/mapTestcasesToTestsuite");
				/*responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("data", "");
				responseJson.put("message", "No inputs provided");
				responseJson.put("Failure_Details", "No inputs provided");
				return responseJson;*/
				return prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			JSONObject authResponse = authenticateUser(userName, "/testExecution/mapTestcasesToTestsuite");
			if (authResponse != null)
				return authResponse;
			
			String testCaseIds = (String) jsonFormatObject.get("testCaseIds");
			if(testCaseIds == null || testCaseIds.isEmpty()) {
				/*responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("data", "");
				responseJson.put("message", "Testcase Ids are missing");
				responseJson.put("Failure_Details", "Testcase Ids are missing");
				return responseJson;*/
				return prepareErrorResponseWithoutData( "Testcase Ids are missing",  "Testcase Ids are missing");
			}
			
			try{
			
				for(String testCaseId : testCaseIds.split(",")){
					testCaseIdList.add(Integer.valueOf(testCaseId));
				}
			}catch(Exception e){
				/*responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("data", "");
				responseJson.put("message", "Invalid testcase Id");
				responseJson.put("Failure_Details", "Invalid testcase Id");
				return responseJson;*/
				return prepareErrorResponseWithoutData( "Invalid testcase Id", "Invalid testcase Id");
			}
			
			String testSuiteIdInput=(String) jsonFormatObject.get("testSuiteId");
			if( testSuiteIdInput!= null && !testSuiteIdInput.isEmpty()) {
				try {
					testSuiteId = Integer.valueOf(testSuiteIdInput);
				} catch (Exception er) {
					testSuiteId = -1;
				}
				testSuiteList=testSuiteConfigurationService.getByTestSuiteId(Integer.valueOf((String) jsonFormatObject.get("testSuiteId")));
				if (testSuiteList == null) {
					/*responseJson.put("result", "ERROR");
					responseJson.put("status", "400");	
					responseJson.put("data", "");
					responseJson.put("message", "Test Suite ID is invalid");
					responseJson.put("Failure_Details", "Test Suite ID is invalid");
					return responseJson;*/
					return prepareErrorResponseWithoutData( "Test Suite ID is invalid", "Test Suite ID is invalid");
				}
			} else {
				/*responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("data", "");
				responseJson.put("message", "Test Suite ID is missing");
				responseJson.put("Failure_Details", "Test Suite ID is missing");
				return responseJson;*/
				return prepareErrorResponseWithoutData( "Test Suite ID is missing", "Test Suite ID is missing");
			}
			
			//Check authorization for user
			authResponse = checkUserAuthorization(testSuiteList.getProductMaster().getProductId(), userName, "/testExecution/mapTestcasesToTestsuite");
			if (authResponse != null)
				return authResponse;
			
			JSONArray arr = new JSONArray();
			if(testSuiteList.getProductMaster() != null)
				totalTestCaseList = testCaseService.getTestCaseListByProductId(testSuiteList.getProductMaster().getProductId(), null, null);
			for(Integer testCaseId : testCaseIdList) {
				TestCaseList testCaseList = testSuiteConfigurationService.getByTestCaseId(testCaseId);
				tstcList = testSuiteList.getTestCaseLists();
				if(tstcList != null && tstcList.contains(testCaseList)){
					JSONObject obj = new JSONObject();
					obj.put("TestCaseId", testCaseList.getTestCaseId());
					obj.put("Message","ERROR-Test Case Already mapped!");
					arr.put(obj);
				} else {
					JSONObject obj = new JSONObject();
					if(totalTestCaseList != null && !totalTestCaseList.isEmpty() && !totalTestCaseList.contains(testCaseList)){
						obj.put("TestCaseId", testCaseList.getTestCaseId());
						obj.put("Message","ERROR-Test Case does not belong to the product :" + testSuiteList.getProductMaster().getProductName());
						arr.put(obj);
					}else{
						testSuiteConfigurationService.addTestCase(testCaseList.getTestCaseId(),testSuiteId);
						obj.put("TestCaseId", testCaseList.getTestCaseId());
						obj.put("Message","OK-Test Case mapped!");
						arr.put(obj);
					}
				}
			}
		/*	responseJson.put("result", "OK");
			responseJson.put("status", "200");			
			responseJson.put("data", arr);
			responseJson.put("message", arr);*/
			return prepareSuccessResponseWithJSONArray(arr.toString(), arr);
		
		}catch(Exception e) {
			try{
			/*responseJson.put("result", "ERROR");
			responseJson.put("status", "400");			
			responseJson.put("data", "");
			responseJson.put("Message", "Error mapping testcase!");
			responseJson.put("Failure_Details", "Error mapping testcase!" + e);
			return responseJson;*/
				return prepareErrorResponseWithoutData("Error mapping testcase!", "Error mapping testcase!");
			}catch(Exception e1) {
				
			}
		}
		return null; 
	}

	@Override
	public JSONObject mapFeatureToTestcases(String mapJsonObject) {
		List<TestCaseList> testCaseIdList = new ArrayList<TestCaseList>();
		List<ProductFeature> featureIdList = new ArrayList<ProductFeature>();
		String[] testCaseIdArr = null;
		String[] featureIdArr = null;
		//JSONObject responseJson = new JSONObject();
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonObject=(org.json.simple.JSONObject) parser.parse(mapJsonObject);
			StringBuffer sb = new StringBuffer();
			if(jsonObject == null) {
				log.info("Could not parse request : /map testcases");
				return prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonObject.get("user") != null ? jsonObject.get("user").toString():"";
			JSONObject authResponse = authenticateUser(userName, "/testExecution/mapFeatureToTestcases");
			if (authResponse != null)
				return authResponse;
			
				String testCaseIds = (String)jsonObject.get("testCaseIds");
				String featureIds = (String)jsonObject.get("featureIds");
				String action = (String)jsonObject.get("action");
				
				if(action == null || action.trim().isEmpty()) {
					/*responseJson.put("result", "ERROR");
					responseJson.put("status", "400");	
					responseJson.put("data", "");
					responseJson.put("message", "Action is required");
					responseJson.put("Failure_Details", "Action is required");
					return responseJson;*/
					return prepareErrorResponseWithoutData("Action is required", "Action is required");
				}else if (!(action.equalsIgnoreCase("map") || action.equalsIgnoreCase("unmap"))){
					/*responseJson.put("result", "ERROR");
					responseJson.put("status", "400");	
					responseJson.put("data", "");
					responseJson.put("message", "Invalid mapping action. Valid actions are map/unmap");
					responseJson.put("Failure_Details", "Action is required");
					return responseJson;*/
					return prepareErrorResponseWithoutData("Invalid mapping action. Valid actions are map/unmap", "Action is required");
				}
				if(testCaseIds != null && !testCaseIds.trim().isEmpty()){
					String tcIds="";
					try{
						testCaseIdArr = testCaseIds.split(",");
						for(String tc : testCaseIdArr) {	
							int tcId = Integer.valueOf(tc);
							TestCaseList testCase = testCaseService.getTestCaseById(tcId);
							if(testCase != null) {
								testCaseIdList.add(testCase);
							} else {
								tcIds+=tc+",";
							}
						}
						if(tcIds !="") {
							tcIds=tcIds.substring(0, tcIds.length()-1);
							sb.append("Testcases : " + tcIds + " are invalid ;");
						}
					} catch(Exception e) {
						log.error("Error while mapping feature to Testcase");
					}
				} else {
					/*responseJson.put("result", "ERROR");
					responseJson.put("status", "400");	
					responseJson.put("data", "");
					responseJson.put("message", "Testcase Id is required");
					responseJson.put("Failure_Details", "Testcase Id is required");
					return responseJson;*/
					return prepareErrorResponseWithoutData("Testcase Id is required", "Testcase Id is required");
				}
				
				if(featureIds != null && !featureIds.trim().isEmpty()){
					String fIds="";
					try{
						featureIdArr = featureIds.split(",");
						for(String fId : featureIdArr) {
							
							int featureId = Integer.valueOf(fId);
							ProductFeature feature = productListService.getByProductFeatureId(featureId);
							if(feature != null) {
								featureIdList.add(feature);
							} else {
								fIds+=fId+",";
							}
						}
						if(fIds != "") {
							fIds=fIds.substring(0, fIds.length()-1);
							sb.append("features : " + fIds + " are invalid.");
						}
					} catch(Exception e) {
						log.error("Error while feature input list");
					}
				} else {
					/*responseJson.put("result", "ERROR");
					responseJson.put("status", "400");	
					responseJson.put("data", "");
					responseJson.put("message", "Feature Id is required");
					responseJson.put("Failure_Details", "Feature Id is required");
					return responseJson;*/
					return prepareErrorResponseWithoutData("Feature Id is required", "Feature Id is required");
				}
				
				
				if((featureIdList != null && featureIdList.size() >0) && (testCaseIdList != null && testCaseIdList.size() >0)) {
					for(ProductFeature feature :featureIdList) {
						ProductMaster product = feature.getProductMaster();
						for(TestCaseList testCase: testCaseIdList) {
							if(testCase.getProductMaster().getProductId().equals(product.getProductId())) {
								//Check authorization for user
								authResponse = checkUserAuthorization(testCase.getProductMaster().getProductId(), userName, "/testExecution/executetestplan");
								if (authResponse == null) {
									sb.append(productListService.updateProductFeatureTestCase(testCase.getTestCaseId(),feature.getProductFeatureId(), action) + System.lineSeparator());
								}
							} else {
								sb.append("TestcaseId : "+testCase.getTestCaseId() +" and "+feature.getProductFeatureId()+" is not associated to the same Product  "); 
							}
						}
					}
				}
				/*if(action != null && action.equalsIgnoreCase("map")) {
					responseJson.put("result", "OK");
					responseJson.put("status", "200");	
					responseJson.put("data", sb.toString());
					responseJson.put("message", sb.toString());
					return prepareSuccessResponse(sb.toString(), sb.toString());
				} else if (action != null && action.equalsIgnoreCase("unmap")) {
					responseJson.put("result", "OK");
					responseJson.put("status", "200");	
					responseJson.put("data", sb.toString());
					responseJson.put("message", sb.toString());
					return prepareSuccessResponse(sb.toString(), sb.toString());
				}*/
				return prepareSuccessResponse(sb.toString().trim(), sb.toString());
			
		}catch(Exception e) {
			try {
				/*responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("data", "");
				responseJson.put("message", "Error in mapping feature to Testcases");
				responseJson.put("Failure_Details", "Error in mapping feature to Testcases");
				return responseJson;*/
				return prepareErrorResponseWithoutData("Error in mapping feature to Testcases", "Error in mapping feature to Testcases");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		}
		return null;
	}
	
private JSONObject checkUserAuthorization(Integer productId, String userName, String serviceName) {
		
		boolean isProductAuthorizedForUser=false;
		UserList user = userListService.getUserByLoginId(userName);
		try {
			if (atlasUserAuthenticationRequired != null && atlasUserAuthenticationRequired.equalsIgnoreCase("yes")) {
				isProductAuthorizedForUser = productListService.isUserPermissionByProductIdandUserId(productId, user.getUserId(), user.getUserRoleMaster().getUserRoleId());
				if (!isProductAuthorizedForUser) {
					
					log.info("REST service : " + serviceName + " : User : " + user.getLoginId() + " is not authorized for product Id : " + productId);
					return prepareErrorResponseWithoutData("User : " + user.getLoginId() + " is not authorized for product Id : " + productId, "User : " + user.getLoginId() + " is not authorized for product Id : " + productId);
				}
			}
		}catch(Exception e) {
			log.error("Problem while authorizing user : " + user.getLoginId(), e);
			try {
				return prepareErrorResponseWithoutData("REST service : " + serviceName + "Problem while authorizing user : " + user.getLoginId(), "REST service : " + serviceName + "Problem while authorizing user : " + user.getLoginId());
			} catch (Exception r) {
				log.error("Problem while authorizing user : " + user.getLoginId(), e);
			}
		}
		return null;
	}
	
	private JSONObject authenticateUser(String userName, String serviceName) {
		
		//boolean atlasUserAuthenticationRequired = true;
		try {
			UserList userList = null;
			if (atlasUserAuthenticationRequired != null && atlasUserAuthenticationRequired.equalsIgnoreCase("yes")) {
				if(userName == null || userName.trim().isEmpty()) {
					log.info("REST service : " + serviceName + " : Registered user name is missing. It is mandatory");
					return prepareErrorResponseWithoutData("User is required","Registered user name is missing. It is mandatory");
				} else {
					userList = userListService.getUserByLoginId(userName);
					if (userList == null) {
						log.info("REST service : " + serviceName + " : Username is not a registered user : " + userName);
						return prepareErrorResponseWithoutData( "User is invalid", "User name specified : "  + userName + "  is invalid");
					}
				}
				log.info("REST service : " + serviceName + " : Username is a valid user : " + userName);
				return null;
			}
		} catch (Exception e) {
			log.error("Problem while validating user : " + userName, e);
			try {
				return prepareErrorResponseWithoutData("REST service : " + serviceName + "Problem while validating user : " + userName, "REST service : " + serviceName + "Problem while validating user : " + userName);
			} catch (Exception r) {
				log.error("Problem while validating user : " + userName, e);
			}
		}
		return null;
	}
	
	private JSONObject prepareErrorResponseWithoutData(String message, String failureDetails) {
		
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("result","ERROR");
			responseJson.put("status", "400");			
			responseJson.put("message",message);
			responseJson.put("Failure_Details", failureDetails);
			responseJson.put("data", "");
		} catch (Exception e){
			log.error("Problem while preparing JSON Response", e);
		}
		return responseJson;
	}
	
private JSONObject prepareSuccessResponse(String message, String data) {
		
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("result","OK");
			responseJson.put("status", "200");			
			responseJson.put("message",message);
			responseJson.put("data", data);
		} catch (Exception e){
			log.error("Problem while preparing JSON Response", e);
		}
		return responseJson;
	}
	


private JSONObject prepareSuccessResponseWithJSONArray(String message, JSONArray data) {
	
	JSONObject responseJson = new JSONObject();
	try {
		responseJson.put("result","OK");
		responseJson.put("status", "200");			
		responseJson.put("message",message);
		responseJson.put("data", data);
	} catch (Exception e){
		log.error("Problem while preparing JSON Response", e);
	}
	return responseJson;
}


}
