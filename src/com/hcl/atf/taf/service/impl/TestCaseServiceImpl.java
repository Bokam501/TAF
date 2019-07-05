package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.ProductFeatureDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.TestCasePriorityDAO;
import com.hcl.atf.taf.dao.TestCaseStepsListDAO;
import com.hcl.atf.taf.dao.TestcaseTypeMasterDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.model.EntityRelationshipCommon;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestcaseTypeMaster;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.dto.TestCaseDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPResultSummaryDTO;
import com.hcl.atf.taf.model.json.JsonFeatureTestCaseDefect;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.JsonTestExecutionResultBugList;
import com.hcl.atf.taf.model.json.JsonTestRunPlan;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionResultSummary;
import com.hcl.atf.taf.model.json.terminal.JsonTestExecutionResult;
import com.hcl.atf.taf.scriptgeneration.ScriptGeneratorUtilities;
import com.hcl.atf.taf.service.EntityRelationshipCommonService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestCaseService;

@Service
public class TestCaseServiceImpl  implements TestCaseService{	 
	
	private static final Log log = LogFactory.getLog(TestCaseServiceImpl.class);

	@Autowired
	private TestCaseListDAO testCaseListDAO;
	@Autowired
	private TestCaseStepsListDAO testCaseStepsListDAO;
	@Autowired
	private TestCasePriorityDAO testCasePriorityDAO;
	@Autowired
	private TestcaseTypeMasterDAO testcaseTypeMasterDAO;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private WorkPackageDAO workPackageDAO;
	
	@Autowired
	private ProductFeatureDAO productFeatureDAO;
	
	@Autowired
	private EntityRelationshipCommonService entityRelationshipCommonService;
	
	@Value("#{ilcmProps['TEST_STEP_BATCH_PROCESSING_COUNT']}")
    private String testStepMaxBatchCount;
	
	
	@Override
	@Transactional
	public List<TestCaseList> getTestCaseListByProductId(int productId, Integer jtStartIndex, Integer jtPageSize) {
		return testCaseListDAO.getTestCaseListByProductId(productId, jtStartIndex, jtPageSize);
	}
	
	@Override
	@Transactional
	public TestCaseList getTestCaseById(int testCaseId) {		
		return testCaseListDAO.getByTestCaseId(testCaseId);
	}
	
	@Override
	@Transactional
	public TestCaseList getTestCaseByCode(String testCaseCode) {		
		return testCaseListDAO.getTestCaseByCode(testCaseCode);
	}
	
	@Override
	@Transactional
	public TestCaseList getTestCaseByName(String testCaseName, int productId) {		
		return testCaseListDAO.getTestCaseByName(testCaseName, productId);
	}

	
	@Override
	@Transactional
	public TestCaseList getTestCaseByCode(String testCaseCode, ProductMaster product) {		
		return testCaseListDAO.getTestCaseByCodeProduct(testCaseCode, product.getProductId().intValue());
	}


