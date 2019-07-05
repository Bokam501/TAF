package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.mongodb.model.ActivityCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ActivityMongo;
import com.hcl.atf.taf.mongodb.model.ActivityTaskMongo;
import com.hcl.atf.taf.mongodb.model.DPAWorkbookCollectionMongo;
import com.hcl.atf.taf.mongodb.model.DefectCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ReviewRecordCollectionMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseDefectsMasterMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseExecutionResultMongo;
import com.hcl.atf.taf.mongodb.model.UtilizationCollectionMongo;

public interface DashboardSLADAO {

	
	List<ActivityTaskMongo> getScheduleVarienceData(List<Integer> productIdList);
	Float getProductQualityIndexData(List<Integer> productIdList, List<String> statusList);
	List<ReviewRecordCollectionMongo> getReivewRecordMongoCollectionList(Date endDate, String testFactoryName, String productName, Set<ProductUserRole> userProducts);
	List<DefectCollectionMongo> getDefectMongoCollectionList(Date endDate, String collectionName, String testFactoryName, String productName, Set<ProductUserRole> userProducts);
	List<ActivityCollectionMongo> getActivityMongoCollectionCountByDateFilter(Date startDate, Date currentDate, String collectionName, String testFactoryName, String productName, Set<ProductUserRole> userProducts);
	List<ActivityCollectionMongo> getActivityMongoCollectionListForScheduleVariance(Date startDate, Date currentDate, String productName, Set<ProductUserRole> userProducts);
	List<TestCaseExecutionResultMongo> getTestCaseExecutionMongoCountByDateFilter(Date startDate, Date currentDate, String testFactoryName, String productName,Set<ProductUserRole> productSet);
	List<TestCaseDefectsMasterMongo> getTestCaseDefectsMasterMongoList(String testFactoryName, String productName, Set<ProductUserRole> userProducts);
	
	List<UtilizationCollectionMongo>getUtilizationMongoCollectionList(Date startDate, Date currentDate, String testFactoryName);
	List<JSONObject> getSingleValueMetricsList(Date startDate, Date currentDate, String testFactoryName);
	List<JSONObject> getSingleValueMetricsList(Date endDate, String testFactoryName, String metricsName);
	Boolean checkAvailabiltyOfMongoDB();
	List<Activity> getScheduleVarianceforProduct(Integer productId);
	List<ActivityMongo> getActivityMongoByDateFilter(Date startDate,Date currentDate, String collectionName,String testFactoryName, String productName,Set<ProductUserRole> userProducts);
	Integer getProjectTeamMembersCount(Date startDate, Date currentDate, String testFactoryName, String productName, Set<ProductUserRole> userProducts);
	List<JSONObject> getSingleValueListPCIScroreByDate(Date endDate, String testFactoryName);
	List<ActivityCollectionMongo> getActivityMongoCollectionListForScheduleVarianceAvg(Date startDate, Date currentDate,String testFactoryName, String productName,Set<ProductUserRole> userProducts);
	void updateParentStatusInChildColletions(List<String> collectionNames,Integer entityMasterId,Integer instanceId,  Integer status, String fieldName);
	
	List<JSONObject> getCollectionForPivotReportBasedOnCollectionName(String collectionName, int testFactoryId, int productId);
	List<String> getKeyNameForPivotReportBasedOnCollectionName(String collectionName);
	void updateParentStatusInChildColletionsWhileUpdatingActive(String collectionName, Integer status,Integer immediateParentStatus, Integer integer, String fieldName);
	List<DPAWorkbookCollectionMongo> getDPAWorkbookMongoCollectionList(Date endDate, String testFactoryName, String productName,Set<ProductUserRole> userProducts);
	Map<String, Object> getDPAWorkbookMongoCollectionCounts(Date startDate, Date endDate, String testFactoryName, String productName, Set<ProductUserRole> userProducts);
	List<JSONObject> getSingleValueMetricsARIDList(Date endDate, String testFactoryName);
	List<JSONObject> getMongoDefectsPmoByPmtId(Float pmtId);
	JSONObject getMongoPmoByPmtId(Float pmtId);
	

}
