package com.hcl.atf.taf.dao;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.ListMultimap;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.Evidence;
import com.hcl.atf.taf.model.EvidenceType;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.PerformanceLevel;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductLocale;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.TestCaseConfiguration;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
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
import com.hcl.atf.taf.model.dto.MetricsMasterDTO;
import com.hcl.atf.taf.model.dto.MetricsMasterDefectsDTO;
import com.hcl.atf.taf.model.dto.MetricsMasterTestCaseResultDTO;
import com.hcl.atf.taf.model.dto.TestCaseDTO;
import com.hcl.atf.taf.model.dto.WorkPackageBuildTestCaseSummaryDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDayWisePlanDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDemandProjectionDTO;
import com.hcl.atf.taf.model.dto.WorkPackageExecutionPlanUserDetails;
import com.hcl.atf.taf.model.dto.WorkPackageStatusSummaryDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPSummaryDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTestCaseExecutionPlanStatusDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTestCaseSummaryDTO;
import com.hcl.atf.taf.model.json.JsonTestCycle;
import com.hcl.atf.taf.model.json.JsonUserList;
import com.hcl.atf.taf.model.json.JsonWorkPackageFeature;
import com.hcl.atf.taf.model.json.JsonWorkPackageFeatureExecutionPlan;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCase;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestSuiteExecutionPlan;


public interface WorkPackageDAO  {	 
	
	WorkPackage getWorkPackageById(int workPackageId);
	WorkPackage getWorkPackage(WorkPackage wp);
	List<WorkPackageTestCase> listWorkPackageTestCases(int workPackageId);
	List<WorkPackageTestCase> listWorkPackageTestCases(int workPackageId, int jtStartIndex, int jtPageSize);
	int addNewWorkPackageTestCases(List<WorkPackageTestCase> workPackageTestCases);
	
	int totalRecordsCountForWorkPackageTestCases(int workPackageId);
	WorkPackageTestCase updateWorkPackageTestCase(WorkPackageTestCase workPackageTestCase);	
	
	List<Environment> getEnvironmentListByProductId(int productId);	
	int addWorkPackageTestcaseExecutionPlan(List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList);

	int addWorkPackageTestcaseExecutionPlan(List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList,List<Environment> environments);
	List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestcaseExecutionPlanByWorkPackageId(int workPackageId,int testcaseId,int testSuiteId,int featureId,String type);
	List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestcaseExecutionPlanByWorkPackageId(int workPackageId,int testcaseId,int environmentId,WorkPackageTestCase workPackageTestCase);
	List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestcaseExecutionPlanByWorkPackageIdByLocale(int workPackageId,int testcaseId,int localeId,WorkPackageTestCase workPackageTestCase);
	WorkPackageTestCase  getWorkPackageTestCaseById(int workPackageTestCaseId);
	void deleteWorkPackageTestcaseExecutionPlan(List<WorkPackageTestCaseExecutionPlan> WorkPackageTestCaseExecutionPlanList);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlan(Map<String, String> searchString,int workPackageId, int jtStartIndex, int jtPageSize,String testLeadId,String testerId,String envId,int localeId,String plannedExecutionDate ,String dcId,String executionPriority,int status);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlan(int workPackageId, int jtStartIndex, int jtPageSize,UserList user,String plannedExecutionDate);
	int totalRecordsCountForWorkPackageTestCaseExecutionPlan(int workPackageId,UserList user);
	List<UserList> userListByRole(String role);
	int getTotalRecordsOfUserByRole(String role);
	

	//For Workpackage
	void addWorkPackage(WorkPackage workPackage);
	void updateWorkPackage(WorkPackage workPackage);
	void deleteWorkPackage(WorkPackage workPackage);
	void deleteEvidence(Evidence evidence);

	List<WorkPackage> list();	
	List<WorkPackage> listWorkPackages(int productBuildId);	
	WorkPackage getWorkPackageByProductBuildId(int workPackageId);
	int getWorkPackagesCountByBuildId(int productBuildId);
	int getTotalRecords();
	List<WorkPackage> listWorkPackages(int productBuildId, int status);	
	void  updateWorkPackageTestCaseExecutionPlan(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlanFromUI);
	Environment getEnvironmentById(int environmentId);

	List<Environment> getWorkPackageTestCasesExecutionPlanEnvironments(int workPackageId, int testCaseId);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanByWorkPackageId(int workPackageId);

	// Added for - Tester's view of 'Workpackage TestCase Plan view/edit'
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlanByTester(int testerId, int workPackageId, int jtStartIndex,int jtPageSize);
	int totalRecordsCountForWorkPackageTestCaseExecutionPlanByTesterId(int testerId, int workPackageId);
	
