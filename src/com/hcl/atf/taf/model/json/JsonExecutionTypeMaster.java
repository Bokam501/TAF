package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ExecutionTypeMaster;

public class JsonExecutionTypeMaster implements java.io.Serializable {

	private static final Log log = LogFactory
			.getLog(JsonExecutionTypeMaster.class);

	@JsonProperty
	private Integer executionTypeId;	
	@JsonProperty
	private String name;
	@JsonProperty
	private String description ;
	@JsonProperty
	private Integer entitymasterid;	

	public JsonExecutionTypeMaster() {
		
	}

	public JsonExecutionTypeMaster(ExecutionTypeMaster executionTypeMaster) {
		this.executionTypeId = executionTypeMaster.getExecutionTypeId();
		this.name = executionTypeMaster.getName();
		this.description = executionTypeMaster.getDescription();
		if(executionTypeMaster.getEntitymaster() != null){
			this.entitymasterid = executionTypeMaster.getEntitymaster().getEntitymasterid();
		}		
	}	

	public Integer getExecutionTypeId() {
		return executionTypeId;
	}

	public void setExecutionTypeId(Integer executionTypeId) {
		this.executionTypeId = executionTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

	public Integer getEntitymasterid() {
		return entitymasterid;
	}

	public void setEntitymasterid(Integer entitymasterid) {
		this.entitymasterid = entitymasterid;
	}

	@JsonIgnore
	public ExecutionTypeMaster getExecutionTypeMaster() {
		ExecutionTypeMaster executionTypeMaster = new ExecutionTypeMaster();
		executionTypeMaster.setExecutionTypeId(executionTypeId);
		executionTypeMaster.setName(name);
		executionTypeMaster.setDescription(description);
		
		if(entitymasterid != null){
			EntityMaster em = new EntityMaster();
			em.setEntitymasterid(entitymasterid);
			executionTypeMaster.setEntitymaster(em);
		}		
		return executionTypeMaster;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}	

}