	@Override
	@Transactional
	public TestCaseStepsList getTestCaseStepByCode(String testStepCode, ProductMaster product) {		
		return testCaseStepsListDAO.getTestCaseStepByCodeProduct(testStepCode, product.getProductId().intValue());
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestCases() {		
		return testCaseListDAO.getTotalRecords();
	}
	
	@Override
	@Transactional
	public int addTestCase(TestCaseList testCaseList) {		
		if (testCaseList.getTestCaseScriptFileName() == null || testCaseList.getTestCaseScriptFileName().trim().isEmpty()) {
			Integer id = null;
			testCaseList.setTestCaseScriptFileName(ScriptGeneratorUtilities.getTestCaseClassName(testCaseList.getTestCaseName(), 0, testCaseList.getTestCaseCode(), 1));
		}
		if (testCaseList.getTestCaseScriptQualifiedName() == null || testCaseList.getTestCaseScriptQualifiedName().trim().isEmpty()) {
			testCaseList.setTestCaseScriptQualifiedName("com.hcl.atf.taf.testcase");
		}
		return testCaseListDAO.add(testCaseList);		
	}
	
	@Override
	@Transactional
	public int addTestCaseReturnId(TestCaseList testCaseList) {
		return testCaseListDAO.add(testCaseList);		
	}
	
	
	@Override
	@Transactional
	public void update(TestCaseList testCaseList) {
		testCaseListDAO.update(testCaseList);		
	}
		
	@Override
	@Transactional
	public void delete(TestCaseList testCaseList) {
		testCaseListDAO.delete(testCaseList);
		
	}
	@Override
	@Transactional
	public void deleteTCWithMappings(TestCaseList testCaseList){
		//Check if TC mapped to Feature, TestSuite, Version	
		if(testCaseList.getProductFeature() != null && testCaseList.getProductFeature().size() >0){					
			unmapTestcaseFromFeatureMapping(testCaseList.getTestCaseId());
		}
		if(testCaseList.getProductVersionList() != null && testCaseList.getProductVersionList().size() >= 0){					
			testCaseListDAO.unmapTestcaseFromVersionMapping(testCaseList.getTestCaseId());
		}
		if(testCaseList.getTestSuiteLists() != null && testCaseList.getTestSuiteLists().size() >= 0){
			testCaseListDAO.unmapTestcaseFromTestSuiteMapping(testCaseList.getTestCaseId());				
		}
		if(testCaseList.getDecouplingCategory() != null && testCaseList.getDecouplingCategory().size() >= 0){
			testCaseListDAO.unmapTestcaseFromDecouplingCategoryMapping(testCaseList.getTestCaseId());				
		}
		testCaseListDAO.deleteTCWithMappings(testCaseList);
	}
	
	@Override
	@Transactional
	public int unmapTestcaseFromFeatureMapping(Integer testCaseId){		
		return testCaseListDAO.unmapTestcaseFromFeatureMapping(testCaseId);
	}
	
	@Override
	@Transactional
	public List<TestCaseStepsList> listTestCaseSteps(int testCaseId) {
		
		return testCaseStepsListDAO.list(testCaseId);
	}
	
	@Override
	@Transactional
	public int getTotalRecordsOfTestCaseSteps() {
		
		return testCaseStepsListDAO.getTotalRecords();
	}

	@Override
	@Transactional
	public TestSuiteList updateTestCasesTestSuites(int testSuiteId,
			int testCaseId) {
		return testCaseListDAO.updateTestCasesTestSuites(testSuiteId, testCaseId);
	}

	@Override
	@Transactional
	public TestCaseList updateTestSuiteTestCasesOneToMany(int testCaseId,
			int testSuiteId, String maporunmap) {
		return testCaseListDAO.updateTestSuiteTestCasesOneToMany(testCaseId, testSuiteId, maporunmap);
	}
	
	@Override
	@Transactional
	public List<TestCaseList> getTestCaseByProductVersionId(int startIndex,
			int pageSize, int productVersionListId) {
		return testCaseListDAO.getTestCaseByProductVersionId(startIndex, pageSize, productVersionListId);
	}
	
	@Override
	@Transactional
	public List<TestCasePriority> listTestCasePriority() {
		return testCasePriorityDAO.list();
	}

	@Override
	@Transactional
	public TestCasePriority getTestCasePriorityBytestcasePriorityId(
			int testcasePriorityId) {
		return testCasePriorityDAO.getTestCasePriorityBytestcasePriorityId(testcasePriorityId);
	}

	@Override
	@Transactional
	public List<TestcaseTypeMaster> listTestcaseTypeMaster() {
		return testcaseTypeMasterDAO.list();
	}
	
	@Override
	@Transactional
	public TestcaseTypeMaster getTestcaseTypeMasterBytestcaseTypeId(
			int testcaseTypeId) {
		return testcaseTypeMasterDAO.getTestcaseTypeMasterBytestcaseTypeId(testcaseTypeId);
	}

	@Override
	@Transactional
	public TestcaseTypeMaster getTestcaseTypeMasterByName(String name){
		return testcaseTypeMasterDAO.getTestcaseTypeMasterByName(name);
	}
	
	@Override
	@Transactional
	public List<TestCaseList> getTestCaseListByProductIdByType(int productId,
			int executionType) {
		return testCaseListDAO.getTestCaseListByProductIdByType(productId,executionType);
	}

	@Override
	public List<ProductVersionListMaster> getTestCasesVersions(int productId,
			int testCaseId) {
		return testCaseListDAO.getTestCasesVersions(productId,testCaseId);
	}

	@Override
	public List<JsonTestCaseList> getTestCaseListWithProductVersionMappingByProductId(Integer productId) {
		
		//Step 1 : Get the testcases for the product
		List<TestCaseList> productTestCases = getTestCaseListByProductId(productId, null, null);
		List<JsonTestCaseList> jsonTestCases = new ArrayList<JsonTestCaseList>();
		if(productTestCases != null){
			log.info("Total test cases for product are : " + productTestCases.size());
			jsonTestCases = new ArrayList<JsonTestCaseList>();
			JsonTestCaseList jsonTestCase  = null;
			for (TestCaseList testCase : productTestCases) {
				jsonTestCase = new JsonTestCaseList(testCase);
				log.info("Testcase ID : " + testCase.getTestCaseId());
				jsonTestCase.setAllVersionMappingsStatus("0");
				jsonTestCases.add(jsonTestCase);
			}
			//Step 2 : Get the existing product versions
			List<ProductVersionListMaster> productVersions = (List) productListService.listProductVersion(productId);
			log.info("Total product versions for product are : " + productVersions.size());
			
			//Step 3 : Iterate through the testcases of each product verison and update the mapping into the jsonTestcase object
			//The key requirement here is that the product versions have to be ordered in ascending order of IDs.
			//The same should be followed in the JSP page. Preferably both JSP and this method should get the versions using the same method.
			int versionNumberCounter = 0;
			int index = -1;
			//Iterate through the product versions
			for (ProductVersionListMaster productVersion : productVersions) {
				log.info("Version : " + productVersion.getProductVersionName());
				//Get the product version test cases
				Set<TestCaseList> testCaseSet = productVersion.getTestCaseLists();
				List<TestCaseList> productVersionMappedTestCases = new ArrayList<TestCaseList>();
				productVersionMappedTestCases.addAll(testCaseSet);
				log.info("Total testcases for version are : " + productVersionMappedTestCases.size());
				//Iterate through the mapped testcases of the product version
				for (TestCaseList mappedTestCase : productVersionMappedTestCases) {
					log.info("Mapped testcase : " + mappedTestCase.getTestCaseName());
					//Get the index of this mapped testcase from the products testcase list
					index = productTestCases.indexOf(mappedTestCase);
					log.info("Index in testcase slist : " + index);
					//If the testcase index is found, then get the corresponding jsontestcase object
					//Assumption here is that the position of the testcase in the producttestcases list and the product jsontestcases list is the same
					if (index >= 0) {
						//Get the corresponding json testcase based on the index of the mapped test case in the product testcases list
						jsonTestCase = jsonTestCases.get(index);
						log.info("Mapped Json testcase : " + jsonTestCase.getTestCaseName());
					}
					//Update the mapping value for the product version in the jsontestcase object
					jsonTestCase = updateTestCaseProductVersionMappingInformation(jsonTestCase, versionNumberCounter);
					//Add the updated jsontestcase object back into the list of jsontestcases at the right position
					jsonTestCases.set(index, jsonTestCase);
				}
				versionNumberCounter++;
			}
		}
		
		return jsonTestCases;
	}	
	
	private JsonTestCaseList updateTestCaseProductVersionMappingInformation(JsonTestCaseList jsonTestCase, int versionNumbercounter) {
	
		if (versionNumbercounter == 0) {
			jsonTestCase.setVer1("1");
			log.info("Updated version 1");
			return jsonTestCase;
		} else if (versionNumbercounter == 1) {
			jsonTestCase.setVer2("1");
			log.info("Updated version 2");
			return jsonTestCase;
		} else if (versionNumbercounter == 2) {
			jsonTestCase.setVer3("1");
			log.info("Updated version 3");
			return jsonTestCase;
		} else if (versionNumbercounter == 3) {
			jsonTestCase.setVer4("1");
			log.info("Updated version 4");
			return jsonTestCase;
		} else if (versionNumbercounter == 4) {
			jsonTestCase.setVer5("1");
			log.info("Updated version 5");
			return jsonTestCase;
		} else if (versionNumbercounter == 5) {
			jsonTestCase.setVer6("1");
			log.info("Updated version 6");
			return jsonTestCase;
		} else if (versionNumbercounter == 6) {
			jsonTestCase.setVer7("1");
			log.info("Updated version 7");
			return jsonTestCase;
		} else if (versionNumbercounter == 7) {
			jsonTestCase.setVer8("1");
			log.info("Updated version 8");
			return jsonTestCase;
		} else if (versionNumbercounter == 8) {
			jsonTestCase.setVer9("1");
			log.info("Updated version 9");
			return jsonTestCase;
		} else if (versionNumbercounter == 9) {
			jsonTestCase.setVer10("1");
			log.info("Updated version 10");
			return jsonTestCase;
		} else if (versionNumbercounter == 10) {
			jsonTestCase.setVer11("1");
			log.info("Updated version 11");
			return jsonTestCase;
		} else if (versionNumbercounter == 11) {
			jsonTestCase.setVer12("1");
			log.info("Updated version 12");
			return jsonTestCase;
		} else if (versionNumbercounter == 12) {
			jsonTestCase.setVer13("1");
			log.info("Updated version 13");
			return jsonTestCase;
		} else if (versionNumbercounter == 13) {
			jsonTestCase.setVer14("1");
			log.info("Updated version 14");
			return jsonTestCase;
		} else if (versionNumbercounter == 14) {
			jsonTestCase.setVer15("1");
			log.info("Updated version 15");
			return jsonTestCase;
		} else if (versionNumbercounter == 15) {
			jsonTestCase.setVer16("1");
			log.info("Updated version 16");
			return jsonTestCase;
		} else if (versionNumbercounter == 16) {
			jsonTestCase.setVer17("1");
			log.info("Updated version 17");
			return jsonTestCase;
		} else if (versionNumbercounter == 17) {
			jsonTestCase.setVer18("1");
			log.info("Updated version 18");
			return jsonTestCase;
		} else if (versionNumbercounter == 18) {
			jsonTestCase.setVer19("1");
			log.info("Updated version 19");
			return jsonTestCase;
		} else if (versionNumbercounter == 19) {
			jsonTestCase.setVer20("1");
			log.info("Updated version 20");
			return jsonTestCase;
		} 		
		return jsonTestCase;
	}

	@Override
	@Transactional
	public TestCaseList processTestExecutionResult(TestCaseList testCaseList, JsonTestExecutionResult jsonTestCase) {
		return testCaseListDAO.processTestExecutionResult(testCaseList, jsonTestCase);
	}
	
	@Override
	@Transactional
	public TestCaseList processTestExecutionResult(TestCaseList testCaseList, JsonTestExecutionResult jsonTestCase, Integer testSuiteId) {
		return testCaseListDAO.processTestExecutionResult(testCaseList, jsonTestCase, testSuiteId);
	}

	@Override
	@Transactional
	public List<TestCaseList> getTestCasesMappedToFeature(
			Integer productFeatureId) {		
		return testCaseListDAO.getTestCasesMappedToFeature(productFeatureId);
	}

	@Override
	@Transactional
	public List<JsonFeatureTestCaseDefect> getFeatureTestCaseDefect(
			Integer productId, List<ProductFeature> featureList) {
		List<TestCaseList> mappedTestCaseList = new ArrayList<TestCaseList>();
		List<JsonFeatureTestCaseDefect> jsonFTDefectList = new ArrayList<JsonFeatureTestCaseDefect>();
		
		if(featureList != null && !featureList.isEmpty()){
			JsonFeatureTestCaseDefect jftDefect = null;
			for (ProductFeature productFeature : featureList) {
				mappedTestCaseList.addAll(productFeature.getTestCaseList());
				if(productFeature.getTestCaseList() != null && !productFeature.getTestCaseList().isEmpty()){
					for(TestCaseList tc : productFeature.getTestCaseList()){
						jftDefect = new JsonFeatureTestCaseDefect();
						jftDefect.setProductFeatureId(productFeature.getProductFeatureId());
						jftDefect.setProductFeatureName(productFeature.getProductFeatureName());
						jftDefect.setProductFeatureCode(productFeature.getProductFeatureCode());
						jftDefect.setTestcaseId(tc.getTestCaseId());
						jftDefect.setTestcaseName(tc.getTestCaseName());
						int newcount = 0;
						int referBackcount = 0;
						int reviewedcount = 0;
						int approvedcount = 0;
						int closedcount = 0;
						int totaldefectscount =0;
						int totalexecutionResultCount = 0;
						TestCaseList tcObj = getTestCaseById(tc.getTestCaseId());
						Set<WorkPackageTestCaseExecutionPlan>  wtcp = tcObj.getWorkPackageTestCaseExecutionPlan();
						for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : wtcp) {
							if(workPackageTestCaseExecutionPlan.getIsExecuted() != 0){
								++totalexecutionResultCount;
								if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult() != null ){
									if( workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestExecutionResultBugListSet() != null){
										Set<TestExecutionResultBugList> terbSet = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestExecutionResultBugListSet();
										for (TestExecutionResultBugList testExecutionResultBugList : terbSet) {											
											if(testExecutionResultBugList.getBugFilingStatus().getWorkFlowId() ==1){
												++newcount;
											}else if(testExecutionResultBugList.getBugFilingStatus().getWorkFlowId() ==2){
												++referBackcount;
											}else if(testExecutionResultBugList.getBugFilingStatus().getWorkFlowId() ==3){
												++reviewedcount;
											}else if(testExecutionResultBugList.getBugFilingStatus().getWorkFlowId() ==4){
												++approvedcount;
											}else if(testExecutionResultBugList.getBugFilingStatus().getWorkFlowId() ==5){
												++closedcount;
											}
										}	
									}
									
								}	
							}														
						}
						totaldefectscount = newcount+referBackcount+reviewedcount+approvedcount+closedcount;
						jftDefect.setOpenDefects(String.valueOf(newcount));
						jftDefect.setReferBackDefects(String.valueOf(referBackcount));
						jftDefect.setReviewedDefects(String.valueOf(reviewedcount));
						jftDefect.setApprovedDefects(String.valueOf(approvedcount));
						jftDefect.setClosedDefects(String.valueOf(closedcount));
						jftDefect.setTotalDefects(String.valueOf(totaldefectscount));
						jftDefect.setTestExecutionCount(String.valueOf(totalexecutionResultCount));
						jsonFTDefectList.add(jftDefect);
						}			
				}
			}
		}		
		return jsonFTDefectList;
	}
	
	@Override
	@Transactional
	public List<JsonTestExecutionResultBugList> getFeatureTestCaseDefectList(Integer testCaseId) {
		List<JsonTestExecutionResultBugList> jsonTERBugList = new ArrayList<JsonTestExecutionResultBugList>();
		TestCaseList tcObj = getTestCaseById(testCaseId);
		Set<WorkPackageTestCaseExecutionPlan> wtcp = tcObj
				.getWorkPackageTestCaseExecutionPlan();
		for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : wtcp) {
			if (workPackageTestCaseExecutionPlan.getTestCaseExecutionResult() != null) {
				if (workPackageTestCaseExecutionPlan
						.getTestCaseExecutionResult()
						.getTestExecutionResultBugListSet() != null) {
					Set<TestExecutionResultBugList> terbSet = workPackageTestCaseExecutionPlan
							.getTestCaseExecutionResult()
							.getTestExecutionResultBugListSet();

					if (terbSet != null) {
						for (TestExecutionResultBugList tERBugList : terbSet) {
							jsonTERBugList.add(new JsonTestExecutionResultBugList(tERBugList));
						}
					}
				}

			}
		}
		return jsonTERBugList;
	}

	