	// Added for - Demand Projection
	List<WorkPackageDemandProjectionDTO> listWorkPackageDemandProjection(int workPackageId, Integer workShiftId, Date startDate, Date endDate);
	int totalRecordsCountForWorkPackageDemandProjection(int workPackageId);
	List<WorkPackageDemandProjection> updateWorkPackageDemandProjection(List<WorkPackageDemandProjection> workPackageDemandProjections);
	
	TestCaseExecutionResult getTestCaseExecutionResultByID(int testCaseExecutionResultId);
	List<WorkShiftMaster> listAllWorkShift();
	WorkShiftMaster getWorkShiftById(int shiftId);
	WorkShiftMaster getWorkShiftByName(String shiftName);
	List<ProductUserRole> userListByProductRole( Integer productId,String userRoleId) ;
	Integer getProductIdByWorkpackage(int workPackageId);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanTestLead(int workPackageId, int jtStartIndex,int jtPageSize,UserList user);

	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanByEnv(int workPackageId, int jtStartIndex,int jtPageSize,int envId);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanTestLeadByEnv(int workPackageId, int jtStartIndex,int jtPageSize,UserList user,int envId);
	
	Set<RunConfiguration> getEnvironmentMappedToWorkpackage(int workpackageId);
	WorkPackage getWorkPackageByWorkPackageName(String workPackageName);
	List<WorkPackage> getWorkPackagesFromWorkPackageTestcaseExecutionPlanByUserId(int userId);
	WorkPackage mapWorkpackageEnv(int workpackageId,int envId,String action);
	Set<ProductLocale> getLocaleMappedToWorkpackage(int workpackageId);
	WorkPackage mapWorkpackageLocale(int workpackageId,int localeId,String action);
	List<ProductLocale> getWorkPackageTestCasesExecutionPlanLocales(int workPackageId, int testCaseId);	
	List<WorkPackageStatusSummaryDTO> workPackageStatusSummary(int workPackageId,ProductUserRole productUserRole);
	int updateWorkPackageTestcaseExecutionPlan(List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList);
	boolean checkExists(TestCaseList testCase,WorkPackage workPackage,Environment environment);
	void deleteWorkPackageTestcaseExecutionPlan(TestCaseList testCase,WorkPackage workPackage,Environment environment,ProductLocale localeSel);
	WorkPackageTestCaseExecutionPlan getWorkpackageTestcaseExecutionPlanById(int workPackageTestCaseExecutionPlanId);
	List<JsonWorkPackageTestCaseExecutionPlan> getNotExecutedTestCasesOfWorkPackageId(int workPackageId);
	List<WorkPackageTestCaseExecutionPlan> listCompletedOrNotCompletedTestCasesOfworkPackage(int workPackageId, int jtStartIndex, int jtPageSize,int isExecuted,ProductUserRole productUserRole);
	WorkPackageTestCaseExecutionPlanStatusDTO listWorkpackageTestCaseExecutionPlanningStatus(int workPackageId);
	List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestCaseExecutionPlanByWorkpackgeId(int workPackageId);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlan(int workPackageId, int jtStartIndex, int jtPageSize,ProductUserRole productUserRole);
	List<WorkPackageDemandProjectionDTO>  listWorkpackageDemandProdjectionByWorkpackageId(Date startDate, Date endDate, Integer testFactoryLabId, Integer testFactoryId, Integer productId, Integer workPackageId, Integer shiftId, String viewType);
	WorkPackage getWorkPackageByIdWithMinimalnitialization(int workPackageId);
	List<WorkPackage> getWorkPackagesByProductId(int productId);
	int getWorkPackagesCountByProductId(int productId);
	List<WorkPackage> getUserAssociatedWorkPackages(int userRoleId,int userId);
	List<TestFactoryResourceReservation>  getUserByBlockedStatus(int workPackageId, int roleId,String plannedExecutionDate,int shiftId);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanTesterByEnv(int workPackageId, int jtStartIndex,int jtPageSize,UserList user,int envId);
	WorkPackageTestCaseExecutionPlan listWorkPackageTestCasesExecutionPlan(int wptcepId,int testcaseId);
	List<TestExecutionResultBugList> listDefectsByTestcaseExecutionPlanId(int tcerId,int jtStartIndex,int jtPageSize);
	void addTestCaseDefect(TestExecutionResultBugList testExecutionResultBugList);
	
