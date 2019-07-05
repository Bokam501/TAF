package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DataExtractorScheduleMaster;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.json.JsonSchedule;
import com.hcl.atf.taf.scheduler.GenericScheduleManager;
import com.hcl.atf.taf.scheduler.JobEntity;
import com.hcl.atf.taf.scheduler.TAFScheduleManager;
import com.hcl.atf.taf.scheduler.TestConfigScheduleEntity;
import com.hcl.atf.taf.service.DataSourceExtractorService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.SchedulerService;

@Service
public class SchedulerServiceImpl implements SchedulerService {

	private static final Log log = LogFactory.getLog(SchedulerServiceImpl.class);

	
	@Autowired
	private ProductListService productListService; 
	
	@Autowired
	private DataSourceExtractorService dataSourceExtractorService;
	
	@Override
	@Transactional
	public JsonSchedule addUpdateSchedule(JsonSchedule jsonSchedule) {

		if (jsonSchedule.getOwnerEntityType().equals(IDPAConstants.ENTITY_TEST_RUN_PLAN)) {
			return addUpdateTestRunPlanSchedule(jsonSchedule);
		} else if (jsonSchedule.getOwnerEntityType().equals(IDPAConstants.ENTITY_TEST_MANAGEMENT_SYSTEM)) {
		} else if (jsonSchedule.getOwnerEntityType().equals(IDPAConstants.ENTITY_DEFECT_MANAGEMENT_SYSTEM)) {
		}else if (jsonSchedule.getOwnerEntityType().equals(IDPAConstants.DATA_EXTRACTOR)) {
			return addUpdateDataExtractorSchedule(jsonSchedule);
		}
		return null;
	}

