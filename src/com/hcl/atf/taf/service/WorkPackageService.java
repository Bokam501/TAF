package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;

import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.Evidence;
import com.hcl.atf.taf.model.EvidenceType;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.PerformanceLevel;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductLocale;
import com.hcl.atf.taf.model.ProductMaster;
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
import com.hcl.atf.taf.model.dto.ISERecommandedTestcases;
import com.hcl.atf.taf.model.dto.TestCaseDTO;
import com.hcl.atf.taf.model.dto.TestStepExecutionResultDTO;
import com.hcl.atf.taf.model.dto.WorkPackageBuildTestCaseSummaryDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDemandProjectionDTO;
import com.hcl.atf.taf.model.dto.WorkPackageExecutionPlanUserDetails;
import com.hcl.atf.taf.model.dto.WorkPackageTestCaseExecutionPlanStatusDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTestCaseSummaryDTO;
import com.hcl.atf.taf.model.json.JsonMetricsMasterProgramDefects;
import com.hcl.atf.taf.model.json.JsonMetricsProgramExecutionProcess;
import com.hcl.atf.taf.model.json.JsonMetricsTestCaseResult;
import com.hcl.atf.taf.model.json.JsonTestCycle;
import com.hcl.atf.taf.model.json.JsonTestFactoryResourceReservation;
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
import com.hcl.atf.taf.report.xml.beans.WorkPackageBean;

public interface WorkPackageService {
	List<WorkPackageTestCase> listWorkPackageTestCases(int workPackageId);
	List<WorkPackageTestCase> listWorkPackageTestCases(int workPackageId, int jtStartIndex, int jtPageSize);
	int totalRecordsCountForWorkPackageTestCases(int workPackageId);
	int initializeWorkPackageWithTestCases(int workPackageId);
	
	WorkPackageTestCase updateWorkPackageTestCase(WorkPackageTestCase workPackageTestCaseFromUI, JsonWorkPackageTestCase jsonWorkPackageTestCase);
	
	
	WorkPackage getWorkPackageByWorkPackageName(String workPackageName);
	boolean isWorkPackageExistingByName(WorkPackage workPackage);
	WorkPackage getWorkPackageById(Integer workPackageId);
	int getTotalRecordsOfWorkPackages();
	WorkPackage getWorkPackageById(int workPackageById);

	
	void addWorkPackage (WorkPackage workPackage);
	void updateWorkPackage (WorkPackage workPackage);
	void deleteWorkPackage (int workPackageId);
	
	List<WorkPackage> list(); 
	List<WorkPackage> list(int startIndex, int pageSize);  
	List<WorkPackage> listWorkPackages(int productBuildId, int status);
	List<WorkPackage> listWorkPackages(int productBuildId);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlan(Map<String, String> searchString,int workPackageId, int jtStartIndex,int jtPageSize,String testLeadId,String testerId,String envId,int localeId,String plannedExecutionDate,String dcId ,String executionPriority,int status);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlan(int workPackageId, int jtStartIndex,int jtPageSize,UserList user,String plannedExecutionDate);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlan(int workPackageId, int jtStartIndex,int jtPageSize,ProductUserRole productUserRole);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanTestLead(int workPackageId, int jtStartIndex,int jtPageSize,UserList user);
	int totalRecordsCountForWorkPackageTestCasesExecutionPlan(int workPackageId,UserList user);
	List<UserList> userListByRole(String role);
	int getTotalRecordsOfUserByRole(String role);
	void  updateWorkPackageTestCaseExecutionPlan(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlanFromUI);
	Environment getEnvironmentById(int environmentId);

	List<Environment> getWorkPackageTestCasesExecutionPlanEnvironments(int workPackageId, int testCaseId);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanByWorkPackageId(int workPackageId);

	// Added for - Tester's view of 'Workpackage TestCase Plan view/edit'
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlanByTesterId(int testerId, int workPackageId, int jtStartIndex,int jtPageSize);
	int totalRecordsCountForWorkPackageTestCaseExecutionPlanByTesterId(int testerId, int workPackageId);
	