	void updateTestCaseDefect(
			TestExecutionResultBugList testExecutionResultBugList);
	List<WorkFlow> getworkFlowListByEntity(int entityType);
	List<TestCaseExecutionResult> listResultsByTestcaseExecutionPlanId(int tcerId);
	void updateTestCaseResults(TestCaseExecutionResult testCaseExecutionResult);
	ExecutionPriority  getExecutionPriorityByName(String priorityValue);
	List<WorkPackageDayWisePlanDTO> listWorkPackageDayWisePlan(int workPackageId, Integer workShiftId, Date startDate, Date endDate);
	List<ExecutionPriority> getExecutionPriority();
	ExecutionPriority getExecutionPriorityById(int executionPriorityId);
	List<WorkShiftMaster>  listActualShiftByPlannedDateAndTestFactory(Integer testFactoryId,String actualExecutionDate);
	void deleteWorkPackageTestCaseExecutionPlan(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan);
	WorkPackageTestCaseExecutionPlan mapWorkPackageTestCaseExecutionPlanEnv(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan, Environment environment,String action);
	void mapWorkpackageWithTestCase(Integer workPackageId,Integer testcaseId,String action);
	Set<Environment> getEnvironmentSet(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan);

	void deleteMappingWorkPackageTestCaseExecutionPlanEnv(Integer wptcepId,Integer environmentId);
	ProductMaster getProductMasterByWorkpackageId(int workPackageId);
	
	String getWptcepTestLeadSpocName(Integer workPackageId, Integer shiftId, Date executionDate, Integer testerId);
	String getWptcepTestLeadSpocName(Integer workPackageId, Integer shiftId, Date executionDate);

	WorkPackageTestCaseSummaryDTO listWorkPackageTestCaseExecutionSummary(Integer workPackageId);
	
	void addWorkPackageDemandProjection(WorkPackageDemandProjection workPackageDemandProjection);
	List<WorkPackageDemandProjection> listWorkPackageDemandProjectionByDate(int workPackageId, int shiftId, Date date);
	List<UserRoleMaster> getUserRolesForDemandProjection();
	WorkPackageDemandProjection getWorkPackageDemandProjectionById(int workPackageDemandProjectionId);
	void deleteWorkPackageDemandProjection(WorkPackageDemandProjection wpDemandProjection);
	void updateWorkPackageDemandProjection(WorkPackageDemandProjection wpDemandProjection);
	
	WorkFlow getWorkFlowByEntityIdStageId(Integer entityId,Integer stageId);
	void addWorkFlowEvent(WorkFlowEvent workFlowEvent);
	
	EntityMaster  getEntityMasterById(Integer entityId);
	void addEvidence(Evidence evidence);
	List<Evidence> testcaseListByEvidence(Integer tcerId,String type);
	List<TestStepExecutionResult> listTestStepPlan(Integer testcaseId,Integer tcerId);
	
	TestStepExecutionResult getTestStepExecutionResultById(Integer testStepId);
	void updateTestStepExecutionResult(TestStepExecutionResult testStepExecutionResult);
	List<WorkPackageTestCaseSummaryDTO> listWorkPackageTestCaseExecutionSummaryByDate(Integer workPackageId);
	WorkFlow getWorkFlowByEntityIdWorkFlowId(Integer entityId,Integer workFlowId);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlan(Integer workPackageId,UserList user,String plannedExecutionDate,String filter,WorkpackageRunConfiguration wpRunConfigObj);
	List<PerformanceLevel> listPerformanceRating();
	
