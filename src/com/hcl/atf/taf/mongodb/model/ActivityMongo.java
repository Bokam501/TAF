package com.hcl.atf.taf.mongodb.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.Activity;

@Document(collection = "activities")
public class ActivityMongo {
	private static final Log log = LogFactory.getLog(ActivityMongo.class);
	@Transient
	private SimpleDateFormat dateFormatForMongoDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

	
	@Id
	private String id;	
	private String _class;
	private Integer activityId;	
	private String activityName;	
	private Integer activityWorkPackageId;
	private String activityWorkPackageName;
	private Integer productFeatureId;
	private String productFeatureName;	
	private Integer activityMasterId;
	private String activityMasterName;
    private Integer clarificationTrackerId;
    private String  clarificationTrackerName;    
	private String activityTrackerNumber;
	private Integer drReferenceNumber;
	private Integer categoryId;
	private String categoryName;	
	private Integer assigneeId;
	private String assigneeName;	
	private Integer reviewerId;	
	private String reviewerName;
	private String status;
	private Integer productId;
	private String productName;
	private Integer buildId;
	private String buildname;
	
	
	private Integer versionId;
	private String versionName;	
	private Integer testFactoryId;
	private String testFactoryName;
	private Integer testCentersId;
	private String testCentersName;
	private Integer customerId;
	private String customerName;
	private Integer statusCategoryId;
	private String statusCategoryName;
	
	private Integer priorityId;
	private String priorityName;
	private String remark;
	private Object baselineStartDate;
	private Object baselineEndDate;
	private Object plannedStartDate;
	private Object plannedEndDate;
	private Object actualStartDate;
	private Object actualEndDate;

	private Integer baselineActivitySize;
	private Integer plannedActivitySize;
	private Integer actualActivitySize;
	private Float baselineUnit;
	private Float plannedUnit;
	private Float actualUnit;
	
	private Integer competencyId;
	private String competencyName;
	
	private String createdBy;
	private String modifiedBy;
	
    private Integer workflowStatusId;
    private String workflowStatusName;
    private Integer workflowStatusCategoryId;
    private String workflowStatusCategoryName;
    
    private Integer activityGroupId;
    private String activityGroupName;
    
    
    private Integer baselineEffort;
    private Integer plannedEffort;
    private Integer totalEffort;
    private Integer lifeCycleStageId;
	private String lifeCycleStageName;
	private Float percentageCompletion;
	
	private String parentStatus;
	private Object createdDate;
	private Object modifiedDate;
	
	public ActivityMongo() {
		
	}