	// Added for - Demand Projection
	List<JsonWorkPackageDemandProjection> listWorkPackageDemandProjection(int workPackageId, int weekNo);
	int totalRecordsCountForWorkPackageDemandProjection(int workPackageId);
	JsonWorkPackageDemandProjection updateWorkPackageDemandProjection(JsonWorkPackageDemandProjection jsonWorkPackageDemandProjection);
	
	TestCaseExecutionResult getTestCaseExecutionResultByID(int testCaseExecutionResultId);
	int seedWorkPackageWithNewTestCases(List<TestCaseList> newTestCases,int workPackageId);
	List<WorkShiftMaster> listAllWorkShift();
	WorkShiftMaster getWorkShiftById(int shiftId);
	WorkShiftMaster getWorkShiftByName(String shiftName);
	List<ProductUserRole> userListByProductRole( Integer productId,String userRoleId) ;
	Integer getProductIdByWorkpackage(int workPackageId);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanByEnv(int workPackageId, int jtStartIndex,int jtPageSize,int envId);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanTestLeadByEnv(int workPackageId, int jtStartIndex,int jtPageSize,UserList user,int envId);
	List<JsonWorkPackageDailyPlan> listWorkPackageDemandDayWisePlan(int workPackageId, int weekNo);
	
	Set<RunConfiguration> getEnvironmentMappedToWorkpackage(int workpackageId);
	List<WorkPackage> listWorkPackagesByUserId(int userId);
	WorkPackage mapWorkpackageEnv(int workpackageId,int envId,String action);
	Set<ProductLocale> getLocaleMappedToWorkpackage(int workpackageId);
	WorkPackage mapWorkpackageLocale(int workpackageId,int localeId,String action);
	WorkPackageTestCase updateWorkPackageTestCase(Set<WorkPackageTestCase> workPackageTestCaseList,List<Environment> environmentList,Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListFromDB,Integer workpackageId);
	WorkPackageTestCase deleteWorkPackageTestCase(Set<WorkPackageTestCase> workPackageTestCaseList,List<Environment> environmentList,Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListFromDB);
	List<ProductLocale> getWorkPackageTestCasesExecutionPlanLocales(int workPackageId, int testCaseId);	
	List<JsonWorkPackageStatusSummary> listWorkPackageStatusSummary(List<ProductBuild> listOfProductBuilds,ProductUserRole productUserRole);
	WorkPackageTestCase updateWorkPackageTestCaseLocale(Set<WorkPackageTestCase> workPackageTestCaseList, List<ProductLocale> localeList,Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListFromDB) ;
	WorkPackageTestCase deleteWorkPackageTestCaseLocale(Set<WorkPackageTestCase> workPackageTestCases,List<ProductLocale> localeRemovedList,Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListFromDB);
	List<WorkPackageTestCaseExecutionPlan> listCompletedOrNotCompletedTestCasesOfworkPackage(int workPackageId, int jtStartIndex, int jtPageSize,int executedStatusId,ProductUserRole productUserRole);
	void updateWorkPackageTestCaseExecutionPlan(String[] wptcepLists,Integer testerId,Integer testLeadId,String plannedExecutionDate,Integer executionPriorityId,Integer shiftId);
	WorkPackageTestCaseExecutionPlanStatusDTO listWorkpackageTestCaseExecutionPlanningStatus(int workPackageId);
	List<WorkPackageDemandProjectionDTO>  listWorkpackageDemandProdjectionByWorkpackageId(Date startDate, Date endDate, Integer testFactoryLabId, Integer testFactoryId, Integer productId, Integer workPackageId, Integer shiftId, String viewType);
	List<WorkPackage> getWorkPackagesByProductId(int productId);
	int getWorkPackagesCountByProductId(int productId);
	List<TestFactoryResourceReservation>  getUserByBlockedStatus(int workPackageId, int roleId,String plannedExecutionDate,int shiftId);
	JTableResponseOptions  getUserByBlockedStatusForWPAssociation(int workPackageId, int roleId,String plannedExecutionDate,int shiftId);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanTesterByEnv(int workPackageId, int jtStartIndex,int jtPageSize,UserList user,int envId);
	WorkPackageTestCaseExecutionPlan listWorkPackageTestCasesExecutionPlan(int wptcepId,int testcaseId);
	List<TestExecutionResultBugList> listDefectsByTestcaseExecutionPlanId(int tcerId,int jtStartIndex,int jtPageSize);
	void addTestCaseDefect(TestExecutionResultBugList testExecutionResultBugList);
	void updateTestCaseDefect(
			TestExecutionResultBugList testExecutionResultBugList);
	List<WorkFlow> getworkFlowListByEntity(int entityType);
	List<TestCaseExecutionResult> listResultsByTestcaseExecutionPlanId(int tcerId);
	void updateTestCaseResults(TestCaseExecutionResult testCaseExecutionResult);
	List<WorkPackage> getUserAssociatedWorkPackages(int roleId,int userId);
	List<ExecutionPriority> getExecutionPriority();
	ExecutionPriority getExecutionPriorityById(int executionPriorityId);
	List<WorkShiftMaster>  listActualShiftByPlannedDateAndTestFactory(Integer testFactoryId,String actualExecutionDate);
	ProductMaster getProductMasterByWorkpackageId(int workPackageId);
	
