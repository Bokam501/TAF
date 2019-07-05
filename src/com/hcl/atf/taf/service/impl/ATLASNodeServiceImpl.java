package com.hcl.atf.taf.service.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.integration.defectManagementSystem.TAFDefectManagementSystemIntegrationFactory;
import com.hcl.atf.taf.integration.defectManagementSystem.TAFDefectManagementSystemIntegrator;
import com.hcl.atf.taf.integration.testManagementSystem.TAFTestManagementSystemIntegrationFactory;
import com.hcl.atf.taf.integration.testManagementSystem.TAFTestManagementSystemIntegrator;
import com.hcl.atf.taf.model.DefectManagementSystem;
import com.hcl.atf.taf.model.DefectManagementSystemMapping;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.LifeCyclePhase;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCycle;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestExecutionResultsExportData;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestManagementSystemMapping;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunPlanGroup;
import com.hcl.atf.taf.model.TestRunPlangroupHasTestRunPlan;
import com.hcl.atf.taf.model.TestStepExecutionResult;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.WorkFlowEvent;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.dto.ISERecommandedTestcases;
import com.hcl.atf.taf.model.dto.ScriptLessExecutionDTO;
import com.hcl.atf.taf.model.dto.VerificationResult;
import com.hcl.atf.taf.model.dto.WorkPackageBuildTestCaseSummaryDTO;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.JsonTestRunPlan;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionSummary;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.service.ATLASNodeService;
import com.hcl.atf.taf.service.DefectManagementService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.PasswordEncryptionService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestExecutionBugsService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.TestManagementService;
import com.hcl.atf.taf.service.TestReportService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.atf.taf.tfs.TFSIntegrator;
import com.hcl.atf.taf.tfs.connector.TFSConnector;
import com.hcl.connector.rest.hpqcrest.ConnectorHPQCRest;
import com.hcl.hpqc.connector.ConnectorHPQC;
import com.hcl.jira.rest.JiraConnector;
import com.hcl.ota.TestResult;
import com.hcl.result.TestCaseResult;
import com.hcl.result.TestSetResult;
import com.hcl.result.TestStepResult;
import com.hcl.types.SEVERITY;
import com.hcl.types.STATUS;
import com.microsoft.tfs.core.TFSTeamProjectCollection;

@SuppressWarnings({"unchecked", "unused", "static-access"})
public class ATLASNodeServiceImpl implements ATLASNodeService{
	
	private static final Log log = LogFactory.getLog(ATLASNodeServiceImpl.class);
	
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService;

	@Autowired
	private TAFTestManagementSystemIntegrationFactory 	tafTestManagementSystemIntegrationFactory;
	
	@Autowired
	private TAFDefectManagementSystemIntegrationFactory tafDefectManagementSystemIntegrationFactory;
	
	@Autowired
	private TAFTestManagementSystemIntegrator tafTestManagementSystemIntegrator;
	
	@Autowired
	TAFDefectManagementSystemIntegrator tafDefectManagementSystemIntegrator;
		
	@Autowired
	private ProductListService 	productListService;
	
	@Autowired
	TestManagementService testManagementService;
	
	@Autowired
	TestReportService testReportsService;
	
	@Autowired
	DefectManagementService defectManagementService;
	
	@Autowired	
	TestRunConfigurationService testRunConfigurationService;
	
	@Autowired
	private WorkPackageService workPackageService;
	
	@Autowired
	EnvironmentService environmentService;
	
	@Autowired
	TestExecutionService testExecutionService;	
	
	@Autowired
	EventsService eventsService;	
	@Autowired	
	TestExecutionBugsService testExecutionBugsService;
	
	@Autowired
	PasswordEncryptionService passwordEncryptionService;
	
	@Autowired
	private ExecutionTypeMasterService executionTypeMasterService;
	
	@Autowired
	ServletContext servletContext;
	
	public String testCaseFolderById = "";
	
	public String testSetFolderById = "";
	
	public String testResourceFolderById = "";
	
	public String testSetFolderByName = "";
	
	@Autowired
	private TestCaseService testCaseService;
	
	@Autowired
	private ProductBuildDAO productBuildDAO;
	
	@Autowired
	private UserListService userListService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Value("#{ilcmProps['USER_AUTHENTICATION_REQUIRED']}")
    private String atlasUserAuthenticationRequired;
	