	@Override
	@Transactional
	public JsonSchedule deleteSchedule(JsonSchedule jsonSchedule) {

		if (jsonSchedule.getOwnerEntityType().equals(IDPAConstants.ENTITY_TEST_RUN_PLAN)) {
			return deleteTestRunPlanSchedule(jsonSchedule);
		} else if (jsonSchedule.getOwnerEntityType().equals(IDPAConstants.ENTITY_TEST_MANAGEMENT_SYSTEM)) {
		} else if (jsonSchedule.getOwnerEntityType().equals(IDPAConstants.ENTITY_DEFECT_MANAGEMENT_SYSTEM)) {
		}else if (jsonSchedule.getOwnerEntityType().equals(IDPAConstants.DATA_EXTRACTOR)) {
			return deleteGenericSchedule(jsonSchedule);
		}
		return null;
	}

	
	@Transactional
	public JsonSchedule addUpdateTestRunPlanSchedule(JsonSchedule jsonSchedule) {

		try {

			log.info("Going to add/update schedule for : " + jsonSchedule.getOwnerEntityType() + "  " + jsonSchedule.getOwnerEntityId());
			//Get the owner TestRunPlan
			TestRunPlan testRunPlan = productListService.getTestRunPlanById(jsonSchedule.getOwnerEntityId());
			if (testRunPlan == null)
				return null;
				
			TAFScheduleManager tafScheduleManager = TAFScheduleManager.getTAFScheduleManager();
			//Find if the request has a schedule to be added/updated
			boolean requestHasSchedule = false;
			boolean requestCronIsValid =  false;
			if (jsonSchedule.getCronExpression() == null || jsonSchedule.getCronExpression().trim().length() < 1) {
				requestHasSchedule = false;
				log.info("Test Run Plan does not have a schedule");
			} else {
				requestHasSchedule = true;
				requestCronIsValid = tafScheduleManager.isValidCronExpression(jsonSchedule.getCronExpression());
				if (!requestCronIsValid) {
					log.info("Cron Expression is not a valid one : " + jsonSchedule.getCronExpression());
				} else {
				}
			}

			//Find if the entity TRP has an existing schedule
			boolean entityHasExistingSchedule = false;
			if (testRunPlan.getTestRunCronSchedule() == null || testRunPlan.getTestRunCronSchedule().trim().length() < 1) {
				entityHasExistingSchedule = false;
				log.info("Test Run Plan does not have an existing schedule.");
			} else {
				entityHasExistingSchedule = true;
				log.info("Test Run Plan has an existing schedule : " + testRunPlan.getTestRunCronSchedule());
			}
				
			//#1. New schedule to be added
			if (requestHasSchedule && !entityHasExistingSchedule) {
				if (requestCronIsValid) {
					updateTestRunPlan(testRunPlan, jsonSchedule);
					tafScheduleManager = TAFScheduleManager.getTAFScheduleManager();
					tafScheduleManager.createTestConfigSchedule(createTestConfigScheduleEntity(testRunPlan));
					log.info("Added new schedule for Test Run Plan");
					jsonSchedule = viewUpcomingFireTimes(jsonSchedule, 10);
					jsonSchedule.setMessage("Schedule created successfully");
					return jsonSchedule;
				} else {
					log.info("Not adding a new schedule for Test Run Plan as the cron expression is not valid");
					return null;
				}
			}
			//#2. Existing Schedule to be updated
			if (requestHasSchedule && entityHasExistingSchedule) {
				if (requestCronIsValid) {
					updateTestRunPlan(testRunPlan, jsonSchedule);
					tafScheduleManager = TAFScheduleManager.getTAFScheduleManager();
					tafScheduleManager.updateTestConfigSchedule(createTestConfigScheduleEntity(testRunPlan));
					log.info("Updated schedule for Test Run Plan from : " + testRunPlan.getTestRunCronSchedule() + " -> " + jsonSchedule.getCronExpression());
					jsonSchedule = viewUpcomingFireTimes(jsonSchedule, 10);
					jsonSchedule.setMessage("Schedule updated successfully");
					return jsonSchedule;
				} else {
					log.info("Not updating schedule for Test Run Plan as the cron expression is not valid");
					return null;
				}
			} 
			
			//#3. Existing schedule to be deleted
			if (!requestHasSchedule && entityHasExistingSchedule) {
					updateTestRunPlan(testRunPlan, jsonSchedule);
				tafScheduleManager = TAFScheduleManager.getTAFScheduleManager();
				tafScheduleManager.deleteTestConfigSchedule(createTestConfigScheduleEntity(testRunPlan));
				log.info("Existing schedule deleted");
				jsonSchedule = viewUpcomingFireTimes(jsonSchedule, 10);
				jsonSchedule.setMessage("Schedule deleted successfully");
				return jsonSchedule;
			}
				
			//#4. No existing schedule and no schedule from UI
			if (!requestHasSchedule && !entityHasExistingSchedule) {
				jsonSchedule.setMessage("Test Run plan does not have an existing schedule and no new schedule request from user");
				//Do Nothing
				log.info("Test Run plan does not have an existing schedule and no new schedule request from user. Doing nothing");
				return jsonSchedule;
			}
		} catch (Exception e) {
			log.info("Problem while adding/updating schedule for Test Run Plan", e);
			return null;
		}
		return jsonSchedule;
	}
	
	@Transactional
	public JsonSchedule deleteTestRunPlanSchedule(JsonSchedule jsonSchedule) {

		try {

			log.info("Going to add/update schedule for : " + jsonSchedule.getOwnerEntityType() + "  " + jsonSchedule.getOwnerEntityId());
			//Get the owner TestRunPlan
			TestRunPlan testRunPlan = productListService.getTestRunPlanById(jsonSchedule.getOwnerEntityId());
			if (testRunPlan == null)
				return null;
				
			TAFScheduleManager tafScheduleManager = TAFScheduleManager.getTAFScheduleManager();
			
			//#3. Existing schedule to be deleted
			if (!(testRunPlan.getTestRunCronSchedule() == null || testRunPlan.getTestRunCronSchedule().isEmpty())) {
					updateTestRunPlan(testRunPlan, jsonSchedule);
				tafScheduleManager = TAFScheduleManager.getTAFScheduleManager();
				tafScheduleManager.deleteTestConfigSchedule(createTestConfigScheduleEntity(testRunPlan));
				log.info("Existing schedule deleted");
				jsonSchedule.setMessage("Schedule deleted successfully");
			}
		} catch (Exception e) {
			log.info("Problem while adding/updating schedule for Test Run Plan", e);
			return null;
		}
		return jsonSchedule;
	}

