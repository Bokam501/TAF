/**
 * 
 */
package com.hcl.ilcm.workflow.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.ilcm.workflow.model.WorkflowMaster;

/**
 * @author silambarasur
 *
 */
public class JsonWorkflowMaster {
	

	@JsonProperty
	private Integer workflowId;
	@JsonProperty
	private String workflowName;
	@JsonProperty
	private String workflowStatusRulesFileId;
	@JsonProperty
	private String workflowStatusRulesFileName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer activeState;
	@JsonProperty
	private Integer readyState;	
	@JsonProperty
	private String workflowType;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;	
	@JsonProperty
	private String oldFieldID;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty
	private String	modifiedFieldID;
	@JsonProperty	
	private String modifiedFieldValue;
	@JsonProperty
	private Integer attachmentCount;
	
	public JsonWorkflowMaster(){
		
	}
	
	public JsonWorkflowMaster(WorkflowMaster workflowMaster) {
		this.activeState = workflowMaster.getActiveState();
		this.description = workflowMaster.getDescription();
		this.workflowId = workflowMaster.getWorkflowId();
		this.workflowName = workflowMaster.getWorkflowName();
		this.workflowStatusRulesFileId = workflowMaster.getWorkflowStatusRulesFileId();
		this.workflowStatusRulesFileName = workflowMaster.getWorkflowStatusRulesFileName();
		this.readyState = workflowMaster.getReadyState();
		this.workflowType = workflowMaster.getWorkflowType();
	}
	public Integer getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}
	public String getWorkflowStatusRulesFileId() {
		return workflowStatusRulesFileId;
	}
	public void setWorkflowStatusRulesFileId(String workflowStatusRulesFileId) {
		this.workflowStatusRulesFileId = workflowStatusRulesFileId;
	}
	public String getWorkflowStatusRulesFileName() {
		return workflowStatusRulesFileName;
	}
	public void setWorkflowStatusRulesFileName(String workflowStatusRulesFileName) {
		this.workflowStatusRulesFileName = workflowStatusRulesFileName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getActiveState() {
		return activeState;
	}
	public void setActiveState(Integer activeState) {
		this.activeState = activeState;
	}
	
	
	
	@JsonIgnore
	public WorkflowMaster getWorkflowMaster() {
		WorkflowMaster workflowMaster = new WorkflowMaster();
		workflowMaster.setActiveState(activeState);
		workflowMaster.setDescription(description);
		workflowMaster.setWorkflowId(workflowId);
		workflowMaster.setWorkflowName(workflowName);
		workflowMaster.setWorkflowStatusRulesFileId(workflowStatusRulesFileId);
		workflowMaster.setWorkflowStatusRulesFileName(workflowStatusRulesFileName);
		if(readyState != null){
			workflowMaster.setReadyState(readyState);
		}else{
			workflowMaster.setReadyState(0);
		}
		workflowMaster.setWorkflowType(workflowType);
		return workflowMaster;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public Integer getReadyState() {
		return readyState;
	}

	public void setReadyState(Integer readyState) {
		this.readyState = readyState;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public String getModifiedField() {
		return modifiedField;
	}

	public void setModifiedField(String modifiedField) {
		this.modifiedField = modifiedField;
	}

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}

	public String getOldFieldID() {
		return oldFieldID;
	}

	public void setOldFieldID(String oldFieldID) {
		this.oldFieldID = oldFieldID;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldID() {
		return modifiedFieldID;
	}

	public void setModifiedFieldID(String modifiedFieldID) {
		this.modifiedFieldID = modifiedFieldID;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}

	public Integer getAttachmentCount() {
		return attachmentCount;
	}

	public void setAttachmentCount(Integer attachmentCount) {
		this.attachmentCount = attachmentCount;
	}
	
	

}
