package com.hcl.atf.taf.scheduler;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hcl.atf.taf.exceptions.ATFException;
import com.hcl.atf.taf.scheduler.quartz.QuartzSchedulerServiceImpl;

/**
 * This class facilitates the creation of schedules for TestConfigs for a product version.<br>
 * Each TestConfig will have one Job and one schedule associated with the Job(Trigger in Quartz scheduler)<br>
 * 
 * Copyright (c) 2013, HCL Technologies.<br>
 * Sep 12, 2013 2:54:28 PM<br>
 * @author rajeshbabus
 * @version $Id$
 */
public class TAFScheduleManager {
	private static final Log log = LogFactory.getLog(TAFScheduleManager.class);
	public static final String TestConfigCycleJobClassName = "com.hcl.atf.taf.scheduler.DefaultTestConfigCycleJob";
	
	private static TAFScheduleManager tafScheduleManagerSingleton;
	/**
	 * Default constructor
	 */
	private TAFScheduleManager() {
		
	}
	
	/**
	 * Get access to the TAFScheduleManager object
	 */
	public static TAFScheduleManager getTAFScheduleManager() {
		
		if (tafScheduleManagerSingleton == null) {
			
			tafScheduleManagerSingleton = new TAFScheduleManager();
		}
		return tafScheduleManagerSingleton;
	}
	
	/**
	 * Create a new Job and schedule Trigger for a test cycle configuration.
	 * This will execute the TestCycleRunJob which will run the test cases on the targeted devices
	 * 
	 * @param scheduleEntity The Test cycle details for which the schedule has to be created
	 */
	public void createTestConfigSchedule(TestConfigScheduleEntity scheduleEntity) throws ATFException {
	
        try {
            getSchedulerService().scheduleCronJob(scheduleEntity);
        } catch (ATFException ae) {
        	throw ae;
        } catch (Exception e) {
        	throw new ATFException("ATF-Scheduler-1", "Unable to create a new Job & Trigger for TestConfig", e);
        }
	}
	
	/**
	 * Create a new Job and schedule Trigger for a test cycle configuration.
	 * This will execute the TestCycleRunJob which will run the test cases on the targeted devices
	 * 
	 * @param scheduleEntity The Test cycle details for which the schedule has to be created
	 */
	public void createTerminalAvailabilityUpdateSchedule(JobEntity jobEntity) throws ATFException {
	
        try {
            getSchedulerService().scheduleCronJob(jobEntity);
        } catch (ATFException ae) {
        	throw ae;
        } catch (Exception e) {
        	log.error("ERROR  ",e);
        	throw new ATFException("ATF-Scheduler-1", "Unable to create a new Job & Trigger for the specified settings", e);
        }
	}

	/**
	 * Delete an existing Job and schedule Trigger for a test cycle configuration.
	 * 
	 * @param scheduleEntity The Test cycle details for which the schedule has to be deleted
	 */
	public void deleteTestConfigSchedule(TestConfigScheduleEntity scheduleEntity) throws ATFException  {

		
		SchedulerService schedulerService = getSchedulerService();
		try {

			schedulerService.deleteJob(scheduleEntity.getProductName(), scheduleEntity.getTestConfigName());
        } catch (ATFException ae) {
        	throw ae;
        } catch (Exception e) {
        	log.error("ERROR  ",e);
        	throw new ATFException("ATF-Scheduler-1", "Unable to create a new Job & Trigger for TestConfig", e);
        }
	}
	
	/**
	 * Update a existing Job and schedule Trigger for a test cycle configuration.
	 * This will execute the TestCycleRunJob which will run the test cases on the targeted devices with 
	 * the updated schedule.
	 * 
	 * @param scheduleEntity The Test cycle details for which the schedule has to be deleted
	 */
	public void updateTestConfigSchedule(TestConfigScheduleEntity scheduleEntity) throws ATFException {
		
		try {

			//For now, delete the existing job and create a new job and trigger
			deleteTestConfigSchedule(scheduleEntity);
			createTestConfigSchedule(scheduleEntity);
        } catch (ATFException ae) {
        	throw ae;
        } catch (Exception e) {
        	throw new ATFException("ATF-Scheduler-1", "Unable to create a new Job & Trigger for TestConfig", e);
        }
	}

