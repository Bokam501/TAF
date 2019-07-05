package com.hcl.atf.taf.controller.utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;
import com.hcl.atf.taf.model.ActivityStatus;
import com.hcl.atf.taf.model.CompetencyMember;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.DataExtractorScheduleMaster;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.DimensionMaster;
import com.hcl.atf.taf.model.DimensionProduct;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserSkills;
import com.hcl.atf.taf.model.VendorMaster;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.json.JsonCustomFieldConfigMaster;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTerminal;
import com.hcl.atf.taf.model.json.terminal.JsonTestExecutionResult;
import com.hcl.atf.taf.service.CompetencyService;
import com.hcl.atf.taf.service.CustomFieldService;
import com.hcl.atf.taf.service.CustomerService;
import com.hcl.atf.taf.service.DataSourceExtractorService;
import com.hcl.atf.taf.service.DecouplingCategoryService;
import com.hcl.atf.taf.service.DimensionService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.SkillService;
import com.hcl.atf.taf.service.StatusService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestFactoryManagementService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.VendorListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.ilcm.workflow.service.WorkflowStatusService;

public class ValidationUtility {
	private static final Log log =  LogFactory.getLog(ValidationUtility.class);
	static String stus ="";	
	
	public static String validateForNewTestFactoryAddition(TestFactory testFactory, TestFactoryManagementService testFactoryManagementService, String addorupdate) {
		boolean existing = false;
		if(addorupdate.equalsIgnoreCase("add")){
			existing = testFactoryManagementService.isTestFactoryExistingByName(testFactory);
		}else if(addorupdate.equalsIgnoreCase("update")){
			 existing = testFactoryManagementService.isTestFactoryExistingByNameForUpdate(testFactory, testFactory.getTestFactoryId());
		}		
		TestFactory TestFactoryFromDB = testFactoryManagementService.getTestFactoryByName(testFactory.getTestFactoryName().trim());
		Integer status =0;
		if (TestFactoryFromDB == null) {
			status=1;
			return null;
		}
		else{
			status = TestFactoryFromDB.getStatus();
		}
		StringBuffer errorMessage = new StringBuffer();
		if (existing) {
			
			appendErrorMessage1(errorMessage, status,TAFConstants.TESTFACTORY_DUPLICATE_MESSAGE);
		}
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	
	public static String validateForNewTestCaseStepAddition(TestCaseStepsList testCaseStepsListFromUI, TestCaseService testCaseService, TestSuiteConfigurationService testSuiteConfigurationService, int testCaseId) {
		
		Integer status =0;	
		List<TestCaseStepsList> testCaseStepsListUI = new ArrayList<TestCaseStepsList>();
		testCaseStepsListUI.add(testCaseStepsListFromUI);
		
		HashMap<String,List<TestCaseStepsList>> tesCaseStepMap= verifyExistingTestCaseSteps(testSuiteConfigurationService, testCaseStepsListUI,testCaseId);
		StringBuffer errorMessage = new StringBuffer();
		
		if (tesCaseStepMap.size() > 0) {
			Set<String> set = tesCaseStepMap.keySet();
			if((set.contains("Duplicate")) && (set.contains("New"))){
				appendErrorMessage1(errorMessage, status,TAFConstants.TESTCASESTEP_DUPLICATE_MESSAGE);
			}else if(set.contains("Duplicate")){
				appendErrorMessage1(errorMessage, status,TAFConstants.TESTCASESTEP_DUPLICATE_MESSAGE);
			}else if(set.contains("Update")){
				
			}
		}		
		
		
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	public static String validateForNewTestCaseStepUpdate(TestCaseStepsList testCaseStepsListFromUI, TestCaseService testCaseService, TestSuiteConfigurationService testSuiteConfigurationService, int testCaseId) {
		
		Integer status =0;	
		List<TestCaseStepsList> testCaseStepsListUI = new ArrayList<TestCaseStepsList>();
		testCaseStepsListUI.add(testCaseStepsListFromUI);
		
		HashMap<String,List<TestCaseStepsList>> tesCaseStepMap= verifyExistingTestCaseSteps(testSuiteConfigurationService, testCaseStepsListUI,testCaseId);
		StringBuffer errorMessage = new StringBuffer();
		
		if (tesCaseStepMap.size() > 0) {
			Set<String> set = tesCaseStepMap.keySet();
			if((set.contains("Duplicate")) && (set.contains("Update"))){
				appendErrorMessage1(errorMessage, status,TAFConstants.TESTCASESTEP_DUPLICATE_MESSAGE);
			}else if(set.contains("Duplicate")){
				appendErrorMessage1(errorMessage, status,TAFConstants.TESTCASESTEP_DUPLICATE_MESSAGE);
			}else if(set.contains("Update")){
				
			}			
		}		
		
		
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	public static String validateForNewTestCaseAddition(TestCaseList testCaseListFromUI, TestCaseService testCaseService, TestSuiteConfigurationService testSuiteConfigurationService, int productId) {
		
		Integer status =0;	
		List<TestCaseList> testCaseList = new ArrayList<TestCaseList>();
		testCaseList.add(testCaseListFromUI);
		
		HashMap<String,List<TestCaseList>> tesCaseMap= verifyExistingTestCases(testSuiteConfigurationService, testCaseList,productId);
		StringBuffer errorMessage = new StringBuffer();
		
		if (tesCaseMap.size() > 0) {
			Set<String> set = tesCaseMap.keySet();
			if((set.contains("Duplicate")) && (set.contains("New"))){
				appendErrorMessage1(errorMessage, status,TAFConstants.TESTCASE_DUPLICATE_MESSAGE);
			}else if(set.contains("Duplicate")){
				appendErrorMessage1(errorMessage, status,TAFConstants.TESTCASE_DUPLICATE_MESSAGE);
			}else if(set.contains("Update")){
				
			}
		}		
		
		
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	public static String validateForNewTestCaseUpdate(TestCaseList testCaseListFromUI, TestCaseService testCaseService, TestSuiteConfigurationService testSuiteConfigurationService, int productId) {
		
		Integer status =0;	
		List<TestCaseList> testCaseList = new ArrayList<TestCaseList>();
		testCaseList.add(testCaseListFromUI);
		
		HashMap<String,List<TestCaseList>> tesCaseMap= verifyExistingTestCases(testSuiteConfigurationService, testCaseList,productId);
		StringBuffer errorMessage = new StringBuffer();
		
		if (tesCaseMap.size() > 0) {
			Set<String> set = tesCaseMap.keySet();
			if((set.contains("Duplicate")) && (set.contains("Update"))){
				appendErrorMessage1(errorMessage, status,TAFConstants.TESTCASE_DUPLICATE_MESSAGE);
			}else if(set.contains("Duplicate")){
				appendErrorMessage1(errorMessage, status,TAFConstants.TESTCASE_DUPLICATE_MESSAGE);
			}else if(set.contains("Update")){
				
			}
		}		
		
		
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}

	public static HashMap<String,List<TestCaseList>> verifyExistingTestCases(TestSuiteConfigurationService testSuiteConfigurationService, List<TestCaseList> testCasesExcel ,int productId) {
		HashMap<String,List<TestCaseList>> tesCaseMap= new HashMap<String,List<TestCaseList>>();
		List<TestCaseList> testCasesDuplicate= new ArrayList<TestCaseList>();
		List<TestCaseList> testCasesUpdate= new ArrayList<TestCaseList>();
		List<TestCaseList> testCasesNew= new ArrayList<TestCaseList>();
		
		try {
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
						if((testCaseCodeDB != null && testCaseCodeExcel != null && testCaseCodeExcel.equals(testCaseCodeDB)) && 
								(testCaseNameDB != null && testCaseNameExcel != null && testCaseNameExcel.equals(testCaseNameDB)))
						{
							testCasesDuplicate.add(testCaseExcel);
						}else if(testCaseCodeDB!=null && testCaseCodeExcel!=null && testCaseCodeExcel.equals(testCaseCodeDB)){
							
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
			if(testCasesDuplicate.size() != 0){
				tesCaseMap.put("Duplicate", testCasesDuplicate);	
			}
			if(testCasesUpdate.size() != 0){
				tesCaseMap.put("Update", testCasesUpdate);	
			}
			
			if(testCasesNew.size() != 0){
				tesCaseMap.put("New", testCasesNew);
			}
			
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return tesCaseMap;
	}
	public static HashMap<String,List<TestCaseStepsList>> verifyExistingTestCaseSteps(TestSuiteConfigurationService testSuiteConfigurationService, List<TestCaseStepsList> testCaseStepsListUI,int testCaseId) {
		HashMap<String,List<TestCaseStepsList>> tesCaseStepMap= new HashMap<String,List<TestCaseStepsList>>();
		List<TestCaseStepsList> testCaseStepDuplicate = new ArrayList<TestCaseStepsList>();
		List<TestCaseStepsList> testCaseStepUpdate = new ArrayList<TestCaseStepsList>();
		List<TestCaseStepsList> testCaseStepNew = new ArrayList<TestCaseStepsList>();
		
		try {
			List<TestCaseStepsList> testCaseStepFromDB = testSuiteConfigurationService.listTestCaseSteps(testCaseId);
			
			String testCaseStepNameDB = null;					
			String testCaseStepCodeDB = null;
			String testCaseStepNameUI = null;	
			String testCaseStepCodeUI = null;
			
			for(TestCaseStepsList testCaseStepUI: testCaseStepsListUI){
				testCaseStepNameUI = testCaseStepUI.getTestStepName();
				testCaseStepCodeUI = testCaseStepUI.getTestStepCode();
				if(testCaseStepFromDB != null && !testCaseStepFromDB.isEmpty()){
					for (TestCaseStepsList testCaseStepsDB : testCaseStepFromDB) {
						testCaseStepNameDB = testCaseStepsDB.getTestStepName();
						testCaseStepCodeDB = testCaseStepsDB.getTestStepCode();
						if((testCaseStepCodeDB != null && testCaseStepCodeUI != null && testCaseStepCodeUI.equals(testCaseStepCodeDB)) && 
								(testCaseStepNameDB != null && testCaseStepNameUI != null && testCaseStepNameUI.equals(testCaseStepNameDB)))
						{
							testCaseStepDuplicate.add(testCaseStepUI);
						}else if(testCaseStepCodeDB != null && testCaseStepCodeUI != null && testCaseStepCodeUI.equals(testCaseStepCodeDB)){
							
							testCaseStepUpdate.add(testCaseStepUI);
						}else if(testCaseStepNameDB != null && testCaseStepNameUI != null && testCaseStepNameUI.equals(testCaseStepNameDB)){
							
							testCaseStepUpdate.add(testCaseStepUI);
						}else{
							testCaseStepNew.add(testCaseStepUI);
						}
					}
				}else{
					testCaseStepNew.add(testCaseStepUI);
				}
				
			}
			if(testCaseStepDuplicate.size() != 0){
				tesCaseStepMap.put("Duplicate", testCaseStepDuplicate);	
			}
			if(testCaseStepUpdate.size() != 0){
				tesCaseStepMap.put("Update", testCaseStepUpdate);	
			}			
			if(testCaseStepNew.size() != 0){
				tesCaseStepMap.put("New", testCaseStepNew);
			}
			
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}		
		return tesCaseStepMap;
	}
	
	public static String validateForNewUserSkillAddition(String UserSkills_SkillName, SkillService skillService, int CurrentUser_userId) {
		
		Integer status =0;	
		boolean existing = skillService.isUserSkillExistingBySkillName(UserSkills_SkillName, CurrentUser_userId);
		UserSkills userSkillFromDB = skillService.getUserSkillBySkillName(UserSkills_SkillName, CurrentUser_userId);
			
		if (userSkillFromDB == null) {
			status=1;
			return null;
		}
		else{
			status = userSkillFromDB.getStatus();
		}
		StringBuffer errorMessage = new StringBuffer();
		if (existing) {
			
			appendErrorMessage1(errorMessage, status,TAFConstants.USER_SKILL_DUPLICATE_MESSAGE);
		}
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}

	public static String validateForNewSkillAddition(String skillName, SkillService skillService) {
		
		Integer status =0;	
		
		boolean existing = skillService.isSkillExistingByName(skillName);
		Skill skillFromDB = skillService.getSkillByName(skillName);
		
		if (skillFromDB == null) {
			status=1;
			return null;
		}
		else{
			status = skillFromDB.getStatus();
		}
		StringBuffer errorMessage = new StringBuffer();
		if (existing) {
			
			appendErrorMessage1(errorMessage, status,TAFConstants.SKILL_DUPLICATE_MESSAGE);
		}
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	
	public static String validateForNewVendorAddition(VendorMaster vendor, VendorListService vendorListService, String addorupdate) {
		boolean existing = false;
		Integer status =0;
		
		if(addorupdate.equalsIgnoreCase("add")){
			 existing = vendorListService.isVendorExistingByregisteredCompanyName(vendor.getRegisteredCompanyName().trim());	
		}else if(addorupdate.equalsIgnoreCase("update")){
			existing = vendorListService.isVendorExistingByregisteredCompanyNameForUpdate(vendor.getRegisteredCompanyName().trim(), vendor.getVendorId());
		}
				
		VendorMaster VendorMasterFromDB = vendorListService.getVendorByregisteredCompanyName(vendor.getRegisteredCompanyName().trim());		
		
		if (VendorMasterFromDB == null) {
			status=1;
			return null;
		}
		else{
			status = VendorMasterFromDB.getStatus();
		}
		StringBuffer errorMessage = new StringBuffer();
		if (existing) {
			
			appendErrorMessage1(errorMessage, status,TAFConstants.VENDOR_DUPLICATE_MESSAGE);
		}
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	public static String validateForNewCustomerAddition(String customerName, CustomerService customerService) {
		
		Integer status =0;		
		boolean existing = customerService.isCustomerExistingByName(customerName);		
		Customer CustomerFromDB = customerService.getCustomerByName(customerName);		
		
		if (CustomerFromDB == null) {
			status=1;
			return null;
		}
		else{
			status = CustomerFromDB.getStatus();
		}
		StringBuffer errorMessage = new StringBuffer();
		if (existing) {
			
			appendErrorMessage1(errorMessage, status,TAFConstants.CUSTOMER_DUPLICATE_MESSAGE);
		}
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}

	public static String validateForNewProductFeatureAddition(ProductFeature featuresfromUI, ProductListService productListService) {		
		
		boolean existing = productListService.isProductFeatureExistingByName(featuresfromUI.getProductFeatureName(), featuresfromUI.getProductMaster().getProductId(), featuresfromUI.getProductFeatureId());
		StringBuffer errorMessage = new StringBuffer();
		if (existing) {					
			appendErrorMessage(errorMessage, TAFConstants.PRODUCT_FEATURE_DUPLICATE_MESSAGE);			
		}
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	
	public static String validateForNewVersionTestSuiteAddition(TestSuiteList testSuiteListFromUI, TestSuiteConfigurationService testSuiteConfigurationService) 
	{		
		boolean existing = testSuiteConfigurationService.isVersionTestSuiteExistingByName(testSuiteListFromUI.getTestSuiteName(), testSuiteListFromUI.getProductVersionListMaster().getProductVersionListId());	
		StringBuffer errorMessage = new StringBuffer();
		if (existing) {					
			appendErrorMessage(errorMessage, TAFConstants.TESTSUITE_DUPLICATE_MESSAGE);			
		}
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	
	public static String validateForNewProductDecouplingCategoryAddition(DecouplingCategory decouplingCategory, DecouplingCategoryService decouplingCategoryService)
	{	
		boolean existing = decouplingCategoryService.isProductDecouplingCategoryExistingByName(decouplingCategory, decouplingCategory.getProduct().getProductId());
		StringBuffer errorMessage = new StringBuffer();
		if (existing) {		
			appendErrorMessage(errorMessage, TAFConstants.PRODUCT_DECOUPLINGCATEGORY_DUPLICATE_MESSAGE);			
		}	
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	
	public static String validateForNewProductEnvironmentAddition(Environment environment, ProductListService productListService) {	
		boolean existing = productListService.isProductEnvironmentExistingByName(environment);
		
		StringBuffer errorMessage = new StringBuffer();
		if (existing) {		
			appendErrorMessage(errorMessage, TAFConstants.PRODUCT_ENVIRONMENT_DUPLICATE_MESSAGE);			
		}	
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	
	public static String validateForNewWorkPackageAddition(String workPackageName, WorkPackageService workPackageService) {	
		WorkPackage workPackage = workPackageService.getWorkPackageByWorkPackageName(workPackageName);
		
		boolean existing = false;
		
		if(workPackage!=null && workPackage.getName().equalsIgnoreCase(workPackageName))
			existing=true;
		
		StringBuffer errorMessage = new StringBuffer();
		if (existing) {				
			appendErrorMessage(errorMessage, TAFConstants.WORKPACKAGE_DUPLICATE_MESSAGE);			
		}
		if (errorMessage.length() == 0){
			return null;
		}
		else{
			return errorMessage.toString();
		}
	}
	
	public static String validateForNewProductBuildAddition(ProductBuild productBuild, ProductListService productListService) {		
		boolean existing = productListService.isProductBuildExistingByName(productBuild);
		StringBuffer errorMessage = new StringBuffer();
		if (existing) {
			appendErrorMessage(errorMessage, TAFConstants.PRODUCT_BUILD_DUPLICATE_MESSAGE);
		}
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}

	public static String validateForNewProductVersionAddition(ProductVersionListMaster productVersionListMaster, ProductListService productListService, String addorupdate) {
		boolean existing = false;
		if(addorupdate.equalsIgnoreCase("add")){
			 existing = productListService.isProductVersionExistingByName(productVersionListMaster);	
		}else if(addorupdate.equalsIgnoreCase("update")){
			 existing = productListService.isProductVersionExistingByNameForUpdate(productVersionListMaster, productVersionListMaster.getProductVersionListId());
		}
		
		ProductVersionListMaster productVersionListMasterFromDB = productListService.getProductVersionListMasterByName(productVersionListMaster.getProductVersionName().trim());
		Integer status =0;
		if (productVersionListMasterFromDB == null) {
			status=1;
			return null;
		}
		else{
			status = productVersionListMasterFromDB.getStatus();
		}
		StringBuffer errorMessage = new StringBuffer();
		if (existing) {
			
			appendErrorMessage1(errorMessage, status,TAFConstants.PRODUCT_VERSION_DUPLICATE_MESSAGE);
		}
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	
	public static String validateForNewProductAddition(String productName, ProductListService productListService) {
		
		Integer status =0;
		boolean existing = productListService.isProductExistingByName(productName);
		ProductMaster productMasterFromDB = productListService.getProductByName(productName);
		if (productMasterFromDB == null) {
			status=1;
			return null;
		}
		else{
			status = productMasterFromDB.getStatus();
		}
		StringBuffer errorMessage = new StringBuffer();
		if (existing) {
			
			appendErrorMessage1(errorMessage, status,TAFConstants.PRODUCT_DUPLICATE_MESSAGE);
		}
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}

	public static String validateForNewTestConfigurationChildAddition(String testRunConfigurationName,TestRunConfigurationService testRunConfigurationService) {
		
		Integer status;
		boolean existing = testRunConfigurationService.isTestConfigurationChildExistingByName(testRunConfigurationName);
		TestRunConfigurationChild testRunConfigurationChildFromDB = testRunConfigurationService.getByTestRunConfigurationChildName(testRunConfigurationName.trim());
		
		if (testRunConfigurationChildFromDB == null) {
			status=1;
			return null;
		}
		else{
			status = testRunConfigurationChildFromDB.getStatus();
			
		}
		StringBuffer errorMessage = new StringBuffer();
		
		if (existing) {
			
			appendErrorMessage1(errorMessage, status,TAFConstants.TESTCONFIGURATIONCHILD_DUPLICATE_MESSAGE);
		}
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	
	public static String validateForNewTER(JsonWorkPackageTestCaseExecutionPlanForTerminal workPackageTestCaseExecutionPlan) {
		
		StringBuffer errorMessage = new StringBuffer();
		if (workPackageTestCaseExecutionPlan.getTestcaseName() == null || workPackageTestCaseExecutionPlan.getTestcaseName().trim().equals(""))
			appendErrorMessage(errorMessage, "Testcase Name is missing.");
		if (workPackageTestCaseExecutionPlan.getTestStepsName() == null || workPackageTestCaseExecutionPlan.getTestStepsName().trim().equals(""))
			appendErrorMessage(errorMessage, "Teststep name is missing.");
		if (workPackageTestCaseExecutionPlan.getTestStepResult() == null || workPackageTestCaseExecutionPlan.getTestStepResult().trim().equals(""))
			appendErrorMessage(errorMessage, "Test result is missing.");
		if (workPackageTestCaseExecutionPlan.getWorkPackageId() < 0)
			appendErrorMessage(errorMessage, "Test Run ref is invalid : " + workPackageTestCaseExecutionPlan.getWorkPackageId());
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}

	public static String validateForNewTER(JsonTestExecutionResult testExecutionResult) {
		
		StringBuffer errorMessage = new StringBuffer();
		if (testExecutionResult.getTestCase() == null || testExecutionResult.getTestCase().trim().equals(""))
			appendErrorMessage(errorMessage, "Testcase Name is missing.");
		if (testExecutionResult.getTestStep() == null || testExecutionResult.getTestStep().trim().equals(""))
			appendErrorMessage(errorMessage, "Teststep name is missing.");
		if (testExecutionResult.getTestResultStatus() == null || testExecutionResult.getTestResultStatus().trim().equals(""))
			appendErrorMessage(errorMessage, "Test result is missing.");
		if (testExecutionResult!=null && testExecutionResult.getTestRunListId()!=null && testExecutionResult.getTestRunListId() < 0)
			appendErrorMessage(errorMessage, "Test Run ref is invalid : " + testExecutionResult.getTestRunListId());
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	
	public static String validateForNewDeviceAddition(GenericDevices deviceList) {
		
		StringBuffer errorMessage = new StringBuffer();
		if (deviceList.getUDID() == null || deviceList.getUDID().trim().equals(""))
			appendErrorMessage(errorMessage, "Device Id is missing.");
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}

	public static String validateForDimensionAdditionOrUpdation(String dimensionName, DimensionService dimensionService, Integer referenceId, Integer dimensionTypeId) {
		
		Integer status =0;
		DimensionMaster dimensionMaster = dimensionService.getDimensionByNameAndType(dimensionName, referenceId, dimensionTypeId);
		StringBuffer errorMessage = new StringBuffer();
		
		if (dimensionMaster == null) {
			return null;
		}
		else{
			status = dimensionMaster.getStatus();
			appendErrorMessage1(errorMessage, status,TAFConstants.DIMENSION_DUPLICATE_MESSAGE);
		}
		
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	
	public static String validateForCompetencyMemberAdditionOrUpdation(Integer userId, CompetencyService competencyService, Integer status, Integer competencyMemberId, Date startDate, Date endDate) {
		
		CompetencyMember competencyMember = competencyService.getMemberMappedStatusByUserId(userId, status, competencyMemberId, startDate, endDate);
		StringBuffer errorMessage = new StringBuffer();
		
		if (competencyMember == null) {
			return null;
		}
		else{
			appendErrorMessage1(errorMessage, competencyMember.getStatus(),TAFConstants.COMPETENCY_MEMBER_DUPLICATE_MESSAGE);
		}
		
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}

	public static String validateForDimensionProductAdditionOrUpdation(Integer productId, Integer dimensionId, DimensionService dimensionService, Integer dimensionProductId) {
		DimensionProduct dimensionProduct = dimensionService.getDimensionProductMappedByProductId(productId, dimensionId, dimensionProductId);
		StringBuffer errorMessage = new StringBuffer();
		
		if (dimensionProduct == null) {
			return null;
		}else{
			appendErrorMessage1(errorMessage, dimensionProduct.getStatus(),TAFConstants.DIMENSION_PRODUCT_DUPLICATE_MESSAGE);
		}
		
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	
	public static String validateForDataExtarctorAdditionOrUpdation(String dataExtractorJobName, DataSourceExtractorService dataSourceExtractorService, Integer dataExtractorId) {
		
		Integer status =0;
		DataExtractorScheduleMaster dataExtractorScheduleMaster = dataSourceExtractorService.getDataExtractorByJobName(dataExtractorJobName, dataExtractorId);
		StringBuffer errorMessage = new StringBuffer();
		
		if (dataExtractorScheduleMaster == null) {
			return null;
		}
		else{
			status = dataExtractorScheduleMaster.getStatus();
			appendErrorMessage1(errorMessage, status,TAFConstants.DATA_EXTRACTOR_DUPLICATE_MESSAGE);
		}
		
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	
	public static StringBuffer appendErrorMessage(StringBuffer errorMessage, String message) {
		
		if (errorMessage.length() == 0)
			errorMessage.append(message);
		else
			errorMessage.append(/*System.lineSeparator()*/"\n" + message);
		
		return errorMessage;
	}
	public static StringBuffer appendErrorMessage1(StringBuffer errorMessage, int status,String message) {
	if(status==1)
		stus ="Active";
	else
		stus ="InActive";
		
		if (errorMessage.length() == 0)
			errorMessage.append(stus +" "+message);
		else
			errorMessage.append(/*System.lineSeparator()*/"\n" + stus + message);
		
		return errorMessage;
	}

	public static String validateForPrimaryStatusAdditionOrUpdation(Integer statusId, String activityStatusName, StatusService statusService, Integer activityStatusId) {
		ActivityStatus activityStatus = statusService.getPrimaryStatusAvailability(statusId, activityStatusName, activityStatusId);
		StringBuffer errorMessage = new StringBuffer();
		
		if (activityStatus == null) {
			return null;
		}
		else{
			appendErrorMessage1(errorMessage, 1, TAFConstants.PRIMARY_STATUS_DUPLICATE_MESSAGE);
		}
		
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}

	public static String validateForSecondaryStatusAdditionOrUpdation(Integer statusId, String activityStatusName, StatusService statusService, Integer activityStatusId) {
		ActivitySecondaryStatusMaster activitySecondaryStatusMaster = statusService.getSecondaryStatusAvailability(statusId, activityStatusName, activityStatusId);
		StringBuffer errorMessage = new StringBuffer();
		
		if (activitySecondaryStatusMaster == null) {
			return null;
		}
		else{
			appendErrorMessage1(errorMessage, 1, TAFConstants.SECONDARY_STATUS_DUPLICATE_MESSAGE);
		}
		
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}

	public static String validateForSecondaryStatusMappingForPrimaryStatus(Integer primaryStatusId, Integer secondaryStatusId, StatusService statusService) {
		StringBuffer errorMessage = new StringBuffer();
		boolean isAlreadyMapped = statusService.isSecondaryStatusAlreadyMappedToPrimaryStatus(primaryStatusId, secondaryStatusId);
		if (!isAlreadyMapped) {
			return null;
		}
		else{
			errorMessage.append(TAFConstants.SECONDARY_STATUS_ALREADY_MAPPED_MESSAGE);
		}
		
		if (errorMessage.length() == 0)
			return null;
		else
			return errorMessage.toString();
	}
	
	public static int returnVersionTestSuiteId(String testSuiteName,Integer versionId, TestSuiteConfigurationService testSuiteConfigurationService){
		int testSuiteId = 0;
		List<TestSuiteList> existingTestSuite = testSuiteConfigurationService.returnVersionTestSuiteId(testSuiteName, versionId);	
		if (existingTestSuite != null && existingTestSuite.size() > 0){
			for(TestSuiteList ts: existingTestSuite){
				testSuiteId  = ts.getTestSuiteId();
			}			
		} 
		return testSuiteId;
	}

	public static String validateForWorkflowStatusMapping(Integer workflowId, Integer sourceStatusId, Integer targetStatusId, WorkflowStatusService workflowStatusService) {
		String errorMessage = null;
		try{
			boolean isAlreadyMapped = workflowStatusService.isWorkflowStatusAlreadyMapped(workflowId, sourceStatusId, targetStatusId);
			if(isAlreadyMapped){
				errorMessage = "Workflow status already mapped";
			}
		}catch(Exception ex){
			errorMessage = "Error in mapping workflow status";
		}
		return errorMessage;
	}

	public static String validateForCustomFieldAdditionOrUpdation(JsonCustomFieldConfigMaster jsonCustomFieldConfigMaster, CustomFieldService customFieldService, Integer isActive) {
		String errorMessage = null;
		try{
			Boolean isCustomFieldAlreadyExist = customFieldService.isCustomFieldAreadyExistWithProperties(jsonCustomFieldConfigMaster.getId(), jsonCustomFieldConfigMaster.getFieldName(), jsonCustomFieldConfigMaster.getEntityId(), jsonCustomFieldConfigMaster.getEntityTypeId(), jsonCustomFieldConfigMaster.getParentEntityId(), jsonCustomFieldConfigMaster.getParentEntityInstanceId(), isActive);
			if(isCustomFieldAlreadyExist){
				errorMessage = "Custom filed with same name already exist for given properties";
			}
		}catch(Exception ex){
			log.error("Error in validateForCustomFieldAdditionOrUpdation - ", ex);
			errorMessage = "Error in custom field processing";
		}
		return errorMessage;
	}
}