	@Transactional
	public JsonSchedule deleteGenericSchedule(JsonSchedule jsonSchedule) {
		try {
			log.info("Going to delete schedule for : " + jsonSchedule.getOwnerEntityType() + "  " + jsonSchedule.getOwnerEntityId());
			Object scheduleObject = null;
			if (jsonSchedule.getOwnerEntityType().equals(IDPAConstants.DATA_EXTRACTOR)) {
				DataExtractorScheduleMaster dataExtractorScheduleMaster = dataSourceExtractorService.getDataExtractorScheduleMasterById(jsonSchedule.getOwnerEntityId());
				jsonSchedule.setStartDate(DateUtility.dateformatWithSlashWithOutTime(dataExtractorScheduleMaster.getStartDate()));
				scheduleObject = dataExtractorScheduleMaster;
			}else if (jsonSchedule.getOwnerEntityType().equals(IDPAConstants.ENTITY_TEST_RUN_PLAN)) {
				TestRunPlan testRunPlan = productListService.getTestRunPlanById(jsonSchedule.getOwnerEntityId());
				scheduleObject = testRunPlan;
			} 
			if(scheduleObject == null){
				return null;
			}
			updateScheduleDetailsForJobEntity(scheduleObject, jsonSchedule);

			GenericScheduleManager genericScheduleManager = GenericScheduleManager.getScheduleManager();
			genericScheduleManager.deleteSchedule(createJobEntity(scheduleObject));
			jsonSchedule.setMessage("Schedule deleted successfully");
			updateActionEntityDetails(scheduleObject);
			log.info("Existing schedule deleted");
			
		} catch (Exception e) {
			log.error("Problem while deleting schedule ", e);
			return null;
		}
		return jsonSchedule;
	}
	
	private void updateTestRunPlan(TestRunPlan testRunPlan, JsonSchedule jsonSchedule) {
		
		testRunPlan.setTestRunCronSchedule(jsonSchedule.getCronExpression());
		testRunPlan.setTestRunScheduledStartTime(DateUtility.dateformatWithOutTime(jsonSchedule.getStartDate()));
		testRunPlan.setTestRunScheduledEndTime(DateUtility.dateformatWithOutTime(jsonSchedule.getEndDate()));
		productListService.updateTestRunPlan(testRunPlan);
	}
	
	private void updateScheduleDetailsForJobEntity(Object scheduleObject, JsonSchedule jsonSchedule) {
		try{
			if(scheduleObject instanceof TestRunPlan){
				TestRunPlan testRunPlan = (TestRunPlan) scheduleObject;
				testRunPlan.setTestRunCronSchedule(jsonSchedule.getCronExpression());
				testRunPlan.setTestRunScheduledStartTime(DateUtility.dateformatWithOutTime(jsonSchedule.getStartDate()));
				testRunPlan.setTestRunScheduledEndTime(DateUtility.dateformatWithOutTime(jsonSchedule.getEndDate()));
			}else if(scheduleObject instanceof DataExtractorScheduleMaster){
				DataExtractorScheduleMaster dataExtractorScheduleMaster = (DataExtractorScheduleMaster) scheduleObject;
				dataExtractorScheduleMaster.setCronExpression(jsonSchedule.getCronExpression());
				dataExtractorScheduleMaster.setStartDate(DateUtility.dateformatWithOutTime(jsonSchedule.getStartDate()));
				dataExtractorScheduleMaster.setEndDate(DateUtility.dateformatWithOutTime(jsonSchedule.getEndDate()));
				dataExtractorScheduleMaster.setNextExecution(null);
			}
			
		}catch(Exception ex){
			log.error("Problem while updating schedule object details for job entity ", ex);
		}
		
	}
	
