package com.hcl.ilcm.workflow.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ProductMaster;

@Entity
@Table(name = "wf_workflows_entity_mapping")
public class WorkflowMasterEntityMapping implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer workflowEntityMappingId;

	private WorkflowMaster workflow;
	
	private ProductMaster product;
	
	private EntityMaster entityType;
	
	private Integer entityId;
	
	private Integer entityInstanceId;
	
	private Integer isDefault;
	
	private String mappingLevel;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getWorkflowEntityMappingId() {
		return workflowEntityMappingId;
	}

	public void setWorkflowEntityMappingId(Integer workflowEntityMappingId) {
		this.workflowEntityMappingId = workflowEntityMappingId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflowId")
	public WorkflowMaster getWorkflow() {
		return workflow;
	}

	public void setWorkflow(WorkflowMaster workflow) {
		this.workflow = workflow;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProduct() {
		return product;
	}

	public void setProduct(ProductMaster product) {
		this.product = product;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityTypeId")
	public EntityMaster getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityMaster entityType) {
		this.entityType = entityType;
	}

	@Column(name = "entityId")
	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	@Column(name = "entityInstanceId")
	public Integer getEntityInstanceId() {
		return entityInstanceId;
	}

	public void setEntityInstanceId(Integer entityInstanceId) {
		this.entityInstanceId = entityInstanceId;
	}

	@Column(name = "isDefault")
	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	@Column(name = "mappingLevel")
	public String getMappingLevel() {
		return mappingLevel;
	}

	public void setMappingLevel(String mappingLevel) {
		this.mappingLevel = mappingLevel;
	}

	
}
