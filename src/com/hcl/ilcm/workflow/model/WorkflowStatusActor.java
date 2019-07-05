/**
 * 
 */
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
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;

/**
 * @author silambarasur
 *
 */
@Entity
@Table(name="wf_entity_instance_status_actors_mapping")
public class WorkflowStatusActor implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer workflowStatusActorId;
	private WorkflowStatus workflowStatus;
	private ProductMaster product;
	private EntityMaster entityType;
	private Integer entityId;
	private Integer entityInstanceId;
	private UserRoleMaster role;
	private UserList user;
	private String roleName;
	private String actorMappingType;
	private String actionRequirement;
	private String userActionStatus;
	private String mappingLevel;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id",unique = true, nullable = false)
	public Integer getWorkflowStatusActorId() {
		return workflowStatusActorId;
	}
	public void setWorkflowStatusActorId(Integer workflowStatusActorId) {
		this.workflowStatusActorId = workflowStatusActorId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflowStatusId")
	public WorkflowStatus getWorkflowStatus() {
		return workflowStatus;
	}
	public void setWorkflowStatus(WorkflowStatus workflowStatus) {
		this.workflowStatus = workflowStatus;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleId")
	public UserRoleMaster getRole() {
		return role;
	}
	public void setRole(UserRoleMaster role) {
		this.role = role;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public UserList getUser() {
		return user;
	}
	public void setUser(UserList user) {
		this.user = user;
	}
	
	@Column(name = "actionRequirement")
	public String getActionRequirement() {
		return actionRequirement;
	}
	public void setActionRequirement(String actionRequirement) {
		this.actionRequirement = actionRequirement;
	}
	
	@Column(name = "userActionStatus")
	public String getUserActionStatus() {
		return userActionStatus;
	}
	public void setUserActionStatus(String userActionStatus) {
		this.userActionStatus = userActionStatus;
	}
	
	@Column(name = "roleName")
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@Column(name = "actorMappingType")
	public String getActorMappingType() {
		return actorMappingType;
	}
	public void setActorMappingType(String actorMappingType) {
		this.actorMappingType = actorMappingType;
	}
	
	@Column(name = "mappingLevel")
	public Object clone() throws CloneNotSupportedException{  
		return super.clone();  
	}
	public String getMappingLevel() {
		return mappingLevel;
	}
	public void setMappingLevel(String mappingLevel) {
		this.mappingLevel = mappingLevel;
	}
	
	
}