	WorkPackageTestCaseSummaryDTO listWorkPackageTestCaseExecutionSummary(Integer workPackageId);
	void addWorkPackageDemandProjection(WorkPackageDemandProjection workPackageDemandProjection);
	List<JsonWorkPackageDemandProjection> listWorkPackageDemandProjectionByDate(int workPackageId, int shiftId, Date date);
	List<UserRoleMaster> getUserRolesForDemandProjection();
	WorkPackageDemandProjection getWorkPackageDemandProjectionById(int workPackageDemandProjectionId);
	void deleteWorkPackageDemandProjection (int wpDemandProjectionId);
	void updateWorkPackageDemandProjection(WorkPackageDemandProjection wpDemandProjection);
	
	WorkFlow getWorkFlowByEntityIdStageId(Integer entityId,Integer stageId);
	void addWorkFlowEvent(WorkFlowEvent workFlowEvent);
	WorkPackageTestCaseExecutionPlan getWorkpackageTestcaseExecutionPlanById(int workPackageTestCaseExecutionPlanId);

	EntityMaster  getEntityMasterById(Integer entityId);
	void addEvidence(Evidence evidence);
	Evidence getEvidenceById(int id);
	void deleteEvidence (Evidence evidence);
	List<Evidence> testcaseListByEvidence(Integer tcerId,String type);
	List<TestStepExecutionResult> listTestStepPlan(Integer testcaseId,Integer tcerId);
	List<TestStepExecutionResult> listTestStepResultByCaseExecId(Integer tcerId);//Added for Bug 844. Get the TestStepExecResults in order of TestStepExecResultId
	TestStepExecutionResult getTestStepExecutionResultById(Integer testStepId);
	void updateTestStepExecutionResult(TestStepExecutionResult testStepExecutionResult);
	WorkFlow getWorkFlowByEntityIdWorkFlowId(Integer entityId,Integer workFlowId);
	List<WorkPackageTestCaseSummaryDTO> listWorkPackageTestCaseExecutionSummaryByDate(Integer workPackageId);
	WorkPackage cloneWorkpackage(Map<String, String>  mapData,UserList user,String copyDataType,String mongoDBAvailability);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlan(Integer workPackageId,UserList user,String plannedExecutionDate,String filter,WorkpackageRunConfiguration wpRunConfigObj);
	List<PerformanceLevel> getPerformanceRating();
	List< UserList> getAllocatedUserListByRole(Integer workpackageId,Integer roleId);
	WorkPackageTestCase getWorkpackageTestCaseById(Integer tcWpId);
	/*Bulk operation for TestSuite*/
	WorkPackageTestSuite getWorkpackageTestSuiteById(Integer tsWpId);
	/*Bulk operation for TestSuite End*/
	
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanByWorkPackageIdGroupByTestcase(int workPackageId,String envId,String executionPriority,String result,int jtStartIndex,int jtPageSize,int sortBy);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanByWorkPackageId(int workPackageId,int testcaseId);
	void updateTestCaseExecutionResult(TestCaseExecutionResult testCaseExecutionResult);
	void addTestCaseConfiguration(TestCaseConfiguration testCaseConfiguration);
	List<WorkPackage> listWorkPackagesByUserIdAndPlannedExecutionDate(
			int userId, String plannedExecutionDate);
	ExecutionPriority  getExecutionPriorityByName(String priorityValue);
	int addWorkPackageTestcaseExecutionPlan(List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList);
	void deleteTestCaseConfigurationByWPTCEID(Integer wptcep);
	void deleteWorkPackageTestCaseExecutionPlan(WorkPackageTestCaseExecutionPlan wptcep);
	WorkPackage mapWorkpackageEnvCombination(int workpackageId,int envId,String action);
	Float listWorkpackageDemandProdjectionByDate(Date selectedDate, Integer testFactoryLabId, Integer shiftTypeId);
	List<TestCaseConfiguration> listTestCaseConfigurations(int workpackageId);
	void deleteTestStepResult(Integer testCaseExecutionResultId);
	int initializeWorkPackageWithTestSuite(WorkPackage workPackage,List<TestSuiteList> productTestSuite) ;
	JTableResponse listWorkpackageTestSuite(Integer workPackageId,Integer jtStartIndex,Integer jtPageSize);
	WorkPackageTestSuite updateWorkPackageTestSuite(WorkPackageTestSuite workPackageTestSuiteFromUI, JsonWorkPackageTestSuite jsonWorkPackageTestSuite);
	List<TestCaseDTO> listWorkPackageTestCasesExecutionPlanByWorkPackageIdGroupByTestcase(int workPackageId,String envId,String executionPriority,String result,int jtStartIndex,int jtPageSize,int sortBy,int testcaseId);
	WorkPackage deleteRunConfigurationWorkpackage(Integer workpackageId,Integer runconfigId,String type);
	WorkpackageRunConfiguration addRunConfigurationWorkpackage(Integer workpackageId,Integer runconfigId,String type);
	WorkpackageRunConfiguration getWorkpackageRunConfiguration(Integer workpackageId,Integer runconfigId,String type);
	List<WorkpackageRunConfiguration> getWorkpackageRunConfigurationList(Integer workpackageId,Integer runconfigId,String type);
	void mapWorkpackageWithTestSuite(Integer workPackageId,Integer testSuiteId,String type);
	int addNewWorkPackageTestSuite(List<WorkPackageTestSuite> workPackageTestSuites);

