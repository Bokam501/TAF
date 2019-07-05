package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ClarificationScope;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.ClarificationTypeMaster;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.EntityStatus;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.ClarificationTrackerDTO;

public class JsonClarificationTracker {

	private static final Log log = LogFactory.getLog(JsonActivity.class);
	@JsonProperty
	private Integer clarificationTrackerId;
	@JsonProperty
	private String clarificationTitle;
	@JsonProperty
	private Integer clarificationScopeId;
	@JsonProperty
	private Integer activityId;
	@JsonProperty
	private String clarificationDescription;
	@JsonProperty
	private String clarificationTypeName;
	@JsonProperty
	private Integer clarificationTypeId;
	@JsonProperty
	private String raisedDate;
	@JsonProperty
	private Integer raisedById;
	@JsonProperty
	private String raisedByName;
	@JsonProperty
	private Integer priorityId;
	@JsonProperty
	private String priorityName;
	@JsonProperty
	private Integer ownerId;
	@JsonProperty
	private String ownerName;
	@JsonProperty
	private Integer workflowStatusId;
	@JsonProperty
	private String workflowStatusName;
	@JsonProperty
	private Integer dependentCR;
	@JsonProperty
	private Integer dependentActivityTracker;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private Integer entityTypeId;
	@JsonProperty
	private Integer entityTypeId2;
	@JsonProperty
	private Integer entityInstanceId;
	@JsonProperty
	private String plannedStartDate;
	@JsonProperty
	private String plannedEndDate;
	@JsonProperty
	private String actualStartDate;
	@JsonProperty
	private String actualEndDate;
	@JsonProperty
	private Integer resolution;
	@JsonProperty
	private Integer testFactoryId;	
	@JsonProperty
	private String entityType;	
	@JsonProperty
	private String entityInstanceName;
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
	
	public JsonClarificationTracker() {
	}

	public JsonClarificationTracker(ClarificationTracker clarificationTracker) {
		this.clarificationTrackerId = clarificationTracker
				.getClarificationTrackerId();
		this.clarificationTitle = clarificationTracker.getClarificationTitle();
		this.dependentCR = clarificationTracker.getDependentCR();
		this.dependentActivityTracker = clarificationTracker
				.getDependentActivityTracker();
		this.clarificationDescription = clarificationTracker
				.getClarificationDescription();
		this.resolution = clarificationTracker.getResolution();
		this.entityInstanceId = clarificationTracker.getEntityInstanceId();
		this.planExpectedValue = clarificationTracker.getPlanExpectedValue();
		if(clarificationTracker.getClarificationType() != null){
			this.clarificationTypeId = clarificationTracker.getClarificationType().getClarificationId();
		}
		if (clarificationTracker.getPlannedStartDate() != null) {
			this.plannedStartDate = DateUtility.dateformatWithOutTime(clarificationTracker
					.getPlannedStartDate());
		}else {
			this.plannedStartDate = "dd-mm-yy";
		}
		if (clarificationTracker.getPlannedEndDate() != null) {
			this.plannedEndDate = DateUtility.dateformatWithOutTime(clarificationTracker
					.getPlannedEndDate());
		}else {
			this.plannedEndDate = "dd-mm-yy";
		}
		
		if (clarificationTracker.getEntityInstanceId() != null) {
			this.activityId = clarificationTracker.getEntityInstanceId();

		}
		if (clarificationTracker.getPriority() != null) {
			this.priorityId = clarificationTracker.getPriority()
					.getExecutionPriorityId();
			this.priorityName = clarificationTracker.getPriority()
					.getExecutionPriorityName();
		}
		if (clarificationTracker.getEntityStatus() != null) {			
			this.workflowStatusId = clarificationTracker.getEntityStatus().getEntityStatusId();
			this.workflowStatusName = clarificationTracker.getEntityStatus().getEntityStatusName();
		}
		if (clarificationTracker.getOwner() != null) {
			this.ownerId = clarificationTracker.getOwner().getUserId();
			this.ownerName = clarificationTracker.getOwner().getLoginId();
		}
		if (clarificationTracker.getRaisedBy() != null) {

			this.raisedById = clarificationTracker.getRaisedBy().getUserId();
			this.raisedByName = clarificationTracker.getRaisedBy().getLoginId();
		}

		if (clarificationTracker.getRaisedDate() != null){
			this.raisedDate = DateUtility
					.dateformatWithOutTime(clarificationTracker.getRaisedDate());
		}
		if(clarificationTracker.getActualStartDate() != null){
								
			this.actualStartDate = DateUtility.dateformatWithOutTime(clarificationTracker.getActualStartDate());
			
		}else {
			
			this.actualStartDate = "dd-mm-yy";
		}

		if(clarificationTracker.getActualEndDate() != null){
			
			this.actualEndDate = DateUtility.dateformatWithOutTime(clarificationTracker.getActualEndDate());
			
		}else {
			
			this.actualEndDate = "dd-mm-yy";
		}
		
		if(clarificationTracker.getClarificationScope() != null){
			this.clarificationScopeId = clarificationTracker.getClarificationScope().getClarificationScopeId();
		}
		
		if(clarificationTracker.getProduct() != null){
			this.productId = clarificationTracker.getProduct().getProductId();
		}
		
		if(clarificationTracker.getTestFactory() != null){
			this.testFactoryId = clarificationTracker.getTestFactory().getTestFactoryId();
		}
		
		if(clarificationTracker.getEntityType() != null){
			this.entityTypeId = clarificationTracker.getEntityType().getEntitymasterid();
		}
		
		if(clarificationTracker.getEntityType().getEntitymasterid() != null){
			
			this.entityType = clarificationTracker.getEntityType().getEntitymastername();
		}
		
		
	}

