/**
 * 
 */
package com.hcl.atf.taf.scheduler.quartz;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.OperableTrigger;

import com.hcl.atf.taf.exceptions.ATFException;
import com.hcl.atf.taf.scheduler.GenericSchedulerService;
import com.hcl.atf.taf.scheduler.JobEntity;
import com.hcl.atf.taf.scheduler.forfuture.JobTriggerDetail;

/**
 * Author: karthivt Company: HCL Technologies. Date: Oct 3, 2007 12:28:12 PM
 * Description: ADD YOUR CLASS DESCRIPTION HERE
 */
@SuppressWarnings("unchecked") public class GenericQuartzSchedulerServiceImpl implements GenericSchedulerService {
    
    /**
     * instance of Scheduler Class .
     */
    private Scheduler scheduler;
    
    /**
     * Default constructor.
     */
    public GenericQuartzSchedulerServiceImpl() {
    }
    
    /**
     * DB driver.
     */
    private String driver;
    
    /**
     * DB Url.
     */
    private String dbUrl;
    
    /**
     * DB user name.
     */
    private String userName;
    
    /**
     * DB password.
     */
    private String password;
    
    /**
     * datasource .
     */
    private String datasource;
    
	private static final Log log = LogFactory.getLog(GenericQuartzSchedulerServiceImpl.class);

    
    /**
     * scheduler initialization method.
     */
    public void init() {
        try {
           
            
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
            
        } catch (final Throwable t) {
        	log.error("Quartz Scheduler Error : Unable to initiate scheduler" , t);
        	t.printStackTrace();
            throw new ExceptionInInitializerError(t);
        }
    }
    
    /**
     * This Method will create a new Job with the given details.
     * @param companyName
     *            - Name of the Company
     * @param jobName
     *            - Name of the Job
     * @param jobDescription
     *            - description of the Job
     * @param jobClassName
     *            - Job class name
     * @throws ATFException
     *             -
     */
    public void createJob(final String groupName, final String jobName, final String jobDescription,
            final String jobClassName, HashMap<String, Object> jobDataMap) throws ATFException {
        
        Class jobClass;
        
        try {
            jobClass = Class.forName(jobClassName);
            
        } catch (final ClassNotFoundException e) {
        	log.error("Quartz Scheduler Error : Unable to find Job Class" , e);
            throw new ATFException(e);
        }
        
        JobDetail jobDetailObj = newJob(jobClass)
        							.withIdentity(jobName, groupName)
        							.withDescription(jobDescription)
        							.storeDurably()
        							.usingJobData(new JobDataMap(jobDataMap))
        							.build();
        
        addJob(jobDetailObj, Boolean.FALSE);
    }
    
    /**
     * This method will updates the Job with given details.
     * @param companyName
     *            - Name of the Company
     * @param jobName
     *            - Name of the Job
     * @param jobDescription
     *            - description of the Job
     * @param jobClassName
     *            - Job class name
     * @throws ATFException
     *             -
     */
    public void updateJob(final String groupName, final String jobName, final String jobDescription,
            final String jobClassName, HashMap<String, Object> jobDataMap) throws ATFException {
        
        Class jobClass;
        
        try {

        	jobClass = Class.forName(jobClassName);
        } catch (final ClassNotFoundException e) {

        	log.error("Quartz Scheduler Error : Unable to find Job Class" , e);
            throw new ATFException(e);
        }
        
        JobDetail jobDetailObj = newJob(jobClass)
		.withIdentity(jobName, groupName)
		.withDescription(jobDescription)
		.storeDurably()
		.usingJobData(new JobDataMap(jobDataMap))
		.build();

        addJob(jobDetailObj, Boolean.TRUE);
    }
    
