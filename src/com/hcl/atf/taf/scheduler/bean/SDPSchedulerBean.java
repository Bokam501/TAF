package com.hcl.atf.taf.scheduler.bean;

/**
 * Domain model that represents a job to scedule. <br>
 * <br>
 * Copyright (c) 2008, HCL Technologies.<br>
 * Sep 15, 2008 7:47:17 PM<br>
 * @author rejithes
 * @version $Id$
 */
public class SDPSchedulerBean { //extends SDPBean {
    /**
     * version id.
     */
    private static final long serialVersionUID = 1L;
    /** Company Name. * */
    private String companyName;
    /** Job Name. * */
    private String jobName;
    /** Job Description. * */
    private String description;
    /** Job class Name. * */
    private String jobClassName;
    /** Trigger Name. * */
    private String triggerName;
    /** Trigger Description. * */
    private String triggerDescription;
    /** Cron Expression. * */
    private String cronExpression;
    
    /**
     * Default constructor.
     */
    public SDPSchedulerBean() {
    }
    
    /**
     * Get the company name.
     * @return String - company name
     */
    public String getCompanyName() {
        return companyName;
    }
    
    /**
     * Set the company name.
     * @param compName
     *            - company name
     */
    public void setCompanyName(final String compName) {
        companyName = compName;
    }
    
    /**
     * Get the job name.
     * @return String - company name
     */
    public String getJobName() {
        return jobName;
    }
    
    /**
     * Set the job name.
     * @param job
     *            - job name
     */
    public void setJobName(final String job) {
        jobName = job;
    }
    
    /**
     * Get the description.
     * @return String - description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Set the description.
     * @param desc
     *            - job description
     */
    public void setDescription(final String desc) {
        description = desc;
    }
    
    /**
     * Get the job class name.
     * @return String - job class name
     */
    public String getJobClassName() {
        return jobClassName;
    }
    
    /**
     * Set the job class name.
     * @param jobClass
     *            - job class name
     */
    public void setJobClassName(final String jobClass) {
        jobClassName = jobClass;
    }
    
    /**
     * Get the trigger name.
     * @return String - trigger name
     */
    public String getTriggerName() {
        return triggerName;
    }
    
    /**
     * Set the trigger name.
     * @param trigger
     *            - trigger name
     */
    public void setTriggerName(final String trigger) {
        triggerName = trigger;
    }
    
    /**
     * Get the trigger description.
     * @return String - trigger description
     */
    public String getTriggerDescription() {
        return triggerDescription;
    }
    
    /**
     * Set the trigger Description.
     * @param triggerDesc
     *            - description of trigger
     */
    public void setTriggerDescription(final String triggerDesc) {
        triggerDescription = triggerDesc;
    }
    
    /**
     * Get the cron expression.
     * @return String - expression
     */
    public String getCronExpression() {
        return cronExpression;
    }
    
    /**
     * Set the cron expression.
     * @param expression
     *            - expression string
     */
    public void setCronExpression(final String expression) {
        cronExpression = expression;
    }
}
