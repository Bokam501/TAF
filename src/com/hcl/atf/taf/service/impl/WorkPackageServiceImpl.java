package com.hcl.atf.taf.service.impl;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.ISEServerAccesUtility;
import com.hcl.atf.taf.dao.EnvironmentDAO;
import com.hcl.atf.taf.dao.ExecutionTypeMasterDAO;
import com.hcl.atf.taf.dao.ISERecommendedTestCaseDAO;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.ProductFeatureDAO;
import com.hcl.atf.taf.dao.ProductLocaleDao;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ProductTeamResourcesDao;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.TestCaseStepsListDAO;
import com.hcl.atf.taf.dao.TestExecutionResultBugDAO;
import com.hcl.atf.taf.dao.TestFactoryProductCoreResourcesDao;
import com.hcl.atf.taf.dao.TestFactoryResourceReservationDao;
import com.hcl.atf.taf.dao.TestSuiteListDAO;
import com.hcl.atf.taf.dao.TimeSheetManagementDAO;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.dao.WorkShiftMasterDAO;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.Evidence;
import com.hcl.atf.taf.model.EvidenceType;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.LifeCyclePhase;
import com.hcl.atf.taf.model.PerformanceLevel;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductLocale;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.TestCaseConfiguration;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseScript;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestCycle;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunPlanGroup;
import com.hcl.atf.taf.model.TestStepExecutionResult;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.WorkFlowEvent;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;
import com.hcl.atf.taf.model.WorkPackageFeature;
import com.hcl.atf.taf.model.WorkPackageFeatureExecutionPlan;
import com.hcl.atf.taf.model.WorkPackageTestCase;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkPackageTestSuite;
import com.hcl.atf.taf.model.WorkPackageTestSuiteExecutionPlan;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;
import com.hcl.atf.taf.model.dto.ISERecommandedTestcases;
import com.hcl.atf.taf.model.dto.MetricsMasterDTO;
import com.hcl.atf.taf.model.dto.MetricsMasterDefectsDTO;
import com.hcl.atf.taf.model.dto.MetricsMasterTestCaseResultDTO;
import com.hcl.atf.taf.model.dto.ScriptLessExecutionDTO;
import com.hcl.atf.taf.model.dto.TestCaseDTO;
import com.hcl.atf.taf.model.dto.TestStepExecutionResultDTO;
import com.hcl.atf.taf.model.dto.WorkPackageBuildTestCaseSummaryDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDayWisePlanDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDemandProjectionDTO;
import com.hcl.atf.taf.model.dto.WorkPackageExecutionPlanUserDetails;
import com.hcl.atf.taf.model.dto.WorkPackageStatusSummaryDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPSummaryDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTestCaseExecutionPlanStatusDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTestCaseSummaryDTO;
import com.hcl.atf.taf.model.json.JsonMetricsMasterProgramDefects;
import com.hcl.atf.taf.model.json.JsonMetricsProgramExecutionProcess;
import com.hcl.atf.taf.model.json.JsonMetricsTestCaseResult;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.JsonTestCycle;
import com.hcl.atf.taf.model.json.JsonTestFactoryResourceReservation;
import com.hcl.atf.taf.model.json.JsonUserList;
import com.hcl.atf.taf.model.json.JsonWorkPackageDailyPlan;
import com.hcl.atf.taf.model.json.JsonWorkPackageDemandProjection;
import com.hcl.atf.taf.model.json.JsonWorkPackageFeature;
import com.hcl.atf.taf.model.json.JsonWorkPackageFeatureExecutionPlan;
import com.hcl.atf.taf.model.json.JsonWorkPackageStatusSummary;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCase;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestSuite;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestSuiteExecutionPlan;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.mongodb.dao.WorkPackageMongoDAO;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.report.xml.beans.TestCaseListBean;
import com.hcl.atf.taf.report.xml.beans.TestCaseStepsListBean;
import com.hcl.atf.taf.report.xml.beans.TestRunJobBean;
import com.hcl.atf.taf.report.xml.beans.TestSuiteListBean;
import com.hcl.atf.taf.report.xml.beans.WorkPackageBean;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.ResourceManagementService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.WorkPackageService;

@Service
public class WorkPackageServiceImpl implements WorkPackageService {

	private static final Log log = LogFactory
			.getLog(WorkPackageServiceImpl.class);

	@Autowired
	private WorkPackageDAO workPackageDAO;
	@Autowired
	private TestCaseStepsListDAO testCaseStepsListDAO;
	@Autowired
	private ProductBuildDAO productBuildDAO;
	@Autowired
	private UserListDAO userListDAO;
	@Autowired
	private ProductMasterDAO productMasterDAO;
	@Autowired
	private ProductLocaleDao localeDao;
	@Autowired
	private TimeSheetManagementDAO timeSheetManagementDAO;
	@Autowired
	private WorkShiftMasterDAO workShiftMasterDAO;
	@Autowired
	private TestCaseListDAO testCaseListDAO;
	@Autowired
	private ResourceManagementService resourceManagementService;
	@Autowired
	private EnvironmentDAO environmentDAO;
	@Autowired
	private ExecutionTypeMasterDAO executionTypeMasterDAO;
	@Autowired
	private TestSuiteListDAO testSuiteListDAO;
	@Autowired
	private ProductTeamResourcesDao productTeamResourcesDao;
	@Autowired
	private TestFactoryProductCoreResourcesDao testFactoryProductCoreResourcesDao;
	@Autowired
	private TestFactoryResourceReservationDao testFactoryResourceReservationDao;

	@Autowired
	private ProductFeatureDAO productFeatureDAO;
	@Autowired
	private WorkPackageMongoDAO workPackageMongoDAO;

	@Autowired
	private TestExecutionResultBugDAO testExecutionResultBugDAO;

	@Value("#{ilcmProps['MONGODB_AVAILABLE']}")
	private String MONGODB_AVAILABLE;
	@Value("#{ilcmProps['ISE_SERVER_URL']}")
    private String iseServerURL;
	
	
	@Value("#{ilcmProps['ISE_REGRESSION_OPTIMIZATION_SERVICE_API']}")
	private String iseRegressionOptimizationServiceName;
	
	@Value("#{ilcmProps['ISE_UNSTABLE_TEST_CASES_IGNORE']}")
	private String iseUnstableTestCaseIgnore;
	
	@Value("#{ilcmProps['ISE_ENVIRONMENT_SPECIFIC_TEST_CASES_IGNORE']}")
	private String iseEnvironmentSpecificTestCaseIgnore;
	
	@Value("#{ilcmProps['ISE_GOLDEN_TEST_CASES_IGNORE']}")
	private String iseGoldenTestCaseIgnore;
	
	
	@Autowired
	private MongoDBService mongoDBService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private EnvironmentService environmentService;
	
	@Autowired
	private ProductVersionListMasterDAO productVersionListMasterDAO;
	
	@Autowired
	private ISERecommendedTestCaseDAO iseRecommendedTestCaseDAO;
	
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService;
	
	@Value("#{ilcmProps['EVIDENCE_FOLDER']}")
    private String evidence_Folder;
	
	@Override
	@Transactional
	public List<WorkPackageTestCase> listWorkPackageTestCases(int workPackageId) {
		return workPackageDAO.listWorkPackageTestCases(workPackageId);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCase> listWorkPackageTestCases(
			int workPackageId, int jtStartIndex, int jtPageSize) {
		return workPackageDAO.listWorkPackageTestCases(workPackageId,
				jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public List<JsonWorkPackageTestCase> listJsonWorkPackageTestCases(
			int workPackageId, int jtStartIndex, int jtPageSize) {
		return workPackageDAO.listJsonWorkPackageTestCases(workPackageId,
				jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public Integer getWorkPackageTestCasesCount(int workPackageId,
			int jtStartIndex, int jtPageSize) {
		return workPackageDAO.getWorkPackageTestCasesCount(workPackageId,
				jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public Integer getWorkPackageTestCaseOfTCID(int testcaseId) {
		return workPackageDAO.getWorkPackageTestCaseOfTCID(testcaseId);
	}

	@Override
	@Transactional
	public int totalRecordsCountForWorkPackageTestCases(int workPackageId) {
	
		return workPackageDAO
				.totalRecordsCountForWorkPackageTestCases(workPackageId);
	}

	@Override
	@Transactional
	public int initializeWorkPackageWithTestCases(int workPackageId) {

		// Get all the test cases for the product of the workpackage
		WorkPackage workPackage = workPackageDAO
				.getWorkPackageById(workPackageId);
		boolean flag = false;
		if (workPackage.getWorkPackageType() != null
				&& workPackage.getWorkPackageType().getExecutionTypeId() == 8)
			flag = true;
		int workPackagesTestCaseCount = 0;
		List<TestCaseList> productTestCases = null;
		if (flag) {
			productTestCases = testCaseListDAO.getTestCaseListByProductId(
					workPackage.getProductBuild().getProductVersion()
							.getProductMaster().getProductId(), null, null);
		} else {
			ExecutionTypeMaster executionTypeMaster = executionTypeMasterDAO
					.getExecutionTypeByExecutionTypeId(workPackage
							.getWorkPackageType().getExecutionTypeId());
			int executionId = -1;
			if (executionTypeMaster.getExecutionTypeId() == 7)
				executionId = 1;
			productTestCases = testCaseListDAO
					.getTestCaseListByProductIdByType(workPackage
							.getProductBuild().getProductVersion()
							.getProductMaster().getProductId(), executionId);
		}
		if (productTestCases == null || productTestCases.isEmpty()) {
			log.info("There are no testcases specified for the product for this workpackage : "
					+ workPackageId + " : " + workPackage.getName());
			return 0;
		} else {

			List<WorkPackageTestCase> workPackageTestCases = new ArrayList<WorkPackageTestCase>();
			WorkPackageTestCase workPackageTestCase = null;
			for (TestCaseList testCase : productTestCases) {
				workPackageTestCase = new WorkPackageTestCase();
				workPackageTestCase.setTestCase(testCase);
				workPackageTestCase.setWorkPackage(workPackage);
				workPackageTestCase.setCreatedDate(new Date(System
						.currentTimeMillis()));
				workPackageTestCase.setEditedDate(new Date(System
						.currentTimeMillis()));
				workPackageTestCase.setIsSelected(0);
				workPackageTestCase.setStatus("ACTIVE");

				workPackageTestCases.add(workPackageTestCase);
			}
			workPackagesTestCaseCount = workPackageDAO
					.addNewWorkPackageTestCases(workPackageTestCases);
		}
		return workPackagesTestCaseCount;
	}

	@Override
	@Transactional
	public WorkPackageTestCase updateWorkPackageTestCase(
			WorkPackageTestCase workPackageTestCase,
			JsonWorkPackageTestCase jsonWorkPackageTestCase) {

		WorkPackageTestCase workPackageTestCaseFromDB = workPackageDAO
				.getWorkPackageTestCaseById(workPackageTestCase.getId());

		WorkPackage workPackage = null;
		WorkPackageTestCase updatedWorkPackageTestCase = null;
		List<WorkPackageTestCaseExecutionPlan> currentWorkPackageTestCaseExecutionPlanList = null;
		workPackage = workPackageDAO.getWorkPackageById(workPackageTestCase
				.getWorkPackage().getWorkPackageId());
		TestCaseList testCaseList = workPackageTestCase.getTestCase();

		if (workPackageTestCaseFromDB.getIsSelected() == workPackageTestCase
				.getIsSelected()) {
			log.info("No change in Test case selection status. Check for env value selections change");
			updatedWorkPackageTestCase = workPackageTestCase;
		} else {

			log.info("Updated Test case selection status : "
					+ workPackageTestCase.getIsSelected());
			// There is a change in the is selected value. Update the test
			// package first.
			workPackageTestCase.setWorkPackage(workPackage);
			workPackageTestCase.setTestCase(testCaseList);
			workPackageTestCaseFromDB.setIsSelected(workPackageTestCase
					.getIsSelected());
			updatedWorkPackageTestCase = workPackageDAO
					.updateWorkPackageTestCase(workPackageTestCaseFromDB);
			if (workPackageTestCase.getIsSelected() == 1) {
				if(workPackageTestCase.getWorkPackage().getWorkFlowEvent() != null && workPackageTestCase.getWorkPackage().getWorkFlowEvent().getWorkFlow() != null){

					if (workPackageTestCase.getWorkPackage().getWorkFlowEvent()
							.getWorkFlow().getStageId() != IDPAConstants.WORKFLOW_STAGE_ID_PLANNING
							&& workPackageTestCase.getWorkPackage()
									.getWorkFlowEvent().getWorkFlow()
									.getStageValue() < 20) {
						WorkFlow workFlow = workPackageDAO
								.getWorkFlowByEntityIdStageId(
										IDPAConstants.WORKPACKAGE_ENTITY_ID,
										IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
						WorkFlowEvent workFlowEvent = new WorkFlowEvent();
						workFlowEvent.setEventDate(DateUtility.getCurrentTime());
						workFlowEvent.setRemarks("Planning Workapckage :"
								+ workPackage.getName());
						UserList user = userListDAO
								.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
						workFlowEvent.setUser(user);
						workFlowEvent.setWorkFlow(workFlow);
						workPackage.setWorkFlowEvent(workFlowEvent);
						workPackageDAO.addWorkFlowEvent(workFlowEvent);
						workPackageDAO.updateWorkPackage(workPackage);
					}	
				}
				workPackageDAO.mapWorkpackageWithTestCase(workPackageTestCase
						.getWorkPackage().getWorkPackageId(), testCaseList
						.getTestCaseId(), "Add");

				List<WorkpackageRunConfiguration> runConfigurations = workPackageDAO
						.getWorkpackageRunConfigurationList(workPackageTestCase
								.getWorkPackage().getWorkPackageId(), null,
								"testcase");
				List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate = new ArrayList<WorkPackageTestCaseExecutionPlan>();

				if (runConfigurations != null && !runConfigurations.isEmpty()) {
					for (WorkpackageRunConfiguration rc : runConfigurations) {

						// Create job
						TestRunJob testRunJob = workPackageDAO
								.getTestRunJobByWP(workPackage,
										rc.getRunconfiguration());
						if (testRunJob != null) {
							workPackageDAO.mapTestRunJobTestCase(testRunJob
									.getTestRunJobId(), workPackageTestCase
									.getTestCase().getTestCaseId(), "Add");
						} else {
							addTestRunJob(rc.getRunconfiguration(), null,
									workPackage, null);
							testRunJob = workPackageDAO.getTestRunJobByWP(
									workPackage, rc.getRunconfiguration());
							workPackageDAO.mapTestRunJobTestCase(testRunJob
									.getTestRunJobId(), workPackageTestCase
									.getTestCase().getTestCaseId(), "Add");
							if (testRunJob != null) {
								if (testRunJob.getTestRunJobId() != null)
									mongoDBService
											.addTestRunJobToMongoDB(testRunJob
													.getTestRunJobId());
							}

						}
						WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
						workPackageTestCaseExecutionPlan
								.setTestCase(workPackageTestCase.getTestCase());
						workPackageTestCaseExecutionPlan
								.setWorkPackage(workPackage);
						workPackageTestCaseExecutionPlan
								.setPlannedExecutionDate(workPackage
										.getPlannedStartDate());
						workPackageTestCaseExecutionPlan
								.setRunConfiguration(rc);
						workPackageTestCaseExecutionPlan.setExecutionStatus(3);
						workPackageTestCaseExecutionPlan.setIsExecuted(0);
						workPackageTestCaseExecutionPlan
								.setSourceType("TestCase");
						workPackageTestCaseExecutionPlan.setStatus(1);
						workPackageTestCaseExecutionPlan
								.setTestRunJob(testRunJob);

						ExecutionPriority executionPriority = null;
						log.info("testcase prority>>>"
								+ workPackageTestCase.getTestCase()
										.getTestCasePriority());
						if (workPackageTestCase.getTestCase()
								.getTestCasePriority() != null) {
							executionPriority = workPackageDAO
									.getExecutionPriorityByName(CommonUtility
											.getExecutionPriority(workPackageTestCase
													.getTestCase()
													.getTestCasePriority()
													.getPriorityName()));
						} else {
							executionPriority = workPackageDAO
									.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
						}
						workPackageTestCaseExecutionPlan
								.setExecutionPriority(executionPriority);

						TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
						testCaseExecutionResult.setResult("");
						testCaseExecutionResult.setComments("");
						testCaseExecutionResult.setDefectsCount(0);
						testCaseExecutionResult.setDefectIds("");
						testCaseExecutionResult.setIsApproved(0);
						testCaseExecutionResult.setIsReviewed(0);
						testCaseExecutionResult.setObservedOutput("");

						// Commented the following statements when remove the
						// WorkPackageTestcaseExecutionId column from Test Case
						// Execution Result table. By: Logeswari, On :
						// 11-Feb-2015
						testCaseExecutionResult
								.setWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
						workPackageTestCaseExecutionPlan
								.setTestCaseExecutionResult(testCaseExecutionResult);
						workPackageTestCaseExecutionPlanListForUpdate
								.add(workPackageTestCaseExecutionPlan);
					}
					addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
				}
			}

			// If the testcase was previously selected and now not selected,
			// then clear all workpackagetestcaseexecutionplan rows
			// as these not be there in the execution plan
			if (workPackageTestCase.getIsSelected() == 0) {
				try {
					List<WorkpackageRunConfiguration> runConfigurations = workPackageDAO
							.getWorkpackageRunConfigurationList(
									workPackageTestCase.getWorkPackage()
											.getWorkPackageId(), null,
									"testcase");
					if (runConfigurations != null
							&& !runConfigurations.isEmpty()) {
						for (WorkpackageRunConfiguration rc : runConfigurations) {
							// Create job
							TestRunJob testRunJob = workPackageDAO
									.getTestRunJobByWP(workPackage,
											rc.getRunconfiguration());
							workPackageDAO.mapTestRunJobTestCase(
									testRunJob.getTestRunJobId(),
									testCaseList.getTestCaseId(), "Remove");
							if (testRunJob.getRunConfiguration()
									.getRunconfigId() == rc
									.getRunconfiguration().getRunconfigId()) {
								if (testRunJob.getTestCaseListSet().isEmpty()
										&& testRunJob.getTestSuiteSet()
												.isEmpty()
										&& testRunJob.getFeatureSet().isEmpty()) {
									environmentDAO.deleteTestRunJob(workPackage
											.getWorkPackageId(), rc
											.getRunconfiguration()
											.getRunconfigId(), null);
								}
							}
						}

					}
					log.info("Clearing all test cases from plan as test case has been de-selected : "
							+ workPackageTestCase.getIsSelected());
					currentWorkPackageTestCaseExecutionPlanList = workPackageDAO
							.getWorkPackageTestcaseExecutionPlanByWorkPackageId(
									workPackage.getWorkPackageId().intValue(),
									workPackageTestCase.getTestCase()
											.getTestCaseId().intValue(), -1,
									-1, "TestCase");
					workPackageDAO.mapWorkpackageWithTestCase(
							workPackageTestCase.getWorkPackage()
									.getWorkPackageId(), testCaseList
									.getTestCaseId(), "Remove");
					workPackageDAO
							.deleteWorkPackageTestcaseExecutionPlan(currentWorkPackageTestCaseExecutionPlanList);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return updatedWorkPackageTestCase;
			} else {

				log.info("Since the test case is now selected, continue to other options evaluation");
			}
		}
		return updatedWorkPackageTestCase;
	}

	private List<List<WorkPackageTestCaseExecutionPlan>> updateTestCasePlansForEnvironment(
			String environmentSelection,
			Environment environment,
			int environmentIndex,
			int environmentsCount,
			TestCaseList testCase,
			WorkPackage workPackage,
			WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan,
			List<List<WorkPackageTestCaseExecutionPlan>> workPackageTestCaseExecutionPlanActions,
			WorkPackageTestCase workPackageTestCaseFromDB) {

		boolean testPlanExists = false;
		ProductLocale localeSel = null;
		if (workPackageTestCaseExecutionPlan == null) {
			testPlanExists = false;
		} else {
			testPlanExists = true;

		}


		boolean addTestPlan = false; // True -> Add, False -> Delete
		if (environmentIndex <= environmentsCount) {
			if (environmentSelection == null
					|| environmentSelection.equalsIgnoreCase("0")) {
				addTestPlan = false;
			} else {
				addTestPlan = true;
			}

			// }
			// 
			// 

			if ((addTestPlan)) {
				if (!testPlanExists) {

					if (environmentSelection == null
							|| environmentSelection.equals("0"))
						environment = null;
					boolean flag = workPackageDAO.checkExists(testCase,
							workPackage, environment);
					// 

					if (!flag) {
						// 

						workPackageTestCaseExecutionPlan = createWorkPackageTestCaseExecutionPlan(
								testCase, workPackage, environment, localeSel,
								workPackageTestCaseFromDB);
						workPackageTestCaseExecutionPlanActions.get(0).clear();
						workPackageTestCaseExecutionPlanActions.get(0).add(
								workPackageTestCaseExecutionPlan);
						workPackageDAO
								.addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanActions
										.get(0));
					}

				} else {
					if (workPackageTestCaseExecutionPlan == null) {
						boolean flag = workPackageDAO.checkExists(testCase,
								workPackage, environment);
						if (!flag) {
							// 

							workPackageTestCaseExecutionPlan = createWorkPackageTestCaseExecutionPlan(
									testCase, workPackage, environment,
									localeSel, workPackageTestCaseFromDB);
							workPackageTestCaseExecutionPlanActions.get(0)
									.clear();
							workPackageTestCaseExecutionPlanActions.get(0).add(
									workPackageTestCaseExecutionPlan);
							workPackageDAO
									.addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanActions
											.get(0));
						}

					}

					else if (workPackageTestCaseExecutionPlan != null) {
						boolean flag = workPackageDAO.checkExists(testCase,
								workPackage, environment);
						if (!flag) {
							// 

							workPackageTestCaseExecutionPlan = createWorkPackageTestCaseExecutionPlan(
									testCase, workPackage, environment,
									localeSel, workPackageTestCaseFromDB);
							workPackageTestCaseExecutionPlanActions.get(0)
									.clear();
							workPackageTestCaseExecutionPlanActions.get(0).add(
									workPackageTestCaseExecutionPlan);
							workPackageDAO
									.addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanActions
											.get(0));
						}
					} else {
						// 
					}
				}

			} else if (addTestPlan && testPlanExists) {
				log.info("Test case needed. Already exists");
				// Do nothing
			} else if (!addTestPlan && testPlanExists) {
				log.info("Test case not for environment not needed. Removing the existing one");
				boolean flag = workPackageDAO.checkExists(testCase,
						workPackage, environment);
				if (flag) {
					workPackageDAO
							.deleteWorkPackageTestCaseExecutionPlan(workPackageDAO
									.getWorkpackageTestcaseExecutionPlanById(workPackageTestCaseExecutionPlan
											.getId()));
				}
			} else if (!addTestPlan && !testPlanExists) {
				log.info("No action");
				// Do Nothing

			}
		}
		return workPackageTestCaseExecutionPlanActions;
	}

	private WorkPackageTestCaseExecutionPlan getTestCasePlan(
			List<WorkPackageTestCaseExecutionPlan> currentWorkPackageTestCaseExecutionPlanList,
			TestCaseList testCase, Environment environment,
			WorkPackage workPackage) {

		if (currentWorkPackageTestCaseExecutionPlanList == null
				|| currentWorkPackageTestCaseExecutionPlanList.isEmpty())
			return null;
		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = null;
		for (WorkPackageTestCaseExecutionPlan testCaseInPlan : currentWorkPackageTestCaseExecutionPlanList) {
		}
		return null;
	}

	private WorkPackageTestCaseExecutionPlan createWorkPackageTestCaseExecutionPlan(
			TestCaseList testCase, WorkPackage workPackage,
			Environment environment, ProductLocale locale,
			WorkPackageTestCase workPackageTestCaseFromDB) {
		// 
		if (workPackageTestCaseFromDB.getIsSelected() == 0) {
			workPackageTestCaseFromDB.setIsSelected(1);
			workPackageDAO.updateWorkPackageTestCase(workPackageTestCaseFromDB);
		}

		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
		workPackageTestCaseExecutionPlan.setTestCase(testCase);
		workPackageTestCaseExecutionPlan.setWorkPackage(workPackage);
		workPackageTestCaseExecutionPlan.setExecutionStatus(3);
		workPackageTestCaseExecutionPlan.setIsExecuted(0);
		ExecutionPriority executionPriority = null;
		if (testCase.getTestCasePriority() != null)
			executionPriority = workPackageDAO
					.getExecutionPriorityByName(CommonUtility
							.getExecutionPriority(testCase
									.getTestCasePriority().getPriorityName()));
		else
			executionPriority = workPackageDAO
					.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);

		workPackageTestCaseExecutionPlan
				.setExecutionPriority(executionPriority);

		TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
		testCaseExecutionResult.setResult("");
		testCaseExecutionResult.setComments("");
		testCaseExecutionResult.setDefectsCount(0);
		testCaseExecutionResult.setDefectIds("");
		testCaseExecutionResult.setIsApproved(0);
		testCaseExecutionResult.setIsReviewed(0);
		testCaseExecutionResult.setObservedOutput("");

		// Adding test step details

		// Commented the following statements when remove the
		// WorkPackageTestcaseExecutionId column from Test Case Execution Result
		// table. By: Logeswari, On : 11-Feb-2015
		testCaseExecutionResult
				.setWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
		workPackageTestCaseExecutionPlan
				.setTestCaseExecutionResult(testCaseExecutionResult);
		return workPackageTestCaseExecutionPlan;
	}

	private WorkPackageTestCaseExecutionPlan updateWorkPackageTestCaseExecutionPlan(
			WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan,
			TestCaseList testCase, WorkPackage workPackage,
			Environment environment, ProductLocale locale) {
		return workPackageTestCaseExecutionPlan;
	}

	@Override
	@Transactional
	public List<WorkPackage> list() {
		return workPackageDAO.list();
	}

	@Override
	@Transactional
	public List<WorkPackage> list(int startIndex, int pageSize) {
	
		return null;
	}

	@Override
	@Transactional
	public List<WorkPackage> listWorkPackages(int productBuildId, int status) {
		return workPackageDAO.listWorkPackages(productBuildId, status);
	}

	@Override
	@Transactional
	public List<WorkPackage> listWorkPackages(int productBuildId) {
		return workPackageDAO.listWorkPackages(productBuildId);
	}

	@Override
	@Transactional
	public void addWorkPackage(WorkPackage workPackage) {
		ProductBuild productBuild = productBuildDAO.getByProductBuildId(workPackage.getProductBuild().getProductBuildId(), 0);
		workPackage.setProductBuild(productBuild);
		workPackageDAO.addWorkPackage(workPackage);
	}

	@Override
	@Transactional
	public void updateWorkPackage(WorkPackage workPackage) {
		workPackageDAO.updateWorkPackage(workPackage);
	}

	@Override
	@Transactional
	public void deleteWorkPackage(int workPackageId) {
		log.info("WPSImpl:" + workPackageId);

		WorkPackage workPackage = workPackageDAO
				.getWorkPackageByProductBuildId(workPackageId);
		workPackageDAO.deleteWorkPackage(workPackage);
	}

	@Override
	@Transactional
	public WorkPackage getWorkPackageByWorkPackageName(String workPackageName) {
	
		return workPackageDAO.getWorkPackageByWorkPackageName(workPackageName);
	}

	@Override
	@Transactional
	public boolean isWorkPackageExistingByName(WorkPackage workPackage) {
	
		return false;
	}

	@Override
	@Transactional
	public WorkPackage getWorkPackageById(Integer workPackageId) {
	
		return workPackageDAO.getWorkPackageById(new Integer(workPackageId)
				.intValue());
	}

	@Override
	@Transactional
	public int getTotalRecordsOfWorkPackages() {
		return workPackageDAO.getTotalRecords();
	}

	@Override
	@Transactional
	public WorkPackage getWorkPackageById(int workPackageById) {
	
		return workPackageDAO.getWorkPackageById(workPackageById);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlan(
			int workPackageId, int jtStartIndex, int jtPageSize, UserList user,
			String plannedExecutionDate) {
	
		return workPackageDAO.listWorkPackageTestCaseExecutionPlan(
				workPackageId, jtStartIndex, jtPageSize, user,
				plannedExecutionDate);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlan(
			Map<String, String> searchString, int workPackageId,
			int jtStartIndex, int jtPageSize, String testLeadId,
			String testerId, String envId, int localeId,
			String plannedExecutionDate, String dcId, String executionPriority,
			int status) {
	
		return workPackageDAO.listWorkPackageTestCaseExecutionPlan(
				searchString, workPackageId, jtStartIndex, jtPageSize,
				testLeadId, testerId, envId, localeId, plannedExecutionDate,
				dcId, executionPriority, status);
	}

	@Override
	@Transactional
	public List<JsonWorkPackageTestCaseExecutionPlan> listJsonWorkPackageTestCaseExecutionPlan(
			Map<String, String> searchString, int workPackageId,
			int jtStartIndex, int jtPageSize, String testLeadId,
			String testerId, String envId, int localeId,
			String plannedExecutionDate, String dcId, String executionPriority,
			int status) {
		return workPackageDAO.listJsonWorkPackageTestCaseExecutionPlan(
				searchString, workPackageId, jtStartIndex, jtPageSize,
				testLeadId, testerId, envId, localeId, plannedExecutionDate,
				dcId, executionPriority, status);
	}

	@Override
	@Transactional
	public List<Environment> getWorkPackageTestCasesExecutionPlanEnvironments(
			int workPackageId, int testCaseId) {
	
		return workPackageDAO.getWorkPackageTestCasesExecutionPlanEnvironments(
				workPackageId, testCaseId);
	}

	@Override
	@Transactional
	public int totalRecordsCountForWorkPackageTestCasesExecutionPlan(
			int workPackageId, UserList user) {
	
		return workPackageDAO
				.totalRecordsCountForWorkPackageTestCaseExecutionPlan(
						workPackageId, user);
	}

	@Override
	@Transactional
	public List<UserList> userListByRole(String role) {
	
		return workPackageDAO.userListByRole(role);
	}

	@Override
	@Transactional
	public int getTotalRecordsOfUserByRole(String role) {
	
		return workPackageDAO.getTotalRecordsOfUserByRole(role);
	}

	@Override
	@Transactional
	public void updateWorkPackageTestCaseExecutionPlan(
			WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlanFromUI) {
	
		workPackageDAO
				.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlanFromUI);
	}

	@Override
	@Transactional
	public Environment getEnvironmentById(int environmentId) {
	
		return workPackageDAO.getEnvironmentById(environmentId);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanByWorkPackageId(
			int workPackageId) {
	
		return workPackageDAO
				.listWorkPackageTestCasesExecutionPlanByWorkPackageId(workPackageId);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlanByTesterId(
			int testerId, int workPackageId, int jtStartIndex, int jtPageSize) {
	
		return workPackageDAO.listWorkPackageTestCaseExecutionPlanByTester(
				testerId, workPackageId, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public int totalRecordsCountForWorkPackageTestCaseExecutionPlanByTesterId(
			int testerId, int workPackageId) {
	
		return workPackageDAO
				.totalRecordsCountForWorkPackageTestCaseExecutionPlanByTesterId(
						testerId, workPackageId);
	}

	@Override
	@Transactional
	public List<JsonWorkPackageDemandProjection> listWorkPackageDemandProjection(
			int workPackageId, int weekNo) {
	

		Date startDate = DateUtility.getDateForDayOfWeek(weekNo, 0);
		log.info("startDate=" + startDate);
		Date endDate = DateUtility.getDateForDayOfWeek(weekNo, 6);
		log.info("endDate=" + endDate);

		List<WorkPackageDemandProjectionDTO> workPackageDemandProjectionDTOList = workPackageDAO.listWorkPackageDemandProjection(workPackageId, null,startDate, endDate);
		
		WorkPackage workPackage = workPackageDAO.getWorkPackageById(workPackageId);
		List<WorkShiftMaster> workShifts = workShiftMasterDAO.listWorkShiftsByTestFactoryId(workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId());
		if (workShifts == null || workShifts.size() <= 0) {
			log.info("No workshifts specified for the TestFactory");
			return null;
		}
		List<JsonWorkPackageDemandProjection> jsonWorkPackageDemandProjections = new ArrayList<JsonWorkPackageDemandProjection>();
		Map<Integer, JsonWorkPackageDemandProjection> jsonWorkPackageDemandProjectionsMap = new HashMap<Integer, JsonWorkPackageDemandProjection>();
		JsonWorkPackageDemandProjection jsonWorkPackageDemandProjectionShift = null;
		int counter = 1;
		for (WorkShiftMaster workShift : workShifts) {
			jsonWorkPackageDemandProjectionShift = new JsonWorkPackageDemandProjection();
			jsonWorkPackageDemandProjectionShift.setWpDemandProjectionId(counter++);
			jsonWorkPackageDemandProjectionShift.setWorkPackageId(workPackageId);
			jsonWorkPackageDemandProjectionShift.setShiftId(workShift.getShiftId());
			jsonWorkPackageDemandProjectionShift.setShiftName(workShift.getShiftName());
			jsonWorkPackageDemandProjectionShift.setWeekNo(weekNo);

			jsonWorkPackageDemandProjectionsMap.put(jsonWorkPackageDemandProjectionShift.getShiftId(),jsonWorkPackageDemandProjectionShift);
		}
		for (WorkPackageDemandProjectionDTO demandProjection : workPackageDemandProjectionDTOList) {

			jsonWorkPackageDemandProjectionShift = jsonWorkPackageDemandProjectionsMap.get(demandProjection.getShiftId());
			if (jsonWorkPackageDemandProjectionShift != null) {log.info("Shift : " + demandProjection.getShiftId()+ " available in map:::   "+ demandProjection.getWorkDate());
				jsonWorkPackageDemandProjectionShift.setWorkDate(DateUtility.sdfDateformatWithOutTime(demandProjection.getWorkDate()));
				int dayOfWeek = DateUtility.getDayOfWeek(demandProjection
						.getWorkDate());
				log.info("dayOfWeek : " + dayOfWeek);
				jsonWorkPackageDemandProjectionShift = loadResourceDemand(
						jsonWorkPackageDemandProjectionShift,
						demandProjection.getResourceCount(), dayOfWeek);
				jsonWorkPackageDemandProjectionsMap.put(
						jsonWorkPackageDemandProjectionShift.getShiftId(),
						jsonWorkPackageDemandProjectionShift);
			}
		}
		jsonWorkPackageDemandProjections = new ArrayList<JsonWorkPackageDemandProjection>(
				jsonWorkPackageDemandProjectionsMap.values());

		return jsonWorkPackageDemandProjections;
	}

	private JsonWorkPackageDemandProjection setDateNames(
			JsonWorkPackageDemandProjection jsonWorkPackageDemandProjectionShift,
			List<String> dateNames) {

		log.info("Ist Date : " + dateNames.get(0));
		return jsonWorkPackageDemandProjectionShift;
	}

	private JsonWorkPackageDemandProjection loadResourceDemand(
			JsonWorkPackageDemandProjection jsonWorkPackageDemandProjectionShift,
			Float resourceCount, int dayOfWeek) {
		log.info("dayOfWeek=" + dayOfWeek);
		log.info("day of the week" + DateUtility.getDateNamesOfWeek(dayOfWeek));
		log.info("resourceCount=" + resourceCount);
		switch (dayOfWeek) {
		// Please dont change this ordering of case numbers
		case 1: // Sunday
			jsonWorkPackageDemandProjectionShift
					.setDay7ResourceCount(resourceCount);
			break;

		case 2: // Monday
			jsonWorkPackageDemandProjectionShift
					.setDay1ResourceCount(resourceCount);
			break;
		case 3:
			jsonWorkPackageDemandProjectionShift
					.setDay2ResourceCount(resourceCount);
			break;
		case 4:
			jsonWorkPackageDemandProjectionShift
					.setDay3ResourceCount(resourceCount);
			break;
		case 5:
			jsonWorkPackageDemandProjectionShift
					.setDay4ResourceCount(resourceCount);
			break;
		case 6:
			jsonWorkPackageDemandProjectionShift
					.setDay5ResourceCount(resourceCount);
			break;
		case 7:
			jsonWorkPackageDemandProjectionShift
					.setDay6ResourceCount(resourceCount);
			break;
		}

		return jsonWorkPackageDemandProjectionShift;
	}

	@Override
	@Transactional
	public int totalRecordsCountForWorkPackageDemandProjection(int workPackageId) {
	
		return workPackageDAO
				.totalRecordsCountForWorkPackageDemandProjection(workPackageId);
	}

	@Override
	@Transactional
	public JsonWorkPackageDemandProjection updateWorkPackageDemandProjection(
			JsonWorkPackageDemandProjection jsonWorkPackageDemandProjection) {

		log.info("updateWorkPackageDemandProjection in serviceImpl");

		log.info("ShiftId" + jsonWorkPackageDemandProjection.getShiftId());
		log.info("WorkpackageId"
				+ jsonWorkPackageDemandProjection.getWorkPackageId());
		log.info("ResourceCount"
				+ jsonWorkPackageDemandProjection.getDay4ResourceCount());

		int weekNo = jsonWorkPackageDemandProjection.getWeekNo();
		Date workDate = DateUtility.getDateForDayOfWeek(weekNo, 1);
		log.info("Date" + workDate);
		WorkShiftMaster workShiftMaster = new WorkShiftMaster();
		workShiftMaster
				.setShiftId(jsonWorkPackageDemandProjection.getShiftId());
		WorkPackage workPackage = new WorkPackage();
		workPackage.setWorkPackageId(jsonWorkPackageDemandProjection
				.getWorkPackageId());

		WorkPackageDemandProjection workPackageDemandProjection = null;
		List<WorkPackageDemandProjection> workPackageDemandProjections = new ArrayList<WorkPackageDemandProjection>();

		// Monday
		workPackageDemandProjection = new WorkPackageDemandProjection();
		workPackageDemandProjection
				.setResourceCount(jsonWorkPackageDemandProjection
						.getDay1ResourceCount());
		workDate = DateUtility.getDateForDayOfWeek(weekNo, 0);
		workPackageDemandProjection.setWorkDate(workDate);
		workPackageDemandProjection.setWorkShiftMaster(workShiftMaster);
		workPackageDemandProjection.setWorkPackage(workPackage);
		workPackageDemandProjections.add(workPackageDemandProjection);
		// Tuesday
		workPackageDemandProjection = new WorkPackageDemandProjection();
		workPackageDemandProjection
				.setResourceCount(jsonWorkPackageDemandProjection
						.getDay2ResourceCount());
		workDate = DateUtility.getDateForDayOfWeek(weekNo, 1);
		workPackageDemandProjection.setWorkDate(workDate);
		workPackageDemandProjection.setWorkShiftMaster(workShiftMaster);
		workPackageDemandProjection.setWorkPackage(workPackage);
		workPackageDemandProjections.add(workPackageDemandProjection);
		// Wednesday
		workPackageDemandProjection = new WorkPackageDemandProjection();
		workPackageDemandProjection
				.setResourceCount(jsonWorkPackageDemandProjection
						.getDay3ResourceCount());
		workDate = DateUtility.getDateForDayOfWeek(weekNo, 2);
		workPackageDemandProjection.setWorkDate(workDate);
		workPackageDemandProjection.setWorkShiftMaster(workShiftMaster);
		workPackageDemandProjection.setWorkPackage(workPackage);
		workPackageDemandProjections.add(workPackageDemandProjection);
		// Thursday
		workPackageDemandProjection = new WorkPackageDemandProjection();
		workPackageDemandProjection
				.setResourceCount(jsonWorkPackageDemandProjection
						.getDay4ResourceCount());
		workDate = DateUtility.getDateForDayOfWeek(weekNo, 3);
		workPackageDemandProjection.setWorkDate(workDate);
		workPackageDemandProjection.setWorkShiftMaster(workShiftMaster);
		workPackageDemandProjection.setWorkPackage(workPackage);
		workPackageDemandProjections.add(workPackageDemandProjection);
		// Friday
		workPackageDemandProjection = new WorkPackageDemandProjection();
		workPackageDemandProjection
				.setResourceCount(jsonWorkPackageDemandProjection
						.getDay5ResourceCount());
		workDate = DateUtility.getDateForDayOfWeek(weekNo, 4);
		workPackageDemandProjection.setWorkDate(workDate);
		workPackageDemandProjection.setWorkShiftMaster(workShiftMaster);
		workPackageDemandProjection.setWorkPackage(workPackage);
		workPackageDemandProjections.add(workPackageDemandProjection);
		// Saturday
		workPackageDemandProjection = new WorkPackageDemandProjection();
		workPackageDemandProjection
				.setResourceCount(jsonWorkPackageDemandProjection
						.getDay6ResourceCount());
		workDate = DateUtility.getDateForDayOfWeek(weekNo, 5);
		workPackageDemandProjection.setWorkDate(workDate);
		workPackageDemandProjection.setWorkShiftMaster(workShiftMaster);
		workPackageDemandProjection.setWorkPackage(workPackage);
		workPackageDemandProjections.add(workPackageDemandProjection);
		// Sunday
		workPackageDemandProjection = new WorkPackageDemandProjection();
		workPackageDemandProjection
				.setResourceCount(jsonWorkPackageDemandProjection
						.getDay7ResourceCount());
		workDate = DateUtility.getDateForDayOfWeek(weekNo, 6);
		workPackageDemandProjection.setWorkDate(workDate);
		workPackageDemandProjection.setWorkShiftMaster(workShiftMaster);
		workPackageDemandProjection.setWorkPackage(workPackage);
		workPackageDemandProjections.add(workPackageDemandProjection);

		// Update in DB
		List<WorkPackageDemandProjection> updatedWorkPackageDemandProjections = workPackageDAO
				.updateWorkPackageDemandProjection(workPackageDemandProjections);

		return jsonWorkPackageDemandProjection;
		// return
	}

	@Override
	@Transactional
	public TestCaseExecutionResult getTestCaseExecutionResultByID(
			int testCaseExecutionResultId) {
		return workPackageDAO
				.getTestCaseExecutionResultByID(testCaseExecutionResultId);
	}

	@Override
	@Transactional
	public List<TestCaseExecutionResult> listAllTestCaseExecutionResult(
			int startIndex, int pageSize, Date startDate, Date endDate) {
		return workPackageDAO.listAllTestCaseExecutionResult(startIndex,
				pageSize, startDate, endDate);
	}

	@Override
	@Transactional
	public List<TestRunJob> listAllTestRunJob(int startIndex, int pageSize,
			Date startDate, Date endDate) {
		return workPackageDAO.listAllTestRunJob(startIndex, pageSize,
				startDate, endDate);
	}

	@Override
	@Transactional
	public Integer countAllTestCaseExecutionResult(Date startDate, Date endDate) {
		return workPackageDAO.countAllTestCaseExecutionResult(startDate,
				endDate);
	}

	@Override
	@Transactional
	public Integer countAllTestRunJob(Date startDate, Date endDate) {
		return workPackageDAO.countAllTestRunJob(startDate, endDate);
	}

	@Override
	@Transactional
	public int seedWorkPackageWithNewTestCases(List<TestCaseList> newTestCases,
			int workPackageId) {
	
		int workPackagesTestCaseCount = 0;
		WorkPackage workPackage = workPackageDAO
				.getWorkPackageById(workPackageId);
		List<WorkPackageTestCase> workPackageTestCases = new ArrayList<WorkPackageTestCase>();
		WorkPackageTestCase workPackageTestCase = null;
		for (TestCaseList testCase : newTestCases) {
			workPackageTestCase = new WorkPackageTestCase();
			workPackageTestCase.setTestCase(testCase);
			workPackageTestCase.setWorkPackage(workPackage);
			workPackageTestCase.setCreatedDate(new Date(System
					.currentTimeMillis()));
			workPackageTestCase.setEditedDate(new Date(System
					.currentTimeMillis()));
			workPackageTestCase.setIsSelected(0);
			workPackageTestCase.setStatus("ACTIVE");
			workPackageTestCases.add(workPackageTestCase);
		}
		workPackagesTestCaseCount = workPackageDAO
				.addNewWorkPackageTestCases(workPackageTestCases);
		return workPackagesTestCaseCount;
	}

	@Override
	@Transactional
	public List<WorkShiftMaster> listAllWorkShift() {
	
		return workPackageDAO.listAllWorkShift();
	}

	@Override
	@Transactional
	public WorkShiftMaster getWorkShiftById(int shiftId) {
	
		return workPackageDAO.getWorkShiftById(shiftId);
	}

	@Override
	@Transactional
	public WorkShiftMaster getWorkShiftByName(String shiftName) {
	
		return workPackageDAO.getWorkShiftByName(shiftName);
	}

	@Override
	@Transactional
	public List<ProductUserRole> userListByProductRole(Integer productId,
			String userRoleId) {
	
		return workPackageDAO.userListByProductRole(productId, userRoleId);
	}

	@Override
	@Transactional
	public Integer getProductIdByWorkpackage(int workPackageId) {
	
		return workPackageDAO.getProductIdByWorkpackage(workPackageId);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanTestLead(
			int workPackageId, int jtStartIndex, int jtPageSize, UserList user) {
	
		return workPackageDAO.listWorkPackageTestCasesExecutionPlanTestLead(
				workPackageId, jtStartIndex, jtPageSize, user);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanByEnv(
			int workPackageId, int jtStartIndex, int jtPageSize, int envId) {
	
		return workPackageDAO.listWorkPackageTestCasesExecutionPlanByEnv(
				workPackageId, jtStartIndex, jtPageSize, envId);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanTestLeadByEnv(
			int workPackageId, int jtStartIndex, int jtPageSize, UserList user,
			int envId) {
	
		return workPackageDAO
				.listWorkPackageTestCasesExecutionPlanTestLeadByEnv(
						workPackageId, jtStartIndex, jtPageSize, user, envId);
	}

	@Override
	@Transactional
	public List<JsonWorkPackageDailyPlan> listWorkPackageDemandDayWisePlan(
			int workPackageId, int weekNo) {
	

		Date startDate = DateUtility.getDateForDayOfWeek(weekNo, 0);
		log.info("startDate=" + startDate);
		Date endDate = DateUtility.getDateForDayOfWeek(weekNo, 6);
		log.info("endDate=" + endDate);
		Integer workShiftId = null;

		List<WorkPackageDayWisePlanDTO> dailyShiftPlans = workPackageDAO
				.listWorkPackageDayWisePlan(workPackageId, workShiftId,
						startDate, endDate);

		WorkPackage workPackage = workPackageDAO
				.getWorkPackageById(workPackageId);
		List<WorkShiftMaster> workShifts = workShiftMasterDAO
				.listWorkShiftsByTestFactoryId(workPackage.getProductBuild()
						.getProductVersion().getProductMaster()
						.getTestFactory().getTestFactoryId());
		if (workShifts == null || workShifts.size() <= 0) {
			log.info("No workshifts specified for the TestFactory");
			return null;
		}

		List<JsonWorkPackageDailyPlan> jsonWorkPackageDailyPlans = new ArrayList<JsonWorkPackageDailyPlan>();
		Map<String, JsonWorkPackageDailyPlan> jsonWorkPackageDailyPlansMap = new HashMap<String, JsonWorkPackageDailyPlan>();
		JsonWorkPackageDailyPlan jsonWorkPackageDailyPlanShift = null;
		int counter = 1;
		for (WorkShiftMaster workShift : workShifts) {
			jsonWorkPackageDailyPlanShift = new JsonWorkPackageDailyPlan();
			jsonWorkPackageDailyPlanShift.setDailyPlanId(counter++);
			;
			jsonWorkPackageDailyPlanShift.setWorkPackageId(workPackageId);
			jsonWorkPackageDailyPlanShift.setShiftId(workShift.getShiftId());
			jsonWorkPackageDailyPlanShift
					.setShiftName(workShift.getShiftName());
			jsonWorkPackageDailyPlanShift.setWeekNo(weekNo);

			jsonWorkPackageDailyPlansMap.put(
					jsonWorkPackageDailyPlanShift.getShiftName(),
					jsonWorkPackageDailyPlanShift);
		}

		for (WorkPackageDayWisePlanDTO dailyShiftPlan : dailyShiftPlans) {

			jsonWorkPackageDailyPlanShift = jsonWorkPackageDailyPlansMap
					.get(dailyShiftPlan.getShiftName());
			if (jsonWorkPackageDailyPlanShift != null) {

				int dayOfWeek = DateUtility.getDayOfWeek(dailyShiftPlan
						.getPlanDate());
				jsonWorkPackageDailyPlanShift = loadPlannedTestCasesCount(
						jsonWorkPackageDailyPlanShift,
						dailyShiftPlan.getPlannedTestCasesCount(), dayOfWeek);
				jsonWorkPackageDailyPlansMap.put(
						jsonWorkPackageDailyPlanShift.getShiftName(),
						jsonWorkPackageDailyPlanShift);
			}
		}
		jsonWorkPackageDailyPlans = new ArrayList<JsonWorkPackageDailyPlan>(
				jsonWorkPackageDailyPlansMap.values());
		return jsonWorkPackageDailyPlans;
	}

	private JsonWorkPackageDailyPlan loadPlannedTestCasesCount(
			JsonWorkPackageDailyPlan jsonWorkPackageDailyPlanShift,
			int testCasesCount, int dayOfWeek) {
		log.info("dayOfWeek=" + dayOfWeek);
		log.info("day of the week" + DateUtility.getDateNamesOfWeek(dayOfWeek));
		log.info("planned test cases : " + testCasesCount);
		switch (dayOfWeek) {
		// Please dont change this ordering of case numbers
		case 1: // Sunday
			jsonWorkPackageDailyPlanShift.setDay7ResourceCount(testCasesCount);
			break;

		case 2: // Monday
			jsonWorkPackageDailyPlanShift.setDay1ResourceCount(testCasesCount);
			break;
		case 3:
			jsonWorkPackageDailyPlanShift.setDay2ResourceCount(testCasesCount);
			break;
		case 4:
			jsonWorkPackageDailyPlanShift.setDay3ResourceCount(testCasesCount);
			break;
		case 5:
			jsonWorkPackageDailyPlanShift.setDay4ResourceCount(testCasesCount);
			break;
		case 6:
			jsonWorkPackageDailyPlanShift.setDay5ResourceCount(testCasesCount);
			break;
		case 7:
			jsonWorkPackageDailyPlanShift.setDay6ResourceCount(testCasesCount);
			break;
		}

		return jsonWorkPackageDailyPlanShift;
	}

	@Override
	@Transactional
	public Set<RunConfiguration> getEnvironmentMappedToWorkpackage(
			int workpackageId) {
	
		return workPackageDAO.getEnvironmentMappedToWorkpackage(workpackageId);
	}

	@Override
	public List<WorkPackage> listWorkPackagesByUserId(int userId) {
		return workPackageDAO
				.getWorkPackagesFromWorkPackageTestcaseExecutionPlanByUserId(userId);
	}

	@Override
	@Transactional
	public WorkPackage mapWorkpackageEnv(int workpackageId, int envId,
			String action) {
	
		return workPackageDAO.mapWorkpackageEnv(workpackageId, envId, action);
	}

	@Override
	@Transactional
	public Set<ProductLocale> getLocaleMappedToWorkpackage(int workpackageId) {
	
		return workPackageDAO.getLocaleMappedToWorkpackage(workpackageId);
	}

	@Override
	@Transactional
	public WorkPackage mapWorkpackageLocale(int workpackageId, int localeId,
			String action) {
	
		return workPackageDAO.mapWorkpackageLocale(workpackageId, localeId,
				action);
	}

	@Override
	@Transactional
	public WorkPackageTestCase updateWorkPackageTestCase(
			Set<WorkPackageTestCase> workPackageTestCaseList,
			List<Environment> environmentList,
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListFromDB,
			Integer workpackageId) {
		TestCaseList testCaseList = null;
		Set<TestCaseList> testCaseSet = null;
		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = null;
		List<WorkPackageTestCaseExecutionPlan> newTestCaseExecutionPlans = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		List<WorkPackageTestCaseExecutionPlan> updateTestCaseExecutionPlans = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		ProductLocale locale = null;
		WorkPackage workPackage = workPackageDAO
				.getWorkPackageById(workpackageId);
		testCaseSet = workPackage.getTestcaseList();
		ArrayList testcaseIdList = new ArrayList();
		for (TestCaseList testcase : testCaseSet) {
			testcaseIdList.add(testcase.getTestCaseId());
		}
		for (WorkPackageTestCase workPackageTestCase : workPackageTestCaseList) {
			testCaseList = workPackageTestCase.getTestCase();

			if (workPackageTestCase.getIsSelected() == 1) {

				for (Environment environment : environmentList) {
					if (workPackageTestCaseExecutionPlanListFromDB != null
							&& !workPackageTestCaseExecutionPlanListFromDB
									.isEmpty()) {

						for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlanFromDB : workPackageTestCaseExecutionPlanListFromDB) {
							Set<Environment> environments = workPackageTestCaseExecutionPlanFromDB
									.getEnvironmentList();
							if (workPackageTestCaseExecutionPlanFromDB
									.getWorkPackage()
									.getWorkPackageId()
									.equals(workPackageTestCase
											.getWorkPackage()
											.getWorkPackageId())) {

								if (!testcaseIdList
										.contains(workPackageTestCaseExecutionPlanFromDB
												.getTestCase().getTestCaseId())) {
									workPackageTestCaseExecutionPlan = createWorkPackageTestCaseExecutionPlan(
											workPackageTestCase.getTestCase(),
											workPackageTestCase
													.getWorkPackage(), null,
											null, workPackageTestCase);
									newTestCaseExecutionPlans
											.add(workPackageTestCaseExecutionPlan);
								} else if (workPackageTestCaseExecutionPlanFromDB
										.getTestCase()
										.getTestCaseId()
										.equals(workPackageTestCase
												.getTestCase().getTestCaseId())) {
									for (Environment environmentExist : environments) {
										if (environmentExist
												.getEnvironmentCategory()
												.getEnvironmentGroup()
												.getEnvironmentGroupId() == environment
												.getEnvironmentCategory()
												.getEnvironmentGroup()
												.getEnvironmentGroupId()) {
											workPackageTestCaseExecutionPlan = createWorkPackageTestCaseExecutionPlan(
													workPackageTestCase
															.getTestCase(),
													workPackageTestCase
															.getWorkPackage(),
													null, null,
													workPackageTestCase);
											newTestCaseExecutionPlans
													.add(workPackageTestCaseExecutionPlan);
										} else {
											updateTestCaseExecutionPlans
													.add(workPackageTestCaseExecutionPlanFromDB);
										}
									}
								} else {

								}
							}
						}
					} else {

						workPackageTestCaseExecutionPlan = createWorkPackageTestCaseExecutionPlan(
								workPackageTestCase.getTestCase(),
								workPackageTestCase.getWorkPackage(), null,
								locale, workPackageTestCase);
						newTestCaseExecutionPlans
								.add(workPackageTestCaseExecutionPlan);
					}

				}
			}
		}

		if (newTestCaseExecutionPlans == null
				|| newTestCaseExecutionPlans.isEmpty()) {
			log.info("New ones need not be added");
		} else {
			int result = workPackageDAO.addWorkPackageTestcaseExecutionPlan(
					newTestCaseExecutionPlans, environmentList);

			log.info("Created new testcase plans : "
					+ newTestCaseExecutionPlans.size());
		}

		if (updateTestCaseExecutionPlans == null
				|| updateTestCaseExecutionPlans.isEmpty()) {
			log.info("update ones need not be added");
		} else {

			if (newTestCaseExecutionPlans.isEmpty()) {

				for (Environment environment : environmentList) {
					for (WorkPackageTestCaseExecutionPlan updateTestCaseExecutionPlan : updateTestCaseExecutionPlans) {
						workPackageDAO
								.mapWorkPackageTestCaseExecutionPlanEnv(
										updateTestCaseExecutionPlan,
										environment, "Add");
					}
				}
			} else {
				Set<Environment> environmentSet = workPackage
						.getEnvironmentList();
				List<Environment> environmentFinalList = new ArrayList<Environment>();

				for (Environment environment : environmentList) {
					if (!environmentSet.contains(environment)) {
						environmentFinalList.add(environment);
					}
				}

				Set<Integer> environmenetGroupList = new HashSet<Integer>();
				for (Environment environment : environmentList) {
					environmenetGroupList.add(environment
							.getEnvironmentCategory().getEnvironmentGroup()
							.getEnvironmentGroupId());
				}

				for (Environment environment : environmentSet) {
					if (!environmenetGroupList.contains(environment
							.getEnvironmentCategory().getEnvironmentGroup()
							.getEnvironmentGroupId())) {
						environmentFinalList.add(environment);
					}
				}

				Set<Environment> environments = null;
				int count = 0;
				Set<Integer> envGid = new HashSet();
				Set<Integer> tid = new HashSet();
				Set<Integer> pid = new HashSet();
				tid.clear();
				envGid.clear();
				pid.clear();
				for (WorkPackageTestCaseExecutionPlan newTestCaseExecutionPlan : newTestCaseExecutionPlans) {

					for (Environment environment : environmentFinalList) {

						Environment environment1 = workPackageDAO
								.getEnvironmentById(environment
										.getEnvironmentId());
						if (envGid.isEmpty()) {
							workPackageDAO
									.mapWorkPackageTestCaseExecutionPlanEnv(
											newTestCaseExecutionPlan,
											environment1, "Add");
							envGid.add(environment1.getEnvironmentId());
							tid.add(newTestCaseExecutionPlan.getTestCase()
									.getTestCaseId());
							pid.add(newTestCaseExecutionPlan.getId());
							break;

						} else if (!envGid.isEmpty()
								&& envGid.contains(environment
										.getEnvironmentId())
								&& tid.contains(newTestCaseExecutionPlan
										.getTestCase().getTestCaseId())) {

						} else if (!envGid.isEmpty()
								&& !envGid.contains(environment
										.getEnvironmentId())
								&& !tid.contains(newTestCaseExecutionPlan
										.getTestCase())
								&& !pid.contains(newTestCaseExecutionPlan
										.getId())) {
							workPackageDAO
									.mapWorkPackageTestCaseExecutionPlanEnv(
											newTestCaseExecutionPlan,
											environment, "Add");
							envGid.add(environment.getEnvironmentId());
							tid.add(newTestCaseExecutionPlan.getTestCase()
									.getTestCaseId());
							pid.add(newTestCaseExecutionPlan.getId());
							break;
						} else if (!pid.contains(newTestCaseExecutionPlan
								.getId())) {
							envGid.clear();
							tid.clear();
							workPackageDAO
									.mapWorkPackageTestCaseExecutionPlanEnv(
											newTestCaseExecutionPlan,
											environment, "Add");
							envGid.add(environment.getEnvironmentId());
							tid.add(newTestCaseExecutionPlan.getTestCase()
									.getTestCaseId());
							pid.add(newTestCaseExecutionPlan.getId());
							break;
						}

					}

				}

			}

			log.info("Created new testcase plans updateTestCaseExecutionPlan : "
					+ updateTestCaseExecutionPlans.size());
		}

		return null;
	}

	@Override
	@Transactional
	public WorkPackageTestCase deleteWorkPackageTestCase(
			Set<WorkPackageTestCase> workPackageTestCaseList,
			List<Environment> environmentList,
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListFromDB) {
	
		log.info("workPackageTestCaseList--->" + workPackageTestCaseList.size());
		log.info("environmentList--->" + environmentList.size());

		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList = null;
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanDeleteList = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		List<WorkPackageTestCaseExecutionPlan> newTestCaseExecutionPlans = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		for (WorkPackageTestCaseExecutionPlan wptcep : workPackageTestCaseExecutionPlanListFromDB) {
			Set<Environment> envMappedWithTC = wptcep.getEnvironmentList();
			for (Environment env : environmentList) {
				if (envMappedWithTC.contains(env)) {
					workPackageDAO
							.deleteMappingWorkPackageTestCaseExecutionPlanEnv(
									wptcep.getId(), env.getEnvironmentId());
					workPackageDAO
							.deleteWorkPackageTestCaseExecutionPlan(wptcep);
				}
			}
		}

		return null;
	}

	@Override
	@Transactional
	public List<ProductLocale> getWorkPackageTestCasesExecutionPlanLocales(
			int workPackageId, int testCaseId) {
	
		return workPackageDAO.getWorkPackageTestCasesExecutionPlanLocales(
				workPackageId, testCaseId);
	}

	@Override
	@Transactional
	public List<JsonWorkPackageStatusSummary> listWorkPackageStatusSummary(
			List<ProductBuild> listOfProductBuilds,
			ProductUserRole productUserRole) {
		log.info("inside listWorkPackageStatusSummary() in service implementation");
		List<WorkPackageStatusSummaryDTO> listOfWorkPackageStatusSummaryDTO = new ArrayList<WorkPackageStatusSummaryDTO>();

		for (ProductBuild productBuild : listOfProductBuilds) {
			List<WorkPackage> listOfWorkPackages = workPackageDAO
					.listWorkPackages(productBuild.getProductBuildId());
			log.info("listOfWorkPackages size: " + listOfWorkPackages.size());
			if (listOfWorkPackages != null) {
				for (WorkPackage workPackage : listOfWorkPackages) {
					log.info("WorkPackages Id: "
							+ workPackage.getWorkPackageId());
					log.info("WorkPackages Name: " + workPackage.getName());
					List<WorkPackageStatusSummaryDTO> subListOfWorkPackageStatusSummaryDTO = new ArrayList<WorkPackageStatusSummaryDTO>();
					subListOfWorkPackageStatusSummaryDTO = workPackageDAO
							.workPackageStatusSummary(
									workPackage.getWorkPackageId(),
									productUserRole);
					log.info("subListOfWorkPackageStatusSummaryDTO size for WorkPackages Id: "
							+ workPackage.getWorkPackageId()
							+ " is : "
							+ subListOfWorkPackageStatusSummaryDTO.size());
					if (listOfWorkPackageStatusSummaryDTO.size() == 0) {
						listOfWorkPackageStatusSummaryDTO = subListOfWorkPackageStatusSummaryDTO;
					} else {
						listOfWorkPackageStatusSummaryDTO
								.addAll(subListOfWorkPackageStatusSummaryDTO);
					}
				}
			}
		}

		List<JsonWorkPackageStatusSummary> listOfJsonWorkPackageStatusSummary = new ArrayList<JsonWorkPackageStatusSummary>();
		log.info("************listOfWorkPackageStatusSummaryDTO size $$$$$$$$$$   "
				+ listOfWorkPackageStatusSummaryDTO.size());
		JsonWorkPackageStatusSummary jsonWorkPackageStatusSummary = null;

		for (WorkPackageStatusSummaryDTO workPackageSummaryDTO : listOfWorkPackageStatusSummaryDTO) {
			jsonWorkPackageStatusSummary = new JsonWorkPackageStatusSummary();
			if (workPackageSummaryDTO != null) {
				jsonWorkPackageStatusSummary
						.setWorkPackageId(workPackageSummaryDTO
								.getWorkPackageId());
				jsonWorkPackageStatusSummary
						.setWorkPackageName(workPackageSummaryDTO
								.getWorkPackageName());
				jsonWorkPackageStatusSummary
						.setSelectedEnvironmentsCount(workPackageSummaryDTO
								.getSelectedEnvironmentsCount());
				jsonWorkPackageStatusSummary
						.setSelectedLocalesCount(workPackageSummaryDTO
								.getSelectedLocalesCount());
				jsonWorkPackageStatusSummary
						.setSelectedTestCasesCount(workPackageSummaryDTO
								.getSelectedTestCasesCount());
				jsonWorkPackageStatusSummary
						.setTotalTestCaseForExecutionCount(workPackageSummaryDTO
								.getTotalTestCaseForExecutionCount());
				jsonWorkPackageStatusSummary
						.setCompletedTestCaseCount(workPackageSummaryDTO
								.getCompletedTestCaseCount());
				jsonWorkPackageStatusSummary
						.setNotCompletedTestCaseCount(workPackageSummaryDTO
								.getNotCompletedTestCaseCount());
				listOfJsonWorkPackageStatusSummary
						.add(jsonWorkPackageStatusSummary);
			} else {
				log.info("workPackageSummaryDTO Object is NULL");
			}

		}
		log.info("************listOfJsonWorkPackageStatusSummary size::::::::::::   "
				+ listOfJsonWorkPackageStatusSummary.size());
		return listOfJsonWorkPackageStatusSummary;
	}

	@Override
	@Transactional
	public WorkPackageTestCase updateWorkPackageTestCaseLocale(
			Set<WorkPackageTestCase> workPackageTestCaseList,
			List<ProductLocale> localeList,
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListFromDB) {
	
		log.info("workPackageTestCaseList--->" + workPackageTestCaseList.size());
		log.info("localeList--->" + localeList.size());
		TestCaseList testCaseList = null;
		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = null;
		Environment environment = null;
		List<WorkPackageTestCaseExecutionPlan> newTestCaseExecutionPlans = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		List<WorkPackageTestCaseExecutionPlan> updateTestCaseExecutionPlans = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		for (WorkPackageTestCase workPackageTestCase : workPackageTestCaseList) {
			testCaseList = workPackageTestCase.getTestCase();

			workPackageTestCase.setTestCase(testCaseList);
			workPackageTestCase.setIsSelected(1);
			workPackageDAO.updateWorkPackageTestCase(workPackageTestCase);

			for (ProductLocale locale : localeList) {
				if (workPackageTestCaseExecutionPlanListFromDB != null
						&& !workPackageTestCaseExecutionPlanListFromDB
								.isEmpty()) {
					for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlanFromDB : workPackageTestCaseExecutionPlanListFromDB) {
					}
				} else {
					workPackageTestCaseExecutionPlan = createWorkPackageTestCaseExecutionPlan(
							workPackageTestCase.getTestCase(),
							workPackageTestCase.getWorkPackage(), environment,
							locale, workPackageTestCase);
					newTestCaseExecutionPlans
							.add(workPackageTestCaseExecutionPlan);
				}
			}
		}

		if (newTestCaseExecutionPlans == null
				|| newTestCaseExecutionPlans.isEmpty()) {
			log.info("New ones need not be added");
		} else {

			int result = workPackageDAO
					.addWorkPackageTestcaseExecutionPlan(newTestCaseExecutionPlans);
			log.info("Created new testcase plans : "
					+ newTestCaseExecutionPlans.size());
		}

		if (updateTestCaseExecutionPlans == null
				|| updateTestCaseExecutionPlans.isEmpty()) {
			log.info("New ones need not be updated");
		} else {

			int result = workPackageDAO
					.updateWorkPackageTestcaseExecutionPlan(updateTestCaseExecutionPlans);
			log.info("updated new testcase plans : "
					+ updateTestCaseExecutionPlans.size());
		}
		return null;
	}

	@Override
	@Transactional
	public WorkPackageTestCase deleteWorkPackageTestCaseLocale(
			Set<WorkPackageTestCase> workPackageTestCaseList,
			List<ProductLocale> localeList,
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListFromDB) {
	
		log.info("workPackageTestCaseList--->" + workPackageTestCaseList.size());
		log.info("environmentList--->" + localeList.size());

		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList = null;
		List<WorkPackageTestCaseExecutionPlan> newTestCaseExecutionPlans = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		for (WorkPackageTestCase workPackageTestCase : workPackageTestCaseList) {
			for (ProductLocale locale : localeList) {
				workPackageTestCaseExecutionPlanList = workPackageDAO
						.getWorkPackageTestcaseExecutionPlanByWorkPackageIdByLocale(
								workPackageTestCase.getWorkPackage()
										.getWorkPackageId(),
								workPackageTestCase.getTestCase()
										.getTestCaseId(), locale
										.getProductLocaleId(),
								workPackageTestCase);
				workPackageDAO
						.deleteWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanList);
			}
		}

		return null;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listCompletedOrNotCompletedTestCasesOfworkPackage(
			int workPackageId, int jtStartIndex, int jtPageSize,
			int isExecuted, ProductUserRole productUserRole) {
		return workPackageDAO
				.listCompletedOrNotCompletedTestCasesOfworkPackage(
						workPackageId, jtStartIndex, jtPageSize, isExecuted,
						productUserRole);
	}

	@Override
	@Transactional
	public void updateWorkPackageTestCaseExecutionPlan(String[] wptcepLists,
			Integer testerId, Integer testLeadId, String plannedExecutionDate,
			Integer executionPriorityId, Integer shiftId) {
	
		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = null;
		ExecutionPriority executionPriority = null;

		try {
			for (String Id : wptcepLists) {
				log.info("ID=-===========>" + Id);
				if (Id != null) {
					workPackageTestCaseExecutionPlan = workPackageDAO
							.getWorkpackageTestcaseExecutionPlanById(Integer
									.parseInt(Id));

					if (testerId != null && testerId != -1) {
						UserList tester = userListDAO.getByUserId(testerId);
						workPackageTestCaseExecutionPlan.setTester(tester);
					}
					if (testLeadId != null && testLeadId != -1) {
						UserList testLead = userListDAO.getByUserId(testLeadId);
						workPackageTestCaseExecutionPlan.setTestLead(testLead);
					}
					if (plannedExecutionDate != null
							&& !plannedExecutionDate.equals("")) {
						workPackageTestCaseExecutionPlan
								.setPlannedExecutionDate(DateUtility
										.dateformatWithOutTime(plannedExecutionDate));
					}

					if (executionPriorityId != -1) {
						executionPriority = workPackageDAO
								.getExecutionPriorityById(executionPriorityId);
						workPackageTestCaseExecutionPlan
								.setExecutionPriority(executionPriority);
					}

					if (shiftId != -1) {
						WorkShiftMaster workShiftMaster = workPackageDAO
								.getWorkShiftById(shiftId);
						workPackageTestCaseExecutionPlan
								.setPlannedWorkShiftMaster(workShiftMaster);
					}
					if (workPackageTestCaseExecutionPlan.getTester() != null
							&& workPackageTestCaseExecutionPlan.getTestLead() != null) {
						workPackageTestCaseExecutionPlan.setExecutionStatus(1);
					}
					if (workPackageTestCaseExecutionPlan.getExecutionPriority() == null) {
						if (workPackageTestCaseExecutionPlan.getTestCase()
								.getTestCasePriority() != null)
							executionPriority = workPackageDAO
									.getExecutionPriorityByName(CommonUtility
											.getExecutionPriority(workPackageTestCaseExecutionPlan
													.getTestCase()
													.getTestCasePriority()
													.getPriorityName()));
						else
							executionPriority = workPackageDAO
									.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);

						workPackageTestCaseExecutionPlan
								.setExecutionPriority(executionPriority);

					}
					workPackageDAO
							.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
				}

			}

		} catch (Exception e) {
			log.info("Problem in adding Plan details records", e);
			return;
		}
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlanStatusDTO listWorkpackageTestCaseExecutionPlanningStatus(
			int workPackageId) {
	
		log.info("inside listWorkpackageTestCaseExecutionPlanningStatus() in service implementation");
		return workPackageDAO
				.listWorkpackageTestCaseExecutionPlanningStatus(workPackageId);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlan(
			int workPackageId, int jtStartIndex, int jtPageSize,
			ProductUserRole productUserRole) {
	
		return workPackageDAO.listWorkPackageTestCasesExecutionPlan(
				workPackageId, jtStartIndex, jtPageSize, productUserRole);
	}

	@Override
	@Transactional
	public List<WorkPackageDemandProjectionDTO> listWorkpackageDemandProdjectionByWorkpackageId(
			Date startDate, Date endDate, Integer testFactoryLabId,
			Integer testFactoryId, Integer productId, Integer workPackageId,
			Integer shiftId, String viewType) {
	
		return workPackageDAO.listWorkpackageDemandProdjectionByWorkpackageId(
				startDate, endDate, testFactoryLabId, testFactoryId, productId,
				workPackageId, shiftId, viewType);
	}

	@Override
	@Transactional
	public List<WorkPackage> getWorkPackagesByProductId(int productId) {
		return workPackageDAO.getWorkPackagesByProductId(productId);
	}

	@Override
	@Transactional
	public int getWorkPackagesCountByProductId(int productId) {
	
		return workPackageDAO.getWorkPackagesCountByProductId(productId);
	}

	@Override
	@Transactional
	public JTableResponseOptions getUserByBlockedStatusForWPAssociation(
			int workPackageId, int requiredRoleId, String plannedExecutionDate,
			int shiftId) {
	
		ProductMaster product = workPackageDAO
				.getProductMasterByWorkpackageId(workPackageId);
		JTableResponseOptions jTableResponseOptions;
		List<JsonUserList> jsonproductTeamUserList = null;

		Date plannedExecutionDateObj = null;

		if (plannedExecutionDate != null && !plannedExecutionDate.equals("")
				&& plannedExecutionDate.contains("/")) {
			plannedExecutionDateObj = DateUtility
					.dateformatWithOutTime(plannedExecutionDate);
		} else {
			if (plannedExecutionDate != null
					&& !plannedExecutionDate.equals("")) {
				plannedExecutionDateObj = DateUtility
						.dateFormatWithOutSeconds(plannedExecutionDate);
			}
		}
		List<JsonUserList> jsonUserList = new ArrayList<JsonUserList>();

		if (product != null) {
			jsonproductTeamUserList = productTeamResourcesDao
					.getProductTeamResourcesJsonListOfProduct(
							product.getProductId(), plannedExecutionDate,
							requiredRoleId);
			if (product.getProductMode().getModeId() == 1) {
				List<JsonUserList> jsonReservedUserList = workPackageDAO
						.getUserByBlockedStatusPerform(workPackageId, 0,
								plannedExecutionDate, shiftId, requiredRoleId,
								product.getProductId());
				jsonUserList.addAll(jsonReservedUserList);
				jsonUserList.addAll(jsonproductTeamUserList);
			} else if (product.getProductMode().getModeId() == 2) {
				jsonUserList.addAll(jsonproductTeamUserList);
			}
		}
		jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList);
		return jTableResponseOptions;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanTesterByEnv(
			int workPackageId, int jtStartIndex, int jtPageSize, UserList user,
			int envId) {
	
		return workPackageDAO.listWorkPackageTestCasesExecutionPlanTesterByEnv(
				workPackageId, jtStartIndex, jtPageSize, user, envId);
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan listWorkPackageTestCasesExecutionPlan(
			int wptcepId, int testcaseId) {
	
		return workPackageDAO.listWorkPackageTestCasesExecutionPlan(wptcepId,
				testcaseId);
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan listWorkPackageTestCasesExecutionPlanOfTCId(
			int testcaseId) {
		return workPackageDAO
				.listWorkPackageTestCasesExecutionPlanOfTCId(testcaseId);
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan listWorkPackageTestCasesExecutionPlanOfTCExeId(int wptcepId) {
		return workPackageDAO
				.listWorkPackageTestCasesExecutionPlanOfTCExeId(wptcepId);
	}
	@Override
	public List<TestExecutionResultBugList> listDefectsByTestcaseExecutionPlanId(
			int tcerId, int jtStartIndex, int jtPageSize) {
	
		return workPackageDAO.listDefectsByTestcaseExecutionPlanId(tcerId,
				jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public void addTestCaseDefect(
			TestExecutionResultBugList testExecutionResultBugList) {
	
		workPackageDAO.addTestCaseDefect(testExecutionResultBugList);

	}

	@Override
	@Transactional
	public void updateTestCaseDefect(
			TestExecutionResultBugList testExecutionResultBugList) {
	
		workPackageDAO.updateTestCaseDefect(testExecutionResultBugList);

	}

	@Override
	@Transactional
	public List<WorkFlow> getworkFlowListByEntity(int entityType) {
	
		return workPackageDAO.getworkFlowListByEntity(entityType);
	}

	@Override
	@Transactional
	public List<TestCaseExecutionResult> listResultsByTestcaseExecutionPlanId(
			int tcerId) {
	
		return workPackageDAO.listResultsByTestcaseExecutionPlanId(tcerId);
	}

	@Override
	@Transactional
	public void updateTestCaseResults(
			TestCaseExecutionResult testCaseExecutionResult) {
	
		workPackageDAO.updateTestCaseResults(testCaseExecutionResult);
	}

	@Override
	@Transactional
	public Date getFirstExecutedTCStartTimeofWP(int workPackageId) {
		return workPackageDAO.getFirstExecutedTCStartTimeofWP(workPackageId);
	}

	@Override
	@Transactional
	public Date getLastExecutedTCEndTimeofWP(int workPackageId) {
		return workPackageDAO.getLastExecutedTCEndTimeofWP(workPackageId);
	}

	@Override
	@Transactional
	public void setUpdateWPActualStartDate(int workPackageId, Date startTime) {
		workPackageDAO.setUpdateWPActualStartDate(workPackageId, startTime);
	}

	@Override
	@Transactional
	public void setUpdateWPActualEndtDate(int workPackageId, Date startTime) {
		workPackageDAO.setUpdateWPActualEndtDate(workPackageId, startTime);
	}

	@Override
	@Transactional
	public List<WorkPackage> getUserAssociatedWorkPackages(int roleId,
			int userId) {
	
		return workPackageDAO.getUserAssociatedWorkPackages(roleId, userId);
	}

	@Override
	@Transactional
	public List<ExecutionPriority> getExecutionPriority() {
	
		return workPackageDAO.getExecutionPriority();
	}

	@Override
	@Transactional
	public ExecutionPriority getExecutionPriorityById(int executionPriorityId) {
	
		return workPackageDAO.getExecutionPriorityById(executionPriorityId);
	}

	@Override
	@Transactional
	public List<WorkShiftMaster> listActualShiftByPlannedDateAndTestFactory(
			Integer testFactoryId, String actualExecutionDate) {
	
		return workPackageDAO.listActualShiftByPlannedDateAndTestFactory(
				testFactoryId, actualExecutionDate);
	}

	@Override
	@Transactional
	public ProductMaster getProductMasterByWorkpackageId(int workPackageId) {
	
		return workPackageDAO.getProductMasterByWorkpackageId(workPackageId);
	}

	@Override
	@Transactional
	public Integer getProductIdByWorkpackageId(int workPackageId) {
		return workPackageDAO.getProductIdByWorkpackageId(workPackageId);
	}

	@Override
	@Transactional
	public Integer getexecutionTypeIdByWorkpackageId(int workPackageId) {
		return workPackageDAO.getexecutionTypeIdByWorkpackageId(workPackageId);
	}

	@Override
	@Transactional
	public Integer getProductTestCaseCount(int productId, int executionType) {
		return workPackageDAO.getProductTestCaseCount(productId, executionType);
	}

	@Override
	@Transactional
	public WorkPackageTestCaseSummaryDTO listWorkPackageTestCaseExecutionSummary(
			Integer workPackageId) {
		return workPackageDAO
				.listWorkPackageTestCaseExecutionSummary(workPackageId);
	}

	@Override
	@Transactional
	public void addWorkPackageDemandProjection(
			WorkPackageDemandProjection workPackageDemandProjection) {
		workPackageDAO
				.addWorkPackageDemandProjection(workPackageDemandProjection);

	}

	@Override
	@Transactional
	public List<JsonWorkPackageDemandProjection> listWorkPackageDemandProjectionByDate(
			int workPackageId, int shiftId, Date date) {
		List<WorkPackageDemandProjection> workPackageDemandProjectionList = workPackageDAO
				.listWorkPackageDemandProjectionByDate(workPackageId, shiftId,
						date);
		log.info("listWorkPackageDemandProjectionByDate >>>>>>>>>>>>>> "
				+ workPackageDemandProjectionList.size());
		List<JsonWorkPackageDemandProjection> jsonWorkPackageDemandProjections = new ArrayList<JsonWorkPackageDemandProjection>();
		JsonWorkPackageDemandProjection jsonWorkPackageDemandProjection = null;
		if (workPackageDemandProjectionList != null&& workPackageDemandProjectionList.size() > 0) {
			for (WorkPackageDemandProjection demandProjection : workPackageDemandProjectionList) {
				jsonWorkPackageDemandProjection = new JsonWorkPackageDemandProjection(demandProjection);
				jsonWorkPackageDemandProjections.add(jsonWorkPackageDemandProjection);
			}
			log.info("jsonWorkPackageDemandProjections size "+ jsonWorkPackageDemandProjections.size());
		} else {
			return null;
		}
		return jsonWorkPackageDemandProjections;
	}

	@Override
	@Transactional
	public List<UserRoleMaster> getUserRolesForDemandProjection() {
	
		return workPackageDAO.getUserRolesForDemandProjection();
	}

	@Override
	@Transactional
	public void deleteWorkPackageDemandProjection(int wpDemandProjectionId) {
		WorkPackageDemandProjection workPackageDemandProjection = workPackageDAO
				.getWorkPackageDemandProjectionById(wpDemandProjectionId);
		workPackageDAO
				.deleteWorkPackageDemandProjection(workPackageDemandProjection);
	}

	@Override
	@Transactional
	public void deleteWorkPackageDemandProjectionWeeklyByGroupDemandId(Long groupDemandId) {
		List<WorkPackageDemandProjection> workPackageDemandProjection = workPackageDAO.getWorkPackageDemandProjectionByGroupDemandId(groupDemandId);
		for(WorkPackageDemandProjection wpdp: workPackageDemandProjection){
			log.info("jsonWorkPackageDemandProjection--------loop------------ "+wpdp.getGroupDemandId());
			workPackageDAO.deleteWorkPackageDemandProjection(wpdp);
		}
	}

	
	
	@Override
	@Transactional
	public WorkPackageDemandProjection getWorkPackageDemandProjectionById(
			int wpDemandProjectionId) {
		return workPackageDAO
				.getWorkPackageDemandProjectionById(wpDemandProjectionId);
	}

	@Override
	@Transactional
	public void updateWorkPackageDemandProjection(
			WorkPackageDemandProjection wpDemandProjection) {
		workPackageDAO.updateWorkPackageDemandProjection(wpDemandProjection);

	}

	@Override
	@Transactional
	public WorkFlow getWorkFlowByEntityIdStageId(Integer entityId,
			Integer stageId) {
	
		return workPackageDAO.getWorkFlowByEntityIdStageId(entityId, stageId);
	}

	@Override
	@Transactional
	public void addWorkFlowEvent(WorkFlowEvent workFlowEvent) {
	
		workPackageDAO.addWorkFlowEvent(workFlowEvent);
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan getWorkpackageTestcaseExecutionPlanById(
			int workPackageTestCaseExecutionPlanId) {
	
		return workPackageDAO
				.getWorkpackageTestcaseExecutionPlanById(workPackageTestCaseExecutionPlanId);
	}

	@Override
	@Transactional
	public EntityMaster getEntityMasterById(Integer entityId) {
	
		return workPackageDAO.getEntityMasterById(entityId);
	}
	
	@Override
	@Transactional
	public EntityMaster getEntityMasterByName(String entityMasterName) {
		return workPackageDAO.getEntityMasterByName(entityMasterName);
	}

	@Override
	@Transactional
	public void addEvidence(Evidence evidence) {
	
		workPackageDAO.addEvidence(evidence);
	}

	@Override
	@Transactional
	public List<Evidence> testcaseListByEvidence(Integer tcerId, String type) {
	
		return workPackageDAO.testcaseListByEvidence(tcerId, type);
	}

	@Override
	@Transactional
	public List<TestStepExecutionResult> listTestStepPlan(Integer testcaseId,
			Integer tcerId) {
	
		return workPackageDAO.listTestStepPlan(testcaseId, tcerId);
	}

	// Added for Bug 844. Get the TestStepExecResults in order of
	// TestStepExecResultId
	public List<TestStepExecutionResult> listTestStepResultByCaseExecId(
			Integer tcerId) {

		return workPackageDAO.listTestStepResultByCaseExecId(tcerId);
	}

	@Override
	@Transactional
	public TestStepExecutionResult getTestStepExecutionResultById(
			Integer testStepId) {
	
		return workPackageDAO.getTestStepExecutionResultById(testStepId);
	}

	@Override
	@Transactional
	public void updateTestStepExecutionResult(
			TestStepExecutionResult testStepExecutionResult) {
	
		workPackageDAO.updateTestStepExecutionResult(testStepExecutionResult);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseSummaryDTO> listWorkPackageTestCaseExecutionSummaryByDate(
			Integer workPackageId) {
	
		return workPackageDAO
				.listWorkPackageTestCaseExecutionSummaryByDate(workPackageId);
	}

	@Override
	@Transactional
	public JsonWorkPackageTestCaseExecutionPlanForTester listWorkPackageTestCaseExecutionSummaryReport(
			Integer workPackageId) {
	

		// return
		WorkPackageTCEPSummaryDTO wpSummaryDTO = new WorkPackageTCEPSummaryDTO();
		wpSummaryDTO = workPackageDAO
				.listWorkPackageTestCaseExecutionSummaryForTiles(workPackageId);
		JsonWorkPackageTestCaseExecutionPlanForTester jsonWPTCEPT = new JsonWorkPackageTestCaseExecutionPlanForTester(
				wpSummaryDTO);
		Date sdate = wpSummaryDTO.getPlannedStartDate();
		Date edate = wpSummaryDTO.getPlannedEndDate();
		Date currentDate = new Date();
		Long nthDayfromStart = (long) 0;
		Integer nthDayfomWPStart = 0;
		if (currentDate.compareTo(edate) > 0) {
			nthDayfomWPStart = 0;
		} else if (currentDate.compareTo(edate) < 0) {
			if (currentDate.compareTo(sdate) == -1) {
				jsonWPTCEPT.setPlannedBeforeAfterCurrentDate("after");
				nthDayfromStart = DateUtility.DateDifferenceInMinutes(
						currentDate, sdate);
			} else {
				jsonWPTCEPT.setPlannedBeforeAfterCurrentDate("before");
				nthDayfromStart = DateUtility.DateDifferenceInMinutes(sdate,
						currentDate);
			}
			String intoHourMinutes = DateUtility.convertTimeInHoursMinutes(0,
					new Integer((int) (long) nthDayfromStart));
			if (intoHourMinutes.indexOf(':') != -1) {
				intoHourMinutes = intoHourMinutes.substring(0,
						intoHourMinutes.indexOf(':'));
			}

			nthDayfomWPStart = Integer.parseInt(intoHourMinutes) / 24;
		}

		jsonWPTCEPT.setWpnthDayfromStrart("-"
				+ String.valueOf(nthDayfomWPStart));
		return jsonWPTCEPT;

	}

	@Override
	@Transactional
	public WorkFlow getWorkFlowByEntityIdWorkFlowId(Integer entityId,
			Integer workFlowId) {
	
		return workPackageDAO.getWorkFlowByEntityIdWorkFlowId(entityId,
				workFlowId);
	}

	@Override
	@Transactional
	public WorkPackage cloneWorkpackage(Map<String, String> mapData,
			UserList user, String copyDataType, String mongoDBAvailability) {
		log.info("copyDataType: " + copyDataType);
		WorkPackage workPackage = new WorkPackage();

		workPackage.setName(mapData.get("workpackageName"));
		workPackage.setDescription(mapData.get("description"));

		workPackage.setCreateDate(DateUtility.getCurrentTime());
		workPackage.setModifiedDate(DateUtility.getCurrentTime());
		workPackage.setStatus(1);
		workPackage.setIsActive(1);
			
		ExecutionTypeMaster executionTypeMaster = executionTypeMasterDAO
				.getExecutionTypeByExecutionTypeId(Integer.parseInt(mapData
						.get("workpackageType")));
		workPackage.setWorkPackageType(executionTypeMaster);
		ProductBuild productBuild = productBuildDAO.getByProductBuildId(Integer
				.parseInt(mapData.get("productBuildId")), 0);
		workPackage.setProductBuild(productBuild);

		WorkFlowEvent workFlowEvent = new WorkFlowEvent();
		workFlowEvent.setEventDate(DateUtility.getCurrentTime());
		workFlowEvent.setRemarks("New Workapckage Added :"
				+ workPackage.getName());
		workFlowEvent.setUser(user);
		workFlowEvent.setWorkFlow(workPackageDAO.getWorkFlowByEntityIdStageId(
				IDPAConstants.WORKPACKAGE_ENTITY_ID,
				IDPAConstants.WORKFLOW_STAGE_ID_NEW));

		workPackageDAO.addWorkFlowEvent(workFlowEvent);
		workPackage.setWorkFlowEvent(workFlowEvent);
		workPackage.setCreateDate(DateUtility.getCurrentTime());
		workPackage.setModifiedDate(DateUtility.getCurrentTime());
		workPackage.setPlannedStartDate(DateUtility
				.dateformatWithOutTime(mapData.get("plannedStartDate")));
		workPackage.setPlannedEndDate(DateUtility.dateformatWithOutTime(mapData
				.get("plannedEndDate")));

		if (mapData.get("action").equalsIgnoreCase("new")) {

			workPackage.setSourceType("Normal");
			workPackage.setUserList(user);
			workPackage.setIterationNumber(-1);
			LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
			lifeCyclePhase.setLifeCyclePhaseId(4);
			workPackage.setLifeCyclePhase(lifeCyclePhase);
			ProductMaster productMaster = workPackage.getProductBuild()
					.getProductVersion().getProductMaster();
			workPackage.setProductMaster(productMaster);
			
			workPackageDAO.addWorkPackage(workPackage);
			if(workPackage!=null && workPackage.getWorkPackageId()!=null){
				mongoDBService.addWorkPackage(workPackage.getWorkPackageId());
			}

		} else if (mapData.get("action").equalsIgnoreCase("copy")) {
			Integer workPackageIdExisting = Integer.parseInt(mapData
					.get("workpackageId"));

			String[] condition = null;
			List conditionList = null;
			if (copyDataType != null && copyDataType.length() != 0) {
				condition = copyDataType.split(",");
				conditionList = Arrays.asList(condition);
			}

			WorkPackage workpPackageExisting = workPackageDAO
					.getWorkPackageById(workPackageIdExisting);
			workPackage.setSourceType("Clone");
			workPackage.setWorkpackageCloneId(workPackageIdExisting);
			workPackage.setUserList(user);
			workPackage.setIterationNumber(-1);
			LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
			lifeCyclePhase.setLifeCyclePhaseId(4);
			workPackage.setLifeCyclePhase(lifeCyclePhase);
		
			workPackageDAO.addWorkPackage(workPackage);
			if(workPackage!=null && workPackage.getWorkPackageId()!=null){
				mongoDBService.addWorkPackage(workPackage.getWorkPackageId());
			}

			
			int productId = workpPackageExisting.getProductBuild()
					.getProductVersion().getProductMaster().getProductId();
			ProductMaster productMaster = workPackage.getProductBuild()
					.getProductVersion().getProductMaster();
			workPackage.setProductMaster(productMaster);
			ProductVersionListMaster productVersion = workPackage
					.getProductBuild().getProductVersion();
			// Add feature plan starts

			List<ProductFeature> allProductFeatureList = productFeatureDAO
					.getFeatureListByProductId(productId, null, null, null);

			Set<WorkPackageFeature> workPackageFeaturesExisting = workpPackageExisting
					.getWorkPackageFeature();
			if (workPackageFeaturesExisting != null
					&& !workPackageFeaturesExisting.isEmpty()) {
				int count = initializeWorkPackageWithFeature(workPackage,
						allProductFeatureList);
			}
			List<WorkPackageFeature> workPackageFeaturesNew = workPackageDAO
					.listWorkPackageFeature(workPackage.getWorkPackageId(), 0,
							0);

			for (WorkPackageFeature wpFNew : workPackageFeaturesNew) {
				for (WorkPackageFeature wpFExist : workPackageFeaturesExisting) {
					if (wpFNew.getFeature().getProductFeatureId() == wpFExist
							.getFeature().getProductFeatureId()) {
						if (wpFExist.getIsSelected() == 1) {
							wpFNew.setIsSelected(1);
							workPackageDAO.updateWorkPackageFeature(wpFNew);
							if (wpFNew.getWorkPackage().getWorkFlowEvent()
									.getWorkFlow().getStageId() != IDPAConstants.WORKFLOW_STAGE_ID_PLANNING
									&& wpFNew.getWorkPackage()
											.getWorkFlowEvent().getWorkFlow()
											.getStageValue() < 20) {
								WorkFlow workFlow = workPackageDAO
										.getWorkFlowByEntityIdStageId(
												IDPAConstants.WORKPACKAGE_ENTITY_ID,
												IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
								workFlowEvent = new WorkFlowEvent();
								workFlowEvent.setEventDate(DateUtility
										.getCurrentTime());
								workFlowEvent
										.setRemarks("Planning Workapckage Feature:"
												+ workPackage.getName());
								user = userListDAO
										.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
								workFlowEvent.setUser(user);
								workFlowEvent.setWorkFlow(workFlow);
								workPackage.setWorkFlowEvent(workFlowEvent);
								workPackageDAO.addWorkFlowEvent(workFlowEvent);
								workPackageDAO.updateWorkPackage(workPackage);
							}
							workPackageDAO.mapWorkpackageWithFeature(
									workPackage.getWorkPackageId(),
									wpFNew.getFeature().getProductFeatureId(),
									"Add");
						}
					}
				}
			}

			// Add feature plan ends

			// Add Test suite plan starts

			List<WorkPackageTestSuite> workPackageTestSuitesTotal = workPackageDAO
					.listWorkPackageTestSuite(workPackageIdExisting, 0, 0);

			ExecutionTypeMaster executionTypeMasterExisting = executionTypeMasterDAO
					.getExecutionTypeByExecutionTypeId(workpPackageExisting
							.getWorkPackageType().getExecutionTypeId());
			int testSuiteExecutionId = -1;
			if (executionTypeMasterExisting.getExecutionTypeId() == 7)
				testSuiteExecutionId = 3;

			List<TestSuiteList> allTestSuiteLists = testSuiteListDAO
					.getTestSuiteByProductId(productId, testSuiteExecutionId);

			if (workPackageTestSuitesTotal != null
					&& !workPackageTestSuitesTotal.isEmpty()) {
				int count = initializeWorkPackageWithTestSuite(workPackage,
						allTestSuiteLists);
			}
			List<WorkPackageTestSuite> workPackageTestSuitesNew = workPackageDAO
					.listWorkPackageTestSuite(workPackage.getWorkPackageId(),
							0, 0);

			for (WorkPackageTestSuite workPackageTestSuiteNew : workPackageTestSuitesNew) {
				for (WorkPackageTestSuite workPackageTestSuiteExist : workPackageTestSuitesTotal) {

					if (workPackageTestSuiteNew.getTestSuite().getTestSuiteId() == workPackageTestSuiteExist
							.getTestSuite().getTestSuiteId()) {
						if (workPackageTestSuiteExist.getIsSelected() == 1) {
							workPackageTestSuiteNew.setIsSelected(1);

							if (workPackage.getWorkFlowEvent().getWorkFlow()
									.getStageId() != IDPAConstants.WORKFLOW_STAGE_ID_PLANNING
									&& workPackage.getWorkFlowEvent()
											.getWorkFlow().getStageValue() < 20) {
								WorkFlow workFlow = workPackageDAO
										.getWorkFlowByEntityIdStageId(
												IDPAConstants.WORKPACKAGE_ENTITY_ID,
												IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
								workFlowEvent = new WorkFlowEvent();
								workFlowEvent.setEventDate(DateUtility
										.getCurrentTime());
								workFlowEvent
										.setRemarks("Planning Workapckage testsuite:"
												+ workPackage.getName());
								workFlowEvent.setUser(user);
								workFlowEvent.setWorkFlow(workFlow);
								workPackage.setWorkFlowEvent(workFlowEvent);
								workPackageDAO.addWorkFlowEvent(workFlowEvent);
								workPackageDAO.updateWorkPackage(workPackage);
							}

							workPackageDAO
									.updateWorkPackageTestSuite(workPackageTestSuiteNew);
							workPackageDAO.mapWorkpackageWithTestSuite(
									workPackageTestSuiteNew.getWorkPackage()
											.getWorkPackageId(),
									workPackageTestSuiteNew.getTestSuite()
											.getTestSuiteId(), "Add");
						}
					}
				}
			}

			// Add Testcases plan starts

			List<WorkPackageTestCase> workPackageTestCasesTotal = workPackageDAO
					.listWorkPackageTestCases(workPackageIdExisting);
			if (workPackageTestCasesTotal != null
					&& !workPackageTestCasesTotal.isEmpty()) {
				int count = initializeWorkPackageWithTestCases(workPackage
						.getWorkPackageId());
			}
			List<WorkPackageTestCase> workPackageTestCasesNew = workPackageDAO
					.listWorkPackageTestCases(workPackage.getWorkPackageId());

			for (WorkPackageTestCase workPackageTestCaseNew : workPackageTestCasesNew) {
				for (WorkPackageTestCase workPackageTestCaseExist : workPackageTestCasesTotal) {
					if (workPackageTestCaseNew.getTestCase().getTestCaseId() == workPackageTestCaseExist
							.getTestCase().getTestCaseId()) {
						if (workPackageTestCaseExist.getIsSelected() == 1) {
							workPackageTestCaseNew.setIsSelected(1);

							if (workPackage.getWorkFlowEvent().getWorkFlow()
									.getStageId() != IDPAConstants.WORKFLOW_STAGE_ID_PLANNING
									&& workPackage.getWorkFlowEvent()
											.getWorkFlow().getStageValue() < 20) {
								WorkFlow workFlow = workPackageDAO
										.getWorkFlowByEntityIdStageId(
												IDPAConstants.WORKPACKAGE_ENTITY_ID,
												IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
								workFlowEvent = new WorkFlowEvent();
								workFlowEvent.setEventDate(DateUtility
										.getCurrentTime());
								workFlowEvent
										.setRemarks("Planning Workapckage :"
												+ workPackage.getName());
								workFlowEvent.setUser(user);
								workFlowEvent.setWorkFlow(workFlow);
								workPackage.setWorkFlowEvent(workFlowEvent);
								workPackageDAO.addWorkFlowEvent(workFlowEvent);
								workPackageDAO.updateWorkPackage(workPackage);
							}

							workPackageDAO
									.updateWorkPackageTestCase(workPackageTestCaseNew);
							workPackageDAO.mapWorkpackageWithTestCase(
									workPackageTestCaseNew.getWorkPackage()
											.getWorkPackageId(),
									workPackageTestCaseNew.getTestCase()
											.getTestCaseId(), "Add");
						}
					}
				}
			}

			if (conditionList != null && !conditionList.isEmpty()
					&& conditionList.contains("environments")) {
				List<WorkpackageRunConfiguration> workpackageRunConfigurationExists = null;
				List<RunConfiguration> runConfigurationListExist = new ArrayList<RunConfiguration>();
				List<RunConfiguration> runConfigurationList = new ArrayList<RunConfiguration>();

				// Feature environment combination selection and allocations
				// starts
				workpackageRunConfigurationExists = workPackageDAO
						.getWorkpackageRunConfigurationList(
								workPackageIdExisting, null, "feature");

				for (WorkpackageRunConfiguration wprcexist : workpackageRunConfigurationExists) {
					runConfigurationListExist.add(wprcexist
							.getRunconfiguration());
				}
				RunConfiguration runConfigurationObj = null;
				for (RunConfiguration rc : runConfigurationListExist) {
					runConfigurationObj = new RunConfiguration();
					runConfigurationObj.setEnvironmentcombination(rc
							.getEnvironmentcombination());
					runConfigurationObj.setProduct(productMaster);
					runConfigurationObj.setProductVersion(productVersion);
					runConfigurationObj.setRunconfigName(rc.getRunconfigName());
					runConfigurationObj.setGenericDevice(rc.getGenericDevice());
					runConfigurationObj.setHostList(rc.getHostList());
					runConfigurationObj.setWorkPackage(workPackage);
					runConfigurationObj = environmentDAO
							.addRunConfiguration(runConfigurationObj);
					runConfigurationList.add(runConfigurationObj);
				}

				WorkpackageRunConfiguration wpRunConfiguration = null;
				WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan = null;
				List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlanListForUpdate = new ArrayList<WorkPackageFeatureExecutionPlan>();
				WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlanExisting = null;

				List<WorkPackageFeature> workPackageFeatures = workPackageDAO
						.listWorkPackageFeature(workPackage.getWorkPackageId());
				Set<WorkPackageFeature> wpf = new HashSet<WorkPackageFeature>(
						workPackageFeatures);

				for (RunConfiguration rc : runConfigurationList) {

					workPackageDAO.addRunConfigurationWorkpackage(
							workPackage.getWorkPackageId(),
							rc.getRunconfigId(), "feature");

					wpRunConfiguration = workPackageDAO
							.getWorkpackageRunConfiguration(
									workPackage.getWorkPackageId(),
									rc.getRunconfigId(), "feature");
					ProductFeature feature = null;
					WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = null;
					WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlanExisting = null;

					List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate = new ArrayList<WorkPackageTestCaseExecutionPlan>();

					for (WorkPackageFeature wpFeature : wpf) {
						if (wpFeature.getIsSelected() == 1) {
							feature = wpFeature.getFeature();
							TestRunJob testRunJob = workPackageDAO
									.getTestRunJobByWP(workPackage, rc);
							if (testRunJob != null) {
								workPackageDAO.mapTestRunJobFeature(
										testRunJob.getTestRunJobId(),
										feature.getProductFeatureId(), "Add");
							} else {
								workPackageDAO.addTestRunJob(rc, null,
										workPackage, null);
								testRunJob = workPackageDAO.getTestRunJobByWP(
										workPackage, rc);
								workPackageDAO.mapTestRunJobFeature(
										testRunJob.getTestRunJobId(),
										feature.getProductFeatureId(), "Add");
							}

							workPackageFeatureExecutionPlan = new WorkPackageFeatureExecutionPlan();
							workPackageFeatureExecutionPlan
									.setCreatedDate(DateUtility
											.getCurrentTime());
							workPackageFeatureExecutionPlan.setFeature(feature);
							workPackageFeatureExecutionPlan
									.setModifiedDate(DateUtility
											.getCurrentTime());
							workPackageFeatureExecutionPlan
									.setPlannedExecutionDate(workPackage
											.getPlannedStartDate());
							workPackageFeatureExecutionPlan
									.setRunConfiguration(wpRunConfiguration);
							workPackageFeatureExecutionPlan
									.setWorkPackage(workPackage);
							workPackageFeatureExecutionPlanExisting = workPackageDAO
									.getWorkpackageFeatureExecutionPlan(
											workPackageIdExisting, feature
													.getProductFeatureId(),
											wpRunConfiguration
													.getRunconfiguration()
													.getRunconfigName());
							workPackageFeatureExecutionPlan
									.setStatus(workPackageFeatureExecutionPlanExisting
											.getStatus());
							workPackageFeatureExecutionPlan
									.setTestRunJob(testRunJob);
							ExecutionPriority executionPriority = null;
							if (wpFeature.getFeature().getExecutionPriority() != null)
								executionPriority = workPackageDAO
										.getExecutionPriorityByName(CommonUtility
												.getExecutionPriority(wpFeature
														.getFeature()
														.getExecutionPriority()
														.getPriorityName()));
							else
								executionPriority = workPackageDAO
										.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
							workPackageFeatureExecutionPlan
									.setExecutionPriority(executionPriority);
							workPackageDAO
									.addWorkpackageFeatureExecutionPlan(workPackageFeatureExecutionPlan);

							Set<TestCaseList> featureTC = feature
									.getTestCaseList();
							for (TestCaseList testCaseList : featureTC) {
								if (testCaseList != null) {
									testCaseList = testCaseListDAO
											.getByTestCaseId(testCaseList
													.getTestCaseId());
									workPackageTestCaseExecutionPlan = addWorkpPackageTestCaseExecutionPlans(
											testCaseList, null, workPackage,
											wpRunConfiguration, feature,
											"Feature", testRunJob);
									workPackageTestCaseExecutionPlanExisting = workPackageDAO
											.getWorkPackageTestcaseExecutionPlan(
													feature.getProductFeatureId(),
													workPackageIdExisting,
													wpRunConfiguration
															.getRunconfiguration()
															.getRunconfigName(),
													"Feature", -1, testCaseList
															.getTestCaseId());
									workPackageTestCaseExecutionPlan
											.setStatus(workPackageTestCaseExecutionPlanExisting
													.getStatus());
									workPackageTestCaseExecutionPlanListForUpdate
											.add(workPackageTestCaseExecutionPlan);
								}
							}
							workPackageDAO
									.addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
						}
					}
				}

				// Feature environment combination selection and allocations
				// ends

				// TestSuite environment combination selection and allocations
				// ends

				WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan = null;
				List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlanListForUpdate = new ArrayList<WorkPackageTestSuiteExecutionPlan>();

				List<WorkPackageTestSuite> workPackageTestSuites = workPackageDAO
						.listWorkPackageTestSuite(workPackage
								.getWorkPackageId());
				Set<WorkPackageTestSuite> wpts = new HashSet<WorkPackageTestSuite>(
						workPackageTestSuites);

				for (RunConfiguration rc : runConfigurationList) {

					workPackageDAO.addRunConfigurationWorkpackage(
							workPackage.getWorkPackageId(),
							rc.getRunconfigId(), "testsuite");

					wpRunConfiguration = workPackageDAO
							.getWorkpackageRunConfiguration(
									workPackage.getWorkPackageId(),
									rc.getRunconfigId(), "testsuite");
					TestSuiteList testSuite = null;
					WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = null;
					List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate = new ArrayList<WorkPackageTestCaseExecutionPlan>();
					WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlanExisting = null;

					for (WorkPackageTestSuite wpTestSuite : wpts) {
						if (wpTestSuite.getIsSelected() == 1) {
							testSuite = wpTestSuite.getTestSuite();
							TestRunJob testRunJob = workPackageDAO
									.getTestRunJobByWP(workPackage, rc);
							if (testRunJob != null) {
								workPackageDAO.mapTestRunJobTestSuite(
										testRunJob.getTestRunJobId(),
										testSuite.getTestSuiteId(), "Add");
							} else {
								workPackageDAO.addTestRunJob(rc, null,
										workPackage, null);
								testRunJob = workPackageDAO.getTestRunJobByWP(
										workPackage, rc);
								workPackageDAO.mapTestRunJobTestSuite(
										testRunJob.getTestRunJobId(),
										testSuite.getTestSuiteId(), "Add");
							}

							workPackageTestSuiteExecutionPlan = new WorkPackageTestSuiteExecutionPlan();
							workPackageTestSuiteExecutionPlan
									.setCreatedDate(DateUtility
											.getCurrentTime());
							workPackageTestSuiteExecutionPlan
									.setTestsuite(testSuite);
							workPackageTestSuiteExecutionPlan
									.setModifiedDate(DateUtility
											.getCurrentTime());
							workPackageTestSuiteExecutionPlan
									.setPlannedExecutionDate(workPackage
											.getPlannedStartDate());
							workPackageTestSuiteExecutionPlan
									.setRunConfiguration(wpRunConfiguration);
							workPackageTestSuiteExecutionPlan
									.setWorkPackage(workPackage);
							workPackageTestSuiteExecutionPlan
									.setTestRunJob(testRunJob);
							workPackageTestSuiteExecutionPlanExisting = workPackageDAO
									.getWorkpackageTestSuiteExecutionPlan(
											workPackageIdExisting, testSuite
													.getTestSuiteId(),
											wpRunConfiguration
													.getRunconfiguration()
													.getRunconfigName());
							workPackageTestSuiteExecutionPlan
									.setStatus(workPackageTestSuiteExecutionPlanExisting
											.getStatus());

							ExecutionPriority executionPriority = null;
							if (wpTestSuite.getTestSuite()
									.getExecutionPriority() != null)
								executionPriority = workPackageDAO
										.getExecutionPriorityByName(CommonUtility
												.getExecutionPriority(wpTestSuite
														.getTestSuite()
														.getExecutionPriority()
														.getPriorityName()));
							else
								executionPriority = workPackageDAO
										.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
							workPackageTestSuiteExecutionPlan
									.setExecutionPriority(executionPriority);
							workPackageDAO
									.addWorkpackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlan);

							Set<TestCaseList> testSuiteTC = testSuite
									.getTestCaseLists();
							for (TestCaseList testCaseList : testSuiteTC) {
								if (testCaseList != null) {
									testCaseList = testCaseListDAO
											.getByTestCaseId(testCaseList
													.getTestCaseId());
									workPackageTestCaseExecutionPlan = addWorkpPackageTestCaseExecutionPlans(
											testCaseList, testSuite,
											workPackage, wpRunConfiguration,
											null, "TestSuite", testRunJob);
									workPackageTestCaseExecutionPlanExisting = workPackageDAO
											.getWorkPackageTestcaseExecutionPlan(
													-1,
													workPackageIdExisting,
													wpRunConfiguration
															.getRunconfiguration()
															.getRunconfigName(),
													"TestSuite", testSuite
															.getTestSuiteId(),
													testCaseList
															.getTestCaseId());
									if (workPackageTestCaseExecutionPlanExisting != null
											&& workPackageTestCaseExecutionPlanExisting
													.getStatus() != null)
										workPackageTestCaseExecutionPlan
												.setStatus(workPackageTestCaseExecutionPlanExisting
														.getStatus());

									workPackageTestCaseExecutionPlanListForUpdate
											.add(workPackageTestCaseExecutionPlan);
								}
							}
							workPackageDAO
									.addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
						}
					}
				}

				// Testsuite environment combination selection and allocations
				// ends

				// Testcase environment combination selection and allocations
				// starts

				List<WorkPackageTestCase> workPackageTestCases = workPackageDAO
						.listWorkPackageTestCases(workPackage
								.getWorkPackageId());
				Set<WorkPackageTestCase> workPackageTestCasesSet = new HashSet<WorkPackageTestCase>(
						workPackageTestCases);
				workPackage.setWorkPackageTestCases(workPackageTestCasesSet);
				workPackageDAO.updateWorkPackage(workPackage);

				for (RunConfiguration rc : runConfigurationList) {
					workPackageDAO.addRunConfigurationWorkpackage(
							workPackage.getWorkPackageId(),
							rc.getRunconfigId(), "testcase");
					wpRunConfiguration = workPackageDAO
							.getWorkpackageRunConfiguration(
									workPackage.getWorkPackageId(),
									rc.getRunconfigId(), "testcase");

					TestCaseList testcase = null;
					WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = null;
					List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate = new ArrayList<WorkPackageTestCaseExecutionPlan>();

					for (WorkPackageTestCase workPackageTestCase : workPackageTestCasesSet) {
						if (workPackageTestCase.getIsSelected() == 1) {
							testcase = workPackageTestCase.getTestCase();
							TestRunJob testRunJob = workPackageDAO
									.getTestRunJobByWP(workPackage, rc);
							if (testRunJob != null) {
								workPackageDAO.mapTestRunJobTestCase(
										testRunJob.getTestRunJobId(),
										testcase.getTestCaseId(), "Add");
							} else {
								workPackageDAO.addTestRunJob(rc, null,
										workPackage, null);
								testRunJob = workPackageDAO.getTestRunJobByWP(
										workPackage, rc);
								workPackageDAO.mapTestRunJobTestCase(
										testRunJob.getTestRunJobId(),
										testcase.getTestCaseId(), "Add");
							}
							workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
							workPackageTestCaseExecutionPlan
									.setTestCase(workPackageTestCase
											.getTestCase());
							workPackageTestCaseExecutionPlan
									.setWorkPackage(workPackage);
							workPackageTestCaseExecutionPlan
									.setRunConfiguration(wpRunConfiguration);
							workPackageTestCaseExecutionPlan
									.setPlannedExecutionDate(workPackage
											.getPlannedStartDate());

							workPackageTestCaseExecutionPlan
									.setExecutionStatus(3);
							workPackageTestCaseExecutionPlan.setIsExecuted(0);
							workPackageTestCaseExecutionPlan
									.setSourceType("TestCase");
							workPackageTestCaseExecutionPlan
									.setTestRunJob(testRunJob);
							workPackageTestCaseExecutionPlanExisting = workPackageDAO
									.getWorkPackageTestcaseExecutionPlan(-1,
											workPackageIdExisting,
											wpRunConfiguration
													.getRunconfiguration()
													.getRunconfigName(),
											"TestCase", -1, workPackageTestCase
													.getTestCase()
													.getTestCaseId());
							workPackageTestCaseExecutionPlan
									.setStatus(workPackageTestCaseExecutionPlanExisting
											.getStatus());

							ExecutionPriority executionPriority = null;
							if (workPackageTestCase.getTestCase()
									.getTestCasePriority() != null)
								executionPriority = workPackageDAO
										.getExecutionPriorityByName(CommonUtility
												.getExecutionPriority(workPackageTestCase
														.getTestCase()
														.getTestCasePriority()
														.getPriorityName()));
							else
								executionPriority = workPackageDAO
										.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);

							workPackageTestCaseExecutionPlan
									.setExecutionPriority(executionPriority);

							TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
							testCaseExecutionResult.setResult("");
							testCaseExecutionResult.setComments("");
							testCaseExecutionResult.setDefectsCount(0);
							testCaseExecutionResult.setDefectIds("");
							testCaseExecutionResult.setIsApproved(0);
							testCaseExecutionResult.setIsReviewed(0);
							testCaseExecutionResult.setObservedOutput("");

							testCaseExecutionResult
									.setWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
							workPackageTestCaseExecutionPlan
									.setTestCaseExecutionResult(testCaseExecutionResult);
							workPackageTestCaseExecutionPlanListForUpdate
									.add(workPackageTestCaseExecutionPlan);
						}

					}
					workPackageDAO
							.addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
				}

				// Testcase environment combination selection and allocations
				// ends

				String existingPED = "";
				long plannedExecutionDateDifference = 0;
				Date newPED = null;
				String existingWPPSD = DateUtility
						.sdfDateformatWithOutTime(workpPackageExisting
								.getPlannedStartDate());
				WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlanNew = null;
				if (conditionList != null && !conditionList.isEmpty()
						&& conditionList.contains("plannedExecutionDate")) {
					Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlansExisting = workpPackageExisting
							.getWorkPackageTestCaseExecutionPlan();
					List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlansNew = workPackageDAO
							.getWorkPackageTestCaseExecutionPlanByWorkpackgeId(workPackage
									.getWorkPackageId());

					List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlansExisting = workPackageDAO
							.getWorkPackageFeatureExecutionPlanByWorkpackgeId(workpPackageExisting
									.getWorkPackageId());
					List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlansNew = workPackageDAO
							.getWorkPackageFeatureExecutionPlanByWorkpackgeId(workPackage
									.getWorkPackageId());
					for (WorkPackageFeatureExecutionPlan wpfepExisting : workPackageFeatureExecutionPlansExisting) {
						for (WorkPackageFeatureExecutionPlan wpfepNew : workPackageFeatureExecutionPlansNew) {
							if (wpfepNew
									.getFeature()
									.getProductFeatureId()
									.equals(wpfepExisting.getFeature()
											.getProductFeatureId())) {
								if (compareRunConfigurations(
										wpfepExisting.getRunConfiguration(),
										wpfepNew.getRunConfiguration())) {
									if (wpfepExisting.getPlannedExecutionDate() != null) {
										existingPED = DateUtility
												.sdfDateformatWithOutTime(wpfepExisting
														.getPlannedExecutionDate());
										plannedExecutionDateDifference = DateUtility
												.DateDifference(existingWPPSD,
														existingPED);
										newPED = DateUtility
												.addOnlyWorkingDays(
														workPackage
																.getPlannedStartDate(),
														(int) plannedExecutionDateDifference);

										wpfepNew.setPlannedExecutionDate(newPED);
										workPackageDAO
												.updateWorkPackageFeatureExecutionPlan(wpfepNew);
									}
								}
							}
						}
					}

					List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlansExisting = workPackageDAO
							.getWorkPackageTestSuiteExecutionPlanByWorkpackgeId(workpPackageExisting
									.getWorkPackageId());
					List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlansNew = workPackageDAO
							.getWorkPackageTestSuiteExecutionPlanByWorkpackgeId(workPackage
									.getWorkPackageId());
					for (WorkPackageTestSuiteExecutionPlan wptsepExisting : workPackageTestSuiteExecutionPlansExisting) {
						for (WorkPackageTestSuiteExecutionPlan wptsepNew : workPackageTestSuiteExecutionPlansNew) {
							if (wptsepNew
									.getTestsuite()
									.getTestSuiteId()
									.equals(wptsepExisting.getTestsuite()
											.getTestSuiteId())) {
								if (compareRunConfigurations(
										wptsepExisting.getRunConfiguration(),
										wptsepNew.getRunConfiguration())) {
									if (wptsepExisting
											.getPlannedExecutionDate() != null) {
										existingPED = DateUtility
												.sdfDateformatWithOutTime(wptsepExisting
														.getPlannedExecutionDate());
										plannedExecutionDateDifference = DateUtility
												.DateDifference(existingWPPSD,
														existingPED);
										newPED = DateUtility
												.addOnlyWorkingDays(
														workPackage
																.getPlannedStartDate(),
														(int) plannedExecutionDateDifference);

										wptsepNew
												.setPlannedExecutionDate(newPED);
										workPackageDAO
												.updateWorkPackageTestSuiteExecutionPlan(wptsepNew);
									}
								}
							}
						}
					}

					for (WorkPackageTestCaseExecutionPlan wptcep : workPackageTestCaseExecutionPlansExisting) {
						for (WorkPackageTestCaseExecutionPlan wptcepNew : workPackageTestCaseExecutionPlansNew) {
							if (wptcepNew
									.getTestCase()
									.getTestCaseId()
									.equals(wptcep.getTestCase()
											.getTestCaseId())) {
								if (compareRunConfigurations(
										wptcep.getRunConfiguration(),
										wptcepNew.getRunConfiguration())) {
									if (wptcep.getSourceType() != null) {
										if (wptcep.getSourceType().equals(
												wptcepNew.getSourceType())) {
											workPackageTestCaseExecutionPlanNew = workPackageDAO
													.getWorkpackageTestcaseExecutionPlanById(wptcepNew
															.getId());
											if (wptcep
													.getPlannedExecutionDate() != null) {
												existingPED = DateUtility
														.sdfDateformatWithOutTime(wptcep
																.getPlannedExecutionDate());
												plannedExecutionDateDifference = DateUtility
														.DateDifference(
																existingWPPSD,
																existingPED);
												newPED = DateUtility
														.addOnlyWorkingDays(
																workPackage
																		.getPlannedStartDate(),
																(int) plannedExecutionDateDifference);
												workPackageTestCaseExecutionPlanNew
														.setPlannedExecutionDate(newPED);
												workPackageDAO
														.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlanNew);
											}
										}

									}
								}
							}
						}
					}
				}

				WorkShiftMaster plannedWorkShiftMaster = null;
				if (conditionList != null && !conditionList.isEmpty()
						&& conditionList.contains("shift")) {
					Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlansExisting = workpPackageExisting
							.getWorkPackageTestCaseExecutionPlan();
					List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlansNew = workPackageDAO
							.getWorkPackageTestCaseExecutionPlanByWorkpackgeId(workPackage
									.getWorkPackageId());

					List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlansExisting = workPackageDAO
							.getWorkPackageFeatureExecutionPlanByWorkpackgeId(workpPackageExisting
									.getWorkPackageId());
					List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlansNew = workPackageDAO
							.getWorkPackageFeatureExecutionPlanByWorkpackgeId(workPackage
									.getWorkPackageId());
					for (WorkPackageFeatureExecutionPlan wpfepExisting : workPackageFeatureExecutionPlansExisting) {
						for (WorkPackageFeatureExecutionPlan wpfepNew : workPackageFeatureExecutionPlansNew) {
							if (wpfepNew
									.getFeature()
									.getProductFeatureId()
									.equals(wpfepExisting.getFeature()
											.getProductFeatureId())) {
								if (compareRunConfigurations(
										wpfepExisting.getRunConfiguration(),
										wpfepNew.getRunConfiguration())) {
									plannedWorkShiftMaster = wpfepExisting
											.getPlannedWorkShiftMaster();
									wpfepNew.setPlannedWorkShiftMaster(plannedWorkShiftMaster);
									workPackageDAO
											.updateWorkPackageFeatureExecutionPlan(wpfepNew);
								}
							}
						}
					}

					List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlansExisting = workPackageDAO
							.getWorkPackageTestSuiteExecutionPlanByWorkpackgeId(workpPackageExisting
									.getWorkPackageId());
					List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlansNew = workPackageDAO
							.getWorkPackageTestSuiteExecutionPlanByWorkpackgeId(workPackage
									.getWorkPackageId());
					for (WorkPackageTestSuiteExecutionPlan wptsepExisting : workPackageTestSuiteExecutionPlansExisting) {
						for (WorkPackageTestSuiteExecutionPlan wptsepNew : workPackageTestSuiteExecutionPlansNew) {
							if (wptsepNew
									.getTestsuite()
									.getTestSuiteId()
									.equals(wptsepExisting.getTestsuite()
											.getTestSuiteId())) {
								if (compareRunConfigurations(
										wptsepExisting.getRunConfiguration(),
										wptsepNew.getRunConfiguration())) {
									plannedWorkShiftMaster = wptsepNew
											.getPlannedWorkShiftMaster();
									wptsepNew
											.setPlannedWorkShiftMaster(plannedWorkShiftMaster);
									workPackageDAO
											.updateWorkPackageTestSuiteExecutionPlan(wptsepNew);
								}
							}
						}
					}

					for (WorkPackageTestCaseExecutionPlan wptcep : workPackageTestCaseExecutionPlansExisting) {
						for (WorkPackageTestCaseExecutionPlan wptcepNew : workPackageTestCaseExecutionPlansNew) {
							if (wptcepNew
									.getTestCase()
									.getTestCaseId()
									.equals(wptcep.getTestCase()
											.getTestCaseId())) {
								if (compareRunConfigurations(
										wptcep.getRunConfiguration(),
										wptcepNew.getRunConfiguration())) {
									if (wptcep.getSourceType().equals(
											wptcepNew.getSourceType())) {
										workPackageTestCaseExecutionPlanNew = workPackageDAO
												.getWorkpackageTestcaseExecutionPlanById(wptcepNew
														.getId());
										plannedWorkShiftMaster = wptcep
												.getPlannedWorkShiftMaster();
										workPackageTestCaseExecutionPlanNew
												.setPlannedWorkShiftMaster(plannedWorkShiftMaster);
										workPackageDAO
												.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlanNew);
									}
								}
							}
						}
					}
				}
				long demanddifference = 0;
				Date tempDate = null;

				if (productMaster.getProductMode().getModeId() == 1) {
					if (conditionList != null && !conditionList.isEmpty()
							&& conditionList.contains("demand")) {
						List<WorkPackageDemandProjection> wpdpExistList = workPackageDAO
								.listWorkPackageDemandProjectionByDate(
										workPackageIdExisting, -1, null);
						for (WorkPackageDemandProjection wpdp : wpdpExistList) {
							if (wpdp.getWorkDate() != null) {
								WorkPackageDemandProjection workPackageDemandProjection = new WorkPackageDemandProjection();
								workPackageDemandProjection
										.setWorkPackage(workPackage);
								workPackageDemandProjection
										.setWorkShiftMaster(wpdp
												.getWorkShiftMaster());
								workPackageDemandProjection.setSkill(wpdp
										.getSkill());
								workPackageDemandProjection.setUserRole(wpdp
										.getUserRole());
								demanddifference = DateUtility.DateDifference(
										existingWPPSD, DateUtility
												.sdfDateformatWithOutTime(wpdp
														.getWorkDate()));
								workPackageDemandProjection
										.setWorkDate(DateUtility
												.addOnlyWorkingDays(workPackage
														.getPlannedStartDate(),
														(int) demanddifference));
								workPackageDemandProjection
										.setResourceCount(wpdp
												.getResourceCount());
								workPackageDemandProjection
										.setDemandRaisedOn(new Date());
								workPackageDemandProjection
										.setDemandRaisedByUser(user);

								workPackageDAO
										.addWorkPackageDemandProjection(workPackageDemandProjection);
							}

						}
					}
				}
				Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlansExisting = workpPackageExisting
						.getWorkPackageTestCaseExecutionPlan();
				List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlansNew = workPackageDAO
						.getWorkPackageTestCaseExecutionPlanByWorkpackgeId(workPackage
								.getWorkPackageId());

				if (conditionList != null && !conditionList.isEmpty()
						&& conditionList.contains("testLead")) {
					Integer workpackageId = workPackage.getWorkPackageId();
					List<UserList> userListUnavaiable = new ArrayList<UserList>();
					List<UserList> userListBlocked = new ArrayList<UserList>();
					List<UserList> userListFinalBlocked = new ArrayList<UserList>();
					if (productMaster.getProductMode().getModeId() == 1) {
						List<WorkPackageDemandProjection> wpdpExistList = workPackageDAO
								.listWorkPackageDemandProjectionByDate(
										workPackageIdExisting, -1, null);
						for (WorkPackageDemandProjection wpdp : wpdpExistList) {
							if (wpdp.getWorkDate() != null) {
								if (wpdp.getUserRole()
										.getUserRoleId()
										.equals(IDPAConstants.ROLE_ID_TEST_LEAD)) {
									demanddifference = DateUtility
											.DateDifference(
													existingWPPSD,
													DateUtility
															.sdfDateformatWithOutTime(wpdp
																	.getWorkDate()));
									tempDate = DateUtility.addOnlyWorkingDays(
											workPackage.getPlannedStartDate(),
											(int) demanddifference);
									List<JsonUserList> jsonUserListUnavaiable = resourceManagementService
											.getAllUnReservedResourcesForReservation(
													workpackageId.intValue(),
													wpdp.getWorkShiftMaster()
															.getShiftId(),
													tempDate, "All");
									for (JsonUserList u : jsonUserListUnavaiable) {
										userListUnavaiable.add(userListDAO
												.getByUserId(u.getUserId()));
									}
									List<JsonUserList> jsonBlockedResourceList = resourceManagementService
											.getBlockedResourcesOfWorkPackage(
													workPackageIdExisting,
													wpdp.getWorkShiftMaster()
															.getShiftId(), wpdp
															.getWorkDate());
									if (jsonBlockedResourceList != null
											&& !jsonBlockedResourceList
													.isEmpty()) {
										for (JsonUserList u : jsonBlockedResourceList) {
											userListBlocked
													.add(userListDAO.getByUserId(u
															.getUserId()));
										}
									}

									if (userListUnavaiable != null
											&& !userListUnavaiable.isEmpty()
											&& userListBlocked != null
											&& !userListBlocked.isEmpty()) {
										for (UserList ul : userListUnavaiable) {
											if (userListBlocked.contains(ul)) {
												if (ul.getUserRoleMaster()
														.getUserRoleId()
														.equals(IDPAConstants.ROLE_ID_TEST_LEAD)
														|| ul.getUserRoleMaster()
																.getUserRoleId()
																.equals(IDPAConstants.ROLE_ID_TEST_MANAGER)) {
													TestFactoryResourceReservation resourceReserved = resourceManagementService
															.saveReservedResource(
																	ul.getUserId(),
																	tempDate,
																	workpackageId
																			+ "",
																	wpdp.getWorkShiftMaster()
																			.getShiftId()
																			+ "",
																	user.getUserId()
																			+ "");
													userListFinalBlocked
															.add(ul);
												}
											}
										}
									}
								}
							}
						}
					}

					List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlansExisting = workPackageDAO
							.getWorkPackageFeatureExecutionPlanByWorkpackgeId(workpPackageExisting
									.getWorkPackageId());
					List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlansNew = workPackageDAO
							.getWorkPackageFeatureExecutionPlanByWorkpackgeId(workPackage
									.getWorkPackageId());
					for (WorkPackageFeatureExecutionPlan wpfepExisting : workPackageFeatureExecutionPlansExisting) {
						for (WorkPackageFeatureExecutionPlan wpfepNew : workPackageFeatureExecutionPlansNew) {
							if (wpfepNew
									.getFeature()
									.getProductFeatureId()
									.equals(wpfepExisting.getFeature()
											.getProductFeatureId())) {
								if (compareRunConfigurations(
										wpfepExisting.getRunConfiguration(),
										wpfepNew.getRunConfiguration())) {
									if (productMaster.getProductMode()
											.getModeId() != 1)
										userListFinalBlocked = productTeamResourcesDao
												.getProductTeamResourcesOfProduct(
														productId,
														DateUtility
																.sdfDateformatWithOutTime(wpfepNew
																		.getPlannedExecutionDate()),
														IDPAConstants.ROLE_ID_TEST_LEAD);
									if (userListFinalBlocked
											.contains(wpfepExisting
													.getTestLead())) {
										wpfepNew.setTestLead(wpfepExisting
												.getTestLead());
										workPackageDAO
												.updateWorkPackageFeatureExecutionPlan(wpfepNew);
									}
								}
							}
						}
					}

					List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlansExisting = workPackageDAO
							.getWorkPackageTestSuiteExecutionPlanByWorkpackgeId(workpPackageExisting
									.getWorkPackageId());
					List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlansNew = workPackageDAO
							.getWorkPackageTestSuiteExecutionPlanByWorkpackgeId(workPackage
									.getWorkPackageId());
					for (WorkPackageTestSuiteExecutionPlan wptsepExisting : workPackageTestSuiteExecutionPlansExisting) {
						for (WorkPackageTestSuiteExecutionPlan wptsepNew : workPackageTestSuiteExecutionPlansNew) {
							if (wptsepNew
									.getTestsuite()
									.getTestSuiteId()
									.equals(wptsepExisting.getTestsuite()
											.getTestSuiteId())) {
								if (compareRunConfigurations(
										wptsepExisting.getRunConfiguration(),
										wptsepNew.getRunConfiguration())) {
									if (productMaster.getProductMode()
											.getModeId() != 1)
										userListFinalBlocked = productTeamResourcesDao
												.getProductTeamResourcesOfProduct(
														productId,
														DateUtility
																.sdfDateformatWithOutTime(wptsepNew
																		.getPlannedExecutionDate()),
														IDPAConstants.ROLE_ID_TEST_LEAD);
									if (userListFinalBlocked
											.contains(wptsepExisting
													.getTestLead())) {
										wptsepNew.setTestLead(wptsepExisting
												.getTestLead());
										workPackageDAO
												.updateWorkPackageTestSuiteExecutionPlan(wptsepNew);
									}
								}
							}
						}
					}

					for (WorkPackageTestCaseExecutionPlan wptcep : workPackageTestCaseExecutionPlansExisting) {
						for (WorkPackageTestCaseExecutionPlan wptcepNew : workPackageTestCaseExecutionPlansNew) {
							if (wptcepNew
									.getTestCase()
									.getTestCaseId()
									.equals(wptcep.getTestCase()
											.getTestCaseId())) {
								if (compareRunConfigurations(
										wptcep.getRunConfiguration(),
										wptcepNew.getRunConfiguration())) {
									if (wptcep.getSourceType().equals(
											wptcepNew.getSourceType())) {
										if (productMaster.getProductMode()
												.getModeId() != 1)
											userListFinalBlocked = productTeamResourcesDao
													.getProductTeamResourcesOfProduct(
															productId,
															DateUtility
																	.sdfDateformatWithOutTime(wptcepNew
																			.getPlannedExecutionDate()),
															IDPAConstants.ROLE_ID_TEST_LEAD);
										if (userListFinalBlocked
												.contains(wptcep.getTestLead())) {
											workPackageTestCaseExecutionPlanNew = workPackageDAO
													.getWorkpackageTestcaseExecutionPlanById(wptcepNew
															.getId());
											workPackageTestCaseExecutionPlanNew
													.setExecutionStatus(1);
											workPackageTestCaseExecutionPlanNew
													.setTestLead(wptcep
															.getTestLead());
											workPackageDAO
													.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlanNew);
										}
									}
								}
							}
						}
					}
				}

				if (conditionList != null && !conditionList.isEmpty()
						&& conditionList.contains("tester")) {
					Integer workpackageId = workPackage.getWorkPackageId();
					List<UserList> userListUnavaiable = new ArrayList<UserList>();
					List<UserList> userListBlocked = new ArrayList<UserList>();
					List<UserList> userListFinalBlocked = new ArrayList<UserList>();

					if (productMaster.getProductMode().getModeId() == 1) {
						List<WorkPackageDemandProjection> wpdpExistList = workPackageDAO
								.listWorkPackageDemandProjectionByDate(
										workPackageIdExisting, -1, null);
						for (WorkPackageDemandProjection wpdp : wpdpExistList) {
							if (wpdp.getWorkDate() != null) {
								if (wpdp.getUserRole().getUserRoleId()
										.equals(IDPAConstants.ROLE_ID_TESTER)) {
									demanddifference = DateUtility
											.DateDifference(
													existingWPPSD,
													DateUtility
															.sdfDateformatWithOutTime(wpdp
																	.getWorkDate()));
									tempDate = DateUtility.addOnlyWorkingDays(
											workPackage.getPlannedStartDate(),
											(int) demanddifference);
									List<JsonUserList> jsonUserListUnavaiable = resourceManagementService
											.getAllUnReservedResourcesForReservation(
													workpackageId.intValue(),
													wpdp.getWorkShiftMaster()
															.getShiftId(),
													tempDate, "All");
									for (JsonUserList u : jsonUserListUnavaiable) {
										userListUnavaiable.add(userListDAO
												.getByUserId(u.getUserId()));
									}
									List<JsonUserList> jsonBlockedResourceList = resourceManagementService
											.getBlockedResourcesOfWorkPackage(
													workPackageIdExisting,
													wpdp.getWorkShiftMaster()
															.getShiftId(), wpdp
															.getWorkDate());

									if (jsonBlockedResourceList != null
											&& !jsonBlockedResourceList
													.isEmpty()) {
										for (JsonUserList u : jsonBlockedResourceList) {
											userListBlocked
													.add(userListDAO.getByUserId(u
															.getUserId()));
										}
									}

									if (userListUnavaiable != null
											&& !userListUnavaiable.isEmpty()
											&& userListBlocked != null
											&& !userListBlocked.isEmpty()) {
										for (UserList ul : userListUnavaiable) {
											if (userListBlocked.contains(ul)) {
												if (ul.getUserRoleMaster()
														.getUserRoleId()
														.equals(IDPAConstants.ROLE_ID_TESTER)) {
													TestFactoryResourceReservation resourceReserved = resourceManagementService
															.saveReservedResource(
																	ul.getUserId(),
																	tempDate,
																	workpackageId
																			+ "",
																	wpdp.getWorkShiftMaster()
																			.getShiftId()
																			+ "",
																	user.getUserId()
																			+ "");

													userListFinalBlocked
															.add(ul);
												}
											}
										}
									}
								}
							}
						}
					}

					List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlansExisting = workPackageDAO
							.getWorkPackageFeatureExecutionPlanByWorkpackgeId(workpPackageExisting
									.getWorkPackageId());
					List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlansNew = workPackageDAO
							.getWorkPackageFeatureExecutionPlanByWorkpackgeId(workPackage
									.getWorkPackageId());
					for (WorkPackageFeatureExecutionPlan wpfepExisting : workPackageFeatureExecutionPlansExisting) {
						for (WorkPackageFeatureExecutionPlan wpfepNew : workPackageFeatureExecutionPlansNew) {
							if (wpfepNew
									.getFeature()
									.getProductFeatureId()
									.equals(wpfepExisting.getFeature()
											.getProductFeatureId())) {
								if (compareRunConfigurations(
										wpfepExisting.getRunConfiguration(),
										wpfepNew.getRunConfiguration())) {
									if (productMaster.getProductMode()
											.getModeId() != 1)
										userListFinalBlocked = productTeamResourcesDao
												.getProductTeamResourcesOfProduct(
														productId,
														DateUtility
																.sdfDateformatWithOutTime(wpfepNew
																		.getPlannedExecutionDate()),
														IDPAConstants.ROLE_ID_TESTER);
									if (userListFinalBlocked
											.contains(wpfepExisting.getTester())) {
										wpfepNew.setTester(wpfepExisting
												.getTester());
										workPackageDAO
												.updateWorkPackageFeatureExecutionPlan(wpfepNew);
									}
								}
							}
						}
					}

					List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlansExisting = workPackageDAO
							.getWorkPackageTestSuiteExecutionPlanByWorkpackgeId(workpPackageExisting
									.getWorkPackageId());
					List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlansNew = workPackageDAO
							.getWorkPackageTestSuiteExecutionPlanByWorkpackgeId(workPackage
									.getWorkPackageId());
					for (WorkPackageTestSuiteExecutionPlan wptsepExisting : workPackageTestSuiteExecutionPlansExisting) {
						for (WorkPackageTestSuiteExecutionPlan wptsepNew : workPackageTestSuiteExecutionPlansNew) {
							if (wptsepNew
									.getTestsuite()
									.getTestSuiteId()
									.equals(wptsepExisting.getTestsuite()
											.getTestSuiteId())) {
								if (compareRunConfigurations(
										wptsepExisting.getRunConfiguration(),
										wptsepNew.getRunConfiguration())) {
									if (productMaster.getProductMode()
											.getModeId() != 1)
										userListFinalBlocked = productTeamResourcesDao
												.getProductTeamResourcesOfProduct(
														productId,
														DateUtility
																.sdfDateformatWithOutTime(wptsepNew
																		.getPlannedExecutionDate()),
														IDPAConstants.ROLE_ID_TESTER);
									if (userListFinalBlocked
											.contains(wptsepExisting
													.getTester())) {
										wptsepNew.setTester(wptsepExisting
												.getTester());
										workPackageDAO
												.updateWorkPackageTestSuiteExecutionPlan(wptsepNew);
									}
								}
							}
						}
					}

					for (WorkPackageTestCaseExecutionPlan wptcep : workPackageTestCaseExecutionPlansExisting) {
						for (WorkPackageTestCaseExecutionPlan wptcepNew : workPackageTestCaseExecutionPlansNew) {
							if (wptcepNew
									.getTestCase()
									.getTestCaseId()
									.equals(wptcep.getTestCase()
											.getTestCaseId())) {
								if (compareRunConfigurations(
										wptcep.getRunConfiguration(),
										wptcepNew.getRunConfiguration())) {
									if (wptcep.getSourceType().equals(
											wptcepNew.getSourceType())) {
										if (productMaster.getProductMode()
												.getModeId() != 1)
											userListFinalBlocked = productTeamResourcesDao
													.getProductTeamResourcesOfProduct(
															productId,
															DateUtility
																	.sdfDateformatWithOutTime(wptcepNew
																			.getPlannedExecutionDate()),
															IDPAConstants.ROLE_ID_TESTER);
										if (userListFinalBlocked
												.contains(wptcep.getTester())) {
											workPackageTestCaseExecutionPlanNew = workPackageDAO
													.getWorkpackageTestcaseExecutionPlanById(wptcepNew
															.getId());
											workPackageTestCaseExecutionPlanNew
													.setTester(wptcep
															.getTester());
											workPackageTestCaseExecutionPlanNew
													.setExecutionStatus(1);
											workPackageDAO
													.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlanNew);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return workPackage;
	}

	public boolean compareEnvironments(Set<Environment> newEnvironments,
			Set<Environment> existingEnvironments) {
		boolean flag = true;
		if (newEnvironments.size() == existingEnvironments.size()) {
			for (Environment env : newEnvironments) {
				if (!existingEnvironments.contains(env)) {
					flag = false;
					return flag;
				}
			}
		} else {
			flag = false;
			return flag;
		}
		return flag;
	}

	public boolean compareRunConfigurations(WorkpackageRunConfiguration wprc,
			WorkpackageRunConfiguration wprcNew) {
		boolean flag = true;
		if (!wprc
				.getRunconfiguration()
				.getEnvironmentcombination()
				.equals(wprcNew.getRunconfiguration()
						.getEnvironmentcombination())) {
			flag = false;
			return flag;
		} else {
			return flag;
		}
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlan(
			Integer workPackageId, UserList user, String plannedExecutionDate,
			String filter, WorkpackageRunConfiguration wpRunConfigObj) {
	
		return workPackageDAO.listWorkPackageTestCasesExecutionPlan(
				workPackageId, user, plannedExecutionDate, filter,
				wpRunConfigObj);
	}

	@Override
	@Transactional
	public List<PerformanceLevel> getPerformanceRating() {
		return workPackageDAO.listPerformanceRating();
	}

	@Override
	@Transactional
	public List<UserList> getAllocatedUserListByRole(Integer workpackageId,
			Integer roleId) {
	
		return workPackageDAO.getAllocatedUserListByRole(workpackageId, roleId);
	}

	@Override
	@Transactional
	public WorkPackageTestCase getWorkpackageTestCaseById(
			Integer workPackageTestCaseId) {
	
		return workPackageDAO.getWorkPackageTestCaseById(workPackageTestCaseId);
	}

	/* Bulk operation for Testsuite */
	@Override
	@Transactional
	public WorkPackageTestSuite getWorkpackageTestSuiteById(
			Integer workPackageTestSuiteId) {

		return workPackageDAO
				.getWorkPackageTestSuiteById(workPackageTestSuiteId);
	}

	/* Bulk operation for Testsuite End */

	@Override
	@Transactional
	public List<TestCaseDTO> listWorkPackageTestCasesExecutionPlanByWorkPackageIdGroupByTestcase(
			int workPackageId, String envId, String executionPriority,
			String result, int jtStartIndex, int jtPageSize, int sortBy,
			int testcaseId) {
	

		List<TestCaseDTO> testCaseDTOList = new ArrayList<TestCaseDTO>(0);
		if (sortBy == -1) {
			testCaseDTOList = getWorkPackageResultSummary(workPackageId);
		} else if (sortBy == 1) {
			testCaseDTOList = getworkPackageTestRunJobSummary(workPackageId);

		} else if (sortBy == 5) {
			testCaseDTOList = listFeaturesByexecutionPriorityWise(workPackageId);

		} else if (sortBy == 2) {
			testCaseDTOList = getWorkPackageTestCaseExeResultSummary(workPackageId);

		} else if (sortBy == 4) {

			testCaseDTOList = getTestCaseExecutionPriorityWiseSummary(workPackageId);
		} else {
			testCaseDTOList = workPackageDAO
					.listWorkPackageTestCasesExecutionPlanByWorkPackageIdGroupByTestcase(
							workPackageId, envId, executionPriority, result,
							jtStartIndex, jtPageSize, sortBy, testcaseId);
		}
		return testCaseDTOList;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanByWorkPackageId(
			int workPackageId, int testcaseId) {
	
		return workPackageDAO
				.listWorkPackageTestCasesExecutionPlanByWorkPackageId(
						workPackageId, testcaseId);
	}

	@Override
	@Transactional
	public void updateTestCaseExecutionResult(
			TestCaseExecutionResult testCaseExecutionResult) {
	
		workPackageDAO.updateTestCaseExecutionResult(testCaseExecutionResult);
	}

	@Override
	@Transactional
	public void addTestCaseConfiguration(
			TestCaseConfiguration testCaseConfiguration) {
	
		workPackageDAO.addTestCaseConfiguration(testCaseConfiguration);
	}

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> getUserByBlockedStatus(
			int workPackageId, int roleId, String plannedExecutionDate,
			int shiftId) {
	
		return workPackageDAO.getUserByBlockedStatus(workPackageId, roleId,
				plannedExecutionDate, shiftId);
	}

	@Override
	@Transactional
	public List<WorkPackage> listWorkPackagesByUserIdAndPlannedExecutionDate(
			int userId, String plannedExecutionDate) {
	
		return workPackageDAO.listWorkPackagesByUserIdAndPlannedExecutionDate(
				userId, plannedExecutionDate);
	}

	@Override
	@Transactional
	public ExecutionPriority getExecutionPriorityByName(String priorityValue) {
	
		return workPackageDAO.getExecutionPriorityByName(priorityValue);
	}

	@Override
	@Transactional
	public int addWorkPackageTestcaseExecutionPlan(
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList) {
	
		return workPackageDAO
				.addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanList);
	}

	@Override
	@Transactional
	public void deleteTestCaseConfigurationByWPTCEID(Integer wptcep) {
	
		workPackageDAO.deleteTestCaseConfigurationByWPTCEID(wptcep);
	}

	@Override
	@Transactional
	public void deleteWorkPackageTestCaseExecutionPlan(
			WorkPackageTestCaseExecutionPlan wptcep) {
	
		workPackageDAO.deleteWorkPackageTestCaseExecutionPlan(wptcep);
	}

	@Override
	@Transactional
	public WorkPackage mapWorkpackageEnvCombination(int workpackageId,
			int envId, String action) {
	
		return workPackageDAO.mapWorkpackageEnvCombination(workpackageId,
				envId, action);
	}

	@Override
	@Transactional
	public List<TestCaseConfiguration> listTestCaseConfigurations(
			int workpackageId) {
	
		return workPackageDAO.listTestCaseConfigurations(workpackageId);
	}

	@Override
	@Transactional
	public void deleteTestStepResult(Integer testCaseExecutionResultId) {
	
		workPackageDAO.deleteTestStepResult(testCaseExecutionResultId);
	}

	@Override
	@Transactional
	public JTableResponse listWorkpackageTestSuite(Integer workPackageId,
			Integer jStartIndex, Integer jPageSize) {
	

		JTableResponse jTableResponse = null;
		List<WorkPackageTestSuite> wptcTotal = null;

		WorkPackage workPackage = workPackageDAO
				.getWorkPackageById(workPackageId);
		int productId = workPackage.getProductBuild().getProductVersion()
				.getProductMaster().getProductId();
		ExecutionTypeMaster executionTypeMaster = workPackage
				.getWorkPackageType();
		int testSuiteExecutionId = -1;
		if (executionTypeMaster.getExecutionTypeId() == 7)
			testSuiteExecutionId = 3;

		List<TestSuiteList> allTestSuiteLists = testSuiteListDAO
				.getTestSuiteByProductId(productId, testSuiteExecutionId);
		List<WorkPackageTestSuite> workPackageTestSuitesTotal = workPackageDAO
				.listWorkPackageTestSuite(workPackageId, 0, 0);

		List<WorkPackageTestSuite> workPackageTestSuites = workPackageDAO
				.listWorkPackageTestSuite(workPackageId, 0, 0);

		List<JsonWorkPackageTestSuite> jsonWorkPackageTestSuites = new ArrayList<JsonWorkPackageTestSuite>();
		int pageSize = 0;
		int productTestSuiteCount = 0;
		if (allTestSuiteLists != null)
			productTestSuiteCount = allTestSuiteLists.size();
		JsonWorkPackageTestSuite jsonWorkPackageTestSuite = null;
		if (workPackageTestSuitesTotal == null
				|| workPackageTestSuitesTotal.isEmpty()) {
			// TODO : Initialize the workpackage with the test cases for the
			// product
			log.info("There are no testsuite for this WPTC. Initializing");
			int count = initializeWorkPackageWithTestSuite(workPackage,
					allTestSuiteLists);
			wptcTotal = workPackageDAO.listWorkPackageTestSuite(workPackageId,
					0, 0);

			if (wptcTotal != null && !wptcTotal.isEmpty()) {
				pageSize = wptcTotal.size();
			}

			workPackageTestSuites = workPackageDAO.listWorkPackageTestSuite(
					workPackageId, jStartIndex, jPageSize);

			for (WorkPackageTestSuite workPackageTestSuite : workPackageTestSuites) {
				jsonWorkPackageTestSuite = new JsonWorkPackageTestSuite(
						workPackageTestSuite);
				jsonWorkPackageTestSuites.add(jsonWorkPackageTestSuite);
			}

			jTableResponse = new JTableResponse("OK",
					jsonWorkPackageTestSuites, pageSize);
			workPackageTestSuites = null;

		} else if (workPackageTestSuitesTotal.size() < productTestSuiteCount) {
			// Seed the remaining test cases
			List<TestSuiteList> newTestSuites = new ArrayList<TestSuiteList>();
			List<TestSuiteList> testSuiteListForWorkpackage = new ArrayList<TestSuiteList>();
			for (WorkPackageTestSuite workPackageTestSuite : workPackageTestSuitesTotal) {
				testSuiteListForWorkpackage.add(workPackageTestSuite
						.getTestSuite());
			}

			for (TestSuiteList testsuite : allTestSuiteLists) {
				if (!testSuiteListForWorkpackage.contains(testsuite)) {
					newTestSuites.add(testsuite);
				}
			}

			int count = workPackageDAO.seedWorkPackageWithNewTestSuites(
					newTestSuites, workPackageId);
			wptcTotal = workPackageDAO.listWorkPackageTestSuite(workPackageId,
					0, 0);

			if (wptcTotal != null && !wptcTotal.isEmpty()) {
				pageSize = wptcTotal.size();
			}
			workPackageTestSuites = workPackageDAO.listWorkPackageTestSuite(
					workPackageId, jStartIndex, jPageSize);
			for (WorkPackageTestSuite workPackageTestSuite : workPackageTestSuites) {
				jsonWorkPackageTestSuite = new JsonWorkPackageTestSuite(
						workPackageTestSuite);
				jsonWorkPackageTestSuites.add(jsonWorkPackageTestSuite);
			}

			jTableResponse = new JTableResponse("OK",
					jsonWorkPackageTestSuites, pageSize);
			workPackageTestSuites = null;

		} else {
			log.info("workPackageTestSuitesTotal count>>>>"
					+ workPackageTestSuitesTotal.size());
			wptcTotal = workPackageDAO.listWorkPackageTestSuite(workPackageId,
					0, 0);

			if (wptcTotal != null && !wptcTotal.isEmpty()) {
				pageSize = wptcTotal.size();
			}
			for (WorkPackageTestSuite workPackageTestSuite : workPackageTestSuites) {
				jsonWorkPackageTestSuite = new JsonWorkPackageTestSuite(
						workPackageTestSuite);
				jsonWorkPackageTestSuites.add(jsonWorkPackageTestSuite);
			}

			jTableResponse = new JTableResponse("OK",
					jsonWorkPackageTestSuites, pageSize);
			workPackageTestSuites = null;
		}
		return jTableResponse;
	}

	@Override
	@Transactional
	public int initializeWorkPackageWithTestSuite(WorkPackage workPackage,
			List<TestSuiteList> productTestSuite) {

		// Get all the test suite for the product of the workpackage
		int workPackagesTestSuiteCount = 0;
		if (productTestSuite == null || productTestSuite.isEmpty()) {
			log.info("There are no testsuite specified for the product for this workpackage : "
					+ workPackage.getName());
			return 0;
		} else {

			List<WorkPackageTestSuite> workPackageTestSuites = new ArrayList<WorkPackageTestSuite>();
			WorkPackageTestSuite workPackageTestSuite = null;
			for (TestSuiteList testSuite : productTestSuite) {

				workPackageTestSuite = new WorkPackageTestSuite();
				workPackageTestSuite.setTestSuite(testSuite);
				workPackageTestSuite.setWorkPackage(workPackage);
				workPackageTestSuite.setCreatedDate(new Date(System
						.currentTimeMillis()));
				workPackageTestSuite.setEditedDate(new Date(System
						.currentTimeMillis()));
				workPackageTestSuite.setIsSelected(0);
				workPackageTestSuite.setStatus("ACTIVE");

				workPackageTestSuites.add(workPackageTestSuite);
			}
			log.info("New testcases to be added for the workpackage : "
					+ workPackage.getWorkPackageId() + " : count : "
					+ workPackageTestSuites.size());
			workPackagesTestSuiteCount = workPackageDAO
					.addNewWorkPackageTestSuite(workPackageTestSuites);
		}
		return workPackagesTestSuiteCount;
	}

	@Override
	@Transactional
	public int initializeWorkPackageWithFeature(WorkPackage workPackage,
			List<ProductFeature> productFeature) {

		// Get all the test suite for the product of the workpackage
		int workPackagesFeatureCount = 0;
		if (workPackage == null) {
			return 0;
		}
		if (productFeature == null || productFeature.isEmpty()) {
			log.info("There are no testsuite specified for the product for this workpackage : "
					+ workPackage.getName());
			return 0;
		} else {

			List<WorkPackageFeature> workPackageFeatures = new ArrayList<WorkPackageFeature>();
			WorkPackageFeature workPackageFEature = null;
			for (ProductFeature feature : productFeature) {

				workPackageFEature = new WorkPackageFeature();
				workPackageFEature.setFeature(feature);
				workPackageFEature.setWorkPackage(workPackage);
				workPackageFEature.setCreatedDate(new Date(System
						.currentTimeMillis()));
				workPackageFEature.setEditedDate(new Date(System
						.currentTimeMillis()));
				workPackageFEature.setIsSelected(0);
				workPackageFEature.setStatus("ACTIVE");

				workPackageFeatures.add(workPackageFEature);
			}
			log.info("New testcases to be added for the workpackage : "
					+ workPackage.getWorkPackageId() + " : count : "
					+ workPackageFeatures.size());
			workPackagesFeatureCount = workPackageDAO
					.addNewWorkPackageFeature(workPackageFeatures);
		}
		return workPackagesFeatureCount;
	}

	@Override
	@Transactional
	public Float listWorkpackageDemandProdjectionByDate(Date selectedDate,
			Integer testFactoryLabId, Integer shiftTypeId) {
	
		Float resourceCount = 0f;
		List<WorkPackageDemandProjectionDTO> listOfWorkPackageDemands = workPackageDAO
				.listWorkpackageDemandProdjectionByDate(selectedDate,
						testFactoryLabId, shiftTypeId);
		if (listOfWorkPackageDemands != null
				&& listOfWorkPackageDemands.size() > 0) {
			for (WorkPackageDemandProjectionDTO workPackageDemandProjectionDTO : listOfWorkPackageDemands) {
				resourceCount = workPackageDemandProjectionDTO
						.getResourceCount();

			}
		}
		return resourceCount;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanByWorkPackageIdGroupByTestcase(
			int workPackageId, String envId, String executionPriority,
			String result, int jtStartIndex, int jtPageSize, int sortBy) {
	
		return null;
	}

	@Override
	@Transactional
	public WorkPackageTestSuite updateWorkPackageTestSuite(
			WorkPackageTestSuite workPackageTestSuite,
			JsonWorkPackageTestSuite jsonWorkPackageTestSuite) {
	
		log.info("updade method"
				+ workPackageTestSuite.getWorkpackageTestSuiteId());

		WorkPackageTestSuite workPackageTestSuiteFromDB = workPackageDAO
				.getWorkPackageTestSuiteById(workPackageTestSuite
						.getWorkpackageTestSuiteId());

		workPackageTestSuite.setCreatedDate(workPackageTestSuiteFromDB
				.getCreatedDate());
		WorkPackage workPackage = null;
		WorkPackageTestSuite updatedWorkPackageTestSuite = null;
		List<WorkPackageTestCaseExecutionPlan> currentWorkPackageTestCaseExecutionPlanList = null;
		List<WorkPackageTestCaseExecutionPlan> finalWorkPackageTestCaseExecutionPlanList = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		workPackage = workPackageDAO.getWorkPackageById(workPackageTestSuite
				.getWorkPackage().getWorkPackageId());
		TestSuiteList testSuiteList = workPackageTestSuite.getTestSuite();
		log.info(">>>>>>>>>>>>>"
				+ workPackageTestSuite.getTestSuite().getTestSuiteId());
		Set<TestCaseList> testCaseListSet = null;
		Set<TestCaseList> childTestCaseListSet = null;

		testCaseListSet = workPackageTestSuite.getTestSuite()
				.getTestCaseLists();

		if (workPackageTestSuiteFromDB.getIsSelected() == workPackageTestSuite
				.getIsSelected()) {
			log.info("No change in Test case selection status. Check for env value selections change");
			updatedWorkPackageTestSuite = workPackageTestSuite;
		} else {

			log.info("Updated Test case selection status : "
					+ workPackageTestSuite.getIsSelected());
			// There is a change in the is selected value. Update the test
			// package first.
			workPackageTestSuite.setWorkPackage(workPackage);
			workPackageTestSuite.setTestSuite(testSuiteList);
			workPackageTestSuiteFromDB.setIsSelected(workPackageTestSuite
					.getIsSelected());
			updatedWorkPackageTestSuite = workPackageDAO
					.updateWorkPackageTestSuite(workPackageTestSuiteFromDB);
			if (workPackageTestSuite.getIsSelected() == 1) {
				if (workPackageTestSuite.getWorkPackage().getWorkFlowEvent()
						.getWorkFlow().getStageId() != IDPAConstants.WORKFLOW_STAGE_ID_PLANNING
						&& workPackageTestSuite.getWorkPackage()
								.getWorkFlowEvent().getWorkFlow()
								.getStageValue() < 20) {
					WorkFlow workFlow = workPackageDAO
							.getWorkFlowByEntityIdStageId(
									IDPAConstants.WORKPACKAGE_ENTITY_ID,
									IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
					WorkFlowEvent workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					workFlowEvent.setRemarks("Planning Workapckage :"
							+ workPackage.getName());
					UserList user = userListDAO
							.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
					workFlowEvent.setUser(user);
					workFlowEvent.setWorkFlow(workFlow);
					workPackage.setWorkFlowEvent(workFlowEvent);
					workPackageDAO.addWorkFlowEvent(workFlowEvent);
					workPackageDAO.updateWorkPackage(workPackage);
				}
				WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan = null;
				workPackageDAO.mapWorkpackageWithTestSuite(workPackageTestSuite
						.getWorkPackage().getWorkPackageId(), testSuiteList
						.getTestSuiteId(), "Add");
				List<WorkpackageRunConfiguration> runConfigurations = workPackageDAO
						.getWorkpackageRunConfigurationList(
								workPackageTestSuite.getWorkPackage()
										.getWorkPackageId(), null, "testsuite");
				if (runConfigurations != null && !runConfigurations.isEmpty()) {
					for (WorkpackageRunConfiguration rc : runConfigurations) {
						TestRunJob testRunJob = getTestRunJobByWP(workPackage,
								rc.getRunconfiguration());
						if (testRunJob != null) {
							mapTestRunJobTestSuite(
									testRunJob.getTestRunJobId(),
									testSuiteList.getTestSuiteId(), "Add");
						} else {
							addTestRunJob(rc.getRunconfiguration(), null,
									workPackage, null);
							testRunJob = getTestRunJobByWP(workPackage,
									rc.getRunconfiguration());
							mapTestRunJobTestSuite(
									testRunJob.getTestRunJobId(),
									testSuiteList.getTestSuiteId(), "Add");
							if (testRunJob != null) {
								if (testRunJob.getTestRunJobId() != null)
									mongoDBService
											.addTestRunJobToMongoDB(testRunJob
													.getTestRunJobId());
							}

						}
						workPackageTestSuiteExecutionPlan = new WorkPackageTestSuiteExecutionPlan();
						workPackageTestSuiteExecutionPlan
								.setCreatedDate(DateUtility.getCurrentTime());
						workPackageTestSuiteExecutionPlan
								.setTestsuite(testSuiteList);
						workPackageTestSuiteExecutionPlan
								.setModifiedDate(DateUtility.getCurrentTime());
						workPackageTestSuiteExecutionPlan
								.setPlannedExecutionDate(workPackage
										.getPlannedStartDate());
						workPackageTestSuiteExecutionPlan
								.setRunConfiguration(rc);
						workPackageTestSuiteExecutionPlan
								.setWorkPackage(workPackage);
						workPackageTestSuiteExecutionPlan.setStatus(1);
						workPackageTestSuiteExecutionPlan
								.setTestRunJob(testRunJob);
						ExecutionPriority executionPriority = null;
						if (workPackageTestSuite.getTestSuite()
								.getExecutionPriority() != null)
							executionPriority = workPackageDAO
									.getExecutionPriorityByName(CommonUtility
											.getExecutionPriority(workPackageTestSuite
													.getTestSuite()
													.getExecutionPriority()
													.getPriorityName()));
						else
							executionPriority = workPackageDAO
									.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
						workPackageTestSuiteExecutionPlan
								.setExecutionPriority(executionPriority);

						workPackageDAO
								.addWorkpackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlan);
					}
				}
				for (TestCaseList testCaseList : testCaseListSet) {
					if (testCaseList != null) {
						testCaseList = testCaseListDAO
								.getByTestCaseId(testCaseList.getTestCaseId());

						List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate = new ArrayList<WorkPackageTestCaseExecutionPlan>();
						if (runConfigurations != null
								&& !runConfigurations.isEmpty()) {
							for (WorkpackageRunConfiguration rc : runConfigurations) {
								TestRunJob testRunJob = getTestRunJobByWP(
										workPackage, rc.getRunconfiguration());

								WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
								workPackageTestCaseExecutionPlan
										.setTestCase(testCaseList);
								workPackageTestCaseExecutionPlan
										.setTestSuiteList(testSuiteList);
								workPackageTestCaseExecutionPlan
										.setWorkPackage(workPackage);
								workPackageTestCaseExecutionPlan
										.setRunConfiguration(rc);
								workPackageTestCaseExecutionPlan
										.setExecutionStatus(3);
								workPackageTestCaseExecutionPlan
										.setIsExecuted(0);
								workPackageTestCaseExecutionPlan
										.setSourceType("TestSuite");
								workPackageTestCaseExecutionPlan
										.setPlannedExecutionDate(workPackage
												.getPlannedStartDate());
								workPackageTestCaseExecutionPlan.setStatus(1);
								workPackageTestCaseExecutionPlan
										.setTestRunJob(testRunJob);
								ExecutionPriority executionPriority = null;
								System.out.println("testcase prority>>"
										+ testCaseList.getTestCasePriority());
								if (testCaseList.getTestCasePriority() != null)
									executionPriority = workPackageDAO
											.getExecutionPriorityByName(CommonUtility
													.getExecutionPriority(testCaseList
															.getTestCasePriority()
															.getPriorityName()));
								else
									executionPriority = workPackageDAO
											.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
								workPackageTestCaseExecutionPlan
										.setExecutionPriority(executionPriority);
								TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
								testCaseExecutionResult.setResult("");
								testCaseExecutionResult.setComments("");
								testCaseExecutionResult.setDefectsCount(0);
								testCaseExecutionResult.setDefectIds("");
								testCaseExecutionResult.setIsApproved(0);
								testCaseExecutionResult.setIsReviewed(0);
								testCaseExecutionResult.setObservedOutput("");

								// Commented the following statements when
								// remove the WorkPackageTestcaseExecutionId
								// column from Test Case Execution Result table.
								// By: Logeswari, On : 11-Feb-2015
								testCaseExecutionResult
										.setWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
								workPackageTestCaseExecutionPlan
										.setTestCaseExecutionResult(testCaseExecutionResult);
								workPackageTestCaseExecutionPlanListForUpdate
										.add(workPackageTestCaseExecutionPlan);

							}
							addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
						}
					}
				}
			}
			// If the testcase was previously selected and now not selected,
			// then clear all workpackagetestcaseexecutionplan rows
			// as these not be there in the execution plan
			if (workPackageTestSuite.getIsSelected() == 0) {
				try {
					log.info("Clearing all test cases from plan as test case has been de-selected : "
							+ workPackageTestSuite.getIsSelected());

					List<WorkpackageRunConfiguration> runConfigurations = workPackageDAO
							.getWorkpackageRunConfigurationList(
									workPackageTestSuite.getWorkPackage()
											.getWorkPackageId(), null,
									"testsuite");
					if (runConfigurations != null
							&& !runConfigurations.isEmpty()) {
						for (WorkpackageRunConfiguration rc : runConfigurations) {
							TestRunJob testRunJob = workPackageDAO
									.getTestRunJobByWP(workPackage,
											rc.getRunconfiguration());
							workPackageDAO.mapTestRunJobTestSuite(
									testRunJob.getTestRunJobId(),
									testSuiteList.getTestSuiteId(), "Remove");
							if (testRunJob.getRunConfiguration()
									.getRunconfigId() == rc
									.getRunconfiguration().getRunconfigId()) {

								if (testRunJob.getTestSuiteSet().isEmpty()
										&& testRunJob.getTestCaseListSet()
												.isEmpty()
										&& testRunJob.getFeatureSet().isEmpty()) {
									environmentDAO.deleteTestRunJob(workPackage
											.getWorkPackageId(), rc
											.getRunconfiguration()
											.getRunconfigId(), null);
								}
							}
						}

					}

					workPackageDAO.mapWorkpackageWithTestSuite(workPackage
							.getWorkPackageId(), workPackageTestSuite
							.getTestSuite().getTestSuiteId(), "Remove");
					WorkPackageTestSuiteExecutionPlan wptsp = workPackageDAO
							.getWorkpackageTestSuiteExecutionPlan(workPackage
									.getWorkPackageId(), workPackageTestSuite
									.getTestSuite().getTestSuiteId(), 0);
					if (wptsp != null)
						workPackageDAO
								.deleteWorkpackageTestSuiteExecutionPlan(wptsp);

					for (TestCaseList testCaseList : testCaseListSet) {
						currentWorkPackageTestCaseExecutionPlanList = workPackageDAO
								.getWorkPackageTestcaseExecutionPlanByWorkPackageId(
										workPackage.getWorkPackageId(),
										testCaseList.getTestCaseId(),
										workPackageTestSuite.getTestSuite()
												.getTestSuiteId(), -1,
										"TestSuite");

						workPackageDAO
								.deleteWorkPackageTestcaseExecutionPlan(currentWorkPackageTestCaseExecutionPlanList);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return updatedWorkPackageTestSuite;
			} else {

				log.info("Since the test case is now selected, continue to other options evaluation");
			}
		}

		// The test case is selected. Process the environments selection
		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = null;
		currentWorkPackageTestCaseExecutionPlanList = workPackageDAO
				.getWorkPackageTestcaseExecutionPlanByWorkPackageId(workPackage
						.getWorkPackageId(), workPackageTestSuite
						.getTestSuite().getTestCaseLists());
		int productId = workPackage.getProductBuild().getProductVersion()
				.getProductMaster().getProductId();
		List<Environment> environmentList = workPackageDAO
				.getEnvironmentListByProductId(productId);

		log.info("Evaluation individual env selection options");
		// User has selected some other environment value. process it env by env
		List<WorkPackageTestCaseExecutionPlan> removeTestCaseExecutionPlans = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		List<WorkPackageTestCaseExecutionPlan> updateTestCaseExecutionPlans = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		List<WorkPackageTestCaseExecutionPlan> newTestCaseExecutionPlans = new ArrayList<WorkPackageTestCaseExecutionPlan>();

		List<List<WorkPackageTestCaseExecutionPlan>> workPackageTestCaseExecutionPlanActions = new ArrayList<List<WorkPackageTestCaseExecutionPlan>>();
		workPackageTestCaseExecutionPlanActions.add(newTestCaseExecutionPlans);
		workPackageTestCaseExecutionPlanActions
				.add(removeTestCaseExecutionPlans);
		workPackageTestCaseExecutionPlanActions
				.add(updateTestCaseExecutionPlans);

		return updatedWorkPackageTestSuite;

	}

	@Override
	@Transactional
	public WorkPackage deleteRunConfigurationWorkpackage(Integer workpackageId,
			Integer runconfigId, String type) {
	
		return workPackageDAO.deleteRunConfigurationWorkpackage(workpackageId,
				runconfigId, type);
	}

	@Override
	@Transactional
	public WorkpackageRunConfiguration addRunConfigurationWorkpackage(
			Integer workpackageId, Integer runconfigId, String type) {
	
		return workPackageDAO.addRunConfigurationWorkpackage(workpackageId,
				runconfigId, type);
	}

	@Override
	@Transactional
	public WorkpackageRunConfiguration getWorkpackageRunConfiguration(
			Integer workpackageId, Integer runconfigId, String type) {
	
		return workPackageDAO.getWorkpackageRunConfiguration(workpackageId,
				runconfigId, type);
	}

	@Override
	@Transactional
	public List<WorkpackageRunConfiguration> getWorkpackageRunConfigurationList(
			Integer workpackageId, Integer runconfigId, String type) {
	
		return workPackageDAO.getWorkpackageRunConfigurationList(workpackageId,
				runconfigId, type);
	}

	@Override
	@Transactional
	public WorkpackageRunConfiguration getWorkpackageRunConfigurationByWPTCEP(
			Integer id) {
	
		return workPackageDAO.getWorkpackageRunConfigurationByWPTCEP(id);
	}

	@Override
	@Transactional
	public WorkPackage addWorkpackageToTestRunPlan(TestRunPlan testRunPlan,
			Map<String, String> mapData, UserList user, HttpServletRequest req,
			TestRunPlanGroup testRunPlanGroup, WorkPackage workPackage) {
	
		return workPackageDAO.addWorkpackageToTestRunPlan(testRunPlan, mapData,
				user, req, testRunPlanGroup, workPackage);
	}

	@Override
	@Transactional
	public WorkPackage addWorkpackageToTestRunPlan(TestRunPlan testRunPlan,
			Map<String, String> mapData, UserList user, HttpServletRequest req,
			TestRunPlanGroup testRunPlanGroup, WorkPackage workPackage,
			String deviceNames) {
		
		return workPackageDAO.addWorkpackageToTestRunPlan(testRunPlan, mapData,
				user, req, testRunPlanGroup, workPackage, deviceNames);
	}
	
	@Override
	@Transactional
	public  List<ISERecommandedTestcases>  getIntelligentTestPlanFromISE(TestRunPlan testRunPlan){		
		JSONObject finalObject = new JSONObject();
		List<TestCaseList> recommendedTestCasesList= new ArrayList<TestCaseList>();
		List<String> recommendedTestCases= new ArrayList<String>();
		JSONParser jsonParser = new JSONParser();		
		List<ISERecommandedTestcases> recommandedList=null;
		try {
			ProductMaster product=productMasterDAO.getProductByName(testRunPlan.getProductVersionListMaster().getProductMaster().getProductName());
			finalObject.put("username","admin@hcl.com");
			//Provide the list of defects fixed in this build. For now, pass an empty list
			finalObject.put("defectIds","");
			//Provide the max no of test cases ISE should recommend. Setting it to 1000 for now
			//TODO : Make a configurable property of Test Run Plan
			finalObject.put("maxNoOfTestCases","1000");
			//Provide the list of builds from whose history to learn
			//In this case, provide all the builds of the product
			if(product != null) {
				finalObject.put("projectName",product.getProductName());
				List<ProductVersionListMaster> productVersionList=productVersionListMasterDAO.list(testRunPlan.getProductVersionListMaster().getProductMaster().getProductId());
				JSONObject versionObject = new JSONObject();
				if(productVersionList != null && productVersionList.size() >0) {
					JSONArray buildIds = new JSONArray();
					for(ProductVersionListMaster productVersion:productVersionList) {
						List<ProductBuild> productBuildList=productBuildDAO.list(productVersion.getProductVersionListId());
						if(productBuildList != null && productBuildList.size() >0) {
							for(ProductBuild build:productBuildList) {
								JSONObject buildObj = new JSONObject();
								buildObj.put("relsname", productVersion.getProductVersionListId());
								buildObj.put("bldid",build.getProductBuildId());
								buildIds.add(buildObj);
							}
						}
					}
					finalObject.put("buildIds", buildIds);
				}
			}

			//Pass the features mapped to the build under test. This indicates the features that were modified in this build
			//TODO : Add Product build mapping to TestRun Plan
			Integer buildId = testRunPlan.getProductBuild().getProductBuildId();	
			System.out.println("Build Id : "+ buildId + " & Build Name : "+testRunPlan.getProductBuild().getBuildname());
			List<ProductFeature> featuresList = new ArrayList<ProductFeature>(); 
			featuresList = productListService.getFeatureListByProductIdAndVersionIdAndBuild(product.getProductId(),testRunPlan.getProductBuild().getProductVersion().getProductVersionListId(),buildId,1, 0, 10000); //This will be from the newly implemented Build - Feature mapping
			JSONObject features = new JSONObject();
			if(featuresList != null && featuresList.size() >0) {
				log.info("Feature List Size : "+featuresList.size()+" & Build : "+buildId);
				System.out.println("Feature List Size : "+featuresList.size()+" & Build : "+buildId);
				JSONArray featureIds = new JSONArray();
				for(ProductFeature feature:featuresList) {
					JSONObject featureObj = new JSONObject();
					if(feature.getProductFeatureName() != null)
						featureObj.put("featurename", feature.getProductFeatureName());
					if(feature.getExecutionPriority() != null && feature.getExecutionPriority().getPriorityName() != null && 
							!feature.getExecutionPriority().getPriorityName().isEmpty()){
						
						if(feature.getExecutionPriority().getPriorityName().equalsIgnoreCase("Critical") || feature.getExecutionPriority().getPriorityName().equalsIgnoreCase("High")){
							featureObj.put("priority", "HIGH");
						} else if(feature.getExecutionPriority().getPriorityName().equalsIgnoreCase("Medium") || feature.getExecutionPriority().getPriorityName().equalsIgnoreCase("Trivial")){
							featureObj.put("priority", "LOW");
						}
					} else {
						featureObj.put("priority", "HIGH");
					}
					featureIds.add(featureObj);
				}
				finalObject.put("features", featureIds);
			}
			//Get only the environment combinations in the Test Run Plan and pass to ISE.
			Set<EnvironmentCombination>  environmentCombinationList = new HashSet<EnvironmentCombination>();
			Set<RunConfiguration> rcList = testRunPlan.getRunConfigurationList();
			if(rcList != null && !rcList.isEmpty()){
				for(RunConfiguration rc : rcList){
					if(rc.getEnvironmentcombination() != null && rc.getEnvironmentcombination().getEnvionmentCombinationStatus().equals(1)) {
						environmentCombinationList.add(rc.getEnvironmentcombination());
					}
				}
			}
			
			if(environmentCombinationList != null && environmentCombinationList.size() >0) {
				JSONArray testbeds = new JSONArray();
				Double distrbt=Double.valueOf(100)/Double.valueOf(environmentCombinationList.size());
				for(EnvironmentCombination envCombination:environmentCombinationList) {
					JSONObject environObj = new JSONObject();
					environObj.put("envname", envCombination.getEnvironmentCombinationName());
					environObj.put("distrbt",distrbt);
					testbeds.add(environObj);
				}
				finalObject.put("testbeds", testbeds);
			}

			log.info("Test plan input :"+finalObject);
			String responseData= ISEServerAccesUtility.GetISERestServiceCall(iseServerURL,finalObject.toString(),iseRegressionOptimizationServiceName);			
			log.info("Final Result"+responseData);			
			recommandedList= generateJsonOutput(responseData,buildId);
			
			if(recommandedList != null && !recommandedList.isEmpty()){
				//Remove duplicate test case recommendations from ISE Analytical part
				Set<ISERecommandedTestcases> distinctRecommendedSet = new HashSet<ISERecommandedTestcases>();
				
				for(ISERecommandedTestcases iseRecommandedTestcase:recommandedList) {
					if(!validateDuplicateForISERecommandedTestCase(iseRecommandedTestcase.getTitle(),distinctRecommendedSet)) {
						distinctRecommendedSet.add(iseRecommandedTestcase);
					}
				}
				recommandedList = null;
				recommandedList = new ArrayList<ISERecommandedTestcases>();
				recommandedList.addAll(distinctRecommendedSet);
				log.info("After filtering duplicate records : "+ recommandedList.size());
			}
			
		} catch (Exception e) {
        	log.error("Problem after pushing  pushing testcase", e);
        }	
		return recommandedList;
	}
	
	public List<ISERecommandedTestcases> generateJsonOutput(String jsonInput,Integer buildId){
		JSONParser parser = new JSONParser();
		JSONObject inputJsonObj=null;
		List<ISERecommandedTestcases> overallResultList = new ArrayList<ISERecommandedTestcases>();
		try{
			log.info("Parsing JSON Input returned from ISE API : \n" + jsonInput+"\n");
			inputJsonObj=(JSONObject)parser.parse(jsonInput);

			if(inputJsonObj == null || inputJsonObj.get("result") == null || inputJsonObj.get("result").equals("Unknown error")){ 
				log.info("Unable to parse JSON from ISE as the format is not matching");
				return overallResultList; //Return empty list
			}
			//Get the First level result element
			JSONObject resultObj = new JSONObject();
			JSONObject parentResultObj =  (JSONObject)parser.parse(inputJsonObj.get("result").toString());
			//if ()
			String iseTestPlanName = null;
			//Look for planName element. This element indicates the presence of the results element that contains the recommended test cases
			if(parentResultObj.get("planNlame") != null && !parentResultObj.get("planNlame").equals("Unknown error")){
				log.info("Parsing ISE Recommendations : Got response of 2 level Results hierarchy");
				iseTestPlanName=(String)parentResultObj.get("planNlame");
				//This is the two level results hierarchy format
				//Get the result element containing the recommended testcases
				if(parentResultObj.get("result") != null){
					resultObj = (JSONObject)parentResultObj.get("result");
				} else {
					return overallResultList; //Return empty list
				}
			} else {
				//This structure contains 3 level hierarchy of results. Get the containing result object and then look for the final result object
				if ((parentResultObj.get("result") != null)) {
					log.info("Parsing ISE Recommendations : Got response of 3 level Results hierarchy");
					JSONObject childResultObj = (JSONObject)parser.parse(parentResultObj.get("result").toString());
					if(childResultObj.get("planNlame") != null){
						log.info("Parsing ISE Recommendations : Got Plan name in 3rd level");
						iseTestPlanName=(String)childResultObj.get("planNlame");
						//This is the two level results hierarchy format
						//Get the result element containing the recommended testcases
						if(childResultObj.get("result") != null){
							resultObj = (JSONObject)childResultObj.get("result");
						} else {
							return overallResultList; //Return empty list
						}
					} else {
						return overallResultList; //Return empty list
					}
				} else {
					return overallResultList; //Return empty list
				}
			}

			//Once the result object is obtained, parse it for recommendations
			if(resultObj != null){
				try {
					if(resultObj.get("CT") != null){
						JSONArray responseCTArr = (JSONArray)resultObj.get("CT");			
						log.info("CT"+responseCTArr.size());
						String category="CT";
						overallResultList.addAll(responseObjectFormatFromISE(responseCTArr, iseTestPlanName,buildId,category));
					}
				}catch(Exception e){
					log.error("Error in parsing : ",e);
				}
				
				try{					
					if(resultObj.get("ST") != null){
						JSONArray responseSTArr = (JSONArray)resultObj.get("ST");
						log.info("ST"+responseSTArr.size());
						String category="ST";
						overallResultList.addAll(responseObjectFormatFromISE(responseSTArr, iseTestPlanName,buildId,category));
					}
				} catch(Exception e){
					log.error("Error in parsing : ",e);
				}
				
				try{
					if(resultObj.get("BT") != null){
						JSONArray responseBTArr = (JSONArray)resultObj.get("BT");
						log.info("BT"+responseBTArr.size());
						String category="BT";
						overallResultList.addAll(responseObjectFormatFromISE(responseBTArr, iseTestPlanName,buildId,category));
					}
				}catch(Exception e){
					log.error("Error in parsing : ",e);
				}
				
				if(iseUnstableTestCaseIgnore.equalsIgnoreCase("NO")) {
					try{
						if(resultObj.get("UST") != null){
							JSONArray responseUSTArr = (JSONArray)resultObj.get("UST");
							log.info("UST:"+responseUSTArr.size());
							String category="UST";
							overallResultList.addAll(responseObjectFormatFromISE(responseUSTArr, iseTestPlanName,buildId,category));
						}
					}catch(Exception e){
						log.error("Error in parsing : ",e);
					}
				}
				
				if(iseEnvironmentSpecificTestCaseIgnore.equalsIgnoreCase("NO")) {
					try{
						if(resultObj.get("ET") != null){
							JSONArray responseETArr = (JSONArray)resultObj.get("ET");
							String category="ET";
							log.info("ET:"+responseETArr.size());
							overallResultList.addAll(responseObjectFormatFromISE(responseETArr, iseTestPlanName,buildId,category));
						}
					}catch(Exception e){
						log.error("Error in parsing : ",e);
					}
				}
				
				if(iseGoldenTestCaseIgnore.equalsIgnoreCase("NO")) {
					try{					
						if(resultObj.get("GT") != null){
							JSONArray responseGTArr = (JSONArray)resultObj.get("GT");
							log.info("GT:"+responseGTArr.size());
							String category="GT";
							overallResultList.addAll(responseObjectFormatFromISE(responseGTArr, iseTestPlanName,buildId,category));
						}
					} catch(Exception e){
						log.error("Error in parsing : ",e);
					}
				}
				
				
				try{
					if(resultObj.get("HFT") != null){
						JSONArray responseHFTArr = (JSONArray)resultObj.get("HFT");
						log.info("HFT"+responseHFTArr.size());
						String category="HFT";
						overallResultList.addAll(responseObjectFormatFromISE(responseHFTArr, iseTestPlanName,buildId,category));
					}
				}catch(Exception e){
					log.error("Error in parsing : ",e);
				}
				try{					
					if(resultObj.get("LFT") != null){
						JSONArray responseLFTArr = (JSONArray)resultObj.get("LFT");
						log.info("LFT"+responseLFTArr.size());
						String category="LFT";
						overallResultList.addAll(responseObjectFormatFromISE(responseLFTArr, iseTestPlanName,buildId,category));
					}
				}catch(Exception e){
					log.error("Error in parsing : ",e);
				}
			}			
			return overallResultList;
		}catch(Exception e){
			log.error("Error in Parsing JSON result from ISE response :",e);
			return null;
		}
	}
	
	private  List<ISERecommandedTestcases> responseObjectFormatFromISE(JSONArray responseArr, String testRunPlanId,Integer buildId,String category) {
		String title = null;
		String rag_status = null;
		String ST = null;
		String NT = null;
		String thresold_prob = null;
		String GT = null;
		String testcase = null;
		
		String ET = null;
		String CT = null;
		String BT = null;
		String feature = null;
		String probability = null;
		String picked = null;
		String HFT = null;
		String UST = null;
		String testbed = null;
		String LFT = null;
		String rag_value = null;
		List<ISERecommandedTestcases> resultList=new ArrayList<ISERecommandedTestcases>();
		try {			
			if(responseArr != null && responseArr.size() >0) {
				for (Object o : responseArr) {
					TestCaseList testCaseList = new TestCaseList();
					ProductBuild build= new ProductBuild();
					build.setProductBuildId(buildId);
						ISERecommandedTestcases testCaseRecommandedResult= new ISERecommandedTestcases();
						testCaseRecommandedResult.setBuild(build);
						testCaseRecommandedResult.setRecommendationCategory(category);
						JSONObject jsonObj = (JSONObject) o;
						if(jsonObj.get("title") != null){
							title = jsonObj.get("title").toString();
						if(jsonObj.get("rag_status") != null)
							rag_status = jsonObj.get("rag_status").toString();
						if(jsonObj.get("ST") != null)
							ST = jsonObj.get("ST").toString();
						if(jsonObj.get("NT") != null)
							NT = jsonObj.get("NT").toString();
						if(jsonObj.get("thresold_prob") != null)
							thresold_prob = jsonObj.get("thresold_prob").toString();
						if(jsonObj.get("GT") != null)
							GT = jsonObj.get("GT").toString();
						if(jsonObj.get("testcase") != null)
							testcase = jsonObj.get("testcase").toString();
						if(jsonObj.get("ET") != null)
							ET = jsonObj.get("ET").toString();
						if(jsonObj.get("CT") != null)
							CT = jsonObj.get("CT").toString();
						if(jsonObj.get("BT") != null)
							BT = jsonObj.get("BT").toString();
						if(jsonObj.get("feature") != null)
							feature = jsonObj.get("feature").toString();
						if(jsonObj.get("pobability") != null)
							probability = jsonObj.get("pobability").toString();
						if(jsonObj.get("picked") != null)
							picked = jsonObj.get("picked").toString();
						if(jsonObj.get("HFT") != null)
							HFT = jsonObj.get("HFT").toString();
						if(jsonObj.get("UST") != null)
							UST = jsonObj.get("UST").toString();
						if(jsonObj.get("testbed") != null)
							testbed = jsonObj.get("testbed").toString();
						if(jsonObj.get("LFT") != null)
							LFT = jsonObj.get("LFT").toString();
						if(jsonObj.get("rag_value") != null)
							rag_value = jsonObj.get("rag_value").toString();
						
						testCaseRecommandedResult.setTitle(title);
						testCaseRecommandedResult.setRag_status(rag_status);
						testCaseRecommandedResult.setST(ST);
						testCaseRecommandedResult.setNT(NT);
						testCaseRecommandedResult.setThresold_prob(thresold_prob);
						testCaseRecommandedResult.setGT(GT);
						testCaseRecommandedResult.setET(ET);
						testCaseRecommandedResult.setCT(CT);
						testCaseRecommandedResult.setBT(BT);
						testCaseRecommandedResult.setFeature(feature);
						testCaseRecommandedResult.setProbability(probability);
						testCaseRecommandedResult.setPicked(picked);
						testCaseRecommandedResult.setHFT(HFT);
						testCaseRecommandedResult.setUST(UST);
						testCaseRecommandedResult.setTestbed(testbed);
						testCaseRecommandedResult.setLFT(LFT);
						testCaseRecommandedResult.setRag_value(rag_value);
						testCaseRecommandedResult.setTestRunPlanId(testRunPlanId);
						resultList.add(testCaseRecommandedResult);						
					}
				}
			}
		}catch(Exception e) {
			log.error("Error in parsing JSON Response : ",e);
		}
		return resultList;
	}
	
	@Override
	@Transactional
	public void mapWorkpackageWithTestCase(Integer workPackageId,
			Integer testcaseId, String action) {
	
		workPackageDAO.mapWorkpackageWithTestCase(workPackageId, testcaseId,
				action);
	}

	@Override
	@Transactional
	public RunConfiguration getWorkpackageRCByEnvDev(Integer workpackageId,
			Integer environmentCombinationId, Integer deviceId) {
	
		return workPackageDAO.getWorkpackageRCByEnvDev(workpackageId,
				environmentCombinationId, deviceId);
	}

	@Override
	@Transactional
	public List<WorkPackage> listWorkPackageBytestrunplanId(
			Integer testRunPlanId) {
	
		return workPackageDAO.listWorkPackageBytestrunplanId(testRunPlanId);
	}

	@Override
	@Transactional
	public void deleteWorkPackageTestcaseExecutionPlan(
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList) {
	
		workPackageDAO
				.deleteWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanList);
	}

	@Override
	@Transactional
	public int getTotalRecordWPTCEP(Integer workPackageId, int status) {
	
		return workPackageDAO.getTotalRecordWPTCEP(workPackageId, status);
	}

	@Override
	@Transactional
	public int getTotalRecordWPTCEPCount(Integer workPackageId, int status) {
		return workPackageDAO.getTotalRecordWPTCEPCount(workPackageId, status);
	}

	@Override
	@Transactional
	public void saveTestStepExecutionResult(
			List<TestStepExecutionResult> testStepExecutionResultList) {
	
		workPackageDAO.saveTestStepExecutionResult(testStepExecutionResultList);
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan workPackageTestCasesExecutionPlanByJobId(
			TestRunJob testRunJob, TestCaseList testCaseList) {
	
		return workPackageDAO.workPackageTestCasesExecutionPlanByJobId(
				testRunJob, testCaseList);
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan workPackageTestCasesExecutionPlanByJobId(
			TestRunJob combinedeRportingTestRunJob, TestRunJob executingTestRunJob, TestCaseList testCaseList) {
	
		return workPackageDAO.workPackageTestCasesExecutionPlanByJobId(
				combinedeRportingTestRunJob, executingTestRunJob, testCaseList);
	}

	@Override
	@Transactional
	public void mapWorkpackageWithTestSuite(Integer workPackageId,
			Integer testSuiteId, String type) {
	
		workPackageDAO.mapWorkpackageWithTestSuite(workPackageId, testSuiteId,
				type);
	}

	@Override
	@Transactional
	public int addNewWorkPackageTestSuite(
			List<WorkPackageTestSuite> workPackageTestSuites) {
	
		return workPackageDAO.addNewWorkPackageTestSuite(workPackageTestSuites);
	}

	@Override
	@Transactional
	public WorkPackageTestSuite workPackageTestCasesByJobId(
			TestRunJob testRunJob) {
	
		return workPackageDAO.workPackageTestCasesByJobId(testRunJob);
	}

	@Override
	@Transactional
	public void addTestRunJob(RunConfiguration runConfiguration,
			TestSuiteList testSuiteList, WorkPackage workPackage,
			TestRunPlan testRunPlan) {
	
		workPackageDAO.addTestRunJob(runConfiguration, testSuiteList,
				workPackage, testRunPlan);
	}

	@Override
	@Transactional
	public TestRunJob getTestRunJobByWP(WorkPackage workPackage,
			RunConfiguration runConfiguration) {
	
		return workPackageDAO.getTestRunJobByWP(workPackage, runConfiguration);
	}

	@Override
	@Transactional
	public List<TestRunJob> getTestRunJobByBuildID(Integer productBuildId,
			Integer workPackageType, Integer jtStartIndex, Integer jtPageSize) {
	
		return workPackageDAO.getTestRunJobByBuildID(productBuildId,
				workPackageType, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public void mapTestRunJobTestCase(Integer testRunJobId, Integer testcaseId,
			String type) {
	
		workPackageDAO.mapTestRunJobTestCase(testRunJobId, testcaseId, type);
	}

	public List<TestCaseDTO> getWorkPackageResultSummary(Integer workPackageId) {

		Integer totalPass = 0;
		Integer totalFail = 0;
		Integer totalNoRun = 0;
		Integer totalBlock = 0;
		Integer totalExecutedTesCases = 0;
		Integer notExecuted = 0;
		Integer totalExecutionPlanTesCases = 0;
		boolean resFlag = true;
		String wpResult = "FAILED";
		String wpStatus;
		List<TestCaseDTO> testCaseDTOList = new ArrayList<TestCaseDTO>(0);

		List<Object[]> listObjFromDb = workPackageDAO
				.listWorkPackageTestCaseExeSummaryByFilters(workPackageId, 0,
						null);
		if (listObjFromDb.size() != 0) {
			for (Object obj[] : listObjFromDb) {
				Integer totalResult = (Integer) 0;
				BigInteger bi1 = (BigInteger) obj[0];
				String resultId = (String) obj[1];
				totalResult = bi1.intValue();
				if (resultId == null || resultId.equals("")) {
					notExecuted = totalResult;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_PASSED)) {
					totalPass = totalResult;
					if (resFlag) {
						wpResult = "PASSED";
					}
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_FAILED)) {
					totalFail = totalResult;
					wpResult = "FAILED";
					resFlag = false;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_NORUN)) {
					totalNoRun = totalResult;
					wpResult = "FAILED";
					resFlag = false;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_BLOCKED)) {
					totalBlock = totalResult;
					wpResult = "FAILED";
					resFlag = false;

				}
			}
		}
		List<Object[]> wpSummaryArr = workPackageDAO
				.listWorkPackageTestCaseExeSummaryByFilters(workPackageId, 0,
						"wpSummary");

		TestCaseDTO testCaseDTO = new TestCaseDTO();
		testCaseDTO.setWorkPackageId(workPackageId);
		for (Object obj[] : wpSummaryArr) {

			testCaseDTO.setWpName((String) obj[0]);

			testCaseDTO.setTotalWPTestCase((Integer) obj[2]);
			testCaseDTO.setTotalWptestSuite((Integer) obj[3]);
			testCaseDTO.setTotalWpFeature((Integer) obj[4]);
			testCaseDTO.setEnvironmentCount((Long) obj[5]);
			testCaseDTO.setDefectsCount((Long) obj[6]);
			wpStatus = convertWorkPackageStatusFromIntToString((Integer) obj[1]);
			testCaseDTO.setWpStatus(wpStatus);
			if ((Integer) obj[1] == IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED
					|| (Integer) obj[1] == IDPAConstants.WORKFLOW_STAGE_ID_CLOSED) {
				testCaseDTO.setWpResult(wpResult);

			} else {
				testCaseDTO.setWpResult("-");
			}
		}

		testCaseDTO.setTotalPass(totalPass);
		testCaseDTO.setTotalFail(totalFail);
		testCaseDTO.setTotalNoRun(totalNoRun);
		testCaseDTO.setTotalBlock(totalBlock);
		testCaseDTO.setNotExecuted(notExecuted);

		totalExecutedTesCases = totalPass + totalFail + totalNoRun + totalBlock;
		testCaseDTO.setTotalExecutedTesCases(totalExecutedTesCases);
		totalExecutionPlanTesCases = notExecuted + totalExecutedTesCases;
		testCaseDTO.setTotalExecutionPlanTestCase(totalExecutionPlanTesCases);

		testCaseDTOList.add(testCaseDTO);
		testCaseDTO = null;
		return testCaseDTOList;
	}

	@Override
	@Transactional
	public void mapTestRunJobTestSuite(Integer testRunJobId,
			Integer testsuiteId, String type) {
	
		workPackageDAO.mapTestRunJobTestSuite(testRunJobId, testsuiteId, type);
	}

	public List<TestCaseDTO> getworkPackageTestRunJobSummary(
			Integer workPackageId) {

		Map<Integer, TestRunJob> testRunJobMap = new HashMap<Integer, TestRunJob>();
		List<TestCaseDTO> tcDtoList = new ArrayList<TestCaseDTO>();
		int tabId = 1;
		TestRunJob testRunJobObj = null;
		TestCaseDTO testCaseDto = new TestCaseDTO();
		int totalNotExecutedTC = 0;
		int notExecutedTC = 0;
		int index = -1;
		int totalExecutedTCs = 0;
		int failCount = 0;
		int noRunCount = 0;
		int blockedCount = 0;
		String jobStatus;
		Integer totalExecutionPlanTestCase = 0;
		String comments;
		Integer jobStatusInt = 0;

		boolean flag = false;
		List<Object[]> listObjFromDb = workPackageDAO
				.listWorkPackageTestCaseExeSummaryByFilters(workPackageId,
						tabId, null);
		if (listObjFromDb.size() != 0) {
			for (Object obj[] : listObjFromDb) {
				testRunJobObj = new TestRunJob();
				Integer testRunJobId = (Integer) obj[0];
				String envCombName = (String) obj[1];
				String resultId = (String) obj[2];
				BigInteger bi1 = (BigInteger) obj[3];
				Integer totalResult = bi1.intValue();
				comments = (String) obj[5];
				jobStatusInt = (Integer) obj[6];
				jobStatus = convertJobStatusFromIntToString(jobStatusInt);

				testRunJobObj.setTestRunJobId(testRunJobId);

				if (!testRunJobMap.containsKey(testRunJobObj)) {
					++index;

					flag = true;
					totalNotExecutedTC = 0;
					notExecutedTC = 0;
					totalExecutedTCs = 0;
					totalExecutionPlanTestCase = 0;

					testCaseDto = new TestCaseDTO();

					testCaseDto.setTestRunJobId(testRunJobId);
					testCaseDto.setEnvCombName(envCombName);
					testCaseDto.setNotExecuted(totalNotExecutedTC);
					testCaseDto
							.setTotalExecutionPlanTestCase(totalExecutionPlanTestCase);
					testCaseDto.setTotalBlock(0);
					testCaseDto.setTotalPass(0);
					testCaseDto.setTotalFail(0);
					testCaseDto.setTotalNoRun(0);
					if (jobStatusInt == 5) {
						testCaseDto.setJobResult("FAILED"); // // if there is no
															// test steps and no
															// test cases,this
															// message will
															// display
					} else {
						testCaseDto.setJobResult("-");
						flag = false;
					}

					testCaseDto.setJobFailureMessage(comments);
					testCaseDto.setJobStatus(jobStatus);
					tcDtoList.add(index, testCaseDto);
					testRunJobMap.put(testRunJobId, testRunJobObj);
				}
				if (resultId == null || resultId.equals("")) {
					notExecutedTC = totalResult;
					totalResult = 0;
					testCaseDto.setNotExecuted(notExecutedTC);
					totalExecutionPlanTestCase = totalExecutionPlanTestCase
							+ notExecutedTC;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_PASSED)) {
					testCaseDto.setTotalPass(totalResult);
					if (flag) {
						testCaseDto.setJobResult("PASSED");
					}

				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_FAILED)) {
					testCaseDto.setTotalFail(totalResult);

					if (flag) {
						testCaseDto.setJobResult("FAILED");
						flag = false;
					}

				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_NORUN)) {
					testCaseDto.setTotalNoRun(totalResult);
					if (flag) {
						testCaseDto.setJobResult("FAILED");
						flag = false;
					}

				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_BLOCKED)) {
					testCaseDto.setTotalBlock(totalResult);
					if (flag) {
						testCaseDto.setJobResult("FAILED");
						flag = false;
					}

				}
				totalExecutedTCs = totalExecutedTCs + totalResult;
				totalExecutionPlanTestCase = totalExecutionPlanTestCase
						+ totalResult;
				testCaseDto
						.setTotalExecutionPlanTestCase(totalExecutionPlanTestCase);
				testCaseDto.setTotalExecutedTesCases(totalExecutedTCs);
				tcDtoList.set(index, testCaseDto);

			}
		}
		return tcDtoList;
	}

	@Override
	@Transactional
	public List<WorkPackage> listActiveWorkPackages(Integer productId) {
	
		return workPackageDAO.listActiveWorkPackages(productId);
	}

	@Override
	@Transactional
	public JTableResponse listWorkpackageFeature(Integer workPackageId,
			Integer jtStartIndex, Integer jtPageSize) {
	

		JTableResponse jTableResponse = null;
		int pageSize = 0;
		WorkPackage workPackage = null;
		int productId = workPackageDAO
				.getProductIdByWorkpackageId(workPackageId);

		
		Integer workPackageFeaturesTotalCount = 0;
		Integer productFeaturesTotalCount = 0;
		
		List<ProductFeature> allFeatureLists = new ArrayList<ProductFeature>();
		productFeaturesTotalCount = productFeatureDAO.getProductFeatureCount(
				productId, -1, -1);
		List<WorkPackageFeature> workPackageFeaturesTotal = new ArrayList<WorkPackageFeature>();
		workPackageFeaturesTotalCount = workPackageDAO
				.getWorkPackageFeatureCount(workPackageId, -1, -1);
		List<WorkPackageFeature> workPackageFeatures = new ArrayList<WorkPackageFeature>();

		List<JsonWorkPackageFeature> jsonWorkPackageFeatures = new ArrayList<JsonWorkPackageFeature>();
		JsonWorkPackageFeature jsonWorkPackageFeature = null;
		if (workPackageFeaturesTotalCount == 0) {
			// TODO : Initialize the workpackage with the test cases for the
			// product
			log.info("There are no testsuite for this WPTC. Initializing");
			allFeatureLists = productFeatureDAO.getFeatureListByProductId(
					productId, null, null, null);
			workPackage = workPackageDAO.getWorkPackageById(workPackageId);
			int count = initializeWorkPackageWithFeature(workPackage,
					allFeatureLists);
			pageSize = workPackageDAO.getWorkPackageFeatureCount(workPackageId,
					-1, -1);
			jsonWorkPackageFeatures = workPackageDAO
					.listJsonWorkPackageFeature(workPackageId, jtStartIndex,
							jtPageSize);
			jTableResponse = new JTableResponse("OK", jsonWorkPackageFeatures,
					pageSize);
			workPackageFeatures = null;

		}
		else if (workPackageFeaturesTotalCount < productFeaturesTotalCount) {
			// Seed the remaining test cases
			List<ProductFeature> newFeatures = new ArrayList<ProductFeature>();
			List<ProductFeature> featureListForWorkpackage = new ArrayList<ProductFeature>();
			workPackageFeaturesTotal = workPackageDAO.listWorkPackageFeature(
					workPackageId, 0, 0);
			for (WorkPackageFeature workPackageFeature : workPackageFeaturesTotal) {
				featureListForWorkpackage.add(workPackageFeature.getFeature());
			}
			allFeatureLists = productFeatureDAO.getFeatureListByProductId(
					productId, null, null, null);
			for (ProductFeature feature : allFeatureLists) {
				if (!featureListForWorkpackage.contains(feature)) {
					newFeatures.add(feature);
				}
			}
			int count = workPackageDAO.seedWorkPackageWithNewFeature(
					newFeatures, workPackageId);
			pageSize = workPackageDAO.getWorkPackageFeatureCount(workPackageId,
					-1, -1);
			jsonWorkPackageFeatures = workPackageDAO
					.listJsonWorkPackageFeature(workPackageId, jtStartIndex,
							jtPageSize);

			jTableResponse = new JTableResponse("OK", jsonWorkPackageFeatures,
					pageSize);
			workPackageFeatures = null;
		} else {
			log.info("workPackageTestSuitesTotal count>>>>"
					+ workPackageFeaturesTotalCount);
			
			pageSize = workPackageDAO.getWorkPackageFeatureCount(workPackageId,
					-1, -1);
			jsonWorkPackageFeatures = workPackageDAO
					.listJsonWorkPackageFeature(workPackageId, jtStartIndex,
							jtPageSize);
			jTableResponse = new JTableResponse("OK", jsonWorkPackageFeatures,
					pageSize);
			workPackageFeatures = null;
		}
		return jTableResponse;

	}

	@Override
	@Transactional
	public WorkPackageFeature updateWorkPackageFeature(
			WorkPackageFeature workPackageFeature,
			JsonWorkPackageFeature jsonWorkPackageFeature) {
	
	
		log.info("updade method" + workPackageFeature.getWorkpackageFeatureId());

		WorkPackageFeature workPackageFeatureFromDB = workPackageDAO
				.getWorkPackageFeatureById(workPackageFeature
						.getWorkpackageFeatureId());

		workPackageFeature.setCreatedDate(workPackageFeatureFromDB
				.getCreatedDate());
		WorkPackage workPackage = null;
		WorkPackageFeature updatedWorkPackageFeature = null;
		List<WorkPackageTestCaseExecutionPlan> currentWorkPackageTestCaseExecutionPlanList = null;
		List<WorkPackageTestCaseExecutionPlan> finalWorkPackageTestCaseExecutionPlanList = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		workPackage = workPackageDAO.getWorkPackageById(workPackageFeature
				.getWorkPackage().getWorkPackageId());
		ProductFeature feature = workPackageFeature.getFeature();
		Set<TestCaseList> testCaseListSet = null;
		Set<TestCaseList> childTestCaseListSet = null;
		testCaseListSet = workPackageFeature.getFeature().getTestCaseList();

		if (workPackageFeatureFromDB.getIsSelected() == workPackageFeature
				.getIsSelected()) {
			log.info("No change in Test case selection status. Check for env value selections change");
			updatedWorkPackageFeature = workPackageFeature;
		} else {

			log.info("Updated Test case selection status : "
					+ workPackageFeature.getIsSelected());
			// There is a change in the is selected value. Update the test
			// package first.
			workPackageFeature.setWorkPackage(workPackage);
			workPackageFeature.setFeature(feature);
			workPackageFeatureFromDB.setIsSelected(workPackageFeature
					.getIsSelected());
			updatedWorkPackageFeature = workPackageDAO
					.updateWorkPackageFeature(workPackageFeatureFromDB);
			if (workPackageFeature.getIsSelected() == 1) {				
				if(workPackageFeature.getWorkPackage().getWorkFlowEvent() != null  && workPackageFeature.getWorkPackage().getWorkFlowEvent().getWorkfloweventId() != null){
					if(workPackageFeature.getWorkPackage().getWorkFlowEvent().getWorkFlow() != null){
						if(workPackageFeature.getWorkPackage().getWorkFlowEvent()
								.getWorkFlow().getStageId() != IDPAConstants.WORKFLOW_STAGE_ID_PLANNING
								&& workPackageFeature.getWorkPackage()
										.getWorkFlowEvent().getWorkFlow()
										.getStageValue() < 20) {
							WorkFlow workFlow = workPackageDAO
									.getWorkFlowByEntityIdStageId(
											IDPAConstants.WORKPACKAGE_ENTITY_ID,
											IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
							WorkFlowEvent workFlowEvent = new WorkFlowEvent();
							workFlowEvent.setEventDate(DateUtility.getCurrentTime());
							workFlowEvent.setRemarks("Planning Workapckage :"
									+ workPackage.getName());
							UserList user = userListDAO
									.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
							workFlowEvent.setUser(user);
							workFlowEvent.setWorkFlow(workFlow);
							workPackage.setWorkFlowEvent(workFlowEvent);
							workPackageDAO.addWorkFlowEvent(workFlowEvent);
							workPackageDAO.updateWorkPackage(workPackage);
						}	
					}
				}
				workPackageDAO.mapWorkpackageWithFeature(workPackageFeature
						.getWorkPackage().getWorkPackageId(), feature
						.getProductFeatureId(), "Add");

				List<WorkpackageRunConfiguration> runConfigurations = workPackageDAO
						.getWorkpackageRunConfigurationList(workPackageFeature
								.getWorkPackage().getWorkPackageId(), null,
								"testsuite");
				WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan = null;
				if (runConfigurations != null && !runConfigurations.isEmpty()) {
					for (WorkpackageRunConfiguration rc : runConfigurations) {
						TestRunJob testRunJob = getTestRunJobByWP(workPackage,
								rc.getRunconfiguration());
						if (testRunJob != null) {
							mapTestRunJobFeature(testRunJob.getTestRunJobId(),
									feature.getProductFeatureId(), "Add");
						} else {
							addTestRunJob(rc.getRunconfiguration(), null,
									workPackage, null);
							testRunJob = getTestRunJobByWP(workPackage,
									rc.getRunconfiguration());
							mapTestRunJobFeature(testRunJob.getTestRunJobId(),
									feature.getProductFeatureId(), "Add");
							if (testRunJob != null) {
								if (testRunJob.getTestRunJobId() != null)
									mongoDBService
											.addTestRunJobToMongoDB(testRunJob
													.getTestRunJobId());
							}

						}

						workPackageFeatureExecutionPlan = new WorkPackageFeatureExecutionPlan();
						workPackageFeatureExecutionPlan
								.setCreatedDate(DateUtility.getCurrentTime());
						workPackageFeatureExecutionPlan.setFeature(feature);
						workPackageFeatureExecutionPlan
								.setModifiedDate(DateUtility.getCurrentTime());
						workPackageFeatureExecutionPlan
								.setPlannedExecutionDate(workPackage
										.getPlannedStartDate());
						workPackageFeatureExecutionPlan.setRunConfiguration(rc);
						workPackageFeatureExecutionPlan.setStatus(1);
						workPackageFeatureExecutionPlan
								.setWorkPackage(workPackage);
						workPackageFeatureExecutionPlan
								.setTestRunJob(testRunJob);
						ExecutionPriority executionPriority = null;
						if (workPackageFeature.getFeature()
								.getExecutionPriority() != null)
							executionPriority = workPackageDAO
									.getExecutionPriorityByName(CommonUtility
											.getExecutionPriority(workPackageFeature
													.getFeature()
													.getExecutionPriority()
													.getPriorityName()));
						else
							executionPriority = workPackageDAO
									.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
						workPackageFeatureExecutionPlan
								.setExecutionPriority(executionPriority);

						workPackageDAO
								.addWorkpackageFeatureExecutionPlan(workPackageFeatureExecutionPlan);
					}
				}
				for (TestCaseList testCaseList : testCaseListSet) {
					if (testCaseList != null) {
						testCaseList = testCaseListDAO
								.getByTestCaseId(testCaseList.getTestCaseId());
						Set<EnvironmentCombination> environmentCombinations = workPackage
								.getEnvironmentCombinationList();

						List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate = new ArrayList<WorkPackageTestCaseExecutionPlan>();
						if (runConfigurations != null
								&& !runConfigurations.isEmpty()) {
							for (WorkpackageRunConfiguration rc : runConfigurations) {
								TestRunJob testRunJob = getTestRunJobByWP(
										workPackage, rc.getRunconfiguration());
								WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
								workPackageTestCaseExecutionPlan
										.setTestCase(testCaseList);
								workPackageTestCaseExecutionPlan
										.setFeature(feature);
								workPackageTestCaseExecutionPlan
										.setWorkPackage(workPackage);
								workPackageTestCaseExecutionPlan
										.setRunConfiguration(rc);
								workPackageTestCaseExecutionPlan
										.setExecutionStatus(3);
								workPackageTestCaseExecutionPlan
										.setIsExecuted(0);
								workPackageTestCaseExecutionPlan
										.setSourceType("Feature");
								workPackageTestCaseExecutionPlan
										.setPlannedExecutionDate(workPackage
												.getPlannedStartDate());
								workPackageTestCaseExecutionPlan.setStatus(1);
								workPackageTestCaseExecutionPlan
										.setTestRunJob(testRunJob);
								ExecutionPriority executionPriority = null;
								executionPriority = workPackageDAO
										.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
								workPackageTestCaseExecutionPlan
										.setExecutionPriority(executionPriority);
								TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
								testCaseExecutionResult.setResult("");
								testCaseExecutionResult.setComments("");
								testCaseExecutionResult.setDefectsCount(0);
								testCaseExecutionResult.setDefectIds("");
								testCaseExecutionResult.setIsApproved(0);
								testCaseExecutionResult.setIsReviewed(0);
								testCaseExecutionResult.setObservedOutput("");

								// Commented the following statements when
								// remove the WorkPackageTestcaseExecutionId
								// column from Test Case Execution Result table.
								// By: Logeswari, On : 11-Feb-2015
								testCaseExecutionResult
										.setWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
								workPackageTestCaseExecutionPlan
										.setTestCaseExecutionResult(testCaseExecutionResult);
								workPackageTestCaseExecutionPlanListForUpdate
										.add(workPackageTestCaseExecutionPlan);

							}
							addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
						}
					}
				}
			}
			// If the testcase was previously selected and now not selected,
			// then clear all workpackagetestcaseexecutionplan rows
			// as these not be there in the execution plan
			if (workPackageFeature.getIsSelected() == 0) {
				try {
					log.info("Clearing all test cases from plan as test case has been de-selected : "
							+ workPackageFeature.getIsSelected());

					List<WorkpackageRunConfiguration> runConfigurations = workPackageDAO
							.getWorkpackageRunConfigurationList(
									workPackageFeature.getWorkPackage()
											.getWorkPackageId(), null,
									"testsuite");
					if (runConfigurations != null
							&& !runConfigurations.isEmpty()) {
						for (WorkpackageRunConfiguration rc : runConfigurations) {
							TestRunJob testRunJob = workPackageDAO
									.getTestRunJobByWP(workPackage,
											rc.getRunconfiguration());
							workPackageDAO.mapTestRunJobFeature(
									testRunJob.getTestRunJobId(),
									feature.getProductFeatureId(), "Remove");
							if (testRunJob.getRunConfiguration()
									.getRunconfigId() == rc
									.getRunconfiguration().getRunconfigId()) {

								if (testRunJob.getTestSuiteSet().isEmpty()
										&& testRunJob.getTestCaseListSet()
												.isEmpty()
										&& testRunJob.getFeatureSet().isEmpty()) {
									environmentDAO.deleteTestRunJob(workPackage
											.getWorkPackageId(), rc
											.getRunconfiguration()
											.getRunconfigId(), null);
								}
							}
						}

					}

					workPackageDAO.mapWorkpackageWithFeature(workPackage
							.getWorkPackageId(), workPackageFeature
							.getFeature().getProductFeatureId(), "Remove");

					WorkPackageFeatureExecutionPlan wpfep = workPackageDAO
							.getWorkpackageFeatureExecutionPlan(workPackage
									.getWorkPackageId(), workPackageFeature
									.getFeature().getProductFeatureId(), 0);

					if (wpfep != null)
						workPackageDAO
								.deleteWorkpackageFeatureExecutionPlan(wpfep);

					for (TestCaseList testCaseList : testCaseListSet) {
						currentWorkPackageTestCaseExecutionPlanList = workPackageDAO
								.getWorkPackageTestcaseExecutionPlanByWorkPackageId(
										workPackage.getWorkPackageId(),
										testCaseList.getTestCaseId(), -1,
										workPackageFeature.getFeature()
												.getProductFeatureId(),
										"Feature");
						// deleteworkpackage feature

						workPackageDAO
								.deleteWorkPackageTestcaseExecutionPlan(currentWorkPackageTestCaseExecutionPlanList);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return updatedWorkPackageFeature;
			} else {

				log.info("Since the test case is now selected, continue to other options evaluation");
			}
		}

		// The test case is selected. Process the environments selection
		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = null;
		currentWorkPackageTestCaseExecutionPlanList = workPackageDAO
				.getWorkPackageTestcaseExecutionPlanByWorkPackageId(workPackage
						.getWorkPackageId(), workPackageFeature.getFeature()
						.getTestCaseList());
		int productId = workPackage.getProductBuild().getProductVersion()
				.getProductMaster().getProductId();
		List<Environment> environmentList = workPackageDAO
				.getEnvironmentListByProductId(productId);

		log.info("Evaluation individual env selection options");

		return updatedWorkPackageFeature;
	}

	@Override
	@Transactional
	public void addWorkpackageFeatureExecutionPlan(
			WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan) {
	
		workPackageDAO
				.addWorkpackageFeatureExecutionPlan(workPackageFeatureExecutionPlan);
	}

	@Override
	@Transactional
	public void mapTestRunJobFeature(Integer testRunJobId, Integer featureId,
			String type) {
	
		workPackageDAO.mapTestRunJobFeature(testRunJobId, featureId, type);
	}

	@Override
	@Transactional
	public List<WorkPackageFeatureExecutionPlan> listWorkPackageFeatureExecutionPlan(
			Map<String, String> searchString, Integer workPackageId,
			Integer jtStartIndex, Integer jtPageSize, int status) {
	
		return workPackageDAO.listWorkPackageFeatureExecutionPlan(searchString,
				workPackageId, jtStartIndex, jtPageSize, status);
	}

	@Override
	@Transactional
	public List<JsonWorkPackageFeatureExecutionPlan> listJsonWorkPackageFeatureExecutionPlan(
			Map<String, String> searchString, Integer workPackageId,
			Integer jtStartIndex, Integer jtPageSize, int status) {
		return workPackageDAO.listJsonWorkPackageFeatureExecutionPlan(
				searchString, workPackageId, jtStartIndex, jtPageSize, status);
	}

	@Override
	@Transactional
	public void updateWorkPackageFeatureExecutionPlan(
			WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlanFromUI) {
	
		workPackageDAO
				.updateWorkPackageFeatureExecutionPlan(workPackageFeatureExecutionPlanFromUI);
	}

	@Override
	@Transactional
	public WorkPackageFeatureExecutionPlan getWorkpackageFeatureExecutionPlanById(
			Integer rowId) {
	
		return workPackageDAO.getWorkpackageFeatureExecutionPlanById(rowId);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestcaseExecutionPlanByWorkPackageId(
			int workPackageId, int testcaseId, int testSuiteId, int featureId,
			String type) {
	
		return workPackageDAO
				.getWorkPackageTestcaseExecutionPlanByWorkPackageId(
						workPackageId, testcaseId, testSuiteId, featureId, type);
	}

	@Override
	@Transactional
	public void updateWorkPackageFeatureExecutionPlan(String[] wptcepLists,
			Integer testerId, Integer testLeadId, String plannedExecutionDate,
			Integer executionPriorityId, Integer shiftId) {
	
		WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan = null;
		ExecutionPriority executionPriority = null;

		try {
			for (String Id : wptcepLists) {
				log.info("id=" + Id);
				workPackageFeatureExecutionPlan = workPackageDAO
						.getWorkpackageFeatureExecutionPlanById(Integer
								.parseInt(Id));

				if (testerId != null && testerId != -1) {
					UserList tester = userListDAO.getByUserId(testerId);
					workPackageFeatureExecutionPlan.setTester(tester);
				}
				if (testLeadId != null && testLeadId != -1) {
					UserList testLead = userListDAO.getByUserId(testLeadId);
					workPackageFeatureExecutionPlan.setTestLead(testLead);
				}
				if (plannedExecutionDate != null
						&& !plannedExecutionDate.equals("")) {
					workPackageFeatureExecutionPlan
							.setPlannedExecutionDate(DateUtility
									.dateformatWithOutTime(plannedExecutionDate));
				}

				if (executionPriorityId != -1) {
					executionPriority = workPackageDAO
							.getExecutionPriorityById(executionPriorityId);
					workPackageFeatureExecutionPlan
							.setExecutionPriority(executionPriority);
				}

				if (shiftId != -1) {
					WorkShiftMaster workShiftMaster = workPackageDAO
							.getWorkShiftById(shiftId);
					workPackageFeatureExecutionPlan
							.setPlannedWorkShiftMaster(workShiftMaster);
				}

				if (workPackageFeatureExecutionPlan.getExecutionPriority() == null) {
					if (workPackageFeatureExecutionPlan.getFeature()
							.getExecutionPriority() != null)
						executionPriority = workPackageDAO
								.getExecutionPriorityByName(CommonUtility
										.getExecutionPriority(workPackageFeatureExecutionPlan
												.getFeature()
												.getExecutionPriority()
												.getPriorityName()));
					else
						executionPriority = workPackageDAO
								.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);

					workPackageFeatureExecutionPlan
							.setExecutionPriority(executionPriority);

				}
				workPackageDAO
						.updateWorkPackageFeatureExecutionPlan(workPackageFeatureExecutionPlan);

				List<WorkPackageTestCaseExecutionPlan> currentWorkPackageTestCaseExecutionPlanList = null;
				currentWorkPackageTestCaseExecutionPlanList = workPackageDAO
						.getWorkPackageTestcaseExecutionPlanByWorkPackageId(
								workPackageFeatureExecutionPlan
										.getWorkPackage().getWorkPackageId(),
								-1, -1, workPackageFeatureExecutionPlan
										.getFeature().getProductFeatureId(),
								"Feature", workPackageFeatureExecutionPlan
										.getRunConfiguration()
										.getRunconfiguration().getRunconfigId());
				for (WorkPackageTestCaseExecutionPlan wptcep : currentWorkPackageTestCaseExecutionPlanList) {
					wptcep.setPlannedExecutionDate(DateUtility
							.dateformatWithOutTime(plannedExecutionDate));
					wptcep.setPlannedWorkShiftMaster(workPackageDAO
							.getWorkShiftByName(workPackageFeatureExecutionPlan
									.getPlannedWorkShiftMaster().getShiftName()));
					if (workPackageFeatureExecutionPlan.getTestLead() != null)
						wptcep.setTestLead(workPackageFeatureExecutionPlan
								.getTestLead());
					if (workPackageFeatureExecutionPlan.getTester() != null)
						wptcep.setTester(workPackageFeatureExecutionPlan
								.getTester());
					if (wptcep.getTester() != null
							&& wptcep.getTestLead() != null) {
						wptcep.setExecutionStatus(1);
					}
					wptcep.setExecutionPriority(executionPriority);
					workPackageDAO
							.updateWorkPackageTestCaseExecutionPlan(wptcep);
				}
			}

		} catch (Exception e) {
			log.info("Problem in adding Plan details records", e);
			return;
		}
	}

	@Override
	@Transactional
	public void addWorkpackageTestSuiteExecutionPlan(
			WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan) {
	
		workPackageDAO
				.addWorkpackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlan);
	}

	@Override
	@Transactional
	public WorkPackageTestSuiteExecutionPlan getWorkpackageTestSuiteExecutionPlan(
			Integer workPackageId, Integer testSuiteId,
			Integer runConfigurationId) {
	
		return workPackageDAO.getWorkpackageTestSuiteExecutionPlan(
				workPackageId, testSuiteId, runConfigurationId);
	}

	@Override
	@Transactional
	public void deleteWorkpackageTestSuiteExecutionPlan(
			WorkPackageTestSuiteExecutionPlan wptsp) {
	
		workPackageDAO.deleteWorkpackageTestSuiteExecutionPlan(wptsp);
	}

	@Override
	@Transactional
	public int getTotalRecordWPFEP(Integer workpackageId, int status) {
	
		return workPackageDAO.getTotalRecordWPFEP(workpackageId, status);
	}

	@Override
	@Transactional
	public int getTotalRecordWPFEPCount(Integer workpackageId, int status) {
		return workPackageDAO.getTotalRecordWPFEPCount(workpackageId, status);
	}

	@Override
	@Transactional
	public int getTestFactoryIdOfWorkPackage(Integer workpackageId) {
		return workPackageDAO.getTestFactoryIdOfWorkPackage(workpackageId);
	}

	@Override
	@Transactional
	public int getTotalRecordWPTSEP(Integer workpackageId, int status) {
	
		return workPackageDAO.getTotalRecordWPTSEP(workpackageId, status);
	}

	@Override
	@Transactional
	public int getTotalRecordWPTSEPCount(Integer workpackageId, int status) {
		return workPackageDAO.getTotalRecordWPTSEPCount(workpackageId, status);
	}

	@Override
	@Transactional
	public List<WorkPackageTestSuiteExecutionPlan> listWorkPackageTestSuiteExecutionPlan(
			Map<String, String> searchString, Integer workpackageId,
			Integer jtStartIndex, Integer jtPageSize, int status) {
	
		return workPackageDAO.listWorkPackageTestSuiteExecutionPlan(
				searchString, workpackageId, jtStartIndex, jtPageSize, status);
	}

	@Override
	@Transactional
	public List<JsonWorkPackageTestSuiteExecutionPlan> listJsonWorkPackageTestSuiteExecutionPlan(
			Map<String, String> searchString, Integer workPackageId,
			Integer jtStartIndex, Integer jtPageSize, int status) {
		return workPackageDAO.listJsonWorkPackageTestSuiteExecutionPlan(
				searchString, workPackageId, jtStartIndex, jtPageSize, status);
	}

	@Override
	@Transactional
	public void updateWorkPackageTestSuiteExecutionPlan(
			WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlanFromUI) {
	
		workPackageDAO
				.updateWorkPackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlanFromUI);
	}

	@Override
	@Transactional
	public WorkPackageTestSuiteExecutionPlan getWorkpackageTestSuiteExecutionPlanById(
			Integer rowId) {
	
		return workPackageDAO.getWorkpackageTestSuiteExecutionPlanById(rowId);
	}

	@Override
	@Transactional
	public void updateWorkPackageTestSuiteExecutionPlan(String[] wptcepLists,
			Integer testerId, Integer testLeadId, String plannedExecutionDate,
			Integer executionPriorityId, Integer shiftId) {
	
		WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan = null;
		ExecutionPriority executionPriority = null;

		try {
			for (String Id : wptcepLists) {
				log.info("id=" + Id);
				workPackageTestSuiteExecutionPlan = workPackageDAO
						.getWorkpackageTestSuiteExecutionPlanById(Integer
								.parseInt(Id));

				if (testerId != null && testerId != -1) {
					UserList tester = userListDAO.getByUserId(testerId);
					workPackageTestSuiteExecutionPlan.setTester(tester);
				}
				if (testLeadId != null && testLeadId != -1) {
					UserList testLead = userListDAO.getByUserId(testLeadId);
					workPackageTestSuiteExecutionPlan.setTestLead(testLead);
				}
				if (plannedExecutionDate != null
						&& !plannedExecutionDate.equals("")) {
					workPackageTestSuiteExecutionPlan
							.setPlannedExecutionDate(DateUtility
									.dateformatWithOutTime(plannedExecutionDate));
				}

				if (executionPriorityId != -1) {
					executionPriority = workPackageDAO
							.getExecutionPriorityById(executionPriorityId);
					workPackageTestSuiteExecutionPlan
							.setExecutionPriority(executionPriority);
				}

				if (shiftId != -1) {
					WorkShiftMaster workShiftMaster = workPackageDAO
							.getWorkShiftById(shiftId);
					workPackageTestSuiteExecutionPlan
							.setPlannedWorkShiftMaster(workShiftMaster);
				}
				if (workPackageTestSuiteExecutionPlan.getExecutionPriority() == null) {
					if (workPackageTestSuiteExecutionPlan.getTestsuite()
							.getExecutionPriority() != null)
						executionPriority = workPackageDAO
								.getExecutionPriorityByName(CommonUtility
										.getExecutionPriority(workPackageTestSuiteExecutionPlan
												.getTestsuite()
												.getExecutionPriority()
												.getPriorityName()));
					else
						executionPriority = workPackageDAO
								.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);

					workPackageTestSuiteExecutionPlan
							.setExecutionPriority(executionPriority);

				}
				workPackageDAO
						.updateWorkPackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlan);

				List<WorkPackageTestCaseExecutionPlan> currentWorkPackageTestCaseExecutionPlanList = null;
				currentWorkPackageTestCaseExecutionPlanList = workPackageDAO
						.getWorkPackageTestcaseExecutionPlanByWorkPackageId(
								workPackageTestSuiteExecutionPlan
										.getWorkPackage().getWorkPackageId(),
								-1, workPackageTestSuiteExecutionPlan
										.getTestsuite().getTestSuiteId(), -1,
								"TestSuite", workPackageTestSuiteExecutionPlan
										.getRunConfiguration()
										.getRunconfiguration().getRunconfigId());
				for (WorkPackageTestCaseExecutionPlan wptcep : currentWorkPackageTestCaseExecutionPlanList) {
					wptcep.setPlannedExecutionDate(DateUtility
							.dateformatWithOutTime(plannedExecutionDate));
					wptcep.setPlannedWorkShiftMaster(workPackageDAO
							.getWorkShiftByName(workPackageTestSuiteExecutionPlan
									.getPlannedWorkShiftMaster().getShiftName()));
					if (workPackageTestSuiteExecutionPlan.getTestLead() != null)
						wptcep.setTestLead(workPackageTestSuiteExecutionPlan
								.getTestLead());
					if (workPackageTestSuiteExecutionPlan.getTester() != null)
						wptcep.setTester(workPackageTestSuiteExecutionPlan
								.getTester());
					if (wptcep.getTester() != null
							&& wptcep.getTestLead() != null) {
						wptcep.setExecutionStatus(1);
					}
					wptcep.setExecutionPriority(executionPriority);

					workPackageDAO
							.updateWorkPackageTestCaseExecutionPlan(wptcep);
				}
			}

		} catch (Exception e) {
			log.info("Problem in adding Plan details records", e);
			return;
		}
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlanStatusDTO listWorkpackageFeatureExecutionPlanningStatus(
			Integer workPackageId) {
	
		return workPackageDAO
				.listWorkpackageFeatureExecutionPlanningStatus(workPackageId);
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlanStatusDTO listWorkpackageTestSuiteExecutionPlanningStatus(
			Integer workPackageId) {
	
		return workPackageDAO
				.listWorkpackageTestSuiteExecutionPlanningStatus(workPackageId);
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan addWorkpPackageTestCaseExecutionPlans(
			TestCaseList testCaseList, TestSuiteList testSuiteList,
			WorkPackage workPackage,
			WorkpackageRunConfiguration wpRunConfiguration,
			ProductFeature feature, String sourceType, TestRunJob testRunJob) {

		testCaseList = testCaseListDAO.getByTestCaseId(testCaseList
				.getTestCaseId());
		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
		workPackageTestCaseExecutionPlan.setTestCase(testCaseList);
		workPackageTestCaseExecutionPlan.setTestSuiteList(testSuiteList);
		workPackageTestCaseExecutionPlan.setWorkPackage(workPackage);
		workPackageTestCaseExecutionPlan.setFeature(feature);
		workPackageTestCaseExecutionPlan.setPlannedExecutionDate(workPackage
				.getPlannedStartDate());
		workPackageTestCaseExecutionPlan
				.setRunConfiguration(wpRunConfiguration);
		workPackageTestCaseExecutionPlan.setExecutionStatus(3);
		workPackageTestCaseExecutionPlan.setIsExecuted(0);
		workPackageTestCaseExecutionPlan.setSourceType(sourceType);
		workPackageTestCaseExecutionPlan.setStatus(1);
		workPackageTestCaseExecutionPlan.setTestRunJob(testRunJob);
		ExecutionPriority executionPriority = null;
		if (sourceType.equalsIgnoreCase("TestSuite")) {
			if (testSuiteList.getExecutionPriority() != null) {
				executionPriority = workPackageDAO
						.getExecutionPriorityByName(CommonUtility
								.getExecutionPriority(testSuiteList
										.getExecutionPriority()
										.getPriorityName()));
			} else {
				executionPriority = workPackageDAO
						.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
			}

		} else if (sourceType.equalsIgnoreCase("Feature")) {
			if (feature.getExecutionPriority() != null) {
				executionPriority = workPackageDAO
						.getExecutionPriorityByName(CommonUtility
								.getExecutionPriority(feature
										.getExecutionPriority()
										.getPriorityName()));
			} else {
				executionPriority = workPackageDAO
						.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
			}
		} else if (testCaseList.getTestCasePriority() != null) {
			executionPriority = workPackageDAO
					.getExecutionPriorityByName(CommonUtility
							.getExecutionPriority(testCaseList
									.getTestCasePriority().getPriorityName()));
		} else {
			executionPriority = workPackageDAO
					.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
		}
		workPackageTestCaseExecutionPlan
				.setExecutionPriority(executionPriority);
		TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
		testCaseExecutionResult.setResult("");
		testCaseExecutionResult.setComments("");
		testCaseExecutionResult.setDefectsCount(0);
		testCaseExecutionResult.setDefectIds("");
		testCaseExecutionResult.setIsApproved(0);
		testCaseExecutionResult.setIsReviewed(0);
		testCaseExecutionResult.setObservedOutput("");

		testCaseExecutionResult
				.setWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
		workPackageTestCaseExecutionPlan
				.setTestCaseExecutionResult(testCaseExecutionResult);

		return workPackageTestCaseExecutionPlan;
	}

	@Override
	@Transactional
	public WorkPackageFeatureExecutionPlan getWorkpackageFeatureExecutionPlan(
			Integer workPackageId, Integer productFeatureId,
			Integer runConfigurationId) {
	
		return workPackageDAO.getWorkpackageFeatureExecutionPlan(workPackageId,
				productFeatureId, runConfigurationId);
	}

	@Override
	@Transactional
	public void mapWorkpackageWithFeature(Integer workPackageId,
			Integer featureId, String action) {
	
		workPackageDAO.mapWorkpackageWithFeature(workPackageId, featureId,
				action);
	}

	@Override
	@Transactional
	public void deleteWorkpackageFeatureExecutionPlan(
			WorkPackageFeatureExecutionPlan wpfep) {
	
		workPackageDAO.deleteWorkpackageFeatureExecutionPlan(wpfep);
	}

	@Override
	@Transactional
	public WorkPackage getWorkPackageByProductBuildId(Integer workPackageId) {
	
		return workPackageDAO.getWorkPackageByProductBuildId(workPackageId);
	}

	@Override
	@Transactional
	public int getWorkPackagesCountByBuildId(int productBuildId) {
	
		return workPackageDAO.getWorkPackagesCountByBuildId(productBuildId);
	}
	
	@Override
	@Transactional
	public WorkPackageFeature getWorkpackageFeaturePlanById(Integer tsWpId) {
	
		return workPackageDAO.getWorkPackageFeatureById(tsWpId);
	}

	@Override
	@Transactional
	public WorkPackageTestCase getWorkpackageTestCaseByPlanId(Integer tcWpId) {
	
		return workPackageDAO.getWorkpackageTestCaseByPlanId(tcWpId);
	}

	@Override
	@Transactional
	public WorkPackageTestSuite getWorkpackageTestSuiteByPlanId(Integer tsWpId) {
	
		return workPackageDAO.getWorkpackageTestSuiteByPlanId(tsWpId);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanBywpId(
			int workPackageId, String filter) {
	
		return workPackageDAO.listWorkPackageTestCasesExecutionPlanBywpId(
				workPackageId, filter);
	}

	@Override
	@Transactional
	public List<RunConfiguration> listRunConfigurationBywpId(
			Integer workPackageId) {
	
		return environmentDAO.listRunConfigurationBywpId(workPackageId);
	}

	public List<TestCaseDTO> getWorkPackageTestCaseExeResultSummary(
			Integer workPackageId) {
		List<TestCaseDTO> testCaseDTOList = new ArrayList<TestCaseDTO>(0);
		try {
			int totalPass = 0;
			int totalFail = 0;
			int totalNoRun = 0;
			int totalBlock = 0;
			int defectsCount = 0;
			int environmentCount = 0;
			Integer countForTestCase = 0;
			TestCaseDTO testCaseDTO = new TestCaseDTO();
			String filterArr[] = { "TestSuiteTestCase", "ProductFeature" };
			List<TestCaseList> testCaseList = new ArrayList<TestCaseList>(0);
			Map<TestSuiteList, List<TestCaseList>> tsMap = new HashMap<TestSuiteList, List<TestCaseList>>(
					0);
			Map<ProductFeature, List<TestCaseList>> pfMap = new HashMap<ProductFeature, List<TestCaseList>>(
					0);
			Map<TestCaseList, Integer[]> tcMap = new HashMap<TestCaseList, Integer[]>(
					0);
			for (String filter : filterArr) {
				testCaseDTO = new TestCaseDTO();
				countForTestCase = 0;
				List<WorkPackageTestCaseExecutionPlan> wptcplanList = workPackageDAO
						.listWorkPackageTestCasesExecutionPlanBywpId(
								workPackageId, filter);

				// 
				if (wptcplanList.size() != 0) {
					WorkPackageTestCaseExecutionPlan wptcp = wptcplanList
							.get(0);
					environmentCount = wptcp.getWorkPackage()
							.getTestRunJobSet().size();
					for (WorkPackageTestCaseExecutionPlan wptcPlan : wptcplanList) {
						Integer prorityIdArr[] = { 0, 0, 0, 0, 0 };
						totalPass = 0;
						totalFail = 0;
						totalNoRun = 0;
						totalBlock = 0;
						defectsCount = 0;
						if (filter.equals("TestSuiteTestCase")) {
							if (wptcPlan.getTestSuiteList() != null) {

								if (tsMap.containsKey(wptcPlan
										.getTestSuiteList())) {
									List<TestCaseList> tclistFromMap = tsMap
											.get(wptcPlan.getTestSuiteList());
									if (!tclistFromMap.contains(wptcPlan
											.getTestCase())) {

										if (++countForTestCase > 1) {
											testCaseDTOList.add(testCaseDTO);
											testCaseDTO = new TestCaseDTO();
											totalPass = 0;
											totalFail = 0;
											totalNoRun = 0;
											totalBlock = 0;
											defectsCount = 0;
											tcMap = new HashMap<TestCaseList, Integer[]>(
													0);

										}
										testCaseList
												.add(wptcPlan.getTestCase());
										tsMap.put(wptcPlan.getTestSuiteList(),
												testCaseList);
										tcMap.put(wptcPlan.getTestCase(),
												prorityIdArr);
										testCaseDTO.setTestCaseId(wptcPlan
												.getTestCase().getTestCaseId());
										testCaseDTO.setTestCaseCode(wptcPlan
												.getTestCase()
												.getTestCaseCode());
										testCaseDTO.setTestCaseName(wptcPlan
												.getTestCase()
												.getTestCaseName());
										testCaseDTO.setProductFeature(null);
										testCaseDTO.setTestSuiteName(wptcPlan
												.getTestSuiteList()
												.getTestSuiteName());
										testCaseDTO
												.setEnvironmentCount((long) environmentCount);

									} else {
										prorityIdArr = tcMap.get(wptcPlan
												.getTestCase());
										totalPass = prorityIdArr[0];
										totalFail = prorityIdArr[1];
										totalNoRun = prorityIdArr[2];
										totalBlock = prorityIdArr[3];
										defectsCount = prorityIdArr[4];
									}

								} else {
									tcMap = new HashMap<TestCaseList, Integer[]>(
											0);
									if (++countForTestCase > 1) {
										testCaseDTOList.add(testCaseDTO);
										testCaseDTO = new TestCaseDTO();
										totalPass = 0;
										totalFail = 0;
										totalNoRun = 0;
										totalBlock = 0;
										defectsCount = 0;
										tcMap = new HashMap<TestCaseList, Integer[]>(
												0);
									}
									testCaseList = new ArrayList<TestCaseList>(
											0);
									testCaseList.add(wptcPlan.getTestCase());
									tsMap.put(wptcPlan.getTestSuiteList(),
											testCaseList);
									tcMap.put(wptcPlan.getTestCase(),
											prorityIdArr);
									testCaseDTO.setTestCaseId(wptcPlan
											.getTestCase().getTestCaseId());
									testCaseDTO.setTestCaseCode(wptcPlan
											.getTestCase().getTestCaseCode());
									testCaseDTO.setTestCaseName(wptcPlan
											.getTestCase().getTestCaseName());
									testCaseDTO.setProductFeature(null);
									testCaseDTO.setTestSuiteName(wptcPlan
											.getTestSuiteList()
											.getTestSuiteName());
									testCaseDTO
											.setEnvironmentCount((long) environmentCount);
								}
							} else {

								if (tcMap.containsKey(wptcPlan.getTestCase())) {
									prorityIdArr = tcMap.get(wptcPlan
											.getTestCase());
									totalPass = prorityIdArr[0];
									totalFail = prorityIdArr[1];
									totalNoRun = prorityIdArr[2];
									totalBlock = prorityIdArr[3];
									defectsCount = prorityIdArr[4];
								} else {
									tcMap.put(wptcPlan.getTestCase(),
											prorityIdArr);

									if (++countForTestCase > 1) {
										testCaseDTOList.add(testCaseDTO);
										testCaseDTO = new TestCaseDTO();
										totalPass = 0;
										totalFail = 0;
										totalNoRun = 0;
										totalBlock = 0;
										defectsCount = 0;

									}
									testCaseDTO.setTestCaseId(wptcPlan
											.getTestCase().getTestCaseId());
									testCaseDTO.setTestCaseCode(wptcPlan
											.getTestCase().getTestCaseCode());
									testCaseDTO.setTestCaseName(wptcPlan
											.getTestCase().getTestCaseName());
									testCaseDTO.setProductFeature(null);
									testCaseDTO.setTestSuiteName(null);
									testCaseDTO
											.setEnvironmentCount((long) environmentCount);

								}
							}
						}

						if (filter.equals("ProductFeature")) {
							if (pfMap.containsKey(wptcPlan.getFeature())) {
								List<TestCaseList> tclistFromMap = pfMap
										.get(wptcPlan.getFeature());
								if (!tclistFromMap.contains(wptcPlan
										.getTestCase())) {

									if (++countForTestCase > 1) {
										testCaseDTOList.add(testCaseDTO);
										testCaseDTO = new TestCaseDTO();
										totalPass = 0;
										totalFail = 0;
										totalNoRun = 0;
										totalBlock = 0;
										defectsCount = 0;
										tcMap = new HashMap<TestCaseList, Integer[]>(
												0);

									}
									testCaseList.add(wptcPlan.getTestCase());
									pfMap.put(wptcPlan.getFeature(),
											testCaseList);
									tcMap.put(wptcPlan.getTestCase(),
											prorityIdArr);
									testCaseDTO.setTestCaseId(wptcPlan
											.getTestCase().getTestCaseId());
									testCaseDTO.setTestCaseCode(wptcPlan
											.getTestCase().getTestCaseCode());
									testCaseDTO.setTestCaseName(wptcPlan
											.getTestCase().getTestCaseName());
									testCaseDTO.setProductFeature(wptcPlan
											.getFeature());
									testCaseDTO.setTestSuiteName(null);
									testCaseDTO
											.setEnvironmentCount((long) environmentCount);

								} else {
									prorityIdArr = tcMap.get(wptcPlan
											.getTestCase());
									totalPass = prorityIdArr[0];
									totalFail = prorityIdArr[1];
									totalNoRun = prorityIdArr[2];
									totalBlock = prorityIdArr[3];
									defectsCount = prorityIdArr[4];
								}

							} else {
								tcMap = new HashMap<TestCaseList, Integer[]>(0);
								if (++countForTestCase > 1) {
									testCaseDTOList.add(testCaseDTO);
									testCaseDTO = new TestCaseDTO();
									totalPass = 0;
									totalFail = 0;
									totalNoRun = 0;
									totalBlock = 0;
									defectsCount = 0;
									tcMap = new HashMap<TestCaseList, Integer[]>(
											0);
								}
								testCaseList = new ArrayList<TestCaseList>(0);
								testCaseList.add(wptcPlan.getTestCase());
								pfMap.put(wptcPlan.getFeature(), testCaseList);
								tcMap.put(wptcPlan.getTestCase(), prorityIdArr);
								testCaseDTO.setTestCaseId(wptcPlan
										.getTestCase().getTestCaseId());
								testCaseDTO.setTestCaseCode(wptcPlan
										.getTestCase().getTestCaseCode());
								testCaseDTO.setTestCaseName(wptcPlan
										.getTestCase().getTestCaseName());
								testCaseDTO.setProductFeature(wptcPlan
										.getFeature());
								testCaseDTO.setTestSuiteName(null);
								testCaseDTO
										.setEnvironmentCount((long) environmentCount);
							}
						}

						TestCaseExecutionResult tcres = wptcPlan
								.getTestCaseExecutionResult();
						if (tcres != null) {
							if (tcres.getResult().equalsIgnoreCase(
									IDPAConstants.EXECUTION_RESULT_PASSED)) {
								prorityIdArr[0] = ++totalPass;
							} else if (tcres.getResult().equalsIgnoreCase(
									IDPAConstants.EXECUTION_RESULT_FAILED)) {
								prorityIdArr[1] = ++totalFail;
							} else if (tcres.getResult().equalsIgnoreCase(
									IDPAConstants.EXECUTION_RESULT_NORUN)) {
								prorityIdArr[2] = ++totalNoRun;
							} else if (tcres.getResult().equalsIgnoreCase(
									IDPAConstants.EXECUTION_RESULT_BLOCKED)) {
								prorityIdArr[3] = ++totalBlock;
							}

							if (tcres.getTestExecutionResultBugListSet().size() != 0) {
								defectsCount = tcres
										.getTestExecutionResultBugListSet()
										.size()
										+ defectsCount;
							}

							prorityIdArr[4] = defectsCount;
							testCaseDTO.setTestCaseExeResArr(prorityIdArr);
							tcMap.put(wptcPlan.getTestCase(), prorityIdArr);
						}
					}
					testCaseDTOList.add(testCaseDTO);
					testCaseDTO = new TestCaseDTO();
				}
			}

		} catch (Exception e) {
			log.error("JSON ERROR", e);
		}
		return testCaseDTOList;
	}

	@Override
	@Transactional
	public List<WorkPackageExecutionPlanUserDetails> listWorkpackageExecutionPlanUserDetails(
			Integer workPackageId, String plannedExecutionDate, String role) {
	
		return workPackageDAO.listWorkpackageExecutionPlanUserDetails(
				workPackageId, plannedExecutionDate, role);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestcaseExecutionPlanByWorkPackageId(
			int workPackageId, int testcaseId, int testSuiteId, int featureId,
			String type, int runConfigId) {
	
		return workPackageDAO
				.getWorkPackageTestcaseExecutionPlanByWorkPackageId(
						workPackageId, testcaseId, testSuiteId, featureId,
						type, runConfigId);
	}

	@Override
	@Transactional
	public List<WorkpackageRunConfiguration> getTestRunJobByWPTCEP(
			Integer workPackageId, Integer userId, String plannedExecutionDate) {
	
		return workPackageDAO.getTestRunJobByWPTCEP(workPackageId, userId,
				plannedExecutionDate);
	}

	@Override
	@Transactional
	public boolean isWorkPackageExecutionAvailable(WorkPackage workPackage) {
	
		return workPackageDAO.isWorkPackageExecutionAvailable(workPackage);
	}

	@Override
	@Transactional
	public WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlanByJobId(
			TestRunJob testRunJob, TestSuiteList testSuiteList) {
	
		return workPackageDAO.workPackageTestSuiteExecutionPlanByJobId(
				testRunJob, testSuiteList);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestCaseExecutionPlanByTestRunJob(
			TestRunJob testRunJob, int testSuiteId) {
	
		return workPackageDAO.getWorkPackageTestCaseExecutionPlanByTestRunJob(
				testRunJob, testSuiteId);
	}

	@Override
	@Transactional
	public List<TestCaseList> getAlreadyExecutedTestcasesForJob(TestRunJob testRunJob, Integer testSuiteId) {
	
		List<WorkPackageTestCaseExecutionPlan>  alreadyExecutedWPTCEPList = workPackageDAO.getWorkPackageTestCaseExecutionPlanByTestRunJob(testRunJob, testSuiteId);
		if (alreadyExecutedWPTCEPList == null || alreadyExecutedWPTCEPList.isEmpty())
			return null;
		
		List<TestCaseList> alreadyExecutedtestcases = new ArrayList<TestCaseList>();
		for (WorkPackageTestCaseExecutionPlan wptcep : alreadyExecutedWPTCEPList) {
			alreadyExecutedtestcases.add(wptcep.getTestCase());
		}
		return alreadyExecutedtestcases;
	}

	
	@Override
	@Transactional
	public List<TestExecutionResultBugList> listDefectsByWorkpackageId(
			int workpackageId) {
	
		return workPackageDAO.listDefectsByWorkpackageId(workpackageId);
	}

	@Override
	@Transactional
	public int addWorkpackageToTestRunPlanGroup(
			TestRunPlanGroup testRunPlanGroup, Map<String, String> mapData,
			UserList user, HttpServletRequest req) {
	
		return workPackageDAO.addWorkpackageToTestRunPlanGroup(
				testRunPlanGroup, mapData, user, req);
	}

	@Override
	@Transactional
	public TestRunPlan getNextTestRunPlan(TestRunPlanGroup testRunPlanGroup,
			TestRunPlan testRunPlan) {
	
		return workPackageDAO.getNextTestRunPlan(testRunPlanGroup, testRunPlan);
	}

	@Override
	@Transactional
	public void workpackageExxecutionPlan(WorkPackage workPackage,
			TestRunPlan testRunPlan, HttpServletRequest req) {
	
		workPackageDAO.workpackageExxecutionPlan(workPackage, testRunPlan, req);
	}

	@Override
	@Transactional
	public int getWPTypeByTestRunPlanExecutionType(int workPackageId) {
		return workPackageDAO
				.getWPTypeByTestRunPlanExecutionType(workPackageId);
	}

	@Override
	@Transactional
	public Set<RunConfiguration> listTestBedByFeature(int featureId,
			int workpackageId, int status) {
	
		return workPackageDAO.listTestBedByFeature(featureId, workpackageId,
				status);
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestcaseExecutionPlan(
			int featureId, int workpackageId, int runConfigId, String type,
			int testSuiteId, int testcaseId) {
	
		return workPackageDAO.listWorkPackageTestcaseExecutionPlan(featureId,
				workpackageId, runConfigId, type, testSuiteId, testcaseId);
	}

	@Override
	@Transactional
	public Set<RunConfiguration> listTestBedByTestSuite(int testsuiteId,
			int workpackageId, int status) {
	
		return workPackageDAO.listTestBedByTestSuite(testsuiteId,
				workpackageId, status);
	}

	public List<TestCaseDTO> getTestCaseExecutionPriorityWiseSummary(
			Integer workPackageId) {
		Integer totalPass = 0;
		Integer totalFail = 0;
		Integer totalNoRun = 0;
		Integer totalBlock = 0;
		Integer totalExecutionPlanTC = 0;
		Integer totalNotExecutedTc = 0;
		Integer totalExecutedTC = 0;
		Integer executionPriorityId = 0;
		int filterId = 4;// 4th tab
		List<TestCaseDTO> testCaseDTOList = new ArrayList<TestCaseDTO>();
		List<Object[]> listObjFromDb = workPackageDAO
				.listWorkPackageTestCaseExeSummaryByFilters(workPackageId,
						filterId, null);
		Map<Integer, TestCaseDTO> executionPriorMap = new HashMap<Integer, TestCaseDTO>();
		TestCaseDTO testCaseDTO = null;
		if (listObjFromDb != null) {
			for (Object obj[] : listObjFromDb) {
				executionPriorityId = (Integer) obj[0];
				String resultId = (String) obj[1];
				BigInteger bi1 = (BigInteger) obj[2];
				Integer totalResult = bi1.intValue();
				if (executionPriorMap.containsKey(executionPriorityId)) {
					testCaseDTO = executionPriorMap.get(executionPriorityId);
				} else {
					testCaseDTO = new TestCaseDTO();
					executionPriorMap.put(executionPriorityId, testCaseDTO);
					totalPass = 0;
					totalFail = 0;
					totalNoRun = 0;
					totalBlock = 0;
					totalExecutionPlanTC = 0;
					totalNotExecutedTc = 0;
					totalExecutedTC = 0;
				}
				if (resultId == null || resultId.equals("")) {
					totalNotExecutedTc = totalResult;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_PASSED)) {
					totalPass = totalResult;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_FAILED)) {
					totalFail = totalResult;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_NORUN)) {
					totalNoRun = totalResult;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_BLOCKED)) {
					totalBlock = totalResult;

				}
				totalExecutedTC = totalPass + totalFail + totalNoRun
						+ totalBlock;
				totalExecutionPlanTC = totalExecutedTC + totalNotExecutedTc;
				testCaseDTO.setExecutionPriorityId(executionPriorityId);
				testCaseDTO.setTotalPass(totalPass);
				testCaseDTO.setTotalFail(totalFail);
				testCaseDTO.setTotalNoRun(totalNoRun);
				testCaseDTO.setTotalBlock(totalBlock);
				testCaseDTO.setNotExecuted(totalNotExecutedTc);
				testCaseDTO.setTotalExecutedTesCases(totalExecutedTC);
				testCaseDTO.setTotalExecutionPlanTestCase(totalExecutionPlanTC);

				executionPriorMap.put(executionPriorityId, testCaseDTO);

			}

		}

		Integer prioritiesArr[] = { 1, 2, 3, 4, 5 };
		for (Integer i : prioritiesArr) {
			boolean flag = false;
			for (Map.Entry<Integer, TestCaseDTO> entry : executionPriorMap
					.entrySet()) {
				testCaseDTO = new TestCaseDTO();
				executionPriorityId = entry.getKey();
				testCaseDTO = entry.getValue();

				if (executionPriorityId.equals(i)) {
					flag = true;
					testCaseDTOList.add(i - 1, testCaseDTO);
					break;
				}
			}
			if (!flag) {
				testCaseDTO = new TestCaseDTO();
				testCaseDTO.setExecutionPriorityId(i);
				testCaseDTO.setTotalPass(0);
				testCaseDTO.setTotalFail(0);
				testCaseDTO.setTotalNoRun(0);
				testCaseDTO.setTotalBlock(0);
				testCaseDTO.setNotExecuted(0);
				testCaseDTO.setTotalExecutedTesCases(0);
				testCaseDTO.setTotalExecutionPlanTestCase(0);
				testCaseDTOList.add(i - 1, testCaseDTO);
				testCaseDTO = null;
			}

		}
		return testCaseDTOList;
	}

	@Override
	@Transactional
	public TestRunJob getTestRunJobById(int testRunJobId) {
	
		return workPackageDAO.getTestRunJobById(testRunJobId);
	}

	@Override
	@Transactional
	public WorkPackage addWorkpackageToClonedProductBuild(
			ProductBuild clonedProductBuild, UserList user,
			HttpServletRequest req, WorkPackage workPackageToBeCloned) {
		WorkPackage workPackage = workPackageDAO
				.addWorkpackageToClonedProductBuild(clonedProductBuild, user,
						req, workPackageToBeCloned);
		return workPackage;
	}

	@Override
	@Transactional
	public void copyPlanningDetailsInClonedWorkPacakge(
			String mONGODB_AVAILABLE, Integer workPackageIdExisting,
			WorkPackage clonedWp, UserList user) {

		ProductMaster productMaster = clonedWp.getProductBuild()
				.getProductVersion().getProductMaster();
		ProductVersionListMaster productVersion = clonedWp.getProductBuild()
				.getProductVersion();

		Integer productModeId = productMaster.getProductType()
				.getProductTypeId();

		if (productModeId == 1) {
			List<WorkPackageDemandProjection> wpdpExistList = workPackageDAO
					.listWorkPackageDemandProjectionByDate(
							workPackageIdExisting, -1, null);
			WorkPackage workpPackageExisting = workPackageDAO
					.getWorkPackageByIdWithMinimalnitialization(workPackageIdExisting);
			long demanddifference = 0;
			String existingWPPSD = DateUtility
					.sdfDateformatWithOutTime(workpPackageExisting
							.getPlannedStartDate());
			for (WorkPackageDemandProjection wpdp : wpdpExistList) {
				if (wpdp.getWorkDate() != null) {
					WorkPackageDemandProjection workPackageDemandProjection = new WorkPackageDemandProjection();
					workPackageDemandProjection.setWorkPackage(clonedWp);
					workPackageDemandProjection.setWorkShiftMaster(wpdp
							.getWorkShiftMaster());
					workPackageDemandProjection.setSkill(wpdp.getSkill());
					workPackageDemandProjection.setUserRole(wpdp.getUserRole());
					demanddifference = DateUtility.DateDifference(
							existingWPPSD, DateUtility
									.sdfDateformatWithOutTime(wpdp
											.getWorkDate()));
					workPackageDemandProjection.setWorkDate(DateUtility
							.addOnlyWorkingDays(clonedWp.getPlannedStartDate(),
									(int) demanddifference));
					workPackageDemandProjection.setResourceCount(wpdp
							.getResourceCount());
					workPackageDemandProjection.setDemandRaisedOn(new Date());
					workPackageDemandProjection.setDemandRaisedByUser(user);
					workPackageDAO
							.addWorkPackageDemandProjection(workPackageDemandProjection);
				}
			}
		}

		workPackageDAO.cloneBuildPlan(workPackageIdExisting, clonedWp, user);
		workPackageDAO.cloneBuildExecution(workPackageIdExisting, clonedWp,
				user);

	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listDefectsByTestcaseExecutionPlanIdByApprovedStatus(
			int productId, int productVersionId, int productBuildId,
			int workPackageId, int approveStatus, Date startDate, Date endDate,
			Integer raisedByUser, Integer jtStartIndex, Integer jtPageSize) {
		List<TestExecutionResultBugList> testExecutionResultBugList = new ArrayList<TestExecutionResultBugList>();
		List<Object> bugIdsList = workPackageDAO
				.listDefectsByTestcaseExecutionPlanIdByApprovedStatus(
						productId, productVersionId, productBuildId,
						workPackageId, approveStatus, startDate, endDate,
						raisedByUser, jtStartIndex, jtPageSize);
		if (bugIdsList != null && bugIdsList.size() > 0) {
			for (Object bugId : bugIdsList) {
				TestExecutionResultBugList defect = testExecutionResultBugDAO
						.getByBugWithCompleteInitialization((Integer) bugId);
				if (defect != null) {
					testExecutionResultBugList.add(defect);
				}
			}
		}
		return testExecutionResultBugList;
	}

	public List<TestCaseDTO> listFeaturesByexecutionPriorityWise(
			int workpackageId) {
		Map<Integer, ProductFeature> featureMap = new HashMap<Integer, ProductFeature>();
		List<TestCaseDTO> tcDtoList = new ArrayList<TestCaseDTO>();

		ProductFeature pf = null;
		TestCaseDTO testCaseDto = new TestCaseDTO();
		int totalNotExecutedTC = 0;
		int notExecutedTC = 0;
		int index = -1;
		int featureByexecutionPriority = 5;
		List<Object[]> listObjFromDb = workPackageDAO
				.listWorkPackageTestCaseExeSummaryByFilters(workpackageId,
						featureByexecutionPriority, null);
		int arr[][] = new int[5][4];
		if (listObjFromDb.size() != 0) {
			for (Object obj[] : listObjFromDb) {
				pf = new ProductFeature();
				Integer featureId = (Integer) obj[0];
				String featureName = (String) obj[1];
				Integer executionPriorityId = (Integer) obj[2];
				pf.setProductFeatureId(featureId);
				pf.setProductFeatureName(featureName);
				String resultId = (String) obj[3];
				BigInteger bi1 = (BigInteger) obj[4];
				Integer totalResult = bi1.intValue();

				if (!featureMap.containsKey(featureId)) {
					++index;
					totalNotExecutedTC = 0;
					notExecutedTC = 0;

					testCaseDto = new TestCaseDTO();
					arr = new int[5][4];
					testCaseDto.setProductFeature(pf);
					testCaseDto.setFeaturepriotiesArry(arr);
					testCaseDto.setNotExecuted(totalNotExecutedTC);
					tcDtoList.add(index, testCaseDto);
					featureMap.put(featureId, pf);
					pf = new ProductFeature();
				}
				executionPriorityId = executionPriorityId - 1;
				if (resultId == null || resultId.equals("")) {
					notExecutedTC = totalResult;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_PASSED)) {
					arr[executionPriorityId][0] = totalResult;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_FAILED)) {
					arr[executionPriorityId][1] = totalResult;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_NORUN)) {
					arr[executionPriorityId][2] = totalResult;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_BLOCKED)) {
					arr[executionPriorityId][3] = totalResult;

				}
				testCaseDto.setFeaturepriotiesArry(arr);
				totalNotExecutedTC = totalNotExecutedTC + notExecutedTC;
				testCaseDto.setNotExecuted(totalNotExecutedTC);
				tcDtoList.set(index, testCaseDto);

			}
		}
		return tcDtoList;
	}

	@Override
	@Transactional
	public EvidenceType getEvidenceTypeById(Integer evidenceTypeId) {
	
		return workPackageDAO.getEvidenceTypeById(evidenceTypeId);
	}

	@Override
	@Transactional
	public List<EvidenceType> getEvidenceTypes() {
	
		return workPackageDAO.getEvidenceTypes();
	}

	@Override
	@Transactional
	public JSONArray listFeaturesByTestRunBeds(int workpackageId,
			List<RunConfiguration> runConfigList) {
		Map<Integer, ProductFeature> featureMap = new HashMap<Integer, ProductFeature>();
		JSONObject rcTitle = null;
		JSONArray finalList = new JSONArray();
		JSONArray columnData = new JSONArray();

		TestCaseDTO testCaseDto = new TestCaseDTO();
		List<TestCaseDTO> tcDtoList = new ArrayList<TestCaseDTO>();
		int envIndex = 0;
		int featureByTestBeds = 8;
		int totalNotExecutedTC = 0;
		int totalPlanExecutionTcs = 0;
		int totalExecutedTcs = 0;
		int notExecutedTC = 0;
		int index = -1;
		int envCombCount = runConfigList.size();
		List<Object[]> listObjFromDb = workPackageDAO
				.listWorkPackageTestCaseExeSummaryByFilters(workpackageId,
						featureByTestBeds, null);
		int arr[][] = new int[envCombCount][4];
		if (listObjFromDb.size() != 0) {
			for (Object obj[] : listObjFromDb) {
				ProductFeature pf = new ProductFeature();
				RunConfiguration runCnfg = new RunConfiguration();
				Integer featureId = (Integer) obj[0];
				String featureName = (String) obj[1];
				Integer runconfigId = (Integer) obj[2];

				runCnfg.setRunconfigId(runconfigId);
				pf.setProductFeatureId(featureId);
				pf.setProductFeatureName(featureName);
				String resultId = (String) obj[3];
				BigInteger bi1 = (BigInteger) obj[4];
				Integer totalResult = bi1.intValue();

				if (!featureMap.containsKey(featureId)) {
					++index;
					totalNotExecutedTC = 0;
					notExecutedTC = 0;
					totalExecutedTcs = 0;
					testCaseDto = new TestCaseDTO();
					arr = new int[envCombCount][4];
					testCaseDto.setProductFeature(pf);
					testCaseDto.setFeaturepriotiesArry(arr);
					testCaseDto.setNotExecuted(totalNotExecutedTC);
					tcDtoList.add(index, testCaseDto);
					featureMap.put(featureId, pf);
					pf = new ProductFeature();
				}
				if (runConfigList.contains(runCnfg)) {
					envIndex = runConfigList.indexOf(runCnfg);
				}
				if (resultId == null || resultId.equals("")) {
					notExecutedTC = totalResult;
					totalResult = 0;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_PASSED)) {
					arr[envIndex][0] = totalResult;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_FAILED)) {
					arr[envIndex][1] = totalResult;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_NORUN)) {
					arr[envIndex][2] = totalResult;
				} else if (resultId
						.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_BLOCKED)) {
					arr[envIndex][3] = totalResult;

				}
				testCaseDto.setFeaturepriotiesArry(arr);
				totalNotExecutedTC = totalNotExecutedTC + notExecutedTC;
				totalExecutedTcs = totalExecutedTcs + totalResult;
				;
				testCaseDto.setTotalExecutedTesCases(totalExecutedTcs);
				testCaseDto.setNotExecuted(totalNotExecutedTC);
				tcDtoList.set(index, testCaseDto);
				notExecutedTC = 0;
			}

		}
		for (TestCaseDTO tcDto : tcDtoList) {
			totalPlanExecutionTcs = 0;
			columnData.add(tcDto.getProductFeature().getProductFeatureId());
			columnData.add(tcDto.getProductFeature().getProductFeatureName());
			totalPlanExecutionTcs = tcDto.getTotalExecutedTesCases()
					+ tcDto.getNotExecuted();
			columnData.add(totalPlanExecutionTcs);
			columnData.add(tcDto.getTotalExecutedTesCases());
			columnData.add(tcDto.getNotExecuted());

			int featureEnvCombResultsArr[][] = tcDto.getFeaturepriotiesArry();
			if (featureEnvCombResultsArr != null) {
				for (int i = 0; i < envCombCount; i++) {
					columnData.add(featureEnvCombResultsArr[i][0]);
					columnData.add(featureEnvCombResultsArr[i][1]);
					columnData.add(featureEnvCombResultsArr[i][2]);
					columnData.add(featureEnvCombResultsArr[i][3]);
				}
			}
			finalList.add(columnData);
			columnData = new JSONArray();
		}

		return finalList;
	}

	@Override
	@Transactional
	public List<WorkPackage> getActiveWorkpackagesByProductBuildId(
			Integer productBuildId) {
	
		return workPackageDAO
				.getActiveWorkpackagesByProductBuildId(productBuildId);
	}

	@Override
	@Transactional
	public WorkPackage getWorkPackageByIdWithMinimalnitialization(
			int workPackageId) {
		return workPackageDAO
				.getWorkPackageByIdWithMinimalnitialization(workPackageId);
	}

	@Override
	public int totalWorkPackageDemandProjectionCountByWpId(int workPackageId,
			int ShiftId, Date date) {
		return workPackageDAO.totalWorkPackageDemandProjectionCountByWpId(
				workPackageId, ShiftId, date);
	}

	@Override
	@Transactional
	public List<TestCaseList> getSelectedTestCasesFromTestRunJob(
			int testRunListId) {
	
		return workPackageDAO.getSelectedTestCasesFromTestRunJob(testRunListId);
	}

	@Override
	@Transactional
	public TestExecutionResultBugList createBug(
			TestStepExecutionResult testStepExecutionResult, ProductBuild productBuild) {

		try {
			TestExecutionResultBugList bug = new TestExecutionResultBugList();
			String testStepObservedOutput = "";
			if(testStepExecutionResult.getObservedOutput() != null){
				testStepObservedOutput = testStepExecutionResult.getObservedOutput();
			}
			
			bug.setBugTitle(testStepExecutionResult.getTestcase()
					.getTestCaseId()
					+ " : "
					+ testStepExecutionResult.getTestSteps().getTestStepId()
					+ " : " + "Failed");
			bug.setBugDescription("Expected : "
					+ testStepExecutionResult.getTestSteps()
							.getTestStepExpectedOutput() + "\n"
					+ "Actual Result" + " : "
					+ testStepObservedOutput);
			bug.setFileBugInBugManagementSystem(false);
			WorkFlow workFlow = workPackageDAO.getWorkFlowByEntityIdWorkFlowId(
					IDPAConstants.ENTITY_DEFECT_ID,
					IDPAConstants.WORKFLOW_STAGE_ID_NEW);
			bug.setBugFilingStatus(workFlow);
			bug.setTestCaseExecutionResult(testStepExecutionResult
					.getTestCaseExecutionResult());
			bug.setBugCreationTime(testStepExecutionResult.getTestStepEndtime());
			bug.setTestStepExecutionResult(testStepExecutionResult);
			bug.setReportedInBuild(productBuild);
			log.info("New Bug Data : Title: " + bug.getBugTitle()
					+ " : Bug Description: " + bug.getBugDescription()
					+ " : File Bug: " + bug.getFileBugInBugManagementSystem()
					+ " : Filing Status: " + bug.getBugFilingStatus());
			bug = workPackageDAO.createBug(bug);
			return bug;
		} catch (Exception e) {
			log.error("Problem creating bug for Test Step Execution Result : "
					+ testStepExecutionResult.getTeststepexecutionresultid(), e);
			return null;
		}
	}

	@Override
	@Transactional
	public void saveTestStepExecutionResult(
			TestStepExecutionResult testStepExecutionResultList) {
	
		workPackageDAO.saveTestStepExecutionResult(testStepExecutionResultList);
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> getTestRunJobDefects(
			int testRunJobId) {
	
		return workPackageDAO.getTestRunJobDefects(testRunJobId);
	}

	@Override
	@Transactional
	public HashMap<Integer, JsonMetricsProgramExecutionProcess> getWorkpackageTestcaseExecutionPlanByDateFilter(
			int status, Date startDate, Date endDate) {

		List<MetricsMasterDTO> listofMetricsMasterDTO = null;
		listofMetricsMasterDTO = workPackageDAO
				.getWorkpackageTestcaseExecutionPlanByDateFilter(status,
						startDate, endDate);
		log.info("listofMetricsMasterDTO=-=-" + listofMetricsMasterDTO.size());
		HashMap<Integer, JsonMetricsProgramExecutionProcess> mapOfJsonMetricsMaster = new HashMap<Integer, JsonMetricsProgramExecutionProcess>();

		if (listofMetricsMasterDTO != null && listofMetricsMasterDTO.size() > 0) {
			JsonMetricsProgramExecutionProcess jsonProgramExecutionMetricsMaster = null;
			for (MetricsMasterDTO metricsMasterDTO : listofMetricsMasterDTO) {
				if (mapOfJsonMetricsMaster.containsKey(metricsMasterDTO
						.getWorkPackageId())) {
					jsonProgramExecutionMetricsMaster = mapOfJsonMetricsMaster
							.get(metricsMasterDTO.getWorkPackageId());
				} else {
					jsonProgramExecutionMetricsMaster = new JsonMetricsProgramExecutionProcess();
				}
				jsonProgramExecutionMetricsMaster.setProductId(metricsMasterDTO
						.getProductId());
				jsonProgramExecutionMetricsMaster
						.setProductName(metricsMasterDTO.getProductName());
				jsonProgramExecutionMetricsMaster
						.setWorkPackageId(metricsMasterDTO.getWorkPackageId());
				jsonProgramExecutionMetricsMaster
						.setWorkPackageName(metricsMasterDTO
								.getWorkPackageName());
				if (metricsMasterDTO.getExecutionStatus() == IDPAConstants.TESTCASE_EXECUTION_STATUS_COMPLETED) {
					jsonProgramExecutionMetricsMaster
							.setTestCaseCompletedCount(metricsMasterDTO
									.getTestCaseCompletedCount());
				}
				if (metricsMasterDTO.getExecutionStatus() == IDPAConstants.TESTCASE_EXECUTION_STATUS_ASSIGNED) {
					jsonProgramExecutionMetricsMaster
							.setTestCaseAllocatedCount(metricsMasterDTO
									.getTestCaseAllocatedCount());
				}
				if (metricsMasterDTO.getExecutionStatus() == IDPAConstants.TESTCASE_EXECUTION_STATUS_NOT_STARTED) {
					jsonProgramExecutionMetricsMaster
							.setTestCaseNotStartedCount(metricsMasterDTO
									.getTestCaseNotStartedCount());
				}
				mapOfJsonMetricsMaster.put(metricsMasterDTO.getWorkPackageId(),
						jsonProgramExecutionMetricsMaster);
			}
		}

		log.info("mapOfJsonMetricsMaster: " + mapOfJsonMetricsMaster.size());
		return mapOfJsonMetricsMaster;
	}

	@Override
	@Transactional
	public HashMap<Integer, JsonMetricsMasterProgramDefects> getTotalBugListByStatus(
			Integer status) {

		List<MetricsMasterDefectsDTO> listofMetricsMasterDefectsDTO = null;
		listofMetricsMasterDefectsDTO = workPackageDAO
				.getTotalBugListByStatus(status);
		log.info("listofMetricsMasterDefectsDTO=-=-"
				+ listofMetricsMasterDefectsDTO.size());
		HashMap<Integer, JsonMetricsMasterProgramDefects> mapOfJsonMetricsMaster = new HashMap<Integer, JsonMetricsMasterProgramDefects>();

		if (listofMetricsMasterDefectsDTO != null
				&& listofMetricsMasterDefectsDTO.size() > 0) {
			JsonMetricsMasterProgramDefects jsonMetricsMasterProgramDefects = null;
			for (MetricsMasterDefectsDTO metricsMasterDefectsDTO : listofMetricsMasterDefectsDTO) {
				if (mapOfJsonMetricsMaster.containsKey(metricsMasterDefectsDTO
						.getProductId())) {
					jsonMetricsMasterProgramDefects = mapOfJsonMetricsMaster
							.get(metricsMasterDefectsDTO.getProductId());
				} else {
					jsonMetricsMasterProgramDefects = new JsonMetricsMasterProgramDefects();
				}
				jsonMetricsMasterProgramDefects
						.setProductId(metricsMasterDefectsDTO.getProductId());
				jsonMetricsMasterProgramDefects
						.setProductName(metricsMasterDefectsDTO
								.getProductName());

				if (metricsMasterDefectsDTO.getSeverityId() == IDPAConstants.TESTCASE_DEFECTS_SEVERITYID_BLOCKER) {
					jsonMetricsMasterProgramDefects
							.setSeverityId(metricsMasterDefectsDTO
									.getBlockerDefects());
				}
				if (metricsMasterDefectsDTO.getSeverityId() == IDPAConstants.TESTCASE_DEFECTS_SEVERITYID_NORMAL) {
					jsonMetricsMasterProgramDefects
							.setSeverityId(metricsMasterDefectsDTO
									.getNormalDefects());
				}

				if (metricsMasterDefectsDTO.getBugFilingStatus() == IDPAConstants.TESTCASE_DEFECTS_STATUS_NEW) {
					jsonMetricsMasterProgramDefects
							.setDefectsBugfilingStatusNewCount(metricsMasterDefectsDTO
									.getDefectsBugfilingStatusNewCount());
				}
				if (metricsMasterDefectsDTO.getBugFilingStatus() == IDPAConstants.TESTCASE_DEFECTS_STATUS_REFERBACK) {
					jsonMetricsMasterProgramDefects
							.setDefectsBugfilingStatusreferbackCount(metricsMasterDefectsDTO
									.getDefectsBugfilingStatusreferbackCount());
				}
				if (metricsMasterDefectsDTO.getBugFilingStatus() == IDPAConstants.TESTCASE_DEFECTS_STATUS_REVIEWED) {
					jsonMetricsMasterProgramDefects
							.setDefectsBugfilingStatusreviewedCount(metricsMasterDefectsDTO
									.getDefectsBugfilingStatusreviewedCount());
				}
				if (metricsMasterDefectsDTO.getBugFilingStatus() == IDPAConstants.TESTCASE_DEFECTS_STATUS_APPROVED) {
					jsonMetricsMasterProgramDefects
							.setDefectsBugfilingStatusapprovedCount(metricsMasterDefectsDTO
									.getDefectsBugfilingStatusapprovedCount());
				}
				if (metricsMasterDefectsDTO.getBugFilingStatus() == IDPAConstants.TESTCASE_DEFECTS_STATUS_CLOSED) {
					jsonMetricsMasterProgramDefects
							.setDefectsBugfilingStatusClosedCount(metricsMasterDefectsDTO
									.getDefectsBugfilingStatusClosedCount());
				}
				if (metricsMasterDefectsDTO.getBugFilingStatus() == IDPAConstants.TESTCASE_DEFECTS_STATUS_DUPLICATE) {
					jsonMetricsMasterProgramDefects
							.setDefectsBugfilingStatusDuplicateCount(metricsMasterDefectsDTO
									.getDefectsBugfilingStatusDuplicateCount());
				}
				if (metricsMasterDefectsDTO.getBugFilingStatus() == IDPAConstants.TESTCASE_DEFECTS_STATUS_FIXED) {
					jsonMetricsMasterProgramDefects
							.setDefectsBugfilingStatusFixedCount(metricsMasterDefectsDTO
									.getDefectsBugfilingStatusFixedCount());
				}
				if (metricsMasterDefectsDTO.getBugFilingStatus() == IDPAConstants.TESTCASE_DEFECTS_STATUS_VIRIFIED) {
					jsonMetricsMasterProgramDefects
							.setDefectsBugfilingStatusVerifiedCount(metricsMasterDefectsDTO
									.getDefectsBugfilingStatusVerifiedCount());
				}
				if (metricsMasterDefectsDTO.getBugFilingStatus() == IDPAConstants.TESTCASE_DEFECTS_STATUS_INTENDED_BEHAVIOUR) {
					jsonMetricsMasterProgramDefects
							.setDefectsBugfilingStatusIntendedBehaviourCount(metricsMasterDefectsDTO
									.getDefectsBugfilingStatusIntendedBehaviourCount());
				}
				if (metricsMasterDefectsDTO.getBugFilingStatus() == IDPAConstants.TESTCASE_DEFECTS_STATUS_NOT_REPRODUCIBLE) {
					jsonMetricsMasterProgramDefects
							.setDefectsBugfilingStatusNotReproducibleCount(metricsMasterDefectsDTO
									.getDefectsBugfilingStatusNotReproducibleCount());
				}

				mapOfJsonMetricsMaster.put(
						metricsMasterDefectsDTO.getProductId(),
						jsonMetricsMasterProgramDefects);
			}
		}
		log.info("mapOfJsonMetricsMaster: " + mapOfJsonMetricsMaster.size());
		return mapOfJsonMetricsMaster;

	}

	@Override
	@Transactional
	public HashMap<Integer, JsonMetricsTestCaseResult> getTestcaseExecutionResultList() {
		List<MetricsMasterTestCaseResultDTO> listofMetricsMasterTestCaseResultDTO = null;
		listofMetricsMasterTestCaseResultDTO = workPackageDAO
				.getTestcaseExecutionResultList();
		log.info("listofMetricsMasterDTO=-=-"
				+ listofMetricsMasterTestCaseResultDTO.size());
		HashMap<Integer, JsonMetricsTestCaseResult> mapOfJsonMetricsMaster = new HashMap<Integer, JsonMetricsTestCaseResult>();
		try {
			if (listofMetricsMasterTestCaseResultDTO != null
					&& listofMetricsMasterTestCaseResultDTO.size() > 0) {
				JsonMetricsTestCaseResult jsonMetricsTestCaseResult = null;
				for (MetricsMasterTestCaseResultDTO metricsMasterTestCaseResultDTO : listofMetricsMasterTestCaseResultDTO) {
					if (mapOfJsonMetricsMaster
							.containsKey(metricsMasterTestCaseResultDTO
									.getProductId())) {
						jsonMetricsTestCaseResult = mapOfJsonMetricsMaster
								.get(metricsMasterTestCaseResultDTO
										.getProductId());
					} else {
						jsonMetricsTestCaseResult = new JsonMetricsTestCaseResult();
					}
					jsonMetricsTestCaseResult
							.setProductId(metricsMasterTestCaseResultDTO
									.getProductId());
					jsonMetricsTestCaseResult
							.setProductName(metricsMasterTestCaseResultDTO
									.getProductName());
					jsonMetricsTestCaseResult
							.setWorkPackageId(metricsMasterTestCaseResultDTO
									.getWorkPackageId());
					jsonMetricsTestCaseResult
							.setWorkPackageName(metricsMasterTestCaseResultDTO
									.getWorkPackageName());

					if (metricsMasterTestCaseResultDTO.getResult() != null) {
						if (metricsMasterTestCaseResultDTO.getResult().equals(
								IDPAConstants.TESTCASE_PASSED)) {
							jsonMetricsTestCaseResult
									.setTestCasePassedCount(metricsMasterTestCaseResultDTO
											.getTestCasePassedCount());
						}
						if (metricsMasterTestCaseResultDTO.getResult().equals(
								IDPAConstants.TESTCASE_FAILED)) {
							jsonMetricsTestCaseResult
									.setTestCaseFailedCount(metricsMasterTestCaseResultDTO
											.getTestCaseFailedCount());
						}
						if (metricsMasterTestCaseResultDTO.getResult().equals(
								IDPAConstants.TESTCASE_BLOCKED)) {
							jsonMetricsTestCaseResult
									.setTestCaseBlockedCount(metricsMasterTestCaseResultDTO
											.getTestCaseBlockedCount());
						}
						if (metricsMasterTestCaseResultDTO.getResult().equals(
								IDPAConstants.TESTCASE_NORUN)) {
							jsonMetricsTestCaseResult
									.setTestCaseNorunCount(metricsMasterTestCaseResultDTO
											.getTestCaseNorunCount());
						}
						mapOfJsonMetricsMaster.put(
								metricsMasterTestCaseResultDTO.getProductId(),
								jsonMetricsTestCaseResult);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("mapOfJsonMetricsMasterTestCaseResult Size: "
				+ mapOfJsonMetricsMaster.size());
		return mapOfJsonMetricsMaster;
	}

	@Override
	@Transactional
	public List<WorkpackageRunConfiguration> getWorkpackageRunConfigurationOfWPwithrcStatus(
			Integer workpackageId, Integer runConfigStatus) {
		return workPackageDAO.getWorkpackageRunConfigurationOfWPwithrcStatus(
				workpackageId, runConfigStatus);
	}

	@Override
	@Transactional
	public HashMap<Integer, JsonMetricsProgramExecutionProcess> getWorkpackageTestcaseExecutionPlanForResourceByDateFilter(
			int status, Date startDate, Date currentDate) {
		List<MetricsMasterDTO> listofMetricsMasterDTO = null;
		listofMetricsMasterDTO = workPackageDAO
				.getWorkpackageTestcaseExecutionPlanForResourceByDateFilter(
						status, startDate, currentDate);
		log.info("listofMetricsMasterDTO=-=-" + listofMetricsMasterDTO.size());
		HashMap<Integer, JsonMetricsProgramExecutionProcess> mapOfJsonMetricsMaster = new HashMap<Integer, JsonMetricsProgramExecutionProcess>();

		if (listofMetricsMasterDTO != null && listofMetricsMasterDTO.size() > 0) {
			JsonMetricsProgramExecutionProcess jsonProgramExecutionMetricsMaster = null;
			for (MetricsMasterDTO metricsMasterDTO : listofMetricsMasterDTO) {
				if (mapOfJsonMetricsMaster.containsKey(metricsMasterDTO
						.getUserId())) {
					jsonProgramExecutionMetricsMaster = mapOfJsonMetricsMaster
							.get(metricsMasterDTO.getUserId());
				} else {
					jsonProgramExecutionMetricsMaster = new JsonMetricsProgramExecutionProcess();
				}
				jsonProgramExecutionMetricsMaster.setUserId(metricsMasterDTO
						.getUserId());
				jsonProgramExecutionMetricsMaster.setUserName(metricsMasterDTO
						.getLoginId());
				if (metricsMasterDTO.getExecutionStatus() == IDPAConstants.TESTCASE_EXECUTION_STATUS_COMPLETED) {
					int tcCompleted = jsonProgramExecutionMetricsMaster
							.getTestCaseCompletedCount()
							+ metricsMasterDTO.getTestCaseCompletedCount();
					jsonProgramExecutionMetricsMaster
							.setTestCaseCompletedCount(tcCompleted);
				}
				if (metricsMasterDTO.getExecutionStatus() == IDPAConstants.TESTCASE_EXECUTION_STATUS_ASSIGNED) {
					int tcAllocated = jsonProgramExecutionMetricsMaster
							.getTestCaseAllocatedCount()
							+ metricsMasterDTO.getTestCaseAllocatedCount();
					jsonProgramExecutionMetricsMaster
							.setTestCaseAllocatedCount(tcAllocated);
				}
				if (metricsMasterDTO.getExecutionStatus() == IDPAConstants.TESTCASE_EXECUTION_STATUS_NOT_STARTED) {
					int tcNotStarted = jsonProgramExecutionMetricsMaster
							.getTestCaseNotStartedCount()
							+ metricsMasterDTO.getTestCaseNotStartedCount();
					jsonProgramExecutionMetricsMaster
							.setTestCaseNotStartedCount(tcNotStarted);
				}
				jsonProgramExecutionMetricsMaster
						.setTotalPlannedTestcaseCount(metricsMasterDTO
								.getTotalTCAllocatedCount());
				mapOfJsonMetricsMaster.put(metricsMasterDTO.getUserId(),
						jsonProgramExecutionMetricsMaster);
			}
		}
		log.info("mapOfJsonMetricsMaster: " + mapOfJsonMetricsMaster.size());
		return mapOfJsonMetricsMaster;
	}

	@Override
	@Transactional
	public void workpackageExxecutionPlan(WorkPackage workPackage,
			TestRunPlan testRunPlan, HttpServletRequest req,
			String deviceNames, String testcaseNames) {
	
		workPackageDAO.workpackageExxecutionPlan(workPackage, testRunPlan, req,
				deviceNames, testcaseNames);
	}

	@Override
	@Transactional
	public List getTestRunResultFromListAndDetailViews(int testRunNo,
			String deviceId) {
		return workPackageDAO.getTestRunResultFromListAndDetailViews(testRunNo,
				deviceId);
	}

	public List getTestRunResultFromViews(int testRunNo) {
		return workPackageDAO.getTestRunResultFromViews(testRunNo);
	}

	@Override
	@Transactional
	public Evidence getEvidenceById(int evidenceId) {
		return workPackageDAO.getEvidenceById(evidenceId);
	}

	@Override
	@Transactional
	public void deleteEvidence(Evidence evidence) {
		workPackageDAO.deleteEvidence(evidence);
	}

	@Override
	@Transactional
	public List<Object[]> getTescaseExecutionHistory(int testCaseId, int workPackageId, String dataLevel, Integer jtStartIndex, Integer jtPageSize) {
		return workPackageDAO.getTescaseExecutionHistory(testCaseId, workPackageId, dataLevel,
				jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public Integer getWPTCEPCountOfATestCaseId(int testCaseId,
			int jtStartIndex, int jtPageSize) {
		return workPackageDAO.getWPTCEPCountOfATestCaseId(testCaseId,
				jtStartIndex, jtPageSize);

	}

	@Override
	@Transactional
	public List<WorkPackageBuildTestCaseSummaryDTO> listWorkPackageTestCaseExecutionBuildSummary(
			Integer workPackageId, Integer productBuildId) {
		return workPackageDAO.listWorkPackageTestCaseExecutionBuildSummary(
				workPackageId, productBuildId);
	}

	@Override
	@Transactional
	public List<JsonWorkPackageTestCaseExecutionPlanForTester> getWPTCExecutionSummaryByProdIdBuildId(Integer testFactoryId,
			Integer productId, Integer productBuildId, Integer jtStartIndex,
			Integer jtPageSize  , String filter) {
		List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEPList = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
		try {
			List<WorkPackageTCEPSummaryDTO> wtpdto = workPackageDAO.getWPTCExecutionSummaryByProdIdBuildId(testFactoryId,productId,
							productBuildId, jtStartIndex, jtPageSize, filter);
			if (wtpdto != null && wtpdto.size() > 0) {
				for (WorkPackageTCEPSummaryDTO workPackageTCEPSummaryDTO : wtpdto) {
					WorkPackage wp = workPackageDAO.getWorkPackageById(workPackageTCEPSummaryDTO.getWorkPackageId());
					JsonWorkPackageTestCaseExecutionPlanForTester jsonWPTCE = new JsonWorkPackageTestCaseExecutionPlanForTester(workPackageTCEPSummaryDTO);
					Integer wpTotalTestcasesCount = 0;
					int count = 0;
					String testSuiteName = "N/A";
					if(wp.getTestRunJobSet() != null){
						for(TestRunJob trj : wp.getTestRunJobSet()){
							wpTotalTestcasesCount += workPackageDAO.getExecutionTCCountForJob(trj.getTestRunJobId());
							testSuiteName = count == 0 ? trj.getTestSuite().getTestSuiteName() : testSuiteName+","+trj.getTestSuite().getTestSuiteName();
							count++;
						}
					}
					jsonWPTCE.setTestsuiteName(testSuiteName);
					String executionMode = "N/A";
					String tpExecutionMode = "N/A";
					if(wp != null){
						if(wp.getTestExecutionMode() != null)
							executionMode = wp.getTestExecutionMode();	
						if(wp.getTestRunPlan() != null){
							jsonWPTCE.setTestPlanId(wp.getTestRunPlan().getTestRunPlanId());
							jsonWPTCE.setTestPlanName(wp.getTestRunPlan().getTestRunPlanName());
							//jsonWPTCE.setUseIntelligentTestPlan(wp.getTestRunPlan().getUseIntelligentTestPlan());
							if(wp.getTestRunPlan().getUseIntelligentTestPlan().equalsIgnoreCase("Yes"))
									jsonWPTCE.setUseIntelligentTestPlan("1");
							else
								jsonWPTCE.setUseIntelligentTestPlan("0");
						}
						if(wp.getTestRunPlan() != null && wp.getTestRunPlan().getAutomationMode() != null)
							tpExecutionMode = wp.getTestRunPlan().getAutomationMode();
					}
					//jsonWPTCE.setTotalWPTestCase(wp.getPlannedTestCasesCount());
					jsonWPTCE.setTotalWPTestCase(wpTotalTestcasesCount);
					Integer totalExecutedTestCasesCount = 0;
					totalExecutedTestCasesCount = jsonWPTCE.getP2totalPass() + jsonWPTCE.getP2totalFail();
					jsonWPTCE.setTotalExecutedTesCases(totalExecutedTestCasesCount);
					if(totalExecutedTestCasesCount > wpTotalTestcasesCount)
						jsonWPTCE.setTotalWPTestCase(totalExecutedTestCasesCount);
					Integer notExecutedCount = 0;
					if(jsonWPTCE != null && jsonWPTCE.getTotalWPTestCase() != null)
						notExecutedCount = jsonWPTCE.getTotalWPTestCase() - (jsonWPTCE.getP2totalPass() + jsonWPTCE.getP2totalFail());
					jsonWPTCE.setNotExecuted(notExecutedCount);
					jsonWPTCE.setP2totalNoRun(notExecutedCount);
					jsonWPTCE.setExecutionMode(executionMode);
					jsonWPTCE.setTestPlanExecutionMode(tpExecutionMode);
					
					//Fix for defect 4372 - If a job has no failed or passed and it has not executed then result should be failed
					//Special condition should be checked that the job should be aborted
					if(notExecutedCount > 0 && jsonWPTCE.getWpStatus() != null && 
							!(jsonWPTCE.getWpStatus().contains("New") || jsonWPTCE.getWpStatus().contains("Planning") || jsonWPTCE.getWpStatus().contains("Execution") || jsonWPTCE.getWpStatus().contains("Workpackage Planning")))
						jsonWPTCE.setResult("Failed");
					String firstActualExecutionDate = "NA";
					if(wp.getCreateDate() != null && wp.getCreateDate().toString().contains(" ")){
						String createdDate = wp.getCreateDate().toString();
						firstActualExecutionDate = createdDate.split(" ")[0];
					}
					jsonWPTCE.setFirstActualExecutionDate(firstActualExecutionDate);	
					if(wp.getProductBuild() != null && wp.getProductBuild().getProductMaster() != null )
					jsonWPTCE.setProductId(wp.getProductBuild().getProductMaster().getProductId());
					/*if(wp.getProductBuild() != null){
						jsonWPTCE.setProductBuildId(wp.getProductBuild().getProductBuildId());
						jsonWPTCE.setProductBuildName(wp.getProductBuild().getBuildname());
						if(wp.getProductBuild().getProductVersion() != null){
							jsonWPTCE.setProductVersionId(wp.getProductBuild().getProductVersion().getProductVersionListId());
							jsonWPTCE.setProductVersionName(wp.getProductBuild().getProductVersion().getProductVersionName());
							if(wp.getProductBuild().getProductVersion().getProductMaster() != null){
								jsonWPTCE.setProductId(wp.getProductBuild().getProductVersion().getProductMaster().getProductId());
							}else{
								jsonWPTCE.setProductId(0);
							}
						}else{
							jsonWPTCE.setProductVersionId(0);
							jsonWPTCE.setProductVersionName("");							
						}
					}else{
						jsonWPTCE.setProductBuildId(0);
						jsonWPTCE.setProductBuildName("");
					}*/
					jsonWPTCEPList.add(jsonWPTCE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in retrieving Workpackage execution summary " ,e);
		}
		return jsonWPTCEPList;
	}
	
	@Override
	@Transactional
	public List<JsonWorkPackageTestCaseExecutionPlanForTester> getWPTCExecutionSummaryByProdIdBuildIdWorkpackageId(Integer testFactoryId,
			Integer productId, Integer productBuildId,Integer workpackageId, Integer jtStartIndex,
			Integer jtPageSize  , String filter) {
		List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEPList = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
		try {
			List<WorkPackageTCEPSummaryDTO> wtpdto = workPackageDAO.getWPTCExecutionSummaryByProdIdAndBuildIdAndWorkpackageId(testFactoryId,productId,
							productBuildId,workpackageId, jtStartIndex, jtPageSize, filter);
			if (wtpdto != null && wtpdto.size() > 0) {
				for (WorkPackageTCEPSummaryDTO workPackageTCEPSummaryDTO : wtpdto) {
					WorkPackage wp = workPackageDAO.getWorkPackageById(workPackageTCEPSummaryDTO.getWorkPackageId());
					JsonWorkPackageTestCaseExecutionPlanForTester jsonWPTCE = new JsonWorkPackageTestCaseExecutionPlanForTester(workPackageTCEPSummaryDTO);
					Integer wpTotalTestcasesCount = 0;
					String testSuiteName = "N/A";
					int count = 0;
					if(wp.getTestRunJobSet() != null){
						for(TestRunJob trj : wp.getTestRunJobSet()){
							wpTotalTestcasesCount += workPackageDAO.getExecutionTCCountForJob(trj.getTestRunJobId());
							testSuiteName = count == 0 ? trj.getTestSuite().getTestSuiteName() : testSuiteName+","+trj.getTestSuite().getTestSuiteName();
							count++;
						}
					}
					jsonWPTCE.setTestsuiteName(testSuiteName);
					String executionMode = "N/A";
					String tpExecutionMode = "N/A";
					if(wp != null){
						if(wp.getTestExecutionMode() != null)
							executionMode = wp.getTestExecutionMode();	
						if(wp.getTestRunPlan() != null){
							jsonWPTCE.setTestPlanId(wp.getTestRunPlan().getTestRunPlanId());
							jsonWPTCE.setTestPlanName(wp.getTestRunPlan().getTestRunPlanName());
							//jsonWPTCE.setUseIntelligentTestPlan(wp.getTestRunPlan().getUseIntelligentTestPlan());
							if(wp.getTestRunPlan().getUseIntelligentTestPlan().equalsIgnoreCase("Yes"))
									jsonWPTCE.setUseIntelligentTestPlan("1");
							else
								jsonWPTCE.setUseIntelligentTestPlan("0");
						}
						if(wp.getTestRunPlan() != null && wp.getTestRunPlan().getAutomationMode() != null)
							tpExecutionMode = wp.getTestRunPlan().getAutomationMode();
					}
					//jsonWPTCE.setTotalWPTestCase(wp.getPlannedTestCasesCount());
					jsonWPTCE.setTotalWPTestCase(wpTotalTestcasesCount);
					Integer totalExecutedTestCasesCount = 0;
					totalExecutedTestCasesCount = jsonWPTCE.getP2totalPass() + jsonWPTCE.getP2totalFail();
					jsonWPTCE.setTotalExecutedTesCases(totalExecutedTestCasesCount);
					if(totalExecutedTestCasesCount > wpTotalTestcasesCount)
						jsonWPTCE.setTotalWPTestCase(totalExecutedTestCasesCount);
					Integer notExecutedCount = 0;
					if(jsonWPTCE != null && jsonWPTCE.getTotalWPTestCase() != null)
						notExecutedCount = jsonWPTCE.getTotalWPTestCase() - (jsonWPTCE.getP2totalPass() + jsonWPTCE.getP2totalFail());
					jsonWPTCE.setNotExecuted(notExecutedCount);
					jsonWPTCE.setP2totalNoRun(notExecutedCount);
					jsonWPTCE.setExecutionMode(executionMode);
					jsonWPTCE.setTestPlanExecutionMode(tpExecutionMode);
					
					//Fix for defect 4372 - If a job has no failed or passed and it has not executed then result should be failed
					//Special condition should be checked that the job should be aborted
					if(notExecutedCount > 0 && jsonWPTCE.getWpStatus() != null && 
							!(jsonWPTCE.getWpStatus().contains("New") || jsonWPTCE.getWpStatus().contains("Planning") || jsonWPTCE.getWpStatus().contains("Execution") || jsonWPTCE.getWpStatus().contains("Workpackage Planning")))
						jsonWPTCE.setResult("Failed");
					String firstActualExecutionDate = "NA";
					if(wp.getCreateDate() != null && wp.getCreateDate().toString().contains(" ")){
						String createdDate = wp.getCreateDate().toString();
						firstActualExecutionDate = createdDate.split(" ")[0];
					}
					jsonWPTCE.setFirstActualExecutionDate(firstActualExecutionDate);	
					if(wp.getProductBuild() != null && wp.getProductBuild().getProductMaster() != null )
					jsonWPTCE.setProductId(wp.getProductBuild().getProductMaster().getProductId());
					/*if(wp.getProductBuild() != null){
						jsonWPTCE.setProductBuildId(wp.getProductBuild().getProductBuildId());
						jsonWPTCE.setProductBuildName(wp.getProductBuild().getBuildname());
						if(wp.getProductBuild().getProductVersion() != null){
							jsonWPTCE.setProductVersionId(wp.getProductBuild().getProductVersion().getProductVersionListId());
							jsonWPTCE.setProductVersionName(wp.getProductBuild().getProductVersion().getProductVersionName());
							if(wp.getProductBuild().getProductVersion().getProductMaster() != null){
								jsonWPTCE.setProductId(wp.getProductBuild().getProductVersion().getProductMaster().getProductId());
							}else{
								jsonWPTCE.setProductId(0);
							}
						}else{
							jsonWPTCE.setProductVersionId(0);
							jsonWPTCE.setProductVersionName("");							
						}
					}else{
						jsonWPTCE.setProductBuildId(0);
						jsonWPTCE.setProductBuildName("");
					}*/
					jsonWPTCEPList.add(jsonWPTCE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in retrieving Workpackage execution summary " ,e);
		}
		return jsonWPTCEPList;
	}

	private String convertJobStatusFromIntToString(Integer jobStatus) {

		String jobStatusString = null;
		switch (jobStatus) {
		case 1:
			jobStatusString = IDPAConstants.JOB_STATUS_NEW;
			break;
		case 2:
			jobStatusString = IDPAConstants.JOB_STATUS_NEW;
			break;
		case 3:
			jobStatusString = IDPAConstants.JOB_STATUS_EXECUTING;
			break;
		case 4:
			jobStatusString = IDPAConstants.JOB_STATUS_QUEUED;
			break;
		case 5:
			jobStatusString = IDPAConstants.JOB_STATUS_COMPLETED;
			break;
		case 6:
			jobStatusString = IDPAConstants.JOB_STATUS_ABORT;
			break;
		case 7:
			jobStatusString = IDPAConstants.JOB_STATUS_ABORTED;
			break;
		default:
			jobStatusString = "";
			break;
		}

		return jobStatusString;
	}

	private String convertWorkPackageStatusFromIntToString(Integer wpStatus) {

		String wpStatusString = null;
		switch (wpStatus) {
		case 1:
			wpStatusString = IDPAConstants.WORKPACKAGE_STATUS_NEW;
			break;
		case 2:
			wpStatusString = IDPAConstants.WORKPACKAGE_STATUS_PLANNING;
			break;
		case 3:
			wpStatusString = IDPAConstants.WORKPACKAGE_STATUS_EXECUTING;
			break;
		case 4:
			wpStatusString = IDPAConstants.WORKPACKAGE_STATUS_COMPLETED;
			break;
		case 5:
			wpStatusString = IDPAConstants.WORKPACKAGE_STATUS_CLOSED;
			break;
		case 6:
			wpStatusString = IDPAConstants.WORKPACKAGE_STATUS_ABORTED;
			break;
		case 7:
			wpStatusString = IDPAConstants.WORKPACKAGE_STATUS_ABORTED;
			break;
		default:
			wpStatusString = "";
			break;
		}

		return wpStatusString;
	}

	@Override
	@Transactional
	public List<TestCaseList> listTestCaseOfTRJob(int testRunJobId) {
		return workPackageDAO.listTestCaseOfTRJob(testRunJobId);
	}

	@Override
	@Transactional
	public List<TestCaseDTO> listTestCaseExecutionDetailsOfTRJob(int testRunJobId, int workPackageId) 
	{
		TestRunJob trj = null;
		WorkPackage wp = null;
		ProductMaster productMaster = null;
		List<TestCaseDTO> testCaseDTOlist = new ArrayList<TestCaseDTO>();

		try {
			Set<WorkPackageTestCaseExecutionPlan> wptcepSet = null ;
			if(workPackageId == 0){
				trj = workPackageDAO.listTestCaseExecutionDetailsOfTRJob(testRunJobId);
				if (trj.getWorkPackageTestCaseExecutionPlans() != null	&& trj.getWorkPackageTestCaseExecutionPlans().size() > 0) 
				{
					wptcepSet = new HashSet<WorkPackageTestCaseExecutionPlan>();
					wptcepSet = trj.getWorkPackageTestCaseExecutionPlans();
				}
				if(trj.getTestRunPlan() != null && trj.getTestRunPlan().getProductVersionListMaster() != null)
				productMaster = trj.getTestRunPlan().getProductVersionListMaster().getProductMaster();
			}else if(testRunJobId == 0){
				wp = workPackageDAO.getWorkPackageById(workPackageId);
				wptcepSet = wp.getWorkPackageTestCaseExecutionPlan();
				if(wp.getProductBuild() != null && wp.getProductBuild().getProductVersion() != null)
					productMaster = wp.getProductBuild().getProductVersion().getProductMaster();
			}
			if(wptcepSet != null && wptcepSet.size() >0){
				TestCaseDTO tcdto = null;
				for (WorkPackageTestCaseExecutionPlan wptcepobj : wptcepSet) {
					tcdto = new TestCaseDTO();
					if (wptcepobj.getTestCase() != null) {
						TestCaseList tc = wptcepobj.getTestCase();
						if(testRunJobId == 0){
							testRunJobId = wptcepobj.getTestRunJob().getTestRunJobId();
						}
						tcdto.setTestRunJobId(testRunJobId);
						tcdto.setTestCaseId(tc.getTestCaseId());
						tcdto.setTestCaseName(tc.getTestCaseName());
						tcdto.setTestCaseCode(tc.getTestCaseCode());

						tcdto.setTestCaseCode(tc.getTestCaseCode());
						tcdto.setTestCaseCode(tc.getTestCaseCode());
						tcdto.setWptcepId(wptcepobj.getId());

						if (wptcepobj.getActualExecutionDate() != null) {
							tcdto.setActualExecutionDate(wptcepobj
									.getActualExecutionDate());
						}
						if (wptcepobj.getTester() != null) {
							tcdto.setTesterName(wptcepobj.getTester()
									.getLoginId());
						}else if(wptcepobj.getWorkPackage()!= null && wptcepobj.getWorkPackage().getWorkPackageType() != null && wptcepobj.getWorkPackage().getWorkPackageType().getExecutionTypeId() == 7 ){
							//Automated, hence Owner of WP is the executer of TC
							if( wptcepobj.getWorkPackage().getUserList() != null){
								tcdto.setTesterName(wptcepobj.getWorkPackage().getUserList().getLoginId());	
							}
						}	
						
						if (wptcepobj.getTestCaseExecutionResult() != null) {
							TestCaseExecutionResult tcer = wptcepobj
									.getTestCaseExecutionResult();
							
							if(tcer.getTestExecutionResultBugListSet() != null){
								int defct = tcer.getTestExecutionResultBugListSet().size();
								tcdto.setDefectsCount(new Long(defct));
							}
							if (tcer.getTestStepExecutionResultSet() != null) {
								tcdto.setTeststepcount(tcer.getTestStepExecutionResultSet().size());
							}
							if (tcer.getResult() != null) {
								tcdto.setResult(tcer.getResult());
							}
							if (tcer.getComments() != null) {
								tcdto.setTcerComments(tcer.getComments());
							}
							if (tcer.getStartTime() != null) {
								tcdto.setStartTime(tcer.getStartTime());
							}
							if (tcer.getEndTime() != null) {
								tcdto.setEndTime(tcer.getEndTime());
							}
							
							if(tcer.getAnalysisRemarks() != null && !tcer.getAnalysisRemarks().isEmpty()) {
								tcdto.setAnalysisRemarks(tcer.getAnalysisRemarks());
							}
							
							if(tcer.getAssignee() != null) {
								tcdto.setAssigneeId(tcer.getAssignee().getUserId());
								tcdto.setAssigneeName(tcer.getAssignee().getLoginId());
							}
							
							if(tcer.getReviewer() != null) {
								tcdto.setReviewerId(tcer.getReviewer().getUserId());
								tcdto.setReviewerName(tcer.getReviewer().getLoginId());
							}
							
							if(tcer.getWorkflowStatus() != null) {
								tcdto.setWorkflowStatusId(tcer.getWorkflowStatus().getWorkflowStatusId());
								tcdto.setWorkflowStatusName(tcer.getWorkflowStatus().getWorkflowStatusName());
							}
							
							if(tcer.getAnalysisOutCome() != null) {
								tcdto.setAnalysisOutCome(tcer.getAnalysisOutCome());
							}
						}
						if(trj != null){
							tcdto.setTestRunJobId(trj.getTestRunJobId());
							if (trj.getRunConfiguration().getRunconfigName() != null) {
								tcdto.setRunConfigurationName(trj
										.getRunConfiguration()
										.getRunconfigName());
							}
						}else{
							tcdto.setTestRunJobId(wptcepobj.getTestRunJob().getTestRunJobId());
							if (wptcepobj.getTestRunJob().getRunConfiguration().getRunconfigName() != null) {
								tcdto.setRunConfigurationName(wptcepobj.getTestRunJob()
										.getRunConfiguration()
										.getRunconfigName());
							}
						}
						tcdto.setProductMaster(productMaster);
						testCaseDTOlist.add(tcdto);
						Collections.sort(testCaseDTOlist, new Comparator<TestCaseDTO>(){
							@Override
						    public int compare(TestCaseDTO tc1, TestCaseDTO tc2) {
						        if(tc1.getTestCaseId() < tc2.getTestCaseId()){
						            return 1;
						        } else {
						            return -1;
						        }
						    }
						});
						tcdto = null;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return testCaseDTOlist;
	}

	@Override
	@Transactional
	public List<TestStepExecutionResultDTO> listTestStepExecutionDetailsOfTCExecutionId(
			int wptcepId) {
		List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEPList = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
		List<WorkPackageTestCaseExecutionPlan> wpTCEPList = null;
		List<TestCaseList> trjTCList = null;
		TestRunJob trj = null;
		List<TestStepExecutionResultDTO> tStepExecutionResultDTOList = new ArrayList<TestStepExecutionResultDTO>();

		WorkPackageTestCaseExecutionPlan wptcepobj = workPackageDAO.listTestStepExecutionDetailsOfTCExecutionId(wptcepId);

		try {
			if (wptcepobj != null) {
				TestStepExecutionResultDTO tstepdto = null;				

						if (wptcepobj.getTestCaseExecutionResult() != null) {
							TestCaseExecutionResult tcer = wptcepobj
									.getTestCaseExecutionResult();
							if (tcer.getTestStepExecutionResultSet() != null
									&& tcer.getTestStepExecutionResultSet()
											.size() > 0) {
								Set<TestStepExecutionResult> tStepExeSet = tcer
										.getTestStepExecutionResultSet();
								for (TestStepExecutionResult tStepExeObj : tStepExeSet) {
									tstepdto = new TestStepExecutionResultDTO();
									tstepdto.setTestcaseId(wptcepobj
											.getTestCase().getTestCaseId());
									tstepdto.setTeststepexecutionresultId(tStepExeObj.getTeststepexecutionresultid());
									if (tStepExeObj.getTestSteps() != null) {
										tstepdto.setTestStepId(tStepExeObj
												.getTestSteps().getTestStepId());
										tstepdto.setTestStepName(tStepExeObj
												.getTestSteps()
												.getTestStepName());
										tstepdto.setTestStepCode(tStepExeObj
												.getTestSteps()
												.getTestStepCode());
										tstepdto.setTestStepDescription(tStepExeObj
												.getTestSteps()
												.getTestStepDescription());
										tstepdto.setTestStepExpectedOutput(tStepExeObj
												.getTestSteps()
												.getTestStepExpectedOutput());
									}
									tstepdto.setObservedOutput(tStepExeObj
											.getObservedOutput());
									tstepdto.setResult(tStepExeObj.getResult());
									if (tStepExeObj.getTestStepStarttime() != null)
										tstepdto.setTestStepStarttime(tStepExeObj
												.getTestStepStarttime());

									if (tStepExeObj.getTestStepEndtime() != null)
										tstepdto.setTestStepEndtime(tStepExeObj
												.getTestStepEndtime());

									tstepdto.setComments(tStepExeObj
											.getComments());
									tStepExecutionResultDTOList.add(tstepdto);
									Collections.sort(tStepExecutionResultDTOList, new Comparator<TestStepExecutionResultDTO>(){
										@Override
									    public int compare(TestStepExecutionResultDTO ts1, TestStepExecutionResultDTO ts2) {
									        if(ts1.getTestStepId() < ts2.getTestStepId()){
									            return 1;
									        } else {
									            return -1;
									        }
									    }
									});
									tstepdto = null;
								}
							}
						}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tStepExecutionResultDTOList;
	}

	@Override
	@Transactional
	public String getDeviceNameByTestRunJob(int testRunJobId) {
	
		return workPackageDAO.getDeviceNameByTestRunJob(testRunJobId);
	}

	@Override
	@Transactional
	public void updateWPStatus(TestRunJob testRunJob,Integer wpId, Integer workflowStageIdExecution, Integer userId, Boolean isNextTPAvailable) {
		log.info("Updating WorkPackageStatus based on Job Status");
		UserList user;
		try {
			Integer workFlowStageValue = 0;
			String remarks = "";
			Integer testRunPlanId = 0;
			Integer workpackageId = 0;
			Integer totalJobsofWP = 0;
			Integer totalJobsOfWPNew = 0;
			Integer totalJobsOfWPExecuting = 0;
			Integer totalJobsOfWPQueued = 0;
			Integer totalJobsOfWPCompleted = 0;
			Integer totalJobsOfWPAborted = 0;
			Integer totalJobsOfWPRestarted = 0;
			ArrayList<Integer> jobStatusList;
			if(testRunJob != null){
				testRunPlanId = testRunJob.getTestRunPlan().getTestRunPlanId();
				if(testRunPlanId == 0){
					if(testRunJob.getWorkPackage().getTestRunPlan() != null){
						testRunPlanId = testRunJob.getWorkPackage().getTestRunPlan().getTestRunPlanId();	
					}else{
						TestRunJob trj = environmentDAO.getTestRunJob(testRunJob.getTestRunJobId());
						testRunPlanId = trj.getTestRunPlan().getTestRunPlanId();
					}					
				}
				workpackageId = testRunJob.getWorkPackage().getWorkPackageId();
				totalJobsofWP = testRunJob.getWorkPackage().getTestRunJobSet().size();
				
				totalJobsOfWPNew = environmentDAO.getTotalJobsOfWPByStatus(workpackageId, testRunPlanId, IDPAConstants.JOB_NEW, null);
				totalJobsOfWPQueued = environmentDAO.getTotalJobsOfWPByStatus(workpackageId, testRunPlanId, IDPAConstants.JOB_QUEUED, null);
				totalJobsOfWPExecuting = environmentDAO.getTotalJobsOfWPByStatus(workpackageId, testRunPlanId, IDPAConstants.JOB_EXECUTING, null);
				totalJobsOfWPCompleted = environmentDAO.getTotalJobsOfWPByStatus(workpackageId, testRunPlanId, IDPAConstants.JOB_COMPLETED, null);
				totalJobsOfWPAborted = environmentDAO.getTotalJobsOfWPByStatus(workpackageId, testRunPlanId, IDPAConstants.JOB_ABORTED, null);
				totalJobsOfWPRestarted = environmentDAO.getTotalJobsOfWPByStatus(workpackageId, testRunPlanId, IDPAConstants.JOB_RESTART, null);
				log.info("Total : New : Queued : Executing : Completed : Aborted Jobs are " + totalJobsofWP + " : " + totalJobsOfWPNew +  " : " + totalJobsOfWPQueued + " : " + totalJobsOfWPExecuting + " : " + totalJobsOfWPCompleted + " : " + totalJobsOfWPCompleted);
				WorkPackage workPackage = workPackageDAO.getWorkPackageById(wpId);
				//Check if there are any executing Jobs
				if(totalJobsOfWPExecuting != null && totalJobsOfWPExecuting != 0 && totalJobsOfWPExecuting>0){
					workFlowStageValue = IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION;
					remarks = "Executing WorkPackage ";		
					workPackage.setResult("In Progress");
					log.info("Check if there are any executing Jobs");
				} else if(totalJobsOfWPQueued != null && totalJobsOfWPQueued != 0 && totalJobsOfWPQueued>0){
					//There are no executing Jobs
					//Queued Jobs are present in the Workpackage
					//Do Nothing, as Jobs are still Pending
				}else if(totalJobsOfWPAborted != null && totalJobsOfWPAborted != 0 && totalJobsOfWPAborted>0){
					//There are no executing or queued JObs in the WP
					//Check for aborted Jobs
					log.info("Check for aborted Jobs");
					if(totalJobsofWP == totalJobsOfWPAborted){
						//All the Jobs in the workpackage have been aborted.
						//Mark the Workpackage as aborted
						workFlowStageValue = IDPAConstants.WORKFLOW_STAGE_ID_ABORTED;
						remarks = "Aborted WorkPackage ";
						log.info("All the Jobs in the workpackage have been aborted");
						//Added for combined evidence pushing
						if(testRunJob.getWorkPackage().getTestRunPlan().getResultsReportingMode() != null)
							log.info("Is it Combined Job Feature : "+testRunJob.getWorkPackage().getTestRunPlan().getResultsReportingMode());
						if(testRunJob.getWorkPackage().getTestRunPlan().getResultsReportingMode() != null && testRunJob.getWorkPackage().getTestRunPlan().getResultsReportingMode().equalsIgnoreCase("COMBINED JOB"))
							copyCombinedJobsEvidencePack(testRunJob.getWorkPackage());
						//Set the actual end date when the workpackage execution aborts
						workPackage.setActualEndDate(new Date());
						workPackage.setResult("FAILED");
					}else if(totalJobsofWP == (totalJobsOfWPCompleted + totalJobsOfWPAborted)){					
						//Only a subset of the Jobs are aborted.
						//This means that all the Jobs in the workpackage are completed
						workFlowStageValue = IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED;
						remarks = "Completed WorkPackage execution."+System.lineSeparator();
						log.info("Only a subset of the Jobs are aborted");
						//Added for combined evidence pushing
						if(testRunJob.getWorkPackage().getTestRunPlan().getResultsReportingMode() != null)
							log.info("Is it Combined Job Feature : "+testRunJob.getWorkPackage().getTestRunPlan().getResultsReportingMode());
						if(testRunJob.getWorkPackage().getTestRunPlan().getResultsReportingMode() != null && testRunJob.getWorkPackage().getTestRunPlan().getResultsReportingMode().equalsIgnoreCase("COMBINED JOB"))
							copyCombinedJobsEvidencePack(testRunJob.getWorkPackage());
						//Set the actual end date when the workpackage execution completes/aborts
						workPackage.setActualEndDate(new Date());
						workPackage.setResult("FAILED");
					} else {
						workFlowStageValue = IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION;
						workPackage.setResult("In Progress");
						remarks = "Executing Workpackage"+System.lineSeparator();
					}
				}else if(totalJobsOfWPCompleted != null && totalJobsOfWPCompleted != 0 && (totalJobsofWP == totalJobsOfWPCompleted)){					
						workFlowStageValue = IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED;
						remarks = "Completed WorkPackage execution."+System.lineSeparator();	
						//Set the actual end date when the workpackage execution completes
						String result = "PASSED";
						if(workPackage.getTestRunJobSet() != null){
							for(TestRunJob trj : workPackage.getTestRunJobSet()){
								if(trj.getResult() != null && trj.getResult().equalsIgnoreCase("FAILED")){
									result = trj.getResult();
									break;
								}
							}
						}
						workPackage.setResult(result);
						workPackage.setActualEndDate(new Date());
						log.info("Completed WorkPackage execution");
				}else if(totalJobsOfWPRestarted != null && totalJobsOfWPRestarted != 0 && totalJobsOfWPRestarted > 0){					
					//This means that all the Jobs or a subset of Jobs in the workpackage are Restarted
					workFlowStageValue = IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION;
					remarks = "WorkPackage execution with a Restart Job."+System.lineSeparator();
					log.info("One or more Jobs are Restarted");
				}
				
				WorkFlow workFlow=workPackageDAO.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, workFlowStageValue);
				WorkFlowEvent workFlowEvent ;
				workFlowEvent = new WorkFlowEvent();
				workFlowEvent.setEventDate(DateUtility.getCurrentTime());
				if(userId != null && userId ==0){
					user = new UserList();
					user= userListDAO.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
				}else{
					user = new UserList();
					user= userListDAO.getByUserId(userId);
				}		
				workFlowEvent.setEventDate(DateUtility.getCurrentTime());		
				workFlowEvent.setUser(user);
				workFlowEvent.setWorkFlow(workFlow);
				if(remarks != "")
					workFlowEvent.setRemarks(remarks);
				if(remarks != "")
					workFlowEvent.setEntityTypeRefId(wpId);
				Integer workFlowEvntId = workPackageDAO.addWorkFlowevent(workFlowEvent);			
				workFlowEvent.setWorkfloweventId(workFlowEvntId);
				if(remarks != "")
				workFlowEvent.setEntityTypeRefId(workPackage.getWorkPackageId());
				workPackageDAO.updateWorkFlowEvent(workFlowEvent);
				if(remarks != "")
				workFlowEvent.setEntityTypeRefId(workPackage.getWorkPackageId());
				workPackage.setWorkFlowEvent(workFlowEvent);
				if(!isNextTPAvailable && workPackage.getSourceType().equalsIgnoreCase(IDPAConstants.WORKPACKAGE_SOURCE_TESTRUNPLANGROUP)){
					TestCycle testCycle = workPackage.getTestCycle();
					testCycle.setTestCycleStatus(workFlow.getStageName());
					testCycle.setTestRunPlanGroup(workPackage.getTestRunPlanGroup());
					testCycle.setEndTime(DateUtility.getCurrentTime());
					String testCycleResult = "FAILED";
					int count = 0;
					boolean isExecutingWPAvailable = false;
					for(WorkPackage wp : testCycle.getWorkPackageList()){
						if(wp.getResult().equalsIgnoreCase("PASSED")){
							testCycleResult = "PASSED";
							count++;
						}else if(wp.getResult().equalsIgnoreCase("In Progress")){
							isExecutingWPAvailable = true;
							break;
						}
					}
					if(isExecutingWPAvailable){
						testCycleResult = "In Progress";
					}else{
						if(count == testCycle.getWorkPackageList().size()){
							testCycleResult = "PASSED";
						}else{
							testCycleResult = "FAILED";
						}
					}
					testCycle.setResult(testCycleResult);
					workPackage.setTestCycle(testCycle);
				}
				workPackageDAO.updateWorkPackage(workPackage);	
			}
		} catch (Exception e) {
			log.error("Error setting WP Status based on TestRunJob status", e);
		}
	}
	
	
	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> getNotExecutedTestCaseListByWpId(Integer wpid){
		return workPackageDAO.getNotExecutedTestCaseListByWpId(wpid);
			}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> getNotExecutedTestCaseListByJobId(Integer jobid) {
		return workPackageDAO.getNotExecutedTestCaseListByJobId(jobid);
	}
	
	@Override
	@Transactional
	public void updateTestRunJob(TestRunJob testRunJob) {
		workPackageDAO.addTestRunJob(testRunJob);
	}

	@Override
	@Transactional
	public Integer getWPTCEPCountOfWPId(Integer testCaseId,
			Integer workPackageId) {
		
		return workPackageDAO. getWPTCEPCountOfWPId(testCaseId, workPackageId);
	}

	@Override
	@Transactional
	public List<JsonWorkPackageDemandProjection> listWorkPackageDemandProjectionByWorkpackageWeekAndYear(int workPackageId, int shiftId, Set<Integer> recursiveWeeks, Integer workYear,Integer skillId,Integer roleId,Integer userTypeId) {
		
		List<WorkPackageDemandProjection> workPackageDemandProjectionList = workPackageDAO.listWorkPackageDemandProjectionByWeekAndYear(workPackageId, shiftId, recursiveWeeks,workYear,skillId,roleId,userTypeId);
	
		
		List<JsonWorkPackageDemandProjection> jsonWorkPackageDemandProjections = new ArrayList<JsonWorkPackageDemandProjection>();
		JsonWorkPackageDemandProjection jsonWorkPackageDemandProjection = null;
		if (workPackageDemandProjectionList != null&& workPackageDemandProjectionList.size() > 0) {
			for (WorkPackageDemandProjection demandProjection : workPackageDemandProjectionList) {
				jsonWorkPackageDemandProjection = new JsonWorkPackageDemandProjection(demandProjection);
				jsonWorkPackageDemandProjections.add(jsonWorkPackageDemandProjection);
			}
			log.debug("jsonWorkPackageDemandProjections size "+ jsonWorkPackageDemandProjections.size());
		} else {
			return null;
		}
		return jsonWorkPackageDemandProjections;
	}

	
	@Override
	@Transactional
	public List<JsonTestFactoryResourceReservation> listWorkPackageResourceReservationByWorkpackageWeekAndYear(Integer workPackageId, Integer shiftId, Integer reservationWeek,Integer reservationYear,Integer resourceId) {
		
		List<TestFactoryResourceReservation> testFactoryResourceReservationList = workPackageDAO.listWorkPackageResourceReservationByWorkpackageWeekAndYear(workPackageId, shiftId,reservationWeek,reservationYear,resourceId);
	
		
		List<JsonTestFactoryResourceReservation> jsonTestFactoryResourceReservations = new ArrayList<JsonTestFactoryResourceReservation>();
		JsonTestFactoryResourceReservation jsonTestFactoryResourceReservation = null;
		
		if (testFactoryResourceReservationList != null&& testFactoryResourceReservationList.size() > 0) {
			
			for (TestFactoryResourceReservation resourceReserved : testFactoryResourceReservationList) 
			{
				jsonTestFactoryResourceReservation = new JsonTestFactoryResourceReservation(resourceReserved);
				jsonTestFactoryResourceReservations.add(jsonTestFactoryResourceReservation);
			}
			log.debug("jsonWorkPackageDemandProjections size "+ jsonTestFactoryResourceReservations.size());
		} else {
			return null;
		}
		return jsonTestFactoryResourceReservations;
	}
		
	@Override
	@Transactional
	public List<WorkPackageDemandProjection> getWorkPackageDemandProjectionByGroupDemandId(Long groupDemandId) {
		return workPackageDAO. getWorkPackageDemandProjectionByGroupDemandId(groupDemandId);
	}

	@Override
	@Transactional
	public void addTestfactoryResourceReservationWeekly(TestFactoryResourceReservation testFactoryResourceReservation) {
		workPackageDAO.addTestfactoryResourceReservationWeekly(testFactoryResourceReservation);
		
	}

	@Override
	@Transactional
	public void deleteWorkPackageDemandProjection(WorkPackageDemandProjection workPackageDemandProjection) {
		workPackageDAO.deleteWorkPackageDemandProjection(workPackageDemandProjection);
	}

	@Override
	@Transactional
	public Integer countAllResourceDemands(Date startDate, Date endDate) {
		return workPackageDAO.countAllResourceDemands(startDate, endDate);
	}

	@Override
	@Transactional
	public List<WorkPackageDemandProjection> listAllResourceDemands(int startIndex,int pageSize, Date startDate, Date endDate) {
		return workPackageDAO.listAllResourceDemands(startIndex,pageSize,startDate, endDate);
	}

	@Override
	@Transactional
	public TestFactoryResourceReservation getTestFactoryResourceReservationById(Integer reservationId) {
		return workPackageDAO.getTestFactoryResourceReservationById(reservationId);
	}

	@Override
	@Transactional
	public Integer countAllReservedResources(Date startDate, Date endDate) {
		return workPackageDAO.countAllReservedResources(startDate, endDate);
	}

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> listAllReservedResources(int startIndex,int pageSize, Date startDate, Date endDate) {
		return workPackageDAO.listAllReservedResources(startIndex,pageSize,startDate, endDate);
		}

	@Override
	@Transactional
	public WorkPackage getLatestWorkPackageByproductId(Integer productId,String wpName) {
		return workPackageDAO.getLatestWorkPackageByproductId(productId,wpName);
	}	

	@Override
	@Transactional
	public void createWorkpackageExecutionPlanForExistingWorkPackage(WorkPackage workPackage, TestRunPlan testRunPlan, HttpServletRequest req, String deviceNames, String testcaseNames) {
		workPackageDAO.createWorkpackageExecutionPlanForExistingWorkPackage(workPackage, testRunPlan, req, deviceNames, testcaseNames);
	}
	
	@Override
	@Transactional
	public List<JsonWorkPackageTestCaseExecutionPlanForTester> getWPTCExecutionSummaryByTestRunPlanId(Integer testRunPlanId , Integer jtStartIndex, Integer jtPageSize) {
		List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEPList = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
		List<WorkPackageTCEPSummaryDTO> wtpdto = new LinkedList<WorkPackageTCEPSummaryDTO>();
		try {
			wtpdto = workPackageDAO.getWPTCExecutionSummaryByTestRunPlanId(testRunPlanId , jtStartIndex, jtPageSize);
			if (wtpdto != null && wtpdto.size() > 0) {
				for (WorkPackageTCEPSummaryDTO workPackageTCEPSummaryDTO : wtpdto) {
					WorkPackage wp = workPackageDAO.getWorkPackageById(workPackageTCEPSummaryDTO.getWorkPackageId());
					String executionMode = "N/A";
					String tpExecutionMode = "N/A";
					JsonWorkPackageTestCaseExecutionPlanForTester jsonWPTCE = new JsonWorkPackageTestCaseExecutionPlanForTester(workPackageTCEPSummaryDTO);
					if(wp != null){
						executionMode = wp.getTestExecutionMode();
						if(wp.getTestRunPlan() != null){
							tpExecutionMode = wp.getTestRunPlan().getAutomationMode();
							jsonWPTCE.setTestPlanName(wp.getTestRunPlan().getTestRunPlanName());
						}
					}
					Integer wpTotalTestcasesCount = 0;
					String testSuiteName = "N/A";
					int count = 0;
					if(wp.getTestRunJobSet() != null){
						for(TestRunJob trj : wp.getTestRunJobSet()){
							wpTotalTestcasesCount += workPackageDAO.getExecutionTCCountForJob(trj.getTestRunJobId());
							testSuiteName = count == 0 ? trj.getTestSuite().getTestSuiteName() : testSuiteName+","+trj.getTestSuite().getTestSuiteName();
							count++;
						}
					}
					jsonWPTCE.setTestsuiteName(testSuiteName);
					//jsonWPTCE.setTotalWPTestCase(wp.getPlannedTestCasesCount());
					jsonWPTCE.setTotalWPTestCase(wpTotalTestcasesCount);
					jsonWPTCE.setExecutionMode(executionMode);
					jsonWPTCE.setTestPlanExecutionMode(tpExecutionMode);
					jsonWPTCEPList.add(jsonWPTCE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonWPTCEPList;
	}

	@Override
	@Transactional
	public List<TestCaseList> getSelectedTestCasesFromTestRunJobTestSuite(int testRunListId, Integer testSuiteId) {
		return workPackageDAO.getSelectedTestCasesFromTestRunJobTestSuite(testRunListId, testSuiteId);
	}
	
	
	@Override
	@Transactional
	public List<ISERecommandedTestcases>  addRecommendationTestcasePlan(Integer productId,Integer buildId){		
		JSONObject finalObject = new JSONObject();
		List<TestCaseList> recommendedTestCasesList= new ArrayList<TestCaseList>();
		List<String> recommendedTestCases= new ArrayList<String>();
		JSONParser jsonParser = new JSONParser();		
		List<ISERecommandedTestcases> recommandedList=null;
		try {
			ProductMaster product=productMasterDAO.getByProductId(productId);
			finalObject.put("username","admin@hcl.com");
			//Provide the list of defects fixed in this build. For now, pass an empty list
			finalObject.put("defectIds","");
			//Provide the max no of test cases ISE should recommend. Setting it to 1000 for now
			//TODO : Make a configurable property of Test Run Plan
			finalObject.put("maxNoOfTestCases","1000");
			//Provide the list of builds from whose history to learn
			//In this case, provide all the builds of the product
			if(product != null) {
				finalObject.put("projectName",product.getProductName());
				ProductBuild build =productBuildDAO.getByProductBuildId(buildId, 0);
			
				JSONArray buildIds = new JSONArray();
				JSONObject buildObj = new JSONObject();
				if(build != null) {
					buildObj.put("relsname", build.getProductVersion().getProductVersionListId());
					buildObj.put("bldid",build.getProductBuildId());
					buildIds.add(buildObj);
					finalObject.put("buildIds", buildIds);
					List<ProductFeature> featuresList = productListService.getFeatureListByProductIdAndVersionIdAndBuild(productId,build.getProductVersion().getProductVersionListId(),buildId,1, 0, 10000); //This will be from the newly implemented Build - Feature mapping
					JSONObject features = new JSONObject();
					if(featuresList != null && featuresList.size() >0) {
						JSONArray featureIds = new JSONArray();
						for(ProductFeature feature:featuresList) {
							JSONObject featureObj = new JSONObject();
							featureObj.put("featurename", feature.getProductFeatureName());
							if(feature.getExecutionPriority() != null && !feature.getExecutionPriority().getPriorityName().isEmpty()){
								
								if(feature.getExecutionPriority().getPriorityName().equalsIgnoreCase("Critical") || feature.getExecutionPriority().getPriorityName().equalsIgnoreCase("High")){
									featureObj.put("priority", "HIGH");
								} else if(feature.getExecutionPriority().getPriorityName().equalsIgnoreCase("Medium") || feature.getExecutionPriority().getPriorityName().equalsIgnoreCase("Trivial")){
									featureObj.put("priority", "LOW");
								}
							} else {
								featureObj.put("priority", "HIGH");
							}
							featureIds.add(featureObj);
						}
						finalObject.put("features", featureIds);
					}
					
					//Get only the environment combinations in the Test Run Plan and pass to ISE.
					Set<EnvironmentCombination>  environmentCombinationList = new HashSet<EnvironmentCombination>();
					List<RunConfiguration> rcList= environmentDAO.getRunConfigurationByProductVersion(build.getProductVersion().getProductVersionListId()) ;
					if(rcList != null && !rcList.isEmpty()){
						for(RunConfiguration rc : rcList){
							if(rc.getEnvironmentcombination() != null && rc.getEnvironmentcombination().getEnvionmentCombinationStatus().equals(1)){
								environmentCombinationList.add(rc.getEnvironmentcombination());
							}
						}
					}
					
					
					
					if(environmentCombinationList != null && environmentCombinationList.size() >0) {
						Double distrbt=Double.valueOf(100/environmentCombinationList.size());
						JSONArray testbeds = new JSONArray();
						for(EnvironmentCombination envCombination:environmentCombinationList) {
							JSONObject environObj = new JSONObject();
							environObj.put("envname", envCombination.getEnvironmentCombinationName());
							environObj.put("distrbt",distrbt);
							testbeds.add(environObj);
						}
						finalObject.put("testbeds", testbeds);
					}
					
				}
			}

			log.info("Get Test plan input :"+finalObject);
			String responseData= ISEServerAccesUtility.GetISERestServiceCall(iseServerURL,finalObject.toString(),iseRegressionOptimizationServiceName);			
			log.info("Final Result"+responseData);			
			recommandedList= generateJsonOutput(responseData,buildId);
			
			if(recommandedList != null && !recommandedList.isEmpty()){
				//Remove duplicate test case recommendations from ISE Analytical part
				Set<ISERecommandedTestcases> distinctRecommendedSet = new HashSet<ISERecommandedTestcases>();
				
				for(ISERecommandedTestcases iseRecommandedTestcase:recommandedList) {
					if(!validateDuplicateForISERecommandedTestCase(iseRecommandedTestcase.getTitle(),distinctRecommendedSet)) {
						distinctRecommendedSet.add(iseRecommandedTestcase);
					}
				}
				recommandedList = null;
				recommandedList = new ArrayList<ISERecommandedTestcases>();
				recommandedList.addAll(distinctRecommendedSet);
				log.info("After filtering duplicate records : "+ recommandedList.size());
			}
			
			if(recommandedList != null && recommandedList.size() >0) {
				iseRecommendedTestCaseDAO.removeISERecommendationTestcases(buildId);
				
				for(ISERecommandedTestcases recommendationTestcase:recommandedList) {
					recommendationTestcase.setPlanObtainTime(new Date());
					iseRecommendedTestCaseDAO.addISERecommendedTestCase(recommendationTestcase);
				}
			}
		} catch (Exception e) {
        	log.error("Problem after pushing  pushing testcase", e);
        }	
		return recommandedList;
	}

	@Override
	@Transactional
	public WorkPackage executeTestRunPlanWorkPackageUnattendedMode(TestRunPlan testRunPlan,Map<String,String> mapData,UserList user,HttpServletRequest req,TestRunPlanGroup testRunPlanGroup,WorkPackage workPackage,String deviceNames) {
		return workPackageDAO.executeTestRunPlanWorkPackageUnattendedMode(testRunPlan, mapData, user, req, testRunPlanGroup, workPackage, deviceNames);
	}

	@Override
	@Transactional
	public List<WorkFlowEvent> workFlowEventlist(int typeId) {
		return workPackageDAO.workFlowEventlist(typeId);
	}

	@Override
	@Transactional
	public String getExecutionTimeForEachWPByTPId(Integer testRunPlanId) {
	
		return workPackageDAO.getExecutionTimeForEachWPByTPId(testRunPlanId);
	}

	@Override
	@Transactional
	public Integer getWPTCExecutionSummaryCount(Integer testFactoryId,
			Integer productId, Integer productBuildId) {
		Integer wtpdto = 0;
		try {
			wtpdto = workPackageDAO.getWPTCExecutionSummaryCount(testFactoryId,productId, productBuildId, -1, -1);
		} catch(Exception e){
			return wtpdto;
		}
		return wtpdto;
	}
	
	@Override
	@Transactional
	public String getAnalyticsRecommendedTestcasesUnattended(Integer testRunPlanId, Integer testSuiteId, Map<String, String> mapData) {

		List<TestCaseList> testCaseList = null;
		List<TestCaseList> testCaseListTotal = null;
		List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
		List<ISERecommandedTestcases> recommendedTestCases = new LinkedList<ISERecommandedTestcases>();
		Set<ISERecommandedTestcases> recommendedTestCasesSet = new HashSet<ISERecommandedTestcases>();
		Map<String, Integer> iseRecommendedTC = new LinkedHashMap<String, Integer>();
		Integer totalTestRunPlanTestCasesCount = 0;
		Integer recommendedTestCasesCount = 0;			
		Double probCount = 0.00;
		StringBuffer recommendedtestcaseNamesSB = new StringBuffer();
		try {
			//Get the workpackage for the unattended test plan execution
			WorkPackage wp = getWorkPackageById(Integer.parseInt(mapData.get("wpId")));
			TestRunPlan trplan = productListService.getTestRunPlanById(testRunPlanId);

			//Get the total number of test run plan test cases count 
			totalTestRunPlanTestCasesCount = productListService.getTotalTestCaseCountForATestRunPlan(testRunPlanId);
			log.info("Test Plan is using analytics driven test plan : " + trplan.getUseIntelligentTestPlan());
			//For ISE recommended Test case obtain from ISE API
			if (trplan.getUseIntelligentTestPlan() != null && trplan.getUseIntelligentTestPlan().equalsIgnoreCase("Yes")) {
				log.info("Getting Test Plan for ISE");				
				//Make a call to ISE API to get the recommended test plan for this build					
				try {
					recommendedTestCases = getIntelligentTestPlanFromISE(trplan);
				} catch (Exception recommendedException) {
					log.error("Problem while invoking ISE ", recommendedException);
				}
				
				List<TestCaseList> testSuiteTestCase = new LinkedList<TestCaseList>();
				//Revised the logic to filter the testcases match with test suite
				Set<RunConfiguration> tcls = trplan.getRunConfigurationList();
				for(RunConfiguration rc : tcls){
					Set<TestSuiteList> tsls = rc.getTestSuiteLists();
					for(TestSuiteList ts : tsls){
						testSuiteTestCase.addAll(ts.getTestCaseLists());
						log.info("Test suite testcases for the test suite id : "+ts.getTestSuiteId() +" : size : "+testSuiteTestCase.size());
					}					
				}
				
				Set<TestCaseList> tccls = new LinkedHashSet<TestCaseList>(testSuiteTestCase);
				Set<String> nonDuplicateTestCaseNames = new LinkedHashSet<String>();
				for(TestCaseList tc : tccls){
					nonDuplicateTestCaseNames.add(tc.getTestCaseName());
					log.info("Test suite testcases for the test suite id : "+tc.getTestCaseName());
				}
				
				if(recommendedTestCases != null && !recommendedTestCases.isEmpty()){
					recommendedTestCasesSet.addAll(recommendedTestCases);
					recommendedTestCases = null;
					recommendedTestCases = new LinkedList<ISERecommandedTestcases>();
					recommendedTestCases.addAll(recommendedTestCasesSet);
					for(int i=0;i< recommendedTestCases.size();i++){
						//Create a comma separated list of recommended testcase names
						if(nonDuplicateTestCaseNames.contains(recommendedTestCases.get(i).getTitle())){
							log.info("Filtered TS TestCase content : "+recommendedTestCases.get(i).getTitle());
							if (i == 0)
								recommendedtestcaseNamesSB.append(recommendedTestCases.get(i).getTitle());
							else 
								recommendedtestcaseNamesSB.append("," + recommendedTestCases.get(i).getTitle());
							//Create a map of recommended testcase names with their ids
							iseRecommendedTC.put(recommendedTestCases.get(i).getTitle(), i);
						}						
					}
					log.info("Obtained recommended test cases from ISE : " + recommendedTestCases.size());				
					log.info("Recommended test case names : " + recommendedtestcaseNamesSB.toString());		
				}
			}		
			
			//Update Workpackage event for the displaying in UI
			try{
				WorkFlowEvent workFlowEvent = new WorkFlowEvent();
				workFlowEvent.setEventDate(DateUtility.getCurrentTime());
				if(recommendedTestCases != null && !recommendedTestCases.isEmpty()){
				   recommendedTestCasesCount = recommendedTestCases.size();
				} 
				ScriptLessExecutionDTO.setTestCasesExecutionCount(recommendedTestCasesCount);
				//This block is for updating Test Planning to the workpackage
				if(trplan.getUseIntelligentTestPlan().equalsIgnoreCase("Yes")){
					if (recommendedTestCasesCount > 0) {
						workFlowEvent.setRemarks("Performed analysis of build for test planning. " + System.lineSeparator() + recommendedTestCasesCount + " test cases recommended for execution for testing the build." + System.lineSeparator() +  "Updated Test Plan");
					} else {
						workFlowEvent.setRemarks("Performed analysis of build for test planning. " + System.lineSeparator() + "Insufficient historical data for meaningful analysis.");
					}
				} else {
					workFlowEvent.setRemarks("Skipping Test Plan analysis.");
				}
				workFlowEvent.setUser(null);
				workFlowEvent.setWorkFlow(getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_PLANNING));
				workFlowEvent.setEntityTypeRefId(wp.getWorkPackageId());
				addWorkFlowEvent(workFlowEvent);
				wp.setWorkFlowEvent(workFlowEvent);
				updateWorkPackage(wp);
				log.info("Obtained recommended test cases for unattended automation");
			} catch(Exception e){
				log.error("Error while getting recommended testcases for unattended automation");
			}
						
		} catch (Exception e) {
			log.error("JSON ERROR", e);
		}
		return recommendedtestcaseNamesSB.toString();
	}

	@Override
	@Transactional
	public Set<String> workFlowEvents(int typeId) {
		return workPackageDAO.workFlowEvents(typeId);
	}
	
	private boolean validateDuplicateForISERecommandedTestCase(String testCase,Set<ISERecommandedTestcases> iseRecommandedTestcases) {
		try {
			if(iseRecommandedTestcases != null && iseRecommandedTestcases.size() >0) {
				for(ISERecommandedTestcases iseRecommandedTestcase:iseRecommandedTestcases) {
					if(iseRecommandedTestcase.getTitle().equalsIgnoreCase(testCase) ) {
						return true;
					}
				}
			}
		}catch(Exception e) {
			
		}
		return false;
	}
	
	private void copyCombinedJobsEvidencePack(WorkPackage workPackage){
		try{
			String combinedJobEvidenceFolder = CommonUtility.getCatalinaPath()+ File.separator+ evidence_Folder + File.separator+ workPackage.getCombinedResultsReportingJob().getTestRunJobId() + File.separator + 
					IDPAConstants.EVIDENCE_SCREENSHOT+File.separator;
			log.info("Combined Job Evidence Path : " +combinedJobEvidenceFolder);
			//Added for Combined results test job and place all other test jobs screenshot folder contents into the combined job folder
            Set<TestRunJob> totalJobs = workPackage.getTestRunJobSet();
            log.info("For Combined Job Evidence placement : "+totalJobs.size() + " to the Test Plan >> "+workPackage.getTestRunPlan().getTestRunPlanId());
            if(totalJobs != null && !totalJobs.isEmpty()){
            	if(workPackage.getTestRunPlan().getResultsReportingMode() != null && workPackage.getTestRunPlan().getResultsReportingMode().equalsIgnoreCase("COMBINED JOB")){
            		log.info("Is it Combined Job behavior : YES");
            		for(TestRunJob testJob : totalJobs){
            			if(workPackage.getCombinedResultsReportingJob().getTestRunJobId() != testJob.getTestRunJobId()){
            				String sourcePath = CommonUtility.getCatalinaPath()+ File.separator+ evidence_Folder + File.separator+ testJob.getTestRunJobId() + File.separator + 
            						IDPAConstants.EVIDENCE_SCREENSHOT+File.separator;
            				Files.copy(Paths.get(sourcePath), Paths.get(combinedJobEvidenceFolder));
            				log.info("Evidence pack copied from Test Job : "+testJob.getTestRunJobId() + " to Test Job : "+workPackage.getCombinedResultsReportingJob().getTestRunJobId());
            			}
            		}
            	}            	
            }    
		} catch(Exception e) {
			log.error("Error in copying evidence packs to the combined job set", e);
		}
	}
	
	@Override
	@Transactional
	public Integer getProductBuildsTestedCount(Integer productBuildId) {
		return workPackageDAO.getProductBuildsTestedCount(productBuildId);
	}

	@Override
	@Transactional
	public Integer getExecutionTCCountForJob(Integer testRunJobId) {
		return workPackageDAO.getExecutionTCCountForJob(testRunJobId);
	}
	@Override
	@Transactional
	public WorkPackageBean getWorkPackageBeanById(Integer wpId) {
		WorkPackageBean workPackage = null;
		Integer executedTCcount = 0;
		Integer selectedTCcount = 0;
		Integer notExecutedTCcount = 0;
		String wpStatus = "";
		String wpResult = "Failed";
		boolean isWpStillRunning = false;
		boolean isTCPassed = true;
		try{
			int jobCount = 0;
			Integer totalTestCases = 0;
			Integer totalTestSteps = 0;
			Integer passedTestCases = 0;
			Integer failedTestCases = 0;
			Integer passedTestSteps = 0;


			//Obtain Workpackage for marshalling WorkpackageBean
			WorkPackage wpFromDb = workPackageDAO.getWorkPackageById(wpId);	 
			Set<TestRunJobBean> testRunJobSet = new HashSet<TestRunJobBean>();
			workPackage = new WorkPackageBean();
			workPackage.setWorkPackageId(wpFromDb.getWorkPackageId());
			workPackage.setName(wpFromDb.getName());
			workPackage.setDescription(wpFromDb.getDescription());
			workPackage.setExecutionType(wpFromDb.getWorkPackageType().getName());
			if(wpFromDb.getIsActive() != null && wpFromDb.getIsActive() == 1){
				workPackage.setIsActive("Yes");
			}else{
				workPackage.setIsActive("No");
			}
			if(wpFromDb.getUserList().getUserDisplayName() != null)
				workPackage.setCreatedBy(wpFromDb.getUserList().getUserDisplayName());
			//workPackage.setIterationNumber(wp.getIterationNumber());
			//workPackage.setLifeCyclePhase((wp.getLifeCyclePhase().getName()));
			/*if(wpFromDb.getStatus().equals(0)){
				workPackage.setStatus("");
			}*/
			if(wpFromDb.getWorkFlowEvent().getWorkfloweventId() != null){
				if(wpFromDb.getWorkFlowEvent().getWorkFlow().getWorkFlowId().equals(6)){
					wpStatus = "New";
					isWpStillRunning = true;
				}else if(wpFromDb.getWorkFlowEvent().getWorkFlow().getWorkFlowId().equals(7)){
					wpStatus = "Planning";
					isWpStillRunning = true;
				}else if(wpFromDb.getWorkFlowEvent().getWorkFlow().getWorkFlowId().equals(8)){
					wpStatus = "Execution";
					isWpStillRunning = true;
				}else if(wpFromDb.getWorkFlowEvent().getWorkFlow().getWorkFlowId().equals(9)){
					wpStatus = "Completed";
				}else if(wpFromDb.getWorkFlowEvent().getWorkFlow().getWorkFlowId().equals(10)){
					wpStatus = "Closed";
					isWpStillRunning = true;
				}else if(wpFromDb.getWorkFlowEvent().getWorkFlow().getWorkFlowId().equals(11)){
					wpStatus = "Abort";
					isWpStillRunning = true;
				}else if(wpFromDb.getWorkFlowEvent().getWorkFlow().getWorkFlowId().equals(23)){
					wpStatus = "Aborted";
					isWpStillRunning = true;
				}else if(wpFromDb.getWorkFlowEvent().getWorkFlow().getWorkFlowId().equals(24)){
					wpStatus = "Workpackage Planning";
					isWpStillRunning = true;
				}
				workPackage.setStatus(wpStatus);

			}

			if(isWpStillRunning ){
				wpResult = "Failed";
			}

			workPackage.setPlannedStartDate(wpFromDb.getPlannedStartDate());
			workPackage.setPlannedEndDate(wpFromDb.getPlannedEndDate());
			workPackage.setActualStartDate(wpFromDb.getActualStartDate());
			workPackage.setActualEndDate(wpFromDb.getActualEndDate());
			workPackage.setCreateDate(wpFromDb.getCreateDate());
			workPackage.setModifiedDate(wpFromDb.getModifiedDate());
			if(wpFromDb.getProductBuild().getProductMaster() != null && wpFromDb.getProductBuild().getProductMaster().getProductType() != null){
				//workPackage.setProductId(wpFromDb.getProductBuild().getProductMaster().getProductId());
				workPackage.setProductName(wpFromDb.getProductBuild().getProductMaster().getProductName());
				workPackage.setProductType(wpFromDb.getProductBuild().getProductMaster().getProductType().getDescription());
			}
			if(wpFromDb.getProductBuild() != null){
				//workPackage.setProductBuildId(wpFromDb.getProductBuild().getProductBuildId());
				workPackage.setProductBuild(wpFromDb.getProductBuild().getBuildname());
			}
			if(wpFromDb.getTestRunPlan() != null && wpFromDb.getTestRunPlan().getTestToolMaster() != null){
				workPackage.setTestPlanId(wpFromDb.getTestRunPlan().getTestRunPlanId());
				workPackage.setTestPlanName(wpFromDb.getTestRunPlan().getTestRunPlanName());
				workPackage.setTestTool(wpFromDb.getTestRunPlan().getTestToolMaster().getTestToolName());
				workPackage.setScriptType(wpFromDb.getTestRunPlan().getScriptTypeMaster().getScriptType());
			}
			//if(wp.getProductMaster().getProductVersionListMasters().stream().findFirst().get() != null){
			if(wpFromDb.getProductBuild().getProductVersion().getProductMaster().getProductVersionListMasters().stream().findFirst().get() != null){
				//ProductVersionListMaster productVersionListMaster = wp.getProductMaster().getProductVersionListMasters().stream().findFirst().get();
				ProductVersionListMaster productVersionListMaster = wpFromDb.getProductBuild().getProductVersion().getProductMaster().getProductVersionListMasters().stream().findFirst().get();
				if(productVersionListMaster.getProductVersionListId() != null){
					workPackage.setProdcutVersionId(productVersionListMaster.getProductVersionListId());
					workPackage.setProductVersion(productVersionListMaster.getProductVersionName());
				}
			}

			//setting test run duration to wp
			if(wpFromDb.getActualEndDate()!= null && wpFromDb.getActualStartDate() != null){
				long diff = wpFromDb.getActualEndDate().getTime() - wpFromDb.getActualStartDate().getTime();
				long diffSeconds = diff / 1000 % 60;
				long diffMinutes = diff / (60 * 1000) % 60;
				long diffHours = diff / (60 * 60 * 1000);
				workPackage.setDuration(diffHours+":"+diffMinutes+":"+diffSeconds);
			}
			if(wpFromDb.getTestRunJobSet() != null){
				for(TestRunJob trj : wpFromDb.getTestRunJobSet()){	    		
					TestRunJobBean testRunJob = new TestRunJobBean();
					GenericDevices genericDevices = trj.getGenericDevices();
					HostList hostList = trj.getHostList();
					if(trj.getGenericDevices() != null){
						testRunJob.setDeviceId(genericDevices.getUDID());
						testRunJob.setDeviceplatform(genericDevices.getPlatformType().getName());
					}
					if(hostList != null && hostList.getHostIpAddress() != null && hostList.getHostName() != null){
						testRunJob.setHostIpAddress(hostList.getHostIpAddress());
						testRunJob.setHostName(hostList.getHostName());
					}
					//EnvironmentCombinationBean envBean = new EnvironmentCombinationBean();
					testRunJob.setTestRunJobId(trj.getTestRunJobId());	
					//Environment Combination Bean setup
					EnvironmentCombination envComb = trj.getEnvironmentCombination();

					envComb.setEnvironment_combination_id(envComb.getEnvironment_combination_id());
					envComb.setEnvironmentCombinationName(envComb.getEnvironmentCombinationName());
					testRunJob.setEnvironment(envComb.getEnvironmentCombinationName());
					//testRunJob.setJobName(envComb.getEnvironmentCombinationName());
					//trjBean.setEnvironmentCombinationBean(envBean);

					if(trj.getTestRunStatus() != null && trj.getTestRunStatus().equals(5)){
						testRunJob.setStatus("COMPLETED");
					}else if(trj.getTestRunStatus() != null && trj.getTestRunStatus().equals(7)){
						testRunJob.setStatus("ABORTED");	
					}else{
						testRunJob.setStatus("EXECUTING");	
					}
					testRunJob.setStartTime(trj.getTestRunStartTime());
					testRunJob.setEndTime(trj.getTestRunEndTime());
					testRunJob.setComments(trj.getTestRunFailureMessage());
					if(trj.getTestToolMaster() != null)
						testRunJob.setTestToolName(trj.getTestToolMaster().getTestToolName());
					if(trj.getScriptTypeMaster() != null)
						testRunJob.setScriptType(trj.getScriptTypeMaster().getScriptType());
					//Test Suite Details
					//1. Get the selected test suites in the job
					// TestSuiteList ts = trj.getTestSuite();
					Set<TestSuiteList> testSuites = trj.getTestSuiteSet();
					Set<TestSuiteListBean> testSuite = new HashSet<TestSuiteListBean>();
					//2. Iterate over the test suite
					for (TestSuiteList ts : testSuites) {
						//Fill the test suite bean attributes
						TestSuiteListBean testSuiteBean = new TestSuiteListBean();
						testSuiteBean.setTestSuiteCode(ts.getTestSuiteCode());
						testSuiteBean.setTestSuiteId(ts.getTestSuiteId());
						testSuiteBean.setTestSuiteName(ts.getTestSuiteName());
						testSuiteBean.setDescription(ts.getDescription());

						//testRunJob.setTestSuite(testSuiteBean);
						//Get the test case executions for the test suite, and job

						List<WorkPackageTestCaseExecutionPlan> wpTestCaseExecutionPlans = workPackageDAO.listWorkPackageTestCaseExecutionPlanByJobAndSuiteId(trj.getTestRunJobId(),ts.getTestSuiteId());
						Set<TestCaseListBean> tclBeanList = new LinkedHashSet<TestCaseListBean>();
						Set<TestCaseStepsList> testCaseStepsLists = new LinkedHashSet<TestCaseStepsList>();
						Set<TestCaseStepsListBean> testCaseStepsListBeans = new LinkedHashSet<TestCaseStepsListBean>();
						Set<String> testcaseNamesList = new HashSet<String>(); 
						for(WorkPackageTestCaseExecutionPlan wpExecutionPlan: wpTestCaseExecutionPlans){
							TestCaseExecutionResult tcer = wpExecutionPlan.getTestCaseExecutionResult();
							TestCaseList tcl = wpExecutionPlan.getTestCase();
							log.info("TC Name : "+ tcl.getTestCaseName());
							if(testcaseNamesList.add(tcl.getTestCaseName())){
								log.info("TC Name Inside if condition: "+ tcl.getTestCaseName());
								//System.out.println(tcl.getTestCaseCode()+" "+tcl.getTestCaseId()+" "+tcl.getTestCaseName()+" "+tcl.getTestCaseDescription()+" "+tcer.getResult());
								TestCaseListBean tc = new TestCaseListBean();
								tc.setTestCaseCode(tcl.getTestCaseCode());
								tc.setTestCaseId(tcl.getTestCaseId());
								tc.setTestCaseName(tcl.getTestCaseName());
								tc.setTestCaseDescription(tcl.getTestCaseDescription());
								Set<ProductFeature> productFeatures = tcl.getProductFeature();
								Set<TestCaseScript> testCaseScripts = tcl.getTestCaseScripts();

								if(tcer.getEndTime() != null && tcer.getStartTime() != null){
									tc.setEndTime(tcer.getEndTime());
									tc.setStartTime(tcer.getStartTime());
								}
								StringBuffer sb = null;
								StringBuffer sb1 = null;;
								if(productFeatures != null){
									for(ProductFeature pf : productFeatures){
										sb.append(pf.getProductFeatureName()+",");
									}
								}
								if(sb != null)
									tc.setFeatureCovered(sb.toString());
								for(TestCaseScript tcs : testCaseScripts){
									sb1.append(tcs.getScriptName()+",");
								}
								if(sb1 != null)
									tc.setTestScriptsCovered(sb1.toString());
								tc.setStatus(tcer.getResult());
								if(!tcer.getResult().equalsIgnoreCase(null) && tcer.getResult().equalsIgnoreCase("PASSED")){
									passedTestCases ++;	

								}else if(tcer.getResult().equalsIgnoreCase("FAILED") && isTCPassed){
									wpResult = "Failed";
									isTCPassed = false;
									failedTestCases ++;
								}
								tc.setResult(tcer.getResult());
								testCaseStepsLists = tcl.getTestCaseStepsLists();
								for(TestCaseStepsList list: tcl.getTestCaseStepsLists()){
									log.info("inside test case step list Id :"+list.getTestStepId());
								}
								
								for(TestStepExecutionResult list: tcer.getTestStepExecutionResultSet()){
									log.info("inside tcer Id :"+list.getTestSteps().getTestStepId());
								}
								Set<TestStepExecutionResult> testStepExecutionResults = tcer.getTestStepExecutionResultSet();
								
								
								for(TestStepExecutionResult tstepExecutionResult : testStepExecutionResults){
									Integer testStepIdfromTser = tstepExecutionResult.getTestSteps().getTestStepId();
									log.info("testStepIdfromTser :"+testStepIdfromTser + "TSTSER :"+tstepExecutionResult.getTestSteps().getTestStepName()+"TC Name :"+tstepExecutionResult.getTestcase().getTestCaseName());
									for(TestCaseStepsList stepsList : testCaseStepsLists){
										log.info("testStepIdrfromlist :"+stepsList.getTestStepId() + "TSname :"+stepsList.getTestStepName()+" TC :"+stepsList.getTestCaseList().getTestCaseName());
										TestCaseStepsListBean tStepsListBean = new TestCaseStepsListBean();
										if(testStepIdfromTser.equals(stepsList.getTestStepId())){
											log.info("Coming into teststep testStepIdfromTser "+testStepIdfromTser +"stepsList Id "+stepsList.getTestStepId());
											//tStepsListBean.setTestStepCode(stepsList.getTestStepCode());
											tStepsListBean.setTestStepId(stepsList.getTestStepId());
											//tStepsListBean.setTestStepName(stepsList.getTestStepName());
											tStepsListBean.setTestStepDescription(stepsList.getTestStepDescription());
											tStepsListBean.setTestStepExpectedOutput(stepsList.getTestStepExpectedOutput());
											tStepsListBean.setObservedOutput(stepsList.getTestStepExpectedOutput());
											tStepsListBean.setTestStepStatus(tstepExecutionResult.getResult());
											tStepsListBean.setStartTime(tstepExecutionResult.getTestStepStarttime());
											tStepsListBean.setEndTime(tstepExecutionResult.getTestStepEndtime());
											//todo for combined jobs
											tStepsListBean.setEnvironment(tstepExecutionResult.getEnvironmentCombination().getEnvironmentCombinationName());
											if(!tstepExecutionResult.getResult().equalsIgnoreCase(null) && tstepExecutionResult.getResult().equalsIgnoreCase("PASSED")){
												passedTestSteps++;
											}else if (tstepExecutionResult.getResult().equalsIgnoreCase("FAILED")){

											}
											tStepsListBean.setFailureReason(tstepExecutionResult.getFailureReason());
											if(tstepExecutionResult.getEvidencePath() != null){
												tStepsListBean.setScreenshot(tstepExecutionResult.getEvidencePath());
											}else{
												tStepsListBean.setScreenshot("N/A");
											}
											if(tstepExecutionResult.getTestStepEndtime() != null && tstepExecutionResult.getTestStepStarttime() != null){
												long diff = tstepExecutionResult.getTestStepEndtime().getTime() - tstepExecutionResult.getTestStepStarttime().getTime();
												long diffSeconds = diff / 1000 % 60;
												long diffMinutes = diff / (60 * 1000) % 60;
												long diffHours = diff / (60 * 60 * 1000);
												tStepsListBean.setDuration(diffHours+":"+diffMinutes+":"+diffSeconds);
											}
											testCaseStepsListBeans.add(tStepsListBean);
											totalTestSteps++;
										}

									}
								}
								tc.setTestStep(testCaseStepsListBeans);
								tclBeanList.add(tc);
								totalTestCases++;
							}
						}
						testSuiteBean.setTotalTestCases(totalTestCases);
						testSuiteBean.setTotalTestSteps(totalTestSteps);
						testSuiteBean.setPassedTestCases(passedTestCases);
						testSuiteBean.setPassedTestSteps(passedTestSteps);
						testSuiteBean.setTestCase(tclBeanList);
						//testRunJob.setTestCaseList(tclBeanList);
						testSuite.add(testSuiteBean);

					}

					testRunJob.setTotalTestCases(totalTestCases);
					testRunJob.setTotalTestSteps(totalTestSteps);
					testRunJob.setPassedTestCases(passedTestCases);
					testRunJob.setPassedTestSteps(passedTestSteps);
					testRunJob.setTestSuite(testSuite);
					testRunJobSet.add(testRunJob);
					jobCount++;

					/*	TestSuiteListBean tslBean = new TestSuiteListBean();
					tslBean.setTestSuiteId(ts.getTestSuiteId());
					tslBean.setTestSuiteName(ts.getTestSuiteName());		    		
					testRunJob.setTestSuite(testSuiteBean);*/
					/*List<WorkPackageTestCaseExecutionPlan> wpTestCaseExecutionPlans = workPackageDAO.listWorkPackageTestCaseExecutionPlanByWorkPackageId(trj.getTestRunJobId());
					Set<TestCaseListBean> tclBeanList = new LinkedHashSet<TestCaseListBean>();
					Set<TestCaseStepsList> testCaseStepsLists = new LinkedHashSet<TestCaseStepsList>();
					Set<TestCaseStepsListBean> testCaseStepsListBeans = new LinkedHashSet<TestCaseStepsListBean>();
					for(WorkPackageTestCaseExecutionPlan wpExecutionPlan: wpTestCaseExecutionPlans){
						TestCaseExecutionResult tcer = wpExecutionPlan.getTestCaseExecutionResult();
						TestCaseList tcl = wpExecutionPlan.getTestCase();
						System.out.println(tcl.getTestCaseCode()+" "+tcl.getTestCaseId()+" "+tcl.getTestCaseName()+" "+tcl.getTestCaseDescription()+" "+tcer.getResult());
						TestCaseListBean tc = new TestCaseListBean();
						tc.setTestCaseCode(tcl.getTestCaseCode());
						tc.setTestCaseId(tcl.getTestCaseId());
						tc.setTestCaseName(tcl.getTestCaseName());
						tc.setTestCaseDescription(tcl.getTestCaseDescription());
						tc.setStatus(tcer.getResult());
						testCaseStepsLists = tcl.getTestCaseStepsLists();
						String testStepStartTime = "", testStepEndTime = "";
						Set<TestStepExecutionResult> testStepExecutionResults = wpExecutionPlan.getTestCaseExecutionResult().getTestStepExecutionResultSet();
						for(TestStepExecutionResult tstepExecutionResult:testStepExecutionResults){
							Integer testStepIdfromTser = tstepExecutionResult.getTestSteps().getTestStepId();
							for(TestCaseStepsList stepsList : testCaseStepsLists){
								TestCaseStepsListBean tStepsListBean = new TestCaseStepsListBean();
								if(testStepIdfromTser.equals(stepsList.getTestStepId())){
									//tStepsListBean.setTestStepCode(stepsList.getTestStepCode());
									tStepsListBean.setTestStepId(stepsList.getTestStepId());
									//tStepsListBean.setTestStepName(stepsList.getTestStepName());
									tStepsListBean.setTestStepDescription(stepsList.getTestStepDescription());
									tStepsListBean.setTestStepExpectedOutput(stepsList.getTestStepExpectedOutput());
									tStepsListBean.setObservedOutput(stepsList.getTestStepExpectedOutput());
									tStepsListBean.setTestStepStatus(tstepExecutionResult.getResult());
									tStepsListBean.setFailureReason(tstepExecutionResult.getFailureReason());
									System.out.println(stepsList.getTestStepId()+" "+stepsList.getTestStepDescription()+" "+stepsList.getTestStepExpectedOutput()+" "+tstepExecutionResult.getResult()+" "+tstepExecutionResult.getFailureReason());
									testCaseStepsListBeans.add(tStepsListBean);
								}

							}
						}
						tc.setTestStep(testCaseStepsListBeans);
						tclBeanList.add(tc);
					}
					testRunJob.setTestCaseList(tclBeanList);
					testRunJobSet.add(testRunJob);
					 */
				}
			}
			workPackage.setJobsCount(jobCount);
			workPackage.setTotalTestCases(totalTestCases);
			workPackage.setTotalTestSteps(totalTestSteps);
			workPackage.setPassedTestCases(passedTestCases);
			workPackage.setPassedTestSteps(passedTestSteps);
			workPackage.setJob(testRunJobSet);
			workPackage.setResult(wpResult);
			notExecutedTCcount = totalTestCases - (failedTestCases + passedTestCases);
			executedTCcount = failedTestCases + passedTestCases;
			workPackage.setExecutedTCCount(executedTCcount);
			//selectedTCcount pending
			workPackage.setNotExecutedTCCount(notExecutedTCcount);
		}catch(Exception e){
			log.error("Error for constructing Beans for Marshalling",e);
			return null;
		}
		return workPackage;
	}

	@Override
	@Transactional
	public void workpackageSelectiveExecutionPlan(WorkPackage workPackage, TestRunPlan testRunPlan, Map<RunConfiguration, String> testConfigs, HttpServletRequest req, String deviceNames) {
		workPackageDAO.workpackageSelectiveExecutionPlan(workPackage, testRunPlan, testConfigs, req, deviceNames);
	}

	@Override
	@Transactional
	public List<WorkPackage> listAllWorkPackages(Integer limit, Integer offset) {
		return workPackageDAO.listAllWorkPackages(limit, offset);
	}

	@Override
	@Transactional
	public WorkPackage getworkpackageByTestPlanId(Integer testPlanId, Integer productBuildId) {
		return workPackageDAO.getworkpackageByTestPlanId(testPlanId, productBuildId);
	}

	@Override
	@Transactional
	public WorkPackage getWorkPackageByProductBuild(Integer productBuildId) {
		return workPackageDAO.getWorkPackageByProductBuild(productBuildId);
	}

	@Override
	@Transactional
	public  List<ISERecommandedTestcases>  getIntelligentTestPlanFromISE(TestRunPlan testRunPlan, Integer productBuildId){		
		JSONObject finalObject = new JSONObject();
		List<TestCaseList> recommendedTestCasesList= new ArrayList<TestCaseList>();
		List<String> recommendedTestCases= new ArrayList<String>();
		JSONParser jsonParser = new JSONParser();		
		List<ISERecommandedTestcases> recommandedList=null;
		Integer buildId = null;
		ProductBuild pBuild = null;
		try {
			ProductMaster product=productMasterDAO.getProductByName(testRunPlan.getProductVersionListMaster().getProductMaster().getProductName());
			finalObject.put("username","admin@hcl.com");
			//Provide the list of defects fixed in this build. For now, pass an empty list
			finalObject.put("defectIds","");
			//Provide the max no of test cases ISE should recommend. Setting it to 1000 for now
			//TODO : Make a configurable property of Test Run Plan
			finalObject.put("maxNoOfTestCases","1000");
			//Provide the list of builds from whose history to learn
			//In this case, provide all the builds of the product
			if(product != null) {
				finalObject.put("projectName",product.getProductName());
				List<ProductVersionListMaster> productVersionList=productVersionListMasterDAO.list(testRunPlan.getProductVersionListMaster().getProductMaster().getProductId());
				JSONObject versionObject = new JSONObject();
				if(productVersionList != null && productVersionList.size() >0) {
					JSONArray buildIds = new JSONArray();
					for(ProductVersionListMaster productVersion:productVersionList) {
						List<ProductBuild> productBuildList=productBuildDAO.list(productVersion.getProductVersionListId());
						if(productBuildList != null && productBuildList.size() >0) {
							for(ProductBuild build:productBuildList) {
								JSONObject buildObj = new JSONObject();
								buildObj.put("relsname", productVersion.getProductVersionListId());
								buildObj.put("bldid",build.getProductBuildId());
								buildIds.add(buildObj);
							}
						}
					}
					finalObject.put("buildIds", buildIds);
				}
			}

			//Pass the features mapped to the build under test. This indicates the features that were modified in this build
			//TODO : Add Product build mapping to TestRun Plan
			
			if(productBuildId != null && productBuildId != -1)
				buildId = productBuildId;
			else
				buildId = testRunPlan.getProductBuild().getProductBuildId();
			
			pBuild = productListService.getProductBuildById(buildId, 0);			
			if(pBuild == null) {
				log.error("Error in retrieving ISE Test cases for the product build id : "+buildId);
				return recommandedList;
			}		
			System.out.println("Build Id : "+ buildId + " & Build Name : "+pBuild.getBuildname());
			List<ProductFeature> featuresList = new ArrayList<ProductFeature>(); 
			featuresList = productListService.getFeatureListByProductIdAndVersionIdAndBuild(product.getProductId(),pBuild.getProductVersion().getProductVersionListId(),buildId,1, 0, 10000); //This will be from the newly implemented Build - Feature mapping
			JSONObject features = new JSONObject();
			if(featuresList != null && featuresList.size() >0) {
				log.info("Feature List Size : "+featuresList.size()+" & Build : "+buildId);
				System.out.println("Feature List Size : "+featuresList.size()+" & Build : "+buildId);
				JSONArray featureIds = new JSONArray();
				for(ProductFeature feature:featuresList) {
					JSONObject featureObj = new JSONObject();
					if(feature.getProductFeatureName() != null)
						featureObj.put("featurename", feature.getProductFeatureName());
					if(feature.getExecutionPriority() != null && feature.getExecutionPriority().getPriorityName() != null && 
							!feature.getExecutionPriority().getPriorityName().isEmpty()){
						
						if(feature.getExecutionPriority().getPriorityName().equalsIgnoreCase("Critical") || feature.getExecutionPriority().getPriorityName().equalsIgnoreCase("High")){
							featureObj.put("priority", "HIGH");
						} else if(feature.getExecutionPriority().getPriorityName().equalsIgnoreCase("Medium") || feature.getExecutionPriority().getPriorityName().equalsIgnoreCase("Trivial")){
							featureObj.put("priority", "LOW");
						}
					} else {
						featureObj.put("priority", "HIGH");
					}
					featureIds.add(featureObj);
				}
				finalObject.put("features", featureIds);
			}
			//Get only the environment combinations in the Test Run Plan and pass to ISE.
			Set<EnvironmentCombination>  environmentCombinationList = new HashSet<EnvironmentCombination>();
			Set<RunConfiguration> rcList = testRunPlan.getRunConfigurationList();
			if(rcList != null && !rcList.isEmpty()){
				for(RunConfiguration rc : rcList){
					if(rc.getEnvironmentcombination() != null && rc.getEnvironmentcombination().getEnvionmentCombinationStatus().equals(1)) {
						environmentCombinationList.add(rc.getEnvironmentcombination());
					}
				}
			}
			
			if(environmentCombinationList != null && environmentCombinationList.size() >0) {
				JSONArray testbeds = new JSONArray();
				Double distrbt=Double.valueOf(100)/Double.valueOf(environmentCombinationList.size());
				for(EnvironmentCombination envCombination:environmentCombinationList) {
					JSONObject environObj = new JSONObject();
					environObj.put("envname", envCombination.getEnvironmentCombinationName());
					environObj.put("distrbt",distrbt);
					testbeds.add(environObj);
				}
				finalObject.put("testbeds", testbeds);
			}

			log.info("Test plan input :"+finalObject);
			String responseData= ISEServerAccesUtility.GetISERestServiceCall(iseServerURL,finalObject.toString(),iseRegressionOptimizationServiceName);			
			log.info("Final Result"+responseData);			
			recommandedList= generateJsonOutput(responseData,buildId);
			
			if(recommandedList != null && !recommandedList.isEmpty()){
				//Remove duplicate test case recommendations from ISE Analytical part
				Set<ISERecommandedTestcases> distinctRecommendedSet = new HashSet<ISERecommandedTestcases>();
				
				for(ISERecommandedTestcases iseRecommandedTestcase:recommandedList) {
					if(!validateDuplicateForISERecommandedTestCase(iseRecommandedTestcase.getTitle(),distinctRecommendedSet)) {
						distinctRecommendedSet.add(iseRecommandedTestcase);
					}
				}
				recommandedList = null;
				recommandedList = new ArrayList<ISERecommandedTestcases>();
				recommandedList.addAll(distinctRecommendedSet);
				log.info("After filtering duplicate records : "+ recommandedList.size());
			}
			
		} catch (Exception e) {
        	log.error("Problem after pushing  pushing testcase", e);
        }	
		return recommandedList;
	}

	@Override
	@Transactional
	public void addTestCycle(TestCycle testCycle) {
		workPackageDAO.addTestCycle(testCycle);
		
	}

	@Override
	@Transactional
	public List<TestCycle> getTestCycleList(Integer testFactoryId,Integer productId, Integer productVersionId, Integer testPlanGroupId, int jtStartIndex, int jtPageSize) {
		return workPackageDAO.getTestCycleList(testFactoryId,productId, productVersionId, testPlanGroupId, jtStartIndex, jtPageSize);
	}
	
	@Override
	@Transactional
	public List<JsonWorkPackageTestCaseExecutionPlanForTester> getWPTCExecutionSummaryByTestCycleId(Integer testCycleId, int jtStartIndex,int jtPageSize ) {
		List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEPList = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
		try {
			List<WorkPackageTCEPSummaryDTO> wtpdto = workPackageDAO.getWPTCExecutionSummaryByTestCycleId(testCycleId, jtStartIndex, jtPageSize);
			if (wtpdto != null && wtpdto.size() > 0) {
				for (WorkPackageTCEPSummaryDTO workPackageTCEPSummaryDTO : wtpdto) {
					WorkPackage wp = workPackageDAO.getWorkPackageById(workPackageTCEPSummaryDTO.getWorkPackageId());
					JsonWorkPackageTestCaseExecutionPlanForTester jsonWPTCE = new JsonWorkPackageTestCaseExecutionPlanForTester(workPackageTCEPSummaryDTO);
					Integer wpTotalTestcasesCount = 0;
					String testSuiteName = "N/A";
					int count = 0;
					if(wp.getTestRunJobSet() != null){
						for(TestRunJob trj : wp.getTestRunJobSet()){
							wpTotalTestcasesCount += workPackageDAO.getExecutionTCCountForJob(trj.getTestRunJobId());
							testSuiteName = count == 0 ? trj.getTestSuite().getTestSuiteName() : testSuiteName+","+trj.getTestSuite().getTestSuiteName();
							count++;
						}
					}
					jsonWPTCE.setTestsuiteName(testSuiteName);
					String executionMode = "N/A";
					String tpExecutionMode = "N/A";
					if(wp != null){
						if(wp.getTestExecutionMode() != null)
							executionMode = wp.getTestExecutionMode();	
						if(wp.getTestRunPlan() != null){
							jsonWPTCE.setTestPlanId(wp.getTestRunPlan().getTestRunPlanId());
							jsonWPTCE.setTestPlanName(wp.getTestRunPlan().getTestRunPlanName());
							//jsonWPTCE.setUseIntelligentTestPlan(wp.getTestRunPlan().getUseIntelligentTestPlan());
							if(wp.getTestRunPlan().getUseIntelligentTestPlan().equalsIgnoreCase("Yes"))
									jsonWPTCE.setUseIntelligentTestPlan("1");
							else
								jsonWPTCE.setUseIntelligentTestPlan("0");
						}
						if(wp.getTestRunPlan() != null && wp.getTestRunPlan().getAutomationMode() != null)
							tpExecutionMode = wp.getTestRunPlan().getAutomationMode();
					}
					//jsonWPTCE.setTotalWPTestCase(wp.getPlannedTestCasesCount());
					jsonWPTCE.setTotalWPTestCase(wpTotalTestcasesCount);
					Integer totalExecutedTestCasesCount = 0;
					totalExecutedTestCasesCount = jsonWPTCE.getP2totalPass() + jsonWPTCE.getP2totalFail();
					jsonWPTCE.setTotalExecutedTesCases(totalExecutedTestCasesCount);
					if(totalExecutedTestCasesCount > wpTotalTestcasesCount)
						jsonWPTCE.setTotalWPTestCase(totalExecutedTestCasesCount);
					Integer notExecutedCount = 0;
					if(jsonWPTCE != null && jsonWPTCE.getTotalWPTestCase() != null)
						notExecutedCount = jsonWPTCE.getTotalWPTestCase() - (jsonWPTCE.getP2totalPass() + jsonWPTCE.getP2totalFail());
					jsonWPTCE.setNotExecuted(notExecutedCount);
					jsonWPTCE.setP2totalNoRun(notExecutedCount);
					jsonWPTCE.setExecutionMode(executionMode);
					jsonWPTCE.setTestPlanExecutionMode(tpExecutionMode);
					
					//Fix for defect 4372 - If a job has no failed or passed and it has not executed then result should be failed
					//Special condition should be checked that the job should be aborted
					if(notExecutedCount > 0 && jsonWPTCE.getWpStatus() != null && 
							!(jsonWPTCE.getWpStatus().contains("New") || jsonWPTCE.getWpStatus().contains("Planning") || jsonWPTCE.getWpStatus().contains("Execution") || jsonWPTCE.getWpStatus().contains("Workpackage Planning")))
						jsonWPTCE.setResult("Failed");
					String firstActualExecutionDate = "NA";
					if(wp.getCreateDate() != null && wp.getCreateDate().toString().contains(" ")){
						String createdDate = wp.getCreateDate().toString();
						firstActualExecutionDate = createdDate.split(" ")[0];
					}
					jsonWPTCE.setFirstActualExecutionDate(firstActualExecutionDate);	
					if(wp.getProductBuild() != null && wp.getProductBuild().getProductMaster() != null )
					jsonWPTCE.setProductId(wp.getProductBuild().getProductMaster().getProductId());
					/*if(wp.getProductBuild() != null){
						jsonWPTCE.setProductBuildId(wp.getProductBuild().getProductBuildId());
						jsonWPTCE.setProductBuildName(wp.getProductBuild().getBuildname());
						if(wp.getProductBuild().getProductVersion() != null){
							jsonWPTCE.setProductVersionId(wp.getProductBuild().getProductVersion().getProductVersionListId());
							jsonWPTCE.setProductVersionName(wp.getProductBuild().getProductVersion().getProductVersionName());
							if(wp.getProductBuild().getProductVersion().getProductMaster() != null){
								jsonWPTCE.setProductId(wp.getProductBuild().getProductVersion().getProductMaster().getProductId());
							}else{
								jsonWPTCE.setProductId(0);
							}
						}else{
							jsonWPTCE.setProductVersionId(0);
							jsonWPTCE.setProductVersionName("");							
						}
					}else{
						jsonWPTCE.setProductBuildId(0);
						jsonWPTCE.setProductBuildName("");
					}*/
					jsonWPTCEPList.add(jsonWPTCE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in retrieving Workpackage execution summary " ,e);
		}
		return jsonWPTCEPList;
	}

	@Override
	@Transactional
	public Integer getTotalWPCount(Integer testFactoryId, Integer productId,Integer productBuildId) {
	Integer wtpdto = 0;
	try {
		wtpdto = workPackageDAO.getWPTotalCount(testFactoryId,productId, productBuildId);
	} catch(Exception e){
		return wtpdto;
	}
	return wtpdto;
	}
}