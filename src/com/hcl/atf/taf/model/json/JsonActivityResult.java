package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ActivityResult;

public class JsonActivityResult {	
	@JsonProperty
	private Integer activityResultId;
	@JsonProperty
	private String activityResultName;
	@JsonProperty
	private String activityResultDescription;
	
	public JsonActivityResult() {
	}

	public JsonActivityResult(ActivityResult activityResult) {
		this.activityResultId = activityResult.getActivityResultId();
		this.activityResultName = activityResult.getActivityResultName();
		this.activityResultDescription = activityResult.getActivityResultDescription();
		
		
	}
	
	public Integer getActivityResultId() {
		return activityResultId;
	}
	public void setActivityResultId(Integer activityResultId) {
		this.activityResultId = activityResultId;
	}
	public String getActivityResultName() {
		return activityResultName;
	}
	public void setActivityResultName(String activityResultName) {
		this.activityResultName = activityResultName;
	}
	public String getActivityResultDescription() {
		return activityResultDescription;
	}
	public void setActivityResultDescription(String activityResultDescription) {
		this.activityResultDescription = activityResultDescription;
	}
	
}