	List< UserList> getAllocatedUserListByRole(Integer workpackageId,Integer roleId);
	WorkPackageTestCaseExecutionPlan getWorkpackageTestcaseExecutionPlanByIdWithMinimalIntizialition(int workPackageTestCaseExecutionPlanId);
	List<TestCaseExecutionResult> listApprovedDefectsByTestcaseExecutionPlanId(int tcerId,int jtStartIndex, int jtPageSize);
	List<WorkPackage> listWpOfUserforSelectedProduct(Integer productId, Integer productVersionId, Integer userId,int startIndex, int pageSize);
	List<TestCaseDTO> listWorkPackageTestCasesExecutionPlanByWorkPackageIdGroupByTestcase(int workPackageId,String envId,String executionPriority,String result,int jtStartIndex,int jtPageSize,int sortBy, int testcaseId);	
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanByWorkPackageId(int workPackageId,int testcaseId);
	void updateTestCaseExecutionResult(TestCaseExecutionResult testCaseExecutionResult);
	void addTestCaseConfiguration(TestCaseConfiguration testCaseConfiguration);
	List<WorkPackage> listWorkPackagesByUserIdAndPlannedExecutionDate(
			int userId, String plannedExecutionDate);
	List<WorkPackage> listWorkPackageByResourceCheckInProductId(Integer productId);
	void deleteTestCaseConfigurationByWPTCEID(Integer wptcep);
	WorkPackage mapWorkpackageEnvCombination(int workpackageId,int envId,String action);
	List<WorkPackageDemandProjectionDTO> listWorkpackageDemandProdjectionByDate(Date selectedDate, Integer testFactoryLabId, Integer shiftTypeId);
	void saveTestStepExecutionResult(List<TestStepExecutionResult> testStepExecutionResultList);
	List<TestCaseConfiguration> listTestCaseConfigurations(int workpackageId);
	void deleteTestStepResult(Integer testCaseExecutionResultId);
	List<WorkPackageTestSuite> listWorkPackageTestSuite(Integer workPackageId,int startIndex, int pageSize);
	int addNewWorkPackageTestSuite(List<WorkPackageTestSuite> workPackageTestSuites);
	int seedWorkPackageWithNewTestSuites(List<TestSuiteList> newTestSuites,Integer workPackageId);
	WorkPackageTestSuite getWorkPackageTestSuiteById(Integer testSuiteId);
	WorkPackageTestSuite updateWorkPackageTestSuite(WorkPackageTestSuite workPackageTestSuiteFromDB);
	List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestcaseExecutionPlanByWorkPackageId(Integer workPackageId,Set<TestCaseList> testCaseLists);
	WorkPackage deleteRunConfigurationWorkpackage(Integer workpackageId,Integer runconfigId,String type);
	WorkpackageRunConfiguration addRunConfigurationWorkpackage(Integer workpackageId,Integer runconfigId,String type);
	WorkpackageRunConfiguration getWorkpackageRunConfiguration(Integer workpackageId,Integer runconfigId,String type);
	List<WorkpackageRunConfiguration> getWorkpackageRunConfigurationList(Integer workpackageId,Integer runconfigId,String type);
	RunConfiguration getRunConfigurationById(Integer runConfigId);
	WorkpackageRunConfiguration getWorkpackageRunConfigurationByWPTCEP(Integer id);
	void deleteRunConfigurationWorkpackageById(WorkpackageRunConfiguration id);
	WorkPackage addWorkpackageToTestRunPlan(TestRunPlan testRunPlan,Map<String,String> mapData,UserList user,HttpServletRequest req,TestRunPlanGroup testRunPlanGroup,WorkPackage workPackage);
	WorkPackage addWorkpackageToTestRunPlan(TestRunPlan testRunPlan,Map<String,String> mapData,UserList user,HttpServletRequest req,TestRunPlanGroup testRunPlanGroup,WorkPackage workPackage,String deviceNames);

	RunConfiguration getWorkpackageRCByEnvDev(Integer workpackageId,Integer environmentCombinationId,Integer deviceId);
	public List<WorkPackage> listWorkPackagesByProductBuild(int productBuildId);
	List<WorkPackage> listWorkPackageBytestrunplanId(Integer testRunPlanId);
	int getTotalRecordWPTCEP(Integer workPackageId,int status);	
	Set<EnvironmentCombination> getEnvironmentCombinationMappedToWorkpackage(int workpackageId);
	Set<GenericDevices> getDevicesMappedToWorkpackage(int workpackageId);
	void mapWorkpackageWithTestSuite(Integer workPackageId,Integer testSuiteId,String type);
	void addTestRunJob(TestRunJob testRunJob);
	WorkPackageTestCaseExecutionPlan  workPackageTestCasesExecutionPlanByJobId(TestRunJob testRunJob,TestCaseList testCaseList);
	WorkPackageTestSuite workPackageTestCasesByJobId(TestRunJob testRunJob);
	 List<TestCaseExecutionResult> listTestCaseExecutionresultBywpRunconfigId(int workpackageId,Integer wpRunConfigId,Integer testCaseId);
	 List<TestCaseExecutionResult> listTestCaseExecutionresultBywpRunconfigId(int workpackageId,Integer wpRunConfigId,Integer testCaseId,Integer priorityId);
	void addTestRunJob(RunConfiguration runConfiguration,TestSuiteList testSuiteList,WorkPackage workPackage,TestRunPlan testRunPlan);
	TestRunJob getTestRunJobByWP(WorkPackage workPackage,RunConfiguration runConfiguration);
	void mapTestRunJobTestCase(Integer testRunJobId,Integer testcaseId,String type);
	void mapTestRunJobTestSuite(Integer testRunJobId,Integer testsuiteId,String type);
	int addNewWorkPackageFeature(List<WorkPackageFeature> workPackageFeatures);
	List<WorkPackageFeature> listWorkPackageFeature(Integer workPackageId,Integer startIndex,Integer pageSize);
	int seedWorkPackageWithNewFeature(List<ProductFeature> newFeatures,Integer workPackageId);
	WorkPackageFeatureExecutionPlan getWorkpackageFeatureExecutionPlanById(Integer rowId);

