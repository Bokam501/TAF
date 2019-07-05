package com.hcl.atf.taf.service.impl;

import java.io.File;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.dao.EnvironmentDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ScriptTypeMasterDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.TestCaseStepsListDAO;
import com.hcl.atf.taf.dao.TestExecutionResultDAO;
import com.hcl.atf.taf.dao.TestRunListDAO;
import com.hcl.atf.taf.dao.TestSuiteListDAO;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResultsExportData;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.TestCaseListDTO;
import com.hcl.atf.taf.model.json.JsonTestSuiteList;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.scriptgeneration.ScriptGeneratorUtilities;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserListService;

@Service
public class TestSuiteConfigurationServiceImpl  implements TestSuiteConfigurationService{	 
	
	private static final Log log = LogFactory.getLog(TestSuiteConfigurationServiceImpl.class);

	
	@Autowired
	private TestSuiteListDAO testSuiteListDAO;
	@Autowired
	private TestCaseListDAO testCaseListDAO;
	@Autowired
	private ScriptTypeMasterDAO scriptTypeMasterDAO;
	@Autowired
	private TestCaseStepsListDAO testCaseStepsListDAO;
	@Autowired
	private TestRunListDAO testRunListDAO;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private EnvironmentDAO environmentDAO;
	@Autowired
	private TestExecutionResultDAO testExecutionResultDAO;
	@Autowired
	private UserListService userListService;	
	@Autowired
	private EventsService eventsService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private ProductMasterDAO productMasterDAO;
	
	@Value("#{ilcmProps['TEST_STEP_PARSER']}")
    private String testStepParser;
	
	@Value("#{ilcmProps['TEST_CASE_BATCH_PROCESSING_COUNT']}")
    private String testCaseMaxBatchCount;
	
	@Value("#{ilcmProps['TEST_STEP_BATCH_PROCESSING_COUNT']}")
	private String testCaseStepMaxBatchCount;
	
	@Override
	@Transactional
	public int addTestCase(TestCaseList testCaseList) {
		if (testCaseList.getTestCaseScriptFileName() == null || testCaseList.getTestCaseScriptFileName().trim().isEmpty()) {
			testCaseList.setTestCaseScriptFileName(ScriptGeneratorUtilities.getTestCaseClassName(testCaseList.getTestCaseName(), testCaseList.getTestCaseId(), testCaseList.getTestCaseCode(), 1));
		}
		if (testCaseList.getTestCaseScriptQualifiedName() == null || testCaseList.getTestCaseScriptQualifiedName().trim().isEmpty()) {
			testCaseList.setTestCaseScriptQualifiedName("com.hcl.atf.taf.testcase");
		}
		return testCaseListDAO.add(testCaseList);
		
	}

	@Override
	@Transactional
	public int addTestCaseStep(TestCaseStepsList testCaseStep) {
		
		return testCaseListDAO.addTestCaseStep(testCaseStep);
	}
	
	@Override
	@Transactional
	public int addTestSuite(TestSuiteList testSuiteList) {
		return testSuiteListDAO.add(testSuiteList);
		
	}

	//Changes for Test Management tool integration
	@Override
	@Transactional
	public void addTestSuites(List<TestSuiteList> testSuites) {
		for(TestSuiteList testSuiteList : testSuites){
			testSuiteListDAO.add(testSuiteList);
		}		
	}
	
	//Changes for Test Management tool integration
	@Override
	@Transactional
	public void updateTestSuites(List<TestSuiteList> testSuites) {
		for(TestSuiteList testSuiteList : testSuites){
			testSuiteListDAO.update(testSuiteList);
		}		
	}
	
	@Override
	@Transactional
	public void deleteTestCaseSteps(int testCaseId) {
		testCaseStepsListDAO.delete(getByTestCaseStepsId(testCaseId));
		
	}

	@Override
	@Transactional
	public void deleteTestSuite(int testSuiteId) {
		testSuiteListDAO.delete(getByTestSuiteId(testSuiteId));
		
	}

	@Override
	@Transactional
	public void reactivateTestSuite(int testSuiteId) {
		testSuiteListDAO.reactivate(getByTestSuiteId(testSuiteId));
		
	}
	
