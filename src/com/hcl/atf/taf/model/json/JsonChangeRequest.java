package com.hcl.atf.taf.model.json;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.ChangeRequestType;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.atf.taf.model.UserList;


public class JsonChangeRequest {


	private static final Log log = LogFactory.getLog(JsonActivity.class);
	@JsonProperty
	private Integer changeRequestId;
	@JsonProperty
	private String changeRequestName;
	@JsonProperty
	private String description;
	@JsonProperty
	private String  raisedDate;
	@JsonProperty
	private Integer priorityId;
	@JsonProperty
	private String priorityName;
	@JsonProperty
	private Integer ownerId;
	@JsonProperty
	private String ownerName;
	@JsonProperty
	private Integer statusCategoryId;
	@JsonProperty
	private String statusCategoryName;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer isActive;
	@JsonProperty
	private Integer entityType1;
	@JsonProperty
	private Integer entityType2;
	@JsonProperty
	private Integer entityInstance1;
	@JsonProperty
	private Integer changeRequestType;
	@JsonProperty
	private String changeRequestTypeName;
	@JsonProperty
	private Integer planExpectedValue;
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
	@JsonProperty
	private String entityType;	
	@JsonProperty
	private String entityInstanceName;
	
	public JsonChangeRequest(){
	}
	