@Override
@Transactional
public HashMap<Integer, JsonWorkPackageTestCaseExecutionPlanForTester> getFeatureTestCaseExecutionResultList(Integer testCaseId) {
	log.info("get FeatureTestCaseExecutionResultList of TestCase --"+testCaseId);

	HashMap<Integer, JsonWorkPackageTestCaseExecutionPlanForTester> listOfWPTEResult = new HashMap<Integer, JsonWorkPackageTestCaseExecutionPlanForTester>();
	
	List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEPList = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
	JsonWorkPackageTestCaseExecutionPlanForTester jsonWPTCEPTesterObj = new JsonWorkPackageTestCaseExecutionPlanForTester();
	TestCaseList tcObj = getTestCaseById(testCaseId);
	Set<WorkPackageTestCaseExecutionPlan> wtcpSet = tcObj
			.getWorkPackageTestCaseExecutionPlan();
	
	for (WorkPackageTestCaseExecutionPlan wptcepObj : wtcpSet) {
		if(wptcepObj.getIsExecuted() == 1){
			jsonWPTCEPTesterObj = new JsonWorkPackageTestCaseExecutionPlanForTester();
			Integer wpId = wptcepObj.getWorkPackage().getWorkPackageId();
			jsonWPTCEPTesterObj.setWorkPackageId(wpId);
			jsonWPTCEPTesterObj.setWorkPackageName(wptcepObj.getWorkPackage().getName());
			if(wptcepObj.getWorkPackage().getProductBuild() != null){
				if(wptcepObj.getWorkPackage().getProductBuild().getProductVersion() != null){
					int prodVerId = wptcepObj.getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();	
					String prodVerName = wptcepObj.getWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
					jsonWPTCEPTesterObj.setProductVersionId(prodVerId);
					jsonWPTCEPTesterObj.setProductVersionName(prodVerName);
				}					
			}	
			int passed=0;
			int failed=0;
			int notrun=0;
			int blocked=0;
			if (wptcepObj.getTestCaseExecutionResult() != null) {
				String tcresult = wptcepObj.getTestCaseExecutionResult().getResult();
				if(tcresult.equals("1") || tcresult.equals("PASS") || tcresult.equals(IDPAConstants.EXECUTION_RESULT_PASSED)){
					++passed;
				}else if(tcresult.equals("2") || tcresult.equals("FAIL") || tcresult.equals(IDPAConstants.EXECUTION_RESULT_FAILED)){
					++failed;
				}else if(tcresult.equals("3")  || tcresult.equals(IDPAConstants.EXECUTION_RESULT_NORUN)){
					++notrun;
				}else if(tcresult.equals("4") || tcresult.equals(IDPAConstants.EXECUTION_RESULT_BLOCKED)){
					++blocked;
				}
				jsonWPTCEPTesterObj.setP2totalPass(passed);
				jsonWPTCEPTesterObj.setP2totalFail(failed);
				jsonWPTCEPTesterObj.setP2totalNoRun(notrun);
				jsonWPTCEPTesterObj.setP2totalBlock(blocked);
				jsonWPTCEPTesterObj.setTotalExecutedTesCases(passed+failed+notrun+blocked);
			}			
			int tempPassed = 0;
			int tempFailed = 0;
			int tempNotRun = 0;
			int tempBlocked = 0;
			//Adding Json to List categroized below
			if(listOfWPTEResult.size() == 0){
				listOfWPTEResult.put(wpId, jsonWPTCEPTesterObj);
				log.debug("Adding 1st element");
			}else if(listOfWPTEResult.containsKey(wpId)){
				jsonWPTCEPTesterObj = listOfWPTEResult.get(wpId);
				
				tempPassed = jsonWPTCEPTesterObj.getP2totalPass()+passed;
				jsonWPTCEPTesterObj.setP2totalPass(tempPassed);
				
				tempFailed  = jsonWPTCEPTesterObj.getP2totalFail()+failed;
				jsonWPTCEPTesterObj.setP2totalFail(tempFailed);
				
				tempNotRun = jsonWPTCEPTesterObj.getP2totalNoRun()+notrun;
				jsonWPTCEPTesterObj.setP2totalNoRun(tempNotRun);
				
				tempBlocked = jsonWPTCEPTesterObj.getP2totalBlock()+blocked;
				jsonWPTCEPTesterObj.setP2totalBlock(tempBlocked);
				
				jsonWPTCEPTesterObj.setTotalExecutedTesCases(tempPassed+tempFailed+tempNotRun+tempBlocked);
				listOfWPTEResult.put(wpId, jsonWPTCEPTesterObj);
			}else if(!listOfWPTEResult.containsKey(wpId)){
								
				jsonWPTCEPTesterObj.setP2totalPass(passed);
				jsonWPTCEPTesterObj.setP2totalFail(failed);				
				jsonWPTCEPTesterObj.setP2totalNoRun(notrun);
				jsonWPTCEPTesterObj.setP2totalBlock(blocked);
				jsonWPTCEPTesterObj.setTotalExecutedTesCases(passed+failed+notrun+blocked);
				listOfWPTEResult.put(wpId, jsonWPTCEPTesterObj);
				log.debug("Directly Adding element-- to wpid--"+wpId+"-- WPObj --"+wptcepObj.getId());
			}
		}else{
			log.info("TC--"+testCaseId+"-- not executed for WP id	--	"+wptcepObj.getId());
		}		
	}
	return listOfWPTEResult;
}