	@Override
	@Transactional
	public TestCaseList getByTestCaseId(int testCaseId) {
		
		return testCaseListDAO.getByTestCaseId(testCaseId);
	}
	
	@Override
	@Transactional
	public TestCaseList getByTestCaseIdBare(int testCaseId) {
		
		return testCaseListDAO.getByTestCaseIdBare(testCaseId);
	}
	
	
	@Override
	@Transactional
	public TestCaseStepsList getByTestCaseStepsId(int testCaseId) {
		
		return testCaseStepsListDAO.getByTestCaseId(testCaseId);
	}
	
	@Override
	@Transactional
	public TestCaseStepsList getByTestStepId(int testStepId){
		return testCaseStepsListDAO.getByTestStepId(testStepId);
	}
	
	@Override
	@Transactional
	public TestSuiteList getByTestSuiteId(int testSuitId) {
		
		return testSuiteListDAO.getByTestSuiteId(testSuitId);
	}

	//Changes for Test Management tools integration
	@Override
	@Transactional
	public TestSuiteList getByTestSuiteCode(String testSuiteCode) {
		
		return testSuiteListDAO.getByTestSuiteCode(testSuiteCode);
	}
	
	@Override
	@Transactional
	public TestSuiteList getByProductTestSuiteCode(int productId, String testSuiteCode) {
		try {
		return testSuiteListDAO.getByProductTestSuiteCode(productId, testSuiteCode);
		} catch(Exception e) {
			
		}
		return null;
	}
	