	private void updateActionEntityDetails(Object scheduleObject) {
		
		if(scheduleObject instanceof TestRunPlan){
			
			TestRunPlan testRunPlan = (TestRunPlan) scheduleObject;
			productListService.updateTestRunPlan(testRunPlan);
			
		}else if(scheduleObject instanceof DataExtractorScheduleMaster){
			
			DataExtractorScheduleMaster dataExtractorScheduleMaster = (DataExtractorScheduleMaster) scheduleObject;
			dataSourceExtractorService.updateDataExtractorScheduleMasster(dataExtractorScheduleMaster);
			
		}
		
	}
	
	@Override
	@Transactional
	public JsonSchedule viewEntitySchedule(int entityId, String entityType) {
	
		
		JsonSchedule jsonSchedule = new JsonSchedule();
		if (entityType.equals(IDPAConstants.ENTITY_TEST_RUN_PLAN)) {
			
			log.info("Getting Schedule for : Entyity Type : " + entityType + " : Entity ID : " + entityId);
			TestRunPlan testRunPlan = productListService.getTestRunPlanById(entityId);
			if (testRunPlan == null) {
				log.info("Could not find Test run plan ID : " + entityId);
				return null;
			} else {
				log.info("Found test run plan : ID : " + entityId);
			}
		
			jsonSchedule = new JsonSchedule(testRunPlan);
			jsonSchedule = viewUpcomingFireTimes(jsonSchedule, 10);
			
		}else if (entityType.equals(IDPAConstants.DATA_EXTRACTOR)) {
			
			log.info("Getting Schedule for : Entyity Type : " + entityType + " : Entity ID : " + entityId);
			DataExtractorScheduleMaster dataExtractorScheduleMaster = dataSourceExtractorService.getDataExtractorScheduleMasterById(entityId);
			
			if (dataExtractorScheduleMaster == null) {
				log.info("Could not find data extractor for ID : " + entityId);
				return null;
			} else {
				log.info("Found data extractor : ID : " + entityId);
			}
		
			jsonSchedule = new JsonSchedule(dataExtractorScheduleMaster);
			jsonSchedule = viewUpcomingFireTimes(jsonSchedule, 10);
		}
		
		
		return jsonSchedule;
	}
	
	/*
	 * Create a ScheduleEntity object from the Test Run Plan object
	 */
	private TestConfigScheduleEntity createTestConfigScheduleEntity(TestRunPlan testRunPlan) {
		
		//Convert the object for creating a new schedule
		TestConfigScheduleEntity entity = new TestConfigScheduleEntity();
		entity.setProductId(testRunPlan.getProductVersionListMaster().getProductMaster().getProductId().toString());
		entity.setProductName(testRunPlan.getProductVersionListMaster().getProductMaster().getProductName());
		entity.setProductVersionId(testRunPlan.getProductVersionListMaster().getProductVersionListId().toString());
		entity.setProductVersionName(testRunPlan.getProductVersionListMaster().getProductVersionName());
		entity.setTestConfigName(testRunPlan.getTestRunPlanName());
		entity.setTestConfigDescription(testRunPlan.getDescription());
		entity.setTestConfigId(testRunPlan.getTestRunPlanId().toString());
		entity.setScheduleCronExpression(testRunPlan.getTestRunCronSchedule());
		entity.setJobClassName("com.hcl.atf.taf.scheduler.jobs.DefaultTestConfigCycleJob");
		entity.setStartDate(testRunPlan.getTestRunScheduledStartTime());
		entity.setEndDate(testRunPlan.getTestRunScheduledEndTime());
		return entity;
	}
	