@Override
@Transactional
public List<JsonWorkPackageTestCaseExecutionResultSummary> getTestCaseExecutionResultSummary(Integer testCaseId, Integer workPackageId, Integer productId, String dataLevel) {
	log.info("getTestCaseExecutionResultSummary--"+testCaseId);
	List<JsonWorkPackageTestCaseExecutionResultSummary>  jsonWPTCEPTObjList = new ArrayList<JsonWorkPackageTestCaseExecutionResultSummary>();
	List<WorkPackageTCEPResultSummaryDTO> wptcepResultList = testCaseListDAO.getWPTestCaseExecutionResultSummary(testCaseId, workPackageId, productId, dataLevel);
	JsonWorkPackageTestCaseExecutionResultSummary jsonWPTCEPTesterObj = new JsonWorkPackageTestCaseExecutionResultSummary();	
	Map<Integer, ProductVersionListMaster> prodVersionMap=new HashMap<Integer,ProductVersionListMaster>();
	Map<Integer, ProductBuild> prodBuildMap=new HashMap<Integer,ProductBuild>();	
	int tabId=1;
	TestRunJob testRunJobObj=null;
	ProductVersionListMaster prodversionObj = null;
	ProductBuild productBuildObj=null;
	 TestCaseDTO testCaseDto=new TestCaseDTO();
	 int totalNotExecutedTC=0;
	 int notExecutedTC=0;
	 int index=-1;
	 int totalExecutedTCs=0;
	 int failCount=0;
	 int noRunCount=0;
	 int blockedCount=0;
	 Integer totalExecutionPlanTestCase=0;	
	if(wptcepResultList.size()!=0){
		for (WorkPackageTCEPResultSummaryDTO workPackageTCEPResultSummaryDTO : wptcepResultList) {	
			testRunJobObj=new TestRunJob();
				String resultId=workPackageTCEPResultSummaryDTO.getResult();
				Integer totalResult=workPackageTCEPResultSummaryDTO.getTotalResultCount();
			productBuildObj = new ProductBuild();
				Integer productBuildId= workPackageTCEPResultSummaryDTO.getProductBuildId();
				productBuildObj.setProductBuildId(productBuildId);
			
				if(!prodBuildMap.containsKey(productBuildId)){
					++index;
					totalNotExecutedTC=0;
					notExecutedTC=0;
					totalExecutedTCs=0;
					totalExecutionPlanTestCase=0;
					jsonWPTCEPTesterObj = new JsonWorkPackageTestCaseExecutionResultSummary();
					jsonWPTCEPTesterObj.setProductVersionId(workPackageTCEPResultSummaryDTO.getProductVersionId());
					jsonWPTCEPTesterObj.setProductVersionName(workPackageTCEPResultSummaryDTO.getProductVersionName());
					jsonWPTCEPTesterObj.setProductBuildId(workPackageTCEPResultSummaryDTO.getProductBuildId());
					jsonWPTCEPTesterObj.setProductBuildName(workPackageTCEPResultSummaryDTO.getProductBuildName());
					jsonWPTCEPTesterObj.setNotExecuted(0);
					jsonWPTCEPTesterObj.setTotalBlock(0);
					jsonWPTCEPTesterObj.setTotalPass(0);
					jsonWPTCEPTesterObj.setTotalFail(0);
					jsonWPTCEPTesterObj.setTotalNoRun(0);
					jsonWPTCEPTObjList.add(index, jsonWPTCEPTesterObj);
					prodBuildMap.put(productBuildId,productBuildObj);
				}
			if(resultId==null || resultId.equals("")){					
				totalNotExecutedTC= totalResult;
					 jsonWPTCEPTesterObj.setNotExecuted(totalNotExecutedTC);
			}else if(resultId.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_PASSED)  ){
					 jsonWPTCEPTesterObj.setTotalPass(totalResult);
				 }else if(resultId.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_FAILED) ){
					 jsonWPTCEPTesterObj.setTotalFail(totalResult);
				 }else if(resultId.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_NORUN) ){
					 jsonWPTCEPTesterObj.setTotalNoRun(totalResult);
				 }else if(resultId.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_BLOCKED) ){
					 jsonWPTCEPTesterObj.setTotalBlock(totalResult);					 
				 }
			 	jsonWPTCEPTObjList.set(index, jsonWPTCEPTesterObj);			
			}
	}
	return jsonWPTCEPTObjList;
}
	@Override
	@Transactional
	public Integer getTestCaseListSize(Integer productId) {
		return testCaseListDAO.getTestCaseListSize(productId);
	}
	

