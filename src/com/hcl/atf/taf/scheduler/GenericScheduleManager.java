package com.hcl.atf.taf.scheduler;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hcl.atf.taf.exceptions.ATFException;
import com.hcl.atf.taf.scheduler.quartz.GenericQuartzSchedulerServiceImpl;

public class GenericScheduleManager {
	private static final Log log = LogFactory.getLog(GenericScheduleManager.class);
	private static GenericScheduleManager scheduleManager;
	
	private GenericScheduleManager() {
		
	}
	
	public static GenericScheduleManager getScheduleManager() {
		
		if (scheduleManager == null) {
			
			scheduleManager = new GenericScheduleManager();
		}
		return scheduleManager;
	}
	
	public void createSchedule(JobEntity jobEntity) throws ATFException {
	
        try {
            getSchedulerService().scheduleCronJob(jobEntity);
        } catch (ATFException ae) {
        	throw ae;
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
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

	public void deleteSchedule(JobEntity jobEntity) throws ATFException  {

		GenericSchedulerService genericSchedulerService = getSchedulerService();
		try {

			genericSchedulerService.deleteJob(jobEntity.getGroupName(), jobEntity.getJobName());
        } catch (ATFException ae) {
        	throw ae;
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new ATFException("ATF-Scheduler-1", "Unable to create a new Job & Trigger", e);
        }
	}
	
	public void updateSchedule(JobEntity jobEntity) throws ATFException {
		
		try {
			deleteSchedule(jobEntity);
			createSchedule(jobEntity);
        } catch (ATFException ae) {
        	throw ae;
        } catch (Exception e) {
        	throw new ATFException("ATF-Scheduler-1", "Unable to create a new Job & Trigger", e);
        }
	}

	public void updateScheduleTrigger(JobEntity jobEntity) throws ATFException {
		
		GenericSchedulerService genericSchedulerService = getSchedulerService();
		try {

			genericSchedulerService.reScheduleCronJob(jobEntity.getGroupName(), jobEntity.getJobName()+"-Trigger", jobEntity.getJobName()+"-Trigger", jobEntity.getDescription(), jobEntity.getScheduleCronExpression());
        } catch (ATFException ae) {
        	throw ae;
        } catch (Exception e) {
        	throw new ATFException("ATF-Scheduler-1", "Unable to create a new Job & Trigger", e);
        }
	}

	public void addScheduleTrigger(JobEntity jobEntity) throws ATFException {
		
		GenericSchedulerService genericSchedulerService = getSchedulerService();
		try {
			genericSchedulerService.reScheduleCronJob(jobEntity.getGroupName(), jobEntity.getJobName()+"-Trigger", jobEntity.getJobName()+"-Trigger", jobEntity.getDescription(), jobEntity.getScheduleCronExpression());
        } catch (ATFException ae) {
        	throw ae;
        } catch (Exception e) {
        	throw new ATFException("ATF-Scheduler-1", "Unable to create a new Job & Trigger", e);
        }
	}

	public void deleteScheduleTrigger(JobEntity jobEntity) throws ATFException {
		
	}

	public void runNow(JobEntity jobEntity) throws ATFException  {
		
		GenericSchedulerService genericSchedulerService = getSchedulerService();
		try {

			genericSchedulerService.runJobNow(jobEntity);
        } catch (ATFException ae) {
        	throw ae;
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	public Map<String, Map<String, List<Date>>> getUpcomingFireTimes(List<String> groupNames, Date fromDate, Date toDate) throws ATFException{
		
		if (fromDate == null)
			fromDate = new Date(System.currentTimeMillis());
		

		if (toDate == null) {
			Calendar cal = Calendar.getInstance();  
			cal.setTime(fromDate);  
			cal.add(Calendar.DATE, 30); 
			toDate = cal.getTime();
		}
		
		GenericSchedulerService genericSchedulerService = getSchedulerService();
		return genericSchedulerService.getUpcomingFireTimes(groupNames, fromDate, toDate);
	}
	
	public Map<String, Map<String, List<Date>>> getUpcomingFireTimes(List<String> groupNames, int numberTimes) throws ATFException{
		
		if (groupNames == null || groupNames.isEmpty()) {
			
			return null;
		}
		
		GenericSchedulerService genericSchedulerService = getSchedulerService();
		
		return genericSchedulerService.getUpcomingFireTimes(groupNames, numberTimes);
	}

	public List<Date> getCalculatedFireTimesFromExpression(String cronExpression, int numberTimes) throws ATFException{
		
		if (cronExpression == null || cronExpression.trim().length() < 0) {
			return null;
		}
				
		GenericSchedulerService genericSchedulerService = getSchedulerService();
		return genericSchedulerService.getCalculatedFireTimesFromExpression(cronExpression, numberTimes);
	}
	
	public Date getTriggerPreviousFireTime(String groupName, String triggerName) throws ATFException{
		
		GenericSchedulerService genericSchedulerService = getSchedulerService();
		return genericSchedulerService.getTriggerPreviousFireTime(groupName, triggerName);
		
	}
	
	public Date getTriggerNextFireTime(String groupName, String triggerName) throws ATFException{
		
		GenericSchedulerService genericSchedulerService = getSchedulerService();
		return genericSchedulerService.getTriggerNextFireTime(groupName, triggerName);
		
	}

	public boolean isValidCronExpression(String cronExpression) throws ATFException{
		
		return getSchedulerService().isValidExpression(cronExpression);
	}
	
    private GenericSchedulerService getSchedulerService() {

    	GenericQuartzSchedulerServiceImpl geneSchedulerServiceImpl = new GenericQuartzSchedulerServiceImpl();
    	geneSchedulerServiceImpl.init();
    	return geneSchedulerServiceImpl;
    }

    public GenericSchedulerService startSchedulerService() {
    	
    	GenericQuartzSchedulerServiceImpl geneSchedulerServiceImpl = new GenericQuartzSchedulerServiceImpl();
    	geneSchedulerServiceImpl.init();
    	return geneSchedulerServiceImpl;
    }
    
}
