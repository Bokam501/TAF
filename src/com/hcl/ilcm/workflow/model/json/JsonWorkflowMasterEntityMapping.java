package com.hcl.ilcm.workflow.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowMasterEntityMapping;


public class JsonWorkflowMasterEntityMapping {
	
	@JsonProperty
	private Integer workflowEntityMappingId;
	@JsonProperty
	private Integer workflowId;
	@JsonProperty
	private String workflowName;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer entityTypeId;
	@JsonProperty
	private String entityTypeName;
	@JsonProperty
	private Integer entityId;
	@JsonProperty
	private Integer entityInstanceId;
	@JsonProperty
	private Integer isDefault;
	@JsonProperty
	private String mappingLevel;
	
	public JsonWorkflowMasterEntityMapping() {
	}

	public JsonWorkflowMasterEntityMapping(WorkflowMasterEntityMapping workflowMasterEntityMapping) {
		this.workflowEntityMappingId = workflowMasterEntityMapping.getWorkflowEntityMappingId();
		if(workflowMasterEntityMapping.getWorkflow() != null){
			this.workflowId = workflowMasterEntityMapping.getWorkflow().getWorkflowId();
			this.workflowName = workflowMasterEntityMapping.getWorkflow().getWorkflowName();
		}
		if(workflowMasterEntityMapping.getProduct() != null){
			this.productId = workflowMasterEntityMapping.getProduct().getProductId();
			this.productName = workflowMasterEntityMapping.getProduct().getProductName();
		}
		if(workflowMasterEntityMapping.getEntityType() != null){
			this.entityTypeId = workflowMasterEntityMapping.getEntityType().getEntitymasterid();
			this.entityTypeName = workflowMasterEntityMapping.getEntityType().getEntitymastername();
		}
		this.entityId = workflowMasterEntityMapping.getEntityId();
		this.entityInstanceId = workflowMasterEntityMapping.getEntityInstanceId();
		this.isDefault = workflowMasterEntityMapping.getIsDefault();
		this.mappingLevel = workflowMasterEntityMapping.getMappingLevel();
	}

	public Integer getWorkflowEntityMappingId() {
		return workflowEntityMappingId;
	}

	public void setWorkflowEntityMappingId(Integer workflowEntityMappingId) {
		this.workflowEntityMappingId = workflowEntityMappingId;
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getEntityTypeId() {
		return entityTypeId;
	}

	public void setEntityTypeId(Integer entityTypeId) {
		this.entityTypeId = entityTypeId;
	}

	public String getEntityTypeName() {
		return entityTypeName;
	}

	public void setEntityTypeName(String entityTypeName) {
		this.entityTypeName = entityTypeName;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public Integer getEntityInstanceId() {
		return entityInstanceId;
	}

	public void setEntityInstanceId(Integer entityInstanceId) {
		this.entityInstanceId = entityInstanceId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public String getMappingLevel() {
		return mappingLevel;
	}

	public void setMappingLevel(String mappingLevel) {
		this.mappingLevel = mappingLevel;
	}

	@JsonIgnore
	public WorkflowMasterEntityMapping getWorkflowMasterEntityMapping() {
		WorkflowMasterEntityMapping workflowMasterEntityMapping = new WorkflowMasterEntityMapping();
		workflowMasterEntityMapping.setWorkflowEntityMappingId(this.workflowEntityMappingId);
		if(this.workflowId != null){
			WorkflowMaster workflowMaster = new WorkflowMaster();
			workflowMaster.setWorkflowId(this.workflowId);
			workflowMasterEntityMapping.setWorkflow(workflowMaster);
		}
		if(this.productId != null){
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(this.productId);
			workflowMasterEntityMapping.setProduct(productMaster);
		}
		if(this.entityTypeId != null){
			EntityMaster entityMaster = new EntityMaster();
			entityMaster.setEntitymasterid(this.entityTypeId);
			workflowMasterEntityMapping.setEntityType(entityMaster);
		}
		if(this.entityId != null && this.entityId > 0){
			workflowMasterEntityMapping.setEntityId(this.entityId);
		}
		if(this.entityInstanceId != null && this.entityInstanceId > 0){
			workflowMasterEntityMapping.setEntityInstanceId(this.entityInstanceId);
		}
		if(this.isDefault != null){
			workflowMasterEntityMapping.setIsDefault(this.isDefault);
		}else{
			workflowMasterEntityMapping.setIsDefault(0);
		}
		if(this.mappingLevel != null){
			workflowMasterEntityMapping.setMappingLevel(this.mappingLevel);
		}else{
			workflowMasterEntityMapping.setMappingLevel("Entity");
		}

		return workflowMasterEntityMapping;
	}
	
	
	
}
