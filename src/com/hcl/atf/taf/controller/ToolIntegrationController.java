package com.hcl.atf.taf.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.integration.CustomTestSystemConnectorsManager;
import com.hcl.atf.taf.integration.defectManagementSystem.TAFDefectManagementSystemIntegrationFactory;
import com.hcl.atf.taf.integration.defectManagementSystem.TAFDefectManagementSystemIntegrator;
import com.hcl.atf.taf.integration.testManagementSystem.TAFTestManagementSystemIntegrationFactory;
import com.hcl.atf.taf.integration.testManagementSystem.TAFTestManagementSystemIntegrator;
import com.hcl.atf.taf.model.DefectManagementSystem;
import com.hcl.atf.taf.model.DefectManagementSystemMapping;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.SCMSystem;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestExecutionResultsExportData;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestManagementSystemMapping;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunConfigurationParent;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestStepExecutionResult;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.ToolIntagrationMaster;
import com.hcl.atf.taf.model.ToolTypeMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPSummaryDTO;
import com.hcl.atf.taf.model.json.JsonDefectManagementSystem;
import com.hcl.atf.taf.model.json.JsonDefectManagementSystemMapping;
import com.hcl.atf.taf.model.json.JsonProductMaster;
import com.hcl.atf.taf.model.json.JsonSCMManagementSystem;
import com.hcl.atf.taf.model.json.JsonTestExecutionResultBugList;
import com.hcl.atf.taf.model.json.JsonTestManagementSystem;
import com.hcl.atf.taf.model.json.JsonTestManagementSystemMapping;
import com.hcl.atf.taf.model.json.JsonToolIntagrationMaster;
import com.hcl.atf.taf.model.json.JsonToolTypeMaster;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.DefectManagementService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.PasswordEncryptionService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestExecutionBugsService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.TestManagementService;
import com.hcl.atf.taf.service.TestReportService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.ToolIntagrationMasterService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.atf.taf.tfs.TFSIntegrator;
import com.hcl.atf.taf.tfs.connector.TFSConnector;
import com.hcl.connector.ConnectorCredentials;
import com.hcl.connector.rest.hpqcrest.ConnectorHPQCRest;
import com.hcl.connector.util.Authentication;
import com.hcl.hpqc.connector.ConnectorHPQC;
import com.hcl.jira.rest.JiraConnector;
import com.hcl.ota.TestResult;
import com.hcl.result.TestCaseResult;
import com.hcl.result.TestSetResult;
import com.hcl.result.TestStepResult;
import com.hcl.types.SEVERITY;
import com.hcl.types.STATUS;
import com.microsoft.tfs.core.TFSTeamProjectCollection;


@Controller
public class ToolIntegrationController {

	private static final Log log = LogFactory.getLog(ToolIntegrationController.class);
	
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
	private WorkPackageDAO workPackageDAO; 
	
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
	private ToolIntagrationMasterService toolIntagrationMasterService;
	
	@Autowired
	private CustomTestSystemConnectorsManager customTestSystemConnectorsManager;
	
	@Value("#{ilcmProps['Testlink_2_DefectManagementSystem']}")
    private String  testlink_2_DefectManagementSystem;
	
	@Autowired
	ServletContext servletContext;
	
	public String testCaseFolderById = "";
	
	public String testSetFolderById = "";
	
	public String testResourceFolderById = "";
	