	public JsonChangeRequest(ChangeRequest changeRequest) {
		this.changeRequestId = changeRequest.getChangeRequestId();	
		this.changeRequestName = changeRequest.getChangeRequestName();	
		this.description = changeRequest.getDescription();	
		this.planExpectedValue = changeRequest.getPlanExpectedValue();
		if (changeRequest.getPriority() != null) {			
			this.priorityId = changeRequest.getPriority().getExecutionPriorityId();
			this.priorityName = changeRequest.getPriority().getExecutionPriorityName();
		}
		if (changeRequest.getStatusCategory() != null) {			
			this.statusCategoryId = changeRequest.getStatusCategory().getStatusCategoryId();
			this.statusCategoryName = changeRequest.getStatusCategory().getStatusCategoryName();
		}
		if (changeRequest.getOwner() != null) {			
			this.ownerId = changeRequest.getOwner().getUserId();
			this.ownerName = changeRequest.getOwner().getLoginId();
		}
		
		if (changeRequest.getProduct() != null) {			
			this.productId = changeRequest.getProduct().getProductId();
			this.productName = changeRequest.getProduct().getProductName();
		}
		
		if (changeRequest.getRaisedDate() != null) {
			this.raisedDate = DateUtility.dateformatWithOutTime(changeRequest
					.getRaisedDate());
		}
		
		if (changeRequest.getModifiedDate() != null)
			this.modifiedDate = DateUtility.dateToStringInSecond(changeRequest.getRaisedDate());
		
		if (changeRequest.getIsActive() != null) {
			this.isActive = changeRequest.getIsActive();		
		}
		
		if(changeRequest.getCrType() != null){			
			this.changeRequestType = changeRequest.getCrType().getChangeRequestTypeId();
			this.changeRequestTypeName = changeRequest.getCrType().getChangeRequestTypeName();
		}
		if(this.attachmentCount == null){
			this.attachmentCount = 0;
		}
		
		if(changeRequest.getEntityType() != null){
			this.entityType = changeRequest.getEntityType().getEntitymastername();
		}
	}
	@JsonIgnore
	public ChangeRequest getChangeRequest() {
		ChangeRequest changeRequest = new ChangeRequest();
		changeRequest.setChangeRequestId(changeRequestId);
		changeRequest.setChangeRequestName(changeRequestName);
		changeRequest.setDescription(description);
		changeRequest.setIsActive(isActive);
		
		if(planExpectedValue != null){
			changeRequest.setPlanExpectedValue(this.planExpectedValue);
		}else{
			changeRequest.setPlanExpectedValue(1);
		}
		
		StatusCategory statusCategory = new StatusCategory();
		if (this.statusCategoryId != null) {
			statusCategory.setStatusCategoryId(this.statusCategoryId);;
		}else{
			statusCategory.setStatusCategoryId(1);
		}
		changeRequest.setStatusCategory(statusCategory);

		if (this.priorityId != null) {
			ExecutionPriority priority = new ExecutionPriority();
			priority.setExecutionPriorityId(priorityId);
			priority.setExecutionPriorityName(priorityName);
			changeRequest.setPriority(priority);
		}
		if (this.ownerId != null) {
			UserList userlist = new UserList();
			userlist.setUserId(ownerId);
			userlist.setLoginId(ownerName);
			changeRequest.setOwner(userlist);
		}
		
		if (this.productId != null) {
			ProductMaster product = new ProductMaster();
			product.setProductId(productId);
			product.setProductName(productName);
			changeRequest.setProduct(product);
		}
		
		if (this.raisedDate != null) {
			changeRequest.setRaisedDate(DateUtility
					.dateformatWithOutTime(this.raisedDate));
		}
		
		if (this.modifiedDate != null) {
			changeRequest.setModifiedDate(DateUtility
					.dateformatWithOutTime(this.modifiedDate));
		}
		
		if(this.changeRequestType != null){
			ChangeRequestType changeRequestType = new ChangeRequestType();
			changeRequestType.setChangeRequestTypeId(this.changeRequestType);
			changeRequestType.setChangeRequestTypeName(this.changeRequestTypeName);
			changeRequest.setCrType(changeRequestType);
		}
		
		if(this.entityType1 != null){
			EntityMaster entityMaster = new EntityMaster();
			entityMaster.setEntitymasterid(this.entityType1);
			changeRequest.setEntityType(entityMaster);
		}
		
		return changeRequest;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getChangeRequestId() {
		return changeRequestId;
	}
	public void setChangeRequestId(Integer changeRequestId) {
		this.changeRequestId = changeRequestId;
	}
	public String getRaisedDate() {
		return raisedDate;
	}
	public void setRaisedDate(String raisedDate) {
		this.raisedDate = raisedDate;
	}
	public Integer getPriorityId() {
		return priorityId;
	}
	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getStatusCategoryId() {
		return statusCategoryId;
	}

	public void setStatusCategoryId(Integer statusCategoryId) {
		this.statusCategoryId = statusCategoryId;
	}

	public String getPriorityName() {
		return priorityName;
	}
	
	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getStatusCategoryName() {
		return statusCategoryName;
	}

	public void setStatusCategoryName(String statusCategoryName) {
		this.statusCategoryName = statusCategoryName;
	}
	

	public String getChangeRequestName() {
		return changeRequestName;
	}

	public void setChangeRequestName(String changeRequestName) {
		this.changeRequestName = changeRequestName;
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

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getEntityType1() {
		return entityType1;
	}

	public void setEntityType1(Integer entityType1) {
		this.entityType1 = entityType1;
	}

	public Integer getEntityType2() {
		return entityType2;
	}

	public void setEntityType2(Integer entityType2) {
		this.entityType2 = entityType2;
	}

	public Integer getEntityInstance1() {
		return entityInstance1;
	}

	public void setEntityInstance1(Integer entityInstance1) {
		this.entityInstance1 = entityInstance1;
	}
	public Integer getChangeRequestType() {
		return changeRequestType;
	}

	public void setChangeRequestType(Integer changeRequestType) {
		this.changeRequestType = changeRequestType;
	}
	
	public Integer getPlanExpectedValue() {
		return planExpectedValue;
	}

	public void setPlanExpectedValue(Integer planExpectedValue) {
		this.planExpectedValue = planExpectedValue;
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

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityInstanceName() {
		return entityInstanceName;
	}

	public void setEntityInstanceName(String entityInstanceName) {
		this.entityInstanceName = entityInstanceName;
	}	
}