	/**
	 * Change the schedule for a given TestConfig
	 * This will execute the TestCycleRunJob which will run the test cases on the targeted devices with 
	 * the updated schedule.
	 * 
	 * @param scheduleEntity The Test cycle details for which the schedule has to be deleted
	 */
	public void updateTestConfigScheduleTrigger(TestConfigScheduleEntity scheduleEntity) throws ATFException {
		
		SchedulerService schedulerService = getSchedulerService();
		try {

			schedulerService.reScheduleCronJob(scheduleEntity.getProductName(), scheduleEntity.getTestConfigName()+"-Trigger", scheduleEntity.getTestConfigName()+"-Trigger", scheduleEntity.getTestConfigDescription(), scheduleEntity.getScheduleCronExpression());
        } catch (ATFException ae) {
        	throw ae;
        } catch (Exception e) {
        	throw new ATFException("ATF-Scheduler-1", "Unable to create a new Job & Trigger for TestConfig", e);
        }
	}

	/**
	 * Add a schedule to an existing Job for a given TestConfig
	 * This will execute the TestCycleRunJob which will run the test cases on the targeted devices with 
	 * the updated schedule.
	 * 
	 * @param scheduleEntity The Test cycle details for which the schedule has to be deleted
	 */
	public void addTestConfigScheduleTrigger(TestConfigScheduleEntity scheduleEntity) throws ATFException {
		
		SchedulerService schedulerService = getSchedulerService();
		try {

			//TODO : Implement the functionality to add a trigger to an existing Job for the Test Cycle Config
			schedulerService.reScheduleCronJob(scheduleEntity.getProductName(), scheduleEntity.getTestConfigName()+"-Trigger", scheduleEntity.getTestConfigName()+"-Trigger", scheduleEntity.getTestConfigDescription(), scheduleEntity.getScheduleCronExpression());
        } catch (ATFException ae) {
        	throw ae;
        } catch (Exception e) {
        	throw new ATFException("ATF-Scheduler-1", "Unable to create a new Job & Trigger for TestConfig", e);
        }
	}

	/**
	 * Delete the schedule for a TestConfig, while retaining the jon
	 * TODO
	 * 
	 * @param scheduleEntity The Test cycle details for which the schedule has to be deleted
	 */
	public void deleteTestCycleScheduleTrigger(TestConfigScheduleEntity scheduleEntity) throws ATFException {
		
	}

	/**
	 * Run a TestConfig immediately, without a schedule. One time execution
	 * 
	 * @param scheduleEntity The Test cycle details for which the schedule has to be deleted
	 */
	public void runTestConfigNow(TestConfigScheduleEntity scheduleEntity) throws ATFException  {
		
		SchedulerService schedulerService = getSchedulerService();
		try {

			schedulerService.runJobNow(scheduleEntity.getTestConfigName(), scheduleEntity.getProductName());
        } catch (ATFException ae) {
        	throw ae;
        } catch (Exception e) {
        	throw new ATFException("ATF-Scheduler-1", "Unable to create a new Job & Trigger for TestConfig", e);
        }
	}
	
	/**
	 * Get the list of jobs that are scheduled for the specified duration
	 * TODO
	 */
	public Map<String, Map<String, List<Date>>> getUpcomingFireTimes(List<String> productNames, Date fromDate, Date toDate) throws ATFException{
		
		if (fromDate == null)
			fromDate = new Date(System.currentTimeMillis());
		

		if (toDate == null) {
			Calendar cal = Calendar.getInstance();  
			cal.setTime(fromDate);  
			cal.add(Calendar.DATE, 30); 
			toDate = cal.getTime();
		}
		
		SchedulerService schedulerService = getSchedulerService();
		
		return schedulerService.getUpcomingFireTimes(productNames, fromDate, toDate);
	}
	