	WorkPackageFeature getWorkPackageFeatureById(Integer workpackageFeatureId);
	WorkPackageFeature updateWorkPackageFeature(WorkPackageFeature workPackageFeatureFromDB);
	void mapWorkpackageWithFeature(Integer workPackageId,
			Integer featureId, String action) ;
	void addWorkpackageFeatureExecutionPlan(WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan);
	void mapTestRunJobFeature(Integer testRunJobId, Integer featureId, String type);
	List<WorkPackageFeatureExecutionPlan> listWorkPackageFeatureExecutionPlan(Map<String, String> searchString,Integer workPackageId,Integer  jtStartIndex,Integer jtPageSize,int status);
	void updateWorkPackageFeatureExecutionPlan(WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlanFromUI);
	WorkPackageFeatureExecutionPlan getWorkpackageFeatureExecutionPlan(Integer workPackageId,Integer productFeatureId,Integer runConfigurationId);
	void deleteWorkpackageFeatureExecutionPlan(WorkPackageFeatureExecutionPlan wpfep);
	void addWorkpackageTestSuiteExecutionPlan(WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan);
	WorkPackageTestSuiteExecutionPlan getWorkpackageTestSuiteExecutionPlan(Integer workPackageId,Integer testSuiteId,Integer runConfigurationId);
	void deleteWorkpackageTestSuiteExecutionPlan(WorkPackageTestSuiteExecutionPlan wptsp);
	int getTotalRecordWPFEP(Integer workpackageId,int status);
	int getTotalRecordWPTSEP(Integer workpackageId,int status);
	List<WorkPackageTestSuiteExecutionPlan> listWorkPackageTestSuiteExecutionPlan(Map<String, String> searchString,Integer workpackageId, Integer jtStartIndex,Integer jtPageSize,int status);
	void updateWorkPackageTestSuiteExecutionPlan(WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlanFromUI);
	WorkPackageTestSuiteExecutionPlan getWorkpackageTestSuiteExecutionPlanById(Integer rowId);
	WorkPackageTestCaseExecutionPlanStatusDTO listWorkpackageFeatureExecutionPlanningStatus(Integer workPackageId);
	List<WorkPackageFeatureExecutionPlan>  getWorkPackageFeatureExecutionPlanByWorkpackgeId(Integer workPackageId);
	WorkPackageTestCaseExecutionPlanStatusDTO listWorkpackageTestSuiteExecutionPlanningStatus(Integer workPackageId);
	List<WorkPackageTestSuiteExecutionPlan> getWorkPackageTestSuiteExecutionPlanByWorkpackgeId(Integer workPackageId); 
    List<WorkPackageTestSuite> listWorkPackageTestSuite(Integer workPackageId);
    List<WorkPackageFeature> listWorkPackageFeature(Integer workPackageId);
	JsonWorkPackageTestCaseExecutionPlanForTester listWorkPackageTestCaseExecutionSummaryReport(
			Integer workPackageId);
	WorkPackageFeature getWorkpackageFeaturePlanById(Integer tsWpId);

	WorkPackageTestCase getWorkpackageTestCaseByPlanId(Integer tcWpId);
	WorkPackageTestSuite getWorkpackageTestSuiteByPlanId(Integer tsWpId);
	List<WorkPackage> listActiveWorkPackages(Integer productId);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanBywpId(int workPackageId,String filter);
	List<WorkPackageExecutionPlanUserDetails> listWorkpackageExecutionPlanUserDetails(Integer workPackageId,String plannedExecutionDate,String role);
	List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestcaseExecutionPlanByWorkPackageId(int workPackageId,int testcaseId,int testSuiteId,int featureId,String type,int runConfigId);
	List<WorkpackageRunConfiguration> getTestRunJobByWPTCEP(Integer workPackageId, Integer userId,String plannedExecutionDate);
	boolean isWorkPackageExecutionAvailable(WorkPackage workPackage);
	WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlanByJobId(TestRunJob testRunJob,TestSuiteList testSuiteList);
	List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestCaseExecutionPlanByTestRunJob(TestRunJob testRunJob,int testSuiteId);
	List<TestExecutionResultBugList>  listDefectsByWorkpackageId(int workpackageId);
	int addWorkpackageToTestRunPlanGroup(TestRunPlanGroup testRunPlanGroup,Map<String,String> mapData,UserList user,HttpServletRequest req);
	TestRunPlan getNextTestRunPlan(TestRunPlanGroup testRunPlanGroup,TestRunPlan testRunPlan);
	void workpackageExxecutionPlan(WorkPackage workPackage,TestRunPlan testRunPlan,HttpServletRequest req);
	List<JsonUserList> getUserByBlockedStatusPerform(
			int workPackageId, int roleId, String plannedExecutionDate,
			int shiftId, int requiredRoleId, int productId);
	
