package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "workflow")
public class WorkFlow implements java.io.Serializable{

	private Integer workFlowId;
	private EntityMaster entityMaster;
	private Integer stageId;
	private Integer stageValue;
	private String stageName;
	private String stageDescription;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "workFlowId", unique = true, nullable = false)
	public Integer getWorkFlowId() {
		return workFlowId;
	}
	public void setWorkFlowId(Integer workFlowId) {
		this.workFlowId = workFlowId;
	}
	
	public WorkFlow() {
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entitymasterid") 
	public EntityMaster getEntityMaster() {
		return entityMaster;
	}
	public void setEntityMaster(EntityMaster entityMaster) {
		this.entityMaster = entityMaster;
	}
	@Column(name = "stageId")
	public Integer getStageId() {
		return stageId;
	}
	
	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}
	@Column(name = "stageValue")
	public Integer getStageValue() {
		return stageValue;
	}
	public void setStageValue(Integer stageValue) {
		this.stageValue = stageValue;
	}
	@Column(name = "stageName", length = 1000)
	public String getStageName() {
		return stageName;
	}
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	@Column(name = "stageDescription", length = 1000)
	public String getStageDescription() {
		return stageDescription;
	}
	public void setStageDescription(String stageDescription) {
		this.stageDescription = stageDescription;
	}
	
	
	
}