	private JobEntity createJobEntity(Object entityObject) {
		
		JobEntity jobEntity = new JobEntity();
		
		if(entityObject instanceof TestRunPlan){
			
			TestRunPlan testRunPlan = (TestRunPlan) entityObject;
			jobEntity.setGroupName(testRunPlan.getProductVersionListMaster().getProductMaster().getProductName());
			jobEntity.setJobName(testRunPlan.getTestRunPlanName());
			jobEntity.setTriggerName(testRunPlan.getTestRunPlanName());
			jobEntity.setTriggerDescription(testRunPlan.getDescription());
			jobEntity.setStartTime(testRunPlan.getTestRunScheduledStartTime());
			jobEntity.setEndTime(testRunPlan.getTestRunScheduledEndTime());
			jobEntity.setScheduleCronExpression(testRunPlan.getTestRunCronSchedule());
			jobEntity.setJobClassName("com.hcl.atf.taf.scheduler.jobs.DefaultTestConfigCycleJob");
			
		}else if(entityObject instanceof DataExtractorScheduleMaster){
			
			DataExtractorScheduleMaster dataExtractorScheduleMaster = (DataExtractorScheduleMaster) entityObject;
			jobEntity.setGroupName(dataExtractorScheduleMaster.getGroupName());
			jobEntity.setJobName(dataExtractorScheduleMaster.getJobName());
			jobEntity.setTriggerName(dataExtractorScheduleMaster.getJobName());
			jobEntity.setTriggerDescription(dataExtractorScheduleMaster.getDescription());
			if(dataExtractorScheduleMaster.getStartDate() != null && dataExtractorScheduleMaster.getStartDate().before(new Date())){
				dataExtractorScheduleMaster.setStartDate(new Date());
			}
			jobEntity.setStartTime(dataExtractorScheduleMaster.getStartDate());
			jobEntity.setEndTime(dataExtractorScheduleMaster.getEndDate());
			jobEntity.setScheduleCronExpression(dataExtractorScheduleMaster.getCronExpression());
			jobEntity.setJobClassName("com.hcl.atf.taf.scheduler.jobs.DataSourceExtractorJob");
			
			HashMap<String, Object> extractorDetails = new HashMap<String, Object>();
			extractorDetails.put("customer", dataExtractorScheduleMaster.getCustomer().getCustomerName());
			extractorDetails.put("customerId", dataExtractorScheduleMaster.getCustomer().getCustomerId());
			extractorDetails.put("competency", dataExtractorScheduleMaster.getCompetency().getName());
			extractorDetails.put("competencyId", dataExtractorScheduleMaster.getCompetency().getDimensionId());
			if(dataExtractorScheduleMaster.getProduct() != null && dataExtractorScheduleMaster.getProduct().getProductId() != null && dataExtractorScheduleMaster.getProduct().getProductId() > 0){
				extractorDetails.put("project", dataExtractorScheduleMaster.getProduct().getProductName());
				extractorDetails.put("projectId", dataExtractorScheduleMaster.getProduct().getProductId());
			}
			extractorDetails.put("testFactoryName", dataExtractorScheduleMaster.getEngagement().getTestFactoryName());
			extractorDetails.put("testFactoryId", dataExtractorScheduleMaster.getEngagement().getTestFactoryId());
			extractorDetails.put("testCentersName", dataExtractorScheduleMaster.getEngagement().getTestFactoryLab().getTestFactoryLabName());
			extractorDetails.put("testCentersId", dataExtractorScheduleMaster.getEngagement().getTestFactoryLab().getTestFactoryLabId());
			extractorDetails.put("extractorType", dataExtractorScheduleMaster.getExtractorType());
			
			jobEntity.setDataMap(extractorDetails);
			
		}
		
		return jobEntity;
		
	}