	public String testSetFolderByName = "";
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value="test.defects.export.workpackage", method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse testDefectsJiraExport(@RequestParam Integer workpackageId,HttpServletRequest request,HttpServletResponse response,@RequestParam String dmsIdValues) {
		log.info("test.defects.export");
		JTableResponse jTableResponse = null;
		String fileName = "";
		String status = "";
		ConnectorHPQCRest hpqcConnector = null;
		
		try {
			log.info("export defects started");
			WorkPackage workPackage=workPackageService.getWorkPackageById(workpackageId);
			ProductMaster productMaster=workPackage.getProductBuild().getProductVersion().getProductMaster();
			String[] dmsIds = dmsIdValues.split(",");
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
	
	
	
	@RequestMapping(value="export.defect.bugId", method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse testDefectExportByBug(@RequestParam Integer tebId,HttpServletRequest request,HttpServletResponse response,@RequestParam String dmsIdValues) {
		log.info("test.defects.jira.export");
		JTableResponse jTableResponse = null;
		String fileName = "";
		String status = "";
		ConnectorHPQCRest hpqcConnector = null;
		ProductMaster productMaster=null;
		try {
			log.info("export defects started");
			TestExecutionResultBugList testExecutionResultBugList=testExecutionBugsService.getByBugId(tebId);
			WorkPackage workPackage=testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage();
			productMaster=workPackage.getProductBuild().getProductVersion().getProductMaster();

			String[] dmsIds = dmsIdValues.split(",");
			for (String dms : dmsIds) {
				if(dms != null && !dms.trim().equalsIgnoreCase("") && !dms.isEmpty()){
					DefectManagementSystem defectManagementSystem=defectManagementService.getDMSByProductId(productMaster.getProductId(),Integer.parseInt(dms));
					if(defectManagementSystem.getToolIntagration() !=null && defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.JIRA_TOOL)){
						JiraConnector jiraConnector = getJIRAConnectorByDefectSystemId(defectManagementSystem.getDefectManagementSystemId(), request);
						status = tafDefectManagementSystemIntegrator.fileDefectsToJira(workPackage.getWorkPackageId(),jiraConnector,testExecutionResultBugList);		
						jTableResponse = new JTableResponse("Ok", status);					
					}else if(defectManagementSystem.getToolIntagration() !=null && defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
						hpqcConnector =  getHPQCDefectManagementSystemConnectorRest(productMaster.getProductId(), request);
						if (hpqcConnector == null) {
							jTableResponse = new JTableResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
							return jTableResponse;
						}
				
						String importCompleteStatus =  tafTestManagementSystemIntegrator.exportTestExecutionDefectsRest(testExecutionResultBugList, hpqcConnector);
						hpqcConnector.logout(hpqcConnector);
						if(importCompleteStatus != null){
							log.info("Export TestDefects completed.");
							jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
						} else{
							log.info(importCompleteStatus);
							jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
						}
					} else if(defectManagementSystem.getToolIntagration() != null && defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.TFS_TOOL)){
						TFSIntegrator tfsConnector = getTFSConnector(defectManagementSystem.getConnectionUri().trim());
						TFSConnector.USERNAME=defectManagementSystem.getConnectionUserName().trim();
						TFSConnector.PASSWORD=passwordEncryptionService.decrypt(defectManagementSystem.getConnectionPassword().trim());
						TFSTeamProjectCollection projectCollection = getTFSProjectCollection(tfsConnector, defectManagementSystem.getConnectionUri().trim());
						if (tfsConnector == null) {
							jTableResponse = new JTableResponse("Error", "Unable to establish connection to TFS. Check the mappings");				
						}
						String projectUrl = defectManagementSystem.getConnectionUri().trim();
						String importCompleteStatus =  tafTestManagementSystemIntegrator.exportTFSTestRunJobTestExecutionDefectsRest(testExecutionResultBugList, tfsConnector, projectCollection, defectManagementSystem.getDefectManagementSystemId(), projectUrl);
						projectCollection.close();
						if(importCompleteStatus != null){
							log.info("Export TestDefects completed.");
							jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
						} else{
							log.info(importCompleteStatus);
							jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
						}
					} else if(true){
						customTestSystemConnectorsManager.initializeTestSystemConnectorsManager(defectManagementSystem.getToolIntagration().getName(),defectManagementSystem.getDefectSystemVersion(),"Defect Management System",testlink_2_DefectManagementSystem);
						if (customTestSystemConnectorsManager.isConnectorAvailable(defectManagementSystem.getToolIntagration().getName(),defectManagementSystem.getDefectSystemVersion(),"Defect Management System")) {
							ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
							JsonTestExecutionResultBugList jsonTestExecutionResultBugList = new JsonTestExecutionResultBugList(testExecutionResultBugList);
							String defectsJson = ow.writeValueAsString(jsonTestExecutionResultBugList);
							String systemConnectionDetailsJson="";
							customTestSystemConnectorsManager.reportDefectsToSystem(systemConnectionDetailsJson, defectsJson);
							
						} else {
							log.info("Connector for the specified Test Management System & version is not available.");
							jTableResponse = new JTableResponse("Ok","Connector for the specified Test Management System & version is not available.");
						}
						if(testExecutionResultBugList != null){
							log.info("Export TestDefects completed.");
							jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
						} 
					}else{
						jTableResponse = new JTableResponse("Error","Defect export is not available for this system:"+defectManagementSystem.getToolIntagration()!=null ? defectManagementSystem.getToolIntagration().getName() : "");
						
					}
				} else {
					log.error("Defect Management System does not exist (or) map to the product : "+productMaster.getProductName());
				}
			}
		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("ERROR","Error in Export defects");
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value="export.defect.bugId.testRunJob", method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse testDefectJiraExportTestRunJob(@RequestParam Integer testRunJobId,HttpServletRequest request,HttpServletResponse response,@RequestParam String dmsIdValues) {
		log.info("test.defects.jira.export");
		JTableResponse jTableResponse = null;
		String fileName = "";
		String status = "";
		ConnectorHPQCRest hpqcConnector = null;
		ProductMaster productMaster=null;
		try {
			log.info("export defects started");
			Set<TestExecutionResultBugList> bugs = null;

			TestRunJob testRunJob=environmentService.getTestRunJob(testRunJobId);
			TestRunPlan testRunPlan =testRunJob.getTestRunPlan();
			
			bugs = new HashSet<TestExecutionResultBugList>();
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans=testRunJob.getWorkPackageTestCaseExecutionPlans();
			for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
				bugs.addAll(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestExecutionResultBugListSet());
			}
			productMaster =testRunPlan.getProductVersionListMaster().getProductMaster();
			int defectSystemId=-1;
			String[] dmsIds = dmsIdValues.split(",");
			for (String dms : dmsIds) {
				if(dms != null && !dms.trim().equalsIgnoreCase("") && !dms.isEmpty()){
					DefectManagementSystem defectManagementSystem=defectManagementService.getDMSByProductId(productMaster.getProductId(),Integer.parseInt(dms));
					if(defectManagementSystem!=null ){
						if( defectManagementSystem.getToolIntagration() !=null && defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase("JIRA")){
							log.info("Primary defect management system is JIRA");
							defectSystemId = defectManagementSystem.getDefectManagementSystemId();
							JiraConnector jiraConnector = getJIRAConnectorByDefectSystemId(defectSystemId, request);
							tafDefectManagementSystemIntegrator.jiraDefectsFile(bugs,jiraConnector);
							jTableResponse = new JTableResponse("Ok", status);					

						}else if(defectManagementSystem.getToolIntagration() !=null && defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase("HPQC")){
							defectSystemId = defectManagementSystem.getDefectManagementSystemId();
							log.info("Primary defect management system is HPQC");
							defectsExportHPQCTestRunJob(testRunJob.getTestRunJobId(),request,defectSystemId);
							jTableResponse = new JTableResponse("Ok", status);					
						}
					}else if(true){
						List<JsonTestExecutionResultBugList> jsonBugs = new ArrayList<JsonTestExecutionResultBugList>();
						customTestSystemConnectorsManager.initializeTestSystemConnectorsManager(defectManagementSystem.getToolIntagration().getName(),defectManagementSystem.getDefectSystemVersion(),"Defect Management System",testlink_2_DefectManagementSystem);
						if (customTestSystemConnectorsManager.isConnectorAvailable(defectManagementSystem.getToolIntagration().getName(),defectManagementSystem.getDefectSystemVersion(),"Defect Management System")) {
							ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
							if(bugs != null && bugs.size() >0) {
								for(TestExecutionResultBugList testExecutionResultBugList:bugs) {
									jsonBugs.add(new JsonTestExecutionResultBugList(testExecutionResultBugList));
								}
							}
							String defectsJson = ow.writeValueAsString(jsonBugs);
							String systemConnectionDetailsJson="";
							customTestSystemConnectorsManager.reportDefectsToSystem(systemConnectionDetailsJson, defectsJson);
						} else {
							log.info("Connector for the specified Test Management System & version is not available.");
							jTableResponse = new JTableResponse("Ok","Connector for the specified Test Management System & version is not available.");
						}
						if(jsonBugs != null && jsonBugs.size() >0){
							log.info("Export TestDefects completed.");
							jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
						}
						} else{
						jTableResponse = new JTableResponse("Error","Defect export is not available for this system:"+defectManagementSystem.getToolIntagration().getName());
					}
				}else {
					log.error("Defect Management System does not exist (or) map to the product : "+productMaster.getProductName());
				}				
			}
		} catch (Exception e) {
			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("ERROR","Error in Export defects");
		}
		return jTableResponse;
	}

	@RequestMapping(value="test.DMS.connection", method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getDMSConnector(@RequestParam int dmsId, HttpServletRequest request){
		JiraConnector jiraConnector = null;
		JTableResponse jTableResponse = null;
		ConnectorHPQCRest hpqcConnector = null;
		try {
			DefectManagementSystem defectManagementSystem=defectManagementService.getByDefectManagementSystemId(dmsId);
			
			if(defectManagementSystem != null && defectManagementSystem.getToolIntagration() !=null && defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.JIRA_TOOL)){
				jiraConnector=getJIRAConnectorByDefectSystemId(dmsId, request);
				if(jiraConnector!=null){
					jTableResponse = new JTableResponse("SUCCESS","Connection Success");
				}else{
					jTableResponse = new JTableResponse("Error","Connection Failed");
				}
			}else if(defectManagementSystem != null && defectManagementSystem.getToolIntagration() != null && defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
				
				hpqcConnector=getHPQCConnectorRest(defectManagementSystem.getProductMaster(), request);
				if(hpqcConnector!=null){
					jTableResponse = new JTableResponse("SUCCESS","Connection Success");
				}else{
					jTableResponse = new JTableResponse("Error","Connection Failed");
				}
			} else if(defectManagementSystem != null && defectManagementSystem.getToolIntagration() != null && defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.TFS_TOOL)){
				TFSIntegrator tfsConnector = getTFSConnector(defectManagementSystem.getConnectionUri().trim());
				TFSConnector.USERNAME=defectManagementSystem.getConnectionUserName().trim();
				TFSConnector.PASSWORD=passwordEncryptionService.decrypt(defectManagementSystem.getConnectionPassword().trim());
				TFSTeamProjectCollection projectCollection = getTFSProjectCollection(tfsConnector, defectManagementSystem.getConnectionUri().trim());
				if(projectCollection!=null){
					jTableResponse = new JTableResponse("SUCCESS","Connection Success");
				} else {
					jTableResponse = new JTableResponse("Error","Connection Failed");
				}
			}
		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("ERROR","Error in configuration");
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

	@RequestMapping(value="test.devices.defects.jira.export", method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse testDeviceDefectsJiraExport(@RequestParam Integer testRunNo,@RequestParam Integer testRunConfigurationChildId,Integer deviceListId, ModelAndView modelAndView,HttpServletRequest request,HttpServletResponse response) {
		log.debug("test defects Jira Export");
		JTableResponse jTableResponse;
		String fileName = "";
		String status = "";
		
		try {			
			log.info("Export Defects to JIRA..Connection setup started.");
			InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\jira.properties");
			JiraConnector jiraConnector = new JiraConnector(fis);
			status = tafDefectManagementSystemIntegrator.fileDeviceDefectsToJira(testRunNo, testRunConfigurationChildId, deviceListId, jiraConnector);
			log.info("Export Defects to JIRA..Compelted.");
			List result = null;			
			jTableResponse = new JTableResponse("Ok",status);
		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("Error in Export");
		}
		return jTableResponse;
	}

	@RequestMapping(value="test.defect.jira.export", method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse testDefectJiraExport(@RequestParam Integer testExecutionResultBugId, ModelAndView modelAndView,HttpServletRequest request,HttpServletResponse response) {
		log.info("test.defect.jira.export");
		JTableResponse jTableResponse;
		String fileName = "";
		String status;
		
		try {
			log.info("export defects started");
			log.info("Export Defects to JIRA..Connection setup started.");
			InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\jira.properties");	
			JiraConnector jiraConnector = new JiraConnector(fis);
			status = tafDefectManagementSystemIntegrator.fileDefectToJira(testExecutionResultBugId, jiraConnector);
			List result = null;
			log.info("Export Defects to JIRA..Completed.");
			jTableResponse = new JTableResponse("Ok",status);
		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("Error in Export");
		}
		return jTableResponse;
	}
		
	@ExceptionHandler(Exception.class)
	public void handleException(Throwable ex, HttpServletResponse response)
			throws Exception {

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				ex.getMessage());
		log.error("Internal Server Error", ex);
		//ex.printStackTrace();
	}
	@RequestMapping(value="test.TMS.connection", method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getTMSConnector(@RequestParam int tmsId, HttpServletRequest request){
		ConnectorHPQC hpqcConnector = null;
		JTableResponse jTableResponse = null;
		
		try {
			TestManagementSystem testManagementSystem=testManagementService.getByTestManagementSystemId(tmsId);
			if(testManagementSystem.getToolIntagration()!=null && testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
				hpqcConnector=getHPQCConnector(testManagementSystem.getProductMaster(), request);
				if(hpqcConnector!=null){
					jTableResponse = new JTableResponse("SUCCESS","Connection Success","");
				}else{
					jTableResponse = new JTableResponse("Error","Connection Failed","");
				}
			}
		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("ERROR","Error in configuration");
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="test.TMS.connection.rest", method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getTMSConnectorRest(@RequestParam int tmsId, HttpServletRequest request){
		ConnectorHPQCRest hpqcConnector = null;
		JTableResponse jTableResponse = null;
		
		try {
			TestManagementSystem testManagementSystem=testManagementService.getByTestManagementSystemId(tmsId);
			if(testManagementSystem.getToolIntagration()!=null && testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
				hpqcConnector=getHPQCConnectorRest(testManagementSystem.getProductMaster(), request);
				if(hpqcConnector!=null){
					List<String> projectnames = tafTestManagementSystemIntegrator.getDomainProjectNameList(hpqcConnector, testManagementSystem.getConnectionProperty1().trim());
					List<String> domainnames = tafTestManagementSystemIntegrator.getDomainNameList(hpqcConnector);
					if(domainnames != null && domainnames.size() >0) {
						for(int j=0;j<domainnames.size();j++){						
							if(domainnames.get(j).equals(testManagementSystem.getConnectionProperty1().trim())){
								for(int i=0;i<projectnames.size();i++){	
									Set<TestManagementSystemMapping> tsm = testManagementSystem.getTestManagementSystemMappings();
									for(TestManagementSystemMapping ts : tsm){
										if(projectnames.get(i).trim().equalsIgnoreCase(ts.getMappingValue().trim())){
											jTableResponse = new JTableResponse("SUCCESS","Connection Success");
											return jTableResponse;
										}
									}													
								}
							}						
						}
					} else {
						jTableResponse = new JTableResponse("Error","Connection Failed");
						return jTableResponse;
					}
				} else {
					jTableResponse = new JTableResponse("Error","Connection Failed");
					return jTableResponse;
				}				
			}else if(testManagementSystem.getToolIntagration()!=null && testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.TFS_TOOL)){
					TFSIntegrator tfsConnector = getTFSConnector(testManagementSystem.getConnectionUri().trim());
					TFSConnector.USERNAME=testManagementSystem.getConnectionUserName().trim();
					TFSConnector.PASSWORD=passwordEncryptionService.decrypt(testManagementSystem.getConnectionPassword().trim());
					TFSTeamProjectCollection projectCollection = getTFSProjectCollection(tfsConnector, testManagementSystem.getConnectionUri().trim());
					if(projectCollection!=null){
						List<String> projectNames = tfsConnector.getTFSProducts(projectCollection);
						if(projectNames != null && projectNames.size() >0) {
							for(int i=0;i<projectNames.size();i++){	
								Set<TestManagementSystemMapping> tsm = testManagementSystem.getTestManagementSystemMappings();
								for(TestManagementSystemMapping ts : tsm){
									if(projectNames.get(i).trim().equalsIgnoreCase(ts.getMappingValue().trim())){
										jTableResponse = new JTableResponse("SUCCESS","Connection Success");
										return jTableResponse;
									} else {
										jTableResponse = new JTableResponse("Error","Connection Failed");
										tfsConnector = null;
									}
								}										
							}	
						}
					}
				}else if(testManagementSystem.getToolIntagration()!=null && testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.SVN_TOOL)){
					String svnConnectUri = testManagementSystem.getConnectionUri().trim();
					if(svnConnectUri != null && svnConnectUri != ""){
						String svnusername = testManagementSystem.getConnectionUserName().trim();
						String svnpassword = passwordEncryptionService.decrypt(testManagementSystem.getConnectionPassword().trim());
						Authentication auth = new Authentication();
						try{
							if(auth.checkPassword(svnConnectUri,svnusername,svnpassword)){
								jTableResponse = new JTableResponse("SUCCESS","Connection Success");
								return jTableResponse;
							}else{
								jTableResponse = new JTableResponse("Error","Connection Failed");
							}
						} catch(Exception e){
							
						}
						
					}
				}
			else {
					jTableResponse = new JTableResponse("Error","Connection Failed");
					hpqcConnector = null;
				}				
				
			
		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("ERROR","Error in configuration");
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
					if (testManagementSystem.getToolIntagration() != null && testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(TAFConstants.TEST_MANAGEMENT_SYSTEM_HPQC)){
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
	
	@RequestMapping(value="test.import.testcases", produces="application/json" )
	public @ResponseBody JTableResponse testDataimport(@RequestParam Integer productId, HttpServletRequest request,HttpServletResponse response) {
		log.debug("test.import");
		JTableResponse jTableResponse;
		ConnectorHPQC hpqcConnector;
		try {
			log.info("Product Id input:" + productId);
			String testCaseSource = IDPAConstants.HPQC_TOOL;
			
			ProductMaster product = productListService.getProductById(productId);		
			hpqcConnector =  getHPQCConnector(product, request);
			if (hpqcConnector == null) {
				jTableResponse = new JTableResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
				return jTableResponse;
			}
		
			String importCompleteStatus =  tafTestManagementSystemIntegrator.importTestCasesOTA(productId, hpqcConnector, testCaseSource);
			hpqcConnector.disconnect();
			if(importCompleteStatus != null){
				log.info("Import testCases Completed.");
				jTableResponse = new JTableResponse("SUCCESS","Import testCases Completed.");
			} else{
				log.info("Import completed");
				jTableResponse = new JTableResponse("SUCCESS","Import completed");
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error in Import");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="test.export.testresults", produces="application/json" )
	public @ResponseBody JTableResponse testResultsExport(@RequestParam Integer workpackageId, HttpServletRequest request,HttpServletResponse response,@RequestParam Integer tmsId) {
		log.debug("test.export");
		JTableResponse jTableResponse = null;
		ConnectorHPQC hpqcConnector = null;
		try {
			log.info("testRunListId Id input:" + workpackageId);
			
			WorkPackage workPackage=workPackageService.getWorkPackageById(workpackageId);
			ProductMaster product = workPackage.getProductBuild().getProductVersion().getProductMaster();
			TestManagementSystem testManagementSystem=null;
			if(tmsId!=null)
				testManagementSystem=testManagementService.getByTestManagementSystemId(tmsId);
			if(testManagementSystem.getToolIntagration() != null && testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
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
	
	@RequestMapping(value="test.export.testresults.testrunjob", produces="application/json" )
	public @ResponseBody JTableResponse testResultsExportByTestrunjob(@RequestParam Integer testRunJobId, HttpServletRequest request,HttpServletResponse response) {
		log.debug("test.export");
		JTableResponse jTableResponse;
		TestRunList testRunList = null;
		ConnectorHPQC hpqcConnector = null;
		try {
			log.info("testRunJobId Id input:" + testRunJobId);
			TestRunJob testRunJob=environmentService.getTestRunJob(testRunJobId);
			
			
			ProductMaster product = testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster();
			
			hpqcConnector =  getHPQCConnector(product, request);
			if (hpqcConnector == null) {
				jTableResponse = new JTableResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
				return jTableResponse;
			}
			TAFTestManagementSystemIntegrator tafTestManagementSystemIntegrator = TAFTestManagementSystemIntegrationFactory.getTestManagementSystemIntegrator("HPQC");
			
			String importCompleteStatus =  exportTestExecutionResults(testRunJob, hpqcConnector);
			hpqcConnector.disconnect();
			if(importCompleteStatus != null){
				log.info("Export TestResults completed.");
				jTableResponse = new JTableResponse("Ok","Export TestResults completed.");
			} else{
				log.info("Export TestResults completed.");
				jTableResponse = new JTableResponse("Ok","Export TestResults completed");
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in Export");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="test.export.testresults.testrunjob.rest", produces="application/json" )
	public @ResponseBody JTableResponse testResultsExportByTestrunjobRest(@RequestParam Integer testRunJobId, HttpServletRequest request,HttpServletResponse response,@RequestParam String tmsIdValues,@RequestParam String fileLocation) {
		log.debug("test.export");
		JTableResponse jTableResponse=null;
		ConnectorHPQCRest hpqcConnector = null;
		String tfsProductName = "";
		try {
			log.info("testRunJobId Id input:" + testRunJobId);
			TestRunJob testRunJob=environmentService.getTestRunJob(testRunJobId);
			TestManagementSystem testManagementSystem=null;
			
			ProductMaster product = testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster();
			String[] tmsIds = tmsIdValues.split(",");
			for (String tms : tmsIds) {
				testManagementSystem=testManagementService.getTMSByProduct(Integer.parseInt(tms),product.getProductId());
				if(testManagementSystem == null) {
					jTableResponse = new JTableResponse("Error", "Unable to get Test management systems for this runJobId"+testRunJobId);
					return jTableResponse;
				}
				Set<TestManagementSystemMapping> testManagementMappings = testManagementSystem.getTestManagementSystemMappings();
				if(testManagementMappings != null && !testManagementMappings.isEmpty()){
					for(TestManagementSystemMapping testManagementSystemMapping : testManagementMappings){
						if(testManagementSystemMapping.getMappingType().equals(TAFConstants.ENTITY_PRODUCT)){
							tfsProductName = testManagementSystemMapping.getMappingValue();
						}
					}
				}
				if(testManagementSystem.getToolIntagration() != null && testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
					hpqcConnector =  getHPQCConnectorRest(product, request);
					if (hpqcConnector == null) {
						jTableResponse = new JTableResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
						return jTableResponse;
					}
					String importCompleteStatus =  tafTestManagementSystemIntegrator.exportTestExecutionResultsRest(testRunJob, hpqcConnector,testManagementSystem);
					if(importCompleteStatus != null){
						log.info("Export TestResults completed.");
						jTableResponse = new JTableResponse("Ok","Export TestResults completed.");
					} else{
						log.info("Export TestResults completed.");
						jTableResponse = new JTableResponse("ERROR","Error in Exporting TestResults");
					}
				} else if(testManagementSystem.getToolIntagration() != null && testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.TFS_TOOL)){
					TFSIntegrator tfsConnector = getTFSConnector(testManagementSystem.getConnectionUri().trim());
					TFSConnector.USERNAME=testManagementSystem.getConnectionUserName().trim();
					TFSConnector.PASSWORD=passwordEncryptionService.decrypt(testManagementSystem.getConnectionPassword().trim());
					TFSTeamProjectCollection projectCollection = getTFSProjectCollection(tfsConnector, testManagementSystem.getConnectionUri().trim());
					List<Integer> runIds = tafTestManagementSystemIntegrator.tfsExportTestResults(testRunJob, tfsConnector, testManagementSystem,projectCollection, tfsProductName);
					for(int i=0;i<runIds.size();i++){
						if(fileLocation==""){
							String productName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductName();
							String testRunPlanName = testRunJob.getWorkPackage().getTestRunPlan().getTestRunPlanName();
							fileLocation = CommonUtility.getCatalinaPath().trim()+IDPAConstants.JASPERREPORTS_PATH+File.separator+"WorkPackages"+File.separator+testRunJob.getWorkPackage().getWorkPackageId()+File.separator+"Jobs"+File.separator+testRunJobId+File.separator+productName+"-"+testRunPlanName+"-"+testRunJobId+".pdf";
						}
						if(new File(fileLocation).exists()){
							uploadReportsToTestRun(fileLocation.trim(),runIds.get(i), testManagementSystem);
						} else {
							log.info("Test Run Job Report does not exist to upload to TFS");
						}
					}
					if(runIds.size() > 0){
						log.info("Export TestResults completed.");
						jTableResponse = new JTableResponse("Ok","Export TestResults completed.");
					} else{
						log.info("Export TestResults completed.");
						jTableResponse = new JTableResponse("ERROR","Error in Exporting TestResults");
					}
					
			} else if(true) {
					customTestSystemConnectorsManager.initializeTestSystemConnectorsManager(testManagementSystem.getToolIntagration().getName(),testManagementSystem.getTestSystemVersion(),"Test Management Systems",testlink_2_DefectManagementSystem);
					if (customTestSystemConnectorsManager.isConnectorAvailable(testManagementSystem.getToolIntagration().getName(),testManagementSystem.getTestSystemVersion(),"Test Management Systems")) {
						String testExecutionResults=tafTestManagementSystemIntegrator.exportTestExecutionResultsToTestLink(testRunJob, testManagementSystem);
						String systemConnectionDetailsJson="";
						customTestSystemConnectorsManager.reportTestExecutionResultsToSystem(systemConnectionDetailsJson, testExecutionResults);
						
					} else {
						log.info("Connector for the specified Test Management System & version is not available.");
						jTableResponse = new JTableResponse("Ok","Connector for the specified Test Management System & version is not available.");
					}
				}
				else{
						jTableResponse = new JTableResponse("Error","Export TestResults is not available for this system: "+ testManagementSystem.getToolIntagration() != null ?testManagementSystem.getToolIntagration().getName():"");
				}
			}
		} catch (Exception e) {
			//jTableResponse = new JTableResponse("Error in Export");
			jTableResponse = new JTableResponse("Error","Error in Export");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
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
	
	private TestManagementSystem getTestManagementSystem(ProductMaster product){
		
		if(product == null){
			return null;
		}
		Set<TestManagementSystem> testManagementSystems = product.getTestManagementSystems();
		if(testManagementSystems == null || testManagementSystems.isEmpty()){
			return null;				
		} else {
			for (TestManagementSystem testManagementSystem : testManagementSystems) {
				if (testManagementSystem.getToolIntagration() != null && testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(TAFConstants.TEST_MANAGEMENT_SYSTEM_HPQC)){
					return testManagementSystem;
				}
			}
			return null;
		} 
	}
	//Changes for UI for tool integration

	@RequestMapping(value="administration.defect.management.system.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listDefectManagementSystem(@RequestParam int productId) {
		log.debug("inside administration.defect.management.system.list");
		JTableResponse jTableResponse;
			try {
			List<DefectManagementSystem> defectManagementSystems=defectManagementService.listDefectManagementSystem(productId);
			List<JsonDefectManagementSystem> jsonDefectManagementSystem=new ArrayList<JsonDefectManagementSystem>();
			for(DefectManagementSystem defectManagementSystem: defectManagementSystems){
				jsonDefectManagementSystem.add(new JsonDefectManagementSystem(defectManagementSystem));
			}
	        jTableResponse = new JTableResponse("OK", jsonDefectManagementSystem,defectManagementService.getTotalRecordsOfDefectManagementSystem(productId));
	        defectManagementSystems = null;   
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	@RequestMapping(value="administration.defect.management.system.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addDefectManagementSystem(HttpServletRequest request,@ModelAttribute JsonDefectManagementSystem jsonDefectManagementSystem,JsonProductMaster jsonProductMaster, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		DefectManagementSystem defectManagementSystem=null;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {		
				defectManagementSystem=jsonDefectManagementSystem.getDefectManagementSystem();	
				String filter="productId="+jsonProductMaster.getProductId();
				String errorMessage=commonService.duplicateName(jsonDefectManagementSystem.getTitle(), "defect_management_system", "title", "Defect Management System",filter);
				if(errorMessage != null){
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION", errorMessage);
				}
				if(jsonDefectManagementSystem.getConnectionPassword() != null && !jsonDefectManagementSystem.getConnectionPassword().trim().equals("")){
					String encryptedPassword = passwordEncryptionService.encrypt(jsonDefectManagementSystem.getConnectionPassword());
					defectManagementSystem.setConnectionPassword(encryptedPassword);
				}
				ToolIntagrationMaster tim = toolIntagrationMasterService.getToolIntagrationMasterListById(jsonDefectManagementSystem.getToolIntagrationId());
				defectManagementService.addDefectManagementSystem(defectManagementSystem);
				//Entity Audition History //Addition		
				UserList userObj = (UserList)request.getSession().getAttribute("USER");
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_DEFECT_MANAGEMENT_SYSTEM, defectManagementSystem.getDefectManagementSystemId(),defectManagementSystem.getToolIntagration() != null ?defectManagementSystem.getToolIntagration().getName():"", userObj);
				List<JsonDefectManagementSystem> jsnDefectManagementSystem=new ArrayList<JsonDefectManagementSystem>();
				jsnDefectManagementSystem.add(jsonDefectManagementSystem);
				
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonDefectManagementSystem(defectManagementSystem));
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);	 
	            e.printStackTrace();
	        }
	        
        return jTableSingleResponse;
    }
	

	@RequestMapping(value="administration.defect.management.system.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateDefectManagementSystem(HttpServletRequest request,@ModelAttribute JsonDefectManagementSystem jsonDefectManagementSystem,BindingResult result) {
		JTableResponse jTableResponse;
		DefectManagementSystem defectManagementSystem=null;
		ProductMaster productMaster = null;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		
		try {
			UserList userObj = (UserList)request.getSession().getAttribute("USER");
				defectManagementSystem=jsonDefectManagementSystem.getDefectManagementSystem();
				String encryptedPassword ="";
				if(jsonDefectManagementSystem.getModifiedFieldTitle() != null && jsonDefectManagementSystem.getModifiedFieldTitle().equalsIgnoreCase("Password")){
					if(jsonDefectManagementSystem.getConnectionPassword() != null && !jsonDefectManagementSystem.getConnectionPassword().trim().equals("")){
						encryptedPassword = passwordEncryptionService.encrypt(jsonDefectManagementSystem.getConnectionPassword());
						defectManagementSystem.setConnectionPassword(encryptedPassword);
					}
				}
				productMaster = defectManagementSystem.getProductMaster();
				remarks = "Product :"+productMaster.getProductName()+", DefectManagementSystem :"+defectManagementSystem.getTitle();
				defectManagementService.updateDefectManagementSystem(defectManagementSystem);
				if(jsonDefectManagementSystem.getModifiedFieldTitle() != null && jsonDefectManagementSystem.getModifiedFieldTitle().equalsIgnoreCase("Password")){
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_DEFECT_MANAGEMENT_SYSTEM, defectManagementSystem.getDefectManagementSystemId(),defectManagementSystem.getToolIntagration().getName(),
							jsonDefectManagementSystem.getModifiedField(), jsonDefectManagementSystem.getModifiedFieldTitle(),
							jsonDefectManagementSystem.getOldFieldValue(), encryptedPassword, userObj, remarks);
					
				}else {
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_DEFECT_MANAGEMENT_SYSTEM, defectManagementSystem.getDefectManagementSystemId(),defectManagementSystem.getToolIntagration() != null ?defectManagementSystem.getToolIntagration().getName():"",
						jsonDefectManagementSystem.getModifiedField(), jsonDefectManagementSystem.getModifiedFieldTitle(),
						jsonDefectManagementSystem.getOldFieldValue(), jsonDefectManagementSystem.getModifiedFieldValue(), userObj, remarks);
				}
				List<DefectManagementSystem> defectManagementSystemList=new ArrayList<DefectManagementSystem>();
				defectManagementSystemList.add(defectManagementSystem);
				jTableResponse = new JTableResponse("OK",defectManagementSystemList,1);
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating Defect Management System!");
	            log.error("JSON ERROR", e);
	            e.printStackTrace();
	        } 
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.defect.management.system.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteDefectManagementSystem(@RequestParam int defectManagementSystemId) {
		JTableResponse jTableResponse;
		log.info("inside the administration.defect.management.system.delete");
		try {
				defectManagementService.deleteDefectManagementSystem(defectManagementSystemId);
				jTableResponse = new JTableResponse("OK");
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
            log.error("JSON ERROR", e);
        }
        return jTableResponse;
    }
	
	// defect Management System Mapping
	@RequestMapping(value="administration.defect.management.mapping.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listDefectManagementSystemMappings(@RequestParam int defectManagementSystemId) {
		log.debug("inside defect.management.mapping.list");
		JTableResponse jTableResponse;
		try {
			List<DefectManagementSystemMapping> defectManagementSystemMappings=defectManagementService.listDefectManagementSystemMapping(defectManagementSystemId);
			List<JsonDefectManagementSystemMapping> jsonDefectManagementSystemMappings=new ArrayList<JsonDefectManagementSystemMapping>();
			JsonDefectManagementSystemMapping jsonDefectManagementSystemMapping = null;
			for(DefectManagementSystemMapping defectManagementSystemMapping: defectManagementSystemMappings){
				jsonDefectManagementSystemMapping = new JsonDefectManagementSystemMapping(defectManagementSystemMapping);
				//Get the name of the entity which this mapping is mapped to
				log.info("Getting Mapped entity name for type : " + defectManagementSystemMapping.getMappingType() + " for ID : " + defectManagementSystemMapping.getMappedEntityIdInTAF());
				if (defectManagementSystemMapping.getMappedEntityIdInTAF() != null) {
					String entityName = defectManagementService.getEntityName(defectManagementSystemMapping.getMappingType(), defectManagementSystemMapping.getMappedEntityIdInTAF());
					log.info("Entity name : " + entityName);
					if (entityName != null) {
						jsonDefectManagementSystemMapping.setMappedEntityNameInTAFOptions(entityName);
						jsonDefectManagementSystemMapping.setMappedEntityNameInTAFId(entityName);
						
					} else {
						jsonDefectManagementSystemMapping.setMappedEntityNameInTAFOptions("");
					}
					
				} else {
					
					jsonDefectManagementSystemMapping.setMappedEntityNameInTAFOptions("");
				}
				
				
				jsonDefectManagementSystemMappings.add(jsonDefectManagementSystemMapping);
			}
		    jTableResponse = new JTableResponse("OK", jsonDefectManagementSystemMappings,jsonDefectManagementSystemMappings.size());
		    defectManagementSystemMappings = null;
		} catch (Exception e) {
		    jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		    log.error("JSON ERROR", e);
		    e.printStackTrace();
		}
		        
	    return jTableResponse;
	}

	@RequestMapping(value="administration.defect.management.mapping.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addDefectManagementSystemMapping(@ModelAttribute JsonDefectManagementSystemMapping jsonDefectManagementSystemMapping, BindingResult result) {
			JTableSingleResponse jTableSingleResponse;
			if(result.hasErrors()){
				jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
			}
			try {
				DefectManagementSystemMapping defectManagementSystemMapping= jsonDefectManagementSystemMapping.getDefectManagementSystemMapping();
				Integer entityId = defectManagementService.getEntityIdByEntityName(jsonDefectManagementSystemMapping.getMappingType(), jsonDefectManagementSystemMapping.getMappedEntityNameInTAFOptions(), jsonDefectManagementSystemMapping.getProductId());
				if (entityId == null) {

					jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to create mapping for chosen entity!");
			        return jTableSingleResponse;
				}
				defectManagementSystemMapping.setMappedEntityIdInTAF(entityId);
				defectManagementService.addDefectManagementSystemMapping(defectManagementSystemMapping);
				List<JsonDefectManagementSystemMapping> jsnDefectManagementSystemMapping=new ArrayList<JsonDefectManagementSystemMapping>();
				jsnDefectManagementSystemMapping.add(new JsonDefectManagementSystemMapping(defectManagementSystemMapping));
				jTableSingleResponse = new JTableSingleResponse("OK",jsnDefectManagementSystemMapping);
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
		            log.error("JSON ERROR", e);	 
		            e.printStackTrace();
		        }
		        
	        return jTableSingleResponse;
	    }
	@RequestMapping(value="administration.defect.management.mapping.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateDefectManagementSystemMapping(@ModelAttribute JsonDefectManagementSystemMapping jsonDefectManagementSystemMapping,BindingResult result) {
		JTableResponse jTableResponse;
		DefectManagementSystemMapping defectManagementSystemMappingFromUI=null;
		DefectManagementSystemMapping defectManagementSystemMappingFromDatabase=null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}	
		try {
			//Get the mapping from the database
			defectManagementSystemMappingFromUI=jsonDefectManagementSystemMapping.getDefectManagementSystemMapping();					
			defectManagementSystemMappingFromDatabase = defectManagementService.getByDefectManagementSystemMappingId((defectManagementSystemMappingFromUI.getDefectManagementSystemMappingId()));
			
			//Update the mapping from the UI with the missing details
			defectManagementSystemMappingFromUI.setMappingType(defectManagementSystemMappingFromDatabase.getMappingType());
			defectManagementSystemMappingFromUI.setMappedEntityIdInTAF(defectManagementSystemMappingFromDatabase.getMappedEntityIdInTAF());
			defectManagementSystemMappingFromUI.setDefectManagementSystem(defectManagementSystemMappingFromDatabase.getDefectManagementSystem());
			defectManagementSystemMappingFromUI.setProductMaster(defectManagementSystemMappingFromDatabase.getProductMaster());
			//Update the mapping and reload from database to get the complete updated data for the mapping
			defectManagementService.updateDefectManagementSystemMapping(defectManagementSystemMappingFromUI);

			defectManagementSystemMappingFromUI = defectManagementService.getByDefectManagementSystemMappingId(defectManagementSystemMappingFromUI.getDefectManagementSystemMappingId());
			
			List<JsonDefectManagementSystemMapping> jsonDefectManagementSystemMappingList=new ArrayList<JsonDefectManagementSystemMapping>();
			jsonDefectManagementSystemMappingList.add(new JsonDefectManagementSystemMapping(defectManagementSystemMappingFromUI));
			jTableResponse = new JTableResponse("OK",jsonDefectManagementSystemMappingList,1);
			defectManagementSystemMappingFromDatabase = null;
			defectManagementSystemMappingFromUI = null;
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating Defect Management System Mapping!");
	            log.error("JSON ERROR", e);
	            e.printStackTrace();
	    } 
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.defect.management.mapping.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteDefectManagementSystemMapping(@RequestParam int defecManagementSystemMappingId) {
		JTableResponse jTableResponse;
		log.info("inside the administration.defect.management.system.mapping.delete");
		try {
				defectManagementService.deleteDefectManagementSystemMapping(defecManagementSystemMappingId);
				jTableResponse = new JTableResponse("OK");
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
            log.error("JSON ERROR", e);
        }
        return jTableResponse;
    }
	
	//Test Management System
	@RequestMapping(value="administration.test.management.system.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestManagementSystem(@RequestParam int productId) {
		log.debug("inside administration.test.management.system.list");
		JTableResponse jTableResponse;
			try {
			List<TestManagementSystem> testManagementSystems=testManagementService.listTestManagementSystem(productId);
			List<JsonTestManagementSystem> jsonTestManagementSystem=new ArrayList<JsonTestManagementSystem>();
			for(TestManagementSystem testManagementSystem: testManagementSystems){
				jsonTestManagementSystem.add(new JsonTestManagementSystem(testManagementSystem));
			}
	        jTableResponse = new JTableResponse("OK", jsonTestManagementSystem,testManagementService.getTotalRecordsCount(productId));
	        testManagementSystems = null;   
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	@RequestMapping(value="administration.test.management.system.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestManagementSystem(HttpServletRequest request, @ModelAttribute JsonTestManagementSystem jsonTestManagementSystem,JsonProductMaster jsonProductMaster, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		TestManagementSystem testManagementSystem=null;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {	
				String msg = "";
				testManagementSystem=jsonTestManagementSystem.getTestManagementSystem();
				TestManagementSystem  isPrimaryTMS = testManagementService.getPrimaryTMSByProductId(jsonTestManagementSystem.getProductId());
				if(testManagementSystem.getIsPrimary() != null &&  testManagementSystem.getIsPrimary() == 1){
					if(isPrimaryTMS != null && isPrimaryTMS.getTestManagementSystemId() != 0 ){//if Primary TMS exists for product
						testManagementSystem.setIsPrimary(0); //Only one Primary TMS can exist
						msg = "Primary TMS already exist for this product, hence uncheck 'Is Primary'";
						jTableSingleResponse = new JTableSingleResponse("ERROR",msg);
						return jTableSingleResponse;
					}else{// if Primary TMS doesnot exists for product
						
					}
				}
				if(jsonTestManagementSystem.getConnectionPassword() != null && !jsonTestManagementSystem.getConnectionPassword().trim().equals("")){
					String encryptedPassword = passwordEncryptionService.encrypt(jsonTestManagementSystem.getConnectionPassword());
					testManagementSystem.setConnectionPassword(encryptedPassword);
				}
				//Logic for duplicate test managment title.
				String filter="productId="+jsonProductMaster.getProductId();
				String errorMessage=commonService.duplicateName(jsonTestManagementSystem.getTitle().trim(), "test_management_system", "title", "Test Management system",filter);
				if(errorMessage != null){
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION", errorMessage);
				}
				//Logic for verifying the same type of TMS mapped to the product
				ToolIntagrationMaster tim = toolIntagrationMasterService.getToolIntagrationMasterListById(jsonTestManagementSystem.getToolIntagrationId());
				boolean tmsExists = testManagementService.checkIfTMSExists(tim != null ?tim.getName():"",jsonTestManagementSystem.getProductId());
				testManagementSystem.setToolIntagration(tim);
				testManagementSystem.setTestSystemName(tim.getName());
				if(!tmsExists){
					testManagementService.addTestManagementSystem(testManagementSystem);
					//Entity Audition History //Addition		
					UserList userObj = (UserList)request.getSession().getAttribute("USER");
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_TEST_MANAGEMENT_SYSTEM, testManagementSystem.getTestManagementSystemId(), testManagementSystem.getToolIntagration().getName(), userObj);
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestManagementSystem(testManagementSystem) );
				} else {
					jTableSingleResponse = new JTableSingleResponse("ERROR",tim.getName().trim()+" already mapped to the product.");
				}
	
					
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding test Mangement System record!");
	            log.error("JSON ERROR", e);	 
	            e.printStackTrace();
	        }
	        
        return jTableSingleResponse;
    }
	

	@RequestMapping(value="administration.test.management.system.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateTestManagementSystem(HttpServletRequest request, @ModelAttribute JsonTestManagementSystem jsonTestManagementSystem,BindingResult result) {
		JTableResponse jTableResponse;
		TestManagementSystem testManagementSystem=null;
		ProductMaster productMaster = null;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		
		try {
			UserList userObj = (UserList)request.getSession().getAttribute("USER");
				testManagementSystem=jsonTestManagementSystem.getTestManagementSystem();
				if(jsonTestManagementSystem.getModifiedFieldTitle() != null && jsonTestManagementSystem.getModifiedFieldTitle().equalsIgnoreCase("Password")){
					if(jsonTestManagementSystem.getConnectionPassword() != null && !jsonTestManagementSystem.getConnectionPassword().trim().equals("")){
						String encryptedPassword = passwordEncryptionService.encrypt(jsonTestManagementSystem.getConnectionPassword());
						testManagementSystem.setConnectionPassword(encryptedPassword);
					}
				}
				productMaster = testManagementSystem.getProductMaster();
				remarks = "Product :"+productMaster.getProductName()+", TestManagementSystem :"+testManagementSystem.getTitle();
				testManagementService.updateTestManagementSystem(testManagementSystem);
				if(jsonTestManagementSystem.getModifiedField() != null && jsonTestManagementSystem.getModifiedField().equalsIgnoreCase("connectionPassword")){
					String modifiedValue = passwordEncryptionService.encrypt(jsonTestManagementSystem.getModifiedFieldValue());
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_TEST_MANAGEMENT_SYSTEM, testManagementSystem.getTestManagementSystemId(), testManagementSystem.getToolIntagration().getName(),
							jsonTestManagementSystem.getModifiedField(), jsonTestManagementSystem.getModifiedFieldTitle(),
							jsonTestManagementSystem.getOldFieldValue(), modifiedValue, userObj, remarks);
				} else {
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_TEST_MANAGEMENT_SYSTEM, testManagementSystem.getTestManagementSystemId(), testManagementSystem.getToolIntagration().getName(),
						jsonTestManagementSystem.getModifiedField(), jsonTestManagementSystem.getModifiedFieldTitle(),
						jsonTestManagementSystem.getOldFieldValue(), jsonTestManagementSystem.getModifiedFieldValue(), userObj, remarks);
				}
				List<TestManagementSystem> testManagementSystemList=new ArrayList<TestManagementSystem>();
				testManagementSystemList.add(testManagementSystem);
				jTableResponse = new JTableResponse("OK",testManagementSystemList,1);
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating test Management System!");
	            log.error("JSON ERROR", e);
	            e.printStackTrace();
	        } 
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.test.management.system.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteTestManagementSystem(@RequestParam int testManagementSystemId) {
		JTableResponse jTableResponse;
		log.info("inside the administration.defect.management.system.delete");
		try {
				testManagementService.deleteTestManagementSystem(testManagementSystemId);
				jTableResponse = new JTableResponse("OK");
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
            log.error("JSON ERROR", e);
        }
        return jTableResponse;
    }
	
	// test Management System Mapping
	@RequestMapping(value="administration.test.management.mapping.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestManagementSystemMappings(@RequestParam int testManagementSystemId) {
		log.debug("inside test.management.mapping.list");
		JTableResponse jTableResponse;
		try {
			List<TestManagementSystemMapping> testManagementSystemMappings=testManagementService.listTestManagementSystemMapping(testManagementSystemId);
			List<JsonTestManagementSystemMapping> jsonTestManagementSystemMappings=new ArrayList<JsonTestManagementSystemMapping>();
			JsonTestManagementSystemMapping jsonTestManagementSystemMapping = null;
			for(TestManagementSystemMapping testManagementSystemMapping: testManagementSystemMappings){
				jsonTestManagementSystemMapping = new JsonTestManagementSystemMapping(testManagementSystemMapping);
				//Get the name of the entity which this mapping is mapped to
				log.info("Getting Mapped entity name for type : " + testManagementSystemMapping.getMappingType() + " for ID : " + testManagementSystemMapping.getMappedEntityIdInTAF());
				if (testManagementSystemMapping.getMappedEntityIdInTAF() != null) {
					String entityName = defectManagementService.getEntityName(testManagementSystemMapping.getMappingType(), testManagementSystemMapping.getMappedEntityIdInTAF());
					log.info("Entity name : " + entityName);
					if (entityName != null) {
						jsonTestManagementSystemMapping.setMappedEntityNameInTAFOptions(entityName);
						jsonTestManagementSystemMapping.setMappedEntityNameInTAFId(entityName);
						
					} else {
						jsonTestManagementSystemMapping.setMappedEntityNameInTAFOptions("");
					}
					
				} else {
					
					jsonTestManagementSystemMapping.setMappedEntityNameInTAFOptions("");
				}
				
				
				jsonTestManagementSystemMappings.add(jsonTestManagementSystemMapping);
			}
		    jTableResponse = new JTableResponse("OK", jsonTestManagementSystemMappings,jsonTestManagementSystemMappings.size());
		    jsonTestManagementSystemMappings = null;
		} catch (Exception e) {
		    jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		    log.error("JSON ERROR", e);
		    e.printStackTrace();
		}
		        
	    return jTableResponse;
	}

	@RequestMapping(value="administration.test.management.mapping.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addTestManagementSystemMapping(@ModelAttribute JsonTestManagementSystemMapping jsonTestManagementSystemMapping, BindingResult result) {
			JTableSingleResponse jTableSingleResponse;
			if(result.hasErrors()){
				jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
			}
			try {
				TestManagementSystemMapping testManagementSystemMapping= jsonTestManagementSystemMapping.getTestManagementSystemMapping();
				Integer entityId = defectManagementService.getEntityIdByEntityName(jsonTestManagementSystemMapping.getMappingType(), jsonTestManagementSystemMapping.getMappedEntityNameInTAFOptions(), jsonTestManagementSystemMapping.getProductId());
				if (entityId == null) {

					jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to create mapping for chosen entity!");
			        return jTableSingleResponse;
				}
				log.info("inside the administration.test.management.mapping.add entity id is:"+entityId);
				testManagementSystemMapping.setMappedEntityIdInTAF(entityId);
				log.info("inside the administration.test.management.mapping.add entity id is:"+testManagementSystemMapping.getMappedEntityIdInTAF());
				testManagementService.addTestManagementSystemMapping(testManagementSystemMapping);
				List<JsonTestManagementSystemMapping> jsnTestManagementSystemMapping=new ArrayList<JsonTestManagementSystemMapping>();
				jsnTestManagementSystemMapping.add(new JsonTestManagementSystemMapping(testManagementSystemMapping));
				jTableSingleResponse = new JTableSingleResponse("OK",jsnTestManagementSystemMapping);
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
		            log.error("JSON ERROR", e);	 
		            e.printStackTrace();
		        }
		        
	        return jTableSingleResponse;
	    }
	@RequestMapping(value="administration.test.management.mapping.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateTestManagementSystemMapping(@ModelAttribute JsonTestManagementSystemMapping jsonTestManagementSystemMapping,BindingResult result) {
		JTableResponse jTableResponse;
		TestManagementSystemMapping testManagementSystemMappingFromUI=null;
		TestManagementSystemMapping testManagementSystemMappingFromDatabase=null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}	
		try {
			//Get the mapping from the database
			testManagementSystemMappingFromUI=jsonTestManagementSystemMapping.getTestManagementSystemMapping();
			testManagementSystemMappingFromDatabase = testManagementService.getByTestManagementSystemMappingId(testManagementSystemMappingFromUI.getTestManagementSystemMappingId());
			//Update the mapping from the UI with the missing details
			testManagementSystemMappingFromUI.setMappingType(testManagementSystemMappingFromDatabase.getMappingType());
			testManagementSystemMappingFromUI.setMappedEntityIdInTAF(testManagementSystemMappingFromDatabase.getMappedEntityIdInTAF());
			testManagementSystemMappingFromUI.setProductMaster(testManagementSystemMappingFromDatabase.getProductMaster());
			testManagementSystemMappingFromUI.setTestManagementSystem(testManagementSystemMappingFromDatabase.getTestManagementSystem());
			//Update the mapping and reload from database to get the complete updated data for the mapping
			testManagementService.updateTestManagementSystemMapping(testManagementSystemMappingFromUI);
			testManagementSystemMappingFromUI= testManagementService.getByTestManagementSystemMappingId(testManagementSystemMappingFromUI.getTestManagementSystemMappingId());
			
			List<JsonTestManagementSystemMapping> jsonTestManagementSystemMappingList=new ArrayList<JsonTestManagementSystemMapping>();
			jsonTestManagementSystemMappingList.add(new JsonTestManagementSystemMapping(testManagementSystemMappingFromUI));
			jTableResponse = new JTableResponse("OK",jsonTestManagementSystemMappingList,1);
			testManagementSystemMappingFromDatabase = null;
			testManagementSystemMappingFromUI = null;
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating Defect Management System Mapping!");
	            log.error("JSON ERROR", e);
	            e.printStackTrace();
	    } 
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.test.management.mapping.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteTestManagementSystemMapping(@RequestParam int testManagementSystemMappingId) {
		JTableResponse jTableResponse;
		log.info("inside the administration.test.management.system.mapping.delete");
		try {
				testManagementService.deleteTestManagementSystemMapping(testManagementSystemMappingId);
				jTableResponse = new JTableResponse("OK");
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
            log.error("JSON ERROR", e);
        }
        return jTableResponse;
    }
	
	/**
	 * The method is used for checking out scripts from Test management system
	 *  Based on the test script source the scripts will be checked out from different locations.
	 * 
	 * @param product
	 * @param testRunConfigChildId
	 * @param request
	 */
    public void checkOutScriptsOTA(ProductMaster product, TestRunPlan testRunPlan , HttpServletRequest request,WorkPackage workPackage) {

		ConnectorHPQC hpqcConnector=null;
		String scriptsCheckoutProperty = "hpqc.scriptsCheckoutPath";
		String scriptsCheckoutPath = "";
		TestSuiteList testSuite = null;
		Set<TestSuiteList> testSuites = null;

		String testSuiteCode = "";
		String testScriptSource = "";
		
		log.info("inside the tools.hpqc.attachments.download");
		try {
						
			testSuites =  testRunPlan.getTestSuiteLists();
			
			
			for (TestSuiteList testSuiteList : testSuites) {
				testSuite=testSuiteList;
			
				testScriptSource = testSuite.getTestScriptSource();
				//Added for Hudson
				if(TAFConstants.TEST_SCRIPT_SOURCE_TAF.equalsIgnoreCase(testScriptSource) || TAFConstants.TEST_SCRIPT_SOURCE_HUDSON.equalsIgnoreCase(testScriptSource)) {
					log.info("TestScriptSource:" + testScriptSource);
				}
				//Added for TestScript source from TestResource
				if(TAFConstants.TEST_SCRIPT_SOURCE_HPQC.equalsIgnoreCase(testScriptSource) ||
						TAFConstants.TEST_SCRIPT_SOURCE_HPQC_TEST_PLAN.equalsIgnoreCase(testSuite.getTestScriptSource())||
						TAFConstants.TEST_SCRIPT_SOURCE_HPQC_TEST_RESOURCES.equalsIgnoreCase(testSuite.getTestScriptSource())) {
					
					//Getting HPQC Connector 
					if(hpqcConnector==null){
						hpqcConnector = getHPQCConnector(product, request);
						if (hpqcConnector == null) {
							log.error("Unable to establish connection to HPQC and checkout scripts. Check the mappings");			
						}
					}
					
					testSuiteCode = testSuite.getTestSuiteCode();
					//Constructing Dynamic Script Name
					String productName = product.getProductName().replace(" ", "_");
					int testRunNo = workPackage.getWorkPackageId();
					String scriptPath = productName + "_TRCC_"+testRunPlan.getTestRunPlanId()+"_TR_"+ String.valueOf(testRunNo)+"_TS_"+testSuiteCode;
					log.info("Path Variable:"+ scriptPath);
					
					//Script upload to TAF Server
					String uploadPath =  request.getServletContext().getRealPath("/")+"TestScripts\\"+scriptPath;
					if (hpqcConnector != null){
						InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\hpqc.properties");
						scriptsCheckoutPath = getHPQCProperties(fis, scriptsCheckoutProperty);
					}
				
					//Included test script source for differentiating scripts sources: TestPlan / TestResources
					tafTestManagementSystemIntegrator.processTestScriptsOTA(hpqcConnector, testSuite, scriptPath, scriptsCheckoutPath,uploadPath, testScriptSource);
				}	
			}
		}catch (Exception e) {           
			e.printStackTrace();
            log.error("Error in ScriptsCheckout from HPQC", e);
        }
       
    }

    
    public String getHPQCProperties(InputStream fis, String propertyName){
    	
    	Properties hpqcProperties = new Properties();
    	String propertyValue = "";
    	try {
			hpqcProperties.load(fis);
			if( hpqcProperties.get(propertyName) != null &&  hpqcProperties.get(propertyName) != ""){
				propertyValue = hpqcProperties.get(propertyName).toString().trim();
			}
		} catch (IOException ioe) {
			log.error("Exception in loading hpqcProperties:", ioe);
		}
    	
		return propertyValue;
    	
    }
    
    /**
     * Checkout scripts for Parent Configuration.
     * 
     * @param testRunConfigParentId
     * @param request
     */
    public void checkOutScriptsForParentConfig( int testRunConfigParentId, HttpServletRequest request){
    	
    	ProductMaster product = null;
    	TestRunConfigurationParent testRunConfigurationParent = null;
    	Set<TestRunConfigurationChild> testRunConfigurationChildList = null;
    	try{
    		testRunConfigurationParent =testRunConfigurationService.getByTestRunConfigurationParentId(testRunConfigParentId);
    		testRunConfigurationChildList = testRunConfigurationParent.getTestRunConfigurationChilds();
	    	if (testRunConfigurationChildList == null || testRunConfigurationChildList.isEmpty()) {
				log.debug("No child configurations found for TestRunConfigurationParent : " + testRunConfigParentId);			
			}
	    	for (TestRunConfigurationChild testRunConfigurationChild : testRunConfigurationChildList) {
	    		
				product = testRunConfigurationParent.getProductMaster();
	    	}
    	} catch(Exception excp){
    		log.error("Exception in checkout scripts from Parent Config:" ,excp);
    	}
    } 
    

    @RequestMapping(value="test.import.testcases.rest", produces="application/json" )
	public @ResponseBody JTableResponse testDataimportRest(@RequestParam Integer productId, HttpServletRequest request,HttpServletResponse response,@RequestParam String tmsIdValues) {
		log.debug("test.import");
		JTableResponse jTableResponse =null;
		String[] tmsIds = null;
		try{
			if(tmsIdValues.contains(",")){
				tmsIds = tmsIdValues.split(",");
			} else {
				tmsIds = new String[]{tmsIdValues.trim()};
			}
			TestManagementSystem testManagementSystem = null;
			ProductMaster product = null;
			String tfsProductName = null;
			String importCompleteStatus = null;
			for (String tms : tmsIds) {
				testManagementSystem=testManagementService.getTMSByProduct(Integer.parseInt(tms),productId);	
				Set<TestManagementSystemMapping> testManagementMappings = testManagementSystem.getTestManagementSystemMappings();
				if(testManagementMappings != null && !testManagementMappings.isEmpty()){
					for(TestManagementSystemMapping testManagementSystemMapping : testManagementMappings){
						if(testManagementSystemMapping.getMappingType().equals(TAFConstants.ENTITY_PRODUCT)){
							tfsProductName = testManagementSystemMapping.getMappingValue();
						}
					}
				}
				log.info("TestSystemName:"+testManagementSystem.getToolIntagration().getName());
				log.info("Property1:"+testManagementSystem.getConnectionProperty1());
				log.info("Property2:"+testManagementSystem.getConnectionProperty2());
				log.info("Property3:"+testManagementSystem.getConnectionProperty3());
				log.info("Property4:"+testManagementSystem.getConnectionProperty4());
				log.info("Property5:"+testManagementSystem.getConnectionProperty5());
				product=testManagementSystem.getProductMaster();
				if(testManagementSystem.getToolIntagration() != null && testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.TFS_TOOL)){
					if(testManagementSystem.getConnectionProperty2().trim().equals(" ") || testManagementSystem.getConnectionProperty2().trim() == " " || testManagementSystem.getConnectionProperty2().trim().equals("") || testManagementSystem.getConnectionProperty2().trim() == "" || testManagementSystem.getConnectionProperty2()== null){
						jTableResponse = new JTableResponse("Error", "Unable to establish connection to TFS. Check the mappings");
						return jTableResponse;
					}
					
					if(testManagementSystem.getConnectionProperty3().trim().equals(" ") || testManagementSystem.getConnectionProperty3().trim() == " " || testManagementSystem.getConnectionProperty3().trim().equals("") || testManagementSystem.getConnectionProperty3().trim() == "" || testManagementSystem.getConnectionProperty3()== null){
						jTableResponse = new JTableResponse("Error", "Unable to establish connection to TFS. Check the mappings");
						return jTableResponse;
					}
					
					if(!(testManagementSystem.getConnectionUri().trim().startsWith("http://") || testManagementSystem.getConnectionUri().trim().startsWith("https://"))){
						jTableResponse = new JTableResponse("Error", "Invalid TFS URL mapping.");
						return jTableResponse;
					}
					
					String tfsProjectCollectionUrl = testManagementSystem.getConnectionUri().trim();
					
					if(tfsProjectCollectionUrl != null && tfsProjectCollectionUrl !=""){						
						TFSIntegrator tfsConnector = getTFSConnector(tfsProjectCollectionUrl);	
						TFSConnector.USERNAME=testManagementSystem.getConnectionUserName().trim();
						TFSConnector.PASSWORD=passwordEncryptionService.decrypt(testManagementSystem.getConnectionPassword().trim());
						TFSTeamProjectCollection projectCollection = getTFSProjectCollection(tfsConnector, tfsProjectCollectionUrl);
						importCompleteStatus =  tafTestManagementSystemIntegrator.importTestCasesFromTFS(productId, Integer.valueOf(testManagementSystem.getConnectionProperty3().trim()), Integer.valueOf(testManagementSystem.getConnectionProperty2().trim()), testManagementSystem.getConnectionUri().trim(), tfsProductName,testManagementSystem.getToolIntagration().getName(), tfsConnector, projectCollection);
					}  
				} else if(testManagementSystem.getToolIntagration() != null && testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
					ConnectorHPQCRest hpqcConnector;
					hpqcConnector =  getHPQCConnectorRest(product, request);
					String downloadPath = request.getServletContext().getRealPath("/")+"TestScripts\\";
					if (hpqcConnector == null) {
						jTableResponse = new JTableResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
						return jTableResponse;
					}					
					if(testCaseFolderById != null){
						testCaseFolderById = testCaseFolderById.replaceAll("\\s+","").trim();
					}
					if(testSetFolderById!=null){
						testSetFolderById =testSetFolderById.replaceAll("\\s+","").trim();
					}
					if(testSetFolderByName!=null){
						testSetFolderByName = testSetFolderByName.replaceAll("\\s+","").trim();
					}
					
					importCompleteStatus =  tafTestManagementSystemIntegrator.importTestCasesRest(productId, hpqcConnector, IDPAConstants.HPQC_TOOL,testCaseFolderById.replaceAll("\\s+","").trim(),testSetFolderById.replaceAll("\\s+","").trim(),testSetFolderByName.replaceAll("\\s+","").trim(),downloadPath.trim());					
					ConnectorHPQCRest.logout(hpqcConnector); 
				}
				else if(testManagementSystem.getToolIntagration() != null && testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.SVN_TOOL)){
					String scriptPath = "http://" + request.getServerName()+":" + request.getServerPort()+request.getContextPath()+ File.separator +"TestScripts"+File.separator;									
					log.info("Script SVN download path : "+scriptPath);
					String svnConnectUri = testManagementSystem.getConnectionUri().trim();
					if(svnConnectUri != null && svnConnectUri != ""){
						String svnusername = testManagementSystem.getConnectionUserName().trim();
						String svnpassword = passwordEncryptionService.decrypt(testManagementSystem.getConnectionPassword().trim());
						try{
							new ConnectorCredentials(svnConnectUri,svnusername,svnpassword,scriptPath,tms);
							if(product!=null){
								Set<TestSuiteList> tsl = testSuiteConfigurationService.getTestSuiteByProductId(product.getProductId());
								if(tsl !=null && tsl.size() > 0){
									TestSuiteList tt = null;
									for(TestSuiteList ts : tsl){
										tt = ts;
										break;
									}
									if(tt !=null) {									
										tt.setTestSuiteScriptFileLocation(scriptPath+"svn"+tms+ File.separator);
										ExecutionTypeMaster executionTypeMasterFromUI = executionTypeMasterService.getExecutionTypeByExecutionTypeId(3);
										tt.setExecutionTypeMaster(executionTypeMasterFromUI);
										ScriptTypeMaster scriptTypeMaster = new ScriptTypeMaster();
										scriptTypeMaster.setScriptType("eDAT");
										tt.setScriptTypeMaster(scriptTypeMaster);
										testSuiteConfigurationService.updateTestSuite(tt);
									} else {
										importCompleteStatus = null;
									}
								} else {
									TestSuiteList newTestSuiteToAdd = new TestSuiteList();
									newTestSuiteToAdd.setTestSuiteName("TMF");
									newTestSuiteToAdd.setProductMaster(product);	
									newTestSuiteToAdd.setTestScriptSource(IDPAConstants.TEST_SCRIPT_SOURCE_ILCM);
									ExecutionTypeMaster executionTypeMasterFromUI = executionTypeMasterService.getExecutionTypeByExecutionTypeId(3);
									newTestSuiteToAdd.setExecutionTypeMaster(executionTypeMasterFromUI);
									ScriptTypeMaster scriptTypeMaster = new ScriptTypeMaster();
									scriptTypeMaster.setScriptType("eDAT");
									newTestSuiteToAdd.setScriptTypeMaster(scriptTypeMaster);
									testSuiteConfigurationService.addTestSuite(newTestSuiteToAdd);
								}
								importCompleteStatus = "Success";
							} else {
								importCompleteStatus = null;
							}							
						} catch(Exception e){
							importCompleteStatus = null;
						}
					}					
				}
				if(importCompleteStatus != null && importCompleteStatus != ""){
					log.info("Import Test Cases Completed.");
					jTableResponse = new JTableResponse("SUCCESS","Import Test Cases Completed.");
				} else {
					log.info("Import Test Cases Failed.");
					jTableResponse = new JTableResponse("Error","Import Test Cases Failed.");
				}
			}
		}catch(Exception e){
			log.error("Error" , e);
		}
		return jTableResponse;
	}
	
	public ConnectorHPQCRest getHPQCConnectorRest(ProductMaster product, HttpServletRequest request){

		ConnectorHPQCRest hpqcConnector = null;
		TestManagementSystem hpqcTestManagementSystem = null;
		
		try {
			log.info("Getting HPQC connection for product : " + product.getProductId());
			String testCaseSource = IDPAConstants.HPQC_TOOL;
			
			Set<TestManagementSystem> testManagementSystems = product.getTestManagementSystems();
			if(testManagementSystems == null || testManagementSystems.isEmpty()){
				log.info("No test Management system mapped to product. Hence using default properties file for connection.");
				try{
					InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\hpqc.properties");	
					hpqcConnector =  new ConnectorHPQCRest(fis);
					
				} catch(Exception e){
					log.error("Unable to get default HPQC connection properties file.Hence aborting connection",e);
					return null;
				}
			} else {
				for (TestManagementSystem testManagementSystem : testManagementSystems) {
					if (testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.HPQC_TOOL) && testManagementSystem.getIsPrimary() == 1){
						hpqcTestManagementSystem = testManagementSystem;						
					}
				}
				if(hpqcTestManagementSystem != null){
					Set<TestManagementSystemMapping> testManagementMappings = hpqcTestManagementSystem.getTestManagementSystemMappings();
					if(testManagementMappings != null && !testManagementMappings.isEmpty()){
						String domainName ="";
						String userName = "";
						String passsword = "";				
						String url ="";
						String encyrptedPasssword = "";
						
						if(hpqcTestManagementSystem.getConnectionPassword()!= null){
							encyrptedPasssword = hpqcTestManagementSystem.getConnectionPassword().trim();
							passsword = passwordEncryptionService.decrypt(encyrptedPasssword);
						}
						
						if(hpqcTestManagementSystem.getConnectionUri()!=null){
							if(!StringUtils.isBlank(hpqcTestManagementSystem.getConnectionUri())){
								url = hpqcTestManagementSystem.getConnectionUri().trim();
							} else {
								url = "";
							}
						}
						
						if(hpqcTestManagementSystem.getConnectionUserName()!=null){
							if(!StringUtils.isBlank(hpqcTestManagementSystem.getConnectionUserName())){
								userName = hpqcTestManagementSystem.getConnectionUserName().trim();
							} else {
								userName = "";
							}
						}
						
						if(hpqcTestManagementSystem.getConnectionProperty1()!=null){
							if(!StringUtils.isBlank(hpqcTestManagementSystem.getConnectionProperty1())){
								domainName = hpqcTestManagementSystem.getConnectionProperty1().trim();
							} else {
								domainName = "";
							}
						}
						//including TestCase/TestSet Filters						
						if(hpqcTestManagementSystem.getConnectionProperty2()!=null){
							if(!StringUtils.isBlank(hpqcTestManagementSystem.getConnectionProperty2())){
								testSetFolderById =hpqcTestManagementSystem.getConnectionProperty2().trim();
							} else {
								testSetFolderById ="";
							}
						} else {
							testSetFolderById ="";
						}
						if(hpqcTestManagementSystem.getConnectionProperty3()!=null){
							if(!StringUtils.isBlank(hpqcTestManagementSystem.getConnectionProperty3())){
								testSetFolderByName=hpqcTestManagementSystem.getConnectionProperty3().trim();//Changes for Bug 792 - Adding TestSetFilter by name
							} else {
								testSetFolderByName= "";
							}
						} else {
							testSetFolderByName = "";
						}
						
						if(hpqcTestManagementSystem.getConnectionProperty4()!=null){
							if(!StringUtils.isBlank(hpqcTestManagementSystem.getConnectionProperty4())){
								testCaseFolderById = hpqcTestManagementSystem.getConnectionProperty4().trim();
							} else {
								testCaseFolderById ="";
							}
						} else {
							testCaseFolderById = "";
						}
						
						if(hpqcTestManagementSystem.getConnectionProperty5()!=null){
							if(!StringUtils.isBlank(hpqcTestManagementSystem.getConnectionProperty5())){
								testResourceFolderById=hpqcTestManagementSystem.getConnectionProperty5().trim();
							} else {
								testResourceFolderById="";
							}
						} else {
							testResourceFolderById="";
						}
						
						String hpqcProductName = "";					
						
						for(TestManagementSystemMapping testManagementSystemMapping : testManagementMappings){
							if(testManagementSystemMapping.getMappingType().equals(TAFConstants.ENTITY_PRODUCT)){
								if(!StringUtils.isBlank(testManagementSystemMapping.getMappingValue())){
									hpqcProductName = testManagementSystemMapping.getMappingValue().trim();
								} else {
									hpqcProductName = "";
								}
							}
						}
						log.info("Parameters ... hpqcProductName : url : userName : passsword -> " + hpqcProductName + " : " + url + " : " + userName + " : " +  encyrptedPasssword);
						if(hpqcProductName == null || url == null || userName == null || passsword == null){
							log.info("Connection Parameters missing. hpqcProductName : url : userName : passsword -> " + hpqcProductName + " : " + url + " : " + userName + " : " +  encyrptedPasssword);
							return null;
						} else {
							if (domainName == null || domainName.isEmpty())
								domainName = "default";
							hpqcConnector= new ConnectorHPQCRest();						
							hpqcConnector=hpqcConnector.createConnection(url,userName,passsword,domainName,hpqcProductName);
							log.info("Obtained HPQC connection from mappings. hpqcProductName : url : userName : passsword -> " + hpqcProductName + " : " + url + " : " + userName + " : " +  encyrptedPasssword);
							return hpqcConnector;
						}
					}
				} else {
					log.info("No HPQC Management system mapped to product. Hence using default properties file for connection.");
					try{
						InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\hpqc.properties");
						hpqcConnector =  new ConnectorHPQCRest(fis);
						
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

	public ConnectorHPQCRest getTMSConnectorForPrimary(TestManagementSystem testManagementSystem,HttpServletRequest request){

		ConnectorHPQCRest hpqcConnector = null;
		TestManagementSystem hpqcTestManagementSystem = null;
		
		try {
			String testCaseSource = IDPAConstants.HPQC_TOOL;
			
			
			if(testManagementSystem == null ){
				log.info("No test Management system is set as Primary. ");
				try{
					InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\hpqc.properties");	
					hpqcConnector =  new ConnectorHPQCRest(fis);
				} catch(Exception e){
					log.error("Unable to get default HPQC connection properties file.Hence aborting connection",e);
					return null;
				}
				
			} else {
				if (testManagementSystem.getToolIntagration() !=null && testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
					hpqcTestManagementSystem = testManagementSystem;						
				
					Set<TestManagementSystemMapping> testManagementMappings = hpqcTestManagementSystem.getTestManagementSystemMappings();
					if(testManagementMappings != null && !testManagementMappings.isEmpty()){
						
						String userName = hpqcTestManagementSystem.getConnectionUserName().trim();
						String encyrptedPasssword = hpqcTestManagementSystem.getConnectionPassword();
						String passsword = passwordEncryptionService.decrypt(encyrptedPasssword);
						String url =hpqcTestManagementSystem.getConnectionUri().trim();
						String domainName =hpqcTestManagementSystem.getConnectionProperty1().trim();
						//including TestCase/TestSet Filters
						testSetFolderById =hpqcTestManagementSystem.getConnectionProperty2().trim();
						testCaseFolderById =hpqcTestManagementSystem.getConnectionProperty3().trim();
						testResourceFolderById=hpqcTestManagementSystem.getConnectionProperty4().trim();
						
						String hpqcProductName = hpqcTestManagementSystem.getProductMaster().getProductName();
						for(TestManagementSystemMapping testManagementSystemMapping : testManagementMappings){
							if(testManagementSystemMapping.getMappingType().equals(TAFConstants.ENTITY_PRODUCT)){
								hpqcProductName = testManagementSystemMapping.getMappingValue();
							}
						}
						log.info("Parameters ... hpqcProductName : url : userName : passsword -> " + hpqcProductName + " : " + url + " : " + userName + " : " +  encyrptedPasssword);
						if(hpqcProductName == null || url == null || userName == null || passsword == null){
							log.info("Connection Parameters missing. hpqcProductName : url : userName : passsword -> " + hpqcProductName + " : " + url + " : " + userName + " : " +  encyrptedPasssword);
							return null;
						} else {
							if (domainName == null || domainName.isEmpty())
								domainName = "default";
							hpqcConnector= new ConnectorHPQCRest();						
							hpqcConnector=hpqcConnector.createConnection(url,userName,passsword,domainName,hpqcProductName);
							log.info("Obtained HPQC connection from mappings. hpqcProductName : url : userName : passsword -> " + hpqcProductName + " : " + url + " : " + userName + " : " +  encyrptedPasssword);
							return hpqcConnector;
						}
					} else {
						log.info("No HPQC Management system mapped to product. Hence using default properties file for connection.");
						try{
							InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\hpqc.properties");
							hpqcConnector =  new ConnectorHPQCRest(fis);
							
							return hpqcConnector;
							
						} catch(Exception e){
							log.error("Unable to get default HPQC connection properties file.Hence aborting connection",e);
							return null;
						}
					}
				}
			}
		} catch(Exception e){
			log.error("Unable to get HPQC Connection.", e);
			return null;
		}
		return hpqcConnector;

	}

	
	/**
	 * The method is used for checking out scripts from Test management system using Rest implementation of HPQC
	 *  Based on the test script source the scripts will be checked out from different locations.
	 * 
	 * @param product
	 * @param testRunConfigChildId
	 * @param request
	 */
    public void checkOutScriptsRest(ProductMaster product, TestRunPlan testRunPlan , HttpServletRequest request,WorkPackage workPackage,TestManagementSystem testManagementSystem) {

    	ConnectorHPQCRest hpqcConnector=null;
		String scriptsCheckoutProperty = "hpqc.scriptsCheckoutPath";
		String scriptsCheckoutPath = "";
		TestSuiteList testSuite = null;
		Set<TestSuiteList> testSuites = null;

		String testSuiteCode = "";
		String testScriptSource = "";
		
		log.info("inside the tools.hpqc.attachments.download");
		try {
						
			testSuites =  testRunPlan.getTestSuiteLists();
			for (TestSuiteList testSuiteList : testSuites) {
				testSuite=testSuiteList;
			
				testScriptSource = testSuite.getTestScriptSource();
				//Added for Hudson
				if(IDPAConstants.TEST_SCRIPT_SOURCE_ILCM.equalsIgnoreCase(testScriptSource) || TAFConstants.TEST_SCRIPT_SOURCE_HUDSON.equalsIgnoreCase(testScriptSource)) {
					log.info("TestScriptSource:" + testScriptSource);
				}
				//Added for TestScript source from TestResource
				if(TAFConstants.TEST_SCRIPT_SOURCE_HPQC.equalsIgnoreCase(testScriptSource) ||
						TAFConstants.TEST_SCRIPT_SOURCE_HPQC_TEST_PLAN.equalsIgnoreCase(testSuite.getTestScriptSource())||
						TAFConstants.TEST_SCRIPT_SOURCE_HPQC_TEST_RESOURCES.equalsIgnoreCase(testSuite.getTestScriptSource())) {
					
					//Getting HPQC Connector 
					if(hpqcConnector==null){
						hpqcConnector = getTMSConnectorForPrimary(testManagementSystem,request);
						if (hpqcConnector == null) {
							log.error("Unable to establish connection to HPQC and checkout scripts. Check the mappings");			
						}
					}
					
					testSuiteCode = testSuite.getTestSuiteCode();
					//Constructing Dynamic Script Name
					String productName = product.getProductName().replace(" ", "_");
					int testRunNo = workPackage.getWorkPackageId();
					String scriptPath = productName + "_TRCC_"+testRunPlan.getTestRunPlanId()+"_TR_"+ String.valueOf(testRunNo)+"_TS_"+testSuiteCode;
					log.info("Path Variable:"+ scriptPath);
					
					//Script upload to TAF Server
					String uploadPath =  request.getServletContext().getRealPath("/")+"TestScripts\\"+scriptPath;
					if (hpqcConnector != null){
						InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\hpqc.properties");
						scriptsCheckoutPath = getHPQCProperties(fis, scriptsCheckoutProperty);
					}
				
					//Included test script source for differentiating scripts sources: TestPlan / TestResources

					tafTestManagementSystemIntegrator = TAFTestManagementSystemIntegrationFactory.getTestManagementSystemIntegrator("HPQC");
					tafTestManagementSystemIntegrator.processTestScriptsRest(hpqcConnector, testSuite, scriptPath, scriptsCheckoutPath,uploadPath, testScriptSource,testResourceFolderById);
			
				}	
			}
		}catch (Exception e) {           
			e.printStackTrace();
            log.error("Error in ScriptsCheckout from HPQC", e);
        }
    }
    
    
    
	
	@RequestMapping(value="test.export.testresults.rest", produces="application/json" )
	public @ResponseBody JTableResponse testResultsExportRest(@RequestParam Integer workpackageId, HttpServletRequest request,HttpServletResponse response,@RequestParam String tmsIdValues) {
		log.debug("test.export");
		JTableResponse jTableResponse = null;
		ConnectorHPQCRest hpqcConnector = null;
		try {
			log.info("testRunListId Id input:" + workpackageId);
			
			WorkPackage workPackage=workPackageService.getWorkPackageById(workpackageId);
			ProductMaster product = workPackage.getProductBuild().getProductVersion().getProductMaster();
			TestManagementSystem testManagementSystem=null;
			
			String[] tmsIds = tmsIdValues.split(",");
			for (String tms : tmsIds) {
				testManagementSystem=testManagementService.getTMSByProduct(Integer.parseInt(tms),product.getProductId());
				
				if(testManagementSystem.getToolIntagration() != null && testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
					hpqcConnector =  getHPQCConnectorRest(product, request);
					if (hpqcConnector == null) {
						jTableResponse = new JTableResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
						return jTableResponse;
					}
					Set<TestRunJob> testRunJobs = workPackage.getTestRunJobSet();
					String importCompleteStatus = "";
					for (TestRunJob testRunJob : testRunJobs) {
						 importCompleteStatus =  tafTestManagementSystemIntegrator.exportTestExecutionResultsRest(testRunJob, hpqcConnector,testManagementSystem);
					}
					if(importCompleteStatus != null){
						log.info("Export TestResults completed.");
						jTableResponse = new JTableResponse("Ok","Export TestResults completed.");
					} else{
						log.info("Export TestResults completed.");
						jTableResponse = new JTableResponse("Ok","Export TestResults completed");
					}
				}else{
					jTableResponse = new JTableResponse("Error","Export TestResults is not available for this system:"+testManagementSystem.getToolIntagration() != null ?testManagementSystem.getToolIntagration().getName():"");
				}
			}
			
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in Export");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	//Changes for Rest implementation of HPQC
	
	@RequestMapping(value="test.defect.export.hpqc.rest", produces="application/json" )
	public @ResponseBody JTableResponse defectsExportHPQCRestWorkpackage(@RequestParam Integer workpackageId,@RequestParam Integer defectManagementSystemId, HttpServletRequest request,HttpServletResponse response) {
		log.debug("ttest.defect.export.hpqc.Rest");
		JTableResponse jTableResponse;
		ConnectorHPQCRest hpqcConnector = null;
		try {
			log.info("workpackageId Id input:" + workpackageId);
			WorkPackage workPackage= workPackageService.getWorkPackageById(workpackageId);
			
			ProductMaster product = workPackage.getProductBuild().getProductVersion().getProductMaster();
			hpqcConnector =  getHPQCDefectManagementSystemConnectorRest(product.getProductId(), request);
			if (hpqcConnector == null) {
				jTableResponse = new JTableResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
				return jTableResponse;
			}
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans= workPackage.getWorkPackageTestCaseExecutionPlan();
			List<TestExecutionResultBugList> testExecutionResultBugLists = new ArrayList<TestExecutionResultBugList>();
			for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
				testExecutionResultBugLists.addAll(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestExecutionResultBugListSet());
			}
			
			String importCompleteStatus =  tafTestManagementSystemIntegrator.exportTestExecutionDefectsRest(testExecutionResultBugLists, hpqcConnector,defectManagementSystemId);
			hpqcConnector.logout(hpqcConnector);
			if(importCompleteStatus != null){
				log.info("Export TestDefects completed.");
				jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
			} else{
				log.info(importCompleteStatus);
				jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in Export");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
		
	
	 /**
		 * Export of defects to HPQC using Rest services
		 * @param testRunListId
		 * @param request
		 * @param response
		 * @return
		 */
	@RequestMapping(value="test.defect.export.hpqc.rest.testrunjob", produces="application/json" )
		public JTableResponse defectsExportHPQCTestRunJob(int testRunJobId,  HttpServletRequest request,@RequestParam int defectManagementSystemId) {
			log.debug("ttest.defect.export.hpqc.rest");
			JTableResponse jTableResponse;
			TestRunList testRunList = null;
			ConnectorHPQCRest hpqcConnector = null;
			String importCompleteStatus = "";
			try {
				log.info("testRunConfigurationChildId Id input:" + testRunJobId);
				
				TestRunJob testRunJob=workPackageService.getTestRunJobById(testRunJobId);
				
				
				
				ProductMaster product = testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster();
				hpqcConnector =  getHPQCDefectManagementSystemConnectorRest(product.getProductId(), request);
				if (hpqcConnector == null) {
					jTableResponse = new JTableResponse("Error", "Unable to establish connection to HPQC. Check the mappings");				
				}
				
				List<TestExecutionResultBugList> bugs= new ArrayList<TestExecutionResultBugList>();
				Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans=testRunJob.getWorkPackageTestCaseExecutionPlans();
				for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
					bugs.addAll(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestExecutionResultBugListSet());
				}
			    
				importCompleteStatus =  tafTestManagementSystemIntegrator.exportTestExecutionDefectsRest(bugs, hpqcConnector,defectManagementSystemId);
			    
				hpqcConnector.logout(hpqcConnector);
				if(importCompleteStatus != null){
					log.info("Export TestDefects completed.");
					jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
				} else{
					log.info(importCompleteStatus);
					jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
				}
			} catch (Exception e) {
				jTableResponse = new JTableResponse("Error in Export");
				log.error("JSON ERROR", e);
			}
			return jTableResponse;
		}
		
		
	/**
	 * Export of defects to HPQC
	 * @param testRunListId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="test.defect.selective.export.hpqc.rest", produces="application/json" )
	public @ResponseBody JTableResponse defectExportHPQCRest(@RequestParam Integer testExecutionResultBugId, HttpServletRequest request,HttpServletResponse response,@RequestParam int defectManagementSystemId) {
		log.debug("ttest.defect.export.hpqc");
		JTableResponse jTableResponse;
		TestRunList testRunList = null;
		ConnectorHPQCRest hpqcConnector = null;
		try {
			log.info("testExecutionResultBugId Id input:" + testExecutionResultBugId);
			
			TestExecutionResultBugList terBugList = testExecutionBugsService.getByBugId(testExecutionResultBugId);
			
			ProductMaster product = terBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster();
			
			
			hpqcConnector =  getHPQCDefectManagementSystemConnectorRest(product.getProductId(), request);
			
			if (hpqcConnector == null) {
				jTableResponse = new JTableResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
				return jTableResponse;
			}
			
			ConnectorHPQCRest hpqcConnectorRest = null;
			hpqcConnectorRest =  getHPQCDefectManagementSystemConnectorRest(product.getProductId(), request);
			
			if (hpqcConnectorRest == null) {
				jTableResponse = new JTableResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
				return jTableResponse;
			}
		
			String importCompleteStatus =  tafTestManagementSystemIntegrator.exportTestExecutionSelectiveDefectRest(terBugList, hpqcConnectorRest, defectManagementSystemId);
			
			hpqcConnector.logout(hpqcConnector);
			if(importCompleteStatus != null){
				log.info("Export TestDefects completed.");
				jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
			} else{
				log.info(importCompleteStatus);
				jTableResponse = new JTableResponse("Ok","Export TestDefects completed.");
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in Export");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
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
					if (defectManagementSystem.getToolIntagration() != null && defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
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
    
	@RequestMapping(value="hpqc.import.features.rest", produces="application/json" )
	public @ResponseBody JTableResponse featuresImport(@RequestParam Integer productId, HttpServletRequest request,HttpServletResponse response) {
		log.debug("HPQC Features Import");
		JTableResponse jTableResponse;
		ConnectorHPQCRest hpqcConnector;	
		try {
			log.info("Product Id input:" + productId);
			
			ProductMaster product = productListService.getProductById(productId);		
			hpqcConnector =  getHPQCConnectorRest(product, request);
			if (hpqcConnector == null) {
				jTableResponse = new JTableResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
				return jTableResponse;
			}
			
			String importCompleteStatus = tafTestManagementSystemIntegrator.importFeatures(productId, hpqcConnector);
			ConnectorHPQCRest.logout(hpqcConnector);
			
			if(importCompleteStatus != null){
				log.info("Import Features Completed.");
				jTableResponse = new JTableResponse("Ok","Import Features Completed.");
			} else{
				log.info("Problem in Import Features");
				jTableResponse = new JTableResponse("Ok","Problem in Import Features");
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in Import");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	//Changes for Bugzilla bug :777 - starts
	/**
	 * Method for uploading report to HPQC testset
	 * 
	 */
	@RequestMapping(value="hpqc.upload.reports.rest", produces="application/json" )
	public @ResponseBody JTableResponse uploadReportsToTestSet(@RequestParam Integer testRunJobId, HttpServletRequest request,HttpServletResponse response) {
		log.debug("Upload Reports to TestSet");
		JTableResponse jTableResponse;
		ConnectorHPQCRest hpqcConnector;
		
		Boolean status = false;
		String entityId = "";
		try {
			log.info("testRunJobId input:" + testRunJobId);	
			
			TestRunJob testRunJob = workPackageService.getTestRunJobById(testRunJobId);
			 int productId = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductId();
			
			entityId = testRunJob.getTestSuite().getTestSuiteCode();
			ProductMaster product = productListService.getProductById(productId);
			
			hpqcConnector =  getHPQCConnectorRest(product, request);
			if (hpqcConnector == null) {
				jTableResponse = new JTableResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
				return jTableResponse;
			}		
			
			//file handling			
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile multipartFile = null;
			String fileName = "";			
			FileOutputStream os = null;
			
			Iterator<String> iterator = multipartRequest.getFileNames();
			File fileForProcess = null;
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileName = multipartFile.getOriginalFilename();
			
				fileForProcess = new File(fileName);
				os = new FileOutputStream(fileForProcess);
				os.write(multipartFile.getBytes());
				os.close();
				log.info("fileForProcess:"+fileForProcess);
			}
			//file handling
		
			if(fileForProcess != null){
				status = tafTestManagementSystemIntegrator.UploadReportsToTestSet(testRunJob, hpqcConnector, fileForProcess, "test-sets", entityId);
			} else{
				log.error("file to upload is null");
				ConnectorHPQCRest.logout(hpqcConnector);
				jTableResponse = new JTableResponse("ERROR", "file to upload is null");
				return jTableResponse;
			}			
			ConnectorHPQCRest.logout(hpqcConnector);
			
			if(status != null){
				log.info("Reports upload to HPQC TestSet Completed.");
				jTableResponse = new JTableResponse("Ok","Reports upload to HPQC TestSet Completed.");
			} else{
				log.info("Problem in Import Features");
				jTableResponse = new JTableResponse("ERROR","Problem in uploading reports to HPQC TestSet");
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error in uploading reports to HPQC TestSet");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	//Changes for Bugzilla bug :777 - Ends

	@RequestMapping(value = "export.name.code.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody	JTableResponse listExportNameAndCode(@RequestParam Integer testCaseExecutionResultId) {
		log.debug("inside export.name.code.list");
		JTableResponse jTableResponse = null;
		
		try {
			List<WorkPackageTCEPSummaryDTO> ExportReportdto = new ArrayList<WorkPackageTCEPSummaryDTO>();
			ExportReportdto=testManagementService.listExportSystemNameAndCode(testCaseExecutionResultId);

			List<JsonTestManagementSystem> ExportJsonList = new ArrayList<JsonTestManagementSystem>();
			if (ExportReportdto == null || ExportReportdto.size() ==0 ) {

				jTableResponse = new JTableResponse("OK", ExportJsonList, 0);
			}else{
				
				for (WorkPackageTCEPSummaryDTO exportReportDto : ExportReportdto) {
					ExportJsonList.add(new JsonTestManagementSystem(exportReportDto));
				}				
				jTableResponse = new JTableResponse("OK", ExportReportdto,ExportReportdto.size() );
				ExportReportdto = null;
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);	            
		}
		return jTableResponse;
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
	
	private void uploadReportsToTestRun( String filePath, Integer tfsRunId, TestManagementSystem testManagementSystem) {
		log.info("Upload Reports to Test Run");		
		Boolean status = false;
		try {
			log.info("TFS Test Run Id:" + tfsRunId);	
			if(testManagementSystem.getToolIntagration() != null && testManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase("TFS")){
				TFSIntegrator tfsConnector = getTFSConnector(testManagementSystem.getConnectionUri());
				TFSConnector.USERNAME=testManagementSystem.getConnectionUserName().trim();
				TFSConnector.PASSWORD=passwordEncryptionService.decrypt(testManagementSystem.getConnectionPassword().trim());
				TFSTeamProjectCollection projectCollection = getTFSProjectCollection(tfsConnector, testManagementSystem.getConnectionUri().trim());
				String productName = null;
				Set<TestManagementSystemMapping> testManagementMappings = testManagementSystem.getTestManagementSystemMappings();
				if(testManagementMappings != null && !testManagementMappings.isEmpty()){
					for(TestManagementSystemMapping testManagementSystemMapping : testManagementMappings){
						if(testManagementSystemMapping.getMappingType().equals(TAFConstants.ENTITY_PRODUCT)){
							productName = testManagementSystemMapping.getMappingValue();
						}
					}
				}				
				if(projectCollection!=null && tfsConnector !=null){								
					String projectName = testManagementSystem.getConnectionUri().trim().concat("/"+productName.trim()).trim();
					log.info("PDF Report file location : "+filePath);
					if(filePath != null){
						status = tafTestManagementSystemIntegrator.tfsUploadReportsToTestRun(tfsRunId, tfsConnector, projectCollection, projectName, filePath);
					}
				}
			}
			if(status != null){
				log.info("Reports upload to "+testManagementSystem.getToolIntagration() != null ?testManagementSystem.getToolIntagration().getName().trim():""+" TestSet Completed.");
			} else{
				log.info("Problem in Import Features");
			}
		} catch (Exception e) {
			log.error("JSON ERROR", e);
		}
	}
	
	public JTableResponse defectsExportTFSTestRunJob(int testRunJobId,  HttpServletRequest request,@RequestParam int defectManagementSystemId, DefectManagementSystem defectManagementSystem) {
		log.debug("test.defect.export.tfs.rest");
		JTableResponse jTableResponse;
		TestRunList testRunList = null;
		String importCompleteStatus = "";
		try {
			log.info("testRunConfigurationChildId Id input:" + testRunJobId);
			
			TestRunJob testRunJob=workPackageService.getTestRunJobById(testRunJobId);		
			
			ProductMaster product = testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster();
			TFSIntegrator tfsConnector = getTFSConnector(defectManagementSystem.getConnectionUri().trim());
			TFSConnector.USERNAME=defectManagementSystem.getConnectionUserName().trim();
			TFSConnector.PASSWORD=passwordEncryptionService.decrypt(defectManagementSystem.getConnectionPassword().trim());
			TFSTeamProjectCollection projectCollection = getTFSProjectCollection(tfsConnector, defectManagementSystem.getConnectionUri().trim());
			if (tfsConnector == null) {
				jTableResponse = new JTableResponse("Error", "Unable to establish connection to TFS. Check the mappings");				
			}
			
			List<TestExecutionResultBugList> bugs= new ArrayList<TestExecutionResultBugList>();
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans=testRunJob.getWorkPackageTestCaseExecutionPlans();
			for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
				bugs.addAll(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestExecutionResultBugListSet());
			}
		    
			importCompleteStatus =  tafTestManagementSystemIntegrator.exportTestExecutionDefectsRest(bugs,tfsConnector,projectCollection,defectManagementSystemId,defectManagementSystem.getConnectionUri().trim());
		    projectCollection.close();
			if(importCompleteStatus != null){
				log.info("Export TFS TestDefects completed.");
				jTableResponse = new JTableResponse("Ok","Export TFS TestDefects completed.");
			} else{
				log.info(importCompleteStatus);
				jTableResponse = new JTableResponse("Ok","Export TFS TestDefects completed.");
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in Export");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value="administration.scm.management.system.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addSCMManagementSystem(HttpServletRequest request,@ModelAttribute JsonSCMManagementSystem jsonSCMManagementSystem, JsonProductMaster jsonProductMaster, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		SCMSystem scmSystem=null;
		String scmSystemTitle = "";
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {		
			scmSystem = jsonSCMManagementSystem.getSCMSystem();	
			scmSystemTitle = jsonSCMManagementSystem.getTitle().trim();
				if(scmSystemTitle != null && scmSystemTitle != "" && jsonSCMManagementSystem.getProductId() != null){
					SCMSystem scms = defectManagementService.getSCMSystemByNameAndProductId(jsonSCMManagementSystem.getProductId(),scmSystemTitle);
					if(scms != null){
						return jTableSingleResponse = new JTableSingleResponse("INFORMATION","Title already exists");
					}
				}
				
				if(jsonSCMManagementSystem.getConnectionPassword() != null && !jsonSCMManagementSystem.getConnectionPassword().trim().equals("")){
					String encryptedPassword = passwordEncryptionService.encrypt(jsonSCMManagementSystem.getConnectionPassword());
					scmSystem.setConnectionPassword(encryptedPassword);
				}
				UserList userObj = (UserList)request.getSession().getAttribute("USER");
				scmSystem.setCreatedBy(userObj);
				scmSystem.setModifiedBy(userObj);
				defectManagementService.addSCMManagementSystem(scmSystem);
				//Entity Audition History //Addition		
				
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_SCM_MANAGEMENT_SYSTEM, scmSystem.getId(), scmSystem.getToolIntagration() !=null? scmSystem.getToolIntagration().getName():"", userObj);
				
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonSCMManagementSystem(scmSystem));
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);	 
	            e.printStackTrace();
	        }
	        
        return jTableSingleResponse;
    }
	

	@RequestMapping(value="administration.scm.management.system.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateSCMManagementSystem(HttpServletRequest request,@ModelAttribute JsonSCMManagementSystem jsonSCMManagementSystem,BindingResult result) {
		JTableResponse jTableResponse;
		SCMSystem scmSystem=null;
		ProductMaster productMaster = null;
		String remarks = "";
		String scmSystemTitle = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		
		try {
			UserList userObj = (UserList)request.getSession().getAttribute("USER");
			if(jsonSCMManagementSystem.getModifiedFieldTitle() != null && jsonSCMManagementSystem.getModifiedFieldTitle().equalsIgnoreCase("title")){
				scmSystemTitle = jsonSCMManagementSystem.getTitle().trim();
				if(scmSystemTitle != null && scmSystemTitle != ""){
					SCMSystem scms = defectManagementService.getSCMSystemByNameAndProductId(jsonSCMManagementSystem.getProductId(), scmSystemTitle);
					if(scms != null){
						return jTableResponse = new JTableResponse("INFORMATION", "Title already exists");
					}
				}
			}
			scmSystem=jsonSCMManagementSystem.getSCMSystem();
			if(jsonSCMManagementSystem.getModifiedFieldTitle()  != null && jsonSCMManagementSystem.getModifiedFieldTitle().contains("Password")){
				if(jsonSCMManagementSystem.getConnectionPassword() != null && !jsonSCMManagementSystem.getConnectionPassword().trim().equals("")){
					String encryptedPassword = passwordEncryptionService.encrypt(jsonSCMManagementSystem.getConnectionPassword());
					scmSystem.setConnectionPassword(encryptedPassword);
				}
			}
			productMaster = scmSystem.getProductMaster();
			productMaster = productListService.getProductById(productMaster.getProductId());
			remarks = "Product :"+productMaster.getProductName()+", SCMManagementSystem :"+scmSystem.getTitle();
			defectManagementService.updateSCMManagementSystem(scmSystem);
			if(jsonSCMManagementSystem.getModifiedField() != null && jsonSCMManagementSystem.getModifiedField().equalsIgnoreCase("connectionPassword")){
				String modifiedValue = passwordEncryptionService.encrypt(jsonSCMManagementSystem.getModifiedFieldValue());
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_SCM_MANAGEMENT_SYSTEM, scmSystem.getId(), scmSystem.getToolIntagration() !=null ? scmSystem.getToolIntagration().getName():"",
							jsonSCMManagementSystem.getModifiedField(), jsonSCMManagementSystem.getModifiedFieldTitle(),
							jsonSCMManagementSystem.getOldFieldValue(),modifiedValue , userObj, remarks);
			} else {
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_SCM_MANAGEMENT_SYSTEM, scmSystem.getId(), scmSystem.getToolIntagration() != null ?scmSystem.getToolIntagration().getName():"",
						jsonSCMManagementSystem.getModifiedField(), jsonSCMManagementSystem.getModifiedFieldTitle(),
						jsonSCMManagementSystem.getOldFieldValue(), jsonSCMManagementSystem.getModifiedFieldValue(), userObj, remarks);
			}
				List<SCMSystem> scmManagementSystems=new ArrayList<SCMSystem>();
				scmManagementSystems.add(scmSystem);
				jTableResponse = new JTableResponse("OK",scmManagementSystems,1);
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating SCM System!");
	            log.error("JSON ERROR", e);
	            e.printStackTrace();
	        } 
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.scm.management.system.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteSCMManagementSystem(@RequestParam int id) {
		JTableResponse jTableResponse;
		log.info("inside the administration.scm.management.system.delete");
		String message = "";		
		try {
			message = defectManagementService.deleteSCMManagementSystem(id);
			if(message.equals("OK")){
			jTableResponse = new JTableResponse(message);
			}else{
			jTableResponse = new JTableResponse("INFORMATION",message);
			}
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
            log.error("JSON ERROR", e);
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.scm.management.system.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listSCMManagementSystem(@RequestParam int productId) {
		log.debug("inside administration.scm.management.system.list");
		JTableResponse jTableResponse;
			try {
			List<SCMSystem> scmManagementSystems=defectManagementService.listSCMManagementSystem(productId);
			List<JsonSCMManagementSystem> jsonSCMManagementSystems = new ArrayList<JsonSCMManagementSystem>();
			for(SCMSystem scmManagementSystem: scmManagementSystems){
				jsonSCMManagementSystems.add(new JsonSCMManagementSystem(scmManagementSystem));
			}
	        jTableResponse = new JTableResponse("OK", jsonSCMManagementSystems, defectManagementService.getTotalRecordsOfSCMManagementSystems(productId));
	        scmManagementSystems = null;   
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	@RequestMapping(value="administration.scm.management.list.by.product.and.status",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listSCMManagementSystemByStatus(@RequestParam int productId, @RequestParam Integer status) {
		log.debug("Listing SCM Systems");
		JTableResponse jTableResponse;			 
		try {
			List<SCMSystem> scmManagementSystems=defectManagementService.listSCMManagementSystemByStatus(productId,status);
			
			List<JsonSCMManagementSystem> jsonSCMManagementSystems = new ArrayList<JsonSCMManagementSystem>();
			for(SCMSystem scm: scmManagementSystems){
				jsonSCMManagementSystems.add(new JsonSCMManagementSystem(scm));					
			}				
			jTableResponse = new JTableResponse("OK", jsonSCMManagementSystems,jsonSCMManagementSystems.size());     
			scmManagementSystems = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Product Test Environments!");
	            log.error("JSON ERROR listing Environment", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.tool.intagration.master.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addToolIntagrationMaster(HttpServletRequest request,@ModelAttribute JsonToolIntagrationMaster jsonToolIntagrationMaster, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		ToolIntagrationMaster toolIntagrationMaster=null;
		try {		
			toolIntagrationMaster=jsonToolIntagrationMaster.getToolIntagrationMaster();
			toolIntagrationMaster.setStatus(1);
			if( toolIntagrationMaster.getName() == null || toolIntagrationMaster.getName().trim().isEmpty() ){
				return  jTableSingleResponse = new JTableSingleResponse("ERROR","Tool Name Required!");
			}
			if(!toolIntagrationMasterService.isExistsToolIntagrationMasterByNameAndTypeId(toolIntagrationMaster.getName(), toolIntagrationMaster.getToolType().getId())) {
				toolIntagrationMasterService.addToolIntagrationMaster(toolIntagrationMaster);
			} else {
				return  jTableSingleResponse = new JTableSingleResponse("ERROR","Already Exist Tool Intagration "+toolIntagrationMaster.getName());
			}
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonToolIntagrationMaster(toolIntagrationMaster));
        } catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding toolIntegrationMaster record!");
            log.error("JSON ERROR", e);	 
            e.printStackTrace();
        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="administration.tool.intagration.master.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse updateToolIntagrationMaster(HttpServletRequest request,@ModelAttribute JsonToolIntagrationMaster jsonToolIntagrationMaster, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		ToolIntagrationMaster toolIntagrationMaster=null;
		try {		
			toolIntagrationMaster=jsonToolIntagrationMaster.getToolIntagrationMaster();
			UserList userObj = (UserList)request.getSession().getAttribute("USER");
			toolIntagrationMasterService.updateToolIntagrationMaster(toolIntagrationMaster);
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonToolIntagrationMaster(toolIntagrationMaster));
        } catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating toolIntegrationMaster record!");
            log.error("JSON ERROR", e);	 
            e.printStackTrace();
        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="administration.tool.intagration.master.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getToolIntagrationMasterList(HttpServletRequest request,Integer status) {
		JTableResponse jTableResponse;
		List<ToolIntagrationMaster> toolIntagrationMasterList= new ArrayList<ToolIntagrationMaster>();
		List<JsonToolIntagrationMaster> jsonToolIntagrationMasterList= new ArrayList<JsonToolIntagrationMaster>();
		try {		
			toolIntagrationMasterList=toolIntagrationMasterService.getToolIntagrationMasterListByStatus(status);
			if(toolIntagrationMasterList != null && toolIntagrationMasterList.size() >0 ) {
				for(ToolIntagrationMaster toolIntagrationMaster:toolIntagrationMasterList) {
					jsonToolIntagrationMasterList.add(new JsonToolIntagrationMaster(toolIntagrationMaster));
				}
			}
			jTableResponse = new JTableResponse("OK",jsonToolIntagrationMasterList,jsonToolIntagrationMasterList.size());
        } catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR","Error getToolIntegrationMasterList record!");
            log.error("JSON ERROR", e);	 
            e.printStackTrace();
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.tool.type.master.option.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions getToolTypeMasterOptiionList(@RequestParam Integer status) {
		log.debug("inside administration.tool.type.master.list");
		JTableResponseOptions jTableResponseOptions = null;	
		List<ToolTypeMaster> toolTypeMasterList= null;
		List<JsonToolTypeMaster> jsonToolTypeMasterList=new ArrayList<JsonToolTypeMaster>();
		try {			
			
			 
			toolTypeMasterList=toolIntagrationMasterService.getToolTypeMasterListByStatus(status);
			if(toolTypeMasterList != null && toolTypeMasterList.size() >0) {
				for(ToolTypeMaster toolTypeMaster: toolTypeMasterList){
					jsonToolTypeMasterList.add(new JsonToolTypeMaster(toolTypeMaster));
					
				}
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonToolTypeMasterList,true);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR administration.tool.type.master.list", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.tool.intagration.master.option.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions getToolIntagrationOptionList(@RequestParam Integer typeId) {
		log.debug("inside administration.tool.intagration.master.option.list");
		JTableResponseOptions jTableResponseOptions = null;	
		List<ToolIntagrationMaster> toolIntagrationMasterList= null;
		List<JsonToolIntagrationMaster> jsoonToolIntagerationMasterList=new ArrayList<JsonToolIntagrationMaster>();
		try {			
			toolIntagrationMasterList=toolIntagrationMasterService.getToolIntagrationMasterListByTypeId(typeId);
			if(toolIntagrationMasterList != null && toolIntagrationMasterList.size() >0) {
				for(ToolIntagrationMaster toolIntagrationMaster: toolIntagrationMasterList){
					jsoonToolIntagerationMasterList.add(new JsonToolIntagrationMaster(toolIntagrationMaster));
					
				}
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsoonToolIntagerationMasterList,true);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR administration.tool.intagration.master.option.list", e);
	        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="process.tool.management.syn.data",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse syncFeaturesIntoTAF(HttpServletRequest request,Integer productId,Integer toolId,String toolTypeName,boolean synFeatures,boolean synTestcases,boolean synTestsuites,boolean syncTestcasesToTestsuitesMapping,boolean syncFeaturesToTestcasesMapping,boolean exportDefectes,boolean exportResults) {
		JTableSingleResponse jTableResponse=null;
		try {
			
			
			String status="";
			if(toolTypeName != "" && toolTypeName.equalsIgnoreCase("Defect Management System")) {
				
				List<TestExecutionResultBugList> bugs= new ArrayList<TestExecutionResultBugList>(); 
				if(toolTypeName.equalsIgnoreCase("Defect Management System") && exportDefectes) {
					List<TestRunPlan> testRunPlanList=productListService.listTestRunPlanByProductId(productId);
					if(testRunPlanList != null && testRunPlanList.size() >0) {
						for(TestRunPlan plan:testRunPlanList) {
							List<WorkPackage> workpackages=workPackageService.listWorkPackageBytestrunplanId(plan.getTestRunPlanId());
							if(workpackages != null && workpackages.size() >0) {
								for(WorkPackage wp:workpackages){
									bugs.addAll(testExecutionBugsService.listByTestRun(wp.getWorkPackageId(), -1));
								}
							}
						}
					}
				}
				if(bugs == null || bugs.size() == 0 && toolTypeName.equalsIgnoreCase("Defect Management System")) {
					jTableResponse = new JTableSingleResponse("ERROR","No Defect available for this Product:"+productId);
					return jTableResponse;
				}
				
				DefectManagementSystem defectManagementSystem=defectManagementService.getDMSByProductId(productId,toolId);
				if(defectManagementSystem != null && (defectManagementSystem.getToolIntagration() != null && defectManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.JIRA_TOOL))) {
					JiraConnector jiraConnector = getJIRAConnectorByDefectSystemId(defectManagementSystem.getDefectManagementSystemId(), request);
					if (jiraConnector == null) {
						jTableResponse = new JTableSingleResponse("ERROR", "Unable to establish connection to JIRA. Check the mappings");
						return jTableResponse;
					}
					status = tafDefectManagementSystemIntegrator.jiraDefectsFile(bugs,jiraConnector);
				} else if(defectManagementSystem != null && (defectManagementSystem.getToolIntagration() != null && defectManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.TFS_TOOL))) {
					TFSIntegrator tfsConnector = getTFSConnector(defectManagementSystem.getConnectionUri().trim());
					TFSConnector.USERNAME=defectManagementSystem.getConnectionUserName().trim();
					TFSConnector.PASSWORD=passwordEncryptionService.decrypt(defectManagementSystem.getConnectionPassword().trim());
					TFSTeamProjectCollection projectCollection = getTFSProjectCollection(tfsConnector, defectManagementSystem.getConnectionUri().trim());
					if (tfsConnector == null) {
						jTableResponse = new JTableSingleResponse("ERROR", "Unable to establish connection to TFS. Check the mappings");				
					}
					String projectUrl = defectManagementSystem.getConnectionUri().trim();
					status =  tafTestManagementSystemIntegrator.exportTestExecutionDefectsRest(bugs, tfsConnector, projectCollection, defectManagementSystem.getDefectManagementSystemId(), projectUrl);
					projectCollection.close();
				} else if(defectManagementSystem != null && (defectManagementSystem.getToolIntagration() != null && defectManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.HPQC_TOOL))) {
					ConnectorHPQCRest hpqcConnector =hpqcConnector =  getHPQCDefectManagementSystemConnectorRest(productId, request);
					if (hpqcConnector == null) {
						jTableResponse = new JTableSingleResponse("ERROR", "Unable to establish connection to HPQC. Check the mappings");
						return jTableResponse;
					}
					/*status =  tafTestManagementSystemIntegrator.exportTestExecutionDefectsRest(bugs, hpqcConnector,defectManagementSystem.getDefectManagementSystemId());
					hpqcConnector.logout(hpqcConnector);*/
					String importCompleteStatus ="";
					if(bugs != null && bugs.size() >0) {
						for(TestExecutionResultBugList testExecutionResultBugList:bugs) {
							importCompleteStatus+=  tafTestManagementSystemIntegrator.exportTestExecutionDefectsRest(testExecutionResultBugList, hpqcConnector);
						}
					}
					hpqcConnector.logout(hpqcConnector);
					if(importCompleteStatus != null){
						log.info("Export TestDefects completed.");
						jTableResponse = new JTableSingleResponse("OK","Export TestDefects completed.");
					} else{
						log.info(importCompleteStatus);
						jTableResponse = new JTableSingleResponse("OK","Export TestDefects completed.");
					}
				} else {
					if(exportDefectes) {
						List<JsonTestExecutionResultBugList> jsonBugs = new ArrayList<JsonTestExecutionResultBugList>();
						ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
						if(bugs != null && bugs.size() >0) {
							for(TestExecutionResultBugList testExecutionResultBugList:bugs) {
								jsonBugs.add(new JsonTestExecutionResultBugList(testExecutionResultBugList));
							}
						}
						String defectsJson = ow.writeValueAsString(jsonBugs);
						String systemConnectionDetailsJson="";
						boolean connectionFlag=customTestSystemConnectorsManager.initializeTestSystemConnectorsManager(defectManagementSystem.getToolIntagration().getName(), defectManagementSystem.getDefectSystemVersion(), "Test Management Systems",testlink_2_DefectManagementSystem);
						
						if(connectionFlag) {
							customTestSystemConnectorsManager.reportDefectsToSystem(systemConnectionDetailsJson, defectsJson);
						} else {
							jTableResponse = new JTableSingleResponse("Error", "Unable to establish connection. Check the mappings");
							return jTableResponse;
						}
					}
				}
			} else if(toolTypeName != "" && toolTypeName.equalsIgnoreCase("Test Management Systems")) {
				ConnectorHPQCRest hpqcConnector = null;
				String tfsProductName = "";
				TestManagementSystem testManagementSystem=null;
				List<TestRunJob> testRunJobList =new ArrayList<TestRunJob>();
				ProductMaster product=productListService.getProductById(productId);
				testManagementSystem=testManagementService.getTMSByProduct(toolId,productId);
				Set<TestManagementSystemMapping> testManagementMappings = testManagementSystem.getTestManagementSystemMappings();
				if(testManagementMappings != null && !testManagementMappings.isEmpty()){
					for(TestManagementSystemMapping testManagementSystemMapping : testManagementMappings){
						if(testManagementSystemMapping.getMappingType().equals(TAFConstants.ENTITY_PRODUCT)){
							tfsProductName = testManagementSystemMapping.getMappingValue();
						}
					}
				}
				if(testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
					hpqcConnector =  getHPQCConnectorRest(product, request);
					if (hpqcConnector == null) {
						jTableResponse = new JTableSingleResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
						return jTableResponse;
					}
					/*String importCompleteStatus="";
					String testCaseSource = IDPAConstants.HPQC_TOOL;
					List<TestSuiteList> testSuiteList = testSuiteConfigurationService.getByProductId(productId);
					if(testSuiteList != null && testSuiteList.size() >0) {
						for(TestSuiteList testSuite:testSuiteList) {
							importCompleteStatus=tafTestManagementSystemIntegrator.importTestCasesRest(productId, hpqcConnector, null, null, testSuite.getTestSuiteName(),testCaseSource);
						}
					}*/
					if(toolTypeName.equalsIgnoreCase("Test Management Systems")) {
						List<TestRunPlan> testRunPlanList=productListService.listTestRunPlanByProductId(productId);
						if(testRunPlanList != null && testRunPlanList.size() >0) {
							for(TestRunPlan plan:testRunPlanList) {
								List<WorkPackage> workpackages=workPackageService.listWorkPackageBytestrunplanId(plan.getTestRunPlanId());
								if(workpackages != null && workpackages.size() >0) {
									for(WorkPackage wp:workpackages){
									
										testRunJobList.addAll(workPackageDAO.completedAndAbortedTestRunJobSetForAWorkpackage(wp.getWorkPackageId()));
									}
								}
							}
						}
					}
					String importCompleteStatus="";
					if(testRunJobList != null && testRunJobList.size() >0) {
						for(TestRunJob testRunJob :testRunJobList) {
							importCompleteStatus +=  tafTestManagementSystemIntegrator.exportTestExecutionResultsRest(testRunJob, hpqcConnector,testManagementSystem);
						}
					}
					if(importCompleteStatus != null && !importCompleteStatus.trim().isEmpty()){
						log.info("Export TestResults completed.");
						jTableResponse= null;
						jTableResponse = new JTableSingleResponse("OK","Data Sync Successfully.");
						return jTableResponse;
					} else{
						log.info("Error in Export TestResults.");
						jTableResponse  = null;
						jTableResponse = new JTableSingleResponse("ERROR","Error in Data Sync");
						return jTableResponse;
						 
					}
				} else if(testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(IDPAConstants.TFS_TOOL)){
					List<Integer> runIds = new ArrayList<Integer>();
					TFSIntegrator tfsConnector = getTFSConnector(testManagementSystem.getConnectionUri().trim());
					TFSConnector.USERNAME=testManagementSystem.getConnectionUserName().trim();
					TFSConnector.PASSWORD=passwordEncryptionService.decrypt(testManagementSystem.getConnectionPassword().trim());
					TFSTeamProjectCollection projectCollection = getTFSProjectCollection(tfsConnector, testManagementSystem.getConnectionUri().trim());
					//TODO Testing 
					String fileLocation="D:\\";
					if(testRunJobList != null && testRunJobList.size() >0) {
						for(TestRunJob testRunJob :testRunJobList) {
							runIds=tafTestManagementSystemIntegrator.tfsExportTestResults(testRunJob, tfsConnector, testManagementSystem,projectCollection, tfsProductName);
				
							for(int i=0;i<runIds.size();i++){
								if(fileLocation==""){
									String productName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductName();
									String testRunPlanName = testRunJob.getWorkPackage().getTestRunPlan().getTestRunPlanName();
									fileLocation = CommonUtility.getCatalinaPath().trim()+IDPAConstants.JASPERREPORTS_PATH+File.separator+"WorkPackages"+File.separator+testRunJob.getWorkPackage().getWorkPackageId()+File.separator+"Jobs"+File.separator+testRunJob.getTestRunJobId()+File.separator+productName+"-"+testRunPlanName+"-"+testRunJob.getTestRunJobId()+".pdf";
								}
								if(new File(fileLocation).exists()){
									uploadReportsToTestRun(fileLocation.trim(),runIds.get(i), testManagementSystem);
								} else {
									log.info("Test Run Job Report does not exist to upload to TFS");
								}
							}
					
						}
					}	
					if(runIds.size() > 0){
						log.info("Export TestResults completed.");
						return jTableResponse = new JTableSingleResponse("OK","Data Sync Successfully");
					} else{
						
						log.info("Export TestResults completed.");
						return jTableResponse = new JTableSingleResponse("ERROR","Error in Data Sync");
					}
					
				} else {
					boolean connectionFlag= customTestSystemConnectorsManager.initializeTestSystemConnectorsManager(testManagementSystem.getToolIntagration().getName(), testManagementSystem.getTestSystemVersion(), "Test Management Systems",testlink_2_DefectManagementSystem);
					if(!connectionFlag) {
						jTableResponse = new JTableSingleResponse("Error", "Unable to establish connection. Check the mappings");
						return jTableResponse;
					}
						if(synFeatures) {
							String featuresJson = customTestSystemConnectorsManager.syncFeaturesIntoTAF("", "", productId);
							
						}
						if(synTestcases) {
							customTestSystemConnectorsManager.syncTestcasesIntoTAF("", "", productId);
						}
						if(synTestsuites) {
							customTestSystemConnectorsManager.syncTestsuitesIntoTAF("", "", productId);
						}
						if(syncTestcasesToTestsuitesMapping)  {
							customTestSystemConnectorsManager.syncTestcaseToTestSuiteMappings("", "", productId);
						}
						if(syncFeaturesToTestcasesMapping) {
							customTestSystemConnectorsManager.syncFeaturesToTestcasesMappings("", "", productId);
						}
				}
				
			} else if(toolTypeName != "" && toolTypeName.equalsIgnoreCase("SCM Systems")) {
				jTableResponse = new JTableSingleResponse("ERROR","No Connector Availabe");
				return jTableResponse;
			}
			jTableResponse = new JTableSingleResponse("OK","Data Sync successfully!");
		}catch(Exception e) {
			jTableResponse = new JTableSingleResponse("ERROR","Error in Sync");
			return jTableResponse;
			
		}
		return jTableResponse;
	}
	
	public String defectsExportTestLinkTestRunJob(int testRunJobId,  HttpServletRequest request,@RequestParam int defectManagementSystemId, DefectManagementSystem defectManagementSystem) {
		log.debug("test.defect.export.testlink.rest");
		String defectJSON = "";
		try {
			log.info("testRunConfigurationChildId Id input:" + testRunJobId);
			TestRunJob testRunJob=workPackageService.getTestRunJobById(testRunJobId);		
			ProductMaster product = testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster();
			List<TestExecutionResultBugList> bugs= new ArrayList<TestExecutionResultBugList>();
			List<JsonTestExecutionResultBugList> jsonBugs= new ArrayList<JsonTestExecutionResultBugList>();
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans=testRunJob.getWorkPackageTestCaseExecutionPlans();
			for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
				bugs.addAll(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestExecutionResultBugListSet());
			}
			if(bugs != null && bugs.size() >0) {
				for(TestExecutionResultBugList testExecutionResultBugList:bugs) {
					jsonBugs.add(new JsonTestExecutionResultBugList(testExecutionResultBugList));
				}
			}
			defectJSON = new Gson().toJson(jsonBugs);
		} catch (Exception e) {
			log.error("Error in export testlink defects");
		}
		return defectJSON;
	}
}
