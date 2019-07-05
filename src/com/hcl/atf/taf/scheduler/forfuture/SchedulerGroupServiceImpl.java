package com.hcl.atf.taf.scheduler.forfuture;


import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;


/**
 * <br>
 * <br>
 * Copyright (c) 2009, HCL Technologies.<br>
 * Aug 18, 2009 2:54:28 PM<br>
 * @author srajendran
 * @version $Id$
 */
public class SchedulerGroupServiceImpl implements SchedulerGroupService {
    
    /**
     * List of schedulers.
     */
    private List<Job> jobs = new ArrayList<Job>();
    
    /**
     * @return the schedulers
     */
    public List<Job> getScheduleJobs() {
        return jobs;
    }
    
    /**
     * @param schedulersParam
     *            the schedulers to set
     */
    public void setScheduleJobs(final List<Job> jobsList) {
        jobs = jobsList;
    }
}
