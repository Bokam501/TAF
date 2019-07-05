package com.hcl.atf.taf.integration.defectManagementSystem.jira;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.integration.defectManagementSystem.TAFDefectManagementSystemIntegrator;
import com.hcl.atf.taf.model.DefectExportData;
import com.hcl.atf.taf.model.DefectManagementSystem;
import com.hcl.atf.taf.model.Evidence;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestStepExecutionResult;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.service.DefectManagementService;
import com.hcl.atf.taf.service.DeviceListService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.TestExecutionBugsService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.atf.taf.util.ZipTool;
import com.hcl.jira.rest.JiraConnector;


public class JiraIntegrator implements TAFDefectManagementSystemIntegrator {

	@Autowired
	TestExecutionBugsService testExecutionBugsService;

	@Autowired
	TestExecutionService testExecutionService;;

	@Autowired
	DeviceListService deviceListService;

	@Autowired
	DefectManagementService defectManagementService;
	
	@Autowired
	WorkPackageService workPackageService;

	@Autowired
	EventsService eventsService;
	
	// Priorities
	private static final int PRIORITY_CRITICAL = 1;
	private static final int PRIORITY_HIGH = 2;
	private static final int PRIORITY_MEDIUM = 3;
	private static final int PRIORITY_TRIVIAL = 4;

	private static final String defectManagementSystem = TAFConstants.DEFECT_MANAGEMENT_SYSTEM_JIRA;
	private static final String defectManagementSystemVersion = TAFConstants.DEFECT_MANAGEMENT_SYSTEM_JIRA_VERSION_6;

	private static final String STATUS_NO_PRODUCT = "No defects filed. No matching product in JIRA.";
	private static final String STATUS_NO_PRODUCT_VERSION = "No defects filed. No matching product version in JIRA.";
	private static final String STATUS_DEFECT_EXISTING = "Defect(s) already filed.";
	private static final String STATUS_DEFECT_FILED = " - Defect(s) filed.";
	private static final String STATUS_VALID_PROD_VERSION = "Valid Product and Product Version.";
	private static final String STATUS_NO_DEFECT_FILED = "No Defects filed.";
	private static final String STATUS_NO_DEFECT_SYSTEM = "No Defect System associated.";
	private static final Log log = LogFactory.getLog(JiraIntegrator.class);

	@Override
	public String fileDefectToJira(int testExecResultBugId,
			JiraConnector jiraConnector) {

		int defectCounter = 0;
		String status = "";
		TestExecutionResultBugList testExecutionResultBug = testExecutionBugsService
				.getByBugId(testExecResultBugId);
		List<TestExecutionResultBugList> testExecutionResultBugs = new ArrayList<TestExecutionResultBugList>();
		testExecutionResultBugs.add(testExecutionResultBug);
		status = jiraDefectsFile(testExecutionResultBugs, jiraConnector);

		return status;
	}

	@Override
	public String fileDefectsToJira(int testRunNo,
			int testRunConfigurationChildId, JiraConnector jiraConnector) {

		int defectCounter = 0;
		String status = "";
		List<TestExecutionResultBugList> testExecutionResultBugs = testExecutionBugsService
				.listByTestRun(testRunNo, testRunConfigurationChildId);
		status = jiraDefectsFile(testExecutionResultBugs, jiraConnector);

		return status;
	}


	@Override
	public String fileDefectsToJira(int workpackageId, JiraConnector jiraConnector) {

		int defectCounter = 0;
		String status = "";
		List<TestExecutionResultBugList> testExecutionResultBugs = workPackageService.listDefectsByWorkpackageId(workpackageId);
				
		status = jiraDefectsFile(testExecutionResultBugs, jiraConnector);

		return status;
	}

		
	@Override
	public String fileDefectsToJira(int workpackageId, JiraConnector jiraConnector,TestExecutionResultBugList testExecutionResultBugList) {

		int defectCounter = 0;
		String status = "";
		List<TestExecutionResultBugList> testExecutionResultBugs = new ArrayList<TestExecutionResultBugList>();
		testExecutionResultBugs.add(testExecutionResultBugList);
		status = jiraDefectsFile(testExecutionResultBugs, jiraConnector);

		return status;
	}
	
