package com.hcl.atf.taf.model.json;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.DimensionMaster;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.ActivityWorkPackageSummaryDTO;
import com.hcl.ilcm.workflow.model.WorkflowStatus;

public class JsonActivityWorkPackage {

	private static final Log log = LogFactory
			.getLog(JsonActivityWorkPackage.class);
	
	@JsonProperty
	private Integer activityWorkPackageId;
	@JsonProperty
	private String activityWorkPackageName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer productVersionListId;
	@JsonProperty
	private String productVersionName;
	@JsonProperty
	private Integer productBuildId;
	@JsonProperty
	private String productBuildName;
	@JsonProperty
	private String baselineStartDate;
	@JsonProperty
	private String baselineEndDate;
	@JsonProperty
	private  String plannedStartDate; 
	@JsonProperty
	private  String plannedEndDate;
	@JsonProperty
	private  String actualStartDate;
	@JsonProperty
	private  String actualEndDate;
	@JsonProperty
	private  Integer statusCategoryId;
	@JsonProperty
	private  String statusCategoryName;
	@JsonProperty
	private  Integer  priorityId;
	@JsonProperty
	private  String priorityName;
	@JsonProperty
	private  Integer  competencyId;
	@JsonProperty
	private  String competencyName;
	@JsonProperty
	private Integer ownerId;
	@JsonProperty
	private String ownerName;
	@JsonProperty
	private String activityWorkPackageTag;
	@JsonProperty
	private String activityWorkPackageType;
	@JsonProperty
	private String remark;
	@JsonProperty
	private Integer isActive;
	

	@JsonProperty
	private  String createdDate;
	@JsonProperty
	private  String modifiedDate;
	@JsonProperty
	private  Integer createdById;
	@JsonProperty
	private  String createdByName;
	@JsonProperty
	private  Integer modifiedById;
	@JsonProperty
	private  String modifiedByName;
	
	@JsonProperty
	private Integer statusId;
	@JsonProperty
	private String statusName;
	@JsonProperty
	private String statusDisplayName;
	@JsonProperty
	private String workflowStatusCategoryName;
	@JsonProperty
	private String workflowStatusName;
	@JsonProperty
	private String workflowStatusDisplayName;
	@JsonProperty
	private Integer totalEffort;
	@JsonProperty
	private Integer workflowId;
	@JsonProperty
	private String actors;
	@JsonProperty
	private String completedBy;
	@JsonProperty
	private Integer remainingHours;
	@JsonProperty
	private String workflowIndicator;
	@JsonProperty
	private String workflowRAG;
	@JsonProperty
	private Integer attachmentCount;	
	@JsonProperty
	private String remainingHrsMins;	
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
	private Float percentageCompletion;
	
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private String productVesionBuildName;	
	@JsonProperty
	private Integer activityCount;	
	@JsonProperty
	private Integer clarificationCount;
	@JsonProperty
	private Integer changeRequestCount;
	@JsonProperty
	private Integer actBeginCount;
	@JsonProperty
	private Integer actIntermediateCount;
	@JsonProperty
	private Integer actAbortCount;
	@JsonProperty
	private Integer actEndCount;
	@JsonProperty
	private String emailId;
	
	@JsonProperty
	private boolean visibleEventComment;
	
	public Integer getActivityWorkPackageId() {
		return activityWorkPackageId;
	}

	public void setActivityWorkPackageId(Integer activityWorkPackageId) {
		this.activityWorkPackageId = activityWorkPackageId;
	}

	public String getActivityWorkPackageName() {
		return activityWorkPackageName;
	}