	public JsonClarificationTracker(ClarificationTrackerDTO clarificationTrackerDTO) {
		
	}
	

	@JsonIgnore	
	public ClarificationTracker getClarificationTracker() {	
		
		ClarificationTracker clarificationTracker = new ClarificationTracker();
		clarificationTracker.setClarificationTrackerId(clarificationTrackerId);
		
		clarificationTracker.setClarificationTitle(clarificationTitle);
		clarificationTracker
				.setClarificationDescription(clarificationDescription);
		clarificationTracker.setDependentCR(dependentCR);
		clarificationTracker
				.setDependentActivityTracker(dependentActivityTracker);
		clarificationTracker.setResolution(resolution);
		
		if(planExpectedValue != null){
			clarificationTracker.setPlanExpectedValue(planExpectedValue);
		}else{
			clarificationTracker.setPlanExpectedValue(1);
		}
		
		if(this.clarificationTypeId != null){
			ClarificationTypeMaster clarificationTypeMaster = new ClarificationTypeMaster();
			clarificationTypeMaster.setClarificationId(this.clarificationTypeId);
			clarificationTracker.setClarificationType(clarificationTypeMaster);
		}
		if(this.clarificationScopeId != null){
			ClarificationScope clarificationScope = new ClarificationScope();
			clarificationScope.setClarificationScopeId(this.clarificationScopeId);
			clarificationTracker.setClarificationScope(clarificationScope);
		}	
		
		if(entityInstanceId != null){
			clarificationTracker.setEntityInstanceId(entityInstanceId);
		}else{
			clarificationTracker.setEntityInstanceId(0);	
		}
		EntityStatus entityStatus = new EntityStatus();
		if (this.workflowStatusId != null) {
			entityStatus.setEntityStatusId(this.workflowStatusId);
		}
		clarificationTracker.setEntityStatus(entityStatus);

		if (this.priorityId != null) {
			ExecutionPriority priority = new ExecutionPriority();
			priority.setExecutionPriorityId(priorityId);
			priority.setExecutionPriorityName(priorityName);
			clarificationTracker.setPriority(priority);
		}
		if (this.ownerId != null) {
			UserList userlist = new UserList();
			userlist.setUserId(ownerId);
			userlist.setLoginId(ownerName);
			clarificationTracker.setOwner(userlist);
		}
		if (this.raisedById != null) {
			UserList userlist = new UserList();
			userlist.setUserId(raisedById);
			userlist.setLoginId(raisedByName);
			clarificationTracker.setRaisedBy(userlist);
		}
		
		if (this.raisedDate != null)
			clarificationTracker.setRaisedDate(DateUtility
					.dateformatWithOutTime(this.raisedDate));
			
		if (this.plannedStartDate != null) {
			if(this.plannedStartDate.equals("mm/dd/yy")){
	             clarificationTracker.setPlannedStartDate(null);	
			}
	       else if(this.plannedStartDate!="mm/dd/yy"){		
	    	   clarificationTracker.setPlannedStartDate(DateUtility
					.dateformatWithOutTime(this.plannedStartDate));
	       }
		}
		
		if (this.plannedEndDate != null) {
			if(this.plannedEndDate.equals("mm/dd/yy")){
	             clarificationTracker.setPlannedEndDate(null);;	
			}
	       else if(this.plannedEndDate!="mm/dd/yy"){		
	    	   clarificationTracker.setPlannedEndDate(DateUtility
					.dateformatWithOutTime(this.plannedEndDate));
	       }
		}
		if (this.actualStartDate != null) {
			if(this.actualStartDate.equals("mm/dd/yy")){
	             clarificationTracker.setActualStartDate(null);	
			}
	       else if(this.actualStartDate!="mm/dd/yy"){		
	    	   clarificationTracker.setActualStartDate(DateUtility
					.dateformatWithOutTime(this.actualStartDate));
	       }
		}
		
		if (this.actualEndDate!= null) {
			if(this.actualEndDate.equals("mm/dd/yy")){
	             clarificationTracker.setActualEndDate(null);	
			}
	       else if(this.actualEndDate!="mm/dd/yy"){		
	    	   clarificationTracker.setActualEndDate(DateUtility
					.dateformatWithOutTime(this.actualEndDate));
	       }
		}
		
		if(this.productId!= null){
			ProductMaster productMaster = new ProductMaster();
			if(productId == 0){
				productMaster = null;
			}else{
			productMaster.setProductId(this.productId);
			}
			clarificationTracker.setProduct(productMaster);
		}
		
		if(this.testFactoryId!= null){
			TestFactory testFactory = new TestFactory();
			testFactory.setTestFactoryId(this.testFactoryId);
			clarificationTracker.setTestFactory(testFactory);
		}
		
		if(this.entityTypeId!= null){
			EntityMaster entityMaster = new EntityMaster();
			entityMaster.setEntitymasterid(this.entityTypeId);
			clarificationTracker.setEntityType(entityMaster);
		}
		
		return clarificationTracker;
	}

