package com.hcl.atf.taf.mongodb.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.DefectCollection;
import com.hcl.atf.taf.model.EntityRelationship;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductFeatureProductBuildMapping;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseProductVersionMapping;
import com.hcl.atf.taf.model.TestCaseScript;
import com.hcl.atf.taf.model.TestCaseScriptHasTestCase;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;
import com.hcl.atf.taf.mongodb.model.ActivityCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ActivityMongo;
import com.hcl.atf.taf.mongodb.model.DPAWorkbookCollectionMongo;
import com.hcl.atf.taf.mongodb.model.DefectCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ReviewRecordCollectionMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseDefectsMasterMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseExecutionResultMongo;
import com.hcl.atf.taf.mongodb.model.TrendCollectionMongo;
import com.hcl.atf.taf.mongodb.model.UtilizationCollectionMongo;
import com.hcl.ilcm.workflow.model.WorkflowEvent;

public interface MongoDBService {

	boolean addTestCaseExecutionResult(Integer testCaseResultId);
	boolean addTestCaseExecutionResult(TestCaseExecutionResult testCaseResult);
	boolean addTestCaseExecutionResult(List<TestCaseExecutionResult> testCaseResults, Date lastSyncDate);
	boolean addAllTestCaseExecutionResult(Date startDate, Date endDate);

	boolean addProductToMongoDB(Integer productId);
	boolean addProductToMongoDB(ProductMaster product);
	boolean addProductToMongoDB(List<ProductMaster> products, Date lastSyncDate);
	boolean addAllProduct(Date startDate,Date endDate);
	
	boolean addProductVersionToMongoDB(Integer versionId);
	boolean addProductVersionToMongoDB(ProductVersionListMaster productVersion);
	boolean addProductVersionToMongoDB(List<ProductVersionListMaster> productVersions, Date lastSyncDate);
	boolean addAllProductVersionMongoDB(Date startDate,Date endDate);
	
	boolean addProductBuildToMongoDB(Integer buildId);
	boolean addProductBuildToMongoDB(ProductBuild productBuild);
	boolean addProductBuildToMongoDB(List<ProductBuild> productBuild, Date lastSyncDate);
	boolean addAllProductBuildToMongoDB(Date startDate,Date endDate);
	
	boolean addProductTestCaseToMongoDB(Integer testCaseId);
	boolean addProductTestCaseToMongoDB(TestCaseList testCaseList);
	boolean addProductTestCaseToMongoDB(List<TestCaseList> testCaseList, Date lastSyncDate);
	boolean addAllProductTestCaseToMongoDB(Date startDate,Date endDate);
	
	boolean addTestRunJobToMongoDB(Integer testRunJobId);
	boolean addTestRunJobToMongoDB(TestRunJob testRunJob);
	boolean addTestRunJobToMongoDB(List<TestRunJob> testCaseResults, Date lastSyncDate);
	boolean addAllTestRunJobToMongoDB(Date startDate,Date endDate);
	
	boolean addTestFactoryToMongo(Integer testFactoryId);
	boolean addTestFactoryToMongo(TestFactory testfactory);
	boolean addTestFactoryToMongo(List<TestFactory> testfactorys, Date lastSyncDate);
	boolean addAllTestFactoryToMongo(Date startDate,Date endDate);
	
	
	boolean addTestStepsToMongoDB(Integer testStepId);
	boolean addTestStepsToMongoDB(TestCaseStepsList testStep);
	boolean addTestStepsToMongoDB(List<TestCaseStepsList> testStep, Date lastSyncDate);
	boolean addAllTestStepsToMongoDB(Date startDate,Date endDate);
	
	void pushAlliLCMDataToMongoDB(Date startDate,Date endDate);
	
	boolean addProductFeature(Integer featureId);
	boolean addProductFeature(ProductFeature feature);
	boolean addAllProductFeatures(Date startDate, Date endDate);
	boolean addProductFeatures(List<ProductFeature> features, Date lastSyncDate);
	boolean addBug(Integer bugId);
	boolean addBug(TestExecutionResultBugList bug);
	boolean addBugs(List<TestExecutionResultBugList> bugs, Date lastSyncDate);
	boolean addAllBugs(Date startDate,Date endDate);
	
	boolean addProductUserRole(Integer productUserRoleId);
	boolean addProductUserRole(ProductUserRole productUserRole);
	boolean addProductUserRole(List<ProductUserRole> productUserRoleList, Date lastSyncDate);
	boolean addAllProductUserRole(Date startDate, Date endDate);
	
	
	boolean addProductTeamResources(Integer productTeamResourcesId);
	boolean addProductTeamResources(ProductTeamResources productTeamResources);
	boolean addAllProductTeamResources(Date startDate, Date endDate);
	
	
	boolean addWorkPackage(Integer workPackageId);
	boolean addWorkPackage(WorkPackage workPackage);
	boolean addWorkPackages(List<WorkPackage> workPackages, Date lastSyncDate);
	boolean addAllWorkPackages(Date startDate, Date endDate);	
	