@Override
@Transactional
public List<JsonFeatureTestCaseDefect> getFeatureTestCaseDefectCount(Integer testCaseId) {
	List<JsonFeatureTestCaseDefect> jsonFTDefectList = new ArrayList<JsonFeatureTestCaseDefect>();
	
		JsonFeatureTestCaseDefect jftDefect = null;
			if(testCaseId != null && testCaseId !=0){
					jftDefect = new JsonFeatureTestCaseDefect();
					int newcount = 0;
					int referBackcount = 0;
					int reviewedcount = 0;
					int approvedcount = 0;
					int closedcount = 0;
					int totaldefectscount =0;
					int totalexecutionResultCount = 0;
					TestCaseList tcObj = getTestCaseById(testCaseId);
					jftDefect.setTestcaseId(tcObj.getTestCaseId());
					jftDefect.setTestcaseName(tcObj.getTestCaseName());
					Set<WorkPackageTestCaseExecutionPlan>  wtcp = tcObj.getWorkPackageTestCaseExecutionPlan();
					if(wtcp != null && !wtcp.isEmpty()){
						for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : wtcp) {
							if(workPackageTestCaseExecutionPlan.getIsExecuted() != 0){
								++totalexecutionResultCount;
								if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult() != null ){
									if( workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestExecutionResultBugListSet() != null){
										if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
											jftDefect.setProductVersionId(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId());
											jftDefect.setProductVersionName(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionName());
										}
										Set<TestExecutionResultBugList> terbSet = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestExecutionResultBugListSet();
										for (TestExecutionResultBugList testExecutionResultBugList : terbSet) {											
											if(testExecutionResultBugList.getBugFilingStatus().getWorkFlowId() ==1){
												++newcount;
											}else if(testExecutionResultBugList.getBugFilingStatus().getWorkFlowId() ==2){
												++referBackcount;
											}else if(testExecutionResultBugList.getBugFilingStatus().getWorkFlowId() ==3){
												++reviewedcount;
											}else if(testExecutionResultBugList.getBugFilingStatus().getWorkFlowId() ==4){
												++approvedcount;
											}else if(testExecutionResultBugList.getBugFilingStatus().getWorkFlowId() ==5){
												++closedcount;
											}
										}	
									}
									
								}	
							}														
						}
						totaldefectscount = newcount+referBackcount+reviewedcount+approvedcount+closedcount;
						jftDefect.setOpenDefects(String.valueOf(newcount));
						jftDefect.setReferBackDefects(String.valueOf(referBackcount));
						jftDefect.setReviewedDefects(String.valueOf(reviewedcount));
						jftDefect.setApprovedDefects(String.valueOf(approvedcount));
						jftDefect.setClosedDefects(String.valueOf(closedcount));
						jftDefect.setTotalDefects(String.valueOf(totaldefectscount));
						jftDefect.setTestExecutionCount(String.valueOf(totalexecutionResultCount));
						jsonFTDefectList.add(jftDefect);
					}
					
			}
	return jsonFTDefectList;
}
	@Override
	@Transactional
	public Integer getUnMappedFeatureCountOfTestCaseByTestCaseId(
		int productId, int testCaseId) {
	return testCaseListDAO.getUnMappedFeatureCountOfTestCaseByTestCaseId(productId, testCaseId);
	}
	
	@Override
	@Transactional
	public List<Object[]> getUnMappedFeatureByTestCaseId(int productId, int testCaseId, Integer jtStartIndex, Integer jtPageSize) {
		return testCaseListDAO.getUnMappedFeatureByTestCaseId(productId, testCaseId, jtStartIndex, jtPageSize);
	}	
	@Override
	@Transactional
	public List<Object[]> getMappedTestCaseListByProductFeatureId(int productId,
			int productFeatureId, Integer jtStartIndex, Integer jtPageSize){
		return testCaseListDAO.getMappedTestCaseListByProductFeatureId(productId,  productFeatureId, jtStartIndex, jtPageSize);
	}

	@Override
	public List<Object[]> getMappedFeatureBytestCaseId(int productId,
			int testCaseId, Integer jtStartIndex, Integer jtPageSize) {
		return testCaseListDAO.getMappedFeatureBytestCaseId(productId, testCaseId, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public List<String> getExistingTestStepsCodes(TestCaseList testCase, ProductMaster product) {
		
		return testCaseListDAO.getExistingTestStepsCodes(testCase,product);
	}

	@Override
	@Transactional
	public List<String> getExistingTestStepsNames(TestCaseList testCase, ProductMaster product) {
		return testCaseListDAO.getExistingTestStepsNames(testCase,product);
	}
	
	@Override
	@Transactional
	public int addTestStepsBulk(List<TestCaseStepsList> testStepsList) {
		return testCaseStepsListDAO.addBulk(testStepsList, Integer.parseInt(testStepMaxBatchCount));
	}

	@Override
	@Transactional
	public TestCasePriority getPrioirtyByName(String testcasePriorityName) {
		return testCasePriorityDAO.getPrioirtyByName(testcasePriorityName);
	}

	@Override
	@Transactional
	public List<String> getExistingTestCaseCodes(ProductMaster product) {
		return testCaseListDAO.getExistingTestCaseCodes(product);
	}

	@Override
	@Transactional
	public List<String> getExistingTestCaseNames(ProductMaster product) {
		return testCaseListDAO.getExistingTestCaseNames(product);
	}

	/*
	 * This method returns all the TestRunPlans that the test case is part of through it's test suites
	 */
	@Override
	@Transactional
	public List<JsonTestRunPlan> getTestCaseTestRunPlans(Integer testCaseId) {
		
		try {
			TestCaseList testCase = getTestCaseById(testCaseId);
			if (testCase == null)
				return null;
			
			Set<TestSuiteList> testSuites = testCase.getTestSuiteLists();
			if (testSuites == null || testSuites.isEmpty())
				return null;
			
			List<JsonTestRunPlan> jsonTestRunPlans = new ArrayList<JsonTestRunPlan>();
			for (TestSuiteList testSuite : testSuites) {
				
				Set<TestRunPlan> testRunPlans = testSuite.getTestRunPlanList();
				if (testRunPlans == null)
					continue;
				for (TestRunPlan testRunPlan : testRunPlans) {
					jsonTestRunPlans.add(new JsonTestRunPlan(testRunPlan));
				}
			}
			return jsonTestRunPlans;
		} catch (Exception e) {
			log.error("Unable to get TestRunPlans for Testcase : " + testCaseId);
			return null;
		}
	}

	@Override
	@Transactional
	public List<Object[]> getUnMappedTestScriptsByProductId(int productId, int testCaseId, int jtStartIndex, int jtPageSize) {
		return testCaseListDAO.getUnMappedTestScriptsByProductId(productId,testCaseId,jtStartIndex,jtPageSize);
	}

	@Override
	@Transactional
	public List<Object[]> getMappedTestScriptsByTestCaseId(int testCaseId) {
		return testCaseListDAO.getMappedTestScriptsByTestCaseId(testCaseId);
	}

	@Override
	@Transactional
	public int getUnMappedTestScriptsCountByProductId(int productId, int testCaseId) {
		return testCaseListDAO.getUnMappedTestScriptsCountByProductId(productId,testCaseId);
	}

	@Override
	@Transactional
	public void updateTestCaseScriptToTestCase(Integer testCaseId, Integer scriptId, String maporunmap) {
		testCaseListDAO.updateTestCaseScriptToTestCase(testCaseId,scriptId,maporunmap);
	}

	@Override
	@Transactional
	public int addBulkTestCases(List<TestCaseList> testCaseList) {
		try {
		return testCaseListDAO.addBulkTestCases(testCaseList, Integer.parseInt(testStepMaxBatchCount));
		}catch(Exception e) {
			
		}
		return 0;
	}
	
	
	
	
	@Override
	@Transactional
	public void updateTestCasePredecessors(TestCaseList testcase, String predecessorsString ) {
		
		if (testcase == null)
			return;
		
		//Tokenize the string into testcaese Ids
		String[] predecessorIdsArray = predecessorsString.trim().split(",");
		if (predecessorsString == "" || predecessorsString.isEmpty()) {
			//TODO : Delete all existing predecessors
			deleteAllPredecessorForTestCase(testcase);
			return;
		}
		Set<Integer> predecessorIdsList = new HashSet<Integer>();
		
		//Get the current list of predecessors for the testcase
		Set<Integer> existingPredecessorIdsList = getTestCasePredecessorIdsList(testcase); 
	
		//Add new testcases to the predecessor list
		Integer id = null;
		for(String predecessorId : predecessorIdsArray) {
			id = new Integer(predecessorId);
			predecessorIdsList.add(id);
			if ((existingPredecessorIdsList != null && existingPredecessorIdsList.size() >0) && existingPredecessorIdsList.contains(id)) {
				//This id is already a predecessor of the testcase
				//Do nothing
			} else {
				addNewPredecessorToTestCase(testcase, id);
			}
		}
		
		//Check for deleted predecessors
		for(Integer existingPredecessorId : existingPredecessorIdsList) {
			if(predecessorIdsList.contains(existingPredecessorId)) {
				//Do nothing
			} else {
				//This id has been removed from the predecessors list in the UI
				//Remove the relationship
				deletePredecessorForTestCase(testcase, existingPredecessorId);
			}
		}
	}
	
	public Set<Integer> getTestCasePredecessorIdsList(TestCaseList testCase) {
		
		Set<Integer> predecessorIds = new HashSet<Integer>();
		List<EntityRelationshipCommon> relationships = entityRelationshipCommonService.getEntityRelationships(IDPAConstants.ENTITY_TEST_CASE_ID, testCase.getTestCaseId(), IDPAConstants.ENTITY_RELATIONSHIP_TYPE_PREDECESSOR, IDPAConstants.ENTITY_RELATIONSHIP_SUBTYPE_FINISH_START);
		if(relationships != null && relationships.size() >0) {
			for(EntityRelationshipCommon relationship :relationships) {
				predecessorIds.add(relationship.getTargetEntityInstanceId());
			}
		}
		return predecessorIds;
	}

	public void addNewPredecessorToTestCase(TestCaseList testCase, Integer predecessorId) {
		entityRelationshipCommonService.addEntityRelationship(IDPAConstants.ENTITY_TEST_CASE_ID, testCase.getTestCaseId(), IDPAConstants.ENTITY_RELATIONSHIP_TYPE_PREDECESSOR, IDPAConstants.ENTITY_RELATIONSHIP_SUBTYPE_FINISH_START, IDPAConstants.ENTITY_TEST_CASE_ID, predecessorId);
	}

	public void deletePredecessorForTestCase(TestCaseList testCase, Integer predecessorId) {
		entityRelationshipCommonService.deleteEntityRelationship(IDPAConstants.ENTITY_TEST_CASE_ID, testCase.getTestCaseId(), IDPAConstants.ENTITY_RELATIONSHIP_TYPE_PREDECESSOR, IDPAConstants.ENTITY_RELATIONSHIP_SUBTYPE_FINISH_START, IDPAConstants.ENTITY_TEST_CASE_ID, predecessorId);
	}

	public void deleteAllPredecessorForTestCase(TestCaseList testCase) {
		entityRelationshipCommonService.deleteEntityRelationships(IDPAConstants.ENTITY_TEST_CASE_ID, testCase.getTestCaseId(), IDPAConstants.ENTITY_RELATIONSHIP_TYPE_PREDECESSOR, IDPAConstants.ENTITY_RELATIONSHIP_SUBTYPE_FINISH_START);
	}

	@Override
	@Transactional
	public Integer getMappedFeatureCountByTestcaseId(int testCaseId) {
		try {
			return productFeatureDAO.getMappedFeatureCountByTestcaseId(testCaseId);
		}catch(Exception e) {
			log.error("Error in getMappedFeatureCountByTestcaseId",e);
		}
		return null;
	}
	

	@Override
	@Transactional
	public List<TestCaseList> getTestCasePredecessorByTestCaseId(Integer testCaseId) {
		List<TestCaseList> predecessorstestCaseList = new ArrayList<TestCaseList>();
			try {	
				Set<Integer> predecessorIds = new HashSet<Integer>();
				List<EntityRelationshipCommon> relationships = entityRelationshipCommonService.getEntityRelationships(IDPAConstants.ENTITY_TEST_CASE_ID, testCaseId, IDPAConstants.ENTITY_RELATIONSHIP_TYPE_PREDECESSOR, IDPAConstants.ENTITY_RELATIONSHIP_SUBTYPE_FINISH_START);
				if(relationships != null && relationships.size() >0) {
					for(EntityRelationshipCommon relationship :relationships) {
						predecessorIds.add(relationship.getTargetEntityInstanceId());
					}
				}
				
				if(predecessorIds != null && predecessorIds.size() >0) {
					predecessorstestCaseList=testCaseListDAO.listTescaseforPredecessors(predecessorIds);
				}
			} catch(Exception e) {
				log.error("Error while getTestCasePredecessorByTestCaseId ",e);
	    	}
			
			return predecessorstestCaseList;
	}

	@Override
	@Transactional
	public List<TestCaseList> getSimilarToTestCaseSByTestCaseId(
			Integer testCaseId) {
		return null;
	}

	@Override
	@Transactional
	public List<TestCaseStepsList> listTestCaseStepsByStatus(int testCaseId,Integer status) {
		try {
			return testCaseStepsListDAO.getTestStepListByStatus(testCaseId, status);
		}catch(Exception e) {
			
		}
		return null;
	}

	@Override
	@Transactional
	public TestCaseList getTestCaseByNameAndCode(String testCaseName,String testCaseCode, int productId) {
		try {
			return testCaseListDAO.getTestCaseByNameAndCode(testCaseName, testCaseCode, productId);
		}catch(Exception e) {
			
		}
		return null;
	}
	
	@Override
	@Transactional
	public TestCaseList getSimilarTestCasesByName(String testCaseName, int productId) {		
		return testCaseListDAO.getSimilarTestCasesByName(testCaseName, productId);
	}
	
	@Override
	@Transactional
	public TestCaseList getByTestCaseIdwithoutInitialization(int testCaseId) {		
		return testCaseListDAO.getByTestCaseIdwithoutInitialization(testCaseId);
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestCaseStepsByTestCaseId(int testCaseId) {
		return testCaseStepsListDAO.getTotalRecordsOfTestCaseStepsByTestCaseId(testCaseId);
	}	
}
