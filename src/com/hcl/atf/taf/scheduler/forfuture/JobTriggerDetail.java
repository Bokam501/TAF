/**
 *
 */
package com.hcl.atf.taf.scheduler.forfuture;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: karthivt Company: HCL Technologies. Date: Dec 17, 2007 2:47:12 PM
 * Description: ADD YOUR CLASS DESCRIPTION HERE
 */
public class JobTriggerDetail implements Serializable {
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;
    
    /** String for jobName. */
    private String jobName;
    
    /** String for jobDescription. */
    private String jobDescription;
    
    /** String for jobClassName. */
    private String jobClassName;
    
    /** String for jobGroup. */
    private String jobGroup;
    
    /** String for triggerName. */
    private String triggerName;
    
    /** String for triggerDescription. */
    private String triggerDescription;
    
    /** String for triggerGroup. */
    private String triggerGroup;
    
    /** startTime date. */
    private Date startTime;
    
    /** endTime date. */
    private Date endTime;
    
    /** repeatCount . */
    private int repeatCount;
    
    /** repeatInterval . */
    private long repeatInterval;
    
    /** String for seconds. */
    private String seconds;
    
    /** String for minutes. */
    private String minutes;
    
    /** String for hours. */
    private String hours;
    
    /** String for dayofMonth. */
    private String dayofMonth;
    
    /** String for month. */
    private String month;
    
    /** String for dayofWeek. */
    private String dayofWeek;
    
    /** String for year. */
    private String year;
    
    /** String for jobType. */
    private String jobType;
    
    /**
     * Empty constructor of the class.
     */
    public JobTriggerDetail() {
        
    }
    
    /**
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
    }
    
    /**
     * @param jobNameVal
     *            the jobName to set
     */
    public void setJobName(final String jobNameVal) {
        jobName = jobNameVal;
    }
    
    /**
     * @return the jobDescription
     */
    public String getJobDescription() {
        return jobDescription;
    }
    
    /**
     * @param jobDescriptionVal
     *            the jobDescription to set
     */
    public void setJobDescription(final String jobDescriptionVal) {
        jobDescription = jobDescriptionVal;
    }
    
    /**
     * @return the jobClassName
     */
    public String getJobClassName() {
        return jobClassName;
    }
    
    /**
     * @param jobClassNameVal
     *            the jobClassName to set
     */
    public void setJobClassName(final String jobClassNameVal) {
        jobClassName = jobClassNameVal;
    }
    
    /**
     * @return the triggerName
     */
    public String getTriggerName() {
        return triggerName;
    }
    
    /**
     * @param triggerNameVal
     *            the triggerName to set
     */
    public void setTriggerName(final String triggerNameVal) {
        triggerName = triggerNameVal;
    }
    
    /**
     * @return the triggerDescription
     */
    public String getTriggerDescription() {
        return triggerDescription;
    }
    
    /**
     * @param triggerDescriptionVal
     *            the triggerDescription to set
     */
    public void setTriggerDescription(final String triggerDescriptionVal) {
        triggerDescription = triggerDescriptionVal;
    }
    
    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }
    
    /**
     * @param startTimeVal
     *            the startTime to set
     */
    public void setStartTime(final Date startTimeVal) {
        startTime = startTimeVal;
    }
    
    /**
     * @return the endTime
     */
    public Date getEndTime() {
        return endTime;
    }
    
    /**
     * @param endTimeVal
     *            the endTime to set
     */
    public void setEndTime(final Date endTimeVal) {
        endTime = endTimeVal;
    }
    
    /**
     * @return the repeatCount
     */
    public int getRepeatCount() {
        return repeatCount;
    }
    
    /**
     * @param repeatCountVal
     *            the repeatCount to set
     */
    public void setRepeatCount(final int repeatCountVal) {
        repeatCount = repeatCountVal;
    }
    
    /**
     * @return the repeatInterval
     */
    public long getRepeatInterval() {
        return repeatInterval;
    }
    
    /**
     * @param paramRepeatInterval
     *            the repeatInterval to set
     */
    public void setRepeatInterval(final long paramRepeatInterval) {
        repeatInterval = paramRepeatInterval;
    }
    
    /**
     * @return the jobGroup
     */
    public String getJobGroup() {
        return jobGroup;
    }
    
    /**
     * @param jobGroupVal
     *            the jobGroup to set
     */
    public void setJobGroup(final String jobGroupVal) {
        jobGroup = jobGroupVal;
    }
    
    /**
     * @return the triggerGroup
     */
    public String getTriggerGroup() {
        return triggerGroup;
    }
    
    /**
     * @param triggerGroupVal
     *            the triggerGroup to set
     */
    public void setTriggerGroup(final String triggerGroupVal) {
        triggerGroup = triggerGroupVal;
    }
    
    /**
     * @return the jobType
     */
    public String getJobType() {
        return jobType;
    }
    
    /**
     * @param jobTypeVal
     *            the jobType to set
     */
    public void setJobType(final String jobTypeVal) {
        jobType = jobTypeVal;
    }
    
    /**
     * @return the seconds
     */
    public String getSeconds() {
        return seconds;
    }
    
    /**
     * @param paramSeconds
     *            the seconds to set
     */
    public void setSeconds(final String paramSeconds) {
        seconds = paramSeconds;
    }
    
    /**
     * @return the minutes
     */
    public String getMinutes() {
        return minutes;
    }
    
    /**
     * @param paramMinutes
     *            the minutes to set
     */
    public void setMinutes(final String paramMinutes) {
        minutes = paramMinutes;
    }
    
    /**
     * @return the hours
     */
    public String getHours() {
        return hours;
    }
    
    /**
     * @param paramHours
     *            the hours to set
     */
    public void setHours(final String paramHours) {
        hours = paramHours;
    }
    
    /**
     * @return the dayofMonth
     */
    public String getDayofMonth() {
        return dayofMonth;
    }
    
    /**
     * @param paramDayofMonth
     *            the dayofMonth to set
     */
    public void setDayofMonth(final String paramDayofMonth) {
        dayofMonth = paramDayofMonth;
    }
    
    /**
     * @return the month
     */
    public String getMonth() {
        return month;
    }
    
    /**
     * @param paramMonth
     *            the month to set
     */
    public void setMonth(final String paramMonth) {
        month = paramMonth;
    }
    
    /**
     * @return the dayofWeek
     */
    public String getDayofWeek() {
        return dayofWeek;
    }
    
    /**
     * @param dayofWeekVal
     *            the dayofWeek to set
     */
    public void setDayofWeek(final String dayofWeekVal) {
        dayofWeek = dayofWeekVal;
    }
    
    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }
    
    /**
     * @param paramYear
     *            the year to set
     */
    public void setYear(final String paramYear) {
        year = paramYear;
    }
}
