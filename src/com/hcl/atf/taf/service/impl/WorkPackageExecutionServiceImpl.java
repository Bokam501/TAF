package com.hcl.atf.taf.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.AOTCConstants;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.dao.WorkPackageExecutionDAO;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.LifeCyclePhase;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestcaseExecutionEvent;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkFlowEvent;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.json.JsonLogFileContent;
import com.hcl.atf.taf.model.json.JsonTestRunPlan;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.HostHeartBeatService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageExecutionService;
import com.hcl.atf.taf.service.WorkPackageService;

@Service
public class WorkPackageExecutionServiceImpl implements WorkPackageExecutionService {


	private static final Log log = LogFactory.getLog(WorkPackageExecutionServiceImpl.class);

	@Autowired
	WorkPackageExecutionDAO workPackageExecutionDAO;
	@Autowired
	private EnvironmentService environmentService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private ExecutionTypeMasterService executionTypeMasterService;
	@Autowired
	private ProductBuildDAO productBuildDAO;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private UserListService userListService;
	@Autowired
	private TestCaseService testCaseService;
	@Autowired
	private TestExecutionService testExecutionService;
	@Autowired
	private HttpServletRequest request;	
	@Autowired
	private HostHeartBeatService hostHeartBeatService;
	@Autowired
	private MongoDBService mongoDBService;	
	@Value("#{ilcmProps['GEN_EVIDENCE_FOLDER']}")
	private String gen_evidence_Folder;
	
	@Override
	@Transactional
	public boolean abortAutomatedTestRunJob(Integer testRunJobId) {
		
		try {
			// Get Test Run Job
			TestRunJob testRunJob = workPackageExecutionDAO.getTestRunJobById(testRunJobId);
			if (testRunJob == null) {
				log.info("Unable to find Test Run Job ... : " + testRunJobId);
				return false;
			}
			
			//Check the status of the job
			if (testRunJob.getTestRunStatus() == IDPAConstants.JOB_NEW) {
				//If it is new, this has not yet been posted to the terminal. Just change the status directly to aborted
				testRunJob.setTestRunStatus(IDPAConstants.WORKFLOW_STAGE_ID_ABORTED);
				environmentService.updateTestRunJob(testRunJob);
				//Setting WP Status
				if(testRunJob.getWorkPackage() != null)
					workPackageService.updateWPStatus(testRunJob,testRunJob.getWorkPackage().getWorkPackageId(), IDPAConstants.WORKFLOW_STAGE_ID_ABORTED, 0, false);
				
				if(testRunJob!=null){
					if(testRunJob.getTestRunJobId()!=null)
					log.info("Updating Testrun to Mongo ID "+testRunJob.getTestRunJobId());
					mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
				}
				
				return true;
			} else if (testRunJob.getTestRunStatus() == IDPAConstants.JOB_QUEUED ||
					testRunJob.getTestRunStatus() == IDPAConstants.JOB_EXECUTING) {
				//Change the status to Abort. Terminal will abort the job and notify
				testRunJob.setTestRunStatus(IDPAConstants.JOB_ABORT);
				environmentService.updateTestRunJob(testRunJob);
				
				//Setting WP Status
				if(testRunJob.getWorkPackage() != null)
					workPackageService.updateWPStatus(testRunJob,testRunJob.getWorkPackage().getWorkPackageId(), IDPAConstants.WORKFLOW_STAGE_ID_ABORTED, 0, false);
				
				hostHeartBeatService.setHostResponseToHeartbeatAsJobsAvailable(testRunJob.getHostList().getHostId());
				
				if(testRunJob!=null){
					if(testRunJob.getTestRunJobId()!=null)
					log.info("Updating Testrun to Mongo ID "+testRunJob.getTestRunJobId());
					mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
				}
				
				return true;
			} else {
				//Do nothing
				return true;
			}
		} catch (Exception e) {
			
			log.error("Unable to abort Job : " + testRunJobId, e);
			return false;
		}
	}
	