    /**
     * This method will add a simpleTrigger to the given Job.
     * @param jobEntity
     *            - JobEntity object
     * @return Date -
     * @throws ATFException
     *             -
     */
    public Date addSimpleTriggerToJob(final JobEntity jobEntity) throws ATFException {
        
        Date returnValue;
        
        try {
            final JobDetail jobDetailObj = scheduler.getJobDetail(new JobKey(jobEntity.getJobName(), jobEntity.getGroupName()));
            final SimpleTrigger simpleTrigger = constructSimpleTrigger(jobEntity);
            returnValue = scheduleJob(jobDetailObj, simpleTrigger);
            
        } catch (final SchedulerException e) {
        	log.error("Quartz Scheduler Error : Unable to add trigger to Job" , e);
            throw new ATFException(e);
        }
        
        return returnValue;
    }
    
    /**
     * This method will schedule the given job.
     * @param jobClassNameVar
     *            - job class name
     * @param jobEntity
     *            - jobEntity object
     * @return Date -
     */
    public Date scheduleSimpleJob(final String jobClassNameVar, final JobEntity jobEntity) throws ATFException {
        
        Date returnValue = new Date();
        
        try {
            
            final Class jobClass = Class.forName(jobClassNameVar);
            
            JobDetail jobDetail = newJob(jobClass)
            	.withIdentity(jobEntity.getJobName(), jobEntity.getGroupName())
				.withDescription(jobEntity.getDescription())
				.storeDurably()
				.usingJobData(new JobDataMap(jobEntity.getDataMap()))
				.build();


            
            for (final Object element : jobEntity.getDataMap().keySet()) {
                final String key = (String) element;
                final Object value = jobEntity.getDataMap().get(key);
                jobDetail.getJobDataMap().put(key, value);
            }
            final SimpleTrigger simpleTrigger = constructSimpleTrigger(jobEntity);
            returnValue = scheduleJob(jobDetail, simpleTrigger);
            
        } catch (final ClassNotFoundException e) {
        	log.error("Quartz Scheduler Error : Unable to create simple job" , e);
            throw new ATFException(e);
        }
        
        return returnValue;
    }

    
    /**
     * This method will construct a simpleTrigger.
     * @param jobEntity
     *            -
     * @return SimpleTrigger -
     */
    private SimpleTrigger constructSimpleTrigger(final JobEntity jobEntity) {
        
    	SimpleTrigger simpleTrigger = null;
    	try {
	    	simpleTrigger = (SimpleTrigger) newTrigger()
	        .withIdentity(jobEntity.getTriggerName(), jobEntity.getGroupName())
	        .withDescription(jobEntity.getDescription())
	        .startAt(jobEntity.getStartTime())
	        .endAt(jobEntity.getEndTime())
	        .withSchedule(simpleSchedule()
	                .withRepeatCount(jobEntity.getRepeatCount())
	                .withIntervalInMilliseconds(jobEntity.getRepeatInterval()))
	        .usingJobData(new JobDataMap(jobEntity.getDataMap()))
	        .build();
    	} catch (Exception e) {
        	log.error("Quartz Scheduler Error : Unable to create simple trigger" , e);
    		
    	}
    	
    	
        return simpleTrigger;
    }
    
    
    
    /**
     * This Method will schedule the cronJob.
     * @param job
     *            - SDPSchedulerBean
     * @return Date -
     * @throws ATFException
     *             -
     */
    public Date scheduleCronJob(JobEntity job) throws ATFException {
    	
    	Date returnValue = null;;
        if (isJobExist(job.getJobName(), job.getGroupName()))
        	return returnValue;

        try {
            final Class jobClass = Class.forName(job.getJobClassName());
            JobDetail jobDetail = newJob(jobClass)
            	.withIdentity(job.getJobName(), job.getGroupName())
            	.withDescription(job.getTriggerDescription())
            	.storeDurably()
            	.usingJobData(new JobDataMap(job.getDataMap()))
            	.build();
            
            final CronTrigger cronTrigger =
                    constructCronTrigger(job.getGroupName(), job.getTriggerName(), job.getTriggerDescription(),
                            job.getScheduleCronExpression(), job.getStartTime(), job.getEndTime());
            
            returnValue = scheduleJob(jobDetail, cronTrigger);
            
        } catch (final ClassNotFoundException e) {
        	log.error("Quartz Scheduler Error : Unable to find Job Class" , e);
            throw new ATFException(e);
        } catch (final ATFException e) {
        	log.error("Quartz Scheduler Error : Unable to schedule cron job" , e);
            throw new ATFException(e);
        }
        
        return returnValue;
    }

    
    /**
     * This method will schedule the given job.
     * @param jobDetails
     *            - job details
     * @param trigger
     *            - trigger
     * @return returnValDate -
     * @throws ATFException
     *             -
     */
    private Date scheduleJob(final JobDetail jobDetails, final Trigger trigger) throws ATFException {
        Date returnValDate;
        
        try {
            returnValDate = scheduler.scheduleJob(jobDetails, trigger);
            
        } catch (final SchedulerException e) {
        	log.error("Quartz Scheduler Error : Unable to schedule job" , e);
            throw new ATFException(e);
        }
        
        return returnValDate;
    }