	boolean addActivityTaskToMongoDB(Integer activityTaskId);	
	boolean addAllActivityTaskToMongoDB(Date startDate, Date endDate);
	boolean addActivityTaskToMongoDB(ActivityTask activityTask);
	void deleteActivityTaskFromMongoDb(Integer activityTaskId);
	
	boolean addWorkflowEventToMongoDB(Integer workflowEventId);
	boolean addWorkflowEventToMongoDB( WorkflowEvent workflowEvent);
	boolean addAllWorkflowEventsToMongoDB(Date startDate,Date endDate);
	
	
	boolean addTestSuiteToMongoDB(Integer testSuiteId);
	boolean addTestSuiteToMongoDB( TestSuiteList testSuite);
	boolean addAllTestSuiteToMongoDB(Date startDate,Date endDate);
	
	boolean addResourceDemandToMongoDB(Integer demandId);
	boolean addResourceDemandToMongoDB( WorkPackageDemandProjection workPackageDemandProjection);
	boolean addAllResourceDemandToMongoDB(Date startDate,Date endDate);
	void deleteResourceDemandFromMongoDb(Integer demandId);
	
	boolean addReseveredResourceToMongoDB(Integer reservationId);
	boolean addReseveredResourceToMongoDB( TestFactoryResourceReservation testFactoryResourceReservation);
	boolean addAllReseveredResourceToMongoDB(Date startDate,Date endDate);
	void deleteReseveredResourceFromMongoDb(Integer reservedId);
	
	boolean addClarificationToMongoDB(Integer clarificationId);
	boolean addClarificationToMongoDB(ClarificationTracker clarificationTracker);
	boolean addAllClarificationToMongoDB(Date startDate,Date endDate);
	
	boolean addActivityTaskToMongoDB(ActivityTask activityTask, Integer productId,
			String productName, Integer productBuildVersionId,
			String productBuildVersionName, Integer productBuildId,
			String productBuildName, Integer activityWorkPackageId,
			String activityWorkPackageName, Integer testFactoryId, String testFactoryName, Integer customerId, String customerName);
	
	
	Float getScheduleVarienceData(List<Integer> productIdList);
	Float getProductQualityIndexData(List<Integer> productIdList, List<String> statusList);
	
	List<ReviewRecordCollectionMongo> getReivewRecordMongoCollectionList(Date endDate, String testFactoryName, String productName, Set<ProductUserRole> userProducts);
	List<DefectCollectionMongo> getDefectMongoCollectionList(Date endDate, String collectionName, String testFactoryName, String productName, Set<ProductUserRole> userProducts);
	List<ActivityCollectionMongo> getActivityMongoCollectionCountByDateFilter(Date startDate, Date currentDate, String testFactoryName, String collectionName, String productName, Set<ProductUserRole> userProducts);
	float getActivityMongoCollectionListForScheduleVariance(Date startDate,Date currentDate, String productName, Set<ProductUserRole> userProducts);
	float getActivityMongoCollectionListForScheduleVarianceAvg(Date startDate,Date currentDate, String testFactoryName, String productName, Set<ProductUserRole> userProducts);
	List<TestCaseExecutionResultMongo> getTestCaseExecutionMongoCountByDateFilter(Date startDate, Date currentDate, String testFactoryName, String productName, Set<ProductUserRole> productSet);
	List<TestCaseDefectsMasterMongo> getTestCaseDefectsMasterMongoList(String testFactoryName, String productName, Set<ProductUserRole> userProducts);
	
	List<UtilizationCollectionMongo> getUtilizationMongoCollectionList(Date startDate, Date currentDate, String testFactoryName);
	boolean pushToMongoDB(Integer collectionType, Date startDate,Date endDate);
	Date getStartDate(Integer summaryValue);
	List<JSONObject> getSingleValueMetricsList(Integer summaryValue, String testFactoryName);
	List<JSONObject> getSingleValueMetricsList(Date endDate, String testFactoryName, String metrisName);
	
	Boolean checkAvailabiltyOfMongoDB();
	
	//activity added MongoDB
	boolean addActivitytoMongoDB(Integer activityId);
	boolean addActivitytoMongoDB(Activity activity);
	boolean addAllActivitytoMongoD(Date startDate, Date endDate);
	void deleteActivityFromMongodb(Integer activityId);
	