	/**
	 * Get the list of jobs that are scheduled for the specified duration
	 */
	public Map<String, Map<String, List<Date>>> getUpcomingFireTimes(List<String> productNames, int numberTimes) throws ATFException{
		
		if (productNames == null || productNames.isEmpty()) {
			
			return null;
		}
		
		SchedulerService schedulerService = getSchedulerService();
		
		return schedulerService.getUpcomingFireTimes(productNames, numberTimes);
	}

	/**
	 * Get the list of jobs that are scheduled for the specified duration
	 */
	public List<Date> getCalculatedFireTimesFromExpression(String cronExpression, int numberTimes) throws ATFException{
		
		if (cronExpression == null || cronExpression.trim().length() < 0) {
			return null;
		}
				
		SchedulerService schedulerService = getSchedulerService();
		return schedulerService.getCalculatedFireTimesFromExpression(cronExpression, numberTimes);
	}

	/**
	 * Get the list of jobs that are scheduled for the specified duration
	 */
	public boolean isValidCronExpression(String cronExpression) throws ATFException{
		
		return getSchedulerService().isValidExpression(cronExpression);
	}
	
	
	/**
	 * Internal method to get a scheduler service to access the scheduler framework
	 * TODO
	 */
    private SchedulerService getSchedulerService() {
    	
    	//TODO : Get it from a Factory
    	//For now, directly instantiate the SchedulerServiceImpl
    	QuartzSchedulerServiceImpl schedulerServiceImpl = new QuartzSchedulerServiceImpl();
    	schedulerServiceImpl.init();
    	return schedulerServiceImpl;
    }

	/**
	 * Start the scheduler service. To be used when the application is launched. One time only
	 * TODO
	 */
    public SchedulerService startSchedulerService() {
    	
    	//For now, directly instantiate the SchedulerServiceImpl
    	QuartzSchedulerServiceImpl schedulerServiceImpl = new QuartzSchedulerServiceImpl();
    	schedulerServiceImpl.init();
    	return schedulerServiceImpl;
    }
    
    public static void main(String[] args) {
    	
    	//For unit testing
    	TAFScheduleManager scheduleManager = new TAFScheduleManager();
    	
    	//Run the existing job schedules in the schedule
    	scheduleManager.startSchedulerService();
    	
    	//Create schedule for Terminal Availability Updating Activity
    	JobEntity jobEntity = new JobEntity();
    	jobEntity.setGroupName("TAFSchedules");
    	jobEntity.setJobName("UpdateTerminalAvailability");
    	jobEntity.setTriggerName("UpdateTerminalAvailability-Trigger");
    	jobEntity.setJobClassName("com.hcl.atf.taf.scheduler.jobs.TerminalsAvailabilityHeartBeatJob");
    	jobEntity.setScheduleCronExpression("0 0/3 * * * ?");
    	jobEntity.setTriggerDescription("Trigger job to check for terminals availability and update status");

   		//Create Schedule for TestConfig
   		try {
   			scheduleManager.createTerminalAvailabilityUpdateSchedule(jobEntity);
   		} catch (Exception e) {
   			log.error("ERROR  ",e);
   		}
   		
   		List<String> productNames = new ArrayList<String>();
   		productNames.add("Optimum Mobile Television");
   		productNames.add("TAFSchedules");
   		try {
   			Map<String, Map<String, List<Date>>> upcomingJobs = scheduleManager.getUpcomingFireTimes(productNames, null, null);  		//Delete Schedule for TestConfig
   	   		Set<String> productNameKeys = upcomingJobs.keySet();
   	   		for (String productName : productNameKeys) {
   	   			
   	   			Map<String, List<Date>> triggerNames = upcomingJobs.get(productName);
   	   	   		Set<String> triggerNameKeys = triggerNames.keySet();
   	   			for (String triggerName : triggerNameKeys) {
   	   				
   	   				List<Date> dates = triggerNames.get(triggerName);
   	   				for (Date date : dates) {
   	   					
   	   					
   	   				}
   	   			}
   	   		}
   	   	} catch (Exception e) {
   			
   			log.error("ERROR  ",e);
   		}
    }

}
