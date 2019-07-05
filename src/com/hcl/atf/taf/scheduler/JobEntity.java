package com.hcl.atf.taf.scheduler;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <br>
 * <br>
 * Copyright (c) 2008, HCL Technologies.<br>
 * Jul 2, 2008 3:03:28 PM<br>
 * @author rajeshbn
 * @version $Id$
 */
public class JobEntity implements Serializable {
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Name of the Company.
     */
    private String groupName;
    /**
     * Name of the Job.
     */
    private String jobName;
    /**
     * Name of the Trigger.
     */
    private String triggerName;
    /**
     * description of the Trigger.
     */
    private String triggerDescription;
    /**
     * startTime of the Job.
     */
    private Date startTime;
    /**
     * endTime of the Job.
     */
    private Date endTime;
    /**
     * job repeatCount.
     */
    private int repeatCount;
    /**
     * repeatInterval of the job.
     */
    private long repeatInterval;
    /**
     * Job description.
     */
    private String description;
    /**
     * triggerOldName.
     */
    private String oldTriggername;
    /**
     * triggerNewName.
     */
    private String newTriggerName;
    
    /**
     * Map.
     */
    private Map<String, Object> dataMap = new HashMap<String, Object>();
    
    /**
     * The schedule for the trigger. This is in Cron expression format. Ref Quartz documentation
     * 
     * Cron-Expressions are strings that are actually made up of seven sub-expressions, separated with white-space
	 * Seconds
	 * Minutes
	 * Hours
	 * Day-of-Month
	 * Month
	 * Day-of-Week
	 * Year (optional field)
     */
    private String scheduleCronExpression;
	/**
	 * @return the scheduleCronExpression
	 */
	public String getScheduleCronExpression() {
		return scheduleCronExpression;
	}

	/**
	 * @param scheduleCronExpression the scheduleCronExpression to set
	 */
	public void setScheduleCronExpression(String scheduleCronExpression) {
		this.scheduleCronExpression = scheduleCronExpression;
	}

	/**
	 * @return the jobClassName
	 */
	public String getJobClassName() {
		return jobClassName;
	}

	/**
	 * @param jobClassName the jobClassName to set
	 */
	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	/**
	 * JobClass name
	 */
    private String jobClassName;

    
    /**
     * @return the companyName
     */
    public String getGroupName() {
        return groupName;
    }
    
    /**
     * @param aCompanyName
     *            the companyName to set
     */
    public void setGroupName(final String groupName) {
    	this.groupName = groupName;
    }
    
    /**
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
    }
    
    /**
     * @param aJobName
     *            the jobName to set
     */
    public void setJobName(final String aJobName) {
        jobName = aJobName;
    }
    
    /**
     * @return the triggerName
     */
    public String getTriggerName() {
        return triggerName;
    }
    
    /**
     * @param aTriggerName
     *            the triggerName to set
     */
    public void setTriggerName(final String aTriggerName) {
        triggerName = aTriggerName;
    }
    
    /**
     * @return the triggerDescription
     */
    public String getTriggerDescription() {
        return triggerDescription;
    }
    
    /**
     * @param aTriggerDescription
     *            the triggerDescription to set
     */
    public void setTriggerDescription(final String aTriggerDescription) {
        triggerDescription = aTriggerDescription;
    }
    
    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }
    
    /**
     * @param aStartTime
     *            the startTime to set
     */
    public void setStartTime(final Date aStartTime) {
        startTime = aStartTime;
    }
    
    /**
     * @return the endTime
     */
    public Date getEndTime() {
        return endTime;
    }
    
    /**
     * @param aEndTime
     *            the endTime to set
     */
    public void setEndTime(final Date aEndTime) {
        endTime = aEndTime;
    }
    
    /**
     * @return the repeatCount
     */
    public int getRepeatCount() {
        return repeatCount;
    }
    
    /**
     * @param aRepeatCount
     *            the repeatCount to set
     */
    public void setRepeatCount(final int aRepeatCount) {
        repeatCount = aRepeatCount;
    }
    
    /**
     * @return the repeatInterval
     */
    public long getRepeatInterval() {
        return repeatInterval;
    }
    
    /**
     * @param aRepeatInterval
     *            the repeatInterval to set
     */
    public void setRepeatInterval(final long aRepeatInterval) {
        repeatInterval = aRepeatInterval;
    }
    
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @param aDescription
     *            the description to set
     */
    public void setDescription(final String aDescription) {
        description = aDescription;
    }
    
    /**
     * @return the oldTriggername
     */
    public String getOldTriggername() {
        return oldTriggername;
    }
    
    /**
     * @param aAldTriggername
     *            the oldTriggername to set
     */
    public void setOldTriggername(final String aAldTriggername) {
        oldTriggername = aAldTriggername;
    }
    
    /**
     * @return the newTriggerName
     */
    public String getNewTriggerName() {
        return newTriggerName;
    }
    
    /**
     * @param aNewTriggerName
     *            the newTriggerName to set
     */
    public void setNewTriggerName(final String aNewTriggerName) {
        newTriggerName = aNewTriggerName;
    }
    
    /**
     * @return the dataMap
     */
    public Map<String, Object> getDataMap() {
        return dataMap;
    }
    
    /**
     * @param paramDataMap
     *            the dataMap to set
     */
    public void setDataMap(final Map<String, Object> paramDataMap) {
        dataMap = paramDataMap;
    }
    
}