	@Override
	@Transactional
	public JTableResponse postWorkpackageDefects(Integer workpackageId, String defectIds, HttpServletRequest request) {
		JTableResponse jTableResponse = null;
		String status = "";
		ConnectorHPQCRest hpqcConnector = null;		
		try {
			log.info("export defects started");
			WorkPackage workPackage = workPackageService.getWorkPackageById(workpackageId);
			ProductMaster productMaster=workPackage.getProductBuild().getProductVersion().getProductMaster();
			String[] dmsIds = defectIds.split(",");
			for (String dms : dmsIds) {
				DefectManagementSystem defectManagementSystem=defectManagementService.getDMSByProductId(productMaster.getProductId(),Integer.parseInt(dms));
				if(defectManagementSystem.getToolIntagration() != null && defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.JIRA_TOOL)){
					JiraConnector jiraConnector = getJIRAConnectorByDefectSystemId(defectManagementSystem.getDefectManagementSystemId(), request);
					status = tafDefectManagementSystemIntegrator.fileDefectsToJira(workpackageId,jiraConnector);		
					jTableResponse = new JTableResponse("Ok", status);					
				}
				else  if(defectManagementSystem.getToolIntagration() != null && defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
					hpqcConnector =  getHPQCDefectManagementSystemConnectorRest(productMaster.getProductId(), request);
					if (hpqcConnector == null) {
						jTableResponse = new JTableResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
						return jTableResponse;
					}
			
					String importCompleteStatus =  tafTestManagementSystemIntegrator.ExportTestExecutionDefectsRest(workPackage, hpqcConnector,defectManagementSystem.getDefectManagementSystemId());
					hpqcConnector.logout(hpqcConnector);
					if(importCompleteStatus != null){
						log.info("Export TestDefects completed.");
						jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
					} else{
						log.info(importCompleteStatus);
						jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
					}
				} else  if(defectManagementSystem.getToolIntagration() != null && defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.TFS_TOOL)){
					TFSIntegrator tfsConnector = getTFSConnector(defectManagementSystem.getConnectionUri().trim());
					TFSConnector.USERNAME=defectManagementSystem.getConnectionUserName().trim();
					TFSConnector.PASSWORD=passwordEncryptionService.decrypt(defectManagementSystem.getConnectionPassword().trim());
					TFSTeamProjectCollection projectCollection = getTFSProjectCollection(tfsConnector, defectManagementSystem.getConnectionUri().trim());
					if (tfsConnector == null) {
						jTableResponse = new JTableResponse("Error", "Unable to establish connection to TFS. Check the mappings");				
					}
					String projectUrl = defectManagementSystem.getConnectionUri().trim();
					String importCompleteStatus =  tafTestManagementSystemIntegrator.exportWorkpackageTestExecutionDefectsRest(workPackage, tfsConnector, projectCollection, defectManagementSystem.getDefectManagementSystemId(), projectUrl);
					projectCollection.close();
					if(importCompleteStatus != null){
						log.info("Export TestDefects completed.");
						jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
					} else{
						log.info(importCompleteStatus);
						jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
					}
				} else{
					jTableResponse = new JTableResponse("Error", "Defect Export feature is not available for this System:"+defectManagementSystem.getToolIntagration().getName());
					return jTableResponse;
				}
			}
		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("ERROR","Error in Export defects");
		}
		return jTableResponse;
	}
	
	public JiraConnector getJIRAConnectorByDefectSystemId(int defectManagementSystemId, HttpServletRequest request){
		JiraConnector jiraConnector = null;
		DefectManagementSystem jiraDefectManagementSystem =null;
		try {
			jiraDefectManagementSystem = defectManagementService.getByDefectManagementSystemId(defectManagementSystemId);
			if(jiraDefectManagementSystem == null){
				log.info("No defect Management system mapped to product. Hence using default properties file for connection.");
				try{
					InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\jira.properties");	
					jiraConnector =  new JiraConnector(fis);					
				} catch(Exception e){
					log.error("Unable to get default JIRA connection properties file.Hence aborting connection",e);
					return null;
				}
			} else {
				//Do Validation check before proceeding further
				String userName = jiraDefectManagementSystem.getConnectionUserName();
				String encyrptedPasssword = jiraDefectManagementSystem.getConnectionPassword();
				String password = passwordEncryptionService.decrypt(encyrptedPasssword);
				String url =jiraDefectManagementSystem.getConnectionUri();
				jiraConnector = new JiraConnector(url, userName, password);
				int statusCode = jiraConnector.isURLValid(url, userName, password);
				
				if(statusCode != 200){
					return null;
				}
		
				Set<DefectManagementSystemMapping> defectManagementMappings = null;
				if(jiraDefectManagementSystem.getDefectManagementSystemMappings() != null){
					defectManagementMappings= jiraDefectManagementSystem.getDefectManagementSystemMappings();
				}
				
				if(defectManagementMappings != null && !defectManagementMappings.isEmpty()){	
					for(DefectManagementSystemMapping dms : defectManagementMappings){
						if(dms != null){
							if(dms.getMappingType() != null && dms.getMappingType().equalsIgnoreCase("PRODUCT") && dms.getMappingValue() != null && !dms.getMappingValue().isEmpty()){
								if(jiraConnector.isProjectExist(dms.getMappingValue()).equalsIgnoreCase(dms.getMappingValue())) {
									log.info("Obtained JIRA connection from mappings. jiraProductName : url : userName : passsword -> " + url + " : " + userName + " : " +  encyrptedPasssword);
									return jiraConnector;
								} else {
									return null;
								}
							}	
						}					
					}			    
				}else{
					log.info("Unable to get default JIRA connection properties file.Hence aborting connection");
					return null;
				}					
			}
		} catch(Exception e){
			log.error("Unable to get JIRA Connection.", e);
			return null;
		}
		return jiraConnector;
	}
	
	/**
     * Defect Management system Connector for HPQC using Rest Services
     * @param productId
     * @param request
     * @return
     */
    public ConnectorHPQCRest getHPQCDefectManagementSystemConnectorRest(int productId, HttpServletRequest request){
    	
		ConnectorHPQCRest hpqcConnectorRest = null;
		DefectManagementSystem hpqcDefectManagementSystem = null;

		try {
			log.info("Getting HPQC connection for product : " + productId);
			String testCaseSource = "HPQC";
			
			ProductMaster product = productListService.getProductById(productId);
			Set<DefectManagementSystem> defectManagementSystems = product.getDefectManagementSystems();
			if(defectManagementSystems == null || defectManagementSystems.isEmpty()){
				log.info("No test Management system mapped to product. Hence using default properties file for connection.");
				try{
					InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\hpqc.properties");
					hpqcConnectorRest =  new ConnectorHPQCRest(fis);
					
				} catch(Exception e){
					log.error("Unable to get default HPQC connection properties file.Hence aborting connection",e);
					return null;
				}
			} else {
				for (DefectManagementSystem defectManagementSystem : defectManagementSystems) {
					log.info("System name:"+defectManagementSystem.getToolIntagration().getName());
					log.info("Tool name:"+IDPAConstants.HPQC_TOOL);
					if (defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
						log.info("inside HPCQ ");
						hpqcDefectManagementSystem = defectManagementSystem;						
					}
				}
				if(hpqcDefectManagementSystem != null){
					Set<DefectManagementSystemMapping> defectManagementMappings = hpqcDefectManagementSystem.getDefectManagementSystemMappings();
					if(defectManagementMappings != null && !defectManagementMappings.isEmpty()){
																	
						String userName = hpqcDefectManagementSystem.getConnectionUserName();
						String encyrptedPasssword = hpqcDefectManagementSystem.getConnectionPassword();
						String passsword = passwordEncryptionService.decrypt(encyrptedPasssword);
						String url =hpqcDefectManagementSystem.getConnectionUri();
						String domainName =hpqcDefectManagementSystem.getConnectionProperty1();
						String hpqcProductName = null;
						for(DefectManagementSystemMapping defectManagementSystemMapping : defectManagementMappings){
							if(defectManagementSystemMapping.getMappingType().equals(TAFConstants.ENTITY_PRODUCT)){
								hpqcProductName = defectManagementSystemMapping.getMappingValue();
							}
						}
						if(hpqcProductName == null || url == null || userName == null || passsword == null){
							log.info("Connection Parameters missing. hpqcProductName : url : userName : passsword -> " + hpqcProductName + " : " + url + " : " + userName + " : " +  encyrptedPasssword);
							return null;
						} else {
							if (domainName == null || domainName.isEmpty())
								domainName = "default";
							hpqcConnectorRest= new ConnectorHPQCRest();						
							hpqcConnectorRest = hpqcConnectorRest.createConnection(url, userName, passsword, domainName, hpqcProductName);
							log.info("Obtained HPQC connection from mappings. hpqcProductName : url : userName : passsword -> " + hpqcProductName + " : " + url + " : " + userName + " : " +  encyrptedPasssword);
							return hpqcConnectorRest;
						}
					}
				} else {
					log.info("No HPQC Defect Management system mapped to product. Hence using default properties file for connection.");
					try{
						InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\hpqc.properties");
						hpqcConnectorRest =  new ConnectorHPQCRest(fis);
						return hpqcConnectorRest;
					} catch(Exception e){
						log.error("Unable to get default HPQC connection properties file.Hence aborting connection",e);
						return null;
					}
				}
			}
			
		} catch(Exception e){
			log.error("Unable to get HPQC Connection.", e);
			return null;
		}
		return hpqcConnectorRest;
    }
    
    public TFSIntegrator getTFSConnector(String tfsCollectionUrl){
		TFSIntegrator tfs = null;
		log.info("Path :"+CommonUtility.getCatalinaPath().concat("\\native"));
		try{
			System.setProperty("com.microsoft.tfs.jni.native.base-directory",servletContext.getRealPath("").concat("\\WEB-INF\\native"));
			tfs = TFSIntegrator.getInstance();						
		}catch(Exception e){
			log.error("Error in getting TFS Connector", e);
		}
		return tfs;
	}
	
	public TFSTeamProjectCollection getTFSProjectCollection(TFSIntegrator tfs, String tfsCollectionUrl){
		TFSTeamProjectCollection projectCollection = null;
		log.info("Path :"+CommonUtility.getCatalinaPath().concat("\\native"));
		log.info(servletContext.getRealPath("").concat("\\WEB-INF\\native\\"));
		try{
			System.setProperty("com.microsoft.tfs.jni.native.base-directory",servletContext.getRealPath("").concat("\\WEB-INF\\native"));
			log.info(System.getProperty("com.microsoft.tfs.jni.native.base-directory"));
			tfs = TFSIntegrator.getInstance();			
			if(tfs!=null){
				TFSConnector.COLLECTION_URL = tfsCollectionUrl;
				projectCollection = TFSConnector.connectToTFS();				
			}
		}catch(Exception e){
			log.error("Error in getting TFS Connector", e);
		}
		return projectCollection;
	}

	@Override
	@Transactional
	public JTableResponse postWorkpackageTestResults(Integer workpackageId,Integer tmsId, HttpServletRequest request) {
		log.debug("test.export");
		JTableResponse jTableResponse = null;
		ConnectorHPQC hpqcConnector = null;
		try {
			log.info("testRunListId Id input:" + workpackageId);
			
			WorkPackage workPackage=workPackageService.getWorkPackageById(workpackageId);
			ProductMaster product = workPackage.getProductBuild().getProductVersion().getProductMaster();
			TestManagementSystem testManagementSystem=null;
			if(tmsId != null)
				testManagementSystem=testManagementService.getByTestManagementSystemId(tmsId);
			if(testManagementSystem.getTestSystemName().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
				hpqcConnector =  getHPQCConnector(product, request);
				if (hpqcConnector == null) {
					jTableResponse = new JTableResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
					return jTableResponse;
				}
				TAFTestManagementSystemIntegrator tafTestManagementSystemIntegrator = TAFTestManagementSystemIntegrationFactory.getTestManagementSystemIntegrator(IDPAConstants.HPQC_TOOL);
				
				
				Set<TestRunJob> testRunJobs = workPackage.getTestRunJobSet();
				String importCompleteStatus = "";
				for (TestRunJob testRunJob : testRunJobs) {
					 importCompleteStatus =  exportTestExecutionResults(testRunJob, hpqcConnector);

				}
				hpqcConnector.disconnect();
				if(importCompleteStatus != null){
					log.info("Export TestResults completed.");
					jTableResponse = new JTableResponse("Ok","Export TestResults completed.");
				} else{
					log.info("Export TestResults completed.");
					jTableResponse = new JTableResponse("Ok","Export TestResults completed");
				}
			}
			
			
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in Export");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	public ConnectorHPQC getHPQCConnector(ProductMaster product, HttpServletRequest request){

		
		ConnectorHPQC hpqcConnector = null;
		TestManagementSystem hpqcTestManagementSystem = null;

		try {
			log.info("Getting HPQC connection for product : " + product.getProductName());
			String testCaseSource = IDPAConstants.HPQC_TOOL;
			
			Set<TestManagementSystem> testManagementSystems = product.getTestManagementSystems();
			if(testManagementSystems == null || testManagementSystems.isEmpty()){
				log.info("No test Management system mapped to product. Hence using default properties file for connection.");
				try{
					InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\hpqc.properties");	
					hpqcConnector =  new ConnectorHPQC(fis);
					
				} catch(Exception e){
					log.error("Unable to get default HPQC connection properties file.Hence aborting connection", e);
					return null;
				}
			} else {
				for (TestManagementSystem testManagementSystem : testManagementSystems) {
					if (testManagementSystem.getTestSystemName().equalsIgnoreCase(TAFConstants.TEST_MANAGEMENT_SYSTEM_HPQC)){
						hpqcTestManagementSystem = testManagementSystem;						
					}
				}
				if(hpqcTestManagementSystem != null){
					Set<TestManagementSystemMapping> testManagementMappings = hpqcTestManagementSystem.getTestManagementSystemMappings();
					if(testManagementMappings != null && !testManagementMappings.isEmpty()){
						
						String userName = hpqcTestManagementSystem.getConnectionUserName();
						String encyrptedPasssword = hpqcTestManagementSystem.getConnectionPassword();
						String passsword = passwordEncryptionService.decrypt(encyrptedPasssword);
						String url =hpqcTestManagementSystem.getConnectionUri();
						String domainName =hpqcTestManagementSystem.getConnectionProperty1();
						String hpqcProductName = null;
						for(TestManagementSystemMapping testManagementSystemMapping : testManagementMappings){
							if(testManagementSystemMapping.getMappingType().equals(TAFConstants.ENTITY_PRODUCT)){
								hpqcProductName = testManagementSystemMapping.getMappingValue();
							}
						}
						if(hpqcProductName == null || url == null || userName == null || passsword == null){
							log.info("Connection Parameters missing. hpqcProductName : url : userName : passsword -> " + hpqcProductName + " : " + url + " : " + userName + " : " + encyrptedPasssword);
							return null;
						} else {
							if (domainName == null || domainName.isEmpty())
								domainName = "default";
													
							hpqcConnector = new ConnectorHPQC(url, userName, passsword, domainName, hpqcProductName);
							log.info("Obtained HPQC connection from mappings. hpqcProductName : url : userName : passsword -> " + hpqcProductName + " : " + url + " : " + userName + " : " +  encyrptedPasssword);
						}
					}else{
						log.info("HPQC connection mapping is not available" );
						return null;
					}
				} else {
					log.info("No HPQC Management system mapped to product. Hence using default properties file for connection.");
					try{
						InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\hpqc.properties");
						hpqcConnector =  new ConnectorHPQC(fis);
						return hpqcConnector;
					} catch(Exception e){
						log.error("Unable to get default HPQC connection properties file.Hence aborting connection",e);
						return null;
					}
				}
			}
			
		} catch(Exception e){
			log.error("Unable to get HPQC Connection.", e);
			return null;
		}
		return hpqcConnector;

	}
	
	public String exportTestExecutionResults(TestRunJob testRunJob, ConnectorHPQC hpqcConnector) {
		
		log.info("Export Test Execution Results");
		
		String status = "";
		TestExecutionResultsExportData testExecutionResultsExportData = new TestExecutionResultsExportData();
		
		boolean isTestCasePassed = true;
		List<TestStepResult> testStepResults = new ArrayList<TestStepResult>();
		List<TestCaseResult> testCaseResults =  new ArrayList<TestCaseResult>(); 
		TestSetResult testSetResult = null;
		Set<TestSuiteList> testSuiteLists = testRunJob.getTestSuiteSet();
		
		for (TestSuiteList tsl : testSuiteLists) {
			
			testSetResult = new TestSetResult();
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans=workPackageService.getWorkPackageTestCaseExecutionPlanByTestRunJob(testRunJob,tsl.getTestSuiteId());
		
		testSetResult.setTestsetID(tsl.getTestSuiteCode());
		TestCaseExecutionResult testCaseExecutionResult=null;
		try{
			TestCaseResult testCaseResult =null;
			
			for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
				testCaseExecutionResult=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
				testCaseResult = new TestCaseResult();
				testStepResults = new ArrayList<TestStepResult>();
				Set<TestStepExecutionResult> testStepExecutionResults=testCaseExecutionResult.getTestStepExecutionResultSet();
				TestStepResult testStepResult = null;
				String actualResult="";
				Time execTime = null;
				if(testCaseExecutionResult.getExecutionTime()!=null){
				 execTime = new Time(testCaseExecutionResult.getExecutionTime());
				}
				else{
					
					
					execTime = new Time(new Date(System.currentTimeMillis()).getTime());
				}
				Date execDate = new Date(new Date(System.currentTimeMillis()).getTime());
				for (TestStepExecutionResult testStepExecutionResult : testStepExecutionResults) {
					log.info("tesstep"+testStepExecutionResult.getTestSteps().getTestStepCode());
					testStepResult = new TestStepResult();
					if(actualResult.equals("")){
						actualResult =testStepExecutionResult.getObservedOutput();
					}else{
						actualResult =actualResult+","+testStepExecutionResult.getObservedOutput();
					}
					
					if(testCaseExecutionResult.getFailureReason()!= null && !testCaseExecutionResult.getFailureReason().isEmpty()){
						actualResult = actualResult + "\n FailureReason : "+ testCaseExecutionResult.getFailureReason();
					}
					
					if(testCaseExecutionResult.getComments()!= null && !testCaseExecutionResult.getComments().isEmpty()){
						actualResult = actualResult + "\n Execution Remarks : "+ testCaseExecutionResult.getComments();
					}
					
					//Adding to include the defect code into the result for traceability
					Set<TestExecutionResultBugList> bugs = testCaseExecutionResult.getTestExecutionResultBugListSet();
					if (bugs == null || bugs.isEmpty()) {
					} else {
						log.info("Bugs available for this result");
						for (TestExecutionResultBugList bug : bugs) {
							if (bug.getBugFilingStatus().equals("FILED")) {
								actualResult = actualResult + "\n Defect Trace : "+ bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId();
								log.info("Bug trace added to result for HPQC : " + bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId());
							}
							if(bug!= null){
								hpqcConnector.createBug(bug.getBugTitle(),new Time(System.currentTimeMillis()), SEVERITY.MEDIUM,false);
							}
						}
					}
					testStepResult.setActaulResult(actualResult);		
					testStepResult.setStepId(testStepExecutionResult.getTestSteps().getTestStepCode());
		
					if(testStepExecutionResult.getResult()!= null){
						if((testStepExecutionResult.getResult()).equalsIgnoreCase("PASS") || (testStepExecutionResult.getResult()).equalsIgnoreCase("PASSED")){
							testStepResult.setStatus(STATUS.PASSED);
						}
						if((testStepExecutionResult.getResult()).equalsIgnoreCase("FAIL") || (testStepExecutionResult.getResult()).equalsIgnoreCase("FAILED")){
							testStepResult.setStatus(STATUS.FAILED);
							isTestCasePassed = false;
						}
					}
					
					testStepResult.setExecutionTime(execTime);
					testStepResult.setExecutionDate(execDate);
					testStepResults.add(testStepResult);
				}
				testCaseResult.setExecutionDate(execDate);
				testCaseResult.setExecutionTime(execTime);	
				testCaseResult.setTestcaseID(workPackageTestCaseExecutionPlan.getTestCase().getTestCaseCode());
				
				testCaseResult.setSteps(testStepResults);
			
				if(isTestCasePassed){
					testCaseResult.setStatus(STATUS.PASSED);
				} else {
					testCaseResult.setStatus(STATUS.FAILED);
				}
				testCaseResults.add(testCaseResult);
			}
		}
		 catch(Exception e){
				log.error("Could not export test execution results due to problem in preparing data for export", e);
				return "Could not export test execution results due to problem in preparing data for export";
		}
		
		
		try{
			if(testCaseResults!= null && !testCaseResults.isEmpty() ){
				log.info("Updating TestSet Results"+testCaseResults.size());

				testSetResult.setTestCases(testCaseResults);
				
				log.info(testSetResult.getTestsetID());
				for (TestCaseResult tcr : testSetResult.getTestCases()) {
					log.info(">>>>"+tcr.getTestcaseID());
					log.info(">>>>>"+tcr.getSteps().size());
				}
				List<TestResult> testResults = hpqcConnector.updateTestSetResult(testSetResult);
				if(testResults!= null && !testResults.isEmpty()){
					log.info("TestResults Updated in Test Management System"+testResults.size());
					for(TestResult testResult :testResults){		
						testResult.getStepId();
						testResult.getTestCaseId();
						testResult.getTestSetId();
						testExecutionResultsExportData.setResultCode(testResult.getRunId());
						testExecutionService.addTestExecutionResultsExportData(testExecutionResultsExportData);
						log.info("TestResult Run id : " + testResult.getRunId() + "Run Name : " + testResult.getRunName());
					}
				}
			}	
		} catch(Exception e){
			e.printStackTrace();
			log.error("Problem in connecting to HP Test Management system ", e);
			return "Problem in connecting to HP Test Management system ";		
		}	
		}
		return status;
	}

	@Override
	@Transactional
	public List<JsonTestCaseList> getISERecommendedTestcases(TestRunPlan testPlan) {
		List<JsonTestCaseList> jsonTestCaseList = new LinkedList<JsonTestCaseList>();
		List<TestCaseList> testCaseList = null;
		List<TestCaseList> testCaseListTotal = new LinkedList<TestCaseList>();
		List<ISERecommandedTestcases> recommendedTestCases = new LinkedList<ISERecommandedTestcases>();
		Set<ISERecommandedTestcases> recommendedTestCasesSet = new HashSet<ISERecommandedTestcases>();
		Map<String, Integer> iseRecommendedTC = new LinkedHashMap<String, Integer>();
		Integer totalTestRunPlanTestCasesCount = 0;
		Integer recommendedTestCasesCount = 0;			
		Double probCount = 0.00;
		Integer testRunPlanId = 0;
		Integer totalTestConfigurationTestCasesCount = 0;
		Set<RunConfiguration> testConfigurationSet = new LinkedHashSet<RunConfiguration>();
		List<TestSuiteList> testConfigurationTestSuiteSet = new LinkedList<TestSuiteList>();
		Set<TestCaseList> testConfigurationTestSuiteTestCaseSet = new HashSet<TestCaseList>();
		try {
			TestRunPlan trplan = productListService.getTestRunPlanById(testPlan.getTestRunPlanId());
			//Obtain list of Test Configuration mapped for the Test Plan
			if(trplan.getRunConfigurationList() != null && !trplan.getRunConfigurationList().isEmpty()){
				testConfigurationSet = trplan.getRunConfigurationList(); 
			}
			//Iterate each Test Configuration to obtain list of test suites associated
			for(RunConfiguration testConfig : testConfigurationSet){
				if(testConfig != null && testConfig.getTestSuiteLists() != null && ! testConfig.getTestSuiteLists().isEmpty()){
					testConfigurationTestSuiteSet.addAll(testConfig.getTestSuiteLists());
					for(TestSuiteList testConfigTestSuite : testConfig.getTestSuiteLists()) {
						if(testConfigTestSuite != null && testConfigTestSuite.getTestCaseLists() != null && !testConfigTestSuite.getTestCaseLists().isEmpty()){
							testConfigurationTestSuiteTestCaseSet.addAll(testConfigTestSuite.getTestCaseLists());							
						}						
					}	
				}				
			}
			
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
					Integer testSuiteId = testSuite.getTestSuiteId();
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
		} catch(Exception e) {
			log.error("Error while get ISE Recommanded response ",e);
			return jsonTestCaseList;
		}
		return jsonTestCaseList;
	}

	@Override
	@Transactional
	public String listTestBeds(Integer testPlanId) {
		String finalResult="";
		try {
			TestRunPlan testRunPlan=null;
			testRunPlan = productListService.getTestRunPlanBytestRunPlanId(testPlanId);			
			//Get Recommended Test Cases
			List<JsonTestCaseList> jsonIseRecommendationTestcases = new ArrayList<JsonTestCaseList>();
			jsonIseRecommendationTestcases = getISERecommendedTestcases(testRunPlan);
			Map<String, String> recomendedTcNamesMap = new HashMap<String, String>();
			for(JsonTestCaseList jstcl : jsonIseRecommendationTestcases) {
				if(jstcl.getIseRecommended() != null && jstcl.getIseRecommended().equalsIgnoreCase("Yes"))
					recomendedTcNamesMap.put(jstcl.getTestCaseName(), jstcl.getRecommendedCategory());
			}
			log.info("ISE Recommended Testcases : "+jsonIseRecommendationTestcases);
			//Initialization parameters
			List<JsonTestRunPlan> jsonTestRunPlanList=new ArrayList<JsonTestRunPlan>();
			JSONArray list = new JSONArray();
			
			JSONObject finalObj = new JSONObject();
			JSONObject tsTitle = new JSONObject();
			JSONObject tcTitle = new JSONObject();
			JSONObject rcTitle = new JSONObject();
			JSONObject iseRecommendedTitle = new JSONObject();
			JSONObject iseRcRecommendedTitle = new JSONObject();
			
			JSONObject tsData = new JSONObject();
			JSONObject tcData = new JSONObject();
			JSONObject rcData = new JSONObject();
			
			JSONArray columnData = new JSONArray();
			JSONArray allcolumnsData = new JSONArray();
			JsonTestRunPlan jsonTestRunPlan = null;
			
			List<Integer> runConfigIds = new LinkedList<Integer>();
			
			//Logic started for Test Configuration / Test Plan - Test Suite levels			
			if(testRunPlan != null ){	
				
				String testSuiteName="";
				String testCaseName="";
				String runConfigMapping="";
				
				tsTitle.put("title", "TestSuite");					
				list.add(tsTitle);
				
				tcTitle.put("title", "TestCase");					
				list.add(tcTitle);
				
				iseRecommendedTitle.put("title", "ISE Recommended");					
				list.add(iseRecommendedTitle);

				Set<RunConfiguration> runConfigurations = testRunPlan.getRunConfigurationList();
				MultiMap testSuiteRunConfigurationMap = new MultiHashMap();
				for(RunConfiguration rc : runConfigurations){
					rcTitle= new JSONObject();
					rcTitle.put("title", rc.getRunconfigName());
					rcTitle.put("id", String.valueOf(rc.getRunconfigId()));
					list.add(rcTitle);
					
					//iseRcRecommendedTitle= new JSONObject();
					//iseRcRecommendedTitle.put("title", "ISE-"+ rc.getRunconfigId());
					//list.add(iseRcRecommendedTitle);
					
					runConfigIds.add(rc.getRunconfigId());
				}				
				finalObj.put("COLUMNS", list);
				
				Set<TestSuiteList> testSuiteLists = new HashSet<TestSuiteList>();
				Set<TestCaseList> testCaseLists = new HashSet<TestCaseList>();	
				
				for(RunConfiguration rc : runConfigurations){
					testSuiteLists.addAll(rc.getTestSuiteLists());
					for(TestSuiteList tsl : testSuiteLists){
						for (TestCaseList tc : productListService.getRunConfigTestSuiteTestCaseMapped(rc.getRunconfigId(), tsl.getTestSuiteId())){
							testSuiteRunConfigurationMap.put(tc.getTestCaseId(),rc.getRunconfigId());
						}
					}
				}
				
				for(TestSuiteList tsl : testSuiteLists){
					testSuiteName = tsl.getTestSuiteName();
					for(RunConfiguration rc : testRunPlan.getRunConfigurationList()){
						if(testRunPlan.getUseIntelligentTestPlan() != null && !testRunPlan.getUseIntelligentTestPlan().equalsIgnoreCase("Yes"))
							testCaseLists.addAll(productListService.getRunConfigTestSuiteTestCaseMapped(rc.getRunconfigId(), tsl.getTestSuiteId()));
						else
							testCaseLists.addAll(tsl.getTestCaseLists());
					}
					for (TestCaseList testCaseList : testCaseLists) {
						boolean isRecommended = false;
						testCaseName = testCaseList.getTestCaseName();						
						columnData = new JSONArray();
						columnData.add(testSuiteName+"["+tsl.getTestSuiteId()+"]");
						columnData.add(testCaseName+"["+testCaseList.getTestCaseId()+"]");
						if(recomendedTcNamesMap != null && !recomendedTcNamesMap.isEmpty() && recomendedTcNamesMap.containsKey(testCaseName)){							
							isRecommended = true;	
						} else {
							isRecommended = false;
						}
						log.info("Is Recommended TC : "+testCaseName+"  is recommended : "+isRecommended+" ; TCNames : "+recomendedTcNamesMap.keySet());
						if(isRecommended)
							columnData.add("Yes" + " [ " + recomendedTcNamesMap.get(testCaseName)+" ]");						
						else
							columnData.add("No");
						
						for(RunConfiguration rc : runConfigurations){							
							List<String> s = (List<String>) testSuiteRunConfigurationMap.get(testCaseList.getTestCaseId());				
							if(s != null && !s.isEmpty() && s.contains(rc.getRunconfigId())){
								//runConfigMapping = rc.getRunconfigName()+"["+rc.getRunconfigId()+"]";																
								runConfigMapping = "Yes";																
							} else {
								runConfigMapping = "NA";
							}
							//columnData.add(isRecommended);
							columnData.add(runConfigMapping);
						}
						allcolumnsData.add(columnData);
					}
					testCaseLists = null;
					testCaseLists = new HashSet<TestCaseList>();
				}
				
				finalObj.put("DATA", allcolumnsData);
				jsonTestRunPlan=new JsonTestRunPlan(testRunPlan,"TestBed");
				jsonTestRunPlanList.add(jsonTestRunPlan);	
			}			
			finalResult=finalObj.toString();
			jsonTestRunPlan = null;
		    return "["+finalResult+"]";								
		} catch (Exception e) {	         
			log.error("JSON ERROR listing TestBeds", e);
	    }		        
	    return "["+finalResult+"]";
	}
	
	
	@Override
	@Transactional
	public List<JsonWorkPackageTestCaseExecutionSummary> listTestJobsWorkpackageSummary(Integer workPackageId , Integer productBuildId) {
		List<WorkPackageBuildTestCaseSummaryDTO> wpBuildTCSummaryDTOList = new ArrayList<WorkPackageBuildTestCaseSummaryDTO>();
		List<JsonWorkPackageTestCaseExecutionSummary> jsonWorkPackageTestCaseExecutionSummaryList = new ArrayList<JsonWorkPackageTestCaseExecutionSummary>();
		try {			
			wpBuildTCSummaryDTOList=workPackageService.listWorkPackageTestCaseExecutionBuildSummary(workPackageId,productBuildId);		
			if (wpBuildTCSummaryDTOList == null || wpBuildTCSummaryDTOList.isEmpty()) {
				return null;
			} else {
				for (WorkPackageBuildTestCaseSummaryDTO workPackageBuildTestCaseSummaryDTO : wpBuildTCSummaryDTOList) {
					TestRunJob trj = workPackageService.getTestRunJobById(workPackageBuildTestCaseSummaryDTO.getTestRunJobId());
					JsonWorkPackageTestCaseExecutionSummary jstces = new JsonWorkPackageTestCaseExecutionSummary(workPackageBuildTestCaseSummaryDTO);
					if(trj != null && trj.getRunConfiguration() != null && trj.getRunConfiguration().getRunconfigName() != null) {
						jstces.setEnvironmentCombination(trj.getRunConfiguration().getRunconfigName());
					}
					if(trj.getTotalTestCasesCount() != null) {
						jstces.setTestCasesCount(workPackageService.getExecutionTCCountForJob(trj.getTestRunJobId()));
						//jstces.setTotalTestCaseCount(workPackageService.getExecutionTCCountForJob(trj.getTestRunJobId())); //Both should be same ... Verify
					}
					
					//To set not Executed count.
					Integer notExecutedCount;
					if(jstces.getTestCasesCount() != null && jstces.getTestCasesCount() > 0){
						if (jstces.getTestCasesCount() > 0) {
							notExecutedCount = jstces.getTestCasesCount() - (jstces.getPassedCount() + jstces.getFailedCount());
							jstces.setNotexecutedCount(notExecutedCount);
						} else {
							jstces.setNotexecutedCount(0);
						}
					}
					
					
					if( trj.getWorkPackage() != null && trj.getWorkPackage().getProductBuild() != null) {
						jstces.setpBuildId(trj.getWorkPackage().getProductBuild().getProductBuildId());
						jstces.setpBuildName(trj.getWorkPackage().getProductBuild().getBuildname());
						if(trj.getWorkPackage().getProductBuild().getProductVersion() != null && trj.getWorkPackage().getProductBuild().getProductVersion().getProductMaster() != null) {
							jstces.setProductId(trj.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId());
							jstces.setProductName(trj.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName());
						}
					}
					if(trj.getWorkPackage() != null ) {
						jstces.setPlannedStartDate(DateUtility.dateToStringInSecond(trj.getWorkPackage().getPlannedStartDate()));
						jstces.setPlannedEndDate(DateUtility.dateToStringInSecond(trj.getWorkPackage().getPlannedEndDate()));
					}
					
					jsonWorkPackageTestCaseExecutionSummaryList.add(jstces);
				}			
				wpBuildTCSummaryDTOList = null;
				return jsonWorkPackageTestCaseExecutionSummaryList;
			}
		} catch (Exception e) {
			log.error("JSON ERROR", e);
			return null;
		}
	}

	@Override
	@Transactional
	public org.json.JSONObject executeSelectiveTestCasesTestPlan(String nodeRedTafJSONQuery) {
		Integer testPlanId = null;
		//Integer productId = null;
		String deviceNames = "";		
		String testcaseIds = "";
		JSONObject workpackageJsonObj = new JSONObject();
		org.json.JSONObject responseJson = new org.json.JSONObject();
		String testcaseNames = "";
		Map<RunConfiguration, String> runConfigTestcaseNames = new HashMap<RunConfiguration, String>();
		List<org.json.simple.JSONObject> testConfigs = new ArrayList<org.json.simple.JSONObject>();
		Set<RunConfiguration> runConfigurations = new HashSet<RunConfiguration>();
		VerificationResult deviceReadinessCheck = null;
		TestRunPlan testPlan = null;
		String message = "";
		String jobIds = null;
		boolean isValid = true;
		try {			
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			org.json.JSONObject authResponse = authenticateUser(userName, "/testExecution/executetestplan");
			if (authResponse != null)
				return authResponse;
			if(jsonFormatObject != null) {	
				if((String) jsonFormatObject.get("testPlanId") != null)
					testPlanId = Integer.valueOf((String) jsonFormatObject.get("testPlanId"));
				/*if((String) jsonFormatObject.get("productId") != null)
					productId = Integer.valueOf((String) jsonFormatObject.get("productId"));*/
						
				if(jsonFormatObject.get("testConfigurations") != null){
					JSONArray jsonArray = (JSONArray) jsonFormatObject.get("testConfigurations");
					for (int i = 0; i < jsonArray.size(); i++) {
					    JSONObject explrObject = (JSONObject) jsonArray.get(i);
					    testConfigs.add(explrObject);
					}
				}
			}
			
			
			log.info("Test Plan Id : " +testPlanId);		
			testPlan = productListService.getTestRunPlanById(testPlanId);
			if (testPlan == null) {
				responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("data", "");
				responseJson.put("message", "Testplan ID is invalid");
				responseJson.put("Failure_Details", "Testplan ID is invalid");
				return responseJson;
			}
			
			ProductMaster product= testPlan.getProductVersionListMaster().getProductMaster();
			//Check authorization for user
			authResponse = checkUserAuthorization(product.getProductId(), userName, "/testExecution/executeSelectiveTestCasesTestPlan");
			if (authResponse != null)
				return authResponse;
			
			List<RunConfiguration> runConfigurationsList = productListService.listRunConfigurationByTestRunPlanId(testPlanId,0,10);	
			
			int isValidTestConfigurationCounter = 0;
			
			if(testConfigs != null && !testConfigs.isEmpty()){				
				for(org.json.simple.JSONObject obj : testConfigs){
					testcaseNames = "";
					RunConfiguration testConfiguration = productListService.getRunConfigurationByIdWithoutInitialization(Integer.valueOf((String)obj.get("testConfigurationId")));
					if(obj.get("testcaseIds") != null){
						testcaseIds = (String) obj.get("testcaseIds");
						if(testcaseIds != null && !testcaseIds.trim().isEmpty()) {
							for(String tcId : testcaseIds.split(",")){
								log.info("Test Case Id : "+tcId);
								TestCaseList tc = testCaseService.getByTestCaseIdwithoutInitialization(Integer.valueOf(tcId));
								testcaseNames += tc.getTestCaseName()+",";
							}
						} else {
							message += "There are no valid testcases available, so unable to trigger job for "+testConfiguration.getRunconfigName()+". ";
						}
					} else if((String) obj.get("testcaseNames") != null) { 
						testcaseNames = (String) obj.get("testcaseNames");
					}
					
					if((String) obj.get("device") != null) {
						deviceNames = (String) obj.get("device");
					}
					log.info("Test Configuration validation starts");											
					if(testConfiguration != null){						
						deviceReadinessCheck = productListService.testConfigurationReadinessCheck(testConfiguration, testPlan, null);
						if(runConfigurationsList.contains(testConfiguration) && deviceReadinessCheck.getIsReady() != null && deviceReadinessCheck.getIsReady().equalsIgnoreCase("yes")){
							if(testcaseNames != null && !testcaseNames.trim().isEmpty()) {
								runConfigurations.add(testConfiguration);
								runConfigTestcaseNames.put(productListService.getRunConfigurationByIdWithoutInitialization(Integer.valueOf((String)obj.get("testConfigurationId"))), testcaseNames);
								isValidTestConfigurationCounter++;
							}
						} else if(deviceReadinessCheck.getIsReady() != null && deviceReadinessCheck.getIsReady().equalsIgnoreCase("no")){
							message += "Test Configuration : "+testConfiguration.getRunconfigName()+" & Test Configuration ID : "+Integer.valueOf((String)obj.get("testConfigurationId"))+" is not active so unable to create job. ";
							responseJson.put("message", message);
							responseJson.put("Failure_Details", message);
							isValid = false;
						}
					}else{
						message += "Test Configuration ID : "+Integer.valueOf((String)obj.get("testConfigurationId"))+" is invalid. ";	
						isValid = false;
					}	
					//Device Validation starts
					String[] deviceArr = deviceNames.split(",");
					for(String device : deviceArr){
						if(testConfiguration.getRunconfigName() != null && !testConfiguration.getRunconfigName().contains(device)){
							message = "No Active Jobs created due to invalid device(s).No Execution Initiated.";
							responseJson.put("result", "ERROR");
							responseJson.put("status", "400");
							responseJson.put("data", "No Active Jobs created due to invalid device(s).No Execution Initiated.");
							responseJson.put("message", message);
							responseJson.put("Failure_Details", message);
							return responseJson;
						}
					}
				}
			}			
			log.info("Test Configuration validation ends");		
			if(runConfigTestcaseNames == null || runConfigTestcaseNames.isEmpty()) {
				message = "No Active Jobs created due to invalid Test Configuration(s).No Execution Initiated.";
				//workpackageJsonObj.put("Result", "FAILED");
				responseJson.put("result", "ERROR");
				responseJson.put("status", "400");
				responseJson.put("data", "No Active Jobs created due to invalid Test Configuration(s).No Execution Initiated.");
				responseJson.put("message", message);
				responseJson.put("Failure_Details", message);
				return responseJson;
			}				
			//Device Validation ends
			if(isValidTestConfigurationCounter > 0){
				log.info("All validation Completed..Going to start workpackage creation");	
				//TestRunPlan testRunPlan = productListService.getTestRunPlanBytestRunPlanId(testPlanId);			
				WorkPackage newWorkpackage=new WorkPackage();
				String name=testPlan.getProductVersionListMaster().getProductMaster().getProductName()+"-"+testPlan.getProductVersionListMaster().getProductVersionName()+"-"+DateUtility.getCurrentTime();
				
				newWorkpackage.setName(name);
				newWorkpackage.setDescription(name +" created.");
				
				newWorkpackage.setCreateDate(DateUtility.getCurrentTime());
				newWorkpackage.setModifiedDate(DateUtility.getCurrentTime());
				newWorkpackage.setStatus(1);
				newWorkpackage.setIsActive(1);
				
				if(testPlan.getExecutionType().getExecutionTypeId()==3) {
					ExecutionTypeMaster executionTypeMaster=executionTypeMasterService.getExecutionTypeByExecutionTypeId(7);
					newWorkpackage.setWorkPackageType(executionTypeMaster);
				} else {
					ExecutionTypeMaster executionTypeMaster=executionTypeMasterService.getExecutionTypeByExecutionTypeId(8);
					newWorkpackage.setWorkPackageType(executionTypeMaster);
				}
				log.info("Intermediate log while workapackage creation");
				ProductBuild productBuild = productBuildDAO.getLatestProductBuild(testPlan.getProductVersionListMaster().getProductMaster().getProductId());
				newWorkpackage.setProductBuild(productBuild);
				
				newWorkpackage.setTestRunPlan(testPlan);
				newWorkpackage.setTestRunPlanGroup(null);
				newWorkpackage.setSourceType(IDPAConstants.WORKPACKAGE_SOURCE_TESTRUNPLAN_CI_REST);
				
				WorkFlowEvent workFlowEvent = new WorkFlowEvent();
				workFlowEvent.setEventDate(DateUtility.getCurrentTime());
				workFlowEvent.setRemarks("New Workapckage Added :"+newWorkpackage.getName());
				workFlowEvent.setUser(null);
				workFlowEvent.setWorkFlow(workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_NEW));
				
				workPackageService.addWorkFlowEvent(workFlowEvent);
				newWorkpackage.setWorkFlowEvent(workFlowEvent);
				newWorkpackage.setPlannedEndDate(DateUtility.getCurrentTime());
				newWorkpackage.setPlannedStartDate(DateUtility.getCurrentTime());
				UserList userList= userListService.getUserByLoginId(IDPAConstants.USER_FOR_TESTRUN_PLAN);
				newWorkpackage.setUserList(userList);
				newWorkpackage.setIterationNumber(-1);
				LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
				lifeCyclePhase.setLifeCyclePhaseId(4);
				newWorkpackage.setLifeCyclePhase(lifeCyclePhase);
				newWorkpackage.setWorkPackageId(0);
				newWorkpackage.setTestExecutionMode(testPlan.getAutomationMode());
				if(productBuild!=null){
					workPackageService.addWorkPackage(newWorkpackage);	
					log.info("WorkPackage created successfully");
					workPackageService.workpackageSelectiveExecutionPlan(newWorkpackage,testPlan,runConfigTestcaseNames,request,deviceNames);
				}else{
					log.info("Unable to execute Test Run Plan. This could be because the Product Build specified is not active");
				}
				
				workpackageJsonObj.put("TP Execution Initiated", "Yes");
				workpackageJsonObj.put("Workpackage ID", newWorkpackage.getWorkPackageId());
				workpackageJsonObj.put("Workpackage Name", newWorkpackage.getName());
				workpackageJsonObj.put("Test Plan Id", testPlan.getTestRunPlanId());
				workpackageJsonObj.put("Test Plan Name", testPlan.getTestRunPlanName());
				if(ScriptLessExecutionDTO.getJobIDs() != null && !ScriptLessExecutionDTO.getJobIDs().isEmpty())
					workpackageJsonObj.put("Total Jobs", ScriptLessExecutionDTO.getJobIDs().split(",").length);
				else
					workpackageJsonObj.put("Total Jobs", 0);
				workpackageJsonObj.put("Job Nos", ScriptLessExecutionDTO.getJobIDs());
				WorkPackage workPackage = workPackageService.getWorkPackageById(newWorkpackage.getWorkPackageId());
				Set<TestRunJob> jobs = workPackage.getTestRunJobSet();
				JSONObject jobJsonObject = null;
				org.json.JSONArray jobJsonObjectsArray = new org.json.JSONArray();
				for (TestRunJob job : jobs) {
					jobJsonObject = new JSONObject();
					jobJsonObject.put("Job ID", job.getTestRunJobId());
					jobJsonObject.put("Environment", job.getEnvironmentCombination().getEnvironmentCombinationName());
					jobJsonObject.put("Test System", job.getHostList().getHostName());
					jobJsonObject.put("Test System IP", job.getHostList().getHostIpAddress());
					if (!(job.getGenericDevices() == null)) {
						jobJsonObject.put("Test Device", job.getGenericDevices().getName());
						jobJsonObject.put("Type", job.getGenericDevices().getDeviceModelMaster().getDeviceType().getDeviceTypeName());
						jobJsonObject.put("Model", job.getGenericDevices().getDeviceModelMaster().getDeviceModel() + " : " + job.getGenericDevices().getDeviceModelMaster().getDeviceName());
						jobJsonObject.put("Resolution", job.getGenericDevices().getDeviceModelMaster().getDeviceResolution());
					} else {
						jobJsonObject.put("Test Device", "NA");
						jobJsonObject.put("Type", "NA");
						jobJsonObject.put("Model", "NA");
						jobJsonObject.put("Resolution", "NA");
					}
					jobJsonObject.put("Total testcases", job.getPlannedTestCasesCount());
					jobJsonObject.put("Average execution time", "TBD");
					jobJsonObject.put("Execution expected to be completed by", "TBD");
					jobJsonObject.put("Status", "TBD");
					jobJsonObject.put("Result", "TBD");
					jobJsonObject.put("Execution_Mode",  testPlan.getAutomationMode());
					jobJsonObjectsArray.put(jobJsonObject);
				}
				workpackageJsonObj.put("Jobs", jobJsonObjectsArray);

				//workpackageJsonObj.put("Result", "PASSED");
				
				if(workpackageJsonObj.get("Job Nos") != null){
					jobIds = (String) workpackageJsonObj.get("Job Nos");
				}
				log.info("Job Nos : "+jobIds);
				String wpMessage = "";
				//Added for selective changes
				if(jobIds != null && !jobIds.trim().isEmpty()){
					wpMessage +=  testPlan.getTestRunPlanName() + " execution initiated with Workpackage [" + newWorkpackage.getWorkPackageId() + "] " + newWorkpackage.getName() + System.lineSeparator() + System.lineSeparator() + "Jobs " + jobIds.substring(0, jobIds.length()-1) + " queued for execution.";
				} else {
					wpMessage += testPlan.getTestRunPlanName() + " execution not initiated as there are no valid Test Jobs";
					isValid = false;
				}
				
				if(!isValid) {
					responseJson.put("result", "ERROR");
				} else {
					responseJson.put("result", "OK");					
				}
				//If any one test configuration is not valid in multiple test configuration scenario, then both the error and success message has to be displayed.
				message = wpMessage + System.lineSeparator() + message;
				//responseJson.put("wpMessage", wpMessage);
				responseJson.put("status", "200");
				responseJson.put("data", workpackageJsonObj.toString());
				responseJson.put("message", message);
				//responseJson.put("Failure_Details", message);
				log.info("responseJson : "+responseJson);
			} else {
				message += "No Active Jobs created due to invalid Test Configuration or Test Plan input.No Execution Initiated.";
				//workpackageJsonObj.put("Result", "FAILED");
				responseJson.put("result", "ERROR");
				responseJson.put("status", "400");
				responseJson.put("data", "No Active Jobs created due to invalid Test Configuration or Test Plan input.No Execution Initiated.");
				responseJson.put("message", message);
				responseJson.put("Failure_Details", message);
			}
		} catch(Exception e){
			try {
				//workpackageJsonObj.put("Result", "FAILED");
				responseJson.put("message", message + System.lineSeparator() + "Test Plan execution not initiated due to some issue." + System.lineSeparator() + e.getStackTrace());
				responseJson.put("Failure_Details", e.getStackTrace());
				responseJson.put("result", "ERROR");
				responseJson.put("status", "400");
				responseJson.put("data", workpackageJsonObj.toString());
			} catch (Exception e1) {
				log.error("Problem while executing Test Plan through REST call ", e1);
			}
			log.error("Problem while executing Test Plan through REST call ", e);
		}
		return responseJson;
	}
	