	@Override
	@Transactional
	public TestRunJob getTestRunJobById(int testRunJobId) {
		
		try {
			// Get Test Run Job
			TestRunJob testRunJob = workPackageExecutionDAO.getTestRunJobById(testRunJobId);
			if (testRunJob == null) {
				log.info("Unable to find Test Run Job ... : " + testRunJobId);
				return null;
			} else {
				return testRunJob;
			}
		} catch (Exception e) {
			log.error("Error while fetching test Run Job", e);
			return null;
		}

	}
	
	@Override
	@Transactional
	public String getTestRunJobResultStatus(int testRunJobId) {
		
		try {
			return workPackageExecutionDAO.getTestRunJobResultStatus(testRunJobId);
		} catch (Exception e) {
			log.error("Error while fetching test Run Job", e);
			return "FAILED";
		}
	}

	@Override
	@Transactional
	public String getWorkpackageResultStatus(int workPackageId) {
		
		try {
			return workPackageExecutionDAO.getWorkpackageResultStatus(workPackageId);
		} catch (Exception e) {
			log.error("Error while fetching test Run Job", e);
			return "FAILED";
		}
	}

	@Override
	@Transactional
	public int getCompletedJobsCount(int workPackageId) {
		
		try {
			return workPackageExecutionDAO.getCompletedJobsCount(workPackageId);
		} catch (Exception e) {
			log.error("Error while fetching workpackage completed jobs count", e);
			return 0;
		}
	}

	@Override
	@Transactional
	public Integer countAllWorkpackages(Date startDate, Date endDate) {
		return workPackageExecutionDAO.countAllWorkpackages(startDate,endDate);
	}

	@Override
	@Transactional
	public boolean executeSingleAutomatedTestCase(Integer testCaseId, Integer testRunPlanId, UserList user,String deviceNames) {

		try {
			TestRunPlan testRunPlan = null;
			if (testRunPlanId == null || testRunPlanId < 0) {
				//Testing, execute it on the first test plan
				if(testCaseService.getTestCaseTestRunPlans(testCaseId) != null){
					if(testRunPlanId != -1){
						List<JsonTestRunPlan> jtrp = testCaseService.getTestCaseTestRunPlans(testCaseId);
						if(jtrp != null && jtrp.size() >0){
							testRunPlanId = jtrp.iterator().next().getTestRunPlanId();	
							testRunPlan = productListService.getTestRunPlanById(testRunPlanId);
						}else{
							return false;
						}
					}
					
				}else{
					return false;
				}
			} else {
				testRunPlan = productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
			}
			log.info("Single Testcase execution Test Run Plan ID : " + testRunPlanId);
			WorkPackage newWorkpackage = createNewWorkpackageForAutomatedExecution(testCaseId, testRunPlanId, user);
			
			if(newWorkpackage != null) {
				TestCaseList testCase = testCaseService.getTestCaseById(testCaseId);
				String testCaseNames = testCase.getTestCaseName();
				workPackageService.workpackageExxecutionPlan(newWorkpackage, testRunPlan, null, deviceNames, testCaseNames);
				
			}else{
				log.info("Unable to execute Test case as workpackage could not be created for it");
				return false;
			}
			return true;

		}
		catch(Exception e){
			log.error("Problem while executing Test case", e);
			return false;
		}
	}

	@Override
	@Transactional
	public WorkPackage createNewWorkpackageForAutomatedExecution (Integer testCaseId, Integer testRunPlanId, UserList loginuser) {

		try {
			TestRunPlan testRunPlan = productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);			
			WorkPackage newWorkpackage=new WorkPackage();
			String name=testRunPlan.getProductVersionListMaster().getProductMaster().getProductName()+"-"+testRunPlan.getProductVersionListMaster().getProductVersionName()+"-"+DateUtility.getCurrentTime();
			
			newWorkpackage.setName(name);
			newWorkpackage.setDescription(name +" created.");
			
			newWorkpackage.setCreateDate(DateUtility.getCurrentTime());
			newWorkpackage.setModifiedDate(DateUtility.getCurrentTime());
			newWorkpackage.setStatus(1); //IDPAConstants.WORKPACKAGE_STATUS_NEW); ????
			newWorkpackage.setIsActive(1);
			if(testRunPlan.getExecutionType().getExecutionTypeId()==3)
			{
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterService.getExecutionTypeByExecutionTypeId(7);
				newWorkpackage.setWorkPackageType(executionTypeMaster);
			}else{
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterService.getExecutionTypeByExecutionTypeId(8);
				newWorkpackage.setWorkPackageType(executionTypeMaster);
			}
			
			ProductBuild productBuild = productBuildDAO.getLatestProductBuild(testRunPlan.getProductVersionListMaster().getProductMaster().getProductId());
			newWorkpackage.setProductBuild(productBuild);
			
			newWorkpackage.setTestRunPlan(testRunPlan);
			newWorkpackage.setTestRunPlanGroup(null);
			newWorkpackage.setSourceType(IDPAConstants.WORKPACKAGE_SOURCE_TESTRUNPLAN);
			
			WorkFlowEvent workFlowEvent = new WorkFlowEvent();
			workFlowEvent.setEventDate(DateUtility.getCurrentTime());
			workFlowEvent.setRemarks("New Workpackage Added :"+newWorkpackage.getName());
			String user1 = loginuser.getLoginId();
			UserList user = userListService.getUserByLoginId(user1);
			
			workFlowEvent.setUser(user);
			workFlowEvent.setWorkFlow(workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_NEW));
			