	public String getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public String getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public Integer getClarificationTrackerId() {
		return clarificationTrackerId;
	}

	public void setClarificationTrackerId(Integer clarificationTrackerId) {
		this.clarificationTrackerId = clarificationTrackerId;
	}

	public String getClarificationTitle() {
		return clarificationTitle;
	}

	public void setClarificationTitle(String clarificationTitle) {
		this.clarificationTitle = clarificationTitle;
	}

	public String getClarificationDescription() {
		return clarificationDescription;
	}

	public void setClarificationDescription(String clarificationDescription) {
		this.clarificationDescription = clarificationDescription;
	}

	public String getRaisedDate() {
		return raisedDate;
	}

	public void setRaisedDate(String raisedDate) {
		this.raisedDate = raisedDate;
	}

	public Integer getRaisedById() {
		return raisedById;
	}

	public void setRaisedById(Integer raisedById) {
		this.raisedById = raisedById;
	}

	public String getRaisedByName() {
		return raisedByName;
	}

	public void setRaisedByName(String raisedByName) {
		this.raisedByName = raisedByName;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Integer getWorkflowStatusId() {
		return workflowStatusId;
	}

	public void setWorkflowStatusId(Integer workflowStatusId) {
		this.workflowStatusId = workflowStatusId;
	}

	public String getWorkflowStatusName() {
		return workflowStatusName;
	}

	public void setWorkflowStatusName(String workflowStatusName) {
		this.workflowStatusName = workflowStatusName;
	}

	public Integer getDependentCR() {
		return dependentCR;
	}

	public void setDependentCR(Integer dependentCR) {
		this.dependentCR = dependentCR;
	}

	public Integer getDependentActivityTracker() {
		return dependentActivityTracker;
	}

	public void setDependentActivityTracker(Integer dependentActivityTracker) {
		this.dependentActivityTracker = dependentActivityTracker;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	public String getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(String plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public String getPlannedEndDate() {
		return plannedEndDate;
	}

	public void setPlannedEndDate(String plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}

	public Integer getResolution() {
		return resolution;
	}

	public void setResolution(Integer resolution) {
		this.resolution = resolution;
	}
	public Integer getEntityInstanceId() {
		return entityInstanceId;
	}
	public void setEntityInstanceId(Integer entityInstanceId) {
		this.entityInstanceId = entityInstanceId;
	}

	public String getClarificationTypeName() {
		return clarificationTypeName;
	}

	public void setClarificationTypeName(String clarificationTypeName) {
		this.clarificationTypeName = clarificationTypeName;
	}

	public Integer getClarificationTypeId() {
		return clarificationTypeId;
	}

	public void setClarificationTypeId(Integer clarificationTypeId) {
		this.clarificationTypeId = clarificationTypeId;
	}
	
	public Integer getClarificationScopeId() {
		return clarificationScopeId;
	}

	public void setClarificationScopeId(Integer clarificationScopeId) {
		this.clarificationScopeId = clarificationScopeId;
	}

	public Integer getTestFactoryId() {
		return testFactoryId;
	}

	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}

	public Integer getEntityTypeId() {
		return entityTypeId;
	}

	public void setEntityTypeId(Integer entityTypeId) {
		this.entityTypeId = entityTypeId;
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
	
	public Integer getPlanExpectedValue() {
		return planExpectedValue;
	}

	public void setPlanExpectedValue(Integer planExpectedValue) {
		this.planExpectedValue = planExpectedValue;
	}
	
	public Integer getEntityTypeId2() {
		return entityTypeId2;
	}

	public void setEntityTypeId2(Integer entityTypeId2) {
		this.entityTypeId2 = entityTypeId2;
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