	boolean addActivityWorkPackagetoMongoDB(Integer activityWorkPackageId);
	boolean addActivityWorkPackagetoMongoDB(ActivityWorkPackage activityWorkPackage);
	boolean addAllActivityWorkPackagetoMongoDB(Date startDate, Date endDate);
	void deleteActivityWorkPackageFromMongoDb(Integer activityWorkPackageId);
	
	List<ActivityMongo> getActivityMongoByDateFilter(Date startDate,Date currentDate, String string, String testFactoryName, String productName,Set<ProductUserRole> userProducts);
	Integer getProjectTeamMembersCount(Date startDate, Date currentDate,String testFactoryName, String productName, Set<ProductUserRole> userProducts);
	boolean addTrendCollectionMongo(TrendCollectionMongo trendValue);
	
	boolean addUserListToMongoDB(Integer userId);
	boolean addUserListToMongoDB(UserList user);
	boolean addAllUserList(Date startDate,Date endDate);
	List<JSONObject> getSingleValueListPCIScroreByDate(Date endDate, String testFactoryName);
	
	boolean addChangeRequestToMongoDB(Integer changeRequestId);
	boolean addChangeRequestToMongoDB(ChangeRequest changeRequest);
	boolean addAllChangeRequestsToMongoDB(Date startDate,Date endDate);
	List<JSONObject> getCollectionForPivotReportBasedOnCollectionName(String collectionName, int testFactoryId, int productId);
	List<String> getKeyNameForPivotReportBasedOnCollectionName(String collectionName);
	void updateParentStatusInChildColletions(Integer entityMasterId,Integer instanceId,Integer status);
	List<DPAWorkbookCollectionMongo> getDPAWorkbookMongoCollectionList(Date endDate, String testFactoryName, String productName, Set<ProductUserRole> userProducts);
	Map<String, Object> getDPAWorkbookMongoCollectionCounts(Date startDate, Date endDate, String testFactoryName, String productName, Set<ProductUserRole> userProducts);
	void addOrUpdateCustomField(Integer entityInstanceId, String fieldName, Object fieldValue, String collectionName);
	void addOrUpdateCustomField(Integer entityInstanceId, HashMap<String, Object> customFields, String collectionName);
	void addCustomFieldsOnBulkUpdate(Integer entityTypeId, List<Integer> instanceIds, String collectionName);
	List<JSONObject> getSingleValueMetricsARIDList(Date endDate, String testFactoryName);
	List<JSONObject> getMongoDefectsPmoByPmtId(Float pmtId);
	JSONObject getMongoPmoByPmtId(Float pmtId);
	boolean addChangeRequestMappingToMongoDB(EntityRelationship entityRelationship);
	boolean removeChangeRequestMappingFromMongoDB(EntityRelationship entityRelationship);
	boolean addAllChangeRequestMappingToMongoDB(Date startDate, Date endDate);
	
	boolean addProductFeatueBuildMappingtoMongoDB(Integer mappingId); 
	boolean addProductFeatueBuildMappingtoMongoDB(ProductFeatureProductBuildMapping productFeatureProductBuildMapping,String featureName,String buildName,Integer versionId,String versionName);
	boolean addAllProductFeatueBuildMappingtoMongoDB(Date startDate, Date endDate);
	
	boolean addTestCaseProductVersionMappingtoMongoDB(Integer mappingId);
	boolean addTestCaseProductVersionMappingtoMongoDB(TestCaseProductVersionMapping testCaseProductVersionMapping,String testCaseName,String versionName);
	boolean addTestCaseProductVersionMappingtoMongoDB(Date startDate, Date endDate);
	
	boolean addTestCaseScriptToMongoDB(TestCaseScript testCaseScript);
	boolean removeTestCaseScriptFromMongoDB(Integer testCaseScriptId);
	boolean addTestCaseScriptMappingToMongoDB(TestCaseScriptHasTestCase testCaseScriptHasTestCase);
	boolean removeTestCaseScriptMappingFromMongoDB(Integer testCaseScriptHasMappingId);
	boolean addAllTestCaseScriptToMongoDB(Date startDate,Date endDate);
	boolean addAllTestCaseScriptMappingToMongoDB(Date startDate, Date endDate);
	boolean addDefectCollectionToMongoDB(DefectCollection defectCollection);
	
	boolean addFeatureTestCaseMappingToMongoDB(ProductFeature productFeature, TestCaseList testCaseList);
	boolean addAllFeatureTestCaseMappingsToMongoDB(Date startDate, Date endDate);
	boolean removeFeatureTestCaseMappingFromMongoDB(Integer productFeatureId, Integer testCaseId);
}
