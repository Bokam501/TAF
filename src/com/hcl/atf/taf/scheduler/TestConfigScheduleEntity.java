package com.hcl.atf.taf.scheduler;

import java.io.Serializable;
import java.util.Date;

public class TestConfigScheduleEntity implements Serializable {
	
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;
	
    /**
     * 
     */
    private String productId;
    /**
     * The will be as the Group for the Jobs and Triggers
     * All the jobs, triggers for a product will belong to a Group named after the Product
     */
    private String productName;
    /**
     * 
     */
    private String productVersionId;
    /**
     * 
     */
    private String productVersionName;
    /**
     * 
     */
    private String testConfigId;
    /**
	 *
     */
    private String testConfigName;
	/**
     * 
     */
    private String testConfigDescription;
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
	 * JobClass name
	 */
    private String jobClassName;

	/**
	 * JobClass name
	 */
    private Date startDate;
	/**
	 * JobClass name
	 */
    private Date endDate;
    
   
    
    /**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the productVersionId
	 */
	public String getProductVersionId() {
		return productVersionId;
	}

	/**
	 * @param productVersionId the productVersionId to set
	 */
	public void setProductVersionId(String productVersionId) {
		this.productVersionId = productVersionId;
	}

	/**
	 * @return the productVersionName
	 */
	public String getProductVersionName() {
		return productVersionName;
	}

	/**
	 * @param productVersionName the productVersionName to set
	 */
	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}

	/**
	 * @return the testCycleConfigId
	 */
	public String getTestConfigId() {
		return testConfigId;
	}

	/**
	 * @param testCycleConfigId the testCycleConfigId to set
	 */
	public void setTestConfigId(String testConfigId) {
		this.testConfigId = testConfigId;
	}

	/**
	 * @return the testCycleConfigName
	 */
	public String getTestConfigName() {
		return testConfigName;
	}

	/**
	 * @param testCycleConfigName the testCycleConfigName to set
	 */
	public void setTestConfigName(String testConfigName) {
		this.testConfigName = testConfigName;
	}

	/**
	 * @return the testCycleConfigDescription
	 */
	public String getTestConfigDescription() {
		return testConfigDescription;
	}

	/**
	 * @param testCycleConfigDescription the testCycleConfigDescription to set
	 */
	public void setTestConfigDescription(String testConfigDescription) {
		this.testConfigDescription = testConfigDescription;
	}
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	

}