private org.json.JSONObject checkUserAuthorization(Integer productId, String userName, String serviceName) {
		
		boolean isProductAuthorizedForUser=false;
		JSONObject responseJson = new JSONObject();
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
	
	private org.json.JSONObject authenticateUser(String userName, String serviceName) {
		
		JSONObject responseJson = new JSONObject();
		try {
			UserList userList = null;
			if (atlasUserAuthenticationRequired != null && atlasUserAuthenticationRequired.equalsIgnoreCase("yes")) {
				if(userName == null || userName.trim().isEmpty()) {
					log.info("REST service : " + serviceName + " : Registered user name is missing. It is mandatory");
					return prepareErrorResponseWithoutData("User is required", "Registered user name is missing. It is mandatory");
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
	
private org.json.JSONObject prepareErrorResponseWithoutData(String message, String failureDetails) {
		
	org.json.JSONObject responseJson = new org.json.JSONObject();
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

@Override
@Transactional
public List<JsonTestCaseList> getISERecommendedTestcases(TestRunPlan testPlan, Integer productBuildId) {
	List<JsonTestCaseList> jsonTestCaseList = new LinkedList<JsonTestCaseList>();
	List<TestCaseList> testCaseList = null;
	List<TestCaseList> testCaseListTotal = new LinkedList<TestCaseList>();
	List<ISERecommandedTestcases> recommendedTestCases = new LinkedList<ISERecommandedTestcases>();
	Set<ISERecommandedTestcases> recommendedTestCasesSet = new HashSet<ISERecommandedTestcases>();
	Map<String, Integer> iseRecommendedTC = new LinkedHashMap<String, Integer>();
	Integer totalTestRunPlanTestCasesCount = 0;
	Integer recommendedTestCasesCount = 0;			
	Double probCount = 0.00;
	Integer testRunPlanId = 0;
	Integer totalTestConfigurationTestCasesCount = 0;
	Set<RunConfiguration> testConfigurationSet = new LinkedHashSet<RunConfiguration>();
	List<TestSuiteList> testConfigurationTestSuiteSet = new LinkedList<TestSuiteList>();
	Set<TestCaseList> testConfigurationTestSuiteTestCaseSet = new HashSet<TestCaseList>();
	try {
		TestRunPlan trplan = productListService.getTestRunPlanById(testPlan.getTestRunPlanId());
		//Obtain list of Test Configuration mapped for the Test Plan
		if(trplan.getRunConfigurationList() != null && !trplan.getRunConfigurationList().isEmpty()){
			testConfigurationSet = trplan.getRunConfigurationList(); 
		}
		//Iterate each Test Configuration to obtain list of test suites associated
		for(RunConfiguration testConfig : testConfigurationSet){
			if(testConfig != null && testConfig.getTestSuiteLists() != null && ! testConfig.getTestSuiteLists().isEmpty()){
				testConfigurationTestSuiteSet.addAll(testConfig.getTestSuiteLists());
				for(TestSuiteList testConfigTestSuite : testConfig.getTestSuiteLists()) {
					if(testConfigTestSuite != null && testConfigTestSuite.getTestCaseLists() != null && !testConfigTestSuite.getTestCaseLists().isEmpty()){
						testConfigurationTestSuiteTestCaseSet.addAll(testConfigTestSuite.getTestCaseLists());							
					}						
				}	
			}				
		}
		
		//Get total number of test configuration test cases count
		if(testConfigurationTestSuiteTestCaseSet != null && !testConfigurationTestSuiteTestCaseSet.isEmpty())
			totalTestConfigurationTestCasesCount = testConfigurationTestSuiteTestCaseSet.size();
		
		log.info("Is Test Configuration for Recommended Process : "+trplan.getUseIntelligentTestPlan());
		
		//For ISE recommended Test case obtain from ISE API
		if (trplan.getUseIntelligentTestPlan() != null && trplan.getUseIntelligentTestPlan().equalsIgnoreCase("Yes")) {
			log.info("Getting Test Plan for ISE");							
			//Make a call to ISE API to get the recommended test plan for this build
			try {
				recommendedTestCases = workPackageService.getIntelligentTestPlanFromISE(trplan, productBuildId);
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
				Integer testSuiteId = testSuite.getTestSuiteId();
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
	} catch(Exception e) {
		log.error("Error while get ISE Recommanded response ",e);
		return jsonTestCaseList;
	}
	return jsonTestCaseList;
}

@Override
@Transactional
public org.json.JSONObject executeTestPlanGroup(String nodeRedTafJSONQuery) {
	Integer productId = null;
	String deviceNames = "";		
	String testcaseIds = "";
	Integer testPlanGroupId = null;
	JSONObject workpackageJsonObj = new JSONObject();
	org.json.JSONObject responseJson = new org.json.JSONObject();
	String testcaseNames = "";
	Map<RunConfiguration, String> runConfigTestcaseNames = new HashMap<RunConfiguration, String>();
	List<org.json.simple.JSONObject> testConfigs = new ArrayList<org.json.simple.JSONObject>();
	Set<RunConfiguration> runConfigurations = new HashSet<RunConfiguration>();
	VerificationResult deviceReadinessCheck = null;
	TestRunPlan testPlan = null;
	String message = "";
	String jobIds = null;
	boolean isValid = true;
	try {			
		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
		
		//Authenticate the user
		String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
		org.json.JSONObject authResponse = authenticateUser(userName, "/testExecution/executetestplan");
		if (authResponse != null)
			return authResponse;
		if(jsonFormatObject != null) {	
			if((String) jsonFormatObject.get("productId") != null)
				productId = Integer.valueOf((String) jsonFormatObject.get("productId"));
					
			if(jsonFormatObject.get("testConfigurations") != null){
				JSONArray jsonArray = (JSONArray) jsonFormatObject.get("testConfigurations");
				for (int i = 0; i < jsonArray.size(); i++) {
				    JSONObject explrObject = (JSONObject) jsonArray.get(i);
				    testConfigs.add(explrObject);
				}
			}
			if((String) jsonFormatObject.get("testPlanGroupId") != null)
				testPlanGroupId = Integer.valueOf((String) jsonFormatObject.get("testPlanGroupId"));
		}
		
		TestRunPlanGroup testPlanGroup = productListService.getTestRunPlanGroupById(testPlanGroupId);
		if(testPlanGroup == null){
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("data", "");
			responseJson.put("message", "TestplanGroup ID is missing or invalid");
			responseJson.put("Failure_Details", "TestplanGroup ID is missing or invalid");
			return responseJson;
		}
		List<TestRunPlangroupHasTestRunPlan> testrunplangrouplist=productListService.listTestRunPlanGroupMap(testPlanGroupId);
		if(testrunplangrouplist == null || testrunplangrouplist.isEmpty()){
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("data", "");
			responseJson.put("message", "There are 0 TestPlans avaialble for the TestplanGroup Name : " +testPlanGroup.getName());
			responseJson.put("Failure_Details", "There are 0 TestPlans avaialble for the TestplanGroup Name : " +testPlanGroup.getName());
			return responseJson;
		}
		testPlan = productListService.getFirstTestRunPlanByTestPlanGroupId(testPlanGroupId);
		if (testPlan == null) {
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("data", "");
			responseJson.put("message", "Testplan ID is invalid");
			responseJson.put("Failure_Details", "Testplan ID is invalid");
			return responseJson;
		}
		log.info("Test Plan Id : " +testPlan.getTestRunPlanId() +"testPlanGroupId : "+testPlanGroupId);
		ProductMaster product= testPlan.getProductVersionListMaster().getProductMaster();
		//Check authorization for user
		authResponse = checkUserAuthorization(product.getProductId(), userName, "/testExecution/executeSelectiveTestCasesTestPlan");
		if (authResponse != null)
			return authResponse;
		
		List<RunConfiguration> runConfigurationsList = productListService.listRunConfigurationByTestRunPlanId(testPlan.getTestRunPlanId(),0,10);	
		
		int isValidTestConfigurationCounter = 0;
		
		if(testConfigs != null && !testConfigs.isEmpty()){				
			for(org.json.simple.JSONObject obj : testConfigs){
				testcaseNames = "";
				RunConfiguration testConfiguration = productListService.getRunConfigurationByIdWithoutInitialization(Integer.valueOf((String)obj.get("testConfigurationId")));
				if(obj.get("testcaseIds") != null){
					testcaseIds = (String) obj.get("testcaseIds");
					if(testcaseIds != null && !testcaseIds.trim().isEmpty()) {
						for(String tcId : testcaseIds.split(",")){
							log.info("Test Case Id : "+tcId);
							TestCaseList tc = testCaseService.getByTestCaseIdwithoutInitialization(Integer.valueOf(tcId));
							testcaseNames += tc.getTestCaseName()+",";
						}
					} else {
						message += "There are no valid testcases available, so unable to trigger job for "+testConfiguration.getRunconfigName()+". ";
					}
				} else if((String) obj.get("testcaseNames") != null) { 
					testcaseNames = (String) obj.get("testcaseNames");
				}
				
				if((String) obj.get("device") != null) {
					deviceNames = (String) obj.get("device");
				}
				log.info("Test Configuration validation starts");											
				if(testConfiguration != null){						
					deviceReadinessCheck = productListService.testConfigurationReadinessCheck(testConfiguration, testPlan, null);
					if(runConfigurationsList.contains(testConfiguration) && deviceReadinessCheck.getIsReady() != null && deviceReadinessCheck.getIsReady().equalsIgnoreCase("yes")){
						if(testcaseNames != null && !testcaseNames.trim().isEmpty()) {
							runConfigurations.add(testConfiguration);
							runConfigTestcaseNames.put(productListService.getRunConfigurationByIdWithoutInitialization(Integer.valueOf((String)obj.get("testConfigurationId"))), testcaseNames);
							isValidTestConfigurationCounter++;
						}
					} else if(deviceReadinessCheck.getIsReady() != null && deviceReadinessCheck.getIsReady().equalsIgnoreCase("no")){
						message += "Test Configuration : "+testConfiguration.getRunconfigName()+" & Test Configuration ID : "+Integer.valueOf((String)obj.get("testConfigurationId"))+" is not active so unable to create job. ";
						responseJson.put("message", message);
						responseJson.put("Failure_Details", message);
						isValid = false;
					}
				}else{
					message += "Test Configuration ID : "+Integer.valueOf((String)obj.get("testConfigurationId"))+" is invalid. ";	
					isValid = false;
				}					
			}
		}			
		log.info("Test Configuration validation ends");		
		if(runConfigTestcaseNames == null || runConfigTestcaseNames.isEmpty()) {
			message = "No Active Jobs created due to invalid Test Configuration(s).No Execution Initiated.";
			workpackageJsonObj.put("Result", "FAILED");
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");
			responseJson.put("data", "No Active Jobs created due to invalid Test Configuration(s).No Execution Initiated.");
			responseJson.put("message", message);
			responseJson.put("Failure_Details", message);
			return responseJson;
		}				
		if(isValidTestConfigurationCounter > 0){
			log.info("All validation Completed..Going to start TestCycle Creation");
			WorkFlow workFlow = workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_NEW);
			TestCycle tc = new TestCycle();
			tc.setTestRunPlanGroup(testPlanGroup);
			tc.setResult("New");
			tc.setTestCycleStatus(workFlow.getStageName());
			tc.setStatus(1);
			tc.setStartTime(DateUtility.getCurrentTime());
			UserList userList= userListService.getUserByLoginId(IDPAConstants.USER_FOR_TESTRUN_PLAN);
			tc.setUserList(userList);
			workPackageService.addTestCycle(tc);
			log.info("Going to start workpackage creation");	
			//TestRunPlan testRunPlan = productListService.getTestRunPlanBytestRunPlanId(testPlanId);			
			WorkPackage newWorkpackage=new WorkPackage();
			String name=testPlan.getProductVersionListMaster().getProductMaster().getProductName()+"-"+testPlan.getProductVersionListMaster().getProductVersionName()+"-"+DateUtility.getCurrentTime();
			
			newWorkpackage.setName(name);
			newWorkpackage.setDescription(name +" created.");
			
			newWorkpackage.setCreateDate(DateUtility.getCurrentTime());
			newWorkpackage.setModifiedDate(DateUtility.getCurrentTime());
			newWorkpackage.setStatus(1);
			newWorkpackage.setIsActive(1);
			
			if(testPlan.getExecutionType().getExecutionTypeId()==3) {
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterService.getExecutionTypeByExecutionTypeId(7);
				newWorkpackage.setWorkPackageType(executionTypeMaster);
			} else {
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterService.getExecutionTypeByExecutionTypeId(8);
				newWorkpackage.setWorkPackageType(executionTypeMaster);
			}
			log.info("Intermediate log while workapackage creation");
			ProductBuild productBuild = productBuildDAO.getLatestProductBuild(testPlan.getProductVersionListMaster().getProductMaster().getProductId());
			newWorkpackage.setProductBuild(productBuild);
			
			newWorkpackage.setTestRunPlan(testPlan);
			newWorkpackage.setTestRunPlanGroup(testPlanGroup);
			newWorkpackage.setSourceType(IDPAConstants.WORKPACKAGE_SOURCE_TESTRUNPLANGROUP);
			
			WorkFlowEvent workFlowEvent = new WorkFlowEvent();
			workFlowEvent.setEventDate(DateUtility.getCurrentTime());
			workFlowEvent.setRemarks("New Workapckage Added :"+newWorkpackage.getName());
			workFlowEvent.setUser(null);
			workFlowEvent.setWorkFlow(workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_NEW));
			
			workPackageService.addWorkFlowEvent(workFlowEvent);
			newWorkpackage.setWorkFlowEvent(workFlowEvent);
			newWorkpackage.setPlannedEndDate(DateUtility.getCurrentTime());
			newWorkpackage.setPlannedStartDate(DateUtility.getCurrentTime());
			//UserList userList= userListService.getUserByLoginId(IDPAConstants.USER_FOR_TESTRUN_PLAN);
			newWorkpackage.setUserList(userList);
			newWorkpackage.setIterationNumber(-1);
			LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
			lifeCyclePhase.setLifeCyclePhaseId(4);
			newWorkpackage.setLifeCyclePhase(lifeCyclePhase);
			newWorkpackage.setWorkPackageId(0);
			newWorkpackage.setTestExecutionMode(testPlan.getAutomationMode());
			newWorkpackage.setTestCycle(tc);
			if(productBuild!=null){
				workPackageService.addWorkPackage(newWorkpackage);	
				log.info("WorkPackage created successfully");
				workPackageService.workpackageSelectiveExecutionPlan(newWorkpackage,testPlan,runConfigTestcaseNames,request,deviceNames);
			}else{
				log.info("Unable to execute Test Run Plan. This could be because the Product Build specified is not active");
			}
			
			workpackageJsonObj.put("TP Execution Initiated", "Yes");
			workpackageJsonObj.put("Workpackage ID", newWorkpackage.getWorkPackageId());
			workpackageJsonObj.put("Workpackage Name", newWorkpackage.getName());
			workpackageJsonObj.put("Test Plan Id", testPlan.getTestRunPlanId());
			workpackageJsonObj.put("Test Plan Name", testPlan.getTestRunPlanName());
			if(ScriptLessExecutionDTO.getJobIDs() != null && !ScriptLessExecutionDTO.getJobIDs().isEmpty())
				workpackageJsonObj.put("Total Jobs", ScriptLessExecutionDTO.getJobIDs().split(",").length);
			else
				workpackageJsonObj.put("Total Jobs", 0);
			workpackageJsonObj.put("Job Nos", ScriptLessExecutionDTO.getJobIDs());
			
			Set<TestRunJob> jobs = newWorkpackage.getTestRunJobSet();
			JSONObject jobJsonObject = null;
			org.json.JSONArray jobJsonObjectsArray = new org.json.JSONArray();
			for (TestRunJob job : jobs) {
				jobJsonObject = new JSONObject();
				jobJsonObject.put("Job ID", job.getTestRunJobId());
				jobJsonObject.put("Environment", job.getEnvironmentCombination().getEnvironmentCombinationName());
				jobJsonObject.put("Test System", job.getHostList().getHostName());
				jobJsonObject.put("Test System IP", job.getHostList().getHostIpAddress());
				if (!(job.getGenericDevices() == null)) {
					jobJsonObject.put("Test Device", job.getGenericDevices().getName());
					jobJsonObject.put("Type", job.getGenericDevices().getDeviceModelMaster().getDeviceType().getDeviceTypeName());
					jobJsonObject.put("Model", job.getGenericDevices().getDeviceModelMaster().getDeviceModel() + " : " + job.getGenericDevices().getDeviceModelMaster().getDeviceName());
					jobJsonObject.put("Resolution", job.getGenericDevices().getDeviceModelMaster().getDeviceResolution());
				} else {
					jobJsonObject.put("Test Device", "NA");
					jobJsonObject.put("Type", "NA");
					jobJsonObject.put("Model", "NA");
					jobJsonObject.put("Resolution", "NA");
				}
				jobJsonObject.put("Total testcases", job.getPlannedTestCasesCount());
				jobJsonObject.put("Average execution time", "TBD");
				jobJsonObject.put("Execution expected to be completed by", "TBD");
				jobJsonObject.put("Status", "TBD");
				jobJsonObject.put("Result", "TBD");
				jobJsonObject.put("Execution_Mode",  testPlan.getAutomationMode());
				jobJsonObjectsArray.put(jobJsonObject);
			}
			workpackageJsonObj.put("Jobs", jobJsonObjectsArray);

			workpackageJsonObj.put("Result", "PASSED");
			
			if(workpackageJsonObj.get("Job Nos") != null){
				jobIds = (String) workpackageJsonObj.get("Job Nos");
			}
			log.info("Job Nos : "+jobIds);
			String wpMessage = "";
			//Added for selective changes
			if(jobIds != null && !jobIds.trim().isEmpty()){
				wpMessage +=  testPlan.getTestRunPlanName() + " execution initiated with Workpackage [" + newWorkpackage.getWorkPackageId() + "] " + newWorkpackage.getName() + System.lineSeparator() + System.lineSeparator() + "Jobs " + jobIds.substring(0, jobIds.length()-1) + " queued for execution.";
			} else {
				wpMessage += testPlan.getTestRunPlanName() + " execution not initiated as there are no valid Test Jobs";
				isValid = false;
			}
			
			if(!isValid) {
				responseJson.put("result", "ERROR");
			} else {
				responseJson.put("result", "OK");					
			}
			//If any one test configuration is not valid in multiple test configuration scenario, then both the error and success message has to be displayed.
			message = wpMessage + System.lineSeparator() + message;
			responseJson.put("wpMessage", wpMessage);
			responseJson.put("status", "200");
			responseJson.put("data", workpackageJsonObj.toString());
			responseJson.put("message", message);
			responseJson.put("Failure_Details", message);
			log.info("responseJson : "+responseJson);
		} else {
			message += "No Active Jobs created due to invalid Test Configuration or Test Plan input.No Execution Initiated.";
			workpackageJsonObj.put("Result", "FAILED");
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");
			responseJson.put("data", "No Active Jobs created due to invalid Test Configuration or Test Plan input.No Execution Initiated.");
			responseJson.put("message", message);
			responseJson.put("Failure_Details", message);
		}
	} catch(Exception e){
		try {
			workpackageJsonObj.put("Result", "FAILED");
			workpackageJsonObj.put("message", message + System.lineSeparator() + "Test Plan execution not initiated due to some issue." + System.lineSeparator() + e.getStackTrace());
			workpackageJsonObj.put("Failure_Details", e.getStackTrace());
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");
			responseJson.put("data", workpackageJsonObj.toString());
		} catch (Exception e1) {
			log.error("Problem while executing Test Plan through REST call ", e1);
		}
		log.error("Problem while executing Test Plan through REST call ", e);
	}
	return responseJson;
}
}