	public ActivityMongo(Activity activity) {		
		this.id=activity.getActivityId()+"";
		this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
		this.activityId = activity.getActivityId();
		this.activityName = activity.getActivityName();
		if(activity.getProductFeature()!=null){
			this.productFeatureId = activity.getProductFeature().getProductFeatureId();
			this.productFeatureName = activity.getProductFeature().getProductFeatureName();
		}
		if(activity.getActivityMaster()!=null){
			this.activityMasterId = activity.getActivityMaster().getActivityMasterId();
			this.activityMasterName = activity.getActivityMaster().getActivityMasterName();
			if(activity.getActivityMaster().getActivityType() != null){
				if(activity.getActivityMaster().getActivityType().getActivityGroup() != null){
					this.activityGroupId = activity.getActivityMaster().getActivityType().getActivityGroup().getActivityGroupId();
					this.activityGroupName = activity.getActivityMaster().getActivityType().getActivityGroup().getActivityGroupName();
				}
			}
		}
		this.percentageCompletion=activity.getPercentageCompletion();
		
		if(activity.getClarificationTracker()!=null){
			this.clarificationTrackerId = activity.getClarificationTracker().getClarificationTrackerId();
			this.clarificationTrackerName = activity.getClarificationTracker().getClarificationTitle();
		}
		
		
		this.activityTrackerNumber = activity.getActivityTrackerNumber();
		this.drReferenceNumber = activity.getDrReferenceNumber();
		if(activity.getCategory()!=null){
			this.categoryName = activity.getCategory().getName();
		}
			
		if(activity.getAssignee()!=null){
			this.assigneeId = activity.getAssignee().getUserId();
			this.assigneeName = activity.getAssignee().getUserDisplayName();
		}
		if(activity.getReviewer()!=null){
			this.reviewerId = activity.getReviewer().getUserId();
			this.reviewerName = activity.getReviewer().getUserDisplayName();
		}
		if(activity.getIsActive()==1){
			this.status="Active";
		}else{
			this.status="InActive";
		}
		if(activity.getActivityWorkPackage()!=null){
			if(activity.getActivityWorkPackage().getProductBuild()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
			this.productId = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
			this.productName = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
			}
			if(activity.getActivityWorkPackage().getProductBuild()!=null){
				this.buildId = activity.getActivityWorkPackage().getProductBuild().getProductBuildId();
				this.buildname = activity.getActivityWorkPackage().getProductBuild().getBuildname();
			}
			if(activity.getActivityWorkPackage().getProductBuild()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion()!=null){
			this.versionId = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
			this.versionName = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
			}
			if(activity.getActivityWorkPackage().getProductBuild()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory()!=null){
			this.testFactoryId = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
			this.testFactoryName = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryName();
			this.testCentersId=activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
			this.testCentersName=activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
			
			
			}
			if(activity.getActivityWorkPackage().getProductBuild()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer()!=null){
			this.customerId = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerId();
			this.customerName = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName();
			}
		}
		
		if(activity.getStatusCategory() != null){
			this.statusCategoryId = activity.getStatusCategory().getStatusCategoryId();
			this.statusCategoryName = activity.getStatusCategory().getStatusCategoryName();
		}
		
		if(activity.getPriority()!=null){
			this.priorityId = activity.getPriority().getExecutionPriorityId();
			this.priorityName = activity.getPriority().getExecutionPriorityName();
		}
		
		this.remark = activity.getRemark();
		this.baselineStartDate = activity.getBaselineStartDate();
		this.baselineEndDate = activity.getBaselineEndDate();
		this.plannedStartDate = activity.getPlannedStartDate();
		this.plannedEndDate = activity.getPlannedEndDate();
		this.actualStartDate = activity.getActualStartDate();
		this.actualEndDate = activity.getActualEndDate();
		
		this.baselineActivitySize = activity.getBaselineActivitySize();
		this.plannedActivitySize = activity.getPlannedActivitySize();
		this.actualActivitySize = activity.getActualActivitySize();
		this.baselineUnit = activity.getBaselineUnit();
		this.plannedUnit = activity.getPlannedUnit();
		this.actualUnit = activity.getActualUnit();
		this.baselineEffort = activity.getBaselineEffort();
		this.plannedEffort = activity.getPlannedEffort();
		this.totalEffort = activity.getTotalEffort();
		
		if(activity.getWorkflowStatus()!=null){
			this.workflowStatusId=activity.getWorkflowStatus().getWorkflowStatusId();
			this.workflowStatusName=activity.getWorkflowStatus().getWorkflowStatusName();	
			if(activity.getWorkflowStatus().getWorkflowStatusCategory()!=null){
				this.workflowStatusCategoryName = activity.getWorkflowStatus().getWorkflowStatusCategory().getWorkflowStatusCategoryName();
				this.workflowStatusCategoryId =  activity.getWorkflowStatus().getWorkflowStatusCategory().getWorkflowStatusCategoryId();
			}
		}
	
		
		this.totalEffort=activity.getTotalEffort();
		this.plannedEffort=activity.getPlannedEffort();
		
		if(activity.getCreatedBy()!=null){
			this.createdBy = activity.getCreatedBy().getLoginId();
		}
		
		if(activity.getModifiedBy()!=null){
			this.modifiedBy = activity.getModifiedBy().getLoginId();
		}
		
		if(activity.getActivityWorkPackage()!=null){
			this.activityWorkPackageId = activity.getActivityWorkPackage().getActivityWorkPackageId();
			this.activityWorkPackageName = activity.getActivityWorkPackage().getActivityWorkPackageName();
			
			if(activity.getActivityWorkPackage().getIsActive() == 1){
				this.parentStatus = "Active";
			}else{
				this.parentStatus = "InActive";
			}
			
			
			if(activity.getActivityWorkPackage().getCompetency() != null && activity.getActivityWorkPackage().getCompetency().getDimensionId() > 1){
				this.competencyId = activity.getActivityWorkPackage().getCompetency().getDimensionId();
				this.competencyName = activity.getActivityWorkPackage().getCompetency().getName();
			}
		}
		
		if(activity.getLifeCycleStage() != null){
		this.lifeCycleStageId = activity.getLifeCycleStage().getWorkflowStatusId();
		this.lifeCycleStageName = activity.getLifeCycleStage().getWorkflowStatusName();
		}
		
		this.createdDate = activity.getCreatedDate();
		this.modifiedDate = activity.getModifiedDate();
		
	}


	

