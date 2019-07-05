package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "execution_priority")
public class ExecutionPriority implements java.io.Serializable{

	private Integer executionPriorityId;
	private String executionPriority;
	private Integer executionPriorityValue;
	private String displayName;
	private String  executionPriorityName;
	
	
	public ExecutionPriority(){
		
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "executionPriorityId", unique = true, nullable = false)	
	public Integer getExecutionPriorityId() {
		return executionPriorityId;
	}
	
	public void setExecutionPriorityId(Integer executionPriorityId) {
		this.executionPriorityId = executionPriorityId;
	}


	@Column(name = "executionPriority", length = 100)
	public String getExecutionPriority() {
		return executionPriority;
	}



	public void setExecutionPriority(String executionPriority) {
		this.executionPriority = executionPriority;
	}


	@Column(name = "executionPriorityValue")
	public Integer getExecutionPriorityValue() {
		return executionPriorityValue;
	}



	public void setExecutionPriorityValue(Integer executionPriorityValue) {
		this.executionPriorityValue = executionPriorityValue;
	}

	@Column(name = "displayName", length = 100)
	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	@Column(name = "executionPriorityName", length = 100)
	public String getExecutionPriorityName() {
		return executionPriorityName;
	}
	public void setExecutionPriorityName(String executionPriorityName) {
		this.executionPriorityName = executionPriorityName;
	}
	
}
