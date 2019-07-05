package com.hcl.atf.taf.model.dto;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;

public class ActivityTasksDTO {
	
	private ActivityTask activityTask;
	private Activity activity;
	private ActivityWorkPackage activityWorkPackage;
	public ActivityTask getActivityTask() {
		return activityTask;
	}
	public void setActivityTask(ActivityTask activityTask) {
		this.activityTask = activityTask;
	}
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public ActivityWorkPackage getActivityWorkPackage() {
		return activityWorkPackage;
	}
	public void setActivityWorkPackage(ActivityWorkPackage activityWorkPackage) {
		this.activityWorkPackage = activityWorkPackage;
	}
	

}