	@Override
	@Transactional
	public JsonSchedule viewUpcomingFireTimes(int entityId, String entityType, int numberTimes) {
	
		
		log.info("Getting fire times for Entyity Type : " + entityType + " : Entity ID : " + entityId + " : Fire times count : " + numberTimes  );
		if (entityType == null || entityId < 1)
			return null;
		
		JsonSchedule jsonSchedule = new JsonSchedule();
		try {
			if (entityType.equals(IDPAConstants.ENTITY_TEST_RUN_PLAN)) {
				
				TestRunPlan testRunPlan = productListService.getTestRunPlanById(entityId);
				if (testRunPlan == null)
					return null;
	
				jsonSchedule = new JsonSchedule(testRunPlan);
				return viewUpcomingFireTimes(jsonSchedule, numberTimes);
				
			}else if (entityType.equals(IDPAConstants.DATA_EXTRACTOR)) {
				
				DataExtractorScheduleMaster dataExtractorScheduleMaster = dataSourceExtractorService.getDataExtractorScheduleMasterById(entityId);
				if (dataExtractorScheduleMaster == null)
					return null;
	
				jsonSchedule = new JsonSchedule(dataExtractorScheduleMaster);
				return viewUpcomingFireTimes(jsonSchedule, numberTimes);
				
			}
		} catch (Exception e) {
			log.info("Unable to get next fire times", e);
			return null;
		}
		return jsonSchedule;
	}


	@Override
	@Transactional
	public JsonSchedule viewUpcomingFireTimes(JsonSchedule jsonSchedule, int numberTimes) {
	
		
		if (jsonSchedule == null) {
			return null;
		}
		try {
			if (jsonSchedule.getOwnerEntityType().equals(IDPAConstants.ENTITY_TEST_RUN_PLAN)) {
				
				TestRunPlan testRunPlan = productListService.getTestRunPlanById(jsonSchedule.getOwnerEntityId());
				if (testRunPlan == null) {
					log.info("Unable to find Owner entity : " + jsonSchedule.getOwnerEntityType() + " : " + jsonSchedule.getOwnerEntityId());
					return null;
				}
	
				TAFScheduleManager tafScheduleManager = TAFScheduleManager.getTAFScheduleManager();
				List<String> productNames = new ArrayList<String>();
				String productName = testRunPlan.getProductVersionListMaster().getProductMaster().getProductName();
				productNames.add(productName);
				
				Map<String, Map<String, List<Date>>> productsFireTimesMap = tafScheduleManager.getUpcomingFireTimes(productNames, numberTimes);
				
				if (productsFireTimesMap == null) {
					log.info("There are no triggers created for specified products");
					return jsonSchedule;
				}
				jsonSchedule = new JsonSchedule(testRunPlan);
				Map<String, List<Date>> productFireTimes = productsFireTimesMap.get(productName);
				if (productFireTimes == null) {
					
					log.info("There are no triggers created for this product : " + productName);
					return jsonSchedule;
				}
				List<Date> fireTimes = productFireTimes.get(testRunPlan.getTestRunPlanName());
				if (fireTimes == null || fireTimes.size() <= 0) {

					log.info("There are no fire times for this Test Run Plan schedule : " + testRunPlan.getTestRunPlanId());
					return jsonSchedule;
				}
				
				List<String> fireTimesString = new ArrayList<String>();
				for (Date fireTime : fireTimes) {
					log.debug("Firetime : " + fireTime.toString());
					fireTimesString.add(fireTime.toString());
				}
				jsonSchedule.setUpcomingFireTimes(fireTimesString);
			}else if (jsonSchedule.getOwnerEntityType().equals(IDPAConstants.DATA_EXTRACTOR)) {
				
				DataExtractorScheduleMaster dataExtractorScheduleMaster = dataSourceExtractorService.getDataExtractorScheduleMasterById(jsonSchedule.getOwnerEntityId());
				
				if (dataExtractorScheduleMaster == null) {
					log.info("Unable to find Owner entity : " + jsonSchedule.getOwnerEntityType() + " : " + jsonSchedule.getOwnerEntityId());
					return null;
				}
	
				GenericScheduleManager genericScheduleManager = GenericScheduleManager.getScheduleManager();
				List<String> groupNames = new ArrayList<String>();
				String groupName = dataExtractorScheduleMaster.getGroupName();
				groupNames.add(groupName);
				
				Map<String, Map<String, List<Date>>> scheculerFireTimesMap = genericScheduleManager.getUpcomingFireTimes(groupNames, numberTimes);
				
				if (scheculerFireTimesMap == null) {
					log.debug("There are no triggers created");
					return jsonSchedule;
				}
				jsonSchedule = new JsonSchedule(dataExtractorScheduleMaster);
				Map<String, List<Date>> schedulerFireTimes = scheculerFireTimesMap.get(groupName);
				if (schedulerFireTimes == null) {
					
					log.debug("There are no triggers created for : " + groupName);
					return jsonSchedule;
				}
				List<Date> fireTimes = schedulerFireTimes.get(dataExtractorScheduleMaster.getJobName());
				if (fireTimes == null || fireTimes.size() <= 0) {

					log.debug("There are no fire times for : " + dataExtractorScheduleMaster.getJobName());
					return jsonSchedule;
				}
				
				List<String> fireTimesString = new ArrayList<String>();
				for (Date fireTime : fireTimes) {
					log.debug("Firetime : " + fireTime.toString());
					fireTimesString.add(fireTime.toString());
				}
				jsonSchedule.setUpcomingFireTimes(fireTimesString);
			}
		} catch (Exception e) {
			log.error("Unable to get next fire times", e);
			return null;
		}
		return jsonSchedule;
	}
	