	@Override
	@Transactional
	public TestSuiteList getByProductIdAndTestSuiteNandAndTestSuiteCode(int productId, String testSuiteName,String testSuiteCode) {
		try {
		return testSuiteListDAO.getByProductIdAndTestSuiteNandAndTestSuiteCode(productId, testSuiteName, testSuiteCode);
		} catch(Exception e) {
			log.error("Error in getByProductIdAndTestSuiteNandAndTestSuiteCode",e);
		}
		return null;
	}
	
	
	@Override
	@Transactional
	public int getTotalRecordsOfTestSuite() {
		
		return testSuiteListDAO.getTotalRecords();
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestCaseSteps() {
		
		return testCaseStepsListDAO.getTotalRecords();
	}
	
	@Override
	@Transactional
	public List<TestCaseList> listTestCase() {
		
		return testCaseListDAO.list();
	}

	@Override
	@Transactional
	public List<TestCaseList> listAllTestCases(int productId) {
		return testCaseListDAO.listAllTestCases(productId);
	}	
	
	@Override
	@Transactional
	public List<TestCaseList> listTestCase(int testSuiteId) {
		
		return testCaseListDAO.list(testSuiteId);
	}

	@Override
	@Transactional
	public List<TestCaseList> listTestCase(int startIndex, int pageSize) {
		
		return testCaseListDAO.list(startIndex, pageSize);
	}

	@Override
	@Transactional
	public List<TestCaseList> listTestCase(int testSuiteId, int startIndex,
			int pageSize) {
		
		return testCaseListDAO.list(testSuiteId, startIndex, pageSize);
	}

	@Override
	@Transactional
	public List<TestSuiteList> listTestSuite() {
		
		return testSuiteListDAO.list();
	}

	@Override
	@Transactional
	public List<TestSuiteList> listTestSuite(int status) {
		
		return testSuiteListDAO.list(status);
	}

	@Override
	@Transactional
	public List<TestSuiteList> listTestSuite(int startIndex, int pageSize) {		
		return testSuiteListDAO.list(startIndex, pageSize);
	}
	
	@Override
	@Transactional
	public List<TestSuiteList> listTestSuite(int startIndex, int pageSize, int status) {		
		return testSuiteListDAO.list(startIndex, pageSize, status);
	}


	@Override
	@Transactional
	public int addTestCase(int testCaseId,int testSuiteId) {
		return testCaseListDAO.addTestCase(testCaseId,testSuiteId);
		
	}
	
	@Override
	@Transactional
	public int add(TestCaseList testCaseList) {
		return testCaseListDAO.add(testCaseList);
	}
	
	@Override
	@Transactional
	public void deleteTestCase(int testCaseId) {
		testCaseListDAO.delete(getByTestCaseId(testCaseId));
	}

	
	@Override
	@Transactional
	public void updateTestCase(TestCaseList testCaseList) {
		testCaseListDAO.update(testCaseList);
		
	}
	
	@Override
	@Transactional
	public void updateTestCaseManually(TestCaseList testCaseList) {
		testCaseListDAO.updateTestCasesManually(testCaseList);
		
	}
	
	@Override
	@Transactional
	public void updateTestCaseSteps(TestCaseStepsList testCaseStepsList) {
		
		testCaseStepsListDAO.update(testCaseStepsList);
	}
	
	@Override
	@Transactional
	public void updateTestSuite(TestSuiteList testSuiteList) {
		
		testSuiteListDAO.updateTestSuite(testSuiteList);
	}
	
	@Override
	@Transactional
	public void updateTestSuiteList(TestSuiteList testSuiteList) {
		
		testSuiteListDAO.update(testSuiteList);
	}

	@Override
	@Transactional
	public List<TestSuiteList> testSuitesList(int productVersionListId) {
		
		return testSuiteListDAO.list(productVersionListId,new String[]{"testSuiteId","testSuiteName"});
	}

	@Override
	@Transactional
	public List<ScriptTypeMaster> testScriptTypeList() {		
		return scriptTypeMasterDAO.list();
	}
	
	@Override
	@Transactional
	public ScriptTypeMaster getScriptTypeMasterByscriptType(String scriptTypeName){
		return scriptTypeMasterDAO.getScriptTypeMasterByscriptType(scriptTypeName);
	}
	
	@Override
	@Transactional
	public List<TestCaseStepsList> listTestCaseSteps() {
		
		return testCaseStepsListDAO.list();
	}

	@Override
	@Transactional
	public List<TestCaseStepsList> listTestCaseSteps(int testCaseId) {
		
		return testCaseStepsListDAO.list(testCaseId);
	}

	@Override
	@Transactional
	public List<TestCaseStepsList> listTestCaseSteps(int startIndex, int pageSize) {
		
		return testCaseStepsListDAO.list(startIndex, pageSize);
	}

	@Override
	@Transactional
	public List<TestCaseStepsList> listTestCaseSteps(int testCaseId, int startIndex,
			int pageSize) {
		
		return testCaseStepsListDAO.list(testCaseId, startIndex, pageSize);
	}
	
	

	@Override
	@Transactional
	public TestCaseList getTestCaseByName(String testCaseName, int productId) {
		
		return testCaseListDAO.getTestCaseByName(testCaseName, productId);
	}	
	
	@Override
	@Transactional
	public TestCaseStepsList getTestCaseStepByName(String testCaseStepName, int productId) {
		
		return testCaseListDAO.getTestCaseStepByName(testCaseStepName, productId);
	}

	@Override
	@Transactional
	public TestCaseStepsList getTestCaseStepByName(String testCaseStepName, int productId, int testCaseId) {
		
		return testCaseListDAO.getTestCaseStepByName(testCaseStepName, productId, testCaseId);
	}

	
	//Changes for TestManagement tools integration - Priya.B
	@Override
	@Transactional
	public void addTestCases(List<TestCaseList> newTestCases) {
		for(TestCaseList list : newTestCases){		
			
			int id = testCaseListDAO.add(list);
			list.setTestCaseId(id);
			TestCaseStepsList testCaseStep = new TestCaseStepsList();
			testCaseStep.setTestStepName(list.getTestCaseName());
			testCaseStep.setTestStepDescription(list.getTestCaseDescription());
			testCaseStep.setTestStepSource(list.getTestCaseSource());
			testCaseStep.setTestStepCreatedDate(list.getTestCaseCreatedDate());
			testCaseStep.setTestStepLastUpdatedDate(list.getTestCaseLastUpdatedDate());
			testCaseStep.setTestStepSource(list.getTestCaseSource());
			testCaseStep.setTestCaseList(list);
			//Adding Test steps
			
			testCaseStepsListDAO.add(testCaseStep);
			
		}
		// TODO Auto-generated method stub
		
	}

	//Changes for TestManagement tools integration - Priya.B
	@Override
	@Transactional
	public void addTestCasesAndSteps(List<TestCaseList> newTestCases) {
		
		for(TestCaseList newTestCase : newTestCases){		
			
			int id = testCaseListDAO.add(newTestCase);
			newTestCase.setTestCaseId(id);
			//Adding Test steps
			Set<TestCaseStepsList> testCaseSteps = newTestCase.getTestCaseStepsLists();
			for(TestCaseStepsList testStep : testCaseSteps){				
				testStep.setTestCaseList(newTestCase);
				testCaseStepsListDAO.add(testStep);		
				
			}		
			
		}
	}

	@Override
	@Transactional
	public List<TestCaseList> getTestCaseListByProductId(int productId) {
		return testCaseListDAO.getTestCaseListByProductId(productId, null, null);
	}	
	
	@Override
	@Transactional
	public TestCaseList getTestCaseByCode(String testCaseCode) {
		
		return testCaseListDAO.getTestCaseByCode(testCaseCode);
	}
	
	@Override
	@Transactional
	public Boolean  IsTestCaseExistsByCode(String testCaseCode) {
		
		return testCaseListDAO.IsTestCaseExistsByCode(testCaseCode);
	}
	
	@Override
	@Transactional
	public void updateTestCasesInImport(List<TestCaseListDTO> modifiedTestCasesListDTO, String tcAddOrUpdateAction) {
		testCaseListDAO.updateTestCasesInImport(modifiedTestCasesListDTO, tcAddOrUpdateAction,Integer.parseInt(testCaseMaxBatchCount));
		///
	}
	
	@Override
	@Transactional
	public void updateTestCaseStepsListInImport(List<TestCaseStepsList> listOfTestStepsList, String tcAddOrUpdateAction) {
		testCaseStepsListDAO.updateTestCaseStepsListInImport(listOfTestStepsList, tcAddOrUpdateAction,Integer.parseInt(testCaseStepMaxBatchCount));
	}
	
	private HashMap<String,List<String>> verifyExistingTestSteps(List<String> testStepsExcel ,int testCaseId) {
		HashMap<String,List<String>> tesCaseMap = new HashMap<String,List<String>>();
		List<String> testStepsUpdate = new ArrayList<String>();
		List<String> testStepsNew = new ArrayList<String>();
		List<TestCaseStepsList> testCasesStepsDB = listTestCaseSteps(testCaseId);
		
		String testStepNameExcel=null;
		String testStepNameDB=null;
		
		//Comparing the testSteps code
		for(String strTestStepName : testStepsExcel){
			testStepNameExcel = strTestStepName;
			if(testCasesStepsDB !=null && !testCasesStepsDB.isEmpty()){
				log.info("testCasesStepsDB size: "+testCasesStepsDB.size());
				for(TestCaseStepsList testStepDB : testCasesStepsDB){
					testStepNameDB = testStepDB.getTestStepName();
					if(testStepNameDB!= null && testStepNameExcel != null && testStepNameExcel.equals(testStepNameDB)){
					}else{
						testStepsNew.add(testStepNameExcel);
					}
				}
			}else{
				testStepsNew.add(testStepNameExcel);
			}
		}
		tesCaseMap.put("Update", testStepsUpdate);
		tesCaseMap.put("New", testStepsNew);
		return tesCaseMap;
	}
	
	private List<String> getListOfTestSteps(String testSteps){
		List<String> listOfTestSteps = null;
		String[] testStepsArray = null;
		if(testSteps != null &&  !testSteps.equals("")){
			if(testStepParser != null && !testStepParser.equals("")){
				log.info("testStepParser: "+testStepParser);
			}
			testStepsArray = testSteps.split(testStepParser);
			if(testStepsArray != null && testStepsArray.length>0){
				listOfTestSteps = new ArrayList<String>();
				for(String testStep: testStepsArray){
					listOfTestSteps.add(testStep.trim());
				}
			}
		}
		return listOfTestSteps;
	}
	
	@Override
	@Transactional
	public TestCaseList getTestCaseByCodeProduct(String testCaseCode,
			int productId) {
		return testCaseListDAO.getTestCaseByCodeProduct(testCaseCode,
				productId);		
	}

	@Override
	@Transactional
	public TestCaseList getTestCaseByNameProduct(String testCaseName,
			int productId) {
		return testCaseListDAO.getTestCaseByNameProduct(testCaseName,
				productId);
	}

	@Override
	@Transactional
	public List<TestCaseList> getTestCaseByCodeNameProduct(String testCaseCode,
			String testCaseName, int productId) {
		return testCaseListDAO.getTestCaseByCodeNameProduct(testCaseCode, testCaseName,
				productId);
	}
	
	//Changes for Test Management tools integration
	@Override	
	public TestCaseStepsList getTestCaseStepsByCodeAndProduct(String testStepCode, int productId) {		
		return testCaseStepsListDAO.getTestCaseStepsByCodeAndProduct(testStepCode, productId);
	}
	
	@Override
	@Transactional
	public void addTestCaseSteps(List<TestCaseStepsList> testCaseSteps) {
		
		for(TestCaseStepsList testCaseStepsList : testCaseSteps){
			testCaseListDAO.addTestCaseStep(testCaseStepsList);
		} 
	}
	
	@Override
	@Transactional
	public void updateTestCaseStepsLists(List<TestCaseStepsList> testCaseStepsLists) {
		
		for(TestCaseStepsList testCaseStepsList : testCaseStepsLists){
			testCaseStepsListDAO.update(testCaseStepsList);
		}
	}

	@Override
	@Transactional
	public void updateTestSuiteTestCase(Set<TestCaseList> testCasesSet, TestSuiteList testSuite) {
		
		for(TestCaseList testCase : testCasesSet){			
			testCaseListDAO.update(testCase);
		}		
		testSuiteListDAO.update(testSuite);
	}
		
	@Override
	@Transactional
	public List<TestSuiteList> filterTestSuites(int jtStartIndex,
			int jtPageSize, Integer productId) {
		return testSuiteListDAO.filterTestSuites(jtStartIndex,jtPageSize,productId);
	}

	/**
	 * Added the method for dynamic script file generation. 
	 * The script file name is constructed for the product, testRunConfigChild and TestRun.
	 */
	@Override
	@Transactional
	public String constructTestScriptFileNameForExternalScriptSource(TestSuiteList testSuiteList, int testRunJobId, String testSuitePath) {
		
		String productName = null;
		String scriptFileName = null;
		String testSuiteFinalPath = null;
		
		if (testSuiteList == null){
			
			return null;
		}
		if (testRunJobId <= 0){
			
			return null;
		}
		
		TestRunJob testRunJob = environmentDAO.getTestRunJobById(testRunJobId);
		if (testRunJob == null){
			
			return null;
		}
		
		if (testSuiteList.getTestScriptSource().startsWith(TAFConstants.TEST_SCRIPT_SOURCE_HPQC)) {
				
			productName = testRunJob.getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductName();
			productName = productName.replace(" ", "_");
			scriptFileName = productName;
			scriptFileName = scriptFileName + "_TRCC_" + testRunJob.getTestRunPlan().getTestRunPlanId();
			scriptFileName = scriptFileName + "_TR_" + testRunJob.getWorkPackage().getWorkPackageId();
			scriptFileName = scriptFileName + "_TS_" + testSuiteList.getTestSuiteCode();
			
			scriptFileName = scriptFileName + "/"+scriptFileName+".zip";
			
			log.info("testSuitePath:" + testSuitePath);
			
			testSuiteFinalPath = testSuitePath + scriptFileName;
			
			log.info("testSuiteFinalPath:" + testSuiteFinalPath);
			
		}
		return testSuiteFinalPath;
	}

	@Override
	@Transactional
	public List<TestSuiteList> getByProductId(Integer startIndex, Integer pageSize, int productId) {
		return testSuiteListDAO.getByProductId(startIndex, pageSize, productId);
	}
	
	@Override
	@Transactional
	public List<TestSuiteList> getByProductId(int productId) {
		return testSuiteListDAO.getByProductId(productId);
	}

	@Override
	@Transactional
	public Set<TestSuiteList> getTestSuiteByProductId(int productId) {
		return testSuiteListDAO.getTestSuiteByProductId(productId);
	}

	@Override
	@Transactional
	public List<TestSuiteList> getByProductVersionId(int startIndex,
			int pageSize, int productVersionListId) {		
		return testSuiteListDAO.getByProductVersionId(startIndex, pageSize, productVersionListId);
	}
	
	@Override
	@Transactional
	public List<TestSuiteList> getTestSuitesMappedToTestCaseByTestCaseId(int testCaseId, Integer startIndex, Integer pageSize){
		return testSuiteListDAO.getTestSuitesMappedToTestCaseByTestCaseId(testCaseId, startIndex, pageSize);
	}
	
	@Override
	@Transactional
	public List<TestSuiteList> listMappedTestSuite(int testSuiteId) {
		return testSuiteListDAO.listMappedTestSuite(testSuiteId);
	}

	@Override
	@Transactional
	public int addTestSuiteMapping(int testSuiteId, int testSuiteIdtobeMapped) {		
		return testSuiteListDAO.addTestSuiteMapping(testSuiteId, testSuiteIdtobeMapped);
	}

	@Override
	@Transactional
	public List<TestSuiteList> getByProductVersionId(Integer jtStartIndex,
			Integer jtPageSize, Integer versionId, Integer testRunPlanId) {
		return testSuiteListDAO.getByProductVersionId(jtStartIndex, jtPageSize, versionId, testRunPlanId);
	}
	
	@Override
	@Transactional
	public int addTestCasetoProductVersion(int testcaseId, int versionId) {
		return testCaseListDAO.addTestCasetoProductVersion(testcaseId, versionId);
	}

	@Override
	@Transactional
	public int updateTestCasetoProductVersionMapping(int testcaseId, String productVersionName, String mappingStatus) {
		
		//Get the productVersion from the testcase
		TestCaseList testCase = testCaseListDAO.getByTestCaseId(testcaseId);
		ProductMaster product = testCase.getProductMaster();
		String verId = (productVersionName.substring(productVersionName.indexOf("r")+1, productVersionName.length()));
		List<ProductVersionListMaster> productVersions = productListService.listProductVersion(product.getProductId());
		Integer index = Integer.parseInt(verId);
		if(index > 0){
			index = index -1; // index obtained from Version20 variable by splitting, hence
		}
		ProductVersionListMaster productVersion = productVersions.get(index);
		log.info("Updated Product Version is  : " + productVersion.getProductVersionName() + " : Testcase is : " + testcaseId + " : Status is : " + mappingStatus);
				
		if (mappingStatus.equals("1")) {
			Set<TestCaseList> testCaseSet = productVersion.getTestCaseLists();
			if (!testCaseSet.contains(testCase)) {
				testCaseSet.add(testCase);
				productVersion.setTestCaseLists(testCaseSet);
				productListService.updateProductVersion(productVersion);
			}
		} else if (mappingStatus.equals("0")) {
			Set<TestCaseList> testCaseSet = productVersion.getTestCaseLists();
			log.info("Version Testcases " + testCaseSet.size());
			
				testCaseSet.remove(testCase);
				log.info("Version Testcases after removal : " + testCaseSet.size());
				
				for(TestCaseList tc : testCaseSet) {
					if (testcaseId == tc.getTestCaseId()) {
						testCaseSet.remove(tc);
						log.info("Version Testcases after iteration removal : " + testCaseSet.size());
						break;
					}
						
				}
				productVersion.setTestCaseLists(testCaseSet);
				productListService.updateProductVersion(productVersion);
		}
		return productVersion.getProductVersionListId();
	}

	@Override
	@Transactional
	public int updateTCtoPVMapping(int testcaseId, int productVersionId,String mappingStatus) {
		
		//Get the productVersion from the testcase
		TestCaseList testCase = testCaseListDAO.getByTestCaseId(testcaseId);
		ProductMaster product = testCase.getProductMaster();
		ProductVersionListMaster productVersion = productListService.getProductVersionListMasterById(productVersionId);
		log.info("Updated Product Version is  : " + productVersion.getProductVersionName() + " : Testcase is : " + testcaseId + " : Status is : " + mappingStatus);
				
		if (mappingStatus.equals("1")) {
			Set<TestCaseList> testCaseSet = productVersion.getTestCaseLists();
			if (!testCaseSet.contains(testCase)) {
				testCaseSet.add(testCase);
				productVersion.setTestCaseLists(testCaseSet);
				productListService.updateProductVersion(productVersion);
			}
		} else if (mappingStatus.equals("0")) {
			Set<TestCaseList> testCaseSet = productVersion.getTestCaseLists();
			log.info("Version Testcases " + testCaseSet.size());
			
				testCaseSet.remove(testCase);
				log.info("Version Testcases after removal : " + testCaseSet.size());
				
				for(TestCaseList tc : testCaseSet) {
					if (testcaseId == tc.getTestCaseId()) {
						testCaseSet.remove(tc);
						log.info("Version Testcases after iteration removal : " + testCaseSet.size());
						break;
					}
						
				}
				productVersion.setTestCaseLists(testCaseSet);
				productListService.updateProductVersion(productVersion);
		}
		return productVersion.getProductVersionListId();
	}



	@Override
	@Transactional
	public void updateTestCases(List<TestCaseList> modifiedTestCases) {
		
		for(TestCaseList testCase : modifiedTestCases){
			
			testCaseListDAO.update(testCase);
		}
	}

	@Override
	@Transactional
	public boolean isVersionTestSuiteExistingByName(String testSuiteName,
			Integer versionId) {
		return testSuiteListDAO.isVersionTestSuiteExistingByName(testSuiteName, versionId);
	}

	@Override
	@Transactional
	public ProductVersionListMaster getLatestProductVersion(
			ProductMaster productMaster) {
		return testSuiteListDAO.getLatestProductVersion(productMaster);
	}

	@Override
	@Transactional
	public void addTestExecutionResultsExportData(TestExecutionResultsExportData testExecutionResultsExportData) {
		testExecutionResultDAO.addTestExportData(testExecutionResultsExportData);
		
	}

	@Override
	@Transactional
	public List<TestCaseList> listTestCaseByTestSuite(int testSuiteId,
			int jtStartIndex, int jtPageSize) {
		return testSuiteListDAO.listTestCaseByTestSuite(testSuiteId, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public List<TestSuiteList> returnVersionTestSuiteId(String testSuiteName,Integer versionId){
		return testSuiteListDAO.returnVersionTestSuiteId(testSuiteName, versionId);
	}

	@Override
	@Transactional
	public Integer countAllTestSuites(Date startDate, Date endDate) {
		return testSuiteListDAO.countAllTestSuites(startDate, endDate);
	}

	@Override
	@Transactional
	public List<TestSuiteList> listAllTestSuites(int startIndex, int pageSize,Date startDate, Date endDate) {
		return testSuiteListDAO.listAllTestSuites(startIndex,pageSize,startDate, endDate);
	}
	
	@Override
	@Transactional
	public JTableSingleResponse updateTestSuiteScriptPack(Integer testSuiteId, String testSuiteName, Integer productId, String testSuitePath, String testScriptPackSource) {
		
		JTableSingleResponse jTableSingleResponse;
		try {
			TestSuiteList testSuiteList = null;
			if (testSuiteId !=  null) {
				testSuiteList = getByTestSuiteId(testSuiteId);
			}
			if (testSuiteList == null) {
				Set<TestSuiteList> testSuites = getTestSuiteByProductId(productId);
				for (TestSuiteList ts : testSuites) {
					if (ts.getTestSuiteName().equals(testSuiteName)) {
						testSuiteList = ts;
						break;
					}
				}
			}
			if (testSuiteList == null) {
				
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to find Test Suite to update the script pack");
				log.info("Unable to find Test Suite to update the script pack");
				return jTableSingleResponse;
			}

			String oldScriptPack = testSuiteList.getTestSuiteScriptFileLocation();
			if (new File(testSuitePath).exists()) {
				if(testSuiteList.getScriptPlatformLocation() != null && testSuiteList.getScriptPlatformLocation().equalsIgnoreCase("SERVER")){
					String path = "http://" + request.getServerName() + ":" + request.getServerPort() + "/"+ request.getContextPath() + "//" + "TestScripts" + "//" + testSuiteId + "//" + new File(testSuitePath).getName();
					testSuiteList.setTestSuiteScriptFileLocation(path);
				} else {				
					testSuiteList.setTestSuiteScriptFileLocation(testSuitePath);
				}
				testSuiteList.setTestScriptSource(testScriptPackSource);
				updateTestSuite(testSuiteList);
			}  else {
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to access the script pack : " + testSuitePath);
				log.info("Unable to access the script pack : " + testSuitePath);
				return jTableSingleResponse;
			}

			UserList adminUser = userListService.getUserListById(1);
			eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_TEST_SUITE, testSuiteList.getTestSuiteId(), testSuiteList.getTestSuiteName(),
					"testSuiteScriptFileLocation", "testSuiteScriptFileLocation", oldScriptPack, testSuitePath, adminUser, 
					"Updated TestSuite script pack from : " + testScriptPackSource);

			jTableSingleResponse = new JTableSingleResponse("OK", new JsonTestSuiteList(testSuiteList));
	
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error while updating test script pack for test suite id : " + testSuiteId + " : for test suite name : " + testSuiteName);
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}

	public boolean checkFileAvailablity(String filePath) {
		URL url = null;
		ReadableByteChannel rbc = null;
		boolean fileAvailable = false;
		try {
			url = new URL(filePath);
			rbc = Channels.newChannel(url.openStream());
			if (rbc != null) {
				fileAvailable = true;
			}
		} catch (Exception e) {
		}
		return fileAvailable;
	}

	@Override
	@Transactional
	public TestSuiteList getByTestSuiteName(String testSuiteName) {		
		return testSuiteListDAO.getByTestSuiteName(testSuiteName);
	}
	
	@Override
	@Transactional
	public void delAllTestCaseMappingsByTestCaseId(Integer testCaseId) {
		TestCaseList testcase = testCaseListDAO.getByTestCaseId(testCaseId);
		List<TestSuiteList> testSuiteLists = testSuiteListDAO.getTestSuitesMappedToTestCaseByTestCaseId(testCaseId, null, null);
		for(TestSuiteList ts : testSuiteLists){
			Set<TestCaseList> testSuiteTestCaseListSet = ts.getTestCaseLists();
			Set<TestCaseList> testSuiteTestCaseListSetToUpdate=new HashSet<TestCaseList>();
			for(TestCaseList tc:testSuiteTestCaseListSet){
				if(!tc.getTestCaseId().equals(testCaseId)){
					testSuiteTestCaseListSetToUpdate.add(tc);
				}
			}
			ts.setTestCaseLists(testSuiteTestCaseListSetToUpdate);
			testSuiteListDAO.update(ts);
		}
		
		//Delete the testcases mapped to the Test Configuration
		List<RunConfiguration> runconfigList = new ArrayList<RunConfiguration>();
		runconfigList = productListService.listRunConfiguration(testcase.getProductMaster().getProductId());
		for(RunConfiguration rc : runconfigList){
			for(TestSuiteList ts : rc.getTestSuiteLists()){
				List<TestCaseList> testConfigTestCasesList = productListService.getRunConfigTestSuiteTestCaseMapped(rc.getRunconfigId(), ts.getTestSuiteId());
				for(TestCaseList tc : testConfigTestCasesList){
					if(tc.getTestCaseId().equals(testCaseId)){
						productMasterDAO.mapTestSuiteTestCasesRunConfiguration(rc.getRunconfigId(),ts.getTestSuiteId(),testCaseId,"Remove");
					}
				}
			}
		}
	}

	@Override
	@Transactional
	public TestSuiteList getTestSuiteByProductIdOrTestSuiteNandOrTestSuiteCode(
			int productId, String testSuiteName, String testSuiteCode) {
		try {
			return testSuiteListDAO.getTestSuiteByProductIdOrTestSuiteNandOrTestSuiteCode(productId, testSuiteName, testSuiteCode);
		}catch(Exception e) {
			
		}
		return null;
	}
}

