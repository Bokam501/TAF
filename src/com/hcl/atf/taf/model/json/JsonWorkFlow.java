package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.WorkFlow;

public class JsonWorkFlow implements java.io.Serializable{

	private static final Log log = LogFactory.getLog(JsonWorkPackage.class);

	@JsonProperty	
	private Integer workFlowId;
	@JsonProperty	
	private Integer stageId;
	@JsonProperty	
	private Integer stageValue;
	@JsonProperty	
	private String stageName;
	@JsonProperty	
	private String stageDescription;
	
	
	public JsonWorkFlow(){
		
	}
	
	public JsonWorkFlow(WorkFlow workFlow){
		this.workFlowId=workFlow.getWorkFlowId();
		
		this.stageId=workFlow.getStageId();
		this.stageName=workFlow.getStageName();
		this.stageValue=workFlow.getStageValue();
		this.stageDescription=workFlow.getStageDescription();
	}
	public Integer getWorkFlowId() {
		return workFlowId;
	}
	public void setWorkFlowId(Integer workFlowId) {
		this.workFlowId = workFlowId;
	}
	
	public Integer getStageId() {
		return stageId;
	}
	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}
	public Integer getStageValue() {
		return stageValue;
	}
	public void setStageValue(Integer stageValue) {
		this.stageValue = stageValue;
	}
	public String getStageName() {
		return stageName;
	}
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	public String getStageDescription() {
		return stageDescription;
	}
	public void setStageDescription(String stageDescription) {
		this.stageDescription = stageDescription;
	}
	
}
