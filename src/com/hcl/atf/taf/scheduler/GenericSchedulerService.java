/**
 * 
 */
package com.hcl.atf.taf.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hcl.atf.taf.exceptions.ATFException;
import com.hcl.atf.taf.scheduler.forfuture.JobTriggerDetail;

/**
 * Author: karthivt Company: HCL Technologies. Date: Oct 10, 2007 10:33:46 AM
 * Description: ADD YOUR CLASS DESCRIPTION HERE
 */
public interface GenericSchedulerService {
    
    /**
     * This method will schedule the given job.
     * @param jobClassNameVar -
     *            job class name
     * @param jobEntity -
     *            jobEntity object
     * @return Date -
     * @throws ATFException -
     */
    Date scheduleSimpleJob(final String jobClassNameVar, final JobEntity jobEntity) throws ATFException;
    
    /**
     * This Method will schedule the cronJob.
     * @param job -
     *            SDPSchedulerBean
     * @return Date -
     * @throws ATFException -
     */
    Date scheduleCronJob(JobEntity jobEntity) throws ATFException;

    /**
     * This method will reSchedule the given job.
     * @param jobEntity -
     *            jobEntity object
     * @return SimpleTrigger -
     * @throws ATFException -
     */
    Date reScheduleSimpleJob(final JobEntity jobEntity) throws ATFException;
    
    /**
     * This method will reSchedule the cronjob.
     * @param companyName -
     *            Name of the company
     * @param triggerOldName -
     *            old trigger Name
     * @param triggerNewName -
     *            new trigger Name
     * @param description -
     *            description of the job
     * @param cronExpString -
     *            Seconds 0-59 , - * / Minutes 0-59 , - * / Hours 0-23 , - * /
     *            Day-of-month 1-31 , - * ? / L W Month 1-12 or JAN-DEC , - * /
     *            Day-of-Week 1-7 or SUN-SAT , - * ? / L # Year (Optional)
     *            empty, 1970-2099 , - * /
     * @return Date -
     * @throws ATFException -
     */
    Date reScheduleCronJob(String productName, String triggerOldName, String triggerNewName, String description, String cronExpString) throws ATFException;
    
    /**
     * This method will deletes the given job.
     * @param companyName -
     *            Name of the company
     * @param jobName -
     *            Name of the job
     * @return boolean -
     * @throws ATFException -
     */
    Boolean deleteJob(String groupName, String jobName) throws ATFException;
    
    /**
     * This Method will create a new Job with the given details.
     * @param companyName -
     *            Name of the Company
     * @param jobName -
     *            Name of the Job
     * @param jobDescription -
     *            description of the Job
     * @param jobClassName -
     *            Job class name
     * @throws ATFException -
     */
    void createJob(String groupName, String jobName, String jobDescription, String jobClassName, HashMap<String, Object> jobDataMap) throws ATFException;
    
    /**
     * This method will updates the Job with given details.
     * @param companyName -
     *            Name of the Company
     * @param jobName -
     *            Name of the Job
     * @param jobDescription -
     *            description of the Job
     * @param jobClassName -
     *            Job class name
     * @throws ATFException -
     */
    void updateJob(String groupName, String jobName, String jobDescription, String jobClassName, HashMap<String, Object> jobDataMap) throws ATFException;
    
    /**
     * This method will add a simpleTrigger to the given Job.
     * @param jobEntity -
     *            JobEntity object
     * @return Date -
     * @throws ATFException -
     */
    Date addSimpleTriggerToJob(final JobEntity jobEntity) throws ATFException;
    
    /**
     * This method will adds a cronTrigger to the given Job.
     * @param companyName -
     *            Name of the company
     * @param jobName -
     *            Name of the Job
     * @param triggerName -
     *            Name of the Trigger
     * @param triggerDescription -
     *            description of the trigger
     * @param cronExpression -
     *            cronExpression
     * @return Date -
     * @throws ATFException -
     */
    Date addCronTriggerToJob(String groupName, String jobName, String triggerName, String triggerDescription, String cronExpression) throws ATFException;
    
    /**
     * @param jobGroupName -
     * @return list
     * @throws ATFException -
     */
    List<JobTriggerDetail> getAllJobsByGroup(String jobGroupName) throws ATFException;
    
    /**
     * This method will runs given job.
     * @param jobName -
     *            Name of the Job
     * @param groupName -
     *            Name of the Group
     * @throws ATFException -
     */
    void runJobNow(JobEntity jobEntity) throws ATFException;
    
    /**
     * Method to verify whether jos is exist or not.
     * @param jobName -
     *            name of the job
     * @param groupName -
     *            name of the group
     * @return -
     * @throws ATFException -
     *             ATFException
     */
    Boolean isJobExist(final String jobName, final String groupName) throws ATFException;
    
    /**
     * This method will return JobTriggerDetails for the given company and
     * jobName.
     * @param companyName -
     *            Name of the company
     * @param jobName -
     *            name of the job
     * @return JobTriggerDetail -
     * @throws ATFException -
     */
    JobTriggerDetail getJobByName(String groupName, String jobName) throws ATFException;
    
    /**
     * This Method will modify the given jobTriggerDetail.
     * @param jobTriggerDetail -
     *            JobTriggerDetail to update
     * @return JobTriggerDetail - updated JobTriggerDetail
     * @throws ATFException -
     */
    JobTriggerDetail modifyJobTrigger(JobTriggerDetail jobTriggerDetail) throws ATFException;
   
    /**
     * This method will fetch the previous fire date of the give trigger.
     * @param companyName
     *            - which is group name in scheduler
     * @param triggerName
     *            - trigger name
     * @return Date - previous fire date
     * @throws ATFException
     *             - throws ATFException
     */
    Date getTriggerPreviousFireTime(final String groupName, final String triggerName) throws ATFException;
    
    /**
     * This method will fetch the next fire date of the give trigger.
     * @param companyName -
     *            which is group name in scheduler
     * @param triggerName -
     *            trigger name
     * @return Date - next fire date
     * @throws ATFException -
     *             throws ATFException
     */
    Date getTriggerNextFireTime(String groupName, String triggerName) throws ATFException;
    
    /**
     * This method will fetch the cronTrigger for the Given triggerName and
     * triggerGroup.
     * @param triggerName -
     *            Name of the Trigger
     * @param triggerGroup -
     *            triggerGroup
     * @return String - CronTrigger
     */
    String getCronTrigger(final String triggerName, final String triggerGroup);
    
    /**
     * This method will return the No of Jobs for the given JobGroup.
     * @param jobGroupName -
     *            Name of the jobGroup
     * @return Integer -
     * @throws ATFException -
     */
    Integer getNoOfJobsByGroupName(final String jobGroupName) throws ATFException;

    /**
     * Return the List of times for the Jobs/triggers to fire in the specified period
     * 
     * @param productNames
     * @param fromDate
     * @param toDate
     * @return
     * @throws ATFException
     */
    public Map<String, Map<String, List<Date>>> getUpcomingFireTimes(List<String> groupNames, Date fromDate, Date toDate) throws ATFException;

    /**
     * Return the List of times for the Jobs/triggers to fire
     * 
     * @param productNames
     * @param fromDate
     * @param toDate
     * @return
     * @throws ATFException
     */
    public Map<String, Map<String, List<Date>>> getUpcomingFireTimes(List<String> groupNames, int numberTimes) throws ATFException;

	List<Date> getCalculatedFireTimesFromExpression(String cronExpression, int numberTimes);

	boolean isValidExpression(String cronExpression);

}
