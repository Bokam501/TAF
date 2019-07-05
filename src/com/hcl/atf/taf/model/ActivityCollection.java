package com.hcl.atf.taf.model;


import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hcl.atf.taf.mongodb.model.ActivityCollectionMongo;

@Entity
@Table(name = "activity_collection")
public class ActivityCollection implements Serializable {

	private static final long serialVersionUID = 194716896982515968L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "_id", unique = true, nullable = false)
	private Integer _id;
	
	@Column(name = "_class")
	private String _class;
	
	@Column(name = "testCentersName")
	private String testCentersName;
	
	@Column(name = "testCentersId")
	private Integer testCentersId;
	
	@Column(name = "testFactoryName")
	private String testFactoryName;
	
	@Column(name = "testFactoryId")
	private Integer testFactoryId;
	
	@Column(name = "customerName")
	private String customerName;
	
	@Column(name = "customerId")
	private Integer customerId;
	
	@Column(name = "programName")
	private String productName;
	
	@Column(name = "programId")
	private Integer productId;
	
	@Column(name = "programManager")
	private String productManager;
	
	@Column(name = "project")
	private String project;
	
	@Column(name = "projectId")
	private Integer projectId;
	
	@Column(name = "projectManager")
	private String projectManager;
	
	@Column(name = "competency")
	private String competency;
	
	@Column(name = "competencyId")
	private Integer competencyId;
	
	@Column(name = "competecnyManager")
	private String competencyManager;
	
	@Column(name = "versionName")
	private String versionName;
	
	@Column(name = "versionId")
	private Integer versionId;
	
	@Column(name = "buildName")
	private String buildName;
	
	@Column(name = "buildId")
	private Integer buildId;
	
	@Column(name = "weekDate")
	private Date weekDate;
	
	@Column(name = "createdDate")
	private Date createdDate;
	
	@Column(name = "updateDate")
	private Date updateDate;
	
	@Column(name = "phase")
	private String phase;
	
	@Column(name = "workpackageName")
	private String workpackageName;
	
	@Column(name = "activityBatchNo")
	private String activityBatchNo;
	
	@Column(name = "activityType")
	private String activityType;
	
	@Column(name = "activityCategory")
	private String activityCategory;
	
	@Column(name = "activityName")
	private String activityName;
	
	@Column(name = "activitySizeActual")
	private Float activitySizeActual;
	
	@Column(name = "activitySizePlanned")
	private Float activitySizePlanned;
	
	@Column(name = "activityReference")
	private String activityReference;
	
	@Column(name = "activityTracker")
	private String activityTracker;
	
	@Column(name = "activityEnvironmentPrimary")
	private String activityEnvironmentPrimary;
	
	@Column(name = "activityEnvironmentSecondary")
	private String activityEnvironmentSecondary;
	
	@Column(name = "activityEnvironmentTertiary")
	private String activityEnvironmentTertiary;
	
	@Column(name = "weightageType")
	private String weightageType;
	
	@Column(name = "weightageUnit")
	private Float weightageUnit;
	
	@Column(name = "workUnitPlanned")
	private Float workUnitPlanned;
	
	@Column(name = "workUnitActual")
	private Float workUnitActual;
	
	@Column(name = "activitySeverity")
	private String activitySeverity;
	
	@Column(name = "activityResolution")
	private String activityResolution;
	
	@Column(name = "activityStatus")
	private String activityStatus;
	
	@Column(name = "activitySource")
	private String activitySource;
	
	@Column(name = "activityComponent")
	private String activityComponent;
	
	@Column(name = "activityStage")
	private String activityStage;
	
	@Column(name = "actualActivityStartDate")
	private Date actualActivityStartDate;
	
	@Column(name = "actualActivityEndDate")
	private Date actualActivityEndDate;
	
	@Column(name = "plannedActivityStartDate")
	private Date plannedActivityStartDate;
	
	@Column(name = "plannedActivityEndDate")
	private Date plannedActivityEndDate;
	
	@Column(name = "revisedActivityStartDate")
	private Date revisedActivityStartDate;
	
	@Column(name = "revisedActivityEndDate")
	private Date revisedActivityEndDate;
	
	@Column(name = "actualActivityEffort")
	private Float actualActivityEffort;
	
	@Column(name = "plannedActivityEffort")
	private Float plannedActivityEffort;
	
	@Column(name = "revisedActivityEffort")
	private Float revisedActivityEffort;
	
	@Column(name = "activityTag")
	private String activityTag;
	
	@Column(name = "cumulativeActivityActual")
	private Float cumulativeActivityActual;
	
	@Column(name = "cumulativeActivityPlanned")
	private Float cumulativeActivityPlanned;
	
	@Column(name = "activityRaisedBy")
	private String activityRaisedBy;
	
	@Column(name = "activityOwner")
	private String activityOwner;
	
	@Column(name = "activityAssignedTo")
	private String activityAssignedTo;
	
	@Column(name = "activityReviewer1")
	private String activityReviewer1;
	
	@Column(name = "activityReviewer2")
	private String activityReviewer2;
	
	@Column(name = "activityReviewer3")
	private String activityReviewer3;
	
	@Column(name = "activityReviewer4")
	private String activityReviewer4;
	
	@Column(name = "activityReviewer5")
	private String activityReviewer5;
	
	@Column(name = "activityParent")
	private String activityParent;
	
	@Column(name = "activityExecutionEffort")
	private Float activityExecutionEffort;
	
	@Column(name = "activityReviewEffort1")
	private Float activityReviewEffort1;
	
	@Column(name = "activityReviewEffort2")
	private Float activityReviewEffort2;	
	
	@Column(name = "activityReviewEffort3")
	private Float activityReviewEffort3;
	
	@Column(name = "activityReviewEffort4")
	private Float activityReviewEffort4;
	
	@Column(name = "activityReviewEffort5")
	private Float activityReviewEffort5;
	
	@Column(name = "activityReworkEffort")
	private Float activityReworkEffort;
	
	@Column(name = "l0ResourceCount")
	private Float l0ResourceCount;
	
	@Column(name = "l1ResourceCount")
	private Float l1ResourceCount;
	
	@Column(name = "l2ResourceCount")
	private Float l2ResourceCount;
	
	@Column(name = "l3ResourceCount")
	private Float l3ResourceCount;
	
	@Column(name = "l4ResourceCount")
	private Float l4ResourceCount;
	
	@Column(name = "l5ResourceCount")
	private Float l5ResourceCount;
	
	@Column(name = "l6ResourceCount")
	private Float l6ResourceCount;
	
	@Column(name = "l7ResourceCount")
	private Float l7ResourceCount;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "eVa")
	private Float eVa;
	
	@Column(name = "eVb")
	private Float eVb;
	
	@Column(name = "sVa")
	private Float sVa;
	
	@Column(name = "sVb")
	private Float sVb;
	
	@Column(name = "recordType")
	private String type;

	public ActivityCollection(){
		//this._id = "";
		this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
		this.testCentersName = "";
		this.testCentersId = 0;
		this.testFactoryName = "";
		this.testFactoryId = 0;
		this.customerName = "";
		this.customerId = 0;
		this.productName = "";
		this.productId = 0;
		this.productManager = "";
		this.project = "";
		this.projectId = 0;
		this.projectManager = "";
		this.competency = "";
		this.competencyId = 0;
		this.competencyManager = "";
		this.versionName = "";
		this.versionId = 0;
		this.buildName = "";
		this.buildId = 0;
		this.weekDate = null;
		this.createdDate = null;
		this.updateDate = null;
		this.phase = "";
		this.workpackageName = "";
		this.activityBatchNo = "";
		this.activityType = "";
		this.activityCategory = "";
		this.activityName = "";
		this.activitySizeActual = 0F;
		this.activitySizePlanned = 0F;
		this.activityReference = "";
		this.activityTracker = "";
		this.activityEnvironmentPrimary = "";
		this.activityEnvironmentSecondary = "";
		this.activityEnvironmentTertiary = "";
		this.weightageType = "";
		this.weightageUnit = 0F;
		this.workUnitPlanned = 0F;
		this.workUnitActual = 0F;
		this.activitySeverity = "";
		this.activityResolution = "";
		this.activityStatus = "";
		this.activitySource = "";
		this.activityComponent = "";
		this.activityStage = "";
		this.actualActivityStartDate = null;
		this.actualActivityEndDate = null;
		this.plannedActivityStartDate = null;
		this.plannedActivityEndDate = null;
		this.revisedActivityStartDate = null;
		this.revisedActivityEndDate = null;
		this.actualActivityEffort = 0F;
		this.plannedActivityEffort = 0F;
		this.revisedActivityEffort = 0F;
		this.activityTag = "";
		this.cumulativeActivityActual = 0F;
		this.cumulativeActivityPlanned = 0F;
		this.activityRaisedBy = "";
		this.activityOwner = "";
		this.activityAssignedTo = "";
		this.activityReviewer1 = "";
		this.activityReviewer2 = "";
		this.activityReviewer3 = "";
		this.activityReviewer4 = "";
		this.activityReviewer5 = "";
		this.activityParent = "";
		this.activityExecutionEffort = 0F;
		this.activityReviewEffort1 = 0F;
		this.activityReviewEffort2 = 0F;
		this.activityReviewEffort3 = 0F;
		this.activityReviewEffort4 = 0F;
		this.activityReviewEffort5 = 0F;
		this.activityReworkEffort = 0F;
		this.l0ResourceCount = 0F;
		this.l1ResourceCount = 0F;
		this.l2ResourceCount = 0F;
		this.l3ResourceCount = 0F;
		this.l4ResourceCount = 0F;
		this.l5ResourceCount = 0F;
		this.l6ResourceCount = 0F;
		this.l7ResourceCount = 0F;
		this.remarks = "";
		this.eVa = 0F;
		this.eVb = 0F;
		this.sVa = 0F;
		this.sVb = 0F;
		this.type = "";
	}

	public ActivityCollection(ActivityCollectionMongo activityCollectionMongo){
		this._id = activityCollectionMongo.getId();
		this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
		this.testCentersName = activityCollectionMongo.getTestCentersName();
		this.testCentersId = activityCollectionMongo.getTestCentersId();
		this.testFactoryName = activityCollectionMongo.getTestFactoryName();
		this.testFactoryId = activityCollectionMongo.getTestFactoryId();
		this.customerName = activityCollectionMongo.getCustomerName();
		this.customerId = activityCollectionMongo.getCustomerId();
		this.productName = activityCollectionMongo.getProductName();
		this.productId = activityCollectionMongo.getProductId();
		this.productManager = activityCollectionMongo.getProductManager();
		this.project = activityCollectionMongo.getProject();
		this.projectId = activityCollectionMongo.getProjectId();
		this.projectManager = activityCollectionMongo.getProjectManager();
		this.competency = activityCollectionMongo.getCompetency();
		this.competencyId = activityCollectionMongo.getCompetencyId();
		this.competencyManager = activityCollectionMongo.getCompetencyManager();
		this.versionName = activityCollectionMongo.getVersionName();
		this.versionId = activityCollectionMongo.getVersionId();
		this.buildName = activityCollectionMongo.getBuildName();
		this.buildId = activityCollectionMongo.getBuildId();
		if(activityCollectionMongo.getWeekDate() != null){
			this.weekDate = (Date) activityCollectionMongo.getWeekDate();
		}
		if(activityCollectionMongo.getCreatedDate() != null){
			this.createdDate = (Date) activityCollectionMongo.getCreatedDate();
		}
		if(activityCollectionMongo.getUpdateDate() != null){
			this.updateDate = (Date) activityCollectionMongo.getUpdateDate();
		}
		this.phase = activityCollectionMongo.getPhase();
		this.workpackageName = activityCollectionMongo.getWorkpackageName();
		this.activityBatchNo = activityCollectionMongo.getActivityBatchNo();
		this.activityType = activityCollectionMongo.getActivityType();
		this.activityCategory = activityCollectionMongo.getActivityCategory();
		this.activityName = activityCollectionMongo.getActivityName();
		this.activitySizeActual = activityCollectionMongo.getActivitySizeActual();
		this.activitySizePlanned = activityCollectionMongo.getActivitySizePlanned();
		this.activityReference = activityCollectionMongo.getActivityReference();
		this.activityTracker = activityCollectionMongo.getActivityTracker();
		this.activityEnvironmentPrimary = activityCollectionMongo.getActivityEnvironmentPrimary();
		this.activityEnvironmentSecondary = activityCollectionMongo.getActivityEnvironmentSecondary();
		this.activityEnvironmentTertiary = activityCollectionMongo.getActivityEnvironmentTertiary();
		this.weightageType = activityCollectionMongo.getWeightageType();
		this.weightageUnit = activityCollectionMongo.getWeightageUnit();
		this.workUnitPlanned = activityCollectionMongo.getWorkUnitPlanned();
		this.workUnitActual = activityCollectionMongo.getWorkUnitActual();
		this.activitySeverity = activityCollectionMongo.getActivitySeverity();
		this.activityResolution = activityCollectionMongo.getActivityResolution();
		this.activityStatus = activityCollectionMongo.getActivityStatus();
		this.activitySource = activityCollectionMongo.getActivitySource();
		this.activityComponent = activityCollectionMongo.getActivityComponent();
		this.activityStage = activityCollectionMongo.getActivityStage();
		if(activityCollectionMongo.getUpdateDate() != null){
			this.updateDate = (Date) activityCollectionMongo.getUpdateDate();
		}
		if(activityCollectionMongo.getActualActivityStartDate() != null){
			this.actualActivityStartDate = (Date) activityCollectionMongo.getActualActivityStartDate();
		}
		if(activityCollectionMongo.getActualActivityEndDate() != null){
			this.actualActivityEndDate = (Date) activityCollectionMongo.getActualActivityEndDate();
		}
		if(activityCollectionMongo.getPlannedActivityStartDate() != null){
			this.plannedActivityStartDate = (Date) activityCollectionMongo.getPlannedActivityStartDate();
		}
		if(activityCollectionMongo.getPlannedActivityEndDate() != null){
			this.plannedActivityEndDate = (Date) activityCollectionMongo.getPlannedActivityEndDate();
		}
		if(activityCollectionMongo.getRevisedActivityStartDate() != null){
			this.revisedActivityStartDate = (Date) activityCollectionMongo.getRevisedActivityStartDate();
		}
		if(activityCollectionMongo.getRevisedActivityEndDate() != null){
			this.revisedActivityEndDate = (Date) activityCollectionMongo.getRevisedActivityEndDate();
		}
		this.actualActivityEffort = activityCollectionMongo.getActualActivityEffort();
		this.plannedActivityEffort = activityCollectionMongo.getPlannedActivityEffort();
		this.revisedActivityEffort = activityCollectionMongo.getRevisedActivityEffort();
		this.activityTag = activityCollectionMongo.getActivityTag();
		this.cumulativeActivityActual = activityCollectionMongo.getCumulativeActivityActual();
		this.cumulativeActivityPlanned = activityCollectionMongo.getCumulativeActivityPlanned();
		this.activityRaisedBy = activityCollectionMongo.getActivityRaisedBy();
		this.activityOwner = activityCollectionMongo.getActivityOwner();
		this.activityAssignedTo = activityCollectionMongo.getActivityAssignedTo();
		this.activityReviewer1 = activityCollectionMongo.getActivityReviewer1();		
		this.activityReviewer2 = activityCollectionMongo.getActivityReviewer2();		
		this.activityReviewer3 = activityCollectionMongo.getActivityReviewer3();
		this.activityReviewer4 = activityCollectionMongo.getActivityReviewer4();
		this.activityReviewer5 = activityCollectionMongo.getActivityReviewer5();	
		this.activityParent = activityCollectionMongo.getActivityParent();
		this.activityExecutionEffort = activityCollectionMongo.getActivityExecutionEffort();
		this.activityReviewEffort1 = activityCollectionMongo.getActivityReviewEffort1();		
		this.activityReviewEffort2 = activityCollectionMongo.getActivityReviewEffort2();
		this.activityReviewEffort3 = activityCollectionMongo.getActivityReviewEffort3();
		this.activityReviewEffort4 = activityCollectionMongo.getActivityReviewEffort4();
		this.activityReviewEffort5 = activityCollectionMongo.getActivityReviewEffort5();
		this.activityReworkEffort = activityCollectionMongo.getActivityReworkEffort();
		this.l0ResourceCount = activityCollectionMongo.getL0ResourceCount();
		this.l1ResourceCount = activityCollectionMongo.getL1ResourceCount();
		this.l2ResourceCount = activityCollectionMongo.getL2ResourceCount();
		this.l3ResourceCount = activityCollectionMongo.getL3ResourceCount();
		this.l4ResourceCount = activityCollectionMongo.getL4ResourceCount();
		this.l5ResourceCount = activityCollectionMongo.getL5ResourceCount();
		this.l6ResourceCount = activityCollectionMongo.getL6ResourceCount();
		this.l7ResourceCount = activityCollectionMongo.getL7ResourceCount();
		this.remarks = activityCollectionMongo.getRemarks();
		this.eVa = activityCollectionMongo.geteVa();
		this.eVb = activityCollectionMongo.geteVb();
		this.sVa = activityCollectionMongo.getsVa();
		this.sVb = activityCollectionMongo.getsVb();
		this.type = activityCollectionMongo.getType();
	}
	
	public Integer get_id() {
		return _id;
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}

	public String get_class() {
		return _class;
	}

	public void set_class(String _class) {
		this._class = _class;
	}

	public String getTestCentersName() {
		return testCentersName;
	}

	public void setTestCentersName(String testCentersName) {
		this.testCentersName = testCentersName;
	}

	public Integer getTestCentersId() {
		return testCentersId;
	}

	public void setTestCentersId(Integer testCentersId) {
		this.testCentersId = testCentersId;
	}

	public String getTestFactoryName() {
		return testFactoryName;
	}

	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}

	public Integer getTestFactoryId() {
		return testFactoryId;
	}

	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductManager() {
		return productManager;
	}

	public void setProductManager(String productManager) {
		this.productManager = productManager;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	public String getCompetency() {
		return competency;
	}

	public void setCompetency(String competency) {
		this.competency = competency;
	}

	public Integer getCompetencyId() {
		return competencyId;
	}

	public void setCompetencyId(Integer competencyId) {
		this.competencyId = competencyId;
	}

	public String getCompetencyManager() {
		return competencyManager;
	}

	public void setCompetencyManager(String competencyManager) {
		this.competencyManager = competencyManager;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

	public Integer getBuildId() {
		return buildId;
	}

	public void setBuildId(Integer buildId) {
		this.buildId = buildId;
	}

	public Date getWeekDate() {
		return weekDate;
	}

	public void setWeekDate(Date weekDate) {
		this.weekDate = weekDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getWorkpackageName() {
		return workpackageName;
	}

	public void setWorkpackageName(String workpackageName) {
		this.workpackageName = workpackageName;
	}

	public String getActivityBatchNo() {
		return activityBatchNo;
	}

	public void setActivityBatchNo(String activityBatchNo) {
		this.activityBatchNo = activityBatchNo;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityCategory() {
		return activityCategory;
	}

	public void setActivityCategory(String activityCategory) {
		this.activityCategory = activityCategory;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Float getActivitySizeActual() {
		return activitySizeActual;
	}

	public void setActivitySizeActual(Float activitySizeActual) {
		this.activitySizeActual = activitySizeActual;
	}

	public Float getActivitySizePlanned() {
		return activitySizePlanned;
	}

	public void setActivitySizePlanned(Float activitySizePlanned) {
		this.activitySizePlanned = activitySizePlanned;
	}

	public String getActivityReference() {
		return activityReference;
	}

	public void setActivityReference(String activityReference) {
		this.activityReference = activityReference;
	}

	public String getActivityTracker() {
		return activityTracker;
	}

	public void setActivityTracker(String activityTracker) {
		this.activityTracker = activityTracker;
	}

	public String getActivityEnvironmentPrimary() {
		return activityEnvironmentPrimary;
	}

	public void setActivityEnvironmentPrimary(String activityEnvironmentPrimary) {
		this.activityEnvironmentPrimary = activityEnvironmentPrimary;
	}

	public String getActivityEnvironmentSecondary() {
		return activityEnvironmentSecondary;
	}

	public void setActivityEnvironmentSecondary(String activityEnvironmentSecondary) {
		this.activityEnvironmentSecondary = activityEnvironmentSecondary;
	}

	public String getActivityEnvironmentTertiary() {
		return activityEnvironmentTertiary;
	}

	public void setActivityEnvironmentTertiary(String activityEnvironmentTertiary) {
		this.activityEnvironmentTertiary = activityEnvironmentTertiary;
	}

	public String getWeightageType() {
		return weightageType;
	}

	public void setWeightageType(String weightageType) {
		this.weightageType = weightageType;
	}

	public Float getWeightageUnit() {
		return weightageUnit;
	}

	public void setWeightageUnit(Float weightageUnit) {
		this.weightageUnit = weightageUnit;
	}

	public Float getWorkUnitPlanned() {
		return workUnitPlanned;
	}

	public void setWorkUnitPlanned(Float workUnitPlanned) {
		this.workUnitPlanned = workUnitPlanned;
	}

	public Float getWorkUnitActual() {
		return workUnitActual;
	}

	public void setWorkUnitActual(Float workUnitActual) {
		this.workUnitActual = workUnitActual;
	}

	public String getActivitySeverity() {
		return activitySeverity;
	}

	public void setActivitySeverity(String activitySeverity) {
		this.activitySeverity = activitySeverity;
	}

	public String getActivityResolution() {
		return activityResolution;
	}

	public void setActivityResolution(String activityResolution) {
		this.activityResolution = activityResolution;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getActivitySource() {
		return activitySource;
	}

	public void setActivitySource(String activitySource) {
		this.activitySource = activitySource;
	}

	public String getActivityComponent() {
		return activityComponent;
	}

	public void setActivityComponent(String activityComponent) {
		this.activityComponent = activityComponent;
	}

	public String getActivityStage() {
		return activityStage;
	}

	public void setActivityStage(String activityStage) {
		this.activityStage = activityStage;
	}

	public Date getActualActivityStartDate() {
		return actualActivityStartDate;
	}

	public void setActualActivityStartDate(Date actualActivityStartDate) {
		this.actualActivityStartDate = actualActivityStartDate;
	}

	public Date getActualActivityEndDate() {
		return actualActivityEndDate;
	}

	public void setActualActivityEndDate(Date actualActivityEndDate) {
		this.actualActivityEndDate = actualActivityEndDate;
	}

	public Date getPlannedActivityStartDate() {
		return plannedActivityStartDate;
	}

	public void setPlannedActivityStartDate(Date plannedActivityStartDate) {
		this.plannedActivityStartDate = plannedActivityStartDate;
	}

	public Date getPlannedActivityEndDate() {
		return plannedActivityEndDate;
	}

	public void setPlannedActivityEndDate(Date plannedActivityEndDate) {
		this.plannedActivityEndDate = plannedActivityEndDate;
	}

	public Date getRevisedActivityStartDate() {
		return revisedActivityStartDate;
	}

	public void setRevisedActivityStartDate(Date revisedActivityStartDate) {
		this.revisedActivityStartDate = revisedActivityStartDate;
	}

	public Date getRevisedActivityEndDate() {
		return revisedActivityEndDate;
	}

	public void setRevisedActivityEndDate(Date revisedActivityEndDate) {
		this.revisedActivityEndDate = revisedActivityEndDate;
	}

	public Float getActualActivityEffort() {
		return actualActivityEffort;
	}

	public void setActualActivityEffort(Float actualActivityEffort) {
		this.actualActivityEffort = actualActivityEffort;
	}

	public Float getPlannedActivityEffort() {
		return plannedActivityEffort;
	}

	public void setPlannedActivityEffort(Float plannedActivityEffort) {
		this.plannedActivityEffort = plannedActivityEffort;
	}

	public Float getRevisedActivityEffort() {
		return revisedActivityEffort;
	}

	public void setRevisedActivityEffort(Float revisedActivityEffort) {
		this.revisedActivityEffort = revisedActivityEffort;
	}

	public String getActivityTag() {
		return activityTag;
	}

	public void setActivityTag(String activityTag) {
		this.activityTag = activityTag;
	}

	public Float getCumulativeActivityActual() {
		return cumulativeActivityActual;
	}

	public void setCumulativeActivityActual(Float cumulativeActivityActual) {
		this.cumulativeActivityActual = cumulativeActivityActual;
	}

	public Float getCumulativeActivityPlanned() {
		return cumulativeActivityPlanned;
	}

	public void setCumulativeActivityPlanned(Float cumulativeActivityPlanned) {
		this.cumulativeActivityPlanned = cumulativeActivityPlanned;
	}

	public String getActivityRaisedBy() {
		return activityRaisedBy;
	}

	public void setActivityRaisedBy(String activityRaisedBy) {
		this.activityRaisedBy = activityRaisedBy;
	}

	public String getActivityOwner() {
		return activityOwner;
	}

	public void setActivityOwner(String activityOwner) {
		this.activityOwner = activityOwner;
	}

	public String getActivityAssignedTo() {
		return activityAssignedTo;
	}

	public void setActivityAssignedTo(String activityAssignedTo) {
		this.activityAssignedTo = activityAssignedTo;
	}
	
	public String getActivityReviewer1() {
		return activityReviewer1;
	}

	public void setActivityReviewer1(String activityReviewer1) {
		this.activityReviewer1 = activityReviewer1;
	}

	public String getActivityReviewer2() {
		return activityReviewer2;
	}

	public void setActivityReviewer2(String activityReviewer2) {
		this.activityReviewer2 = activityReviewer2;
	}

	public String getActivityReviewer3() {
		return activityReviewer3;
	}

	public void setActivityReviewer3(String activityReviewer3) {
		this.activityReviewer3 = activityReviewer3;
	}

	public String getActivityReviewer4() {
		return activityReviewer4;
	}

	public void setActivityReviewer4(String activityReviewer4) {
		this.activityReviewer4 = activityReviewer4;
	}

	public String getActivityReviewer5() {
		return activityReviewer5;
	}

	public void setActivityReviewer5(String activityReviewer5) {
		this.activityReviewer5 = activityReviewer5;
	}

	public String getActivityParent() {
		return activityParent;
	}

	public void setActivityParent(String activityParent) {
		this.activityParent = activityParent;
	}

	public Float getL0ResourceCount() {
		return l0ResourceCount;
	}

	public Float getActivityExecutionEffort() {
		return activityExecutionEffort;
	}

	public void setActivityExecutionEffort(Float activityExecutionEffort) {
		this.activityExecutionEffort = activityExecutionEffort;
	}

	public Float getActivityReviewEffort1() {
		return activityReviewEffort1;
	}

	public void setActivityReviewEffort1(Float activityReviewEffort1) {
		this.activityReviewEffort1 = activityReviewEffort1;
	}

	public Float getActivityReviewEffort2() {
		return activityReviewEffort2;
	}

	public void setActivityReviewEffort2(Float activityReviewEffort2) {
		this.activityReviewEffort2 = activityReviewEffort2;
	}

	public Float getActivityReviewEffort3() {
		return activityReviewEffort3;
	}

	public void setActivityReviewEffort3(Float activityReviewEffort3) {
		this.activityReviewEffort3 = activityReviewEffort3;
	}

	public Float getActivityReviewEffort4() {
		return activityReviewEffort4;
	}

	public void setActivityReviewEffort4(Float activityReviewEffort4) {
		this.activityReviewEffort4 = activityReviewEffort4;
	}

	public Float getActivityReviewEffort5() {
		return activityReviewEffort5;
	}

	public void setActivityReviewEffort5(Float activityReviewEffort5) {
		this.activityReviewEffort5 = activityReviewEffort5;
	}

	public Float getActivityReworkEffort() {
		return activityReworkEffort;
	}

	public void setActivityReworkEffort(Float activityReworkEffort) {
		this.activityReworkEffort = activityReworkEffort;
	}

	public void setL0ResourceCount(Float l0ResourceCount) {
		this.l0ResourceCount = l0ResourceCount;
	}

	public Float getL1ResourceCount() {
		return l1ResourceCount;
	}

	public void setL1ResourceCount(Float l1ResourceCount) {
		this.l1ResourceCount = l1ResourceCount;
	}

	public Float getL2ResourceCount() {
		return l2ResourceCount;
	}

	public void setL2ResourceCount(Float l2ResourceCount) {
		this.l2ResourceCount = l2ResourceCount;
	}

	public Float getL3ResourceCount() {
		return l3ResourceCount;
	}

	public void setL3ResourceCount(Float l3ResourceCount) {
		this.l3ResourceCount = l3ResourceCount;
	}

	public Float getL4ResourceCount() {
		return l4ResourceCount;
	}

	public void setL4ResourceCount(Float l4ResourceCount) {
		this.l4ResourceCount = l4ResourceCount;
	}

	public Float getL5ResourceCount() {
		return l5ResourceCount;
	}

	public void setL5ResourceCount(Float l5ResourceCount) {
		this.l5ResourceCount = l5ResourceCount;
	}

	public Float getL6ResourceCount() {
		return l6ResourceCount;
	}

	public void setL6ResourceCount(Float l6ResourceCount) {
		this.l6ResourceCount = l6ResourceCount;
	}

	public Float getL7ResourceCount() {
		return l7ResourceCount;
	}

	public void setL7ResourceCount(Float l7ResourceCount) {
		this.l7ResourceCount = l7ResourceCount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Float geteVa() {
		return eVa;
	}

	public void seteVa(Float eVa) {
		this.eVa = eVa;
	}

	public Float geteVb() {
		return eVb;
	}

	public void seteVb(Float eVb) {
		this.eVb = eVb;
	}

	public Float getsVa() {
		return sVa;
	}

	public void setsVa(Float sVa) {
		this.sVa = sVa;
	}

	public Float getsVb() {
		return sVb;
	}

	public void setsVb(Float sVb) {
		this.sVb = sVb;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