    /**
     * This method will reSchedule the given job.
     * @param jobEntity
     *            - jobEntity object
     * @return SimpleTrigger -
     * @throws ATFException
     *             -
     */
    public Date reScheduleSimpleJob(final JobEntity jobEntity) throws ATFException {
        
        final Date returnValue = new Date();
        
        try {
            final SimpleTrigger trigger = constructSimpleTrigger(jobEntity);
            scheduler.rescheduleJob(new TriggerKey(jobEntity.getOldTriggername(), jobEntity.getGroupName()), trigger);
            
        } catch (final SchedulerException e) {
        	log.error("Quartz Scheduler Error : Unable to reschedule cron job" , e);
            throw new ATFException(e);
        }
        
        return returnValue;
    }
    
    /**
     * This method will construct a new cronTrigger.
     * @param companyName
     *            - Name of the company
     * @param triggerName
     *            - name of the Trigger
     * @param description
     *            - description of the trigger
     * @param cronExpString
     *            - Seconds 0-59 , - * / Minutes 0-59 , - * / Hours 0-23 , - * /
     *            Day-of-month 1-31 , - * ? / L W Month 1-12 or JAN-DEC , - * /
     *            Day-of-Week 1-7 or SUN-SAT , - * ? / L # Year (Optional)
     *            empty, 1970-2099 , - * /
     * @return CronTrigger -
     * @throws ATFException
     *             -
     */
    private CronTrigger constructCronTrigger(final String companyName, final String triggerName,
            final String description, final String cronExpString, Date startDate, Date endDate) throws ATFException {
        

    	CronTrigger cronTrigger;

        try {
        	Calendar cal = Calendar.getInstance();
        	cal.setTime(new Date());
        	if (startDate == null)
        		startDate =  cal.getTime();
        	if (endDate == null) {
        		cal.add(Calendar.DATE, 1);
        		endDate = cal.getTime();
        	}
            final CronExpression cronExpression = new CronExpression(cronExpString);
        	cronTrigger = (CronTrigger) newTrigger()
            .withIdentity(triggerName, companyName)
            .withDescription(description)
            .startAt(startDate)
            .endAt(endDate)
            .withSchedule(cronSchedule(cronExpression))
            .build();

        } catch (final ParseException e) {
        	log.error("Quartz Scheduler Error : Unable to parse cron expression" , e);
            throw new ATFException(e);
        }
        return cronTrigger;
    }
    
    /**
     * This method will reSchedule the cronjob.
     * @param companyName
     *            - Name of the company
     * @param triggerOldName
     *            - old trigger Name
     * @param triggerNewName
     *            - new trigger Name
     * @param description
     *            - description of the job
     * @param cronExpString
     *            - Seconds 0-59 , - * / Minutes 0-59 , - * / Hours 0-23 , - * /
     *            Day-of-month 1-31 , - * ? / L W Month 1-12 or JAN-DEC , - * /
     *            Day-of-Week 1-7 or SUN-SAT , - * ? / L # Year (Optional)
     *            empty, 1970-2099 , - * /
     * @return Date -
     * @throws ATFException
     *             -
     */
    public Date reScheduleCronJob(final String groupName, final String triggerOldName, final String triggerNewName,
            final String description, final String cronExpString) throws ATFException {
        
        Date returnValue;
        
        try {
            String triggerName = null;
            
            if (triggerNewName == null) {
                triggerName = triggerOldName;
                
            } else {
                triggerName = triggerNewName;
            }
            
            final CronTrigger cronTrigger = constructCronTrigger(groupName, triggerName, description, cronExpString,null,null);
            
            returnValue = scheduler.rescheduleJob(new TriggerKey(triggerOldName, groupName), cronTrigger);
            
        } catch (final SchedulerException e) {
        	log.error("Quartz Scheduler Error : Unable to reschedule cron job" , e);
            throw new ATFException(e);
        }
        
        return returnValue;
    }
    