	@Override
	public String fileDeviceDefectsToJira(int testRunNo,
			int testRunConfigurationChildId, int deviceListId,
			JiraConnector jiraConnector) {

		int defectCounter = 0;
		String status = "";
		List<TestExecutionResultBugList> testExecutionResultBugs = testExecutionBugsService
				.list(testRunNo, testRunConfigurationChildId, deviceListId);
		status = jiraDefectsFile(testExecutionResultBugs, jiraConnector);
		return status;
	}
	
	@Override
	@Transactional
	public String jiraDefectsFile(Set<TestExecutionResultBugList> testExecutionResultBugsList,JiraConnector jiraConnector) {

		int defectCounter = 0;
		String status = STATUS_NO_DEFECT_FILED;
		String productName = "";
		String IssueTypename = "";
		String summary = "";
		String priorityName = "";
		String environment = "";
		String description = "";
		String component = "";
		String[] productVersionName = new String[] { "" };
		String returnKey = "";
		GenericDevices deviceList = null;
		HostList hostList = null;

		TestCaseList testCaseList = null;
		TestCaseStepsList testStepList = null;
		Set<TestStepExecutionResult> testStepExecutionResults = null;

		String deviceModel = "";
		String deviceMake = "";
		String devicePlatformName = "";
		String devicePlatformVersion = "";
		TestExecutionResultBugList testExecutionResultBug = null;
		int defectManagementSystemId = 0;
		int productId = 0;
		DefectManagementSystem defectSystem = null;
		DefectExportData defectsExportData = null;
		String evidenceFolderPath = getAppProperties();
		ProductMaster productMaster=null;
		if (testExecutionResultBugsList != null&& !testExecutionResultBugsList.isEmpty()) {

			// Validate Product and Version
			Iterator iter = testExecutionResultBugsList.iterator();

			testExecutionResultBug = (TestExecutionResultBugList) iter.next();
			TestCaseExecutionResult testExecResult = testExecutionResultBug.getTestCaseExecutionResult();
			productMaster=testExecResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster();
			productId =testExecResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
			
			defectManagementSystemId = defectManagementService.getDefectManagementSystemId(productId,defectManagementSystem,defectManagementSystemVersion);
			if (defectManagementSystemId <= 0) {
					status = STATUS_NO_DEFECT_SYSTEM;
					return status;
			} else {
				productName = defectManagementService.getDefectSystemMappingProductName(defectManagementSystemId, productId);
				//productVersionName[0] = defectManagementService	.getDefectSystemMappingProductVersion(defectManagementSystemId, productId);
				if (productName == null || productName != null&& productName.isEmpty()) {
					status = STATUS_NO_PRODUCT;
					return status;
				}
				/*if (productVersionName[0] == null|| productVersionName[0] != null&& productVersionName[0].isEmpty()) {
					status = STATUS_NO_PRODUCT_VERSION;
					return status;
				}*/
				defectSystem = defectManagementService.getByDefectManagementSystemId(defectManagementSystemId);
			}
		}

		TestCaseExecutionResult testCaseExecutionResult=null; 
		String deviceId="";
		for (TestExecutionResultBugList testExecutionResultBugList : testExecutionResultBugsList) {
			if (testExecutionResultBugList == null	) {
				
				status = STATUS_DEFECT_EXISTING;
			} else {
				summary = testExecutionResultBugList.getBugTitle();
				description = testExecutionResultBugList.getBugDescription();
				if(defectSystem.getDefectSystemVersion().startsWith("6")){
					priorityName = "Minor";
				} else if(defectSystem.getDefectSystemVersion().startsWith("7")){
					priorityName = "Medium";
				}
				IssueTypename = "Bug";
				component = "";
				String failureReason = "";
				testCaseExecutionResult=testExecutionResultBugList.getTestCaseExecutionResult();
				
				if (testCaseExecutionResult != null) {
					if ((testCaseExecutionResult != null)&& testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration() != null) {
						environment = testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getRunconfigName();
						if(productMaster.getProductType().getProductTypeId() == IDPAConstants.PRODUCT_TYPE_DEVICE || productMaster.getProductType().getProductTypeId() == IDPAConstants.PRODUCT_TYPE_MOBILE){
							deviceList=testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice();
							if (deviceList != null) {
								deviceId = deviceList.getUDID();
								
								if(deviceList.getDeviceModelMaster()!=null)
								{
								deviceModel = deviceList.getDeviceModelMaster().getDeviceModel();
								deviceMake = deviceList.getDeviceModelMaster().getDeviceMakeMaster().getDeviceMake();
								}
								if( deviceList.getPlatformType()!=null){
								devicePlatformName = deviceList.getPlatformType().getName();
								devicePlatformVersion = deviceList.getPlatformType().getVersion();
								}
								environment = deviceModel + "," + deviceMake + ","+ devicePlatformName + ","+ devicePlatformVersion;
							}
						}else if(productMaster.getProductType().getProductTypeId() == IDPAConstants.PRODUCT_TYPE_WEB || productMaster.getProductType().getProductTypeId() == IDPAConstants.PRODUCT_TYPE_EMBEDDED){
							hostList=testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getHostList();
							environment=environment+","+hostList.getHostIpAddress()+","+hostList.getHostName();
						}
					}
					testCaseList = testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getTestCase();
					testStepExecutionResults = testCaseExecutionResult.getTestStepExecutionResultSet();
					failureReason = testCaseExecutionResult.getFailureReason();
					//Added RegEx for removing any special characters exist in the failureReason String while posting
					if(failureReason != null && !failureReason.isEmpty())
						failureReason = failureReason.replace("{", "").replace("}", "").replace("/", "").replace("\\", "");
					if (testCaseList != null) {
						description = "Test Case : "+ testCaseList.getTestCaseName() + " ";
					}
					String testStepName="";
					String testStepCode="";
					Integer testStepId = 0;
					
					//Logic changes for Defect Reporting on a test step execution result - starts
					if(testExecutionResultBugList != null){
						if(testExecutionResultBugList.getTestStepExecutionResult() != null && testExecutionResultBugList.getTestStepExecutionResult().getTeststepexecutionresultid() != null && testExecutionResultBugList.getTestStepExecutionResult().getTeststepexecutionresultid() != 0){
							TestStepExecutionResult testStepExecutionResult = workPackageService.getTestStepExecutionResultById(testExecutionResultBugList.getTestStepExecutionResult().getTeststepexecutionresultid());
							testStepList=testStepExecutionResult.getTestSteps();
							if (testStepList != null) {							
								if(testStepList.getTestStepName()!=null){
									testStepName = testStepList.getTestStepName();
									description += "Test Step : "+testStepList.getTestStepName()+" ";
								}else{
									testStepName = "Not Available";
									description += "Test Step : Not Available" + " ";
								}
								
								if(testStepList.getTestStepId() != null){
									testStepId = testStepList.getTestStepId();
									description += "Test Step ID : "+ testStepList.getTestStepId()+" ";
								} else {
									testStepId = 0;
									description += "Test Step ID : 0" + " ";
								}								
								
								if(testCaseList.getTestCaseSource() != null && !TAFConstants.TESTCASE_SOURCE_TAF.equalsIgnoreCase(testCaseList.getTestCaseSource())){
									if(testStepList.getTestStepCode() != null){
										testStepCode = testStepList.getTestStepCode();
										description += "Test Step Code : "+ testStepList.getTestStepCode()+" ";
									} else {
										testStepCode = "Not Available";
										description += "Test Step Code : Not Available" + " ";
									}
								}								
							}	
						}								 
					}							
					
					if (failureReason != null) {
						description = description + " "+ " Failure Reason : " + failureReason + " ";
					}else{
						description = description + " "+ " Failure Reason : " + "NA" + " ";
					}
					if(testExecutionResultBugList.getBugDescription()!=null){
						description = description+ " "+ testExecutionResultBugList.getBugDescription();
					}
	
					if (testCaseList != null && testCaseList.getTestCasePriority() != null) {
						priorityName = testCaseList.getTestCasePriority().getPriorityName();
						if(defectSystem.getDefectSystemVersion().startsWith("6")){
							priorityName= getPriority(priorityName);
						} else if(defectSystem.getDefectSystemVersion().startsWith("7")){
							priorityName= getJira7Priority(priorityName);
						}
					}
					summary = testExecutionResultBugList.getBugTitle();
					
					if (testCaseList != null) {
						if (testCaseList.getTestCaseSource() != null&& !TAFConstants.TESTCASE_SOURCE_TAF.equalsIgnoreCase(testCaseList.getTestCaseSource())
								&& (testCaseList.getTestCaseCode() != null && !testCaseList.getTestCaseCode().isEmpty()) && testCaseList.getTestCaseName() != null && testCaseList.getTestCaseId() != null) {
							summary = summary + " TC Source : "+ testCaseList.getTestCaseSource()+ " TC : "+testCaseList.getTestCaseName()+" TC ID : "+ testCaseList.getTestCaseId()+ "  TC Code : "+ testCaseList.getTestCaseCode()+ "  TS : "+testStepName+" TS ID : "+testStepId+" TS Code : "+testStepCode;
						} else if (testCaseList.getTestCaseSource() != null&& TAFConstants.TESTCASE_SOURCE_TAF.equalsIgnoreCase(testCaseList.getTestCaseSource()) && testCaseList.getTestCaseName() != null && testCaseList.getTestCaseId() != null) {
							summary = summary + " TC Source : "+ testCaseList.getTestCaseSource()+ " TC : "+testCaseList.getTestCaseName()+" TC ID : "+ testCaseList.getTestCaseId() + " TS : "+testStepName+" TS ID : "+testStepId;
						}
					}
					
					if(summary.length() > 255){
						summary = summary.substring(0, 255).trim();
						log.info("Summary length: "+summary.length());
					}
					
					if(description != null && !description.isEmpty())
						description = description.replace("{", "").replace("}", "").replace("/", "").replace("\\", "");
					
					log.info("JIRA defect insert for bug :" + summary);
					log.info("environment:"+environment);
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
						returnKey = jiraConnector.insertDefect(productName,IssueTypename, summary, priorityName,environment, description, null,productVersionName);
	
					} catch (Exception e) {
						log.error("Unable to create defects");
					}
	
					
					
					List<Evidence> evidenceList=null;
					if (returnKey != null && !returnKey.isEmpty()) {
						try {
							int runListId = testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkPackageId();
							int testcaseid =testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseId(); 
						
							String ZipFileName = returnKey+ "_"+ testcaseid+ "_"+ testExecutionResultBugList.getTestCaseExecutionResult().
								getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseName();
							String zipFolderPath = evidenceFolderPath + File.separator+ testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestRunJob() + File.separator+ TAFConstants.BUG_IMAGES_TO_TMS;
							File zipfolder = new File(zipFolderPath);
							if (!zipfolder.exists()) {
								zipfolder.mkdirs();
							}
							zipFolderPath =  zipFolderPath+  File.separator + ZipFileName;
							String filepath = CommonUtility.getCatalinaPath()+File.separator;
							ArrayList<File> listfiles = new ArrayList<File>();
							evidenceList=workPackageService.testcaseListByEvidence(testCaseExecutionResult.getTestCaseExecutionResultId(), "testcase");
							if (evidenceList!=null && !evidenceList.isEmpty()) {
								for (Evidence evd : evidenceList) {
									log.info("file path>>"+filepath+ evd.getFileuri());
									File file = new File(filepath+ evd.getFileuri());
									if (file.exists())
										listfiles.add(file);
								}
							}
							if (listfiles.size() > 0) {
								try{
									String returm = ZipTool.dozipFiles(zipFolderPath, listfiles);
									if(!returm.equals("")){
										if(ZipTool.isValidZipArchive(zipFolderPath+ ".zip")){
											jiraConnector.uploadfile(returnKey, zipFolderPath+ ".zip");
										}
									}
								}catch (Exception e) {
									e.printStackTrace();
									log.error("Unable to attach  file in jira");
								}
							}
							File zipfile = new File(zipFolderPath + ".zip");
							if (zipfile.exists()) {
								zipfile.delete();
							}
						} catch (Exception e) {
							e.printStackTrace();
							log.error("Exception in attaching the screenshots in JIRA");
						}
	
						testExecutionResultBugList.setBugManagementSystemBugId(returnKey);
						WorkFlow workFlow=workPackageService.getWorkFlowByEntityIdStageId(1, 6);
						testExecutionResultBugList.setBugFilingStatus(workFlow);
						testExecutionResultBugList.setBugManagementSystemName(TAFConstants.DEFECT_MANAGEMENT_SYSTEM_JIRA);
						testExecutionBugsService.update(testExecutionResultBugList);
	
						defectsExportData = new DefectExportData();
						defectsExportData.setDefectManagementSystemId(defectManagementSystemId);
						defectsExportData.setDefectSystemCode(returnKey);
						defectsExportData.setTestExecutionResultsBugId(testExecutionResultBugList.getTestExecutionResultBugId());
	
						testExecutionBugsService.addDefectExportData(defectsExportData);
	
						defectCounter++;
					} else {
						log.info("Defects not filed in JIRA");
					}
				}//
			}//else
		}
		if (defectCounter > 0) {
			status = defectCounter + STATUS_DEFECT_FILED;
		}
		return status;
	}

	@Override
	@Transactional
	public String jiraDefectsFile(List<TestExecutionResultBugList> testExecutionResultBugsList,JiraConnector jiraConnector) {

		int defectCounter = 0;
		String status = STATUS_NO_DEFECT_FILED;
		String productName = "";
		String IssueTypename = "";
		String summary = "";
		String priorityName = "";
		String environment = "";
		String description = "";
		String component = "";
		String[] productVersionName = new String[] { "" };
		String returnKey = "";
		GenericDevices deviceList = null;
		HostList hostList = null;

		TestCaseList testCaseList = null;
		TestCaseStepsList testStepList = null;
		Set<TestStepExecutionResult> testStepExecutionResults = null;

		String deviceModel = "";
		String deviceMake = "";
		String devicePlatformName = "";
		String devicePlatformVersion = "";
		TestExecutionResultBugList testExecutionResultBug = null;
		int defectManagementSystemId = 0;
		int productId = 0;
		DefectManagementSystem defectSystem = null;
		DefectExportData defectsExportData = null;
		String evidenceFolderPath = getAppProperties();
		ProductMaster productMaster=null;
		if (testExecutionResultBugsList != null&& !testExecutionResultBugsList.isEmpty()) {

			// Validate Product and Version
			testExecutionResultBug = testExecutionResultBugsList.get(0);
			TestCaseExecutionResult testExecResult = testExecutionResultBug.getTestCaseExecutionResult();
			productMaster=testExecResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster();
			productId =testExecResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
			
			defectManagementSystemId = defectManagementService.getDefectManagementSystemId(productId,defectManagementSystem,defectManagementSystemVersion);
			if (defectManagementSystemId <= 0) {
					status = STATUS_NO_DEFECT_SYSTEM;
					return status;
			} else {
				productName = defectManagementService.getDefectSystemMappingProductName(defectManagementSystemId, productId);
				//productVersionName[0] = defectManagementService	.getDefectSystemMappingProductVersion(defectManagementSystemId, productId);
				if (productName == null || productName != null&& productName.isEmpty()) {
					status = STATUS_NO_PRODUCT;
					return status;
				}
				/*if (productVersionName[0] == null|| productVersionName[0] != null&& productVersionName[0].isEmpty()) {
					status = STATUS_NO_PRODUCT_VERSION;
					return status;
				}*/
				defectSystem = defectManagementService.getByDefectManagementSystemId(defectManagementSystemId);
			}
		}

		TestCaseExecutionResult testCaseExecutionResult=null; 
		String deviceId="";
		for (TestExecutionResultBugList testExecutionResultBugList : testExecutionResultBugsList) {
			if (testExecutionResultBugList == null	) {
				
				status = STATUS_DEFECT_EXISTING;
			} else {
				summary = testExecutionResultBugList.getBugTitle();
				description = testExecutionResultBugList.getBugDescription();
				
				if(defectSystem.getDefectSystemVersion().startsWith("6")){
					priorityName = "Minor";
				} else if(defectSystem.getDefectSystemVersion().startsWith("7")){
					priorityName = "Medium";
				}
				IssueTypename = "Bug";
				component = "";
				String failureReason = "";
				testCaseExecutionResult=testExecutionResultBugList.getTestCaseExecutionResult();
				if (testCaseExecutionResult != null) {
					if ((testCaseExecutionResult != null)&& testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration() != null) {
						environment = testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getRunconfigName();
						if(productMaster.getProductType().getProductTypeId() == IDPAConstants.PRODUCT_TYPE_DEVICE || productMaster.getProductType().getProductTypeId() == IDPAConstants.PRODUCT_TYPE_MOBILE){
							deviceList=testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice();
							if (deviceList != null) {
								deviceId = deviceList.getUDID();
								
								if(deviceList.getDeviceModelMaster()!=null)
								{
								deviceModel = deviceList.getDeviceModelMaster().getDeviceModel();
								deviceMake = deviceList.getDeviceModelMaster().getDeviceMakeMaster().getDeviceMake();
								}
								if( deviceList.getPlatformType()!=null){
								devicePlatformName = deviceList.getPlatformType().getName();
								devicePlatformVersion = deviceList.getPlatformType().getVersion();
								}
								environment = deviceModel + "," + deviceMake + ","+ devicePlatformName + ","+ devicePlatformVersion;
							}
						}else if(productMaster.getProductType().getProductTypeId() == IDPAConstants.PRODUCT_TYPE_WEB || productMaster.getProductType().getProductTypeId() == IDPAConstants.PRODUCT_TYPE_EMBEDDED){
							hostList=testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getHostList();
							environment=environment+","+hostList.getHostIpAddress()+","+hostList.getHostName();
						}
					}
					testCaseList = testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getTestCase();
					testStepExecutionResults = testCaseExecutionResult.getTestStepExecutionResultSet();
					failureReason = testCaseExecutionResult.getFailureReason();
					//Added RegEx for removing any special characters exist in the failureReason String while posting
					if(failureReason != null && !failureReason.isEmpty())
						failureReason = failureReason.replace("{", "").replace("}", "").replace("/", "").replace("\\", "");
					if (testCaseList != null) {
						description = "Test Case : "+ testCaseList.getTestCaseName() + " ";
					}
					String testStepName="";
					String testStepCode="";
					Integer testStepId = 0;
					
					//Logic changes for Defect Reporting on a test step execution result - starts
					if(testExecutionResultBugList != null){
						if(testExecutionResultBugList.getTestStepExecutionResult() != null && testExecutionResultBugList.getTestStepExecutionResult().getTeststepexecutionresultid() != null && testExecutionResultBugList.getTestStepExecutionResult().getTeststepexecutionresultid() != 0){
							TestStepExecutionResult testStepExecutionResult = workPackageService.getTestStepExecutionResultById(testExecutionResultBugList.getTestStepExecutionResult().getTeststepexecutionresultid());
							testStepList=testStepExecutionResult.getTestSteps();
							if (testStepList != null) {							
								if(testStepList.getTestStepName()!=null){
									testStepName = testStepList.getTestStepName();
									description += "Test Step : "+testStepList.getTestStepName()+" ";
								}else{
									testStepName = "Not Available";
									description += "Test Step : Not Available" + " ";
								}
								
								if(testStepList.getTestStepId() != null){
									testStepId = testStepList.getTestStepId();
									description += "Test Step ID : "+ testStepList.getTestStepId()+" ";
								} else {
									testStepId = 0;
									description += "Test Step ID : 0" + " ";
								}								
								
								if(testCaseList.getTestCaseSource() != null && !TAFConstants.TESTCASE_SOURCE_TAF.equalsIgnoreCase(testCaseList.getTestCaseSource())){
									if(testStepList.getTestStepCode() != null){
										testStepCode = testStepList.getTestStepCode();
										description += "Test Step Code : "+ testStepList.getTestStepCode()+" ";
									} else {
										testStepCode = "Not Available";
										description += "Test Step Code : Not Available" + " ";
									}
								}								
							}	
						}								 
					}							
	
					if (failureReason != null) {
						description = description + " "+ " Failure Reason : " + failureReason + " ";
					}else{
						description = description + " "+ " Failure Reason : " + "NA" + " ";
					}
					if(testExecutionResultBugList.getBugDescription()!=null){
						description = description+ " "+ testExecutionResultBugList.getBugDescription();
					}
	
					if (testCaseList != null && testCaseList.getTestCasePriority() != null) {
						priorityName = testCaseList.getTestCasePriority().getPriorityName();
						if(defectSystem.getDefectSystemVersion().startsWith("6")){
							priorityName= getPriority(priorityName);
						} else if(defectSystem.getDefectSystemVersion().startsWith("7")){
							priorityName= getJira7Priority(priorityName);
						}
					}
					summary = testExecutionResultBugList.getBugTitle();
					
					if (testCaseList != null) {
						if (testCaseList.getTestCaseSource() != null&& !TAFConstants.TESTCASE_SOURCE_TAF.equalsIgnoreCase(testCaseList.getTestCaseSource())
								&& (testCaseList.getTestCaseCode() != null && !testCaseList.getTestCaseCode().isEmpty()) && testCaseList.getTestCaseName() != null && testCaseList.getTestCaseId() != null) {
							summary = summary + " TC Source : "+ testCaseList.getTestCaseSource()+ " TC : "+testCaseList.getTestCaseName()+" TC ID : "+ testCaseList.getTestCaseId()+ "  TC Code : "+ testCaseList.getTestCaseCode()+ "  TS : "+testStepName+" TS ID : "+testStepId+" TS Code : "+testStepCode;
						} else if (testCaseList.getTestCaseSource() != null&& TAFConstants.TESTCASE_SOURCE_TAF.equalsIgnoreCase(testCaseList.getTestCaseSource()) && testCaseList.getTestCaseName() != null && testCaseList.getTestCaseId() != null) {
							summary = summary + " TC Source : "+ testCaseList.getTestCaseSource()+ " TC : "+testCaseList.getTestCaseName()+" TC ID : "+ testCaseList.getTestCaseId() + " TS : "+testStepName+" TS ID : "+testStepId;
						}
					}
					
					if(summary.length() > 255){
						summary = summary.substring(0, 255).trim();
						log.info("Summary length: "+summary.length());
					}
					
					if(description != null && !description.isEmpty())
						description = description.replace("{", "").replace("}", "").replace("/", "").replace("\\", "");
					
					log.info("JIRA defect insert for bug :" + summary);
					log.info("environment:"+environment);
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
						returnKey = jiraConnector.insertDefect(productName,IssueTypename, summary, priorityName,environment, description, null,productVersionName);
	
					} catch (Exception e) {
						log.error("Unable to create defects");
					}
	
					List<Evidence> evidenceList=null;
					if (returnKey != null && !returnKey.isEmpty()) {
						try {
							int runListId = testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkPackageId();
							int testcaseid =testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseId(); 
						
							String ZipFileName = returnKey+ "_"+ testcaseid+ "_"+ testExecutionResultBugList.getTestCaseExecutionResult().
								getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseName();
							String zipFolderPath = evidenceFolderPath + File.separator+ testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestRunJob() + File.separator+ TAFConstants.BUG_IMAGES_TO_TMS;
							File zipfolder = new File(zipFolderPath);
							if (!zipfolder.exists()) {
								zipfolder.mkdirs();
							}
							zipFolderPath =  zipFolderPath+  File.separator + ZipFileName;
							String filepath = CommonUtility.getCatalinaPath()+File.separator;
							ArrayList<File> listfiles = new ArrayList<File>();
							evidenceList=workPackageService.testcaseListByEvidence(testCaseExecutionResult.getTestCaseExecutionResultId(), "testcase");
							if (evidenceList!=null && !evidenceList.isEmpty()) {
								for (Evidence evd : evidenceList) {
									log.info("file path>>"+filepath+ evd.getFileuri());
									File file = new File(filepath+ evd.getFileuri());
									if (file.exists())
										listfiles.add(file);
								}
							}
							evidenceList=null;
							testStepExecutionResults = testCaseExecutionResult.getTestStepExecutionResultSet();							
							
							for (TestStepExecutionResult tser : testStepExecutionResults) {
								if(tser.getTeststepexecutionresultid()<= testExecutionResultBugList.getTestStepExecutionResult().getTeststepexecutionresultid()){
									evidenceList=workPackageService.testcaseListByEvidence(tser.getTeststepexecutionresultid(), "teststep");
									if (evidenceList!=null && !evidenceList.isEmpty()) {
										for (Evidence evd : evidenceList) {
											log.info("file path>>"+filepath+ evd.getFileuri());
											File file = new File(filepath+ evd.getFileuri());
											if (file.exists())
												listfiles.add(file);
										}
									}
								}
							}
							if (listfiles.size() > 0) {
								try{
									String returm = ZipTool.dozipFiles(zipFolderPath, listfiles);
									if(!returm.equals("")){
										if(ZipTool.isValidZipArchive(zipFolderPath+ ".zip")){
											jiraConnector.uploadfile(returnKey, zipFolderPath+ ".zip");
										}
									}
								}catch (Exception e) {
									e.printStackTrace();
									log.error("Unable to attach  file in jira");
								}
							}
							File zipfile = new File(zipFolderPath + ".zip");
							if (zipfile.exists()) {
								zipfile.delete();
							}
						} catch (Exception e) {
							e.printStackTrace();
							log.error("Exception in attaching the screenshots in JIRA");
						}
	
						testExecutionResultBugList.setBugManagementSystemBugId(returnKey);
						WorkFlow workFlow=workPackageService.getWorkFlowByEntityIdStageId(1, 6);
						testExecutionResultBugList.setBugFilingStatus(workFlow);
						testExecutionResultBugList.setBugManagementSystemName(TAFConstants.DEFECT_MANAGEMENT_SYSTEM_JIRA);
						testExecutionBugsService.update(testExecutionResultBugList);
	
						defectsExportData = new DefectExportData();
						Date defectDate = DateUtility.getCurrentTime();					
						
						defectsExportData.setDefectManagementSystemId(defectManagementSystemId);
						defectsExportData.setDefectSystemCode(returnKey);
						defectsExportData.setTestExecutionResultsBugId(testExecutionResultBugList.getTestExecutionResultBugId());			
						defectsExportData.setDefectExportDate(defectDate);
						testExecutionBugsService.addDefectExportData(defectsExportData);
	
						defectCounter++;
					} else {
						log.info("Defects not filed in JIRA");
					}
				}//
			}//else
		}
		if (defectCounter > 0) {
			status = defectCounter + STATUS_DEFECT_FILED;
		}
		return status;
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
	
	public String getJira7Priority(String testCasePriority) {

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
			priority = "Highest";
		case 2:
			priority = "High";
		case 3:
			priority = "Medium";
		case 4:
			priority = "Low";
		default:
			priority = "Medium";
		}
		return priority;
	}

	public String validateProduct(
			TestExecutionResultBugList testExecutionResultBugList) {

		String validProductOrVersion = STATUS_VALID_PROD_VERSION;
		TestSuiteList testSuiteList;
		ProductVersionListMaster prodVersion;
		ProductMaster prod;
		int productId = 0;
		String productName;
		String[] productVersionName = new String[] { "" };
		TestExecutionResult testExecResult = null;
		if (testExecResult != null) {

			testSuiteList = testExecResult.getTestSuiteList();
			prodVersion = testSuiteList.getProductVersionListMaster();

			if (prodVersion != null) {
				prod = prodVersion.getProductMaster();
				productId = prod.getProductId();
			}

			// Retrieve Mapping values in JIRA
			productName = getProductMappingInJira(productId);
			productVersionName[0] = getProductVersionMappingInJira(productId);
			if (productName == null || productName != null
					&& productName.isEmpty()) {
				validProductOrVersion = STATUS_NO_PRODUCT;
				return validProductOrVersion;
			}

			if (productVersionName[0] == null || productVersionName[0] != null
					&& productVersionName[0].isEmpty()) {
				validProductOrVersion = STATUS_NO_PRODUCT_VERSION;
				return validProductOrVersion;
			}
		}
		return validProductOrVersion;
	}

	public String getProductMappingInJira(int productId) {

		String jiraProductName = "";
		int defectManagementSystemId = defectManagementService
				.getDefectManagementSystemId(productId, defectManagementSystem,
						defectManagementSystemVersion);

		jiraProductName = defectManagementService
				.getDefectSystemMappingProductName(defectManagementSystemId,
						productId);

		return jiraProductName;
	}

	public String getProductVersionMappingInJira(int productId) {

		String jiraProductVersionName = "";
		int defectManagementSystemId = defectManagementService
				.getDefectManagementSystemId(productId, defectManagementSystem,
						defectManagementSystemVersion);
		jiraProductVersionName = defectManagementService
				.getDefectSystemMappingProductVersion(defectManagementSystemId,
						productId);

		return jiraProductVersionName;
	}

	// Added for getting the evidence folder location from properties location
	// The changes are made for attaching the screenshot while filing defects in
	// JIRA
	public String getAppProperties() {

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
			log.error("Exception in getting TAFServer.properties file");
		} catch (IOException ioe) {
			log.error("Exception in loading hpqcProperties:", ioe);
		}
		return propertyValue;
	}
}
