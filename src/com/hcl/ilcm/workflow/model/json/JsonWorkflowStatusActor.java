/**
 * 
 */
package com.hcl.ilcm.workflow.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.WorkflowStatusActor;

/**
 * @author silambarasur
 *
 */
public class JsonWorkflowStatusActor {
	
	@JsonProperty
	private Integer workflowStatusActorId;
	@JsonProperty
	private Integer workflowStatusId;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer entityTypeId;
	@JsonProperty
	private Integer entityId;
	@JsonProperty
	private Integer entityInstanceId;
	@JsonProperty
	private Integer roleId;
	@JsonProperty
	private String roleName;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String userName;
	@JsonProperty
	private String actorMappingType;
	@JsonProperty
	private String actionRequirement;
	@JsonProperty
	private String userActionStatus;
	
	public JsonWorkflowStatusActor(){
		
	}
	
	public JsonWorkflowStatusActor(WorkflowStatusActor workflowStatusActor){
		this.workflowStatusActorId = workflowStatusActor.getWorkflowStatusActorId();
		if(workflowStatusActor.getEntityType() != null){
			this.entityTypeId = workflowStatusActor.getEntityType().getEntitymasterid();
		}
		this.entityId = workflowStatusActor.getEntityId();
		this.entityInstanceId = workflowStatusActor.getEntityInstanceId();
		if(workflowStatusActor.getWorkflowStatus() != null){
			this.workflowStatusId = workflowStatusActor.getWorkflowStatus().getWorkflowStatusId();
		}
		if(workflowStatusActor.getRole() != null){
			this.roleId = workflowStatusActor.getRole().getUserRoleId();
			this.roleName = workflowStatusActor.getRole().getRoleLabel();
		}
		if(workflowStatusActor.getUser() != null){
			this.userId = workflowStatusActor.getUser().getUserId();
			this.userName = workflowStatusActor.getUser().getLoginId();
		}
		if(workflowStatusActor.getProduct() != null){
			this.productId = workflowStatusActor.getProduct().getProductId();
			this.productName = workflowStatusActor.getProduct().getProductName();
		}
		this.actorMappingType = workflowStatusActor.getActorMappingType();
		this.actionRequirement = workflowStatusActor.getActionRequirement();
		this.userActionStatus = workflowStatusActor.getUserActionStatus();
	}
	
	public Integer getWorkflowStatusActorId() {
		return workflowStatusActorId;
	}
	public void setWorkflowStatusActorId(Integer workflowStatusActorId) {
		this.workflowStatusActorId = workflowStatusActorId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getActionRequirement() {
		return actionRequirement;
	}
	public void setActionRequirement(String actionRequirement) {
		this.actionRequirement = actionRequirement;
	}
	public String getUserActionStatus() {
		return userActionStatus;
	}
	public void setUserActionStatus(String userActionStatus) {
		this.userActionStatus = userActionStatus;
	}
	public Integer getWorkflowStatusId() {
		return workflowStatusId;
	}
	public void setWorkflowStatusId(Integer workflowStatusId) {
		this.workflowStatusId = workflowStatusId;
	}
	public Integer getEntityTypeId() {
		return entityTypeId;
	}
	public void setEntityTypeId(Integer entityTypeId) {
		this.entityTypeId = entityTypeId;
	}
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public Integer getEntityInstanceId() {
		return entityInstanceId;
	}
	public void setEntityInstanceId(Integer entityInstanceId) {
		this.entityInstanceId = entityInstanceId;
	}
	public String getActorMappingType() {
		return actorMappingType;
	}
	public void setActorMappingType(String actorMappingType) {
		this.actorMappingType = actorMappingType;
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

	@JsonIgnore
	public WorkflowStatusActor getWorkflowStatusActor(){
		WorkflowStatusActor workflowStatusActor = new WorkflowStatusActor();
		
		workflowStatusActor.setWorkflowStatusActorId(this.workflowStatusActorId);
		if(this.entityTypeId != null){
			EntityMaster entityMaster = new EntityMaster();
			entityMaster.setEntitymasterid(this.entityTypeId);
			workflowStatusActor.setEntityType(entityMaster);
		}
		workflowStatusActor.setEntityId(this.entityId);
		workflowStatusActor.setEntityInstanceId(this.entityInstanceId);
		if(this.workflowStatusId != null){
			WorkflowStatus workflowStatus = new WorkflowStatus();
			workflowStatus.setWorkflowStatusId(this.workflowStatusId);
			workflowStatusActor.setWorkflowStatus(workflowStatus);
		}
		if(this.roleId != null){
			UserRoleMaster userRoleMaster = new UserRoleMaster();
			userRoleMaster.setUserRoleId(this.roleId);
			workflowStatusActor.setRole(userRoleMaster);
		}
		if(this.userId != null){
			UserList userList = new UserList();
			userList.setUserId(this.userId);
			workflowStatusActor.setUser(userList);
		}
		workflowStatusActor.setActorMappingType(this.actorMappingType);
		workflowStatusActor.setActionRequirement(this.actionRequirement);
		if(this.userActionStatus != null && !this.userActionStatus.trim().isEmpty()){
			workflowStatusActor.setUserActionStatus(this.userActionStatus);
		}else{
			workflowStatusActor.setUserActionStatus(IDPAConstants.WORKFLOW_STATUS_USER_ACTION_NOT_COMPELTE);
		}
		if(this.productId != null && this.productId > 0){
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(this.productId);
			workflowStatusActor.setProduct(productMaster);
		}
		
		
		return workflowStatusActor;
	}
}