    /**
     * This method will deletes the given job.
     * @param companyName
     *            - Name of the company
     * @param jobName
     *            - Name of the job
     * @return boolean -
     * @throws ATFException
     *             -
     */
    public Boolean deleteJob(final String groupName, final String jobName) throws ATFException {
        
        boolean returnValue = false;
        
        try {
            log.info("Deleting Cron Trigger for : " +  groupName + " : Test Run Plan : " + jobName);
        	if (scheduler.checkExists(new JobKey(jobName, groupName))) {
            	log.info("Job Exists for deleting : " + jobName);
                returnValue = scheduler.deleteJob(new JobKey(jobName, groupName));
            	log.info("Deleted job : " + jobName);
            }
        } catch (final SchedulerException e) {
        	log.error("Quartz Scheduler Error : Unable to delete job" , e);
            throw new ATFException(e);
        } catch (Exception e) {
        	log.error("Quartz Scheduler Error : Unable to delete job" , e);
            throw new ATFException(e);
        }
        return returnValue;
    }
    
    /**
     * This method will add a job.
     * @param job
     *            -
     * @param createOrUpdate
     *            -
     * @throws ATFException
     *             -
     */
    private void addJob(final JobDetail job, final boolean createOrUpdate) throws ATFException {
        
        try {
            scheduler.addJob(job, createOrUpdate);
        } catch (final SchedulerException e) {
        	log.error("Quartz Scheduler Error : Unable to add job" , e);
            throw new ATFException(e);
        }
    }
    
    /**
     * This method will fetch all simple jobs by jobGroup.
     * @param jobGroupName
     *            - Name of the jobGroup
     * @return List -
     * @throws ATFException
     *             -
     */
    public List<JobTriggerDetail> getAllJobsByGroup(final String jobGroupName) throws ATFException {
        
        String[] jobs;
        final List<JobTriggerDetail> jobDetailObj = new ArrayList<JobTriggerDetail>();
        
        try {

        	Set<JobKey> jobKeysOfGroup = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroupName));

