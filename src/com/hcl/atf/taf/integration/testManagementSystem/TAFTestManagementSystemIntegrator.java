package com.hcl.atf.taf.integration.testManagementSystem;

import java.io.File;
import java.util.List;

import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.tfs.TFSIntegrator;
import com.hcl.connector.rest.hpqcrest.ConnectorHPQCRest;
import com.hcl.hpqc.connector.ConnectorHPQC;
import com.microsoft.tfs.core.TFSTeamProjectCollection;


public interface TAFTestManagementSystemIntegrator {
	
	public String importTestCasesOTA(int productId, ConnectorHPQC hpqcConnector, String testCaseSource);
	public String importTestSetsOTA(int testRunNo, int testRunConfigChild, ConnectorHPQC hpqcConnector);	
	public String ExportTestExecutionResultsOTA(int testRunListId, ConnectorHPQC hpqcConnector);
	public String ExportTestExecutionResultsOTA(WorkPackage workPackage, ConnectorHPQC hpqcConnector);
	public String ExportTestExecutionResultsOTA(TestRunJob testRunJob, ConnectorHPQC hpqcConnector);

	public String importTestSetsOTA(ProductMaster productMaster, ConnectorHPQC hpqcConnector);
	//Changes for checking out scripts from Test Resources
	public String processTestScriptsOTA(ConnectorHPQC hpqcConnector, TestSuiteList testSuite, String path, String checkOutPath, String uploadPath, String testScriptSource);
	//Changes for Bug 792 - Adding TestSetFolder Filter by name - Starts
	public String importTestSetsRest(ProductMaster productMaster, ConnectorHPQCRest hpqcConnector, String testCaseFolder, String testSetFolder, String testSetFolderByName, String source);	 
	public String importTestSetsRest(ProductMaster productMaster, ConnectorHPQCRest hpqcConnector, String testCaseFolder, String testSetFolder, String testSetFolderByName, String source,String downloadPath);
	public String importTestCasesRest(int productId, ConnectorHPQCRest hpqcConnefexportTestExecutionResultsToTestLinkctor, String testCaseSource, String testCaseFolder, String testSetFolder,String testSetFolderByName);
	public String importTestCasesRest(int productId, ConnectorHPQCRest hpqcConnector, String testCaseSource, String testCaseFolder, String testSetFolder,String testSetFolderByName,String resourceBundleDownloadPath);
	//Changes for Bug 792 - Adding TestSetFolder Filter by name - Ends
	public String processTestScriptsRest(ConnectorHPQCRest hpqcConnector, TestSuiteList testSuite, String scriptName, String checkOutPath,
	String uploadPath, String testScriptSource,  String resourceFolderId);	
	
	
	public String exportTestExecutionResultsRest(TestRunJob testRunJob, ConnectorHPQCRest hpqcConnector,TestManagementSystem testManagementSystem);	
	public String exportTestExecutionDefectsRest(TestExecutionResultBugList testExecutionResultBugList, ConnectorHPQCRest hpqcConnector);		
	public String ExportTestExecutionDefectsRest(WorkPackage workPackage, ConnectorHPQCRest hpqcConnector,int defectManagementSystemId);
	public String exportTestExecutionDefectsRest(List<TestExecutionResultBugList> testExecutionResultBugList, ConnectorHPQCRest hpqcConnector,int defectManagementSystemId);
	public String exportTestExecutionDefectsRest(List<TestExecutionResultBugList> testExecutionResultBugList, TFSIntegrator tfsConnector,TFSTeamProjectCollection projectCollection, int defectManagementSystemId,String url);
	
	public String exportTestExecutionSelectiveDefectRest(TestExecutionResultBugList testExecutionResultBug, ConnectorHPQCRest hpqcConnector,int defectManagementSystemId);
	//Modification for Feature and TestCase Mapping
	public String importFeatures(Integer productId, ConnectorHPQCRest hpqcConnector);
	public void processFeatureTCMappings(String excelDataFilePath, Integer productId, ConnectorHPQCRest hpqcConnector);
	//Modification for uploading reports to TestSets - Bug 777
	public Boolean UploadReportsToTestSet(TestRunJob testRunJob, ConnectorHPQCRest connection, File file, String entity, String entityId);
	
	public List<String> getDomainProjectNameList(ConnectorHPQCRest hpqcConnector,String connectionProperty1);
	public String importTestCasesFromTFS(int productId, int tfsTestPlanId, int tfsTestSuiteId, String tfsProjectCollectionPath, String tfsProjectName, String source, TFSIntegrator tfsConnector, TFSTeamProjectCollection projectCollection);
	public String importTestSuiteFromTFS(ProductMaster productMaster, int tfsTestPlanId, int tfsTestSuiteId, String tfsProjectCollectionPath, String tfsProjectName, String source, TFSIntegrator tfsConnector, TFSTeamProjectCollection projectCollection);
	public List<Integer> tfsExportTestResults(TestRunJob testRunJob,TFSIntegrator tfsConnector ,TestManagementSystem testManagementSystem,TFSTeamProjectCollection projectCollection, String projectName);
	public Boolean tfsUploadReportsToTestRun(Integer tfsTestRunId, TFSIntegrator tfsConnector, TFSTeamProjectCollection projectCollection, String projectName, String fileName);
	public List<String> getDomainNameList(ConnectorHPQCRest hpqcConnector);
	public String exportWorkpackageTestExecutionDefectsRest(WorkPackage workPackage,TFSIntegrator tfsConnector,TFSTeamProjectCollection projectCollection,int defectManagementSystemId, String projectUrl);
	public String exportTFSTestRunJobTestExecutionDefectsRest(TestExecutionResultBugList bug, TFSIntegrator tfsConnector,TFSTeamProjectCollection projectCollection,int defectManagementSystemId, String projectUrl);
	public String exportTestExecutionResultsToTestLink(TestRunJob testRunJob,TestManagementSystem testManagementSystem);
}