			workPackageService.addWorkFlowEvent(workFlowEvent);
			newWorkpackage.setWorkFlowEvent(workFlowEvent);
			newWorkpackage.setActualStartDate(DateUtility.getCurrentTime());
			newWorkpackage.setPlannedEndDate(DateUtility.getCurrentTime());
			newWorkpackage.setPlannedStartDate(DateUtility.getCurrentTime());
			newWorkpackage.setUserList(user);
			newWorkpackage.setIterationNumber(-1);
			LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
			lifeCyclePhase.setLifeCyclePhaseId(4);
			newWorkpackage.setLifeCyclePhase(lifeCyclePhase);
			newWorkpackage.setWorkPackageId(0);
			newWorkpackage.setTestExecutionMode(testRunPlan.getAutomationMode());
			if(productBuild!=null){
				workPackageService.addWorkPackage(newWorkpackage);
				if(newWorkpackage!=null && newWorkpackage.getWorkPackageId()!=null){
					mongoDBService.addWorkPackage(newWorkpackage.getWorkPackageId());
				}
			}else{
				log.info("Unable to create new workpackage. This could be because the Product Build specified is not active");
			}
			return newWorkpackage;

		}
		catch(Exception e){
			log.error("Unable to create new workpackage", e);
			return null;
		}
	}

	//@Override
	@Transactional
	public boolean triggerJenkinsJobToInitiateLatestBuild(Integer testSuiteId) {
		
		//TODO : Trigger the jenkins jobs to update the test script packs for the test suites in the test run plan
	    boolean buildSuccessful = false;
		try {
			//TODO : Get the URL for the Jenkins Job from the test suite(Permanent) / Properties file (Temporary)
			URL url = new URL ("http://localhost:8080/job/test/build"); // Jenkins URL localhost:8080, job named 'test'
		    String user = "developer"; // username
		    String pass = "developer"; // password or API token
		    String authStr = user +":"+  pass;
		    String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));

		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setDoOutput(true);
		    connection.setRequestProperty("Authorization", "Basic " + encoding);
		    InputStream content = connection.getInputStream();
		    BufferedReader in = new BufferedReader (new InputStreamReader (content));
		    String line;
		    while ((line = in.readLine()) != null) {
		    	System.out.println(line);
		    	if (line.contains("201")) {
		    		buildSuccessful = true;
		        	break;
		        }
		    }
		} catch(Exception e) {
			log.error("Unable to trigger Jenkins Workpackage", e);
		}
	    return buildSuccessful;
	}

	@Override
	@Transactional
	public void postEvent(TestcaseExecutionEvent testcaseExecutionEvent){
		
		workPackageExecutionDAO.addTestcaseExecutionEvent(testcaseExecutionEvent);
	}

	@Override
	@Transactional
	public TestcaseExecutionEvent getTestcaseExecutiontEvent(String eventName, String testcaseName, Integer jobId, Integer workPackageId) {
		try {
			TestcaseExecutionEvent testcaseExecutionEvent = workPackageExecutionDAO.getTestcaseExecutiontEvent(eventName, testcaseName, jobId, workPackageId);
			
			if (testcaseExecutionEvent != null && testcaseExecutionEvent.getExpiryPolicy() == TestcaseExecutionEvent.TEV_EXPIRY_POLICY_SINGLEUSE) {
				killTestcaseExecutiontEvent(testcaseExecutionEvent);
			}
			return testcaseExecutionEvent;
		} catch (Exception e) {
			log.error("Error while posting TestcaseExecutionEvent", e);
			return null;
		}
	}
	
	@Override
	@Transactional
	public void killTestcaseExecutiontEvent(TestcaseExecutionEvent testcaseExecutionEvent) {
		
		try {
			workPackageExecutionDAO.deleteTestcaseExecutiontEvent(testcaseExecutionEvent);
		} catch (Exception e) {
			log.error("Error while killing TestcaseExecutionEvent", e);
		}
	}

	@Override
	@Transactional
	public void clearJobTestcaseExecutiontEvents(Integer jobId) {
		
		try {
			workPackageExecutionDAO.deleteTestcaseExecutiontEvents(null, null, jobId, null, TestcaseExecutionEvent.TEV_EXPIRY_POLICY_JOB_COMPLETION, null);
		} catch (Exception e) {
			log.error("Error while killing Job TestcaseExecutionEvents", e);
		}
	}

	@Override
	@Transactional
	public void clearWorkpackageTestcaseExecutiontEvents(Integer workpackageId) {
		
		try {
			workPackageExecutionDAO.deleteTestcaseExecutiontEvents(null, null, null, workpackageId, TestcaseExecutionEvent.TEV_EXPIRY_POLICY_WORKPACKAGE_COMPLETION, null);
		} catch (Exception e) {
			log.error("Error while killing Workpackage TestcaseExecutionEvents", e);
		}
	}

	@Override
	@Transactional
	public void clearNamedTestcaseExecutiontEvents(String eventName) {
		
		try {
			workPackageExecutionDAO.deleteTestcaseExecutiontEvents(eventName, null, null, null, null, null);
		} catch (Exception e) {
			log.error("Error while killing named TestcaseExecutionEvents", e);
		}
	}

	@Override
	@Transactional
	public void clearTimeBasedTestcaseExecutiontEvents(Date expiryTime) {
		
		try {
			workPackageExecutionDAO.deleteTestcaseExecutiontEvents(null, null, null, null, TestcaseExecutionEvent.TEV_EXPIRY_POLICY_TIME_BASED, expiryTime.getTime());
		} catch (Exception e) {
			log.error("Error while killing timebased TestcaseExecutionEvent", e);
		}
	}

	@Override
	@Transactional
	public void clearImmortalTestcaseExecutiontEvents() {
		
		try {
			workPackageExecutionDAO.deleteTestcaseExecutiontEvents(null, null, null, null, TestcaseExecutionEvent.TEV_EXPIRY_POLICY_IMMORTAL, null);
		} catch (Exception e) {
			log.error("Error while killing immortal TestcaseExecutionEvents", e);
		}
	}
	
	@Override
	@Transactional
	public JsonLogFileContent getJobLogUpdatedContent(Integer testJobId, Integer lastLine) {	
		
		JsonLogFileContent jsonLogFileContent = null;
		StringBuffer sb = new StringBuffer();
		String filePath = null;
		String testJobStatus = "";
		try {			
			TestRunJob testJob = workPackageExecutionDAO.getTestRunJobById(testJobId);
			if(testJob != null && testJob.getTestRunStatus() != null && (testJob.getTestRunStatus() == 3 || testJob.getTestRunStatus() == 8))
				filePath = CommonUtility.getCatalinaPath()+ File.separator + "webapps" + request.getContextPath()+ File.separator + "Evidence"+File.separator+testJobId+File.separator+IDPAConstants.EVIDENCE_LOG+ File.separator + "Live-log-"+testJobId+".txt";
			else if(testJob != null && testJob.getTestRunStatus() != null &&  (testJob.getTestRunStatus() == 5 || testJob.getTestRunStatus() == 7)){
				//filePath = CommonUtility.getCatalinaPath()+ File.separator + "webapps" + request.getContextPath()+ File.separator + "Evidence"+File.separator+testJobId+File.separator+IDPAConstants.EVIDENCE_LOG+ File.separator + "ILCM-Terminal-Job-"+testJobId+".txt";
				filePath = CommonUtility.getCatalinaPath()+ File.separator + "webapps" + File.separator + "ROOT"+File.separator+testJobId+File.separator+IDPAConstants.EVIDENCE_LOG+ File.separator + "ILCM-Terminal-Job-"+testJobId+".txt";
			}
			File jobLogFile = new File(filePath);
			if (!jobLogFile.exists() && testJob.getTestRunStatus() != 7) {				
				log.info("Job : " + testJobId + "Job log does not exist");
				return null;
			} else if(!jobLogFile.exists() &&  testJob.getTestRunStatus() == 7) {
				filePath = CommonUtility.getCatalinaPath()+ File.separator + "webapps" + request.getContextPath()+ File.separator + "Evidence"+File.separator+testJobId+File.separator+IDPAConstants.EVIDENCE_LOG+ File.separator + "Live-log-"+testJobId+".txt";
				if(!new File(filePath).exists()){
					log.info("Job : " + testJobId + "Job log does not exist");
					return null;
				}
			}
			
			/*List<Object> list = new ArrayList<>();
			Stream<String> stream = Files.lines(Paths.get(filePath));*/
			List<String> lines = Files.readAllLines(Paths.get(filePath));
			int lineCounter = 0;
			/*list = stream.collect(Collectors.toList());*/
			if (lastLine == null || lastLine <= 0)
				lastLine = 0;
//			for (Object line : list) {
//				lineCounter++;
//				if (lineCounter > lastLine) {
//					sb.append(System.lineSeparator() + (Object) line);
//				}
//			}
			
			if(lines != null && !lines.isEmpty()) {
				for (String line :lines ) {
					lineCounter++;
					if (lineCounter > lastLine) {
						sb.append(System.lineSeparator() + line);
					}
				}
			}
			
			log.info("Lines from log file : " +sb.toString());
			
			if(testJob != null && testJob.getTestRunStatus() != null){
				if(testJob.getTestRunStatus() == IDPAConstants.JOB_COMPLETED){
					testJobStatus = IDPAConstants.JOB_STATUS_COMPLETED;
				}else if(testJob.getTestRunStatus() == IDPAConstants.JOB_ABORTED){
					testJobStatus = IDPAConstants.JOB_STATUS_ABORTED;
				}else if(testJob.getTestRunStatus() == IDPAConstants.JOB_EXECUTING){
					testJobStatus = IDPAConstants.JOB_STATUS_EXECUTING;
				}else if(testJob.getTestRunStatus() == IDPAConstants.JOB_QUEUED){
					testJobStatus = IDPAConstants.JOB_STATUS_QUEUED;
				}else{
					testJobStatus = IDPAConstants.JOB_STATUS_COMPLETED;
				}
			}
			jsonLogFileContent = new JsonLogFileContent();
			jsonLogFileContent.setStartingLine(lastLine + 1);
			jsonLogFileContent.setLastLine(lineCounter);
			jsonLogFileContent.setLogFileContent(sb.toString());
			jsonLogFileContent.setJobId(testJobId);
			jsonLogFileContent.setJobStatus(testJobStatus);
			jsonLogFileContent.setWorkpackageId(testJob.getWorkPackage().getWorkPackageId());/*
			log.info("Log file content : " + (lastLine + 1) + " : " + lineCounter + System.lineSeparator() + sb.toString());*/
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to read log file content", e);
		}
		return jsonLogFileContent;
	}
	
	@Override
	@Transactional
	public boolean abortAutomatedWorkPackage(Integer workPackageId) {
		boolean abortAutomatedWorkPackage = false;
		try {
			WorkPackage wp = workPackageService.getWorkPackageById(workPackageId);
			Set<TestRunJob> trjs = wp.getTestRunJobSet();
			for(TestRunJob trj : trjs){
				abortAutomatedWorkPackage = abortAutomatedTestRunJob(trj.getTestRunJobId()); 
			}
			return abortAutomatedWorkPackage;
		} catch (Exception e) {
			
			log.error("Unable to the workpackage : " + workPackageId, e);
			return abortAutomatedWorkPackage;
		}
	}
}
