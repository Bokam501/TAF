package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ExecutionPriority;

public class JsonExecutionPriority implements java.io.Serializable {

	private static final Log log = LogFactory
			.getLog(JsonExecutionPriority.class);

	@JsonProperty
	private Integer executionPriorityId;
	@JsonProperty
	private String executionPriorityName;
	@JsonProperty
	private Integer executionPriorityValue;
	@JsonProperty
	private String displayName;
	@JsonProperty	
	private String executionName;
	
	
	
	public JsonExecutionPriority() {
	}

	public JsonExecutionPriority(ExecutionPriority executionPriority) {
		this.executionPriorityId =executionPriority.getExecutionPriorityId();
		this.executionPriorityName=executionPriority.getExecutionPriority();
		this.displayName=executionPriority.getDisplayName();
		this.executionName=executionPriority.getExecutionPriorityName();
		
		if(executionPriorityName.equalsIgnoreCase("P0")){
			executionPriorityValue=5;
		}else if(executionPriorityName.equalsIgnoreCase("P1")){
			executionPriorityValue=4;
		}else  if(executionPriorityName.equalsIgnoreCase("P2")){
			executionPriorityValue=3;
		}else if(executionPriorityName.equalsIgnoreCase("P3")){
			executionPriorityValue=2;
		}else if(executionPriorityName.equalsIgnoreCase("P4")){
			executionPriorityValue=1;
		}
	}


	

	

	public Integer getExecutionPriorityId() {
		return executionPriorityId;
	}

	public void setExecutionPriorityId(Integer executionPriorityId) {
		this.executionPriorityId = executionPriorityId;
	}

	
	public String getExecutionPriorityName() {
		return executionPriorityName;
	}

	public void setExecutionPriorityName(String executionPriorityName) {
		this.executionPriorityName = executionPriorityName;
	}

	public Integer getExecutionPriorityValue() {
		return executionPriorityValue;
	}

	public void setExecutionPriorityValue(Integer executionPriorityValue) {
		this.executionPriorityValue = executionPriorityValue;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@JsonIgnore
	public ExecutionPriority getExecutionPriority() {
		ExecutionPriority executionPriority= new ExecutionPriority();
		executionPriority.setDisplayName(displayName);
		executionPriority.setExecutionPriority(this.executionPriorityName);
		executionPriority.setExecutionPriorityId(executionPriorityId);
		executionPriority.setExecutionPriorityValue(executionPriorityValue);
		executionPriority.setExecutionPriorityName(executionName);
		return executionPriority;
	}

	public String getExecutionName() {
		return executionName;
	}

	public void setExecutionName(String executionName) {
		this.executionName = executionName;
	}



}
