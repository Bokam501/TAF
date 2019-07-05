package com.hcl.atf.taf.scheduler.forfuture;

import java.util.List;

import org.quartz.Job;

/**
 * <br>
 * <br>
 * Copyright (c) 2009, HCL Technologies.<br>
 * Aug 17, 2009 6:02:50 PM<br>
 * @author srajendran
 * @version $Id$
 */
public interface SchedulerGroupService {
    
    /**
     * @return the schedulers
     */
    List<Job> getScheduleJobs();
    
    /**
     * @param schedulers
     *            the schedulers to set
     */
    void setScheduleJobs(List<Job> jobs);
}