	 List<Object[]> listWorkPackageTestCaseExeSummaryByFilters(int workpackageId,int filterId,String defectsCount);
	 Set<RunConfiguration>  listTestBedByFeature(int featureId,int workpackageId,int status);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestcaseExecutionPlan(int featureId,int workpackageId,int runConfigId,String type,int testSuiteId,int testcaseId);
	Set<RunConfiguration> listTestBedByTestSuite(int testsuiteId,int workpackageId,int status);
	WorkPackageTestCaseExecutionPlan getWorkPackageTestcaseExecutionPlan(int featureId,int workpackageId,String runConfigName,String type,int testSuiteId,int testcaseId);
	 WorkPackageFeatureExecutionPlan getWorkpackageFeatureExecutionPlan(Integer workPackageId,Integer productFeatureId,String runConfigurationName);
	WorkPackageTestSuiteExecutionPlan getWorkpackageTestSuiteExecutionPlan(Integer workPackageId,Integer testSuiteId,String runConfigurationName);
	List<TestRunJob> getTestRunJobByBuildID(Integer productBuildId,Integer workPackageType,Integer jtStartIndex, Integer jtPageSize);
	TestRunJob getTestRunJobById(int testRunJobId);
	WorkPackage addWorkpackageToClonedProductBuild(ProductBuild clonedProductBuild, UserList user, HttpServletRequest req,WorkPackage workPackageToBeCloned);
	List<Object> listDefectsByTestcaseExecutionPlanIdByApprovedStatus(Integer productId, Integer productVersionId, Integer productBuildId,Integer workPackageId, Integer approveStatus, Date startDate, Date endDate, Integer raisedByUser,Integer jtStartIndex,Integer jtPageSize);
	EvidenceType getEvidenceTypeById(Integer evidenceTypeId);
	List<EvidenceType> getEvidenceTypes();
	List<WorkPackage> getActiveWorkpackagesByProductBuildId(Integer productBuildId);
	int addWorkPackageTestcaseExecutionPlan(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan);
	int totalWorkPackageDemandProjectionCountByWpId(int workPackageId,int ShiftId, Date date);
	WorkPackage getWorkPackageByIdWithLoad(int workpackageId);
	void cloneBuildPlan(int workPackageIdExisting,WorkPackage clonedWorkpackage,UserList user);
	void cloneBuildExecution(int workPackageIdExisting,WorkPackage clonedWorkpackage,UserList user);
	List<RunConfiguration> getRunConfigurationByWPId(Integer workpackageId) ;