	WorkpackageRunConfiguration getWorkpackageRunConfigurationByWPTCEP(Integer id);
	WorkPackage addWorkpackageToTestRunPlan(TestRunPlan testRunPlan,Map<String,String> mapData,UserList user,HttpServletRequest req,TestRunPlanGroup testRunPlanGroup,WorkPackage workPackage);
	WorkPackage addWorkpackageToTestRunPlan(TestRunPlan testRunPlan,Map<String,String> mapData,UserList user,HttpServletRequest req,TestRunPlanGroup testRunPlanGroup,WorkPackage workPackage,String deviceNames);

	void mapWorkpackageWithTestCase(Integer workPackageId,Integer testcaseId,String action);
	RunConfiguration getWorkpackageRCByEnvDev(Integer workpackageId,Integer environmentCombinationId,Integer deviceId);
	List<WorkPackage> listWorkPackageBytestrunplanId(Integer testRunPlanId);
	void deleteWorkPackageTestcaseExecutionPlan(List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList);
	int getTotalRecordWPTCEP(Integer workPackageId,int status);
	
	void saveTestStepExecutionResult(List<TestStepExecutionResult> testStepExecutionResultList);
	WorkPackageTestCaseExecutionPlan  workPackageTestCasesExecutionPlanByJobId(TestRunJob testRunJob,TestCaseList testCaseList);
	WorkPackageTestSuite workPackageTestCasesByJobId(TestRunJob testRunJob);
	void addTestRunJob(RunConfiguration runConfiguration,TestSuiteList testSuiteList,WorkPackage workPackage,TestRunPlan testRunPlan);
	TestRunJob getTestRunJobByWP(WorkPackage workPackage,RunConfiguration runConfiguration);
	void mapTestRunJobTestCase(Integer testRunJobId,Integer testcaseId,String type);
	void mapTestRunJobTestSuite(Integer testRunJobId,Integer testsuiteId,String type);
	JTableResponse listWorkpackageFeature(Integer workPackageId,Integer jtStartIndex,Integer jtPageSize);
	int initializeWorkPackageWithFeature(WorkPackage workPackage,List<ProductFeature> productFeature) ;
	WorkPackageFeature updateWorkPackageFeature(WorkPackageFeature workPackageFeatureFromUI,JsonWorkPackageFeature jsonWorkPackageFeature);
	void addWorkpackageFeatureExecutionPlan(WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan);
	void mapTestRunJobFeature(Integer testRunJobId, Integer featureId, String type);
	List<WorkPackageFeatureExecutionPlan> listWorkPackageFeatureExecutionPlan(Map<String, String> searchString,Integer workPackageId,Integer  jtStartIndex,Integer jtPageSize,int status);
	void updateWorkPackageFeatureExecutionPlan(WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlanFromUI);
	WorkPackageFeatureExecutionPlan getWorkpackageFeatureExecutionPlanById(Integer rowId);
	List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestcaseExecutionPlanByWorkPackageId(int workPackageId,int testcaseId,int testSuiteId,int featureId,String type);
	void updateWorkPackageFeatureExecutionPlan(String[] wptcepLists,Integer testerId,Integer testLeadId,String plannedExecutionDate,Integer executionPriorityId,Integer shiftId);
	void addWorkpackageTestSuiteExecutionPlan(WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan);
	WorkPackageTestSuiteExecutionPlan getWorkpackageTestSuiteExecutionPlan(Integer workPackageId,Integer testSuiteId,Integer runConfigurationId);
	void deleteWorkpackageTestSuiteExecutionPlan(WorkPackageTestSuiteExecutionPlan wptsp);
	int getTotalRecordWPFEP(Integer workpackageId,int status);
	int getTotalRecordWPTSEP(Integer workpackageId,int status);
	List<WorkPackageTestSuiteExecutionPlan> listWorkPackageTestSuiteExecutionPlan(Map<String, String> searchStrings,Integer workpackageId, Integer jtStartIndex,Integer jtPageSize,int status);
	void updateWorkPackageTestSuiteExecutionPlan(WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlanFromUI);
	WorkPackageTestSuiteExecutionPlan getWorkpackageTestSuiteExecutionPlanById(Integer rowId);
	void updateWorkPackageTestSuiteExecutionPlan(String[] wptcepLists,Integer testerId,Integer testLeadId,String plannedExecutionDate,Integer executionPriorityId,Integer shiftId);
	WorkPackageTestCaseExecutionPlanStatusDTO listWorkpackageFeatureExecutionPlanningStatus(Integer workPackageId);
	WorkPackageTestCaseExecutionPlanStatusDTO listWorkpackageTestSuiteExecutionPlanningStatus(Integer workPackageId);
	WorkPackageTestCaseExecutionPlan addWorkpPackageTestCaseExecutionPlans(TestCaseList testCaseList,TestSuiteList testSuiteList,WorkPackage workPackage,WorkpackageRunConfiguration wpRunConfiguration,ProductFeature feature,String sourceType,TestRunJob testRunJob);
	WorkPackageFeatureExecutionPlan getWorkpackageFeatureExecutionPlan(Integer workPackageId,Integer productFeatureId,Integer runConfigurationId);
	void mapWorkpackageWithFeature(Integer workPackageId,Integer featureId, String action) ;
	void deleteWorkpackageFeatureExecutionPlan(WorkPackageFeatureExecutionPlan wpfep);
	JsonWorkPackageTestCaseExecutionPlanForTester listWorkPackageTestCaseExecutionSummaryReport(
			Integer workPackageId);
	List<WorkPackage> listActiveWorkPackages(Integer productId);
	WorkPackage getWorkPackageByProductBuildId(Integer workPackageId);	
	int getWorkPackagesCountByBuildId(int productBuildId);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanBywpId(int workPackageId,String filter);
	List<RunConfiguration> listRunConfigurationBywpId(Integer workPackageId);
	WorkPackageFeature getWorkpackageFeaturePlanById(Integer tsWpId);
	WorkPackageTestCase getWorkpackageTestCaseByPlanId(Integer tcWpId);
	WorkPackageTestSuite getWorkpackageTestSuiteByPlanId(Integer tsWpId);
	List<WorkPackageExecutionPlanUserDetails> listWorkpackageExecutionPlanUserDetails(Integer workPackageId,String plannedExecutionDate,String role);
	List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestcaseExecutionPlanByWorkPackageId(int workPackageId,int testcaseId,int testSuiteId,int featureId,String type,int runConfigId);
	WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlanByJobId(TestRunJob testRunJob,TestSuiteList testSuiteList);
	List<WorkpackageRunConfiguration> getTestRunJobByWPTCEP(Integer workPackageId,Integer  userId,String plannedExecutionDate);
	boolean isWorkPackageExecutionAvailable(WorkPackage workPackage);
	List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestCaseExecutionPlanByTestRunJob(TestRunJob testRunJob,int testSuiteId);
	List<TestExecutionResultBugList>  listDefectsByWorkpackageId(int workpackageId);
	int addWorkpackageToTestRunPlanGroup(TestRunPlanGroup testRunPlanGroup,Map<String,String> mapData,UserList user,HttpServletRequest req);
	TestRunPlan getNextTestRunPlan(TestRunPlanGroup testRunPlanGroup,TestRunPlan testRunPlan);
	void workpackageExxecutionPlan(WorkPackage workPackage,TestRunPlan testRunPlan,HttpServletRequest req);
	JSONArray listFeaturesByTestRunBeds(int workpackageId,List<RunConfiguration> runConfigList);
	Set<RunConfiguration>  listTestBedByFeature(int featureId,int workpackageId,int status);
	List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestcaseExecutionPlan(int featureId,int workpackageId,int runConfigId,String type,int testSuiteId,int testcaseId);
	Set<RunConfiguration> listTestBedByTestSuite(int testsuiteId,int workpackageId,int status);
	List<TestRunJob> getTestRunJobByBuildID(Integer productBuildId, Integer workPackageType,Integer jtStartIndex, Integer jtPageSize);
	TestRunJob getTestRunJobById(int testRunJobId);
	WorkPackage addWorkpackageToClonedProductBuild(ProductBuild clonedProductBuild,UserList user,HttpServletRequest req, WorkPackage workPackageToBeCloned);
	void copyPlanningDetailsInClonedWorkPacakge(String mONGODB_AVAILABLE, Integer workPackageId,WorkPackage clonedWp, UserList user);
	List<TestExecutionResultBugList> listDefectsByTestcaseExecutionPlanIdByApprovedStatus(int productId, int productVersionId, int productBuildId,int workPackageId, int approveStatus, Date startDate, Date endDate,Integer raisedByUser, Integer jtStartIndex,Integer jtPageSize);
	EvidenceType getEvidenceTypeById(Integer evidenceTypeId);
	List<EvidenceType> getEvidenceTypes();
	List<WorkPackage> getActiveWorkpackagesByProductBuildId(Integer productBuildId);
	WorkPackage getWorkPackageByIdWithMinimalnitialization(int workPackageId);
	int totalWorkPackageDemandProjectionCountByWpId(int workPackageId,int ShiftId, Date date);
	List<TestCaseList> getSelectedTestCasesFromTestRunJob(int testRunListId);
	TestExecutionResultBugList createBug(TestStepExecutionResult testStepExecutionResult, ProductBuild productBuild);
	void saveTestStepExecutionResult(TestStepExecutionResult testStepExecutionResultList);
	List<TestExecutionResultBugList> getTestRunJobDefects(int testRunJobId);
	HashMap<Integer,JsonMetricsProgramExecutionProcess> getWorkpackageTestcaseExecutionPlanByDateFilter(int status, Date startDate,Date endDate);
	HashMap<Integer,JsonMetricsMasterProgramDefects> getTotalBugListByStatus(
			Integer status);
	 HashMap<Integer,JsonMetricsTestCaseResult>getTestcaseExecutionResultList();
	HashMap<Integer, JsonMetricsProgramExecutionProcess> getWorkpackageTestcaseExecutionPlanForResourceByDateFilter(int i, Date startDate, Date currentDate);
		List<WorkpackageRunConfiguration> getWorkpackageRunConfigurationOfWPwithrcStatus(Integer workpackageId, Integer runConfigStatus);
		void workpackageExxecutionPlan(WorkPackage workPackage,TestRunPlan testRunPlan,HttpServletRequest req,String deviceNames,String testcaseNames);
		List getTestRunResultFromListAndDetailViews(int testRunNo,String deviceId);
		List getTestRunResultFromViews(int testRunNo);
		List<TestCaseExecutionResult> listAllTestCaseExecutionResult(int startIndex, int pageSize, Date startDate, Date endDate);
		Integer countAllTestCaseExecutionResult(Date startDate, Date endDate);
		Integer countAllTestRunJob(Date startDate,Date endDate);
		Integer getWorkPackageTestCasesCount(int workPackageId, int jtStartIndex, int jtPageSize);
		Integer getProductIdByWorkpackageId(int workPackageId);
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
				String testerId, String envId, int localeId, String plannedExecutionDate, String dcId,
				String executionPriority, int status);
		List<TestRunJob> listAllTestRunJob(int startIndex, int pageSize,Date startDate, Date endDate);
		List<Object[]> getTescaseExecutionHistory(int testCaseId, int workPackageId, String dataLevel, Integer jtStartIndex, Integer jtPageSize);
		Integer getWPTCEPCountOfATestCaseId(int testCaseId, int jtStartIndex,
				int jtPageSize);
		Integer getWorkPackageTestCaseOfTCID(int testcaseId);
		int getWPTypeByTestRunPlanExecutionType(int workPackageId);
		WorkPackageTestCaseExecutionPlan listWorkPackageTestCasesExecutionPlanOfTCId(
				int testcaseId);
		List<WorkPackageBuildTestCaseSummaryDTO> listWorkPackageTestCaseExecutionBuildSummary(Integer workPackageId,Integer productBuildId);
		List<JsonWorkPackageTestCaseExecutionPlanForTester> getWPTCExecutionSummaryByProdIdBuildId(
			Integer	testFactoryId,Integer productId, Integer productBuildId, Integer jtStartIndex, Integer jtPageSize , String filter);
		Date getFirstExecutedTCStartTimeofWP(int workPackageId);
		Date getLastExecutedTCEndTimeofWP(int workPackageId);
		void setUpdateWPActualStartDate(int workPackageId, Date startTime);
		void setUpdateWPActualEndtDate(int workPackageId, Date startTime);
		List<TestCaseList> listTestCaseOfTRJob(int testRunJobId);
		List<TestCaseDTO> listTestCaseExecutionDetailsOfTRJob(
				int testRunJobId, int workPackageId);
		List<TestStepExecutionResultDTO> listTestStepExecutionDetailsOfTCExecutionId(
				int wptcepId);
		String getDeviceNameByTestRunJob(int testRunJobId);
		WorkPackageTestCaseExecutionPlan listWorkPackageTestCasesExecutionPlanOfTCExeId(
				int wptcepId);
		void updateWPStatus(TestRunJob testRunJob, Integer workflowStageIdExecution, Integer workflowStageIdExecution2, Integer userId, Boolean isNextTPAvailable);
		List<WorkPackageTestCaseExecutionPlan> getNotExecutedTestCaseListByWpId(Integer wpid);
		List<WorkPackageTestCaseExecutionPlan> getNotExecutedTestCaseListByJobId(Integer jobid);
		void updateTestRunJob(TestRunJob testRunJob);
		EntityMaster getEntityMasterByName(String entityMasterName);
		Integer getWPTCEPCountOfWPId(Integer testCaseId, Integer workPackageId);
		List<JsonWorkPackageDemandProjection> listWorkPackageDemandProjectionByWorkpackageWeekAndYear(int workPackageId, int shiftId, Set<Integer> recursiveWeeks,Integer workYear,Integer skillId,Integer roleId, Integer userTypeId);
		List<WorkPackageDemandProjection> getWorkPackageDemandProjectionByGroupDemandId(Long groupDemandId);
		void deleteWorkPackageDemandProjectionWeeklyByGroupDemandId(Long groupDemandId);
		void addTestfactoryResourceReservationWeekly(TestFactoryResourceReservation testFactoryResourceReservation);
		List<JsonTestFactoryResourceReservation> listWorkPackageResourceReservationByWorkpackageWeekAndYear(Integer workPackageId, Integer shiftId,Integer reservationWeek, Integer reservationYear,Integer resourceId);
		void deleteWorkPackageDemandProjection(WorkPackageDemandProjection workPackageDemandProjection);
		Integer countAllResourceDemands(Date startDate, Date endDate);
		List<WorkPackageDemandProjection> listAllResourceDemands(int i,int pageSize, Date startDate, Date endDate);
		TestFactoryResourceReservation getTestFactoryResourceReservationById(Integer reservationId);
		Integer countAllReservedResources(Date startDate, Date endDate);
		List<TestFactoryResourceReservation> listAllReservedResources(int i,int pageSize, Date startDate, Date endDate);
		WorkPackage getLatestWorkPackageByproductId(Integer productId, String workPackageName);
		void createWorkpackageExecutionPlanForExistingWorkPackage(WorkPackage workPackage, TestRunPlan testRunPlan, HttpServletRequest req, String deviceNames, String testcaseNames);
		List<JsonWorkPackageTestCaseExecutionPlanForTester> getWPTCExecutionSummaryByTestRunPlanId(Integer testRunPlanId , Integer jtStartIndex, Integer jtPageSize);
		List<TestCaseList> getSelectedTestCasesFromTestRunJobTestSuite(int testRunListId, Integer testSuiteId);
		List<ISERecommandedTestcases> getIntelligentTestPlanFromISE(TestRunPlan testRunPlan);
		List<ISERecommandedTestcases>  addRecommendationTestcasePlan(Integer productId,Integer buildId);		
		WorkPackage executeTestRunPlanWorkPackageUnattendedMode(TestRunPlan testRunPlan, Map<String, String> mapData,UserList user, HttpServletRequest req,TestRunPlanGroup testRunPlanGroup, WorkPackage workPackage,String deviceNames);
		List<WorkFlowEvent> workFlowEventlist(int typeId);
		String getExecutionTimeForEachWPByTPId(Integer testRunPlanId);
		Integer getWPTCExecutionSummaryCount(Integer testFactoryId,Integer productId, Integer productBuildId);
		String getAnalyticsRecommendedTestcasesUnattended(Integer testRunPlanId, Integer testSuiteId, Map<String, String> mapData);
		Set<String> workFlowEvents(int typeId);
		WorkPackageTestCaseExecutionPlan workPackageTestCasesExecutionPlanByJobId(TestRunJob combinedResultsReportingJob, TestRunJob execuingTestRunJob,
				TestCaseList testCaseList);
		Integer getProductBuildsTestedCount(Integer productBuildId);
		List<TestCaseList> getAlreadyExecutedTestcasesForJob(TestRunJob testRunJob, Integer testSuiteId);
		Integer getExecutionTCCountForJob(Integer testRunJobId);
		public WorkPackageBean getWorkPackageBeanById(Integer wpId);
		public void workpackageSelectiveExecutionPlan(WorkPackage workPackage, TestRunPlan testRunPlan, Map<RunConfiguration, String> testConfigs, HttpServletRequest req, String deviceNames);
		List<WorkPackage> listAllWorkPackages(Integer limit, Integer offset);
		List<JsonWorkPackageTestCaseExecutionPlanForTester> getWPTCExecutionSummaryByProdIdBuildIdWorkpackageId(Integer testFactoryId,Integer productId, Integer productBuildId,Integer workpackageId, Integer jtStartIndex,Integer jtPageSize  , String filter);
		WorkPackage getworkpackageByTestPlanId(Integer testPlanId, Integer productBuild);
		WorkPackage getWorkPackageByProductBuild(Integer productBuildId);
		List<ISERecommandedTestcases> getIntelligentTestPlanFromISE(TestRunPlan trplan, Integer productBuildId);
		void addTestCycle(TestCycle testCycle);
		List<TestCycle> getTestCycleList(Integer testFactoryId,Integer productId, Integer productVersionId,Integer testPlanGroupId, int jtStartIndex, int jtPageSize);
		List<JsonWorkPackageTestCaseExecutionPlanForTester> getWPTCExecutionSummaryByTestCycleId(Integer testCycleId, int jtStartIndex,int jtPageSize);
		Integer getTotalWPCount(Integer testFactoryId, Integer productId,Integer productBuildId);
}