	public Integer getActivityId() {
		return activityId;
	}


	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}


	public String getActivityName() {
		return activityName;
	}


	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}


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


	public Integer getProductFeatureId() {
		return productFeatureId;
	}


	public void setProductFeatureId(Integer productFeatureId) {
		this.productFeatureId = productFeatureId;
	}


	public String getProductFeatureName() {
		return productFeatureName;
	}


	public void setProductFeatureName(String productFeatureName) {
		this.productFeatureName = productFeatureName;
	}


	public Integer getActivityMasterId() {
		return activityMasterId;
	}


	public void setActivityMasterId(Integer activityMasterId) {
		this.activityMasterId = activityMasterId;
	}


	public String getActivityMasterName() {
		return activityMasterName;
	}


	public void setActivityMasterName(String activityMasterName) {
		this.activityMasterName = activityMasterName;
	}


	public Integer getClarificationTrackerId() {
		return clarificationTrackerId;
	}


	public void setClarificationTrackerId(Integer clarificationTrackerId) {
		this.clarificationTrackerId = clarificationTrackerId;
	}


	public String getClarificationTrackerName() {
		return clarificationTrackerName;
	}


	public void setClarificationTrackerName(String clarificationTrackerName) {
		this.clarificationTrackerName = clarificationTrackerName;
	}


	public String getActivityTrackerNumber() {
		return activityTrackerNumber;
	}


	public void setActivityTrackerNumber(String activityTrackerNumber) {
		this.activityTrackerNumber = activityTrackerNumber;
	}


	public Integer getDrReferenceNumber() {
		return drReferenceNumber;
	}


	public void setDrReferenceNumber(Integer drReferenceNumber) {
		this.drReferenceNumber = drReferenceNumber;
	}


	public Integer getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public Integer getAssigneeId() {
		return assigneeId;
	}


	public void setAssigneeId(Integer assigneeId) {
		this.assigneeId = assigneeId;
	}


	public String getAssigneeName() {
		return assigneeName;
	}


	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}


	public Integer getReviewerId() {
		return reviewerId;
	}


	public void setReviewerId(Integer reviewerId) {
		this.reviewerId = reviewerId;
	}


	public String getReviewerName() {
		return reviewerName;
	}


	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
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


	public Integer getBuildId() {
		return buildId;
	}


	public void setBuildId(Integer buildId) {
		this.buildId = buildId;
	}


	public String getBuildname() {
		return buildname;
	}


	public void setBuildname(String buildname) {
		this.buildname = buildname;
	}


	public Integer getVersionId() {
		return versionId;
	}


	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}


	public String getVersionName() {
		return versionName;
	}


	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}


	public Integer getTestFactoryId() {
		return testFactoryId;
	}


	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}


	public String getTestFactoryName() {
		return testFactoryName;
	}


	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}


	public Integer getCustomerId() {
		return customerId;
	}


	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Object getBaselineStartDate() {
		return baselineStartDate;
	}


	public void setBaselineStartDate(Object baselineStartDate) {

		String formatCheck = baselineStartDate.toString();
		if(formatCheck.contains("date=")){
			try {
				baselineStartDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.baselineStartDate = baselineStartDate;
	
	}


	public Object getBaselineEndDate() {
		return baselineEndDate;
	}


	public void setBaselineEndDate(Object baselineEndDate) {

		String formatCheck = baselineEndDate.toString();
		if(formatCheck.contains("date=")){
			try {
				baselineEndDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.baselineEndDate = baselineEndDate;
	}

	public Object getPlannedStartDate() {
		return plannedStartDate;
	}


	public void setPlannedStartDate(Object plannedStartDate) {

		String formatCheck = plannedStartDate.toString();
		if(formatCheck.contains("date=")){
			try {
				plannedStartDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.plannedStartDate = plannedStartDate;
	
	}


	public Object getPlannedEndDate() {
		return plannedEndDate;
	}


	public void setPlannedEndDate(Object plannedEndDate) {

		String formatCheck = plannedEndDate.toString();
		if(formatCheck.contains("date=")){
			try {
				plannedEndDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.plannedEndDate = plannedEndDate;
	}

	public Object getActualStartDate() {
		return actualStartDate;
	}


	public void setActualStartDate(Object actualStartDate) {

		String formatCheck = actualStartDate.toString();
		if(formatCheck.contains("date=")){
			try {
				actualStartDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.actualStartDate = actualStartDate;
	}


	public Object getActualEndDate() {
		return actualEndDate;
	}


	public void setActualEndDate(Object actualEndDate) {
		String formatCheck = actualEndDate.toString();
		if(formatCheck.contains("date=")){
			try {
				actualEndDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.actualEndDate = actualEndDate;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getModifiedBy() {
		return modifiedBy;
	}


	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	public Object getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Object createdDate) {
		String formatCheck = createdDate.toString();
		if(formatCheck.contains("date=")){
			try {
				createdDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.createdDate = createdDate;
	}


	public Object getModifiedDate() {
		return modifiedDate;
	}


	public void setModifiedDate(Object modifiedDate) {
		String formatCheck = modifiedDate.toString();
		if(formatCheck.contains("date=")){
			try {
				modifiedDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.modifiedDate = modifiedDate;
	}


	public Integer getPlannedActivitySize() {
		return plannedActivitySize;
	}


	public void setPlannedActivitySize(Integer plannedActivitySize) {
		this.plannedActivitySize = plannedActivitySize;
	}


	public Integer getActualActivitySize() {
		return actualActivitySize;
	}


	public void setActualActivitySize(Integer actualActivitySize) {
		this.actualActivitySize = actualActivitySize;
	}


	public Float getPlannedUnit() {
		return plannedUnit;
	}


	public void setPlannedUnit(Float plannedUnit) {
		this.plannedUnit = plannedUnit;
	}


	public Float getActualUnit() {
		return actualUnit;
	}


	public void setActualUnit(Float actualUnit) {
		this.actualUnit = actualUnit;
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


	public String getId() {
		return id;
	}


	public void setId(String _id) {
		this.id = _id;
	}


	public String get_class() {
		return _class;
	}


	public void set_class(String _class) {
		this._class = _class;
	}


	public SimpleDateFormat getDateFormatForMongoDB() {
		return dateFormatForMongoDB;
	}


	public void setDateFormatForMongoDB(SimpleDateFormat dateFormatForMongoDB) {
		this.dateFormatForMongoDB = dateFormatForMongoDB;
	}




	Integer getTestCentersId() {
		return testCentersId;
	}


	void setTestCentersId(Integer testCentersId) {
		this.testCentersId = testCentersId;
	}


	String getTestCentersName() {
		return testCentersName;
	}


	void setTestCentersName(String testCentersName) {
		this.testCentersName = testCentersName;
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


	public Integer getTotalEffort() {
		return totalEffort;
	}


	public void setTotalEffort(Integer totalEffort) {
		this.totalEffort = totalEffort;
	}
	
	public Integer getLifeCycleStageId() {
		return lifeCycleStageId;
	}


	public void setLifeCycleStageId(Integer lifeCycleStageId) {
		this.lifeCycleStageId = lifeCycleStageId;
	}


	public String getLifeCycleStageName() {
		return lifeCycleStageName;
	}


	public void setLifeCycleStageName(String lifeCycleStageName) {
		this.lifeCycleStageName = lifeCycleStageName;
	}


	public Float getPercentageCompletion() {
		return percentageCompletion;
	}


	public void setPercentageCompletion(Float percentageCompletion) {
		this.percentageCompletion = percentageCompletion;
	}


	public String getParentStatus() {
		return parentStatus;
	}


	public void setParentStatus(String parentStatus) {
		this.parentStatus = parentStatus;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getBaselineActivitySize() {
		return baselineActivitySize;
	}


	public void setBaselineActivitySize(Integer baselineActivitySize) {
		this.baselineActivitySize = baselineActivitySize;
	}

	public Float getBaselineUnit() {
		return baselineUnit;
	}


	public void setBaselineUnit(Float baselineUnit) {
		this.baselineUnit = baselineUnit;
	}


	public Integer getPlannedEffort() {
		return plannedEffort;
	}


	public void setPlannedEffort(Integer plannedEffort) {
		this.plannedEffort = plannedEffort;
	}


	public Integer getBaselineEffort() {
		return baselineEffort;
	}


	public void setBaselineEffort(Integer baselineEffort) {
		this.baselineEffort = baselineEffort;
	}


	public Integer getWorkflowStatusCategoryId() {
		return workflowStatusCategoryId;
	}


	public void setWorkflowStatusCategoryId(Integer workflowStatusCategoryId) {
		this.workflowStatusCategoryId = workflowStatusCategoryId;
	}


	public String getWorkflowStatusCategoryName() {
		return workflowStatusCategoryName;
	}


	public void setWorkflowStatusCategoryName(String workflowStatusCategoryName) {
		this.workflowStatusCategoryName = workflowStatusCategoryName;
	}


	public Integer getActivityGroupId() {
		return activityGroupId;
	}


	public void setActivityGroupId(Integer activityGroupId) {
		this.activityGroupId = activityGroupId;
	}


	public String getActivityGroupName() {
		return activityGroupName;
	}


	public void setActivityGroupName(String activityGroupName) {
		this.activityGroupName = activityGroupName;
	}

	
	
	
}