	public void setActivityWorkPackageName(String activityWorkPackageName) {
		this.activityWorkPackageName = activityWorkPackageName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getProductVersionListId() {
		return productVersionListId;
	}

	public void setProductVersionListId(Integer productVersionListId) {
		this.productVersionListId = productVersionListId;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}

	public Integer getProductBuildId() {
		return productBuildId;
	}

	public void setProductBuildId(Integer productBuildId) {
		this.productBuildId = productBuildId;
	}

	public String getProductBuildName() {
		return productBuildName;
	}

	public void setProductBuildName(String productBuildName) {
		this.productBuildName = productBuildName;
	}

	public String getBaselineStartDate() {
		return baselineStartDate;
	}

	public void setBaselineStartDate(String baselineStartDate) {
		this.baselineStartDate = baselineStartDate;
	}

	public String getBaselineEndDate() {
		return baselineEndDate;
	}

	public void setBaselineEndDate(String baselineEndDate) {
		this.baselineEndDate = baselineEndDate;
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
	public Integer getStatusCategoryId() {
		return statusCategoryId;
	}

	public void setStatusCategoryId(Integer statusCategoryId) {
		this.statusCategoryId = statusCategoryId;
	}

	public String getStatusCategoryName() {
		return statusCategoryName;
	}

	public void setStatusCategoryName(String statusCategoryName) {
		this.statusCategoryName = statusCategoryName;
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

	public Integer getCompetencyId() {
		return competencyId;
	}

	public void setCompetencyId(Integer competencyId) {
		this.competencyId = competencyId;
	}

	public String getCompetencyName() {
		return competencyName;
	}

	public void setCompetencyName(String competencyName) {
		this.competencyName = competencyName;
	}

	public String getActivityWorkPackageTag() {
		return activityWorkPackageTag;
	}

	public void setActivityWorkPackageTag(String activityWorkPackageTag) {
		this.activityWorkPackageTag = activityWorkPackageTag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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


	public String getActivityWorkPackageType() {
		return activityWorkPackageType;
	}

	public void setActivityWorkPackageType(String activityWorkPackageType) {
		this.activityWorkPackageType = activityWorkPackageType;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public Integer getModifiedById() {
		return modifiedById;
	}

	public void setModifiedById(Integer modifiedById) {
		this.modifiedById = modifiedById;
	}

	public String getModifiedByName() {
		return modifiedByName;
	}

	public void setModifiedByName(String modifiedByName) {
		this.modifiedByName = modifiedByName;
	}
	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}


	public JsonActivityWorkPackage() {
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getStatusDisplayName() {
		return statusDisplayName;
	}

	public void setStatusDisplayName(String statusDisplayName) {
		this.statusDisplayName = statusDisplayName;
	}

	public String getWorkflowStatusCategoryName() {
		return workflowStatusCategoryName;
	}

	public void setWorkflowStatusCategoryName(String workflowStatusCategoryName) {
		this.workflowStatusCategoryName = workflowStatusCategoryName;
	}

	public String getWorkflowStatusName() {
		return workflowStatusName;
	}
	
	public void setWorkflowStatusName(String workflowStatusName) {
		this.workflowStatusName = workflowStatusName;
	}

	public String getWorkflowStatusDisplayName() {
		return workflowStatusDisplayName;
	}

	public void setWorkflowStatusDisplayName(String workflowStatusDisplayName) {
		this.workflowStatusDisplayName = workflowStatusDisplayName;
	}

	public Integer getTotalEffort() {
		return totalEffort;
	}

	public void setTotalEffort(Integer totalEffort) {
		this.totalEffort = totalEffort;
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getCompletedBy() {
		return completedBy;
	}

	public void setCompletedBy(String completedBy) {
		this.completedBy = completedBy;
	}

	public Integer getRemainingHours() {
		return remainingHours;
	}

	public void setRemainingHours(Integer remainingHours) {
		this.remainingHours = remainingHours;
	}

	public String getWorkflowIndicator() {
		return workflowIndicator;
	}

	public void setWorkflowIndicator(String workflowIndicator) {
		this.workflowIndicator = workflowIndicator;
	}

	public String getWorkflowRAG() {
		return workflowRAG;
	}

	public void setWorkflowRAG(String workflowRAG) {
		this.workflowRAG = workflowRAG;
	}

	public Integer getAttachmentCount() {
		return attachmentCount;
	}

	public void setAttachmentCount(Integer attachmentCount) {
		this.attachmentCount = attachmentCount;
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

	public Integer getActivityCount() {
		return activityCount;
	}

	public void setActivityCount(Integer activityCount) {
		this.activityCount = activityCount;
	}
	
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public JsonActivityWorkPackage(ActivityWorkPackage activityWorkPackage) {
		
		this.activityWorkPackageId = activityWorkPackage.getActivityWorkPackageId();
		this.activityWorkPackageName=activityWorkPackage.getActivityWorkPackageName();
		this.description = activityWorkPackage.getDescription();
		this.activityWorkPackageTag =activityWorkPackage.getActivityWorkPackageTag();
		this.activityWorkPackageType = activityWorkPackage.getActivityWorkPackageType();
		this.remark=activityWorkPackage.getRemark();
		this.isActive=activityWorkPackage.getIsActive();
		this.emailId = "";
		this.remainingHours = 0;
		this.remainingHrsMins = new String("0");
		this.workflowRAG = "<i class="+"'fa fa-sort-asc'"+" ; "+"' title='"+"Action Completed'"+"></i>";
		this.workflowIndicator = "<i class='fa fa-circle' style='color: red;' title='SLA duration elapsed'></i>";
		this.completedBy = "--";
		this.actors = "--";
		this.attachmentCount = 0;
		
		if(activityWorkPackage.getProductBuild() != null){
			if(activityWorkPackage.getProductBuild().getProductVersion() != null){
				if(activityWorkPackage.getProductBuild().getProductVersion().getProductMaster() != null){
					this.productId = activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
					this.productName = activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductName();
				}
			}
			
		}
		
		if (activityWorkPackage.getProductBuild() != null)
		{ 
			this.productBuildId = activityWorkPackage.getProductBuild().getProductBuildId();
			this.productBuildName = activityWorkPackage.getProductBuild().getBuildname();
			
			if(activityWorkPackage.getProductBuild().getProductVersion() != null){
				this.productVersionName = activityWorkPackage.getProductBuild().getProductVersion().getProductVersionName();			

				if(activityWorkPackage.getProductBuild().getProductVersion().getProductMaster() != null){
					this.productName = activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductName();
					
					this.productVesionBuildName = this.productName+"/"+this.productVersionName+"/"+this.productBuildName ;
				}						
			}
		}
		
		if (activityWorkPackage.getPriority() != null)
		{ 
			this.priorityId=activityWorkPackage.getPriority().getExecutionPriorityId();
			this.priorityName=activityWorkPackage.getPriority().getExecutionPriorityName();
		}
		if (activityWorkPackage.getStatusCategory()!= null)
		{ 
			this.statusCategoryId=activityWorkPackage.getStatusCategory().getStatusCategoryId();
			this.statusCategoryName=activityWorkPackage.getStatusCategory().getStatusCategoryName();
		}else{
			statusCategoryName = "--";
		}
		
		if(activityWorkPackage.getCompetency() != null){
			this.competencyId = activityWorkPackage.getCompetency().getDimensionId();
			this.competencyName = activityWorkPackage.getCompetency().getName();
		}else{
			this.competencyName = "--";
		}
		
		if (activityWorkPackage.getOwner()!= null){
			this.ownerId=activityWorkPackage.getOwner().getUserId();
			this.ownerName=activityWorkPackage.getOwner().getLoginId();
			this.emailId=activityWorkPackage.getOwner().getEmailId();
		}else{
			 this.ownerName="--";
			 this.emailId="--";
		}
		if(activityWorkPackage.getCreatedDate() != null){
			this.createdDate = DateUtility.dateToStringWithSeconds1(activityWorkPackage.getCreatedDate());
		}
		if(activityWorkPackage.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(activityWorkPackage.getModifiedDate());
		}
		
		if(activityWorkPackage.getCreatedBy()!=null){
			this.createdById=activityWorkPackage.getCreatedBy().getUserId();
			this.createdByName=activityWorkPackage.getCreatedBy().getLoginId();	
		}else{
			this.createdByName="--";
		}
		
       if(activityWorkPackage.getModifiedBy()!=null){
			this.modifiedById=activityWorkPackage.getModifiedBy().getUserId();
			this.modifiedByName=activityWorkPackage.getModifiedBy().getLoginId();	
		}else{
			this.modifiedByName="--";
		}
       
      if(activityWorkPackage.getPlannedStartDate() != null){
			this.plannedStartDate = DateUtility.dateformatWithOutTime(activityWorkPackage.getPlannedStartDate());
       }
       
       if(activityWorkPackage.getPlannedEndDate() != null){
			this.plannedEndDate = DateUtility.dateformatWithOutTime(activityWorkPackage.getPlannedEndDate());
       }
       
       if(activityWorkPackage.getBaselineStartDate() != null){
			this.baselineStartDate = DateUtility.dateformatWithOutTime(activityWorkPackage.getBaselineStartDate());
      }else{
    	  this.baselineStartDate = this.plannedStartDate;
      }
      
      if(activityWorkPackage.getBaselineEndDate() != null){
			this.baselineEndDate = DateUtility.dateformatWithOutTime(activityWorkPackage.getBaselineEndDate());
      }else{
    	  this.baselineEndDate = this.plannedEndDate;
      }
      
       if (activityWorkPackage.getActualStartDate()!= null)			
			this.actualStartDate = DateUtility.dateformatWithOutTime(activityWorkPackage.getActualStartDate());
		else {
			this.actualStartDate = "dd-mm-yy";
		}

       if(activityWorkPackage.getActualEndDate() != null){
			this.actualEndDate = DateUtility.dateformatWithOutTime(activityWorkPackage.getActualEndDate());
       }     		
	
       if(activityWorkPackage.getWorkflowStatus() != null){
    	   this.statusId = activityWorkPackage.getWorkflowStatus().getWorkflowStatusId();
    	   if(statusId != -1){
    		   this.statusName = activityWorkPackage.getWorkflowStatus().getWorkflowStatusName();
        	   this.statusDisplayName = activityWorkPackage.getWorkflowStatus().getWorkflowStatusDisplayName();
        	   if(activityWorkPackage.getWorkflowStatus().getWorkflowStatusCategory() != null){
        		   this.workflowStatusCategoryName = "["+activityWorkPackage.getWorkflowStatus().getWorkflowStatusCategory().getWorkflowStatusCategoryName()+"]";
        	   }else{
        		   this.workflowStatusCategoryName = "";
        	   } 
    	   }else{
    		   this.statusName = "";
        	   this.statusDisplayName = "";
        	   this.workflowStatusCategoryName = "";
    	   }
    	 
       }else{
			this.statusName = "";
			this.statusDisplayName = "";
			this.workflowStatusCategoryName = "";
		}
       
       if(activityWorkPackage.getTotalEffort() != null){
			this.totalEffort = activityWorkPackage.getTotalEffort();
		}else{
			this.totalEffort = 0;
		}
       if(activityWorkPackage.getPercentageCompletion() != null){
    	   this.percentageCompletion = activityWorkPackage.getPercentageCompletion();
       }else{
    	   this.percentageCompletion = 0.0F;
       }
       this.visibleEventComment=visibleEventComment;
       
       
		/*this.clarificationCount = 0;
		this.changeRequestCount = 0;
		
		this.actBeginCount = 0;
		this.actIntermediateCount = 0;
		this.actAbortCount = 0;
		this.actEndCount = 0;
		this.activityCount = 0;
       */
	}

	public JsonActivityWorkPackage(ActivityWorkPackageSummaryDTO activityWorkPackageSummaryDTO) 
	{
		this.activityWorkPackageId = activityWorkPackageSummaryDTO.getActivityWorkPackageId();
		this.activityWorkPackageName = activityWorkPackageSummaryDTO.getActivityWorkPackageName();
		this.description = activityWorkPackageSummaryDTO.getDescription();
		if (activityWorkPackageSummaryDTO.getPlannedStartDate() != null) {
			this.plannedStartDate = DateUtility.sdfDateformatWithOutTime(activityWorkPackageSummaryDTO.getPlannedStartDate());
		}

		if (activityWorkPackageSummaryDTO.getPlannedEndDate() != null) {
			this.plannedEndDate = DateUtility.sdfDateformatWithOutTime(activityWorkPackageSummaryDTO.getPlannedEndDate());
		}

		if(activityWorkPackageSummaryDTO.getBaselineStartDate() != null){
			this.baselineStartDate = DateUtility.dateformatWithOutTime(activityWorkPackageSummaryDTO.getBaselineStartDate());
		}else{
		  this.baselineStartDate = this.plannedStartDate;
		}
		  
		if(activityWorkPackageSummaryDTO.getBaselineEndDate() != null){
			this.baselineEndDate = DateUtility.dateformatWithOutTime(activityWorkPackageSummaryDTO.getBaselineEndDate());
		}else{
		  this.baselineEndDate = this.plannedEndDate;
		}
	      
		if (activityWorkPackageSummaryDTO.getActualStartDate() != null) {
			this.actualStartDate = DateUtility.sdfDateformatWithOutTime(activityWorkPackageSummaryDTO.getActualStartDate());
		}

		if (activityWorkPackageSummaryDTO.getActualEndDate() != null) {
			this.actualEndDate = DateUtility.sdfDateformatWithOutTime(activityWorkPackageSummaryDTO.getActualEndDate());
		}
		 
		this.isActive = activityWorkPackageSummaryDTO.getIsActive();
		
		this.productBuildId = activityWorkPackageSummaryDTO.getProductBuildId();
		this.productBuildName = activityWorkPackageSummaryDTO.getProductBuildName();

		this.productVersionListId = activityWorkPackageSummaryDTO.getProductVersionListId();
		this.productVersionName = activityWorkPackageSummaryDTO.getProductVersionName();	
		
		this.productId = activityWorkPackageSummaryDTO.getProductId();
		this.productName = activityWorkPackageSummaryDTO.getProductName();
		
		this.ownerId = activityWorkPackageSummaryDTO.getOwnerId();
		this.ownerName = activityWorkPackageSummaryDTO.getOwnerName();
		
		this.statusCategoryId = activityWorkPackageSummaryDTO.getStatusCategoryId();
		this.statusCategoryName = activityWorkPackageSummaryDTO.getStatusCategoryName();		
		this.workflowStatusName = activityWorkPackageSummaryDTO.getWorkflowStatusName();
		this.workflowStatusDisplayName = activityWorkPackageSummaryDTO.getWorkflowStatusDisplayName();
		this.clarificationCount = activityWorkPackageSummaryDTO.getClarificationCount();
		this.changeRequestCount = activityWorkPackageSummaryDTO.getChangeRequestCount();
		this.attachmentCount = activityWorkPackageSummaryDTO.getAttachmentCount();
		this.actBeginCount = activityWorkPackageSummaryDTO.getActBeginCount();
		this.actIntermediateCount = activityWorkPackageSummaryDTO.getActIntermediateCount();
		this.actAbortCount = activityWorkPackageSummaryDTO.getActAbortCount();
		this.actEndCount = activityWorkPackageSummaryDTO.getActEndCount();
		this.activityCount = activityWorkPackageSummaryDTO.getActivityCount();
	}

	@JsonIgnore
	public ActivityWorkPackage getActivityWorkPackage(){
		
		ActivityWorkPackage activityWorkPackage = new ActivityWorkPackage();
		activityWorkPackage.setDescription(this.description);		
			
		if(activityWorkPackageId!=null){
			activityWorkPackage.setActivityWorkPackageId(activityWorkPackageId);			
		}
		
		activityWorkPackage.setActivityWorkPackageName(activityWorkPackageName);
		activityWorkPackage.setIsActive(isActive);
	
		activityWorkPackage.setRemark(remark);
		if(this.productBuildId != null){
			ProductBuild productBuild = new ProductBuild();
			productBuild.setProductBuildId(productBuildId);
			productBuild.setBuildname(productBuildName);
			activityWorkPackage.setProductBuild(productBuild);
			
		}
		
		if(this.productId != null){
			
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(productId);
			productMaster.setProductName(productName);
			activityWorkPackage.setProductMaster(productMaster);
			
		}
		StatusCategory statusCategory = new StatusCategory();
		if(this.statusCategoryId != null && this.statusCategoryId > 0){			
			statusCategory.setStatusCategoryId(statusCategoryId);
			statusCategory.setStatusCategoryName(statusCategoryName);
		}else{
			statusCategory.setStatusCategoryId(1);
		}
		activityWorkPackage.setStatusCategory(statusCategory);		
		
		if(this.priorityId != null ){			
			ExecutionPriority priority = new ExecutionPriority();
			priority.setExecutionPriorityId(priorityId);
			priority.setExecutionPriorityName(priorityName);
			activityWorkPackage.setPriority(priority);		
		}
		
		if(this.competencyId != null){
			DimensionMaster dimensionMaster = new DimensionMaster();
			dimensionMaster.setDimensionId(competencyId);
			activityWorkPackage.setCompetency(dimensionMaster);
		}
		
		if(this.ownerId != null){
			UserList userlist = new UserList();
			userlist.setUserId(ownerId);
			userlist.setLoginId(ownerName);
			activityWorkPackage.setOwner(userlist);
		}
		
	   if(this.plannedStartDate != null) {			
			activityWorkPackage.setPlannedStartDate(DateUtility.dateformatWithOutTime(this.plannedStartDate));
					
		}
		
		if(this.plannedEndDate != null) {
			activityWorkPackage.setPlannedEndDate(DateUtility.dateformatWithOutTime(this.plannedEndDate));
		}
		
		if(this.baselineStartDate != null) {			
			activityWorkPackage.setBaselineStartDate(DateUtility.dateformatWithOutTime(this.baselineStartDate));
					
		}else{
			activityWorkPackage.setBaselineStartDate(activityWorkPackage.getPlannedStartDate());
		}
		
		if(this.baselineEndDate != null) {
			activityWorkPackage.setBaselineEndDate(DateUtility.dateformatWithOutTime(this.baselineEndDate));
		}else{
			activityWorkPackage.setBaselineEndDate(activityWorkPackage.getPlannedEndDate());
		}
	
		if (this.actualStartDate != null) {
			if(this.actualStartDate.equals("mm/dd/yy"))
				activityWorkPackage.setActualStartDate(null);				
		       else if(this.actualStartDate!="mm/dd/yy")
		    	   activityWorkPackage.setActualStartDate(DateUtility.dateformatWithOutTime(this.actualStartDate));
		 }
		
		if(this.actualEndDate != null) {
			activityWorkPackage.setActualEndDate(DateUtility.dateformatWithOutTime(this.actualEndDate));
		}
		
		if(this.createdById!=null){
			UserList user=new UserList();
			user.setUserId(createdById);
			user.setLoginId(createdByName);
			activityWorkPackage.setCreatedBy(user);
		}
		
		if(this.modifiedById!=null){
			UserList user=new UserList();
			user.setUserId(createdById);
			user.setLoginId(createdByName);
			activityWorkPackage.setModifiedBy(user);
		}
		
		if(this.statusId != null){
			WorkflowStatus workflowStatus = new WorkflowStatus();
			workflowStatus.setWorkflowStatusId(this.statusId);
			activityWorkPackage.setWorkflowStatus(workflowStatus);
		}
		
		activityWorkPackage.setTotalEffort(this.totalEffort);
		if(this.percentageCompletion != null){
			activityWorkPackage.setPercentageCompletion(this.percentageCompletion);
		}else{
			activityWorkPackage.setPercentageCompletion(0.0F);
		}
		
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			activityWorkPackage.setCreatedDate(DateUtility.getCurrentTime());
		} else {		
			activityWorkPackage.setCreatedDate(DateUtility.toFormatDate(this.createdDate));
		}
		activityWorkPackage.setModifiedDate(DateUtility.getCurrentTime());
		
		
		
		return activityWorkPackage;
	}
	
	@JsonIgnore
	public ActivityWorkPackage getActivityWorkPackage(String activityWorkpackageName){
		
		ActivityWorkPackage activityWorkPackage = new ActivityWorkPackage();
		activityWorkPackage.setDescription(this.description);		
			
		if(activityWorkPackageId!=null){
			activityWorkPackage.setActivityWorkPackageId(activityWorkPackageId);
			
		}
		
		activityWorkPackage.setActivityWorkPackageName(activityWorkPackageName);
		activityWorkPackage.setIsActive(isActive);
	
		activityWorkPackage.setRemark(remark);
		if(this.productBuildId != null){
			ProductBuild productBuild = new ProductBuild();
			productBuild.setProductBuildId(productBuildId);
			productBuild.setBuildname(productBuildName);
			activityWorkPackage.setProductBuild(productBuild);
			
		}
		StatusCategory statusCategory = new StatusCategory();
		if(this.statusCategoryId != null && this.statusCategoryId > 0){			
			statusCategory.setStatusCategoryId(statusCategoryId);
			statusCategory.setStatusCategoryName(statusCategoryName);
		}else{
			statusCategory.setStatusCategoryId(1);
		}
		activityWorkPackage.setStatusCategory(statusCategory);		
		
		if(this.priorityId != null ){			
			ExecutionPriority priority = new ExecutionPriority();
			priority.setExecutionPriorityId(priorityId);
			priority.setExecutionPriorityName(priorityName);
			activityWorkPackage.setPriority(priority);		
		}
		
		if(this.competencyId != null){
			DimensionMaster dimensionMaster = new DimensionMaster();
			dimensionMaster.setDimensionId(competencyId);
			activityWorkPackage.setCompetency(dimensionMaster);
		}
		
		if(this.ownerId != null){
			UserList userlist = new UserList();
			userlist.setUserId(ownerId);
			userlist.setLoginId(ownerName);
			activityWorkPackage.setOwner(userlist);
		}
		
	   if(this.plannedStartDate != null) {			
			activityWorkPackage.setPlannedStartDate(DateUtility.dateformatWithOutTime(this.plannedStartDate));
					
		}
		
		if(this.plannedEndDate != null) {
			activityWorkPackage.setPlannedEndDate(DateUtility.dateformatWithOutTime(this.plannedEndDate));
		}
		
		if(this.baselineStartDate != null) {			
			activityWorkPackage.setBaselineStartDate(DateUtility.dateformatWithOutTime(this.baselineStartDate));
					
		}else{
			activityWorkPackage.setBaselineStartDate(activityWorkPackage.getPlannedStartDate());
		}
		
		if(this.baselineEndDate != null) {
			activityWorkPackage.setBaselineEndDate(DateUtility.dateformatWithOutTime(this.baselineEndDate));
		}else{
			activityWorkPackage.setBaselineEndDate(activityWorkPackage.getPlannedEndDate());
		}
	
		if (this.actualStartDate != null) {
			if(this.actualStartDate.equals("mm/dd/yy"))
				activityWorkPackage.setActualStartDate(null);				
		       else if(this.actualStartDate!="mm/dd/yy")
		    	   activityWorkPackage.setActualStartDate(DateUtility.dateformatWithOutTime(this.actualStartDate));
		 }
		
		if(this.actualEndDate != null) {
			activityWorkPackage.setActualEndDate(DateUtility.dateformatWithOutTime(this.actualEndDate));
		}
		
		if(this.createdById!=null){
			UserList user=new UserList();
			user.setUserId(createdById);
			user.setLoginId(createdByName);
			activityWorkPackage.setCreatedBy(user);
		}
		
		if(this.modifiedById!=null){
			UserList user=new UserList();
			user.setUserId(createdById);
			user.setLoginId(createdByName);
			activityWorkPackage.setModifiedBy(user);
		}
		
		if(this.statusId != null){
			WorkflowStatus workflowStatus = new WorkflowStatus();
			workflowStatus.setWorkflowStatusId(this.statusId);
			activityWorkPackage.setWorkflowStatus(workflowStatus);
		}
		
		activityWorkPackage.setTotalEffort(this.totalEffort);

		return activityWorkPackage;
	}

	public String getRemainingHrsMins() {
		return remainingHrsMins;
	}

	public void setRemainingHrsMins(String remainingHrsMins) {
		this.remainingHrsMins = remainingHrsMins;
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

	public Float getPercentageCompletion() {
		return percentageCompletion;
	}

	public void setPercentageCompletion(Float percentageCompletion) {
		this.percentageCompletion = percentageCompletion;
	}

	public String getProductVesionBuildName() {
		return productVesionBuildName;
	}

	public void setProductVesionBuildName(String productVesionBuildName) {
		this.productVesionBuildName = productVesionBuildName;
	}

	public Integer getClarificationCount() {
		return clarificationCount;
	}

	public void setClarificationCount(Integer clarificationCount) {
		this.clarificationCount = clarificationCount;
	}

	public Integer getChangeRequestCount() {
		return changeRequestCount;
	}

	public void setChangeRequestCount(Integer changeRequestCount) {
		this.changeRequestCount = changeRequestCount;
	}

	public Integer getActBeginCount() {
		return actBeginCount;
	}

	public void setActBeginCount(Integer actBeginCount) {
		this.actBeginCount = actBeginCount;
	}

	public Integer getActIntermediateCount() {
		return actIntermediateCount;
	}

	public void setActIntermediateCount(Integer actIntermediateCount) {
		this.actIntermediateCount = actIntermediateCount;
	}

	public Integer getActAbortCount() {
		return actAbortCount;
	}

	public void setActAbortCount(Integer actAbortCount) {
		this.actAbortCount = actAbortCount;
	}

	public Integer getActEndCount() {
		return actEndCount;
	}

	public void setActEndCount(Integer actEndCount) {
		this.actEndCount = actEndCount;
	}

	public boolean isVisibleEventComment() {
		return visibleEventComment;
	}

	public void setVisibleEventComment(boolean visibleEventComment) {
		this.visibleEventComment = visibleEventComment;
	}
	
}