	TestRunJob getTestRunJobByWPAndRunConfigName(int workPackageIdExisting,String runconfigName);
	void mapTestRunJobTestSuiteTestCase(int testRunJobId,int testSuiteId,List<TestCaseList> testCaseLists,String type);
	List<TestCaseList> getSelectedTestCasesFromTestRunJob(int testRunListId);
	TestExecutionResultBugList createBug(TestExecutionResultBugList testExecutionResultBugList);
	void saveTestStepExecutionResult(TestStepExecutionResult testStepExecutionResultList);
	List<TestExecutionResultBugList> getTestRunJobDefects(int testRunJobId);
	List<MetricsMasterDTO> getWorkpackageTestcaseExecutionPlanByDateFilter(int status, Date startDate,
			Date endDate);
	List<MetricsMasterDefectsDTO> getTotalBugListByStatus(
			Integer status);
	List<MetricsMasterTestCaseResultDTO> getTestcaseExecutionResultList();
	List<MetricsMasterDTO> getWorkpackageTestcaseExecutionPlanForResourceByDateFilter(int i, Date startDate, Date currentDate);
	List<WorkpackageRunConfiguration> getWorkpackageRunConfigurationOfWPwithrcStatus(Integer workpackageId, Integer runConfigStatus);
	void workpackageExxecutionPlan(WorkPackage workPackage,TestRunPlan testRunPlan,HttpServletRequest req,String deviceNames,String testcaseNames);
	List getTestRunResultFromListAndDetailViews(int testRunNo,String deviceId);
	List getTestRunResultFromViews(int testRunNo);
	Evidence getEvidenceById(int evidenceId);
	List<TestStepExecutionResult> listTestStepResultByCaseExecId(Integer tcerId);
	List<TestCaseExecutionResult> listAllTestCaseExecutionResult(int startIndex, int pageSize, Date startDate, Date endDate);
	Integer countAllTestCaseExecutionResult(Date startDate, Date endDate);
	List<WorkPackage> list(int startIndex, int pageSize, Date startDate,Date endDate);
	Integer getWorkPackageFeatureCount(Integer workPackageId, Integer jtStartIndex, Integer jtPageSize);
	List<JsonWorkPackageFeature> listJsonWorkPackageFeature(Integer workPackageId, Integer jtStartIndex, Integer jtPageSize);
	Integer getProductIdByWorkpackageId(int workPackageId);
	Integer getWorkPackageTestCasesCount(int workPackageId, int jtStartIndex, int jtPageSize);
	Integer getexecutionTypeIdByWorkpackageId(int workPackageId);
	Integer getProductTestCaseCount(int productId, int executionType);
	List<JsonWorkPackageTestCase> listJsonWorkPackageTestCases(int workPackageId, int jtStartIndex, int jtPageSize);
	List<JsonWorkPackageFeatureExecutionPlan> listJsonWorkPackageFeatureExecutionPlan(Map<String, String> searchString, Integer workPackageId,
			Integer jtStartIndex, Integer jtPageSize, int status);
	int getTotalRecordWPFEPCount(Integer workpackageId, int status);
	int getTestFactoryIdOfWorkPackage(Integer workpackageId);
	List<JsonWorkPackageTestSuiteExecutionPlan> listJsonWorkPackageTestSuiteExecutionPlan(Map<String, String> searchString, Integer workPackageId,
			Integer jtStartIndex, Integer jtPageSize, int status);
	int getTotalRecordWPTSEPCount(Integer workpackageId, int status);
	int getTotalRecordWPTCEPCount(Integer workPackageId, int status);
	List<JsonWorkPackageTestCaseExecutionPlan> listJsonWorkPackageTestCaseExecutionPlan(
			Map<String, String> searchString, int workPackageId, int jtStartIndex, int jtPageSize, String testLeadId,
			String testerId, String envId, int localeId, String plannedExecutionDate, String dcId, String executionPriority,
			int status);
	Integer countAllTestRunJob(Date startDate, Date endDate);
	List<TestRunJob> listAllTestRunJob(int startIndex, int pageSize, Date startDate, Date endDate);
	List<Object[]> getTescaseExecutionHistory(int testCaseId, Integer workPackageId, String dataLevel, Integer jtStartIndex, Integer jtPageSize);
	Integer getWPTCEPCountOfATestCaseId(int testCaseId, int jtStartIndex,
			int jtPageSize);
	Integer getWorkPackageTestCaseOfTCID(int testcaseId);
	int getWPTypeByTestRunPlanExecutionType(int workPackageId);
	WorkPackageTestCaseExecutionPlan listWorkPackageTestCasesExecutionPlanOfTCId(
			int testcaseId);
	List<WorkPackageBuildTestCaseSummaryDTO> listWorkPackageTestCaseExecutionBuildSummary(Integer workPackageId,Integer productBuildId);
	List<WorkPackageTCEPSummaryDTO> getWPTCExecutionSummaryByProdIdBuildId(Integer testFactoryId,
			Integer productId, Integer productBuildId, Integer jtStartIndex, Integer jtPageSize, String filter);
	HashMap<String, Integer> getTotalAndCompletedTestRunJobsOfWorkPackageById(int workPackageId);
	Date getFirstExecutedTCStartTimeofWP(int workPackageId);
	Date getLastExecutedTCEndTimeofWP(int workPackageId);
	void setUpdateWPActualStartDate(int workPackageId, Date startTime);
	void setUpdateWPActualEndtDate(int workPackageId, Date endTime);
	List<TestCaseList> listTestCaseOfTRJob(int testRunJobId);
	TestRunJob listTestCaseExecutionDetailsOfTRJob(
			int testRunJobId);
	WorkPackageTCEPSummaryDTO listWorkPackageTestCaseExecutionSummaryForTiles(Integer workPackageId);
	WorkPackageTestCaseExecutionPlan listTestStepExecutionDetailsOfTCExecutionId(
			int wptcepId);
	String getDeviceNameByTestRunJob(int testRunJobId);
	WorkPackageTestCaseExecutionPlan listWorkPackageTestCasesExecutionPlanOfTCExeId(
			int wptcepId);
	List<WorkPackageTestCaseExecutionPlan> getNotExecutedTestCaseListByWpId(
			Integer wpid);
	Integer addWorkFlowevent(WorkFlowEvent workFlowEvent);
	List<WorkPackageTestCaseExecutionPlan> getNotExecutedTestCaseListByJobId(Integer jobid);
	EntityMaster getEntityMasterByName(String entityMasterName);
	Integer getWPTCEPCountOfWPId(Integer testCaseId, Integer workPackageId);
	List<WorkPackage> getWorkpackageSetForAHost(Integer hoslistWorkPackageTestCaseExecutionPlanByWorkPackageIdtId);
	List<TestRunJob> incompleteTestRunJobSetForAWorkpackage(Integer workpackageId);
	List<WorkPackageDemandProjection> listWorkPackageDemandProjectionByWeekAndYear(int workPackageId, int shiftId, Set<Integer> recursiveWeeks, Integer workYear,Integer skillId,Integer roleId, Integer userTypeId);
	List<WorkPackageDemandProjection> getWorkPackageDemandProjectionByGroupDemandId(Long groupDemandId);
	void addTestfactoryResourceReservationWeekly(TestFactoryResourceReservation testFactoryResourceReservation);
	List<TestFactoryResourceReservation> listWorkPackageResourceReservationByWorkpackageWeekAndYear(Integer workPackageId, Integer shiftId, Integer reservationWeek,Integer reservationYear, Integer resourceId);
	Integer countAllResourceDemands(Date startDate, Date endDate);
	List<WorkPackageDemandProjection> listAllResourceDemands(int startIndex,int pageSize, Date startDate, Date endDate);
	TestFactoryResourceReservation getTestFactoryResourceReservationById(Integer reservationId);
	Integer countAllReservedResources(Date startDate, Date endDate);
	List<TestFactoryResourceReservation> listAllReservedResources(int startIndex, int pageSize, Date startDate, Date endDate);
	public List<String> generateTestRunListHtmlReport(Integer testRunNo,Integer testRunConfigurationChildId, String strPrintMode,ProductType productType, BufferedImage logo, String loginUserName);
	WorkPackage getLatestWorkPackageByproductId(Integer productId, String wpName);
	void createWorkpackageExecutionPlanForExistingWorkPackage(WorkPackage workPackage, TestRunPlan testRunPlan, HttpServletRequest req, String deviceNames, String testcaseNames);
	List<WorkPackageTCEPSummaryDTO> getWPTCExecutionSummaryByTestRunPlanId(Integer testRunPlanId , Integer jtStartIndex, Integer jtPageSize);
	List<TestCaseList> getSelectedTestCasesFromTestRunJobTestSuite(int testRunListId, Integer testSuiteId);
	ListMultimap<Integer, Object> getFeatureNamesByTestCaseIds(
			Set<Integer> listOfTestCaseIds);
	WorkPackage executeTestRunPlanWorkPackageUnattendedMode(TestRunPlan testRunPlan, Map<String, String> mapData,UserList user, HttpServletRequest req,TestRunPlanGroup testRunPlanGroup, WorkPackage workPackage,String deviceNames);
	WorkFlowEvent updateWorkFlowEvent(WorkFlowEvent workFlowEvent);		
	List<WorkFlowEvent> workFlowEventlist(int typeId);
	String getExecutionTimeForEachWPByTPId(Integer testRunPlanId);
	Integer getWPTCExecutionSummaryCount(Integer testFactoryId, Integer productId, Integer productBuildId, Integer jtStartIndex, Integer jtPageSize);
	Set<String> workFlowEvents(int typeId);
	public List<String> getAllJobIdsByWorkpackageId(Integer testRunNo);
	WorkPackageTestCaseExecutionPlan workPackageTestCasesExecutionPlanByJobId(TestRunJob executingTestRunJob, TestRunJob actualTestRunJob,
			TestCaseList testCaseList);
	Integer getProductBuildsTestedCount(Integer productBuildId);
	Integer getExecutionTCCountForJob(Integer testRunJobId);
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlanByJobAndSuiteId(
			Integer testRunJobId, Integer testSuiteId);
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlanByWorkPackageId(
			Integer testRunJobId);
	public void workpackageSelectiveExecutionPlan(WorkPackage workPackage, TestRunPlan testRunPlan, Map<RunConfiguration, String> testConfigs, HttpServletRequest req, String deviceNames);
	List<WorkPackage> listAllWorkPackages(int limit, int jtPageSize);
	List<WorkPackage> listAllWorkPackagesBasedOnDateFilters(String fromDate, String toDate); 
	List<WorkPackage> listAllWorkPackagesBasedOnProductBuildIdAndDateFilters(Integer productBuildId, String fromDate, String toDate);
	List<TestRunJob> completedAndAbortedTestRunJobSetForAWorkpackage(Integer workpackageId);
	List<WorkPackageTCEPSummaryDTO> getWPTCExecutionSummaryByProdIdAndBuildIdAndWorkpackageId(Integer testFactoryId,Integer productId, Integer productBuildId,Integer workpackageId, Integer jtStartIndex, Integer jtPageSize, String filter);
	WorkPackage getworkpackageByTestPlanId(Integer testPlanId, Integer productBuildId);
	WorkPackage getWorkPackageByProductBuild(Integer productBuildId);
	void addTestCycle(TestCycle testCycle);
	List<TestCycle> getTestCycleList(Integer testFactoryId,Integer productId, Integer productVersionId, Integer testPlanGroupId, int jtStartIndex, int jtPageSize);
	List<WorkPackageTCEPSummaryDTO> getWPTCExecutionSummaryByTestCycleId(Integer testPlanGroupId, int jtStartIndex, int jtPageSize);
	Integer getWPTotalCount(Integer testFactoryId, Integer productId,Integer productBuildId);
}