	@Transactional
	public JsonSchedule addUpdateDataExtractorSchedule(JsonSchedule jsonSchedule) {

		try {

			log.info("Going to add/update schedule for : " + jsonSchedule.getOwnerEntityType() + "  " + jsonSchedule.getOwnerEntityId());

			DataExtractorScheduleMaster dataExtractorScheduleMaster = dataSourceExtractorService.getDataExtractorScheduleMasterById(jsonSchedule.getOwnerEntityId());
			
			if (dataExtractorScheduleMaster == null)
				return null;
				
			GenericScheduleManager genericScheduleManager = GenericScheduleManager.getScheduleManager();
			
			//Find if the request has a schedule to be added/updated
			boolean requestHasSchedule = false;
			boolean requestCronIsValid =  false;
			if (jsonSchedule.getCronExpression() == null || jsonSchedule.getCronExpression().trim().length() < 1) {
				requestHasSchedule = false;
				log.info("Does not have a schedule");
			} else {
				requestHasSchedule = true;
				requestCronIsValid = genericScheduleManager.isValidCronExpression(jsonSchedule.getCronExpression());
				if (!requestCronIsValid) {
					log.info("Cron Expression is not a valid one : " + jsonSchedule.getCronExpression());
				}
			}

			//Find if the entity TRP has an existing schedule
			boolean entityHasExistingSchedule = false;
			if (dataExtractorScheduleMaster.getCronExpression() == null || dataExtractorScheduleMaster.getCronExpression().trim().length() < 1) {
				entityHasExistingSchedule = false;
				log.info("Have an existing schedule.");
			} else {
				entityHasExistingSchedule = true;
				log.info("Has an existing schedule : " + dataExtractorScheduleMaster.getCronExpression());
			}
				
			//#1. New schedule to be added
			if (requestHasSchedule && !entityHasExistingSchedule) {
				if (requestCronIsValid) {
					updateScheduleDetailsForJobEntity(dataExtractorScheduleMaster, jsonSchedule);
					genericScheduleManager.createSchedule(createJobEntity(dataExtractorScheduleMaster));
					log.info("Added new schedule");
					dataExtractorScheduleMaster.setNextExecution(genericScheduleManager.getTriggerNextFireTime(dataExtractorScheduleMaster.getGroupName(), dataExtractorScheduleMaster.getJobName()));
					
					updateActionEntityDetails(dataExtractorScheduleMaster);
					jsonSchedule = viewUpcomingFireTimes(jsonSchedule, 10);
					jsonSchedule.setMessage("Schedule created successfully");
					return jsonSchedule;
				} else {
					log.info("Not adding a new schedule as the cron expression is not valid");
					return null;
				}
			}
			//#2. Existing Schedule to be updated
			if (requestHasSchedule && entityHasExistingSchedule) {
				if (requestCronIsValid) {
					updateScheduleDetailsForJobEntity(dataExtractorScheduleMaster, jsonSchedule);
					genericScheduleManager.updateSchedule(createJobEntity(dataExtractorScheduleMaster));
					log.info("Updated schedule from : " + dataExtractorScheduleMaster.getCronExpression() + " -> " + jsonSchedule.getCronExpression());
					dataExtractorScheduleMaster.setNextExecution(genericScheduleManager.getTriggerNextFireTime(dataExtractorScheduleMaster.getGroupName(), dataExtractorScheduleMaster.getJobName()));
					updateActionEntityDetails(dataExtractorScheduleMaster);
					jsonSchedule = viewUpcomingFireTimes(jsonSchedule, 10);
					jsonSchedule.setMessage("Schedule updated successfully");
					return jsonSchedule;
				} else {
					log.info("Not updating schedule as the cron expression is not valid");
					return null;
				}
			} 
			
			//#3. Existing schedule to be deleted
			if (!requestHasSchedule && entityHasExistingSchedule) {
				updateScheduleDetailsForJobEntity(dataExtractorScheduleMaster, jsonSchedule);
				genericScheduleManager.deleteSchedule(createJobEntity(dataExtractorScheduleMaster));
				log.info("Existing schedule deleted");
				dataExtractorScheduleMaster.setNextExecution(genericScheduleManager.getTriggerNextFireTime(dataExtractorScheduleMaster.getGroupName(), dataExtractorScheduleMaster.getJobName()));
				updateActionEntityDetails(dataExtractorScheduleMaster);
				jsonSchedule = viewUpcomingFireTimes(jsonSchedule, 10);
				jsonSchedule.setMessage("Schedule deleted successfully");
				return jsonSchedule;
			}
				
			//#4. No existing schedule and no schedule from UI
			if (!requestHasSchedule && !entityHasExistingSchedule) {
				jsonSchedule.setMessage("No existing schedule and no new schedule request from user");
				//Do Nothing
				log.info("No existing schedule and no new schedule request from user. Doing nothing");
				return jsonSchedule;
			}
		} catch (Exception e) {
			log.info("Problem while adding/updating schedule", e);
			return null;
		}
		return jsonSchedule;
	}


	public JsonSchedule runJobNow(Integer entityId, String entityType) {
		JsonSchedule jsonSchedule = null;
		try {
			log.info("Run job now for entity id - "+entityId+" and type "+entityType);
			if (entityType.equals(IDPAConstants.DATA_EXTRACTOR)) {
				DataExtractorScheduleMaster dataExtractorScheduleMaster = dataSourceExtractorService.getDataExtractorScheduleMasterById(entityId);
				
				GenericScheduleManager genericScheduleManager = GenericScheduleManager.getScheduleManager();
				genericScheduleManager.runNow(createJobEntity(dataExtractorScheduleMaster));
				
				jsonSchedule = new JsonSchedule(dataExtractorScheduleMaster);
				jsonSchedule.setMessage("Extraction operation performed");
			}else{
				jsonSchedule = new JsonSchedule();
				jsonSchedule.setMessage("Unable to find entity type - "+entityType);
			}
			
		}catch(Exception ex){
			log.info("Problem while triggering schedule", ex);
			return null;
		}
		
		return jsonSchedule;
	}
}