        	Iterator<JobKey> jobKeyIterator = jobKeysOfGroup.iterator();
        	JobKey jobKey;
        	while (jobKeyIterator.hasNext()) {
        		
        		jobKey = jobKeyIterator.next();
        		scheduler.getJobDetail(jobKey);
        		jobDetailObj.add(getJobByName(jobGroupName, jobKey.getName()));
        	}
        		
        } catch (final SchedulerException e) {
        	log.error("Quartz Scheduler Error : Unable to get jobs for the group / product" , e);
            throw new ATFException(e);
        }
        
        return jobDetailObj;
    }
    
    /**
     * This method will return the No of Jobs for the given JobGroup.
     * @param jobGroupName
     *            - Name of the jobGroup
     * @return Integer -
     * @throws ATFException
     *             -
     */
    public Integer getNoOfJobsByGroupName(final String jobGroupName) throws ATFException {
        
        try {
        	Set<JobKey> jobKeysOfGroup = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroupName));
        	return new Integer(jobKeysOfGroup.size());
        	
        } catch (final SchedulerException e) {
        	log.error("Quartz Scheduler Error : Unable to get jobs count" , e);
            throw new ATFException(e);
        }
    }
    
    /**
     * This method will runs given job.
     * @param jobName
     *            - Name of the Job
     * @param groupName
     *            - Name of the Group
     * @throws ATFException
     *             -
     */
    public void runJobNow(JobEntity jobEntity) throws ATFException {
        try {
        	if (scheduler.checkExists(new JobKey(jobEntity.getJobName(), jobEntity.getGroupName()))) {
        		scheduler.triggerJob(new JobKey(jobEntity.getJobName(), jobEntity.getGroupName()));
            	log.info("Job exectued : " + jobEntity.getJobName());
            }else{
            	jobEntity.setStartTime(new Date());
            	jobEntity.setEndTime(new Date());
            	scheduleSimpleJob(jobEntity.getJobClassName(), jobEntity);
            	if (scheduler.checkExists(new JobKey(jobEntity.getJobName(), jobEntity.getGroupName()))) {
            		scheduler.triggerJob(new JobKey(jobEntity.getJobName(), jobEntity.getGroupName()));
                	log.info("Simple trigger configured and executed : " + jobEntity.getJobName());
                }
            }
        	
        	while(true){
        		try {
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
        		
        		List<JobExecutionContext> jobExecutionContexts = scheduler.getCurrentlyExecutingJobs();
        		if(jobExecutionContexts != null){
        			boolean isMyJobCompleted = false;
        			int count = 0;
        			for(JobExecutionContext jobExecutionContext : jobExecutionContexts){
        				String jobName = jobExecutionContext.getJobDetail().getKey().getName();
        				String groupName = jobExecutionContext.getJobDetail().getKey().getGroup();
        				if(jobEntity.getJobName().equalsIgnoreCase(jobName) && jobEntity.getGroupName().equalsIgnoreCase(groupName)){
        					isMyJobCompleted = false;
        					break;
        				}
        				count++;
        			}
        			if(count == jobExecutionContexts.size()){
        				isMyJobCompleted = true;
        			}
        			if(isMyJobCompleted){
        				break;
        			}
        		}else{
        			break;
        		}
        	}
        	log.info("Execotion completed for job : " + jobEntity.getJobName());
        } catch (final SchedulerException e) {
        	log.error("Quartz Scheduler Error : Unable to run job immediately" , e);
            throw new ATFException(e);
        }
    }
    
    /**
     * This method will adds a cronTrigger to the given Job.
     * @param companyName
     *            - Name of the company
     * @param jobName
     *            - Name of the Job
     * @param triggerName
     *            - Name of the Trigger
     * @param description
     *            - description of the trigger
     * @param cronExpression
     *            - cronExpression
     * @return Date -
     * @throws ATFException
     *             -
     */
    public Date addCronTriggerToJob(final String companyName, final String jobName, final String triggerName,
            final String description, final String cronExpression) throws ATFException {
        
        Date returnValue;
        try {
            
            final JobDetail job = scheduler.getJobDetail(new JobKey(jobName, Scheduler.DEFAULT_GROUP));
            
            final CronTrigger trigger = constructCronTrigger(companyName, triggerName, description, cronExpression,null,null);
            
            returnValue = scheduleJob(job, trigger);
            
        } catch (final SchedulerException e) {
        	log.error("Quartz Scheduler Error : Unable to add trigger to job" , e);
            throw new ATFException(e);
        }
        return returnValue;
    }
    
    /**
     * This method will release the scheduler.
     * @throws ATFException
     *             -
     */
    public void release() throws ATFException {
        try {
            scheduler.shutdown(Boolean.TRUE);
        } catch (final SchedulerException se) {
            try {
                scheduler.shutdown();
            	log.error("Quartz Scheduler Error : Unable to release scheduler" , se);
                throw new ATFException(se);
            } catch (final SchedulerException e) {
            	log.error("Quartz Scheduler Error : Unable to release scheduler" , se);
                throw new ATFException(e);
            }
        }
    }
    
    /**
     * Method to verify whether jos is exist or not.
     * @param jobName
     *            - name of the job
     * @param groupName
     *            - name of the group
     * @return boolean
     * @throws ATFException
     *             - ATFException
     */
    public Boolean isJobExist(final String jobName, final String groupName) throws ATFException {
        try {
            final JobDetail jobDetailObj = scheduler.getJobDetail(new JobKey(jobName, groupName));
            if (jobDetailObj != null) {
                return true;
            }
            return false;
        } catch (final SchedulerException se) {
        	log.error("Quartz Scheduler Error : Unable to find if job already exists" , se);
            throw new ATFException(se);
        }
    }
    
    /**
     * This method will return JobTriggerDetails for the given company and
     * jobName.
     * @param companyName
     *            - Name of the company
     * @param jobName
     *            - name of the job
     * @return JobTriggerDetail -
     * @throws ATFException
     *             -
     */
    public JobTriggerDetail getJobByName(final String groupName, final String jobName) throws ATFException {
        
    	final JobTriggerDetail jobTriggerDetail = new JobTriggerDetail();
        
        try {
        	
        	
            final JobDetail jobDetailObj = scheduler.getJobDetail(new JobKey(jobName, groupName));
            final CronTrigger trigger = (CronTrigger)getTriggerByJobName(jobName, groupName);
            jobTriggerDetail.setJobName(jobDetailObj.getKey().getName());
            jobTriggerDetail.setJobDescription(jobDetailObj.getDescription());
            jobTriggerDetail.setJobGroup(jobDetailObj.getKey().getGroup());
            jobTriggerDetail.setJobClassName(jobDetailObj.getClass().getName());
            jobTriggerDetail.setTriggerName(trigger.getKey().getName());
            jobTriggerDetail.setTriggerDescription(trigger.getDescription());
            jobTriggerDetail.setTriggerGroup(trigger.getKey().getGroup());
            jobTriggerDetail.setJobType("CRON");
            jobTriggerDetail.setStartTime(trigger.getStartTime());
            jobTriggerDetail.setEndTime(trigger.getEndTime());
            
        } catch (final SchedulerException se) {
        	log.error("Quartz Scheduler Error : Unable to get job by name" , se);
            throw new ATFException(se);
        }
        return jobTriggerDetail;
    }
    
    /**
     * This method will fetch simpleTrigger for the given company and Job.
     * @param companyName
     *            - Name of the Company
     * @param jobName
     *            - Name of the Job
     * @return SimpleTrigger -
     * @throws ATFException
     *             -
     */
    private CronTrigger getTriggerByJobName(final String companyName, final String jobName) throws ATFException {
        try {
            
            final List<? extends Trigger> triggers = scheduler.getTriggersOfJob(new JobKey(jobName, companyName));
            
            if (triggers.size() > 0) {
                return (CronTrigger)triggers.get(0);
            }
        } catch (final SchedulerException e) {
        	log.error("Quartz Scheduler Error : Unable to get trigger for job" , e);
            throw new ATFException(e);
        }
        return null;
    }
    
    /**
     * This Method will modify the given jobTriggerDetail.
     * @param jobTriggerDetail
     *            - JobTriggerDetail to update
     * @return JobTriggerDetail - updated JobTriggerDetail
     * @throws ATFException
     *             -
     */
    public JobTriggerDetail modifyJobTrigger(final JobTriggerDetail jobTriggerDetail) throws ATFException {
        
        updateJob(jobTriggerDetail.getJobGroup(), jobTriggerDetail.getJobName(), jobTriggerDetail.getJobDescription(),
                jobTriggerDetail.getJobClassName(), null);
        
        final JobEntity jobEntity = new JobEntity();
        jobEntity.setJobName(jobTriggerDetail.getJobName());
        jobEntity.setGroupName(jobTriggerDetail.getTriggerGroup());
        jobEntity.setOldTriggername(jobTriggerDetail.getTriggerName());
        jobEntity.setNewTriggerName(jobTriggerDetail.getTriggerName());
        jobEntity.setDescription(jobTriggerDetail.getTriggerDescription());
        jobEntity.setStartTime(jobTriggerDetail.getStartTime());
        jobEntity.setEndTime(jobTriggerDetail.getEndTime());
        jobEntity.setRepeatCount(jobTriggerDetail.getRepeatCount());
        jobEntity.setRepeatInterval(jobTriggerDetail.getRepeatInterval());
        reScheduleSimpleJob(jobEntity);
        
        return jobTriggerDetail;
    }
    
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
    public final Date getTriggerPreviousFireTime(final String companyName, final String triggerName) throws ATFException {
        try {
            // Gets the trigger form quartz table
            final Trigger trigger = scheduler.getTrigger(new TriggerKey(triggerName, companyName));
            return trigger.getPreviousFireTime();
            
        } catch (final SchedulerException e) {
        	log.error("Quartz Scheduler Error : Unable to get previous fire time" , e);
            throw new ATFException(e);
        }
    }
    
    /**
     * This method will fetch the next fire date of the give trigger.
     * @param companyName
     *            - which is group name in scheduler
     * @param triggerName
     *            - trigger name
     * @return Date - next fire date
     * @throws ATFException
     *             - throws ATFException
     */
    public final Date getTriggerNextFireTime(final String companyName, final String triggerName) throws ATFException {
        try {
            // Gets the trigger form quartz table
            final Trigger trigger = scheduler.getTrigger(new TriggerKey(triggerName, companyName));
            return trigger.getNextFireTime();
            
        } catch (final SchedulerException e) {
        	log.error("Quartz Scheduler Error : Unable to get next fire time" , e);
            throw new ATFException(e);
        }
    }
    
    /**
     * This method will fetch the cronTrigger for the Given triggerName and
     * triggerGroup.
     * @param triggerName
     *            - Name of the Trigger
     * @param triggerGroup
     *            - triggerGroup
     * @return String - CronTrigger
     */
    /* (non-Javadoc)
     * @see com.hcl.atf.taf.scheduler.SchedulerService#getCronTrigger(java.lang.String, java.lang.String)
     */
    public final String getCronTrigger(final String triggerName, final String triggerGroup) {
        
        String cronExp = null;
        
        try {
            final CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(new TriggerKey(triggerName, triggerGroup));
            cronExp = cronTrigger.getCronExpression();
            
        } catch (final Exception e) {
        	log.error("Quartz Scheduler Error : Unable to get trigger" , e);
        }
        return cronExp;
    }
    
    /**
     * Return the List of times for the Jobs/triggers to fire in the specified period
     * 
     * @param groupNames
     * @param fromDate
     * @param toDate
     * @return
     * @throws ATFException
     */
    public Map<String, Map<String, List<Date>>> getUpcomingFireTimes(List<String> groupNames, Date fromDate, Date toDate) throws ATFException{
    	
    	Map<String, Map<String, List<Date>>> upcomingJobs = new HashMap<String, Map<String, List<Date>>>();
    	
    	//If no product names have been specified, then find for all the existing products
    	if (groupNames == null || groupNames.isEmpty()) {
    		try {
    			groupNames = scheduler.getTriggerGroupNames();
    		} catch (SchedulerException e) {
            	log.error("Quartz Scheduler Error : Unable to get upcoming jobs" , e);
    			return upcomingJobs;
    		}
    	}
    	

    	for (String groupName : groupNames) {
    		
    		Map<String, List<Date>> upcomingTriggerTimes = new HashMap<String, List<Date>>();
    		try {
    			GroupMatcher gm = GroupMatcher.triggerGroupEquals(groupName);
    			Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(gm);
    			if (triggerKeys == null || triggerKeys.isEmpty())
    				continue;
    			for (TriggerKey triggerKey : triggerKeys) {
    				
    				Trigger trigger = scheduler.getTrigger(triggerKey);
    				List<Date> triggerTimes = TriggerUtils.computeFireTimesBetween((OperableTrigger) trigger, null, fromDate, toDate); 
    				upcomingTriggerTimes.put(triggerKey.getName(), triggerTimes);
    			}
    		} catch (SchedulerException e) {
    			
    			e.printStackTrace();
    		}
    		upcomingJobs.put(groupName, upcomingTriggerTimes);
    	}
    	return upcomingJobs;
    }
    

    /**
     * Return the List of times for the Jobs/triggers to fire in the specified period
     * 
     * @param groupNames
     * @param fromDate
     * @param toDate
     * @return
     * @throws ATFException
     */
    public Map<String, Map<String, List<Date>>> getUpcomingFireTimes(List<String> groupNames, int numberTimes) throws ATFException{
    	
    	Map<String, Map<String, List<Date>>> upcomingJobs = new HashMap<String, Map<String, List<Date>>>();
    	
    	//If no product names have been specified, then find for all the existing products
    	if (groupNames == null || groupNames.isEmpty()) {
    		try {
    			groupNames = scheduler.getTriggerGroupNames();
    		} catch (SchedulerException e) {
            	log.error("Quartz Scheduler Error : Unable to get upcoming jobs" , e);
    			return upcomingJobs;
    		}
    	}
    	

    	for (String groupName : groupNames) {
    		
    		Map<String, List<Date>> upcomingTriggerTimes = new HashMap<String, List<Date>>();
    		try {
    			log.info("Getting fire times for : " + groupName);
    			GroupMatcher gm = GroupMatcher.triggerGroupEquals(groupName);
    			Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(gm);
    			if (triggerKeys == null || triggerKeys.isEmpty())
    				continue;
    			for (TriggerKey triggerKey : triggerKeys) {
    				
    				Trigger trigger = scheduler.getTrigger(triggerKey);
        			log.info("Getting fire times for Trigger key : " + triggerKey);
    				//TODO : Will null work as a calendar input ?
    				List<Date> triggerTimes = TriggerUtils.computeFireTimes((OperableTrigger) trigger, null, numberTimes); 
    				upcomingTriggerTimes.put(triggerKey.getName(), triggerTimes);
    			}
    		} catch (SchedulerException e) {
    			
    			log.error("Error while getting fire times", e);
    		}
    		upcomingJobs.put(groupName, upcomingTriggerTimes);
    	}
    	return upcomingJobs;
    }

    /**
     * @return the driver
     */
    public String getDriver() {
        return driver;
    }
    
    /**
     * @param driverParam
     *            the driver to set
     */
    public void setDriver(final String driverParam) {
        driver = driverParam;
    }
    
    /**
     * @return the dbUrl
     */
    public String getDbUrl() {
        return dbUrl;
    }
    
    /**
     * @param url
     *            the dbUrl to set
     */
    public void setDbUrl(final String url) {
        dbUrl = url;
    }
    
    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * @param userNameParam
     *            the userName to set
     */
    public void setUserName(final String userNameParam) {
        userName = userNameParam;
    }
    
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * @param passwordParam
     *            the password to set
     */
    public void setPassword(final String passwordParam) {
        password = passwordParam;
    }
    
    // TODO List
    /**
     * Validation of the jobclasspath Documentation Commends Logging
     */
    
    
    /**
     * @return the datasource
     */
    public String getDatasource() {
        return datasource;
    }
    
    /**
     * @param ds
     *            the datasource to set
     */
    public void setDatasource(final String ds) {
        datasource = ds;
    }
    
        
	@Override
	public List<Date> getCalculatedFireTimesFromExpression(String cronExpression, int numberTimes) {
		
		List<Date> fireTimes = new ArrayList<Date>();
		Date currTime = new Date();
		try {
			if (CronExpression.isValidExpression(cronExpression)) {
				CronExpression cr = new CronExpression(cronExpression);
		
				for (int i = 0; i < numberTimes; i++) {
					currTime = cr.getNextValidTimeAfter(currTime);
					fireTimes.add(cr.getNextValidTimeAfter(currTime));
				}
			} else {
				log.info("Cron Expression is invalid : " + cronExpression);
			}
		}catch (Exception e) {
			log.error("Error calculating fire times from expression", e);
		}
		return fireTimes;
	}
    
	@Override
	public boolean isValidExpression(String cronExpression) {
		
		if (cronExpression == null || cronExpression.isEmpty()) 
			return false;
		else
			return CronExpression.isValidExpression(cronExpression);
	}
}
