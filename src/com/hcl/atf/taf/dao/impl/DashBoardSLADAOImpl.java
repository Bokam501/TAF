package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.dao.DashboardSLADAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.model.ActivityCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ActivityMongo;
import com.hcl.atf.taf.mongodb.model.ActivityTaskMongo;
import com.hcl.atf.taf.mongodb.model.DPAWorkbookCollectionMongo;
import com.hcl.atf.taf.mongodb.model.DefectCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ProductTeamResourcesMongo;
import com.hcl.atf.taf.mongodb.model.ReviewRecordCollectionMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseDefectsMasterMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseExecutionResultMongo;
import com.hcl.atf.taf.mongodb.model.UtilizationCollectionMongo;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@Repository
public class DashBoardSLADAOImpl implements DashboardSLADAO {
	private static final Log log = LogFactory.getLog(DashBoardSLADAOImpl.class);
	
	
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	private MongoOperations mongoOperation;
	
	public DashBoardSLADAOImpl(){
    }

	
	public DashBoardSLADAOImpl(MongoOperations mongoOperation){
		this.mongoOperation = mongoOperation;
	}
	
	@Override
	@Transactional
	public List<ActivityTaskMongo> getScheduleVarienceData(List<Integer> productIdList) {
		List<ActivityTaskMongo> dashboardMongoList = new ArrayList<ActivityTaskMongo>();
		log.debug("geting data from  MOngo DB");
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection("activity_task");
			ActivityTaskMongo activityTaskMongo;
			DBCursor cursor = dBCollection.find().sort(new BasicDBObject("plannedStartDate", 1)).limit(1);  
			
			if(cursor.hasNext()) {
			    activityTaskMongo = new ActivityTaskMongo();
			    BasicDBObject basicDBObject = (BasicDBObject) cursor.next();
			    activityTaskMongo.setPlannedStartDate(basicDBObject.getDate("plannedStartDate"));
			    dashboardMongoList.add(activityTaskMongo);
			}
			
			cursor = dBCollection.find().sort(new BasicDBObject("plannedEndDate",-1)).limit(1);  
			
			if (cursor.hasNext()) {
				 activityTaskMongo = new ActivityTaskMongo();
				 BasicDBObject basicDBObject = (BasicDBObject) cursor.next();
				 activityTaskMongo.setPlannedEndDate(basicDBObject.getDate("plannedEndDate"));
				 dashboardMongoList.add(activityTaskMongo);
			} 
		
			cursor = dBCollection.find().sort(new BasicDBObject("actualEndDate",-1)).limit(1);  
			
			if (cursor.hasNext()) {
				 activityTaskMongo = new ActivityTaskMongo();
				 BasicDBObject basicDBObject = (BasicDBObject) cursor.next();
				 activityTaskMongo.setActualEndDate(basicDBObject.getDate("actualEndDate"));
				 dashboardMongoList.add(activityTaskMongo);
			} 

		} catch (Exception e) {
			log.error("Unable to retrive ", e);
		}
		return dashboardMongoList;
	}
	
	@Override
	@Transactional
	public Float getProductQualityIndexData(List<Integer> productIdList, List<String> statusList) {
		Float productQualityIndex = 0.0F;
		log.debug("geting data from  MOngo DB");
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection("defects");
			
			int totalDefectCount = 0;
			int closedDefectCount = 0;
			
			totalDefectCount = dBCollection.find().count();
			
			BasicDBObject totalConstarints = new BasicDBObject();
			BasicDBObject inConstraints = new BasicDBObject();
			BasicDBList inConstraintsValues = new BasicDBList();
			for(String status : statusList){
				inConstraintsValues.add(status);				
			}
			inConstraints.put("$in", inConstraintsValues);
			totalConstarints.put("approvalStatus", inConstraints);
			
			closedDefectCount = dBCollection.find(totalConstarints).count();
			
			productQualityIndex = (float)closedDefectCount / (float)totalDefectCount; 

		} catch (Exception e) {
			log.error("Unable to retrive ", e);
		}
		return productQualityIndex;
	}


	@Override
	@Transactional
	public List<ReviewRecordCollectionMongo> getReivewRecordMongoCollectionList(Date endDate,String testFactoryName, String productName,Set<ProductUserRole> userProducts) {
	
		List<ReviewRecordCollectionMongo> reviewRecordList = new ArrayList<ReviewRecordCollectionMongo>();
		
		log.info("geting data from  MOngo DB - review_record_collection");
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection("review_record_collection");

			BasicDBObject whereQuery = new BasicDBObject();
			BasicDBList basicDBList=new BasicDBList();
			
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			
			if(!testFactoryName.equalsIgnoreCase("ALL")){
				whereQuery.put("testFactoryName",testFactoryName);
			}
			
			if(!productName.equalsIgnoreCase("ALL")){
				whereQuery.put("productName",productName);
			}else if(userProducts!=null){
				for(ProductUserRole productUserRole : userProducts){
					basicDBList.add(productUserRole.getProduct().getProductName());
				}
			}
			if(basicDBList != null && basicDBList.size() > 0){
				whereQuery.put("productName", new BasicDBObject("$in", basicDBList));
			}
			if(endDate!=null){
				obj.add(new BasicDBObject("reviewEndDate", BasicDBObjectBuilder.start("$lte", endDate).get()));
				whereQuery.put("$and", obj);
			}
		
			
			DBCursor cursor = dBCollection.find(whereQuery);
			log.info("whereQuery   "+whereQuery);
	        int i = 1;
	        ObjectMapper mapper = new ObjectMapper();
				
	        while (cursor.hasNext()) { 
	        	reviewRecordList.add(mapper.readValue(cursor.next().toString(), ReviewRecordCollectionMongo.class));
	           i++;
	        }
			
	        

		} catch (Exception e) {
			log.error("Unable to retrive ", e);
		}
		return reviewRecordList;
	}


	@Override
	@Transactional
	public List<DefectCollectionMongo> getDefectMongoCollectionList(Date endDate,String collectionName,String testFactoryName, String productName,Set<ProductUserRole> userProducts) {
	
		List<DefectCollectionMongo> defectList = new ArrayList<DefectCollectionMongo>();
		
		log.info("geting data from  MOngo DB - "+collectionName);
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection(collectionName);

			BasicDBObject whereQuery = new BasicDBObject();
			BasicDBList basicDBList=new BasicDBList();
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			
			if(!testFactoryName.equalsIgnoreCase("ALL")){
				whereQuery.put("testFactoryName",testFactoryName);
			}
			
			if(!productName.equalsIgnoreCase("ALL")){
				whereQuery.put("productName",productName);
			}else if(userProducts!=null){
				for(ProductUserRole productUserRole : userProducts){
					basicDBList.add(productUserRole.getProduct().getProductName());
				}
			}
			if(basicDBList != null && basicDBList.size() > 0){
				whereQuery.put("productName", new BasicDBObject("$in", basicDBList));
			}
			if(endDate!=null){
				obj.add(new BasicDBObject("raisedDate", BasicDBObjectBuilder.start("$lte", endDate).get()));
				whereQuery.put("$and", obj);
				
			}
		
			
			DBCursor cursor = dBCollection.find(whereQuery);
	         int i = 1;
	         ObjectMapper mapper = new ObjectMapper();
	         while (cursor.hasNext()) { 
	        	   defectList.add(mapper.readValue(cursor.next().toString(), DefectCollectionMongo.class));
	            i++;
	         }
			

		} catch (Exception e) {
			log.error("Unable to retrive ", e);
		}
		return defectList;
	}


	@Override
	@Transactional
	public List<ActivityCollectionMongo> getActivityMongoCollectionCountByDateFilter(Date startDate, Date currentDate,String collectionName, String testFactoryName, String productName,Set<ProductUserRole> userProducts) {
	
		List<ActivityCollectionMongo> activityList = new ArrayList<ActivityCollectionMongo>();
		
		log.info("geting data from  MOngo DB tested - "+collectionName);
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection(collectionName);
	         
	         	BasicDBObject andQuery = new BasicDBObject();
				List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
				BasicDBList basicDBList=new BasicDBList();
				
				if(!testFactoryName.equalsIgnoreCase("ALL")){
					obj.add(new BasicDBObject("testFactoryName",testFactoryName));
				}
				
				if(!productName.equalsIgnoreCase("ALL")){
					obj.add(new BasicDBObject("productName",productName));
				}else if(userProducts!=null){
					for(ProductUserRole productUserRole : userProducts){
						basicDBList.add(productUserRole.getProduct().getProductName());
					}
				}
				if(basicDBList != null && basicDBList.size() > 0){
					
					andQuery.put("productName", new BasicDBObject("$in", basicDBList));
				}
				
				
				obj.add(new BasicDBObject("actualActivityEndDate", BasicDBObjectBuilder.start("$gte", startDate).add("$lte", currentDate).get()));
				andQuery.put("$and", obj);
				 DBCursor cursor = dBCollection.find(andQuery);
	         
	         int i = 1;
	         ObjectMapper mapper = new ObjectMapper();
	         while (cursor.hasNext()) { 
	        	 activityList.add(mapper.readValue(cursor.next().toString(), ActivityCollectionMongo.class));
	            i++;
	         }
			

		} catch (Exception e) {
			log.error("Unable to retrive ", e);
		}
		return activityList;
	}
	

	@Override
	@Transactional
	public List<ActivityMongo> getActivityMongoByDateFilter(Date startDate,
			Date currentDate, String collectionName, String testFactoryName, String productName,
			Set<ProductUserRole> userProducts) {
	
		List<ActivityMongo> activityList = new ArrayList<ActivityMongo>();		
		log.info("geting data from  MOngo DB tested - "+collectionName);
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection(collectionName);
	         
	         	BasicDBObject andQuery = new BasicDBObject();
				List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
				BasicDBList basicDBList=new BasicDBList();
				
				if(!testFactoryName.equalsIgnoreCase("ALL")){
					obj.add(new BasicDBObject("testFactoryName",testFactoryName));
				}
				
				if(!productName.equalsIgnoreCase("ALL")){
					obj.add(new BasicDBObject("productName",productName));
				}else if(userProducts!=null){
					for(ProductUserRole productUserRole : userProducts){
						basicDBList.add(productUserRole.getProduct().getProductName());
					}
				}
				if(basicDBList != null && basicDBList.size() > 0){
					obj.add(new BasicDBObject("productName",basicDBList));
				}
				obj.add(new BasicDBObject("plannedStartDate", BasicDBObjectBuilder.start("$lte", currentDate).get()));
				andQuery.put("$and", obj);

				log.info("*** andQuery ******"+andQuery);
				 DBCursor cursor = dBCollection.find(andQuery);
	         
	         int i = 1;
	         ObjectMapper mapper = new ObjectMapper();
	         while (cursor.hasNext()) { 
	        	 activityList.add(mapper.readValue(cursor.next().toString(), ActivityMongo.class));
	            i++;
	         }
			

		} catch (Exception e) {
			log.error("Unable to retrive ", e);
		}
		log.info("size"+activityList.size());
		return activityList;
	}
	


	@Override
	@Transactional
	public List<ActivityCollectionMongo> getActivityMongoCollectionListForScheduleVariance(Date startDate, Date currentDate,String productName,Set<ProductUserRole> userProducts) {
		List<ActivityCollectionMongo> dashboardMongoList = new ArrayList<ActivityCollectionMongo>();
		log.debug("geting data from  MOngo DB");
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection("activity_collection");
			
			BasicDBObject andQuery = new BasicDBObject();
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			BasicDBList basicDBList=new BasicDBList();
			if(!productName.equalsIgnoreCase("ALL")){
				obj.add(new BasicDBObject("productName", productName));
			}else if(userProducts!=null){
				for(ProductUserRole productUserRole : userProducts){
					basicDBList.add(productUserRole.getProduct().getProductName());
				}
			}
			if(basicDBList != null && basicDBList.size() > 0){
				
				obj.add(new BasicDBObject("productName",basicDBList));
			}
			
			
			obj.add(new BasicDBObject("weekDate", BasicDBObjectBuilder.start("$gte", startDate).add("$lte", currentDate).get()));
			andQuery.put("$and", obj);

			log.info("And queryyy "+andQuery);
			
			
			  DBCursor cursor = dBCollection.find(andQuery).sort(new BasicDBObject("plannedActivityStartDate", 1)).limit(1);  
			  ObjectMapper mapper = new ObjectMapper();
			if(cursor.hasNext()) {
			    dashboardMongoList.add(mapper.readValue(cursor.next().toString(), ActivityCollectionMongo.class));
			    log.info("dashboardMongoList** getPlannedActivityStartDate "+dashboardMongoList.get(0).getPlannedActivityStartDate());
			}
			
			cursor = dBCollection.find(andQuery).sort(new BasicDBObject("plannedActivityEndDate",-1)).limit(1);  
			
			if (cursor.hasNext()) {
				  dashboardMongoList.add(mapper.readValue(cursor.next().toString(), ActivityCollectionMongo.class));
				  log.info("dashboardMongoList** getPlannedActivityEndDate "+dashboardMongoList.get(1).getPlannedActivityEndDate());
			} 
		
			cursor = dBCollection.find(andQuery).sort(new BasicDBObject("actualActivityEndDate",-1)).limit(1);  
			
			if (cursor.hasNext()) {
				 dashboardMongoList.add(mapper.readValue(cursor.next().toString(), ActivityCollectionMongo.class));
				 log.info("dashboardMongoList** getActualActivityEndDate "+dashboardMongoList.get(2).getActualActivityEndDate());
			} 

		} catch (Exception e) {
			log.error("Unable to retrive ", e);
		}
		return dashboardMongoList;
	}

	
	
	@Override
	@Transactional
	public List<ActivityCollectionMongo> getActivityMongoCollectionListForScheduleVarianceAvg(Date startDate, Date currentDate, String testFactoryName, String productName,Set<ProductUserRole> userProducts) {
		List<ActivityCollectionMongo> dashboardMongoList = new ArrayList<ActivityCollectionMongo>();
		log.debug("geting data from  MOngo DB");
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection("activity_collection");
			
			BasicDBObject andQuery = new BasicDBObject();
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			BasicDBList basicDBList=new BasicDBList();
			
			if(!testFactoryName.equalsIgnoreCase("ALL")){
				obj.add(new BasicDBObject("testFactoryName", testFactoryName));
			}
			
			if(!productName.equalsIgnoreCase("ALL")){
				obj.add(new BasicDBObject("productName", productName));
			}else if(userProducts!=null){
				for(ProductUserRole productUserRole : userProducts){
					basicDBList.add(productUserRole.getProduct().getProductName());
				}
			}
			if(basicDBList != null && basicDBList.size() > 0){
				andQuery.put("productName", new BasicDBObject("$in", basicDBList));
			}
			
			obj.add(new BasicDBObject("actualActivityEndDate", BasicDBObjectBuilder.start("$gte", startDate).add("$lte", currentDate).get()));
			andQuery.put("$and", obj);
			  DBCursor cursor = dBCollection.find(andQuery);  
			  ObjectMapper mapper = new ObjectMapper();
			while(cursor.hasNext()) {
			    dashboardMongoList.add(mapper.readValue(cursor.next().toString(), ActivityCollectionMongo.class));
			}			

		} catch (Exception e) {
			log.error("Unable to retrive ", e);
		}
		return dashboardMongoList;
	}


	
	@Override
	@Transactional
	public List<TestCaseExecutionResultMongo> getTestCaseExecutionMongoCountByDateFilter(Date startDate, Date currentDate,String testFactoryName, String productName,Set<ProductUserRole> productSet) {

		List<TestCaseExecutionResultMongo>testCaseList=new ArrayList<TestCaseExecutionResultMongo>();
		try{
			
			DBCollection dBCollection=this.mongoOperation.getCollection(MongodbConstants.TESTCASEEXECUTION);

			BasicDBObject andQuery = new BasicDBObject();
			BasicDBObject productORQuery = new BasicDBObject();
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			BasicDBList basicDBList = new BasicDBList();
			
			if(!testFactoryName.equalsIgnoreCase("ALL")){
				obj.add(new BasicDBObject("testFactoryName",testFactoryName));
			}
			
			if(!productName.equalsIgnoreCase("ALL")){
				obj.add(new BasicDBObject("productName",productName));
			}else if(productSet!=null){
				for(ProductUserRole productUserRole : productSet){
					basicDBList.add(productUserRole.getProduct().getProductName());
				}
			}
			if(basicDBList != null && basicDBList.size() > 0){
				
				obj.add(new BasicDBObject("productName",basicDBList));
			}
			obj.add(new BasicDBObject("modifiedDate", BasicDBObjectBuilder.start("$gte", startDate).add("$lte", currentDate).get()));
			andQuery.put("$and", obj);
			

			log.info("*** andQuery ******"+andQuery);
			 DBCursor cursor = dBCollection.find(andQuery);
			 
			 int i = 1;
	         ObjectMapper mapper = new ObjectMapper();
	         while (cursor.hasNext()) { 
	        	 testCaseList.add(mapper.readValue(cursor.next().toString(), TestCaseExecutionResultMongo.class));
	            i++;
	         }
			
		}catch(Exception e){
			log.error("Unable to retrive ", e);
		}
		
		return testCaseList;
	}


	@Override
	@Transactional
	public List<TestCaseDefectsMasterMongo> getTestCaseDefectsMasterMongoList(String testFactoryName, String productName,Set<ProductUserRole> userProducts) {
	
		List<TestCaseDefectsMasterMongo> defectList = new ArrayList<TestCaseDefectsMasterMongo>();
		
		log.info("geting data from  MOngo DB - "+MongodbConstants.DEFECTS);
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection(MongodbConstants.DEFECTS);
				
			BasicDBObject whereQuery = new BasicDBObject();
			BasicDBList basicDBList=new BasicDBList();
			
			if(!testFactoryName.equalsIgnoreCase("ALL")){
				whereQuery.put("testFactoryName",testFactoryName);
			}
			
			if(!productName.equalsIgnoreCase("ALL")){
				whereQuery.put("productName",productName);
			}else if(userProducts!=null){
				for(ProductUserRole productUserRole : userProducts){
					basicDBList.add(productUserRole.getProduct().getProductName());
				}
			}
			if(basicDBList != null && basicDBList.size() > 0){
				
				whereQuery.put("productName",basicDBList);
			}
			DBCursor cursor = dBCollection.find(whereQuery);
			log.info("whereQuery   "+whereQuery);
			
			
	         int i = 1;
	         ObjectMapper mapper = new ObjectMapper();
	         while (cursor.hasNext()) { 
	        	   defectList.add(mapper.readValue(cursor.next().toString(), TestCaseDefectsMasterMongo.class));
	            i++;
	         }
			

		} catch (Exception e) {
			log.error("Unable to retrive ", e);
		}
		return defectList;
	}


	@Override
	@Transactional
	public List<UtilizationCollectionMongo> getUtilizationMongoCollectionList(Date startDate, Date currentDate, String testFactoryName) {
	
		List<UtilizationCollectionMongo> utilizationList = new ArrayList<UtilizationCollectionMongo>();
		
		log.info("geting data from  MOngo DB - utilization_collection");
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection("utilization_collection");
			BasicDBObject allQuery=new BasicDBObject();
			allQuery.put("endDate", BasicDBObjectBuilder.start("$gte", startDate).add("$lte", currentDate).get());
			if(!testFactoryName.equalsIgnoreCase("ALL")){
				List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
				obj.add(new BasicDBObject("testFactoryName",testFactoryName));
				allQuery.put("$and", obj);
			}
			
			 DBCursor cursor = dBCollection.find(allQuery);
	         int i = 1;
	         ObjectMapper mapper = new ObjectMapper();
	         while (cursor.hasNext()) { 
	        	 utilizationList.add(mapper.readValue(cursor.next().toString(), UtilizationCollectionMongo.class));
	            i++;
	         }
			
		} catch (Exception e) {
			log.error("Unable to retrive ", e);
		}
		return utilizationList;
	}


	@Override
	@Transactional
	public List<JSONObject> getSingleValueMetricsList(Date startDate,Date currentDate, String testFactoryName) {
		List<JSONObject>singleValueMetricsList=new ArrayList<JSONObject>();
		try{

			DBCollection dBCollection = this.mongoOperation.getCollection("single_value_metric_collection");
			BasicDBObject allQuery=new BasicDBObject();
			allQuery.put("Date", BasicDBObjectBuilder.start("$gte", startDate).add("$lte", currentDate).get());
			if(!testFactoryName.equalsIgnoreCase("ALL")){
				List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
				obj.add(new BasicDBObject("testFactoryName",testFactoryName));
				allQuery.put("$and", obj);
			}
			
			DBCursor cursor = dBCollection.find(allQuery);
	         int i = 1;
	         while (cursor.hasNext()) { 
	        	 singleValueMetricsList.add(new JSONObject(cursor.next().toString()));
	            i++;
	         }
		}catch(Exception e){
			log.error("Unable to retrive ", e);
		}
		
		return singleValueMetricsList;
	}


	@Override
	@Transactional
	public List<JSONObject> getSingleValueMetricsList(Date endDate, String testFactoryName, String metricsName) {
		List<JSONObject>singleValueMetricsList=new ArrayList<JSONObject>();
		try{

			DBCollection dBCollection = this.mongoOperation.getCollection("single_value_metric_collection");
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			BasicDBObject allQuery=new BasicDBObject();
			if(!testFactoryName.equalsIgnoreCase("ALL")){
				obj.add(new BasicDBObject("testFactoryName",testFactoryName));
			}
			obj.add(new BasicDBObject("Date", BasicDBObjectBuilder.start("$lte", endDate).get()));
			obj.add(new BasicDBObject("Date", new BasicDBObject("$exists", true)));
			obj.add(new BasicDBObject(metricsName, new BasicDBObject("$exists", true)));
			allQuery.put("$and", obj);
			
			DBCursor cursor = dBCollection.find(allQuery).sort(new BasicDBObject("Date",-1));  
	         int i = 1;
	         while (cursor.hasNext()) { 
	        	 singleValueMetricsList.add(new JSONObject(cursor.next().toString()));
	            i++;
	         }
		}catch(Exception e){
			log.error("Unable to retrive ", e);
		}
		return singleValueMetricsList;
	}


	@Override
	@Transactional
	public Boolean checkAvailabiltyOfMongoDB() {
		boolean isMongoConnectionAvailable = true;
		log.debug("Inside checkMongoConnectivity");
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection("connection_check");
			dBCollection.find().count();
			log.debug("Connection successfull");
		} catch (Exception e) {
			isMongoConnectionAvailable = false;
			log.error("Connection failure", e);
		}
		return isMongoConnectionAvailable;
	}


	@Override
	@Transactional
	public List<Activity> getScheduleVarianceforProduct(Integer productId) {
			List<Activity> activityCollections = new ArrayList<Activity>();
			try {

				Criteria c = sessionFactory.getCurrentSession().createCriteria(Activity.class, "activity");
				
				c.createAlias("activity.activityWorkPackage", "workpackage");
				c.createAlias("workpackage.productBuild", "build");
				c.createAlias("build.productVersion", "version");
				c.createAlias("version.productMaster", "product");
				c.add(Restrictions.eq("product.productId", productId));
				
				
				c.add(Restrictions.ne("activity.actualEndDate", null));
				c.addOrder(Order.desc("activity.actualEndDate"));
				List resultList = c.list();
				
				if(resultList != null && resultList.size() > 0){
					activityCollections.add((Activity) resultList.get(0));
				}
				
				c = sessionFactory.getCurrentSession().createCriteria(Activity.class, "activity");
				c.createAlias("activity.activityWorkPackage", "workpackage");
				c.createAlias("workpackage.productBuild", "build");
				c.createAlias("build.productVersion", "version");
				c.createAlias("version.productMaster", "product");
				c.add(Restrictions.eq("product.productId", productId));
				
				c.add(Restrictions.ne("activity.plannedEndDate", null));
				c.addOrder(Order.desc("activity.plannedEndDate"));
				resultList = c.list();
				
				if(resultList != null && resultList.size() > 0){
					activityCollections.add((Activity) resultList.get(0));
				}
				
				c = sessionFactory.getCurrentSession().createCriteria(Activity.class, "activity");
				c.createAlias("activity.activityWorkPackage", "workpackage");
				c.createAlias("workpackage.productBuild", "build");
				c.createAlias("build.productVersion", "version");
				c.createAlias("version.productMaster", "product");
				c.add(Restrictions.eq("product.productId", productId));
				
				c.add(Restrictions.ne("activity.plannedStartDate", null));
				c.addOrder(Order.asc("activity.plannedStartDate"));
				resultList = c.list();
				
				if(resultList != null && resultList.size() > 0){
					activityCollections.add((Activity) resultList.get(0));
				}
				
				log.info("Data fetched Successfully from Activity ");
				
			} catch (Exception e) {

				log.error("Unable to get data from Table", e);
			}

			return activityCollections;
		
	}


	@Override
	@Transactional
	public Integer getProjectTeamMembersCount(Date startDate, Date currentDate,String testFactoryName,String productName, Set<ProductUserRole> userProducts) {

		List<ProductTeamResourcesMongo>productTeamResources=new ArrayList<ProductTeamResourcesMongo>();
		List<ProductTeamResourcesMongo>finalList=new ArrayList<ProductTeamResourcesMongo>();
		Integer size=0;
		try{
			
			DBCollection dBCollection=this.mongoOperation.getCollection(MongodbConstants.PRODUCT_TEAM_RESOURCES);

			BasicDBObject andQuery = new BasicDBObject();
			BasicDBObject productORQuery = new BasicDBObject();
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			BasicDBList basicDBList = new BasicDBList();
			
			if(!testFactoryName.equalsIgnoreCase("ALL")){
				obj.add(new BasicDBObject("testFactoryName",testFactoryName));
			}
			
			if(!productName.equalsIgnoreCase("ALL")){
				obj.add(new BasicDBObject("productName",productName));
			}else if(userProducts!=null){
				for(ProductUserRole productUserRole : userProducts){
					basicDBList.add(productUserRole.getProduct().getProductName());
				}
			}
			if(basicDBList != null && basicDBList.size() > 0){
				
				obj.add(new BasicDBObject("productName",basicDBList));
			}
			obj.add(new BasicDBObject("fromDate", BasicDBObjectBuilder.start("$gte", startDate).add("$lte", currentDate).get()));
			obj.add(new BasicDBObject("toDate", BasicDBObjectBuilder.start("$gte", startDate).add("$lte", currentDate).get()));
			andQuery.put("$and", obj);
			

			log.info("*** andQuery ******"+andQuery);
			 DBCursor cursor = dBCollection.find(andQuery);
			 
			 int i = 1;
	         ObjectMapper mapper = new ObjectMapper();
	         while (cursor.hasNext()) { 
	        	 productTeamResources.add(mapper.readValue(cursor.next().toString(), ProductTeamResourcesMongo.class));
	            i++;
	         }
	         
	         
	         for(ProductTeamResourcesMongo teamUser:productTeamResources){
	        	 if(!finalList.contains(teamUser)){
	        		 finalList.add(teamUser);
	        	 }
	         }
	         size=finalList.size();
			
		}catch(Exception e){
			log.error("Unable to retrive ", e);
		}
		
		return size;
	}


	@Override
	@Transactional
	public List<JSONObject> getSingleValueListPCIScroreByDate(Date endDate, String testFactoryName) {
		List<JSONObject>singleValueMetricsList=new ArrayList<JSONObject>();
		try{
		
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();

			DBCollection dBCollection = this.mongoOperation.getCollection("single_value_metric_collection");
			BasicDBObject allQuery=new BasicDBObject();
	
			if(!testFactoryName.equalsIgnoreCase("ALL")){
				obj.add(new BasicDBObject("testFactoryName",testFactoryName));
			}
			obj.add(new BasicDBObject("Date", BasicDBObjectBuilder.start("$lte", endDate).get()));
			obj.add(new BasicDBObject("Date", new BasicDBObject("$exists", true)));
			allQuery.put("$and", obj);
	
			DBCursor cursor = dBCollection.find(allQuery).sort(new BasicDBObject("Date",-1)).limit(1);
				
	         int i = 1;
	         while (cursor.hasNext()) { 
	        	 singleValueMetricsList.add(new JSONObject(cursor.next().toString()));
	            i++;
	         }			
			
			
		}catch(Exception e){
			log.error("Unable to retrive ", e);
		}		
		return singleValueMetricsList;
	}

	@Override
	@Transactional
	public List<JSONObject> getCollectionForPivotReportBasedOnCollectionName(String collectionName, int testFactoryId, int productId) {
		List<JSONObject>singleValueMetricsList=new ArrayList<JSONObject>();
		try{
			DBCursor cursor = null;
			DBCollection dBCollection = this.mongoOperation.getCollection(collectionName);
			
			if(testFactoryId!=-1 && productId!=-1){
				BasicDBObject andQuery = new BasicDBObject();
				List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			  	obj.add(new BasicDBObject("productId", productId));
			  	obj.add(new BasicDBObject("testFactoryId", testFactoryId));
			  	andQuery.put("$and", obj);
				cursor = dBCollection.find(andQuery);
			}else if(testFactoryId!=-1 && productId==-1){
			  	BasicDBObject whereQuery = new BasicDBObject();
			  	whereQuery.put("testFactoryId",testFactoryId);
				cursor = dBCollection.find(whereQuery);
			}else{
				cursor = dBCollection.find();
			}
			
			String tmp="";
						
			while(cursor.hasNext()) {
				tmp=cursor.next().toString();
			    singleValueMetricsList.add(new JSONObject(tmp));
			}
		}catch(Exception e){
			log.error("Unable to retrive ", e);
		}
		
		return singleValueMetricsList;
	}	
	@Override
	@Transactional
	public List<String> getKeyNameForPivotReportBasedOnCollectionName(String collectionName) {
		List<String> keyList=new ArrayList<String>();
		try{
			
			DBCollection dBCollection = this.mongoOperation.getCollection(collectionName);
			
			DBCursor cursor = dBCollection.find();
			String tmp="";
			for (String key: cursor.next().keySet()) {
			    if(!keyList.contains(key))
			    	keyList.add(key);
			}
			
		}catch(Exception e){
			log.error("Unable to retrive ", e);
		}
		
		return keyList;
	}
	@Override
	@Transactional
	public void updateParentStatusInChildColletions(List<String> collectionNames, Integer entityMasterId,Integer instanceId, Integer status,String fieldName) {
		try{
			String statusValue="";
			if(status == 1){
				statusValue = "Active";
			}else{
				statusValue = "InActive";
			}
			for(String collection:collectionNames){
				DBCollection dBCollection = this.mongoOperation.getCollection(collection);
				BasicDBObject newDocument =  new BasicDBObject().append("$set", new BasicDBObject().append("parentStatus", statusValue));
				dBCollection.updateMulti(new BasicDBObject().append(fieldName, instanceId), newDocument);
			}
		}catch(Exception e){
			log.error("Update Failed ", e);
		}
		
	}


	@Override
	@Transactional
	public void updateParentStatusInChildColletionsWhileUpdatingActive(String collectionName,Integer status, Integer immediateParentStatus,Integer instanceId, String fieldName) {
		
		String statusValue="";
		if(status == 1 && immediateParentStatus == 1 ){
			statusValue = "Active";
		}else{
			statusValue = "InActive";
		}
			DBCollection dBCollection = this.mongoOperation.getCollection(collectionName);
			BasicDBObject newDocument =  new BasicDBObject().append("$set", new BasicDBObject().append("parentStatus", statusValue));
			dBCollection.updateMulti(new BasicDBObject().append(fieldName, instanceId), newDocument);
	}


	@Override
	@Transactional
	public List<DPAWorkbookCollectionMongo> getDPAWorkbookMongoCollectionList(Date endDate, String testFactoryName, String productName,	Set<ProductUserRole> userProducts) {
		List<DPAWorkbookCollectionMongo> dpaWorkbookCollectionMongos = new ArrayList<DPAWorkbookCollectionMongo>();
		log.info("geting data from  MOngo DB - dpa_workbook_collection");
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection("dpa_workbook_collection");

			BasicDBObject whereQuery = new BasicDBObject();
			BasicDBList basicDBList=new BasicDBList();
			
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			
			if(!testFactoryName.equalsIgnoreCase("ALL")){
				whereQuery.put("testFactoryName",testFactoryName);
			}
			
			if(!productName.equalsIgnoreCase("ALL")){
				whereQuery.put("productName",productName);
			}else if(userProducts!=null){
				for(ProductUserRole productUserRole : userProducts){
					basicDBList.add(productUserRole.getProduct().getProductName());
				}
			}
			if(basicDBList != null && basicDBList.size() > 0){
				whereQuery.put("productName", new BasicDBObject("$in", basicDBList));
			}
			if(endDate!=null){
				obj.add(new BasicDBObject("correctivePlannedStartDate", BasicDBObjectBuilder.start("$lte", endDate).get()));
				whereQuery.put("$and", obj);
			}
		
			
			DBCursor cursor = dBCollection.find(whereQuery);
			log.info("whereQuery   "+whereQuery);
	        int i = 1;
	        ObjectMapper mapper = new ObjectMapper();
				
	        while (cursor.hasNext()) { 
	        	dpaWorkbookCollectionMongos.add(mapper.readValue(cursor.next().toString(), DPAWorkbookCollectionMongo.class));
	           i++;
	        }
			
	        

		} catch (Exception e) {
			log.error("Unable to retrive ", e);
		}
		return dpaWorkbookCollectionMongos;
	}


	@Override
	@Transactional
	public Map<String, Object> getDPAWorkbookMongoCollectionCounts(Date startDate, Date endDate, String testFactoryName, String productName, Set<ProductUserRole> userProducts) {
		HashMap<String, Object> dpaWorkbookCounts = new HashMap<String, Object>();
		log.info("geting data from  MOngo DB - dpa_workbook_collection");
		try {
			Integer totalDefectsClosedOnPeriod = 0;
			Integer totalDefectsTillPeriodEndDate = 0;
			Integer defectsClosedBeforePeriodStartDate = 0;
			
			dpaWorkbookCounts.put("totalDefectsClosedOnPeriod", totalDefectsClosedOnPeriod);
			dpaWorkbookCounts.put("totalDefectsTillPeriodEndDate", totalDefectsTillPeriodEndDate);
			dpaWorkbookCounts.put("defectsClosedBeforePeriodStartDate", defectsClosedBeforePeriodStartDate);
			
			DBCollection dBCollection = this.mongoOperation.getCollection("dpa_workbook_collection");

			BasicDBObject whereQuery = new BasicDBObject();
			BasicDBList basicDBList=new BasicDBList();
			
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			
			if(!testFactoryName.equalsIgnoreCase("ALL")){
				whereQuery.put("testFactoryName",testFactoryName);
			}
			
			if(!productName.equalsIgnoreCase("ALL")){
				whereQuery.put("productName",productName);
			}else if(userProducts!=null){
				for(ProductUserRole productUserRole : userProducts){
					basicDBList.add(productUserRole.getProduct().getProductName());
				}
			}
			if(basicDBList != null && basicDBList.size() > 0){
				whereQuery.put("productName", new BasicDBObject("$in", basicDBList));
			}
			if(endDate!=null){
				obj.add(new BasicDBObject("correctivePlannedStartDate", BasicDBObjectBuilder.start("$lte", endDate).get()));
				whereQuery.put("$and", obj);
			}
		
			DBCursor cursor = dBCollection.find(whereQuery);
			
			if(cursor != null){
				totalDefectsTillPeriodEndDate = cursor.size();
				dpaWorkbookCounts.put("totalDefectsTillPeriodEndDate", totalDefectsTillPeriodEndDate);
			}
			
			whereQuery.remove("$and");
			obj = new ArrayList<BasicDBObject>();
			if(endDate!=null){
				obj.add(new BasicDBObject("correctiveActualEndDate", BasicDBObjectBuilder.start("$lte", startDate).get()));
				whereQuery.put("$and", obj);
			}

			cursor = dBCollection.find(whereQuery);
			if(cursor != null){
				defectsClosedBeforePeriodStartDate = cursor.size();
				dpaWorkbookCounts.put("defectsClosedBeforePeriodStartDate", defectsClosedBeforePeriodStartDate);
			}
			
			whereQuery.remove("$and");
			obj = new ArrayList<BasicDBObject>();
			if(endDate!=null){
				obj.add(new BasicDBObject("correctiveActualEndDate", BasicDBObjectBuilder.start("$gte", startDate).add("$lte", endDate).get()));
				whereQuery.put("$and", obj);
				whereQuery.put("correctiveActionStatus", "Completed");
			}

			cursor = dBCollection.find(whereQuery);
			
			if(cursor != null){
				totalDefectsClosedOnPeriod = cursor.size();
				dpaWorkbookCounts.put("totalDefectsClosedOnPeriod", totalDefectsClosedOnPeriod);
			}
		} catch (Exception e) {
			log.error("Unable to retrive ", e);
		}
		return dpaWorkbookCounts;
	}


	@Override
	@Transactional
	public List<JSONObject> getSingleValueMetricsARIDList(Date endDate, String testFactoryName) {
		List<JSONObject>singleValueMetricsList=new ArrayList<JSONObject>();
		try{
		
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();

			DBCollection dBCollection = this.mongoOperation.getCollection("single_value_metric_collection");
			BasicDBObject allQuery=new BasicDBObject();
	
			if(!testFactoryName.equalsIgnoreCase("ALL")){
				obj.add(new BasicDBObject("testFactoryName",testFactoryName));
			}
			obj.add(new BasicDBObject("Date", BasicDBObjectBuilder.start("$lte", endDate).get()));
			obj.add(new BasicDBObject("Date", new BasicDBObject("$exists", true)));
			allQuery.put("$and", obj);
	
			DBCursor cursor = dBCollection.find(allQuery).sort(new BasicDBObject("Date",-1)).limit(1);
				
	         while (cursor.hasNext()) { 
	        	 singleValueMetricsList.add(new JSONObject(cursor.next().toString()));
	         }			
			
		}catch(Exception e){
			log.error("Unable to retrive ", e);
		}		
		return singleValueMetricsList;
	}
	
	
	@Override
	@Transactional
	public List<JSONObject> getMongoDefectsPmoByPmtId(Float pmtId) {
		List<JSONObject>defectPmoList=new ArrayList<JSONObject>();
		try{
		
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();

			DBCollection dBCollection = this.mongoOperation.getCollection("defectpmo");
			BasicDBObject allQuery=new BasicDBObject();
	
			obj.add(new BasicDBObject("PMT",pmtId));
			
			allQuery.put("$and", obj);
	
			DBCursor cursor = dBCollection.find(allQuery);
				
	         while (cursor.hasNext()) { 
	        	 defectPmoList.add(new JSONObject(cursor.next().toString()));
	         }			
			
		}catch(Exception e){
			log.error("Unable to retrive ", e);
		}		
		return defectPmoList;
	}


	@Override
	@Transactional
	public JSONObject getMongoPmoByPmtId(Float pmtId) {
		List<JSONObject>defectPmoList=new ArrayList<JSONObject>();
		JSONObject pmoObject =null;
		try{
		
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();

			DBCollection dBCollection = this.mongoOperation.getCollection("pmo_collection");
			BasicDBObject allQuery=new BasicDBObject();
	
			obj.add(new BasicDBObject("PMT",pmtId));
			
			allQuery.put("$and", obj);
	
			DBCursor cursor = dBCollection.find(allQuery).limit(1);
				
	         while (cursor.hasNext()) { 
	        	 defectPmoList.add(new JSONObject(cursor.next().toString()));
	         }	
	         if(defectPmoList.size() != 0){
	        	 pmoObject = defectPmoList.get(0);
	         }
		}catch(Exception e){
			log.error("Unable to retrive ", e);
		}		
		return pmoObject;
	}
	
}
